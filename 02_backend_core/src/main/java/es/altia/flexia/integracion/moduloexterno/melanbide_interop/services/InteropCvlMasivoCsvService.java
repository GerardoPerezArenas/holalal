package es.altia.flexia.integracion.moduloexterno.melanbide_interop.services;

import es.altia.flexia.integracion.moduloexterno.melanbide_interop.dao.InteropCvlMasivoNifDAO;
import es.altia.flexia.integracion.moduloexterno.melanbide_interop.dao.MeLanbideInteropVidaLaboralDAO;
import es.altia.flexia.integracion.moduloexterno.melanbide_interop.util.ConfigurationParameter;
import es.altia.flexia.integracion.moduloexterno.melanbide_interop.util.ConstantesMeLanbideInterop;
import es.altia.flexia.integracion.moduloexterno.melanbide_interop.util.MeLanbideInteropMappingUtils;
import es.altia.flexia.integracion.moduloexterno.melanbide_interop.vo.InteropCvlMasivoNifVO;
import es.altia.flexia.integracion.moduloexterno.melanbide_interop.vo.InteropCvlMasivoResultadoVO;
import es.altia.flexia.integracion.moduloexterno.melanbide_interop.vo.RegistroVidaLaboralVO;
import es.altia.flexia.integracion.moduloexterno.melanbide_interop.vo.cvl.Persona;
import es.altia.flexia.integracion.moduloexterno.melanbide_interop.ws.client.vidalaboralws.clientws.ClientWSVidaLaboral;
import es.altia.flexia.integracion.moduloexterno.melanbide_interop.ws.client.vidalaboralws.response.Response;
import java.io.ByteArrayInputStream;
import java.io.BufferedReader;
import java.io.Reader;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.regex.Pattern;
import org.apache.commons.codec.binary.Base64;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

/**
 * Servicio batch para procesar un CSV exportado de Excel con NIFs.
 */
public class InteropCvlMasivoCsvService {

    private static final Logger log = LogManager.getLogger(InteropCvlMasivoCsvService.class);
    private static final String SEPARADOR_CSV = ";";
    private static final String PREFIJO_EXP_TECNICO = "CVL_MASIVO";
    private static final String TIPO_DOC_NIF = "NIF";
    private static final String TIPO_DOC_DNI = "DNI";
    private static final String TIPO_DOC_NIE = "NIE";
    private static final String TIPO_DOC_CIF = "CIF";
    private static final Pattern BASE64_PATTERN = Pattern.compile("^[A-Za-z0-9+/=]+$");

    public InteropCvlMasivoResultadoVO procesarEntrada(final String listaDocsMasivo,
            final String excelBase64, final String fechaDesdeCVL, final String fechaHastaCVL,
            final int codOrganizacion, final String numExpediente,
            final String fkWSSolicitado, final String usuario,
            final Connection con) throws Exception {

        final String csvEntrada = obtenerCsvDesdeEntrada(listaDocsMasivo, excelBase64);
        return procesarCsv(new StringReader(csvEntrada), fechaDesdeCVL, fechaHastaCVL,
                codOrganizacion, numExpediente, fkWSSolicitado, usuario, con);
    }

    public InteropCvlMasivoResultadoVO procesarCsv(final Reader csvReader,
            final String fechaDesdeCVL, final String fechaHastaCVL,
            final int codOrganizacion, final String numExpediente,
            final String fkWSSolicitado, final String usuario,
            final Connection con) throws Exception {
        final InteropCvlMasivoResultadoVO resumen = new InteropCvlMasivoResultadoVO();
        final String numExpedienteTrabajo = (numExpediente != null && numExpediente.trim().length() > 0)
                ? numExpediente.trim() : generarNumExpedienteTecnico(con);
        resumen.setNumExpedienteContexto(numExpedienteTrabajo);

        try (final BufferedReader br = new BufferedReader(csvReader)) {

        String linea = null;
        int numLinea = 0;
        while ((linea = br.readLine()) != null) {
            numLinea++;

            if (linea == null || linea.trim().length() == 0) {
                continue;
            }

            if (numLinea == 1 && linea.toUpperCase().indexOf("NIF") >= 0) {
                // Cabecera CSV
                continue;
            }

            resumen.setTotalLeidos(resumen.getTotalLeidos() + 1);

            final String[] columnas = normalizarSeparador(linea).split(SEPARADOR_CSV);
            final String nif = columnas.length > 0 ? columnas[0].trim().toUpperCase() : "";
            final String tipoDoc = columnas.length > 1 ? columnas[1].trim().toUpperCase() : TIPO_DOC_NIF;

            if (!esDocumentoValido(nif)) {
                resumen.setTotalErrores(resumen.getTotalErrores() + 1);
                resumen.addError("Linea " + numLinea + ": NIF vacio o invalido -> " + nif);
                registrarAuditoriaError(nif, tipoDoc, usuario,
                        "VALIDACION", "NIF vacio o invalido en CSV", con);
                continue;
            }

            try {
                final Persona p = new Persona();
                p.setNumDocumento(nif);
                p.setTipoDocumento(tipoDoc);

                final Response response = ClientWSVidaLaboral.getVidaLaboral(p,
                        fechaDesdeCVL, fechaHastaCVL, codOrganizacion, numExpedienteTrabajo,
                        fkWSSolicitado);

                final String codRespuesta = response != null ? response.getCodRespuesta() : "WS_NULL";
                final String descRespuesta = response != null ? response.getDescRespuesta() : "Respuesta nula del WS CVL";
                final String payloadResumen = construirPayloadResumen(response);

                final InteropCvlMasivoNifVO registro = new InteropCvlMasivoNifVO(
                        null,
                        new Timestamp(System.currentTimeMillis()),
                        nif,
                        tipoDoc,
                        codRespuesta,
                        descRespuesta,
                        payloadResumen,
                        usuario);

                InteropCvlMasivoNifDAO.getInstance().insertarRegistro(registro, con);

                resumen.setTotalProcesados(resumen.getTotalProcesados() + 1);
                if ("0000".equals(codRespuesta)) {
                    persistirEnInteropVidaLaboral(response, p, fechaDesdeCVL, fechaHastaCVL, numExpedienteTrabajo, con);
                    resumen.setTotalCorrectos(resumen.getTotalCorrectos() + 1);
                } else {
                    resumen.setTotalErrores(resumen.getTotalErrores() + 1);
                    resumen.addError("Linea " + numLinea + ": " + nif + " -> " + codRespuesta + " " + descRespuesta);
                }
            } catch (Exception ex) {
                log.error("Error procesando linea " + numLinea + " para NIF " + nif, ex);
                resumen.setTotalErrores(resumen.getTotalErrores() + 1);
                resumen.addError("Linea " + numLinea + ": error tecnico para " + nif + " -> " + ex.getMessage());
                registrarAuditoriaError(nif, tipoDoc, usuario, "ERROR", ex.getMessage(), con);
            }
        }
        } // end try-with-resources BufferedReader

        return resumen;
    }

    private void registrarAuditoriaError(final String nif, final String tipoDoc,
            final String usuario, final String codRespuesta,
            final String descRespuesta, final Connection con) {
        try {
            final InteropCvlMasivoNifVO registro = new InteropCvlMasivoNifVO(
                    null,
                    new Timestamp(System.currentTimeMillis()),
                    nif,
                    tipoDoc,
                    codRespuesta,
                    descRespuesta,
                    descRespuesta,
                    usuario);
            InteropCvlMasivoNifDAO.getInstance().insertarRegistro(registro, con);
        } catch (Exception ex) {
            log.error("No se pudo registrar auditoria de error para NIF " + nif, ex);
        }
    }

    private String construirPayloadResumen(final Response response) {
        if (response == null) {
            return "WS_NULL";
        }

        final StringBuilder sb = new StringBuilder();
        sb.append(response.getCodigoEstado() != null ? response.getCodigoEstado() : "");
        sb.append("|");
        sb.append(response.getTextoEstado() != null ? response.getTextoEstado() : "");

        if (sb.length() > 2000) {
            return sb.substring(0, 2000);
        }
        return sb.toString();
    }

    private boolean esDocumentoValido(final String documento) {
        return documento != null && documento.trim().length() >= 8;
    }

    private String normalizarSeparador(final String linea) {
        if (linea.indexOf(';') >= 0) {
            return linea;
        }
        return linea.replace(',', ';');
    }

    private String obtenerCsvDesdeEntrada(final String listaDocsMasivo, final String excelBase64) throws Exception {
        if (listaDocsMasivo != null && listaDocsMasivo.trim().length() > 0) {
            return listaDocsMasivo;
        }
        if (excelBase64 != null && excelBase64.trim().length() > 0) {
            return convertirExcelEnCsv(excelBase64);
        }
        return "";
    }

    private String convertirExcelEnCsv(final String excelBase64) throws Exception {
        Workbook libro = null;
        try {
            final String excelBase64Normalizado = excelBase64 != null ? excelBase64.replaceAll("\\s+", "") : "";
            if (excelBase64Normalizado.length() == 0 || !BASE64_PATTERN.matcher(excelBase64Normalizado).matches()) {
                throw new IllegalArgumentException("Contenido Excel en base64 no válido.");
            }
            final byte[] bytesExcel = Base64.decodeBase64(excelBase64Normalizado.getBytes(StandardCharsets.UTF_8));
            libro = WorkbookFactory.create(new ByteArrayInputStream(bytesExcel));
            if (libro == null || libro.getNumberOfSheets() <= 0) {
                return "";
            }

            final Sheet hoja = libro.getSheetAt(0);
            if (hoja == null) {
                return "";
            }

            final DataFormatter dataFormatter = new DataFormatter();
            final StringBuilder csv = new StringBuilder();
            final int primeraFila = hoja.getFirstRowNum();
            final int ultimaFila = hoja.getLastRowNum();

            for (int i = primeraFila; i <= ultimaFila; i++) {
                final Row fila = hoja.getRow(i);
                if (fila == null) {
                    continue;
                }
                final Cell celdaCol0 = fila.getCell(0);
                final Cell celdaCol1 = fila.getCell(1);
                final String col0 = celdaCol0 != null ? dataFormatter.formatCellValue(celdaCol0) : "";
                final String col1 = celdaCol1 != null ? dataFormatter.formatCellValue(celdaCol1) : "";
                String docNormalizado = col0 != null ? col0.trim() : "";
                String tipoDocNormalizado = col1 != null ? col1.trim() : "";

                if (esTipoDocumento(docNormalizado) && tipoDocNormalizado.length() > 0) {
                    final String tempDocumento = tipoDocNormalizado;
                    tipoDocNormalizado = docNormalizado;
                    docNormalizado = tempDocumento;
                }
                if (docNormalizado.length() == 0 && tipoDocNormalizado.length() == 0) {
                    continue;
                }
                if (csv.length() > 0) {
                    csv.append('\n');
                }
                csv.append(docNormalizado);
                csv.append(SEPARADOR_CSV);
                csv.append(tipoDocNormalizado.length() > 0 ? tipoDocNormalizado : TIPO_DOC_NIF);
            }
            return csv.toString();
        } catch (Exception ex) {
            log.error("Error convirtiendo Excel CVL masivo a CSV", ex);
            throw new Exception("No se ha podido convertir el Excel a lista de documentos: " + ex.getMessage(), ex);
        } finally {
            if (libro != null) {
                try {
                    libro.close();
                } catch (Exception e) {
                    log.error("Error cerrando libro Excel CVL masivo", e);
                }
            }
        }
    }

    private boolean esTipoDocumento(final String valor) {
        if (valor == null) {
            return false;
        }
        final String tipo = valor.trim().toUpperCase();
        return TIPO_DOC_DNI.equals(tipo) || TIPO_DOC_NIF.equals(tipo)
                || TIPO_DOC_NIE.equals(tipo) || TIPO_DOC_CIF.equals(tipo);
    }

    private String generarNumExpedienteTecnico(final Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        try {
            final int year = Calendar.getInstance().get(Calendar.YEAR);
            final String seqName = ConfigurationParameter.getParameter(
                    ConstantesMeLanbideInterop.SEQ_VIDALABORAL,
                    ConstantesMeLanbideInterop.FICHERO_PROPIEDADES);
            st = con.createStatement();
            rs = st.executeQuery("SELECT " + seqName + ".NEXTVAL FROM DUAL");
            int siguiente = 1;
            if (rs.next()) {
                siguiente = rs.getInt(1);
            }

            final String secuencia6 = String.format("%06d", siguiente);
            return PREFIJO_EXP_TECNICO + "/" + year + "/" + secuencia6;
        } catch (Exception ex) {
            log.error("Error generando expediente tecnico CVL masivo", ex);
            throw new Exception(ex);
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (st != null) {
                st.close();
            }
        }
    }

    /**
     * Inserta en INTEROP_VIDALABORAL como el batch actual de expediente.
     */
    private void persistirEnInteropVidaLaboral(final Response response, final Persona persona,
            final String fechaDesdeCVL, final String fechaHastaCVL, final String numExpediente,
            final Connection con) throws Exception {

        final java.util.List<RegistroVidaLaboralVO> registros
                = MeLanbideInteropMappingUtils.mapListaSituacionToListaVidaLaboral(
                        response.getIdentidad(), persona, fechaDesdeCVL, fechaHastaCVL);

        if (registros == null || registros.isEmpty()) {
            return;
        }

        for (int i = 0; i < registros.size(); i++) {
            final RegistroVidaLaboralVO registro = registros.get(i);
            MeLanbideInteropVidaLaboralDAO.getInstance().insertarRegistroVidaLaboral(registro, numExpediente, con);
        }
    }
}

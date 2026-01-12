/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.melanbide74;

import com.google.gson.Gson;
import es.altia.agora.business.util.GeneralValueObject;
import es.altia.agora.technical.ConstantesDatos;
import es.altia.common.exception.TechnicalException;
import es.altia.flexia.integracion.moduloexterno.melanbide74.manager.MeLanbide74Manager;
import es.altia.flexia.integracion.moduloexterno.melanbide74.util.ConfigurationParameter;
import es.altia.flexia.integracion.moduloexterno.melanbide74.util.ConstantesMeLanbide74;
import es.altia.flexia.integracion.moduloexterno.melanbide74.util.Utilidades74;
import es.altia.flexia.integracion.moduloexterno.melanbide74.vo.ParamsAltaCartaPagoRenuncia;
import es.altia.flexia.integracion.moduloexterno.plugin.ModuloIntegracionExterno;
import es.altia.flexia.integracion.moduloexterno.plugin.ModuloIntegracionExternoCampoSupFactoria;
import es.altia.flexia.integracion.moduloexterno.plugin.camposuplementario.IModuloIntegracionExternoCamposFlexia;
import es.altia.flexia.integracion.moduloexterno.plugin.camposuplementario.ModuloIntegracionExternoCamposFlexia;
import es.altia.flexia.integracion.moduloexterno.plugin.persistence.vo.CampoSuplementarioModuloIntegracionVO;
import es.altia.flexia.integracion.moduloexterno.plugin.persistence.vo.DomicilioInteresadoModuloIntegracionVO;
import es.altia.flexia.integracion.moduloexterno.plugin.persistence.vo.InteresadoExpedienteModuloIntegracionVO;
import es.altia.flexia.integracion.moduloexterno.plugin.persistence.vo.SalidaIntegracionVO;
import es.altia.technical.PortableContext;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.altia.util.conexion.BDException;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.nio.charset.UnsupportedCharsetException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;
import java.util.ResourceBundle;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.ParseException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author Kepa
 */
public class MELANBIDE74 extends ModuloIntegracionExterno {

    //Logger
    private static final Logger log = LogManager.getLogger(MELANBIDE74.class);
    private final Utilidades74 util74 = new Utilidades74();
    //Constantes de la clase
    private final String BARRA = ConstantesMeLanbide74.BARRA;
    private final String MODULO_INTEGRACION = ConstantesMeLanbide74.MODULO_INTEGRACION;

    // codigos de error
    private static final String ERROR_LLAMADA_SW = "1";
    private static final String ERROR_RESPUESTA_SW = "2";
    private static final String ERROR_CODIGO_ACCESO = "3";
    private static final String ERROR_404 = "4";
    private static final String ERROR_CODIGO_VIA = "5";
    private static final String ERROR_ANIO_CONVOCATORIA = "6";
    private static final String ERROR_COD_SUBVENCION = "7";
    private static final String ERROR_CP = "8";
    private static final String ERROR_IMPORTE_NEGATIVO = "9";
    private static final String ERROR_DOMICILIO = "10";
    private static final String ERROR_CONVIRTIENDO_IMPORTE_DEUDA = "11";
    private static final String ERROR_GRABAR_CAMPOS_SUPLEMENTARIOS = "12";
    private static final String ERROR_BORRANDO_CARTA = "13";
    private static final String ERROR_GRABANDO_CARTA = "14";
    private static final String ERROR_RECUPERANDO_FECHA_RENUNCIA = "15";
    private static final String ERROR_RECUPERANDO_FECHA_PAGO = "16";
    private static final String ERROR_RECUPERANDO_IMPORTE = "17";

    /**
     * Prepara la pantalla para el alta de datos y la rellena con los datos en
     * caso de que existan ya.
     *
     * @param codOrganizacion
     * @param codTramite
     * @param ocurrenciaTramite
     * @param numExpediente
     * @param request
     * @param response
     * @return String con el forward a la JSP
     */
    public String cargarPestanaRenuncia(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) throws SQLException {
        log.info("===========>  ENTRA EN cargarPestanaRenuncia " + numExpediente + " - Tr: " + codTramite + "_" + ocurrenciaTramite);
        String ejercicio = "";
        String codProcedimiento = "";
        if (numExpediente != null && !"".equals(numExpediente)) {
            String[] datos = numExpediente.split(BARRA);
            ejercicio = datos[0];
            codProcedimiento = datos[1];
        }
        String cTramite = ConfigurationParameter.getParameter(codProcedimiento + BARRA + ConstantesMeLanbide74.TRAM_INICIO_RENUNCIA, ConstantesMeLanbide74.FICHERO_PROPIEDADES);
        String ocuTramite = request.getParameter("ocurrenciaTramite");
        String nombreModulo = ConfigurationParameter.getParameter(ConstantesMeLanbide74.NOMBRE_MODULO, ConstantesMeLanbide74.FICHERO_PROPIEDADES);
        String codIdDeuda = ConfigurationParameter.getParameter(codOrganizacion + BARRA + MODULO_INTEGRACION + BARRA + nombreModulo + BARRA + ConstantesMeLanbide74.CAMPO_ID_DEUDA, ConstantesMeLanbide74.FICHERO_PROPIEDADES);
        String idDeuda = null;
        try {
            idDeuda = MeLanbide74Manager.getInstance().getValorCampoTextoTramite(codOrganizacion, codProcedimiento, numExpediente, ejercicio, Integer.parseInt(cTramite), Integer.parseInt(ocuTramite), codIdDeuda, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));

            log.debug("IdDeuda: " + idDeuda);
        } catch (BDException e) {
            log.error("error en cargarPestanaRenuncia ", e);
        } catch (NumberFormatException e) {
            log.error("error en cargarPestanaRenuncia ", e);
        } catch (Exception ex) {
            log.error("error en cargarPestanaRenuncia ", ex);
        }

        request.setAttribute("numExp", numExpediente);
        request.setAttribute("codigoProcedimiento", codProcedimiento);
        request.setAttribute("ejercicioExp", ejercicio);
        request.setAttribute("codigoTramite", cTramite);
        request.setAttribute("ocuTramite", ocuTramite);
        request.setAttribute("valorIdDeuda", idDeuda);
        log.debug("Carga la pestaña ==> /jsp/extension/melanbide74/melanbide74.jsp");
        return "/jsp/extension/melanbide74/melanbide74.jsp";
    }

    /**
     * Operacion que llama a la API de ZORKU altaCartaPagoRenunciaSubvenciones
     *
     * @param codOrganizacion
     * @param codTramite
     * @param ocurrenciaTramite
     * @param numExpediente
     * @param request
     * @param response
     * @return String con el codigo de la operación
     * @throws java.sql.SQLException
     * @throws es.altia.util.conexion.BDException
     */
    public String altaCartaPagoRenunciaNoRGI(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) throws SQLException, BDException {
        log.info("===========>  ENTRA EN altaCartaPagoRenunciaSubvenciones ( numExpediente = " + numExpediente + " )");
        String cTramite = request.getParameter("codTramite");
        String cOcurrencia = request.getParameter("ocurrenciaTramite");
        log.debug("  -- altaCartaPagoRenunciaSubvenciones --  cTramite: " + cTramite);
        log.debug("  -- altaCartaPagoRenunciaSubvenciones --  cOcurrencia: " + cOcurrencia);
        String ejercicioExp = "";
        String codProcedimiento = "";

        IModuloIntegracionExternoCamposFlexia gestorCampoSup = ModuloIntegracionExternoCampoSupFactoria.getInstance().getImplClass();

        MeLanbide74Manager manager = MeLanbide74Manager.getInstance();
        GeneralValueObject resultado = new GeneralValueObject();
        String codOperacion = "0";
        AdaptadorSQLBD adapt = null;

        if (numExpediente != null && !"".equals(numExpediente)) {
            String[] datos = numExpediente.split(BARRA);
            ejercicioExp = datos[0];
            codProcedimiento = datos[1];
        }//if(numExpediente!=null && !"".equals(numExpediente))

        // Creo el VO de resultado
        String idLiquidacion = "";
        String nombreCarta = null;

        String nombreModulo = ConfigurationParameter.getParameter(ConstantesMeLanbide74.NOMBRE_MODULO, ConstantesMeLanbide74.FICHERO_PROPIEDADES);
        String zorDeExpRei = null;
        String zorDeCodSubvencion = null;
        String zorLaEjercicio = "";
        Calendar fechaRequerimiento = null;
        String zorLiFechaRequerimiento = null;
        Calendar fechaRenuncia = null;
        String zorLiFechaRenuncia = null;
        Calendar fechaPagoSubv = null;
        String zorLiFechaPagoExpSubv = null;
        Calendar fechaLimitePago = null;
        String zorLiFechaLimitePago = null;
        BigDecimal zorLiImporteDeuda = null;
        BigDecimal intereses = new BigDecimal(BigInteger.ZERO);
        BigDecimal importeMasInts = new BigDecimal(BigInteger.ZERO);

        String zorTpCodigo = null;
        String zorEdCodigo = null;
        String codigoAreaSW = null; //  se utiliza para obtener codigoAreaAcceso
        int zorArCodigo = 0; //  se envia en areaAccesoVO
        String esDevolucion = null;

        // interesado
        String zorTerNumDocumento = null;
        String zorTerTipoDocumento = null;
        int tipoDocumentoINT = 0;
        String zorTerNombre = null;
        String zorTerApellido1 = null;
        String zorTerApellido2 = null;
        String zorTerTelefono = null;
        String zorTerEmail = null;

        // domicilio
        String zorDirCp = null;
        String codigoVia = null;
        String municipioId = null;
        String provinciaId = null;
        String zorDirDireccionCompleta = "";

        String codCampoFechaRenuncia = ConfigurationParameter.getParameter(codOrganizacion + BARRA + MODULO_INTEGRACION + BARRA + nombreModulo + BARRA + ConstantesMeLanbide74.CAMPO_FEC_ACEPTACION, ConstantesMeLanbide74.FICHERO_PROPIEDADES);
        String codCampoFechaPagoSubv = ConfigurationParameter.getParameter(codOrganizacion + BARRA + MODULO_INTEGRACION + BARRA + nombreModulo + BARRA + ConstantesMeLanbide74.CAMPO_FEC_PAGO_SUBV, ConstantesMeLanbide74.FICHERO_PROPIEDADES);
        String codCampoFechaLimitePago = ConfigurationParameter.getParameter(codOrganizacion + BARRA + MODULO_INTEGRACION + BARRA + nombreModulo + BARRA + ConstantesMeLanbide74.CAMPO_FEC_LIMITE_PAGO, ConstantesMeLanbide74.FICHERO_PROPIEDADES);
        String codCampoCodigoAreaSW = ConfigurationParameter.getParameter(codOrganizacion + BARRA + MODULO_INTEGRACION + BARRA + nombreModulo + BARRA + ConstantesMeLanbide74.CAMPO_CODIGO_AREA, ConstantesMeLanbide74.FICHERO_PROPIEDADES);
        String codCampoImporteDeuda = ConfigurationParameter.getParameter(codOrganizacion + BARRA + MODULO_INTEGRACION + BARRA + nombreModulo + BARRA + ConstantesMeLanbide74.CAMPO_IMPORTE_DEUDA, ConstantesMeLanbide74.FICHERO_PROPIEDADES);
        String codCampoIntereses = ConfigurationParameter.getParameter(codOrganizacion + BARRA + MODULO_INTEGRACION + BARRA + nombreModulo + BARRA + ConstantesMeLanbide74.CAMPO_INTERESES, ConstantesMeLanbide74.FICHERO_PROPIEDADES);
        String codCampoImporteIntereses = ConfigurationParameter.getParameter(codOrganizacion + BARRA + MODULO_INTEGRACION + BARRA + nombreModulo + BARRA + ConstantesMeLanbide74.CAMPO_IMPORTE_INTERESES, ConstantesMeLanbide74.FICHERO_PROPIEDADES);
        String codCampoDevolucion = ConfigurationParameter.getParameter(codOrganizacion + BARRA + MODULO_INTEGRACION + BARRA + nombreModulo + BARRA + ConstantesMeLanbide74.CAMPO_DEVOLUCION, ConstantesMeLanbide74.FICHERO_PROPIEDADES);
        String codCampoAnioConvocatoria = ConfigurationParameter.getParameter(codOrganizacion + BARRA + MODULO_INTEGRACION + BARRA + nombreModulo + BARRA + ConstantesMeLanbide74.CAMPO_ANIO_CONVO, ConstantesMeLanbide74.FICHERO_PROPIEDADES);
        log.info("  -- altaCartaPagoRenunciaSubvenciones --  Procedimiento " + codProcedimiento + " cod Tram Inicio Renuncia: " + cTramite);
        try {
            log.debug("  -- altaCartaPagoRenunciaSubvenciones --  adaptador");
            adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));

            int ocuTramite = Integer.parseInt(cOcurrencia);

            // datos conexion al SW DEUDA de ZORKU
            String urlAltaCartaRenuncia = ConfigurationParameter.getParameter(ConstantesMeLanbide74.URL_ALTA_CARTA_RENUNCIA, ConstantesMeLanbide74.FICHERO_PROPIEDADES);
            //Valores de usuario y contraseña para el servicio
            String zorUsUsuario = ConfigurationParameter.getParameter(ConstantesMeLanbide74.USUARIO, ConstantesMeLanbide74.FICHERO_PROPIEDADES);
            String zorUsPassword = ConfigurationParameter.getParameter(ConstantesMeLanbide74.CONTRASENA, ConstantesMeLanbide74.FICHERO_PROPIEDADES);
            String area = ConfigurationParameter.getParameter(ConstantesMeLanbide74.AREA, ConstantesMeLanbide74.FICHERO_PROPIEDADES);

            log.info("  -- altaCartaPagoRenunciaSubvenciones --   Recojo suplementarios - FECHA SOLICITUD RENUNCIA - FECHA DE PAGO DE LA SUBVENCIÓN - IMPORTE DEUDA - CODIGO AREA");

            SalidaIntegracionVO campoFechRenuncia = gestorCampoSup.getCampoSuplementarioTramite(Integer.toString(codOrganizacion), ejercicioExp, numExpediente, codProcedimiento,
                    Integer.parseInt(cTramite), ocuTramite, codCampoFechaRenuncia, ModuloIntegracionExternoCamposFlexia.CAMPO_FECHA);

            SalidaIntegracionVO campoFechPagoSubv = gestorCampoSup.getCampoSuplementarioTramite(Integer.toString(codOrganizacion), ejercicioExp, numExpediente, codProcedimiento,
                    Integer.parseInt(cTramite), ocuTramite, codCampoFechaPagoSubv, ModuloIntegracionExternoCamposFlexia.CAMPO_FECHA);

            SalidaIntegracionVO campoCodigoAreaSW = gestorCampoSup.getCampoSuplementarioTramite(Integer.toString(codOrganizacion), ejercicioExp, numExpediente, codProcedimiento,
                    Integer.parseInt(cTramite), ocuTramite, codCampoCodigoAreaSW, ModuloIntegracionExternoCamposFlexia.CAMPO_DESPLEGABLE);

            SalidaIntegracionVO campoImporteDeuda = gestorCampoSup.getCampoSuplementarioTramite(Integer.toString(codOrganizacion), ejercicioExp, numExpediente, codProcedimiento,
                    Integer.parseInt(cTramite), ocuTramite, codCampoImporteDeuda, ModuloIntegracionExternoCamposFlexia.CAMPO_NUMERICO);

            SalidaIntegracionVO campoDevolucion = gestorCampoSup.getCampoSuplementarioTramite(Integer.toString(codOrganizacion), ejercicioExp, numExpediente, codProcedimiento,
                    Integer.parseInt(cTramite), ocuTramite, codCampoDevolucion, ModuloIntegracionExternoCamposFlexia.CAMPO_DESPLEGABLE);

            SalidaIntegracionVO campoAnioConvocatoria = gestorCampoSup.getCampoSuplementarioTramite(Integer.toString(codOrganizacion), ejercicioExp, numExpediente, codProcedimiento,
                    Integer.parseInt(cTramite), ocuTramite, codCampoAnioConvocatoria, ModuloIntegracionExternoCamposFlexia.CAMPO_DESPLEGABLE);

            log.debug("  -- altaCartaPagoRenunciaSubvenciones --   Paso suplementarios a variables");

            if (campoFechRenuncia.getStatus() == 0) {
                fechaRenuncia = campoFechRenuncia.getCampoSuplementario().getValorFecha();
            } else {
                log.error("  -- altaCartaPagoRenunciaSubvenciones --   ERROR  FECHA RENUNCIA");
                resultado.setAtributo("codigoOperacion", ERROR_RECUPERANDO_FECHA_RENUNCIA);
                util74.retornarJSON(new Gson().toJson(resultado), response);
                return ERROR_RECUPERANDO_FECHA_RENUNCIA;
            }

            if (campoFechPagoSubv.getStatus() == 0) {
                fechaPagoSubv = campoFechPagoSubv.getCampoSuplementario().getValorFecha();
            } else {
                log.error("  -- altaCartaPagoRenunciaSubvenciones --   ERROR  FECHA PAGO subv");
                resultado.setAtributo("codigoOperacion", ERROR_RECUPERANDO_FECHA_PAGO);
                util74.retornarJSON(new Gson().toJson(resultado), response);
                return ERROR_RECUPERANDO_FECHA_PAGO;
            }

            if ((campoImporteDeuda.getStatus() == 0)) {
                try {
                    zorLiImporteDeuda = new BigDecimal(campoImporteDeuda.getCampoSuplementario().getValorNumero());
                    log.debug("Importe "+zorLiImporteDeuda);
                    if (zorLiImporteDeuda.compareTo(BigDecimal.ZERO) < 0) {
                        log.error("  -- altaCartaPagoRenunciaSubvenciones --  Importe negativo: " + zorLiImporteDeuda);
                        resultado.setAtributo("codigoOperacion", ERROR_IMPORTE_NEGATIVO);
                        util74.retornarJSON(new Gson().toJson(resultado), response);
                        return ERROR_IMPORTE_NEGATIVO;
                    }
                } catch (Exception e) {
                    log.error("  -- altaCartaPagoRenunciaSubvenciones --  Salta excepcion al convertir a numero el importe de la deuda");
                    log.error("altaCartaPagoRenunciaSubvenciones.Devolvemos: " + ERROR_CONVIRTIENDO_IMPORTE_DEUDA);
                    resultado.setAtributo("codigoOperacion", ERROR_CONVIRTIENDO_IMPORTE_DEUDA);
                    util74.retornarJSON(new Gson().toJson(resultado), response);
                    return ERROR_CONVIRTIENDO_IMPORTE_DEUDA;
                }
            } else {
                log.error("  -- altaCartaPagoRenunciaSubvenciones --   ERROR IMPORTE DEUDA");
                resultado.setAtributo("codigoOperacion", ERROR_RECUPERANDO_IMPORTE);
                util74.retornarJSON(new Gson().toJson(resultado), response);
                return ERROR_RECUPERANDO_IMPORTE;
            }

            // codigo subv EIKA
            // recojo el valor del anio de la subvencion
            if (campoAnioConvocatoria.getStatus() == 0) {
                zorLaEjercicio = campoAnioConvocatoria.getCampoSuplementario().getValorDesplegable();
                log.debug("Año Convocatoria ayuda: " + zorLaEjercicio);
            } else {
                log.error("---- altaCartaPagoRenunciaSubvenciones - No existe el año de subvención");
                resultado.setAtributo("codigoOperacion", ERROR_ANIO_CONVOCATORIA);
                util74.retornarJSON(new Gson().toJson(resultado), response);
                return ERROR_ANIO_CONVOCATORIA;
            }
            // con el ANIO y el procedimiento del expediente inicial obtengo el codigo de la subvencion
            zorDeCodSubvencion = manager.getCodigoSubvencionEika(zorLaEjercicio, codProcedimiento, adapt);
            if (zorDeCodSubvencion == null || zorDeCodSubvencion.isEmpty()) {
                log.error("---- altaCartaPagoRenunciaSubvenciones - No existe el código de subvención");
                resultado.setAtributo("codigoOperacion", ERROR_COD_SUBVENCION);
                util74.retornarJSON(new Gson().toJson(resultado), response);
                return ERROR_COD_SUBVENCION;
            }

            // codigo area acceso
            if (campoCodigoAreaSW.getStatus() == 0) {
                codigoAreaSW = campoCodigoAreaSW.getCampoSuplementario().getValorDesplegable();
            } else {
                log.error("  -- altaCartaPagoRenunciaSubvenciones --  ERROR recuperando el código de Acceso del Área REINT: ");
                resultado.setAtributo("codigoOperacion", ERROR_CODIGO_ACCESO);
                util74.retornarJSON(new Gson().toJson(resultado), response);
                return ERROR_CODIGO_ACCESO;
            }

            try {
                int codArea = Integer.parseInt(codigoAreaSW);
                zorArCodigo = Utilidades74.dameAcCodZorku(codArea);
            } catch (NumberFormatException e) {
                log.error("  -- altaCartaPagoRenunciaSubvenciones --  ERROR recuperando el código de Acceso del Área REINT: " + e);
                resultado.setAtributo("codigoOperacion", ERROR_CODIGO_ACCESO);
                util74.retornarJSON(new Gson().toJson(resultado), response);
                return ERROR_CODIGO_ACCESO;
            }

            // es Devolucion
            if (campoDevolucion.getStatus() == 0) {
                esDevolucion = campoDevolucion.getCampoSuplementario().getValorDesplegable();
            }
            //  constantes
            fechaRequerimiento = Calendar.getInstance();
            fechaLimitePago = Calendar.getInstance();
            fechaLimitePago.add(Calendar.MONTH, 1);
            zorDeExpRei = numExpediente + BARRA + "R";
            zorTpCodigo = ConfigurationParameter.getParameter(ConstantesMeLanbide74.TIPO_PAGO_DEUDA, ConstantesMeLanbide74.FICHERO_PROPIEDADES);
            zorEdCodigo = ConfigurationParameter.getParameter(ConstantesMeLanbide74.ESTADO_DEUDA_PENDIENTE, ConstantesMeLanbide74.FICHERO_PROPIEDADES);

//Datos del interesado
            SalidaIntegracionVO salida = gestorCampoSup.getExpediente(String.valueOf(codOrganizacion), numExpediente, codProcedimiento, ejercicioExp);
            InteresadoExpedienteModuloIntegracionVO interesado = new InteresadoExpedienteModuloIntegracionVO();

            if (salida != null) {
                if (salida.getExpediente() != null) {
                    if ((salida.getExpediente().getInteresados() != null) && (!salida.getExpediente().getInteresados().isEmpty())) {
                        int numInteresados = salida.getExpediente().getInteresados().size();
                        for (int x = 0; x < numInteresados; x++) {
                            interesado = salida.getExpediente().getInteresados().get(x);
                            int rolInteresado = interesado.getCodigoRol();
                            if (rolInteresado == 1) {
                                break;
                            }
                        }
                        log.debug("  -- altaCartaPagoRenunciaSubvenciones --  He cogido el interesado: " + interesado.getNombreCompleto());

                        //   interesado = salida.getExpediente().getInteresados().get(0);
                        if (interesado.getNombre() != null) {
                            zorTerNombre = interesado.getNombre();
                        }
                        if (interesado.getApellido1() != null) {
                            zorTerApellido1 = interesado.getApellido1();
                        }
                        if (interesado.getApellido2() != null) {
                            zorTerApellido2 = interesado.getApellido2();
                        }
                        if (interesado.getNumeroTelefonoFax() != null) {
                            zorTerTelefono = interesado.getNumeroTelefonoFax();
                            zorTerTelefono = Utilidades74.recuperaCifrasTelefono(interesado.getNumeroTelefonoFax());
                        }
                        if (interesado.getEmail() != null) {
                            zorTerEmail = interesado.getEmail();
                        }
                        if (interesado.getDocumento() != null) {
                            zorTerNumDocumento = interesado.getDocumento();
                        }
                        tipoDocumentoINT = interesado.getTipoDocumento();
                        zorTerTipoDocumento = Utilidades74.traducirTipoDocZorku(String.valueOf(tipoDocumentoINT));

                    } // interesado

                    // domicilio interesado
                    if ((interesado.getDomicilios() != null) && (!interesado.getDomicilios().isEmpty())) {
                        int codDomicilioExpediente = interesado.getCodDomicilioExpediente();
                        log.debug("  -- altaCartaPagoRenunciaSubvenciones --  CodDomicilio Expediente " + codDomicilioExpediente);

                        DomicilioInteresadoModuloIntegracionVO domicilio = new DomicilioInteresadoModuloIntegracionVO();
                        for (int i = 0; i < interesado.getDomicilios().size(); i++) {
                            domicilio = interesado.getDomicilios().get(i);
                            String codDom = domicilio.getIdDomicilio();
//                            log.debug("  -- altaCartaPagoRenunciaSubvenciones --  codDomicilio " + i + " Interesado " + codDom);
                            if ((Integer.toString(codDomicilioExpediente)).equals(codDom)) {
                                break;
                            } else {
                                domicilio = interesado.getDomicilios().get(0);
                            }
                        }
                        log.debug("  -- altaCartaPagoRenunciaSubvenciones --  He recogido el codDomicilio  " + domicilio.getIdDomicilio());

                        provinciaId = domicilio.getIdProvincia();
                        municipioId = domicilio.getIdMunicipio();

                        try {
                            codigoVia = manager.dameCodigoTipoVia(codDomicilioExpediente, adapt);
                            if (codigoVia != null && !"".equals(codigoVia)) {
                                if (codigoVia.length() > 2) {
                                    codigoVia = codigoVia.substring(0, 2);
                                }
                                if (!manager.coincideCodigo(codigoVia, adapt)) {
                                    log.debug("  -- altaCartaPagoRenunciaSubvenciones --  No coincide codigo, paso 'CL'");
                                    codigoVia = "CL";
                                }
                            } else {
                                codigoVia = "CL";
                            }
                        } catch (Exception e) {
                            log.error("  -- altaCartaPagoRenunciaSubvenciones --  Devolvemos: " + ERROR_CODIGO_VIA);
                            resultado.setAtributo("codigoOperacion", ERROR_CODIGO_VIA);
                            util74.retornarJSON(new Gson().toJson(resultado), response);
                            return ERROR_CODIGO_VIA;
                        }

                        zorDirDireccionCompleta = codigoVia + " " + domicilio.getDescripcionVia() + " " + (domicilio.getNumDesde() != null ? domicilio.getNumDesde() + " " : "") + (domicilio.getPortal() != null && !domicilio.getPortal().isEmpty() ? domicilio.getPortal() + " " : "") + (domicilio.getBloque() != null && !domicilio.getBloque().isEmpty() ? domicilio.getBloque() + " " : "") + (domicilio.getEscalera() != null && !domicilio.getEscalera().isEmpty() ? domicilio.getEscalera() + " " : "") + (domicilio.getPlanta() != null && !domicilio.getPlanta().isEmpty() ? domicilio.getPlanta() + " " : "") + (domicilio.getPuerta() != null && !domicilio.getPuerta().isEmpty() ? domicilio.getPuerta() : "");

                        zorDirCp = domicilio.getCodigoPostal();
                    } // domicilio interesado
                } // salida.getExpediente() != null
            } // salida != null

// control de datos obligatorios
            log.debug("  -- altaCartaPagoRenunciaSubvenciones --  Control datos ");

            if ((zorArCodigo == 0)) {
                log.error("  -- altaCartaPagoRenunciaSubvenciones --   ERROR  CODIGO AREA ACCESO");
                resultado.setAtributo("codigoOperacion", ERROR_CODIGO_ACCESO);
                util74.retornarJSON(new Gson().toJson(resultado), response);
                return ERROR_CODIGO_ACCESO;
            }
            if ((("".equals(zorDirDireccionCompleta)) || (zorDirDireccionCompleta == null))) {
                log.error("--- ERROR NOMBRE CALLE ");
                resultado.setAtributo("codigoOperacion", ERROR_DOMICILIO);
                util74.retornarJSON(new Gson().toJson(resultado), response);
                return ERROR_DOMICILIO;
            }
            if ((("".equals(zorDirCp)) || (zorDirCp == null))) {
                log.error("--- ERROR  CP");
                resultado.setAtributo("codigoOperacion", ERROR_CP);
                util74.retornarJSON(new Gson().toJson(resultado), response);
                return ERROR_CP;
            }
            // datos recogidos
            log.info("======== PARAMETROS altaCartaPagoRenunciaSubvenciones =================");
            log.info("---  INTERESADO");
            log.info("Nombre: " + zorTerNombre + " " + zorTerApellido1 + " " + zorTerApellido2);
            log.info("Documento: " + zorTerNumDocumento + " - Tipo: " + tipoDocumentoINT);
            log.info("eMail: " + zorTerEmail);
            log.info("Telefono: " + zorTerTelefono);
            log.info("---  DOMICILIO");
            log.info("Dirección: " + zorDirDireccionCompleta);
            log.info("Id municipio: " + municipioId);
            log.info("Id provincia: " + provinciaId);
            log.info("C.P.:" + zorDirCp);
            log.info("----   DEUDA");
            log.info("Importe: " + zorLiImporteDeuda);
            log.info("Estado: " + zorEdCodigo);
            log.info("Tipo Pago: " + zorTpCodigo);
            log.info("--- FECHAS");
            log.info("Aceptación Renuncia: " + fechaRenuncia.getTime());
            log.info("Requerimiento: " + fechaRequerimiento.getTime());
            log.info("Limite Pago: " + fechaLimitePago.getTime());
            log.info("Pago Subvención: " + fechaPagoSubv.getTime());
            log.info("--- PROCEDIMIENTO");
            log.info("EjercicioCarta: " + zorLaEjercicio + " - paso esta");
//                log.info("Año actual: " + Calendar.getInstance().get(Calendar.YEAR));
            log.info("Código Area Acceso: " + zorArCodigo);

//Rellenamos los objetos del cliente            
            log.debug("  -- altaCartaPagoRenunciaSubvenciones --  Llenado objetos del cliente ");

            // ZORKU
            ParamsAltaCartaPagoRenuncia parametrosAlta = new ParamsAltaCartaPagoRenuncia();
            parametrosAlta.setZorUsUsuario(zorUsUsuario);
            parametrosAlta.setZorUsPassword(zorUsPassword);
            parametrosAlta.setArea(area);
//Area acceso
            parametrosAlta.setZorArCodigo(new BigDecimal(zorArCodigo));
            parametrosAlta.setZorLaEjercicio(new BigDecimal(zorLaEjercicio));

//Datos personales del interesado
            parametrosAlta.setZorTerTipoDocumento(zorTerTipoDocumento);
            parametrosAlta.setZorTerNumDocumento(zorTerNumDocumento);
            parametrosAlta.setZorTerNombre(zorTerNombre);
            parametrosAlta.setZorTerApellido1(zorTerApellido1);
            parametrosAlta.setZorTerApellido2(zorTerApellido2);
            parametrosAlta.setZorTerEmail(zorTerEmail);
            if (zorTerTelefono != null && !zorTerTelefono.isEmpty()) {
                parametrosAlta.setZorTerTelefono(new BigDecimal(zorTerTelefono));
            }
            // domicilio
            parametrosAlta.setZorDirProv(Utilidades74.traducirIdProvincia(provinciaId));
            parametrosAlta.setZorDirMunicipio(Utilidades74.traducirIdMunicipio(municipioId));
            parametrosAlta.setZorDirDireccionCompleta(zorDirDireccionCompleta);
            parametrosAlta.setZorDirCp(zorDirCp);
            if (provinciaId.equals("48")) {
                parametrosAlta.setZorDeTerritorio("1");
            } else if (provinciaId.equals("20")) {
                parametrosAlta.setZorDeTerritorio("2");
            } else if (provinciaId.equals("1")) {
                parametrosAlta.setZorDeTerritorio("3");
            } else {
                parametrosAlta.setZorDeTerritorio("4");
            }
            // deuda
            parametrosAlta.setZorDeCodSubvencion(zorDeCodSubvencion);
            parametrosAlta.setZorLiImporteDeuda(zorLiImporteDeuda);
            parametrosAlta.setZorEdCodigo(zorEdCodigo);
            parametrosAlta.setZorTpCodigo(zorTpCodigo);
            if (esDevolucion != null && esDevolucion.equals("1")) {
                parametrosAlta.setDevolucionFondos(new BigDecimal(1));
            }
            parametrosAlta.setZorDeExpediente(numExpediente);
            parametrosAlta.setZorDeExpRei(zorDeExpRei);
            parametrosAlta.setZorDeExpInicial(numExpediente);
            log.debug("  -- altaCartaPagoRenunciaSubvenciones --  Pasamos al servicio web, el numero de expediente: " + numExpediente);

            //Fechas
            //convertir el Calendar a formato ZORKU
            log.debug("  -- altaCartaPagoRenunciaSubvenciones --   Llenado objetos del cliente  - FECHAS");
            if (fechaRenuncia != null) {
                zorLiFechaRenuncia = Utilidades74.convierteFechaZorku(fechaRenuncia);
                log.debug("  -- altaCartaPagoRenunciaSubvenciones --  Fecha PRESENTACION: " + zorLiFechaRenuncia);
            }
            // requerimiento
            if (fechaRequerimiento != null) {
                zorLiFechaRequerimiento = Utilidades74.convierteFechaZorku(fechaRequerimiento);
                log.debug("  -- altaCartaPagoRenunciaSubvenciones --  Fecha Requerimiento: " + zorLiFechaRequerimiento);
            }
            // limite
            if (fechaLimitePago != null) {
                zorLiFechaLimitePago = Utilidades74.convierteFechaZorku(fechaLimitePago);
                log.debug("  -- altaCartaPagoRenunciaSubvenciones --  Fecha Limite Pago: " + zorLiFechaLimitePago);
            }
            // pago subv
            if (fechaPagoSubv != null) {
                zorLiFechaPagoExpSubv = Utilidades74.convierteFechaZorku(fechaPagoSubv);
                log.info("  -- altaCartaPagoRenunciaSubvenciones --  Fecha Pago Subvención: " + zorLiFechaPagoExpSubv);
            }
            parametrosAlta.setZorLiFechaRenuncia(zorLiFechaRenuncia);
            parametrosAlta.setZorLiFechaRequerimiento(zorLiFechaRequerimiento);
            parametrosAlta.setZorLiFechaPagoExpSubv(zorLiFechaPagoExpSubv);
            parametrosAlta.setZorLiFechaLimitePago(zorLiFechaLimitePago);
            log.info("======== PARAMETROS altaCartaPagoRenunciaSubvenciones  -- Parámetros llamada a ZORKU: " + parametrosAlta.toString());

// llamada al SW altaCartaPagoRenunciaSubvenciones
            try {
                log.info("  -- altaCartaPagoRenunciaSubvenciones --  Llamando al servicio ... " + urlAltaCartaRenuncia);
                HttpClient client = HttpClients.createDefault();
                StringEntity entidadLlamada = new StringEntity(new Gson().toJson(parametrosAlta), ContentType.APPLICATION_JSON);
                HttpPost llamadaAltaDeuda = new HttpPost(urlAltaCartaRenuncia);
                llamadaAltaDeuda.setHeader("Accept", "application/json");
                llamadaAltaDeuda.setEntity(entidadLlamada);
                HttpResponse respuestaAltaDeuda = client.execute(llamadaAltaDeuda);
                // recojo respuesta
                log.info("  --  altaCartaPagoRenunciaSubvenciones  --  getStatusLine() : " + respuestaAltaDeuda.getStatusLine());
                int statusCodeAlta = respuestaAltaDeuda.getStatusLine().getStatusCode();
                log.info("  --  altaCartaPagoRenunciaSubvenciones  --  getStatusLine().getStatusCode() : " + statusCodeAlta);
                log.info("  --  altaCartaPagoRenunciaSubvenciones  --  Hay respuesta de ZORKU");
                HttpEntity entidadRespuestaAlta = respuestaAltaDeuda.getEntity();
                // convierto la respuesta a STRING
                String respuestaAltaString = EntityUtils.toString(entidadRespuestaAlta);
                log.info("  --  altaCartaPagoRenunciaSubvenciones  --  Respuesta String: " + respuestaAltaString);
                if (!respuestaAltaString.isEmpty()) {
                    // convierto a JSON
                    JSONObject respuestaAltaJson;

                    switch (statusCodeAlta) {
                        case HttpStatus.SC_OK:
                            respuestaAltaJson = new JSONObject(respuestaAltaString);
                            idLiquidacion = respuestaAltaJson.getString("numLiquidacion");
                            nombreCarta = "LIQ_" + idLiquidacion + ".pdf";
                            String oidCarta = respuestaAltaJson.getString("oidCartaPago");
                            intereses = new BigDecimal(respuestaAltaJson.getDouble("importeIntereses"));
                            intereses = intereses.setScale(2, RoundingMode.HALF_UP);
                            importeMasInts = zorLiImporteDeuda.add(intereses);
                            log.info("  -- altaCartaPagoRenunciaSubvenciones --  Identificador de la liquidación : " + idLiquidacion);
                            String codIdDeuda = ConfigurationParameter.getParameter(codOrganizacion + BARRA + MODULO_INTEGRACION + BARRA + nombreModulo + BARRA + ConstantesMeLanbide74.CAMPO_ID_DEUDA, ConstantesMeLanbide74.FICHERO_PROPIEDADES);
                            String codCampoCartaPago = ConfigurationParameter.getParameter(codOrganizacion + BARRA + MODULO_INTEGRACION + BARRA + nombreModulo + BARRA + ConstantesMeLanbide74.CAMPO_CARTA_PAGO, ConstantesMeLanbide74.FICHERO_PROPIEDADES);
                            log.info("  -- altaCartaPagoRenunciaSubvenciones --  Se van a grabar  - " + codIdDeuda + " - " + codCampoCartaPago + " -  Trámite: " + Integer.valueOf(cTramite) + " Ocu: " + ocuTramite);
                            log.info("Intereses: " + intereses.toString() + " - Imp + Intereses: " + importeMasInts);
                            try {
                                // SI EXISTE LA CARTA SE BORRA
                                if (MeLanbide74Manager.getInstance().existeDocumentoTFIT(numExpediente, Integer.parseInt(cTramite), ocuTramite, codCampoCartaPago, adapt)) {
                                    int cartaBorrada = MeLanbide74Manager.getInstance().borrarCartaPago(numExpediente, Integer.parseInt(cTramite), ocuTramite, codCampoCartaPago, adapt);
                                    int relBorrada = MeLanbide74Manager.getInstance().borrarRelacionDokusiCST(codOrganizacion, numExpediente, Integer.parseInt(cTramite), ocuTramite, codCampoCartaPago, adapt);
                                    if (cartaBorrada <= 0 || relBorrada <= 0) {
                                        log.error("  -- altaCartaPagoRenunciaSubvenciones --  Ha ocurrido un error al borrar la carta de pago");
                                        resultado.setAtributo("codigoOperacion", ERROR_BORRANDO_CARTA);
                                        util74.retornarJSON(new Gson().toJson(resultado), response);
                                        return ERROR_BORRANDO_CARTA;
                                    }
                                }
                                if (MeLanbide74Manager.getInstance().grabarCartaDokusi(codOrganizacion, numExpediente, cTramite, Integer.toString(ocuTramite), codCampoCartaPago, nombreCarta, oidCarta, adapt)) {
                                    log.info("  -- altaCartaPagoRenunciaSubvenciones --  Carta Grabada");
                                } else {
                                    log.error("  -- altaCartaPagoRenunciaSubvenciones --  .Devolvemos: " + ERROR_GRABANDO_CARTA);
                                    resultado.setAtributo("codigoOperacion", ERROR_GRABANDO_CARTA);
                                    util74.retornarJSON(new Gson().toJson(resultado), response);
                                    return ERROR_GRABANDO_CARTA;
                                }

                                CampoSuplementarioModuloIntegracionVO campoSuplementarioGrabar = new CampoSuplementarioModuloIntegracionVO();
                                campoSuplementarioGrabar.setCodOrganizacion(String.valueOf(codOrganizacion));
                                campoSuplementarioGrabar.setCodProcedimiento(codProcedimiento);
                                campoSuplementarioGrabar.setTipoCampo(ModuloIntegracionExternoCamposFlexia.CAMPO_TEXTO);
                                campoSuplementarioGrabar.setTramite(true);
                                campoSuplementarioGrabar.setCodTramite(cTramite);
                                campoSuplementarioGrabar.setOcurrenciaTramite(String.valueOf(ocuTramite));
                                campoSuplementarioGrabar.setNumExpediente(numExpediente);
                                campoSuplementarioGrabar.setEjercicio(ejercicioExp);
                                campoSuplementarioGrabar.setCodigoCampo(codIdDeuda);
                                campoSuplementarioGrabar.setValorTexto(idLiquidacion);
                                SalidaIntegracionVO grabaCampo = gestorCampoSup.grabarCampoSuplementario(campoSuplementarioGrabar);
                                if (grabaCampo.getStatus() != 0) {
                                    log.error("  -- altaCartaPagoRenunciaSubvenciones --  Ha ocurrido un error al grabar el Id de la Carta de Renuncia");
                                    resultado.setAtributo("codigoOperacion", ERROR_GRABAR_CAMPOS_SUPLEMENTARIOS);
                                    util74.retornarJSON(new Gson().toJson(resultado), response);
                                    return ERROR_GRABAR_CAMPOS_SUPLEMENTARIOS;
                                }
                                log.debug("  -- altaCartaPagoRenunciaSubvenciones --  IDDEUDA Grabado");

                                campoSuplementarioGrabar = new CampoSuplementarioModuloIntegracionVO();
                                campoSuplementarioGrabar.setCodOrganizacion(String.valueOf(codOrganizacion));
                                campoSuplementarioGrabar.setCodProcedimiento(codProcedimiento);
                                campoSuplementarioGrabar.setTipoCampo(ModuloIntegracionExternoCamposFlexia.CAMPO_FECHA);
                                campoSuplementarioGrabar.setTramite(true);
                                campoSuplementarioGrabar.setCodTramite(cTramite);
                                campoSuplementarioGrabar.setOcurrenciaTramite(String.valueOf(ocuTramite));
                                campoSuplementarioGrabar.setNumExpediente(numExpediente);
                                campoSuplementarioGrabar.setEjercicio(ejercicioExp);
                                campoSuplementarioGrabar.setCodigoCampo(codCampoFechaLimitePago);
                                campoSuplementarioGrabar.setValorFecha(fechaLimitePago);
                                grabaCampo = gestorCampoSup.grabarCampoSuplementario(campoSuplementarioGrabar);
                                if (grabaCampo.getStatus() == 0) {
                                    log.debug("  -- altaCartaPagoRenunciaSubvenciones --  FECHA LIMITE PAGO Grabada");
                                }

                                int grabado = MeLanbide74Manager.getInstance().guardarValorCampoNumericoTramite(codOrganizacion, codProcedimiento, ejercicioExp, numExpediente, Integer.parseInt(cTramite), ocuTramite, codCampoIntereses, intereses, adapt);
                                if (grabado > 0) {
                                    log.debug("  -- altaCartaPagoRenunciaSubvenciones --  INTERESES Grabado");
                                }

                                grabado = MeLanbide74Manager.getInstance().guardarValorCampoNumCalculadoTramite(codOrganizacion, codProcedimiento, ejercicioExp, numExpediente, Integer.parseInt(cTramite), ocuTramite, codCampoImporteIntereses, importeMasInts, adapt);
                                if (grabado > 0) {
                                    log.debug("  -- altaCartaPagoRenunciaSubvenciones --  IMPORTE + INTERESES Grabado");
                                }
                            } catch (Exception e) {
                                log.error("  -- altaCartaPagoRenunciaSubvenciones --  Devolvemos: " + ERROR_GRABAR_CAMPOS_SUPLEMENTARIOS);
                                resultado.setAtributo("codigoOperacion", ERROR_GRABAR_CAMPOS_SUPLEMENTARIOS);
                                util74.retornarJSON(new Gson().toJson(resultado), response);
                                return ERROR_GRABAR_CAMPOS_SUPLEMENTARIOS;
                            }
                            break;

                        case HttpStatus.SC_BAD_REQUEST:
                            respuestaAltaJson = new JSONObject(respuestaAltaString);
                            String codError = respuestaAltaJson.getString("codigoError");
                            String msgErrorCas = respuestaAltaJson.getJSONObject("mensaje").getString("descripcionCastellano");
                            String msgErrorEus = respuestaAltaJson.getJSONObject("mensaje").getString("descripcionEuskera");
                            log.error("  --  altaCartaPagoRenunciaSubvenciones  -- Error  al llamar a la API  - " + codError);
                            log.error("  --  altaCartaPagoRenunciaSubvenciones  -- " + msgErrorCas);
                            log.error("  --  altaCartaPagoRenunciaSubvenciones  -- " + msgErrorEus);
                            resultado.setAtributo("codigoOperacion", msgErrorCas);
                            util74.retornarJSON(new Gson().toJson(resultado), response);
                            return msgErrorCas;
                        case HttpStatus.SC_NOT_FOUND:
                            log.error("  --  altaCartaPagoRenunciaSubvenciones  -- Error 404 al llamar a la API " + respuestaAltaDeuda.getStatusLine().getReasonPhrase());
                            resultado.setAtributo("codigoOperacion", ERROR_404);
                            util74.retornarJSON(new Gson().toJson(resultado), response);
                            return ERROR_404;
                        default:
                            log.error("  --  altaCartaPagoRenunciaSubvenciones  -- Error " + statusCodeAlta + " al llamar a la API " + respuestaAltaDeuda.getStatusLine().getReasonPhrase());
                            resultado.setAtributo("codigoOperacion", statusCodeAlta + " - " + respuestaAltaDeuda.getStatusLine().getReasonPhrase());
                            util74.retornarJSON(new Gson().toJson(resultado), response);
                            return ERROR_RESPUESTA_SW;
                    }
                } else {
                    log.error("  --  altaCartaPagoRenunciaSubvenciones  -- Respuesta  de la API " + statusCodeAlta + " - " + respuestaAltaDeuda.getStatusLine().getReasonPhrase());
                    resultado.setAtributo("codigoOperacion", ERROR_RESPUESTA_SW + " - " + statusCodeAlta + " - " + respuestaAltaDeuda.getStatusLine().getReasonPhrase());
                    util74.retornarJSON(new Gson().toJson(resultado), response);
                    return ERROR_RESPUESTA_SW;
                }
            } catch (IOException e) {
                log.error("  --  altaCartaPagoRenunciaSubvenciones  -- IOException  al llamar a la API " + e);
                resultado.setAtributo("codigoOperacion", ERROR_LLAMADA_SW);
                util74.retornarJSON(new Gson().toJson(resultado), response);
                return ERROR_LLAMADA_SW;
            } catch (NumberFormatException e) {
                log.error("  --  altaCartaPagoRenunciaSubvenciones  -- NumberFormatException  al llamar a la API " + e.getMessage());
                resultado.setAtributo("codigoOperacion", ERROR_LLAMADA_SW);
                util74.retornarJSON(new Gson().toJson(resultado), response);
                return ERROR_LLAMADA_SW;
            } catch (UnsupportedCharsetException e) {
                log.error("  --  altaCartaPagoRenunciaSubvenciones  -- UnsupportedCharsetException  al llamar a la API " + e.getMessage());
                resultado.setAtributo("codigoOperacion", ERROR_LLAMADA_SW);
                util74.retornarJSON(new Gson().toJson(resultado), response);
                return ERROR_LLAMADA_SW;
            } catch (ParseException e) {
                log.error("  --  altaCartaPagoRenunciaSubvenciones  -- Exception  al llamar a la API " + e.getMessage());
                resultado.setAtributo("codigoOperacion", ERROR_LLAMADA_SW);
                util74.retornarJSON(new Gson().toJson(resultado), response);
                return ERROR_LLAMADA_SW;
            } catch (JSONException e) {
                log.error("  --  altaCartaPagoRenunciaSubvenciones  -- Exception  al llamar a la API " + e.getMessage());
                resultado.setAtributo("codigoOperacion", ERROR_LLAMADA_SW);
                util74.retornarJSON(new Gson().toJson(resultado), response);
                return ERROR_LLAMADA_SW;
            }

        } catch (Exception e) {
            //Resultado con error
            log.error("error en altaCartaPagoRenunciaSubvenciones ", e);
            resultado.setAtributo("codigoOperacion", ERROR_LLAMADA_SW);
            util74.retornarJSON(new Gson().toJson(resultado), response);
            return ERROR_LLAMADA_SW;
        }
        String respuesta = idLiquidacion + BARRA + nombreCarta + BARRA + intereses;
        log.info("<====== altaCartaPagoRenunciaSubvenciones - Devuelve: " + codOperacion + " - " + respuesta);
        resultado.setAtributo("codigoOperacion", codOperacion);
        resultado.setAtributo("idDeuda", idLiquidacion);
        resultado.setAtributo("nombreCarta", nombreCarta);
        resultado.setAtributo("interesesGenerados", intereses);
        util74.retornarJSON(new Gson().toJson(resultado), response);
        return codOperacion;
    } // altaCartaPagoRenunciaSubvenciones

    /**
     * Operación que recupera los datos de conexión a la BBDD
     *
     * @param codOrganizacion
     * @return AdaptadorSQLBD
     */
    private AdaptadorSQLBD getAdaptSQLBD(String codOrganizacion) {
        ResourceBundle config = ResourceBundle.getBundle("techserver");
        String gestor = config.getString("CON.gestor");
        String jndiGenerico = config.getString("CON.jndi");

        Connection conGenerico = null;
        Statement st = null;
        ResultSet rs = null;
        String[] salida = null;
        DataSource ds = null;
        AdaptadorSQLBD adapt = null;
        synchronized (this) {
            try {
                PortableContext pc = PortableContext.getInstance();

                ds = (DataSource) pc.lookup(jndiGenerico, DataSource.class);
                // Conexión al esquema genérico
                conGenerico = ds.getConnection();

                String sql = "SELECT EEA_BDE FROM A_EEA WHERE EEA_APL=" + ConstantesDatos.APP_GESTION_EXPEDIENTES + " AND AAE_ORG=" + codOrganizacion;
                st = conGenerico.createStatement();
                rs = st.executeQuery(sql);
                String jndi = null;
                while (rs.next()) {
                    jndi = rs.getString("EEA_BDE");
                }//while(rs.next())

                st.close();
                rs.close();
                conGenerico.close();

                if (jndi != null && gestor != null && !"".equals(jndi) && !"".equals(gestor)) {
                    salida = new String[7];
                    salida[0] = gestor;
                    salida[1] = "";
                    salida[2] = "";
                    salida[3] = "";
                    salida[4] = "";
                    salida[5] = "";
                    salida[6] = jndi;
                    adapt = new AdaptadorSQLBD(salida);
                }//if(jndi!=null && gestor!=null && !"".equals(jndi) && !"".equals(gestor))
            } catch (TechnicalException te) {
                log.error("*** AdaptadorSQLBD: " + te.toString());
            } catch (SQLException e) {
            } finally {
                try {
                    if (st != null) {
                        st.close();
                    }
                    if (rs != null) {
                        rs.close();
                    }
                    if (conGenerico != null && !conGenerico.isClosed()) {
                        conGenerico.close();
                    }
                } catch (SQLException e) {
                }//try-catch
            }// finally
        }// synchronized
        return adapt;
    }//getConnection

}// class

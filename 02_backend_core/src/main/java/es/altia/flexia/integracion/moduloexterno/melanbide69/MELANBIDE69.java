package es.altia.flexia.integracion.moduloexterno.melanbide69;

import com.google.gson.Gson;
import es.altia.agora.business.escritorio.UsuarioValueObject;
import es.altia.agora.business.util.GeneralValueObject;
import es.altia.agora.technical.ConstantesDatos;
import es.altia.common.exception.TechnicalException;
import es.altia.flexia.integracion.moduloexterno.melanbide69.dao.MeLanbide69DAO;
import es.altia.flexia.integracion.moduloexterno.melanbide69.i18n.MeLanbide69I18n;
import es.altia.flexia.integracion.moduloexterno.melanbide69.manager.MeLanbide69Manager;
import es.altia.flexia.integracion.moduloexterno.melanbide69.util.ConstantesMeLanbide69;
import es.altia.flexia.integracion.moduloexterno.melanbide69.util.MeLanbide69Util;
import es.altia.flexia.integracion.moduloexterno.melanbide69.vo.DatosEconomicosExpVO;
import es.altia.flexia.integracion.moduloexterno.melanbide69.vo.DatosSuplementariosExpedienteVO;
import es.altia.flexia.integracion.moduloexterno.melanbide69.vo.ExpedienteVO;
import es.altia.flexia.integracion.moduloexterno.melanbide69.vo.FacturaVO;
import es.altia.flexia.integracion.moduloexterno.melanbide69.vo.InfoCampoSuplementarioVO;
import es.altia.flexia.integracion.moduloexterno.melanbide69.vo.InfoDesplegableVO;
import es.altia.flexia.integracion.moduloexterno.melanbide69.vo.InformeApesVO;
import es.altia.flexia.integracion.moduloexterno.melanbide69.vo.InformeApes_No_AceptadasVO;
import es.altia.flexia.integracion.moduloexterno.melanbide69.vo.SocioVO;
import es.altia.flexia.integracion.moduloexterno.plugin.ModuloIntegracionExterno;
import es.altia.technical.PortableContext;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.altia.util.conexion.BDException;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.logging.Level;
import java.util.zip.DataFormatException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFHeader;
import org.apache.poi.hssf.usermodel.HSSFPalette;
import org.apache.poi.hssf.usermodel.HSSFPrintSetup;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.struts.upload.CommonsMultipartRequestHandler;
import org.apache.struts.upload.FormFile;

public class MELANBIDE69 extends ModuloIntegracionExterno {

    private static final Logger log = LogManager.getLogger(MELANBIDE69.class);
    private static final ResourceBundle properties = ResourceBundle.getBundle(ConstantesMeLanbide69.getFICHERO_PROPIEDADES());
    private static final MeLanbide69Util m69Util = new MeLanbide69Util();
    @Override
    public void cargarExpedienteExtension(int codigoOrganizacion, String numeroExpediente, String xml) throws Exception {
        final Class cls = Class.forName("es.altia.flexia.integracion.moduloexterno.melanbide42.MELANBIDE42");
        final Object me42Class = cls.newInstance();
        final Class[] types = {int.class, String.class, String.class};
        final Method method = cls.getMethod("cargarExpedienteExtension", types);

        method.invoke(me42Class, codigoOrganizacion, numeroExpediente, xml);
    }

    public String procesarXMLCarga(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.debug("BEGIN procesarXMLCarga " + this.getClass().getName());
        int codIdioma = 1;
        try {
            UsuarioValueObject usuario = (UsuarioValueObject) request.getSession().getAttribute("usuario");
            if (usuario != null) {
                codIdioma = usuario.getIdioma();
            }

            CommonsMultipartRequestHandler handler = new CommonsMultipartRequestHandler();
            handler.handleRequest(request);
            Hashtable<String, FormFile> table = handler.getFileElements();
            FormFile file = table.get("fichero_xml");
            //Use commons-fileupload to obtain the byte[] of the file (in a servlet of yours)
            byte[] archivoComprimido = file.getFileData();
            String fileName = file.getFileName();
            log.debug("fileName: " + fileName);
            try {
                String xml = decompressZip(archivoComprimido);
                if (!("").equals(xml)) {
                    cargarExpedienteExtension(codOrganizacion, numExpediente, xml);
                }
            } catch (DataFormatException ex) {
                java.util.logging.Logger.getLogger(MELANBIDE69.class.getName()).log(Level.SEVERE, null, ex);
            }

            request.getSession().setAttribute("mensajeImportar", MeLanbide69I18n.getMensaje(codIdioma, "msg.facturasCargadasOK"));
        } catch (FileNotFoundException ex) {
            java.util.logging.Logger.getLogger(MELANBIDE69.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            java.util.logging.Logger.getLogger(MELANBIDE69.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ServletException ex) {
            java.util.logging.Logger.getLogger(MELANBIDE69.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            request.getSession().setAttribute("mensajeImportar", MeLanbide69I18n.getMensaje(codIdioma, "error.errorImportarGen"));
        }

        return "/jsp/extension/melanbide69/recargarListaFacturas.jsp";
    }

    public String decompressZip(byte[] data) throws IOException, DataFormatException {
        ByteArrayInputStream bis = new ByteArrayInputStream(data);
        ZipInputStream zipIn = new ZipInputStream(bis);
        ZipEntry entry = zipIn.getNextEntry();
        String xml = "";

        // iterates over entries in the zip file
        while (entry != null) {
            //String filePath = "/unzipFolder" + File.separator + entry.getName();
            String filePath = entry.getName();
            if (!entry.isDirectory()) {
                // if the entry is a file, extracts it
                //extract file
                BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filePath));
                byte[] bytesIn = new byte[4096/*BUFFER_SIZE*/];
                int read = 0;
                while ((read = zipIn.read(bytesIn)) != -1) {
                    bos.write(bytesIn, 0, read);
                }
                bos.close();
                //String aString = bos.toString();
                //String aString = new String(bos.toByteArray(),"UTF-8");
                xml = readFileAsString(filePath);
            } else {
                // if the entry is a directory, make the directory
                File dir = new File(filePath);
                dir.mkdir();
            }
            zipIn.closeEntry();
            entry = zipIn.getNextEntry();
        }
        zipIn.close();
        log.debug("Original: " + data.length);
        log.debug("Compressed: " + xml);
        return xml;
    }

    private String readFileAsString(String filePath) throws IOException {
        StringBuilder fileData = new StringBuilder();
        BufferedReader reader = new BufferedReader(
                new FileReader(filePath));
        char[] buf = new char[1024];
        int numRead = 0;
        while ((numRead = reader.read(buf)) != -1) {
            String readData = String.valueOf(buf, 0, numRead);
            fileData.append(readData);
        }
        reader.close();
        return fileData.toString();
    }

    public String cargarPantallaListaFacturas(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.debug("Entramos en cargarPantallaListaFacturas de " + this.getClass().getName());
        InfoDesplegableVO desplEstado = new InfoDesplegableVO();
        InfoDesplegableVO desplConcepto = new InfoDesplegableVO();
        InfoDesplegableVO desplSN = new InfoDesplegableVO();
        InfoCampoSuplementarioVO csDesplSubs = new InfoCampoSuplementarioVO();
        InfoCampoSuplementarioVO csFechaSubs = new InfoCampoSuplementarioVO();
        HashMap<String, String> fechasCalculadas = new HashMap<String, String>();
        ArrayList<FacturaVO> relacionFacturas = null;
        MeLanbide69Manager meLanbide69Manager = null;
        AdaptadorSQLBD adapt = null;
        int ejercicio;
        String codProcedimiento;
        String nombreModulo;

        try {
            adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
            String[] partes = numExpediente.split("/");
            ejercicio = Integer.parseInt(partes[0]);
            codProcedimiento = partes[1];
            meLanbide69Manager = MeLanbide69Manager.getInstance();
            nombreModulo = ConstantesMeLanbide69.getNOMBRE_MODULO();
            String codOrg = String.valueOf(codOrganizacion);
            int idioma= m69Util.getIdioma(request);
            // recuperamos la informacion de los desplegables necesarios
            desplEstado = meLanbide69Manager.recuperarCampoDesplegable(codOrg, idioma, ConstantesMeLanbide69.getPropVal_COD_DESPL_EST(codOrg, codProcedimiento));
            desplConcepto = meLanbide69Manager.recuperarCampoDesplegable(codOrg, idioma,ConstantesMeLanbide69.getPropVal_COD_DESPL_CPTO(codOrg, codProcedimiento));
            desplSN = meLanbide69Manager.recuperarCampoDesplegable(codOrg, idioma, ConstantesMeLanbide69.getPropVal_COD_DESPL_SN(codOrg, codProcedimiento));
            relacionFacturas = meLanbide69Manager.recuperarListaFacturas(numExpediente, ConstantesMeLanbide69.getNOMBRE_TABLA_FACTURAS(), codOrg, codProcedimiento, adapt);

            GeneralValueObject camposSup = mostrarCamposSuplementarios(codOrg, codProcedimiento, ejercicio, numExpediente,request, adapt);
            csDesplSubs = (InfoCampoSuplementarioVO) camposSup.getAtributo("csDesplSubs");
            csFechaSubs = (InfoCampoSuplementarioVO) camposSup.getAtributo("csFechaSubs");
            fechasCalculadas = (HashMap<String, String>) camposSup.getAtributo("fechasCalculadas");
        } catch (NumberFormatException nfe) {
            log.error("Hay un error de formato en los datos de entrada", nfe);
        } catch (BDException ex) {
            log.error("Ha ocurrido un error al cargar los datos de la pantalla lista de facturas para el expediente " + numExpediente, ex);
        } catch (SQLException ex) {
            log.error("Ha ocurrido un error al cargar los datos de la pantalla lista de facturas para el expediente " + numExpediente, ex);
        } finally {
            request.setAttribute("comboEstados", desplEstado);
            request.setAttribute("comboConceptos", desplConcepto);
            request.setAttribute("comboSiNo", desplSN);
            request.setAttribute("relacionFacturas", relacionFacturas);
            Set<String> keys = fechasCalculadas.keySet();
            for (String key : keys) {
                request.setAttribute(key, fechasCalculadas.get(key));
            }
            request.setAttribute("csDesplSubsanar", csDesplSubs);
            request.setAttribute("csFechaSubsanar", csFechaSubs);
        }
        return "/jsp/extension/melanbide69/listaFacturasJustificacion.jsp";
    }

    public String cargarDesgloseConcepto(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.debug("Entramos en cargarDesgloseConcepto de " + this.getClass().getName());
        InfoDesplegableVO desplDesglose;
        MeLanbide69Manager meLanbide69Manager;
        AdaptadorSQLBD adapt;
        int ejercicio;
        String codOrg;
        String codProcedimiento;
        String numExp = numExpediente;
        String codCpto;
        String nombreModulo;

        try {
            // Recuperamos valores de la request
            numExp = request.getParameter("numExp");
            codOrg = request.getParameter("codOrg");
            codCpto = request.getParameter("codConcepto");

            adapt = this.getAdaptSQLBD(codOrg);
            String[] partes = numExp.split("/");
            ejercicio = Integer.parseInt(partes[0]);
            codProcedimiento = partes[1];
            meLanbide69Manager = MeLanbide69Manager.getInstance();
            nombreModulo = ConstantesMeLanbide69.getNOMBRE_MODULO();

            // recuperamos la informacion de los desplegables necesarios
            desplDesglose = meLanbide69Manager.obtenerInfoDesplegablePorCodigo(ConstantesMeLanbide69.getPropVal_COD_DESPL_DESGLOSE_CPTO(codOrg, codProcedimiento), codCpto, adapt);

            // Devolvemos la respuesta a la peticion
            GeneralValueObject resultado = new GeneralValueObject();
            resultado.setAtributo("concepto", codCpto);
            resultado.setAtributo("desglose", desplDesglose);
            this.retornarJSON(new Gson().toJson(resultado), response);
        } catch (NumberFormatException nfe) {
            log.error("Hay un error de formato en los datos de entrada", nfe);
        } catch (SQLException ex) {
            log.error("Ha ocurrido un error al cargar los datos del desplegable Desglose de Concepto", ex);
        }
        return null;
    }

    /**
     * Posibles códigos: 
     *      0 -> sin fallos 
     *      1 -> error al crear la conexión a la BBDD
     *      2 -> error al dar de alta una factura
     *      3 -> erro al formatear los datos recibidos
     *      4 -> fallo en la operación de alta
     *      5 -> fallo en la operación de modificar
     *      6 -> error al modificar una factura
     *      9 -> error al recuperar el listado de facturas
     *
     * @param codOrganizacion
     * @param codTramite
     * @param ocurrenciaTramite
     * @param numExpediente
     * @param request
     * @param response
     * @return
     */
    public String guardarFactura(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.debug("Entramos en guardarFactura de " + this.getClass().getName());
        MeLanbide69Manager meLanbide69Manager;
        FacturaVO factura = new FacturaVO();
        AdaptadorSQLBD adapt;
        int ejercicio;
        String codOrg;
        String codProcedimiento;
        String numExp = numExpediente;
        boolean exito = false;
        ArrayList<FacturaVO> lista = null;
        int identificador = -1;
        String error = "0";

        try {
            // Recuperamos valores de la request
            numExp = request.getParameter("numExp");
            codOrg = request.getParameter("codOrg");
            if (request.getParameter("id") != null) {
                identificador = Integer.parseInt(request.getParameter("id"));
            }
            String codEstado = request.getParameter("estado");
            String fechaFactura = request.getParameter("fecha");
            String codConcepto = request.getParameter("codConcepto");
            String codSubcpto = request.getParameter("codSubcpto");
            String entregada = request.getParameter("entreg");
            String justificada = request.getParameter("justif");
            String observaciones = request.getParameter("observ");
            String importe = request.getParameter("importe");
            String identFact = request.getParameter("identFact");

            adapt = this.getAdaptSQLBD(String.valueOf(codOrg));
            String[] partes = numExp.split("/");
            ejercicio = Integer.parseInt(partes[0]);
            codProcedimiento = partes[1];
            meLanbide69Manager = MeLanbide69Manager.getInstance();

            // construimos el objeto FacturaVO y guardamos los datos en la base de datos
            factura.setCodIdent(identificador);
            factura.setNumExpediente(numExp);
            factura.setCodEstado(codEstado);
            factura.setFecha(MeLanbide69Util.formattedStringToDate(fechaFactura));
            factura.setCodConcepto(codConcepto);
            factura.setCodDesgloseCpto(codSubcpto);
            factura.setImporte(Double.parseDouble(importe.replace(".", "").replace(",", ".")));
            factura.setCodEntregaFact(entregada);
            factura.setCodEntregaJustif(justificada);
            factura.setObservaciones(observaciones);
            factura.setIdentFactura(identFact);
            exito = meLanbide69Manager.guardarFactura(factura, ConstantesMeLanbide69.getNOMBRE_TABLA_FACTURAS(), ConstantesMeLanbide69.getNOMBRE_SEQ_FACTURAS(), adapt);
            if (!exito) {
                if (identificador == -1) {
                    error = "4";
                } else {
                    error = "5";
                }
            }
            // Recuperamos el listado de facturas en base de datos y devolvemos la respuesta a la petición
            lista = meLanbide69Manager.recuperarListaFacturas(numExp, ConstantesMeLanbide69.getNOMBRE_TABLA_FACTURAS(), codOrg, codProcedimiento, adapt);
            if (exito && lista == null) {
                error = "9";
            }
        } catch (SQLException ex) {
            if (identificador == -1) {
                error = "2";
                log.error("Ha ocurrido un error al aoadir una nueva factura para el expediente " + numExp, ex);
            } else {
                error = "6";
                log.error("Ha ocurrido un error al actualizar la factura de id " + identificador + " para el expediente " + numExp, ex);
            }
        } catch (BDException ex) {
            error = "1";
        } catch (NumberFormatException nfe) {
            log.error("Hay un error de formato en los datos de entrada", nfe);
            error = "3";
        } catch (ParseException nfe) {
            log.error("Hay un error de formato en los datos de entrada", nfe);
            error = "3";
        } finally {
            GeneralValueObject resultado = new GeneralValueObject();
            if (error.equals("0")) {
                resultado.setAtributo("lista", lista);
                resultado.setAtributo("error", "0");
            } else {
                resultado.setAtributo("error", error);
            }
            this.retornarJSON(new Gson().toJson(resultado), response);
        }
        return null;
    }

    /**
     * Posibles codigos:
     *      0 -> sin fallos
     *      1 -> error al crear la conexión a la BBDD
     *      3 -> error al formatear los datos recibidos
     *      7 -> fallo en la operación de borrado
     *      8 -> error al borrar una factura
     *      9 -> error al recuperar el listado de facturas
     *
     * @param codOrganizacion
     * @param codTramite
     * @param ocurrenciaTramite
     * @param numExpediente
     * @param request
     * @param response
     * @return
     */
    public String eliminarFactura(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.debug("Entramos en eliminarFactura de " + this.getClass().getName());
        MeLanbide69Manager meLanbide69Manager;
        FacturaVO factura = new FacturaVO();
        AdaptadorSQLBD adapt;
        int ejercicio;
        String codOrg;
        String codProcedimiento;
        String numExp = numExpediente;
        boolean exito = false;
        ArrayList<FacturaVO> lista = null;
        int identificador = -1;
        String error = "0";

        try {
            // Recuperamos valores de la request
            numExp = request.getParameter("numExp");
            codOrg = request.getParameter("codOrg");
            if (request.getParameter("id") != null) {
                identificador = Integer.parseInt(request.getParameter("id"));
            }

            adapt = this.getAdaptSQLBD(String.valueOf(codOrg));
            String[] partes = numExp.split("/");
            ejercicio = Integer.parseInt(partes[0]);
            codProcedimiento = partes[1];
            meLanbide69Manager = MeLanbide69Manager.getInstance();

            // construimos el objeto FacturaVO y guardamos los datos en la base de datos
            factura.setCodIdent(identificador);
            factura.setNumExpediente(numExp);
            exito = meLanbide69Manager.borrarFactura(factura, ConstantesMeLanbide69.getNOMBRE_TABLA_FACTURAS(), adapt);
            if (!exito) {
                error = "7";
            }
            // Recuperamos el listado de facturas en base de datos y devolvemos la respuesta a la peticion
            lista = meLanbide69Manager.recuperarListaFacturas(numExp, ConstantesMeLanbide69.getNOMBRE_TABLA_FACTURAS(), codOrg, codProcedimiento, adapt);
            if (exito && lista == null) {
                error = "9";
            }
        } catch (NumberFormatException nfe) {
            log.error("Hay un error de formato en los datos de entrada", nfe);
            error = "3";
        } catch (BDException ex) {
            error = "1";
        } catch (SQLException ex) {
            log.error("Ha ocurrido un error al eliminar la factura de id " + identificador + " para el expediente " + numExp, ex);
            error = "8";
        } finally {
            GeneralValueObject resultado = new GeneralValueObject();
            if (error.equals("0")) {
                resultado.setAtributo("lista", lista);
                resultado.setAtributo("error", "0");
            } else {
                resultado.setAtributo("error", error);
            }
            this.retornarJSON(new Gson().toJson(resultado), response);
        }
        return null;
    }

    /**
     * Posibles codigos: 
     *      0 -> sin fallos 
     *      1 -> error al crear la conexion a la BBDD
     *      3 -> error al formatear los datos recibidos 
     *      10 -> error al recuperar valores de campos suplementarios del expediente
     *
     * @param codOrganizacion
     * @param codTramite
     * @param ocurrenciaTramite
     * @param numExpediente
     * @param request
     * @param response
     * @return
     */
    public String recuperarValoresCS(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.debug("Entramos en recuperarValoresCS de " + this.getClass().getName());
        MeLanbide69Manager meLanbide69Manager;
        AdaptadorSQLBD adapt;
        int ejercicio;
        String codOrg;
        String codProcedimiento;
        String numExp = numExpediente;
        String error = "0";
        GeneralValueObject valoresIniciales = new GeneralValueObject();

        try {
            // Recuperamos valores de la request
            numExp = request.getParameter("numExp");
            codOrg = request.getParameter("codOrg");

            adapt = this.getAdaptSQLBD(codOrg);
            String[] partes = numExp.split("/");
            ejercicio = Integer.parseInt(partes[0]);
            codProcedimiento = partes[1];
            meLanbide69Manager = MeLanbide69Manager.getInstance();

            HashMap<String, String> cSIniciales = ConstantesMeLanbide69.getPropVal_CS_CodYTipoLeer(codOrg, codProcedimiento);
            Set<String> codsCampo = cSIniciales.keySet();
            for (String codigo : codsCampo) {
                int tipo = Integer.parseInt(cSIniciales.get(codigo));
                Object valor = meLanbide69Manager.getDatoSuplementarioExpediente(codigo, tipo, codOrg, ejercicio, codProcedimiento, numExp, adapt);
                valoresIniciales.setAtributo(codigo, valor);
            }
        } catch (NumberFormatException nfe) {
            log.error("Hay un error de formato en los datos de entrada", nfe);
            error = "3";
        } catch (BDException ex) {
            error = "1";
        } catch (SQLException ex) {
            log.error("Ha ocurrido un error al recuperar valores de datos suplementarios para el expediente " + numExp, ex);
            error = "10";
        } finally {
            GeneralValueObject resultado = new GeneralValueObject();
            if (error.equals("0")) {
                resultado.setAtributo("camposSuplementarios", valoresIniciales);
                resultado.setAtributo("error", "0");
            } else {
                resultado.setAtributo("error", error);
            }
            this.retornarJSON(new Gson().toJson(resultado), response);
        }
        return null;
    }

    /**
     * Posibles codigos:
     * 0 -> sin fallos
     * 1 -> error al crear la conexion a la BBDD
     * 3 -> error al formatear los datos recibidos
     * 11 -> error al grabar valores de campos suplementarios del expediente
     * 12 -> fallo en la operacion
     *
     * @param codOrganizacion
     * @param codTramite
     * @param ocurrenciaTramite
     * @param numExpediente
     * @param request
     * @param response
     * @return
     */
    public String guardarImportePago(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.debug("Entramos en guardarImportePago de " + this.getClass().getName());
        MeLanbide69Manager meLanbide69Manager;
        AdaptadorSQLBD adapt;
        int ejercicio;
        String codOrg;
        String codProcedimiento;
        String numExp = numExpediente;
        String error = "0";
        String tipoCalculo = "";
        String codCS1 = "";
        String valCS1 = "";
        String codCS2 = "";
        double valImp = 0;
        Double valImp2 = Double.valueOf(0);
        ArrayList<InfoCampoSuplementarioVO> datos = new ArrayList<InfoCampoSuplementarioVO>();
        boolean exito = false;
        InfoCampoSuplementarioVO csCausa = new InfoCampoSuplementarioVO();
        InfoCampoSuplementarioVO csReintegrar = new InfoCampoSuplementarioVO();
        InfoCampoSuplementarioVO csVO1 = new InfoCampoSuplementarioVO();
        InfoCampoSuplementarioVO csVO2 = new InfoCampoSuplementarioVO();
        String minoracion = "";
        String codMinoracion = "";
        InfoCampoSuplementarioVO csMinoracion = new InfoCampoSuplementarioVO();

        try {
            // Recuperamos valores de la request
            numExp = request.getParameter("numExp");
            codOrg = request.getParameter("codOrg");
            codCS1 = request.getParameter("campo1");
            valCS1 = request.getParameter("valor1");
            codCS2 = request.getParameter("campo2");
            tipoCalculo = request.getParameter("tipoCalculo");
            minoracion = request.getParameter("cantminorada");//si no tiene cantidad a minorar me llega valor 0
            adapt = this.getAdaptSQLBD(codOrg);
            String[] partes = numExp.split("/");
            ejercicio = Integer.parseInt(partes[0]);
            codProcedimiento = partes[1];
            meLanbide69Manager = MeLanbide69Manager.getInstance();

            valImp = Double.parseDouble(valCS1);
            valImp2 = Double.valueOf(0);

            csVO1.setCodigo(codCS1);
            csVO1.setValor(valImp);
            csVO1.setTipoCampo(1);
            datos.add(csVO1);
            //indicamos un campo suplementario sin valor para que lo elimine de base de datos
            csVO2.setCodigo(codCS2);
            csVO2.setValor(valImp2);//no tiene importe segundo pago o importe reintegro, hay que meter un 0 porque no quieren que quede vacoo
            csVO2.setTipoCampo(1);
            datos.add(csVO2);
            //indicamos la cantidad minorada
            codMinoracion = ConstantesMeLanbide69.getPropVal_CS_CANTMIN(codOrg, codProcedimiento);
            csMinoracion.setCodigo(codMinoracion);
            csMinoracion.setTipoCampo(Integer.parseInt(ConstantesMeLanbide69.getPropVal_CS_CANTMIN_TIPO(codOrg, codProcedimiento)));
            csMinoracion.setValor(Double.valueOf(minoracion));
            datos.add(csMinoracion);
            //si se trata de un reintegro indicamos la causa, si es pago indicamos un valor nulo para eliminar el dato, en otro caso no hacemos nada
            if (tipoCalculo.equals("reintegro") || tipoCalculo.equals("pago")) {
                csCausa.setCodigo(ConstantesMeLanbide69.getPropVal_CS_CAUSAREINT(codOrg, codProcedimiento));
                csCausa.setTipoCampo(Integer.parseInt(ConstantesMeLanbide69.getPropVal_CS_CAUSAREINT_TIPO(codOrg, codProcedimiento)));
                if (tipoCalculo.equals("reintegro")) {
                    csCausa.setValor(ConstantesMeLanbide69.getCOD_MOTIVOREINT());
                }
                datos.add(csCausa);
            }
            //si se trata de un reintegro (parcial o total), marcamos el desplegable a subsanar,si es pago indicamos un valor nulo para eliminar el dato
            csReintegrar.setCodigo(ConstantesMeLanbide69.getPropVal_CS_REINTEGRAR(codOrg, codProcedimiento));
            csReintegrar.setTipoCampo(Integer.parseInt(ConstantesMeLanbide69.getPropVal_CS_REINTEGRAR_TIPO(codOrg, codProcedimiento)));
            if (!tipoCalculo.equals("pago")) {
                csReintegrar.setValor(ConstantesMeLanbide69.getCOD_AREINTEGRAR());
            }
            datos.add(csReintegrar);

            exito = meLanbide69Manager.setDatosSuplementariosExpediente(datos, codOrg, codProcedimiento, ejercicio, numExp, adapt);
            if (exito) {
                error = "0";
            } else {
                error = "12";
            }
        } catch (NumberFormatException nfe) {
            log.error("Hay un error de formato en los datos de entrada", nfe);
            error = "3";
        } catch (BDException ex) {
            error = "1";
        } catch (SQLException ex) {
            log.error("Ha ocurrido un error al grabar valores de datos suplementarios para el expediente " + numExp, ex);
            error = "11";
        } finally {
            GeneralValueObject resultado = new GeneralValueObject();
            resultado.setAtributo("error", error);
            resultado.setAtributo("tipoCalculo", tipoCalculo);
            resultado.setAtributo(codCS1, MeLanbide69Util.doubleToFormattedStringSinZeros(valImp));
            resultado.setAtributo(codCS2, MeLanbide69Util.doubleToFormattedStringSinZeros(valImp2));
            resultado.setAtributo(codMinoracion, MeLanbide69Util.doubleToFormattedStringSinZeros(Double.valueOf(minoracion)));
            this.retornarJSON(new Gson().toJson(resultado), response);
        }
        return null;
    }

    /**
     * Posibles codigos:
     * 0 -> sin fallos
     * 1 -> error al crear la conexion a la BBDD
     * 3 -> error al formatear los datos recibidos
     * 10 -> error al recuperar valores de campos suplementarios del expediente
     *
     * @param codOrganizacion
     * @param codTramite
     * @param ocurrenciaTramite
     * @param numExpediente
     * @param request
     * @param response
     * @return
     */
    public String recargarFechasCalculadas(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.debug("Entramos en recargarFechasCalculadas de " + this.getClass().getName());
        AdaptadorSQLBD adapt;
        HashMap<String, String> fechasCalculadas = new HashMap<String, String>();
        InfoCampoSuplementarioVO csDesplSubs = new InfoCampoSuplementarioVO();
        InfoCampoSuplementarioVO csFechaSubs = new InfoCampoSuplementarioVO();
        int ejercicio;
        String codOrg;
        String codProcedimiento;
        String numExp = numExpediente;
        String error = "0";

        try {
                   
            // Recuperamos valores de la request
            numExp = request.getParameter("numExp");
            codOrg = request.getParameter("codOrg");

            adapt = this.getAdaptSQLBD(codOrg);
            String[] partes = numExp.split("/");
            ejercicio = Integer.parseInt(partes[0]);
            codProcedimiento = partes[1];
            
            GeneralValueObject camposSup = mostrarCamposSuplementarios(codOrg,codProcedimiento, ejercicio, numExp, request, adapt);
            csDesplSubs = (InfoCampoSuplementarioVO) camposSup.getAtributo("csDesplSubs");
            csFechaSubs = (InfoCampoSuplementarioVO) camposSup.getAtributo("csFechaSubs");
            fechasCalculadas = (HashMap<String, String>) camposSup.getAtributo("fechasCalculadas");

        } catch (NumberFormatException nfe) {
            log.error("Hay un error de formato en los datos de entrada", nfe);
            error = "3";
        } catch (BDException ex) {
            error = "1";
        } catch (SQLException ex) {
            log.error("Ha ocurrido un error al recuperar valores de datos suplementarios para el expediente " + numExp, ex);
            error = "10";
        } finally {
            GeneralValueObject resultado = new GeneralValueObject();
            if (error.equals("0")) {
                Set<String> keys = fechasCalculadas.keySet();
                resultado.setAtributo("error", "0");
                resultado.setAtributo("keys", keys);
                for (String key : keys) {
                    resultado.setAtributo(key, fechasCalculadas.get(key));
                }
                resultado.setAtributo("csDesplSubsanar", csDesplSubs);
                resultado.setAtributo("csFechaSubsanar", csFechaSubs);
            } else {
                resultado.setAtributo("error", error);
            }
            this.retornarJSON(new Gson().toJson(resultado), response);
        }
        return null;
    }

    /**
     * Posibles codigos:
     * 0 -> sin fallos
     * 3 -> error al formatear los datos recibidos
     * 9 -> error al recuperar el listado de facturas
     * 13 -> error al copiar los datos al portapapeles
     * 20 -> no hay facturas para subsanar
     *
     * @param codOrganizacion
     * @param codTramite
     * @param ocurrenciaTramite
     * @param numExpediente
     * @param request
     * @param response
     * @return
     */
    public String copiarTextoAPortapapeles(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.debug("Entramos en copiarTextoAPortapapeles de " + this.getClass().getName());
        AdaptadorSQLBD adapt;
        HashMap<String, String> fechasCalculadas = new HashMap<String, String>();
        int ejercicio;
        String codOrg;
        String codProcedimiento;
        String numExp = numExpediente;
        String error = "0";
        ArrayList<FacturaVO> relacionFacturas;
        String texto = "";

        try {
            // Recuperamos valores de la request
            numExp = request.getParameter("numExp");
            codOrg = request.getParameter("codOrg");

            adapt = this.getAdaptSQLBD(codOrg);
            String[] partes = numExp.split("/");
            ejercicio = Integer.parseInt(partes[0]);
            codProcedimiento = partes[1];

            relacionFacturas = MeLanbide69Manager.getInstance().recuperarListaFacturas(numExp, ConstantesMeLanbide69.getNOMBRE_TABLA_FACTURAS(), codOrg, codProcedimiento, adapt);
            if (relacionFacturas == null) {
                error = "9";
            }
            int numFila = 1;
            StringBuilder fila;
            for (FacturaVO factura : relacionFacturas) {
                if (factura.esFacturaASubsanar()) {
                    fila = new StringBuilder("FACTURA DE FILA " + numFila + " -->\n");

                    texto += fila.toString() + factura.factToString(false) + "\n";
                }
                numFila++;
            }

            if (texto.isEmpty()) {
                error = "20";
            }

        } catch (NumberFormatException nfe) {
            log.error("Hay un error de formato en los datos de entrada", nfe);
            error = "3";
        } catch (IllegalStateException iee) {
            log.error("Ha ocurrido un error al copiar datos al portapapeles", iee);
            error = "13";
        } catch (SQLException ex) {
            java.util.logging.Logger.getLogger(MELANBIDE69.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            GeneralValueObject resultado = new GeneralValueObject();
            resultado.setAtributo("error", error);
            if (error.equals("0")) {
                resultado.setAtributo("texto", texto);
            }
            this.retornarJSON(new Gson().toJson(resultado), response);
        }
        return null;
    }

    /**
     * Posibles codigos:
     * 0 -> sin fallos 
     * 1 -> error al crear la conexion a la BBDD
     * 3 -> error al formatear los datos recibidos
     * 11 -> error al grabar valores de campos suplementarios del expediente
     * 14 -> fallo en la operacion
     *
     * @param codOrganizacion
     * @param codTramite
     * @param ocurrenciaTramite
     * @param numExpediente
     * @param request
     * @param response
     * @return
     */
    public String guardarValoresCS(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.debug("Entramos en guardarValoresCS de " + this.getClass().getName());
        AdaptadorSQLBD adapt;
        int ejercicio;
        String codOrg;
        String codProcedimiento;
        String numExp = numExpediente;
        String error = "0";
        String valorFecha = null;
        String valorASubs = null;
        String codigoFecha = null;
        String codigoASubs = null;
        boolean exito = false;
        ArrayList<InfoCampoSuplementarioVO> datosCS = new ArrayList<InfoCampoSuplementarioVO>();
        InfoCampoSuplementarioVO campoFecha = new InfoCampoSuplementarioVO();
        InfoCampoSuplementarioVO campoASubs = new InfoCampoSuplementarioVO();

        try {
            // Recuperamos valores de la request
            numExp = request.getParameter("numExp");
            codOrg = request.getParameter("codOrg");
            valorFecha = request.getParameter("fecha");
            valorASubs = request.getParameter("subs");

            adapt = this.getAdaptSQLBD(codOrg);
            String[] partes = numExp.split("/");
            ejercicio = Integer.parseInt(partes[0]);
            codProcedimiento = partes[1];

            // Campo fecha subsanacion
            codigoFecha = ConstantesMeLanbide69.getPropVal_CS_FECSUBSANAR(codOrg, codProcedimiento);
            campoFecha.setCodigo(codigoFecha);
            campoFecha.setTipoCampo(Integer.parseInt(ConstantesMeLanbide69.getPropVal_CS_FECSUBSANAR_TIPO(codOrg, codProcedimiento)));
            if (valorFecha != null && !valorFecha.isEmpty()) {
                campoFecha.setValor(MeLanbide69Util.formattedStringToDate(valorFecha));
            } else {
                campoFecha.setValor(null);
            }
            datosCS.add(campoFecha);

            // Campo a subsanar
            codigoASubs = ConstantesMeLanbide69.getPropVal_CS_SUBSANAR(codOrg, codProcedimiento);
            campoASubs.setCodigo(codigoASubs);
            campoASubs.setTipoCampo(Integer.parseInt(ConstantesMeLanbide69.getPropVal_CS_SUBSANAR_TIPO(codOrg, codProcedimiento)));
            if (valorASubs != null && !valorASubs.isEmpty()) {
                campoASubs.setValor(valorASubs);
            } else {
                campoASubs.setValor(null);
            }
            datosCS.add(campoASubs);

            exito = MeLanbide69Manager.getInstance().setDatosSuplementariosExpediente(datosCS, codOrg, codProcedimiento, ejercicio, numExp, adapt);
            if (exito) {
                error = "0";
            } else {
                error = "14";
            }
        } catch (NumberFormatException nfe) {
            log.error("Hay un error de formato en los datos de entrada", nfe);
            error = "3";
        } catch (ParseException pe) {
            log.error("Hay un error de formato en los datos de entrada", pe);
            error = "3";
        } catch (BDException ex) {
            error = "1";
        } catch (SQLException ex) {
            log.error("Ha ocurrido un error al grabar valores de datos suplementarios para el expediente " + numExp, ex);
            error = "11";
        } finally {
            GeneralValueObject resultado = new GeneralValueObject();
            resultado.setAtributo("error", error);
            resultado.setAtributo(codigoFecha, valorFecha);
            resultado.setAtributo(codigoASubs, valorASubs);
            this.retornarJSON(new Gson().toJson(resultado), response);
        }
        return null;
    }

    private HashMap<String, String> calcularFechas(String codOrg, String codProc, int ejerc, String numExp, AdaptadorSQLBD adapt) throws BDException, SQLException {
        HashMap<String, String> fechas = new HashMap<String, String>();

        String codFechaAltaIAE = ConstantesMeLanbide69.getPropVal_CS_FECALTAIAE(codOrg, codProc);
        String tipoFechaAltaIAE = ConstantesMeLanbide69.getPropVal_CS_FECALTAIAE_TIPO(codOrg, codProc);
        Calendar valFechaAltaIAE = null;

        String codFechaPresentacion = ConstantesMeLanbide69.getPropVal_CS_FECHAPRESENTACION(codOrg, codProc);
        String tipoFechaPresentacion = ConstantesMeLanbide69.getPropVal_CS_FECHAPRESENTACION_TIPO(codOrg, codProc);
        Calendar valFechaPresentacion = null;

        Date fecIniPeriodo = null;
        Date fecFinPeriodo = null;
       // Date fecFinPresentacion = null;
        Date unoEnero = null;

        Calendar cal1;
        Calendar cal2;     
        String anioCurso = String.valueOf(ejerc);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String fechaSt = "01/01/" + anioCurso;
        try {
            unoEnero = sdf.parse(fechaSt);
        } catch (ParseException e) {
            log.error("Error parseando 1 Enero - " + e.toString());
        }

//        Object valorCampo = MeLanbide69Manager.getInstance().getDatoSuplementarioExpediente(codFechaAcuse, Integer.parseInt(tipoFechaAcuse), codOrg, ejerc, codProc, numExp, adapt);
        Object valorIAE = MeLanbide69Manager.getInstance().getDatoSuplementarioExpediente(codFechaAltaIAE, Integer.parseInt(tipoFechaAltaIAE), codOrg, ejerc, codProc, numExp, adapt);
        Object valorPresentacion = MeLanbide69Manager.getInstance().getDatoSuplementarioExpediente(codFechaPresentacion, Integer.parseInt(tipoFechaPresentacion), codOrg, ejerc, codProc, numExp, adapt);
        cal1 = Calendar.getInstance();
        cal2 = Calendar.getInstance();
        if (ejerc <= 2019) {
            if (valorIAE != null && valorIAE instanceof Calendar && valorPresentacion != null && valorPresentacion instanceof Calendar) {
                valFechaAltaIAE = (Calendar) valorIAE;
                valFechaPresentacion = (Calendar) valorPresentacion;

                /* El inicio del periodo sería 3 meses antes de la fecha de alta en el IAE y el fin del periodo 3 meses después de la fecha de alta en el IAE,
                siempre con el tope máximo del 01/01 del año en curso para la fecha de inicio y la fecha de solicitud para la fecha fin. */
                //calculamos fecIniPeriodo
                cal1.setTime(valFechaAltaIAE.getTime());
                cal1.add(Calendar.MONTH, -3);
                cal2.setTime(unoEnero);
                if (cal1.before(cal2)) {
                    log.debug("Fecha Inicio SUPERA Tope");
                    fecIniPeriodo = cal2.getTime();
                } else {
                    log.debug("Fecha Inicio NO SUPERA Tope");
                    fecIniPeriodo = cal1.getTime();
                }
                //calculamos fecFinPeriodo
                cal1.setTime(valFechaAltaIAE.getTime());
                cal1.add(Calendar.MONTH, 3);
                cal1.add(Calendar.DAY_OF_YEAR, -1);
                cal2.setTime(valFechaPresentacion.getTime());
                if (cal1.after(cal2)) {
                    log.debug("Fecha Fin SUPERA Tope");
                    fecFinPeriodo = cal2.getTime();
                } else {
                    log.debug("Fecha Fin NO SUPERA TOPE");
                    fecFinPeriodo = cal1.getTime();
                }
                fechas.put("fecIniPeriodo", MeLanbide69Util.dateToFormattedString(fecIniPeriodo));
                fechas.put("fecFinPeriodo", MeLanbide69Util.dateToFormattedString(fecFinPeriodo));
            } else {
                fechas.put("fecIniPeriodo", "-");
                fechas.put("fecFinPeriodo", "-");
                //  fechas.put("fecFinPresentacion", "-");
            }
        } else if (ejerc >= 2020) {
            /* #451227 la Fecha Inicio Periodo será siempre 01/01/2020 y la Fecha Fin Periodo será la fecha de solicitud de ese expediente,
                ya que las inversiones deben realizarse entre esas fechas. */
                //calculamos fecIniPeriodo
                cal2.setTime(unoEnero);
                fecIniPeriodo = cal2.getTime();
                fechas.put("fecIniPeriodo", MeLanbide69Util.dateToFormattedString(fecIniPeriodo));
            if (valorPresentacion != null && valorPresentacion instanceof Calendar) {
                valFechaPresentacion = (Calendar) valorPresentacion;
                //calculamos fecFinPeriodo
                cal2.setTime(valFechaPresentacion.getTime());
                fecFinPeriodo = cal2.getTime();                
                fechas.put("fecFinPeriodo", MeLanbide69Util.dateToFormattedString(fecFinPeriodo));
            } else {
                fechas.put("fecFinPeriodo", "-");
            }
        }
        return fechas;
    }

    private GeneralValueObject mostrarCamposSuplementarios(String codOrg, String codProcedimiento, int ejercicio, String numExpediente, HttpServletRequest request, AdaptadorSQLBD adapt) throws BDException, SQLException {
        InfoCampoSuplementarioVO csDesplSubs = new InfoCampoSuplementarioVO();
        InfoCampoSuplementarioVO csFechaSubs = new InfoCampoSuplementarioVO();
        HashMap<String, String> fechasCalculadas = new HashMap<String, String>();
        GeneralValueObject salida = new GeneralValueObject();

        // calculamos valores de fechas a partir de FECALTAIAE
        fechasCalculadas = calcularFechas(codOrg, codProcedimiento, ejercicio, numExpediente, adapt);
        // recuperar campos suplementarios desplegables
        String codCampo = ConstantesMeLanbide69.getPropVal_CS_SUBSANAR(codOrg, codProcedimiento);
        String tipoCampo = ConstantesMeLanbide69.getPropVal_CS_SUBSANAR_TIPO(codOrg, codProcedimiento);
        int idioma = m69Util.getIdioma(request);
        csDesplSubs = MeLanbide69Manager.getInstance().recuperarCampoSupDes(codCampo, Integer.parseInt(tipoCampo), codOrg, idioma,codProcedimiento, ejercicio, numExpediente, adapt);

        codCampo = ConstantesMeLanbide69.getPropVal_CS_FECSUBSANAR(codOrg, codProcedimiento);
        tipoCampo = ConstantesMeLanbide69.getPropVal_CS_FECSUBSANAR_TIPO(codOrg, codProcedimiento);
        Object valor = MeLanbide69Manager.getInstance().getDatoSuplementarioExpediente(codCampo, Integer.parseInt(tipoCampo), codOrg, ejercicio, codProcedimiento, numExpediente, adapt);
        csFechaSubs.setCodigo(codCampo);
        if (valor != null) {
            csFechaSubs.setValor(MeLanbide69Util.dateToFormattedString(((Calendar) valor).getTime()));
        } else {
            csFechaSubs.setValor(null);
        }

        salida.setAtributo("fechasCalculadas", fechasCalculadas);
        salida.setAtributo("csDesplSubs", csDesplSubs);
        salida.setAtributo("csFechaSubs", csFechaSubs);

        return salida;
    }

    /**
     * Operación que recupera los datos de conexión a la BBDD
     *
     * @param codOrganizacion
     * @return AdaptadorSQLBD
     */
    private AdaptadorSQLBD getAdaptSQLBD(String codOrganizacion) throws SQLException {
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
                if (log.isDebugEnabled()) {
                    log.debug("He cogido el jndi: " + jndiGenerico);
                }
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
                if (st != null) {
                    st.close();
                }
                if (rs != null) {
                    rs.close();
                }
                if (conGenerico != null && !conGenerico.isClosed()) {
                    conGenerico.close();
                }
            }// finally
           
        }// synchronized
        return adapt;
    }//getConnection

    /**
     * Motodo llamado para devolver un String en formato JSON al cliente que ha
     * realiza la peticion a alguna de las operaciones de este action
     *
     * @param json: String que contiene el JSON a devolver
     * @param response: Objeto de tipo HttpServletResponse a travos del cual se
     * devuelve la salida al cliente que ha realizado la solicitud
     */
    private void retornarJSON(String json, HttpServletResponse response) {
        try {
            if (json != null) {
                response.setCharacterEncoding("UTF-8");
                PrintWriter out = response.getWriter();
                out.print(json);
                out.flush();
                out.close();
            }

        } catch (IOException e) {
        }

    }

    public void descargarInformeInterno(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        if (log.isDebugEnabled()) {
            log.debug("descargarInformeInterno() : BEGIN");
        }
        String NombreInteresado = null;
    //    String documentoInteresado = "";
        int idiomaUsuario = 1;
        String fechaInicioPeriodo = "";
        String fechaFinPeriodo = "";
        int numFila = 0;
        int numFila2 = 0;
     //   final int numColumnasMerge = 3;
        double subeTotal = 0;
        double PrimerPagoRealizado = 0;
        try {
            try {
                UsuarioValueObject usuario = (UsuarioValueObject) request.getSession().getAttribute(numExpediente);
                idiomaUsuario = usuario.getIdioma();
            } catch (Exception e) {
                log.error("Error al recuperar los datos del usuario desde la session : " + e.getMessage(), e);
            }

            log.info("Leemos template con el classloader");
            InputStream template = getClass().getClassLoader().getResourceAsStream("/es/altia/flexia/integracion/moduloexterno/melanbide69/reports/templates/informeInternoAPES_Template.xls");
            log.info("Lectura InputStream template correcta ");

            if (log.isDebugEnabled()) {
                log.debug("descargarInformeInterno() : 1");
            }

            HSSFWorkbook libro = new HSSFWorkbook(template, true); // new HSSFWorkbook();

            if (log.isDebugEnabled()) {
                log.debug("descargarInformeInterno() : 1.1");
            }
            HSSFPalette palette = libro.getCustomPalette();
            short colorIndex = 60;
            //Definimos el color que queremos de fondo para las celdas de cabecera.
            palette.setColorAtIndex(colorIndex, (byte) 142, (byte) 180, (byte) 227);
            HSSFCellStyle my_style = libro.createCellStyle();
            /* Create HSSFFont object from the workbook */
            HSSFFont my_font = libro.createFont();
            /* set the weight of the font */
            my_font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
            /* attach the font to the style created earlier */
            my_style.setFont(my_font);
            //Color de fondo Azul
            //Adaptamos el color definido por nosotros a las celdas.
            my_style.setFillForegroundColor(colorIndex);
            my_style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);

            //Bordes para las celdas de cabecera.
            my_style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
            my_style.setBorderTop(HSSFCellStyle.BORDER_THIN);
            my_style.setBorderRight(HSSFCellStyle.BORDER_THIN);
            my_style.setBorderLeft(HSSFCellStyle.BORDER_THIN);

            HSSFCellStyle estiloImportes = libro.createCellStyle();
            estiloImportes.setAlignment(HSSFCellStyle.ALIGN_RIGHT);

            HSSFCellStyle estiloConceptos = libro.createCellStyle();
            estiloConceptos.setAlignment(HSSFCellStyle.ALIGN_JUSTIFY);
            estiloConceptos.setWrapText(true);
            estiloConceptos.setShrinkToFit(true);
            
            HSSFCellStyle estiloObservaciones = libro.createCellStyle();
            estiloObservaciones.setWrapText(true);


            if (log.isDebugEnabled()) {
                log.debug("descargarInformeInterno() : 1.2");
            }

            log.info("Renombramos hojas del template");
            libro.setSheetName(0, MeLanbide69I18n.getMensaje(idiomaUsuario, "informe.excel.hoja1"));
            libro.setSheetName(1, MeLanbide69I18n.getMensaje(idiomaUsuario, "informe.excel.hoja2"));

            log.info("cogemos las hojas del template");
            HSSFSheet hoja = libro.getSheet(MeLanbide69I18n.getMensaje(idiomaUsuario, "informe.excel.hoja1"));
            HSSFSheet hoja2 = libro.getSheet(MeLanbide69I18n.getMensaje(idiomaUsuario, "informe.excel.hoja2"));

            log.info("Iniciamos tratamiento de las Hojas");
            if (log.isDebugEnabled()) {
                log.debug("descargarInformeInterno() : 1.3");
            }
            /**
             * *******
             * CABECERAS *******
             */
            
            //textos en encabezados diferentes para cada hoja
            HSSFHeader encabezado1 = hoja.getHeader();
            HSSFHeader encabezado2 = hoja2.getHeader();
            
            encabezado1.setCenter(HSSFHeader.font("Arial", "regular") + HSSFHeader.fontSize((short) 14) + 
                    HSSFHeader.startBold() + MeLanbide69I18n.getMensaje(idiomaUsuario, "informe.excel.hoja1") + HSSFHeader.endBold() );
            
            encabezado2.setCenter(HSSFHeader.font("Arial", "regular") + HSSFHeader.fontSize((short) 14) + 
                    HSSFHeader.startBold() + MeLanbide69I18n.getMensaje(idiomaUsuario, "informe.excel.hoja2") + HSSFHeader.endBold() );

            //recupero los datos a mostrar
            AdaptadorSQLBD adapt = null;
            try {
                //adapt = this.getAdaptSQLBD_GestionErores();
                adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
            } catch (SQLException ex) {
                log.error("Error al recuperar el adaptador getAdaptSQLBD ", ex);
            }
            Connection con = null;
            con = adapt.getConnection();
            NombreInteresado = MeLanbide69DAO.getInstance().getNombreInteresado(numExpediente, con);
            log.info(" Preparar Informe APES : NombreInteresado : " + NombreInteresado);
            //MESES (fila 1)
            int contCelda = 0;
            HSSFRow fila;
            HSSFCell celda;

            colorIndex = 60;
            //Definimos el color que queremos de fondo para las celdas de cabecera.
            palette.setColorAtIndex(colorIndex, (byte) 142, (byte) 180, (byte) 227);
            HSSFCellStyle style = libro.createCellStyle();
            HSSFFont font = libro.createFont();
            font.setFontName("Calibri");
            font.setFontHeightInPoints((short) 14);
            font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
            font.setColor(HSSFColor.DARK_BLUE.index);
            // font.setColor(colorIndex);
            style.setFont(font);

            fila = hoja.createRow(numFila++);
            hoja.setColumnWidth(contCelda, 6000);
            celda = fila.createCell(contCelda++);
            celda.setCellValue("");
            hoja2.setColumnWidth(contCelda, 6000);
            celda = fila.createCell(contCelda++);
            celda.setCellValue("");
            hoja.setColumnWidth(contCelda, 16000);
            celda = fila.createCell(contCelda++);
            celda.setCellValue("FACTURAS ACEPTADAS");
            celda.setCellStyle(style);
            fila = hoja.createRow(numFila++);
            contCelda = 0;
            fila = hoja.createRow(numFila++);

            //fila= hoja.createRow(numFila++);
            //celda en blanco
            hoja.setColumnWidth(contCelda, 6000);
            celda = fila.createCell(contCelda++);
            celda.setCellValue("N\u00ba EXPTE.:");
            celda.setCellStyle(my_style);

            hoja.setColumnWidth(contCelda, 6000);
            celda = fila.createCell(contCelda++);
            celda.setCellValue(numExpediente);
            //celda.setCellStyle(my_style);

            fila = hoja.createRow(numFila++);
            contCelda = 0;
            fila = hoja.createRow(numFila++);
            hoja.setColumnWidth(contCelda, 6000);
            celda = fila.createCell(contCelda++);
            celda.setCellValue("BENEFICIARIO:");
            celda.setCellStyle(my_style);

            hoja.setColumnWidth(contCelda, 6000);
            celda = fila.createCell(contCelda++);
            celda.setCellValue(NombreInteresado);

            fila = hoja.createRow(numFila++);

            //Tabla de expediente y beneficiario
            fila = hoja.createRow(numFila++);
            contCelda = 0;
            String[] datosExp = numExpediente.split("/");
            HashMap<String, String> datosFechasCalculadas = calcularFechas(String.valueOf(codOrganizacion), datosExp[1], Integer.parseInt(datosExp[0]), numExpediente, adapt);
            fechaInicioPeriodo = datosFechasCalculadas.get("fecIniPeriodo");
            fechaFinPeriodo = datosFechasCalculadas.get("fecFinPeriodo");

            hoja.setColumnWidth(contCelda, 6000);
            celda = fila.createCell(contCelda++);
            celda.setCellValue(MeLanbide69I18n.getMensaje(idiomaUsuario, "informe.excel.fecha.periodo.inicio"));
            celda.setCellStyle(my_style);

            hoja.setColumnWidth(contCelda, 5000);
            celda = fila.createCell(contCelda++);
            celda.setCellValue(fechaInicioPeriodo);

            hoja.setColumnWidth(contCelda, 6500);
            celda = fila.createCell(contCelda++);
            celda.setCellValue(MeLanbide69I18n.getMensaje(idiomaUsuario, "informe.excel.fecha.periodo.fin"));
            celda.setCellStyle(my_style);

            hoja.setColumnWidth(contCelda, 5000);
            celda = fila.createCell(contCelda++);
            celda.setCellValue(fechaFinPeriodo);
            fila= hoja.createRow(numFila++);
            fila = hoja.createRow(numFila++);
            contCelda = 0;
            hoja.setColumnWidth(contCelda, 6000); // 0
            celda = fila.createCell(contCelda++);
            celda.setCellValue("N\u00ba FACTURA");
            celda.setCellStyle(my_style);

            hoja.setColumnWidth(contCelda, 5000); // 1
            celda = fila.createCell(contCelda++);
            celda.setCellValue("FACTURA");
            celda.setCellStyle(my_style);

            hoja.setColumnWidth(contCelda, 5500); // 2
            celda = fila.createCell(contCelda++);
            celda.setCellValue("JUSTIFICADA");
            celda.setCellStyle(my_style);

            hoja.setColumnWidth(contCelda, 6000); // 3
            celda = fila.createCell(contCelda++);
            celda.setCellValue("CONCEPTO");
            celda.setCellStyle(my_style);

            hoja.setColumnWidth(contCelda, 6000); // 4
            celda = fila.createCell(contCelda++);
            celda.setCellValue("IMPORTE");
            celda.setCellStyle(my_style);

            hoja.getPrintSetup().setLandscape(true);
            hoja.getPrintSetup().setPaperSize(HSSFPrintSetup.A4_PAPERSIZE);

            ///Cabecera Hoja2
            contCelda = 0;
            fila = hoja2.createRow(numFila2++);
            fila = hoja2.createRow(numFila2++);
            hoja2.setColumnWidth(contCelda, 6000);
            celda = fila.createCell(contCelda++);
            celda.setCellValue("");
            hoja2.setColumnWidth(contCelda, 6000);
            celda = fila.createCell(contCelda++);
            celda.setCellValue("");
            hoja2.setColumnWidth(contCelda, 16000);
            celda = fila.createCell(contCelda++);
            celda.setCellValue("FACTURAS NO ACEPTADAS");
            celda.setCellStyle(style);
            fila = hoja2.createRow(numFila2++);
            fila = hoja2.createRow(numFila2++);

            fila = hoja2.createRow(numFila2++);
            contCelda = 0;

            hoja2.setColumnWidth(contCelda, 6000);
            celda = fila.createCell(contCelda++);
            celda.setCellValue("FECHA INCIO PERIODO:");
            celda.setCellStyle(my_style);

            hoja2.setColumnWidth(contCelda, 5000);
            celda = fila.createCell(contCelda++);
            celda.setCellValue(fechaInicioPeriodo);

            hoja2.setColumnWidth(contCelda, 6000);
            celda = fila.createCell(contCelda++);
            celda.setCellValue("FECHA FIN :");
            celda.setCellStyle(my_style);

            hoja2.setColumnWidth(contCelda, 6000);
            celda = fila.createCell(contCelda++);
            celda.setCellValue(fechaFinPeriodo);
         
            fila = hoja2.createRow(numFila2++);
            contCelda = 0;
            fila = hoja2.createRow(numFila2++);
            hoja2.setColumnWidth(contCelda, 8000); // 0
            celda = fila.createCell(contCelda++);
            celda.setCellValue("N\u00ba FACTURA");
            celda.setCellStyle(my_style);

            hoja2.setColumnWidth(contCelda, 3000); // 1
            celda = fila.createCell(contCelda++);
            celda.setCellValue("FACTURA");
            celda.setCellStyle(my_style);

            hoja2.setColumnWidth(contCelda, 3000); // 2
            celda = fila.createCell(contCelda++);
            celda.setCellValue("JUSTIFICADA");
            celda.setCellStyle(my_style);

            hoja2.setColumnWidth(contCelda, 3000); // 3
            celda = fila.createCell(contCelda++);
            celda.setCellValue("CONCEPTO");
            celda.setCellStyle(my_style);

            hoja2.setColumnWidth(contCelda, 4000); // 4
            celda = fila.createCell(contCelda++);
            celda.setCellValue("IMPORTE");
            celda.setCellStyle(my_style);

            
            hoja2.setColumnWidth(contCelda, 10000); // 5
            celda = fila.createCell(contCelda++);
            celda.setCellValue("OBSERVACIONES");
            celda.setCellStyle(my_style);

            hoja2.getPrintSetup().setLandscape(true);
            hoja2.getPrintSetup().setPaperSize(HSSFPrintSetup.A4_PAPERSIZE);

            // hoja.autoSizeColumn(0);
            try {
                if (con != null) {
                    adapt.devolverConexion(con);
                }
            } catch (BDException e) {
            }
            
            if (log.isDebugEnabled()) {
                log.debug("descargarInformeInterno() : 2");
            }

            /**
             * ****
             * DATOS ****
             */
            //recupero los datos a mostrar
            adapt = null;
            try {
                //adapt = this.getAdaptSQLBD_GestionErores();
                adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
            } catch (SQLException ex) {
                log.error("Error al recuperar el adaptador getAdaptSQLBD ", ex);
            }
            if (adapt != null) {
                con = null;
                try {
                    con = adapt.getConnection();

                    List<InformeApesVO> listaDatosInformeInternoResueltas = MeLanbide69DAO.getInstance().getListaApes(numExpediente, con);
                    //Facturas No Pagadas
                    List<InformeApes_No_AceptadasVO> listaDatosFacturasNoPagadas = MeLanbide69DAO.getInstance().getListaApesNoPagadas(numExpediente, con);
                    //Nombre del Interesado Segun el expediente Proporcionado.
                    NombreInteresado = MeLanbide69DAO.getInstance().getNombreInteresado(numExpediente, con);

                    //Campo suplementarios a mostrar en el excell
                    //importes subvencion y primer pago
                    DatosSuplementariosExpedienteVO importesVO = MeLanbide69DAO.getInstance().getDatosSuplementariosInforme(codOrganizacion, numExpediente, con);
                    double acobrar = 0.0;
                    double adevolver = 0.0;
                    if (importesVO != null) {
                        //subvencion total... solo debe tener uno (para CB o no CB... por error puede llegar a tener los dos... :( )
                        if (importesVO.getImporteSubvencion() != null) {
                            subeTotal = importesVO.getImporteSubvencion();
                        } else if (importesVO.getImporteSubvencionCBSC() != null) {
                            subeTotal = importesVO.getImporteSubvencionCBSC();
                        }
                        //si no se ha realizado el primer pago no se tiene en cuenta
                        if (importesVO.getFecPrimerPago() != null) {
                            //primer pago... solo debe tener uno (para CB o no CB... por error puede llegar a tener los dos... :( )
                            if (importesVO.getImportePrimerPago() != null) {
                                PrimerPagoRealizado = importesVO.getImportePrimerPago();
                            } else if (importesVO.getImportePrimerPagoCBSC() != null) {
                                PrimerPagoRealizado = importesVO.getImportePrimerPagoCBSC();
                            }
                        }
                        /*if (importesVO.getImporteSegundoPago() != null) {
                            acobrar = importesVO.getImporteSegundoPago();
                        }*/
                        
                        if (importesVO.getResolucion() != null) {
                            if (importesVO.getResolucion().equals("S")) {
                                acobrar = subeTotal;
                            }
                        }
                        if (importesVO.getImporteReintegro() != null) {
                            adevolver = importesVO.getImporteReintegro();
                        }

                    }

                    if (log.isDebugEnabled()) {
                        log.debug("descargarInformeInterno() : 4");
                    }
                    //la lista de datosInformeGeneral esto ordenada por unidades pero no por meses y aoos
                    double importe;
                    double sumatorio = 0.00;
                    double importe2;
                    double sumatorio2 = 0.00;
                    // numFila = 7;//7
                    for (InformeApesVO datosInformeInterno : listaDatosInformeInternoResueltas) {
                        fila = hoja.createRow(numFila++);
                        //muestro la unidad en la primera celda

                        celda = fila.createCell(0);
                        celda.setCellValue(datosInformeInterno.getIDENTIFICACION());

                        celda = fila.createCell(1);
                        celda.setCellValue(datosInformeInterno.getENTREGADA_FACT());

                        celda = fila.createCell(2);
                        celda.setCellValue(datosInformeInterno.getENTREGADO_JUSTIF());

                        celda = fila.createCell(3);
                        celda.setCellValue(datosInformeInterno.getCODIGO_SUBCONCEPTO());

                        celda = fila.createCell(4);
                        importe = datosInformeInterno.getIMPORTE();
                        celda.setCellValue(importe);
                        sumatorio = sumatorio + importe;

                    }

                    for (InformeApes_No_AceptadasVO datosInformeInterno : listaDatosFacturasNoPagadas) {
                        fila = hoja2.createRow(numFila2++);
                        //muestro la unidad en la primera celda
                        celda = fila.createCell(0);
                        celda.setCellValue(datosInformeInterno.getIDENTIFICACION());

                        celda = fila.createCell(1);
                        celda.setCellValue(datosInformeInterno.getENTREGADA_FACT());

                        celda = fila.createCell(2);
                        celda.setCellValue(datosInformeInterno.getENTREGADO_JUSTIF());

                        celda = fila.createCell(3);
                        celda.setCellValue(datosInformeInterno.getCODIGO_SUBCONCEPTO());

                        celda = fila.createCell(4);
                        importe2 = datosInformeInterno.getIMPORTE();
                        celda.setCellValue(importe2);
                        sumatorio2 = sumatorio2 + importe2;

                        celda = fila.createCell(5);
                        celda.setCellValue(datosInformeInterno.getOBSERVACIONES());
                        celda.setCellStyle(estiloObservaciones);
                    }

                    ///Total Sumatorio de la columna Importe 
                    fila = hoja.createRow(numFila++);
                    hoja.setColumnWidth(contCelda, 6000);
                    celda = fila.createCell(4);
                    celda.setCellValue("TOTAL JUSTIFICADO");
                    celda.setCellStyle(my_style);

                    fila = hoja.createRow(numFila++);
                    celda = fila.createCell(4);
                    celda.setCellValue(sumatorio);

                    ///Total Sumatorio de la columna Importe (hoja 2) 
                    fila = hoja2.createRow(numFila2++);
                    hoja.setColumnWidth(contCelda, 6000);
                    celda = fila.createCell(4);
                    celda.setCellValue("TOTAL");
                    celda.setCellStyle(my_style);

                    fila = hoja2.createRow(numFila2++);
                    celda = fila.createCell(4);
                    celda.setCellValue(sumatorio2);

                    //conjunto de celdas despues del total
                    fila = hoja.createRow(numFila++);
                    hoja.setColumnWidth(contCelda, 6000);
                    celda = fila.createCell(3);
                    celda.setCellValue("SUBVENCI\u00d3N TOTAL:");
                    celda.setCellStyle(my_style);

                    fila = hoja.createRow(numFila++);
                    celda = fila.createCell(3);
                    celda.setCellValue(subeTotal);
                    
/* 22/10/2019
    Olaia Palacios comunica que sobran los campos de "1Âº pago realizado", "a cobrar" y "a devolver"
    Seria suficiente con que se mantengan los datos "total justificado", y "subvencion total"
                    fila = hoja.createRow(numFila++);
                    hoja.setColumnWidth((short) contCelda, (short) 6000);
                    celda = fila.createCell((short) 3);
                    celda.setCellValue("1\u00ba  PAGO REALIZADO");
                    celda.setCellStyle(my_style);

                    fila = hoja.createRow(numFila++);
                    celda = fila.createCell((short) 3);
                    celda.setCellValue(PrimerPagoRealizado);
*/
                    fila = hoja.createRow(numFila++);
                    hoja.setColumnWidth(contCelda, 6000);
                    celda = fila.createCell(3);
                    celda.setCellValue("A COBRAR");
                    celda.setCellStyle(my_style);

                    fila = hoja.createRow(numFila++);
                    celda = fila.createCell(3);
                    celda.setCellValue(acobrar);
/*
                    fila = hoja.createRow(numFila++);
                    hoja.setColumnWidth( contCelda,  6000);
                    celda = fila.createCell( 3);
                    celda.setCellValue("A DEVOLVER");
                    celda.setCellStyle(my_style);

                    fila = hoja.createRow(numFila++);
                    celda = fila.createCell( 3);
                    celda.setCellValue(adevolver);
*/
                    hoja.setRowBreak(fila.getRowNum());
                    fila = hoja.createRow(numFila++);
                    contCelda = 0;
                    hoja.setColumnWidth(contCelda, 6000);
                    celda = fila.createCell(contCelda++);
                    celda.setCellValue(MeLanbide69I18n.getMensaje(idiomaUsuario, "informe.excel.notas.conceptos"));
                    celda.setCellStyle(my_style);
                    fila = hoja.createRow(numFila++);
                    //Preparamos los estilos para las celdas de la notas de conceptos
                    CellRangeAddress rangoMismaFila3Columnas;
                    
                    fila = hoja.createRow(numFila++);
                    contCelda = 0;
                    rangoMismaFila3Columnas = new CellRangeAddress(fila.getRowNum(), fila.getRowNum(), contCelda, contCelda + 2);
                    hoja.addMergedRegion(rangoMismaFila3Columnas);
                    celda = fila.createCell(contCelda);
                    celda.setCellStyle(estiloConceptos);
                    celda.getRow().setHeightInPoints(25);
                    celda.setCellValue(MeLanbide69I18n.getMensaje(idiomaUsuario, "informe.excel.notas.conceptos.101"));

                    contCelda = contCelda + 3;
                    rangoMismaFila3Columnas = new CellRangeAddress(fila.getRowNum(), fila.getRowNum(), contCelda, contCelda + 2);
                    hoja.addMergedRegion(rangoMismaFila3Columnas);
                    celda = fila.createCell(contCelda);
                    celda.setCellStyle(estiloConceptos);
                    celda.setCellValue(MeLanbide69I18n.getMensaje(idiomaUsuario, "informe.excel.notas.conceptos.601"));

                    fila = hoja.createRow(numFila++);
                    contCelda = 0;
                    rangoMismaFila3Columnas = new CellRangeAddress(fila.getRowNum(), fila.getRowNum(), contCelda, contCelda + 2);
                    hoja.addMergedRegion(rangoMismaFila3Columnas);
                    celda = fila.createCell(contCelda);
                    celda.setCellStyle(estiloConceptos);
                    celda.setCellValue(MeLanbide69I18n.getMensaje(idiomaUsuario, "informe.excel.notas.conceptos.1101"));

                    contCelda = contCelda + 3;
                    rangoMismaFila3Columnas = new CellRangeAddress(fila.getRowNum(), fila.getRowNum(), contCelda, contCelda + 2);
                    hoja.addMergedRegion(rangoMismaFila3Columnas);
                    celda = fila.createCell(contCelda);
                    celda.setCellStyle(estiloConceptos);
                    celda.setCellValue(MeLanbide69I18n.getMensaje(idiomaUsuario, "informe.excel.notas.conceptos.701"));
                    
                    fila = hoja.createRow(numFila++);
                    contCelda = 0;
                    rangoMismaFila3Columnas = new CellRangeAddress(fila.getRowNum(), fila.getRowNum(), contCelda, contCelda + 2);
                    hoja.addMergedRegion(rangoMismaFila3Columnas);
                    celda = fila.createCell(contCelda);
                    celda.setCellStyle(estiloConceptos);
                    celda.setCellValue(MeLanbide69I18n.getMensaje(idiomaUsuario, "informe.excel.notas.conceptos.1201"));

                    contCelda = contCelda + 3;
                    rangoMismaFila3Columnas = new CellRangeAddress(fila.getRowNum(), fila.getRowNum(), contCelda, contCelda + 2);
                    hoja.addMergedRegion(rangoMismaFila3Columnas);
                    celda = fila.createCell(contCelda);
                    celda.setCellStyle(estiloConceptos);
                    celda.setCellValue(MeLanbide69I18n.getMensaje(idiomaUsuario, "informe.excel.notas.conceptos.702"));
                    
                    fila = hoja.createRow(numFila++);
                    contCelda = 0;
                    rangoMismaFila3Columnas = new CellRangeAddress(fila.getRowNum(), fila.getRowNum(), contCelda, contCelda + 2);
                    hoja.addMergedRegion(rangoMismaFila3Columnas);
                    celda = fila.createCell(contCelda);
                    celda.setCellStyle(estiloConceptos);
                    celda.setCellValue(MeLanbide69I18n.getMensaje(idiomaUsuario, "informe.excel.notas.conceptos.1301"));

                    contCelda = contCelda + 3;
                    rangoMismaFila3Columnas = new CellRangeAddress(fila.getRowNum(), fila.getRowNum(), contCelda, contCelda + 2);
                    hoja.addMergedRegion(rangoMismaFila3Columnas);
                    celda = fila.createCell(contCelda);
                    celda.setCellStyle(estiloConceptos);
                    celda.setCellValue(MeLanbide69I18n.getMensaje(idiomaUsuario, "informe.excel.notas.conceptos.703"));
                    
                    fila = hoja.createRow(numFila++);
                    contCelda = 0;
                    rangoMismaFila3Columnas = new CellRangeAddress(fila.getRowNum(), fila.getRowNum(), contCelda, contCelda + 2);
                    hoja.addMergedRegion(rangoMismaFila3Columnas);
                    celda = fila.createCell(contCelda);
                    celda.setCellStyle(estiloConceptos);
                    celda.setCellValue(MeLanbide69I18n.getMensaje(idiomaUsuario, "informe.excel.notas.conceptos.201"));

                    contCelda = contCelda + 3;
                    rangoMismaFila3Columnas = new CellRangeAddress(fila.getRowNum(), fila.getRowNum(), contCelda, contCelda + 2);
                    hoja.addMergedRegion(rangoMismaFila3Columnas);
                    celda = fila.createCell(contCelda);
                    celda.setCellStyle(estiloConceptos);
                    celda.setCellValue(MeLanbide69I18n.getMensaje(idiomaUsuario, "informe.excel.notas.conceptos.704"));
                    
                    fila = hoja.createRow(numFila++);
                    contCelda = 0;
                    rangoMismaFila3Columnas = new CellRangeAddress(fila.getRowNum(), fila.getRowNum(), contCelda, contCelda + 2);
                    hoja.addMergedRegion(rangoMismaFila3Columnas);
                    celda = fila.createCell(contCelda);
                    celda.setCellStyle(estiloConceptos);
                    celda.setCellValue(MeLanbide69I18n.getMensaje(idiomaUsuario, "informe.excel.notas.conceptos.301"));

                    contCelda = contCelda + 3;
                    rangoMismaFila3Columnas = new CellRangeAddress(fila.getRowNum(), fila.getRowNum(), contCelda, contCelda + 2);
                    hoja.addMergedRegion(rangoMismaFila3Columnas);
                    celda = fila.createCell(contCelda);
                    celda.setCellStyle(estiloConceptos);
                    celda.setCellValue(MeLanbide69I18n.getMensaje(idiomaUsuario, "informe.excel.notas.conceptos.801"));
                    
                    fila = hoja.createRow(numFila++);
                    contCelda = 0;
                    rangoMismaFila3Columnas = new CellRangeAddress(fila.getRowNum(), fila.getRowNum(), contCelda, contCelda + 2);
                    hoja.addMergedRegion(rangoMismaFila3Columnas);
                    celda = fila.createCell(contCelda);
                    celda.setCellStyle(estiloConceptos);
                    celda.setCellValue(MeLanbide69I18n.getMensaje(idiomaUsuario, "informe.excel.notas.conceptos.401"));

                    contCelda = contCelda + 3;
                    rangoMismaFila3Columnas = new CellRangeAddress(fila.getRowNum(), fila.getRowNum(), contCelda, contCelda + 2);
                    hoja.addMergedRegion(rangoMismaFila3Columnas);
                    celda = fila.createCell(contCelda);
                    celda.setCellStyle(estiloConceptos);
                    celda.setCellValue(MeLanbide69I18n.getMensaje(idiomaUsuario, "informe.excel.notas.conceptos.901"));
                    
                    fila = hoja.createRow(numFila++);
                    contCelda = 0;
                    rangoMismaFila3Columnas = new CellRangeAddress(fila.getRowNum(), fila.getRowNum(), contCelda, contCelda + 2);
                    hoja.addMergedRegion(rangoMismaFila3Columnas);
                    celda = fila.createCell(contCelda);
                    celda.setCellStyle(estiloConceptos);
                    celda.setCellValue(MeLanbide69I18n.getMensaje(idiomaUsuario, "informe.excel.notas.conceptos.501"));

                } catch (Exception e) {
                    log.error("Error al descargarInformeInterno: " + e.getMessage());
                } finally {
                    try {
                        adapt.devolverConexion(con);
                    } catch (BDException e) {
                        log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
                    }
                }
            }
            if (log.isDebugEnabled()) {
                log.debug("descargarInformeInterno() : 6");
            }
            //AUTOAJUSTAR CONTENIDO
            //TODO: mejorar autosize porque deja textos en dos filas..
            for (int j = 0; j < contCelda; j++) {
                //hoja.autoSizeColumn(j);
            }

            /**
             * ***********
             * CREAR FICHERO ***********
             */
            crearFichero(libro, response);
        } catch (Exception ex) {
            log.error("Se ha producido un error recuperando el informe interno ", ex);
        }
        if (log.isDebugEnabled()) {
            log.debug("descargarInformeInterno() : END");
        }
    }

    private void crearFichero(HSSFWorkbook libro, HttpServletResponse response) throws Exception {
        ByteArrayOutputStream excelOutput = new ByteArrayOutputStream();
        libro.write(excelOutput);
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-Disposition", "attachment; filename=informeInterno.xls");
        response.setHeader("Content-Transfer-Encoding", "binary");
        response.setContentLength(excelOutput.size());
        response.getOutputStream().write(excelOutput.toByteArray(), 0, excelOutput.size());
        response.getOutputStream().flush();
        response.getOutputStream().close();
    }

    /**
     * operacion que modifica los datos de titularidad en un centro ya escrito
     *
     * @param codOrganizacion: Código de la organización
     * @param codTramite: Código del trámite
     * @param ocurrenciaTramite: Ocurrencia del trámite
     * @param numExpediente: Número del expediente
     * @return String que puede tomar los siguientes valores:
     *
     * "0" --> Si la operaciï¿½n se ha ejecutado correctamente 
     * "1" --> Ha ocurrido un error genï¿½rico
     * "2" --> No se ha podido obtener una conexiï¿½n a la BBDD
     * "3" --> Error al obtener datos del expediente y tercero
     * "4" --> Faltan datos del expediente y/o tercero 
     *
     * TODO: añadir más detalles de error... (no tengo tiempo ahora!)
     * @throws es.altia.util.conexion.BDException
     */
    public String actualizarDatosSuplementariosAPES(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente) throws BDException {
        log.info("actualizarDatosSuplementariosAPES. INICIO - Expediente: " + numExpediente + "  =================>");
        AdaptadorSQLBD adapt = null;
        Connection con = null;
        MeLanbide69Manager meLanbide69Manager;
        ExpedienteVO datosExpediente;
        Integer edad;
        DatosEconomicosExpVO datosEcon;
        Integer importeSubvencion = null;
        Integer importePago = null;
        try {
            try {
                adapt = getAdaptSQLBD(Integer.toString(codOrganizacion));
                con = adapt.getConnection();
            } catch (BDException e) {
                log.error("Error al obtener una conexiï¿½n a la BBDD: " + e.getMessage());
                return "2";
            } catch (SQLException e) {
                log.error("Error al obtener una conexiï¿½n a la BBDD: " + e.getMessage());
                return "2";
            }
            meLanbide69Manager = MeLanbide69Manager.getInstance();
            // ini DATOS TERCERO
            try {
                datosExpediente = meLanbide69Manager.getDatosExpediente(codOrganizacion, numExpediente, con);
            } catch (Exception e) {
                log.error("Error al obtener datos del tercero: " + e.getMessage());
                return "3";
            }
            //sean CB o no, se calcula la edad y el sexo
            if (datosExpediente != null && datosExpediente.getFecPresentacion() != null && datosExpediente.getTercero() != null && datosExpediente.getTercero().getTFecNacimiento() != null && datosExpediente.getTercero().getTSexoTercero() != null) {
                edad = MeLanbide69Util.calcularEdad(datosExpediente.getTercero().getTFecNacimiento(), datosExpediente.getFecPresentacion());
                adapt.inicioTransaccion(con);
                meLanbide69Manager.actualizaEdad(codOrganizacion, numExpediente, edad, con);
                meLanbide69Manager.actualizaSexo(codOrganizacion, numExpediente, datosExpediente.getTercero().getTSexoTercero(), con);
                // fin DATOS TERCERO
                // ini IMPORTE DE LA SUBVENCION
                //  #403538 se calcula para todos los expedientes y en función de si son CB y S o no se graban los datos en los suplementarios correspondientes
                String[] arrNumExpediente = numExpediente.split("/");
                datosEcon = meLanbide69Manager.getImporteSubvencionYPorc(edad, datosExpediente.getTercero().getTSexoTercero(), arrNumExpediente[1], arrNumExpediente[0], con);
                importeSubvencion = datosEcon.getImporteSubvencion();
                importePago = datosEcon.getImportePago();
                log.debug("Subvención: " + importeSubvencion);
                log.debug("Primer pago: " + importePago);

                if (!datosExpediente.getEsCBSC()) {//si no es CB Guardo los importes en IMPSUBVENCIONG IMPPRIMERPAGOG
                    meLanbide69Manager.actualizaImporteSubvencion(codOrganizacion, numExpediente, importeSubvencion, con);
                    meLanbide69Manager.actualizaImportePrimerPago(codOrganizacion, numExpediente, importePago, con);
                } else {// grabo en IMPSUBVENCBSC IMPPRIMERPAGOCBSC
                    meLanbide69Manager.actualizaImporteSubvencionCByS(codOrganizacion, numExpediente, importeSubvencion, con);
                    meLanbide69Manager.actualizaImportePrimerPagoCByS(codOrganizacion, numExpediente, importePago, con);
                }
                adapt.finTransaccion(con);
            } else {
                return "4";
            }

        } catch (Exception e) {
            log.error("ERROR.- actualizarDatosSuplementariosAPES(). Error: " + e.getMessage());
            if (con != null) {
                adapt.rollBack(con);
            }
            return "1";
        } finally {
            if (adapt != null) {
                adapt.devolverConexion(con);
            }
        }
        log.info("actualizarDatosSuplementariosAPES " + numExpediente + "  =================>");
        return "0";
    }

    public void getListaFacturas(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        try {
            try {
                //Al importar un fichero, se inserta este mensaje en sesion como resultado
                //Justo despues de importar se llama a este metodo para recargar la tabla.
                //Este es el punto donde se borra este mensaje de sesion, para no mantenerlo en memoria innecesariamente.
                request.getSession().removeAttribute("mensajeImportar");
            } catch (Exception ex) {

            }

            int codIdioma = 1;
            try {
                UsuarioValueObject usuario = (UsuarioValueObject) request.getSession().getAttribute("usuario");
                if (usuario != null) {
                    codIdioma = usuario.getIdioma();
                }
            } catch (Exception ex) {

            }
            String[] partes = numExpediente.split("/");

            String codProcedimiento = partes[1];
            AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
            MeLanbide69Manager meLanbide44Manager = MeLanbide69Manager.getInstance();
            ArrayList<FacturaVO> facturas = null;

            facturas = meLanbide44Manager.recuperarListaFacturas(numExpediente, ConstantesMeLanbide69.getNOMBRE_TABLA_FACTURAS(), String.valueOf(codOrganizacion), codProcedimiento, adapt);

            String codigoOperacion = "0";

            //this.escribirListaFacturasRequest(codigoOperacion, facturas, null, codIdioma, response);
            GeneralValueObject resultado = new GeneralValueObject();
            resultado.setAtributo("lista", facturas);
            resultado.setAtributo("error", codigoOperacion);

            this.retornarJSON(new Gson().toJson(resultado), response);

        } catch (SQLException ex) {
        }
    }

    /**
     *
     * @param codOrganizacion
     * @param codTramite
     * @param ocurrenciaTramite
     * @param numExpediente
     * @param request
     * @param response
     * @return url pantalla socios
     */
    public String cargarPantallaSocios(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.info("Entramos en cargarPantallaListaFacturas de " + this.getClass().getSimpleName());
        AdaptadorSQLBD adapt = null;
        String url = "/jsp/extension/melanbide69/socios.jsp";
        try {
            adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
        } catch (SQLException ex) {
            log.error(this.getClass().getName() + " Error al recuperar el adaptador getAdaptSQLBD ", ex);
        }
        request.setAttribute("numExp", numExpediente);
        if (adapt != null) {
            try {
                List<SocioVO> listaSocios = MeLanbide69Manager.getInstance().getListaSocios(numExpediente, codOrganizacion, adapt);
                if (!listaSocios.isEmpty()) {
                    request.setAttribute("listaSocios", listaSocios);
                }
            } catch (Exception ex) {
                log.error("Error al recuperar los datos de Socios - MELANBIDE69 - cargarPantallaSocios ", ex);
            }
        }
        return url;
    }

    /**
     *
     * @param codOrganizacion
     * @param codTramite
     * @param ocurrenciaTramite
     * @param numExpediente
     * @param request
     * @param response
     * @return url nuevo Socio
     */
    public String cargarNuevoSocio(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        String nuevo = "1";
        String numExp = "";
        String urlnuevoSocio = "/jsp/extension/melanbide69/mantenimientoSocios.jsp?codOrganizacion=" + codOrganizacion;
        try {
            if (request.getAttribute("nuevo") != null) {
                if (request.getAttribute("nuevo").toString().equals("0")) {
                    request.setAttribute("nuevo", nuevo);
                }
            } else {
                request.setAttribute("nuevo", nuevo);
            }
            if (request.getParameter("numExp") != null) {
                numExp = request.getParameter("numExp");
                request.setAttribute("numExp", numExp);
            }
        } catch (Exception ex) {
            log.debug("Se ha presentado un error al intentar preparar la jsp de una nuevo socio : " + ex.getMessage());
        }
        return urlnuevoSocio;
    }

    /**
     *
     * @param codOrganizacion
     * @param codTramite
     * @param ocurrenciaTramite
     * @param numExpediente
     * @param request
     * @param response
     * @return url nuevo Socio
     */
    public String cargarModificarSocio(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        String nuevo = "0";
        String urlnuevoSocio = "/jsp/extension/melanbide69/mantenimientoSocios.jsp?codOrganizacion=" + codOrganizacion;
        try {
            if (request.getAttribute("nuevo") != null) {
                if (!request.getAttribute("nuevo").toString().equals("0")) {
                    request.setAttribute("nuevo", nuevo);
                }
            } else {
                request.setAttribute("nuevo", nuevo);
            }
            String id = request.getParameter("id");
            String numExp = request.getParameter("numExp");
            request.setAttribute("numExp", numExp);
            // Recuperramos datos de Socio a modificar y cargamos en el request
            if (id != null && !id.isEmpty()) {
                SocioVO datModif = MeLanbide69Manager.getInstance().getSocioPorID(id, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                if (datModif != null) {
                    request.setAttribute("datModif", datModif);
                }
            }
        } catch (SQLException ex) {
            log.debug("Error al tratar de preparar los datos para modificar y llamar la jsp de modificacion : " + ex.getMessage());
        } catch (Exception ex) {
            log.debug("Error al tratar de preparar los datos para modificar y llamar la jsp de modificacion : " + ex.getMessage());
        }
        return urlnuevoSocio;
    }

    /**
     *
     * @param codOrganizacion
     * @param codTramite
     * @param ocurrenciaTramite
     * @param numExpediente
     * @param request
     * @param response
     */
    public void crearNuevoSocio(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.info("Entramos en crearNuevoSocio - ");
        String codigoOperacion = "-1";
        List<SocioVO> lista = new ArrayList<SocioVO>();
        SocioVO nuevoSocio = new SocioVO();
        try {
            AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
            String numExp = request.getParameter("numExp");
            String dninie = request.getParameter("dninie");
            String nombre = request.getParameter("nombre");
            String apellido1 = request.getParameter("apellido1");
            String apellido2 = request.getParameter("apellido2");

            nuevoSocio.setNumExp(numExp);
            nuevoSocio.setDni(dninie);
            nuevoSocio.setNombre(nombre);
            nuevoSocio.setApellido1(apellido1);
            if (apellido2 != null && !"".equalsIgnoreCase(apellido2)) {
                nuevoSocio.setApellido2(apellido2);
            }
            MeLanbide69Manager meLanbide69Manager = MeLanbide69Manager.getInstance();
            if (meLanbide69Manager.crearSocio(nuevoSocio, adapt)) {
                log.debug("Socio Insertado Correctamente");
                codigoOperacion = "0";
                try {
                    lista = meLanbide69Manager.getListaSocios(numExp, codOrganizacion, adapt);
                } catch (BDException bde) {
                    codigoOperacion = "1";
                    log.debug("Error de tipo BD al recuperar la lista de Socios después de crear : " + bde.getMensaje());
                } catch (Exception ex) {
                    codigoOperacion = "5";
                    log.debug("Error al recuperar la lista de Socios después de crear : " + ex.getMessage());
                }

            } else {
                log.debug("NO se ha insertado correctamente el nuevo SOCIO");
                codigoOperacion = "1";
            }

        } catch (SQLException ex) {
            log.debug("Error al parsear los parámetros recibidos del jsp al objeto " + ex.getMessage());
            codigoOperacion = "3";
        } catch (Exception ex) {
            log.debug("NO se ha insertado correctamente el nuevo SOCIO");
            codigoOperacion = "2";
        }

        GeneralValueObject resultado = new GeneralValueObject();
        resultado.setAtributo("codigoOperacion", codigoOperacion);
        if (codigoOperacion.equalsIgnoreCase("0")) {
            resultado.setAtributo("lista", lista);
        }
        retornarJSON(new Gson().toJson(resultado), response);
    }

    /**
     *
     * @param codOrganizacion
     * @param codTramite
     * @param ocurrenciaTramite
     * @param numExpediente
     * @param request
     * @param response
     */
    public void modificarSocio(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.info("Entramos en modificarSocio - ");
        String codigoOperacion = "-1";
        List<SocioVO> lista = new ArrayList<SocioVO>();
        try {
            AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
            String id = request.getParameter("id");
            String numExp = request.getParameter("numExp");
            String dninie = request.getParameter("dninie");
            String nombre = request.getParameter("nombre");
            String apellido1 = request.getParameter("apellido1");
            String apellido2 = request.getParameter("apellido2");
            if (id == null || id.isEmpty()) {
                log.debug("No se ha recibido desde la JSP el id del Socio a Modificar ");
                codigoOperacion = "3";
            } else {
                SocioVO socioModif = new SocioVO();
                socioModif.setId(Integer.valueOf(id));
                socioModif.setNumExp(numExp);
                socioModif.setDni(dninie);
                socioModif.setNombre(nombre);
                socioModif.setApellido1(apellido1);
                if (apellido2 != null && !"".equalsIgnoreCase(apellido2)) {
                    socioModif.setApellido2(apellido2);
                }
                MeLanbide69Manager meLanbide69Manager = MeLanbide69Manager.getInstance();

                if (meLanbide69Manager.modificarSocio(socioModif, adapt)) {
                    log.debug("Socio Modificado Correctamente");
                    codigoOperacion = "0";
                    try {
                        lista = meLanbide69Manager.getListaSocios(numExp, codOrganizacion, adapt);
                    } catch (BDException bde) {
                        codigoOperacion = "1";
                        log.debug("Error de tipo BD al recuperar la lista de Socios después de modificar : " + bde.getMensaje());
                    } catch (Exception ex) {
                        codigoOperacion = "5";
                        log.debug("Error al recuperar la lista de Socios después de modificar : " + ex.getMessage());
                    }
                } else {
                    log.debug("NO se ha modificado correctamente el SOCIO");
                    codigoOperacion = "1";
                }
            }
        } catch (Exception ex) {
            log.debug("Error modificar --- ", ex);
            codigoOperacion = "3";
        }
        GeneralValueObject resultado = new GeneralValueObject();
        resultado.setAtributo("codigoOperacion", codigoOperacion);
        if (codigoOperacion.equalsIgnoreCase("0")) {
            resultado.setAtributo("lista", lista);
        }
        retornarJSON(new Gson().toJson(resultado), response);
    }

    /**
     *
     * @param codOrganizacion
     * @param codTramite
     * @param ocurrenciaTramite
     * @param numExpediente
     * @param request
     * @param response
     */
    public void eliminarSocio(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.info("Entramos en eliminarSocio - ");
        String codigoOperacion = "-1";
        List<SocioVO> lista = new ArrayList<SocioVO>();
        String numExp = "";

        try {
            String id = request.getParameter("id");
            if (id == null || id.isEmpty()) {
                log.debug("No se ha recibido desde la JSP el id del Socio a Elimnar ");
                codigoOperacion = "3";
            } else {
                numExp = request.getParameter("numExp");
                AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
                int result = 0;
                try {
                    result = MeLanbide69Manager.getInstance().eliminarSocio(id, numExp, adapt);
                } catch (Exception ex) {
                    codigoOperacion = "6";
                }
                if (result <= 0) {
                    codigoOperacion = "6";
                } else {
                    log.debug("Socio eliminado Correctamente");
                    codigoOperacion = "0";
                    try {
                        lista = MeLanbide69Manager.getInstance().getListaSocios(numExp, codOrganizacion, adapt);
                    } catch (BDException bde) {
                        codigoOperacion = "1";
                        log.debug("Error de tipo BD al recuperar la lista de Socios después de eliminar : " + bde.getMensaje());
                    } catch (Exception ex) {
                        codigoOperacion = "5";
                        log.debug("Error al recuperar la lista de Socios después de eliminar : " + ex.getMessage());
                    }
                }
            }
        } catch (SQLException ex) {
            log.debug("Error eliminando una Socio: " + ex);
            codigoOperacion = "2";
        }
        GeneralValueObject resultado = new GeneralValueObject();
        resultado.setAtributo("codigoOperacion", codigoOperacion);
        if (codigoOperacion.equalsIgnoreCase("0")) {
            resultado.setAtributo("lista", lista);
        }
        retornarJSON(new Gson().toJson(resultado), response);
    }

    /**
     * Operación que graba las fechas de firma, envio y recepción de la
     * notificación en suplementarios del expediente
     *
     * @param codOrganizacion: Código de la organización
     * @param codTramite: Código del trámite
     * @param ocurrenciaTramite: Ocurrencia del trámite
     * @param numExpediente: Número del expediente
     * @return String que puede tomar los siguientes valores:
     *
     * 0 --> Si la operación se ha ejecutado correctamente
     * 1 --> Ha ocurrido un error genérico
     * 2 --> No se ha podido obtener una conexión a la BBDD
     * 3 --> Error al obtener las fecha de la firma
     * 4 --> Error al obtener las fechas de la notificación
     * 5 --> No se han recuperado las fechas de la notificación
     * @throws es.altia.util.conexion.BDException
     *
     */
    public String grabarFechasNotificacionAPES(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente) throws BDException {
        log.info(".grabarFechasNotificacionAPES  =================> " + numExpediente);
        AdaptadorSQLBD adapt = null;
        Connection con = null;
        MeLanbide69Manager meLanbide69Manager;
        String codOperacion = "1";
        String codOrg = String.valueOf(codOrganizacion);
        String codProc = numExpediente.split("/")[1];

        String codFechaResolucion = ConstantesMeLanbide69.getPropVal_CS_FECRESOL(codOrg, codProc);
        String codFechaEnvio = ConstantesMeLanbide69.getPropVal_CS_FECENVIO(codOrg, codProc);
        String codFechaAcuse = ConstantesMeLanbide69.getPropVal_CS_FECACUSERECIBO(codOrg, codProc);
        Date[] fechasNotif = null;
        boolean exito = false;
        ArrayList<InfoCampoSuplementarioVO> fechasAPES = new ArrayList<InfoCampoSuplementarioVO>();
        InfoCampoSuplementarioVO campoFechaResolucion = new InfoCampoSuplementarioVO();
        InfoCampoSuplementarioVO campoFechaEnvio = new InfoCampoSuplementarioVO();
        InfoCampoSuplementarioVO campoFechaAcuse = new InfoCampoSuplementarioVO();
        try {
            try {
                adapt = getAdaptSQLBD(Integer.toString(codOrganizacion));
            } catch (SQLException e) {
                log.error("Error al obtener una conexión a la BBDD: " + e.getMessage());
                return "2";
            }
            // pongo espera para que se grabe la fecha de acuse en el expediente
            try {
                log.debug("grabarFechasNotificacionAPES - DUERMO la op 1 segundo");
                Thread.sleep(60000);
            } catch (InterruptedException e) {
            }
            log.debug("grabarFechasNotificacionAPES - DESPIERTA");

            meLanbide69Manager = MeLanbide69Manager.getInstance();
            // recoger fechas         
            try {
                fechasNotif = meLanbide69Manager.getFechasNotificacion(codOrganizacion, codProc, numExpediente, adapt);
            } catch (Exception e) {
                log.error("Error al obtener las fechas de la notificación: " + e.getMessage());
                return "4";
            }
            if (fechasNotif != null && fechasNotif.length == 3) {
                // grabar fechas
                try {
                    // firma => resolucion
                    campoFechaResolucion.setCodigo(codFechaResolucion);
                    campoFechaResolucion.setTipoCampo(Integer.parseInt(ConstantesMeLanbide69.getPropVal_CS_FECRESOL_TIPO(codOrg, codProc)));
                    if (fechasNotif[0] != null) {
                        campoFechaResolucion.setValor(fechasNotif[0]);
                    } else {
                        campoFechaResolucion.setValor(null);
                    }
                    fechasAPES.add(campoFechaResolucion);
                    // envio
                    campoFechaEnvio.setCodigo(codFechaEnvio);
                    campoFechaEnvio.setTipoCampo(Integer.parseInt(ConstantesMeLanbide69.getPropVal_CS_FECENVIO_TIPO(codOrg, codProc)));
                    if (fechasNotif[1] != null) {
                        campoFechaEnvio.setValor(fechasNotif[1]);
                    } else {
                        campoFechaEnvio.setValor(null);
                    }
                    fechasAPES.add(campoFechaEnvio);
                    // acuse
                    campoFechaAcuse.setCodigo(codFechaAcuse);
                    campoFechaAcuse.setTipoCampo(Integer.parseInt(ConstantesMeLanbide69.getPropVal_CS_FECACUSERECIBO_TIPO(codOrg, codProc)));
                    if (fechasNotif[2] != null) {
                        campoFechaAcuse.setValor(fechasNotif[2]);
                    } else {
                        campoFechaAcuse.setValor(null);
                    }
                    fechasAPES.add(campoFechaAcuse);
                    exito = MeLanbide69Manager.getInstance().setDatosSuplementariosExpediente(fechasAPES, codOrg, codProc, Integer.parseInt(numExpediente.split("/")[0]), numExpediente, adapt);
                    if (exito) {
                        log.info("Se han grabado las fechas en el expediente " + numExpediente);
                        codOperacion = "0";
                    } else {
                        log.error("Se ha producido un error al grabar valores de datos suplementarios para el expediente  " + numExpediente);
                        codOperacion = "1";
                    }
                } catch (BDException e) {
                    log.error("Excepción en grabarFechasNotificacionAPES: " + e.getMessage());
                    return "1";
                } catch (NumberFormatException e) {
                    log.error("Excepción en grabarFechasNotificacionAPES: " + e.getMessage());
                    return "1";
                } catch (SQLException e) {
                    log.error("Excepción en grabarFechasNotificacionAPES: " + e.getMessage());
                    return "1";
                }
            } else {
                log.error("No se han recuperado las fechas de la notificación.");
                codOperacion = "5";
            }
        } catch (Exception e) {
            log.error("Excepción en grabarFechasNotificacionAPES: " + e.getMessage());
            return "1";
        } finally {
            if (adapt != null) {
                adapt.devolverConexion(con);
            }
        }
        return codOperacion;
    }

}

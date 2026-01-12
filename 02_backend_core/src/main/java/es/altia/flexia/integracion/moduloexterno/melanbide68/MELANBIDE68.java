/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.melanbide68;
import es.altia.agora.business.escritorio.UsuarioValueObject;
import es.altia.agora.technical.ConstantesDatos;
import es.altia.common.exception.TechnicalException;
//import es.altia.flexia.integracion.moduloexterno.melanbide68.dao.MeLanbide68DAO;
//import es.altia.flexia.integracion.moduloexterno.melanbide68.exception.ExcelRowMappingException;
import es.altia.flexia.integracion.moduloexterno.melanbide68.manager.MeLanbide68Manager;
import es.altia.flexia.integracion.moduloexterno.plugin.ModuloIntegracionExterno;
import es.altia.technical.PortableContext;
import es.altia.util.conexion.AdaptadorSQLBD;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import javax.sql.DataSource;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
//import es.altia.flexia.integracion.moduloexterno.melanbide68.i18n.MeLanbide68I18n;
//import es.altia.flexia.integracion.moduloexterno.melanbide68.util.MeLanbide68MappingUtils;
//import es.altia.flexia.integracion.moduloexterno.melanbide68.util.MeLanbide68Utils;
import es.altia.flexia.integracion.moduloexterno.melanbide68.vo.FilaMetadatoVO;
import es.altia.flexia.integracion.moduloexterno.melanbide68.vo.FilaTipDocDokusiVO;
import es.altia.flexia.integracion.moduloexterno.melanbide68.vo.FilaTipDocLanbideVO;
import es.altia.flexia.integracion.moduloexterno.melanbide68.vo.FilaTipDocDokusiVO;
import es.altia.flexia.integracion.moduloexterno.melanbide68.vo.FilaTipDocProcVO;
import es.altia.flexia.integracion.moduloexterno.melanbide68.vo.FilaTipDokusiVO;
import es.altia.flexia.integracion.moduloexterno.melanbide68.vo.GrupoTipDocVO;
import es.altia.flexia.integracion.moduloexterno.melanbide68.vo.MetadatoVO;
//import es.altia.flexia.integracion.moduloexterno.melanbide68.vo.ObjetoSGAVO;
import es.altia.flexia.integracion.moduloexterno.melanbide68.vo.ProcedimientoVO;
import es.altia.flexia.integracion.moduloexterno.melanbide68.vo.TipDocLanbideVO;
import es.altia.flexia.integracion.moduloexterno.melanbide68.vo.TipDocPorProcedVO;
import es.altia.flexia.integracion.moduloexterno.melanbide68.vo.TipDokusiVO;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
//import es.altia.util.conexion.BDException;
//import java.io.ByteArrayInputStream;
//import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
//import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
//import java.util.Date;
import java.util.Hashtable;
import java.util.List;
//import java.util.Map;
import java.util.logging.Level;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
//import org.apache.poi.hssf.usermodel.HSSFRow;
//import org.apache.poi.hssf.usermodel.HSSFSheet;
//import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.struts.upload.CommonsMultipartRequestHandler;
import org.apache.struts.upload.FormFile;
import org.apache.struts.upload.MultipartRequestWrapper;

public class MELANBIDE68 extends ModuloIntegracionExterno {

    private static Logger log = LogManager.getLogger(MELANBIDE68.class);
    private final String BARRA = "/";
    private static DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

    public String cargarPantallaDatosSGA(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.debug("Entramos en cargarPantallaDatosSGA de " + this.getClass().getName());

        return "/jsp/extension/melanbide68/procesos/cargaDatosSGA.jsp";
    }

    public String procesarExcelSGA(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) throws Exception {

        AdaptadorSQLBD adaptador = null;
        Connection con = null;

        try {
            MultipartRequestWrapper wrapper = new MultipartRequestWrapper(request);

            wrapper.getParameterMap();
            CommonsMultipartRequestHandler handler = new CommonsMultipartRequestHandler();
            handler.handleRequest(request);
            Hashtable<String, FormFile> table = handler.getFileElements();
            FormFile file = table.get("ficheroSGA");

            adaptador = this.getAdaptSQLBD(String.valueOf(codOrganizacion));

            log.error("LLAMANDO AL THREAD EjecutaCargaDatosSGA");
            EjecutaCargaDatosSGA ejecuta = new EjecutaCargaDatosSGA();
            ejecuta.start(file, adaptador);

        } catch (Exception ex) {
            ex.printStackTrace();
            log.error("Se ha producido un error importando datos del archivo excel: " + ex.getMessage());
        }
        return "/jsp/extension/melanbide68/procesos/cargaDatosSGA.jsp";
    }

    // Mï¿½todo privado para obtener una adaptador de BBDD
    private AdaptadorSQLBD getAdaptSQLBD(String codOrganizacion) throws SQLException {
        if (log.isDebugEnabled()) {
            log.debug("getConnection ( codOrganizacion = " + codOrganizacion + " ) : BEGIN");
        }
        ResourceBundle config = ResourceBundle.getBundle("techserver");
        String gestor = config.getString("CON.gestor");
        String jndiGenerico = config.getString("CON.jndi");
        Connection conGenerico = null;
        Statement st = null;
        ResultSet rs = null;
        String[] salida = null;
        Connection con = null;

        if (log.isDebugEnabled()) {
            log.debug("getJndi =========> ");
            log.debug("parametro codOrganizacion: " + codOrganizacion);
            log.debug("gestor: " + gestor);
            log.debug("jndi: " + jndiGenerico);
        }//if(log.isDebugEnabled())

        DataSource ds = null;
        AdaptadorSQLBD adapt = null;
        synchronized (this) {
            try {
                PortableContext pc = PortableContext.getInstance();
                if (log.isDebugEnabled()) {
                    log.debug("He cogido el jndi: " + jndiGenerico);
                }
                ds = (DataSource) pc.lookup(jndiGenerico, DataSource.class);
                // Conexiï¿½n al esquema genï¿½rico
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
                te.printStackTrace();
                log.error("*** AdaptadorSQLBD: " + te.toString());
            } catch (SQLException e) {
                e.printStackTrace();
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
            if (log.isDebugEnabled()) {
                log.debug("getConnection() : END");
            }
        }// synchronized
        return adapt;
    }//getConnection 


    public String cargarPantallaTiposDocLanbide(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) throws Exception {
        log.debug("Entramos en cargarPantallaTiposDocLanbide de " + this.getClass().getName());
        AdaptadorSQLBD adapt = null;
        try {
            adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
        } catch (Exception ex) {
            log.error(this.getClass().getName() + " Error al recuperar el adaptador getAdaptSQLBD ", ex);
        }
        String url = "/jsp/extension/melanbide68/gestiones/tiposDocumentales.jsp";

        if (adapt != null) {
            log.error("adapt != null");

            cargarTablaTiposDocumentales(adapt, request);
        } else {
            log.error("adapt == null");
        }
        return url;
    }

    private void cargarTablaTiposDocumentales(AdaptadorSQLBD adapt, HttpServletRequest request) {
        try {
            //     List<FilaTipDocLanbideVO> tiposDocumentales = MeLanbide68Manager.getInstance().getListaTiposDocumentales(adapt);
            //     request.setAttribute("tiposDocumentales", tiposDocumentales);
            List<FilaTipDocLanbideVO> listaTiposDocumentales = MeLanbide68Manager.getInstance().getListaTiposDocumentales(adapt);
            request.setAttribute("listaTiposDocumentales", listaTiposDocumentales);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }



    private void escribirListatiposDocumentalesRequest(String codigoOperacion, List<FilaTipDocLanbideVO> tiposDocumentales, HttpServletResponse response) {
        StringBuffer xmlSalida = new StringBuffer();
        xmlSalida.append("<RESPUESTA>");
        xmlSalida.append("<CODIGO_OPERACION>");
        xmlSalida.append(codigoOperacion);
        xmlSalida.append("</CODIGO_OPERACION>");

        for (FilaTipDocLanbideVO tipDocLanbide : tiposDocumentales) {
            xmlSalida.append("<FILA>");
            xmlSalida.append("<ID>");
            xmlSalida.append(tipDocLanbide.getId());
            xmlSalida.append("</ID>");
            xmlSalida.append("<COD_TIPDOC>");
            xmlSalida.append(tipDocLanbide.getTipDocID());
            xmlSalida.append("</COD_TIPDOC>");
            xmlSalida.append("<TIPDOC_ES>");
            xmlSalida.append(tipDocLanbide.getTipDocLanbide_es());
            xmlSalida.append("</TIPDOC_ES>");
            xmlSalida.append("<TIPDOC_EU>");
            xmlSalida.append(tipDocLanbide.getTipDocLanbide_eu());
            xmlSalida.append("</TIPDOC_EU>");
            xmlSalida.append("<TIPDOC_ES_L>");
            xmlSalida.append(tipDocLanbide.getTipDocLanbide_es_L());
            xmlSalida.append("</TIPDOC_ES_L>");
            xmlSalida.append("<TIPDOC_EU_L>");
            xmlSalida.append(tipDocLanbide.getTipDocLanbide_eu_L());
            xmlSalida.append("</TIPDOC_EU_L>");
            xmlSalida.append("<TIPDOC_DOKUSI>");
            xmlSalida.append(tipDocLanbide.getTipDocDokusi());
            xmlSalida.append("</TIPDOC_DOKUSI>");
            xmlSalida.append("<TIENE_METADATO>");
            xmlSalida.append(tipDocLanbide.getTieneMetadato());
            xmlSalida.append("</TIENE_METADATO>");
            xmlSalida.append("<DESHABILITADO>");
            xmlSalida.append(tipDocLanbide.getDeshabilitado());
            xmlSalida.append("</DESHABILITADO>");
            xmlSalida.append("<GRUPO>");
            xmlSalida.append(tipDocLanbide.getCodGrupo());
            xmlSalida.append("</GRUPO>");
            xmlSalida.append("</FILA>");
        }
        xmlSalida.append("</RESPUESTA>");
       
        try {
            response.setContentType("text/xml");
            //response.setCharacterEncoding("ISO-8859-15");
            response.setCharacterEncoding("UTF-8");
           
            PrintWriter out = response.getWriter();
            out.print(xmlSalida.toString());
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }//try-catch
    }
    private void escribirListaMensajesRequest(String codigoOperacion, List<String> mensajes, HttpServletResponse response) {
        StringBuffer xmlSalida = new StringBuffer();
        xmlSalida.append("<RESPUESTA>");
        xmlSalida.append("<CODIGO_OPERACION>");
        xmlSalida.append(codigoOperacion);
        xmlSalida.append("</CODIGO_OPERACION>");

        for (String mens  : mensajes) {
            xmlSalida.append("<FILA>");
            xmlSalida.append("<MENS>");
            xmlSalida.append(mens);
            xmlSalida.append("</MENS>");
            xmlSalida.append("</FILA>");
        }
        xmlSalida.append("</RESPUESTA>");
        try {
            response.setContentType("text/xml");
            //response.setCharacterEncoding("ISO-8859-15");
            response.setCharacterEncoding("UTF-8");
            PrintWriter out = response.getWriter();
            out.print(xmlSalida.toString());
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }//try-catch
    }

    public void cargarDeshabilitarTipDocLanbide(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        String codigoOperacion = "0";
        List<FilaTipDocLanbideVO> tiposDocumentales = new ArrayList();

        AdaptadorSQLBD adaptador = null;
        try {
            adaptador = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
        } catch (SQLException ex) {
            java.util.logging.Logger.getLogger(MELANBIDE68.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            String id = request.getParameter("id");

            int idInt = 0;
            if (id != null) {
                idInt = Integer.parseInt(id);
            }

            FilaTipDocLanbideVO tipDocLanbide = new FilaTipDocLanbideVO();
            tipDocLanbide.setTipDocID(idInt);

            tipDocLanbide = MeLanbide68Manager.getInstance().getTipoDocumentalById(idInt, adaptador);

            if (tipDocLanbide.getTipDocID() == 0) {
                codigoOperacion = "3";
            } else {
                //Elimina(Deshabilita) de las dos tablas: MELANBIDE68_TIPDOC_LANBIDE y MELANBIDE68_DOKUSI_TIPDOC_LANB
                MeLanbide68Manager.getInstance().deshabilitarTipDocLanbideVO(codOrganizacion, tipDocLanbide, adaptador);
            }

        } catch (SQLException e) {
            //Si existen registros con el mismo código, en la tabla de relaciones Lanbide-DOKUSI o en la tabla Metadatos           
            if (e.getErrorCode() == 2292) {
                codigoOperacion = "4";
            } else {
                codigoOperacion = "2";
            }
            e.printStackTrace();

        } catch (Exception ex) {
            codigoOperacion = "2";
            ex.printStackTrace();
        } finally {
            try {
                tiposDocumentales = MeLanbide68Manager.getInstance().getListaTiposDocumentales(adaptador);
            } catch (Exception ex) {
                codigoOperacion = "2";
                ex.printStackTrace();
            }
            escribirListatiposDocumentalesRequest(codigoOperacion, tiposDocumentales, response);
        }
    }

    public String cargarNuevoTipDocLanbide(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        cargarComboNuevoTipDokusi(codOrganizacion, request);
        cargarComboNuevoGruposTipDoc(codOrganizacion, request);
        return "/jsp/extension/melanbide68/gestiones/nuevoTipDocLanbide.jsp?codOrganizacionModulo=" + codOrganizacion;
    }
    
    public String cargarModificarTipDoc(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        //Recoger la descripcion tipo dokusi en castellano
        String tipoDokusi = "";
        
        AdaptadorSQLBD adaptador = null;
        try {
            adaptador = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
        } catch (SQLException ex) {
            java.util.logging.Logger.getLogger(MELANBIDE68.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            String id = request.getParameter("id");
            // Recuperamos datos a modificar y cargamos en el request
            if (id != null && !id.equals("")) {
                FilaTipDocLanbideVO datModif = MeLanbide68Manager.getInstance().getTipoDocumentalById(Integer.parseInt(id), this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                if (datModif != null) {
                    request.setAttribute("datModif", datModif);
                }
                
            }
            
        } catch (Exception ex) {
            log.debug("Error al tratar de preparar los datos para modificar y llamar la jsp de modificacion : " + ex.getMessage());
        }
        try {
            String codTipDokusi = request.getParameter("tipDocDokusi");
            FilaTipDokusiVO tipDokusi = new FilaTipDokusiVO();
            tipDokusi = MeLanbide68Manager.getInstance().getTipoDokusi(codTipDokusi, adaptador);
            tipoDokusi = tipDokusi.getDesDokusi_es();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        
        
        cargarComboNuevoTipDokusi(codOrganizacion, request);
        cargarComboNuevoGruposTipDoc(codOrganizacion, request);
        return "/jsp/extension/melanbide68/gestiones/nuevoTipDocLanbide.jsp?codOrganizacionModulo=" + codOrganizacion
                + "&id=" + request.getParameter("id")
                //+ "&codTipDoc=" + request.getParameter("codTipDoc")
                //+ "&tipDocCas=" + request.getParameter("tipDocCas")
                //+ "&tipDocEus=" + request.getParameter("tipDocEus")
                //+ "&tipDocCasL=" + request.getParameter("tipDocCasL")
                //+ "&tipDocEusL=" + request.getParameter("tipDocEusL")
                + "&tipDocDokusi=" + request.getParameter("tipDocDokusi")
                + "&tipDokusi=" + tipoDokusi
                ;
    }
    
    public void modificarTipDoc(int codOrganizacion, int codTram, int ocurrenciaTram, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        String codigoOperacion = "0";
        FilaTipDocLanbideVO tipoDoc = new FilaTipDocLanbideVO();
        List<FilaTipDocLanbideVO> tiposDocumentales = new ArrayList();
        try {
            AdaptadorSQLBD adaptador = null;
            try {
                adaptador = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
            } catch (SQLException ex) {
                java.util.logging.Logger.getLogger(MELANBIDE68.class.getName()).log(Level.SEVERE, null, ex);
            }

            String id = (String) request.getParameter("id");
            if (id == null || id.equals("")) {
                log.debug("No se ha recibido desde la JSP el id del tipo documental ");
                codigoOperacion = "3";
            } else {
                String codTipDoc = (String) request.getParameter("codTipDoc");
                int codTipDocInt = 0;
                if (codTipDoc != null) {
                    codTipDocInt = Integer.parseInt(codTipDoc);
                }
                String tipDocCas = (String) request.getParameter("tipDocCas");
                String tipDocEus = (String) request.getParameter("tipDocEus");
                String tipDocCasL = (String) request.getParameter("tipDocCasL");
                String tipDocEusL = (String) request.getParameter("tipDocEusL");
                
                String codGrupo = (String) request.getParameter("codGrupo");
                
                FilaTipDocLanbideVO tipoDocByCodigo = null;
                tipoDocByCodigo = new FilaTipDocLanbideVO();
                tipoDocByCodigo = MeLanbide68Manager.getInstance().getTipoDocumentalByCodigo(codTipDocInt, adaptador);
                
                FilaTipDocLanbideVO tipoDocById = null;
                tipoDocById = new FilaTipDocLanbideVO();
                tipoDocById = MeLanbide68Manager.getInstance().getTipoDocumentalById(Integer.parseInt(id), adaptador);
                
                //si existe dar error de duplicado
                //if (codTipDocInt !=0 && codTipDocInt != tipoDocById.getTipDocID() && tipoDocByCodigo.getTipDocID() != 0) {
                //if (codTipDocInt ==0 || tipoDocByCodigo.getTipDocID() > 0) {
                if (codTipDocInt == 0 || (tipoDocById.getTipDocID() != tipoDocByCodigo.getTipDocID() && tipoDocByCodigo.getTipDocID() > 0)) {
                    codigoOperacion = "4";
                } else {
                    tipoDoc = MeLanbide68Manager.getInstance().getTipoDocumentalById(Integer.parseInt(id), adaptador);
                    
                    //  if(tipoDoc.getTipDocID == null)
                    if (tipoDoc.getTipDocID() == 0) {
                        codigoOperacion = "3";
                    } else {
                        tipoDoc.setTipDocID(codTipDocInt);
                        tipoDoc.setTipDocLanbide_es(tipDocCas);
                        tipoDoc.setTipDocLanbide_eu(tipDocEus);
                        tipoDoc.setTipDocLanbide_es_L(tipDocCasL);
                        tipoDoc.setTipDocLanbide_eu_L(tipDocEusL);
                        
                        tipoDoc.setCodGrupo(codGrupo);
                               
                        //MeLanbide68Manager.getInstance().modificarTipDocLanbideVO(codOrganizacion, tipoDoc, adaptador);
                        
                        //Modificar tabla de relación Lanbide-DOKUSI                
                        FilaTipDocDokusiVO tipoDocDokusi = new FilaTipDocDokusiVO();
                        String tipDocDokusi = (String) request.getParameter("tipDocDokusi");
                        tipoDocDokusi.setCodTipDoc(Integer.parseInt(id));
                        tipoDocDokusi.setTipDocDokusi(tipDocDokusi);
                        //MeLanbide68Manager.getInstance().modificarTipDocDokusiVO(codOrganizacion, tipoDocDokusi, adaptador);
                      
                        //Modificar las 2 tablas relacionadas
                        MeLanbide68Manager.getInstance().modificarTipDocLanbideVO(codOrganizacion, tipoDoc, tipoDocDokusi, adaptador);
                    }
                    
                    tiposDocumentales = MeLanbide68Manager.getInstance().getListaTiposDocumentales(adaptador);
                    
                    //escribirListatiposDocumentalesRequest(codigoOperacion, tiposDocumentales, response);
                }
                
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        
        escribirListatiposDocumentalesRequest(codigoOperacion, tiposDocumentales, response);
    }
    
    public void guardarTipDoc(int codOrganizacion, int codTram, int ocurrenciaTram, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        String codigoOperacion = "0";
        List<FilaTipDocLanbideVO> tiposDocumentales = new ArrayList<FilaTipDocLanbideVO>();
        
        AdaptadorSQLBD adaptador;
        try {
            adaptador = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
            
            try {
                String codTipDoc = (String) request.getParameter("codTipDoc");
                int codTipDocInt = 0;
            if (codTipDoc == null || codTipDoc.isEmpty()) {
                    // Obtener automáticamente el siguiente código
                    //codTipDocInt = MeLanbide68Manager.getInstance().obtenerUltimoCodTipDocValido(adaptador) +1;
            } else {
                    codTipDocInt = Integer.parseInt(codTipDoc);
                }
                String tipDocCas = (String) request.getParameter("tipDocCas");
                String tipDocEus = (String) request.getParameter("tipDocEus");
                String tipDocCasL = (String) request.getParameter("tipDocCasL");
                String tipDocEusL = (String) request.getParameter("tipDocEusL");
                
                String codGrupoTipDoc = (String) request.getParameter("codGrupo");
                String todosProcedimientos = (String) request.getParameter("todosProcedimientos");
                
                FilaTipDocLanbideVO tipoDoc = null;
                tipoDoc = new FilaTipDocLanbideVO();

                tipoDoc = MeLanbide68Manager.getInstance().getTipoDocumentalByCodigo(codTipDocInt, adaptador);
                
                //si existe dar error de duplicado
                if (tipoDoc.getTipDocID() != 0) {
                    codigoOperacion = "4";
                } else {
                // Configurar los valores del nuevo tipo documental
                    tipoDoc.setTipDocID(codTipDocInt);
                    tipoDoc.setTipDocLanbide_es(tipDocCas);
                    tipoDoc.setTipDocLanbide_eu(tipDocEus);
                    tipoDoc.setTipDocLanbide_es_L(tipDocCasL);
                    tipoDoc.setTipDocLanbide_eu_L(tipDocEusL);
                    
                    tipoDoc.setCodGrupo(codGrupoTipDoc);
                    
                    //FilaTipDocLanbideVO tipDocInsertado = null;
                    //tipDocInsertado = new FilaTipDocLanbideVO();
                    //tipDocInsertado = MeLanbide68Manager.getInstance().guardarTipDocLanbideVO(codOrganizacion, tipoDoc, adaptador);
                    //int idInsertado = tipDocInsertado.getId();
                    //log.debug("El ID insertado de tipo documental es: " + idInsertado);
                    
                    //Insertar en la tabla de relación Lanbide-DOKUSI                
                    //FilaTipDocDokusiVO tipoDocDokusi = new FilaTipDocDokusiVO();
                    String tipDocDokusi = (String) request.getParameter("tipDocDokusi");
                    
                    //tipoDocDokusi.setCodTipDoc(idInsertado);
                    //tipoDocDokusi.setTipDocDokusi(tipDocDokusi);
                    //MeLanbide68Manager.getInstance().guardarTipDocDokusiVO(codOrganizacion, tipoDocDokusi, adaptador);
                    
                    //guardar en ambas tablas relacionadas
                MeLanbide68Manager.getInstance().guardarTipDocLanbideVO(codOrganizacion, tipoDoc, tipDocDokusi, Boolean.parseBoolean(todosProcedimientos), adaptador);
                    
                // Recuperar la lista actualizada de tipos documentales
                    tiposDocumentales = MeLanbide68Manager.getInstance().getListaTiposDocumentales(adaptador);
                }
            } catch (Exception ex) {
            codigoOperacion = "2"; // Código de error general
            log.error("Error al guardar el tipo documental", ex);
            }
        } catch (SQLException ex) {
            java.util.logging.Logger.getLogger(MELANBIDE68.class.getName()).log(Level.SEVERE, null, ex);
        }

    // Escribir la respuesta con el estado de la operación y la lista actualizada
        escribirListatiposDocumentalesRequest(codigoOperacion, tiposDocumentales, response);
    }
 public void guardarInsertarEnTodos(int codOrganizacion, int codTram, int ocurrenciaTram, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        String codigoOperacion = "0";
       List<String> mensajes =  null;
        

    AdaptadorSQLBD adaptador;
    try {
        adaptador = this.getAdaptSQLBD(String.valueOf(codOrganizacion));

        try {
                String idTipoDocumental = (String) request.getParameter("idTipoDocumental");
            int codTipDocInt = 0;
            if (idTipoDocumental == null || idTipoDocumental.isEmpty()) {
                    // Obtener automáticamente el siguiente código
                    //codTipDocInt = MeLanbide68Manager.getInstance().obtenerUltimoCodTipDocValido(adaptador) +1;
            } else {
                codTipDocInt = Integer.parseInt(idTipoDocumental);
            }
                

            
                mensajes =  MeLanbide68Manager.getInstance().insertarEnTodos(codOrganizacion, codTipDocInt, adaptador);
                
            
        } catch (Exception ex) {
            codigoOperacion = "2"; // Código de error general
            log.error("Error al guardar el tipo documental", ex);
        }
    } catch (SQLException ex) {
        java.util.logging.Logger.getLogger(MELANBIDE68.class.getName()).log(Level.SEVERE, null, ex);
    }

    // Escribir la respuesta con el estado de la operación y la lista actualizada
    escribirListaMensajesRequest(codigoOperacion, mensajes, response);
}
    
    public String cargarPantallaRelacLanDOKUSI(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) throws Exception {
        log.debug("Entramos en cargarPantallaRelacLanDOKUSI de " + this.getClass().getName());
        AdaptadorSQLBD adapt = null;
        try {
            adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
        } catch (Exception ex) {
            log.error(this.getClass().getName() + " Error al recuperar el adaptador getAdaptSQLBD ", ex);
        }
        String url = "/jsp/extension/melanbide68/gestiones/relacionTiposDocumentales.jsp";

        if (adapt != null) {
            log.error("adapt != null");

            cargarTablaTiposDocumentalesDOKUSI(adapt, request);
        } else {
            log.error("adapt == null");
        }
        return url;
    }

    private void cargarTablaTiposDocumentalesDOKUSI(AdaptadorSQLBD adapt, HttpServletRequest request) {
        try {
            List<FilaTipDocDokusiVO> tiposDocumentalesDokusi = MeLanbide68Manager.getInstance().getListaTiposDocumentalesDokusi(adapt);
            request.setAttribute("tiposDocumentalesDokusi", tiposDocumentalesDokusi);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void cargarEliminarTipDocDokusi(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        String codigoOperacion = "0";
        List<FilaTipDocDokusiVO> tiposDocumentalesDokusi = new ArrayList();

        AdaptadorSQLBD adaptador = null;
        try {
            adaptador = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
        } catch (SQLException ex) {
            java.util.logging.Logger.getLogger(MELANBIDE68.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            String codTipDoc = request.getParameter("codTipDoc");

            int codTipDocInt = 0;
            if (codTipDoc != null) {
                codTipDocInt = Integer.parseInt(codTipDoc);
            }

            FilaTipDocDokusiVO tipDocDokusi = new FilaTipDocDokusiVO();
            tipDocDokusi.setCodTipDoc(codTipDocInt);

            tipDocDokusi = MeLanbide68Manager.getInstance().getTipoDocumentalDokusi(codTipDocInt, adaptador);

            if (tipDocDokusi.getCodTipDoc() == 0) {
                codigoOperacion = "3";
            } else {
                MeLanbide68Manager.getInstance().eliminarTipDocDokusiVO(codOrganizacion, tipDocDokusi, adaptador);
            }
        } catch (Exception ex) {
            codigoOperacion = "2";
            ex.printStackTrace();
        } finally {
            try {
                tiposDocumentalesDokusi = MeLanbide68Manager.getInstance().getListaTiposDocumentalesDokusi(adaptador);
            } catch (Exception ex) {
                codigoOperacion = "2";
                ex.printStackTrace();
            }
            escribirListatiposDocumentalesDokusiRequest(codigoOperacion, tiposDocumentalesDokusi, response);
        }
    }

    private void escribirListatiposDocumentalesDokusiRequest(String codigoOperacion, List<FilaTipDocDokusiVO> tiposDocumentalesDokusi, HttpServletResponse response) {
        StringBuffer xmlSalida = new StringBuffer();
        xmlSalida.append("<RESPUESTA>");
        xmlSalida.append("<CODIGO_OPERACION>");
        xmlSalida.append(codigoOperacion);
        xmlSalida.append("</CODIGO_OPERACION>");

        for (FilaTipDocDokusiVO tipDocDokusi : tiposDocumentalesDokusi) {
            xmlSalida.append("<FILA>");
            xmlSalida.append("<COD_TIPDOC>");
            xmlSalida.append(tipDocDokusi.getCodTipDoc());
            xmlSalida.append("</COD_TIPDOC>");
            xmlSalida.append("<TIPDOC_LANBIDE>");
            xmlSalida.append(tipDocDokusi.getDescTipDoc());
            xmlSalida.append("</TIPDOC_LANBIDE>");
            xmlSalida.append("<TIPDOC_DOKUSI>");
            xmlSalida.append(tipDocDokusi.getTipDocDokusi());
            xmlSalida.append("</TIPDOC_DOKUSI>");
            xmlSalida.append("</FILA>");
        }
        xmlSalida.append("</RESPUESTA>");
        try {
            response.setContentType("text/xml");
            response.setCharacterEncoding("UTF-8");
            PrintWriter out = response.getWriter();
            out.print(xmlSalida.toString());
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }//try-catch
    }

    public String cargarNuevoTipDocDokusi(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        cargarComboNuevoTipDoc(codOrganizacion, request);
        cargarComboNuevoTipDokusi(codOrganizacion, request);
        return "/jsp/extension/melanbide68/gestiones/nuevaRelacionDocumental.jsp?codOrganizacionModulo=" + codOrganizacion;
    }

    private void cargarComboNuevoTipDoc(int codOrganizacion, HttpServletRequest request) {
        log.debug("Entramos en cargarComboNuevoTipDoc de " + this.getClass().getName());
        AdaptadorSQLBD adapt = null;
        try {
            adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
        } catch (Exception ex) {
            log.error(this.getClass().getName() + " Error al recuperar el adaptador getAdaptSQLBD ", ex);
        }

        if (adapt != null) {
            try {
                List<TipDocLanbideVO> listaTiposDoc = MeLanbide68Manager.getInstance().getTiposDoc(adapt);
                if (listaTiposDoc.size() > 0) {
                    request.setAttribute("listaTipDoc", listaTiposDoc);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                log.error("Error al recueperar los datos De Acceso - MELANBIDE68 - cargarComboNuevoTipDoc", ex);
            }
        }
    }

    private void cargarComboNuevoTipDokusi(int codOrganizacion, HttpServletRequest request) {
        log.debug("Entramos en cargarComboNuevoTipDokusi de " + this.getClass().getName());
        AdaptadorSQLBD adapt = null;
        try {
            adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
        } catch (Exception ex) {
            log.error(this.getClass().getName() + " Error al recuperar el adaptador getAdaptSQLBD ", ex);
        }

        if (adapt != null) {
            try {
                List<TipDokusiVO> listaTiposDokusi = MeLanbide68Manager.getInstance().getTiposDokusi(adapt);
                if (listaTiposDokusi.size() > 0) {
                    request.setAttribute("listaTipDokusi", listaTiposDokusi);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                log.error("Error al recueperar los datos De Acceso - MELANBIDE68 - cargarComboNuevoTipDokusi", ex);
            }
        }
    }
    
    private void cargarComboNuevoGruposTipDoc(int codOrganizacion, HttpServletRequest request) {
        log.debug("Entramos en cargarComboNuevoGruposTipDoc de " + this.getClass().getName());
        AdaptadorSQLBD adapt = null;
        try {
            adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
        } catch (Exception ex) {
            log.error(this.getClass().getName() + " Error al recuperar el adaptador getAdaptSQLBD ", ex);
        }

        if (adapt != null) {
            try {
                List<GrupoTipDocVO> listaGruposTipDoc = MeLanbide68Manager.getInstance().getGruposTipDoc(adapt);
                if (listaGruposTipDoc.size() > 0) {
                    request.setAttribute("listaGruposTipDoc", listaGruposTipDoc);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                log.error("Error al recueperar los datos De Acceso - MELANBIDE68 - cargarComboNuevoGruposTipDoc", ex);
            }
        }
    }

    public String cargarModificarTipDocDokusi(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        List<TipDocLanbideVO> listaTiposDoc = new ArrayList<TipDocLanbideVO>();
        TipDocLanbideVO tipDoc = new TipDocLanbideVO();

        String codTipDoc = (String) request.getParameter("codTipDoc");
        int codTipDocInt = 0;
        if (codTipDoc != null) {
            codTipDocInt = Integer.parseInt(codTipDoc);
        }
        tipDoc.setCodTipDoc(codTipDocInt);
        tipDoc.setDescTipDoc(request.getParameter("tipDocLanbide"));
        listaTiposDoc.add(tipDoc);
        if (listaTiposDoc.size() > 0) {
            request.setAttribute("listaTipDoc", listaTiposDoc);
        }
        //Recoger la descripcion tipo dokusi en castellano
        String tipoDokusi = "";

        AdaptadorSQLBD adaptador = null;
        try {
            adaptador = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
        } catch (SQLException ex) {
            java.util.logging.Logger.getLogger(MELANBIDE68.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            String codTipDokusi = request.getParameter("tipDocDokusi");
            FilaTipDokusiVO tipDokusi = new FilaTipDokusiVO();
            tipDokusi = MeLanbide68Manager.getInstance().getTipoDokusi(codTipDokusi, adaptador);
            tipoDokusi = tipDokusi.getDesDokusi_es();
            log.debug(tipoDokusi);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        cargarComboNuevoTipDokusi(codOrganizacion, request);
        return "/jsp/extension/melanbide68/gestiones/nuevaRelacionDocumental.jsp?codOrganizacionModulo=" + codOrganizacion
                + "&codTipDoc=" + request.getParameter("codTipDoc")
                + "&tipDocLanbide=" + request.getParameter("tipDocLanbide")
                + "&tipDocDokusi=" + request.getParameter("tipDocDokusi")
                + "&tipDokusi=" + tipoDokusi;
    }

    public void modificarTipDocDokusi(int codOrganizacion, int codTram, int ocurrenciaTram, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        String codigoOperacion = "0";
        FilaTipDocDokusiVO tipoDocDokusi = new FilaTipDocDokusiVO();
        List<FilaTipDocDokusiVO> tiposDocumentalesDokusi = new ArrayList();
        try {
            AdaptadorSQLBD adaptador = null;
            try {
                adaptador = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
            } catch (SQLException ex) {
                java.util.logging.Logger.getLogger(MELANBIDE68.class.getName()).log(Level.SEVERE, null, ex);
            }

            String codTipDoc = (String) request.getParameter("codTipDoc");
            int codTipDocInt = 0;
            if (codTipDoc != null) {
                codTipDocInt = Integer.parseInt(codTipDoc);
            }
            String tipDocDokusi = (String) request.getParameter("tipDocDokusi");

            tipoDocDokusi = MeLanbide68Manager.getInstance().getTipoDocumentalDokusi(codTipDocInt, adaptador);

            //  if(tipoDoc.getTipDocID == null)
            if (tipoDocDokusi.getCodTipDoc() == 0) {
                codigoOperacion = "3";
            } else {
                tipoDocDokusi.setTipDocDokusi(tipDocDokusi);
                MeLanbide68Manager.getInstance().modificarTipDocDokusiVO(codOrganizacion, tipoDocDokusi, adaptador);
            }

            tiposDocumentalesDokusi = MeLanbide68Manager.getInstance().getListaTiposDocumentalesDokusi(adaptador);

            escribirListatiposDocumentalesDokusiRequest(codigoOperacion, tiposDocumentalesDokusi, response);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void guardarTipDocDokusi(int codOrganizacion, int codTram, int ocurrenciaTram, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        String codigoOperacion = "0";
        List<FilaTipDocDokusiVO> tiposDocumentalesDokusi = new ArrayList<FilaTipDocDokusiVO>();

        AdaptadorSQLBD adaptador;
        try {
            adaptador = this.getAdaptSQLBD(String.valueOf(codOrganizacion));

            try {
                String codTipDoc = (String) request.getParameter("codTipDoc");
                int codTipDocInt = 0;
                if (codTipDoc != null) {
                    codTipDocInt = Integer.parseInt(codTipDoc);
                }
                String tipDocDokusi = (String) request.getParameter("tipDocDokusi");

                FilaTipDocDokusiVO tipoDocDokusi = null;
                tipoDocDokusi = new FilaTipDocDokusiVO();

                tipoDocDokusi = MeLanbide68Manager.getInstance().getTipoDocumentalDokusi(codTipDocInt, adaptador);

                //si existe dar error de duplicado
                if (tipoDocDokusi.getCodTipDoc() != 0) {
                    codigoOperacion = "4";
                } else {
                    tipoDocDokusi.setCodTipDoc(codTipDocInt);
                    tipoDocDokusi.setTipDocDokusi(tipDocDokusi);
                    MeLanbide68Manager.getInstance().guardarTipDocDokusiVO(codOrganizacion, tipoDocDokusi, adaptador);

                    tiposDocumentalesDokusi = MeLanbide68Manager.getInstance().getListaTiposDocumentalesDokusi(adaptador);
                }
            } catch (SQLException e) {
                //Si no existe el registro con el mismo código en la tabla de tipos documentales de Lanbide           
                if (e.getErrorCode() == 2291) {
                    codigoOperacion = "5";
                } else {
                    codigoOperacion = "2";
                }
                e.printStackTrace();
            } catch (Exception ex) {
                codigoOperacion = "2";
                ex.printStackTrace();
            }
        } catch (SQLException ex) {
            java.util.logging.Logger.getLogger(MELANBIDE68.class.getName()).log(Level.SEVERE, null, ex);
        }
        escribirListatiposDocumentalesDokusiRequest(codigoOperacion, tiposDocumentalesDokusi, response);
    }

    public String cargarPantallaMetadatos(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) throws Exception {
        log.debug("Entramos en cargarPantallaMetadatos de " + this.getClass().getName());
        AdaptadorSQLBD adapt = null;
        try {
            adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
        } catch (Exception ex) {
            log.error(this.getClass().getName() + " Error al recuperar el adaptador getAdaptSQLBD ", ex);
        }
        String url = "/jsp/extension/melanbide68/gestiones/metadatos.jsp";

        if (adapt != null) {
            log.error("adapt != null");
            try {
                List<TipDocLanbideVO> listaTiposDoc = MeLanbide68Manager.getInstance().getTiposDoc(adapt);
                if (listaTiposDoc.size() > 0) {
                    request.setAttribute("listaTipDoc", listaTiposDoc);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                log.error("Error al recueperar los datos De Acceso - MELANBIDE68 - cargarPantallaMetadatos", ex);
            }
        } else {
            log.error("adapt == null");
        }
        return url;
    }

    public void cargarTablaMetadatos(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) throws Exception {
        String codTipDoc = (String) request.getParameter("codTipDoc");
        int codTipDocInt = 0;
        if (codTipDoc != null) {
            codTipDocInt = Integer.parseInt(codTipDoc);
        }
        String codigoOperacion = "1";
        List<FilaMetadatoVO> lista = (List<FilaMetadatoVO>) MeLanbide68Manager.getInstance().getListaMetadatosporTipodocumental(codTipDocInt, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));

        if (lista.size() > 0) {
            codigoOperacion = "0";
        }

        StringBuffer xmlSalida = new StringBuffer();
        xmlSalida.append("<RESPUESTA>");
        xmlSalida.append("<CODIGO_OPERACION>");
        xmlSalida.append(codigoOperacion);
        xmlSalida.append("</CODIGO_OPERACION>");

        if (lista.size() > 0) {
            for (FilaMetadatoVO metadato : lista) {
                xmlSalida.append("<FILA>");
                xmlSalida.append("<METADATO>");
                xmlSalida.append(metadato.getMetadato());
                xmlSalida.append("</METADATO>");
                xmlSalida.append("<METADATODCTM>");
                xmlSalida.append(metadato.getMetadatoDCTM());
                xmlSalida.append("</METADATODCTM>");
                xmlSalida.append("<OBLIGATORIO>");
                xmlSalida.append(metadato.getObligatorio());
                xmlSalida.append("</OBLIGATORIO>");
                xmlSalida.append("<DESHABILITADO>");
                xmlSalida.append(metadato.getDeshabilitado());
                xmlSalida.append("</DESHABILITADO>");
                xmlSalida.append("</FILA>");
            }
        }

        xmlSalida.append("</RESPUESTA>");
        try {
            response.setContentType("text/xml");
            response.setCharacterEncoding("UTF-8");
            PrintWriter out = response.getWriter();
            out.print(xmlSalida.toString());
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }   //try-catch
    }

    public void cargarEliminarMetadato(int codOrganizacion, int codTram, int ocurrenciaTram, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        String codigoOperacion = "0";
        List<FilaMetadatoVO> metadatos = new ArrayList();

        AdaptadorSQLBD adaptador = null;
        try {
            adaptador = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
        } catch (SQLException ex) {
            java.util.logging.Logger.getLogger(MELANBIDE68.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            String codTipDoc = request.getParameter("codTipDoc");
            int codTipDocInt = 0;
            if (codTipDoc != null) {
                codTipDocInt = Integer.parseInt(codTipDoc);
            }
            String codMetadato = request.getParameter("codMetadato");

            MetadatoVO metadato = new MetadatoVO();
            metadato.setTipoDocumental(codTipDocInt);
            metadato.setMetadato(codMetadato);

            metadato = MeLanbide68Manager.getInstance().getTipodocumentalyMetadato(codTipDocInt, codMetadato, adaptador);

            if (metadato.getTipoDocumental() == 0) {
                codigoOperacion = "3";
            } else {
                MeLanbide68Manager.getInstance().eliminarMetadatoVO(codOrganizacion, metadato, adaptador);
            }

            metadatos = MeLanbide68Manager.getInstance().getListaMetadatosporTipodocumental(codTipDocInt, adaptador);
        } catch (Exception ex) {
            codigoOperacion = "2";
            ex.printStackTrace();
        }
        escribirListaMetadatosRequest(codigoOperacion, metadatos, response);
    }

    private void escribirListaMetadatosRequest(String codigoOperacion, List<FilaMetadatoVO> metadatos, HttpServletResponse response) {
        StringBuffer xmlSalida = new StringBuffer();
        xmlSalida.append("<RESPUESTA>");
        xmlSalida.append("<CODIGO_OPERACION>");
        xmlSalida.append(codigoOperacion);
        xmlSalida.append("</CODIGO_OPERACION>");

        for (FilaMetadatoVO metadato : metadatos) {
            xmlSalida.append("<FILA>");
            xmlSalida.append("<METADATO>");
            xmlSalida.append(metadato.getMetadato());
            xmlSalida.append("</METADATO>");
            xmlSalida.append("<METADATODCTM>");
            xmlSalida.append(metadato.getMetadatoDCTM());
            xmlSalida.append("</METADATODCTM>");
            xmlSalida.append("<OBLIGATORIO>");
            xmlSalida.append(metadato.getObligatorio());
            xmlSalida.append("</OBLIGATORIO>");
            xmlSalida.append("<DESHABILITADO>");
            xmlSalida.append(metadato.getDeshabilitado());
            xmlSalida.append("</DESHABILITADO>");
            xmlSalida.append("</FILA>");
        }

        xmlSalida.append("</RESPUESTA>");
        try {
            response.setContentType("text/xml");
            response.setCharacterEncoding("UTF-8");
            PrintWriter out = response.getWriter();
            out.print(xmlSalida.toString());
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void cargarDeshabilitarMetadato(int codOrganizacion, int codTram, int ocurrenciaTram, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        String codigoOperacion = "0";
        List<FilaMetadatoVO> metadatos = new ArrayList();

        AdaptadorSQLBD adaptador = null;
        try {
            adaptador = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
        } catch (SQLException ex) {
            java.util.logging.Logger.getLogger(MELANBIDE68.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            String codTipDoc = request.getParameter("codTipDoc");
            int codTipDocInt = 0;
            if (codTipDoc != null) {
                codTipDocInt = Integer.parseInt(codTipDoc);
            }
            String codMetadato = java.net.URLDecoder.decode(request.getParameter("codMetadato"),"iso-8859-15");
                    //request.getParameter("codMetadato");

            MetadatoVO metadato = new MetadatoVO();
            metadato.setTipoDocumental(codTipDocInt);
            metadato.setMetadato(codMetadato);

            metadato = MeLanbide68Manager.getInstance().getTipodocumentalyMetadato(codTipDocInt, codMetadato, adaptador);

            if (metadato.getTipoDocumental() == 0) {
                codigoOperacion = "3";
            } else {
                MeLanbide68Manager.getInstance().deshabilitarMetadatoVO(codOrganizacion, metadato, adaptador);
            }

            metadatos = MeLanbide68Manager.getInstance().getListaMetadatosporTipodocumental(codTipDocInt, adaptador);
        } catch (Exception ex) {
            codigoOperacion = "2";
            ex.printStackTrace();
        }
        escribirListaMetadatosRequest(codigoOperacion, metadatos, response);
    }

    public String cargarNuevoMetadato(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        return "/jsp/extension/melanbide68/gestiones/nuevoMetadato.jsp?codOrganizacionModulo=" + codOrganizacion;
    }

    public String cargarModificarMetadato(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        return "/jsp/extension/melanbide68/gestiones/nuevoMetadato.jsp?codOrganizacionModulo=" + codOrganizacion
                + "&codTipDoc=" + request.getParameter("codTipDoc")
                + "&codMetadato=" + request.getParameter("codMetadato")
                + "&metadatoDCTM=" + request.getParameter("metadatoDCTM")
                + "&obligatorio=" + request.getParameter("obligatorio");
    }

    public void modificarMetadato(int codOrganizacion, int codTram, int ocurrenciaTram, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        String codigoOperacion = "0";
        MetadatoVO metadato = new MetadatoVO();
        List<FilaMetadatoVO> metadatos = new ArrayList();
        try {
            AdaptadorSQLBD adaptador = null;
            try {
                adaptador = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
            } catch (SQLException ex) {
                java.util.logging.Logger.getLogger(MELANBIDE68.class.getName()).log(Level.SEVERE, null, ex);
            }

            String codTipDoc = (String) request.getParameter("codTipDoc");
            int codTipDocInt = 0;
            if (codTipDoc != null) {
                codTipDocInt = Integer.parseInt(codTipDoc);
            }
            String codMetadato = (String) request.getParameter("codMetadato");
            String metadatoDCTM = (String) request.getParameter("metadatoDCTM");
            String obligatorio = (String) request.getParameter("obligatorio").toUpperCase();
            if (obligatorio.equals("S")) {
                obligatorio = "1";
            } else {
                obligatorio = "0";
            }

            metadato = MeLanbide68Manager.getInstance().getTipodocumentalyMetadato(codTipDocInt, codMetadato, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));

            if (metadato.getTipoDocumental() == 0) {
                codigoOperacion = "3";
            } else {
                metadato.setMetadato(codMetadato);
                metadato.setMetadatoDCTM(metadatoDCTM);
                metadato.setObligatorio(obligatorio);
                MeLanbide68Manager.getInstance().modificarMetadatoVO(codOrganizacion, metadato, adaptador);

                metadatos = MeLanbide68Manager.getInstance().getListaMetadatosporTipodocumental(codTipDocInt, adaptador);

            }

            //         metadatos = MeLanbide68Manager.getInstance().getListaMetadatosporTipodocumental(codTipDocInt, adaptador);
        } catch (Exception ex) {
            codigoOperacion = "5";
            ex.printStackTrace();
        }
        escribirListaMetadatosRequest(codigoOperacion, metadatos, response);
    }

    public void guardarMetadato(int codOrganizacion, int codTram, int ocurrenciaTram, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        String codigoOperacion = "0";
        List<FilaMetadatoVO> metadatos = new ArrayList<FilaMetadatoVO>();

        AdaptadorSQLBD adaptador;
        try {
            adaptador = this.getAdaptSQLBD(String.valueOf(codOrganizacion));

            try {
                String codTipDoc = (String) request.getParameter("codTipDoc");
                int codTipDocInt = 0;
                if (codTipDoc != null) {
                    codTipDocInt = Integer.parseInt(codTipDoc);
                }
                String codMetadato = (String) request.getParameter("codMetadato");
                String metadatoDCTM = (String) request.getParameter("metadatoDCTM");
                String obligatorio = (String) request.getParameter("obligatorio").toUpperCase();
                if (obligatorio.equals("S")) {
                    obligatorio = "1";
                } else {
                    obligatorio = "0";
                }

                MetadatoVO metadato = null;

                metadato = new MetadatoVO();
                metadato = MeLanbide68Manager.getInstance().getTipodocumentalyMetadato(codTipDocInt, codMetadato, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));

                //si existe dar error
                if (metadato.getTipoDocumental() != 0) {
                    codigoOperacion = "4";
                } else {
                    metadato.setTipoDocumental(codTipDocInt);
                    metadato.setMetadato(codMetadato);
                    metadato.setMetadatoDCTM(metadatoDCTM);
                    metadato.setObligatorio(obligatorio);

                    MeLanbide68Manager.getInstance().guardarMetadatoVO(codOrganizacion, metadato, adaptador);
                }
                metadatos = MeLanbide68Manager.getInstance().getListaMetadatosporTipodocumental(codTipDocInt, adaptador);
            } catch (Exception ex) {
                codigoOperacion = "2";
                ex.printStackTrace();
            }

        } catch (SQLException ex) {
            java.util.logging.Logger.getLogger(MELANBIDE68.class.getName()).log(Level.SEVERE, null, ex);
        }
        escribirListaMetadatosRequest(codigoOperacion, metadatos, response);
    }

    public String cargarPantallaTiposDocDOKUSI(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) throws Exception {
        log.debug("Entramos en cargarPantallaTiposDocDOKUSI de " + this.getClass().getName());
        AdaptadorSQLBD adapt = null;
        try {
            adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
        } catch (Exception ex) {
            log.error(this.getClass().getName() + " Error al recuperar el adaptador getAdaptSQLBD ", ex);
        }
        String url = "/jsp/extension/melanbide68/gestiones/tiposDocumentalesDokusi.jsp";

        if (adapt != null) {
            log.error("adapt != null");

            cargarTablaTiposDokusi(adapt, request);
        } else {
            log.error("adapt == null");
        }
        return url;
    }

    private void cargarTablaTiposDokusi(AdaptadorSQLBD adapt, HttpServletRequest request) {
        try {
            List<FilaTipDokusiVO> tiposDokusi = MeLanbide68Manager.getInstance().getListaTiposDokusi(adapt);
            request.setAttribute("tiposDokusi", tiposDokusi);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void cargarEliminarTipDokusi(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        String codigoOperacion = "0";
        List<FilaTipDokusiVO> tiposDokusi = new ArrayList();

        AdaptadorSQLBD adaptador = null;
        try {
            adaptador = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
        } catch (SQLException ex) {
            java.util.logging.Logger.getLogger(MELANBIDE68.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            String codTipDokusi = request.getParameter("codTipDokusi");
            FilaTipDokusiVO tipDokusi = new FilaTipDokusiVO();
            tipDokusi.setCodDokusi(codTipDokusi);

            tipDokusi = MeLanbide68Manager.getInstance().getTipoDokusi(codTipDokusi, adaptador);

            if (tipDokusi.getCodDokusi() == null) {
                codigoOperacion = "3";
            } else {
                MeLanbide68Manager.getInstance().eliminarTipDokusiVO(codOrganizacion, tipDokusi, adaptador);
            }

        } catch (Exception ex) {
            codigoOperacion = "2";
            ex.printStackTrace();
        } finally {
            try {
                tiposDokusi = MeLanbide68Manager.getInstance().getListaTiposDokusi(adaptador);
            } catch (Exception ex) {
                codigoOperacion = "2";
                ex.printStackTrace();
            }
            escribirListatiposDokusiRequest(codigoOperacion, tiposDokusi, response);
        }
    }

    private void escribirListatiposDokusiRequest(String codigoOperacion, List<FilaTipDokusiVO> tiposDokusi, HttpServletResponse response) {
        StringBuffer xmlSalida = new StringBuffer();
        xmlSalida.append("<RESPUESTA>");
        xmlSalida.append("<CODIGO_OPERACION>");
        xmlSalida.append(codigoOperacion);
        xmlSalida.append("</CODIGO_OPERACION>");

        for (FilaTipDokusiVO tipDokusi : tiposDokusi) {
            xmlSalida.append("<FILA>");
            xmlSalida.append("<COD_TIPDOC>");
            xmlSalida.append(tipDokusi.getCodTipDoc());
            xmlSalida.append("</COD_TIPDOC>");
            xmlSalida.append("<COD_DOKUSI>");
            xmlSalida.append(tipDokusi.getCodDokusi());
            xmlSalida.append("</COD_DOKUSI>");
            xmlSalida.append("<DOKUSI_ES>");
            xmlSalida.append(tipDokusi.getDesDokusi_es());
            xmlSalida.append("</DOKUSI_ES>");
            xmlSalida.append("<DOKUSI_EU>");
            xmlSalida.append(tipDokusi.getDesDokusi_eu());
            xmlSalida.append("</DOKUSI_EU>");
            xmlSalida.append("<TIPDOC_DOKUSI_PADRE>");
            xmlSalida.append(tipDokusi.getCodDokusiPadre() );
            xmlSalida.append("</TIPDOC_DOKUSI_PADRE>");
            xmlSalida.append("<DOKUSI_FAMILIA>");
            xmlSalida.append(tipDokusi.getCodDokusiFamilia());
            xmlSalida.append("</DOKUSI_FAMILIA>");
            xmlSalida.append("</FILA>");
        }
        xmlSalida.append("</RESPUESTA>");
        try {
            response.setContentType("text/xml");
            response.setCharacterEncoding("UTF-8");
            PrintWriter out = response.getWriter();
            out.print(xmlSalida.toString());
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }//try-catch
    }

    public String cargarNuevoTipDokusi(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        return "/jsp/extension/melanbide68/gestiones/nuevoTipDokusi.jsp?codOrganizacionModulo=" + codOrganizacion;
    }

    public String cargarModificarTipDokusi(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        return "/jsp/extension/melanbide68/gestiones/nuevoTipDokusi.jsp?codOrganizacionModulo=" + codOrganizacion
                + "&codTipDoc=" + request.getParameter("codTipDoc")               
                + "&codDokusi=" + request.getParameter("codDokusi")
                + "&codDokusiPadre=" + request.getParameter("codDokusiPadre")
                + "&familiaDokusi=" + request.getParameter("familiaDokusi")
                + "&tipDokusiCas=" + request.getParameter("tipDokusiCas")
                + "&tipDokusiEus=" + request.getParameter("tipDokusiEus");
    }

  public void modificarTipDokusi(int codOrganizacion, int codTram, int ocurrenciaTram,
                               String numExpediente,
                               HttpServletRequest request,
                               HttpServletResponse response) {

        log.debug("Entramos en modificarTipDokusi de " + this.getClass().getName());
        String codigoOperacion = "0";
        FilaTipDokusiVO tipoDokusi = new FilaTipDokusiVO();
    List<FilaTipDokusiVO> tiposDokusi = new ArrayList<FilaTipDokusiVO>();

        try {
            AdaptadorSQLBD adaptador = null;
            try {
                adaptador = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
            } catch (SQLException ex) {
            java.util.logging.Logger.getLogger(MELANBIDE68.class.getName())
                                   .log(Level.SEVERE, null, ex);
            }

        // 1) Recuperamos parámetros del request
        String codTipDoc        = request.getParameter("codTipDoc");
        String codDokusi        = request.getParameter("codDokusi");       
        String tipDokusiCas     = request.getParameter("tipDokusiCas");
        String tipDokusiEus     = request.getParameter("tipDokusiEus");
        String codDokusiPadre   = request.getParameter("codDokusiPadre");
        String familiaDokusi    = request.getParameter("familiaDokusi");

            Integer familiaDokusiInt = null;
        if (familiaDokusi != null && !"".equals(familiaDokusi)) {
            familiaDokusiInt = Integer.valueOf(familiaDokusi);
            }

        // 2) Obtenemos el registro actual de la BD
            tipoDokusi = MeLanbide68Manager.getInstance().getTipoDokusi(codDokusi, adaptador);

        // 3) Si no existe en BD, marcamos código de error
            if (tipoDokusi.getCodDokusi() == null) {
                codigoOperacion = "3";
            } else {
        

            // 5) Solo sobreescribimos DOKUSI_ES si estaba vacío en la BD
            if (tipoDokusi.getDesDokusi_es() == null
                || "".equals(tipoDokusi.getDesDokusi_es().trim())) {
                tipoDokusi.setDesDokusi_es(tipDokusiCas);
            }

            // 6) Solo sobreescribimos DOKUSI_EU si estaba vacío en la BD
            if (tipoDokusi.getDesDokusi_eu() == null
                || "".equals(tipoDokusi.getDesDokusi_eu().trim())) {
                tipoDokusi.setDesDokusi_eu(tipDokusiEus);
      }
// 4) Actualizamos los campos principales (padre, familia, etc.)
        tipoDokusi.setCodDokusiPadre(codDokusiPadre);
        tipoDokusi.setCodDokusiFamilia(familiaDokusiInt);
            // 7) Guardamos cambios en BD
                MeLanbide68Manager.getInstance().modificarTipDokusiVO(codOrganizacion, tipoDokusi, adaptador);
            }

        // 8) Recargamos la lista completa y la devolvemos
            tiposDokusi = MeLanbide68Manager.getInstance().getListaTiposDokusi(adaptador);

            escribirListatiposDokusiRequest(codigoOperacion, tiposDokusi, response);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void guardarTipDokusi(int codOrganizacion, int codTram, int ocurrenciaTram, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.debug("Entramos en guardarTipDokusi de " + this.getClass().getName());
        String codigoOperacion = "0";
        List<FilaTipDokusiVO> tiposDokusi = new ArrayList<FilaTipDokusiVO>();

        AdaptadorSQLBD adaptador;
        try {
            adaptador = this.getAdaptSQLBD(String.valueOf(codOrganizacion));

            try {
                String codDokusi = (String) request.getParameter("codDokusi");
                String codDokusiPadre = (String) request.getParameter("codDokusiPadre");
                String familiaDokusi = (String) request.getParameter("familiaDokusi");
                Integer familiaDokusiInt = null;
                if (!"".equals(familiaDokusi)) {
                    familiaDokusiInt = Integer.parseInt(familiaDokusi);
                }
                String tipDokusiCas = (String) request.getParameter("tipDokusiCas");
                String tipDokusiEus = (String) request.getParameter("tipDokusiEus");

                FilaTipDokusiVO tipoDokusi = null;
                tipoDokusi = new FilaTipDokusiVO();

                tipoDokusi = MeLanbide68Manager.getInstance().getTipoDokusi(codDokusi, adaptador);

                //si existe dar error de duplicado
                if (tipoDokusi.getCodDokusi() != null) {
                    codigoOperacion = "4";
                } else {
                    tipoDokusi.setCodDokusi(codDokusi);
                    tipoDokusi.setCodDokusiPadre(codDokusiPadre);
                    tipoDokusi.setCodDokusiFamilia(familiaDokusiInt);
                    tipoDokusi.setDesDokusi_es(tipDokusiCas);
                    tipoDokusi.setDesDokusi_eu(tipDokusiEus);
                    MeLanbide68Manager.getInstance().guardarTipDokusiVO(codOrganizacion, tipoDokusi, adaptador);

                    tiposDokusi = MeLanbide68Manager.getInstance().getListaTiposDokusi(adaptador);
                }
            } catch (Exception ex) {
                codigoOperacion = "2";
                ex.printStackTrace();
            }
        } catch (SQLException ex) {
            java.util.logging.Logger.getLogger(MELANBIDE68.class.getName()).log(Level.SEVERE, null, ex);
        }
        escribirListatiposDokusiRequest(codigoOperacion, tiposDokusi, response);
    }

//Mantenimiento tipos documentales--Tabla de tipos documentales por procedimiento
    public String cargarPantallaTiposDocPorProcedimiento(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) throws Exception {
        log.debug("Entramos en cargarPantallaTiposDocPorProcedimiento de " + this.getClass().getName());
        AdaptadorSQLBD adapt = null;
        try {
            adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
        } catch (Exception ex) {
            log.error(this.getClass().getName() + " Error al recuperar el adaptador getAdaptSQLBD ", ex);
        }
        String url = "/jsp/extension/melanbide68/gestiones/tiposDocProcedimiento.jsp";

        if (adapt != null) {
            try {

                List<ProcedimientoVO> listaProcedimientos = MeLanbide68Manager.getInstance().getProcedimientos(this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                if (listaProcedimientos.size() > 0) {
                    request.setAttribute("listaProcedimiento", listaProcedimientos);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                log.error("Error al recueperar los datos De Acceso - MELANBIDE68 - cargarPantallaTiposDocPorProcedimiento", ex);
            }
        } else {
            log.error("adapt == null");
        }
        return url;
    }

    public void cargarTablaTiposDocProcedimiento(
            int codOrganizacion, int codTramite, int ocurrenciaTramite,
            String numExpediente, HttpServletRequest request, HttpServletResponse response
    ) throws Exception {
        String codProc = request.getParameter("codProc");
        String codigoOperacion = "1";
        List<FilaTipDocProcVO> lista = new ArrayList<FilaTipDocProcVO>();

        // Obtener tipos documentales para un procedimiento específico
        lista = MeLanbide68Manager.getInstance()
                .getListaTiposDocporProcedimiento(codProc, true, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));

        if (!lista.isEmpty()) {
            codigoOperacion = "0";
        }

        // Escribir la respuesta con los datos obtenidos
        escribirListaTiposDocRequest(codigoOperacion, lista, response);
    }

    private void escribirListaTiposDocRequest(String codigoOperacion, List<FilaTipDocProcVO> tiposDoc, HttpServletResponse response) {
        StringBuffer xmlSalida = new StringBuffer();
        xmlSalida.append("<RESPUESTA>");
        xmlSalida.append("<CODIGO_OPERACION>");
        xmlSalida.append(codigoOperacion);
        xmlSalida.append("</CODIGO_OPERACION>");

        for (FilaTipDocProcVO tipoDoc : tiposDoc) {
            xmlSalida.append("<FILA>");
             xmlSalida.append("<TIP_DOC>").append(tipoDoc.getCodTipDoc()).append("</TIP_DOC>");
             xmlSalida.append("<COD_TIPDOC>").append(tipoDoc.getCodTipDocBBDD()).append("</COD_TIPDOC>");
             xmlSalida.append("<TIPDOC_ES>").append(tipoDoc.getDescTipDoc_es()).append("</TIPDOC_ES>");
             xmlSalida.append("<TIPDOC_EU>").append(tipoDoc.getDescTipDoc_eu()).append("</TIPDOC_EU>");
             xmlSalida.append("<TIPDOC_DOKUSI>").append(tipoDoc.getTipDocDokusi()).append("</TIPDOC_DOKUSI>");
            xmlSalida.append("</FILA>");

         log.debug("Enviando XML con TIPDOC_DOKUSI -> " + tipoDoc.getTipDocDokusi());
        }

        xmlSalida.append("</RESPUESTA>");
        try {
            response.setContentType("text/xml");
            response.setCharacterEncoding("UTF-8");
            PrintWriter out = response.getWriter();
            out.print(xmlSalida.toString());
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void cargarEliminarTipoDoc(int codOrganizacion, int codTram, int ocurrenciaTram, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.debug("Entramos en cargarEliminarTipoDoc de " + this.getClass().getName());
        String codigoOperacion = "0";
        List<FilaTipDocProcVO> lista = new ArrayList();

        AdaptadorSQLBD adaptador = null;
        try {
            adaptador = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
        } catch (SQLException ex) {
            java.util.logging.Logger.getLogger(MELANBIDE68.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            String codProc = request.getParameter("codProc");
            String codTipDoc = request.getParameter("codTipDoc");
            int codTipDocInt = 0;
            try {
                if (codTipDoc != null && !codTipDoc.trim().isEmpty()) {
                codTipDocInt = Integer.parseInt(codTipDoc);
            }
            } catch (NumberFormatException e) {
                log.error("Error al convertir codTipDoc a número en eliminar: " + codTipDoc, e);
            }

            TipDocPorProcedVO tipoDocporProc = new TipDocPorProcedVO();
            tipoDocporProc.setProcedimiento(codProc);
            tipoDocporProc.setTipoDocumental(codTipDocInt);

            tipoDocporProc = MeLanbide68Manager.getInstance().getProcedimientoYTipodocumental(codProc, codTipDocInt, true, adaptador);

            if (tipoDocporProc.getTipoDocumental() == 0) {
                codigoOperacion = "3";
            } else {
                MeLanbide68Manager.getInstance().eliminarTipoDocumental(codOrganizacion, tipoDocporProc, adaptador);
            }

            lista = MeLanbide68Manager.getInstance().getListaTiposDocporProcedimiento(codProc, adaptador);
        } catch (Exception ex) {
            codigoOperacion = "2";
            ex.printStackTrace();
        }
        escribirListaTiposDocRequest(codigoOperacion, lista, response);
    }

    public String cargarNuevoTipoDoc(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        cargarComboNuevoTipDoc(codOrganizacion, request);
        return "/jsp/extension/melanbide68/gestiones/nuevoTipoDocumentalEnProcedimiento.jsp?codOrganizacionModulo=" + codOrganizacion;
    }

    public void guardarTipDocProc(int codOrganizacion, int codTram, int ocurrenciaTram, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.debug("Entramos en guardarTipDocProc de " + this.getClass().getName());
        String codigoOperacion = "0";
        List<FilaTipDocProcVO> lista = new ArrayList();

        AdaptadorSQLBD adaptador;
        try {
            adaptador = this.getAdaptSQLBD(String.valueOf(codOrganizacion));

            try {
                String codProc = (String) request.getParameter("codProc");
                String codTipDoc = (String) request.getParameter("codTipDoc");
                int codTipDocInt = 0;
                try {
                    if (codTipDoc != null && !codTipDoc.trim().isEmpty()) {
                    codTipDocInt = Integer.parseInt(codTipDoc);
                }
            } catch (NumberFormatException e) {
                log.error("Error al convertir codTipDoc a número: " + codTipDoc, e);
                codTipDocInt = 0; // O manejarlo según la lógica de negocio
            }

                TipDocPorProcedVO tipdocproced = null;

                tipdocproced = new TipDocPorProcedVO();
                tipdocproced = MeLanbide68Manager.getInstance().getProcedimientoYTipodocumental(codProc, codTipDocInt, adaptador);

                //si existe dar error
                if (tipdocproced.getTipoDocumental() != 0) {
                    codigoOperacion = "4";
                } else {
                    tipdocproced.setProcedimiento(codProc);
                    tipdocproced.setTipoDocumental(codTipDocInt);
                    //saco las descripciones en castellano y euskera                    
                    FilaTipDocLanbideVO tipDocLanbide = new FilaTipDocLanbideVO();
                    //tipDocLanbide = MeLanbide68Manager.getInstance().getTipoDocumentalByCodigo(codTipDocInt, adaptador);
                    tipDocLanbide = MeLanbide68Manager.getInstance().getTipoDocumentalById(codTipDocInt, true, adaptador);
                    if (tipDocLanbide.getTipDocID() == 0) {
                        codigoOperacion = "3";
                    } else {
                        tipdocproced.setTipoDoc_es(tipDocLanbide.getTipDocLanbide_es());
                        tipdocproced.setTipoDoc_eu(tipDocLanbide.getTipDocLanbide_eu());

                        MeLanbide68Manager.getInstance().guardarTipDocPorProcedVO(codOrganizacion, tipdocproced, adaptador);

                lista = MeLanbide68Manager.getInstance().getListaTiposDocporProcedimiento(codProc, true, adaptador);
                    }
                }
            } catch (Exception ex) {
                codigoOperacion = "2";
                ex.printStackTrace();
            }

        } catch (SQLException ex) {
            java.util.logging.Logger.getLogger(MELANBIDE68.class.getName()).log(Level.SEVERE, null, ex);
        }
        escribirListaTiposDocRequest(codigoOperacion, lista, response);
    }
    
    
    public void getExcelTiposDocumentales(int codOrganizacion, int codTram, int ocurrenciaTram,String numExpediente, HttpServletRequest request, HttpServletResponse response){
       //Recuperar los datos para descargar excel
       AdaptadorSQLBD adaptador = null;
       try{
            adaptador = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
             // 1) Obtener el idioma del usuario
        int idioma = 1; 
        HttpSession session = request.getSession(false);
        if (session != null) {
            UsuarioValueObject usuario = (UsuarioValueObject) session.getAttribute("usuario");
            if (usuario != null) {
                idioma = usuario.getIdioma();
            }
        }
            HSSFWorkbook wb = MeLanbide68Manager.getInstance().descargarExcelTiposLanbide(adaptador, idioma);
            String fechaActual = new SimpleDateFormat("ddMMyyyy").format(new Date());
            String filename = "tiposDocumentalesLanbide" + fechaActual + ".xls";
                        
            response.setContentType("application/vnd.ms-excel");
            response.setHeader("Content-Disposition", "attachment; filename=" + filename); 

            OutputStream out = response.getOutputStream();
            wb.write(out);
            out.flush();
            out.close();
            
       
       }catch(Exception e){ 
           e.printStackTrace();
           log.error("Error al recuperar el Excel con los datos de tipos documentales Lanbide - MELANBIDE68 - getExcelTiposDocumentales", e);
       }
    }
    
    public String getProcedimientosByTipoDocumental(int codOrganizacion, int codTram, int ocurrenciaTram,String numExpediente, HttpServletRequest request, HttpServletResponse response){
        AdaptadorSQLBD adaptador = null;
        try{
            Integer idTipoDocumental = Integer.parseInt((String) request.getParameter("idTipoDocumental"));
            adaptador = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
            List<ProcedimientoVO> lstProcedimientos = MeLanbide68Manager.getInstance().getProcedimientosByTipoDocumentalLanbide(adaptador, idTipoDocumental);
            request.setAttribute("lstProcedimientos", lstProcedimientos);
        }catch(Exception e){ 
           e.printStackTrace();
           log.error("Error al recuperar los procedimientos del tipo documental - MELANBIDE68 - getProcedimientosByTipoDocumental", e);
       }
        
       return "/jsp/extension/melanbide68/gestiones/procedimientosTiposDocumentales.jsp";
    }
  
 public void procesarSolicitud(int codOrganizacion, HttpServletRequest request, HttpServletResponse response) throws Exception {
    int nuevoCodTipDoc = 0;
    AdaptadorSQLBD adaptador = null;
    try {
        // Llama al método del Manager para generar el nuevo CODTIPDOC
       
        adaptador = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
        nuevoCodTipDoc = MeLanbide68Manager.getInstance().generarCodTipDoc(adaptador);
        // Envía el valor al JSP como atributo
        request.setAttribute("nuevoCodTipDoc", nuevoCodTipDoc);

        // Redirige al JSP
        request.getRequestDispatcher("/jsp/extension/melanbide68/gestiones/nuevoTipDocLanbide.jsp").forward(request, response);
    } catch (Exception ex) {
        log.error("Error al procesar la solicitud: ", ex);
        throw ex; // Maneja la excepción según sea necesario
    }
}
public void getExcelTipDocuLanbideProcedimiento(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
    AdaptadorSQLBD adaptador = null;
    try {
        adaptador = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
        
        // 1) Obtener el idioma del usuario
        int idioma = 1; 
        HttpSession session = request.getSession(false);
        if (session != null) {
            UsuarioValueObject usuario = (UsuarioValueObject) session.getAttribute("usuario");
            if (usuario != null) {
                idioma = usuario.getIdioma();
            }
        }

        // 2) Llamar al Manager, pasándole el adaptador y el idioma
        HSSFWorkbook wb = MeLanbide68Manager.getInstance()
            .descargarExcelTipDocuLanbideProc(adaptador, idioma);

        // 3) Preparar la respuesta
        String fechaActual = new SimpleDateFormat("ddMMyyyy").format(new Date());
        String filename = "tiposDocumentalesLanbideporProcedimiento" + fechaActual + ".xls";

        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-Disposition", "attachment; filename=" + filename);

        OutputStream out = response.getOutputStream();
        wb.write(out);
        out.flush();
        out.close();

    } catch (Exception e) {
        e.printStackTrace();
        log.error("Error al recuperar el Excel con los datos de tipos documentales Lanbide - MELANBIDE68", e);
    }
}

}

           

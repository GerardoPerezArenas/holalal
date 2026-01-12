package es.altia.flexia.integracion.moduloexterno.melanbide63;

import es.altia.agora.business.escritorio.UsuarioValueObject;
import es.altia.agora.business.sge.plugin.documentos.AlmacenDocumento;
import es.altia.agora.business.sge.plugin.documentos.AlmacenDocumentoDokusiImpl;
import es.altia.agora.business.sge.plugin.documentos.vo.DocumentoGestor;
import es.altia.agora.technical.ConstantesDatos;
import es.altia.common.exception.TechnicalException;
import es.altia.flexia.integracion.moduloexterno.melanbide63.exception.CopiarDocumentosTramitacionException;
import es.altia.flexia.integracion.moduloexterno.melanbide63.i18n.MeLanbide63I18n;
import es.altia.flexia.integracion.moduloexterno.melanbide63.manager.MeLanbide63Manager;
import es.altia.flexia.integracion.moduloexterno.melanbide63.util.ConfigurationParameter;
import es.altia.flexia.integracion.moduloexterno.melanbide63.util.ConstantesMeLanbide63;
import es.altia.flexia.integracion.moduloexterno.melanbide63.util.MeLanbide63GeneralUtils;
import es.altia.flexia.integracion.moduloexterno.plugin.ModuloIntegracionExterno;
import es.altia.technical.PortableContext;
import es.altia.util.conexion.AdaptadorSQLBD;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.apache.poi.util.IOUtils;


public class MELANBIDE63 extends ModuloIntegracionExterno {
    private static Logger log = LogManager.getLogger(MELANBIDE63.class);

    /**
     * 
     * @param codOrganizacion
     * @param codTramite
     * @param ocurrenciaTramite
     * @param numExpediente
     * @return Uno de los siguientes cï¿½digos: 
     * 0:todo correcto.
     * 1:El código de trámite indicado es incorrecto.
     * 2:No existe el módulo.
     * 3:Se ha producido un error al obtener propiedades de MELANBIDE63.properties
     * 4:Ha ocurrido un error al recuperar la última ocurrencia de un trámite.
     * 5:Ha ocurrido un error al recuperar documentos de un trámite.
     * 6:Ha ocurrido un error de E/S al recuperar el contenido de un documento.
     * 7:Ha ocurrido un error al insertar documentos en un trámite.
     * 8:Ha ocurrido un error al recuperar lel numero de ultimo documento de un trámite.
     * 9:Ha ocurrido un error al copiar los documentos.
     * 10:Se ha producido un error al obtener la conexiï¿½n a la BBDD
     * 11:Se intenta recuperar un documento que no existe en BBDD ni en DOKUSI.
     * 12:Ha ocurrido un error al almacenar el documento.
     */
    public String copiadoDocumentosTramitacion(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente) {

        String salida = null;
        AdaptadorSQLBD adapt = null;
        try {
            adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
        } catch (SQLException ex) {
            java.util.logging.Logger.getLogger(MELANBIDE63.class.getName()).log(Level.SEVERE, null, ex);
        }
        String operacion = this.getNombreMetodo();

        String[] datosExp = numExpediente.split("/");
        int ejercicio = Integer.parseInt(datosExp[0]);
        String codProcedimiento = datosExp[1];     
                
        log.info("CODIGO ORGANIZACION: " + codOrganizacion );
        log.info("CODIGO TRAMITE: " + codTramite );
        log.info("OCURRENCIA TRAMITE: " + ocurrenciaTramite );
        log.info("NUMERO EXPEDIENTE: " + numExpediente );
        log.info("CODIGO PROCEDIMIENTO: " + codProcedimiento );
        log.info("EJERCICIO: " + ejercicio );
        log.info("OPERACION: " + operacion );
        
        try {
            salida = MeLanbide63Manager.getInstance().copiadoDocumentosTramitacion(codOrganizacion, ejercicio, numExpediente, codProcedimiento, codTramite, ocurrenciaTramite, operacion, adapt);
        } catch (CopiarDocumentosTramitacionException ex) {
            log.error(ex.getError());
        }
        return salida;
    }
    
    /**
     * 
     * @param codOrganizacion
     * @param codTramite
     * @param ocurrenciaTramite
     * @param numExpediente
     * @return Uno de los siguientes cï¿½digos: 
     * 0:todo correcto. 
     * 1:El código de trámite indicado es incorrecto.
     * 2:No existe el módulo.
     * 3:Se ha producido un error al obtener propiedades de MELANBIDE63.properties
     * 4:Ha ocurrido un error al recuperar la última ocurrencia de un trámite.
     * 5:Ha ocurrido un error al recuperar documentos de un trámite.
     * 6:Ha ocurrido un error de E/S al recuperar el contenido de un documento.
     * 7:Ha ocurrido un error al insertar documentos en un trámite.
     * 8:Ha ocurrido un error al recuperar el numero de ultimo documento de un trámite.
     * 9:Ha ocurrido un error al copiar los documentos. 
     * 10:Se ha producido un error al obtener la conexiï¿½n a la BBDD
     * 11:Se intenta recuperar un documento que no existe en BBDD ni en DOKUSI.
     * 12:Ha ocurrido un error al almacenar el documento.
     * 13.Ha ocurrido un obtener al obtener un id de copia del documento en Dokusi
     */
      public String copiadoDocumentosDOKUSI(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente) {
        
        String salida = null;
        AdaptadorSQLBD adapt = null;
        try {
            adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
        } catch (SQLException ex) {
            java.util.logging.Logger.getLogger(MELANBIDE63.class.getName()).log(Level.SEVERE, null, ex);
        }
        String operacion = this.getNombreMetodo();

        String[] datosExp = numExpediente.split("/");
        int ejercicio = Integer.parseInt(datosExp[0]);
        String codProcedimiento = datosExp[1];              
        
        log.info("CODIGO ORGANIZACION: " + codOrganizacion );
        log.info("CODIGO TRAMITE: " + codTramite );
        log.info("OCURRENCIA TRAMITE: " + ocurrenciaTramite );
        log.info("NUMERO EXPEDIENTE: " + numExpediente );
        log.info("CODIGO PROCEDIMIENTO: " + codProcedimiento );
        log.info("EJERCICIO: " + ejercicio );
        log.info("OPERACION: " + operacion );
        
        try {
            salida = MeLanbide63Manager.getInstance().copiadoDocumentosTramitacion(codOrganizacion, ejercicio, numExpediente, codProcedimiento, codTramite, ocurrenciaTramite, operacion, adapt);
        } catch (CopiarDocumentosTramitacionException ex) {
            log.error(ex.getError());
        }
        return salida;
    }
    
    
      /**
     * Operación copiadoDocumentosREINT
     * @param codOrganizacion
     * Expediente origen
     * Expediente destino
     * TrÃ¡mite origen
     * TrÃ¡mite destino 
     * @param codTramite 
     * @param ocurrenciaTramite 
     * @param numExpediente 
     * @return  
     * @throws es.altia.flexia.integracion.moduloexterno.melanbide63.exception.CopiarDocumentosTramitacionException 
     */
      
      public String copiadoDocumentosREINT(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente) throws CopiarDocumentosTramitacionException, Exception {
        
        String salida = null;
        AdaptadorSQLBD adapt = null;
        try {
            adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
        } catch (SQLException ex) {
            java.util.logging.Logger.getLogger(MELANBIDE63.class.getName()).log(Level.SEVERE, null, ex);
        }
        String operacion = this.getNombreMetodo();

        String[] datosExp = numExpediente.split("/");
        int ejercicio = Integer.parseInt(datosExp[0]);
        String codProcedimiento = datosExp[1];              
        
        log.debug("CODIGO ORGANIZACION: " + codOrganizacion );
        log.debug("CODIGO TRAMITE: " + codTramite );
        log.debug("OCURRENCIA TRAMITE: " + ocurrenciaTramite );
        log.debug("NUMERO EXPEDIENTE: " + numExpediente );
        log.debug("CODIGO PROCEDIMIENTO: " + codProcedimiento );
        log.debug("EJERCICIO: " + ejercicio );
        log.debug("OPERACION: " + operacion );
        
        //obtenemos el expediente reint asociado al de apec que nos llega.
          
        String expedienteAsociadoReint= MeLanbide63Manager.getInstance().ExpedienteAsociadoREINT(codOrganizacion, numExpediente,adapt);
                
        //Una vez obtenido el expediente asociado al apec inicial pasaremos los siguientes paramentros.
        //expediente inicial APEC
        //EXPEDIENTE ASOCIADO REINT
        //TRAMITE ORIGEN SERIA EL 400 DE APEC 400 - DOCUMENTOS REINTEGRO
        //TRAMITE DESTINO EL 1 DE REINT 1 - NOTIFICACIÃ“N REQUERIMIENTO REINTEGRO
        log.debug("NUMEROEXPEDIENTE: " + numExpediente );
        log.debug("ASOCIADO REINT: " + expedienteAsociadoReint );
        
        //CODTRAMITE INTERNO DEL TRAMITE SELECCIONADO
        
 
        
        
        try {
           salida = MeLanbide63Manager.getInstance().copiadoDocumentosREINT(codOrganizacion, ejercicio, numExpediente, codProcedimiento, codTramite, ocurrenciaTramite, operacion,expedienteAsociadoReint, adapt);
        } catch (CopiarDocumentosTramitacionException ex) {
            log.error(ex.getError());
        }
        return salida;
    }
     
              
              
              
              
    
    
    /**
     * Operación que recupera los datos de conexión a la BBDD
     * @param codOrganizacion
     * @return AdaptadorSQLBD
     * @throws java.sql.SQLException
     */
    private AdaptadorSQLBD getAdaptSQLBD(String codOrganizacion) throws SQLException{
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
            try{
                PortableContext pc = PortableContext.getInstance();
                log.debug("He cogido el jndi: " + jndiGenerico);
                ds = (DataSource)pc.lookup(jndiGenerico, DataSource.class);
                // Conexión al esquema genÃ©rico
                conGenerico = ds.getConnection();

                String sql = "SELECT EEA_BDE FROM A_EEA WHERE EEA_APL=" + ConstantesDatos.APP_GESTION_EXPEDIENTES + " AND AAE_ORG=" + codOrganizacion;
                st = conGenerico.createStatement();
                rs = st.executeQuery(sql);
                String jndi = null;
                while(rs.next()){
                    jndi = rs.getString("EEA_BDE");
                }//while(rs.next())

                st.close();
                rs.close();
                conGenerico.close();

                if(jndi!=null && gestor!=null && !"".equals(jndi) && !"".equals(gestor)){
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
            }catch(TechnicalException te){
                te.printStackTrace();
                log.error("*** AdaptadorSQLBD: " + te.toString());
            }catch(SQLException e){
                e.printStackTrace();
            }finally{
                    if(st!=null) st.close();
                    if(rs!=null) rs.close();
                    if(conGenerico!=null && !conGenerico.isClosed())
                    conGenerico.close();
            }// finally
         }// synchronized
        return adapt;
     }//getConnection
    
    private String getNombreMetodo(){
      //Retorna el nombre del metodo desde el cual se hace el llamado
      String metodo = new Exception().getStackTrace()[1].getMethodName();
      String[] partes = metodo.split("Documentos");
      return partes[1].toUpperCase();
   }
    
    private static void docBytesToFile (byte[] bFile, String fileDest) {

        FileOutputStream fileOuputStream = null;

        try {
            fileOuputStream = new FileOutputStream(fileDest);
            fileOuputStream.write(bFile);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fileOuputStream != null) {
                try {
                    fileOuputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }
    
    public void descargaMasivaRegexlanResolucionesDsdURL(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.info("descargaMasivaRegexlanResolucionesDsdURL - Begin");
        String codigoOperacion = "0";
        String desOperacion="";
        
        Integer codigoUsuarioOperacion = ConstantesMeLanbide63.CODIGO_ID_USUARIO_ADMIN;
        Integer idiomaUsuarioOperacion = ConstantesMeLanbide63.IDIOMA_USUARIO_ADMIN;
        
        String numTarea = "";
        String codigoProcIndicadoInterfaz = "";
        String codigoInteTramIndicaInterf = "";
        String codigoPlantilla = "";
        String urlDocumentosResol = "";
        
        //modificación para permitir descargar documentos externos (no se indicaría ni el código de trámite ni el código de plantilla en caso de documentos externos)
        boolean esExterno = false;
        
        try {
            AdaptadorSQLBD adapt=null;
            MeLanbide63Manager meLanbide63Manager = MeLanbide63Manager.getInstance();
            try {
                adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
            } catch (Exception e) {
                log.error("Error al recuperar el Adaptador de BBDD." + e.getMessage(),e);
                codigoOperacion="1";
            }
            // Si todo correcto continuamos
            if("0".equalsIgnoreCase(codigoOperacion)){
                
                boolean procesoLanzado = meLanbide63Manager.procesoLanzado(adapt);
                if (procesoLanzado == false) {
                    meLanbide63Manager.lanzarProceso(adapt);
                
                numTarea = request.getParameter("numTarea");
                urlDocumentosResol = request.getParameter("urlDocumentosResol");
                codigoProcIndicadoInterfaz = request.getParameter("codigoProcIndicadoInterfaz");
                codigoInteTramIndicaInterf = request.getParameter("codigoInteTramIndicaInterf");
                    if (codigoInteTramIndicaInterf == null || codigoInteTramIndicaInterf.equalsIgnoreCase("")) {
                        esExterno = true;
                    }
                codigoPlantilla = request.getParameter("codigoPlantilla");
                if(codigoProcIndicadoInterfaz!=null && !codigoProcIndicadoInterfaz.equalsIgnoreCase("")){
                    codigoProcIndicadoInterfaz=codigoProcIndicadoInterfaz.trim();
                    codigoProcIndicadoInterfaz=codigoProcIndicadoInterfaz.toUpperCase();
                }
                if(codigoInteTramIndicaInterf!=null && !codigoInteTramIndicaInterf.equalsIgnoreCase("")){
                    codigoInteTramIndicaInterf=codigoInteTramIndicaInterf.trim();
                }
                if(codigoPlantilla!=null && !codigoPlantilla.equalsIgnoreCase("")){
                    codigoPlantilla=codigoPlantilla.trim();
                }
                
                //Validamos el codigo de procedimiento,Codigo de tramite y codigo de plantilla indicado.
                if(meLanbide63Manager.existeProcedimiento(codigoProcIndicadoInterfaz,adapt)){
                        if ((esExterno == true) || ((esExterno == false) && (meLanbide63Manager.existeCodTramiteInternoProcedimiento(codigoProcIndicadoInterfaz,codigoInteTramIndicaInterf,adapt)))) {
                            if((esExterno == true) || ((esExterno == false) && (meLanbide63Manager.existeCodPlantillaDocumentoTramiteProcedimiento(codigoProcIndicadoInterfaz,codigoInteTramIndicaInterf,codigoPlantilla,adapt)))){
                            
                            if (urlDocumentosResol == null || urlDocumentosResol.equalsIgnoreCase("")) {
                                    log.info("No hemos recibido la url desde la interfaz, la cogemos del properties - ");
                                urlDocumentosResol = ConfigurationParameter.getParameter(ConstantesMeLanbide63.URL_DIRECTORIO_DOCS_CARGA_MASIVA, ConstantesMeLanbide63.FICHERO_PROPIEDADES)
                                        + codigoProcIndicadoInterfaz;
                            }
                            // Tratamos de recuperar datos del Usuario que hace la operación Sino Traceamos y asignamos codigo de administrador.
                            UsuarioValueObject usuario = new UsuarioValueObject();
                            try {
                                if (request != null && request.getSession() != null) {
                                    if (request.getSession().getAttribute("usuario") != null) {
                                        usuario = (UsuarioValueObject) request.getSession().getAttribute("usuario");
                                    }
                                    if (usuario != null) {
                                        idiomaUsuarioOperacion = usuario.getIdioma();
                                        codigoUsuarioOperacion = usuario.getIdUsuario();
                                    }
                                }
                            } catch (Exception ex) {
                                log.error("Error al recuperar los datos del usuario para la descarga masiva de documentos. Asignamos el ID 5 = ADMIN.", ex);
                                codigoUsuarioOperacion = ConstantesMeLanbide63.CODIGO_ID_USUARIO_ADMIN;
                                idiomaUsuarioOperacion = ConstantesMeLanbide63.IDIOMA_USUARIO_ADMIN;
                            }
                            if (urlDocumentosResol != null && !urlDocumentosResol.equalsIgnoreCase("")) {
                                    log.info("URL donde se van a grabar los documentos : " + urlDocumentosResol);
                                final File folderDocFile = new File(urlDocumentosResol+"/doc/");
                                if(!folderDocFile.exists()){
                                    if(folderDocFile.mkdir()){
                                            log.info("CARPETA para guardar documentos creada correctamente.");
                                    }else{
                                            log.info("CARPETA para guardar documentos YA EXISTIA");
                                    }
                                }
                                
                                if (folderDocFile != null && folderDocFile.isDirectory()) {
                                    
                                    ArrayList<String> listaExpedientesOcurrencia = meLanbide63Manager.recuperarExpedientesOcurrencia(Integer.parseInt(numTarea), adapt);
                                    
                                    for (int i = 0; i < listaExpedientesOcurrencia.size(); i++){
                                        
                                        
                                        //descarga de documento de dokusi
                                        //ejemplo DESA: organizacion MUN 0(codOrganizacion), 2017, CUOTS, 2017/CUOTS/000017, tramite 71, ocurrencia 1, NUD 1, oid 09f42401808343a3 (lo recoge el getDocumento())
                                        //se modifica para obtener el NUD y el tipo de documento, PDF ó DOC
                                        //ejemplo DESA: 2022/TRECO/000033_1_2_pdf
                                            //si el documento es externo el NUD es el campo DOC_EXT_COD de tabla E_DOC_EXT
                                        String [] expediente = listaExpedientesOcurrencia.get(i).split("/");
                                        int ejercicio = Integer.parseInt(expediente[0]);
                                        String [] expedienteOcurrencia = expediente[2].split("_");
                                        String nExpediente = ejercicio + "/" + codigoProcIndicadoInterfaz + "/" + expedienteOcurrencia[0];
                                        int ocurrencia = Integer.parseInt(expedienteOcurrencia[1]);
                                        String nud = expedienteOcurrencia[2];
                                        String t_doc = expedienteOcurrencia[3];
                                        
                                        DocumentoGestor doc = new DocumentoGestor();
                                        doc.setCodMunicipio(codOrganizacion);
                                        doc.setEjercicio(ejercicio);
                                        doc.setCodProcedimiento(codigoProcIndicadoInterfaz);
                                        doc.setNumeroExpediente(nExpediente);
                                            if (esExterno == false) {
                                        doc.setCodTramite(Integer.parseInt(codigoInteTramIndicaInterf));
                                            }
                                            
                                        doc.setOcurrenciaTramite(ocurrencia);
                                        doc.setNumeroDocumento(nud);
                                        doc.setExtension(t_doc);

                                        try {
                                            AlmacenDocumentoDokusiImpl docDokusi = new AlmacenDocumentoDokusiImpl();
                                                if (esExterno == false) {
                                            byte[] docT = docDokusi.getDocumento(doc);

                                            //docBytesToFile(docWord, urlDocumentosResol + "/doc/2017_CUOTS_000017_1.doc");
                                            docBytesToFile(docT, urlDocumentosResol + "/doc/" + ejercicio + "_" + codigoProcIndicadoInterfaz + "_" + expedienteOcurrencia[0] + "_" + codigoInteTramIndicaInterf + "_" + expedienteOcurrencia[1] + "_" + nud + "." + t_doc);
                                                } else {
                                                    byte[] docE = docDokusi.getDocumentoExterno(doc).getFichero();
                                                    docBytesToFile(docE, urlDocumentosResol + "/doc/" + ejercicio + "_" + codigoProcIndicadoInterfaz + "_" + expedienteOcurrencia[0] + "_" + nud + "." + t_doc);
                                                }
                                            
                                            log.info("listaExpedientesOcurrencia - nº " + i + ": " + listaExpedientesOcurrencia.get(i) + "  OK - CORRECTO");
                                        } catch (Exception e) {
                                            log.error("listaExpedientesOcurrencia - nº " + i + ": " + listaExpedientesOcurrencia.get(i) + "  KO - ERROR: NO HA CARGADO DOCUMENTO");
                                        }
                                        
                                    }
                                    
                                } else {
                                    log.error("La URL recibida no existe o no es un directorio valido." + urlDocumentosResol);
                                    codigoOperacion = "3";
                                }
                            } else {
                                log.error("No hemos conseguido la URL ni desde la request ni desde el properties - Devolcemos Error 1");
                                codigoOperacion = "2";
                            }

                        }else{
                            log.error("Código de plantilla indicado NO VALIDO o no existe en ese procedimiento y tramite." + codigoProcIndicadoInterfaz + "-" + codigoInteTramIndicaInterf+"-->"+codigoPlantilla);
                            codigoOperacion = "7";
                        }
                    } else {
                        log.error("Codigo de trámite indicado NO VALIDO o no existe en ese procedimiento." + codigoProcIndicadoInterfaz +"-->"+ codigoInteTramIndicaInterf );
                        codigoOperacion = "6";
                    }
                    
                }else{
                    log.error("Codigo de procedimiento indicado NO VALIDO." + codigoProcIndicadoInterfaz);
                    codigoOperacion = "5";
                }
            }
            }
        } catch (IOException  e) {
            log.error("Error al leer ó grabar los documentos de la descarga masiva. " + e.getMessage(), e);
            codigoOperacion = "4";
        } catch (Exception e) {
            log.error("Error Generico al procesar la descarga masiva de documentos Regexlan : " + e.getMessage(), e);
            codigoOperacion = "-1";
        }
        try {
            // Traducimos el resultado de la operacion
            MeLanbide63I18n meLanbide63I18n = MeLanbide63I18n.getInstance();
            desOperacion = meLanbide63I18n.getMensaje(idiomaUsuarioOperacion, ConstantesMeLanbide63.PREFIJO_RESULTADO_CODIGO_DOT + codigoOperacion);
            
            log.info("Escribimos la respuesta en la request : " + codigoOperacion);
            StringBuilder xmlSalida = new StringBuilder();
            xmlSalida.append("<RESPUESTA>");
                xmlSalida.append("<CODIGO_OPERACION>");
                    xmlSalida.append(codigoOperacion);
                xmlSalida.append("</CODIGO_OPERACION>");
                xmlSalida.append("<DETALLE_OPERACION>");
                    xmlSalida.append(desOperacion);
                xmlSalida.append("</DETALLE_OPERACION>");
            xmlSalida.append("</RESPUESTA>");
            log.info("XML en la request : " + xmlSalida);
            response.setContentType("text/xml");
            response.setCharacterEncoding("UTF-8");
            PrintWriter out1 = response.getWriter();
            out1.print(xmlSalida.toString());
            out1.flush();
            out1.close();
        } catch (Exception e) {
            log.error("Error al escribir la respuesta de la llamada en la request " + e.getMessage(), e);
        }
        log.info("descargaMasivaRegexlanResolucionesDsdURL - End");
    }
    
    public void cargaMasivaRegexlanResolucionesDsdURL(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        
        try {
            String codigoOperacion = "0";
            String desOperacion="";
            String urlDocumentosResol = "";
            String codigoProcIndicadoInterfaz = "";
            String codigoInteTramIndicaInterf = "";
            String codigoPlantilla = "";
            String eliminarDocServidor = "";
            Boolean eliminarDocServ = false;
            Integer codigoUsuarioOperacion = ConstantesMeLanbide63.CODIGO_ID_USUARIO_ADMIN;
            Integer idiomaUsuarioOperacion = ConstantesMeLanbide63.IDIOMA_USUARIO_ADMIN;
            File carpetaLocal = null;
            FileWriter fw = null;
            BufferedWriter out=null;
            AdaptadorSQLBD adapt=null;
            MeLanbide63Manager meLanbide63Manager = MeLanbide63Manager.getInstance();
            log.info("Revisamos si el proceso ya está lanzado");
            try {
                adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
            } catch (Exception e) {
                log.error("Error al recuperar el Adaptador de BBDD." + e.getMessage(),e);
                codigoOperacion="1";
            }
            boolean procesoLanzado = meLanbide63Manager.procesoLanzado(adapt);
            if (procesoLanzado == false) {
                meLanbide63Manager.lanzarProceso(adapt);
                
                log.info("cargaMasivaRegexlanResolucionesDsdURL - Begin");
                
                try {
                    // Para probar com carga FileUpload (De momento no, son muy pesados los ficheros)
                    try {
                        carpetaLocal = (request.getParameter("carpetaLocal") != null ? (File) request.getAttribute("carpetaLocal") : null);
                    } catch (Exception e) {
                        log.error("Error al recuperar el Objeto File como carpeta desde la request.", e);
                    }
                    // Fin Prueba como FileUpload
                    /*try {
                        adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
                    } catch (Exception e) {
                        log.error("Error al recuperar el Adaptador de BBDD." + e.getMessage(),e);
                        codigoOperacion="1";
                    }*/
                    // Si todo correcto continuamos
                    if("0".equalsIgnoreCase(codigoOperacion)){

                        urlDocumentosResol = request.getParameter("urlDocumentosResol");
                        codigoProcIndicadoInterfaz = request.getParameter("codigoProcIndicadoInterfaz");
                        codigoInteTramIndicaInterf = request.getParameter("codigoInteTramIndicaInterf");
                        codigoPlantilla = request.getParameter("codigoPlantilla");
                        eliminarDocServidor = request.getParameter("eliminarDocServidor");
                        if(codigoProcIndicadoInterfaz!=null && !codigoProcIndicadoInterfaz.equalsIgnoreCase("")){
                            codigoProcIndicadoInterfaz=codigoProcIndicadoInterfaz.trim();
                            codigoProcIndicadoInterfaz=codigoProcIndicadoInterfaz.toUpperCase();
                        }
                        if(codigoInteTramIndicaInterf!=null && !codigoInteTramIndicaInterf.equalsIgnoreCase("")){
                            codigoInteTramIndicaInterf=codigoInteTramIndicaInterf.trim();
                        }
                        if(codigoPlantilla!=null && !codigoPlantilla.equalsIgnoreCase("")){
                            codigoPlantilla=codigoPlantilla.trim();
                        }
                        if(eliminarDocServidor!=null && "1".equalsIgnoreCase(eliminarDocServidor)){
                            eliminarDocServ=true;
                        }

                        //Validamos el codigo de procedimiento,Codigo de tramite y codigo de plantilla indicado.
                        if(meLanbide63Manager.existeProcedimiento(codigoProcIndicadoInterfaz,adapt)){
                            if (meLanbide63Manager.existeCodTramiteInternoProcedimiento(codigoProcIndicadoInterfaz,codigoInteTramIndicaInterf,adapt)) {
                                if(meLanbide63Manager.existeCodPlantillaDocumentoTramiteProcedimiento(codigoProcIndicadoInterfaz,codigoInteTramIndicaInterf,codigoPlantilla,adapt)){

                                    if (urlDocumentosResol == null || urlDocumentosResol.equalsIgnoreCase("")) {
                                        log.info("No hemos recibido la url desde la interfaz, la cogemos del properties - ");
                                        urlDocumentosResol = ConfigurationParameter.getParameter(ConstantesMeLanbide63.URL_DIRECTORIO_DOCS_CARGA_MASIVA, ConstantesMeLanbide63.FICHERO_PROPIEDADES)
                                                + codigoProcIndicadoInterfaz;
                                    }
                                    // Tratamos de recuperar datos del Usuario que hace la operación Sino Traceamos y asignamos codigo de administrador.
                                    UsuarioValueObject usuario = new UsuarioValueObject();
                                    try {
                                        if (request != null && request.getSession() != null) {
                                            if (request.getSession().getAttribute("usuario") != null) {
                                                usuario = (UsuarioValueObject) request.getSession().getAttribute("usuario");
                                            }
                                            if (usuario != null) {
                                                idiomaUsuarioOperacion = usuario.getIdioma();
                                                codigoUsuarioOperacion = usuario.getIdUsuario();
                                            }
                                        }
                                    } catch (Exception ex) {
                                        log.error("Error al recuperar los datos del usuario para la carga masiva de documentos. Asignamos el ID 5 = ADMIN.", ex);
                                        codigoUsuarioOperacion = ConstantesMeLanbide63.CODIGO_ID_USUARIO_ADMIN;
                                        idiomaUsuarioOperacion = ConstantesMeLanbide63.IDIOMA_USUARIO_ADMIN;
                                    }
                                    if (urlDocumentosResol != null && !urlDocumentosResol.equalsIgnoreCase("")) {
                                        // Creamos el fichero para registrar paso a paso el proceso.
                                        log.info("URL De donde se van a leer los ficheros y escribir el log : " + urlDocumentosResol);
                                        String nombreFicLog =MeLanbide63GeneralUtils.preparaNombreFicheroLogCargaMasivaDoc(codigoProcIndicadoInterfaz) + ".txt";
                                        File folderlogFile = new File(urlDocumentosResol+"/log/");
                                        if(!folderlogFile.exists()){
                                            if(folderlogFile.mkdir()){
                                                log.info("CARPETA para escritura del log del proceso creada correctamente.");
                                            }else{
                                                log.info("CARPETA para escritura del log YA EXISTIA");
                                            }
                                        }
                                        File logFile = new File(folderlogFile.getAbsolutePath()+"/"+nombreFicLog);
                                        if (logFile.createNewFile()) {
                                            log.info("FICHERO para escritura del log del proceso creado correctamente.");
                                        } else {
                                            log.error("FICHERO para escritura del log ya existe o se presento un error al crearlo.");
                                        }
                                        fw = new FileWriter(logFile);
                                        out = new BufferedWriter(fw);
                                        out.write("Iniciamos proceso recorriendo ficheros en carpeta. " + new Date().toString());

                                        final File carpeta = new File(urlDocumentosResol);
                                        if (carpeta != null && carpeta.isDirectory()) {
                                            for (File ficheroIn : carpeta.listFiles()) {
                                                //Solo recorremos los Ficheros no directorios
                                                if(ficheroIn.isFile()){
                                                    String Nombre = ficheroIn.getName();
                                                    //Quitamos del nombre espacios en blanco y las extensiones .doc y docx. (Los documentos deberian ser solo de estos dos tipos)
                                                    Nombre = Nombre.trim();
                                                    Nombre = Nombre.toUpperCase();
                                                    Nombre = Nombre.replaceAll(".DOCX", "");
                                                    Nombre = Nombre.replaceAll(".DOC", "");

                                                    //pueden ser pdfs para subir a Dokusi para cargar en la aplicación NOTIFICA
                                                    Boolean esPDF = false;
                                                    Integer longNombre = Nombre.length();
                                                    String extens = Nombre.substring(longNombre-3, longNombre);
                                                    if (extens.equals("PDF")) {
                                                        esPDF = true;
                                                        Nombre = Nombre.replaceAll(".PDF", "");
                                                    }

                                                    // Preparamos numero de Expediente (Los ficheros deben crearse con el numero de expediente como nombre, al dividir la combinación de correspondencia. Salvo el caracter / que es prohibido)
                                                    String numExpOcu = Nombre.replaceAll("_", "/");

                                                    //la ocurrencia viene dada por el último valor, si lo tiene (2017/CUOTS/000017/1), si no, vendría sólo el expediente (2017/CUOTS/000017)
                                                    String[] expOcu = numExpOcu.split("/");
                                                    Integer ocurrencia = 0;
                                                    String numExp = numExpOcu;
                                                    //si tiene ocurrencia
                                                    if (expOcu.length > 3){
                                                        numExp = expOcu[0] + "/" + expOcu[1] + "/" + expOcu[2];
                                                        ocurrencia = Integer.parseInt(expOcu[3]);
                                                    }

                                                    String datosBasicosExpte[] = MeLanbide63GeneralUtils.getDatosExpteDsdNumExpediente(numExp);
                                                    Integer ejercicio;
                                                    String codProcedimiento;
                                                    ejercicio = (datosBasicosExpte != null && datosBasicosExpte.length > 0 && !datosBasicosExpte[0].equalsIgnoreCase("") ? Integer.parseInt(datosBasicosExpte[0]) : null);
                                                    codProcedimiento = (datosBasicosExpte != null && datosBasicosExpte.length > 1 ? datosBasicosExpte[1] : null);
                                                    /**
                                                     * Comprobamos: Existe expediente El
                                                     * expediente ha pasado por ese
                                                     * tramite.
                                                     */

                                                    if (meLanbide63Manager.existeExpediente(numExp, adapt)) {
                                                        if (meLanbide63Manager.existeTramiteEnExpediente(numExp, codigoInteTramIndicaInterf, adapt)) {
                                                            if (codigoProcIndicadoInterfaz.equalsIgnoreCase(codProcedimiento)) {

                                                                Nombre = ConfigurationParameter.getParameter(ConstantesMeLanbide63.PREFIJO_TEXTO_NOMBREDOC_RESOLCONCEDIDA, ConstantesMeLanbide63.FICHERO_PROPIEDADES)
                                                                        + Nombre;
                                                                //Parametros Tabla
                                                                Integer tramite = codigoInteTramIndicaInterf != null && !codigoInteTramIndicaInterf.equalsIgnoreCase("") ? Integer.parseInt(codigoInteTramIndicaInterf) : 0;

                                                                //si no hay ocurrencia especificada, se toma la última
                                                                if (expOcu.length < 4){
                                                                    ocurrencia = meLanbide63Manager.obtenerMaxOcurrenciaTramiteEnExpediente(numExp, String.valueOf(tramite), adapt);
                                                                }

                                                                // Solo para el caso de BBDD - NO es un Seq asi que no afecta la numeracion si no se guarda en BBDD
                                                                Integer numDocumento = meLanbide63Manager.obtenerIDNumDocumentoTramiteExpediente(numExp, tramite, ocurrencia, adapt);
                                                                Date fechaAlta = new Date();
                                                                DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                                                                String FechaDoc = dateFormat.format(fechaAlta);
                                                                Integer documentoPlantilla = codigoPlantilla != null && !codigoPlantilla.equalsIgnoreCase("") ? Integer.parseInt(codigoPlantilla) : null;
                                                                FileInputStream is = new FileInputStream(ficheroIn);
                                                                byte[] contenido = IOUtils.toByteArray(is);
                                                                is.close();

                                                                DocumentoGestor documentoGestor = new DocumentoGestor();
                                                                documentoGestor.setCodMunicipio(codOrganizacion);
                                                                documentoGestor.setCodProcedimiento(codProcedimiento);
                                                                documentoGestor.setEjercicio(ejercicio);
                                                                documentoGestor.setNumeroExpediente(numExp);
                                                                documentoGestor.setCodTramite(tramite);
                                                                documentoGestor.setOcurrenciaTramite(ocurrencia);
                                                                documentoGestor.setFechaDocumento(FechaDoc);
                                                                documentoGestor.setCodUsuario(codigoUsuarioOperacion);
                                                                documentoGestor.setFichero(contenido);
                                                                documentoGestor.setNombreDocumento(Nombre);
                                                                documentoGestor.setCodDocumento(documentoPlantilla);
                                                                if (esPDF) {
                                                                    documentoGestor.setExtension("pdf");
                                                                } else {
                                                                    documentoGestor.setExtension("doc");
                                                                }

                                                                String salida = "";
                                                                if(meLanbide63Manager.esDokusiActivoProcedimiento(codProcedimiento, adapt)){
                                                                    log.info("Procedimiento Activo para Dokusi - Guardamos Documento en Dokusi." + Nombre + "-" + numExp);
                                                                    AlmacenDocumentoDokusiImpl almacen = new AlmacenDocumentoDokusiImpl();
                                                                    try{
                                                                        boolean guardadoDokusi= almacen.setDocumento(documentoGestor);
                                                                        if(guardadoDokusi)
                                                                            salida="1";
                                                                    }catch(Exception ex){
                                                                        log.error("Error en al Guardar el Documento en Dokusi." + Nombre + "-" + numExp);
                                                                        String traza = "";
                                                                        if(ex.getStackTrace()!=null && ex.getStackTrace().length>0){
                                                                            for(StackTraceElement stackTrace :ex.getStackTrace()){
                                                                                traza += stackTrace.toString();
                                                                            }
                                                                        }
                                                                        salida=ex.getMessage() + " *** Traza *** " + traza;
                                                                    }
                                                                }else{
                                                                    log.info("Procedimiento No Activo para Dokusi - Guardamos Documento en BBDD." + Nombre + "-" + numExp);
                                                                    documentoGestor.setNumeroDocumento(String.valueOf(numDocumento));
                                                                    salida=meLanbide63Manager.cargarDocumentoResolucionTramiteBBDD(documentoGestor, adapt);
                                                                }
                                                                if (salida != null && !salida.equalsIgnoreCase("0")) {
                                                                    log.info("Documento insertado correctamente." + Nombre + "-" + numExp);
                                                                    out.newLine();
                                                                    out.write(numExp + " --> " + "Documento Cargado Correctamente : " + Nombre);
                                                                    if(eliminarDocServ){
                                                                        if(ficheroIn.exists()){
                                                                            if (ficheroIn.delete()) {
                                                                                log.info("Fichero cargado, eliminado correctamente del servidor.");
                                                                            } else {
                                                                                log.error("Fichero cargado, no se pudo eliminar correctamente del servidor.");
                                                                            }
                                                                        }
                                                                    }
                                                                } else {
                                                                    if (salida != null) {
                                                                        log.info("No hubo errores pero no se inserto el documento : " + Nombre + "-" + numExp + ". Salida operacion: " + salida);
                                                                    } else {
                                                                        log.error("Se presento un error al cargar el documento " + Nombre + "-" + numExp + ". Salida operacion: " + salida);
                                                                    }
                                                                    out.newLine();
                                                                    out.write(numExp + " --> " + "NO se cargo ningun documento : " + Nombre + ". Salida operacion: " + salida);
                                                                }

                                                            } else {
                                                                log.error("Codigo de procedimiento indicado en la inerfaz no coincide con el cod procedimiento en el nombre del Documento, no cargamos Fichero. " + numExp + "-" + Nombre + ": " + codigoProcIndicadoInterfaz + "--" + codProcedimiento);
                                                                out.newLine();
                                                                out.write(numExp + " --> " + "No coincide codigo procedimiento indicado en interfaz con codigo procedimiento en nombre del documento. Fichero no cargado : " + Nombre + "--" + codigoProcIndicadoInterfaz);
                                                            }

                                                        } else {
                                                            log.error("Tramite no existe en el Expediente, no cargamos Documento " + codigoInteTramIndicaInterf + "-" + numExp + "-" + Nombre);
                                                            out.newLine();
                                                            out.write(numExp + " --> " + "No existe Tramite en el Expediente. Fichero no cargado. " + Nombre);
                                                        }
                                                    } else {
                                                        log.error("Expediente no existe, no cargamos Documento " + numExp + "-" + Nombre);
                                                        out.newLine();
                                                        out.write(numExp + " --> " + "No existe Expediente. Fichero no cargado. " + Nombre);
                                                    }
                                                }
                                            }
                                        } else {
                                            log.error("La URL recibida no existe o no es un directorio valido." + urlDocumentosResol);
                                            codigoOperacion = "3";
                                        }
                                    } else {
                                        log.error("No hemos conseguido la URL ni desde la request ni desde el properties - Devolcemos Error 1");
                                        codigoOperacion = "2";
                                    }

                                }else{
                                    log.error("Codigo de plantilla indicado NO VALIDO o no existe en ese procedimiento y tramite." + codigoProcIndicadoInterfaz + "-" + codigoInteTramIndicaInterf+"-->"+codigoPlantilla);
                                    codigoOperacion = "7";
                                }
                            } else {
                                log.error("Codigo de tramite indicado NO VALIDO o no existe en ese procedimiento." + codigoProcIndicadoInterfaz +"-->"+ codigoInteTramIndicaInterf );
                                codigoOperacion = "6";
                            }

                        }else{
                            log.error("Codigo de procedimiento indicado NO VALIDO." + codigoProcIndicadoInterfaz);
                            codigoOperacion = "5";
                        }
                    }
                } catch (IOException  e) {
                    log.error("Error al escribir el log o leer los documentos de la carga masiva. " + e.getMessage(), e);
                    codigoOperacion = "4";
                } catch (Exception e) {
                    log.error("Error Generico al procesar la carga masiva de documentoa Regexlan : " + e.getMessage(), e);
                    codigoOperacion = "-1";
                }finally{
                    try {
                        if (out != null) {
                            out.newLine();
                            out.write("Proceso Finalizado. Cerramos buffer de Escritura. " + new Date().toString());
                            out.close();
                            meLanbide63Manager.liberarProceso(adapt);
                        }

                        if (fw != null) {
                            fw.close();
                        }
                    } catch (IOException ex) {
                        log.error("Error al cerrar el buffer o el filewriter del log la carga masiva. " + ex.getMessage(), ex);
                    }
                }
                try {
                    // Traducimos el resultado de la operacion
                    MeLanbide63I18n meLanbide63I18n = MeLanbide63I18n.getInstance();
                    desOperacion = meLanbide63I18n.getMensaje(idiomaUsuarioOperacion, ConstantesMeLanbide63.PREFIJO_RESULTADO_CODIGO_DOT + codigoOperacion);

                    log.info("Escribimos la respuesta en la request : " + codigoOperacion);
                    StringBuilder xmlSalida = new StringBuilder();
                    xmlSalida.append("<RESPUESTA>");
                        xmlSalida.append("<CODIGO_OPERACION>");
                        xmlSalida.append(codigoOperacion);
                        xmlSalida.append("</CODIGO_OPERACION>");
                        xmlSalida.append("<DETALLE_OPERACION>");
                        xmlSalida.append(desOperacion);
                        xmlSalida.append("</DETALLE_OPERACION>");
                    xmlSalida.append("</RESPUESTA>");
                    log.info("XML en la request : " + xmlSalida);
                    response.setContentType("text/xml");
                    response.setCharacterEncoding("UTF-8");
                    PrintWriter out1 = response.getWriter();
                    out1.print(xmlSalida.toString());
                    out1.flush();
                    out1.close();
                } catch (Exception e) {
                    log.error("Error al escribir la respuesta de la llamada en la request " + e.getMessage(), e);
                }
                log.info("cargaMasivaRegexlanResolucionesDsdURL - End");

            }
        } catch (Exception e) {
            
        }
    
        
    }
    
    /**
     * Pemrite borrar los documentos subidos en en tramite, se borrar tambien del servidor, el numero de expedinte se lee como en la carga,
     * los documentos deben estar almacenados en l misma URL y con el donde se realiza la carga.
     * @param codOrganizacion
     * @param codTramite
     * @param ocurrenciaTramite
     * @param numExpediente
     * @param request
     * @param response 
     */
    public void deshacerCargaMasivaRegexlanResolucionesDsdURL(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response) {
        log.info("deshacerCargaMasivaRegexlanResolucionesDsdURL - Begin");
        String codigoOperacion = "0";
        String desOperacion="";
        String urlDocumentosResol = "";
        String codigoProcIndicadoInterfaz = "";
        String codigoInteTramIndicaInterf = "";
        String codigoPlantilla = "";
        String eliminarDocServidor = "";
        Boolean eliminarDocServ = true;// Por defecto ponemos que se borren.
        Integer codigoUsuarioOperacion = ConstantesMeLanbide63.CODIGO_ID_USUARIO_ADMIN;
        Integer idiomaUsuarioOperacion = ConstantesMeLanbide63.IDIOMA_USUARIO_ADMIN;
        File carpetaLocal = null;
        FileWriter fw = null;
        BufferedWriter out=null;
        try {
            AdaptadorSQLBD adapt=null;
            MeLanbide63Manager meLanbide63Manager = MeLanbide63Manager.getInstance();
            // Para probar com carga FileUpload (De momento no, son muy pesados los ficheros)
            try {
                carpetaLocal = (request.getParameter("carpetaLocal") != null ? (File) request.getAttribute("carpetaLocal") : null);
            } catch (Exception e) {
                log.error("Error al recuperar el Objeto File como carpeta desde la request.", e);
            }
            // Fin Prueba como FileUpload
            try {
                adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
            } catch (Exception e) {
                log.error("Error al recuperar el Adaptador de BBDD." + e.getMessage(),e);
                codigoOperacion="1";
            }
            // Si todo correcto continuamos
            if("0".equalsIgnoreCase(codigoOperacion)){
                
                urlDocumentosResol = request.getParameter("urlDocumentosResol");
                codigoProcIndicadoInterfaz = request.getParameter("codigoProcIndicadoInterfaz");
                codigoInteTramIndicaInterf = request.getParameter("codigoInteTramIndicaInterf");
                codigoPlantilla = request.getParameter("codigoPlantilla");
                if(codigoProcIndicadoInterfaz!=null && !codigoProcIndicadoInterfaz.equalsIgnoreCase("")){
                    codigoProcIndicadoInterfaz=codigoProcIndicadoInterfaz.trim();
                    codigoProcIndicadoInterfaz=codigoProcIndicadoInterfaz.toUpperCase();
                }
                if(codigoInteTramIndicaInterf!=null && !codigoInteTramIndicaInterf.equalsIgnoreCase("")){
                    codigoInteTramIndicaInterf=codigoInteTramIndicaInterf.trim();
                }
                if(codigoPlantilla!=null && !codigoPlantilla.equalsIgnoreCase("")){
                    codigoPlantilla=codigoPlantilla.trim();
                }

                
                //Validamos el codigo de procedimiento,Codigo de tramite y codigo de plantilla indicado.
                if(meLanbide63Manager.existeProcedimiento(codigoProcIndicadoInterfaz,adapt)){
                    if (meLanbide63Manager.existeCodTramiteInternoProcedimiento(codigoProcIndicadoInterfaz,codigoInteTramIndicaInterf,adapt)) {
                        if(meLanbide63Manager.existeCodPlantillaDocumentoTramiteProcedimiento(codigoProcIndicadoInterfaz,codigoInteTramIndicaInterf,codigoPlantilla,adapt)){
                            
                            if (urlDocumentosResol == null || urlDocumentosResol.equalsIgnoreCase("")) {
                                log.info("No hemos recibido la url desde la interfaz, la cogemos del properties - ");
                                urlDocumentosResol = ConfigurationParameter.getParameter(ConstantesMeLanbide63.URL_DIRECTORIO_DOCS_CARGA_MASIVA, ConstantesMeLanbide63.FICHERO_PROPIEDADES)
                                        + codigoProcIndicadoInterfaz;
                            }
                            // Tratamos de recuperar datos del Usuario que hace la operación Sino Traceamos y asignamos codigo de administrador.
                            UsuarioValueObject usuario = new UsuarioValueObject();
                            try {
                                if (request != null && request.getSession() != null) {
                                    if (request.getSession().getAttribute("usuario") != null) {
                                        usuario = (UsuarioValueObject) request.getSession().getAttribute("usuario");
                                    }
                                    if (usuario != null) {
                                        idiomaUsuarioOperacion = usuario.getIdioma();
                                        codigoUsuarioOperacion = usuario.getIdUsuario();
                                    }
                                }
                            } catch (Exception ex) {
                                log.error("Error al recuperar los datos del usuario para la carga masiva de documentos. Asignamos el ID 5 = ADMIN.", ex);
                                codigoUsuarioOperacion = ConstantesMeLanbide63.CODIGO_ID_USUARIO_ADMIN;
                                idiomaUsuarioOperacion = ConstantesMeLanbide63.IDIOMA_USUARIO_ADMIN;
                            }
                            if (urlDocumentosResol != null && !urlDocumentosResol.equalsIgnoreCase("")) {
                                // Creamos el fichero para registrar paso a paso el proceso.
                                log.info("URL De donde se van a leer los ficheros y escribir el log : " + urlDocumentosResol);
                                String nombreFicLog =MeLanbide63GeneralUtils.preparaNombreFicheroLogCargaMasivaDoc(codigoProcIndicadoInterfaz) + ".txt";
                                File folderlogFile = new File(urlDocumentosResol+"/log/");
                                if(!folderlogFile.exists()){
                                    if(folderlogFile.mkdir()){
                                        log.info("CARPETA para escritura del log del proceso creada correctamente.");
                                    }else{
                                        log.info("CARPETA para escritura del log YA EXISTIA");
                                    }
                                }
                                File logFile = new File(folderlogFile.getAbsolutePath()+"/"+nombreFicLog);
                                if (logFile.createNewFile()) {
                                    log.info("FICHERO para escritura del log del proceso creado correctamente.");
                                } else {
                                    log.error("FICHERO para escritura del log ya existe o se presento un error al crearlo.");
                                }
                                fw = new FileWriter(logFile);
                                out = new BufferedWriter(fw);
                                out.write("Iniciamos proceso recorriendo ficheros en carpeta. " + new Date().toString());

                                final File carpeta = new File(urlDocumentosResol);
                                if (carpeta != null && carpeta.isDirectory()) {
                                    for (File ficheroIn : carpeta.listFiles()) {
                                        //Solo recorremos los Ficheros no directorios
                                        if(ficheroIn.isFile()){
                                            String Nombre = ficheroIn.getName();
                                            //Quitamos del nombre espacios en blanco y las extensiones .doc y docx. (Los documentos deberian ser solo de estos dos tipos)
                                            Nombre = Nombre.trim();
                                            Nombre = Nombre.toUpperCase();
                                            Nombre = Nombre.replaceAll(".DOCX", "");
                                            Nombre = Nombre.replaceAll(".DOC", "");
                                            // Preparamos numero de Expediente (Los ficheros deben crearse con el numero de expediente como nombre, al dividir la combinación de correspondencia. Salvo el caracter / que es prohibido)
                                            String numExpOcu = Nombre.replaceAll("_", "/");
                                            
                                            //la ocurrencia viene dada por el último valor, si lo tiene (2017/CUOTS/000017/1), si no, vendría sólo el expediente (2017/CUOTS/000017)
                                            String[] expOcu = numExpOcu.split("/");
                                            Integer ocurrencia = 0;
                                            String numExp = numExpOcu;
                                            //si tiene ocurrencia
                                            if (expOcu.length > 3){
                                                numExp = expOcu[0] + "/" + expOcu[1] + "/" + expOcu[2];
                                                ocurrencia = Integer.parseInt(expOcu[3]);
                                            }
                                            
                                            String datosBasicosExpte[] = MeLanbide63GeneralUtils.getDatosExpteDsdNumExpediente(numExp);
                                            Integer ejercicio;
                                            String codProcedimiento;
                                            ejercicio = (datosBasicosExpte != null && datosBasicosExpte.length > 0 && !datosBasicosExpte[0].equalsIgnoreCase("") ? Integer.parseInt(datosBasicosExpte[0]) : null);
                                            codProcedimiento = (datosBasicosExpte != null && datosBasicosExpte.length > 1 ? datosBasicosExpte[1] : null);
                                            /**
                                             * Comprobamos: Existe expediente El
                                             * expediente ha pasado por ese
                                             * tramite.
                                             */

                                            if (meLanbide63Manager.existeExpediente(numExp, adapt)) {
                                                if (meLanbide63Manager.existeTramiteEnExpediente(numExp, codigoInteTramIndicaInterf, adapt)) {
                                                    if (codigoProcIndicadoInterfaz.equalsIgnoreCase(codProcedimiento)) {

                                                        Nombre = ConfigurationParameter.getParameter(ConstantesMeLanbide63.PREFIJO_TEXTO_NOMBREDOC_RESOLCONCEDIDA, ConstantesMeLanbide63.FICHERO_PROPIEDADES)
                                                                + Nombre;
                                                        //Parametros Tabla
                                                        Integer tramite = codigoInteTramIndicaInterf != null && !codigoInteTramIndicaInterf.equalsIgnoreCase("") ? Integer.parseInt(codigoInteTramIndicaInterf) : 0;
                                                        
                                                        //si no hay ocurrencia especificada, se toma la última
                                                        if (expOcu.length < 4){
                                                            ocurrencia = meLanbide63Manager.obtenerMaxOcurrenciaTramiteEnExpediente(numExp, String.valueOf(tramite), adapt);
                                                        }
                                                        
                                                        // Recogemos la lista de documentos que pueda haber con el nombre como se forma en la carga, 
                                                        // en el caso de que en la carga se hayan subido mas uno, se borran todos en la primera consulta.
                                                        // Debemos incluir el OID en caso de que este en Dokusi
                                                        Integer documentoPlantilla = codigoPlantilla != null && !codigoPlantilla.equalsIgnoreCase("") ? Integer.parseInt(codigoPlantilla) : null;
                                                        List<DocumentoGestor> listaDocumentos = new ArrayList<DocumentoGestor>();
                                                        listaDocumentos = meLanbide63Manager.obtenerListaDocExpteTramiteOcurrPlantillaNombreDoc(codOrganizacion,ejercicio,codProcedimiento,numExp,tramite,ocurrencia,documentoPlantilla,Nombre,adapt);

                                                        String salida = "";
                                                        if(listaDocumentos!=null && listaDocumentos.size()>0){
                                                            log.info("Procedemos a eliminar los documentos recuperados en BBDD para : " + numExp + "-" +Nombre);
                                                            // En el caso de eliminar no hace falta validar si el procedimiento esta activo para dokusi
                                                            // En el plugin se consulta, si existe en Dokusi se borra de dokusi sino solo de BBDD.
                                                            AlmacenDocumentoDokusiImpl almacen = new AlmacenDocumentoDokusiImpl();
                                                            for (DocumentoGestor documento : listaDocumentos) {
                                                                try {
                                                                    boolean documentoEliminado = almacen.eliminarDocumento(documento);
                                                                    if (documentoEliminado) {
                                                                        salida = "1";
                                                                        out.newLine();
                                                                        out.write(numExp + " --> " + "Documento eliminado Correctamente : " + documento.getNumeroDocumento() + "-" + Nombre);
                                                                    }
                                                                } catch (Exception ex) {
                                                                    log.error("Error al Eliminar un Documento." + Nombre + "-" + numExp);
                                                                    String traza = "";
                                                                    if (ex.getStackTrace() != null && ex.getStackTrace().length > 0) {
                                                                        for (StackTraceElement stackTrace : ex.getStackTrace()) {
                                                                            traza += stackTrace.toString();
                                                                        }
                                                                    }
                                                                    salida = ex.getMessage() + " *** Traza *** " + traza;
                                                                    out.newLine();
                                                                    out.write(numExp + " --> " + "Error al Eliminar Documento : " + documento.getNumeroDocumento() + "-" + Nombre + " : \n " + salida);
                                                                }                                                                
                                                            }
                                                        }else{
                                                            log.info("No se obtuvieron Documentos desde BBDD para eliminar." + numExp + "-" + Nombre);
                                                            out.newLine();
                                                            out.write(numExp + " --> " + "No Documentos en BBDD para eliminar : " + Nombre);
                                                        }
                                                        
                                                        if (salida != null && !salida.equalsIgnoreCase("0")) {
                                                            log.info("Documentos Eliminados correctamente." + Nombre + "-" + numExp);
                                                            out.newLine();
                                                            out.write(numExp + " --> " + "Documentos eliminados Correctamente : " + Nombre);
                                                            if(eliminarDocServ){
                                                                if(ficheroIn.exists()){
                                                                    if (ficheroIn.delete()) {
                                                                        log.info("Fichero Eliminado, borrado correctamente del servidor.");
                                                                    } else {
                                                                        log.error("Fichero Eliminado, no se pudo borrar correctamente del servidor.");
                                                                    }
                                                                }
                                                            }
                                                        } else {
                                                            if (salida != null) {
                                                                log.info("No hubo errores pero no se pudo eliminar el documento : " + Nombre + "-" + numExp + ". Salida operacion: " + salida);
                                                            } else {
                                                                log.error("Se presento un error al eliminar el documento " + Nombre + "-" + numExp + ". Salida operacion: " + salida);
                                                            }
                                                            out.newLine();
                                                            out.write(numExp + " --> " + "NO se pudo eliminar el documento : " + Nombre + ". Salida operacion: " + salida);
                                                        }

                                                    } else {
                                                        log.error("Codigo de procedimiento indicado en la inerfaz no coincide con el cod procedimiento en el nombre del Documento, no eliminamos Fichero. " + numExp + "-" + Nombre + ": " + codigoProcIndicadoInterfaz + "--" + codProcedimiento);
                                                        out.newLine();
                                                        out.write(numExp + " --> " + "No coincide codigo procedimiento indicado en interfaz con codigo procedimiento en nombre del documento. Fichero no eliminado : " + Nombre + "--" + codigoProcIndicadoInterfaz);
                                                    }
                                                } else {
                                                    log.error("Tramite no existe en el Expediente, no eliminanos Documento " + codigoInteTramIndicaInterf + "-" + numExp + "-" + Nombre);
                                                    out.newLine();
                                                    out.write(numExp + " --> " + "No existe Tramite en el Expediente. Fichero no Eliminado. " + Nombre);
                                                }
                                            } else {
                                                log.error("Expediente no existe, no Eliminamos Documento " + numExp + "-" + Nombre);
                                                out.newLine();
                                                out.write(numExp + " --> " + "No existe Expediente. Fichero no Eliminado. " + Nombre);
                                            }
                                        }
                                    }
                                } else {
                                    log.error("La URL recibida no existe o no es un directorio valido." + urlDocumentosResol);
                                    codigoOperacion = "3";
                                }
                            } else {
                                log.error("No hemos conseguido la URL ni desde la request ni desde el properties - Devolcemos Error 1");
                                codigoOperacion = "2";
                            }

                        }else{
                            log.error("Codigo de plantilla indicado NO VALIDO o no existe en ese procedimiento y tramite." + codigoProcIndicadoInterfaz + "-" + codigoInteTramIndicaInterf+"-->"+codigoPlantilla);
                            codigoOperacion = "7";
                        }
                    } else {
                        log.error("Codigo de tramite indicado NO VALIDO o no existe en ese procedimiento." + codigoProcIndicadoInterfaz +"-->"+ codigoInteTramIndicaInterf );
                        codigoOperacion = "6";
                    }
                    
                }else{
                    log.error("Codigo de procedimiento indicado NO VALIDO." + codigoProcIndicadoInterfaz);
                    codigoOperacion = "5";
                }
            }
        } catch (IOException  e) {
            log.error("Error al escribir el log o leer los documentos de la carga masiva. " + e.getMessage(), e);
            codigoOperacion = "4";
        } catch (Exception e) {
            log.error("Error Generico al procesar la carga masiva de documentos Regexlan : " + e.getMessage(), e);
            codigoOperacion = "-1";
        }finally{
            try {
                if (out != null) {
                    out.newLine();
                    out.write("Poceso Finalizado. Cerramos buffer de Escritura. " + new Date().toString());
                    out.close();
                }

                if (fw != null) {
                    fw.close();
                }
            } catch (IOException ex) {
                log.error("Error al cerrar el buffer o el filewriter del log la carga masiva. " + ex.getMessage(), ex);
            }
        }
        try {
            // Traducimos el resultado de la operacion
            MeLanbide63I18n meLanbide63I18n = MeLanbide63I18n.getInstance();
            desOperacion = meLanbide63I18n.getMensaje(idiomaUsuarioOperacion, ConstantesMeLanbide63.PREFIJO_RESULTADO_CODIGO_DOT + codigoOperacion);
            
            log.info("Escribimos la respuesta en la request : " + codigoOperacion);
            StringBuilder xmlSalida = new StringBuilder();
            xmlSalida.append("<RESPUESTA>");
                xmlSalida.append("<CODIGO_OPERACION>");
                xmlSalida.append(codigoOperacion);
                xmlSalida.append("</CODIGO_OPERACION>");
                xmlSalida.append("<DETALLE_OPERACION>");
                xmlSalida.append(desOperacion);
                xmlSalida.append("</DETALLE_OPERACION>");
            xmlSalida.append("</RESPUESTA>");
            log.info("XML en la request : " + xmlSalida);
            response.setContentType("text/xml");
            response.setCharacterEncoding("UTF-8");
            PrintWriter out1 = response.getWriter();
            out1.print(xmlSalida.toString());
            out1.flush();
            out1.close();
        } catch (Exception e) {
            log.error("Error al escribir la respuesta de la llamada en la request " + e.getMessage(), e);
        }
        log.error("deshacerCargaMasivaRegexlanResolucionesDsdURL - Begin");
    }


}

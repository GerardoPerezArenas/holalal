/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package es.altia.flexia.integracion.moduloexterno.pluginnotificacionplatea;

import com.lanbide.lan6.errores.bean.ErrorBean;
import com.lanbide.lan6.registro.error.RegistroErrores;
import es.altia.agora.technical.ConstantesDatos;
import es.altia.common.exception.TechnicalException;
import es.altia.common.service.config.Config;
import es.altia.common.service.config.ConfigServiceHelper;
import es.altia.flexia.notificacion.firma.FirmaNotificacionAltia;
import es.altia.flexia.notificacion.persistence.*;
import es.altia.flexia.notificacion.plugin.PluginNotificacion;
import es.altia.flexia.notificacion.vo.*;
import es.altia.flexia.registro.digitalizacion.lanbide.util.GestionAdaptadoresLan6Config;
import es.altia.notificacion.webservice.servicioaltanotificacion.client.ServicioAltaNotificacion;
import es.altia.notificacion.webservice.servicioaltanotificacion.client.ServicioAltaNotificacionService;
import es.altia.notificacion.webservice.servicioaltanotificacion.client.ServicioAltaNotificacionServiceLocator;
import es.altia.technical.PortableContext;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.altia.util.notificaciones.NotificacionesUtil;
import es.altia.util.notificaciones.TipoNotificacionValueObject;
import es.altia.x509.certificados.validacion.ValidacionCertificado;
import es.lanbide.lan6.adaptadoresPlatea.utilidades.constantes.Lan6Constantes;
import java.io.File;
import java.io.FileOutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javax.sql.DataSource;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

public class PluginNotificacionPlatea implements PluginNotificacion {

    List<TipoNotificacionValueObject> listaTiposNotificacion = NotificacionesUtil.getTiposNotificacion();

    private static final GestionAdaptadoresLan6Config gestionAdaptadoresLan6Config = new GestionAdaptadoresLan6Config();
    
    protected static Config conf = ConfigServiceHelper.getConfig("notificaciones");

    private static Logger log = LogManager.getLogger(PluginNotificacionPlatea.class);

    private String codOrganizacion;

    public String getCodOrganizacion() {
        return codOrganizacion;
    }

    public void setCodOrganizacion(String codOrganizacion) {
        this.codOrganizacion = codOrganizacion;
    }

    public boolean existenInteresadosAdmitenNotificacion(NotificacionVO notificacionVO, String[] params) throws TechnicalException {

        boolean res=false;

	try{

            AutorizadoNotificacionManager autorizadoNotificacionManager=AutorizadoNotificacionManager.getInstance();
            res=autorizadoNotificacionManager.existenInteresadosNotificacionElectronica(notificacionVO, params);
	}catch (Exception e) {
            e.printStackTrace();
	}finally{
            return res;
	}
    }

    public NotificacionVO getNotificacion(NotificacionVO notificacionVO, String[] params) throws TechnicalException {
        log.debug("PluginNotificacionPlatea -> Entra a getNotificacion");
        
        NotificacionVO notificacionVORetorno=new NotificacionVO();
        AdaptadorSQLBD adapt = null;
        Connection con = null;
        
	try{

            adapt = new AdaptadorSQLBD(params);
            con   = adapt.getConnection();
            
            //Otengo la notificacion
            notificacionVORetorno=notificacionVO;
            PluginNotificacionPlateaNotificacionDAO plateaNotificacionDAO = PluginNotificacionPlateaNotificacionDAO.getInstance();
            notificacionVORetorno = plateaNotificacionDAO.getNotificacion(notificacionVO,con);
            
            log.debug("SACO DE BD " + new String(notificacionVORetorno.getTextoNotificacion().getBytes("UTF-8"), "ISO-8859-1"));

           //Obtengo los documentos de la notificacion
            AdjuntoNotificacionVO adjuntoNotificacionVO=new AdjuntoNotificacionVO();
            adjuntoNotificacionVO.setCodigoMunicipio(notificacionVO.getCodigoMunicipio());
            adjuntoNotificacionVO.setEjercicio(notificacionVO.getEjercicio());
            adjuntoNotificacionVO.setNumeroExpediente(notificacionVO.getNumExpediente());
            adjuntoNotificacionVO.setCodigoProcedimiento(notificacionVO.getCodigoProcedimiento());
            adjuntoNotificacionVO.setCodigoTramite(notificacionVO.getCodigoTramite());
            adjuntoNotificacionVO.setOcurrenciaTramite(notificacionVO.getOcurrenciaTramite());
            adjuntoNotificacionVO.setCodigoNotificacion(notificacionVO.getCodigoNotificacion());

            /* DOCUMENTOS DE TRAMITACIÓN ANEXOS A LA NOTIFICACIÓN */
            AdjuntoNotificacionDAO adjuntoNotificacionDAO = AdjuntoNotificacionDAO.getInstance();
            ArrayList<AdjuntoNotificacionVO> arrayDocumentos= new ArrayList<AdjuntoNotificacionVO>();
            arrayDocumentos=adjuntoNotificacionDAO.getDocumentosTramitacion(adjuntoNotificacionVO,con);
            notificacionVORetorno.setAdjuntos(arrayDocumentos);
            
            /* DOCUMENTOS EXTERNOS ANEXOS A LA NOTIFICACIÓN */
            ArrayList<AdjuntoNotificacionVO> adjuntosExternos = adjuntoNotificacionDAO.getListaAdjuntosExterno(notificacionVO.getCodigoNotificacion(), con);
            notificacionVORetorno.setAdjuntosExternos(adjuntosExternos);
            

            /* SE RECUPERAN LOS INTERESADOS DEL EXPEDIENTE */
            ArrayList<AutorizadoNotificacionVO> arrayAutorizadoNotif=new ArrayList<AutorizadoNotificacionVO>();
            AutorizadoNotificacionDAO autorizadoNotificacionDAO = AutorizadoNotificacionDAO.getInstance();
            arrayAutorizadoNotif=autorizadoNotificacionDAO.getInteresadosExpediente((notificacionVO.getNumExpediente()), notificacionVO.getCodigoNotificacion(),params[0], con);
     //       arrayAutorizadoNotif = PluginNotificacionPlateaNotificacionDAO.getInteresadosExpedientePluginNotificacion((notificacionVO.getNumExpediente()), notificacionVO.getCodigoNotificacion(), con);

            notificacionVORetorno.setAutorizados(arrayAutorizadoNotif);
	
	}catch (Exception e) {            
            log.error("PluginNotificacionPlatea.getNotificacion(): Error al recuperar la notificación: " + e.getMessage());
            e.printStackTrace();
            log.error("enviamos el error");
            
            ErrorBean error = new ErrorBean();
            error.setIdError("NOTIFICACIONES_005");
            error.setMensajeError(e.getMessage());
            error.setSituacion("enviarNotificacion");
            
            grabarError(error, e.getMessage(), e.toString(), notificacionVO.getNumExpediente());
            //RegistroErrores.registroError(error);

	}finally{
            try{
                if(con!=null) con.close();
                
            }catch(Exception e){
                log.error("Error al cerrar conexión a la BBDD");
            }            
            return notificacionVORetorno;
	}
    }

    public String getUrlPantallaDatosNotificacion() throws TechnicalException {

        String url="";


        try{
            
            String plugin=conf.getString(codOrganizacion +"/plugin");

            url=conf.getString(codOrganizacion +"/Notificacion/"+plugin+"/urlPaginaDatosNotificacion");

           

        }catch (Exception e) {
            e.printStackTrace();
	}finally{
            return url;
	}
    }

    public boolean grabarNotificacion(NotificacionVO notificacionVO, String[] params) throws TechnicalException {

        boolean res=false;
        int codigoNotificacion=-1;
        Connection con = null;
        AdaptadorSQLBD adapt = null;
        
	try{

            // SE OBTIENE CONEXIÓN A LA BBDD
            adapt = new AdaptadorSQLBD(params);
            con = adapt.getConnection();
            
            
            //Otengo la notificacion
            log.debug("Hay autorizados? " + !notificacionVO.getAutorizados().isEmpty());
            if (!notificacionVO.getAutorizados().isEmpty()){
                NotificacionManager notificacionManager=NotificacionManager.getInstance();

                codigoNotificacion=NotificacionDAO.getInstance().insertarNotificacion(notificacionVO,con);
                notificacionVO.setCodigoNotificacion(codigoNotificacion);
                ArrayList<AdjuntoNotificacionVO> arrayDocumentos= new ArrayList<AdjuntoNotificacionVO>();

                arrayDocumentos=notificacionVO.getAdjuntos();

                if (codigoNotificacion != -1) {
                    res=true;
                //Inserto documentos
                    for (int i = 0; i < arrayDocumentos.size(); i++) {
                        AdjuntoNotificacionVO adjuntoVO=new AdjuntoNotificacionVO() ;
                        adjuntoVO=arrayDocumentos.get(i);
                        if (adjuntoVO.getSeleccionado().equals("SI")) {
                            adjuntoVO.setCodigoNotificacion(codigoNotificacion);                        
                            AdjuntoNotificacionDAO adjuntoNotifDAO = AdjuntoNotificacionDAO.getInstance();
                            res=res&(adjuntoNotifDAO.insertarAdjunto(adjuntoVO, con));
                        }
                    }

                   ArrayList<AutorizadoNotificacionVO> arrayAutorizados= new ArrayList<AutorizadoNotificacionVO>();
                   arrayAutorizados=notificacionVO.getAutorizados();

                   //Inserto Autorizados
                    for (int i = 0; i < arrayAutorizados.size(); i++) {
                        AutorizadoNotificacionVO autorizadoVO=new AutorizadoNotificacionVO() ;
                        autorizadoVO=arrayAutorizados.get(i);
                        if (autorizadoVO.getSeleccionado().equals("SI")) {
                            autorizadoVO.setCodigoNotificacion(codigoNotificacion);
                            //AutorizadoNotificacionManager autorizadoNotificacionManager=AutorizadoNotificacionManager.getInstance();
                            AutorizadoNotificacionDAO autorizadoDAO = AutorizadoNotificacionDAO.getInstance();
                            res=res&(autorizadoDAO.insertarAutorizado(autorizadoVO, con));
                        }
                    }


                   
                   ArrayList<AdjuntoNotificacionVO> adjuntosExternos = notificacionVO.getAdjuntosExternos();
                    for (int i = 0; adjuntosExternos != null && i < adjuntosExternos.size(); i++) {
                        AdjuntoNotificacionVO adjuntoVO=new AdjuntoNotificacionVO() ;
                        adjuntoVO=adjuntosExternos.get(i);

                        adjuntoVO.setCodigoNotificacion(codigoNotificacion);                        
                        AdjuntoNotificacionDAO adjuntoNotifDAO = AdjuntoNotificacionDAO.getInstance();

                        res=res&(adjuntoNotifDAO.insertarAdjuntoExterno(adjuntoVO,params[0],con));

                    }
                   




                 }
            } else {
                res = false;
            }

	}catch (Exception e) {
            e.printStackTrace();
            
            //Error al guardar notificación
            ErrorBean error = new ErrorBean();
            error.setIdError("NOTIFICACIONES_001");
            error.setMensajeError("Error al guardar notificacion");
            error.setSituacion("grabarNotificacion");
            grabarError(error, e.getMessage(), e.toString(), notificacionVO.getNumExpediente());
            
	}finally{
            
            try{
                // Se cierra la conexión a la BBDD
                if(con!=null) con.close();
                
            }catch(Exception e){
                
            }
            
            return res;
	}

    }

     public boolean guardarFirma(int codigoNotificacion,String firma,String[] params) throws TechnicalException {

        boolean res=false;


	try{

            //Otengo la notificacion
            NotificacionManager notificacionManager=NotificacionManager.getInstance();

            res=notificacionManager.guardarFirma(codigoNotificacion, firma, params);

	}catch (Exception e) {
            e.printStackTrace();
            //error al guardar la firma
            
            
            
            
	}finally{
            return res;
	}

    }

    //LLAMADA AL SERVICIO WEB
    public boolean enviarNotificacion(NotificacionVO notificacionVO2, String[] params) throws TechnicalException {

        boolean dev=false;
        //Connection con = null;
        ArrayList<NotificacionBVO> notificaciones = null;

        try{
            log.debug("Autorizados? " + notificacionVO2.getAutorizados());
            // COMO PUEDE HABER VARIOS AUTORIZADOS, ENVIAMOS UNA NOTIFICACION POR CADA UNO DE ELLOS
            if (notificacionVO2.getTextoNotificacion() != null && !notificacionVO2.getTextoNotificacion().isEmpty()) {
                if(notificacionVO2.getAutorizados() != null && !notificacionVO2.getAutorizados().isEmpty()){
                    dev = PluginNotificacionPlateaNotificacionDAO.getInstance().enviarNotificacion(notificacionVO2.getCodigoNotificacion(), params);
                       
            }
            }
        } catch (TechnicalException e) {
            //Error al enviar la notificación
            log.error("Error en  enviarNotificacion", e);
            ErrorBean error = new ErrorBean();
            error.setIdError("NOTIFICACIONES_002");
            error.setMensajeError("Error al enviar notificacion");
            error.setSituacion("enviarNotificacion");
            grabarError(error, e.getMessage(), e.toString(), notificacionVO2.getNumExpediente());
        }
        return dev;
    }
    
    private String[] getTituloExtension(String fichero, String contentType, boolean esDocumentoTramite){
        log.info("Titulo " + fichero + " , tipo " + contentType);
        String[] res = new String[2];
        res[0] = "";
        res[1] = "";
        
        if(esDocumentoTramite){
            // ES DOCUMENTO DEL TRAMITE, NO TIENE EXTENSION
            //res[0] = sustituirCaracteresEspeciales(fichero);
            res[0] = fichero;
            res[1] = "pdf";
        }else{
            // ES DOCUMENTO EXTERNO ADJUNTO, SACAMOS LA EXTENSION DEL contentType
            int dotSlash = contentType != null ? contentType.lastIndexOf("/") : -1;
            if(dotSlash == -1){
                // EL contentType NO TIENE NINGUNA BARRA
                res[0] = fichero;
                res[1] = contentType;
            }else{
                // EL contentType TIENE BARRA
                res[0] = fichero;
                res[1] = contentType.substring(dotSlash + 1);
            }
        }
        
        return res;
    }
    
    private String sustituirCaracteresEspeciales(String input){
        String s = input.replaceAll(" ", "");
        s = s.replaceAll("á", "a");
        s = s.replaceAll("é", "e");
        s = s.replaceAll("í", "i");
        s = s.replaceAll("ó", "o");
        s = s.replaceAll("ú", "u");
        s = s.replaceAll("Á", "A");
        s = s.replaceAll("É", "E");
        s = s.replaceAll("Í", "I");
        s = s.replaceAll("Ó", "O");
        s = s.replaceAll("Ú", "U");
        
        return s;
    }
    
    private String getExtensionFichero(String contentType){
        int slashIndex = contentType != null ? contentType.lastIndexOf("/") : -1;
        if(slashIndex == -1){
            return contentType;
        }else{
            return contentType.substring(slashIndex + 1);
        }
    }
    
    private String getEquivalenciaTipoNotificacion(String tipoNotifFlexia){
        if(tipoNotifFlexia != null){
            if(tipoNotifFlexia.equals("1")){
                return Lan6Constantes.Notificaciones.ACTO_NOTIFICADO_REQ_SUB;
            }else if(tipoNotifFlexia.equals("2")){
                return Lan6Constantes.Notificaciones.ACTO_NOTIFICADO_RESOL;
            }else if(tipoNotifFlexia.equals("3")){
                return Lan6Constantes.Notificaciones.ACTO_NOTIFICADO_REQ_ALEG;
            }else if(tipoNotifFlexia.equals("4")){
                return Lan6Constantes.Notificaciones.ACTO_NOTIFICADO_COMUNICATION;
            }else if(tipoNotifFlexia.equals("5")){
                return Lan6Constantes.Notificaciones.ACTO_NOTIFICADO_REQUERIMIENTO;
            }else if(tipoNotifFlexia.equals("6")){
                return Lan6Constantes.Notificaciones.ACTO_NOTIFICADO_RESOL_ALEG;
            }else if(tipoNotifFlexia.equals("7")){
                return Lan6Constantes.Notificaciones.ACTO_NOTIFICADO_RESOL_RECUR;
            }else if(tipoNotifFlexia.equals("8")){
                return Lan6Constantes.Notificaciones.ACTO_NOTIFICADO_RESOL_DESIS;
            }else if(tipoNotifFlexia.equals("9")){
                return Lan6Constantes.Notificaciones.ACTO_NOTIFICADO_RESOL_RENUN;
            }else if(tipoNotifFlexia.equals("10")){
                return Lan6Constantes.Notificaciones.ACTO_NOTIFICADO_OTROS_MOD_IMPORTE;
            }else if(tipoNotifFlexia.equals("11")){
                return Lan6Constantes.Notificaciones.ACTO_NOTIFICADO_RESOL_MOD_IMPORTE;
            }else{
                return "";
            }
        }
        return "";
    }
 
    
    private void escribirFichero(String texto){  
        try{
            log.debug("El texto a escribir en los ficheros es: " + texto);
            File f = new File("/pcw1/user_projects/domains/flexia/servers/AdminServer/apps/flexia/WEB-INF/classes/es/altia/flexia/integracion/moduloexterno/pluginnotificacionplatea/utf8.txt");
            FileOutputStream fos = new FileOutputStream(f);
            fos.write(texto.getBytes("UTF-8"));
            fos.flush();
            fos.close();
            f = new File("/pcw1/user_projects/domains/flexia/servers/AdminServer/apps/flexia/WEB-INF/classes/es/altia/flexia/integracion/moduloexterno/pluginnotificacionplatea/iso88591.txt");
            fos = new FileOutputStream(f);
            fos.write(texto.getBytes("ISO-8859-1"));
            fos.flush();
            fos.close();
        }catch(Exception e){
            log.error("Error al escribir ficheros");
        }
    }

    public boolean existenDocumentosFirmados(NotificacionVO notificacionVO, String[] params) throws TechnicalException {

         boolean res=false;

	try{

            String numExpediente=notificacionVO.getNumExpediente();
            int codTramite=notificacionVO.getCodigoTramite();
            int ocuTramite=notificacionVO.getOcurrenciaTramite();

            AdjuntoNotificacionManager adjuntoNotificacionManager=AdjuntoNotificacionManager.getInstance();
            res=adjuntoNotificacionManager.existeDocumentosFirmados(numExpediente,codTramite,ocuTramite, params);
            
	}catch (Exception e) {
            e.printStackTrace();
            ErrorBean error = new ErrorBean();
            error.setIdError("NOTIFICACIONES_006");
            error.setMensajeError("Error al comprobar si existen documentos firmados");
            error.setSituacion("existenDocumentosFirmados");
            
            grabarError(error, e.getMessage(), e.toString(), notificacionVO.getNumExpediente());

	}finally{
            return res;
	}
    }

    //LLAMADA AL SERVICIO WEB
    public String getNotificacionFirma(NotificacionVO notificacionVO, String[] params) throws TechnicalException {
        AdaptadorSQLBD adapt = null;
        Connection con = null;
        String res="";

        try{

            String plugin=conf.getString(codOrganizacion +"/plugin");
            String codAplicacion=conf.getString(codOrganizacion +"/"+plugin+"/codigo_aplicacion_ws_notificacion");
            notificacionVO.setAplicacion(codAplicacion);            
            FirmaNotificacionAltia firmaNotificacion=new FirmaNotificacionAltia();
            res=firmaNotificacion.generarResumen(notificacionVO);

            adapt = new AdaptadorSQLBD(params);
            con = adapt.getConnection();
            adapt.inicioTransaccion(con);

            boolean exito = NotificacionDAO.getInstance().guardarXMLFirmaNotificacion(notificacionVO.getCodigoNotificacion(),res, con);
            if(exito) adapt.finTransaccion(con);
            else adapt.rollBack(con);

        }catch (Exception e) {
             e.printStackTrace();
             try{
                 adapt.rollBack(con);
             }catch(Exception f){
                log.error("Error al realizar un rollback contra la BBDD: " + e.getMessage());
             }
             ErrorBean error = new ErrorBean();
            error.setIdError("NOTIFICACIONES_007");
            error.setMensajeError("Error al obtener la notificacion firmada");
            error.setSituacion("getNotificacionFirma");
            grabarError(error, e.getMessage(), e.toString(), notificacionVO.getNumExpediente());

        }finally{
            try{
                adapt.devolverConexion(con);
            }catch(Exception e){
                log.error("Error al cerrar conexión a la BBDD: " + e.getMessage());
            }

        }

        return res;

    }

    public boolean verificarFirma(NotificacionVO notificacionVO, String firma,String[] params) throws TechnicalException {

      boolean res=false;
      String xml="";

	try{

            String plugin=conf.getString(codOrganizacion +"/plugin");

            String codAplicacion=conf.getString(codOrganizacion +"/"+plugin+"/codigo_aplicacion_ws_notificacion");

            notificacionVO.setAplicacion(codAplicacion);

            FirmaNotificacionAltia firmaNotificacion=new FirmaNotificacionAltia();
            xml=firmaNotificacion.generarResumen(notificacionVO);

            byte[] documento = null;

            documento = xml.getBytes();

            File f = File.createTempFile("prueba",".temp");
            FileOutputStream fos = new FileOutputStream(f);
            fos.write(documento);
            fos.flush();
            fos.close();

            res = verificarFirma(f,firma);

            f.delete();
	}catch (Exception e) {
            e.printStackTrace();
            ErrorBean error = new ErrorBean();
            error.setIdError("NOTIFICACIONES_008");
            error.setMensajeError("Error al verificar la firma");
            error.setSituacion("verificarFirma");
            
            grabarError(error, e.getMessage(), e.toString(), notificacionVO.getNumExpediente());

	}finally{
            return res;
	}

    }

    public boolean verificarFirma(File documento,String firma){
       ValidacionCertificado validar = new ValidacionCertificado();
        return validar.verificarFirma(documento, firma);
    }

    public boolean guardarEstadoNotificacionEnviada(NotificacionVO notificacionVO, String[] params) throws TechnicalException {
          boolean res=false;


	try{

            //Otengo la notificacion
            NotificacionManager notificacionManager=NotificacionManager.getInstance();
            //#282252
            //res=notificacionManager.guardarEstadoNotificacionEnviada(notificacionVO,params);
            res=true;

	}catch (Exception e) {
            e.printStackTrace();
            ErrorBean error = new ErrorBean();
            error.setIdError("NOTIFICACIONES_009");
            error.setMensajeError("Error al guardar el estado de la notificacion");
            error.setSituacion("guardarEstadoNotificacionEnviada");
            grabarError(error, e.getMessage(), e.toString(), notificacionVO.getNumExpediente());
	}finally{
            return res;
	}

     }


     
    public boolean actualizarNotificacion(NotificacionVO notificacionVO, String[] params) throws TechnicalException{
        boolean res=false;        
        Connection con = null;
        AdaptadorSQLBD adapt = null;
        
	try{
            // SE OBTIENE CONEXIÓN A LA BBDD
            adapt = new AdaptadorSQLBD(params);
            con = adapt.getConnection();
            adapt.inicioTransaccion(con);
            log.debug("Hay autorizados? " + !notificacionVO.getAutorizados().isEmpty());
            boolean actualizacionAutorizados = false;            
            boolean actualizacionAdjuntosTramitacion = false;
            boolean actualizado = false;
            if(!notificacionVO.getAutorizados().isEmpty()){
                int codigoNotificacion = notificacionVO.getCodigoNotificacion();
                log.debug("******************* scodigoNotificacion : " + codigoNotificacion);

                actualizado = NotificacionDAO.getInstance().updateNotificacion(notificacionVO,con);
                //actualizado = PluginNotificacionPlateaNotificacionDAO.getInstance().updateNotificacion(notificacionBVO, codOrganizacion,adapt);
                if(!actualizado){
                    log.error("PluginNotificacionPlatea error al actualizar la notificación");
                    res = false;                
                }else{

                    ArrayList<AdjuntoNotificacionVO> arrayDocumentos= new ArrayList<AdjuntoNotificacionVO>();

                    arrayDocumentos=notificacionVO.getAdjuntos();                                        


                    /*** DOCUMENTOS DE TRAMITACIÓN  ***/                        
                    AdjuntoNotificacionDAO adjuntoNotifDAO = AdjuntoNotificacionDAO.getInstance();                 
                    actualizacionAdjuntosTramitacion = adjuntoNotifDAO.actualizarAdjunto(notificacionVO.getCodigoNotificacion(),notificacionVO.getNumExpediente(),notificacionVO.getCodigoMunicipio(),notificacionVO.getCodigoTramite(),notificacionVO.getOcurrenciaTramite(),notificacionVO.getAdjuntos(),con);                                            


                    /****** AUTORIZADOS *******/
                    ArrayList<AutorizadoNotificacionVO> arrayAutorizados = new ArrayList<AutorizadoNotificacionVO>();
                    arrayAutorizados=notificacionVO.getAutorizados();

                    AutorizadoNotificacionDAO autorizadoDAO = AutorizadoNotificacionDAO.getInstance();                        
                    actualizacionAutorizados = autorizadoDAO.actualizarAutorizado(notificacionVO.getCodigoNotificacion(),notificacionVO.getCodigoMunicipio(),notificacionVO.getNumExpediente(),arrayAutorizados,con);


                }// else


                if(actualizado && actualizacionAutorizados && actualizacionAdjuntosTramitacion){
                    adapt.finTransaccion(con);
                    res = true;
                } else {
                    adapt.rollBack(con);
                    res = false;
                }
            }else res=false;

	
	}catch (Exception e) {
            res = false;
            e.printStackTrace();
            ErrorBean error = new ErrorBean();
            error.setIdError("NOTIFICACIONES_010");
            error.setMensajeError("Error al actualizar la notificacion");
            error.setSituacion("actualizarNotificacion");
            grabarError(error, e.getMessage(), e.toString(), notificacionVO.getNumExpediente());
            try{
                adapt.rollBack(con);
                
            }catch(Exception f){
                f.printStackTrace();
            }
            
	}finally{
            
            try{
                // Se cierra la conexión a la BBDD
                if(con!=null) con.close();                
            }catch(Exception e){
                log.error("Error al cerrar la conexión a la BBDD: " + e.getMessage());
            }            
            return res;
	}
        
    }
    
    
    public void crearNotificacionDefecto(NotificacionVO notificacion,String[] params){
        Connection con = null;
        AdaptadorSQLBD adapt = null;
        try{
            adapt = new AdaptadorSQLBD(params);
            con = adapt.getConnection();
            adapt.inicioTransaccion(con);
            
            NotificacionDAO.getInstance().crearNotificacionPorDefecto(notificacion, con);
            
            adapt.finTransaccion(con);
            
        }catch(Exception e){
            e.printStackTrace();
            ErrorBean error = new ErrorBean();
            error.setIdError("NOTIFICACIONES_011");
            error.setMensajeError("Error crear la notificación defecto");
            error.setSituacion("crearNotificacionDefecto");
            
            grabarError(error, e.getMessage(), e.toString(), notificacion.getNumExpediente());

            try{
                adapt.rollBack(con);
            }catch(Exception f){
                log.error("Error al realizar un rollback: " + f.getMessage());
            }
        }finally{
            try{
                adapt.devolverConexion(con);
            }catch(Exception e){
                log.error("Error al cerrar la conexión a la BBDD: " + e.getMessage());
            }
        }        
    }



    public es.altia.merlin.notificacion.webservices.servicioaltanotificacion.vo.NotificacionVO consultarNotificacionSNE(String codOrganizacion,String numeroRegistroRT){

        ServicioAltaNotificacionService servicio;
        ServicioAltaNotificacion port=null;
        es.altia.merlin.notificacion.webservices.servicioaltanotificacion.vo.NotificacionVO notif = null;
        try {
            ResourceBundle bundle = ResourceBundle.getBundle("notificaciones");
            String PLUGIN = bundle.getString(codOrganizacion + "/plugin");
            String URL_WS = bundle.getString(codOrganizacion + "/Notificacion/" + PLUGIN + "/servicioWebNotificacion");

            URL urlEndPoint = new URL(URL_WS);
            servicio = new ServicioAltaNotificacionServiceLocator();
            port = servicio.getServicioAltaNotificacion(urlEndPoint);

            notif = port.consultarNotificacion(numeroRegistroRT);

        } catch (MalformedURLException error) {
            // ERROR AL GENERAR LA SALIDA TELEMATICA
            error.printStackTrace();
        } catch (Exception error) {
            // ERROR AL GENERAR LA SALIDA TELEMATICA
            error.printStackTrace();
        }

        return notif;

    }

    public static void grabarError(ErrorBean error, String excepError, String causa, String numExp) {
        try {
            log.error("grabando el error");
            error.setMensajeExcepError(excepError); //churro e.getException
            error.setTraza(excepError);
            error.setCausa(causa);//causa    .getMensaje.getcausa
            log.error("causa: " + causa);
            log.error("numExp: " + numExp);
            if ("".equals(numExp)) {
                numExp = "0000/ERR/000000";
            }

            String valor[] = numExp.split("/");
            String idProcedimiento = "";
            if (valor.length > 1) {
                idProcedimiento = gestionAdaptadoresLan6Config.getCodProcedimientoPlatea(valor[1]);
            }
            log.error("idProcedimiento: " + idProcedimiento);
            error.setIdProcedimiento(idProcedimiento);
            error.setSistemaOrigen("REGEXLAN");
            error.setIdClave("");
            error.setErrorLog("flexia_debug");
            error.setIdFlexia(numExp);

            String respGrabarError = RegistroErrores.registroError(error);
            log.error("Respuesta RegistroErrores.registroError(): " + respGrabarError);
        } catch (Exception ex) {
            log.error("Error al grabarError" + ex);
        }
    }

// Cambiamos la forma de recoger el Adaptador de BD - Este debe ser generico -  Sin organizacion, como FLBGEN
    private AdaptadorSQLBD getAdaptSQLBD_GestionErores() {
        if (log.isDebugEnabled()) {
            log.debug("getAdaptSQLBD_GestionErores()");
        }
        ResourceBundle config = ResourceBundle.getBundle("techserver");
        String gestor = config.getString("CON.gestor");
        String jndiError = config.getString("CON.jndi.GestionErrores");
        Connection conErrores = null;
        String[] salida = null;

        if (log.isDebugEnabled()) {
            log.debug("getJndi =========> ");
            log.debug("gestor: " + gestor);
            log.debug("jndi: " + jndiError);
        }//if(log.isDebugEnabled())

        DataSource ds = null;
        AdaptadorSQLBD adapt = null;
        synchronized (this) {
            try {
                PortableContext pc = PortableContext.getInstance();
                if (log.isDebugEnabled()) {
                    log.debug("He cogido el jndi: " + jndiError);
                }
                ds = (DataSource) pc.lookup(jndiError, DataSource.class);
                // Conexión al esquema genérico
                conErrores = ds.getConnection();
                salida = new String[7];
                if (jndiError != null && gestor != null && !"".equals(jndiError) && !"".equals(gestor)) {
                    salida[0] = gestor;
                    salida[1] = "";
                    salida[2] = "";
                    salida[3] = "";
                    salida[4] = "";
                    salida[5] = "";
                    salida[6] = jndiError;
                    adapt = new AdaptadorSQLBD(salida);
                }
                conErrores.close();

            } catch (TechnicalException te) {
                te.printStackTrace();
                log.error("*** AdaptadorSQLBD: " + te.toString());
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (conErrores != null && !conErrores.isClosed()) {
                        conErrores.close();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }//try-catch
            }// finally
            if (log.isDebugEnabled()) {
                log.debug("getAdaptSQLBD_GestionErores() : END");
            }
        }// synchronized
        return adapt;
    }//getAdaptSQLBD_GestionErores

private AdaptadorSQLBD getAdaptSQLBD(String codOrganizacion){
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
        
        if(log.isDebugEnabled()){
            log.debug("getJndi =========> ");
            log.debug("parametro codOrganizacion: " + codOrganizacion);
            log.debug("gestor: " + gestor);
            log.debug("jndi: " + jndiGenerico);
        }//if(log.isDebugEnabled())

        DataSource ds = null;
        AdaptadorSQLBD adapt = null;
        synchronized (this) {
            try{
                PortableContext pc = PortableContext.getInstance();
                if (log.isDebugEnabled()) {
                    log.debug("He cogido el jndi: " + jndiGenerico);
                }
                ds = (DataSource)pc.lookup(jndiGenerico, DataSource.class);
                // Conexión al esquema genérico
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
                try{
                    if (st != null) {
                        st.close();
                    }
                    if (rs != null) {
                        rs.close();
                    }
                    if (conGenerico != null && !conGenerico.isClosed()) {
                    conGenerico.close();
                    }
                }catch(SQLException e){
                    e.printStackTrace();
                }//try-catch
            }// finally
            if (log.isDebugEnabled()) {
                log.debug("getConnection() : END");
            }
         }// synchronized
        return adapt;
     }//getConnection

    @Override
    public boolean existenDocumentosPendientesFirma(NotificacionVO notificacionVO, String[] params) throws TechnicalException {
        log.debug("inicio existenDocumentosPendientesFirma");
        boolean res=false;
        
	try{
            String numExpediente = notificacionVO.getNumExpediente();
            int codTramite = notificacionVO.getCodigoTramite();
            int ocuTramite = notificacionVO.getOcurrenciaTramite();

            AdjuntoNotificacionManager adjuntoNotificacionManager = AdjuntoNotificacionManager.getInstance();
            res = adjuntoNotificacionManager.existenDocumentosPendientesFirma(numExpediente, codTramite, ocuTramite, params);
	}catch (Exception e) {
            e.printStackTrace();
	}finally{
            log.debug("existenDocumentosPendientesFirma - resultado: " + res);

            return res;
	}
    }
}

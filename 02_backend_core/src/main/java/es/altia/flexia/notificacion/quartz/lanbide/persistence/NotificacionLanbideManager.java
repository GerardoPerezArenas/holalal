/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.notificacion.quartz.lanbide.persistence;

import es.altia.common.service.config.Config;
import es.altia.common.service.config.ConfigServiceHelper;
import es.altia.common.service.mail.MailHelper;
import es.altia.flexia.notificacion.vo.AnotacionSinAdjuntoVO;
import java.util.Vector;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author david.vidal
 */
public class NotificacionLanbideManager {

    private static NotificacionLanbideManager instance = null;
    protected static Config m_ConfigTechnical = ConfigServiceHelper.getConfig("techserver");
    protected static Config m_ConfigError;
    protected static Log m_Log = LogFactory.getLog(NotificacionLanbideManager.class.getName());

    protected NotificacionLanbideManager() {
        // Queremos usar el fichero de configuración technical
        m_ConfigTechnical = ConfigServiceHelper.getConfig("techserver");
        // Queremos tener acceso a los mensajes de error localizados
        m_ConfigError = ConfigServiceHelper.getConfig("error");
    }//NotificacionLanbideManager

    public static NotificacionLanbideManager getInstance() {
        if(m_Log.isDebugEnabled()) m_Log.debug("getInstance() : BEGIN");
        if (instance == null) {
            synchronized(NotificacionLanbideManager.class) {
                if (instance == null) {
                    if(m_Log.isDebugEnabled()) m_Log.debug("Creamos una nueva instancia");
                    instance = new NotificacionLanbideManager();
                }//if (instance == null) 
            }//synchronized(NotificacionLanbideManager.class) 
        }//if (instance == null) 
        if(m_Log.isDebugEnabled()) m_Log.debug("getInstance() : END");
        return instance;
    }//getInstance
    
    public void getAnotacionesConAsunto(String[] params){
        if(m_Log.isDebugEnabled()) m_Log.debug("getAnotacionesConAsunto() : BEGIN");
        if(m_Log.isDebugEnabled()) m_Log.debug("params "+params[0]+" -- "+params[6]);
    
        Config m_Conf = ConfigServiceHelper.getConfig("notificaciones_lanbide");
        String asunto = m_Conf.getString("COD_ASUNTO_CODIFICADO");
        String dias_Anotaciones = m_Conf.getString("NUMERO_DIAS_REGISTRO_ANOTACIONES");
        String textoAsuntoEmail  = new String("");
        String textoNotif = m_Conf.getString("TEXTO_NOTIFICACION_EMAIL");
        String textoNotifEusk = "";
        
        try{
            if(m_Log.isDebugEnabled()) m_Log.debug("Recuperamos el mensaje en euskera si existe");
            textoNotifEusk = m_Conf.getString("TEXTO_NOTIFICACION_EMAIL_EUSK");
        }catch(Exception ex){
            m_Log.error("No existe la propiedad en Euskera");
        }//try-catch
        
        try{
            if(m_Log.isDebugEnabled()) m_Log.debug("Recuperamos el asunto del email");
            textoAsuntoEmail = m_Conf.getString("TEXTO_ASUNTO_EMAIL");
        }catch(Exception ex){
            m_Log.error("No existe un asunto para los emails en el fichero de propiedades");
        }//try-catch
        
        Vector resultado = new Vector(); 
        String correo = "";
        String mensaje = "";
        String mensajeEusk = "";
        try{
            NotificacionLanbideDAO notif =  NotificacionLanbideDAO.getInstance();    
            if(m_Log.isDebugEnabled()) m_Log.debug("Recuperamos las anotaciones con el asunto indicado (" + asunto + ")");
            resultado = notif.getAnotacionesNotificacionConAsunto(asunto, dias_Anotaciones,params);
    
            if ((resultado != null) && (resultado.size() >0)){
                AnotacionSinAdjuntoVO aux  = new AnotacionSinAdjuntoVO();
                for (int i=0; i < resultado.size(); i++){
                    aux = (AnotacionSinAdjuntoVO)resultado.elementAt(i);
                    correo = notif.RecuperaCorreo(aux,params);
                    if(m_Log.isInfoEnabled()) m_Log.info("Se va a enviar email a la siguiente direccion: " + correo);
                    if (correo != null){
                        try{
                            mensaje = textoNotif.replace("[NUMERO_ENTRADA]",aux.getEjercicio_anotacion() + "/" + aux.getNumero_anotacion() );
                            mensaje = mensaje.replace("[XX/XX/XXXX]",aux.getFecha_proceso());
                            if(textoNotifEusk != null && !"".equalsIgnoreCase(textoNotifEusk)){
                                mensajeEusk = textoNotifEusk.replace("[NUMERO_ENTRADA]",aux.getEjercicio_anotacion() + "/" + aux.getNumero_anotacion() );
                                mensajeEusk = mensajeEusk.replace("[XX/XX/XXXX]",aux.getFecha_proceso_alt());
                                mensaje += "\n \n \n " + mensajeEusk;
                            }//if(texto_Notif_Eusk != null && !"".equalsIgnoreCase(texto_Notif_Eusk))
                            
                            MailHelper DCorreo = new MailHelper();
                            DCorreo.sendMail(correo,textoAsuntoEmail, mensaje, null);
                            
                            //Se elimina la parte que guarda las notificaciones para que no se vuelvan a enviar de nuevo
                            //notif.GuardarNotificacion(aux, "0",params);
                        }catch (Exception e){
                            e.printStackTrace();
                            m_Log.error("NOTIFICACION LANBIDE: Error en el envio del correo electronico con mensaje: " + mensaje + " " + e.getMessage());
                            //Se elimina la parte que guarda las notificaciones para que no se vuelvan a enviar de nuevo
                            //notif.GuardarNotificacion(aux,"2",params);
                        }//try-catch
                    }else{
                        if(m_Log.isDebugEnabled()) m_Log.debug("El correo es nulo");
                        //Se elimina la parte que guarda las notificaciones para que no se vuelvan a enviar de nuevo
                        //notif.GuardarNotificacion(aux,"1",params);
                    }//if (correo != null)
                }//for (int i=0; i < resultado.size(); i++)
            }//if ((resultado != null) && (resultado.size() >0))
        }catch (Exception e){
            m_Log.debug("NOTIFICACION LANBIDE: Error en NotificacionLanbiderManager " + e.getMessage());
        }//try-catch
        if(m_Log.isInfoEnabled()) m_Log.info("NotificacionLanbideManager.getAnotacionesConAsunto()::email enviado con exito");
        if(m_Log.isDebugEnabled()) m_Log.debug("getAnotacionesConAsunto() : END");
    }//getAnotacionesConAsunto
  
}//class
    
   

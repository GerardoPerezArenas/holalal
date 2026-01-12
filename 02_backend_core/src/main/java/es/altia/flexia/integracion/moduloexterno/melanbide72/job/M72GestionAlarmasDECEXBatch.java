
package es.altia.flexia.integracion.moduloexterno.melanbide72.job;

import es.altia.agora.technical.ConstantesDatos;
import es.altia.common.exception.TechnicalException;
import es.altia.common.service.mail.MailHelper;
import es.altia.flexia.integracion.moduloexterno.melanbide72.job.manager.M72GestionAlarmasDECEXBatchManager;
import es.altia.flexia.integracion.moduloexterno.melanbide72.manager.MeLanbide72Manager;
import es.altia.flexia.integracion.moduloexterno.melanbide72.util.ConfigurationParameter;
import es.altia.flexia.integracion.moduloexterno.melanbide72.util.ConstantesMeLanbide72;
import es.altia.flexia.integracion.moduloexterno.melanbide72.vo.AlarmaVO;
import es.altia.technical.PortableContext;
import es.altia.util.conexion.AdaptadorSQLBD;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import javax.sql.DataSource;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

/**
 *
 * @author gerardo.perez
 */
public class M72GestionAlarmasDECEXBatch {
    
    private static final Logger log = LogManager.getLogger(M72GestionAlarmasDECEXBatch.class);

    public void envioCorreosAlarma1(AdaptadorSQLBD adaptadorSQLBD, String numExp){
    try
    {
      log.info("Entramos en envioCorreosAlarma1");
      String codOrganizacion = "0";
      
      log.info("--------------------------------");
      M72GestionAlarmasDECEXBatchManager m72BatchManager = M72GestionAlarmasDECEXBatchManager.getInstance();
      log.info("--------------------------------");
      
      List<AlarmaVO> listaAlarmas = m72BatchManager.getListaExpedienteMailAlarma1(adaptadorSQLBD);
      log.info("numero de listaAlarmas1 " + listaAlarmas.size());
      if(listaAlarmas!=null && !listaAlarmas.isEmpty()) {
     
           ResourceBundle propmail = ResourceBundle.getBundle("melanbide72AlarmasMail");
                String maildestino = propmail.getString("alarma1.mail.destino");
                String mailasunto = propmail.getString("alarma1.mail.asunto");
                
                String mailcontenido = propmail.getString("alarma1.mail.contenido");
        String contenidoFinal ="";
                
                if ((mailcontenido != null) && (!mailcontenido.isEmpty()))
        {          
                for (AlarmaVO alarmavo : listaAlarmas) {
                String contenidoFinalExpte = mailcontenido;
                contenidoFinalExpte = contenidoFinalExpte.replaceAll("@fechaAltaRegistro@", alarmavo.getFechaAltaRegistro());
                contenidoFinalExpte = contenidoFinalExpte.replaceAll("@numeroEntradaRegistro@", alarmavo.getNumeroEntradaRegistro());
                contenidoFinalExpte = contenidoFinalExpte.replaceAll("@numeroexpediente@", alarmavo.getNumeroExpediente()) + " \n ";
                contenidoFinal += contenidoFinalExpte;
            }          
        }
                String mailfrom = propmail.getString("mail.from");
                MailHelper DCorreo = new MailHelper();
                DCorreo.sendMail(maildestino, mailasunto, contenidoFinal, mailfrom);
      
                    }                       
                        }  
         catch (Exception e) 
         {
            e.printStackTrace();
        } 
         log.debug("envioCorreoAlarma1() : END");                   
    }
  
    public void envioCorreosAlarma2(AdaptadorSQLBD adaptadorSQLBD){
        try {
            log.info("Entramos en envioCorreosAlarma2");
             String codOrganizacion ="0";
             
            log.info("--------------------------------");         
            M72GestionAlarmasDECEXBatchManager m72BatchManager = M72GestionAlarmasDECEXBatchManager.getInstance();
            log.info("--------------------------------");
            
            List<AlarmaVO> listaAlarmas = m72BatchManager.getListaExpedienteMailAlarma2(adaptadorSQLBD);
            log.info("numero de listaAlarmas2 " + listaAlarmas.size());
            if(listaAlarmas!=null && !listaAlarmas.isEmpty()) {              
                                      
            ResourceBundle propmail = ResourceBundle.getBundle("melanbide72AlarmasMail");
            String maildestino = propmail.getString("alarma2.mail.destino");
            String mailasunto = propmail.getString("alarma2.mail.asunto");
            String mailcontenidoBase = propmail.getString("alarma2.mail.contenido.base");
             
            String contenidoFinal = " ";
                       
            String contenidoTextoAlarma1= "";
            String contenidoTextoAlarma2= "";
            String contenidoTextoAlarma3= "";
            String contenidoTextoAlarma4= "";
 
            if ((mailcontenidoBase != null) && (!mailcontenidoBase.isEmpty())){                 
            contenidoFinal += mailcontenidoBase + " \n";
           
                for (AlarmaVO alarmavo : listaAlarmas) {                    
                    if(alarmavo.getsubCasoAlarma().startsWith("101")){
                        if(contenidoTextoAlarma1.isEmpty())
                            contenidoTextoAlarma1 = propmail.getString("alarma2.mail.contenido.texto.1");
                            contenidoTextoAlarma1 += " \n - " + alarmavo.getNumeroExpediente() ;
                    }else if(alarmavo.getsubCasoAlarma().startsWith("131")){
                        if(contenidoTextoAlarma2.isEmpty())    
                            contenidoTextoAlarma2 = propmail.getString("alarma2.mail.contenido.texto.2");
                            contenidoTextoAlarma2 += " \n - " + alarmavo.getNumeroExpediente() ;
                    }else if(alarmavo.getsubCasoAlarma().startsWith("191")){
                        if(contenidoTextoAlarma3.isEmpty())    
                            contenidoTextoAlarma3 = propmail.getString("alarma2.mail.contenido.texto.3");
                            contenidoTextoAlarma3 += " \n - " + alarmavo.getNumeroExpediente() ;
                    }else if( alarmavo.getsubCasoAlarma().startsWith("181")){
                        if(contenidoTextoAlarma4.isEmpty())   
                            contenidoTextoAlarma4 = propmail.getString("alarma2.mail.contenido.texto.4");
                            contenidoTextoAlarma4 += " \n - " + alarmavo.getNumeroExpediente() ;
                    }
                }
            
                     if(!contenidoTextoAlarma1.isEmpty()){
                    contenidoFinal += " \n " + contenidoTextoAlarma1;
                    
                    }
                    if(!contenidoTextoAlarma2.isEmpty()){
                    contenidoFinal += " \n " + contenidoTextoAlarma2;
                    
                    }
                    if(!contenidoTextoAlarma3.isEmpty()){
                    contenidoFinal += " \n " + contenidoTextoAlarma3;
                    
                    }
                    if(!contenidoTextoAlarma4.isEmpty()){
                    contenidoFinal += " \n " + contenidoTextoAlarma4;
                    
                    }
//       }
                
      
        String mailfrom = propmail.getString("mail.from");
        MailHelper DCorreo = new MailHelper();
        DCorreo.sendMail(maildestino, mailasunto, contenidoFinal, mailfrom);
      
    }
      } }
        catch (Exception e) 
        {
            e.printStackTrace();
            log.error("Error al enviar Alarma2",e);
            log.error("Errorrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrrr " );
        } 
            log.debug("ErrorenvioCorreoAalrma2() : END");
        
        
    }
    
    public void envioCorreosAlarma3(AdaptadorSQLBD adaptadorSQLBD){
         try 
         {
            log.info("Entramos en envioCorreosAlarma3");
            String codOrganizacion ="0";
            
            log.info("--------------------------------");         
            M72GestionAlarmasDECEXBatchManager m72BatchManager = M72GestionAlarmasDECEXBatchManager.getInstance();
            log.info("--------------------------------");
            log.info("--------------------------------");
            log.info("--------------------------------");
            log.info("--------------------------------");
            log.info("--------------------------------");
            log.info("--------------------------------");
            
            List<AlarmaVO> listaAlarmas = m72BatchManager.getListaExpedienteMailAlarma3(adaptadorSQLBD);
            log.info("numero de listaAlarmas3 " + listaAlarmas.size());
            if(listaAlarmas!=null && !listaAlarmas.isEmpty()) {
                
            
                ResourceBundle propmail = ResourceBundle.getBundle("melanbide72AlarmasMail");
                String maildestino = propmail.getString("alarma3.mail.destino");
                String mailasunto = propmail.getString("alarma3.mail.asunto");
                
                String mailcontenido = propmail.getString("alarma3.mail.contenido");
                String mailcontenidoVariable = propmail.getString("alarma3.mail.contenido.variable");
                String contenidoFinal = mailcontenido;
                
                if ((mailcontenido != null) && (!mailcontenido.isEmpty()))
                {
                    for(AlarmaVO alarmavo : listaAlarmas){
                        String contenidoFinalExpte = mailcontenidoVariable;
                        contenidoFinalExpte = contenidoFinalExpte.replaceAll("@fechaAltaRegistro@", alarmavo.getFechaAltaRegistro());
                        contenidoFinalExpte = contenidoFinalExpte.replaceAll("@numeroEntradaRegistro@", alarmavo.getNumeroEntradaRegistro());
                        contenidoFinalExpte = contenidoFinalExpte.replaceAll("@numeroExpediente@", alarmavo.getNumeroExpediente()) + " \n ";
                        contenidoFinal += contenidoFinalExpte;                
                    }                
                                       
                
                }
                String mailfrom= propmail.getString("mail.from");
                MailHelper DCorreo = new MailHelper();
                DCorreo.sendMail(maildestino, mailasunto, contenidoFinal, mailfrom);
                    }                       
                        }  
           
         catch (Exception e) 
         {
            e.printStackTrace();
        } 
         log.debug("envioCorreoAlarma3() : END");                   
    }
    
    public void envioCorreosAlarma4(AdaptadorSQLBD adaptadorSQLBD){
         try {
               log.info("Entramos en envioCorreosAlarma4");
               String codOrganizacion ="0";
                
               log.info("--------------------------------");         
               M72GestionAlarmasDECEXBatchManager m72BatchManager = M72GestionAlarmasDECEXBatchManager.getInstance();
               log.info("--------------------------------");
               log.info("--------------------------------");
               log.info("--------------------------------");
               log.info("--------------------------------");
               log.info("--------------------------------");
           
               List<AlarmaVO> listaAlarmas = m72BatchManager.getListaExpedienteMailAlarma4(adaptadorSQLBD);
               log.info("numero de expedientes Alarma 4 " + listaAlarmas.size());
               if(listaAlarmas!=null && !listaAlarmas.isEmpty()) {
                 
                   ResourceBundle propmail = ResourceBundle.getBundle("melanbide72AlarmasMail");
                   String maildestino = propmail.getString("alarma4.mail.destino");
                   String mailasunto = propmail.getString("alarma4.mail.asunto");
                   
                   String mailcontenido = propmail.getString("alarma4.mail.contenido");
                   String mailcontenidoVariable = propmail.getString("alarma4.mail.contenido.variable");
                   String contenidoFinal = mailcontenido;           
                              
                
                    if ((mailcontenido != null) && (!mailcontenido.isEmpty()))
                {
                    for(AlarmaVO alarmavo : listaAlarmas){
                        String contenidoFinalExpte = mailcontenidoVariable;
                        contenidoFinalExpte = contenidoFinalExpte.replaceAll("@fechaAltaRegistro@", alarmavo.getFechaAltaRegistro());
                        contenidoFinalExpte = contenidoFinalExpte.replaceAll("@numeroEntradaRegistro@", alarmavo.getNumeroEntradaRegistro());
                        contenidoFinalExpte = contenidoFinalExpte.replaceAll("@numeroExpediente@", alarmavo.getNumeroExpediente()) + " \n ";
                        contenidoFinal += contenidoFinalExpte;                
                    }                
                                       
                
                }
                String mailfrom= propmail.getString("mail.from");
                MailHelper DCorreo = new MailHelper();
                DCorreo.sendMail(maildestino, mailasunto, contenidoFinal, mailfrom);
                    }                       
            
                }  
            catch (Exception e) 
            {
            e.printStackTrace();
            } 
            log.debug("envioCorreoAlarma4() : END");
        }
    private AdaptadorSQLBD getAdaptSQLBD(String codOrganizacion){
       if(log.isDebugEnabled()) log.error("getConnection ( codOrganizacion = " + codOrganizacion + " ) : BEGIN");
       ResourceBundle config = ResourceBundle.getBundle("techserver");
       String gestor = config.getString("CON.gestor");
       String jndiGenerico = config.getString("CON.jndi");
       Connection conGenerico = null;
       Statement st = null;
       ResultSet rs = null;
       String[] salida = null;
       Connection con = null;
        
        if(log.isDebugEnabled()){
            log.error("getJndi =================================================> ");
            log.error("parametro codOrganizacion: " + codOrganizacion);
            log.error("gestor: " + gestor);
            log.error("jndi: " + jndiGenerico);
        }

        DataSource ds = null;
        AdaptadorSQLBD adapt = null;
        synchronized (this) {
            try{
                PortableContext pc = PortableContext.getInstance();
                if(log.isDebugEnabled())log.error("He cogido el jndi: " + jndiGenerico);
                ds = (DataSource)pc.lookup(jndiGenerico, DataSource.class);
                // Conexi?n al esquema gen?rico
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
                    if(st!=null) st.close();
                    if(rs!=null) rs.close();
                    if(conGenerico!=null && !conGenerico.isClosed())
                    conGenerico.close();
                }catch(SQLException e){
                    e.printStackTrace();
                }//try-catch
            }// finally
            if(log.isDebugEnabled()) log.error("getConnection() : END");
         }// synchronized
        return adapt;
     }//getConnection
    
    
}

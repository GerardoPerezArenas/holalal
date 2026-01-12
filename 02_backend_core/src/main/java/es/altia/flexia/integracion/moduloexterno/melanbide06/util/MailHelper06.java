package es.altia.flexia.integracion.moduloexterno.melanbide06.util;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import es.altia.common.service.mail.exception.*;
import es.altia.common.exception.CriticalException;

import java.util.*;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;
import javax.naming.InitialContext;
import javax.naming.NamingException;


/**
 * Clase de ayuda para crear y enviar un mail
 */
public class MailHelper06 {

    public MailHelper06() {
        //Queremos usar el fichero de configuración technical
        configMail = ResourceBundle.getBundle("mail06");
    }

    /**
     *  Envia un mail
     */
  

     private String getRecipient(String tipo) throws CriticalException{

         String  mail="";

        try {
             mail=configMail.getString(tipo);
             return mail;


        } catch (CriticalException e) {
            mail="";
            return mail;
        }
     }
     
     
      public void sendMail(String email, String subject, String textoMensaje, String from) throws MailException,
            MailServiceNotActivedException {

            String  mailCC="";
            String  mailBCC="";
            Transport transporte=null;
        //Si el servicio de mail no esta activado, se lanza una excepcion
        try {
            String servicioActivo = configMail.getString("mail.service.active");
            if (!servicioActivo.equalsIgnoreCase("si")) {
                throw new MailServiceNotActivedException("Servicio de mail no activado");
            }
        } catch (CriticalException e) {
            throw new MailException(configMail.getString("Error.Mail.Envio"), e);
        }
        try {
             mailCC=configMail.getString("mail.cc");
             mailBCC=configMail.getString("mail.bcc");
             
        } catch (CriticalException e) {
            mailCC="";
            mailBCC="";
        }
                

        Session session = null;
        String puerto = null;
        
        boolean SESION_MAIL_DEFINIDO = false;
        
         try{
           String MAIL_SESSION = configMail.getString("Mail.mail_session");
           log.info("Se ha producido un error enviando el email a la UOR " + MAIL_SESSION);
           if(MAIL_SESSION!=null && !"".equals(MAIL_SESSION)){
               InitialContext ic = new InitialContext();
               session = (Session)ic.lookup(MAIL_SESSION);
               //session.getProperties().setProperty("mail.transport.protocol", "smtp");
               session.setDebug(true);                
               SESION_MAIL_DEFINIDO = true;
           }
        }catch(Exception e){
            e.printStackTrace();
            log.error(" =================================>  ERROR AL RECUPERAR EL SERVICIO DE NOMENCLATURA PARA ESTABLECER SESIÓN CON EL SERVIDOR SMTP::  " + e.getMessage());
            SESION_MAIL_DEFINIDO = false;
        }
        

        try {
           
            if (from == null) from = configMail.getString("mail.from");
            

              if(!SESION_MAIL_DEFINIDO){
                    String USAR_AUTENTICACION = configMail.getString("mail.smtp.auth");

                    log.info(" ==============================> NO ESTÁ CONFIGURADA UNA SESIÓN DE MAIL EN EL SERVIDOR, POR TANTO SE RECUPERAN LOS DATOS DE techserver");
                    // Si no está definido un servicio de nomenclatura para el envio de correos electrónicos, se recuperan los
                    // datos de conexión al servidor de las propiedades de configuración del techserver correspondientes.                
                    //Inicializamos los datos de la sesion
                    Properties properties = new Properties();
                    // Si no se ha indicado remitente se coge el del fichero de config                
                    puerto = configMail.getString("mail.smtp.port");
                    properties.put("mail.transport.protocol", "smtp");
                    //properties.put("mail.mime.host", m_ConfigTechnical.getString("mail.mime.host"));
                    properties.put("mail.smtp.host", configMail.getString("mail.smtp.host"));            
                    if (!"".equals(puerto)&&puerto!=null) properties.put("mail.smtp.port", Integer.parseInt(configMail.getString("mail.smtp.port")));            
                    properties.put("mail.smtp.auth", configMail.getString("mail.smtp.auth"));

                    log.info("From:\t" + from);
                    log.info("Host:\t" + configMail.getString("mail.smtp.host"));
                    if (!"".equals(puerto)&&puerto!=null) log.info("Port:\t" + Integer.parseInt(configMail.getString("mail.smtp.port")));
                    log.info("Auth:\t" + configMail.getString("mail.smtp.auth"));
                    log.info("User:\t" + configMail.getString("mail.user"));
                    log.info("Pass:\t" + configMail.getString("mail.password"));


                    if(USAR_AUTENTICACION!=null && USAR_AUTENTICACION.equalsIgnoreCase("true")){
                        Authenticator authenticator = new Authenticator() {
                            protected PasswordAuthentication getPasswordAuthentication() {
                                return new PasswordAuthentication(configMail.getString("mail.user"), configMail.getString("mail.password"));
                            }
                        };
                        session = Session.getDefaultInstance(properties, authenticator);
                    }
                    else
                        session = Session.getDefaultInstance(properties);

                    //session = Session.getDefaultInstance(properties, authenticator);    
                    session.setDebug(true);
           
              }
            /** ORIGINAL
            Session session = Session.getDefaultInstance(properties, authenticator);
            session.setDebug(true);
            ***/
            Message mensaje = new MimeMessage(session);
            mensaje.setSubject(subject);
            BodyPart textoMensajeBody = new MimeBodyPart();
            textoMensajeBody.setText(textoMensaje);
            Multipart contenedor = new MimeMultipart();
            contenedor.addBodyPart(textoMensajeBody);

            mensaje.setContent(contenedor);
            mensaje.setSentDate(new Date());
            mensaje.setFrom(new InternetAddress(from));            
            transporte = session.getTransport();

            log.info("email dstino "+email);
            String addressTo = (String) email;
            Address addr = new InternetAddress(addressTo);
            mensaje.addRecipient(Message.RecipientType.TO,addr);

            mailCC=getRecipient("mail.cc");
            mailBCC=getRecipient("mail.bcc");

            StringTokenizer stCC = new StringTokenizer(mailCC);
            StringTokenizer stBCC = new StringTokenizer(mailBCC);
           
            while (stCC.hasMoreTokens()){
                String addressStr = (String) stCC.nextToken();
                Address address = new InternetAddress(addressStr);
                mensaje.addRecipient(Message.RecipientType.CC,address);
            }//while (stCC.hasMoreTokens())
           
            while (stBCC.hasMoreTokens()){
                String addressStr = (String) stBCC.nextToken();
                Address address = new InternetAddress(addressStr);
                mensaje.addRecipient(Message.RecipientType.BCC,address);
            }//while (stBCC.hasMoreTokens())
          
            log.info("MailHelper.sendMail antes de hacer transporte.connect");
            if (!"".equals(puerto)&&puerto!=null)
            transporte.connect(configMail.getString("mail.smtp.host"), Integer.parseInt(puerto), configMail.getString("mail.user"), configMail.getString("mail.password"));
            else transporte.connect();
            
            transporte.sendMessage(mensaje,mensaje.getAllRecipients());
        } catch (Exception e) {      
            //e.printStackTrace(); 
            log.error(e.getMessage());
            log.error(e.toString());
            e.printStackTrace();
            throw new MailException("Excepción: Error al enviar el mail", e);
        }//try-catch
        finally{
            if (transporte!=null)
                try {
                    transporte.close();
                }catch (MessagingException ex )
                {
                    log.error(ex.getMessage());
                }
        }
    }//sendMail

    

    protected static ResourceBundle configMail;
    protected static Logger log = LogManager.getLogger(MailHelper06.class);
}//class

    
package es.altia.flexia.notificacion.quartz.lanbide.util;

import es.altia.common.exception.CriticalException;
import es.altia.common.service.config.Config;
import es.altia.common.service.config.ConfigServiceHelper;
import es.altia.common.service.mail.exception.MailException;
import es.altia.common.service.mail.exception.MailServiceNotActivedException;
import java.util.Date;
import java.util.Properties;
import java.util.StringTokenizer;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import org.apache.log4j.Logger;

public class EnvioNotificacion {
    private static Logger m_Log = Logger.getLogger(es.altia.flexia.notificacion.quartz.lanbide.util.EnvioNotificacion.class);

    protected static Config m_Conf;

    protected static Config m_ConfigTechnical;

    protected static Config m_ConfigError;

    public EnvioNotificacion() {
        m_ConfigTechnical = ConfigServiceHelper.getConfig("techserver");
        m_ConfigError = ConfigServiceHelper.getConfig("error");
    }

    public void sendMail(String email, String subject, String textoMensaje, String from) throws MailException, MailServiceNotActivedException {
        if (m_Log.isDebugEnabled())
            m_Log.debug("sendMail() : BEGIN");
        String mailCC = "";
        String mailBCC = "";
        try {
            String servicioActivo = m_ConfigTechnical.getString("mail.service.active");
            if (!servicioActivo.equalsIgnoreCase("si"))
                throw new MailServiceNotActivedException("Servicio de mail no activado");
        } catch (CriticalException e) {
            throw new MailException(m_ConfigError.getString("Error.Mail.Envio"), e);
        }
        try {
            mailCC = m_ConfigTechnical.getString("mail.cc");
            mailBCC = m_ConfigTechnical.getString("mail.bcc");
        } catch (CriticalException e) {
            mailCC = "";
            mailBCC = "";
        }
        Session session = null;
        String puerto = null;
        Transport transporte = null;
        boolean SESION_MAIL_DEFINIDO = false;
        try {
            String MAIL_SESSION = m_ConfigTechnical.getString("Mail.mail_session");
            m_Log.debug(" ==============================> ESTA CONFIGURADA UNA SESION DE MAIL EN EL SERVIDOR CUYO NOMBRE ES: " + MAIL_SESSION);
            if (MAIL_SESSION != null && !"".equals(MAIL_SESSION)) {
                InitialContext ic = new InitialContext();
                session = (Session)ic.lookup(MAIL_SESSION);
                session.getProperties().setProperty("mail.transport.protocol", "smtp");
                session.setDebug(true);
                SESION_MAIL_DEFINIDO = true;
            }
        } catch (NamingException e) {
            e.printStackTrace();
            m_Log.error(" =================================>  ERROR AL RECUPERAR EL SERVICIO DE NOMENCLATURA PARA ESTABLECER SESICON EL SERVIDOR SMTP::  " + e.getMessage());
            SESION_MAIL_DEFINIDO = false;
        }
        try {
            InternetAddress[] direccion = new InternetAddress[1];
            direccion[0] = new InternetAddress(email);
            if (from == null)
                from = m_ConfigTechnical.getString("mail.from");
            if (!SESION_MAIL_DEFINIDO) {
                m_Log.debug(" ==============================> NO ESTCONFIGURADA UNA SESIDE MAIL EN EL SERVIDOR, POR TANTO SE RECUPERAN LOS DATOS DE techserver");
                Properties properties = new Properties();
                puerto = m_ConfigTechnical.getString("mail.smtp.port");
                properties.put("mail.transport.protocol", "smtp");
                properties.put("mail.smtp.host", m_ConfigTechnical.getString("mail.smtp.host"));
                if (!"".equals(puerto) && puerto != null)
                    properties.put("mail.smtp.port", Integer.valueOf(Integer.parseInt(m_ConfigTechnical.getString("mail.smtp.port"))));
                properties.put("mail.smtp.auth", m_ConfigTechnical.getString("mail.smtp.auth"));
                m_Log.debug("From:\t" + from);
                m_Log.debug("Host:\t" + m_ConfigTechnical.getString("mail.smtp.host"));
                if (!"".equals(puerto) && puerto != null)
                    m_Log.debug("Port:\t" + Integer.parseInt(m_ConfigTechnical.getString("mail.smtp.port")));
                m_Log.debug("Auth:\t" + m_ConfigTechnical.getString("mail.smtp.auth"));
                m_Log.debug("User:\t" + m_ConfigTechnical.getString("mail.user"));
                m_Log.debug("Pass:\t" + m_ConfigTechnical.getString("mail.password"));
                Authenticator authenticator = new Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(m_ConfigTechnical.getString("mail.user"), m_ConfigTechnical.getString("mail.password"));
                    }
                };
                session = Session.getDefaultInstance(properties, authenticator);
                session.setDebug(true);
            } else {
                m_Log.debug(" ==============================> LA CONFIGURACIDE ENVIO DE CORREO SE OBTIENE DE LA SESIDEL CORREO DEL SERVIDOR");
            }
            MimeMessage mimeMessage = new MimeMessage(session);
            mimeMessage.setSubject(subject);
            MimeBodyPart mimeBodyPart = new MimeBodyPart();
            mimeBodyPart.setText(textoMensaje);
            MimeMultipart mimeMultipart = new MimeMultipart();
            mimeMultipart.addBodyPart((BodyPart)mimeBodyPart);
            mimeMessage.setContent((Multipart)mimeMultipart);
            mimeMessage.setSentDate(new Date());
            mimeMessage.setFrom((Address)new InternetAddress(from));
            transporte = session.getTransport();
            String addressTo = email;
            InternetAddress internetAddress = new InternetAddress(addressTo);
            mimeMessage.addRecipient(Message.RecipientType.TO, (Address)internetAddress);
            mailCC = getRecipient("mail.cc");
            mailBCC = getRecipient("mail.bcc");
            StringTokenizer stCC = new StringTokenizer(mailCC);
            StringTokenizer stBCC = new StringTokenizer(mailBCC);
            while (stCC.hasMoreTokens()) {
                String addressStr = stCC.nextToken();
                InternetAddress internetAddress1 = new InternetAddress(addressStr);
                mimeMessage.addRecipient(Message.RecipientType.CC, (Address)internetAddress1);
            }
            while (stBCC.hasMoreTokens()) {
                String addressStr = stBCC.nextToken();
                InternetAddress internetAddress1 = new InternetAddress(addressStr);
                mimeMessage.addRecipient(Message.RecipientType.BCC, (Address)internetAddress1);
            }
            if ("".equals(puerto) || puerto == null) {
                transporte.connect(m_ConfigTechnical.getString("mail.smtp.host"), m_ConfigTechnical.getString("mail.user"), m_ConfigTechnical.getString("mail.password"));
            } else {
                transporte.connect(m_ConfigTechnical.getString("mail.smtp.host"), Integer.parseInt(puerto), m_ConfigTechnical.getString("mail.user"), m_ConfigTechnical.getString("mail.password"));
            }
            transporte.sendMessage((Message)mimeMessage, mimeMessage.getAllRecipients());
        } catch (Exception e) {
            m_Log.error(e.getMessage());
            m_Log.error(e.toString());
            throw new MailException(m_Conf.getString("Error.Mail.Envio"), e);
        }
        if (m_Log.isDebugEnabled())
            m_Log.debug("sendMail() : BEGIN");
    }

    private String getRecipient(String tipo) throws CriticalException {
        if (m_Log.isDebugEnabled())
            m_Log.debug("getRecipient() : BEGIN");
        String mail = "";
        try {
            mail = m_ConfigTechnical.getString(tipo);
            if (m_Log.isDebugEnabled())
                m_Log.debug("getRecipient() : END");
            return mail;
        } catch (CriticalException e) {
            mail = "";
            if (m_Log.isDebugEnabled())
                m_Log.debug("getRecipient() : END");
            return mail;
        }
    }
}


/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package es.altia.flexia.integracion.moduloexterno.melanbide34.job;

import es.altia.agora.technical.ConstantesDatos;
import es.altia.common.exception.TechnicalException;
import es.altia.common.service.config.Config;
import es.altia.common.service.config.ConfigServiceHelper;
import es.altia.common.service.mail.MailHelper;
import es.altia.flexia.integracion.moduloexterno.melanbide34.dao.MeLanbide34DAO;
import es.altia.flexia.integracion.moduloexterno.melanbide34.i18n.MeLanbide34I18n;
import es.altia.flexia.integracion.moduloexterno.melanbide34.util.ConfigurationParameter;
import es.altia.flexia.integracion.moduloexterno.melanbide34.util.ConstantesMeLanbide34;
import es.altia.flexia.integracion.moduloexterno.melanbide34.vo.DatosAviso;
import es.altia.technical.PortableContext;
import es.altia.util.conexion.AdaptadorSQLBD;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import javax.sql.DataSource;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.Trigger;

public class EnvioEmailsJob implements Job {
    
   @Override
   public void execute(JobExecutionContext jec) throws JobExecutionException 
   {
       try
       {
            int pepe = jec.getRefireCount();
            Job trab = jec.getJobInstance();

            log.error("jec.getRefireCount(): " + pepe);
            Trigger pepito = jec.getTrigger();
            
            String servidor = ConfigurationParameter.getParameter(ConstantesMeLanbide34.CAMPO_SERVIDOR, 
                        ConstantesMeLanbide34.FICHERO_PROPIEDADES);
            //String servidor = "flexia1"; //DESA
            //String servidor = "paprergi1_flexia1"; //PRE
            //String servidor = "pargi1_flexia1"; //PRO

            if(servidor.equals(System.getProperty("weblogic.Name")))            {//PARA LOCAL QUITAR
            synchronized(jec){
                 Connection con = null;
                 
                 boolean dev = false;
                 int numIntentos = 0;
                 String numExpediente = "";
                 int id = 0;
                 String[] params = new String []{"ORACLE"};
                 try
                 {
                     log.error("Execute lanzado " + System.getProperty("weblogic.Name"));
                     //int codOrg = 0;      //pruebas
                     //int codOrg = 1;      //real
                     
                    int codOrg = Integer.parseInt(ConfigurationParameter.getParameter(ConstantesMeLanbide34.COD_ORG,ConstantesMeLanbide34.FICHERO_PROPIEDADES));
                    //int codOrg = 0;      //pruebas
                    //int codOrg = 1;      //real
                    boolean dosEntornos = Boolean.getBoolean(ConfigurationParameter.getParameter(ConstantesMeLanbide34.DOS_ENTORNOS, 
                    ConstantesMeLanbide34.FICHERO_PROPIEDADES));
                    
                    boolean docNotif = true;
                    while(codOrg < 2){

                        con = this.getAdaptSQLBD(String.valueOf(codOrg)).getConnection();
                        log.error("en el while de tokens codOrg: " + codOrg);

                        //comprobar expedientes CEI con trámite RECEPCIÓN DOCUMENTACIÓN PARA 2ºPAGO abierto (31/12/Año solicitud)
                        MeLanbide34DAO meLanbide34DAO = MeLanbide34DAO.getInstance();
                        String email = meLanbide34DAO.getEmailUO(codOrg, ConfigurationParameter.getParameter(ConstantesMeLanbide34.UNIDAD_ORG,ConstantesMeLanbide34.FICHERO_PROPIEDADES), con);
                        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");


                        Long codTramDocu2pago = meLanbide34DAO.getCodigoInternoTramite(codOrg, ConstantesMeLanbide34.CODIGO_TRAM_RECDOCUSEGUNPAGO, con);                     
                        List<DatosAviso> listaExpPendienteDocu2 = meLanbide34DAO.getExpDocuTramiteAbierto(codOrg, numExpediente, codTramDocu2pago.toString(),1L, con);
                        String nomTramite = meLanbide34DAO.getNombreTramite(codOrg,codTramDocu2pago,con);
                        log.debug("Expedientes con documentación pendiente en 2ºpago "+listaExpPendienteDocu2.size());
                        Date hoy =new Date();
                        for (DatosAviso exp : listaExpPendienteDocu2) {                            
                           log.debug("Expediente: "+exp.getNumExp()+ " fecha solicitud: "+exp.getFecSolicitud());
                           
                           //fecha máxima 31/12/año sol+1
                           Date fecMaximo =exp.getFecSolicitud();
                           Calendar cal = new GregorianCalendar();
                           cal.setTimeInMillis(fecMaximo.getTime());                           
                           cal.set(cal.get(Calendar.YEAR), 11,31);                           
                           fecMaximo = new Date(cal.getTimeInMillis());                           
                           log.debug("Fecha maxima: "+fecMaximo);                          
                           if (fecMaximo.before(hoy)) {  
                               log.debug("SE ENVÍA EMAIL");
                            //ENVIO DE CORREO A TRAMITADOR
                            sendMail(nomTramite, email, exp.getNumExp(),df.format(fecMaximo));
                            }                            
                        }                                         

                        //comprobar expedientes CEI con trámite RECEPCIÓN DOCUMENTACIÓN PARA 3ºPAGO abierto (Fecha de entrega del estudio de mercado+ 1 año)
                        Long codTramDocu3pago = meLanbide34DAO.getCodigoInternoTramite(codOrg, ConstantesMeLanbide34.CODIGO_TRAM_RECDOCUTERCERPAGO, con);
                        List<DatosAviso> listaExpPendienteDocu3 = meLanbide34DAO.getExpDocuTramiteAbierto(codOrg, numExpediente, codTramDocu3pago.toString(),1L, con);        
                        
                        nomTramite = meLanbide34DAO.getNombreTramite(codOrg,codTramDocu3pago,con);
                        
                        log.debug("Expedientes con documentación pendiente en 3ºpago "+listaExpPendienteDocu3.size());
                        for (DatosAviso exp : listaExpPendienteDocu3) {                            
                           log.debug("Expediente "+exp.getNumExp()+ " fecha estudio:"+exp.getFecEstudio());
                           
                           //fecha máxima 31/12/año sol+1
                           if (exp.getFecEstudio()!=null){
                                Date fecMaximo =exp.getFecEstudio();
                                Calendar cal = new GregorianCalendar();
                                cal.setTimeInMillis(fecMaximo.getTime());
                                cal.add(Calendar.YEAR, 1);
                                fecMaximo = new Date(cal.getTimeInMillis());
                                log.debug("Fecha maxima: "+fecMaximo);
                                if (fecMaximo.before(hoy)) {   
                                     log.debug("SE ENVÍA EMAIL");
                                 //ENVIO DE CORREO A TRAMITADOR
                                 sendMail(nomTramite, email, exp.getNumExp(),df.format(fecMaximo) );
                                } 
                           }
                        }
                                           
                        if(dosEntornos) codOrg++;
                        else codOrg = 2;
                 
                    }
                 }
                 
                 catch(Exception e)
                 {
                     log.error("Error en el job de notificacion: " + e);

                      try{
                          int intentos = numIntentos + 1;
                          //meLanbide34DAO.getInstance().actualizarError(id, intentos, e.toString(), con);
                      }catch(Exception i){
                          log.error("Error en la funci�n actualizarError: " + i.getMessage());

                      }

                     try {
                         throw e;
                     } catch (Exception ex) {
                         java.util.logging.Logger.getLogger(EnvioEmailsJob.class.getName()).log(Level.SEVERE, null, ex);
                     }

                 }
                 finally{
                     if(con != null)
                         try {
                             con.close();
                     } catch (SQLException ex) {
                         java.util.logging.Logger.getLogger(EnvioEmailsJob.class.getName()).log(Level.SEVERE, null, ex);
                     }
                 }
            }//para local quitar
            }
       }
       catch(Exception ex)
       {
           log.error("Error: " +ex);
       }
   }

    protected static Config conf =ConfigServiceHelper.getConfig("notificaciones");    
    
    private Logger log = LogManager.getLogger(EnvioEmailsJob.class);

    private String codOrganizacion;
   

    public String getCodOrganizacion() {
        return codOrganizacion;
    }

    public void setCodOrganizacion(String codOrganizacion) {
        this.codOrganizacion = codOrganizacion;
    }

   /*
    private String[] getTituloExtension(String fichero, String contentType, boolean esDocumentoTramite){
        log.error("Titulo " + fichero + " , tipo " + contentType);
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
        s = s.replaceAll("�", "a");
        s = s.replaceAll("�", "e");
        s = s.replaceAll("�", "i");
        s = s.replaceAll("�", "o");
        s = s.replaceAll("�", "u");
        s = s.replaceAll("�", "A");
        s = s.replaceAll("�", "E");
        s = s.replaceAll("�", "I");
        s = s.replaceAll("�", "O");
        s = s.replaceAll("�", "U");
        
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
    */
     private void sendMail(String nombreTramite, String emailUtr,String numExpediente,String fecha) {

        log.debug(">>>>>>>destinatario del mail: " + emailUtr);
        if (emailUtr != null) {            
            // Se recupera el asunto y mensaje del correo
            
            int idiomaUsuario=1;
            MeLanbide34I18n meLanbide34I18n = MeLanbide34I18n.getInstance();
            String asunto =ConfigurationParameter.getParameter(ConstantesMeLanbide34.ENTORNO,ConstantesMeLanbide34.FICHERO_PROPIEDADES)+" - "+meLanbide34I18n.getMensaje(idiomaUsuario, "email.asunto").replaceAll("@expediente@", numExpediente);
            String mensaje =meLanbide34I18n.getMensaje(idiomaUsuario, "email.contenido").replaceAll("@nombre@",nombreTramite ).replaceAll("@fechaMaxima@", fecha);
            
            
            log.debug("Mensaje de notificación: " + mensaje);
            log.debug("Asunto del mail: " + asunto);

            MailHelper mailer = new MailHelper();
            try {
                mailer.sendMail(emailUtr, asunto, mensaje);
            } catch (Exception e) {
                // Si ocurre algún error durante el envío del correo
                e.printStackTrace();
                log.error("Error al enviar e-mail (CEI) con notificación de fin de plazo " + nombreTramite + ": " + e.getMessage());
            }
        } 
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
            log.error("getJndi =========> ");
            log.error("parametro codOrganizacion: " + codOrganizacion);
            log.error("gestor: " + gestor);
            log.error("jndi: " + jndiGenerico);
        }//if(log.isDebugEnabled())

        DataSource ds = null;
        AdaptadorSQLBD adapt = null;
        synchronized (this) {
            try{
                PortableContext pc = PortableContext.getInstance();
                if(log.isDebugEnabled())log.error("He cogido el jndi: " + jndiGenerico);
                ds = (DataSource)pc.lookup(jndiGenerico, DataSource.class);
                // Conexi�n al esquema gen�rico
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

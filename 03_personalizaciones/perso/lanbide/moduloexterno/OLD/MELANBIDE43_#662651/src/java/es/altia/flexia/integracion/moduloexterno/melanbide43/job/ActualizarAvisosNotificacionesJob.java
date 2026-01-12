/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package es.altia.flexia.integracion.moduloexterno.melanbide43.job;

import es.altia.agora.technical.ConstantesDatos;
import es.altia.common.exception.TechnicalException;
import es.altia.common.service.config.Config;
import es.altia.common.service.config.ConfigServiceHelper;
import es.altia.flexia.integracion.moduloexterno.melanbide43.dao.MeLanbide43DAO;
import es.altia.flexia.integracion.moduloexterno.melanbide43.util.ConfigurationParameter;
import es.altia.flexia.integracion.moduloexterno.melanbide43.util.ConstantesMeLanbide43;
import es.altia.flexia.integracion.moduloexterno.melanbide43.vo.ExpAvisos;
import es.altia.technical.PortableContext;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.lanbide.lan6.adaptadoresPlatea.excepciones.Lan6Excepcion;
import es.lanbide.lan6.adaptadoresPlatea.informarConsulta.beans.Lan6Expediente;
import es.lanbide.lan6.adaptadoresPlatea.informarConsulta.beans.Lan6Participacion;
import es.lanbide.lan6.adaptadoresPlatea.informarConsulta.servicios.Lan6InformarConsultaServicios;
import es.lanbide.lan6.adaptadoresPlatea.utilidades.config.Lan6Config;
import es.lanbide.lan6.adaptadoresPlatea.utilidades.constantes.Lan6Constantes;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;
import javax.sql.DataSource;
import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.Trigger;

public class ActualizarAvisosNotificacionesJob implements Job {
    
   @Override
   public void execute(JobExecutionContext jec) throws JobExecutionException 
   {
       try
       {
            int pepe = jec.getRefireCount();

            log.error("jec.getRefireCount(): " + pepe);
            
            String servidor = ConfigurationParameter.getParameter(ConstantesMeLanbide43.CAMPO_SERVIDOR, ConstantesMeLanbide43.FICHERO_PROPIEDADES);
            
            //String servidor = "flexia1"; //DESA
            //String servidor = "paprergi1_flexia1"; //PRE
            //String servidor = "pargi1_flexia1"; //PRO
            log.error("servidor: " + servidor);
            log.error("weblogic.Name: " + System.getProperty("weblogic.Name"));
            if(servidor.equals(System.getProperty("weblogic.Name")))            {//PARA LOCAL QUITAR
            synchronized(jec){
                 Connection con = null;
                 
                try
                 {
                     log.error("Execute lanzado " + System.getProperty("weblogic.Name"));
                     
                    int codOrg = Integer.parseInt(ConfigurationParameter.getParameter("COD_ORG",ConstantesMeLanbide43.FICHERO_PROPIEDADES));
                    
                    boolean dosEntornos = Boolean.getBoolean(ConfigurationParameter.getParameter("DOS_ENTORNOS", 
                    ConstantesMeLanbide43.FICHERO_PROPIEDADES));
                  
                    while(codOrg < 2){
                        AdaptadorSQLBD adaptador= this.getAdaptSQLBD(String.valueOf(codOrg));
                        con = adaptador.getConnection();
                        log.error("en el while de tokens codOrg: " + codOrg);

                        log.error("JOB LANZADO ->ActualizarAvisosNotificacionesJob");
                        MeLanbide43DAO meLanbide43DAO = MeLanbide43DAO.getInstance();
                        String numExpediente ="";
                        List<ExpAvisos> expedientesAvisos = meLanbide43DAO.getExpedientesActualizarAvisos(con);
                        try {
                            log.error ("ActualizarAvisosNotificacionesJob - N expedientesAvisos -> " + expedientesAvisos.size());
                            
                            
                            for(Iterator<ExpAvisos> i = expedientesAvisos.iterator(); i.hasNext(); ) {
                                ExpAvisos item = i.next();
                                String idProcedimiento="";
                                if ("SEI".equals(item.getProcedimiento())) {
                                    idProcedimiento = Lan6Config.getProperty(Lan6Constantes.FICHERO_PROP_ADAPTADORES_PLATEA, "PROCEDIMIENTO_ID_LAN68_SEI");
                                } /*else if ("ORI14".equals(item.getProcedimiento()) || "ORI".equals(item.getProcedimiento())) {
                                    idProcedimiento = Lan6Config.getProperty(Lan6Constantes.FICHERO_PROP_ADAPTADORES_PLATEA, "PROCEDIMIENTO_ID_"+"LAN62_ORI14");
                                } else if ("COLEC".equals(item.getProcedimiento())) {
                                    idProcedimiento = Lan6Config.getProperty(Lan6Constantes.FICHERO_PROP_ADAPTADORES_PLATEA, "PROCEDIMIENTO_ID_"+"LAN62_COLEC");
                                }else if ("CEMP".equals(item.getProcedimiento())) {
                                    idProcedimiento = Lan6Config.getProperty(Lan6Constantes.FICHERO_PROP_ADAPTADORES_PLATEA, "PROCEDIMIENTO_ID_"+"LAN62_CEMP");
                                }*/                        
                                 
                                                      
                                log.error ("ActualizarAvisosNotificacionesJob - expediente: " + item.getNumExpediente() + ", tipo_notificacion: " + item.getTipoNotificacion() + ", email: " + item.getEmail());
                                Lan6InformarConsultaServicios servicios = new Lan6InformarConsultaServicios(idProcedimiento);
                                
                                // Expediente
                                numExpediente = item.getNumExpediente();

                                Lan6Expediente lan6Expediente = new Lan6Expediente();
                                lan6Expediente.setNumero(item.getNumExpediente());
                                lan6Expediente.setEjercicio(item.getEjercicio());                                                                  

                                // Participacion
                                Lan6Participacion lan6participacion = new Lan6Participacion();   			

                                if ("ELECTRONICA".equals(item.getTipoNotificacion())) {
                                    lan6participacion.setCanalNotificacion(Lan6Constantes.NOTIF_ELECTRONICA);
                                } else {
                                    lan6participacion.setCanalNotificacion(Lan6Constantes.NOTIF_POSTAL);
                                }

                                List<String> mailsAvisos = new ArrayList<String>();
                                mailsAvisos.add(item.getEmail());
                                lan6participacion.setMailsAvisos(mailsAvisos);


                                lan6participacion.setIdParticipacion(Lan6Constantes.PARTICIPACION_UNICA);
                                lan6Expediente.setParticipacion(lan6participacion);   

                                // Llamada método
                                servicios.actualizarAvisosNotificaciones(lan6Expediente);
                                 
                            }
                            

                        } catch (Lan6Excepcion e) {
                            log.error ("Error al ActualizarAvisosNotificacionesJob: expediente: " + numExpediente, e);
                        }                        
                        
                        
                        
                        if(dosEntornos) codOrg++;
                        else codOrg = 2;  
                        if(con != null){                           
                           con.close();                           
                        }
                        
                    }
                 }
                 catch(Exception e)
                 {
                     log.error("Error en el job de ActualizarAvisosNotificacionesJob: ", e);  

                 }
                 finally{
                     if(con != null){
                          con.close();                        
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
    
    private Logger log = Logger.getLogger(ActualizarAvisosNotificacionesJob.class);

    private String codOrganizacion;
   

    public String getCodOrganizacion() {
        return codOrganizacion;
    }

    public void setCodOrganizacion(String codOrganizacion) {
        this.codOrganizacion = codOrganizacion;
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

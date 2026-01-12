/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package es.altia.flexia.integracion.moduloexterno.melanbide43.job;

import com.lanbide.lan6.errores.bean.ErrorBean;
import es.altia.agora.technical.ConstantesDatos;
import es.altia.common.exception.TechnicalException;
import es.altia.common.service.config.Config;
import es.altia.common.service.config.ConfigServiceHelper;
import es.altia.flexia.integracion.moduloexterno.melanbide43.dao.MeLanbide43DAO;
import es.altia.flexia.integracion.moduloexterno.melanbide43.manager.MeLanbide43Manager;

import es.altia.flexia.integracion.moduloexterno.melanbide43.util.ConfigurationParameter;
import es.altia.flexia.integracion.moduloexterno.melanbide43.util.ConstantesMeLanbide43;
import es.altia.flexia.integracion.moduloexterno.melanbide43.vo.ExpAvisos;
import es.altia.technical.PortableContext;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.lanbide.lan6.adaptadoresPlatea.excepciones.Lan6Excepcion;
import es.lanbide.lan6.adaptadoresPlatea.excepciones.Lan6InformarConsultaExcepcion;
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

public class AperturaEsperaNuevoPersonalJob implements Job {
    
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

                        log.error("JOB LANZADO ->AperturaEsperaNuevoPersonalJob");
                        MeLanbide43DAO meLanbide43DAO = MeLanbide43DAO.getInstance();
                        List<String> expedientesApertura = meLanbide43DAO.getExpedientesAperturaEsperaNuevoPersonal(con);
                        try {
                            log.error ("AperturaEsperaNuevoPersonalJob - N expedientesApertura -> " + expedientesApertura.size());
                            String mensaje ="";
                            Integer codTramite=0;
                            Integer ocuTramite=1;
                            String proc="";
                            
                            
                            for(Iterator<String> i = expedientesApertura.iterator(); i.hasNext(); ) {
                                String item = i.next();                                
                                
                                String []  partes = item.split("/");
                                proc = partes[1];
                                //Inicio generarMisGestionesAvance
                                try{ 
                                    if(log.isDebugEnabled()) log.error("AperturaEsperaNuevoPersonalJob:generarMisGestionesAvance() : BEGIN -> "+item);
                                    if ("ORI14".equals(proc) || "ORI".equals(proc)){
                                        codTramite = Integer.parseInt(ConfigurationParameter.getParameter(ConstantesMeLanbide43.TRAM_ORI14_ENV_ORDEN_PRIMER_PAGO, ConstantesMeLanbide43.FICHERO_PROPIEDADES));
                                        
                                    } else if ("COLEC".equals(proc)){
                                        codTramite = Integer.parseInt(ConfigurationParameter.getParameter(ConstantesMeLanbide43.TRAM_COLEC_ENV_ORDEN_PRIMER_PAGO, ConstantesMeLanbide43.FICHERO_PROPIEDADES));
                                        
                                    } else if ("CEMP".equals(proc)){
                                        codTramite = Integer.parseInt(ConfigurationParameter.getParameter(ConstantesMeLanbide43.TRAM_CEMP_ENV_ORDEN_PRIMER_PAGO, ConstantesMeLanbide43.FICHERO_PROPIEDADES));
                                        
                                    }
                                    mensaje = MeLanbide43Manager.getInstance().generarMisGestiones(codOrg, codTramite, ocuTramite, item, "A",getAdaptSQLBD(String.valueOf(codOrg)));
                                    if (!"0".equals(mensaje)){
                                        log.error("AperturaEsperaNuevoPersonalJob: Error al generarMisGestionesAvance "+item+ " - mensaje: "+ mensaje);
                                    } else {
                                        log.error("AperturaEsperaNuevoPersonalJob:generarMisGestionesAvance: OK: "+item );
                                    }
                                    if(log.isDebugEnabled()) log.error("AperturaEsperaNuevoPersonalJob:generarMisGestionesAvance() : END -> "+item);

                                } catch(Exception x){
                                    log.error("AperturaEsperaNuevoPersonalJob: Error al generarMisGestionesAvance " + item + ": " + x);                                    
                                }
                                
                                // Fin generarMisGestionesAvance
                                
                                //Inicio JustificantesPagos
                                try{
                                    log.error("AperturaEsperaNuevoPersonalJob:justificantesPagos: BEGIN " + item);
                                    if ("ORI14".equals(proc) || "ORI".equals(proc)){
                                        codTramite = Integer.parseInt(ConfigurationParameter.getParameter(ConstantesMeLanbide43.TRAM_ORI14_ESP_APER_DOC_SOL_ALTA, ConstantesMeLanbide43.FICHERO_PROPIEDADES));
                                    } else if ("COLEC".equals(proc)){
                                        codTramite = Integer.parseInt(ConfigurationParameter.getParameter(ConstantesMeLanbide43.TRAM_COLEC_ESP_APER_DOC_SOL_ALTA, ConstantesMeLanbide43.FICHERO_PROPIEDADES));
                                    } else if ("CEMP".equals(proc)){
                                        codTramite = Integer.parseInt(ConfigurationParameter.getParameter(ConstantesMeLanbide43.TRAM_CEMP_ESP_APER_DOC_SOL_ALTA, ConstantesMeLanbide43.FICHERO_PROPIEDADES));
                                    }
                                    mensaje = MeLanbide43Manager.getInstance().justificantesPagos (codOrg,codTramite, item, this.getAdaptSQLBD(String.valueOf(codOrg)));
                                    if (!"0".equals(mensaje)){
                                        log.error("AperturaEsperaNuevoPersonalJob: Error en justificantesPagos "+item+ " - mensaje: "+ mensaje);
                                    } else {
                                        log.error("AperturaEsperaNuevoPersonalJob:justificantesPagos: OK: " +item);
                                    }
                                    if(log.isDebugEnabled()) log.error("AperturaEsperaNuevoPersonalJob:justificantesPagos() : END -> "+item);
                                    
                                    
                                }catch(Exception ex)
                                {            
                                    log.error("AperturaEsperaNuevoPersonalJob: Error en justificantesPagos() " +item+ ": " + ex);
                                }
                                //Fin JustificantesPagos
                                
                                
                            }
                            

                        } catch (Exception e) {
                            log.error ("Error al AperturaEsperaNuevoPersonalJob: ", e);
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
                     log.error("Error en el job de AperturaEsperaNuevoPersonalJob: ", e);  

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
    
    private Logger log = Logger.getLogger(AperturaEsperaNuevoPersonalJob.class);

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

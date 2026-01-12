
package es.altia.flexia.integracion.moduloexterno.melanbide43.job;

import es.altia.agora.technical.ConstantesDatos;
import es.altia.common.exception.TechnicalException;
import es.altia.common.service.config.Config;
import es.altia.common.service.config.ConfigServiceHelper;
import es.altia.flexia.integracion.moduloexterno.melanbide43.dao.MeLanbide43DAO;
import es.altia.flexia.integracion.moduloexterno.melanbide43.manager.MeLanbide43Manager;
import es.altia.flexia.integracion.moduloexterno.melanbide43.util.ConfigurationParameter;
import es.altia.flexia.integracion.moduloexterno.melanbide43.util.ConstantesMeLanbide43;
import es.altia.flexia.integracion.moduloexterno.melanbide43.vo.ExpTram;
import es.altia.flexia.integracion.moduloexterno.melanbide43.vo.Participantes;
import es.altia.technical.PortableContext;
import es.altia.util.conexion.AdaptadorSQLBD;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import javax.sql.DataSource;
import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.Trigger;

public class AbrirListaExpedientesPlateaJob implements Job {
    
    //protected static Config conf =ConfigServiceHelper.getConfig("notificaciones");    
    
    private final Logger log = Logger.getLogger(AbrirListaExpedientesPlateaJob.class);

    /*private String codOrganizacion;
    public String getCodOrganizacion() {
        return codOrganizacion;
    }
    public void setCodOrganizacion(String codOrganizacion) {
        this.codOrganizacion = codOrganizacion;
    }*/
    
   @Override
   public void execute(JobExecutionContext jec) throws JobExecutionException 
   {
       try
       {
            int refCount = jec.getRefireCount();
            Job trab = jec.getJobInstance();

            log.info("jec.getRefireCount(): " + refCount);
            Trigger pepito = jec.getTrigger();
            
            String servidor = ConfigurationParameter.getParameter(ConstantesMeLanbide43.CAMPO_SERVIDOR, ConstantesMeLanbide43.FICHERO_PROPIEDADES);
            
            //String servidor = "flexia1"; //DESA
            //String servidor = "paprergi1_flexia1"; //PRE
            //String servidor = "pargi1_flexia1"; //PRO
            log.info("servidor: " + servidor);
            if(servidor.equals(System.getProperty("weblogic.Name")))            {//PARA LOCAL QUITAR
            synchronized(jec){
                 Connection con = null;
                 
                 //boolean dev = false;
                 int numIntentos = 0;
                 String numExpediente = "";
                 String procExpediente = "";
                 //int id = 0;
                 //String[] params = new String []{"ORACLE"};
                 try
                 {
                    log.info("Execute lanzado " + System.getProperty("weblogic.Name"));
              
                    int codOrg = Integer.parseInt(ConfigurationParameter.getParameter("COD_ORG",ConstantesMeLanbide43.FICHERO_PROPIEDADES));
       
                    boolean dosEntornos = Boolean.getBoolean(ConfigurationParameter.getParameter("DOS_ENTORNOS", 
                    ConstantesMeLanbide43.FICHERO_PROPIEDADES));
                  
                    while(codOrg < 2){
                        AdaptadorSQLBD adaptador= this.getAdaptSQLBD(String.valueOf(codOrg));
                        con = adaptador.getConnection();
                        log.info("en el while de tokens codOrg: " + codOrg);

                        MeLanbide43DAO meLanbide43DAO = MeLanbide43DAO.getInstance();
                   
                        //abrir los expedientes en MiCarpeta, de la lista de la tabla auxiliar EXP_ABRIR_MICARPETA  
                        List<String> expedientes = meLanbide43DAO.getExpedientesReabrirMicarpeta(con);
                        log.info("numexpedientes:"+expedientes.size());

                        for(Iterator<String> i = expedientes.iterator(); i.hasNext(); ) {
                            String item = i.next();
                            numExpediente = item;
                            log.info("numexpediente: "+numExpediente);
                            String[] datos = numExpediente.split("/");
                            procExpediente = datos[1];
                            log.info("procExpediente: "+procExpediente);
                            //LLAMAR A MIS GESTIONES INICIO                            
                            MeLanbide43Manager meLanbide43Manager = MeLanbide43Manager.getInstance();            
                            Participantes par = meLanbide43Manager.obtenerDatosParticipantes(numExpediente,adaptador);
                            log.info("PARTICIPANTES: "+par);  
                            log.info("tipo id: " + par.getTipoID());
                            if(par.getTipoID() != 0){
                                Long codTramiteInicio = meLanbide43DAO.obtenerCodigoTramiteFase (procExpediente, "1", con); 
                                log.info("codTramiteInicio: " + codTramiteInicio);    
                                String idGest = MeLanbide43Manager.getInstance().guardarGestiones(numExpediente, codOrg, adaptador, codTramiteInicio.intValue(), "I");
                                log.info("Id generado: " + idGest);

                                //iniciar expediente en MiCarpeta
                                String codigoOperacion = "";
                                codigoOperacion = MeLanbide43Manager.getInstance().avanzarGestiones(idGest, String.valueOf(codOrg),adaptador);       
                                
                                //eliminar expediente tratado en tabla EXP_ABRIR_MICARPETA
                                boolean resultDeleteExpTratado = meLanbide43DAO.eliminarExpTratadoAbrirMiCarpeta(numExpediente,con);
                                
                            }

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
                     log.error("Error en el job de AbrirExpedientesPlateaJob: ", e);
                  
                     try {
                         throw e;
                     } catch (Exception ex) {
                         java.util.logging.Logger.getLogger(AbrirListaExpedientesPlateaJob.class.getName()).log(Level.SEVERE, null, ex);
                     }

                 }
                 finally{
                     if(con != null){
                         try {
                                con.close();
                        } catch (SQLException ex) {
                            java.util.logging.Logger.getLogger(AbrirListaExpedientesPlateaJob.class.getName()).log(Level.SEVERE, null, ex);
                        }
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

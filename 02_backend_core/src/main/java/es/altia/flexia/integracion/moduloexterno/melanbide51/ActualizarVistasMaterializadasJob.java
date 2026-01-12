/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package es.altia.flexia.integracion.moduloexterno.melanbide51;

import es.altia.agora.technical.ConstantesDatos;
import es.altia.common.exception.TechnicalException;
import es.altia.common.service.config.Config;
import es.altia.common.service.config.ConfigServiceHelper;
import es.altia.flexia.integracion.moduloexterno.melanbide51.manager.MeLanbide51Manager;
import es.altia.flexia.integracion.moduloexterno.melanbide51.manager.util.ConfigurationParameter;
import es.altia.flexia.integracion.moduloexterno.melanbide51.manager.util.ConstantesMelanbide51;
import es.altia.technical.PortableContext;
import es.altia.util.conexion.AdaptadorSQLBD;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import javax.sql.DataSource;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class ActualizarVistasMaterializadasJob implements Job {
    
   @Override
   public void execute(JobExecutionContext jec) throws JobExecutionException 
   {
       
       Connection con = null;
       try
       {
            String servidor = ConfigurationParameter.getParameter(ConstantesMelanbide51.CAMPO_SERVIDOR, ConstantesMelanbide51.FICHERO_PROPIEDADES);
            //PARA PROBAR EN LOCAL COMENTAR EL IF DEL SERVIDOR
            if(servidor.equals(System.getProperty("weblogic.Name")))
            {
                synchronized(jec){
                    try
                    {
                        log.error("Execute ActualizarVistasMaterializadasJob lanzado " + System.getProperty("weblogic.Name"));
                        int codOrg = Integer.parseInt(ConfigurationParameter.getParameter(ConstantesMelanbide51.COD_ORG, ConstantesMelanbide51.FICHERO_PROPIEDADES));
                        String vistas = ConfigurationParameter.getParameter(ConstantesMelanbide51.VISTAS_MATERIALIZADAS_ACTUALIZAR, ConstantesMelanbide51.FICHERO_PROPIEDADES);
                        boolean dosEntornos = Boolean.getBoolean(ConfigurationParameter.getParameter(ConstantesMelanbide51.DOS_ENTORNOS, ConstantesMelanbide51.FICHERO_PROPIEDADES));
                        log.error("dosEntornos: " + dosEntornos);
                        while(codOrg < 2){
                            con = this.getAdaptSQLBD(String.valueOf(codOrg)).getConnection();
                            log.error("en el while de tokens codOrg: " + codOrg);
                            String resultado = MeLanbide51Manager.getInstance().actualizaVistasMaterializadas(vistas, con);
                            log.error("El resultado de la actualización de las vistas materializadas es: " + resultado);
                            if(dosEntornos)
                                codOrg++;
                            else
                                codOrg = 2;
                        }
                    }
                    catch(Exception e)
                    {
                        log.error("Error en ActualizarVistasMaterializadasJob: " + e);
                        throw e;
                    }
                }
            }
       }
        catch(JobExecutionException ex){
            log.error("Error en ActualizarVistasMaterializadasJob: " + ex);
            throw ex;
        }
        catch(Exception e)
        {
            log.error("Error en ActualizarVistasMaterializadasJob: " + e);
            //throw e;
        }
       finally{
           try
            {
                if(log.isDebugEnabled()) 
                    log.error("Procedemos a cerrar la conexion");
                
                if(con != null)
                    con.close();
            }
            catch(Exception e)
            {
                log.error("Se ha producido un error cerrando la conexion", e);
            }
       }
   }

    protected static Config conf = ConfigServiceHelper.getConfig("notificaciones");
    
    private Logger log = LogManager.getLogger(ActualizarVistasMaterializadasJob.class);

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
//        Connection con = null;
        
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

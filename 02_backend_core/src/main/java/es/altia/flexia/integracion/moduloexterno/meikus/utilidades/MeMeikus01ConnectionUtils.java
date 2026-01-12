package es.altia.flexia.integracion.moduloexterno.meikus.utilidades;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import java.util.ResourceBundle;
import java.sql.Statement;
import java.sql.ResultSet;
import javax.sql.DataSource;
import es.altia.technical.PortableContext;
import es.altia.agora.technical.ConstantesDatos;
import es.altia.util.conexion.AdaptadorSQLBD;

import es.altia.common.exception.TechnicalException;
import es.altia.util.conexion.BDException;

public class MeMeikus01ConnectionUtils {

    private static Logger log = LogManager.getLogger(MeMeikus01ConnectionUtils.class);

    public static AdaptadorSQLBD getAdapt(int codOrganizacion){
        if(log.isDebugEnabled()) log.debug("getAdapt() : BEGIN");
        ResourceBundle config = ResourceBundle.getBundle("techserver");
        String gestor = config.getString("CON.gestor");
        String jndiGenerico = config.getString("CON.jndi");
        Connection conGenerico = null;
        Statement st = null;
        ResultSet rs = null;
        String[] salida = null;
        AdaptadorSQLBD adapt = null;

        //log.debug(" getJndi =========> ");
        //log.debug("     parametro codOrganizacion: " + codOrganizacion);
        //log.debug("     gestor: " + gestor);
        //log.debug("     jndi: " + jndiGenerico);

        DataSource ds = null;
        //synchronized (this) {
           try{
                PortableContext pc = PortableContext.getInstance();
                //log.debug("He cogido el jndi: " + jndiGenerico);
                ds = (DataSource)pc.lookup(jndiGenerico, DataSource.class);
                // Conexión al esquema genérico
                conGenerico = ds.getConnection();

                String sql   = "SELECT EEA_BDE FROM A_EEA WHERE EEA_APL=" + ConstantesDatos.APP_GESTION_EXPEDIENTES + " AND AAE_ORG=" + codOrganizacion;
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
               //log.error("*** AdaptadorSQLBD: " + te.toString());
           }catch(SQLException e){
               e.printStackTrace();
           }finally{
               try{
                   if(st!=null) st.close();
                   if(rs!=null) rs.close();
                   if(conGenerico!=null && !conGenerico.isClosed()) conGenerico.close();

               }catch(SQLException e){
                   e.printStackTrace();
               }//try-catch-finally
           }//try-catch-finally
        //}// synchronized
        if(log.isDebugEnabled()) log.debug("getAdapt() : END");
        return adapt;
    }//getAdapt
}//class

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.melanbide49;

import es.altia.agora.technical.ConstantesDatos;
import es.altia.common.exception.TechnicalException;
import es.altia.flexia.integracion.moduloexterno.melanbide49.manager.MeLanbide49Manager;
import es.altia.flexia.integracion.moduloexterno.plugin.ModuloIntegracionExterno;
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

/**
 *
 * @author santiagoc
 */
public class MELANBIDE49 extends ModuloIntegracionExterno {

    //Logger
    private static Logger log = LogManager.getLogger(MELANBIDE49.class);
    ResourceBundle m_Conf = ResourceBundle.getBundle("common");

    public String insertarSecuencia(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente) {
        log.info("insertarSecuencia ( codOrganizacion = " + codOrganizacion + " codTramite = " + codTramite + " ocurrenciaTramite = " + ocurrenciaTramite + " numExpediente = " + numExpediente + " ) : BEGIN");
        String clave = null;
        String codigoOperacion = "0";
        String mensaje = "";
        Boolean insertOK=false;
        try {
            if (numExpediente != null && !"".equals(numExpediente)) {
                 log.debug("insertarSecuencia() : ANTES de adaptador ");
                AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
                log.debug("insertarSecuencia() : ANTES de manager.getInstance ");
                MeLanbide49Manager meLanbide49Manager = MeLanbide49Manager.getInstance();
                log.debug("insertarSecuencia() : ANTES de insertar ");
//                 insertOK = meLanbide49Manager.insertarSecuencia(codOrganizacion, numExpediente, String.valueOf(codTramite), adapt);
                 
                     insertOK =        MeLanbide49Manager.getInstance().insertarSecuencia(codOrganizacion, numExpediente, String.valueOf(codTramite), adapt);
                if (insertOK) {
                    log.info("Numero de Agencia grabado Correctamente");
                } else {
                    log.error("NO se ha insertado correctamente el Numero de Agencia");
                    codigoOperacion = "1";
                }

            }
        } catch (Exception ex) {
            log.error("NO se ha insertado correctamente el Numero de Agencia - " + ex);
            codigoOperacion = "2";
        }
//        if(log.isDebugEnabled()) log.debug("insertarSecuencia() : END");
        log.info("insertarSecuencia() : END - devuelve " + codigoOperacion);
        return codigoOperacion;
    }

    /**
     * Operación que recupera los datos de conexión a la BBDD
     *
     * @param codOrganizacion
     * @return AdaptadorSQLBD
     */
    private AdaptadorSQLBD getAdaptSQLBD(String codOrganizacion) {
        if (log.isDebugEnabled()) {
            log.debug("getConnection ( codOrganizacion = " + codOrganizacion + " ) : BEGIN");
        }
        ResourceBundle config = ResourceBundle.getBundle("techserver");
        String gestor = config.getString("CON.gestor");
        String jndiGenerico = config.getString("CON.jndi");
        Connection conGenerico = null;
        Statement st = null;
        ResultSet rs = null;
        String[] salida = null;
        Connection con = null;

        if (log.isDebugEnabled()) {
            log.debug("getJndi =========> ");
            log.debug("parametro codOrganizacion: " + codOrganizacion);
            log.debug("gestor: " + gestor);
            log.debug("jndi: " + jndiGenerico);
        }//if(log.isDebugEnabled())

        DataSource ds = null;
        AdaptadorSQLBD adapt = null;
        synchronized (this) {
            try {
                PortableContext pc = PortableContext.getInstance();
                if (log.isDebugEnabled()) {
                    log.debug("He cogido el jndi: " + jndiGenerico);
                }
                ds = (DataSource) pc.lookup(jndiGenerico, DataSource.class);
                // Conexión al esquema genérico
                conGenerico = ds.getConnection();

                String sql = "SELECT EEA_BDE FROM A_EEA WHERE EEA_APL=" + ConstantesDatos.APP_GESTION_EXPEDIENTES + " AND AAE_ORG=" + codOrganizacion;
                st = conGenerico.createStatement();
                rs = st.executeQuery(sql);
                String jndi = null;
                while (rs.next()) {
                    jndi = rs.getString("EEA_BDE");
                }//while(rs.next())

                st.close();
                rs.close();
                conGenerico.close();

                if (jndi != null && gestor != null && !"".equals(jndi) && !"".equals(gestor)) {
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
            } catch (TechnicalException te) {
                te.printStackTrace();
                log.error("*** AdaptadorSQLBD: " + te.toString());
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (st != null) {
                        st.close();
                    }
                    if (rs != null) {
                        rs.close();
                    }
                    if (conGenerico != null && !conGenerico.isClosed()) {
                        conGenerico.close();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }//try-catch
            }// finally
            if (log.isDebugEnabled()) {
                log.debug("getConnection() : END");
            }
        }// synchronized
        return adapt;
    }//getConnection
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package es.altia.flexia.integracion.moduloexterno.meikus.persistence.dao;

import es.altia.flexia.integracion.moduloexterno.meikus.utilidades.MeIkus01ConfigurationParameter;
import es.altia.common.service.config.Config;
import es.altia.common.service.config.ConfigServiceHelper;
import es.altia.flexia.integracion.moduloexterno.meikus.utilidades.MeIkus01Constantes;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

/**
 *
 * @author pablo.bugia
 */
public class DatosPasikusDAO {

    //Instancia
    private static DatosPasikusDAO instance = null;

    //Ficheros de configuracion
    protected static Config campos; // Para el fichero de configuracion técnico
    protected static Config m_ConfigError; // Para los mensajes de error localizados

    //Logger
    protected static Logger m_Log = LogManager.getLogger(DatosPasikusDAO.class);

    /**
     * Constructor por defecto
     */
    private DatosPasikusDAO() {
        super();
        // Queremos usar el fichero de configuracion techserver
        campos = ConfigServiceHelper.getConfig("techserver");
        // Queremos tener acceso a los mensajes de error localizados
        m_ConfigError = ConfigServiceHelper.getConfig("error");
    }//ConvocatoriaDAO

    /**
     * Devuelve una unica instancia (Singleton) de la clase ConvocatoriaDAO
     *
     * @return ConvocatoriaDAO
     */
    public static DatosPasikusDAO getInstance() {
        // si no hay ninguna instancia de esta clase tenemos que crear una
        if (instance == null) {
            // Necesitamos sincronizacion para serializar (no multithread)
            // Las invocaciones de este metodo
            synchronized (DatosPasikusDAO.class) {
                if (instance == null) {
                    m_Log.debug("Creamos una instancia de la clase");
                    instance = new DatosPasikusDAO();
                }//if (instance == null) 
            }//synchronized (ConvocatoriaDAO.class) 
        }//if (instance == null) 
        return instance;
    }//getInstance;

    public BigDecimal getValorCampoNumerico(int codOrganizacion, String numExp, String ejercicio, String codigoCampo, Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        BigDecimal valor = null;
        try {
            String query = null;
            query = "select TNU_VALOR from " + MeIkus01ConfigurationParameter.getParameter(MeIkus01Constantes.TABLA_TIPO_DATO_NUMERICO, MeIkus01Constantes.FICHERO_PROPIEDADES)
                    + " where TNU_MUN = '" + codOrganizacion + "' and TNU_EJE = '" + ejercicio
                    + "' and TNU_NUM = '" + numExp + "' and TNU_COD = '" + codigoCampo + "'";
            if (m_Log.isDebugEnabled()) {
                m_Log.debug("sql = " + query);
            }
            st = con.createStatement();
            rs = st.executeQuery(query);
            if (rs.next()) {
                valor = rs.getBigDecimal("TNU_VALOR");
            }
        } catch (Exception ex) {
            m_Log.error("Se ha producido un error recuperando el campo suplementario numerico " + codigoCampo + " para el expediente " + numExp, ex);
            throw new Exception(ex);
        } finally {
            try {
                if (m_Log.isDebugEnabled()) {
                    m_Log.debug("Procedemos a cerrar el statement y el resultset");
                }
                if (st != null) {
                    st.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
                m_Log.error("Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }
        m_Log.debug("Valor " + codigoCampo + ": " + valor);
        return valor;
    }

    public String getValorCampoTexto(int codOrganizacion, String numExp, String ejercicio, String codigoCampo, Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        String valor = null;
        try {
            String query = null;
            query = "select TXT_VALOR from " + MeIkus01ConfigurationParameter.getParameter(MeIkus01Constantes.TABLA_TIPO_DATO_TEXTO, MeIkus01Constantes.FICHERO_PROPIEDADES)
                    + " where TXT_MUN = '" + codOrganizacion + "' and TXT_EJE = '" + ejercicio
                    + "' and TXT_NUM = '" + numExp + "' and TXT_COD = '" + codigoCampo + "'";
            if (m_Log.isDebugEnabled()) {
                m_Log.debug("sql = " + query);
            }
            st = con.createStatement();
            rs = st.executeQuery(query);
            if (rs.next()) {
                valor = rs.getString("TXT_VALOR");
            }
        } catch (Exception ex) {
            m_Log.error("Se ha producido un error recuperando el campo suplementario texto " + codigoCampo + " para el expediente " + numExp, ex);
            throw new Exception(ex);
        } finally {
            try {
                if (m_Log.isDebugEnabled()) {
                    m_Log.debug("Procedemos a cerrar el statement y el resultset");
                }
                if (st != null) {
                    st.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
                m_Log.error("Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }
        m_Log.debug("Valor " + codigoCampo + ": " + valor);
        return valor;
    }

    public String getValorCampoDesplegable(int codOrganizacion, String numExp, String ejercicio, String codigoCampo, Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        String valor = null;
        try {
            String query = null;
            query = "select TDE_VALOR from " + MeIkus01ConfigurationParameter.getParameter(MeIkus01Constantes.TABLA_TIPO_DATO_DESPLEGABLE, MeIkus01Constantes.FICHERO_PROPIEDADES)
                    + " where TDE_MUN = '" + codOrganizacion + "' and TDE_EJE = '" + ejercicio
                    + "' and TDE_NUM = '" + numExp + "' and TDE_COD = '" + codigoCampo + "'";
            if (m_Log.isDebugEnabled()) {
                m_Log.debug("sql = " + query);
            }
            st = con.createStatement();
            rs = st.executeQuery(query);
            if (rs.next()) {
                valor = rs.getString("TDE_VALOR");
            }
        } catch (Exception ex) {
            m_Log.error("Se ha producido un error recuperando el campo suplementario desplegable " + codigoCampo + " para el expediente " + numExp, ex);
            throw new Exception(ex);
        } finally {
            try {
                if (m_Log.isDebugEnabled()) {
                    m_Log.debug("Procedemos a cerrar el statement y el resultset");
                }
                if (st != null) {
                    st.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
                m_Log.error("Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }
        m_Log.debug("Valor " + codigoCampo + ": " + valor);
        return valor;
    }


}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package es.altia.flexia.integracion.moduloexterno.melanbide67.dao;

import es.altia.common.service.config.Config;
import es.altia.common.service.config.ConfigServiceHelper;
import es.altia.flexia.integracion.moduloexterno.melanbide67.util.ConfigurationParameter;
import es.altia.flexia.integracion.moduloexterno.melanbide67.util.ConstantesMeLanbide67;
import es.altia.flexia.integracion.moduloexterno.melanbide67.vo.DetallesCampoSuplementarioVO;
import es.altia.agora.technical.CamposFormulario;
import es.altia.flexia.registro.digitalizacion.lanbide.vo.GeneralComboVO;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

/**
 *
 * @author pablo.bugia
 */
public class PersonaContratadaPuestoTrabajoDAO {

    //Instancia
    private static PersonaContratadaPuestoTrabajoDAO instance = null;

    //Ficheros de configuracion
    protected static Config campos; // Para el fichero de configuracion técnico
    protected static Config m_ConfigError; // Para los mensajes de error localizados

    //Logger
    protected static Logger m_Log = LogManager.getLogger(PersonaContratadaPuestoTrabajoDAO.class);

    /**
     * Constructor por defecto
     */
    private PersonaContratadaPuestoTrabajoDAO() {
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
    public static PersonaContratadaPuestoTrabajoDAO getInstance() {
        // si no hay ninguna instancia de esta clase tenemos que crear una
        if (instance == null) {
            // Necesitamos sincronizacion para serializar (no multithread)
            // Las invocaciones de este metodo
            synchronized (PersonaContratadaPuestoTrabajoDAO.class) {
                if (instance == null) {
                    m_Log.debug("Creamos una instancia de la clase");
                    instance = new PersonaContratadaPuestoTrabajoDAO();
                }//if (instance == null) 
            }//synchronized (ConvocatoriaDAO.class) 
        }//if (instance == null) 
        return instance;
    }//getInstance;

    public DetallesCampoSuplementarioVO getDetallesCampo(final String procedimiento, final String codCampo, Connection con) throws Exception {
        DetallesCampoSuplementarioVO detallesCampoSuplementario = null;
        Statement st = null;
        ResultSet rs = null;
        try {
            String query = null;
            query = "select PCA_TDA, PCA_TAM, PCA_BLOQ, PCA_OCULTO, PCA_DESPLEGABLE from " + ConfigurationParameter.getParameter(ConstantesMeLanbide67.TABLA_TIPO_DATO_SUPL, ConstantesMeLanbide67.FICHERO_PROPIEDADES)
                    + " where PCA_COD = '" + codCampo + "' AND PCA_PRO = '" + procedimiento + "'";
            if (m_Log.isDebugEnabled()) {
                m_Log.debug("sql = " + query);
            }
            st = con.createStatement();
            rs = st.executeQuery(query);
            if (rs.next()) {
                detallesCampoSuplementario = new DetallesCampoSuplementarioVO(null, rs.getString("PCA_TDA"),
                        rs.getString("PCA_TAM"), rs.getString("PCA_BLOQ"), rs.getString("PCA_OCULTO"),
                        rs.getString("PCA_DESPLEGABLE"));
            }
        } catch (Exception ex) {
            m_Log.error("Se ha producido un error recuperando las propiedades del campo suplementario  " + codCampo, ex);
            throw new Exception(ex);
        } finally {
            try {
                if (m_Log.isDebugEnabled()) {
                    m_Log.debug("getDetallesCampo: Procedemos a cerrar el statement y el resultset");
                }
                if (st != null) {
                    st.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
                m_Log.error("getDetallesCampo: Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }
        m_Log.debug("getDetallesCampo campo = " + codCampo);
        return detallesCampoSuplementario;
    }

    public String getValorCampoDesplegable(int codOrganizacion, String numExp, String ejercicio, String codigoCampo, Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        String valor = null;
        try {
            String query = null;
            query = "select TDE_VALOR from " + ConfigurationParameter.getParameter(ConstantesMeLanbide67.TABLA_TIPO_DATO_DESPLEGABLE, ConstantesMeLanbide67.FICHERO_PROPIEDADES)
                    + " where TDE_MUN = '" + codOrganizacion + "' and TDE_EJE = '" + ejercicio
                    + "' and TDE_NUM = '" + numExp + "' and TDE_COD = '" + codigoCampo + "'";
            if (m_Log.isDebugEnabled()) {
                m_Log.debug("sql = " + query);
            }
            st = con.createStatement();
            rs = st.executeQuery(query);
            if (rs.next()) {
                valor = rs.getString("TDEX_VALOR");
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

    public String getValorCampoDesplegableExterno(int codOrganizacion, String numExp, String ejercicio, String codigoCampo, Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        String valor = null;
        try {
            String query = null;
            query = "select TDEX_CODSEL from " + ConfigurationParameter.getParameter(ConstantesMeLanbide67.TABLA_TIPO_DATO_DESPLEGABLE_EXTERNO, ConstantesMeLanbide67.FICHERO_PROPIEDADES)
                    + " where TDEX_MUN = '" + codOrganizacion + "' and TDEX_EJE = '" + ejercicio
                    + "' and TDEX_NUM = '" + numExp + "' and TDEX_COD = '" + codigoCampo + "'";
            if (m_Log.isDebugEnabled()) {
                m_Log.debug("sql = " + query);
            }
            st = con.createStatement();
            rs = st.executeQuery(query);
            if (rs.next()) {
                valor = rs.getString("TDEX_CODSEL");
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

    public CamposFormulario getValoresFichero(int codOrganizacion, String numExp, String ejercicio, String codigoCampo, Connection con) {
        PreparedStatement ps = null;
        ResultSet rs = null;
        HashMap campos = new HashMap();
        CamposFormulario cF = null;

        try {
            String sql = "select TFI_COD, TFI_NOMFICH, TFI_VALOR, TFI_MIME from " + ConfigurationParameter.getParameter(ConstantesMeLanbide67.TABLA_TIPO_DATO_FICHERO, ConstantesMeLanbide67.FICHERO_PROPIEDADES)
                    + " where TFI_MUN = '" + codOrganizacion + "' and TFI_EJE = '" + ejercicio
                    + "' and TFI_NUM = '" + numExp + "' and TFI_COD = '" + codigoCampo + "'";
            if (m_Log.isDebugEnabled()) {
                m_Log.debug(sql);
            }

            int i = 1;
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();

            String entrar = "no";
            while (rs.next()) {
                entrar = "si";
                String codCampo = rs.getString("tfi_cod");
                String valorCampo = rs.getString("tfi_nomfich");

                final Blob fichero = rs.getBlob("tfi_valor");

                campos.put(codCampo, fichero);

                String mime = rs.getString("tfi_mime");
                campos.put(codCampo + "_TIPO", mime);
                campos.put(codCampo + "_NOMBRE", valorCampo);

                cF = new CamposFormulario(campos);
            }
            if ("no".equals(entrar)) {
                cF = new CamposFormulario(campos);
            }
        } catch (Exception e) {
            cF = null;
            m_Log.error("Excepcion capturada en: " + getClass().getName() + ", "
                    + e.getMessage());
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
            } catch (Exception ex) {
                m_Log.error("Excepcion capturada en: " + getClass().getName() + ", "
                        + ex.getMessage());
            }
        }
        return cF;
    }

    public List<GeneralComboVO> cargarDesplegable(Connection con, String codDesplegable) throws Exception {
        if (m_Log.isInfoEnabled()) {
            m_Log.debug("INICIA cargarDesplegable DAO");
        }
        Statement st = null;
        ResultSet rs = null;
        final List<GeneralComboVO> listaReturn = new ArrayList<GeneralComboVO>();
        try {
            final String sql = "select DES_VAL_COD, DES_NOM from "
                    + ConfigurationParameter.getParameter(ConstantesMeLanbide67.TABLA_E_DES_VAL, ConstantesMeLanbide67.FICHERO_PROPIEDADES)
                    + " where DES_COD = '" + codDesplegable
                    + "' order by DES_NOM";
            if (m_Log.isInfoEnabled()) {
                m_Log.debug("cargarDesplegable: " + sql);
            }
            st = con.createStatement();
            rs = st.executeQuery(sql);
            while (rs.next()) {
                GeneralComboVO generalComboVO = new GeneralComboVO();
                generalComboVO.setCodigo(rs.getString("DES_VAL_COD"));
                generalComboVO.setDescripcion(rs.getString("DES_NOM"));
                listaReturn.add(generalComboVO);
            }
        } catch (SQLException e) {
            m_Log.error("Se ha producido un error recuperando los valores del desplegable - " + codDesplegable, e);
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (st != null) {
                st.close();
            }
        }
        return listaReturn;
    }

    public List<GeneralComboVO> cargarDesplegableExterno(Connection con,
            String vista, String nombreCod, String nombreDesc) throws Exception {
        if (m_Log.isInfoEnabled()) {
            m_Log.debug("INICIA cargarDesplegableExterno DAO");
        }
        Statement st = null;
        ResultSet rs = null;
        final List<GeneralComboVO> listaReturn = new ArrayList<GeneralComboVO>();
        try {
            final String sql = "select " + nombreCod + ", " + nombreDesc + " from "
                    + vista
                    + " order by " + nombreDesc;
            if (m_Log.isInfoEnabled()) {
                m_Log.debug("cargarDesplegableExterno: " + sql);
            }
            st = con.createStatement();
            rs = st.executeQuery(sql);
            while (rs.next()) {
                GeneralComboVO generalComboVO = new GeneralComboVO();
                generalComboVO.setCodigo(rs.getString(nombreCod));
                generalComboVO.setDescripcion(rs.getString(nombreDesc));
                listaReturn.add(generalComboVO);
            }
        } catch (SQLException e) {
            m_Log.error("Se ha producido un error recuperando los municipios - ", e);
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (st != null) {
                st.close();
            }
        }
        return listaReturn;
    }
}

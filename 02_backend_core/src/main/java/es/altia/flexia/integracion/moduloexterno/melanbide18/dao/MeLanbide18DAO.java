/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.melanbide18.dao;

import es.altia.flexia.integracion.moduloexterno.melanbide18.util.ConfigurationParameter;
import es.altia.flexia.integracion.moduloexterno.melanbide18.util.ConstantesMeLanbide18;
import es.altia.flexia.integracion.moduloexterno.melanbide18.util.MeLanbide18MappingUtils;
import es.altia.flexia.integracion.moduloexterno.melanbide18.vo.FilaDeudaFraccVO;
import es.altia.flexia.integracion.moduloexterno.melanbide18.vo.DeudaZorkuVO;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

/**
 *
 * @author santiagoc
 */
public class MeLanbide18DAO {
    
    //Logger
    private static Logger log = LogManager.getLogger(MeLanbide18DAO.class);
    
    //Instancia
    private static MeLanbide18DAO instance = null;
    
    private MeLanbide18DAO() {
        
    }
    
    public static MeLanbide18DAO getInstance() {
        if(instance == null) {
            synchronized(MeLanbide18DAO.class) {
                instance = new MeLanbide18DAO();
            }
        }
        return instance;
    }    
    
    public String getDocumentoInteresado(String numExpediente, int codOrganizacion, Connection con) throws Exception {
        log.debug("getDocumentoInteresado " + numExpediente);
        Statement st = null;
        ResultSet rs = null;
        String numDoc = "";
        try {
            String query = "SELECT HTE_DOC FROM " + ConfigurationParameter.getParameter(ConstantesMeLanbide18.TABLA_E_EXT, ConstantesMeLanbide18.FICHERO_PROPIEDADES)
                    + " INNER JOIN " + ConfigurationParameter.getParameter(ConstantesMeLanbide18.TABLA_T_HTE, ConstantesMeLanbide18.FICHERO_PROPIEDADES)
                    + " ON EXT_TER = HTE_TER AND EXT_NVR = HTE_NVR AND EXT_ROL = 1"
                    + " and EXT_PRO = '" + numExpediente.split(ConstantesMeLanbide18.BARRA)[1] + "' and EXT_NUM = '" + numExpediente + "'";
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            rs = st.executeQuery(query);
            if (rs.next()) {
                numDoc = rs.getString("HTE_DOC");
            }
        } catch (SQLException ex) {
            log.error("Se ha producido un error recuperando el documento del interesado ", ex);
            throw new Exception(ex);
        } finally {
            if (st != null) {
                st.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        return numDoc;
    }
    
    public List<DeudaZorkuVO> getDeudasZorku(String numDocumento, int codOrg, Connection con) throws Exception {
        List<DeudaZorkuVO> lista = new ArrayList<DeudaZorkuVO>();
        PreparedStatement ps = null;
        ResultSet rs = null;
        String query = null;
        try {
            query = "select * from " + ConfigurationParameter.getParameter(ConstantesMeLanbide18.VISTA_DEUDAS_A_FRACCIONAR, ConstantesMeLanbide18.FICHERO_PROPIEDADES)
                    + " where NUM_DOCUMENTO = ? "
                    + " order by NUM_LIQUIDACION";
            log.debug("sql = " + query);
            int i = 1;
            ps = con.prepareStatement(query);
            ps.setString(i++, numDocumento);
            rs = ps.executeQuery();
            while (rs.next()) {
                DeudaZorkuVO deuda = (DeudaZorkuVO) MeLanbide18MappingUtils.getInstance().map(rs, DeudaZorkuVO.class);
                lista.add(deuda);
            }
        } catch (Exception ex) {
            log.error("Se ha producido un error recuperando las deudas de ZORKU", ex);
            throw new Exception(ex);
        } finally {
            if (ps != null) {
                ps.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        return lista;
    }

    /**
     *
     * @param numExp
     * @param numDocumento
     * @param codOrg
     * @param con
     * @return
     * @throws Exception
     */
    public List<DeudaZorkuVO> getDeudasZorkuSinGrabar(String numExp, String numDocumento, int codOrg, Connection con) throws Exception {
        List<DeudaZorkuVO> lista = new ArrayList<DeudaZorkuVO>();
        PreparedStatement ps = null;
        ResultSet rs = null;
        String query = null;
        try {
            query = "select * from " + ConfigurationParameter.getParameter(ConstantesMeLanbide18.VISTA_DEUDAS_A_FRACCIONAR, ConstantesMeLanbide18.FICHERO_PROPIEDADES)
                    + " where NUM_DOCUMENTO = ? and NUM_LIQUIDACION not in "
                    + "(select NUM_LIQUIDACION from " + ConfigurationParameter.getParameter(ConstantesMeLanbide18.TABLA_MELANBIDE18_DEUDASFRAC, ConstantesMeLanbide18.FICHERO_PROPIEDADES)
                    + " where NUM_EXP = ?)"
                    + " order by NUM_LIQUIDACION";
                log.debug("sql = " + query);
            int i = 1;
            ps = con.prepareStatement(query);
            ps.setString(i++, numDocumento);
            ps.setString(i++, numExp);
            rs = ps.executeQuery();
            while (rs.next()) {
                DeudaZorkuVO deuda = (DeudaZorkuVO) MeLanbide18MappingUtils.getInstance().map(rs, DeudaZorkuVO.class);
                lista.add(deuda);
            }
        } catch (Exception ex) {
            log.error("Se ha producido un error recuperando las deudas de ZORKU", ex);
            throw new Exception(ex);
        } finally {
            if (ps != null) {
                ps.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        return lista;
    }
    
    /**
     *
     * @param numDocumento
     * @param codOrganizacion
     * @param con
     * @return
     * @throws Exception
     */
    public boolean tieneDeudasZorkuSinFraccionar(String numDocumento, int codOrganizacion, Connection con) throws Exception {
        log.info("tieneDeudasZorkuSinFraccionar ");
        PreparedStatement ps = null;
        ResultSet rs = null;
        String query = null;
        boolean tiene = false;
        try {
            query = "select count(*) DEUDAS from " + ConfigurationParameter.getParameter(ConstantesMeLanbide18.VISTA_DEUDAS_A_FRACCIONAR, ConstantesMeLanbide18.FICHERO_PROPIEDADES)
                    + " where NUM_DOCUMENTO =?";
            log.debug("sql = " + query);
            int i = 1;
            ps = con.prepareStatement(query);
            ps.setString(i++, numDocumento);
            rs = ps.executeQuery();
            if (rs.next()) {
                tiene = rs.getInt("DEUDAS") > 0;
            }
        } catch (SQLException ex) {
            log.error("Se ha producido un error comprobando si hay deudas para fraccionar en ZORKU", ex);
            throw new Exception(ex);
        } finally {
            if (ps != null) {
                ps.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        return tiene;
    }

    public DeudaZorkuVO getDeudaZorkuPorNumLiq(Long numLiquidacion, int codOrganizacion, Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        DeudaZorkuVO deuda = null;

        try {
            String query = null;
            query = "SELECT * FROM " + ConfigurationParameter.getParameter(ConstantesMeLanbide18.VISTA_DEUDAS_A_FRACCIONAR, ConstantesMeLanbide18.FICHERO_PROPIEDADES)
                    + " WHERE NUM_LIQUIDACION = " + numLiquidacion;
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            rs = st.executeQuery(query);
            if (rs.next()) {
                deuda = (DeudaZorkuVO) MeLanbide18MappingUtils.getInstance().map(rs, DeudaZorkuVO.class);
            }
        } catch (Exception ex) {
            log.error("Se ha producido un error recuperando deudas ", ex);
            throw new Exception(ex);
        } finally {
            if (st != null) {
                st.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        return deuda;
    }    

    public List<FilaDeudaFraccVO> getDeudasFracc(String numExp, int codOrganizacion, Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        List<FilaDeudaFraccVO> lista = new ArrayList<FilaDeudaFraccVO>();

        try {
            String query = null;
            query = "SELECT * FROM " + ConfigurationParameter.getParameter(ConstantesMeLanbide18.TABLA_MELANBIDE18_DEUDASFRAC, ConstantesMeLanbide18.FICHERO_PROPIEDADES)
                    + " WHERE NUM_EXP ='" + numExp + "'"
                    + " ORDER BY ID";
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                FilaDeudaFraccVO deuda = (FilaDeudaFraccVO) MeLanbide18MappingUtils.getInstance().map(rs, FilaDeudaFraccVO.class);
                //Cargamos en el request los valores de los desplegables
                lista.add(deuda);
            }
        } catch (Exception ex) {
            log.error("Se ha producido un error recuperando deudas ", ex);
            throw new Exception(ex);
        } finally {
            if (st != null) {
                st.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        return lista;
    }    
    
    public FilaDeudaFraccVO getDeudaFraccPorNumLiquidacion(String numExp, Long numLiquidacion, Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        FilaDeudaFraccVO deuda = null;

        try {
            String query = null;
            query = "SELECT * FROM " + ConfigurationParameter.getParameter(ConstantesMeLanbide18.TABLA_MELANBIDE18_DEUDASFRAC, ConstantesMeLanbide18.FICHERO_PROPIEDADES)
                    + " WHERE NUM_LIQUIDACION =" + numLiquidacion
                    + " AND NUM_EXP = '" + numExp + "'";
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            rs = st.executeQuery(query);
            if (rs.next()) {
                deuda = (FilaDeudaFraccVO) MeLanbide18MappingUtils.getInstance().map(rs, FilaDeudaFraccVO.class);               
            }
        } catch (Exception ex) {
            log.error("Se ha producido un error recuperando deudas ", ex);
            throw new Exception(ex);
        } finally {
            if (st != null) {
                st.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        return deuda;
    }
    
  public FilaDeudaFraccVO getDeudaFraccPorID(Integer id, Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        FilaDeudaFraccVO deuda = null;
        try {
            String query = null;
            query = "SELECT * FROM " + ConfigurationParameter.getParameter(ConstantesMeLanbide18.TABLA_MELANBIDE18_DEUDASFRAC, ConstantesMeLanbide18.FICHERO_PROPIEDADES)
                    + " WHERE ID = " + (id != null ? id : "null");
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            rs = st.executeQuery(query);
            if (rs.next()) {
                deuda = (FilaDeudaFraccVO) MeLanbide18MappingUtils.getInstance().map(rs, FilaDeudaFraccVO.class);
            }
        } catch (Exception ex) {
            log.error("Se ha producido un error recuperando una deuda fraccionada : " + id, ex);
            throw new Exception(ex);
        } finally {
            if (st != null) {
                st.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        return deuda;
    }
  
      public boolean crearDeudaFracc(FilaDeudaFraccVO deuda, Connection con) throws Exception {
        Statement st = null;
        String query = "";
        try {
            int id = recogerIDInsertar(ConfigurationParameter.getParameter(ConstantesMeLanbide18.SECUENCIA_M18_DEUDASFRAC, ConstantesMeLanbide18.FICHERO_PROPIEDADES), con);
            query = "INSERT INTO " + ConfigurationParameter.getParameter(ConstantesMeLanbide18.TABLA_MELANBIDE18_DEUDASFRAC, ConstantesMeLanbide18.FICHERO_PROPIEDADES)
                    + "(ID, NUM_EXP, NUM_EXP_DEUDA, DEUDA_IMPORTE, NUM_LIQUIDACION)"
                    + " VALUES (" + id
                    + ", '" + deuda.getNumExp()
                    + "', '" + deuda.getNumExpDeuda()
                    + "', " + deuda.getDeudaImporte() 
                    + ", " + deuda.getNumLiquidacion() + ")";
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            int insert = st.executeUpdate(query);
            if (insert > 0) {
                return true;
            } else {
                log.error("No Se ha podido guardar una nueva deuda fraccionada");
                return false;
            }

        } catch (Exception ex) {
            log.error("Se ha producido un error al insertar una nueva deuda fraccionada" + ex.getMessage());
            throw new Exception(ex);
        } finally {
            if (st != null) {
                st.close();
            }
        }
    }

    public int eliminarDeudaFracc(Integer id, Connection con) throws Exception {
        Statement st = null;
        try {
            String query = null;
            query = "DELETE FROM " + ConfigurationParameter.getParameter(ConstantesMeLanbide18.TABLA_MELANBIDE18_DEUDASFRAC, ConstantesMeLanbide18.FICHERO_PROPIEDADES)
                    + " WHERE ID = " + id;
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            return st.executeUpdate(query);
        } catch (SQLException ex) {
            log.error("Se ha producido un error eliminando deuda fraccinada ID : " + id, ex);
            throw new Exception(ex);
        } finally {
            if (st != null) {
                st.close();
            }
        }
    }

    /**
     *
     * @param numExp
     * @param con
     * @return
     * @throws Exception
     */
    public List<String> getListaExpedientesFraccionar(String numExp, Connection con) throws Exception {
        log.info("getListaExpedientesFraccionar");
        List<String> expedientesFraccionar = new ArrayList<String>();
        String expedienteFracc = "";
        PreparedStatement ps = null;
        ResultSet rs = null;
        String query = null;
        try {
            query = "select NUM_EXP_DEUDA from " + ConfigurationParameter.getParameter(ConstantesMeLanbide18.TABLA_MELANBIDE18_DEUDASFRAC, ConstantesMeLanbide18.FICHERO_PROPIEDADES)
                    + " where NUM_EXP = ? order by NUM_EXP_DEUDA";
                log.debug("sql = " + query);
            int i = 1;
            ps = con.prepareStatement(query);
            ps.setString(i++, numExp);
            rs = ps.executeQuery();
            while (rs.next()) {
                expedienteFracc = rs.getString("NUM_EXP_DEUDA");
                expedientesFraccionar.add(expedienteFracc);
            }
        } catch (SQLException ex) {
            log.error("Se ha producido un error recuperando la lista de expedientes a Fraccionar ", ex);
            throw new Exception(ex);
        } finally {
            if (ps != null) {
                ps.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        return expedientesFraccionar;
    }    
    
    /**
     *
     * @param codOrg
     * @param numExp
     * @param con
     * @return
     * @throws Exception
     */
    public boolean estaEnTramiteNotificacion(int codOrg, String numExp, Connection con) throws Exception {
        log.info("getCodigoInternoTramite ");
        PreparedStatement ps = null;
        ResultSet rs = null;
        boolean esta = false;
        String query = null;
        int codInterno = 0;
        int codExterno = ConstantesMeLanbide18.COD_TRAM_NOTIFICACION;
        String codProcedimiento = numExp.split(ConstantesMeLanbide18.BARRA)[1];
        try {
            codInterno = getCodigoInternoTramite(codOrg, codProcedimiento, codExterno, con);
            query = "select count(1) ESTA from " + ConfigurationParameter.getParameter(ConstantesMeLanbide18.TABLA_E_CRO, ConstantesMeLanbide18.FICHERO_PROPIEDADES)
                    + " where COD_PRO = ? and CRO_EJE = ? and CRO_MUN = ? and AND CRO_NUM = ? and  AND CRO_TRA = ? and CRO_FEF is null";
            log.debug("sql = " + query);
            int i = 1;
            ps = con.prepareStatement(query);
            ps.setString(i++, codProcedimiento);
            ps.setString(i++, numExp.split(ConstantesMeLanbide18.BARRA)[0]);
            ps.setInt(i++, codOrg);
            ps.setString(i++, numExp);
            ps.setInt(i++, codInterno);
            rs = ps.executeQuery();
            if (rs.next()) {
                esta = (rs.getInt("ESTA") > 0);
            }
        } catch (Exception e) {
            log.error("Se ha producido un error al comprobar si está en  el trámite de Notificación - " + e);
            throw new Exception(e);
        } finally {
            if (ps != null) {
                ps.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        return esta;
    }
        
    /**
     *
     * @param codOrg
     * @param numExp
     * @param con
     * @return
     * @throws Exception
     */
    public boolean tieneMarcaTelematica(int codOrg, String numExp, Connection con) throws Exception {
        log.info("tieneMarcaTelematica ");
        PreparedStatement ps = null;
        ResultSet rs = null;
        boolean tiene = false;
        String query = null;
        try {
            query = "select count(EXT_NOTIFICACION_ELECTRONICA) TIENE from " + ConfigurationParameter.getParameter(ConstantesMeLanbide18.TABLA_E_EXT, ConstantesMeLanbide18.FICHERO_PROPIEDADES)
                    + " where EXT_NUM = ? and EXT_EJE = ? and EXT_PRO = ? and EXT_MUN = ? and EXT_NOTIFICACION_ELECTRONICA = 1";
                log.debug("sql = " + query);
            int i = 1;
            ps = con.prepareStatement(query);
            ps.setString(i++, numExp);
            ps.setString(i++, numExp.split(ConstantesMeLanbide18.BARRA)[0]);
            ps.setString(i++, numExp.split(ConstantesMeLanbide18.BARRA)[1]);
            ps.setInt(i++, codOrg);
            rs = ps.executeQuery();
            if (rs.next()) {
                tiene = (rs.getInt("TIENE") > 0);
            }
        } catch (SQLException e) {
            log.error("Se ha producido un error al comprobar si hay marca de notificación telemática - " + e);
            throw new Exception(e);
        } finally {
            if (ps != null) {
                ps.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        return tiene;
    }    
    
    private int recogerIDInsertar(String sequence, Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        int id = 0;
        try {
            String query = "SELECT " + sequence + ".NextVal AS PROXID FROM DUAL ";
            log.debug("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            if (rs.next()) {
                id = rs.getInt("PROXID");
            }
        } catch (SQLException ex) {
            log.error("Se ha producido un error recuperando el número de ID para inserción al llamar la secuencia " + sequence + " : ", ex);
            throw new Exception(ex);
        } finally {
            if (st != null) {
                st.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        return id;
    }        

    private int getCodigoInternoTramite(int codOrg, String procedimiento, int codExterno, Connection con) throws Exception {
        log.info("getCodigoInternoTramite ");
        PreparedStatement ps = null;
        ResultSet rs = null;
        String query = null;
        int codInterno = 0;
        try {
            query = "select TRA_COD from " + ConfigurationParameter.getParameter(ConstantesMeLanbide18.TABLA_E_TRA, ConstantesMeLanbide18.FICHERO_PROPIEDADES)
                    + " where TRA_MUN=? and TRA_PRO=? and TRA_COU=?";
            log.debug("sql = " + query);
            int i = 1;
            ps = con.prepareStatement(query);
            ps.setInt(i++, codOrg);
            ps.setString(i++, procedimiento);
            ps.setInt(i++, codExterno);
            rs = ps.executeQuery();
            if (rs.next()) {
                codInterno = rs.getInt("TRA_COD");
            }
        } catch (SQLException ex) {
            log.error("Se ha producido un error recuperando la lista de expedientes a Fraccionar ", ex);
            throw new Exception(ex);
        } finally {
            if (ps != null) {
                ps.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        return codInterno;
    }
}

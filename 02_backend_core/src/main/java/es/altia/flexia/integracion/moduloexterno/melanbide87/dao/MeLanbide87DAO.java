package es.altia.flexia.integracion.moduloexterno.melanbide87.dao;

import es.altia.flexia.integracion.moduloexterno.melanbide87.util.ConfigurationParameter;
import es.altia.flexia.integracion.moduloexterno.melanbide87.util.ConstantesMeLanbide87;
import es.altia.flexia.integracion.moduloexterno.melanbide87.util.MeLanbide87MappingUtils;
import es.altia.flexia.integracion.moduloexterno.melanbide87.vo.DesplegableAdmonLocalVO;
import es.altia.flexia.integracion.moduloexterno.melanbide87.vo.InstalacionVO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

public class MeLanbide87DAO {

    //Logger
    private static final Logger log = LogManager.getLogger(MeLanbide87DAO.class);

    //Instancia
    private static MeLanbide87DAO instance = null;

    // Constructor
    private MeLanbide87DAO() {

    }

    public static MeLanbide87DAO getInstance() {
        if (instance == null) {
            synchronized (MeLanbide87DAO.class) {
                instance = new MeLanbide87DAO();
            }
        }
        return instance;
    }

    /**
     *
     * @param numExp
     * @param codOrganizacion
     * @param con
     * @return
     * @throws Exception
     */
    public List<InstalacionVO> getListaInstalaciones(String numExp, int codOrganizacion, Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        List<InstalacionVO> lista = new ArrayList<InstalacionVO>();
        InstalacionVO Instalación = new InstalacionVO();
        String query = null;
        try {
            query = "SELECT * FROM " + ConfigurationParameter.getParameter(ConstantesMeLanbide87.MELANBIDE87_INSTALACIONES, ConstantesMeLanbide87.FICHERO_PROPIEDADES)
                    + " WHERE NUM_EXP ='" + numExp + "'"
                    + " ORDER BY ID";
            log.debug("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                Instalación = (InstalacionVO) MeLanbide87MappingUtils.getInstance().map(rs, InstalacionVO.class);
                lista.add(Instalación);
                Instalación = new InstalacionVO();
            }
        } catch (Exception ex) {
            log.error("Se ha producido un error recuperando Instalacións ");
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

    /**
     *
     * @param id
     * @param con
     * @return
     * @throws Exception
     */
    public InstalacionVO getInstalacionPorID(String id, Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        InstalacionVO Instalación = new InstalacionVO();
        String query = null;
        try {
            query = "SELECT * FROM " + ConfigurationParameter.getParameter(ConstantesMeLanbide87.MELANBIDE87_INSTALACIONES, ConstantesMeLanbide87.FICHERO_PROPIEDADES)
                    + " WHERE ID=" + (id != null && !id.equals("") ? id : "null");
            log.debug("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                Instalación = (InstalacionVO) MeLanbide87MappingUtils.getInstance().map(rs, InstalacionVO.class);
            }
        } catch (Exception ex) {
            log.error("Se ha producido un error recuperando una Instalación : " + id, ex);
            throw new Exception(ex);
        } finally {
            if (st != null) {
                st.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        return Instalación;
    }

    /**
     *
     * @param nuevaInstalacion
     * @param con
     * @return
     * @throws Exception
     */
    public boolean crearNuevaInstalacion(InstalacionVO nuevaInstalacion, Connection con) throws Exception {
        PreparedStatement ps = null;
        String query = "";
        int contBD = 1;
        try {
            int id = recogerIDInsertar(ConfigurationParameter.getParameter(ConstantesMeLanbide87.SEQ_MELANBIDE87_INSTALACIONES, ConstantesMeLanbide87.FICHERO_PROPIEDADES), con);
            query = "INSERT INTO " + ConfigurationParameter.getParameter(ConstantesMeLanbide87.MELANBIDE87_INSTALACIONES, ConstantesMeLanbide87.FICHERO_PROPIEDADES)
                    + "(ID, NUM_EXP, TIPINST, INSTMUNICIPIO, INSTLOCALIDAD, INSTDIRECCION, INSTCP)"
                    + " VALUES(?,?,?,?,?,?,?)";
            log.debug("sql = " + query);
            
            ps = con.prepareStatement(query);
            ps.setInt(contBD++, id);
            ps.setString(contBD++, nuevaInstalacion.getNumExp());
            ps.setString(contBD++, nuevaInstalacion.getTipoInst().toUpperCase());
            ps.setString(contBD++, nuevaInstalacion.getMunicipio());
            ps.setString(contBD++, nuevaInstalacion.getLocalidad());
            ps.setString(contBD++, nuevaInstalacion.getDireccion());
            ps.setInt(contBD++, nuevaInstalacion.getCodPost());
            
            if (ps.executeUpdate() > 0) {
                return true;
            } else {
                log.error("No Se ha podido guardar una nueva Instalación ");
                return false;
            }
        } catch (Exception ex) {
            log.error("Se ha producido un error al Insertar una nueva Instalación" + ex.getMessage());
            throw new Exception(ex);
        } finally {
            if (ps != null) {
                ps.close();
            }
        }
    }

    /**
     *
     * @param instalacionModif
     * @param con
     * @return
     * @throws Exception
     */
    public boolean modificarInstalacion(InstalacionVO instalacionModif, Connection con) throws Exception {
        PreparedStatement ps = null;
        String query = "";
        int contBD = 1;
        try {
            query = "UPDATE " + ConfigurationParameter.getParameter(ConstantesMeLanbide87.MELANBIDE87_INSTALACIONES, ConstantesMeLanbide87.FICHERO_PROPIEDADES)
                    + " SET TIPINST = ?, INSTMUNICIPIO=?, INSTLOCALIDAD = ?, INSTDIRECCION=?, INSTCP=? "
                    + " WHERE NUM_EXP = ? AND ID = ?";

            ps = con.prepareStatement(query);
            ps.setString(contBD++, instalacionModif.getTipoInst().toUpperCase());
            ps.setString(contBD++, instalacionModif.getMunicipio());
            ps.setString(contBD++, instalacionModif.getLocalidad());
            ps.setString(contBD++, instalacionModif.getDireccion());
            ps.setInt(contBD++, instalacionModif.getCodPost());          
            ps.setString(contBD++, instalacionModif.getNumExp());
            ps.setInt(contBD++, instalacionModif.getId());

            log.debug("sql = " + query);
            if (ps.executeUpdate() > 0) {
                return true;
            } else {
                log.error("Se ha producido un error al Modificar una Instalación - " + instalacionModif.getId() + " - " + instalacionModif.getNumExp());
                return false;
            }
        } catch (SQLException ex) {
            log.error("Se ha producido un error al Modificar una Instalación - " + instalacionModif.getId() + " - " + ex);
            throw new Exception(ex);
        } finally {
            if (ps != null) {
                ps.close();
            }
        }
    }

    /**
     *
     * @param id
     * @param con
     * @return
     * @throws Exception
     */
    public int eliminarInstalacion(String id, Connection con) throws Exception {
        String query = null;
        Statement st = null;
        try {
            query = "DELETE FROM " + ConfigurationParameter.getParameter(ConstantesMeLanbide87.MELANBIDE87_INSTALACIONES, ConstantesMeLanbide87.FICHERO_PROPIEDADES)
                    + " WHERE ID=" + (id != null && !id.equals("") ? id : "null");
            log.debug("sql = " + query);
            st = con.createStatement();
            return st.executeUpdate(query);
        } catch (SQLException ex) {
            log.error("Se ha producido un error Eliminando Instalación ID : " + id, ex);
            throw new Exception(ex);
        } finally {
            if (st != null) {
                st.close();
            }
        }
    }

    /**
     *
     * @param sequence
     * @param con
     * @return
     * @throws Exception
     */
    private int recogerIDInsertar(String sequence, Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        int id = 0;
        try {
            String query = "SELECT " + sequence + ".NextVal AS PROXID FROM DUAL ";
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            rs = st.executeQuery(query);
            if (rs.next()) {
                id = rs.getInt("PROXID");
            }
        } catch (SQLException ex) {
            log.error("Se ha producido un error recuperando El numero de ID para insercion al llamar la sequencia " + sequence + " : ");
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

    /**
     *
     * @param des_cod
     * @param con
     * @return
     * @throws Exception
     */
    public List<DesplegableAdmonLocalVO> getValoresDesplegablesAdmonLocalxdes_cod(String des_cod, Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        List<DesplegableAdmonLocalVO> lista = new ArrayList<DesplegableAdmonLocalVO>();
        DesplegableAdmonLocalVO valoresDesplegabe = new DesplegableAdmonLocalVO();
        String query = null;
        try {
            query = "SELECT * FROM " + ConfigurationParameter.getParameter(ConstantesMeLanbide87.TABLA_VALORES_DESPLEGABLES, ConstantesMeLanbide87.FICHERO_PROPIEDADES)
                    + " WHERE DES_COD='" + des_cod + "'";
            log.debug("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                valoresDesplegabe = (DesplegableAdmonLocalVO) MeLanbide87MappingUtils.getInstance().map(rs, DesplegableAdmonLocalVO.class);
                lista.add(valoresDesplegabe);
                valoresDesplegabe = new DesplegableAdmonLocalVO();
            }
        } catch (Exception ex) {
            log.error("Se ha producido un error recuperando un valores de un desplegable : " + des_cod, ex);
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
}

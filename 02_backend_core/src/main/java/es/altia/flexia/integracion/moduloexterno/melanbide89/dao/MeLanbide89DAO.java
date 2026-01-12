
package es.altia.flexia.integracion.moduloexterno.melanbide89.dao;

import es.altia.flexia.integracion.moduloexterno.melanbide89.util.ConfigurationParameter;
import es.altia.flexia.integracion.moduloexterno.melanbide89.util.ConstantesMeLanbide89;
import es.altia.flexia.integracion.moduloexterno.melanbide89.util.MeLanbide89MappingUtils;
import es.altia.flexia.integracion.moduloexterno.melanbide89.vo.AccesoVO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

public class MeLanbide89DAO {

    //Logger
    private static Logger log = LogManager.getLogger(MeLanbide89DAO.class);
    //Instancia
    private static MeLanbide89DAO instance = null;

    // Constructor
    private MeLanbide89DAO() {

    }

    public static MeLanbide89DAO getInstance() {
        if (instance == null) {
            synchronized (MeLanbide89DAO.class) {
                instance = new MeLanbide89DAO();
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
    public List<AccesoVO> getListaAccesos(String numExp, int codOrganizacion, Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        List<AccesoVO> lista = new ArrayList<AccesoVO>();
        AccesoVO acceso = new AccesoVO();
        try {
            String query = null;
            query = "select * from " + ConfigurationParameter.getParameter(ConstantesMeLanbide89.MELANBIDE89_ACCESOS, ConstantesMeLanbide89.FICHERO_PROPIEDADES) + " ctr"
                    + " where ctr.num_exp ='" + numExp + "'"
                    + " order by ctr.id";
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                acceso = (AccesoVO) MeLanbide89MappingUtils.getInstance().map(rs, AccesoVO.class);
            
                lista.add(acceso);
                acceso = new AccesoVO();
            }
        } catch (Exception ex) {
            log.error("Se ha producido un error recuperando accesos ", ex);
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
    public AccesoVO getAccesoPorID(String id, Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        AccesoVO acceso = new AccesoVO();
        String query = null;
        try {
            query = "SELECT * FROM " + ConfigurationParameter.getParameter(ConstantesMeLanbide89.MELANBIDE89_ACCESOS, ConstantesMeLanbide89.FICHERO_PROPIEDADES) + " ctr"
                    + " WHERE ctr.ID=" + (id != null && !id.equals("") ? id : "null");
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                acceso = (AccesoVO) MeLanbide89MappingUtils.getInstance().map(rs, AccesoVO.class);
            }
        } catch (Exception ex) {
            log.error("Se ha producido un error recuperando una acceso : " + id, ex);
            throw new Exception(ex);
        } finally {
            if (st != null) {
                st.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        return acceso;
    }

    /**
     * 
     * @param nuevoAcceso
     * @param con
     * @return
     * @throws Exception 
     */
    public boolean crearAcceso(AccesoVO nuevoAcceso, Connection con) throws Exception {
        PreparedStatement ps = null;
        String query = "";
        int contBD = 1;
        boolean resultado = false;
        try {
            int id = recogerIDInsertar(ConfigurationParameter.getParameter(ConstantesMeLanbide89.SEQ_MELANBIDE89_ACCESOS, ConstantesMeLanbide89.FICHERO_PROPIEDADES), con);
            query = "INSERT INTO " + ConfigurationParameter.getParameter(ConstantesMeLanbide89.MELANBIDE89_ACCESOS, ConstantesMeLanbide89.FICHERO_PROPIEDADES)
                    + "(ID, NUM_EXP, NOMBRE, APELLIDO1, APELLIDO2, DNINIE)"
                    + " VALUES(?,?,?,?,?,?)";
            log.debug("sql = " + query);
            
            ps = con.prepareStatement(query);
            ps.setInt(contBD++, id);
            ps.setString(contBD++, nuevoAcceso.getNumExp());
            ps.setString(contBD++, nuevoAcceso.getNombre());
            ps.setString(contBD++, nuevoAcceso.getApellido1());
            ps.setString(contBD++, nuevoAcceso.getApellido2());
            ps.setString(contBD++, nuevoAcceso.getDninie());
            
            if (ps.executeUpdate() > 0) {
                resultado = true;
            } else {
                log.error("No Se ha podido guardar un nuevo acceso en " + ConfigurationParameter.getParameter(ConstantesMeLanbide89.MELANBIDE89_ACCESOS, ConstantesMeLanbide89.FICHERO_PROPIEDADES));
                return false;
            }

        } catch (Exception ex) {
            log.debug("Se ha producido un error al insertar un nuevo acceso" + ex.getMessage());
            throw new Exception(ex);
        } finally {
            if (ps != null) {
                ps.close();
            }
        }
        return resultado;
    }

    /**
     * 
     * @param accesoModif
     * @param con
     * @return
     * @throws Exception 
     */
    public boolean modificarAcceso(AccesoVO accesoModif, Connection con) throws Exception {
        PreparedStatement ps = null;
        String query = "";
        boolean resultado = false;
        int contBD = 1;
        try {
            query = "UPDATE " + ConfigurationParameter.getParameter(ConstantesMeLanbide89.MELANBIDE89_ACCESOS, ConstantesMeLanbide89.FICHERO_PROPIEDADES)
                    + " SET NOMBRE=?, APELLIDO1=?, APELLIDO2=?, DNINIE=?"
                    + " WHERE NUM_EXP = ? AND ID = ?";
            log.debug("sql = " + query);

            ps = con.prepareStatement(query);
            ps.setString(contBD++, accesoModif.getNombre());
            ps.setString(contBD++, accesoModif.getApellido1());
            ps.setString(contBD++, accesoModif.getApellido2());
            ps.setString(contBD++, accesoModif.getDninie());

            ps.setString(contBD++, accesoModif.getNumExp());
            ps.setInt(contBD++, accesoModif.getId());

            if (ps.executeUpdate() > 0) {
                resultado = true;
            } else {
                log.error("Se ha producido un error al Modificar un acceso - " + accesoModif.getId() + " - " + accesoModif.getNumExp());
                return false;
            }
        } catch (SQLException ex) {
            log.error("Se ha producido un error al Modificar un acceso - " + accesoModif.getId() + " - " + accesoModif.getNumExp() + " - " + ex);
            throw new Exception(ex);
        } finally {
            if (ps != null) {
                ps.close();
            }
        }
        return resultado;
    }
    
    /**
     * 
     * @param id
     * @param numExp
     * @param con
     * @return
     * @throws Exception 
     */
    public int eliminarAcceso(String id, String numExp, Connection con) throws Exception {
        String query = null;
        Statement st = null;
        int resultado = 0;
        try {
            query = "DELETE FROM " + ConfigurationParameter.getParameter(ConstantesMeLanbide89.MELANBIDE89_ACCESOS, ConstantesMeLanbide89.FICHERO_PROPIEDADES)
                    + " WHERE ID=" + (id != null && !id.equals("") ? id : "null");
            log.debug("sql = " + query);
            st = con.createStatement();
            resultado = st.executeUpdate(query);
            
        } catch (Exception ex) {
            log.error("Se ha producido un error Eliminando Contratación ID : " + id, ex);
            throw new Exception(ex);
        } finally {
            if (st != null) {
                st.close();
            }
        }
        return resultado;
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
        } catch (Exception ex) {
            log.error("Se ha producido un error recuperando el número de ID para inserción al llamar la secuencia " + sequence + " : ", ex);
            throw new Exception(ex);
        } finally {
            if (log.isDebugEnabled()) {
                log.debug("Procedemos a cerrar el statement y el resultset");
            }
            if (st != null) {
                st.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        return id;
    }
  
}

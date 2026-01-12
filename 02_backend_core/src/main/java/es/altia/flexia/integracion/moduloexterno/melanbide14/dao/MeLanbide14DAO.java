package es.altia.flexia.integracion.moduloexterno.melanbide14.dao;

import es.altia.flexia.integracion.moduloexterno.melanbide14.util.ConfigurationParameter;
import es.altia.flexia.integracion.moduloexterno.melanbide14.util.ConstantesMeLanbide14;
import es.altia.flexia.integracion.moduloexterno.melanbide14.util.MeLanbide14MappingUtils;
import es.altia.flexia.integracion.moduloexterno.melanbide14.vo.DesplegableAdmonLocalVO;
import es.altia.flexia.integracion.moduloexterno.melanbide14.vo.OperacionSolicitadaVO;
import es.altia.flexia.integracion.moduloexterno.melanbide14.vo.OperacionVO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

public class MeLanbide14DAO {

    //Logger
    private static Logger log = LogManager.getLogger(MeLanbide14DAO.class);
    private static final DateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
    //Instancia
    private static MeLanbide14DAO instance = null;

    // Constructor
    private MeLanbide14DAO() {

    }

    public static MeLanbide14DAO getInstance() {
        if (instance == null) {
            synchronized (MeLanbide14DAO.class) {
                instance = new MeLanbide14DAO();
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
    public List<OperacionVO> getListaOperaciones(final String numExp, int codOrganizacion, final Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        List<OperacionVO> lista = new ArrayList<OperacionVO>();
        OperacionVO operacion = new OperacionVO();
        try {
            String query = null;
            query = "select * from " + ConfigurationParameter.getParameter(ConstantesMeLanbide14.MELANBIDE14_OPERACIONES, ConstantesMeLanbide14.FICHERO_PROPIEDADES) + " OPR"
                    + " where OPR.num_exp ='" + numExp + "'"
                    + " order by OPR.id";
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                operacion = (OperacionVO) MeLanbide14MappingUtils.getInstance().map(rs, OperacionVO.class);

                lista.add(operacion);
                operacion = new OperacionVO();
            }
        } catch (Exception ex) {
            log.error("Se ha producido un error recuperando operaciones ", ex);
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
    public OperacionVO getOperacionPorID(final String id, final Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        OperacionVO operacion = new OperacionVO();
        String query = null;
        try {
            query = "SELECT * FROM " + ConfigurationParameter.getParameter(ConstantesMeLanbide14.MELANBIDE14_OPERACIONES, ConstantesMeLanbide14.FICHERO_PROPIEDADES) + " OPR"
                    + " WHERE OPR.ID=" + (id != null && !id.equals("") ? Integer.valueOf(id) : null);
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                operacion = (OperacionVO) MeLanbide14MappingUtils.getInstance().map(rs, OperacionVO.class);
            }
        } catch (Exception ex) {
            log.error("Se ha producido un error recuperando una operacion : " + id, ex);
            throw new Exception(ex);
        } finally {
            if (st != null) {
                st.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        return operacion;
    }

    /**
     *
     * @param nuevoAcceso
     * @param con
     * @return
     * @throws Exception
     */
    public boolean crearOperacion(final OperacionVO operacion, final Connection con) throws Exception {
        PreparedStatement ps = null;
        String query = "";
        int contBD = 1;
        boolean resultado = false;
        try {
            int id = recogerIDInsertar(ConfigurationParameter.getParameter(ConstantesMeLanbide14.SEQ_MELANBIDE14_OPERACIONES, ConstantesMeLanbide14.FICHERO_PROPIEDADES), con);
            query = "INSERT INTO " + ConfigurationParameter.getParameter(ConstantesMeLanbide14.MELANBIDE14_OPERACIONES, ConstantesMeLanbide14.FICHERO_PROPIEDADES)
                    + "(ID, NUM_EXP, NUMOPER, NOMBREOPER, PRIOROBJ, LINACT1, LINACT2, LINACT3, IMPOPER)"
                    + " VALUES(?,?,?,?,?,?,?,?,?)";
            log.debug("sql = " + query);

            ps = con.prepareStatement(query);
            ps.setInt(contBD++, id);
            ps.setString(contBD++, operacion.getNumExp());
            ps.setInt(contBD++, operacion.getNumOper());
            ps.setString(contBD++, operacion.getNombreOper());
            ps.setString(contBD++, operacion.getPrio());
            ps.setString(contBD++, operacion.getLin1());
            ps.setString(contBD++, operacion.getLin2());
            ps.setString(contBD++, operacion.getLin3());
            ps.setDouble(contBD++, operacion.getImpOper());

            if (ps.executeUpdate() > 0) {
                resultado = true;
            } else {
                log.error("No Se ha podido guardar una nueva operación en " + ConfigurationParameter.getParameter(ConstantesMeLanbide14.MELANBIDE14_OPERACIONES, ConstantesMeLanbide14.FICHERO_PROPIEDADES));
                return false;
            }

        } catch (Exception ex) {
            log.debug("Se ha producido un error al insertar una nueva operacion " + ex.getMessage());
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
    public boolean modificarOperacion(final OperacionVO operacionModif, final Connection con) throws Exception {
        PreparedStatement ps = null;
        String query = "";
        boolean resultado = false;
        int contBD = 1;
        try {
            query = "UPDATE " + ConfigurationParameter.getParameter(ConstantesMeLanbide14.MELANBIDE14_OPERACIONES, ConstantesMeLanbide14.FICHERO_PROPIEDADES)
                    + " SET NUMOPER=?, NOMBREOPER=?, PRIOROBJ=?, LINACT1=?, LINACT2=?, LINACT3=?, IMPOPER=?"
                    + " WHERE NUM_EXP = ? AND ID = ?";
            log.debug("sql = " + query);

            ps = con.prepareStatement(query);
            ps.setInt(contBD++, operacionModif.getNumOper());
            ps.setString(contBD++, operacionModif.getNombreOper());
            ps.setString(contBD++, operacionModif.getPrio());
            ps.setString(contBD++, operacionModif.getLin1());
            ps.setString(contBD++, operacionModif.getLin2());
            ps.setString(contBD++, operacionModif.getLin3());
            ps.setDouble(contBD++, operacionModif.getImpOper());
            ps.setString(contBD++, operacionModif.getNumExp());
            ps.setInt(contBD++, operacionModif.getId());

            if (ps.executeUpdate() > 0) {
                resultado = true;
            } else {
                log.error("Se ha producido un error al Modificar una operacion - " + operacionModif.getId() + " - " + operacionModif.getNumExp());
                return false;
            }
        } catch (SQLException ex) {
            log.error("Se ha producido un error al Modificar una operacion - " + operacionModif.getId() + " - " + operacionModif.getNumExp() + " - " + ex);
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
    public int eliminarOperacion(final String id, final String numExp, final Connection con) throws Exception {
        String query = null;
        Statement st = null;
        int resultado = 0;
        try {
            query = "DELETE FROM " + ConfigurationParameter.getParameter(ConstantesMeLanbide14.MELANBIDE14_OPERACIONES, ConstantesMeLanbide14.FICHERO_PROPIEDADES)
                    + " WHERE ID=" + (id != null && !id.equals("") ? Integer.valueOf(id) : null);
            log.debug("sql = " + query);
            st = con.createStatement();
            resultado = st.executeUpdate(query);

        } catch (Exception ex) {
            log.error("Se ha producido un error Eliminando Operación ID : " + id, ex);
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
     * @param numExp
     * @param con
     * @return
     * @throws Exception
     */
    public List<String> getEjerciciosSolicitados(String numExp, Connection con) throws Exception {
        log.info("getEjerciciosPresentados - " + numExp);
        List<String> listaEjercicios = new ArrayList<String>();
        PreparedStatement ps = null;
        ResultSet rs = null;
        String query = null;
        try {
            query = "select distinct(EJEOPERACION) from " + ConfigurationParameter.getParameter(ConstantesMeLanbide14.M14_OPE_PRESENTADAS, ConstantesMeLanbide14.FICHERO_PROPIEDADES)
                    + " where NUM_EXP = ? order by EJEOPERACION";
            log.debug("sql = " + query);
            int i = 1;
            ps = con.prepareStatement(query);
            ps.setString(i++, numExp);
            rs = ps.executeQuery();
            while (rs.next()) {
                listaEjercicios.add(rs.getString("EJEOPERACION"));
            }
        } catch (SQLException ex) {
            log.error("Se ha producido un error recuperando los EJERCICIOS Solicitados ", ex);
            throw new Exception(ex);
        } finally {
            if (ps != null) {
                ps.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        return listaEjercicios;
    }

    /**
     *
     * @param numExp
     * @param ejercicio
     * @param con
     * @return
     * @throws Exception
     */
    public List<OperacionSolicitadaVO> getOperacionesSolicitadasEjercicio(String numExp, String ejercicio, Connection con) throws Exception {
        log.info("getOperacionesSolicitadasEjercicio - " + numExp + " - Ejercicio: " + ejercicio);
        List<OperacionSolicitadaVO> listaOperaciones = new ArrayList<OperacionSolicitadaVO>();
        OperacionSolicitadaVO operacion = new OperacionSolicitadaVO();
        PreparedStatement ps = null;
        ResultSet rs = null;
        String query = null;
        try {
            query = "select OPS.*, PRI.DES_NOM DES_PRIORIDAD, OBJ.DES_NOM DES_OBJETIVO, TIP.DES_NOM DES_TIPOLOGIA, ENT.DES_NOM DES_ENTIDAD"
                    + " from " + ConfigurationParameter.getParameter(ConstantesMeLanbide14.M14_OPE_PRESENTADAS, ConstantesMeLanbide14.FICHERO_PROPIEDADES) + " OPS"
                    + " left join " + ConfigurationParameter.getParameter(ConstantesMeLanbide14.TABLA_VALORES_DESPLEGABLES, ConstantesMeLanbide14.FICHERO_PROPIEDADES) + " PRI "
                    + " on PRI.DES_COD = '" + ConfigurationParameter.getParameter(ConstantesMeLanbide14.COD_DES_PRIORIDAD, ConstantesMeLanbide14.FICHERO_PROPIEDADES) + "' and PRI.DES_VAL_COD = OPS.PRIORIDAD"
                    + " left join " + ConfigurationParameter.getParameter(ConstantesMeLanbide14.TABLA_VALORES_DESPLEGABLES, ConstantesMeLanbide14.FICHERO_PROPIEDADES) + " OBJ "
                    + " on OBJ.DES_COD = '" + ConfigurationParameter.getParameter(ConstantesMeLanbide14.COD_DES_OBJETIVO, ConstantesMeLanbide14.FICHERO_PROPIEDADES) + "' and OBJ.DES_VAL_COD = OPS.OBJETIVO"
                    + " left join " + ConfigurationParameter.getParameter(ConstantesMeLanbide14.TABLA_VALORES_DESPLEGABLES, ConstantesMeLanbide14.FICHERO_PROPIEDADES) + " TIP "
                    + " on TIP.DES_COD = '" + ConfigurationParameter.getParameter(ConstantesMeLanbide14.COD_DES_TIPOLOGIA, ConstantesMeLanbide14.FICHERO_PROPIEDADES) + "' and TIP.DES_VAL_COD = OPS.TIPOLOGIA"
                    + " left join " + ConfigurationParameter.getParameter(ConstantesMeLanbide14.TABLA_VALORES_DESPLEGABLES, ConstantesMeLanbide14.FICHERO_PROPIEDADES) + " ENT "
                    + " on ENT.DES_COD = '" + ConfigurationParameter.getParameter(ConstantesMeLanbide14.COD_DES_ENTIDAD, ConstantesMeLanbide14.FICHERO_PROPIEDADES) + "' and ENT.DES_VAL_COD = OPS.ENTIDAD"
                    + " where NUM_EXP = ? and EJEOPERACION = ?"
                    + " order by OPS.ID";

            log.debug("sql = " + query);
            int i = 1;
            ps = con.prepareStatement(query);
            ps.setString(i++, numExp);
            ps.setString(i++, ejercicio);

            rs = ps.executeQuery();
            while (rs.next()) {
                operacion = (OperacionSolicitadaVO) MeLanbide14MappingUtils.getInstance().map(rs, OperacionSolicitadaVO.class);
                listaOperaciones.add(operacion);
                operacion = new OperacionSolicitadaVO();
            }
        } catch (Exception ex) {
            log.error("Se ha producido un error recuperando las Operaciones Solicitadas ", ex);
            throw new Exception(ex);
        } finally {
            if (ps != null) {
                ps.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        return listaOperaciones;
    }

    /**
     *
     * @param id
     * @param con
     * @return el objeto OperacionSolicitadaVO
     * @throws Exception
     */
    public OperacionSolicitadaVO getOperacionSolocitadaPorId(String id, Connection con) throws Exception {
        log.info("getOperacionSolocitadaPorId - " + id);
        OperacionSolicitadaVO operacion = new OperacionSolicitadaVO();
        PreparedStatement ps = null;
        ResultSet rs = null;
        String query = null;
        try {
            query = "select OPS.*, PRI.DES_NOM DES_PRIORIDAD, OBJ.DES_NOM DES_OBJETIVO, TIP.DES_NOM DES_TIPOLOGIA, ENT.DES_NOM DES_ENTIDAD"
                    + " from " + ConfigurationParameter.getParameter(ConstantesMeLanbide14.M14_OPE_PRESENTADAS, ConstantesMeLanbide14.FICHERO_PROPIEDADES) + " OPS"
                    + " left join " + ConfigurationParameter.getParameter(ConstantesMeLanbide14.TABLA_VALORES_DESPLEGABLES, ConstantesMeLanbide14.FICHERO_PROPIEDADES) + " PRI "
                    + " on PRI.DES_COD = '" + ConfigurationParameter.getParameter(ConstantesMeLanbide14.COD_DES_PRIORIDAD, ConstantesMeLanbide14.FICHERO_PROPIEDADES) + "' and PRI.DES_VAL_COD = OPS.PRIORIDAD"
                    + " left join " + ConfigurationParameter.getParameter(ConstantesMeLanbide14.TABLA_VALORES_DESPLEGABLES, ConstantesMeLanbide14.FICHERO_PROPIEDADES) + " OBJ "
                    + " on OBJ.DES_COD = '" + ConfigurationParameter.getParameter(ConstantesMeLanbide14.COD_DES_OBJETIVO, ConstantesMeLanbide14.FICHERO_PROPIEDADES) + "' and OBJ.DES_VAL_COD = OPS.OBJETIVO"
                    + " left join " + ConfigurationParameter.getParameter(ConstantesMeLanbide14.TABLA_VALORES_DESPLEGABLES, ConstantesMeLanbide14.FICHERO_PROPIEDADES) + " TIP "
                    + " on TIP.DES_COD = '" + ConfigurationParameter.getParameter(ConstantesMeLanbide14.COD_DES_TIPOLOGIA, ConstantesMeLanbide14.FICHERO_PROPIEDADES) + "' and TIP.DES_VAL_COD = OPS.TIPOLOGIA"
                    + " left join " + ConfigurationParameter.getParameter(ConstantesMeLanbide14.TABLA_VALORES_DESPLEGABLES, ConstantesMeLanbide14.FICHERO_PROPIEDADES) + " ENT "
                    + " on ENT.DES_COD = '" + ConfigurationParameter.getParameter(ConstantesMeLanbide14.COD_DES_ENTIDAD, ConstantesMeLanbide14.FICHERO_PROPIEDADES) + "' and ENT.DES_VAL_COD = OPS.ENTIDAD"
                    + " where ID = ?";

            log.debug("sql = " + query);
            int i = 1;
            ps = con.prepareStatement(query);
            ps.setString(i++, id);
            rs = ps.executeQuery();
            if (rs.next()) {
                operacion = (OperacionSolicitadaVO) MeLanbide14MappingUtils.getInstance().map(rs, OperacionSolicitadaVO.class);
            }
        } catch (Exception e) {
        } finally {
            if (ps != null) {
                ps.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        return operacion;
    }

    public boolean nuevaOpeSolicitada(OperacionSolicitadaVO operacion, Connection con) throws Exception {
        log.info("nuevaOpeSolicitada");
        PreparedStatement ps = null;
        String query = null;
        try {
            int i = 1;
            int id = recogerIDInsertar(ConfigurationParameter.getParameter(ConstantesMeLanbide14.SEQ_M14_OPE_PRESENTADAS, ConstantesMeLanbide14.FICHERO_PROPIEDADES), con);
            query = "Insert into " + ConfigurationParameter.getParameter(ConstantesMeLanbide14.M14_OPE_PRESENTADAS, ConstantesMeLanbide14.FICHERO_PROPIEDADES)
                    + " (ID, NUM_EXP, EJEOPERACION, NUMOPEPRE, PRIORIDAD, OBJETIVO, TIPOLOGIA, FECINICIO, FECFIN, ENTIDAD, ORGANISMO)"
                    + " values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            ps = con.prepareStatement(query);
            ps.setInt(i++, id);
            ps.setString(i++, operacion.getNumExp());
            ps.setInt(i++, operacion.getEjeOperacion());
            ps.setInt(i++, operacion.getNumOpePre());
            ps.setString(i++, operacion.getPrioridad());
            ps.setString(i++, operacion.getObjetivo());
            ps.setString(i++, operacion.getTipologia());
            ps.setDate(i++, operacion.getFecInicio());
            ps.setDate(i++, operacion.getFecFin());
            ps.setString(i++, operacion.getEntidad());
            ps.setString(i++, operacion.getOrganismo());

            log.debug("sql = " + query);

            return ps.executeUpdate() > 0;
        } catch (Exception ex) {
            log.error("Se ha producido un error al insertar una nueva Operación SOLICITADA " + ex.getMessage());
            throw new Exception(ex);
        } finally {
            if (ps != null) {
                ps.close();
            }
        }
    }

    public boolean modificarOpeSolicitada(OperacionSolicitadaVO operacion, Connection con) throws Exception {
        log.info("modificarOpeSolicitada");
        PreparedStatement ps = null;
        String query = null;
        try {
            int i = 1;
            query = "update " + ConfigurationParameter.getParameter(ConstantesMeLanbide14.M14_OPE_PRESENTADAS, ConstantesMeLanbide14.FICHERO_PROPIEDADES)
                    + " set EJEOPERACION = ?, NUMOPEPRE = ?, PRIORIDAD = ?, OBJETIVO = ?, TIPOLOGIA = ?, FECINICIO = ?, FECFIN = ?, ENTIDAD = ?, ORGANISMO = ?"
                    + " where ID=? and NUM_EXP=?";
            ps = con.prepareStatement(query);
            ps.setInt(i++, operacion.getEjeOperacion());
            ps.setInt(i++, operacion.getNumOpePre());
            ps.setString(i++, operacion.getPrioridad());
            ps.setString(i++, operacion.getObjetivo());
            ps.setString(i++, operacion.getTipologia());
            ps.setDate(i++, operacion.getFecInicio());
            ps.setDate(i++, operacion.getFecFin());
            ps.setString(i++, operacion.getEntidad());
            ps.setString(i++, operacion.getOrganismo());
            ps.setInt(i++, operacion.getId());
            ps.setString(i++, operacion.getNumExp());
            log.debug("sql = " + query);
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            log.error("Se ha producido un error al modificar una Operación SOLICITADA " + ex.getMessage());
            throw new Exception(ex);
        } finally {
            if (ps != null) {
                ps.close();
            }
        }
    }

    public boolean eliminarOpeSolicitada(String id, Connection con) throws Exception {
        log.info("eliminarOpeSolicitada");
        PreparedStatement ps = null;
        String query = null;
        try {
            query = "DELETE FROM " + ConfigurationParameter.getParameter(ConstantesMeLanbide14.M14_OPE_PRESENTADAS, ConstantesMeLanbide14.FICHERO_PROPIEDADES)
                    + " WHERE ID = ?";
            log.debug("sql = " + query);
            ps = con.prepareStatement(query);
            ps.setString(1, id);
            log.debug("sql = " + query);
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            log.error("Se ha producido un error al modificar una Operación SOLICITADA " + ex.getMessage());
            throw new Exception(ex);
        } finally {
            if (ps != null) {
                ps.close();
            }
        }
    }

    public List<DesplegableAdmonLocalVO> getValoresE_DES_VAL(String des_cod, Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        List<DesplegableAdmonLocalVO> lista = new ArrayList<DesplegableAdmonLocalVO>();
        DesplegableAdmonLocalVO valoresDesplegable = new DesplegableAdmonLocalVO();
        try {
            String query = null;
            query = "SELECT * FROM " + ConfigurationParameter.getParameter(ConstantesMeLanbide14.TABLA_VALORES_DESPLEGABLES, ConstantesMeLanbide14.FICHERO_PROPIEDADES)
                    + " WHERE DES_COD='" + des_cod + "' order by DES_NOM";
            log.debug("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                valoresDesplegable = (DesplegableAdmonLocalVO) MeLanbide14MappingUtils.getInstance().map(rs, DesplegableAdmonLocalVO.class);
                lista.add(valoresDesplegable);
                valoresDesplegable = new DesplegableAdmonLocalVO();
            }
        } catch (Exception ex) {
            log.error("Se ha producido un error recuperando valores de desplegable : " + des_cod, ex);
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
     * @param sequence
     * @param con
     * @return
     * @throws Exception
     */
    private int recogerIDInsertar(final String sequence, final Connection con) throws Exception {
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

}

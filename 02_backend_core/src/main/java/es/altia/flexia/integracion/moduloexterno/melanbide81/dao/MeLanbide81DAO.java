package es.altia.flexia.integracion.moduloexterno.melanbide81.dao;

import es.altia.flexia.integracion.moduloexterno.melanbide81.util.ConfigurationParameter;
import es.altia.flexia.integracion.moduloexterno.melanbide81.util.ConstantesMeLanbide81;
import es.altia.flexia.integracion.moduloexterno.melanbide81.util.MeLanbide81MappingUtils;
import es.altia.flexia.integracion.moduloexterno.melanbide81.vo.ContratacionVO;
import es.altia.flexia.integracion.moduloexterno.melanbide81.vo.DesplegableVO;
import es.altia.flexia.integracion.moduloexterno.melanbide81.vo.ProyectoVO;
import es.altia.flexia.integracion.moduloexterno.melanbide81.vo.PuestoVO;
import es.altia.flexia.integracion.moduloexterno.melanbide81.vo.Tipo1VO;
import es.altia.flexia.integracion.moduloexterno.melanbide81.vo.Tipo2VO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

public class MeLanbide81DAO {

    //Logger
    private static final Logger log = LogManager.getLogger(MeLanbide81DAO.class);
    private final MeLanbide81MappingUtils m81Map = new MeLanbide81MappingUtils();

    //Instancia
    private static MeLanbide81DAO instance = null;

    public static MeLanbide81DAO getInstance() {
        if (instance == null) {
            synchronized (MeLanbide81DAO.class) {
                instance = new MeLanbide81DAO();
            }
        }
        return instance;
    }

    /**
     * Método que recoge el ID generado por la secuencia recibida
     *
     * @param sequence
     * @param con
     * @return NextVal de la secuencia
     * @throws Exception
     */
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
            log.error("Se ha producido un error recuperando El numero de ID para insercion al llamar la sequencia " + sequence + " : ", ex);
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
     * Método que recoge los valores del campo desplegable con código des_cod
     *
     * @param des_cod
     * @param con
     * @return lista de los códigos y valores del campo desplegable
     * @throws Exception
     */
    public List<DesplegableVO> getValoresE_Des(String des_cod, Connection con) throws Exception {
        List<DesplegableVO> lista = new ArrayList<DesplegableVO>();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            String query = null;
            query = "SELECT * FROM " + ConfigurationParameter.getParameter(ConstantesMeLanbide81.TABLA_VALORES_DESPLEGABLES, ConstantesMeLanbide81.FICHERO_PROPIEDADES)
                    + " WHERE DES_COD=?";
            log.debug("sql = " + query + " => " + des_cod);
            ps = con.prepareStatement(query);
            ps.setString(1, des_cod);
            rs = ps.executeQuery();
            while (rs.next()) {
                lista.add(m81Map.mapearDesplegableAdmonLocalVO(rs));
            }
        } catch (SQLException ex) {
            log.error("Se ha producido un error recuperando un valores de un desplegable : " + des_cod, ex);
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
     * Metodo que recupera los proyectos de un expediente
     *
     * @param numExp
     * @param con
     * @return lista de proyectos
     * @throws java.lang.Exception
     */
    public List<ProyectoVO> getListaProyectos(String numExp, Connection con) throws Exception {
        log.info("Entramos en getListaProyectos DAO - " + numExp);
        List<ProyectoVO> lista = new ArrayList<ProyectoVO>();
        PreparedStatement ps = null;
        ResultSet rs = null;
        String query = null;
        try {
            query = "SELECT * FROM " + ConfigurationParameter.getParameter(ConstantesMeLanbide81.TAB_PROYECTOS, ConstantesMeLanbide81.FICHERO_PROPIEDADES)
                    + " WHERE NUM_EXP = ? ORDER BY PRIORIDAD";
            log.debug("sql = " + query);
            ps = con.prepareStatement(query);
            ps.setString(1, numExp);
            rs = ps.executeQuery();
            while (rs.next()) {
                lista.add(m81Map.mapearProyecto(rs));
            }
        } catch (SQLException e) {
            log.error("Se ha producido un error en getListaProyectos ", e);
        }
        return lista;
    }

    /**
     * Metodo que recupera un proyecto por su PK
     *
     * @param numExp
     * @param id
     * @param con
     * @return el objeto proyecto
     * @throws java.lang.Exception
     */
    public ProyectoVO getProyectoPorID(String numExp, String id, Connection con) throws Exception {
        log.info("Entramos en getProyectoPorID DAO - " + numExp + " - " + id);
        PreparedStatement ps = null;
        ResultSet rs = null;
        ProyectoVO proyecto = new ProyectoVO();
        String query = null;
        try {
            query = "SELECT * FROM " + ConfigurationParameter.getParameter(ConstantesMeLanbide81.TAB_PROYECTOS, ConstantesMeLanbide81.FICHERO_PROPIEDADES)
                    + " WHERE NUM_EXP = ? AND ID = ?";
            log.debug("sql = " + query + " - id= " + id);
            ps = con.prepareStatement(query);
            ps.setString(1, numExp);
            ps.setString(2, id);
            rs = ps.executeQuery();
            while (rs.next()) {
                proyecto = m81Map.mapearProyecto(rs);
            }
        } catch (SQLException ex) {
            log.error("Se ha producido un error recuperando un Proyecto : " + id, ex);
            throw new Exception(ex);
        } finally {
            if (ps != null) {
                ps.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        return proyecto;
    }

    /**
     * Metodo que inserta un nuevo Proyecto
     *
     * @param nuevoProyecto
     * @param con
     * @return booleano resultado insert
     * @throws java.lang.Exception
     */
    public boolean crearProyecto(ProyectoVO nuevoProyecto, Connection con) throws Exception {
        log.info("Entramos en crearProyecto DAO - " + nuevoProyecto.getNumExp());
        PreparedStatement ps = null;
        String query = "";
        int contBD = 1;
        try {
            int id = recogerIDInsertar(ConfigurationParameter.getParameter(ConstantesMeLanbide81.SEQ_PROYECTOS, ConstantesMeLanbide81.FICHERO_PROPIEDADES), con);
            query = "INSERT INTO " + ConfigurationParameter.getParameter(ConstantesMeLanbide81.TAB_PROYECTOS, ConstantesMeLanbide81.FICHERO_PROPIEDADES)
                    + " (ID, NUM_EXP, PRIORIDAD, DENOM, ENTIDAD, TIPO, NUMFASES)"
                    + " VALUES(?,?,?,?,?,?,?)";
            log.debug("sql = " + query);

            ps = con.prepareStatement(query);
            ps.setInt(contBD++, id);
            ps.setString(contBD++, nuevoProyecto.getNumExp());
            ps.setInt(contBD++, nuevoProyecto.getPrioridad());
            ps.setString(contBD++, nuevoProyecto.getDenominacion());
            ps.setString(contBD++, nuevoProyecto.getEntidad());
            ps.setString(contBD++, nuevoProyecto.getTipoProyecto());
            ps.setInt(contBD++, nuevoProyecto.getFases());
            return ps.executeUpdate() > 0;
        } catch (Exception ex) {
            log.error("Se ha producido un error grabando un nuevo Proyecto : ", ex);
            throw new Exception(ex);
        } finally {
            if (ps != null) {
                ps.close();
            }
        }
    }

    /**
     * Metodo que actualiza un Proyecto
     *
     * @param datModif
     * @param con
     * @return booleano resultado update
     * @throws java.lang.Exception
     */
    public boolean modificarProyecto(ProyectoVO datModif, Connection con) throws Exception {
        log.info("Entramos en modificarProyecto DAO - " + datModif.getNumExp());
        PreparedStatement ps = null;
        String query = "";
        int contBD = 1;
        try {
            query = "UPDATE " + ConfigurationParameter.getParameter(ConstantesMeLanbide81.TAB_PROYECTOS, ConstantesMeLanbide81.FICHERO_PROPIEDADES)
                    + " SET PRIORIDAD=?, DENOM = ?, ENTIDAD = ?, TIPO = ? , NUMFASES = ?"
                    + " WHERE NUM_EXP = ? AND ID = ?";
            log.debug("sql = " + query);
            ps = con.prepareStatement(query);
            ps.setInt(contBD++, datModif.getPrioridad());
            ps.setString(contBD++, datModif.getDenominacion());
            ps.setString(contBD++, datModif.getEntidad());
            ps.setString(contBD++, datModif.getTipoProyecto());
            ps.setInt(contBD++, datModif.getFases());
            ps.setString(contBD++, datModif.getNumExp());
            ps.setInt(contBD++, datModif.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            log.error("Se ha producido un error grabando un nuevo Proyecto : ", ex);
            throw new Exception(ex);
        } finally {
            if (ps != null) {
                ps.close();
            }
        }
    }

    /**
     * Metodo que elimina un Proyecto
     *
     * @param numExp
     * @param id
     * @param con
     * @return booleano resultado delete
     * @throws java.lang.Exception
     */
    public boolean eliminarProyecto(String numExp, int id, Connection con) throws Exception {
        log.info("Entramos en eliminarProyecto DAO  " + numExp + " -  Id: " + id);
        PreparedStatement ps = null;
        String query = "";
        int contBD = 1;
        try {
            query = "DELETE FROM " + ConfigurationParameter.getParameter(ConstantesMeLanbide81.TAB_PUESTOS, ConstantesMeLanbide81.FICHERO_PROPIEDADES)
                    + " WHERE NUM_EXP = ? AND ID_PROYECTO= ?";
            log.debug("sql = " + query);
            ps = con.prepareStatement(query);
            ps.setString(contBD++, numExp);
            ps.setInt(contBD++, id);
            ps.executeUpdate();
            contBD = 1;
            query = "DELETE FROM " + ConfigurationParameter.getParameter(ConstantesMeLanbide81.TAB_CONTRATOS, ConstantesMeLanbide81.FICHERO_PROPIEDADES)
                    + " WHERE NUM_EXP = ? AND ID_PROYECTO= ?";
            log.debug("sql = " + query);
            ps = con.prepareStatement(query);
            ps.setString(contBD++, numExp);
            ps.setInt(contBD++, id);
            ps.executeUpdate();
            contBD = 1;
            query = "DELETE FROM " + ConfigurationParameter.getParameter(ConstantesMeLanbide81.TAB_PROYECTOS, ConstantesMeLanbide81.FICHERO_PROPIEDADES)
                    + " WHERE NUM_EXP = ? AND ID= ?";
            log.debug("sql = " + query);
            ps = con.prepareStatement(query);
            ps.setString(contBD++, numExp);
            ps.setInt(contBD++, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            log.error("Se ha producido un error eliminando el Proyecto con ID: " + id, ex);
            throw new Exception(ex);
        } finally {
            if (ps != null) {
                ps.close();
            }
        }
    }

    /**
     * Metodo que recupera todos los puestos de un proyecto
     *
     * @param numExp
     * @param idProyecto
     * @param con
     * @return lista de puestos
     * @throws java.lang.Exception
     */
    public List<PuestoVO> getListaPuestosxProyecto(String numExp, int idProyecto, Connection con) throws Exception {
        log.info("Entramos en getListaPuestosxProyecto DAO - " + numExp);
        List<PuestoVO> lista = new ArrayList<PuestoVO>();
        PreparedStatement ps = null;
        ResultSet rs = null;
        String query = null;
        try {
            query = "SELECT * FROM " + ConfigurationParameter.getParameter(ConstantesMeLanbide81.TAB_PUESTOS, ConstantesMeLanbide81.FICHERO_PROPIEDADES)
                    + " WHERE NUM_EXP = ? AND ID_PROYECTO = ? ORDER BY ID";
            log.debug("sql = " + query);
            ps = con.prepareStatement(query);
            ps.setString(1, numExp);
            ps.setInt(2, idProyecto);
            rs = ps.executeQuery();
            while (rs.next()) {
                lista.add(m81Map.mapearPuesto(rs));
            }
        } catch (SQLException e) {
            log.error("Se ha producido un error en getListaPuestosxProyecto ", e);
        }
        return lista;
    }

    /**
     * Metodo que recupera un puesto por su PK
     *
     * @param numExp
     * @param id
     * @param con
     * @return el objeto PuestoVO
     * @throws java.lang.Exception
     */
    public PuestoVO getPuestoPorID(String numExp, String id, Connection con) throws Exception {
        log.info("Entramos en getPuestoPorID DAO - " + numExp + " - " + id);
        PreparedStatement ps = null;
        ResultSet rs = null;
        PuestoVO puesto = new PuestoVO();
        String query = null;
        try {
            query = "SELECT * FROM " + ConfigurationParameter.getParameter(ConstantesMeLanbide81.TAB_PUESTOS, ConstantesMeLanbide81.FICHERO_PROPIEDADES)
                    + " WHERE NUM_EXP = ? AND ID = ?";
            log.debug("sql = " + query + " - id= " + id);
            ps = con.prepareStatement(query);
            ps.setString(1, numExp);
            ps.setString(2, id);
            rs = ps.executeQuery();
            while (rs.next()) {
                puesto = m81Map.mapearPuesto(rs);
            }
        } catch (SQLException ex) {
            log.error("Se ha producido un error recuperando un Proyecto : " + id, ex);
            throw new Exception(ex);
        } finally {
            if (ps != null) {
                ps.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        return puesto;
    }

    /**
     * Metodo que inserta un nuevo Puesto
     *
     * @param nuevoPuesto
     * @param con
     * @return booleano resultado insert
     * @throws java.lang.Exception
     */
    public boolean crearPuesto(PuestoVO nuevoPuesto, Connection con) throws Exception {
        log.info("Entramos en crearPuesto DAO - " + nuevoPuesto.getNumExp());
        PreparedStatement ps = null;
        String query = "";
        int contBD = 1;
        try {
            int id = recogerIDInsertar(ConfigurationParameter.getParameter(ConstantesMeLanbide81.SEQ_PUESTOS, ConstantesMeLanbide81.FICHERO_PROPIEDADES), con);
            query = "INSERT INTO " + ConfigurationParameter.getParameter(ConstantesMeLanbide81.TAB_PUESTOS, ConstantesMeLanbide81.FICHERO_PROPIEDADES)
                    + " (ID, ID_PROYECTO, NUM_EXP, DENOMPUESTO, DURESTIM, PORCJORN, NUMCONTRATOS, COSTEESTIMADO, SUBVENSOLIC, ID_PRIORIDAD_PROYECTO)"
                    + " VALUES(?,?,?,?,?,?,?,?,?,?)";
            log.debug("sql = " + query);

            ps = con.prepareStatement(query);
            ps.setInt(contBD++, id);
            ps.setInt(contBD++, nuevoPuesto.getIdProyecto());
            ps.setString(contBD++, nuevoPuesto.getNumExp());
            ps.setString(contBD++, nuevoPuesto.getDenominacion());
            ps.setDouble(contBD++, nuevoPuesto.getDuracion());
            ps.setDouble(contBD++, nuevoPuesto.getPorcJorn());
            ps.setInt(contBD++, nuevoPuesto.getNumContratos());
            ps.setDouble(contBD++, nuevoPuesto.getCoste());
            ps.setDouble(contBD++, nuevoPuesto.getSubvencion());
            ps.setInt(contBD++, nuevoPuesto.getIdPrioridadProyecto());
            return ps.executeUpdate() > 0;
        } catch (Exception ex) {
            log.error("Se ha producido un error grabando un nuevo PUESTO : ", ex);
            throw new Exception(ex);
        } finally {
            if (ps != null) {
                ps.close();
            }
        }
    }

    /**
     * Metodo que actualiza un Puesto
     *
     * @param datModif
     * @param con
     * @return booleano resultado update
     * @throws java.lang.Exception
     */
    public boolean modificarPuesto(PuestoVO datModif, Connection con) throws Exception {
        log.info("Entramos en modificarPuesto DAO - " + datModif.getNumExp());
        PreparedStatement ps = null;
        String query = "";
        int contBD = 1;
        try {
            query = "UPDATE " + ConfigurationParameter.getParameter(ConstantesMeLanbide81.TAB_PUESTOS, ConstantesMeLanbide81.FICHERO_PROPIEDADES)
                    + " SET DENOMPUESTO=?, DURESTIM = ?, PORCJORN = ?, NUMCONTRATOS = ? , COSTEESTIMADO = ?, SUBVENSOLIC = ?"
                    + " WHERE NUM_EXP = ? AND ID = ?";
            log.debug("sql = " + query);
            ps = con.prepareStatement(query);
            ps.setString(contBD++, datModif.getDenominacion());
            ps.setDouble(contBD++, datModif.getDuracion());
            ps.setDouble(contBD++, datModif.getPorcJorn());
            ps.setInt(contBD++, datModif.getNumContratos());
            ps.setDouble(contBD++, datModif.getCoste());
            ps.setDouble(contBD++, datModif.getSubvencion());
            ps.setString(contBD++, datModif.getNumExp());
            ps.setInt(contBD++, datModif.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            log.error("Se ha producido un error modificando un puesto : ", ex);
            throw new Exception(ex);
        } finally {
            if (ps != null) {
                ps.close();
            }
        }
    }

    /**
     * Metodo que elimina un Puesto
     *
     * @param numExp
     * @param id
     * @param con
     * @return booleano resultado delete
     * @throws java.lang.Exception
     */
    public boolean eliminarPuesto(String numExp, int id, Connection con) throws Exception {
        log.info("Entramos en eliminarPuesto DAO - " + numExp + " -  Id: " + id);
        PreparedStatement ps = null;
        String query = "";
        int contBD = 1;
        try {
            query = "DELETE FROM " + ConfigurationParameter.getParameter(ConstantesMeLanbide81.TAB_PUESTOS, ConstantesMeLanbide81.FICHERO_PROPIEDADES)
                    + " WHERE NUM_EXP = ? AND ID= ?";
            log.debug("sql = " + query);
            ps = con.prepareStatement(query);
            ps.setString(contBD++, numExp);
            ps.setInt(contBD++, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            log.error("Se ha producido un error eliminando el Puesto con ID: " + id, ex);
            throw new Exception(ex);
        } finally {
            if (ps != null) {
                ps.close();
            }
        }
    }

    /**
     * Metodo que recupera las contrataciones de un proyecto
     *
     * @param numExp
     * @param idProyecto
     * @param con
     * @return lista de contrataciones del proyecto
     * @throws java.lang.Exception
     */
    public List<ContratacionVO> getListaContratacionesxProyecto(String numExp, int idProyecto, Connection con) throws Exception {
        log.info("Entramos en getListaContratacionesxProyecto DAO - " + numExp);
        List<ContratacionVO> lista = new ArrayList<ContratacionVO>();
        PreparedStatement ps = null;
        ResultSet rs = null;
        String query = null;
        try {
            query = "SELECT * FROM " + ConfigurationParameter.getParameter(ConstantesMeLanbide81.TAB_CONTRATOS, ConstantesMeLanbide81.FICHERO_PROPIEDADES)
                    + " WHERE NUM_EXP = ? AND ID_PROYECTO = ? ORDER BY ID";
            log.debug("sql = " + query);
            ps = con.prepareStatement(query);
            ps.setString(1, numExp);
            ps.setInt(2, idProyecto);
            rs = ps.executeQuery();
            while (rs.next()) {
                lista.add(m81Map.mapearContratacion(rs));
            }
        } catch (SQLException e) {
            log.error("Se ha producido un error en getListaContratacionesxProyecto ", e);
        }
        return lista;
    }

    /**
     * Metodo que recupera una Contratacion por su PK
     *
     * @param numExp
     * @param id
     * @param con
     * @return el objeto ContratacionVO
     * @throws java.lang.Exception
     */
    public ContratacionVO getContratacionPorID(String numExp, String id, Connection con) throws Exception {
        log.info("Entramos en getContratacionPorID DAO - " + numExp + " - " + id);
        PreparedStatement ps = null;
        ResultSet rs = null;
        ContratacionVO puesto = new ContratacionVO();
        String query = null;
        try {
            query = "SELECT * FROM " + ConfigurationParameter.getParameter(ConstantesMeLanbide81.TAB_CONTRATOS, ConstantesMeLanbide81.FICHERO_PROPIEDADES)
                    + " WHERE NUM_EXP = ? AND ID = ?";
            log.debug("sql = " + query + " - id= " + id);
            ps = con.prepareStatement(query);
            ps.setString(1, numExp);
            ps.setString(2, id);
            rs = ps.executeQuery();
            while (rs.next()) {
                puesto = m81Map.mapearContratacion(rs);
            }
        } catch (SQLException ex) {
            log.error("Se ha producido un error recuperando una Contratación : " + id, ex);
            throw new Exception(ex);
        } finally {
            if (ps != null) {
                ps.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        return puesto;
    }

    /**
     * Metodo que inserta una nueva Contratacion
     *
     * @param nuevaContratacion
     * @param con
     * @return booleano resultado insert
     * @throws java.lang.Exception
     */
    public boolean crearContratacion(ContratacionVO nuevaContratacion, Connection con) throws Exception {
        log.info("Entramos en crearContratacion DAO - " + nuevaContratacion.getNumExp());
        PreparedStatement ps = null;
        String query = "";
        int contBD = 1;
        try {
            int id = recogerIDInsertar(ConfigurationParameter.getParameter(ConstantesMeLanbide81.SEQ_CONTRATOS, ConstantesMeLanbide81.FICHERO_PROPIEDADES), con);
            query = "INSERT INTO " + ConfigurationParameter.getParameter(ConstantesMeLanbide81.TAB_CONTRATOS, ConstantesMeLanbide81.FICHERO_PROPIEDADES)
                    + " (ID, ID_PROYECTO, NUM_EXP, TIPOPERSDESEM, DURACESTIMADAA, SEXO, PORCJORN, NUMCONTRPREV,SUBVENSOLIC, ID_PRIORIDAD_PROYECTO)"
                    + " VALUES(?,?,?,?,?,?,?,?,?,?)";
            log.debug("sql = " + query);

            ps = con.prepareStatement(query);
            ps.setInt(contBD++, id);
            ps.setInt(contBD++, nuevaContratacion.getIdProyecto());
            ps.setString(contBD++, nuevaContratacion.getNumExp());
            ps.setString(contBD++, nuevaContratacion.getTipoDesempleado());
            ps.setDouble(contBD++, nuevaContratacion.getDuracion());
            ps.setString(contBD++, nuevaContratacion.getSexo());
            ps.setDouble(contBD++, nuevaContratacion.getPorcJorn());
            ps.setInt(contBD++, nuevaContratacion.getNumContratos());
            ps.setDouble(contBD++, nuevaContratacion.getSubvencion());
            ps.setInt(contBD++, nuevaContratacion.getIdPrioridadProyecto());
            return ps.executeUpdate() > 0;
        } catch (Exception ex) {
            log.error("Se ha producido un error grabando una nueva Contratación : ", ex);
            throw new Exception(ex);
        } finally {
            if (ps != null) {
                ps.close();
            }
        }
    }

    /**
     * Metodo que actualiza una Contratacion
     *
     * @param datModif
     * @param con
     * @return booleano resultado update
     * @throws java.lang.Exception
     */
    public boolean modificarContratacion(ContratacionVO datModif, Connection con) throws Exception {
        log.info("Entramos en modificarContratacion DAO - " + datModif.getNumExp());
        PreparedStatement ps = null;
        String query = "";
        int contBD = 1;
        try {
            query = "UPDATE " + ConfigurationParameter.getParameter(ConstantesMeLanbide81.TAB_CONTRATOS, ConstantesMeLanbide81.FICHERO_PROPIEDADES)
                    + " SET TIPOPERSDESEM=?, DURACESTIMADAA = ?, SEXO = ?, PORCJORN = ?, NUMCONTRPREV = ? , SUBVENSOLIC = ?"
                    + " WHERE NUM_EXP = ? AND ID = ?";
            log.debug("sql = " + query);
            ps = con.prepareStatement(query);
            ps.setString(contBD++, datModif.getTipoDesempleado());
            ps.setDouble(contBD++, datModif.getDuracion());
            ps.setString(contBD++, datModif.getSexo());
            ps.setDouble(contBD++, datModif.getPorcJorn());
            ps.setInt(contBD++, datModif.getNumContratos());
            ps.setDouble(contBD++, datModif.getSubvencion());
            ps.setString(contBD++, datModif.getNumExp());
            ps.setInt(contBD++, datModif.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            log.error("Se ha producido un error modificando ua Contratación : ", ex);
            throw new Exception(ex);
        } finally {
            if (ps != null) {
                ps.close();
            }
        }
    }

    /**
     * Metodo que elimina una Contratacion
     *
     * @param numExpediente
     * @param id
     * @param con
     * @return booleano resultado delete
     * @throws java.lang.Exception
     */
    public boolean eliminarContratacion(String numExpediente, int id, Connection con) throws Exception {
        log.info("Entramos en eliminarContratacion DAO - " + numExpediente + " -  Id: " + id);
        PreparedStatement ps = null;
        String query = "";
        int contBD = 1;
        try {
            query = "DELETE FROM " + ConfigurationParameter.getParameter(ConstantesMeLanbide81.TAB_CONTRATOS, ConstantesMeLanbide81.FICHERO_PROPIEDADES)
                    + " WHERE NUM_EXP = ? AND ID= ?";
            log.debug("sql = " + query);
            ps = con.prepareStatement(query);
            ps.setString(contBD++, numExpediente);
            ps.setInt(contBD++, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            log.error("Se ha producido un error eliminando la Contratación con ID: " + id, ex);
            throw new Exception(ex);
        } finally {
            if (ps != null) {
                ps.close();
            }
        }
    }

    /**
     * Metodo que recupera un proyecto por su PK
     *
     * @param numExp
     * @param id
     * @param con
     * @return el objeto proyecto
     * @throws java.lang.Exception
     */
    public Tipo1VO getTipo1PorID(String numExp, String id, Connection con) throws Exception {
        log.info("Entramos en getTipo1PorID DAO - " + numExp + " - " + id);
        PreparedStatement ps = null;
        ResultSet rs = null;
        Tipo1VO tipo1 = new Tipo1VO();
        String query = null;
        try {
            query = "SELECT * FROM " + ConfigurationParameter.getParameter(ConstantesMeLanbide81.TAB_TIPO1, ConstantesMeLanbide81.FICHERO_PROPIEDADES)
                    + " WHERE NUM_EXP = ? AND ID = ?";
            log.debug("sql = " + query + " - id= " + id);
            ps = con.prepareStatement(query);
            ps.setString(1, numExp);
            ps.setString(2, id);
            rs = ps.executeQuery();
            while (rs.next()) {
                tipo1 = m81Map.mapearTipo1(rs);
            }
        } catch (SQLException ex) {
            log.error("Se ha producido un error al recuperar un tipo1 : " + id, ex);
            throw new Exception(ex);
        } finally {
            if (ps != null) {
                ps.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        return tipo1;
    }

    /**
     * Metodo que inserta un nuevo Tipo1
     *
     * @param nuevoTipo1
     * @param con
     * @return booleano resultado insert
     * @throws java.lang.Exception
     */
    public boolean crearNuevoTipo1(Tipo1VO nuevoTipo1, Connection con) throws Exception {
        log.info("Entramos en crearPuesto DAO - " + nuevoTipo1.getNumExp());
        PreparedStatement ps = null;
        String query = "";
        int contBD = 1;
        try {
            int id = recogerIDInsertar(ConfigurationParameter.getParameter(ConstantesMeLanbide81.SEQ_TIPO1, ConstantesMeLanbide81.FICHERO_PROPIEDADES), con);
            query = "INSERT INTO " + ConfigurationParameter.getParameter(ConstantesMeLanbide81.TAB_TIPO1, ConstantesMeLanbide81.FICHERO_PROPIEDADES)
                    + " (ID, NUM_EXP, ENTBENE, ENTCONTRA, CIF, CCC, DENOMPROY, DENOMPUESTO, NOMBRE, APELLIDO1, APELLIDO2, DNI,  NAF, FECNACIMIENTO, SEXO, GRUPOCOT, FECINICIO, FECFIN, PORCJORN,DURCONTRATO, EDAD, MUNICIPIO, COSTESAL, COSTESS, COSTETOTAL, INSCRITA, CERTINTER, SUBCONCEDIDA, PAGO1, SUBLIQUIDADA, PAGO2, OBSERVACIONES)"
                    + " VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            log.debug("sql = " + query);

            ps = con.prepareStatement(query);
            ps.setInt(contBD++, id);
            ps.setString(contBD++, nuevoTipo1.getNumExp());
            ps.setString(contBD++, nuevoTipo1.getEntbene());
            ps.setString(contBD++, nuevoTipo1.getEntcontra());
            ps.setString(contBD++, nuevoTipo1.getCif());
            ps.setString(contBD++, nuevoTipo1.getCcc());
            ps.setString(contBD++, nuevoTipo1.getDenomproy());
            ps.setString(contBD++, nuevoTipo1.getDenompuesto());
            ps.setString(contBD++, nuevoTipo1.getNombre());
            ps.setString(contBD++, nuevoTipo1.getApellido1());
            ps.setString(contBD++, nuevoTipo1.getApellido2());
            ps.setString(contBD++, nuevoTipo1.getDni());
            ps.setString(contBD++, nuevoTipo1.getNaf());
            ps.setDate(contBD++, nuevoTipo1.getFecnacimiento());
            ps.setString(contBD++, nuevoTipo1.getSexo());
            ps.setString(contBD++, nuevoTipo1.getGrupocot());
            ps.setDate(contBD++, nuevoTipo1.getFecinicio());
            ps.setDate(contBD++, nuevoTipo1.getFecfin());
            ps.setDouble(contBD++, nuevoTipo1.getPorcJorn());
            ps.setString(contBD++, nuevoTipo1.getDurcontrato());
            ps.setInt(contBD++, nuevoTipo1.getEdad());
            ps.setString(contBD++, nuevoTipo1.getMunicipio());
            ps.setDouble(contBD++, nuevoTipo1.getCostesal());
            ps.setDouble(contBD++, nuevoTipo1.getCostess());
            ps.setDouble(contBD++, nuevoTipo1.getCostetotal());
            ps.setString(contBD++, nuevoTipo1.getInscrita());
            ps.setString(contBD++, nuevoTipo1.getCertinter());
            ps.setDouble(contBD++, nuevoTipo1.getSubconcedida());
            ps.setDouble(contBD++, nuevoTipo1.getPago1());
            ps.setDouble(contBD++, nuevoTipo1.getSubliquidada());
            ps.setDouble(contBD++, nuevoTipo1.getPago2());
            ps.setString(contBD++, nuevoTipo1.getObservaciones());

            return ps.executeUpdate() > 0;
        } catch (Exception ex) {
            log.error("Se ha producido un error grabando un nuevo TIPO1 : ", ex);
            throw new Exception(ex);
        } finally {
            if (ps != null) {
                ps.close();
            }
        }
    }

    /**
     * Metodo que actualiza un Tipo1
     *
     * @param datModif
     * @param con
     * @return booleano resultado update
     * @throws java.lang.Exception
     */
    public boolean modificarTipo1(Tipo1VO datModif, Connection con) throws Exception {
        log.info("Entramos en modificarTipo1 DAO - " + datModif.getNumExp());
        PreparedStatement ps = null;
        String query = "";
        int contBD = 1;
        try {
            query = "UPDATE " + ConfigurationParameter.getParameter(ConstantesMeLanbide81.TAB_TIPO1, ConstantesMeLanbide81.FICHERO_PROPIEDADES)
                    + " SET ENTBENE=? , ENTCONTRA=?, CIF=?, CCC=?, DENOMPROY=?, DENOMPUESTO=?, NOMBRE=?, APELLIDO1=?, APELLIDO2=?, DNI=?,  NAF=?, FECNACIMIENTO=?, SEXO=?, GRUPOCOT=?, FECINICIO=?, FECFIN=?, PORCJORN=?,DURCONTRATO=?, EDAD=?, MUNICIPIO=?, COSTESAL=?, COSTESS=?, COSTETOTAL=?, INSCRITA=?, CERTINTER=?, SUBCONCEDIDA=?, PAGO1=?, SUBLIQUIDADA=?, PAGO2=?, OBSERVACIONES=?"
                    + " WHERE NUM_EXP = ? AND ID = ?";
            log.debug("sql = " + query);
            ps = con.prepareStatement(query);
            ps.setString(contBD++, datModif.getEntbene());
            ps.setString(contBD++, datModif.getEntcontra());
            ps.setString(contBD++, datModif.getCif());
            ps.setString(contBD++, datModif.getCcc());
            ps.setString(contBD++, datModif.getDenomproy());
            ps.setString(contBD++, datModif.getDenompuesto());
            ps.setString(contBD++, datModif.getNombre());
            ps.setString(contBD++, datModif.getApellido1());
            ps.setString(contBD++, datModif.getApellido2());
            ps.setString(contBD++, datModif.getDni());
            ps.setString(contBD++, datModif.getNaf());
            ps.setDate(contBD++, datModif.getFecnacimiento());
            ps.setString(contBD++, datModif.getSexo());
            ps.setString(contBD++, datModif.getGrupocot());
            ps.setDate(contBD++, datModif.getFecinicio());
            ps.setDate(contBD++, datModif.getFecfin());
            ps.setDouble(contBD++, datModif.getPorcJorn());
            ps.setString(contBD++, datModif.getDurcontrato());
            ps.setInt(contBD++, datModif.getEdad());
            ps.setString(contBD++, datModif.getMunicipio());
            ps.setDouble(contBD++, datModif.getCostesal());
            ps.setDouble(contBD++, datModif.getCostess());
            ps.setDouble(contBD++, datModif.getCostetotal());
            ps.setString(contBD++, datModif.getInscrita());
            ps.setString(contBD++, datModif.getCertinter());
            ps.setDouble(contBD++, datModif.getSubconcedida());
            ps.setDouble(contBD++, datModif.getPago1());
            ps.setDouble(contBD++, datModif.getSubliquidada());
            ps.setDouble(contBD++, datModif.getPago2());
            ps.setString(contBD++, datModif.getObservaciones());
            ps.setString(contBD++, datModif.getNumExp());
            ps.setInt(contBD++, datModif.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            log.error("Se ha producido un error modificando un puesto : ", ex);
            throw new Exception(ex);
        } finally {
             if (ps != null) {
                ps.close();
            }
        }
    }

    /**
     * Metodo que elimina un Tipo1
     *
     * @param numExp
     * @param id
     * @param con
     * @return booleano resultado delete
     * @throws java.lang.Exception
     */
    public boolean eliminarTipo1(String numExp, int id, Connection con) throws Exception {
        log.info("Entramos en eliminarPuesto DAO - " + numExp + " -  Id: " + id);
        PreparedStatement ps = null;
        String query = "";
        int contBD = 1;
        try {
            query = "DELETE FROM " + ConfigurationParameter.getParameter(ConstantesMeLanbide81.TAB_TIPO1, ConstantesMeLanbide81.FICHERO_PROPIEDADES)
                    + " WHERE NUM_EXP = ? AND ID= ?";
            log.debug("sql = " + query);
            ps = con.prepareStatement(query);
            ps.setString(contBD++, numExp);
            ps.setInt(contBD++, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            log.error("Se ha producido un error eliminando el Puesto con ID: " + id, ex);
            throw new Exception(ex);
        } finally {
            if (ps != null) {
                ps.close();
            }
        }
    }

    /**
     * Metodo que recupera todos los datos de Tipo1
     *
     * @param numExp
     * @param con
     * @return lista de tipo1
     * @throws java.lang.Exception
     */
    public List<Tipo1VO> getListaTipo1(String numExp,Connection con) throws Exception {
        log.info("Entramos en getListaTipo1 DAO - " + numExp);
        List<Tipo1VO> lista = new ArrayList<Tipo1VO>();
        Tipo1VO tipo1 = new Tipo1VO();
        PreparedStatement ps = null;
        ResultSet rs = null;
        String query = null;
        try {
            query = "SELECT * FROM " + ConfigurationParameter.getParameter(ConstantesMeLanbide81.TAB_TIPO1, ConstantesMeLanbide81.FICHERO_PROPIEDADES)
                    + " WHERE NUM_EXP = ?"
                    + " ORDER BY ID";
            log.debug("sql = " + query);
            ps = con.prepareStatement(query);
            ps.setString(1, numExp);
            rs = ps.executeQuery();
            while (rs.next()) {
                tipo1 = (Tipo1VO) m81Map.mapearTipo1(rs);
                //Cargamos el el request los valores  de los desplegables
                lista.add(tipo1);
            }
        } catch (SQLException e) {
            log.error("Se ha producido un error en getListaTipo1 ", e);
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
     * Metodo que recupera un proyecto por su PK
     *
     * @param numExp
     * @param id
     * @param con
     * @return el objeto proyecto
     * @throws java.lang.Exception
     */
    public Tipo2VO getTipo2PorID(String numExp, String id, Connection con) throws Exception {
        log.info("Entramos en getTipo2PorID DAO - " + numExp + " - " + id);
        PreparedStatement ps = null;
        ResultSet rs = null;
        Tipo2VO tipo2 = new Tipo2VO();
        String query = null;
        try {
            query = "SELECT * FROM " + ConfigurationParameter.getParameter(ConstantesMeLanbide81.TAB_TIPO2, ConstantesMeLanbide81.FICHERO_PROPIEDADES)
                    + " WHERE NUM_EXP = ? AND ID = ?";
            log.debug("sql = " + query + " - id= " + id);
            ps = con.prepareStatement(query);
            ps.setString(1, numExp);
            ps.setString(2, id);
            rs = ps.executeQuery();
            while (rs.next()) {
                tipo2 = m81Map.mapearTipo2(rs);
            }
        } catch (SQLException ex) {
            log.error("Se ha producido un error al recuperar un tipo2 : " + id, ex);
            throw new Exception(ex);
        } finally {
            if (ps != null) {
                ps.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        return tipo2;
    }

    /**
     * Metodo que inserta un nuevo Tipo2
     *
     * @param nuevoTipo2
     * @param con
     * @return booleano resultado insert
     * @throws java.lang.Exception
     */
    public boolean crearNuevoTipo2(Tipo2VO nuevoTipo2, Connection con) throws Exception {
        log.info("Entramos en crearPuesto DAO - " + nuevoTipo2.getNumExp());
        PreparedStatement ps = null;
        String query = "";
        int contBD = 1;
        try {
            int id = recogerIDInsertar(ConfigurationParameter.getParameter(ConstantesMeLanbide81.SEQ_TIPO2, ConstantesMeLanbide81.FICHERO_PROPIEDADES), con);
            query = "INSERT INTO " + ConfigurationParameter.getParameter(ConstantesMeLanbide81.TAB_TIPO2, ConstantesMeLanbide81.FICHERO_PROPIEDADES)
                    + " (ID, NUM_EXP, ENTBENE, EMPCONTRA, CIF, CCC, NOMBRE, APELLIDO1, APELLIDO2, DNI,  NAF, FECNACIMIENTO, SEXO, GRUPOCOT, FECINICIO, FECFIN, PORCJORN, DURCONTRATO, EDAD, MUNICIPIO, COSTESAL, COSTESS, COSTETOTAL, INSCRITA, CERTINTER, SUBCONCEDIDA, PAGO1, SUBLIQUIDADA, PAGO2, OBSERVACIONES, TIPOCONTRATO, COLECTIVO)"
                    + " VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            log.debug("sql = " + query);

            ps = con.prepareStatement(query);
            ps.setInt(contBD++, id);
            ps.setString(contBD++, nuevoTipo2.getNumExp());
            ps.setString(contBD++, nuevoTipo2.getEntbene());
            ps.setString(contBD++, nuevoTipo2.getEmpcontra());
            ps.setString(contBD++, nuevoTipo2.getCif());
            ps.setString(contBD++, nuevoTipo2.getCcc());
            ps.setString(contBD++, nuevoTipo2.getNombre());
            ps.setString(contBD++, nuevoTipo2.getApellido1());
            ps.setString(contBD++, nuevoTipo2.getApellido2());
            ps.setString(contBD++, nuevoTipo2.getDni());
            ps.setString(contBD++, nuevoTipo2.getNaf());
            ps.setDate(contBD++, nuevoTipo2.getFecnacimiento());
            ps.setString(contBD++, nuevoTipo2.getSexo());
            ps.setString(contBD++, nuevoTipo2.getGrupocot());
            ps.setDate(contBD++, nuevoTipo2.getFecinicio());
            ps.setDate(contBD++, nuevoTipo2.getFecfin());
            ps.setDouble(contBD++, nuevoTipo2.getPorcJorn());
            ps.setString(contBD++, nuevoTipo2.getDurcontrato());
            ps.setInt(contBD++, nuevoTipo2.getEdad());
            ps.setString(contBD++, nuevoTipo2.getMunicipio());
            ps.setDouble(contBD++, nuevoTipo2.getCostesal());
            ps.setDouble(contBD++, nuevoTipo2.getCostess());
            ps.setDouble(contBD++, nuevoTipo2.getCostetotal());
            ps.setString(contBD++, nuevoTipo2.getInscrita());
            ps.setString(contBD++, nuevoTipo2.getCertinter());
            ps.setDouble(contBD++, nuevoTipo2.getSubconcedida());
            ps.setDouble(contBD++, nuevoTipo2.getPago1());
            ps.setDouble(contBD++, nuevoTipo2.getSubliquidada());
            ps.setDouble(contBD++, nuevoTipo2.getPago2());
            ps.setString(contBD++, nuevoTipo2.getObservaciones());
            ps.setString(contBD++, nuevoTipo2.getTipocontrato());
            ps.setString(contBD++, nuevoTipo2.getColectivo());

            return ps.executeUpdate() > 0;
        } catch (Exception ex) {
            log.error("Se ha producido un error grabando un nuevo PUESTO : ", ex);
            throw new Exception(ex);
        } finally {
            if (ps != null) {
                ps.close();
            }
        }
    }

    /**
     * Metodo que actualiza un Tipo2
     *
     * @param datModif
     * @param con
     * @return booleano resultado update
     * @throws java.lang.Exception
     */
    public boolean modificarTipo2(Tipo2VO datModif, Connection con) throws Exception {
        log.info("Entramos en modificarTipo2 DAO - " + datModif.getNumExp());
        PreparedStatement ps = null;
        String query = "";
        int contBD = 1;
        try {
            query = "UPDATE " + ConfigurationParameter.getParameter(ConstantesMeLanbide81.TAB_TIPO2, ConstantesMeLanbide81.FICHERO_PROPIEDADES)
                    + " SET ENTBENE=? , EMPCONTRA=?, CIF=?, CCC=?, NOMBRE=?, APELLIDO1=?, APELLIDO2=?, DNI=?,  NAF=?, FECNACIMIENTO=?, SEXO=?, GRUPOCOT=?, FECINICIO=?, FECFIN=?, "
                    + " PORCJORN=?,DURCONTRATO=?, EDAD=?, MUNICIPIO=?, COSTESAL=?, COSTESS=?, COSTETOTAL=?, INSCRITA=?, CERTINTER=?, SUBCONCEDIDA=?, PAGO1=?, SUBLIQUIDADA=?, PAGO2=?, OBSERVACIONES=?, TIPOCONTRATO=?, COLECTIVO=? "
                    + " WHERE NUM_EXP = ? AND ID = ?";
            log.debug("sql = " + query);
            ps = con.prepareStatement(query);
            ps.setString(contBD++, datModif.getEntbene());
            ps.setString(contBD++, datModif.getEmpcontra());
            ps.setString(contBD++, datModif.getCif());
            ps.setString(contBD++, datModif.getCcc());
            ps.setString(contBD++, datModif.getNombre());
            ps.setString(contBD++, datModif.getApellido1());
            ps.setString(contBD++, datModif.getApellido2());
            ps.setString(contBD++, datModif.getDni());
            ps.setString(contBD++, datModif.getNaf());
            ps.setDate(contBD++, datModif.getFecnacimiento());
            ps.setString(contBD++, datModif.getSexo());
            ps.setString(contBD++, datModif.getGrupocot());
            ps.setDate(contBD++, datModif.getFecinicio());
            ps.setDate(contBD++, datModif.getFecfin());
            ps.setDouble(contBD++, datModif.getPorcJorn());
            ps.setString(contBD++, datModif.getDurcontrato());
            ps.setInt(contBD++, datModif.getEdad());
            ps.setString(contBD++, datModif.getMunicipio());
            ps.setDouble(contBD++, datModif.getCostesal());
            ps.setDouble(contBD++, datModif.getCostess());
            ps.setDouble(contBD++, datModif.getCostetotal());
            ps.setString(contBD++, datModif.getInscrita());
            ps.setString(contBD++, datModif.getCertinter());
            ps.setDouble(contBD++, datModif.getSubconcedida());
            ps.setDouble(contBD++, datModif.getPago1());
            ps.setDouble(contBD++, datModif.getSubliquidada());
            ps.setDouble(contBD++, datModif.getPago2());
            ps.setString(contBD++, datModif.getObservaciones());
            ps.setString(contBD++, datModif.getTipocontrato());
            ps.setString(contBD++, datModif.getColectivo());
            ps.setString(contBD++, datModif.getNumExp());
            ps.setInt(contBD++, datModif.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            log.error("Se ha producido un error modificando un puesto : ", ex);
            throw new Exception(ex);
        } finally {
            if (ps != null) {
                ps.close();
            }
        }
    }

    /**
     * Metodo que elimina un Tipo2
     *
     * @param numExpediente
     * @param id
     * @param con
     * @return booleano resultado delete
     * @throws java.lang.Exception
     */
    public boolean eliminarTipo2(String numExpediente, int id, Connection con) throws Exception {
        log.info("Entramos en eliminarPuesto DAO - " + numExpediente + " -  Id: " + id);
        PreparedStatement ps = null;
        String query = "";
        int contBD = 1;
        try {
            query = "DELETE FROM " + ConfigurationParameter.getParameter(ConstantesMeLanbide81.TAB_TIPO2, ConstantesMeLanbide81.FICHERO_PROPIEDADES)
                    + " WHERE NUM_EXP = ? AND ID= ?";
            log.debug("sql = " + query);
            ps = con.prepareStatement(query);
            ps.setString(contBD++, numExpediente);
            ps.setInt(contBD++, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            log.error("Se ha producido un error eliminando el Puesto con ID: " + id, ex);
            throw new Exception(ex);
        } finally {
            if (ps != null) {
                ps.close();
            }
        }
    }

    /**
     * Metodo que recupera todos los datos de Tipo2
     *
     * @param numExp
     * @param con
     * @return lista de tipo2
     * @throws java.lang.Exception
     */
    public List<Tipo2VO> getListaTipo2(String numExp,Connection con) throws Exception {
        log.info("Entramos en getListaTipo2 DAO - " + numExp);
        List<Tipo2VO> lista = new ArrayList<Tipo2VO>();
        Tipo2VO tipo2 = new Tipo2VO();
        PreparedStatement ps = null;
        ResultSet rs = null;
        String query = null;
        try {
            query = "SELECT * FROM " + ConfigurationParameter.getParameter(ConstantesMeLanbide81.TAB_TIPO2, ConstantesMeLanbide81.FICHERO_PROPIEDADES)
                    + " WHERE NUM_EXP = ?"
                    + " ORDER BY ID";
            log.debug("sql = " + query);
            ps = con.prepareStatement(query);
            ps.setString(1, numExp);
            rs = ps.executeQuery();
            while (rs.next()) {
                tipo2 = (Tipo2VO) m81Map.mapearTipo2(rs);
                //Cargamos el el request los valores  de los desplegables
                lista.add(tipo2);
            }
        } catch (SQLException e) {
            log.error("Se ha producido un error en getListaTipo2 ", e);
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

}

package es.altia.flexia.integracion.moduloexterno.melanbide15.dao;

import es.altia.flexia.integracion.moduloexterno.melanbide15.util.ConfigurationParameter;
import es.altia.flexia.integracion.moduloexterno.melanbide15.util.ConstantesMeLanbide15;
import es.altia.flexia.integracion.moduloexterno.melanbide15.util.MeLanbide15MappingUtils;
import es.altia.flexia.integracion.moduloexterno.melanbide15.vo.ContratacionVO;
import es.altia.flexia.integracion.moduloexterno.melanbide15.vo.IdentidadVO;
import es.altia.flexia.integracion.moduloexterno.melanbide15.vo.DesplegableVO;
import es.altia.flexia.integracion.moduloexterno.melanbide15.vo.FormacionVO;
import es.altia.flexia.integracion.moduloexterno.melanbide15.vo.OrientacionVO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

public class MeLanbide15DAO {

    //Logger
    private static final Logger log = LogManager.getLogger(MeLanbide15DAO.class);

    //Instancia
    private static MeLanbide15DAO instance = null;

    public static MeLanbide15DAO getInstance() {
        if (instance == null) {
            synchronized (MeLanbide15DAO.class) {
                instance = new MeLanbide15DAO();
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

    public List<IdentidadVO> getListaIdentidad(String numExp, int codOrganizacion, Connection con) throws Exception {
        log.info("Entramos en getListaIdentidad DAO - " + numExp);
        Statement st = null;
        ResultSet rs = null;
        List<IdentidadVO> lista = new ArrayList<IdentidadVO>();
        IdentidadVO identidad = null;

        try {
            String query = "SELECT * FROM " + ConfigurationParameter.getParameter(ConstantesMeLanbide15.MELANBIDE15_IDENTIDAD, ConstantesMeLanbide15.FICHERO_PROPIEDADES)
                    + " WHERE NUM_EXP ='" + numExp + "'"
                    + " ORDER BY ID";
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }

            st = con.createStatement();
            rs = st.executeQuery(query);

            while (rs.next()) {
                identidad = (IdentidadVO) MeLanbide15MappingUtils.getInstance().map(rs, IdentidadVO.class);
                //Cargamos en el request los valores de los desplegables
                lista.add(identidad);
            }
        } catch (Exception ex) {
            log.error("Se ha producido un error recuperando identidad ", ex);
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

    public IdentidadVO getIdentidadPorID(String id, Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        IdentidadVO identidad = new IdentidadVO();
        try {
            String query = null;
            query = "SELECT * FROM " + ConfigurationParameter.getParameter(ConstantesMeLanbide15.MELANBIDE15_IDENTIDAD, ConstantesMeLanbide15.FICHERO_PROPIEDADES)
                    + " WHERE ID=" + (id != null && !id.equals("") ? id : "null");
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                identidad = (IdentidadVO) MeLanbide15MappingUtils.getInstance().map(rs, IdentidadVO.class);
            }
        } catch (Exception ex) {
            log.error("Se ha producido un error recuperando una identidad : " + id, ex);
            throw new Exception(ex);
        } finally {
            if (st != null) {
                st.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        return identidad;
    }

    public boolean eliminarIdentidad(int id, String numExp, Connection con) throws Exception {
        PreparedStatement ps = null;
        String query = null;

        try {
            // Generar la consulta SQL
            query = "DELETE FROM " + ConfigurationParameter.getParameter(ConstantesMeLanbide15.MELANBIDE15_IDENTIDAD, ConstantesMeLanbide15.FICHERO_PROPIEDADES)
                    + " WHERE ID = ? AND NUM_EXP = ?";

            // Preparar el statement
            ps = con.prepareStatement(query);

            // Configurar los parámetros
            ps.setInt(1, id);
            ps.setString(2, numExp);

            log.debug("Ejecutando consulta SQL: " + query + " [ID=" + id + ", NUM_EXP=" + numExp + "]");

            // Ejecutar la consulta y devolver el resultado
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            log.error("Error al eliminar identidad con ID: " + id + " y NUM_EXP: " + numExp, ex);
            throw new Exception("Error en la base de datos al eliminar identidad.", ex);
        } finally {
            // Cerrar el PreparedStatement
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException e) {
                    log.error("Error al cerrar el PreparedStatement: " + e.getMessage(), e);
                }
            }
        }
    }

    public boolean crearNuevaIdentidad(IdentidadVO nuevaIdentidad, Connection con) throws Exception {
        PreparedStatement ps = null;
        String query = null;
        int contBD = 1;
        try {
            int id = recogerIDInsertar(ConfigurationParameter.getParameter(ConstantesMeLanbide15.SEQ_M15_IDENTIDAD, ConstantesMeLanbide15.FICHERO_PROPIEDADES), con);
            // Consulta SQL para insertar una nueva identidad
            query = "INSERT INTO " + ConfigurationParameter.getParameter(ConstantesMeLanbide15.MELANBIDE15_IDENTIDAD, ConstantesMeLanbide15.FICHERO_PROPIEDADES)
                    + " (ID, NUM_EXP, DNI, NUM_IDEN, NOMBRE, APELLIDO1, APELLIDO2, SEXO, FEC_NAC, SUSTITUTO)"
                    + " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

            log.debug("sql = " + query);

            // Preparar el statement
            ps = con.prepareStatement(query);

            // Configurar los parámetros de la consulta
            ps.setInt(contBD++, id);
            ps.setString(contBD++, nuevaIdentidad.getNumExp());
            ps.setString(contBD++, nuevaIdentidad.getDniNie());
            ps.setString(contBD++, nuevaIdentidad.getNumIden());
            ps.setString(contBD++, nuevaIdentidad.getNombre());
            ps.setString(contBD++, nuevaIdentidad.getApellido1());
            ps.setString(contBD++, nuevaIdentidad.getApellido2());
            ps.setString(contBD++, nuevaIdentidad.getSexo());
            ps.setDate(contBD++, nuevaIdentidad.getFechaNacimiento());
            ps.setString(contBD++, nuevaIdentidad.getSustituto());

            // Ejecutar la consulta y devolver el resultado
            return ps.executeUpdate() > 0;
        } catch (Exception ex) {
            log.error("Se ha producido un error al crear una nueva identidad: " + nuevaIdentidad.getId(), ex);
            throw new Exception(ex);
        } finally {
            if (ps != null) {
                ps.close();
            }
        }
    }

    public boolean modificarIdentidad(IdentidadVO datModif, Connection con) throws Exception {
        PreparedStatement ps = null;
        String query = null;
        int contBD = 1;
        try {
            int id = recogerIDInsertar(ConfigurationParameter.getParameter(ConstantesMeLanbide15.SEQ_M15_IDENTIDAD, ConstantesMeLanbide15.FICHERO_PROPIEDADES), con);
            // Construir la consulta SQL para actualizar la tabla de identidad
            query = "UPDATE " + ConfigurationParameter.getParameter(ConstantesMeLanbide15.MELANBIDE15_IDENTIDAD, ConstantesMeLanbide15.FICHERO_PROPIEDADES)
                    + " SET DNI = ?, NUM_IDEN = ?, NOMBRE = ?, APELLIDO1 = ?, APELLIDO2 = ?, SEXO = ?, FEC_NAC = ?, SUSTITUTO = ?"
                    + " WHERE ID = ? AND NUM_EXP = ?";

            // Preparar el statement
            ps = con.prepareStatement(query);

            // Configurar los parámetros de la consulta
            ps.setString(contBD++, datModif.getDniNie());
            ps.setString(contBD++, datModif.getNumIden());
            ps.setString(contBD++, datModif.getNombre());
            ps.setString(contBD++, datModif.getApellido1());
            ps.setString(contBD++, datModif.getApellido2());
            ps.setString(contBD++, datModif.getSexo());
            ps.setDate(contBD++, datModif.getFechaNacimiento());
            ps.setString(contBD++, datModif.getSustituto());
            ps.setInt(contBD++, datModif.getId());
            ps.setString(contBD++, datModif.getNumExp());

            log.debug("sql = " + query);

            // Ejecutar la consulta y devolver el resultado
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            log.error("Se ha producido un error al modificar una identidad: " + datModif.getId(), ex);
            throw new Exception(ex);
        } finally {
            if (ps != null) {
                ps.close();
            }
        }
    }

    public List<FormacionVO> getListaFormacion(String numExp, int codOrganizacion, Connection con) throws Exception {
        log.info("Entramos en getListaFormacion DAO - " + numExp);
        Statement st = null;
        ResultSet rs = null;
        List<FormacionVO> lista = new ArrayList<FormacionVO>();

        try {
            String query = "SELECT * FROM "
                    + ConfigurationParameter.getParameter(ConstantesMeLanbide15.MELANBIDE15_FORMACION, ConstantesMeLanbide15.FICHERO_PROPIEDADES)
                    + " WHERE NUM_EXP ='" + numExp + "'"
                    + " ORDER BY ID";

            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }

            st = con.createStatement();
            rs = st.executeQuery(query);

            while (rs.next()) {
                FormacionVO formacion = (FormacionVO) MeLanbide15MappingUtils.getInstance().map(rs, FormacionVO.class);
                lista.add(formacion);
            }
        } catch (Exception ex) {
            log.error("Se ha producido un error recuperando la lista de formación: ", ex);
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

    public FormacionVO getFormacionPorID(String id, Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        FormacionVO formacion = new FormacionVO();

        try {
            String query = "SELECT * FROM "
                    + ConfigurationParameter.getParameter(ConstantesMeLanbide15.MELANBIDE15_FORMACION, ConstantesMeLanbide15.FICHERO_PROPIEDADES)
                    + " WHERE ID = " + (id != null && !id.isEmpty() ? id : "null");

            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }

            st = con.createStatement();
            rs = st.executeQuery(query);

            while (rs.next()) {
                formacion = (FormacionVO) MeLanbide15MappingUtils.getInstance().map(rs, FormacionVO.class);
            }
        } catch (Exception ex) {
            log.error("Se ha producido un error recuperando una formación por ID: " + id, ex);
            throw new Exception(ex);
        } finally {
            if (st != null) {
                st.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        return formacion;
    }

    public boolean crearNuevaFormacion(FormacionVO nuevaFormacion, Connection con) throws Exception {
        PreparedStatement ps = null;
        String query = null;
        int contBD = 1;
        try {
            int id = recogerIDInsertar(ConfigurationParameter.getParameter(ConstantesMeLanbide15.SEQ_M15_FORMACION, ConstantesMeLanbide15.FICHERO_PROPIEDADES), con);

            query = "INSERT INTO "
                    + ConfigurationParameter.getParameter(ConstantesMeLanbide15.MELANBIDE15_FORMACION, ConstantesMeLanbide15.FICHERO_PROPIEDADES)
                    + " (ID, NUM_EXP, DNI_FOR, NUM_IDEN_FOR, HORAS_FOR, PORCENTAJE_FOR, SUBVENCION_FOR)"
                    + " VALUES (?, ?, ?, ?, ?, ?, ?)";

            log.debug("sql = " + query);

            ps = con.prepareStatement(query);

            ps.setInt(contBD++, id);

            ps.setString(contBD++, nuevaFormacion.getNumExp());
            ps.setString(contBD++, nuevaFormacion.getDniFor());
            ps.setString(contBD++, nuevaFormacion.getNumIdenFor());

            ps.setDouble(contBD++, nuevaFormacion.getHorasFor());

            ps.setDouble(contBD++, nuevaFormacion.getPorcentajeFor());

            if (nuevaFormacion.getSubvencionFor() != null) {
                ps.setDouble(contBD++, nuevaFormacion.getSubvencionFor());
            } else {
                ps.setNull(contBD++, java.sql.Types.DOUBLE);
            }

            // Ejecutar la consulta
            int resultado = ps.executeUpdate();
            log.debug("Filas insertadas: " + resultado);

            return resultado > 0;

        } catch (Exception ex) {
            log.error("Error al crear una nueva formacion con ID: " + nuevaFormacion.getId(), ex);
            throw new Exception("Error al insertar la fofrmacion: ", ex);
        } finally {
            if (ps != null) {
                ps.close();
            }
        }
    }

    public boolean modificarFormacion(FormacionVO datModif, Connection con) throws Exception {
        PreparedStatement ps = null;
        String query = null;
        int contBD = 1;
        try {
            int id = recogerIDInsertar(ConfigurationParameter.getParameter(ConstantesMeLanbide15.SEQ_M15_FORMACION, ConstantesMeLanbide15.FICHERO_PROPIEDADES), con);
            query = "UPDATE "
                    + ConfigurationParameter.getParameter(ConstantesMeLanbide15.MELANBIDE15_FORMACION, ConstantesMeLanbide15.FICHERO_PROPIEDADES)
                    + " SET DNI_FOR = ?, NUM_IDEN_FOR = ?, HORAS_FOR = ?, PORCENTAJE_FOR = ?, SUBVENCION_FOR = ?"
                    + " WHERE ID = ? AND NUM_EXP = ?";

            ps = con.prepareStatement(query);

            ps.setString(contBD++, datModif.getDniFor());
            ps.setString(contBD++, datModif.getNumIdenFor());
            ps.setDouble(contBD++, datModif.getHorasFor());
            ps.setDouble(contBD++, datModif.getPorcentajeFor());
            ps.setDouble(contBD++, datModif.getSubvencionFor());
            ps.setInt(contBD++, datModif.getId());
            ps.setString(contBD++, datModif.getNumExp());

            log.debug("sql = " + query);

            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            log.error("Error al modificar una formación: " + datModif.getId(), ex);
            throw new Exception(ex);
        } finally {
            if (ps != null) {
                ps.close();
            }
        }
    }

    public boolean eliminarFormacion(int id, String numExp, Connection con) throws Exception {
        PreparedStatement ps = null;

        try {
            String query = "DELETE FROM "
                    + ConfigurationParameter.getParameter(ConstantesMeLanbide15.MELANBIDE15_FORMACION, ConstantesMeLanbide15.FICHERO_PROPIEDADES)
                    + " WHERE ID = ? AND NUM_EXP = ?";

            ps = con.prepareStatement(query);
            ps.setInt(1, id);
            ps.setString(2, numExp);

            log.debug("Ejecutando consulta SQL: " + query + " [ID=" + id + ", NUM_EXP=" + numExp + "]");

            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            log.error("Error al eliminar una formación: ID=" + id + ", NUM_EXP=" + numExp, ex);
            throw new Exception(ex);
        } finally {
            if (ps != null) {
                ps.close();
            }
        }
    }

    public List<ContratacionVO> getListaContratacion(String numExp, int codOrganizacion, Connection con) throws Exception {
        log.info("Entramos en getListaContratacion DAO - " + numExp);
        Statement st = null;
        ResultSet rs = null;
        List<ContratacionVO> lista = new ArrayList<ContratacionVO>();
        ContratacionVO contratacion;

        try {
            String query = "SELECT * FROM " + ConfigurationParameter.getParameter(ConstantesMeLanbide15.MELANBIDE15_CONTRATACION, ConstantesMeLanbide15.FICHERO_PROPIEDADES)
                    + " WHERE NUM_EXP ='" + numExp + "'"
                    + " ORDER BY ID";

            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }

            st = con.createStatement();
            rs = st.executeQuery(query);

            while (rs.next()) {
                contratacion = (ContratacionVO) MeLanbide15MappingUtils.getInstance().map(rs, ContratacionVO.class);
                lista.add(contratacion);
            }
        } catch (Exception ex) {
            log.error("Se ha producido un error recuperando identidad ", ex);
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

    public ContratacionVO getContratacionPorID(String id, Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        ContratacionVO contratacion = new ContratacionVO();

        try {
            String query = "SELECT * FROM " + ConfigurationParameter.getParameter(ConstantesMeLanbide15.MELANBIDE15_CONTRATACION, ConstantesMeLanbide15.FICHERO_PROPIEDADES)
                    + " WHERE ID = " + (id != null && !id.isEmpty() ? id : "null");

            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }

            st = con.createStatement();
            rs = st.executeQuery(query);

            while (rs.next()) {
                contratacion = (ContratacionVO) MeLanbide15MappingUtils.getInstance().map(rs, ContratacionVO.class);
            }
        } catch (Exception ex) {
            log.error("Se ha producido un error recuperando una identidad : " + id, ex);
            throw new Exception(ex);
        } finally {
            if (st != null) {
                st.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        return contratacion;
    }

    public boolean eliminarContratacion(int id, String numExp, Connection con) throws Exception {
        PreparedStatement ps = null;

        try {
            String query = "DELETE FROM " + ConfigurationParameter.getParameter(ConstantesMeLanbide15.MELANBIDE15_CONTRATACION, ConstantesMeLanbide15.FICHERO_PROPIEDADES)
                    + " WHERE ID = ? AND NUM_EXP = ?";
            ps = con.prepareStatement(query);
            ps.setInt(1, id);
            ps.setString(2, numExp);

            log.debug("Ejecutando consulta SQL: " + query + " [ID=" + id + ", NUM_EXP=" + numExp + "]");
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            log.error("Error al eliminar identidad con ID: " + id + " y NUM_EXP: " + numExp, ex);
            throw new Exception("Error en la base de datos al eliminar contratacón.", ex);
        } finally {
            // Cerrar el PreparedStatement
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException e) {
                    log.error("Error al cerrar el PreparedStatement: " + e.getMessage(), e);
                }
            }
        }
    }

    public boolean crearNuevaContratacion(ContratacionVO nuevaContratacion, Connection con) throws Exception {
        PreparedStatement ps = null;
        String query = null;
        int contBD = 1;

        try {
            int id = recogerIDInsertar(ConfigurationParameter.getParameter(ConstantesMeLanbide15.SEQ_M15_CONTRATACION, ConstantesMeLanbide15.FICHERO_PROPIEDADES), con);

            query = "INSERT INTO " + ConfigurationParameter.getParameter(ConstantesMeLanbide15.MELANBIDE15_CONTRATACION, ConstantesMeLanbide15.FICHERO_PROPIEDADES)
                    + " (ID, NUM_EXP, DNI_CON, NUM_IDEN_CON, FEC_INI_CON, FEC_FIN_CON, SUBVENCION_CON)"
                    + " VALUES ( ?, ?, ?, ?, ?, ?, ?)";

            log.debug("sql = " + query);

            // Preparar el statement
            ps = con.prepareStatement(query);

            // Configurar los parámetros de la consulta
            ps.setInt(contBD++, id);

            ps.setString(contBD++, nuevaContratacion.getNumExp());
            ps.setString(contBD++, nuevaContratacion.getDniCon());
            ps.setString(contBD++, nuevaContratacion.getNumIdenCon());
            ps.setDate(contBD++, nuevaContratacion.getFecIniCon());
            ps.setDate(contBD++, nuevaContratacion.getFecFinCon());
            if (nuevaContratacion.getSubvencionCon() != null) {
                ps.setDouble(contBD++, nuevaContratacion.getSubvencionCon());
            } else {
                ps.setNull(contBD++, java.sql.Types.DOUBLE);
            }
            log.debug("sql = " + query);
            return ps.executeUpdate() > 0;
        } catch (Exception ex) {
            log.error("Se ha producido un error al crear una nueva Contratacion: " + nuevaContratacion.getId(), ex);
            throw new Exception(ex);
        } finally {
            if (ps != null) {
                ps.close();
            }
        }
    }

    public boolean modificarContratacion(ContratacionVO datModif, Connection con) throws Exception {
        PreparedStatement ps = null;
        String query = null;
        int contBD = 1;

        try {

            int id = recogerIDInsertar(ConfigurationParameter.getParameter(ConstantesMeLanbide15.SEQ_M15_CONTRATACION, ConstantesMeLanbide15.FICHERO_PROPIEDADES), con);

            query = "UPDATE " + ConfigurationParameter.getParameter(ConstantesMeLanbide15.MELANBIDE15_CONTRATACION, ConstantesMeLanbide15.FICHERO_PROPIEDADES)
                    + " SET DNI_CON = ?, NUM_IDEN_CON = ?, FEC_INI_CON = ?, FEC_FIN_CON = ?, SUBVENCION_CON = ?"
                    + " WHERE ID = ? AND NUM_EXP = ?";

            // Preparar el statement
            ps = con.prepareStatement(query);

            // Configurar los parámetros de la consulta
            ps.setString(contBD++, datModif.getDniCon());
            ps.setString(contBD++, datModif.getNumIdenCon());
            ps.setDate(contBD++, datModif.getFecIniCon());
            ps.setDate(contBD++, datModif.getFecFinCon());
            ps.setDouble(contBD++, datModif.getSubvencionCon());
            ps.setInt(contBD++, datModif.getId());
            ps.setString(contBD++, datModif.getNumExp());

            log.debug("sql = " + query);

            // Ejecutar la consulta y devolver el resultado
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            log.error("Se ha producido un error al modificar una contratación: " + datModif.getId(), ex);
            throw new Exception(ex);
        } finally {
            if (ps != null) {
                ps.close();
            }
        }
    }

    public List<OrientacionVO> getListaOrientacion(String numExp, int codOrganizacion, Connection con) throws Exception {
        log.info("Entramos en getListaOrientacion DAO - " + numExp);
        Statement st = null;
        ResultSet rs = null;
        List<OrientacionVO> lista = new ArrayList<OrientacionVO>();
        OrientacionVO orientacion = null;

        try {
            String query = "SELECT * FROM " + ConfigurationParameter.getParameter(ConstantesMeLanbide15.MELANBIDE15_ORIENTACION, ConstantesMeLanbide15.FICHERO_PROPIEDADES)
                    + " WHERE NUM_EXP ='" + numExp + "'"
                    + " ORDER BY ID";

            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }

            st = con.createStatement();
            rs = st.executeQuery(query);

            while (rs.next()) {
                orientacion = (OrientacionVO) MeLanbide15MappingUtils.getInstance().map(rs, OrientacionVO.class);
                lista.add(orientacion);
            }
        } catch (Exception ex) {
            log.error("Se ha producido un error recuperando identidad ", ex);
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

    public OrientacionVO getOrientacionPorID(String id, Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        OrientacionVO orientacion = new OrientacionVO();

        try {
            String query = "SELECT * FROM " + ConfigurationParameter.getParameter(ConstantesMeLanbide15.MELANBIDE15_ORIENTACION, ConstantesMeLanbide15.FICHERO_PROPIEDADES)
                    + " WHERE ID = " + (id != null && !id.isEmpty() ? id : "null");

            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }

            st = con.createStatement();
            rs = st.executeQuery(query);

            while (rs.next()) {
                orientacion = (OrientacionVO) MeLanbide15MappingUtils.getInstance().map(rs, OrientacionVO.class);
            }
        } catch (Exception ex) {
            log.error("Se ha producido un error recuperando una orientacion : " + id, ex);
            throw new Exception(ex);
        } finally {
            if (st != null) {
                st.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        return orientacion;
    }

    public boolean eliminarOrientacion(int id, String numExp, Connection con) throws Exception {
        PreparedStatement ps = null;

        try {
            String query = "DELETE FROM " + ConfigurationParameter.getParameter(ConstantesMeLanbide15.MELANBIDE15_ORIENTACION, ConstantesMeLanbide15.FICHERO_PROPIEDADES)
                    + " WHERE ID = ? AND NUM_EXP = ?";

            ps = con.prepareStatement(query);

            ps.setInt(1, id);
            ps.setString(2, numExp);

            log.debug("Ejecutando consulta SQL: " + query + " [ID=" + id + ", NUM_EXP=" + numExp + "]");
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            log.error("Error al eliminar identidad con ID: " + id + " y NUM_EXP: " + numExp, ex);
            throw new Exception("Error en la base de datos al eliminar identidad.", ex);
        } finally {
            // Cerrar el PreparedStatement
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException e) {
                    log.error("Error al cerrar el PreparedStatement: " + e.getMessage(), e);
                }
            }
        }
    }

    public boolean crearNuevaOrientacion(OrientacionVO nuevaOrientacion, Connection con) throws Exception {
        PreparedStatement ps = null;
        String query = null;
        int contBD = 1;

        try {
            // Generar el ID para la nueva orientación
            int id = recogerIDInsertar(ConfigurationParameter.getParameter(ConstantesMeLanbide15.SEQ_M15_ORIENTACION, ConstantesMeLanbide15.FICHERO_PROPIEDADES), con);

            query = "INSERT INTO " + ConfigurationParameter.getParameter(ConstantesMeLanbide15.MELANBIDE15_ORIENTACION, ConstantesMeLanbide15.FICHERO_PROPIEDADES)
                    + " (ID, NUM_EXP, DNI_ORI, NUM_IDEN_ORI, HORAS_ORI, SUBVENCION_ORI)"
                    + " VALUES (?, ?, ?, ?, ?, ?)";

            log.debug("SQL = " + query);

            ps = con.prepareStatement(query);

            // Asignar valores al PreparedStatement asegurando que no sean nulos
            ps.setInt(contBD++, id); // ID generado

            ps.setString(contBD++, nuevaOrientacion.getNumExp() != null ? nuevaOrientacion.getNumExp() : "");
            ps.setString(contBD++, nuevaOrientacion.getDniOri() != null ? nuevaOrientacion.getDniOri() : "");
            ps.setString(contBD++, nuevaOrientacion.getNumIdenOri() != null ? nuevaOrientacion.getNumIdenOri() : "");

            if (nuevaOrientacion.getHorasOri() != null) {
                ps.setDouble(contBD++, nuevaOrientacion.getHorasOri());
            } else {
                ps.setNull(contBD++, java.sql.Types.DOUBLE);
            }

            if (nuevaOrientacion.getSubvencionOri() != null) {
                ps.setDouble(contBD++, nuevaOrientacion.getSubvencionOri());
            } else {
                ps.setNull(contBD++, java.sql.Types.DOUBLE);
            }

            // Ejecutar la consulta
            int resultado = ps.executeUpdate();
            log.debug("Filas insertadas: " + resultado);

            return resultado > 0;

        } catch (Exception ex) {
            log.error("Error al crear una nueva orientación con ID: " + nuevaOrientacion.getId(), ex);
            throw new Exception("Error al insertar la orientación: ", ex);
        } finally {
            if (ps != null) {
                ps.close();
            }
        }
    }

    public boolean modificarOrientacion(OrientacionVO datModif, Connection con) throws Exception {
        PreparedStatement ps = null;
        String query = null;
        int contBD = 1;

        try {
            int id = recogerIDInsertar(ConfigurationParameter.getParameter(ConstantesMeLanbide15.SEQ_M15_IDENTIDAD, ConstantesMeLanbide15.FICHERO_PROPIEDADES), con);
            query = "UPDATE " + ConfigurationParameter.getParameter(ConstantesMeLanbide15.MELANBIDE15_ORIENTACION, ConstantesMeLanbide15.FICHERO_PROPIEDADES)
                    + " SET DNI_ORI = ?, NUM_IDEN_ORI = ?, HORAS_ORI = ?, SUBVENCION_ORI = ?"
                    + " WHERE ID = ? AND NUM_EXP = ?";
            ps = con.prepareStatement(query);
            ps.setString(contBD++, datModif.getDniOri());
            ps.setString(contBD++, datModif.getNumIdenOri());
            ps.setDouble(contBD++, datModif.getHorasOri());
            ps.setDouble(contBD++, datModif.getSubvencionOri());
            ps.setInt(contBD++, datModif.getId());
            ps.setString(contBD++, datModif.getNumExp());

            log.debug("sql = " + query);

            // Ejecutar la consulta y devolver el resultado
            return ps.executeUpdate() > 0;
        } catch (SQLException ex) {
            log.error("Se ha producido un error al modificar una orientación: " + datModif.getId(), ex);
            throw new Exception(ex);
        } finally {
            if (ps != null) {
                ps.close();
            }
        }
    }

    public List<DesplegableVO> getValoresE_Des(String des_cod, Connection con) throws Exception {
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<DesplegableVO> lista = new ArrayList<DesplegableVO>();
        DesplegableVO valoresDesplegable = new DesplegableVO();
        try {
            String query = null;
            query = "SELECT * FROM " + ConfigurationParameter.getParameter(ConstantesMeLanbide15.TABLA_VALORES_DESPLEGABLES, ConstantesMeLanbide15.FICHERO_PROPIEDADES)
                    + " WHERE DES_COD= ? order by DES_VAL_COD";
            log.debug("sql = " + query);
            int i = 1;
            ps = con.prepareStatement(query);
            ps.setString(i++, des_cod);
            rs = ps.executeQuery();

            while (rs.next()) {
                valoresDesplegable = (DesplegableVO) MeLanbide15MappingUtils.getInstance().map(rs, DesplegableVO.class);
                lista.add(valoresDesplegable);
                valoresDesplegable = new DesplegableVO();
            }
        } catch (Exception ex) {
            log.error("Se ha producido un error recuperando valores de desplegable : " + des_cod, ex);
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

}

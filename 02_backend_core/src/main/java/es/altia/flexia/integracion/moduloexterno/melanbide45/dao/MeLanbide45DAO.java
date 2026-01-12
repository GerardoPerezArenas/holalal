package es.altia.flexia.integracion.moduloexterno.melanbide45.dao;

import es.altia.agora.business.util.GlobalNames;
import es.altia.flexia.integracion.moduloexterno.melanbide45.util.ConfigurationParameter;
import es.altia.flexia.integracion.moduloexterno.melanbide45.util.ConstantesMeLanbide45;
import es.altia.flexia.integracion.moduloexterno.melanbide45.util.MeLanbide45Exception;
import es.altia.flexia.integracion.moduloexterno.melanbide45.util.MeLanbide45MappingUtils;
import es.altia.flexia.integracion.moduloexterno.melanbide45.vo.espacioform.EspacioFormVO;
import es.altia.flexia.integracion.moduloexterno.melanbide45.vo.materialconsu.MaterialConsuVO;
import es.altia.flexia.integracion.moduloexterno.melanbide45.vo.moduloform.ModuloFormVO;
import es.altia.flexia.integracion.moduloexterno.melanbide45.vo.ubicacion.UbicacionVO;
import es.altia.flexia.integracion.moduloexterno.melanbide45.util.MeLanbide45Utils;
import es.altia.flexia.integracion.moduloexterno.melanbide45.vo.especialidades.EspecialidadesVO;

import java.sql.*;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

/**
 *
 * @author davidg
 */
public class MeLanbide45DAO {

    //Logger
    private static Logger log = LogManager.getLogger(MeLanbide45DAO.class);
    //Instancia
    private static MeLanbide45DAO instance = null;

    private MeLanbide45DAO() {

    }

    public static MeLanbide45DAO getInstance() {
        if (instance == null) {
            synchronized (MeLanbide45DAO.class) {
                instance = new MeLanbide45DAO();
            }
        }
        return instance;
    }

    /**
     * Metodos select datos
     *
     * @param numExpediente
     * @param con
     * @return
     * @throws java.lang.Exception
     *
     */
    public List<ModuloFormVO> getDatosModulosForm(String numExpediente, Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        List<ModuloFormVO> listModulosForm = new ArrayList<ModuloFormVO>();
        ModuloFormVO moduloForm = new ModuloFormVO();
        try {
            String query = null;
            query = "SELECT * FROM " + ConfigurationParameter.getParameter(ConstantesMeLanbide45.TABLA_RGEF_MODFORMATIVO, ConstantesMeLanbide45.FICHERO_PROPIEDADES)
                    + " WHERE MDF_NUM = '" + numExpediente + "' ORDER BY MDF_DEN";
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                moduloForm = (ModuloFormVO) MeLanbide45MappingUtils.getInstance().map(rs, ModuloFormVO.class);
                listModulosForm.add(moduloForm);
                moduloForm = new ModuloFormVO();
            }
        } catch (Exception ex) {
            log.error("Se ha producido un error recuperando Modulos Formativos del Expediente " + numExpediente, ex);
            throw new Exception(ex);
        } finally {
            try {
                if (log.isDebugEnabled()) {
                    log.debug("Procedemos a cerrar el statement y el resultset");
                }
                if (st != null) {
                    st.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException e) {
                log.error("Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }
        return listModulosForm;
    }

    public List<MaterialConsuVO> getDatosMaterial(String numExpediente, Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        List<MaterialConsuVO> listModulosForm = new ArrayList<MaterialConsuVO>();
        MaterialConsuVO moduloForm = new MaterialConsuVO();
        try {
            String query = null;
            query = "SELECT * FROM " + ConfigurationParameter.getParameter(ConstantesMeLanbide45.TABLA_RGEF_MATERIALCONSU, ConstantesMeLanbide45.FICHERO_PROPIEDADES)
                    + " WHERE MAC_NUM = '" + numExpediente + "' ORDER BY MAC_DET";
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                moduloForm = (MaterialConsuVO) MeLanbide45MappingUtils.getInstance().map(rs, MaterialConsuVO.class);
                listModulosForm.add(moduloForm);
                moduloForm = new MaterialConsuVO();
            }
        } catch (Exception ex) {
            log.error("Se ha producido un error recuperando Material consumible del Expediente " + numExpediente, ex);
            throw new Exception(ex);
        } finally {
            try {
                if (log.isDebugEnabled()) {
                    log.debug("Procedemos a cerrar el statement y el resultset");
                }
                if (st != null) {
                    st.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException e) {
                log.error("Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }
        return listModulosForm;
    }

    public List<EspacioFormVO> getDatosEspaciosForm(String numExpediente, Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        List<EspacioFormVO> listEspaciosForm = new ArrayList<EspacioFormVO>();
        EspacioFormVO espacioForm = new EspacioFormVO();
        try {
            String query = null;
            query = "SELECT * FROM " + ConfigurationParameter.getParameter(ConstantesMeLanbide45.TABLA_RGEF_ESPFORMATIVO, ConstantesMeLanbide45.FICHERO_PROPIEDADES)
                    + " WHERE EPF_NUM = '" + numExpediente + "' ORDER BY EPF_DES";
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                espacioForm = (EspacioFormVO) MeLanbide45MappingUtils.getInstance().map(rs, EspacioFormVO.class);
                listEspaciosForm.add(espacioForm);
                espacioForm = new EspacioFormVO();
            }
        } catch (Exception ex) {
            log.error("Se ha producido un error recuperando Espacios Formativos del Expediente " + numExpediente, ex);
            throw new Exception(ex);
        } finally {
            try {
                if (log.isDebugEnabled()) {
                    log.debug("Procedemos a cerrar el statement y el resultset");
                }
                if (st != null) {
                    st.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException e) {
                log.error("Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }
        return listEspaciosForm;
    }

    public List<MaterialConsuVO> getDatosMaterialConsu(String numExpediente, Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        List<MaterialConsuVO> listEspaciosForm = new ArrayList<MaterialConsuVO>();
        MaterialConsuVO espacioForm = new MaterialConsuVO();
        try {
            String query = null;
            query = "SELECT * FROM " + ConfigurationParameter.getParameter(ConstantesMeLanbide45.TABLA_RGEF_MATERIALCONSU, ConstantesMeLanbide45.FICHERO_PROPIEDADES)
                    + " WHERE MAC_NUM = '" + numExpediente + "' ";
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                espacioForm = (MaterialConsuVO) MeLanbide45MappingUtils.getInstance().map(rs, MaterialConsuVO.class);
                listEspaciosForm.add(espacioForm);
                espacioForm = new MaterialConsuVO();
            }
        } catch (Exception ex) {
            log.error("Se ha producido un error recuperando Espacios Formativos del Expediente " + numExpediente, ex);
            throw new Exception(ex);
        } finally {
            try {
                if (log.isDebugEnabled()) {
                    log.debug("Procedemos a cerrar el statement y el resultset");
                }
                if (st != null) {
                    st.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException e) {
                log.error("Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }
        return listEspaciosForm;
    }

    public ModuloFormVO getModuloFormPorCodigo(String numExpediente, String id, Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        ModuloFormVO _datoReturn = new ModuloFormVO();
        try {
            String query = null;
            query = "SELECT * FROM " + ConfigurationParameter.getParameter(ConstantesMeLanbide45.TABLA_RGEF_MODFORMATIVO, ConstantesMeLanbide45.FICHERO_PROPIEDADES)
                    + " WHERE MDF_NUM='" + numExpediente + "' AND ID =" + id + "  ORDER BY MDF_DEN";
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                _datoReturn = (ModuloFormVO) MeLanbide45MappingUtils.getInstance().map(rs, ModuloFormVO.class);
            }
        } catch (Exception ex) {
            log.error("Se ha producido un error recuperando Datos Modulo Formativo por codigo" + numExpediente + " -" + id, ex);
            throw new Exception(ex);
        } finally {
            try {
                if (log.isDebugEnabled()) {
                    log.debug("Procedemos a cerrar el statement y el resultset");
                }
                if (st != null) {
                    st.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException e) {
                log.error("Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }
        return _datoReturn;
    }

    public MaterialConsuVO getMaterialesPorCodigo(String numExpediente, String id, Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        MaterialConsuVO _datoReturn = new MaterialConsuVO();
        try {
            String query = null;
            query = "SELECT * FROM " + ConfigurationParameter.getParameter(ConstantesMeLanbide45.TABLA_RGEF_MATERIALCONSU, ConstantesMeLanbide45.FICHERO_PROPIEDADES)
                    + " WHERE MAC_NUM='" + numExpediente + "' AND ID =" + id;
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                _datoReturn = (MaterialConsuVO) MeLanbide45MappingUtils.getInstance().map(rs, MaterialConsuVO.class);
            }
        } catch (Exception ex) {
            log.error("Se ha producido un error recuperando Datos Modulo Formativo por codigo" + numExpediente + " -" + id, ex);
            throw new Exception(ex);
        } finally {
            try {
                if (log.isDebugEnabled()) {
                    log.debug("Procedemos a cerrar el statement y el resultset");
                }
                if (st != null) {
                    st.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException e) {
                log.error("Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }
        return _datoReturn;
    }

    public EspacioFormVO getEspacioFormPorCodigo(String numExpediente, String id, Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        EspacioFormVO _datoReturn = new EspacioFormVO();
        try {
            String query = null;
            query = "SELECT * FROM " + ConfigurationParameter.getParameter(ConstantesMeLanbide45.TABLA_RGEF_ESPFORMATIVO, ConstantesMeLanbide45.FICHERO_PROPIEDADES)
                    + " WHERE EPF_NUM='" + numExpediente + "' AND ID =" + id + "  ORDER BY EPF_DES";
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                _datoReturn = (EspacioFormVO) MeLanbide45MappingUtils.getInstance().map(rs, EspacioFormVO.class);
            }
        } catch (Exception ex) {
            log.error("Se ha producido un error recuperando Datos Espacio Formativo por codigo" + numExpediente + " -" + id, ex);
            throw new Exception(ex);
        } finally {
            try {
                if (log.isDebugEnabled()) {
                    log.debug("Procedemos a cerrar el statement y el resultset");
                }
                if (st != null) {
                    st.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException e) {
                log.error("Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }
        return _datoReturn;
    }

    /**
     * Metodos Insert datos
     *
     * @param nuevDato
     * @param con
     * @return boolean
     * @throws java.lang.Exception
     *
     */
    public boolean crearNuevoModuloForm(ModuloFormVO nuevDato, Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        //boolean opeCorrecta = true;
        String query = "";
        try {
            query = "INSERT INTO " + ConfigurationParameter.getParameter(ConstantesMeLanbide45.TABLA_RGEF_MODFORMATIVO, ConstantesMeLanbide45.FICHERO_PROPIEDADES)
                    + "(ID, MDF_NUM, MDF_DEN, MDF_DUR, MDF_OBJ, MDF_CT"
                    + ", MDF_COD, MDF_UC_COD, MDF_UC_DEN, MDF_DUR_MAX_TEL, MDF_UC_NIVEL, MDF_UC_EXISTENTE, "
                    + " MDF_NIVEL, MDF_EXISTENTE) "
                    + " VALUES (" + ConfigurationParameter.getParameter(ConstantesMeLanbide45.SEQ_RGEF_MODFOR, ConstantesMeLanbide45.FICHERO_PROPIEDADES) + ".NextVal "
                    + ",'" + nuevDato.getNumExp() + "'"
                    + ",'" + nuevDato.getDenominacion() + "'"
                    + "," + (nuevDato.getDuracion() != null ? nuevDato.getDuracion() : "null")
                    + ",'" + nuevDato.getObjetivo() + "'"
                    + ",'" + nuevDato.getContenidoTP() + "'"
                    + ",'" + nuevDato.getCodMod() + "'"
                    + ",'" + nuevDato.getCodUC() + "'"
                    + ",'" + nuevDato.getDesUC() + "'"
                    + "," + (nuevDato.getDuracMax() != null ? nuevDato.getDuracMax() : "null")
                    + ",'" + nuevDato.getUc_nivel() + "'"
                    + ",'" + nuevDato.getUc_existe() + "'" + ",'" + nuevDato.getNivel() + "'"
                    + ",'" + nuevDato.getExiste() + "'"
                    + ")";
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            int insert = st.executeUpdate(query);
            return insert > 0;

        } catch (SQLException ex) {
            //opeCorrecta = false;
            log.debug("Se ha producido un error al Insertar un nuevo Registro de Modulo formativo para la especialidad " + " - " + ex.getMessage() + ex);
            throw new Exception(ex);
            //return opeCorrecta;
        } finally {
            try {
                if (log.isDebugEnabled()) {
                    log.debug("Procedemos a cerrar el statement y el resultset");
                }
                if (st != null) {
                    st.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException e) {
                log.error("Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }
    }

    public boolean crearNuevoMaterial(MaterialConsuVO nuevDato, Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        //boolean opeCorrecta = true;
        String query = "";
        try {
            query = "INSERT INTO " + ConfigurationParameter.getParameter(ConstantesMeLanbide45.TABLA_RGEF_MATERIALCONSU, ConstantesMeLanbide45.FICHERO_PROPIEDADES)
                    + "(ID, MAC_NUM, MAC_CANT, MAC_DET) "
                    + " VALUES (" + ConfigurationParameter.getParameter(ConstantesMeLanbide45.SEQ_RGEF_MATCON, ConstantesMeLanbide45.FICHERO_PROPIEDADES) + ".NextVal "
                    + ",'" + nuevDato.getNumExp() + "'"
                    + "," + (nuevDato.getCantidad() != null ? nuevDato.getCantidad() : "null")
                    + ",'" + nuevDato.getDescripcion() + "'"
                    + ")";
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            int insert = st.executeUpdate(query);
            return insert > 0;

        } catch (SQLException ex) {
            //opeCorrecta = false;
            log.debug("Se ha producido un error al Insertar un nuevo Registro de Modulo formativo para la especialidad " + " - " + ex.getMessage() + ex);
            throw new Exception(ex);
            //return opeCorrecta;
        } finally {
            try {
                if (log.isDebugEnabled()) {
                    log.debug("Procedemos a cerrar el statement y el resultset");
                }
                if (st != null) {
                    st.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException e) {
                log.error("Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }
    }

    public boolean crearNuevoEspacioForm(EspacioFormVO nuevDato, Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        //boolean opeCorrecta = true;
        String query = "";
        try {
            query = "INSERT INTO " + ConfigurationParameter.getParameter(ConstantesMeLanbide45.TABLA_RGEF_ESPFORMATIVO, ConstantesMeLanbide45.FICHERO_PROPIEDADES)
                    + "(ID, EPF_NUM, EPF_DES, EPF_SUP) "
                    + " VALUES (" + ConfigurationParameter.getParameter(ConstantesMeLanbide45.SEQ_RGEF_ESPFOR, ConstantesMeLanbide45.FICHERO_PROPIEDADES) + ".NextVal "
                    + ",'" + nuevDato.getNumExp() + "'"
                    + ",'" + nuevDato.getDescripcion() + "'"
                    + "," + (nuevDato.getSuperficie() != null ? nuevDato.getSuperficie() : "null")
                    + ")";
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            int insert = st.executeUpdate(query);
            return insert > 0;

        } catch (SQLException ex) {
            //opeCorrecta = false;
            log.debug("Se ha producido un error al Insertar un nuevo Registro de Espacio formativo para la especialidad " + " - " + ex.getMessage() + ex);
            throw new Exception(ex);
            //return opeCorrecta;
        } finally {
            try {
                if (log.isDebugEnabled()) {
                    log.debug("Procedemos a cerrar el statement y el resultset");
                }
                if (st != null) {
                    st.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException e) {
                log.error("Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }
    }

    /**
     * Metodos update datos
     *
     * @param nuevoDato
     * @param con
     * @return boolean
     * @throws java.lang.Exception
     *
     */
    public boolean modificarModuloForm(ModuloFormVO nuevoDato, Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        //boolean opeCorrecta = true;
        String query = "";
        try {
            query = "UPDATE " + ConfigurationParameter.getParameter(ConstantesMeLanbide45.TABLA_RGEF_MODFORMATIVO, ConstantesMeLanbide45.FICHERO_PROPIEDADES)
                    + " SET MDF_DEN='" + nuevoDato.getDenominacion() + "'"
                    + ", MDF_DUR=" + (nuevoDato.getDuracion() != null ? nuevoDato.getDuracion() : "null")
                    + ", MDF_OBJ='" + nuevoDato.getObjetivo() + "'"
                    + ", MDF_CT='" + nuevoDato.getContenidoTP() + "'"
                    + ", MDF_COD='" + nuevoDato.getCodMod() + "'"
                    + ", MDF_UC_COD='" + nuevoDato.getCodUC() + "'"
                    + ", MDF_UC_DEN='" + nuevoDato.getDesUC() + "'"
                    + ", MDF_DUR_MAX_TEL=" + (nuevoDato.getDuracMax() != null ? nuevoDato.getDuracMax() : "null") + ""
                    + ", MDF_UC_NIVEL='" + nuevoDato.getUc_nivel() + "'"
                    + ", MDF_UC_EXISTENTE='" + nuevoDato.getUc_existe() + "'"
                    + ", MDF_NIVEL='" + nuevoDato.getNivel() + "'"
                    + ", MDF_EXISTENTE='" + nuevoDato.getExiste() + "'"
                    + " WHERE MDF_NUM='" + nuevoDato.getNumExp() + "' AND ID=" + nuevoDato.getId();
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            int insert = st.executeUpdate(query);
            return insert > 0;

        } catch (SQLException ex) {
            //opeCorrecta = false;
            log.debug("Se ha producido un error al Modificar Modulo Formativo" + " - "
                    + nuevoDato.getNumExp() + " - " + nuevoDato.getId() + " - " + ex.getMessage() + ex);
            throw new Exception(ex);
            //return opeCorrecta;
        } finally {
            try {
                if (log.isDebugEnabled()) {
                    log.debug("Procedemos a cerrar el statement y el resultset");
                }
                if (st != null) {
                    st.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException e) {
                log.error("Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }
    }

    public boolean modificarMFyUCModuloForm(ModuloFormVO nuevoDato, Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        //boolean opeCorrecta = true;
        String query = "";
        try {
            query = "UPDATE " + ConfigurationParameter.getParameter(ConstantesMeLanbide45.TABLA_RGEF_MODFORMATIVO, ConstantesMeLanbide45.FICHERO_PROPIEDADES)
                    + " SET MDF_COD='" + nuevoDato.getCodMod() + "'"
                    + ", MDF_UC_COD='" + nuevoDato.getCodUC() + "'"
                    + " WHERE MDF_NUM='" + nuevoDato.getNumExp() + "' AND ID=" + nuevoDato.getId();
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            int insert = st.executeUpdate(query);
            return insert > 0;

        } catch (SQLException ex) {
            //opeCorrecta = false;
            log.debug("Se ha producido un error al Modificar Modulo Formativo" + " - "
                    + nuevoDato.getNumExp() + " - " + nuevoDato.getId() + " - " + ex.getMessage() + ex);
            log.debug("sql = " + query);
            throw new Exception(ex);
            //return opeCorrecta;
        } finally {
            try {
                if (log.isDebugEnabled()) {
                    log.debug("Procedemos a cerrar el statement y el resultset");
                }
                if (st != null) {
                    st.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException e) {
                log.error("Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }
    }

    public boolean modificaMaterial(MaterialConsuVO nuevoDato, Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        //boolean opeCorrecta = true;
        String query = "";
        try {
            query = "UPDATE " + ConfigurationParameter.getParameter(ConstantesMeLanbide45.TABLA_RGEF_MATERIALCONSU, ConstantesMeLanbide45.FICHERO_PROPIEDADES)
                    + " SET MAC_DET='" + nuevoDato.getDescripcion() + "'"
                    + ", MAC_CANT=" + (nuevoDato.getCantidad() != null ? nuevoDato.getCantidad() : "null")
                    + " WHERE MAC_NUM='" + nuevoDato.getNumExp() + "' AND ID=" + nuevoDato.getId();
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            int insert = st.executeUpdate(query);
            return insert > 0;

        } catch (SQLException ex) {
            //opeCorrecta = false;
            log.debug("Se ha producido un error al Modificar Modulo Formativo" + " - "
                    + nuevoDato.getNumExp() + " - " + nuevoDato.getId() + " - " + ex.getMessage() + ex);
            throw new Exception(ex);
            //return opeCorrecta;
        } finally {
            try {
                if (log.isDebugEnabled()) {
                    log.debug("Procedemos a cerrar el statement y el resultset");
                }
                if (st != null) {
                    st.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException e) {
                log.error("Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }
    }

    public boolean modificarEspacioForm(EspacioFormVO nuevoDato, Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        //boolean opeCorrecta = true;
        String query = "";
        try {
            query = "UPDATE " + ConfigurationParameter.getParameter(ConstantesMeLanbide45.TABLA_RGEF_ESPFORMATIVO, ConstantesMeLanbide45.FICHERO_PROPIEDADES)
                    + " SET EPF_DES='" + nuevoDato.getDescripcion() + "'"
                    + ", EPF_SUP=" + (nuevoDato.getSuperficie() != null ? nuevoDato.getSuperficie() : "null")
                    + " WHERE EPF_NUM='" + nuevoDato.getNumExp() + "' AND ID=" + nuevoDato.getId();
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            int insert = st.executeUpdate(query);
            return insert > 0;

        } catch (SQLException ex) {
            //opeCorrecta = false;
            log.debug("Se ha producido un error al Modificar Espacio Formativo" + " - "
                    + nuevoDato.getNumExp() + " - " + nuevoDato.getId() + " - " + ex.getMessage() + ex);
            throw new Exception(ex);
            //return opeCorrecta;
        } finally {
            try {
                if (log.isDebugEnabled()) {
                    log.debug("Procedemos a cerrar el statement y el resultset");
                }
                if (st != null) {
                    st.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException e) {
                log.error("Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }
    }

    /**
     * Metodos dalete datos
     *
     * @param numExp
     * @param id
     * @param con
     * @return int
     * @throws java.lang.Exception
     *
     */
    public int eliminarModuloForm(String numExp, Integer id, Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        try {
            String query = null;
            query = "DELETE FROM " + ConfigurationParameter.getParameter(ConstantesMeLanbide45.TABLA_RGEF_MODFORMATIVO, ConstantesMeLanbide45.FICHERO_PROPIEDADES)
                    + " WHERE MDF_NUM= '" + numExp + "' AND ID=" + (id != null ? id : "null");
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            return st.executeUpdate(query);
        } catch (SQLException ex) {
            log.error("Se ha producido un error Eliminando Modulo Formativo " + numExp + " - " + id, ex);
            throw new Exception(ex);
        } finally {
            try {
                if (log.isDebugEnabled()) {
                    log.debug("Procedemos a cerrar el statement y el resultset");
                }
                if (st != null) {
                    st.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException e) {
                log.error("Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }
    }

    public int eliminarMaterialesConsu(String numExp, Integer id, Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        try {
            String query = null;
            query = "DELETE FROM " + ConfigurationParameter.getParameter(ConstantesMeLanbide45.TABLA_RGEF_MATERIALCONSU, ConstantesMeLanbide45.FICHERO_PROPIEDADES)
                    + " WHERE MAC_NUM= '" + numExp + "' AND ID=" + (id != null ? id : "null");
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            return st.executeUpdate(query);
        } catch (SQLException ex) {
            log.error("Se ha producido un error Eliminando Modulo Formativo " + numExp + " - " + id, ex);
            throw new Exception(ex);
        } finally {
            try {
                if (log.isDebugEnabled()) {
                    log.debug("Procedemos a cerrar el statement y el resultset");
                }
                if (st != null) {
                    st.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException e) {
                log.error("Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }
    }

    public int eliminarEspacioForm(String numExp, Integer id, Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        try {
            String query = null;
            query = "DELETE FROM " + ConfigurationParameter.getParameter(ConstantesMeLanbide45.TABLA_RGEF_ESPFORMATIVO, ConstantesMeLanbide45.FICHERO_PROPIEDADES)
                    + " WHERE EPF_NUM= '" + numExp + "' AND ID=" + (id != null ? id : "null");
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            return st.executeUpdate(query);
        } catch (SQLException ex) {
            log.error("Se ha producido un error Eliminando Espacio Formativo " + numExp + " - " + id, ex);
            throw new Exception(ex);
        } finally {
            try {
                if (log.isDebugEnabled()) {
                    log.debug("Procedemos a cerrar el statement y el resultset");
                }
                if (st != null) {
                    st.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException e) {
                log.error("Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }
    }

    /**
     * Recupera el documento de un interesado que tenga el rol por defecto en el
     * expediente, y además se comprueba que su documento sea un CIF (tipo
     * documento 4 o 5 en Flexia) Si hay más de un interesado se recupera uno de
     * ellos, aunque esto no debería pasar.
     *
     * @param codOrganizacion: Código de la organización
     * @param numExpediente: Número del expediente
     * @param con: Conexión a la BBDD
     * @return String con el documento o null en caso contrario
     * @throws
     * es.altia.flexia.integracion.moduloexterno.melanbide45.util.MeLanbide45Exception
     */
    public String getDocumentoInteresadoRolDefecto(int codOrganizacion, String numExpediente, Connection con) throws MeLanbide45Exception {
        String documento = null;
        ResultSet rs = null;
        PreparedStatement ps = null;

        String[] datos = numExpediente.split("/");
        String ejercicio = datos[0];
        String codProcedimiento = datos[1];

        String sql = "SELECT HTE_DOC,HTE_TID,HTE_DOC FROM E_EXT,T_HTE,E_ROL WHERE EXT_MUN=? AND EXT_NUM=? AND EXT_EJE=? AND EXT_PRO=? "
                //+"AND EXT_TER=T_HTE.HTE_TER AND (HTE_TID=4 OR HTE_TID=5) "
                + "AND EXT_NVR=T_HTE.HTE_NVR AND ROL_MUN=? AND ROL_MUN=EXT_MUN AND "
                + "ROL_PRO=EXT_PRO AND ROL_PRO=EXT_PRO AND ROL_PDE=1 AND ROL_COD=EXT_ROL";
        log.debug("getDocumentoInteresadoRolDefecto sql: " + sql);
        log.debug("getDocumentoInteresadoRolDefecto param1: " + codOrganizacion + ",param2: " + numExpediente + ",param3: " + ejercicio + ",codProcedimiento: " + codProcedimiento + ",codOrganizacion: " + codOrganizacion);

        try {
            ps = con.prepareStatement(sql);
            int i = 1;
            ps.setInt(i++, codOrganizacion);
            ps.setString(i++, numExpediente);
            ps.setInt(i++, Integer.parseInt(ejercicio));
            ps.setString(i++, codProcedimiento);
            ps.setInt(i++, codOrganizacion);
            rs = ps.executeQuery();
            while (rs.next()) {
                documento = rs.getString("HTE_DOC");
            }

        } catch (NumberFormatException e) {
            throw new MeLanbide45Exception("Error al recuperar el documento del interesado con rol por defecto en expediente: " + numExpediente + ": " + e.getMessage());
        } catch (SQLException e) {
            throw new MeLanbide45Exception("Error al recuperar el documento del interesado con rol por defecto en expediente: " + numExpediente + ": " + e.getMessage());
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException e) {
            }
        }
        return documento;
    }

    /**
     * Recupera el código de centro que se busca a través de un CIF
     *
     * @param documento: String que contiene el CIF
     * @param con: Conexión a la BBDD
     * @return ArrayList String: Colección con los centros homologados
     * @throws MeLanbide45Exception si ocurre algún error
     */
    public ArrayList<String> getCodCentro(String documento, Connection con) throws MeLanbide45Exception {
        ArrayList<String> centros = new ArrayList<String>();
        PreparedStatement ps = null;
        ResultSet rs = null;
        String sql = null;
        try {
            sql = "SELECT CENTROS.GEN_CEN_COD_CENTRO "
                    + " FROM GEN_EMPRESARIO, " + ConfigurationParameter.getParameter(ConstantesMeLanbide45.TABLA_CENFOR_CENTROS, ConstantesMeLanbide45.FICHERO_PROPIEDADES) + " CENTROS "
                    + " WHERE GEN_USU_NUM_DOC=?"
                    + " AND GEN_EMPRESARIO.GEN_EMP_CORR = CENTROS.GEN_EMP_CORR"
                    + " AND CENTROS.GEN_CEN_ESTADO ='HO'";
            log.debug("getCodCentro sql = " + sql);
            log.debug("getCodCentro parametro = " + documento);
            ps = con.prepareStatement(sql);
            int i = 1;
            ps.setString(i++, documento);

            rs = ps.executeQuery();
            while (rs.next()) {
                centros.add(rs.getString("GEN_CEN_COD_CENTRO"));
            }
        } catch (SQLException e) {
            throw new MeLanbide45Exception("Error al recuperar el código de centro: " + e.getMessage());
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException e) {
            }
        }
        return centros;
    }

    /**
     * Recupera las ubicaciones de un centro
     *
     * @param codCentro
     * @param con: Conexión a la BBDD
     * @return RespuestaUbicacionesVO: Objeto instancia de la clase
     * RespuestaUbicacionesVO con las ubicaciones y en caso contrario, con el
     * código de error producido
     * @throws MeLanbide45Exception
     */
    public ArrayList<UbicacionVO> getUbicacionesCentro(String codCentro, Connection con) throws MeLanbide45Exception {
        ArrayList<UbicacionVO> ubicaciones = new ArrayList<UbicacionVO>();
        ResultSet rs = null;
        PreparedStatement ps = null;
        String sql = null;
        try {
            sql = "SELECT UBIC.GEN_CEN_COD_UBIC AS CODUBICACION, UBIC.GEN_CEN_NOM_UBIC,"
                    + " UBIC.GEN_GRAL_TIPO_VIA, UBIC.GEN_CEN_NOM_VIA, UBIC.GEN_CEN_NUM_VIA,"
                    + " UBIC.GEN_GRAL_BISDUPLICADO, UBIC.GEN_CEN_ESCALERA, UBIC.GEN_CEN_PISO,"
                    + " UBIC.GEN_CEN_PUERTA, UBIC.GEN_PRO_COD_PROV, UBIC.GEN_MUN_COD_MUN,"
                    + " UBIC.GEN_COD_CODPOSTAL"
                    + " FROM " + ConfigurationParameter.getParameter(ConstantesMeLanbide45.TABLA_CENFOR_UBICACION, ConstantesMeLanbide45.FICHERO_PROPIEDADES) + " UBIC"
                    + " JOIN " + ConfigurationParameter.getParameter(ConstantesMeLanbide45.TABLA_CENFOR_SUBSERV, ConstantesMeLanbide45.FICHERO_PROPIEDADES) + " SUBSERV"
                    + " ON UBIC.GEN_CEN_COD_CENTRO = SUBSERV.GEN_CEN_COD_CENTRO"
                    + " AND UBIC.GEN_CEN_COD_UBIC = SUBSERV.GEN_CEN_COD_UBIC"
                    + " WHERE UBIC.GEN_CEN_COD_CENTRO=? "
                    + " AND UBIC.GEN_CEN_ESTADO ='HO'";

            log.debug("getUbicacionesCentro sql: " + sql);
            log.debug("getUbicacionesCentro param: " + codCentro);
            ps = con.prepareStatement(sql);
            int i = 1;
            ps.setString(i++, codCentro);
            rs = ps.executeQuery();

            Hashtable<String, String> provincias = new Hashtable<String, String>();
            Hashtable<String, String> municipios = new Hashtable<String, String>();

            while (rs.next()) {
                String codUbicacion = rs.getString("CODUBICACION");
                String nombreUbicacion = rs.getString("GEN_CEN_NOM_UBIC");
                String tipoVia = rs.getString("GEN_GRAL_TIPO_VIA");
                String nombreCalle = rs.getString("GEN_CEN_NOM_VIA");
                String numeroCalle = rs.getString("GEN_CEN_NUM_VIA");
                String bis = rs.getString("GEN_GRAL_BISDUPLICADO");
                String escalera = rs.getString("GEN_CEN_ESCALERA");
                String piso = rs.getString("GEN_CEN_PISO");
                String puerta = rs.getString("GEN_CEN_PUERTA");
                String codProvincia = rs.getString("GEN_PRO_COD_PROV");
                String codMunicipio = rs.getString("GEN_MUN_COD_MUN");
                String codPostal = rs.getString("GEN_COD_CODPOSTAL");

                UbicacionVO ubicacion = new UbicacionVO();
                ubicacion.setCodCentro(codCentro);
                ubicacion.setNombreUbicacion(nombreUbicacion);
                ubicacion.setCodUbicacion(codUbicacion);
                ubicacion.setNombreCalle(nombreCalle);
                ubicacion.setNumeroCalle(numeroCalle);
                ubicacion.setBis(bis);
                ubicacion.setEscalera(escalera);
                ubicacion.setPiso(piso);
                ubicacion.setPuerta(puerta);
                ubicacion.setTipoCalle(tipoVia);
                ubicacion.setCodProvincia(codProvincia);
                ubicacion.setCodMunicipio(codMunicipio);
                ubicacion.setCodPostal(codPostal);

                if (codProvincia != null && codMunicipio != null && MeLanbide45Utils.isInteger(codProvincia) && MeLanbide45Utils.isInteger(codMunicipio)) {

                    Integer iProvincia = new Integer(codProvincia);
                    //Integer iMunicipio = new Integer(codMunicipio);

                    if (provincias.contains(new Integer(codProvincia))) {
                        ubicacion.setDescProvincia(provincias.get(codProvincia));
                    } else {
                        String descProvincia = getDescProvincia(108, iProvincia, con);
                        if (descProvincia != null) {
                            ubicacion.setDescProvincia(descProvincia);
                            provincias.put(iProvincia.toString(), descProvincia);
                        } else {
                            ubicacion.setDescProvincia("-");
                        }
                    }

                    String aux = codMunicipio.substring(2, codMunicipio.length());
                    Integer iMunicipio = new Integer(aux);

                    if (municipios.contains(iProvincia + "-" + iMunicipio)) {
                        ubicacion.setDescMunicipio(municipios.get(iProvincia + "-" + iMunicipio));
                    } else {
                        String descMunicipio = getDescMunicipio(108, iProvincia, iMunicipio, con);
                        if (descMunicipio != null) {
                            ubicacion.setDescMunicipio(descMunicipio);
                            municipios.put(iProvincia + "-" + iMunicipio, descMunicipio);
                        } else {
                            ubicacion.setDescMunicipio("-");
                        }
                    }
                }
                ubicaciones.add(ubicacion);
            }

        } catch (MeLanbide45Exception e) {
            throw new MeLanbide45Exception("Error al recuperar las ubicaciones del centro con código " + codCentro + " :" + e.getMessage());
        } catch (NumberFormatException e) {
            throw new MeLanbide45Exception("Error al recuperar las ubicaciones del centro con código " + codCentro + " :" + e.getMessage());
        } catch (SQLException e) {
            throw new MeLanbide45Exception("Error al recuperar las ubicaciones del centro con código " + codCentro + " :" + e.getMessage());
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException e) {
            }
        }
        return ubicaciones;
    }

    /**
     * Recupera la descripción de la provincia
     *
     * @param codPais: Código del pais
     * @param codProvincia: Código de la provincia
     * @param con: Conexión a la BBDD
     * @return String con al descripción
     * @throws MeLanbide45Exception si ocurre algún error
     */
    public String getDescProvincia(int codPais, int codProvincia, Connection con) throws MeLanbide45Exception {
        String salida = null;
        Statement st = null;
        ResultSet rs = null;

        try {
            String sql = "SELECT PRV_NOM FROM " + GlobalNames.ESQUEMA_GENERICO + "T_PRV "
                    + "WHERE PRV_PAI=" + codPais + " AND PRV_COD=" + codProvincia;

            st = con.createStatement();
            rs = st.executeQuery(sql);

            while (rs.next()) {
                salida = rs.getString("PRV_NOM");
            }

        } catch (SQLException e) {
            throw new MeLanbide45Exception("Error al recuperar la descripción de la provincia: " + e.getMessage());
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (st != null) {
                    st.close();
                }

            } catch (SQLException e) {
            }
        }
        return salida;
    }

    /**
     * Recupera la descripción de un municipio
     *
     * @param codPais: código del pais
     * @param codProvincia: Código de la provincia
     * @param codMunicipio: Códigio del municipio
     * @param con: Conexión a la BBDD
     * @return String con al descripción
     * @throws MeLanbide45Exception si ocurre algún error
     */
    public String getDescMunicipio(int codPais, int codProvincia, int codMunicipio, Connection con) throws MeLanbide45Exception {
        String salida = null;
        Statement st = null;
        ResultSet rs = null;

        try {
            String sql = "SELECT MUN_NOM FROM " + GlobalNames.ESQUEMA_GENERICO + "T_MUN "
                    + "WHERE MUN_PAI=" + codPais + " AND MUN_PRV=" + codProvincia + " AND MUN_COD=" + codMunicipio;

            st = con.createStatement();
            rs = st.executeQuery(sql);

            while (rs.next()) {
                salida = rs.getString("MUN_NOM");
            }

        } catch (SQLException e) {
            throw new MeLanbide45Exception("Error al recuperar la descripción del municipio: " + e.getMessage());
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (st != null) {
                    st.close();
                }

            } catch (SQLException e) {
            }
        }
        return salida;
    }

    /**
     * Recupera el valor de un campo suplementario desplegable externo para un
     * determinado expediente
     *
     * @param codOrganizacion: Código de la organización
     * @param numExpediente: Número del expediente
     * @param codCampo: Código del campo
     * @param con: Conexión a la BBDD
     * @return String con el valor o null sino se ha podido recuperar
     * @throws Exception
     */
    public String getValorCampoDesplegableExternoExpediente(int codOrganizacion, String numExpediente, String codCampo, Connection con) throws Exception {
        String salida = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        //boolean opeCorrecta = true;
        String query = "";

        try {
            String[] datos = numExpediente.split("/");
            String ejercicio = datos[0];

            query = "SELECT TDEX_VALOR FROM E_TDEX "
                    + "WHERE TDEX_NUM=? AND TDEX_EJE=? AND TDEX_MUN=? AND TDEX_COD=?";

            if (log.isDebugEnabled()) {
                log.info("sql = " + query);
            }
            int i = 1;
            ps = con.prepareStatement(query);
            ps.setString(i++, numExpediente);
            ps.setInt(i++, Integer.parseInt(ejercicio));
            ps.setInt(i++, codOrganizacion);
            ps.setString(i++, codCampo);

            rs = ps.executeQuery();
            while (rs.next()) {
                salida = rs.getString("TDEX_VALOR");
            }
        } catch (NumberFormatException ex) {
            log.error("Error al recuperar el valor del campo desplegable " + codCampo + " en el expediente " + numExpediente + ": " + ex.getMessage());
            throw ex;
        } catch (SQLException ex) {
            log.error("Error al recuperar el valor del campo desplegable " + codCampo + " en el expediente " + numExpediente + ": " + ex.getMessage());
            throw ex;
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException e) {
            }
        }
        return salida;
    }

    /**
     * Recupera el valor del desplegable
     *
     * @param codOrganizacion: Código de la organización
     * @param numExpediente: Número del expediente
     * @param codCampo: Código del campo
     * @param con: Conexión a la BBDD
     * @return String con el valor o null sino se ha podido recuperar
     * @throws Exception
     */
    public String getValorCodDesplegableExternoExpediente(int codOrganizacion, String numExpediente, String codCampo, Connection con) throws Exception {

        String desplegable = null;
        String descDesplegable = null;
        String codigo = null;
        String driver = null;
        String url = null;
        String usuario = null;
        String password = null;
        String tabla = null;
        String campoCodigo = null;
        String campoValor = null;
        Connection conExt = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        int i;
        String query = "";

        try {

            String[] datos = numExpediente.split("/");
            String ejercicio = datos[0];
            String procedimiento = datos[1];

            query = "SELECT TDEX_VALOR FROM E_TDEX "
                    + "WHERE TDEX_NUM=? AND TDEX_EJE=? AND TDEX_MUN=? AND TDEX_COD=?";

            if (log.isDebugEnabled()) {
                log.info("sql = " + query);
            }
            i = 1;
            ps = con.prepareStatement(query);
            ps.setString(i++, numExpediente);
            ps.setInt(i++, Integer.parseInt(ejercicio));
            ps.setInt(i++, codOrganizacion);
            ps.setString(i++, codCampo);

            rs = ps.executeQuery();
            while (rs.next()) {
                descDesplegable = rs.getString("TDEX_VALOR");
            }

            if (descDesplegable != null && descDesplegable.trim().length() > 0) {
                //Recupera el valor del desplegable
                query = "SELECT PCA_DESPLEGABLE FROM E_PCA "
                        + "WHERE PCA_MUN=? AND PCA_PRO=? AND PCA_COD=?";

                if (log.isDebugEnabled()) {
                    log.info("sql = " + query);
                }

                i = 1;
                ps = con.prepareStatement(query);
                ps.setInt(i++, codOrganizacion);
                ps.setString(i++, procedimiento);
                ps.setString(i++, codCampo);

                rs = ps.executeQuery();
                while (rs.next()) {
                    desplegable = rs.getString("PCA_DESPLEGABLE");
                }

                if (desplegable != null && desplegable.trim().length() > 0) {
                    //Recuperar la información para la conexión con la BD externa para recuperar el codigo del valor del deplegable
                    query = "SELECT DRIVER_JDBC,URL_JDBC,USUARIO,PASSWORD,TABLA,CAMPO_CODIGO,CAMPO_VALOR "
                            + "FROM DESPLEGABLE_EXTERNO "
                            + "WHERE CODIGO=?";

                    if (log.isDebugEnabled()) {
                        log.info("sql = " + query);
                    }

                    i = 1;
                    ps = con.prepareStatement(query);
                    ps.setString(i++, desplegable);

                    rs = ps.executeQuery();
                    while (rs.next()) {
                        driver = rs.getString("DRIVER_JDBC");
                        url = rs.getString("URL_JDBC");
                        usuario = rs.getString("USUARIO");
                        password = rs.getString("PASSWORD");
                        tabla = rs.getString("TABLA");
                        campoCodigo = rs.getString("CAMPO_CODIGO");
                        campoValor = rs.getString("CAMPO_VALOR");
                    }

                    //Obtenemos la conexion para conectarnos a la BD Externa
                    Class.forName(driver);
                    conExt = DriverManager.getConnection(url, usuario, password);

                    query = "SELECT " + campoCodigo + " FROM " + tabla
                            + " WHERE " + campoValor + "='" + descDesplegable + "'";

                    if (log.isDebugEnabled()) {
                        log.info("sql = " + query);
                    }

                    i = 1;
                    ps = conExt.prepareStatement(query);

                    rs = ps.executeQuery();
                    while (rs.next()) {
                        codigo = rs.getString(campoCodigo);
                    }
                }
            }

        } catch (ClassNotFoundException cnfe) {
            log.error("Error en la conexión con la BD " + cnfe.getMessage());
            throw cnfe;
        } catch (SQLException sqle) {
            log.error("Error en la conexión con la BD " + sqle.getMessage());
            throw sqle;
        } catch (NumberFormatException ex) {
            log.error("Error al recuperar el valor del codigo del campo desplegable externo " + codCampo + " en el expediente " + numExpediente + ": " + ex.getMessage());
            throw ex;
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                if (rs != null) {
                    rs.close();
                }
                if (conExt != null) {
                    conExt.close();
                }
            } catch (SQLException e) {
            }
        }

        return codigo;
    }

    public String getValorSiguienteCodArea(int codOrganizacion, String numExpediente, String area, Connection con) throws Exception {
        {
            String Query = "";
            PreparedStatement ps = null;
            ResultSet rs = null;
            String sigArea;
            String codNuevaEspecialidad = null;
            String error = null;

            try {
                Query = "select LPAD(SEQ_MELANBIDE45_CODESP.nextval,4,0) as sigArea from dual";
                //Query = "select LPAD(MAX(TO_NUMBER(SUBSTR(CODESPECIALIDAD,5,4 )))+ 1,4,0) as sigArea from FPE_ESP_ESPECIALIDADESCAT WHERE CODAREA='" + area +"'";
                if (log.isDebugEnabled()) {
                    log.info("sql = " + Query);
                }
                ps = con.prepareStatement(Query);
                rs = ps.executeQuery();
                rs.next();
                sigArea = rs.getString("sigArea");
            } catch (SQLException ex) {
                log.error("Error al recuperar el valor sigArea para area: " + area + " en el expediente " + numExpediente + ":  Query -> " + Query + " " + ex.getMessage());
                error = "1";
                throw ex;
            } finally {
                if (ps != null) {
                    ps.close();
                }
                if (rs != null) {
                    rs.close();
                }
            }
            if (error == null) {
                codNuevaEspecialidad = area + sigArea + "V";
                log.debug("Codigo Nueva Especialidad generado-> " + codNuevaEspecialidad);
            }

            return codNuevaEspecialidad;
        }

    }

    public String getValorSiguienteUC(int codOrganizacion, String numExpediente, String nivel, Connection con) throws Exception {
        {
            String Query = "";
            PreparedStatement ps = null;
            ResultSet rs = null;
            String error = null;
            String codUC = null;
            String sigUC;

            // Buscamos las tablas donde estan UC
            try {
                Query = "select LPAD(SEQ_MELANBIDE45_UC.nextval,4,0) as sigUC from dual";
                if (log.isDebugEnabled()) {
                    log.info("sql = " + Query);
                }
                ps = con.prepareStatement(Query);
                rs = ps.executeQuery();
                rs.next();
                sigUC = rs.getString("sigUC");
            } catch (SQLException ex) {
                log.error("Error al recuperar el valor sigUC en el expediente " + numExpediente + ":  Query -> " + Query + " " + ex.getMessage());
                error = "1";
                throw ex;
            } finally {
                if (ps != null) {
                    ps.close();
                }
                if (rs != null) {
                    rs.close();
                }
            }

            if (error == null) {
                codUC = "UC" + sigUC + "_" + nivel;
                log.debug("Codigo UC generado-> " + codUC);
            }

            return codUC;
        }
    }

    public String getValorSiguienteMF(int codOrganizacion, String numExpediente, String nivel, Connection con) throws Exception {
        {
            String Query = "";
            PreparedStatement ps = null;
            ResultSet rs = null;
            String error = null;
            String codMF = null;
            String sigMF;

            try {
                Query = "select LPAD(SEQ_MELANBIDE45_MF.nextval,4,0) as sigMF from dual";
                if (log.isDebugEnabled()) {
                    log.info("sql = " + Query);
                }
                ps = con.prepareStatement(Query);
                rs = ps.executeQuery();
                rs.next();
                sigMF = rs.getString("sigMF");
            } catch (SQLException ex) {
                log.error("Error al recuperar el valor sigMF en el expediente " + numExpediente + "  Query -> " + Query + " " + ex.getMessage());
                error = "1";
                throw ex;
            } finally {
                if (ps != null) {
                    ps.close();
                }
                if (rs != null) {
                    rs.close();
                }
            }

            if (error == null) {
                codMF = "MF" + sigMF + "_" + nivel;
                log.debug("Codigo MF generado-> " + codMF);
            }

            return codMF;
        }
    }

    public boolean crearCodigoModifEspecialidad(String numExp, int mun, String valor, Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        String query = "";
        boolean exito = false;
        try {
            query = "INSERT INTO E_TXT (TXT_MUN, TXT_EJE, TXT_NUM, TXT_COD, TXT_VALOR) "
                    + " VALUES (" + mun
                    + ",'" + Integer.parseInt(numExp.substring(0, numExp.indexOf('/'))) + "'"
                    + ",'" + numExp + "'"
                    + ",'CODESP'"
                    + ",'" + (valor != null ? valor : "null") + "'"
                    + ")";
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            int insert = st.executeUpdate(query);
            exito = insert > 0;

        } catch (NumberFormatException ex) {
            log.debug("Se ha producido un error al Insertar un nuevo Registro en crearCodigoEspecialidad-> " + "Query: " + query + "  " + ex.getMessage() + ex);
            {
                try {
                    query = "UPDATE E_TXT SET TXT_VALOR= "
                            + " '" + (valor != null ? valor : "null") + "'"
                            + " WHERE TXT_NUM = '" + numExp + "'' AND TXT_MUN = " + mun + " AND TXT_COD = 'CODESP'";
                    if (log.isDebugEnabled()) {
                        log.debug("sql = " + query);
                    }

                    st = con.createStatement();
                    int update = st.executeUpdate(query);
                    exito = update > 0;
                } catch (SQLException exx) {
                    exito = false;
                    log.debug("Se ha producido un error al Updatear un nuevo Registro en crearCodigoEspecialidad-> " + "Query: " + query + "  " + exx.getMessage() + exx);
                    throw exx;
                }

            }
            throw ex;

        } catch (SQLException ex) {
            log.debug("Se ha producido un error al Insertar un nuevo Registro en crearCodigoEspecialidad-> " + "Query: " + query + "  " + ex.getMessage() + ex);
            {
                try {
                    query = "UPDATE E_TXT SET TXT_VALOR= "
                            + " '" + (valor != null ? valor : "null") + "'"
                            + " WHERE TXT_NUM = '" + numExp + "'' AND TXT_MUN = " + mun + " AND TXT_COD = 'CODESP'";
                    if (log.isDebugEnabled()) {
                        log.debug("sql = " + query);
                    }

                    st = con.createStatement();
                    int update = st.executeUpdate(query);
                    exito = update > 0;
                } catch (SQLException exx) {
                    exito = false;
                    log.debug("Se ha producido un error al Updatear un nuevo Registro en crearCodigoEspecialidad-> " + "Query: " + query + "  " + exx.getMessage() + exx);
                    throw exx;
                }

            }
            throw ex;
        } finally {
            try {
                if (log.isDebugEnabled()) {
                    log.debug("Procedemos a cerrar el statement y el resultset");
                }
                if (st != null) {
                    st.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException e) {
                log.error("Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }
        return exito;
    }

    /**
     *
     * Función que obtiene el número de censo SILICOI de base de datos
     *
     * @param codCentro: Código del centro
     * @param codUbicacion: Código de la ubicación
     * @param corrSubservicio
     * @param con: Conexion
     * @return String con el numero de censo SILICOI
     * @throws java.lang.Exception
     */
    public String getNumeroCensoSILICOI(String codCentro, String codUbicacion, String corrSubservicio, Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        String censo = "";
        try {
            String query = null;
            query = "select NCENSO_SILCOI_CONCAT from FLBGEN.VW_CENTROS_REG "
                    + " where GEN_CEN_COD_CENTRO  = '" + codCentro + "'"
                    + " AND GEN_CEN_COD_UBIC = '" + codUbicacion + "'"
                    + " AND CEN_CENTRO_SUBSERVIC_CORR = '" + corrSubservicio + "'";
            if (log.isDebugEnabled()) {
                log.error("sql = " + query);
            }
            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                censo = rs.getString("NCENSO_SILCOI_CONCAT");
            }
        } catch (SQLException ex) {
            log.error("Se ha producido un error recuperando Especialidades Solicitadas", ex);
            throw new Exception(ex);
        } finally {
            try {
                if (st != null) {
                    st.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException e) {
                log.error("Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }
        return censo;
    }

    /**
     *
     * Función para obtener el número de censo de un centro
     *
     * @param codCentro: Código del centro
     * @param codUbicacion: Código de la ubicación
     * @param con: conexión
     * @return
     * @throws java.lang.Exception
     */
    public String getNumeroCensoLanbide(String codCentro, String codUbicacion, Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        List<EspecialidadesVO> listEsp = new ArrayList<EspecialidadesVO>();
        EspecialidadesVO Especialidad = new EspecialidadesVO();
        String censo = "";
        try {
            String query = null;
            query = "select LAN_NUM_REG_AUT from FLBGEN.LAN_CENTROS_SERVICIOS "
                    + " where GEN_CEN_COD_CENTRO  = '" + codCentro + "' AND GEN_CEN_COD_UBIC = '" + codUbicacion + "' AND LAN_UBIC_SERVICIO_LMV='314'";
            if (log.isDebugEnabled()) {
                log.error("sql getNumeroCensoLanbide = " + query);
            }
            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                censo = rs.getString("LAN_NUM_REG_AUT");
            }
        } catch (SQLException ex) {
            log.error("Se ha producido un error recuperando Especialidades Solicitadas", ex);
            throw new Exception(ex);
        } finally {
            try {
                if (st != null) {
                    st.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException e) {
                log.error("Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }
        return censo;
    }

    /**
     * Recupera los datos necesarios para pasar al WS de Registro Centros
     *
     * @param codCentro codigo del Centro
     * @param codUbic codigo de Ubicacion del centro
     * @param con
     * @return String[] con corrServicio y corrSubservicio
     * @throws Exception
     */
    public String[] getCodigosCorr(String codCentro, String codUbic, Connection con) throws Exception {
        log.debug("Entra en getCodigosCorr de " + this.getClass().getSimpleName() + " - Centro: " + codCentro + " - Ubicación: " + codUbic);
        Statement st = null;
        ResultSet rs = null;
        String query = null;
        String[] codigosCorr = new String[2];
        try {
            query = "SELECT gen_cen_servicio_corr,gen_cen_subservicio_corr FROM " + ConfigurationParameter.getParameter(ConstantesMeLanbide45.TABLA_CENFOR_SUBSERV, ConstantesMeLanbide45.FICHERO_PROPIEDADES)
                    + " WHERE GEN_CEN_COD_CENTRO = '" + codCentro + "'"
                    + " AND GEN_CEN_ESTADO = 'HO'"
                    + " AND gen_cen_cod_ubic='" + codUbic + "'";
            log.debug("query: " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
//            while (rs.next()) {
            if (rs.next()) {
                codigosCorr[0] = rs.getString("gen_cen_servicio_corr");
                codigosCorr[1] = rs.getString("gen_cen_subservicio_corr");
                log.debug("corr_ser: " + codigosCorr[0]);
                log.debug("corr_SUB: " + codigosCorr[1]);
            }
        } catch (SQLException e) {
            log.error("Se ha producido un error recuperando codigos Servicio y SubServicio: ", e);
            throw new Exception(e);
        } finally {
            if (st != null) {
                st.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        return codigosCorr;
    }

    /**
     *
     * Metodo que obtiene el numero de censo de un centro
     *
     * @param codCentro Código del centro
     * @param codUbic Código de la ubicación
     * @param corrSubservicio corrSubservicio
     * @param con Conexion
     * @return String con el numero Censo CENFOR
     * @throws Exception
     */
    public String getNumeroCensoCenFor(String codCentro, String codUbic, String corrSubservicio, Connection con) throws Exception {
        log.debug("Entra en getNumeroCensoCenFor de " + this.getClass().getSimpleName() + " - Centro: " + codCentro + " - Ubicación: " + codUbic + " - corrSubservicio: " + corrSubservicio);
        String numCenso = null;
        Statement st = null;
        ResultSet rs = null;
        String query = null;
        try {
            query = " select CEN_NUM_REG_AUT from " + ConfigurationParameter.getParameter(ConstantesMeLanbide45.TABLA_CENFOR_SUBSERV, ConstantesMeLanbide45.FICHERO_PROPIEDADES)
                    + " WHERE GEN_CEN_COD_CENTRO = '" + codCentro + "'"
                    + " AND GEN_CEN_COD_UBIC = '" + codUbic + "'"
                    + " AND GEN_CEN_SUBSERVICIO_CORR   = '" + corrSubservicio + "'";
            log.debug("query: " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                numCenso = rs.getString("CEN_NUM_REG_AUT");
            }
        } catch (SQLException e) {
            log.error("Se ha producido un error recuperando codigos Servicio y SubServicio: ", e);
            throw new Exception(e);
        } finally {
            if (st != null) {
                st.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        return numCenso;
    }

}

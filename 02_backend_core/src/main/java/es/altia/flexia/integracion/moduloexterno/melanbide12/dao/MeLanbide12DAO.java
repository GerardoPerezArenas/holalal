package es.altia.flexia.integracion.moduloexterno.melanbide12.dao;

import es.altia.flexia.integracion.moduloexterno.melanbide12.util.ConfigurationParameter;
import es.altia.flexia.integracion.moduloexterno.melanbide12.util.ConstantesMeLanbide12;
import es.altia.flexia.integracion.moduloexterno.melanbide12.util.MeLanbide12MappingUtils;
import es.altia.flexia.integracion.moduloexterno.melanbide12.vo.DesplegableAdmonLocalVO;
import es.altia.flexia.integracion.moduloexterno.melanbide12.vo.FilaMinimisVO;
import es.altia.flexia.integracion.moduloexterno.melanbide12.vo.FilaL1ParticipanteVO;
import es.altia.flexia.integracion.moduloexterno.melanbide12.vo.FilaL1EmpresaExternaVO;
import es.altia.flexia.integracion.moduloexterno.melanbide12.vo.FilaL1EmpresaPropiaVO;
import es.altia.flexia.integracion.moduloexterno.melanbide12.vo.FilaL2ParticipanteVO;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

public class MeLanbide12DAO {

    //Logger
    private static final Logger log = LogManager.getLogger(MeLanbide12DAO.class);
    //Instancia
    private static MeLanbide12DAO instance = null;

    // Constructor
    private MeLanbide12DAO() {

    }

    public static MeLanbide12DAO getInstance() {
        if (instance == null) {
            synchronized (MeLanbide12DAO.class) {
                instance = new MeLanbide12DAO();
            }
        }
        return instance;
    }

    public List<FilaMinimisVO> getDatosMinimis(String numExp, int codOrganizacion, Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        List<FilaMinimisVO> lista = new ArrayList<FilaMinimisVO>();
        FilaMinimisVO minimis = new FilaMinimisVO();
        try {
            String query = null;
            query = "SELECT * FROM " + ConfigurationParameter.getParameter(ConstantesMeLanbide12.MELANBIDE12_SUBSOLIC, ConstantesMeLanbide12.FICHERO_PROPIEDADES)
                    + " WHERE NUM_EXP ='" + numExp + "'"
                    + " ORDER BY ID";
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                minimis = (FilaMinimisVO) MeLanbide12MappingUtils.getInstance().map(rs, FilaMinimisVO.class);
                //Cargamos en el request los valores de los desplegables
                lista.add(minimis);
                minimis = new FilaMinimisVO();
            }
        } catch (Exception ex) {
            log.error("Se ha producido un error recuperando Minimis ", ex);
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

    public FilaMinimisVO getMinimisPorID(String id, Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        FilaMinimisVO minimis = new FilaMinimisVO();
        try {
            String query = null;
            query = "SELECT * FROM " + ConfigurationParameter.getParameter(ConstantesMeLanbide12.MELANBIDE12_SUBSOLIC, ConstantesMeLanbide12.FICHERO_PROPIEDADES)
                    + " WHERE ID=" + (id != null && !id.equals("") ? id : "null");
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                minimis = (FilaMinimisVO) MeLanbide12MappingUtils.getInstance().map(rs, FilaMinimisVO.class);
            }
        } catch (Exception ex) {
            log.error("Se ha producido un error recuperando una Minimis : " + id, ex);
            throw new Exception(ex);
        } finally {
            if (st != null) {
                st.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        return minimis;
    }

    public int eliminarMinimis(String id, Connection con) throws Exception {
        Statement st = null;
        try {
            String query = null;
            query = "DELETE FROM " + ConfigurationParameter.getParameter(ConstantesMeLanbide12.MELANBIDE12_SUBSOLIC, ConstantesMeLanbide12.FICHERO_PROPIEDADES)
                    + " WHERE ID=" + (id != null && !id.equals("") ? id : "null");
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            return st.executeUpdate(query);
        } catch (SQLException ex) {
            log.error("Se ha producido un error Eliminando Minimis ID : " + id, ex);
            throw new Exception(ex);
        } finally {
            if (st != null) {
                st.close();
            }
        }
    }

    public boolean crearNuevoMinimis(FilaMinimisVO nuevaMinimis, Connection con) throws Exception {
        Statement st = null;
        String query = "";
        String fechaSub = "";
        if (nuevaMinimis != null && nuevaMinimis.getFecha() != null && !nuevaMinimis.getFecha().equals("")) {
            SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
            fechaSub = formatoFecha.format(nuevaMinimis.getFecha());
        }
        try {

            int id = recogerIDInsertar(ConfigurationParameter.getParameter(ConstantesMeLanbide12.SEQ_MELANBIDE12_SUBSOLIC, ConstantesMeLanbide12.FICHERO_PROPIEDADES), con);
            query = "INSERT INTO " + ConfigurationParameter.getParameter(ConstantesMeLanbide12.MELANBIDE12_SUBSOLIC, ConstantesMeLanbide12.FICHERO_PROPIEDADES)
                    + "(ID,NUM_EXP,ESTADO,ORGANISMO,OBJETO,IMPORTE,FECHA) "
                    + " VALUES (" + id
                    + ", '" + nuevaMinimis.getNumExp()
                    + "', '" + nuevaMinimis.getEstado()
                    + "', '" + nuevaMinimis.getOrganismo()
                    + "', '" + nuevaMinimis.getObjeto()
                    + "', " + nuevaMinimis.getImporte()
                    + " , TO_DATE('" + fechaSub + "','dd/mm/yyyy')"
                    + ")";
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            int insert = st.executeUpdate(query);
            if (insert > 0) {
                return true;
            } else {
                log.error("No Se ha podido guardar una nueva Minimis ");
                return false;
            }

        } catch (Exception ex) {
            log.error("Se ha producido un error al insertar una nueva Minimis" + ex.getMessage());
            throw new Exception(ex);
        } finally {
            if (st != null) {
                st.close();
            }
        }
    }

    public boolean modificarMinimis(FilaMinimisVO datModif, Connection con) throws Exception {
        Statement st = null;
        String query = "";
        String fechaSub = "";
        if (datModif != null && datModif.getFecha() != null && !datModif.getFecha().toString().equals("")) {
            SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
            fechaSub = formatoFecha.format(datModif.getFecha());
        }

        try {
            query = "UPDATE " + ConfigurationParameter.getParameter(ConstantesMeLanbide12.MELANBIDE12_SUBSOLIC, ConstantesMeLanbide12.FICHERO_PROPIEDADES)
                    + " SET ESTADO='" + datModif.getEstado() + "'"
                    + ", ORGANISMO='" + datModif.getOrganismo() + "'"
                    + ", OBJETO='" + datModif.getObjeto() + "'"
                    + ", IMPORTE=" + datModif.getImporte()
                    + ", FECHA=TO_DATE('" + fechaSub + "','dd/mm/yyyy')"
                    + " WHERE ID=" + datModif.getId();
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            return st.executeUpdate(query) > 0;

        } catch (SQLException ex) {
            log.error("Se ha producido un error al modificar una Minimis - " + datModif.getId() + " - " + ex);
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

    /**
     *
     * @param des_cod
     * @param con
     * @return
     * @throws Exception
     */
    public List<DesplegableAdmonLocalVO> getValoresDesplegablesAdmonLocalxdes_cod(String des_cod, Connection con) throws Exception {
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<DesplegableAdmonLocalVO> lista = new ArrayList<DesplegableAdmonLocalVO>();
        DesplegableAdmonLocalVO valoresDesplegable = new DesplegableAdmonLocalVO();
        try {
            String query = null;
            query = "SELECT * FROM " + ConfigurationParameter.getParameter(ConstantesMeLanbide12.TABLA_VALORES_DESPLEGABLES, ConstantesMeLanbide12.FICHERO_PROPIEDADES)
                    + " WHERE DES_COD= ? order by DES_NOM";
            log.debug("sql = " + query);
            int i = 1;
            ps = con.prepareStatement(query);
            ps.setString(i++, des_cod);
            rs = ps.executeQuery();

            while (rs.next()) {
                valoresDesplegable = (DesplegableAdmonLocalVO) MeLanbide12MappingUtils.getInstance().map(rs, DesplegableAdmonLocalVO.class);
                lista.add(valoresDesplegable);
                valoresDesplegable = new DesplegableAdmonLocalVO();
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

    /**
     * Metodo que recupera el valor de un campo desplegabe de un expediente
     *
     * @param codOrg
     * @param numExp
     * @param codDesplegable
     * @param con
     * @return
     * @throws Exception
     */
    public String getValorDesplegableExpediente(int codOrg, String numExp, String codDesplegable, Connection con) throws Exception {
        log.info("getValorDesplegableExpediente - " + numExp + " - " + codDesplegable);
        PreparedStatement ps = null;
        ResultSet rs = null;
        String query = null;
        String valor = null;
        try {
            query = "SELECT TDE_VALOR FROM " + ConfigurationParameter.getParameter(ConstantesMeLanbide12.TABLA_DESPLEGABLES_EXP, ConstantesMeLanbide12.FICHERO_PROPIEDADES)
                    + " WHERE TDE_MUN=? AND TDE_EJE=? AND TDE_NUM=? AND TDE_COD=?";
            log.debug("sql = " + query);
            int i = 1;
            ps = con.prepareStatement(query);
            ps.setInt(i++, codOrg);
            ps.setString(i++, numExp.split(ConstantesMeLanbide12.BARRA_SEPARADORA)[0]);
            ps.setString(i++, numExp);
            ps.setString(i++, codDesplegable);
            rs = ps.executeQuery();
            while (rs.next()) {
                valor = rs.getString("TDE_VALOR");
            }
        } catch (SQLException ex) {
            log.error("Excepcion - getValorDesplegableExpediente : " + ex.getMessage(), ex);
            throw new Exception(ex);
        } finally {
            if (ps != null) {
                ps.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        return valor;
    }

    public List<FilaL1ParticipanteVO> getDatosL1Participante(String numExp, int codOrganizacion, Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        List<FilaL1ParticipanteVO> lista = new ArrayList<FilaL1ParticipanteVO>();
        FilaL1ParticipanteVO l1Participante = new FilaL1ParticipanteVO();
        try {
            String query = null;
            query = "SELECT * FROM " + ConfigurationParameter.getParameter(ConstantesMeLanbide12.MELANBIDE12_PERS_PRACT_LIN1, ConstantesMeLanbide12.FICHERO_PROPIEDADES)
                    + " WHERE NUM_EXP ='" + numExp + "'"
                    + " ORDER BY ID";
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                l1Participante = (FilaL1ParticipanteVO) MeLanbide12MappingUtils.getInstance().map(rs, FilaL1ParticipanteVO.class);
                //Cargamos en el request los valores de los desplegables
                lista.add(l1Participante);
                l1Participante = new FilaL1ParticipanteVO();
            }
        } catch (Exception ex) {
            log.error("Se ha producido un error recuperando L1Participante ", ex);
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

    public FilaL1ParticipanteVO getL1ParticipantePorID(String id, Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        FilaL1ParticipanteVO l1Participante = new FilaL1ParticipanteVO();
        try {
            String query = null;
            query = "SELECT * FROM " + ConfigurationParameter.getParameter(ConstantesMeLanbide12.MELANBIDE12_PERS_PRACT_LIN1, ConstantesMeLanbide12.FICHERO_PROPIEDADES)
                    + " WHERE ID=" + (id != null && !id.equals("") ? id : "null");
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                l1Participante = (FilaL1ParticipanteVO) MeLanbide12MappingUtils.getInstance().map(rs, FilaL1ParticipanteVO.class);
            }
        } catch (Exception ex) {
            log.error("Se ha producido un error recuperando una L1Participante : " + id, ex);
            throw new Exception(ex);
        } finally {
            if (st != null) {
                st.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        return l1Participante;
    }

    public int eliminarL1Participante(String id, Connection con) throws Exception {
        Statement st = null;
        try {
            String query = null;
            query = "DELETE FROM " + ConfigurationParameter.getParameter(ConstantesMeLanbide12.MELANBIDE12_PERS_PRACT_LIN1, ConstantesMeLanbide12.FICHERO_PROPIEDADES)
                    + " WHERE ID=" + (id != null && !id.equals("") ? id : "null");
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            return st.executeUpdate(query);
        } catch (SQLException ex) {
            log.error("Se ha producido un error Eliminando L1Participante ID : " + id, ex);
            throw new Exception(ex);
        } finally {
            if (st != null) {
                st.close();
            }
        }
    }

    public boolean crearNuevoL1Participante(FilaL1ParticipanteVO nuevoL1Participante, Connection con) throws Exception {
        Statement st = null;
        String query = "";
        String fechaIniPract = "";
        if (nuevoL1Participante != null && nuevoL1Participante.getFecIniPract() != null && !nuevoL1Participante.getFecIniPract().equals("")) {
            SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
            fechaIniPract = formatoFecha.format(nuevoL1Participante.getFecIniPract());
        }
        String fechaFinPract = "";
        if (nuevoL1Participante != null && nuevoL1Participante.getFecFinPract() != null && !nuevoL1Participante.getFecFinPract().equals("")) {
            SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
            fechaFinPract = formatoFecha.format(nuevoL1Participante.getFecFinPract());
        }
        try {

            int id = recogerIDInsertar(ConfigurationParameter.getParameter(ConstantesMeLanbide12.SEQ_M12_PERS_PRACT_LIN1, ConstantesMeLanbide12.FICHERO_PROPIEDADES), con);
            query = "INSERT INTO " + ConfigurationParameter.getParameter(ConstantesMeLanbide12.MELANBIDE12_PERS_PRACT_LIN1, ConstantesMeLanbide12.FICHERO_PROPIEDADES)
                    + "(ID,NUM_EXP,TIPO_DOC,DOC,NOMBRE,APE1,APE2,NSS,COD_ACT_FORM,FEC_INI_PRACT,FEC_FIN_PRACT,CC_COT,DIAS_COT,IMP_SOLIC) "
                    + " VALUES (" + id
                    + ", '" + nuevoL1Participante.getNumExp()
                    + "', '" + nuevoL1Participante.getTipoDoc()
                    + "', '" + nuevoL1Participante.getDoc()
                    + "', '" + nuevoL1Participante.getNombre()
                    + "', '" + nuevoL1Participante.getApe1()
                    + "', '" + nuevoL1Participante.getApe2()
                    + "', '" + nuevoL1Participante.getNss()
                    + "', '" + nuevoL1Participante.getCodActForm()
                    + "', TO_DATE('" + fechaIniPract + "','dd/mm/yyyy')"
                    + " , TO_DATE('" + fechaFinPract + "','dd/mm/yyyy')"
                    + " , '" + nuevoL1Participante.getCcCot()
                    + "', " + nuevoL1Participante.getDiasCot()
                    + " , " + nuevoL1Participante.getImpSolic()
                    + ")";
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            int insert = st.executeUpdate(query);
            if (insert > 0) {
                return true;
            } else {
                log.error("No se ha podido guardar un nuevo L1Participante ");
                return false;
            }

        } catch (Exception ex) {
            log.error("Se ha producido un error al insertar un nuevo L1Participante" + ex.getMessage());
            throw new Exception(ex);
        } finally {
            if (st != null) {
                st.close();
            }
        }
    }

    public boolean modificarL1Participante(FilaL1ParticipanteVO datModif, Connection con) throws Exception {
        Statement st = null;
        String query = "";
        String fechaIniPract = "";
        if (datModif != null && datModif.getFecIniPract() != null && !datModif.getFecIniPract().toString().equals("")) {
            SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
            fechaIniPract = formatoFecha.format(datModif.getFecIniPract());
        }
        String fechaFinPract = "";
        if (datModif != null && datModif.getFecFinPract() != null && !datModif.getFecFinPract().toString().equals("")) {
            SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
            fechaFinPract = formatoFecha.format(datModif.getFecFinPract());
        }

        try {
            query = "UPDATE " + ConfigurationParameter.getParameter(ConstantesMeLanbide12.MELANBIDE12_PERS_PRACT_LIN1, ConstantesMeLanbide12.FICHERO_PROPIEDADES)
                    + " SET TIPO_DOC='" + datModif.getTipoDoc() + "'"
                    + ", DOC='" + datModif.getDoc() + "'"
                    + ", NOMBRE='" + datModif.getNombre() + "'"
                    + ", APE1='" + datModif.getApe1() + "'"
                    + ", APE2='" + datModif.getApe2() + "'"
                    + ", NSS='" + datModif.getNss() + "'"
                    + ", COD_ACT_FORM='" + datModif.getCodActForm() + "'"
                    + ", FEC_INI_PRACT=TO_DATE('" + fechaIniPract + "','dd/mm/yyyy')"
                    + ", FEC_FIN_PRACT=TO_DATE('" + fechaFinPract + "','dd/mm/yyyy')"
                    + ", CC_COT='" + datModif.getCcCot() + "'"
                    + ", DIAS_COT=" + datModif.getDiasCot()
                    + ", IMP_SOLIC=" + datModif.getImpSolic()
                    + " WHERE ID=" + datModif.getId();
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            return st.executeUpdate(query) > 0;

        } catch (SQLException ex) {
            log.error("Se ha producido un error al modificar un L1Participante - " + datModif.getId() + " - " + ex);
            throw new Exception(ex);
        } finally {
            if (st != null) {
                st.close();
            }
        }
    }

    public List<FilaL1EmpresaExternaVO> getDatosL1EmpresaExterna(String numExp, int codOrganizacion, Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        List<FilaL1EmpresaExternaVO> lista = new ArrayList<FilaL1EmpresaExternaVO>();
        FilaL1EmpresaExternaVO l1EmpresaExterna = new FilaL1EmpresaExternaVO();
        try {
            String query = null;
            query = "SELECT * FROM " + ConfigurationParameter.getParameter(ConstantesMeLanbide12.MELANBIDE12_EMPR_EXT_LIN1, ConstantesMeLanbide12.FICHERO_PROPIEDADES)
                    + " WHERE NUM_EXP ='" + numExp + "'"
                    + " ORDER BY ID";
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                l1EmpresaExterna = (FilaL1EmpresaExternaVO) MeLanbide12MappingUtils.getInstance().map(rs, FilaL1EmpresaExternaVO.class);
                //Cargamos en el request los valores de los desplegables
                lista.add(l1EmpresaExterna);
                l1EmpresaExterna = new FilaL1EmpresaExternaVO();
            }
        } catch (Exception ex) {
            log.error("Se ha producido un error recuperando L1EmpresaExterna ", ex);
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

    public FilaL1EmpresaExternaVO getL1EmpresaExternaPorID(String id, Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        FilaL1EmpresaExternaVO l1EmpresaExterna = new FilaL1EmpresaExternaVO();
        try {
            String query = null;
            query = "SELECT * FROM " + ConfigurationParameter.getParameter(ConstantesMeLanbide12.MELANBIDE12_EMPR_EXT_LIN1, ConstantesMeLanbide12.FICHERO_PROPIEDADES)
                    + " WHERE ID=" + (id != null && !id.equals("") ? id : "null");
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                l1EmpresaExterna = (FilaL1EmpresaExternaVO) MeLanbide12MappingUtils.getInstance().map(rs, FilaL1EmpresaExternaVO.class);
            }
        } catch (Exception ex) {
            log.error("Se ha producido un error recuperando un L1EmpresaExterna : " + id, ex);
            throw new Exception(ex);
        } finally {
            if (st != null) {
                st.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        return l1EmpresaExterna;
    }

    public int eliminarL1EmpresaExterna(String id, Connection con) throws Exception {
        Statement st = null;
        try {
            String query = null;
            query = "DELETE FROM " + ConfigurationParameter.getParameter(ConstantesMeLanbide12.MELANBIDE12_EMPR_EXT_LIN1, ConstantesMeLanbide12.FICHERO_PROPIEDADES)
                    + " WHERE ID=" + (id != null && !id.equals("") ? id : "null");
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            return st.executeUpdate(query);
        } catch (SQLException ex) {
            log.error("Se ha producido un error eliminando L1EmpresaExterna ID : " + id, ex);
            throw new Exception(ex);
        } finally {
            if (st != null) {
                st.close();
            }
        }
    }

    public boolean crearNuevoL1EmpresaExterna(FilaL1EmpresaExternaVO nuevoL1EmpresaExterna, Connection con) throws Exception {
        Statement st = null;
        String query = "";
        String fechaEmis = "";
        if (nuevoL1EmpresaExterna != null && nuevoL1EmpresaExterna.getFecEmis() != null && !nuevoL1EmpresaExterna.getFecEmis().equals("")) {
            SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
            fechaEmis = formatoFecha.format(nuevoL1EmpresaExterna.getFecEmis());
        }
        String fechaPago = "";
        if (nuevoL1EmpresaExterna != null && nuevoL1EmpresaExterna.getFecPago() != null && !nuevoL1EmpresaExterna.getFecPago().equals("")) {
            SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
            fechaPago = formatoFecha.format(nuevoL1EmpresaExterna.getFecPago());
        }
        try {

            int id = recogerIDInsertar(ConfigurationParameter.getParameter(ConstantesMeLanbide12.SEQ_M12_EMPR_EXT_LIN1, ConstantesMeLanbide12.FICHERO_PROPIEDADES), con);
            query = "INSERT INTO " + ConfigurationParameter.getParameter(ConstantesMeLanbide12.MELANBIDE12_EMPR_EXT_LIN1, ConstantesMeLanbide12.FICHERO_PROPIEDADES)
                    + "(ID,NUM_EXP,CIF,DENOM_EMPR,N_FACTURA,FEC_EMIS,FEC_PAGO,IMP_BASE,IMP_IVA,IMP_TOTAL,PERSONAS,IMP_PERSONA_FACT,IMP_SOLIC) "
                    + " VALUES (" + id
                    + ", '" + nuevoL1EmpresaExterna.getNumExp()
                    + "', '" + nuevoL1EmpresaExterna.getCif()
                    + "', '" + nuevoL1EmpresaExterna.getDenomEmpr()
                    + "', '" + nuevoL1EmpresaExterna.getnFactura()
                    + "', TO_DATE('" + fechaEmis + "','dd/mm/yyyy')"
                    + " , TO_DATE('" + fechaPago + "','dd/mm/yyyy')"
                    + " , " + nuevoL1EmpresaExterna.getImpBase()
                    + " , " + nuevoL1EmpresaExterna.getImpIva()
                    + " , " + nuevoL1EmpresaExterna.getImpTotal()
                    + " , " + nuevoL1EmpresaExterna.getPersonas()
                    + " , " + nuevoL1EmpresaExterna.getImpPersonaFact()
                    + " , " + nuevoL1EmpresaExterna.getImpSolic()
                    + ")";
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            int insert = st.executeUpdate(query);
            if (insert > 0) {
                return true;
            } else {
                log.error("No se ha podido guardar un nuevo L1EmpresaExterna ");
                return false;
            }

        } catch (Exception ex) {
            log.error("Se ha producido un error al insertar un nuevo L1EmpresaExterna" + ex.getMessage());
            throw new Exception(ex);
        } finally {
            if (st != null) {
                st.close();
            }
        }
    }

    public boolean modificarL1EmpresaExterna(FilaL1EmpresaExternaVO datModif, Connection con) throws Exception {
        Statement st = null;
        String query = "";
        String fechaEmis = "";
        if (datModif != null && datModif.getFecEmis() != null && !datModif.getFecEmis().toString().equals("")) {
            SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
            fechaEmis = formatoFecha.format(datModif.getFecEmis());
        }
        String fechaPago = "";
        if (datModif != null && datModif.getFecPago() != null && !datModif.getFecPago().toString().equals("")) {
            SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
            fechaPago = formatoFecha.format(datModif.getFecPago());
        }

        try {
            query = "UPDATE " + ConfigurationParameter.getParameter(ConstantesMeLanbide12.MELANBIDE12_EMPR_EXT_LIN1, ConstantesMeLanbide12.FICHERO_PROPIEDADES)
                    + " SET CIF='" + datModif.getCif() + "'"
                    + ", DENOM_EMPR='" + datModif.getDenomEmpr() + "'"
                    + ", N_FACTURA='" + datModif.getnFactura() + "'"
                    + ", FEC_EMIS=TO_DATE('" + fechaEmis + "','dd/mm/yyyy')"
                    + ", FEC_PAGO=TO_DATE('" + fechaPago + "','dd/mm/yyyy')"
                    + ", IMP_BASE=" + datModif.getImpBase()
                    + ", IMP_IVA=" + datModif.getImpIva()
                    + ", IMP_TOTAL=" + datModif.getImpTotal()
                    + ", PERSONAS=" + datModif.getPersonas()
                    + ", IMP_PERSONA_FACT=" + datModif.getImpPersonaFact()
                    + ", IMP_SOLIC=" + datModif.getImpSolic()
                    + " WHERE ID=" + datModif.getId();
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            return st.executeUpdate(query) > 0;

        } catch (SQLException ex) {
            log.error("Se ha producido un error al modificar un L1Participante - " + datModif.getId() + " - " + ex);
            throw new Exception(ex);
        } finally {
            if (st != null) {
                st.close();
            }
        }
    }

    public List<FilaL1EmpresaPropiaVO> getDatosL1EmpresaPropia(String numExp, int codOrganizacion, Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        List<FilaL1EmpresaPropiaVO> lista = new ArrayList<FilaL1EmpresaPropiaVO>();
        FilaL1EmpresaPropiaVO l1EmpresaPropia = new FilaL1EmpresaPropiaVO();
        try {
            String query = null;
            query = "SELECT * FROM " + ConfigurationParameter.getParameter(ConstantesMeLanbide12.MELANBIDE12_EMPR_PROP_LIN1, ConstantesMeLanbide12.FICHERO_PROPIEDADES)
                    + " WHERE NUM_EXP ='" + numExp + "'"
                    + " ORDER BY ID";
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                l1EmpresaPropia = (FilaL1EmpresaPropiaVO) MeLanbide12MappingUtils.getInstance().map(rs, FilaL1EmpresaPropiaVO.class);
                //Cargamos en el request los valores de los desplegables
                lista.add(l1EmpresaPropia);
                l1EmpresaPropia = new FilaL1EmpresaPropiaVO();
            }
        } catch (Exception ex) {
            log.error("Se ha producido un error recuperando L1EmpresaPropia ", ex);
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

    public FilaL1EmpresaPropiaVO getL1EmpresaPropiaPorID(String id, Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        FilaL1EmpresaPropiaVO l1EmpresaPropia = new FilaL1EmpresaPropiaVO();
        try {
            String query = null;
            query = "SELECT * FROM " + ConfigurationParameter.getParameter(ConstantesMeLanbide12.MELANBIDE12_EMPR_PROP_LIN1, ConstantesMeLanbide12.FICHERO_PROPIEDADES)
                    + " WHERE ID=" + (id != null && !id.equals("") ? id : "null");
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                l1EmpresaPropia = (FilaL1EmpresaPropiaVO) MeLanbide12MappingUtils.getInstance().map(rs, FilaL1EmpresaPropiaVO.class);
            }
        } catch (Exception ex) {
            log.error("Se ha producido un error recuperando una L1EmpresaPropia : " + id, ex);
            throw new Exception(ex);
        } finally {
            if (st != null) {
                st.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        return l1EmpresaPropia;
    }

    public int eliminarL1EmpresaPropia(String id, Connection con) throws Exception {
        Statement st = null;
        try {
            String query = null;
            query = "DELETE FROM " + ConfigurationParameter.getParameter(ConstantesMeLanbide12.MELANBIDE12_EMPR_PROP_LIN1, ConstantesMeLanbide12.FICHERO_PROPIEDADES)
                    + " WHERE ID=" + (id != null && !id.equals("") ? id : "null");
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            return st.executeUpdate(query);
        } catch (SQLException ex) {
            log.error("Se ha producido un error Eliminando L1EmpresaPropia ID : " + id, ex);
            throw new Exception(ex);
        } finally {
            if (st != null) {
                st.close();
            }
        }
    }

    public boolean crearNuevoL1EmpresaPropia(FilaL1EmpresaPropiaVO nuevoL1EmpresaPropia, Connection con) throws Exception {
        Statement st = null;
        String query = "";
        try {
            int id = recogerIDInsertar(ConfigurationParameter.getParameter(ConstantesMeLanbide12.SEQ_M12_EMPR_PROP_LIN1, ConstantesMeLanbide12.FICHERO_PROPIEDADES), con);
            query = "INSERT INTO " + ConfigurationParameter.getParameter(ConstantesMeLanbide12.MELANBIDE12_EMPR_PROP_LIN1, ConstantesMeLanbide12.FICHERO_PROPIEDADES)
                    + "(ID,NUM_EXP,DNI,NOMBRE,APE1,APE2,RETR_ANUAL_BRUTA,CC_COT_SS,HORAS_LAB_ANUAL,HORAS_IMPUT,IMP_GEST,PERSON_PRACT,IMP_SOLIC) "
                    + " VALUES (" + id
                    + ", '" + nuevoL1EmpresaPropia.getNumExp()
                    + "', '" + nuevoL1EmpresaPropia.getDni()
                    + "', '" + nuevoL1EmpresaPropia.getNombre()
                    + "', '" + nuevoL1EmpresaPropia.getApe1()
                    + "', '" + nuevoL1EmpresaPropia.getApe2()
                    + "', " + nuevoL1EmpresaPropia.getRetrAnualBruta()
                    + ", " + nuevoL1EmpresaPropia.getCcCotSs()
                    + " , " + nuevoL1EmpresaPropia.getHorasLabAnual()
                    + " , " + nuevoL1EmpresaPropia.getHorasImput()
                    + " , " + nuevoL1EmpresaPropia.getImpGest()
                    + " , " + nuevoL1EmpresaPropia.getPersonPract()
                    + " , " + nuevoL1EmpresaPropia.getImpSolic()
                    + ")";
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            int insert = st.executeUpdate(query);
            if (insert > 0) {
                return true;
            } else {
                log.error("No se ha podido guardar un nuevo L1EmpresaPropia ");
                return false;
            }

        } catch (Exception ex) {
            log.error("Se ha producido un error al insertar un nuevo L1EmpresaPropia" + ex.getMessage());
            throw new Exception(ex);
        } finally {
            if (st != null) {
                st.close();
            }
        }
    }

    public boolean modificarL1EmpresaPropia(FilaL1EmpresaPropiaVO datModif, Connection con) throws Exception {
        Statement st = null;
        String query = "";
        try {
            query = "UPDATE " + ConfigurationParameter.getParameter(ConstantesMeLanbide12.MELANBIDE12_EMPR_PROP_LIN1, ConstantesMeLanbide12.FICHERO_PROPIEDADES)
                    + " SET DNI='" + datModif.getDni() + "'"
                    + ", NOMBRE='" + datModif.getNombre() + "'"
                    + ", APE1='" + datModif.getApe1() + "'"
                    + ", APE2='" + datModif.getApe2() + "'"
                    + ", RETR_ANUAL_BRUTA=" + datModif.getRetrAnualBruta()
                    + ", CC_COT_SS=" + datModif.getCcCotSs()
                    + ", HORAS_LAB_ANUAL=" + datModif.getHorasLabAnual()
                    + ", HORAS_IMPUT=" + datModif.getHorasImput()
                    + ", IMP_GEST=" + datModif.getImpGest()
                    + ", PERSON_PRACT=" + datModif.getPersonPract()
                    + ", IMP_SOLIC=" + datModif.getImpSolic()
                    + " WHERE ID=" + datModif.getId();
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            return st.executeUpdate(query) > 0;

        } catch (SQLException ex) {
            log.error("Se ha producido un error al modificar un L1EmpresaPropia - " + datModif.getId() + " - " + ex);
            throw new Exception(ex);
        } finally {
            if (st != null) {
                st.close();
            }
        }
    }

    public List<FilaL2ParticipanteVO> getDatosL2Participante(String numExp, int codOrganizacion, Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        List<FilaL2ParticipanteVO> lista = new ArrayList<FilaL2ParticipanteVO>();
        FilaL2ParticipanteVO l2Participante = new FilaL2ParticipanteVO();
        try {
            String query = null;
            query = "SELECT * FROM " + ConfigurationParameter.getParameter(ConstantesMeLanbide12.MELANBIDE12_PERS_PRACT_LIN2, ConstantesMeLanbide12.FICHERO_PROPIEDADES)
                    + " WHERE NUM_EXP ='" + numExp + "'"
                    + " ORDER BY ID";
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                l2Participante = (FilaL2ParticipanteVO) MeLanbide12MappingUtils.getInstance().map(rs, FilaL2ParticipanteVO.class);
                //Cargamos en el request los valores de los desplegables
                lista.add(l2Participante);
                l2Participante = new FilaL2ParticipanteVO();
            }
        } catch (Exception ex) {
            log.error("Se ha producido un error recuperando L2Participante ", ex);
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

    public FilaL2ParticipanteVO getL2ParticipantePorID(String id, Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        FilaL2ParticipanteVO l2Participante = new FilaL2ParticipanteVO();
        try {
            String query = null;
            query = "SELECT * FROM " + ConfigurationParameter.getParameter(ConstantesMeLanbide12.MELANBIDE12_PERS_PRACT_LIN2, ConstantesMeLanbide12.FICHERO_PROPIEDADES)
                    + " WHERE ID=" + (id != null && !id.equals("") ? id : "null");
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                l2Participante = (FilaL2ParticipanteVO) MeLanbide12MappingUtils.getInstance().map(rs, FilaL2ParticipanteVO.class);
            }
        } catch (Exception ex) {
            log.error("Se ha producido un error recuperando una L2Participante : " + id, ex);
            throw new Exception(ex);
        } finally {
            if (st != null) {
                st.close();
            }
            if (rs != null) {
                rs.close();
            }
        }
        return l2Participante;
    }

    public int eliminarL2Participante(String id, Connection con) throws Exception {
        Statement st = null;
        try {
            String query = null;
            query = "DELETE FROM " + ConfigurationParameter.getParameter(ConstantesMeLanbide12.MELANBIDE12_PERS_PRACT_LIN2, ConstantesMeLanbide12.FICHERO_PROPIEDADES)
                    + " WHERE ID=" + (id != null && !id.equals("") ? id : "null");
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            return st.executeUpdate(query);
        } catch (SQLException ex) {
            log.error("Se ha producido un error Eliminando L2Participante ID : " + id, ex);
            throw new Exception(ex);
        } finally {
            if (st != null) {
                st.close();
            }
        }
    }

    public boolean crearNuevoL2Participante(FilaL2ParticipanteVO nuevoL2Participante, Connection con) throws Exception {
        Statement st = null;
        String query = "";
        try {

            int id = recogerIDInsertar(ConfigurationParameter.getParameter(ConstantesMeLanbide12.SEQ_M12_PERS_PRACT_LIN2, ConstantesMeLanbide12.FICHERO_PROPIEDADES), con);
            query = "INSERT INTO " + ConfigurationParameter.getParameter(ConstantesMeLanbide12.MELANBIDE12_PERS_PRACT_LIN2, ConstantesMeLanbide12.FICHERO_PROPIEDADES)
                    + "(ID,NUM_EXP,TIPO_DOC,DOC,NOMBRE,APE1,APE2,COD_ACT_FORM,HORAS_PRACT,IMP_SOLIC) "
                    + " VALUES (" + id
                    + ", '" + nuevoL2Participante.getNumExp()
                    + "', '" + nuevoL2Participante.getTipoDoc()
                    + "', '" + nuevoL2Participante.getDoc()
                    + "', '" + nuevoL2Participante.getNombre()
                    + "', '" + nuevoL2Participante.getApe1()
                    + "', '" + nuevoL2Participante.getApe2()
                    + "', '" + nuevoL2Participante.getCodActForm()
                    + "', " + nuevoL2Participante.getHorasPract()
                    + " , " + nuevoL2Participante.getImpSolic()
                    + ")";
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            int insert = st.executeUpdate(query);
            if (insert > 0) {
                return true;
            } else {
                log.error("No se ha podido guardar un nuevo L2Participante ");
                return false;
            }

        } catch (Exception ex) {
            log.error("Se ha producido un error al insertar un nuevo L2Participante" + ex.getMessage());
            throw new Exception(ex);
        } finally {
            if (st != null) {
                st.close();
            }
        }
    }

    public boolean modificarL2Participante(FilaL2ParticipanteVO datModif, Connection con) throws Exception {
        Statement st = null;
        String query = "";
        try {
            query = "UPDATE " + ConfigurationParameter.getParameter(ConstantesMeLanbide12.MELANBIDE12_PERS_PRACT_LIN2, ConstantesMeLanbide12.FICHERO_PROPIEDADES)
                    + " SET TIPO_DOC='" + datModif.getTipoDoc() + "'"
                    + ", DOC='" + datModif.getDoc() + "'"
                    + ", NOMBRE='" + datModif.getNombre() + "'"
                    + ", APE1='" + datModif.getApe1() + "'"
                    + ", APE2='" + datModif.getApe2() + "'"
                    + ", COD_ACT_FORM='" + datModif.getCodActForm() + "'"
                    + ", HORAS_PRACT=" + datModif.getHorasPract()
                    + ", IMP_SOLIC=" + datModif.getImpSolic()
                    + " WHERE ID=" + datModif.getId();
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            return st.executeUpdate(query) > 0;

        } catch (SQLException ex) {
            log.error("Se ha producido un error al modificar un L2Participante - " + datModif.getId() + " - " + ex);
            throw new Exception(ex);
        } finally {
            if (st != null) {
                st.close();
            }
        }
    }

}

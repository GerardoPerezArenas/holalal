
package es.altia.flexia.integracion.moduloexterno.melanbide80.dao;

import es.altia.flexia.integracion.moduloexterno.melanbide80.util.ConfigurationParameter;
import es.altia.flexia.integracion.moduloexterno.melanbide80.util.ConstantesMeLanbide80;
import es.altia.flexia.integracion.moduloexterno.melanbide80.util.MeLanbide80MappingUtils;
import es.altia.flexia.integracion.moduloexterno.melanbide80.vo.PersonaVO;
import es.altia.flexia.integracion.moduloexterno.melanbide80.vo.DesplegableAdmonLocalVO;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

public class MeLanbide80DAO {

    //Logger
    private static Logger log = LogManager.getLogger(MeLanbide80DAO.class);
    //Instancia
    private static MeLanbide80DAO instance = null;

    // Constructor
    private MeLanbide80DAO() {

    }

    public static MeLanbide80DAO getInstance() {
        if (instance == null) {
            synchronized (MeLanbide80DAO.class) {
                instance = new MeLanbide80DAO();
            }
        }
        return instance;
    }

    public List<PersonaVO> getDatosPersona(String numExp, int codOrganizacion, Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        List<PersonaVO> lista = new ArrayList<PersonaVO>();
        PersonaVO persona = new PersonaVO();
        try {
            String query = null;
            query = "SELECT * FROM " + ConfigurationParameter.getParameter(ConstantesMeLanbide80.MELANBIDE80_PERSONAS, ConstantesMeLanbide80.FICHERO_PROPIEDADES)
                    + " WHERE NUM_EXP ='" + numExp + "'"
                    + " ORDER BY ID";
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                persona = (PersonaVO) MeLanbide80MappingUtils.getInstance().map(rs, PersonaVO.class);
                //Cargamos en el request los valores de los desplegables
                lista.add(persona);
                persona = new PersonaVO();
            }
        } catch (Exception ex) {
            log.error("Se ha producido un error recuperando Personas ", ex);
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
        return lista;
    }

    public PersonaVO getPersonaPorID(String id, Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        PersonaVO persona = new PersonaVO();
        try {
            String query = null;
            query = "SELECT * FROM " + ConfigurationParameter.getParameter(ConstantesMeLanbide80.MELANBIDE80_PERSONAS, ConstantesMeLanbide80.FICHERO_PROPIEDADES)
                    + " WHERE ID=" + (id != null && !id.equals("") ? id : "null");
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                persona = (PersonaVO) MeLanbide80MappingUtils.getInstance().map(rs, PersonaVO.class);
            }
        } catch (Exception ex) {
            log.error("Se ha producido un error recuperando una persona : " + id, ex);
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
        return persona;
    }

    public int eliminarPersona(String id, Connection con) throws Exception {
        Statement st = null;
        try {
            String query = null;
            query = "DELETE FROM " + ConfigurationParameter.getParameter(ConstantesMeLanbide80.MELANBIDE80_PERSONAS, ConstantesMeLanbide80.FICHERO_PROPIEDADES)
                    + " WHERE ID=" + (id != null && !id.equals("") ? id : "null");
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            return st.executeUpdate(query);
        } catch (Exception ex) {
            log.error("Se ha producido un error Eliminando Persona ID : " + id, ex);
            throw new Exception(ex);
        } finally {
            if (log.isDebugEnabled()) {
                log.debug("Procedemos a cerrar el statement y el resultset");
            }
            if (st != null) {
                st.close();
            }
        }
    }

    public boolean crearNuevaPersona(PersonaVO nuevaPersona, Connection con) throws Exception {
        Statement st = null;
        String query = "";
        String fecinisit = "";
        String fecfinsit = "";
        if (nuevaPersona != null && nuevaPersona.getFecinisit()!= null && !nuevaPersona.getFecinisit().equals("")) {
            SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
            fecinisit = formatoFecha.format(nuevaPersona.getFecinisit());
        }
        if (nuevaPersona != null && nuevaPersona.getFecfinsit()!= null && !nuevaPersona.getFecfinsit().equals("")) {
            SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
            fecfinsit = formatoFecha.format(nuevaPersona.getFecfinsit());
        }
        try {

            int id = recogerIDInsertar(ConfigurationParameter.getParameter(ConstantesMeLanbide80.SEQ_MELANBIDE80_PERSONAS, ConstantesMeLanbide80.FICHERO_PROPIEDADES), con);
            query = "INSERT INTO " + ConfigurationParameter.getParameter(ConstantesMeLanbide80.MELANBIDE80_PERSONAS, ConstantesMeLanbide80.FICHERO_PROPIEDADES)
                    + "(ID,NUM_EXP,DNI,NOMBRE,APEL1,APEL2,TIPCONTA,TIPCONTB,JORNADA,PORJORPAR,SITUACION,REDUCJORN,"
                    + "FECINISIT,FECFINSIT,NUMDIASIT,BASEREGUL,IMPPREST,COMPLSAL,IMPSUBVSOL) "
                    + " VALUES (" + id
                    + ", '" + nuevaPersona.getNumExp()
                    + "', '" + nuevaPersona.getDni()
                    + "', '" + nuevaPersona.getNombre()
                    + "', '" + nuevaPersona.getApel1()
                    + "', '" + nuevaPersona.getApel2()
                    + "', '" + nuevaPersona.getTipcontA()
                    + "', " + nuevaPersona.getTipcontB()
                    + ", '" + nuevaPersona.getJornada()
                    + "', " + nuevaPersona.getPorjorpar()
                    + ", '" + nuevaPersona.getSituacion()
                    + "', " + nuevaPersona.getReducjorn()
                    + " , TO_DATE('" + fecinisit + "','dd/mm/yyyy')"
                    + " , TO_DATE('" + fecfinsit + "','dd/mm/yyyy')"
                    + "," + nuevaPersona.getNumdiasit()
                    + "," + nuevaPersona.getBaseregul()
                    + "," + nuevaPersona.getImpprest()
                    + "," + nuevaPersona.getComplsal()
                    + "," + nuevaPersona.getImpsubvsol()
                    + ")";
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            int insert = st.executeUpdate(query);
            if (insert > 0) {
                return true;
            } else {
                log.debug("No Se ha podido guardar una nueva Persona ");
                return false;
            }

        } catch (Exception ex) {
            //opeCorrecta = false;
            log.debug("Se ha producido un error al Insertar una nueva Persona" + ex.getMessage());
            throw new Exception(ex);
            //return opeCorrecta;
        } finally {
            if (log.isDebugEnabled()) {
                log.debug("Procedemos a cerrar el statement y el resultset");
            }
            if (st != null) {
                st.close();
            }
        }
    }

    public boolean modificarPersona(PersonaVO datModif, Connection con) throws Exception {
        Statement st = null;
        String query = "";
        String fecinisit = "";
        String fecfinsit = "";
        if (datModif != null && datModif.getFecinisit()!= null && !datModif.getFecinisit().equals("")) {
            SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
            fecinisit = formatoFecha.format(datModif.getFecinisit());
        }
        if (datModif != null && datModif.getFecfinsit()!= null && !datModif.getFecfinsit().equals("")) {
            SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
            fecfinsit = formatoFecha.format(datModif.getFecfinsit());
        }
        try {
            query = "UPDATE " + ConfigurationParameter.getParameter(ConstantesMeLanbide80.MELANBIDE80_PERSONAS, ConstantesMeLanbide80.FICHERO_PROPIEDADES)
                    + " SET DNI='" + datModif.getDni()+ "'"
                    + ", NOMBRE='" + datModif.getNombre()+ "'"
                    + ", APEL1='" + datModif.getApel1()+ "'"
                    + ", APEL2='" + datModif.getApel2()+ "'"
                    + ", TIPCONTA='" + datModif.getTipcontA()+ "'"
                    + ", TIPCONTB=" + datModif.getTipcontB()
                    + ", JORNADA='" + datModif.getJornada()+ "'"
                    + ", PORJORPAR=" + datModif.getPorjorpar()
                    + ", SITUACION='" + datModif.getSituacion()+ "'"
                    + ", REDUCJORN=" + datModif.getReducjorn()
                    + ", FECINISIT=TO_DATE('" + fecinisit + "','dd/mm/yyyy')"
                    + ", FECFINSIT=tO_DATE('" + fecfinsit + "','dd/mm/yyyy')"
                    + ", NUMDIASIT=" + datModif.getNumdiasit()
                    + ", BASEREGUL=" + datModif.getBaseregul()
                    + ", IMPPREST=" + datModif.getImpprest()
                    + ", COMPLSAL=" + datModif.getComplsal()
                    + ", IMPSUBVSOL=" + datModif.getImpsubvsol()
                    + " WHERE ID=" + datModif.getId();
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            int insert = st.executeUpdate(query);
            if (insert > 0) {
                return true;
            } else {
                return false;
            }

        } catch (Exception ex) {
            log.debug("Se ha producido un error al Modificar una persona - "
                    + datModif.getId() + " - " + ex);
            throw new Exception(ex);
        } finally {
            if (log.isDebugEnabled()) {
                log.debug("Procedemos a cerrar el statement y el resultset");
            }
            if (st != null) {
                st.close();
            }
        }
    }

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
            log.error("Se ha producido un error recuperando El numero de ID para insercion al llamar la sequencia " + sequence + " : ", ex);
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

    public List<DesplegableAdmonLocalVO> getValoresDesplegablesAdmonLocalxdes_cod(String des_cod, Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        List<DesplegableAdmonLocalVO> lista = new ArrayList<DesplegableAdmonLocalVO>();
        DesplegableAdmonLocalVO valoresDesplegabe = new DesplegableAdmonLocalVO();
        try {
            String query = null;
            query = "SELECT * FROM " + ConfigurationParameter.getParameter(ConstantesMeLanbide80.TABLA_VALORES_DESPLEGABLES, ConstantesMeLanbide80.FICHERO_PROPIEDADES)
                    + " WHERE DES_COD='" + des_cod + "'";
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                valoresDesplegabe = (DesplegableAdmonLocalVO) MeLanbide80MappingUtils.getInstance().map(rs, DesplegableAdmonLocalVO.class);
                lista.add(valoresDesplegabe);
                valoresDesplegabe = new DesplegableAdmonLocalVO();
            }
        } catch (Exception ex) {
            log.error("Se ha producido un error recuperando un valores de un desplegable : " + des_cod, ex);
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
        return lista;
    }

        public String getOidDocumento(String numExpediente, Connection con) throws Exception {
        log.info("getOidDocumento - Begin()");
        Statement st = null;
        ResultSet rs = null;
        String query = null;
        String oid = "";
        try {
            query = "SELECT red_iddoc_gestor AS OID FROM " + ConfigurationParameter.getParameter(ConstantesMeLanbide80.TABLA_DOCUMENTOS_REGISTRO, ConstantesMeLanbide80.FICHERO_PROPIEDADES)
                    + " JOIN " + ConfigurationParameter.getParameter(ConstantesMeLanbide80.TABLA_EXPEDIENTE_REGISTRO, ConstantesMeLanbide80.FICHERO_PROPIEDADES)
                    + " ON e_exr.exr_nre = r_red.red_num"
                    + " AND e_exr.exr_num = '" + numExpediente + "'"
                    + " AND e_exr.exr_tip = r_red.red_tip"
                    + " AND e_exr.exr_top = '0'"
                    + " WHERE red_tip = 'E' "
                    + " AND red_nom_doc = 'FLX_DATOS_CONTRATOS_AEXCE'";
            log.debug("sql = " + query);
            st = con.createStatement();
            rs = st.executeQuery(query);
            if (rs.next()) {
                oid = rs.getString("OID");
            }
            log.info("OID: " + oid);
            return oid;
        } catch (Exception ex) {
            log.error("Se ha producido un error en getOidDocumento", ex);
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
    }
}

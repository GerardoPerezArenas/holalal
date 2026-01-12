
package es.altia.flexia.integracion.moduloexterno.melanbide75.dao;

import es.altia.flexia.integracion.moduloexterno.melanbide75.util.ConfigurationParameter;
import es.altia.flexia.integracion.moduloexterno.melanbide75.util.ConstantesMeLanbide75;
import es.altia.flexia.integracion.moduloexterno.melanbide75.util.MeLanbide75MappingUtils;
import es.altia.flexia.integracion.moduloexterno.melanbide75.vo.ControlAccesoVO;
import es.altia.flexia.integracion.moduloexterno.melanbide75.vo.DesplegableAdmonLocalVO;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

public class MeLanbide75DAO {

    //Logger
    private static Logger log = LogManager.getLogger(MeLanbide75DAO.class);
    //Instancia
    private static MeLanbide75DAO instance = null;

    // Constructor
    private MeLanbide75DAO() {

    }

    public static MeLanbide75DAO getInstance() {
        if (instance == null) {
            synchronized (MeLanbide75DAO.class) {
                instance = new MeLanbide75DAO();
            }
        }
        return instance;
    }

    public List<ControlAccesoVO> getDatosControlAcceso(String numExp, int codOrganizacion, Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        List<ControlAccesoVO> lista = new ArrayList<ControlAccesoVO>();
        ControlAccesoVO controlAcceso = new ControlAccesoVO();
        try {
            String query = null;
            query = "SELECT * FROM " + ConfigurationParameter.getParameter(ConstantesMeLanbide75.MELANBIDE75_PLANTILLA, ConstantesMeLanbide75.FICHERO_PROPIEDADES)
                    + " WHERE NUM_EXP ='" + numExp + "'"
                    + " ORDER BY ID";
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                controlAcceso = (ControlAccesoVO) MeLanbide75MappingUtils.getInstance().map(rs, ControlAccesoVO.class);
                //Cargamos el el request los valores  de los desplegables
                lista.add(controlAcceso);
                controlAcceso = new ControlAccesoVO();
            }
        } catch (Exception ex) {
            log.error("Se ha producido un error recuperando Puestos ", ex);
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

    public List<ControlAccesoVO> getRegistroAcceso(String numExp, Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        List<ControlAccesoVO> lista = new ArrayList<ControlAccesoVO>();
        ControlAccesoVO controlAcceso = new ControlAccesoVO();
        try {
            String query = null;
            query = "Select * From " + ConfigurationParameter.getParameter(ConstantesMeLanbide75.MELANBIDE75_PLANTILLA, ConstantesMeLanbide75.FICHERO_PROPIEDADES) + " A "
                    + "Where A.Num_Exp= '" + numExp + "' Order By A.Id";
            if (log.isDebugEnabled()) {
                log.debug("sql getRegistro= " + query);
            }
            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                controlAcceso = (ControlAccesoVO) MeLanbide75MappingUtils.getInstance().map(rs, ControlAccesoVO.class);
                //Cargamos el el request los valores  de los desplegables
                lista.add(controlAcceso);
                controlAcceso = new ControlAccesoVO();
            }
        } catch (Exception ex) {
            log.error("Se ha producido un error recuperando puestos ", ex);
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

    public ControlAccesoVO getControlAccesoPorID(String id, Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        ControlAccesoVO controlAcceso = new ControlAccesoVO();
        try {
            String query = null;
            query = "SELECT * FROM " + ConfigurationParameter.getParameter(ConstantesMeLanbide75.MELANBIDE75_PLANTILLA, ConstantesMeLanbide75.FICHERO_PROPIEDADES)
                    + " WHERE ID=" + (id != null && !id.equals("") ? id : "null");
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                controlAcceso = (ControlAccesoVO) MeLanbide75MappingUtils.getInstance().map(rs, ControlAccesoVO.class);
            }
        } catch (Exception ex) {
            log.error("Se ha producido un error recuperando un puesto : " + id, ex);
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
        return controlAcceso;
    }

    public int eliminarAcceso(String id, Connection con) throws Exception {
        Statement st = null;
        try {
            String query = null;
            query = "DELETE FROM " + ConfigurationParameter.getParameter(ConstantesMeLanbide75.MELANBIDE75_PLANTILLA, ConstantesMeLanbide75.FICHERO_PROPIEDADES)
                    + " WHERE ID=" + (id != null && !id.equals("") ? id : "null");
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            return st.executeUpdate(query);
        } catch (Exception ex) {
            log.error("Se ha producido un error Eliminando Puesto ID : " + id, ex);
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

    public boolean crearNuevoAcceso(ControlAccesoVO nuevoAcceso, Connection con) throws Exception {
        Statement st = null;
        //boolean opeCorrecta = true;
        String query = "";
        String fechaConDesde = "";
        String fechaConHasta = "";
        if (nuevoAcceso != null && nuevoAcceso.getConDesde() != null && !nuevoAcceso.getConDesde().equals("")) {
            SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
            fechaConDesde = formatoFecha.format(nuevoAcceso.getConDesde());
        }
        if (nuevoAcceso != null && nuevoAcceso.getConHasta() != null && !nuevoAcceso.getConHasta().equals("")) {
            SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
            fechaConHasta = formatoFecha.format(nuevoAcceso.getConHasta());
        }
        try {

            int id = recogerIDInsertar(ConfigurationParameter.getParameter(ConstantesMeLanbide75.SEQ_MELANBIDE75_PLANTILLA, ConstantesMeLanbide75.FICHERO_PROPIEDADES), con);
            query = "INSERT INTO " + ConfigurationParameter.getParameter(ConstantesMeLanbide75.MELANBIDE75_PLANTILLA, ConstantesMeLanbide75.FICHERO_PROPIEDADES)
                    + "(ID,NUM_EXP,DENOM_PUESTO,NOMBRE_APELLIDOS,NIF,POR_DISC,TIPO_CONTRATO,"
                    + "CONTRATO_DESDE,CONTRATO_HASTA,TOTAL_DIAS) "
                    + " VALUES (" + id
                    + ", '" + nuevoAcceso.getNumExp()
                    + "', '" + nuevoAcceso.getPuesto()
                    + "', '" + nuevoAcceso.getNombre()
                    + "', '" + nuevoAcceso.getNif()
                    + "', " + nuevoAcceso.getPorDisc()
                    + ", '" + nuevoAcceso.getTipoCon()
                    + "' , TO_DATE('" + fechaConDesde + "','dd/mm/yyyy')"
                    + " , TO_DATE('" + fechaConHasta + "','dd/mm/yyyy')"
                    + "," + nuevoAcceso.getTotalDias()
                    + ")";
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            int insert = st.executeUpdate(query);
            if (insert > 0) {
                return true;
            } else {
                log.debug("No Se ha podido guardar un nuevo Puesto ");
                return false;
            }

        } catch (Exception ex) {
            //opeCorrecta = false;
            log.debug("Se ha producido un error al Insertar un nuevo Puesto" + ex.getMessage());
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

    public boolean modificarAcceso(ControlAccesoVO datModif, Connection con) throws Exception {
        Statement st = null;
        String query = "";
        String fechaConDesde = "";
        String fechaConHasta = "";
        if (datModif != null && datModif.getConDesde() != null && !datModif.getConDesde().equals("")) {
            SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
            fechaConDesde = formatoFecha.format(datModif.getConDesde());
        }
        if (datModif != null && datModif.getConHasta() != null && !datModif.getConHasta().equals("")) {
            SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
            fechaConHasta = formatoFecha.format(datModif.getConHasta());
        }
        try {
            query = "UPDATE " + ConfigurationParameter.getParameter(ConstantesMeLanbide75.MELANBIDE75_PLANTILLA, ConstantesMeLanbide75.FICHERO_PROPIEDADES)
                    + " SET NOMBRE_APELLIDOS='" + datModif.getNombre() + "'"
                    + ", DENOM_PUESTO='" + datModif.getPuesto() + "'"
                    + ", NIF='" + datModif.getNif() + "'"
                    + ", CONTRATO_DESDE=TO_DATE('" + fechaConDesde + "','dd/mm/yyyy')"
                    + ", TOTAL_DIAS=" + datModif.getTotalDias()
                    + ", CONTRATO_HASTA=tO_DATE('" + fechaConHasta + "','dd/mm/yyyy')"
                    + ", POR_DISC=" + datModif.getPorDisc()
                    + ", TIPO_CONTRATO='" + datModif.getTipoCon() + "'"
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
            log.debug("Se ha producido un error al Modificar un puesto - "
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
            query = "SELECT * FROM " + ConfigurationParameter.getParameter(ConstantesMeLanbide75.TABLA_VALORES_DESPLEGABLES, ConstantesMeLanbide75.FICHERO_PROPIEDADES)
                    + " WHERE DES_COD='" + des_cod + "'";
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                valoresDesplegabe = (DesplegableAdmonLocalVO) MeLanbide75MappingUtils.getInstance().map(rs, DesplegableAdmonLocalVO.class);
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

    
}

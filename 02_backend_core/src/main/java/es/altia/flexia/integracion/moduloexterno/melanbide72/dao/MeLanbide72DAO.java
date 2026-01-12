package es.altia.flexia.integracion.moduloexterno.melanbide72.dao;

import es.altia.flexia.integracion.moduloexterno.melanbide72.util.ConfigurationParameter;
import es.altia.flexia.integracion.moduloexterno.melanbide72.util.ConstantesMeLanbide72;
import es.altia.flexia.integracion.moduloexterno.melanbide72.util.MeLanbide72MappingUtils;
import es.altia.flexia.integracion.moduloexterno.melanbide72.vo.AlarmaVO;
import es.altia.flexia.integracion.moduloexterno.melanbide72.vo.MedidaA1BCVO;
import es.altia.flexia.integracion.moduloexterno.melanbide72.vo.MedidaA2VO;
import es.altia.flexia.integracion.moduloexterno.melanbide72.vo.RegistroBatchVO;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

public class MeLanbide72DAO {

    //Logger
    private static Logger log = LogManager.getLogger(MeLanbide72DAO.class);

    //Instancia
    private static MeLanbide72DAO instance = null;

    private MeLanbide72DAO() {

    }

    public static MeLanbide72DAO getInstance() {
        if (instance == null) {
            synchronized (MeLanbide72DAO.class) {
                instance = new MeLanbide72DAO();
            }
        }
        return instance;
    }
    
    public List<MedidaA1BCVO> getDatosMedidasA1(String numExp, int codOrganizacion, Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        List<MedidaA1BCVO> lista = new ArrayList<MedidaA1BCVO>();
        MedidaA1BCVO medida = new MedidaA1BCVO();
        try {
            String query = null;
            query = "SELECT * FROM " + ConfigurationParameter.getParameter(ConstantesMeLanbide72.MELANBIDE72_MEDIDAS_ALT_A1, ConstantesMeLanbide72.FICHERO_PROPIEDADES)
                    + " WHERE NUM_EXP ='" + numExp + "'"
                    + " ORDER BY ID";

            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                medida = (MedidaA1BCVO) MeLanbide72MappingUtils.getInstance().mapA1(rs, MedidaA1BCVO.class);
              
                lista.add(medida);
                medida = new MedidaA1BCVO();
            }
        } catch (Exception ex) {
            log.error("Se ha producido un error recuperando Medidas A1 ", ex);
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
    
    public List<MedidaA2VO> getDatosMedidasA2(String numExp, int codOrganizacion, Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        List<MedidaA2VO> lista = new ArrayList<MedidaA2VO>();
        MedidaA2VO medida = new MedidaA2VO();
        try {
            String query = null;
            query = "SELECT * FROM " + ConfigurationParameter.getParameter(ConstantesMeLanbide72.MELANBIDE72_MEDIDAS_ALT_A2, ConstantesMeLanbide72.FICHERO_PROPIEDADES)
                    + " WHERE NUM_EXP ='" + numExp + "'"
                    + " ORDER BY ID";

            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                medida = (MedidaA2VO) MeLanbide72MappingUtils.getInstance().mapA2(rs, MedidaA2VO.class);
               
                lista.add(medida);
                medida = new MedidaA2VO();
            }
        } catch (Exception ex) {
            log.error("Se ha producido un error recuperando Medidas A2 ", ex);
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
    
    public List<MedidaA1BCVO> getDatosMedidasB(String numExp, int codOrganizacion, Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        List<MedidaA1BCVO> lista = new ArrayList<MedidaA1BCVO>();
        MedidaA1BCVO medida = new MedidaA1BCVO();
        try {
            String query = null;
            query = "SELECT * FROM " + ConfigurationParameter.getParameter(ConstantesMeLanbide72.MELANBIDE72_MEDIDAS_ALT_B, ConstantesMeLanbide72.FICHERO_PROPIEDADES)
                    + " WHERE NUM_EXP ='" + numExp + "'"
                    + " ORDER BY ID";

            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                medida = (MedidaA1BCVO) MeLanbide72MappingUtils.getInstance().mapBC(rs, MedidaA1BCVO.class);
              
                lista.add(medida);
                medida = new MedidaA1BCVO();
            }
        } catch (Exception ex) {
            log.error("Se ha producido un error recuperando Medidas B ", ex);
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
    
    public List<MedidaA1BCVO> getDatosMedidasC(String numExp, int codOrganizacion, Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        List<MedidaA1BCVO> lista = new ArrayList<MedidaA1BCVO>();
        MedidaA1BCVO medida = new MedidaA1BCVO();
        try {
            String query = null;
            query = "SELECT * FROM " + ConfigurationParameter.getParameter(ConstantesMeLanbide72.MELANBIDE72_MEDIDAS_ALT_C, ConstantesMeLanbide72.FICHERO_PROPIEDADES)
                    + " WHERE NUM_EXP ='" + numExp + "'"
                    + " ORDER BY ID";

            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                medida = (MedidaA1BCVO) MeLanbide72MappingUtils.getInstance().mapBC(rs, MedidaA1BCVO.class);
              
                lista.add(medida);
                medida = new MedidaA1BCVO();
            }
        } catch (Exception ex) {
            log.error("Se ha producido un error recuperando Medidas C ", ex);
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

    public Double getCampoSuplementarioNumerico(String numExp, int codOrganizacion, String codCampSupl, Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        Double valor_camp_supl_num = 0.0;
        try {
            String query = null;
            query = "SELECT TNU_VALOR FROM " + ConfigurationParameter.getParameter(ConstantesMeLanbide72.TABLA_VALORES_CAMPOS_SUPLEMENTARIOS_NUMERICOS, ConstantesMeLanbide72.FICHERO_PROPIEDADES)
                    + " WHERE TNU_NUM ='" + numExp + "'"
                    + " AND TNU_COD='" + codCampSupl + "'";

            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            rs = st.executeQuery(query);
            if (rs.next()) {
                valor_camp_supl_num = rs.getDouble("TNU_VALOR");
            }
        } catch (Exception ex) {
            log.error("Se ha producido un error recuperando Campo suplementario numérico ", ex);
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
        return valor_camp_supl_num;
    }
    
    public MedidaA1BCVO getMedidaA1PorID(String id, Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        MedidaA1BCVO medida = new MedidaA1BCVO();
        try {
            String query = null;
            query = "SELECT * FROM " + ConfigurationParameter.getParameter(ConstantesMeLanbide72.MELANBIDE72_MEDIDAS_ALT_A1, ConstantesMeLanbide72.FICHERO_PROPIEDADES)
                    + " WHERE ID=" + (id != null && !id.equals("") ? id : "null");
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                medida = (MedidaA1BCVO) MeLanbide72MappingUtils.getInstance().mapA1(rs, MedidaA1BCVO.class);
            }
        } catch (Exception ex) {
            log.error("Se ha producido un error recuperando una medida A1 : " + id, ex);
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
        return medida;
    }
    
    public MedidaA2VO getMedidaA2PorID(String id, Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        MedidaA2VO medida = new MedidaA2VO();
        try {
            String query = null;
            query = "SELECT * FROM " + ConfigurationParameter.getParameter(ConstantesMeLanbide72.MELANBIDE72_MEDIDAS_ALT_A2, ConstantesMeLanbide72.FICHERO_PROPIEDADES)
                    + " WHERE ID=" + (id != null && !id.equals("") ? id : "null");
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                medida = (MedidaA2VO) MeLanbide72MappingUtils.getInstance().mapA2(rs, MedidaA2VO.class);
            }
        } catch (Exception ex) {
            log.error("Se ha producido un error recuperando una medida A2 : " + id, ex);
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
        return medida;
    }
    
    public MedidaA1BCVO getMedidaBPorID(String id, Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        MedidaA1BCVO medida = new MedidaA1BCVO();
        try {
            String query = null;
            query = "SELECT * FROM " + ConfigurationParameter.getParameter(ConstantesMeLanbide72.MELANBIDE72_MEDIDAS_ALT_B, ConstantesMeLanbide72.FICHERO_PROPIEDADES)
                    + " WHERE ID=" + (id != null && !id.equals("") ? id : "null");
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                medida = (MedidaA1BCVO) MeLanbide72MappingUtils.getInstance().mapBC(rs, MedidaA1BCVO.class);
            }
        } catch (Exception ex) {
            log.error("Se ha producido un error recuperando una medida B : " + id, ex);
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
        return medida;
    }
    
    public MedidaA1BCVO getMedidaCPorID(String id, Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        MedidaA1BCVO medida = new MedidaA1BCVO();
        try {
            String query = null;
            query = "SELECT * FROM " + ConfigurationParameter.getParameter(ConstantesMeLanbide72.MELANBIDE72_MEDIDAS_ALT_C, ConstantesMeLanbide72.FICHERO_PROPIEDADES)
                    + " WHERE ID=" + (id != null && !id.equals("") ? id : "null");
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                medida = (MedidaA1BCVO) MeLanbide72MappingUtils.getInstance().mapBC(rs, MedidaA1BCVO.class);
            }
        } catch (Exception ex) {
            log.error("Se ha producido un error recuperando una medida C : " + id, ex);
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
        return medida;
    }
    
    public boolean crearNuevaMedidaA1(MedidaA1BCVO nuevaMedidaA1, Connection con) throws Exception {
        Statement st = null;
        String query = "";

        try {
            int id = recogerIDInsertar(ConfigurationParameter.getParameter(ConstantesMeLanbide72.SEQ_MELANBIDE72_MEDIDAS_ALT_A1, ConstantesMeLanbide72.FICHERO_PROPIEDADES), con);
            
            query = "INSERT INTO " + ConfigurationParameter.getParameter(ConstantesMeLanbide72.MELANBIDE72_MEDIDAS_ALT_A1, ConstantesMeLanbide72.FICHERO_PROPIEDADES)
                    + "(ID,NUM_EXP,NOMBRE,NIF_CIF,IMPORTE_ANUAL,OBJETO_CONTRATO) "
                    + " VALUES (" + id
                    + ", '" + nuevaMedidaA1.getNumExp()
                    + "', '" + nuevaMedidaA1.getNombre()
                    + "', '" + nuevaMedidaA1.getNif()
                    + "', " + nuevaMedidaA1.getImporteAnual()
                    + ", '" + nuevaMedidaA1.getObjetoContrato() + "')";
           
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            int insert = st.executeUpdate(query);
            if (insert > 0) {
                return true;
            } else {
                log.debug("No Se ha podido guardar una nueva Medida A1 ");
                return false;
            }
        } catch (Exception ex) {
            con.rollback();
            log.debug("Se ha producido un error al Insertar una nueva Medida A1 " + ex.getMessage());
            throw new Exception(ex);
        } finally {
            if (log.isDebugEnabled()) {
                log.debug("Procedemos a cerrar el statement ");
            }
            if (st != null) {
                st.close();
            }
        }
    }
    
    public boolean modificarMedidaA1(MedidaA1BCVO datModif, Connection con) throws Exception {
        Statement st = null;
        String query = "";
        try {
            query = "UPDATE " + ConfigurationParameter.getParameter(ConstantesMeLanbide72.MELANBIDE72_MEDIDAS_ALT_A1, ConstantesMeLanbide72.FICHERO_PROPIEDADES)
                    + " SET NOMBRE='" + datModif.getNombre() + "'"
                    + ", NIF_CIF='" + datModif.getNif() + "'"
                    + ", IMPORTE_ANUAL=" + datModif.getImporteAnual()
                    + ", OBJETO_CONTRATO='" + datModif.getObjetoContrato() + "'"
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
            con.rollback();
            log.debug("Se ha producido un error al Modificar un registro de Medida A1 - "
                    + datModif.getId() + " - " + ex);
            throw new Exception(ex);
        } finally {
            if (log.isDebugEnabled()) {
                log.debug("Procedemos a cerrar el statement ");
            }
            if (st != null) {
                st.close();
            }
        }
    }
    
    public int eliminarMedidaA1(String id, Connection con) throws Exception {
        Statement st = null;
        try {
            String query = null;
            query = "DELETE FROM " + ConfigurationParameter.getParameter(ConstantesMeLanbide72.MELANBIDE72_MEDIDAS_ALT_A1, ConstantesMeLanbide72.FICHERO_PROPIEDADES)
                    + " WHERE ID=" + (id!=null && !id.equals("") ? id : "null")
                    ;
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            return st.executeUpdate(query);
        } catch (Exception ex) {
            con.rollback();
            log.error("Se ha producido un error Eliminando Medida A1 ID : " + id, ex);
            throw new Exception(ex);
        } finally {
            if (log.isDebugEnabled()) {
                log.debug("Procedemos a cerrar el statement ");
            }
            if (st != null) {
                st.close();
            }
        }
    }
    
    public boolean crearNuevaMedidaA2(MedidaA2VO nuevaMedidaA2, Connection con) throws Exception {
        Statement st = null;
        String query = "";

        try {
            int id = recogerIDInsertar(ConfigurationParameter.getParameter(ConstantesMeLanbide72.SEQ_MELANBIDE72_MEDIDAS_ALT_A2, ConstantesMeLanbide72.FICHERO_PROPIEDADES), con);
            
            query = "INSERT INTO " + ConfigurationParameter.getParameter(ConstantesMeLanbide72.MELANBIDE72_MEDIDAS_ALT_A2, ConstantesMeLanbide72.FICHERO_PROPIEDADES)
                    + "(ID,NUM_EXP,NOMBRE,APELLIDO1,APELLIDO2,TIPO_DOCUMENTO,DNI_NIE,TFNO,EMAIL,PROVINCIA,MUNICIPIO,LOCALIDAD,DIRECCION,PORTAL,PISO,LETRA,C_POSTAL,IMPORTE_ANUAL,OBJETO_CONTRATO) "
                    + " VALUES (" + id
                    + ", '" + nuevaMedidaA2.getNumExp()
                    + "', '" + nuevaMedidaA2.getNombre()
                    + "', '" + nuevaMedidaA2.getApellido1()
                    + "', '" + nuevaMedidaA2.getApellido2()
                    + "', '" + nuevaMedidaA2.getTipoDocumento()
                    + "', '" + nuevaMedidaA2.getDni()
                    + "', '" + nuevaMedidaA2.getTfno()
                    + "', '" + nuevaMedidaA2.getEmail()
                    + "', '" + nuevaMedidaA2.getProvincia()
                    + "', '" + nuevaMedidaA2.getMunicipio()
                    + "', '" + nuevaMedidaA2.getLocalidad()
                    + "', '" + nuevaMedidaA2.getDireccion()
                    + "', '" + nuevaMedidaA2.getPortal()
                    + "', '" + nuevaMedidaA2.getPiso()
                    + "', '" + nuevaMedidaA2.getLetra()
                    + "', '" + nuevaMedidaA2.getC_postal()
                    + "', " + nuevaMedidaA2.getImporteAnual()
                    + ", '" + nuevaMedidaA2.getObjetoContrato() + "')";
           
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            int insert = st.executeUpdate(query);
            if (insert > 0) {
                return true;
            } else {
                log.debug("No Se ha podido guardar una nueva Medida A2 ");
                return false;
            }
        } catch (Exception ex) {
            con.rollback();
            log.debug("Se ha producido un error al Insertar una nueva Medida A2 " + ex.getMessage());
            throw new Exception(ex);
        } finally {
            if (log.isDebugEnabled()) {
                log.debug("Procedemos a cerrar el statement ");
            }
            if (st != null) {
                st.close();
            }
        }
    }
    
    public boolean modificarMedidaA2(MedidaA2VO datModif, Connection con) throws Exception {
        Statement st = null;
        String query = "";
        try {
            query = "UPDATE " + ConfigurationParameter.getParameter(ConstantesMeLanbide72.MELANBIDE72_MEDIDAS_ALT_A2, ConstantesMeLanbide72.FICHERO_PROPIEDADES)
                    + " SET NOMBRE='" + datModif.getNombre() + "'"
                    + ", APELLIDO1='" + datModif.getApellido1()+ "'"
                    + ", APELLIDO2='" + datModif.getApellido2()+ "'"
                    + ", TIPO_DOCUMENTO='" + datModif.getTipoDocumento()+ "'"
                    + ", DNI_NIE='" + datModif.getDni() + "'"
                    + ", TFNO='" + datModif.getTfno()+ "'"
                    + ", EMAIL='" + datModif.getEmail()+ "'"
                    + ", PROVINCIA='" + datModif.getProvincia()+ "'"
                    + ", MUNICIPIO='" + datModif.getMunicipio()+ "'"
                    + ", LOCALIDAD='" + datModif.getLocalidad()+ "'"
                    + ", DIRECCION='" + datModif.getDireccion()+ "'"
                    + ", PORTAL='" + datModif.getPortal()+ "'"
                    + ", PISO='" + datModif.getPiso()+ "'"
                    + ", LETRA='" + datModif.getLetra()+ "'"
                    + ", C_POSTAL='" + datModif.getC_postal()+ "'"
                    + ", IMPORTE_ANUAL=" + datModif.getImporteAnual()
                    + ", OBJETO_CONTRATO='" + datModif.getObjetoContrato() + "'"
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
            con.rollback();
            log.debug("Se ha producido un error al Modificar un registro de Medida A2 - "
                    + datModif.getId() + " - " + ex);
            throw new Exception(ex);
        } finally {
            if (log.isDebugEnabled()) {
                log.debug("Procedemos a cerrar el statement ");
            }
            if (st != null) {
                st.close();
            }
        }
    }
    
    public int eliminarMedidaA2(String id, Connection con) throws Exception {
        Statement st = null;
        try {
            String query = null;
            query = "DELETE FROM " + ConfigurationParameter.getParameter(ConstantesMeLanbide72.MELANBIDE72_MEDIDAS_ALT_A2, ConstantesMeLanbide72.FICHERO_PROPIEDADES)
                    + " WHERE ID=" + (id!=null && !id.equals("") ? id : "null")
                    ;
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            return st.executeUpdate(query);
        } catch (Exception ex) {
            con.rollback();
            log.error("Se ha producido un error Eliminando Medida A2 ID : " + id, ex);
            throw new Exception(ex);
        } finally {
            if (log.isDebugEnabled()) {
                log.debug("Procedemos a cerrar el statement ");
            }
            if (st != null) {
                st.close();
            }
        }
    }
    
    public boolean crearNuevaMedidaB(MedidaA1BCVO nuevaMedidaB, Connection con) throws Exception {
        Statement st = null;
        String query = "";

        try {
            int id = recogerIDInsertar(ConfigurationParameter.getParameter(ConstantesMeLanbide72.SEQ_MELANBIDE72_MEDIDAS_ALT_B, ConstantesMeLanbide72.FICHERO_PROPIEDADES), con);
            
            query = "INSERT INTO " + ConfigurationParameter.getParameter(ConstantesMeLanbide72.MELANBIDE72_MEDIDAS_ALT_B, ConstantesMeLanbide72.FICHERO_PROPIEDADES)
                    + "(ID,NUM_EXP,NOMBRE,NIF_CIF,IMPORTE_ANUAL) "
                    + " VALUES (" + id
                    + ", '" + nuevaMedidaB.getNumExp()
                    + "', '" + nuevaMedidaB.getNombre()
                    + "', '" + nuevaMedidaB.getNif()
                    + "', " + nuevaMedidaB.getImporteAnual() + ")";
           
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            int insert = st.executeUpdate(query);
            if (insert > 0) {
                return true;
            } else {
                log.debug("No Se ha podido guardar una nueva Medida B ");
                return false;
            }
        } catch (Exception ex) {
            con.rollback();
            log.debug("Se ha producido un error al Insertar una nueva Medida B " + ex.getMessage());
            throw new Exception(ex);
        } finally {
            if (log.isDebugEnabled()) {
                log.debug("Procedemos a cerrar el statement ");
            }
            if (st != null) {
                st.close();
            }
        }
    }
    
    public boolean modificarMedidaB(MedidaA1BCVO datModif, Connection con) throws Exception {
        Statement st = null;
        String query = "";
        try {
            query = "UPDATE " + ConfigurationParameter.getParameter(ConstantesMeLanbide72.MELANBIDE72_MEDIDAS_ALT_B, ConstantesMeLanbide72.FICHERO_PROPIEDADES)
                    + " SET NOMBRE='" + datModif.getNombre() + "'"
                    + ", NIF_CIF='" + datModif.getNif() + "'"
                    + ", IMPORTE_ANUAL=" + datModif.getImporteAnual()
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
            con.rollback();
            log.debug("Se ha producido un error al Modificar un registro de Medida B - "
                    + datModif.getId() + " - " + ex);
            throw new Exception(ex);
        } finally {
            if (log.isDebugEnabled()) {
                log.debug("Procedemos a cerrar el statement ");
            }
            if (st != null) {
                st.close();
            }
        }
    }
    
    public int eliminarMedidaB(String id, Connection con) throws Exception {
        Statement st = null;
        try {
            String query = null;
            query = "DELETE FROM " + ConfigurationParameter.getParameter(ConstantesMeLanbide72.MELANBIDE72_MEDIDAS_ALT_B, ConstantesMeLanbide72.FICHERO_PROPIEDADES)
                    + " WHERE ID=" + (id!=null && !id.equals("") ? id : "null")
                    ;
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            return st.executeUpdate(query);
        } catch (Exception ex) {
            con.rollback();
            log.error("Se ha producido un error Eliminando Medida B ID : " + id, ex);
            throw new Exception(ex);
        } finally {
            if (log.isDebugEnabled()) {
                log.debug("Procedemos a cerrar el statement ");
            }
            if (st != null) {
                st.close();
            }
        }
    }
    
    public boolean crearNuevaMedidaC(MedidaA1BCVO nuevaMedidaC, Connection con) throws Exception {
        Statement st = null;
        String query = "";

        try {
            int id = recogerIDInsertar(ConfigurationParameter.getParameter(ConstantesMeLanbide72.SEQ_MELANBIDE72_MEDIDAS_ALT_C, ConstantesMeLanbide72.FICHERO_PROPIEDADES), con);
            
            query = "INSERT INTO " + ConfigurationParameter.getParameter(ConstantesMeLanbide72.MELANBIDE72_MEDIDAS_ALT_C, ConstantesMeLanbide72.FICHERO_PROPIEDADES)
                    + "(ID,NUM_EXP,NOMBRE,NIF_CIF,IMPORTE_ANUAL) "
                    + " VALUES (" + id
                    + ", '" + nuevaMedidaC.getNumExp()
                    + "', '" + nuevaMedidaC.getNombre()
                    + "', '" + nuevaMedidaC.getNif()
                    + "', " + nuevaMedidaC.getImporteAnual() + ")";
           
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            int insert = st.executeUpdate(query);
            if (insert > 0) {
                return true;
            } else {
                log.debug("No Se ha podido guardar una nueva Medida C ");
                return false;
            }
        } catch (Exception ex) {
            con.rollback();
            log.debug("Se ha producido un error al Insertar una nueva Medida C " + ex.getMessage());
            throw new Exception(ex);
        } finally {
            if (log.isDebugEnabled()) {
                log.debug("Procedemos a cerrar el statement ");
            }
            if (st != null) {
                st.close();
            }
        }
    }
    
    public boolean modificarMedidaC(MedidaA1BCVO datModif, Connection con) throws Exception {
        Statement st = null;
        String query = "";
        try {
            query = "UPDATE " + ConfigurationParameter.getParameter(ConstantesMeLanbide72.MELANBIDE72_MEDIDAS_ALT_C, ConstantesMeLanbide72.FICHERO_PROPIEDADES)
                    + " SET NOMBRE='" + datModif.getNombre() + "'"
                    + ", NIF_CIF='" + datModif.getNif() + "'"
                    + ", IMPORTE_ANUAL=" + datModif.getImporteAnual()
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
            con.rollback();
            log.debug("Se ha producido un error al Modificar un registro de Medida C - "
                    + datModif.getId() + " - " + ex);
            throw new Exception(ex);
        } finally {
            if (log.isDebugEnabled()) {
                log.debug("Procedemos a cerrar el statement ");
            }
            if (st != null) {
                st.close();
            }
        }
    }
    
    public int eliminarMedidaC(String id, Connection con) throws Exception {
        Statement st = null;
        try {
            String query = null;
            query = "DELETE FROM " + ConfigurationParameter.getParameter(ConstantesMeLanbide72.MELANBIDE72_MEDIDAS_ALT_C, ConstantesMeLanbide72.FICHERO_PROPIEDADES)
                    + " WHERE ID=" + (id!=null && !id.equals("") ? id : "null")
                    ;
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            return st.executeUpdate(query);
        } catch (Exception ex) {
            con.rollback();
            log.error("Se ha producido un error Eliminando Medida C ID : " + id, ex);
            throw new Exception(ex);
        } finally {
            if (log.isDebugEnabled()) {
                log.debug("Procedemos a cerrar el statement ");
            }
            if (st != null) {
                st.close();
            }
        }
    }
    
    // Funciones Privadas
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
    
    //campos suplementarios numéricos necesarios para el formulario (tabla E_TNU, campos TNU_COD, TNU_VALOR para TNU_NUM='numExp')
                //A1NUMTRABSUST __  TRABAJADORES SUSTITUYE CONTRATACION DISCP CENTRO EMPLEO A1 (num_trab_sust_A1)
                //A1PERANIOS __  PERIODO AÑOS CENTRO EMPLEO A1 (periodo_contr_A1)
                
                //A2NUMTRABSUST __  TRABAJADORES SUSTITUYE CONTRATACION DISCP AUTÓNOMA A2 (num_trab_sust_A2)
                //A2PERANIOS __  PERIODO AÑOS AUTÓNOMA A2 (periodo_contr_A2)
                
                //BNUMTRABSUST __  TRABAJADORES SUSTITUYE CONTRATACION DISCP DONACIÓN B (num_trab_sust_B)
                //BPERANIOS __  PERIODO AÑOS DONACIÓN B (periodo_contr_B)
                
                //CNUMTRABSUST __  TRABAJADORES SUSTITUYE CONTRATACION DISCP ENCLAVE C (num_trab_sust_C)
                //CPERANIOS __  PERIODO AÑOS ENCLAVE C (periodo_contr_C)
                //CPERDISCPENCL __  TRABAJADORES DISCP OCUPADOS EN EL ENCLAVE C (pers_disc_ocup_C)

                //CECON CUANTIFICACION ECONOMICA ANUAL (cuant_econ_anual)
                //IMPSIM IMPORTE ANUAL TOTAL (imp_anual_total)
                //TRABSIM CONTRATACION TOTAL (contratacion_total)
    public boolean guardarSuplementarios(int codOrganizacion, String numExp, String num_trab_sust_A1,
                                                                             String periodo_contr_A1,
                                                                             String num_trab_sust_A2,
                                                                             String periodo_contr_A2,
                                                                             String num_trab_sust_B,
                                                                             String periodo_contr_B,
                                                                             String num_trab_sust_C,
                                                                             String periodo_contr_C,
                                                                             String pers_disc_ocup_C,
                                                                             String cuantificacion_econ,
                                                                             String imp_anual_total,
                                                                             String contratacion_total, 
                                         Connection con) throws Exception {
    
        String codigo_num_trab_sust_A1 = "A1NUMTRABSUST";
        String codigo_periodo_contr_A1 = "A1PERANIOS";
        String codigo_num_trab_sust_A2 = "A2NUMTRABSUST";
        String codigo_periodo_contr_A2 = "A2PERANIOS";
        String codigo_num_trab_sust_B = "BNUMTRABSUST";
        String codigo_periodo_contr_B = "BPERANIOS";
        String codigo_num_trab_sust_C = "CNUMTRABSUST";
        String codigo_periodo_contr_C = "CPERANIOS";
        String codigo_pers_disc_ocup_C = "CPERDISCPENCL";
        String codigo_cuantificacion_econ = "CECON";
        String codigo_imp_anual_total = "IMPSIM";
        String codigo_contratacion_total = "TRABSIM";
        
        try {
            guardarCampoSuplementario(codOrganizacion, numExp, num_trab_sust_A1, codigo_num_trab_sust_A1, con);
            guardarCampoSuplementario(codOrganizacion, numExp, periodo_contr_A1, codigo_periodo_contr_A1, con);
            guardarCampoSuplementario(codOrganizacion, numExp, num_trab_sust_A2, codigo_num_trab_sust_A2, con);
            guardarCampoSuplementario(codOrganizacion, numExp, periodo_contr_A2, codigo_periodo_contr_A2, con);
            guardarCampoSuplementario(codOrganizacion, numExp, num_trab_sust_B, codigo_num_trab_sust_B, con);
            guardarCampoSuplementario(codOrganizacion, numExp, periodo_contr_B, codigo_periodo_contr_B, con);
            guardarCampoSuplementario(codOrganizacion, numExp, num_trab_sust_C, codigo_num_trab_sust_C, con);
            guardarCampoSuplementario(codOrganizacion, numExp, periodo_contr_C, codigo_periodo_contr_C, con);
            guardarCampoSuplementario(codOrganizacion, numExp, pers_disc_ocup_C, codigo_pers_disc_ocup_C, con);
            guardarCampoSuplementario(codOrganizacion, numExp, cuantificacion_econ, codigo_cuantificacion_econ, con);
            guardarCampoSuplementario(codOrganizacion, numExp, imp_anual_total, codigo_imp_anual_total, con);
            guardarCampoSuplementario(codOrganizacion, numExp, contratacion_total, codigo_contratacion_total, con);
            
        } catch (Exception ex) {
            con.rollback();
            log.debug("Se ha producido un error al guardar los suplementarios" + ex.getMessage());
            throw new Exception(ex);
        } 
      
        return true;
            
    }
            
    /*SELECT TNU_VALOR FROM E_TNU WHERE TNU_NUM='2020/DECEX/000002' AND TNU_COD='A1NUMTRABSUST';

    MERGE INTO E_TNU USING (SELECT 1 FROM DUAL) ON (TNU_NUM = '2020/DECEX/000002' AND TNU_COD = 'A1NUMTRABSUST' AND TNU_MUN = 1) WHEN MATCHED THEN UPDATE SET TNU_VALOR = 100
    WHEN NOT MATCHED THEN INSERT (TNU_MUN, TNU_EJE, TNU_NUM, TNU_COD, TNU_VALOR) VALUES (1, 2020, '2020/DECEX/000002', 'A1NUMTRABSUST', 100);

    DELETE FROM E_TNU WHERE TNU_NUM='2020/DECEX/000002' AND TNU_COD='A1NUMTRABSUST' AND TNU_MUN=1;*/
    private void guardarCampoSuplementario(int codOrganizacion, String numExp, String valorCampo, String codigoCampo, Connection con) throws Exception {
        String query = "";
        Statement st = null;
        Double vCampo = 0.0;
        
        if (valorCampo != null && !"".equals(valorCampo) && !"0,0".equals(valorCampo) && !"0".equals(valorCampo)) {
            vCampo = Double.parseDouble(valorCampo.replace(",", "."));
        }
        
        try {
            if (vCampo == 0 || vCampo == null || vCampo == 0.0){
                //delete
                query = "DELETE FROM E_TNU WHERE TNU_NUM='" + numExp + "' AND TNU_COD='" + codigoCampo + "' AND TNU_MUN=" + codOrganizacion;
                if (log.isDebugEnabled()) {
                    log.debug("sql = " + query);
                }
                st = con.createStatement();
                st.executeUpdate(query);
                st.close();
            } else {
                //merge into
                query = "MERGE INTO E_TNU USING (SELECT 1 FROM DUAL) ON (TNU_NUM = '"
                        + numExp
                        + "' AND TNU_COD = '"
                        + codigoCampo
                        + "' AND TNU_MUN ="
                        + codOrganizacion
                        + ") WHEN MATCHED THEN UPDATE SET TNU_VALOR ="
                        + vCampo
                        + " WHEN NOT MATCHED THEN INSERT (TNU_MUN, TNU_EJE, TNU_NUM, TNU_COD, TNU_VALOR) VALUES ("
                        + codOrganizacion
                        + ", '"
                        + numExp.substring(0, 4) + "', '" 
                        + numExp + "', '" 
                        + codigoCampo + "', " 
                        + vCampo + ")";
                
                if (log.isDebugEnabled()) {
                    log.debug("sql = " + query);
                }
                st = con.createStatement();
                st.executeUpdate(query);
                st.close();
            }
        } catch (Exception ex) {
            con.rollback();
            log.debug("Se ha producido un error al guardar los campos suplementarios" + ex.getMessage());
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

    public List<RegistroBatchVO> getListaRegistrosDECEX3Meses(Connection con) throws SQLException, Exception {

        log.info(" BEGIN getListaRegistrosDECEX3Meses  ");
        Statement st = null;
        ResultSet rs = null;
        List<RegistroBatchVO> listReg = new ArrayList<RegistroBatchVO>();
  try {
            String query = null;
            //Obtenemos todos los que llevan mas de tres meses des de el acuse de recepcion de la notificacion de resolucion y no estan abierto ya el tramite de recepcion de los tres meses
           
            query = "SELECT CRO_EJE, CRO_USU, CRO_NUM, CRO_FEF, CRO_UTR FROM ("
                    + " SELECT *"
                    + " FROM  " + ConfigurationParameter.getParameter(ConstantesMeLanbide72.TABLA_OCURRENCIAS, ConstantesMeLanbide72.FICHERO_PROPIEDADES)
                    + " WHERE CRO_NUM IN "
                    + "(SELECT CRO_NUM FROM " + ConfigurationParameter.getParameter(ConstantesMeLanbide72.TABLA_OCURRENCIAS, ConstantesMeLanbide72.FICHERO_PROPIEDADES) 
                    + " WHERE CRO_PRO = '"+ConfigurationParameter.getParameter(ConstantesMeLanbide72.COD_PROCEDIMIENTO, ConstantesMeLanbide72.FICHERO_PROPIEDADES)+"' "
                    + " AND CRO_TRA = "+ConfigurationParameter.getParameter(ConstantesMeLanbide72.ACUSE_RECIBO_NOTIFICACION_RESOLUCION, ConstantesMeLanbide72.FICHERO_PROPIEDADES)+" AND  "
                    + " (CRO_FEF IS NOT null AND ADD_MONTHS(CRO_FEF,3) < sysdate) AND CRO_NUM NOT IN (SELECT CRO_NUM "
                    + " FROM "+ ConfigurationParameter.getParameter(ConstantesMeLanbide72.TABLA_OCURRENCIAS, ConstantesMeLanbide72.FICHERO_PROPIEDADES)
                    + " WHERE CRO_PRO = '"+ConfigurationParameter.getParameter(ConstantesMeLanbide72.COD_PROCEDIMIENTO, ConstantesMeLanbide72.FICHERO_PROPIEDADES)+"' AND CRO_TRA = "+ ConfigurationParameter.getParameter(ConstantesMeLanbide72.TRAM_DECEX_RECEP_REG_ENT_3M, ConstantesMeLanbide72.FICHERO_PROPIEDADES)+"))"
                    + " AND CRO_TRA = "+ConfigurationParameter.getParameter(ConstantesMeLanbide72.ACUSE_RECIBO_NOTIFICACION_RESOLUCION, ConstantesMeLanbide72.FICHERO_PROPIEDADES)+""
                    + " AND CRO_NUM IN ( SELECT EXP_NUM FROM "+ConfigurationParameter.getParameter(ConstantesMeLanbide72.TABLA_EXPEDIENTES, ConstantesMeLanbide72.FICHERO_PROPIEDADES)+" WHERE EXP_EST = 0 ))";
            
            
            
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                listReg.add((RegistroBatchVO) MeLanbide72MappingUtils.getInstance().mapRegistroBatch(rs));
            }
        } catch (Exception ex) {
            log.error("Se ha producido un error en getListaRegistrosDECEX3Meses ", ex);
            throw new Exception(ex);
        } finally {
            log.debug(" END getListaRegistrosDECEX3Meses  ");
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
        return listReg;

    }
    
   
    

     public List<RegistroBatchVO> getListaRegistrosDECEXAños(Connection con,Integer años) throws SQLException, Exception {

        log.info(" BEGIN getListaRegistrosDECEX3Meses  ");
        Statement st = null;
        ResultSet rs = null;
        List<RegistroBatchVO> listReg = new ArrayList<RegistroBatchVO>();
        
        //if para saber si es año 1 2 o 3 
        String procedimiento="";
        //Usaremos este para saber si el anterior procedimiento a sido contestado 
        String procedimientoAnterior="";
        String procedimientoPosterior2="";
        String procedimientoPosterior3="";
        String meses="";
        if (años == 1 ){
            procedimientoAnterior= ConfigurationParameter.getParameter(ConstantesMeLanbide72.ACUSE_RECIBO_NOTIFICACION_RESOLUCION, ConstantesMeLanbide72.FICHERO_PROPIEDADES);
            procedimiento= ConfigurationParameter.getParameter(ConstantesMeLanbide72.TRAM_DECEX_RECEP_REG_ENT_A1, ConstantesMeLanbide72.FICHERO_PROPIEDADES);
            procedimientoPosterior3= ConfigurationParameter.getParameter(ConstantesMeLanbide72.TRAM_DECEX_RECEP_REG_ENT_A2, ConstantesMeLanbide72.FICHERO_PROPIEDADES);
            procedimientoPosterior2= ConfigurationParameter.getParameter(ConstantesMeLanbide72.TRAM_DECEX_RECEP_REG_ENT_A2, ConstantesMeLanbide72.FICHERO_PROPIEDADES);
            meses="12";
        }
        if (años == 2 ){
            procedimientoPosterior3= ConfigurationParameter.getParameter(ConstantesMeLanbide72.TRAM_DECEX_RECEP_REG_ENT_A3, ConstantesMeLanbide72.FICHERO_PROPIEDADES);
            procedimientoAnterior= ConfigurationParameter.getParameter(ConstantesMeLanbide72.TRAM_DECEX_RECEP_REG_ENT_A1, ConstantesMeLanbide72.FICHERO_PROPIEDADES);
            procedimiento= ConfigurationParameter.getParameter(ConstantesMeLanbide72.TRAM_DECEX_RECEP_REG_ENT_A2, ConstantesMeLanbide72.FICHERO_PROPIEDADES);
            meses="24";
        }
             
         if (años == 3 ){
            procedimientoAnterior= ConfigurationParameter.getParameter(ConstantesMeLanbide72.TRAM_DECEX_RECEP_REG_ENT_A2, ConstantesMeLanbide72.FICHERO_PROPIEDADES);
            procedimiento= ConfigurationParameter.getParameter(ConstantesMeLanbide72.TRAM_DECEX_RECEP_REG_ENT_A3, ConstantesMeLanbide72.FICHERO_PROPIEDADES);
            meses="36";
        }
        
        
  try {
            String query = null;
            //Obtenemos todos los que llevan mas de tres meses des de el acuse de recepcion de la notificacion de resolucion y no estan abierto ya el tramite de recepcion de los tres meses
           
            query = "SELECT CRO_EJE, CRO_USU, CRO_NUM, CRO_FEF, CRO_UTR FROM ("
                    + " SELECT *"
                    + " FROM  " + ConfigurationParameter.getParameter(ConstantesMeLanbide72.TABLA_OCURRENCIAS, ConstantesMeLanbide72.FICHERO_PROPIEDADES)
                    + " WHERE CRO_NUM IN "
                    + "(SELECT CRO_NUM FROM " + ConfigurationParameter.getParameter(ConstantesMeLanbide72.TABLA_OCURRENCIAS, ConstantesMeLanbide72.FICHERO_PROPIEDADES) 
                    + " WHERE CRO_PRO = '"+ConfigurationParameter.getParameter(ConstantesMeLanbide72.COD_PROCEDIMIENTO, ConstantesMeLanbide72.FICHERO_PROPIEDADES)+"' "
                    + " AND CRO_TRA = "+ConfigurationParameter.getParameter(ConstantesMeLanbide72.ACUSE_RECIBO_NOTIFICACION_RESOLUCION, ConstantesMeLanbide72.FICHERO_PROPIEDADES)+" AND  "
                    + " (CRO_FEF IS NOT null AND ADD_MONTHS(CRO_FEF,"+meses+") < sysdate) AND CRO_NUM NOT IN (SELECT CRO_NUM "
                    + " FROM "+ ConfigurationParameter.getParameter(ConstantesMeLanbide72.TABLA_OCURRENCIAS, ConstantesMeLanbide72.FICHERO_PROPIEDADES)
                    + " WHERE CRO_PRO = '"+ConfigurationParameter.getParameter(ConstantesMeLanbide72.COD_PROCEDIMIENTO, ConstantesMeLanbide72.FICHERO_PROPIEDADES)+"' AND CRO_TRA = "+ procedimiento+"))"
                    + " AND (CRO_TRA = "+ConfigurationParameter.getParameter(ConstantesMeLanbide72.TRAM_DECEX_RECEP_REG_ENT_3M, ConstantesMeLanbide72.FICHERO_PROPIEDADES)
                    + " AND CRO_FEF IS NOT NULL)"
                    + " AND CRO_NUM IN ( SELECT EXP_NUM FROM "+ConfigurationParameter.getParameter(ConstantesMeLanbide72.TABLA_EXPEDIENTES, ConstantesMeLanbide72.FICHERO_PROPIEDADES)+" WHERE EXP_EST = 0 ))"
                    + " WHERE CRO_NUM NOT IN (SELECT CRO_NUM FROM E_TDE WHERE CRO_NUM = TDE_NUM AND TDE_VALOR='S' AND TDE_COD='ENTREGAAÑO"+años+"')";
            
            //Depende de el año añadimos condiciones si el anterior esta realizado
            if (años == 3 ){
               query += " AND (cro_num IN (SELECT cro_num from " + ConfigurationParameter.getParameter(ConstantesMeLanbide72.TABLA_OCURRENCIAS, ConstantesMeLanbide72.FICHERO_PROPIEDADES)
                       + " tramite where tramite.cro_num=cro_num and tramite.cro_tra = "+procedimientoAnterior+" and tramite.cro_fef IS NOT NULL)"
                       + " OR cro_num IN ( SELECT cro_num FROM e_tde WHERE cro_num = tde_num AND tde_valor = 'S' AND tde_cod = 'ENTREGAAÑO"+(años-1)+"'))";
              
           }
            
            if (años == 2 ){
               query += " AND (cro_num IN (SELECT cro_num from " + ConfigurationParameter.getParameter(ConstantesMeLanbide72.TABLA_OCURRENCIAS, ConstantesMeLanbide72.FICHERO_PROPIEDADES)
                       + " tramite where tramite.cro_num=cro_num and tramite.cro_tra = "+procedimientoAnterior+" and tramite.cro_fef IS NOT NULL)"
                       + " OR cro_num IN ( SELECT cro_num FROM e_tde WHERE cro_num = tde_num AND tde_valor = 'S' AND tde_cod = 'ENTREGAAÑO"+(años-1)+"'))"
                       + " AND cro_num NOT IN (SELECT cro_num from e_cro tramite2 where tramite2.cro_num=cro_num and tramite2.cro_tra = "+procedimientoPosterior3+" ) AND cro_num NOT IN (  "
                       + " SELECT cro_num FROM e_tde WHERE cro_num = tde_num AND tde_valor = 'S' AND tde_cod = 'ENTREGAAÑO"+(años+1)+"')";
           }
            
            
             if (años == 1 ){
               query += " AND cro_num NOT IN (SELECT cro_num from e_cro tramite2 where tramite2.cro_num=cro_num and tramite2.cro_tra = "+procedimientoPosterior3+" ) AND cro_num NOT IN ( " 
                       + " SELECT cro_num FROM e_tde WHERE cro_num = tde_num AND tde_valor = 'S' AND tde_cod = 'ENTREGAAÑO"+(años+2)+"')"
                       + " AND cro_num NOT IN (SELECT cro_num from e_cro tramite2 where tramite2.cro_num=cro_num and tramite2.cro_tra = "+procedimientoPosterior2+" ) AND cro_num NOT IN ( "
                       + " SELECT cro_num FROM e_tde WHERE cro_num = tde_num AND tde_valor = 'S' AND tde_cod = 'ENTREGAAÑO"+(años+1)+"')";
           }
            
           
            
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                listReg.add((RegistroBatchVO) MeLanbide72MappingUtils.getInstance().mapRegistroBatch(rs));
            }
        } catch (Exception ex) {
            log.error("Se ha producido un error en getListaRegistrosDECEX3Meses ", ex);
            throw new Exception(ex);
        } finally {
            log.debug(" END getListaRegistrosDECEX3Meses  ");
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
        return listReg;

    }
    

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.melanbide51.dao;

import es.altia.flexia.integracion.moduloexterno.melanbide51.vo.ControlAccesoVO;
import es.altia.flexia.integracion.moduloexterno.melanbide51.util.ConfigurationParameter;
import es.altia.flexia.integracion.moduloexterno.melanbide51.util.ConstantesMeLanbide51;
import es.altia.flexia.integracion.moduloexterno.melanbide51.util.MeLanbide51MappingUtils;
import es.altia.flexia.integracion.moduloexterno.melanbide51.vo.CriteriosBusquedaAccesoVO;
import es.altia.flexia.integracion.moduloexterno.melanbide51.vo.DesplegableAdmonLocalVO;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

/**
 *
 * @author davidg
 */
public class MeLanbide51DAO {
    
    //Logger
    private static Logger log = LogManager.getLogger(MeLanbide51DAO.class);
    //Instancia
    private static MeLanbide51DAO instance = null;
    // Constructor
    private MeLanbide51DAO()
    {   
        
    }
    
    public static MeLanbide51DAO getInstance()
    {
        if(instance == null)
        {
            synchronized(MeLanbide51DAO.class)
            {
                instance = new MeLanbide51DAO();
            }
        }
        return instance;
    }

    public List<ControlAccesoVO> getDatosControlAcceso(int codOrganizacion, Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        List<ControlAccesoVO> lista = new ArrayList<ControlAccesoVO>();
        ControlAccesoVO controlAcceso = new ControlAccesoVO();
        try {
            String query = null;
            query = "SELECT * FROM " + ConfigurationParameter.getParameter(ConstantesMeLanbide51.TABLA_MELANBIDE51_CONTROL_ACCESO, ConstantesMeLanbide51.FICHERO_PROPIEDADES)
                    + " WHERE M51CA_HORSAL IS NULL "
                    + " ORDER BY M51CA_FECHA DESC, M51CA_HORENT DESC";
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                controlAcceso = (ControlAccesoVO) MeLanbide51MappingUtils.getInstance().map(rs, ControlAccesoVO.class);
                //Cargamos el el request los valores  de los desplegables
                lista.add(controlAcceso);
                controlAcceso = new ControlAccesoVO();
            }
        } catch (Exception ex) {
            log.error("Se ha producido un error recuperando Controles Accesos ", ex);
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
            } catch (Exception e) {
                log.error("Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
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
            query = "SELECT * FROM " + ConfigurationParameter.getParameter(ConstantesMeLanbide51.TABLA_MELANBIDE51_CONTROL_ACCESO, ConstantesMeLanbide51.FICHERO_PROPIEDADES)
                    + " WHERE ID="+ (id!=null && !id.equals("") ? id : "null")
                    ;
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                controlAcceso = (ControlAccesoVO) MeLanbide51MappingUtils.getInstance().map(rs, ControlAccesoVO.class);
            }
        } catch (Exception ex) {
            log.error("Se ha producido un error recuperando un control de acceso : " + id, ex);
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
            } catch (Exception e) {
                log.error("Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }
        return controlAcceso;
    }

    public int eliminarAcceso(String id, Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        try {
            String query = null;
            query = "DELETE FROM " + ConfigurationParameter.getParameter(ConstantesMeLanbide51.TABLA_MELANBIDE51_CONTROL_ACCESO, ConstantesMeLanbide51.FICHERO_PROPIEDADES)
                    + " WHERE ID=" + (id!=null && !id.equals("") ? id : "null")
                    ;
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            return st.executeUpdate(query);
        } catch (Exception ex) {
            log.error("Se ha producido un error Eliminando Control de Acceso ID : " + id, ex);
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
            } catch (Exception e) {
                log.error("Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }
    }

    public boolean crearNuevoAcceso(ControlAccesoVO nuevoAcceso, Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        //boolean opeCorrecta = true;
        String query = "";
        String fecha = "";
        String fechaiv = "" ;
        String fechafv = ""; 
        if (nuevoAcceso != null && nuevoAcceso.getFecha() != null && !nuevoAcceso.getFecha().equals("")) {
            SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
            fecha = formatoFecha.format(nuevoAcceso.getFecha());
        }
        if(nuevoAcceso != null && nuevoAcceso.getFechaIV()!= null && !nuevoAcceso.getFechaIV().equals("")){
            SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
            fechaiv = formatoFecha.format(nuevoAcceso.getFechaIV());
            }
        if(nuevoAcceso != null && nuevoAcceso.getFechaFV()!= null && !nuevoAcceso.getFechaFV().equals("")){
            SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
            fechafv = formatoFecha.format(nuevoAcceso.getFechaFV());
            }
       
        
            
        try {
            
            int id = recogerIDInsertar(ConfigurationParameter.getParameter(ConstantesMeLanbide51.SEQ_MELANBIDE51_CONTROL_ACCESO, ConstantesMeLanbide51.FICHERO_PROPIEDADES), con);
            query = "INSERT INTO " + ConfigurationParameter.getParameter(ConstantesMeLanbide51.TABLA_MELANBIDE51_CONTROL_ACCESO, ConstantesMeLanbide51.FICHERO_PROPIEDADES)
                    + "(ID,M51CA_FECHA,M51CA_NUMTAR,M51CA_DNI_CIF,M51CA_NOMBRE,M51CA_APE1,M51CA_APE2,M51CA_TEL,"
                    + "M51CA_EMPENT,M51CA_SERVVIS,M51CA_PERSCONT,M51CA_MOTVIS,M51CA_FECHAIV,M51CA_FECHAFV,M51CA_HORENT,M51CA_HORSAL,M51CA_OBSER) "
                    + " VALUES (" + id 
                        + ", TO_DATE('" + fecha + "','dd/mm/yyyy')"  //+ (nuevoAcceso.getFecha() != null ? "TO_DATE('" + nuevoAcceso.getFecha() + "','dd/mm/yyyy')" : "null") 
                        + ",'" + nuevoAcceso.getNo_tarjeta() + "'"
                        + ",'" + nuevoAcceso.getNif_Dni() + "'"
                        + ",'" + nuevoAcceso.getNombre() + "'"
                        + ",'" + nuevoAcceso.getApellido1() + "'"
                        + ",'" + nuevoAcceso.getApellido2() + "'"
                        + ",'" + nuevoAcceso.getTelefono() + "'"
                        + ",'" + nuevoAcceso.getEmpresa_entidad() + "'"
                        + ",'" + nuevoAcceso.getServicio_visitado()  + "'"
                        + ",'" + nuevoAcceso.getPersona_contacto() + "'"
                        + ",'" + nuevoAcceso.getCod_mot_visita() + "'"
                        + ", TO_DATE('" + fechaiv + "','dd/mm/yyyy')"
                        + ", TO_DATE('" + fechafv + "','dd/mm/yyyy')"
                        + "," +(nuevoAcceso.getHora_entrada()!= null ? "TO_TIMESTAMP('"+fecha+"' || ' " + nuevoAcceso.getHora_entrada().toString() + "','dd/mm/yyyy HH24.MI.SS')" : "null") 
                        + "," +(nuevoAcceso.getHora_salida()!= null ? "TO_TIMESTAMP('"+fecha+"' || ' " + nuevoAcceso.getHora_salida().toString() + "','dd/mm/yyyy HH24.MI.SS')" : "null") 
                        + ",'" + nuevoAcceso.getObservaciones() + "'"
                    
                    + ")";
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            int insert = st.executeUpdate(query);
            if (insert > 0) {
                return true;
            } else {
                log.debug("No Se ha podido guardar un nuevo registro de Acceso ");
                return false;
            }

        } catch (Exception ex) {
            //opeCorrecta = false;
            log.debug("Se ha producido un error al Insertar un nuevo registro de Acceso" + ex.getMessage());
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
            } catch (Exception e) {
                log.error("Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }
    }
    
    public boolean modificarAcceso(ControlAccesoVO datModif, Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        String query = "";
        String fecha = "";
        String fechaiv = "";
        String fechafv = "";
        if (datModif != null && datModif.getFecha() != null && !datModif.getFecha().equals("")) {
            SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
            fecha = formatoFecha.format(datModif.getFecha());
        }
        if (datModif != null && datModif.getFechaIV() != null && !datModif.getFechaIV().equals("")) {
            SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
            fechaiv = formatoFecha.format(datModif.getFechaIV());
        }
        if (datModif != null && datModif.getFechaFV() != null && !datModif.getFechaFV().equals("")) {
            SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
            fechafv = formatoFecha.format(datModif.getFechaFV());
        }
        
        try {
            query = "UPDATE " + ConfigurationParameter.getParameter(ConstantesMeLanbide51.TABLA_MELANBIDE51_CONTROL_ACCESO, ConstantesMeLanbide51.FICHERO_PROPIEDADES)
                    + " SET M51CA_FECHA=TO_DATE('" + fecha + "','dd/mm/yyyy')" //+ (datModif.getFecha() != null ? "TO_DATE('" + datModif.getFecha() + "','dd/mm/yyyy')" : "null")
                    + ", M51CA_NUMTAR='" + datModif.getNo_tarjeta()+ "'"
                    + ", M51CA_DNI_CIF='" + datModif.getNif_Dni()+ "'"
                    + ", M51CA_NOMBRE='" + datModif.getNombre()+ "'"
                    + ", M51CA_APE1='" + datModif.getApellido1()+ "'"
                    + ", M51CA_APE2='" + datModif.getApellido2()+ "'"
                    + ", M51CA_TEL='" + datModif.getTelefono()+ "'"
                    + ", M51CA_EMPENT='" + datModif.getEmpresa_entidad()+ "'"
                    + ", M51CA_SERVVIS='" + datModif.getServicio_visitado()+ "'"
                    + ", M51CA_PERSCONT='" + datModif.getPersona_contacto()+ "'"
                    + ", M51CA_MOTVIS='" + datModif.getCod_mot_visita()+ "'"
                    + ",  M51CA_FECHAIV=TO_DATE('" + fechaiv + "','dd/mm/yyyy')"
                    + ",  M51CA_FECHAFV=TO_DATE('" + fechafv + "','dd/mm/yyyy')"
                    + ", M51CA_HORENT=" + (datModif.getHora_entrada() != null ? "TO_TIMESTAMP('" + fecha + "' || ' " + datModif.getHora_entrada().toString() + "','DD/MM/YYYY HH24.MI.SS')" : "null")
                    + ", M51CA_HORSAL=" + (datModif.getHora_salida()!= null ? "TO_TIMESTAMP('" + fecha + "' || ' " + datModif.getHora_salida().toString() + "','DD/MM/YYYY HH24.MI.SS')" : "null")
                    + ", M51CA_OBSER='" + datModif.getObservaciones()+ "'"
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
            log.debug("Se ha producido un error al Modificar un registro de Control de acceso - "
                    + datModif.getId() + " - " + ex);
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
            } catch (Exception e) {
                log.error("Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }
    }
    
    // Funcniones Privadas

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
            } catch (Exception e) {
                log.error("Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
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
            query = "SELECT * FROM " + ConfigurationParameter.getParameter(ConstantesMeLanbide51.TABLA_VALORES_DESPLEGABLES, ConstantesMeLanbide51.FICHERO_PROPIEDADES)
                    + " WHERE DES_COD='" + des_cod +"'";
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                valoresDesplegabe = (DesplegableAdmonLocalVO) MeLanbide51MappingUtils.getInstance().map(rs, DesplegableAdmonLocalVO.class);
                lista.add(valoresDesplegabe);
                valoresDesplegabe = new DesplegableAdmonLocalVO();
            }
        } catch (Exception ex) {
            log.error("Se ha producido un error recuperando un valores de un desplegable : " + des_cod, ex);
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
            } catch (Exception e) {
                log.error("Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }
        return lista;
    }

    public List<ControlAccesoVO> busquedaFiltrandoListaAcceso(CriteriosBusquedaAccesoVO _criteriosBusquedaAccesoVO, Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        List<ControlAccesoVO> lista = new ArrayList<ControlAccesoVO>();
        ControlAccesoVO controlAcceso = new ControlAccesoVO();
        try {
            String query = null;
            String where = preparaClausulaWhere(_criteriosBusquedaAccesoVO);        
            query = "SELECT * FROM " + ConfigurationParameter.getParameter(ConstantesMeLanbide51.TABLA_MELANBIDE51_CONTROL_ACCESO, ConstantesMeLanbide51.FICHERO_PROPIEDADES)
                    + " " + where
                    + " ORDER BY M51CA_FECHA DESC, M51CA_HORENT DESC"
                    ;
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                controlAcceso = (ControlAccesoVO) MeLanbide51MappingUtils.getInstance().map(rs, ControlAccesoVO.class);
                //Cargamos el el request los valores  de los desplegables
                lista.add(controlAcceso);
                controlAcceso = new ControlAccesoVO();
            }
        } catch (Exception ex) {
            log.error("Se ha producido un error recuperando Controles Accesos ", ex);
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
            } catch (Exception e) {
                log.error("Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }
        return lista;
    }

    private String preparaClausulaWhere(CriteriosBusquedaAccesoVO _criteriosBusquedaAccesoVO) throws Exception {
        String where = "";
        List<String> whereClause = new ArrayList<String>();
        whereClause.add("WHERE");
        try{
            if(_criteriosBusquedaAccesoVO.getDni_nif_busq()!=null && !_criteriosBusquedaAccesoVO.getDni_nif_busq().equals("")){
                if(where.startsWith("WHERE")){
                    where += " AND  (UPPER(M51CA_DNI_CIF) LIKE'%" + _criteriosBusquedaAccesoVO.getDni_nif_busq().toUpperCase()  + "%')";
                }else{
                    where += "WHERE (UPPER(M51CA_DNI_CIF) LIKE'%" + _criteriosBusquedaAccesoVO.getDni_nif_busq().toUpperCase()  + "%')";
                }
            }
            if(_criteriosBusquedaAccesoVO.getNombre_busq()!=null && !_criteriosBusquedaAccesoVO.getNombre_busq().equals("")){
                if(where.startsWith("WHERE")){
                    where += " AND  (UPPER(M51CA_NOMBRE) LIKE'%" + _criteriosBusquedaAccesoVO.getNombre_busq().toUpperCase()  + "%')";
                }else{
                    where += "WHERE (UPPER(M51CA_NOMBRE) LIKE'%" + _criteriosBusquedaAccesoVO.getNombre_busq().toUpperCase()  + "%')";
                }
            }
            if(_criteriosBusquedaAccesoVO.getApellido1_busq()!=null && !_criteriosBusquedaAccesoVO.getApellido1_busq().equals("")){
                if(where.startsWith("WHERE")){
                    where += " AND  (UPPER(M51CA_APE1) LIKE'%" + _criteriosBusquedaAccesoVO.getApellido1_busq().toUpperCase()  + "%')";
                }else{
                    where += "WHERE (UPPER(M51CA_APE1) LIKE'%" + _criteriosBusquedaAccesoVO.getApellido1_busq().toUpperCase()  + "%')";
                }
            }
            if(_criteriosBusquedaAccesoVO.getApellido2_busq()!=null && !_criteriosBusquedaAccesoVO.getApellido2_busq().equals("")){
                if(where.startsWith("WHERE")){
                    where += " AND  (UPPER(M51CA_APE2) LIKE'%" + _criteriosBusquedaAccesoVO.getApellido2_busq().toUpperCase()  + "%')";
                }else{
                    where += "WHERE (UPPER(M51CA_APE2) LIKE'%" + _criteriosBusquedaAccesoVO.getApellido2_busq().toUpperCase()  + "%')";
                }
            }
            if(_criteriosBusquedaAccesoVO.getEmpresa_entidad_busq()!=null && !_criteriosBusquedaAccesoVO.getEmpresa_entidad_busq().equals("")){
                if(where.startsWith("WHERE")){
                    where += " AND  (UPPER(M51CA_EMPENT) LIKE'%" + _criteriosBusquedaAccesoVO.getEmpresa_entidad_busq().toUpperCase()  + "%')";
                }else{
                    where += "WHERE (UPPER(M51CA_EMPENT) LIKE'%" + _criteriosBusquedaAccesoVO.getEmpresa_entidad_busq().toUpperCase()  + "%')";
                }
            }
            if(_criteriosBusquedaAccesoVO.getCodListaMotivoVisita_busq()!=null && !_criteriosBusquedaAccesoVO.getCodListaMotivoVisita_busq().equals("")){
                if(where.startsWith("WHERE")){
                    where += " AND  (M51CA_MOTVIS='" + _criteriosBusquedaAccesoVO.getCodListaMotivoVisita_busq()  + "')";
                }else{
                    where += "WHERE (M51CA_MOTVIS='" + _criteriosBusquedaAccesoVO.getCodListaMotivoVisita_busq()  + "')";
                }
            }
            if(_criteriosBusquedaAccesoVO.getEstado()!=null && !_criteriosBusquedaAccesoVO.getEstado().equals("")){
                if(_criteriosBusquedaAccesoVO.getEstado().equals("D")){
                    if (where.startsWith("WHERE")) {
                        where += " AND  (M51CA_HORENT IS NOT NULL AND M51CA_HORSAL IS NULL)";
                    } else {
                        where += "WHERE (M51CA_HORENT IS NOT NULL AND M51CA_HORSAL IS NULL)";
                    }
                }else if(_criteriosBusquedaAccesoVO.getEstado().equals("F")){
                    if (where.startsWith("WHERE")) {
                        where += " AND  (M51CA_HORENT IS NOT NULL AND M51CA_HORSAL IS NOT NULL)";
                    } else {
                        where += "WHERE (M51CA_HORENT IS NOT NULL AND M51CA_HORSAL IS NOT NULL)";
                    }
                }
            }
            if((_criteriosBusquedaAccesoVO.getMeLanbide51Fecha_busqE()!=null && !_criteriosBusquedaAccesoVO.getMeLanbide51Fecha_busqE().equals("")) 
                    && (_criteriosBusquedaAccesoVO.getMeLanbide51Fecha_busqS()!=null && !_criteriosBusquedaAccesoVO.getMeLanbide51Fecha_busqS().equals(""))){
                if(where.startsWith("WHERE")){
                    where += " AND  (M51CA_FECHA BETWEEN TO_DATE('"+ _criteriosBusquedaAccesoVO.getMeLanbide51Fecha_busqE() +"','dd/mm/yyyy') AND TO_DATE('"+ _criteriosBusquedaAccesoVO.getMeLanbide51Fecha_busqS() +"','dd/mm/yyyy'))";
                }else{
                    where += "WHERE (M51CA_FECHA BETWEEN TO_DATE('"+ _criteriosBusquedaAccesoVO.getMeLanbide51Fecha_busqE() +"','dd/mm/yyyy') AND TO_DATE('"+ _criteriosBusquedaAccesoVO.getMeLanbide51Fecha_busqS() +"','dd/mm/yyyy'))";
                }
            }else if(_criteriosBusquedaAccesoVO.getMeLanbide51Fecha_busqE()!=null && !_criteriosBusquedaAccesoVO.getMeLanbide51Fecha_busqE().equals("")){
                if(where.startsWith("WHERE")){
                    where += " AND  (TO_CHAR(M51CA_FECHA,'dd/mm/yyyy')='"+ _criteriosBusquedaAccesoVO.getMeLanbide51Fecha_busqE() +"')";
                }else{
                    where += "WHERE (TO_CHAR(M51CA_FECHA,'dd/mm/yyyy')='"+ _criteriosBusquedaAccesoVO.getMeLanbide51Fecha_busqE() +"')";
                }
            }else if(_criteriosBusquedaAccesoVO.getMeLanbide51Fecha_busqS()!=null && !_criteriosBusquedaAccesoVO.getMeLanbide51Fecha_busqS().equals("")){
                if(where.startsWith("WHERE")){
                    where += " AND  (TO_CHAR(M51CA_FECHA,'dd/mm/yyyy')='"+ _criteriosBusquedaAccesoVO.getMeLanbide51Fecha_busqS()+"')";
                }else{
                    where += "WHERE (TO_CHAR(M51CA_FECHA,'dd/mm/yyyy')='"+ _criteriosBusquedaAccesoVO.getMeLanbide51Fecha_busqS()+"')";
                }
            }
            // prepara criteri para intervalos de horas
            if((_criteriosBusquedaAccesoVO.getHora_entrada_busq()!=null && !_criteriosBusquedaAccesoVO.getHora_entrada_busq().equals("")) 
                    && (_criteriosBusquedaAccesoVO.getHora_entrada_busqF()!=null && !_criteriosBusquedaAccesoVO.getHora_entrada_busqF().equals(""))){
                if(where.startsWith("WHERE")){
                    where += " AND  (TO_CHAR(M51CA_HORENT,'HH24:MI:SS') BETWEEN '" + _criteriosBusquedaAccesoVO.getHora_entrada_busq() +"' AND '" + _criteriosBusquedaAccesoVO.getHora_entrada_busqF()+"')";
                }else{
                    where += "WHERE (TO_CHAR(M51CA_HORENT,'HH24:MI:SS') BETWEEN '" + _criteriosBusquedaAccesoVO.getHora_entrada_busq() +"' AND '" + _criteriosBusquedaAccesoVO.getHora_entrada_busqF()+"')";
                }
            }else if(_criteriosBusquedaAccesoVO.getHora_entrada_busq()!=null && !_criteriosBusquedaAccesoVO.getHora_entrada_busq().equals("")){
                if(where.startsWith("WHERE")){
                    where += " AND  (TO_CHAR(M51CA_HORENT,'HH24:MI:SS') >= '" + _criteriosBusquedaAccesoVO.getHora_entrada_busq() +"')";
                }else{
                    where += "WHERE (TO_CHAR(M51CA_HORENT,'HH24:MI:SS') >= '" + _criteriosBusquedaAccesoVO.getHora_entrada_busq() +"')";
                }
            }else if(_criteriosBusquedaAccesoVO.getHora_entrada_busqF()!=null && !_criteriosBusquedaAccesoVO.getHora_entrada_busqF().equals("")){
                if(where.startsWith("WHERE")){
                    where += " AND  (TO_CHAR(M51CA_HORENT,'HH24:MI:SS') <= '" + _criteriosBusquedaAccesoVO.getHora_entrada_busqF() +"')";
                }else{
                    where += "WHERE (TO_CHAR(M51CA_HORENT,'HH24:MI:SS') <= '" + _criteriosBusquedaAccesoVO.getHora_entrada_busqF() +"')";
                }
            }
            if ((_criteriosBusquedaAccesoVO.getHora_salida_busq()!= null && !_criteriosBusquedaAccesoVO.getHora_salida_busq().equals(""))
                    && (_criteriosBusquedaAccesoVO.getHora_salida_busqF() != null && !_criteriosBusquedaAccesoVO.getHora_salida_busqF().equals(""))) {
                if (where.startsWith("WHERE")) {
                    where += " AND  (TO_CHAR(M51CA_HORSAL,'HH24:MI:SS') BETWEEN '" + _criteriosBusquedaAccesoVO.getHora_salida_busq() + "' AND '" + _criteriosBusquedaAccesoVO.getHora_salida_busqF() + "')";
                } else {
                    where += "WHERE (TO_CHAR(M51CA_HORSAL,'HH24:MI:SS') BETWEEN '" + _criteriosBusquedaAccesoVO.getHora_salida_busq() + "' AND '" + _criteriosBusquedaAccesoVO.getHora_salida_busqF() + "')";
                }
            } else if (_criteriosBusquedaAccesoVO.getHora_salida_busq() != null && !_criteriosBusquedaAccesoVO.getHora_salida_busq().equals("")) {
                if (where.startsWith("WHERE")) {
                    where += " AND  (TO_CHAR(M51CA_HORSAL,'HH24:MI:SS') >= '" + _criteriosBusquedaAccesoVO.getHora_salida_busq() + "')";
                } else {
                    where += "WHERE (TO_CHAR(M51CA_HORSAL,'HH24:MI:SS') >= '" + _criteriosBusquedaAccesoVO.getHora_salida_busq() + "')";
                }
            } else if (_criteriosBusquedaAccesoVO.getHora_salida_busqF() != null && !_criteriosBusquedaAccesoVO.getHora_salida_busqF().equals("")) {
                if (where.startsWith("WHERE")) {
                    where += " AND  (TO_CHAR(M51CA_HORSAL,'HH24:MI:SS') <= '" + _criteriosBusquedaAccesoVO.getHora_salida_busqF() + "')";
                } else {
                    where += "WHERE (TO_CHAR(M51CA_HORSAL,'HH24:MI:SS') <= '" + _criteriosBusquedaAccesoVO.getHora_salida_busqF() + "')";
                }
            }
        }catch(Exception ex){
            log.error("Se ha producido un error preparando la clausula where de ka sentencia de consulta con filtros", ex);
            throw new Exception(ex);
        }
        return where;
    }
    
    public ControlAccesoVO getControlAccesoPorDNICIF(String cif_dni, Connection con) throws Exception {
        Statement st = null;
        ResultSet rs = null;
        ControlAccesoVO controlAcceso = new ControlAccesoVO();
        try {
            String query = null;
            query = "select * " +
                    " FROM " + ConfigurationParameter.getParameter(ConstantesMeLanbide51.TABLA_MELANBIDE51_CONTROL_ACCESO, ConstantesMeLanbide51.FICHERO_PROPIEDADES) +
                    " WHERE M51CA_DNI_CIF='" + cif_dni + "'" +
                    " ORDER BY M51CA_HORENT DESC "
                    ;
            if (log.isDebugEnabled()) {
                log.debug("sql = " + query);
            }
            st = con.createStatement();
            rs = st.executeQuery(query);
            while (rs.next()) {
                controlAcceso = (ControlAccesoVO) MeLanbide51MappingUtils.getInstance().map(rs, ControlAccesoVO.class);
                break;
            }
        } catch (Exception ex) {
            log.error("Se ha producido un error recuperando un control de acceso : " + cif_dni, ex);
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
            } catch (Exception e) {
                log.error("Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }
        return controlAcceso;
    }

    public String actualizaVistasMaterializadas(String vistas, Connection con) throws Exception
    {
        CallableStatement st = null;
        ResultSet rs = null;
        try
        {
            String query = "call MVIEW_REFRESH(?,?)";
            if(log.isDebugEnabled()) 
                log.debug("sql = " + query);
            st = con.prepareCall(query);
            st.setString(1, vistas);
            st.registerOutParameter(2, java.sql.Types.VARCHAR);
            st.executeUpdate();
            return st.getString(2);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error actualizando las vistas materializadas ", ex);
            throw new Exception(ex);
        }
        finally
        {
            try
            {
                if(log.isDebugEnabled()) 
                    log.debug("Procedemos a cerrar el statement y el resultset");
                if(st!=null) 
                    st.close();
                if(rs!=null) 
                    rs.close();
            }
            catch(Exception e)
            {
                log.error("Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }
    }
       
}

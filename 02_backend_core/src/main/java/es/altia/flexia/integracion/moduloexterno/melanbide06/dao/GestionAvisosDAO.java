/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.melanbide06.dao;

import es.altia.flexia.integracion.moduloexterno.melanbide06.util.MeLanbide06ConfigurationParameter;
import es.altia.flexia.integracion.moduloexterno.melanbide06.util.MeLanbide06Constantes;
import es.altia.flexia.integracion.moduloexterno.melanbide06.util.MeLanbide06MappingUtils;
import es.altia.flexia.integracion.moduloexterno.melanbide06.vo.GestionAvisosVO;
import es.altia.flexia.integracion.moduloexterno.melanbide06.vo.Melanbide06GA;
import es.altia.flexia.integracion.moduloexterno.melanbide06.vo.SelectItem;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

/**
 *
 * @author jaime.hermoso
 */
public class GestionAvisosDAO {
    
    //Logger
    private static Logger log = LogManager.getLogger(GestionAvisosDAO.class);
    
    //Instance 
    private static GestionAvisosDAO instance = null;
    
    /**
     * Devuelve una unica instancia (Singleton) de la clase GestionAvisosDAO
     * 
     * @return GestionAvisosDAO
     */
    public static GestionAvisosDAO getInstance(){
        if(log.isDebugEnabled()) log.info("getInstance() : BEGIN");
        if(instance == null){
            synchronized(GestionAvisosDAO.class){
                if(instance == null){
                    if(log.isDebugEnabled()) log.info("Se crea una instancia de la clase");
                    instance = new GestionAvisosDAO();
                }//if(instance == null)
            }//synchronized(MeLanbide06Dao.class)
        }//if(instance == null)
        if(log.isDebugEnabled()) log.info("getInstance() : END");
        return instance;
    }//getInstance
   
    
    public List<SelectItem> getListaProcedimientos(Connection con) throws Exception{
	Statement st = null;
        ResultSet rs = null;
        List<SelectItem> listaProcedimientos = new ArrayList<SelectItem>();
	    String query = null;
		try {
			query ="SELECT pro_cod CODIGO, pro_des DESCRIPCION FROM " + MeLanbide06ConfigurationParameter.getParameter(MeLanbide06Constantes.TABLA_PROCEDIMIENTOS, MeLanbide06Constantes.FICHERO_CONFIGURACION)
				+ " WHERE pro_est=1 AND pro_fba IS NULL"
				+ " ORDER BY pro_cod";
			
			log.info("Sql: " + query);
			st = con.createStatement();
            rs = st.executeQuery(query);
			while(rs.next()) {
				listaProcedimientos.add((SelectItem)MeLanbide06MappingUtils.getInstance().map(rs, SelectItem.class));
			}
		}  catch(Exception ex) {
            log.error("Se ha producido un error recuperando procedimientos.", ex);
            throw new Exception(ex);
        }
        finally {
            try {
                if(st!=null) {
                    st.close();
				}
                if(rs!=null) {
                    rs.close();
				}
            }
            catch(Exception e) {
                log.error("Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }
        return listaProcedimientos;
	}
    
    
    public List<SelectItem> getListaUors(Connection con) throws Exception{
	Statement st = null;
        ResultSet rs = null;
        List<SelectItem> listaUors = new ArrayList<SelectItem>();
	    String query = null;
		try {
			query ="SELECT UOR_COD CODIGO, UOR_NOM DESCRIPCION FROM " + MeLanbide06ConfigurationParameter.getParameter(MeLanbide06Constantes.TABLA_UORS, MeLanbide06Constantes.FICHERO_CONFIGURACION)
				+ " ORDER BY UOR_COD";
			
			log.info("Sql: " + query);
			st = con.createStatement();
            rs = st.executeQuery(query);
			while(rs.next()) {
				listaUors.add((SelectItem)MeLanbide06MappingUtils.getInstance().map(rs, SelectItem.class));
			}
		}  catch(Exception ex) {
            log.error("Se ha producido un error recuperando uors.", ex);
            throw new Exception(ex);
        }
        finally {
            try {
                if(st!=null) {
                    st.close();
				}
                if(rs!=null) {
                    rs.close();
				}
            }
            catch(Exception e) {
                log.error("Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }
        return listaUors;
	}
       
    public List<SelectItem> getListaAsuntos(Connection con) throws Exception{
	Statement st = null;
        ResultSet rs = null;
        List<SelectItem> listaAsuntos = new ArrayList<SelectItem>();
	    String query = null;
		try {
			query ="SELECT CODIGO, DESCRIPCION, PROCEDIMIENTO FROM " + MeLanbide06ConfigurationParameter.getParameter(MeLanbide06Constantes.TABLA_ASUNTOS, MeLanbide06Constantes.FICHERO_CONFIGURACION)
				+ " ORDER BY CODIGO";
	
			log.info("Sql: " + query);
			st = con.createStatement();
            rs = st.executeQuery(query);
			while(rs.next()) {
				listaAsuntos.add((SelectItem)MeLanbide06MappingUtils.getInstance().map(rs, SelectItem.class));
			}
		}  catch(Exception ex) {
            log.error("Se ha producido un error recuperando asuntos.", ex);
            throw new Exception(ex);
        }
        finally {
            try {
                if(st!=null) {
                    st.close();
				}
                if(rs!=null) {
                    rs.close();
				}
            }
            catch(Exception e) {
                log.error("Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }
        return listaAsuntos;
	}
     
    public List<SelectItem> getListaEventos(Connection con) throws Exception{
	Statement st = null;
        ResultSet rs = null;
        List<SelectItem> listaEventos = new ArrayList<SelectItem>();
	    String query = null;
		try {
			query ="SELECT COD_EVENTO CODIGO, NOMBRE_EVENTO DESCRIPCION, EXTRACTO FROM " + MeLanbide06ConfigurationParameter.getParameter(MeLanbide06Constantes.TABLA_MELANBIDE06_EVENTO_MAIL, MeLanbide06Constantes.FICHERO_CONFIGURACION);
	
			log.info("Sql: " + query);
			st = con.createStatement();
            rs = st.executeQuery(query);
			while(rs.next()) {
				listaEventos.add((SelectItem)MeLanbide06MappingUtils.getInstance().map(rs, SelectItem.class));
			}
		}  catch(Exception ex) {
            log.error("Se ha producido un error recuperando eventos.", ex);
            throw new Exception(ex);
        }
        finally {
            try {
                if(st!=null) {
                    st.close();
				}
                if(rs!=null) {
                    rs.close();
				}
            }
            catch(Exception e) {
                log.error("Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }
        return listaEventos;
	}        
    
    public List<SelectItem> getListaOrganizaciones(Connection con) throws Exception{
	Statement st = null;
        ResultSet rs = null;
        List<SelectItem> listaOrganizaciones = new ArrayList<SelectItem>();
	    String query = null;
		try {
                        String tablaOrganizacion = MeLanbide06ConfigurationParameter.getParameter(MeLanbide06Constantes.ESQUEMA_GENERICO, MeLanbide06Constantes.FICHERO_CONFIGURACION)+MeLanbide06ConfigurationParameter.getParameter(MeLanbide06Constantes.TABLA_ORGANIZACIONES, MeLanbide06Constantes.FICHERO_CONFIGURACION);
			query ="SELECT ORG_COD CODIGO, ORG_DES DESCRIPCION FROM " + tablaOrganizacion
				+ " ORDER BY ORG_COD";

			log.info("Sql: " + query);
			st = con.createStatement();
            rs = st.executeQuery(query);
			while(rs.next()) {
				listaOrganizaciones.add((SelectItem)MeLanbide06MappingUtils.getInstance().map(rs, SelectItem.class));
			}
		}  catch(Exception ex) {
            log.error("Se ha producido un error recuperando organizaciones.", ex);
            throw new Exception(ex);
        }
        finally {
            try {
                if(st!=null) {
                    st.close();
				}
                if(rs!=null) {
                    rs.close();
				}
            }
            catch(Exception e) {
                log.error("Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }
        return listaOrganizaciones;
	}
    
    public List<SelectItem> getListaEntidades(Connection con) throws Exception{
	Statement st = null;
        ResultSet rs = null;
        List<SelectItem> listaEntidades = new ArrayList<SelectItem>();
	    String query = null;
		try {
                        String tablaEntidad = MeLanbide06ConfigurationParameter.getParameter(MeLanbide06Constantes.ESQUEMA_GENERICO, MeLanbide06Constantes.FICHERO_CONFIGURACION)+MeLanbide06ConfigurationParameter.getParameter(MeLanbide06Constantes.TABLA_ENTIDADES, MeLanbide06Constantes.FICHERO_CONFIGURACION);
			query ="SELECT ENT_COD CODIGO, ENT_NOM DESCRIPCION FROM " + tablaEntidad
				+ " ORDER BY ENT_COD";
			
			log.info("Sql: " + query);
			st = con.createStatement();
            rs = st.executeQuery(query);
			while(rs.next()) {
				listaEntidades.add((SelectItem)MeLanbide06MappingUtils.getInstance().map(rs, SelectItem.class));
			}
		}  catch(Exception ex) {
            log.error("Se ha producido un error recuperando entidades.", ex);
            throw new Exception(ex);
        }
        finally {
            try {
                if(st!=null) {
                    st.close();
				}
                if(rs!=null) {
                    rs.close();
				}
            }
            catch(Exception e) {
                log.error("Se ha producido un error cerrando el statement y el resulset", e);
                throw new Exception(e);
            }
        }
        return listaEntidades;
	}
    
        public SelectItem getRescatarProcedimientoByCodAsunto(String codAsunto, Connection con) throws SQLException, Exception{
        //log.info(" getRescatarProcedimientoByCodAsunto - Begin "+ codAsunto + formatFechaLog.format(new Date()));
        String resultado = "";
        String query="";
        PreparedStatement ps = null;
        ResultSet rs = null;
        SelectItem procedimiento = new SelectItem();
        try{
            query ="SELECT PROCEDIMIENTO FROM " + MeLanbide06ConfigurationParameter.getParameter(MeLanbide06Constantes.TABLA_ASUNTOS, MeLanbide06Constantes.FICHERO_CONFIGURACION)
				+ " WHERE CODIGO =?";
            log.info("sql = " + query);
            ps = con.prepareStatement(query);
            ps.setString(1, codAsunto);            
            log.info("params = " + codAsunto);
            rs=ps.executeQuery();
            while (rs.next()) {                
                procedimiento.setCodigo(rs.getString("PROCEDIMIENTO"));
            }
        }catch (SQLException e) {
            log.error("Se ha producido recuperando codPro ", e);            
            throw e;
        }catch (Exception e) {
            log.error("Se ha producido recuperando codPro ", e);            
            throw e;
        }finally{
            if(log.isDebugEnabled()) log.info("Procedemos a cerrar el resultset");
            if(ps!=null) ps.close();
            if(rs!=null) rs.close();
        }
        //log.info(" getRescatarProcedimientoByCodAsunto - End "+ codAsunto + formatFechaLog.format(new Date()));
        return procedimiento;
    }    
    
        public List<SelectItem> getRescatarUorByCodProcedimiento(String codPro, Connection con) throws SQLException, Exception{
        //log.info(" getRescatarProcedimientoByCodAsunto - Begin "+ codAsunto + formatFechaLog.format(new Date()));
        String resultado = "";
        String query="";
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<SelectItem> unidadesOrganicas = new ArrayList<SelectItem>();
        SelectItem unidadOrganica = new SelectItem();
        try{
            query ="SELECT UOR.UOR_COD CODIGO, UOR.UOR_NOM DESCRIPCION FROM " + MeLanbide06ConfigurationParameter.getParameter(MeLanbide06Constantes.TABLA_UORS, MeLanbide06Constantes.FICHERO_CONFIGURACION)
				+ " UOR INNER JOIN "+MeLanbide06ConfigurationParameter.getParameter(MeLanbide06Constantes.TABLA_PUI, MeLanbide06Constantes.FICHERO_CONFIGURACION)+" PUI ON PUI.PUI_COD = UOR.UOR_COD WHERE PUI.PUI_PRO =?";
            log.info("sql = " + query);
            ps = con.prepareStatement(query);
            ps.setString(1, codPro);            
            log.info("params = " + codPro);
            rs=ps.executeQuery();
            while (rs.next()) {
                unidadOrganica = new SelectItem();
                unidadOrganica.setCodigo(rs.getString("CODIGO"));
                unidadOrganica.setDescripcion(rs.getString("DESCRIPCION"));
                unidadesOrganicas.add(unidadOrganica);    
            }
        }catch (SQLException e) {
            log.error("Se ha producido recuperando codPro ", e);            
            throw e;
        }catch (Exception e) {
            log.error("Se ha producido recuperando codPro ", e);            
            throw e;
        }finally{
            if(log.isDebugEnabled()) log.info("Procedemos a cerrar el resultset");
            if(ps!=null) ps.close();
            if(rs!=null) rs.close();
        }
        //log.info(" getRescatarUorByCodProcedimiento - End "+ codAsunto + formatFechaLog.format(new Date()));
        return unidadesOrganicas;
    }   
        
    public List<GestionAvisosVO> getDatosGA(Connection con) throws SQLException, Exception{
        //log.info(" getDatosGA - Begin "+ codAsunto + formatFechaLog.format(new Date()));
        String resultado = "";
        String query="";
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<GestionAvisosVO> gestionesAvisosVO = new ArrayList<GestionAvisosVO>();
        GestionAvisosVO gestionAvisosVO = new GestionAvisosVO();
        try{
            query ="SELECT GA.ID, GA.ASUNTO, GA.CONTENIDO, GA.EMAIL, GA.PROCEDIMIENTO, GA.CONFIGURACION_ACTIVA, GA.UOR, GA.ASUNTO_REGISTRO, GA.EVENTO_COD, EM.NOMBRE_EVENTO, UOR.UOR_NOM, PRO.PRO_DES, ASU.DESCRIPCION FROM " + MeLanbide06ConfigurationParameter.getParameter(MeLanbide06Constantes.TABLA_MELANBIDE06_CONFIG_MAIL, MeLanbide06Constantes.FICHERO_CONFIGURACION)
                    + " GA INNER JOIN "+MeLanbide06ConfigurationParameter.getParameter(MeLanbide06Constantes.TABLA_MELANBIDE06_EVENTO_MAIL, MeLanbide06Constantes.FICHERO_CONFIGURACION)+" EM ON EM.COD_EVENTO = GA.EVENTO_COD"
                    + " INNER JOIN "+MeLanbide06ConfigurationParameter.getParameter(MeLanbide06Constantes.TABLA_UORS, MeLanbide06Constantes.FICHERO_CONFIGURACION)+" UOR ON UOR.UOR_COD = GA.UOR"
                    + " INNER JOIN "+MeLanbide06ConfigurationParameter.getParameter(MeLanbide06Constantes.TABLA_PROCEDIMIENTOS, MeLanbide06Constantes.FICHERO_CONFIGURACION)+" PRO ON PRO.PRO_COD = GA.PROCEDIMIENTO"
                    + " INNER JOIN "+MeLanbide06ConfigurationParameter.getParameter(MeLanbide06Constantes.TABLA_ASUNTOS, MeLanbide06Constantes.FICHERO_CONFIGURACION)+" ASU ON ASU.CODIGO = GA.ASUNTO_REGISTRO";
            log.info("sql = " + query);
            ps = con.prepareStatement(query);
            rs=ps.executeQuery();
            while (rs.next()) {
                gestionAvisosVO = new GestionAvisosVO();
                gestionAvisosVO.setId(rs.getInt("ID"));
                gestionAvisosVO.setAsunto(rs.getString("ASUNTO"));
                gestionAvisosVO.setContenido(rs.getString("CONTENIDO"));
                gestionAvisosVO.setEmail(rs.getString("EMAIL"));
                gestionAvisosVO.setCodProcedimiento(rs.getString("PROCEDIMIENTO"));
                gestionAvisosVO.setConfiguracion_activa(rs.getString("CONFIGURACION_ACTIVA"));
                gestionAvisosVO.setUor(rs.getString("UOR"));  
                gestionAvisosVO.setEvento(rs.getString("NOMBRE_EVENTO"));       
                gestionAvisosVO.setCodAsuntoRegistro(rs.getString("ASUNTO_REGISTRO"));   
                gestionAvisosVO.setAsuntoRegistro(rs.getString("DESCRIPCION"));
                gestionAvisosVO.setProcedimiento(rs.getString("PRO_DES")); 
                gestionAvisosVO.setCodUor(rs.getString("UOR"));  
                gestionAvisosVO.setUor(rs.getString("UOR_NOM"));  
                gestionAvisosVO.setCodEvento(rs.getString("EVENTO_COD"));
                gestionesAvisosVO.add(gestionAvisosVO);    
            }
        }catch (SQLException e) {
            log.error("Se ha producido recuperando los datos de GA ", e);            
            throw e;
        }catch (Exception e) {
            log.error("Se ha producido recuperando datos de GA ", e);            
            throw e;
        }finally{
            if(log.isDebugEnabled()) log.info("Procedemos a cerrar el resultset");
            if(ps!=null) ps.close();
            if(rs!=null) rs.close();
        }
        //log.info(" getDatosGA - End "+ codAsunto + formatFechaLog.format(new Date()));
        return gestionesAvisosVO;
    }   
        
        
      public void getAnadirGA(Melanbide06GA datosGA, Connection con) throws SQLException, Exception{
        //log.info(" getRescatarProcedimientoByCodAsunto - Begin "+ codAsunto + formatFechaLog.format(new Date()));
        String resultado = "";
        String query="";
        PreparedStatement ps = null;
        ResultSet rs = null;
        try{
            int i = 1;
            int id = getNextId(MeLanbide06ConfigurationParameter.getParameter(MeLanbide06Constantes.SEQ_MELANBIDE06_CONFIG_MAIL, MeLanbide06Constantes.FICHERO_CONFIGURACION),con);
            query ="INSERT INTO "+MeLanbide06ConfigurationParameter.getParameter(MeLanbide06Constantes.TABLA_MELANBIDE06_CONFIG_MAIL, MeLanbide06Constantes.FICHERO_CONFIGURACION)+
            " (ID, ASUNTO,CONTENIDO,EMAIL,PROCEDIMIENTO,EVENTO_COD,UOR,ASUNTO_REGISTRO,CONFIGURACION_ACTIVA) VALUES(?,?,?,?,?,?,?,?,?)";
            log.info("sql = " + query); 
            ps = con.prepareStatement(query);
            ps.setInt(i++, id);  
            ps.setString(i++, datosGA.getDesAsunto());     
            ps.setString(i++, datosGA.getDesContenido());
            ps.setString(i++, datosGA.getDesEmail().replace(" ",""));
            ps.setString(i++, datosGA.getCodProcedimiento());
            ps.setInt(i++, Integer.parseInt(datosGA.getCodEvento()));
            ps.setString(i++, datosGA.getCodUOR());
            ps.setString(i++, datosGA.getCodAsunto());
            if(datosGA.getCheckConfActiva() == null){
               ps.setInt(i++, 0); 
            }else{
                if(datosGA.getCheckConfActiva().equals("N")){
                   ps.setInt(i++, 0); 
                }else{
                   ps.setInt(i++, 1);  
                }
            }
            ps.executeUpdate();

        }catch (SQLException e) {
            log.error("Se ha producido al anadir una GA ", e);            
            throw e;
        }catch (Exception e) {
            log.error("Se ha producido al anadir una GA ", e);            
            throw e;
        }finally{
            if(log.isDebugEnabled()) log.info("Procedemos a cerrar el resultset");
            if(ps!=null) ps.close();
            if(rs!=null) rs.close();
        }
        //log.info(" getAnadirGA - End "+ codAsunto + formatFechaLog.format(new Date()));
    } 
      
    public void getEliminarGA(String id, Connection con) throws SQLException, Exception{
        //log.info(" getEliminarGA - Begin "+ codAsunto + formatFechaLog.format(new Date()));
        String resultado = "";
        String query="";
        PreparedStatement ps = null;
        ResultSet rs = null;
        try{
            int i = 1;
            query ="DELETE FROM "+MeLanbide06ConfigurationParameter.getParameter(MeLanbide06Constantes.TABLA_MELANBIDE06_CONFIG_MAIL, MeLanbide06Constantes.FICHERO_CONFIGURACION)+
            " WHERE ID = ?";
            log.info("sql = " + query); 
            ps = con.prepareStatement(query);
            ps.setInt(i++, Integer.parseInt(id));
            
            ps.executeUpdate();

        }catch (SQLException e) {
            log.error("Se ha producido al eliminar una GA ", e);            
            throw e;
        }catch (Exception e) {
            log.error("Se ha producido al eliminar una GA ", e);            
            throw e;
        }finally{
            if(log.isDebugEnabled()) log.info("Procedemos a cerrar el resultset");
            if(ps!=null) ps.close();
            if(rs!=null) rs.close();
        }
        //log.info(" getEliminarGA - End "+ codAsunto + formatFechaLog.format(new Date()));
    }   
      
    public void getModificarGA(Melanbide06GA datosGA, String id, Connection con) throws SQLException, Exception{
        //log.info(" getModificarGA - Begin "+ codAsunto + formatFechaLog.format(new Date()));
        String resultado = "";
        String query="";
        PreparedStatement ps = null;
        ResultSet rs = null;
        try{
            int i = 1;
            query ="UPDATE "+MeLanbide06ConfigurationParameter.getParameter(MeLanbide06Constantes.TABLA_MELANBIDE06_CONFIG_MAIL, MeLanbide06Constantes.FICHERO_CONFIGURACION)+
            " SET ASUNTO = ?,CONTENIDO = ?,EMAIL = ?,PROCEDIMIENTO = ?,EVENTO_COD = ?,UOR = ?,ASUNTO_REGISTRO = ?,CONFIGURACION_ACTIVA = ? WHERE ID = ?";
            log.info("sql = " + query); 
            ps = con.prepareStatement(query);
            ps.setString(i++, datosGA.getDesAsunto());     
            ps.setString(i++, datosGA.getDesContenido());
            ps.setString(i++, datosGA.getDesEmail().replace(" ",""));
            ps.setString(i++, datosGA.getCodProcedimiento());
            ps.setInt(i++, Integer.parseInt(datosGA.getCodEvento()));
            ps.setString(i++, datosGA.getCodUOR());
            ps.setString(i++, datosGA.getCodAsunto());
            if(datosGA.getCheckConfActiva() == null){
               ps.setInt(i++, 0); 
            }else{
                if(datosGA.getCheckConfActiva().equals("N")){
                   ps.setInt(i++, 0); 
                }else{
                   ps.setInt(i++, 1);  
                }
            }
            ps.setInt(i++, Integer.parseInt(id));
            ps.executeUpdate();

        }catch (SQLException e) {
            log.error("Se ha producido al modificar una GA ", e);            
            throw e;
        }catch (Exception e) {
            log.error("Se ha producido al modificar una GA ", e);            
            throw e;
        }finally{
            if(log.isDebugEnabled()) log.info("Procedemos a cerrar el resultset");
            if(ps!=null) ps.close();
            if(rs!=null) rs.close();
        }
        //log.info(" getAnadirGA - End "+ codAsunto + formatFechaLog.format(new Date()));
    }
    
        private Integer getNextId(String seqName, Connection con) throws Exception {

        Statement st = null;
        ResultSet rs = null;
        Integer numSec = null;
        try {
            String query = null;
            //Creo el id con la secuencia
            query = "select " + seqName + ".nextval from dual";
            if (log.isDebugEnabled()) {
                log.info("sql = " + query);
            }
            st = con.createStatement();
            rs = st.executeQuery(query);
            if (rs.next()) {
                numSec = rs.getInt(1);
                if (rs.wasNull()) {
                    throw new Exception();
                }
            }
        } catch (Exception ex) {
            log.error("Se ha producido un error generando el siguiente valor de la secuencia " + seqName, ex);
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
        return numSec;
    }

    public GestionAvisosVO getConfiguracionEmail(Connection con, String codEvento, String codigoBusqueda, String opcion) throws Exception {        
        String resultado = "";
        String query="";
        PreparedStatement ps = null;
        ResultSet rs = null;
        GestionAvisosVO gestionAvisosVO = null;
        try{
            if(opcion.equals("doc")){
                int contador = 1;
                query ="SELECT ASUNTO, CONTENIDO, EMAIL, CONFIGURACION_ACTIVA FROM " + MeLanbide06ConfigurationParameter.getParameter(MeLanbide06Constantes.TABLA_MELANBIDE06_CONFIG_MAIL, MeLanbide06Constantes.FICHERO_CONFIGURACION)
                        + " WHERE PROCEDIMIENTO = ? AND EVENTO_COD = ?";
                log.info("sql = " + query);
                ps = con.prepareStatement(query);
                ps.setString(contador++, codigoBusqueda);
                ps.setInt(contador++, Integer.parseInt(codEvento));
                rs=ps.executeQuery();
                while (rs.next()) {
                    gestionAvisosVO = new GestionAvisosVO();
                    gestionAvisosVO.setAsunto(rs.getString("ASUNTO"));
                    gestionAvisosVO.setContenido(rs.getString("CONTENIDO"));
                    gestionAvisosVO.setEmail(rs.getString("EMAIL"));
                    gestionAvisosVO.setConfiguracion_activa(rs.getString("CONFIGURACION_ACTIVA"));
                }
            }else if(opcion.equals("wsregistro")){
                int contador = 1;
                query ="SELECT ASUNTO, CONTENIDO, EMAIL, CONFIGURACION_ACTIVA FROM " + MeLanbide06ConfigurationParameter.getParameter(MeLanbide06Constantes.TABLA_MELANBIDE06_CONFIG_MAIL, MeLanbide06Constantes.FICHERO_CONFIGURACION)
                        + " WHERE ASUNTO_REGISTRO = ? AND EVENTO_COD = ?";
                log.info("sql = " + query);
                ps = con.prepareStatement(query);
                ps.setString(contador++, codigoBusqueda);
                ps.setInt(contador++, Integer.parseInt(codEvento));
                rs=ps.executeQuery();
                while (rs.next()) {
                    gestionAvisosVO = new GestionAvisosVO();
                    gestionAvisosVO.setAsunto(rs.getString("ASUNTO"));
                    gestionAvisosVO.setContenido(rs.getString("CONTENIDO"));
                    gestionAvisosVO.setEmail(rs.getString("EMAIL"));
                    gestionAvisosVO.setConfiguracion_activa(rs.getString("CONFIGURACION_ACTIVA"));
                }              
            }
        }catch (SQLException e) {
            log.error("Se ha producido recuperando los datos de GA ", e);            
            throw new Exception (e);
        }catch (Exception e) {
            log.error("Se ha producido recuperando datos de GA ", e);            
            throw e;
        }finally{
            if(log.isDebugEnabled()) log.info("Procedemos a cerrar el resultset");
            if(ps!=null) ps.close();
            if(rs!=null) rs.close();
        }
        //log.info(" getDatosGA - End "+ codAsunto + formatFechaLog.format(new Date()));
        return gestionAvisosVO;
        
    }
    
    public String buscarEventoExtracto(Connection con, String extracto) throws Exception {        
        String resultado = "";
        String query="";
        PreparedStatement ps = null;
        ResultSet rs = null;
        try{
            int contador = 1;
            query ="SELECT COD_EVENTO FROM " + MeLanbide06ConfigurationParameter.getParameter(MeLanbide06Constantes.TABLA_MELANBIDE06_EVENTO_MAIL, MeLanbide06Constantes.FICHERO_CONFIGURACION)
                    + " WHERE ? LIKE '%'||UPPER(NOMBRE_EVENTO)||'%'";
            log.info("sql = " + query);
            ps = con.prepareStatement(query);
            ps.setString(contador++, extracto.toUpperCase());
            rs=ps.executeQuery();
            while (rs.next()) {
                resultado = Integer.toString(rs.getInt("COD_EVENTO"));
            }
        }catch (SQLException e) {
            log.error("Se ha producido recuperando el evento ", e);            
            throw new Exception (e);
        }catch (Exception e) {
            log.error("Se ha producido recuperando el evento ", e);            
            throw e;
        }finally{
            if(log.isDebugEnabled()) log.info("Procedemos a cerrar el resultset");
            if(ps!=null) ps.close();
            if(rs!=null) rs.close();
        }
        //log.info(" getDatosGA - End "+ extracto + formatFechaLog.format(new Date()));
        return resultado;
        
    }
    
     
}//class

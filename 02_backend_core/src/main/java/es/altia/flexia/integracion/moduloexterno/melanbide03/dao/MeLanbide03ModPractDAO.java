/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.melanbide03.dao;

import es.altia.flexia.integracion.moduloexterno.melanbide03.util.ConfigurationParameter;
import es.altia.flexia.integracion.moduloexterno.melanbide03.util.ConstantesMeLanbide03;
import es.altia.flexia.integracion.moduloexterno.melanbide03.vo.CerModuloFormativoVO;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;



/**
 * @author david.caamano
 * @version 16/08/2012 1.0
 * Historial de cambios:
 * <ol>
 *  <li>david.caamano * 20-09-2012 * #89154 Edición inicial</li>
 * </ol> 
 */
public class MeLanbide03ModPractDAO {
    
    //Logger
    private static Logger log = LogManager.getLogger(MeLanbide03ModPractDAO.class);
    
    //Instance
    private static MeLanbide03ModPractDAO instance = null;
    
    public static MeLanbide03ModPractDAO getInstance(){
        if(log.isDebugEnabled()) log.debug("getInstance() : BEGIN");
        if(instance == null){
            synchronized(MeLanbide03ModPractDAO.class){
                if(instance == null){
                    instance = new MeLanbide03ModPractDAO();
                }//if(instance == null)
            }//synchronized(MeLanbide03ModPractDAO.class)
        }//if(instance == null)
        if(log.isDebugEnabled()) log.debug("getInstance() : END");
        return instance;
    }//getInstance
    
    public void insertarModuloPracticas (CerModuloFormativoVO modulo, Connection con) throws SQLException, Exception{
        if(log.isDebugEnabled()) log.debug("insertarModuloPracticas() : BEGIN");
        if(log.isDebugEnabled()) log.debug("Comprobamos si existe ya una entrada para el expediente");
        try{
            if(existeModuloFormativo(modulo, con)){
                eliminarModuloFormativo(modulo, con);
            }//if(existeModuloFormativo(modulo, con))
            anhadirModuloPracticas(modulo, con);
        }catch (SQLException e) {
            log.error("Se ha producido un error insertando el módulo de formación", e);            
            throw e;
        }catch (Exception e) {
            log.error("Se ha producido un error insertando el módulo de formación", e);            
            throw e;
        }//try-catch
        if(log.isDebugEnabled()) log.debug("insertarModuloPracticas() : END");
    }//insertarModuloPracticas
    
    public CerModuloFormativoVO getModuloFormativo (String numExpediente, Integer codOrganizacion, Connection con) 
            throws SQLException, Exception{
        if(log.isDebugEnabled()) log.debug("getModuloFormativo() : BEGIN");
        Statement st = null;
        ResultSet rs = null;
        CerModuloFormativoVO modulo = new CerModuloFormativoVO();
        try{    
            String sql ="Select COD_CERTIFICADO,NUM_EXPEDIENTE,COD_MODULO,COD_ORGANIZACION,COD_PROCEDIMIENTO,MODULO_ACREDITADO,COD_MOTIVO_ACREDITADO, (select fecha_obtencion_mp from "
                    + ConfigurationParameter.getParameter(ConstantesMeLanbide03.VW_REGEXLAN_CPS_FECHA_OBTENCION, ConstantesMeLanbide03.FICHERO_PROPIEDADES)
                    + " where num_expdte_regexlan='" + numExpediente + "' ) FEC_PRACTICAS from "
                    + ConfigurationParameter.getParameter(ConstantesMeLanbide03.MELANBIDE03_MOD_PRACT, ConstantesMeLanbide03.FICHERO_PROPIEDADES)
                    + " where NUM_EXPEDIENTE = '" + numExpediente + "' and COD_ORGANIZACION = " + codOrganizacion;

            if(log.isDebugEnabled()) log.debug("sql = " + sql);
            st = con.createStatement();
            rs = st.executeQuery(sql);
            
            while(rs.next()){
                modulo.setCodCertificado(rs.getString("COD_CERTIFICADO"));
                modulo.setNumExpediente(rs.getString("NUM_EXPEDIENTE"));
                modulo.setCodModulo(rs.getString("COD_MODULO"));
                modulo.setCodOrganizacion(rs.getInt("COD_ORGANIZACION"));
                modulo.setCodProcedimiento("COD_PROCEDIMIENTO");
                if(rs.getObject("MODULO_ACREDITADO") != null && !rs.wasNull()){
                    modulo.setModuloAcreditado(rs.getInt("MODULO_ACREDITADO"));
                }//if(rs.getObject("MODULO_ACREDITADO") != null && !rs.wasNull())
                modulo.setCodMotivoAcreditacion(rs.getString("COD_MOTIVO_ACREDITADO"));
                modulo.setFecPracticas(rs.getDate("FEC_PRACTICAS"));
            }//while(rs.next())  
        }catch (SQLException e) {
            log.error("Se ha producido un error recuperando el módulo de formación", e);            
            throw e;
        }catch (Exception e) {
            log.error("Se ha producido un error recuperando el módulo de formación", e);            
            throw e;
        }finally{
            if(log.isDebugEnabled()) log.debug("Procedemos a cerrar el statement y el resultset");
            if(rs!=null) rs.close();
            if(st!=null) st.close();
        }//try-catch-finally
        if(log.isDebugEnabled()) log.debug("getModuloFormativo() : END");
        return modulo;
    }//getModuloFormativo
    
    public void borrarModuloPracticas(CerModuloFormativoVO modulo, Connection con) throws SQLException, Exception{
        if(log.isDebugEnabled()) log.debug("borrarModuloPracticas() : BEGIN");
        if(log.isDebugEnabled()) log.debug("Comprobamos si existe ya una entrada para el expediente");
        try{
            if(existeModuloFormativo(modulo, con)){
                eliminarModuloFormativo(modulo, con);
            }//if(existeModuloFormativo(modulo, con))
        }catch (SQLException e) {
            log.error("Se ha producido un error insertando el módulo de formación", e);            
            throw e;
        }catch (Exception e) {
            log.error("Se ha producido un error insertando el módulo de formación", e);            
            throw e;
        }//try-catch
        if(log.isDebugEnabled()) log.debug("borrarModuloPracticas() : END");
    }//borrarModuloPracticas
    
    private void anhadirModuloPracticas (CerModuloFormativoVO modulo, Connection con) throws SQLException, Exception{
        if(log.isDebugEnabled()) log.debug("anhadirModuloPracticas() : BEGIN");
        Statement st = null;
        try{
            String sql = "Insert into " + ConfigurationParameter.getParameter(ConstantesMeLanbide03.MELANBIDE03_MOD_PRACT, ConstantesMeLanbide03.FICHERO_PROPIEDADES)
                    + " (COD_CERTIFICADO, NUM_EXPEDIENTE, COD_ORGANIZACION, COD_PROCEDIMIENTO, COD_MODULO, MODULO_ACREDITADO, COD_MOTIVO_ACREDITADO)"
                    + " values ('" + modulo.getCodCertificado() + "','" + modulo.getNumExpediente() + "'," + modulo.getCodOrganizacion() 
                    + ",'" + modulo.getCodProcedimiento() + "','" + modulo.getCodModulo() + "'," + modulo.getModuloAcreditado() + ","
                    + "'" + modulo.getCodMotivoAcreditacion() + "')";
            
            if(log.isDebugEnabled()) log.debug("sql = " + sql);
            st = con.createStatement();
            int rowsInserted = st.executeUpdate(sql);
            
        }catch (SQLException e) {
            log.error("Se ha producido un error insertando el módulo de formación", e);            
            throw e;
        }catch (Exception e) {
            log.error("Se ha producido un error insertando el módulo de formación", e);            
            throw e;
        }finally{
            if(log.isDebugEnabled()) log.debug("Procedemos a cerrar el statement y el resultset");
            if(st!=null) st.close();
        }//try-catch-finally
        if(log.isDebugEnabled()) log.debug("anhadirModuloPracticas() : END");
    }//insertarModuloPracticas
    
    private Boolean existeModuloFormativo(CerModuloFormativoVO modulo, Connection con)throws SQLException, Exception{
        if(log.isDebugEnabled()) log.debug("existeModuloFormativo() : BEGIN");
        Statement st = null;
        ResultSet rs = null;
        Boolean existe = false;
        try{
            String sql ="Select * from " + ConfigurationParameter.getParameter(ConstantesMeLanbide03.MELANBIDE03_MOD_PRACT, ConstantesMeLanbide03.FICHERO_PROPIEDADES)
                    + " where COD_ORGANIZACION=" + modulo.getCodOrganizacion() + " and NUM_EXPEDIENTE='" + modulo.getNumExpediente() + "'";
            
            if(log.isDebugEnabled()) log.debug("sql = " + sql);
            st = con.createStatement();
            rs = st.executeQuery(sql);
            
            while(rs.next()){
                existe = true;
            }//while(rs.next())
        }catch (SQLException e) {
            log.error("Se ha producido un error insertando el módulo de formación", e);            
            throw e;
        }catch (Exception e) {
            log.error("Se ha producido un error insertando el módulo de formación", e);            
            throw e;
        }finally{
            if(log.isDebugEnabled()) log.debug("Procedemos a cerrar el statement y el resultset");
            if(rs!=null) rs.close();
            if(st!=null) st.close();
        }//try-catch-finally
        if(log.isDebugEnabled()) log.debug("existeModuloFormativo() : END");
        return existe;
    }//existeModuloFormativo
    
    private void eliminarModuloFormativo (CerModuloFormativoVO modulo, Connection con)throws SQLException, Exception{
        if(log.isDebugEnabled()) log.debug("eliminarModuloFormativo() : BEGIN");
        Statement st = null;
        try{
            String sql = "Delete from " + ConfigurationParameter.getParameter(ConstantesMeLanbide03.MELANBIDE03_MOD_PRACT, ConstantesMeLanbide03.FICHERO_PROPIEDADES)
                    + " where COD_ORGANIZACION=" + modulo.getCodOrganizacion() + " and NUM_EXPEDIENTE='" + modulo.getNumExpediente() + "'";
            
            if(log.isDebugEnabled()) log.debug("sql = " + sql);
            st = con.createStatement();
            int rowsDeleted = st.executeUpdate(sql);
        }catch (SQLException e) {
            log.error("Se ha producido un error insertando el módulo de formación", e);            
            throw e;
        }catch (Exception e) {
            log.error("Se ha producido un error insertando el módulo de formación", e);            
            throw e;
        }finally{
            if(log.isDebugEnabled()) log.debug("Procedemos a cerrar el statement y el resultset");
            if(st!=null) st.close();
        }//try-catch-finally
        if(log.isDebugEnabled()) log.debug("eliminarModuloFormativo() : END");
    }//eliminarModuloFormativo
    
}//MeLanbide03ModPractDAO

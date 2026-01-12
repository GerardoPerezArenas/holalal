package es.altia.flexia.integracion.moduloexterno.melanbide05.persistence.dao;

import es.altia.common.service.config.Config;
import es.altia.common.service.config.ConfigServiceHelper;
import es.altia.util.conexion.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import es.altia.common.exception.TechnicalException;
import es.altia.flexia.integracion.moduloexterno.melanbide05.exception.MeLanbide05Exception;
import es.altia.util.commons.DateOperations;
import java.util.Calendar;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

/**
 * @author paz.rodriguez
 */
public class FechaPresentacionDAO {
    
    //Logger
    private static Logger log = LogManager.getLogger(FechaPresentacionDAO.class);
    
    //Instancia de la clase
    private static FechaPresentacionDAO instance = null;
    
    /**
     * Devuelve una instancia unica (Singleton) de la clase
     * 
     * @return FechaPresentacionDAO
     */
    public static FechaPresentacionDAO getInstance(){
        if(log.isDebugEnabled()) log.debug(("getInstance() : BEGIN"));
        if (instance == null) {
            // Necesitamos sincronizacion para serializar (no multithread) las invocaciones de este metodo
            synchronized (FechaPresentacionDAO.class) {
                if (instance == null) {
                    if(log.isDebugEnabled()) log.debug("Creamos una nueva instacia de la clase");
                    instance = new FechaPresentacionDAO();
                }//if (instance == null) 
            }//synchronized (FechaPresentacionDAO.class) 
        }//if (instance == null) 
        if(log.isDebugEnabled()) log.debug(("getInstance() : END"));
        return instance;
    }//getInstance
     
    /** 
     * Metodo que obtiene la fecha de presentacion de un expediente
     *
     * @param conexion Datos de conexión a BBDD
     * @param numExpediente Numero de expediente
     * @return Calendar
     */
    public Calendar getFechaPresentacion(Integer codOrganizacion, String numExpediente, Connection c) throws SQLException{
        if(log.isDebugEnabled()) log.debug("getFechaPresentacion() : BEGIN");
        Calendar fechaPresentacion = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try{
            String sql = "SELECT RES_FEC ";
            sql = sql + " FROM E_EXR,R_RES   ";
            sql = sql + " WHERE EXR_TIP = RES_TIP AND EXR_UOR = RES_UOR AND EXR_NRE = RES_NUM AND EXR_ORI = 1 AND EXR_EJR = RES_EJE ";
            sql = sql + " AND EXR_MUN = ? AND EXR_NUM=?";

            if (log.isDebugEnabled()) log.debug("SQL = " + sql);
            ps = c.prepareStatement(sql);
            ps.setInt(1, codOrganizacion);
            ps.setString(2, numExpediente);
            rs = ps.executeQuery();

            while(rs.next()){
                java.sql.Timestamp tFecha = rs.getTimestamp("RES_FEC");
                fechaPresentacion = DateOperations.timestampToCalendar(tFecha);
            }//while(rs.next()
        }catch(SQLException e){
            log.error("Se ha producido un error recuperando la fecha de presentacion del expediente " + e.getMessage());
            throw e;
        }finally{
            try{
                if(log.isDebugEnabled()) log.debug("Cerramos el prepared statement y el resultset");
                if(ps!=null) ps.close();
                if(rs!=null) rs.close();
            }catch(SQLException e){
                log.error("Se ha producido un error cerrando el prepared statement y el resultset " + e.getMessage());
                throw e;
            }//try-catch
        }//try-catch-finally
        if(log.isDebugEnabled()) log.debug("getFechaPresentacion() : END");
        return fechaPresentacion;
    }//class


   /**
    * Metodo que comprueba si existe fecha de Presentacion de un expediente
    * 
    * @param c: Conexión a la BBDD
    * @param numExpediente: Numero expediente
    * @return boolean
    */
    public boolean existeFechaPresentacion(Integer codOrganizacion, String numExpediente, Connection c) throws Exception {
       if(log.isDebugEnabled()) log.debug("existeFechaPresentacion() : BEGIN");
       boolean retorno=false;
       ResultSet rs = null;
       Statement st = null;
       try{
            String sql = "SELECT RES_FEC, EXR_EJR,EXR_NRE  ";
            sql = sql + " FROM E_EXR,R_RES ";
            sql = sql + " WHERE EXR_NUM='" + numExpediente + "' AND EXR_MUN = " + codOrganizacion + " AND EXR_EJR = RES_EJE";
            sql = sql + " AND EXR_TIP = RES_TIP AND EXR_UOR = RES_UOR AND EXR_NRE = RES_NUM AND EXR_ORI = 1";
            
            if (log.isDebugEnabled()) log.debug("SQL = " + sql);
            st = c.createStatement();
            rs = st.executeQuery(sql);
            
            if(rs.next()){
                if(log.isDebugEnabled()) log.debug("Existe fecha");
                retorno=true;
            }//if(rs.next())
        }catch(Exception e){
            log.error("Se ha producido un error comprobando si existe fecha de presentacion " + e.getMessage());
            throw e;
        }finally{
            try{
                if(log.isDebugEnabled()) log.debug("Cerramos el statement y el resultset");
                if(st!=null) st.close();
                if(rs!=null) rs.close();
            }catch(Exception e){
                log.error("Se ha producido un error cerrando el statement y el resultset " + e.getMessage());
            }//try-catch
        }//try-catch-finally
        
        if (retorno){
            if(log.isDebugEnabled()) log.debug("La fecha de presentacion SI esta en la tabla");
        }else{
            if(log.isDebugEnabled()) log.debug("La fecha de presentacion NO esta en la tabla");
        }//if(retorno)
        if(log.isDebugEnabled()) log.debug("existeFechaPresentacion() : BEGIN");
        return retorno;
    }//existeFechaPresentacion

}//class
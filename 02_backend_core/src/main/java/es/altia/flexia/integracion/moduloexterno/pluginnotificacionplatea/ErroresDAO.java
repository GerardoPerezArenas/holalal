/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package es.altia.flexia.integracion.moduloexterno.pluginnotificacionplatea;

import es.altia.common.exception.TechnicalException;
import es.altia.common.service.config.Config;
import es.altia.common.service.config.ConfigServiceHelper;
import es.altia.flexia.integracion.moduloexterno.pluginnotificacionplatea.util.ErroresVO;
import es.altia.flexia.notificacion.vo.*;
import es.altia.technical.PortableContext;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.altia.util.conexion.BDException;


import java.sql.*;
import java.sql.Connection;
import java.util.ArrayList;
import javax.sql.DataSource;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

public class ErroresDAO {

    private static ErroresDAO instance =	null;
    protected   static Config m_CommonProperties; // Para el fichero de contantes
    protected static Config m_ConfigTechnical; //	Para el fichero de configuracion técnico
    protected static Config m_ConfigError; // Para los mensajes de error localizados
    protected static Logger m_Log =
            LogManager.getLogger(ErroresDAO.class.getName());




     protected ErroresDAO() {
        m_CommonProperties = ConfigServiceHelper.getConfig("common");
        // Queremos usar el	fichero de configuración technical
        m_ConfigTechnical =	ConfigServiceHelper.getConfig("techserver");
        // Queremos tener acceso a los mensajes de error localizados
        m_ConfigError	= ConfigServiceHelper.getConfig("error");
    }

    
     
     public static ErroresDAO getInstance() {
        // Si no hay una instancia de esta clase tenemos que crear una
        synchronized (ErroresDAO.class) {
            if (instance == null) {
                instance = new ErroresDAO();
            }
        }
        return instance;
    }
     
     
     /**
  * Actualiza la información de una notificación
  * @param notificacion: NotificacionVO
  * @param con: Connection 
  * @return Un boolean
  */
 public boolean insertaRegistroError(ErroresVO error, Connection con) throws TechnicalException {

    boolean exito = false;
    Statement st = null;
    String sql = "";
    
     //AdaptadorSQLBD obd = null;
    //Connection con = null;
    
    try{
      //obd = new AdaptadorSQLBD(params);
      //con = obd.getConnection();
      int id = recuperaIdError(con);
      
      sql = "INSERT INTO ERRDIGIT.REG_ERR (REG_ERR_ID, REG_ERR_MEN, REG_ERR_EXCEP_MEN, REG_ERR_CAUSA, "
              + "REG_ERR_TRAZA, REG_ERR_ID_PROC, REG_ERR_ID_CLAVE, REG_ERR_IDEN_ERR_ID, "
              + "REG_ERR_SIS_ORIG, REG_ERR_SITU, REG_ERR_LOG, REG_ERR_OBS, REG_ERR_EVEN,"
              + "REG_ERR_ID_FLEXIA) VALUES ("+id+", '"+error.getMensajeError()+"', '"+error.getMensajeExcepError()+"',"
              + "'" + error.getCausa() + "', '"+error.getTraza()+"', '"+ error.getIdProcedimiento()+"',"
              + "'" + error.getIdClave()+ "', '"+error.getIdError()+"', '"+ error.getSistemaOrigen()+"',"
              + "'" + error.getSituacion()+ "', '"+error.getErrorLog()+"', '"+ error.getObservaciones()+"',"
              + "'" + error.getEvento()+ "', '"+error.getIdFlexia()+"')";

      if(m_Log.isDebugEnabled()) m_Log.debug("Insertando error notificaciones: " +sql);
      st = con.createStatement();
      int resultado = st.executeUpdate(sql);
      if(resultado>=1) exito = true;      

    }catch (Exception e){
        e.printStackTrace();
        if(m_Log.isErrorEnabled()) m_Log.debug("Excepcion capturada en: " + getClass().getName());
        m_Log.error("Error: "+ e);
        exito = false;
    }finally{
        try{
            if (st!=null) st.close();    
            if(con != null) con.close();
        }catch(Exception e){
            e.printStackTrace();
            if(m_Log.isErrorEnabled()) m_Log.debug("Excepcion capturada en: " + getClass().getName());
            
        }
    }

    return exito;
   }
 
 public int recuperaIdError(Connection con){
     int id = 0;
     ResultSet rs = null;
     Statement st = null;
     try
     {
       String sql = "SELECT MAX(REG_ERR_ID) AS NUM FROM ERRDIGIT.REG_ERR";  
       if(m_Log.isDebugEnabled()) m_Log.debug(sql);
       st = con.createStatement();
       rs = st.executeQuery(sql);
       if(rs.next()){
           id = rs.getInt("NUM");
           id++;
        }
     }catch(Exception ex)
     {
         m_Log.debug("Error en recuperaIdError: " + ex);
     }finally{
        try{
            if(st!=null) st.close();
            if(rs!=null) rs.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
     return id;
 }
 
 
 

}

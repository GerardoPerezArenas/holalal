package es.altia.flexia.notificacion.quartz.lanbide.persistence;

import es.altia.agora.business.util.GlobalNames;
import es.altia.common.service.config.Config;
import es.altia.common.service.config.ConfigServiceHelper;
import es.altia.flexia.notificacion.vo.AnotacionSinAdjuntoVO;
import es.altia.util.commons.DateOperations;
import es.altia.util.conexion.AdaptadorSQLBD;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Vector;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 *
 * @author david.vidal
 */
public class NotificacionLanbideDAO {
  
    private static NotificacionLanbideDAO instance = null;
    protected static Config m_ConfigTechnical = ConfigServiceHelper.getConfig("techserver");
    protected static Config m_ConfigError;
    protected static Log m_Log = LogFactory.getLog(NotificacionLanbideDAO.class.getName());

    protected NotificacionLanbideDAO() {
        // Queremos usar el fichero de configuración technical
        m_ConfigTechnical = ConfigServiceHelper.getConfig("techserver");
        // Queremos tener acceso a los mensajes de error localizados
        m_ConfigError = ConfigServiceHelper.getConfig("error");
    }//NotificacionLanbideDAO

    public static NotificacionLanbideDAO getInstance() {
        if(m_Log.isDebugEnabled()) m_Log.debug("getInstance() : BEGIN");
        if (instance == null) {
            synchronized(NotificacionLanbideDAO.class) {
                if (instance == null){
                    if(m_Log.isDebugEnabled()) m_Log.debug("Creamos una nueva instancia");
                    instance = new NotificacionLanbideDAO();
                }//if (instance == null)
            }//synchronized(NotificacionLanbideDAO.class) 
        }//if (instance == null) 
        if(m_Log.isDebugEnabled()) m_Log.debug("getInstance() : BEGIN");
        return instance;
    }//getInstance
    
    
    public Vector getAnotacionesNotificacionConAsunto(String asunto, String dias,String[] params) throws SQLException{
        if(m_Log.isDebugEnabled()) m_Log.debug("getAnotacionesNotificacionConAsunto() : BEGIN");
        Vector resultado = new Vector();     
        AdaptadorSQLBD oad = null;
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat sdfEusk = new SimpleDateFormat("yyyy/MM/dd");
        Connection conexion = null;
        Statement stmt = null;
        ResultSet rs = null;
        String sql = "";   
    
        try{                   
            oad = new AdaptadorSQLBD(params);
            conexion = oad.getConnection();
            
            //Se modifica el código para que no se guarden como envidadas las notificaciones y se envien ciclicamente
            /* ORIGINAL
            sql = " SELECT DISTINCT R_RES.RES_USU,R_RES.RES_NUM,R_RES.RES_EJE,R_RES.RES_UOR,R_RES.RES_DEP,R_RES.ASUNTO,R_RES.RES_FEC, " +
                "     LAN.NOTIFICACION_ENVIADA " +
                " FROM R_RES " +
                " LEFT JOIN LANBIDE_NOTIFICACION LAN ON LAN.NUM_ANOTACION = R_RES.RES_NUM AND " +  
                "   LAN.EJERCICIO_ANOTACION = R_RES.RES_EJE AND " +
                "   LAN.UNIDAD_REGISTRO = R_RES.RES_UOR AND " +
                "   LAN.COD_DEPARTAMENTO = R_RES.RES_DEP AND " +
                "   LAN.COD_ASUNTO = R_RES.ASUNTO AND " +             
                "   LAN.NOTIFICACION_ENVIADA = 0 " +
                " INNER JOIN R_RED ON R_RED.RED_DEP = R_RES.RES_DEP AND " +           
                "   R_RED.RED_UOR = R_RES.RES_UOR AND " +
                "   R_RED.RED_EJE = R_RES.RES_EJE AND " +
                "   R_RED.RED_NUM = R_RES.RES_NUM AND " +
                "   R_RED.RED_TIP = R_RES.RES_TIP AND " +
                "   R_RED.RED_DOC IS NULL " +
                " WHERE R_RES.ASUNTO in (" + asunto.trim() + ") " +
                " AND R_RES.RES_FEC + " + dias +" < SYSDATE " +
                " AND RES_TIP = 'E' " +
                " AND LAN.NOTIFICACION_ENVIADA IS NULL";
            */
                sql = "SELECT R_RES.RES_USU,R_RES.RES_NUM,R_RES.RES_TIP,R_RES.RES_EJE,R_RES.RES_UOR,R_RES.RES_DEP,R_RES.ASUNTO,R_RES.RES_FEC ";
                sql += " FROM R_RES ";
                sql += " WHERE R_RES.ASUNTO in (" + asunto.trim() + ") ";
                sql += " AND R_RES.RES_FEC + " + dias +" < SYSDATE ";
				sql += " AND R_RES.RES_FEC > TO_DATE('2013/01/21','YYYY/MM/DD') ";
                sql += " AND RES_TIP = 'E' ";
				sql += " AND RES_EST <> 9 ";
                sql += " AND  (SELECT COUNT (*) FROM R_RED ";
                sql += " WHERE R_RED.RED_DEP = R_RES.RES_DEP ";
                sql += " AND R_RED.RED_UOR = R_RES.RES_UOR AND ";
                sql += " R_RED.RED_EJE = R_RES.RES_EJE AND ";
                sql += " R_RED.RED_NUM = R_RES.RES_NUM AND ";
                sql += " R_RED.RED_TIP = R_RES.RES_TIP ";
                sql += " AND R_RED.RED_DOC IS NULL) = ";
                sql += " (SELECT COUNT (*) FROM R_RED ";
                sql += " WHERE R_RED.RED_DEP = R_RES.RES_DEP ";
                sql += " AND R_RED.RED_UOR = R_RES.RES_UOR AND ";
                sql += " R_RED.RED_EJE = R_RES.RES_EJE AND ";
                sql += " R_RED.RED_NUM = R_RES.RES_NUM AND ";
                sql += " R_RED.RED_TIP = R_RES.RES_TIP )";
               
            if(m_Log.isInfoEnabled()) m_Log.info("getAnotacionesNotificacionConAsunto()::sql = " + sql);
        
            stmt = conexion.createStatement();
            rs = stmt.executeQuery(sql);
            while(rs.next()){
                AnotacionSinAdjuntoVO registro = new AnotacionSinAdjuntoVO();
                String numero=rs.getString("res_num");
                registro.setNumero_anotacion(numero);
                String ejercicio=rs.getString("res_eje");
                registro.setEjercicio_anotacion(ejercicio);
                registro.setUnidad_registro(rs.getString("res_uor"));
                registro.setFecha_proceso(sdf.format(rs.getDate("res_fec")));
                registro.setFecha_proceso_alt(sdfEusk.format(rs.getDate("res_fec")));
                registro.setCod_departamento(rs.getString("res_dep"));            
                registro.setCod_asunto(rs.getString("asunto"));
                registro.setUsuario(rs.getString("RES_USU"));
                String tipo=rs.getString("res_tip");
                if(!(existeDocumentoRegistroDokusi(tipo,ejercicio,numero,conexion)))
                {
                    resultado.add(registro);
                }
            }//while(rs.next())
        }catch (Exception e){
            m_Log.error("NOTIFICACION LANBIDE: Error en la recuperacion de Notificaciones " + e.getMessage());
        }finally{
            if(m_Log.isDebugEnabled()) m_Log.debug("cerramos el prepared statement y el resultset");
        if (rs != null) rs.close();
        if (stmt != null) stmt.close();       
        conexion.close();
        }//try-catch
        if(m_Log.isDebugEnabled()) m_Log.debug("getAnotacionesNotificacionConAsunto() : END");
        return resultado;
    }//getAnotacionesNotificacionConAsunto
    
    
     public boolean existeDocumentoRegistroDokusi(String tipo, String ejercicio, String numero, Connection conexion) throws SQLException 
        {
            Statement stmt = null;
            ResultSet rs = null;
            String sql = "";
            boolean resultado = false;

            try {
                
               sql = "SELECT * FROM R_RED WHERE RED_TIP='"+tipo+"' AND RED_EJE="+ejercicio+" AND RED_NUM="+numero+" AND RED_IDDOC_GESTOR IS NOT NULL"; 
               
               if(m_Log.isInfoEnabled()) m_Log.debug("existeDocumentoRegistroDokusi()::sql = " + sql);
        
            stmt = conexion.createStatement();
            rs = stmt.executeQuery(sql);
            while(rs.next()){
                if(m_Log.isDebugEnabled()) m_Log.debug("tiene documentos asociados");
                resultado=true;
            }
            
           }catch(SQLException e){
            m_Log.error("Se ha producido un error durante la ejecución de la consulta sobre R_RED.RED_IDDOC_GESTOR: " + e.getMessage());
            e.printStackTrace();            
        }finally{
            try{
                if(stmt!=null) stmt.close();
                if(rs!=null) rs.close();
            }catch(SQLException e){
                m_Log.error("Se ha producido un error al cerrar recursos asociados a la conexión a la BBDD: " + e.getMessage());
            }
          }
            return resultado;
        }
      
    
    public String RecuperaCorreo(AnotacionSinAdjuntoVO datos,String[] params) throws SQLException{        
        if(m_Log.isDebugEnabled()) m_Log.debug("RecuperaCorreo () : BEGIN");
        AdaptadorSQLBD oad = null;
        Connection conexion = null;
        Statement stmt = null;
        ResultSet rs = null;   
    
        try{
            oad = new AdaptadorSQLBD(params);
            conexion = oad.getConnection(); 
        
            String sql = "SELECT USU_EMAIL FROM " + GlobalNames.ESQUEMA_GENERICO + "A_USU WHERE USU_COD = " + datos.getUsuario();   
            if(m_Log.isDebugEnabled()) m_Log.debug("sql = " + sql);
        
            stmt = conexion.createStatement();
            rs = stmt.executeQuery(sql);
        
            if (rs.next()){
                return rs.getString("USU_EMAIL");        
            }else        {
                return null;        
            }//if (rs.next())
        }catch (Exception e){
            m_Log.error("NOTIFICACION LANBIDE: Error durante la recuperacion de correo electronico " + e.getMessage());
        }finally{
            if(m_Log.isDebugEnabled()) m_Log.debug("Cerramos el statement y el resultset");
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();       
            conexion.close();
        }//try-catch-finally
        if(m_Log.isDebugEnabled()) m_Log.debug("RecuperaCorreo () : END");
        return null;        
    }//RecuperaCorreo
    
    public void GuardarNotificacion(AnotacionSinAdjuntoVO datos, String estado_notif,String[] params) throws SQLException{
        if(m_Log.isDebugEnabled()) m_Log.debug("GuardarNotificacion () : BEGIN");
        AdaptadorSQLBD oad = null;
        Connection conexion = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;       
        Timestamp Fecha = null;
    
        try{
            oad = new AdaptadorSQLBD(params);
            conexion = oad.getConnection();       
            Fecha = DateOperations.toTimestamp(Calendar.getInstance());        
        
            String sql = "INSERT INTO LANBIDE_NOTIFICACION VALUES (" + 
                datos.getNumero_anotacion() +","+
                datos.getEjercicio_anotacion()+","+
                datos.getUnidad_registro()+","+
                "?,"+
                datos.getCod_departamento()+",'"+
                datos.getCod_asunto()+"',"+
                estado_notif +")";        
            if(m_Log.isDebugEnabled()) m_Log.debug("sql = " + sql);
        
            stmt = conexion.prepareStatement(sql);
            stmt.setTimestamp(1,Fecha);
            stmt.executeUpdate();      
        }catch (Exception e){
            m_Log.error("NOTIFICACION LANBIDE: Error durante el almacenamiento de datos " + e.getMessage());
        }finally{
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();       
            conexion.close();
        }//try-catch-finally
        if(m_Log.isDebugEnabled()) m_Log.debug("GuardarNotificacion () : END");
    }//GuardarNotificacion

}//class
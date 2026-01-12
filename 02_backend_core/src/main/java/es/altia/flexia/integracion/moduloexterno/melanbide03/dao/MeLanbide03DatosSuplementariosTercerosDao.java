/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.melanbide03.dao;

import es.altia.flexia.integracion.moduloexterno.melanbide03.exception.MELANBIDE03Exception;
import es.altia.flexia.integracion.moduloexterno.melanbide03.util.ConfigurationParameter;
import es.altia.flexia.integracion.moduloexterno.melanbide03.util.ConstantesMeLanbide03;
import es.altia.flexia.integracion.moduloexterno.melanbide03.vo.Melanbide03ExpedientesVO;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

/**
 * @author david.caamano
 * @version 16/10/2012 1.0
 * Historial de cambios:
 * <ol>
 *  <li>david.caamano * 16-10-2012 * Edición inicial</li>
 * </ol> 
 */
public class MeLanbide03DatosSuplementariosTercerosDao {
    
    //Logger
    private static Logger log = LogManager.getLogger(MeLanbide03DatosSuplementariosTercerosDao.class);
    
    //Instancia
    private static MeLanbide03DatosSuplementariosTercerosDao instance = null;
    
    /**
     * Devuelve una instancia de MeLanbide03CertificadoDAO, si no existe la crea
     * @return MeLanbide03DatosSuplementariosTercerosDao
     */
    public static MeLanbide03DatosSuplementariosTercerosDao getInstance(){
        if(log.isDebugEnabled()) log.debug("getInstance() : BEGIN");
        if(instance == null){
            synchronized(MeLanbide03DatosSuplementariosTercerosDao.class){
                if(instance == null){
                    instance = new MeLanbide03DatosSuplementariosTercerosDao();
                }//if(instance == null)
            }//synchronized(MeLanbide03DatosSuplementariosTercerosDao.class)
        }//if(instance == null)
        if(log.isDebugEnabled()) log.debug("getInstance() : END");
        return instance;
    }//getInstance
    
    public Date getFechaNacimientoTercero (Integer codTercero, Connection con) throws SQLException, Exception{
        if(log.isDebugEnabled()) log.debug("getFechaNacimientoTercero() : BEGIN");
        Date fechaNacimiento = null;
        Statement st = null;
        ResultSet rs = null;
        try{
            //Recuperamos del fichero de propiedades la tabla y el codigo del campo adicional de tercero para su fecha de nacimiento
            String tablaFecNac = "";
            String campoFecNac = "";
                tablaFecNac = ConfigurationParameter.getParameter(ConstantesMeLanbide03.TABLA_FECHA_NACIMIENTO, 
                        ConstantesMeLanbide03.FICHERO_PROPIEDADES);
                campoFecNac = ConfigurationParameter.getParameter(ConstantesMeLanbide03.CAMPO_FECHA_NACIMIENTO, 
                        ConstantesMeLanbide03.FICHERO_PROPIEDADES);
                
            //Realizamos la consulta
            String sql="Select VALOR from " + tablaFecNac + " where COD_CAMPO = '" + campoFecNac + "' and COD_TERCERO = " + codTercero;
            if(log.isDebugEnabled()) log.debug("sql = " + sql);
            st = con.createStatement();
            rs = st.executeQuery(sql);
                
            while(rs.next()){
                Calendar cal=Calendar.getInstance();
                if(rs.getDate("VALOR") != null){
                    cal.setTime(rs.getDate("VALOR"));
                    fechaNacimiento = cal.getTime();
                }//if(rs.getDate("VALOR") != null)
            }//while(rs.next())
        }catch (SQLException e) {
            log.error("Se ha producido un error recuperando el campo suplementario de la fecha de nacimiento del tercero", e);            
            throw e;
        }catch (Exception e) {
            log.error("Se ha producido un error recuperando el campo suplementario de la fecha de nacimiento del tercero", e);            
            throw e;
        }finally{
            if(log.isDebugEnabled()) log.debug("Procedemos a cerrar el statement y el resultset");
            if(rs!=null) rs.close();
            if(st!=null) st.close();
        }//try-catch-finally
        if(log.isDebugEnabled()) log.debug("getFechaNacimientoTercero() : END");
        return fechaNacimiento;
    }//getFechaNacimientoTercero
    
    public boolean obetenerDatosExpedientesEMPNL(int codOrganizacion, String numExpediente, Connection con)throws MELANBIDE03Exception, SQLException
    {
        boolean existe = false;
        Statement st = null;
        ResultSet rs = null;
        try
        {
            String sql = "SELECT * " +
                "FROM E_EXT " +
                "INNER JOIN T_TER ON EXT_TER = TER_COD " +
                "INNER JOIN MELANBIDE03_CERTIFICADO ON NUM_EXPEDIENTE = EXT_NUM " +
                "WHERE EXT_NUM = '"+numExpediente+"' AND TER_DOC||COD_CERTIFICADO IN ( " +
                "  SELECT TER_DOC||COD_CERTIFICADO  " +
                "  FROM E_EXT " +
                "  INNER JOIN T_TER ON EXT_TER = TER_COD " +
                "  INNER JOIN MELANBIDE40_CERTIFICADO ON NUM_EXPEDIENTE = EXT_NUM "+
                "  WHERE EXT_PRO = 'EMPNL' " +
                ")";
            if(log.isDebugEnabled()) log.debug("sql = " + sql);
            st = con.createStatement();
            rs = st.executeQuery(sql);
                
            if(rs.next())
                existe = true;           
            
        }
        catch(Exception ex){            
            log.error("Error en MeLanbide03DatosSuplementariosTercerosDao", ex);    
            throw new MELANBIDE03Exception("Error en MeLanbide03DatosSuplementariosTercerosDao", ex);   
        }finally{
            if(log.isDebugEnabled()) log.debug("Procedemos a cerrar el statement y el resultset");
            if(rs!=null) rs.close();
            if(st!=null) st.close();
        }//try-catch-finally
        return existe;        
    }
    
    public ArrayList<Melanbide03ExpedientesVO>  datosExpedientesEMPNL(int codOrganizacion, String numExpediente, Connection con)throws MELANBIDE03Exception, SQLException
    {
        boolean existe = false;
        ArrayList<Melanbide03ExpedientesVO> exp = new ArrayList<Melanbide03ExpedientesVO>();
        
        Statement st = null;
        ResultSet rs = null;
        try
        {
            
            String sql = "SELECT EXT_NUM, DES_NOM " +
                    "FROM (" +
                    "select EXT_NUM, TER_DOC "
                    + "FROM T_TER " +
                    "  INNER JOIN E_EXT ON TER_COD = EXT_TER " +
                    "  INNER JOIN MELANBIDE40_CERTIFICADO ON NUM_EXPEDIENTE = EXT_NUM "+
                    "  WHERE EXT_PRO = 'EMPNL' AND TER_DOC||COD_CERTIFICADO IN ( " +
                    "    select TER_DOC||COD_CERTIFICADO " +
                    "    FROM T_TER " +
                    "    INNER JOIN E_EXT ON TER_COD = EXT_TER " +
                    "    INNER JOIN MELANBIDE03_CERTIFICADO ON NUM_EXPEDIENTE = EXT_NUM"+
                    "    WHERE EXT_NUM = '"+numExpediente+"' " +
                    "  ) " +
                    ") "
                    + "LEFT JOIN E_TDE ON TDE_NUM  = EXT_NUM AND TDE_COD = 'MOTEXENC' " +
                    "LEFT JOIN E_DES_VAL ON DES_vAL_COD = TDE_VALOR";
            /*String sql = "SELECT EXT_NUM, DES_NOM " +
                    "FROM (" +
                    "  SELECT DISTINCT P.EXT_NUM AS EXT_NUM " +
                    "  FROM E_EXT E " +
                    "  INNER JOIN T_TER ON EXT_TER = TER_COD " +
                    "  LEFT JOIN T_HTE ON EXT_TER =HTE_TER AND EXT_NVR = HTE_NVR " +
                    "  LEFT JOIN T_DOT ON EXT_TER = DOT_TER " +
                    "  LEFT JOIN T_DNN ON  DOT_DOM = DNN_DOM " +
                    "  INNER JOIN (" + 
                    "   SELECT  E_EXT.* " + 
                    "   FROM E_EXT  " + 
                    "   WHERE EXT_PRO = 'EMPNL'  " + 
                    "  ) P ON P.EXT_TER = E.EXT_TER AND  P.EXT_DOT = E.EXT_DOT AND  P.EXT_ROL = E.EXT_ROL AND DOT_TER = P.EXT_TER  " + 
                    "  AND  P.EXT_TER =HTE_TER  AND P.EXT_TER = TER_COD   " +                     
                    "  WHERE E.EXT_NUM = '"+numExpediente+"' " + 
                    ") " +
                    "LEFT JOIN E_TDE ON TDE_NUM = EXT_NUM and TDE_COD = 'MOTEXENC' " +
                    "LEFT JOIN E_DES_VAL ON DES_vAL_COD = TDE_VALOR";*/
            if(log.isDebugEnabled()) log.debug("sql = " + sql);
            st = con.createStatement();
            rs = st.executeQuery(sql); 
            while(rs.next()){               
                String motivo = "";
                Melanbide03ExpedientesVO datos = new Melanbide03ExpedientesVO();
                datos.setCodExpediente(rs.getString("EXT_NUM"));
                if(rs.getString("DES_NOM") != null)
                    motivo = rs.getString("DES_NOM");
                datos.setDesMotivo(motivo);
                exp.add(datos);
            }
        }
        catch(Exception ex){            
            log.error("Error en MeLanbide03DatosSuplementariosTercerosDao", ex);    
            throw new MELANBIDE03Exception("Error en MeLanbide03DatosSuplementariosTercerosDao", ex);   
        } finally{
            if(log.isDebugEnabled()) log.debug("Procedemos a cerrar el statement y el resultset");
            if(rs!=null) rs.close();
            if(st!=null) st.close();
        }//try-catch-finally
        
        return exp;        
    }
    
}//class

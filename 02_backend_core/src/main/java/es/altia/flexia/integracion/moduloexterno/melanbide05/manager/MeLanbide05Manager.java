package es.altia.flexia.integracion.moduloexterno.melanbide05.manager;

import es.altia.flexia.integracion.moduloexterno.melanbide05.exception.MeLanbide05Exception;
import es.altia.flexia.integracion.moduloexterno.melanbide05.persistence.dao.FechaPresentacionDAO;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.altia.util.conexion.BDException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.logging.Level;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

/**
 * @author david.caamano
 * @version 14/01/2013 1.0
 * Historial de cambios:
 * <ol>
 *  <li>david.caamano * 14/01/2013 * Edición inicial</li>
 * </ol> 
 */
public class MeLanbide05Manager {
    
    //Logger
    private static Logger log = LogManager.getLogger(MeLanbide05Manager.class);
    
    //Instancia de la clase
    private static MeLanbide05Manager instance = null;
    
    /**
     * Devuelve una instancia (Singleton) de la clase
     * 
     * @return MeLanbide05Manager
     */
    public static MeLanbide05Manager getInstance(){
        if(log.isDebugEnabled()) log.debug("getInstance() : BEGIN");
        if(instance == null){
            synchronized(MeLanbide05Manager.class){
                if(instance == null){
                    if(log.isDebugEnabled()) log.debug("Creamos una nueva instancia de la clase");
                    instance = new MeLanbide05Manager();
                }//if(instance == null)
            }//synchronized(MeLanbide05Manager.class)
        }//if(instance == null)
        if(log.isDebugEnabled()) log.debug("getInstance() : END");
        return instance;
    }//getInstance
    
    /**
     * Devuelve la fecha de presentacion de la anotacion en registro que inicio el expediente
     * 
     * @param codOrganizacion
     * @param numExpediente
     * @param adaptador
     * @return
     * @throws MeLanbide05Exception 
     */
    public Calendar getFechaPresentacion(Integer codOrganizacion, String numExpediente, AdaptadorSQLBD adaptador) throws MeLanbide05Exception{
        if(log.isDebugEnabled()) log.debug("getFechaPresentacion() : BEGIN");
        Calendar fechaPresentacion = null;
        Connection con = null;
        try{
            con = adaptador.getConnection();
            FechaPresentacionDAO fechaPresentacionDao = FechaPresentacionDAO.getInstance();
            fechaPresentacion = fechaPresentacionDao.getFechaPresentacion(codOrganizacion, numExpediente, con);      
            if(con!=null) con.close();
        } catch (BDException ex) {
            log.error("Se ha producido un error recuperando la fecha de presentacion del expediente " + ex.getMessage());
            throw new MeLanbide05Exception(numExpediente, ex);
        }catch(SQLException ex){
            log.error("Se ha producido un error recuperando la fecha de presentacion del expediente " + ex.getMessage());
            throw new MeLanbide05Exception(numExpediente, ex);
          } finally {
            if (con != null) try {
                adaptador.devolverConexion(con);
            } catch (BDException ex) {
                log.error("Erron al cerrar la conexión con la BBDD");
            }
        }
        if(log.isDebugEnabled()) log.debug("getFechaPresentacion() : END");
        return fechaPresentacion;
    }//getFechaPresentacion
    
    /**
     * Devuelve si existe una fecha de presentacion de la anotacion en registro que inicio el expediente
     * 
     * @param codOrganizacion
     * @param numExpediente
     * @param adaptador
     * @return
     * @throws MeLanbide05Exception 
     */
    public Boolean existeFechaPresentacion(Integer codOrganizacion, String numExpediente, AdaptadorSQLBD adaptador) throws MeLanbide05Exception{
        if(log.isDebugEnabled()) log.debug("existeFechaPresentacion() : BEGIN");
        Boolean existe = false;
        Connection con = null;
        try{
            con = adaptador.getConnection();
            FechaPresentacionDAO fechaPresentacionDao = FechaPresentacionDAO.getInstance();
            existe = fechaPresentacionDao.existeFechaPresentacion(codOrganizacion, numExpediente, con);            
            if(con!=null) con.close();
        } catch (BDException ex) {
            log.error("Se ha producido un error comprobando si existe fecha de presentacion para el expediente " + ex.getMessage());
            throw new MeLanbide05Exception(numExpediente, ex);
        }catch(Exception ex){
            log.error("Se ha producido un error comprobando si existe fecha de presentacion para el expediente " + ex.getMessage());
            throw new MeLanbide05Exception(numExpediente, ex);
         } finally {
            if (con != null) try {
                adaptador.devolverConexion(con);
            } catch (BDException ex) {
                log.error("Erron al cerrar la conexión con la BBDD");
            }
        }
        if(log.isDebugEnabled()) log.debug("existeFechaPresentacion() : END");
        return existe;
    }//existeFechaPresentacion
    
}//class

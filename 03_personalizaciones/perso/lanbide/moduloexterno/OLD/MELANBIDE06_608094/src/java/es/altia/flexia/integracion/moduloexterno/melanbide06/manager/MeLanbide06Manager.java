package es.altia.flexia.integracion.moduloexterno.melanbide06.manager;

import es.altia.flexia.integracion.moduloexterno.melanbide06.MeLanbide06Exception;
import es.altia.flexia.integracion.moduloexterno.melanbide06.dao.MeLanbide06Dao;
import es.altia.flexia.integracion.moduloexterno.melanbide06.vo.SelectItem;
import es.altia.util.conexion.AdaptadorSQLBD;
import java.sql.Connection;
import java.sql.SQLException;
import org.apache.log4j.Logger;


/**
 * @author david.caamano
 * @version 17/01/2013 1.0
 * Historial de cambios:
 * <ol>
 *  <li>david.caamano * 17/01/2013 * Edición inicial</li>
 * </ol> 
 */
public class MeLanbide06Manager {
    
    //Logger
    private static Logger log = Logger.getLogger(MeLanbide06Manager.class);
    
    //Instance
    private static MeLanbide06Manager instance = null;
    
    /**
     * Devuelve una unica instancia (Singleton) de la clase MeLanbide06Manager
     * 
     * @return MeLanbide06Manager
     */
    public static MeLanbide06Manager getInstance(){
       if(log.isDebugEnabled()) log.debug("getInstance() : BEGIN"); 
       if(instance == null){
            synchronized(MeLanbide06Manager.class){
                if(instance == null){
                    if(log.isDebugEnabled()) log.debug("Creamos una nueva instancia");
                    instance = new MeLanbide06Manager();
                }//if(instance == null)
            }//synchronized(MeLanbide06Manager.class)
       }//if(instance == null)
       if(log.isDebugEnabled()) log.debug("getInstance() : BEGIN"); 
       return instance;
    }//getInstance
    
    /**
     * Devuelve la descripcion de un tramite para un expediente
     * 
     * @param codOrganizacion
     * @param codTramite
     * @param numExpediente
     * @param codProcedimiento
     * @param ejercicio
     * @param adaptador
     * @return
     * @throws MeLanbide06Exception 
     */
    public String getDescripcionUorTramite(Integer codOrganizacion, Integer codTramite, String numExpediente, String codProcedimiento,
            String ejercicio,  AdaptadorSQLBD adaptador) throws MeLanbide06Exception{
        if(log.isDebugEnabled()) log.debug("getDescripcionUorTramite() : BEGIN");
        String descripcion = new String();
        Connection con = null;
        try{
            con = adaptador.getConnection();
            MeLanbide06Dao meLanbide06Dao = MeLanbide06Dao.getInstance();
            if(log.isDebugEnabled()) log.debug("Recuperamos la descripcion de la unidad organica");
            descripcion = meLanbide06Dao.getDescripcionUorTramite(codOrganizacion, codTramite, codProcedimiento, numExpediente, ejercicio, con);
        }catch(Exception ex){
            log.error("Se ha producido un error recuperando la descripcion de la unidad organica de un tramite " + ex.getMessage());
            throw new MeLanbide06Exception("Se ha producido un error recuperando la descripcion de la unidad organica de un tramite ", ex);
        }finally{
            try{
                if(con!=null) con.close();                
            }catch(SQLException e){
                log.error("Error al cerrar la conexión a la BBDD: " + e.getMessage());
            }
        }////try-catch-finally
        if(log.isDebugEnabled()) log.debug("getDescripcionUorTramite() : END");
        return descripcion;
    }//getDescripcionUorTramite
    
    /**
     * Recupera la descripcion de una unidad organica por su codigo visible
     * 
     * @param uorCodVis
     * @param adaptador
     * @return
     * @throws Exception 
     */
    public String getDescripcionUor (String uorCodVis, AdaptadorSQLBD adaptador) throws Exception{
        if(log.isDebugEnabled()) log.debug("getDescripcionUor() : BEGIN");
        String descripcion = new String();
        Connection con = null;
        try{
            con = adaptador.getConnection();
            MeLanbide06Dao meLanbide06Dao = MeLanbide06Dao.getInstance();
            if(log.isDebugEnabled()) log.debug("Recuperamos la descripcion de la unidad organica");
            descripcion = meLanbide06Dao.getDescripcionUor(uorCodVis, con);
        }catch(Exception ex){
            log.error("Se ha producido un error recuperando la descripcion de la unidad organica " + ex.getMessage());
            throw new MeLanbide06Exception("Se ha producido un error recuperando la descripcion de la unidad organica ", ex);
        }finally{
            try{
                if(con!=null) con.close();                
            }catch(SQLException e){
                log.error("Error al cerrar la conexión a la BBDD: " + e.getMessage());
            }
        }//try-catch-finally
        if(log.isDebugEnabled()) log.debug("getDescripcionUor() : END");
        return descripcion;
    }//getDescripcionUor
    
    /**
     * Recupera la descripcion de una unidad organica por su codigo visible
     * 
     * @param uorCodVis
     * @param adaptador
     * @return
     * @throws Exception 
     */
    public String getDescripcionTramite (int codTramite, String codProcedimiento, AdaptadorSQLBD adaptador) throws Exception{
        if(log.isDebugEnabled()) log.debug("getDescripcionUor() : BEGIN");
        String descripcion = new String();
        Connection con = null;
        try{
            con = adaptador.getConnection();
            MeLanbide06Dao meLanbide06Dao = MeLanbide06Dao.getInstance();
            if(log.isDebugEnabled()) log.debug("Recuperamos la descripcion del Tramite");
            descripcion = meLanbide06Dao.getDescripcionTramite(codTramite, codProcedimiento, con);
        }catch(Exception ex){
            log.error("Se ha producido un error recuperando la descripcion de la unidad organica " + ex.getMessage());
            throw new MeLanbide06Exception("Se ha producido un error recuperando la descripcion de la unidad organica ", ex);
        }finally{
            try{
                if(con!=null) con.close();                
            }catch(SQLException e){
                log.error("Error al cerrar la conexión a la BBDD: " + e.getMessage());
            }
        }//try-catch-finally
        if(log.isDebugEnabled()) log.debug("getDescripcionUor() : END");
        return descripcion;
    }//getDescripcionUor
    
    /**
     * Recupera el interesado
     * 
     * @param expediente
     * @param adaptador
     * @return
     * @throws Exception 
     */
    public String getInteresado (String expediente, AdaptadorSQLBD adaptador) throws Exception{
        if(log.isDebugEnabled()) log.debug("getInteresado() : BEGIN");
        String interesado = new String();
        Connection con = null;
        try{
            con = adaptador.getConnection();
            MeLanbide06Dao meLanbide06Dao = MeLanbide06Dao.getInstance();
            if(log.isDebugEnabled()) log.debug("Recuperamos el interesado");
            interesado = meLanbide06Dao.getInteresado(expediente, con);
        }catch(Exception ex){
            log.error("Se ha producido un error recuperando el interesado " + ex.getMessage());
            throw new MeLanbide06Exception("Se ha producido un error recuperando el interesado ", ex);
        }finally{
            try{
                if(con!=null) con.close();                
            }catch(SQLException e){
                log.error("Error al cerrar la conexión a la BBDD: " + e.getMessage());
            }
        }//try-catch-finally
        if(log.isDebugEnabled()) log.debug("getInteresado() : END");
        return interesado;
    }//getInteresado
    
}//class

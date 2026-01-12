package es.altia.flexia.integracion.moduloexterno.melanbide03.dao;

import es.altia.agora.business.util.GlobalNames;
import es.altia.flexia.integracion.moduloexterno.melanbide03.util.ConfigurationParameter;
import es.altia.flexia.integracion.moduloexterno.melanbide03.util.ConstantesMeLanbide03;
import es.altia.flexia.integracion.moduloexterno.melanbide03.vo.CerCentroVO;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.ResourceBundle;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;


/**
 * @author david.caamano
 * @version 16/08/2012 1.0
 * Historial de cambios:
 * <ol>
 *  <li>david.caamano * 16-08-2012 * #86969 Edición inicial</li>
 * </ol> 
 */
public class CerCentrosDAO {
    
    //Logger
    private static Logger log = LogManager.getLogger(CerCentrosDAO.class);
    
    //Instancia
    private static CerCentrosDAO instance = null;
    
    /**
     * Recupera una única instancia de la clase CerCentrosDao y si no existe la crea.
     * @return CerCentrosDao
     */
    public static CerCentrosDAO getInstance(){
        if(log.isDebugEnabled()) log.debug("getInstance() : BEGIN");
        if (instance == null) {
            synchronized(CerCentrosDAO.class){
                if(instance == null){
                    instance = new CerCentrosDAO();
                }//if(instance == null)
            }//synchronized(CerCentrosDao.class)
        }////if (instance == null)
        if(log.isDebugEnabled()) log.debug("getInstance() : END");
        return instance;
    }//getInstance
   
   
}
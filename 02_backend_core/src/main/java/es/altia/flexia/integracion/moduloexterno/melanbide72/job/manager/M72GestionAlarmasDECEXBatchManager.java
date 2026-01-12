
package es.altia.flexia.integracion.moduloexterno.melanbide72.job.manager;

import es.altia.flexia.integracion.moduloexterno.melanbide72.jobs.dao.M72GestionAlarmasDECEXBatchDAO;
import es.altia.flexia.integracion.moduloexterno.melanbide72.vo.AlarmaVO;
import es.altia.util.conexion.AdaptadorSQL;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

/**
 *
 * @author gerardo.perez
 */


public class M72GestionAlarmasDECEXBatchManager {
    
    
    private M72GestionAlarmasDECEXBatchDAO  m72GestionAlarmasDECEXBatchDAO = new M72GestionAlarmasDECEXBatchDAO();
    private static final Logger log = LogManager.getLogger(M72GestionAlarmasDECEXBatchManager.class);
    
     //Instancia
    private static M72GestionAlarmasDECEXBatchManager instance = null;

    public static M72GestionAlarmasDECEXBatchManager getInstance() {
        if (instance == null) {
            synchronized (M72GestionAlarmasDECEXBatchManager.class) {
                instance = new M72GestionAlarmasDECEXBatchManager();
            }
        }
        return instance;
    }
    
    public List<AlarmaVO> getListaExpedienteMailAlarma1(AdaptadorSQL adaptadorSQL) throws Exception {
        List<AlarmaVO> listaAlarmas = new ArrayList();
        Connection con = null;
        try
        {
          con = adaptadorSQL.getConnection();
          listaAlarmas = this.m72GestionAlarmasDECEXBatchDAO.getListaExpedienteMailAlarma1(con);
        }
        catch (Exception ex)
        {
          log.error("Error recogiendo/enviando alertas Alarma1 : " + ex.getMessage(),ex);
        }
        finally
        {
          adaptadorSQL.devolverConexion(con);
        }
        return listaAlarmas;
    }

    public List<AlarmaVO> getListaExpedienteMailAlarma2(AdaptadorSQL adaptadorSQL) throws Exception {
        List<AlarmaVO> listaAlarmas = new ArrayList();
        Connection con = null;
        try {
            con = adaptadorSQL.getConnection();
            listaAlarmas=m72GestionAlarmasDECEXBatchDAO.getListaExpedienteMailAlarma2(con);            
        } 
        catch (Exception ex) 
        {
            log.error("Error recogiendo/enviando alertas Alarma2 : " + ex.getMessage(),ex);
        }finally{
            adaptadorSQL.devolverConexion(con);
        }
        return listaAlarmas;
    } 
    
    public List<AlarmaVO> getListaExpedienteMailAlarma3(AdaptadorSQL adaptadorSQL) throws Exception {
        List<AlarmaVO> listaAlarmas = new ArrayList();
        Connection con = null;
        try {
            con = adaptadorSQL.getConnection();
            listaAlarmas=m72GestionAlarmasDECEXBatchDAO.getListaExpedienteMailAlarma3(con);            
        } 
        catch (Exception ex) 
        {
            log.error("Error recogiendo/enviando alertas Alarma3 : " + ex.getMessage(),ex);
        }finally{
            adaptadorSQL.devolverConexion(con);
        }
        return listaAlarmas;
    } 
    
    public List<AlarmaVO> getListaExpedienteMailAlarma4(AdaptadorSQL adaptadorSQL) throws Exception {
        List<AlarmaVO> listaAlarmas = new ArrayList();
        Connection con = null;
        try {
            con = adaptadorSQL.getConnection();
            listaAlarmas=m72GestionAlarmasDECEXBatchDAO.getListaExpedienteMailAlarma4(con);            
        } 
        catch (Exception ex) 
        {
            log.error("Error recogiendo/enviando alertas Alarma4 : " + ex.getMessage(),ex);
        }finally{
            adaptadorSQL.devolverConexion(con);
        }
        return listaAlarmas;
    } 
}
    

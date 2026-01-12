
package es.altia.flexia.integracion.moduloexterno.melanbide08.manager;
import es.altia.agora.business.sge.TramitacionExpedientesValueObject;
import es.altia.agora.business.sge.exception.TramitacionException;
import es.altia.agora.business.sge.persistence.manual.DefinicionProcedimientosDAO;
import es.altia.agora.business.sge.persistence.manual.DefinicionTramitesDAO;
import es.altia.agora.business.sge.persistence.manual.TramitacionExpedientesDAO;
import es.altia.agora.business.sge.plugin.documentos.vo.DocumentoGestor;
import es.altia.common.exception.TechnicalException;
import es.altia.flexia.integracion.moduloexterno.melanbide08.dao.MeLanbide08DAO;
import es.altia.flexia.integracion.moduloexterno.melanbide08.dao.MeLanbide08JobDAO;
import es.altia.flexia.integracion.moduloexterno.melanbide08.vo.ComboVO;
import es.altia.flexia.integracion.moduloexterno.melanbide08.vo.FiltrosVO;
import es.altia.flexia.integracion.moduloexterno.melanbide08.vo.ResultadoConsultaVO;
import es.altia.flexia.integracion.moduloexterno.melanbide08.vo.RetornoResultadosFiltros;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.altia.util.conexion.BDException;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import es.altia.flexia.integracion.moduloexterno.melanbide_dokusi.manager.MeLanbideDokusiManager;
import es.altia.flexia.integracion.moduloexterno.melanbide_dokusi.util.ConstantesMeLanbide_Dokusi;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
/**
 *
 * @author alexandrep
 */
public class MeLanbide08LogJobManager {
    
    private static final Logger LOG = LogManager.getLogger(MeLanbide08LogJobManager.class);
    SimpleDateFormat formatDate = new SimpleDateFormat("yyyyMMddHHmmssSSS");

        //Instancia
    private static MeLanbide08LogJobManager instance = null;

    public static MeLanbide08LogJobManager getInstance() {
        if (instance == null) {
            synchronized (MeLanbide08LogJobManager.class) {
                instance = new MeLanbide08LogJobManager();
            }
        }
        return instance;
    }
    
    /**
     *

     * @throws Exception
     */
    public int insertarLineasLogJob(AdaptadorSQLBD adaptador, ResultadoConsultaVO resultadoConsulta) throws Exception {
        LOG.info("insertarLineasLogJob - Begin () " + formatDate.format(new Date()));
        Connection con = null;
         int id = 0;

        try { 
            con = adaptador.getConnection();
            
            id = MeLanbide08JobDAO.getInstance().insertarLineasLogJob(con,resultadoConsulta);
           
        } catch (BDException ex){
            LOG.error("Se ha producido una excepcion en la BBDD recuperando los datos " + " Error : " + ex.getMensaje(), ex);
            throw ex;
        } catch(Exception e){
            LOG.error("Error al leer los datos " + " Error : " + e.getMessage());
            throw e;
        } finally {
            try{
                adaptador.devolverConexion(con);
            }catch (Exception e){
                LOG.error("Error al cerrar conexion a la BBDD; " + e.getMessage());
            }
        }
        LOG.info("getLog - End () " + formatDate.format(new Date()));
        return id;
    }
    
    
     /**
     *

     * @throws Exception
     */
    public Boolean actualizarLineasLogJob(AdaptadorSQLBD adaptador, int idLog, String estado, String Mensaje) throws Exception {
        LOG.info("actualizarLineasLogJob - Begin () " + formatDate.format(new Date()));
        Connection con = null;
         boolean resultado = false;

        try { 
            con = adaptador.getConnection();
            
           resultado=  MeLanbide08JobDAO.getInstance().actualizarLineasLogJob(con,idLog, estado, Mensaje);
           
        } catch (BDException ex){
            LOG.error("Se ha producido una excepcion en la BBDD recuperando los datos " + " Error : " + ex.getMensaje(), ex);
            throw ex;
        } catch(Exception e){
            LOG.error("Error al leer los datos " + " Error : " + e.getMessage());
            throw e;
        } finally {
            try{
                adaptador.devolverConexion(con);
            }catch (Exception e){
                LOG.error("Error al cerrar conexion a la BBDD; " + e.getMessage());
            }
        }
        LOG.info("getLog - End () " + formatDate.format(new Date()));
        return resultado;
    }
    
    
      public RetornoResultadosFiltros getLog(int codOrganizacion, AdaptadorSQLBD adaptador, FiltrosVO filtros, List<ComboVO> listaProcedimiento) throws Exception {
        LOG.info("getLog - Begin () " + formatDate.format(new Date()));
        Connection con = null;
        RetornoResultadosFiltros retorno = new RetornoResultadosFiltros();     

        try { 
            con = adaptador.getConnection();
            //recuperar registros a devolver
            retorno.setLstRegistros(MeLanbide08JobDAO.getInstance().getLogJob(codOrganizacion, con, filtros, false, listaProcedimiento));
            //recuperar numero total de registros
            int totalRecords = MeLanbide08JobDAO.getInstance().getNumRegistrosLogJob(codOrganizacion, con, filtros, listaProcedimiento);
            retorno.setDraw(filtros.getDraw());
            retorno.setRecordsFiltered(totalRecords);
            retorno.setRecordsTotal(totalRecords);
        } catch (BDException ex){
            LOG.error("Se ha producido una excepcion en la BBDD recuperando los datos " + " Error : " + ex.getMensaje(), ex);
            throw ex;
        } catch(Exception e){
            LOG.error("Error al leer los datos " + " Error : " + e.getMessage());
            throw e;
        } finally {
            try{
                adaptador.devolverConexion(con);
            }catch (Exception e){
                LOG.error("Error al cerrar conexion a la BBDD; " + e.getMessage());
            }
        }
        LOG.info("getLog - End () " + formatDate.format(new Date()));
        return retorno;
    }
    
}

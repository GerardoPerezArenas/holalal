
package es.altia.flexia.integracion.moduloexterno.melanbide09.manager;
import es.altia.flexia.integracion.moduloexterno.melanbide09.dao.MeLanbide09DAO;
import es.altia.flexia.integracion.moduloexterno.melanbide09.vo.ComboVO;
import es.altia.flexia.integracion.moduloexterno.melanbide09.vo.FiltrosVO;
import es.altia.flexia.integracion.moduloexterno.melanbide09.vo.ResultadoConsultaVO;
import es.altia.flexia.integracion.moduloexterno.melanbide09.vo.RetornoResultadosFiltros;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.altia.util.conexion.BDException;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
/**
 *
 * @author alexandrep
 */
public class MeLanbide09Manager {
    
    private static final Logger LOG = LogManager.getLogger(MeLanbide09Manager.class);
    SimpleDateFormat formatDate = new SimpleDateFormat("yyyyMMddHHmmssSSS");

        //Instancia
    private static MeLanbide09Manager instance = null;

    public static MeLanbide09Manager getInstance() {
        if (instance == null) {
            synchronized (MeLanbide09Manager.class) {
                instance = new MeLanbide09Manager();
            }
        }
        return instance;
    }
    
  
    public List<ComboVO> getComboProcedimiento(int codOrganizacion,int codUsuario,AdaptadorSQLBD adaptador) throws Exception {
        LOG.info("getComboProcedimiento - Begin () " + formatDate.format(new Date()));
        Connection con = null;
        try {
            con=adaptador.getConnection();
            return MeLanbide09DAO.getInstance().getComboProcedimiento(codOrganizacion,codUsuario,con);
        } catch (Exception e) {
            LOG.error("getComboProcedimiento  " + codOrganizacion + " Error : " + e.getMessage(), e);
            throw e;
        } finally {
            try {
                adaptador.devolverConexion(con);
            } catch (Exception e) {
                LOG.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
            LOG.info("getComboProcedimiento - End ()" + formatDate.format(new Date()));
        }
    }

    
        public List<ComboVO> getComboTramites(int codOrganizacion,int codUsuario,String codProcedimiento, AdaptadorSQLBD adaptador) throws Exception {
        LOG.info("getComboTramites - Begin () " + formatDate.format(new Date()));
        Connection con = null;
        try {
            con=adaptador.getConnection();
            return MeLanbide09DAO.getInstance().getComboTramites(codOrganizacion,codUsuario,codProcedimiento,con);
        } catch (Exception e) {
            LOG.error("getComboTramites  " + codOrganizacion + " Error : " + e.getMessage(), e);
            throw e;
        } finally {
            try {
                adaptador.devolverConexion(con);
            } catch (Exception e) {
                LOG.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
            LOG.info("getComboTramites - End ()" + formatDate.format(new Date()));
        }
    }
        
        /**
     *
     * @param codOrganizacion
     * @param adaptador
     * @param filtros
     * @return
     * @throws Exception
     */
    public RetornoResultadosFiltros getLog(int codOrganizacion, AdaptadorSQLBD adaptador, FiltrosVO filtros,  List<ComboVO> listaProcedimiento) throws Exception {
        LOG.info("getLog - Begin () " + formatDate.format(new Date()));
        Connection con = null;
        RetornoResultadosFiltros retorno = new RetornoResultadosFiltros();     

        try { 
            con = adaptador.getConnection();
            //recuperar registros a devolver
            retorno.setLstRegistros(MeLanbide09DAO.getInstance().getExpedientesSinNotificar(codOrganizacion, con, filtros, false,listaProcedimiento));
            //recuperar numero total de registros
            int totalRecords = MeLanbide09DAO.getInstance().getNumRegistrosTotalExpedientesSinNotificar(codOrganizacion, con, filtros,listaProcedimiento);
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
    
    
     public RetornoResultadosFiltros getLogSinPaginar(int codOrganizacion, AdaptadorSQLBD adaptador, FiltrosVO filtros,  List<ComboVO> listaProcedimiento) throws Exception {
        LOG.info("getLog - Begin () " + formatDate.format(new Date()));
        Connection con = null;
        RetornoResultadosFiltros retorno = new RetornoResultadosFiltros();     

        try { 
            con = adaptador.getConnection();
            //recuperar registros a devolver
            retorno.setLstRegistros(MeLanbide09DAO.getInstance().getExpedientesSinNotificarSinPaginar(codOrganizacion, con, filtros, false,listaProcedimiento));
            //recuperar numero total de registros
            int totalRecords = MeLanbide09DAO.getInstance().getNumRegistrosTotalExpedientesSinNotificar(codOrganizacion, con, filtros,listaProcedimiento);
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
        
    public String cerrarTramite(int codOrganizacion, String numExp, String codTramite,String procedimiento, AdaptadorSQLBD adaptador) throws Exception {
        LOG.info("cerrarTramite - Begin () " + formatDate.format(new Date()));
        Connection con = null;
        String retorno = null;     

        try { 
            con = adaptador.getConnection();
           retorno =  MeLanbide09DAO.getInstance().cerrarTramite(codOrganizacion, numExp, codTramite, procedimiento, con);
        } catch (BDException ex){
            LOG.error("Se ha producido una excepcion en la BBDD cerrando el tramite " + " Error : " + ex.getMensaje(), ex);
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
        LOG.info("cerrarTramite - End () " + formatDate.format(new Date()));
        return retorno;
    }   
    
    
    
    public String abrirTramite(int codOrganizacion, String numExp, String codTramite,String procedimiento, String uor, Integer usuario, AdaptadorSQLBD adaptador) throws Exception {
        LOG.info("abrirTramite - Begin () " + formatDate.format(new Date()));
        Connection con = null;
        String retorno = null;     

        try { 
            con = adaptador.getConnection();
           retorno =  MeLanbide09DAO.getInstance().abrirTramite(codOrganizacion, numExp, codTramite, procedimiento, uor, usuario, con);
        } catch (BDException ex){
            LOG.error("Se ha producido una excepcion en la BBDD abriendo el tramite " + " Error : " + ex.getMensaje(), ex);
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
        LOG.info("cerrarTramite - End () " + formatDate.format(new Date()));
        return retorno;
    }   
    
    
  public List<ResultadoConsultaVO> getTramitesSalida(int codOrganizacion, AdaptadorSQLBD adaptador, String procedimiento, int codigoTramite ) throws Exception {
   LOG.info("getTramitesSalida - Begin () " + formatDate.format(new Date()));
        Connection con = null;
        List<ResultadoConsultaVO> retorno = null;     

        try { 
            con = adaptador.getConnection();
           retorno =  MeLanbide09DAO.getInstance().getTramitesSalida(codOrganizacion, con, procedimiento, codigoTramite);
        } catch (BDException ex){
            LOG.error("Se ha producido una excepcion en la BBDD obteniendo los tramites " + " Error : " + ex.getMensaje(), ex);
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
        LOG.info("getTramitesSalida - End () " + formatDate.format(new Date()));
        return retorno;
    }   
    
}

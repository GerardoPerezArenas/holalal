/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package es.altia.flexia.integracion.moduloexterno.melanbide54.manager;

import es.altia.agora.business.sge.persistence.manual.FichaExpedienteDAO;
import es.altia.agora.business.util.GeneralValueObject;
import es.altia.common.exception.TechnicalException;
import es.altia.flexia.integracion.moduloexterno.melanbide54.dao.MeLanbide54DAO;
import es.altia.flexia.integracion.moduloexterno.melanbide54.vo.CentroVO;
import es.altia.flexia.integracion.moduloexterno.melanbide54.vo.MunicipioVO;
import es.altia.flexia.integracion.moduloexterno.melanbide54.vo.RegistroAACCVO;
import es.altia.flexia.integracion.moduloexterno.melanbide54.vo.RegistroErrorVO;
import es.altia.flexia.integracion.moduloexterno.melanbide54.vo.SelectItem;
import es.altia.webservice.client.tramitacion.ws.wto.ExpedienteVO;
import es.altia.webservice.client.tramitacion.ws.wto.InteresadoExpedienteVO;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.altia.util.conexion.BDException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

/**
 *
 * @author davidg
 */
public class MeLanbide54Manager {
    
    //Logger
    private static Logger log = LogManager.getLogger(MeLanbide54Manager.class);
    
    //Instancia
    private static MeLanbide54Manager instance = null;
    
    public static MeLanbide54Manager getInstance()
    {
        if(instance == null)
        {
            synchronized(MeLanbide54Manager.class)
            {
                instance = new MeLanbide54Manager();
            }
        }
        return instance;
    }
    
    public List<CentroVO> recogeCentros(String numExp, AdaptadorSQLBD adaptador)
    {
        List<CentroVO> centros = new ArrayList<CentroVO>();
        Connection con = null;
        try
        {
             if(adaptador != null)
                log.error(" adapt es dif de null -- antes de crear conexion" + adaptador.toString());
            con = adaptador.getConnection();
            centros = MeLanbide54DAO.getInstance().recogeCentros(numExp, con);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido una excepcion en recogeCentros: "+ ex);
            //throw new Exception(ex);
        }
        finally
        {
            try
            {
                adaptador.devolverConexion(con);       
            }
            catch(Exception e)
            {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
        return centros;
    }
    
    public CentroVO getCentroPorCodigo(String numExpediente, String id, AdaptadorSQLBD adaptador) throws Exception
    {
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            MeLanbide54DAO meLanbide54DAO = MeLanbide54DAO.getInstance();
            return meLanbide54DAO.getCentroPorCodigo(numExpediente, id, con);
        }
        catch(BDException e)
        {
            log.error("Se ha producido una excepcion en la BBDD recuperando Datos sobre un centro por codigo: " + numExpediente + " - " + id, e);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido una excepcion en la BBDD recuperando sobre un centro por codigo:" + numExpediente + " - " + id, ex);
            throw new Exception(ex);
        }
        finally
        {
            try
            {
                adaptador.devolverConexion(con);       
            }
            catch(Exception e)
            {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }
    
    public List<SelectItem> recogeMunicipios(String th, AdaptadorSQLBD adaptador)
    {
        List<SelectItem> lista = new ArrayList<SelectItem>();
        Connection con = null;
        try
        {
             if(adaptador != null)
                log.error(" adapt es dif de null -- antes de crear conexion" + adaptador.toString());
            con = adaptador.getConnection();
            lista = MeLanbide54DAO.getInstance().recogeMunicipios(th, con);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido una excepcion en recogeMunicipios: "+ ex);
            //throw new Exception(ex);
        }
        finally
        {
            try
            {
                adaptador.devolverConexion(con);       
            }
            catch(Exception e)
            {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
        return lista;
    }
    
    public boolean crearNuevCentro(CentroVO centro, String numExp, AdaptadorSQLBD adaptador)throws Exception
    {
        Connection con = null;
        boolean insertOK;
        try
        {
            con = adaptador.getConnection();
            MeLanbide54DAO meLanbide54DAO = MeLanbide54DAO.getInstance();
            insertOK = meLanbide54DAO.crearNuevoCentro(centro, numExp, con);
        }
        catch(BDException e)
        {
            log.error("Se ha producido un Excepcion en la BBDD creando  un centro : " + e.getMessage(), e);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido una excepcion en la BBDD creando centro : " + ex.getMessage(), ex);
            throw new Exception(ex);
        }
        finally
        {
            try
            {
                adaptador.devolverConexion(con);       
            }
            catch(Exception e)
            {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
        return insertOK;
    }
    
    public boolean modificandoCentro(CentroVO centro, String numExp, AdaptadorSQLBD adaptador)throws Exception
    {
        Connection con = null;
        boolean insertOK;
        try
        {
            con = adaptador.getConnection();
            MeLanbide54DAO meLanbide54DAO = MeLanbide54DAO.getInstance();
            insertOK = meLanbide54DAO.actualizaCentro(centro, numExp, con);
        }
        catch(BDException e)
        {
            log.error("Se ha producido un Excepcion en la BBDD modificando  un centro : " + e.getMessage(), e);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido una excepcion en la BBDD modificando centro : " + ex.getMessage(), ex);
            throw new Exception(ex);
        }
        finally
        {
            try
            {
                adaptador.devolverConexion(con);       
            }
            catch(Exception e)
            {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
        return insertOK;
    }
    
    public boolean eliminandoandoCentro(String id, String numExp, AdaptadorSQLBD adaptador)throws Exception
    {
        Connection con = null;
        boolean insertOK;
        try
        {
            con = adaptador.getConnection();
            MeLanbide54DAO meLanbide54DAO = MeLanbide54DAO.getInstance();
            insertOK = meLanbide54DAO.eliminaCentro(id, numExp, con);
        }
        catch(BDException e)
        {
            log.error("Se ha producido un Excepcion en la BBDD eliminando  un centro : " + e.getMessage(), e);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido una excepcion en la BBDD eliminando centro : " + ex.getMessage(), ex);
            throw new Exception(ex);
        }
        finally
        {
            try
            {
                adaptador.devolverConexion(con);       
            }
            catch(Exception e)
            {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
        return insertOK;
    }
    
    public InteresadoExpedienteVO getDatosInteresado(int codOrganizacion, int ejercicio, RegistroAACCVO registro, Connection con) throws Exception {
            try
            {
                MeLanbide54DAO meLanbide54DAO = MeLanbide54DAO.getInstance();
                return MeLanbide54DAO.getDatosInteresado(codOrganizacion,ejercicio,registro, con);
            }
            catch(Exception ex)
            {
                log.error("Se ha producido un error recuperando los datos del tercero del registro "+registro.getResEje()+"/"+registro.getResNum(), ex);
                throw new Exception(ex);
            }
    }    

    public Vector cargaEstructuraDatosSuplementariosProcedimiento(GeneralValueObject gVO, String[] params) {
        log.error("cargaEstructuraDatosSuplementariosProcedimiento");
        Vector lista = new Vector();

        try{
            lista = FichaExpedienteDAO.getInstance().cargaEstructuraDatosSuplementariosProcedimiento(gVO, params);
        }catch (TechnicalException te) {
            log.error("JDBC Technical problem");
        }catch (Exception e) {
            e.printStackTrace();
        }finally{
            return lista;
        }   
  }        
        
}

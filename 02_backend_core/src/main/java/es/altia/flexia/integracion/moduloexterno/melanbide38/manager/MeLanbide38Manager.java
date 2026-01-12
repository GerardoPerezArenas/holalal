/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.melanbide38.manager;

import es.altia.flexia.integracion.moduloexterno.melanbide38.dao.MeLanbide38DAO;
import es.altia.flexia.integracion.moduloexterno.melanbide38.vo.ContratoVO;
import es.altia.flexia.integracion.moduloexterno.melanbide38.vo.FilaContratoVO;
import es.altia.flexia.integracion.moduloexterno.melanbide38.vo.Tercero;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.altia.util.conexion.BDException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

/**
 *
 * @author santiagoc
 */
public class MeLanbide38Manager 
{
    
    //Logger
    private static Logger log = LogManager.getLogger(MeLanbide38Manager.class);
    
    //Instancia
    private static MeLanbide38Manager instance = null;    
    
    private MeLanbide38Manager()
    {
        
    }
    
    public static MeLanbide38Manager getInstance()
    {
        if(instance == null)
        {
            synchronized(MeLanbide38Manager.class)
            {
                instance = new MeLanbide38Manager();
            }
        }
        return instance;
    }    
    
    public List<FilaContratoVO> getContratosExpediente(String numExp, AdaptadorSQLBD adaptador) throws BDException, Exception
    {
        if(log.isDebugEnabled()) 
            log.debug("getContratosExpediente( numExpediente = " + numExp + " ) : BEGIN");
        List<FilaContratoVO> retList = new ArrayList<FilaContratoVO>();
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            MeLanbide38DAO meLanbide30DAO = MeLanbide38DAO.getInstance();
            retList = meLanbide30DAO.getContratosExpediente(numExp, con);
        }
        catch(BDException e)
        {
            log.error("Se ha producido una excepción en la BBDD recuperando los contratos para el expediente " + numExp, e);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido una excepción en la BBDD recuperando los contratos para el expediente " + numExp, ex);
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
                log.error("Error al cerrar conexión a la BBDD: " + e.getMessage());
            }
        }
        if(log.isDebugEnabled()) 
            log.debug("getContratosExpediente() : END");
        return retList;
    }
    
    public List<Tercero> busquedaTerceros(Tercero tercero, AdaptadorSQLBD adaptador) throws Exception
    {
        Connection con = null;
        List<Tercero> retList = new ArrayList<Tercero>();
        try
        {
            con = adaptador.getConnection();
            MeLanbide38DAO meLanbide38DAO = MeLanbide38DAO.getInstance();
            retList =  meLanbide38DAO.busquedaTerceros(tercero, con);
        }
        catch(BDException e)
        {
            throw new Exception(e);
        }catch(Exception ex)
        {
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
                log.error("Error al cerrar conexión a la BBDD: " + e.getMessage());
            }
        }
        return retList;
    }
    
    public int getNuevoNumContrato(String numExpediente, AdaptadorSQLBD adaptador) throws Exception
    {
        
        Connection con = null;
        int retValue = -1;
        try
        {
            con = adaptador.getConnection();
            MeLanbide38DAO meLanbide38DAO = MeLanbide38DAO.getInstance();
            retValue =  meLanbide38DAO.getNuevoNumContrato(numExpediente, con);
        }
        catch(BDException e)
        {
            throw new Exception(e);
        }catch(Exception ex)
        {
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
                log.error("Error al cerrar conexión a la BBDD: " + e.getMessage());
            }
        }
        return retValue;
    }
    
    public boolean crearContrato(ContratoVO contrato, AdaptadorSQLBD adaptador) throws BDException, Exception
    {
        Connection con = null;
        boolean ret = false;
        try
        {
            con = adaptador.getConnection();
            MeLanbide38DAO meLanbide38DAO = MeLanbide38DAO.getInstance();
            ret =  meLanbide38DAO.crearContrato(contrato, con);
        }
        catch(BDException e)
        {
            throw new Exception(e);
        }catch(Exception ex)
        {
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
                log.error("Error al cerrar conexión a la BBDD: " + e.getMessage());
            }
        }
        return ret;
    }
    
    public ContratoVO getContratoExpedientePorNumContrato(String numExpediente, Integer numContrato, AdaptadorSQLBD adaptador) throws BDException, Exception
    {
        Connection con = null;
        ContratoVO contrato = null;
        try
        {
            con = adaptador.getConnection();
            contrato = MeLanbide38DAO.getInstance().getContratoExpedientePorNumContrato(numExpediente, numContrato, con);
        }
        catch(BDException e)
        {
            throw new Exception(e);
        }
        catch(Exception ex)
        {
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
                log.error("Error al cerrar conexión a la BBDD: " + e.getMessage());
            }
        }
        return contrato;
    }
    
    public boolean modificarContrato(ContratoVO contrato, AdaptadorSQLBD adaptador) throws BDException, Exception
    {
        Connection con = null;
        boolean ret = false;
        try
        {
            con = adaptador.getConnection();
            MeLanbide38DAO meLanbide38DAO = MeLanbide38DAO.getInstance();
            ret =  meLanbide38DAO.modificarContrato(contrato, con);
        }
        catch(BDException e)
        {
            throw new Exception(e);
        }catch(Exception ex)
        {
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
                log.error("Error al cerrar conexión a la BBDD: " + e.getMessage());
            }
        }
        return ret;
    }
    
    public boolean eliminarContrato(String numExp, Integer numContrato, AdaptadorSQLBD adaptador) throws BDException, Exception
    {
        if(log.isDebugEnabled()) 
            log.debug("eliminarContrato( numExpediente = " + numExp + " numContrato = " + numContrato + ") : BEGIN");
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            MeLanbide38DAO meLanbide38DAO = MeLanbide38DAO.getInstance();
            boolean retValue = meLanbide38DAO.eliminarContrato(numExp, numContrato, con);            
            if(log.isDebugEnabled()) 
                log.debug("eliminarContrato() : END");
            return retValue;
        }
        catch(BDException e)
        {
            log.error("Se ha producido una excepción en la BBDD al eliminar el contrato " + numContrato + "para el expediente "+numExp, e);
            throw new Exception(e);
        }catch(Exception ex)
        {
            log.error("Se ha producido una excepción en la BBDD al eliminar el contrato " + numContrato + "para el expediente "+numExp, ex);
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
                log.error("Error al cerrar conexión a la BBDD: " + e.getMessage());
            }
        }
    }
}

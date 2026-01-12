/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.melanbide30.manager;

import es.altia.flexia.integracion.moduloexterno.melanbide30.dao.MeLanbide30DAO;
import es.altia.flexia.integracion.moduloexterno.melanbide30.vo.ContratoSociosVO;
import es.altia.flexia.integracion.moduloexterno.melanbide30.vo.FilaContratoSociosVO;
import es.altia.flexia.integracion.moduloexterno.melanbide30.vo.Tercero;
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
public class MeLanbide30Manager 
{
    
    //Logger
    private static Logger log = LogManager.getLogger(MeLanbide30Manager.class);
    
    //Instancia
    private static MeLanbide30Manager instance = null;    
    
    private MeLanbide30Manager()
    {
        
    }
    
    public static MeLanbide30Manager getInstance()
    {
        if(log.isDebugEnabled()) log.debug("getInstance() : BEGIN");
        if(instance == null)
        {
            synchronized(MeLanbide30Manager.class)
            {
                instance = new MeLanbide30Manager();
            }
        }
        if(log.isDebugEnabled()) log.debug("getInstance() : END");
        return instance;
    }
    
    public List<FilaContratoSociosVO> getContratosExpediente(String numExp, AdaptadorSQLBD adaptador) throws BDException, Exception
    {
        if(log.isDebugEnabled()) 
            log.debug("getContratosExpediente( numExpediente = " + numExp + " ) : BEGIN");
        List<FilaContratoSociosVO> retList = new ArrayList<FilaContratoSociosVO>();
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            MeLanbide30DAO meLanbide30DAO = MeLanbide30DAO.getInstance();
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
            MeLanbide30DAO meLanbide29DAO = MeLanbide30DAO.getInstance();
            retList =  meLanbide29DAO.busquedaTerceros(tercero, con);
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
            MeLanbide30DAO meLanbide30DAO = MeLanbide30DAO.getInstance();
            retValue =  meLanbide30DAO.getNuevoNumContrato(numExpediente, con);
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
    
    public boolean crearContrato(ContratoSociosVO contrato, AdaptadorSQLBD adaptador) throws BDException, Exception
    {
        Connection con = null;
        boolean ret = false;
        try
        {
            con = adaptador.getConnection();
            MeLanbide30DAO meLanbide30DAO = MeLanbide30DAO.getInstance();
            ret =  meLanbide30DAO.crearContrato(contrato, con);
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
    
    public ContratoSociosVO getContratoExpedientePorNumContrato(String numExpediente, Integer numContrato, AdaptadorSQLBD adaptador) throws BDException, Exception
    {
        Connection con = null;
        ContratoSociosVO contrato = null;
        try
        {
            con = adaptador.getConnection();
            contrato = MeLanbide30DAO.getInstance().getContratoExpedientePorNumContrato(numExpediente, numContrato, con);
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
    
    public boolean modificarContrato(ContratoSociosVO contrato, AdaptadorSQLBD adaptador) throws BDException, Exception
    {
        Connection con = null;
        boolean ret = false;
        try
        {
            con = adaptador.getConnection();
            MeLanbide30DAO meLanbide30DAO = MeLanbide30DAO.getInstance();
            ret =  meLanbide30DAO.modificarContrato(contrato, con);
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
            MeLanbide30DAO meLanbide30DAO = MeLanbide30DAO.getInstance();
            boolean retValue = meLanbide30DAO.eliminarContrato(numExp, numContrato, con);            
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

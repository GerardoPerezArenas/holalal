/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.melanbide29.manager;


import es.altia.flexia.integracion.moduloexterno.melanbide29.dao.MeLanbide29DAO;
import es.altia.flexia.integracion.moduloexterno.melanbide29.vo.ContratoRenovacionPlantillaVO;
import es.altia.flexia.integracion.moduloexterno.melanbide29.vo.FilaContratoRenovacionPlantillaVO;
import es.altia.flexia.integracion.moduloexterno.melanbide29.vo.FilaDocumentoContableVO;
import es.altia.flexia.integracion.moduloexterno.melanbide29.vo.Tercero;
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
public class MeLanbide29Manager 
{
    
    //Logger
    private static Logger log = LogManager.getLogger(MeLanbide29Manager.class);
    
    //Instancia
    private static MeLanbide29Manager instance = null;
    
    private MeLanbide29Manager()
    {
        
    }
    
    public static MeLanbide29Manager getInstance()
    {
        if(log.isDebugEnabled()) log.debug("getInstance() : BEGIN");
        if(instance == null)
        {
            synchronized(MeLanbide29Manager.class)
            {
                instance = new MeLanbide29Manager();
            }
        }
        if(log.isDebugEnabled()) log.debug("getInstance() : END");
        return instance;
    }
    
    public List<FilaContratoRenovacionPlantillaVO> getContratosExpediente(String numExp, AdaptadorSQLBD adaptador) throws BDException, Exception
    {
        if(log.isDebugEnabled()) 
            log.debug("getContratosExpediente( numExpediente = " + numExp + " ) : BEGIN");
        List<FilaContratoRenovacionPlantillaVO> retList = new ArrayList<FilaContratoRenovacionPlantillaVO>();
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            MeLanbide29DAO meLanbide29DAO = MeLanbide29DAO.getInstance();
            retList = meLanbide29DAO.getContratosExpediente(numExp, con);
        }
        catch(BDException e)
        {
            log.error("Se ha producido una excepción en la BBDD recuperando los contratos de renovación para el expediente " + numExp, e);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido una excepción en la BBDD recuperando los contratos de renovación para el expediente " + numExp, ex);
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
    
    public ContratoRenovacionPlantillaVO getContratoExpedientePorNumContrato(String numExp, Integer numContrato, AdaptadorSQLBD adaptador) throws BDException, Exception
    {
        if(log.isDebugEnabled()) 
            log.debug("getContratoExpedientePorNumContrato( numExpediente = " + numExp + " numContrato = " + numContrato + ") : BEGIN");
        ContratoRenovacionPlantillaVO contrato = null;
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            MeLanbide29DAO meLanbide29DAO = MeLanbide29DAO.getInstance();
            contrato = meLanbide29DAO.getContratoExpedientePorNumContrato(numExp, numContrato, con);
        }
        catch(BDException e)
        {
            log.error("Se ha producido una excepción en la BBDD recuperando los contratos de renovación para el expediente " + numExp, e);
            throw new Exception(e);
        }catch(Exception ex)
        {
            log.error("Se ha producido una excepción en la BBDD recuperando los contratos de renovación para el expediente " + numExp, ex);
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
            log.debug("getContratoExpedientePorNumContrato() : END");
        return contrato;
    }
    
    public boolean guardarContrato(ContratoRenovacionPlantillaVO contrato, AdaptadorSQLBD adaptador) throws BDException, Exception
    {
        Connection con = null;
        boolean ret = false;
        try
        {
            con = adaptador.getConnection();
            MeLanbide29DAO meLanbide29DAO = MeLanbide29DAO.getInstance();
            ret =  meLanbide29DAO.guardarContrato(contrato, con);
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
    
    public List<Tercero> busquedaTerceros(Tercero tercero, AdaptadorSQLBD adaptador) throws Exception
    {
        Connection con = null;
        List<Tercero> retList = new ArrayList<Tercero>();
        try
        {
            con = adaptador.getConnection();
            MeLanbide29DAO meLanbide29DAO = MeLanbide29DAO.getInstance();
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
            MeLanbide29DAO meLanbide29DAO = MeLanbide29DAO.getInstance();
            retValue =  meLanbide29DAO.getNuevoNumContrato(numExpediente, con);
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
    
    public boolean crearContrato(ContratoRenovacionPlantillaVO contrato, AdaptadorSQLBD adaptador) throws BDException, Exception
    {
        Connection con = null;
        boolean ret = false;
        try
        {
            con = adaptador.getConnection();
            MeLanbide29DAO meLanbide29DAO = MeLanbide29DAO.getInstance();
            ret =  meLanbide29DAO.crearContrato(contrato, con);
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
            MeLanbide29DAO meLanbide29DAO = MeLanbide29DAO.getInstance();
            boolean retValue = meLanbide29DAO.eliminarContrato(numExp, numContrato, con);            
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
    
    public List<FilaDocumentoContableVO> getListaDocumentosContablesExpediente(String numExp, AdaptadorSQLBD adaptador) throws BDException, Exception
    {
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            MeLanbide29DAO meLanbide29DAO = MeLanbide29DAO.getInstance();
            return meLanbide29DAO.getListaDocumentosContablesExpediente(numExp, con);      
        }
        catch(BDException e)
        {
            log.error("Se ha producido una excepción en la BBDD al recuperar el listado de documentos contables para el expediente "+numExp, e);
            throw new Exception(e);
        }catch(Exception ex)
        {
            log.error("Se ha producido una excepción en la BBDD al recuperar el listado de documentos contables para el expediente "+numExp, ex);
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

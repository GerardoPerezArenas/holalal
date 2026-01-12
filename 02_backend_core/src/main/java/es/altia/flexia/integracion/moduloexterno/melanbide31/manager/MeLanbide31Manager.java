/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.melanbide31.manager;

import es.altia.flexia.integracion.moduloexterno.melanbide31.dao.MeLanbide31DAO;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.altia.util.conexion.BDException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.util.Date;
import java.util.List;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

/**
 *
 * @author santiagoc
 */
public class MeLanbide31Manager {
    //Logger
    private static Logger log = LogManager.getLogger(MeLanbide31Manager.class);
    
    //Instancia
    private static MeLanbide31Manager instance = null;
    
    private MeLanbide31Manager()
    {
        
    }
    
    public static MeLanbide31Manager getInstance()
    {
        if(instance == null)
        {
            synchronized(MeLanbide31Manager.class)
            {
                instance = new MeLanbide31Manager();
            }
        }
        return instance;
    }
    
    public BigDecimal getValorCampoNumerico(int codOrganizacion, String numExp, String ejercicio, String codigoCampo, AdaptadorSQLBD adaptador)throws Exception
    {
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            MeLanbide31DAO meLanbide31DAO = MeLanbide31DAO.getInstance();
            return meLanbide31DAO.getValorCampoNumerico(codOrganizacion, numExp, ejercicio, codigoCampo, con);
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
    }
    
    public String getValorCampoTexto(int codOrganizacion, String numExp, String ejercicio, String codigoCampo, AdaptadorSQLBD adaptador)throws Exception
    {
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            MeLanbide31DAO meLanbide31DAO = MeLanbide31DAO.getInstance();
            return meLanbide31DAO.getValorCampoTexto(codOrganizacion, numExp, ejercicio, codigoCampo, con);
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
    }
    
    public Date getValorCampoFecha(int codOrganizacion, String numExp, String ejercicio, String codigoCampo, AdaptadorSQLBD adaptador)throws Exception
    {
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            MeLanbide31DAO meLanbide31DAO = MeLanbide31DAO.getInstance();
            return meLanbide31DAO.getValorCampoFecha(codOrganizacion, numExp, ejercicio, codigoCampo, con);
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
    }
    
    public String getValorCampoTextoLargo(int codOrganizacion, String numExp, String ejercicio, String codigoCampo, AdaptadorSQLBD adaptador)throws Exception
    {
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            MeLanbide31DAO meLanbide31DAO = MeLanbide31DAO.getInstance();
            return meLanbide31DAO.getValorCampoTextoLargo(codOrganizacion, numExp, ejercicio, codigoCampo, con);
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
    }
    
    public String getValorCampoDesplegable(int codOrganizacion, String numExp, String ejercicio, String codigoCampo, AdaptadorSQLBD adaptador)throws Exception
    {
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            MeLanbide31DAO meLanbide31DAO = MeLanbide31DAO.getInstance();
            return meLanbide31DAO.getValorCampoDesplegable(codOrganizacion, numExp, ejercicio, codigoCampo, con);
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
    }
    
    public String getProvinciaTercero(int codOrganizacion, String numExp, String ejercicio, AdaptadorSQLBD adaptador)throws Exception
    {
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            MeLanbide31DAO meLanbide31DAO = MeLanbide31DAO.getInstance();
            return meLanbide31DAO.getProvinciaTercero(codOrganizacion, numExp, ejercicio, con);
        }
        catch(BDException e)
        {
            log.error("Se ha producido una excepción en la BBDD recuperando la provincia para el expediente " + numExp, e);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido una excepción en la BBDD recuperando la provincia para el expediente " + numExp, ex);
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
    
    public String getCodPostalTercero(int codOrganizacion, String numExp, String ejercicio, AdaptadorSQLBD adaptador)throws Exception
    {
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            MeLanbide31DAO meLanbide31DAO = MeLanbide31DAO.getInstance();
            return meLanbide31DAO.getCodPostalTercero(codOrganizacion, numExp, ejercicio, con);
        }
        catch(BDException e)
        {
            log.error("Se ha producido una excepción en la BBDD recuperando el codigo postal para el expediente " + numExp, e);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido una excepción en la BBDD recuperando el codigo postal para el expediente " + numExp, ex);
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
    
    public String getDescripcionProvincia(String codigoProv, AdaptadorSQLBD adaptador)throws Exception
    {
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            MeLanbide31DAO meLanbide31DAO = MeLanbide31DAO.getInstance();
            return meLanbide31DAO.getDescripcionProvincia(codigoProv, con);
        }
        catch(BDException e)
        {
            log.error("Se ha producido una excepción en la BBDD recuperando la descripcion de la provincia " + codigoProv, e);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido una excepción en la BBDD recuperando la descripcion de la provincia " + codigoProv, ex);
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
    
    public List<String> getValoresCumplimentados(int codOrganizacion, String numExp, String ejercicio, AdaptadorSQLBD adaptador) throws Exception
    {
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            MeLanbide31DAO meLanbide31DAO = MeLanbide31DAO.getInstance();
            return meLanbide31DAO.getValoresCumplimentados(codOrganizacion, numExp, ejercicio, con);
        }
        catch(BDException e)
        {
            log.error("Se ha producido una excepción en la BBDD recuperando los datos cumplimentados del expediente " + numExp, e);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido una excepción en la BBDD recuperando los datos cumplimentados del expediente " + numExp, ex);
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
    
    public String getDescripcionCampo(int codOrganizacion, String codigoCampo, AdaptadorSQLBD adaptador) throws Exception
    {
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            MeLanbide31DAO meLanbide31DAO = MeLanbide31DAO.getInstance();
            return meLanbide31DAO.getDescripcionCampo(codOrganizacion, codigoCampo, con);
        }
        catch(BDException e)
        {
            log.error("Se ha producido una excepción en la BBDD recuperando la descripcion del campo " + codigoCampo, e);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido una excepción en la BBDD recuperando la descripcion del campo " + codigoCampo, ex);
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

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.melanbide56.manager;

import es.altia.flexia.integracion.moduloexterno.melanbide56.dao.MeLanbide56DAO;
import es.altia.flexia.integracion.moduloexterno.melanbide56.vo.CampoSuplementario;
import es.altia.flexia.integracion.moduloexterno.melanbide56.vo.Expediente;
import es.altia.flexia.integracion.moduloexterno.melanbide56.vo.Tercero;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.altia.util.conexion.BDException;
import java.sql.Connection;
import java.util.List;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

/**
 *
 * @author santiagoc
 */
public class MeLanbide56Manager
{
    //Logger
    private static final Logger log = LogManager.getLogger(MeLanbide56Manager.class);    
    
    //Instancia
    private static MeLanbide56Manager instance = null;
    
    private MeLanbide56Manager()
    {
        
    }
    
    public static MeLanbide56Manager getInstance()
    {
        if(instance == null)
        {
            synchronized(MeLanbide56Manager.class)
            {
                instance = new MeLanbide56Manager();
            }
        }
        return instance;
    }        
    
    public Expediente getDatosExpediente(int codMunicipio, String numExp, AdaptadorSQLBD adaptador) throws Exception
    {
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            MeLanbide56DAO meLanbide56DAO = MeLanbide56DAO.getInstance();
            return meLanbide56DAO.getDatosExpediente(codMunicipio, numExp, con);
        }
        catch(BDException e)
        {
            log.error("Se ha producido un error recuperando la lista de terceros para el expediente "+numExp, e);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error recuperando datos del expediente "+numExp, ex);
            throw new Exception(ex);
        }
        finally
        {
            try
            {
                adaptador.devolverConexion(con);       
            }
            catch(BDException e)
            {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }        
    
    public List<Tercero> getTercerosExpediente(int codMunicipio, Integer ejercicio, String numExp, AdaptadorSQLBD adaptador) throws Exception
    {
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            MeLanbide56DAO meLanbide56DAO = MeLanbide56DAO.getInstance();
            return meLanbide56DAO.getTercerosExpediente(codMunicipio, ejercicio, numExp, con);
        }
        catch(BDException e)
        {
            log.error("Se ha producido un error recuperando la lista de terceros para el expediente "+numExp, e);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error recuperando la lista de terceros para el expediente "+numExp, ex);
            throw new Exception(ex);
        }
        finally
        {
            try
            {
                adaptador.devolverConexion(con);       
            }
            catch(BDException e)
            {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }
 
    public boolean grabarCampoSuplementarioTramite(int codOrganizacion, String codProcedimiento, Integer ejercicio, String numExp, int codTramite, int ocurrenciaTramite, String codCampo, Object valor, int tipoDato, AdaptadorSQLBD adaptador) throws Exception
    {
        
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            MeLanbide56DAO meLanbide56DAO = MeLanbide56DAO.getInstance();
            return meLanbide56DAO.grabarCampoSuplementarioTramite(codOrganizacion, codProcedimiento, ejercicio, numExp, codTramite, ocurrenciaTramite, codCampo, valor, tipoDato, con);
        }
        catch(BDException e)
        {
            log.error("Se ha producido un error grabando campo suplementario "+codCampo+" para tramite "+codTramite+" y expediente "+numExp, e);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error grabando campo suplementario "+codCampo+" para tramite "+codTramite+" y expediente "+numExp, ex);
            throw new Exception(ex);
        }
        finally
        {
            try
            {
                adaptador.devolverConexion(con);       
            }
            catch(BDException e)
            {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }
 
    public boolean grabarCamposSuplementariosTramite(int codOrganizacion, String codProcedimiento, Integer ejercicio, String numExp, int codTramite, int ocurrenciaTramite, List<CampoSuplementario> camposSuplementarios, AdaptadorSQLBD adaptador) throws Exception
    {
        
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            adaptador.inicioTransaccion(con);
            MeLanbide56DAO meLanbide56DAO = MeLanbide56DAO.getInstance();
            boolean result = meLanbide56DAO.grabarCamposSuplementariosTramite(codOrganizacion, codProcedimiento, ejercicio, numExp, codTramite, ocurrenciaTramite, camposSuplementarios, con);
            if(result == true)
                adaptador.finTransaccion(con);
            else
                adaptador.rollBack(con);
            return result;
        }
        catch(BDException e)
        {
            log.error("Se ha producido un error grabando campos suplementarios para tramite "+codTramite+" y expediente "+numExp, e);
            adaptador.rollBack(con);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error grabando campos suplementarios para tramite "+codTramite+" y expediente "+numExp, ex);
            adaptador.rollBack(con);
            throw new Exception(ex);
        }
        finally
        {
            try
            {
                adaptador.devolverConexion(con);       
            }
            catch(BDException e)
            {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }
}

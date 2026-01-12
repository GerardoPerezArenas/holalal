/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.melanbide36.manager;

import es.altia.flexia.integracion.moduloexterno.melanbide36.dao.MeLanbide36DAO;
import es.altia.flexia.integracion.moduloexterno.melanbide36.vo.FilaExpedienteVO;
import es.altia.flexia.integracion.moduloexterno.melanbide36.vo.InfoContactoVO;        
import es.altia.flexia.integracion.moduloexterno.melanbide36.vo.TerceroVO;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.altia.util.conexion.BDException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

/**
 *
 * @author santiagoc
 */
public class MeLanbide36Manager 
{
    //Logger
    private static Logger log = LogManager.getLogger(MeLanbide36Manager.class);
    
    //Instancia
    private static MeLanbide36Manager instance = null;
    
    private MeLanbide36Manager()
    {
        
    }
    
    public static MeLanbide36Manager getInstance()
    {
        if(instance == null)
        {
            synchronized(MeLanbide36Manager.class)
            {
                instance = new MeLanbide36Manager();
            }
        }
        return instance;
    }        
    
    public List<FilaExpedienteVO> getListaExpedientesRelacionados(String numExp, AdaptadorSQLBD adaptador) throws Exception
    {
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            MeLanbide36DAO meLanbide36DAO = MeLanbide36DAO.getInstance();
            return meLanbide36DAO.getListaExpedientesRelacionados(numExp, con);
        }
        catch(BDException e)
        {
            log.error("Se ha producido una excepciÃ³n en la BBDD recuperando la lista de expedientes relacionados con "+numExp, e);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido una excepciÃ³n en la BBDD recuperando la lista de expedientes relacionados con "+numExp, ex);
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
                log.error("Error al cerrar conexiÃ³n a la BBDD: " + e.getMessage());
            }
        }
    }
    
    // --- metodos para recibir los NIF de rol de persona sustituida y rol de persona contratada (si los hay) ---- Rosa 2018/01/16 -----------

    public String getNIFSustituida(int codOrganizacion, String numExp, Connection con) throws BDException, Exception
    {
        InfoContactoVO infContact = new InfoContactoVO();
        infContact.setCodOrganizacion(codOrganizacion);
        infContact.setNumExpediente(numExp);
        try
        {
            MeLanbide36DAO meLanbide36DAO = MeLanbide36DAO.getInstance();
            return meLanbide36DAO.getNIFSustituida(infContact, con).getCodTercero();
        }
        catch(BDException e)
        {
            log.error("Se ha producido una excepciÃ³n en la BBDD recuperando el NIF de persona sustituida del expediente "+numExp, e);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido una excepciÃ³n recuperando el NIF de persona sustituida del expediente "+numExp, ex);
            throw new Exception(ex);
        }
    }
 
    public String getNIFContratada(int codOrganizacion, String numExp, Connection con) throws BDException, Exception
    {
        InfoContactoVO infContact = new InfoContactoVO();
        infContact.setCodOrganizacion(codOrganizacion);
        infContact.setNumExpediente(numExp);
        try
        {
            MeLanbide36DAO meLanbide36DAO = MeLanbide36DAO.getInstance();
            return meLanbide36DAO.getNIFContratada(infContact, con).getCodTercero();
        }
        catch(BDException e)
        {
            log.error("Se ha producido una excepciÃ³n en la BBDD recuperando el NIF de persona contratada del expediente "+numExp, e);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido una excepciÃ³n recuperando el NIF de persona contratada del expediente "+numExp, ex);
            throw new Exception(ex);
        }
    }
    
     // --- metodos para comprobar si un tercero existe ---- Rosa 2018/01/16 -----------
    public TerceroVO existeTercero(TerceroVO tercero, Connection con) throws BDException, Exception
    {
        try
        {
            MeLanbide36DAO meLanbide36DAO = MeLanbide36DAO.getInstance();
            return meLanbide36DAO.existeTercero(tercero, con);
        }
        catch(BDException e)
        {
            log.error("Se ha producido una excepciÃ³n en la BBDD recuperando el Tercero del NIF de persona sustituida "+ tercero.getCodDocumento(), e);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido una excepciÃ³n recuperando el el Tercero del NIF de persona sustituida "+ tercero.getCodDocumento(), ex);
            throw new Exception(ex);
        }
    }
    
    // --- graba el NIF de la persona sustituida ---- Rosa 2018/01/16 -----------
    public int grabaNIFSust(int codOrganizacion, String numExp, TerceroVO tercero, Connection con) throws BDException, Exception
    {
        int grabaSust = 0;
        InfoContactoVO infContact = new InfoContactoVO();
        infContact.setCodOrganizacion(codOrganizacion);
        infContact.setNumExpediente(numExp);
        MeLanbide36DAO meLanbide36DAO = MeLanbide36DAO.getInstance();
        try
        {
            grabaSust = meLanbide36DAO.grabaNIFSust(codOrganizacion, numExp, tercero, con).getNumGraba();
            //----------- grabar datos suplementarios del tercero sustituido -------------
            infContact.setCodTercero(tercero.getCodTercero());
            infContact = meLanbide36DAO.getAnteriorExpedienteSustituida(infContact, con);
            meLanbide36DAO.copiarDatosSustituidoTNU(infContact, con);
            meLanbide36DAO.copiarDatosSustituidoTXT(infContact, con);
            meLanbide36DAO.copiarDatosSustituidoTFE(infContact, con);
            meLanbide36DAO.copiarDatosSustituidoTDE(infContact, con);
            return grabaSust;
        }
        catch(BDException e)
        {
            log.error("Se ha producido una excepciÃ³n en la BBDD grabando el NIF de persona sustituida del expediente " + tercero.getCodDocumento(), e);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido una excepciÃ³n grabando el NIF de persona sustituida del expediente " + tercero.getCodDocumento(), ex);
            throw new Exception(ex);
        }
    }
    
    // --- graba el NIF de la persona contratada ---- Rosa 2018/01/16 -----------
    public int grabaNIFContr(int codOrganizacion, String numExp, TerceroVO tercero, Connection con) throws BDException, Exception
    {
        int grabaContr = 0;
        InfoContactoVO infContact = new InfoContactoVO();
        infContact.setCodOrganizacion(codOrganizacion);
        infContact.setNumExpediente(numExp);
        MeLanbide36DAO meLanbide36DAO = MeLanbide36DAO.getInstance();
        try
        {
            grabaContr = meLanbide36DAO.grabaNIFContr(codOrganizacion, numExp, tercero, con).getNumGraba();
            //----------- grabar datos suplementarios del tercero contratado -------------
            infContact.setCodTercero(tercero.getCodTercero());
            infContact = meLanbide36DAO.getAnteriorExpedienteContratada(infContact, con);
            meLanbide36DAO.copiarDatosContratadoTNU(infContact, con);
            meLanbide36DAO.copiarDatosContratadoTFE(infContact, con);
            meLanbide36DAO.copiarDatosContratadoTDE(infContact, con);
            return grabaContr;
        }
        catch(BDException e)
        {
            log.error("Se ha producido una excepciÃ³n en la BBDD grabando el NIF de persona contratada del expediente " + tercero.getCodDocumento(), e);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido una excepciÃ³n grabando el NIF de persona contratada del expediente " + tercero.getCodDocumento(), ex);
            throw new Exception(ex);
        }
    }
    
    /**
     *
     * @param codOrganizacion
     * @param numExp
     * @param adaptador
     * @throws BDException
     * @throws SQLException
     * @throws Exception
     */
    public void getDatosContacto(int codOrganizacion, String numExp, AdaptadorSQLBD adaptador) throws BDException, SQLException, Exception 
    {
        Connection con = null;
        InfoContactoVO infContact = new InfoContactoVO();
        infContact.setCodOrganizacion(codOrganizacion);
        infContact.setNumExpediente(numExp);
        MeLanbide36DAO meLanbide36DAO = MeLanbide36DAO.getInstance();
      
        try {
            con = adaptador.getConnection();
            infContact = meLanbide36DAO.getEmpresaExpediente(infContact, con);
            infContact = meLanbide36DAO.getAnteriorExpediente(infContact, con);
            meLanbide36DAO.copiarDatosContactoTXT(infContact, con);
            meLanbide36DAO.copiarDatosContactoTNU(infContact, con);
            meLanbide36DAO.copiarDatosContactoDES(infContact, con);
            adaptador.finTransaccion(con);  
        } catch(BDException e) {
            log.error("Se ha producido un error al obtener la conexi�n a la BBDD", e);
            throw e;
        } catch(SQLException ex) {
            log.error("Se ha producido un error al recuperar informaci�n de contacto", ex);
            throw ex;
        } finally {
            try {
                adaptador.devolverConexion(con);       
            } catch(Exception e) {
                log.error("Error al cerrar la conexi�n a la BBDD: " + e.getMessage());
            }
        }
    }
    
      // --- metodos para validar que un NIF existe y obtener los datos que necesitamos grabar en el expediente en los roles persona sustituida y persona contratada (si los hay) ---- Rosa 2018/01/16 -----------  
}

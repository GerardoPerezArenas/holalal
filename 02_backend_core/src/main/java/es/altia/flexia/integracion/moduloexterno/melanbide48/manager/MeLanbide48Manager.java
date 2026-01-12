/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.melanbide48.manager;

import es.altia.flexia.integracion.moduloexterno.melanbide48.dao.ColecAmbitosBloquesHorasDAO;
import es.altia.flexia.integracion.moduloexterno.melanbide48.dao.ColecColectivoDAO;
import es.altia.flexia.integracion.moduloexterno.melanbide48.dao.ColecMunicipioDAO;
import es.altia.flexia.integracion.moduloexterno.melanbide48.dao.ColecTrayeEntiValidaDAO;
import es.altia.flexia.integracion.moduloexterno.melanbide48.dao.ColecUbicCTValoracionDAO;
import es.altia.flexia.integracion.moduloexterno.melanbide48.dao.MeLanbide48DAO;
import es.altia.flexia.integracion.moduloexterno.melanbide48.dao.MeLanbideConvocatoriasDAO;
import es.altia.flexia.integracion.moduloexterno.melanbide48.i18n.MeLanbide48I18n;
import es.altia.flexia.integracion.moduloexterno.melanbide48.util.ConfigurationParameter;
import es.altia.flexia.integracion.moduloexterno.melanbide48.util.ConstantesMeLanbide48;
import es.altia.flexia.integracion.moduloexterno.melanbide48.util.MeLanbide48MappingUtils;
import es.altia.flexia.integracion.moduloexterno.melanbide48.util.MeLanbide48Utils;
import es.altia.flexia.integracion.moduloexterno.melanbide48.vo.ColecAmbitosBloquesHoras;
import es.altia.flexia.integracion.moduloexterno.melanbide48.vo.ColecCentrosVO;
import es.altia.flexia.integracion.moduloexterno.melanbide48.vo.ColecColectivo;
import es.altia.flexia.integracion.moduloexterno.melanbide48.vo.ColecComproRealizacionVO;
import es.altia.flexia.integracion.moduloexterno.melanbide48.vo.ColecEntidadEnAgrupacionVO;
import es.altia.flexia.integracion.moduloexterno.melanbide48.vo.ColecEntidadVO;
import es.altia.flexia.integracion.moduloexterno.melanbide48.vo.ColecMunVO;
import es.altia.flexia.integracion.moduloexterno.melanbide48.vo.ColecProcesoAdjudicacionRespuestaVO;
import es.altia.flexia.integracion.moduloexterno.melanbide48.vo.ColecProgConvActPredefinidaColectivo;
import es.altia.flexia.integracion.moduloexterno.melanbide48.vo.ColecRepresentanteVO;
import es.altia.flexia.integracion.moduloexterno.melanbide48.vo.ColecSolicitudVO;
import es.altia.flexia.integracion.moduloexterno.melanbide48.vo.ColecTrayActividadVO;
import es.altia.flexia.integracion.moduloexterno.melanbide48.vo.ColecTrayEspVO;
import es.altia.flexia.integracion.moduloexterno.melanbide48.vo.ColecTrayOtroProgramaVO;
import es.altia.flexia.integracion.moduloexterno.melanbide48.vo.ColecTrayVO;
import es.altia.flexia.integracion.moduloexterno.melanbide48.vo.ColecTrayeEntiValida;
import es.altia.flexia.integracion.moduloexterno.melanbide48.vo.ColecTrayectoriaEntidad;
import es.altia.flexia.integracion.moduloexterno.melanbide48.vo.ColecUbicCTValoracion;
import es.altia.flexia.integracion.moduloexterno.melanbide48.vo.ColecUbicacionesCTVO;
import es.altia.flexia.integracion.moduloexterno.melanbide48.vo.FilaBusquedaMunicipioVO;
import es.altia.flexia.integracion.moduloexterno.melanbide48.vo.FilaColecCentrosVO;
import es.altia.flexia.integracion.moduloexterno.melanbide48.vo.FilaEntidadVO;
import es.altia.flexia.integracion.moduloexterno.melanbide48.vo.FilaTrayEspVO;
import es.altia.flexia.integracion.moduloexterno.melanbide48.vo.MeLanbideConvocatorias;
import es.altia.flexia.integracion.moduloexterno.melanbide48.vo.SelectItem;
import es.altia.flexia.integracion.moduloexterno.plugin.ModuloIntegracionExternoCampoSupFactoria;
import es.altia.flexia.integracion.moduloexterno.plugin.camposuplementario.IModuloIntegracionExternoCamposFlexia;
import es.altia.flexia.integracion.moduloexterno.plugin.persistence.vo.CampoSuplementarioModuloIntegracionVO;
import es.altia.flexia.integracion.moduloexterno.plugin.persistence.vo.SalidaIntegracionVO;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.altia.util.conexion.BDException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

/**
 *
 * @author santiagoc
 */
public class MeLanbide48Manager 
{
    //Logger
    private static final Logger log = LogManager.getLogger(MeLanbide48Manager.class);
    // Formateador de fecha para trzas en logs dureacion de metodos, etc
    SimpleDateFormat formatFechaLog = new SimpleDateFormat("yyyyMMddHHmmssSSS");
    SimpleDateFormat formatFechaddMMyyy = new SimpleDateFormat("dd/MM/yyyy");

    //Instancia
    private static MeLanbide48Manager instance = null;
    private final MeLanbideConvocatoriasDAO meLanbideConvocatoriasDAO = new MeLanbideConvocatoriasDAO();
    private final MeLanbide48DAO meLanbide48DAO = MeLanbide48DAO.getInstance();
    private final ColecMunicipioDAO colecMunicipioDAO = new ColecMunicipioDAO();
    private final ColecUbicCTValoracionDAO colecUbicCTValoracionDAO = new ColecUbicCTValoracionDAO();
    private final ColecTrayeEntiValidaDAO colecTrayeEntiValidaDAO = new ColecTrayeEntiValidaDAO();
    private final ColecColectivoDAO colecColectivoDAO = new ColecColectivoDAO();
    private final ColecAmbitosBloquesHorasDAO colecAmbitosBloquesHorasDAO = new ColecAmbitosBloquesHorasDAO();

    private final MELanbide48ManagerColecEntidadCertCalidad meLanbide48ManagerColecEntidadCertCalidad = new MELanbide48ManagerColecEntidadCertCalidad();
    
    private MeLanbide48Manager(){}
    
    public static MeLanbide48Manager getInstance()
    {
        if(instance == null)
        {
            synchronized(MeLanbide48Manager.class)
            {
                instance = new MeLanbide48Manager();
            }
        }
        return instance;
    }      
    
        public List<SelectItem> getTiposDocumento(AdaptadorSQLBD adaptador) throws Exception
    {
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            
            return meLanbide48DAO.getTiposDocumento(con);
        }
        catch(BDException e)
        {
            log.error("Se ha producido un error recuperando la lista tipos de documento", e);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error recuperando la lista tipos de documento", ex);
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
    
    public BigDecimal getValorCampoNumerico(int codOrganizacion, String ejercicio, String numExpediente, String codCampo, AdaptadorSQLBD adaptador) throws Exception
    {
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            
            return meLanbide48DAO.getValorCampoNumerico(codOrganizacion, numExpediente, ejercicio, codCampo, con);
        }
        catch(BDException e)
        {
            log.error("Se ha producido un error recuperando el campo suplementario numerico "+codCampo+" para el expediente "+numExpediente, e);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error recuperando el campo suplementario numerico "+codCampo+" para el expediente "+numExpediente, ex);
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
    
    public int guardarValorCampoNumerico(int codOrganizacion, String ejercicio, String numExpediente, String codCampo, BigDecimal valor, AdaptadorSQLBD adaptador) throws Exception
    {
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            
            return meLanbide48DAO.guardarValorCampoNumerico(codOrganizacion, numExpediente, ejercicio, codCampo, valor, con);
        }
        catch(BDException e)
        {
            log.error("Se ha producido un error grabando el campo suplementario numerico "+codCampo+" para el expediente "+numExpediente, e);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error grabando el campo suplementario numerico "+codCampo+" para el expediente "+numExpediente, ex);
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
    
    public String getValorCampoTexto(int codOrganizacion, String ejercicio, String numExpediente, String codCampo, AdaptadorSQLBD adaptador) throws Exception
    {
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            
            return meLanbide48DAO.getValorCampoTexto(codOrganizacion, numExpediente, ejercicio, codCampo, con);
        }
        catch(BDException e)
        {
            log.error("Se ha producido un error recuperando el campo suplementario texto "+codCampo+" para el expediente "+numExpediente, e);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error recuperando el campo suplementario texto "+codCampo+" para el expediente "+numExpediente, ex);
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
    
    public int guardarValorCampoTexto(int codOrganizacion, String ejercicio, String numExpediente, String codCampo, String valor, AdaptadorSQLBD adaptador) throws Exception
    {
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            
            return meLanbide48DAO.guardarValorCampoTexto(codOrganizacion, numExpediente, ejercicio, codCampo, valor, con);
        }
        catch(BDException e)
        {
            log.error("Se ha producido un error grabando el campo suplementario texto "+codCampo+" para el expediente "+numExpediente, e);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error grabando el campo suplementario texto "+codCampo+" para el expediente "+numExpediente, ex);
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
    
    public String getValorCampoDesplegable(int codOrganizacion, String ejercicio, String numExpediente, String codCampo, AdaptadorSQLBD adaptador) throws Exception
    {
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            
            return meLanbide48DAO.getValorCampoDesplegable(codOrganizacion, numExpediente, ejercicio, codCampo, con);
        }
        catch(BDException e)
        {
            log.error("Se ha producido un error recuperando el campo suplementario desplegable "+codCampo+" para el expediente "+numExpediente, e);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error recuperando el campo suplementario desplegable "+codCampo+" para el expediente "+numExpediente, ex);
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
    
    public int guardarValorCampoDesplegable(int codOrganizacion, String ejercicio, String numExpediente, String codCampo, String valor, AdaptadorSQLBD adaptador) throws Exception
    {
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            
            return meLanbide48DAO.guardarValorCampoDesplegable(codOrganizacion, numExpediente, ejercicio, codCampo, valor, con);
        }
        catch(BDException e)
        {
            log.error("Se ha producido un error grabando el campo suplementario desplegable "+codCampo+" para el expediente "+numExpediente, e);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error grabando el campo suplementario desplegable "+codCampo+" para el expediente "+numExpediente, ex);
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
    
    public void guardarDatosColec(String ejercicio, ColecEntidadVO entidad, AdaptadorSQLBD adaptador) throws Exception
    {
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            adaptador.inicioTransaccion(con);
            entidad = meLanbide48DAO.guardarColecEntidadVO(entidad, con);
            //representante = meLanbide48DAO.guardarColecRepresentanteVO(representante, con);
            if(entidad != null ) //&& representante != null
            {
                adaptador.finTransaccion(con);
            }
            else
            {
                throw new BDException();
            }
        }
        catch(BDException e)
        {
            log.error("Se ha producido una excepcion en la BBDD guardando datos Colec Entidad "+(entidad != null ? entidad.getCif(): "(Entidad = null)")+" para el expediente "+(entidad != null ? entidad.getNumExp() : "(Entidad = null)"), e);
            adaptador.rollBack(con);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido una excepcion en la BBDD guardando datos Colec Entidad "+(entidad != null ? entidad.getCif(): "(Entidad = null)")+" para el expediente "+(entidad != null ? entidad.getNumExp() : "(Entidad = null)"), ex);
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
                adaptador.rollBack(con);
            }
        }
    }
    
    public List<SelectItem> getListaPaises(AdaptadorSQLBD adaptador) throws Exception
    {
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            
            return meLanbide48DAO.getListaPaises(con);
        }
        catch(BDException e)
        {
            log.error("Se ha producido un error recuperando la lista de paises", e);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error recuperando la lista de paises", ex);
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
    
    public ColecEntidadVO getEntidadPorCodigoYExpediente(ColecEntidadVO p, int idioma,AdaptadorSQLBD adaptador) throws Exception
    {
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            
            return meLanbide48DAO.getEntidadPorCodigoYExpediente(p,idioma, con);
        }
        catch(BDException e)
        {
            log.error("Se ha producido un error recuperando entidad "+(p != null ? p.getCodEntidad(): "(entidad = null)")+" para expediente "+(p != null ? p.getNumExp() : "(entidad = null)"), e);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error recuperando entidad "+(p != null ? p.getCodEntidad(): "(entidad = null)")+" para expediente "+(p != null ? p.getNumExp() : "(entidad = null)"), ex);
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
    
    public ColecEntidadVO getEntidadInteresadaPorExpediente(ColecEntidadVO p, AdaptadorSQLBD adaptador) throws Exception
    {
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            
            return meLanbide48DAO.getEntidadInteresadaPorExpediente(p, con);
        }
        catch(BDException e)
        {
            log.error("Se ha producido un error recuperando entidad "+(p != null ? p.getCodEntidad(): "(entidad = null)")+" para expediente "+(p != null ? p.getNumExp() : "(entidad = null)"), e);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error recuperando entidad "+(p != null ? p.getCodEntidad(): "(entidad = null)")+" para expediente "+(p != null ? p.getNumExp() : "(entidad = null)"), ex);
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
    
    public ColecRepresentanteVO getRepresentantePorExpediente(ColecRepresentanteVO p, AdaptadorSQLBD adaptador) throws Exception
    {
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            
            return meLanbide48DAO.getRepresentantePorExpediente(p, con);
        }
        catch(BDException e)
        {
            log.error("Se ha producido un error recuperando representante "+(p != null ? p.getCodRepresentante(): "(representante = null)")+" para expediente "+(p != null ? p.getNumExp() : "(representante = null)"), e);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error recuperando representante "+(p != null ? p.getCodRepresentante(): "(representante = null)")+" para expediente "+(p != null ? p.getNumExp() : "(representante = null)"), ex);
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
    
    public ColecEntidadVO guardarColecEntidadVO(int codOrganizacion, ColecEntidadVO entidad, AdaptadorSQLBD adaptador) throws Exception
    {
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            adaptador.inicioTransaccion(con);
            
            entidad = meLanbide48DAO.guardarColecEntidadVO(entidad, con);
            if(entidad != null)
            {
                adaptador.finTransaccion(con);
                return entidad;
            }
            else
            {
                throw new BDException();
            }
        }
        catch(BDException e)
        {
            log.error("Se ha producido una excepcion en la BBDD guardando entidad "+(entidad != null ? entidad.getCodEntidad(): "(entidad = null)")+" para el expediente "+(entidad != null ? entidad.getNumExp() : "(entidad = null)"), e);
            adaptador.rollBack(con);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido una excepcion en la BBDD guardando entidad "+(entidad != null ? entidad.getCodEntidad(): "(entidad = null)")+" para el expediente "+(entidad != null ? entidad.getNumExp() : "(entidad = null)"), ex);
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
                adaptador.rollBack(con);
            }
        }
    }
    
    public ColecEntidadEnAgrupacionVO guardarColecEntidadEnAgrupacionVO(int codOrganizacion, ColecEntidadEnAgrupacionVO entidad, AdaptadorSQLBD adaptador) throws Exception
    {
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            adaptador.inicioTransaccion(con);
            
            entidad = meLanbide48DAO.guardarColecEntidadEnAgrupacionVO(entidad, con);
            if(entidad != null)
            {
                adaptador.finTransaccion(con);
                return entidad;
            }
            else
            {
                throw new BDException();
            }
        }
        catch(BDException e)
        {
            log.error("Se ha producido una excepcion en la BBDD guardando entidad "+(entidad != null ? entidad.getCodEntidad()+"/Entidadpadre:" +entidad.getCodEntidadPadreAgrup():"(entidad = null)")+" para el expediente "+(entidad != null ? entidad.getNumExp() : "(entidad = null)"), e);
            adaptador.rollBack(con);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido una excepcion en la BBDD guardando entidad "+(entidad != null ? entidad.getCodEntidad()+"/Entidadpadre:" + entidad.getCodEntidadPadreAgrup():"(entidad = null)")+" para el expediente "+(entidad != null ? entidad.getNumExp() : "(entidad = null)"), ex);
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
                adaptador.rollBack(con);
            }
        }
    }
    
    public List<ColecEntidadEnAgrupacionVO> getListaEntidadesPorExpediente(String numExpediente, AdaptadorSQLBD adaptador) throws Exception
    {
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            
            return meLanbide48DAO.getListaEntidadesPorExpediente(numExpediente, con);
        }
        catch(BDException e)
        {
            log.error("Se ha producido un error recuperando la lista de entidades para expediente "+numExpediente, e);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error recuperando la lista de entidades para expediente "+numExpediente, ex);
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
    public List<ColecEntidadEnAgrupacionVO> getListaEntidadesEnAgrupacion(ColecEntidadVO entidad, AdaptadorSQLBD adaptador) throws Exception
    {
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            
            return meLanbide48DAO.getListaEntidadesEnAgrupacion(entidad, con);
        }
        catch(BDException e)
        {
            log.error("Se ha producido un error recuperando la lista de entidades para expediente "+(entidad!=null && entidad.getNumExp()!=null?entidad.getNumExp():""), e);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error recuperando la lista de entidades para expediente "+(entidad!=null && entidad.getNumExp()!=null?entidad.getNumExp():""), ex);
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
    
    public int eliminarColecEntidadVO(int codOrganizacion, ColecEntidadVO entidad, AdaptadorSQLBD adaptador) throws Exception
    {
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            adaptador.inicioTransaccion(con);
            
            if(entidad != null)
            {
                int eliminados = meLanbide48DAO.eliminarColecEntidad(entidad, con);
                if(eliminados > 0)
                {
                    adaptador.finTransaccion(con);   
                    return eliminados;
                }
                else
                {
                    throw new BDException();
                }
            }
            else
            {
                throw new BDException();
            }
        }
        catch(BDException e)
        {
            adaptador.rollBack(con);
            log.error("Se ha producido una excepcion en la BBDD eliminando entidad "+(entidad != null ? entidad.getCodEntidad(): "(entidad = null)")+" para el expediente "+(entidad != null ? entidad.getNumExp() : "(entidad = null)"), e);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            adaptador.rollBack(con);
            log.error("Se ha producido una excepcion en la BBDD eliminando entidad "+(entidad != null ? entidad.getCodEntidad(): "(entidad = null)")+" para el expediente "+(entidad != null ? entidad.getNumExp() : "(entidad = null)"), ex);
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
                adaptador.rollBack(con);
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }
    
    public int eliminarColecEntidadEnAgrupacionVO(int codOrganizacion, ColecEntidadEnAgrupacionVO entidad, AdaptadorSQLBD adaptador) throws Exception
    {
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            adaptador.inicioTransaccion(con);
            
            if(entidad != null)
            {
                int eliminados = meLanbide48DAO.eliminarColecEntidadEnAgrupacionVO(entidad, con);
                if(eliminados > 0)
                {
                    adaptador.finTransaccion(con);   
                    return eliminados;
                }
                else
                {
                    throw new BDException();
                }
            }
            else
            {
                throw new BDException();
            }
        }
        catch(BDException e)
        {
            adaptador.rollBack(con);
            log.error("Se ha producido una excepcion en la BBDD eliminando entidad Asociada/Padre : "+(entidad != null ? entidad.getCodEntidad() +"/"+entidad.getCodEntidadPadreAgrup(): "(entidad = null)")+" para el expediente "+(entidad != null ? entidad.getNumExp() : "(entidad = null)"), e);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            adaptador.rollBack(con);
            log.error("Se ha producido una excepcion en la BBDD eliminando entidad "+(entidad != null ? entidad.getCodEntidad(): "(entidad = null)")+" para el expediente "+(entidad != null ? entidad.getNumExp() : "(entidad = null)"), ex);
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
                adaptador.rollBack(con);
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }
    
    public HashMap<String, String> getDatosTercero(int codOrganizacion, String numExp, String ejercicio, String rol, AdaptadorSQLBD adaptador)throws Exception
    {
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            
            return meLanbide48DAO.getDatosTercero(codOrganizacion, numExp, ejercicio, rol, con);
        }
        catch(BDException e)
        {
            log.error("Se ha producido una excepcion en la BBDD recuperando datos de tercero para expediente "+numExp, e);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido una excepcion en la BBDD recuperando datos de tercero para expediente "+numExp, ex);
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
                log.error("Error al cerrar conexiÃƒÂ³n a la BBDD: " + e.getMessage());
            }
        }
    }
    
    public Integer getNumMeses( String fini, String ffin, AdaptadorSQLBD adaptador) throws Exception
    {
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            
            return meLanbide48DAO.obtenerNumMeses(fini, ffin, con);
        }
        catch(BDException e)
        {
            log.error("Se ha producido un error recuperando el numero de meses", e);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error recuperando el numero de meses ", ex);
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
    
    public String getDescripcionCampoDesplegable(String codigoCampo, String valorDesplegable, AdaptadorSQLBD adaptador) throws Exception
    {
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            
            return meLanbide48DAO.getDescripcionCampoDesplegable(codigoCampo, valorDesplegable, con);
        }
        catch(BDException e)
        {
            log.error("Se ha producido un error recuperando descripcion de campo desplegable con codigoCampo =  "+codigoCampo+" y valorDesplegable = "+valorDesplegable, e);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error recuperando descripcion de campo desplegable con codigoCampo =  "+codigoCampo+" y valorDesplegable = "+valorDesplegable, ex);
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
    
    public Long getTramiteActualExpediente(int codOrganizacion, int ejercicio, String numExp, AdaptadorSQLBD adaptador)throws Exception
    {
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            
            return meLanbide48DAO.getTramiteActualExpediente(codOrganizacion, ejercicio, numExp, con);
        }
        catch(BDException e)
        {
            log.error("Se ha producido una excepcion en la BBDD recuperando tramite actual del expediente " + numExp, e);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido una excepcion en la BBDD recuperando tramite actual del expediente " + numExp, ex);
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
    
    public Long obtenerCodigoInternoTramite(Integer codOrganizacion, String codTramite, AdaptadorSQLBD adaptador)throws Exception
    {
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            
            return meLanbide48DAO.obtenerCodigoInternoTramite(codOrganizacion, codTramite, con);
        }
        catch(BDException e)
        {
            log.error("Se ha producido una excepcion en la BBDD recuperando codigo interno del tramite " + codTramite, e);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido una excepcion en la BBDD recuperando codigo interno del tramite " + codTramite, ex);
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
    
    public boolean tieneTramiteIniciado(int codOrganizacion, int ejercicio, String numExp, Long codTramite, AdaptadorSQLBD adaptador) throws Exception
    {
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            
            return meLanbide48DAO.tieneTramiteIniciado(codOrganizacion, ejercicio, numExp, codTramite, con);
        }
        catch(BDException e)
        {
            log.error("Se ha producido una excepcion en la BBDD comprobando si tiene tramite iniciado "+codTramite, e);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido una excepcion en la BBDD comprobando si tiene tramite iniciado "+codTramite, ex);
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
    
    public boolean tieneTramiteFinalizado(int codOrganizacion, int ejercicio, String numExp, Long codTramite, AdaptadorSQLBD adaptador) throws Exception
    {
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            return meLanbide48DAO.tieneTramiteFinalizado(codOrganizacion, ejercicio, numExp, codTramite, con);
        }
        catch(BDException e)
        {
            log.error("Se ha producido una excepcion en la BBDD comprobando si tiene tramite finalizado "+codTramite, e);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido una excepcion en la BBDD comprobando si tiene tramite finalizado "+codTramite, ex);
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
    
    public List<ColecSolicitudVO> getSolicitudPorExpediente(ColecSolicitudVO s, Integer idioma, Integer idBDConvocaoriaExpte, AdaptadorSQLBD adaptador) throws Exception
    {
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            return meLanbide48DAO.getSolicitudPorExpediente(s,idioma,idBDConvocaoriaExpte,con);
        }
        catch(BDException e)
        {
            log.error("Se ha producido un error recuperando solicitud para expediente "+(s != null ? s.getNumExp() : "(solicitud = null)"), e);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error recuperando solicitud para expediente "+(s != null ? s.getNumExp() : "(solicitud = null)"), ex);
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
    
    public ColecSolicitudVO getSolicitudPorId(Integer s, AdaptadorSQLBD adaptador) throws Exception
    {
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            
            return meLanbide48DAO.getSolicitudPorId(s, con);
        }
        catch(BDException e)
        {
            log.error("Se ha producido un error recuperando solicitud por ID "+(s != null ? s : "(id = null)"), e);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error recuperando solicitud para ID "+(s != null ? s : "(id = null)"), ex);
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
    
    public ColecSolicitudVO guardarColecSolicitudVO(int codOrganizacion, ColecSolicitudVO solicitud, AdaptadorSQLBD adaptador) throws Exception
    {
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            adaptador.inicioTransaccion(con);
            
            solicitud = meLanbide48DAO.guardarColecSolicitudVO(solicitud, con);
            if(solicitud != null)
            {
                adaptador.finTransaccion(con);
                return solicitud;
            }
            else
            {
                throw new BDException();
            }
        }
        catch(BDException e)
        {
            log.error("Se ha producido una excepcion en la BBDD guardando solicitud "+(solicitud != null ? solicitud.getCodSolicitud(): "(entidad = null)")+" para el expediente "+(solicitud != null ? solicitud.getNumExp() : "(solicitud = null)"), e);
            adaptador.rollBack(con);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido una excepcion en la BBDD guardando solicitud "+(solicitud != null ? solicitud.getCodSolicitud(): "(entidad = null)")+" para el expediente "+(solicitud != null ? solicitud.getNumExp() : "(solicitud = null)"), ex);
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
                adaptador.rollBack(con);
            }
        }
    }
    
    public List<SelectItem> getListaColectivos(int codIdioma, AdaptadorSQLBD adaptador) throws Exception
    {
        List<SelectItem> retList = new ArrayList<SelectItem>();
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            retList=colecColectivoDAO.getListaColectivos(codIdioma, con);
            // Convocatoria 2021 - Leemos de BD
            /*
            SelectItem si = null;
            //Colectivo 1
            si = new SelectItem();
            si.setCodigo("1");
            si.setDescripcion(MeLanbide48I18n.getInstance().getMensaje(codIdioma, "combo.colectivos.colectivo1"));
            retList.add(si);
            
            //Colectivo 2
            si = new SelectItem();
            si.setCodigo("2");
            si.setDescripcion(MeLanbide48I18n.getInstance().getMensaje(codIdioma, "combo.colectivos.colectivo2"));
            retList.add(si);
            
            //Colectivo 3
            si = new SelectItem();
            si.setCodigo("3");
            si.setDescripcion(MeLanbide48I18n.getInstance().getMensaje(codIdioma, "combo.colectivos.colectivo3"));
            retList.add(si);
            
            //Colectivo 4
            si = new SelectItem();
            si.setCodigo("4");
            si.setDescripcion(MeLanbide48I18n.getInstance().getMensaje(codIdioma, "combo.colectivos.colectivo4"));
            retList.add(si);
            */
        }
        catch(Exception ex)
        {
            log.error("Error recuperando la lista de colectivo para un desplegabla: " + ex.getMessage(), ex);
        }finally{
            try {
                adaptador.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
        return retList;
    }
    
    public List<SelectItem> getListaProvincias(AdaptadorSQLBD adaptador) throws Exception
    {
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            return meLanbide48DAO.getListaProvincias(con);
        }
        catch(BDException e)
        {
            log.error("Se ha producido una excepcion en la BBDD recuperando provincias.");
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido una excepcion en la BBDD recuperando provincias.");
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
                adaptador.rollBack(con);
            }
        }
    }
    
    public List<SelectItem> getListaComarcas(AdaptadorSQLBD adaptador) throws Exception
    {
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            return meLanbide48DAO.getListaComarcas(con);
        }
        catch(BDException e)
        {
            log.error("Se ha producido una excepcion en la BBDD recuperando comarcas.");
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido una excepcion en la BBDD recuperando comarcas.");
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
                adaptador.rollBack(con);
            }
        }
    }
    
    public List<SelectItem> getComarcasPorAmbito(Integer codProvincia, AdaptadorSQLBD adaptador) throws Exception
    {
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            return meLanbide48DAO.getComarcasPorAmbito(codProvincia, con);
        }
        catch(BDException e)
        {
            log.error("Se ha producido una excepcion en la BBDD recuperando comarcas para provincia "+(codProvincia != null ? codProvincia : " (codProvincia = null)"), e);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido una excepcion en la BBDD recuperando comarcas para provincia "+(codProvincia != null ? codProvincia : " (codProvincia = null)"), ex);
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
    
    public List<SelectItem> getMunicipiosPorComarca(Long codComarca, AdaptadorSQLBD adaptador) throws Exception
    {
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            return colecMunicipioDAO.getMunicipiosPorComarca(codComarca, con);
        }
        catch(BDException e)
        {
            log.error("Se ha producido una excepcion en la BBDD recuperando municipios para comarca "+(codComarca != null ? codComarca : " (codComarca = null)"), e);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido una excepcion en la BBDD recuperando municipios para comarca "+(codComarca != null ? codComarca : " (codComarca = null)"), ex);
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
    public List<SelectItem> getMunicipiosPorConvocatoriaColectivoComarca(Integer idFkConvocatoria,Integer codColectivo,Integer codComarca, Integer idioma, AdaptadorSQLBD adaptador) throws Exception
    {
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            return colecMunicipioDAO.getMunicipiosPorConvocatoriaColectivoComarca(idFkConvocatoria,codColectivo,codComarca,idioma, con);
        }
        catch(BDException e)
        {
            log.error("Se ha producido una excepcion en la BBDD recuperando municipios para comarca "+(codComarca != null ? codComarca : " (codComarca = null)"), e);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido una excepcion en la BBDD recuperando municipios para comarca "+(codComarca != null ? codComarca : " (codComarca = null)"), ex);
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
    public List<SelectItem> getListaMunicipios(AdaptadorSQLBD adaptador) throws Exception
    {
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            return colecMunicipioDAO.getListaMunicipios(con);
        }
        catch(BDException e)
        {
            log.error("Se ha producido una excepcion en la BBDD recuperando municipios", e);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido una excepcion en la BBDD recuperando municipios ", ex);
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
                adaptador.rollBack(con);
            }
        } 
    }
    
    public List<FilaBusquedaMunicipioVO> buscarMunicipios(List<String> codComarcas, AdaptadorSQLBD adaptador) throws Exception
    {
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            return colecMunicipioDAO.buscarMunicipios(codComarcas, con);
        }
        catch(BDException e)
        {
            log.error("Se ha producido una excepcion en la BBDD recuperando municipios para comarcas "+(codComarcas != null ? codComarcas.toArray(new String[]{}) : " (codComarcas = null)"), e);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido una excepcion en la BBDD recuperando municipios para comarcas "+(codComarcas != null ? codComarcas.toArray(new String[]{}) : " (codComarcas = null)"), ex);
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
                adaptador.rollBack(con);
            }
        } 
    }
    
    public List<String> getCodigosComarcaPorAmbito(String codAmbito, AdaptadorSQLBD adaptador) throws Exception
    {
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            return meLanbide48DAO.getCodigosComarcaPorAmbito(codAmbito, con);
        }
        catch(BDException e)
        {
            log.error("Se ha producido una excepcion en la BBDD recuperando codigos de comarca para ambito "+(codAmbito != null ? codAmbito : " (codAmbito = null)"), e);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido una excepcion en la BBDD recuperando codigos de comarca para provincia "+(codAmbito != null ? codAmbito : " (codAmbito = null)"), ex);
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
                adaptador.rollBack(con);
            }
        } 
    }
    
    public boolean guardarCentros(int codIdioma, List<ColecCentrosVO> centros, List<ColecCentrosVO> centrosEliminar, AdaptadorSQLBD adaptador) throws Exception
    {
        Connection con = null;
        try
        {
            boolean error = false;
            con = adaptador.getConnection();
            adaptador.inicioTransaccion(con);
            if(centrosEliminar != null && centrosEliminar.size() > 0)
            {
                try
                {
                    int result = meLanbide48DAO.eliminarCentros(centrosEliminar, con);
                    if(result != centrosEliminar.size())
                    {
                        throw new BDException(MeLanbide48I18n.getInstance().getMensaje(codIdioma, "error.centros.errorEliminarCentros"));
                    }
                }
                catch(Exception ex)
                {
                    throw new BDException(MeLanbide48I18n.getInstance().getMensaje(codIdioma, "error.centros.errorEliminarCentros"));
                }
            }
            
            if(centros != null && centros.size() > 0)
            {
                try
                {
                    int pos = 0;
                    ColecCentrosVO centro = null;
                    while(pos < centros.size() && !error)
                    {
                        centro = centros.get(pos);
                        centro = meLanbide48DAO.guardarCentro(centro, con);
                        if(centro == null)
                        {
                            error = true;
                        }
                        pos++;
                    }
                    if(error)
                    {
                        throw new BDException(MeLanbide48I18n.getInstance().getMensaje(codIdioma, "error.centros.errorGuardarCentros"));
                    }
                }
                catch(Exception ex)
                {
                    throw new BDException(MeLanbide48I18n.getInstance().getMensaje(codIdioma, "error.centros.errorGuardarCentros"));
                }
            }
            adaptador.finTransaccion(con);
        }
        catch(BDException e)
        {
            log.error("Se ha producido una excepcion en la BBDD eliminando centros", e);
            adaptador.rollBack(con);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido una excepcion en la BBDD eliminando centros", ex);
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
                adaptador.rollBack(con);
            }
        } 
        return true;
    }
    public boolean guardarUbicacionesCT(int codIdioma, ColecUbicacionesCTVO ubicacion, AdaptadorSQLBD adaptador) throws Exception
    {
        Connection con = null;
        try
        {
            boolean error = false;
            con = adaptador.getConnection();
            adaptador.inicioTransaccion(con);
            if(ubicacion != null)
            {
                ubicacion = meLanbide48DAO.guardarUbicacionesCT(ubicacion, con);
            }
            adaptador.finTransaccion(con);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido una excepcion en la BBDD guardando ubicaicones", ex);
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
                adaptador.rollBack(con);
            }
        } 
        return true;
    }
    
    public List<ColecUbicacionesCTVO> getUbicacionesCTPorExpedienteYColectivo(Integer codOrganizacion,Integer idioma,String numExp, Integer colectivo, AdaptadorSQLBD adaptador) throws Exception
    {
        Connection con = null;
        try
        {
            MeLanbideConvocatorias convocatoria = getDecretoAplicableExpediente(codOrganizacion,numExp, adaptador);
            con = adaptador.getConnection();
            return meLanbide48DAO.getUbicacionesCTPorExpedienteYColectivo(idioma,(convocatoria!=null?convocatoria.getId():null),numExp, colectivo, con);
        }
        catch(BDException e)
        {
            log.error("Se ha producido una excepcion en la BBDD recuperando centros par expediente "+(numExp != null ? numExp : " (expediente = null)")+" y colectivo "+(colectivo != null ? colectivo : " (colectivo = null)"), e);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido una excepcion en la BBDD recuperando centros par expediente "+(numExp != null ? numExp : " (expediente = null)")+" y colectivo "+(colectivo != null ? colectivo : " (colectivo = null)"), ex);
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
                adaptador.rollBack(con);
            }
        } 
    }
    
    public List<ColecUbicacionesCTVO> getUbicacionesCTxNumExpediente(String numExp, int idioma,Integer idBDConvocatoria,AdaptadorSQLBD adaptador) throws Exception
    {
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            return meLanbide48DAO.getUbicacionesCTxNumExpediente(numExp,idioma,idBDConvocatoria, con);
        }
        catch(BDException e)
        {
            log.error("Se ha producido una excepcion en la BBDD recuperando centros par expediente "+(numExp != null ? numExp : " (expediente = null)"), e);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido una excepcion en la BBDD recuperando centros par expediente "+(numExp != null ? numExp : " (expediente = null)"), ex);
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
                adaptador.rollBack(con);
            }
        } 
    }
    
    public ColecUbicacionesCTVO getUbicacionCTPorCodigoYExpediente(Integer codOrganizacion,Integer idioma, ColecUbicacionesCTVO ubicacion, AdaptadorSQLBD adaptador) throws Exception
    {
        Connection con = null;
        try
        {
            MeLanbideConvocatorias convocatoria = getDecretoAplicableExpediente(codOrganizacion, (ubicacion!=null?ubicacion.getNumExpediente():""), adaptador);
            con = adaptador.getConnection();
            return meLanbide48DAO.getUbicacionCTPorCodigoYExpediente(idioma,(convocatoria!=null?convocatoria.getId():null),ubicacion, con);
        }
        catch(BDException e)
        {
            log.error("Se ha producido una excepcion en la BBDD recuperando ubicacion "+(ubicacion != null ? ubicacion.getCodId() : "(centro = null)")+" para el expediente "+(ubicacion != null ? ubicacion.getNumExpediente(): " (centro = null)"), e);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido una excepcion en la BBDD recuperando ubicacioin "+(ubicacion != null ? ubicacion.getCodId() : "(centro = null)")+" para el expediente "+(ubicacion != null ? ubicacion.getNumExpediente(): " (centro = null)"), ex);
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
                adaptador.rollBack(con);
            }
        } 
    }
    
    public boolean eliminarUbicacionesCentroTraPorCodigoYExpediente(ColecUbicacionesCTVO centro, AdaptadorSQLBD adaptador) throws Exception
    {
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            return meLanbide48DAO.eliminarUbicacionesCentroTraPorCodigoYExpediente(centro, con);
        }
        catch(BDException e)
        {
            log.error("Se ha producido una excepcion en la BBDD eliminando el centro "+(centro != null ? centro.getCodId(): "(centro = null)")+" para el expediente "+(centro != null ? centro.getNumExpediente(): " (centro = null)"), e);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido una excepcion en la BBDD eliminando el centro "+(centro != null ? centro.getCodId(): "(centro = null)")+" para el expediente "+(centro != null ? centro.getNumExpediente(): " (centro = null)"), ex);
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
                adaptador.rollBack(con);
            }
        } 
    }
    
    public ColecTrayVO getTrayectoriaPorEntidadYExpediente(ColecTrayVO tray, AdaptadorSQLBD adaptador) throws Exception
    {
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            return meLanbide48DAO.getTrayectoriaPorEntidadYExpediente(tray, con);
        }
        catch(BDException e)
        {
            log.error("Se ha producido una excepcion en la BBDD recuperando trayectoria para entidad "+(tray != null ? tray.getCodEntidad() : "(tray = null)")+" y el expediente "+(tray != null ? tray.getNumExp() : " (tray = null)"), e);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido una excepcion en la BBDD recuperando trayectoria para entidad "+(tray != null ? tray.getCodEntidad() : "(tray = null)")+" y el expediente "+(tray != null ? tray.getNumExp() : " (tray = null)"), ex);
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
        
    public boolean guardarTrayectoriaGeneral(ColecTrayVO tray, AdaptadorSQLBD adaptador) throws Exception
    {
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            return meLanbide48DAO.guardarTrayectoriaGeneral(tray, con);
        }
        catch(BDException e)
        {
            log.error("Se ha producido una excepcion en la BBDD guardando trayectoria para entidad "+(tray != null ? tray.getCodEntidad() : "(tray = null)")+" y expediente "+(tray != null ? tray.getNumExp() : " (centro = null)"), e);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido una excepcion en la BBDD guardando trayectoria para entidad "+(tray != null ? tray.getCodEntidad() : "(tray = null)")+" y expediente "+(tray != null ? tray.getNumExp() : " (centro = null)"), ex);
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
                adaptador.rollBack(con);
            }
        } 
    }
    
    public boolean guardarTrayectoriaGeneralEntidades(List<ColecTrayVO> listaTrayectorias, AdaptadorSQLBD adaptador) throws Exception
    {
        Connection con = null;
        boolean correcto = true;
        int i = 0;
        try
        {
            con = adaptador.getConnection();
            adaptador.inicioTransaccion(con);
            ColecTrayVO tray = null;
            while(correcto && i < listaTrayectorias.size())
            {
                tray = listaTrayectorias.get(i);
                correcto = meLanbide48DAO.guardarTrayectoriaGeneral(tray, con);
                i++;
            }
            
            if(correcto)
            {
                adaptador.finTransaccion(con);
                return true;
            }
            else
            {
                adaptador.rollBack(con);
                return false;
            }
        }
        catch(BDException e)
        {
            log.error("Se ha producido una excepcion en la BBDD la trayectoria de una o más entidades", e);
            adaptador.rollBack(con);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido una excepcion en la BBDD la trayectoria de una o más entidades", ex);
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
                adaptador.rollBack(con);
            }
        } 
    }
    
    public List<ColecTrayVO> getListaTrayectoriasPorExpediente(ColecTrayVO trayEjemplo, AdaptadorSQLBD adaptador) throws Exception
    {
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            return meLanbide48DAO.getListaTrayectoriasPorExpediente(trayEjemplo, con);
        }
        catch(BDException e)
        {
            log.error("Se ha producido una excepcion en la BBDD recuperando trayectorias para el expediente "+(trayEjemplo != null ? trayEjemplo.getNumExp() : " (trayEjemplo = null)"), e);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido una excepcion en la BBDD recuperando trayectorias para el expediente "+(trayEjemplo != null ? trayEjemplo.getNumExp() : " (trayEjemplo = null)"), ex);
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
                adaptador.rollBack(con);
            }
        } 
    }
    
    public ColecTrayEspVO getTrayectoriaEspecifica(ColecTrayEspVO tray, AdaptadorSQLBD adaptador) throws Exception
    {
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            return meLanbide48DAO.getTrayectoriaEspecifica(tray, con);
        }
        catch(BDException e)
        {
            log.error("Se ha producido una excepcion en la BBDD recuperando trayectoria especifica para entidad "+(tray != null ? tray.getCodEntidad() : "(tray = null)")+", expediente "+(tray != null ? tray.getNumExp() : " (tray = null)")+" y colectivo "+(tray != null ? tray.getColectivo() : " (tray = null)"), e);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido una excepcion en la BBDD recuperando trayectoria especifica para entidad "+(tray != null ? tray.getCodEntidad() : "(tray = null)")+", expediente "+(tray != null ? tray.getNumExp() : " (tray = null)")+" y colectivo "+(tray != null ? tray.getColectivo() : " (tray = null)"), ex);
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
    
    public boolean guardarTrayectoriaEspecifica(ColecTrayEspVO tray, AdaptadorSQLBD adaptador) throws Exception
    {
        Connection con = null;
        boolean exito = true;
        try
        {
            boolean error = false;
            con = adaptador.getConnection();
            adaptador.inicioTransaccion(con);
            tray = meLanbide48DAO.guardarTrayectoriaEspecifica(tray, con);
            if(tray == null){
                exito = false;
                log.debug("No se han guardado losd atos correctamente de la trayectoria especial. Se ha devuelto el objeto ColecTrayEspVO a null");
            }
            adaptador.finTransaccion(con);
        }
        catch(BDException e)
        {
            log.error("Se ha producido una excepcion en la BBDD guardarTrayectoriasEspecificas ", e);
            adaptador.rollBack(con);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido una excepcion en la BBDD guardarTrayectoriasEspecificas ", ex);
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
                adaptador.rollBack(con);
            }
        } 
        return exito;
    }
    
    public List<FilaTrayEspVO> getListaTrayectoriasEspecificasPorExpedienteYColectivo(ColecTrayEspVO trayEjemplo, AdaptadorSQLBD adaptador) throws Exception
    {
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            return meLanbide48DAO.getListaTrayectoriasEspecificasPorExpedienteYColectivo(trayEjemplo, con);
        }
        catch(BDException e)
        {
            log.error("Se ha producido una excepcion en la BBDD recuperando trayectorias para el expediente "+(trayEjemplo != null ? trayEjemplo.getNumExp() : " (trayEjemplo = null)")+", colectivo "+(trayEjemplo != null ? trayEjemplo.getColectivo() : " (trayEjemplo = null)"), e);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido una excepcion en la BBDD recuperando trayectorias para el expediente "+(trayEjemplo != null ? trayEjemplo.getNumExp() : " (trayEjemplo = null)")+", colectivo "+(trayEjemplo != null ? trayEjemplo.getColectivo() : " (trayEjemplo = null)"), ex);
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
                adaptador.rollBack(con);
            }
        } 
    }
    
    public List<SelectItem> getListaSelectItemEntidadesPorExpediente(String numExpediente, AdaptadorSQLBD adaptador) throws Exception
    {
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            
            return meLanbide48DAO.getListaSelectItemEntidadesPorExpediente(numExpediente, con);
        }
        catch(BDException e)
        {
            log.error("Se ha producido un error recuperando la lista de entidades para expediente "+numExpediente, e);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error recuperando la lista de entidades para expediente "+numExpediente, ex);
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

    public void eliminarDatosTrayectoriaExpecialxExpediente(String numExp, AdaptadorSQLBD adaptador) throws BDException, Exception {
        log.debug("eliminarDatosTrayectoriaExpecialxExpediente() - Begin()");
        Connection con = null;
        try {
            con = adaptador.getConnection();
            adaptador.inicioTransaccion(con);
            
            if (numExp != null) {
                int eliminados = meLanbide48DAO.eliminarDatosTrayectoriaExpecialxExpediente(numExp, con);
                if (eliminados > 0) {
                    log.debug("Se han eliminado " + eliminados+ "Registros para el expediente :" + numExp);
                } else {
                    log.debug("No habían registros para eliminar : " + eliminados+ ", para el expediente :" + numExp);
                }
                adaptador.finTransaccion(con);
            } else {
                throw new BDException();
            }
        } catch (BDException e) {
            adaptador.rollBack(con);
            log.error("Se ha producido una excepcion en la BBDD eliminando datos del expediente : " + numExp, e);
            throw new Exception(e);
        } catch (Exception ex) {
            adaptador.rollBack(con);
            log.error("Se ha producido una excepcion en la BBDD eliminando datos del expediente datos del expediente : " + numExp, ex);
            throw new Exception(ex);
        } finally {
            try {
                adaptador.devolverConexion(con);
            } catch (BDException e) {
                adaptador.rollBack(con);
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
        log.debug("eliminarDatosTrayectoriaExpecialxExpediente() - Begin()");
    }
    
    public void eliminarTrayectoriaEspecial(ColecTrayEspVO trayectoriaEsp, AdaptadorSQLBD adaptador) throws BDException, Exception {
        log.debug("eliminarTrayectoriaExpecial() - Begin()");
        Connection con = null;
        try {
            con = adaptador.getConnection();
            adaptador.inicioTransaccion(con);
            
            if (trayectoriaEsp != null) {
                int eliminados = meLanbide48DAO.eliminarTrayectoriaEspecifica(trayectoriaEsp, con);
                if (eliminados > 0) {
                    log.debug("Se han eliminado " + eliminados+ "Registros para el expediente/codEntidad/CodTrayEsp :" + trayectoriaEsp!=null?trayectoriaEsp.getNumExp()+"/"+trayectoriaEsp.getCodEntidad()+"/"+trayectoriaEsp.getCodTrayEsp():"");
                } else {
                    log.debug("No habían registros para eliminar : " + eliminados+ ", para el expediente/codEntidad/CodTrayEsp :" + trayectoriaEsp!=null?trayectoriaEsp.getNumExp()+"/"+trayectoriaEsp.getCodEntidad()+"/"+trayectoriaEsp.getCodTrayEsp():"");
                }
                adaptador.finTransaccion(con);
            } else {
                throw new BDException();
            }
        } catch (BDException e) {
            adaptador.rollBack(con);
            log.error("Se ha producido una excepcion en la BBDD eliminando datos del expediente/codEntidad/CodTrayEsp :" + trayectoriaEsp!=null?trayectoriaEsp.getNumExp()+"/"+trayectoriaEsp.getCodEntidad()+"/"+trayectoriaEsp.getCodTrayEsp():"", e);
            throw new Exception(e);
        } catch (Exception ex) {
            adaptador.rollBack(con);
            log.error("Se ha producido una excepcion en la BBDD eliminando datos del expediente/codEntidad/CodTrayEsp :" + trayectoriaEsp!=null?trayectoriaEsp.getNumExp()+"/"+trayectoriaEsp.getCodEntidad()+"/"+trayectoriaEsp.getCodTrayEsp():"", ex);
            throw new Exception(ex);
        } finally {
            try {
                adaptador.devolverConexion(con);
            } catch (BDException e) {
                adaptador.rollBack(con);
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
        log.debug("eliminarTrayectoriaExpecial() - End()");
    }

    public void eliminarDatosTrayectoriaGeneralxExpediente(String numExp, AdaptadorSQLBD adaptador) throws BDException, Exception {
        log.debug("eliminarDatosTrayectoriaGeneralxExpediente() - Begin()");
        Connection con = null;
        try {
            con = adaptador.getConnection();
            adaptador.inicioTransaccion(con);
            
            if (numExp != null) {
                int eliminados = meLanbide48DAO.eliminarDatosTrayectoriaGeneralxExpediente(numExp, con);
                if (eliminados > 0) {
                    log.debug("Se han eliminado " + eliminados + "Registros para el expediente :" + numExp);
                } else {
                    log.debug("No habían registros para eliminar : " + eliminados + ", para el expediente :" + numExp);
                }
                adaptador.finTransaccion(con);
            } else {
                throw new BDException();
            }
        } catch (BDException e) {
            adaptador.rollBack(con);
            log.error("Se ha producido una excepcion en la BBDD eliminando datos del expediente : " + numExp, e);
            throw new Exception(e);
        } catch (Exception ex) {
            adaptador.rollBack(con);
            log.error("Se ha producido una excepcion en la BBDD eliminando datos del expediente datos del expediente : " + numExp, ex);
            throw new Exception(ex);
        } finally {
            try {
                adaptador.devolverConexion(con);
            } catch (BDException e) {
                adaptador.rollBack(con);
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
        log.debug("eliminarDatosTrayectoriaGeneralxExpediente() - End()");
    }

    public void eliminarDatosListaEntidadesAgrupacionxExpediente(String numExp, AdaptadorSQLBD adaptador) throws BDException, Exception {
        log.debug("eliminarDatosListaEntidadesAgrupacionxExpediente() - Begin()");
        Connection con = null;
        try {
            con = adaptador.getConnection();
            adaptador.inicioTransaccion(con);
            
            if (numExp != null) {
                // Obtener lista entidades antes de borrar para eliminar datos de certificados de calidad que estan en tabla aparte
                List<ColecEntidadEnAgrupacionVO> listaEntidades = meLanbide48DAO.getListaEntidadesPorExpediente(numExp,con);
                int eliminados = meLanbide48DAO.eliminarDatosListaEntidadesAgrupacionxExpediente(numExp, con);
                if (eliminados > 0) {
                    log.debug("Se han eliminado " + eliminados+ " Registros para el expediente :" + numExp);
                } else {
                    log.debug("No habían registros para eliminar : " + eliminados+ ", para el expediente :" + numExp);
                }
                // Una vez elimandas las entidades, eliminamos los datos de certificados calidad
                if(listaEntidades!= null && listaEntidades.size()>0){
                    log.info("Se han eliminado las entidades de la asociacion .. Procedemos a borrar los datos de Certificados de calidad de "+listaEntidades.size()+" entidades" );
                    for (ColecEntidadEnAgrupacionVO colecEntidadEnAgrupacionVO : listaEntidades) {
                        meLanbide48ManagerColecEntidadCertCalidad.deleteColecEntidadCertCalidadByIdEntidad(colecEntidadEnAgrupacionVO.getCodEntidad().intValue(),con);
                        log.info("ColecEntidadEnAgrupacionVO Eliminada: CodEntPadre: " + colecEntidadEnAgrupacionVO.getPorcentaCompromisoRealizacion()
                                + " Entidad " + colecEntidadEnAgrupacionVO.getCodEntidad() + " - " +colecEntidadEnAgrupacionVO.getNombre()
                        );
                    }
                }
                adaptador.finTransaccion(con);
            } else {
                throw new BDException();
            }
        } catch (BDException e) {
            adaptador.rollBack(con);
            log.error("Se ha producido una excepcion en la BBDD eliminando datos del expediente : " + numExp, e);
            throw new Exception(e);
        } catch (Exception ex) {
            adaptador.rollBack(con);
            log.error("Se ha producido una excepcion en la BBDD eliminando datos del expediente datos del expediente : " + numExp, ex);
            throw new Exception(ex);
        } finally {
            try {
                adaptador.devolverConexion(con);
            } catch (BDException e) {
                adaptador.rollBack(con);
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
        log.debug("eliminarDatosListaEntidadesAgrupacionxExpediente() - End()");
    }

    public ColecEntidadEnAgrupacionVO getEntidadPorCodEntiPadre_CodEntidad_NumExp(ColecEntidadEnAgrupacionVO entidad, AdaptadorSQLBD adaptador) throws Exception {
        Connection con = null;
        try {
            con = adaptador.getConnection();
            
            return meLanbide48DAO.getEntidadPorCodEntiPadre_CodEntidad_NumExp(entidad, con);
        } catch (BDException e) {
            log.error("Se ha producido un error recuperando entidad " + (entidad != null ? entidad.getCodEntidad(): "(entidad = null)") + " para expediente " + (entidad != null ? entidad.getNumExp() : "(entidad = null)"), e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido un error recuperando entidad " + (entidad != null ? entidad.getCodEntidad(): "(entidad = null)") + " para expediente " + (entidad != null ? entidad.getNumExp() : "(entidad = null)"), ex);
            throw new Exception(ex);
        } finally {
            try {
                adaptador.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    public List<FilaEntidadVO> getEntidadadesTrayectoriaGeneralxExpte(String numExpediente, AdaptadorSQLBD adaptador) throws Exception {
        log.debug("getEntidadadesTrayectoriaGeneralxExpte - Begin()");
        List<FilaEntidadVO> listaEntidadesTray = new ArrayList<FilaEntidadVO>();
        try {
            // Consultamos la entidad Principal.
            ColecEntidadVO entidadPadre = new ColecEntidadVO();
            entidadPadre.setNumExp(numExpediente);
            entidadPadre=getEntidadInteresadaPorExpediente(entidadPadre, adaptador);
            if(entidadPadre!=null){
                if(new Integer(1).equals(entidadPadre.getEsAgrupacion())){
                    List<ColecEntidadEnAgrupacionVO> listaEntidadesAsociacion = getListaEntidadesPorExpediente(numExpediente, adaptador);
                    for(ColecEntidadEnAgrupacionVO entidadEnAgrup : listaEntidadesAsociacion){
                        listaEntidadesTray.add(MeLanbide48MappingUtils.getInstance().mapearFromColecEntidadEnAgrupacionVO_To_FilaEntidadVO(entidadEnAgrup));
                    }
                }else{
                    listaEntidadesTray.add(MeLanbide48MappingUtils.getInstance().mapearFromColecEntidadVO_To_FilaEntidadVO(entidadPadre));
                }
            }
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD recuperando entidades de la trayectoria general para el expediente " + numExpediente, e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD recuperando entidades de la trayectoria general para el expediente  " + numExpediente, ex);
            throw new Exception(ex);
        } finally {
            //try {
                //adaptador.devolverConexion(con);
            //} catch (BDException e) {
                //log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
                //adaptador.rollBack(con);
            //}
            log.debug("getEntidadadesTrayectoriaGeneralxExpte  - End()");
        }
        return listaEntidadesTray;
    }

    public List<ColecTrayOtroProgramaVO> getListaTrayGeneralOtrosProgramasxCodEntNumExp(ColecTrayOtroProgramaVO trayEjemplo, AdaptadorSQLBD adapt) throws Exception {
        log.debug("getListaTrayGeneralOtrosProgramasxCodEntNumExp - Begin()");
        Connection con = null;
        try {
            con = adapt.getConnection();
            return meLanbide48DAO.getListaTrayGeneralOtrosProgramasxCodEntNumExp(trayEjemplo, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD recuperando trayectorias para el expediente " + (trayEjemplo != null ? trayEjemplo.getNumExpediente(): " (trayEjemplo = null)"), e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD recuperando trayectorias para el expediente " + (trayEjemplo != null ? trayEjemplo.getNumExpediente(): " (trayEjemplo = null)"), ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
                adapt.rollBack(con);
            }
        }
    }
    
    public List<ColecTrayActividadVO> getListaTrayGeneralActividadesxCodEntNumExp(ColecTrayActividadVO trayEjemplo, AdaptadorSQLBD adapt) throws Exception {
        log.debug("getListaTrayGeneralActividadesxCodEntNumExp - Begin()");
        Connection con = null;
        try {
            con = adapt.getConnection();
            return meLanbide48DAO.getListaTrayGeneralActividadesxCodEntNumExp(trayEjemplo, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD recuperando trayectorias para el expediente " + (trayEjemplo != null ? trayEjemplo.getNumExpediente(): " (trayEjemplo = null)"), e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD recuperando trayectorias para el expediente " + (trayEjemplo != null ? trayEjemplo.getNumExpediente(): " (trayEjemplo = null)"), ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
                adapt.rollBack(con);
            }
        }
    }
    public List<ColecTrayOtroProgramaVO> getListaTrayGeneralOtrosProgramasxNumExp(ColecTrayOtroProgramaVO trayEjemplo, AdaptadorSQLBD adapt) throws Exception {
        log.debug("getListaTrayGeneralOtrosProgramasxNumExp - Begin()");
        Connection con = null;
        try {
            con = adapt.getConnection();
            return meLanbide48DAO.getListaTrayGeneralOtrosProgramasxNumExp(trayEjemplo, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD recuperando trayectorias para el expediente " + (trayEjemplo != null ? trayEjemplo.getNumExpediente(): " (trayEjemplo = null)"), e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD recuperando trayectorias para el expediente " + (trayEjemplo != null ? trayEjemplo.getNumExpediente(): " (trayEjemplo = null)"), ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
                adapt.rollBack(con);
            }
        }
    }
    
    public List<ColecTrayActividadVO> getListaTrayGeneralActividadesxNumExp(ColecTrayActividadVO trayEjemplo, AdaptadorSQLBD adapt) throws Exception {
        log.debug("getListaTrayGeneralActividadesxNumExp - Begin()");
        Connection con = null;
        try {
            con = adapt.getConnection();
            return meLanbide48DAO.getListaTrayGeneralActividadesxNumExp(trayEjemplo, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD recuperando trayectorias para el expediente " + (trayEjemplo != null ? trayEjemplo.getNumExpediente(): " (trayEjemplo = null)"), e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD recuperando trayectorias para el expediente " + (trayEjemplo != null ? trayEjemplo.getNumExpediente(): " (trayEjemplo = null)"), ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
                adapt.rollBack(con);
            }
        }
    }

    public ColecTrayOtroProgramaVO getTrayectoriaGeneralOtrosProgramas(ColecTrayOtroProgramaVO tray, AdaptadorSQLBD adaptador) throws Exception {
        Connection con = null;
        try {
            con = adaptador.getConnection();
            return meLanbide48DAO.getTrayectoriaGeneralOtrosProgramas(tray, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD recuperando trayectoria general otros programas para entidad " + (tray != null ? tray.getCodEntidad() : "(tray = null)") + ", expediente " + (tray != null ? tray.getNumExpediente(): " (tray = null)"), e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD recuperando trayectoria general otros programas  para entidad " + (tray != null ? tray.getCodEntidad() : "(tray = null)") + ", expediente " + (tray != null ? tray.getNumExpediente(): " (tray = null)"), ex);
            throw new Exception(ex);
        } finally {
            try {
                adaptador.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    public ColecTrayActividadVO getTrayectoriaGeneralActividades(ColecTrayActividadVO tray, AdaptadorSQLBD adaptador) throws Exception {
        Connection con = null;
        try {
            con = adaptador.getConnection();
            return meLanbide48DAO.getTrayectoriaGeneralActividades(tray, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD recuperando trayectoria general actividades para entidad " + (tray != null ? tray.getCodEntidad() : "(tray = null)") + ", expediente " + (tray != null ? tray.getNumExpediente() : " (tray = null)"), e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD recuperando trayectoria general actividadespara entidad " + (tray != null ? tray.getCodEntidad() : "(tray = null)") + ", expediente " + (tray != null ? tray.getNumExpediente() : " (tray = null)"), ex);
            throw new Exception(ex);
        } finally {
            try {
                adaptador.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    public boolean guardarTrayectoriaGeneralOtrosProgramas(ColecTrayOtroProgramaVO tray, AdaptadorSQLBD adaptador) throws Exception {
        Connection con = null;
        boolean exito = true;
        try {
            boolean error = false;
            con = adaptador.getConnection();
            adaptador.inicioTransaccion(con);
            tray = meLanbide48DAO.guardarTrayectoriaGeneralOtrosProgramas(tray, con);
            if (tray == null) {
                exito = false;
                log.debug("No se han guardado los datos correctamente de la trayectoria general otros programas. Se ha devuelto el objeto ColecTrayOtroProgramaVO a null");
            }
            adaptador.finTransaccion(con);
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD guardarTrayectoriaGeneralOtrosProgramas ", e);
            adaptador.rollBack(con);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD guardarTrayectoriaGeneralOtrosProgramas ", ex);
            adaptador.rollBack(con);
            throw new Exception(ex);
        } finally {
            try {
                adaptador.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
                adaptador.rollBack(con);
            }
        }
        return exito;
    }
    public boolean guardarTrayectoriaGeneralActividades(ColecTrayActividadVO tray, AdaptadorSQLBD adaptador) throws Exception {
        Connection con = null;
        boolean exito = true;
        try {
            boolean error = false;
            con = adaptador.getConnection();
            adaptador.inicioTransaccion(con);
            tray = meLanbide48DAO.guardarTrayectoriaGeneralActividades(tray, con);
            if (tray == null) {
                exito = false;
                log.debug("No se han guardado los datos correctamente de la trayectoria general actividades. Se ha devuelto el objeto ColecTrayActividadVO a null");
            }
            adaptador.finTransaccion(con);
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD guardarTrayectoriaGeneralActividades ", e);
            adaptador.rollBack(con);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD guardarTrayectoriaGeneralActividades ", ex);
            adaptador.rollBack(con);
            throw new Exception(ex);
        } finally {
            try {
                adaptador.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
                adaptador.rollBack(con);
            }
        }
        return exito;
    }

    public void eliminarTrayectoriaGralOtrosProgramas(ColecTrayOtroProgramaVO tray, AdaptadorSQLBD adaptador) throws Exception {
        log.debug("eliminarTrayectoriaGralOtrosProgramas() - Begin()");
        Connection con = null;
        try {
            con = adaptador.getConnection();
            adaptador.inicioTransaccion(con);
            
            if (tray != null) {
                int eliminados = meLanbide48DAO.eliminarTrayectoriaGralOtrosProgramas(tray, con);
                if (eliminados > 0) {
                    log.debug("Se han eliminado " + eliminados + "Registros para el expediente/codEntidad/CodTray :" + tray != null ? tray.getNumExpediente()+ "/" + tray.getCodEntidad() + "/" + tray.getCodIdOtroPrograma(): "");
                } else {
                    log.debug("No habían registros para eliminar : " + eliminados + ", para el expediente/codEntidad/CodTray :" + tray != null ? tray.getNumExpediente()+ "/" + tray.getCodEntidad() + "/" + tray.getCodIdOtroPrograma(): "");
                }
                adaptador.finTransaccion(con);
            } else {
                throw new BDException();
            }
        } catch (BDException e) {
            adaptador.rollBack(con);
            log.error("Se ha producido una excepcion en la BBDD eliminando datos del expediente/codEntidad/CodTray :" + tray != null ? tray.getNumExpediente()+ "/" + tray.getCodEntidad() + "/" + tray.getCodIdOtroPrograma(): "", e);
            throw new Exception(e);
        } catch (Exception ex) {
            adaptador.rollBack(con);
            log.error("Se ha producido una excepcion en la BBDD eliminando datos del expediente/codEntidad/CodTrayEsp :" + tray != null ? tray.getNumExpediente()+ "/" + tray.getCodEntidad() + "/" + tray.getCodIdOtroPrograma(): "", ex);
            throw new Exception(ex);
        } finally {
            try {
                adaptador.devolverConexion(con);
            } catch (BDException e) {
                adaptador.rollBack(con);
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
        log.debug("eliminarTrayectoriaGralOtrosProgramas() - End()");
    }

    public void eliminarTrayectoriaGralActividades(ColecTrayActividadVO tray, AdaptadorSQLBD adaptador) throws Exception {
        log.debug("eliminarTrayectoriaGralActividades() - Begin()");
        Connection con = null;
        try {
            con = adaptador.getConnection();
            adaptador.inicioTransaccion(con);
            
            if (tray != null) {
                int eliminados = meLanbide48DAO.eliminarTrayectoriaGralActividades(tray, con);
                if (eliminados > 0) {
                    log.debug("Se han eliminado " + eliminados + "Registros para el expediente/codEntidad/CodTray :" + tray != null ? tray.getNumExpediente() + "/" + tray.getCodEntidad() + "/" + tray.getCodIdActividad(): "");
                } else {
                    log.debug("No habían registros para eliminar : " + eliminados + ", para el expediente/codEntidad/CodTray :" + tray != null ? tray.getNumExpediente() + "/" + tray.getCodEntidad() + "/" + tray.getCodIdActividad(): "");
                }
                adaptador.finTransaccion(con);
            } else {
                throw new BDException();
            }
        } catch (BDException e) {
            adaptador.rollBack(con);
            log.error("Se ha producido una excepcion en la BBDD eliminando datos del expediente/codEntidad/CodTray :" + tray != null ? tray.getNumExpediente() + "/" + tray.getCodEntidad() + "/" + tray.getCodIdActividad(): "", e);
            throw new Exception(e);
        } catch (Exception ex) {
            adaptador.rollBack(con);
            log.error("Se ha producido una excepcion en la BBDD eliminando datos del expediente/codEntidad/CodTrayEsp :" + tray != null ? tray.getNumExpediente() + "/" + tray.getCodEntidad() + "/" + tray.getCodIdActividad(): "", ex);
            throw new Exception(ex);
        } finally {
            try {
                adaptador.devolverConexion(con);
            } catch (BDException e) {
                adaptador.rollBack(con);
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
        log.debug("eliminarTrayectoriaGralActividades() - End()");
    }

    public List<ColecComproRealizacionVO> getDatosEntidadadesPorcenComproRealixColecyTHxExpte(String numExpediente, AdaptadorSQLBD adaptador) throws Exception {
        log.debug("getDatosEntidadadesPorcenComproRealixColecyTHxExpte - Begin()");
        Connection con = null;
        try {
            con = adaptador.getConnection();
            return meLanbide48DAO.getDatosEntidadadesPorcenComproRealixColecyTHxExpte(numExpediente, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD getDatosEntidadadesPorcenComproRealixColecyTHxExpte expediente " + numExpediente,e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion  General getDatosEntidadadesPorcenComproRealixColecyTHxExpte  expediente " + numExpediente, ex);
            throw new Exception(ex);
        } finally {
            try {
                adaptador.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }
    public List<ColecComproRealizacionVO> getListaEntidadadesInterfazPorcenComproRealixColecyTHxExpte(String numExpediente, AdaptadorSQLBD adaptador) throws Exception {
        log.debug("getListaEntidadadesInterfazPorcenComproRealixColecyTHxExpte - Begin()");
        Connection con = null;
        try {
            con = adaptador.getConnection();
            return meLanbide48DAO.getListaEntidadadesInterfazPorcenComproRealixColecyTHxExpte(numExpediente, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD getListaEntidadadesInterfazPorcenComproRealixColecyTHxExpte expediente " + numExpediente,e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion  General getListaEntidadadesInterfazPorcenComproRealixColecyTHxExpte  expediente " + numExpediente, ex);
            throw new Exception(ex);
        } finally {
            try {
                adaptador.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    public String guardarDatosComproRealxColeTH(int codOrganizacion, List<ColecComproRealizacionVO> listacompReal, AdaptadorSQLBD adaptador) throws Exception {
        log.debug("guardarDatosComproRealxColeTH - Begin() - Manager");
        String codOperacion = "0";
        Connection con = adaptador.getConnection();
        try {
            for(ColecComproRealizacionVO dato : listacompReal){
                meLanbide48DAO.guardarDatosComproRealxColeTH(dato,con);
            }
        }catch(BDException e)
        {
            log.error("Se ha producido una excepcion en la BBDD guardando datos guardarDatosComproRealxColeTH : " + e.getMensaje(), e);
            adaptador.rollBack(con);
            throw new Exception(e);
        }catch(Exception ex)
        {
            log.error("Error guardarDatosComproRealxColeTH : " + ex.getMessage(),ex);
            codOperacion="3";
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
                adaptador.rollBack(con);
            }
        } 
        
        log.debug("guardarDatosComproRealxColeTH - End() - Manager");
        return codOperacion;
    }

    public void eliminarColecComproRealizacionVOxEntExpte(int codOrganizacion, String numExpediente, Long codEntidad, AdaptadorSQLBD adaptador) throws Exception {
        log.debug("eliminarColecComproRealizacionVOxEntExpte - Begin() - Manager");
        Connection con = null;
        try {
            con = adaptador.getConnection();
            adaptador.inicioTransaccion(con); 
            
            if (codEntidad != null) {
                int eliminados = meLanbide48DAO.eliminarColecComproRealizacionVOxEntExpte(numExpediente,codEntidad, con);
                log.debug("Registros Eliminados : " + eliminados +"- Entidad/Expediente " + codEntidad + " Expediente : " + numExpediente);
                adaptador.finTransaccion(con);
            } else {
                log.error("NO se puede eliminar datos porcentaje compromiso de realización, el codigo de la entidad ha llegado a null");
                throw new BDException();
            }
        } catch (BDException e) {
            adaptador.rollBack(con);
            log.error("Se ha producido una excepcion en la BBDD eliminando Datos Porcentaje Compromiso entidad   " + codEntidad + " Expediente : " + numExpediente, e);
            throw new Exception(e);
        } catch (Exception ex) {
            adaptador.rollBack(con);
            log.error("Se ha producido una excepcion en la BBDD eliminando Datos Porcentaje Compromiso  entidad   " + codEntidad + " Expediente : " + numExpediente, ex);
            throw new Exception(ex);
        } finally {
            try {
                adaptador.devolverConexion(con);
            } catch (BDException e) {
                adaptador.rollBack(con);
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
       log.debug("eliminarColecComproRealizacionVOxEntExpte - End() - Manager");
    }

    /**
     * Metodo que devuelve el dereto o convocatoria a la que pertenece el expediente en funcion de una fecha de referencia: Ejemplo, Fecha presentacion expediente (campo suplementario).
     * @param codOrganizacion
     * @param numExpediente
     * @param adaptador
     * @return
     * @throws Exception
     */
    public MeLanbideConvocatorias getDecretoAplicableExpediente(Integer codOrganizacion,String numExpediente, AdaptadorSQLBD adaptador) throws Exception {
        log.info("getDecretoAplicableExpediente() - Manager - : BEGIN " + formatFechaLog.format(new Date()) + " " + numExpediente);
        Connection con = null;
        try {
            // Recogemos los datos Necesarios
            Date fechaReferenciaExpediente = null;
            fechaReferenciaExpediente = getFechaReferenciaDecretoExpediente(codOrganizacion, numExpediente, adaptador);
            log.info("Fecha Referencia Expediente: " + formatFechaddMMyyy.format(fechaReferenciaExpediente));
            String codProcedimiento = MeLanbide48Utils.getCodProcedimientoDeExpediente(numExpediente);
            con = adaptador.getConnection();
            return meLanbideConvocatoriasDAO.getDecretoAplicableExpediente(fechaReferenciaExpediente, codProcedimiento,con);
        } catch (Exception ex) {
            log.error("Se ha producido una excepción general al recuperar datos del DECRETO " + ex.getMessage(), ex);
            throw new Exception("Error al leer Datos de DECRETO. " + ex.getMessage(), ex);
        } finally {
            try {
                adaptador.devolverConexion(con);
                log.info("getDecretoAplicableExpediente() - Manager - End  " + formatFechaLog.format(new Date()));
            } catch (Exception e) {
                log.error("Error al cerrar conexión a la BBDD: getDecretoAplicableExpediente - " + e.getMessage());
            }
        }
    }

    public Date getFechaReferenciaDecretoExpediente(Integer codOrganizacion, String numExpediente, AdaptadorSQLBD adapt) throws Exception {
        log.info("getFechaReferenciaDecretoExpediente() - Manager - : BEGIN " + formatFechaLog.format(new Date()) + " " + numExpediente);
        Connection con = null;
        Date respuesta = null;
        try {
            con = adapt.getConnection();
            String campoSuplementarioFechRefExpe = ConfigurationParameter.getParameter(ConstantesMeLanbide48.CODIGO_CAMPO_SUP_FECHA_REFE_EXPTE_DECRETO_APLI, ConstantesMeLanbide48.FICHERO_PROPIEDADES);
            if(campoSuplementarioFechRefExpe!=null && !campoSuplementarioFechRefExpe.isEmpty()){
                Integer ejercicio = MeLanbide48Utils.getEjercicioDeExpediente(numExpediente);
                String codProcedimiento  = MeLanbide48Utils.getCodProcedimientoDeExpediente(numExpediente);
                respuesta=getCampoSuplementarioFecha(codOrganizacion,ejercicio,codProcedimiento,numExpediente,campoSuplementarioFechRefExpe);
            }else{
                respuesta=meLanbide48DAO.getFechaAltaExpediente(codOrganizacion,numExpediente,con);
            }
        } catch (Exception ex) {
            log.error("Se ha producido una excepción general al recuperar datos de fecha referencia del expedientes para el decreto aplicable " + ex.getMessage(), ex);
            throw new Exception("Error al leer Datos de DECRETO. " + ex.getMessage(), ex);
        } finally {
            try {
                adapt.devolverConexion(con);
                log.info("getDecretoAplicableExpediente() - Manager - End  " + formatFechaLog.format(new Date()));
            } catch (Exception e) {
                log.error("Error al cerrar conexión a la BBDD: getDecretoAplicableExpediente - " + e.getMessage());
            }
        }
        return respuesta;
    }
    
    public Date getCampoSuplementarioFecha(Integer codOrganizacion, Integer ejercicio,String codProcedimiento, String numExpediente,String codigoCampo) throws Exception {
        log.info("getCampoSuplementarioFecha() : BEGIN ");
        Date valor = null;
        IModuloIntegracionExternoCamposFlexia el = ModuloIntegracionExternoCampoSupFactoria.getInstance().getImplClass();
        SalidaIntegracionVO salida = new SalidaIntegracionVO();
        CampoSuplementarioModuloIntegracionVO campoSuplementario = new CampoSuplementarioModuloIntegracionVO();
        salida = el.getCampoSuplementarioExpediente(String.valueOf(codOrganizacion), String.valueOf(ejercicio), numExpediente,
                codProcedimiento, codigoCampo, 3);
        if (salida.getStatus() == 0) {
            campoSuplementario = salida.getCampoSuplementario();
            if(campoSuplementario!=null && campoSuplementario.getValorFecha()!=null)
                valor = campoSuplementario.getValorFecha().getTime();
        }//if(salida.getStatus() == 0)
        log.info("getCampoSuplementarioFecha() : END ");
        return valor;
    }

    public boolean eliminarLineaColecSolicitud(String identificadorBDEliminar, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        int eliminados=0;
        try {
            con = adapt.getConnection();
            adapt.inicioTransaccion(con);
            
            if (identificadorBDEliminar != null && !identificadorBDEliminar.isEmpty()) {
                eliminados = meLanbide48DAO.eliminarLineaColecSolicitud(identificadorBDEliminar, con);
                adapt.finTransaccion(con);
            } else {
               log.error("Identificador de Line en BD no recibido. No se trata ninguna liena.");
            }
        } catch (BDException e) {
            adapt.rollBack(con);
            log.error("Se ha producido una BDException en la BBDD eliminando Datos ColecSolicitud: " + identificadorBDEliminar, e);
            throw e;
        } catch (Exception ex) {
            adapt.rollBack(con);
            log.error("Se ha producido una Exception eliminando Datos ColecSolicitud " + identificadorBDEliminar, ex);
            throw ex;
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                adapt.rollBack(con);
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
        return eliminados>0;
    }
    
    public List<ColecTrayectoriaEntidad> getListaTrayectoriaAcreditableColectivo(String numExpediente,int colectivo,AdaptadorSQLBD adaptador) throws Exception {
        log.debug("getListaTrayectoriaAcreditableEntidadesColectivo - Begin()");
        Connection con = null;
        try {
            con = adaptador.getConnection();
            return meLanbide48DAO.getListaTrayectoriaAcreditableColectivo(numExpediente,colectivo,con);
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD recuperando entidades de la trayectoria acreditable para el expediente/colectivo " + numExpediente + "/"+ colectivo, e);
            throw new Exception(e);
        } catch (Exception e) {
            log.error("Se ha producido una excepcion en la BBDD recuperando entidades de la trayectoria acreditable para el expediente/colectivo  " + numExpediente+ "/"+ colectivo, e);
            throw new Exception(e);
        } finally {
            adaptador.devolverConexion(con);
            log.debug("getListaTrayectoriaAcreditableEntidadesColectivo  - End()");
        }
    }
        
    public List<ColecTrayectoriaEntidad> getListaTrayectoriaAcreditableGrupoColectivoEntidad(String numExpediente,int codigoGrupo, int colectivo,int entidadIDBD,AdaptadorSQLBD adaptador) throws Exception {
        log.debug("getListaTrayectoriaAcreditableGrupoColectivoEntidad - Begin()");
        Connection con = null;
        try {
            con = adaptador.getConnection();
            return meLanbide48DAO.getListaTrayectoriaAcreditableGrupoColectivoEntidad(numExpediente,codigoGrupo,colectivo,entidadIDBD,con);
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD recuperando entidades de la trayectoria acreditable para el expediente/colectivo " + numExpediente + "/"+ codigoGrupo + "/"+ colectivo+ "/"+ entidadIDBD, e);
            throw new Exception(e);
        } catch (Exception e) {
            log.error("Se ha producido una excepcion en la BBDD recuperando entidades de la trayectoria acreditable para el expediente/colectivo  " + numExpediente + "/"+ codigoGrupo + "/"+ colectivo+ "/"+ entidadIDBD, e);
            throw new Exception(e);
        } finally {
            adaptador.devolverConexion(con);
            log.debug("getListaTrayectoriaAcreditableGrupoColectivoEntidad  - End()");
        }
    }
    
    public List<ColecProgConvActPredefinidaColectivo> getListaConvocatoriasPredefinidaXGrupoXColectivo(Integer idioma, Integer codigoGrupo, Integer colectivo,AdaptadorSQLBD adaptador) throws Exception {
        log.debug("getListaConvocatoriasPredefinidaXGrupoXColectivo - Begin()");
        Connection con = null;
        try {
            con = adaptador.getConnection();
            return meLanbide48DAO.getListaConvocatoriasPredefinidaXGrupoXColectivo(idioma,codigoGrupo,colectivo,con);
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD recuperando lista convocatorias predefinidas por colectivo - "+ colectivo, e);
            throw new Exception(e);
        } catch (Exception e) {
            log.error("Se ha producido una excepcion General recuperando lista convocatorias predefinidas por colectivo - "+ colectivo, e);
            throw new Exception(e);
        } finally {
            adaptador.devolverConexion(con);
            log.debug("getListaConvocatoriasPredefinidaXGrupoXColectivo  - End()");
        }
    }

    /**
     * Guarda los datos de las convocatorias predefinidas - Como en interfaz son check/si-no tiene experiencia, si no la tiene se borrar el registro de BD
     * no se almacenan datos innesarios en la tabla para la entidad definidad
     * @param codIdioma
     * @param datosList
     * @param codigoEntidad
     * @param codigoColectivo
     * @param numeroExpediente
     * @param adaptador
     * @return
     * @throws BDException
     * @throws Exception 
     */
    public String  guardarDatosColecConvocatoriasPredefColectivoEntidad(int codIdioma, List<ColecTrayectoriaEntidad> datosList,int codigoEntidad, int codigoColectivo,String numeroExpediente, AdaptadorSQLBD adaptador) throws BDException, Exception {
        Connection con = null;
        String respuesta="OK";
        try {
            if(datosList!=null){
                con = adaptador.getConnection();
                // Lo hacemos con transaccion porque necesitmos borrar antes de grabar.
                adaptador.inicioTransaccion(con);
                meLanbide48DAO.eliminarDatosColecConvocatoriasPredefColectivoEntidad(new ColecTrayectoriaEntidad(codigoColectivo,codigoEntidad,numeroExpediente), con);
                for (ColecTrayectoriaEntidad datos : datosList) {
                    meLanbide48DAO.guardarDatosColecConvocatoriasPredefColectivoEntidad(datos, con);
                }
                adaptador.finTransaccion(con);
            }
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD guardarDatosColecConvocatoriasPredefColectivoEntidad ", e);
            respuesta="ERROR";
            adaptador.rollBack(con);
            throw e;
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD guardarTrayectoriaGeneralOtrosProgramas ", ex);
            respuesta="ERROR";
            adaptador.rollBack(con);
            throw ex;
        } finally {
            try {
                adaptador.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
        return respuesta;
    }
    
    public boolean guardarDatosColecTrayectoriaEntidad(int codIdioma, ColecTrayectoriaEntidad datos, AdaptadorSQLBD adaptador) throws BDException, Exception {
        Connection con = null;
        try {
            con = adaptador.getConnection();
            return meLanbide48DAO.guardarDatosColecTrayectoriaEntidad(datos, con);
        } catch (BDException e) {
            log.error("Se ha producido una BDException guardarDatosColecTrayectoriaEntidad ", e);
            throw e;
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion guardarDatosColecTrayectoriaEntidad ", ex);
            throw ex;
        } finally {
            try {
                adaptador.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }
    
    public boolean eliminarDatosColecTrayectoriaEntidad(int codIdioma, ColecTrayectoriaEntidad datos, AdaptadorSQLBD adaptador) throws BDException, Exception {
        Connection con = null;
        try {
            con = adaptador.getConnection();
            return meLanbide48DAO.eliminarDatosColecTrayectoriaEntidad(datos, con);
        } catch (BDException e) {
            log.error("Se ha producido una BDException eliminarDatosColecTrayectoriaEntidad ", e);
            throw e;
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion eliminarDatosColecTrayectoriaEntidad ", ex);
            throw ex;
        } finally {
            try {
                adaptador.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    public ColecTrayectoriaEntidad getColecTrayectoriaEntidadXid(Integer identificadorBDGestionar, AdaptadorSQLBD adaptador) throws Exception {
        log.debug("getColecTrayectoriaEntidadXid - Begin()");
        Connection con = null;
        try {
            con = adaptador.getConnection();
            return meLanbide48DAO.getColecTrayectoriaEntidadXid(identificadorBDGestionar,con);
        } catch (BDException e) {
            log.error("Se ha producido una BDException recuperando trayectoria acreditable " + identificadorBDGestionar, e);
            throw e;
        } catch (Exception e) {
            log.error("Se ha producido una Exception recuperando trayectoria acreditable "+ identificadorBDGestionar, e);
            throw new Exception(e);
        } finally {
            adaptador.devolverConexion(con);
            log.debug("getColecTrayectoriaEntidadXid  - End()");
        }
    }
    
    public List<SelectItem> getAmbitoSolicitadoxColectivoConvocatoria(Integer idioma,Integer idBDConvocatoria, Integer codColectivo, AdaptadorSQLBD adaptador) throws Exception {
        Connection con = null;
        try {
            con = adaptador.getConnection();
            return colecAmbitosBloquesHorasDAO.getAmbitoSolicitadoxColectivoConvocatoria(idioma,idBDConvocatoria,codColectivo,con);
        } catch (BDException e) {
            log.error("Se ha producido un error recuperando la lista desplegable ambitos " + idBDConvocatoria+"/"+codColectivo, e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido un error recuperando la lista desplegable ambitos " + idBDConvocatoria+"/"+codColectivo, ex);
            throw new Exception(ex);
        } finally {
            try {
                adaptador.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }
    
    public List<SelectItem> getAmbitoSolicitadoxColectivoConvocatoriaTH(Integer idioma,Integer idBDConvocatoria, Integer codColectivo,Integer territorioHistorico, AdaptadorSQLBD adaptador) throws Exception {
        Connection con = null;
        try {
            con = adaptador.getConnection();
            return colecAmbitosBloquesHorasDAO.getAmbitoSolicitadoxColectivoConvocatoriaTH(idioma,idBDConvocatoria,codColectivo,territorioHistorico,con);
        } catch (BDException e) {
            log.error("Se ha producido un error recuperando la lista desplegable ambitos " + idBDConvocatoria+"/"+codColectivo+"/"+territorioHistorico, e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido un error recuperando la lista desplegable ambitos " + idBDConvocatoria+"/"+codColectivo+"/"+territorioHistorico, ex);
            throw new Exception(ex);
        } finally {
            try {
                adaptador.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: ", e);
            }
        }
    }
    
    public List<SelectItem> getMunicipiosPorConvocatoriaColectivoTH(Integer idioma, Integer idConvocatoria,Integer codColectivo,Integer territorioHistorico,AdaptadorSQLBD adaptador) throws Exception {
        Connection con = null;
        try {
            con = adaptador.getConnection();
            return colecMunicipioDAO.getMunicipiosPorConvocatoriaColectivoTH(idioma,idConvocatoria, codColectivo, territorioHistorico,con);
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD recuperando municipios para codColectivo/territorioHistorico/AmbitoSol " + codColectivo+"/"+territorioHistorico, e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD recuperando municipios para codColectivo/territorioHistorico/AmbitoSol " + codColectivo+"/"+territorioHistorico, ex);
            throw new Exception(ex);
        } finally {
            try {
                adaptador.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }
    public List<SelectItem> getMunicipiosPorConvocatoriaColectivoTHAmbitoSol(Integer idioma, Integer idConvocatoria,Integer codColectivo,Integer territorioHistorico, Integer codAmbitoSol, AdaptadorSQLBD adaptador) throws Exception {
        Connection con = null;
        try {
            con = adaptador.getConnection();
            return colecMunicipioDAO.getMunicipiosPorConvocatoriaColectivoTHAmbitoSol(idioma,idConvocatoria, codColectivo, territorioHistorico,codAmbitoSol, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD recuperando municipios para codColectivo/territorioHistorico/AmbitoSol " + codColectivo+"/"+territorioHistorico+"/"+codAmbitoSol, e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD recuperando municipios para codColectivo/territorioHistorico/AmbitoSol " + codColectivo+"/"+territorioHistorico+"/"+codAmbitoSol, ex);
            throw new Exception(ex);
        } finally {
            try {
                adaptador.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }
    
    public ColecMunVO getMunicipioPorTHCodMun(Integer territorioHistorico, Integer codMunicipio, AdaptadorSQLBD adaptador) throws Exception {
        Connection con = null;
        try {
            con = adaptador.getConnection();
            return colecMunicipioDAO.getMunicipioPorTHCodMun(territorioHistorico,codMunicipio, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD datos municipio territorioHistorico/CodMunicipio " +territorioHistorico+"/"+codMunicipio, e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD recuperando datos municipio territorioHistorico/CodMunicipio " +territorioHistorico+"/"+codMunicipio, ex);
            throw new Exception(ex);
        } finally {
            try {
                adaptador.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }
    
    public Integer getIdByDecretoCodigo(String decretoCodigo,String codigoProcedimiento,AdaptadorSQLBD adaptador) throws Exception {
        Connection con = null;
        try {
            con = adaptador.getConnection();
            return meLanbideConvocatoriasDAO.getIdByDecretoCodigoProcedimiento(decretoCodigo,codigoProcedimiento,con);
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD datos ID Dereto Codigo " +decretoCodigo, e);
            throw new Exception(e);
        } catch (Exception e) {
            log.error("Se ha producido una excepcion en la BBDD datos ID Dereto Codigo " +decretoCodigo, e);
            throw new Exception(e);
        } finally {
            try {
                adaptador.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }
    
    /**
     * Guarda los datos de Meses validados por el tramitador desde la pantalla Resumen Trayectoria.
     * @param datos
     * @param numeroExpediente
     * @param adaptador
     * @return True SI se actualiza al menos un registro, debe existir una entidad dada de alta.
     * @throws BDException
     * @throws Exception 
     */
    public boolean guardarValidarTotalMesesResumen(String numeroExpediente,List<ColecTrayeEntiValida> datos, AdaptadorSQLBD adaptador) throws BDException, Exception {
        Connection con = null;
        boolean exito=true;
        try {
            if(datos!=null && !datos.isEmpty()){
                con = adaptador.getConnection();
                for (ColecTrayeEntiValida dato : datos) {
                    log.info("Procesando : " + dato.getNumeroExpediente() + ": " + dato.getId() + " " + dato.getNumeroMesesValidados());
                    if((dato.getId()!=null)
                            || (dato.getId()== null  && dato.getNumeroMesesValidados()!=null)){
                        log.info("Guardado?: " +
                                colecTrayeEntiValidaDAO.guardarColecTrayeEntiValida(dato, con)
                        );
                    }
                }
            }
        } catch (BDException e) {
            log.error("Se ha producido una BDException guardarValidarTotalMesesResumen ", e);
            exito=false;
            throw e;
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion guardarValidarTotalMesesResumen ", ex);
            exito=false;
            throw ex;
        } finally {
            try {
                adaptador.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: ",e);
            }
        }
        return exito;
    }
    
    /**
     * Recoge la lista de toda la trayectoria, decartando las que no se marcan
     * si tiene experiencia (caso de predefinidas) y fechas
     * invalidas(inicio>fin)
     *
     * @param idioma
     * @param numExpediente
     * @param adaptador
     * @return
     * @throws es.altia.util.conexion.BDException
     * @throws SQLException
     * @throws Exception
     */
    public List<ColecTrayectoriaEntidad> getTodaTrayectoriaAcreditableExpedienteNoSolapable(int idioma, String numExpediente, AdaptadorSQLBD adaptador) throws BDException, Exception {
        Connection con = null;
        try {
            con = adaptador.getConnection();
            return meLanbide48DAO.getTodaTrayectoriaAcreditableExpedienteNoSolapable(idioma, numExpediente, con);
        } catch (BDException e) {
            log.error("Se ha producido una BDException getTodaTrayectoriaAcreditableExpedienteNoSolapable ", e);
            throw e;
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion getTodaTrayectoriaAcreditableExpedienteNoSolapable ", ex);
            throw ex;
        } finally {
            try {
                adaptador.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD",e);
            }
        }
    }

    public Double getNumeroTotalMesesValidadosExpediente(String numExpediente, AdaptadorSQLBD adapt) throws BDException, Exception {
        Connection con = null;
        try {
            con = adapt.getConnection();
            return meLanbide48DAO.getNumeroTotalMesesValidadosExpediente(numExpediente, con);
        } catch (BDException e) {
            log.error("Se ha producido una BDException getNumeroTotalMesesValidadosExpediente ", e);
            throw e;
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion getNumeroTotalMesesValidadosExpediente ", ex);
            throw ex;
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD", e);
            }
        }
    }
    
    public Double getNumeroTotalMesesSinSolaparFechasTrayExpedienteColectivo(int idioma,String numExpediente,int colectivo, AdaptadorSQLBD adapt) throws BDException, Exception {
        Double respuesta = 0.0D;
        try {
            List<ColecTrayectoriaEntidad> lista = this.getTodaTrayectoriaAcreditableExpedienteNoSolapable(idioma, numExpediente, adapt);
            if(lista!=null && !lista.isEmpty()){
                for (ColecTrayectoriaEntidad colecTrayectoriaEntidad : lista) {
                    if(colectivo==colecTrayectoriaEntidad.getTrayCodColectivo())
                        respuesta+=colecTrayectoriaEntidad.getTrayNumeroMeses();
                }
            }
        } catch (BDException e) {
            log.error("Se ha producido una BDException getNumeroTotalMesesValidadosExpediente ", e);
            throw e;
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion getNumeroTotalMesesValidadosExpediente ", ex);
            throw ex;
        } 
        return respuesta;
    }
    
    public Double getMunicipioPuntuacionPorConvocatoriaColectivoTHCodMun(Integer idBDConvocattoria, Integer colectivo, Integer territorioHistorico, Integer codMunicipio, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try {
            con = adapt.getConnection();
            return colecMunicipioDAO.getMunicipioPuntuacionPorConvocatoriaColectivoTHCodMun(idBDConvocattoria, colectivo, territorioHistorico, codMunicipio, con);
        } catch (BDException e) {
            log.error("Se ha producido una BDException getMunicipioPuntuacionPorConvocatoriaColectivoTHCodMun ", e);
            throw e;
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion getMunicipioPuntuacionPorConvocatoriaColectivoTHCodMun ", ex);
            throw ex;
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD", e);
            }
        }
    }
    /**
     * rECUPERA LOS DATOS DE Valoracion de una ubicacion, por ID 
     * @param idBD
     * @param adapt
     * @return
     * @throws Exception 
     */
    public ColecUbicCTValoracion getColecUbicCTValoracionByID(Integer idBD,AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try {
            con = adapt.getConnection();
            return colecUbicCTValoracionDAO.getColecUbicCTValoracionByID(idBD,con);
        } catch (BDException e) {
            log.error("Se ha producido una BDException getColecUbicCTValoracionByID ", e);
            throw e;
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion getColecUbicCTValoracionByID ", ex);
            throw ex;
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD", e);
            }
        }
    }
    
    /**
     * Guarda datos de la validacion y caloracionde una ubicacion
     * @param colecUbicCTValoracion
     * @param adapt
     * @return
     * @throws Exception 
     */
    public ColecUbicCTValoracion guardarColecUbicCTValoracion(ColecUbicCTValoracion colecUbicCTValoracion,AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try {
            con = adapt.getConnection();
            return colecUbicCTValoracionDAO.guardarColecUbicCTValoracion(colecUbicCTValoracion, con);
        } catch (BDException e) {
            log.error("Se ha producido una BDException guardarColecUbicCTValoracion ", e);
            throw e;
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion guardarColecUbicCTValoracion ", ex);
            throw ex;
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD", e);
            }
        }
    }
    
    /**
     * Devuelve datos de valoracion y validacion de una ubicacion, hay resticcion por BBDD solouna linea por expediente y ubicacion para tabla de valoracion.
     * @param idFkUbicacion
     * @param adapt
     * @return
     * @throws Exception 
     */
    public ColecUbicCTValoracion getColecUbicCTValoracionByidFkUbicacion(Integer idFkUbicacion, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try {
            con = adapt.getConnection();
            return colecUbicCTValoracionDAO.getColecUbicCTValoracionByidFkUbicacion(idFkUbicacion, con);
        } catch (BDException e) {
            log.error("Se ha producido una BDException getColecUbicCTValoracionByidFkUbicacion ", e);
            throw e;
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion getColecUbicCTValoracionByidFkUbicacion ", ex);
            throw ex;
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD", e);
            }
        }
    }
    
    public ColecTrayeEntiValida getColecTrayeEntiValidaByID(int id, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try {
            con = adapt.getConnection();
            return colecTrayeEntiValidaDAO.getColecTrayeEntiValidaByID(id, con);
        } catch (BDException e) {
            log.error("Se ha producido una BDException getColecTrayeEntiValidaByID ", e);
            throw e;
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion getColecTrayeEntiValidaByID ", ex);
            throw ex;
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD", e);
            }
        }
    }
    
    public List<ColecTrayeEntiValida> getColecTrayeEntiValidaByNumExpedienteCodEntidad(String numeroExpediente,int codEntidad, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try {
            con = adapt.getConnection();
            return colecTrayeEntiValidaDAO.getColecTrayeEntiValidaByNumExpedienteCodEntidad(numeroExpediente,codEntidad, con);
        } catch (BDException e) {
            log.error("Se ha producido una BDException getColecTrayeEntiValidaByNumExpedienteCodEntidad ", e);
            throw e;
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion getColecTrayeEntiValidaByNumExpedienteCodEntidad ", ex);
            throw ex;
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD", e);
            }
        }
    }
    
    public ColecTrayeEntiValida getColecTrayeEntiValidaByEntidadColectivo(int codEntidad,int codColectivo, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try {
            con = adapt.getConnection();
            return colecTrayeEntiValidaDAO.getColecTrayeEntiValidaByEntidadColectivo(codEntidad,codColectivo, con);
        } catch (BDException e) {
            log.error("Se ha producido una BDException getColecTrayeEntiValidaByEntidadColectivo ", e);
            throw e;
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion getColecTrayeEntiValidaByEntidadColectivo ", ex);
            throw ex;
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD", e);
            }
        }
    }
        
    public List<SelectItem> cagarDesplegableConvocatoriasProcAdj(String codigoProcedimiento, Integer idioma, AdaptadorSQLBD adaptador) throws Exception {
        Connection con = null;
        List<SelectItem> respuesta = new ArrayList<SelectItem>();
        try {
            con = adaptador.getConnection();
            List<MeLanbideConvocatorias> listaConvocatorias =  meLanbideConvocatoriasDAO.getDecretosxCodigoProcedimientoRegexlan(codigoProcedimiento,con);
            for (MeLanbideConvocatorias listaConvocatoria : listaConvocatorias) {
                respuesta.add(new SelectItem(String.valueOf(listaConvocatoria.getId())
                        , listaConvocatoria.getDecretoCodigo()
                        , (idioma==4?listaConvocatoria.getDecretoDesripcionEu():listaConvocatoria.getDecretoDesripcion()))
                );
            }
        } catch (BDException e) {
            log.error("Se ha producido un error recuperando la lista desplegable Convocatorias " + codigoProcedimiento, e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido un error recuperando la lista desplegable Convocatorias " + codigoProcedimiento, ex);
            throw new Exception(ex);
        } finally {
            try {
                adaptador.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: ", e);
            }
        }
        return respuesta;
    }

    public ColecProcesoAdjudicacionRespuestaVO lanzarProcesoAdjudicacion(Integer idBdConvocatoria, Integer codIdioma,AdaptadorSQLBD adaptador) throws Exception {
        Connection con = null;
        try {
            con = adaptador.getConnection();
            return  meLanbide48DAO.lanzarProcesoAdjudicacion(idBdConvocatoria,codIdioma,con);
        } catch (BDException ex) {
            log.error("Se ha producido un error Ejecutando proceso adjudicacion " + idBdConvocatoria, ex);
            throw new Exception(ex);
        } catch (Exception ex) {
            log.error("Se ha producido un error Ejecutando proceso de adjudicacion " + idBdConvocatoria, ex);
            throw new Exception(ex);
        } finally {
            try {
                adaptador.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: ", e);
            }
        }
    }
    
    public ColecColectivo getColecColecColectivoById(int id, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try {
            con = adapt.getConnection();
            return colecColectivoDAO.getColecColectivoById(id, con);
        } catch (BDException e) {
            log.error("Se ha producido una BDException getColecColecColectivoById ", e);
            throw e;
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion getColecColecColectivoById ", ex);
            throw ex;
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD", e);
            }
        }
    }
    
    public List<ColecColectivo> getColecColectivoTodos(AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try {
            con = adapt.getConnection();
            return colecColectivoDAO.getColecColectivoTodos(con);
        } catch (BDException e) {
            log.error("Se ha producido una BDException getColecColectivoTodos ", e);
            throw e;
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion getColecColectivoTodos ", ex);
            throw ex;
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD", e);
            }
        }
    }
    
    public ColecAmbitosBloquesHoras getColecAmbitosBloquesHorasByPK(int idBdConvocatoria, int colectivo, int id,AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try {
            con = adapt.getConnection();
            return colecAmbitosBloquesHorasDAO.getColecAmbitosBloquesHorasByPK(idBdConvocatoria,colectivo,id,con);
        } catch (BDException e) {
            log.error("Se ha producido una BDException getColecAmbitosBloquesHorasByPK ", e);
            throw e;
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion getColecAmbitosBloquesHorasByPK ", ex);
            throw ex;
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD", e);
            }
        }
    }
    
    public List<ColecAmbitosBloquesHoras> getColecAmbitosBloquesHorasByConvocatoriaColectivo(int idBdConvocatoria, int colectivo,AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try {
            con = adapt.getConnection();
            return colecAmbitosBloquesHorasDAO.getColecAmbitosBloquesHorasByConvocatoriaColectivo(idBdConvocatoria,colectivo,con);
        } catch (BDException e) {
            log.error("Se ha producido una BDException getColecAmbitosBloquesHorasByConvocatoriaColectivo ", e);
            throw e;
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion getColecAmbitosBloquesHorasByConvocatoriaColectivo ", ex);
            throw ex;
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD", e);
            }
        }
    }
    
}
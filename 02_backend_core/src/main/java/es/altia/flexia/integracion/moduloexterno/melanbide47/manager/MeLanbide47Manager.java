/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.melanbide47.manager;

import es.altia.flexia.integracion.moduloexterno.melanbide47.dao.MeLanbide47DAO;
import es.altia.flexia.integracion.moduloexterno.melanbide47.dao.MeLanbideConvocatoriasDAO;
import es.altia.flexia.integracion.moduloexterno.melanbide47.util.ConfigurationParameter;
import es.altia.flexia.integracion.moduloexterno.melanbide47.util.ConstantesMeLanbide47;
import es.altia.flexia.integracion.moduloexterno.melanbide47.util.MeLanbide47Utils;
import es.altia.flexia.integracion.moduloexterno.melanbide47.vo.comun.AuditoriaVO;
import es.altia.flexia.integracion.moduloexterno.melanbide47.vo.comun.MeLanbideConvocatorias;
import es.altia.flexia.integracion.moduloexterno.melanbide47.vo.comun.DomicilioVO;
import es.altia.flexia.integracion.moduloexterno.melanbide47.vo.comun.EntidadVO;
import es.altia.flexia.integracion.moduloexterno.melanbide47.vo.comun.EspecialidadesVO;
import es.altia.flexia.integracion.moduloexterno.melanbide47.vo.comun.MunicipioVO;
import es.altia.flexia.integracion.moduloexterno.melanbide47.vo.comun.ProvinciaVO;
import es.altia.flexia.integracion.moduloexterno.melanbide47.vo.comun.SelectItem;
import es.altia.flexia.integracion.moduloexterno.melanbide47.vo.comun.TerceroVO;
import es.altia.flexia.integracion.moduloexterno.melanbide47.vo.comun.TipoViaVO;
import es.altia.flexia.integracion.moduloexterno.melanbide47.vo.comun.ViaVO;
import es.altia.flexia.integracion.moduloexterno.melanbide47.vo.orientacion.AmbitosHorasVO;
import es.altia.flexia.integracion.moduloexterno.melanbide47.vo.orientacion.AsociacionVO;
import es.altia.flexia.integracion.moduloexterno.melanbide47.vo.orientacion.FilaAsociacionVO;
import es.altia.flexia.integracion.moduloexterno.melanbide47.vo.orientacion.FilaOriAmbitoSolicitadoVO;
import es.altia.flexia.integracion.moduloexterno.melanbide47.vo.orientacion.FilaUbicOrientacionVO;
import es.altia.flexia.integracion.moduloexterno.melanbide47.vo.orientacion.OriAmbitoSolicitadoVO;
import es.altia.flexia.integracion.moduloexterno.melanbide47.vo.orientacion.OriTrayActividadVO;
import es.altia.flexia.integracion.moduloexterno.melanbide47.vo.orientacion.OriTrayOtroProgramaVO;
import es.altia.flexia.integracion.moduloexterno.melanbide47.vo.orientacion.OriTrayectoriaEntidadVO;
import es.altia.flexia.integracion.moduloexterno.melanbide47.vo.orientacion.OriTrayectoriaVO;
import es.altia.flexia.integracion.moduloexterno.melanbide47.vo.orientacion.OriUbicVO;
import es.altia.flexia.integracion.moduloexterno.melanbide47.vo.orientacion.UbicacionesVO;
import es.altia.flexia.integracion.moduloexterno.melanbide47.vo.procesos.FilaAuditoriaProcesosVO;
import es.altia.flexia.integracion.moduloexterno.plugin.ModuloIntegracionExternoCampoSupFactoria;
import es.altia.flexia.integracion.moduloexterno.plugin.camposuplementario.IModuloIntegracionExternoCamposFlexia;
import es.altia.flexia.integracion.moduloexterno.plugin.persistence.vo.CampoSuplementarioModuloIntegracionVO;
import es.altia.flexia.integracion.moduloexterno.plugin.persistence.vo.SalidaIntegracionVO;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.altia.util.conexion.BDException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;



/**
 *
 * @author santiagoc
 */
public class MeLanbide47Manager 
{
    //Logger
    private static Logger log = LogManager.getLogger(MeLanbide47Manager.class);
    
    // Formateador de fecha para trzas en logs dureacion de metodos, etc
    SimpleDateFormat formatFechaLog = new SimpleDateFormat("yyyyMMddHHmmssSSS");

    //Instancia
    private static MeLanbide47Manager instance = null;
    private MeLanbideConvocatoriasDAO meLanbideConvocatoriasDAO = new MeLanbideConvocatoriasDAO();

    private MeLanbide47Manager() {
        
    }
    
    public static MeLanbide47Manager getInstance()
    {
        if(instance == null)
        {
            synchronized(MeLanbide47Manager.class)
            {
                instance = new MeLanbide47Manager();
            }
        }
        return instance;
    }    
    
    public List<SelectItem> getListaProvincias(Integer codigoPais, List<String> codigosProv, AdaptadorSQLBD adaptador)throws Exception
    {
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            MeLanbide47DAO meLanbide47DAO = MeLanbide47DAO.getInstance();
            return meLanbide47DAO.getListaProvincias(codigoPais, codigosProv, con);
        }
        catch(BDException e)
        {
            log.error("Se ha producido una excepci贸n en la BBDD recuperando la lista de territorios para el pais " + codigoPais, e);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido una excepci贸n en la BBDD recuperando la lista de territorios para el pais " + codigoPais, ex);
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
                log.error("Error al cerrar conexi贸n a la BBDD: " + e.getMessage());
            }
        }
    }
    
    public List<SelectItem> getAmbitosHorasPorProvincia(String codProvincia, String anoConv, AdaptadorSQLBD adaptador)throws Exception
    {
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            MeLanbide47DAO meLanbide47DAO = MeLanbide47DAO.getInstance();
            return meLanbide47DAO.getAmbitosHorasPorProvincia(codProvincia, anoConv, con);
        }
        catch(BDException e)
        {
            log.error("Se ha producido una excepci贸n en la BBDD recuperando la lista de ambitos para el territorio historico " + codProvincia, e);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido una excepci贸n en la BBDD recuperando la lista de ambitos para el territorio historico " + codProvincia, ex);
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
                log.error("Error al cerrar conexi贸n a la BBDD: " + e.getMessage());
            }
        }
    }
    
    public List<SelectItem> getMunicipiosPorAmbitoProvinciaHoras(String codProvincia, Integer codAmbito, Integer ano, AdaptadorSQLBD adaptador)throws Exception
    {
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            MeLanbide47DAO meLanbide47DAO = MeLanbide47DAO.getInstance();
            return meLanbide47DAO.getMunicipiosPorAmbitoProvinciaHoras(codProvincia, codAmbito, ano, con);
        }
        catch(BDException e)
        {
            log.error("Se ha producido una excepci贸n en la BBDD recuperando la lista de municipios para el territorio historico " + codProvincia+", ambito "+codAmbito, e);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido una excepci贸n en la BBDD recuperando la lista de municipios para el territorio historico " + codProvincia+", ambito "+codAmbito, ex);
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
                log.error("Error al cerrar conexi贸n a la BBDD: " + e.getMessage());
            }
        }
    }
    
    public EntidadVO getEntidad(int codOrganizacion, String numExp, Integer ejercicio, AdaptadorSQLBD adaptador)throws Exception
    {
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            MeLanbide47DAO meLanbide47DAO = MeLanbide47DAO.getInstance();
            return meLanbide47DAO.getEntidad(codOrganizacion, numExp, ejercicio, con);
        }
        catch(BDException e)
        {
            log.error("Se ha producido una excepci贸n en la BBDD recuperando entidad ", e);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido una excepci贸n en la BBDD recuperando entidad ", ex);
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
                log.error("Error al cerrar conexi贸n a la BBDD: " + e.getMessage());
            }
        }
    }
    
    public EntidadVO getEntidadPorCodigoYExpediente(EntidadVO entidad, int idioma,  AdaptadorSQLBD adaptador)throws Exception
    {
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            MeLanbide47DAO meLanbide47DAO = MeLanbide47DAO.getInstance();
            return meLanbide47DAO.getEntidadPorCodigoYExpediente(entidad, idioma,con);
        }
        catch(BDException e)
        {
            log.error("Se ha producido una excepci贸n en la BBDD recuperando entidad ", e);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido una excepci贸n en la BBDD recuperando entidad ", ex);
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
                log.error("Error al cerrar conexi贸n a la BBDD: " + e.getMessage());
            }
        }
    }
    
    public HashMap<String, String> getDatosTercero(int codOrganizacion, String numExp, Integer ejercicio, AdaptadorSQLBD adaptador)throws Exception
    {
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            MeLanbide47DAO meLanbide47DAO = MeLanbide47DAO.getInstance();
            return meLanbide47DAO.getDatosTercero(codOrganizacion, numExp, ejercicio, con);
        }
        catch(BDException e)
        {
            log.error("Se ha producido una excepci贸n en la BBDD recuperando entidad ", e);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido una excepci贸n en la BBDD recuperando entidad ", ex);
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
                log.error("Error al cerrar conexi贸n a la BBDD: " + e.getMessage());
            }
        }
    }
    
    public int eliminarUbicacionORI(OriUbicVO ubic, AdaptadorSQLBD adaptador)throws Exception
    {
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            MeLanbide47DAO meLanbide47DAO = MeLanbide47DAO.getInstance();
            return meLanbide47DAO.eliminarUbicacionORI(ubic, con);
        }
        catch(BDException e)
        {
            log.error("Se ha producido una excepci贸n en la BBDD eliminando ubicacion "+(ubic != null ? ubic.getOriOrientUbicCod() : "(ubicacion = null)")+" para entidad "+(ubic != null ? ubic.getOriEntCod() : "(ubicacion = null)"), e);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido una excepci贸n en la BBDD eliminando ubicacion "+(ubic != null ? ubic.getOriOrientUbicCod() : "(ubicacion = null)")+" para entidad "+(ubic != null ? ubic.getOriEntCod() : "(ubicacion = null)"), ex);
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
                log.error("Error al cerrar conexi贸n a la BBDD: " + e.getMessage());
            }
        }
    }
    
    public List<FilaUbicOrientacionVO> getUbicacionesORI(EntidadVO ent,boolean nuevaCon, AdaptadorSQLBD adaptador)throws Exception
    {
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            MeLanbide47DAO meLanbide47DAO = MeLanbide47DAO.getInstance();
            return meLanbide47DAO.getUbicacionesORI(ent, nuevaCon, con);
        }
        catch(BDException e)
        {
            log.error("Se ha producido una excepci贸n en la BBDD recuperando ubicaciones para el expediente "+(ent != null ? ent.getExtNum() : "(entidad = null)"), e);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido una excepci贸n en la BBDD recuperando ubicaciones para el expediente "+(ent != null ? ent.getExtNum() : "(entidad = null)"), ex);
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
                log.error("Error al cerrar conexi贸n a la BBDD: " + e.getMessage());
            }
        }
    }
    
    public OriUbicVO getUbicacionORIPorCodigo(OriUbicVO ubic, boolean nuevaCon, AdaptadorSQLBD adaptador)throws Exception
    {
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            MeLanbide47DAO meLanbide47DAO = MeLanbide47DAO.getInstance();
            return meLanbide47DAO.getUbicacionORIPorCodigo(ubic,nuevaCon, con);
        }
        catch(BDException e)
        {
            log.error("Se ha producido una excepcion en la BBDD recuperando ubicacion "+(ubic != null ? ubic.getOriOrientUbicCod() : "(ubic = null)"), e);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido una excepcion en la BBDD recuperando ubicacion "+(ubic != null ? ubic.getOriOrientUbicCod() : "(ubic = null)"), ex);
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
                log.error("Error al cerrar conexi贸n a la BBDD: " + e.getMessage());
            }
        }
    }
    
    public ProvinciaVO getProvinciaPorCodigo(Integer provCod, AdaptadorSQLBD adaptador)throws Exception
    {
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            MeLanbide47DAO meLanbide47DAO = MeLanbide47DAO.getInstance();
            return meLanbide47DAO.getProvinciaPorCodigo(provCod, con);
        }
        catch(BDException e)
        {
            log.error("Se ha producido una excepci贸n en la BBDD recuperando provincia "+provCod, e);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido una excepci贸n en la BBDD recuperando provincia "+provCod, ex);
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
                log.error("Error al cerrar conexi贸n a la BBDD: " + e.getMessage());
            }
        }
    }
    
    public AmbitosHorasVO getAmbitoHorasPorCodigo(Integer ambCod, AdaptadorSQLBD adaptador)throws Exception
    {
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            MeLanbide47DAO meLanbide47DAO = MeLanbide47DAO.getInstance();
            return meLanbide47DAO.getAmbitoHorasPorCodigo(ambCod, con);
        }
        catch(BDException e)
        {
            log.error("Se ha producido una excepci贸n en la BBDD recuperando ambito "+ambCod, e);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido una excepci贸n en la BBDD recuperando ambito "+ambCod, ex);
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
                log.error("Error al cerrar conexi贸n a la BBDD: " + e.getMessage());
            }
        }
    }
    
    public MunicipioVO getMunicipioPorCodigoYProvincia(Integer munCod, Integer provCod, AdaptadorSQLBD adaptador)throws Exception
    {
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            MeLanbide47DAO meLanbide47DAO = MeLanbide47DAO.getInstance();
            return meLanbide47DAO.getMunicipioPorCodigoYProvincia(munCod, provCod, con);
        }
        catch(BDException e)
        {
            log.error("Se ha producido una excepci贸n en la BBDD recuperando municipio "+munCod+" para la provincia "+provCod, e);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido una excepci贸n en la BBDD recuperando municipio "+munCod+" para la provincia "+provCod, ex);
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
                log.error("Error al cerrar conexi贸n a la BBDD: " + e.getMessage());
            }
        }
    }
    
    public UbicacionesVO getUbicacion(OriUbicVO ubic, AdaptadorSQLBD adaptador)throws Exception
    {
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            MeLanbide47DAO meLanbide47DAO = MeLanbide47DAO.getInstance();
            return meLanbide47DAO.getUbicacion(ubic, con);
        }
        catch(BDException e)
        {
            log.error("Se ha producido una excepci贸n en la BBDD recuperando ubicacion", e);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido una excepci贸n en la BBDD recuperando ubicacion", ex);
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
                log.error("Error al cerrar conexi贸n a la BBDD: " + e.getMessage());
            }
        }
    }
    
    public OriTrayectoriaVO getTrayectoriaORIPorCodigo(String codTray, AdaptadorSQLBD adaptador)throws Exception
    {
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            MeLanbide47DAO meLanbide47DAO = MeLanbide47DAO.getInstance();
            return meLanbide47DAO.getTrayectoriaORIPorCodigo(codTray, con);
        }
        catch(BDException e)
        {
            log.error("Se ha producido una excepci贸n en la BBDD recuperando trayectoria "+codTray, e);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido una excepci贸n en la BBDD recuperando trayectoria "+codTray, ex);
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
                log.error("Error al cerrar conexi贸n a la BBDD: " + e.getMessage());
            }
        }
    }
    
    public EntidadVO guardarEntidad(EntidadVO entidad, AdaptadorSQLBD adaptador)throws Exception
    {
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            MeLanbide47DAO meLanbide47DAO = MeLanbide47DAO.getInstance();
            return meLanbide47DAO.guardarEntidad(entidad, con);
        }
        catch(BDException e)
        {
            log.error("Se ha producido una excepci贸n en la BBDD guardando entidad "+entidad.getOriEntCod(), e);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido una excepci贸n en la BBDD guardando entidad "+entidad.getOriEntCod(), ex);
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
                log.error("Error al cerrar conexi贸n a la BBDD: " + e.getMessage());
            }
        }
    }
    
    public List<SelectItem> getListaEspecialidades(AdaptadorSQLBD adaptador)throws Exception
    {
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            MeLanbide47DAO meLanbide47DAO = MeLanbide47DAO.getInstance();
            return meLanbide47DAO.getListaEspecialidades(con);
        }
        catch(BDException e)
        {
            log.error("Se ha producido una excepci贸n en la BBDD recuperando especialidades", e);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido una excepci贸n en la BBDD recuperando especialidades", ex);
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
                log.error("Error al cerrar conexi贸n a la BBDD: " + e.getMessage());
            }
        }
    }
    
    public List<Integer> getDistintasProvDeAmbitosHoras(AdaptadorSQLBD adaptador)throws Exception
    {
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            MeLanbide47DAO meLanbide47DAO = MeLanbide47DAO.getInstance();
            return meLanbide47DAO.getDistintasProvDeAmbitosHoras(con);
        }
        catch(BDException e)
        {
            log.error("Se ha producido una excepci贸n en la BBDD recuperando provincias de ambitos", e);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido una excepci贸n en la BBDD recuperando provincias de ambitos", ex);
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
                log.error("Error al cerrar conexi贸n a la BBDD: " + e.getMessage());
            }
        }
    }
    
    public List<Integer> getDistintasProvDeAmbitosCentroEmpleo(AdaptadorSQLBD adaptador)throws Exception
    {
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            MeLanbide47DAO meLanbide47DAO = MeLanbide47DAO.getInstance();
            return meLanbide47DAO.getDistintasProvDeAmbitosCentroEmpleo(con);
        }
        catch(BDException e)
        {
            log.error("Se ha producido una excepci贸n en la BBDD recuperando provincias de ambitos", e);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido una excepci贸n en la BBDD recuperando provincias de ambitos", ex);
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
                log.error("Error al cerrar conexi贸n a la BBDD: " + e.getMessage());
            }
        }
    }
    
    public List<AmbitosHorasVO> getAmbitosHorasPorProvincia(Integer codProvincia, Integer anoConv, AdaptadorSQLBD adaptador)throws Exception
    {
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            MeLanbide47DAO meLanbide47DAO = MeLanbide47DAO.getInstance();
            return meLanbide47DAO.getAmbitosHorasPorProvincia(codProvincia, anoConv, con);
        }
        catch(BDException e)
        {
            log.error("Se ha producido una excepci贸n en la BBDD recuperando la lista de ambitos para el territorio historico " + codProvincia, e);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido una excepci贸n en la BBDD recuperando la lista de ambitos para el territorio historico " + codProvincia, ex);
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
                log.error("Error al cerrar conexi贸n a la BBDD: " + e.getMessage());
            }
        }
    }
    
    public List<OriUbicVO> getUbicacionesDeAmbitoORIDocumResolProvisional(AmbitosHorasVO amb, AdaptadorSQLBD adaptador) throws Exception
    {
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            MeLanbide47DAO meLanbide47DAO = MeLanbide47DAO.getInstance();
            return meLanbide47DAO.getUbicacionesDeAmbitoORIDocumResolProvisional(amb, con);
        }
        catch(BDException e)
        {
            log.error("Se ha producido una excepci贸n en la BBDD recuperando la lista de entidades para el ambito " + amb.getOriAmbCod(), e);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido una excepci贸n en la BBDD recuperando la lista de entidades para el ambito " + amb.getOriAmbCod(), ex);
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
                log.error("Error al cerrar conexi贸n a la BBDD: " + e.getMessage());
            }
        }
    }
    
    public String[] adjudicaOrientacion(Integer ano, AdaptadorSQLBD adaptador) throws Exception
    {
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            MeLanbide47DAO meLanbide47DAO = MeLanbide47DAO.getInstance();
            return meLanbide47DAO.adjudicaOrientacion(ano, con);
        }
        catch(BDException e)
        {
            log.error("Se ha producido una excepci贸n en la BBDD ejectuando el proceso adjudica_orientacion para el a帽o " + ano, e);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido una excepci贸n en la BBDD ejectuando el proceso adjudica_orientacion para el a帽o " + ano, ex);
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
                log.error("Error al cerrar conexi贸n a la BBDD: " + e.getMessage());
            }
        }
    }
    
    public String consolidaHoras(AdaptadorSQLBD adaptador) throws Exception
    {
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            MeLanbide47DAO meLanbide47DAO = MeLanbide47DAO.getInstance();
            return meLanbide47DAO.consolidaHoras(con);
        }
        catch(BDException e)
        {
            log.error("Se ha producido una excepci贸n en la BBDD ejectuando el proceso consolida_horas", e);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido una excepci贸n en la BBDD ejectuando el proceso consolida_horas", ex);
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
                log.error("Error al cerrar conexi贸n a la BBDD: " + e.getMessage());
            }
        }
    }
    
    public String deshacerConsolidacionHoras(Integer ano, AdaptadorSQLBD adaptador) throws Exception
    {
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            MeLanbide47DAO meLanbide47DAO = MeLanbide47DAO.getInstance();
            return meLanbide47DAO.deshacerConsolidacionHoras(ano, con);
        }
        catch(BDException e)
        {
            log.error("Se ha producido una excepci贸n en la BBDD ejectuando el proceso elimina_consolidacion_centros", e);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido una excepci贸n en la BBDD ejectuando el proceso elimina_consolidacion_centros", ex);
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
                log.error("Error al cerrar conexi贸n a la BBDD: " + e.getMessage());
            }
        }
    }
    
    public EspecialidadesVO getEspecialidadPorCodigo(Integer codEsp, AdaptadorSQLBD adaptador) throws Exception
    {
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            MeLanbide47DAO meLanbide47DAO = MeLanbide47DAO.getInstance();
            return meLanbide47DAO.getEspecialidadPorCodigo(codEsp, con);
        }
        catch(BDException e)
        {
            log.error("Se ha producido una excepci贸n en la BBDD recuperando especialidad "+codEsp, e);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido una excepci贸n en la BBDD recuperando especialidad "+codEsp, ex);
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
                log.error("Error al cerrar conexi贸n a la BBDD: " + e.getMessage());
            }
        }
    }
    
    public BigDecimal getHorasAsignadasUbicacionORI(Integer codUbic, AdaptadorSQLBD adaptador) throws Exception
    {
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            MeLanbide47DAO meLanbide47DAO = MeLanbide47DAO.getInstance();
            return meLanbide47DAO.getHorasAsignadasUbicacionORI(codUbic, con);
        }
        catch(BDException e)
        {
            log.error("Se ha producido una excepci贸n en la BBDD recuperando horas adjudicadas para ubicacion "+codUbic, e);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido una excepci贸n en la BBDD recuperando horas adjudicadas para ubicacion "+codUbic, ex);
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
                log.error("Error al cerrar conexi贸n a la BBDD: " + e.getMessage());
            }
        }
    }
    
    public int actualizarValoracionTrayectoriaUbicaciones(EntidadVO entidad, Integer valor, AdaptadorSQLBD adaptador) throws Exception
    {
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            MeLanbide47DAO meLanbide47DAO = MeLanbide47DAO.getInstance();
            return meLanbide47DAO.actualizarValoracionTrayectoriaUbicaciones(entidad, valor, con);
        }
        catch(BDException e)
        {
            log.error("Se ha producido una excepci贸n en la BBDD actualizando trayectoria ubicaciones para entidad "+entidad.getOriEntCod(), e);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido una excepci贸n en la BBDD actualizando trayectoria ubicaciones para entidad "+entidad.getOriEntCod(), ex);
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
                log.error("Error al cerrar conexi贸n a la BBDD: " + e.getMessage());
            }
        }
    }
    
    public boolean entidadTieneUbicaciones(EntidadVO entidad, AdaptadorSQLBD adaptador) throws Exception
    {
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            MeLanbide47DAO meLanbide47DAO = MeLanbide47DAO.getInstance();
            return meLanbide47DAO.entidadTieneUbicaciones(entidad, con);
        }
        catch(BDException e)
        {
            log.error("Se ha producido una excepci贸n en la BBDD actualizando trayectoria ubicaciones para entidad "+entidad.getOriEntCod(), e);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido una excepci贸n en la BBDD actualizando trayectoria ubicaciones para entidad "+entidad.getOriEntCod(), ex);
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
                log.error("Error al cerrar conexi贸n a la BBDD: " + e.getMessage());
            }
        }
    }
    
    public List<OriUbicVO> getUbicacionesDeEntidadORI(EntidadVO entidad, AdaptadorSQLBD adaptador) throws Exception
    {
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            MeLanbide47DAO meLanbide47DAO = MeLanbide47DAO.getInstance();
            return meLanbide47DAO.getUbicacionesDeEntidadORI(entidad,false, con);
        }
        catch(BDException e)
        {
            log.error("Se ha producido una excepci贸n en la BBDD recuperando ubicaciones para entidad "+entidad.getOriEntCod(), e);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido una excepci贸n en la BBDD recuperando ubicaciones para entidad "+entidad.getOriEntCod(), ex);
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
                log.error("Error al cerrar conexi贸n a la BBDD: " + e.getMessage());
            }
        }
    }
    
    public int modificarUbicacionesORI(List<OriUbicVO> ubicList, AdaptadorSQLBD adaptador) throws Exception
    {
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            adaptador.inicioTransaccion(con);
            MeLanbide47DAO meLanbide47DAO = MeLanbide47DAO.getInstance();
            int cont = 0;
            OriUbicVO act = null;
            for(OriUbicVO ubic : ubicList)
            {
                act = meLanbide47DAO.guardarUbicacion(ubic, con);
                if(act != null)
                    cont++;
            }
            adaptador.finTransaccion(con);
            return cont;
        }
        catch(BDException e)
        {
            log.error("Se ha producido una excepci贸n en la BBDD guardando ubicaciones ", e);
            adaptador.rollBack(con);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido una excepci贸n en la BBDD recuperando ubicaciones", ex);
            adaptador.rollBack(con);
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
                log.error("Error al cerrar conexi贸n a la BBDD: " + e.getMessage());
            }
        }
    }
    
    public int crearAuditoria(AuditoriaVO aud, AdaptadorSQLBD adaptador) throws Exception
    {
        int retValue = 0;
        if(aud != null)
        {
            Connection con = null;
            try
            {
                con = adaptador.getConnection();
                MeLanbide47DAO meLanbide47DAO = MeLanbide47DAO.getInstance();
                retValue = meLanbide47DAO.crearAuditoria(aud, con);
            }
            catch(BDException e)
            {
                log.error("Se ha producido una excepci贸n al crear auditoria: "+aud.getUsuCod()+"/"+aud.getProcedimiento(), e);
                throw new Exception(e);
            }
            catch(Exception ex)
            {
                log.error("Se ha producido una excepci贸n en la BBDD recuperando ubicaciones", ex);
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
                    log.error("Error al cerrar conexi贸n a la BBDD: " + e.getMessage());
                }
            }
        }
        return retValue;
    }
    
    public List<FilaAuditoriaProcesosVO> filtrarAuditoriaProcesos(String nombre, Date feDesde, Date feHasta, Integer codProceso, String codProcedimiento, int codIdioma, AdaptadorSQLBD adaptador) throws Exception
    {
        List<FilaAuditoriaProcesosVO> retList = new ArrayList<FilaAuditoriaProcesosVO>();
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            MeLanbide47DAO meLanbide47DAO = MeLanbide47DAO.getInstance();
            retList = meLanbide47DAO.filtrarAuditoriaProcesos(nombre, feDesde, feHasta, codProceso, codProcedimiento, codIdioma, con);
        }
        catch(BDException e)
        {
            log.error("Se ha producido una excepcion recuperando auditoria de procesos", e);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido una excepcion recuperando auditoria de procesos", ex);
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
                log.error("Error al cerrar conexi贸n a la BBDD: " + e.getMessage());
            }
        }
        return retList;
    }
    
    public List<EntidadVO> getEntidadesQueNoEstanEnEstado(String ejercicio, String codProc, List<String> tramites, AdaptadorSQLBD adaptador) throws Exception
    {
        List<EntidadVO> retList = new ArrayList<EntidadVO>();
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            MeLanbide47DAO meLanbide47DAO = MeLanbide47DAO.getInstance();
            retList = meLanbide47DAO.getEntidadesQueNoEstanEnEstado(ejercicio, codProc, tramites, con);
        }
        catch(BDException e)
        {
            log.error("Se ha producido una excepcion recuperando auditoria de procesos", e);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido una excepcion recuperando auditoria de procesos", ex);
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
                log.error("Error al cerrar conexi贸n a la BBDD: " + e.getMessage());
            }
        }
        return retList;
    }
    
    public TerceroVO getTerceroPorCodigo(Long codTercero, AdaptadorSQLBD adaptador) throws Exception
    {
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            MeLanbide47DAO meLanbide47DAO = MeLanbide47DAO.getInstance();
            return meLanbide47DAO.getTerceroPorCodigo(codTercero, con);
        }
        catch(BDException e)
        {
            log.error("Se ha producido una excepcion en la BBDD recuperando tercero "+codTercero, e);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido una excepcion en la BBDD recuperando tercero "+codTercero, ex);
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
                log.error("Error al cerrar conexi贸n a la BBDD: " + e.getMessage());
            }
        }
    }
    
    public DomicilioVO getDomicilioTercero(TerceroVO ter, AdaptadorSQLBD adaptador) throws Exception
    {
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            MeLanbide47DAO meLanbide47DAO = MeLanbide47DAO.getInstance();
            return meLanbide47DAO.getDomicilioTercero(ter, con);
        }
        catch(BDException e)
        {
            log.error("Se ha producido una excepcion en la BBDD recuperando domicilio tercero "+ter.getTerDoc(), e);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido una excepcion en la BBDD recuperando domicilio tercero "+ter.getTerDoc(), ex);
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
                log.error("Error al cerrar conexi贸n a la BBDD: " + e.getMessage());
            }
        }
    }
    
    public TipoViaVO getTipoViaDomicilio(DomicilioVO dom, AdaptadorSQLBD adaptador) throws Exception
    {
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            MeLanbide47DAO meLanbide47DAO = MeLanbide47DAO.getInstance();
            return meLanbide47DAO.getTipoViaDomicilio(dom, con);
        }
        catch(BDException e)
        {
            log.error("Se ha producido una excepcion en la BBDD recuperando tipo via domicilio "+dom.getDom(), e);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido una excepcion en la BBDD recuperando tipo via domicilio "+dom.getDom(), ex);
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
                log.error("Error al cerrar conexi贸n a la BBDD: " + e.getMessage());
            }
        }
    }
    
    public ViaVO getViaDomicilio(DomicilioVO dom, AdaptadorSQLBD adaptador) throws Exception
    {
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            MeLanbide47DAO meLanbide47DAO = MeLanbide47DAO.getInstance();
            return meLanbide47DAO.getViaDomicilio(dom, con);
        }
        catch(BDException e)
        {
            log.error("Se ha producido una excepcion en la BBDD recuperando via domicilio "+dom.getDom(), e);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido una excepcion en la BBDD recuperando via domicilio "+dom.getDom(), ex);
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
                log.error("Error al cerrar conexi贸n a la BBDD: " + e.getMessage());
            }
        }
    }
    
    public TerceroVO getTerceroPorExpedienteYRol(int codOrganizacion, String numExpediente, String ejercicio, String rol, AdaptadorSQLBD adaptador) throws Exception
    {
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            MeLanbide47DAO meLanbide47DAO = MeLanbide47DAO.getInstance();
            return meLanbide47DAO.getTerceroPorExpedienteYRol(codOrganizacion, numExpediente, ejercicio, rol, con);
        }
        catch(BDException e)
        {
            log.error("Se ha producido una excepcion en la BBDD recuperando tercero con rol "+rol+" para expediente "+numExpediente, e);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido una excepcion en la BBDD recuperando tercero con rol "+rol+" para expediente "+numExpediente, ex);
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
                log.error("Error al cerrar conexi贸n a la BBDD: " + e.getMessage());
            }
        }
    }
    
    public List<OriUbicVO> getUbicacionesAdjudicadasDeEntidadORI(EntidadVO entidad, AdaptadorSQLBD adaptador) throws Exception
    {
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            MeLanbide47DAO meLanbide47DAO = MeLanbide47DAO.getInstance();
            return meLanbide47DAO.getUbicacionesAdjudicadasDeEntidadORI(entidad, con);
        }
        catch(BDException e)
        {
            log.error("Se ha producido una excepci贸n en la BBDD recuperando ubicaciones para entidad "+entidad.getOriEntCod(), e);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido una excepci贸n en la BBDD recuperando ubicaciones para entidad "+entidad.getOriEntCod(), ex);
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
                log.error("Error al cerrar conexi贸n a la BBDD: " + e.getMessage());
            }
        }
    }
    
    public String getCodigoInternoTramite(int codOrganizacion, String codProc, String codTramite, AdaptadorSQLBD adaptador) throws Exception
    {
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            MeLanbide47DAO meLanbide47DAO = MeLanbide47DAO.getInstance();
            return meLanbide47DAO.getCodigoInternoTramite(codOrganizacion, codProc, codTramite, con);
        }
        catch(BDException e)
        {
            log.error("Se ha producido una excepci贸n en la BBDD recuperando el c骴igo interno del tr醡ite "+codTramite, e);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido una excepci贸n en la BBDD recuperando el c骴igo interno del tr醡ite "+codTramite, ex);
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
                log.error("Error al cerrar conexi贸n a la BBDD: " + e.getMessage());
            }
        }
    }
    
    public boolean expedienteEstaCerradoOAnulado(int codOrganizacion, String numExpediente, String ejercicio, AdaptadorSQLBD adaptador) throws Exception
    {
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            MeLanbide47DAO meLanbide47DAO = MeLanbide47DAO.getInstance();
            return meLanbide47DAO.expedienteEstaCerradoOAnulado(codOrganizacion, numExpediente, ejercicio, con);
        }
        catch(BDException e)
        {
            log.error("Se ha producido una error recuperando el estado del expediente "+numExpediente, e);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error recuperando el estado del expediente "+numExpediente, ex);
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
                log.error("Error al cerrar conexi贸n a la BBDD: " + e.getMessage());
            }
        }
    }
    
    public Long getCodigoUltimoTramiteAbierto(int codOrganizacion, String ejercicio, String codProc, String numExp, Long ocurrencia, AdaptadorSQLBD adaptador) throws Exception
    {
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            MeLanbide47DAO meLanbide47DAO = MeLanbide47DAO.getInstance();
            return meLanbide47DAO.getCodigoUltimoTramiteAbierto(codOrganizacion, ejercicio, codProc, numExp, ocurrencia, con);
        }
        catch(BDException e)
        {
            log.error("Se ha producido una excepci髇 en la BBDD recuperando el tramite abierto ("+numExp+")", e);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido una excepci髇 en la BBDD recuperando el tramite abierto ("+numExp+")", ex);
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
                log.error("Error al cerrar conexi髇 a la BBDD: " + e.getMessage());
            }
        }
    }
    
    public AsociacionVO getAsociacionPorCodigoYEntidad(AsociacionVO asoc, AdaptadorSQLBD adaptador) throws Exception
    {
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            MeLanbide47DAO meLanbide47DAO = MeLanbide47DAO.getInstance();
            return meLanbide47DAO.getAsociacionPorCodigoYEntidad(asoc, con);
        }
        catch(BDException e)
        {
            log.error("Se ha producido una excepcion en la BBDD recuperando asociacion ", e);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido una excepcion en la BBDD recuperando asociacion ", ex);
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
                log.error("Error al cerrar conexi贸n a la BBDD: " + e.getMessage());
            }
        }
    }
    
    public AsociacionVO guardarAsociacion(EntidadVO entidadEjemplo, AsociacionVO asociacionEjemplo,  int idioma, AdaptadorSQLBD adapt)throws Exception
    {
        boolean result = true;
        EntidadVO entidadBD = new EntidadVO();
        Connection con = null;
        try
        {
            con = adapt.getConnection();
            adapt.inicioTransaccion(con);
            
            //Recupero la entidad
            entidadBD = MeLanbide47DAO.getInstance().getEntidadPorCodigoYExpediente(entidadEjemplo, idioma, con);
            
            if(entidadBD == null)
            {
                try
                {
                    //Si no existe la entidad, hay que crearla lo primero
                    HashMap<String, String> map = MeLanbide47Manager.getInstance().getDatosTercero(entidadEjemplo.getExtMun(), entidadEjemplo.getExtNum(), entidadEjemplo.getExtEje(), adapt);
                    try
                    {
                        entidadEjemplo.setExtNvr(Integer.parseInt(map.get("EXT_NVR")));
                        entidadEjemplo.setExtTer(Long.parseLong(map.get("EXT_TER")));
                    }
                    catch(Exception ex)
                    {
                        
                    }
                    entidadBD = MeLanbide47Manager.getInstance().guardarEntidad(entidadEjemplo, adapt);
                    asociacionEjemplo.setOriEntCod(entidadBD.getOriEntCod());
                }
                catch(Exception ex)
                {
                    result = false;
                    entidadBD = null;
                }
            }
            else
            {
                //Miro si cambia el flag de asociacion
                if(entidadBD.getOriEntAsociacion() == null || entidadBD.getOriEntAsociacion() == 0)
                {
                    //Si cambia, elimino todas las asociaciones
                    try
                    {
                        //Eliminar las asociaciones
                        result = MeLanbide47DAO.getInstance().eliminarAsociacionesDeEntidad(entidadBD, con);
                    }
                    catch(Exception ex)
                    {
                        result = false;
                    }
                }
                //Si la entidad ya existia, simplemente la actualizamos
                entidadBD.setOriEntAsociacion(entidadEjemplo.getOriEntAsociacion());
                entidadBD.setOriEntNom(entidadEjemplo.getOriEntNom());

                entidadBD = MeLanbide47DAO.getInstance().guardarEntidad(entidadBD, con);
            }

            if(result)
            {
                if(entidadBD != null)
                {
                    asociacionEjemplo.setOriEntCod(entidadBD.getOriEntCod());
                    //Si ha guardado bien la entidad, habra que mirar si es asociacion
                    //En caso de que no sea asociacion, se crea un registro en la tabla asociacion automaticamente
                    asociacionEjemplo = MeLanbide47DAO.getInstance().guardarAsociacion(asociacionEjemplo, con);
                }
            }
            adapt.finTransaccion(con);
            return asociacionEjemplo;
        }
        catch(BDException e)
        {
            log.error("Se ha producido una excepcion en la BBDD guardando datos ORI", e);
            adapt.rollBack(con);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido una excepcion en la BBDD guardando datos ORI", ex);
            adapt.rollBack(con);
            throw new Exception(ex);
        }
        finally
        {
            try
            {
                adapt.devolverConexion(con);       
            }
            catch(Exception e)
            {
                log.error("Error al cerrar conexi贸n a la BBDD: " + e.getMessage());
            }
        }
    }
    
    public int eliminarAsociacion(AsociacionVO asoc, AdaptadorSQLBD adaptador) throws Exception
    {
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            MeLanbide47DAO meLanbide47DAO = MeLanbide47DAO.getInstance();
            return meLanbide47DAO.eliminarAsociacion(asoc, con);
        }
        catch(BDException e)
        {
            log.error("Se ha producido una excepcion en la BBDD creando asociacion ", e);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido una excepcion en la BBDD eliminando asociacion "+(asoc != null ? asoc.getOriAsocCod() : "(asoc = null)")+" para la entidad "+(asoc != null ? asoc.getOriEntCod() : "(asoc = null)"), ex);
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
                log.error("Error al cerrar conexi贸n a la BBDD: " + e.getMessage());
            }
        }
    }
    
    public List<FilaAsociacionVO> getListaAsociacionesPorEntidad(EntidadVO entidad, AdaptadorSQLBD adaptador) throws Exception
    {
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            MeLanbide47DAO meLanbide47DAO = MeLanbide47DAO.getInstance();
            return meLanbide47DAO.getListaAsociacionesPorEntidad(entidad, con);
        }
        catch(BDException e)
        {
            log.error("Se ha producido una excepcion en la BBDD recuperando asociaciones para entidad "+(entidad != null ? entidad.getOriEntCod() : "(entidad = null)")+" del expediente "+(entidad != null ? entidad.getExtNum() : "(entidad = null)"), e);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido una excepcion en la BBDD recuperando asociaciones para entidad "+(entidad != null ? entidad.getOriEntCod() : "(entidad = null)")+" del expediente "+(entidad != null ? entidad.getExtNum() : "(entidad = null)"), ex);
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
                log.error("Error al cerrar conexi贸n a la BBDD: " + e.getMessage());
            }
        }
    }
    
    public boolean asociacionRepetida(EntidadVO ent, AsociacionVO asoc,  int idioma,AdaptadorSQLBD adapt) throws Exception
    {
        boolean haCambiado = false;
        Connection con = null;
        EntidadVO entBD = null;
        try
        {
            con = adapt.getConnection();
            if(ent != null && ent.getExtNum() != null)
            {
                if(ent.getOriEntCod() != null)
                {
                    entBD = MeLanbide47DAO.getInstance().getEntidadPorCodigoYExpediente(ent, idioma, con);
                    if(entBD != null)
                    {
                        if(entBD.getOriEntAsociacion() != null && ent.getOriEntAsociacion() != null)
                        {
                            haCambiado = entBD.getOriEntAsociacion().equals(ent.getOriEntAsociacion());
                        }
                        else if(entBD.getOriEntAsociacion() != null || ent.getOriEntAsociacion() != null)
                        {
                            haCambiado = true;
                        }
                    }
                }
                else
                {
                    haCambiado = true;
                }
            }
            if(haCambiado && asoc != null && asoc.getOriAsocCif() != null && !asoc.getOriAsocCif().equals(""))
            {
                return MeLanbide47DAO.getInstance().asociacionRepetida(entBD, asoc, con);
            }
            else
            {
                return false;
            }
        }
        catch(Exception ex)
        {
                    log.error("Se ha producido una excepcion en la BBDD comprobando asociaciones repetidas", ex);
                    throw new Exception(ex);
        }
        finally
        {
            try
            {
                adapt.devolverConexion(con);       
            }
            catch(Exception e)
            {
                log.error("Error al cerrar conexi贸n a la BBDD: " + e.getMessage());
            }
        }
    }
    
    public EntidadVO guardarDatosORI(EntidadVO entidadEjemplo, AsociacionVO asocEjemplo, boolean nuevaCon,  int idioma,AdaptadorSQLBD adapt) throws Exception
    {
        boolean result = true;
        //Recupero la entidad asociada al expediente
        EntidadVO entidadBD = new EntidadVO();
        Connection con = null;
        boolean nuevaEntidad = false;
        try
        {
            con = adapt.getConnection();
            adapt.inicioTransaccion(con);
            entidadBD = MeLanbide47DAO.getInstance().getEntidadPorCodigoYExpediente(entidadEjemplo, idioma, con);

            if(entidadBD == null)
            {
                nuevaEntidad = true;
                //Si no existe la entidad, entonces hay que crearla
                entidadBD = new EntidadVO();
                entidadBD.setExtEje(entidadEjemplo.getExtEje());
                entidadBD.setExtMun(entidadEjemplo.getExtMun());
                entidadBD.setExtNum(entidadEjemplo.getExtNum());
                HashMap<String, String> map = MeLanbide47Manager.getInstance().getDatosTercero(entidadEjemplo.getExtMun(), entidadEjemplo.getExtNum(), entidadEjemplo.getExtEje(), adapt);
                try
                {
                    entidadBD.setExtNvr(Integer.parseInt(map.get("EXT_NVR")));
                    entidadBD.setExtTer(Long.parseLong(map.get("EXT_TER")));
                }
                catch(Exception ex)
                {

                }
                entidadBD.setOriEntNom(entidadEjemplo.getOriEntNom());
            }

            try
            {
                //Si ya existia, hay que comprobar si ha cambiado el dato "Es asociacion".
                //En caso de que haya cambiado, habr韆 que eliminar las asociaciones de la entidad
                boolean haCambiado = false;
                if(entidadEjemplo.getOriEntAsociacion() != null && entidadBD.getOriEntAsociacion() != null && entidadEjemplo.getOriEntAsociacion() != entidadBD.getOriEntAsociacion())
                {
                    haCambiado = true;
                }

                if(haCambiado)
                {
                    try
                    {
                        //Eliminar las asociaciones
                        result = MeLanbide47DAO.getInstance().eliminarAsociacionesDeEntidad(entidadBD, con);
                    }
                    catch(Exception ex)
                    {
                        result = false;
                    }
                }

                if(result)
                {
                    //Si ha ido todo bien hasta aqui, guardamos/actualizamos la entidad
                    entidadBD.setOriEntAsociacion(entidadEjemplo.getOriEntAsociacion());
                    entidadBD.setOriEntNom(entidadEjemplo.getOriEntNom());

                    entidadBD = MeLanbide47DAO.getInstance().guardarEntidad(entidadEjemplo, con);
                    if(entidadBD == null)
                    {
                        result = false;
                    }
                    else 
                    {
                        //2017/08/08 Hay que mirar si no ha cambiado, no es nueva y no es aociacion, puede ser un aactualizacion de datos de la entidad
                        //En esta caso hay que psar el codigo de la asociacion para que actualice los datos.
                        boolean actualizaDatosEntidadNoAsociacion = false;
                        long codigoAsocEntidadNoAsociacion=0;
                        if(!nuevaEntidad && !haCambiado && entidadBD.getOriEntAsociacion() != null && entidadBD.getOriEntAsociacion() == 0){
                            codigoAsocEntidadNoAsociacion = MeLanbide47DAO.getInstance().getCodigoAsociacionEntidadNoAsociacionxCodEntidad(entidadBD.getOriEntCod(), con);
                            // Si es cero hubo un error o hay mas de un aliena en la tabla asociacion con el mismo codentidad
                            if(codigoAsocEntidadNoAsociacion!=0){
                                actualizaDatosEntidadNoAsociacion=true;
                            }
                        }
                        
                        if(nuevaEntidad || (haCambiado && entidadBD.getOriEntAsociacion() != null && entidadBD.getOriEntAsociacion() == 0) || actualizaDatosEntidadNoAsociacion)
                        {
                            //Si ha guardado bien la entidad, habra que mirar si es asociacion
                            //En caso de que no sea asociacion, se crea un registro en la tabla asociacion automaticamente
                            AsociacionVO asoc = new AsociacionVO();
                            // Ponemos el codigo de la asoiciacion par las update de entidaddes
                            if(actualizaDatosEntidadNoAsociacion)
                                asoc.setOriAsocCod(codigoAsocEntidadNoAsociacion);
                            asoc.setOriEntCod(entidadEjemplo.getOriEntCod());
                            asoc.setOriAsocCif(asocEjemplo.getOriAsocCif());
                            asoc.setOriAsocNombre(asocEjemplo.getOriAsocNombre());
                            asoc.setOriEntAdmLocal(asocEjemplo.getOriEntAdmLocal());
                            asoc.setOriEntSupramun(asocEjemplo.getOriEntSupramun());
                            asoc.setOriExpCentrofpPriv(asocEjemplo.getOriExpCentrofpPriv());
                            asoc.setOriExpCentrofpPub(asocEjemplo.getOriExpCentrofpPub());
                            asoc.setAgenciaColocacion(asocEjemplo.getAgenciaColocacion());
                            asoc.setOriExpSinAnimoLucro(asocEjemplo.getOriExpSinAnimoLucro());
                            asoc.setPlanIgualdad(asocEjemplo.getPlanIgualdad());
                            asoc.setCertificadoCalidad(asocEjemplo.getCertificadoCalidad());
                            asoc.setNumAgenciaColocacion(asocEjemplo.getNumAgenciaColocacion());
                            asoc.setPlanIgualdadVal(asocEjemplo.getPlanIgualdadVal());
                            asoc.setCertificadoCalidadVal(asocEjemplo.getCertificadoCalidadVal());
                            asoc.setNumAgenciaColocacionVal(asocEjemplo.getNumAgenciaColocacionVal());
                            log.debug("asoc.planIgualdad+++++++: "+asoc.getPlanIgualdad());
                            log.debug("asoc.certificadoCalidad+++++++: "+asoc.getCertificadoCalidad());
                            log.debug("asoc.numAgenciaColocacion+++++++: "+asoc.getNumAgenciaColocacion());
                            asoc.setEntSujetaDerPubl(asocEjemplo.getEntSujetaDerPubl());
                            asoc.setCompIgualdadOpcion(asocEjemplo.getCompIgualdadOpcion());
                            asoc.setEntSujetaDerPublVal(asocEjemplo.getEntSujetaDerPublVal());
                            asoc.setCompIgualdadOpcionVal(asocEjemplo.getCompIgualdadOpcionVal());
                            asoc = MeLanbide47DAO.getInstance().guardarAsociacion(asoc, con);
                            if(asoc == null)
                            {
                                result = false;
                            }
                        }
                        
                        //Actualizamos todas las ubicaciones con la trayectoria validada
                        result = actualizarValoracionUbicaciones(entidadBD, nuevaCon, con);
                    }
                }
            }
            catch(Exception ex)
            {
                ex.printStackTrace();
                result = false;
            }

            adapt.finTransaccion(con);
            
            return result ? entidadBD : null;
        }
        catch(BDException e)
        {
            log.error("Se ha producido una excepcion en la BBDD guardando datos ORI", e);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido una excepcion en la BBDD guardando datos ORI", ex);
            throw new Exception(ex);
        }
        finally
        {
            try
            {
                adapt.devolverConexion(con);       
            }
            catch(Exception e)
            {
                log.error("Error al cerrar conexi髇 a la BBDD: " + e.getMessage());
            }
        }
    }
            
    private boolean actualizarValoracionUbicaciones(EntidadVO entidad, boolean nuevaCon, Connection con)
    {
        try
        {
            List<OriUbicVO> uList = MeLanbide47DAO.getInstance().getUbicacionesDeEntidadORI(entidad, nuevaCon, con);
            //A駉s trayectoria
            Double anosMeses = entidad.getOriEntTrayectoriaVal();
            //anos = anos - 2;
            BigDecimal valoracionTrayectoria =  new BigDecimal("0.0"); //entidad.getOriValoracionTrayectoria();
            BigDecimal total = new BigDecimal("0.0");
            OriUbicVO act = null;
            int actualizados = 0;
            if(uList.size() > 0)
            {
                for(OriUbicVO ubic : uList)
                {
                    // 2017/08/08 Cuando se guardan datos de entidad, se reclaculan los valores de la puntuacion. 
                    // Aunque los conceptos para convicatoria a partir de 2017 cambiaron de Despachos --> Disp. 1 Espacio Adicional y Aula Grupal --> Espacio comp. con ordenador, internet wifi
                    // los puntos obtenidos son los mismo. Reutilizamos el metodo de caluclo solo pasando los parametros correctos.
                    // Si cambian la puntuacion hay que cambiar el metodo o parametrizar los valores de la puntacion.
                    String comodinDespachosDisp1EspAdicional = "";
                    String comodinAulaGrupaDisp1EspOrdInterWifi = "";
                    
                    String numeroExpediente = entidad.getExtNum();
                    int anioExpediente = (numeroExpediente != null && !numeroExpediente.equalsIgnoreCase("") ? Integer.parseInt(numeroExpediente.substring(0,4)):0);
                    if(anioExpediente>=2017){
                        comodinDespachosDisp1EspAdicional=ubic.getOriOrientUbicaEspacioAdicionalVal();
                        comodinAulaGrupaDisp1EspOrdInterWifi=ubic.getOriOrientUbicaEspAdicioHerrBusqEmpleoVal();
                    }else{
                        comodinDespachosDisp1EspAdicional=ubic.getOriOrientDespachosValidados();
                        comodinAulaGrupaDisp1EspOrdInterWifi=ubic.getOriOrientAulaGrupalValidada();
                    } 
                    if (nuevaCon) {
                        total = calcularValoracion21(entidad.getOriEntTrayectoriaVal(), ubic.getOriOrientValUbic(), comodinDespachosDisp1EspAdicional, comodinAulaGrupaDisp1EspOrdInterWifi, entidad.getPlanIgualdadVal(), entidad.getCertificadoCalidadVal());
                        valoracionTrayectoria = new BigDecimal(getPuntuacionTrayectoria21(anosMeses));
                    } else {
                        total = this.calcularValoracion(entidad.getOriEntTrayectoriaVal(), ubic.getOriOrientValUbic(), comodinDespachosDisp1EspAdicional, comodinAulaGrupaDisp1EspOrdInterWifi);
                        Double trayVal = getPuntuacionTrayectoriaAnos(anosMeses);
                        valoracionTrayectoria = new BigDecimal(trayVal);
                    }

                    ubic.setOriOrientValTray(valoracionTrayectoria);
                    ubic.setOriOrientPuntuacion(total);
                    act = MeLanbide47DAO.getInstance().guardarUbicacion(ubic, con);
                    if(act != null)
                    {
                        actualizados++;
                    }
                }
                return actualizados == uList.size();
            }
            else
            {
                return true;
            }
        }
        catch(Exception ex)
        {
            return false;
        }
    }
    
    private BigDecimal calcularValoracion(Double trayectoriaVal,BigDecimal ubicVal,String numDespachos,String aulaVal)
    {
        Double total = 0.0;
        //Trayectoria
        try{
            //trayectoriaVal = (trayectoriaVal - 2) * 2;
            if(trayectoriaVal < 0)
            {
                trayectoriaVal = 0D;
            }
            else if(trayectoriaVal > 10){
                trayectoriaVal = 10D;
            }
            total += trayectoriaVal;
        }
        catch(Exception ex)
        {

        }

        //Ubicacion
        try
        {
            Double ubicacion = ubicVal.doubleValue();
            total += ubicacion;
        }
        catch(Exception ex)
        {

        }

        //Numero despachos
        try
        {
            if(numDespachos != null && numDespachos.equalsIgnoreCase(ConstantesMeLanbide47.SI))
            {
                total += 4;
            }
        }
        catch(Exception ex)
        {

        }

        //Aula grupal
        try
        {
            if(aulaVal != null && aulaVal.equalsIgnoreCase(ConstantesMeLanbide47.SI))
            {
                total += 1;
            }
        }
        catch(Exception ex)
        {

        }
        return new BigDecimal(total.toString());
    }
    
    public OriUbicVO guardarUbicacion(EntidadVO entidadEjemplo, OriUbicVO ubicacionEjemplo, boolean nuevaCon,  int idioma, AdaptadorSQLBD adapt) throws Exception {
        boolean result = true;
        EntidadVO entidadBD = new EntidadVO();
        Connection con = null;
        try {
            con = adapt.getConnection();
            adapt.inicioTransaccion(con);

            //Recupero la entidad
            entidadBD = MeLanbide47DAO.getInstance().getEntidadPorCodigoYExpediente(entidadEjemplo,  idioma, con);

            if (entidadBD == null) {
                try {
                    //Si no existe la entidad, hay que crearla lo primero
                    HashMap<String, String> map = MeLanbide47Manager.getInstance().getDatosTercero(entidadEjemplo.getExtMun(), entidadEjemplo.getExtNum(), entidadEjemplo.getExtEje(), adapt);
                    try {
                        entidadEjemplo.setExtNvr(Integer.parseInt(map.get("EXT_NVR")));
                        entidadEjemplo.setExtTer(Long.parseLong(map.get("EXT_TER")));
                        entidadEjemplo.setOriEntNom(map.get("HTE_NOC"));
                    } catch (Exception ex) {

                    }
                    entidadBD = MeLanbide47Manager.getInstance().guardarEntidad(entidadEjemplo, adapt);
                    // Una vez creada la entidad debemos crear la L韓ea en la tabla de asociaci髇 para que relaciones las trayectorias 
                    log.debug("Entidad Creada desde GuardarUbicacion " + (entidadBD != null ? entidadBD.getOriEntCod() + "/" + entidadBD.getOriEntNom() : ""));
                    //2017/09/26
                    AsociacionVO asociacionVO = new AsociacionVO();
                    asociacionVO.setOriEntCod(entidadBD.getOriEntCod());
                    asociacionVO.setOriAsocCif(map.get("HTE_DOC"));
                    asociacionVO.setOriAsocNombre(map.get("HTE_NOC"));
                    asociacionVO = MeLanbide47DAO.getInstance().guardarAsociacion(asociacionVO, con);
                    log.debug("Asociacion Creada desde GuardarUbicacion " + (asociacionVO != null ? asociacionVO.getOriAsocCod() + "/" + asociacionVO.getOriEntCod() + "-" + asociacionVO.getOriAsocCif() + "-" + asociacionVO.getOriAsocNombre() : ""));
                    // Fin creaci髇 Fila Tabla asociavion
                    ubicacionEjemplo.setOriEntCod(entidadBD.getOriEntCod());
                } catch (Exception ex) {
                    result = false;
                    entidadBD = null;
                }
            }

            if (result) {
                if (entidadBD != null) {
                    //Si ha guardado bien la entidad, habra que mirar si es asociacion
                    //En caso de que no sea asociacion, se crea un registro en la tabla asociacion automaticamente
                    if (ubicacionEjemplo.getOriOrientUbicCod() == null) {
                        //Si es una ubicacion nueva, le meto la trayectoria validada de la entidad
                        BigDecimal tray = new BigDecimal(getPuntuacionTrayectoria21(entidadBD.getOriEntTrayectoriaVal()));                       
                        ubicacionEjemplo.setOriOrientValTray(tray);
                    }
                    ubicacionEjemplo = MeLanbide47DAO.getInstance().guardarUbicacion(ubicacionEjemplo, con);
                }
            }
            adapt.finTransaccion(con);
            return ubicacionEjemplo;
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD guardando datos ORI", e);
            adapt.rollBack(con);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD guardando datos ORI", ex);
            adapt.rollBack(con);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexi贸n a la BBDD: " + e.getMessage());
            }
        }
    }


    // Sobrecarga para caso de los datos validados
    public List<OriTrayectoriaVO> getListaTrayectoriasPorExpediente(OriTrayectoriaVO trayEjemplo, AdaptadorSQLBD adaptador) throws Exception{
        return getListaTrayectoriasPorExpediente(trayEjemplo,adaptador,false);
    }
    public List<OriTrayectoriaVO> getListaTrayectoriasPorExpediente(OriTrayectoriaVO trayEjemplo, AdaptadorSQLBD adaptador,boolean sonDatosValidados) throws Exception
    {
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            return MeLanbide47DAO.getInstance().getListaTrayectoriasPorExpediente(trayEjemplo, con,sonDatosValidados);
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
            }
        } 
    }
    
    
    // Creamos sobrecarga para en el DAO Guardar/Modificar/ Sobre la tabla de Validacion o Datos Presentado
    public OriTrayectoriaVO getTrayectoriaPorAsociacionYExpediente(OriTrayectoriaVO tray, AdaptadorSQLBD adaptador) throws Exception{
        return getTrayectoriaPorAsociacionYExpediente(tray,adaptador,false);
    }
    public OriTrayectoriaVO getTrayectoriaPorAsociacionYExpediente(OriTrayectoriaVO tray, AdaptadorSQLBD adaptador, boolean sonDatosValidados) throws Exception
    {
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            return MeLanbide47DAO.getInstance().getTrayectoriaPorAsociacionYExpediente(tray, con,sonDatosValidados);
        }
        catch(BDException e)
        {
            log.error("Se ha producido una excepcion en la BBDD recuperando trayectoria para entidad "+(tray != null ? tray.getOriAsocCod() : "(tray = null)")+" y el expediente "+(tray != null ? tray.getNumExp() : " (tray = null)"), e);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido una excepcion en la BBDD recuperando trayectoria para entidad "+(tray != null ? tray.getOriAsocCod() : "(tray = null)")+" y el expediente "+(tray != null ? tray.getNumExp() : " (tray = null)"), ex);
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
    
    //Sobrecarga para gestion de nuevo apartado de validacion. Sera true si viene de la jsp de validacion hay que guardar y leer de otra tabla
    public boolean guardarTrayectoriaAsociaciones(List<OriTrayectoriaVO> listaTrayectorias, AdaptadorSQLBD adaptador) throws Exception{
        return guardarTrayectoriaAsociaciones(listaTrayectorias,adaptador,false);
    }
    public boolean guardarTrayectoriaAsociaciones(List<OriTrayectoriaVO> listaTrayectorias, AdaptadorSQLBD adaptador, boolean sonDatosValidados) throws Exception
    {
        Connection con = null;
        boolean correcto = true;
        int i = 0;
        try
        {
            con = adaptador.getConnection();
            adaptador.inicioTransaccion(con);
            OriTrayectoriaVO tray = null;
            while(correcto && i < listaTrayectorias.size())
            {
                tray = listaTrayectorias.get(i);
                correcto = MeLanbide47DAO.getInstance().guardarTrayectoriaAsociacion(tray, con,sonDatosValidados);
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
            log.error("Se ha producido una excepcion en la BBDD la trayectoria de una o m醩 entidades", e);
            adaptador.rollBack(con);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido una excepcion en la BBDD la trayectoria de una o m醩 entidades", ex);
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
    
    private Double getPuntuacionTrayectoriaAnos(Double anos)
    {
        log.debug("getPuntuacionTrayectoriaAnos : BEGIN() ");
        Double trayVal = 0D;
        log.debug("getPuntuacionTrayectoriaAnos : paramEntrada:"+anos);
        try
        {
            //trayVal = (anos - 2)*2;
            trayVal = anos;
            if(trayVal < 0)
            {
                trayVal = 0D;
            }
            else if(trayVal > 10)
            {
                trayVal = 10D;
            }
        }
        catch(Exception ex)
        {
            trayVal = 0D;
            log.error("getPuntuacionTrayectoriaAnos : Error al calcular puntuacion a駉s trayectoria Validada -. SI valor es null(Puntuaci髇 por a駉s validados, no se ha cumplimentado. Asignamos 0)  : ParaEntrada:" + anos + ". ParamReturn:"+trayVal,ex);
        }
        log.debug("getPuntuacionTrayectoriaAnos : END() --> " + trayVal);
        return trayVal;
    }

    private double getPuntuacionTrayectoria21(Double meses) {
        log.debug("getPuntuacionTrayectoria21 " + meses);
        double puntuacion = 0;
        double puntos = 0.2;
        try {
            puntuacion = puntos * meses;
            if (puntuacion > 12.0) {
                puntuacion = 12.0;
            }
        } catch (Exception ex) {
            puntuacion = 0;
            log.error("getPuntuacionTrayectoria21 : Error al calcular puntuacion a駉s trayectoria Validada -. SI valor es null(Puntuaci髇 por meses validados, no se ha cumplimentado. Asignamos 0)  : ParaEntrada: " + meses + ". ParamReturn: " + puntuacion, ex);
        }
        return puntuacion;
    }
    public Boolean expteEstaTramiteResolProvisional(String numExp, AdaptadorSQLBD adaptador) throws Exception {
        log.debug("expteEstaTramiteResolProvisional - Begin() - Manager");
        Connection con = null;
        try {
            con = adaptador.getConnection();
            return MeLanbide47DAO.getInstance().expteEstaTramiteResolProvisional(numExp, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD validando si expediente esta en resolucion provisional " + numExp, e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD validando si expediente esta en resolucion provisional " + numExp, ex);
            throw new Exception(ex);
        } finally {
            try {
                adaptador.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    public String getFechaRegistroExpediente(String numExp,String codPro, AdaptadorSQLBD adaptador) throws Exception {
        log.debug("getFechaRegistroExpediente - Begin() - Manager");
        Connection con = null;
        try {
            con = adaptador.getConnection();
            return MeLanbide47DAO.getInstance().getFechaRegistroExpediente(numExp,codPro, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD obteniendo fehca de registro expediente " + numExp, e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD obteniendo fehca de registro expediente  " + numExp, ex);
            throw new Exception(ex);
        } finally {
            try {
                adaptador.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    public List<OriTrayOtroProgramaVO> getListaTrayectoriaOtrosProgramas(OriTrayOtroProgramaVO trayOtro, AdaptadorSQLBD adapt) throws Exception {
         log.debug("getListaTrayectoriaOtrosProgramasxNumExp - Begin()");
        Connection con = null;
        try {
            con = adapt.getConnection();
            return MeLanbide47DAO.getInstance().getListaTrayectoriaOtrosProgramasxNumExp(trayOtro, con);
        } catch (BDException e) {
              log.error("Se ha producido una excepcion en la BBDD recuperando Otros Programas para el expediente " + (trayOtro != null ? trayOtro.getNumExpediente(): " (trayOtro = null)"), e);
            throw new Exception(e);
        }catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD recuperando Otros Programas para el expediente " + (trayOtro != null ? trayOtro.getNumExpediente(): " (trayOtro = null)"), ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    public OriTrayOtroProgramaVO getTrayectoriaOtrosProgramas(OriTrayOtroProgramaVO tray, AdaptadorSQLBD adaptador) throws Exception{
    Connection con = null;
        try {
            con = adaptador.getConnection();
            return MeLanbide47DAO.getInstance().getTrayectoriaOtrosProgramas(tray,con);
        }  catch (BDException e) {
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

    public List<SelectItem> getListaSelectItemEntidadesPorExpediente(String numExpediente, AdaptadorSQLBD adaptador) throws Exception
    {
        Connection con = null;
        try
        {
            con=adaptador.getConnection();
            MeLanbide47DAO meLanbide47DAO = MeLanbide47DAO.getInstance();
            return meLanbide47DAO.getListaSelectItemEntidadesPorExpediente(numExpediente, con);
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

    public boolean guardarTrayectoriaOtrosProgramas(OriTrayOtroProgramaVO tray, AdaptadorSQLBD adaptador) throws Exception{
          log.debug("guardarTrayectoriaOtrosProgramas - Manager - Begin()");
        Connection con = null;
        boolean exito=true;
        try {
            con = adaptador.getConnection();
            adaptador.inicioTransaccion(con);
            tray = MeLanbide47DAO.getInstance().guardarTrayectoriaOtrosProgramas(tray,con);
            if (tray == null) {
                exito = false;
                log.debug("No se han guardado los datos correctamente de la trayectoria  otros programas. Se ha devuelto el objeto OriTrayOtroProgramaVO a null");
            }
            adaptador.finTransaccion(con);
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD guardarTrayectoriaOtrosProgramas ", e);
            adaptador.rollBack(con);
            throw new Exception(e);
        }catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD guardarTrayectoriaOtrosProgramas ", ex);
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

    public void eliminarTrayectoriaOtrosProgramas(OriTrayOtroProgramaVO tray, AdaptadorSQLBD adaptador)throws Exception  {
       log.debug("eliminarTrayectoriaOtrosProgramas() - MANAGER - Begin()");
        Connection con = null; 
        try {
            con = adaptador.getConnection();
            adaptador.inicioTransaccion(con);
            MeLanbide47DAO meLanbide47DAO = MeLanbide47DAO.getInstance();
            if(tray!=null){
                int eliminados = meLanbide47DAO.eliminarTrayectoriaOtrosProgramas(tray,con);
                if (eliminados > 0) {
                    log.debug("Se han eliminado " + eliminados + "Registros para el expediente/codEntidad/CodTray :" + tray != null ? tray.getNumExpediente()+ "/" + tray.getCodEntidad() + "/" + tray.getCodIdOtroPrograma(): "");
                } else {
                    log.debug("No hab韆n registros para eliminar : " + eliminados + ", para el expediente/codEntidad/CodTray :" + tray != null ? tray.getNumExpediente()+ "/" + tray.getCodEntidad() + "/" + tray.getCodIdOtroPrograma(): "");
                }
                adaptador.finTransaccion(con);
            }else {
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

    }

    public List<OriTrayActividadVO> getListaTrayectoriaActividadesXNumExp(OriTrayActividadVO trayActividad, AdaptadorSQLBD adapt) throws Exception {
         log.debug("getListaTrayectoriaActividadesxNumExp - Begin()");
        Connection con = null;
        try {
            con = adapt.getConnection();
            return MeLanbide47DAO.getInstance().getListaTrayectoriaActividadesXNumExp(trayActividad, con);
        }  catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD recuperando trayectorias para el expediente " + (trayActividad != null ? trayActividad.getNumExpediente(): " (trayActividad = null)"), e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD recuperando trayectorias para el expediente " + (trayActividad != null ? trayActividad.getNumExpediente(): " (trayActividad = null)"), ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    public OriTrayActividadVO getTrayectoriaActividades(OriTrayActividadVO tray, AdaptadorSQLBD adaptador) throws Exception{
        Connection con = null;
        try {
            con = adaptador.getConnection();
            return MeLanbide47DAO.getInstance().getTrayectoriaGeneralActividades(tray, con);
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

    public void eliminarTrayectoriaActividades(OriTrayActividadVO tray, AdaptadorSQLBD adaptador) throws Exception{
        log.debug("eliminarTrayectoriaActividades() - Begin()");
        Connection con = null;
        try {
            con = adaptador.getConnection();
            adaptador.inicioTransaccion(con);
            MeLanbide47DAO meLanbide47DAO = MeLanbide47DAO.getInstance();
            if(tray!=null){
                int eliminados = meLanbide47DAO.eliminarTrayectoriaActividades(tray,con);
                if (eliminados > 0) {
                    log.debug("Se han eliminado " + eliminados + "Registros para el expediente/codEntidad/CodTray :" + tray != null ? tray.getNumExpediente() + "/" + tray.getCodEntidad() + "/" + tray.getCodIdActividad(): "");
                } else {
                    log.debug("No hab韆n registros para eliminar : " + eliminados + ", para el expediente/codEntidad/CodTray :" + tray != null ? tray.getNumExpediente() + "/" + tray.getCodEntidad() + "/" + tray.getCodIdActividad(): "");
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

    public boolean guardarTrayectoriaActividades(OriTrayActividadVO tray, AdaptadorSQLBD adaptador)throws Exception {
        Connection con = null;
        boolean exito = true;
        try {
            con = adaptador.getConnection();
            adaptador.inicioTransaccion(con);
            tray = MeLanbide47DAO.getInstance().guardarTrayectoriaActividades(tray,con);
            if (tray == null) {
                exito = false;
                log.debug("No se han guardado los datos correctamente de la trayectoria  actividades. Se ha devuelto el objeto OriTrayActividadVO a null");               
            }
            adaptador.finTransaccion(con); 
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD guardarTrayectoriaActividades ", e);
            adaptador.rollBack(con);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD guardarTrayectoriaActividades ", ex);
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

    public List<FilaOriAmbitoSolicitadoVO> getAmbitosSolicitadosORI(String numExpediente, AdaptadorSQLBD adaptador) throws Exception {
        Connection con = null;
        try {
            con = adaptador.getConnection();
            MeLanbide47DAO meLanbide47DAO = MeLanbide47DAO.getInstance();
            return meLanbide47DAO.getAmbitosSolicitadosORI(numExpediente, con);
        } catch (BDException e) {
            log.error("Se ha producido BDException  al getAmbitosSolicitadosORI " + numExpediente, e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("SSe ha producido Exception al getAmbitosSolicitadosORI expediente " + numExpediente, ex);
            throw new Exception(ex);
        } finally {
            try {
                adaptador.devolverConexion(con);
            } catch (Exception e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    public OriAmbitoSolicitadoVO guardarAmbitoSolicitadoORI(OriAmbitoSolicitadoVO ambitSol, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try {
            con = adapt.getConnection();
            MeLanbide47DAO meLanbide47DAO = MeLanbide47DAO.getInstance();
            return meLanbide47DAO.guardarAmbitoSolicitadoORI(ambitSol, con);
        } catch (BDException e) {
            log.error("Se ha producido BDException  al guardarAmbitoSolicitadoORI " + (ambitSol!=null?ambitSol.getOriAmbSolNumExp():""), e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("SSe ha producido Exception al guardarAmbitoSolicitadoORI expediente " + (ambitSol!=null?ambitSol.getOriAmbSolNumExp():""), ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (Exception e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    public void eliminarAmbitoSolicitadoORI(OriAmbitoSolicitadoVO ambiSol, AdaptadorSQLBD adaptador) throws Exception {
        log.debug("eliminarAmbitoSolicitadoORI() - Begin()");
        Connection con = null;
        try {
            con = adaptador.getConnection();
            MeLanbide47DAO meLanbide47DAO = MeLanbide47DAO.getInstance();
            if (ambiSol != null) {
                int eliminados = meLanbide47DAO.eliminarAmbitoSolicitadoORI(ambiSol, con);
                if (eliminados > 0) {
                    log.debug("Se han eliminado " + eliminados + " Ambitos Solicitados para el expediente :" + (ambiSol != null ? ambiSol.getOriAmbSolNumExp(): "Sin Datos Numero Expediente"));
                } else {
                    log.error("No se ha podido eliminar ningun registro de ambitos solicitado para el expediente  : " + (ambiSol != null ? ambiSol.getOriAmbSolNumExp(): "Sin Datos Numero Expediente"));
                }
            } else {
                throw new BDException();
            }
        } catch (BDException e) {
            log.error("Se ha producido una BDException en la BBDD eliminando Ambitos Solicitado del expediente/codEntidad/CodTray :" +  (ambiSol != null ? ambiSol.getOriAmbSolNumExp(): "Sin Datos Numero Expediente"), e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una Exception en la BBDD eliminando Ambitos Solicitado del expediente :" +  (ambiSol != null ? ambiSol.getOriAmbSolNumExp(): "Sin Datos Numero Expediente"), ex);
            throw new Exception(ex);
        } finally {
            try {
                adaptador.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
        log.debug("eliminarAmbitoSolicitadoORI() - End()");
    }

    public OriAmbitoSolicitadoVO getAmbitoSolicitadoORIPorCodigo(OriAmbitoSolicitadoVO ambiSol, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try {
            con = adapt.getConnection();
            MeLanbide47DAO meLanbide47DAO = MeLanbide47DAO.getInstance();
            return meLanbide47DAO.getAmbitoSolicitadoORIPorCodigo(ambiSol, con);
        } catch (BDException e) {
            log.error("Se ha producido BDException  al getAmbitoSolicitadoORIPorCodigo " + (ambiSol != null ? ambiSol.getOriAmbSolNumExp() : ""), e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("SSe ha producido Exception al getAmbitoSolicitadoORIPorCodigo expediente " + (ambiSol != null ? ambiSol.getOriAmbSolNumExp() : ""), ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (Exception e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }
 
    /**
     * Metodo que devuelve el dereto o convocatoria a la que pertenece el
     * expediente en funcion de una fecha de referencia recbida: Ejemplo, Fecha
     * presentacion expediente (campo suplementario).
     *
     * @param fechaReferenciaExpediente
     * @param codProcedimiento
     * @param adaptador
     * @return Objeto con los datos de la convocatoria o decreto aplicacble
     * segun la fecha de referecia : Tipo MeLanbideConvocatorias tabla
     * MELANBIDE_CONVOCATORIAS
     * @throws Exception
     */
    public MeLanbideConvocatorias getDecretoAplicableExpediente(Date fechaReferenciaExpediente, String codProcedimiento, AdaptadorSQLBD adaptador) throws Exception {
        log.info("getDecretoAplicableExpediente() - Manager - : BEGIN " + formatFechaLog.format(new Date()) + " " + fechaReferenciaExpediente);
        Connection con = null;
        try {
            con = adaptador.getConnection();
            return meLanbideConvocatoriasDAO.getDecretoAplicableExpediente(fechaReferenciaExpediente, codProcedimiento, con);
        } catch (Exception ex) {
            log.error("Se ha producido una excepci髇 general al recuperar datos del DECRETO " + ex.getMessage(), ex);
            throw new Exception("Error al leer Datos de DECRETO. " + ex.getMessage(), ex);
        } finally {
            try {
                adaptador.devolverConexion(con);
                log.info("getDecretoAplicableExpediente() - Manager - End  " + formatFechaLog.format(new Date()));
            } catch (BDException e) {
                log.error("Error al cerrar conexi髇 a la BBDD: getDecretoAplicableExpediente - " + e.getMessage());
            }
        }
    }

    public Date getFechaReferenciaDecretoExpediente(Integer codOrganizacion, String numExpediente, AdaptadorSQLBD adapt) throws Exception {
        log.info("getFechaReferenciaDecretoExpediente() - Manager - : BEGIN " + formatFechaLog.format(new Date()) + " " + numExpediente);
        Connection con = null;
        Date respuesta = null;
        try {
            con = adapt.getConnection();
            String campoSuplementarioFechRefExpe = ConfigurationParameter.getParameter(ConstantesMeLanbide47.COD_CAMPO_SUP_FECHA_REF_EXP, ConstantesMeLanbide47.FICHERO_PROPIEDADES);
            if (campoSuplementarioFechRefExpe != null && !campoSuplementarioFechRefExpe.isEmpty()) {
                Integer ejercicio = MeLanbide47Utils.getEjercicioDeExpediente(numExpediente);
                String codProcedimiento = MeLanbide47Utils.getCodProcedimientoDeExpediente(numExpediente);
                respuesta = getCampoSuplementarioFecha(codOrganizacion, ejercicio, codProcedimiento, numExpediente, campoSuplementarioFechRefExpe);
            } else {
                respuesta = MeLanbide47DAO.getInstance().getFechaAltaExpediente(codOrganizacion, numExpediente, con);
            }
        } catch (Exception ex) {
            log.error("Se ha producido una excepci髇 general al recuperar datos de fecha referencia del expedientes para el decreto aplicable " + ex.getMessage(), ex);
            throw new Exception("Error al leer Datos de DECRETO. " + ex.getMessage(), ex);
        } finally {
            try {
                adapt.devolverConexion(con);
                log.info("getDecretoAplicableExpediente() - Manager - End  " + formatFechaLog.format(new Date()));
            } catch (BDException e) {
                log.error("Error al cerrar conexi髇 a la BBDD: getDecretoAplicableExpediente - " + e.getMessage());
            }
        }
        return respuesta;
    }

    public Date getCampoSuplementarioFecha(Integer codOrganizacion, Integer ejercicio, String codProcedimiento, String numExpediente, String codigoCampo) throws Exception {
        log.info("getCampoSuplementarioFecha() : BEGIN ");
        Date valor = null;
        IModuloIntegracionExternoCamposFlexia el = ModuloIntegracionExternoCampoSupFactoria.getInstance().getImplClass();
        SalidaIntegracionVO salida = new SalidaIntegracionVO();
        CampoSuplementarioModuloIntegracionVO campoSuplementario = new CampoSuplementarioModuloIntegracionVO();
        salida = el.getCampoSuplementarioExpediente(String.valueOf(codOrganizacion), String.valueOf(ejercicio), numExpediente,
                codProcedimiento, codigoCampo, 3);
        if (salida.getStatus() == 0) {
            campoSuplementario = salida.getCampoSuplementario();
            if (campoSuplementario != null && campoSuplementario.getValorFecha() != null) {
                valor = campoSuplementario.getValorFecha().getTime();
            }
        }//if(salida.getStatus() == 0)
        log.info("getCampoSuplementarioFecha() : END ");
        return valor;
    }

    public List<OriTrayectoriaEntidadVO> getListaTrayectoriaEntidadXGrupo(OriTrayectoriaEntidadVO trayEntidad, AdaptadorSQLBD adapt) throws Exception {
        log.info("getListaTrayectoriaEntidadXExp() - Manager - : BEGIN ");
        Connection con = null;
        try {
            con = adapt.getConnection();
            return MeLanbide47DAO.getInstance().getListaTrayectoriaEntidadXGrupo(trayEntidad, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD recuperando Programas / Actividades para el expediente " + (trayEntidad != null ? trayEntidad.getNumExpediente() : " (trayectoriaEntidad = null)"), e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD recuperando Programas / Actividades para el expediente " + (trayEntidad != null ? trayEntidad.getNumExpediente() : " (trayectoriaEntidad = null)"), ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    public List<OriTrayectoriaEntidadVO> getTotalTrayectoriaEntidadXExp(String numExpediente, AdaptadorSQLBD adapt) throws Exception {
        log.info("getTotalTrayectoriaEntidadXExp() - Manager - : BEGIN ");
        Connection con = null;
        try {
            con = adapt.getConnection();
            return MeLanbide47DAO.getInstance().getTotalTrayectoriaEntidadXExp(numExpediente, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD recuperando Programas / Actividades para el expediente " + (numExpediente != null ? numExpediente : " (numExpediente = null)"), e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD recuperando Programas / Actividades para el expediente " + (numExpediente != null ? numExpediente : " (numExpediente = null)"), ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    public OriTrayectoriaEntidadVO getTrayEntidadXId(OriTrayectoriaEntidadVO trayEntidad, AdaptadorSQLBD adapt) throws Exception {
        log.info("getTrayEntidadXId() - Manager - : BEGIN ");
        Connection con = null;
        try {
            con = adapt.getConnection();
            return MeLanbide47DAO.getInstance().getTrayEntidadXId(trayEntidad, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD recuperando Programas/Actividades para el expediente " + (trayEntidad != null ? trayEntidad.getNumExpediente() : " (trayectoriaEntidad = null)"), e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD recuperando Programas/Actividades para el expediente " + (trayEntidad != null ? trayEntidad.getNumExpediente() : " (trayectoriaEntidad = null)"), ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    public int eliminarTrayEntidad(OriTrayectoriaEntidadVO trayEntidad, AdaptadorSQLBD adapt) throws Exception {
        log.info("eliminarTrayEntidad() - Manager - : BEGIN ");
        Connection con = null;
        int eliminados = 0;
        try {
            con = adapt.getConnection();
            adapt.inicioTransaccion(con);
            MeLanbide47DAO meLanbide47DAO = MeLanbide47DAO.getInstance();
            if (trayEntidad != null) {
                eliminados = meLanbide47DAO.eliminarTrayEntidad(trayEntidad, con);
                if (eliminados > 0) {
                    log.debug("Se han eliminado " + eliminados + "Registros para el expediente/codEntidad/CodTray :" + trayEntidad != null ? trayEntidad.getNumExpediente() + "/" + trayEntidad.getCodEntidad() + "/" + trayEntidad.getIdTrayEntidad() : "");
                } else {
                    log.debug("No hab韆n registros para eliminar : " + eliminados + ", para el expediente/codEntidad/CodTray :" + trayEntidad != null ? trayEntidad.getNumExpediente() + "/" + trayEntidad.getCodEntidad() + "/" + trayEntidad.getIdTrayEntidad() : "");
                }
                adapt.finTransaccion(con);
            } else {
                throw new BDException();
            }
        } catch (BDException e) {
            adapt.rollBack(con);
            log.error("Se ha producido una excepcion en la BBDD eliminando datos del expediente/codEntidad/CodTray :" + trayEntidad != null ? trayEntidad.getNumExpediente() + "/" + trayEntidad.getCodEntidad() + "/" + trayEntidad.getIdTrayEntidad() : "", e);
            throw new Exception(e);
        } catch (Exception ex) {
            adapt.rollBack(con);
            log.error("Se ha producido una excepcion en la BBDD eliminando datos del expediente/codEntidad/CodTrayEsp :" + trayEntidad != null ? trayEntidad.getNumExpediente() + "/" + trayEntidad.getCodEntidad() + "/" + trayEntidad.getIdTrayEntidad() : "", ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                adapt.rollBack(con);
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
        log.debug("eliminarTrayEntidad() - End()");
        return eliminados;
    }

    public boolean guardarTrayEntidad(OriTrayectoriaEntidadVO trayEntidad, boolean validado, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        boolean exito = true;
        try {
            con = adapt.getConnection();
            adapt.inicioTransaccion(con);
            exito = MeLanbide47DAO.getInstance().guardarTrayEntidad(trayEntidad, validado, con);
            if (exito == false) {
                log.debug("No se han guardado los datos correctamente de la trayectoria  actividades. Se ha devuelto el objeto OriTrayActividadVO a null");
            }
            adapt.finTransaccion(con);
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD guardarTrayectoriaActividades ", e);
            adapt.rollBack(con);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepcion en la BBDD guardarTrayectoriaActividades ", ex);
            adapt.rollBack(con);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
                adapt.rollBack(con);
            }
        }
        return exito;
    }

    public String[] getCifNombreEntidad(String numExpediente, String codEntidad, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try {
            con = adapt.getConnection();
            return MeLanbide47DAO.getInstance().getCifNombreEntidad(numExpediente, codEntidad, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD recuperando CIF y Nombre de la Entidad para el expediente " + numExpediente, e);
            throw new Exception(e);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    public String getCodEntidad(int codOrganizacion, String numExpediente, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try {
            con = adapt.getConnection();
            return MeLanbide47DAO.getInstance().getCodEntidad(codOrganizacion, numExpediente, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD recuperando la Entidad para el expediente " + numExpediente, e);
            throw new Exception(e);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    public List<SelectItem> getListaSubgrupos(int idioma, AdaptadorSQLBD adaptador) throws Exception {
        Connection con = null;
        try {
            con = adaptador.getConnection();
            MeLanbide47DAO meLanbide47DAO = MeLanbide47DAO.getInstance();
            return meLanbide47DAO.getListaSubgrupos(idioma, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepci髇 en la BBDD recuperando la lista de SubGrupos ", e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una excepci髇 en la BBDD recuperando la lista de SubGrupos ", ex);
            throw new Exception(ex);
        } finally {
            try {
                adaptador.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexi髇 a la BBDD: " + e.getMessage());
            }
        }
    }

    public boolean guardarMesesTrayectorias(int codOrganizacion, String numExpediente, int mesesSol, Double mesesVal, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        boolean correcto = true;
        try {
            con = adapt.getConnection();
            return MeLanbide47DAO.getInstance().guardarMesesTrayectorias(codOrganizacion, numExpediente, mesesSol, mesesVal, con);
        } catch (BDException ex) {
            log.error("Se ha producido una excepci髇 en la BBDD guardando los meses validados ", ex);
            throw new Exception(ex);
        } catch (Exception ex) {
            log.error("Se ha producido una excepci髇 en la BBDD  guardando los meses validados ", ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexi髇 a la BBDD: " + e.getMessage());
            }
        }
    }

    public boolean validarDatosTrayectoria21(int codOrganizacion, String numExpediente, int grupo, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try {
            con = adapt.getConnection();
            return MeLanbide47DAO.getInstance().validarDatosTrayectoria21(codOrganizacion, numExpediente, grupo, con);
        } catch (BDException ex) {
            log.error("Se ha producido una excepci髇 en la BBDD guardando los meses validados ", ex);
            throw new Exception(ex);
        } catch (Exception ex) {
            log.error("Se ha producido una excepci髇 en la BBDD  guardando los meses validados ", ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexi髇 a la BBDD: " + e.getMessage());
            }
        }
    }

    public boolean guardarPuntuacionTrayectorias(int codOrganizacion, String numExpediente, Double puntuacion, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try {
            con = adapt.getConnection();
            return MeLanbide47DAO.getInstance().guardarPuntuacionTrayectorias(codOrganizacion, numExpediente, puntuacion, con);
        } catch (BDException ex) {
            log.error("Se ha producido una excepci髇 en la BBDD guardando la puntuaci髇 ", ex);
            throw new Exception(ex);
        } catch (Exception ex) {
            log.error("Se ha producido una excepci髇 en la BBDD  guardando la puntuaci髇  ", ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexi髇 a la BBDD: " + e.getMessage());
            }
        }
    }

    private BigDecimal calcularValoracion21(Double meses, BigDecimal ubicVal, String espAdicional, String disponeWifi, Integer planIgualdad, Integer certificadoCalidad) {
        Double total = 0.0;
       //Trayectoria
        Double puntos = 0.2;
        try {
          Double  puntuacion = puntos * meses;
            if (puntuacion > 12.0) {
                puntuacion = 12.0;
            }
            total += puntuacion;
        } catch (Exception ex) {
        }

        //Ubicacion
        try {
            Double ubicacion = ubicVal.doubleValue();
            total += ubicacion;
        } catch (Exception ex) {
        }

        //Dispone espacio adicional
        try {
            if (espAdicional != null && espAdicional.equalsIgnoreCase(ConstantesMeLanbide47.SI)) {
                total += 4;
            }
        } catch (Exception ex) {
        }

        //Dispone WIFI
        try {
            if (disponeWifi != null && disponeWifi.equalsIgnoreCase(ConstantesMeLanbide47.SI)) {
                total += 1;
            }
        } catch (Exception ex) {
        }
        
        try {
            if (planIgualdad != null && planIgualdad ==1) {
            total += 2;
            }
        } catch (Exception e) {
        }
        
        try {
            if (certificadoCalidad != null && certificadoCalidad ==1) {
            total += 2;
            }
        } catch (Exception e) {
        }
        
        return new BigDecimal(total.toString());
    }

    public int getValorCampoEntidad(String numExpediente, String campo, Long entidad,  AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try {
            con = adapt.getConnection();
            return MeLanbide47DAO.getInstance().getValorCampoEntidad(numExpediente,entidad, campo, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepcion en la BBDD recuperando la Entidad para el expediente " + numExpediente, e);
            throw new Exception(e);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }
    
    
    public int getCalculoNroMesesTrayectoriaNoSolapadaSolicitud(String numExpediente, AdaptadorSQLBD adapt) throws Exception{
        Connection con = null;
        int retorno = 0;
        try {
            con = adapt.getConnection();
            List<OriTrayectoriaEntidadVO> listaDatos= MeLanbide47DAO.getInstance().getListaFechasTrayectoriaEntidadXExpNoSolapamiento(numExpediente,con);
            if(listaDatos!=null){
                for(OriTrayectoriaEntidadVO dato:listaDatos){
                    retorno += (dato.getNumMeses()!=null?dato.getNumMeses():0);
                }
            }
        } catch (BDException e) {
            log.error("getCalculoNroMesesTrayectoriaNoSolapadaSolicitud :  " + numExpediente, e);
            retorno=-1;
            throw new Exception(e);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
        return retorno;
    }
    
    public Double getCalculoNroMesesTrayectoriaNoSolapadaSolicitudValidada(String numExpediente, AdaptadorSQLBD adapt) throws Exception{
        Connection con = null;
        Double retorno = 0D;
        try {
            con = adapt.getConnection();
            List<OriTrayectoriaEntidadVO> listaDatos= MeLanbide47DAO.getInstance().getListaFechasTrayectoriaEntidadXExpNoSolapamientoValidada(numExpediente,con);
            if(listaDatos!=null){
                for(OriTrayectoriaEntidadVO dato:listaDatos){
                    retorno += (dato.getNumMesesVal()!=null?dato.getNumMesesVal():0);
                }
            }
        } catch (BDException e) {
            log.error("getCalculoNroMesesTrayectoriaNoSolapadaSolicitud :  " + numExpediente, e);
            retorno=-1D;
            throw new Exception(e);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
        return retorno;
    }
    
}

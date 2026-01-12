/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.melanbide32.manager;

import es.altia.flexia.integracion.moduloexterno.melanbide32.dao.MeLanbide32DAO;
import es.altia.flexia.integracion.moduloexterno.melanbide32.util.ConstantesMeLanbide32;
import es.altia.flexia.integracion.moduloexterno.melanbide32.vo.centroempleo.CeTrayectoriaVO;
import es.altia.flexia.integracion.moduloexterno.melanbide32.vo.centroempleo.CeUbicacionVO;
import es.altia.flexia.integracion.moduloexterno.melanbide32.vo.centroempleo.FilaTrayectoriaCentroEmpleoVO;
import es.altia.flexia.integracion.moduloexterno.melanbide32.vo.centroempleo.FilaUbicCentroEmpleoVO;
import es.altia.flexia.integracion.moduloexterno.melanbide32.vo.centroempleo.AmbitoCentroEmpleoVO;
import es.altia.flexia.integracion.moduloexterno.melanbide32.vo.centroempleo.AmbitoSolicitadoCempVO;
import es.altia.flexia.integracion.moduloexterno.melanbide32.vo.centroempleo.FilaAmbitoSolicitadoCempVO;
import es.altia.flexia.integracion.moduloexterno.melanbide32.vo.comun.EntidadVO;
import es.altia.flexia.integracion.moduloexterno.melanbide32.vo.comun.EspecialidadVO;
import es.altia.flexia.integracion.moduloexterno.melanbide32.vo.comun.MunicipioVO;
import es.altia.flexia.integracion.moduloexterno.melanbide32.vo.comun.ProvinciaVO;
import es.altia.flexia.integracion.moduloexterno.melanbide32.vo.comun.SelectItem;
import es.altia.flexia.integracion.moduloexterno.melanbide32.vo.orientacion.UbicacionHorasVO;
import es.altia.flexia.integracion.moduloexterno.melanbide32.vo.comun.AuditoriaVO;
import es.altia.flexia.integracion.moduloexterno.melanbide32.vo.comun.DomicilioVO;
import es.altia.flexia.integracion.moduloexterno.melanbide32.vo.comun.TipoViaVO;
import es.altia.flexia.integracion.moduloexterno.melanbide32.vo.comun.ViaVO;
import es.altia.flexia.integracion.moduloexterno.melanbide32.vo.comun.TerceroVO;
import es.altia.flexia.integracion.moduloexterno.melanbide32.vo.orientacion.AmbitoHorasVO;
import es.altia.flexia.integracion.moduloexterno.melanbide32.vo.orientacion.FilaTrayectoriaOrientacionVO;
import es.altia.flexia.integracion.moduloexterno.melanbide32.vo.orientacion.FilaUbicOrientacionVO;
import es.altia.flexia.integracion.moduloexterno.melanbide32.vo.orientacion.OriTrayectoriaVO;
import es.altia.flexia.integracion.moduloexterno.melanbide32.vo.orientacion.OriUbicacionVO;
import es.altia.flexia.integracion.moduloexterno.melanbide32.vo.procesos.FilaAuditoriaProcesosVO;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.altia.util.conexion.BDException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

/**
 *
 * @author santiagoc
 */
public class MeLanbide32Manager 
{
    //Logger
    private static Logger log = LogManager.getLogger(MeLanbide32Manager.class);    
    
    //Instancia
    private static MeLanbide32Manager instance = null;
    
    public MeLanbide32Manager()
    {
        
    }
    
    public static MeLanbide32Manager getInstance()
    {
        if(instance == null)
        {
            synchronized(MeLanbide32Manager.class)
            {
                instance = new MeLanbide32Manager();
            }
        }
        return instance;
    }    
    
    public List<SelectItem> getListaProvincias(String codigoPais, List<String> codigosProv, AdaptadorSQLBD adaptador)throws Exception
    {
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            MeLanbide32DAO meLanbide32DAO = MeLanbide32DAO.getInstance();
            return meLanbide32DAO.getListaProvincias(codigoPais, codigosProv, con);
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
            MeLanbide32DAO meLanbide32DAO = MeLanbide32DAO.getInstance();
            return meLanbide32DAO.getAmbitosHorasPorProvincia(codProvincia, anoConv, con);
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
    
    public List<SelectItem> getAmbitosCentroEmpleoPorProvincia(String codProvincia, String anoConv, AdaptadorSQLBD adaptador)throws Exception
    {
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            MeLanbide32DAO meLanbide32DAO = MeLanbide32DAO.getInstance();
            return meLanbide32DAO.getAmbitosCentroEmpleoPorProvincia(codProvincia, anoConv, con);
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
    
    public List<SelectItem> getAmbitosCentroEmpleoPorAnioConvAndTipo(String oriAmbCeAnoconv,int oriAmbCeTipoAmbito,AdaptadorSQLBD adaptador)throws Exception
    {
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            MeLanbide32DAO meLanbide32DAO = MeLanbide32DAO.getInstance();
            return meLanbide32DAO.getAmbitosCentroEmpleoPorAnioConvAndTipo(oriAmbCeAnoconv,oriAmbCeTipoAmbito,con);
        }
        catch(BDException e)
        {
            log.error("Se ha producido una BDException recuperando la lista de ambitos para el territorio historico " + oriAmbCeAnoconv, e);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido una Exception recuperando la lista de ambitos cemp " + oriAmbCeAnoconv, ex);
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
    
//    public List<SelectItem> getMunicipiosPorProvincia(String codProvincia, HashMap<Integer, List<Integer>> municipiosAnadidos, HashMap<Integer, List<Integer>> municipiosExcluidos, AdaptadorSQLBD adaptador)throws Exception
//    {
//        Connection con = null;
//        try
//        {
//            con = adaptador.getConnection();
//            MeLanbide32DAO meLanbide32DAO = MeLanbide32DAO.getInstance();
//            return meLanbide32DAO.getMunicipiosPorProvincia(codProvincia, municipiosAnadidos, municipiosExcluidos, con);
//        }
//        catch(BDException e)
//        {
//            log.error("Se ha producido una excepci贸n en la BBDD recuperando la lista de ambitos para el territorio historico " + codProvincia, e);
//            throw new Exception(e);
//        }
//        catch(Exception ex)
//        {
//            log.error("Se ha producido una excepci贸n en la BBDD recuperando la lista de ambitos para el territorio historico " + codProvincia, ex);
//            throw new Exception(ex);
//        }
//        finally
//        {
//            try
//            {
//                adaptador.devolverConexion(con);       
//            }
//            catch(Exception e)
//            {
//                log.error("Error al cerrar conexi贸n a la BBDD: " + e.getMessage());
//            }
//        }
//    }
    
    public List<SelectItem> getMunicipiosPorAmbitoProvinciaHoras(String codProvincia, Integer codAmbito, Integer ano, AdaptadorSQLBD adaptador)throws Exception
    {
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            MeLanbide32DAO meLanbide32DAO = MeLanbide32DAO.getInstance();
            return meLanbide32DAO.getMunicipiosPorAmbitoProvinciaHoras(codProvincia, codAmbito, ano, con);
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
    
    public List<SelectItem> getMunicipiosPorAmbitoProvinciaCentroEmpleo(String codProvincia, Integer codAmbito, Integer ano, AdaptadorSQLBD adaptador)throws Exception
    {
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            MeLanbide32DAO meLanbide32DAO = MeLanbide32DAO.getInstance();
            return meLanbide32DAO.getMunicipiosPorAmbitoProvinciaCentroEmpleo(codProvincia, codAmbito, ano, con);
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
    
    public EntidadVO getEntidad(int codOrganizacion, String numExp, String ejercicio, AdaptadorSQLBD adaptador)throws Exception
    {
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            MeLanbide32DAO meLanbide32DAO = MeLanbide32DAO.getInstance();
            return meLanbide32DAO.getEntidad(codOrganizacion, numExp, ejercicio, con);
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
    
    public EntidadVO getEntidadPorCodigo(Long codEnt, AdaptadorSQLBD adaptador)throws Exception
    {
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            MeLanbide32DAO meLanbide32DAO = MeLanbide32DAO.getInstance();
            return meLanbide32DAO.getEntidadPorCodigo(codEnt, con);
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
    
    public HashMap<String, String> getDatosTercero(int codOrganizacion, String numExp, String ejercicio, AdaptadorSQLBD adaptador)throws Exception
    {
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            MeLanbide32DAO meLanbide32DAO = MeLanbide32DAO.getInstance();
            return meLanbide32DAO.getDatosTercero(codOrganizacion, numExp, ejercicio, con);
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
    
    public EntidadVO crearEntidad(EntidadVO entidad, AdaptadorSQLBD adaptador)throws Exception
    {
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            MeLanbide32DAO meLanbide32DAO = MeLanbide32DAO.getInstance();
            return meLanbide32DAO.crearEntidad(entidad, con);
        }
        catch(BDException e)
        {
            log.error("Se ha producido una excepci贸n en la BBDD creando entidad ", e);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido una excepci贸n en la BBDD creando entidad ", ex);
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
    
    public OriUbicacionVO crearUbicacionORI(OriUbicacionVO ubic, AdaptadorSQLBD adaptador)throws Exception
    {
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            MeLanbide32DAO meLanbide32DAO = MeLanbide32DAO.getInstance();
            return meLanbide32DAO.crearUbicacionORI(ubic, con);
        }
        catch(BDException e)
        {
            log.error("Se ha producido una excepci贸n en la BBDD creando ubicacion ", e);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido una excepci贸n en la BBDD creando ubicacion ", ex);
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
    
    public OriUbicacionVO modificarUbicacionORI(OriUbicacionVO ubic, AdaptadorSQLBD adaptador)throws Exception
    {
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            MeLanbide32DAO meLanbide32DAO = MeLanbide32DAO.getInstance();
            return meLanbide32DAO.modificarUbicacionORI(ubic, con);
        }
        catch(BDException e)
        {
            log.error("Se ha producido una excepci贸n en la BBDD modificando ubicacion "+ubic.getOriOrientUbicCod(), e);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido una excepci贸n en la BBDD modificando "+ubic.getOriOrientUbicCod(), ex);
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
    
    public int eliminarUbicacionORI(Integer idUbic, AdaptadorSQLBD adaptador)throws Exception
    {
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            MeLanbide32DAO meLanbide32DAO = MeLanbide32DAO.getInstance();
            return meLanbide32DAO.eliminarUbicacionORI(idUbic, con);
        }
        catch(BDException e)
        {
            log.error("Se ha producido una excepci贸n en la BBDD eliminando ubicacion "+idUbic, e);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido una excepci贸n en la BBDD eliminando ubicacion "+idUbic, ex);
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
    
    public List<FilaUbicOrientacionVO> getUbicacionesORI(int codOrganizacion, String ejercicio, String numExpediente, AdaptadorSQLBD adaptador)throws Exception
    {
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            MeLanbide32DAO meLanbide32DAO = MeLanbide32DAO.getInstance();
            return meLanbide32DAO.getUbicacionesORI(codOrganizacion, ejercicio, numExpediente, con);
        }
        catch(BDException e)
        {
            log.error("Se ha producido una excepci贸n en la BBDD recuperando ubicaciones para el expediente "+numExpediente, e);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido una excepci贸n en la BBDD recuperando ubicaciones para el expediente "+numExpediente, ex);
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
    
    public OriUbicacionVO getUbicacionORIPorCodigo(String codUbic, AdaptadorSQLBD adaptador)throws Exception
    {
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            MeLanbide32DAO meLanbide32DAO = MeLanbide32DAO.getInstance();
            return meLanbide32DAO.getUbicacionORIPorCodigo(codUbic, con);
        }
        catch(BDException e)
        {
            log.error("Se ha producido una excepci贸n en la BBDD recuperando ubicacion "+codUbic, e);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido una excepci贸n en la BBDD recuperando ubicacion "+codUbic, ex);
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
            MeLanbide32DAO meLanbide32DAO = MeLanbide32DAO.getInstance();
            return meLanbide32DAO.getProvinciaPorCodigo(provCod, con);
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
    
    public AmbitoHorasVO getAmbitoHorasPorCodigo(Integer ambCod, AdaptadorSQLBD adaptador)throws Exception
    {
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            MeLanbide32DAO meLanbide32DAO = MeLanbide32DAO.getInstance();
            return meLanbide32DAO.getAmbitoHorasPorCodigo(ambCod, con);
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
    
    public AmbitoCentroEmpleoVO getAmbitoCentroEmpleoPorCodigo(Integer ambCod, AdaptadorSQLBD adaptador)throws Exception
    {
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            MeLanbide32DAO meLanbide32DAO = MeLanbide32DAO.getInstance();
            return meLanbide32DAO.getAmbitoCentroEmpleoPorCodigo(ambCod, con);
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
            MeLanbide32DAO meLanbide32DAO = MeLanbide32DAO.getInstance();
            return meLanbide32DAO.getMunicipioPorCodigoYProvincia(munCod, provCod, con);
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
    
    public UbicacionHorasVO getUbicacion(OriUbicacionVO ubic, AdaptadorSQLBD adaptador)throws Exception
    {
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            MeLanbide32DAO meLanbide32DAO = MeLanbide32DAO.getInstance();
            return meLanbide32DAO.getUbicacion(ubic, con);
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
    
    public OriTrayectoriaVO crearTrayectoriaORI(OriTrayectoriaVO tray, AdaptadorSQLBD adaptador)throws Exception
    {
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            MeLanbide32DAO meLanbide32DAO = MeLanbide32DAO.getInstance();
            return meLanbide32DAO.crearTrayectoriaORI(tray, con);
        }
        catch(BDException e)
        {
            log.error("Se ha producido una excepci贸n en la BBDD creando trayectoria ", e);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido una excepci贸n en la BBDD creando trayectoria ", ex);
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
    
    public List<FilaTrayectoriaOrientacionVO> getTrayectoriasORI(Long oriEntCod, AdaptadorSQLBD adaptador)throws Exception
    {
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            MeLanbide32DAO meLanbide32DAO = MeLanbide32DAO.getInstance();
            return meLanbide32DAO.getTrayectoriasORI(oriEntCod, con);
        }
        catch(BDException e)
        {
            log.error("Se ha producido una excepci贸n en la BBDD recuperando trayectorias para la entidad "+oriEntCod, e);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido una excepci贸n en la BBDD recuperando trayectorias para la entidad "+oriEntCod, ex);
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
            MeLanbide32DAO meLanbide32DAO = MeLanbide32DAO.getInstance();
            return meLanbide32DAO.getTrayectoriaORIPorCodigo(codTray, con);
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
    
    public OriTrayectoriaVO modificarTrayectoriaORI(OriTrayectoriaVO tray, AdaptadorSQLBD adaptador)throws Exception
    {
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            MeLanbide32DAO meLanbide32DAO = MeLanbide32DAO.getInstance();
            return meLanbide32DAO.modificarTrayectoriaORI(tray, con);
        }
        catch(BDException e)
        {
            log.error("Se ha producido una excepci贸n en la BBDD modificando trayectoria "+tray.getOriOritrayCod(), e);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido una excepci贸n en la BBDD modificando trayectoria "+tray.getOriOritrayCod(), ex);
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
    
    public int eliminarTrayectoriaORI(Integer idTray, AdaptadorSQLBD adaptador)throws Exception
    {
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            MeLanbide32DAO meLanbide32DAO = MeLanbide32DAO.getInstance();
            return meLanbide32DAO.eliminarTrayectoriaORI(idTray, con);
        }
        catch(BDException e)
        {
            log.error("Se ha producido una excepci贸n en la BBDD eliminando trayectoria "+idTray, e);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido una excepci贸n en la BBDD eliminando trayectoria "+idTray, ex);
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
    
    public int modificarEntidad(EntidadVO entidad, AdaptadorSQLBD adaptador)throws Exception
    {
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            MeLanbide32DAO meLanbide32DAO = MeLanbide32DAO.getInstance();
            return meLanbide32DAO.modificarEntidad(entidad, con);
        }
        catch(BDException e)
        {
            log.error("Se ha producido una excepci贸n en la BBDD modificando entidad "+entidad.getOriEntCod(), e);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido una excepci贸n en la BBDD modificando entidad "+entidad.getOriEntCod(), ex);
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
    
    public List<FilaUbicCentroEmpleoVO> getUbicacionesCE(int codOrganizacion, String ejercicio, String numExpediente, AdaptadorSQLBD adaptador)throws Exception
    {
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            MeLanbide32DAO meLanbide32DAO = MeLanbide32DAO.getInstance();
            return meLanbide32DAO.getUbicacionesCE(codOrganizacion, ejercicio, numExpediente, con);
        }
        catch(BDException e)
        {
            log.error("Se ha producido una excepci贸n en la BBDD recuperando ubicaciones para el expediente "+numExpediente, e);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido una excepci贸n en la BBDD recuperando ubicaciones para el expediente "+numExpediente, ex);
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
    
    public List<FilaTrayectoriaCentroEmpleoVO> getTrayectoriasCE(Long oriEntCod, String procedimiento, AdaptadorSQLBD adaptador)throws Exception
    {
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            MeLanbide32DAO meLanbide32DAO = MeLanbide32DAO.getInstance();
            return meLanbide32DAO.getTrayectoriasCE(oriEntCod, procedimiento, con);
        }
        catch(BDException e)
        {
            log.error("Se ha producido una excepci贸n en la BBDD recuperando trayectorias para la entidad "+oriEntCod, e);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido una excepci贸n en la BBDD recuperando trayectorias para la entidad "+oriEntCod, ex);
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
    
    public CeUbicacionVO crearUbicacionCE(CeUbicacionVO ubic, AdaptadorSQLBD adaptador)throws Exception
    {
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            MeLanbide32DAO meLanbide32DAO = MeLanbide32DAO.getInstance();
            return meLanbide32DAO.crearUbicacionCE(ubic, con);
        }
        catch(BDException e)
        {
            log.error("Se ha producido una excepci贸n en la BBDD creando ubicacion ", e);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido una excepci贸n en la BBDD creando ubicacion ", ex);
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
    
    public CeUbicacionVO getUbicacionCEPorCodigo(String codUbic, AdaptadorSQLBD adaptador)throws Exception
    {
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            MeLanbide32DAO meLanbide32DAO = MeLanbide32DAO.getInstance();
            return meLanbide32DAO.getUbicacionCEPorCodigo(codUbic, con);
        }
        catch(BDException e)
        {
            log.error("Se ha producido una excepci贸n en la BBDD recuperando ubicacion "+codUbic, e);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido una excepci贸n en la BBDD recuperando ubicacion "+codUbic, ex);
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
    
    public CeUbicacionVO modificarUbicacionCE(CeUbicacionVO ubic, AdaptadorSQLBD adaptador)throws Exception
    {
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            MeLanbide32DAO meLanbide32DAO = MeLanbide32DAO.getInstance();
            return meLanbide32DAO.modificarUbicacionCE(ubic, con);
        }
        catch(BDException e)
        {
            log.error("Se ha producido una excepcion en la BBDD modificando ubicacion "+ubic.getOriCeUbicCod(), e);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido una excepcion en la BBDD modificando "+ubic.getOriCeUbicCod(), ex);
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
    
    public int eliminarUbicacionCE(Integer idUbic, AdaptadorSQLBD adaptador)throws Exception
    {
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            MeLanbide32DAO meLanbide32DAO = MeLanbide32DAO.getInstance();
            return meLanbide32DAO.eliminarUbicacionCE(idUbic, con);
        }
        catch(BDException e)
        {
            log.error("Se ha producido una excepci贸n en la BBDD eliminando ubicacion "+idUbic, e);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido una excepci贸n en la BBDD eliminando ubicacion "+idUbic, ex);
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
    
    public CeTrayectoriaVO crearTrayectoriaCE(CeTrayectoriaVO tray, AdaptadorSQLBD adaptador)throws Exception
    {
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            MeLanbide32DAO meLanbide32DAO = MeLanbide32DAO.getInstance();
            return meLanbide32DAO.crearTrayectoriaCE(tray, con);
        }
        catch(BDException e)
        {
            log.error("Se ha producido una excepci贸n en la BBDD creando trayectoria ", e);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido una excepci贸n en la BBDD creando trayectoria ", ex);
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
    
    public CeTrayectoriaVO getTrayectoriaCEPorCodigo(String codTray, AdaptadorSQLBD adaptador)throws Exception
    {
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            MeLanbide32DAO meLanbide32DAO = MeLanbide32DAO.getInstance();
            return meLanbide32DAO.getTrayectoriaCEPorCodigo(codTray, con);
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
    
    public CeTrayectoriaVO modificarTrayectoriaCE(CeTrayectoriaVO tray, AdaptadorSQLBD adaptador)throws Exception
    {
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            MeLanbide32DAO meLanbide32DAO = MeLanbide32DAO.getInstance();
            return meLanbide32DAO.modificarTrayectoriaCE(tray, con);
        }
        catch(BDException e)
        {
            log.error("Se ha producido una excepci贸n en la BBDD modificando trayectoria "+tray.getOriCeCod(), e);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido una excepci贸n en la BBDD modificando trayectoria "+tray.getOriCeCod(), ex);
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
    
    public int eliminarTrayectoriaCE(Integer idTray, AdaptadorSQLBD adaptador)throws Exception
    {
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            MeLanbide32DAO meLanbide32DAO = MeLanbide32DAO.getInstance();
            return meLanbide32DAO.eliminarTrayectoriaCE(idTray, con);
        }
        catch(BDException e)
        {
            log.error("Se ha producido una excepci贸n en la BBDD eliminando trayectoria "+idTray, e);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido una excepci贸n en la BBDD eliminando trayectoria "+idTray, ex);
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
            MeLanbide32DAO meLanbide32DAO = MeLanbide32DAO.getInstance();
            return meLanbide32DAO.getListaEspecialidades(con);
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
            MeLanbide32DAO meLanbide32DAO = MeLanbide32DAO.getInstance();
            return meLanbide32DAO.getDistintasProvDeAmbitosHoras(con);
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
            MeLanbide32DAO meLanbide32DAO = MeLanbide32DAO.getInstance();
            return meLanbide32DAO.getDistintasProvDeAmbitosCentroEmpleo(con);
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
    
    public List<AmbitoHorasVO> getAmbitosHorasPorProvincia(Integer codProvincia, Integer anoConv, AdaptadorSQLBD adaptador)throws Exception
    {
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            MeLanbide32DAO meLanbide32DAO = MeLanbide32DAO.getInstance();
            return meLanbide32DAO.getAmbitosHorasPorProvincia(codProvincia, anoConv, con);
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
    
    public List<AmbitoCentroEmpleoVO> getAmbitosPorProvincia(Integer codProvincia, Integer anoConv, AdaptadorSQLBD adaptador)throws Exception
    {
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            MeLanbide32DAO meLanbide32DAO = MeLanbide32DAO.getInstance();
            return meLanbide32DAO.getAmbitosCentroEmpleoPorProvincia(codProvincia, anoConv, con);
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
    
    
    
    public List<OriUbicacionVO> getUbicacionesDeAmbitoORI(AmbitoHorasVO amb, AdaptadorSQLBD adaptador) throws Exception
    {
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            MeLanbide32DAO meLanbide32DAO = MeLanbide32DAO.getInstance();
            return meLanbide32DAO.getUbicacionesDeAmbitoORI(amb, con);
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
    public List<CeUbicacionVO> getUbicacionesDeAmbitoCE(AmbitoCentroEmpleoVO amb, AdaptadorSQLBD adaptador) throws Exception
    {
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            MeLanbide32DAO meLanbide32DAO = MeLanbide32DAO.getInstance();
            return meLanbide32DAO.getUbicacionesDeAmbitoCE(amb, con);
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
    
    public List<CeUbicacionVO> getUbicacionesDeAmbitoCEMP2014(AmbitoCentroEmpleoVO amb, AdaptadorSQLBD adaptador) throws Exception
    {
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            MeLanbide32DAO meLanbide32DAO = MeLanbide32DAO.getInstance();
            return meLanbide32DAO.getUbicacionesDeAmbitoCEMP2014(amb, con);
        }
        catch(BDException e)
        {
            log.error("Se ha producido una excepcion en la BBDD recuperando la lista de entidades para el ambito " + amb.getOriAmbCod(), e);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido una excepcion en la BBDD recuperando la lista de entidades para el ambito " + amb.getOriAmbCod(), ex);
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
            MeLanbide32DAO meLanbide32DAO = MeLanbide32DAO.getInstance();
            return meLanbide32DAO.adjudicaOrientacion(ano, con);
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
            MeLanbide32DAO meLanbide32DAO = MeLanbide32DAO.getInstance();
            return meLanbide32DAO.consolidaHoras(con);
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
            MeLanbide32DAO meLanbide32DAO = MeLanbide32DAO.getInstance();
            return meLanbide32DAO.deshacerConsolidacionHoras(ano, con);
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
    
    public String[] adjudicaCentros(Integer ano, String codProcedimiento, AdaptadorSQLBD adaptador) throws Exception
    {
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            MeLanbide32DAO meLanbide32DAO = MeLanbide32DAO.getInstance();
            if(codProcedimiento != null)
            {
                if(codProcedimiento.equalsIgnoreCase(ConstantesMeLanbide32.CODIGO_PROCEDIMIENTO_AYORI))
                {
                    return meLanbide32DAO.adjudicaCentros(ano, con);
                }
                else if(codProcedimiento.equalsIgnoreCase(ConstantesMeLanbide32.CODIGO_PROCEDIMIENTO_CEMP))
                {
                     return meLanbide32DAO.adjudicaCentrosCemp(ano, con);
                }
                else
                {
                    return new String[]{ConstantesMeLanbide32.ERROR};
                }
            }
            else
            {
                return new String[]{ConstantesMeLanbide32.ERROR};
            }
        }
        catch(BDException e)
        {
            log.error("Se ha producido una excepci贸n en la BBDD ejectuando el proceso adjudica_centros para el a帽o " + ano, e);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido una excepci贸n en la BBDD ejectuando el proceso adjudica_centros para el a帽o " + ano, ex);
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
    
    public String consolidaCentros(String codProcedimiento, Integer ano,AdaptadorSQLBD adaptador) throws Exception
    {
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            MeLanbide32DAO meLanbide32DAO = MeLanbide32DAO.getInstance();
            if(codProcedimiento != null)
            {
                if(codProcedimiento.equalsIgnoreCase(ConstantesMeLanbide32.CODIGO_PROCEDIMIENTO_AYORI))
                {
                    return meLanbide32DAO.consolidaCentros(con);
                }
                else if(codProcedimiento.equalsIgnoreCase(ConstantesMeLanbide32.CODIGO_PROCEDIMIENTO_CEMP))
                {
                    return meLanbide32DAO.consolidaCentrosCemp(ano,con);
                }
                else
                {
                    return ConstantesMeLanbide32.ERROR;
                }
            }
            else
            {
                return ConstantesMeLanbide32.ERROR;
            }
        }
        catch(BDException e)
        {
            log.error("Se ha producido una excepci贸n en la BBDD ejectuando el proceso consolida_centros", e);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido una excepci贸n en la BBDD ejectuando el proceso consolida_centros", ex);
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
    
    public String deshacerConsolidacionCentros(Integer ano, String codProcedimiento, AdaptadorSQLBD adaptador) throws Exception
    {
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            MeLanbide32DAO meLanbide32DAO = MeLanbide32DAO.getInstance();
            if(codProcedimiento != null)
            {
                if(codProcedimiento.equalsIgnoreCase(ConstantesMeLanbide32.CODIGO_PROCEDIMIENTO_AYORI))
                {
                    return meLanbide32DAO.deshacerConsolidacionCentros(ano, con);
                }
                else if(codProcedimiento.equalsIgnoreCase(ConstantesMeLanbide32.CODIGO_PROCEDIMIENTO_CEMP))
                {
                    return meLanbide32DAO.deshacerConsolidacionCentrosCemp(ano, con);
                }
                else
                {
                    return ConstantesMeLanbide32.ERROR;
                }
            }
            else
            {
                return ConstantesMeLanbide32.ERROR;
            }
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
    
    public EspecialidadVO getEspecialidadPorCodigo(Integer codEsp, AdaptadorSQLBD adaptador) throws Exception
    {
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            MeLanbide32DAO meLanbide32DAO = MeLanbide32DAO.getInstance();
            return meLanbide32DAO.getEspecialidadPorCodigo(codEsp, con);
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
            MeLanbide32DAO meLanbide32DAO = MeLanbide32DAO.getInstance();
            return meLanbide32DAO.getHorasAsignadasUbicacionORI(codUbic, con);
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
    
    public String getUbicacionAdjudicadaCE(Integer codUbic, AdaptadorSQLBD adaptador) throws Exception
    {
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            MeLanbide32DAO meLanbide32DAO = MeLanbide32DAO.getInstance();
            return meLanbide32DAO.getUbicacionAdjudicadaCE(codUbic, con);
        }
        catch(BDException e)
        {
            log.error("Se ha producido una excepci贸n en la BBDD recuperando valor adjudicada para ubicacion "+codUbic, e);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido una excepci贸n en la BBDD recuperando valor adjudicada para ubicacion "+codUbic, ex);
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
    
    public String getUbicacionAdjudicadaCEMP2014(Integer codUbic, Integer order,  AdaptadorSQLBD adaptador) throws Exception
    {
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            MeLanbide32DAO meLanbide32DAO = MeLanbide32DAO.getInstance();
            return meLanbide32DAO.getUbicacionAdjudicadaCEMP2014(codUbic,order, con);
        }
        catch(BDException e)
        {
            log.error("Se ha producido una excepcion en la BBDD recuperando valor adjudicada para ubicacion "+codUbic, e);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido una excepci贸n en la BBDD recuperando valor adjudicada para ubicacion "+codUbic, ex);
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
    
    public String getUbicacionAdjudicadaCEMP2014dsd2017(Integer codUbic, Integer order,  AdaptadorSQLBD adaptador) throws Exception
    {
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            MeLanbide32DAO meLanbide32DAO = MeLanbide32DAO.getInstance();
            return meLanbide32DAO.getUbicacionAdjudicadaCEMP2014dsd2017(codUbic,order, con);
        }
        catch(BDException e)
        {
            log.error("Se ha producido una excepcion en la BBDD recuperando valor adjudicada para ubicacion conv >= 2017 "+codUbic, e);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido una excepci贸n en la BBDD recuperando valor adjudicada para ubicacion conv >= 2017 "+codUbic, ex);
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
    
    public int actualizarValoracionTrayectoriaUbicaciones(EntidadVO entidad, Integer valor, AdaptadorSQLBD adaptador) throws Exception
    {
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            MeLanbide32DAO meLanbide32DAO = MeLanbide32DAO.getInstance();
            return meLanbide32DAO.actualizarValoracionTrayectoriaUbicaciones(entidad, valor, con);
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
            MeLanbide32DAO meLanbide32DAO = MeLanbide32DAO.getInstance();
            return meLanbide32DAO.entidadTieneUbicaciones(entidad, con);
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
    
    public List<OriUbicacionVO> getUbicacionesDeEntidadORI(EntidadVO entidad, AdaptadorSQLBD adaptador) throws Exception
    {
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            MeLanbide32DAO meLanbide32DAO = MeLanbide32DAO.getInstance();
            return meLanbide32DAO.getUbicacionesDeEntidadORI(entidad, con);
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
    
    public int modificarUbicacionesORI(List<OriUbicacionVO> ubicList, AdaptadorSQLBD adaptador) throws Exception
    {
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            adaptador.inicioTransaccion(con);
            MeLanbide32DAO meLanbide32DAO = MeLanbide32DAO.getInstance();
            int cont = 0;
            OriUbicacionVO act = null;
            for(OriUbicacionVO ubic : ubicList)
            {
                act = meLanbide32DAO.modificarUbicacionORI(ubic, con);
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
                MeLanbide32DAO meLanbide32DAO = MeLanbide32DAO.getInstance();
                retValue = meLanbide32DAO.crearAuditoria(aud, con);
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
            MeLanbide32DAO meLanbide32DAO = MeLanbide32DAO.getInstance();
            retList = meLanbide32DAO.filtrarAuditoriaProcesos(nombre, feDesde, feHasta, codProceso, codProcedimiento, codIdioma, con);
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
            MeLanbide32DAO meLanbide32DAO = MeLanbide32DAO.getInstance();
            retList = meLanbide32DAO.getEntidadesQueNoEstanEnEstado(ejercicio, codProc, tramites, con);
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
    
    public List<CeUbicacionVO> getUbicacionesDeEntidadCE(EntidadVO entidad, AdaptadorSQLBD adaptador) throws Exception
    {
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            MeLanbide32DAO meLanbide32DAO = MeLanbide32DAO.getInstance();
            return meLanbide32DAO.getUbicacionesDeEntidadCE(entidad, con);
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
    
    public TerceroVO getTerceroPorCodigo(Long codTercero, AdaptadorSQLBD adaptador) throws Exception
    {
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            MeLanbide32DAO meLanbide32DAO = MeLanbide32DAO.getInstance();
            return meLanbide32DAO.getTerceroPorCodigo(codTercero, con);
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
            MeLanbide32DAO meLanbide32DAO = MeLanbide32DAO.getInstance();
            return meLanbide32DAO.getDomicilioTercero(ter, con);
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
            MeLanbide32DAO meLanbide32DAO = MeLanbide32DAO.getInstance();
            return meLanbide32DAO.getTipoViaDomicilio(dom, con);
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
            MeLanbide32DAO meLanbide32DAO = MeLanbide32DAO.getInstance();
            return meLanbide32DAO.getViaDomicilio(dom, con);
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
            MeLanbide32DAO meLanbide32DAO = MeLanbide32DAO.getInstance();
            return meLanbide32DAO.getTerceroPorExpedienteYRol(codOrganizacion, numExpediente, ejercicio, rol, con);
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
    
    public List<OriUbicacionVO> getUbicacionesAdjudicadasDeEntidadORI(EntidadVO entidad, AdaptadorSQLBD adaptador) throws Exception
    {
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            MeLanbide32DAO meLanbide32DAO = MeLanbide32DAO.getInstance();
            return meLanbide32DAO.getUbicacionesAdjudicadasDeEntidadORI(entidad, con);
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
    
    public List<CeUbicacionVO> getUbicacionesAdjudicadasDeEntidadCE(EntidadVO entidad, AdaptadorSQLBD adaptador) throws Exception
    {
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            MeLanbide32DAO meLanbide32DAO = MeLanbide32DAO.getInstance();
            return meLanbide32DAO.getUbicacionesAdjudicadasDeEntidadCE(entidad, con);
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
            MeLanbide32DAO meLanbide32DAO = MeLanbide32DAO.getInstance();
            return meLanbide32DAO.getCodigoInternoTramite(codOrganizacion, codProc, codTramite, con);
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
            MeLanbide32DAO meLanbide32DAO = MeLanbide32DAO.getInstance();
            return meLanbide32DAO.expedienteEstaCerradoOAnulado(codOrganizacion, numExpediente, ejercicio, con);
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
            MeLanbide32DAO meLanbide32DAO = MeLanbide32DAO.getInstance();
            return meLanbide32DAO.getCodigoUltimoTramiteAbierto(codOrganizacion, ejercicio, codProc, numExp, ocurrencia, con);
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
    
    public int getAnosTrayectoriaCE(EntidadVO entidad, AdaptadorSQLBD adaptador) throws Exception
    {
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            MeLanbide32DAO meLanbide32DAO = MeLanbide32DAO.getInstance();
            return meLanbide32DAO.getAnosTrayectoriaCE(entidad, con);
        }
        catch(BDException e)
        {
            log.error("Se ha producido una excepci髇 en la BBDD recuperando los a駉s de trayectoria CE para la entidad "+(entidad != null ? entidad.getOriEntCod() : "(entidad = null)"), e);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido una excepci髇 en la BBDD recuperando los a駉s de trayectoria CE para la entidad "+(entidad != null ? entidad.getOriEntCod() : "(entidad = null)"), ex);
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
    
    public int getAnosTrayectoriaValidadaCE(EntidadVO entidad, AdaptadorSQLBD adaptador) throws Exception
    {
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            MeLanbide32DAO meLanbide32DAO = MeLanbide32DAO.getInstance();
            return meLanbide32DAO.getAnosTrayectoriaValidadaCE(entidad, con);
        }
        catch(BDException e)
        {
            log.error("Se ha producido una excepci髇 en la BBDD recuperando los a駉s de trayectoria validad CE para la entidad "+(entidad != null ? entidad.getOriEntCod() : "(entidad = null)"), e);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido una excepci髇 en la BBDD recuperando los a駉s de trayectoria validada CE para la entidad "+(entidad != null ? entidad.getOriEntCod() : "(entidad = null)"), ex);
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
    
    public String getMotivoDenegacionCEMP2014_ExcelResol(EntidadVO entidad , AdaptadorSQLBD adaptador) throws Exception
    {
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            MeLanbide32DAO meLanbide32DAO = MeLanbide32DAO.getInstance();
            return meLanbide32DAO.getMotivoDenegacionCEMP2014_ExcelResol(entidad, con);
        }
        catch(BDException e)
        {
            log.error("Se ha producido una excepcionn en la BBDD recuperando el motivo de denegacion en la generacion de excel resolucion -CEMP Procesos- para el expediente "+entidad.getExtNum(), e);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido una excepcion en la BBDD recuperando el motivo de denegacion en la generacion de excel resolucion -CEMP Procesos- para el expediente "+entidad.getExtNum(), ex);
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
    
    public Boolean expteEstaTramiteResolProvisional(String numExp, AdaptadorSQLBD adaptador) throws Exception {
        log.debug("expteEstaTramiteResolProvisional CEMP - Begin() - Manager");
        Connection con = null;
        try {
            con = adaptador.getConnection();
            return MeLanbide32DAO.getInstance().expteEstaTramiteResolProvisional(numExp, con);
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

    public String getFechaRegistroExpediente(String numExp, String codPro, AdaptadorSQLBD adaptador) throws Exception {
        log.debug("getFechaRegistroExpediente CEMP - Begin() - Manager");
        Connection con = null;
        try {
            con = adaptador.getConnection();
            return MeLanbide32DAO.getInstance().getFechaRegistroExpediente(numExp, codPro, con);
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

    public List<FilaAmbitoSolicitadoCempVO> getAmbitosSolicitadosCEMP(String numExpediente, AdaptadorSQLBD adaptador) throws Exception {
        Connection con = null;
        try {
            con = adaptador.getConnection();
            MeLanbide32DAO meLanbide32DAO = MeLanbide32DAO.getInstance();
            return meLanbide32DAO.getAmbitosSolicitadosCEMP(numExpediente, con);
        } catch (BDException e) {
            log.error("Se ha producido BDException  al getAmbitosSolicitadosCEMP " + numExpediente, e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("SSe ha producido Exception al getAmbitosSolicitadosCEMP expediente " + numExpediente, ex);
            throw new Exception(ex);
        } finally {
            try {
                adaptador.devolverConexion(con);
            } catch (Exception e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    public AmbitoSolicitadoCempVO getAmbitoSolicitadoCEMPPorCodigo(AmbitoSolicitadoCempVO ambiSol, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try {
            con = adapt.getConnection();
            MeLanbide32DAO meLanbide3DAO = MeLanbide32DAO.getInstance();
            return meLanbide3DAO.getAmbitoSolicitadoCEMPPorCodigo(ambiSol, con);
        } catch (BDException e) {
            log.error("Se ha producido BDException  al getAmbitoSolicitadoCEMPPorCodigo " + (ambiSol != null ? ambiSol.getOriAmbSolNumExp() : ""), e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("SSe ha producido Exception al getAmbitoSolicitadoCEMPPorCodigo expediente " + (ambiSol != null ? ambiSol.getOriAmbSolNumExp() : ""), ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (Exception e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    public AmbitoSolicitadoCempVO guardarAmbitoSolicitadoCEMP(AmbitoSolicitadoCempVO ambitSol, AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try {
            con = adapt.getConnection();
            MeLanbide32DAO meLanbide3DAO = MeLanbide32DAO.getInstance();
            return meLanbide3DAO.guardarAmbitoSolicitadoCEMP(ambitSol, con);
        } catch (BDException e) {
            log.error("Se ha producido BDException  al guardarAmbitoSolicitadoCEMP " + (ambitSol != null ? ambitSol.getOriAmbSolNumExp() : ""), e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("SSe ha producido Exception al guardarAmbitoSolicitadoCEMP expediente " + (ambitSol != null ? ambitSol.getOriAmbSolNumExp() : ""), ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (Exception e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }

    public void eliminarAmbitoSolicitadoCEMP(AmbitoSolicitadoCempVO ambiSol, AdaptadorSQLBD adaptador) throws Exception {
        log.debug("eliminarAmbitoSolicitadoCEMP() - Begin()");
        Connection con = null;
        try {
            con = adaptador.getConnection();
            MeLanbide32DAO meLanbide47DAO = MeLanbide32DAO.getInstance();
            if (ambiSol != null) {
                int eliminados = meLanbide47DAO.eliminarAmbitoSolicitadoCEMP(ambiSol, con);
                if (eliminados > 0) {
                    log.debug("Se han eliminado " + eliminados + " Ambitos Solicitados para el expediente :" + (ambiSol != null ? ambiSol.getOriAmbSolNumExp() : "Sin Datos Numero Expediente"));
                } else {
                    log.error("No se ha podido eliminar ningun registro de ambitos solicitado para el expediente  : " + (ambiSol != null ? ambiSol.getOriAmbSolNumExp() : "Sin Datos Numero Expediente"));
                }
            } else {
                throw new BDException();
            }
        } catch (BDException e) {
            log.error("Se ha producido una BDException en la BBDD eliminando Ambitos Solicitado del expediente :" + (ambiSol != null ? ambiSol.getOriAmbSolNumExp() : "Sin Datos Numero Expediente"), e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una Exception en la BBDD eliminando Ambitos Solicitado del expediente :" + (ambiSol != null ? ambiSol.getOriAmbSolNumExp() : "Sin Datos Numero Expediente"), ex);
            throw new Exception(ex);
        } finally {
            try {
                adaptador.devolverConexion(con);
            } catch (BDException e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
        log.debug("eliminarAmbitoSolicitadoCEMP() - End()");
    }

    public List<SelectItem> getDesplegableTipoAmbitosCEMP(AdaptadorSQLBD adapt) throws Exception {
        Connection con = null;
        try {
            con = adapt.getConnection();
            MeLanbide32DAO meLanbide3DAO = MeLanbide32DAO.getInstance();
            return meLanbide3DAO.getDesplegableTipoAmbitosCEMP(con);
        } catch (BDException e) {
            log.error("Se ha producido BDException  al getDesplegableTipoAmbitosCEMP " + e.getMensaje(), e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("SSe ha producido Exception al getDesplegableTipoAmbitosCEMP  " + ex.getMessage(), ex);
            throw new Exception(ex);
        } finally {
            try {
                adapt.devolverConexion(con);
            } catch (Exception e) {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }    
    
    public int getPuntuacionUbicacionCE(CeUbicacionVO ubicacion, AdaptadorSQLBD adaptador) throws Exception {
        Connection con = null;
        try {
            con = adaptador.getConnection();
            MeLanbide32DAO meLanbide32DAO = MeLanbide32DAO.getInstance();
            return meLanbide32DAO.getPuntuacionUbicacionCE(ubicacion, con);
        } catch (BDException e) {
            log.error("Se ha producido una excepci髇 en la BBDD recuperando sumatoria puntuacion Ubicacion " + (ubicacion != null ? ubicacion.getOriEntCod()+"/"+ubicacion.getOriCeUbicCod() : "(entidad = null)"), e);
            throw new Exception(e);
        } catch (Exception ex) {
            log.error("Se ha producido una  excepci髇 en la BBDD recuperando sumatoria puntuacion Ubicacion " + (ubicacion != null ? ubicacion.getOriEntCod()+"/"+ubicacion.getOriCeUbicCod() : "(entidad = null)"), ex);
            throw new Exception(ex);
        } finally {
            try {
                adaptador.devolverConexion(con);
            } catch (Exception e) {
                log.error("Error al cerrar conexi髇 a la BBDD: " + e.getMessage());
            }
        }
    }
}

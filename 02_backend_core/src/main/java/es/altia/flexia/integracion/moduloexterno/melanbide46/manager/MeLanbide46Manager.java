/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.melanbide46.manager;

import es.altia.flexia.integracion.moduloexterno.melanbide46.dao.MeLanbide46DAO;
import es.altia.flexia.integracion.moduloexterno.melanbide46.util.ConfigurationParameter;
import es.altia.flexia.integracion.moduloexterno.melanbide46.util.ConstantesMeLanbide46;
import es.altia.flexia.integracion.moduloexterno.melanbide46.util.MeLanbide46MappingUtils;
import es.altia.flexia.integracion.moduloexterno.melanbide46.util.MeLanbide46Utils;
import es.altia.flexia.integracion.moduloexterno.melanbide46.vo.CmeJustificacionVO;
import es.altia.flexia.integracion.moduloexterno.melanbide46.vo.CmeOfertaVO;
import es.altia.flexia.integracion.moduloexterno.melanbide46.vo.CmePuestoVO;
import es.altia.flexia.integracion.moduloexterno.melanbide46.vo.FilaInformeDatosPuestosVO;
import es.altia.flexia.integracion.moduloexterno.melanbide46.vo.FilaInformeHoja2DatosPuestosVO;
import es.altia.flexia.integracion.moduloexterno.melanbide46.vo.FilaInformeResumenEconomicoVO;
import es.altia.flexia.integracion.moduloexterno.melanbide46.vo.FilaInformeResumenPuestosContratadosVO;
import es.altia.flexia.integracion.moduloexterno.melanbide46.vo.FilaJustificacionVO;
import es.altia.flexia.integracion.moduloexterno.melanbide46.vo.FilaOfertaVO;
import es.altia.flexia.integracion.moduloexterno.melanbide46.vo.FilaPersonaContratadaVO;
import es.altia.flexia.integracion.moduloexterno.melanbide46.vo.FilaPuestoVO;
import es.altia.flexia.integracion.moduloexterno.melanbide46.vo.FilaResultadoBusqTitulaciones;
import es.altia.flexia.integracion.moduloexterno.melanbide46.vo.FilaResumenVO;
import es.altia.flexia.integracion.moduloexterno.melanbide46.vo.SalarioVO;
import es.altia.flexia.integracion.moduloexterno.melanbide46.vo.SelectItem;
import es.altia.flexia.integracion.moduloexterno.melanbide46.vo.datosCmeVO;
import es.altia.flexia.integracion.moduloexterno.plugin.persistence.vo.ValorCampoDesplegableModuloIntegracionVO;
import es.altia.util.conexion.AdaptadorSQLBD;
import es.altia.util.conexion.BDException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

/**
 *
 * @author santiagoc
 */
public class MeLanbide46Manager 
{
    //Logger
    private static Logger log = LogManager.getLogger(MeLanbide46Manager.class);
    
    //Instancia
    private static MeLanbide46Manager instance = null;
    
    private MeLanbide46Manager()
    {
        
    }
    
    public static MeLanbide46Manager getInstance()
    {
        if(instance == null)
        {
            synchronized(MeLanbide46Manager.class)
            {
                instance = new MeLanbide46Manager();
            }
        }
        return instance;
    }      
    
    public BigDecimal getValorCampoNumerico(int codOrganizacion, String ejercicio, String numExpediente, String codCampo, AdaptadorSQLBD adaptador) throws Exception
    {
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            MeLanbide46DAO meLanbide46DAO = MeLanbide46DAO.getInstance();
            return meLanbide46DAO.getValorCampoNumerico(codOrganizacion, numExpediente, ejercicio, codCampo, con);
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
            catch(Exception e)
            {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }
    
    public void guardaHistorico(String numExpediente, String codTramite, AdaptadorSQLBD adaptador) throws Exception
    {
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            MeLanbide46DAO meLanbide46DAO = MeLanbide46DAO.getInstance();
            meLanbide46DAO.guardarHistorico(numExpediente, codTramite, con);
        }
        catch(BDException e)
        {
            log.error("Se ha producido una excepcion en la BBDD al generar el histórico con número de expediente "+ numExpediente, e);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido una excepcion en la BBDD al generar el histórico con número de expediente "+numExpediente, ex);
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
    
    public void borraHistorico(String numExpediente, String codTramite, AdaptadorSQLBD adaptador) throws Exception
    {
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            MeLanbide46DAO meLanbide46DAO = MeLanbide46DAO.getInstance();
            meLanbide46DAO.borrarHistorico(numExpediente, codTramite, con);
        }
        catch(BDException e)
        {
            log.error("Se ha producido una excepcion en la BBDD al borrar el histórico con número de expediente "+ numExpediente, e);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido una excepcion en la BBDD al borrar el histórico con número de expediente "+numExpediente, ex);
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
    
    public int guardarValorCampoNumerico(int codOrganizacion, String ejercicio, String numExpediente, String codCampo, BigDecimal valor, AdaptadorSQLBD adaptador) throws Exception
    {
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            MeLanbide46DAO meLanbide46DAO = MeLanbide46DAO.getInstance();
            return meLanbide46DAO.guardarValorCampoNumerico(codOrganizacion, numExpediente, ejercicio, codCampo, valor, con);
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
            catch(Exception e)
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
            MeLanbide46DAO meLanbide46DAO = MeLanbide46DAO.getInstance();
            return meLanbide46DAO.getValorCampoTexto(codOrganizacion, numExpediente, ejercicio, codCampo, con);
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
            catch(Exception e)
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
            MeLanbide46DAO meLanbide46DAO = MeLanbide46DAO.getInstance();
            return meLanbide46DAO.guardarValorCampoTexto(codOrganizacion, numExpediente, ejercicio, codCampo, valor, con);
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
            catch(Exception e)
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
            MeLanbide46DAO meLanbide46DAO = MeLanbide46DAO.getInstance();
            return meLanbide46DAO.getValorCampoDesplegable(codOrganizacion, numExpediente, ejercicio, codCampo, con);
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
            catch(Exception e)
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
            MeLanbide46DAO meLanbide46DAO = MeLanbide46DAO.getInstance();
            return meLanbide46DAO.guardarValorCampoDesplegable(codOrganizacion, numExpediente, ejercicio, codCampo, valor, con);
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
            catch(Exception e)
            {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }
    

    
    public int guardarDatosCme(int codOrganizacion, String ejercicio, String numExpediente, String gestor, String empresa, BigDecimal impPagado, BigDecimal impPagado2, BigDecimal otrasAyudasSolic, BigDecimal otrasAyudasConce, BigDecimal minimisSolic, BigDecimal minimisConce, BigDecimal importeConcedido,BigDecimal importeRenunciaRes, BigDecimal importeReintegrar,BigDecimal importeJustificado, AdaptadorSQLBD adaptador) throws Exception
    {
        Connection con = null;
        String campoActual = "";
        BigDecimal cero = new BigDecimal(ConstantesMeLanbide46.CERO_CON_DECIMALES);
        try
        {
            con = adaptador.getConnection();
            adaptador.inicioTransaccion(con);
            int res = 0;
            MeLanbide46DAO meLanbide46DAO = MeLanbide46DAO.getInstance();
            campoActual = ConfigurationParameter.getParameter(ConstantesMeLanbide46.CAMPO_SUPL_GESTOR_TRAMITADOR, ConstantesMeLanbide46.FICHERO_PROPIEDADES);
            if(gestor != null && !gestor.equals(""))
            {
                res += meLanbide46DAO.guardarValorCampoDesplegable(codOrganizacion, String.valueOf(ejercicio), numExpediente, campoActual, gestor, con);
                if(res <= 0)
                {
                    throw new Exception();
                }
            }

            campoActual = ConfigurationParameter.getParameter(ConstantesMeLanbide46.CAMPO_SUPL_EMPRESA, ConstantesMeLanbide46.FICHERO_PROPIEDADES);
            if(empresa != null && !empresa.equals(""))
            {
                res += meLanbide46DAO.guardarValorCampoTexto(codOrganizacion,  String.valueOf(ejercicio), numExpediente, campoActual, empresa, con);
                if(res <= 0)
                {
                    throw new Exception();
                }
            }

            //En funcion del tramite en que se encuentre el expediente, habra que guardar en un campo o en otro
            
            //Miro a ver si tiene iniciado el tramite de "Resolucion de concesion o denegacion"
            Long codTramResModif = MeLanbide46Manager.getInstance().obtenerCodigoInternoTramite(codOrganizacion, ConstantesMeLanbide46.CODIGO_TRAMITE_RESOLUCION_MODIF, adaptador);
            if(codTramResModif != null)
            {
                if(tieneTramiteFinalizado(codOrganizacion, Integer.parseInt(ejercicio), numExpediente, codTramResModif, adaptador))
                {
                    campoActual = ConfigurationParameter.getParameter(ConstantesMeLanbide46.CAMPO_SUPL_IMP_PAG_RES, ConstantesMeLanbide46.FICHERO_PROPIEDADES);
                }
                else
                {
                    campoActual = ConfigurationParameter.getParameter(ConstantesMeLanbide46.CAMPO_SUPL_IMP_PAG, ConstantesMeLanbide46.FICHERO_PROPIEDADES);
                }
            }
            if(impPagado == null)
            {
                impPagado = new BigDecimal(ConstantesMeLanbide46.CERO_CON_DECIMALES);
            }
            else if(impPagado.compareTo(cero) < 0)
            {
                impPagado = new BigDecimal(ConstantesMeLanbide46.CERO_CON_DECIMALES);
            }
            res += meLanbide46DAO.guardarValorCampoNumerico(codOrganizacion,  String.valueOf(ejercicio), numExpediente, campoActual, impPagado, con);
            if(res <= 0)
            {
                throw new Exception();
            }

            campoActual = ConfigurationParameter.getParameter(ConstantesMeLanbide46.CAMPO_SUPL_IMP_PAG_2, ConstantesMeLanbide46.FICHERO_PROPIEDADES);
            if(impPagado2 == null)
            {
                impPagado2 = new BigDecimal(ConstantesMeLanbide46.CERO_CON_DECIMALES);
            }
            else if(impPagado.compareTo(cero) < 0)
            {
                impPagado2 = new BigDecimal(ConstantesMeLanbide46.CERO_CON_DECIMALES);
            }
            res += meLanbide46DAO.guardarValorCampoNumerico(codOrganizacion,  String.valueOf(ejercicio), numExpediente, campoActual, impPagado2, con);
            if(res <= 0)
            {
                throw new Exception();
            }

            campoActual = ConfigurationParameter.getParameter(ConstantesMeLanbide46.CAMPO_SUPL_OTRAS_AYUDAS_CONCE, ConstantesMeLanbide46.FICHERO_PROPIEDADES);
            if(otrasAyudasConce == null)
            {
                otrasAyudasConce = new BigDecimal(ConstantesMeLanbide46.CERO_CON_DECIMALES);
            }
            else if(impPagado.compareTo(cero) < 0)
            {
                otrasAyudasConce = new BigDecimal(ConstantesMeLanbide46.CERO_CON_DECIMALES);
            }
            res += meLanbide46DAO.guardarValorCampoNumerico(codOrganizacion,  String.valueOf(ejercicio), numExpediente, campoActual, otrasAyudasConce, con);
            if(res <= 0)
            {
                throw new Exception();
            }

            campoActual = ConfigurationParameter.getParameter(ConstantesMeLanbide46.CAMPO_SUPL_OTRAS_AYUDAS_SOLIC, ConstantesMeLanbide46.FICHERO_PROPIEDADES);
            if(otrasAyudasSolic == null)
            {
                otrasAyudasSolic = new BigDecimal(ConstantesMeLanbide46.CERO_CON_DECIMALES);
            }
            else if(impPagado.compareTo(cero) < 0)
            {
                otrasAyudasSolic = new BigDecimal(ConstantesMeLanbide46.CERO_CON_DECIMALES);
            }
            res += meLanbide46DAO.guardarValorCampoNumerico(codOrganizacion,  String.valueOf(ejercicio), numExpediente, campoActual, otrasAyudasSolic, con);
            if(res <= 0)
            {
                throw new Exception();
            }

            campoActual = ConfigurationParameter.getParameter(ConstantesMeLanbide46.CAMPO_SUPL_MINIMIS_SOLIC, ConstantesMeLanbide46.FICHERO_PROPIEDADES);
            if(minimisSolic == null)
            {
                minimisSolic = new BigDecimal(ConstantesMeLanbide46.CERO_CON_DECIMALES);
            }
            else if(impPagado.compareTo(cero) < 0)
            {
                minimisSolic = new BigDecimal(ConstantesMeLanbide46.CERO_CON_DECIMALES);
            }
            res += meLanbide46DAO.guardarValorCampoNumerico(codOrganizacion,  String.valueOf(ejercicio), numExpediente, campoActual, minimisSolic, con);
            if(res <= 0)
            {
                throw new Exception();
            }

            campoActual = ConfigurationParameter.getParameter(ConstantesMeLanbide46.CAMPO_SUPL_MINIMIS_CONCE, ConstantesMeLanbide46.FICHERO_PROPIEDADES);
            if(minimisConce == null)
            {
                minimisConce = new BigDecimal(ConstantesMeLanbide46.CERO_CON_DECIMALES);
            }
            else if(impPagado.compareTo(cero) < 0)
            {
                minimisConce = new BigDecimal(ConstantesMeLanbide46.CERO_CON_DECIMALES);
            }
            res += meLanbide46DAO.guardarValorCampoNumerico(codOrganizacion,  String.valueOf(ejercicio), numExpediente, campoActual, minimisConce, con);
            if(res <= 0)
            {
                throw new Exception();
            }

            campoActual = ConfigurationParameter.getParameter(ConstantesMeLanbide46.CAMPO_SUPL_IMP_CON, ConstantesMeLanbide46.FICHERO_PROPIEDADES);
            if(importeConcedido == null)
            {
                importeConcedido = new BigDecimal(ConstantesMeLanbide46.CERO_CON_DECIMALES);
            }
            else if(impPagado.compareTo(cero) < 0)
            {
                importeConcedido = new BigDecimal(ConstantesMeLanbide46.CERO_CON_DECIMALES);
            }
            res += meLanbide46DAO.guardarValorCampoNumerico(codOrganizacion,  String.valueOf(ejercicio), numExpediente, campoActual, importeConcedido, con);
            if(res <= 0)
            {
                throw new Exception();
            }

            campoActual = ConfigurationParameter.getParameter(ConstantesMeLanbide46.CAMPO_SUPL_IMP_REI, ConstantesMeLanbide46.FICHERO_PROPIEDADES);
            if(importeReintegrar == null)
            {
                importeReintegrar = new BigDecimal(ConstantesMeLanbide46.CERO_CON_DECIMALES);
            }
            else if(impPagado.compareTo(cero) < 0)
            {
                importeReintegrar = new BigDecimal(ConstantesMeLanbide46.CERO_CON_DECIMALES);
            }
            res += meLanbide46DAO.guardarValorCampoNumerico(codOrganizacion,  String.valueOf(ejercicio), numExpediente, campoActual, importeReintegrar, con);
            if(res <= 0)
            {
                throw new Exception();
            }
            
            campoActual = ConfigurationParameter.getParameter(ConstantesMeLanbide46.CAMPO_SUPL_IMP_REN_RES, ConstantesMeLanbide46.FICHERO_PROPIEDADES);
            if(importeRenunciaRes == null)
            {
                importeRenunciaRes = new BigDecimal(ConstantesMeLanbide46.CERO_CON_DECIMALES);
            }
            else if(impPagado.compareTo(cero) < 0)
            {
                importeRenunciaRes = new BigDecimal(ConstantesMeLanbide46.CERO_CON_DECIMALES);
            }
            res += meLanbide46DAO.guardarValorCampoNumerico(codOrganizacion,  String.valueOf(ejercicio), numExpediente, campoActual, importeRenunciaRes, con);
            if(res <= 0)
            {
                throw new Exception();
            }
            
            campoActual = ConfigurationParameter.getParameter(ConstantesMeLanbide46.CAMPO_SUPL_IMP_JUS, ConstantesMeLanbide46.FICHERO_PROPIEDADES);
            if(importeJustificado == null)
            {
                importeJustificado = new BigDecimal(ConstantesMeLanbide46.CERO_CON_DECIMALES);
            }
            else if(importeJustificado.compareTo(cero) < 0)
            {
                importeJustificado = new BigDecimal(ConstantesMeLanbide46.CERO_CON_DECIMALES);
            }
            res += meLanbide46DAO.guardarValorCampoNumerico(codOrganizacion,  String.valueOf(ejercicio), numExpediente, campoActual, importeJustificado, con);
            if(res <= 0)
            {
                throw new Exception();
            }
            adaptador.finTransaccion(con);
            return res;
        }
        catch(BDException e)
        {
            adaptador.rollBack(con);
            log.error("Se ha producido un error grabando el campo suplementario desplegable "+campoActual+" para el expediente "+numExpediente, e);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            adaptador.rollBack(con);
            log.error("Se ha producido un error grabando el campo suplementario desplegable "+campoActual+" para el expediente "+numExpediente, ex);
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
    
    public List<SelectItem> getListaPaises(AdaptadorSQLBD adaptador) throws Exception
    {
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            MeLanbide46DAO meLanbide46DAO = MeLanbide46DAO.getInstance();
            return meLanbide46DAO.getListaPaises(con);
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
            catch(Exception e)
            {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }
    
    public Map<String, BigDecimal> cargarCalculosCme(int codOrganizacion, Integer ejercicio, String numExpediente, AdaptadorSQLBD adaptador) throws Exception
    {
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            MeLanbide46DAO meLanbide46DAO = MeLanbide46DAO.getInstance();
            return meLanbide46DAO.cargarCalculosCme(codOrganizacion, ejercicio, numExpediente, con);
        }
        catch(BDException e)
        {
            log.error("Se ha producido un error calculando importes para expediente "+numExpediente, e);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error calculando importes para expediente "+numExpediente, ex);
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
    
    public CmePuestoVO getPuestoPorCodigoYExpediente(CmePuestoVO p, AdaptadorSQLBD adaptador) throws Exception
    {
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            MeLanbide46DAO meLanbide46DAO = MeLanbide46DAO.getInstance();
            return meLanbide46DAO.getPuestoPorCodigoYExpediente(p, con);
        }
        catch(BDException e)
        {
            log.error("Se ha producido un error recuperando puesto "+(p != null ? p.getCodPuesto() : "(puesto = null)")+" para expediente "+(p != null ? p.getNumExp() : "(puesto = null)"), e);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error recuperando puesto "+(p != null ? p.getCodPuesto() : "(puesto = null)")+" para expediente "+(p != null ? p.getNumExp() : "(puesto = null)"), ex);
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
    
   
    public CmePuestoVO guardarCmePuestoVO(int codOrganizacion, CmePuestoVO puesto, BigDecimal impAnterior, String estadoAnterior, AdaptadorSQLBD adaptador) throws Exception
    {
        Connection con = null;
        try
        {
            boolean nuevo = puesto != null && puesto.getCodPuesto() != null ? false : true;
            con = adaptador.getConnection();
            adaptador.inicioTransaccion(con);
            MeLanbide46DAO meLanbide46DAO = MeLanbide46DAO.getInstance();
            puesto = meLanbide46DAO.guardarCmePuestoVO(puesto, con);
            if(puesto != null)
            {
                guardarCalculosCme(codOrganizacion, puesto, impAnterior, false, nuevo, adaptador, con);
                guardarCalculosPuestos(codOrganizacion, puesto.getEjercicio(), puesto.getNumExp(), adaptador, con);
                CmeOfertaVO oferta = null;
                boolean puestoRenuncia = puesto.getCodResult() != null && puesto.getCodResult().equalsIgnoreCase(ConstantesMeLanbide46.CODIGO_RESULTADO_RENUNCIA);
                if(nuevo)
                {
                    //Cuando se crea un puesto, automaticamente se crea tb la oferta
                    oferta = new CmeOfertaVO();
                    oferta.setCodPuesto(puesto.getCodPuesto());
                    oferta.setExpEje(puesto.getEjercicio());
                    oferta.setNumExp(puesto.getNumExp());
                    
                    oferta.setDescPuesto(puesto.getDescPuesto());
                    oferta.setPaiCod1(puesto.getPaiCod1());
                    oferta.setPaiCod2(puesto.getPaiCod2());
                    oferta.setPaiCod3(puesto.getPaiCod3());
                    oferta.setCodTit1(puesto.getCodTit1());
                    oferta.setCodTit2(puesto.getCodTit2());
                    oferta.setCodTit3(puesto.getCodTit3());
                    oferta.setFunciones(puesto.getFunciones());
                    oferta.setCodIdioma1(puesto.getCodIdioma1());
                    oferta.setCodIdioma2(puesto.getCodIdioma2());
                    oferta.setCodIdioma3(puesto.getCodIdioma3());
                    oferta.setCodNivIdi1(puesto.getCodNivIdi1());
                    oferta.setCodNivIdi2(puesto.getCodNivIdi2());
                    oferta.setCodNivIdi3(puesto.getCodNivIdi3());
                    oferta.setCodNivForm(puesto.getCodNivForm());
                    
                    oferta.setCiudadDestino(puesto.getCiudadDestino());
                    oferta.setDpto(puesto.getDpto());
                    oferta = meLanbide46DAO.guardarCmeOfertaVO(oferta, con);
                    
                    //Cuando se crea una oferta, automaticamente se crea tb la justificacion    
                    CmeJustificacionVO justif = new CmeJustificacionVO();
                    justif.setCodPuesto(oferta.getCodPuesto());
                    justif.setEjercicio(oferta.getExpEje());
                    justif.setIdOferta(oferta.getIdOferta());
                    justif.setNumExpediente(oferta.getNumExp());
                    meLanbide46DAO.guardarCmeJustificacionVO(justif, con);
                }
                if(puestoRenuncia)
                {
                    meLanbide46DAO.actualizarOfertasPuestoPorRenuncia(puesto, true, con);
                    meLanbide46DAO.actualizarJustificacionesPuestoPorRenuncia(puesto, true, con);
                }
                else if(estadoAnterior != null && estadoAnterior.equalsIgnoreCase(ConstantesMeLanbide46.CODIGO_ESTADO_RENUNCIA))
                {
                    //Si se ha cambiado el resultado de RENUNCIA a otro cualquiera, borramos la fecha y causa de baja
                    meLanbide46DAO.actualizarOfertasPuestoPorRenuncia(puesto, false, con);
                    meLanbide46DAO.actualizarJustificacionesPuestoPorRenuncia(puesto, false, con);
                }
                adaptador.finTransaccion(con);
                return puesto;
            }
            else
            {
                throw new BDException();
            }
        }
        catch(BDException e)
        {
            log.error("Se ha producido una excepcion en la BBDD guardando puesto "+(puesto != null ? puesto.getCodPuesto() : "(puesto = null)")+" para el expediente "+(puesto != null ? puesto.getNumExp() : "(puesto = null)"), e);
            adaptador.rollBack(con);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido una excepcion en la BBDD guardando puesto "+(puesto != null ? puesto.getCodPuesto() : "(puesto = null)")+" para el expediente "+(puesto != null ? puesto.getNumExp() : "(puesto = null)"), ex);
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
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
                adaptador.rollBack(con);
            }
        }
    }
    
    public List<FilaPuestoVO> getListaPuestosPorExpediente(String numExpediente, AdaptadorSQLBD adaptador) throws Exception
    {
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            MeLanbide46DAO meLanbide46DAO = MeLanbide46DAO.getInstance();
            return meLanbide46DAO.getListaPuestosPorExpediente(numExpediente, con);
        }
        catch(BDException e)
        {
            log.error("Se ha producido un error recuperando puestos para expediente "+numExpediente, e);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error recuperando puestos para expediente "+numExpediente, ex);
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
    
    public List<FilaPuestoVO> getListaPuestosHistPorExpediente(int codOrganizacion, String numExpediente, String codTramite, AdaptadorSQLBD adaptador) throws Exception
    {
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            MeLanbide46DAO meLanbide46DAO = MeLanbide46DAO.getInstance();
            return meLanbide46DAO.getListaPuestosHistPorExpediente(codOrganizacion, numExpediente, codTramite, con);
        }
        catch(BDException e)
        {
            log.error("Se ha producido un error recuperando puestos históricos para expediente "+numExpediente, e);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error recuperando puestos históricos para expediente "+numExpediente, ex);
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
    
    public int eliminarCmePuestoVO(int codOrganizacion, CmePuestoVO puesto, AdaptadorSQLBD adaptador) throws Exception
    {
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            adaptador.inicioTransaccion(con);
            MeLanbide46DAO meLanbide46DAO = MeLanbide46DAO.getInstance();
            if(puesto != null)
            {
                meLanbide46DAO.eliminarJustificacionesPorPuestoYExpediente(puesto, con);
                meLanbide46DAO.eliminarOfertasPorPuestoYExpediente(puesto, con);
                int eliminados = meLanbide46DAO.eliminarCmePuesto(puesto, con);
                if(eliminados > 0)
                {
                    guardarCalculosCme(codOrganizacion, puesto, null, true, false, adaptador, con);
                    guardarCalculosPuestos(codOrganizacion, puesto.getEjercicio(), puesto.getNumExp(), adaptador, con);
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
            log.error("Se ha producido una excepcion en la BBDD eliminando puesto "+(puesto != null ? puesto.getCodPuesto() : "(puesto = null)")+" para el expediente "+(puesto != null ? puesto.getNumExp() : "(puesto = null)"), e);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            adaptador.rollBack(con);
            log.error("Se ha producido una excepcion en la BBDD eliminando puesto "+(puesto != null ? puesto.getCodPuesto() : "(puesto = null)")+" para el expediente "+(puesto != null ? puesto.getNumExp() : "(puesto = null)"), ex);
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
                adaptador.rollBack(con);
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }
    
    public void guardarCalculosCme(int codOrganizacion, CmePuestoVO puesto, BigDecimal impAnterior, boolean eliminar, boolean nuevo, AdaptadorSQLBD adaptador, Connection con) throws Exception
    {
        BigDecimal cero = new BigDecimal(ConstantesMeLanbide46.CERO_CON_DECIMALES);
        MeLanbide46DAO meLanbide46DAO = MeLanbide46DAO.getInstance();
        String claveCampo = null;
        String codCampo = null;
        BigDecimal valor = null;
        Map<String, BigDecimal> calculos = meLanbide46DAO.cargarCalculosCme(codOrganizacion, puesto.getEjercicio(), puesto.getNumExp(), con);

        //Importe solicitado
        claveCampo = ConstantesMeLanbide46.CAMPO_SUPL_IMP_SOLICITADO;
        codCampo = ConfigurationParameter.getParameter(claveCampo, ConstantesMeLanbide46.FICHERO_PROPIEDADES);
        valor = calculos.get(claveCampo);
        if(valor == null)
        {
            valor = new BigDecimal(ConstantesMeLanbide46.CERO_CON_DECIMALES);
        }
        if(valor.compareTo(cero) < 0)
        {
            valor = new BigDecimal(ConstantesMeLanbide46.CERO_CON_DECIMALES);
        }
        meLanbide46DAO.guardarValorCampoNumerico(codOrganizacion, String.valueOf(puesto.getEjercicio()), puesto.getNumExp(), codCampo, valor, con);

        //Importe convocatoria
        claveCampo = ConstantesMeLanbide46.CAMPO_SUPL_IMP_CONVOCATORIA;
        codCampo = ConfigurationParameter.getParameter(claveCampo, ConstantesMeLanbide46.FICHERO_PROPIEDADES);
        valor = calculos.get(claveCampo);
        if(valor == null)
        {
            valor = new BigDecimal(ConstantesMeLanbide46.CERO_CON_DECIMALES);
        }
        if(valor.compareTo(cero) < 0)
        {
            valor = new BigDecimal(ConstantesMeLanbide46.CERO_CON_DECIMALES);
        }
        meLanbide46DAO.guardarValorCampoNumerico(codOrganizacion, String.valueOf(puesto.getEjercicio()), puesto.getNumExp(), codCampo, valor, con);

        //Importe previsot concesion
        claveCampo = ConstantesMeLanbide46.CAMPO_SUPL_IMP_PREV_CON;
        codCampo = ConfigurationParameter.getParameter(claveCampo, ConstantesMeLanbide46.FICHERO_PROPIEDADES);
        valor = calculos.get(claveCampo);
        if(valor == null)
        {
            valor = new BigDecimal(ConstantesMeLanbide46.CERO_CON_DECIMALES);
        }
        if(valor.compareTo(cero) < 0)
        {
            valor = new BigDecimal(ConstantesMeLanbide46.CERO_CON_DECIMALES);
        }
        meLanbide46DAO.guardarValorCampoNumerico(codOrganizacion, String.valueOf(puesto.getEjercicio()), puesto.getNumExp(), codCampo, valor, con);

        //Importe concedido
        claveCampo = ConstantesMeLanbide46.CAMPO_SUPL_IMP_CON;
        codCampo = ConfigurationParameter.getParameter(claveCampo, ConstantesMeLanbide46.FICHERO_PROPIEDADES);
        if(this.getValorCampoNumerico(codOrganizacion, String.valueOf(puesto.getEjercicio()), puesto.getNumExp(), codCampo, adaptador) == null)
        {
            valor = calculos.get(claveCampo);
            if(valor == null)
            {
                valor = new BigDecimal(ConstantesMeLanbide46.CERO_CON_DECIMALES);
            }
            if(valor.compareTo(cero) < 0)
            {
                valor = new BigDecimal(ConstantesMeLanbide46.CERO_CON_DECIMALES);
            }
            meLanbide46DAO.guardarValorCampoNumerico(codOrganizacion, String.valueOf(puesto.getEjercicio()), puesto.getNumExp(), codCampo, valor, con);
        }

        //Importe justificado
        claveCampo = ConstantesMeLanbide46.CAMPO_SUPL_IMP_JUS;
        codCampo = ConfigurationParameter.getParameter(claveCampo, ConstantesMeLanbide46.FICHERO_PROPIEDADES);
        valor = calculos.get(claveCampo);
        if(valor == null)
        {
            valor = new BigDecimal(ConstantesMeLanbide46.CERO_CON_DECIMALES);
        }
        if(valor.compareTo(cero) < 0)
        {
            valor = new BigDecimal(ConstantesMeLanbide46.CERO_CON_DECIMALES);
        }
        meLanbide46DAO.guardarValorCampoNumerico(codOrganizacion, String.valueOf(puesto.getEjercicio()), puesto.getNumExp(), codCampo, valor, con);

        //Importe renuncia solicitud
        claveCampo = ConstantesMeLanbide46.CAMPO_SUPL_IMP_REN;
        codCampo = ConfigurationParameter.getParameter(claveCampo, ConstantesMeLanbide46.FICHERO_PROPIEDADES);
        valor = calculos.get(claveCampo);
        if(valor == null)
        {
            valor = new BigDecimal(ConstantesMeLanbide46.CERO_CON_DECIMALES);
        }
        if(valor.compareTo(cero) < 0)
        {
            valor = new BigDecimal(ConstantesMeLanbide46.CERO_CON_DECIMALES);
        }
        meLanbide46DAO.guardarValorCampoNumerico(codOrganizacion, String.valueOf(puesto.getEjercicio()), puesto.getNumExp(), codCampo, valor, con);
        
         //Importe renuncia resolución
        claveCampo = ConstantesMeLanbide46.CAMPO_SUPL_IMP_REN_RES;
        codCampo = ConfigurationParameter.getParameter(claveCampo, ConstantesMeLanbide46.FICHERO_PROPIEDADES);
        valor = calculos.get(claveCampo);
        if(valor == null)
        {
            valor = new BigDecimal(ConstantesMeLanbide46.CERO_CON_DECIMALES);
        }
        if(valor.compareTo(cero) < 0)
        {
            valor = new BigDecimal(ConstantesMeLanbide46.CERO_CON_DECIMALES);
        }
        meLanbide46DAO.guardarValorCampoNumerico(codOrganizacion, String.valueOf(puesto.getEjercicio()), puesto.getNumExp(), codCampo, valor, con);
        
        //Importe por despido
        claveCampo = ConstantesMeLanbide46.CAMPO_SUPL_IMP_DESP;
        codCampo = ConfigurationParameter.getParameter(claveCampo, ConstantesMeLanbide46.FICHERO_PROPIEDADES);
        valor = calculos.get(claveCampo);
        if(valor == null)
        {
            valor = new BigDecimal(ConstantesMeLanbide46.CERO_CON_DECIMALES);
        }
        if(valor.compareTo(cero) < 0)
        {
            valor = new BigDecimal(ConstantesMeLanbide46.CERO_CON_DECIMALES);
        }
        meLanbide46DAO.guardarValorCampoNumerico(codOrganizacion, String.valueOf(puesto.getEjercicio()), puesto.getNumExp(), codCampo, valor, con);
        
        //Importe por baja
        claveCampo = ConstantesMeLanbide46.CAMPO_SUPL_IMP_BAJA;
        codCampo = ConfigurationParameter.getParameter(claveCampo, ConstantesMeLanbide46.FICHERO_PROPIEDADES);
        valor = calculos.get(claveCampo);
        if(valor == null)
        {
            valor = new BigDecimal(ConstantesMeLanbide46.CERO_CON_DECIMALES);
        }
        if(valor.compareTo(cero) < 0)
        {
            valor = new BigDecimal(ConstantesMeLanbide46.CERO_CON_DECIMALES);
        }
        meLanbide46DAO.guardarValorCampoNumerico(codOrganizacion, String.valueOf(puesto.getEjercicio()), puesto.getNumExp(), codCampo, valor, con);
        
        //Importe primer pago
        claveCampo = ConstantesMeLanbide46.CAMPO_SUPL_IMP_PAG;
        codCampo = ConfigurationParameter.getParameter(claveCampo, ConstantesMeLanbide46.FICHERO_PROPIEDADES);
        if(this.getValorCampoNumerico(codOrganizacion, String.valueOf(puesto.getEjercicio()), puesto.getNumExp(), codCampo, adaptador) == null)
        {
            valor = calculos.get(claveCampo);
            if(valor == null)
            {
                valor = new BigDecimal(ConstantesMeLanbide46.CERO_CON_DECIMALES);
            }
            if(valor.compareTo(cero) < 0)
            {
                valor = new BigDecimal(ConstantesMeLanbide46.CERO_CON_DECIMALES);
            }
            meLanbide46DAO.guardarValorCampoNumerico(codOrganizacion, String.valueOf(puesto.getEjercicio()), puesto.getNumExp(), codCampo, valor, con);
        }
        
        //Importe segundo pago
        claveCampo = ConstantesMeLanbide46.CAMPO_SUPL_IMP_PAG_2;
        codCampo = ConfigurationParameter.getParameter(claveCampo, ConstantesMeLanbide46.FICHERO_PROPIEDADES);
        if(this.getValorCampoNumerico(codOrganizacion, String.valueOf(puesto.getEjercicio()), puesto.getNumExp(), codCampo, adaptador) == null)
        {
            valor = calculos.get(claveCampo);
            if(valor == null)
            {
                valor = new BigDecimal(ConstantesMeLanbide46.CERO_CON_DECIMALES);
            }
            if(valor.compareTo(cero) < 0)
            {
                valor = new BigDecimal(ConstantesMeLanbide46.CERO_CON_DECIMALES);
            }
            meLanbide46DAO.guardarValorCampoNumerico(codOrganizacion, String.valueOf(puesto.getEjercicio()), puesto.getNumExp(), codCampo, valor, con);
        }

        //Importe a reintegrar
        claveCampo = ConstantesMeLanbide46.CAMPO_SUPL_IMP_REI;
        codCampo = ConfigurationParameter.getParameter(claveCampo, ConstantesMeLanbide46.FICHERO_PROPIEDADES);
        valor = calculos.get(claveCampo);
        if(valor == null)
        {
            valor = new BigDecimal(ConstantesMeLanbide46.CERO_CON_DECIMALES);
        }
        if(valor.compareTo(cero) < 0)
        {
            valor = new BigDecimal(ConstantesMeLanbide46.CERO_CON_DECIMALES);
        }
        meLanbide46DAO.guardarValorCampoNumerico(codOrganizacion, String.valueOf(puesto.getEjercicio()), puesto.getNumExp(), codCampo, valor, con);

        //Otras ayudas solic
        claveCampo = ConstantesMeLanbide46.CAMPO_SUPL_OTRAS_AYUDAS_SOLIC;
        codCampo = ConfigurationParameter.getParameter(claveCampo, ConstantesMeLanbide46.FICHERO_PROPIEDADES);
        if(this.getValorCampoNumerico(codOrganizacion, String.valueOf(puesto.getEjercicio()), puesto.getNumExp(), codCampo, adaptador) == null)
        {
            valor = calculos.get(claveCampo);
            if(valor == null)
            {
                valor = new BigDecimal(ConstantesMeLanbide46.CERO_CON_DECIMALES);
            }
            if(valor.compareTo(cero) < 0)
            {
                valor = new BigDecimal(ConstantesMeLanbide46.CERO_CON_DECIMALES);
            }
            meLanbide46DAO.guardarValorCampoNumerico(codOrganizacion, String.valueOf(puesto.getEjercicio()), puesto.getNumExp(), codCampo, valor, con);
        }
        
        //Otras ayudas concedido
        claveCampo = ConstantesMeLanbide46.CAMPO_SUPL_OTRAS_AYUDAS_CONCE;
        codCampo = ConfigurationParameter.getParameter(claveCampo, ConstantesMeLanbide46.FICHERO_PROPIEDADES);
        if(this.getValorCampoNumerico(codOrganizacion, String.valueOf(puesto.getEjercicio()), puesto.getNumExp(), codCampo, adaptador) == null)
        {
            valor = calculos.get(claveCampo);
            if(valor == null)
            {
                valor = new BigDecimal(ConstantesMeLanbide46.CERO_CON_DECIMALES);
            }
            if(valor.compareTo(cero) < 0)
            {
                valor = new BigDecimal(ConstantesMeLanbide46.CERO_CON_DECIMALES);
            }
            meLanbide46DAO.guardarValorCampoNumerico(codOrganizacion, String.valueOf(puesto.getEjercicio()), puesto.getNumExp(), codCampo, valor, con);
        }
        
        //Minimis solic
        claveCampo = ConstantesMeLanbide46.CAMPO_SUPL_MINIMIS_SOLIC;
        codCampo = ConfigurationParameter.getParameter(claveCampo, ConstantesMeLanbide46.FICHERO_PROPIEDADES);
        if(this.getValorCampoNumerico(codOrganizacion, String.valueOf(puesto.getEjercicio()), puesto.getNumExp(), codCampo, adaptador) == null)
        {
            valor = calculos.get(claveCampo);
            if(valor == null)
            {
                valor = new BigDecimal(ConstantesMeLanbide46.CERO_CON_DECIMALES);
            }
            if(valor.compareTo(cero) < 0)
            {
                valor = new BigDecimal(ConstantesMeLanbide46.CERO_CON_DECIMALES);
            }
            meLanbide46DAO.guardarValorCampoNumerico(codOrganizacion, String.valueOf(puesto.getEjercicio()), puesto.getNumExp(), codCampo, valor, con);
        }
        
        //Minimis concedido
        claveCampo = ConstantesMeLanbide46.CAMPO_SUPL_MINIMIS_CONCE;
        codCampo = ConfigurationParameter.getParameter(claveCampo, ConstantesMeLanbide46.FICHERO_PROPIEDADES);
        if(this.getValorCampoNumerico(codOrganizacion, String.valueOf(puesto.getEjercicio()), puesto.getNumExp(), codCampo, adaptador) == null)
        {
            valor = calculos.get(claveCampo);
            if(valor == null)
            {
                valor = new BigDecimal(ConstantesMeLanbide46.CERO_CON_DECIMALES);
            }
            if(valor.compareTo(cero) < 0)
            {
                valor = new BigDecimal(ConstantesMeLanbide46.CERO_CON_DECIMALES);
            }
            meLanbide46DAO.guardarValorCampoNumerico(codOrganizacion, String.valueOf(puesto.getEjercicio()), puesto.getNumExp(), codCampo, valor, con);
        }
    }
    
//    public void guardarCalculosPuestos(int codOrganizacion, CmePuestoVO puesto, boolean eliminar, boolean nuevo, AdaptadorSQLBD adaptador, Connection con) throws Exception
//    {
//        MeLanbide46DAO meLanbide46DAO = MeLanbide46DAO.getInstance();
//        String claveCampo = null;
//        String codCampo = null;
//        Integer valor = null;
//        Map<String, Integer> calculos = meLanbide46DAO.cargarCalculosPuestos(codOrganizacion, puesto.getEjercicio(), puesto.getNumExp(), con);
//        //Puestos solicitados
//        claveCampo = ConstantesMeLanbide46.CAMPO_SUPL_PUESTOS_SOLICITADOS;
//        codCampo = ConfigurationParameter.getParameter(claveCampo, ConstantesMeLanbide46.FICHERO_PROPIEDADES);
//        valor = calculos.get(claveCampo);
//        if(valor == null)
//        {
//            valor = 0;
//        }
//        if(eliminar)
//        {
//            valor -= 1;
//        }
//        else if(nuevo)
//        {
//            valor += 1;
//        }
//        if(valor < 0)
//        {
//            valor = 0;
//        }
//        meLanbide46DAO.guardarValorCampoNumerico(codOrganizacion, String.valueOf(puesto.getEjercicio()), puesto.getNumExp(), codCampo, new BigDecimal(String.valueOf(valor)), con);
//
//        //Puestos denegados
//        
//            claveCampo = ConstantesMeLanbide46.CAMPO_SUPL_PUESTOS_DENEGADOS;
//            codCampo = ConfigurationParameter.getParameter(claveCampo, ConstantesMeLanbide46.FICHERO_PROPIEDADES);
//            valor = calculos.get(claveCampo);
//            if(valor == null)
//            {
//                valor = 0;
//            }
//            if(puesto.getCodResult() != null && puesto.getCodResult().equalsIgnoreCase(ConstantesMeLanbide46.CODIGO_RESULTADO_DENEGADO))
//            {
//                if(eliminar)
//                {
//                    valor -= 1;
//                }
//                else
//                {
//                    valor += 1;
//                }
//                if(valor < 0)
//                {
//                    valor = 0;
//                }
//            }
//            meLanbide46DAO.guardarValorCampoNumerico(codOrganizacion, String.valueOf(puesto.getEjercicio()), puesto.getNumExp(), codCampo, new BigDecimal(String.valueOf(valor)), con);
//
//        //TODO: puestos contratados
//        //TODO: puestos despidos/baja voluntaria
//        //TODO: total puestos
//    }
    
//    public void guardarCalculosPuestos(int codOrganizacion, Integer ejercicio, String numExpediente, AdaptadorSQLBD adaptador, Connection con) throws Exception
//    {
//        MeLanbide46DAO meLanbide46DAO = MeLanbide46DAO.getInstance();
//        String claveCampo = null;
//        String codCampo = null;
//        Integer valor = null;
//        Map<String, Integer> calculos = meLanbide46DAO.cargarCalculosPuestos(codOrganizacion, ejercicio, numExpediente, con);
//        //Puestos solicitados
//        claveCampo = ConstantesMeLanbide46.CAMPO_SUPL_PUESTOS_SOLICITADOS;
//        codCampo = ConfigurationParameter.getParameter(claveCampo, ConstantesMeLanbide46.FICHERO_PROPIEDADES);
//        valor = calculos.get(claveCampo);
//        if(valor == null)
//        {
//            valor = 0;
//        }
//        if(valor < 0)
//        {
//            valor = 0;
//        }
//        meLanbide46DAO.guardarValorCampoNumerico(codOrganizacion, String.valueOf(ejercicio), numExpediente, codCampo, new BigDecimal(String.valueOf(valor)), con);
//
//        //Puestos denegados
//        
//        claveCampo = ConstantesMeLanbide46.CAMPO_SUPL_PUESTOS_DENEGADOS;
//        codCampo = ConfigurationParameter.getParameter(claveCampo, ConstantesMeLanbide46.FICHERO_PROPIEDADES);
//        valor = calculos.get(claveCampo);
////        if(valor == null)
////        {
////            valor = 0;
////        }
////        if(puesto.getCodResult() != null && puesto.getCodResult().equalsIgnoreCase(ConstantesMeLanbide46.CODIGO_RESULTADO_DENEGADO))
////        {
////            if(valor < 0)
////            {
////                valor = 0;
////            }
////        }
//        if(valor != null)
//        {
//            meLanbide46DAO.guardarValorCampoNumerico(codOrganizacion, String.valueOf(ejercicio), numExpediente, codCampo, new BigDecimal(String.valueOf(valor)), con);
//        }
//
//        //puestos contratados
//        claveCampo = ConstantesMeLanbide46.CAMPO_SUPL_PUESTOS_CONTRATADOS;
//        codCampo = ConfigurationParameter.getParameter(claveCampo, ConstantesMeLanbide46.FICHERO_PROPIEDADES);
//        valor = calculos.get(claveCampo);
//        if(valor != null)
//        {
//            meLanbide46DAO.guardarValorCampoNumerico(codOrganizacion, String.valueOf(ejercicio), numExpediente, codCampo, new BigDecimal(String.valueOf(valor)), con);
//        }
//        
//        //Personas contratadas
//        claveCampo = ConstantesMeLanbide46.CAMPO_SUPL_PERSONAS_CONTRATADAS;
//        codCampo = ConfigurationParameter.getParameter(claveCampo, ConstantesMeLanbide46.FICHERO_PROPIEDADES);
//        valor = calculos.get(claveCampo);
//        if(valor != null)
//        {
//            meLanbide46DAO.guardarValorCampoNumerico(codOrganizacion, String.valueOf(ejercicio), numExpediente, codCampo, new BigDecimal(String.valueOf(valor)), con);
//        }
//
//        //puestos despido
//        claveCampo = ConstantesMeLanbide46.CAMPO_SUPL_PUESTOS_DESPIDO;
//        codCampo = ConfigurationParameter.getParameter(claveCampo, ConstantesMeLanbide46.FICHERO_PROPIEDADES);
//        valor = calculos.get(claveCampo);
//        if(valor != null)
//        {
//            meLanbide46DAO.guardarValorCampoNumerico(codOrganizacion, String.valueOf(ejercicio), numExpediente, codCampo, new BigDecimal(String.valueOf(valor)), con);
//        }
//
//        //personas despido
//        claveCampo = ConstantesMeLanbide46.CAMPO_SUPL_PERSONAS_DESPIDO;
//        codCampo = ConfigurationParameter.getParameter(claveCampo, ConstantesMeLanbide46.FICHERO_PROPIEDADES);
//        valor = calculos.get(claveCampo);
//        if(valor != null)
//        {
//            meLanbide46DAO.guardarValorCampoNumerico(codOrganizacion, String.valueOf(ejercicio), numExpediente, codCampo, new BigDecimal(String.valueOf(valor)), con);
//        }
//
//        //puestos baja
//        claveCampo = ConstantesMeLanbide46.CAMPO_SUPL_PUESTOS_BAJA;
//        codCampo = ConfigurationParameter.getParameter(claveCampo, ConstantesMeLanbide46.FICHERO_PROPIEDADES);
//        valor = calculos.get(claveCampo);
//        if(valor != null)
//        {
//            meLanbide46DAO.guardarValorCampoNumerico(codOrganizacion, String.valueOf(ejercicio), numExpediente, codCampo, new BigDecimal(String.valueOf(valor)), con);
//        }
//
//        //personas baja
//        claveCampo = ConstantesMeLanbide46.CAMPO_SUPL_PERSONAS_BAJA;
//        codCampo = ConfigurationParameter.getParameter(claveCampo, ConstantesMeLanbide46.FICHERO_PROPIEDADES);
//        valor = calculos.get(claveCampo);
//        if(valor != null)
//        {
//            meLanbide46DAO.guardarValorCampoNumerico(codOrganizacion, String.valueOf(ejercicio), numExpediente, codCampo, new BigDecimal(String.valueOf(valor)), con);
//        }
//        
//        //Puestos renunciados
//        claveCampo = ConstantesMeLanbide46.CAMPO_SUPL_PUESTOS_RENUNCIADOS;
//        codCampo = ConfigurationParameter.getParameter(claveCampo, ConstantesMeLanbide46.FICHERO_PROPIEDADES);
//        valor = calculos.get(claveCampo);
//        if(valor != null)
//        {
//            meLanbide46DAO.guardarValorCampoNumerico(codOrganizacion, String.valueOf(ejercicio), numExpediente, codCampo, new BigDecimal(String.valueOf(valor)), con);
//        }
//    }
    
    public void guardarCalculosPuestos(int codOrganizacion, Integer ejercicio, String numExpediente, AdaptadorSQLBD adaptador, Connection con) throws Exception
    {
        MeLanbide46DAO meLanbide46DAO = MeLanbide46DAO.getInstance();
        String claveCampo = null;
        String codCampo = null;
        Integer valor = null;
        Map<String, Integer> calculos = meLanbide46DAO.cargarCalculosPuestos(codOrganizacion, ejercicio, numExpediente, con);
        
        //Puestos solicitados
        claveCampo = ConstantesMeLanbide46.CAMPO_SUPL_PUESTOS_SOLICITADOS;
        codCampo = ConfigurationParameter.getParameter(claveCampo, ConstantesMeLanbide46.FICHERO_PROPIEDADES);
        valor = calculos.get(claveCampo);
        if(valor == null)
        {
            valor = 0;
        }
        else if(valor < 0)
        {
            valor = 0;
        }
        meLanbide46DAO.guardarValorCampoNumerico(codOrganizacion, String.valueOf(ejercicio), numExpediente, codCampo, new BigDecimal(String.valueOf(valor)), con);

        //Puestos denegados
        claveCampo = ConstantesMeLanbide46.CAMPO_SUPL_PUESTOS_DENEGADOS;
        codCampo = ConfigurationParameter.getParameter(claveCampo, ConstantesMeLanbide46.FICHERO_PROPIEDADES);
        valor = calculos.get(claveCampo);
        
        if(valor == null)
        {
            valor = 0;
        }
        else if(valor < 0)
        {
            valor = 0;
        }
        meLanbide46DAO.guardarValorCampoNumerico(codOrganizacion, String.valueOf(ejercicio), numExpediente, codCampo, new BigDecimal(String.valueOf(valor)), con);

        //puestos contratados
        claveCampo = ConstantesMeLanbide46.CAMPO_SUPL_PUESTOS_CONTRATADOS;
        codCampo = ConfigurationParameter.getParameter(claveCampo, ConstantesMeLanbide46.FICHERO_PROPIEDADES);
        valor = calculos.get(claveCampo);
        if(valor == null)
        {
            valor = 0;
        }
        else if(valor < 0)
        {
            valor = 0;
        }
        meLanbide46DAO.guardarValorCampoNumerico(codOrganizacion, String.valueOf(ejercicio), numExpediente, codCampo, new BigDecimal(String.valueOf(valor)), con);
        
        //Personas contratadas
        claveCampo = ConstantesMeLanbide46.CAMPO_SUPL_PERSONAS_CONTRATADAS;
        codCampo = ConfigurationParameter.getParameter(claveCampo, ConstantesMeLanbide46.FICHERO_PROPIEDADES);
        valor = calculos.get(claveCampo);
        if(valor == null)
        {
            valor = 0;
        }
        else if(valor < 0)
        {
            valor = 0;
        }
        meLanbide46DAO.guardarValorCampoNumerico(codOrganizacion, String.valueOf(ejercicio), numExpediente, codCampo, new BigDecimal(String.valueOf(valor)), con);

        //puestos despido
        claveCampo = ConstantesMeLanbide46.CAMPO_SUPL_PUESTOS_DESPIDO;
        codCampo = ConfigurationParameter.getParameter(claveCampo, ConstantesMeLanbide46.FICHERO_PROPIEDADES);
        valor = calculos.get(claveCampo);
        if(valor == null)
        {
            valor = 0;
        }
        else if(valor < 0)
        {
            valor = 0;
        }
        meLanbide46DAO.guardarValorCampoNumerico(codOrganizacion, String.valueOf(ejercicio), numExpediente, codCampo, new BigDecimal(String.valueOf(valor)), con);

        //personas despido
        claveCampo = ConstantesMeLanbide46.CAMPO_SUPL_PERSONAS_DESPIDO;
        codCampo = ConfigurationParameter.getParameter(claveCampo, ConstantesMeLanbide46.FICHERO_PROPIEDADES);
        valor = calculos.get(claveCampo);
        if(valor == null)
        {
            valor = 0;
        }
        else if(valor < 0)
        {
            valor = 0;
        }
        meLanbide46DAO.guardarValorCampoNumerico(codOrganizacion, String.valueOf(ejercicio), numExpediente, codCampo, new BigDecimal(String.valueOf(valor)), con);

        //puestos baja
        claveCampo = ConstantesMeLanbide46.CAMPO_SUPL_PUESTOS_BAJA;
        codCampo = ConfigurationParameter.getParameter(claveCampo, ConstantesMeLanbide46.FICHERO_PROPIEDADES);
        valor = calculos.get(claveCampo);
        if(valor == null)
        {
            valor = 0;
        }
        else if(valor < 0)
        {
            valor = 0;
        }
        meLanbide46DAO.guardarValorCampoNumerico(codOrganizacion, String.valueOf(ejercicio), numExpediente, codCampo, new BigDecimal(String.valueOf(valor)), con);

        //personas baja
        claveCampo = ConstantesMeLanbide46.CAMPO_SUPL_PERSONAS_BAJA;
        codCampo = ConfigurationParameter.getParameter(claveCampo, ConstantesMeLanbide46.FICHERO_PROPIEDADES);
        valor = calculos.get(claveCampo);
        if(valor == null)
        {
            valor = 0;
        }
        else if(valor < 0)
        {
            valor = 0;
        }
        meLanbide46DAO.guardarValorCampoNumerico(codOrganizacion, String.valueOf(ejercicio), numExpediente, codCampo, new BigDecimal(String.valueOf(valor)), con);
        
        //Puestos renunciados
        claveCampo = ConstantesMeLanbide46.CAMPO_SUPL_PUESTOS_RENUNCIADOS;
        codCampo = ConfigurationParameter.getParameter(claveCampo, ConstantesMeLanbide46.FICHERO_PROPIEDADES);
        valor = calculos.get(claveCampo);
        if(valor == null)
        {
            valor = 0;
        }
        else if(valor < 0)
        {
            valor = 0;
        }
        meLanbide46DAO.guardarValorCampoNumerico(codOrganizacion, String.valueOf(ejercicio), numExpediente, codCampo, new BigDecimal(String.valueOf(valor)), con);
    }
    
    public List<FilaOfertaVO> getListaOfertasNoDenegadasPorExpediente(String numExpediente, AdaptadorSQLBD adaptador) throws Exception
    {
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            MeLanbide46DAO meLanbide46DAO = MeLanbide46DAO.getInstance();
            return meLanbide46DAO.getListaOfertasNoDenegadasPorExpediente(numExpediente, con);
        }
        catch(BDException e)
        {
            log.error("Se ha producido un error recuperando ofertas para expediente "+numExpediente, e);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error recuperando ofertas para expediente "+numExpediente, ex);
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
    
    public List<FilaResumenVO> getListaResumenPorExpediente(String numExpediente, AdaptadorSQLBD adaptador) throws Exception
    {
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            MeLanbide46DAO meLanbide39DAO = MeLanbide46DAO.getInstance();
            return meLanbide39DAO.getListaResumenPorExpediente(numExpediente, con);
        }
        catch(BDException e)
        {
            log.error("Se ha producido un error recuperando el resumen para expediente "+numExpediente, e);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error recuperando el resumen para expediente "+numExpediente, ex);
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
    
    
    public List<FilaOfertaVO> getListaOfertasHistNoDenegadasPorExpediente(int codOrganizacion, String numExpediente, String codTramite, AdaptadorSQLBD adaptador) throws Exception
    {
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            MeLanbide46DAO meLanbide46DAO = MeLanbide46DAO.getInstance();
            return meLanbide46DAO.getListaOfertasHistNoDenegadasPorExpediente(codOrganizacion, numExpediente, codTramite, con);
        }
        catch(BDException e)
        {
            log.error("Se ha producido un error recuperando ofertas históricas para expediente "+numExpediente, e);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error recuperando ofertas históricas para expediente "+numExpediente, ex);
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
    
    public CmeOfertaVO getOfertaPorCodigoPuestoYExpediente(CmeOfertaVO p, AdaptadorSQLBD adaptador) throws Exception
    {
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            MeLanbide46DAO meLanbide46DAO = MeLanbide46DAO.getInstance();
            return meLanbide46DAO.getOfertaPorCodigoPuestoYExpediente(p, con);
        }
        catch(BDException e)
        {
            log.error("Se ha producido un error recuperando oferta "+(p != null ? p.getIdOferta() : "(oferta = null)")+" para puesto "+(p != null ? p.getCodPuesto() : "(oferta = null)")+" expediente "+(p != null ? p.getNumExp() : "(oferta = null)"), e);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error recuperando oferta "+(p != null ? p.getIdOferta() : "(oferta = null)")+" para puesto "+(p != null ? p.getCodPuesto() : "(oferta = null)")+" y expediente "+(p != null ? p.getNumExp() : "(oferta = null)"), ex);
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
   
    public CmeOfertaVO guardarCmeOfertaVO(int codOrganizacion, CmeOfertaVO oferta, String alta, String copiar, AdaptadorSQLBD adaptador) throws Exception
    {
        Connection con = null;
        try
        {
            boolean nuevo = oferta != null && oferta.getIdOferta() != null ? false : true;
            con = adaptador.getConnection();
            adaptador.inicioTransaccion(con);
            MeLanbide46DAO meLanbide46DAO = MeLanbide46DAO.getInstance();
            oferta = meLanbide46DAO.guardarCmeOfertaVO(oferta, con);
            if(oferta != null)
            {
                if(nuevo)
                {
                    //Cuando se crea una oferta, automaticamente se crea tb la justificacion    
                    CmeJustificacionVO justif = new CmeJustificacionVO();
                    justif.setCodPuesto(oferta.getCodPuesto());
                    justif.setEjercicio(oferta.getExpEje());
                    justif.setIdOferta(oferta.getIdOferta());
                    justif.setNumExpediente(oferta.getNumExp());
                    meLanbide46DAO.guardarCmeJustificacionVO(justif, con);
                }
                
                if(alta != null && alta.equals("1"))
                {
                    CmeOfertaVO ofertaNueva = new CmeOfertaVO();
                    ofertaNueva.setExpEje(oferta.getExpEje());
                    ofertaNueva.setNumExp(oferta.getNumExp());  
                    ofertaNueva.setCodPuesto(oferta.getCodPuesto());
                                       
                    if(copiar != null && copiar.equalsIgnoreCase("1"))
                    {
                        ofertaNueva.setIdOfertaOrigen(oferta.getIdOferta());
                        
                        //datos oferta sustituir igual q OFERTA ORIGEN
                        ofertaNueva.setDescPuesto(oferta.getDescPuesto());
                        ofertaNueva.setPaiCod1(oferta.getPaiCod1());
                        ofertaNueva.setPaiCod2(oferta.getPaiCod2());
                        ofertaNueva.setPaiCod3(oferta.getPaiCod3());
                        ofertaNueva.setCodTit1(oferta.getCodTit1());
                        ofertaNueva.setCodTit2(oferta.getCodTit2());
                        ofertaNueva.setCodTit3(oferta.getCodTit3());
                        ofertaNueva.setDpto(oferta.getDpto());
                        ofertaNueva.setCiudadDestino(oferta.getCiudadDestino());
                        ofertaNueva.setFunciones(oferta.getFunciones());
                        ofertaNueva.setCodIdioma1(oferta.getCodIdioma1());
                        ofertaNueva.setCodIdioma2(oferta.getCodIdioma2());
                        ofertaNueva.setCodIdioma3(oferta.getCodIdioma3());
                        ofertaNueva.setCodNivIdi1(oferta.getCodNivIdi1());
                        ofertaNueva.setCodNivIdi2(oferta.getCodNivIdi2());
                        ofertaNueva.setCodNivIdi3(oferta.getCodNivIdi3());
                        ofertaNueva.setCodNivForm(oferta.getCodNivForm());
                        
                        ofertaNueva.setCodOferta(oferta.getCodOferta());
                        ofertaNueva.setCodOfiGest(oferta.getCodOfiGest());
                        ofertaNueva.setPrec(oferta.getPrec());
                        ofertaNueva.setPrecNom(oferta.getPrecNom());
                        ofertaNueva.setDifusion(oferta.getDifusion());
                        ofertaNueva.setFecEnvCand(oferta.getFecEnvCand());
                        ofertaNueva.setNumTotCand(oferta.getNumTotCand());
                        ofertaNueva.setContratacion(oferta.getContratacion());
                        ofertaNueva.setCodCausaRenuncia(oferta.getCodCausaRenuncia());
                    }
                    ofertaNueva = meLanbide46DAO.guardarCmeOfertaVO(ofertaNueva, con);
                    
                    //Cuando se crea una oferta, automaticamente se crea tb la justificacion    
                    CmeJustificacionVO justif = new CmeJustificacionVO();
                    justif.setCodPuesto(ofertaNueva.getCodPuesto());
                    justif.setEjercicio(ofertaNueva.getExpEje());
                    justif.setIdOferta(ofertaNueva.getIdOferta());
                    justif.setNumExpediente(ofertaNueva.getNumExp());
                    meLanbide46DAO.guardarCmeJustificacionVO(justif, con);
                }
                
                CmePuestoVO puesto = new CmePuestoVO();
                puesto.setCodPuesto(oferta.getCodPuesto());
                puesto.setEjercicio(oferta.getExpEje());
                puesto.setNumExp(oferta.getNumExp());
                puesto = meLanbide46DAO.getPuestoPorCodigoYExpediente(puesto, con);
                if(puesto != null)
                {
                    guardarCalculosCme(codOrganizacion, puesto, null, true, false, adaptador, con);
                    guardarCalculosPuestos(codOrganizacion, oferta.getExpEje(), oferta.getNumExp(), adaptador, con);
                }
                adaptador.finTransaccion(con);
                return oferta;
            }
            else
            {
                throw new BDException();
            }
        }
        catch(BDException e)
        {
            log.error("Se ha producido una excepcion en la BBDD guardando oferta "+(oferta != null ? oferta.getIdOferta() : "(oferta = null)")+" para el expediente "+(oferta != null ? oferta.getNumExp() : "(oferta = null)"), e);
            adaptador.rollBack(con);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido una excepcion en la BBDD guardando puesto "+(oferta != null ? oferta.getIdOferta() : "(oferta = null)")+" para el expediente "+(oferta != null ? oferta.getNumExp() : "(oferta = null)"), ex);
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
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
                adaptador.rollBack(con);
            }
        }
    }
    
    public List<SelectItem> getTiposDocumento(AdaptadorSQLBD adaptador) throws Exception
    {
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            MeLanbide46DAO meLanbide46DAO = MeLanbide46DAO.getInstance();
            return meLanbide46DAO.getTiposDocumento(con);
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
            catch(Exception e)
            {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }
    
    public int eliminarCmeOfertaVO(int codOrganizacion, CmeOfertaVO oferta, AdaptadorSQLBD adaptador) throws Exception
    {
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            adaptador.inicioTransaccion(con);
            MeLanbide46DAO meLanbide46DAO = MeLanbide46DAO.getInstance();
            if(oferta != null)
            {
                int eliminados = meLanbide46DAO.eliminarCmeOferta(oferta, con);
                if(eliminados > 0)
                {
                    guardarCalculosPuestos(codOrganizacion, oferta.getExpEje(), oferta.getNumExp(), adaptador, con);
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
            log.error("Se ha producido una excepcion en la BBDD eliminando puesto "+(oferta != null ? oferta.getIdOferta() : "(oferta = null)")+" para el expediente "+(oferta != null ? oferta.getNumExp() : "(oferta = null)"), e);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            adaptador.rollBack(con);
            log.error("Se ha producido una excepcion en la BBDD eliminando puesto "+(oferta != null ? oferta.getIdOferta() : "(oferta = null)")+" para el expediente "+(oferta != null ? oferta.getNumExp() : "(oferta = null)"), ex);
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
            MeLanbide46DAO meLanbide46DAO = MeLanbide46DAO.getInstance();
            return meLanbide46DAO.getDatosTercero(codOrganizacion, numExp, ejercicio, rol, con);
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
            catch(Exception e)
            {
                log.error("Error al cerrar conexiÃƒÂ³n a la BBDD: " + e.getMessage());
            }
        }
    }
    
    public List<FilaJustificacionVO> getListaJustificacionesNoDenegadasPorExpediente(String numExpediente, AdaptadorSQLBD adaptador) throws Exception
    {
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            MeLanbide46DAO meLanbide46DAO = MeLanbide46DAO.getInstance();
            return meLanbide46DAO.getListaJustificacionesNoDenegadasPorExpediente(numExpediente, con);
        }
        catch(BDException e)
        {
            log.error("Se ha producido un error recuperando justificaciones para expediente "+numExpediente, e);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error recuperando justificaciones para expediente "+numExpediente, ex);
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
    
    public List<FilaJustificacionVO> getListaJustificacionesHistNoDenegadasPorExpediente(int codOrganizacion, String numExpediente, String codTramite, AdaptadorSQLBD adaptador) throws Exception
    {
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            MeLanbide46DAO meLanbide46DAO = MeLanbide46DAO.getInstance();
            return meLanbide46DAO.getListaJustificacionesHistNoDenegadasPorExpediente(codOrganizacion, numExpediente, codTramite, con);
        }
        catch(BDException e)
        {
            log.error("Se ha producido un error recuperando justificaciones históricas para expediente "+numExpediente, e);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error recuperando justificaciones históricas para expediente "+numExpediente, ex);
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
    //public String obtenerDiasTrabajados(String numExpediente, String nif, AdaptadorSQLBD adaptador) throws SQLException 
    public String obtenerDiasTrabajados(String numExpediente, String codPuesto, Integer idOferta,  AdaptadorSQLBD adaptador)throws SQLException 
    {
        Connection con = null;
        String dias = "0";
        try
        {
            con = adaptador.getConnection();
            //dias = MeLanbide46DAO.getInstance().obtenerDiasTrabajados(numExpediente, nif, con);
            dias = MeLanbide46DAO.getInstance().obtenerDiasTrabajados(numExpediente, codPuesto,idOferta, con);
        }
        catch (Exception ex)
        {
            log.error("Error en obtenerDiasTrabajados: " + ex.getMessage());
        }
        finally{
            try
            {
                adaptador.devolverConexion(con);       
            }
            catch(Exception e)
            {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
        return dias;
    }
    
    public CmeJustificacionVO getJustificacionPorCodigoPuestoYExpediente(CmeJustificacionVO p, AdaptadorSQLBD adaptador) throws Exception
    {
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            MeLanbide46DAO meLanbide46DAO = MeLanbide46DAO.getInstance();
            return meLanbide46DAO.getJustificacionPorCodigoPuestoYExpediente(p, con);
        }
        catch(BDException e)
        {
            log.error("Se ha producido un error recuperando oferta "+(p != null ? p.getIdOferta() : "(oferta = null)")+" para puesto "+(p != null ? p.getCodPuesto() : "(oferta = null)")+" expediente "+(p != null ? p.getNumExpediente() : "(oferta = null)"), e);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error recuperando oferta "+(p != null ? p.getIdOferta() : "(oferta = null)")+" para puesto "+(p != null ? p.getCodPuesto() : "(oferta = null)")+" y expediente "+(p != null ? p.getNumExpediente() : "(oferta = null)"), ex);
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
    
    public CmeJustificacionVO getJustificacionPorCodigoPuestoOfertaYExpediente(CmeJustificacionVO p, AdaptadorSQLBD adaptador) throws Exception
    {
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            MeLanbide46DAO meLanbide46DAO = MeLanbide46DAO.getInstance();
            return meLanbide46DAO.getJustificacionPorCodigoPuestoOfertaYExpediente(p, con);
        }
        catch(BDException e)
        {
            log.error("Se ha producido un error recuperando oferta "+(p != null ? p.getIdOferta() : "(oferta = null)")+" para puesto "+(p != null ? p.getCodPuesto() : "(oferta = null)")+" expediente "+(p != null ? p.getNumExpediente() : "(oferta = null)"), e);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error recuperando oferta "+(p != null ? p.getIdOferta() : "(oferta = null)")+" para puesto "+(p != null ? p.getCodPuesto() : "(oferta = null)")+" y expediente "+(p != null ? p.getNumExpediente() : "(oferta = null)"), ex);
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
    
   
    public CmeJustificacionVO guardarCmeJustificacionVO(int codOrganizacion, CmeJustificacionVO justif, AdaptadorSQLBD adaptador) throws Exception
    {
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            adaptador.inicioTransaccion(con);
            MeLanbide46DAO meLanbide46DAO = MeLanbide46DAO.getInstance();
            justif = meLanbide46DAO.guardarCmeJustificacionVO(justif, con);
            
            //Importe justificado --> guardar en campo suplememtario
       
        
               
        //VALOR IMPORTE JUSTIFICADO
        //#238083: al guardar fila justificación que se guarde importe justificación en campo suplementario IMJUS
        String codCampo = ConfigurationParameter.getParameter(ConstantesMeLanbide46.CAMPO_SUPL_IMP_JUS, ConstantesMeLanbide46.FICHERO_PROPIEDADES);
        List<FilaResumenVO> fila = MeLanbide46DAO.getInstance().getListaResumenPorExpediente(justif.getNumExpediente(), con);
        Double coste = 0.0;      
        Double dato = 0.0;
        DecimalFormat df = new DecimalFormat("#.##");
        for(FilaResumenVO res : fila)
        {                
            //importe justificado (tener en cuenta dietas y gastos de gestión)
             if(res.getCosteSubvecionable() != null)
                {
                    coste += new Double(res.getCosteSubvecionable().replace(",", "."));                    
                }
        }
        meLanbide46DAO.guardarValorCampoNumerico(codOrganizacion, String.valueOf(justif.getEjercicio()), justif.getNumExpediente(), codCampo, new BigDecimal(String.valueOf(coste)), con);
        //FIN #238083
        
        //VALOR REINTEGRO
        //#238083: al guardar fila justificación que se guarde importe reintegro calculado en campo suplementario IMREI
        codCampo = ConfigurationParameter.getParameter(ConstantesMeLanbide46.CAMPO_SUPL_IMP_REI, ConstantesMeLanbide46.FICHERO_PROPIEDADES);
        Integer ejercicio = MeLanbide46Utils.getEjercicioDeExpediente(justif.getNumExpediente());
        Double pago=MeLanbide46DAO.getInstance().getValorCampoNumerico(codOrganizacion, justif.getNumExpediente(), ejercicio.toString(),
                    ConfigurationParameter.getParameter(ConstantesMeLanbide46.CAMPO_SUPL_IMP_PAG, ConstantesMeLanbide46.FICHERO_PROPIEDADES),
                    con).doubleValue();
        if(pago > coste)
                dato = pago - coste;
        else 
                dato = 0.0;
        meLanbide46DAO.guardarValorCampoNumerico(codOrganizacion, String.valueOf(justif.getEjercicio()), justif.getNumExpediente(), codCampo, new BigDecimal(String.valueOf(dato)), con);
        //FIN #238084
            
            if(justif != null)
            {
                adaptador.finTransaccion(con);
                return justif;
            }
            else
            {
                throw new BDException();
            }
        }
        catch(BDException e)
        {
            log.error("Se ha producido una excepcion en la BBDD guardando justificacion "+(justif != null ? justif.getIdJustificacion() : "(justif = null)")+" para el expediente "+(justif != null ? justif.getNumExpediente() : "(justif = null)"), e);
            adaptador.rollBack(con);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido una excepcion en la BBDD guardando justificacion "+(justif != null ? justif.getIdJustificacion() : "(justif = null)")+" para el expediente "+(justif != null ? justif.getNumExpediente() : "(justif = null)"), ex);
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
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
                adaptador.rollBack(con);
            }
        }
    }
    
    public List<FilaInformeResumenPuestosContratadosVO> getDatosInformeResumenPuestosContratados(Integer ejercicio, AdaptadorSQLBD adaptador) throws Exception
    {
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            MeLanbide46DAO meLanbide46DAO = MeLanbide46DAO.getInstance();
            return meLanbide46DAO.getDatosInformeResumenPuestosContratados(ejercicio, con);
        }
        catch(BDException e)
        {
            log.error("Se ha producido una excepcion en la BBDD recuperando datos informe resumen puestos contratados", e);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido una excepcion en la BBDD recuperando datos informe resumen puestos contratados", ex);
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
                adaptador.rollBack(con);
            }
        }
    }
    
    public List<FilaInformeResumenEconomicoVO> getDatosInformeResumenEconomico(Integer ejercicio, AdaptadorSQLBD adaptador) throws Exception
    {
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            MeLanbide46DAO meLanbide46DAO = MeLanbide46DAO.getInstance();
            return meLanbide46DAO.getDatosInformeResumenEconomico(ejercicio, con);
        }
        catch(BDException e)
        {
            log.error("Se ha producido una excepcion en la BBDD recuperando datos informe resumen economico", e);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido una excepcion en la BBDD recuperando datos informe resumen economico", ex);
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
                adaptador.rollBack(con);
            }
        }
    }
    
    public List<FilaInformeResumenEconomicoVO> getDatosInformeResumenEconomico2016(Integer ejercicio, AdaptadorSQLBD adaptador) throws Exception
    {
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            MeLanbide46DAO meLanbide46DAO = MeLanbide46DAO.getInstance();
            return meLanbide46DAO.getDatosInformeResumenEconomico2016(ejercicio, con);
        }
        catch(BDException e)
        {
            log.error("Se ha producido una excepcion en la BBDD recuperando datos informe resumen economico", e);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido una excepcion en la BBDD recuperando datos informe resumen economico", ex);
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
                adaptador.rollBack(con);
            }
        }
    }
    
    public List<String> getNumerosExpedientes(Integer ejercicio, AdaptadorSQLBD adaptador) throws Exception
    {
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            MeLanbide46DAO meLanbide46DAO = MeLanbide46DAO.getInstance();
            return meLanbide46DAO.getNumerosExpedientes(ejercicio, con);
        }
        catch(BDException e)
        {
            log.error("Se ha producido una excepcion en la BBDD recuperando lista numeros exepediente", e);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido una excepcion en la BBDD recuperando lista numeros exepediente", ex);
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
                adaptador.rollBack(con);
            }
        }
    }
    
    
    public Integer getNumMeses( String fini, String ffin, AdaptadorSQLBD adaptador) throws Exception
    {
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            MeLanbide46DAO meLanbide46DAO = MeLanbide46DAO.getInstance();
            return meLanbide46DAO.obtenerNumMeses(fini, ffin, con);
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
            catch(Exception e)
            {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }
    
    public CmeOfertaVO copiarDatosPuesto(int codOrganizacion, CmePuestoVO puesto, CmeOfertaVO oferta, AdaptadorSQLBD adaptador) throws Exception
    {
        Connection con = null;
        try
        {
            //boolean nuevo = puesto != null && puesto.getCodPuesto() != null ? false : true;
            con = adaptador.getConnection();
            adaptador.inicioTransaccion(con);
            MeLanbide46DAO meLanbide46DAO = MeLanbide46DAO.getInstance();    
            
            //CmeOfertaVO of = new CmeOfertaVO();
            if(puesto != null)
            {     
                oferta.setCodPuesto(puesto.getCodPuesto());
                    oferta.setExpEje(puesto.getEjercicio());
                    oferta.setNumExp(puesto.getNumExp());
                    //oferta.setCodOferta(codOferta);                    
                    oferta.setDescPuesto(puesto.getDescPuesto());
                    oferta.setPaiCod1(puesto.getPaiCod1());
                    oferta.setPaiCod2(puesto.getPaiCod2());
                    oferta.setPaiCod3(puesto.getPaiCod3());
                    oferta.setCodTit1(puesto.getCodTit1());
                    oferta.setCodTit2(puesto.getCodTit2());
                    oferta.setCodTit3(puesto.getCodTit3());
                    oferta.setFunciones(puesto.getFunciones());
                    oferta.setCodIdioma1(puesto.getCodIdioma1());
                    oferta.setCodIdioma2(puesto.getCodIdioma2());
                    oferta.setCodIdioma3(puesto.getCodIdioma3());
                    oferta.setCodNivIdi1(puesto.getCodNivIdi1());
                    oferta.setCodNivIdi2(puesto.getCodNivIdi2());
                    oferta.setCodNivIdi3(puesto.getCodNivIdi3());
                    oferta.setCodNivForm(puesto.getCodNivForm());
                    
                    oferta.setCiudadDestino(puesto.getCiudadDestino());
                    oferta.setDpto(puesto.getDpto());
                    
                //}
                
                adaptador.finTransaccion(con);
                return oferta;
            }
            else
            {
                throw new BDException();
            }
        }
        catch(BDException e)
        {
            log.error("Se ha producido una excepcion en la BBDD guardando puesto "+(puesto != null ? puesto.getCodPuesto() : "(puesto = null)")+" para el expediente "+(puesto != null ? puesto.getNumExp() : "(puesto = null)"), e);
            adaptador.rollBack(con);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido una excepcion en la BBDD guardando puesto "+(puesto != null ? puesto.getCodPuesto() : "(puesto = null)")+" para el expediente "+(puesto != null ? puesto.getNumExp() : "(puesto = null)"), ex);
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
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
                adaptador.rollBack(con);
            }
        }
    }
    
    public CmeOfertaVO getUltimaOfertaPorPuestoYExpediente(CmeOfertaVO p, AdaptadorSQLBD adaptador) throws Exception
    {
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            MeLanbide46DAO meLanbide46DAO = MeLanbide46DAO.getInstance();
            return meLanbide46DAO.getUltimaOfertaPorPuestoYExpediente(p, con);
        }
        catch(BDException e)
        {
            log.error("Se ha producido un error recuperando ultima oferta para puesto "+(p != null ? p.getCodPuesto() : "(oferta = null)")+" expediente "+(p != null ? p.getNumExp() : "(oferta = null)"), e);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error recuperando ultima oferta para puesto "+(p != null ? p.getCodPuesto() : "(oferta = null)")+" y expediente "+(p != null ? p.getNumExp() : "(oferta = null)"), ex);
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
    
    public List<FilaPersonaContratadaVO> getListaContratacionesPuesto(CmePuestoVO p, AdaptadorSQLBD adaptador) throws Exception
    {
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            MeLanbide46DAO meLanbide46DAO = MeLanbide46DAO.getInstance();
            return meLanbide46DAO.getListaContratacionesPuesto(p, con);
        }
        catch(BDException e)
        {
            log.error("Se ha producido un error recuperando lista contrataciones para puesto "+(p != null ? p.getCodPuesto() : "(oferta = null)")+" expediente "+(p != null ? p.getNumExp() : "(oferta = null)"), e);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error recuperando lista contrataciones para puesto "+(p != null ? p.getCodPuesto() : "(oferta = null)")+" y expediente "+(p != null ? p.getNumExp() : "(oferta = null)"), ex);
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
    
    public List<FilaResultadoBusqTitulaciones> buscarTitulaciones(String codigo, String descripcion, AdaptadorSQLBD adaptador) throws Exception
    {
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            MeLanbide46DAO meLanbide46DAO = MeLanbide46DAO.getInstance();
            return meLanbide46DAO.buscarTitulaciones(codigo, descripcion, con);
        }
        catch(BDException e)
        {
            log.error("Se ha producido un error buscando titulaciones con codigo =  "+codigo+" descripcion = "+descripcion, e);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error buscando titulaciones con codigo =  "+codigo+" descripcion = "+descripcion, ex);
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
    
    /*public String getDescripcionCampoDesplegable(String codigoCampo, String valorDesplegable, AdaptadorSQLBD adaptador) throws Exception
    {
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            MeLanbide46DAO meLanbide46DAO = MeLanbide46DAO.getInstance();
            return meLanbide46DAO.getDescripcionCampoDesplegable(codigoCampo, valorDesplegable, con);
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
            catch(Exception e)
            {
                log.error("Error al cerrar conexion a la BBDD: " + e.getMessage());
            }
        }
    }*/
    
    public String getDescripcionTitulacion(String codigo,  AdaptadorSQLBD adaptador) throws Exception
    {
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            MeLanbide46DAO meLanbide39DAO = MeLanbide46DAO.getInstance();
            return meLanbide39DAO.getDescripcionTitulacion(codigo, con);
        }
        catch(BDException e)
        {
            log.error("Se ha producido un error recuperando descripcion de titulacion con codigo =  "+codigo, e);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error recuperando descripcion de titulacion con codigo =  "+codigo, ex);
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
    
    public Map<String, Integer> cargarCalculosPuestos(int codOrganizacion, Integer ejercicio, String numExpediente, AdaptadorSQLBD adaptador) throws Exception
    {
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            MeLanbide46DAO meLanbide46DAO = MeLanbide46DAO.getInstance();
            return meLanbide46DAO.cargarCalculosPuestos(codOrganizacion, ejercicio, numExpediente, con);
        }
        catch(BDException e)
        {
            log.error("Se ha producido un error calculando puestos para expediente "+numExpediente, e);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido un error calculando puestos para expediente "+numExpediente, ex);
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
    
    public Long getTramiteActualExpediente(int codOrganizacion, int ejercicio, String numExp, AdaptadorSQLBD adaptador)throws Exception
    {
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            MeLanbide46DAO meLanbide46DAO = MeLanbide46DAO.getInstance();
            return meLanbide46DAO.getTramiteActualExpediente(codOrganizacion, ejercicio, numExp, con);
        }
        catch(BDException e)
        {
            log.error("Se ha producido una excepción en la BBDD recuperando tramite actual del expediente " + numExp, e);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido una excepción en la BBDD recuperando tramite actual del expediente " + numExp, ex);
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
    
    public Long obtenerCodigoInternoTramite(Integer codOrganizacion, String codTramite, AdaptadorSQLBD adaptador)throws Exception
    {
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            MeLanbide46DAO meLanbide46DAO = MeLanbide46DAO.getInstance();
            return meLanbide46DAO.obtenerCodigoInternoTramite(codOrganizacion, codTramite, con);
        }
        catch(BDException e)
        {
            log.error("Se ha producido una excepción en la BBDD recuperando codigo interno del tramite " + codTramite, e);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido una excepción en la BBDD recuperando codigo interno del tramite " + codTramite, ex);
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
    
    public boolean tieneTramiteIniciado(int codOrganizacion, int ejercicio, String numExp, Long codTramite, AdaptadorSQLBD adaptador) throws Exception
    {
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            MeLanbide46DAO meLanbide46DAO = MeLanbide46DAO.getInstance();
            return meLanbide46DAO.tieneTramiteIniciado(codOrganizacion, ejercicio, numExp, codTramite, con);
        }
        catch(BDException e)
        {
            log.error("Se ha producido una excepción en la BBDD comprobando si tiene tramite iniciado "+codTramite, e);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido una excepción en la BBDD comprobando si tiene tramite iniciado "+codTramite, ex);
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
    
    public boolean tieneTramiteFinalizado(int codOrganizacion, int ejercicio, String numExp, Long codTramite, AdaptadorSQLBD adaptador) throws Exception
    {
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            MeLanbide46DAO meLanbide46DAO = MeLanbide46DAO.getInstance();
            return meLanbide46DAO.tieneTramiteFinalizado(codOrganizacion, ejercicio, numExp, codTramite, con);
        }
        catch(BDException e)
        {
            log.error("Se ha producido una excepción en la BBDD comprobando si tiene tramite finalizado "+codTramite, e);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido una excepción en la BBDD comprobando si tiene tramite finalizado "+codTramite, ex);
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
    
    public HashMap<String, Object> cargarImportesResolucion(int codOrganizacion, Integer ejercicio, String numExpediente, String res, AdaptadorSQLBD adaptador) throws Exception
    {
        Connection con = null;
        try
        {
            HashMap<String, Object> valores = null;
            if(ejercicio != null && res != null && !res.equals(""))
            {
                con = adaptador.getConnection();
                if(res.equals("I"))
                {
                    valores = cargarImportesResolucionCD(codOrganizacion, ejercicio.toString(), numExpediente, con);
                }
                else if(res.equals("M"))
                {
                    valores = cargarImportesResolucionModif(codOrganizacion, ejercicio.toString(), numExpediente, con);
                }
            }
            return valores;
        }
        catch(BDException e)
        {
            log.error("Se ha producido una excepción en la BBDD recuperando importes resolucion "+res+" para el expediente "+numExpediente, e);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido una excepción en la BBDD recuperando importes resolucion "+res+" para el expediente "+numExpediente, ex);
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
    
    private HashMap<String, Object> cargarImportesResolucionCD(int codOrganizacion, String ejercicio, String numExpediente, Connection con) throws Exception
    {
        HashMap<String, Object> retMap = new HashMap<String, Object>();
        MeLanbide46DAO meLanbide46DAO = MeLanbide46DAO.getInstance();
        BigDecimal bd = null;
        Date d = null;
        String codigo_campo = null;
        String clave = null;
        
        //Fecha resolucion
        clave = ConstantesMeLanbide46.CAMPO_SUPL_FEC_RES;
        codigo_campo = ConfigurationParameter.getParameter(clave, ConstantesMeLanbide46.FICHERO_PROPIEDADES);
        d = meLanbide46DAO.getValorCampoFecha(codOrganizacion, numExpediente, ejercicio, codigo_campo, con);
        if(d != null)
        {
            retMap.put(clave, d);
        }
        
        //Fecha notificacion resolucion inicial
        clave = ConstantesMeLanbide46.CAMPO_SUPL_RES_NOT_RES;
        codigo_campo = ConfigurationParameter.getParameter(clave, ConstantesMeLanbide46.FICHERO_PROPIEDADES);
        d = meLanbide46DAO.getValorCampoFecha(codOrganizacion, numExpediente, ejercicio, codigo_campo, con);
        if(d != null)
        {
            retMap.put(clave, d);
        }
        
        //Puestos concendido resolucion inicial
        clave = ConstantesMeLanbide46.CAMPO_SUPL_RES_PUESTOS_CONC;
        codigo_campo = ConfigurationParameter.getParameter(clave, ConstantesMeLanbide46.FICHERO_PROPIEDADES);
        bd = meLanbide46DAO.getValorCampoNumerico(codOrganizacion, numExpediente, ejercicio, codigo_campo, con);
        if(bd != null)
        {
            retMap.put(clave, bd);
        }
        
        //Importe concedido resolucion inicial
        clave = ConstantesMeLanbide46.CAMPO_SUPL_RES_IMP_CONC;
        codigo_campo = ConfigurationParameter.getParameter(clave, ConstantesMeLanbide46.FICHERO_PROPIEDADES);
        bd = meLanbide46DAO.getValorCampoNumerico(codOrganizacion, numExpediente, ejercicio, codigo_campo, con);
        if(bd != null)
        {
            retMap.put(clave, bd);
        }
        
        //Importe primer pago resolucion inicial
        clave = ConstantesMeLanbide46.CAMPO_SUPL_RES_IMP_PRIMER_PAG;
        codigo_campo = ConfigurationParameter.getParameter(clave, ConstantesMeLanbide46.FICHERO_PROPIEDADES);
        bd = meLanbide46DAO.getValorCampoNumerico(codOrganizacion, numExpediente, ejercicio, codigo_campo, con);
        if(bd != null)
        {
            retMap.put(clave, bd);
        }
        
        //Importe segundo pago resolucion inicial
        clave = ConstantesMeLanbide46.CAMPO_SUPL_RES_IMP_SEG_PAGO;
        codigo_campo = ConfigurationParameter.getParameter(clave, ConstantesMeLanbide46.FICHERO_PROPIEDADES);
        bd = meLanbide46DAO.getValorCampoNumerico(codOrganizacion, numExpediente, ejercicio, codigo_campo, con);
        if(bd != null)
        {
            retMap.put(clave, bd);
        }
        
        //Fecha presentacion renuncua
        clave = ConstantesMeLanbide46.CAMPO_SUPL_RES_FEC_RESOL;
        codigo_campo = ConfigurationParameter.getParameter(clave, ConstantesMeLanbide46.FICHERO_PROPIEDADES);
        d = meLanbide46DAO.getValorCampoFecha(codOrganizacion, numExpediente, ejercicio, codigo_campo, con);
        if(d != null)
        {
            retMap.put(clave, d);
        }
        
        //Numero puestos renunciados tras resolucion
        clave = ConstantesMeLanbide46.CAMPO_SUPL_RES_NUM_PUESTO;
        codigo_campo = ConfigurationParameter.getParameter(clave, ConstantesMeLanbide46.FICHERO_PROPIEDADES);
        bd = meLanbide46DAO.getValorCampoNumerico(codOrganizacion, numExpediente, ejercicio, codigo_campo, con);
        if(bd != null)
        {
            retMap.put(clave, bd);
        }
        
        //Importe asociado al número de puestos renunciado
        clave = ConstantesMeLanbide46.CAMPO_SUPL_RES_IMP_RENUN;
        codigo_campo = ConfigurationParameter.getParameter(clave, ConstantesMeLanbide46.FICHERO_PROPIEDADES);
        bd = meLanbide46DAO.getValorCampoNumerico(codOrganizacion, numExpediente, ejercicio, codigo_campo, con);
        if(bd != null)
        {
            retMap.put(clave, bd);
        }
        
        return retMap;
    }
    
    private HashMap<String, Object> cargarImportesResolucionModif(int codOrganizacion, String ejercicio, String numExpediente, Connection con) throws Exception
    {
        HashMap<String, Object> retMap = new HashMap<String, Object>();
        MeLanbide46DAO meLanbide46DAO = MeLanbide46DAO.getInstance();
        BigDecimal bd = null;
        Date d = null;
        String clave = null;
        String codigo_campo = null;        
        
        //Fecha resolucion modificatoria
        clave = ConstantesMeLanbide46.CAMPO_SUPL_RES_MOD_FEC_RES;
        codigo_campo = ConfigurationParameter.getParameter(clave, ConstantesMeLanbide46.FICHERO_PROPIEDADES);
        d = meLanbide46DAO.getValorCampoFecha(codOrganizacion, numExpediente, ejercicio, codigo_campo, con);
        if(d != null)
        {
            retMap.put(clave, d);
        }
        
        //Fecha notificacion resolucion modificatoria
        clave = ConstantesMeLanbide46.CAMPO_SUPL_RES_MOD_NOT_RES;
        codigo_campo = ConfigurationParameter.getParameter(clave, ConstantesMeLanbide46.FICHERO_PROPIEDADES);
        d = meLanbide46DAO.getValorCampoFecha(codOrganizacion, numExpediente, ejercicio, codigo_campo, con);
        if(d != null)
        {
            retMap.put(clave, d);
        }
        
        //Puestos concendido resolucion modificatoria
        clave = ConstantesMeLanbide46.CAMPO_SUPL_RES_MOD_PUES_CONC;
        codigo_campo = ConfigurationParameter.getParameter(clave, ConstantesMeLanbide46.FICHERO_PROPIEDADES);
        bd = meLanbide46DAO.getValorCampoNumerico(codOrganizacion, numExpediente, ejercicio, codigo_campo, con);
        if(bd != null)
        {
            retMap.put(clave, bd);
        }
        
        //Importe concedido resolucion modificatoria
        clave = ConstantesMeLanbide46.CAMPO_SUPL_RES_MOD_IMP_CONC;
        codigo_campo = ConfigurationParameter.getParameter(clave, ConstantesMeLanbide46.FICHERO_PROPIEDADES);
        bd = meLanbide46DAO.getValorCampoNumerico(codOrganizacion, numExpediente, ejercicio, codigo_campo, con);
        if(bd != null)
        {
            retMap.put(clave, bd);
        }
        
        //Importe primer pago resolucion modificatoria
        clave = ConstantesMeLanbide46.CAMPO_SUPL_RES_MOD_IMP_PRI_PAG;
        codigo_campo = ConfigurationParameter.getParameter(clave, ConstantesMeLanbide46.FICHERO_PROPIEDADES);
        bd = meLanbide46DAO.getValorCampoNumerico(codOrganizacion, numExpediente, ejercicio, codigo_campo, con);
        if(bd != null)
        {
            retMap.put(clave, bd);
        }
        
        //Importe segundo pago resolucion modificatoria
        clave = ConstantesMeLanbide46.CAMPO_SUPL_RES_MOD_SEG_PAG;
        codigo_campo = ConfigurationParameter.getParameter(clave, ConstantesMeLanbide46.FICHERO_PROPIEDADES);
        bd = meLanbide46DAO.getValorCampoNumerico(codOrganizacion, numExpediente, ejercicio, codigo_campo, con);
        if(bd != null)
        {
            retMap.put(clave, bd);
        }
        
        //Fecha presentacion renuncua
        clave = ConstantesMeLanbide46.CAMPO_SUPL_RES_MOD_FEC_RENUN;
        codigo_campo = ConfigurationParameter.getParameter(clave, ConstantesMeLanbide46.FICHERO_PROPIEDADES);
        d = meLanbide46DAO.getValorCampoFecha(codOrganizacion, numExpediente, ejercicio, codigo_campo, con);
        if(d != null)
        {
            retMap.put(clave, d);
        }
        
        //Numero puestos renunciados tras resolucion modificatoria
        clave = ConstantesMeLanbide46.CAMPO_SUPL_RES_MOD_PUES_REN;
        codigo_campo = ConfigurationParameter.getParameter(clave, ConstantesMeLanbide46.FICHERO_PROPIEDADES);
        bd = meLanbide46DAO.getValorCampoNumerico(codOrganizacion, numExpediente, ejercicio, codigo_campo, con);
        if(bd != null)
        {
            retMap.put(clave, bd);
        }
        
        //Importe asociado al número de puestos renunciado
        clave = ConstantesMeLanbide46.CAMPO_SUPL_RES_MOD_IMP_REN;
        codigo_campo = ConfigurationParameter.getParameter(clave, ConstantesMeLanbide46.FICHERO_PROPIEDADES);
        bd = meLanbide46DAO.getValorCampoNumerico(codOrganizacion, numExpediente, ejercicio, codigo_campo, con);
        if(bd != null)
        {
            retMap.put(clave, bd);
        }
        
        return retMap;
    }
    
    public void grabarImportesResolucion(int codOrganizacion, Integer ejercicio, String numExpediente, AdaptadorSQLBD adaptador) throws Exception
    {
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            Map<String, BigDecimal> importes = this.cargarCalculosCme(codOrganizacion, ejercicio, numExpediente, adaptador);
            Map<String, Integer> puestos = this.cargarCalculosPuestos(codOrganizacion, ejercicio, numExpediente, adaptador);
            adaptador.inicioTransaccion(con);
            
            BigDecimal bd = null;
            Integer i = null;
            
            MeLanbide46DAO meLanbide46DAO = MeLanbide46DAO.getInstance();
            
            //Fecha resolución (la de hoy)
            meLanbide46DAO.guardarValorCampoFecha(codOrganizacion, ejercicio.toString(), numExpediente, ConfigurationParameter.getParameter(ConstantesMeLanbide46.CAMPO_SUPL_FEC_RES, ConstantesMeLanbide46.FICHERO_PROPIEDADES), new Date(), con);
            
            //Fecha notificación resolución inicial (¿Cuál?)
            Date d = meLanbide46DAO.getValorCampoFecha(codOrganizacion, numExpediente, ejercicio.toString(), ConfigurationParameter.getParameter(ConstantesMeLanbide46.CAMPO_SUPL_FE_AC_REN_RS, ConstantesMeLanbide46.FICHERO_PROPIEDADES), con);
            if(d != null)
            {
                meLanbide46DAO.guardarValorCampoFecha(codOrganizacion, ejercicio.toString(), numExpediente, ConfigurationParameter.getParameter(ConstantesMeLanbide46.CAMPO_SUPL_RES_NOT_RES, ConstantesMeLanbide46.FICHERO_PROPIEDADES), d, con);
            }
            
            //Puestos concedidos
            i = puestos.get(ConstantesMeLanbide46.CAMPO_SUPL_PUESTOS_CONTRATADOS);
            if(i != null)
            {
                meLanbide46DAO.guardarValorCampoNumerico(codOrganizacion, ejercicio.toString(), numExpediente, ConfigurationParameter.getParameter(ConstantesMeLanbide46.CAMPO_SUPL_RES_PUESTOS_CONC, ConstantesMeLanbide46.FICHERO_PROPIEDADES), new BigDecimal(i.toString()), con);
            }
            
            //Importe concedido
            bd = importes.get(ConstantesMeLanbide46.CAMPO_SUPL_IMP_CON);
            if(bd != null)
            {
                meLanbide46DAO.guardarValorCampoNumerico(codOrganizacion, ejercicio.toString(), numExpediente, ConfigurationParameter.getParameter(ConstantesMeLanbide46.CAMPO_SUPL_RES_IMP_CONC, ConstantesMeLanbide46.FICHERO_PROPIEDADES), bd, con);
            }
            
            //Importe primer pago
            bd = importes.get(ConstantesMeLanbide46.CAMPO_SUPL_IMP_PAG);
            if(bd != null)
            {
                meLanbide46DAO.guardarValorCampoNumerico(codOrganizacion, ejercicio.toString(), numExpediente, ConfigurationParameter.getParameter(ConstantesMeLanbide46.CAMPO_SUPL_RES_IMP_PRIMER_PAG, ConstantesMeLanbide46.FICHERO_PROPIEDADES), bd, con);
            }
            
            //Importe segundo pago
            bd = importes.get(ConstantesMeLanbide46.CAMPO_SUPL_IMP_PAG_2);
            if(bd != null)
            {
                meLanbide46DAO.guardarValorCampoNumerico(codOrganizacion, ejercicio.toString(), numExpediente, ConfigurationParameter.getParameter(ConstantesMeLanbide46.CAMPO_SUPL_RES_IMP_SEG_PAGO, ConstantesMeLanbide46.FICHERO_PROPIEDADES), bd, con);
            }
            
            //Numero de puestos renunciados tras resolucion
            i = puestos.get(ConstantesMeLanbide46.CAMPO_SUPL_RES_NUM_PUESTO);
            if(i != null)
            {
                meLanbide46DAO.guardarValorCampoNumerico(codOrganizacion, ejercicio.toString(), numExpediente, ConfigurationParameter.getParameter(ConstantesMeLanbide46.CAMPO_SUPL_RES_NUM_PUESTO, ConstantesMeLanbide46.FICHERO_PROPIEDADES), new BigDecimal(i.toString()), con);
            }
            
            //importe asociado al numero de puestos renunciados
            bd = importes.get(ConstantesMeLanbide46.CAMPO_SUPL_IMP_REN_RES);
            if(bd != null)
            {
                meLanbide46DAO.guardarValorCampoNumerico(codOrganizacion, ejercicio.toString(), numExpediente, es.altia.flexia.integracion.moduloexterno.melanbide46.util.ConfigurationParameter.getParameter(ConstantesMeLanbide46.CAMPO_SUPL_RES_IMP_RENUN, ConstantesMeLanbide46.FICHERO_PROPIEDADES), bd, con);
            }
            
            adaptador.finTransaccion(con);
        }
        catch(BDException e)
        {
            log.error("Se ha producido una excepción en la BBDD guardando importes resolucion I para el expediente "+numExpediente, e);
            adaptador.rollBack(con);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            adaptador.rollBack(con);
            log.error("Se ha producido una excepción en la BBDD guardando importes resolucion I para el expediente "+numExpediente, ex);
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
    
    public void grabarImportesResolucionModif(int codOrganizacion, Integer ejercicio, String numExpediente, AdaptadorSQLBD adaptador) throws Exception
    {
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            
            Map<String, BigDecimal> importes = this.cargarCalculosCme(codOrganizacion, ejercicio, numExpediente, adaptador);
            Map<String, Integer> puestos = this.cargarCalculosPuestos(codOrganizacion, ejercicio, numExpediente, adaptador);
            adaptador.inicioTransaccion(con);
            
            BigDecimal bd = null;
            Integer i = null;
            
            MeLanbide46DAO meLanbide46DAO = MeLanbide46DAO.getInstance();
            
            //Fecha resolución (la de hoy)
            meLanbide46DAO.guardarValorCampoFecha(codOrganizacion, ejercicio.toString(), numExpediente, ConfigurationParameter.getParameter(ConstantesMeLanbide46.CAMPO_SUPL_RES_MOD_FEC_RES, ConstantesMeLanbide46.FICHERO_PROPIEDADES), new Date(), con);
            
            //Fecha notificación resolución inicial (¿Cuál?)
            meLanbide46DAO.guardarValorCampoFecha(codOrganizacion, ejercicio.toString(), numExpediente, ConfigurationParameter.getParameter(ConstantesMeLanbide46.CAMPO_SUPL_RES_MOD_NOT_RES, ConstantesMeLanbide46.FICHERO_PROPIEDADES), new Date(), con);
            
            //Puestos concedidos
            i = puestos.get(ConstantesMeLanbide46.CAMPO_SUPL_PUESTOS_CONTRATADOS);
            if(i != null)
            {
                meLanbide46DAO.guardarValorCampoNumerico(codOrganizacion, ejercicio.toString(), numExpediente, ConfigurationParameter.getParameter(ConstantesMeLanbide46.CAMPO_SUPL_RES_MOD_PUES_CONC, ConstantesMeLanbide46.FICHERO_PROPIEDADES), new BigDecimal(i.toString()), con);
            }
            
            //Importe concedido
            bd = importes.get(ConstantesMeLanbide46.CAMPO_SUPL_IMP_CON);
            if(bd != null)
            {
                meLanbide46DAO.guardarValorCampoNumerico(codOrganizacion, ejercicio.toString(), numExpediente, ConfigurationParameter.getParameter(ConstantesMeLanbide46.CAMPO_SUPL_RES_MOD_IMP_CONC, ConstantesMeLanbide46.FICHERO_PROPIEDADES), bd, con);
            }
            
            //Importe primer pago
            bd = importes.get(ConstantesMeLanbide46.CAMPO_SUPL_IMP_PAG);
            if(bd != null)
            {
                meLanbide46DAO.guardarValorCampoNumerico(codOrganizacion, ejercicio.toString(), numExpediente, ConfigurationParameter.getParameter(ConstantesMeLanbide46.CAMPO_SUPL_RES_MOD_IMP_PRI_PAG, ConstantesMeLanbide46.FICHERO_PROPIEDADES), bd, con);
            }
            
            //Importe segundo pago
            bd = importes.get(ConstantesMeLanbide46.CAMPO_SUPL_IMP_PAG_2);
            if(bd != null)
            {
                meLanbide46DAO.guardarValorCampoNumerico(codOrganizacion, ejercicio.toString(), numExpediente, ConfigurationParameter.getParameter(ConstantesMeLanbide46.CAMPO_SUPL_RES_MOD_SEG_PAG, ConstantesMeLanbide46.FICHERO_PROPIEDADES), bd, con);
            }
            
            //Numero de puestos renunciados tras resolucion
            i = puestos.get(ConstantesMeLanbide46.CAMPO_SUPL_RES_NUM_PUESTO);
            if(i != null)
            {
                meLanbide46DAO.guardarValorCampoNumerico(codOrganizacion, ejercicio.toString(), numExpediente, ConfigurationParameter.getParameter(ConstantesMeLanbide46.CAMPO_SUPL_RES_MOD_PUES_REN, ConstantesMeLanbide46.FICHERO_PROPIEDADES), new BigDecimal(i.toString()), con);
            }
            
            //importe asociado al numero de puestos renunciados
            bd = importes.get(ConstantesMeLanbide46.CAMPO_SUPL_IMP_REN_RES);
            if(bd != null)
            {
                meLanbide46DAO.guardarValorCampoNumerico(codOrganizacion, ejercicio.toString(), numExpediente, es.altia.flexia.integracion.moduloexterno.melanbide46.util.ConfigurationParameter.getParameter(ConstantesMeLanbide46.CAMPO_SUPL_RES_MOD_IMP_REN, ConstantesMeLanbide46.FICHERO_PROPIEDADES), bd, con);
            }
            
            adaptador.finTransaccion(con);
        }
        catch(BDException e)
        {
            log.error("Se ha producido una excepción en la BBDD guardando importes resolucion M para el expediente "+numExpediente, e);
            adaptador.rollBack(con);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido una excepción en la BBDD guardando importes resolucion M para el expediente "+numExpediente, ex);
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
                log.error("Error al cerrar conexión a la BBDD: " + e.getMessage());
            }
        }
    }
    
    public datosCmeVO obtenerDatosCme(int codOrganizacion, String numExp, AdaptadorSQLBD adaptador) throws Exception
    {
        datosCmeVO datos = new datosCmeVO();
        Connection con = adaptador.getConnection();
        try
        {
            List<FilaResumenVO> fila = MeLanbide46DAO.getInstance().getListaResumenPorExpediente(numExp, con);
            Double coste = 0.0;
            Double conce = 0.0;
            Double coste2 = 0.0;
            Double conce2 = 0.0;
            Double reint = 0.0;
            Double pago = 0.0;
            Double importe = 0.0;
            Double dato = 0.0;
            DecimalFormat df = new DecimalFormat("#.##");
            for(FilaResumenVO res : fila)
            {
                if(!res.getSustituto().equals("S"))
                {
//                    if(res.getCosteSubvecionable() != null)
//                    {
//                        coste2 += new Double(res.getCosteSubvecionable().replace(",", "."));                    
//                    }
                    if(res.getResolInical() != null)
                    {
                        conce2 += new Double(res.getResolInical().replace(",", ".")); 
                    }           
                    
                    //modificado reintegro se calcula con 1ºpago no con segundo
                    /*if(res.getSegundoPago() != null)
                    {
                        pago += new Double(res.getSegundoPago().replace(",", ".")); 
                    }*/
                    
                    if(res.getReintegro()!= null)
                    {
                        reint += new Double(res.getReintegro().replace(",", ".")); 
                    }
                    if(res.getImporteMaxSubv()!= null)
                    {
                        importe += new Double(res.getMaxSubv().replace(",", "."));                    
                    }
                }
                //importe justificado (tener en cuenta dietas y gastos de gestión)
               // if(res.getImporteMaxSubv()!= null)
               // {
               //     coste += new Double(res.getImporteMaxSubv().replace(",", "."));                    
               // }
                //importe justificado (tener en cuenta dietas y gastos de gestión)
                 if(res.getCosteSubvecionable() != null)
                    {
                        coste += new Double(res.getCosteSubvecionable().replace(",", "."));                    
                    }
//                if(res.getResolInical() != null)
//                {
//                    conce += new Double(res.getResolInical().replace(",", ".")); 
//                }
//                if(res.getReintegro()!= null)
//                {
//                    reint += new Double(res.getReintegro().replace(",", ".")); 
//                }
//                if(res.getSegundoPago() != null)
//                {
//                    pago += new Double(res.getSegundoPago().replace(",", ".")); 
//                }
                
            }
            
            datos.setImpJustificado(df.format(coste).toString());
            if(importe < coste)
                dato = 0.0;
            else
                dato = importe - coste;
            //dato = conce2 - coste;
            datos.setImpNoJusi(df.format(dato).toString());
            
            Integer ejercicio = MeLanbide46Utils.getEjercicioDeExpediente(numExp);
            pago=MeLanbide46DAO.getInstance().getValorCampoNumerico(codOrganizacion, numExp, ejercicio.toString(),
                    ConfigurationParameter.getParameter(ConstantesMeLanbide46.CAMPO_SUPL_IMP_PAG, ConstantesMeLanbide46.FICHERO_PROPIEDADES),
                    con).doubleValue();
            
            if(pago > coste)
                dato = pago - coste;
            else 
                dato = 0.0;
            datos.setReintegro(df.format(dato).toString());
        }
        catch(Exception ex){
            throw ex;
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
        return datos;
    }
    
    public List<FilaInformeDatosPuestosVO> getDatosInformePuestos(Integer ejercicio, AdaptadorSQLBD adaptador) throws Exception
    {
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            MeLanbide46DAO meLanbide39DAO = MeLanbide46DAO.getInstance();
            return meLanbide39DAO.getInformeDatosPuestos(ejercicio, con);
        }
        catch(BDException e)
        {
            log.error("Se ha producido una excepcion en la BBDD en getDatosInformePuestos", e);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido una excepcion en getDatosInformePuestos", ex);
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
                adaptador.rollBack(con);
            }
        }
    }
    
    
    public List<FilaInformeHoja2DatosPuestosVO> getDatosInformePuestosHoja2(Integer ejercicio, AdaptadorSQLBD adaptador) throws Exception
    {
        Connection con = null;
        try
        {
            con = adaptador.getConnection();
            MeLanbide46DAO meLanbide39DAO = MeLanbide46DAO.getInstance();
            return meLanbide39DAO.getInformeDatosPuestosHoja2(ejercicio, con);
        }
        catch(BDException e)
        {
            log.error("Se ha producido una excepcion en la BBDD en getDatosInformePuestosHoja2", e);
            throw new Exception(e);
        }
        catch(Exception ex)
        {
            log.error("Se ha producido una excepcion en getDatosInformePuestosHoja2", ex);
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
                adaptador.rollBack(con);
            }
        }
    }
}

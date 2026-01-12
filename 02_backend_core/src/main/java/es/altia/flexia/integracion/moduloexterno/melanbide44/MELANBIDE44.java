/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.melanbide44;

import es.altia.agora.business.escritorio.UsuarioValueObject;
import es.altia.agora.technical.ConstantesDatos;
import es.altia.common.exception.TechnicalException;
import es.altia.flexia.integracion.moduloexterno.melanbide44.exception.ExcelRowMappingException;
import es.altia.flexia.integracion.moduloexterno.melanbide44.exception.FilaProspectorSolicitudNoValidaException;
import es.altia.flexia.integracion.moduloexterno.melanbide44.exception.FilaSeguimientoPrepNoValidaException;
import es.altia.flexia.integracion.moduloexterno.melanbide44.i18n.MeLanbide44I18n;
import es.altia.flexia.integracion.moduloexterno.melanbide44.manager.MeLanbide44Manager;
import es.altia.flexia.integracion.moduloexterno.melanbide44.util.ConstantesMeLanbide44;
import es.altia.flexia.integracion.moduloexterno.melanbide44.util.MeLanbide44InformeUtils;
import es.altia.flexia.integracion.moduloexterno.melanbide44.util.MeLanbide44MappingUtils;
import es.altia.flexia.integracion.moduloexterno.melanbide44.util.MeLanbide44Utils;
import es.altia.flexia.integracion.moduloexterno.melanbide44.vo.justificacion.EcaJusPreparadoresVO;
import es.altia.flexia.integracion.moduloexterno.melanbide44.vo.justificacion.EcaSegPreparadoresVO;
import es.altia.flexia.integracion.moduloexterno.melanbide44.vo.justificacion.EcaSegPreparadores2016VO;
import es.altia.flexia.integracion.moduloexterno.melanbide44.vo.justificacion.FilaPreparadorJustificacionVO;
import es.altia.flexia.integracion.moduloexterno.melanbide44.vo.justificacion.FilaProspectorJustificacionVO;
import es.altia.flexia.integracion.moduloexterno.melanbide44.vo.justificacion.FilaSegPreparadoresVO;
import es.altia.flexia.integracion.moduloexterno.melanbide44.vo.comun.EcaConfiguracionVO;
import es.altia.flexia.integracion.moduloexterno.melanbide44.vo.comun.SelectItem;
import es.altia.flexia.integracion.moduloexterno.melanbide44.vo.informes.FilaInformeDesglose;
import es.altia.flexia.integracion.moduloexterno.melanbide44.vo.informes.FilaInformeProyectos;
import es.altia.flexia.integracion.moduloexterno.melanbide44.vo.informes.FilaResumenInformeProyectos;
import es.altia.flexia.integracion.moduloexterno.melanbide44.vo.justificacion.EcaInsPreparadoresVO;
import es.altia.flexia.integracion.moduloexterno.melanbide44.vo.justificacion.EcaInsPreparadores2016VO;
import es.altia.flexia.integracion.moduloexterno.melanbide44.vo.justificacion.EcaJusProspectoresVO;
import es.altia.flexia.integracion.moduloexterno.melanbide44.vo.justificacion.EcaVisProspectoresVO;
import es.altia.flexia.integracion.moduloexterno.melanbide44.vo.justificacion.EcaVisProspectores2016VO;
import es.altia.flexia.integracion.moduloexterno.melanbide44.vo.justificacion.FilaInsPreparadoresVO;
import es.altia.flexia.integracion.moduloexterno.melanbide44.vo.justificacion.FilaVisProspectoresVO;
import es.altia.flexia.integracion.moduloexterno.melanbide44.vo.procesos.FilaAuditoriaProcesosVO;
import es.altia.flexia.integracion.moduloexterno.melanbide44.vo.resumen.EcaResDetalleInsercionesVO;
import es.altia.flexia.integracion.moduloexterno.melanbide44.vo.resumen.EcaResumenVO;
import es.altia.flexia.integracion.moduloexterno.melanbide44.vo.resumen.FilaEcaResPreparadoresVO;
import es.altia.flexia.integracion.moduloexterno.melanbide44.vo.resumen.FilaEcaResProspectoresVO;
import es.altia.flexia.integracion.moduloexterno.melanbide44.vo.solicitud.DatosAnexosVO;
import es.altia.flexia.integracion.moduloexterno.melanbide44.vo.solicitud.EcaSolPreparadoresVO;
import es.altia.flexia.integracion.moduloexterno.melanbide44.vo.solicitud.EcaSolProspectoresVO;
import es.altia.flexia.integracion.moduloexterno.melanbide44.vo.solicitud.EcaSolValoracionVO;
import es.altia.flexia.integracion.moduloexterno.melanbide44.vo.solicitud.EcaSolicitudVO;
import es.altia.flexia.integracion.moduloexterno.melanbide44.vo.solicitud.FilaPreparadorSolicitudVO;
import es.altia.flexia.integracion.moduloexterno.melanbide44.vo.solicitud.FilaProspectorSolicitudVO;
import es.altia.flexia.integracion.moduloexterno.plugin.ModuloIntegracionExterno;
import es.altia.technical.PortableContext;
import es.altia.util.conexion.AdaptadorSQLBD;
//import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
//import java.io.File;
//import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.struts.upload.MultipartRequestWrapper;
import org.apache.struts.upload.CommonsMultipartRequestHandler;
import org.apache.struts.upload.FormFile;
/*import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.BufferedInputStream;*/



/**
 *
 * @author santiagoc
 */
public class MELANBIDE44  extends ModuloIntegracionExterno
{
    
    //Logger
    private static Logger log = LogManager.getLogger(MELANBIDE44.class);
    ResourceBundle m_Conf = ResourceBundle.getBundle("common");
    
    public void cargarExpedienteExtension(int codigoOrganizacion, String numeroExpediente, String xml) throws Exception {
            final Class cls = Class.forName("es.altia.flexia.integracion.moduloexterno.melanbide42.MELANBIDE42");
            final Object me42Class = cls.newInstance();
            final Class[] types = {int.class, String.class, String.class};
            final Method method = cls.getMethod("cargarExpedienteExtension", types);

            method.invoke(me42Class, codigoOrganizacion, numeroExpediente, xml);
    }
    
    public String lanzaHistorico (int codOrganizacion,int codTramite,int ocurrenciaTramite, String numExpediente)
   {
        if(log.isDebugEnabled()) log.debug("lanzaHistorico ( codOrganizacion = " + codOrganizacion + " codTramite = " + codTramite + " ocurrenciaTramite = " + ocurrenciaTramite + " numExpediente = " + numExpediente  + " ) : BEGIN");
       String clave= null;
        String codigoOperacion = "0";
        String mensaje = "";
        
        try{           
            if(numExpediente!=null && !"".equals(numExpediente)){
                AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
                                                     
                 MeLanbide44Manager.getInstance().guardaHistorico(numExpediente, adapt);
            }
        }
        catch(Exception ex)
        {
            codigoOperacion = "2";
        }
        if(log.isDebugEnabled()) log.debug("lanzaHistorico() : END");
        return codigoOperacion;
    }
    
    public String cargarPantallaSolicitudPrincipal(int codOrganizacion,  int codTramite, int ocurrenciaTramite,String numExpediente,HttpServletRequest request,HttpServletResponse response)
    {
        AdaptadorSQLBD adapt = null;
        try
        {
            adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
        }
        catch(Exception ex)
        {
            
        }
        String url = null;
        
        try
        {
            int ano = MeLanbide44Utils.getEjercicioDeExpediente(numExpediente);
            EcaConfiguracionVO config = MeLanbide44Manager.getInstance().getConfiguracionEca(ano, adapt);
            if(config != null)
            {
                request.getSession().setAttribute("ecaConfiguracion", config);
            }
        }catch(Exception ex)
        {
            
        }
        
        //Subpestana SOLICITUD
        if(adapt != null)
        {
            try
            {
                EcaSolicitudVO sol = MeLanbide44Manager.getInstance().getDatosSolicitud(numExpediente, adapt);
                if(sol != null)
                {
                    request.setAttribute("ecaSolicitud", sol);
                }
                    
                try
                {
                    url = cargarSubpestanaSolicitud_Solicitud(sol, adapt, request);
                    if(url != null)
                    {
                        request.setAttribute("urlPestanaSolicitud_solicitud", url);
                    }
                }
                catch(Exception ex)
                {

                }

                //Subpestana PREPARADORES
                try
                {
                    url = cargarSubpestanaPreparadores_Solicitud(sol, adapt, request);
                    if(url != null)
                    {
                        request.setAttribute("urlPestanaPreparadores_solicitud", url);
                    }
                }
                catch(Exception ex)
                {

                }

                //Subpestana PROSPECTORES
                try
                {
                    url = cargarSubpestanaProspectores_Solicitud(sol, adapt, request);
                    if(url != null)
                    {
                        request.setAttribute("urlPestanaProspectores_solicitud", url);
                    }
                }
                catch(Exception ex)
                {

                }
                
                //Subpestana VALORACION
                try
                {
                    url = cargarSubpestanaValoracion_Solicitud(sol, adapt, request);
                    if(url != null)
                    {
                        request.setAttribute("urlPestanaValoracion_solicitud", url);
                    }
                }
                catch(Exception ex)
                {

                }
            }
            catch(Exception ex)
            {

            }
        }
        
        return "/jsp/extension/melanbide44/solicitud/solicitud_principal.jsp";
    }
    
    public String cargarPantallaJustificacion(int codOrganizacion,  int codTramite, int ocurrenciaTramite,String numExpediente,HttpServletRequest request,HttpServletResponse response)
    {
        AdaptadorSQLBD adapt = null;
        
        try
        {
            adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
            
        }
        catch(Exception ex)
        {
            
        }
        String url = null;
        //Subpestana SOLICITUD
        if(adapt != null)
        {
            try
            {               
                //Subpestana PREPARADORES
                try
                {
                    EcaSolicitudVO sol = MeLanbide44Manager.getInstance().getDatosSolicitud(numExpediente, adapt);
                    if(sol == null)
                    {
                        sol = new EcaSolicitudVO();
                    }
                    url = cargarSubpestanaPreparadores_Justificacion(sol,adapt, request);
                    if(url != null)
                    {
                        request.setAttribute("urlPestanaPreparadores_justificacion", url);
                    }
                }
                catch(Exception ex)
                {
                    log.error(ex.getMessage());
                }

                //Subpestana PROSPECTORES
                try
                {
                    EcaSolicitudVO sol = MeLanbide44Manager.getInstance().getDatosSolicitud(numExpediente, adapt);
                    if(sol == null)
                    {
                        sol = new EcaSolicitudVO();
                    }
                    url = cargarSubpestanaProspectores_Justificacion(sol, adapt, request);
                    if(url != null)
                    {
                        request.setAttribute("urlPestanaProspectores_justificacion", url);
                    }
                }
                catch(Exception ex)
                {
                    log.error(ex.getMessage());
                }
            }
            catch(Exception ex)
            {
                log.error(ex.getMessage());
            }
        }
        
        return "/jsp/extension/melanbide44/justificacion/justificacion.jsp";
    }
    
    private String cargarSubpestanaPreparadores_Justificacion(EcaSolicitudVO sol,AdaptadorSQLBD adapt, HttpServletRequest request)
    {
        int codIdioma = 1;    
        try
        {
            UsuarioValueObject usuario = (UsuarioValueObject) request.getSession().getAttribute("usuario");
            if(usuario != null)
            {
                codIdioma = usuario.getIdioma();
            }
        }
        catch(Exception ex)
        {

        }
        try
        {
            if(sol.getNumExp() != null){
                List<FilaPreparadorJustificacionVO> listaPreparadoresJustificacion = MeLanbide44Manager.getInstance().getListaPreparadoresJustificacion(sol, adapt, codIdioma);
                if(listaPreparadoresJustificacion != null)
                {
                    request.setAttribute("listaPreparadoresJustificacion", listaPreparadoresJustificacion);
                }
            }
        }
        catch(Exception ex)
        {
            log.error(ex.getMessage());
        }       
        return "/jsp/extension/melanbide44/justificacion/preparadores.jsp";
    }
    
    private String cargarSubpestanaProspectores_Justificacion(EcaSolicitudVO sol, AdaptadorSQLBD adapt, HttpServletRequest request)
    {
        if(sol != null)
        {
            int codIdioma = 1;    
            try
            {
                UsuarioValueObject usuario = (UsuarioValueObject) request.getSession().getAttribute("usuario");
                if(usuario != null)
                {
                    codIdioma = usuario.getIdioma();
                }
            }
            catch(Exception ex)
            {
                log.error(ex.getMessage());
            }
            try
            {
                if(sol.getNumExp() != null){
                    List<FilaProspectorJustificacionVO> listaProspectoresJus = MeLanbide44Manager.getInstance().getListaProspectoresJustificacion(sol, adapt, codIdioma);
                    if(listaProspectoresJus != null)
                    {
                        request.setAttribute("listaProspectoresJus", listaProspectoresJus);
                    }
                }
            }
            catch(Exception ex)
            {
                log.error(ex.getMessage());
            } 
        }
        return "/jsp/extension/melanbide44/justificacion/prospectores.jsp";
    }
    
    public String cargarPantallaResumen(int codOrganizacion,  int codTramite, int ocurrenciaTramite,String numExpediente,HttpServletRequest request,HttpServletResponse response)
    {
        
        try
        {
            EcaResumenVO res = this.calcularDatosResumen(codOrganizacion, codTramite, ocurrenciaTramite, numExpediente, request, response);
            if(res != null)
            {
                request.setAttribute("datosResumen", res);
            }
        }
        
        catch(Exception ee)
        {
            log.error("Error en cargarPantallaResumen" , ee);
        }
        
        return "/jsp/extension/melanbide44/resumen/resumen.jsp";
    }
    
    public void guardarDatosSolicitud(int codOrganizacion,  int codTramite, int ocurrenciaTramite,String numExpediente,HttpServletRequest request,HttpServletResponse response)
    {
        
        String codigoOperacion = "0";
        try
        {
            AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
            try
            {
                String c1H = (String)request.getParameter("col1SolicH");
                String c1M = (String)request.getParameter("col1SolicM");
                String c2H = (String)request.getParameter("col2SolicH");
                String c2M = (String)request.getParameter("col2SolicM");
                String c3H = (String)request.getParameter("col3SolicH");
                String c3M = (String)request.getParameter("col3SolicM");
                String c4H = (String)request.getParameter("col4SolicH");
                String c4M = (String)request.getParameter("col4SolicM");
                  String c5H = (String)request.getParameter("col5SolicH");
                String c5M = (String)request.getParameter("col5SolicM");
                  String c6H = (String)request.getParameter("col6SolicH");
                String c6M = (String)request.getParameter("col6SolicM");
                
                String segH = (String)request.getParameter("seg1SolicH");
                String segM = (String)request.getParameter("seg1SolicM");
                String segActuaciones = (String)request.getParameter("nActuacionesSolic");
                String prospectoresNum = (String)request.getParameter("prospectoresSolicNum");
                String prospectoresImp = (String)request.getParameter("prospectoresSolicSol");
                String preparadoresNum = (String)request.getParameter("preparadoresSolicNum");
                String preparadoresImp = (String)request.getParameter("preparadoresSolicSol");
                String gastos = (String)request.getParameter("gastosGeneralesSolic");
                String otrasSub = (String)request.getParameter("otrasAyudas");
                String subPub = (String)request.getParameter("impOrgPublicos");
                String subPriv = (String)request.getParameter("impOrgPrivados");
                String totalSub = (String)request.getParameter("totSubSolicitada");
                String totalAprobado = (String)request.getParameter("totSubAprobada");
                String subPubCargaManual = (String)request.getParameter("impOrgPublicosAnexSol");
                String subPrivCargaManual = (String)request.getParameter("impOrgPrivadosAnexSol");
                //concedido
                String c1HConc = (String)request.getParameter("col1HConc");
                String c1MConc = (String)request.getParameter("col1MConc");
                String c2HConc = (String)request.getParameter("col2HConc");
                String c2MConc = (String)request.getParameter("col2MConc");
                String c3HConc = (String)request.getParameter("col3HConc");
                String c3MConc = (String)request.getParameter("col3MConc");
                String c4HConc = (String)request.getParameter("col4HConc");
                String c4MConc = (String)request.getParameter("col4MConc");
                String c5HConc = (String)request.getParameter("col5HConc");
                String c5MConc = (String)request.getParameter("col5MConc");
                String c6HConc = (String)request.getParameter("col6HConc");
                String c6MConc = (String)request.getParameter("col6MConc");
                String segHConc = (String)request.getParameter("segHConc");
                String segMConc = (String)request.getParameter("segMConc");
                String segActuacionesConc = (String)request.getParameter("nActuacionesConc");
                String prospectoresNumConc = (String)request.getParameter("prospectoresNumConc");
                String prospectoresImpConc = (String)request.getParameter("prospectoresSolConc");
                String preparadoresNumConc = (String)request.getParameter("preparadoresNumConc");
                String preparadoresImpConc = (String)request.getParameter("preparadoresSolConc");
                String gastosConc = (String)request.getParameter("gastosGeneralesConc");
                
                
                String subPubConcedidos = (String)request.getParameter("impOrgPublicosConcedidos");
                String subPrivConcedidos = (String)request.getParameter("impOrgPrivadosConcedidos");
                
                EcaSolicitudVO sol = MeLanbide44Manager.getInstance().getDatosSolicitud(numExpediente, adapt);
                if(sol == null)
                {
                    sol = new EcaSolicitudVO();
                }
                
                sol.setGastos(gastos != null && !gastos.equals("") ? new BigDecimal(gastos.replaceAll(",", "\\.")) : null);
                sol.setInserC1H(c1H != null && !c1H.equals("") ? Float.parseFloat(c1H) : null);
                sol.setInserC1M(c1M != null && !c1M.equals("") ? Float.parseFloat(c1M) : null);
                sol.setInserC2H(c2H != null && !c2H.equals("") ? Float.parseFloat(c2H) : null);
                sol.setInserC2M(c2M != null && !c2M.equals("") ? Float.parseFloat(c2M) : null);
                sol.setInserC3H(c3H != null && !c3H.equals("") ? Float.parseFloat(c3H) : null);
                sol.setInserC3M(c3M != null && !c3M.equals("") ? Float.parseFloat(c3M) : null);
                sol.setInserC4H(c4H != null && !c4H.equals("") ? Float.parseFloat(c4H) : null);
                sol.setInserC4M(c4M != null && !c4M.equals("") ? Float.parseFloat(c4M) : null);
                sol.setInserC5H(c5H != null && !c5H.equals("") ? Float.parseFloat(c5H) : null);
                sol.setInserC5M(c5M != null && !c5M.equals("") ? Float.parseFloat(c5M) : null);
                sol.setInserC6H(c6H != null && !c6H.equals("") ? Float.parseFloat(c6H) : null);
                sol.setInserC6M(c6M != null && !c6M.equals("") ? Float.parseFloat(c6M) : null);
                sol.setNumExp(numExpediente);
                sol.setExpEje(MeLanbide44Utils.getEjercicioDeExpediente(numExpediente));
                
                sol.setInserC1HConc(c1HConc != null && !c1HConc.equals("") ? Float.parseFloat(c1HConc) : null);
                sol.setInserC1MConc(c1MConc != null && !c1MConc.equals("") ? Float.parseFloat(c1MConc) : null);
                sol.setInserC2HConc(c2HConc != null && !c2HConc.equals("") ? Float.parseFloat(c2HConc) : null);
                sol.setInserC2MConc(c2MConc != null && !c2MConc.equals("") ? Float.parseFloat(c2MConc) : null);
                sol.setInserC3HConc(c3HConc != null && !c3HConc.equals("") ? Float.parseFloat(c3HConc) : null);
                sol.setInserC3MConc(c3MConc != null && !c3MConc.equals("") ? Float.parseFloat(c3MConc) : null);
                sol.setInserC4HConc(c4HConc != null && !c4HConc.equals("") ? Float.parseFloat(c4HConc) : null);
                sol.setInserC4MConc(c4MConc != null && !c4MConc.equals("") ? Float.parseFloat(c4MConc) : null);
                sol.setInserC5HConc(c5HConc != null && !c5HConc.equals("") ? Float.parseFloat(c5HConc) : null);
                sol.setInserC5MConc(c5MConc != null && !c5MConc.equals("") ? Float.parseFloat(c5MConc) : null);
                sol.setInserC6HConc(c6HConc != null && !c6HConc.equals("") ? Float.parseFloat(c6HConc) : null);
                sol.setInserC6MConc(c6MConc != null && !c6MConc.equals("") ? Float.parseFloat(c6MConc) : null);
                
                sol.setSegHConc(segHConc != null && !segHConc.equals("") ? Integer.parseInt(segHConc) : null);
                sol.setSegMConc(segMConc != null && !segMConc.equals("") ? Integer.parseInt(segMConc) : null);
                sol.setPreparadoresImpConc(preparadoresImpConc != null && !preparadoresImpConc.equals("") ?  new BigDecimal(preparadoresImpConc.replaceAll(",", "\\.")) : null);
                sol.setPreparadoresNumConc(preparadoresNumConc != null && !preparadoresNumConc.equals("") ? Integer.parseInt(preparadoresNumConc) : null);
                sol.setProspectoresImpConc(prospectoresImpConc != null && !prospectoresImpConc.equals("") ? new BigDecimal(prospectoresImpConc.replaceAll(",", "\\.")) : null);
                sol.setProspectoresNumConc(prospectoresNumConc != null && !prospectoresNumConc.equals("") ? Integer.parseInt(prospectoresNumConc) : null);
                sol.setSegActuacionesConc(segActuacionesConc != null && !segActuacionesConc.equals("") ? new BigDecimal(segActuacionesConc.replaceAll(",", "\\.")) : null);
                sol.setGastosConc(gastosConc != null && !gastosConc.equals("") ? new BigDecimal(gastosConc.replaceAll(",", "\\.")) : null);
                
                if(otrasSub != null)
                {
                    if(otrasSub.equalsIgnoreCase(ConstantesMeLanbide44.CIERTO))
                    {
                        sol.setOtrasSub(true);
                        sol.setSubPriv(subPriv != null && !subPriv.equals("") ? new BigDecimal(subPriv.replaceAll(",", "\\.")) : null);
                        sol.setSubPub(subPub != null && !subPub.equals("") ? new BigDecimal(subPub.replaceAll(",", "\\.")) : null);
                        sol.setSubPrivCargaManual(subPrivCargaManual != null && !subPrivCargaManual.equals("") ? new BigDecimal(subPrivCargaManual.replaceAll(",", "\\.")) : null);
                        sol.setSubPubCargaManual(subPubCargaManual != null && !subPubCargaManual.equals("") ? new BigDecimal(subPubCargaManual.replaceAll(",", "\\.")) : null);
                        sol.setSubPrivConcedidos(subPrivCargaManual != null && !subPrivConcedidos.equals("") ? new BigDecimal(subPrivConcedidos.replaceAll(",", "\\.")) : null);
                        sol.setSubPubConcedidos(subPubCargaManual != null && !subPubConcedidos.equals("") ? new BigDecimal(subPubConcedidos.replaceAll(",", "\\.")) : null);
                    }
                    else if(otrasSub.equalsIgnoreCase(ConstantesMeLanbide44.FALSO))
                    {
                        sol.setOtrasSub(false);
                        sol.setSubPriv(null);
                        sol.setSubPub(null);
                        sol.setSubPrivCargaManual(null);
                        sol.setSubPubCargaManual(null);
                    }
                    else
                    {
                        sol.setOtrasSub(null);
                        sol.setSubPriv(null);
                        sol.setSubPub(null);
                        sol.setSubPrivCargaManual(null);
                        sol.setSubPubCargaManual(null);
                    }
                }


                sol.setPreparadoresImp(preparadoresImp != null && !preparadoresImp.equals("") ?  new BigDecimal(preparadoresImp.replaceAll(",", "\\.")) : null);
                sol.setPreparadoresNum(preparadoresNum != null && !preparadoresNum.equals("") ? Integer.parseInt(preparadoresNum) : null);
                sol.setProspectoresImp(prospectoresImp != null && !prospectoresImp.equals("") ? new BigDecimal(prospectoresImp.replaceAll(",", "\\.")) : null);
                sol.setProspectoresNum(prospectoresNum != null && !prospectoresNum.equals("") ? Integer.parseInt(prospectoresNum) : null);
                sol.setSegActuaciones(segActuaciones != null && !segActuaciones.equals("") ? new BigDecimal(segActuaciones.replaceAll(",", "\\.")) : null);
                sol.setSegH(segH != null && !segH.equals("") ? Integer.parseInt(segH) : null);
                sol.setSegM(segM != null && !segM.equals("") ? Integer.parseInt(segM) : null);
                sol.setTotalAprobado(totalAprobado != null && !totalAprobado.equals("") ? new BigDecimal(totalAprobado.replaceAll(",", "\\.")) : null);
                sol.setTotalSubvencion(totalSub != null && !totalSub.equals("") ? new BigDecimal(totalSub.replaceAll(",", "\\.")) : null);
                sol = MeLanbide44Manager.getInstance().guardarDatosSolicitud(sol, adapt);
                if(sol != null)
                {
                    codigoOperacion = "0";
                }
                else
                {
                    codigoOperacion = "1";
                }
            }
            catch(Exception ex)
            {
                codigoOperacion = "2";
                java.util.logging.Logger.getLogger(MELANBIDE44.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        catch(Exception ex)
        {
            codigoOperacion = "2";
        }
        
        StringBuffer xmlSalida = new StringBuffer();
        xmlSalida.append("<RESPUESTA>");
            xmlSalida.append("<CODIGO_OPERACION>");
                xmlSalida.append(codigoOperacion);
            xmlSalida.append("</CODIGO_OPERACION>");
        xmlSalida.append("</RESPUESTA>");
        try{
            response.setContentType("text/xml");
            response.setCharacterEncoding("UTF-8");
            PrintWriter out = response.getWriter();
            out.print(xmlSalida.toString());
            out.flush();
            out.close();
        }catch(Exception e){
            log.error(" Error : " + e.getMessage(), e);
        }//try-catch
    }
    
    public String procesarExcelPreparadores(int codOrganizacion,  int codTramite, int ocurrenciaTramite,String numExpediente,HttpServletRequest request,HttpServletResponse response)
    {
        int codIdioma = 1; 
        int i = 0;
        try
        {
            UsuarioValueObject usuario = (UsuarioValueObject) request.getSession().getAttribute("usuario");
            if(usuario != null)
            {
                codIdioma = usuario.getIdioma();
            }
        }
        catch(Exception ex)
        {

        }
        try
        {
            
            MultipartRequestWrapper wrapper = new MultipartRequestWrapper(request);
            
            wrapper.getParameterMap();
            CommonsMultipartRequestHandler handler = new CommonsMultipartRequestHandler();
            handler.handleRequest(request);
            Hashtable<String, FormFile> table = handler.getFileElements();
            FormFile file = table.get("fichero_preparadores_sol");
            
            byte[] data = file.getFileData();
            ByteArrayInputStream bais = new ByteArrayInputStream(data);
            HSSFWorkbook workbook = new HSSFWorkbook(bais);
            
            HSSFSheet sheet = workbook.getSheetAt(0);
            
            List<String> nifsRepetidos = this.hayNifsRepetidos(sheet, 0);
            
            

            if(nifsRepetidos == null || nifsRepetidos.size() == 0)
            {
                List<String> sustitutosDeSustitutos = this.haySustitutosDeSustitutos(sheet, 0, 24);
                if(sustitutosDeSustitutos == null || sustitutosDeSustitutos.isEmpty())
                {
                    int filaIni = sheet.getFirstRowNum();
                    int filaFin = sheet.getLastRowNum();

                    HSSFRow row = null;
                    List<EcaSolPreparadoresVO> filasImportar = new ArrayList<EcaSolPreparadoresVO>();
                    EcaSolPreparadoresVO prep = null;
                    AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
                    try
                    {
                        for(i = filaIni+1; i <= filaFin; i++)
                        {
                            row = sheet.getRow(i);
                            prep = (EcaSolPreparadoresVO)MeLanbide44MappingUtils.getInstance().map(row, EcaSolPreparadoresVO.class);
                            if(prep != null)
                            {
                                //los datos se guardan en campos carga y modificables
                                prep.setNif_Carga(prep.getNif());
                                prep.setNombre_Carga(prep.getNombre());
                                prep.setFecIni_Carga(prep.getFecIni());
                                prep.setFecFin_Carga(prep.getFecFin());
                                prep.setHorasCont_Carga(prep.getHorasCont());
                                prep.setHorasJC_Carga(prep.getHorasJC());
                                prep.setHorasEca_Carga(prep.getHorasEca());
                                prep.setImpSSJC_Carga(prep.getImpSSJC());
                                prep.setImpSSJR_Carga(prep.getImpSSJR());
                                prep.setImpSSECA_Carga(prep.getImpSSECA());
                                prep.setSegAnt_Carga(prep.getSegAnt());
                                prep.setImpSegAnt_Carga(prep.getImpSegAnt());
                                prep.setInsC1H_Carga(prep.getInsC1H());
                                prep.setInsC1M_Carga(prep.getInsC1M());
                                prep.setInsC1_Carga(prep.getInsC1());
                                prep.setInsC2H_Carga(prep.getInsC2H());
                                prep.setInsC2M_Carga(prep.getInsC2M());
                                prep.setInsC2_Carga(prep.getInsC2());
                                prep.setInsC3H_Carga(prep.getInsC3H());
                                prep.setInsC3M_Carga(prep.getInsC3M());
                                prep.setInsC3_Carga(prep.getInsC3());
                                prep.setInsC4H_Carga(prep.getInsC4H());
                                prep.setInsC4M_Carga(prep.getInsC4M());
                                prep.setInsC4_Carga(prep.getInsC4());

                                //prep.setInsImporte_Carga(prep.getInsImporte());
                                //prep.setInsSegImporte_Carga(prep.getInsSegImporte());
                                //prep.setCoste_Carga(prep.getCoste());

                                //datos calculados
                                if(prep.getInsC1H() != null || prep.getInsC1M() != null)
                                {
                                    prep.setInsC1((prep.getInsC1H()!=null?prep.getInsC1H():BigDecimal.ZERO).add(prep.getInsC1M()!=null?prep.getInsC1M():BigDecimal.ZERO));
                                    if(!MeLanbide44Utils.validarDatoExcel(prep.getInsC1().toPlainString(), ConstantesMeLanbide44.TiposRequeridos.NUMERICO_DECIMAL, -1, 8, 2, true))
                                    {
                                        ExcelRowMappingException erme = new ExcelRowMappingException("12");
                                        erme.setLongMax(-1);
                                        erme.setTipo(ConstantesMeLanbide44.TiposRequeridos.NUMERICO_DECIMAL);
                                        erme.setpEntera(8);
                                        erme.setpDecimal(2);
                                        erme.setMensaje(String.format(MeLanbide44I18n.getInstance().getMensaje(codIdioma, "validaciones.solicitud.preparadores.totalColectivoDemasiadoGrande"), 1, i+1));
                                        throw erme;
                                    }
                                }
                                else
                                {
                                    prep.setInsC1(null);
                                }
                                if(prep.getInsC2H() != null || prep.getInsC2M() != null)
                                {
                                    prep.setInsC2((prep.getInsC2H()!=null?prep.getInsC2H():BigDecimal.ZERO).add(prep.getInsC2M()!=null?prep.getInsC2M():BigDecimal.ZERO));
                                    if(!MeLanbide44Utils.validarDatoExcel(prep.getInsC2().toPlainString(), ConstantesMeLanbide44.TiposRequeridos.NUMERICO_DECIMAL, -1, 8, 2, true))
                                    {
                                        ExcelRowMappingException erme = new ExcelRowMappingException("12");
                                        erme.setLongMax(-1);
                                        erme.setTipo(ConstantesMeLanbide44.TiposRequeridos.NUMERICO_DECIMAL);
                                        erme.setpEntera(8);
                                        erme.setpDecimal(2);
                                        erme.setMensaje(String.format(MeLanbide44I18n.getInstance().getMensaje(codIdioma, "validaciones.solicitud.preparadores.totalColectivoDemasiadoGrande"), 2, i+1));
                                        throw erme;
                                    }
                                }
                                else
                                {
                                    prep.setInsC2(null);
                                }
                                if(prep.getInsC3H() != null || prep.getInsC3M() != null)
                                {
                                    prep.setInsC3((prep.getInsC3H()!=null?prep.getInsC3H():BigDecimal.ZERO).add(prep.getInsC3M()!=null?prep.getInsC3M():BigDecimal.ZERO));
                                    if(!MeLanbide44Utils.validarDatoExcel(prep.getInsC3().toPlainString(), ConstantesMeLanbide44.TiposRequeridos.NUMERICO_DECIMAL, -1, 8, 2, true))
                                    {
                                        ExcelRowMappingException erme = new ExcelRowMappingException("12");
                                        erme.setLongMax(-1);
                                        erme.setTipo(ConstantesMeLanbide44.TiposRequeridos.NUMERICO_DECIMAL);
                                        erme.setpEntera(8);
                                        erme.setpDecimal(2);
                                        erme.setMensaje(String.format(MeLanbide44I18n.getInstance().getMensaje(codIdioma, "validaciones.solicitud.preparadores.totalColectivoDemasiadoGrande"), 3, i+1));
                                        throw erme;
                                    }
                                }
                                else
                                {
                                    prep.setInsC3(null);
                                }
                                if(prep.getInsC4H() != null || prep.getInsC4M() != null)
                                {
                                    prep.setInsC4((prep.getInsC4H()!=null?prep.getInsC4H():BigDecimal.ZERO).add(prep.getInsC4M()!=null?prep.getInsC4M():BigDecimal.ZERO));
                                    if(!MeLanbide44Utils.validarDatoExcel(prep.getInsC4().toPlainString(), ConstantesMeLanbide44.TiposRequeridos.NUMERICO_DECIMAL, -1, 8, 2, true))
                                    {
                                        ExcelRowMappingException erme = new ExcelRowMappingException("12");
                                        erme.setLongMax(-1);
                                        erme.setTipo(ConstantesMeLanbide44.TiposRequeridos.NUMERICO_DECIMAL);
                                        erme.setpEntera(8);
                                        erme.setpDecimal(2);
                                        erme.setMensaje(String.format(MeLanbide44I18n.getInstance().getMensaje(codIdioma, "validaciones.solicitud.preparadores.totalColectivoDemasiadoGrande"), 4, i+1));
                                        throw erme;
                                    }
                                }
                                else
                                {
                                    prep.setInsC4(null);
                                }

                                int ano = MeLanbide44Utils.getEjercicioDeExpediente(numExpediente);
                                EcaConfiguracionVO config = MeLanbide44Manager.getInstance().getConfiguracionEca(ano, adapt);
                                BigDecimal importeCalculado = null;
                                if(prep.getInsC1H() != null)
                                {
                                    if(importeCalculado == null)
                                    {
                                        importeCalculado = new BigDecimal("0.00");
                                    }
                                    importeCalculado = importeCalculado.add(prep.getInsC1H().multiply((config!= null && config.getImC1h() != null ?config.getImC1h() : new BigDecimal("1"))));
                                }
                                if(prep.getInsC1M() != null)
                                {
                                    if(importeCalculado == null)
                                    {
                                        importeCalculado = new BigDecimal("0.00");
                                    }
                                    importeCalculado = importeCalculado.add(prep.getInsC1M().multiply((config!= null && config.getImC1m() != null ?config.getImC1m() : new BigDecimal("1"))));
                                }
                                if(prep.getInsC2H() != null)
                                {
                                    if(importeCalculado == null)
                                    {
                                        importeCalculado = new BigDecimal("0.00");
                                    }
                                    importeCalculado = importeCalculado.add(prep.getInsC2H().multiply((config!= null && config.getImC2h() != null ?config.getImC2h() : new BigDecimal("1"))));
                                }
                                if(prep.getInsC2M() != null)
                                {
                                    if(importeCalculado == null)
                                    {
                                        importeCalculado = new BigDecimal("0.00");
                                    }
                                    importeCalculado = importeCalculado.add(prep.getInsC2M().multiply((config!= null && config.getImC2m() != null ?config.getImC2m() : new BigDecimal("1"))));
                                }
                                if(prep.getInsC3H() != null)
                                {
                                    if(importeCalculado == null)
                                    {
                                        importeCalculado = new BigDecimal("0.00");
                                    }
                                    importeCalculado = importeCalculado.add(prep.getInsC3H().multiply((config!= null && config.getImC3h() != null ?config.getImC3h() : new BigDecimal("1"))));
                                }
                                if(prep.getInsC3M() != null)
                                {
                                    if(importeCalculado == null)
                                    {
                                        importeCalculado = new BigDecimal("0.00");
                                    }
                                    importeCalculado = importeCalculado.add(prep.getInsC3M().multiply((config!= null && config.getImC3m() != null ?config.getImC3m() : new BigDecimal("1"))));
                                }
                                if(prep.getInsC4H() != null)
                                {
                                    if(importeCalculado == null)
                                    {
                                        importeCalculado = new BigDecimal("0.00");
                                    }
                                    importeCalculado = importeCalculado.add(prep.getInsC4H().multiply((config!= null && config.getImC4h() != null ?config.getImC4h() : new BigDecimal("1"))));
                                }
                                if(prep.getInsC4M() != null)
                                {
                                    if(importeCalculado == null)
                                    {
                                        importeCalculado = new BigDecimal("0.00");
                                    }
                                    importeCalculado = importeCalculado.add(prep.getInsC4M().multiply((config!= null && config.getImC4m() != null ?config.getImC4m() : new BigDecimal("1"))));
                                }
                                if(importeCalculado != null && !MeLanbide44Utils.validarDatoExcel(importeCalculado.toPlainString(), ConstantesMeLanbide44.TiposRequeridos.NUMERICO_DECIMAL, -1, 8, 2, true))
                                {
                                    ExcelRowMappingException erme = new ExcelRowMappingException("12");
                                    erme.setLongMax(-1);
                                    erme.setTipo(ConstantesMeLanbide44.TiposRequeridos.NUMERICO_DECIMAL);
                                    erme.setpEntera(8);
                                    erme.setpDecimal(2);
                                    erme.setMensaje(String.format(MeLanbide44I18n.getInstance().getMensaje(codIdioma, "validaciones.solicitud.preparadores.impInsercionesDemasiadoGrande"), i+1));
                                    throw erme;
                                }
                                else
                                {
                                    prep.setInsImporte(importeCalculado);
                                }

                                BigDecimal costesSalSSEca = null;
                                if(prep.getImpSSJC()!=null && prep.getHorasEca() != null && prep.getHorasJC() != null)
                                {
                                    costesSalSSEca = new BigDecimal("0.00");
                                    costesSalSSEca = prep.getImpSSJC().multiply(prep.getHorasEca());
                                    costesSalSSEca = !prep.getHorasJC().equals(BigDecimal.ZERO) ? costesSalSSEca.divide(prep.getHorasJC(),2, BigDecimal.ROUND_HALF_UP) : BigDecimal.ZERO;  
                                }
                                prep.setImpSSECA(costesSalSSEca);

                                if(prep.getSegAnt() != null)
                                {
                                    BigDecimal impSeg =config!= null && config.getImSeguimiento() != null ?config.getImSeguimiento() : BigDecimal.ZERO;
                                    BigDecimal importeCalcSeg = new BigDecimal(prep.getSegAnt().toString()).multiply(impSeg);                     

                                    if(costesSalSSEca != null)
                                    {
                                        BigDecimal poMax =config!= null && config.getPoMaxSeguimientos() != null ?config.getPoMaxSeguimientos() : new BigDecimal("1");
                                        BigDecimal por = costesSalSSEca.multiply(poMax);
                                        if(importeCalcSeg != null && importeCalcSeg.compareTo(por) > 0)//mayor
                                        {
                                            importeCalcSeg = new BigDecimal(por.toPlainString());
                                        }
                                    }

                                    importeCalcSeg = importeCalcSeg.setScale(2, RoundingMode.HALF_UP);

                                    if(importeCalcSeg != null && !MeLanbide44Utils.validarDatoExcel(importeCalcSeg.toPlainString(), ConstantesMeLanbide44.TiposRequeridos.NUMERICO_DECIMAL, -1, 8, 2, true))
                                    {
                                        ExcelRowMappingException erme = new ExcelRowMappingException("12");
                                        erme.setLongMax(-1);
                                        erme.setTipo(ConstantesMeLanbide44.TiposRequeridos.NUMERICO_DECIMAL);
                                        erme.setpEntera(8);
                                        erme.setpDecimal(2);
                                        erme.setMensaje(String.format(MeLanbide44I18n.getInstance().getMensaje(codIdioma, "validaciones.solicitud.preparadores.importeSegAntDemasiadoGrande"), String.valueOf(ano), i+1));
                                        throw erme;
                                    }
                                    else
                                    {   
                                        prep.setImpSegAnt(importeCalcSeg); 
                                    }
                                }    

                                BigDecimal ImpSSJR = null;
                                if(prep.getImpSSJC()!=null && prep.getHorasCont()!=null && prep.getHorasJC()!=null)
                                {
                                    ImpSSJR = prep.getImpSSJC().multiply(prep.getHorasCont());
                                    ImpSSJR = !prep.getHorasJC().equals(BigDecimal.ZERO) ? ImpSSJR.divide(prep.getHorasJC(),2, BigDecimal.ROUND_HALF_UP) : BigDecimal.ZERO; 
                                }
                                if(ImpSSJR != null && !MeLanbide44Utils.validarDatoExcel(ImpSSJR.toPlainString(), ConstantesMeLanbide44.TiposRequeridos.NUMERICO_DECIMAL, -1, 8, 2, true))
                                {
                                    ExcelRowMappingException erme = new ExcelRowMappingException("12");
                                    erme.setLongMax(-1);
                                    erme.setTipo(ConstantesMeLanbide44.TiposRequeridos.NUMERICO_DECIMAL);
                                    erme.setpEntera(8);
                                    erme.setpDecimal(2);
                                    erme.setMensaje(String.format(MeLanbide44I18n.getInstance().getMensaje(codIdioma, "validaciones.solicitud.preparadores.impSSJRDemasiadoGrande"), i+1));
                                    throw erme;
                                }
                                else
                                {
                                    prep.setImpSSJR(ImpSSJR);      
                                }                

                                if(prep.getImpSegAnt() != null || prep.getInsImporte() != null)
                                {
                                    BigDecimal importeCalcInsSeg = BigDecimal.ZERO;
                                    if(prep.getImpSegAnt() != null)
                                    {
                                        importeCalcInsSeg = importeCalcInsSeg.add(prep.getImpSegAnt());
                                    }
                                    if(prep.getInsImporte() != null)
                                    {
                                        importeCalcInsSeg = importeCalcInsSeg.add(prep.getInsImporte());
                                    }

                                    if(importeCalcInsSeg != null && !MeLanbide44Utils.validarDatoExcel(importeCalcInsSeg.toPlainString(), ConstantesMeLanbide44.TiposRequeridos.NUMERICO_DECIMAL, -1, 8, 2, true))
                                    {
                                        ExcelRowMappingException erme = new ExcelRowMappingException("12");
                                        erme.setLongMax(-1);
                                        erme.setTipo(ConstantesMeLanbide44.TiposRequeridos.NUMERICO_DECIMAL);
                                        erme.setpEntera(8);
                                        erme.setpDecimal(2);
                                        erme.setMensaje(String.format(MeLanbide44I18n.getInstance().getMensaje(codIdioma, "validaciones.solicitud.preparadores.impInsSegDemasiadoGrande"), i+1));
                                        throw erme;
                                    }
                                    else
                                    {   
                                        prep.setInsSegImporte(importeCalcInsSeg);
                                    }   
                                }

                                if(prep.getInsSegImporte() != null && costesSalSSEca != null)
                                {
                                    prep.setCoste(costesSalSSEca.compareTo(prep.getInsSegImporte())>0?prep.getInsSegImporte():costesSalSSEca);
                                }
                                else if(prep.getInsSegImporte() != null)
                                {
                                    prep.setCoste(prep.getInsSegImporte());
                                }
                                else if(costesSalSSEca != null)
                                {
                                    prep.setCoste(costesSalSSEca);
                                }
                                else
                                {
                                    prep.setCoste(null);
                                }

                                if(prep.getCoste() != null && !MeLanbide44Utils.validarDatoExcel(prep.getCoste().toPlainString(), ConstantesMeLanbide44.TiposRequeridos.NUMERICO_DECIMAL, -1, 8, 2, true))
                                { 
                                    ExcelRowMappingException erme = new ExcelRowMappingException("12");
                                    erme.setLongMax(-1);
                                    erme.setTipo(ConstantesMeLanbide44.TiposRequeridos.NUMERICO_DECIMAL);
                                    erme.setpEntera(8);
                                    erme.setpDecimal(2);
                                    erme.setMensaje(String.format(MeLanbide44I18n.getInstance().getMensaje(codIdioma, "validaciones.solicitud.preparadores.costeDemasiadoGrande"), i+1));
                                    throw erme;
                                }  

                                if(prep.getInsC1_Carga() == null)
                                {
                                   prep.setInsC1_Carga(prep.getInsC1());
                                }
                                if(prep.getInsC2_Carga() == null)
                                {
                                   prep.setInsC2_Carga(prep.getInsC2());
                                }
                                if(prep.getInsC3_Carga() == null)
                                {
                                   prep.setInsC3_Carga(prep.getInsC3());
                                }
                                if(prep.getInsC4_Carga() == null)
                                {
                                   prep.setInsC4_Carga(prep.getInsC4());
                                }

                                if(prep.getImpSegAnt_Carga() == null)
                                {
                                   prep.setImpSegAnt_Carga(prep.getImpSegAnt());
                                }

                                if(prep.getImpSSECA_Carga() == null)
                                { 
                                   prep.setImpSSECA_Carga(prep.getImpSSECA()); 
                                }

                                if(prep.getImpSSJR_Carga() == null)
                                {
                                   prep.setImpSSJR_Carga(prep.getImpSSJR());
                                }


                                //No vienen en excel
                                prep.setInsImporte_Carga(prep.getInsImporte());
                                prep.setInsSegImporte_Carga(prep.getInsSegImporte());
                                prep.setCoste_Carga(prep.getCoste());

                                filasImportar.add(prep);
                            }
                        }
                        if(filasImportar != null && filasImportar.size() > 0)
                        {
                            Collections.sort(filasImportar);
                            int filasImportadas = MeLanbide44Manager.getInstance().importarPreparadores(numExpediente, filasImportar, adapt);


                            if(filasImportadas < filasImportar.size())
                            {
                                request.getSession().setAttribute("mensajeImportar", MeLanbide44I18n.getInstance().getMensaje(codIdioma, "error.errorImportarGen"));
                            }
                            else
                            {
                                request.getSession().setAttribute("mensajeImportar", MeLanbide44I18n.getInstance().getMensaje(codIdioma, "msg.registrosImportadosOK_1")+" "+filasImportadas+" "+MeLanbide44I18n.getInstance().getMensaje(codIdioma, "msg.registrosImportadosOK_2"));
                            }
                        }
                        else
                        {
                            request.getSession().setAttribute("mensajeImportar", MeLanbide44I18n.getInstance().getMensaje(codIdioma, "error.noHayRegistrosImportar"));
                        }
                    }
                    catch(ExcelRowMappingException erme)
                    {
                        String mensajeImportar = null;
                        if(erme.getMensaje() != null && !erme.getMensaje().equals(""))
                        {
                            mensajeImportar = erme.getMensaje();
                        }
                        else
                        {
                            mensajeImportar = MeLanbide44Utils.crearMensajeDatoExcelNoValido(erme, i+1, codIdioma);
                        }
                        request.getSession().setAttribute("mensajeImportar", mensajeImportar);  
                    }
                }
                else
                {
                    String mensajeImportar = MeLanbide44I18n.getInstance().getMensaje(codIdioma, "error.sustitutosDeSustitutos")+"<br/>";
                    for(String nif : sustitutosDeSustitutos)
                    {
                        mensajeImportar += "<br/>"+nif.toUpperCase();
                    }
                    request.getSession().setAttribute("mensajeImportar", mensajeImportar);
                }
            }
            else
            {
                String mensajeImportar = MeLanbide44I18n.getInstance().getMensaje(codIdioma, "error.nifsRepetidosExcel_1")+"<br/>";
                for(String nif : nifsRepetidos)
                {
                    mensajeImportar += "<br/>"+nif.toUpperCase();
                }
                mensajeImportar += "<br/><br/>"+MeLanbide44I18n.getInstance().getMensaje(codIdioma, "error.nifsRepetidosExcel_2");
                request.getSession().setAttribute("mensajeImportar", mensajeImportar);
            }
        }
        catch(FilaSeguimientoPrepNoValidaException ex)
        {
            log.error(" Error : " + ex.getMessage(), ex);
            request.getSession().setAttribute("mensajeImportar", MeLanbide44I18n.getInstance().getMensaje(codIdioma, "error.datosNoValidosImportar"));
        }
        catch(IOException ex)
        {
            log.error(" Error : " + ex.getMessage(), ex);
            request.getSession().setAttribute("mensajeImportar", MeLanbide44I18n.getInstance().getMensaje(codIdioma, "error.errorProcesarExcel"));
        }
        catch(Exception ex)
        {
            log.error(" Error : " + ex.getMessage(), ex);
            request.getSession().setAttribute("mensajeImportar", MeLanbide44I18n.getInstance().getMensaje(codIdioma, "error.errorImportarGen"));
        }
        return "/jsp/extension/melanbide44/solicitud/recargarListaPreparadoresSolic.jsp";
    }
    
    public void getListaPreparadoresSolicitud(int codOrganizacion,  int codTramite, int ocurrenciaTramite,String numExpediente,HttpServletRequest request,HttpServletResponse response)
    {
        try
        {
            try
            {
                //Al importar un fichero, se inserta este mensaje en sesion como resultado
                //Justo despues de importar se llama a este metodo para recargar la tabla.
                //Este es el punto donde se borra este mensaje de sesion, para no mantenerlo en memoria innecesariamente.
                request.getSession().removeAttribute("mensajeImportar");
            }
            catch(Exception ex)
            {
                
            }
        
            int codIdioma = 1;    
            try
            {
                UsuarioValueObject usuario = (UsuarioValueObject) request.getSession().getAttribute("usuario");
                if(usuario != null)
                {
                    codIdioma = usuario.getIdioma();
                }
            }
            catch(Exception ex)
            {

            }
            
            AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
            MeLanbide44Manager meLanbide44Manager = MeLanbide44Manager.getInstance();
            EcaSolicitudVO sol = meLanbide44Manager.getDatosSolicitud(numExpediente, adapt);
            List<FilaPreparadorSolicitudVO> preparadores = null;
            
            if(sol != null)
            {
                preparadores = MeLanbide44Manager.getInstance().getListaPreparadoresSolicitud(sol, adapt, codIdioma);
            }
            else
            {
                preparadores = new ArrayList<FilaPreparadorSolicitudVO>();
            }
            
            String codigoOperacion = "0";
            
            this.escribirListaPreparadoresRequest(codigoOperacion, preparadores, null, codIdioma, response);
            
        }
        catch(Exception ex)
        {
            log.error(" Error : " + ex.getMessage(), ex);
        }
    }
    
    public void eliminarPreparadorSolicitud(int codOrganizacion,  int codTramite, int ocurrenciaTramite,String numExpediente,HttpServletRequest request,HttpServletResponse response)
    {
        
        String codigoOperacion = "0";
        try
        {
            String idPrep = (String)request.getParameter("idPrep");
            Integer idP = null;
            if(idPrep == null || idPrep.equals(""))
            {
                codigoOperacion = "3";
            }
            else
            {
                try
                {
                    idP = Integer.parseInt(idPrep);
                }
                catch(Exception ex)
                {
                    codigoOperacion = "3";
                }
                if(idP != null)
                {
                    EcaSolPreparadoresVO prep = new EcaSolPreparadoresVO();
                    prep.setSolPreparadoresCod(idP);
                    int result = MeLanbide44Manager.getInstance().eliminarPreparadorSolicitud(prep, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                    if(result <= 0)
                    {
                        codigoOperacion = "1";
                    }
                    else
                    {
                        codigoOperacion = "0";
                    }
                }
                else
                {
                    codigoOperacion = "3";
                }
            }
        }
        catch(Exception ex)
        {
            codigoOperacion = "2";
        }
        if(codigoOperacion.equalsIgnoreCase("0"))
        {
            this.getListaPreparadoresSolicitud(codOrganizacion, codTramite, ocurrenciaTramite, numExpediente, request, response);
        }
        else
        {
            StringBuffer xmlSalida = new StringBuffer();
            xmlSalida.append("<RESPUESTA>");
                xmlSalida.append("<CODIGO_OPERACION>");
                    xmlSalida.append(codigoOperacion);
                xmlSalida.append("</CODIGO_OPERACION>");
            xmlSalida.append("</RESPUESTA>");
            try{
                response.setContentType("text/xml");
                response.setCharacterEncoding("UTF-8");
                PrintWriter out = response.getWriter();
                out.print(xmlSalida.toString());
                out.flush();
                out.close();
            }catch(Exception e){
                log.error(" Error : " + e.getMessage(), e);
            }//try-catch
        }
    }
    
    public String cargarNuevoPreparadorSolicitud(int codOrganizacion,  int codTramite, int ocurrenciaTramite,String numExpediente,HttpServletRequest request,HttpServletResponse response)
    {
        try
        {
            String opcion = (String)request.getParameter("opcion");
            if(opcion != null)
            {
                if(opcion.equalsIgnoreCase("nuevo"))
                {
                    //Cargar nuevo preparador
                }
                else if(opcion.equalsIgnoreCase("modificar") || opcion.equalsIgnoreCase("consultar"))
                {
                    //Cargar modificar preparador
                    String idPrep = (String)request.getParameter("idPrepModif");
                    if(idPrep != null)
                    {
                        EcaSolPreparadoresVO prep = new EcaSolPreparadoresVO();
                        prep.setSolPreparadoresCod(Integer.parseInt(idPrep));
                        prep = MeLanbide44Manager.getInstance().getPreparadorSolicitudPorId(prep, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                        if(prep != null)
                        {
                            request.setAttribute("preparadorModif", prep);
                            EcaSolPreparadoresVO sustituto = MeLanbide44Manager.getInstance().getPreparadorSolicitudSustituto(prep.getSolPreparadoresCod(), this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                            if(sustituto != null)
                            {
                                request.setAttribute("sustituto", sustituto);
                            }
                            
                            if(prep.getSolPreparadorOrigen() != null)
                            {
                                EcaSolPreparadoresVO origen = new EcaSolPreparadoresVO();
                                origen.setSolPreparadoresCod(prep.getSolPreparadorOrigen());
                                origen = MeLanbide44Manager.getInstance().getPreparadorSolicitudPorId(origen, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                                if(origen != null)
                                {
                                    request.setAttribute("preparadorOrigen", origen);
                                }
                            }
                        }
                    }
                    if(opcion.equalsIgnoreCase("consultar"))
                    {
                        request.setAttribute("consulta", true);
                    }
                }
            }
        }
        catch(Exception ex)
        {
            
        }   
        return "/jsp/extension/melanbide44/solicitud/nuevoPreparadorSolicitud.jsp";
    }
    
    public void guardarPreparadorSolicitud(int codOrganizacion,  int codTramite, int ocurrenciaTramite,String numExpediente,HttpServletRequest request,HttpServletResponse response)
    {
        String codigoOperacion = "0";
        List<String> validaciones = new ArrayList<String>();
        List<FilaPreparadorSolicitudVO> preparadores = new ArrayList<FilaPreparadorSolicitudVO>();
        
        int codIdioma = 1;    
        try
        {
            UsuarioValueObject usuario = (UsuarioValueObject) request.getSession().getAttribute("usuario");
            if(usuario != null)
            {
                codIdioma = usuario.getIdioma();
            }
        }
        catch(Exception ex)
        {

        }
        
        try
        {
            AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
            
            EcaSolicitudVO sol = MeLanbide44Manager.getInstance().getDatosSolicitud(numExpediente, adapt);
            if(sol != null && sol.getSolicitudCod() != null)
            {
                    
                SimpleDateFormat format = new SimpleDateFormat(ConstantesMeLanbide44.FORMATO_FECHA);
                //Recojo los parámetros
                String idPrep = (String)request.getParameter("idPrep");
                String nif = (String)request.getParameter("nif");
                String nomApel = (String)request.getParameter("nomApel");
                String feIni = (String)request.getParameter("feIni");
                String feFin = (String)request.getParameter("feFin");
                String horasAnualesJC = (String)request.getParameter("horasAnualesJC");
                String horasContrato = (String)request.getParameter("horasContrato");
                String horasECA = (String)request.getParameter("horasECA");
                String costesSSJor = (String)request.getParameter("costesSSJor");
                String costesSSPorJor = (String)request.getParameter("costesSSPorJor");
                String costesSSECA = (String)request.getParameter("costesSSECA");
                String segAnt = (String)request.getParameter("segAnt");
                String importe = (String)request.getParameter("importe");
                String c1h = (String)request.getParameter("c1h");
                String c1m = (String)request.getParameter("c1m");
                String c1total = (String)request.getParameter("c1total");
                String c2h = (String)request.getParameter("c2h");
                String c2m = (String)request.getParameter("c2m");
                String c2total = (String)request.getParameter("c2total");
                String c3h = (String)request.getParameter("c3h");
                String c3m = (String)request.getParameter("c3m");
                String c3total = (String)request.getParameter("c3total");
                String c4h = (String)request.getParameter("c4h");
                String c4m = (String)request.getParameter("c4m");
                String c4total = (String)request.getParameter("c4total");
                
                String c5h = (String)request.getParameter("c5h");
                String c5m = (String)request.getParameter("c5m");
                String c5total = (String)request.getParameter("c5total");
                
                String c6h = (String)request.getParameter("c6h");
                String c6m = (String)request.getParameter("c6m");
                String c6total = (String)request.getParameter("c6total");
                String inserciones = (String)request.getParameter("inserciones");
                String segIns = (String)request.getParameter("segIns");
                String costesSS = (String)request.getParameter("costesSS");
                String impteConcedido = (String)request.getParameter("impteConcedido");
                String idPrepOrigen = (String)request.getParameter("idPrepOrigen");

                EcaSolPreparadoresVO prepValida = MeLanbide44Manager.getInstance().getPreparadorSolicitudPorNIF(sol, nif, adapt);
                boolean prepRepetido = false;
                
                if(prepValida != null && idPrep != null)
                {
                    if(!idPrep.equalsIgnoreCase(prepValida.getSolPreparadoresCod().toString()))
                    {
                        prepRepetido = true;
                    }
                }
                if(!prepRepetido)
                {
                    EcaSolPreparadoresVO prep = null;
                    if(idPrep != null && !idPrep.equals(""))
                    {
                        prep = new EcaSolPreparadoresVO();
                        prep.setSolPreparadoresCod(Integer.parseInt(idPrep));
                        prep = MeLanbide44Manager.getInstance().getPreparadorSolicitudPorId(prep, adapt);
                    }
                    else
                    {
                        prep = new EcaSolPreparadoresVO();
                    }
                    if(prep == null)
                    {
                        codigoOperacion = "3";
                    }
                    else
                    {
                        prep.setCoste(costesSS != null && !costesSS.equals("") ? new BigDecimal(costesSS) : null);
                        prep.setImpteConcedido(impteConcedido != null && !impteConcedido.equals("") ? new BigDecimal(impteConcedido) : null);
                        prep.setFecFin(feFin != null && !feFin.equals("") ? format.parse(feFin) : null);
                        prep.setFecIni(feIni != null && !feIni.equals("") ? format.parse(feIni) : null);
                        prep.setHorasCont(horasContrato != null && !horasContrato.equals("") ? new BigDecimal(horasContrato) : null);
                        prep.setHorasEca(horasECA != null && !horasECA.equals("") ? new BigDecimal(horasECA) : null);
                        prep.setHorasJC(horasAnualesJC != null && !horasAnualesJC.equals("") ? new BigDecimal(horasAnualesJC) : null);
                        prep.setImpSSECA(costesSSECA != null && !costesSSECA.equals("") ? new BigDecimal(costesSSECA) : null);
                        prep.setImpSSJC(costesSSJor != null && !costesSSJor.equals("") ? new BigDecimal(costesSSJor) : null);
                        prep.setImpSSJR(costesSSPorJor != null && !costesSSPorJor.equals("") ? new BigDecimal(costesSSPorJor) : null);
                        prep.setImpSegAnt(importe != null && !importe.equals("") ? new BigDecimal(importe) : null);
                        prep.setInsC1(c1total != null && !c1total.equals("") ? new BigDecimal(c1total) : null);
                        prep.setInsC1H(c1h != null && !c1h.equals("") ? new BigDecimal(c1h) : null);
                        prep.setInsC1M(c1m != null && !c1m.equals("") ? new BigDecimal(c1m) : null);
                        prep.setInsC2(c2total != null && !c2total.equals("") ? new BigDecimal(c2total) : null);
                        prep.setInsC2H(c2h != null && !c2h.equals("") ? new BigDecimal(c2h) : null);
                        prep.setInsC2M(c2m != null && !c2m.equals("") ? new BigDecimal(c2m) : null);
                        prep.setInsC3(c3total != null && !c3total.equals("") ? new BigDecimal(c3total) : null);
                        prep.setInsC3H(c3h != null && !c3h.equals("") ? new BigDecimal(c3h) : null);
                        prep.setInsC3M(c3m != null && !c3m.equals("") ? new BigDecimal(c3m) : null);
                        prep.setInsC4(c4total != null && !c4total.equals("") ? new BigDecimal(c4total) : null);
                        prep.setInsC4H(c4h != null && !c4h.equals("") ? new BigDecimal(c4h) : null);
                        prep.setInsC4M(c4m != null && !c4m.equals("") ? new BigDecimal(c4m) : null);
                        
                        prep.setInsC5(c5total != null && !c5total.equals("") ? new BigDecimal(c5total) : null);
                        prep.setInsC5H(c5h != null && !c5h.equals("") ? new BigDecimal(c5h) : null);
                        prep.setInsC5M(c5m != null && !c5m.equals("") ? new BigDecimal(c5m) : null);
                        
                        prep.setInsC6(c6total != null && !c6total.equals("") ? new BigDecimal(c6total) : null);
                        prep.setInsC6H(c6h != null && !c6h.equals("") ? new BigDecimal(c6h) : null);
                        prep.setInsC6M(c6m != null && !c6m.equals("") ? new BigDecimal(c6m) : null);
                        
                        prep.setInsImporte(inserciones != null && !inserciones.equals("") ? new BigDecimal(inserciones) : null);
                        prep.setInsSegImporte(segIns != null && !segIns.equals("") ? new BigDecimal(segIns) : null);
                        prep.setNif(nif != null ? nif.toUpperCase() : null);
                        prep.setNombre(nomApel != null ? nomApel.toUpperCase() : null);
                        prep.setSegAnt(segAnt != null && !segAnt.equals("") ? Integer.parseInt(segAnt) : null);
                        prep.setSolicitud(sol.getSolicitudCod());
                        if(idPrepOrigen != null && !idPrepOrigen.equals(""))
                        {
                            prep.setSolPreparadorOrigen(Integer.parseInt(idPrepOrigen));
                            if(prep.getTipoSust() == null)
                            {
                                prep.setTipoSust(ConstantesMeLanbide44.TIPOS_SUSTITUTO.SUSTITUTO_SOLICITUD);
                            }
                        }

                        EcaConfiguracionVO config = MeLanbide44Manager.getInstance().getConfiguracionEca(MeLanbide44Utils.getEjercicioDeExpediente(numExpediente), adapt);

                        boolean soloFormato = prep.getSolPreparadoresCod() != null;
                        
                        EcaSolPreparadoresVO origen = null;
                        
                        if(prep.getSolPreparadorOrigen() != null)
                        {
                            origen = new EcaSolPreparadoresVO();
                            origen.setSolPreparadoresCod(prep.getSolPreparadorOrigen());
                            origen = MeLanbide44Manager.getInstance().getPreparadorSolicitudPorId(origen, adapt);
                        }
                        
                        List<EcaSolPreparadoresVO> sustitutos = MeLanbide44Manager.getInstance().getSustitutosPreparadorSolicitud(prep.getSolPreparadoresCod(), adapt);

                        validaciones = MeLanbide44Utils.validarEcaSolPreparadoresVO(prep, origen, sustitutos, config, sol.getExpEje(), codIdioma, soloFormato, false);
                        if(validaciones == null || validaciones.size() == 0)
                        {
                            MeLanbide44Manager.getInstance().guardarEcaSolPreparadoresVO(prep, adapt);
                            preparadores = MeLanbide44Manager.getInstance().getListaPreparadoresSolicitud(sol, adapt, codIdioma);
                        }
                        else
                        {
                            codigoOperacion = "4";
                        }
                    }
                }
                else
                {
                    codigoOperacion = "5";
                }
            }
            else
            {
                codigoOperacion = "1";
            }
        }
        catch(Exception ex)
        {
            log.error(" Error : " + ex.getMessage(), ex);
            codigoOperacion = "2";
        }
        
        this.escribirListaPreparadoresRequest(codigoOperacion, preparadores, validaciones, codIdioma, response);
    }
    
    
 
    
    
    
    public String cargarNuevoPreparadorJustificacion(int codOrganizacion,  int codTramite, int ocurrenciaTramite,String numExpediente,HttpServletRequest request,HttpServletResponse response)
    {
        
        int codIdioma = 1;    
        try
        {
            UsuarioValueObject usuario = (UsuarioValueObject) request.getSession().getAttribute("usuario");
            if(usuario != null)
            {
                codIdioma = usuario.getIdioma();
            }
        }
        catch(Exception ex)
        {

        }
        try
        {
            String opcion = (String)request.getParameter("opcion");
            if(opcion != null)
            {
                 List<SelectItem> listaTipoContrato =  MeLanbide44Manager.getInstance().getListaTipoContrato( codIdioma, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                request.setAttribute("lstTipoContrato", listaTipoContrato);
                if(opcion.equalsIgnoreCase("nuevo"))
                {
                    //Cargar nuevo preparador
                    String idPrepS = (String)request.getParameter("idPrepSustituir");
                    if(idPrepS != null)
                    {
                        EcaJusPreparadoresVO prepS = new EcaJusPreparadoresVO();
                        prepS.setJusPreparadoresCod(Integer.parseInt(idPrepS));
                        prepS = MeLanbide44Manager.getInstance().getPreparadorJustificacionPorId(prepS, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                        if(prepS != null)
                        {
                            request.setAttribute("preparadorSustituir", prepS);
                        }
                    }
                }
                else if(opcion.equalsIgnoreCase("modificar") || opcion.equalsIgnoreCase("consultar"))
                {
                    //Cargar modificar preparador
                    String idPrep = (String)request.getParameter("idPrepModificar");
                    if(idPrep != null)
                    {
                        EcaJusPreparadoresVO prep = new EcaJusPreparadoresVO();
                        prep.setJusPreparadoresCod(Integer.parseInt(idPrep));
                        prep = MeLanbide44Manager.getInstance().getPreparadorJustificacionPorId(prep, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                        if(prep != null)
                        {
                            log.debug(prep.getHorasCont() != null ? prep.getHorasCont().toPlainString() : "");
                            request.setAttribute("preparadorModif", prep);
                            EcaJusPreparadoresVO sustituto = new EcaJusPreparadoresVO();
                            sustituto = MeLanbide44Manager.getInstance().getPreparadorJustificacionSustituto(prep.getJusPreparadoresCod(), this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                            if(sustituto != null)
                            {
                                request.setAttribute("sustituto", sustituto);
                            }
                        }
                        
                        if (!prep.getJusPreparadorOrigen().equals("")){
                            EcaJusPreparadoresVO prepS = new EcaJusPreparadoresVO();
                            prepS.setJusPreparadoresCod(prep.getJusPreparadorOrigen());
                            prepS = MeLanbide44Manager.getInstance().getPreparadorJustificacionPorId(prepS, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                            if(prepS != null)
                            {
                                request.setAttribute("preparadorSustituir", prepS);
                            }
                        }
                        
                        
                    }
                    if(opcion.equalsIgnoreCase("consultar"))
                    {
                        request.setAttribute("consulta", true);
                    }
                }
                
               
               
            }
        }
        catch(Exception ex)
        {
            
        }   
        return "/jsp/extension/melanbide44/justificacion/nuevoPreparadorJustificacion.jsp";
    }
    
    public String procesarExcelProspectores(int codOrganizacion,  int codTramite, int ocurrenciaTramite,String numExpediente,HttpServletRequest request,HttpServletResponse response)
    {
        int codIdioma = 1;    
        try
        {
            UsuarioValueObject usuario = (UsuarioValueObject) request.getSession().getAttribute("usuario");
            if(usuario != null)
            {
                codIdioma = usuario.getIdioma();
            }
        }
        catch(Exception ex)
        {

        }
        try
        {
            
            MultipartRequestWrapper wrapper = new MultipartRequestWrapper(request);
            
            wrapper.getParameterMap();
            CommonsMultipartRequestHandler handler = new CommonsMultipartRequestHandler();
            handler.handleRequest(request);
            Hashtable<String, FormFile> table = handler.getFileElements();
            FormFile file = table.get("fichero_prospectores_sol");
            
            byte[] data = file.getFileData();
            ByteArrayInputStream bais = new ByteArrayInputStream(data);
            HSSFWorkbook workbook = new HSSFWorkbook(bais);
            
            HSSFSheet sheet = workbook.getSheetAt(0);
            
            List<String> nifsRepetidos = this.hayNifsRepetidos(sheet, 0);
            
            if(nifsRepetidos == null || nifsRepetidos.size() == 0)
            {
                List<String> sustitutosDeSustitutos = this.haySustitutosDeSustitutos(sheet, 0, 13);
                if(sustitutosDeSustitutos == null || sustitutosDeSustitutos.isEmpty())
                {
                    int filaIni = sheet.getFirstRowNum();
                    int filaFin = sheet.getLastRowNum();

                    int i = 0;

                    HSSFRow row = null;
                    List<EcaSolProspectoresVO> filasImportar = new ArrayList<EcaSolProspectoresVO>();
                    EcaSolProspectoresVO prep = null;
                    try
                    {
                        AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
                        EcaConfiguracionVO config = MeLanbide44Manager.getInstance().getConfiguracionEca(MeLanbide44Utils.getEjercicioDeExpediente(numExpediente), adapt);
                        for(i = filaIni+1; i <= filaFin; i++)
                        {
                            row = sheet.getRow(i);
                            prep = (EcaSolProspectoresVO)MeLanbide44MappingUtils.getInstance().map(row, EcaSolProspectoresVO.class);
                            if(prep != null)
                            {
                                //los datos se guardan en campos carga y modificables
                                prep.setNif_Carga(prep.getNif());
                                prep.setNombre_Carga(prep.getNombre());
                                prep.setFecIni_Carga(prep.getFecIni());
                                prep.setFecFin_Carga(prep.getFecFin());
                                prep.setHorasCont_Carga(prep.getHorasCont());
                                prep.setHorasJC_Carga(prep.getHorasJC());
                                prep.setHorasEca_Carga(prep.getHorasEca());
                                prep.setImpSSJC_Carga(prep.getImpSSJC());
                                prep.setImpSSJR_Carga(prep.getImpSSJR());
                                prep.setImpSSECA_Carga(prep.getImpSSECA());
                                prep.setVisitas_Carga(prep.getVisitas());
                                prep.setVisitasImp_Carga(prep.getVisitasImp());                            
                                prep.setCoste_Carga(prep.getCoste());

                                //datos calculados   
                                BigDecimal importeCalcVis = null;
                                if(prep.getVisitas() != null)
                                {
                                    importeCalcVis = new BigDecimal("0.00");
                                    importeCalcVis = new BigDecimal(prep.getVisitas().toString()).multiply(
                                        config!= null && config.getImpVisita() != null ?config.getImpVisita() : new BigDecimal("0"));

                                    if(importeCalcVis != null && !MeLanbide44Utils.validarDatoExcel(importeCalcVis.toPlainString(), ConstantesMeLanbide44.TiposRequeridos.NUMERICO_DECIMAL, -1, 8, 2, true))
                                    {
                                        ExcelRowMappingException erme = new ExcelRowMappingException("12");
                                        erme.setLongMax(-1);
                                        erme.setTipo(ConstantesMeLanbide44.TiposRequeridos.NUMERICO_DECIMAL);
                                        erme.setpEntera(8);
                                        erme.setpDecimal(2);
                                        erme.setMensaje(String.format(MeLanbide44I18n.getInstance().getMensaje(codIdioma, "validaciones.solicitud.prospectores.impVisitasDemasiadoGrande"), i+1));
                                        throw erme;
                                    }
                                    else
                                    {
                                        prep.setVisitasImp(importeCalcVis);
                                    }
                                }

                                BigDecimal costesSalSSEca = null;
                                if(prep.getImpSSJC() != null && prep.getHorasEca()!=null && prep.getHorasJC() != null)
                                {
                                    costesSalSSEca = new BigDecimal("0.00");
                                    costesSalSSEca = prep.getImpSSJC().multiply(prep.getHorasEca());
                                    costesSalSSEca = !prep.getHorasJC().equals(BigDecimal.ZERO) ? costesSalSSEca.divide(prep.getHorasJC(),2, BigDecimal.ROUND_HALF_UP) : BigDecimal.ZERO;  
                                }
                                if(costesSalSSEca != null && !MeLanbide44Utils.validarDatoExcel(costesSalSSEca.toPlainString(), ConstantesMeLanbide44.TiposRequeridos.NUMERICO_DECIMAL, -1, 8, 2, true))
                                {
                                    ExcelRowMappingException erme = new ExcelRowMappingException("12");
                                    erme.setLongMax(-1);
                                    erme.setTipo(ConstantesMeLanbide44.TiposRequeridos.NUMERICO_DECIMAL);
                                    erme.setpEntera(8);
                                    erme.setpDecimal(2);
                                    erme.setMensaje(String.format(MeLanbide44I18n.getInstance().getMensaje(codIdioma, "validaciones.solicitud.prospectores.impSSEcaDemasiadoGrande"), i+1));
                                    throw erme;
                                }
                                else
                                {
                                    prep.setImpSSECA(costesSalSSEca);
                                }



                                BigDecimal ImpSSJR = null;
                                if(prep.getImpSSJC()!=null && prep.getHorasCont()!=null && prep.getHorasJC() != null)
                                {
                                    ImpSSJR = new BigDecimal("0.00");
                                    ImpSSJR = prep.getImpSSJC().multiply(prep.getHorasCont());
                                    ImpSSJR = !prep.getHorasJC().equals(BigDecimal.ZERO) ? ImpSSJR.divide(prep.getHorasJC(),2, BigDecimal.ROUND_HALF_UP) : BigDecimal.ZERO; 
                                }
                                if(ImpSSJR != null && !MeLanbide44Utils.validarDatoExcel(ImpSSJR.toPlainString(), ConstantesMeLanbide44.TiposRequeridos.NUMERICO_DECIMAL, -1, 8, 2, true))
                                {
                                    ExcelRowMappingException erme = new ExcelRowMappingException("12");
                                    erme.setLongMax(-1);
                                    erme.setTipo(ConstantesMeLanbide44.TiposRequeridos.NUMERICO_DECIMAL);
                                    erme.setpEntera(8);
                                    erme.setpDecimal(2);
                                    erme.setMensaje(String.format(MeLanbide44I18n.getInstance().getMensaje(codIdioma, "validaciones.solicitud.prospectores.impSSJRDemasiadoGrande"), i+1));
                                    throw erme;
                                }
                                else
                                {
                                    prep.setImpSSJR(ImpSSJR);
                                }


                                if(importeCalcVis != null && costesSalSSEca != null)
                                {
                                    prep.setCoste(costesSalSSEca.compareTo(importeCalcVis)<0?costesSalSSEca:importeCalcVis);
                                }
                                else if(importeCalcVis != null)
                                {
                                    prep.setCoste(importeCalcVis);   
                                }
                                else if(costesSalSSEca != null)
                                {
                                    prep.setCoste(costesSalSSEca);
                                }
                                else
                                {
                                    prep.setCoste(null);
                                }

                                if(prep.getCoste() != null && !MeLanbide44Utils.validarDatoExcel(prep.getCoste().toPlainString(), ConstantesMeLanbide44.TiposRequeridos.NUMERICO_DECIMAL, -1, 8, 2, true))
                                {
                                    ExcelRowMappingException erme = new ExcelRowMappingException("12");
                                    erme.setLongMax(-1);
                                    erme.setTipo(ConstantesMeLanbide44.TiposRequeridos.NUMERICO_DECIMAL);
                                    erme.setpEntera(8);
                                    erme.setpDecimal(2);
                                    erme.setMensaje(String.format(MeLanbide44I18n.getInstance().getMensaje(codIdioma, "validaciones.solicitud.prospectores.costeDemasiadoGrande"), i+1));
                                    throw erme;
                                }

                                if(prep.getVisitasImp_Carga() == null)
                                {
                                    prep.setVisitasImp_Carga(prep.getVisitasImp());
                                }

                                if(prep.getImpSSECA_Carga() == null)
                                {
                                    prep.setImpSSECA_Carga(prep.getImpSSECA());
                                }

                                if(prep.getImpSSJC_Carga() == null)
                                {
                                    prep.setImpSSJC_Carga(prep.getImpSSJR());
                                }

                                if(prep.getCoste_Carga() == null)
                                {
                                    prep.setCoste_Carga(prep.getCoste());
                                }

                                filasImportar.add(prep);
                            }
                        }
                        if(filasImportar != null && filasImportar.size() > 0)
                        {
                            Collections.sort(filasImportar);
                            int filasImportadas = MeLanbide44Manager.getInstance().importarProspectores(numExpediente, filasImportar, adapt);


                            if(filasImportadas < filasImportar.size())
                            {
                                request.getSession().setAttribute("mensajeImportar", MeLanbide44I18n.getInstance().getMensaje(codIdioma, "error.errorImportarGen"));
                            }
                            else
                            {
                                request.getSession().setAttribute("mensajeImportar", MeLanbide44I18n.getInstance().getMensaje(codIdioma, "msg.registrosImportadosOK_1")+" "+filasImportadas+" "+MeLanbide44I18n.getInstance().getMensaje(codIdioma, "msg.registrosImportadosOK_2"));
                            }
                        }
                        else
                        {
                            request.getSession().setAttribute("mensajeImportar", MeLanbide44I18n.getInstance().getMensaje(codIdioma, "error.noHayRegistrosImportar"));
                        }
                    }
                    catch(ExcelRowMappingException erme)
                    {
                        String mensajeImportar = null;
                        if(erme.getMensaje() != null && !erme.getMensaje().equals(""))
                        {
                            mensajeImportar = erme.getMensaje();
                        }
                        else
                        {
                            mensajeImportar = MeLanbide44Utils.crearMensajeDatoExcelNoValido(erme, i+1, codIdioma);
                        }
                        request.getSession().setAttribute("mensajeImportar", mensajeImportar);   
                    }
                }
                else
                {
                    String mensajeImportar = MeLanbide44I18n.getInstance().getMensaje(codIdioma, "error.sustitutosDeSustitutos")+"<br/>";
                    for(String nif : sustitutosDeSustitutos)
                    {
                        mensajeImportar += "<br/>"+nif.toUpperCase();
                    }
                    request.getSession().setAttribute("mensajeImportar", mensajeImportar);
                }
            }
            else
            {
                String mensajeImportar = MeLanbide44I18n.getInstance().getMensaje(codIdioma, "error.nifsRepetidosExcel_1")+"<br/>";
                for(String nif : nifsRepetidos)
                {
                    mensajeImportar += "<br/>"+nif.toUpperCase();
                }
                mensajeImportar += "<br/><br/>"+MeLanbide44I18n.getInstance().getMensaje(codIdioma, "error.nifsRepetidosExcel_2");
                request.getSession().setAttribute("mensajeImportar", mensajeImportar);
            }
        }
        catch(FilaProspectorSolicitudNoValidaException ex)
        {
            log.error(" Error : " + ex.getMessage(), ex);
            request.getSession().setAttribute("mensajeImportar", MeLanbide44I18n.getInstance().getMensaje(codIdioma, "error.datosNoValidosImportar"));
        }
        catch(IOException ex)
        {
            log.error(" Error : " + ex.getMessage(), ex);
            request.getSession().setAttribute("mensajeImportar", MeLanbide44I18n.getInstance().getMensaje(codIdioma, "error.errorProcesarExcel"));
        }
        catch(Exception ex)
        {
            log.error(" Error : " + ex.getMessage(), ex);
            request.getSession().setAttribute("mensajeImportar", MeLanbide44I18n.getInstance().getMensaje(codIdioma, "error.errorImportarGen"));
        }
        return "/jsp/extension/melanbide44/solicitud/recargarListaProspectoresSolic.jsp";
    }
    
    public void getListaProspectoresSolicitud(int codOrganizacion,  int codTramite, int ocurrenciaTramite,String numExpediente,HttpServletRequest request,HttpServletResponse response)
    {
        try
        {
            try
            {
                //Al importar un fichero, se inserta este mensaje en sesion como resultado
                //Justo despues de importar se llama a este metodo para recargar la tabla.
                //Este es el punto donde se borra este mensaje de sesion, para no mantenerlo en memoria innecesariamente.
                request.getSession().removeAttribute("mensajeImportar");
            }
            catch(Exception ex)
            {
                
            }
        
            int codIdioma = 1;    
            try
            {
                UsuarioValueObject usuario = (UsuarioValueObject) request.getSession().getAttribute("usuario");
                if(usuario != null)
                {
                    codIdioma = usuario.getIdioma();
                }
            }
            catch(Exception ex)
            {

            }
            
            AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
            MeLanbide44Manager meLanbide44Manager = MeLanbide44Manager.getInstance();
            EcaSolicitudVO sol = meLanbide44Manager.getDatosSolicitud(numExpediente, adapt);
            List<FilaProspectorSolicitudVO> prospectores = null;
            
            if(sol != null)
            {
                prospectores = MeLanbide44Manager.getInstance().getListaProspectoresSolicitud(sol, adapt, codIdioma);
            }
            else
            {
                prospectores = new ArrayList<FilaProspectorSolicitudVO>();
            }
            
            String codigoOperacion = "0";
            
            this.escribirListaProspectoresRequest(codigoOperacion, prospectores, null, codIdioma, response);
            
        }
        catch(Exception ex)
        {
            log.error(" Error : " + ex.getMessage(), ex);
        }
    }
    
    public String cargarNuevoProspectorSolicitud(int codOrganizacion,  int codTramite, int ocurrenciaTramite,String numExpediente,HttpServletRequest request,HttpServletResponse response)
    {
        try
        {
            String opcion = (String)request.getParameter("opcion");
            if(opcion != null)
            {
                if(opcion.equalsIgnoreCase("nuevo"))
                {
                    //Cargar nuevo preparador
                }
                else if(opcion.equalsIgnoreCase("modificar") || opcion.equalsIgnoreCase("consultar"))
                {
                    //Cargar modificar preparador
                    String idPros = (String)request.getParameter("idProsModif");
                    if(idPros != null)
                    {
                        EcaSolProspectoresVO pros = new EcaSolProspectoresVO();
                        pros.setSolProspectoresCod(Integer.parseInt(idPros));
                        pros = MeLanbide44Manager.getInstance().getProspectorSolicitudPorId(pros, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                        if(pros != null)
                        {
                            request.setAttribute("prospectorModif", pros);    
                            EcaSolProspectoresVO sustituto = new EcaSolProspectoresVO();
                            sustituto = MeLanbide44Manager.getInstance().getProspectorSolicitudSustituto(pros.getSolProspectoresCod(), this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                            if(sustituto != null)
                            {
                                request.setAttribute("sustitutopros", sustituto);
                            }  
                        
                            if (pros.getSolProspectorOrigen()!= null && !pros.getSolProspectorOrigen().equals("")){
                                EcaSolProspectoresVO prosS = new EcaSolProspectoresVO();
                                prosS.setSolProspectoresCod(pros.getSolProspectorOrigen());
                                prosS = MeLanbide44Manager.getInstance().getProspectorSolicitudPorId(prosS, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                                if(prosS != null)
                                {
                                    request.setAttribute("prospectorOrigen", prosS);
                                }
                            }
                                                  
                        }
                    }
                    if(opcion.equalsIgnoreCase("consultar"))
                    {
                        request.setAttribute("consulta", true);
                    }
                }
            }
        }
        catch(Exception ex)
        {
            
        }   
        return "/jsp/extension/melanbide44/solicitud/nuevoProspectorSolicitud.jsp";
    }
    
    public void guardarProspectorSolicitud(int codOrganizacion,  int codTramite, int ocurrenciaTramite,String numExpediente,HttpServletRequest request,HttpServletResponse response)
    {
        String codigoOperacion = "0";
        List<String> validaciones = new ArrayList<String>();
        List<FilaProspectorSolicitudVO> prospectores = new ArrayList<FilaProspectorSolicitudVO>();
        
        int codIdioma = 1;    
        try
        {
            UsuarioValueObject usuario = (UsuarioValueObject) request.getSession().getAttribute("usuario");
            if(usuario != null)
            {
                codIdioma = usuario.getIdioma();
            }
        }
        catch(Exception ex)
        {

        }
        
        try
        {
            AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
            
            EcaSolicitudVO sol = MeLanbide44Manager.getInstance().getDatosSolicitud(numExpediente, adapt);
            if(sol != null && sol.getSolicitudCod() != null)
            {
                    
                SimpleDateFormat format = new SimpleDateFormat(ConstantesMeLanbide44.FORMATO_FECHA);
                //Recojo los parámetros
                String idPros = (String)request.getParameter("idPros");
                String nif = (String)request.getParameter("nif");
                String nomApel = (String)request.getParameter("nomApel");
                String feIni = (String)request.getParameter("feIni");
                String feFin = (String)request.getParameter("feFin");
                String horasAnualesJC = (String)request.getParameter("horasAnualesJC");
                String horasContrato = (String)request.getParameter("horasContrato");
                String horasECA = (String)request.getParameter("horasECA");
                String costesSSJor = (String)request.getParameter("costesSSJor");
                String costesSSPorJor = (String)request.getParameter("costesSSPorJor");
                String costesSSECA = (String)request.getParameter("costesSSECA");
                String visitas = (String)request.getParameter("visitas");
                String visitasImp = (String)request.getParameter("visitasImp");
                String coste = (String)request.getParameter("coste");
                String impteConcedido = (String)request.getParameter("impteConcedido");
                String idProsOrigen = (String)request.getParameter("idProsOrigen");

                EcaSolProspectoresVO prosValida = MeLanbide44Manager.getInstance().getProspectorSolicitudPorNIF(sol, nif, adapt);
                boolean prosRepetido = false;
                
                if(prosValida != null && idPros != null)
                {
                    if(!idPros.equalsIgnoreCase(prosValida.getSolProspectoresCod().toString()))
                    {
                        prosRepetido = true;
                    }
                }
                if(!prosRepetido)
                {
                    EcaSolProspectoresVO pros = null;
                    if(idPros != null && !idPros.equals(""))
                    {
                        pros = new EcaSolProspectoresVO();
                        pros.setSolProspectoresCod(Integer.parseInt(idPros));
                        pros = MeLanbide44Manager.getInstance().getProspectorSolicitudPorId(pros, adapt);
                    }
                    else
                    {
                        pros = new EcaSolProspectoresVO();
                    }
                    if(pros == null)
                    {
                        codigoOperacion = "3";
                    }
                    else
                    {
                        pros.setCoste(coste != null && !coste.equals("") ? new BigDecimal(coste.replaceAll(",", "\\.")) : null);
                        pros.setImpteConcedido(impteConcedido != null && !impteConcedido.equals("") ? new BigDecimal(impteConcedido.replaceAll(",", "\\.")) : null);
                        pros.setFecFin(feFin != null && !feFin.equals("") ? format.parse(feFin) : null);
                        pros.setFecIni(feIni != null && !feIni.equals("") ? format.parse(feIni) : null);
                        pros.setHorasCont(horasContrato != null && !horasContrato.equals("") ? new BigDecimal(horasContrato.replaceAll(",", "\\.")) : null);
                        pros.setHorasEca(horasECA != null && !horasECA.equals("") ? new BigDecimal(horasECA.replaceAll(",", "\\.")) : null);
                        pros.setHorasJC(horasAnualesJC != null && !horasAnualesJC.equals("") ? new BigDecimal(horasAnualesJC.replaceAll(",", "\\.")) : null);
                        pros.setImpSSECA(costesSSECA != null && !costesSSECA.equals("") ? new BigDecimal(costesSSECA.replaceAll(",", "\\.")) : null);
                        pros.setImpSSJC(costesSSJor != null && !costesSSJor.equals("") ? new BigDecimal(costesSSJor.replaceAll(",", "\\.")) : null);
                        pros.setImpSSJR(costesSSPorJor != null && !costesSSPorJor.equals("") ? new BigDecimal(costesSSPorJor.replaceAll(",", "\\.")) : null);
                        pros.setNif(nif != null ? nif.toUpperCase() : null);
                        pros.setNombre(nomApel != null ? nomApel.toUpperCase() : null);
                        pros.setSolicitud(sol.getSolicitudCod());
                        pros.setVisitas(visitas != null && !visitas.equals("") ? Integer.parseInt(visitas) : null);
                        pros.setVisitasImp(visitasImp != null && !visitasImp.equals("") ? new BigDecimal(visitasImp.replaceAll(",", "\\.")) : null);
                        if(idProsOrigen != null && !idProsOrigen.equals(""))
                        {
                            pros.setSolProspectorOrigen(Integer.parseInt(idProsOrigen));
                            pros.setTipoSust(ConstantesMeLanbide44.TIPOS_SUSTITUTO.SUSTITUTO_SOLICITUD);
                        }                        
                        EcaConfiguracionVO config = MeLanbide44Manager.getInstance().getConfiguracionEca(MeLanbide44Utils.getEjercicioDeExpediente(numExpediente), adapt);

                        boolean soloFormato = pros.getSolProspectoresCod() != null;
                        
                        EcaSolProspectoresVO origen = null;
                        
                        if(pros.getSolProspectorOrigen() != null)
                        {
                            origen = new EcaSolProspectoresVO();
                            origen.setSolProspectoresCod(pros.getSolProspectorOrigen());
                            origen = MeLanbide44Manager.getInstance().getProspectorSolicitudPorId(origen, adapt);
                        }
                        
                        List<EcaSolProspectoresVO> sustitutos = MeLanbide44Manager.getInstance().getSustitutosProspectorSolicitud(pros.getSolProspectoresCod(), adapt);

                        validaciones = MeLanbide44Utils.validarEcaSolProspectoresVO(pros, origen, sustitutos, config, sol.getExpEje(), codIdioma, soloFormato, false);
                        if(validaciones == null || validaciones.size() == 0)
                        {
                            MeLanbide44Manager.getInstance().guardarEcaSolProspectoresVO(pros, adapt);
                            prospectores = MeLanbide44Manager.getInstance().getListaProspectoresSolicitud(sol, adapt, codIdioma);
                        }
                        else
                        {
                            codigoOperacion = "4";
                        }
                    }
                }
                else
                {
                    codigoOperacion = "5";
                }
            }
            else
            {
                codigoOperacion = "1";
            }
        }
        catch(Exception ex)
        {
            log.error(" Error : " + ex.getMessage(), ex);
            codigoOperacion = "2";
        }
        
        this.escribirListaProspectoresRequest(codigoOperacion, prospectores, validaciones, codIdioma, response);
    }
    
    public void eliminarProspectorSolicitud(int codOrganizacion,  int codTramite, int ocurrenciaTramite,String numExpediente,HttpServletRequest request,HttpServletResponse response)
    {
        
        String codigoOperacion = "0";
        try
        {
            String idPros = (String)request.getParameter("idPros");
            Integer idP = null;
            if(idPros == null || idPros.equals(""))
            {
                codigoOperacion = "3";
            }
            else
            {
                try
                {
                    idP = Integer.parseInt(idPros);
                }
                catch(Exception ex)
                {
                    codigoOperacion = "3";
                }
                if(idP != null)
                {
                    EcaSolProspectoresVO pros = new EcaSolProspectoresVO();
                    pros.setSolProspectoresCod(idP);
                    int result = MeLanbide44Manager.getInstance().eliminarProspectorSolicitud(pros, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                    if(result <= 0)
                    {
                        codigoOperacion = "1";
                    }
                    else
                    {
                        codigoOperacion = "0";
                    }
                }
                else
                {
                    codigoOperacion = "3";
                }
            }
        }
        catch(Exception ex)
        {
            codigoOperacion = "2";
        }
        if(codigoOperacion.equalsIgnoreCase("0"))
        {
            this.getListaProspectoresSolicitud(codOrganizacion, codTramite, ocurrenciaTramite, numExpediente, request, response);
        }
        else
        {
            StringBuffer xmlSalida = new StringBuffer();
            xmlSalida.append("<RESPUESTA>");
                xmlSalida.append("<CODIGO_OPERACION>");
                    xmlSalida.append(codigoOperacion);
                xmlSalida.append("</CODIGO_OPERACION>");
            xmlSalida.append("</RESPUESTA>");
            try{
                response.setContentType("text/xml");
                response.setCharacterEncoding("UTF-8");
                PrintWriter out = response.getWriter();
                out.print(xmlSalida.toString());
                out.flush();
                out.close();
            }catch(Exception e){
                log.error(" Error : " + e.getMessage(), e);
            }//try-catch
        }
    }
    
    public void guardarDatosValoracionSolicitud(int codOrganizacion,  int codTramite, int ocurrenciaTramite,String numExpediente,HttpServletRequest request,HttpServletResponse response)
    {
        
        String codigoOperacion = "0";
        try
        {
            AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
            try
            {
                String numProyectosExp = (String)request.getParameter("numProyectosExp");
                String puntuacionExp = (String)request.getParameter("puntuacionExp");
                String porInsMujeres = (String)request.getParameter("porInsMujeres");
                String puntuacionInsMujeres = (String)request.getParameter("puntuacionInsMujeres");
                String porSensEmpresarial = (String)request.getParameter("numProyectosSensEmpresarial");
                String puntuacionSensEmpresarial = (String)request.getParameter("puntuacionSensEmpresarial");
                String totValoracionSol = (String)request.getParameter("totValoracionSol");
                
                EcaSolicitudVO sol = MeLanbide44Manager.getInstance().getDatosSolicitud(numExpediente, adapt);
                if(sol != null)
                {
                    EcaSolValoracionVO valoracion = MeLanbide44Manager.getInstance().getValoracionSolicitud(sol, adapt);
                    if(valoracion == null)
                    {
                        valoracion = new EcaSolValoracionVO();
                    }
                    valoracion.setSolicitud(sol.getSolicitudCod());
                    valoracion.setExperienciaNum(numProyectosExp != null && !numProyectosExp.equals("") ? Integer.parseInt(numProyectosExp) : null);
                    valoracion.setExperienciaVal(puntuacionExp != null && !puntuacionExp.equals("") ? new BigDecimal(puntuacionExp.replaceAll(",", "\\.")) : null);
                    valoracion.setInsMujeresNum(porInsMujeres != null && !porInsMujeres.equals("") ? new BigDecimal(porInsMujeres.replaceAll(",", "\\.")) : null);
                    valoracion.setInsMujeresVal(puntuacionInsMujeres != null && !puntuacionInsMujeres.equals("") ? new BigDecimal(puntuacionInsMujeres.replaceAll(",", "\\.")) : null);
                    valoracion.setSensibilidadNum(porSensEmpresarial != null && !porSensEmpresarial.equals("") ? Integer.parseInt(porSensEmpresarial) : null);
                    valoracion.setSensibilidadVal(puntuacionSensEmpresarial != null && !puntuacionSensEmpresarial.equals("") ? new BigDecimal(puntuacionSensEmpresarial.replaceAll(",", "\\.")) : null);
                    valoracion.setTotal(totValoracionSol != null && !totValoracionSol.equals("") ? new BigDecimal(totValoracionSol.replaceAll(",", "\\.")) : null);
                    
                    valoracion = MeLanbide44Manager.getInstance().guardarDatosValoracionSolicitud(valoracion, adapt);
                    if(valoracion != null)
                    {
                        codigoOperacion = "0";
                    }
                    else
                    {
                        codigoOperacion = "1";
                    }
                }
            }
            catch(Exception ex)
            {
                codigoOperacion = "2";
                java.util.logging.Logger.getLogger(MELANBIDE44.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        catch(Exception ex)
        {
            codigoOperacion = "2";
        }
        
        StringBuffer xmlSalida = new StringBuffer();
        xmlSalida.append("<RESPUESTA>");
            xmlSalida.append("<CODIGO_OPERACION>");
                xmlSalida.append(codigoOperacion);
            xmlSalida.append("</CODIGO_OPERACION>");
        xmlSalida.append("</RESPUESTA>");
        try{
            response.setContentType("text/xml");
            response.setCharacterEncoding("UTF-8");
            PrintWriter out = response.getWriter();
            out.print(xmlSalida.toString());
            out.flush();
            out.close();
        }catch(Exception e){
            log.error(" Error : " + e.getMessage(), e);
        }//try-catch
    }
    
    private String cargarSubpestanaSolicitud_Solicitud(EcaSolicitudVO sol, AdaptadorSQLBD adapt, HttpServletRequest request)
    {
                     
        if(sol != null)
        {
            try
            {
            int ano = MeLanbide44Utils.getEjercicioDeExpediente(sol.getNumExp());
            EcaConfiguracionVO config = MeLanbide44Manager.getInstance().getConfiguracionEca(ano, adapt);
            request.setAttribute("ecaConfiguracion", config);
            }catch(Exception ex)
                {
                }   
            
            try
            {
                //Columna de medio en subpestaña solicitud (datos detalle anexos)
                DatosAnexosVO datosAnexos = MeLanbide44Manager.getInstance().getDatosSolicitudAnexos(sol, adapt);
                if(datosAnexos != null)
                {
                    request.setAttribute("datosAnexos", datosAnexos);
                }
                
                //Columna de la derecha en subpestaña solicitud (concedido) Hasta ahora coincidía
                //La columna derecha se relelna con datos de la solicitud
                
                
                /*
                //Columna central en subpestaña solicitud (carga automática)
                DatosAnexosVO datosCarga = MeLanbide44Manager.getInstance().getDatosSolicitudCarga(sol, adapt);
                if(datosCarga != null)
                {
                    request.setAttribute("datosCarga", datosCarga);
                }*/
                
                int codIdioma = 1;    
                try
                {
                    UsuarioValueObject usuario = (UsuarioValueObject) request.getSession().getAttribute("usuario");
                    if(usuario != null)
                    {
                        codIdioma = usuario.getIdioma();
                    }
                }
                catch(Exception ex)
                {
                }
                List<FilaPreparadorSolicitudVO> listaPreparadoresSolicitud = MeLanbide44Manager.getInstance().getListaPreparadoresSolicitud(sol, adapt, codIdioma);
                if(listaPreparadoresSolicitud != null)
                {
                    request.setAttribute("listaPreparadoresSolicitud", listaPreparadoresSolicitud);
                    
                    
                    restarDatosPreparadoresConErrores(listaPreparadoresSolicitud, datosAnexos, adapt);
                }
                
                List<FilaProspectorSolicitudVO> listaProspectoresSolicitud = MeLanbide44Manager.getInstance().getListaProspectoresSolicitud(sol, adapt, codIdioma);
                if(listaProspectoresSolicitud != null)
                {
                    request.setAttribute("listaProspectoresSolicitud", listaProspectoresSolicitud);
                }
            }
            catch(Exception ex)
            {
                log.error(" Error : " + ex.getMessage(), ex);
            }
        }
        return "/jsp/extension/melanbide44/solicitud/solicitud.jsp";
    }
    
    private String cargarSubpestanaPreparadores_Solicitud(EcaSolicitudVO sol, AdaptadorSQLBD adapt, HttpServletRequest request)
    {
        if(sol != null)
        {
            int codIdioma = 1;    
            try
            {
                UsuarioValueObject usuario = (UsuarioValueObject) request.getSession().getAttribute("usuario");
                if(usuario != null)
                {
                    codIdioma = usuario.getIdioma();
                }
            }
            catch(Exception ex)
            {

            }
            
            try
            {
                List<FilaPreparadorSolicitudVO> listaPreparadoresSolicitud = MeLanbide44Manager.getInstance().getListaPreparadoresSolicitud(sol, adapt, codIdioma);
                if(listaPreparadoresSolicitud != null)
                {
                    request.setAttribute("listaPreparadoresSolicitud", listaPreparadoresSolicitud);
                }
            }
            catch(Exception ex)
            {
                log.error(ex.getMessage());
            }
        }
        return "/jsp/extension/melanbide44/solicitud/preparadores.jsp";
    }
    
    private String cargarSubpestanaProspectores_Solicitud(EcaSolicitudVO sol, AdaptadorSQLBD adapt, HttpServletRequest request)
    {
        if(sol != null)
        {
        
            int codIdioma = 1;    
            try
            {
                UsuarioValueObject usuario = (UsuarioValueObject) request.getSession().getAttribute("usuario");
                if(usuario != null)
                {
                    codIdioma = usuario.getIdioma();
                }
            }
            catch(Exception ex)
            {

            }
            
            try
            {
                List<FilaProspectorSolicitudVO> listaProspectoresSolicitud = MeLanbide44Manager.getInstance().getListaProspectoresSolicitud(sol, adapt, codIdioma);
                if(listaProspectoresSolicitud != null)
                {
                    request.setAttribute("listaProspectoresSolicitud", listaProspectoresSolicitud);
                }
            }
            catch(Exception ex)
            {
                log.error(ex.getMessage());
            }
        }
        return "/jsp/extension/melanbide44/solicitud/prospectores.jsp";
    }
    
    private String cargarSubpestanaValoracion_Solicitud(EcaSolicitudVO sol, AdaptadorSQLBD adapt, HttpServletRequest request)
    {
        if(sol != null)
        {
            try
            {
                EcaSolValoracionVO valoracion = MeLanbide44Manager.getInstance().getValoracionSolicitud(sol, adapt);
                if(valoracion != null)
                {
                    request.setAttribute("valoracionSolicitud", valoracion);
                }
            }
            catch(Exception ex)
            {
                log.error(ex.getMessage());
            }
        }
        return "/jsp/extension/melanbide44/solicitud/valoracion.jsp";
    }
    
    private void escribirListaPreparadoresRequest(String codigoOperacion, List<FilaPreparadorSolicitudVO> preparadores, List<String> validaciones, int codigoIdioma, HttpServletResponse response)
    {
        StringBuffer xmlSalida = new StringBuffer();
            xmlSalida.append("<RESPUESTA>");
                xmlSalida.append("<CODIGO_OPERACION>");
                    xmlSalida.append(codigoOperacion);
                xmlSalida.append("</CODIGO_OPERACION>");
                int i = 0;
                for(FilaPreparadorSolicitudVO prep : preparadores)
                {
                    xmlSalida.append("<FILA>");
                        xmlSalida.append("<ID>");
                            xmlSalida.append("<VALOR>");
                                xmlSalida.append(prep.getId());
                            xmlSalida.append("</VALOR>");
                            xmlSalida.append("<ERROR>");
                                xmlSalida.append(ConstantesMeLanbide44.FALSO);
                            xmlSalida.append("</ERROR>");
                        xmlSalida.append("</ID>");
                        xmlSalida.append("<NIF>");
                            xmlSalida.append("<VALOR>");
                                xmlSalida.append(prep.getNif());
                            xmlSalida.append("</VALOR>");
                            xmlSalida.append("<ERROR>");
                                xmlSalida.append(prep.getErrorCampo(FilaPreparadorSolicitudVO.POS_CAMPO_NIF));
                            xmlSalida.append("</ERROR>");
                        xmlSalida.append("</NIF>");
                        xmlSalida.append("<NOMBRE>");
                            xmlSalida.append("<VALOR>");
                                xmlSalida.append(prep.getNombreApel());
                            xmlSalida.append("</VALOR>");
                            xmlSalida.append("<ERROR>");
                                xmlSalida.append(prep.getErrorCampo(FilaPreparadorSolicitudVO.POS_CAMPO_NOMBREAPEL));
                            xmlSalida.append("</ERROR>");
                        xmlSalida.append("</NOMBRE>");
                        xmlSalida.append("<FECHA_INICIO>");
                            xmlSalida.append("<VALOR>");
                                xmlSalida.append(prep.getFechaInicio());
                            xmlSalida.append("</VALOR>");
                            xmlSalida.append("<ERROR>");
                                xmlSalida.append(prep.getErrorCampo(FilaPreparadorSolicitudVO.POS_CAMPO_FECHA_INICIO));
                            xmlSalida.append("</ERROR>");
                        xmlSalida.append("</FECHA_INICIO>");
                        xmlSalida.append("<FECHA_FIN>");
                            xmlSalida.append("<VALOR>");
                                xmlSalida.append(prep.getFechaFin());
                            xmlSalida.append("</VALOR>");
                            xmlSalida.append("<ERROR>");
                                xmlSalida.append(prep.getErrorCampo(FilaPreparadorSolicitudVO.POS_CAMPO_FECHA_FIN));
                            xmlSalida.append("</ERROR>");
                        xmlSalida.append("</FECHA_FIN>");
                        xmlSalida.append("<HORAS_ANUALES>");
                            xmlSalida.append("<VALOR>");
                                xmlSalida.append(prep.getHorasAnuales());
                            xmlSalida.append("</VALOR>");
                            xmlSalida.append("<ERROR>");
                                xmlSalida.append(prep.getErrorCampo(FilaPreparadorSolicitudVO.POS_CAMPO_HORAS_ANUALES));
                            xmlSalida.append("</ERROR>");
                        xmlSalida.append("</HORAS_ANUALES>");
                        xmlSalida.append("<HORAS_CONTRATO>");
                            xmlSalida.append("<VALOR>");
                                xmlSalida.append(prep.getHorasContrato());
                            xmlSalida.append("</VALOR>");
                            xmlSalida.append("<ERROR>");
                                xmlSalida.append(prep.getErrorCampo(FilaPreparadorSolicitudVO.POS_CAMPO_HORAS_CONTRATO));
                            xmlSalida.append("</ERROR>");
                        xmlSalida.append("</HORAS_CONTRATO>");
                        xmlSalida.append("<HORAS_DEDICACION_ECA>");
                            xmlSalida.append("<VALOR>");
                                xmlSalida.append(prep.getHorasDedicacionECA());
                            xmlSalida.append("</VALOR>");
                            xmlSalida.append("<ERROR>");
                                xmlSalida.append(prep.getErrorCampo(FilaPreparadorSolicitudVO.POS_CAMPO_HORAS_DEDICACION_ECA));
                            xmlSalida.append("</ERROR>");
                        xmlSalida.append("</HORAS_DEDICACION_ECA>");
                        xmlSalida.append("<COSTES_SALARIALES_SS_JOR>");
                            xmlSalida.append("<VALOR>");
                                xmlSalida.append(prep.getCostesSalarialesSSJor());
                            xmlSalida.append("</VALOR>");
                            xmlSalida.append("<ERROR>");
                                xmlSalida.append(prep.getErrorCampo(FilaPreparadorSolicitudVO.POS_CAMPO_COSTES_SALARIALES_SS_JOR));
                            xmlSalida.append("</ERROR>");
                        xmlSalida.append("</COSTES_SALARIALES_SS_JOR>");
                        xmlSalida.append("<COSTES_SALARIALES_SS_POR_JOR>");
                            xmlSalida.append("<VALOR>");
                                xmlSalida.append(prep.getCostesSalarialesSSPorJor());
                            xmlSalida.append("</VALOR>");
                            xmlSalida.append("<ERROR>");
                                xmlSalida.append(prep.getErrorCampo(FilaPreparadorSolicitudVO.POS_CAMPO_COSTES_SALARIALES_SS_POR_JOR));
                            xmlSalida.append("</ERROR>");
                        xmlSalida.append("</COSTES_SALARIALES_SS_POR_JOR>");
                        xmlSalida.append("<COSTES_SALARIALES_SS_ECA>");
                            xmlSalida.append("<VALOR>");
                                xmlSalida.append(prep.getCostesSalarialesSSEca());
                            xmlSalida.append("</VALOR>");
                            xmlSalida.append("<ERROR>");
                                xmlSalida.append(prep.getErrorCampo(FilaPreparadorSolicitudVO.POS_CAMPO_COSTES_SALARIALES_SS_ECA));
                            xmlSalida.append("</ERROR>");
                        xmlSalida.append("</COSTES_SALARIALES_SS_ECA>");
                        xmlSalida.append("<NUM_SEG_ANTERIORES>");
                            xmlSalida.append("<VALOR>");
                                xmlSalida.append(prep.getNumSegAnteriores());
                            xmlSalida.append("</VALOR>");
                            xmlSalida.append("<ERROR>");
                                xmlSalida.append(prep.getErrorCampo(FilaPreparadorSolicitudVO.POS_CAMPO_NUM_SEG_ANTERIORES));
                            xmlSalida.append("</ERROR>");
                        xmlSalida.append("</NUM_SEG_ANTERIORES>");
                        xmlSalida.append("<IMPORTE>");
                            xmlSalida.append("<VALOR>");
                                xmlSalida.append(prep.getImporte());
                            xmlSalida.append("</VALOR>");
                            xmlSalida.append("<ERROR>");
                                xmlSalida.append(prep.getErrorCampo(FilaPreparadorSolicitudVO.POS_CAMPO_IMPORTE));
                            xmlSalida.append("</ERROR>");
                        xmlSalida.append("</IMPORTE>");
                        xmlSalida.append("<C1H>");
                            xmlSalida.append("<VALOR>");
                                xmlSalida.append(prep.getC1h());
                            xmlSalida.append("</VALOR>");
                            xmlSalida.append("<ERROR>");
                                xmlSalida.append(prep.getErrorCampo(FilaPreparadorSolicitudVO.POS_CAMPO_C1H));
                            xmlSalida.append("</ERROR>");
                        xmlSalida.append("</C1H>");
                        xmlSalida.append("<C1M>");
                            xmlSalida.append("<VALOR>");
                                xmlSalida.append(prep.getC1m());
                            xmlSalida.append("</VALOR>");
                            xmlSalida.append("<ERROR>");
                                xmlSalida.append(prep.getErrorCampo(FilaPreparadorSolicitudVO.POS_CAMPO_C1M));
                            xmlSalida.append("</ERROR>");
                        xmlSalida.append("</C1M>");
                        xmlSalida.append("<C1TOTAL>");
                            xmlSalida.append("<VALOR>");
                                xmlSalida.append(prep.getC1Total());
                            xmlSalida.append("</VALOR>");
                            xmlSalida.append("<ERROR>");
                                xmlSalida.append(prep.getErrorCampo(FilaPreparadorSolicitudVO.POS_CAMPO_C1TOTAL));
                            xmlSalida.append("</ERROR>");
                        xmlSalida.append("</C1TOTAL>");
                        xmlSalida.append("<C2H>");
                            xmlSalida.append("<VALOR>");
                                xmlSalida.append(prep.getC2h());
                            xmlSalida.append("</VALOR>");
                            xmlSalida.append("<ERROR>");
                                xmlSalida.append(prep.getErrorCampo(FilaPreparadorSolicitudVO.POS_CAMPO_C2H));
                            xmlSalida.append("</ERROR>");
                        xmlSalida.append("</C2H>");
                        xmlSalida.append("<C2M>");
                            xmlSalida.append("<VALOR>");
                                xmlSalida.append(prep.getC2m());
                            xmlSalida.append("</VALOR>");
                            xmlSalida.append("<ERROR>");
                                xmlSalida.append(prep.getErrorCampo(FilaPreparadorSolicitudVO.POS_CAMPO_C2M));
                            xmlSalida.append("</ERROR>");
                        xmlSalida.append("</C2M>");
                        xmlSalida.append("<C2TOTAL>");
                            xmlSalida.append("<VALOR>");
                                xmlSalida.append(prep.getC2Total());
                            xmlSalida.append("</VALOR>");
                            xmlSalida.append("<ERROR>");
                                xmlSalida.append(prep.getErrorCampo(FilaPreparadorSolicitudVO.POS_CAMPO_C2TOTAL));
                            xmlSalida.append("</ERROR>");
                        xmlSalida.append("</C2TOTAL>");
                        xmlSalida.append("<C3H>");
                            xmlSalida.append("<VALOR>");
                                xmlSalida.append(prep.getC3h());
                            xmlSalida.append("</VALOR>");
                            xmlSalida.append("<ERROR>");
                                xmlSalida.append(prep.getErrorCampo(FilaPreparadorSolicitudVO.POS_CAMPO_C3H));
                            xmlSalida.append("</ERROR>");
                        xmlSalida.append("</C3H>");
                        xmlSalida.append("<C3M>");
                            xmlSalida.append("<VALOR>");
                                xmlSalida.append(prep.getC3m());
                            xmlSalida.append("</VALOR>");
                            xmlSalida.append("<ERROR>");
                                xmlSalida.append(prep.getErrorCampo(FilaPreparadorSolicitudVO.POS_CAMPO_C3M));
                            xmlSalida.append("</ERROR>");
                        xmlSalida.append("</C3M>");
                        xmlSalida.append("<C3TOTAL>");
                            xmlSalida.append("<VALOR>");
                                xmlSalida.append(prep.getC3Total());
                            xmlSalida.append("</VALOR>");
                            xmlSalida.append("<ERROR>");
                                xmlSalida.append(prep.getErrorCampo(FilaPreparadorSolicitudVO.POS_CAMPO_C3TOTAL));
                            xmlSalida.append("</ERROR>");
                        xmlSalida.append("</C3TOTAL>");
                        xmlSalida.append("<C4H>");
                            xmlSalida.append("<VALOR>");
                                xmlSalida.append(prep.getC4h());
                            xmlSalida.append("</VALOR>");
                            xmlSalida.append("<ERROR>");
                                xmlSalida.append(prep.getErrorCampo(FilaPreparadorSolicitudVO.POS_CAMPO_C4H));
                            xmlSalida.append("</ERROR>");
                        xmlSalida.append("</C4H>");
                        xmlSalida.append("<C4M>");
                            xmlSalida.append("<VALOR>");
                                xmlSalida.append(prep.getC4m());
                            xmlSalida.append("</VALOR>");
                            xmlSalida.append("<ERROR>");
                                xmlSalida.append(prep.getErrorCampo(FilaPreparadorSolicitudVO.POS_CAMPO_C4M));
                            xmlSalida.append("</ERROR>");
                        xmlSalida.append("</C4M>");
                        xmlSalida.append("<C4TOTAL>");
                            xmlSalida.append("<VALOR>");
                                xmlSalida.append(prep.getC4m());
                            xmlSalida.append("</VALOR>");
                            xmlSalida.append("<ERROR>");
                                xmlSalida.append(prep.getErrorCampo(FilaPreparadorSolicitudVO.POS_CAMPO_C4TOTAL));
                            xmlSalida.append("</ERROR>");
                        xmlSalida.append("</C4TOTAL>");
                         xmlSalida.append("<C5H>");
                            xmlSalida.append("<VALOR>");
                                xmlSalida.append(prep.getC5h());
                            xmlSalida.append("</VALOR>");
                            xmlSalida.append("<ERROR>");
                                xmlSalida.append(prep.getErrorCampo(FilaPreparadorSolicitudVO.POS_CAMPO_C5H));
                            xmlSalida.append("</ERROR>");
                        xmlSalida.append("</C5H>");
                        xmlSalida.append("<C5M>");
                            xmlSalida.append("<VALOR>");
                                xmlSalida.append(prep.getC5m());
                            xmlSalida.append("</VALOR>");
                            xmlSalida.append("<ERROR>");
                                xmlSalida.append(prep.getErrorCampo(FilaPreparadorSolicitudVO.POS_CAMPO_C5M));
                            xmlSalida.append("</ERROR>");
                        xmlSalida.append("</C5M>");
                        xmlSalida.append("<C5TOTAL>");
                            xmlSalida.append("<VALOR>");
                                xmlSalida.append(prep.getC5m());
                            xmlSalida.append("</VALOR>");
                            xmlSalida.append("<ERROR>");
                                xmlSalida.append(prep.getErrorCampo(FilaPreparadorSolicitudVO.POS_CAMPO_C5TOTAL));
                            xmlSalida.append("</ERROR>");
                        xmlSalida.append("</C5TOTAL>");
                         xmlSalida.append("<C6H>");
                            xmlSalida.append("<VALOR>");
                                xmlSalida.append(prep.getC6h());
                            xmlSalida.append("</VALOR>");
                            xmlSalida.append("<ERROR>");
                                xmlSalida.append(prep.getErrorCampo(FilaPreparadorSolicitudVO.POS_CAMPO_C6H));
                            xmlSalida.append("</ERROR>");
                        xmlSalida.append("</C6H>");
                        xmlSalida.append("<C6M>");
                            xmlSalida.append("<VALOR>");
                                xmlSalida.append(prep.getC6m());
                            xmlSalida.append("</VALOR>");
                            xmlSalida.append("<ERROR>");
                                xmlSalida.append(prep.getErrorCampo(FilaPreparadorSolicitudVO.POS_CAMPO_C6M));
                            xmlSalida.append("</ERROR>");
                        xmlSalida.append("</C6M>");
                        xmlSalida.append("<C6TOTAL>");
                            xmlSalida.append("<VALOR>");
                                xmlSalida.append(prep.getC6m());
                            xmlSalida.append("</VALOR>");
                            xmlSalida.append("<ERROR>");
                                xmlSalida.append(prep.getErrorCampo(FilaPreparadorSolicitudVO.POS_CAMPO_C6TOTAL));
                            xmlSalida.append("</ERROR>");
                        xmlSalida.append("</C6TOTAL>");
                        xmlSalida.append("<INSERCIONES>");
                            xmlSalida.append("<VALOR>");
                                xmlSalida.append(prep.getInserciones());
                            xmlSalida.append("</VALOR>");
                            xmlSalida.append("<ERROR>");
                                xmlSalida.append(prep.getErrorCampo(FilaPreparadorSolicitudVO.POS_CAMPO_INSERCIONES));
                            xmlSalida.append("</ERROR>");
                        xmlSalida.append("</INSERCIONES>");
                        xmlSalida.append("<SEGUIMIENTOS_INSERCIONES>");
                            xmlSalida.append("<VALOR>");
                                xmlSalida.append(prep.getSeguimientosInserciones());
                            xmlSalida.append("</VALOR>");
                            xmlSalida.append("<ERROR>");
                                xmlSalida.append(prep.getErrorCampo(FilaPreparadorSolicitudVO.POS_CAMPO_SEGUIMIENTOS_INSERCIONES));
                            xmlSalida.append("</ERROR>");
                        xmlSalida.append("</SEGUIMIENTOS_INSERCIONES>");
                        xmlSalida.append("<COSTES_SALARIALES_SS>");
                            xmlSalida.append("<VALOR>");
                                xmlSalida.append(prep.getCostesSalarialesSS());
                            xmlSalida.append("</VALOR>");
                            xmlSalida.append("<ERROR>");
                                xmlSalida.append(prep.getErrorCampo(FilaPreparadorSolicitudVO.POS_CAMPO_COSTES_SALARIALES_SS));
                            xmlSalida.append("</ERROR>");
                        xmlSalida.append("</COSTES_SALARIALES_SS>");
                        xmlSalida.append("<IMPORTE_CONCEDIDO>");
                            xmlSalida.append("<VALOR>");
                                xmlSalida.append(prep.getImporteConcedido());
                            xmlSalida.append("</VALOR>");
                            xmlSalida.append("<ERROR>");
                                xmlSalida.append(prep.getErrorCampo(FilaPreparadorSolicitudVO.POS_CAMPO_IMPORTE_CONCEDIDO));
                            xmlSalida.append("</ERROR>");
                        xmlSalida.append("</IMPORTE_CONCEDIDO>");
                        if(prep.getTipoSust() != null && !prep.getTipoSust().equals(""))
                        {
                            xmlSalida.append("<TIPO_SUST>");
                                xmlSalida.append(prep.getTipoSust());
                            xmlSalida.append("</TIPO_SUST>");
                        }
                        xmlSalida.append("<ERRORES>");
                        if(prep.getErrores() != null)
                        {
                            for(int contE = 0; contE < prep.getErrores().size(); contE++)
                            {
                                xmlSalida.append("<ERROR>");
                                    xmlSalida.append(prep.getErrores().get(contE));
                                xmlSalida.append("</ERROR>");
                            }
                        }
                        xmlSalida.append("</ERRORES>");
                    xmlSalida.append("</FILA>");
                    i++;
                }
                if(validaciones != null && !validaciones.isEmpty())
                {
                    xmlSalida.append("<VALIDACION>");
                        String validacion = validaciones.get(0);
                        String mensajeCompleto = null;
                        if(validacion != null && !validacion.equals(""))
                        {
                            if(validacion.contains("/"))
                            {
                                String[] array = validacion.split("/");
                                String parte = null;
                                for(int j = 1; j < array.length; j++)
                                {
                                    parte = array[j];
                                    if(mensajeCompleto == null)
                                    {
                                        mensajeCompleto = new String(parte);
                                    }
                                    else
                                    {
                                        mensajeCompleto += ConstantesMeLanbide44.BARRA_SEPARADORA+parte;
                                    }
                                }
                                //validacion = array[array.length-1];
                            }
                        }
                        else
                        {
                            mensajeCompleto = MeLanbide44I18n.getInstance().getMensaje(codigoIdioma,"error.datosNoValidos");
                        }
                        xmlSalida.append(mensajeCompleto);
                    xmlSalida.append("</VALIDACION>");
                }
            xmlSalida.append("</RESPUESTA>");
            try{
                response.setContentType("text/xml");
                response.setCharacterEncoding("UTF-8");
                PrintWriter out = response.getWriter();
                out.print(xmlSalida.toString());
                out.flush();
                out.close();
            }catch(Exception e){
                log.error(" Error : " + e.getMessage(), e);
            }//try-catch
    }
    
    private void escribirListaProspectoresRequest(String codigoOperacion, List<FilaProspectorSolicitudVO> prospectores, List<String> validaciones, int codigoIdioma, HttpServletResponse response)
    {
        StringBuffer xmlSalida = new StringBuffer();
            xmlSalida.append("<RESPUESTA>");
                xmlSalida.append("<CODIGO_OPERACION>");
                    xmlSalida.append(codigoOperacion);
                xmlSalida.append("</CODIGO_OPERACION>");
                int i = 0;
                for(FilaProspectorSolicitudVO pros : prospectores)
                {
                    xmlSalida.append("<FILA>");
                        xmlSalida.append("<ID>");
                            xmlSalida.append("<VALOR>");
                                xmlSalida.append(pros.getId());
                            xmlSalida.append("</VALOR>");
                            xmlSalida.append("<ERROR>");
                                xmlSalida.append(ConstantesMeLanbide44.FALSO);
                            xmlSalida.append("</ERROR>");
                        xmlSalida.append("</ID>");
                        xmlSalida.append("<NIF>");
                            xmlSalida.append("<VALOR>");
                                xmlSalida.append(pros.getNif());
                            xmlSalida.append("</VALOR>");
                            xmlSalida.append("<ERROR>");
                                xmlSalida.append(pros.getErrorCampo(FilaProspectorSolicitudVO.POS_CAMPO_NIF));
                            xmlSalida.append("</ERROR>");
                        xmlSalida.append("</NIF>");
                        xmlSalida.append("<NOMBRE>");
                            xmlSalida.append("<VALOR>");
                                xmlSalida.append(pros.getNombreApel());
                            xmlSalida.append("</VALOR>");
                            xmlSalida.append("<ERROR>");
                                xmlSalida.append(pros.getErrorCampo(FilaProspectorSolicitudVO.POS_CAMPO_NOMBREAPEL));
                            xmlSalida.append("</ERROR>");
                        xmlSalida.append("</NOMBRE>");
                        xmlSalida.append("<FECHA_INICIO>");
                            xmlSalida.append("<VALOR>");
                                xmlSalida.append(pros.getFechaInicio());
                            xmlSalida.append("</VALOR>");
                            xmlSalida.append("<ERROR>");
                                xmlSalida.append(pros.getErrorCampo(FilaProspectorSolicitudVO.POS_CAMPO_FECHA_INICIO));
                            xmlSalida.append("</ERROR>");
                        xmlSalida.append("</FECHA_INICIO>");
                        xmlSalida.append("<FECHA_FIN>");
                            xmlSalida.append("<VALOR>");
                                xmlSalida.append(pros.getFechaFin());
                            xmlSalida.append("</VALOR>");
                            xmlSalida.append("<ERROR>");
                                xmlSalida.append(pros.getErrorCampo(FilaProspectorSolicitudVO.POS_CAMPO_FECHA_FIN));
                            xmlSalida.append("</ERROR>");
                        xmlSalida.append("</FECHA_FIN>");
                        xmlSalida.append("<HORAS_ANUALES>");
                            xmlSalida.append("<VALOR>");
                                xmlSalida.append(pros.getHorasAnuales());
                            xmlSalida.append("</VALOR>");
                            xmlSalida.append("<ERROR>");
                                xmlSalida.append(pros.getErrorCampo(FilaProspectorSolicitudVO.POS_CAMPO_HORAS_ANUALES));
                            xmlSalida.append("</ERROR>");
                        xmlSalida.append("</HORAS_ANUALES>");
                        xmlSalida.append("<HORAS_CONTRATO>");
                            xmlSalida.append("<VALOR>");
                                xmlSalida.append(pros.getHorasContrato());
                            xmlSalida.append("</VALOR>");
                            xmlSalida.append("<ERROR>");
                                xmlSalida.append(pros.getErrorCampo(FilaProspectorSolicitudVO.POS_CAMPO_HORAS_CONTRATO));
                            xmlSalida.append("</ERROR>");
                        xmlSalida.append("</HORAS_CONTRATO>");
                        xmlSalida.append("<HORAS_DEDICACION_ECA>");
                            xmlSalida.append("<VALOR>");
                                xmlSalida.append(pros.getHorasDedicacionECA());
                            xmlSalida.append("</VALOR>");
                            xmlSalida.append("<ERROR>");
                                xmlSalida.append(pros.getErrorCampo(FilaProspectorSolicitudVO.POS_CAMPO_HORAS_DEDICACION_ECA));
                            xmlSalida.append("</ERROR>");
                        xmlSalida.append("</HORAS_DEDICACION_ECA>");
                        xmlSalida.append("<COSTES_SALARIALES_SS_JOR>");
                            xmlSalida.append("<VALOR>");
                                xmlSalida.append(pros.getCostesSalarialesSSJor());
                            xmlSalida.append("</VALOR>");
                            xmlSalida.append("<ERROR>");
                                xmlSalida.append(pros.getErrorCampo(FilaProspectorSolicitudVO.POS_CAMPO_COSTES_SALARIALES_SS_JOR));
                            xmlSalida.append("</ERROR>");
                        xmlSalida.append("</COSTES_SALARIALES_SS_JOR>");
                        xmlSalida.append("<COSTES_SALARIALES_SS_POR_JOR>");
                            xmlSalida.append("<VALOR>");
                                xmlSalida.append(pros.getCostesSalarialesSSPorJor());
                            xmlSalida.append("</VALOR>");
                            xmlSalida.append("<ERROR>");
                                xmlSalida.append(pros.getErrorCampo(FilaProspectorSolicitudVO.POS_CAMPO_COSTES_SALARIALES_SS_POR_JOR));
                            xmlSalida.append("</ERROR>");
                        xmlSalida.append("</COSTES_SALARIALES_SS_POR_JOR>");
                        xmlSalida.append("<COSTES_SALARIALES_SS_ECA>");
                            xmlSalida.append("<VALOR>");
                                xmlSalida.append(pros.getCostesSalarialesSSEca());
                            xmlSalida.append("</VALOR>");
                            xmlSalida.append("<ERROR>");
                                xmlSalida.append(pros.getErrorCampo(FilaProspectorSolicitudVO.POS_CAMPO_COSTES_SALARIALES_SS_ECA));
                            xmlSalida.append("</ERROR>");
                        xmlSalida.append("</COSTES_SALARIALES_SS_ECA>");
                        xmlSalida.append("<VISITAS>");
                            xmlSalida.append("<VALOR>");
                                xmlSalida.append(pros.getVisitas());
                            xmlSalida.append("</VALOR>");
                            xmlSalida.append("<ERROR>");
                                xmlSalida.append(pros.getErrorCampo(FilaProspectorSolicitudVO.POS_CAMPO_VISITAS));
                            xmlSalida.append("</ERROR>");
                        xmlSalida.append("</VISITAS>");
                        xmlSalida.append("<VISITAS_IMP>");
                            xmlSalida.append("<VALOR>");
                                xmlSalida.append(pros.getVisitasImp());
                            xmlSalida.append("</VALOR>");
                            xmlSalida.append("<ERROR>");
                                xmlSalida.append(pros.getErrorCampo(FilaProspectorSolicitudVO.POS_CAMPO_VISITAS_IMP));
                            xmlSalida.append("</ERROR>");
                        xmlSalida.append("</VISITAS_IMP>");
                        xmlSalida.append("<COSTE>");
                            xmlSalida.append("<VALOR>");
                                xmlSalida.append(pros.getCoste());
                            xmlSalida.append("</VALOR>");
                            xmlSalida.append("<ERROR>");
                                xmlSalida.append(pros.getErrorCampo(FilaProspectorSolicitudVO.POS_CAMPO_COSTE));
                            xmlSalida.append("</ERROR>");
                        xmlSalida.append("</COSTE>");
                        xmlSalida.append("<IMPORTE_CONCEDIDO>");
                            xmlSalida.append("<VALOR>");
                                xmlSalida.append(pros.getImporteConcedido());
                            xmlSalida.append("</VALOR>");
                            xmlSalida.append("<ERROR>");
                                xmlSalida.append(pros.getErrorCampo(FilaProspectorSolicitudVO.POS_CAMPO_COSTE));
                            xmlSalida.append("</ERROR>");
                        xmlSalida.append("</IMPORTE_CONCEDIDO>");
                        if(pros.getTipoSust() != null)
                        {
                            xmlSalida.append("<TIPO_SUST>");
                                xmlSalida.append(pros.getTipoSust());
                            xmlSalida.append("</TIPO_SUST>");
                        }
                        xmlSalida.append("<ERRORES>");
                        if(pros.getErrores() != null)
                        {
                            for(int contE = 0; contE < pros.getErrores().size(); contE++)
                            {
                                xmlSalida.append("<ERROR>");
                                    xmlSalida.append(pros.getErrores().get(contE));
                                xmlSalida.append("</ERROR>");
                            }
                        }
                        xmlSalida.append("</ERRORES>");
                    xmlSalida.append("</FILA>");
                    i++;
                }
                if(validaciones != null && !validaciones.isEmpty())
                {
                    xmlSalida.append("<VALIDACION>");
                        String validacion = validaciones.get(0);
                        String mensajeCompleto = null;
                        if(validacion != null && !validacion.equals(""))
                        {
                            if(validacion.contains("/"))
                            {
                                String[] array = validacion.split("/");
                                String parte = null;
                                for(int j = 1; j < array.length; j++)
                                {
                                    parte = array[j];
                                    if(mensajeCompleto == null)
                                    {
                                        mensajeCompleto = new String(parte);
                                    }
                                    else
                                    {
                                        mensajeCompleto += ConstantesMeLanbide44.BARRA_SEPARADORA+parte;
                                    }
                                }
                                //validacion = array[array.length-1];
                            }
                        }
                        else
                        {
                            mensajeCompleto = MeLanbide44I18n.getInstance().getMensaje(codigoIdioma,"error.datosNoValidos");
                        }
                        xmlSalida.append(mensajeCompleto);
                    xmlSalida.append("</VALIDACION>");
                }
            xmlSalida.append("</RESPUESTA>");
            try{
                response.setContentType("text/xml");
                response.setCharacterEncoding("UTF-8");
                PrintWriter out = response.getWriter();
                out.print(xmlSalida.toString());
                out.flush();
                out.close();
            }catch(Exception e){
                log.error(" Error : " + e.getMessage(), e);
            }//try-catch
    }
    
    /******************
     * JUSTIFICACION
     * 
     ***************/
    
    public void eliminarPreparadorJustificacion(int codOrganizacion,  int codTramite, int ocurrenciaTramite,String numExpediente,HttpServletRequest request,HttpServletResponse response)
    {
        int codIdioma = 1;    
        try
        {
            UsuarioValueObject usuario = (UsuarioValueObject) request.getSession().getAttribute("usuario");
            if(usuario != null)
            {
                codIdioma = usuario.getIdioma();
            }
        }
        catch(Exception ex)
        {
        }
        
        List<FilaPreparadorJustificacionVO> preparadores = null;
        String codigoOperacion = "0";
        try
        {
            String idPrep = (String)request.getParameter("idPrep");
            Integer idP = null;
            if(idPrep == null || idPrep.equals(""))
            {
                codigoOperacion = "3";
            }
            else
            {
                try
                {
                    idP = Integer.parseInt(idPrep);
                }
                catch(Exception ex)
                {
                    codigoOperacion = "3";
                }
                if(idP != null)
                {
                    EcaJusPreparadoresVO prep = new EcaJusPreparadoresVO();
                    prep.setJusPreparadoresCod(idP);
                    int result = MeLanbide44Manager.getInstance().eliminarPreparadorJustificacion(prep, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                    if(result <= 0)
                    {
                        codigoOperacion = "1";
                    }
                    else
                    {
                        codigoOperacion = "0";                       
                    }
                }
                else
                {
                    codigoOperacion = "3";
                }
            }
        }
        catch(Exception ex)
        {
            codigoOperacion = "2";
        }
     /*   if(codigoOperacion.equalsIgnoreCase("0"))
        {*/
            //this.getListaPreparadoresSoliictud(codOrganizacion, codTramite, ocurrenciaTramite, numExpediente, request, response);
            try{
                EcaSolicitudVO sol = MeLanbide44Manager.getInstance().getDatosSolicitud(numExpediente, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                preparadores = MeLanbide44Manager.getInstance().getListaPreparadoresJustificacion(sol,this.getAdaptSQLBD(String.valueOf(codOrganizacion)), codIdioma);
                this.escribirListaPreparadoresJusRequest(codigoOperacion, preparadores, null, codIdioma, response);
            }catch(Exception e){
                log.error(" Error : " + e.getMessage(), e);                
            } 
            
            
/*        }
        else
        {
            StringBuffer xmlSalida = new StringBuffer();
            xmlSalida.append("<RESPUESTA>");
                xmlSalida.append("<CODIGO_OPERACION>");
                    xmlSalida.append(codigoOperacion);
                xmlSalida.append("</CODIGO_OPERACION>");
            xmlSalida.append("</RESPUESTA>");
            try{
                response.setContentType("text/xml");
                response.setCharacterEncoding("UTF-8");
                PrintWriter out = response.getWriter();
                out.print(xmlSalida.toString());
                out.flush();
                out.close();
            }catch(Exception e){
                log.error(" Error : " + e.getMessage(), e);
            }//try-catch
        }*/
    }
    
    public void guardarPreparadorJustificacion(int codOrganizacion,  int codTramite, int ocurrenciaTramite,String numExpediente,HttpServletRequest request,HttpServletResponse response)
    {
        int codIdioma = 1;    
        boolean soloFormato = false;
        List<String> validaciones = new ArrayList<String>();
        try
        {
            UsuarioValueObject usuario = (UsuarioValueObject) request.getSession().getAttribute("usuario");
            if(usuario != null)
            {
                codIdioma = usuario.getIdioma();
            }
        }
        catch(Exception ex)
        {

        }
        
        String codigoOperacion = "0";
        List<FilaPreparadorJustificacionVO> preparadores = new ArrayList<FilaPreparadorJustificacionVO>();
        try
        {
            AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
            
            EcaSolicitudVO sol = MeLanbide44Manager.getInstance().getDatosSolicitud(numExpediente, adapt);
            if(sol != null && sol.getSolicitudCod() != null)
            {
                    
                SimpleDateFormat format = new SimpleDateFormat(ConstantesMeLanbide44.FORMATO_FECHA);
                //Recojo los parámetros
                String idPrep = (String)request.getParameter("idPrep");
                String nif = (String)request.getParameter("nif");
                String nomApel = (String)request.getParameter("nomApel");
                String feIni = (String)request.getParameter("feIni");
                String feFin = (String)request.getParameter("feFin");
                String horasAnualesJC = (String)request.getParameter("horasAnualesJC");
                String horasContrato = (String)request.getParameter("horasContrato");
                String horasECA = (String)request.getParameter("horasECA");
                String costesSSJor = (String)request.getParameter("costesSSJor");
                String costesSSPorJor = (String)request.getParameter("costesSSPorJor");
                String costesSSECA = (String)request.getParameter("costesSSECA");
                String segAnt = (String)request.getParameter("segAnt");
                String importe = (String)request.getParameter("importe");                
                String inserciones = (String)request.getParameter("inserciones");
                String segIns = (String)request.getParameter("segIns");
                String costesSS = (String)request.getParameter("costesSS");
                String tipoContrato = (String)request.getParameter("tipoContrato");
                String idPrepOrigen = (String)request.getParameter("idPrepOrigen");
                
                //SimpleDateFormat format = new SimpleDateFormat(ConstantesMeLanbide44.FORMATO_FECHA);
                EcaJusPreparadoresVO prepValida = MeLanbide44Manager.getInstance().getPreparadorJustificacionPorNIF(sol, nif, feIni != null && !feIni.equals("") ? format.parse(feIni) : null, /*feFin,*/ adapt);
                boolean prepRepetido = false;
                if(prepValida != null && idPrep != null)
                {
                    if(!idPrep.equalsIgnoreCase(prepValida.getJusPreparadoresCod().toString()))
                    {
                        prepRepetido = true;
                    }
                }
                if(!prepRepetido)
                {
                    EcaJusPreparadoresVO prep = null;
                    if(idPrep != null && !idPrep.equals(""))
                    {
                        prep = new EcaJusPreparadoresVO();
                        prep.setJusPreparadoresCod(Integer.parseInt(idPrep));
                        prep = MeLanbide44Manager.getInstance().getPreparadorJustificacionPorId(prep, adapt);
                        soloFormato = true;
                    }
                    else
                    {
                        prep = new EcaJusPreparadoresVO();
                    }
                    if(prep == null)
                    {
                        codigoOperacion = "3";
                    }
                    else
                    {   
                        EcaSolPreparadoresVO prepRelacion = null;
                        try
                        {
                            prepRelacion = MeLanbide44Manager.getInstance().getPreparadorSolicitudPorNIF(sol, nif, adapt);
                        }
                        catch(Exception ex)
                        {
                            
                        }
                        if(prepRelacion != null)
                        {
                            prep.setSolPreparadoresCod(prepRelacion.getSolPreparadoresCod());
                        }
                        else
                        {
                            prep.setSolPreparadoresCod(null);
                        }
                        
                        
                        prep.setCoste(costesSS != null && !costesSS.equals("") ? new BigDecimal(costesSS.replaceAll(",", "\\.")) : null);
                        prep.setFecFin(feFin != null && !feFin.equals("") ? format.parse(feFin) : null);
                        prep.setFecIni(feIni != null && !feIni.equals("") ? format.parse(feIni) : null);
                        prep.setHorasCont(horasContrato != null && !horasContrato.equals("") ? new BigDecimal(horasContrato.replaceAll(",", "\\.")) : null);
                        prep.setHorasEca(horasECA != null && !horasECA.equals("") ? new BigDecimal(horasECA.replaceAll(",", "\\.")) : null);
                        prep.setHorasJC(horasAnualesJC != null && !horasAnualesJC.equals("") ? new BigDecimal(horasAnualesJC.replaceAll(",", "\\.")) : null);
                        prep.setImpSSECA(costesSSECA != null && !costesSSECA.equals("") ? new BigDecimal(costesSSECA.replaceAll(",", "\\.")) : null);
                        prep.setImpSSJC(costesSSJor != null && !costesSSJor.equals("") ? new BigDecimal(costesSSJor.replaceAll(",", "\\.")) : null);
                        prep.setImpSSJR(costesSSPorJor != null && !costesSSPorJor.equals("") ? new BigDecimal(costesSSPorJor.replaceAll(",", "\\.")) : null);
                        prep.setImpSegAnt(importe != null && !importe.equals("") ? new BigDecimal(importe.replaceAll(",", "\\.")) : null);
                        prep.setInsImporte(inserciones != null && !inserciones.equals("") ? new BigDecimal(inserciones.replaceAll(",", "\\.")) : null);
                        prep.setInsSegImporte(segIns != null && !segIns.equals("") ? new BigDecimal(segIns.replaceAll(",", "\\.")) : null);
                        prep.setNif(nif != null ? nif.toUpperCase() : null);
                        prep.setNombre(nomApel != null ? nomApel.toUpperCase() : null);
                        prep.setSegAnt(segAnt != null && !segAnt.equals("") ? new BigDecimal(segAnt.replaceAll(",", "\\.")) : null);
                        prep.setSolicitud(sol.getSolicitudCod());
                        prep.setTipoContrato(tipoContrato != null && !tipoContrato.equals("")?Integer.parseInt(tipoContrato): null);
                        if(idPrepOrigen != null && !idPrepOrigen.equals(""))
                        {
                            prep.setJusPreparadorOrigen(Integer.parseInt(idPrepOrigen));
                            if(prep.getTipoSust() == null)
                            {
                                prep.setTipoSust(ConstantesMeLanbide44.TIPOS_SUSTITUTO.SUSTITUTO_JUSTIFICACION);
                            }
                        }

                        List<Integer> listaTipoContrato =  MeLanbide44Manager.getInstance().getListaCodigosTipoContrato(adapt);
                        //String validacion = MeLanbide44Utils.validarEcaJusPreparadoresVO(prep,true);
                        
                        EcaJusPreparadoresVO origen = null;
                        
                        if(prep.getJusPreparadorOrigen() != null)
                        {
                            origen = new EcaJusPreparadoresVO();
                            origen.setJusPreparadoresCod(prep.getJusPreparadorOrigen());
                            origen = MeLanbide44Manager.getInstance().getPreparadorJustificacionPorId(origen, adapt);
                        }
                        
                        List<EcaJusPreparadoresVO> sustitutos = MeLanbide44Manager.getInstance().getSustitutosPreparadorJustificacion(prep.getJusPreparadoresCod(), adapt);
                        
                        validaciones = MeLanbide44Utils.validarEcaJusPreparadoresVO(prep, origen, sustitutos, listaTipoContrato, null, sol.getExpEje(), codIdioma, soloFormato, false);
                        
                        if(validaciones == null || validaciones.isEmpty())
                        {
                            MeLanbide44Manager.getInstance().guardarEcaJusPreparadoresVO(prep, adapt);
                            preparadores = MeLanbide44Manager.getInstance().getListaPreparadoresJustificacion(sol, adapt, codIdioma);
                        }
                        else
                        {
                            codigoOperacion = "4";
                        }
                    }
                }
                else
                {
                    codigoOperacion = "5";
                }
            }
            else
            {
                codigoOperacion = "1";
            }
        }
        catch(Exception ex)
        {
            log.error(" Error : " + ex.getMessage(), ex);
            codigoOperacion = "2";
        }
        
        this.escribirListaPreparadoresJusRequest(codigoOperacion, preparadores, validaciones, codIdioma, response);
    }
    
    private void escribirListaPreparadoresJusRequest(String codigoOperacion, List<FilaPreparadorJustificacionVO> preparadores, List<String> validaciones, int codigoIdioma, HttpServletResponse response)
    {
         StringBuffer xmlSalida = new StringBuffer();
            xmlSalida.append("<RESPUESTA>");
                xmlSalida.append("<CODIGO_OPERACION>");
                    xmlSalida.append(codigoOperacion);
                xmlSalida.append("</CODIGO_OPERACION>");
                int i = 0;
                for(FilaPreparadorJustificacionVO prep : preparadores)
                {
                    xmlSalida.append("<FILA>");
                        xmlSalida.append("<ID>");
                            xmlSalida.append("<VALOR>");
                                xmlSalida.append(prep.getId());
                            xmlSalida.append("</VALOR>");
                            xmlSalida.append("<ERROR>");
                                xmlSalida.append(ConstantesMeLanbide44.FALSO);
                            xmlSalida.append("</ERROR>");
                        xmlSalida.append("</ID>");
                        xmlSalida.append("<NIF>");
                            xmlSalida.append("<VALOR>");
                                xmlSalida.append(prep.getNif());
                            xmlSalida.append("</VALOR>");
                            xmlSalida.append("<ERROR>");
                                xmlSalida.append(prep.getErrorCampo(FilaPreparadorJustificacionVO.POS_CAMPO_NIF));
                            xmlSalida.append("</ERROR>");
                        xmlSalida.append("</NIF>");
                        xmlSalida.append("<NOMBRE>");
                            xmlSalida.append("<VALOR>");
                                xmlSalida.append(prep.getNombreApel());
                            xmlSalida.append("</VALOR>");
                            xmlSalida.append("<ERROR>");
                                xmlSalida.append(prep.getErrorCampo(FilaPreparadorJustificacionVO.POS_CAMPO_NOMBREAPEL));
                            xmlSalida.append("</ERROR>");
                        xmlSalida.append("</NOMBRE>");
                        xmlSalida.append("<FECHA_INICIO>");
                            xmlSalida.append("<VALOR>");
                                xmlSalida.append(prep.getFechaInicio());
                            xmlSalida.append("</VALOR>");
                            xmlSalida.append("<ERROR>");
                                xmlSalida.append(prep.getErrorCampo(FilaPreparadorJustificacionVO.POS_CAMPO_FECHA_INICIO));
                            xmlSalida.append("</ERROR>");
                        xmlSalida.append("</FECHA_INICIO>");
                        xmlSalida.append("<FECHA_FIN>");
                            xmlSalida.append("<VALOR>");
                                xmlSalida.append(prep.getFechaFin());
                            xmlSalida.append("</VALOR>");
                            xmlSalida.append("<ERROR>");
                                xmlSalida.append(prep.getErrorCampo(FilaPreparadorJustificacionVO.POS_CAMPO_FECHA_FIN));
                            xmlSalida.append("</ERROR>");
                        xmlSalida.append("</FECHA_FIN>");
                        xmlSalida.append("<TIPO_CONTRATO>");
                            xmlSalida.append("<VALOR>");
                                xmlSalida.append(prep.getTipoContrato());
                            xmlSalida.append("</VALOR>");
                            xmlSalida.append("<ERROR>");
                                xmlSalida.append(prep.getErrorCampo(FilaPreparadorJustificacionVO.POS_CAMPO_TIPO_CONTRATO));
                            xmlSalida.append("</ERROR>");
                        xmlSalida.append("</TIPO_CONTRATO>");
                        xmlSalida.append("<HORAS_ANUALES>");
                            xmlSalida.append("<VALOR>");
                                xmlSalida.append(prep.getHorasAnuales());
                            xmlSalida.append("</VALOR>");
                            xmlSalida.append("<ERROR>");
                                xmlSalida.append(prep.getErrorCampo(FilaPreparadorJustificacionVO.POS_CAMPO_HORAS_ANUALES));
                            xmlSalida.append("</ERROR>");
                        xmlSalida.append("</HORAS_ANUALES>");
                        xmlSalida.append("<HORAS_CONTRATO>");
                            xmlSalida.append("<VALOR>");
                                xmlSalida.append(prep.getHorasContrato());
                            xmlSalida.append("</VALOR>");
                            xmlSalida.append("<ERROR>");
                                xmlSalida.append(prep.getErrorCampo(FilaPreparadorJustificacionVO.POS_CAMPO_HORAS_CONTRATO));
                            xmlSalida.append("</ERROR>");
                        xmlSalida.append("</HORAS_CONTRATO>");
                        xmlSalida.append("<HORAS_DEDICACION_ECA>");
                            xmlSalida.append("<VALOR>");
                                xmlSalida.append(prep.getHorasDedicacionECA());
                            xmlSalida.append("</VALOR>");
                            xmlSalida.append("<ERROR>");
                                xmlSalida.append(prep.getErrorCampo(FilaPreparadorJustificacionVO.POS_CAMPO_HORAS_DEDICACION_ECA));
                            xmlSalida.append("</ERROR>");
                        xmlSalida.append("</HORAS_DEDICACION_ECA>");
                        xmlSalida.append("<COSTES_SALARIALES_SS_JOR>");
                            xmlSalida.append("<VALOR>");
                                xmlSalida.append(prep.getCostesSalarialesSSJor());
                            xmlSalida.append("</VALOR>");
                            xmlSalida.append("<ERROR>");
                                xmlSalida.append(prep.getErrorCampo(FilaPreparadorJustificacionVO.POS_CAMPO_COSTES_SALARIALES_SS_JOR));
                            xmlSalida.append("</ERROR>");
                        xmlSalida.append("</COSTES_SALARIALES_SS_JOR>");
                        xmlSalida.append("<COSTES_SALARIALES_SS_POR_JOR>");
                            xmlSalida.append("<VALOR>");
                                xmlSalida.append(prep.getCostesSalarialesSSPorJor());
                            xmlSalida.append("</VALOR>");
                            xmlSalida.append("<ERROR>");
                                xmlSalida.append(prep.getErrorCampo(FilaPreparadorJustificacionVO.POS_CAMPO_COSTES_SALARIALES_SS_POR_JOR));
                            xmlSalida.append("</ERROR>");
                        xmlSalida.append("</COSTES_SALARIALES_SS_POR_JOR>");
                        xmlSalida.append("<COSTES_SALARIALES_SS_ECA>");
                            xmlSalida.append("<VALOR>");
                                xmlSalida.append(prep.getCostesSalarialesSSEca());
                            xmlSalida.append("</VALOR>");
                            xmlSalida.append("<ERROR>");
                                xmlSalida.append(prep.getErrorCampo(FilaPreparadorJustificacionVO.POS_CAMPO_COSTES_SALARIALES_SS_ECA));
                            xmlSalida.append("</ERROR>");
                        xmlSalida.append("</COSTES_SALARIALES_SS_ECA>");
                        xmlSalida.append("<NUM_SEG_ANTERIORES>");
                            xmlSalida.append("<VALOR>");
                                xmlSalida.append(prep.getNumSegAnteriores());
                            xmlSalida.append("</VALOR>");
                            xmlSalida.append("<ERROR>");
                                xmlSalida.append(prep.getErrorCampo(FilaPreparadorJustificacionVO.POS_CAMPO_NUM_SEG_ANTERIORES));
                            xmlSalida.append("</ERROR>");
                        xmlSalida.append("</NUM_SEG_ANTERIORES>");
                        xmlSalida.append("<IMPORTE>");
                            xmlSalida.append("<VALOR>");
                                xmlSalida.append(prep.getImporte());
                            xmlSalida.append("</VALOR>");
                            xmlSalida.append("<ERROR>");
                                xmlSalida.append(prep.getErrorCampo(FilaPreparadorJustificacionVO.POS_CAMPO_IMPORTE));
                            xmlSalida.append("</ERROR>");
                        xmlSalida.append("</IMPORTE>");
                        xmlSalida.append("<C1H>");
                            xmlSalida.append("<VALOR>");
                                xmlSalida.append(prep.getC1h());
                            xmlSalida.append("</VALOR>");
                            xmlSalida.append("<ERROR>");
                                xmlSalida.append(prep.getErrorCampo(FilaPreparadorJustificacionVO.POS_CAMPO_C1H));
                            xmlSalida.append("</ERROR>");
                        xmlSalida.append("</C1H>");
                        xmlSalida.append("<C1M>");
                            xmlSalida.append("<VALOR>");
                                xmlSalida.append(prep.getC1m());
                            xmlSalida.append("</VALOR>");
                            xmlSalida.append("<ERROR>");
                                xmlSalida.append(prep.getErrorCampo(FilaPreparadorJustificacionVO.POS_CAMPO_C1M));
                            xmlSalida.append("</ERROR>");
                        xmlSalida.append("</C1M>");
                        xmlSalida.append("<C1TOTAL>");
                            xmlSalida.append("<VALOR>");
                                xmlSalida.append(prep.getC1Total());
                            xmlSalida.append("</VALOR>");
                            xmlSalida.append("<ERROR>");
                                xmlSalida.append(prep.getErrorCampo(FilaPreparadorJustificacionVO.POS_CAMPO_C1TOTAL));
                            xmlSalida.append("</ERROR>");
                        xmlSalida.append("</C1TOTAL>");
                        xmlSalida.append("<C2H>");
                            xmlSalida.append("<VALOR>");
                                xmlSalida.append(prep.getC2h());
                            xmlSalida.append("</VALOR>");
                            xmlSalida.append("<ERROR>");
                                xmlSalida.append(prep.getErrorCampo(FilaPreparadorJustificacionVO.POS_CAMPO_C2H));
                            xmlSalida.append("</ERROR>");
                        xmlSalida.append("</C2H>");
                        xmlSalida.append("<C2M>");
                            xmlSalida.append("<VALOR>");
                                xmlSalida.append(prep.getC2m());
                            xmlSalida.append("</VALOR>");
                            xmlSalida.append("<ERROR>");
                                xmlSalida.append(prep.getErrorCampo(FilaPreparadorJustificacionVO.POS_CAMPO_C2M));
                            xmlSalida.append("</ERROR>");
                        xmlSalida.append("</C2M>");
                        xmlSalida.append("<C2TOTAL>");
                            xmlSalida.append("<VALOR>");
                                xmlSalida.append(prep.getC2Total());
                            xmlSalida.append("</VALOR>");
                            xmlSalida.append("<ERROR>");
                                xmlSalida.append(prep.getErrorCampo(FilaPreparadorJustificacionVO.POS_CAMPO_C2TOTAL));
                            xmlSalida.append("</ERROR>");
                        xmlSalida.append("</C2TOTAL>");
                        xmlSalida.append("<C3H>");
                            xmlSalida.append("<VALOR>");
                                xmlSalida.append(prep.getC3h());
                            xmlSalida.append("</VALOR>");
                            xmlSalida.append("<ERROR>");
                                xmlSalida.append(prep.getErrorCampo(FilaPreparadorJustificacionVO.POS_CAMPO_C3H));
                            xmlSalida.append("</ERROR>");
                        xmlSalida.append("</C3H>");
                        xmlSalida.append("<C3M>");
                            xmlSalida.append("<VALOR>");
                                xmlSalida.append(prep.getC3m());
                            xmlSalida.append("</VALOR>");
                            xmlSalida.append("<ERROR>");
                                xmlSalida.append(prep.getErrorCampo(FilaPreparadorJustificacionVO.POS_CAMPO_C3M));
                            xmlSalida.append("</ERROR>");
                        xmlSalida.append("</C3M>");
                        xmlSalida.append("<C3TOTAL>");
                            xmlSalida.append("<VALOR>");
                                xmlSalida.append(prep.getC3Total());
                            xmlSalida.append("</VALOR>");
                            xmlSalida.append("<ERROR>");
                                xmlSalida.append(prep.getErrorCampo(FilaPreparadorJustificacionVO.POS_CAMPO_C3TOTAL));
                            xmlSalida.append("</ERROR>");
                        xmlSalida.append("</C3TOTAL>");
                        xmlSalida.append("<C4H>");
                            xmlSalida.append("<VALOR>");
                                xmlSalida.append(prep.getC4h());
                            xmlSalida.append("</VALOR>");
                            xmlSalida.append("<ERROR>");
                                xmlSalida.append(prep.getErrorCampo(FilaPreparadorJustificacionVO.POS_CAMPO_C4H));
                            xmlSalida.append("</ERROR>");
                        xmlSalida.append("</C4H>");
                        xmlSalida.append("<C4M>");
                            xmlSalida.append("<VALOR>");
                                xmlSalida.append(prep.getC4m());
                            xmlSalida.append("</VALOR>");
                            xmlSalida.append("<ERROR>");
                                xmlSalida.append(prep.getErrorCampo(FilaPreparadorJustificacionVO.POS_CAMPO_C4M));
                            xmlSalida.append("</ERROR>");
                        xmlSalida.append("</C4M>");
                        xmlSalida.append("<C4TOTAL>");
                            xmlSalida.append("<VALOR>");
                                xmlSalida.append(prep.getC4Total());
                            xmlSalida.append("</VALOR>");
                            xmlSalida.append("<ERROR>");
                                xmlSalida.append(prep.getErrorCampo(FilaPreparadorJustificacionVO.POS_CAMPO_C4TOTAL));
                            xmlSalida.append("</ERROR>");
                        xmlSalida.append("</C4TOTAL>");
                         xmlSalida.append("<C5H>");
                            xmlSalida.append("<VALOR>");
                                xmlSalida.append(prep.getC5h());
                            xmlSalida.append("</VALOR>");
                            xmlSalida.append("<ERROR>");
                                xmlSalida.append(prep.getErrorCampo(FilaPreparadorJustificacionVO.POS_CAMPO_C5H));
                            xmlSalida.append("</ERROR>");
                        xmlSalida.append("</C5H>");
                        xmlSalida.append("<C5M>");
                            xmlSalida.append("<VALOR>");
                                xmlSalida.append(prep.getC5m());
                            xmlSalida.append("</VALOR>");
                            xmlSalida.append("<ERROR>");
                                xmlSalida.append(prep.getErrorCampo(FilaPreparadorJustificacionVO.POS_CAMPO_C5M));
                            xmlSalida.append("</ERROR>");
                        xmlSalida.append("</C5M>");
                        xmlSalida.append("<C5TOTAL>");
                            xmlSalida.append("<VALOR>");
                                xmlSalida.append(prep.getC5Total());
                            xmlSalida.append("</VALOR>");
                            xmlSalida.append("<ERROR>");
                                xmlSalida.append(prep.getErrorCampo(FilaPreparadorJustificacionVO.POS_CAMPO_C5TOTAL));
                            xmlSalida.append("</ERROR>");
                        xmlSalida.append("</C5TOTAL>");
                         xmlSalida.append("<C6H>");
                            xmlSalida.append("<VALOR>");
                                xmlSalida.append(prep.getC6h());
                            xmlSalida.append("</VALOR>");
                            xmlSalida.append("<ERROR>");
                                xmlSalida.append(prep.getErrorCampo(FilaPreparadorJustificacionVO.POS_CAMPO_C6H));
                            xmlSalida.append("</ERROR>");
                        xmlSalida.append("</C6H>");
                        xmlSalida.append("<C6M>");
                            xmlSalida.append("<VALOR>");
                                xmlSalida.append(prep.getC6m());
                            xmlSalida.append("</VALOR>");
                            xmlSalida.append("<ERROR>");
                                xmlSalida.append(prep.getErrorCampo(FilaPreparadorJustificacionVO.POS_CAMPO_C6M));
                            xmlSalida.append("</ERROR>");
                        xmlSalida.append("</C6M>");
                        xmlSalida.append("<C6TOTAL>");
                            xmlSalida.append("<VALOR>");
                                xmlSalida.append(prep.getC6Total());
                            xmlSalida.append("</VALOR>");
                            xmlSalida.append("<ERROR>");
                                xmlSalida.append(prep.getErrorCampo(FilaPreparadorJustificacionVO.POS_CAMPO_C6TOTAL));
                            xmlSalida.append("</ERROR>");
                        xmlSalida.append("</C6TOTAL>");
                        xmlSalida.append("<INSERCIONES>");
                            xmlSalida.append("<VALOR>");
                                xmlSalida.append(prep.getInserciones());
                            xmlSalida.append("</VALOR>");
                            xmlSalida.append("<ERROR>");
                                xmlSalida.append(prep.getErrorCampo(FilaPreparadorJustificacionVO.POS_CAMPO_INSERCIONES));
                            xmlSalida.append("</ERROR>");
                        xmlSalida.append("</INSERCIONES>");
                        xmlSalida.append("<SEGUIMIENTOS_INSERCIONES>");
                            xmlSalida.append("<VALOR>");
                                xmlSalida.append(prep.getSeguimientosInserciones());
                            xmlSalida.append("</VALOR>");
                            xmlSalida.append("<ERROR>");
                                xmlSalida.append(prep.getErrorCampo(FilaPreparadorJustificacionVO.POS_CAMPO_SEGUIMIENTOS_INSERCIONES));
                            xmlSalida.append("</ERROR>");
                        xmlSalida.append("</SEGUIMIENTOS_INSERCIONES>");
                        xmlSalida.append("<COSTES_SALARIALES_SS>");
                            xmlSalida.append("<VALOR>");
                                xmlSalida.append(prep.getCostesSalarialesSS());
                            xmlSalida.append("</VALOR>");
                            xmlSalida.append("<ERROR>");
                                xmlSalida.append(prep.getErrorCampo(FilaPreparadorJustificacionVO.POS_CAMPO_COSTES_SALARIALES_SS));
                            xmlSalida.append("</ERROR>");
                        xmlSalida.append("</COSTES_SALARIALES_SS>");
                        xmlSalida.append("<ERRORES>");
                        if(prep.getErrores() != null)
                        {
                            for(int contE = 0; contE < prep.getErrores().size(); contE++)
                            {
                                xmlSalida.append("<ERROR>");
                                    xmlSalida.append(prep.getErrores().get(contE));
                                xmlSalida.append("</ERROR>");
                            }
                        }
                        xmlSalida.append("</ERRORES>");
                        if(prep.esSustituto() != null)
                        {
                            xmlSalida.append("<ES_SUSTITUTO>");
                                xmlSalida.append(prep.esSustituto() ? "1" : "0");
                            xmlSalida.append("</ES_SUSTITUTO>");
                        }
                    xmlSalida.append("</FILA>");
                    i++;
                }
                if(validaciones != null && !validaciones.isEmpty())
                {
                    xmlSalida.append("<VALIDACION>");
                        String validacion = validaciones.get(0);
                        String mensajeCompleto = null;
                        if(validacion != null && !validacion.equals(""))
                        {
                            if(validacion.contains("/"))
                            {
                                String[] array = validacion.split("/");
                                String parte = null;
                                for(int j = 1; j < array.length; j++)
                                {
                                    parte = array[j];
                                    if(mensajeCompleto == null)
                                    {
                                        mensajeCompleto = new String(parte);
                                    }
                                    else
                                    {
                                        mensajeCompleto += ConstantesMeLanbide44.BARRA_SEPARADORA+parte;
                                    }
                                }
                            }
                        }
                        else
                        {
                            mensajeCompleto = MeLanbide44I18n.getInstance().getMensaje(codigoIdioma,"error.datosNoValidos");
                        }
                        xmlSalida.append(mensajeCompleto);
                    xmlSalida.append("</VALIDACION>");
                }
            xmlSalida.append("</RESPUESTA>");
            try{
                response.setContentType("text/xml");
                response.setCharacterEncoding("UTF-8");
                PrintWriter out = response.getWriter();
                out.print(xmlSalida.toString());
                out.flush();
                out.close();
            }catch(Exception e){
                log.error(" Error : " + e.getMessage(), e);
            }//try-catch

    }
    
    public String procesarExcelSeguimientosPrep(int codOrganizacion,  int codTramite, int ocurrenciaTramite,String numExpediente,HttpServletRequest request,HttpServletResponse response)
    {
        int codIdioma = 1;    
        
        try
        {
            UsuarioValueObject usuario = (UsuarioValueObject) request.getSession().getAttribute("usuario");
            if(usuario != null)
            {
                codIdioma = usuario.getIdioma();
            }
        }
        catch(Exception ex)
        {
        }
        
        int ano = MeLanbide44Utils.getEjercicioDeExpediente(numExpediente);
        if (ano<2016){//<2016
            try
            {
                String tipo = (String)request.getParameter("tiposeg");
                MultipartRequestWrapper wrapper = new MultipartRequestWrapper(request);

                wrapper.getParameterMap();
                CommonsMultipartRequestHandler handler = new CommonsMultipartRequestHandler();
                handler.handleRequest(request);
                Hashtable<String, FormFile> table = handler.getFileElements();
                FormFile file = null;
                if (tipo.equals(ConstantesMeLanbide44.TIPO_SEGUIMIENTO_MELANBIDE44))
                    file = table.get("fichero_seguimientos_jus");
                else file = table.get("fichero_inserciones_jus");
                byte[] data = file.getFileData();
                ByteArrayInputStream bais = new ByteArrayInputStream(data);
                HSSFWorkbook workbook = new HSSFWorkbook(bais);

                HSSFSheet sheet = workbook.getSheetAt(0);

                int filaIni = sheet.getFirstRowNum();
                int filaFin = sheet.getLastRowNum();

                HSSFRow row = null;
                List<EcaSegPreparadoresVO> filasImportar = new ArrayList<EcaSegPreparadoresVO>();
                EcaSegPreparadoresVO seg = null;
                int i = 0;
                EcaSolicitudVO sol = MeLanbide44Manager.getInstance().getDatosSolicitud(numExpediente, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                try
                {
                    for(i = filaIni+1; i <= filaFin; i++)
                    {
                        row = sheet.getRow(i);
                        if (tipo.equals("0"))
                            seg = (EcaSegPreparadoresVO)MeLanbide44MappingUtils.getInstance().map(row, EcaSegPreparadoresVO.class);
                        else 
                            seg = (EcaSegPreparadoresVO)MeLanbide44MappingUtils.getInstance().map(row, EcaInsPreparadoresVO.class);
                        if(seg != null )
                        {

                            //validacion = MeLanbide44Utils.validarEcaSegPreparadoresVO(seg,true);
                            /*validacion = MeLanbide44Utils.validarEcaSegPreparadoresVO
                            if(validacion.equals(ConstantesMeLanbide44.OK))
                            {   
                              */
                                EcaJusPreparadoresVO prep = null;
                                if ( tipo.equals("1") && seg.getFecIni()!=null){
                                    prep = MeLanbide44Manager.getInstance().getPreparadorJustificacionPorNIF(sol, seg.getNifPreparador(), seg.getFecIni(), this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                                }else  if ( tipo.equals("0") && seg.getFecIni()!=null){
                                    prep = MeLanbide44Manager.getInstance().getPreparadorJustificacionPorNIF(sol, seg.getNifPreparador(), seg.getFecIni(), this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                                }
                                if(prep != null)
                                {
                                    seg.setJusPreparadoresCod(prep.getJusPreparadoresCod());
                                    filasImportar.add(seg);
                                } else {                            
                                      //throw new ExcelRowMappingException("13");  
                                    ExcelRowMappingException erme = new ExcelRowMappingException("13");
                                    erme.setMensaje(String.format(MeLanbide44I18n.getInstance().getMensaje(codIdioma,"error.NifPreparadorNoEncontrado"), MeLanbide44Utils.fromColNumberToExcelColName(13), i+1));
                                    throw erme;
                                }   
                           /* }
                            else
                            {
                               System.out.println("ERROR excepcion");
                                throw new FilaSeguimientoPrepNoValidaException();
                            }*/
                        }
                    }
                    if(filasImportar != null && filasImportar.size() > 0)
                    {
                        int filasImportadas = 0;
                        if (tipo.equals(ConstantesMeLanbide44.TIPO_SEGUIMIENTO_MELANBIDE44))
                            filasImportadas = MeLanbide44Manager.getInstance().importarSeguimientos(numExpediente, filasImportar, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                        else filasImportadas = MeLanbide44Manager.getInstance().importarInserciones(numExpediente, filasImportar, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));

                        if(filasImportadas < filasImportar.size())
                        {
                            request.getSession().setAttribute("mensajeImportar", MeLanbide44I18n.getInstance().getMensaje(codIdioma, "error.errorImportarGen"));
                        }
                        else
                        {
                            request.getSession().setAttribute("mensajeImportar", MeLanbide44I18n.getInstance().getMensaje(codIdioma, "msg.registrosImportadosOK_1")+" "+filasImportadas+" "+MeLanbide44I18n.getInstance().getMensaje(codIdioma, "msg.registrosImportadosOK_2"));
                        }
                    }
                    else
                    {
                        request.getSession().setAttribute("mensajeImportar", MeLanbide44I18n.getInstance().getMensaje(codIdioma, "error.noHayRegistrosImportar"));
                    }
                }
                catch(ExcelRowMappingException erme)
                {
                    String mensajeImportar = null;
                    if(erme.getMensaje() == null || erme.getMensaje().equals(""))
                    {
                        mensajeImportar = MeLanbide44Utils.crearMensajeDatoExcelNoValido(erme, i+1, codIdioma);
                        if (erme.getCampo().equals("13"))
                            mensajeImportar = mensajeImportar+" "+MeLanbide44I18n.getInstance().getMensaje(codIdioma,"error.NifPreparadorNoEncontrado");                
                        //si campo tipodiscapacodad
                        //else if (erme.getCampo().equals("5"))
                            //mensajeImportar = MeLanbide44I18n.getInstance().getMensaje(codIdioma,"error.TipoDiscapacidad"); 
                        //si campo tipo contrato
                        //else if (erme.getCampo().equals("7"))
                            //mensajeImportar = MeLanbide44I18n.getInstance().getMensaje(codIdioma,"error.TipoContrato"); 
                    }
                    else
                    {
                        mensajeImportar = erme.getMensaje();
                    }
                    request.getSession().setAttribute("mensajeImportar", mensajeImportar);  
                }
            }
            catch(FilaSeguimientoPrepNoValidaException ex)
            {
                log.error(" Error : " + ex.getMessage(), ex);
                request.getSession().setAttribute("mensajeImportar", MeLanbide44I18n.getInstance().getMensaje(codIdioma, "error.datosNoValidosImportar"));
            }
            catch(IOException ex)
            {
                log.error(" Error : " + ex.getMessage(), ex);
                request.getSession().setAttribute("mensajeImportar", MeLanbide44I18n.getInstance().getMensaje(codIdioma, "error.errorProcesarExcel"));
            }
            catch(Exception ex)
            {
                log.error(" Error : " + ex.getMessage(), ex);
                request.getSession().setAttribute("mensajeImportar", MeLanbide44I18n.getInstance().getMensaje(codIdioma, "error.errorImportarGen"));
            }
        }else{//a partir de 2016
            try
            {
                String tipo = (String)request.getParameter("tiposeg");
                MultipartRequestWrapper wrapper = new MultipartRequestWrapper(request);

                wrapper.getParameterMap();
                CommonsMultipartRequestHandler handler = new CommonsMultipartRequestHandler();
                handler.handleRequest(request);
                Hashtable<String, FormFile> table = handler.getFileElements();
                FormFile file = null;
                if (tipo.equals(ConstantesMeLanbide44.TIPO_SEGUIMIENTO_MELANBIDE44))
                    file = table.get("fichero_seguimientos_jus");
                else file = table.get("fichero_inserciones_jus");
                byte[] data = file.getFileData();
                ByteArrayInputStream bais = new ByteArrayInputStream(data);
                HSSFWorkbook workbook = new HSSFWorkbook(bais);

                HSSFSheet sheet = workbook.getSheetAt(0);

                int filaIni = sheet.getFirstRowNum();
                int filaFin = sheet.getLastRowNum();

                HSSFRow row = null;
                List<EcaSegPreparadores2016VO> filasImportar = new ArrayList<EcaSegPreparadores2016VO>();
                EcaSegPreparadores2016VO seg = null;
                int i = 0;
                EcaSolicitudVO sol = MeLanbide44Manager.getInstance().getDatosSolicitud(numExpediente, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                try
                {
                    for(i = filaIni+1; i <= filaFin; i++)
                    {
                        row = sheet.getRow(i);
                        if (tipo.equals("0"))
                            seg = (EcaSegPreparadores2016VO)MeLanbide44MappingUtils.getInstance().map(row, EcaSegPreparadores2016VO.class);
                        else 
                            seg = (EcaSegPreparadores2016VO)MeLanbide44MappingUtils.getInstance().map(row, EcaInsPreparadores2016VO.class);
                        if(seg != null )
                        {

                            
                                EcaJusPreparadoresVO prep = null;
                                if ( tipo.equals("1") && seg.getFecIni()!=null){
                                    prep = MeLanbide44Manager.getInstance().getPreparadorJustificacionPorNIF(sol, seg.getNifPreparador(), seg.getFecIni(), this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                                }else  if ( tipo.equals("0") && seg.getFecIni()!=null){
                                    prep = MeLanbide44Manager.getInstance().getPreparadorJustificacionPorNIF(sol, seg.getNifPreparador(), seg.getFecIni(), this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                                }
                                if(prep != null)
                                {
                                    seg.setJusPreparadoresCod(prep.getJusPreparadoresCod());
                                    filasImportar.add(seg);
                                } else {                            
                                      //throw new ExcelRowMappingException("13");  
                                    ExcelRowMappingException erme = new ExcelRowMappingException("13");
                                    erme.setMensaje(String.format(MeLanbide44I18n.getInstance().getMensaje(codIdioma,"error.NifPreparadorNoEncontrado"), MeLanbide44Utils.fromColNumberToExcelColName(13), i+1));
                                    throw erme;
                                }   
                          
                        }
                    }
                    if(filasImportar != null && filasImportar.size() > 0)
                    {
                        int filasImportadas = 0;
                        if (tipo.equals(ConstantesMeLanbide44.TIPO_SEGUIMIENTO_MELANBIDE44))
                            filasImportadas = MeLanbide44Manager.getInstance().importarSeguimientos2016(numExpediente, filasImportar, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                        else filasImportadas = MeLanbide44Manager.getInstance().importarInserciones2016(numExpediente, filasImportar, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));

                        if(filasImportadas < filasImportar.size())
                        {
                            request.getSession().setAttribute("mensajeImportar", MeLanbide44I18n.getInstance().getMensaje(codIdioma, "error.errorImportarGen"));
                        }
                        else
                        {
                            request.getSession().setAttribute("mensajeImportar", MeLanbide44I18n.getInstance().getMensaje(codIdioma, "msg.registrosImportadosOK_1")+" "+filasImportadas+" "+MeLanbide44I18n.getInstance().getMensaje(codIdioma, "msg.registrosImportadosOK_2"));
                        }
                    }
                    else
                    {
                        request.getSession().setAttribute("mensajeImportar", MeLanbide44I18n.getInstance().getMensaje(codIdioma, "error.noHayRegistrosImportar"));
                    }
                }
                catch(ExcelRowMappingException erme)
                {
                    String mensajeImportar = null;
                    if(erme.getMensaje() == null || erme.getMensaje().equals(""))
                    {
                        mensajeImportar = MeLanbide44Utils.crearMensajeDatoExcelNoValido(erme, i+1, codIdioma);
                        if (erme.getCampo().equals("13"))
                            mensajeImportar = mensajeImportar+" "+MeLanbide44I18n.getInstance().getMensaje(codIdioma,"error.NifPreparadorNoEncontrado");                
                        //si campo tipodiscapacodad
                        //else if (erme.getCampo().equals("5"))
                            //mensajeImportar = MeLanbide44I18n.getInstance().getMensaje(codIdioma,"error.TipoDiscapacidad"); 
                        //si campo tipo contrato
                        //else if (erme.getCampo().equals("7"))
                            //mensajeImportar = MeLanbide44I18n.getInstance().getMensaje(codIdioma,"error.TipoContrato"); 
                    }
                    else
                    {
                        mensajeImportar = erme.getMensaje();
                    }
                    request.getSession().setAttribute("mensajeImportar", mensajeImportar);  
                }
            }
            catch(FilaSeguimientoPrepNoValidaException ex)
            {
                log.error(" Error : " + ex.getMessage(), ex);
                request.getSession().setAttribute("mensajeImportar", MeLanbide44I18n.getInstance().getMensaje(codIdioma, "error.datosNoValidosImportar"));
            }
            catch(IOException ex)
            {
                log.error(" Error : " + ex.getMessage(), ex);
                request.getSession().setAttribute("mensajeImportar", MeLanbide44I18n.getInstance().getMensaje(codIdioma, "error.errorProcesarExcel"));
            }
            catch(Exception ex)
            {
                log.error(" Error : " + ex.getMessage(), ex);
                request.getSession().setAttribute("mensajeImportar", MeLanbide44I18n.getInstance().getMensaje(codIdioma, "error.errorImportarGen"));
            }
        }
        return "/jsp/extension/melanbide44/justificacion/recargarListaSeguimientosPrep.jsp";
    }
    
        
    public String cargarSeguimientos(int codOrganizacion,  int codTramite, int ocurrenciaTramite,String numExpediente,HttpServletRequest request,HttpServletResponse response)
    {
         String url="";
        try
        {
            int idiomaUsuario = 1;
            UsuarioValueObject usuario = (UsuarioValueObject) request.getSession().getAttribute("usuario");
            try
            {
                    if (usuario != null) 
                    {                    
                        idiomaUsuario = usuario.getIdioma();
                    }
            }
            catch(Exception ex)
            {
            }
            String tipo = (String)request.getParameter("tiposeg"); 
            String colectivo = request.getParameter("colectivo")!=null?(String)request.getParameter("colectivo"):""; 
            String sexo = request.getParameter("sexo")!=null?(String)request.getParameter("sexo"):""; 
             if (tipo.equals("0"))
                  url= "/jsp/extension/melanbide44/justificacion/seguimientos.jsp";
             else url= "/jsp/extension/melanbide44/justificacion/inserciones.jsp";
             AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));            
             EcaSolicitudVO sol = MeLanbide44Manager.getInstance().getDatosSolicitud(numExpediente, adapt);
            String idprep = (String)request.getParameter("idPrep");   
            if (idprep!=null){
            
                EcaJusPreparadoresVO prep = new EcaJusPreparadoresVO();
                prep.setJusPreparadoresCod(Integer.parseInt(idprep));
                
                prep = MeLanbide44Manager.getInstance().getPreparadorJustificacionPorId(prep, adapt);
                if(prep != null) {
                     request.setAttribute("preparador", prep);
                    if (tipo.equals("0")){
                        List<FilaSegPreparadoresVO> listaSeguimientos = MeLanbide44Manager.getInstance().getListaSeguimientos(sol, prep, tipo , colectivo, sexo, idiomaUsuario, adapt);
                         if(listaSeguimientos != null)
                        {
                            request.setAttribute("listaSeguimientos", listaSeguimientos);
                            request.setAttribute("colectivo", colectivo);
                            request.setAttribute("sexo", sexo);
                        }
                    }else {
                        List<FilaInsPreparadoresVO> listaInserciones = MeLanbide44Manager.getInstance().getListaInserciones(sol, prep, tipo , colectivo, sexo, idiomaUsuario, adapt);
                        if(listaInserciones != null)
                           {
                               request.setAttribute("listaSeguimientos", listaInserciones);
                               request.setAttribute("colectivo", colectivo);
                               request.setAttribute("sexo", sexo);
                           }
                    }
                 }
            }else {
                if (tipo.equals("0")){
                List<FilaSegPreparadoresVO> listaSeguimientos = MeLanbide44Manager.getInstance().getListaSeguimientos(sol, null, tipo , colectivo, sexo, idiomaUsuario, adapt);
                if(listaSeguimientos != null)
                    {
                        request.setAttribute("listaSeguimientos", listaSeguimientos);
                    }
                }else {
                        List<FilaInsPreparadoresVO> listaInserciones = MeLanbide44Manager.getInstance().getListaInserciones(sol, null, tipo , colectivo, sexo, idiomaUsuario, adapt);
                        if(listaInserciones != null)
                    {
                        request.setAttribute("listaSeguimientos", listaInserciones);
                    }
                }
            }
           
            String opcion = (String)request.getParameter("opcion");
            if(opcion.equalsIgnoreCase("consultar"))
            {
                request.setAttribute("consulta", true);
            }
        }
        catch(Exception ex)
        {
            log.error(ex.getMessage());
        }
        return url;
    }
    
    public void getListaPreparadoresJustificacion(int codOrganizacion,  int codTramite, int ocurrenciaTramite,String numExpediente,HttpServletRequest request,HttpServletResponse response)
    {
        try
        {
            try
            {
                //Al importar un fichero, se inserta este mensaje en sesion como resultado
                //Justo despues de importar se llama a este metodo para recargar la tabla.
                //Este es el punto donde se borra este mensaje de sesion, para no mantenerlo en memoria innecesariamente.
                request.getSession().removeAttribute("mensajeImportar");
            }
            catch(Exception ex)
            {
                
            }
        
            int codIdioma = 1;    
            try
            {
                UsuarioValueObject usuario = (UsuarioValueObject) request.getSession().getAttribute("usuario");
                if(usuario != null)
                {
                    codIdioma = usuario.getIdioma();
                }
            }
            catch(Exception ex)
            {

            }
            
            AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
            MeLanbide44Manager meLanbide44Manager = MeLanbide44Manager.getInstance();
            EcaSolicitudVO sol = meLanbide44Manager.getDatosSolicitud(numExpediente, adapt);
            List<FilaPreparadorJustificacionVO> preparadores = null;
            
            if(sol != null)
            {
                preparadores = MeLanbide44Manager.getInstance().getListaPreparadoresJustificacion(sol, adapt, codIdioma);
            }
            else
            {
                preparadores = new ArrayList<FilaPreparadorJustificacionVO>();
            }
            
            String codigoOperacion = "0";
            
            this.escribirListaPreparadoresJusRequest(codigoOperacion, preparadores, null, codIdioma, response);
            
        }
        catch(Exception ex)
        {
            log.error(" Error : " + ex.getMessage(), ex);
        }
    }
    
    public void cargarComboProcesos(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response)
    {
            
        int codIdioma = 1;
        
        UsuarioValueObject usuario = (UsuarioValueObject) request.getSession().getAttribute("usuario");
        if(usuario != null)
        {
            codIdioma = usuario.getIdioma();
        }
        
        List<SelectItem> listaProcesos = new ArrayList<SelectItem>();
        try
        {
            SelectItem si = null;
            
            si = new SelectItem();
            
            si.setId(ConstantesMeLanbide44.COD_PROC_RESOLUCION_PROV);
            si.setLabel(MeLanbide44Utils.obtenerNombreProceso(ConstantesMeLanbide44.COD_PROC_RESOLUCION_PROV, codIdioma));
            listaProcesos.add(si);
            
            si = new SelectItem();
            si.setId(ConstantesMeLanbide44.COD_PROC_DOC_RESOLUCION);
            si.setLabel(MeLanbide44Utils.obtenerNombreProceso(ConstantesMeLanbide44.COD_PROC_DOC_RESOLUCION, codIdioma));
            listaProcesos.add(si);
            
            si = new SelectItem();
            si.setId(ConstantesMeLanbide44.COD_PROC_CONSOLIDAR);
            si.setLabel(MeLanbide44Utils.obtenerNombreProceso(ConstantesMeLanbide44.COD_PROC_CONSOLIDAR, codIdioma));
            listaProcesos.add(si);
            
            si = new SelectItem();
            si.setId(ConstantesMeLanbide44.COD_PROC_DESHACER_CONSOLIDACION);
            si.setLabel(MeLanbide44Utils.obtenerNombreProceso(ConstantesMeLanbide44.COD_PROC_DESHACER_CONSOLIDACION, codIdioma));
            listaProcesos.add(si);
        }
        catch(Exception ex)
        {
            
        }
        StringBuffer xmlSalida = new StringBuffer();
        xmlSalida.append("<RESPUESTA>");
            xmlSalida.append("<CODIGO_OPERACION>");
                xmlSalida.append("0");
            xmlSalida.append("</CODIGO_OPERACION>");
            for(SelectItem si : listaProcesos)
            {
                xmlSalida.append("<SELECT_ITEM>");
                    xmlSalida.append("<ID>");
                        xmlSalida.append(si.getId());
                    xmlSalida.append("</ID>");
                    xmlSalida.append("<LABEL>");
                        xmlSalida.append(si.getLabel());
                    xmlSalida.append("</LABEL>");
                xmlSalida.append("</SELECT_ITEM>");
            }
        xmlSalida.append("</RESPUESTA>");
        try{
            response.setContentType("text/xml");
            response.setCharacterEncoding("UTF-8");
            PrintWriter out = response.getWriter();
            out.print(xmlSalida.toString());
            out.flush();
            out.close();
        }catch(Exception e){
            log.error(" Error : " + e.getMessage(), e);
        }//try-catch
    }
    
    public void filtrarAuditoriaProcesos(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response)
    {
        List<FilaAuditoriaProcesosVO> audList = new ArrayList<FilaAuditoriaProcesosVO>();
        List<FilaAuditoriaProcesosVO> tempList = new ArrayList<FilaAuditoriaProcesosVO>();
        try
        {
            String p1 = (String)request.getParameter("pagAct");
            String p2 = (String)request.getParameter("maxFilas");
            
            String nombre = (String)request.getParameter("nomApellidos");
            String p3 = (String)request.getParameter("feDesde");
            String p4 = (String)request.getParameter("feHasta");
            String p5 = (String)request.getParameter("codProc");
            
            SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
            
            Date feDesde = null;
            if(p3 != null && !p3.equals(""))
            {
                try
                {
                    feDesde = format.parse(p3);
                    Calendar cal = new GregorianCalendar();
                    cal.setTime(feDesde);
                    cal.set(Calendar.HOUR, 0);
                    cal.set(Calendar.MINUTE, 0);
                    cal.set(Calendar.SECOND, 0);
                    feDesde = cal.getTime();
                }
                catch(Exception ex)
                {
                    
                }
            }
            Date feHasta = null;
            if(p4 != null && !p4.equals(""))
            {
                try
                {
                    feHasta = format.parse(p4);
                    Calendar cal = new GregorianCalendar();
                    cal.setTime(feHasta);
                    cal.set(Calendar.HOUR, 23);
                    cal.set(Calendar.MINUTE, 59);
                    cal.set(Calendar.SECOND, 59);
                    feHasta = cal.getTime();
                }
                catch(Exception ex)
                {
                    
                }
            }
            
            Integer codProc = null;
            if(p5 != null && !p5.equals(""))
            {
                codProc = Integer.parseInt(p5);
            }
            
            Integer act = null;
            Integer max = null;
            try
            {
                act = Integer.parseInt(p1);
                max = Integer.parseInt(p2);
            }
            catch(Exception ex)
            {
                act = 0;
                max = 0;
            }
            
            int codIdioma = 1;
        
            UsuarioValueObject usuario = (UsuarioValueObject) request.getSession().getAttribute("usuario");
            if(usuario != null)
            {
                codIdioma = usuario.getIdioma();
            }
            
            tempList = MeLanbide44Manager.getInstance().filtrarAuditoriaProcesos(nombre, feDesde, feHasta, codProc, codIdioma, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
            
            int desde = act-1;
            desde = desde*max;
            if(desde < 0)
            {
                desde = 0;
            }
            int hasta = desde + max;
            if(hasta > tempList.size())
            {
                hasta = tempList.size();
            }
            
            for(int i = desde; i < hasta; i++)
            {
                audList.add(tempList.get(i));
            }
        }
        catch(Exception ex)
        {
            
        }
        StringBuffer xmlSalida = new StringBuffer();
        xmlSalida.append("<RESPUESTA>");
            xmlSalida.append("<CODIGO_OPERACION>");
                xmlSalida.append("0");
            xmlSalida.append("</CODIGO_OPERACION>");
            xmlSalida.append("<TOTAL_REGISTROS>");
                xmlSalida.append(tempList.size());
            xmlSalida.append("</TOTAL_REGISTROS>");
            for(FilaAuditoriaProcesosVO fila : audList)
            {
                xmlSalida.append("<REGISTRO>");
                    xmlSalida.append("<NOMAPELLIDOS>");
                        xmlSalida.append(fila.getNomApellidos());
                    xmlSalida.append("</NOMAPELLIDOS>");
                    xmlSalida.append("<PROCESO>");
                        xmlSalida.append(fila.getProceso());
                    xmlSalida.append("</PROCESO>");
                    xmlSalida.append("<FECHA>");
                        xmlSalida.append(fila.getFecha());
                    xmlSalida.append("</FECHA>");
                    xmlSalida.append("<RESULTADO>");
                        xmlSalida.append(fila.getResultado());
                    xmlSalida.append("</RESULTADO>");
                xmlSalida.append("</REGISTRO>");
            }
        xmlSalida.append("</RESPUESTA>");
        try{
            response.setContentType("text/xml");
            response.setCharacterEncoding("UTF-8");
            PrintWriter out = response.getWriter();
            out.print(xmlSalida.toString());
            out.flush();
            out.close();
        }catch(Exception e){
            log.error(" Error : " + e.getMessage(), e);
        }//try-catch
    }
    
    public void generarInformeDesglose(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response)
    {
        try
        {
            String ano = (String)request.getParameter("ano");
            String formato = (String)request.getParameter("formato");
            
            int codIdioma = 1; 
            try
            {
                UsuarioValueObject usuario = (UsuarioValueObject) request.getSession().getAttribute("usuario");
                if(usuario != null)
                {
                    codIdioma = usuario.getIdioma();
                }
            }
            catch(Exception ex)
            {

            }
            
            
            List<FilaInformeDesglose> filas = MeLanbide44Manager.getInstance().getDatosInformeDesglose(ano, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
            if(formato!= null)
            {
                if(formato.equalsIgnoreCase("pdf") || formato.equalsIgnoreCase("word"))
                {
                    MeLanbide44InformeUtils.getInstance().generarInformeDesglosePDF(filas, ano, formato, codIdioma, response);
                }
                else if(formato.equalsIgnoreCase("excel"))
                {
                    MeLanbide44InformeUtils.getInstance().generarInformeDesgloseExcel(filas, ano, codIdioma, response);
                }
            }
        }
        catch(Exception ex)
        {
            log.error(" Error : " + ex.getMessage(), ex);
        }
    }
    
    public void generarInformeProyectos(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response)
    {
        try
        {
            String ano = (String)request.getParameter("ano");
            String formato = (String)request.getParameter("formato");
            
            int codIdioma = 1; 
            try
            {
                UsuarioValueObject usuario = (UsuarioValueObject) request.getSession().getAttribute("usuario");
                if(usuario != null)
                {
                    codIdioma = usuario.getIdioma();
                }
            }
            catch(Exception ex)
            {

            }
            
            AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
            
            MeLanbide44Manager manager = MeLanbide44Manager.getInstance();
            
            List<FilaResumenInformeProyectos> filasResumen = manager.getDatosResumenInformeProyectos(ano, adapt);
            
            HashMap<String, List<FilaInformeProyectos>> filasPorExpediente = new HashMap<String, List<FilaInformeProyectos>>();
            
            for(FilaResumenInformeProyectos fila : filasResumen)
            {
                filasPorExpediente.put(fila.getNumExp(), manager.getDatosInformeProyectos(fila.getNumExp(), adapt));
            }
                    
            MeLanbide44Manager.getInstance().getDatosInformeProyectos(ano, adapt);
            
            if(formato != null)
            {
                if(formato.equalsIgnoreCase("pdf") || formato.equalsIgnoreCase("word"))
                {
                    MeLanbide44InformeUtils.getInstance().generarInformeProyectosPDF(filasResumen, filasPorExpediente, ano, formato, codIdioma, response);
                }
                else if(formato.equalsIgnoreCase("excel"))
                {
                    MeLanbide44InformeUtils.getInstance().generarInformeProyectosExcel(filasResumen, filasPorExpediente, ano, codIdioma, response);
                }
            }
        }
        catch(Exception ex)
        {
            log.error(" Error : " + ex.getMessage(), ex);
        }
    }
    
    
    public String cargarNuevoSeguimientoPreparador(int codOrganizacion,  int codTramite, int ocurrenciaTramite,String numExpediente,HttpServletRequest request,HttpServletResponse response)
    {
        
        int codIdioma = 1;
        
        UsuarioValueObject usuario = (UsuarioValueObject) request.getSession().getAttribute("usuario");
        if(usuario != null)
        {
            codIdioma = usuario.getIdioma();
        }
        try
        {
            
            String sexo = (String)request.getParameter("sexo");
            String colectivo = (String)request.getParameter("colectivo");
            String preparador = (String)request.getParameter("preparador");
            String tiposeg = (String)request.getParameter("tiposeg");
            if (tiposeg!=null){
                request.setAttribute("tiposeg", tiposeg);
            }
            String opcion = (String)request.getParameter("opcion");
            if(opcion != null)
            {
                if(opcion.equalsIgnoreCase("nuevo"))
                {
                    //Cargar nuevo preparador
                }
                else if(opcion.equalsIgnoreCase("modificar") || opcion.equalsIgnoreCase("consultar"))
                {
                    //Cargar modificar preparador
                    String idSeg = (String)request.getParameter("idSegModificar");
                    if(idSeg != null)
                    {
                        
                        String[] datos = idSeg.split("_");
                        
                        EcaSegPreparadoresVO seg = new EcaSegPreparadoresVO();
                        seg.setJusPreparadoresCod(Integer.parseInt(datos[0]));
                        seg.setSegPreparadoresCod(Integer.parseInt(datos[1]));                        
                        seg = MeLanbide44Manager.getInstance().getSeguimientoPreparadorPorId(seg, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                        if(seg != null)
                        {
                            request.setAttribute("seguimientoModif", seg);
                        }
                    }
                    if(opcion.equalsIgnoreCase("consultar"))
                    {
                        request.setAttribute("consulta", true);
                    }
                                  
                   
                }
                List<SelectItem> listaTipoContrato =  MeLanbide44Manager.getInstance().getListaTipoContrato( codIdioma, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                List<SelectItem> listaTipoDiscapacidad =  MeLanbide44Manager.getInstance().getListaTipoDiscapacidad( codIdioma, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                List<SelectItem> listaGravedad =  MeLanbide44Manager.getInstance().getListaGravedad( codIdioma, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));

                request.setAttribute("lstTipoContrato", listaTipoContrato);
                request.setAttribute("lstTipoDiscapacidad", listaTipoDiscapacidad);
                request.setAttribute("lstGravedad", listaGravedad);     
                request.setAttribute("colectivo", colectivo);     
                request.setAttribute("sexo", sexo);     
                request.setAttribute("preparador", preparador);    
                if (preparador !=null){
                    EcaJusPreparadoresVO prep = new EcaJusPreparadoresVO();
                    prep.setJusPreparadoresCod(Integer.parseInt(preparador));
                    prep = MeLanbide44Manager.getInstance().getPreparadorJustificacionPorId(prep, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                    request.setAttribute("nifprep", prep.getNif());    
                    log.debug("nifpreparador:"+prep.getNif());
                }
            }
        }
        catch(Exception ex)
        {
            
        }   
        return "/jsp/extension/melanbide44/justificacion/nuevoSeguimientoPreparador.jsp";
    }
    
    public void guardarSeguimientoPreparador(int codOrganizacion,  int codTramite, int ocurrenciaTramite,String numExpediente,HttpServletRequest request,HttpServletResponse response)
    {
        int codIdioma = 1;    
        try
        {
            UsuarioValueObject usuario = (UsuarioValueObject) request.getSession().getAttribute("usuario");
            if(usuario != null)
            {
                codIdioma = usuario.getIdioma();
            }
        }
        catch(Exception ex)
        {

        }
        
        String codigoOperacion = "0";
        List<FilaSegPreparadoresVO> seguimientos = null;
        List<FilaInsPreparadoresVO> inserciones = null;
        List<String> validaciones = new ArrayList<String>();
        try
        {
            AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
            String tiposeg ="0";
            String sexosegs="";
            String colectivosegs="";
            EcaJusPreparadoresVO prepseg=null;
            EcaSolicitudVO sol = MeLanbide44Manager.getInstance().getDatosSolicitud(numExpediente, adapt);
            if(sol != null && sol.getSolicitudCod() != null)
            {
                    
                SimpleDateFormat format = new SimpleDateFormat(ConstantesMeLanbide44.FORMATO_FECHA);
                //Recojo los parámetros
                String idPrep = (String)request.getParameter("idPrep");
                String idSeg = (String)request.getParameter("idSeg");
                String nif = (String)request.getParameter("nif");
                String nomApel = (String)request.getParameter("nomApel");
                String feIni = (String)request.getParameter("feIni");
                String feFin = (String)request.getParameter("feFin");                
                String horasContrato = (String)request.getParameter("horasCont");
                String fecNacimiento = (String)request.getParameter("fecNacimiento");
                String sexo = (String)request.getParameter("sexo");
                String tipoDiscap = (String)request.getParameter("tipoDiscap");
                String gravedad = (String)request.getParameter("gravedad");
                String tipoCont = (String)request.getParameter("tipoCont");
                String porcJorn = (String)request.getParameter("porcJorn");
                String finContratoDespido = (String)request.getParameter("finContratoDespido");
                String empresa = (String)request.getParameter("empresa");                
                //String perscont = (String)request.getParameter("perscont");
                String fecSeguimiento = (String)request.getParameter("fecSeguimiento");
                String observaciones = (String)request.getParameter("observaciones");
                tiposeg = (String)request.getParameter("tiposeg");
                String nifprep = (String)request.getParameter("nifPreparador");
                
                sexosegs = (String)request.getParameter("sexosegs");
                colectivosegs = (String)request.getParameter("colectivosegs");
                String idprepsegs = (String)request.getParameter("prepsegs");
                if (idprepsegs!=null && !idprepsegs.equals("")){
                    prepseg=new EcaJusPreparadoresVO();
                    prepseg.setJusPreparadoresCod(Integer.parseInt(idprepsegs));
                    prepseg=MeLanbide44Manager.getInstance().getPreparadorJustificacionPorId(prepseg, adapt);
                }

                EcaSegPreparadoresVO seg = null;
                boolean soloFormato = false;
                if(idPrep != null && !idPrep.equals("") && idSeg != null && !idSeg.equals(""))
                {
                    seg = new EcaSegPreparadoresVO();
                    seg.setJusPreparadoresCod(Integer.parseInt(idPrep));
                    seg.setSegPreparadoresCod(Integer.parseInt(idSeg));
                    seg = MeLanbide44Manager.getInstance().getSeguimientoPreparadorPorId(seg, adapt);
                    soloFormato = true;
                }
                else
                {
                    seg = new EcaSegPreparadoresVO();
                    if(prepseg == null)
                    {
                        //EcaJusPreparadoresVO prep = MeLanbide44Manager.getInstance().getPreparadorJustificacionPorNIF(sol, nifprep.toUpperCase(),tiposeg.equals("0")?format.parse( fecSeguimiento):format.parse(feIni), this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                        EcaJusPreparadoresVO prep = MeLanbide44Manager.getInstance().getPreparadorJustificacionPorNIF(sol, nifprep.toUpperCase(),tiposeg.equals("0")?format.parse( feIni):format.parse(feIni), this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                   
                        if (prep!=null)   
                            seg.setJusPreparadoresCod(prep.getJusPreparadoresCod());  
                    }
                    else
                    {
                        seg.setJusPreparadoresCod(prepseg.getJusPreparadoresCod());
                    }
                   
                }
                if(seg == null)
                {
                    codigoOperacion = "3";
                }
                else
                {
                    /*seg.setNif(nif != null ? nif.toUpperCase() : null);
                    seg.setNombre(nomApel != null ? nomApel.toUpperCase() : null);                    
                    seg.setFecFin(feFin != null && !feFin.equals("") ? format.parse(feFin) : null);
                    seg.setFecIni(feIni != null && !feIni.equals("") ? format.parse(feIni) : null);
                    seg.setHorasCont(horasContrato != null && !horasContrato.equals("") ? new BigDecimal(horasContrato.replaceAll(",", "\\.")) : null);
                    seg.setFecNacimiento(fecNacimiento != null && !fecNacimiento.equals("") ? format.parse(fecNacimiento) : null);
                    seg.setSexo(sexo != null && !sexo.equals("") ? Integer.parseInt(sexo) : null);
                    seg.setTipoDiscapacidad(tipoDiscap != null && !tipoDiscap.equals("") ? Integer.parseInt(tipoDiscap) : null);
                    seg.setGravedad(gravedad != null && !gravedad.equals("") ? Integer.parseInt(gravedad) : null);
                    seg.setTipoContrato(tipoCont != null && !tipoCont.equals("") ? Integer.parseInt(tipoCont) : null);
                    seg.setPorcJornada(porcJorn != null && !porcJorn.equals("") ? new BigDecimal(porcJorn.replaceAll(",", "\\.")) : null);
                    seg.setEmpresa(empresa != null ? empresa.toUpperCase() : null);   
                    seg.setNomPersContacto(perscont != null ? perscont.toUpperCase() : null);   
                    seg.setFecSeguimiento(fecSeguimiento != null && !fecSeguimiento.equals("") ? format.parse(fecSeguimiento) : null);
                    seg.setObservaciones(observaciones != null && !observaciones.equals("") ? observaciones.toUpperCase() : null);
                    seg.setTipo(tiposeg != null && !tiposeg.equals("") ? Integer.parseInt(tiposeg.toUpperCase()) : null);
                                        
                    
                    List<Integer> listaTipoContrato =  MeLanbide44Manager.getInstance().getListaCodigosTipoContrato(this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                    List<Integer> listaTipoDiscapacidad =  MeLanbide44Manager.getInstance().getListaCodigosTipoDiscapacidad(this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                    List<Integer> listaGravedad =  MeLanbide44Manager.getInstance().getListaCodigosGravedad(this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                    
                    EcaConfiguracionVO config = MeLanbide44Manager.getInstance().getConfiguracionEca(MeLanbide44Utils.getEjercicioDeExpediente(numExpediente), adapt);
                    List<String> validacion = MeLanbide44Utils.validarEcaSegPreparadoresVO(sol.getNumExp(), seg, config, codIdioma, listaTipoDiscapacidad, listaGravedad,listaTipoContrato, soloFormato);
                    if(validacion == null || validacion.size() == 0)
                    {
                        MeLanbide44Manager.getInstance().guardarEcaSegPreparadoresVO(seg, adapt);
                    }
                    else
                    {
                        if (seg.getJusPreparadoresCod()!=null)
                        codigoOperacion = "4";
                        else codigoOperacion = "5";
                    }*/
                    
                    if (seg.getJusPreparadoresCod()!=null)
                    {
                        seg.setNif(nif != null ? nif.toUpperCase() : null);
                        seg.setNombre(nomApel != null ? nomApel.toUpperCase() : null);                    
                        seg.setFecFin(feFin != null && !feFin.equals("") ? format.parse(feFin) : null);
                        seg.setFecIni(feIni != null && !feIni.equals("") ? format.parse(feIni) : null);
                        seg.setHorasCont(horasContrato != null && !horasContrato.equals("") ? new BigDecimal(horasContrato.replaceAll(",", "\\.")) : null);
                        seg.setFecNacimiento(fecNacimiento != null && !fecNacimiento.equals("") ? format.parse(fecNacimiento) : null);
                        seg.setSexo(sexo != null && !sexo.equals("") ? Integer.parseInt(sexo) : null);
                        seg.setTipoDiscapacidad(tipoDiscap != null && !tipoDiscap.equals("") ? Integer.parseInt(tipoDiscap) : null);
                        seg.setGravedad(gravedad != null && !gravedad.equals("") ? Integer.parseInt(gravedad) : null);
                        seg.setTipoContrato(tipoCont != null && !tipoCont.equals("") ? Integer.parseInt(tipoCont) : null);
                        seg.setPorcJornada(porcJorn != null && !porcJorn.equals("") ? new BigDecimal(porcJorn.replaceAll(",", "\\.")) : null);
                        seg.setFinContratoDespido(finContratoDespido != null && !finContratoDespido.equals("") ? Integer.valueOf(finContratoDespido) : null);
                        seg.setEmpresa(empresa != null ? empresa.toUpperCase() : null);   
                        //seg.setNomPersContacto(perscont != null ? perscont.toUpperCase() : null);   
                        if (tiposeg.equals("0"))
                            seg.setFecSeguimiento(fecSeguimiento != null && !fecSeguimiento.equals("") ? format.parse(fecSeguimiento) : null);
                        seg.setObservaciones(observaciones != null && !observaciones.equals("") ? observaciones.toUpperCase() : null);
                        seg.setTipo(tiposeg != null && !tiposeg.equals("") ? Integer.parseInt(tiposeg.toUpperCase()) : null);


                        List<Integer> listaTipoContrato =  MeLanbide44Manager.getInstance().getListaCodigosTipoContrato(this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                        List<Integer> listaTipoDiscapacidad =  MeLanbide44Manager.getInstance().getListaCodigosTipoDiscapacidad(this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                        List<Integer> listaGravedad =  MeLanbide44Manager.getInstance().getListaCodigosGravedad(this.getAdaptSQLBD(String.valueOf(codOrganizacion)));

                        EcaConfiguracionVO config = MeLanbide44Manager.getInstance().getConfiguracionEca(MeLanbide44Utils.getEjercicioDeExpediente(numExpediente), adapt);
                        validaciones = MeLanbide44Utils.validarEcaSegPreparadoresVO(sol.getNumExp(), seg, config, codIdioma, listaTipoDiscapacidad, listaGravedad,listaTipoContrato, soloFormato, false);
                        if(validaciones == null || validaciones.size() == 0)
                        {
                            MeLanbide44Manager.getInstance().guardarEcaSegPreparadoresVO(seg, adapt);
                        }
                        else
                        {
                            codigoOperacion = "4";
                        }
                    }
                    else
                    {
                        codigoOperacion = "5";
                    }
                }
            }
            else
            {
                codigoOperacion = "1";
            }
           
           if (tiposeg.equals("0")) 
           {
                seguimientos = MeLanbide44Manager.getInstance().getListaSeguimientos(sol,prepseg , tiposeg, colectivosegs, sexosegs, codIdioma, adapt);
           }
           else 
           {
                inserciones = MeLanbide44Manager.getInstance().getListaInserciones(sol, prepseg, tiposeg, colectivosegs, sexosegs, codIdioma, adapt);
           }
            
        }
        catch(Exception ex)
        {
            log.error(" Error : " + ex.getMessage(), ex);
            codigoOperacion = "2";
        }
        
        if (seguimientos !=null)
        {
            this.escribirListaSegPreparadoresRequest(codigoOperacion, seguimientos, validaciones, codIdioma, response);
        }
        else  
        {
            this.escribirListaInsPreparadoresRequest(codigoOperacion, inserciones, validaciones, codIdioma, response);
        }
    }
    
    public void descargarPlantilla(int codOrganizacion,  int codTramite, int ocurrenciaTramite,String numExpediente,HttpServletRequest request,HttpServletResponse response)
    {
        try
        {
            String nombrePlantilla = request.getParameter("nombrePlantilla");
            String urlArchivo = "/es/altia/flexia/integracion/moduloexterno/melanbide44/plantillas/"+nombrePlantilla;
            
          /*  //MODIFICAR CABECERA            
            FileInputStream file;
            file = new FileInputStream(new File(urlArchivo));
            //Get the workbook instance for XLS file 
            HSSFWorkbook workbook = new HSSFWorkbook(file);
            
             //Get first sheet from the workbook
            HSSFSheet sheet = workbook.getSheetAt(0);

            //Pongo el nombre de la ubicacion del centro logeado
            HSSFRow row = sheet.getRow(0);
            HSSFCell cell = row.getCell(10);                       
            cell.setCellValue("nº seg anteriores a "+MeLanbide44Utils.getEjercicioDeExpediente(numExpediente));
            
            file.close();
                BufferedInputStream bstr = new BufferedInputStream( file ); // promote
                File directorioTemp = new File(m_Conf.getString("PDF.base_dir"));
                File informe = File.createTempFile("plantilla", ".xls", directorioTemp);
                int size = (int) informe.length(); 
                byte[] data = new byte[size]; 
                bstr.read( data, 0, size ); 
                bstr.close();

                response.setContentType("application/vnd.ms-excel");
                response.setHeader("Content-Disposition", "inline; filename=" + informe.getName());
                response.setHeader("Content-Transfer-Encoding", "binary");  
                response.setContentLength(data.length);
                response.getOutputStream().write(data, 0, data.length);
                response.getOutputStream().flush();
                response.getOutputStream().close();            
            */
            InputStream is = getClass().getClassLoader().getResourceAsStream(urlArchivo);
            
            byte[] data = IOUtils.toByteArray(is); 

            response.setHeader("Cache-Control","must-revalidate, post-check=0, pre-check=0");
            response.setHeader("Pragma","public");
            response.setHeader ("Content-Type", "application/octet-stream");
            response.setHeader ("Content-Disposition", "attachment; filename="+nombrePlantilla);

            response.setContentLength(data.length);
            response.getOutputStream().write(data, 0, data.length);
            response.getOutputStream().flush();
            response.getOutputStream().close();
        }
        catch(Exception ex)
        {
            log.error(" Error : " + ex.getMessage(), ex);
        }
    }
    
    private void escribirListaSegPreparadoresRequest(String codigoOperacion, List<FilaSegPreparadoresVO> seguimientos, List<String> validaciones, int codigoIdioma, HttpServletResponse response)
    {
        StringBuffer xmlSalida = new StringBuffer();
            xmlSalida.append("<RESPUESTA>");
                xmlSalida.append("<CODIGO_OPERACION>");
                    xmlSalida.append(codigoOperacion);
                xmlSalida.append("</CODIGO_OPERACION>");
                int i = 0;
                for(FilaSegPreparadoresVO seg : seguimientos)
                {
                    xmlSalida.append("<FILA>");                    
                        xmlSalida.append("<ID>");
                            xmlSalida.append("<VALOR>");
                                xmlSalida.append(seg.getCodPreparador()+"_"+seg.getIdseg());
                            xmlSalida.append("</VALOR>");
                            xmlSalida.append("<ERROR>");
                                xmlSalida.append(ConstantesMeLanbide44.FALSO);
                            xmlSalida.append("</ERROR>");
                        xmlSalida.append("</ID>");
                        xmlSalida.append("<NIF>");
                            xmlSalida.append("<VALOR>");
                                xmlSalida.append(seg.getNif());
                            xmlSalida.append("</VALOR>");
                            xmlSalida.append("<ERROR>");
                                if (seg.getTipo().equals("0")) 
                                    xmlSalida.append(seg.getErrorCampo(FilaSegPreparadoresVO.POS_CAMPO_NIF));
                                else xmlSalida.append(seg.getErrorCampo(FilaInsPreparadoresVO.POS_CAMPO_NIF));
                            xmlSalida.append("</ERROR>");
                        xmlSalida.append("</NIF>");
                        xmlSalida.append("<NOMBRE>");
                            xmlSalida.append("<VALOR>");
                                xmlSalida.append(seg.getNombreApe());
                            xmlSalida.append("</VALOR>");
                            xmlSalida.append("<ERROR>");
                                if (seg.getTipo().equals("0"))     
                                    xmlSalida.append(seg.getErrorCampo(FilaSegPreparadoresVO.POS_CAMPO_NOMBREAPEL));
                                else  xmlSalida.append(seg.getErrorCampo(FilaInsPreparadoresVO.POS_CAMPO_NOMBREAPEL));
                            xmlSalida.append("</ERROR>");
                        xmlSalida.append("</NOMBRE>");
                        
 /*                       if (seg.getTipo().equals("0")) {
                            xmlSalida.append("<FECHA_SEGUIMIENTO>");
                                xmlSalida.append("<VALOR>");
                                    xmlSalida.append(seg.getFecSeguimiento());
                                xmlSalida.append("</VALOR>");
                                xmlSalida.append("<ERROR>");
                                    if (seg.getTipo().equals("0"))   
                                        xmlSalida.append(seg.getErrorCampo(FilaSegPreparadoresVO.POS_CAMPO_FECHA_SEGUIMIENTO));
                                xmlSalida.append("</ERROR>");                           
                            xmlSalida.append("</FECHA_SEGUIMIENTO>");
                        }
 */                       
                        xmlSalida.append("<FECHA_NACIMIENTO>");
                            xmlSalida.append("<VALOR>");    
                                xmlSalida.append(seg.getFecNacimiento());
                            xmlSalida.append("</VALOR>");
                            xmlSalida.append("<ERROR>");
                                if (seg.getTipo().equals("0"))   
                                   xmlSalida.append(seg.getErrorCampo(FilaSegPreparadoresVO.POS_CAMPO_FECHA_NACIMIENTO));
                                else xmlSalida.append(seg.getErrorCampo(FilaInsPreparadoresVO.POS_CAMPO_FECHA_NACIMIENTO));
                            xmlSalida.append("</ERROR>"); 
                        xmlSalida.append("</FECHA_NACIMIENTO>");
                        xmlSalida.append("<SEXO>");
                            xmlSalida.append("<VALOR>");   
                                xmlSalida.append(seg.getSexo());
                            xmlSalida.append("</VALOR>");
                            xmlSalida.append("<ERROR>");
                                if (seg.getTipo().equals("0"))   
                                   xmlSalida.append(seg.getErrorCampo(FilaSegPreparadoresVO.POS_CAMPO_SEXO));
                                else xmlSalida.append(seg.getErrorCampo(FilaInsPreparadoresVO.POS_CAMPO_SEXO));
                            xmlSalida.append("</ERROR>");
                        xmlSalida.append("</SEXO>");
                        xmlSalida.append("<TIPO_DISCAPACIDAD>");
                            xmlSalida.append("<VALOR>");   
                                xmlSalida.append(seg.getTipoDiscapacidad());
                            xmlSalida.append("</VALOR>");
                            xmlSalida.append("<ERROR>");
                                     if (seg.getTipo().equals("0"))   
                                        xmlSalida.append(seg.getErrorCampo(FilaSegPreparadoresVO.POS_CAMPO_TIPODISCAPACIDAD));
                                     else xmlSalida.append(seg.getErrorCampo(FilaInsPreparadoresVO.POS_CAMPO_TIPODISCAPACIDAD));
                            xmlSalida.append("</ERROR>");
                        xmlSalida.append("</TIPO_DISCAPACIDAD>");
                        xmlSalida.append("<GRAVEDAD>");
                            xmlSalida.append("<VALOR>");   
                                xmlSalida.append(seg.getGravedad());
                            xmlSalida.append("</VALOR>");
                            xmlSalida.append("<ERROR>");
                                     if (seg.getTipo().equals("0"))   
                                        xmlSalida.append(seg.getErrorCampo(FilaSegPreparadoresVO.POS_CAMPO_GRAVEDAD));
                                     else xmlSalida.append(seg.getErrorCampo(FilaInsPreparadoresVO.POS_CAMPO_GRAVEDAD));
                            xmlSalida.append("</ERROR>");
                        xmlSalida.append("</GRAVEDAD>");
                        xmlSalida.append("<TIPO_CONTRATO>");
                            xmlSalida.append("<VALOR>");   
                                xmlSalida.append(seg.getTipoContrato());
                            xmlSalida.append("</VALOR>");
                            xmlSalida.append("<ERROR>");
                                     if (seg.getTipo().equals("0"))   
                                        xmlSalida.append(seg.getErrorCampo(FilaSegPreparadoresVO.POS_CAMPO_TIPOCONTRATO));
                                     else xmlSalida.append(seg.getErrorCampo(FilaInsPreparadoresVO.POS_CAMPO_TIPOCONTRATO));
                            xmlSalida.append("</ERROR>");
                        xmlSalida.append("</TIPO_CONTRATO>");
                        xmlSalida.append("<PORC_JORNADA>");
                            xmlSalida.append("<VALOR>");   
                                xmlSalida.append(seg.getPorcJornada());
                            xmlSalida.append("</VALOR>");
                            xmlSalida.append("<ERROR>");
                                     if (seg.getTipo().equals("0"))   
                                        xmlSalida.append(seg.getErrorCampo(FilaSegPreparadoresVO.POS_CAMPO_PORCJORN));
                                     else xmlSalida.append(seg.getErrorCampo(FilaInsPreparadoresVO.POS_CAMPO_PORCJORN));
                            xmlSalida.append("</ERROR>");
                        xmlSalida.append("</PORC_JORNADA>");                        
                        xmlSalida.append("<FECHA_INICIO>");
                            xmlSalida.append("<VALOR>");
                                xmlSalida.append(seg.getFecIni());
                            xmlSalida.append("</VALOR>");
                            xmlSalida.append("<ERROR>");
                                     if (seg.getTipo().equals("0"))   
                                        xmlSalida.append(seg.getErrorCampo(FilaSegPreparadoresVO.POS_CAMPO_FECHA_INICIO));
                                     else xmlSalida.append(seg.getErrorCampo(FilaInsPreparadoresVO.POS_CAMPO_FECHA_INICIO));
                            xmlSalida.append("</ERROR>");    
                        xmlSalida.append("</FECHA_INICIO>");
                        xmlSalida.append("<FECHA_FIN>");
                            xmlSalida.append("<VALOR>");   
                                xmlSalida.append(seg.getFecFin());
                            xmlSalida.append("</VALOR>");
                            xmlSalida.append("<ERROR>");
                                     if (seg.getTipo().equals("0"))   
                                        xmlSalida.append(seg.getErrorCampo(FilaSegPreparadoresVO.POS_CAMPO_FECHA_FIN));
                                     else xmlSalida.append(seg.getErrorCampo(FilaInsPreparadoresVO.POS_CAMPO_FECHA_FIN));
                            xmlSalida.append("</ERROR>");    
                        xmlSalida.append("</FECHA_FIN>");
                        xmlSalida.append("<NIF_PREPARADOR>");
                            xmlSalida.append("<VALOR>");    
                                xmlSalida.append(seg.getNifPreparador());
                            xmlSalida.append("</VALOR>");
                            xmlSalida.append("<ERROR>");
                                     if (seg.getTipo().equals("0"))   
                                        xmlSalida.append(seg.getErrorCampo(FilaSegPreparadoresVO.POS_CAMPO_NIF_PREPARADOR));
                                     else xmlSalida.append(seg.getErrorCampo(FilaInsPreparadoresVO.POS_CAMPO_NIF_PREPARADOR));
                            xmlSalida.append("</ERROR>");    
                        xmlSalida.append("</NIF_PREPARADOR>");
                        xmlSalida.append("<EMPRESA>");
                            xmlSalida.append("<VALOR>");    
                                xmlSalida.append(seg.getEmpresa());
                            xmlSalida.append("</VALOR>");
                            xmlSalida.append("<ERROR>");
                                     if (seg.getTipo().equals("0"))   
                                        xmlSalida.append(seg.getErrorCampo(FilaSegPreparadoresVO.POS_CAMPO_EMPRESA));
                                     else xmlSalida.append(seg.getErrorCampo(FilaInsPreparadoresVO.POS_CAMPO_EMPRESA));
                            xmlSalida.append("</ERROR>");    
                        xmlSalida.append("</EMPRESA>");
                       /* xmlSalida.append("<PERS_CONTACTO>");
                            xmlSalida.append("<VALOR>");    
                                xmlSalida.append(seg.getNombrePersContacto());
                            xmlSalida.append("</VALOR>");
                            xmlSalida.append("<ERROR>");
                                     if (seg.getTipo().equals("0"))   
                                        xmlSalida.append(seg.getErrorCampo(FilaSegPreparadoresVO.POS_CAMPO_CONTACTO));
                                     else xmlSalida.append(seg.getErrorCampo(FilaInsPreparadoresVO.POS_CAMPO_CONTACTO));
                            xmlSalida.append("</ERROR>");
                        xmlSalida.append("</PERS_CONTACTO>");     */                   
                        xmlSalida.append("<HORAS_CONTRATO>");
                            xmlSalida.append("<VALOR>");
                                xmlSalida.append(seg.getHorasCont());
                            xmlSalida.append("</VALOR>");
                            xmlSalida.append("<ERROR>");
                                     if (seg.getTipo().equals("0"))   
                                        xmlSalida.append(seg.getErrorCampo(FilaSegPreparadoresVO.POS_CAMPO_HORAS_ANUALES));
                                     else xmlSalida.append(seg.getErrorCampo(FilaInsPreparadoresVO.POS_CAMPO_HORAS_ANUALES));
                            xmlSalida.append("</ERROR>");
                        xmlSalida.append("</HORAS_CONTRATO>");
                        xmlSalida.append("<OBSERVACIONES>");
                            xmlSalida.append("<VALOR>");
                                xmlSalida.append(seg.getObservaciones());
                            xmlSalida.append("</VALOR>");
                            xmlSalida.append("<ERROR>");
                                     if (seg.getTipo().equals("0"))   
                                        xmlSalida.append(seg.getErrorCampo(FilaSegPreparadoresVO.POS_CAMPO_OBSERVACIONES));
                                     else xmlSalida.append(seg.getErrorCampo(FilaInsPreparadoresVO.POS_CAMPO_OBSERVACIONES));
                            xmlSalida.append("</ERROR>");                            
                        xmlSalida.append("</OBSERVACIONES>");
                        xmlSalida.append("<ERRORES>");
                        if(seg.getErrores() != null)
                        {
                            for(int contE = 0; contE < seg.getErrores().size(); contE++)
                            {
                                xmlSalida.append("<ERROR>");
                                    xmlSalida.append(seg.getErrores().get(contE));
                                xmlSalida.append("</ERROR>");
                            }
                        }
                        xmlSalida.append("</ERRORES>");
                    xmlSalida.append("</FILA>");
                    i++;
                }
                if(validaciones != null && !validaciones.isEmpty())
                {
                    xmlSalida.append("<VALIDACION>");
                        String validacion = validaciones.get(0);
                        if(validacion != null && !validacion.equals(""))
                        {
                            if(validacion.contains("/"))
                            {
                                String[] array = validacion.split("/");
                                validacion = array[array.length-1];
                            }
                        }
                        else
                        {
                            validacion = MeLanbide44I18n.getInstance().getMensaje(codigoIdioma,"error.datosNoValidos");
                        }
                        xmlSalida.append(validacion);
                    xmlSalida.append("</VALIDACION>");
                }
            xmlSalida.append("</RESPUESTA>");
            log.debug("xmlSalida:"+xmlSalida);
            try{
                response.setContentType("text/xml");
                response.setCharacterEncoding("UTF-8");
                PrintWriter out = response.getWriter();
                out.print(xmlSalida.toString());
                out.flush();
                out.close();
            }catch(Exception e){
                log.error(" Error : " + e.getMessage(), e);
            }//try-catch
    }
    
    
    private void escribirListaInsPreparadoresRequest(String codigoOperacion, List<FilaInsPreparadoresVO> inserciones, List<String> validaciones, int codigoIdioma, HttpServletResponse response)
    {
        StringBuffer xmlSalida = new StringBuffer();
            xmlSalida.append("<RESPUESTA>");
                xmlSalida.append("<CODIGO_OPERACION>");
                    xmlSalida.append(codigoOperacion);
                xmlSalida.append("</CODIGO_OPERACION>");
                int i = 0;
                for(FilaInsPreparadoresVO ins : inserciones)
                {
                    xmlSalida.append("<FILA>");                    
                        xmlSalida.append("<ID>");
                            xmlSalida.append("<VALOR>");
                                xmlSalida.append(ins.getCodPreparador()+"_"+ins.getIdseg());
                            xmlSalida.append("</VALOR>");
                            xmlSalida.append("<ERROR>");
                                xmlSalida.append(ConstantesMeLanbide44.FALSO);
                            xmlSalida.append("</ERROR>");
                        xmlSalida.append("</ID>");
                        xmlSalida.append("<NIF>");
                            xmlSalida.append("<VALOR>");
                                xmlSalida.append(ins.getNif());
                            xmlSalida.append("</VALOR>");
                            xmlSalida.append("<ERROR>");
                                 xmlSalida.append(ins.getErrorCampo(FilaInsPreparadoresVO.POS_CAMPO_NIF));
                            xmlSalida.append("</ERROR>");
                        xmlSalida.append("</NIF>");
                        xmlSalida.append("<NOMBRE>");
                            xmlSalida.append("<VALOR>");
                                xmlSalida.append(ins.getNombreApe());
                            xmlSalida.append("</VALOR>");
                            xmlSalida.append("<ERROR>");
                                xmlSalida.append(ins.getErrorCampo(FilaInsPreparadoresVO.POS_CAMPO_NOMBREAPEL));
                            xmlSalida.append("</ERROR>");
                        xmlSalida.append("</NOMBRE>");
                        
                        xmlSalida.append("<FECHA_NACIMIENTO>");
                            xmlSalida.append("<VALOR>");    
                                xmlSalida.append(ins.getFecNacimiento());
                            xmlSalida.append("</VALOR>");
                            xmlSalida.append("<ERROR>");
                                xmlSalida.append(ins.getErrorCampo(FilaInsPreparadoresVO.POS_CAMPO_FECHA_NACIMIENTO));
                            xmlSalida.append("</ERROR>"); 
                        xmlSalida.append("</FECHA_NACIMIENTO>");
                        xmlSalida.append("<SEXO>");
                            xmlSalida.append("<VALOR>");   
                                xmlSalida.append(ins.getSexo());
                            xmlSalida.append("</VALOR>");
                            xmlSalida.append("<ERROR>");
                                xmlSalida.append(ins.getErrorCampo(FilaInsPreparadoresVO.POS_CAMPO_SEXO));
                            xmlSalida.append("</ERROR>");
                        xmlSalida.append("</SEXO>");
                        xmlSalida.append("<COLECTIVO>");
                            xmlSalida.append("<VALOR>");   
                                xmlSalida.append(ins.getColectivo());
                            xmlSalida.append("</VALOR>");
                            xmlSalida.append("<ERROR>");
                                xmlSalida.append(ins.getErrorCampo(FilaInsPreparadoresVO.POS_CAMPO_COLECTIVO));
                            xmlSalida.append("</ERROR>");
                        xmlSalida.append("</COLECTIVO>");
                        xmlSalida.append("<TIPO_DISCAPACIDAD>");
                            xmlSalida.append("<VALOR>");   
                                xmlSalida.append(ins.getTipoDiscapacidad());
                            xmlSalida.append("</VALOR>");
                            xmlSalida.append("<ERROR>");
                                     xmlSalida.append(ins.getErrorCampo(FilaInsPreparadoresVO.POS_CAMPO_TIPODISCAPACIDAD));
                            xmlSalida.append("</ERROR>");
                        xmlSalida.append("</TIPO_DISCAPACIDAD>");
                        xmlSalida.append("<GRAVEDAD>");
                            xmlSalida.append("<VALOR>");   
                                xmlSalida.append(ins.getGravedad());
                            xmlSalida.append("</VALOR>");
                            xmlSalida.append("<ERROR>");                                     
                                xmlSalida.append(ins.getErrorCampo(FilaInsPreparadoresVO.POS_CAMPO_GRAVEDAD));
                            xmlSalida.append("</ERROR>");
                        xmlSalida.append("</GRAVEDAD>");
                        xmlSalida.append("<TIPO_CONTRATO>");
                            xmlSalida.append("<VALOR>");   
                                xmlSalida.append(ins.getTipoContrato());
                            xmlSalida.append("</VALOR>");
                            xmlSalida.append("<ERROR>");
                                     xmlSalida.append(ins.getErrorCampo(FilaInsPreparadoresVO.POS_CAMPO_TIPOCONTRATO));
                            xmlSalida.append("</ERROR>");
                        xmlSalida.append("</TIPO_CONTRATO>");
                        xmlSalida.append("<PORC_JORNADA>");
                            xmlSalida.append("<VALOR>");   
                                xmlSalida.append(ins.getPorcJornada());
                            xmlSalida.append("</VALOR>");
                            xmlSalida.append("<ERROR>");
                                xmlSalida.append(ins.getErrorCampo(FilaInsPreparadoresVO.POS_CAMPO_PORCJORN));
                            xmlSalida.append("</ERROR>");
                        xmlSalida.append("</PORC_JORNADA>");                        
                        xmlSalida.append("<FECHA_INICIO>");
                            xmlSalida.append("<VALOR>");
                                xmlSalida.append(ins.getFecIni());
                            xmlSalida.append("</VALOR>");
                            xmlSalida.append("<ERROR>");
                                xmlSalida.append(ins.getErrorCampo(FilaInsPreparadoresVO.POS_CAMPO_FECHA_INICIO));
                            xmlSalida.append("</ERROR>");    
                        xmlSalida.append("</FECHA_INICIO>");
                        xmlSalida.append("<FECHA_FIN>");
                            xmlSalida.append("<VALOR>");   
                                xmlSalida.append(ins.getFecFin());
                            xmlSalida.append("</VALOR>");
                            xmlSalida.append("<ERROR>");
                                xmlSalida.append(ins.getErrorCampo(FilaInsPreparadoresVO.POS_CAMPO_FECHA_FIN));
                            xmlSalida.append("</ERROR>");    
                        xmlSalida.append("</FECHA_FIN>");
                        xmlSalida.append("<NIF_PREPARADOR>");
                            xmlSalida.append("<VALOR>");    
                                xmlSalida.append(ins.getNifPreparador());
                            xmlSalida.append("</VALOR>");
                            xmlSalida.append("<ERROR>");
                                xmlSalida.append(ins.getErrorCampo(FilaInsPreparadoresVO.POS_CAMPO_NIF_PREPARADOR));
                            xmlSalida.append("</ERROR>");    
                        xmlSalida.append("</NIF_PREPARADOR>");
                        xmlSalida.append("<EMPRESA>");
                            xmlSalida.append("<VALOR>");    
                                xmlSalida.append(ins.getEmpresa());
                            xmlSalida.append("</VALOR>");
                            xmlSalida.append("<ERROR>");
                                xmlSalida.append(ins.getErrorCampo(FilaInsPreparadoresVO.POS_CAMPO_EMPRESA));
                            xmlSalida.append("</ERROR>");    
                        xmlSalida.append("</EMPRESA>");
                        /*xmlSalida.append("<PERS_CONTACTO>");
                            xmlSalida.append("<VALOR>");    
                                xmlSalida.append(ins.getNombrePersContacto());
                            xmlSalida.append("</VALOR>");
                            xmlSalida.append("<ERROR>");
                                xmlSalida.append(ins.getErrorCampo(FilaInsPreparadoresVO.POS_CAMPO_CONTACTO));
                            xmlSalida.append("</ERROR>");
                        xmlSalida.append("</PERS_CONTACTO>");  */                      
                        xmlSalida.append("<HORAS_CONTRATO>");
                            xmlSalida.append("<VALOR>");
                                xmlSalida.append(ins.getHorasCont());
                            xmlSalida.append("</VALOR>");
                            xmlSalida.append("<ERROR>");
                                xmlSalida.append(ins.getErrorCampo(FilaInsPreparadoresVO.POS_CAMPO_HORAS_ANUALES));
                            xmlSalida.append("</ERROR>");
                        xmlSalida.append("</HORAS_CONTRATO>");
                        xmlSalida.append("<OBSERVACIONES>");
                            xmlSalida.append("<VALOR>");
                                xmlSalida.append(ins.getObservaciones());
                            xmlSalida.append("</VALOR>");
                            xmlSalida.append("<ERROR>");
                                xmlSalida.append(ins.getErrorCampo(FilaInsPreparadoresVO.POS_CAMPO_OBSERVACIONES));
                            xmlSalida.append("</ERROR>");                            
                        xmlSalida.append("</OBSERVACIONES>");
                        xmlSalida.append("<ERRORES>");
                        if(ins.getErrores() != null)
                        {
                            for(int contE = 0; contE < ins.getErrores().size(); contE++)
                            {
                                xmlSalida.append("<ERROR>");
                                    xmlSalida.append(ins.getErrores().get(contE));
                                xmlSalida.append("</ERROR>");
                            }
                        }
                        xmlSalida.append("</ERRORES>");
                    xmlSalida.append("</FILA>");
                    i++;
                }
                if(validaciones != null && !validaciones.isEmpty())
                {
                    xmlSalida.append("<VALIDACION>");
                        String validacion = validaciones.get(0);
                        if(validacion != null && !validacion.equals(""))
                        {
                            if(validacion.contains("/"))
                            {
                                String[] array = validacion.split("/");
                                validacion = array[array.length-1];
                            }
                        }
                        else
                        {
                            validacion = MeLanbide44I18n.getInstance().getMensaje(codigoIdioma,"error.datosNoValidos");
                        }
                        xmlSalida.append(validacion);
                    xmlSalida.append("</VALIDACION>");
                }
            xmlSalida.append("</RESPUESTA>");
            try{
                response.setContentType("text/xml");
                response.setCharacterEncoding("UTF-8");
                PrintWriter out = response.getWriter();
                out.print(xmlSalida.toString());
                out.flush();
                out.close();
            }catch(Exception e){
                log.error(" Error : " + e.getMessage(), e);
            }//try-catch
    }
    
    public void eliminarSegPreparador(int codOrganizacion,  int codTramite, int ocurrenciaTramite,String numExpediente,HttpServletRequest request,HttpServletResponse response)
    {
        List<FilaSegPreparadoresVO> seguimientos = null;
        String codigoOperacion = "0";
        String tipoSeg = "";
        int codIdioma = 1;    
        String colectivo = "";
        String sexo = "";
        String filtrar = null;
        Integer idP = null;
        Integer idS = null;
        try
        {
            UsuarioValueObject usuario = (UsuarioValueObject) request.getSession().getAttribute("usuario");
            if(usuario != null)
            {
                codIdioma = usuario.getIdioma();
            }
        }
        catch(Exception ex)
        {
        }
        try
        {
            tipoSeg = (String)request.getParameter("tipoSeg");
            String idSegPrep = (String)request.getParameter("idSegPrep");
            colectivo = (String)request.getParameter("colectivo");
            sexo = (String)request.getParameter("sexo");
            filtrar = (String)request.getParameter("filtrar");
            if(idSegPrep == null || idSegPrep.equals(""))
            {
                codigoOperacion = "3";
            }
            else
            {
                try
                {
                    String[] datos =idSegPrep.split("_");
                    idP = Integer.parseInt(datos[0]);
                    idS = Integer.parseInt(datos[1]);
                }
                catch(Exception ex)
                {
                    codigoOperacion = "3";
                }
                if(idP != null && idS != null)
                {
                    EcaSegPreparadoresVO seg = new EcaSegPreparadoresVO();
                    seg.setJusPreparadoresCod(idP);
                    seg.setSegPreparadoresCod(idS);
                    int result = MeLanbide44Manager.getInstance().eliminarSegPreparador(seg, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                    if(result <= 0)
                    {
                        codigoOperacion = "1";
                    }
                    else
                    {
                        codigoOperacion = "0";
                    }
                }
                else
                {
                    codigoOperacion = "3";
                }
            }
        }
        catch(Exception ex)
        {
            codigoOperacion = "2";
        }
        if(codigoOperacion.equalsIgnoreCase("0"))
        {
            //cargarSeguimientos(codOrganizacion, codTramite, ocurrenciaTramite, numExpediente, request, response);
            
            try{
                EcaSolicitudVO sol = MeLanbide44Manager.getInstance().getDatosSolicitud(numExpediente, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                EcaJusPreparadoresVO prep = null;
                if(filtrar != null && filtrar.equalsIgnoreCase("1"))
                {
                    prep = new EcaJusPreparadoresVO();
                    prep.setJusPreparadoresCod(idP);
                    prep = MeLanbide44Manager.getInstance().getPreparadorJustificacionPorId(prep, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                }
                seguimientos = MeLanbide44Manager.getInstance().getListaSeguimientos(sol, prep, tipoSeg, colectivo, sexo, codIdioma, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                this.escribirListaSegPreparadoresRequest(codigoOperacion,seguimientos, null, codIdioma, response);
            }catch(Exception e){
                log.error(" Error : " + e.getMessage(), e);                
            }
        }
        else
        {
            StringBuffer xmlSalida = new StringBuffer();
            xmlSalida.append("<RESPUESTA>");
                xmlSalida.append("<CODIGO_OPERACION>");
                    xmlSalida.append(codigoOperacion);
                xmlSalida.append("</CODIGO_OPERACION>");
            xmlSalida.append("</RESPUESTA>");
            try{
                response.setContentType("text/xml");
                response.setCharacterEncoding("UTF-8");
                PrintWriter out = response.getWriter();
                out.print(xmlSalida.toString());
                out.flush();
                out.close();
            }catch(Exception e){
                log.error(" Error : " + e.getMessage(), e);
            }//try-catch
        }
    }
    
    public String cargarNuevoProspectorJustificacion(int codOrganizacion,  int codTramite, int ocurrenciaTramite,String numExpediente,HttpServletRequest request,HttpServletResponse response)
    {
        try
        {
            String opcion = (String)request.getParameter("opcion");
            if(opcion != null)
            {
                if(opcion.equalsIgnoreCase("nuevo"))
                {
                    //Cargar nuevo preparador
                    //Cargar nuevo preparador
                    String idProsS = (String)request.getParameter("idProsOrigen");
                    if(idProsS != null)
                    {
                        EcaJusProspectoresVO prosS = new EcaJusProspectoresVO();
                        prosS.setJusProspectoresCod(Integer.parseInt(idProsS));
                        prosS = MeLanbide44Manager.getInstance().getProspectorJustificacionPorId(prosS, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                        if(prosS != null)
                        {
                            request.setAttribute("prospectorOrigen", prosS);
                        }
                    }
                }
                else if(opcion.equalsIgnoreCase("modificar") || opcion.equalsIgnoreCase("consultar"))
                {
                    //Cargar modificar preparador
                    String idPrep = (String)request.getParameter("idProsModif");
                    if(idPrep != null)
                    {
                        EcaJusProspectoresVO pros = new EcaJusProspectoresVO();
                        pros.setJusProspectoresCod(Integer.parseInt(idPrep));
                        pros = MeLanbide44Manager.getInstance().getProspectorJustificacionPorId(pros, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                        if(pros != null)
                        {
                            request.setAttribute("prospectorModif", pros);
                            EcaJusProspectoresVO sustituto = new EcaJusProspectoresVO();
                            sustituto = MeLanbide44Manager.getInstance().getProspectorJustificacionSustituto(pros.getJusProspectoresCod(), this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                            if(sustituto != null)
                            {
                                request.setAttribute("sustitutopros", sustituto);
                            }
                        }
                        
                        if (pros.getJusProspectorOrigen()!= null && !pros.getJusProspectorOrigen().equals("")){
                            EcaJusProspectoresVO prosS = new EcaJusProspectoresVO();
                            prosS.setJusProspectoresCod(pros.getJusProspectorOrigen());
                            prosS = MeLanbide44Manager.getInstance().getProspectorJustificacionPorId(prosS, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                            if(prosS != null)
                            {
                                request.setAttribute("prospectorOrigen", prosS);
                            }
                        }
                    }
                    if(opcion.equalsIgnoreCase("consultar"))
                    {
                        request.setAttribute("consulta", true);
                    }
                }
            }
        }
        catch(Exception ex)
        {
            
        }   
        return "/jsp/extension/melanbide44/justificacion/nuevoProspectorJustificacion.jsp";
    }
    
   public void guardarProspectorJustificacion(int codOrganizacion,  int codTramite, int ocurrenciaTramite,String numExpediente,HttpServletRequest request,HttpServletResponse response)
    {
        String codigoOperacion = "0";
        List<FilaProspectorJustificacionVO> prospectores = new ArrayList<FilaProspectorJustificacionVO>();;
        
        int codIdioma = 1;    
        try
        {
            UsuarioValueObject usuario = (UsuarioValueObject) request.getSession().getAttribute("usuario");
            if(usuario != null)
            {
                codIdioma = usuario.getIdioma();
            }
        }
        catch(Exception ex)
        {

        }
        
        try
        {
            AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
            
            EcaSolicitudVO sol = MeLanbide44Manager.getInstance().getDatosSolicitud(numExpediente, adapt);
            if(sol != null && sol.getSolicitudCod() != null)
            {
                    
                SimpleDateFormat format = new SimpleDateFormat(ConstantesMeLanbide44.FORMATO_FECHA);
                //Recojo los parámetros
                String idPros = (String)request.getParameter("idPros");
                String nif = (String)request.getParameter("nif");
                String nomApel = (String)request.getParameter("nomApel");
                String feIni = (String)request.getParameter("feIni");
                String feFin = (String)request.getParameter("feFin");
                String horasAnualesJC = (String)request.getParameter("horasAnualesJC");
                String horasContrato = (String)request.getParameter("horasContrato");
                String horasECA = (String)request.getParameter("horasECA");
                String costesSSJor = (String)request.getParameter("costesSSJor");
                String costesSSPorJor = (String)request.getParameter("costesSSPorJor");
                String costesSSECA = (String)request.getParameter("costesSSECA");  
                String idProsOrigen = (String)request.getParameter("idProsOrigen");
                
                EcaJusProspectoresVO prosValida = MeLanbide44Manager.getInstance().getProspectorJustificacionPorNIF(sol, nif,feIni != null && !feIni.equals("") ? format.parse(feIni) : null, adapt);
                boolean prepRepetido = false;
                if(prosValida != null && idPros != null)
                {
                    if(!idPros.equalsIgnoreCase(prosValida.getJusProspectoresCod().toString()))
                    {
                        prepRepetido = true;
                    }
                }
                if(!prepRepetido)
                {

                    EcaJusProspectoresVO pros = null;
                    if(idPros != null && !idPros.equals(""))
                    {
                        pros = new EcaJusProspectoresVO();
                        pros.setJusProspectoresCod(Integer.parseInt(idPros));
                        pros = MeLanbide44Manager.getInstance().getProspectorJustificacionPorId(pros, adapt);
                    }
                    else
                    {
                        pros = new EcaJusProspectoresVO();
                    }
                    if(pros == null)
                    {
                        codigoOperacion = "3";
                    }
                    else
                    {
                        EcaSolProspectoresVO prosRelacion = null;
                        try
                        {
                            prosRelacion = MeLanbide44Manager.getInstance().getProspectorSolicitudPorNIF(sol, nif, adapt);
                        }
                        catch(Exception ex)
                        {
                            
                        }
                        if(prosRelacion != null)
                        {
                            pros.setSolProspectoresCod(prosRelacion.getSolProspectoresCod());
                        }
                        else
                        {
                            pros.setSolProspectoresCod(null);
                        }
                        
                        
                        pros.setFecFin(feFin != null && !feFin.equals("") ? format.parse(feFin) : null);
                        pros.setFecIni(feIni != null && !feIni.equals("") ? format.parse(feIni) : null);
                        pros.setHorasCont(horasContrato != null && !horasContrato.equals("") ? new BigDecimal(horasContrato.replaceAll(",", "\\.")) : null);
                        pros.setHorasEca(horasECA != null && !horasECA.equals("") ? new BigDecimal(horasECA.replaceAll(",", "\\.")) : null);
                        pros.setHorasJC(horasAnualesJC != null && !horasAnualesJC.equals("") ? new BigDecimal(horasAnualesJC.replaceAll(",", "\\.")) : null);
                        pros.setImpSSECA(costesSSECA != null && !costesSSECA.equals("") ? new BigDecimal(costesSSECA.replaceAll(",", "\\.")) : null);
                        pros.setImpSSJC(costesSSJor != null && !costesSSJor.equals("") ? new BigDecimal(costesSSJor.replaceAll(",", "\\.")) : null);
                        pros.setImpSSJR(costesSSPorJor != null && !costesSSPorJor.equals("") ? new BigDecimal(costesSSPorJor.replaceAll(",", "\\.")) : null);
                        pros.setNif(nif != null ? nif.toUpperCase() : null);
                        pros.setNombre(nomApel != null ? nomApel.toUpperCase() : null);
                        pros.setSolicitud(sol.getSolicitudCod());
                        
                        if(idProsOrigen != null && !idProsOrigen.equals(""))
                        {
                            pros.setJusProspectorOrigen(Integer.parseInt(idProsOrigen));
                            //Si el codOrigen no es nulo, significa que el prospector que vamos a dar de alta es un sustituto por lo que hay
                            //que indicar el tipo de sustituto correspondiente
                            pros.setTipoSust(ConstantesMeLanbide44.TIPOS_SUSTITUTO.SUSTITUTO_JUSTIFICACION);
                        }

                        EcaConfiguracionVO config = MeLanbide44Manager.getInstance().getConfiguracionEca(MeLanbide44Utils.getEjercicioDeExpediente(numExpediente), adapt);

                        boolean soloFormato = pros.getJusProspectoresCod() != null;
                        
                        EcaJusProspectoresVO origen = null;
                        
                        if(pros.getJusProspectorOrigen() != null)
                        {
                            origen = new EcaJusProspectoresVO();
                            origen.setJusProspectoresCod(pros.getJusProspectorOrigen());
                            origen = MeLanbide44Manager.getInstance().getProspectorJustificacionPorId(origen, adapt);
                        }

                        List<EcaJusProspectoresVO> sustitutos = MeLanbide44Manager.getInstance().getSustitutosProspectorJustificacion(pros.getJusProspectoresCod(), adapt);
                            
                        List<String> validacion = MeLanbide44Utils.validarEcaJusProspectoresVO(pros, origen, sustitutos, config, sol.getExpEje(), codIdioma, soloFormato, false);
                        if(validacion == null || validacion.size() == 0)
                        {
                            MeLanbide44Manager.getInstance().guardarEcaJusProspectoresVO(pros, adapt);
                            prospectores = MeLanbide44Manager.getInstance().getListaProspectoresJustificacion(sol, adapt, codIdioma);
                        }
                        else
                        {
                            codigoOperacion = "4";
                        }
                    }
                }
                else
                {
                    codigoOperacion = "5";
                }
            }
            else
            {
                codigoOperacion = "1";
            }
        }
        catch(Exception ex)
        {
            log.error(" Error : " + ex.getMessage(), ex);
            codigoOperacion = "2";
        }
        
        this.escribirListaProspectoresJusRequest(codigoOperacion, prospectores, response);
    }
    
   
   private void escribirListaProspectoresJusRequest(String codigoOperacion, List<FilaProspectorJustificacionVO> prospectores, HttpServletResponse response)
    {
        StringBuffer xmlSalida = new StringBuffer();
            xmlSalida.append("<RESPUESTA>");
                xmlSalida.append("<CODIGO_OPERACION>");
                    xmlSalida.append(codigoOperacion);
                xmlSalida.append("</CODIGO_OPERACION>");
                int i = 0;
                for(FilaProspectorJustificacionVO pros : prospectores)
                {
                    xmlSalida.append("<FILA>");
                        xmlSalida.append("<ID>");
                            xmlSalida.append("<VALOR>");
                                xmlSalida.append(pros.getId());
                            xmlSalida.append("</VALOR>");
                            xmlSalida.append("<ERROR>");
                                xmlSalida.append(ConstantesMeLanbide44.FALSO);
                            xmlSalida.append("</ERROR>");
                        xmlSalida.append("</ID>");
                        xmlSalida.append("<NIF>");
                            xmlSalida.append("<VALOR>");
                                xmlSalida.append(pros.getNif());
                            xmlSalida.append("</VALOR>");
                            xmlSalida.append("<ERROR>");
                                xmlSalida.append(pros.getErrorCampo(FilaProspectorJustificacionVO.POS_CAMPO_NIF));
                            xmlSalida.append("</ERROR>");
                        xmlSalida.append("</NIF>");
                        xmlSalida.append("<NOMBRE>");
                            xmlSalida.append("<VALOR>");
                                xmlSalida.append(pros.getNombreApel());
                            xmlSalida.append("</VALOR>");
                            xmlSalida.append("<ERROR>");
                                xmlSalida.append(pros.getErrorCampo(FilaProspectorJustificacionVO.POS_CAMPO_NOMBREAPEL));
                            xmlSalida.append("</ERROR>");
                        xmlSalida.append("</NOMBRE>");
                        xmlSalida.append("<FECHA_INICIO>");
                            xmlSalida.append("<VALOR>");
                                xmlSalida.append(pros.getFechaInicio());
                            xmlSalida.append("</VALOR>");
                            xmlSalida.append("<ERROR>");
                                xmlSalida.append(pros.getErrorCampo(FilaProspectorJustificacionVO.POS_CAMPO_FECHA_INICIO));
                            xmlSalida.append("</ERROR>");
                        xmlSalida.append("</FECHA_INICIO>");
                        xmlSalida.append("<FECHA_FIN>");
                            xmlSalida.append("<VALOR>");
                                xmlSalida.append(pros.getFechaFin());
                            xmlSalida.append("</VALOR>");
                            xmlSalida.append("<ERROR>");
                                xmlSalida.append(pros.getErrorCampo(FilaProspectorJustificacionVO.POS_CAMPO_FECHA_FIN));
                            xmlSalida.append("</ERROR>");
                        xmlSalida.append("</FECHA_FIN>");
                        xmlSalida.append("<HORAS_ANUALES>");
                            xmlSalida.append("<VALOR>");
                                xmlSalida.append(pros.getHorasAnuales());
                            xmlSalida.append("</VALOR>");
                            xmlSalida.append("<ERROR>");
                                xmlSalida.append(pros.getErrorCampo(FilaProspectorJustificacionVO.POS_CAMPO_HORAS_ANUALES));
                            xmlSalida.append("</ERROR>");
                        xmlSalida.append("</HORAS_ANUALES>");
                        xmlSalida.append("<HORAS_CONTRATO>");
                            xmlSalida.append("<VALOR>");
                                xmlSalida.append(pros.getHorasContrato());
                            xmlSalida.append("</VALOR>");
                            xmlSalida.append("<ERROR>");
                                xmlSalida.append(pros.getErrorCampo(FilaProspectorJustificacionVO.POS_CAMPO_HORAS_CONTRATO));
                            xmlSalida.append("</ERROR>");
                        xmlSalida.append("</HORAS_CONTRATO>");
                        xmlSalida.append("<HORAS_DEDICACION_ECA>");
                            xmlSalida.append("<VALOR>");
                                xmlSalida.append(pros.getHorasDedicacionECA());
                            xmlSalida.append("</VALOR>");
                            xmlSalida.append("<ERROR>");
                                xmlSalida.append(pros.getErrorCampo(FilaProspectorJustificacionVO.POS_CAMPO_HORAS_DEDICACION_ECA));
                            xmlSalida.append("</ERROR>");
                        xmlSalida.append("</HORAS_DEDICACION_ECA>");
                        xmlSalida.append("<COSTES_SALARIALES_SS_JOR>");
                            xmlSalida.append("<VALOR>");
                                xmlSalida.append(pros.getCostesSalarialesSSJor());
                            xmlSalida.append("</VALOR>");
                            xmlSalida.append("<ERROR>");
                                xmlSalida.append(pros.getErrorCampo(FilaProspectorJustificacionVO.POS_CAMPO_COSTES_SALARIALES_SS_JOR));
                            xmlSalida.append("</ERROR>");
                        xmlSalida.append("</COSTES_SALARIALES_SS_JOR>");
                        xmlSalida.append("<COSTES_SALARIALES_SS_POR_JOR>");
                            xmlSalida.append("<VALOR>");
                                xmlSalida.append(pros.getCostesSalarialesSSPorJor());
                            xmlSalida.append("</VALOR>");
                            xmlSalida.append("<ERROR>");
                                xmlSalida.append(pros.getErrorCampo(FilaProspectorJustificacionVO.POS_CAMPO_COSTES_SALARIALES_SS_POR_JOR));
                            xmlSalida.append("</ERROR>");
                        xmlSalida.append("</COSTES_SALARIALES_SS_POR_JOR>");
                        xmlSalida.append("<COSTES_SALARIALES_SS_ECA>");
                            xmlSalida.append("<VALOR>");
                                xmlSalida.append(pros.getCostesSalarialesSSEca());
                            xmlSalida.append("</VALOR>");
                            xmlSalida.append("<ERROR>");
                                xmlSalida.append(pros.getErrorCampo(FilaProspectorJustificacionVO.POS_CAMPO_COSTES_SALARIALES_SS_ECA));
                            xmlSalida.append("</ERROR>");
                        xmlSalida.append("</COSTES_SALARIALES_SS_ECA>");
                        xmlSalida.append("<VISITAS>");
                            xmlSalida.append("<VALOR>");
                                xmlSalida.append(pros.getVisitas());
                            xmlSalida.append("</VALOR>");
                            xmlSalida.append("<ERROR>");
                                xmlSalida.append(pros.getErrorCampo(FilaProspectorJustificacionVO.POS_CAMPO_VISITAS));
                            xmlSalida.append("</ERROR>");
                        xmlSalida.append("</VISITAS>");
                        xmlSalida.append("<VISITAS_IMP>");
                            xmlSalida.append("<VALOR>");
                                xmlSalida.append(pros.getVisitasImp());
                            xmlSalida.append("</VALOR>");
                            xmlSalida.append("<ERROR>");
                                xmlSalida.append(pros.getErrorCampo(FilaProspectorJustificacionVO.POS_CAMPO_VISITAS_IMP));
                            xmlSalida.append("</ERROR>");
                        xmlSalida.append("</VISITAS_IMP>");                        
                        xmlSalida.append("<ERRORES>");
                        if(pros.getErrores() != null)
                        {
                            for(int contE = 0; contE < pros.getErrores().size(); contE++)
                            {
                                xmlSalida.append("<ERROR>");
                                    xmlSalida.append(pros.getErrores().get(contE));
                                xmlSalida.append("</ERROR>");
                            }
                        }
                        xmlSalida.append("</ERRORES>");
                        if(pros.esSustituto() != null)
                        {
                            xmlSalida.append("<ES_SUSTITUTO>");
                                xmlSalida.append(pros.esSustituto() ? "1" : "0");
                            xmlSalida.append("</ES_SUSTITUTO>");
                        }
                    xmlSalida.append("</FILA>");
                    i++;
                }
            xmlSalida.append("</RESPUESTA>");
            try{
                response.setContentType("text/xml");
                response.setCharacterEncoding("UTF-8");
                PrintWriter out = response.getWriter();
                out.print(xmlSalida.toString());
                out.flush();
                out.close();
            }catch(Exception e){
                log.error(" Error : " + e.getMessage(), e);
            }//try-catch
    }
   
   
   public void eliminarProspectorJustificacion(int codOrganizacion,  int codTramite, int ocurrenciaTramite,String numExpediente,HttpServletRequest request,HttpServletResponse response)
    {
        
        String codigoOperacion = "0";
        try
        {
            String idPros = (String)request.getParameter("idPros");
            Integer idP = null;
            if(idPros == null || idPros.equals(""))
            {
                codigoOperacion = "3";
            }
            else
            {
                try
                {
                    idP = Integer.parseInt(idPros);
                }
                catch(Exception ex)
                {
                    codigoOperacion = "3";
                }
                if(idP != null)
                {
                    EcaJusProspectoresVO pros = new EcaJusProspectoresVO();
                    pros.setJusProspectoresCod(idP);
                    int result = MeLanbide44Manager.getInstance().eliminarProspectorJustificacion(pros, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                    if(result <= 0)
                    {
                        codigoOperacion = "1";
                    }
                    else
                    {
                        codigoOperacion = "0";
                    }
                }
                else
                {
                    codigoOperacion = "3";
                }
            }
        }
        catch(Exception ex)
        {
            codigoOperacion = "2";
        }
        
        /*if(codigoOperacion.equalsIgnoreCase("0"))
        {
            this.getListaProspectoresJustificacion(codOrganizacion, codTramite, ocurrenciaTramite, numExpediente, request, response);
        }
        else
        {
            StringBuffer xmlSalida = new StringBuffer();
            xmlSalida.append("<RESPUESTA>");
                xmlSalida.append("<CODIGO_OPERACION>");
                    xmlSalida.append(codigoOperacion);
                xmlSalida.append("</CODIGO_OPERACION>");
            xmlSalida.append("</RESPUESTA>");
            try{
                response.setContentType("text/xml");
                response.setCharacterEncoding("UTF-8");
                PrintWriter out = response.getWriter();
                out.print(xmlSalida.toString());
                out.flush();
                out.close();
            }catch(Exception e){
                log.error(" Error : " + e.getMessage(), e);
            }//try-catch
        }*/
        
        int codIdioma = 1;    
        List<FilaProspectorJustificacionVO> prospectores = null;
        try
        {
            UsuarioValueObject usuario = (UsuarioValueObject) request.getSession().getAttribute("usuario");
            if(usuario != null)
            {
                codIdioma = usuario.getIdioma();
            }
        }
        catch(Exception ex)
        {

        }

        try
        {
            prospectores = this.getListaProspectoresJustificacion(codOrganizacion, numExpediente, codIdioma);
        }
        catch(Exception ex)
        {

        }
        
        this.escribirListaProspectoresJusRequest(codigoOperacion, prospectores, response);
    }
    
   public void getListaProspectoresJustificacion(int codOrganizacion,  int codTramite, int ocurrenciaTramite,String numExpediente,HttpServletRequest request,HttpServletResponse response)
    {
        try
        {
            try
            {
                //Al importar un fichero, se inserta este mensaje en sesion como resultado
                //Justo despues de importar se llama a este metodo para recargar la tabla.
                //Este es el punto donde se borra este mensaje de sesion, para no mantenerlo en memoria innecesariamente.
                request.getSession().removeAttribute("mensajeImportar");
            }
            catch(Exception ex)
            {
                
            }
        
            int codIdioma = 1;    
            try
            {
                UsuarioValueObject usuario = (UsuarioValueObject) request.getSession().getAttribute("usuario");
                if(usuario != null)
                {
                    codIdioma = usuario.getIdioma();
                }
            }
            catch(Exception ex)
            {

            }
            
            List<FilaProspectorJustificacionVO> prospectores = null;
            
            try
            {
                prospectores = this.getListaProspectoresJustificacion(codOrganizacion, numExpediente, codIdioma);
            }
            catch(Exception ex)
            {
                
            }
            
            String codigoOperacion = "0";
            
            this.escribirListaProspectoresJusRequest(codigoOperacion, prospectores, response);
            
        }
        catch(Exception ex)
        {
            log.error(" Error : " + ex.getMessage(), ex);
        }
    }
   
   public String cargarVisitas(int codOrganizacion,  int codTramite, int ocurrenciaTramite,String numExpediente,HttpServletRequest request,HttpServletResponse response)
    {
         String url="";
        try
        {
            int idiomaUsuario = 1;
            UsuarioValueObject usuario = (UsuarioValueObject) request.getSession().getAttribute("usuario");
            try
            {
                    if (usuario != null) 
                    {                    
                        idiomaUsuario = usuario.getIdioma();
                    }
            }
            catch(Exception ex)
            {
            }
            
             AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));            
             EcaSolicitudVO sol = MeLanbide44Manager.getInstance().getDatosSolicitud(numExpediente, adapt);
            String idprep = (String)request.getParameter("idProspector");   
            String opcion = (String)request.getParameter("opcion");
            if (idprep!=null){
                url= "/jsp/extension/melanbide44/justificacion/visitas.jsp";
                EcaJusProspectoresVO pros = new EcaJusProspectoresVO();
                pros.setJusProspectoresCod(Integer.parseInt(idprep));
                
                pros = MeLanbide44Manager.getInstance().getProspectorJustificacionPorId(pros, adapt);
                if(pros != null) {
                    request.setAttribute("prospector", pros);
                    List<FilaVisProspectoresVO> listaVisitas = MeLanbide44Manager.getInstance().getListaVisitas(sol, pros,idiomaUsuario, adapt);
                    if(listaVisitas != null)
                    {
                        request.setAttribute("listaVisitas", listaVisitas);
                        
                    }
                 }
            }else {
                url= "/jsp/extension/melanbide44/justificacion/visitas.jsp";
                List<FilaVisProspectoresVO> listaVisitas = MeLanbide44Manager.getInstance().getListaVisitas(sol, null, idiomaUsuario, adapt);
                if(listaVisitas != null)
                {
                    request.setAttribute("listaVisitas", listaVisitas);
                }
            }
            if(opcion.equalsIgnoreCase("consultar"))
            {
                request.setAttribute("consulta", true);
            }
        }
        catch(Exception ex)
        {
            log.error(ex.getMessage());
        }
        return url;
    }
   
   public String procesarExcelVisitasPros_Antes(int codOrganizacion,  int codTramite, int ocurrenciaTramite,String numExpediente,HttpServletRequest request,HttpServletResponse response)
    {
        int codIdioma = 1;    
        
        try
        {
            UsuarioValueObject usuario = (UsuarioValueObject) request.getSession().getAttribute("usuario");
            if(usuario != null)
            {
                codIdioma = usuario.getIdioma();
            }
        }
        catch(Exception ex)
        {

        }
        
        int ano = MeLanbide44Utils.getEjercicioDeExpediente(numExpediente);
        if (ano<2016){//<2016
            try
            {   
                MultipartRequestWrapper wrapper = new MultipartRequestWrapper(request);

                wrapper.getParameterMap();
                CommonsMultipartRequestHandler handler = new CommonsMultipartRequestHandler();
                handler.handleRequest(request);
                Hashtable<String, FormFile> table = handler.getFileElements();
                FormFile file = null;            
                file = table.get("fichero_visitas_jus");            
                byte[] data = file.getFileData();
                ByteArrayInputStream bais = new ByteArrayInputStream(data);
                HSSFWorkbook workbook = new HSSFWorkbook(bais);

                HSSFSheet sheet = workbook.getSheetAt(0);

                int filaIni = sheet.getFirstRowNum();
                int filaFin = sheet.getLastRowNum();

                HSSFRow row = null;
                List<EcaVisProspectoresVO> filasImportar = new ArrayList<EcaVisProspectoresVO>();
                EcaVisProspectoresVO vis = null;
                String validacion = null;
                int i = 0;
                EcaSolicitudVO sol = MeLanbide44Manager.getInstance().getDatosSolicitud(numExpediente, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                try
                {
                    for(i = filaIni+1; i <= filaFin; i++)
                    {
                        row = sheet.getRow(i);

                        ///////vis = (EcaVisProspectoresVO)MeLanbide44MappingUtils.getInstance().map(row, EcaVisProspectoresVO.class);
                        vis = (EcaVisProspectoresVO)MeLanbide44MappingUtils.getInstance().mapearEcaVisProspectoresVO_old(row, EcaVisProspectoresVO.class);

                        if(vis != null )
                        {                         
                            EcaJusProspectoresVO pros=null;    
                                if ( vis.getFecVisita()!=null){
                                    pros = MeLanbide44Manager.getInstance().getProspectorJustificacionPorNIF(sol, vis.getNifProspector(), vis.getFecVisita(), this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                                }
                                if (pros!=null){
                                    vis.setJusProspectoresCod(pros.getJusProspectoresCod());

                                    filasImportar.add(vis);
                                }else {                            
                                    //throw new ExcelRowMappingException("2");                            
                                    ExcelRowMappingException erme = new ExcelRowMappingException("2");
                                    erme.setMensaje(String.format(MeLanbide44I18n.getInstance().getMensaje(codIdioma,"error.NifProspectorNoEncontrado"), MeLanbide44Utils.fromColNumberToExcelColName(2), i+1));
                                    throw erme;
                                }                        
                        }
                    }
                     int filasImportadas = 0;
                    filasImportadas = MeLanbide44Manager.getInstance().importarVisitas(numExpediente, filasImportar, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));

                    if(filasImportadas < filasImportar.size())
                    {
                        request.getSession().setAttribute("mensajeImportar", MeLanbide44I18n.getInstance().getMensaje(codIdioma, "error.errorImportarGen"));
                    }
                    else
                    {
                        request.getSession().setAttribute("mensajeImportar", MeLanbide44I18n.getInstance().getMensaje(codIdioma, "msg.registrosImportadosOK_1")+" "+filasImportadas+" "+MeLanbide44I18n.getInstance().getMensaje(codIdioma, "msg.registrosImportadosOK_2"));
                    }
                }
                catch(ExcelRowMappingException erme)
                {
                    String mensajeImportar = null;
                    if(erme.getMensaje() == null || erme.getMensaje().equals(""))
                    {
                        mensajeImportar = MeLanbide44Utils.crearMensajeDatoExcelNoValido(erme, i+1, codIdioma);
                        if (erme.getCampo().equals("2"))
                            mensajeImportar = mensajeImportar+" "+MeLanbide44I18n.getInstance().getMensaje(codIdioma,"error.NifProspectorNoEncontrado");
                    }
                    else
                    {
                        mensajeImportar = erme.getMensaje();
                    }
                    request.getSession().setAttribute("mensajeImportar", mensajeImportar);  
                }
            }
            catch(FilaSeguimientoPrepNoValidaException ex)
            {
                log.error(" Error : " + ex.getMessage(), ex);
                request.getSession().setAttribute("mensajeImportar", MeLanbide44I18n.getInstance().getMensaje(codIdioma, "error.datosNoValidosImportar"));
            }
            catch(IOException ex)
            {
                log.error(" Error : " + ex.getMessage(), ex);
                request.getSession().setAttribute("mensajeImportar", MeLanbide44I18n.getInstance().getMensaje(codIdioma, "error.errorProcesarExcel"));
            }
            catch(Exception ex)
            {
                log.error(" Error : " + ex.getMessage(), ex);
                request.getSession().setAttribute("mensajeImportar", MeLanbide44I18n.getInstance().getMensaje(codIdioma, "error.errorImportarGen"));
            }
        }else{//a partir de 2016
            try
            {   
                MultipartRequestWrapper wrapper = new MultipartRequestWrapper(request);

                wrapper.getParameterMap();
                CommonsMultipartRequestHandler handler = new CommonsMultipartRequestHandler();
                handler.handleRequest(request);
                Hashtable<String, FormFile> table = handler.getFileElements();
                FormFile file = null;            
                file = table.get("fichero_visitas_jus");            
                byte[] data = file.getFileData();
                ByteArrayInputStream bais = new ByteArrayInputStream(data);
                HSSFWorkbook workbook = new HSSFWorkbook(bais);

                HSSFSheet sheet = workbook.getSheetAt(0);

                int filaIni = sheet.getFirstRowNum();
                int filaFin = sheet.getLastRowNum();

                HSSFRow row = null;
                List<EcaVisProspectores2016VO> filasImportar = new ArrayList<EcaVisProspectores2016VO>();
                EcaVisProspectores2016VO vis = null;
                String validacion = null;
                int i = 0;
                EcaSolicitudVO sol = MeLanbide44Manager.getInstance().getDatosSolicitud(numExpediente, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                try
                {
                    for(i = filaIni+1; i <= filaFin; i++)
                    {
                        row = sheet.getRow(i);

                        vis = (EcaVisProspectores2016VO)MeLanbide44MappingUtils.getInstance().mapearEcaVisProspectoresVO_old(row, EcaVisProspectores2016VO.class);

                        if(vis != null )
                        {                         
                            EcaJusProspectoresVO pros=null;    
                                if ( vis.getFecVisita()!=null){
                                    pros = MeLanbide44Manager.getInstance().getProspectorJustificacionPorNIF(sol, vis.getNifProspector(), vis.getFecVisita(), this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                                }
                                if (pros!=null){
                                    vis.setJusProspectoresCod(pros.getJusProspectoresCod());

                                    filasImportar.add(vis);
                                }else {                            
                                    //throw new ExcelRowMappingException("2");                            
                                    ExcelRowMappingException erme = new ExcelRowMappingException("2");
                                    erme.setMensaje(String.format(MeLanbide44I18n.getInstance().getMensaje(codIdioma,"error.NifProspectorNoEncontrado"), MeLanbide44Utils.fromColNumberToExcelColName(2), i+1));
                                    throw erme;
                                }                        
                        }
                    }
                     int filasImportadas = 0;
                    filasImportadas = MeLanbide44Manager.getInstance().importarVisitas2016(numExpediente, filasImportar, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));

                    if(filasImportadas < filasImportar.size())
                    {
                        request.getSession().setAttribute("mensajeImportar", MeLanbide44I18n.getInstance().getMensaje(codIdioma, "error.errorImportarGen"));
                    }
                    else
                    {
                        request.getSession().setAttribute("mensajeImportar", MeLanbide44I18n.getInstance().getMensaje(codIdioma, "msg.registrosImportadosOK_1")+" "+filasImportadas+" "+MeLanbide44I18n.getInstance().getMensaje(codIdioma, "msg.registrosImportadosOK_2"));
                    }
                }
                catch(ExcelRowMappingException erme)
                {
                    String mensajeImportar = null;
                    if(erme.getMensaje() == null || erme.getMensaje().equals(""))
                    {
                        mensajeImportar = MeLanbide44Utils.crearMensajeDatoExcelNoValido(erme, i+1, codIdioma);
                        if (erme.getCampo().equals("2"))
                            mensajeImportar = mensajeImportar+" "+MeLanbide44I18n.getInstance().getMensaje(codIdioma,"error.NifProspectorNoEncontrado");
                    }
                    else
                    {
                        mensajeImportar = erme.getMensaje();
                    }
                    request.getSession().setAttribute("mensajeImportar", mensajeImportar);  
                }
            }
            catch(FilaSeguimientoPrepNoValidaException ex)
            {
                log.error(" Error : " + ex.getMessage(), ex);
                request.getSession().setAttribute("mensajeImportar", MeLanbide44I18n.getInstance().getMensaje(codIdioma, "error.datosNoValidosImportar"));
            }
            catch(IOException ex)
            {
                log.error(" Error : " + ex.getMessage(), ex);
                request.getSession().setAttribute("mensajeImportar", MeLanbide44I18n.getInstance().getMensaje(codIdioma, "error.errorProcesarExcel"));
            }
            catch(Exception ex)
            {
                log.error(" Error : " + ex.getMessage(), ex);
                request.getSession().setAttribute("mensajeImportar", MeLanbide44I18n.getInstance().getMensaje(codIdioma, "error.errorImportarGen"));
            }
                        
        }
        

        return "/jsp/extension/melanbide44/justificacion/recargarListaVisitas.jsp";
    }
   
   public String procesarExcelVisitasPros(int codOrganizacion,  int codTramite, int ocurrenciaTramite,String numExpediente,HttpServletRequest request,HttpServletResponse response)
    {
        int codIdioma = 1;    
        
        try
        {
            UsuarioValueObject usuario = (UsuarioValueObject) request.getSession().getAttribute("usuario");
            if(usuario != null)
            {
                codIdioma = usuario.getIdioma();
            }
        }
        catch(Exception ex)
        {
        }
        
        int ano = MeLanbide44Utils.getEjercicioDeExpediente(numExpediente);
        if (ano<2016){//<2016
            try
            {   
                MultipartRequestWrapper wrapper = new MultipartRequestWrapper(request);

                wrapper.getParameterMap();
                CommonsMultipartRequestHandler handler = new CommonsMultipartRequestHandler();
                handler.handleRequest(request);
                Hashtable<String, FormFile> table = handler.getFileElements();
                FormFile file = null;            
                file = table.get("fichero_visitas_jus");            
                byte[] data = file.getFileData();
                ByteArrayInputStream bais = new ByteArrayInputStream(data);
                HSSFWorkbook workbook = new HSSFWorkbook(bais);

                HSSFSheet sheet = workbook.getSheetAt(0);

                int filaIni = sheet.getFirstRowNum();
                int filaFin = sheet.getLastRowNum();

                HSSFRow row = null;

                List<EcaVisProspectoresVO> filasImportar = new ArrayList<EcaVisProspectoresVO>();
                EcaVisProspectoresVO vis = null;
                String validacion = null;
                int i = 0;
                EcaSolicitudVO sol = MeLanbide44Manager.getInstance().getDatosSolicitud(numExpediente, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                try
                {
                    for(i = filaIni+1; i <= filaFin; i++)
                    {
                        row = sheet.getRow(i);

                        vis = (EcaVisProspectoresVO)MeLanbide44MappingUtils.getInstance().map(row, EcaVisProspectoresVO.class);

                        if(vis != null )
                        {                         
                            EcaJusProspectoresVO pros=null;    
                                if ( vis.getFecVisita()!=null){
                                    pros = MeLanbide44Manager.getInstance().getProspectorJustificacionPorNIF(sol, vis.getNifProspector(), vis.getFecVisita(), this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                                }
                                if (pros!=null){
                                    vis.setJusProspectoresCod(pros.getJusProspectoresCod());

                                    filasImportar.add(vis);
                                }else {                            
                                    //throw new ExcelRowMappingException("2");                            
                                    ExcelRowMappingException erme = new ExcelRowMappingException("2");
                                    erme.setMensaje(String.format(MeLanbide44I18n.getInstance().getMensaje(codIdioma,"error.NifProspectorNoEncontrado"), MeLanbide44Utils.fromColNumberToExcelColName(2), i+1));
                                    throw erme;
                                }                        
                        }
                    }
                     int filasImportadas = 0;
                    filasImportadas = MeLanbide44Manager.getInstance().importarVisitas(numExpediente, filasImportar, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));

                    if(filasImportadas < filasImportar.size())
                    {
                        request.getSession().setAttribute("mensajeImportar", MeLanbide44I18n.getInstance().getMensaje(codIdioma, "error.errorImportarGen"));
                    }
                    else
                    {
                        request.getSession().setAttribute("mensajeImportar", MeLanbide44I18n.getInstance().getMensaje(codIdioma, "msg.registrosImportadosOK_1")+" "+filasImportadas+" "+MeLanbide44I18n.getInstance().getMensaje(codIdioma, "msg.registrosImportadosOK_2"));
                    }
                }
                catch(ExcelRowMappingException erme)
                {
                    String mensajeImportar = null;
                    if(erme.getMensaje() == null || erme.getMensaje().equals(""))
                    {
                        mensajeImportar = MeLanbide44Utils.crearMensajeDatoExcelNoValido(erme, i+1, codIdioma);
                        if (erme.getCampo().equals("2"))
                            mensajeImportar = mensajeImportar+" "+MeLanbide44I18n.getInstance().getMensaje(codIdioma,"error.NifProspectorNoEncontrado");
                    }
                    else
                    {
                        mensajeImportar = erme.getMensaje();
                    }
                    request.getSession().setAttribute("mensajeImportar", mensajeImportar);  
                }
            }
            catch(FilaSeguimientoPrepNoValidaException ex)
            {
                log.error("Error Procesando Peticion : " + ex.getMessage(), ex);
                request.getSession().setAttribute("mensajeImportar", MeLanbide44I18n.getInstance().getMensaje(codIdioma, "error.datosNoValidosImportar"));
            }
            catch(IOException ex)
            {
                log.error("Error Procesando Peticion : " + ex.getMessage(), ex);
                request.getSession().setAttribute("mensajeImportar", MeLanbide44I18n.getInstance().getMensaje(codIdioma, "error.errorProcesarExcel"));
            }
            catch(Exception ex)
            {
                log.error("Error Procesando Peticion : " + ex.getMessage(), ex);
                request.getSession().setAttribute("mensajeImportar", MeLanbide44I18n.getInstance().getMensaje(codIdioma, "error.errorImportarGen"));
            }
        }else{//a partir de 2016
            try
            {   
                log.info("Iniciamos carga anios >= 2016");
                MultipartRequestWrapper wrapper = new MultipartRequestWrapper(request);

                wrapper.getParameterMap();
                CommonsMultipartRequestHandler handler = new CommonsMultipartRequestHandler();
                handler.handleRequest(request);
                Hashtable<String, FormFile> table = handler.getFileElements();
                FormFile file = null;            
                file = table.get("fichero_visitas_jus");            
                byte[] data = file.getFileData();
                ByteArrayInputStream bais = new ByteArrayInputStream(data);
                HSSFWorkbook workbook = new HSSFWorkbook(bais);

                HSSFSheet sheet = workbook.getSheetAt(0);

                int filaIni = sheet.getFirstRowNum();
                int filaFin = sheet.getLastRowNum();

                HSSFRow row = null;
                String mensajesError = "";
                List<ExcelRowMappingException> errores = new ArrayList<ExcelRowMappingException>();
                List<EcaVisProspectores2016VO> filasImportar = new ArrayList<EcaVisProspectores2016VO>();
                EcaVisProspectores2016VO vis = null;
                HashMap<EcaVisProspectores2016VO, List<ExcelRowMappingException>> map = new HashMap();
                String validacion = null;
                int i = 0;
                log.info("variables Inicializadas, recogemos datos Solicitud...");
                EcaSolicitudVO sol = MeLanbide44Manager.getInstance().getDatosSolicitud(numExpediente, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                log.info("Datos Solicitud Recogidos...");
                try
                {
                    for(i = filaIni+1; i <= filaFin; i++)
                    {
                        row = sheet.getRow(i);
                        try{
                            // vis = (EcaVisProspectores2016VO)MeLanbide44MappingUtils.getInstance().map(row, EcaVisProspectores2016VO.class);
                            log.info("Vamos a validar los datos del Excel...");
                            map = (HashMap) MeLanbide44MappingUtils.getInstance().map(row, EcaVisProspectores2016VO.class);
                            log.info("Validacion terminada ...");
                            List<ExcelRowMappingException> erroresVis = new ArrayList<ExcelRowMappingException>();
                            if (map != null) {
                                log.info("El map de la validacion viene con Datos. Obtenemos el Objeto EcaVisProspectores2016VO y los Errores List<ExcelRowMappingException>");
                                erroresVis = (List<ExcelRowMappingException>) map.get("errores");
                                log.info("Obtenida lista de errores List<ExcelRowMappingException>");
                                vis = (EcaVisProspectores2016VO) map.get("visita");
                                log.info("Obtenido el Objeto EcaVisProspectores2016VO ");
                            } else {
                                log.error("El mapeo de los datos del Excel, se ha devuelto a NULL no se proesan ni errores ni datos para la carga...");
                            }
                            for (int j = 0; j < erroresVis.size(); j++) {
                                mensajesError = MeLanbide44Utils.crearMensajeDatoExcelNoValido(erroresVis.get(j), i + 1, codIdioma);//cambiado
                                erroresVis.get(j).setMensaje(mensajesError);
                                errores.add(erroresVis.get(j));
                            }                            
                        }catch(ExcelRowMappingException erme){
                            if(erme.getMensaje() == null || erme.getMensaje().equals(""))
                            {
                                mensajesError = MeLanbide44Utils.crearMensajeDatoExcelNoValido(erme, i+1, codIdioma);
                                if (erme.getCampo().equals("2")){
                                    mensajesError = mensajesError+" "+MeLanbide44I18n.getInstance().getMensaje(codIdioma,"error.NifProspectorNoEncontrado");
                                    erme.setMensaje(mensajesError);
                                     errores.add(erme);
                                }
                            }
                            else
                            {
                                //mensajesError = erme.getMensaje();
                                 errores.add(erme);
                            }
                            //request.getSession().setAttribute("mensajeImportar", mensajeImportar);  
                        }
                        if(vis != null )
                        {
                            log.info("Vamos a procesar la visita...");
                            EcaJusProspectoresVO pros=null;    
                                if ( vis.getFecVisita()!=null){
                                    pros = MeLanbide44Manager.getInstance().getProspectorJustificacionPorNIF(sol, vis.getNifProspector(), vis.getFecVisita(), this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                                }
                                if (pros!=null){
                                    vis.setJusProspectoresCod(pros.getJusProspectoresCod());
                                    filasImportar.add(vis);
                                }else {                                                                                          
                                    ExcelRowMappingException erme = new ExcelRowMappingException("2");
                                    erme.setMensaje(String.format(MeLanbide44I18n.getInstance().getMensaje(codIdioma,"error.NifProspectorNoEncontrado"), MeLanbide44Utils.fromColNumberToExcelColName(2), i+1));
                                    //throw erme;                                    
                                    //mensajesError = mensajesError+" "+String.format(MeLanbide44I18n.getInstance().getMensaje(codIdioma,"error.NifProspectorNoEncontrado"), MeLanbide44Utils.fromColNumberToExcelColName(2), i+1);
                                     errores.add(erme);
                                }                        
                        }
                    }
                    int filasImportadas = 0;
                    if (errores.size()==0){                    
                    //if ("".equals(mensajesError)){                    
                        filasImportadas = MeLanbide44Manager.getInstance().importarVisitas2016(numExpediente, filasImportar, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));

                        if(filasImportadas < filasImportar.size())
                        {
                            request.getSession().setAttribute("mensajeImportar", MeLanbide44I18n.getInstance().getMensaje(codIdioma, "error.errorImportarGen"));
                        }
                        else
                        {
                            request.getSession().setAttribute("mensajeImportar", MeLanbide44I18n.getInstance().getMensaje(codIdioma, "msg.registrosImportadosOK_1")+" "+filasImportadas+" "+MeLanbide44I18n.getInstance().getMensaje(codIdioma, "msg.registrosImportadosOK_2"));
                        }
                    }else {
                        mensajesError="";
                        for (int j=0;j<errores.size();j++){                                 
                                 mensajesError+=errores.get(j).getMensaje()+ "<br/>";                                
                             }
                        request.getSession().setAttribute("mensajeImportar", mensajesError);
                    }
                }
                catch(ExcelRowMappingException erme)
                {
                    String mensajeImportar = null;
                    if(erme.getMensaje() == null || erme.getMensaje().equals(""))
                    {
                        mensajeImportar = MeLanbide44Utils.crearMensajeDatoExcelNoValido(erme, i+1, codIdioma);
                        if (erme.getCampo().equals("2"))
                            mensajeImportar = mensajeImportar+" "+MeLanbide44I18n.getInstance().getMensaje(codIdioma,"error.NifProspectorNoEncontrado");
                    }
                    else
                    {
                        mensajeImportar = erme.getMensaje();
                    }
                    request.getSession().setAttribute("mensajeImportar", mensajeImportar);  
                }
            }
            catch(FilaSeguimientoPrepNoValidaException ex)
            {
                log.error("Error Procesando Peticion : " + ex.getMessage(), ex);
                request.getSession().setAttribute("mensajeImportar", MeLanbide44I18n.getInstance().getMensaje(codIdioma, "error.datosNoValidosImportar"));
            }
            catch(IOException ex)
            {
                log.error("Error Procesando Peticion : " + ex.getMessage(), ex);
                request.getSession().setAttribute("mensajeImportar", MeLanbide44I18n.getInstance().getMensaje(codIdioma, "error.errorProcesarExcel"));
            }
            catch(Exception ex)
            {
                log.error("Error Procesando Peticion : " + ex.getMessage(), ex);
                request.getSession().setAttribute("mensajeImportar", MeLanbide44I18n.getInstance().getMensaje(codIdioma, "error.errorImportarGen"));
            }
                        
        }
        return "/jsp/extension/melanbide44/justificacion/recargarListaVisitas.jsp";
    }
   
   public String cargarNuevaVisitaProspector(int codOrganizacion,  int codTramite, int ocurrenciaTramite,String numExpediente,HttpServletRequest request,HttpServletResponse response)
    {
        
        int codIdioma = 1;
        
        UsuarioValueObject usuario = (UsuarioValueObject) request.getSession().getAttribute("usuario");
        if(usuario != null)
        {
            codIdioma = usuario.getIdioma();
        }
        try
        {
            String prospector = (String)request.getParameter("prospector");
           
            String opcion = (String)request.getParameter("opcion");
            if(opcion != null)
            {
                if(opcion.equalsIgnoreCase("nuevo"))
                {
                    //Cargar nuevo preparador
                }
                else if(opcion.equalsIgnoreCase("modificar") || opcion.equalsIgnoreCase("consultar"))
                {
                    //Cargar modificar visita
                    String idVis = (String)request.getParameter("idVisModificar");
                    if(idVis != null)
                    {
                        
                        String[] datos = idVis.split("_");
                        
                        EcaVisProspectoresVO vis = new EcaVisProspectoresVO();
                        vis.setJusProspectoresCod(Integer.parseInt(datos[0]));
                        vis.setVisProspectoresCod(Integer.parseInt(datos[1]));                        
                        vis = MeLanbide44Manager.getInstance().getVisitaProspectorPorId(vis, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                        if(vis != null)
                        {
                            request.setAttribute("visitaModif", vis);
                        }
                    }
                    if(opcion.equalsIgnoreCase("consultar"))
                    {
                        request.setAttribute("consulta", true);
                    }
                                  
                   
                }
                
                List<SelectItem> listaSector =  MeLanbide44Manager.getInstance().getListaSectorActividad( codIdioma, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                List<SelectItem> listaProvincia =  MeLanbide44Manager.getInstance().getListaProvincias( codIdioma, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));            
                List<SelectItem> listaCumple =  MeLanbide44Manager.getInstance().getListaCumpleLismi( codIdioma, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                List<SelectItem> listaResultado =  MeLanbide44Manager.getInstance().getListaResultadoFinal( codIdioma, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                              
                request.setAttribute("lstSector", listaSector);
                request.setAttribute("lstProvincia", listaProvincia);
                request.setAttribute("lstCumpleLismi", listaCumple);
                request.setAttribute("lstResultado", listaResultado);     
                
                request.setAttribute("prospector", prospector);    
                if (prospector !=null){
                    EcaJusProspectoresVO pros = new EcaJusProspectoresVO();
                    pros.setJusProspectoresCod(Integer.parseInt(prospector));
                    pros = MeLanbide44Manager.getInstance().getProspectorJustificacionPorId(pros, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                    request.setAttribute("nifpros", pros.getNif());    
                    log.debug("nifprospector:"+pros.getNif());
                }
            }
        }
        catch(Exception ex)
        {
            
        }   
        return "/jsp/extension/melanbide44/justificacion/nuevaVisitaProspector.jsp";
    }
   
   public void guardarVisitaProspector(int codOrganizacion,  int codTramite, int ocurrenciaTramite,String numExpediente,HttpServletRequest request,HttpServletResponse response)
    {
        int codIdioma = 1;    
        try
        {
            UsuarioValueObject usuario = (UsuarioValueObject) request.getSession().getAttribute("usuario");
            if(usuario != null)
            {
                codIdioma = usuario.getIdioma();
            }
        }
        catch(Exception ex)
        {

        }
        
        String codigoOperacion = "0";
        List<FilaVisProspectoresVO> visitas = null;
        try
        {
            AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
            String tiposeg ="0";
            String sexosegs="";
            String colectivosegs="";
            
            List<Integer> listaSector =  MeLanbide44Manager.getInstance().getListaCodigosSectorActividad( adapt);
            List<Integer> listaProvincias =  MeLanbide44Manager.getInstance().getListaCodigosProvincia( adapt);
            List<Integer> listaCumple =  MeLanbide44Manager.getInstance().getListaCodigosCumple( adapt);
            List<Integer> listaResultado =  MeLanbide44Manager.getInstance().getListaCodigosResultado( adapt);
            
            EcaJusProspectoresVO prosvis=null;
            EcaSolicitudVO sol = MeLanbide44Manager.getInstance().getDatosSolicitud(numExpediente, adapt);
            if(sol != null && sol.getSolicitudCod() != null)
            {
                    
                SimpleDateFormat format = new SimpleDateFormat(ConstantesMeLanbide44.FORMATO_FECHA);
                //Recojo los parámetros
                String idPros = (String)request.getParameter("idPros");
                String idVis = (String)request.getParameter("idVis");
                String cif = (String)request.getParameter("cif");
                String empresa = (String)request.getParameter("empresa");
                
                String sector = (String)request.getParameter("sector");
                String direccion = (String)request.getParameter("direccion");                
                String cpostal = (String)request.getParameter("cpostal");
                String localidad = (String)request.getParameter("localidad");
                String provincia = (String)request.getParameter("provincia");
                String personaContacto = (String)request.getParameter("personaContacto");
                String puesto = (String)request.getParameter("puesto");
                String email = (String)request.getParameter("email");
                String telefono = (String)request.getParameter("telefono");
                String numtrab = (String)request.getParameter("numtrab");                
                String numtrabdisc = (String)request.getParameter("numtrabdisc");
                String fecVisita = (String)request.getParameter("fecVisita");
                 String cumple = (String)request.getParameter("cumple");
                  String resultado = (String)request.getParameter("resultado");
                String observaciones = (String)request.getParameter("observaciones");
                tiposeg = (String)request.getParameter("tiposeg");
                String nifpros = (String)request.getParameter("nifProspector");
                                
                String idprosvis = (String)request.getParameter("prosvis");
                if (idprosvis!=null && !idprosvis.equals("")){
                    prosvis=new EcaJusProspectoresVO();
                    prosvis.setJusProspectoresCod(Integer.parseInt(idprosvis));
                    prosvis=MeLanbide44Manager.getInstance().getProspectorJustificacionPorId(prosvis, adapt);
                }

                EcaVisProspectoresVO vis = null;
                boolean soloFormato = false;
                if(idPros != null && !idPros.equals("") && idVis != null && !idVis.equals(""))
                {
                    vis = new EcaVisProspectoresVO();
                    vis.setJusProspectoresCod(Integer.parseInt(idPros));
                    vis.setVisProspectoresCod(Integer.parseInt(idVis));
                    vis = MeLanbide44Manager.getInstance().getVisitaProspectorPorId(vis, adapt);
                    soloFormato = true;
                }
                else
                {
                    vis = new EcaVisProspectoresVO();
                    EcaJusProspectoresVO pros = MeLanbide44Manager.getInstance().getProspectorJustificacionPorNIF(sol, nifpros.toUpperCase(), format.parse(fecVisita), this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                   
                    if (pros!=null)   {
                        vis.setJusProspectoresCod(pros.getJusProspectoresCod()); 
                        //nuevo marzo 2014 laura
                        vis.setNifProspector(nifpros.toUpperCase());
                    }
                }
                if(vis == null)
                {
                    codigoOperacion = "3";
                }
                else
                {
                    if (vis.getJusProspectoresCod()!=null)
                    {
                    vis.setCif(cif != null ? cif.toUpperCase() : null);
                    vis.setEmpresa(empresa != null ? empresa.toUpperCase() : null);                    
                    vis.setSector(sector != null && !sector.equals("") ? Integer.parseInt(sector)  : null);
                    vis.setDireccion(direccion != null ? direccion.toUpperCase() : null);               
                    vis.setCpostal(cpostal != null ? cpostal : null); 
                    vis.setLocalidad(localidad != null ? localidad.toUpperCase() : null);  
                    vis.setProvincia(provincia != null && !provincia.equals("") ? Integer.parseInt(provincia)  : null);  
                    vis.setPersContacto(personaContacto != null ? personaContacto.toUpperCase() : null);       
                    vis.setPuesto(puesto != null ? puesto.toUpperCase() : null);   
                    vis.setMail(email != null ? email.toUpperCase() : null);   
                    vis.setTelefono(telefono != null && !telefono.equals("") ? Integer.parseInt(telefono)  : null);
                    vis.setNumTrab(numtrab != null && !numtrab.equals("") ? Integer.parseInt(numtrab)  : null);
                    vis.setNumTrabDisc(numtrabdisc != null && !numtrabdisc.equals("") ? Integer.parseInt(numtrabdisc)  : null);
                    vis.setCumpleLismi(cumple != null && !cumple.equals("") ? Integer.parseInt(cumple) : null);
                    vis.setResultadoFinal(resultado != null && !resultado.equals("") ? Integer.parseInt(resultado) : null);
                    vis.setFecVisita(fecVisita != null && !fecVisita.equals("") ? format.parse(fecVisita) : null);
                    vis.setObservaciones(observaciones != null && !observaciones.equals("") ? observaciones.toUpperCase() : null);
                    
                    EcaConfiguracionVO config = MeLanbide44Manager.getInstance().getConfiguracionEca(MeLanbide44Utils.getEjercicioDeExpediente(numExpediente), adapt);
                    List<String> validacion = MeLanbide44Utils.validarEcaVisProspectoresVO(sol.getNumExp(), vis, listaSector, listaProvincias,listaCumple, listaResultado, config, codIdioma, true);
                    if(validacion == null || validacion.size() == 0)
                    {
                        MeLanbide44Manager.getInstance().guardarEcaVisProspectoresVO(vis, adapt);
                    }
                    else
                    {
                        //if (vis.getJusProspectoresCod()!=null)
                        codigoOperacion = "4";
                       
                    }
                    } else codigoOperacion = "5";
                }
            }
            else
            {
                codigoOperacion = "1";
            }
            
           visitas = MeLanbide44Manager.getInstance().getListaVisitas(sol,prosvis , codIdioma, adapt);
            
        }
        catch(Exception ex)
        {
            log.error(" Error : " + ex.getMessage(), ex);
            codigoOperacion = "2";
        }
        
        this.escribirListaVisProspectoresRequest(codigoOperacion, visitas, response);
    }
   
    public void eliminarVisitaProspectorJustificacion(int codOrganizacion,  int codTramite, int ocurrenciaTramite,String numExpediente,HttpServletRequest request,HttpServletResponse response)
    {
        
        String codigoOperacion = "0";
        List<FilaVisProspectoresVO> listaVisitas = new ArrayList<FilaVisProspectoresVO>();
        try
        {
            AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
            String idVis = (String)request.getParameter("idVis");
            String filtrar = (String)request.getParameter("filtrar");
            if(idVis == null || idVis.equals(""))
            {
                codigoOperacion = "3";
            }
            else
            {
                String[] datos = idVis.split("_");
                String idPros = datos[0];
                idVis = datos[1];
                Integer idP = null;
                Integer idV = null;
                try
                {
                    idP = Integer.parseInt(idPros);
                }
                catch(Exception ex)
                {
                    codigoOperacion = "3";
                }
                try
                {
                    idV = Integer.parseInt(idVis);
                }
                catch(Exception ex)
                {
                    codigoOperacion = "3";
                }
                if(idP != null && idV != null)
                {
                    EcaVisProspectoresVO vis = new EcaVisProspectoresVO();
                    vis.setJusProspectoresCod(idP);
                    vis.setVisProspectoresCod(idV);
                    int result = MeLanbide44Manager.getInstance().eliminarVisitaProspectorJustificacion(vis, adapt);
                    if(result <= 0)
                    {
                        codigoOperacion = "1";
                    }
                    else
                    {
                        EcaSolicitudVO sol = MeLanbide44Manager.getInstance().getDatosSolicitud(numExpediente, adapt);
                        if(sol != null)
                        {
                            EcaJusProspectoresVO pros = null;
                            if(filtrar != null && filtrar.equalsIgnoreCase("1"))
                            {
                                pros = new EcaJusProspectoresVO();
                                pros.setJusProspectoresCod(idP);
                                pros = MeLanbide44Manager.getInstance().getProspectorJustificacionPorId(pros, adapt);
                            }
                            listaVisitas = MeLanbide44Manager.getInstance().getListaVisitas(sol, pros, idV, adapt);
                        }
                    }
                }
                else
                {
                    codigoOperacion = "3";
                }
            }
        }
        catch(Exception ex)
        {
            codigoOperacion = "2";
        }
        if(codigoOperacion.equalsIgnoreCase("0"))
        {
            this.escribirListaVisProspectoresRequest(codigoOperacion, listaVisitas, response);
        }
        else
        {
            StringBuffer xmlSalida = new StringBuffer();
            xmlSalida.append("<RESPUESTA>");
                xmlSalida.append("<CODIGO_OPERACION>");
                    xmlSalida.append(codigoOperacion);
                xmlSalida.append("</CODIGO_OPERACION>");
            xmlSalida.append("</RESPUESTA>");
            try{
                response.setContentType("text/xml");
                response.setCharacterEncoding("UTF-8");
                PrintWriter out = response.getWriter();
                out.print(xmlSalida.toString());
                out.flush();
                out.close();
            }catch(Exception e){
                log.error(" Error : " + e.getMessage(), e);
            }//try-catch
        }
    }
   
    public void getNumeroSeguimientosInsercionesPreparador(int codOrganizacion,  int codTramite, int ocurrenciaTramite,String numExpediente,HttpServletRequest request,HttpServletResponse response)
    {
        String codigoOperacion = "0";
        Map<String, Integer> valores = new HashMap<String, Integer>();
        try
        {
            AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
            String idPrep = (String)request.getParameter("idPrep");
            if(idPrep == null || idPrep.equals(""))
            {
                codigoOperacion = "3";
            }
            else
            {
                Integer idP = null;
                try
                {
                    idP = Integer.parseInt(idPrep);
                }
                catch(Exception ex)
                {
                    codigoOperacion = "3";
                }
                if(idP != null)
                {
                    EcaSolicitudVO sol = MeLanbide44Manager.getInstance().getDatosSolicitud(numExpediente, adapt);
                    if(sol != null)
                    {
                        EcaJusPreparadoresVO prep = new EcaJusPreparadoresVO();
                        prep.setJusPreparadoresCod(idP);
                        prep.setSolicitud(sol.getSolicitudCod());
                        prep = MeLanbide44Manager.getInstance().getPreparadorJustificacionPorId(prep, adapt);
                        if(prep != null)
                        {
                            valores = MeLanbide44Manager.getInstance().getNumeroSeguimientosInsercionesPreparador(prep, adapt);
                        }
                    }
                    else
                    {
                        codigoOperacion = "3";
                    }
                }
                else
                {
                    codigoOperacion = "3";
                }
            }
        }
        catch(Exception ex)
        {
            codigoOperacion = "2";
        }
        Integer valor = null;
        StringBuffer xmlSalida = new StringBuffer();
        xmlSalida.append("<RESPUESTA>");
            xmlSalida.append("<CODIGO_OPERACION>");
                xmlSalida.append(codigoOperacion);
            xmlSalida.append("</CODIGO_OPERACION>");
            xmlSalida.append("<SEGUIMIENTOS>");
                valor = valores.get(ConstantesMeLanbide44.TIPO_SEGUIMIENTO_MELANBIDE44);
                xmlSalida.append(valor != null ? valor.toString() : "0");
            xmlSalida.append("</SEGUIMIENTOS>");
            xmlSalida.append("<INSERCIONES>");
                valor = valores.get(ConstantesMeLanbide44.TIPO_INSERCION_MELANBIDE44);
                xmlSalida.append(valor != null ? valor.toString() : "0");
            xmlSalida.append("</INSERCIONES>");
        xmlSalida.append("</RESPUESTA>");
        try{
            response.setContentType("text/xml");
            response.setCharacterEncoding("UTF-8");
            PrintWriter out = response.getWriter();
            out.print(xmlSalida.toString());
            out.flush();
            out.close();
        }catch(Exception e){
            log.error(" Error : " + e.getMessage(), e);
        }//try-catch
    }
   
    public void getNumeroVisitasProspector(int codOrganizacion,  int codTramite, int ocurrenciaTramite,String numExpediente,HttpServletRequest request,HttpServletResponse response)
    {
        String codigoOperacion = "0";
        Integer valor = 0;
        try
        {
            AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
            String idPrep = (String)request.getParameter("idPros");
            if(idPrep == null || idPrep.equals(""))
            {
                codigoOperacion = "3";
            }
            else
            {
                Integer idP = null;
                try
                {
                    idP = Integer.parseInt(idPrep);
                }
                catch(Exception ex)
                {
                    codigoOperacion = "3";
                }
                if(idP != null)
                {
                    EcaSolicitudVO sol = MeLanbide44Manager.getInstance().getDatosSolicitud(numExpediente, adapt);
                    if(sol != null)
                    {
                        EcaJusProspectoresVO pros = new EcaJusProspectoresVO();
                        pros.setJusProspectoresCod(idP);
                        pros.setSolicitud(sol.getSolicitudCod());
                        pros = MeLanbide44Manager.getInstance().getProspectorJustificacionPorId(pros, adapt);
                        if(pros != null)
                        {
                            valor = MeLanbide44Manager.getInstance().getNumeroVisitasProspector(pros, adapt);
                        }
                    }
                    else
                    {
                        codigoOperacion = "3";
                    }
                }
                else
                {
                    codigoOperacion = "3";
                }
            }
        }
        catch(Exception ex)
        {
            codigoOperacion = "2";
        }
        StringBuffer xmlSalida = new StringBuffer();
        xmlSalida.append("<RESPUESTA>");
            xmlSalida.append("<CODIGO_OPERACION>");
                xmlSalida.append(codigoOperacion);
            xmlSalida.append("</CODIGO_OPERACION>");
            xmlSalida.append("<VISITAS>");
                xmlSalida.append(valor != null ? valor.toString() : "0");
            xmlSalida.append("</VISITAS>");
        xmlSalida.append("</RESPUESTA>");
        try{
            response.setContentType("text/xml");
            response.setCharacterEncoding("UTF-8");
            PrintWriter out = response.getWriter();
            out.print(xmlSalida.toString());
            out.flush();
            out.close();
        }catch(Exception e){
            log.error(" Error : " + e.getMessage(), e);
        }//try-catch
    }
    
    public void getDatosAnexosSolicitud(int codOrganizacion,  int codTramite, int ocurrenciaTramite,String numExpediente,HttpServletRequest request,HttpServletResponse response)
    {
        DatosAnexosVO datosAnexos = null;
        DatosAnexosVO datosCarga = null;
        EcaSolicitudVO solicitud =null;
        String codigoOperacion = "0";
        try
        {  
            int codIdioma = 1;  

            try
            {
                UsuarioValueObject usuario = (UsuarioValueObject) request.getSession().getAttribute("usuario");
                if(usuario != null)
                {
                    codIdioma = usuario.getIdioma();
                }
            }
            catch(Exception ex)
            {
            }
            
            AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
            solicitud = MeLanbide44Manager.getInstance().getDatosSolicitud(numExpediente, adapt);
            datosAnexos = MeLanbide44Manager.getInstance().getDatosSolicitudAnexos(solicitud, adapt);
            
            /*try
            {
                List<FilaPreparadorSolicitudVO> preparadores = MeLanbide44Manager.getInstance().getListaPreparadoresSolicitud(solicitud, adapt, codIdioma);
                if(preparadores != null && !preparadores.isEmpty())
                {
                    int numPreparadores = 0;
                    BigDecimal imp = new BigDecimal("0.00");
                    for(FilaPreparadorSolicitudVO prep : preparadores)
                    {
                        if(prep.getErrores() == null || prep.getErrores().isEmpty())
                        {
                            numPreparadores++;
                            imp = imp.add(new BigDecimal(prep.getCostesSalarialesSS().replace(".", "").replace(",",".")));
                        }
                    }
                    datosAnexos.setNumPreparadores(new BigDecimal(numPreparadores));
                    datosAnexos.setImportePreparadores(imp);
                }
                
                restarDatosPreparadoresConErrores(preparadores, datosAnexos, adapt);
            }
            catch(Exception ex)
            {
                
            }*/
            
            /*try
            {
                List<FilaProspectorSolicitudVO> prospectores = MeLanbide44Manager.getInstance().getListaProspectoresSolicitud(solicitud, adapt, codIdioma);
                if(prospectores != null && !prospectores.isEmpty())
                {
                    int numProspectores = 0;
                    BigDecimal imp = new BigDecimal("0.00");
                    for(FilaProspectorSolicitudVO pros : prospectores)
                    {
                        //if(pros.getErrores() == null || pros.getErrores().isEmpty()  || (pros.getErrores().size()==1 && pros.getErrorCampo(11).equals("S")))
                        //{
                            numProspectores++;
                            imp = imp.add(new BigDecimal(pros.getCoste().replace(".", "").replace(",",".")));
                        //}
                    }
                    datosAnexos.setNumProspectores(new BigDecimal(numProspectores));
                    datosAnexos.setImporteProspectores(imp);
                }
            }
            catch(Exception ex)
            {
                
            }*/
            
            datosCarga = MeLanbide44Manager.getInstance().getDatosSolicitudCarga(solicitud, adapt);
        }
        catch(Exception ex)
        {
            log.error(" Error : " + ex.getMessage(), ex);
        }
        this.escribirDatosAnexosRequest(codigoOperacion, datosAnexos, solicitud, response);
    }
    
    private void escribirDatosAnexosRequest(String codigoOperacion, DatosAnexosVO datosAnexos, /*DatosAnexosVO datosCarga*/  EcaSolicitudVO solicitud, HttpServletResponse response)
    {
        StringBuffer xmlSalida = new StringBuffer();
            xmlSalida.append("<RESPUESTA>");
                xmlSalida.append("<CODIGO_OPERACION>");
                    xmlSalida.append(codigoOperacion);
                xmlSalida.append("</CODIGO_OPERACION>");
                    xmlSalida.append("<VALORES_ANEXOS>");   
                        xmlSalida.append("<C1H>"); 
                            xmlSalida.append(datosAnexos != null && datosAnexos.getC1H() != null ? datosAnexos.getC1H().toPlainString() : "");
                        xmlSalida.append("</C1H>");
                        xmlSalida.append("<C1M>"); 
                            xmlSalida.append(datosAnexos != null && datosAnexos.getC1M() != null ? datosAnexos.getC1M().toPlainString() : "");
                        xmlSalida.append("</C1M>");
                        xmlSalida.append("<C1T>"); 
                            xmlSalida.append(datosAnexos != null && datosAnexos.getC1T() != null ? datosAnexos.getC1T().toPlainString() : "");
                        xmlSalida.append("</C1T>");
                        xmlSalida.append("<C2H>"); 
                            xmlSalida.append(datosAnexos != null && datosAnexos.getC2H() != null ? datosAnexos.getC2H().toPlainString() : "");
                        xmlSalida.append("</C2H>");
                        xmlSalida.append("<C2M>"); 
                            xmlSalida.append(datosAnexos != null && datosAnexos.getC2M() != null ? datosAnexos.getC2M().toPlainString() : "");
                        xmlSalida.append("</C2M>");
                        xmlSalida.append("<C2T>"); 
                            xmlSalida.append(datosAnexos != null && datosAnexos.getC2T() != null ? datosAnexos.getC2T().toPlainString() : "");
                        xmlSalida.append("</C2T>");
                        xmlSalida.append("<C3H>"); 
                            xmlSalida.append(datosAnexos != null && datosAnexos.getC3H() != null ? datosAnexos.getC3H().toPlainString() : "");
                        xmlSalida.append("</C3H>");
                        xmlSalida.append("<C3M>"); 
                            xmlSalida.append(datosAnexos != null && datosAnexos.getC3M() != null ? datosAnexos.getC3M().toPlainString() : "");
                        xmlSalida.append("</C3M>");
                        xmlSalida.append("<C3T>"); 
                            xmlSalida.append(datosAnexos != null && datosAnexos.getC3T() != null ? datosAnexos.getC3T().toPlainString() : "");
                        xmlSalida.append("</C3T>");
                        xmlSalida.append("<C4H>"); 
                            xmlSalida.append(datosAnexos != null && datosAnexos.getC4H() != null ? datosAnexos.getC4H().toPlainString() : "");
                        xmlSalida.append("</C4H>");
                        xmlSalida.append("<C4M>"); 
                            xmlSalida.append(datosAnexos != null && datosAnexos.getC4M() != null ? datosAnexos.getC4M().toPlainString() : "");
                        xmlSalida.append("</C4M>");
                        xmlSalida.append("<C4T>"); 
                            xmlSalida.append(datosAnexos != null && datosAnexos.getC4T() != null ? datosAnexos.getC4T().toPlainString() : "");
                        xmlSalida.append("</C4T>");
                        xmlSalida.append("<C5H>"); 
                            xmlSalida.append(datosAnexos != null && datosAnexos.getC5H() != null ? datosAnexos.getC5H().toPlainString() : "");
                        xmlSalida.append("</C5H>");
                        xmlSalida.append("<C5M>"); 
                            xmlSalida.append(datosAnexos != null && datosAnexos.getC5M() != null ? datosAnexos.getC5M().toPlainString() : "");
                        xmlSalida.append("</C5M>");
                        xmlSalida.append("<C5T>"); 
                            xmlSalida.append(datosAnexos != null && datosAnexos.getC5T() != null ? datosAnexos.getC5T().toPlainString() : "");
                        xmlSalida.append("</C5T>");
                        xmlSalida.append("<C6H>"); 
                            xmlSalida.append(datosAnexos != null && datosAnexos.getC6H() != null ? datosAnexos.getC6H().toPlainString() : "");
                        xmlSalida.append("</C6H>");
                        xmlSalida.append("<C6M>"); 
                            xmlSalida.append(datosAnexos != null && datosAnexos.getC6M() != null ? datosAnexos.getC6M().toPlainString() : "");
                        xmlSalida.append("</C6M>");
                        xmlSalida.append("<C6T>"); 
                            xmlSalida.append(datosAnexos != null && datosAnexos.getC6T() != null ? datosAnexos.getC6T().toPlainString() : "");
                        xmlSalida.append("</C6T>");
                        xmlSalida.append("<TOT_ACTUACIONES>"); 
                            xmlSalida.append(datosAnexos != null && datosAnexos.getSeguimientosAnt() != null ? datosAnexos.getSeguimientosAnt().toPlainString() : "");
                        xmlSalida.append("</TOT_ACTUACIONES>");
                        xmlSalida.append("<PROSPECTORES_NUM>"); 
                            xmlSalida.append(datosAnexos != null && datosAnexos.getNumProspectores() != null ? datosAnexos.getNumProspectores().toPlainString() : "");
                        xmlSalida.append("</PROSPECTORES_NUM>");
                        xmlSalida.append("<PROSPECTORES_SOL>"); 
                            xmlSalida.append(datosAnexos != null && datosAnexos.getImporteProspectores() != null ? datosAnexos.getImporteProspectores().toPlainString() : "");
                        xmlSalida.append("</PROSPECTORES_SOL>");
                        xmlSalida.append("<PREPARADORES_NUM>"); 
                            xmlSalida.append(datosAnexos != null && datosAnexos.getNumPreparadores() != null ? datosAnexos.getNumPreparadores().toPlainString() : "");
                        xmlSalida.append("</PREPARADORES_NUM>");
                        xmlSalida.append("<PREPARADORES_SOL>"); 
                            xmlSalida.append(datosAnexos != null && datosAnexos.getImportePreparadores() != null ? datosAnexos.getImportePreparadores().toPlainString() : "");
                        xmlSalida.append("</PREPARADORES_SOL>");
                        xmlSalida.append("<GASTOS>"); 
                            xmlSalida.append(datosAnexos != null && datosAnexos.getGastos() != null ? datosAnexos.getGastos().toPlainString() : "");
                        xmlSalida.append("</GASTOS>");
                        xmlSalida.append("<MAX_SUBV>"); 
                            xmlSalida.append(datosAnexos != null && datosAnexos.getMaxSubvencionable() != null ? datosAnexos.getMaxSubvencionable().toPlainString() : "");
                        xmlSalida.append("</MAX_SUBV>");
                    xmlSalida.append("</VALORES_ANEXOS>");   
                    
                    xmlSalida.append("<VALORES_CONCEDIDO>");   
                        xmlSalida.append("<C1H>"); 
                            xmlSalida.append(solicitud != null && solicitud.getInserC1HConc() != null ? solicitud.getInserC1HConc().toString() : "");
                        xmlSalida.append("</C1H>");
                        xmlSalida.append("<C1M>"); 
                            xmlSalida.append(solicitud != null && solicitud.getInserC1MConc() != null ? solicitud.getInserC1MConc().toString() : "");
                        xmlSalida.append("</C1M>");
                        xmlSalida.append("<C1T>"); 
                            xmlSalida.append(solicitud != null && solicitud.getInserC1HConc() != null && solicitud.getInserC1MConc() != null ? (solicitud.getInserC1HConc()+solicitud.getInserC1MConc()) : "");
                        xmlSalida.append("</C1T>");
                        xmlSalida.append("<C2H>"); 
                            xmlSalida.append(solicitud != null && solicitud.getInserC2HConc() != null ? solicitud.getInserC2HConc().toString() : "");
                        xmlSalida.append("</C2H>");
                        xmlSalida.append("<C2M>"); 
                            xmlSalida.append(solicitud != null && solicitud.getInserC2MConc() != null ? solicitud.getInserC2MConc().toString() : "");
                        xmlSalida.append("</C2M>");
                        xmlSalida.append("<C2T>"); 
                            xmlSalida.append(solicitud != null && solicitud.getInserC2HConc() != null && solicitud.getInserC2MConc() != null ? (solicitud.getInserC2HConc()+solicitud.getInserC2MConc()) : "");
                        xmlSalida.append("</C2T>");
                        xmlSalida.append("<C3H>"); 
                            xmlSalida.append(solicitud != null && solicitud.getInserC3HConc() != null ? solicitud.getInserC3HConc().toString() : "");
                        xmlSalida.append("</C3H>");
                        xmlSalida.append("<C3M>"); 
                            xmlSalida.append(solicitud != null && solicitud.getInserC3MConc() != null ? solicitud.getInserC3MConc().toString() : "");
                        xmlSalida.append("</C3M>");
                        xmlSalida.append("<C3T>"); 
                            xmlSalida.append(solicitud != null && solicitud.getInserC3HConc() != null && solicitud.getInserC3MConc() != null ? (solicitud.getInserC3HConc()+solicitud.getInserC3MConc()) : "");
                        xmlSalida.append("</C3T>");
                        xmlSalida.append("<C4H>"); 
                            xmlSalida.append(solicitud != null && solicitud.getInserC4HConc() != null ? solicitud.getInserC4HConc().toString() : "");
                        xmlSalida.append("</C4H>");
                        xmlSalida.append("<C4M>"); 
                            xmlSalida.append(solicitud != null && solicitud.getInserC4MConc() != null ? solicitud.getInserC4MConc().toString() : "");
                        xmlSalida.append("</C4M>");
                        xmlSalida.append("<C4T>"); 
                            xmlSalida.append(solicitud != null && solicitud.getInserC4HConc() != null && solicitud.getInserC4MConc() != null ? (solicitud.getInserC4HConc()+solicitud.getInserC4MConc()) : "");
                        xmlSalida.append("</C4T>");
                         xmlSalida.append("<C5H>"); 
                            xmlSalida.append(solicitud != null && solicitud.getInserC5HConc() != null ? solicitud.getInserC5HConc().toString() : "");
                        xmlSalida.append("</C5H>");
                        xmlSalida.append("<C5M>"); 
                            xmlSalida.append(solicitud != null && solicitud.getInserC5MConc() != null ? solicitud.getInserC5MConc().toString() : "");
                        xmlSalida.append("</C5M>");
                        xmlSalida.append("<C5T>"); 
                            xmlSalida.append(solicitud != null && solicitud.getInserC5HConc() != null && solicitud.getInserC5MConc() != null ? (solicitud.getInserC4HConc()+solicitud.getInserC4MConc()) : "");
                        xmlSalida.append("</C5T>");
                         xmlSalida.append("<C6H>"); 
                            xmlSalida.append(solicitud != null && solicitud.getInserC6HConc() != null ? solicitud.getInserC6HConc().toString() : "");
                        xmlSalida.append("</C6H>");
                        xmlSalida.append("<C6M>"); 
                            xmlSalida.append(solicitud != null && solicitud.getInserC6MConc() != null ? solicitud.getInserC6MConc().toString() : "");
                        xmlSalida.append("</C6M>");
                        xmlSalida.append("<C6T>"); 
                            xmlSalida.append(solicitud != null && solicitud.getInserC6HConc() != null && solicitud.getInserC6MConc() != null ? (solicitud.getInserC4HConc()+solicitud.getInserC4MConc()) : "");
                        xmlSalida.append("</C6T>");
                        xmlSalida.append("<TOT_ACTUACIONES>"); 
                            xmlSalida.append(solicitud != null && solicitud.getSegActuacionesConc() != null ? solicitud.getSegActuacionesConc().toPlainString() : "");
                        xmlSalida.append("</TOT_ACTUACIONES>");
                        xmlSalida.append("<PROSPECTORES_NUM>"); 
                            xmlSalida.append(solicitud != null && solicitud.getProspectoresNumConc() != null ? solicitud.getProspectoresNumConc().toString() : "");
                        xmlSalida.append("</PROSPECTORES_NUM>");
                        xmlSalida.append("<PROSPECTORES_SOL>"); 
                            xmlSalida.append(solicitud != null && solicitud.getProspectoresImpConc() != null ? solicitud.getProspectoresImpConc().toPlainString() : "");
                        xmlSalida.append("</PROSPECTORES_SOL>");
                        xmlSalida.append("<PREPARADORES_NUM>"); 
                            xmlSalida.append(solicitud != null && solicitud.getPreparadoresNumConc() != null ? solicitud.getPreparadoresNumConc().toString() : "");
                        xmlSalida.append("</PREPARADORES_NUM>");
                        xmlSalida.append("<PREPARADORES_SOL>"); 
                            xmlSalida.append(solicitud != null && solicitud.getPreparadoresImpConc() != null ? solicitud.getPreparadoresImpConc().toPlainString() : "");
                        xmlSalida.append("</PREPARADORES_SOL>");
                        xmlSalida.append("<GASTOS>"); 
                            xmlSalida.append(solicitud != null && solicitud.getGastosConc() != null ? solicitud.getGastosConc().toPlainString() : "");
                        xmlSalida.append("</GASTOS>");
                    xmlSalida.append("</VALORES_CONCEDIDO>");
                    
                    /*xmlSalida.append("<VALORES_CARGA>");   
                        xmlSalida.append("<C1H>"); 
                            xmlSalida.append(datosCarga != null && datosCarga.getC1H() != null ? datosCarga.getC1H().toPlainString() : "");
                        xmlSalida.append("</C1H>");
                        xmlSalida.append("<C1M>"); 
                            xmlSalida.append(datosCarga != null && datosCarga.getC1M() != null ? datosCarga.getC1M().toPlainString() : "");
                        xmlSalida.append("</C1M>");
                        xmlSalida.append("<C1T>"); 
                            xmlSalida.append(datosCarga != null && datosCarga.getC1T() != null ? datosCarga.getC1T().toPlainString() : "");
                        xmlSalida.append("</C1T>");
                        xmlSalida.append("<C2H>"); 
                            xmlSalida.append(datosCarga != null && datosCarga.getC2H() != null ? datosCarga.getC2H().toPlainString() : "");
                        xmlSalida.append("</C2H>");
                        xmlSalida.append("<C2M>"); 
                            xmlSalida.append(datosCarga != null && datosCarga.getC2M() != null ? datosCarga.getC2M().toPlainString() : "");
                        xmlSalida.append("</C2M>");
                        xmlSalida.append("<C2T>"); 
                            xmlSalida.append(datosCarga != null && datosCarga.getC2T() != null ? datosCarga.getC2T().toPlainString() : "");
                        xmlSalida.append("</C2T>");
                        xmlSalida.append("<C3H>"); 
                            xmlSalida.append(datosCarga != null && datosCarga.getC3H() != null ? datosCarga.getC3H().toPlainString() : "");
                        xmlSalida.append("</C3H>");
                        xmlSalida.append("<C3M>"); 
                            xmlSalida.append(datosCarga != null && datosCarga.getC3M() != null ? datosCarga.getC3M().toPlainString() : "");
                        xmlSalida.append("</C3M>");
                        xmlSalida.append("<C3T>"); 
                            xmlSalida.append(datosCarga != null && datosCarga.getC3T() != null ? datosCarga.getC3T().toPlainString() : "");
                        xmlSalida.append("</C3T>");
                        xmlSalida.append("<C4H>"); 
                            xmlSalida.append(datosCarga != null && datosCarga.getC4H() != null ? datosCarga.getC4H().toPlainString() : "");
                        xmlSalida.append("</C4H>");
                        xmlSalida.append("<C4M>"); 
                            xmlSalida.append(datosCarga != null && datosCarga.getC4M() != null ? datosCarga.getC4M().toPlainString() : "");
                        xmlSalida.append("</C4M>");
                        xmlSalida.append("<C4T>"); 
                            xmlSalida.append(datosCarga != null && datosCarga.getC4T() != null ? datosCarga.getC4T().toPlainString() : "");
                        xmlSalida.append("</C4T>");
                        xmlSalida.append("<TOT_ACTUACIONES>"); 
                            xmlSalida.append(datosCarga != null && datosCarga.getSeguimientosAnt() != null ? datosCarga.getSeguimientosAnt().toPlainString() : "");
                        xmlSalida.append("</TOT_ACTUACIONES>");
                        xmlSalida.append("<PROSPECTORES_NUM>"); 
                            xmlSalida.append(datosCarga != null && datosCarga.getNumProspectores() != null ? datosCarga.getNumProspectores().toPlainString() : "");
                        xmlSalida.append("</PROSPECTORES_NUM>");
                        xmlSalida.append("<PROSPECTORES_SOL>"); 
                            xmlSalida.append(datosCarga != null && datosCarga.getImporteProspectores() != null ? datosCarga.getImporteProspectores().toPlainString() : "");
                        xmlSalida.append("</PROSPECTORES_SOL>");
                        xmlSalida.append("<PREPARADORES_NUM>"); 
                            xmlSalida.append(datosCarga != null && datosCarga.getNumPreparadores() != null ? datosCarga.getNumPreparadores().toPlainString() : "");
                        xmlSalida.append("</PREPARADORES_NUM>");
                        xmlSalida.append("<PREPARADORES_SOL>"); 
                            xmlSalida.append(datosCarga != null && datosCarga.getImportePreparadores() != null ? datosCarga.getImportePreparadores().toPlainString() : "");
                        xmlSalida.append("</PREPARADORES_SOL>");
                        xmlSalida.append("<GASTOS>"); 
                            xmlSalida.append(datosCarga != null && datosCarga.getGastos() != null ? datosCarga.getGastos().toPlainString() : "");
                        xmlSalida.append("</GASTOS>");
                    xmlSalida.append("</VALORES_CARGA>");*/
                    
                    
            xmlSalida.append("</RESPUESTA>");
            try{
                response.setContentType("text/xml");
                response.setCharacterEncoding("UTF-8");
                PrintWriter out = response.getWriter();
                out.print(xmlSalida.toString());
                out.flush();
                out.close();
            }catch(Exception e){
                log.error(" Error : " + e.getMessage(), e);
            }//try-catch
    }
   
   private void escribirListaVisProspectoresRequest(String codigoOperacion, List<FilaVisProspectoresVO> visitas, HttpServletResponse response)
    {
        StringBuffer xmlSalida = new StringBuffer();
            xmlSalida.append("<RESPUESTA>");
                xmlSalida.append("<CODIGO_OPERACION>");
                    xmlSalida.append(codigoOperacion);
                xmlSalida.append("</CODIGO_OPERACION>");
                int i = 0;
                for(FilaVisProspectoresVO vis : visitas)
                {
                    xmlSalida.append("<FILA>");                    
                        xmlSalida.append("<ID>");
                            xmlSalida.append("<VALOR>");
                                xmlSalida.append(vis.getCodProspector()+"_"+vis.getIdvisita());
                            xmlSalida.append("</VALOR>");
                            xmlSalida.append("<ERROR>");
                                xmlSalida.append(ConstantesMeLanbide44.FALSO);
                            xmlSalida.append("</ERROR>");
                        xmlSalida.append("</ID>");
                        xmlSalida.append("<CIF>");
                            xmlSalida.append("<VALOR>");
                                xmlSalida.append(vis.getCif());
                            xmlSalida.append("</VALOR>");
                            xmlSalida.append("<ERROR>");
                                xmlSalida.append(vis.getErrorCampo(FilaVisProspectoresVO.POS_CAMPO_CIF));
                            xmlSalida.append("</ERROR>");
                        xmlSalida.append("</CIF>");
                        xmlSalida.append("<EMPRESA>");
                            xmlSalida.append("<VALOR>");
                                xmlSalida.append(vis.getEmpresa());
                            xmlSalida.append("</VALOR>");
                            xmlSalida.append("<ERROR>");
                                xmlSalida.append(vis.getErrorCampo(FilaVisProspectoresVO.POS_CAMPO_EMPRESA));
                            xmlSalida.append("</ERROR>");
                        xmlSalida.append("</EMPRESA>");
                        xmlSalida.append("<FECHA_VISITA>");
                            xmlSalida.append("<VALOR>");
                                xmlSalida.append(vis.getFecVisita());
                            xmlSalida.append("</VALOR>");
                            xmlSalida.append("<ERROR>");
                                xmlSalida.append(vis.getErrorCampo(FilaVisProspectoresVO.POS_CAMPO_FECVISITA));
                            xmlSalida.append("</ERROR>");                           
                        xmlSalida.append("</FECHA_VISITA>");
                        xmlSalida.append("<SECTOR>");
                            xmlSalida.append("<VALOR>");    
                                xmlSalida.append(vis.getDescSector());
                            xmlSalida.append("</VALOR>");
                            xmlSalida.append("<ERROR>");
                                xmlSalida.append(vis.getErrorCampo(FilaVisProspectoresVO.POS_CAMPO_SECTORACT));
                            xmlSalida.append("</ERROR>"); 
                        xmlSalida.append("</SECTOR>");
                        xmlSalida.append("<DIRECCION>");
                            xmlSalida.append("<VALOR>");   
                                xmlSalida.append(vis.getDireccion());
                            xmlSalida.append("</VALOR>");
                            xmlSalida.append("<ERROR>");
                                xmlSalida.append(vis.getErrorCampo(FilaVisProspectoresVO.POS_CAMPO_DIRECCION));
                            xmlSalida.append("</ERROR>");
                        xmlSalida.append("</DIRECCION>");
                        xmlSalida.append("<CPOSTAL>");
                            xmlSalida.append("<VALOR>");   
                                xmlSalida.append(vis.getCpostal());
                            xmlSalida.append("</VALOR>");
                            xmlSalida.append("<ERROR>");
                                xmlSalida.append(vis.getErrorCampo(FilaVisProspectoresVO.POS_CAMPO_CPOSTAL));
                            xmlSalida.append("</ERROR>");
                        xmlSalida.append("</CPOSTAL>");
                        xmlSalida.append("<LOCALIDAD>");
                            xmlSalida.append("<VALOR>");   
                                xmlSalida.append(vis.getLocalidad());
                            xmlSalida.append("</VALOR>");
                            xmlSalida.append("<ERROR>");
                                xmlSalida.append(vis.getErrorCampo(FilaVisProspectoresVO.POS_CAMPO_LOCALIDAD));
                            xmlSalida.append("</ERROR>");
                        xmlSalida.append("</LOCALIDAD>");
                        xmlSalida.append("<PROVINCIA>");
                            xmlSalida.append("<VALOR>");   
                                xmlSalida.append(vis.getDescProvincia());
                            xmlSalida.append("</VALOR>");
                            xmlSalida.append("<ERROR>");
                                xmlSalida.append(vis.getErrorCampo(FilaVisProspectoresVO.POS_CAMPO_PROVINCIA));
                            xmlSalida.append("</ERROR>");
                        xmlSalida.append("</PROVINCIA>");
                        xmlSalida.append("<PERS_CONTACTO>");
                            xmlSalida.append("<VALOR>");   
                                xmlSalida.append(vis.getPersContacto());
                            xmlSalida.append("</VALOR>");
                            xmlSalida.append("<ERROR>");
                                xmlSalida.append(vis.getErrorCampo(FilaVisProspectoresVO.POS_CAMPO_PCONTACTO));
                            xmlSalida.append("</ERROR>");
                        xmlSalida.append("</PERS_CONTACTO>");                        
                        xmlSalida.append("<PUESTO>");
                            xmlSalida.append("<VALOR>");
                                xmlSalida.append(vis.getPuesto());
                            xmlSalida.append("</VALOR>");
                            xmlSalida.append("<ERROR>");
                                xmlSalida.append(vis.getErrorCampo(FilaVisProspectoresVO.POS_CAMPO_CARGO));
                            xmlSalida.append("</ERROR>");    
                        xmlSalida.append("</PUESTO>");
                        xmlSalida.append("<EMAIL>");
                            xmlSalida.append("<VALOR>");   
                                xmlSalida.append(vis.getMail());
                            xmlSalida.append("</VALOR>");
                            xmlSalida.append("<ERROR>");
                                xmlSalida.append(vis.getErrorCampo(FilaVisProspectoresVO.POS_CAMPO_EMAIL));
                            xmlSalida.append("</ERROR>");    
                        xmlSalida.append("</EMAIL>");
                        xmlSalida.append("<TELEFONO>");
                            xmlSalida.append("<VALOR>");    
                                xmlSalida.append(vis.getTelefono());
                            xmlSalida.append("</VALOR>");
                            xmlSalida.append("<ERROR>");
                                xmlSalida.append(vis.getErrorCampo(FilaVisProspectoresVO.POS_CAMPO_TELEFONO));
                            xmlSalida.append("</ERROR>");    
                        xmlSalida.append("</TELEFONO>");
                        xmlSalida.append("<NIF_PROSPECTOR>");
                            xmlSalida.append("<VALOR>");    
                                xmlSalida.append(vis.getNifProspector());
                            xmlSalida.append("</VALOR>");
                            xmlSalida.append("<ERROR>");
                                xmlSalida.append(vis.getErrorCampo(FilaVisProspectoresVO.POS_CAMPO_NIFPROSPECTOR));
                            xmlSalida.append("</ERROR>");    
                        xmlSalida.append("</NIF_PROSPECTOR>");
                        xmlSalida.append("<NUMTRAB>");
                            xmlSalida.append("<VALOR>");    
                                xmlSalida.append(vis.getNumTrab());
                            xmlSalida.append("</VALOR>");
                            xmlSalida.append("<ERROR>");
                                xmlSalida.append(vis.getErrorCampo(FilaVisProspectoresVO.POS_CAMPO_NUMTRAB));
                            xmlSalida.append("</ERROR>");
                        xmlSalida.append("</NUMTRAB>");                        
                        xmlSalida.append("<NUMTRABDISC>");
                            xmlSalida.append("<VALOR>");
                                xmlSalida.append(vis.getNumTrabDisc());
                            xmlSalida.append("</VALOR>");
                            xmlSalida.append("<ERROR>");
                                xmlSalida.append(vis.getErrorCampo(FilaVisProspectoresVO.POS_CAMPO_NUMTRABDISC));
                            xmlSalida.append("</ERROR>");
                        xmlSalida.append("</NUMTRABDISC>");
                        xmlSalida.append("<CUMPLELISMI>");
                            xmlSalida.append("<VALOR>");
                                xmlSalida.append(vis.getDescCumpleLismi());
                            xmlSalida.append("</VALOR>");
                            xmlSalida.append("<ERROR>");
                                xmlSalida.append(vis.getErrorCampo(FilaVisProspectoresVO.POS_CAMPO_CUMPLELISMI));
                            xmlSalida.append("</ERROR>");
                        xmlSalida.append("</CUMPLELISMI>");
                        xmlSalida.append("<RESULTADO>");
                            xmlSalida.append("<VALOR>");
                                xmlSalida.append(vis.getDescResultadoFinal());
                            xmlSalida.append("</VALOR>");
                            xmlSalida.append("<ERROR>");
                                xmlSalida.append(vis.getErrorCampo(FilaVisProspectoresVO.POS_CAMPO_RESULTADO));
                            xmlSalida.append("</ERROR>");
                        xmlSalida.append("</RESULTADO>");
                        xmlSalida.append("<OBSERVACIONES>");
                            xmlSalida.append("<VALOR>");
                                xmlSalida.append(vis.getObservaciones());
                            xmlSalida.append("</VALOR>");
                            xmlSalida.append("<ERROR>");
                                xmlSalida.append(vis.getErrorCampo(FilaSegPreparadoresVO.POS_CAMPO_OBSERVACIONES));
                            xmlSalida.append("</ERROR>");                            
                        xmlSalida.append("</OBSERVACIONES>");
                        xmlSalida.append("<ERRORES>");
                        if(vis.getErrores() != null)
                        {
                            for(int contE = 0; contE < vis.getErrores().size(); contE++)
                            {
                                xmlSalida.append("<ERROR>");
                                    xmlSalida.append(vis.getErrores().get(contE));
                                xmlSalida.append("</ERROR>");
                            }
                        }
                        xmlSalida.append("</ERRORES>");
                    xmlSalida.append("</FILA>");
                    i++;
                }
            xmlSalida.append("</RESPUESTA>");
            try{
                response.setContentType("text/xml");
                response.setCharacterEncoding("UTF-8");
                PrintWriter out = response.getWriter();
                out.print(xmlSalida.toString());
                out.flush();
                out.close();
            }catch(Exception e){
                log.error(" Error : " + e.getMessage(), e);
            }//try-catch
    }
   
    private List<String> hayNifsRepetidos(HSSFSheet sheet, int colNumber)
    {
        //TODO: implementar --> Busca nifs repetidos en la columna indicada. En caso de que los haya, devuelve true y se mostrará un mensaje de aviso.
        
        int filaIni = sheet.getFirstRowNum();
        int filaFin = sheet.getLastRowNum();

        HSSFRow row = null;
        List<String> nifs = new ArrayList<String>();
        List<String> nifsRepetidos = new ArrayList<String>();
        HSSFCell cell = null;
        String value = null;
        try
        {
            for(int i = filaIni+1; i< filaFin; i++)
            {
                row = sheet.getRow(i);
                cell = row.getCell(colNumber);
                if(cell.getCellType() == HSSFCell.CELL_TYPE_STRING)
                {
                    try
                    {
                        value = cell.getStringCellValue();
                        if(nifs.contains(value))
                        {
                            nifsRepetidos.add(value);
                        }
                        else
                        {
                            nifs.add(value);
                        }
                    }
                    catch(Exception ex)
                    {

                    }
                }
            }
        }
        catch(Exception ex)
        {
            
        }
        return nifsRepetidos;
    }
   
    private List<String> haySustitutosDeSustitutos(HSSFSheet sheet, int colNumber1, int colNumber2)
    {
        //TODO: implementar --> Busca nifs repetidos en la columna indicada. En caso de que los haya, devuelve true y se mostrará un mensaje de aviso.
        
        int filaIni = sheet.getFirstRowNum();
        int filaFin = sheet.getLastRowNum();

        HSSFRow row = null;
        List<String> retList = new ArrayList<String>();
        List<String> sustitutos = new ArrayList<String>();
        List<String> sustituidos = new ArrayList<String>();
        HSSFCell cell = null;
        HSSFCell cellSustituido = null;
        String nif = null;
        String nifSustituido = null;
        try
        {
            for(int i = filaIni+1; i< filaFin; i++)
            {
                row = sheet.getRow(i);
                cell = row.getCell(colNumber1);
                cellSustituido = row.getCell(colNumber2);
                //if(cell.getCellType() == HSSFCell.CELL_TYPE_STRING && cellSustituido.getCellType() == HSSFCell.CELL_TYPE_STRING)
                //{
                    try
                    {
                        nif = cell.getStringCellValue();
                        nifSustituido = cellSustituido.getStringCellValue();
                        
                        if(nifSustituido != null && !nifSustituido.equals(""))
                        {
                            if(sustitutos.contains(nifSustituido))
                            {
                                retList.add(nifSustituido);
                            }
                            else
                            {
                                sustitutos.add(nif);
                                sustituidos.add(nifSustituido);
                            }
                        }
                    }
                    catch(Exception ex)
                    {

                    }
                //}
            }
        }
        catch(Exception ex)
        {
            
        }
        return retList;
    }
   
    public void copiarPreparadores(int codOrganizacion,  int codTramite, int ocurrenciaTramite,String numExpediente,HttpServletRequest request,HttpServletResponse response)
    {
        List<FilaPreparadorJustificacionVO> preparadores = null;
        String codigoOperacion = "0";        
        int codIdioma = 1;  
       
        try
        {
            UsuarioValueObject usuario = (UsuarioValueObject) request.getSession().getAttribute("usuario");
            if(usuario != null)
            {
                codIdioma = usuario.getIdioma();
            }
        }
        catch(Exception ex)
        {
        }
        try
        {
            //EcaSolicitudVO sol = MeLanbide44Manager.getInstance().getDatosSolicitud(numExpediente, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
            MeLanbide44Manager manager = MeLanbide44Manager.getInstance();
            if(manager.hayPreparadoresParaCopiar(numExpediente, this.getAdaptSQLBD(String.valueOf(codOrganizacion))))
            {
                int result = manager.copiarPreparadores(numExpediente, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                if(result <= 0)
                {
                    codigoOperacion = "1";
                }
                else
                {
                    codigoOperacion = "0";
                }
            }
            else
            {
                codigoOperacion = "-1";
            }

        }
        catch(Exception ex)
        {
            codigoOperacion = "2";
        }
        if(codigoOperacion.equalsIgnoreCase("0"))
        {
            try{
                EcaSolicitudVO sol = MeLanbide44Manager.getInstance().getDatosSolicitud(numExpediente, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                if(sol != null)
                        preparadores = MeLanbide44Manager.getInstance().getListaPreparadoresJustificacion(sol,  this.getAdaptSQLBD(String.valueOf(codOrganizacion)), codIdioma);
                else    preparadores = new ArrayList<FilaPreparadorJustificacionVO>();
                this.escribirListaPreparadoresJusRequest(codigoOperacion,preparadores, null, codIdioma, response);
            }catch(Exception e){
                log.error(" Error : " + e.getMessage(), e);                
            }            
        }
        else
        {
            StringBuffer xmlSalida = new StringBuffer();
            xmlSalida.append("<RESPUESTA>");
                xmlSalida.append("<CODIGO_OPERACION>");
                    xmlSalida.append(codigoOperacion);
                xmlSalida.append("</CODIGO_OPERACION>");
            xmlSalida.append("</RESPUESTA>");
            try{
                response.setContentType("text/xml");
                response.setCharacterEncoding("UTF-8");
                PrintWriter out = response.getWriter();
                out.print(xmlSalida.toString());
                out.flush();
                out.close();
            }catch(Exception e){
                log.error(" Error : " + e.getMessage(), e);
            }//try-catch
        }
    }
    
     public void copiarProspectores(int codOrganizacion,  int codTramite, int ocurrenciaTramite,String numExpediente,HttpServletRequest request,HttpServletResponse response)
    {
        List<FilaProspectorJustificacionVO> prospectores = null;
        String codigoOperacion = "0";        
        int codIdioma = 1;  
       
        try
        {
            UsuarioValueObject usuario = (UsuarioValueObject) request.getSession().getAttribute("usuario");
            if(usuario != null)
            {
                codIdioma = usuario.getIdioma();
            }
        }
        catch(Exception ex)
        {
        }
        try
        {
            //EcaSolicitudVO sol = MeLanbide44Manager.getInstance().getDatosSolicitud(numExpediente, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
            MeLanbide44Manager manager = MeLanbide44Manager.getInstance();
            if(manager.hayProspectoresParaCopiar(numExpediente, this.getAdaptSQLBD(String.valueOf(codOrganizacion))))
            {
                int result = MeLanbide44Manager.getInstance().copiarProspectores(numExpediente, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                if(result <= 0)
                {
                    codigoOperacion = "1";
                }
                else
                {
                    codigoOperacion = "0";
                }
            }
            else
            {
                codigoOperacion = "-1";
            }


        }
        catch(Exception ex)
        {
            codigoOperacion = "2";
        }
        if(codigoOperacion.equalsIgnoreCase("0"))
        {
            try{
                EcaSolicitudVO sol = MeLanbide44Manager.getInstance().getDatosSolicitud(numExpediente, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                if(sol != null)
                        prospectores = MeLanbide44Manager.getInstance().getListaProspectoresJustificacion(sol,  this.getAdaptSQLBD(String.valueOf(codOrganizacion)), codIdioma);
                else    prospectores = new ArrayList<FilaProspectorJustificacionVO>();
                this.escribirListaProspectoresJusRequest(codigoOperacion,prospectores, response);
            }catch(Exception e){
                log.error(" Error : " + e.getMessage(), e);                
            }            
        }
        else
        {
            StringBuffer xmlSalida = new StringBuffer();
            xmlSalida.append("<RESPUESTA>");
                xmlSalida.append("<CODIGO_OPERACION>");
                    xmlSalida.append(codigoOperacion);
                xmlSalida.append("</CODIGO_OPERACION>");
            xmlSalida.append("</RESPUESTA>");
            try{
                response.setContentType("text/xml");
                response.setCharacterEncoding("UTF-8");
                PrintWriter out = response.getWriter();
                out.print(xmlSalida.toString());
                out.flush();
                out.close();
            }catch(Exception e){
                log.error(" Error : " + e.getMessage(), e);
            }//try-catch
        }
    }
    
     
    public String compararDatosPrepSol(int codOrganizacion,  int codTramite, int ocurrenciaTramite,String numExpediente,HttpServletRequest request,HttpServletResponse response)
    {
         String url="";
        try
        {
            int idiomaUsuario = 1;
            UsuarioValueObject usuario = (UsuarioValueObject) request.getSession().getAttribute("usuario");
            try
            {
                    if (usuario != null) 
                    {                    
                        idiomaUsuario = usuario.getIdioma();
                    }
            }
            catch(Exception ex)
            {
            }
            
             AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));            
             EcaSolicitudVO sol = MeLanbide44Manager.getInstance().getDatosSolicitud(numExpediente, adapt);
            String idprep = (String)request.getParameter("idPrep");      
            url= "/jsp/extension/melanbide44/solicitud/comparacionPreparadorSolicitud.jsp";
            if (idprep!=null){                
                EcaSolPreparadoresVO prep = new EcaSolPreparadoresVO();
                prep.setSolPreparadoresCod(Integer.parseInt(idprep));
                
                prep = MeLanbide44Manager.getInstance().getPreparadorSolicitudPorId(prep, adapt);
                if(prep != null) {
                    request.setAttribute("preparador", prep);                    
                 }
            }
            
        }
        catch(Exception ex)
        {
            log.error(ex.getMessage());
        }
        return url;
    }
 
    public String compararDatosProsSol(int codOrganizacion,  int codTramite, int ocurrenciaTramite,String numExpediente,HttpServletRequest request,HttpServletResponse response)
    {
         String url="";
        try
        {
            int idiomaUsuario = 1;
            UsuarioValueObject usuario = (UsuarioValueObject) request.getSession().getAttribute("usuario");
            try
            {
                    if (usuario != null) 
                    {                    
                        idiomaUsuario = usuario.getIdioma();
                    }
            }
            catch(Exception ex)
            {
            }
            
             AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));            
             EcaSolicitudVO sol = MeLanbide44Manager.getInstance().getDatosSolicitud(numExpediente, adapt);
            String idpros = (String)request.getParameter("idPros");              
            if (idpros!=null){
                url= "/jsp/extension/melanbide44/solicitud/comparacionProspectorSolicitud.jsp";
                EcaSolProspectoresVO pros = new EcaSolProspectoresVO();
                pros.setSolProspectoresCod(Integer.parseInt(idpros));
                
                pros = MeLanbide44Manager.getInstance().getProspectorSolicitudPorId(pros, adapt);
                if(pros != null) {
                    request.setAttribute("prospector", pros);                    
                 }
            }else {
                url= "/jsp/extension/melanbide44/solicitud/comparacionProspectorSolicitud.jsp";
                
            }
            
        }
        catch(Exception ex)
        {
            log.error(ex.getMessage());
        }
        return url;
    }
 
     
    /**
     * Operación que recupera los datos de conexión a la BBDD
     * @param codOrganizacion
     * @return AdaptadorSQLBD
     */
    private AdaptadorSQLBD getAdaptSQLBD(String codOrganizacion){
        if(log.isDebugEnabled()) log.debug("getConnection ( codOrganizacion = " + codOrganizacion + " ) : BEGIN");
        ResourceBundle config = ResourceBundle.getBundle("techserver");
        String gestor = config.getString("CON.gestor");
        String jndiGenerico = config.getString("CON.jndi");
        Connection conGenerico = null;
        Statement st = null;
        ResultSet rs = null;
        String[] salida = null;
        Connection con = null;
        
        if(log.isDebugEnabled()){
            log.debug("getJndi =========> ");
            log.debug("parametro codOrganizacion: " + codOrganizacion);
            log.debug("gestor: " + gestor);
            log.debug("jndi: " + jndiGenerico);
        }//if(log.isDebugEnabled())

        DataSource ds = null;
        AdaptadorSQLBD adapt = null;
        synchronized (this) {
            try{
                PortableContext pc = PortableContext.getInstance();
                if(log.isDebugEnabled())log.debug("He cogido el jndi: " + jndiGenerico);
                ds = (DataSource)pc.lookup(jndiGenerico, DataSource.class);
                // Conexión al esquema genérico
                conGenerico = ds.getConnection();

                String sql = "SELECT EEA_BDE FROM A_EEA WHERE EEA_APL=" + ConstantesDatos.APP_GESTION_EXPEDIENTES + " AND AAE_ORG=" + codOrganizacion;
                st = conGenerico.createStatement();
                rs = st.executeQuery(sql);
                String jndi = null;
                while(rs.next()){
                    jndi = rs.getString("EEA_BDE");
                }//while(rs.next())

                st.close();
                rs.close();
                conGenerico.close();

                if(jndi!=null && gestor!=null && !"".equals(jndi) && !"".equals(gestor)){
                    salida = new String[7];
                    salida[0] = gestor;
                    salida[1] = "";
                    salida[2] = "";
                    salida[3] = "";
                    salida[4] = "";
                    salida[5] = "";
                    salida[6] = jndi;
                    adapt = new AdaptadorSQLBD(salida);
                }//if(jndi!=null && gestor!=null && !"".equals(jndi) && !"".equals(gestor))
            }catch(TechnicalException te){
                log.error("*** AdaptadorSQLBD: " + te.toString());
            }catch(SQLException e){
                log.error(" Error : " + e.getMessage(), e);
            }finally{
                try{
                    if(st!=null) st.close();
                    if(rs!=null) rs.close();
                    if(conGenerico!=null && !conGenerico.isClosed())
                    conGenerico.close();
                }catch(SQLException e){
                    log.error(" Error : " + e.getMessage(), e);
                }//try-catch
            }// finally
            if(log.isDebugEnabled()) log.debug("getConnection() : END");
         }// synchronized
        return adapt;
     }//getConnection
    
    private void restarDatosPreparadoresConErrores(List<FilaPreparadorSolicitudVO> listaPreparadoresSolicitud, DatosAnexosVO datosAnexos, AdaptadorSQLBD adapt)
    {
        try
        {
            //Este for es para ir recorriendo todos los preparadores y mirar cuales tienen errores
            //En la pestaña de solicitud, columna de anexos, no deben contarse las inserciones, visitas, etc de los preparadores con errores
            //La parte de actuación subvencionable se hace en la propia jsp, pero esto se hace aqui porque es más rápido
            BigDecimal bd1 = null;
            BigDecimal bd2 = null;
            BigDecimal total = null;
            if(listaPreparadoresSolicitud != null)
            {
                for(FilaPreparadorSolicitudVO f : listaPreparadoresSolicitud)
                {
                    if(f.getErrores() != null && !f.getErrores().isEmpty())
                    {
                        EcaSolPreparadoresVO prep = new EcaSolPreparadoresVO();
                        prep.setSolPreparadoresCod(Integer.parseInt(f.getId()));
                        prep = MeLanbide44Manager.getInstance().getPreparadorSolicitudPorId(prep, adapt);
                        if(prep != null)
                        {
                            //Inserciones col. 1
                            if(prep.getInsC1H() != null || datosAnexos.getC1H() != null)
                            {
                                bd1 = datosAnexos.getC1H() != null ? datosAnexos.getC1H() : new BigDecimal("0.00");
                                bd2 = prep.getInsC1H() != null ? prep.getInsC1H() : new BigDecimal("0.00");
                                total = bd1.subtract(bd2);
                                datosAnexos.setC1H(total);
                            }
                            if(prep.getInsC1M() != null || datosAnexos.getC1M() != null)
                            {
                                bd1 = datosAnexos.getC1M() != null ? datosAnexos.getC1M() : new BigDecimal("0.00");
                                bd2 = prep.getInsC1M() != null ? prep.getInsC1M() : new BigDecimal("0.00");
                                total = bd1.subtract(bd2);
                                datosAnexos.setC1M(total);
                            }
                            if(prep.getInsC1() != null || datosAnexos.getC1T() != null)
                            {
                                bd1 = datosAnexos.getC1T() != null ? datosAnexos.getC1T() : new BigDecimal("0.00");
                                bd2 = prep.getInsC1() != null ? prep.getInsC1() : new BigDecimal("0.00");
                                total = bd1.subtract(bd2);
                                datosAnexos.setC1T(total);
                            }

                            //Inserciones col. 2
                            if(prep.getInsC2H() != null || datosAnexos.getC2H() != null)
                            {
                                bd1 = datosAnexos.getC2H() != null ? datosAnexos.getC2H() : new BigDecimal("0.00");
                                bd2 = prep.getInsC2H() != null ? prep.getInsC2H() : new BigDecimal("0.00");
                                total = bd1.subtract(bd2);
                                datosAnexos.setC2H(total);
                            }
                            if(prep.getInsC2M() != null || datosAnexos.getC2M() != null)
                            {
                                bd1 = datosAnexos.getC2M() != null ? datosAnexos.getC2M() : new BigDecimal("0.00");
                                bd2 = prep.getInsC2M() != null ? prep.getInsC2M() : new BigDecimal("0.00");
                                total = bd1.subtract(bd2);
                                datosAnexos.setC2M(total);
                            }
                            if(prep.getInsC2() != null || datosAnexos.getC2T() != null)
                            {
                                bd1 = datosAnexos.getC2T() != null ? datosAnexos.getC2T() : new BigDecimal("0.00");
                                bd2 = prep.getInsC2() != null ? prep.getInsC2() : new BigDecimal("0.00");
                                total = bd1.subtract(bd2);
                                datosAnexos.setC2T(total);
                            }

                            //Inserciones col. 3
                            if(prep.getInsC3H() != null || datosAnexos.getC3H() != null)
                            {
                                bd1 = datosAnexos.getC3H() != null ? datosAnexos.getC3H() : new BigDecimal("0.00");
                                bd2 = prep.getInsC3H() != null ? prep.getInsC3H() : new BigDecimal("0.00");
                                total = bd1.subtract(bd2);
                                datosAnexos.setC3H(total);
                            }
                            if(prep.getInsC3M() != null || datosAnexos.getC3M() != null)
                            {
                                bd1 = datosAnexos.getC3M() != null ? datosAnexos.getC3M() : new BigDecimal("0.00");
                                bd2 = prep.getInsC3M() != null ? prep.getInsC3M() : new BigDecimal("0.00");
                                total = bd1.subtract(bd2);
                                datosAnexos.setC3M(total);
                            }
                            if(prep.getInsC3() != null || datosAnexos.getC3T() != null)
                            {
                                bd1 = datosAnexos.getC3T() != null ? datosAnexos.getC3T() : new BigDecimal("0.00");
                                bd2 = prep.getInsC3() != null ? prep.getInsC3() : new BigDecimal("0.00");
                                total = bd1.subtract(bd2);
                                datosAnexos.setC3T(total);
                            }

                            //Inserciones col. 4
                            if(prep.getInsC4H() != null || datosAnexos.getC4H() != null)
                            {
                                bd1 = datosAnexos.getC4H() != null ? datosAnexos.getC4H() : new BigDecimal("0.00");
                                bd2 = prep.getInsC4H() != null ? prep.getInsC4H() : new BigDecimal("0.00");
                                total = bd1.subtract(bd2);
                                datosAnexos.setC4H(total);
                            }
                            if(prep.getInsC4M() != null || datosAnexos.getC4M() != null)
                            {
                                bd1 = datosAnexos.getC4M() != null ? datosAnexos.getC4M() : new BigDecimal("0.00");
                                bd2 = prep.getInsC4M() != null ? prep.getInsC4M() : new BigDecimal("0.00");
                                total = bd1.subtract(bd2);
                                datosAnexos.setC4M(total);
                            }
                            if(prep.getInsC4() != null || datosAnexos.getC4T() != null)
                            {
                                bd1 = datosAnexos.getC4T() != null ? datosAnexos.getC4T() : new BigDecimal("0.00");
                                bd2 = prep.getInsC4() != null ? prep.getInsC4() : new BigDecimal("0.00");
                                total = bd1.subtract(bd2);
                                datosAnexos.setC4T(total);
                            }
                            
                            
                            //Inserciones col. 5
                            if(prep.getInsC5H() != null || datosAnexos.getC5H() != null)
                            {
                                bd1 = datosAnexos.getC5H() != null ? datosAnexos.getC5H() : new BigDecimal("0.00");
                                bd2 = prep.getInsC5H() != null ? prep.getInsC5H() : new BigDecimal("0.00");
                                total = bd1.subtract(bd2);
                                datosAnexos.setC5H(total);
                            }
                            if(prep.getInsC5M() != null || datosAnexos.getC5M() != null)
                            {
                                bd1 = datosAnexos.getC5M() != null ? datosAnexos.getC5M() : new BigDecimal("0.00");
                                bd2 = prep.getInsC4M() != null ? prep.getInsC5M() : new BigDecimal("0.00");
                                total = bd1.subtract(bd2);
                                datosAnexos.setC5M(total);
                            }
                            if(prep.getInsC5() != null || datosAnexos.getC5T() != null)
                            {
                                bd1 = datosAnexos.getC5T() != null ? datosAnexos.getC5T() : new BigDecimal("0.00");
                                bd2 = prep.getInsC5() != null ? prep.getInsC5() : new BigDecimal("0.00");
                                total = bd1.subtract(bd2);
                                datosAnexos.setC5T(total);
                            }
                            
                            //Inserciones col. 6
                            if(prep.getInsC6H() != null || datosAnexos.getC6H() != null)
                            {
                                bd1 = datosAnexos.getC6H() != null ? datosAnexos.getC6H() : new BigDecimal("0.00");
                                bd2 = prep.getInsC6H() != null ? prep.getInsC6H() : new BigDecimal("0.00");
                                total = bd1.subtract(bd2);
                                datosAnexos.setC6H(total);
                            }
                            if(prep.getInsC6M() != null || datosAnexos.getC6M() != null)
                            {
                                bd1 = datosAnexos.getC6M() != null ? datosAnexos.getC6M() : new BigDecimal("0.00");
                                bd2 = prep.getInsC6M() != null ? prep.getInsC6M() : new BigDecimal("0.00");
                                total = bd1.subtract(bd2);
                                datosAnexos.setC6M(total);
                            }
                            if(prep.getInsC6() != null || datosAnexos.getC6T() != null)
                            {
                                bd1 = datosAnexos.getC6T() != null ? datosAnexos.getC6T() : new BigDecimal("0.00");
                                bd2 = prep.getInsC6() != null ? prep.getInsC6() : new BigDecimal("0.00");
                                total = bd1.subtract(bd2);
                                datosAnexos.setC6T(total);
                            }

                            //Seguimientos
                            if(prep.getSegAnt() != null || datosAnexos.getSeguimientosAnt() != null)
                            {
                                bd1 = datosAnexos.getSeguimientosAnt() != null ? datosAnexos.getSeguimientosAnt() : new BigDecimal("0.00");
                                bd2 = new BigDecimal(prep.getSegAnt() != null ? prep.getSegAnt().toString() : "0.00");
                                total = bd1.subtract(bd2);
                                datosAnexos.setSeguimientosAnt(total);
                            }
                        }
                    }
                }
            }
        }
        catch(Exception ex)
        {
            
        }
    }
    
    public String cargarDetalleProspectoresResumen(int codOrganizacion,  int codTramite, int ocurrenciaTramite,String numExpediente,HttpServletRequest request,HttpServletResponse response)
    {
        int codIdioma = 1;    
        try
        {
            UsuarioValueObject usuario = (UsuarioValueObject) request.getSession().getAttribute("usuario");
            if(usuario != null)
            {
                codIdioma = usuario.getIdioma();
            }
        }
        catch(Exception ex)
        {

        }
        try
        {
            List<FilaEcaResProspectoresVO> listaDetalleProspectoresResumen = MeLanbide44Manager.getInstance().getListaDetalleProspectoresResumen(numExpediente, codIdioma, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
            if(listaDetalleProspectoresResumen != null)
            {
                request.setAttribute("listaDetalleProspectoresResumen", listaDetalleProspectoresResumen);
            }
        }
        catch(Exception ex)
        {
            log.error(" Error : " + ex.getMessage(), ex);
        }
        
        return "/jsp/extension/melanbide44/resumen/detalleProspectoresResumen.jsp";
    }
    
    public String cargarDetallePreparadoresResumen(int codOrganizacion,  int codTramite, int ocurrenciaTramite,String numExpediente,HttpServletRequest request,HttpServletResponse response)
    {
        try
        {
            List<FilaEcaResPreparadoresVO> listaDetallePreparadoresResumen = MeLanbide44Manager.getInstance().getListaDetallePreparadoresResumen(numExpediente, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
            if(listaDetallePreparadoresResumen != null)
            {
                request.setAttribute("listaDetallePreparadoresResumen", listaDetallePreparadoresResumen);
            }
        }
        catch(Exception ex)
        {
            log.error(" Error : " + ex.getMessage(), ex);
        }
        
        return "/jsp/extension/melanbide44/resumen/detallePreparadoresResumen.jsp";
    }
    
    public String cargarDetalleInsercionesPrep(int codOrganizacion,  int codTramite, int ocurrenciaTramite,String numExpediente,HttpServletRequest request,HttpServletResponse response)
    {
        try
        {
            String idPrep = (String)request.getParameter("prepId");
            String ejercicio = String.valueOf(MeLanbide44Utils.getEjercicioDeExpediente(numExpediente));
            EcaJusPreparadoresVO prep = new EcaJusPreparadoresVO();
            prep.setJusPreparadoresCod(Integer.parseInt(idPrep));
            prep = MeLanbide44Manager.getInstance().getPreparadorJustificacionPorId(prep, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
            EcaResDetalleInsercionesVO det = null;
            if(prep != null)
            {
                request.setAttribute("preparador", prep);
                det = MeLanbide44Manager.getInstance().cargarDetalleInsercionesPrep(ejercicio, prep, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                if(det != null)
                {
                    request.setAttribute("detalleInserciones", det);
                }
            }
        }
        catch(Exception ex)
        {
            log.error(" Error : " + ex.getMessage(), ex);
        }
        
        return "/jsp/extension/melanbide44/resumen/detalleInsercionesPrepResumen.jsp";   
    }
    
    public void actualizarDatosResumen(int codOrganizacion,  int codTramite, int ocurrenciaTramite,String numExpediente,HttpServletRequest request,HttpServletResponse response)
    {
        String mensaje = "";
        try
        {
            numExpediente = (String)request.getParameter("numero");
            mensaje = MeLanbide44Manager.getInstance().actualizarDatosResumen(numExpediente, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
            if(mensaje != null && !mensaje.equals(""))
            {
                int codIdioma = 1;    
                try
                {
                    UsuarioValueObject usuario = (UsuarioValueObject) request.getSession().getAttribute("usuario");
                    if(usuario != null)
                    {
                        codIdioma = usuario.getIdioma();
                    }
                    
                    //mensaje = MeLanbide44I18n.getInstance().getMensaje(codIdioma, mensaje);
                }
                catch(Exception ex)
                {

                }
            }
        }
        catch(Exception ex)
        {
            log.error(" Error : " + ex.getMessage(), ex);
        } 
        StringBuffer xmlSalida = new StringBuffer();
        xmlSalida.append("<RESPUESTA>");
            xmlSalida.append("<MENSAJE>");
                //xmlSalida.append(mensaje != null ? mensaje : "");
                xmlSalida.append(mensaje != null && !mensaje.equals("") ? mensaje : ConstantesMeLanbide44.OK);
            xmlSalida.append("</MENSAJE>");
        xmlSalida.append("</RESPUESTA>");  
        try
        {
            response.setContentType("text/xml");
            response.setCharacterEncoding("UTF-8");
            PrintWriter out = response.getWriter();
            out.print(xmlSalida.toString());
            out.flush();
            out.close();
        }
        catch(Exception e)
        {
            log.error(" Error : " + e.getMessage(), e);
        }//try-catch  
    }
    
    public void guardarDatosResumen(int codOrganizacion,  int codTramite, int ocurrenciaTramite,String numExpediente,HttpServletRequest request,HttpServletResponse response)
    {
        
        String codigoOperacion = "0";
        try
        {
            AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
            try
            {
                String subvPriv = (String)request.getParameter("subvPriv");
                String subvPub = (String)request.getParameter("subvPub");
                String totSubv = (String)request.getParameter("totSubv");
                String gastos = (String)request.getParameter("gastos");
                
                EcaSolicitudVO sol = MeLanbide44Manager.getInstance().getDatosSolicitud(numExpediente, adapt);
                if(sol != null)
                {
                    EcaResumenVO res = MeLanbide44Manager.getInstance().getResumenSolicitud(sol, adapt);
                    if(res == null)
                    {
                        res = new EcaResumenVO();
                        res.setSolicitud(sol.getSolicitudCod());
                    }
                    if(subvPriv != null && !subvPriv.equals(""))
                    {
                        res.setEcaResSubPriv(new BigDecimal(subvPriv.replaceAll(",", "\\.")));
                    }
                    else
                    {
                        res.setEcaResSubPriv(null);
                    }
                    if(subvPub != null && !subvPub.equals(""))
                    {
                        res.setEcaResSubPub(new BigDecimal(subvPub.replaceAll(",", "\\.")));
                    }
                    else
                    {
                        res.setEcaResSubPub(null);
                    }
                    if(totSubv != null && !totSubv.equals(""))
                    {
                        res.setEcaResTotSubv(new BigDecimal(totSubv.replaceAll(",", "\\.")));
                    }
                    else
                    {
                        res.setEcaResTotSubv(null);
                    }
                    if(gastos != null && !gastos.equals(""))
                    {
                        res.setGastosGenerales_pag(new BigDecimal(gastos.replaceAll(",", "\\.")));
                    }
                    else
                    {
                        res.setGastosGenerales_pag(null);
                    }
                    res = MeLanbide44Manager.getInstance().guardarResumenSolicitud(res, adapt);
                    
                    if(res != null)
                    {
                        codigoOperacion = "0";
                    }
                    else
                    {
                        codigoOperacion = "1";
                    }
                }
                else
                {
                    codigoOperacion = "3";
                }
            }
            catch(Exception ex)
            {
                codigoOperacion = "2";
                java.util.logging.Logger.getLogger(MELANBIDE44.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        catch(Exception ex)
        {
            codigoOperacion = "2";
        }
        
        StringBuffer xmlSalida = new StringBuffer();
        xmlSalida.append("<RESPUESTA>");
            xmlSalida.append("<CODIGO_OPERACION>");
                xmlSalida.append(codigoOperacion);
            xmlSalida.append("</CODIGO_OPERACION>");
        xmlSalida.append("</RESPUESTA>");
        try{
            response.setContentType("text/xml");
            response.setCharacterEncoding("UTF-8");
            PrintWriter out = response.getWriter();
            out.print(xmlSalida.toString());
            out.flush();
            out.close();
        }catch(Exception e){
            log.error(" Error : " + e.getMessage(), e);
        }//try-catch
    }
    
    public String cargarSustituirPreparadorSolicitud(int codOrganizacion,  int codTramite, int ocurrenciaTramite,String numExpediente,HttpServletRequest request,HttpServletResponse response)
    {
        
        int codIdioma = 1;    
        try
        {
            UsuarioValueObject usuario = (UsuarioValueObject) request.getSession().getAttribute("usuario");
            if(usuario != null)
            {
                codIdioma = usuario.getIdioma();
            }
        }
        catch(Exception ex)
        {

        }
        try
        {
            //Cargar modificar preparador
            String idPrep = (String)request.getParameter("idPrepSustituir");
            if(idPrep != null)
            {
                EcaSolPreparadoresVO prep = new EcaSolPreparadoresVO();
                prep.setSolPreparadoresCod(Integer.parseInt(idPrep));
                prep = MeLanbide44Manager.getInstance().getPreparadorSolicitudPorId(prep, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                if(prep != null)
                {
                    log.debug(prep.getHorasCont() != null ? prep.getHorasCont().toPlainString() : "");
                    request.setAttribute("preparadorOrigen", prep);
                }
            }
        }
        catch(Exception ex)
        {
            
        }   
        return "/jsp/extension/melanbide44/solicitud/nuevoPreparadorSolicitud.jsp";
    }
    
    public String cargarSustituirProspectorSolicitud(int codOrganizacion,  int codTramite, int ocurrenciaTramite,String numExpediente,HttpServletRequest request,HttpServletResponse response)
    {
        
        int codIdioma = 1;    
        try
        {
            UsuarioValueObject usuario = (UsuarioValueObject) request.getSession().getAttribute("usuario");
            if(usuario != null)
            {
                codIdioma = usuario.getIdioma();
            }
        }
        catch(Exception ex)
        {

        }
        try
        {
            //Cargar modificar preparador
            String idPros = (String)request.getParameter("idProsSustituir");
            if(idPros != null)
            {
                EcaSolProspectoresVO pros = new EcaSolProspectoresVO();
                pros.setSolProspectoresCod(Integer.parseInt(idPros));
                pros = MeLanbide44Manager.getInstance().getProspectorSolicitudPorId(pros, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                if(pros != null)
                {
                    log.debug(pros.getHorasCont() != null ? pros.getHorasCont().toPlainString() : "");
                    request.setAttribute("prospectorOrigen", pros);
                }
            }
        }
        catch(Exception ex)
        {
            
        }   
        return "/jsp/extension/melanbide44/solicitud/nuevoProspectorSolicitud.jsp";
    }
   
    private List<FilaProspectorJustificacionVO> getListaProspectoresJustificacion(int codOrganizacion, String numExpediente, int codIdioma) throws Exception
    {
        AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
        MeLanbide44Manager meLanbide44Manager = MeLanbide44Manager.getInstance();
        EcaSolicitudVO sol = meLanbide44Manager.getDatosSolicitud(numExpediente, adapt);
        List<FilaProspectorJustificacionVO> prospectores = null;

        if(sol != null)
        {
            prospectores = MeLanbide44Manager.getInstance().getListaProspectoresJustificacion(sol, adapt, codIdioma);
        }
        else
        {
            prospectores = new ArrayList<FilaProspectorJustificacionVO>();
        }
        
        return prospectores;
    }
    
    private EcaResumenVO calcularDatosResumen(int codOrganizacion,  int codTramite, int ocurrenciaTramite,String numExpediente,HttpServletRequest request,HttpServletResponse response)
    {
        AdaptadorSQLBD adapt = null;
        EcaResumenVO res = null;
        
        try
        {
            adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
            EcaSolicitudVO sol = MeLanbide44Manager.getInstance().getDatosSolicitud(numExpediente, adapt);
            EcaConfiguracionVO config = MeLanbide44Manager.getInstance().getConfiguracionEca(MeLanbide44Utils.getEjercicioDeExpediente(numExpediente), adapt);
            if(sol != null)
            {
                int codIdioma = 1;    
                try
                {
                    UsuarioValueObject usuario = (UsuarioValueObject) request.getSession().getAttribute("usuario");
                    if(usuario != null)
                    {
                        codIdioma = usuario.getIdioma();
                    }
                }
                catch(Exception ex)
                {

                }
                Integer numPreparadoresSinErrores = null;
                Integer numProspectoresSinErrores = null;
                
                BigDecimal importePreparadores = null;
                BigDecimal importeProspectores = null;   
                
                BigDecimal gastosGenerales = null;
                
                BigDecimal subvTot = null;
                        
                try
                {
                    res = MeLanbide44Manager.getInstance().getResumenSolicitud(sol, adapt);
                }
                catch(Exception ex)
                {
                    log.error(" Error : " + ex.getMessage(), ex);
                }
                if(res == null)
                {
                    res = new EcaResumenVO();
                }
                
                //SOLICITADO
                
                subvTot = null;
                
                res.setNumeroProspectores_solic(sol.getProspectoresNum());
                res.setImporteProspectores_solic(sol.getProspectoresImp());
                res.setNumeroPreparadores_solic(sol.getPreparadoresNum());
                res.setImportePreparadores_solic(sol.getPreparadoresImp());
                res.setGastosGenerales_solic(sol.getGastos());
                res.setSubvOrgPublicos_solic(sol.getSubPub());
                res.setSubvOrgPrivados_solic(sol.getSubPriv());
                res.setOtrasSubv(sol.getOtrasSub());
                
                if(sol.getProspectoresImp() != null)
                {
                    if(subvTot == null)
                    {
                        subvTot = new BigDecimal("0.00");
                    }
                    subvTot = subvTot.add(sol.getProspectoresImp());
                }
                if(sol.getPreparadoresImp() != null)
                {
                    if(subvTot == null)
                    {
                        subvTot = new BigDecimal("0.00");
                    }
                    subvTot = subvTot.add(sol.getPreparadoresImp());
                }
                if(sol.getGastos() != null)
                {
                    if(subvTot == null)
                    {
                        subvTot = new BigDecimal("0.00");
                    }
                    subvTot = subvTot.add(sol.getGastos());
                }
                if(sol.getSubPub() != null)
                {
                    if(subvTot == null)
                    {
                        subvTot = new BigDecimal("0.00");
                    }
                    subvTot = subvTot.subtract(sol.getSubPub());
                }
                if(sol.getSubPriv() != null)
                {
                    if(subvTot == null)
                    {
                        subvTot = new BigDecimal("0.00");
                    }
                    subvTot = subvTot.subtract(sol.getSubPriv());
                }
                
                if(subvTot != null && subvTot.compareTo(BigDecimal.ZERO) < 0)
                {
                    subvTot = new BigDecimal("0.00");
                }
                
                res.setTotSubv_solic(subvTot);
                res.setTotAprobado_solic(sol.getTotalAprobado());
                
                
                //CONCEDIDO
                
                numPreparadoresSinErrores = null;
                numProspectoresSinErrores = null;
                
                importePreparadores = null;
                importeProspectores = null;   
                
                gastosGenerales = null;
                
                subvTot = null;
                               
                res.setNumeroPreparadores_conc(sol.getPreparadoresNumConc());
                res.setImportePreparadores_conc(sol.getPreparadoresImpConc());
                res.setNumeroProspectores_conc(sol.getProspectoresNumConc());
                res.setImporteProspectores_conc(sol.getProspectoresImpConc());
                res.setGastosGenerales_conc(sol.getGastosConc());
                res.setSubvOrgPublicos_conc(sol.getSubPubConcedidos());
                res.setSubvOrgPrivados_conc(sol.getSubPrivConcedidos());
                if(subvTot == null) {
                        subvTot = new BigDecimal("0.00");
                    }               
                subvTot = subvTot.add(sol.getPreparadoresImpConc()!=null?sol.getPreparadoresImpConc():new BigDecimal("0.00"));
                subvTot = subvTot.add(sol.getProspectoresImpConc()!=null?sol.getProspectoresImpConc():new BigDecimal("0.00"));
                subvTot = subvTot.add(sol.getGastosConc()!=null?sol.getGastosConc():new BigDecimal("0.00"));
                subvTot = subvTot.subtract(sol.getSubPubConcedidos()!=null?sol.getSubPubConcedidos():new BigDecimal("0.00"));
                subvTot = subvTot.subtract(sol.getSubPrivConcedidos()!=null?sol.getSubPrivConcedidos():new BigDecimal("0.00"));
                res.setTotSubv_conc(subvTot);
                
                /*FilaPreparadorSolicitudVO prep = null;        
                List<FilaPreparadorSolicitudVO> solPrepList = MeLanbide44Manager.getInstance().getListaPreparadoresSolicitud(sol, adapt, codIdioma);
                if (solPrepList!= null && solPrepList.size() >0)
                {
                    for (int i = 0; i <solPrepList.size(); i++)
                    {
                        prep = solPrepList.get(i);            
                        if (prep.getErrores().size()==0) 
                        {
                            if(importePreparadores == null)
                            {
                                importePreparadores = new BigDecimal("0.00");
                            }
                            if(numPreparadoresSinErrores == null)
                            {
                                numPreparadoresSinErrores = 0;
                            }
                            
                            numPreparadoresSinErrores++;
                            if((prep.getCostesSalarialesSS()!=null) && (!prep.getCostesSalarialesSS().equals("-")))
                            {
                               // numero= new BigDecimal(numformateado.replace(".", "").replace(",", "."));
                              importePreparadores = importePreparadores.add(new BigDecimal(prep.getCostesSalarialesSS().replace(".", "").replace(",",".")));
                            }
                        }
                    }
                }
                
                res.setNumeroPreparadores_conc(numPreparadoresSinErrores);
                res.setImportePreparadores_conc(importePreparadores);

                
                FilaProspectorSolicitudVO pros = null;        
                List<FilaProspectorSolicitudVO> solProsList = MeLanbide44Manager.getInstance().getListaProspectoresSolicitud(sol, adapt, codIdioma);
                if (solProsList!= null && solProsList.size() >0)
                {
                    for (int i = 0; i <solProsList.size(); i++)
                    {
                        pros = solProsList.get(i);            
                        if ((pros.getErrores().size()==0)|| (pros.getErrores().size()==1  &&  pros.getErrorCampo(11).equals("S")) ) 
                        {
                            if(importeProspectores == null)
                            {
                                importeProspectores = new BigDecimal("0.00");
                            }
                            if(numProspectoresSinErrores == null)
                            {
                                numProspectoresSinErrores = 0;
                            }
                            
                            numProspectoresSinErrores++;
                            if ( (pros.getCoste()!=null) && (!pros.getCoste().equals("-")) )
                            {
                              importeProspectores = importeProspectores.add(new BigDecimal(pros.getCoste().replace(".", "").replace(",",".")));
                            }
                        }
                    }
                }
                
                res.setNumeroProspectores_conc(numProspectoresSinErrores);
                res.setImporteProspectores_conc(importeProspectores);

                
                if(importeProspectores != null && importePreparadores != null)
                {
                    gastosGenerales = importeProspectores.add(importePreparadores);
                }
                else if(importeProspectores != null)
                {
                    gastosGenerales = importeProspectores;
                }
                else if(importePreparadores != null)
                {
                    gastosGenerales = importePreparadores;
                }
                
                if(gastosGenerales != null)
                {
                    gastosGenerales = gastosGenerales.multiply(config != null && config.getPoGastos() != null ? new BigDecimal(config.getPoGastos().toString()) : new BigDecimal("1"));
                    res.setGastosGenerales_conc(gastosGenerales);
                }
                
                res.setSubvOrgPublicos_conc(sol.getSubPubCargaManual());
                res.setSubvOrgPrivados_conc(sol.getSubPrivCargaManual());
                
                
                if(importePreparadores != null)
                {
                    if(subvTot == null)
                    {
                        subvTot = new BigDecimal("0.00");
                    }
                    subvTot = subvTot.add(importePreparadores);
                }
                if(importeProspectores != null)
                {
                    if(subvTot == null)
                    {
                        subvTot = new BigDecimal("0.00");
                    }
                    subvTot = subvTot.add(importeProspectores);
                }
                if(gastosGenerales != null)
                {
                    if(subvTot == null)
                    {
                        subvTot = new BigDecimal("0.00");
                    }
                    subvTot = subvTot.add(gastosGenerales);
                }
                if(sol.getSubPubCargaManual() != null)
                {
                    if(subvTot == null)
                    {
                        subvTot = new BigDecimal("0.00");
                    }
                    subvTot = subvTot.subtract(sol.getSubPubCargaManual());
                }
                if(sol.getSubPrivCargaManual() != null)
                {
                    if(subvTot == null)
                    {
                        subvTot = new BigDecimal("0.00");
                    }
                    subvTot = subvTot.subtract(sol.getSubPrivCargaManual());
                }
                
                if(subvTot != null && subvTot.compareTo(BigDecimal.ZERO) < 0)
                {
                    subvTot = new BigDecimal("0.00");
                }
                
                res.setTotSubv_conc(subvTot);*/
                
                //JUSTIFICADO
                
                numPreparadoresSinErrores = null;
                numProspectoresSinErrores = null;
                
                importePreparadores = null;
                importeProspectores = null;   
                
                gastosGenerales = null;
                
                subvTot = null;

                
                FilaProspectorJustificacionVO prosJ = null;        
                List<FilaProspectorJustificacionVO> solProsListJ = MeLanbide44Manager.getInstance().getListaProspectoresJustificacion(sol, adapt, codIdioma);
                if (solProsListJ!= null && solProsListJ.size() >0)
                {
                    for (int i = 0; i <solProsListJ.size(); i++)
                    {
                        prosJ = solProsListJ.get(i);            
                        if (prosJ.getErrores().isEmpty()|| (prosJ.getErrores().size()==1  &&  prosJ.getErrorCampo(11).equals("S"))
                                 //CONTRA PROSPECTOR AUNQUE ERRORES EN VISITAS                                
                           ) 
                        {
                            if(importeProspectores == null)
                            {
                                importeProspectores = new BigDecimal("0.00");
                            }
                            if(numProspectoresSinErrores == null)
                            {
                                numProspectoresSinErrores = 0;
                            }
                            

                        //comentamos para que cuente tb sustitutos 
                        //#241430/Nº de Prospectores y Nºde Preparadores debe tener en cuenta los sustitutos (aunque el sustituto tenga errores)
                            //Si es sustituto en la solicitud, no se cuenta para el numero pero si debe contabilizarse para importe
                            /*if(prosJ.getTipoSust() == null || !prosJ.getTipoSust().equals(ConstantesMeLanbide44.TIPOS_SUSTITUTO.SUSTITUTO_SOLICITUD))
                            {
                                numProspectoresSinErrores++;
                            }*/
                            numProspectoresSinErrores++;
                            if (prosJ.getVisitasImp() != null && !prosJ.getVisitasImp().equals("-"))
                            {
                                importeProspectores = importeProspectores.add(new BigDecimal(prosJ.getVisitasImp().replace(".", "").replace(",",".")));
                            }
                        }
                    }
                }
                
                res.setNumeroProspectores_justif(numProspectoresSinErrores);
                res.setImporteProspectores_justif(importeProspectores);
                
                FilaPreparadorJustificacionVO prepJ = null;  
                MeLanbide44I18n traductor = MeLanbide44I18n.getInstance();
                List<FilaPreparadorJustificacionVO> solPrepListJ = MeLanbide44Manager.getInstance().getListaPreparadoresJustificacion(sol, adapt, codIdioma);
                if (solPrepListJ!= null && solPrepListJ.size() >0)
                {
                    for (int i = 0; i <solPrepListJ.size(); i++)
                    {
                        prepJ = solPrepListJ.get(i); 
                        //
                        log.debug("prep nº errores: "+prepJ.getErrores().size());
                        log.debug("Error en nif prep?: "+prepJ.getErrorCampo(1));
                        log.debug("Errores texto: "+prepJ.getErrorCampo(1));
                        if (prepJ.getErrores().isEmpty()                            
                            //SI TIENE ERRORES PERO DE SEGUIMIENTOS O INSERCIONES HAY QUE CONTAR EL PREPARADOR
                            || (prepJ.getErrores().size()==1  &&  prepJ.getErrorCampo(1).equals("S") 
                                //SI MENSAJE TIENE QUE VER CON SEGUIMIENTOS O INSERCIOENS                                
                                && 
                                (prepJ.getErrores().contains(traductor.getMensaje(codIdioma, "validaciones.justificacion.preparadores.SegErrores"))
                                || prepJ.getErrores().contains(traductor.getMensaje(codIdioma, "validaciones.justificacion.preparadores.C1Errores"))
                                || prepJ.getErrores().contains(traductor.getMensaje(codIdioma, "validaciones.justificacion.preparadores.C2Errores"))
                                || prepJ.getErrores().contains(traductor.getMensaje(codIdioma, "validaciones.justificacion.preparadores.C3Errores"))
                                || prepJ.getErrores().contains(traductor.getMensaje(codIdioma, "validaciones.justificacion.preparadores.C4Errores"))
                                || prepJ.getErrores().contains(traductor.getMensaje(codIdioma, "validaciones.justificacion.preparadores.C5Errores"))
                                || prepJ.getErrores().contains(traductor.getMensaje(codIdioma, "validaciones.justificacion.preparadores.C6Errores"))
                                || prepJ.getErrores().contains(traductor.getMensaje(codIdioma, "validaciones.justificacion.preparadores.NoColectivoErrores"))
                                )
                             )   
                           || (prepJ.getErrores().size()==1  &&  prepJ.getErrorCampo(12).equals("S"))   //seguimientos 
                           || (prepJ.getErrores().size()==2  &&  prepJ.getErrorCampo(1).equals("S") && prepJ.getErrorCampo(12).equals("S"))   //INSERCIONES Y seguimientos   
                        )
                        {
                            if(importePreparadores == null)
                            {
                                importePreparadores = new BigDecimal("0.00");
                            }
                            if(numPreparadoresSinErrores == null)
                            {
                                numPreparadoresSinErrores = 0;
                            }

                            //comentamos para que cuente tb sustitutos 
                            //#241430/Nº de Prospectores y Nºde Preparadores debe tener en cuenta los sustitutos (aunque el sustituto tenga errores)
                            //Si es sustituto en la solicitud, no se cuenta para el numero pero si debe contabilizarse para importe
                            /*if(prepJ.getTipoSust() == null || !prepJ.getTipoSust().equals(ConstantesMeLanbide44.TIPOS_SUSTITUTO.SUSTITUTO_SOLICITUD))
                            {
                                numPreparadoresSinErrores++;
                            }*/
                            numPreparadoresSinErrores++;
                            if(prepJ.getCostesSalarialesSS() != null && !prepJ.getCostesSalarialesSS().equals("-"))
                            {
                               // numero= new BigDecimal(numformateado.replace(".", "").replace(",", "."));
                              //importePreparadores = importePreparadores.add(new BigDecimal(prepJ.getCostesSalarialesSS().replace(".", "").replace(",",".")));
                              //#241430: importe preparadores tiene q ser importe de seguimientos/inserciones
                                importePreparadores = importePreparadores.add(new BigDecimal(prepJ.getSeguimientosInserciones().replace(".", "").replace(",",".")));
                                
                            }
                        }
                    }
                }
                
                res.setNumeroPreparadores_justif(numPreparadoresSinErrores);
                res.setImportePreparadores_justif(importePreparadores);
                
                if(gastosGenerales == null)
                {
                    gastosGenerales = new BigDecimal("0.00");
                }
                
                if(importeProspectores != null)
                {
                    gastosGenerales = gastosGenerales.add(importeProspectores);
                }
                
                if(importePreparadores != null)
                {
                    gastosGenerales = gastosGenerales.add(importePreparadores);
                }
                
                if(gastosGenerales != null)
                {
                    gastosGenerales = gastosGenerales.multiply(config != null && config.getPoGastos() != null ? new BigDecimal(config.getPoGastos().toString()) : new BigDecimal("1"));
                    res.setGastosGenerales_justif(gastosGenerales);
                }
                
                res.setSubvOrgPrivados_justif(res.getEcaResSubPriv() != null ? res.getEcaResSubPriv() : res.getSubvOrgPrivados_solic());
                res.setSubvOrgPublicos_justif(res.getEcaResSubPub() != null ? res.getEcaResSubPub() : res.getSubvOrgPublicos_solic());
                
                if(res.getEcaResTotSubv() == null)
                {
                
                    if(importePreparadores != null)
                    {
                        if(subvTot == null)
                        {
                            subvTot = new BigDecimal("0.00");
                        }
                        subvTot = subvTot.add(importePreparadores);
                    }
                    if(importeProspectores != null)
                    {
                        if(subvTot == null)
                        {
                            subvTot = new BigDecimal("0.00");
                        }
                        subvTot = subvTot.add(importeProspectores);
                    }
                    if(gastosGenerales != null)
                    {
                        if(subvTot == null)
                        {
                            subvTot = new BigDecimal("0.00");
                        }
                        subvTot = subvTot.add(gastosGenerales);
                    }
                    if(res.getSubvOrgPublicos_justif() != null)
                    {
                        if(subvTot == null)
                        {
                            subvTot = new BigDecimal("0.00");
                        }
                        subvTot = subvTot.subtract(res.getSubvOrgPublicos_justif());
                    }
                    if(res.getSubvOrgPrivados_justif() != null)
                    {
                        if(subvTot == null)
                        {
                            subvTot = new BigDecimal("0.00");
                        }
                        subvTot = subvTot.subtract(res.getSubvOrgPrivados_justif());
                    }
                    
                    res.setTotSubv_justif(subvTot);
                }
                else
                {
                    res.setTotSubv_justif(res.getEcaResTotSubv());
                }
                
                if(res.getTotSubv_justif().compareTo(BigDecimal.ZERO) < 0)
                {
                    res.setTotSubv_justif(new BigDecimal("0.00"));
                }
                
                //PAGADO
                
                //numPreparadoresSinErrores = null;
                //numProspectoresSinErrores = null;
                
                /*importePreparadores = null;
                importeProspectores = null;   
                
                gastosGenerales = null;
                
                BigDecimal costesSalarialesSSECA = null;
                BigDecimal total = null;
                
                subvTot = null;
                
                //El numero de prospectores es el mismo que en la columna JUSTIFICADO
                res.setNumeroProspectores_pag(numProspectoresSinErrores);
                
                //El importe de prospectores es el SUMATORIO (MIN(COSTES SALARIALES + SS ECA, TOTAL))
                if (solProsListJ!= null && solProsListJ.size() >0)
                {
                    for (int i = 0; i <solProsListJ.size(); i++)
                    {
                        prosJ = solProsListJ.get(i);            
                        if ((prosJ.getErrores().isEmpty())|| (prosJ.getErrores().size()==1  &&  prosJ.getErrorCampo(11).equals("S")) ) 
                        {
                            if(importeProspectores == null)
                            {
                                importeProspectores = new BigDecimal("0.00");
                            }
                            
                            if (prosJ.getCostesSalarialesSSEca() != null && !prosJ.getCostesSalarialesSSEca().equals("-"))
                            {
                                costesSalarialesSSECA = new BigDecimal(prosJ.getCostesSalarialesSSEca().replace(".", "").replace(",","."));
                            }
                            else
                            {
                                costesSalarialesSSECA = new BigDecimal("0.00");
                            }
                            
                            if(prosJ.getVisitasImp() != null && !prosJ.getVisitasImp().equals("-"))
                            {
                                total = new BigDecimal(prosJ.getVisitasImp().replace(".", "").replace(",","."));
                            }
                            else
                            {
                                total = new BigDecimal("0.00");
                            }    
                            importeProspectores = importeProspectores.add(new BigDecimal(Math.min(costesSalarialesSSECA.doubleValue(), total.doubleValue())));
                        }
                    }
                }
                
                res.setImporteProspectores_pag(importeProspectores);
                
                //El numero de preparadores es el mismo que en la columna JUSTIFICADO
                res.setNumeroPreparadores_pag(numPreparadoresSinErrores);
                
                //El importe de preparadores es el SUMATORIO (SEG+INS)
                if (solPrepListJ!= null && solPrepListJ.size() >0)
                {
                    for (int i = 0; i <solPrepListJ.size(); i++)
                    {
                        prepJ = solPrepListJ.get(i);            
                        if (prepJ.getErrores().isEmpty()) 
                        {
                            if(importePreparadores == null)
                            {
                                importePreparadores = new BigDecimal("0.00");
                            }
                            
                            if(prepJ.getSeguimientosInserciones() != null && !prepJ.getSeguimientosInserciones().equals("-"))
                            {
                                // numero= new BigDecimal(numformateado.replace(".", "").replace(",", "."));
                                importePreparadores = importePreparadores.add(new BigDecimal(prepJ.getSeguimientosInserciones().replace(".", "").replace(",",".")));
                            }
                        }
                    }
                }
                
                res.setImportePreparadores_pag(importePreparadores);
                
                if(gastosGenerales == null)
                {
                    gastosGenerales = new BigDecimal("0.00");
                }

                if(importeProspectores != null)
                {
                    gastosGenerales = gastosGenerales.add(importeProspectores);
                }
                
                if(importePreparadores != null)
                {
                    gastosGenerales = gastosGenerales.add(importePreparadores);
                }
                
                if(gastosGenerales != null)
                {
                    gastosGenerales = gastosGenerales.multiply(config != null && config.getPoGastos() != null ? new BigDecimal(config.getPoGastos().toString()) : new BigDecimal("1"));
                    res.setGastosGenerales_pag(gastosGenerales);
                }
                
                res.setSubvOrgPublicos_pag(res.getSubvOrgPublicos_justif());
                res.setSubvOrgPrivados_pag(res.getSubvOrgPrivados_justif());
                //res.setTotSubv_pag(res.getTotSubv_justif());
                if(importePreparadores != null)
                {
                    if(subvTot == null)
                    {
                        subvTot = new BigDecimal("0.00");
                    }
                    subvTot = subvTot.add(importePreparadores.setScale(2, RoundingMode.HALF_UP));
                }
                if(importeProspectores != null)
                {
                    if(subvTot == null)
                    {
                        subvTot = new BigDecimal("0.00");
                    }
                    subvTot = subvTot.add(importeProspectores.setScale(2, RoundingMode.HALF_UP));
                }
                if(gastosGenerales != null)
                {
                    if(subvTot == null)
                    {
                        subvTot = new BigDecimal("0.00");
                    }
                    subvTot = subvTot.add(gastosGenerales.setScale(2, RoundingMode.HALF_UP));
                }
                if(res.getSubvOrgPublicos_pag() != null)
                {
                    if(subvTot == null)
                    {
                        subvTot = new BigDecimal("0.00");
                    }
                    subvTot = subvTot.subtract(res.getSubvOrgPublicos_pag().setScale(2, RoundingMode.HALF_UP));
                }
                if(res.getSubvOrgPrivados_pag() != null)
                {
                    if(subvTot == null)
                    {
                        subvTot = new BigDecimal("0.00");
                    }
                    subvTot = subvTot.subtract(res.getSubvOrgPrivados_pag().setScale(2, RoundingMode.HALF_UP));
                }
                
                res.setTotSubv_pag(subvTot);*/
                
                importePreparadores = null;
                importeProspectores = null;   
                
                gastosGenerales = null;
                
                BigDecimal impPagPros = null;
                BigDecimal total = null;
                
                subvTot = null;
                
                List<FilaEcaResProspectoresVO> listaProspectoresResumen = MeLanbide44Manager.getInstance().getListaDetalleProspectoresResumen(numExpediente, codIdioma, adapt);
                List<FilaEcaResPreparadoresVO> listaPreparadoresResumen = MeLanbide44Manager.getInstance().getListaDetallePreparadoresResumen(numExpediente, adapt);
                
                FilaEcaResProspectoresVO filaProsJ = null;
                FilaEcaResPreparadoresVO filaPrepJ = null;
                
                int numPrep = 0;
                int numPros = 0;
                
                //El importe de prospectores es el SUMATORIO (MIN(COSTES SALARIALES + SS ECA, TOTAL))
                if (listaProspectoresResumen != null)
                {
                    
                    for (int i = 0; i <listaProspectoresResumen.size(); i++)
                    {
                        filaProsJ = listaProspectoresResumen.get(i);
                        
                        
                        //comentamos para que cuente tb sustitutos 
                        //#241430/Nº de Prospectores y Nºde Preparadores debe tener en cuenta los sustitutos (aunque el sustituto tenga errores)
                        /*//Si es sustituto en la solicitud, no se cuenta para el numero pero si debe contabilizarse para importe
                        if(filaProsJ.getTipoSust() == null || !filaProsJ.getTipoSust().equals(ConstantesMeLanbide44.TIPOS_SUSTITUTO.SUSTITUTO_SOLICITUD))
                        {
                            numPros++;
                        }*/
                        numPros++;
                        
                        if(importeProspectores == null)
                        {
                            importeProspectores = new BigDecimal("0.00");
                        }

                        if (filaProsJ.getImpPagar() != null && !filaProsJ.getImpPagar().equals("-"))
                        {
                            impPagPros = new BigDecimal(filaProsJ.getImpPagar().replace(".", "").replace(",","."));
                        }
                        else
                        {
                            impPagPros = new BigDecimal("0.00");
                        } 
                        importeProspectores = importeProspectores.add(impPagPros);
                    }
                }
                
                res.setNumeroProspectores_pag(numPros);
                res.setImporteProspectores_pag(importeProspectores);
                
                if (listaPreparadoresResumen != null)
                {
                    for (int i = 0; i <listaPreparadoresResumen.size(); i++)
                    {
                        filaPrepJ = listaPreparadoresResumen.get(i);     
                        
                        if(filaPrepJ.getTipoSust() == null || !filaPrepJ.getTipoSust().equals(ConstantesMeLanbide44.TIPOS_SUSTITUTO.SUSTITUTO_SOLICITUD))
                        {
                            numPrep++;
                        }
                        
                        if(importePreparadores == null)
                        {
                            importePreparadores = new BigDecimal("0.00");
                        }

                        if(filaPrepJ.getImpPagar() != null && !filaPrepJ.getImpPagar().equals("-"))
                        {
                            importePreparadores = importePreparadores.add(new BigDecimal(filaPrepJ.getImpPagar().replace(".", "").replace(",",".")));
                        }
                    }
                }
                
                res.setNumeroPreparadores_pag(numPrep);
                res.setImportePreparadores_pag(importePreparadores);
                
                if(gastosGenerales == null)
                {
                    gastosGenerales = new BigDecimal("0.00");
                }

                if(importeProspectores != null)
                {
                    gastosGenerales = gastosGenerales.add(importeProspectores);
                }
                
                if(importePreparadores != null)
                {
                    gastosGenerales = gastosGenerales.add(importePreparadores);
                }
                gastosGenerales = gastosGenerales.multiply(config != null && config.getPoGastos() != null ? new BigDecimal(config.getPoGastos().toString()) : new BigDecimal("1"));
                res.setTopeGastosGenerales_pag(gastosGenerales);
                
                if(res.getGastosGenerales_pag() == null && gastosGenerales != null)
                {
                    gastosGenerales = gastosGenerales.multiply(config != null && config.getPoGastos() != null ? new BigDecimal(config.getPoGastos().toString()) : new BigDecimal("1"));
                    res.setGastosGenerales_pag(gastosGenerales);
                }
                
                res.setSubvOrgPublicos_pag(res.getSubvOrgPublicos_justif());
                res.setSubvOrgPrivados_pag(res.getSubvOrgPrivados_justif());
                //res.setTotSubv_pag(res.getTotSubv_justif());
                if(importePreparadores != null)
                {
                    if(subvTot == null)
                    {
                        subvTot = new BigDecimal("0.00");
                    }
                    subvTot = subvTot.add(importePreparadores.setScale(2, RoundingMode.HALF_UP));
                }
                if(importeProspectores != null)
                {
                    if(subvTot == null)
                    {
                        subvTot = new BigDecimal("0.00");
                    }
                    subvTot = subvTot.add(importeProspectores.setScale(2, RoundingMode.HALF_UP));
                }
                if(gastosGenerales != null)
                {
                    if(subvTot == null)
                    {
                        subvTot = new BigDecimal("0.00");
                    }
                    subvTot = subvTot.add(gastosGenerales.setScale(2, RoundingMode.HALF_UP));
                }
                if(res.getSubvOrgPublicos_pag() != null)
                {
                    if(subvTot == null)
                    {
                        subvTot = new BigDecimal("0.00");
                    }
                    subvTot = subvTot.subtract(res.getSubvOrgPublicos_pag().setScale(2, RoundingMode.HALF_UP));
                }
                if(res.getSubvOrgPrivados_pag() != null)
                {
                    if(subvTot == null)
                    {
                        subvTot = new BigDecimal("0.00");
                    }
                    subvTot = subvTot.subtract(res.getSubvOrgPrivados_pag().setScale(2, RoundingMode.HALF_UP));
                }
                
                if(subvTot != null && subvTot.compareTo(BigDecimal.ZERO) < 0)
                {
                    subvTot = new BigDecimal("0.00");
                }
                
                res.setTotSubv_pag(subvTot);
            }
        }
        catch(Exception ex)
        {
            log.error(" Error : " + ex.getMessage(), ex);
        }
        
        return res;
    }
    
    public void getDatosResumenEca(int codOrganizacion,  int codTramite, int ocurrenciaTramite,String numExpediente,HttpServletRequest request,HttpServletResponse response)
    {
        
        String codigoOperacion = "0";
        int codIdioma = 1;    
        EcaResumenVO resumen = null;
        try
        {
            UsuarioValueObject usuario = (UsuarioValueObject) request.getSession().getAttribute("usuario");
            if(usuario != null)
            {
                codIdioma = usuario.getIdioma();
            }
        }
        catch(Exception ex)
        {

        }
        try
        {
            numExpediente = (String)request.getParameter("numero");
            resumen = this.calcularDatosResumen(codOrganizacion, codTramite, ocurrenciaTramite, numExpediente, request, response);
            
        }
        catch(Exception ex)
        {
            codigoOperacion = "1";
        }
        
        escribirDatosResumenRequest(codigoOperacion, resumen, codIdioma, response);
    }
    
    private void escribirDatosResumenRequest(String codigoOperacion, EcaResumenVO resumen, int codigoIdioma, HttpServletResponse response)
    {
        if(resumen == null)
        {
            resumen = new EcaResumenVO();
        }
        StringBuffer xmlSalida = new StringBuffer();
        xmlSalida.append("<RESPUESTA>");
            xmlSalida.append("<CODIGO_OPERACION>");
                xmlSalida.append(codigoOperacion);
            xmlSalida.append("</CODIGO_OPERACION>");
            xmlSalida.append("<VALORES_SOLICITADO>");
                xmlSalida.append("<NUM_PROS>");
                    xmlSalida.append(resumen.getNumeroProspectores_solic() != null ? resumen.getNumeroProspectores_solic().toString() : "");
                xmlSalida.append("</NUM_PROS>");
                xmlSalida.append("<IMP_PROS>");
                    xmlSalida.append(resumen.getImporteProspectores_solic() != null ? resumen.getImporteProspectores_solic().toString() : "");
                xmlSalida.append("</IMP_PROS>");
                xmlSalida.append("<NUM_PREP>");
                    xmlSalida.append(resumen.getNumeroPreparadores_solic() != null ? resumen.getNumeroPreparadores_solic().toString() : "");
                xmlSalida.append("</NUM_PREP>");
                xmlSalida.append("<IMP_PREP>");
                    xmlSalida.append(resumen.getImportePreparadores_solic() != null ? resumen.getImportePreparadores_solic().toString() : "");
                xmlSalida.append("</IMP_PREP>");
                xmlSalida.append("<GASTOS_GENERALES>");
                    xmlSalida.append(resumen.getGastosGenerales_solic() != null ? resumen.getGastosGenerales_solic().toString() : "");
                xmlSalida.append("</GASTOS_GENERALES>");
                xmlSalida.append("<FL_OTRAS_SUBV>");
                    xmlSalida.append(resumen.getOtrasSubv() != null && resumen.getOtrasSubv() == true ? resumen.getOtrasSubv().toString() : "");
                xmlSalida.append("</FL_OTRAS_SUBV>");
                xmlSalida.append("<OTRAS_SUBV_PUB>");
                    xmlSalida.append(resumen.getSubvOrgPublicos_solic() != null ? resumen.getSubvOrgPublicos_solic().toString() : "");
                xmlSalida.append("</OTRAS_SUBV_PUB>");
                xmlSalida.append("<OTRAS_SUBV_PRIV>");
                    xmlSalida.append(resumen.getSubvOrgPrivados_solic() != null ? resumen.getSubvOrgPrivados_solic().toString() : "");
                xmlSalida.append("</OTRAS_SUBV_PRIV>");
                xmlSalida.append("<TOT_SUBV>");
                    xmlSalida.append(resumen.getTotSubv_solic() != null ? resumen.getTotSubv_solic().toString() : "");
                xmlSalida.append("</TOT_SUBV>");
                xmlSalida.append("<TOT_SUBV_APROBADA>");
                    xmlSalida.append(resumen.getTotAprobado_solic() != null ? resumen.getTotAprobado_solic().toString() : "");
                xmlSalida.append("</TOT_SUBV_APROBADA>");
            xmlSalida.append("</VALORES_SOLICITADO>");

            xmlSalida.append("<VALORES_CONCEDIDO>");
                xmlSalida.append("<NUM_PROS>");
                    xmlSalida.append(resumen.getNumeroProspectores_conc() != null ? resumen.getNumeroProspectores_conc().toString() : "");
                xmlSalida.append("</NUM_PROS>");
                xmlSalida.append("<IMP_PROS>");
                    xmlSalida.append(resumen.getImporteProspectores_conc() != null ? resumen.getImporteProspectores_conc().toString() : "");
                xmlSalida.append("</IMP_PROS>");
                xmlSalida.append("<NUM_PREP>");
                    xmlSalida.append(resumen.getNumeroPreparadores_conc() != null ? resumen.getNumeroPreparadores_conc().toString() : "");
                xmlSalida.append("</NUM_PREP>");
                xmlSalida.append("<IMP_PREP>");
                    xmlSalida.append(resumen.getImportePreparadores_conc() != null ? resumen.getImportePreparadores_conc().toString() : "");
                xmlSalida.append("</IMP_PREP>");
                xmlSalida.append("<GASTOS_GENERALES>");
                    xmlSalida.append(resumen.getGastosGenerales_conc() != null ? resumen.getGastosGenerales_conc().toString() : "");
                xmlSalida.append("</GASTOS_GENERALES>");
                xmlSalida.append("<OTRAS_SUBV_PUB>");
                    xmlSalida.append(resumen.getSubvOrgPublicos_conc() != null ? resumen.getSubvOrgPublicos_conc().toString() : "");
                xmlSalida.append("</OTRAS_SUBV_PUB>");
                xmlSalida.append("<OTRAS_SUBV_PRIV>");
                    xmlSalida.append(resumen.getSubvOrgPrivados_conc() != null ? resumen.getSubvOrgPrivados_conc().toString() : "");
                xmlSalida.append("</OTRAS_SUBV_PRIV>");
                xmlSalida.append("<TOT_SUBV>");
                    xmlSalida.append(resumen.getTotSubv_conc() != null ? resumen.getTotSubv_conc().toString() : "");
                xmlSalida.append("</TOT_SUBV>");
            xmlSalida.append("</VALORES_CONCEDIDO>");

            xmlSalida.append("<VALORES_JUSTIFICADO>");
                xmlSalida.append("<NUM_PROS>");
                    xmlSalida.append(resumen.getNumeroProspectores_justif() != null ? resumen.getNumeroProspectores_justif().toString() : "");
                xmlSalida.append("</NUM_PROS>");
                xmlSalida.append("<IMP_PROS>");
                    xmlSalida.append(resumen.getImporteProspectores_justif() != null ? resumen.getImporteProspectores_justif().toString() : "");
                xmlSalida.append("</IMP_PROS>");
                xmlSalida.append("<NUM_PREP>");
                    xmlSalida.append(resumen.getNumeroPreparadores_justif() != null ? resumen.getNumeroPreparadores_justif().toString() : "");
                xmlSalida.append("</NUM_PREP>");
                xmlSalida.append("<IMP_PREP>");
                    xmlSalida.append(resumen.getImportePreparadores_justif() != null ? resumen.getImportePreparadores_justif().toString() : "");
                xmlSalida.append("</IMP_PREP>");
                xmlSalida.append("<GASTOS_GENERALES>");
                    xmlSalida.append(resumen.getGastosGenerales_justif() != null ? resumen.getGastosGenerales_justif().toString() : "");
                xmlSalida.append("</GASTOS_GENERALES>");
                xmlSalida.append("<OTRAS_SUBV_PUB>");
                    xmlSalida.append(resumen.getSubvOrgPublicos_justif() != null ? resumen.getSubvOrgPublicos_justif().toString() : "");
                xmlSalida.append("</OTRAS_SUBV_PUB>");
                xmlSalida.append("<OTRAS_SUBV_PRIV>");
                    xmlSalida.append(resumen.getSubvOrgPrivados_justif() != null ? resumen.getSubvOrgPrivados_justif().toString() : "");
                xmlSalida.append("</OTRAS_SUBV_PRIV>");
                xmlSalida.append("<TOT_SUBV>");
                    xmlSalida.append(resumen.getTotSubv_justif() != null ? resumen.getTotSubv_justif().toString() : "");
                xmlSalida.append("</TOT_SUBV>");
            xmlSalida.append("</VALORES_JUSTIFICADO>");

            xmlSalida.append("<VALORES_PAGAR>");
                xmlSalida.append("<NUM_PROS>");
                    xmlSalida.append(resumen.getNumeroProspectores_pag() != null ? resumen.getNumeroProspectores_pag().toString() : "");
                xmlSalida.append("</NUM_PROS>");
                xmlSalida.append("<IMP_PROS>");
                    xmlSalida.append(resumen.getImporteProspectores_pag() != null ? resumen.getImporteProspectores_pag().toString() : "");
                xmlSalida.append("</IMP_PROS>");
                xmlSalida.append("<NUM_PREP>");
                    xmlSalida.append(resumen.getNumeroPreparadores_pag() != null ? resumen.getNumeroPreparadores_pag().toString() : "");
                xmlSalida.append("</NUM_PREP>");
                xmlSalida.append("<IMP_PREP>");
                    xmlSalida.append(resumen.getImportePreparadores_pag() != null ? resumen.getImportePreparadores_pag().toString() : "");
                xmlSalida.append("</IMP_PREP>");
                xmlSalida.append("<GASTOS_GENERALES>");
                    xmlSalida.append(resumen.getGastosGenerales_pag() != null ? resumen.getGastosGenerales_pag().toString() : "");
                xmlSalida.append("</GASTOS_GENERALES>");
                xmlSalida.append("<OTRAS_SUBV_PUB>");
                    xmlSalida.append(resumen.getSubvOrgPublicos_pag() != null ? resumen.getSubvOrgPublicos_pag().toString() : "");
                xmlSalida.append("</OTRAS_SUBV_PUB>");
                xmlSalida.append("<OTRAS_SUBV_PRIV>");
                    xmlSalida.append(resumen.getSubvOrgPrivados_pag() != null ? resumen.getSubvOrgPrivados_pag().toString() : "");
                xmlSalida.append("</OTRAS_SUBV_PRIV>");
                xmlSalida.append("<TOT_SUBV>");
                    xmlSalida.append(resumen.getTotSubv_pag() != null ? resumen.getTotSubv_pag().toString() : "");
                xmlSalida.append("</TOT_SUBV>"); 
                xmlSalida.append("<TOPE_GASTOS_GENERALES>");
                    xmlSalida.append(resumen.getTopeGastosGenerales_pag() != null ? resumen.getTopeGastosGenerales_pag().toString() : "");
                xmlSalida.append("</TOPE_GASTOS_GENERALES>");
            xmlSalida.append("</VALORES_PAGAR>");
        xmlSalida.append("</RESPUESTA>");
        try{
            response.setContentType("text/xml");
            response.setCharacterEncoding("UTF-8");
            PrintWriter out = response.getWriter();
            out.print(xmlSalida.toString());
            out.flush();
            out.close();
        }catch(Exception e){
            log.error(" Error : " + e.getMessage(), e);
        }//try-catch
    }
}

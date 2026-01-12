/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package es.altia.flexia.integracion.moduloexterno.melanbide46;

import es.altia.agora.business.escritorio.UsuarioValueObject; 
import es.altia.agora.business.terceros.CondicionesBusquedaTerceroVO;
import es.altia.agora.business.terceros.TercerosValueObject;
import es.altia.agora.business.terceros.persistence.TercerosManager;
import es.altia.agora.technical.ConstantesDatos;
import es.altia.common.exception.TechnicalException;
import es.altia.flexia.integracion.moduloexterno.melanbide46.i18n.MeLanbide46I18n;
import es.altia.flexia.integracion.moduloexterno.melanbide46.manager.MeLanbide46Manager;
import es.altia.flexia.integracion.moduloexterno.melanbide46.util.ConfigurationParameter;
import es.altia.flexia.integracion.moduloexterno.melanbide46.util.ConstantesMeLanbide46;
import es.altia.flexia.integracion.moduloexterno.melanbide46.util.MeLanbide46MappingUtils;
import es.altia.flexia.integracion.moduloexterno.melanbide46.util.MeLanbide46Utils;
import es.altia.flexia.integracion.moduloexterno.melanbide46.util.MeLanbide46Validator;
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
import es.altia.flexia.integracion.moduloexterno.melanbide46.vo.SelectItem;
import es.altia.flexia.integracion.moduloexterno.melanbide46.vo.datosCmeVO;
import es.altia.flexia.integracion.moduloexterno.melanbide60.MELANBIDE60;
import es.altia.flexia.integracion.moduloexterno.plugin.ModuloIntegracionExterno;
import es.altia.flexia.integracion.moduloexterno.plugin.ModuloIntegracionExternoCampoSupFactoria;
import es.altia.flexia.integracion.moduloexterno.plugin.camposuplementario.IModuloIntegracionExternoCamposFlexia;
import es.altia.flexia.integracion.moduloexterno.plugin.persistence.vo.SalidaIntegracionVO;
import es.altia.flexia.integracion.moduloexterno.plugin.persistence.vo.ValorCampoDesplegableModuloIntegracionVO;
import es.altia.technical.PortableContext;
import es.altia.util.conexion.AdaptadorSQLBD;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Vector;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFPalette;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
//import org.apache.poi.hssf.util.Region;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.struts.upload.CommonsMultipartRequestHandler;
import org.apache.struts.upload.FormFile;
import org.apache.struts.upload.MultipartRequestWrapper;

/**
 *
 * @author santiagoc
 */
public class MELANBIDE46 extends ModuloIntegracionExterno 
{
    //Logger
    private static Logger log = LogManager.getLogger(MELANBIDE46.class);
    ResourceBundle m_Conf = ResourceBundle.getBundle("common");
    
    public String lanzaHistoricoCme (int codOrganizacion,int codTramite,int ocurrenciaTramite, String numExpediente)
   {
        if(log.isDebugEnabled()) log.debug("lanzaHistoricoCme ( codOrganizacion = " + codOrganizacion + " codTramite = " + codTramite + " ocurrenciaTramite = " + ocurrenciaTramite + " numExpediente = " + numExpediente  + " ) : BEGIN");
       String clave= null;
        String codigoOperacion = "0";
        String mensaje = "";
        
        try{           
            if(numExpediente!=null && !"".equals(numExpediente)){
                AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
                                                     
                 MeLanbide46Manager.getInstance().guardaHistorico(numExpediente, String.valueOf(codTramite), adapt);
            }
        }
        catch(Exception ex)
        {
            codigoOperacion = "2";
        }
        if(log.isDebugEnabled()) log.debug("lanzaHistorico() : END");
        return codigoOperacion;
    }
    
    public String borrarHistoricoCme (int codOrganizacion,int codTramite,int ocurrenciaTramite, String numExpediente)
   {
        if(log.isDebugEnabled()) log.debug("borrarHistorico ( codOrganizacion = " + codOrganizacion + " codTramite = " + codTramite + " ocurrenciaTramite = " + ocurrenciaTramite + " numExpediente = " + numExpediente  + " ) : BEGIN");
       String clave= null;
        String codigoOperacion = "0";
        String mensaje = "";
        
        try{           
            if(numExpediente!=null && !"".equals(numExpediente)){
                AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
                                                     
                 MeLanbide46Manager.getInstance().borraHistorico(numExpediente, String.valueOf(codTramite), adapt);
            }
        }
        catch(Exception ex)
        {
            codigoOperacion = "2";
        }
        if(log.isDebugEnabled()) log.debug("borrarHistorico() : END");
        return codigoOperacion;
    }
    
    public String cargarPantallaDatosCme(int codOrganizacion,  int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response)
    {
        //Cargo los datos CME
        try
        {
            int ano = MeLanbide46Utils.getEjercicioDeExpediente(numExpediente);
            if(ano >2015)
            {
                MELANBIDE60 melanbide60 = new MELANBIDE60();
                return melanbide60.cargarPantallaDatosCme(codOrganizacion, codTramite, ocurrenciaTramite, numExpediente, request, response); 
            }
            else
            {
                cargarDatosCme(codOrganizacion, numExpediente, request);
                try
                {
                    datosCmeVO datos = MeLanbide46Manager.getInstance().obtenerDatosCme(codOrganizacion,numExpediente,this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                    request.setAttribute("datosCme", datos);
                }
                catch(Exception ex)
                {
                    log.error("Error obteniendo los datos de cme " + ex);
                }
                //Cargo los datos de las pestaÃ±as
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
                    url = cargarSubpestanaSolicitud_DatosCme(numExpediente, adapt, request);
                    if(url != null)
                    {
                        request.setAttribute("urlPestanaDatos_solicitud", url);
                    }
                }
                catch(Exception ex)
                {

                }
                try
                {
                    url = cargarSubpestanaOferta_DatosCme(numExpediente, adapt, request);
                    if(url != null)
                    {
                        request.setAttribute("urlPestanaDatos_oferta", url);
                    }
                }
                catch(Exception ex)
                {

                }
                try
                {
                    url = cargarSubpestanaJustificacion_DatosCme(numExpediente, adapt, request);
                    if(url != null)
                    {
                        request.setAttribute("urlPestanaDatos_justif", url);
                    }
                }
                catch(Exception ex)
                {

                }

                try
                {
                    url = cargarSubpestanaResumen_DatosCme(numExpediente, adapt, request);
                    if(url != null)
                    {
                        request.setAttribute("urlPestanaDatos_resumen", url);
                    }
                }
                catch(Exception ex)
                {

                }
            }
        }
        catch(Exception ex)
        {
            log.error("error ", ex);
        }
        return "/jsp/extension/melanbide46/datosCme.jsp";
    }
    
    //Guarda los importes de la parte superior
    public void guardarDatosCme(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response)
    {
        String codigoOperacion = "0";
        Map<String, BigDecimal> calculos = new HashMap<String, BigDecimal>();
        datosCmeVO datos = new datosCmeVO();
        try
        {
            if(validarDatosCme(request))
            {
                Integer ejercicio = null;
                try
                {
                    ejercicio = MeLanbide46Utils.getEjercicioDeExpediente(numExpediente);
                }
                catch(Exception ex)
                {

                }
                
                if(ejercicio != null)
                {
                    AdaptadorSQLBD adaptador = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
                     
                    String gestor = request.getParameter("gestor");
                    String empresa = request.getParameter("empresa");
                    String impPagado = request.getParameter("impPagado");
                    String impPagado2 = request.getParameter("impPagado2");
                    String otrasAyudasSolic = request.getParameter("otrasAyudasSolic");
                    String otrasAyudasConce = request.getParameter("otrasAyudasConce");
                    String minimisSolic = request.getParameter("minimisSolic");
                    String minimisConce = request.getParameter("minimisConce");
                    String impCon = request.getParameter("importeConcedido");
                    String impRei = request.getParameter("importeReintegrar");
                    String impJus = request.getParameter("importeJustificado");
                    String impRenRes = request.getParameter("importeRenunciadoRes");

                    try
                    {
                        boolean correcto = true;
                        BigDecimal impPagadoNum = null;
                        BigDecimal impPagadoNum2 = null;
                        BigDecimal otrasAyudasSolicNum = null;
                        BigDecimal otrasAyudasConceNum = null;
                        BigDecimal minimisSolicNum = null;
                        BigDecimal minimisConceNum = null;
                        BigDecimal importeConcedidoNum = null;
                        BigDecimal importeRenunciaResNum = null;
                        BigDecimal impReiNum = null;
                        BigDecimal impJusNum = null;
                        String ejeStr = null;
                        try
                        {
                            impPagadoNum = impPagado != null && !impPagado.equals("") ? new BigDecimal(impPagado.replaceAll(",", "\\.")) : null;
                            impPagadoNum2 = impPagado2 != null && !impPagado2.equals("") ? new BigDecimal(impPagado2.replaceAll(",", "\\.")) : null;
                            otrasAyudasSolicNum = otrasAyudasSolic != null && !otrasAyudasSolic.equals("") ? new BigDecimal(otrasAyudasSolic.replaceAll(",", "\\.")) : null;
                            otrasAyudasConceNum = otrasAyudasConce != null && !otrasAyudasConce.equals("") ? new BigDecimal(otrasAyudasConce.replaceAll(",", "\\.")) : null;
                            minimisSolicNum = minimisSolic != null && !minimisSolic.equals("") ? new BigDecimal(minimisSolic.replaceAll(",", "\\.")) : null;
                            minimisConceNum = minimisConce != null && !minimisConce.equals("") ? new BigDecimal(minimisConce.replaceAll(",", "\\.")) : null;
                            importeConcedidoNum = impCon != null && !impCon.equals("") ? new BigDecimal(impCon.replaceAll(",", "\\.")) : null;
                            importeRenunciaResNum = impRenRes != null && !impRenRes.equals("") ? new BigDecimal(impRenRes.replaceAll(",", "\\.")) : null;
                            impReiNum = impRei != null && !impRei.equals("") ? new BigDecimal(impRei.replaceAll(",", "\\.")) : null;
                            impJusNum = impJus != null && !impJus.equals("") ? new BigDecimal(impJus.replaceAll(",", "\\.")) : null;
                            ejeStr = String.valueOf(ejercicio);
                        }
                        catch(Exception ex)
                        {
                            correcto = false;
                            ex.printStackTrace();
                            codigoOperacion = "3";
                        }
                        
                        if(correcto)
                        {
                            int res = 0;
                            res = MeLanbide46Manager.getInstance().guardarDatosCme(codOrganizacion, ejeStr, numExpediente, gestor, empresa, impPagadoNum, impPagadoNum2, otrasAyudasSolicNum, otrasAyudasConceNum, minimisSolicNum, minimisConceNum, importeConcedidoNum,importeRenunciaResNum, impReiNum, impJusNum,adaptador);
                            if(res > 0)
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
                        ex.printStackTrace();
                        codigoOperacion = "1";
                    }
                    
                    calculos = MeLanbide46Manager.getInstance().cargarCalculosCme(codOrganizacion, ejercicio, numExpediente, adaptador);
                    datos = MeLanbide46Manager.getInstance().obtenerDatosCme(codOrganizacion, numExpediente,this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                }
                else
                {
                    codigoOperacion = "3";
                }
            }
            else
            {
                codigoOperacion = "4";
            }
        }
        catch(Exception ex)
        {
            codigoOperacion = "2";
        }
        
        escribirCalculosCmeRequest(codigoOperacion, calculos, datos, response);
        
//        StringBuffer xmlSalida = new StringBuffer();
//        xmlSalida.append("<RESPUESTA>");
//            xmlSalida.append("<CODIGO_OPERACION>");
//                xmlSalida.append(codigoOperacion);
//            xmlSalida.append("</CODIGO_OPERACION>");
//        xmlSalida.append("</RESPUESTA>");
//        try{
//            response.setContentType("text/xml");
//            response.setCharacterEncoding("UTF-8");
//            PrintWriter out = response.getWriter();
//            out.print(xmlSalida.toString());
//            out.flush();
//            out.close();
//        }catch(Exception e){
//            e.printStackTrace();
//        }//try-catch
    }
    
    public String cargarNuevoPuesto(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response)
    {
        cargarCombosNuevoPuesto(codOrganizacion, request);
        return "/jsp/extension/melanbide46/nuevoPuesto.jsp?codOrganizacionModulo="+codOrganizacion;
    }
    
    public String cargarModificarPuesto(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response)
    {
        try
        {
            String opcion = (String)request.getParameter("opcion");
            if(opcion != null)
            {
                String codPuesto = (String)request.getParameter("idPuesto");
                cargarCombosNuevoPuesto(codOrganizacion, request);

                if(codPuesto != null && !codPuesto.equals(""))
                {
                    Integer ejercicio = MeLanbide46Utils.getEjercicioDeExpediente(numExpediente);
                    CmePuestoVO puesto = new CmePuestoVO();
                    puesto.setCodPuesto(codPuesto);
                    puesto.setEjercicio(ejercicio);
                    puesto.setNumExp(numExpediente);
                    puesto = MeLanbide46Manager.getInstance().getPuestoPorCodigoYExpediente(puesto, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                    if(puesto != null)
                    {
                        request.setAttribute("puestoModif", puesto);
                        
                        AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
                        String desc = null;
                        if(puesto.getCodTit1() != null && !puesto.getCodTit1().equals(""))
                        {
                            desc = MeLanbide46Manager.getInstance().getDescripcionTitulacion(puesto.getCodTit1(), adapt);
                            if(desc != null)
                            {
                                request.setAttribute("descTit1", desc);
                            }
                        }
                        if(puesto.getCodTit2() != null && !puesto.getCodTit2().equals(""))
                        {
                            desc = MeLanbide46Manager.getInstance().getDescripcionTitulacion( puesto.getCodTit2(), adapt);
                            if(desc != null)
                            {
                                request.setAttribute("descTit2", desc);
                            }
                        }
                        if(puesto.getCodTit3() != null && !puesto.getCodTit3().equals(""))
                        {
                            desc = MeLanbide46Manager.getInstance().getDescripcionTitulacion( puesto.getCodTit3(), adapt);
                            if(desc != null)
                            {
                                request.setAttribute("descTit3", desc);
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
            ex.printStackTrace();
        }
        return "/jsp/extension/melanbide46/nuevoPuesto.jsp?codOrganizacionModulo="+codOrganizacion;
    }
    
    public void guardarPuesto(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response)
    {
        String codigoOperacion = "0";
        List<FilaPuestoVO> puestos = new ArrayList<FilaPuestoVO>();
        Map<String, BigDecimal> calculos = new HashMap<String, BigDecimal>();
        
       
        datosCmeVO datos = new datosCmeVO();
        try
        {
            AdaptadorSQLBD adaptador = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
            Integer ejercicio = MeLanbide46Utils.getEjercicioDeExpediente(numExpediente);
            if(ejercicio != null)
            {
                SimpleDateFormat format = new SimpleDateFormat(ConstantesMeLanbide46.FORMATO_FECHA);
                
                String codPuesto = (String)request.getParameter("idPuesto");
                String descPuesto = (String)request.getParameter("descPuesto");
                String ciudadDestino = (String)request.getParameter("ciudadDestino");
                String dpto = (String)request.getParameter("dpto");
                String pais1 = (String)request.getParameter("codPais1");
                String pais2 = (String)request.getParameter("codPais2");
                String pais3 = (String)request.getParameter("codPais3");
                String tit1 = (String)request.getParameter("codTitulacion1");
                String tit2 = (String)request.getParameter("codTitulacion2");
                String tit3 = (String)request.getParameter("codTitulacion3");
                String funciones = (String)request.getParameter("funciones");
                String idioma1 = (String)request.getParameter("codIdioma1");
                String idioma2 = (String)request.getParameter("codIdioma2");
                String idioma3 = (String)request.getParameter("codIdioma3");
                String nivelIdioma1 = (String)request.getParameter("codNivelIdioma1");
                String nivelIdioma2 = (String)request.getParameter("codNivelIdioma2");
                String nivelIdioma3 = (String)request.getParameter("codNivelIdioma3");
                String nivelFor = (String)request.getParameter("codNivelFormativo");
                String salarioAnexo1 = (String)request.getParameter("salarioAnexo1");
                String salarioAnexo2 = (String)request.getParameter("salarioAnexo2");
                String salarioAnexo3 = (String)request.getParameter("salarioAnexo3");
                String salarioAnexoTot = (String)request.getParameter("salarioAnexoTot");
                String dietasManutencion = (String)request.getParameter("dietasManutencion");
                String mesesManutencion = (String)request.getParameter("mesesManutencion");
                String tramSeguros = (String)request.getParameter("tramSeguros");
                String impSolic = (String)request.getParameter("impSolic");
                String fechaIni = (String)request.getParameter("fechaIni");
                String fechaFin = (String)request.getParameter("fechaFin");
                String mesesContrato = (String)request.getParameter("mesesContrato");
                String grupoCot = (String)request.getParameter("codGrupoCot");
                String convenioCol = (String)request.getParameter("convenioCol");
                String rangoEdad = (String)request.getParameter("rangoEdadPrevisto");
                String salarioBruto = (String)request.getParameter("salarioBruto");
                String dietasConvenio = (String)request.getParameter("dietasConvenio");
                String costeContrato = (String)request.getParameter("costeContrato");
                String otrosBenef = (String)request.getParameter("otrosBenef");
                String resultado = (String)request.getParameter("codResultado");
                String motivo = (String)request.getParameter("codMotivo");
                String observaciones = (String)request.getParameter("observaciones");
                String salarioSolicitud = (String)request.getParameter("salarioSolic");
                String dietasSolicitud = (String)request.getParameter("dietasSolic");
                String tramitacionSolicitud = (String)request.getParameter("tramitacionSolic");
                String impTotSolicitud = (String)request.getParameter("impTotSolic");

                CmePuestoVO puesto = null;
                if(codPuesto != null && !codPuesto.equals(""))
                {
                    puesto = new CmePuestoVO();
                    puesto.setCodPuesto(codPuesto);
                    puesto.setNumExp(numExpediente);
                    puesto.setEjercicio(ejercicio);
                    puesto = MeLanbide46Manager.getInstance().getPuestoPorCodigoYExpediente(puesto, adaptador);
                }
                else
                {
                    puesto = new CmePuestoVO();
                    puesto.setNumExp(numExpediente);
                    puesto.setEjercicio(ejercicio);
                }
                if(puesto == null)
                {
                    codigoOperacion = "3";
                }
                else
                {
                    BigDecimal impAnterior = puesto.getImpMaxSuv();
                    String estadoAnterior = puesto.getCodResult();
                    puesto.setCiudadDestino(ciudadDestino != null && !ciudadDestino.equals("") ? ciudadDestino.toUpperCase() : null);
                    puesto.setCodGrCot(grupoCot != null && !grupoCot.equals("") ? grupoCot.toUpperCase() : null);
                    puesto.setCodIdioma1(idioma1 != null && !idioma1.equals("") ? idioma1.toUpperCase() : null);
                    puesto.setCodIdioma2(idioma2 != null && !idioma2.equals("") ? idioma2.toUpperCase() : null);
                    puesto.setCodIdioma3(idioma3 != null && !idioma3.equals("") ? idioma3.toUpperCase() : null);
                    
                    if(resultado != null && (resultado.equalsIgnoreCase(ConstantesMeLanbide46.CODIGO_RESULTADO_DENEGADO) || resultado.equalsIgnoreCase(ConstantesMeLanbide46.CODIGO_RESULTADO_RENUNCIA)))
                    {
                        puesto.setCodMotivo(motivo != null && !motivo.equals("") ? motivo.toUpperCase() : null);
                    }
                    else
                    {
                        puesto.setCodMotivo(null);
                    }
                    puesto.setCodNivForm(nivelFor != null && !nivelFor.equals("") ? nivelFor.toUpperCase() : null);
                    puesto.setCodNivIdi1(nivelIdioma1 != null && !nivelIdioma1.equals("") ? nivelIdioma1.toUpperCase() : null);
                    puesto.setCodNivIdi2(nivelIdioma2 != null && !nivelIdioma2.equals("") ? nivelIdioma2.toUpperCase() : null);
                    puesto.setCodNivIdi3(nivelIdioma3 != null && !nivelIdioma3.equals("") ? nivelIdioma3.toUpperCase() : null);
                    puesto.setCodResult(resultado != null && !resultado.equals("") ? resultado.toUpperCase() : null);
                    puesto.setCodTit1(tit1 != null && !tit1.equals("") ? tit1.toUpperCase() : null);
                    puesto.setCodTit2(tit2 != null && !tit2.equals("") ? tit2.toUpperCase() : null);
                    puesto.setCodTit3(tit3 != null && !tit3.equals("") ? tit3.toUpperCase() : null);
                    puesto.setConvenioCol(convenioCol != null && !convenioCol.equals("") ? convenioCol.toUpperCase() : null);
                    puesto.setCosteCont(costeContrato != null && !costeContrato.equals("") ? new BigDecimal(costeContrato.replaceAll(",", "\\.")) : null);
                    puesto.setDescPuesto(descPuesto != null && !descPuesto.equals("") ? descPuesto.toUpperCase() : null);
                    puesto.setDietasConv(dietasConvenio != null && !dietasConvenio.equals("") ? new BigDecimal(dietasConvenio.replaceAll(",", "\\.")) : null);
                    puesto.setDietasManut(dietasManutencion != null && !dietasManutencion.equals("") ? new BigDecimal(dietasManutencion.replaceAll(",", "\\.")) : null);
                    puesto.setDpto(dpto != null && !dpto.equals("") ? dpto.toUpperCase() : null);
                    puesto.setFecFin(fechaFin != null && !fechaFin.equals("") ? format.parse(fechaFin) : null);
                    puesto.setFecIni(fechaIni != null && !fechaIni.equals("") ? format.parse(fechaIni) : null);
                    puesto.setMesesContrato(mesesContrato != null && !mesesContrato.equals("") ? Integer.parseInt(mesesContrato) : null);
                    puesto.setFunciones(funciones != null && !funciones.equals("") ? funciones.toUpperCase() : null);
                    puesto.setImpMaxSuv(impSolic != null && !impSolic.equals("") ? new BigDecimal(impSolic.replaceAll(",", "\\.")) : null);
                    puesto.setMesesManut(mesesManutencion != null && !mesesManutencion.equals("") ? Integer.parseInt(mesesManutencion) : null);
                    puesto.setObservaciones(observaciones != null && !observaciones.equals("") ? observaciones.toUpperCase() : null);
                    puesto.setOtrosBenef(otrosBenef != null && !otrosBenef.equals("") ? otrosBenef.toUpperCase() : null);
                    puesto.setPaiCod1(pais1 != null && !pais1.equals("") ? Integer.parseInt(pais1) : null);
                    puesto.setPaiCod2(pais2 != null && !pais2.equals("") ? Integer.parseInt(pais2) : null);
                    puesto.setPaiCod3(pais3 != null && !pais3.equals("") ? Integer.parseInt(pais3) : null);
                    puesto.setRangoEdadPrevisto(rangoEdad != null && !rangoEdad.equals("") ? Integer.parseInt(rangoEdad) : null);
                    puesto.setSalBruto(salarioBruto != null && !salarioBruto.equals("") ? new BigDecimal(salarioBruto.replaceAll(",", "\\.")) : null);
                    puesto.setSalarioAnex1(salarioAnexo1 != null && !salarioAnexo1.equals("") ? new BigDecimal(salarioAnexo1.replaceAll(",", "\\.")) : null);
                    puesto.setSalarioAnex2(salarioAnexo2 != null && !salarioAnexo2.equals("") ? new BigDecimal(salarioAnexo2.replaceAll(",", "\\.")) : null);
                    puesto.setSalarioAnex3(salarioAnexo3 != null && !salarioAnexo3.equals("") ? new BigDecimal(salarioAnexo3.replaceAll(",", "\\.")) : null);
                    puesto.setSalarioAnexTot(salarioAnexoTot != null && !salarioAnexoTot.equals("") ? new BigDecimal(salarioAnexoTot.replaceAll(",", "\\.")) : null);
                    puesto.setTramSeguros(tramSeguros != null && !tramSeguros.equals("") ? new BigDecimal(tramSeguros.replaceAll(",", "\\.")) : null);
                    
                    
                    puesto.setSalSolic(salarioSolicitud != null && !salarioSolicitud.equals("") ? new BigDecimal(salarioSolicitud.replaceAll(",", "\\.")) : null);
                    puesto.setDietasSolic(dietasSolicitud != null && !dietasSolicitud.equals("") ? new BigDecimal(dietasSolicitud.replaceAll(",", "\\.")) : null);
                    puesto.setTramSolic(tramitacionSolicitud != null && !tramitacionSolicitud.equals("") ? new BigDecimal(tramitacionSolicitud.replaceAll(",", "\\.")) : null);
                    puesto.setImpTotSolic(impTotSolicitud != null && !impTotSolicitud.equals("") ? new BigDecimal(impTotSolicitud.replaceAll(",", "\\.")) : null);
                    
                    
                    //TODO: se podria validar los datos del puesto
                    
                    
                    MeLanbide46Manager.getInstance().guardarCmePuestoVO(codOrganizacion, puesto, impAnterior, estadoAnterior, adaptador);
                }
                calculos = MeLanbide46Manager.getInstance().cargarCalculosCme(codOrganizacion, ejercicio, numExpediente, adaptador);
                
                datos = MeLanbide46Manager.getInstance().obtenerDatosCme(codOrganizacion,numExpediente,adaptador);
                puestos = MeLanbide46Manager.getInstance().getListaPuestosPorExpediente(numExpediente, adaptador);
            }
            else
            {
                codigoOperacion = "3";
            }
        }
        catch(Exception ex)
        {
            codigoOperacion = "2";
            ex.printStackTrace();
        }
        escribirListaPuestosRequest(codigoOperacion, puestos, calculos, datos, response);
    }
    
    public void eliminarPuesto(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response)
    {
        String codigoOperacion = "0";
        List<FilaPuestoVO> puestos = new ArrayList<FilaPuestoVO>();
        Map<String, BigDecimal> calculos = new HashMap<String, BigDecimal>();
        
        datosCmeVO datos = new datosCmeVO();
        try
        {
            AdaptadorSQLBD adaptador = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
            Integer ejercicio = MeLanbide46Utils.getEjercicioDeExpediente(numExpediente);
            if(ejercicio != null)
            {
                
                String codPuesto = (String)request.getParameter("idPuesto");

                CmePuestoVO puesto = null;
                if(codPuesto != null && !codPuesto.equals(""))
                {
                    puesto = new CmePuestoVO();
                    puesto.setCodPuesto(codPuesto);
                    puesto.setNumExp(numExpediente);
                    puesto.setEjercicio(ejercicio);
                    puesto = MeLanbide46Manager.getInstance().getPuestoPorCodigoYExpediente(puesto, adaptador);
                }
                if(puesto == null)
                {
                    codigoOperacion = "3";
                }
                else
                {
                    MeLanbide46Manager.getInstance().eliminarCmePuestoVO(codOrganizacion, puesto, adaptador);
                }
                calculos = MeLanbide46Manager.getInstance().cargarCalculosCme(codOrganizacion, ejercicio, numExpediente, adaptador);
                datos = MeLanbide46Manager.getInstance().obtenerDatosCme(codOrganizacion,numExpediente,adaptador);
                puestos = MeLanbide46Manager.getInstance().getListaPuestosPorExpediente(numExpediente, adaptador);
            }
            else
            {
                codigoOperacion = "3";
            }
        }
        catch(Exception ex)
        {
            codigoOperacion = "2";
            ex.printStackTrace();
        }
        escribirListaPuestosRequest(codigoOperacion, puestos, calculos, datos, response);
    }
    
    public String cargarNuevaOferta(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response)
    {
        cargarCombosNuevaOferta(codOrganizacion, request);
        return "/jsp/extension/melanbide46/nuevaOferta.jsp?codOrganizacionModulo="+codOrganizacion;
    }
    
    public String cargarModificarOferta(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response)
    {
        try
        {
            String opcion = (String)request.getParameter("opcion");
            if(opcion != null)
            {
                String codOferta = (String)request.getParameter("idOferta");
                String codPuesto = (String)request.getParameter("idPuesto");
                cargarCombosNuevaOferta(codOrganizacion, request);

                if(codOferta != null && !codOferta.equals(""))
                {
                    Integer ejercicio = MeLanbide46Utils.getEjercicioDeExpediente(numExpediente);
                    CmeOfertaVO oferta = new CmeOfertaVO();
                    oferta.setIdOferta(Integer.parseInt(codOferta));
                    oferta.setCodPuesto(codPuesto);
                    oferta.setExpEje(ejercicio);
                    oferta.setNumExp(numExpediente);
                    oferta = MeLanbide46Manager.getInstance().getOfertaPorCodigoPuestoYExpediente(oferta, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                    if(oferta != null)
                    {
                         //pruebas copiardatos
                       /* String copiardatos = (String)request.getParameter("copiardatos");
                        CmePuestoVO origenpuesto = new CmePuestoVO();                
                        origenpuesto.setCodPuesto(codPuesto);
                        origenpuesto.setEjercicio(ejercicio);
                        origenpuesto.setNumExp(numExpediente);
                        origenpuesto = MeLanbide46Manager.getInstance().getPuestoPorCodigoYExpediente(origenpuesto, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                                              
                        if (copiardatos!=null)       
                                oferta = MeLanbide46Manager.getInstance().copiarDatosPuesto(codOrganizacion, origenpuesto, oferta, this.getAdaptSQLBD(String.valueOf(codOrganizacion))); 
                        //fin copiardatos
                        */
                        
                        request.setAttribute("ofertaModif", oferta);
                        if(oferta.getCodPuesto() != null && !oferta.getCodPuesto().equals(""))
                        {
                            CmePuestoVO puesto = new CmePuestoVO();
                            puesto.setCodPuesto(oferta.getCodPuesto());
                            puesto.setEjercicio(ejercicio);
                            puesto.setNumExp(numExpediente);
                            puesto = MeLanbide46Manager.getInstance().getPuestoPorCodigoYExpediente(puesto, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                            if(puesto != null)
                            {
                                request.setAttribute("puestoConsulta", puesto);
                            }
                        }
                        if(oferta.getIdOfertaOrigen() != null)
                        {
                            CmeOfertaVO ofertaOrigen = new CmeOfertaVO();
                            ofertaOrigen.setIdOferta(oferta.getIdOfertaOrigen());
                            ofertaOrigen.setExpEje(ejercicio);
                            ofertaOrigen.setNumExp(numExpediente);
                            ofertaOrigen = MeLanbide46Manager.getInstance().getOfertaPorCodigoPuestoYExpediente(ofertaOrigen, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                            if(ofertaOrigen != null)
                            {
                                request.setAttribute("ofertaOrigen", ofertaOrigen);
                            }
                        }
                        
                        AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
                        String desc = null;
                        if(oferta.getCodTit1() != null && !oferta.getCodTit1().equals(""))
                        {
                            desc = MeLanbide46Manager.getInstance().getDescripcionTitulacion(oferta.getCodTit1(), adapt);
                            if(desc != null)
                            {
                                request.setAttribute("descTit1", desc);
                            }
                        }
                        if(oferta.getCodTit2() != null && !oferta.getCodTit2().equals(""))
                        {
                            desc = MeLanbide46Manager.getInstance().getDescripcionTitulacion( oferta.getCodTit2(), adapt);
                            if(desc != null)
                            {
                                request.setAttribute("descTit2", desc);
                            }
                        }
                        if(oferta.getCodTit3() != null && !oferta.getCodTit3().equals(""))
                        {
                            desc = MeLanbide46Manager.getInstance().getDescripcionTitulacion( oferta.getCodTit3(), adapt);
                            if(desc != null)
                            {
                                request.setAttribute("descTit3", desc);
                            }
                        }
                        if(oferta.getCodTitulacion() != null && !oferta.getCodTitulacion().equals(""))
                        {
                            desc = MeLanbide46Manager.getInstance().getDescripcionTitulacion( oferta.getCodTitulacion(), adapt);
                            if(desc != null)
                            {
                                request.setAttribute("descTitContratado", desc);
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
            ex.printStackTrace();
        }
        return "/jsp/extension/melanbide46/nuevaOferta.jsp?codOrganizacionModulo="+codOrganizacion;
    }
    
    public void getListaOfertasNoDenegadasPorExpediente(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response)
    {
        String codigoOperacion = "0";
        datosCmeVO datos = new datosCmeVO();
        try
        {
            //Al guardar una oferta, se inserta este mensaje en sesion como resultado
            //Justo despues de guardar se llama a este metodo para recargar la tabla.
            //Este es el punto donde se borra este mensaje de sesion, para no mantenerlo en memoria innecesariamente.
            codigoOperacion = request.getSession().getAttribute("mensajeImportar") != null ? (String)request.getSession().getAttribute("mensajeImportar") : "0";
            request.getSession().removeAttribute("mensajeImportar");
        }
        catch(Exception ex)
        {

        }
        List<FilaOfertaVO> ofertas = new ArrayList<FilaOfertaVO>();
        Map<String, BigDecimal> calculos = new HashMap<String, BigDecimal>();
        try
        {
            Integer ejercicio = MeLanbide46Utils.getEjercicioDeExpediente(numExpediente);
            if(ejercicio != null)
            {
                AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
                ofertas = MeLanbide46Manager.getInstance().getListaOfertasNoDenegadasPorExpediente(numExpediente, adapt);
                calculos = MeLanbide46Manager.getInstance().cargarCalculosCme(codOrganizacion, ejercicio, numExpediente, adapt);
                
                datos = MeLanbide46Manager.getInstance().obtenerDatosCme(codOrganizacion,numExpediente,adapt);
            }
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        
        escribirListaOfertasRequest(codigoOperacion, ofertas, calculos, datos, response);
    }
    
    public void getListaJustificacionesNoDenegadasPorExpediente(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response)
    {
        String codigoOperacion = "0";
        datosCmeVO datos = new datosCmeVO();
        try
        {
            //Al guardar una oferta, se inserta este mensaje en sesion como resultado
            //Justo despues de guardar se llama a este metodo para recargar la tabla.
            //Este es el punto donde se borra este mensaje de sesion, para no mantenerlo en memoria innecesariamente.
            codigoOperacion = request.getSession().getAttribute("mensajeImportar") != null ? (String)request.getSession().getAttribute("mensajeImportar") : "0";
            request.getSession().removeAttribute("mensajeImportar");
        }
        catch(Exception ex)
        {

        }
        List<FilaJustificacionVO> justificaciones = new ArrayList<FilaJustificacionVO>();
        Map<String, BigDecimal> calculos = new HashMap<String, BigDecimal>();
        try
        {
            Integer ejercicio = MeLanbide46Utils.getEjercicioDeExpediente(numExpediente);
            if(ejercicio != null)
            {
                AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
                justificaciones = MeLanbide46Manager.getInstance().getListaJustificacionesNoDenegadasPorExpediente(numExpediente, adapt);
                calculos = MeLanbide46Manager.getInstance().cargarCalculosCme(codOrganizacion, ejercicio, numExpediente, adapt);
                datos = MeLanbide46Manager.getInstance().obtenerDatosCme(codOrganizacion,numExpediente,adapt);
            }
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        
        escribirListaJustificacionesRequest(codigoOperacion, justificaciones, calculos, datos, response);
    }
    
    public String cargarNuevaJustificacion(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response)
    {
        try
        {
            cargarCombosNuevaJustificacion(codOrganizacion, request);
            cargarImportesJustificacion(numExpediente, codOrganizacion, null, request);
        }
        catch(Exception ex)
        {
            
        }
        return "/jsp/extension/melanbide46/nuevaJustificacion.jsp?codOrganizacionModulo="+codOrganizacion;
    }
    
    public String cargarModificarJustificacion(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response)
    {
        cargarCombosNuevaJustificacion(codOrganizacion, request);
        try
        {
            String opcion = (String)request.getParameter("opcion");
            if(opcion != null)
            {
                String codPuesto = (String)request.getParameter("idPuesto");
                cargarCombosNuevaOferta(codOrganizacion, request);
                
                AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
                Integer ejercicio = MeLanbide46Utils.getEjercicioDeExpediente(numExpediente);
                CmePuestoVO puesto = null;
                if(codPuesto != null && !codPuesto.equals(""))
                {
                    puesto = new CmePuestoVO();
                    puesto.setCodPuesto(codPuesto);
                    puesto.setEjercicio(ejercicio);
                    puesto.setNumExp(numExpediente);
                    puesto = MeLanbide46Manager.getInstance().getPuestoPorCodigoYExpediente(puesto, adapt);
                    if(puesto != null)
                    {
                        request.setAttribute("puestoConsulta", puesto);
                        
                        List<FilaPersonaContratadaVO> contrataciones = MeLanbide46Manager.getInstance().getListaContratacionesPuesto(puesto, adapt);
                        if(contrataciones != null)
                        {
                            request.setAttribute("listaContrataciones", contrataciones);
                        }
                    }
                    CmeOfertaVO ofertaConsulta = new CmeOfertaVO();
                    ofertaConsulta.setExpEje(ejercicio);
                    ofertaConsulta.setCodPuesto(codPuesto);
                    ofertaConsulta.setNumExp(numExpediente);
                    ofertaConsulta = MeLanbide46Manager.getInstance().getUltimaOfertaPorPuestoYExpediente(ofertaConsulta, adapt);
                    if(ofertaConsulta != null)
                    {
                        request.setAttribute("ofertaConsulta", ofertaConsulta);
                        
                        String desc = null;
                        if(ofertaConsulta.getCodTit1() != null && !ofertaConsulta.getCodTit1().equals(""))
                        {
                            desc = MeLanbide46Manager.getInstance().getDescripcionTitulacion( ofertaConsulta.getCodTit1(), adapt);
                            if(desc != null)
                            {
                                request.setAttribute("descTit1", desc);
                            }
                        }
                        if(ofertaConsulta.getCodTit2() != null && !ofertaConsulta.getCodTit2().equals(""))
                        {
                            desc = MeLanbide46Manager.getInstance().getDescripcionTitulacion( ofertaConsulta.getCodTit2(), adapt);
                            if(desc != null)
                            {
                                request.setAttribute("descTit2", desc);
                            }
                        }
                        if(ofertaConsulta.getCodTit3() != null && !ofertaConsulta.getCodTit3().equals(""))
                        {
                            desc = MeLanbide46Manager.getInstance().getDescripcionTitulacion( ofertaConsulta.getCodTit3(), adapt);
                            if(desc != null)
                            {
                                request.setAttribute("descTit3", desc);
                            }
                        }
                    }
                }
                cargarImportesJustificacion(numExpediente, codOrganizacion, puesto, request);
                if(opcion.equalsIgnoreCase("consultar"))
                {
                    request.setAttribute("consulta", true);
                }
            }
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return "/jsp/extension/melanbide46/nuevaJustificacion.jsp?codOrganizacionModulo="+codOrganizacion;
    }
    
    public void generarInformeResumenPuestosContratados(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response)
    {
        String codigoOperacion = "0";
        String rutaArchivoSalida = null;
        try
        {
            Integer ano = Integer.parseInt(request.getParameter("ano"));
            AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
            
            int idioma = 1;
            HttpSession session = request.getSession();
            UsuarioValueObject usuario = new UsuarioValueObject();
            try
            {
                if (session != null) 
                {
                    usuario = (UsuarioValueObject) session.getAttribute("usuario");
                    if (usuario != null) 
                    {
                        idioma = usuario.getIdioma();
                    }
                }
            }
            catch(Exception ex)
            {

            }
            
            MeLanbide46I18n meLanbide46I18n = MeLanbide46I18n.getInstance();
            MeLanbide46Manager manager = MeLanbide46Manager.getInstance();
            SimpleDateFormat format = new SimpleDateFormat(ConstantesMeLanbide46.FORMATO_FECHA);
            
            try 
            {
                HSSFWorkbook libro = new HSSFWorkbook();
            
                //se escribe el fichero
                DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
                //get current date time with Date()
                Date date = new Date();
                String fechaAct=dateFormat.format(date);
           
                File informe= new File("resumenPuestosContratados_"+fechaAct+".xls");

                FileOutputStream archivoSalida = new FileOutputStream(informe);
                    
                HSSFPalette palette = libro.getCustomPalette();
                HSSFColor hssfColor = null;
                try 
                {
                    hssfColor= palette.findColor((byte)75, (byte)149, (byte)211); 
                    if (hssfColor == null )
                    {
                        hssfColor = palette.getColor(HSSFColor.ROYAL_BLUE.index);
                    }
                }
                catch (Exception e) 
                {
                    e.printStackTrace();
                }
                HSSFFont negrita = libro.createFont();
                negrita.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
                List<FilaInformeResumenPuestosContratadosVO> listaPuestos = null;
                
                HSSFFont normal = libro.createFont();
                normal.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
                normal.setFontHeight((short)150);
            
                HSSFFont negritaTitulo = libro.createFont();
                negritaTitulo.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
                negritaTitulo.setColor(HSSFColor.WHITE.index);
                negritaTitulo.setFontHeight((short)200);
            
                HSSFSheet hoja = null;
                int numFila = 0;
                int numFila2 = 0;

                HSSFRow fila = null;
                HSSFCell celda = null;
                HSSFCellStyle estiloCelda = null;
                
                HSSFRow fila2 = null;
                HSSFCell celda2 = null;
                HSSFCellStyle estilocelda2 = null;

                hoja = libro.createSheet();
                HSSFSheet hoja2 = libro.createSheet("sin formato");
                
                //Se establece el ancho de cada columnas
                hoja.setColumnWidth(0, 6000);//Num. expediente
                hoja.setColumnWidth(1, 10000);//empresa
                hoja.setColumnWidth(2, 8000);//territorio historico
                hoja.setColumnWidth(3, 5000);//puestos solic
                hoja.setColumnWidth(4, 10000);//puesto
                hoja.setColumnWidth(5, 8000);//pais solicitud
                hoja.setColumnWidth(6, 12000);//titulacion
                hoja.setColumnWidth(7, 8000);//idiomas
                hoja.setColumnWidth(8, 6000);//resultado
                hoja.setColumnWidth(9, 4000);//puestos denegados
                hoja.setColumnWidth(10, 6000);//puestos ren antes res
                hoja.setColumnWidth(11, 6000);//motivo
                hoja.setColumnWidth(12, 3600);//oferta
                hoja.setColumnWidth(13, 4500);//pers precandidatas
                hoja.setColumnWidth(14, 3000);//difusion
                hoja.setColumnWidth(15, 5000);//fecha envio pers candidatas
                hoja.setColumnWidth(16, 5000);//num pers candidatas
                hoja.setColumnWidth(17, 5000);//num. puestos ren antes primer pago
                hoja.setColumnWidth(18, 5000);//Causa renuncia
                hoja.setColumnWidth(19, 10000);//persona seleccionada
                hoja.setColumnWidth(20, 5000);//fecha nacimiento
                hoja.setColumnWidth(21, 5000);//edad fecha contrato
                hoja.setColumnWidth(22, 2500);//sexo
                hoja.setColumnWidth(23, 8000);//pais contratacion
                hoja.setColumnWidth(24, 8000);//titulacion pers candidata
                hoja.setColumnWidth(25, 6000);//nivel formativo
                hoja.setColumnWidth(26, 5000);//fecha contratacion
                hoja.setColumnWidth(27, 5000);//fin contrato
                hoja.setColumnWidth(28, 4000);//tiempo contratacion
                hoja.setColumnWidth(29, 5000);//num contratos realizados
                
                hoja2.setColumnWidth(0, 6000);//Num. expediente
                hoja2.setColumnWidth(1, 10000);//empresa
                hoja2.setColumnWidth(2, 8000);//territorio historico
                hoja2.setColumnWidth(3, 5000);//puestos solic
                hoja2.setColumnWidth(4, 10000);//puesto
                hoja2.setColumnWidth(5, 8000);//pais solicitud
                hoja2.setColumnWidth(6, 12000);//titulacion
                hoja2.setColumnWidth(7, 8000);//idiomas
                hoja2.setColumnWidth(8, 6000);//resultado
                hoja2.setColumnWidth(9, 4000);//puestos denegados
                hoja2.setColumnWidth(10, 6000);//puestos ren antes res
                hoja2.setColumnWidth(11, 6000);//motivo
                hoja2.setColumnWidth(12, 3600);//oferta
                hoja2.setColumnWidth(13, 4500);//pers precandidatas
                hoja2.setColumnWidth(14, 3000);//difusion
                hoja2.setColumnWidth(15, 5000);//fecha envio pers candidatas
                hoja2.setColumnWidth(16, 5000);//num pers candidatas
                hoja2.setColumnWidth(17, 5000);//num. puestos ren antes primer pago
                hoja2.setColumnWidth(18, 5000);//Causa renuncia
                hoja2.setColumnWidth(19, 10000);//persona seleccionada
                hoja2.setColumnWidth(20, 5000);//fecha nacimiento
                hoja2.setColumnWidth(21, 5000);//edad fecha contrato
                hoja2.setColumnWidth(22, 2500);//sexo
                hoja2.setColumnWidth(23, 8000);//pais contratacion
                hoja2.setColumnWidth(24, 8000);//titulacion pers candidata
                hoja2.setColumnWidth(25, 6000);//nivel formativo
                hoja2.setColumnWidth(26, 5000);//fecha contratacion
                hoja2.setColumnWidth(27, 5000);//fin contrato
                hoja2.setColumnWidth(28, 4000);//tiempo contratacion
                hoja2.setColumnWidth(29, 5000);//num contratos realizados
                //hoja.setColumnWidth(27, 5000);//motivo renuncia
                
                
                fila = hoja.createRow(numFila);
                fila2 = hoja2.createRow(numFila2);

                //Se añaden los titulos de las columnas
                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(0);
                celda.setCellValue(meLanbide46I18n.getMensaje(idioma, "doc.informePuestosContratados.col1").toUpperCase());
                celda.setCellStyle(estiloCelda);

                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(1);
                celda.setCellValue(meLanbide46I18n.getMensaje(idioma, "doc.informePuestosContratados.col2").toUpperCase());
                celda.setCellStyle(estiloCelda);

                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(2);
                celda.setCellValue(meLanbide46I18n.getMensaje(idioma, "doc.informePuestosContratados.col31").toUpperCase());
                celda.setCellStyle(estiloCelda);

                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(3);
                celda.setCellValue(meLanbide46I18n.getMensaje(idioma, "doc.informePuestosContratados.col3").toUpperCase());
                celda.setCellStyle(estiloCelda);

                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(4);
                celda.setCellValue(meLanbide46I18n.getMensaje(idioma, "doc.informePuestosContratados.col4").toUpperCase());
                celda.setCellStyle(estiloCelda);

                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(5);
                celda.setCellValue(meLanbide46I18n.getMensaje(idioma, "doc.informePuestosContratados.col5").toUpperCase());
                celda.setCellStyle(estiloCelda);

                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(6);
                celda.setCellValue(meLanbide46I18n.getMensaje(idioma, "doc.informePuestosContratados.col6").toUpperCase());
                celda.setCellStyle(estiloCelda);

                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(7);
                celda.setCellValue(meLanbide46I18n.getMensaje(idioma, "doc.informePuestosContratados.col7").toUpperCase());
                celda.setCellStyle(estiloCelda);

                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(8);
                celda.setCellValue(meLanbide46I18n.getMensaje(idioma, "doc.informePuestosContratados.col8").toUpperCase());
                celda.setCellStyle(estiloCelda);

                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(9);
                celda.setCellValue(meLanbide46I18n.getMensaje(idioma, "doc.informePuestosContratados.col9").toUpperCase());
                celda.setCellStyle(estiloCelda);

                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(10);
                celda.setCellValue(meLanbide46I18n.getMensaje(idioma, "doc.informePuestosContratados.col10").toUpperCase());
                celda.setCellStyle(estiloCelda);

                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(11);
                celda.setCellValue(meLanbide46I18n.getMensaje(idioma, "doc.informePuestosContratados.col11").toUpperCase());
                celda.setCellStyle(estiloCelda);

                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(12);
                celda.setCellValue(meLanbide46I18n.getMensaje(idioma, "doc.informePuestosContratados.col12").toUpperCase());
                celda.setCellStyle(estiloCelda);

                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(13);
                celda.setCellValue(meLanbide46I18n.getMensaje(idioma, "doc.informePuestosContratados.col13").toUpperCase());
                celda.setCellStyle(estiloCelda);

                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(14);
                celda.setCellValue(meLanbide46I18n.getMensaje(idioma, "doc.informePuestosContratados.col14").toUpperCase());
                celda.setCellStyle(estiloCelda);

                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(15);
                celda.setCellValue(meLanbide46I18n.getMensaje(idioma, "doc.informePuestosContratados.col15").toUpperCase());
                celda.setCellStyle(estiloCelda);

                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(16);
                celda.setCellValue(meLanbide46I18n.getMensaje(idioma, "doc.informePuestosContratados.col16").toUpperCase());
                celda.setCellStyle(estiloCelda);

                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderRight(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(17);
                celda.setCellValue(meLanbide46I18n.getMensaje(idioma, "doc.informePuestosContratados.col29").toUpperCase());
                celda.setCellStyle(estiloCelda);

                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(18);
                celda.setCellValue(meLanbide46I18n.getMensaje(idioma, "doc.informePuestosContratados.col30").toUpperCase());
                celda.setCellStyle(estiloCelda);

                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(19);
                celda.setCellValue(meLanbide46I18n.getMensaje(idioma, "doc.informePuestosContratados.col17").toUpperCase());
                celda.setCellStyle(estiloCelda);

                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(20);
                celda.setCellValue(meLanbide46I18n.getMensaje(idioma, "doc.informePuestosContratados.col18").toUpperCase());
                celda.setCellStyle(estiloCelda);

                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(21);
                celda.setCellValue(meLanbide46I18n.getMensaje(idioma, "doc.informePuestosContratados.col19").toUpperCase());
                celda.setCellStyle(estiloCelda);

                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(22);
                celda.setCellValue(meLanbide46I18n.getMensaje(idioma, "doc.informePuestosContratados.col20").toUpperCase());
                celda.setCellStyle(estiloCelda);

                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(23);
                celda.setCellValue(meLanbide46I18n.getMensaje(idioma, "doc.informePuestosContratados.col21").toUpperCase());
                celda.setCellStyle(estiloCelda);

                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(24);
                celda.setCellValue(meLanbide46I18n.getMensaje(idioma, "doc.informePuestosContratados.col22").toUpperCase());
                celda.setCellStyle(estiloCelda);

                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(25);
                celda.setCellValue(meLanbide46I18n.getMensaje(idioma, "doc.informePuestosContratados.col23").toUpperCase());
                celda.setCellStyle(estiloCelda);

                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(26);
                celda.setCellValue(meLanbide46I18n.getMensaje(idioma, "doc.informePuestosContratados.col24").toUpperCase());
                celda.setCellStyle(estiloCelda);

                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(27);
                celda.setCellValue(meLanbide46I18n.getMensaje(idioma, "doc.informePuestosContratados.col25").toUpperCase());
                celda.setCellStyle(estiloCelda);

                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(28);
                celda.setCellValue(meLanbide46I18n.getMensaje(idioma, "doc.informePuestosContratados.col26").toUpperCase());
                celda.setCellStyle(estiloCelda);

                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderRight(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(29);
                celda.setCellValue(meLanbide46I18n.getMensaje(idioma, "doc.informePuestosContratados.col27").toUpperCase());
                celda.setCellStyle(estiloCelda);

                /*estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(27);
                celda.setCellValue(meLanbide46I18n.getMensaje(idioma, "doc.informePuestosContratados.col28").toUpperCase());
                celda.setCellStyle(estiloCelda);*/
                
                crearEstiloHoja2(libro, fila2, celda2, estilocelda2, idioma);
                
                
                //Estilos celdas datos
                    //celdas primera columna -> 2 Estilos
                HSSFCellStyle estiloCeldaColumnaPrimeraFilaPrimera = libro.createCellStyle();
                estiloCeldaColumnaPrimeraFilaPrimera.setWrapText(true);
                estiloCeldaColumnaPrimeraFilaPrimera.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCeldaColumnaPrimeraFilaPrimera.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCeldaColumnaPrimeraFilaPrimera.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCeldaColumnaPrimeraFilaPrimera.setFont(normal);                     

                HSSFCellStyle estiloCeldaColumnaPrimeraFilaIntermedia = libro.createCellStyle();
                estiloCeldaColumnaPrimeraFilaIntermedia.setWrapText(true);
                estiloCeldaColumnaPrimeraFilaIntermedia.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCeldaColumnaPrimeraFilaIntermedia.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCeldaColumnaPrimeraFilaIntermedia.setFont(normal); 

                    //celdas segunda y tercera columna (antes de totales) -> 3 Estilos
                HSSFCellStyle estiloCeldaColumnasDosFilaPrimera = libro.createCellStyle();
                estiloCeldaColumnasDosFilaPrimera.setWrapText(true);
                estiloCeldaColumnasDosFilaPrimera.setBorderLeft(HSSFCellStyle.BORDER_THIN);
                estiloCeldaColumnasDosFilaPrimera.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCeldaColumnasDosFilaPrimera.setFont(normal);
                estiloCeldaColumnasDosFilaPrimera.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);

                HSSFCellStyle estiloCeldaColumnasDosFilaIntermedia = libro.createCellStyle();
                estiloCeldaColumnasDosFilaIntermedia.setWrapText(true);
                estiloCeldaColumnasDosFilaIntermedia.setBorderLeft(HSSFCellStyle.BORDER_THIN);                    
                estiloCeldaColumnasDosFilaIntermedia.setFont(normal);
                estiloCeldaColumnasDosFilaIntermedia.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);

                    //celdas siguientes Texto (pueden tener totales) ->  3 Estilos
                HSSFCellStyle estiloCeldaTextoFilaPrimera = libro.createCellStyle();
                estiloCeldaTextoFilaPrimera.setWrapText(true);
                estiloCeldaTextoFilaPrimera.setBorderLeft(HSSFCellStyle.BORDER_THIN);
                estiloCeldaTextoFilaPrimera.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCeldaTextoFilaPrimera.setFont(normal);
                estiloCeldaTextoFilaPrimera.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);

                HSSFCellStyle estiloCeldaTextoFilaIntermedia = libro.createCellStyle();
                estiloCeldaTextoFilaIntermedia.setWrapText(true);
                estiloCeldaTextoFilaIntermedia.setBorderLeft(HSSFCellStyle.BORDER_THIN);
                estiloCeldaTextoFilaIntermedia.setBorderTop(HSSFCellStyle.BORDER_THIN);
                estiloCeldaTextoFilaIntermedia.setFont(normal);
                estiloCeldaTextoFilaIntermedia.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);

                 //celdas siguientes Numericos (pueden tener totales) ->  3 Estilos
                HSSFCellStyle estiloCeldaNumericoFilaPrimera = libro.createCellStyle();
                estiloCeldaNumericoFilaPrimera.setWrapText(true);
                estiloCeldaNumericoFilaPrimera.setBorderLeft(HSSFCellStyle.BORDER_THIN);
                estiloCeldaNumericoFilaPrimera.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCeldaNumericoFilaPrimera.setFont(normal);
                estiloCeldaNumericoFilaPrimera.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
                estiloCeldaNumericoFilaPrimera.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);

                HSSFCellStyle estiloCeldaNumericoFilaIntermedia = libro.createCellStyle();
                estiloCeldaNumericoFilaIntermedia.setWrapText(true);
                estiloCeldaNumericoFilaIntermedia.setBorderLeft(HSSFCellStyle.BORDER_THIN);
                estiloCeldaNumericoFilaIntermedia.setBorderTop(HSSFCellStyle.BORDER_THIN);
                estiloCeldaNumericoFilaIntermedia.setFont(normal);
                estiloCeldaNumericoFilaIntermedia.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
                estiloCeldaNumericoFilaIntermedia.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);

                //celdas ultima columna -> 3 Estilos
                HSSFCellStyle estiloCeldaColumnaUltimaFilaPrimera = libro.createCellStyle();
                estiloCeldaColumnaUltimaFilaPrimera.setWrapText(true);
                estiloCeldaColumnaUltimaFilaPrimera.setBorderLeft(HSSFCellStyle.BORDER_THIN);
                estiloCeldaColumnaUltimaFilaPrimera.setBorderRight(HSSFCellStyle.BORDER_THICK);                                          
                estiloCeldaColumnaUltimaFilaPrimera.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCeldaColumnaUltimaFilaPrimera.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCeldaColumnaUltimaFilaPrimera.setFont(normal); 
                estiloCeldaColumnaUltimaFilaPrimera.setAlignment(HSSFCellStyle.ALIGN_RIGHT);

                HSSFCellStyle estiloCeldaColumnaUltimaFilaIntermedia = libro.createCellStyle();
                estiloCeldaColumnaUltimaFilaIntermedia.setWrapText(true);
                estiloCeldaColumnaUltimaFilaIntermedia.setBorderLeft(HSSFCellStyle.BORDER_THIN);
                estiloCeldaColumnaUltimaFilaIntermedia.setBorderRight(HSSFCellStyle.BORDER_THICK);
                estiloCeldaColumnaUltimaFilaIntermedia.setBorderTop(HSSFCellStyle.BORDER_THIN);
                estiloCeldaColumnaUltimaFilaIntermedia.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCeldaColumnaUltimaFilaIntermedia.setFont(normal);
                estiloCeldaColumnaUltimaFilaIntermedia.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
                
                boolean nuevo = true;
                HashMap<String, String> datosTercero = null;
                String empresa = null;
                String idiomas = null;
                String pais = null;
                String titulacion = null;
                int p = 0;
                int n = 0;
                int numIdiomas = 0;
                int numPaises = 0;
                int numTitulaciones = 0;
                int height = 0;
                
                //Sumatorios parciales
                int totalPuestosSolic = 0;
                int totalContRealizados = 0;
                int totalPuestosDenegados = 0;
                int totalPuestosRenAntesRes = 0;
                int totalPersCandidatas = 0;
                int totalPuestosRenAntesPag1 = 0;
                
                
                String numExp = null;
                listaPuestos = manager.getDatosInformeResumenPuestosContratados(ano, adapt);
                
                int maxLengthNombre = 0;
    
                //Insertamos los datos, fila a fila
                for(FilaInformeResumenPuestosContratadosVO filaI : listaPuestos)
                {
                    if(filaI.getNumExpediente() != null && !filaI.getNumExpediente().equals(numExp))
                    {
                        if(numExp != null && !numExp.equals(""))
                        {
                            //se añade el sumatorio parcial
                            numFila++;
                            fila = hoja.createRow(numFila);                            
                            
                            anadirSumatoriosParcialesResumenPuestos(idioma, libro, fila, normal, totalPuestosSolic, totalContRealizados, totalPuestosDenegados, totalPuestosRenAntesRes, totalPersCandidatas, totalPuestosRenAntesPag1);
                            
                            //Reinicio los contadores
                            totalPuestosSolic = 0;
                            totalContRealizados = 0;
                            totalPuestosDenegados = 0;
                            totalPuestosRenAntesRes = 0;
                            totalPersCandidatas = 0;
                            totalPuestosRenAntesPag1 = 0;
                            
                        }
                        
                        //se añaden los merged region
                        //if(p != 0)
                        //{
                        if (numFila >1){
                            /*//POI ALPHA
                            hoja.addMergedRegion(new Region(numFila-p-1, 0, numFila, 0));
                            hoja.addMergedRegion(new Region(numFila-p-1, 1, numFila, 1));
                            hoja.addMergedRegion(new Region(numFila-p-1, 2, numFila-1, 2));*/
                             //poi 3.10
                            hoja.addMergedRegion(new CellRangeAddress(numFila-p-1, numFila, 0, 0));
                             hoja.addMergedRegion(new CellRangeAddress(numFila-p-1, numFila, 1, 1));
                             hoja.addMergedRegion(new CellRangeAddress(numFila-p-1, numFila-1, 2, 2));

                        }
                        numExp = filaI.getNumExpediente();
                        nuevo = true;
                        p = 0;
                        n++;
                        numFila++;
                        //numFila2++;
                    }
                    else
                    {
                        numFila++;    
                        //numFila2++;
                        nuevo = false;
                        p++;
                    }
                    
                    fila = hoja.createRow(numFila);
                    //fila2 = hoja2.createRow(numFila2);
                    
                    numIdiomas = 0;
                    numPaises = 0;
                    numTitulaciones = 0;
                    
                    if(filaI.getIdioma1() != null)
                    {
                        numIdiomas++;
                    }
                    if(filaI.getIdioma2() != null)
                    {
                        numIdiomas++;
                    }
                    if(filaI.getIdioma3() != null)
                    {
                        numIdiomas++;
                    }

                    if(filaI.getTitulacion1() != null)
                    {
                        numTitulaciones++;
                        //En cada linea caben 42 caracteres, por tanto si tiene entre 42 y 84 caracteres seran dos lineas, entre 85 y 127 tres lineas, etc.
                        numTitulaciones += (filaI.getTitulacion1().length() / 42);
                    }
                    if(filaI.getTitulacion2() != null)
                    {
                        numTitulaciones++;
                        //En cada linea caben 42 caracteres, por tanto si tiene entre 42 y 84 caracteres seran dos lineas, entre 85 y 127 tres lineas, etc.
                        numTitulaciones += (filaI.getTitulacion2().length() / 42);
                    }
                    if(filaI.getTitulacion3() != null)
                    {
                        numTitulaciones++;
                        //En cada linea caben 42 caracteres, por tanto si tiene entre 42 y 84 caracteres seran dos lineas, entre 85 y 127 tres lineas, etc.
                        numTitulaciones += (filaI.getTitulacion3().length() / 42);
                    }

                    if(filaI.getPaisSol1() != null)
                    {
                        numPaises++;
                    }
                    if(filaI.getPaisSol2() != null)
                    {
                        numPaises++;
                    }
                    if(filaI.getPaisSol3() != null)
                    {
                        numPaises++;
                    }

                    height = 0;
                    height = Math.max(numIdiomas, numTitulaciones);
                    height = Math.max(height, numPaises);
                    height = Math.max(height, 1);
                    height = height * fila.getHeight();

                    fila.setHeight((short)height);
                    fila2.setHeight((short)height);

                    //COLUMNA: NUM_EXPEDIENTE
                    celda = fila.createCell(0);
                    if(nuevo)
                    {
                        celda.setCellStyle(estiloCeldaColumnaPrimeraFilaPrimera);                            
                    } 
                    else 
                    {
                        celda.setCellStyle(estiloCeldaColumnaPrimeraFilaIntermedia);
                    }

                    if(nuevo)
                    {
                        celda.setCellValue(numExp != null ? numExp.toUpperCase() : "");
                    }

                    //COLUMNA: EMPRESA
                    if(filaI.getEmpresa() != null)
                    {
                        empresa = filaI.getEmpresa();
                    }
                    else
                    {
                        datosTercero = MeLanbide46Manager.getInstance().getDatosTercero(codOrganizacion, numExp, String.valueOf(ano), ConstantesMeLanbide46.CODIGO_ROL_ENTIDAD_SOLICITANTE, adapt);
                        if(datosTercero.containsKey("TER_NOC"))
                        {
                            empresa = datosTercero.get("TER_NOC");
                        }
                        else
                        {
                            empresa = "";
                        }
                    }
                    celda = fila.createCell(1);
                    if(nuevo)
                    {
                        celda.setCellStyle(estiloCeldaColumnasDosFilaPrimera);                            
                    } 
                    else 
                    {
                        celda.setCellStyle(estiloCeldaColumnasDosFilaIntermedia);
                    }

                    if(nuevo)
                    {
                        celda.setCellValue(empresa != null ? empresa.toUpperCase() : "");
                    }

                    //COLUMNA: TERRITORIO HISTORICO
                    celda = fila.createCell(2);
                    if(nuevo)
                    {
                        celda.setCellStyle(estiloCeldaTextoFilaPrimera);                            
                    } 
                    else
                    {
                        celda.setCellStyle(estiloCeldaTextoFilaIntermedia);
                    }

                    if(nuevo)
                    {
                        celda.setCellValue(filaI.getTerritorioHistorico() != null ? filaI.getTerritorioHistorico().toUpperCase() : "");
                    }  

                    //COLUMNA: PUESTOS SOLICITADOS
                    celda = fila.createCell(3);
                    if(nuevo)
                    {
                        celda.setCellStyle(estiloCeldaNumericoFilaPrimera);                            
                    } 
                    else //if(numFila == listaPuestos.size())
                    {
                        celda.setCellStyle(estiloCeldaNumericoFilaIntermedia);
                    }

                    celda.setCellValue(filaI.getPuestosSolic() != null ? filaI.getPuestosSolic() : 0);
                    totalPuestosSolic += filaI.getPuestosSolic() != null ? filaI.getPuestosSolic() : 0;
                    celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                    

                    //COLUMNA: PUESTO
                    celda = fila.createCell(4);
                    if(nuevo)
                    {
                        celda.setCellStyle(estiloCeldaTextoFilaPrimera);
                    }
                    else                         
                    {
                        celda.setCellStyle(estiloCeldaTextoFilaIntermedia);
                    }

                    celda.setCellValue(filaI.getDescPuesto() != null ? filaI.getCodPuesto()+"-"+filaI.getDescPuesto().toUpperCase() : "");

                    //COLUMNA: PAIS_SOLICITUD
                    pais = filaI.getPaisSol1() != null ? filaI.getPaisSol1().toUpperCase() : "";
                    pais += filaI.getPaisSol2() != null && !filaI.getPaisSol2().equals("") ? (pais != null && !pais.equals("") ? System.getProperty("line.separator") : "") : "";
                    pais += filaI.getPaisSol2() != null && !filaI.getPaisSol2().equals("") ? filaI.getPaisSol2().toUpperCase() : "";
                    pais += filaI.getPaisSol3() != null && !filaI.getPaisSol3().equals("") ? (pais != null && !pais.equals("") ? System.getProperty("line.separator") : "") : "";
                    pais += filaI.getPaisSol3() != null && !filaI.getPaisSol3().equals("") ? filaI.getPaisSol3().toUpperCase() : "";

                    celda = fila.createCell(5);
                    if(nuevo)
                    {
                        celda.setCellStyle(estiloCeldaTextoFilaPrimera);
                    }
                    else
                    {
                        celda.setCellStyle(estiloCeldaTextoFilaIntermedia);
                    }                        

                    celda.setCellValue(pais != null ? pais.toString() : "");                   

                    //COLUMNA: TITULACION
                    titulacion = filaI.getTitulacion1() != null ? filaI.getTitulacion1().toUpperCase() : "";
                    titulacion += filaI.getTitulacion2() != null && !filaI.getTitulacion2().equals("") ? (titulacion != null && !titulacion.equals("") ? System.getProperty("line.separator") : "") : "";
                    titulacion += filaI.getTitulacion2() != null && !filaI.getTitulacion2().equals("") ? filaI.getTitulacion2().toUpperCase() : "";
                    titulacion += filaI.getTitulacion3() != null && !filaI.getTitulacion3().equals("") ? (titulacion != null && !titulacion.equals("") ? System.getProperty("line.separator") : "") : "";
                    titulacion += filaI.getTitulacion3() != null && !filaI.getTitulacion3().equals("") ? filaI.getTitulacion3().toUpperCase() : "";
                    
                    celda = fila.createCell(6);
                    if(nuevo)
                    {
                        celda.setCellStyle(estiloCeldaTextoFilaPrimera);
                    }
                    else
                    {
                        celda.setCellStyle(estiloCeldaTextoFilaIntermedia);
                    }

                    celda.setCellValue(titulacion != null ? titulacion.toUpperCase() : "");

                    //COLUMNA: IDIOMAS
                    idiomas = filaI.getIdioma1() != null ? filaI.getIdioma1() : "";
                    idiomas += filaI.getNivelIdioma1() != null && !filaI.getNivelIdioma1().equals("") ? (idiomas != null && !idiomas.equals("") ? " " : "") : "";
                    idiomas += filaI.getNivelIdioma1() != null ? filaI.getNivelIdioma1() : "";

                    idiomas += filaI.getIdioma2() != null && !filaI.getIdioma2().equals("") ? (idiomas != null && !idiomas.equals("") ? System.getProperty("line.separator") : "")+filaI.getIdioma2() : "";
                    idiomas += filaI.getNivelIdioma2() != null && !filaI.getNivelIdioma2().equals("") ? (idiomas != null && !idiomas.equals("") ? " " : "") : "";
                    idiomas += filaI.getNivelIdioma2() != null ? filaI.getNivelIdioma2() : "";

                    idiomas += filaI.getIdioma3() != null && !filaI.getIdioma3().equals("") ? (idiomas != null && !idiomas.equals("") ? System.getProperty("line.separator") : "")+filaI.getIdioma3() : "";
                    idiomas += filaI.getNivelIdioma3() != null && !filaI.getNivelIdioma3().equals("") ? (idiomas != null && !idiomas.equals("") ? " " : "") : "";
                    idiomas += filaI.getNivelIdioma3() != null ? filaI.getNivelIdioma3() : "";


                    celda = fila.createCell(7);
                    if(nuevo)
                    {
                        celda.setCellStyle(estiloCeldaTextoFilaPrimera);
                    }
                    else
                    {
                        celda.setCellStyle(estiloCeldaTextoFilaIntermedia);
                    }                       

                    celda.setCellValue(idiomas != null ? idiomas.toUpperCase() : "");

                    //COLUMNA: RESULTADO
                    celda = fila.createCell(8);
                    if(nuevo)
                    {
                        celda.setCellStyle(estiloCeldaTextoFilaPrimera);
                    }
                    else
                    {
                        celda.setCellStyle(estiloCeldaTextoFilaIntermedia);
                    }

                    celda.setCellValue(filaI.getResultado() != null ? filaI.getResultado().toUpperCase() : "");

                    //COLUMNA: PUESTOS_DENEGADOS
                    celda = fila.createCell(9);
                    if(nuevo)
                    {
                        celda.setCellStyle(estiloCeldaNumericoFilaPrimera);
                    }
                    else
                    {
                        celda.setCellStyle(estiloCeldaNumericoFilaIntermedia);
                    }

                    celda.setCellValue(filaI.getPuestosDen() != null ? filaI.getPuestosDen() : 0);
                    totalPuestosDenegados += filaI.getPuestosDen() != null ? filaI.getPuestosDen() : 0;
                    celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);

                    //COLUMNA: PUESTOS_REN_ANTES_RES
                    celda = fila.createCell(10);
                    if(nuevo)
                    {
                        celda.setCellStyle(estiloCeldaNumericoFilaPrimera);
                    }
                    else
                    {
                        celda.setCellStyle(estiloCeldaNumericoFilaIntermedia);
                    }
                    celda.setCellValue(filaI.getPuestosRenAntesRes() != null ? filaI.getPuestosRenAntesRes() : 0);
                    totalPuestosRenAntesRes += filaI.getPuestosRenAntesRes() != null ? filaI.getPuestosRenAntesRes() : 0;                        
                    celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);

                    //COLUMNA: MOTIVO
                    celda = fila.createCell(11);                        
                    if(nuevo)
                    {
                        celda.setCellStyle(estiloCeldaTextoFilaPrimera);                           
                    }
                    else
                    {
                        celda.setCellStyle(estiloCeldaTextoFilaIntermedia);   
                    }

                    celda.setCellValue(filaI.getMotivo() != null ? filaI.getMotivo() : "");
                    
                    //COLUMNA: OFERTA
                    celda = fila.createCell(12);
                    if(nuevo)
                    {
                        celda.setCellStyle(estiloCeldaTextoFilaPrimera);                            
                    }
                    else
                    {
                        celda.setCellStyle(estiloCeldaTextoFilaIntermedia);                            
                    }                        

                    celda.setCellValue(filaI.getOferta() != null ? filaI.getOferta() : "");
                    
                    //COLUMNA: PERSONAS PRECANDIDATAS
                    celda = fila.createCell(13);
                    if(nuevo)
                    {
                        celda.setCellStyle(estiloCeldaTextoFilaPrimera);   
                    }
                    else
                    {
                        celda.setCellStyle(estiloCeldaTextoFilaIntermedia);   
                    } 
                    celda.setCellValue(filaI.getPersPrecandidatas() != null ? filaI.getPersPrecandidatas().toUpperCase() : "");

                    //COLUMNA: DIFUSION
                    celda = fila.createCell(14);
                    if(nuevo)
                    {
                        celda.setCellStyle(estiloCeldaTextoFilaPrimera);
                    }
                    else
                    {
                        celda.setCellStyle(estiloCeldaTextoFilaIntermedia);
                    }

                    celda.setCellValue(filaI.getDifusion() != null ? filaI.getDifusion().toUpperCase() : "");

                    //COLUMNA: FECHA_ENV_PERS_CANDIDATAS
                    celda = fila.createCell(15);
                    if(nuevo)
                    {
                        celda.setCellStyle(estiloCeldaTextoFilaPrimera);
                    }
                    else
                    {
                        celda.setCellStyle(estiloCeldaTextoFilaIntermedia);
                    }
                    celda.setCellValue(filaI.getFecEnvPersPrecandidatas() != null ? filaI.getFecEnvPersPrecandidatas() : "");

                    //COLUMNA: NUMERO_CANDIDATOS
                    celda = fila.createCell(16);
                    if(nuevo)
                    {
                        celda.setCellStyle(estiloCeldaNumericoFilaPrimera);
                    }
                    else
                    {
                        celda.setCellStyle(estiloCeldaNumericoFilaIntermedia);
                    }

                    celda.setCellValue(filaI.getNumCandidatos() != null ? filaI.getNumCandidatos() : 0);
                    totalPersCandidatas += filaI.getNumCandidatos() != null ? filaI.getNumCandidatos() : 0;
                    celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);

                    //COLUMNA: PUESTOS_REN_ANTES_1PAGO
                    celda = fila.createCell(17);
                    if(nuevo)
                    {
                        celda.setCellStyle(estiloCeldaNumericoFilaPrimera);   
                    }
                    else
                    {
                        celda.setCellStyle(estiloCeldaNumericoFilaIntermedia);   
                    }
                    celda.setCellValue(filaI.getPuestosRenAntPrimPago() != null ? filaI.getPuestosRenAntPrimPago() : 0);
                    totalPuestosRenAntesPag1 += filaI.getPuestosRenAntPrimPago() != null ? filaI.getPuestosRenAntPrimPago() : 0;
                    celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);

                    //COLUMNA: CAUSA RENUNCIA
                    celda = fila.createCell(18);
                    if(nuevo)
                    {
                        celda.setCellStyle(estiloCeldaTextoFilaPrimera);
                    }
                    else
                    {
                        celda.setCellStyle(estiloCeldaTextoFilaIntermedia);
                    }
                    celda.setCellValue(filaI.getCausaRenuncia() != null ? filaI.getCausaRenuncia().toUpperCase() : "");   

                    //COLUMNA: PERSONA SELECCIONADA
                    String nomApe = filaI.getNifSelec() != null ? filaI.getNifSelec() : "";
                    
                    boolean hayMasDatos = false;
                    
                    if(filaI.getNomSelec() != null && !filaI.getNomSelec().equals(""))
                    {
                        hayMasDatos = true;
                    }
                    else if(filaI.getApe1Selec() != null && !filaI.getApe1Selec().equals(""))
                    {
                        hayMasDatos = true;
                    }
                    else if(filaI.getApe2Selec() != null && !filaI.getApe2Selec().equals(""))
                    {
                        hayMasDatos = true;
                    }
                    if(hayMasDatos)
                    {
                        nomApe += " - ";
                        //nomApe = filaI.getNomSelec() != null ? filaI.getNomSelec() : "";
                        nomApe += filaI.getNomSelec() != null && !filaI.getNomSelec().equals("") ? (nomApe != null && !nomApe.equals("") ? " " : "") : "";
                        nomApe += filaI.getNomSelec() != null ? filaI.getNomSelec() : "";

                        nomApe += filaI.getApe1Selec() != null && !filaI.getApe1Selec().equals("") ? (nomApe != null && !nomApe.equals("") ? " " : "") : "";
                        nomApe += filaI.getApe1Selec() != null ? filaI.getApe1Selec() : "";

                        nomApe += filaI.getApe2Selec() != null && !filaI.getApe2Selec().equals("") ? (nomApe != null && !nomApe.equals("") ? " " : "") : "";
                        nomApe += filaI.getApe2Selec() != null ? filaI.getApe2Selec() : "";
                    }
                    
                    if(nomApe.length() > maxLengthNombre)
                    {
                        maxLengthNombre = nomApe.length();
                    }

                    celda = fila.createCell(19);
                    if(nuevo)
                    {
                        celda.setCellStyle(estiloCeldaTextoFilaPrimera);
                    }
                    else
                    {
                        celda.setCellStyle(estiloCeldaTextoFilaIntermedia);
                    }
                    celda.setCellValue(nomApe != null ? nomApe.toUpperCase() : "");

                    //COLUMNA: FECHA_NACIMIENTO
                    celda = fila.createCell(20);
                    if(nuevo)
                    {
                        celda.setCellStyle(estiloCeldaTextoFilaPrimera);
                    }
                    else
                    {
                        celda.setCellStyle(estiloCeldaTextoFilaIntermedia);
                    }
                    celda.setCellValue(filaI.getFecNac() != null ? filaI.getFecNac() : "");

                    //COLUMNA: EDAD
                    Integer edad = MeLanbide46Utils.calcularEdad2(filaI.getFecNac() != null ? format.parse(filaI.getFecNac()) : null, filaI.getFecIni() != null ? format.parse(filaI.getFecIni()) : null);
                    
                    celda = fila.createCell(21);    
                    if(nuevo)
                    {
                        celda.setCellStyle(estiloCeldaNumericoFilaPrimera);
                    }
                    else
                    {
                        celda.setCellStyle(estiloCeldaNumericoFilaIntermedia);
                    }                        
                    celda.setCellValue(edad!= null ? edad : 0);                        
                    celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);

                    //COLUMNA: SEXO
                    celda = fila.createCell(22);
                    if(nuevo)
                    {
                        celda.setCellStyle(estiloCeldaTextoFilaPrimera);
                    }
                    else
                    {    
                        celda.setCellStyle(estiloCeldaTextoFilaIntermedia);
                    }
                    celda.setCellValue(filaI.getSexo() != null ? filaI.getSexo().toUpperCase() : "");

                    //COLUMNA: PAIS_CONTRATACION
                    
                    String paisCont = filaI.getPaisCont1() != null ? filaI.getPaisCont1().toUpperCase() : "";
                    paisCont += filaI.getPaisCont2() != null && !filaI.getPaisCont2().equals("") ? (paisCont != null && !paisCont.equals("") ? System.getProperty("line.separator") : "") : "";
                    paisCont += filaI.getPaisCont2() != null && !filaI.getPaisCont2().equals("") ? filaI.getPaisCont2().toUpperCase() : "";
                    paisCont += filaI.getPaisCont3() != null && !filaI.getPaisCont3().equals("") ? (paisCont != null && !paisCont.equals("") ? System.getProperty("line.separator") : "") : "";
                    paisCont += filaI.getPaisCont3() != null && !filaI.getPaisCont3().equals("") ? filaI.getPaisCont3().toUpperCase() : "";
                    
                    celda = fila.createCell(23);    
                    if(nuevo)
                    {
                        celda.setCellStyle(estiloCeldaTextoFilaPrimera);
                    }
                    else
                    {
                        celda.setCellStyle(estiloCeldaTextoFilaIntermedia);
                    }
                    celda.setCellValue(paisCont != null ? paisCont.toUpperCase() : "");

                    //COLUMNA: TITULACION_PERS_CANDIDATA
                    celda = fila.createCell(24);
                    if(nuevo)
                    {
                        celda.setCellStyle(estiloCeldaTextoFilaPrimera);
                    }
                    else
                    {
                        celda.setCellStyle(estiloCeldaTextoFilaIntermedia);
                    }
                    celda.setCellValue(filaI.getTitulacionSelec() != null ? filaI.getTitulacionSelec().toUpperCase() : "");
                    
                    //COLUMNA: NIVEL FORMATIVO
                    celda = fila.createCell(25);
                    if(nuevo)
                    {
                        celda.setCellStyle(estiloCeldaTextoFilaPrimera);
                    }
                    else
                    {
                        celda.setCellStyle(estiloCeldaTextoFilaIntermedia);
                    }                        
                    celda.setCellValue(filaI.getNivelFormativo() != null ? filaI.getNivelFormativo().toUpperCase() : "");
                    
                    //COLUMNA: FECHA_INICIO
                    celda = fila.createCell(26);
                    if(nuevo)
                    {
                        celda.setCellStyle(estiloCeldaTextoFilaPrimera);
                    }
                    else
                    {
                        celda.setCellStyle(estiloCeldaTextoFilaIntermedia);
                    }
                    celda.setCellValue(filaI.getFecIni() != null ? filaI.getFecIni() : "");
                    
                    //COLUMNA: FECHA_FIN
                    celda = fila.createCell(27);
                    if(nuevo)
                    {
                        celda.setCellStyle(estiloCeldaTextoFilaPrimera);
                    }
                    else
                    {
                        celda.setCellStyle(estiloCeldaTextoFilaIntermedia);
                    }                        
                    celda.setCellValue(filaI.getFecFin() != null ? filaI.getFecFin() : ""); 

                    //COLUMNA: TIEMPO CONTRATACION
                    celda = fila.createCell(28);
                    if(nuevo)
                    {
                        celda.setCellStyle(estiloCeldaNumericoFilaPrimera);
                    }
                    else
                    {
                        celda.setCellStyle(estiloCeldaNumericoFilaIntermedia);
                    }                        
                    celda.setCellValue(filaI.getTiempoContratacion() != null ? filaI.getTiempoContratacion() : 0);                        
                    celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);

                    //COLUMNA: PUESTOS CONTRATADOS
                    celda = fila.createCell(29);
                    if(nuevo)
                    {
                        celda.setCellStyle(estiloCeldaColumnaUltimaFilaPrimera);
                    }
                    else
                    {
                        celda.setCellStyle(estiloCeldaColumnaUltimaFilaIntermedia);
                    }                                                
                    celda.setCellValue(filaI.getPuestosCont() != null ? filaI.getPuestosCont() : 0);
                    totalContRealizados += filaI.getPuestosCont() != null ? filaI.getPuestosCont() : 0;                        
                    celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);

                    //COLUMNA: MOTIVO_RENUNCIA
                    /*estiloCelda = libro.createCellStyle();
                    estiloCelda.setWrapText(true);
                    estiloCelda.setFont(normal);
                    estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THIN);
                    if(nuevo)
                    {
                        if(n == 1)
                        {
                            estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                        }
                        else
                        {
                            //estiloCelda.setBorderTop(HSSFCellStyle.BORDER_DOUBLE);
                            estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                        }
                    }
                    else
                    {
                        estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THIN);
                    }
                    if(numFila == listaPuestos.size())
                    {
                        estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                    }
                    celda = fila.createCell(27);
                    celda.setCellValue(filaI.getMotivoRenuncia() != null ? filaI.getMotivoRenuncia() : "");
                    celda.setCellStyle(estiloCelda);*/
                    numFila2++;
                    fila2 = hoja2.createRow(numFila2);
                    crearDatosHoja2(libro, fila2, celda2, estilocelda2, idioma, filaI,numExp, numFila2, nuevo, n, listaPuestos, datosTercero,  normal, codOrganizacion, ano,  adapt,  format);
                }
                
                hoja.setColumnWidth(16, (short)(maxLengthNombre*400));
                hoja2.setColumnWidth(16, (short)(maxLengthNombre*400));
                
                //Añade sumatorios a la ultima fila
                numFila++;
                fila = hoja.createRow(numFila);       
                anadirSumatoriosParcialesResumenPuestos(idioma, libro, fila, normal, totalPuestosSolic, totalContRealizados, totalPuestosDenegados, totalPuestosRenAntesRes, totalPersCandidatas, totalPuestosRenAntesPag1);
                
                //Esto añade un merged region a la ultima fila, columnas de num expediente, entidad y territorio historico
                //if(p != 0)
                //{  
                        //poi alpha
                    /*hoja.addMergedRegion(new Region(numFila-p-1, 0, numFila, 0));
                    hoja.addMergedRegion(new Region(numFila-p-1, 1, numFila, 1));
                    hoja.addMergedRegion(new Region(numFila-p-1, 2, numFila-1, 2));*/
                    // poi 3.10
                    hoja.addMergedRegion(new CellRangeAddress(numFila-p-1,  numFila, 0, 0));
                    hoja.addMergedRegion(new CellRangeAddress(numFila-p-1,  numFila, 1, 1));
                    hoja.addMergedRegion(new CellRangeAddress(numFila-p-1,  numFila-1, 2,2));

                    /*estiloCelda = libro.createCellStyle();
                    estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                    estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                    estiloCelda.setWrapText(true);
                    estiloCelda.setFont(normal);
                    
                    hoja.getRow(numFila-p).getCell(0).setCellStyle(estiloCelda);
                    
                    estiloCelda = libro.createCellStyle();
                    estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                    estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THIN);
                    estiloCelda.setWrapText(true);
                    estiloCelda.setFont(normal);
                    
                    hoja.getRow(numFila-p).getCell(1).setCellStyle(estiloCelda);*/
                    
                    /*estiloCelda = libro.createCellStyle();
                    estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                    estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THIN);
                    estiloCelda.setWrapText(true);
                    estiloCelda.setFont(normal);
                    
                    hoja.getRow(numFila-p).getCell(2).setCellStyle(estiloCelda);*/
                //}
                
                
                /*File directorioTemp = new File(m_Conf.getString("PDF.base_dir"));
                File informe = File.createTempFile("resumenPuestosContratados", ".xls", directorioTemp);

                FileOutputStream archivoSalida = new FileOutputStream(informe);*/
                libro.write(archivoSalida);
                archivoSalida.close();

                rutaArchivoSalida = informe.getAbsolutePath();
                
                FileInputStream istr = new FileInputStream(rutaArchivoSalida);

                BufferedInputStream bstr = new BufferedInputStream( istr ); // promote

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
            } 
            catch (Exception ioe) 
            {
                log.debug("EXCEPCION informe resumenPuestosContratados");
                ioe.printStackTrace();
                
            }
        }
        catch(Exception ex)
        {
            log.debug("EXCEPCION informe resumenPuestosContratados");
            ex.printStackTrace();
            
        }
    }    
    
    private void crearDatosHoja2(HSSFWorkbook libro, HSSFRow fila, HSSFCell celda, HSSFCellStyle estiloCelda, int idioma, FilaInformeResumenPuestosContratadosVO filaI,
    String numExp, int numFila, boolean nuevo, int n, List<FilaInformeResumenPuestosContratadosVO> listaPuestos, HashMap<String, String> datosTercero,
    HSSFFont normal, int codOrganizacion, int ano, AdaptadorSQLBD adapt, SimpleDateFormat format)
    {
        try
        {
            int numIdiomas = 0;
            int numPaises = 0;
            int numTitulaciones = 0;
            int height = 0;
            String empresa = null;
            String idiomas = null;
            String pais = null;
            String titulacion = null;
            int maxLengthNombre = 0;

            //Sumatorios parciales
            int totalPuestosSolic = 0;
            int totalContRealizados = 0;
            int totalPuestosDenegados = 0;
            int totalPuestosRenAntesRes = 0;
            int totalPersCandidatas = 0;
            int totalPuestosRenAntesPag1 = 0;
            
            
            estiloCelda = libro.createCellStyle();
            estiloCelda.setWrapText(true);
            estiloCelda.setFont(normal);
            
            height = 0;
            height = Math.max(numIdiomas, numTitulaciones);
            height = Math.max(height, numPaises);
            height = Math.max(height, 1);
            height = height * fila.getHeight();

            fila.setHeight((short)height);

            //COLUMNA: NUM_EXPEDIENTE
            
            celda = fila.createCell(0);
            celda.setCellValue(numExp != null ? numExp.toUpperCase() : "");
            celda.setCellStyle(estiloCelda);

            //COLUMNA: EMPRESA
            if(filaI.getEmpresa() != null)
            {
                empresa = filaI.getEmpresa();
            }
            else
            {
                datosTercero = MeLanbide46Manager.getInstance().getDatosTercero(codOrganizacion, numExp, String.valueOf(ano), ConstantesMeLanbide46.CODIGO_ROL_ENTIDAD_SOLICITANTE, adapt);
                if(datosTercero.containsKey("TER_NOC"))
                {
                    empresa = datosTercero.get("TER_NOC");
                }
                else
                {
                    empresa = "";
                }
            }
            celda = fila.createCell(1);
            celda.setCellValue(empresa != null ? empresa.toUpperCase() : "");
            celda.setCellStyle(estiloCelda);

            //COLUMNA: TERRITORIO HISTORICO
            celda = fila.createCell(2);
            celda.setCellValue(filaI.getTerritorioHistorico() != null ? filaI.getTerritorioHistorico().toUpperCase() : "");
            celda.setCellStyle(estiloCelda);

            //COLUMNA: PUESTOS SOLICITADOS
            celda = fila.createCell(3);
            celda.setCellValue(filaI.getPuestosSolic() != null ? filaI.getPuestosSolic() : 0);
            totalPuestosSolic += filaI.getPuestosSolic() != null ? filaI.getPuestosSolic() : 0;
            celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
            celda.setCellStyle(estiloCelda);

            //COLUMNA: PUESTO
            celda = fila.createCell(4);
            celda.setCellValue(filaI.getDescPuesto() != null ? filaI.getCodPuesto()+"-"+filaI.getDescPuesto().toUpperCase() : "");
            celda.setCellStyle(estiloCelda);

            //COLUMNA: PAIS_SOLICITUD
            pais = filaI.getPaisSol1() != null ? filaI.getPaisSol1().toUpperCase() : "";
            pais += filaI.getPaisSol2() != null && !filaI.getPaisSol2().equals("") ? (pais != null && !pais.equals("") ? System.getProperty("line.separator") : "") : "";
            pais += filaI.getPaisSol2() != null && !filaI.getPaisSol2().equals("") ? filaI.getPaisSol2().toUpperCase() : "";
            pais += filaI.getPaisSol3() != null && !filaI.getPaisSol3().equals("") ? (pais != null && !pais.equals("") ? System.getProperty("line.separator") : "") : "";
            pais += filaI.getPaisSol3() != null && !filaI.getPaisSol3().equals("") ? filaI.getPaisSol3().toUpperCase() : "";


            celda = fila.createCell(5);
            celda.setCellValue(pais != null ? pais.toString() : "");
            celda.setCellStyle(estiloCelda);

            //COLUMNA: TITULACION
            titulacion = filaI.getTitulacion1() != null ? filaI.getTitulacion1().toUpperCase() : "";
            titulacion += filaI.getTitulacion2() != null && !filaI.getTitulacion2().equals("") ? (titulacion != null && !titulacion.equals("") ? System.getProperty("line.separator") : "") : "";
            titulacion += filaI.getTitulacion2() != null && !filaI.getTitulacion2().equals("") ? filaI.getTitulacion2().toUpperCase() : "";
            titulacion += filaI.getTitulacion3() != null && !filaI.getTitulacion3().equals("") ? (titulacion != null && !titulacion.equals("") ? System.getProperty("line.separator") : "") : "";
            titulacion += filaI.getTitulacion3() != null && !filaI.getTitulacion3().equals("") ? filaI.getTitulacion3().toUpperCase() : "";

            celda = fila.createCell(6);
            celda.setCellValue(titulacion != null ? titulacion.toUpperCase() : "");
            celda.setCellStyle(estiloCelda);

            //COLUMNA: IDIOMAS
            idiomas = filaI.getIdioma1() != null ? filaI.getIdioma1() : "";
            idiomas += filaI.getNivelIdioma1() != null && !filaI.getNivelIdioma1().equals("") ? (idiomas != null && !idiomas.equals("") ? " " : "") : "";
            idiomas += filaI.getNivelIdioma1() != null ? filaI.getNivelIdioma1() : "";

            idiomas += filaI.getIdioma2() != null && !filaI.getIdioma2().equals("") ? (idiomas != null && !idiomas.equals("") ? System.getProperty("line.separator") : "")+filaI.getIdioma2() : "";
            idiomas += filaI.getNivelIdioma2() != null && !filaI.getNivelIdioma2().equals("") ? (idiomas != null && !idiomas.equals("") ? " " : "") : "";
            idiomas += filaI.getNivelIdioma2() != null ? filaI.getNivelIdioma2() : "";

            idiomas += filaI.getIdioma3() != null && !filaI.getIdioma3().equals("") ? (idiomas != null && !idiomas.equals("") ? System.getProperty("line.separator") : "")+filaI.getIdioma3() : "";
            idiomas += filaI.getNivelIdioma3() != null && !filaI.getNivelIdioma3().equals("") ? (idiomas != null && !idiomas.equals("") ? " " : "") : "";
            idiomas += filaI.getNivelIdioma3() != null ? filaI.getNivelIdioma3() : "";

            celda = fila.createCell(7);
            celda.setCellValue(idiomas != null ? idiomas.toUpperCase() : "");
            celda.setCellStyle(estiloCelda);

            //COLUMNA: RESULTADO
            celda = fila.createCell(8);
            celda.setCellValue(filaI.getResultado() != null ? filaI.getResultado().toUpperCase() : "");
            celda.setCellStyle(estiloCelda);

            //COLUMNA: PUESTOS_DENEGADOS
            celda = fila.createCell(9);
            celda.setCellValue(filaI.getPuestosDen() != null ? filaI.getPuestosDen() : 0);
            totalPuestosDenegados += filaI.getPuestosDen() != null ? filaI.getPuestosDen() : 0;
            celda.setCellStyle(estiloCelda);
            celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);

            //COLUMNA: PUESTOS_REN_ANTES_RES
            celda = fila.createCell(10);
            celda.setCellValue(filaI.getPuestosRenAntesRes() != null ? filaI.getPuestosRenAntesRes() : 0);
            totalPuestosRenAntesRes += filaI.getPuestosRenAntesRes() != null ? filaI.getPuestosRenAntesRes() : 0;
            celda.setCellStyle(estiloCelda);
            celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);

            //COLUMNA: MOTIVO
            celda = fila.createCell(11);
            celda.setCellValue(filaI.getMotivo() != null ? filaI.getMotivo() : "");
            celda.setCellStyle(estiloCelda);

            //COLUMNA: OFERTA
            celda = fila.createCell(12);
            celda.setCellValue(filaI.getOferta() != null ? filaI.getOferta() : "");
            celda.setCellStyle(estiloCelda);

            //COLUMNA: PERSONAS PRECANDIDATAS
            celda = fila.createCell(13);
            celda.setCellValue(filaI.getPersPrecandidatas() != null ? filaI.getPersPrecandidatas().toUpperCase() : "");
            celda.setCellStyle(estiloCelda);

            //COLUMNA: DIFUSION
            celda = fila.createCell(14);
            celda.setCellValue(filaI.getDifusion() != null ? filaI.getDifusion().toUpperCase() : "");
            celda.setCellStyle(estiloCelda);

            //COLUMNA: FECHA_ENV_PERS_CANDIDATAS
            celda = fila.createCell(15);
            celda.setCellValue(filaI.getFecEnvPersPrecandidatas() != null ? filaI.getFecEnvPersPrecandidatas() : "");
            celda.setCellStyle(estiloCelda);

            //COLUMNA: NUMERO_CANDIDATOS
            celda = fila.createCell(16);
            celda.setCellValue(filaI.getNumCandidatos() != null ? filaI.getNumCandidatos() : 0);
            totalPersCandidatas += filaI.getNumCandidatos() != null ? filaI.getNumCandidatos() : 0;
            celda.setCellStyle(estiloCelda);
            celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);

            //COLUMNA: PUESTOS_REN_ANTES_1PAGO
            celda = fila.createCell(17);
            celda.setCellValue(filaI.getPuestosRenAntPrimPago() != null ? filaI.getPuestosRenAntPrimPago() : 0);
            totalPuestosRenAntesPag1 += filaI.getPuestosRenAntPrimPago() != null ? filaI.getPuestosRenAntPrimPago() : 0;
            celda.setCellStyle(estiloCelda);
            celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);

            //COLUMNA: CAUSA RENUNCIA
            celda = fila.createCell(18);
            celda.setCellValue(filaI.getCausaRenuncia() != null ? filaI.getCausaRenuncia().toUpperCase() : "");
            celda.setCellStyle(estiloCelda);

            //COLUMNA: PERSONA SELECCIONADA
            String nomApe = filaI.getNifSelec() != null ? filaI.getNifSelec() : "";

            boolean hayMasDatos = false;

            if(filaI.getNomSelec() != null && !filaI.getNomSelec().equals(""))
            {
                hayMasDatos = true;
            }
            else if(filaI.getApe1Selec() != null && !filaI.getApe1Selec().equals(""))
            {
                hayMasDatos = true;
            }
            else if(filaI.getApe2Selec() != null && !filaI.getApe2Selec().equals(""))
            {
                hayMasDatos = true;
            }
            if(hayMasDatos)
            {
                nomApe += " - ";
                //nomApe = filaI.getNomSelec() != null ? filaI.getNomSelec() : "";
                nomApe += filaI.getNomSelec() != null && !filaI.getNomSelec().equals("") ? (nomApe != null && !nomApe.equals("") ? " " : "") : "";
                nomApe += filaI.getNomSelec() != null ? filaI.getNomSelec() : "";

                nomApe += filaI.getApe1Selec() != null && !filaI.getApe1Selec().equals("") ? (nomApe != null && !nomApe.equals("") ? " " : "") : "";
                nomApe += filaI.getApe1Selec() != null ? filaI.getApe1Selec() : "";

                nomApe += filaI.getApe2Selec() != null && !filaI.getApe2Selec().equals("") ? (nomApe != null && !nomApe.equals("") ? " " : "") : "";
                nomApe += filaI.getApe2Selec() != null ? filaI.getApe2Selec() : "";
            }

            if(nomApe.length() > maxLengthNombre)
            {
                maxLengthNombre = nomApe.length();
            }

            celda = fila.createCell(19);
            celda.setCellValue(nomApe != null ? nomApe.toUpperCase() : "");
            celda.setCellStyle(estiloCelda);

            //COLUMNA: FECHA_NACIMIENTO
            celda = fila.createCell(20);
            celda.setCellValue(filaI.getFecNac() != null ? filaI.getFecNac() : "");
            celda.setCellStyle(estiloCelda);

            //COLUMNA: EDAD
            Integer edad = MeLanbide46Utils.calcularEdad2(filaI.getFecNac() != null ? format.parse(filaI.getFecNac()) : null, filaI.getFecIni() != null ? format.parse(filaI.getFecIni()) : null);

            celda = fila.createCell(21);
            celda.setCellValue(edad!= null ? edad : 0);
            celda.setCellStyle(estiloCelda);
            celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);

            //COLUMNA: SEXO
            celda = fila.createCell(22);
            celda.setCellValue(filaI.getSexo() != null ? filaI.getSexo().toUpperCase() : "");
            celda.setCellStyle(estiloCelda);

            //COLUMNA: PAIS_CONTRATACION

            String paisCont = filaI.getPaisCont1() != null ? filaI.getPaisCont1().toUpperCase() : "";
            paisCont += filaI.getPaisCont2() != null && !filaI.getPaisCont2().equals("") ? (paisCont != null && !paisCont.equals("") ? System.getProperty("line.separator") : "") : "";
            paisCont += filaI.getPaisCont2() != null && !filaI.getPaisCont2().equals("") ? filaI.getPaisCont2().toUpperCase() : "";
            paisCont += filaI.getPaisCont3() != null && !filaI.getPaisCont3().equals("") ? (paisCont != null && !paisCont.equals("") ? System.getProperty("line.separator") : "") : "";
            paisCont += filaI.getPaisCont3() != null && !filaI.getPaisCont3().equals("") ? filaI.getPaisCont3().toUpperCase() : "";

            celda = fila.createCell(23);
            celda.setCellValue(paisCont != null ? paisCont.toUpperCase() : "");
            celda.setCellStyle(estiloCelda);

            //COLUMNA: TITULACION_PERS_CANDIDATA
            celda = fila.createCell(24);
            celda.setCellValue(filaI.getTitulacionSelec() != null ? filaI.getTitulacionSelec().toUpperCase() : "");
            celda.setCellStyle(estiloCelda);

            //COLUMNA: NIVEL FORMATIVO
            celda = fila.createCell(25);
            celda.setCellValue(filaI.getNivelFormativo() != null ? filaI.getNivelFormativo().toUpperCase() : "");
            celda.setCellStyle(estiloCelda);

            //COLUMNA: FECHA_INICIO
            celda = fila.createCell(26);
            celda.setCellValue(filaI.getFecIni() != null ? filaI.getFecIni() : "");
            celda.setCellStyle(estiloCelda);

            //COLUMNA: FECHA_FIN
            celda = fila.createCell(27);
            celda.setCellValue(filaI.getFecFin() != null ? filaI.getFecFin() : "");
            celda.setCellStyle(estiloCelda);

            //COLUMNA: TIEMPO CONTRATACION
            celda = fila.createCell(28);
            celda.setCellValue(filaI.getTiempoContratacion() != null ? filaI.getTiempoContratacion() : 0);
            celda.setCellStyle(estiloCelda);
            celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);

            //COLUMNA: PUESTOS CONTRATADOS
            celda = fila.createCell(29);
            celda.setCellValue(filaI.getPuestosCont() != null ? filaI.getPuestosCont() : 0);
            totalContRealizados += filaI.getPuestosCont() != null ? filaI.getPuestosCont() : 0;
            celda.setCellStyle(estiloCelda);
            celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                
        
        }catch(Exception ex){
            
        }
        
    }
    
    private void crearEstiloHoja2(HSSFWorkbook libro, HSSFRow fila, HSSFCell celda, HSSFCellStyle estiloCelda, int idioma)
    {
        try
        {
            MeLanbide46I18n meLanbide46I18n = MeLanbide46I18n.getInstance();
            
            HSSFPalette palette = libro.getCustomPalette();
            HSSFFont negritaTitulo = libro.createFont();
            negritaTitulo.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
            negritaTitulo.setColor(HSSFColor.WHITE.index);
            negritaTitulo.setFontHeight((short)200);
            HSSFColor hssfColor = null;
            try 
            {
                hssfColor= palette.findColor((byte)75, (byte)149, (byte)211); 
                if (hssfColor == null )
                {
                    hssfColor = palette.getColor(HSSFColor.ROYAL_BLUE.index);
                }
                
                 //Se añaden los titulos de las columnas
                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(0);
                celda.setCellValue(meLanbide46I18n.getMensaje(idioma, "doc.informePuestosContratados.col1").toUpperCase());
                celda.setCellStyle(estiloCelda);

                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(1);
                celda.setCellValue(meLanbide46I18n.getMensaje(idioma, "doc.informePuestosContratados.col2").toUpperCase());
                celda.setCellStyle(estiloCelda);

                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(2);
                celda.setCellValue(meLanbide46I18n.getMensaje(idioma, "doc.informePuestosContratados.col31").toUpperCase());
                celda.setCellStyle(estiloCelda);

                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(3);
                celda.setCellValue(meLanbide46I18n.getMensaje(idioma, "doc.informePuestosContratados.col3").toUpperCase());
                celda.setCellStyle(estiloCelda);

                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(4);
                celda.setCellValue(meLanbide46I18n.getMensaje(idioma, "doc.informePuestosContratados.col4").toUpperCase());
                celda.setCellStyle(estiloCelda);

                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(5);
                celda.setCellValue(meLanbide46I18n.getMensaje(idioma, "doc.informePuestosContratados.col5").toUpperCase());
                celda.setCellStyle(estiloCelda);

                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(6);
                celda.setCellValue(meLanbide46I18n.getMensaje(idioma, "doc.informePuestosContratados.col6").toUpperCase());
                celda.setCellStyle(estiloCelda);

                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(7);
                celda.setCellValue(meLanbide46I18n.getMensaje(idioma, "doc.informePuestosContratados.col7").toUpperCase());
                celda.setCellStyle(estiloCelda);

                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(8);
                celda.setCellValue(meLanbide46I18n.getMensaje(idioma, "doc.informePuestosContratados.col8").toUpperCase());
                celda.setCellStyle(estiloCelda);

                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(9);
                celda.setCellValue(meLanbide46I18n.getMensaje(idioma, "doc.informePuestosContratados.col9").toUpperCase());
                celda.setCellStyle(estiloCelda);

                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(10);
                celda.setCellValue(meLanbide46I18n.getMensaje(idioma, "doc.informePuestosContratados.col10").toUpperCase());
                celda.setCellStyle(estiloCelda);

                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(11);
                celda.setCellValue(meLanbide46I18n.getMensaje(idioma, "doc.informePuestosContratados.col11").toUpperCase());
                celda.setCellStyle(estiloCelda);

                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(12);
                celda.setCellValue(meLanbide46I18n.getMensaje(idioma, "doc.informePuestosContratados.col12").toUpperCase());
                celda.setCellStyle(estiloCelda);

                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(13);
                celda.setCellValue(meLanbide46I18n.getMensaje(idioma, "doc.informePuestosContratados.col13").toUpperCase());
                celda.setCellStyle(estiloCelda);

                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(14);
                celda.setCellValue(meLanbide46I18n.getMensaje(idioma, "doc.informePuestosContratados.col14").toUpperCase());
                celda.setCellStyle(estiloCelda);

                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(15);
                celda.setCellValue(meLanbide46I18n.getMensaje(idioma, "doc.informePuestosContratados.col15").toUpperCase());
                celda.setCellStyle(estiloCelda);

                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(16);
                celda.setCellValue(meLanbide46I18n.getMensaje(idioma, "doc.informePuestosContratados.col16").toUpperCase());
                celda.setCellStyle(estiloCelda);

                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderRight(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(17);
                celda.setCellValue(meLanbide46I18n.getMensaje(idioma, "doc.informePuestosContratados.col29").toUpperCase());
                celda.setCellStyle(estiloCelda);

                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(18);
                celda.setCellValue(meLanbide46I18n.getMensaje(idioma, "doc.informePuestosContratados.col30").toUpperCase());
                celda.setCellStyle(estiloCelda);

                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(19);
                celda.setCellValue(meLanbide46I18n.getMensaje(idioma, "doc.informePuestosContratados.col17").toUpperCase());
                celda.setCellStyle(estiloCelda);

                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(20);
                celda.setCellValue(meLanbide46I18n.getMensaje(idioma, "doc.informePuestosContratados.col18").toUpperCase());
                celda.setCellStyle(estiloCelda);

                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(21);
                celda.setCellValue(meLanbide46I18n.getMensaje(idioma, "doc.informePuestosContratados.col19").toUpperCase());
                celda.setCellStyle(estiloCelda);

                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(22);
                celda.setCellValue(meLanbide46I18n.getMensaje(idioma, "doc.informePuestosContratados.col20").toUpperCase());
                celda.setCellStyle(estiloCelda);

                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(23);
                celda.setCellValue(meLanbide46I18n.getMensaje(idioma, "doc.informePuestosContratados.col21").toUpperCase());
                celda.setCellStyle(estiloCelda);

                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(24);
                celda.setCellValue(meLanbide46I18n.getMensaje(idioma, "doc.informePuestosContratados.col22").toUpperCase());
                celda.setCellStyle(estiloCelda);

                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(25);
                celda.setCellValue(meLanbide46I18n.getMensaje(idioma, "doc.informePuestosContratados.col23").toUpperCase());
                celda.setCellStyle(estiloCelda);

                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(26);
                celda.setCellValue(meLanbide46I18n.getMensaje(idioma, "doc.informePuestosContratados.col24").toUpperCase());
                celda.setCellStyle(estiloCelda);

                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(27);
                celda.setCellValue(meLanbide46I18n.getMensaje(idioma, "doc.informePuestosContratados.col25").toUpperCase());
                celda.setCellStyle(estiloCelda);

                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(28);
                celda.setCellValue(meLanbide46I18n.getMensaje(idioma, "doc.informePuestosContratados.col26").toUpperCase());
                celda.setCellStyle(estiloCelda);

                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderRight(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(29);
                celda.setCellValue(meLanbide46I18n.getMensaje(idioma, "doc.informePuestosContratados.col27").toUpperCase());
                celda.setCellStyle(estiloCelda);
            }
            catch (Exception e) 
            {
                e.printStackTrace();
            }

               
        }catch(Exception ex){
            
        }
        
    }
    
    private void anadirSumatoriosParcialesResumenPuestos(int idioma, HSSFWorkbook libro, HSSFRow fila, HSSFFont fuente, int totalPuestosSolic, int totalContRealizados, int totalPuestosDenegados, int totalPuestosRenAntesRes, int totalPersCandidatas, int totalPuestosRenAntesPag1)
    {   
        HSSFCellStyle estiloCelda = null;
        HSSFCell celda = null;
        MeLanbide46I18n meLanbide46I18n = MeLanbide46I18n.getInstance();
        
        //COLUMNA: NUM. EXPEDIENTE
        estiloCelda = libro.createCellStyle();
        estiloCelda.setWrapText(true);
        estiloCelda.setFont(fuente);
        estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);

        celda = fila.createCell(0);
        celda.setCellStyle(estiloCelda);
        
        //COLUMNA: EMPRESA
        estiloCelda = libro.createCellStyle();
        estiloCelda.setWrapText(true);
        estiloCelda.setFont(fuente);
        estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);

        celda = fila.createCell(1);
        celda.setCellStyle(estiloCelda);

        //COLUMNA: TERRITORIO HISTORICO
        estiloCelda = libro.createCellStyle();
        estiloCelda.setWrapText(true);
        estiloCelda.setFont(fuente);
        estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        estiloCelda.setBorderTop(HSSFCellStyle.BORDER_DOUBLE);
        estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);

        celda = fila.createCell(2);
        celda.setCellValue(meLanbide46I18n.getMensaje(idioma, "doc.informePuestosContratados.total").toUpperCase());
        celda.setCellStyle(estiloCelda);

        //COLUMNA: PUESTOS SOLICITADOS
        estiloCelda = libro.createCellStyle();
        estiloCelda.setWrapText(true);
        estiloCelda.setFont(fuente);
        estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        estiloCelda.setBorderTop(HSSFCellStyle.BORDER_DOUBLE);
        estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);

        celda = fila.createCell(3);
        celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
        celda.setCellValue(totalPuestosSolic);
        celda.setCellStyle(estiloCelda);

        //COLUMNA: PUESTO
        estiloCelda = libro.createCellStyle();
        estiloCelda.setWrapText(true);
        estiloCelda.setFont(fuente);
        estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        estiloCelda.setBorderTop(HSSFCellStyle.BORDER_DOUBLE);
        estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);

        celda = fila.createCell(4);
        celda.setCellType(HSSFCell.CELL_TYPE_BLANK);
        celda.setCellStyle(estiloCelda);

        //COLUMNA: PAIS
        estiloCelda = libro.createCellStyle();
        estiloCelda.setWrapText(true);
        estiloCelda.setFont(fuente);
        estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        estiloCelda.setBorderTop(HSSFCellStyle.BORDER_DOUBLE);
        estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);

        celda = fila.createCell(5);
        celda.setCellType(HSSFCell.CELL_TYPE_BLANK);
        celda.setCellStyle(estiloCelda);

        //COLUMNA TITULACION
        estiloCelda = libro.createCellStyle();
        estiloCelda.setWrapText(true);
        estiloCelda.setFont(fuente);
        estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        estiloCelda.setBorderTop(HSSFCellStyle.BORDER_DOUBLE);
        estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);

        celda = fila.createCell(6);
        celda.setCellType(HSSFCell.CELL_TYPE_BLANK);
        celda.setCellStyle(estiloCelda);

        //COLUMNA: IDIOMAS
        estiloCelda = libro.createCellStyle();
        estiloCelda.setWrapText(true);
        estiloCelda.setFont(fuente);
        estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        estiloCelda.setBorderTop(HSSFCellStyle.BORDER_DOUBLE);
        estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);

        celda = fila.createCell(7);
        celda.setCellType(HSSFCell.CELL_TYPE_BLANK);
        celda.setCellStyle(estiloCelda);

        //COLUMNA: RESULTADO
        estiloCelda = libro.createCellStyle();
        estiloCelda.setWrapText(true);
        estiloCelda.setFont(fuente);
        estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        estiloCelda.setBorderTop(HSSFCellStyle.BORDER_DOUBLE);
        estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);

        celda = fila.createCell(8);
        celda.setCellType(HSSFCell.CELL_TYPE_BLANK);
        celda.setCellStyle(estiloCelda);

        //COLUMNA: PUESTOS DENEGADOS
        estiloCelda = libro.createCellStyle();
        estiloCelda.setWrapText(true);
        estiloCelda.setFont(fuente);
        estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        estiloCelda.setBorderTop(HSSFCellStyle.BORDER_DOUBLE);
        estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);

        celda = fila.createCell(9);
        celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
        celda.setCellValue(totalPuestosDenegados);
        celda.setCellStyle(estiloCelda);

        //COLUMNA: TOTAL PUESTOS REN. ANTES RES.
        estiloCelda = libro.createCellStyle();
        estiloCelda.setWrapText(true);
        estiloCelda.setFont(fuente);
        estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        estiloCelda.setBorderTop(HSSFCellStyle.BORDER_DOUBLE);
        estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);

        celda = fila.createCell(10);
        celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
        celda.setCellValue(totalPuestosRenAntesRes);
        celda.setCellStyle(estiloCelda);

        //COLUMNA: MOTIVO
        estiloCelda = libro.createCellStyle();
        estiloCelda.setWrapText(true);
        estiloCelda.setFont(fuente);
        estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        estiloCelda.setBorderTop(HSSFCellStyle.BORDER_DOUBLE);
        estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);

        celda = fila.createCell(11);
        celda.setCellType(HSSFCell.CELL_TYPE_BLANK);
        celda.setCellStyle(estiloCelda);

        //COLUMNA: OFERTA
        estiloCelda = libro.createCellStyle();
        estiloCelda.setWrapText(true);
        estiloCelda.setFont(fuente);
        estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        estiloCelda.setBorderTop(HSSFCellStyle.BORDER_DOUBLE);
        estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);

        celda = fila.createCell(12);
        celda.setCellType(HSSFCell.CELL_TYPE_BLANK);
        celda.setCellStyle(estiloCelda);

        //COLUMNA: PERSONAS PRECANDIDATAS
        estiloCelda = libro.createCellStyle();
        estiloCelda.setWrapText(true);
        estiloCelda.setFont(fuente);
        estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        estiloCelda.setBorderTop(HSSFCellStyle.BORDER_DOUBLE);
        estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);

        celda = fila.createCell(13);
        celda.setCellType(HSSFCell.CELL_TYPE_BLANK);
        celda.setCellStyle(estiloCelda);

        //COLUMNA: DIFUSION
        estiloCelda = libro.createCellStyle();
        estiloCelda.setWrapText(true);
        estiloCelda.setFont(fuente);
        estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        estiloCelda.setBorderTop(HSSFCellStyle.BORDER_DOUBLE);
        estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);

        celda = fila.createCell(14);
        celda.setCellType(HSSFCell.CELL_TYPE_BLANK);
        celda.setCellStyle(estiloCelda);

        //COLUMNA: FECHA ENVIO PERSONAS CANDIDATAS
        estiloCelda = libro.createCellStyle();
        estiloCelda.setWrapText(true);
        estiloCelda.setFont(fuente);
        estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        estiloCelda.setBorderTop(HSSFCellStyle.BORDER_DOUBLE);
        estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);

        celda = fila.createCell(15);
        celda.setCellType(HSSFCell.CELL_TYPE_BLANK);
        celda.setCellStyle(estiloCelda);

        //COLUMNA: TOTAL PERSONAS CANDIDATAS
        estiloCelda = libro.createCellStyle();
        estiloCelda.setWrapText(true);
        estiloCelda.setFont(fuente);
        estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        estiloCelda.setBorderTop(HSSFCellStyle.BORDER_DOUBLE);
        estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);

        celda = fila.createCell(16);
        celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
        celda.setCellValue(totalPersCandidatas);
        celda.setCellStyle(estiloCelda);

        //COLUMNA: NUM. PUESTO REN. ANTES 1 PAGO
        estiloCelda = libro.createCellStyle();
        estiloCelda.setWrapText(true);
        estiloCelda.setFont(fuente);
        estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        estiloCelda.setBorderTop(HSSFCellStyle.BORDER_DOUBLE);
        estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);

        celda = fila.createCell(17);
        celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
        celda.setCellValue(totalPuestosRenAntesPag1);
        celda.setCellStyle(estiloCelda);

        //COLUMNA: CAUSA RENUNCIA
        estiloCelda = libro.createCellStyle();
        estiloCelda.setWrapText(true);
        estiloCelda.setFont(fuente);
        estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        estiloCelda.setBorderTop(HSSFCellStyle.BORDER_DOUBLE);
        estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);

        celda = fila.createCell(18);
        celda.setCellType(HSSFCell.CELL_TYPE_BLANK);
        celda.setCellStyle(estiloCelda);

        //COLUMNA: PERSONA SELECCIONADA
        estiloCelda = libro.createCellStyle();
        estiloCelda.setWrapText(true);
        estiloCelda.setFont(fuente);
        estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        estiloCelda.setBorderTop(HSSFCellStyle.BORDER_DOUBLE);
        estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);

        celda = fila.createCell(19);
        celda.setCellType(HSSFCell.CELL_TYPE_BLANK);
        celda.setCellStyle(estiloCelda);

        //COLUMNA: FECHA NACIMIENTO
        estiloCelda = libro.createCellStyle();
        estiloCelda.setWrapText(true);
        estiloCelda.setFont(fuente);
        estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        estiloCelda.setBorderTop(HSSFCellStyle.BORDER_DOUBLE);
        estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);

        celda = fila.createCell(20);
        celda.setCellType(HSSFCell.CELL_TYPE_BLANK);
        celda.setCellStyle(estiloCelda);

        //COLUMNA: EDAD FECHA CONTRATO
        estiloCelda = libro.createCellStyle();
        estiloCelda.setWrapText(true);
        estiloCelda.setFont(fuente);
        estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        estiloCelda.setBorderTop(HSSFCellStyle.BORDER_DOUBLE);
        estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);

        celda = fila.createCell(21);
        celda.setCellType(HSSFCell.CELL_TYPE_BLANK);
        celda.setCellStyle(estiloCelda);

        //COLUMNA: SEXO
        estiloCelda = libro.createCellStyle();
        estiloCelda.setWrapText(true);
        estiloCelda.setFont(fuente);
        estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        estiloCelda.setBorderTop(HSSFCellStyle.BORDER_DOUBLE);
        estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);

        celda = fila.createCell(22);
        celda.setCellType(HSSFCell.CELL_TYPE_BLANK);
        celda.setCellStyle(estiloCelda);

        //COLUMNA: PAIS CONTRATACION
        estiloCelda = libro.createCellStyle();
        estiloCelda.setWrapText(true);
        estiloCelda.setFont(fuente);
        estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        estiloCelda.setBorderTop(HSSFCellStyle.BORDER_DOUBLE);
        estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);

        celda = fila.createCell(23);
        celda.setCellType(HSSFCell.CELL_TYPE_BLANK);
        celda.setCellStyle(estiloCelda);

        //COLUMNA: TITULACION PERSONA CANDIDATA
        estiloCelda = libro.createCellStyle();
        estiloCelda.setWrapText(true);
        estiloCelda.setFont(fuente);
        estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        estiloCelda.setBorderTop(HSSFCellStyle.BORDER_DOUBLE);
        estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);

        celda = fila.createCell(24);
        celda.setCellType(HSSFCell.CELL_TYPE_BLANK);
        celda.setCellStyle(estiloCelda);

        //COLUMNA: NIVEL FORMATIVO
        estiloCelda = libro.createCellStyle();
        estiloCelda.setWrapText(true);
        estiloCelda.setFont(fuente);
        estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        estiloCelda.setBorderTop(HSSFCellStyle.BORDER_DOUBLE);
        estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);

        celda = fila.createCell(25);
        celda.setCellType(HSSFCell.CELL_TYPE_BLANK);
        celda.setCellStyle(estiloCelda);

        //COLUMNA: FECHA REAL INICIO CONTRATO
        estiloCelda = libro.createCellStyle();
        estiloCelda.setWrapText(true);
        estiloCelda.setFont(fuente);
        estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        estiloCelda.setBorderTop(HSSFCellStyle.BORDER_DOUBLE);
        estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);

        celda = fila.createCell(26);
        celda.setCellType(HSSFCell.CELL_TYPE_BLANK);
        celda.setCellStyle(estiloCelda);

        //COLUMNA: FECHA REAL FIN CONTRATO
        estiloCelda = libro.createCellStyle();
        estiloCelda.setWrapText(true);
        estiloCelda.setFont(fuente);
        estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        estiloCelda.setBorderTop(HSSFCellStyle.BORDER_DOUBLE);
        estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);

        celda = fila.createCell(27);
        celda.setCellType(HSSFCell.CELL_TYPE_BLANK);
        celda.setCellStyle(estiloCelda);

        //COLUMNA: TIEMPO DE CONTRATACION
        estiloCelda = libro.createCellStyle();
        estiloCelda.setWrapText(true);
        estiloCelda.setFont(fuente);
        estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        estiloCelda.setBorderTop(HSSFCellStyle.BORDER_DOUBLE);
        estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);

        celda = fila.createCell(28);
        celda.setCellType(HSSFCell.CELL_TYPE_BLANK);
        celda.setCellStyle(estiloCelda);

        //COLUMNA: NUM CONTRATOS REALIZADOS
        estiloCelda = libro.createCellStyle();
        estiloCelda.setWrapText(true);
        estiloCelda.setFont(fuente);
        estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        estiloCelda.setBorderTop(HSSFCellStyle.BORDER_DOUBLE);
        estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderRight(HSSFCellStyle.BORDER_THICK);

        celda = fila.createCell(29);
        celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
        celda.setCellValue(totalContRealizados);
        celda.setCellStyle(estiloCelda);
    }
    
    public void generarInformeResumenEconomico(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response)
    {
        String rutaArchivoSalida = null;
        try
        {
            Integer ano = Integer.parseInt(request.getParameter("ano"));
            if(ano > 2015 ){
                generarInformeResumenEconomico2016(codOrganizacion, codTramite, ocurrenciaTramite, numExpediente, request, response);
            }
            else
            {
                AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));

                int idioma = 1;
                HttpSession session = request.getSession();
                UsuarioValueObject usuario = new UsuarioValueObject();
                try
                {
                    if (session != null) 
                    {
                        usuario = (UsuarioValueObject) session.getAttribute("usuario");
                        if (usuario != null) 
                        {
                            idioma = usuario.getIdioma();
                        }
                    }
                }
                catch(Exception ex)
                {

                }

                MeLanbide46I18n meLanbide46I18n = MeLanbide46I18n.getInstance();
                MeLanbide46Manager manager = MeLanbide46Manager.getInstance();

                try 
                {
                    HSSFWorkbook libro = new HSSFWorkbook();

                    //se escribe el fichero
                    DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
                    //get current date time with Date()
                    Date date = new Date();
                    String fechaAct=dateFormat.format(date);
           
                    File informe= new File("resumenEconomico_"+fechaAct+".xls");

                    FileOutputStream archivoSalida = new FileOutputStream(informe);
                   
                    
                    HSSFPalette palette = libro.getCustomPalette();
                    HSSFColor hssfColor = null;
                    try 
                    {
                        hssfColor= palette.findColor((byte)75, (byte)149, (byte)211); 
                        if (hssfColor == null )
                        {
                            hssfColor = palette.getColor(HSSFColor.ROYAL_BLUE.index);
                        }
                    }
                    catch (Exception e) 
                    {
                        e.printStackTrace();
                    }

                    HSSFFont negrita = libro.createFont();
                    negrita.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
                    negrita.setFontHeight((short)150);

                    HSSFFont normal = libro.createFont();
                    normal.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
                    normal.setFontHeight((short)150);

                    HSSFFont negritaTitulo = libro.createFont();
                    negritaTitulo.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
                    negritaTitulo.setColor(HSSFColor.WHITE.index);
                    negritaTitulo.setFontHeight((short)170);

                    HSSFSheet hoja = null;                

                    int numFila = 0;
                    int numFila2 = 0;

                    HSSFRow fila = null;
                    HSSFCell celda = null;
                    HSSFCellStyle estiloCelda = null;

                    HSSFRow fila2 = null;
                    HSSFCell celda2 = null;
                    HSSFCellStyle estiloCelda2 = null;

                    hoja = libro.createSheet();
                    HSSFSheet hoja2 = libro.createSheet("sin formato");

                    //Se establece el ancho de cada columnas
                    hoja.setColumnWidth(0, 6000);//Num Expediente
                    hoja.setColumnWidth(1, 10000);//Entidad
                    hoja.setColumnWidth(2, 8000);//Territorio historico
                    hoja.setColumnWidth(3, 4000);//CNAE
                    hoja.setColumnWidth(4, 5000);//Num puestos solic
                    hoja.setColumnWidth(5, 10000);//puesto
                    hoja.setColumnWidth(6, 6000);//resultado
                    hoja.setColumnWidth(7, 5000);//fecha inicio
                    hoja.setColumnWidth(8, 5000);//fecha fin
                    hoja.setColumnWidth(9, 4000);//periodo meses
                    hoja.setColumnWidth(10, 5000);//rango edad
                    hoja.setColumnWidth(11, 8000);//pais
                    hoja.setColumnWidth(12, 5000);//costes contrato salario + ss
                    hoja.setColumnWidth(13, 5000);//dietas convenio
                    hoja.setColumnWidth(14, 5000);//coste de la contratacion
                    hoja.setColumnWidth(15, 5000);//salario anual bruto
                    hoja.setColumnWidth(16, 5000);//dietas alojamiento y manutencion
                    hoja.setColumnWidth(17, 5000);//tramitacion de permisos y seguros
                    hoja.setColumnWidth(18, 5000);//importe total solicitado
                    hoja.setColumnWidth(19, 5000);//dotacion anual
                    hoja.setColumnWidth(20, 5000);//meses subvencionables
                    hoja.setColumnWidth(21, 5000);//dietas subvencionables
                    hoja.setColumnWidth(22, 5000);//tramitacion de permisos y seguros
                    hoja.setColumnWidth(23, 2700);//minimis
                    hoja.setColumnWidth(24, 4000);//importe maximo subvencionable
                    hoja.setColumnWidth(25, 3500);//importe concedido
                    hoja.setColumnWidth(26, 4500);//num renuncias
                    hoja.setColumnWidth(27, 3000);//importe renuncia
                    hoja.setColumnWidth(28, 5000);//motivo renuncia
                    hoja.setColumnWidth(29, 5000);//importe concedido tras res modif


                    //Se establece el ancho de cada columnas
                    hoja2.setColumnWidth(0, 6000);//Num Expediente
                    hoja2.setColumnWidth(1, 10000);//Entidad
                    hoja2.setColumnWidth(2, 8000);//Territorio historico
                    hoja2.setColumnWidth(3, 4000);//CNAE
                    hoja2.setColumnWidth(4, 5000);//Num puestos solic
                    hoja2.setColumnWidth(5, 10000);//puesto
                    hoja2.setColumnWidth(6, 6000);//resultado
                    hoja2.setColumnWidth(7, 5000);//fecha inicio
                    hoja2.setColumnWidth(8, 5000);//fecha fin
                    hoja2.setColumnWidth(9, 4000);//periodo meses
                    hoja2.setColumnWidth(10, 5000);//rango edad
                    hoja2.setColumnWidth(11, 8000);//pais
                    hoja2.setColumnWidth(12, 5000);//costes contrato salario + ss
                    hoja2.setColumnWidth(13, 5000);//dietas convenio
                    hoja2.setColumnWidth(14, 5000);//coste de la contratacion
                    hoja2.setColumnWidth(15, 5000);//salario anual bruto
                    hoja2.setColumnWidth(16, 5000);//dietas alojamiento y manutencion
                    hoja2.setColumnWidth(17, 5000);//tramitacion de permisos y seguros
                    hoja2.setColumnWidth(18, 5000);//importe total solicitado
                    hoja2.setColumnWidth(19, 5000);//dotacion anual
                    hoja2.setColumnWidth(20, 5000);//meses subvencionables
                    hoja2.setColumnWidth(21, 5000);//dietas subvencionables
                    hoja2.setColumnWidth(22, 5000);//tramitacion de permisos y seguros
                    hoja2.setColumnWidth(23, 2700);//minimis
                    hoja2.setColumnWidth(24, 4000);//importe maximo subvencionable
                    hoja2.setColumnWidth(25, 3500);//importe concedido
                    hoja2.setColumnWidth(26, 4500);//num renuncias
                    hoja2.setColumnWidth(27, 3000);//importe renuncia
                    hoja2.setColumnWidth(28, 5000);//motivo renuncia
                    hoja2.setColumnWidth(29, 5000);//importe concedido tras res modif

                    //Se añaden los titulos de las columnas
                    fila = hoja.createRow(numFila);
                    fila2 = hoja2.createRow(numFila2);

                    estiloCelda = libro.createCellStyle();
                    estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                    estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                    estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                    estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                    estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                    estiloCelda.setWrapText(true);
                    estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                    estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                    estiloCelda.setFont(negritaTitulo);
                    celda = fila.createCell(0);
                    celda.setCellValue(meLanbide46I18n.getMensaje(idioma, "doc.informeEconomico.col1").toUpperCase());
                    celda.setCellStyle(estiloCelda);

                    estiloCelda = libro.createCellStyle();
                    estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                    estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                    estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                    estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                    estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                    estiloCelda.setWrapText(true);
                    estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                    estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                    estiloCelda.setFont(negritaTitulo);
                    celda = fila.createCell(1);
                    celda.setCellValue(meLanbide46I18n.getMensaje(idioma, "doc.informeEconomico.col2").toUpperCase());
                    celda.setCellStyle(estiloCelda);

                    estiloCelda = libro.createCellStyle();
                    estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                    estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                    estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                    estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                    estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                    estiloCelda.setWrapText(true);
                    estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                    estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                    estiloCelda.setFont(negritaTitulo);
                    celda = fila.createCell(2);
                    celda.setCellValue(meLanbide46I18n.getMensaje(idioma, "doc.informeEconomico.col30").toUpperCase());
                    celda.setCellStyle(estiloCelda);

                    estiloCelda = libro.createCellStyle();
                    estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                    estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                    estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                    estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                    estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                    estiloCelda.setWrapText(true);
                    estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                    estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                    estiloCelda.setFont(negritaTitulo);
                    celda = fila.createCell(3);
                    celda.setCellValue(meLanbide46I18n.getMensaje(idioma, "doc.informeEconomico.col3").toUpperCase());
                    celda.setCellStyle(estiloCelda);

                    estiloCelda = libro.createCellStyle();
                    estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                    estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                    estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                    estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                    estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                    estiloCelda.setWrapText(true);
                    estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                    estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                    estiloCelda.setFont(negritaTitulo);
                    celda = fila.createCell(4);
                    celda.setCellValue(meLanbide46I18n.getMensaje(idioma, "doc.informeEconomico.col4").toUpperCase());
                    celda.setCellStyle(estiloCelda);

                    estiloCelda = libro.createCellStyle();
                    estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                    estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                    estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                    estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                    estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                    estiloCelda.setWrapText(true);
                    estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                    estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                    estiloCelda.setFont(negritaTitulo);
                    celda = fila.createCell(5);
                    celda.setCellValue(meLanbide46I18n.getMensaje(idioma, "doc.informeEconomico.col5").toUpperCase());
                    celda.setCellStyle(estiloCelda);

                    estiloCelda = libro.createCellStyle();
                    estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                    estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                    estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                    estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                    estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                    estiloCelda.setWrapText(true);
                    estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                    estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                    estiloCelda.setFont(negritaTitulo);
                    celda = fila.createCell(6);
                    celda.setCellValue(meLanbide46I18n.getMensaje(idioma, "doc.informeEconomico.col6").toUpperCase());
                    celda.setCellStyle(estiloCelda);

                    estiloCelda = libro.createCellStyle();
                    estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                    estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                    estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                    estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                    estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                    estiloCelda.setWrapText(true);
                    estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                    estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                    estiloCelda.setFont(negritaTitulo);
                    celda = fila.createCell(7);
                    celda.setCellValue(meLanbide46I18n.getMensaje(idioma, "doc.informeEconomico.col7").toUpperCase());
                    celda.setCellStyle(estiloCelda);

                    estiloCelda = libro.createCellStyle();
                    estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                    estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                    estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                    estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                    estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                    estiloCelda.setWrapText(true);
                    estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                    estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                    estiloCelda.setFont(negritaTitulo);
                    celda = fila.createCell(8);
                    celda.setCellValue(meLanbide46I18n.getMensaje(idioma, "doc.informeEconomico.col8").toUpperCase());
                    celda.setCellStyle(estiloCelda);

                    estiloCelda = libro.createCellStyle();
                    estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                    estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                    estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                    estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                    estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                    estiloCelda.setWrapText(true);
                    estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                    estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                    estiloCelda.setFont(negritaTitulo);
                    celda = fila.createCell(9);
                    celda.setCellValue(meLanbide46I18n.getMensaje(idioma, "doc.informeEconomico.col9").toUpperCase());
                    celda.setCellStyle(estiloCelda);

                    estiloCelda = libro.createCellStyle();
                    estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                    estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                    estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                    estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                    estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                    estiloCelda.setWrapText(true);
                    estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                    estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                    estiloCelda.setFont(negritaTitulo);
                    celda = fila.createCell(10);
                    celda.setCellValue(meLanbide46I18n.getMensaje(idioma, "doc.informeEconomico.col10").toUpperCase());
                    celda.setCellStyle(estiloCelda);

                    estiloCelda = libro.createCellStyle();
                    estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                    estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                    estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                    estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                    estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                    estiloCelda.setWrapText(true);
                    estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                    estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                    estiloCelda.setFont(negritaTitulo);
                    celda = fila.createCell(11);
                    celda.setCellValue(meLanbide46I18n.getMensaje(idioma, "doc.informeEconomico.col11").toUpperCase());
                    celda.setCellStyle(estiloCelda);

                    estiloCelda = libro.createCellStyle();
                    estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                    estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                    estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                    estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                    estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                    estiloCelda.setWrapText(true);
                    estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                    estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                    estiloCelda.setFont(negritaTitulo);
                    celda = fila.createCell(12);
                    celda.setCellValue(meLanbide46I18n.getMensaje(idioma, "doc.informeEconomico.col12").toUpperCase());
                    celda.setCellStyle(estiloCelda);

                    estiloCelda = libro.createCellStyle();
                    estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                    estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                    estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                    estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                    estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                    estiloCelda.setWrapText(true);
                    estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                    estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                    estiloCelda.setFont(negritaTitulo);
                    celda = fila.createCell(13);
                    celda.setCellValue(meLanbide46I18n.getMensaje(idioma, "doc.informeEconomico.col13").toUpperCase());
                    celda.setCellStyle(estiloCelda);

                    estiloCelda = libro.createCellStyle();
                    estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                    estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                    estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                    estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                    estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                    estiloCelda.setWrapText(true);
                    estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                    estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                    estiloCelda.setFont(negritaTitulo);
                    celda = fila.createCell(14);
                    celda.setCellValue(meLanbide46I18n.getMensaje(idioma, "doc.informeEconomico.col14").toUpperCase());
                    celda.setCellStyle(estiloCelda);

                    estiloCelda = libro.createCellStyle();
                    estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                    estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                    estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                    estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                    estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                    estiloCelda.setWrapText(true);
                    estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                    estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                    estiloCelda.setFont(negritaTitulo);
                    celda = fila.createCell(15);
                    celda.setCellValue(meLanbide46I18n.getMensaje(idioma, "doc.informeEconomico.col15").toUpperCase());
                    celda.setCellStyle(estiloCelda);

                    estiloCelda = libro.createCellStyle();
                    estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                    estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                    estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                    estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                    estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                    estiloCelda.setWrapText(true);
                    estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                    estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                    estiloCelda.setFont(negritaTitulo);
                    celda = fila.createCell(16);
                    celda.setCellValue(meLanbide46I18n.getMensaje(idioma, "doc.informeEconomico.col16").toUpperCase());
                    celda.setCellStyle(estiloCelda);

                    estiloCelda = libro.createCellStyle();
                    estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                    estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                    estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                    estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                    estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                    estiloCelda.setWrapText(true);
                    estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                    estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                    estiloCelda.setFont(negritaTitulo);
                    celda = fila.createCell(17);
                    celda.setCellValue(meLanbide46I18n.getMensaje(idioma, "doc.informeEconomico.col17").toUpperCase());
                    celda.setCellStyle(estiloCelda);

                    estiloCelda = libro.createCellStyle();
                    estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                    estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                    estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                    estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                    estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                    estiloCelda.setWrapText(true);
                    estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                    estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                    estiloCelda.setFont(negritaTitulo);
                    celda = fila.createCell(18);
                    celda.setCellValue(meLanbide46I18n.getMensaje(idioma, "doc.informeEconomico.col18").toUpperCase());
                    celda.setCellStyle(estiloCelda);

                    estiloCelda = libro.createCellStyle();
                    estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                    estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                    estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                    estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                    estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                    estiloCelda.setWrapText(true);
                    estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                    estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                    estiloCelda.setFont(negritaTitulo);
                    celda = fila.createCell(19);
                    celda.setCellValue(meLanbide46I18n.getMensaje(idioma, "doc.informeEconomico.col19").toUpperCase());
                    celda.setCellStyle(estiloCelda);

                    estiloCelda = libro.createCellStyle();
                    estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                    estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                    estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                    estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                    estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                    estiloCelda.setWrapText(true);
                    estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                    estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                    estiloCelda.setFont(negritaTitulo);
                    celda = fila.createCell(20);
                    celda.setCellValue(meLanbide46I18n.getMensaje(idioma, "doc.informeEconomico.col20").toUpperCase());
                    celda.setCellStyle(estiloCelda);

                    estiloCelda = libro.createCellStyle();
                    estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                    estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                    estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                    estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                    estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                    estiloCelda.setWrapText(true);
                    estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                    estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                    estiloCelda.setFont(negritaTitulo);
                    celda = fila.createCell(21);
                    celda.setCellValue(meLanbide46I18n.getMensaje(idioma, "doc.informeEconomico.col21").toUpperCase());
                    celda.setCellStyle(estiloCelda);

                    estiloCelda = libro.createCellStyle();
                    estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                    estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                    estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                    estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                    estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                    estiloCelda.setWrapText(true);
                    estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                    estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                    estiloCelda.setFont(negritaTitulo);
                    celda = fila.createCell(22);
                    celda.setCellValue(meLanbide46I18n.getMensaje(idioma, "doc.informeEconomico.col22").toUpperCase());
                    celda.setCellStyle(estiloCelda);

                    estiloCelda = libro.createCellStyle();
                    estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                    estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                    estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                    estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                    estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                    estiloCelda.setWrapText(true);
                    estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                    estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                    estiloCelda.setFont(negritaTitulo);
                    celda = fila.createCell(23);
                    celda.setCellValue(meLanbide46I18n.getMensaje(idioma, "doc.informeEconomico.col23").toUpperCase());
                    celda.setCellStyle(estiloCelda);

                    estiloCelda = libro.createCellStyle();
                    estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                    estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                    estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                    estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                    estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                    estiloCelda.setWrapText(true);
                    estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                    estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                    estiloCelda.setFont(negritaTitulo);
                    celda = fila.createCell(24);
                    celda.setCellValue(meLanbide46I18n.getMensaje(idioma, "doc.informeEconomico.col24").toUpperCase());
                    celda.setCellStyle(estiloCelda);


                    estiloCelda = libro.createCellStyle();
                    estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                    estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                    estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                    estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                    estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                    estiloCelda.setBorderRight(HSSFCellStyle.BORDER_THICK);
                    estiloCelda.setWrapText(true);
                    estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                    estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                    estiloCelda.setFont(negritaTitulo);
                    celda = fila.createCell(25);
                    celda.setCellValue(meLanbide46I18n.getMensaje(idioma, "doc.informeEconomico.col25").toUpperCase());
                    celda.setCellStyle(estiloCelda);

                    estiloCelda = libro.createCellStyle();
                    estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                    estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                    estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                    estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                    estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                    estiloCelda.setBorderRight(HSSFCellStyle.BORDER_THICK);
                    estiloCelda.setWrapText(true);
                    estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                    estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                    estiloCelda.setFont(negritaTitulo);
                    celda = fila.createCell(26);
                    celda.setCellValue(meLanbide46I18n.getMensaje(idioma, "doc.informeEconomico.col26").toUpperCase());
                    celda.setCellStyle(estiloCelda);

                    estiloCelda = libro.createCellStyle();
                    estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                    estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                    estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                    estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                    estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                    estiloCelda.setBorderRight(HSSFCellStyle.BORDER_THICK);
                    estiloCelda.setWrapText(true);
                    estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                    estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                    estiloCelda.setFont(negritaTitulo);
                    celda = fila.createCell(27);
                    celda.setCellValue(meLanbide46I18n.getMensaje(idioma, "doc.informeEconomico.col27").toUpperCase());
                    celda.setCellStyle(estiloCelda);

                    estiloCelda = libro.createCellStyle();
                    estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                    estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                    estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                    estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                    estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                    estiloCelda.setBorderRight(HSSFCellStyle.BORDER_THICK);
                    estiloCelda.setWrapText(true);
                    estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                    estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                    estiloCelda.setFont(negritaTitulo);
                    celda = fila.createCell(28);
                    celda.setCellValue(meLanbide46I18n.getMensaje(idioma, "doc.informeEconomico.col28").toUpperCase());
                    celda.setCellStyle(estiloCelda);

                    estiloCelda = libro.createCellStyle();
                    estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                    estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                    estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                    estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                    estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                    estiloCelda.setBorderRight(HSSFCellStyle.BORDER_THICK);
                    estiloCelda.setWrapText(true);
                    estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                    estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                    estiloCelda.setFont(negritaTitulo);
                    celda = fila.createCell(29);
                    celda.setCellValue(meLanbide46I18n.getMensaje(idioma, "doc.informeEconomico.col29").toUpperCase());
                    celda.setCellStyle(estiloCelda);

                    crearEstiloHoja2Economico(libro, fila2, celda2, estiloCelda2, idioma);
                    
                    //Estilos celdas datos
                        //celdas primera columna -> 3 Estilos
                    HSSFCellStyle estiloCeldaColumnaPrimeraFilaPrimera = libro.createCellStyle();
                    estiloCeldaColumnaPrimeraFilaPrimera.setWrapText(true);
                    estiloCeldaColumnaPrimeraFilaPrimera.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                    estiloCeldaColumnaPrimeraFilaPrimera.setBorderTop(HSSFCellStyle.BORDER_THICK);
                    estiloCeldaColumnaPrimeraFilaPrimera.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                    estiloCeldaColumnaPrimeraFilaPrimera.setFont(normal);                     
                        
                    HSSFCellStyle estiloCeldaColumnaPrimeraFilaIntermedia = libro.createCellStyle();
                    estiloCeldaColumnaPrimeraFilaIntermedia.setWrapText(true);
                    estiloCeldaColumnaPrimeraFilaIntermedia.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                    estiloCeldaColumnaPrimeraFilaIntermedia.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                    estiloCeldaColumnaPrimeraFilaIntermedia.setFont(normal);
                        
                    HSSFCellStyle estiloCeldaColumnaPrimeraFilaUltima = libro.createCellStyle();
                    estiloCeldaColumnaPrimeraFilaUltima.setWrapText(true);
                    estiloCeldaColumnaPrimeraFilaUltima.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                    estiloCeldaColumnaPrimeraFilaUltima.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                    estiloCeldaColumnaPrimeraFilaUltima.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                    estiloCeldaColumnaPrimeraFilaUltima.setFont(normal);                        
                    
                        //celdas segunda y tercera columna (antes de totales) -> 3 Estilos
                    HSSFCellStyle estiloCeldaColumnasDosyTresFilaPrimera = libro.createCellStyle();
                    estiloCeldaColumnasDosyTresFilaPrimera.setWrapText(true);
                    estiloCeldaColumnasDosyTresFilaPrimera.setBorderLeft(HSSFCellStyle.BORDER_THIN);
                    estiloCeldaColumnasDosyTresFilaPrimera.setBorderTop(HSSFCellStyle.BORDER_THICK);
                    estiloCeldaColumnasDosyTresFilaPrimera.setFont(normal);
                    estiloCeldaColumnasDosyTresFilaPrimera.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                    
                    HSSFCellStyle estiloCeldaColumnasDosyTresFilaIntermedia = libro.createCellStyle();
                    estiloCeldaColumnasDosyTresFilaIntermedia.setWrapText(true);
                    estiloCeldaColumnasDosyTresFilaIntermedia.setBorderLeft(HSSFCellStyle.BORDER_THIN);                    
                    estiloCeldaColumnasDosyTresFilaIntermedia.setFont(normal);
                    estiloCeldaColumnasDosyTresFilaIntermedia.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                    
                    HSSFCellStyle estiloCeldaColumnasDosyTresFilaUltima = libro.createCellStyle();
                    estiloCeldaColumnasDosyTresFilaUltima.setWrapText(true);
                    estiloCeldaColumnasDosyTresFilaUltima.setBorderLeft(HSSFCellStyle.BORDER_THIN);
                    estiloCeldaColumnasDosyTresFilaUltima.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                    estiloCeldaColumnasDosyTresFilaUltima.setFont(normal);
                    estiloCeldaColumnasDosyTresFilaUltima.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                    
                        //celdas siguientes Texto (pueden tener totales) ->  3 Estilos
                    HSSFCellStyle estiloCeldaTextoFilaPrimera = libro.createCellStyle();
                    estiloCeldaTextoFilaPrimera.setWrapText(true);
                    estiloCeldaTextoFilaPrimera.setBorderLeft(HSSFCellStyle.BORDER_THIN);
                    estiloCeldaTextoFilaPrimera.setBorderTop(HSSFCellStyle.BORDER_THICK);
                    estiloCeldaTextoFilaPrimera.setFont(normal);
                    estiloCeldaTextoFilaPrimera.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                    
                    HSSFCellStyle estiloCeldaTextoFilaIntermedia = libro.createCellStyle();
                    estiloCeldaTextoFilaIntermedia.setWrapText(true);
                    estiloCeldaTextoFilaIntermedia.setBorderLeft(HSSFCellStyle.BORDER_THIN);
                    estiloCeldaTextoFilaIntermedia.setBorderTop(HSSFCellStyle.BORDER_THIN);
                    estiloCeldaTextoFilaIntermedia.setFont(normal);
                    estiloCeldaTextoFilaIntermedia.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                    
                    HSSFCellStyle estiloCeldaTextoFilaUltima = libro.createCellStyle();
                    estiloCeldaTextoFilaUltima.setWrapText(true);
                    estiloCeldaTextoFilaUltima.setBorderLeft(HSSFCellStyle.BORDER_THIN);
                    estiloCeldaTextoFilaUltima.setBorderTop(HSSFCellStyle.BORDER_DOUBLE);
                    estiloCeldaTextoFilaUltima.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                    estiloCeldaTextoFilaUltima.setFont(negrita);
                    estiloCeldaTextoFilaUltima.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                     
                     //celdas siguientes Numericos (pueden tener totales) ->  3 Estilos
                    HSSFCellStyle estiloCeldaNumericoFilaPrimera = libro.createCellStyle();
                    estiloCeldaNumericoFilaPrimera.setWrapText(true);
                    estiloCeldaNumericoFilaPrimera.setBorderLeft(HSSFCellStyle.BORDER_THIN);
                    estiloCeldaNumericoFilaPrimera.setBorderTop(HSSFCellStyle.BORDER_THICK);
                    estiloCeldaNumericoFilaPrimera.setFont(normal);
                    estiloCeldaNumericoFilaPrimera.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
                    estiloCeldaNumericoFilaPrimera.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                    
                    HSSFCellStyle estiloCeldaNumericoFilaIntermedia = libro.createCellStyle();
                    estiloCeldaNumericoFilaIntermedia.setWrapText(true);
                    estiloCeldaNumericoFilaIntermedia.setBorderLeft(HSSFCellStyle.BORDER_THIN);
                    estiloCeldaNumericoFilaIntermedia.setBorderTop(HSSFCellStyle.BORDER_THIN);
                    estiloCeldaNumericoFilaIntermedia.setFont(normal);
                    estiloCeldaNumericoFilaIntermedia.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
                    estiloCeldaNumericoFilaIntermedia.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                    
                    HSSFCellStyle estiloCeldaNumericoFilaUltima = libro.createCellStyle();
                    estiloCeldaNumericoFilaUltima.setWrapText(true);
                    estiloCeldaNumericoFilaUltima.setBorderLeft(HSSFCellStyle.BORDER_THIN);
                    estiloCeldaNumericoFilaUltima.setBorderTop(HSSFCellStyle.BORDER_DOUBLE);
                    estiloCeldaNumericoFilaUltima.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                    estiloCeldaNumericoFilaUltima.setFont(negrita);
                    estiloCeldaNumericoFilaUltima.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
                    estiloCeldaNumericoFilaUltima.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                    
                    //celdas ultima columna -> 3 Estilos
                    HSSFCellStyle estiloCeldaColumnaUltimaFilaPrimera = libro.createCellStyle();
                    estiloCeldaColumnaUltimaFilaPrimera.setWrapText(true);
                    estiloCeldaColumnaUltimaFilaPrimera.setBorderLeft(HSSFCellStyle.BORDER_THIN);
                    estiloCeldaColumnaUltimaFilaPrimera.setBorderRight(HSSFCellStyle.BORDER_THICK);                                          
                    estiloCeldaColumnaUltimaFilaPrimera.setBorderTop(HSSFCellStyle.BORDER_THICK);
                    estiloCeldaColumnaUltimaFilaPrimera.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                    estiloCeldaColumnaUltimaFilaPrimera.setFont(normal); 
                    estiloCeldaColumnaUltimaFilaPrimera.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
                        
                    HSSFCellStyle estiloCeldaColumnaUltimaFilaIntermedia = libro.createCellStyle();
                    estiloCeldaColumnaUltimaFilaIntermedia.setWrapText(true);
                    estiloCeldaColumnaUltimaFilaIntermedia.setBorderLeft(HSSFCellStyle.BORDER_THIN);
                    estiloCeldaColumnaUltimaFilaIntermedia.setBorderRight(HSSFCellStyle.BORDER_THICK);
                    estiloCeldaColumnaUltimaFilaIntermedia.setBorderTop(HSSFCellStyle.BORDER_THIN);
                    estiloCeldaColumnaUltimaFilaIntermedia.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                    estiloCeldaColumnaUltimaFilaIntermedia.setFont(normal);
                    estiloCeldaColumnaUltimaFilaIntermedia.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
                        
                    HSSFCellStyle estiloCeldaColumnaUltimaFilaUltima = libro.createCellStyle();
                    estiloCeldaColumnaUltimaFilaUltima.setWrapText(true);
                    estiloCeldaColumnaUltimaFilaUltima.setBorderLeft(HSSFCellStyle.BORDER_THIN);
                    estiloCeldaColumnaUltimaFilaUltima.setBorderRight(HSSFCellStyle.BORDER_THICK);
                    estiloCeldaColumnaUltimaFilaUltima.setBorderTop(HSSFCellStyle.BORDER_DOUBLE);
                    estiloCeldaColumnaUltimaFilaUltima.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                    estiloCeldaColumnaUltimaFilaUltima.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                    estiloCeldaColumnaUltimaFilaUltima.setFont(negrita);
                    estiloCeldaColumnaUltimaFilaUltima.setAlignment(HSSFCellStyle.ALIGN_RIGHT); 
                    

                    boolean nuevo = true;
                    HashMap<String, String> datosTercero = null;
                    String empresa = null;
                    String territorio = null;
                    String pais = null;
                    List<FilaInformeResumenEconomicoVO> listaPuestos = null;
                    int n = 0;
                    int p = 0;
                    int numPaises = 0;
                    int height = 0;
                    
                    Double solicitadoTotal=0.00;
                    Double subTot=0.00;
                    Double gastosTra=0.00;
                    Double salarioAnualBruto=0.00;
                    Double dietasMan=0.00;
                    Double totalMinimis=0.00;

                    String numExp = null;

                    listaPuestos = manager.getDatosInformeResumenEconomico(ano, adapt);


                    //Insertamos los datos, fila a fila
                    for(FilaInformeResumenEconomicoVO filaI : listaPuestos)
                    {
                        if(filaI.getNumExpediente() != null && !filaI.getNumExpediente().equals(numExp))
                        {
                            if(p != 0)
                            {
                                //poi alpha
                                /*hoja.addMergedRegion(new Region(numFila-p, 0, numFila, 0));
                                hoja.addMergedRegion(new Region(numFila-p, 1, numFila, 1));
                                hoja.addMergedRegion(new Region(numFila-p, 2, numFila, 2));
                                hoja.addMergedRegion(new Region(numFila-p, 3, numFila-1, 3));*/
                                //poi 3.10
                                hoja.addMergedRegion(new CellRangeAddress(numFila-p,  numFila, 0, 0));
                                hoja.addMergedRegion(new CellRangeAddress(numFila-p,  numFila, 1, 1));
                                hoja.addMergedRegion(new CellRangeAddress(numFila-p,  numFila, 2, 2));
                                hoja.addMergedRegion(new CellRangeAddress(numFila-p,  numFila-1, 3, 3));

                            }
                            numExp = filaI.getNumExpediente();
                            nuevo = true;
                            p = 0;
                            n++;
                            numFila++;

                        }
                        else
                        {
                            numFila++;   

                            nuevo = false;
                            p++;
                        }

                        fila = hoja.createRow(numFila);

                        numPaises = 0;

                        if(filaI.getPais1() != null)
                        {
                            numPaises++;
                        }
                        if(filaI.getPais2() != null)
                        {
                            numPaises++;
                        }
                        if(filaI.getPais3() != null)
                        {
                            numPaises++;
                        }

                        height = 0;
                        height = Math.max(numPaises, 1);
                        height = height * fila.getHeight();

                        fila.setHeight((short)height);
                        fila2.setHeight((short)height);

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

                        //COLUMNA: NUM EXPEDIENTE
                        celda = fila.createCell(0);
                        if(nuevo) {
                            celda.setCellStyle(estiloCeldaColumnaPrimeraFilaPrimera);                            
                        } else if(numFila == listaPuestos.size()) {
                            celda.setCellStyle(estiloCeldaColumnaPrimeraFilaUltima);                            
                        } else {
                            celda.setCellStyle(estiloCeldaColumnaPrimeraFilaIntermedia);
                        }                        
                        
                        if(nuevo)
                        {
                            celda.setCellValue(numExp);
                        }

                        //COLUMNA: ENTIDAD
                        if(filaI.getEntidad() != null)
                        {
                            empresa = filaI.getEntidad();
                        }
                        else
                        {
                            datosTercero = MeLanbide46Manager.getInstance().getDatosTercero(codOrganizacion, numExp, String.valueOf(ano), ConstantesMeLanbide46.CODIGO_ROL_ENTIDAD_SOLICITANTE, adapt);
                            if(datosTercero.containsKey("TER_NOC"))
                            {
                                empresa = datosTercero.get("TER_NOC");
                            }
                            else
                            {
                                empresa = "";
                            }
                        }
                        celda = fila.createCell(1);
                        if(nuevo) {
                            celda.setCellStyle(estiloCeldaColumnasDosyTresFilaPrimera);
                        } else if(numFila == listaPuestos.size()) {
                            celda.setCellStyle(estiloCeldaColumnasDosyTresFilaUltima);
                        } else {
                            celda.setCellStyle(estiloCeldaColumnasDosyTresFilaIntermedia);
                        }                       
                        
                        if(nuevo)
                        {
                            celda.setCellValue(empresa != null ? empresa.toUpperCase() : "");
                        }

                        //COLUMNA: TERRITORIO HISTORICO
                        celda = fila.createCell(2);
                        if(nuevo) {
                            celda.setCellStyle(estiloCeldaColumnasDosyTresFilaPrimera);
                        } else if(numFila == listaPuestos.size()) {
                            celda.setCellStyle(estiloCeldaColumnasDosyTresFilaUltima);
                        } else {
                            celda.setCellStyle(estiloCeldaColumnasDosyTresFilaIntermedia);
                        }
                        if(nuevo)
                        {
                            territorio = filaI.getTerritorioHistorico() != null ? filaI.getTerritorioHistorico().toUpperCase() : "";
                            celda.setCellValue(territorio);
                        }

                        //COLUMNA: CNAE09
                        celda = fila.createCell(3);
                        if(nuevo)
                        {
                            celda.setCellStyle(estiloCeldaTextoFilaPrimera);                            
                        }
                        else if(filaI.getOrden() != null && filaI.getOrden() == 2)
                        {
                            celda.setCellStyle(estiloCeldaTextoFilaUltima);                            
                        }
                        else if(p > 0)
                        {
                            celda.setCellStyle(estiloCeldaTextoFilaIntermedia);                            
                        }
                        String valor = filaI.getCnae09() != null ? filaI.getCnae09().toUpperCase() : "";
                        if(!valor.equals(""))
                            valor = valor.replaceAll("TOTAL", meLanbide46I18n.getMensaje(idioma, "doc.informeEconomico.total").toUpperCase());
                        if(valor.equals(meLanbide46I18n.getMensaje(idioma, "doc.informeEconomico.total").toUpperCase()) || nuevo)
                        {
                            celda.setCellValue(valor);
                        }

                        //COLUMNA: Nº PUESTOS SOLIC
                        celda = fila.createCell(4);
                        if(nuevo)
                        {
                            celda.setCellStyle(estiloCeldaNumericoFilaPrimera);                            
                        }
                        else if(filaI.getOrden() != null && filaI.getOrden() == 2)
                        {
                            celda.setCellStyle(estiloCeldaNumericoFilaUltima);                            
                        }
                        else if(p > 0)
                        {
                            celda.setCellStyle(estiloCeldaNumericoFilaIntermedia);                            
                        }                        
                        
                        celda.setCellValue(filaI.getnPuestosSolicitados() != null ? filaI.getnPuestosSolicitados() : 0);
                        celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);

                        //COLUMNA: PUESTO
                        celda = fila.createCell(5);
                        if(nuevo)
                        {
                            celda.setCellStyle(estiloCeldaTextoFilaPrimera);                            
                        }
                        else if(filaI.getOrden() != null && filaI.getOrden() == 2)
                        {
                            celda.setCellStyle(estiloCeldaTextoFilaUltima);                            
                        }
                        else if(p > 0)
                        {
                            celda.setCellStyle(estiloCeldaTextoFilaIntermedia);                            
                        }
                        
                        celda.setCellValue(filaI.getPuesto() != null ? filaI.getCodPuesto()+"-"+filaI.getPuesto().toUpperCase() : "");

                        //COLUMNA: RESULTADO
                        celda = fila.createCell(6);
                        if(nuevo)
                        {
                            celda.setCellStyle(estiloCeldaTextoFilaPrimera);                            
                        }
                        else if(filaI.getOrden() != null && filaI.getOrden() == 2)
                        {
                            celda.setCellStyle(estiloCeldaTextoFilaUltima);                            
                        }
                        else if(p > 0)
                        {
                            celda.setCellStyle(estiloCeldaTextoFilaIntermedia);                            
                        }
                        celda.setCellValue(filaI.getResultado() != null ? filaI.getResultado() : "");

                        //COLUMNA: FECHA INI
                        celda = fila.createCell(7);
                        if(nuevo)
                        {
                            celda.setCellStyle(estiloCeldaTextoFilaPrimera);                            
                        }
                        else if(filaI.getOrden() != null && filaI.getOrden() == 2)
                        {
                            celda.setCellStyle(estiloCeldaTextoFilaUltima);                            
                        }
                        else if(p > 0)
                        {
                            celda.setCellStyle(estiloCeldaTextoFilaIntermedia);                            
                        }
                        celda.setCellValue(filaI.getFecIni() != null ? filaI.getFecIni() : "");

                        //COLUMNA: FECHA FIN
                        celda = fila.createCell(8);
                        if(nuevo)
                        {
                            celda.setCellStyle(estiloCeldaTextoFilaPrimera);                            
                        }
                        else if(filaI.getOrden() != null && filaI.getOrden() == 2)
                        {
                            celda.setCellStyle(estiloCeldaTextoFilaUltima);                            
                        }
                        else if(p > 0)
                        {
                            celda.setCellStyle(estiloCeldaTextoFilaIntermedia);                            
                        }
                        celda.setCellValue(filaI.getFecFin() != null ? filaI.getFecFin() : "");

                        //COLUMNA: PERIODO MESES SUBV
                        celda = fila.createCell(9);
                        if(nuevo)
                        {
                            celda.setCellStyle(estiloCeldaNumericoFilaPrimera);                            
                        }
                        else if(filaI.getOrden() != null && filaI.getOrden() == 2)
                        {
                            celda.setCellStyle(estiloCeldaNumericoFilaUltima);                            
                        }
                        else if(p > 0)
                        {
                            celda.setCellStyle(estiloCeldaNumericoFilaIntermedia);                            
                        }                        
                        
                        if (filaI.getOrden()!=2) {
                            celda.setCellValue(filaI.getPeriodoMesesSubv() != null ? filaI.getPeriodoMesesSubv() : 0);                    
                            celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                        }

                        //COLUMNA: RANGO EDAD
                        celda = fila.createCell(10);
                        if(nuevo)
                        {
                            celda.setCellStyle(estiloCeldaTextoFilaPrimera);                            
                        }
                        else if(filaI.getOrden() != null && filaI.getOrden() == 2)
                        {
                            celda.setCellStyle(estiloCeldaTextoFilaUltima);                            
                        }
                        else if(p > 0)
                        {
                            celda.setCellStyle(estiloCeldaTextoFilaIntermedia);                            
                        }
                        celda.setCellValue(filaI.getTitulacionAB() != null ? filaI.getTitulacionAB() : ""); 

                        //COLUMNA: PAIS
                       celda = fila.createCell(11);
                        if(nuevo)
                        {
                            celda.setCellStyle(estiloCeldaTextoFilaPrimera);                            
                        }
                        else if(filaI.getOrden() != null && filaI.getOrden() == 2)
                        {
                            celda.setCellStyle(estiloCeldaTextoFilaUltima);                            
                        }
                        else if(p > 0)
                        {
                            celda.setCellStyle(estiloCeldaTextoFilaIntermedia);                            
                        }

                        pais = filaI.getPais1() != null ? filaI.getPais1().toUpperCase() : "";
                        pais += filaI.getPais2() != null && !filaI.getPais2().equals("") ? (pais != null && !pais.equals("") ? System.getProperty("line.separator") : "") : "";
                        pais += filaI.getPais2() != null && !filaI.getPais2().equals("") ? filaI.getPais2().toUpperCase() : "";
                        pais += filaI.getPais3() != null && !filaI.getPais3().equals("") ? (pais != null && !pais.equals("") ? System.getProperty("line.separator") : "") : "";
                        pais += filaI.getPais3() != null && !filaI.getPais3().equals("") ? filaI.getPais3().toUpperCase() : "";

                        celda.setCellValue(pais);

                        //COLUMNA: COSTES CONTRATO SALARIO
                        celda = fila.createCell(12);
                        if(nuevo)
                        {
                            celda.setCellStyle(estiloCeldaNumericoFilaPrimera);                            
                        }
                        else if(filaI.getOrden() != null && filaI.getOrden() == 2)
                        {
                            celda.setCellStyle(estiloCeldaNumericoFilaUltima);                            
                        }
                        else if(p > 0)
                        {
                            celda.setCellStyle(estiloCeldaNumericoFilaIntermedia);                            
                        }
                        if (filaI.getOrden()!=2) {
                            celda.setCellValue(filaI.getCostesContSalarioSS() != null ? MeLanbide46Utils.redondearDecimalesBigDecimal(filaI.getCostesContSalarioSS(), 2).doubleValue() : new BigDecimal(ConstantesMeLanbide46.CERO_CON_DECIMALES).doubleValue());
                            celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                        }

                        //COLUMNA: DIETAS CONVENIO
                        celda = fila.createCell(13);
                        if(nuevo)
                        {
                            celda.setCellStyle(estiloCeldaNumericoFilaPrimera);                            
                        }
                        else if(filaI.getOrden() != null && filaI.getOrden() == 2)
                        {
                            celda.setCellStyle(estiloCeldaNumericoFilaUltima);                            
                        }
                        else if(p > 0)
                        {
                            celda.setCellStyle(estiloCeldaNumericoFilaIntermedia);                            
                        }
                        if (filaI.getOrden()!=2) {
                            celda.setCellValue(filaI.getCostesContDietasConve() != null ? MeLanbide46Utils.redondearDecimalesBigDecimal(filaI.getCostesContDietasConve(), 2).doubleValue() : new BigDecimal(ConstantesMeLanbide46.CERO_CON_DECIMALES).doubleValue());
                            celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                        }

                        //COLUMNA: COSTES CONTRATACION
                        celda = fila.createCell(14);
                        if(nuevo)
                        {
                            celda.setCellStyle(estiloCeldaNumericoFilaPrimera);                            
                        }
                        else if(filaI.getOrden() != null && filaI.getOrden() == 2)
                        {
                            celda.setCellStyle(estiloCeldaNumericoFilaUltima);                            
                        }
                        else if(p > 0)
                        {
                            celda.setCellStyle(estiloCeldaNumericoFilaIntermedia);                            
                        }
                        celda.setCellValue(filaI.getCostesCont() != null ? MeLanbide46Utils.redondearDecimalesBigDecimal(filaI.getCostesCont(), 2).doubleValue() : new BigDecimal(ConstantesMeLanbide46.CERO_CON_DECIMALES).doubleValue());
                        celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);

                        //COLUMNA: SALARIO ANUAL BRUTO
                        celda = fila.createCell(15);
                        if(nuevo)
                        {
                            celda.setCellStyle(estiloCeldaNumericoFilaPrimera);                            
                        }
                        else if(filaI.getOrden() != null && filaI.getOrden() == 2)
                        {
                            celda.setCellStyle(estiloCeldaNumericoFilaUltima);                            
                        }
                        else if(p > 0)
                        {
                            celda.setCellStyle(estiloCeldaNumericoFilaIntermedia);                            
                        }
                        if (filaI.getOrden()!=2) {
                            celda.setCellValue(filaI.getSolSalarioSS() != null ? MeLanbide46Utils.redondearDecimalesBigDecimal(filaI.getSolSalarioSS(), 2).doubleValue() : new BigDecimal(ConstantesMeLanbide46.CERO_CON_DECIMALES).doubleValue());
                            celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                        }
                        salarioAnualBruto=filaI.getSolSalarioSS() != null ? MeLanbide46Utils.redondearDecimalesBigDecimal(filaI.getSolSalarioSS(), 2).doubleValue() : new BigDecimal(ConstantesMeLanbide46.CERO_CON_DECIMALES).doubleValue();
                        
                        //COLUMNA: DIETAS ALOJAMIENTO Y MANUTENCION
                        celda = fila.createCell(16);
                        if(nuevo)
                        {
                            celda.setCellStyle(estiloCeldaNumericoFilaPrimera);                            
                        }
                        else if(filaI.getOrden() != null && filaI.getOrden() == 2)
                        {
                            celda.setCellStyle(estiloCeldaNumericoFilaUltima);                            
                        }
                        else if(p > 0)
                        {
                            celda.setCellStyle(estiloCeldaNumericoFilaIntermedia);                            
                        }
                        if (filaI.getOrden()!=2) {
                            celda.setCellValue(filaI.getSolDietasConvSS() != null ? MeLanbide46Utils.redondearDecimalesBigDecimal(filaI.getSolDietasConvSS(), 2).doubleValue() : new BigDecimal(ConstantesMeLanbide46.CERO_CON_DECIMALES).doubleValue());
                            celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                        }
                        dietasMan=filaI.getSolDietasConvSS() != null ? MeLanbide46Utils.redondearDecimalesBigDecimal(filaI.getSolDietasConvSS(), 2).doubleValue() : new BigDecimal(ConstantesMeLanbide46.CERO_CON_DECIMALES).doubleValue();
                        
                        //COLUMNA: TRAMITACION DE PERMISOS Y SEGUROS MEDICOS
                        celda = fila.createCell(17);
                        if(nuevo)
                        {
                            celda.setCellStyle(estiloCeldaNumericoFilaPrimera);                            
                        }
                        else if(filaI.getOrden() != null && filaI.getOrden() == 2)
                        {
                            celda.setCellStyle(estiloCeldaNumericoFilaUltima);                            
                        }
                        else if(p > 0)
                        {
                            celda.setCellStyle(estiloCeldaNumericoFilaIntermedia);                            
                        }
                        if (filaI.getOrden()!=2) {
                            celda.setCellValue(filaI.getSolGastosTra() != null ? MeLanbide46Utils.redondearDecimalesBigDecimal(filaI.getSolGastosTra(), 2).doubleValue() : new BigDecimal(ConstantesMeLanbide46.CERO_CON_DECIMALES).doubleValue());
                            celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                        }
                        gastosTra=filaI.getSolGastosTra() != null ? MeLanbide46Utils.redondearDecimalesBigDecimal(filaI.getSolGastosTra(), 2).doubleValue() : new BigDecimal(ConstantesMeLanbide46.CERO_CON_DECIMALES).doubleValue();
                        
                        //COLUMNA: IMPORTE SOLICITADO
                        solicitadoTotal=gastosTra+salarioAnualBruto+dietasMan;
                        celda = fila.createCell(18);
                        if(nuevo)
                        {
                            celda.setCellStyle(estiloCeldaNumericoFilaPrimera);
                            subTot = solicitadoTotal;
                        }
                        else if(filaI.getOrden() != null && filaI.getOrden() == 2)
                        {
                            celda.setCellStyle(estiloCeldaNumericoFilaUltima);
                            subTot = subTot + solicitadoTotal;
                        }
                        else if(p > 0)
                        {
                            celda.setCellStyle(estiloCeldaNumericoFilaIntermedia);
                            subTot = subTot + solicitadoTotal;
                        }
                        //celda.setCellValue(filaI.getSolicitado() != null ? MeLanbide46Utils.redondearDecimalesBigDecimal(filaI.getSolicitado(), 2).doubleValue() : new BigDecimal(ConstantesMeLanbide46.CERO_CON_DECIMALES).doubleValue());
                        //celda.setCellValue(filaI.getSolicitado() != null ? solicitadoTotal : new BigDecimal(ConstantesMeLanbide46.CERO_CON_DECIMALES).doubleValue());
                        celda.setCellValue(filaI.getOrden() != 2 ? solicitadoTotal : subTot);
                        //celda.setCellValue(solicitadoTotal);
                        celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);

                        //COLUMNA: DOTACION ANUAL MAXIMA
                        celda = fila.createCell(19);
                        if(nuevo)
                        {
                            celda.setCellStyle(estiloCeldaNumericoFilaPrimera);                            
                        }
                        else if(filaI.getOrden() != null && filaI.getOrden() == 2)
                        {
                            celda.setCellStyle(estiloCeldaNumericoFilaUltima);                            
                        }
                        else if(p > 0)
                        {
                            celda.setCellStyle(estiloCeldaNumericoFilaIntermedia);                            
                        }
                        if (filaI.getOrden()!=2) {
                            celda.setCellValue(filaI.getDotacionAnualMaxima() != null ? MeLanbide46Utils.redondearDecimalesBigDecimal(filaI.getDotacionAnualMaxima(), 2).doubleValue() : new BigDecimal(ConstantesMeLanbide46.CERO_CON_DECIMALES).doubleValue());
                            celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                        }

                        //COLUMNA: MESES SUBVENCIONABLES
                        celda = fila.createCell(20);
                        if(nuevo)
                        {
                            celda.setCellStyle(estiloCeldaNumericoFilaPrimera);                            
                        }
                        else if(filaI.getOrden() != null && filaI.getOrden() == 2)
                        {
                            celda.setCellStyle(estiloCeldaNumericoFilaUltima);                            
                        }
                        else if(p > 0)
                        {
                            celda.setCellStyle(estiloCeldaNumericoFilaIntermedia);                            
                        }
                        if (filaI.getOrden()!=2) {
                            celda.setCellValue(filaI.getMesesSubvencionables() != null ? filaI.getMesesSubvencionables() : 0);
                            celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                        }

                        //COLUMNA: DIETAS SUBVENCIONABLES
                        celda = fila.createCell(21);
                        if(nuevo)
                        {
                            celda.setCellStyle(estiloCeldaNumericoFilaPrimera);                            
                        }
                        else if(filaI.getOrden() != null && filaI.getOrden() == 2)
                        {
                            celda.setCellStyle(estiloCeldaNumericoFilaUltima);                            
                        }
                        else if(p > 0)
                        {
                            celda.setCellStyle(estiloCeldaNumericoFilaIntermedia);                            
                        }
                        if (filaI.getOrden()!=2) {
                            celda.setCellValue(filaI.getDietasSubv() != null ? MeLanbide46Utils.redondearDecimalesBigDecimal(filaI.getDietasSubv(), 2).doubleValue() : new BigDecimal(ConstantesMeLanbide46.CERO_CON_DECIMALES).doubleValue());
                            celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                        }

                        //COLUMNA: TRAMITACION DE PERMISOS Y SEGUROS MEDICOS MAX 450
                        celda = fila.createCell(22);
                        if(nuevo)
                        {
                            celda.setCellStyle(estiloCeldaNumericoFilaPrimera);                            
                        }
                        else if(filaI.getOrden() != null && filaI.getOrden() == 2)
                        {
                            celda.setCellStyle(estiloCeldaNumericoFilaUltima);                            
                        }
                        else if(p > 0)
                        {
                            celda.setCellStyle(estiloCeldaNumericoFilaIntermedia);                            
                        }
                        if (filaI.getOrden()!=2) {
                            celda.setCellValue(filaI.getGastosTram() != null ? MeLanbide46Utils.redondearDecimalesBigDecimal(filaI.getGastosTram(), 2).doubleValue() : new BigDecimal(ConstantesMeLanbide46.CERO_CON_DECIMALES).doubleValue());
                            celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                        }

                        //COLUMNA: MINIMIS CONCEDIDOS
                        celda = fila.createCell(23);
                        int colMinimis =23;
                        if(nuevo)
                        {
                            celda.setCellStyle(estiloCeldaNumericoFilaPrimera);                            
                        }
                        else if(filaI.getOrden() != null && filaI.getOrden() == 2)
                        {
                            celda.setCellStyle(estiloCeldaNumericoFilaUltima);                            
                        }
                        else if(p > 0)
                        {
                            celda.setCellStyle(estiloCeldaNumericoFilaIntermedia);                            
                        }
                        
                        if(nuevo) {
                            totalMinimis = filaI.getMinimisConcedidos() != null ? MeLanbide46Utils.redondearDecimalesBigDecimal(filaI.getMinimisConcedidos(), 2).doubleValue() : new BigDecimal(ConstantesMeLanbide46.CERO_CON_DECIMALES).doubleValue();
                            celda.setCellValue(totalMinimis);                            
                        }
                        if (filaI.getOrden()!=2) {
                            celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC); 
                        }

                        //COLUMNA: IMPORTE MAX SUBVENCIONABLE
                        celda = fila.createCell(24);
                        if(nuevo)
                        {
                            celda.setCellStyle(estiloCeldaNumericoFilaPrimera);                            
                        }
                        else if(filaI.getOrden() != null && filaI.getOrden() == 2)
                        {
                            celda.setCellStyle(estiloCeldaNumericoFilaUltima);                            
                        }
                        else if(p > 0)
                        {
                            celda.setCellStyle(estiloCeldaNumericoFilaIntermedia);                            
                        }
                        celda.setCellValue(filaI.getImpMaxSubvencionable() != null ? MeLanbide46Utils.redondearDecimalesBigDecimal(filaI.getImpMaxSubvencionable(), 2).doubleValue() : new BigDecimal(ConstantesMeLanbide46.CERO_CON_DECIMALES).doubleValue());
                        celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);

                        //COLUMNA: IMPORTE CONCEDIDO
                        celda = fila.createCell(25);
                        if(nuevo)
                        {
                            celda.setCellStyle(estiloCeldaNumericoFilaPrimera);                            
                        }
                        else if(filaI.getOrden() != null && filaI.getOrden() == 2)
                        {
                            celda.setCellStyle(estiloCeldaNumericoFilaUltima);                            
                        }
                        else if(p > 0)
                        {
                            celda.setCellStyle(estiloCeldaNumericoFilaIntermedia);                            
                        }
                        celda.setCellValue(filaI.getImporteConcedido() != null ? MeLanbide46Utils.redondearDecimalesBigDecimal(filaI.getImporteConcedido(), 2).doubleValue() : new BigDecimal(ConstantesMeLanbide46.CERO_CON_DECIMALES).doubleValue());
                        celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);

                        //COLUMNA: NUM RENUNCIAS
                        celda = fila.createCell(26);
                        if(nuevo)
                        {
                            celda.setCellStyle(estiloCeldaNumericoFilaPrimera);                            
                        }
                        else if(filaI.getOrden() != null && filaI.getOrden() == 2)
                        {
                            celda.setCellStyle(estiloCeldaNumericoFilaUltima);                            
                        }
                        else if(p > 0)
                        {
                            celda.setCellStyle(estiloCeldaNumericoFilaIntermedia);                            
                        }
                        if (filaI.getOrden()!=2) {
                            celda.setCellValue(filaI.getNumRenuncias() != null ? filaI.getNumRenuncias() : 0);
                            celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                        }

                        //COLUMNA: IMPORTE RENUNCIA
                        celda = fila.createCell(27);
                        if(nuevo)
                        {
                            celda.setCellStyle(estiloCeldaNumericoFilaPrimera);                            
                        }
                        else if(filaI.getOrden() != null && filaI.getOrden() == 2)
                        {
                            celda.setCellStyle(estiloCeldaNumericoFilaUltima);                            
                        }
                        else if(p > 0)
                        {
                            celda.setCellStyle(estiloCeldaNumericoFilaIntermedia);                            
                        }
                        celda.setCellValue(filaI.getImporteRenuncia() != null ? MeLanbide46Utils.redondearDecimalesBigDecimal(filaI.getImporteRenuncia(), 2).doubleValue() : new BigDecimal(ConstantesMeLanbide46.CERO_CON_DECIMALES).doubleValue());
                        celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);

                        //COLUMNA: MOTIVO RENUNCIA
                        celda = fila.createCell(28);
                        if(nuevo)
                        {
                            celda.setCellStyle(estiloCeldaTextoFilaPrimera);                            
                        }
                        else if(filaI.getOrden() != null && filaI.getOrden() == 2)
                        {
                            celda.setCellStyle(estiloCeldaTextoFilaUltima);                            
                        }
                        else if(p > 0)
                        {
                            celda.setCellStyle(estiloCeldaTextoFilaIntermedia);                            
                        }
                        valor = filaI.getMotivo() != null ? filaI.getMotivo().toUpperCase() : "";
                        if(valor != null && !valor.equals(""))
                        {
                            valor = valor.replaceAll("-OBSERVACIONES:", " - "+meLanbide46I18n.getMensaje(idioma, "doc.informeEconomico.observaciones").toUpperCase()+":");
                        }
                        celda.setCellValue(valor);

                        //COLUMNA: IMPORTE CONCEDIDO TRAS RESOLUCION MODIF
                        celda = fila.createCell(29);
                        if(nuevo)
                        {
                            celda.setCellStyle(estiloCeldaColumnaUltimaFilaPrimera);
                        }
                        else if(filaI.getOrden() != null && filaI.getOrden() == 2)
                        {
                            celda.setCellStyle(estiloCeldaColumnaUltimaFilaUltima);
                        }
                        else if(p > 0)
                        {
                            celda.setCellStyle(estiloCeldaColumnaUltimaFilaIntermedia);                            
                        }
                        /*if(numFila == listaPuestos.size())
                        {
                            celda.setCellStyle(estiloCeldaColumnaUltimaFilaIntermedia);
                        }*/
                        
                        
                        celda.setCellValue(filaI.getImpConcedidoTrasResol() != null ? MeLanbide46Utils.redondearDecimalesBigDecimal(filaI.getImpConcedidoTrasResol(), 2).doubleValue() : new BigDecimal(ConstantesMeLanbide46.CERO_CON_DECIMALES).doubleValue());
                        celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);

                        if(!"TOTAL".equals(filaI.getCnae09()))
                            numFila2++;
                            fila2 = hoja2.createRow(numFila2);
                            crearDatosHoja2Economico(libro, fila2, fila, normal, numExp, empresa, territorio, colMinimis, totalMinimis); 
                    }

                    //Esto añade un merged region a la ultima fila, columnas de num expediente, entidad y territorio historico
                    if(p != 0)
                    {
                        //poi alpha
                        /*hoja.addMergedRegion(new Region(numFila-p, 0, numFila, 0));
                        hoja.addMergedRegion(new Region(numFila-p, 1, numFila, 1));
                        hoja.addMergedRegion(new Region(numFila-p, 2, numFila, 2));
                        hoja.addMergedRegion(new Region(numFila-p, 3, numFila-1, 3));*/
                        //poi 3.10
                        hoja.addMergedRegion(new CellRangeAddress(numFila-p,  numFila, 0, 0));
                        hoja.addMergedRegion(new CellRangeAddress(numFila-p,  numFila, 1, 1));
                        hoja.addMergedRegion(new CellRangeAddress(numFila-p,  numFila, 2, 2));
                        hoja.addMergedRegion(new CellRangeAddress(numFila-p,  numFila-1, 3, 3));
                        hoja.addMergedRegion(new CellRangeAddress(numFila-p,  numFila-1, 23, 23));

                        estiloCelda = libro.createCellStyle();
                        estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                        estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                        ////
                        estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                        estiloCelda.setWrapText(true);
                        estiloCelda.setFont(normal);

                        hoja.getRow(numFila-p).getCell(0).setCellStyle(estiloCelda);

                        estiloCelda = libro.createCellStyle();
                        estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                        estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THIN);
                        ////
                        estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                        estiloCelda.setWrapText(true);
                        estiloCelda.setFont(normal);

                        hoja.getRow(numFila-p).getCell(1).setCellStyle(estiloCelda);
                        
                        hoja.getRow(numFila-p).getCell(23).setCellStyle(estiloCelda);
                    }

                    //se escribe el fichero
                    /*File directorioTemp = new File(m_Conf.getString("PDF.base_dir"));
                    File informe = File.createTempFile("resumenEconomico", ".xls", directorioTemp);

                    FileOutputStream archivoSalida = new FileOutputStream(informe);*/
                    libro.write(archivoSalida);
                    archivoSalida.close();

                    rutaArchivoSalida = informe.getAbsolutePath();

                    FileInputStream istr = new FileInputStream(rutaArchivoSalida);

                    BufferedInputStream bstr = new BufferedInputStream( istr ); // promote

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
                }
                catch (Exception ioe) 
                {
                    ioe.printStackTrace();
                }
            }
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }   
    
    
    private void crearDatosHoja2Economico(HSSFWorkbook libro, HSSFRow filaHoja1, HSSFRow filaHoja0, HSSFFont normal, String numExp, String empresa, String territorio, int colMinimis, Double totalMinimis)
    {
        try
        {
            HSSFCellStyle estiloCelda = null;
            estiloCelda = libro.createCellStyle();
            estiloCelda.setFont(normal);
            
            HSSFCellStyle estiloCeldaNum = null;
            estiloCeldaNum = libro.createCellStyle();
            estiloCeldaNum.setFont(normal);
            estiloCeldaNum.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
            
            HSSFCell celda=null;//nuevo  
            HSSFCell celdaI=null;
            
            Iterator cellIter = filaHoja0.cellIterator(); 
            int i=0;
	    while (cellIter.hasNext()){
        
		celdaI = (HSSFCell)cellIter.next();
                switch(celdaI.getCellType()) {
	
		case HSSFCell.CELL_TYPE_NUMERIC:
                    celda = filaHoja1.createCell(i);
                    celda.setCellValue(celdaI.getNumericCellValue());
                    celda.setCellStyle(estiloCeldaNum);
                    break;
	
		case HSSFCell.CELL_TYPE_STRING:
                    celda = filaHoja1.createCell(i);
                    celda.setCellValue(celdaI.getStringCellValue());
                    celda.setCellStyle(estiloCelda);
                    break;
		}
                i++;
            }
            if (filaHoja1.getCell(0) == null){
                celda = filaHoja1.createCell(0);
                celda.setCellValue(numExp);
                celda.setCellStyle(estiloCelda);
            }
            
            if (filaHoja1.getCell(1) == null){
                celda = filaHoja1.createCell(1);
                celda.setCellValue(empresa != null? empresa: "");
                celda.setCellStyle(estiloCelda);
            }
            
            if (filaHoja1.getCell(2) == null){
                celda = filaHoja1.createCell(2);
                celda.setCellValue(territorio);
                celda.setCellStyle(estiloCelda);
            }
            
            filaHoja1.getCell(colMinimis).setCellValue(totalMinimis);

        }catch(Exception ex){
            log.debug("ERROR INSERTADO FILA EN HOJA SIN FORMATO: "+ex);
        }
        
    }
    
    private void crearEstiloHoja2Economico(HSSFWorkbook libro, HSSFRow fila, HSSFCell celda, HSSFCellStyle estiloCelda,  int idioma)
    {
        try
        {
            MeLanbide46I18n meLanbide46I18n = MeLanbide46I18n.getInstance();
            
            HSSFPalette palette = libro.getCustomPalette();
            HSSFFont negritaTitulo = libro.createFont();
            negritaTitulo.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
            negritaTitulo.setColor(HSSFColor.WHITE.index);
            negritaTitulo.setFontHeight((short)200);
            HSSFColor hssfColor = null;
            try 
            {
                hssfColor= palette.findColor((byte)75, (byte)149, (byte)211); 
                if (hssfColor == null )
                {
                    hssfColor = palette.getColor(HSSFColor.ROYAL_BLUE.index);
                }
                
                //Se añaden los titulos de las columnas
                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(0);
                celda.setCellValue(meLanbide46I18n.getMensaje(idioma, "doc.informeEconomico.col1").toUpperCase());
                celda.setCellStyle(estiloCelda);

                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(1);
                celda.setCellValue(meLanbide46I18n.getMensaje(idioma, "doc.informeEconomico.col2").toUpperCase());
                celda.setCellStyle(estiloCelda);

                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(2);
                celda.setCellValue(meLanbide46I18n.getMensaje(idioma, "doc.informeEconomico.col30").toUpperCase());
                celda.setCellStyle(estiloCelda);

                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(3);
                celda.setCellValue(meLanbide46I18n.getMensaje(idioma, "doc.informeEconomico.col3").toUpperCase());
                celda.setCellStyle(estiloCelda);

                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(4);
                celda.setCellValue(meLanbide46I18n.getMensaje(idioma, "doc.informeEconomico.col4").toUpperCase());
                celda.setCellStyle(estiloCelda);

                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(5);
                celda.setCellValue(meLanbide46I18n.getMensaje(idioma, "doc.informeEconomico.col5").toUpperCase());
                celda.setCellStyle(estiloCelda);

                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(6);
                celda.setCellValue(meLanbide46I18n.getMensaje(idioma, "doc.informeEconomico.col6").toUpperCase());
                celda.setCellStyle(estiloCelda);

                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(7);
                celda.setCellValue(meLanbide46I18n.getMensaje(idioma, "doc.informeEconomico.col7").toUpperCase());
                celda.setCellStyle(estiloCelda);

                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(8);
                celda.setCellValue(meLanbide46I18n.getMensaje(idioma, "doc.informeEconomico.col8").toUpperCase());
                celda.setCellStyle(estiloCelda);

                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(9);
                celda.setCellValue(meLanbide46I18n.getMensaje(idioma, "doc.informeEconomico.col9").toUpperCase());
                celda.setCellStyle(estiloCelda);

                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(10);
                celda.setCellValue(meLanbide46I18n.getMensaje(idioma, "doc.informeEconomico.col10").toUpperCase());
                celda.setCellStyle(estiloCelda);

                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(11);
                celda.setCellValue(meLanbide46I18n.getMensaje(idioma, "doc.informeEconomico.col11").toUpperCase());
                celda.setCellStyle(estiloCelda);
                
                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(12);
                celda.setCellValue(meLanbide46I18n.getMensaje(idioma, "doc.informeEconomico.col12").toUpperCase());
                celda.setCellStyle(estiloCelda);

                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(13);
                celda.setCellValue(meLanbide46I18n.getMensaje(idioma, "doc.informeEconomico.col13").toUpperCase());
                celda.setCellStyle(estiloCelda);

                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(14);
                celda.setCellValue(meLanbide46I18n.getMensaje(idioma, "doc.informeEconomico.col14").toUpperCase());
                celda.setCellStyle(estiloCelda);

                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(15);
                celda.setCellValue(meLanbide46I18n.getMensaje(idioma, "doc.informeEconomico.col15").toUpperCase());
                celda.setCellStyle(estiloCelda);

                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(16);
                celda.setCellValue(meLanbide46I18n.getMensaje(idioma, "doc.informeEconomico.col16").toUpperCase());
                celda.setCellStyle(estiloCelda);

                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(17);
                celda.setCellValue(meLanbide46I18n.getMensaje(idioma, "doc.informeEconomico.col17").toUpperCase());
                celda.setCellStyle(estiloCelda);

                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(18);
                celda.setCellValue(meLanbide46I18n.getMensaje(idioma, "doc.informeEconomico.col18").toUpperCase());
                celda.setCellStyle(estiloCelda);

                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(19);
                celda.setCellValue(meLanbide46I18n.getMensaje(idioma, "doc.informeEconomico.col19").toUpperCase());
                celda.setCellStyle(estiloCelda);

                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(20);
                celda.setCellValue(meLanbide46I18n.getMensaje(idioma, "doc.informeEconomico.col20").toUpperCase());
                celda.setCellStyle(estiloCelda);

                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(21);
                celda.setCellValue(meLanbide46I18n.getMensaje(idioma, "doc.informeEconomico.col21").toUpperCase());
                celda.setCellStyle(estiloCelda);
                
                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(22);
                celda.setCellValue(meLanbide46I18n.getMensaje(idioma, "doc.informeEconomico.col22").toUpperCase());
                celda.setCellStyle(estiloCelda);

                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(23);
                celda.setCellValue(meLanbide46I18n.getMensaje(idioma, "doc.informeEconomico.col23").toUpperCase());
                celda.setCellStyle(estiloCelda);

                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(24);
                celda.setCellValue(meLanbide46I18n.getMensaje(idioma, "doc.informeEconomico.col24").toUpperCase());
                celda.setCellStyle(estiloCelda);

                
                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderRight(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(25);
                celda.setCellValue(meLanbide46I18n.getMensaje(idioma, "doc.informeEconomico.col25").toUpperCase());
                celda.setCellStyle(estiloCelda);
                
                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderRight(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(26);
                celda.setCellValue(meLanbide46I18n.getMensaje(idioma, "doc.informeEconomico.col26").toUpperCase());
                celda.setCellStyle(estiloCelda);
                
                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderRight(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(27);
                celda.setCellValue(meLanbide46I18n.getMensaje(idioma, "doc.informeEconomico.col27").toUpperCase());
                celda.setCellStyle(estiloCelda);
                
                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderRight(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(28);
                celda.setCellValue(meLanbide46I18n.getMensaje(idioma, "doc.informeEconomico.col28").toUpperCase());
                celda.setCellStyle(estiloCelda);
                
                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderRight(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(29);
                celda.setCellValue(meLanbide46I18n.getMensaje(idioma, "doc.informeEconomico.col29").toUpperCase());
                celda.setCellStyle(estiloCelda);
            }
            catch (Exception e) 
            {
                e.printStackTrace();
            }

                
        }catch(Exception ex){
            
        }
        
    }
    
    //informe justificación final
    public void generarInformeDatosPuestos(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response)
    {
        String codigoOperacion = "0";
        String rutaArchivoSalida = null;
        try
        {
            Integer ano = Integer.parseInt(request.getParameter("ano"));
            AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
            log.error("Anio a generar: "+ ano);
            if(ano > 2015)
            {
                generarInformeDatosPuestos2016(codOrganizacion, codTramite, ocurrenciaTramite, numExpediente, request, response);
            }
            else 
            {
                int idioma = 1;
                HttpSession session = request.getSession();
                UsuarioValueObject usuario = new UsuarioValueObject();
                try
                {
                    if (session != null) 
                    {
                        usuario = (UsuarioValueObject) session.getAttribute("usuario");
                        if (usuario != null) 
                        {
                            idioma = usuario.getIdioma();
                        }
                    }
                }
                catch(Exception ex)
                {
                    log.error("Error", ex);
                }

                MeLanbide46I18n meLanbide46I18n = MeLanbide46I18n.getInstance();
                MeLanbide46Manager manager = MeLanbide46Manager.getInstance();
                SimpleDateFormat format = new SimpleDateFormat(ConstantesMeLanbide46.FORMATO_FECHA);
                
                FileOutputStream archivoSalida = null;
                FileInputStream istr = null;
                try 
                {
                    HSSFWorkbook libro = new HSSFWorkbook();

                    //se escribe el fichero
                    DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
                    //get current date time with Date()
                    Date date = new Date();
                    String fechaAct=dateFormat.format(date);
           
                    File informe= new File("justificacionFinal_"+fechaAct+".xls");

                    archivoSalida = new FileOutputStream(informe);
                    
                    HSSFPalette palette = libro.getCustomPalette();
                    HSSFColor hssfColor = null;
                    try 
                    {
                        hssfColor= palette.findColor((byte)75, (byte)149, (byte)211); 
                        if (hssfColor == null )
                        {
                            hssfColor = palette.getColor(HSSFColor.ROYAL_BLUE.index);
                        }
                    }
                    catch (Exception e) 
                    {
                        e.printStackTrace();
                        log.error("Error", e);
                    }
                    List<FilaInformeDatosPuestosVO> listaPuestos = null;
                    List<FilaInformeHoja2DatosPuestosVO> listaPuestosH = null;
                    HSSFFont negrita = libro.createFont();
                    negrita.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);

                    HSSFFont normal = libro.createFont();
                    normal.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
                    normal.setFontHeight((short)150);

                    HSSFFont negritaTitulo = libro.createFont();
                    negritaTitulo.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
                    negritaTitulo.setColor(HSSFColor.WHITE.index);
                    negritaTitulo.setFontHeight((short)200);
                    int numFila = 0;
                    int numFila2 = 0;

                    HSSFRow fila = null;
                    HSSFCell celda = null;
                    HSSFCellStyle estiloCelda = null;

                    HSSFRow fila2 = null;
                    HSSFCell celda2 = null;
                    HSSFCellStyle estiloCelda2 = null;

                    HSSFSheet hoja = libro.createSheet("Datos puestos");
                    HSSFSheet hoja2 = libro.createSheet("Datos pagos");
                    //hoja2 = libro.createSheet();


                    //Se establece el ancho de cada columnas
                    hoja.setColumnWidth(0, 6000);//Num. expediente
                    hoja.setColumnWidth(1, 10000);//empresa
                    hoja.setColumnWidth(2, 8000);//persona contratada
                    hoja.setColumnWidth(3, 3000);//sexo
                    hoja.setColumnWidth(4, 3000);//nivel formativo
                    hoja.setColumnWidth(5, 8000);//país
                    hoja.setColumnWidth(6, 5000);//salario anual bruto + ss
                    hoja.setColumnWidth(7, 5000);//dietas aloj y manutención
                    hoja.setColumnWidth(8, 3000);//meses exterior
                    hoja.setColumnWidth(9, 4000);//tramitación permisos
                    hoja.setColumnWidth(10, 6000);//importe concedido
                    hoja.setColumnWidth(11, 6000);//país final
                    hoja.setColumnWidth(12, 5000);//salario anexo
                    hoja.setColumnWidth(13, 4500);//contrato inicio
                    hoja.setColumnWidth(14, 3000);//fin periodo subv
                    hoja.setColumnWidth(15, 5000);//días subv contrato
                    hoja.setColumnWidth(16, 5000);//meses ext subv
                    hoja.setColumnWidth(17, 5000);//motivo baja
                    hoja.setColumnWidth(18, 5000);//Salario anual bruto + ss emp jus
                    hoja.setColumnWidth(19, 5000);//Bonificaciones jus
                    hoja.setColumnWidth(20, 5000);//Salario + SS bonif
                    hoja.setColumnWidth(21, 5000);//dietas aloj y manu jus
                    hoja.setColumnWidth(22, 5000);//tram permisos jus
                    hoja.setColumnWidth(23, 5000);//total justi
                    hoja.setColumnWidth(24, 5000);//max subv
                    hoja.setColumnWidth(25, 5000);//bonificaciones
                    hoja.setColumnWidth(26, 5000);//dietas aloj y manu subv
                    hoja.setColumnWidth(27, 5000);//tram permisos subv
                    hoja.setColumnWidth(28, 5000);//total subv

                    hoja2.setColumnWidth(0, 6000);//Num. expediente
                    hoja2.setColumnWidth(1, 10000);//empresa
                    hoja2.setColumnWidth(2, 8000);//persona contratada
                    hoja2.setColumnWidth(3, 5000);//sexo
                    hoja2.setColumnWidth(4, 10000);//nivel formativo
                    hoja2.setColumnWidth(5, 8000);//país
                    hoja2.setColumnWidth(6, 12000);//salario anual bruto + ss


                    log.error("pasa5");
                    fila = hoja.createRow(numFila);
                    log.error("6");

                    //Se añaden los titulos de las columnas
                    //poi alpha
                    /*hoja.addMergedRegion(new Region(numFila, 7, numFila, 11));
//                    log.error("pasa7");
                    hoja.addMergedRegion(new Region(numFila, 18, numFila, 23));
//                    log.error("pasa8");
                    hoja.addMergedRegion(new Region(numFila, 24, numFila, 28));*/

                    //poi 3.10
                    hoja.addMergedRegion(new CellRangeAddress(numFila,  numFila, 7,11));
                    hoja.addMergedRegion(new CellRangeAddress(numFila,  numFila, 18,23));
                    hoja.addMergedRegion(new CellRangeAddress(numFila,  numFila, 24, 28));

                    log.error("45d ");
                    crearEstiloCabeceraInformeDatosPuestos(libro, fila, celda, estiloCelda, idioma);
                    numFila ++;

                    fila = hoja.createRow(numFila);
                    fila2 = hoja2.createRow(numFila2);
                    crearEstiloInformeDatosPuestos(libro, fila, celda, estiloCelda, idioma);
                    crearEstiloInformeDatosPuestosHoja2(libro, fila2, celda2, estiloCelda2, idioma);

                    boolean nuevo = true;
                    int p = 0;
                    int n = 0;
                    int height = 0;                

                    String numExp = null;
                    log.error("ano a generar " + ano);
                    listaPuestos = manager.getDatosInformePuestos(ano, adapt);

                    int maxLengthNombre = 0;
                    //Insertamos los datos, fila a fila
                    for(FilaInformeDatosPuestosVO filaI : listaPuestos)
                    {
                        if(filaI.getNumExpediente() != null && !filaI.getNumExpediente().equals(numExp))
                        {
                            numExp = filaI.getNumExpediente();
                            nuevo = true;
                            p = 0;
                            n++;
                            numFila++;
                        }
                        else
                        {
                            numFila++;   
                            nuevo = false;
                            p++;
                        }

                        fila = hoja.createRow(numFila);

                        crearDatosInformePuestos(libro, fila, celda, estiloCelda, idioma, filaI, numExp, numFila, nuevo, n, listaPuestos, normal, codOrganizacion, ano,  adapt,  format);

                    }

                    listaPuestosH = manager.getDatosInformePuestosHoja2(ano, adapt);
                    nuevo = true; numExp = null;
                    for(FilaInformeHoja2DatosPuestosVO filaI : listaPuestosH)
                    {
                        if(filaI.getNumExpediente() != null && !filaI.getNumExpediente().equals(numExp))
                        {
    //                        }
                            numExp = filaI.getNumExpediente();
                            nuevo = true;
                            p = 0;
                            n++;
                            numFila2++;
                        }
                        else
                        {
                            numFila2++;   
                            nuevo = false;
                            p++;
                        }

                        fila2 = hoja2.createRow(numFila2);               
                        crearDatosInformePuestosHoja2(libro, fila2, celda2, estiloCelda2, idioma, filaI, numExp, numFila, nuevo, n, listaPuestosH, normal, codOrganizacion, ano,  adapt,  format);

                    }

                    hoja.setColumnWidth(16, (short)(maxLengthNombre*400));

                    //Añade sumatorios a la ultima fila
                    numFila++;

                    /*File directorioTemp = new File(m_Conf.getString("PDF.base_dir"));
                    File informe = File.createTempFile("justificacionFinal", ".xls", directorioTemp);

                    FileOutputStream archivoSalida = new FileOutputStream(informe);*/
                    libro.write(archivoSalida);                    

                    rutaArchivoSalida = informe.getAbsolutePath();

                    istr = new FileInputStream(rutaArchivoSalida);

                    BufferedInputStream bstr = new BufferedInputStream( istr ); // promote

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
                } 
                catch (Exception ioe) 
                {
                    log.debug("EXCEPCION informe resumenPuestosContratados");
                    ioe.printStackTrace();

                }
                finally
                {
                    if (archivoSalida != null) archivoSalida.close();
                    if (istr != null) istr.close();
                }
            }
        }
        catch(Exception ex)
        {
            log.debug("EXCEPCION informe resumenPuestosContratados");
            ex.printStackTrace();
            
        }
    }
    
    private void crearEstiloInformeDatosPuestos(HSSFWorkbook libro, HSSFRow fila, HSSFCell celda, HSSFCellStyle estiloCelda, int idioma)
    {
        try
        {
            MeLanbide46I18n meLanbide46I18n = MeLanbide46I18n.getInstance();
            
            HSSFPalette palette = libro.getCustomPalette();
            HSSFColor hssfColor = null;
            HSSFColor hssfColor2 = null;
            try 
            {
                hssfColor= palette.findColor((byte)75, (byte)149, (byte)211); 
                if (hssfColor == null )
                {
                    hssfColor = palette.getColor(HSSFColor.ROYAL_BLUE.index);
                }
                hssfColor2 = palette.getColor(HSSFColor.GREY_50_PERCENT.index);
                
                HSSFFont negrita = libro.createFont();
                negrita.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);

                HSSFFont normal = libro.createFont();
                normal.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
                normal.setFontHeight((short)150);

                HSSFFont negritaTitulo = libro.createFont();
                negritaTitulo.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
                negritaTitulo.setFontHeight((short)170);
                negritaTitulo.setColor(HSSFColor.WHITE.index);
                //celda = fila.

                //cabeceras
                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(0);
                celda.setCellValue(meLanbide46I18n.getMensaje(idioma, "doc.informeDatosPuestos.col1").toUpperCase());
                celda.setCellStyle(estiloCelda);

                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(1);
                celda.setCellValue(meLanbide46I18n.getMensaje(idioma, "doc.informeDatosPuestos.col2").toUpperCase());
                celda.setCellStyle(estiloCelda);

                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(2);
                celda.setCellValue(meLanbide46I18n.getMensaje(idioma, "doc.informeDatosPuestos.col3").toUpperCase());
                celda.setCellStyle(estiloCelda);

                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(3);
                celda.setCellValue(meLanbide46I18n.getMensaje(idioma, "doc.informeDatosPuestos.col4").toUpperCase());
                celda.setCellStyle(estiloCelda);

                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(4);
                celda.setCellValue(meLanbide46I18n.getMensaje(idioma, "doc.informeDatosPuestos.col5").toUpperCase());
                celda.setCellStyle(estiloCelda);

                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(5);
                celda.setCellValue(meLanbide46I18n.getMensaje(idioma, "doc.informeDatosPuestos.col6").toUpperCase());
                celda.setCellStyle(estiloCelda);

                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(6);
                celda.setCellValue(meLanbide46I18n.getMensaje(idioma, "doc.informeDatosPuestos.col7").toUpperCase());
                celda.setCellStyle(estiloCelda);

                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor2.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(7);
                celda.setCellValue(meLanbide46I18n.getMensaje(idioma, "doc.informeDatosPuestos.col8").toUpperCase());
                celda.setCellStyle(estiloCelda);

                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor2.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(8);
                celda.setCellValue(meLanbide46I18n.getMensaje(idioma, "doc.informeDatosPuestos.col9").toUpperCase());
                celda.setCellStyle(estiloCelda);

                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor2.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(9);
                celda.setCellValue(meLanbide46I18n.getMensaje(idioma, "doc.informeDatosPuestos.col10").toUpperCase());
                celda.setCellStyle(estiloCelda);

                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor2.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(10);
                celda.setCellValue(meLanbide46I18n.getMensaje(idioma, "doc.informeDatosPuestos.col11").toUpperCase());
                celda.setCellStyle(estiloCelda);

                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor2.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(11);
                celda.setCellValue(meLanbide46I18n.getMensaje(idioma, "doc.informeDatosPuestos.col12").toUpperCase());
                celda.setCellStyle(estiloCelda);

                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(12);
                celda.setCellValue(meLanbide46I18n.getMensaje(idioma, "doc.informeDatosPuestos.col13").toUpperCase());
                celda.setCellStyle(estiloCelda);

                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(13);
                celda.setCellValue(meLanbide46I18n.getMensaje(idioma, "doc.informeDatosPuestos.col14").toUpperCase());
                celda.setCellStyle(estiloCelda);

                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(14);
                celda.setCellValue(meLanbide46I18n.getMensaje(idioma, "doc.informeDatosPuestos.col15").toUpperCase());
                celda.setCellStyle(estiloCelda);

                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(15);
                celda.setCellValue(meLanbide46I18n.getMensaje(idioma, "doc.informeDatosPuestos.col16").toUpperCase());
                celda.setCellStyle(estiloCelda);

                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderRight(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(16);
                celda.setCellValue(meLanbide46I18n.getMensaje(idioma, "doc.informeDatosPuestos.col17").toUpperCase());
                celda.setCellStyle(estiloCelda);

                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(17);
                celda.setCellValue(meLanbide46I18n.getMensaje(idioma, "doc.informeDatosPuestos.col18").toUpperCase());
                celda.setCellStyle(estiloCelda);

                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(18);
                celda.setCellValue(meLanbide46I18n.getMensaje(idioma, "doc.informeDatosPuestos.col19").toUpperCase());
                celda.setCellStyle(estiloCelda);

                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(19);
                celda.setCellValue(meLanbide46I18n.getMensaje(idioma, "doc.informeDatosPuestos.col20").toUpperCase());
                celda.setCellStyle(estiloCelda);

                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(20);
                celda.setCellValue(meLanbide46I18n.getMensaje(idioma, "doc.informeDatosPuestos.col21").toUpperCase());
                celda.setCellStyle(estiloCelda);

                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(21);
                celda.setCellValue(meLanbide46I18n.getMensaje(idioma, "doc.informeDatosPuestos.col22").toUpperCase());
                celda.setCellStyle(estiloCelda);

                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(22);
                celda.setCellValue(meLanbide46I18n.getMensaje(idioma, "doc.informeDatosPuestos.col23").toUpperCase());
                celda.setCellStyle(estiloCelda);

                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(23);
                celda.setCellValue(meLanbide46I18n.getMensaje(idioma, "doc.informeDatosPuestos.col24").toUpperCase());
                celda.setCellStyle(estiloCelda);

                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor2.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(24);
                celda.setCellValue(meLanbide46I18n.getMensaje(idioma, "doc.informeDatosPuestos.col25").toUpperCase());
                celda.setCellStyle(estiloCelda);

                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor2.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(25);
                celda.setCellValue(meLanbide46I18n.getMensaje(idioma, "doc.informeDatosPuestos.col26").toUpperCase());
                celda.setCellStyle(estiloCelda);

                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor2.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(26);
                celda.setCellValue(meLanbide46I18n.getMensaje(idioma, "doc.informeDatosPuestos.col27").toUpperCase());
                celda.setCellStyle(estiloCelda);

                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor2.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(27);
                celda.setCellValue(meLanbide46I18n.getMensaje(idioma, "doc.informeDatosPuestos.col28").toUpperCase());
                celda.setCellStyle(estiloCelda);

                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor2.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(28);
                celda.setCellValue(meLanbide46I18n.getMensaje(idioma, "doc.informeDatosPuestos.col29").toUpperCase());
                celda.setCellStyle(estiloCelda);
            }
            catch (Exception e) 
            {
                e.printStackTrace();
            }

            
        }catch(Exception ex){
                    log.error("Error", ex);
            
        }
        
    }
    private void crearEstiloCabeceraInformeDatosPuestos(HSSFWorkbook libro, HSSFRow fila, HSSFCell celda, HSSFCellStyle estiloCelda, int idioma)
    {
        try
        {
            //MeLanbide46I18n meLanbide46I18n = MeLanbide46I18n.getInstance();
            
            HSSFPalette palette = libro.getCustomPalette();
            HSSFColor hssfColor = null;
            HSSFColor hssfColor2 = null;
            try 
            {
                hssfColor= palette.findColor((byte)75, (byte)149, (byte)211); 
                if (hssfColor == null )
                {
                    hssfColor = palette.getColor(HSSFColor.GREY_50_PERCENT.index);
                }
                hssfColor2 = palette.getColor(HSSFColor.ROYAL_BLUE.index);
                
                HSSFFont negrita = libro.createFont();
                negrita.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);

                HSSFFont normal = libro.createFont();
                normal.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
                normal.setFontHeight((short)150);

                HSSFFont negritaTitulo = libro.createFont();
                negritaTitulo.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
                negritaTitulo.setFontHeight((short)170);
                negritaTitulo.setColor(HSSFColor.WHITE.index);


                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(7);
                celda.setCellValue("SUBVENCIÓN CONCEDIDA");
                celda.setCellStyle(estiloCelda);

                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor2.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(18);
                celda.setCellValue("JUSTIFICADO");
                celda.setCellStyle(estiloCelda);


                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(24);
                celda.setCellValue("SUBVENCIÓN FINAL");
                celda.setCellStyle(estiloCelda);
            }
            catch (Exception e) 
            {
                e.printStackTrace();
            }

            
        }catch(Exception ex){
                    log.error("Error", ex);
            
        }
        
    }
    
    private void crearDatosInformePuestos (HSSFWorkbook libro, HSSFRow fila, HSSFCell celda, HSSFCellStyle estiloCelda, int idioma, FilaInformeDatosPuestosVO filaI,
    String numExp, int numFila, boolean nuevo, int n, List<FilaInformeDatosPuestosVO> listaPuestos, 
    HSSFFont normal, int codOrganizacion, int ano, AdaptadorSQLBD adapt, SimpleDateFormat format)
    {
        try
        {
            String empresa = null;
            String idiomas = null;
            String pais = null;
            String titulacion = null;
                
            //COLUMNA: NUM_EXPEDIENTE
            estiloCelda = libro.createCellStyle();
            estiloCelda.setWrapText(true);
            estiloCelda.setFont(normal);
            //estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
            estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);

            if(n == 1)
            {
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
            }
            else
            {
                //estiloCelda.setBorderTop(HSSFCellStyle.BORDER_DOUBLE);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
            }
            if(numFila == listaPuestos.size())
            {
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
            }
            celda = fila.createCell(0);
            celda.setCellValue(filaI.getNumExpediente() != null ? filaI.getNumExpediente().toUpperCase() : "");
            celda.setCellStyle(estiloCelda);

            //COLUMNA: EMPRESA
            estiloCelda = libro.createCellStyle();
            estiloCelda.setWrapText(true);
            estiloCelda.setFont(normal);
            estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THIN);
            //estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);

            estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);

            if(numFila == listaPuestos.size())
            {
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
            }
            celda = fila.createCell(1);

            celda.setCellValue(filaI.getEmpresa() != null ? filaI.getEmpresa().toUpperCase() : "");
            celda.setCellStyle(estiloCelda);
            
            
            //COLUMNA: ACTIVIDAD
            estiloCelda = libro.createCellStyle();
            estiloCelda.setWrapText(true);
            estiloCelda.setFont(normal);
            estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THIN);
            //estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);

            estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);

            if(numFila == listaPuestos.size())
            {
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
            }
            celda = fila.createCell(2);

            celda.setCellValue(filaI.getActividad()!= null ? filaI.getActividad().toUpperCase() : "");
            celda.setCellStyle(estiloCelda);

            //COLUMNA: PERSONA CONTRATADA
            estiloCelda = libro.createCellStyle();
            estiloCelda.setWrapText(true);
            estiloCelda.setFont(normal);
            estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THIN);

            if(n == 1)
            {
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
            }
            else
            {
                //estiloCelda.setBorderTop(HSSFCellStyle.BORDER_DOUBLE);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
            }

            if(numFila == listaPuestos.size())
            {
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
            }
            celda = fila.createCell(3);
            celda.setCellValue(filaI.getPersContratada() != null ? filaI.getPersContratada().toUpperCase() : "");
            celda.setCellStyle(estiloCelda);

            //COLUMNA: SEXO
            estiloCelda = libro.createCellStyle();
            estiloCelda.setWrapText(true);
            estiloCelda.setFont(normal);
            estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THIN);
            estiloCelda.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
            //estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
            if(n == 1)
            {
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
            }
            else
            {
                //estiloCelda.setBorderTop(HSSFCellStyle.BORDER_DOUBLE);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
            }
            if(numFila == listaPuestos.size())
            {
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
            }
            celda = fila.createCell(4);
            celda.setCellValue(filaI.getSexo() != null ? filaI.getSexo() : "");
            //celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
            celda.setCellStyle(estiloCelda);


            //COLUMNA: NIVEL FORMATIVO
            estiloCelda = libro.createCellStyle();
            estiloCelda.setWrapText(true);
            estiloCelda.setFont(normal);
            estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THIN);
            if(n == 1)
            {
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
            }
            else
            {
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
            }
            if(numFila == listaPuestos.size())
            {
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
            }
            celda = fila.createCell(5);
            celda.setCellValue(filaI.getNivelFormativo() != null ? filaI.getNivelFormativo().toUpperCase() : "");
            celda.setCellStyle(estiloCelda);

            //COLUMNA: PAIS
            estiloCelda = libro.createCellStyle();
            estiloCelda.setWrapText(true);
            estiloCelda.setFont(normal);
            estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THIN);
            if(n == 1)
            {
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
            }
            else
            {
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
            }
            if(numFila == listaPuestos.size())
            {
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
            }
            celda = fila.createCell(6);
            celda.setCellValue(filaI.getPais() != null ? filaI.getPais().toString() : "");
            celda.setCellStyle(estiloCelda);

            //COLUMNA: SALARIO ANUAL BRUTO + SS
            estiloCelda = libro.createCellStyle();
            estiloCelda.setWrapText(true);
            estiloCelda.setFont(normal);
            estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THIN);

            if(n == 1)
            {
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
            }
            else
            {
                //estiloCelda.setBorderTop(HSSFCellStyle.BORDER_DOUBLE);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
            }
            if(numFila == listaPuestos.size())
            {
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
            }
            celda = fila.createCell(7);
            //celda.setCellValue(filaI.getSalarioAnualBruto() != null ? filaI.getSalarioAnualBruto().toUpperCase() : "");
            celda.setCellStyle(estiloCelda);
            if (filaI.getSalarioAnualBruto()!= null){
                if (filaI.getSalarioAnualBruto()!= "-"){
                    Double aux=Double.parseDouble(filaI.getSalarioAnualBruto().replace(",","."));
                    celda.setCellValue(aux);
                    celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                }else{
                    celda.setCellValue(filaI.getSalarioAnualBruto());
                }
            }
            
            //COLUMNA: DIETAS ALOJAMIENTO Y MANUTENCIÓN
            estiloCelda = libro.createCellStyle();
            estiloCelda.setWrapText(true);
            estiloCelda.setFont(normal);
            estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THIN);
            if(n == 1)
            {
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
            }
            else
            {
                //estiloCelda.setBorderTop(HSSFCellStyle.BORDER_DOUBLE);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
            }
            if(numFila == listaPuestos.size())
            {
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
            }
            celda = fila.createCell(8);
            //celda.setCellValue(filaI.getDietasAloj() != null ? filaI.getDietasAloj().toUpperCase() : "");
            celda.setCellStyle(estiloCelda);
            if (filaI.getDietasAloj()!= null){
                if (filaI.getDietasAloj()!= "-"){
                    Double aux=Double.parseDouble(filaI.getDietasAloj().replace(",","."));
                    celda.setCellValue(aux);
                    celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                }else{
                    celda.setCellValue(filaI.getDietasAloj());
                }
            }

            //COLUMNA: MESES EXTERIOR
            estiloCelda = libro.createCellStyle();
            estiloCelda.setWrapText(true);
            estiloCelda.setFont(normal);
            estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THIN);
            if(n == 1)
            {
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
            }
            else
            {
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
            }
            if(numFila == listaPuestos.size())
            {
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
            }
            celda = fila.createCell(9);
            //celda.setCellValue(filaI.getMesesExt()!= null ? filaI.getMesesExt().toUpperCase() : "");
            celda.setCellStyle(estiloCelda);
             if (filaI.getMesesExt()!= null){
                if (filaI.getMesesExt()!= "-"){
                    Double aux=Double.parseDouble(filaI.getMesesExt().replace(",","."));
                    celda.setCellValue(aux);
                    celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                }else{
                    celda.setCellValue(filaI.getMesesExt());
                }
            }

            //COLUMNA: TRAMITACIÓN DE PERMISOS Y SEGUROS MÉDICOS
            estiloCelda = libro.createCellStyle();
            estiloCelda.setWrapText(true);
            estiloCelda.setFont(normal);
            estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THIN);
            estiloCelda.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
            if(n == 1)
            {
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
            }
            else
            {
                //estiloCelda.setBorderTop(HSSFCellStyle.BORDER_DOUBLE);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
            }
            if(numFila == listaPuestos.size())
            {
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
            }
            celda = fila.createCell(10);
            //celda.setCellValue(filaI.getTramitacionPer() != null ? filaI.getTramitacionPer() : "");
            celda.setCellStyle(estiloCelda);
            if (filaI.getTramitacionPer()!= null){
                if (filaI.getTramitacionPer()!= "-"){
                    Double aux=Double.parseDouble(filaI.getTramitacionPer().replace(",","."));
                    celda.setCellValue(aux);
                    celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                }else{
                    celda.setCellValue(filaI.getTramitacionPer());
                }
            }
            //celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);

            //COLUMNA: IMP TOTAL CONCEDIDO
            estiloCelda = libro.createCellStyle();
            estiloCelda.setWrapText(true);
            estiloCelda.setFont(normal);
            estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THIN);
            estiloCelda.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
            if(n == 1)
            {
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
            }
            else
            {
                //estiloCelda.setBorderTop(HSSFCellStyle.BORDER_DOUBLE);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
            }
            if(numFila == listaPuestos.size())
            {
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
            }
            celda = fila.createCell(11);
            celda.setCellValue(filaI.getImporteConce() != null ? filaI.getImporteConce() : "");
            celda.setCellStyle(estiloCelda);
            //celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);

            //COLUMNA: PAIS FINAL
            estiloCelda = libro.createCellStyle();
            estiloCelda.setWrapText(true);
            estiloCelda.setFont(normal);
            estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THIN);
            if(n == 1)
            {
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
            }
            else
            {
                //estiloCelda.setBorderTop(HSSFCellStyle.BORDER_DOUBLE);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
            }
            if(numFila == listaPuestos.size())
            {
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
            }
            celda = fila.createCell(12);
            celda.setCellValue(filaI.getPaisDestino() != null ? filaI.getPaisDestino() : "");
            celda.setCellStyle(estiloCelda);

            //COLUMNA: CONTRATO INICIO
            estiloCelda = libro.createCellStyle();
            estiloCelda.setWrapText(true);
            estiloCelda.setFont(normal);
            estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THIN);
            if(n == 1)
            {
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
            }
            else
            {
                //estiloCelda.setBorderTop(HSSFCellStyle.BORDER_DOUBLE);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
            }
            if(numFila == listaPuestos.size())
            {
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
            }
            celda = fila.createCell(13);
            celda.setCellValue(filaI.getContratoInicio() != null ? filaI.getContratoInicio().toUpperCase() : "");
            celda.setCellStyle(estiloCelda);

            //COLUMNA: FIN PERIODO SUBVEN
            estiloCelda = libro.createCellStyle();
            estiloCelda.setWrapText(true);
            estiloCelda.setFont(normal);
            estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THIN);
            if(n == 1)
            {
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
            }
            else
            {
                //estiloCelda.setBorderTop(HSSFCellStyle.BORDER_DOUBLE);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
            }
            if(numFila == listaPuestos.size())
            {
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
            }
            celda = fila.createCell(14);
            celda.setCellValue(filaI.getFinPeriodoSubv() != null ? filaI.getFinPeriodoSubv().toUpperCase() : "");
            celda.setCellStyle(estiloCelda);

            //COLUMNA: DIAS SUBVENCIONABLES CONTRATO 
            estiloCelda = libro.createCellStyle();
            estiloCelda.setWrapText(true);
            estiloCelda.setFont(normal);
            estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THIN);

            if(n == 1)
            {
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
            }
            else
            {
                //estiloCelda.setBorderTop(HSSFCellStyle.BORDER_DOUBLE);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
            }
            if(numFila == listaPuestos.size())
            {
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
            }
            celda = fila.createCell(15);
            //celda.setCellValue(filaI.getDiasSubv() != null ? filaI.getDiasSubv() : "");
            celda.setCellStyle(estiloCelda);
            if (filaI.getDiasSubv()!= null){
                if (filaI.getDiasSubv()!= "-"){
                    Double aux=Double.parseDouble(filaI.getDiasSubv().replace(",","."));
                    celda.setCellValue(aux);
                    celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                }else{
                    celda.setCellValue(filaI.getDiasSubv());
                }
            }

            //COLUMNA: MESES EN EL EXTERIOR SUBV
            estiloCelda = libro.createCellStyle();
            estiloCelda.setWrapText(true);
            estiloCelda.setFont(normal);
            estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THIN);
            estiloCelda.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
            if(n == 1)
            {
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
            }
            else
            {
                //estiloCelda.setBorderTop(HSSFCellStyle.BORDER_DOUBLE);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
            }
            if(numFila == listaPuestos.size())
            {
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
            }
            celda = fila.createCell(16);
            celda.setCellValue(filaI.getMesesSubv() != null ? filaI.getMesesSubv() : "");
            celda.setCellStyle(estiloCelda);
            //celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);

            //COLUMNA: MOTIVO BAJA/DESPIDO
            estiloCelda = libro.createCellStyle();
            estiloCelda.setWrapText(true);
            estiloCelda.setFont(normal);
            estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THIN);
            estiloCelda.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
            if(n == 1)
            {
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
            }
            else
            {
                //estiloCelda.setBorderTop(HSSFCellStyle.BORDER_DOUBLE);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
            }
            if(numFila == listaPuestos.size())
            {
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
            }
            celda = fila.createCell(17);
            celda.setCellValue(filaI.getMotivoBaja()!= null ? filaI.getMotivoBaja() : "");
            celda.setCellStyle(estiloCelda);
            //celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);

            //COLUMNA: SALARIO ANUAL BRUTO +SS EMPRESA
            estiloCelda = libro.createCellStyle();
            estiloCelda.setWrapText(true);
            estiloCelda.setFont(normal);
            estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THIN);
            if(n == 1)
            {
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
            }
            else
            {
                //estiloCelda.setBorderTop(HSSFCellStyle.BORDER_DOUBLE);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
            }
            if(numFila == listaPuestos.size())
            {
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
            }
            celda = fila.createCell(18);
            //celda.setCellValue(filaI.getSalarioAnualBruto() != null ? filaI.getSalarioAnualBruto().toUpperCase() : "");
            //celda.setCellValue(filaI.getMaxSubvencion() != null ? filaI.getMaxSubvencion().toUpperCase()  : "");
            celda.setCellStyle(estiloCelda);
            
            if (filaI.getMaxSubvencion()!= null){
                if (filaI.getMaxSubvencion()!= "-"){
                    Double aux=Double.parseDouble(filaI.getMaxSubvencion().replace(",","."));
                    celda.setCellValue(aux);
                    celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                }else{
                    celda.setCellValue(filaI.getMaxSubvencion());
                }
            }
            //COLUMNA: BONIFICACIONES
            estiloCelda = libro.createCellStyle();
            estiloCelda.setWrapText(true);
            estiloCelda.setFont(normal);
            estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THIN);
            if(n == 1)
            {
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
            }
            else
            {
                //estiloCelda.setBorderTop(HSSFCellStyle.BORDER_DOUBLE);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
            }
            if(numFila == listaPuestos.size())
            {
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
            }
            celda = fila.createCell(19);
            //celda.setCellValue(filaI.getImporteJus() != null ? filaI.getImporteJus() : "");
            //celda.setCellValue(filaI.getContratoBonif() != null ? filaI.getContratoBonif().replaceAll("\\.", ",") : "");
            celda.setCellStyle(estiloCelda);
            if (filaI.getContratoBonif()!= null){
                if (filaI.getContratoBonif()!= "-"){
                    Double aux=Double.parseDouble(filaI.getContratoBonif().replace(",","."));
                    celda.setCellValue(aux);
                    celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                }else{
                    celda.setCellValue(filaI.getContratoBonif());
                }
            }
            //COLUMNA: SALARIO ANUAL BRUTO + SS-EMPRESA
            estiloCelda = libro.createCellStyle();
            estiloCelda.setWrapText(true);
            estiloCelda.setFont(normal);
            estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THIN);
            if(n == 1)
            {
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
            }
            else
            {
                //estiloCelda.setBorderTop(HSSFCellStyle.BORDER_DOUBLE);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
            }
            if(numFila == listaPuestos.size())
            {
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
            }
            celda = fila.createCell(20);
            //celda.setCellValue(filaI.getSalarioSSBonif()!= null ? filaI.getSalarioSSBonif().replaceAll("\\.", ",") : "");
            celda.setCellStyle(estiloCelda);
            if (filaI.getSalarioSSBonif()!= null){
                if (filaI.getSalarioSSBonif()!= "-"){
                    Double aux=Double.parseDouble(filaI.getSalarioSSBonif().replace(",","."));
                    celda.setCellValue(aux);
                    celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                }else{
                    celda.setCellValue(filaI.getSalarioSSBonif());
                }
            }   
            //COLUMNA: DIETAS ALOJAMIENTO Y MANUTENCIÓN
            estiloCelda = libro.createCellStyle();
            estiloCelda.setWrapText(true);
            estiloCelda.setFont(normal);
            estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THIN);
            estiloCelda.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
            if(n == 1)
            {
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
            }
            else
            {
                //estiloCelda.setBorderTop(HSSFCellStyle.BORDER_DOUBLE);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
            }
            if(numFila == listaPuestos.size())
            {
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
            }
            celda = fila.createCell(21);
            //celda.setCellValue(filaI.getDietasJus()!= null ? filaI.getDietasJus().replaceAll("\\.", ",") : "");
            celda.setCellStyle(estiloCelda);
            //celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
            if (filaI.getDietasJus()!= null){
                if (filaI.getDietasJus()!= "-"){
                    Double aux=Double.parseDouble(filaI.getDietasJus().replace(",","."));
                    celda.setCellValue(aux);
                    celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                }else{
                    celda.setCellValue(filaI.getDietasJus());
                }
            } 

            //COLUMNA: TRAMITTACIÓN DE PERMISOS Y SEG MÉDICOS
            estiloCelda = libro.createCellStyle();
            estiloCelda.setWrapText(true);
            estiloCelda.setFont(normal);
            estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THIN);
            if(n == 1)
            {
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
            }
            else
            {
                //estiloCelda.setBorderTop(HSSFCellStyle.BORDER_DOUBLE);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
            }
            if(numFila == listaPuestos.size())
            {
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
            }
            celda = fila.createCell(22);
            //celda.setCellValue(filaI.getGastosAbonados()!= null ? filaI.getGastosAbonados().toUpperCase() : "");
            celda.setCellStyle(estiloCelda);
            if (filaI.getGastosAbonados()!= null){
                if (filaI.getGastosAbonados()!= "-"){
                    Double aux=Double.parseDouble(filaI.getGastosAbonados().replace(",","."));
                    celda.setCellValue(aux);
                    celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                }else{
                    celda.setCellValue(filaI.getGastosAbonados());
                }
            } 
            
            //COLUMNA: TOTAL JUSTIFICADO
            estiloCelda = libro.createCellStyle();
            estiloCelda.setWrapText(true);
            estiloCelda.setFont(normal);
            estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THIN);
            if(n == 1)
            {
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
            }
            else
            {
                //estiloCelda.setBorderTop(HSSFCellStyle.BORDER_DOUBLE);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
            }
            if(numFila == listaPuestos.size())
            {
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
            }
            celda = fila.createCell(23);
            //celda.setCellValue(filaI.getImporteJus() != null ? filaI.getImporteJus().toUpperCase().replaceAll("\\.", ",") : "");
            celda.setCellStyle(estiloCelda);
            if (filaI.getImporteJus()!= null){
                if (filaI.getImporteJus()!= "-"){
                    Double aux=Double.parseDouble(filaI.getImporteJus().replace(",","."));
                    celda.setCellValue(aux);
                    celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                }else{
                    celda.setCellValue(filaI.getImporteJus());
                }
            }

            //COLUMNA: MAXIMO SUBV SALARIO + SS (A)
            estiloCelda = libro.createCellStyle();
            estiloCelda.setWrapText(true);
            estiloCelda.setFont(normal);
            estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THIN);
            if(n == 1)
            {
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
            }
            else
            {
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
            }
            if(numFila == listaPuestos.size())
            {
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
            }
            celda = fila.createCell(24);
            //celda.setCellValue(filaI.getMaxSubvencion() != null ? filaI.getMaxSubvencion().toUpperCase() : "");
            //celda.setCellValue(filaI.getMaxSubvencionSubFinal() != null ? filaI.getMaxSubvencionSubFinal().toUpperCase()  : "");
            celda.setCellStyle(estiloCelda);
            if (filaI.getMaxSubvencionSubFinal()!= null){
                if (filaI.getMaxSubvencionSubFinal()!= "-"){
                    Double aux=Double.parseDouble(filaI.getMaxSubvencionSubFinal().replace(",","."));
                    celda.setCellValue(aux);
                    celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                }else{
                    celda.setCellValue(filaI.getMaxSubvencionSubFinal());
                }
            }
            //COLUMNA: BONIFICACIONES(B)
            estiloCelda = libro.createCellStyle();
            estiloCelda.setWrapText(true);
            estiloCelda.setFont(normal);
            estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THIN);
            if(n == 1)
            {
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
            }
            else
            {
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
            }
            if(numFila == listaPuestos.size())
            {
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
            }
            celda = fila.createCell(25);
            //celda.setCellValue(filaI.getContratoBonif() != null ? filaI.getContratoBonif().toUpperCase().replaceAll("\\.", ",") : "");
            celda.setCellStyle(estiloCelda);
            if (filaI.getContratoBonif()!= null){
                if (filaI.getContratoBonif()!= "-"){
                    Double aux=Double.parseDouble(filaI.getContratoBonif().replace(",","."));
                    celda.setCellValue(aux);
                    celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                }else{
                    celda.setCellValue(filaI.getContratoBonif());
                }
            }

            //COLUMNA: DIETAS ALOJAMIENTO Y MANU SUVB (C)
            estiloCelda = libro.createCellStyle();
            estiloCelda.setWrapText(true);
            estiloCelda.setFont(normal);
            estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THIN);
            if(n == 1)
            {
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
            }
            else
            {
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
            }
            if(numFila == listaPuestos.size())
            {
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
            }
            celda = fila.createCell(26);
            //celda.setCellValue(filaI.getDietasMaxSubv() != null ? filaI.getDietasMaxSubv() : "");
            celda.setCellStyle(estiloCelda);
            if (filaI.getDietasMaxSubv()!= null){
                if (filaI.getDietasMaxSubv()!= "-"){
                    Double aux=Double.parseDouble(filaI.getDietasMaxSubv().replace(",","."));
                    celda.setCellValue(aux);
                    celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                }else{
                    celda.setCellValue(filaI.getDietasMaxSubv());
                }
            }

            //COLUMNA: TRAMITACIÓN DE PERMISOS Y SEGUROS MÉDICOS (D)
            estiloCelda = libro.createCellStyle();
            estiloCelda.setWrapText(true);
            estiloCelda.setFont(normal);
            estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THIN);
            if(n == 1)
            {
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
            }
            else
            {
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
            }
            if(numFila == listaPuestos.size())
            {
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
            }
            celda = fila.createCell(27);
            //celda.setCellValue(filaI.getSeguroMedico()!= null ? filaI.getSeguroMedico() : "");
            celda.setCellStyle(estiloCelda);
             if (filaI.getSeguroMedico()!= null){
                if (filaI.getSeguroMedico()!= "-"){
                    Double aux=Double.parseDouble(filaI.getSeguroMedico().replace(",","."));
                    celda.setCellValue(aux);
                    celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                }else{
                    celda.setCellValue(filaI.getSeguroMedico());
                }
            }


            //COLUMNA: TOTAL SUBV ((A-B)+C+D)
            estiloCelda = libro.createCellStyle();
            estiloCelda.setWrapText(true);
            estiloCelda.setFont(normal);
            estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THIN);
            estiloCelda.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
            if(n == 1)
            {
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
            }
            else
            {
                //estiloCelda.setBorderTop(HSSFCellStyle.BORDER_DOUBLE);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
            }
            if(numFila == listaPuestos.size())
            {
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
            }
            celda = fila.createCell(28);
            celda.setCellValue(filaI.getTotalSubv()!= null ? filaI.getTotalSubv() : "");
            celda.setCellStyle(estiloCelda);
            //celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
        }catch(Exception ex){
            log.error("Error: " + ex);
        }
        
    }
    
    private void crearEstiloInformeDatosPuestosHoja2(HSSFWorkbook libro, HSSFRow fila, HSSFCell celda, HSSFCellStyle estiloCelda, int idioma)
    {
        try
        {
            MeLanbide46I18n meLanbide46I18n = MeLanbide46I18n.getInstance();
            
            HSSFPalette palette = libro.getCustomPalette();
            HSSFColor hssfColor = null;
            try 
            {
                hssfColor= palette.findColor((byte)75, (byte)149, (byte)211); 
                if (hssfColor == null )
                {
                    hssfColor = palette.getColor(HSSFColor.ROYAL_BLUE.index);
                }
                
                HSSFFont negrita = libro.createFont();
                negrita.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);

                HSSFFont normal = libro.createFont();
                normal.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
                normal.setFontHeight((short)150);

                HSSFFont negritaTitulo = libro.createFont();
                negritaTitulo.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
                negritaTitulo.setFontHeight((short)170);
                negritaTitulo.setColor(HSSFColor.WHITE.index);

                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(0);
                celda.setCellValue(meLanbide46I18n.getMensaje(idioma, "doc.informeDatosPuestosHoja2.col1").toUpperCase());
                celda.setCellStyle(estiloCelda);

                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(1);
                celda.setCellValue(meLanbide46I18n.getMensaje(idioma, "doc.informeDatosPuestosHoja2.col2").toUpperCase());
                celda.setCellStyle(estiloCelda);

                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(2);
                celda.setCellValue(meLanbide46I18n.getMensaje(idioma, "doc.informeDatosPuestosHoja2.col3").toUpperCase());
                celda.setCellStyle(estiloCelda);

                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(3);
                celda.setCellValue(meLanbide46I18n.getMensaje(idioma, "doc.informeDatosPuestosHoja2.col4").toUpperCase());
                celda.setCellStyle(estiloCelda);

                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(4);
                celda.setCellValue(meLanbide46I18n.getMensaje(idioma, "doc.informeDatosPuestosHoja2.col5").toUpperCase());
                celda.setCellStyle(estiloCelda);

                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(5);
                celda.setCellValue(meLanbide46I18n.getMensaje(idioma, "doc.informeDatosPuestosHoja2.col6").toUpperCase());
                celda.setCellStyle(estiloCelda);

                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(6);
                celda.setCellValue(meLanbide46I18n.getMensaje(idioma, "doc.informeDatosPuestosHoja2.col7").toUpperCase());
                celda.setCellStyle(estiloCelda);

                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(7);
                celda.setCellValue(meLanbide46I18n.getMensaje(idioma, "doc.informeDatosPuestosHoja2.col8").toUpperCase());
                celda.setCellStyle(estiloCelda);
            
            }
            catch (Exception e) 
            {
                e.printStackTrace();
            }

            
        }catch(Exception ex){
            log.error("Error", ex);
            
        }
        
    }
    
    private void crearDatosInformePuestosHoja2 (HSSFWorkbook libro, HSSFRow fila, HSSFCell celda, HSSFCellStyle estiloCelda, int idioma, FilaInformeHoja2DatosPuestosVO filaI,
    String numExp, int numFila, boolean nuevo, int n, List<FilaInformeHoja2DatosPuestosVO> listaPuestos, 
    HSSFFont normal, int codOrganizacion, int ano, AdaptadorSQLBD adapt, SimpleDateFormat format)
    {
        try
        {
            String empresa = null;
            String idiomas = null;
            String pais = null;
            String titulacion = null;
                
            //COLUMNA: NUM_EXPEDIENTE
            estiloCelda = libro.createCellStyle();
            estiloCelda.setWrapText(true);
            estiloCelda.setFont(normal);
            //estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
            estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);

            if(n == 1)
            {
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
            }
            else
            {
                //estiloCelda.setBorderTop(HSSFCellStyle.BORDER_DOUBLE);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
            }
            if(numFila == listaPuestos.size())
            {
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
            }
            celda = fila.createCell(0);
            celda.setCellValue(filaI.getNumExpediente() != null ? filaI.getNumExpediente().toUpperCase() : "");
            celda.setCellStyle(estiloCelda);

            //COLUMNA: EMPRESA
            estiloCelda = libro.createCellStyle();
            estiloCelda.setWrapText(true);
            estiloCelda.setFont(normal);
            estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THIN);
            //estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);

            estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);

            if(numFila == listaPuestos.size())
            {
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
            }
            celda = fila.createCell(1);

            celda.setCellValue(filaI.getEmpresa() != null ? filaI.getEmpresa() : "");
            celda.setCellStyle(estiloCelda);

            //COLUMNA: IMPORTE CONCEDIDO
            estiloCelda = libro.createCellStyle();
            estiloCelda.setWrapText(true);
            estiloCelda.setFont(normal);
            estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THIN);

            if(n == 1)
            {
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
            }
            else
            {
                //estiloCelda.setBorderTop(HSSFCellStyle.BORDER_DOUBLE);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
            }

            if(numFila == listaPuestos.size())
            {
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
            }
            celda = fila.createCell(2);
            celda.setCellValue(filaI.getImporteConce()!= null ? filaI.getImporteConce().toUpperCase() : "");
            celda.setCellStyle(estiloCelda);

            //COLUMNA: IMPORTE SUBVENCIONABLE FINAL
            estiloCelda = libro.createCellStyle();
            estiloCelda.setWrapText(true);
            estiloCelda.setFont(normal);
            estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THIN);
            estiloCelda.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
            //estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
            if(n == 1)
            {
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
            }
            else
            {
                //estiloCelda.setBorderTop(HSSFCellStyle.BORDER_DOUBLE);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
            }
            if(numFila == listaPuestos.size())
            {
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
            }
            celda = fila.createCell(3);
            celda.setCellValue(filaI.getImporteSubv()!= null ? filaI.getImporteSubv() : "");
            //celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
            celda.setCellStyle(estiloCelda);


            //COLUMNA: PRIMER PAGO
            estiloCelda = libro.createCellStyle();
            estiloCelda.setWrapText(true);
            estiloCelda.setFont(normal);
            estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THIN);
            if(n == 1)
            {
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
            }
            else
            {
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
            }
            if(numFila == listaPuestos.size())
            {
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
            }
            celda = fila.createCell(4);
            celda.setCellValue(filaI.getPrimerPago()!= null ? filaI.getPrimerPago().toUpperCase() : "");
            celda.setCellStyle(estiloCelda);

            //COLUMNA: SEGUNDO PAGO
            estiloCelda = libro.createCellStyle();
            estiloCelda.setWrapText(true);
            estiloCelda.setFont(normal);
            estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THIN);
            if(n == 1)
            {
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
            }
            else
            {
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
            }
            if(numFila == listaPuestos.size())
            {
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
            }
            celda = fila.createCell(5);
            celda.setCellValue(filaI.getSegundoPago()!= null ? filaI.getSegundoPago().toString() : "");
            celda.setCellStyle(estiloCelda);

            //COLUMNA: D/
            estiloCelda = libro.createCellStyle();
            estiloCelda.setWrapText(true);
            estiloCelda.setFont(normal);
            estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THIN);

            if(n == 1)
            {
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
            }
            else
            {
                //estiloCelda.setBorderTop(HSSFCellStyle.BORDER_DOUBLE);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
            }
            if(numFila == listaPuestos.size())
            {
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
            }
            celda = fila.createCell(6);
            Double dato2 = 0.0;
            
            String impSubv = filaI.getImporteSubv().replace(",", ".");
            String impConce = filaI.getImporteConce().replace(",", ".");
            String pPago = filaI.getPrimerPago().replace(",", ".");
            String sPago = filaI.getSegundoPago().replace(",", ".");
            
            if(sPago.equals(""))
                sPago = "0";
            if(impConce.equals(""))
                impConce = "0";
            if(null != filaI.getImporteSubv() && null != filaI.getPrimerPago())
            {
                if(Double.valueOf(impSubv) <= Double.valueOf(pPago))
                {
                    dato2 = Double.valueOf(impConce) - Double.valueOf(pPago);
                }else{
                    dato2 = Double.valueOf(impConce) - Double.valueOf(pPago) - Double.valueOf(sPago);
                }
            }
            celda.setCellValue(dato2);
            celda.setCellStyle(estiloCelda);
            
            //COLUMNA: REINTEGRO
            estiloCelda = libro.createCellStyle();
            estiloCelda.setWrapText(true);
            estiloCelda.setFont(normal);
            estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THIN);

            if(n == 1)
            {
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
            }
            else
            {
                //estiloCelda.setBorderTop(HSSFCellStyle.BORDER_DOUBLE);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
            }
            if(numFila == listaPuestos.size())
            {
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
            }
            celda = fila.createCell(7);
            Double dato = 0.0;
            if(null != filaI.getImporteSubv() && null != filaI.getPrimerPago())
            {
                if(Double.valueOf(pPago) > Double.valueOf(impSubv))
                {
                    dato = Double.valueOf(pPago) - Double.valueOf(impSubv);
                }
            }
            celda.setCellValue(dato);
            celda.setCellStyle(estiloCelda);
        }catch(Exception ex){
            log.error("Error: " + ex);
        }
        
    }
    
    private String cargarSubpestanaSolicitud_DatosCme(String numExpediente, AdaptadorSQLBD adapt, HttpServletRequest request)
    {
        try
        {
            List<FilaPuestoVO> puestos = MeLanbide46Manager.getInstance().getListaPuestosPorExpediente(numExpediente, adapt);
            request.setAttribute("puestos", puestos);
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return "/jsp/extension/melanbide46/solicitud.jsp";
    }
    
    private String cargarSubpestanaOferta_DatosCme(String numExpediente, AdaptadorSQLBD adapt, HttpServletRequest request)
    {
        try
        {
            List<FilaOfertaVO> ofertas = MeLanbide46Manager.getInstance().getListaOfertasNoDenegadasPorExpediente(numExpediente, adapt);
            request.setAttribute("ofertas", ofertas);
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return "/jsp/extension/melanbide46/oferta.jsp";
    }
    
    private String cargarSubpestanaJustificacion_DatosCme(String numExpediente, AdaptadorSQLBD adapt, HttpServletRequest request)
    {
        try
        {
            List<FilaJustificacionVO> justificaciones = MeLanbide46Manager.getInstance().getListaJustificacionesNoDenegadasPorExpediente(numExpediente, adapt);
            request.setAttribute("justificaciones", justificaciones);
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return "/jsp/extension/melanbide46/justificacion.jsp";
    }
    
    private String cargarSubpestanaResumen_DatosCme(String numExpediente, AdaptadorSQLBD adapt, HttpServletRequest request)
    {
        try
        {
            List<FilaResumenVO> filasResumen = MeLanbide46Manager.getInstance().getListaResumenPorExpediente(numExpediente, adapt);
            log.error("antes de establecer el atributo resumen del request");
            request.setAttribute("resumen", filasResumen);
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
            log.error("Error en cargarSubpestanaResumen_DatosCpe: " + ex);
        }
        return "/jsp/extension/melanbide46/resumen.jsp";
    }
    
    private String cargarSubpestanaSolicitudHist_DatosCme(int codOrganizacion, String numExpediente, String codTramite, AdaptadorSQLBD adapt, HttpServletRequest request)
    {
        try
        {
            List<FilaPuestoVO> puestos = MeLanbide46Manager.getInstance().getListaPuestosHistPorExpediente(codOrganizacion, numExpediente, codTramite, adapt);
            request.setAttribute("puestos", puestos);
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return "/jsp/extension/melanbide46/solicitudHist.jsp";
    }
    
    private String cargarSubpestanaOfertaHist_DatosCme(int codOrganizacion, String numExpediente, String codTramite, AdaptadorSQLBD adapt, HttpServletRequest request)
    {
        try
        {
            List<FilaOfertaVO> ofertas = MeLanbide46Manager.getInstance().getListaOfertasHistNoDenegadasPorExpediente(codOrganizacion, numExpediente, codTramite, adapt);
            request.setAttribute("ofertas", ofertas);
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return "/jsp/extension/melanbide46/ofertaHist.jsp";
    }
    
    private String cargarSubpestanaJustificacionHist_DatosCme(int codOrganizacion, String numExpediente, String codTramite, AdaptadorSQLBD adapt, HttpServletRequest request)
    {
        try
        {
            List<FilaJustificacionVO> justificaciones = MeLanbide46Manager.getInstance().getListaJustificacionesHistNoDenegadasPorExpediente(codOrganizacion, numExpediente, codTramite, adapt);
            request.setAttribute("justificaciones", justificaciones);
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return "/jsp/extension/melanbide46/justificacionHist.jsp";
    }
    
    private void cargarDatosCme(int codOrganizacion, String numExpediente, HttpServletRequest request)
    {
        IModuloIntegracionExternoCamposFlexia el = ModuloIntegracionExternoCampoSupFactoria.getInstance().getImplClass();
        
        List<ValorCampoDesplegableModuloIntegracionVO> list = new ArrayList<ValorCampoDesplegableModuloIntegracionVO>();
        List<SelectItem> listaGestores = new ArrayList<SelectItem>();
        
        //Combo GESTOR TRAMITADOR
        try
        {
            String campoDesplegable = ConfigurationParameter.getParameter(ConstantesMeLanbide46.CAMPO_SUPL_GESTOR_TRAMITADOR, ConstantesMeLanbide46.FICHERO_PROPIEDADES);
        
            SalidaIntegracionVO salidaIntegracion = el.getCampoDesplegable(String.valueOf(codOrganizacion), campoDesplegable);
            list = salidaIntegracion.getCampoDesplegable().getValores();
            listaGestores = MeLanbide46MappingUtils.getInstance().fromCampoDesplegableToSelectItemVO(list);
        }
        catch(Exception ex)
        {
            
        }
        request.setAttribute("listaGestores", listaGestores);
        
        
        
        Integer ejercicio = null;
        try
        {
            ejercicio = MeLanbide46Utils.getEjercicioDeExpediente(numExpediente);
        }
        catch(Exception ex)
        {
            
        }
        
        if(ejercicio != null)
        {
            
            Map<String, BigDecimal> calculos = null;
            //Cargo el gestor tramitador, empresa e importes de la subvencion (camposSuplementarios)
            try
            {
                AdaptadorSQLBD adaptador = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
                String gestor = MeLanbide46Manager.getInstance().getValorCampoDesplegable(codOrganizacion, String.valueOf(ejercicio), numExpediente, ConfigurationParameter.getParameter(ConstantesMeLanbide46.CAMPO_SUPL_GESTOR_TRAMITADOR, ConstantesMeLanbide46.FICHERO_PROPIEDADES), adaptador);
                if(gestor != null && !gestor.equals(""))
                {
                    request.setAttribute("gestor", gestor);
                }
            
                String empresa = MeLanbide46Manager.getInstance().getValorCampoTexto(codOrganizacion, String.valueOf(ejercicio), numExpediente, ConfigurationParameter.getParameter(ConstantesMeLanbide46.CAMPO_SUPL_EMPRESA, ConstantesMeLanbide46.FICHERO_PROPIEDADES), adaptador);
                if(empresa != null && !empresa.equals(""))
                {
                    request.setAttribute("empresa", empresa);
                }
                else
                {
                    //Si no ha guardado en campo suplementario, precargamos con el tercero del expediente
                    HashMap<String, String> datosTercero = MeLanbide46Manager.getInstance().getDatosTercero(codOrganizacion, numExpediente, String.valueOf(ejercicio), ConstantesMeLanbide46.CODIGO_ROL_ENTIDAD_SOLICITANTE, adaptador);
                    if(datosTercero != null)
                    {
                        if(datosTercero.containsKey("TER_NOC"))
                        {
                            request.setAttribute("empresa", datosTercero.get("TER_NOC").toUpperCase());
                        }
                    }
                }
                            
                calculos = MeLanbide46Manager.getInstance().cargarCalculosCme(codOrganizacion, ejercicio, numExpediente, adaptador);
            }
            catch(Exception ex)
            {
                ex.printStackTrace();
            }
            
            if(calculos != null)
            {
                //importe solicitado
                BigDecimal impSolicitado = null;
                try
                {
                    impSolicitado = calculos.get(ConstantesMeLanbide46.CAMPO_SUPL_IMP_SOLICITADO);
                    if(impSolicitado != null)
                    {
                        request.setAttribute("solicitado", impSolicitado.toPlainString());
                    }
                    else
                    {
                        request.setAttribute("solicitado", new BigDecimal(ConstantesMeLanbide46.CERO_CON_DECIMALES).toPlainString());
                    }
                }
                catch(Exception ex)
                {
                    ex.printStackTrace();
                }

                //importe convocatoria
                BigDecimal impConv = null;
                try
                {
                    impConv = calculos.get(ConstantesMeLanbide46.CAMPO_SUPL_IMP_CONVOCATORIA);
                    if(impConv != null)
                    {
                        request.setAttribute("convocatoria", impConv.toPlainString());
                    }
                    else
                    {
                        request.setAttribute("convocatoria", new BigDecimal(ConstantesMeLanbide46.CERO_CON_DECIMALES).toPlainString());
                    }
                }
                catch(Exception ex)
                {
                    ex.printStackTrace();
                }

                //importe previsto concesion
                BigDecimal impPrevCon = null;
                try
                {
                    impPrevCon = calculos.get(ConstantesMeLanbide46.CAMPO_SUPL_IMP_PREV_CON);
                    if(impPrevCon != null)
                    {
                        request.setAttribute("previsto", impPrevCon.toPlainString());
                    }
                    else
                    {
                        request.setAttribute("previsto", new BigDecimal(ConstantesMeLanbide46.CERO_CON_DECIMALES).toPlainString());
                    }
                }
                catch(Exception ex)
                {
                    ex.printStackTrace();
                }
                
                //importe concedido
                BigDecimal concedido = null;
                try
                {
                    concedido = calculos.get(ConstantesMeLanbide46.CAMPO_SUPL_IMP_CON);
                    if(concedido != null)
                    {
                        request.setAttribute("concedido", concedido.toPlainString());
                    }
                    else
                    {
                        request.setAttribute("concedido", new BigDecimal(ConstantesMeLanbide46.CERO_CON_DECIMALES).toPlainString());
                    }
                }
                catch(Exception ex)
                {
                    ex.printStackTrace();
                }
                
                //importe justificado
                BigDecimal justificado = null;
                try
                {
                    justificado = calculos.get(ConstantesMeLanbide46.CAMPO_SUPL_IMP_JUS);
                    if(justificado != null)
                    {
                        request.setAttribute("justificado", justificado.toPlainString());
                    }
                    else
                    {
                        request.setAttribute("justificado", new BigDecimal(ConstantesMeLanbide46.CERO_CON_DECIMALES).toPlainString());
                    }
                }
                catch(Exception ex)
                {
                    ex.printStackTrace();
                }
                
                //importe no justificado
                BigDecimal noJustificado = null;
                try
                {
                    noJustificado = calculos.get(ConstantesMeLanbide46.CAMPO_SUPL_IMP_NO_JUS);
                    if(noJustificado != null)
                    {
                        request.setAttribute("noJustificado", noJustificado.toPlainString());
                    }
                    else
                    {
                        request.setAttribute("noJustificado", new BigDecimal(ConstantesMeLanbide46.CERO_CON_DECIMALES).toPlainString());
                    }
                }
                catch(Exception ex)
                {
                    ex.printStackTrace();
                }
                
                //importe renunciado
                BigDecimal renunciado = null;
                try
                {
                    renunciado = calculos.get(ConstantesMeLanbide46.CAMPO_SUPL_IMP_REN);
                    if(renunciado != null)
                    {
                        request.setAttribute("renunciado", renunciado.toPlainString());
                    }
                    else
                    {
                        request.setAttribute("renunciado", new BigDecimal(ConstantesMeLanbide46.CERO_CON_DECIMALES).toPlainString());
                    }
                }
                catch(Exception ex)
                {
                    ex.printStackTrace();
                }
                
                //importe renunciado resolucion
                BigDecimal renunciadoRes = null;
                try
                {
                    renunciadoRes = calculos.get(ConstantesMeLanbide46.CAMPO_SUPL_IMP_REN_RES);
                    if(renunciadoRes != null)
                    {
                        request.setAttribute("renunciadoRes", renunciadoRes.toPlainString());
                    }
                    else
                    {
                        request.setAttribute("renunciadoRes", new BigDecimal(ConstantesMeLanbide46.CERO_CON_DECIMALES).toPlainString());
                    }
                }
                catch(Exception ex)
                {
                    ex.printStackTrace();
                }
                
                //importe pagado
                BigDecimal impPag = null;
                try
                {
                    impPag = calculos.get(ConstantesMeLanbide46.CAMPO_SUPL_IMP_PAG);
                    if(impPag != null)
                    {
                        request.setAttribute("pagado", impPag.toPlainString());
                    }
                    else
                    {
                        request.setAttribute("pagado", new BigDecimal(ConstantesMeLanbide46.CERO_CON_DECIMALES).toPlainString());
                    }
                }
                catch(Exception ex)
                {
                    ex.printStackTrace();
                }
                
                //importe pagado resolucion
                BigDecimal impPagRes = null;
                try
                {
                    impPagRes = calculos.get(ConstantesMeLanbide46.CAMPO_SUPL_IMP_PAG_RES);
                    if(impPagRes != null)
                    {
                        request.setAttribute("pagadoRes", impPagRes.toPlainString());
                    }
                    else
                    {
                        request.setAttribute("pagadoRes", new BigDecimal(ConstantesMeLanbide46.CERO_CON_DECIMALES).toPlainString());
                    }
                }
                catch(Exception ex)
                {
                    ex.printStackTrace();
                }

                //importe segundo pago
                BigDecimal impPag2 = null;
                try
                {
                    impPag2 = calculos.get(ConstantesMeLanbide46.CAMPO_SUPL_IMP_PAG_2);
                    if(impPag2 != null)
                    {
                        request.setAttribute("pagado2", impPag2.toPlainString());
                    }
                    else
                    {
                        request.setAttribute("pagado2", new BigDecimal(ConstantesMeLanbide46.CERO_CON_DECIMALES).toPlainString());
                    }
                }
                catch(Exception ex)
                {
                    ex.printStackTrace();
                }
                
                //importe reintegrar
                BigDecimal reint = null;
                try
                {
                    reint = calculos.get(ConstantesMeLanbide46.CAMPO_SUPL_IMP_REI);
                    if(reint != null)
                    {
                        request.setAttribute("reintegrar", reint.toPlainString());
                    }
                    else
                    {
                        request.setAttribute("reintegrar", new BigDecimal(ConstantesMeLanbide46.CERO_CON_DECIMALES).toPlainString());
                    }
                }
                catch(Exception ex)
                {
                    ex.printStackTrace();
                }
                
                //otras ayudas solicitado
                BigDecimal oayuS = null;
                try
                {
                    oayuS = calculos.get(ConstantesMeLanbide46.CAMPO_SUPL_OTRAS_AYUDAS_SOLIC);
                    if(oayuS != null)
                    {
                        request.setAttribute("otrasAyudasSolic", oayuS.toPlainString());
                    }
                    else
                    {
                        request.setAttribute("otrasAyudasSolic", new BigDecimal(ConstantesMeLanbide46.CERO_CON_DECIMALES).toPlainString());
                    }
                }
                catch(Exception ex)
                {
                    ex.printStackTrace();
                }
                
                //otras ayudas concedido
                BigDecimal oayuC = null;
                try
                {
                    oayuC = calculos.get(ConstantesMeLanbide46.CAMPO_SUPL_OTRAS_AYUDAS_CONCE);
                    if(oayuC != null)
                    {
                        request.setAttribute("otrasAyudasConce", oayuC.toPlainString());
                    }
                    else
                    {
                        request.setAttribute("otrasAyudasConce", new BigDecimal(ConstantesMeLanbide46.CERO_CON_DECIMALES).toPlainString());
                    }
                }
                catch(Exception ex)
                {
                    ex.printStackTrace();
                }
                
                //minimis solicitado
                BigDecimal minS = null;
                try
                {
                    minS = calculos.get(ConstantesMeLanbide46.CAMPO_SUPL_MINIMIS_SOLIC);
                    if(minS != null)
                    {
                        request.setAttribute("minimisSolic", minS.toPlainString());
                    }
                    else
                    {
                        request.setAttribute("minimisSolic", new BigDecimal(ConstantesMeLanbide46.CERO_CON_DECIMALES).toPlainString());
                    }
                }
                catch(Exception ex)
                {
                    ex.printStackTrace();
                }
                
                //minimis concedido
                BigDecimal minC = null;
                try
                {
                    minC = calculos.get(ConstantesMeLanbide46.CAMPO_SUPL_MINIMIS_CONCE);
                    if(minC != null)
                    {
                        request.setAttribute("minimisConce", minC.toPlainString());
                    }
                    else
                    {
                        request.setAttribute("minimisConce", new BigDecimal(ConstantesMeLanbide46.CERO_CON_DECIMALES).toPlainString());
                    }
                }
                catch(Exception ex)
                {
                    ex.printStackTrace();
                }
                
                //importe por despido
                BigDecimal impDesp = null;
                try
                {
                    impDesp = calculos.get(ConstantesMeLanbide46.CAMPO_SUPL_IMP_DESP);
                    if(impDesp != null)
                    {
                        request.setAttribute("importeDespido", impDesp.toPlainString());
                    }
                    else
                    {
                        request.setAttribute("importeDespido", new BigDecimal(ConstantesMeLanbide46.CERO_CON_DECIMALES).toPlainString());
                    }
                }
                catch(Exception ex)
                {
                    ex.printStackTrace();
                }
                
                //importe por bajas
                BigDecimal impBaj = null;
                try
                {
                    impBaj = calculos.get(ConstantesMeLanbide46.CAMPO_SUPL_IMP_BAJA);
                    if(impBaj != null)
                    {
                        request.setAttribute("importeBajas", impBaj.toPlainString());
                    }
                    else
                    {
                        request.setAttribute("importeBajas", new BigDecimal(ConstantesMeLanbide46.CERO_CON_DECIMALES).toPlainString());
                    }
                }
                catch(Exception ex)
                {
                    ex.printStackTrace();
                }
                
                //concedido real
                BigDecimal concedidoReal = null;
                try
                {
                    concedidoReal = calculos.get("CONCEDIDO_REAL");
                    if(concedidoReal != null)
                    {
                        request.setAttribute("concedidoReal", concedidoReal.toPlainString());
                    }
                    else
                    {
                        request.setAttribute("concedidoReal", new BigDecimal(ConstantesMeLanbide46.CERO_CON_DECIMALES).toPlainString());
                    }
                }
                catch(Exception ex)
                {
                    ex.printStackTrace();
                }
                
                //pagado real
                BigDecimal pagadoReal = null;
                try
                {
                    pagadoReal = calculos.get("PAGADO_REAL");
                    if(pagadoReal != null)
                    {
                        request.setAttribute("pagadoReal", pagadoReal.toPlainString());
                    }
                    else
                    {
                        request.setAttribute("pagadoReal", new BigDecimal(ConstantesMeLanbide46.CERO_CON_DECIMALES).toPlainString());
                    }
                }
                catch(Exception ex)
                {
                    ex.printStackTrace();
                }
                
                //segundo pago real
                BigDecimal pagadoReal2 = null;
                try
                {
                    pagadoReal2 = calculos.get("PAGADO_REAL_2");
                    if(pagadoReal2 != null)
                    {
                        request.setAttribute("pagadoReal2", pagadoReal2.toPlainString());
                    }
                    else
                    {
                        request.setAttribute("pagadoReal2", new BigDecimal(ConstantesMeLanbide46.CERO_CON_DECIMALES).toPlainString());
                    }
                }
                catch(Exception ex)
                {
                    ex.printStackTrace();
                }
            }
        } 
        
        try
        {
            boolean tramEncontrado = false;
            AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
            //Miro a ver si tiene iniciado el tramite de "Resolucion de concesion o denegacion"
            Long codTramRes = MeLanbide46Manager.getInstance().obtenerCodigoInternoTramite(codOrganizacion, ConstantesMeLanbide46.CODIGO_TRAMITE_RESOLUCION, adapt);
            if(codTramRes != null)
            {
                if(MeLanbide46Manager.getInstance().tieneTramiteFinalizado(codOrganizacion, ejercicio, numExpediente, codTramRes, this.getAdaptSQLBD(String.valueOf(codOrganizacion))))
                {
                    request.setAttribute("tramRes", true);
                    tramEncontrado = true;
                }
            }
            
            if(tramEncontrado)
            {
                //Miro a ver si tiene iniciado el tramite de "Resolucion de concesion o denegacion"
                Long codTramResModif = MeLanbide46Manager.getInstance().obtenerCodigoInternoTramite(codOrganizacion, ConstantesMeLanbide46.CODIGO_TRAMITE_RESOLUCION_MODIF, adapt);
                if(codTramResModif != null)
                {
                    if(MeLanbide46Manager.getInstance().tieneTramiteFinalizado(codOrganizacion, ejercicio, numExpediente, codTramResModif, this.getAdaptSQLBD(String.valueOf(codOrganizacion))))
                    {
                        request.setAttribute("tramResModif", true);
                    }
                }
            }
        }
        catch(Exception ex)
        {
            
        }
    }
    
    
    public void getListaResumenPorExpediente(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response)
    {
        String codigoOperacion = "0";
        try
        {
            //Al guardar una oferta, se inserta este mensaje en sesion como resultado
            //Justo despues de guardar se llama a este metodo para recargar la tabla.
            //Este es el punto donde se borra este mensaje de sesion, para no mantenerlo en memoria innecesariamente.
            codigoOperacion = request.getSession().getAttribute("mensajeImportar") != null ? (String)request.getSession().getAttribute("mensajeImportar") : "0";
            request.getSession().removeAttribute("mensajeImportar");
        }
        catch(Exception ex)
        {

        }
        List<FilaResumenVO> filasResumen = new ArrayList<FilaResumenVO>();
        //Map<String, BigDecimal> calculos = new HashMap<String, BigDecimal>();
        try
        {
            Integer ejercicio = MeLanbide46Utils.getEjercicioDeExpediente(numExpediente);
            if(ejercicio != null)
            {
                AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
                filasResumen = MeLanbide46Manager.getInstance().getListaResumenPorExpediente(numExpediente, adapt);
                //calculos = MeLanbide39Manager.getInstance().cargarCalculosCpe(codOrganizacion, ejercicio, numExpediente, adapt);
            }
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        
        escribirListaResumenRequest(codigoOperacion, filasResumen, response);
    }
    
    private void escribirListaResumenRequest(String codigoOperacion, List<FilaResumenVO> filasResumen, HttpServletResponse response)
    {
        StringBuffer xmlSalida = new StringBuffer();
        xmlSalida.append("<RESPUESTA>");
            xmlSalida.append("<CODIGO_OPERACION>");
                xmlSalida.append(codigoOperacion);
            xmlSalida.append("</CODIGO_OPERACION>");
            
        for(FilaResumenVO filaResumen : filasResumen)
        {
            xmlSalida.append("<FILA>");
                xmlSalida.append("<NOM_APE>");
                        xmlSalida.append(filaResumen.getNomApe());
                xmlSalida.append("</NOM_APE>");
                xmlSalida.append("<DNI>");
                        xmlSalida.append(filaResumen.getDni());
                xmlSalida.append("</DNI>");
                xmlSalida.append("<PAIS_SOLICITADO>");
                        xmlSalida.append(filaResumen.getPaisSolicitado());
                xmlSalida.append("</PAIS_SOLICITADO>");
                xmlSalida.append("<RANGO_EDAD_CONCE>");
                    xmlSalida.append(filaResumen.getRangoEdadConce());
                xmlSalida.append("</RANGO_EDAD_CONCE>");
                xmlSalida.append("<RANGO_EDAD_CONTRA>");
                    xmlSalida.append(filaResumen.getRangoEdadContra());
                xmlSalida.append("</RANGO_EDAD_CONTRA>");
                xmlSalida.append("<MESES_CONTRATO>");
                    xmlSalida.append(filaResumen.getMesesContrato());
                xmlSalida.append("</MESES_CONTRATO>");        
                xmlSalida.append("<IMP_MAX_SUBV>");
                    xmlSalida.append(filaResumen.getImporteMaxSubv());
                xmlSalida.append("</IMP_MAX_SUBV>");                   
                xmlSalida.append("<MINORACION>");
                        xmlSalida.append(filaResumen.getMinoracion());
                xmlSalida.append("</MINORACION>");                     
                xmlSalida.append("<IMPROTE-MINO>");
                        xmlSalida.append(filaResumen.getImporteConceMinora());
                xmlSalida.append("</IMPROTE-MINO>");          
                xmlSalida.append("<FEC_INI>");
                    xmlSalida.append(filaResumen.getFecIni());
                xmlSalida.append("</FEC_INI>");
                xmlSalida.append("<FEC_FIN>");
                        xmlSalida.append(filaResumen.getFecFin());
                xmlSalida.append("</FEC_FIN>");
                xmlSalida.append("<TOTAL_DIAS>");
                        xmlSalida.append(filaResumen.getTotalDias());
                xmlSalida.append("</TOTAL_DIAS>");                
                xmlSalida.append("<BC_PERIODO>");
                        xmlSalida.append(filaResumen.getBcPeriodo());
                xmlSalida.append("</BC_PERIODO>");                
                xmlSalida.append("<COEFICIENTE_TGSS>");
                        xmlSalida.append(filaResumen.getCoeficienteTGSS());
                xmlSalida.append("</COEFICIENTE_TGSS>");
                
//                xmlSalida.append("<SALARIO_ANEXO>");
//                        xmlSalida.append(filaResumen.getSalarioAnexo());
//                xmlSalida.append("</SALARIO_ANEXO>");
//                xmlSalida.append("<MAXIMO_SUBV>");
//                        xmlSalida.append(filaResumen.getMaximoSubv());
//                xmlSalida.append("</MAXIMO_SUBV>");
//                xmlSalida.append("<MINORACION>");
//                        xmlSalida.append(filaResumen.getMinoracion());
//                xmlSalida.append("</MINORACION>");
//                xmlSalida.append("<DEVENGADO>");
//                        xmlSalida.append(filaResumen.getDevengado());
//                xmlSalida.append("</DEVENGADO>");
//                xmlSalida.append("<BC_PERIODO>");
//                        xmlSalida.append(filaResumen.getBcPeriodo());
//                xmlSalida.append("</BC_PERIODO>");
//                xmlSalida.append("<COEFICIENTE_TGSS>");
//                        xmlSalida.append(filaResumen.getCoeficienteTGSS());
//                xmlSalida.append("</COEFICIENTE_TGSS>");
                
                xmlSalida.append("<SS_EMPRESA>");
                        xmlSalida.append(filaResumen.getSsEmpresa());
                xmlSalida.append("</SS_EMPRESA>");
                xmlSalida.append("<BC_AT>");
                        xmlSalida.append(filaResumen.getBcAT());
                xmlSalida.append("</BC_AT>");
                xmlSalida.append("<COEFICIENTE_FOGASA>");
                        xmlSalida.append(filaResumen.getCoeficienteFogasa());
                xmlSalida.append("</COEFICIENTE_FOGASA>");
                xmlSalida.append("<COEFICIENTE_AT>");
                        xmlSalida.append(filaResumen.getCoefiecienteAT());
                xmlSalida.append("</COEFICIENTE_AT>");
                xmlSalida.append("<FOGASA_AT_EMPRESA>");
                        xmlSalida.append(filaResumen.getFogasaATEmpresa());
                xmlSalida.append("</FOGASA_AT_EMPRESA>");
                xmlSalida.append("<PORC_EPSV>");
                        xmlSalida.append(filaResumen.getPorcEPSV());
                xmlSalida.append("</PORC_EPSV>");
                xmlSalida.append("<APORT_EPSV>");
                        xmlSalida.append(filaResumen.getAportEPSV());
                xmlSalida.append("</APORT_EPSV>");
                xmlSalida.append("<TOTAL_EMPRESA>");
                        xmlSalida.append(filaResumen.getTotalEmpresa());
                xmlSalida.append("</TOTAL_EMPRESA>");
                xmlSalida.append("<SALARIO>");
                        xmlSalida.append(filaResumen.getCostePeriodoSubv());
                xmlSalida.append("</SALARIO>");
                xmlSalida.append("<SUBV_MINORADA>");
                        xmlSalida.append(filaResumen.getImporteMaxSubv());
                xmlSalida.append("</SUBV_MINORADA>");
                xmlSalida.append("<MAXIMO_PERIODO_SUBV>");
                        xmlSalida.append(filaResumen.getMaximoPeriodoSubv());
                xmlSalida.append("</MAXIMO_PERIODO_SUBV>");                  
                xmlSalida.append("<MINO>");
                        xmlSalida.append(filaResumen.getMinoracion());
                xmlSalida.append("</MINO>");   
                xmlSalida.append("<SUBVFINAL>");
                        xmlSalida.append(filaResumen.getSubvFinal());
                xmlSalida.append("</SUBVFINAL>");
                xmlSalida.append("<DIETAS_PERIODO_SUBV>");
                        xmlSalida.append(filaResumen.getDietasPeriodoSubv());
                xmlSalida.append("</DIETAS_PERIODO_SUBV>");
                xmlSalida.append("<DIETAS_CONCEDIDAS>");
                        xmlSalida.append(filaResumen.getDietasConcedidas());
                xmlSalida.append("</DIETAS_CONCEDIDAS>");
                xmlSalida.append("<MAXIMO_DIETAS>");
                        xmlSalida.append(filaResumen.getMaximoDietas());
                xmlSalida.append("</MAXIMO_DIETAS>");
                xmlSalida.append("<VISADO_ABONADO>");
                        xmlSalida.append(filaResumen.getVisadoAbonado());
                xmlSalida.append("</VISADO_ABONADO>");
                xmlSalida.append("<VISADO_CONCEDIDO>");
                        xmlSalida.append(filaResumen.getVisadoConcedido());
                xmlSalida.append("</VISADO_CONCEDIDO>");
                xmlSalida.append("<VISADO_SUBV>");
                        xmlSalida.append(filaResumen.getVisadoSubv());
                xmlSalida.append("</VISADO_SUBV>");
//                xmlSalida.append("<CONTRATO_BONIF>");
//                        xmlSalida.append(filaResumen.getContratoBonif());
//                xmlSalida.append("</CONTRATO_BONIF>");
                xmlSalida.append("<COSTE_SUBVENCIONABLE>");
                        xmlSalida.append(filaResumen.getCosteSubvecionable());
                xmlSalida.append("</COSTE_SUBVENCIONABLE>");
                xmlSalida.append("<RESOLINICIAL>");
                        xmlSalida.append(filaResumen.getResolInical());
                xmlSalida.append("</RESOLINICIAL>");
                xmlSalida.append("<PRIMERPAGO>");
                        xmlSalida.append(filaResumen.getPrimerPago());
                xmlSalida.append("</PRIMERPAGO>");
                xmlSalida.append("<SEGUNDOPAGO>");
                        xmlSalida.append(filaResumen.getSegundoPago());
                xmlSalida.append("</SEGUNDOPAGO>");
                xmlSalida.append("<REINTEGRO>");
                        xmlSalida.append(filaResumen.getReintegro());
                xmlSalida.append("</REINTEGRO>");
                xmlSalida.append("<D>");
                        xmlSalida.append(filaResumen.getD());
                xmlSalida.append("</D>");
//                xmlSalida.append("<MESES_EXT>");
//                        xmlSalida.append(filaResumen.getMesesExt());
//                xmlSalida.append("</MESES_EXT>");
//                xmlSalida.append("<SALARIO_P>");
//                        xmlSalida.append(filaResumen.getSalario());
//                xmlSalida.append("</SALARIO_P>");
            xmlSalida.append("</FILA>");
            
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
            e.printStackTrace();
        }//try-catch
    }
    
    public String guardarOferta(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response)
    {
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
            AdaptadorSQLBD adaptador = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
            Integer ejercicio = MeLanbide46Utils.getEjercicioDeExpediente(numExpediente);
            if(ejercicio != null)
            {
                SimpleDateFormat format = new SimpleDateFormat(ConstantesMeLanbide46.FORMATO_FECHA);
                
                MultipartRequestWrapper wrapper = new MultipartRequestWrapper(request);
            
                wrapper.getParameterMap();
                CommonsMultipartRequestHandler handler = new CommonsMultipartRequestHandler();
                handler.handleRequest(request);
                Hashtable<String, FormFile> fileTable = handler.getFileElements();
                Hashtable<String, String[]> textTable = handler.getTextElements();
                
                String codOferta = (String)request.getParameter("idOferta");
                String codPuesto = (String)request.getParameter("idPuesto");
                String descPuesto = textTable.get("descPuesto")[0];
                String codpais1 = textTable.get("codPaisPuesto1")[0];
                String codpais2 = textTable.get("codPaisPuesto2")[0];
                String codpais3 = textTable.get("codPaisPuesto3")[0];
                String ciudaddestino = textTable.get("ciudadDestino")[0];
                String departamento = textTable.get("dpto")[0];
                String codtitu1 = textTable.get("codTitulacionPuesto1")[0];
                String codtitu2 = textTable.get("codTitulacionPuesto2")[0];
                String codtitu3 = textTable.get("codTitulacionPuesto3")[0];
                String funciones = textTable.get("funcionesPuesto")[0];
                String codidioma1 = textTable.get("codIdiomaPuesto1")[0];
                String codidioma2 = textTable.get("codIdiomaPuesto2")[0];
                String codidioma3 = textTable.get("codIdiomaPuesto3")[0];                
                String codnividioma1 = textTable.get("codNivelIdiomaPuesto1")[0];
                String codnividioma2 = textTable.get("codNivelIdiomaPuesto2")[0];
                String codnividioma3 = textTable.get("codNivelIdiomaPuesto3")[0];
                String codnivformativo = textTable.get("codNivelFormativo")[0];                
                String numOferta = textTable.get("txtNumOferta")[0];
                String ofiGest = textTable.get("codOfiGestionOferta")[0];
                String prec = (String)request.getParameter("checkPrecandidatosOferta");
                String nomPrec = textTable.get("txtNomPrecandidatos")[0];
                String difu = (String)request.getParameter("checkDifusionOferta");
                String feDifu = textTable.get("fechaDifusionOferta")[0];
                String feEnvC = textTable.get("fechaEnvioCandidatosOferta")[0];
                String nCand = textTable.get("txtNumCandidatosEnv")[0];
                String contratacion = (String)request.getParameter("radioCont");
                String contratacionPresOferta = (String)request.getParameter("radioPresOferta");
                String motivo = textTable.get("motivoNoCont")[0];
                String tDoc = textTable.get("codNifContratado")[0];
                String nif = textTable.get("txtNifOferta")[0];
                String nom = textTable.get("txtNombre")[0];
                String ape1 = textTable.get("txtApe1")[0];
                String ape2 = textTable.get("txtApe2")[0];
                String mail = textTable.get("txtEmail")[0];
                String telf = textTable.get("txtTfno")[0];
                String sex = textTable.get("txtSexo")[0];
                String fNac = textTable.get("fechaNacContratado")[0];
                String tit = textTable.get("codTitulacionContratado")[0];
                String anoTit = textTable.get("anoTitulacion")[0];
                String feIni = textTable.get("fechaIniContratado")[0];
                String feFin = textTable.get("fechaFinContratado")[0];
                String duracionContrato = textTable.get("txtMesesContratado")[0];
                String gCot = textTable.get("codGrupoCotContratado")[0];
                String salb = textTable.get("txtSalarioContratado")[0];
                String dConv = textTable.get("txtDietasConv")[0];
                String dConvoc = textTable.get("txtDietasConvoc")[0];
                String feBaja = textTable.get("fechaBaja")[0];
                String causaBaja = textTable.get("codCausaBaja")[0];
                String sust = (String)request.getParameter("radioSustituye");
                String obs = textTable.get("observacionesBaja")[0];
                String alta = (String)request.getParameter("alta");
                String copiar = (String)request.getParameter("copiar");
                String codCausaRenuncia = (String)request.getParameter("codCausaRenuncia");
                String codCausaRenunciaPresOferta = (String)request.getParameter("codCausaRenunciaPresOferta");
                
                //Ficheros
                FormFile docContratacion = fileTable.get("docContratacion");
                FormFile docGestionOferta = fileTable.get("docGestionOferta");
                
//                String codOferta = (String)request.getParameter("idOferta");
//                String codPuesto = (String)request.getParameter("idPuesto");
//                String numOferta = (String)request.getParameter("numOferta");
//                String ofiGest = (String)request.getParameter("codOfiGest");
//                String prec = (String)request.getParameter("prec");
//                String nomPrec = (String)request.getParameter("nomPrec");
//                String difu = (String)request.getParameter("difusion");
//                String feDifu = (String)request.getParameter("fecDifusion");
//                String feEnvC = (String)request.getParameter("fecEnvCand");
//                String nCand = (String)request.getParameter("numTotCand");
//                String contratacion = (String)request.getParameter("contratacion");
//                String motivo = (String)request.getParameter("motivo");
//                String tDoc = (String)request.getParameter("tipoDoc");
//                String nif = (String)request.getParameter("nif");
//                String nom = (String)request.getParameter("nombre");
//                String ape1 = (String)request.getParameter("ape1");
//                String ape2 = (String)request.getParameter("ape2");
//                String mail = (String)request.getParameter("email");
//                String telf = (String)request.getParameter("tfno");
//                String sex = (String)request.getParameter("sexo");
//                String fNac = (String)request.getParameter("fNac");
//                String tit = (String)request.getParameter("codTit");
//                String anoTit = (String)request.getParameter("anoTit");
//                String feIni = (String)request.getParameter("feIni");
//                String feFin = (String)request.getParameter("feFin");
//                String gCot = (String)request.getParameter("grCot");
//                String salb = (String)request.getParameter("salarioB");
//                String dConv = (String)request.getParameter("dietasConv");
//                String dConvoc = (String)request.getParameter("dietasConvoc");
//                String feBaja = (String)request.getParameter("feBaja");
//                String causaBaja = (String)request.getParameter("causaBaja");
//                String sust = (String)request.getParameter("sustituto");
//                String obs = (String)request.getParameter("observaciones");
                

                CmeOfertaVO oferta = null;
                if(codOferta != null && !codOferta.equals("") && codPuesto != null && !codPuesto.equals(""))
                {
                    oferta = new CmeOfertaVO();
                    oferta.setIdOferta(Integer.parseInt(codOferta));
                    oferta.setNumExp(numExpediente);
                    oferta.setExpEje(ejercicio);
                    oferta.setCodPuesto(codPuesto);
                    oferta = MeLanbide46Manager.getInstance().getOfertaPorCodigoPuestoYExpediente(oferta, adaptador);
                }
                else
                {
                    oferta = new CmeOfertaVO();
                    oferta.setNumExp(numExpediente);
                    oferta.setExpEje(ejercicio);
                    oferta.setCodPuesto(codPuesto);
                }
                if(oferta == null)
                {
                    codigoOperacion = "3";
                }
                else
                {
                    oferta.setDescPuesto(descPuesto!= null && !descPuesto.equals("") ? descPuesto.toUpperCase() : null);
                    oferta.setPaiCod1(codpais1 != null && !codpais1.equals("") ? Integer.parseInt(codpais1) : null);
                    oferta.setPaiCod2(codpais2 != null && !codpais2.equals("") ? Integer.parseInt(codpais2) : null);
                    oferta.setPaiCod3(codpais3 != null && !codpais3.equals("") ? Integer.parseInt(codpais3) : null);
                    oferta.setCiudadDestino(ciudaddestino!= null && !ciudaddestino.equals("") ? ciudaddestino.toUpperCase() : null);
                    oferta.setDpto(departamento!= null && !departamento.equals("") ? departamento.toUpperCase() : null);
                    oferta.setCodTit1(codtitu1 != null && !codtitu1.equals("") ? codtitu1 : null);
                    oferta.setCodTit2(codtitu2 != null && !codtitu2.equals("") ? codtitu2 : null);
                    oferta.setCodTit3(codtitu3 != null && !codtitu3.equals("") ? codtitu3 : null);
                    oferta.setFunciones(funciones!= null && !funciones.equals("") ? funciones.toUpperCase() : null);
                    oferta.setCodIdioma1(codidioma1 != null && !codidioma1.equals("") ? codidioma1 : null);
                    oferta.setCodIdioma2(codidioma2 != null && !codidioma2.equals("") ? codidioma2 : null);
                    oferta.setCodIdioma3(codidioma3 != null && !codidioma3.equals("") ? codidioma3 : null);
                    oferta.setCodNivIdi1(codnividioma1 != null && !codnividioma1.equals("") ? codnividioma1 : null);
                    oferta.setCodNivIdi2(codnividioma2 != null && !codnividioma2.equals("") ? codnividioma2 : null);
                    oferta.setCodNivIdi3(codnividioma3 != null && !codnividioma3.equals("") ? codnividioma3 : null);
                    oferta.setCodNivForm(codnivformativo != null && !codnivformativo.equals("") ? codnivformativo : null);
                                        
                    oferta.setAnoTitulacion(anoTit != null && !anoTit.equals("") ? Integer.parseInt(anoTit) : null);
                    oferta.setApe1(ape1 != null && !ape1.equals("") ? ape1.toUpperCase() : null);
                    oferta.setApe2(ape2 != null && !ape2.equals("") ? ape2.toUpperCase() : null);
                    oferta.setCodCausaBaja(causaBaja != null && !causaBaja.equals("") ? causaBaja : null);
                    oferta.setCodGrCot(gCot != null && !gCot.equals("") ? gCot : null);
                    oferta.setCodOferta(numOferta != null && !numOferta.equals("") ? numOferta.toUpperCase() : null);
                    oferta.setCodOfiGest(ofiGest != null && !ofiGest.equals("") ? ofiGest : null);
                    oferta.setCodTipoNif(tDoc != null && !tDoc.equals("") ? tDoc : null);
                    oferta.setCodTitulacion(tit != null && !tit.equals("") ? tit : null);
                    oferta.setContratacion(contratacion != null && !contratacion.equals("") ? contratacion.toUpperCase() : null);
                    oferta.setContratacionPresOferta(contratacionPresOferta != null && !contratacionPresOferta.equals("") ? contratacionPresOferta.toUpperCase() : null);
                    oferta.setDietasConvenio(dConv != null && !dConv.equals("") ? new BigDecimal(dConv.replaceAll(",", "\\.")) : null);
                    oferta.setDietasConvoc(dConvoc != null && !dConvoc.equals("") ? new BigDecimal(dConvoc.replaceAll(",", "\\.")) : null);
                    oferta.setDifusion(difu != null && !difu.equals("") ? difu.toUpperCase() : null);
                    oferta.setEmail(mail != null && !mail.equals("") ? mail : null);
                    oferta.setFecBaja(feBaja != null && !feBaja.equals("") ? format.parse(feBaja) : null);
                    if(difu != null && difu.equalsIgnoreCase(ConstantesMeLanbide46.CIERTO))
                    {
                        oferta.setFecDifusion(feDifu != null && !feDifu.equals("") ? format.parse(feDifu) : null);
                    }
                    else
                    {
                        oferta.setFecDifusion(null);
                    }
                    oferta.setFecEnvCand(feEnvC != null && !feEnvC.equals("") ? format.parse(feEnvC) : null);
                    oferta.setFecFin(feFin != null && !feFin.equals("") ? format.parse(feFin) : null);
                    oferta.setFecIni(feIni != null && !feIni.equals("") ? format.parse(feIni) : null);
                    oferta.setMesesContrato(duracionContrato != null && !duracionContrato.equals("") ? Integer.parseInt(duracionContrato) : null);
                    oferta.setFecNac(fNac != null && !fNac.equals("") ? format.parse(fNac) : null);
                    oferta.setFlSustituto(sust != null && !sust.equals("") ? sust.toUpperCase() : "");
                    if(contratacion != null && contratacion.equalsIgnoreCase(ConstantesMeLanbide46.FALSO))
                    {
                        oferta.setMotContratacion(motivo != null && !motivo.equals("") ? motivo.toUpperCase() : null);
                        oferta.setCodCausaRenuncia(codCausaRenuncia);
                    }
                    else
                    {
                        oferta.setMotContratacion(null);
                        oferta.setCodCausaRenuncia(null);
                    }
                    
                    if(contratacionPresOferta != null && contratacionPresOferta.equalsIgnoreCase(ConstantesMeLanbide46.FALSO))
                    {
                        oferta.setCodCausaRenunciaPresOferta(codCausaRenunciaPresOferta);
                    }
                    else
                    {
                        oferta.setCodCausaRenunciaPresOferta(null);
                    }
                    oferta.setNif(nif != null && !nif.equals("") ? nif.toUpperCase() : null);
                    oferta.setNombre(nom != null && !nom.equals("") ? nom.toUpperCase() : null);
                    oferta.setNumTotCand(nCand != null && !nCand.equals("") ? Integer.parseInt(nCand) : null);
                    oferta.setObservaciones(obs != null && !obs.equals("") ? obs.toUpperCase() : null);
                    oferta.setPrec(prec != null && !prec.equals("") ? prec : null);
                    if(prec != null && prec.equalsIgnoreCase(ConstantesMeLanbide46.CIERTO))
                    {
                        oferta.setPrecNom(nomPrec != null && !nomPrec.equals("") ? nomPrec.toUpperCase() : null);
                    }
                    else
                    {
                        oferta.setPrecNom(null);
                    }
                    oferta.setSalarioB(salb != null && !salb.equals("") ? new BigDecimal(salb.replaceAll(",", "\\.")) : null);
                    oferta.setSexo(sex != null && !sex.equals("") ? sex.toUpperCase() : null);
                    oferta.setTlf(telf != null && !telf.equals("") ? telf.toUpperCase() : null);
                    
                    if(docContratacion != null && docContratacion.getFileName() != null && !docContratacion.getFileName().equals(""))
                    {
                        oferta.setDocContratacion(docContratacion.getFileData());
                        oferta.setNomDocContratacion(docContratacion.getFileName());
                    }
                    
                    if(docGestionOferta != null && docGestionOferta.getFileName() != null && !docGestionOferta.getFileName().equals(""))
                    {
                        oferta.setDocGestOfe(docGestionOferta.getFileData());
                        oferta.setNomDocGestOfe(docGestionOferta.getFileName());
                    }
                    
                    //TODO: se podria validar los datos de la oferta
                    
                    
                    MeLanbide46Manager.getInstance().guardarCmeOfertaVO(codOrganizacion, oferta, alta, copiar, adaptador);
                    
                    //#241414
                    //al modificar las fechas de contrato se debe recalcular el núm días de justificación (si hay fila de justificación)
                    if ( oferta.getNif()!=null && !oferta.getNif().isEmpty() && oferta.getFecIni()!=null && oferta.getFecFin()!=null){
                        CmeJustificacionVO justifModif = new CmeJustificacionVO();
                        justifModif.setCodPuesto(oferta.getCodPuesto());
                        justifModif.setEjercicio(ejercicio);
                        //justifModif.setIdJustificacion(Integer.parseInt(idJustif));                       
                        justifModif.setIdOferta(oferta.getIdOferta());
                        justifModif.setNumExpediente(numExpediente);
                        justifModif = MeLanbide46Manager.getInstance().getJustificacionPorCodigoPuestoOfertaYExpediente(justifModif, adaptador);                    
                        int dias=0;
                        //String rdo = MeLanbide39Manager.getInstance().obtenerDiasTrabajados(numExpediente, oferta.getNif(), adaptador); 
                         String rdo = MeLanbide46Manager.getInstance().obtenerDiasTrabajados(numExpediente, oferta.getCodPuesto(),oferta.getIdOferta(), adaptador);
                        if(rdo != null && !rdo.equals(""))
                            dias = Integer.parseInt(rdo);
                        else
                        dias = 0;
                        justifModif.setDiasTrab(dias);
                        justifModif.setDiasSegSoc(dias);
                        //guardar dias justificacion
                        MeLanbide46Manager.getInstance().guardarCmeJustificacionVO(codOrganizacion,justifModif, adaptador) ;
                    }
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
            ex.printStackTrace();
        }
        request.getSession().setAttribute("codOperacionGuardarOfertaCme", codigoOperacion);
        return "/jsp/extension/melanbide46/recargarListaOfertas.jsp?numExp="+numExpediente;
    }
    
    public void descargarDocumentoOferta(int codOrganizacion,  int codTramite, int ocurrenciaTramite,String numExpediente,HttpServletRequest request,HttpServletResponse response)
    {
        try
        {
            String nombreDocumento = (String)request.getParameter("documento");
            String idOferta = (String)request.getParameter("idOferta");
            String codPuesto = (String)request.getParameter("idPuesto");
            if(idOferta != null && !idOferta.equals(""))
            {
                int id = Integer.parseInt(idOferta);
                Integer ejercicio = MeLanbide46Utils.getEjercicioDeExpediente(numExpediente);
                CmeOfertaVO oferta = new CmeOfertaVO();
                oferta.setNumExp(numExpediente);
                oferta.setExpEje(ejercicio);
                oferta.setIdOferta(id);
                oferta.setCodPuesto(codPuesto);
                AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
                oferta = MeLanbide46Manager.getInstance().getOfertaPorCodigoPuestoYExpediente(oferta, adapt);
                if(oferta != null)
                {
                    if(nombreDocumento != null && !nombreDocumento.equals(""))
                    {
                        byte[] data = null;
                        if(nombreDocumento.equalsIgnoreCase("gestionOferta"))
                        {
                            data = oferta.getDocGestOfe();
                            nombreDocumento = oferta.getNomDocGestOfe();
                        }
                        else if(nombreDocumento.equalsIgnoreCase("contratacionOferta"))
                        {
                            data = oferta.getDocContratacion();
                            nombreDocumento = oferta.getNomDocContratacion();
                        }
                        descargarDocumento(nombreDocumento, data, response);
                    }
                }
            }
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }
    
    public void eliminarOferta(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response)
    {
        String codigoOperacion = "0";
        List<FilaOfertaVO> ofertas = new ArrayList<FilaOfertaVO>();
        Map<String, BigDecimal> calculos = new HashMap<String, BigDecimal>();
        datosCmeVO datos = new datosCmeVO();
        
        try
        {
            AdaptadorSQLBD adaptador = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
            Integer ejercicio = MeLanbide46Utils.getEjercicioDeExpediente(numExpediente);
            if(ejercicio != null)
            {
                
                String codOferta = (String)request.getParameter("idOferta");
                String codPuesto = (String)request.getParameter("idPuesto");

                CmeOfertaVO oferta = null;
                if(codOferta != null && !codOferta.equals(""))
                {
                    oferta = new CmeOfertaVO();
                    oferta.setIdOferta(Integer.parseInt(codOferta));
                    oferta.setNumExp(numExpediente);
                    oferta.setExpEje(ejercicio);
                    oferta.setCodPuesto(codPuesto);
                    oferta = MeLanbide46Manager.getInstance().getOfertaPorCodigoPuestoYExpediente(oferta, adaptador);
                }
                if(oferta == null)
                {
                    codigoOperacion = "3";
                }
                else
                {
                    MeLanbide46Manager.getInstance().eliminarCmeOfertaVO(codOrganizacion, oferta, adaptador);
                }
                ofertas = MeLanbide46Manager.getInstance().getListaOfertasNoDenegadasPorExpediente(numExpediente, adaptador);
                calculos = MeLanbide46Manager.getInstance().cargarCalculosCme(codOrganizacion, ejercicio, numExpediente, adaptador);
                datos = MeLanbide46Manager.getInstance().obtenerDatosCme(codOrganizacion,numExpediente,adaptador);
            }
            else
            {
                codigoOperacion = "3";
            }
        }
        catch(Exception ex)
        {
            codigoOperacion = "2";
            ex.printStackTrace();
        }
        escribirListaOfertasRequest(codigoOperacion, ofertas, calculos, datos, response);
    }
    
    public void altaSustituto(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response)
    {
        String codigoOperacion = "0";
        List<FilaOfertaVO> ofertas = new ArrayList<FilaOfertaVO>();
        Map<String, BigDecimal> calculos = new HashMap<String, BigDecimal>();
        datosCmeVO datos = new datosCmeVO();
        try
        {
            Integer ejercicio = null;
            Integer idOfertaOrigen = null;
            String copiar = null;
            String idPuesto = null;
            try
            {
                ejercicio = MeLanbide46Utils.getEjercicioDeExpediente(numExpediente);
                String id = (String)request.getParameter("idOferta");
                idPuesto = (String)request.getParameter("idPuesto");
                idOfertaOrigen = Integer.parseInt(id);
                copiar = (String)request.getParameter("copiar");
            }
            catch(Exception ex)
            {
                codigoOperacion = "3";
            }
            if(codigoOperacion.equals("0"))
            {
                AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
                CmeOfertaVO ofertaOrigen = new CmeOfertaVO();
                ofertaOrigen.setIdOferta(idOfertaOrigen);
                ofertaOrigen.setExpEje(ejercicio);
                ofertaOrigen.setNumExp(numExpediente);
                ofertaOrigen.setCodPuesto(idPuesto);
                ofertaOrigen = MeLanbide46Manager.getInstance().getOfertaPorCodigoPuestoYExpediente(ofertaOrigen, adapt);
                if(ofertaOrigen != null)
                {
                    CmeOfertaVO ofertaNueva = new CmeOfertaVO();
                    ofertaNueva.setExpEje(ejercicio);
                    ofertaNueva.setNumExp(numExpediente);  
                    ofertaNueva.setCodPuesto(ofertaOrigen.getCodPuesto());
                    if(copiar != null && copiar.equalsIgnoreCase("1"))
                    {
                        ofertaNueva.setIdOfertaOrigen(idOfertaOrigen);
                    }
                    MeLanbide46Manager.getInstance().guardarCmeOfertaVO(codOrganizacion, ofertaNueva, "1", copiar, adapt);
                    ofertas = MeLanbide46Manager.getInstance().getListaOfertasNoDenegadasPorExpediente(numExpediente, adapt);
                    calculos = MeLanbide46Manager.getInstance().cargarCalculosCme(codOrganizacion, ejercicio, numExpediente, adapt);
                    datos = MeLanbide46Manager.getInstance().obtenerDatosCme(codOrganizacion,numExpediente,adapt);
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
            ex.printStackTrace();
        }
        escribirListaOfertasRequest(codigoOperacion, ofertas, calculos, datos, response);
    }
    public void guardarDatosContratacion(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response)
    {
        String codigoOperacion = "0";
        List<FilaPersonaContratadaVO> personasContratadas = new ArrayList<FilaPersonaContratadaVO>();
        Map<String, BigDecimal> calculos = new HashMap<String, BigDecimal>();
        datosCmeVO datos = new datosCmeVO();
        
        try
        {
            AdaptadorSQLBD adaptador = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
            Integer ejercicio = MeLanbide46Utils.getEjercicioDeExpediente(numExpediente);
            if(ejercicio != null)
            {
                SimpleDateFormat format = new SimpleDateFormat(ConstantesMeLanbide46.FORMATO_FECHA);
                String idJustif = (String)request.getParameter("idJustif");
                String codPuesto = (String)request.getParameter("idPuesto");
                String radioVariosTrab = (String)request.getParameter("radioVariosTrab");
                String impJustificado = (String)request.getParameter("impJustificado");
                String estado = (String)request.getParameter("estado");
                String salarioSub = (String)request.getParameter("salarioSub");
                String dietasJusti = (String)request.getParameter("dietasJusti");
                String gastosGestion = (String)request.getParameter("gastosGestion");
                String bonif = (String)request.getParameter("bonif");
                String mesesExt = (String)request.getParameter("mesesExt");

                String minoracion = (String)request.getParameter("minoracion");
                String salario = (String)request.getParameter("salario");
                String baseCC = (String)request.getParameter("baseCC");
                String baseAT = (String)request.getParameter("baseAT");
                String coeficienteApli = (String)request.getParameter("coeficienteApli");
                String porcFogasa = (String)request.getParameter("porcFogasa");
                String porcCoeficiente = (String)request.getParameter("porcCoeficiente");
                //String porcAportacion = (String)request.getParameter("porcAportacion");
                String diasTrab = (String)request.getParameter("diasTrab");
                String diasSeg = (String)request.getParameter("diasSegSoc");
                String aportEpsv = (String)request.getParameter("aportacionEPSV");
                

                CmeJustificacionVO justif = null;
                if(idJustif != null && !idJustif.equals("") && codPuesto != null && !codPuesto.equals(""))
                {
                    justif = new CmeJustificacionVO();
                    justif.setIdJustificacion(Integer.parseInt(idJustif));
                    justif.setCodPuesto(codPuesto);
                    justif.setNumExpediente(numExpediente);
                    justif.setEjercicio(ejercicio);
                    justif = MeLanbide46Manager.getInstance().getJustificacionPorCodigoPuestoYExpediente(justif, adaptador);
                }
                else
                {
                    justif = new CmeJustificacionVO();
                    justif.setNumExpediente(numExpediente);
                    justif.setEjercicio(ejercicio);
                }
                if(justif == null)
                {
                    codigoOperacion = "3";
                }
                else
                {
                    
                    justif.setFlVariosTrab(radioVariosTrab != null && !radioVariosTrab.equals("") ? radioVariosTrab.toUpperCase() : null);
                    justif.setCodEstado(estado);
                    
                    //TODO: se podria validar los datos del puesto
                    BigDecimal bd = null;// = new BigDecimal("0");
                    BigDecimal bd1 = null;
                    BigDecimal bd2 = null;
                    BigDecimal bd3 = null;
                    BigDecimal bd4 = null;
                    
                    BigDecimal bd5 = null;
                    BigDecimal bd6 = null;
                    BigDecimal bd7 = null;
                    BigDecimal bd8 = null;
                    //BigDecimal bd9 = null;
                    BigDecimal bd10 = null;
                    BigDecimal bd11 = null;
                    BigDecimal bd12 = null;
                    BigDecimal bd13 = null;
                    BigDecimal bd14 = null;
                    
                    try
                    {
                       if(impJustificado != null && !impJustificado.equals(""))
                        {
                            bd = new BigDecimal(impJustificado.replaceAll(",", "\\."));
                        }
                        //bd1 = new BigDecimal(salarioSub.replaceAll(",", "\\."));
                        bd1=new BigDecimal(0);
                        if(dietasJusti != null && !dietasJusti.equals(""))
                        {
                            bd2 = new BigDecimal(dietasJusti.replaceAll(",", "\\."));
                        }
                        if(gastosGestion != null && !gastosGestion.equals(""))
                        {
                            bd3 = new BigDecimal(gastosGestion.replaceAll(",", "\\."));
                        }
                        if(bonif != null && !bonif.equals(""))
                        {
                            bd4 = new BigDecimal(bonif.replaceAll(",", "\\."));
                        }
                        if(minoracion != null && !minoracion.equals(""))
                        {
                            bd10 = new BigDecimal(minoracion.replaceAll(",", "\\."));
                        }
                        if(baseCC != null && !baseCC.equals(""))
                        {
                            bd5 = new BigDecimal(baseCC.replaceAll(",", "\\."));
                        }
                        if(baseAT != null && !baseAT.equals(""))
                        {
                            bd11 = new BigDecimal(baseAT.replaceAll(",", "\\."));
                        }
                        if(coeficienteApli != null && !coeficienteApli.equals(""))
                        {
                            bd6 = new BigDecimal(coeficienteApli.replaceAll(",", "\\."));
                        }
                        if(porcFogasa != null && !porcFogasa.equals(""))
                        {
                            bd7 = new BigDecimal(porcFogasa.replaceAll(",", "\\."));
                        }
                        if(porcCoeficiente != null && !porcCoeficiente.equals(""))
                        {
                            bd8 = new BigDecimal(porcCoeficiente.replaceAll(",", "\\."));
                        }
                        /*if(porcAportacion != null && !porcAportacion.equals(""))
                        {
                            bd9 = new BigDecimal(porcAportacion.replaceAll(",", "\\."));
                        }*/
                        if(mesesExt != null && !mesesExt.equals(""))
                        {
                            bd12 = new BigDecimal(mesesExt.replaceAll(",", "\\."));
                        }
                        if(salario != null && !salario.equals(""))
                        {
                            bd13 = new BigDecimal(salario.replaceAll(",", "\\."));
                        }
                        if(aportEpsv != null && !aportEpsv.equals(""))
                        {
                            bd14 = new BigDecimal(aportEpsv.replaceAll(",", "\\."));
                        }
                    }
                    catch(Exception ex)
                    {
                        
                    }
                    justif.setImpJustificado(bd);
                    justif.setSalarioSub(bd1);
                    justif.setDietasJusti(bd2);
                    justif.setMesesExt(bd12);
                    justif.setGastosGestion(bd3);
                    justif.setBonif(bd4);
                    
                    justif.setMinoracion(bd10);
                    justif.setBaseCC(bd5);
                    justif.setCoeficienteApli(bd6);
                    justif.setPorcFogasa(bd7);
                    justif.setPorcCoeficiente(bd8);
                    //justif.setPorcAportacion(bd9);
                    justif.setDiasTrab(diasTrab != "" ? Integer.parseInt(diasTrab) : 0);
                    justif.setDiasSegSoc(diasSeg != "" ? Integer.parseInt(diasSeg) : 0);
                    justif.setBaseAT(bd11);
                    justif.setSalario(bd13);
                    justif.setAportEpsv(bd14);
                                        
                    MeLanbide46Manager.getInstance().guardarCmeJustificacionVO(codOrganizacion, justif, adaptador);
                }
                calculos = MeLanbide46Manager.getInstance().cargarCalculosCme(codOrganizacion, ejercicio, numExpediente, adaptador);
                datos = MeLanbide46Manager.getInstance().obtenerDatosCme(codOrganizacion,numExpediente,this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                
                CmePuestoVO puesto = new CmePuestoVO();
                puesto.setEjercicio(ejercicio);
                puesto.setNumExp(numExpediente);
                puesto.setCodPuesto(codPuesto);
                puesto = MeLanbide46Manager.getInstance().getPuestoPorCodigoYExpediente(puesto, adaptador);
                if(puesto != null)
                {
                    personasContratadas = MeLanbide46Manager.getInstance().getListaContratacionesPuesto(puesto, adaptador);
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
            ex.printStackTrace();
        }
        //escribirListaJustificacionesRequest(codigoOperacion, justificaciones, calculos, response);
        escribirListaContratacionesRequest(codigoOperacion, personasContratadas, calculos, datos, response);
    }
    
    private void descargarDocumento(String nombreDocumento, byte[] data, HttpServletResponse response)
    {
        try
        {
            response.setHeader("Cache-Control","must-revalidate, post-check=0, pre-check=0");
            response.setHeader("Pragma","public");
            response.setHeader ("Content-Type", "application/octet-stream");
            response.setHeader ("Content-Disposition", "attachment; filename="+nombreDocumento);

            response.setContentLength(data.length);
            response.getOutputStream().write(data, 0, data.length);
            response.getOutputStream().flush();
            response.getOutputStream().close();
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }
    
    private boolean validarDatosCme(HttpServletRequest request)
    {
        try
        {
            String empresa = request.getParameter("empresa");
            String impSubvAprobadaExp = request.getParameter("impSubvAprobadaExp");
            String impSubvAprobadaConv = request.getParameter("impSubvAprobadaConv");
            String otrasAyudas = request.getParameter("otrasAyudas");
            String impSubvFinal = request.getParameter("impSubvFinal");
            
            if(!MeLanbide46Validator.validarTexto(empresa, 500))
            {
                return false;
            }
            if(!MeLanbide46Validator.validarNumericoDecimal(impSubvAprobadaExp, 10, 2))
            {
                return false;
            }
            if(!MeLanbide46Validator.validarNumericoDecimal(impSubvAprobadaConv, 10, 2))
            {
                return false;
            }
            if(!MeLanbide46Validator.validarNumericoDecimal(otrasAyudas, 10, 2))
            {
                return false;
            }
            if(!MeLanbide46Validator.validarNumericoDecimal(impSubvFinal, 10, 2))
            {
                return false;
            }
            return true;
        }
        catch(Exception ex)
        {
            return false;
        }
    }
    
    private void cargarCombosNuevoPuesto(int codOrganizacion, HttpServletRequest request)
    {
        IModuloIntegracionExternoCamposFlexia el = ModuloIntegracionExternoCampoSupFactoria.getInstance().getImplClass();
        
        List<ValorCampoDesplegableModuloIntegracionVO> list = new ArrayList<ValorCampoDesplegableModuloIntegracionVO>();
        
        List<SelectItem> listaPaises = new ArrayList<SelectItem>();
        List<SelectItem> listaIdiomas = new ArrayList<SelectItem>();
        List<SelectItem> listaNivelIdiomas = new ArrayList<SelectItem>();
        List<SelectItem> listaNivelFormativo = new ArrayList<SelectItem>();
        List<SelectItem> listaGrupoCotizacion = new ArrayList<SelectItem>();
        List<SelectItem> listaResultado = new ArrayList<SelectItem>();
        List<SelectItem> listaMotivos = new ArrayList<SelectItem>();
        
        //Combo PAISES
        try
        {
            listaPaises = MeLanbide46Manager.getInstance().getListaPaises(this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
        }
        catch(Exception ex)
        {
            
        }
        request.setAttribute("listaPaises", listaPaises);
        list = new ArrayList<ValorCampoDesplegableModuloIntegracionVO>();
        
        //Combo IDIOMA
        try
        {
            String campoDesplegable = ConfigurationParameter.getParameter(ConstantesMeLanbide46.CAMPO_SUPL_IDIOMA, ConstantesMeLanbide46.FICHERO_PROPIEDADES);
        
            SalidaIntegracionVO salidaIntegracion = el.getCampoDesplegable(String.valueOf(codOrganizacion), campoDesplegable);
            list = salidaIntegracion.getCampoDesplegable().getValores();
            listaIdiomas = MeLanbide46MappingUtils.getInstance().fromCampoDesplegableToSelectItemVO(list);
        }
        catch(Exception ex)
        {
            
        }
        request.setAttribute("listaIdiomas", listaIdiomas);
        list = new ArrayList<ValorCampoDesplegableModuloIntegracionVO>();
        
        //Combo NIVEL IDIOMA
        try
        {
            String campoDesplegable = ConfigurationParameter.getParameter(ConstantesMeLanbide46.CAMPO_SUPL_NIVEL_IDIOMA, ConstantesMeLanbide46.FICHERO_PROPIEDADES);
        
            SalidaIntegracionVO salidaIntegracion = el.getCampoDesplegable(String.valueOf(codOrganizacion), campoDesplegable);
            list = salidaIntegracion.getCampoDesplegable().getValores();
            listaNivelIdiomas = MeLanbide46MappingUtils.getInstance().fromCampoDesplegableToSelectItemVO(list);
        }
        catch(Exception ex)
        {
            
        }
        request.setAttribute("listaNivelIdiomas", listaNivelIdiomas);
        list = new ArrayList<ValorCampoDesplegableModuloIntegracionVO>();
        
        //Combo NIVEL FORMATIVO
        try
        {
            String campoDesplegable = ConfigurationParameter.getParameter(ConstantesMeLanbide46.CAMPO_SUPL_NIVEL_FORMATIVO, ConstantesMeLanbide46.FICHERO_PROPIEDADES);
        
            SalidaIntegracionVO salidaIntegracion = el.getCampoDesplegable(String.valueOf(codOrganizacion), campoDesplegable);
            list = salidaIntegracion.getCampoDesplegable().getValores();
            listaNivelFormativo = MeLanbide46MappingUtils.getInstance().fromCampoDesplegableToSelectItemVO(list);
        }
        catch(Exception ex)
        {
            
        }
        request.setAttribute("listaNivelFormativo", listaNivelFormativo);
        list = new ArrayList<ValorCampoDesplegableModuloIntegracionVO>();
        
        //Combo GRUPO COTIZACION
        try
        {
            String campoDesplegable = ConfigurationParameter.getParameter(ConstantesMeLanbide46.CAMPO_SUPL_GRUPO_COTIZACION, ConstantesMeLanbide46.FICHERO_PROPIEDADES);
        
            SalidaIntegracionVO salidaIntegracion = el.getCampoDesplegable(String.valueOf(codOrganizacion), campoDesplegable);
            list = salidaIntegracion.getCampoDesplegable().getValores();
            listaGrupoCotizacion = MeLanbide46MappingUtils.getInstance().fromCampoDesplegableToSelectItemVO(list, SelectItem.ORDENAR_POR_CODIGO);
        }
        catch(Exception ex)
        {
            
        }
        request.setAttribute("listaGrupoCotizacion", listaGrupoCotizacion);
        list = new ArrayList<ValorCampoDesplegableModuloIntegracionVO>();
        
        //Combo RESULTADO
        try
        {
            String campoDesplegable = ConfigurationParameter.getParameter(ConstantesMeLanbide46.CAMPO_SUPL_RESULTADO, ConstantesMeLanbide46.FICHERO_PROPIEDADES);
        
            SalidaIntegracionVO salidaIntegracion = el.getCampoDesplegable(String.valueOf(codOrganizacion), campoDesplegable);
            list = salidaIntegracion.getCampoDesplegable().getValores();
            listaResultado = MeLanbide46MappingUtils.getInstance().fromCampoDesplegableToSelectItemVO(list);
        }
        catch(Exception ex)
        {
            
        }
        request.setAttribute("listaResultado", listaResultado);
        list = new ArrayList<ValorCampoDesplegableModuloIntegracionVO>();
        
        //Combo MOTIVO
        try
        {
            String campoDesplegable = ConfigurationParameter.getParameter(ConstantesMeLanbide46.CAMPO_SUPL_MOTIVO, ConstantesMeLanbide46.FICHERO_PROPIEDADES);
        
            SalidaIntegracionVO salidaIntegracion = el.getCampoDesplegable(String.valueOf(codOrganizacion), campoDesplegable);
            list = salidaIntegracion.getCampoDesplegable().getValores();
            listaMotivos = MeLanbide46MappingUtils.getInstance().fromCampoDesplegableToSelectItemVO(list);
        }
        catch(Exception ex)
        {
            
        }
        request.setAttribute("listaMotivos", listaMotivos);
        list = new ArrayList<ValorCampoDesplegableModuloIntegracionVO>();
    }
    
    private void cargarCombosNuevaOferta(int codOrganizacion, HttpServletRequest request)
    {
        IModuloIntegracionExternoCamposFlexia el = ModuloIntegracionExternoCampoSupFactoria.getInstance().getImplClass();
        
        List<ValorCampoDesplegableModuloIntegracionVO> list = new ArrayList<ValorCampoDesplegableModuloIntegracionVO>();
        
        List<SelectItem> listaPaises = new ArrayList<SelectItem>();
        List<SelectItem> listaIdiomas = new ArrayList<SelectItem>();
        List<SelectItem> listaNivelIdiomas = new ArrayList<SelectItem>();
        List<SelectItem> listaNivelFormativo = new ArrayList<SelectItem>();
        List<SelectItem> listaOficinasLanbide = new ArrayList<SelectItem>();
        List<SelectItem> listaGrupoCot = new ArrayList<SelectItem>();
        List<SelectItem> listaCausaBaja = new ArrayList<SelectItem>();
        List<SelectItem> listaTipoDoc = new ArrayList<SelectItem>();
        List<SelectItem> listaCausaRenuncia = new ArrayList<SelectItem>();
        List<SelectItem> listaCausaRenunciaPresOferta = new ArrayList<SelectItem>();
        
        //Combo PAISES
        try
        {
            listaPaises = MeLanbide46Manager.getInstance().getListaPaises(this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
        }
        catch(Exception ex)
        {
            
        }
        request.setAttribute("listaPaises", listaPaises);
        list = new ArrayList<ValorCampoDesplegableModuloIntegracionVO>();
        
        //Combo IDIOMA
        try
        {
            String campoDesplegable = ConfigurationParameter.getParameter(ConstantesMeLanbide46.CAMPO_SUPL_IDIOMA, ConstantesMeLanbide46.FICHERO_PROPIEDADES);
        
            SalidaIntegracionVO salidaIntegracion = el.getCampoDesplegable(String.valueOf(codOrganizacion), campoDesplegable);
            list = salidaIntegracion.getCampoDesplegable().getValores();
            listaIdiomas = MeLanbide46MappingUtils.getInstance().fromCampoDesplegableToSelectItemVO(list);
        }
        catch(Exception ex)
        {
            
        }
        request.setAttribute("listaIdiomas", listaIdiomas);
        list = new ArrayList<ValorCampoDesplegableModuloIntegracionVO>();
        
        //Combo NIVEL IDIOMA
        try
        {
            String campoDesplegable = ConfigurationParameter.getParameter(ConstantesMeLanbide46.CAMPO_SUPL_NIVEL_IDIOMA, ConstantesMeLanbide46.FICHERO_PROPIEDADES);
        
            SalidaIntegracionVO salidaIntegracion = el.getCampoDesplegable(String.valueOf(codOrganizacion), campoDesplegable);
            list = salidaIntegracion.getCampoDesplegable().getValores();
            listaNivelIdiomas = MeLanbide46MappingUtils.getInstance().fromCampoDesplegableToSelectItemVO(list);
        }
        catch(Exception ex)
        {
            
        }
        request.setAttribute("listaNivelIdiomas", listaNivelIdiomas);
        list = new ArrayList<ValorCampoDesplegableModuloIntegracionVO>();
        
        //Combo NIVEL FORMATIVO
        try
        {
            String campoDesplegable = ConfigurationParameter.getParameter(ConstantesMeLanbide46.CAMPO_SUPL_NIVEL_FORMATIVO, ConstantesMeLanbide46.FICHERO_PROPIEDADES);
        
            SalidaIntegracionVO salidaIntegracion = el.getCampoDesplegable(String.valueOf(codOrganizacion), campoDesplegable);
            list = salidaIntegracion.getCampoDesplegable().getValores();
            listaNivelFormativo = MeLanbide46MappingUtils.getInstance().fromCampoDesplegableToSelectItemVO(list);
        }
        catch(Exception ex)
        {
            
        }
        request.setAttribute("listaNivelFormativo", listaNivelFormativo);
        list = new ArrayList<ValorCampoDesplegableModuloIntegracionVO>();
        
        //Combo OFICINAS LANBIDE
        try
        {
            String campoDesplegable = ConfigurationParameter.getParameter(ConstantesMeLanbide46.CAMPO_SUPL_OFICINAS_LANBIDE, ConstantesMeLanbide46.FICHERO_PROPIEDADES);
        
            SalidaIntegracionVO salidaIntegracion = el.getCampoDesplegable(String.valueOf(codOrganizacion), campoDesplegable);
            list = salidaIntegracion.getCampoDesplegable().getValores();
            listaOficinasLanbide = MeLanbide46MappingUtils.getInstance().fromCampoDesplegableToSelectItemVO(list);
        }
        catch(Exception ex)
        {
            
        }
        request.setAttribute("listaOficinasGestion", listaOficinasLanbide);
        list = new ArrayList<ValorCampoDesplegableModuloIntegracionVO>();
        
        //Combo GRUPO COTIZACION
        try
        {
            String campoDesplegable = ConfigurationParameter.getParameter(ConstantesMeLanbide46.CAMPO_SUPL_GRUPO_COTIZACION, ConstantesMeLanbide46.FICHERO_PROPIEDADES);
        
            SalidaIntegracionVO salidaIntegracion = el.getCampoDesplegable(String.valueOf(codOrganizacion), campoDesplegable);
            list = salidaIntegracion.getCampoDesplegable().getValores();
            listaGrupoCot = MeLanbide46MappingUtils.getInstance().fromCampoDesplegableToSelectItemVO(list, SelectItem.ORDENAR_POR_CODIGO);
        }
        catch(Exception ex)
        {
            
        }
        request.setAttribute("listaGrupoCotizacion", listaGrupoCot);
        list = new ArrayList<ValorCampoDesplegableModuloIntegracionVO>();
        
        //Combo CAUSA BAJA
        try
        {
            String campoDesplegable = ConfigurationParameter.getParameter(ConstantesMeLanbide46.CAMPO_SUPL_CAUSA_BAJA, ConstantesMeLanbide46.FICHERO_PROPIEDADES);
        
            SalidaIntegracionVO salidaIntegracion = el.getCampoDesplegable(String.valueOf(codOrganizacion), campoDesplegable);
            list = salidaIntegracion.getCampoDesplegable().getValores();
            listaCausaBaja = MeLanbide46MappingUtils.getInstance().fromCampoDesplegableToSelectItemVO(list);
        }
        catch(Exception ex)
        {
            
        }
        request.setAttribute("listaCausaBaja", listaCausaBaja);
        list = new ArrayList<ValorCampoDesplegableModuloIntegracionVO>();
        
        //Combo TIPO DOCUMENTO
        try
        {
            listaTipoDoc = MeLanbide46Manager.getInstance().getTiposDocumento(this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
        }
        catch(Exception ex)
        {
            
        }
        request.setAttribute("listaNif", listaTipoDoc);
        
        //Combo CAUSA RENUNCIA
        try
        {
            String campoDesplegable = ConfigurationParameter.getParameter(ConstantesMeLanbide46.CAMPO_SUPL_CAUSA_RENUNCIA, ConstantesMeLanbide46.FICHERO_PROPIEDADES);
        
            SalidaIntegracionVO salidaIntegracion = el.getCampoDesplegable(String.valueOf(codOrganizacion), campoDesplegable);
            list = salidaIntegracion.getCampoDesplegable().getValores();
            listaCausaRenuncia = MeLanbide46MappingUtils.getInstance().fromCampoDesplegableToSelectItemVO(list);
        }
        catch(Exception ex)
        {
            
        }
        request.setAttribute("listaCausaRenuncia", listaCausaRenuncia);
        
        //Combo CAUSA RENUNCIA PRESENTA OFERTA
        try
        {
            String campoDesplegablePresOferta = ConfigurationParameter.getParameter(ConstantesMeLanbide46.CAMPO_SUPL_CAUSA_RENUNCIA, ConstantesMeLanbide46.FICHERO_PROPIEDADES);
        
            SalidaIntegracionVO salidaIntegracion = el.getCampoDesplegable(String.valueOf(codOrganizacion), campoDesplegablePresOferta);
            list = salidaIntegracion.getCampoDesplegable().getValores();
            listaCausaRenunciaPresOferta = MeLanbide46MappingUtils.getInstance().fromCampoDesplegableToSelectItemVO(list);
        }
        catch(Exception ex)
        {
            
        }
        request.setAttribute("listaCausaRenunciaPresOferta", listaCausaRenunciaPresOferta);
        
        list = new ArrayList<ValorCampoDesplegableModuloIntegracionVO>();
    }
    
    private void cargarCombosPersonaContratada(int codOrganizacion, HttpServletRequest request)
    {
        IModuloIntegracionExternoCamposFlexia el = ModuloIntegracionExternoCampoSupFactoria.getInstance().getImplClass();
        
        List<ValorCampoDesplegableModuloIntegracionVO> list = new ArrayList<ValorCampoDesplegableModuloIntegracionVO>();
        
        List<SelectItem> listaGrupoCot = new ArrayList<SelectItem>();
        List<SelectItem> listaCausaBaja = new ArrayList<SelectItem>();
        List<SelectItem> listaTipoDoc = new ArrayList<SelectItem>();
        
        //Combo GRUPO COTIZACION
        try
        {
            String campoDesplegable = ConfigurationParameter.getParameter(ConstantesMeLanbide46.CAMPO_SUPL_GRUPO_COTIZACION, ConstantesMeLanbide46.FICHERO_PROPIEDADES);
        
            SalidaIntegracionVO salidaIntegracion = el.getCampoDesplegable(String.valueOf(codOrganizacion), campoDesplegable);
            list = salidaIntegracion.getCampoDesplegable().getValores();
            listaGrupoCot = MeLanbide46MappingUtils.getInstance().fromCampoDesplegableToSelectItemVO(list, SelectItem.ORDENAR_POR_CODIGO);
        }
        catch(Exception ex)
        {
            
        }
        request.setAttribute("listaGrupoCotizacion", listaGrupoCot);
        list = new ArrayList<ValorCampoDesplegableModuloIntegracionVO>();
        
        //Combo CAUSA BAJA
        try
        {
            String campoDesplegable = ConfigurationParameter.getParameter(ConstantesMeLanbide46.CAMPO_SUPL_CAUSA_BAJA, ConstantesMeLanbide46.FICHERO_PROPIEDADES);
        
            SalidaIntegracionVO salidaIntegracion = el.getCampoDesplegable(String.valueOf(codOrganizacion), campoDesplegable);
            list = salidaIntegracion.getCampoDesplegable().getValores();
            listaCausaBaja = MeLanbide46MappingUtils.getInstance().fromCampoDesplegableToSelectItemVO(list);
        }
        catch(Exception ex)
        {
            
        }
        request.setAttribute("listaCausaBaja", listaCausaBaja);
        list = new ArrayList<ValorCampoDesplegableModuloIntegracionVO>();
        
        //Combo TIPO DOCUMENTO
        try
        {
            listaTipoDoc = MeLanbide46Manager.getInstance().getTiposDocumento(this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
        }
        catch(Exception ex)
        {
            
        }
        request.setAttribute("listaNif", listaTipoDoc);
        
         List<SelectItem> listaCausaRenunciaPresOferta = new ArrayList<SelectItem>();
    }
    
    private void cargarCombosNuevaJustificacion(int codOrganizacion, HttpServletRequest request)
    {
        IModuloIntegracionExternoCamposFlexia el = ModuloIntegracionExternoCampoSupFactoria.getInstance().getImplClass();
        
        List<ValorCampoDesplegableModuloIntegracionVO> list = new ArrayList<ValorCampoDesplegableModuloIntegracionVO>();
        
        List<SelectItem> listaPaises = new ArrayList<SelectItem>();
        List<SelectItem> listaIdiomas = new ArrayList<SelectItem>();
        List<SelectItem> listaNivelIdiomas = new ArrayList<SelectItem>();
        List<SelectItem> listaNivelFormativo = new ArrayList<SelectItem>();
        
        //Combo PAISES
        try
        {
            listaPaises = MeLanbide46Manager.getInstance().getListaPaises(this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
        }
        catch(Exception ex)
        {
            
        }
        request.setAttribute("listaPaises", listaPaises);
        list = new ArrayList<ValorCampoDesplegableModuloIntegracionVO>();
        
        //Combo IDIOMA
        try
        {
            String campoDesplegable = ConfigurationParameter.getParameter(ConstantesMeLanbide46.CAMPO_SUPL_IDIOMA, ConstantesMeLanbide46.FICHERO_PROPIEDADES);
        
            SalidaIntegracionVO salidaIntegracion = el.getCampoDesplegable(String.valueOf(codOrganizacion), campoDesplegable);
            list = salidaIntegracion.getCampoDesplegable().getValores();
            listaIdiomas = MeLanbide46MappingUtils.getInstance().fromCampoDesplegableToSelectItemVO(list);
        }
        catch(Exception ex)
        {
            
        }
        request.setAttribute("listaIdiomas", listaIdiomas);
        list = new ArrayList<ValorCampoDesplegableModuloIntegracionVO>();
        
        //Combo NIVEL IDIOMA
        try
        {
            String campoDesplegable = ConfigurationParameter.getParameter(ConstantesMeLanbide46.CAMPO_SUPL_NIVEL_IDIOMA, ConstantesMeLanbide46.FICHERO_PROPIEDADES);
        
            SalidaIntegracionVO salidaIntegracion = el.getCampoDesplegable(String.valueOf(codOrganizacion), campoDesplegable);
            list = salidaIntegracion.getCampoDesplegable().getValores();
            listaNivelIdiomas = MeLanbide46MappingUtils.getInstance().fromCampoDesplegableToSelectItemVO(list);
        }
        catch(Exception ex)
        {
            
        }
        request.setAttribute("listaNivelIdiomas", listaNivelIdiomas);
        list = new ArrayList<ValorCampoDesplegableModuloIntegracionVO>();
        
        //Combo NIVEL FORMATIVO
        try
        {
            String campoDesplegable = ConfigurationParameter.getParameter(ConstantesMeLanbide46.CAMPO_SUPL_NIVEL_FORMATIVO, ConstantesMeLanbide46.FICHERO_PROPIEDADES);
        
            SalidaIntegracionVO salidaIntegracion = el.getCampoDesplegable(String.valueOf(codOrganizacion), campoDesplegable);
            list = salidaIntegracion.getCampoDesplegable().getValores();
            listaNivelFormativo = MeLanbide46MappingUtils.getInstance().fromCampoDesplegableToSelectItemVO(list);
        }
        catch(Exception ex)
        {
            
        }
        request.setAttribute("listaNivelFormativo", listaNivelFormativo);
        list = new ArrayList<ValorCampoDesplegableModuloIntegracionVO>();
    }
    
    private void cargarImportesJustificacion(String numExpediente, int codOrganizacion, CmePuestoVO puesto, HttpServletRequest request)
    {
        
            Integer ejercicio = MeLanbide46Utils.getEjercicioDeExpediente(numExpediente);
            
                          
            String codigoCampo = null;
            //importe concedido
            BigDecimal concedido = null;
            //importe renunciado
            BigDecimal renunciado = null;
            try
            {
                AdaptadorSQLBD adapt =this.getAdaptSQLBD(String.valueOf(codOrganizacion));
                codigoCampo = ConfigurationParameter.getParameter(ConstantesMeLanbide46.CAMPO_SUPL_IMP_CON, ConstantesMeLanbide46.FICHERO_PROPIEDADES);
                /*concedido = MeLanbide46Manager.getInstance().getValorCampoNumerico(codOrganizacion, String.valueOf(ejercicio), numExpediente, codigoCampo, adapt);
                if(concedido != null)
                {
                    request.setAttribute("concedido", concedido.toPlainString());
                }
                else
                {
                    request.setAttribute("concedido", new BigDecimal("0").toPlainString());
                }*/
                if(puesto != null)
                {
                    if(puesto.getCodResult() != null && puesto.getCodResult().equalsIgnoreCase(ConstantesMeLanbide46.CODIGO_RESULTADO_CONCEDIDO))
                    {
                        concedido = puesto.getImpMaxSuv() != null ? puesto.getImpMaxSuv().add(new BigDecimal(ConstantesMeLanbide46.CERO_CON_DECIMALES)) : new BigDecimal(ConstantesMeLanbide46.CERO_CON_DECIMALES);
                    }
                }
                if(concedido == null)
                {
                    concedido = new BigDecimal(ConstantesMeLanbide46.CERO_CON_DECIMALES);
                }
                
                request.setAttribute("concedido", concedido.toPlainString());
            
                codigoCampo = ConfigurationParameter.getParameter(ConstantesMeLanbide46.CAMPO_SUPL_IMP_REN, ConstantesMeLanbide46.FICHERO_PROPIEDADES);
                renunciado = MeLanbide46Manager.getInstance().getValorCampoNumerico(codOrganizacion, String.valueOf(ejercicio), numExpediente, codigoCampo, adapt);
                if(renunciado != null)
                {
                    request.setAttribute("renunciado", renunciado.toPlainString());
                }
                else
                {
                    request.setAttribute("renunciado", new BigDecimal("0").toPlainString());
                }
            }
            catch(Exception ex)
            {
                ex.printStackTrace();
            }
    }
    
    private void escribirListaPuestosRequest(String codigoOperacion, List<FilaPuestoVO> puestos, Map<String, BigDecimal> calculos, datosCmeVO datos,  HttpServletResponse response)
    {
        StringBuffer xmlSalida = new StringBuffer();
        xmlSalida.append("<RESPUESTA>");
            xmlSalida.append("<CODIGO_OPERACION>");
                xmlSalida.append(codigoOperacion);
            xmlSalida.append("</CODIGO_OPERACION>");
            
        if(calculos != null && !calculos.isEmpty())
        {
            xmlSalida.append("<CALCULOS>");
            
            xmlSalida.append("<IMP_SOL>");
            if(calculos.containsKey(ConstantesMeLanbide46.CAMPO_SUPL_IMP_SOLICITADO))
            {
                BigDecimal bd = calculos.get(ConstantesMeLanbide46.CAMPO_SUPL_IMP_SOLICITADO);
                xmlSalida.append(bd.toPlainString().replaceAll("\\.", ","));
            }
            xmlSalida.append("</IMP_SOL>");
            
            xmlSalida.append("<IMP_CONV>");
            if(calculos.containsKey(ConstantesMeLanbide46.CAMPO_SUPL_IMP_CONVOCATORIA))
            {
                BigDecimal bd = calculos.get(ConstantesMeLanbide46.CAMPO_SUPL_IMP_CONVOCATORIA);
                xmlSalida.append(bd.toPlainString().replaceAll("\\.", ","));
            }
            xmlSalida.append("</IMP_CONV>");
            
            xmlSalida.append("<IMP_PREV_CON>");
            if(calculos.containsKey(ConstantesMeLanbide46.CAMPO_SUPL_IMP_PREV_CON))
            {
                BigDecimal bd = calculos.get(ConstantesMeLanbide46.CAMPO_SUPL_IMP_PREV_CON);
                xmlSalida.append(bd.toPlainString().replaceAll("\\.", ","));
            }
            xmlSalida.append("</IMP_PREV_CON>");
            
            xmlSalida.append("<IMP_CON>");
            if(calculos.containsKey(ConstantesMeLanbide46.CAMPO_SUPL_IMP_CON))
            {
                BigDecimal bd = calculos.get(ConstantesMeLanbide46.CAMPO_SUPL_IMP_CON);
                xmlSalida.append(bd.toPlainString().replaceAll("\\.", ","));
            }
            xmlSalida.append("</IMP_CON>");
            
            xmlSalida.append("<IMP_JUS>");
//            if(calculos.containsKey(ConstantesMeLanbide46.CAMPO_SUPL_IMP_JUS))
//            {
//                BigDecimal bd = calculos.get(ConstantesMeLanbide46.CAMPO_SUPL_IMP_JUS);
//                xmlSalida.append(bd.toPlainString().replaceAll("\\.", ","));
//            }
            xmlSalida.append(datos.getImpJustificado().replaceAll("\\.", ","));
            xmlSalida.append("</IMP_JUS>");
            
            xmlSalida.append("<IMP_REN>");
            if(calculos.containsKey(ConstantesMeLanbide46.CAMPO_SUPL_IMP_REN))
            {
                BigDecimal bd = calculos.get(ConstantesMeLanbide46.CAMPO_SUPL_IMP_REN);
                xmlSalida.append(bd.toPlainString().replaceAll("\\.", ","));
            }
            xmlSalida.append("</IMP_REN>");
            
            xmlSalida.append("<IMP_PAG>");
            if(calculos.containsKey(ConstantesMeLanbide46.CAMPO_SUPL_IMP_PAG))
            {
                BigDecimal bd = calculos.get(ConstantesMeLanbide46.CAMPO_SUPL_IMP_PAG);
                xmlSalida.append(bd.toPlainString().replaceAll("\\.", ","));
            }
            xmlSalida.append("</IMP_PAG>");
            
            xmlSalida.append("<IMP_PAG_2>");
            if(calculos.containsKey(ConstantesMeLanbide46.CAMPO_SUPL_IMP_PAG_2))
            {
                BigDecimal bd = calculos.get(ConstantesMeLanbide46.CAMPO_SUPL_IMP_PAG_2);
                xmlSalida.append(bd.toPlainString().replaceAll("\\.", ","));
            }
            xmlSalida.append("</IMP_PAG_2>");
            
            xmlSalida.append("<IMP_REI>");
//            if(calculos.containsKey(ConstantesMeLanbide46.CAMPO_SUPL_IMP_REI))
//            {
//                BigDecimal bd = calculos.get(ConstantesMeLanbide46.CAMPO_SUPL_IMP_REI);
//                xmlSalida.append(bd.toPlainString().replaceAll("\\.", ","));
//            }
                xmlSalida.append(datos.getReintegro().replaceAll("\\.", ","));
            xmlSalida.append("</IMP_REI>");
            
            xmlSalida.append("<OTRAS_AYUDAS_SOLIC>");
            if(calculos.containsKey(ConstantesMeLanbide46.CAMPO_SUPL_OTRAS_AYUDAS_SOLIC))
            {
                BigDecimal bd = calculos.get(ConstantesMeLanbide46.CAMPO_SUPL_OTRAS_AYUDAS_SOLIC);
                xmlSalida.append(bd.toPlainString().replaceAll("\\.", ","));
            }
            xmlSalida.append("</OTRAS_AYUDAS_SOLIC>");
            
            xmlSalida.append("<OTRAS_AYUDAS_CONCE>");
            if(calculos.containsKey(ConstantesMeLanbide46.CAMPO_SUPL_OTRAS_AYUDAS_CONCE))
            {
                BigDecimal bd = calculos.get(ConstantesMeLanbide46.CAMPO_SUPL_OTRAS_AYUDAS_CONCE);
                xmlSalida.append(bd.toPlainString().replaceAll("\\.", ","));
            }
            xmlSalida.append("</OTRAS_AYUDAS_CONCE>");
            
            xmlSalida.append("<MINIMIS_SOLIC>");
            if(calculos.containsKey(ConstantesMeLanbide46.CAMPO_SUPL_MINIMIS_SOLIC))
            {
                BigDecimal bd = calculos.get(ConstantesMeLanbide46.CAMPO_SUPL_MINIMIS_SOLIC);
                xmlSalida.append(bd.toPlainString().replaceAll("\\.", ","));
            }
            xmlSalida.append("</MINIMIS_SOLIC>");
            
            xmlSalida.append("<MINIMIS_CONCE>");
            if(calculos.containsKey(ConstantesMeLanbide46.CAMPO_SUPL_MINIMIS_CONCE))
            {
                BigDecimal bd = calculos.get(ConstantesMeLanbide46.CAMPO_SUPL_MINIMIS_CONCE);
                xmlSalida.append(bd.toPlainString().replaceAll("\\.", ","));
            }
            xmlSalida.append("</MINIMIS_CONCE>");
            
            xmlSalida.append("<IMP_DESP>");
            if(calculos.containsKey(ConstantesMeLanbide46.CAMPO_SUPL_IMP_REN))
            {
                BigDecimal bd = calculos.get(ConstantesMeLanbide46.CAMPO_SUPL_IMP_DESP);
                xmlSalida.append(bd.toPlainString().replaceAll("\\.", ","));
            }
            xmlSalida.append("</IMP_DESP>");
            
            xmlSalida.append("<IMP_BAJA>");
            if(calculos.containsKey(ConstantesMeLanbide46.CAMPO_SUPL_IMP_BAJA))
            {
                BigDecimal bd = calculos.get(ConstantesMeLanbide46.CAMPO_SUPL_IMP_BAJA);
                xmlSalida.append(bd.toPlainString().replaceAll("\\.", ","));
            }
            xmlSalida.append("</IMP_BAJA>");
            
            xmlSalida.append("<CONCEDIDO_REAL>");
            if(calculos.containsKey("CONCEDIDO_REAL"))
            {
                BigDecimal bd = calculos.get("CONCEDIDO_REAL");
                xmlSalida.append(bd.toPlainString().replaceAll("\\.", ","));
            }
            xmlSalida.append("</CONCEDIDO_REAL>");
            xmlSalida.append("<PAGADO_REAL>");
            if(calculos.containsKey("PAGADO_REAL"))
            {
                BigDecimal bd = calculos.get("PAGADO_REAL");
                xmlSalida.append(bd.toPlainString().replaceAll("\\.", ","));
            }
            xmlSalida.append("</PAGADO_REAL>");
            xmlSalida.append("<PAGADO_REAL_2>");
            if(calculos.containsKey("PAGADO_REAL_2"))
            {
                BigDecimal bd = calculos.get("PAGADO_REAL_2");
                xmlSalida.append(bd.toPlainString().replaceAll("\\.", ","));
            }
            xmlSalida.append("</PAGADO_REAL_2>");
            
            xmlSalida.append("<IMP_NO_JUS>");
//            if(calculos.containsKey(ConstantesMeLanbide46.CAMPO_SUPL_IMP_NO_JUS))
//            {
//                BigDecimal bd = calculos.get(ConstantesMeLanbide46.CAMPO_SUPL_IMP_NO_JUS);
//                xmlSalida.append(bd.toPlainString().replaceAll("\\.", ","));
//            }
                xmlSalida.append(datos.getImpNoJusi().replaceAll("\\.", ","));
            xmlSalida.append("</IMP_NO_JUS>");
            
            xmlSalida.append("<IMP_REN_RES>");
            if(calculos.containsKey(ConstantesMeLanbide46.CAMPO_SUPL_IMP_REN_RES))
            {
                BigDecimal bd = calculos.get(ConstantesMeLanbide46.CAMPO_SUPL_IMP_REN_RES);
                xmlSalida.append(bd.toPlainString().replaceAll("\\.", ","));
            }
            xmlSalida.append("</IMP_REN_RES>");
            
            xmlSalida.append("<IMP_PAG_RES>");
            if(calculos.containsKey(ConstantesMeLanbide46.CAMPO_SUPL_IMP_PAG_RES))
            {
                BigDecimal bd = calculos.get(ConstantesMeLanbide46.CAMPO_SUPL_IMP_PAG_RES);
                xmlSalida.append(bd.toPlainString().replaceAll("\\.", ","));
            }
            xmlSalida.append("</IMP_PAG_RES>");
            xmlSalida.append("</CALCULOS>");
        }
        for(FilaPuestoVO puesto : puestos)
        {
            xmlSalida.append("<FILA>");
                xmlSalida.append("<ID>");
                        xmlSalida.append(puesto.getCodPuesto());
                xmlSalida.append("</ID>");
                xmlSalida.append("<DESC_PUESTO>");
                        xmlSalida.append(puesto.getDescPuesto());
                xmlSalida.append("</DESC_PUESTO>");
                xmlSalida.append("<RANGO_EDAD>");
                        xmlSalida.append(puesto.getRangoEdad());
                xmlSalida.append("</RANGO_EDAD>");
                xmlSalida.append("<PAIS>");
                        xmlSalida.append(puesto.getPais());
                xmlSalida.append("</PAIS>");
                xmlSalida.append("<NIVEL_FORMATIVO>");
                        xmlSalida.append(puesto.getNivelFor());
                xmlSalida.append("</NIVEL_FORMATIVO>");
                xmlSalida.append("<TITULACION_1>");
                        xmlSalida.append(puesto.getTitulacion1());
                xmlSalida.append("</TITULACION_1>");
                xmlSalida.append("<TITULACION_2>");
                        xmlSalida.append(puesto.getTitulacion2());
                xmlSalida.append("</TITULACION_2>");
                xmlSalida.append("<TITULACION_3>");
                        xmlSalida.append(puesto.getTitulacion3());
                xmlSalida.append("</TITULACION_3>");
                xmlSalida.append("<SUBV_SOLIC>");
                        xmlSalida.append(puesto.getSubvSolic());
                xmlSalida.append("</SUBV_SOLIC>");
                xmlSalida.append("<SUBV_APROB>");
                        xmlSalida.append(puesto.getSubvAprob());
                xmlSalida.append("</SUBV_APROB>");
                xmlSalida.append("<RESULTADO>");
                        xmlSalida.append(puesto.getResultado());
                xmlSalida.append("</RESULTADO>");
                xmlSalida.append("<MOTIVO>");
                        xmlSalida.append(puesto.getMotivo());
                xmlSalida.append("</MOTIVO>");
                xmlSalida.append("<OBSERVACIONES>");
                        xmlSalida.append(puesto.getObservaciones());
                xmlSalida.append("</OBSERVACIONES>");
            xmlSalida.append("</FILA>");
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
            e.printStackTrace();
        }//try-catch
    }
    
    private void escribirListaOfertasRequest(String codigoOperacion, List<FilaOfertaVO> ofertas, Map<String, BigDecimal> calculos, datosCmeVO datos, HttpServletResponse response)
    {
        StringBuffer xmlSalida = new StringBuffer();
        xmlSalida.append("<RESPUESTA>");
            xmlSalida.append("<CODIGO_OPERACION>");
                xmlSalida.append(codigoOperacion);
            xmlSalida.append("</CODIGO_OPERACION>");
            
        if(calculos != null && !calculos.isEmpty())
        {
            xmlSalida.append("<CALCULOS>");
            
            xmlSalida.append("<IMP_SOL>");
            if(calculos.containsKey(ConstantesMeLanbide46.CAMPO_SUPL_IMP_SOLICITADO))
            {
                BigDecimal bd = calculos.get(ConstantesMeLanbide46.CAMPO_SUPL_IMP_SOLICITADO);
                xmlSalida.append(bd.toPlainString().replaceAll("\\.", ","));
            }
            xmlSalida.append("</IMP_SOL>");
            
            xmlSalida.append("<IMP_CONV>");
            if(calculos.containsKey(ConstantesMeLanbide46.CAMPO_SUPL_IMP_CONVOCATORIA))
            {
                BigDecimal bd = calculos.get(ConstantesMeLanbide46.CAMPO_SUPL_IMP_CONVOCATORIA);
                xmlSalida.append(bd.toPlainString().replaceAll("\\.", ","));
            }
            xmlSalida.append("</IMP_CONV>");
            
            xmlSalida.append("<IMP_PREV_CON>");
            if(calculos.containsKey(ConstantesMeLanbide46.CAMPO_SUPL_IMP_PREV_CON))
            {
                BigDecimal bd = calculos.get(ConstantesMeLanbide46.CAMPO_SUPL_IMP_PREV_CON);
                xmlSalida.append(bd.toPlainString().replaceAll("\\.", ","));
            }
            xmlSalida.append("</IMP_PREV_CON>");
            
            xmlSalida.append("<IMP_CON>");
            if(calculos.containsKey(ConstantesMeLanbide46.CAMPO_SUPL_IMP_CON))
            {
                BigDecimal bd = calculos.get(ConstantesMeLanbide46.CAMPO_SUPL_IMP_CON);
                xmlSalida.append(bd.toPlainString().replaceAll("\\.", ","));
            }
            xmlSalida.append("</IMP_CON>");
            
            xmlSalida.append("<IMP_JUS>");
//            if(calculos.containsKey(ConstantesMeLanbide46.CAMPO_SUPL_IMP_JUS))
//            {
//                BigDecimal bd = calculos.get(ConstantesMeLanbide46.CAMPO_SUPL_IMP_JUS);
//                xmlSalida.append(bd.toPlainString().replaceAll("\\.", ","));
//            }
            xmlSalida.append(datos.getImpJustificado().replaceAll("\\.", ","));
            xmlSalida.append("</IMP_JUS>");
            
            xmlSalida.append("<IMP_REN>");
            if(calculos.containsKey(ConstantesMeLanbide46.CAMPO_SUPL_IMP_REN))
            {
                BigDecimal bd = calculos.get(ConstantesMeLanbide46.CAMPO_SUPL_IMP_REN);
                xmlSalida.append(bd.toPlainString().replaceAll("\\.", ","));
            }
            xmlSalida.append("</IMP_REN>");
            
            xmlSalida.append("<IMP_PAG>");
            if(calculos.containsKey(ConstantesMeLanbide46.CAMPO_SUPL_IMP_PAG))
            {
                BigDecimal bd = calculos.get(ConstantesMeLanbide46.CAMPO_SUPL_IMP_PAG);
                xmlSalida.append(bd.toPlainString().replaceAll("\\.", ","));
            }
            xmlSalida.append("</IMP_PAG>");
            
            xmlSalida.append("<IMP_PAG_2>");
            if(calculos.containsKey(ConstantesMeLanbide46.CAMPO_SUPL_IMP_PAG_2))
            {
                BigDecimal bd = calculos.get(ConstantesMeLanbide46.CAMPO_SUPL_IMP_PAG_2);
                xmlSalida.append(bd.toPlainString().replaceAll("\\.", ","));
            }
            xmlSalida.append("</IMP_PAG_2>");
            
            xmlSalida.append("<IMP_REI>");
//            if(calculos.containsKey(ConstantesMeLanbide46.CAMPO_SUPL_IMP_REI))
//            {
//                BigDecimal bd = calculos.get(ConstantesMeLanbide46.CAMPO_SUPL_IMP_REI);
//                xmlSalida.append(bd.toPlainString().replaceAll("\\.", ","));
//            }
                xmlSalida.append(datos.getReintegro().replaceAll("\\.", ","));
            xmlSalida.append("</IMP_REI>");
            
            xmlSalida.append("<OTRAS_AYUDAS_SOLIC>");
            if(calculos.containsKey(ConstantesMeLanbide46.CAMPO_SUPL_OTRAS_AYUDAS_SOLIC))
            {
                BigDecimal bd = calculos.get(ConstantesMeLanbide46.CAMPO_SUPL_OTRAS_AYUDAS_SOLIC);
                xmlSalida.append(bd.toPlainString().replaceAll("\\.", ","));
            }
            xmlSalida.append("</OTRAS_AYUDAS_SOLIC>");
            
            xmlSalida.append("<OTRAS_AYUDAS_CONCE>");
            if(calculos.containsKey(ConstantesMeLanbide46.CAMPO_SUPL_OTRAS_AYUDAS_CONCE))
            {
                BigDecimal bd = calculos.get(ConstantesMeLanbide46.CAMPO_SUPL_OTRAS_AYUDAS_CONCE);
                xmlSalida.append(bd.toPlainString().replaceAll("\\.", ","));
            }
            xmlSalida.append("</OTRAS_AYUDAS_CONCE>");
            
            xmlSalida.append("<MINIMIS_SOLIC>");
            if(calculos.containsKey(ConstantesMeLanbide46.CAMPO_SUPL_MINIMIS_SOLIC))
            {
                BigDecimal bd = calculos.get(ConstantesMeLanbide46.CAMPO_SUPL_MINIMIS_SOLIC);
                xmlSalida.append(bd.toPlainString().replaceAll("\\.", ","));
            }
            xmlSalida.append("</MINIMIS_SOLIC>");
            
            xmlSalida.append("<MINIMIS_CONCE>");
            if(calculos.containsKey(ConstantesMeLanbide46.CAMPO_SUPL_MINIMIS_CONCE))
            {
                BigDecimal bd = calculos.get(ConstantesMeLanbide46.CAMPO_SUPL_MINIMIS_CONCE);
                xmlSalida.append(bd.toPlainString().replaceAll("\\.", ","));
            }
            xmlSalida.append("</MINIMIS_CONCE>");
            
            xmlSalida.append("<IMP_DESP>");
            if(calculos.containsKey(ConstantesMeLanbide46.CAMPO_SUPL_IMP_REN))
            {
                BigDecimal bd = calculos.get(ConstantesMeLanbide46.CAMPO_SUPL_IMP_DESP);
                xmlSalida.append(bd.toPlainString().replaceAll("\\.", ","));
            }
            xmlSalida.append("</IMP_DESP>");
            
            xmlSalida.append("<IMP_BAJA>");
            if(calculos.containsKey(ConstantesMeLanbide46.CAMPO_SUPL_IMP_BAJA))
            {
                BigDecimal bd = calculos.get(ConstantesMeLanbide46.CAMPO_SUPL_IMP_BAJA);
                xmlSalida.append(bd.toPlainString().replaceAll("\\.", ","));
            }
            xmlSalida.append("</IMP_BAJA>");
            
            xmlSalida.append("<CONCEDIDO_REAL>");
            if(calculos.containsKey("CONCEDIDO_REAL"))
            {
                BigDecimal bd = calculos.get("CONCEDIDO_REAL");
                xmlSalida.append(bd.toPlainString().replaceAll("\\.", ","));
            }
            xmlSalida.append("</CONCEDIDO_REAL>");
            xmlSalida.append("<PAGADO_REAL>");
            if(calculos.containsKey("PAGADO_REAL"))
            {
                BigDecimal bd = calculos.get("PAGADO_REAL");
                xmlSalida.append(bd.toPlainString().replaceAll("\\.", ","));
            }
            xmlSalida.append("</PAGADO_REAL>");
            xmlSalida.append("<PAGADO_REAL_2>");
            if(calculos.containsKey("PAGADO_REAL_2"))
            {
                BigDecimal bd = calculos.get("PAGADO_REAL_2");
                xmlSalida.append(bd.toPlainString().replaceAll("\\.", ","));
            }
            xmlSalida.append("</PAGADO_REAL_2>");
            
            xmlSalida.append("<IMP_NO_JUS>");
//            if(calculos.containsKey(ConstantesMeLanbide46.CAMPO_SUPL_IMP_NO_JUS))
//            {
//                BigDecimal bd = calculos.get(ConstantesMeLanbide46.CAMPO_SUPL_IMP_NO_JUS);
//                xmlSalida.append(bd.toPlainString().replaceAll("\\.", ","));
//            }
                xmlSalida.append(datos.getImpNoJusi().replaceAll("\\.", ","));
            xmlSalida.append("</IMP_NO_JUS>");
            
            xmlSalida.append("<IMP_REN_RES>");
            if(calculos.containsKey(ConstantesMeLanbide46.CAMPO_SUPL_IMP_REN_RES))
            {
                BigDecimal bd = calculos.get(ConstantesMeLanbide46.CAMPO_SUPL_IMP_REN_RES);
                xmlSalida.append(bd.toPlainString().replaceAll("\\.", ","));
            }
            xmlSalida.append("</IMP_REN_RES>");
            
            xmlSalida.append("<IMP_PAG_RES>");
            if(calculos.containsKey(ConstantesMeLanbide46.CAMPO_SUPL_IMP_PAG_RES))
            {
                BigDecimal bd = calculos.get(ConstantesMeLanbide46.CAMPO_SUPL_IMP_PAG_RES);
                xmlSalida.append(bd.toPlainString().replaceAll("\\.", ","));
            }
            xmlSalida.append("</IMP_PAG_RES>");
            xmlSalida.append("</CALCULOS>");
        }
        for(FilaOfertaVO oferta : ofertas)
        {
            xmlSalida.append("<FILA>");
                xmlSalida.append("<ID_OFERTA>");
                        xmlSalida.append(oferta.getIdOferta());
                xmlSalida.append("</ID_OFERTA>");
                xmlSalida.append("<COD_PUESTO>");
                        xmlSalida.append(oferta.getCodPuesto());
                xmlSalida.append("</COD_PUESTO>");
                xmlSalida.append("<DESC_PUESTO>");
                        xmlSalida.append(oferta.getDescPuesto());
                xmlSalida.append("</DESC_PUESTO>");
                xmlSalida.append("<DESC_OFERTA>");
                        xmlSalida.append(oferta.getDescOferta());
                xmlSalida.append("</DESC_OFERTA>");
                xmlSalida.append("<NOMAPEL>");
                        xmlSalida.append(oferta.getNomApel());
                xmlSalida.append("</NOMAPEL>");
                xmlSalida.append("<DNI>");
                        xmlSalida.append(oferta.getDni());
                xmlSalida.append("</DNI>");
                xmlSalida.append("<FEC_INI>");
                        xmlSalida.append(oferta.getFecIni());
                xmlSalida.append("</FEC_INI>");
                xmlSalida.append("<FEC_FIN>");
                        xmlSalida.append(oferta.getFecFin());
                xmlSalida.append("</FEC_FIN>");
                xmlSalida.append("<FEC_BAJA>");
                        xmlSalida.append(oferta.getFecBaja());
                xmlSalida.append("</FEC_BAJA>");
                xmlSalida.append("<CAUSA_BAJA>");
                        xmlSalida.append(oferta.getCausaBaja());
                xmlSalida.append("</CAUSA_BAJA>");
            xmlSalida.append("</FILA>");
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
            e.printStackTrace();
        }//try-catch
    }
    
    private void escribirListaJustificacionesRequest(String codigoOperacion, List<FilaJustificacionVO> justificaciones, Map<String, BigDecimal> calculos, datosCmeVO datos, HttpServletResponse response)
    {
        StringBuffer xmlSalida = new StringBuffer();
        xmlSalida.append("<RESPUESTA>");
            xmlSalida.append("<CODIGO_OPERACION>");
                xmlSalida.append(codigoOperacion);
            xmlSalida.append("</CODIGO_OPERACION>");
            
        if(calculos != null && !calculos.isEmpty())
        {
            xmlSalida.append("<CALCULOS>");
            
            xmlSalida.append("<IMP_SOL>");
            if(calculos.containsKey(ConstantesMeLanbide46.CAMPO_SUPL_IMP_SOLICITADO))
            {
                BigDecimal bd = calculos.get(ConstantesMeLanbide46.CAMPO_SUPL_IMP_SOLICITADO);
                xmlSalida.append(bd.toPlainString().replaceAll("\\.", ","));
            }
            xmlSalida.append("</IMP_SOL>");
            
            xmlSalida.append("<IMP_CONV>");
            if(calculos.containsKey(ConstantesMeLanbide46.CAMPO_SUPL_IMP_CONVOCATORIA))
            {
                BigDecimal bd = calculos.get(ConstantesMeLanbide46.CAMPO_SUPL_IMP_CONVOCATORIA);
                xmlSalida.append(bd.toPlainString().replaceAll("\\.", ","));
            }
            xmlSalida.append("</IMP_CONV>");
            
            xmlSalida.append("<IMP_PREV_CON>");
            if(calculos.containsKey(ConstantesMeLanbide46.CAMPO_SUPL_IMP_PREV_CON))
            {
                BigDecimal bd = calculos.get(ConstantesMeLanbide46.CAMPO_SUPL_IMP_PREV_CON);
                xmlSalida.append(bd.toPlainString().replaceAll("\\.", ","));
            }
            xmlSalida.append("</IMP_PREV_CON>");
            
            xmlSalida.append("<IMP_CON>");
            if(calculos.containsKey(ConstantesMeLanbide46.CAMPO_SUPL_IMP_CON))
            {
                BigDecimal bd = calculos.get(ConstantesMeLanbide46.CAMPO_SUPL_IMP_CON);
                xmlSalida.append(bd.toPlainString().replaceAll("\\.", ","));
            }
            xmlSalida.append("</IMP_CON>");
            
            xmlSalida.append("<IMP_JUS>");
//            if(calculos.containsKey(ConstantesMeLanbide46.CAMPO_SUPL_IMP_JUS))
//            {
//                BigDecimal bd = calculos.get(ConstantesMeLanbide46.CAMPO_SUPL_IMP_JUS);
//                xmlSalida.append(bd.toPlainString().replaceAll("\\.", ","));
//            }
            xmlSalida.append(datos.getImpJustificado().replaceAll("\\.", ","));
            xmlSalida.append("</IMP_JUS>");
            
            xmlSalida.append("<IMP_REN>");
            if(calculos.containsKey(ConstantesMeLanbide46.CAMPO_SUPL_IMP_REN))
            {
                BigDecimal bd = calculos.get(ConstantesMeLanbide46.CAMPO_SUPL_IMP_REN);
                xmlSalida.append(bd.toPlainString().replaceAll("\\.", ","));
            }
            xmlSalida.append("</IMP_REN>");
            
            xmlSalida.append("<IMP_PAG>");
            if(calculos.containsKey(ConstantesMeLanbide46.CAMPO_SUPL_IMP_PAG))
            {
                BigDecimal bd = calculos.get(ConstantesMeLanbide46.CAMPO_SUPL_IMP_PAG);
                xmlSalida.append(bd.toPlainString().replaceAll("\\.", ","));
            }
            xmlSalida.append("</IMP_PAG>");
            
            xmlSalida.append("<IMP_PAG_2>");
            if(calculos.containsKey(ConstantesMeLanbide46.CAMPO_SUPL_IMP_PAG_2))
            {
                BigDecimal bd = calculos.get(ConstantesMeLanbide46.CAMPO_SUPL_IMP_PAG_2);
                xmlSalida.append(bd.toPlainString().replaceAll("\\.", ","));
            }
            xmlSalida.append("</IMP_PAG_2>");
            
            xmlSalida.append("<IMP_REI>");
//            if(calculos.containsKey(ConstantesMeLanbide46.CAMPO_SUPL_IMP_REI))
//            {
//                BigDecimal bd = calculos.get(ConstantesMeLanbide46.CAMPO_SUPL_IMP_REI);
//                xmlSalida.append(bd.toPlainString().replaceAll("\\.", ","));
//            }
            xmlSalida.append(datos.getReintegro().replaceAll("\\.", ","));
            xmlSalida.append("</IMP_REI>");
            
            xmlSalida.append("<OTRAS_AYUDAS_SOLIC>");
            if(calculos.containsKey(ConstantesMeLanbide46.CAMPO_SUPL_OTRAS_AYUDAS_SOLIC))
            {
                BigDecimal bd = calculos.get(ConstantesMeLanbide46.CAMPO_SUPL_OTRAS_AYUDAS_SOLIC);
                xmlSalida.append(bd.toPlainString().replaceAll("\\.", ","));
            }
            xmlSalida.append("</OTRAS_AYUDAS_SOLIC>");
            
            xmlSalida.append("<OTRAS_AYUDAS_CONCE>");
            if(calculos.containsKey(ConstantesMeLanbide46.CAMPO_SUPL_OTRAS_AYUDAS_CONCE))
            {
                BigDecimal bd = calculos.get(ConstantesMeLanbide46.CAMPO_SUPL_OTRAS_AYUDAS_CONCE);
                xmlSalida.append(bd.toPlainString().replaceAll("\\.", ","));
            }
            xmlSalida.append("</OTRAS_AYUDAS_CONCE>");
            
            xmlSalida.append("<MINIMIS_SOLIC>");
            if(calculos.containsKey(ConstantesMeLanbide46.CAMPO_SUPL_MINIMIS_SOLIC))
            {
                BigDecimal bd = calculos.get(ConstantesMeLanbide46.CAMPO_SUPL_MINIMIS_SOLIC);
                xmlSalida.append(bd.toPlainString().replaceAll("\\.", ","));
            }
            xmlSalida.append("</MINIMIS_SOLIC>");
            
            xmlSalida.append("<MINIMIS_CONCE>");
            if(calculos.containsKey(ConstantesMeLanbide46.CAMPO_SUPL_MINIMIS_CONCE))
            {
                BigDecimal bd = calculos.get(ConstantesMeLanbide46.CAMPO_SUPL_MINIMIS_CONCE);
                xmlSalida.append(bd.toPlainString().replaceAll("\\.", ","));
            }
            xmlSalida.append("</MINIMIS_CONCE>");
            
            xmlSalida.append("<IMP_DESP>");
            if(calculos.containsKey(ConstantesMeLanbide46.CAMPO_SUPL_IMP_REN))
            {
                BigDecimal bd = calculos.get(ConstantesMeLanbide46.CAMPO_SUPL_IMP_DESP);
                xmlSalida.append(bd.toPlainString().replaceAll("\\.", ","));
            }
            xmlSalida.append("</IMP_DESP>");
            
            xmlSalida.append("<IMP_BAJA>");
            if(calculos.containsKey(ConstantesMeLanbide46.CAMPO_SUPL_IMP_BAJA))
            {
                BigDecimal bd = calculos.get(ConstantesMeLanbide46.CAMPO_SUPL_IMP_BAJA);
                xmlSalida.append(bd.toPlainString().replaceAll("\\.", ","));
            }
            xmlSalida.append("</IMP_BAJA>");
            
            xmlSalida.append("<CONCEDIDO_REAL>");
            if(calculos.containsKey("CONCEDIDO_REAL"))
            {
                BigDecimal bd = calculos.get("CONCEDIDO_REAL");
                xmlSalida.append(bd.toPlainString().replaceAll("\\.", ","));
            }
            xmlSalida.append("</CONCEDIDO_REAL>");
            xmlSalida.append("<PAGADO_REAL>");
            if(calculos.containsKey("PAGADO_REAL"))
            {
                BigDecimal bd = calculos.get("PAGADO_REAL");
                xmlSalida.append(bd.toPlainString().replaceAll("\\.", ","));
            }
            xmlSalida.append("</PAGADO_REAL>");
            xmlSalida.append("<PAGADO_REAL_2>");
            if(calculos.containsKey("PAGADO_REAL_2"))
            {
                BigDecimal bd = calculos.get("PAGADO_REAL_2");
                xmlSalida.append(bd.toPlainString().replaceAll("\\.", ","));
            }
            xmlSalida.append("</PAGADO_REAL_2>");
            
            xmlSalida.append("<IMP_NO_JUS>");
//            if(calculos.containsKey(ConstantesMeLanbide46.CAMPO_SUPL_IMP_NO_JUS))
//            {
//                BigDecimal bd = calculos.get(ConstantesMeLanbide46.CAMPO_SUPL_IMP_NO_JUS);
//                xmlSalida.append(bd.toPlainString().replaceAll("\\.", ","));
//            }
                xmlSalida.append(datos.getImpNoJusi().replaceAll("\\.", ","));
            xmlSalida.append("</IMP_NO_JUS>");
            
            xmlSalida.append("<IMP_REN_RES>");
            if(calculos.containsKey(ConstantesMeLanbide46.CAMPO_SUPL_IMP_REN_RES))
            {
                BigDecimal bd = calculos.get(ConstantesMeLanbide46.CAMPO_SUPL_IMP_REN_RES);
                xmlSalida.append(bd.toPlainString().replaceAll("\\.", ","));
            }
            xmlSalida.append("</IMP_REN_RES>");
            
            xmlSalida.append("<IMP_PAG_RES>");
            if(calculos.containsKey(ConstantesMeLanbide46.CAMPO_SUPL_IMP_PAG_RES))
            {
                BigDecimal bd = calculos.get(ConstantesMeLanbide46.CAMPO_SUPL_IMP_PAG_RES);
                xmlSalida.append(bd.toPlainString().replaceAll("\\.", ","));
            }
            xmlSalida.append("</IMP_PAG_RES>");
            xmlSalida.append("</CALCULOS>");
        }
        for(FilaJustificacionVO justif : justificaciones)
        {
            xmlSalida.append("<FILA>");
                xmlSalida.append("<ID_JUSTIF>");
                        xmlSalida.append(justif.getIdJustificacion());
                xmlSalida.append("</ID_JUSTIF>");
                xmlSalida.append("<COD_PUESTO>");
                        xmlSalida.append(justif.getCodPuesto());
                xmlSalida.append("</COD_PUESTO>");
                xmlSalida.append("<ID_OFERTA>");
                        xmlSalida.append(justif.getIdOferta());
                xmlSalida.append("</ID_OFERTA>");
                xmlSalida.append("<DESC_PUESTO>");
                        xmlSalida.append(justif.getDescPuesto());
                xmlSalida.append("</DESC_PUESTO>");
                xmlSalida.append("<IMP_SOLIC>");
                    xmlSalida.append(justif.getImpSolic());
                xmlSalida.append("</IMP_SOLIC>");
                xmlSalida.append("<IMP_JUSTIF>");
                    xmlSalida.append(justif.getImpJustif());
                xmlSalida.append("</IMP_JUSTIF>");
                xmlSalida.append("<NUM_CONTRATACIONES>");
                    xmlSalida.append(justif.getNumContrataciones());
                xmlSalida.append("</NUM_CONTRATACIONES>");
                xmlSalida.append("<ESTADO>");
                        xmlSalida.append(justif.getEstado());
                xmlSalida.append("</ESTADO>");
            xmlSalida.append("</FILA>");
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
            e.printStackTrace();
        }//try-catch
    }
    
    private void escribirListaContratacionesRequest(String codigoOperacion, List<FilaPersonaContratadaVO> contrataciones, Map<String, BigDecimal> calculos, datosCmeVO datos, HttpServletResponse response)
    {
        StringBuffer xmlSalida = new StringBuffer();
        xmlSalida.append("<RESPUESTA>");
            xmlSalida.append("<CODIGO_OPERACION>");
                xmlSalida.append(codigoOperacion);
            xmlSalida.append("</CODIGO_OPERACION>");
            
        if(calculos != null && !calculos.isEmpty())
        {
            xmlSalida.append("<CALCULOS>");
            
            xmlSalida.append("<IMP_SOL>");
            if(calculos.containsKey(ConstantesMeLanbide46.CAMPO_SUPL_IMP_SOLICITADO))
            {
                BigDecimal bd = calculos.get(ConstantesMeLanbide46.CAMPO_SUPL_IMP_SOLICITADO);
                xmlSalida.append(bd.toPlainString().replaceAll("\\.", ","));
            }
            xmlSalida.append("</IMP_SOL>");
            
            xmlSalida.append("<IMP_CONV>");
            if(calculos.containsKey(ConstantesMeLanbide46.CAMPO_SUPL_IMP_CONVOCATORIA))
            {
                BigDecimal bd = calculos.get(ConstantesMeLanbide46.CAMPO_SUPL_IMP_CONVOCATORIA);
                xmlSalida.append(bd.toPlainString().replaceAll("\\.", ","));
            }
            xmlSalida.append("</IMP_CONV>");
            
            xmlSalida.append("<IMP_PREV_CON>");
            if(calculos.containsKey(ConstantesMeLanbide46.CAMPO_SUPL_IMP_PREV_CON))
            {
                BigDecimal bd = calculos.get(ConstantesMeLanbide46.CAMPO_SUPL_IMP_PREV_CON);
                xmlSalida.append(bd.toPlainString().replaceAll("\\.", ","));
            }
            xmlSalida.append("</IMP_PREV_CON>");
            
            xmlSalida.append("<IMP_CON>");
            if(calculos.containsKey(ConstantesMeLanbide46.CAMPO_SUPL_IMP_CON))
            {
                BigDecimal bd = calculos.get(ConstantesMeLanbide46.CAMPO_SUPL_IMP_CON);
                xmlSalida.append(bd.toPlainString().replaceAll("\\.", ","));
            }
            xmlSalida.append("</IMP_CON>");
            
            xmlSalida.append("<IMP_JUS>");
//            if(calculos.containsKey(ConstantesMeLanbide46.CAMPO_SUPL_IMP_JUS))
//            {
//                BigDecimal bd = calculos.get(ConstantesMeLanbide46.CAMPO_SUPL_IMP_JUS);
//                xmlSalida.append(bd.toPlainString().replaceAll("\\.", ","));
//            }
            xmlSalida.append(datos.getImpJustificado().replaceAll("\\.", ","));
            xmlSalida.append("</IMP_JUS>");
            
            xmlSalida.append("<IMP_REN>");
            if(calculos.containsKey(ConstantesMeLanbide46.CAMPO_SUPL_IMP_REN))
            {
                BigDecimal bd = calculos.get(ConstantesMeLanbide46.CAMPO_SUPL_IMP_REN);
                xmlSalida.append(bd.toPlainString().replaceAll("\\.", ","));
            }
            xmlSalida.append("</IMP_REN>");
            
            xmlSalida.append("<IMP_PAG>");
            if(calculos.containsKey(ConstantesMeLanbide46.CAMPO_SUPL_IMP_PAG))
            {
                BigDecimal bd = calculos.get(ConstantesMeLanbide46.CAMPO_SUPL_IMP_PAG);
                xmlSalida.append(bd.toPlainString().replaceAll("\\.", ","));
            }
            xmlSalida.append("</IMP_PAG>");
            
            xmlSalida.append("<IMP_PAG_2>");
            if(calculos.containsKey(ConstantesMeLanbide46.CAMPO_SUPL_IMP_PAG_2))
            {
                BigDecimal bd = calculos.get(ConstantesMeLanbide46.CAMPO_SUPL_IMP_PAG_2);
                xmlSalida.append(bd.toPlainString().replaceAll("\\.", ","));
            }
            xmlSalida.append("</IMP_PAG_2>");
            
            xmlSalida.append("<IMP_REI>");
//            if(calculos.containsKey(ConstantesMeLanbide46.CAMPO_SUPL_IMP_REI))
//            {
//                BigDecimal bd = calculos.get(ConstantesMeLanbide46.CAMPO_SUPL_IMP_REI);
//                xmlSalida.append(bd.toPlainString().replaceAll("\\.", ","));
//            }
            xmlSalida.append(datos.getReintegro().replaceAll("\\.", ","));
            xmlSalida.append("</IMP_REI>");
            
            xmlSalida.append("<OTRAS_AYUDAS_SOLIC>");
            if(calculos.containsKey(ConstantesMeLanbide46.CAMPO_SUPL_OTRAS_AYUDAS_SOLIC))
            {
                BigDecimal bd = calculos.get(ConstantesMeLanbide46.CAMPO_SUPL_OTRAS_AYUDAS_SOLIC);
                xmlSalida.append(bd.toPlainString().replaceAll("\\.", ","));
            }
            xmlSalida.append("</OTRAS_AYUDAS_SOLIC>");
            
            xmlSalida.append("<OTRAS_AYUDAS_CONCE>");
            if(calculos.containsKey(ConstantesMeLanbide46.CAMPO_SUPL_OTRAS_AYUDAS_CONCE))
            {
                BigDecimal bd = calculos.get(ConstantesMeLanbide46.CAMPO_SUPL_OTRAS_AYUDAS_CONCE);
                xmlSalida.append(bd.toPlainString().replaceAll("\\.", ","));
            }
            xmlSalida.append("</OTRAS_AYUDAS_CONCE>");
            
            xmlSalida.append("<MINIMIS_SOLIC>");
            if(calculos.containsKey(ConstantesMeLanbide46.CAMPO_SUPL_MINIMIS_SOLIC))
            {
                BigDecimal bd = calculos.get(ConstantesMeLanbide46.CAMPO_SUPL_MINIMIS_SOLIC);
                xmlSalida.append(bd.toPlainString().replaceAll("\\.", ","));
            }
            xmlSalida.append("</MINIMIS_SOLIC>");
            
            xmlSalida.append("<MINIMIS_CONCE>");
            if(calculos.containsKey(ConstantesMeLanbide46.CAMPO_SUPL_MINIMIS_CONCE))
            {
                BigDecimal bd = calculos.get(ConstantesMeLanbide46.CAMPO_SUPL_MINIMIS_CONCE);
                xmlSalida.append(bd.toPlainString().replaceAll("\\.", ","));
            }
            xmlSalida.append("</MINIMIS_CONCE>");
            
            xmlSalida.append("<IMP_DESP>");
            if(calculos.containsKey(ConstantesMeLanbide46.CAMPO_SUPL_IMP_REN))
            {
                BigDecimal bd = calculos.get(ConstantesMeLanbide46.CAMPO_SUPL_IMP_DESP);
                xmlSalida.append(bd.toPlainString().replaceAll("\\.", ","));
            }
            xmlSalida.append("</IMP_DESP>");
            
            xmlSalida.append("<IMP_BAJA>");
            if(calculos.containsKey(ConstantesMeLanbide46.CAMPO_SUPL_IMP_BAJA))
            {
                BigDecimal bd = calculos.get(ConstantesMeLanbide46.CAMPO_SUPL_IMP_BAJA);
                xmlSalida.append(bd.toPlainString().replaceAll("\\.", ","));
            }
            xmlSalida.append("</IMP_BAJA>");
            
            xmlSalida.append("<CONCEDIDO_REAL>");
            if(calculos.containsKey("CONCEDIDO_REAL"))
            {
                BigDecimal bd = calculos.get("CONCEDIDO_REAL");
                xmlSalida.append(bd.toPlainString().replaceAll("\\.", ","));
            }
            xmlSalida.append("</CONCEDIDO_REAL>");
            xmlSalida.append("<PAGADO_REAL>");
            if(calculos.containsKey("PAGADO_REAL"))
            {
                BigDecimal bd = calculos.get("PAGADO_REAL");
                xmlSalida.append(bd.toPlainString().replaceAll("\\.", ","));
            }
            xmlSalida.append("</PAGADO_REAL>");
            xmlSalida.append("<PAGADO_REAL_2>");
            if(calculos.containsKey("PAGADO_REAL_2"))
            {
                BigDecimal bd = calculos.get("PAGADO_REAL_2");
                xmlSalida.append(bd.toPlainString().replaceAll("\\.", ","));
            }
            xmlSalida.append("</PAGADO_REAL_2>");
            
            xmlSalida.append("<IMP_NO_JUS>");
//            if(calculos.containsKey(ConstantesMeLanbide46.CAMPO_SUPL_IMP_NO_JUS))
//            {
//                BigDecimal bd = calculos.get(ConstantesMeLanbide46.CAMPO_SUPL_IMP_NO_JUS);
//                xmlSalida.append(bd.toPlainString().replaceAll("\\.", ","));
//            }
            xmlSalida.append(datos.getImpNoJusi().replaceAll("\\.", ","));
            xmlSalida.append("</IMP_NO_JUS>");
            
            xmlSalida.append("<IMP_REN_RES>");
            if(calculos.containsKey(ConstantesMeLanbide46.CAMPO_SUPL_IMP_REN_RES))
            {
                BigDecimal bd = calculos.get(ConstantesMeLanbide46.CAMPO_SUPL_IMP_REN_RES);
                xmlSalida.append(bd.toPlainString().replaceAll("\\.", ","));
            }
            xmlSalida.append("</IMP_REN_RES>");
            
            xmlSalida.append("<IMP_PAG_RES>");
            if(calculos.containsKey(ConstantesMeLanbide46.CAMPO_SUPL_IMP_PAG_RES))
            {
                BigDecimal bd = calculos.get(ConstantesMeLanbide46.CAMPO_SUPL_IMP_PAG_RES);
                xmlSalida.append(bd.toPlainString().replaceAll("\\.", ","));
            }
            xmlSalida.append("</IMP_PAG_RES>");
            xmlSalida.append("</CALCULOS>");
        }
        for(FilaPersonaContratadaVO cont : contrataciones)
        {
            xmlSalida.append("<FILA>");
                xmlSalida.append("<ID_OFERTA>");
                        xmlSalida.append(cont.getIdOferta());
                xmlSalida.append("</ID_OFERTA>");
                xmlSalida.append("<ID_JUSTIF>");
                        xmlSalida.append(cont.getIdJustificacion());
                xmlSalida.append("</ID_JUSTIF>");
                xmlSalida.append("<NIF>");
                        xmlSalida.append(cont.getNif());
                xmlSalida.append("</NIF>");
                xmlSalida.append("<NOMAPEL>");
                    xmlSalida.append(cont.getNomApel());
                xmlSalida.append("</NOMAPEL>");
                xmlSalida.append("<FE_DESDE>");
                    xmlSalida.append(cont.getFeDesde());
                xmlSalida.append("</FE_DESDE>");
                xmlSalida.append("<FE_HASTA>");
                    xmlSalida.append(cont.getFeHasta());
                xmlSalida.append("</FE_HASTA>");
                xmlSalida.append("<IMP_JUSTIF>");
                    xmlSalida.append(cont.getImpJustif());
                xmlSalida.append("</IMP_JUSTIF>");
                
                xmlSalida.append("<SALARIO_SUB>");
                    xmlSalida.append(cont.getSalarioSub());
                xmlSalida.append("</SALARIO_SUB>");
                xmlSalida.append("<DIETAS_JUSTI>");
                    xmlSalida.append(cont.getDietasJusti());
                xmlSalida.append("</DIETAS_JUSTI>");
                xmlSalida.append("<GASTOS_GESION>");
                    xmlSalida.append(cont.getGastosGestion());
                xmlSalida.append("</GASTOS_GESION>");
                xmlSalida.append("<BONIF>");
                    xmlSalida.append(cont.getBonif());
                xmlSalida.append("</BONIF>");
            xmlSalida.append("</FILA>");
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
            e.printStackTrace();
        }//try-catch
    }
    
    private void escribirCalculosCmeRequest(String codigoOperacion, Map<String, BigDecimal> calculos, datosCmeVO datos, HttpServletResponse response)
    {
        StringBuffer xmlSalida = new StringBuffer();
        xmlSalida.append("<RESPUESTA>");
            xmlSalida.append("<CODIGO_OPERACION>");
                xmlSalida.append(codigoOperacion);
            xmlSalida.append("</CODIGO_OPERACION>");
            
        if(calculos != null && !calculos.isEmpty())
        {
            xmlSalida.append("<CALCULOS>");
            
            xmlSalida.append("<IMP_SOL>");
            if(calculos.containsKey(ConstantesMeLanbide46.CAMPO_SUPL_IMP_SOLICITADO))
            {
                BigDecimal bd = calculos.get(ConstantesMeLanbide46.CAMPO_SUPL_IMP_SOLICITADO);
                xmlSalida.append(bd.toPlainString().replaceAll("\\.", ","));
            }
            xmlSalida.append("</IMP_SOL>");
            
            xmlSalida.append("<IMP_CONV>");
            if(calculos.containsKey(ConstantesMeLanbide46.CAMPO_SUPL_IMP_CONVOCATORIA))
            {
                BigDecimal bd = calculos.get(ConstantesMeLanbide46.CAMPO_SUPL_IMP_CONVOCATORIA);
                xmlSalida.append(bd.toPlainString().replaceAll("\\.", ","));
            }
            xmlSalida.append("</IMP_CONV>");
            
            xmlSalida.append("<IMP_PREV_CON>");
            if(calculos.containsKey(ConstantesMeLanbide46.CAMPO_SUPL_IMP_PREV_CON))
            {
                BigDecimal bd = calculos.get(ConstantesMeLanbide46.CAMPO_SUPL_IMP_PREV_CON);
                xmlSalida.append(bd.toPlainString().replaceAll("\\.", ","));
            }
            xmlSalida.append("</IMP_PREV_CON>");
            
            xmlSalida.append("<IMP_CON>");
            if(calculos.containsKey(ConstantesMeLanbide46.CAMPO_SUPL_IMP_CON))
            {
                BigDecimal bd = calculos.get(ConstantesMeLanbide46.CAMPO_SUPL_IMP_CON);
                xmlSalida.append(bd.toPlainString().replaceAll("\\.", ","));
            }
            xmlSalida.append("</IMP_CON>");
            
            xmlSalida.append("<IMP_JUS>");
            xmlSalida.append(datos.getImpJustificado().replaceAll("\\.", ","));
//            if(calculos.containsKey(ConstantesMeLanbide46.CAMPO_SUPL_IMP_JUS))
//            {
//                BigDecimal bd = calculos.get(ConstantesMeLanbide46.CAMPO_SUPL_IMP_JUS);
//                xmlSalida.append(bd.toPlainString().replaceAll("\\.", ","));
//            }
            xmlSalida.append("</IMP_JUS>");
            
            xmlSalida.append("<IMP_REN>");
            if(calculos.containsKey(ConstantesMeLanbide46.CAMPO_SUPL_IMP_REN))
            {
                BigDecimal bd = calculos.get(ConstantesMeLanbide46.CAMPO_SUPL_IMP_REN);
                xmlSalida.append(bd.toPlainString().replaceAll("\\.", ","));
            }
            xmlSalida.append("</IMP_REN>");
            
            xmlSalida.append("<IMP_PAG>");
            if(calculos.containsKey(ConstantesMeLanbide46.CAMPO_SUPL_IMP_PAG))
            {
                BigDecimal bd = calculos.get(ConstantesMeLanbide46.CAMPO_SUPL_IMP_PAG);
                xmlSalida.append(bd.toPlainString().replaceAll("\\.", ","));
            }
            xmlSalida.append("</IMP_PAG>");
            
            xmlSalida.append("<IMP_PAG_2>");
            if(calculos.containsKey(ConstantesMeLanbide46.CAMPO_SUPL_IMP_PAG_2))
            {
                BigDecimal bd = calculos.get(ConstantesMeLanbide46.CAMPO_SUPL_IMP_PAG_2);
                xmlSalida.append(bd.toPlainString().replaceAll("\\.", ","));
            }
            xmlSalida.append("</IMP_PAG_2>");
            
            xmlSalida.append("<IMP_REI>");
            xmlSalida.append(datos.getReintegro().replaceAll("\\.", ","));
//            if(calculos.containsKey(ConstantesMeLanbide46.CAMPO_SUPL_IMP_REI))
//            {
//                BigDecimal bd = calculos.get(ConstantesMeLanbide46.CAMPO_SUPL_IMP_REI);
//                xmlSalida.append(bd.toPlainString().replaceAll("\\.", ","));
//            }
            xmlSalida.append("</IMP_REI>");
            
            xmlSalida.append("<OTRAS_AYUDAS_SOLIC>");
            if(calculos.containsKey(ConstantesMeLanbide46.CAMPO_SUPL_OTRAS_AYUDAS_SOLIC))
            {
                BigDecimal bd = calculos.get(ConstantesMeLanbide46.CAMPO_SUPL_OTRAS_AYUDAS_SOLIC);
                xmlSalida.append(bd.toPlainString().replaceAll("\\.", ","));
            }
            xmlSalida.append("</OTRAS_AYUDAS_SOLIC>");
            
            xmlSalida.append("<OTRAS_AYUDAS_CONCE>");
            if(calculos.containsKey(ConstantesMeLanbide46.CAMPO_SUPL_OTRAS_AYUDAS_CONCE))
            {
                BigDecimal bd = calculos.get(ConstantesMeLanbide46.CAMPO_SUPL_OTRAS_AYUDAS_CONCE);
                xmlSalida.append(bd.toPlainString().replaceAll("\\.", ","));
            }
            xmlSalida.append("</OTRAS_AYUDAS_CONCE>");
            
            xmlSalida.append("<MINIMIS_SOLIC>");
            if(calculos.containsKey(ConstantesMeLanbide46.CAMPO_SUPL_MINIMIS_SOLIC))
            {
                BigDecimal bd = calculos.get(ConstantesMeLanbide46.CAMPO_SUPL_MINIMIS_SOLIC);
                xmlSalida.append(bd.toPlainString().replaceAll("\\.", ","));
            }
            xmlSalida.append("</MINIMIS_SOLIC>");
            
            xmlSalida.append("<MINIMIS_CONCE>");
            if(calculos.containsKey(ConstantesMeLanbide46.CAMPO_SUPL_MINIMIS_CONCE))
            {
                BigDecimal bd = calculos.get(ConstantesMeLanbide46.CAMPO_SUPL_MINIMIS_CONCE);
                xmlSalida.append(bd.toPlainString().replaceAll("\\.", ","));
            }
            xmlSalida.append("</MINIMIS_CONCE>");
            
            xmlSalida.append("<IMP_DESP>");
            if(calculos.containsKey(ConstantesMeLanbide46.CAMPO_SUPL_IMP_REN))
            {
                BigDecimal bd = calculos.get(ConstantesMeLanbide46.CAMPO_SUPL_IMP_DESP);
                xmlSalida.append(bd.toPlainString().replaceAll("\\.", ","));
            }
            xmlSalida.append("</IMP_DESP>");
            
            xmlSalida.append("<IMP_BAJA>");
            if(calculos.containsKey(ConstantesMeLanbide46.CAMPO_SUPL_IMP_BAJA))
            {
                BigDecimal bd = calculos.get(ConstantesMeLanbide46.CAMPO_SUPL_IMP_BAJA);
                xmlSalida.append(bd.toPlainString().replaceAll("\\.", ","));
            }
            xmlSalida.append("</IMP_BAJA>");
            
            xmlSalida.append("<CONCEDIDO_REAL>");
            if(calculos.containsKey("CONCEDIDO_REAL"))
            {
                BigDecimal bd = calculos.get("CONCEDIDO_REAL");
                xmlSalida.append(bd.toPlainString().replaceAll("\\.", ","));
            }
            xmlSalida.append("</CONCEDIDO_REAL>");
            xmlSalida.append("<PAGADO_REAL>");
            if(calculos.containsKey("PAGADO_REAL"))
            {
                BigDecimal bd = calculos.get("PAGADO_REAL");
                xmlSalida.append(bd.toPlainString().replaceAll("\\.", ","));
            }
            xmlSalida.append("</PAGADO_REAL>");
            xmlSalida.append("<PAGADO_REAL_2>");
            if(calculos.containsKey("PAGADO_REAL_2"))
            {
                BigDecimal bd = calculos.get("PAGADO_REAL_2");
                xmlSalida.append(bd.toPlainString().replaceAll("\\.", ","));
            }
            xmlSalida.append("</PAGADO_REAL_2>");
            
            xmlSalida.append("<IMP_NO_JUS>");
            
            xmlSalida.append(datos.getImpNoJusi().replaceAll("\\.", ","));
//            if(calculos.containsKey(ConstantesMeLanbide46.CAMPO_SUPL_IMP_NO_JUS))
//            {
//                BigDecimal bd = calculos.get(ConstantesMeLanbide46.CAMPO_SUPL_IMP_NO_JUS);
//                xmlSalida.append(bd.toPlainString().replaceAll("\\.", ","));
//            }
            xmlSalida.append("</IMP_NO_JUS>");
            
            xmlSalida.append("<IMP_REN_RES>");
            if(calculos.containsKey(ConstantesMeLanbide46.CAMPO_SUPL_IMP_REN_RES))
            {
                BigDecimal bd = calculos.get(ConstantesMeLanbide46.CAMPO_SUPL_IMP_REN_RES);
                xmlSalida.append(bd.toPlainString().replaceAll("\\.", ","));
            }
            xmlSalida.append("</IMP_REN_RES>");
            
            xmlSalida.append("<IMP_PAG_RES>");
            if(calculos.containsKey(ConstantesMeLanbide46.CAMPO_SUPL_IMP_PAG_RES))
            {
                BigDecimal bd = calculos.get(ConstantesMeLanbide46.CAMPO_SUPL_IMP_PAG_RES);
                xmlSalida.append(bd.toPlainString().replaceAll("\\.", ","));
            }
            xmlSalida.append("</IMP_PAG_RES>");
            xmlSalida.append("</CALCULOS>");
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
            e.printStackTrace();
        }//try-catch
    }
    
    
     public void calcularMeses(int codOrganizacion, int codTramite, int ocurrenciaTramite,   String numExpediente, HttpServletRequest request, HttpServletResponse response)
    {
        String codigoOperacion = "0";
        Integer meses =0;
        
        
        try
        {
            AdaptadorSQLBD adaptador = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
            String fini = (String)request.getParameter("fini");
            String ffin = (String)request.getParameter("ffin");
            
            meses = MeLanbide46Manager.getInstance().getNumMeses(fini, ffin, adaptador);
        }catch(Exception e){
                e.printStackTrace();
        }
        
        
        StringBuffer xmlSalida = new StringBuffer();
        xmlSalida.append("<RESPUESTA>");
            xmlSalida.append("<MESES>");
                xmlSalida.append(meses);
            xmlSalida.append("</MESES>");
        xmlSalida.append("</RESPUESTA>");
        try{
            response.setContentType("text/xml");
            response.setCharacterEncoding("UTF-8");
            PrintWriter out = response.getWriter();
            out.print(xmlSalida.toString());
            out.flush();
            out.close();
        }catch(Exception e){
            e.printStackTrace();
        }//try-catch
            
       
    }
    
     public void  copiarDatosPuesto(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response)
    {
        String codigoOperacion = "0";
        
        int codIdioma = 1;    
        try
        {
            AdaptadorSQLBD adaptador = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
            UsuarioValueObject usuario = (UsuarioValueObject) request.getSession().getAttribute("usuario");
            if(usuario != null)
            {
                codIdioma = usuario.getIdioma();
            }
                    
            Integer ejercicio = MeLanbide46Utils.getEjercicioDeExpediente(numExpediente);
            if(ejercicio != null)
            {
                SimpleDateFormat format = new SimpleDateFormat(ConstantesMeLanbide46.FORMATO_FECHA);
                
                MultipartRequestWrapper wrapper = new MultipartRequestWrapper(request);
            
                wrapper.getParameterMap();
                
                String codOferta = (String)request.getParameter("idOferta");
                String codPuesto = (String)request.getParameter("idPuesto");

                cargarCombosNuevaOferta(codOrganizacion, request);

                CmeOfertaVO oferta = null;
                CmePuestoVO puesto = new CmePuestoVO();
                
                    puesto.setCodPuesto(codPuesto);
                    puesto.setEjercicio(ejercicio);
                    puesto.setNumExp(numExpediente);
                    puesto = MeLanbide46Manager.getInstance().getPuestoPorCodigoYExpediente(puesto, adaptador);
                         
                    oferta = new CmeOfertaVO();
                    oferta.setIdOferta(Integer.parseInt(codOferta));
                    oferta.setCodPuesto(codPuesto);
                    oferta.setExpEje(ejercicio);
                    oferta.setNumExp(numExpediente);
                    oferta = MeLanbide46Manager.getInstance().getOfertaPorCodigoPuestoYExpediente(oferta, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                                        
                    oferta = MeLanbide46Manager.getInstance().copiarDatosPuesto(codOrganizacion, puesto, oferta, adaptador);
                    StringBuffer xmlSalida = new StringBuffer();
                    xmlSalida.append("<RESPUESTA>");
                   
                        xmlSalida.append("<DESCPUESTO>");
                            xmlSalida.append(oferta.getDescPuesto()!=null?oferta.getDescPuesto():"");
                        xmlSalida.append("</DESCPUESTO>");
                        xmlSalida.append("<CODPAIS1>");
                            xmlSalida.append(oferta.getPaiCod1()!=null?oferta.getPaiCod1():"");
                        xmlSalida.append("</CODPAIS1>");
                        xmlSalida.append("<CODPAIS2>");
                            xmlSalida.append(oferta.getPaiCod2()!=null?oferta.getPaiCod2():"");
                        xmlSalida.append("</CODPAIS2>");
                        xmlSalida.append("<CODPAIS3>");
                            xmlSalida.append(oferta.getPaiCod3()!=null?oferta.getPaiCod3():"");
                        xmlSalida.append("</CODPAIS3>");
                        xmlSalida.append("<CODTITU1>");
                            xmlSalida.append(oferta.getCodTit1()!=null?oferta.getCodTit1():"");
                        xmlSalida.append("</CODTITU1>");
                        xmlSalida.append("<CODTITU2>");
                            xmlSalida.append(oferta.getCodTit2()!=null?oferta.getCodTit2():"");
                        xmlSalida.append("</CODTITU2>");
                        xmlSalida.append("<CODTITU3>");
                            xmlSalida.append(oferta.getCodTit3()!=null?oferta.getCodTit3():"");
                        xmlSalida.append("</CODTITU3>");
                        String desc="";
                        xmlSalida.append("<DESCTITU1>");                            
                             desc = MeLanbide46Manager.getInstance().getDescripcionTitulacion(oferta.getCodTit1(), adaptador);
                             xmlSalida.append(oferta.getCodTit1()!=null?desc:"");
                        xmlSalida.append("</DESCTITU1>");
                        xmlSalida.append("<DESCTITU2>");
                            desc = MeLanbide46Manager.getInstance().getDescripcionTitulacion(oferta.getCodTit2(), adaptador);
                            xmlSalida.append(oferta.getCodTit2()!=null?desc:"");
                        xmlSalida.append("</DESCTITU2>");
                        xmlSalida.append("<DESCTITU3>");
                            desc = MeLanbide46Manager.getInstance().getDescripcionTitulacion(oferta.getCodTit3(), adaptador);
                            xmlSalida.append(oferta.getCodTit3()!=null?desc:"");
                        xmlSalida.append("</DESCTITU3>");
                        xmlSalida.append("<FUNCIONES>");
                            xmlSalida.append(oferta.getFunciones()!=null?oferta.getFunciones():"");
                        xmlSalida.append("</FUNCIONES>");
                         xmlSalida.append("<CODIDIOMA1>");
                            xmlSalida.append(oferta.getCodIdioma1()!=null?oferta.getCodIdioma1():"");
                        xmlSalida.append("</CODIDIOMA1>");
                        xmlSalida.append("<CODIDIOMA2>");
                            xmlSalida.append(oferta.getCodIdioma2()!=null?oferta.getCodIdioma2():"");
                        xmlSalida.append("</CODIDIOMA2>");
                        xmlSalida.append("<CODIDIOMA3>");
                            xmlSalida.append(oferta.getCodIdioma3()!=null?oferta.getCodIdioma3():"");
                        xmlSalida.append("</CODIDIOMA3>");
                         xmlSalida.append("<CODNIVIDI1>");
                            xmlSalida.append(oferta.getCodNivIdi1()!=null?oferta.getCodNivIdi1():"");
                        xmlSalida.append("</CODNIVIDI1>");
                        xmlSalida.append("<CODNIVIDI2>");
                            xmlSalida.append(oferta.getCodNivIdi2()!=null?oferta.getCodNivIdi2():"");
                        xmlSalida.append("</CODNIVIDI2>");
                        xmlSalida.append("<CODNIVIDI3>");
                            xmlSalida.append(oferta.getCodNivIdi3()!=null?oferta.getCodNivIdi3():"");
                        xmlSalida.append("</CODNIVIDI3>");
                         xmlSalida.append("<CODNIVFORM>");
                            xmlSalida.append(oferta.getCodNivForm()!=null?oferta.getCodNivForm():"");
                        xmlSalida.append("</CODNIVFORM>");
                        xmlSalida.append("<CIUDAD>");
                            xmlSalida.append(oferta.getCiudadDestino()!=null?oferta.getCiudadDestino():"");
                        xmlSalida.append("</CIUDAD>");
                        xmlSalida.append("<DEPARTAMENTO>");
                            xmlSalida.append(oferta.getDpto()!=null?oferta.getDpto():"");
                        xmlSalida.append("</DEPARTAMENTO>");
                    xmlSalida.append("</RESPUESTA>");
                    try{
                        response.setContentType("text/xml");
                        response.setCharacterEncoding("UTF-8");
                        PrintWriter out = response.getWriter();
                        out.print(xmlSalida.toString());
                        out.flush();
                        out.close();
                    }catch(Exception e){
                        e.printStackTrace();
                    }//try-catch
            
                    
                    
                    /*request.setAttribute("ofertaModif", oferta);
                     request.setAttribute("puestoConsulta", puesto);
                     if(oferta.getIdOfertaOrigen() != null)
                        {
                            CmeOfertaVO ofertaOrigen = new CmeOfertaVO();
                            ofertaOrigen.setIdOferta(oferta.getIdOfertaOrigen());
                            ofertaOrigen.setExpEje(ejercicio);
                            ofertaOrigen.setNumExp(numExpediente);
                            ofertaOrigen = MeLanbide46Manager.getInstance().getOfertaPorCodigoPuestoYExpediente(ofertaOrigen, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                            if(ofertaOrigen != null)
                            {
                                request.setAttribute("ofertaOrigen", ofertaOrigen);
                            }
                        }
                        */
            }
            else
            {
                codigoOperacion = "3";
            }
        }
        catch(Exception ex)
        {
            codigoOperacion = "2";
            ex.printStackTrace();
        }
        //log.debug(request.getAttributeNames());      
        //return "/jsp/extension/melanbide46/nuevaOferta.jsp?codOrganizacionModulo="+codOrganizacion;
    }
     
    public void busquedaTercero(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response)
    {
        String codigoOperacion = "0";
        HashMap mapaResultados = new HashMap();
        try
        {
            String tipoDoc = (String)request.getParameter("tipoDoc");
            String numDoc = (String)request.getParameter("numDoc");
            if(tipoDoc != null && !tipoDoc.equals("") && numDoc != null && !numDoc.equals(""))
            {
                TercerosManager terMan = TercerosManager.getInstance();
                CondicionesBusquedaTerceroVO condsBusq = new CondicionesBusquedaTerceroVO();
                condsBusq.setCodOrganizacion(codOrganizacion);
                log.debug("BusquedaTerceros strTipoDoc: " + tipoDoc);
                condsBusq.setTipoDocumento(Integer.parseInt(tipoDoc));
                condsBusq.setDocumento(numDoc);
                log.debug("BusquedaTerceros txtDNI busqueda: " + request.getParameter("txtDNI"));
                
                HttpSession session = request.getSession();
                UsuarioValueObject usuario = (UsuarioValueObject)session.getAttribute("usuario");
                String[] params = usuario.getParamsCon();
                
                // Llamar al manager.
                mapaResultados = terMan.getTercero(condsBusq, params);
                System.out.println("resultado = "+(mapaResultados != null ? mapaResultados.size() : "null"));
            }
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
            codigoOperacion = "2";
        }
        escribirResultadoBusquedaTercero(codigoOperacion, mapaResultados, response);
    }
    
    private void escribirResultadoBusquedaTercero(String codigoOperacion, HashMap mapaResultados, HttpServletResponse response)
    {
        Vector resultados = (Vector)mapaResultados.get("resultados");
        StringBuffer xmlSalida = new StringBuffer();
        xmlSalida.append("<RESPUESTA>");
            xmlSalida.append("<CODIGO_OPERACION>");
                xmlSalida.append(codigoOperacion);
            xmlSalida.append("</CODIGO_OPERACION>");
        if(resultados != null && resultados.size() > 0)
        {
            xmlSalida.append("<RESULTADOS>");
            TercerosValueObject tercero = null;
            //En local al menos devuelve mas de un resultado, por eso se coge siempre el primero
            //Se podrÃ­a cambiar para que devuelva mÃ¡s de un tercero
            int numResultados = 1;//resultados.size();
            for(int i = 0; i < numResultados; i++)
            {
                xmlSalida.append("<TERCERO>");
                tercero = (TercerosValueObject)resultados.get(i);
                    xmlSalida.append("<APE1>");
                    xmlSalida.append(tercero.getApellido1().toUpperCase());
                    xmlSalida.append("</APE1>");
                    xmlSalida.append("<APE2>");
                    xmlSalida.append(tercero.getApellido2().toUpperCase());
                    xmlSalida.append("</APE2>");
                    xmlSalida.append("<NOMBRE>");
                    xmlSalida.append(tercero.getNombre().toUpperCase());
                    xmlSalida.append("</NOMBRE>");
                    xmlSalida.append("<EMAIL>");
                    xmlSalida.append(tercero.getEmail());
                    xmlSalida.append("</EMAIL>");
                    xmlSalida.append("<TELF>");
                    xmlSalida.append(tercero.getTelefono());
                    xmlSalida.append("</TELF>");
                xmlSalida.append("</TERCERO>");
            }
            xmlSalida.append("</RESULTADOS>");
        }
        List errores = (List)mapaResultados.get("errores");
        if(errores != null && errores.size() > 0)
        {
            xmlSalida.append("<ERRORES>");
            String error = null;
            for(int i = 0; i < errores.size(); i++)
            {
                error = (String)errores.get(i);
                xmlSalida.append("<ERROR>");
                    xmlSalida.append(error);
                xmlSalida.append("</ERROR>");
            }
            xmlSalida.append("</ERRORES>");
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
            e.printStackTrace();
        }//try-catch
    }
    
    public String cargarDatosPersonaContratada(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response)
    {
        try
        {
            String opcion = (String)request.getParameter("opcion");
            if(opcion != null)
            {
                String idOferta = (String)request.getParameter("idOferta");
                String idPuesto = (String)request.getParameter("idPuesto");
                String idJustif = (String)request.getParameter("idJustif");

                cargarCombosPersonaContratada(codOrganizacion, request);

                if(idOferta != null && !idOferta.equals("") && idPuesto != null && !idPuesto.equals(""))
                {
                    Integer ejercicio = MeLanbide46Utils.getEjercicioDeExpediente(numExpediente);
                    AdaptadorSQLBD adaptador = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
                    CmePuestoVO puesto = new CmePuestoVO();
                    puesto.setCodPuesto(idPuesto);
                    puesto.setEjercicio(ejercicio);
                    puesto.setNumExp(numExpediente);
                    puesto = MeLanbide46Manager.getInstance().getPuestoPorCodigoYExpediente(puesto, adaptador);
                    if(puesto != null)
                    {
                        request.setAttribute("puestoConsulta", puesto);
                    }
                    
                    CmeOfertaVO oferta = new CmeOfertaVO();
                    oferta.setIdOferta(Integer.parseInt(idOferta));
                    oferta.setCodPuesto(idPuesto);
                    oferta.setExpEje(ejercicio);
                    oferta.setNumExp(numExpediente);
                    oferta = MeLanbide46Manager.getInstance().getOfertaPorCodigoPuestoYExpediente(oferta, adaptador);
                    if(oferta != null)
                    {
                        request.setAttribute("ofertaConsulta", oferta);
                        String desc = null;
                        if(oferta.getCodTitulacion() != null && !oferta.getCodTitulacion().equals(""))
                        {
                            desc = MeLanbide46Manager.getInstance().getDescripcionTitulacion( oferta.getCodTitulacion(), adaptador);
                            if(desc != null)
                            {
                                request.setAttribute("descTitContratado", desc);
                            }
                        }
                        
                        
                    }
                    
                    CmeJustificacionVO justifModif = new CmeJustificacionVO();
                    justifModif.setCodPuesto(idPuesto);
                    justifModif.setEjercicio(ejercicio);
                    justifModif.setIdJustificacion(Integer.parseInt(idJustif));
                    justifModif.setIdOferta(Integer.parseInt(idOferta));
                    justifModif.setNumExpediente(numExpediente);
                    justifModif = MeLanbide46Manager.getInstance().getJustificacionPorCodigoPuestoYExpediente(justifModif, adaptador);
                    
                    int dias=0;
                    //Leire, cálculo de días
                    //String rdo = MeLanbide46Manager.getInstance().obtenerDiasTrabajados(numExpediente, oferta.getNif(), adaptador);
                    String rdo = MeLanbide46Manager.getInstance().obtenerDiasTrabajados(numExpediente, idPuesto, Integer.parseInt(idOferta), adaptador);
                    if(rdo != null && !rdo.equals(""))
                        dias = Integer.parseInt(rdo);
                    else
                        dias = 0;
                    if(justifModif.getDiasTrab() == null)
                    {
                        justifModif.setDiasTrab(dias);
                    }else if(justifModif.getDiasTrab() != null && justifModif.getDiasTrab().equals(0))
                        justifModif.setDiasTrab(dias);
                    if(justifModif.getDiasSegSoc() == null)
                    {
                        justifModif.setDiasSegSoc(dias);
                    }
                    
                    //if(justifModif != null)
                    //{
                        request.setAttribute("justifModif", justifModif);
                    //}
                    
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
        return "/jsp/extension/melanbide46/datosPersonaContratada.jsp";
    }
    
    public void buscarTitulaciones(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response)
    {
        System.out.println("ORGA = "+codOrganizacion);
        System.out.println("NUMEXP = "+numExpediente);
        
        List<FilaResultadoBusqTitulaciones> listaTitulaciones = new ArrayList<FilaResultadoBusqTitulaciones>();
        String codigoOperacion = "0";
        try
        {
            String codigo = (String)request.getParameter("codigo");
            String desc = (String)request.getParameter("desc");
            
            AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
            listaTitulaciones = MeLanbide46Manager.getInstance().buscarTitulaciones(codigo, desc, adapt);
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
            
        if(listaTitulaciones != null && !listaTitulaciones.isEmpty())
        {
            xmlSalida.append("<TITULACIONES>");
            
            for(FilaResultadoBusqTitulaciones fila : listaTitulaciones)
            {
                xmlSalida.append("<TITULACION>");
                    xmlSalida.append("<CODIGO_INTERNO>");
                        xmlSalida.append(fila.getCodigoInterno());
                    xmlSalida.append("</CODIGO_INTERNO>");
                    
                    xmlSalida.append("<CODIGO_VISIBLE>");
                        xmlSalida.append(fila.getCodigoVisible().toUpperCase());
                    xmlSalida.append("</CODIGO_VISIBLE>");

                    xmlSalida.append("<DESCRIPCION>");
                        xmlSalida.append(fila.getDescripcion().toUpperCase());
                    xmlSalida.append("</DESCRIPCION>");
                xmlSalida.append("</TITULACION>");
            }
            
            xmlSalida.append("</TITULACIONES>");
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
            e.printStackTrace();
        }//try-catch
    }
    
    public void getTodosCamposSuplementarios(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response)
    {
        Integer ejercicio = null;
        try
        {
            ejercicio = MeLanbide46Utils.getEjercicioDeExpediente(numExpediente);
        }
        catch(Exception ex)
        {

        }
        Map<String, BigDecimal> importes = null;
        Map<String, Integer> puestos = null;
        String gestor = null;
        String empresa = null;
        datosCmeVO datos = new datosCmeVO();
            
        try
        {
            AdaptadorSQLBD adaptador = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
            importes = MeLanbide46Manager.getInstance().cargarCalculosCme(codOrganizacion, ejercicio, numExpediente, adaptador);
            puestos = MeLanbide46Manager.getInstance().cargarCalculosPuestos(codOrganizacion, ejercicio, numExpediente, adaptador);
        
            //Cargo el gestor tramitador, empresa e importes de la subvencion (camposSuplementarios)
            gestor = MeLanbide46Manager.getInstance().getValorCampoDesplegable(codOrganizacion, String.valueOf(ejercicio), numExpediente, ConfigurationParameter.getParameter(ConstantesMeLanbide46.CAMPO_SUPL_GESTOR_TRAMITADOR, ConstantesMeLanbide46.FICHERO_PROPIEDADES), adaptador);
        
            empresa = MeLanbide46Manager.getInstance().getValorCampoTexto(codOrganizacion, String.valueOf(ejercicio), numExpediente, ConfigurationParameter.getParameter(ConstantesMeLanbide46.CAMPO_SUPL_EMPRESA, ConstantesMeLanbide46.FICHERO_PROPIEDADES), adaptador);
            
            datos = MeLanbide46Manager.getInstance().obtenerDatosCme(codOrganizacion,numExpediente,adaptador);
        
            StringBuffer xmlSalida = new StringBuffer();
            
            xmlSalida.append("<RESPUESTA>");
            xmlSalida.append("<CODIGO_OPERACION>");
            xmlSalida.append("0");
            xmlSalida.append("</CODIGO_OPERACION>");
            
            if(importes != null && !importes.isEmpty())
            {
                xmlSalida.append("<IMPORTES>");

                xmlSalida.append("<IMP_SOL>");
                if(importes.containsKey(ConstantesMeLanbide46.CAMPO_SUPL_IMP_SOLICITADO))
                {
                    BigDecimal bd = importes.get(ConstantesMeLanbide46.CAMPO_SUPL_IMP_SOLICITADO);
                    xmlSalida.append(bd.toPlainString().replaceAll("\\.", ","));
                }
                xmlSalida.append("</IMP_SOL>");

                xmlSalida.append("<IMP_CONV>");
                if(importes.containsKey(ConstantesMeLanbide46.CAMPO_SUPL_IMP_CONVOCATORIA))
                {
                    BigDecimal bd = importes.get(ConstantesMeLanbide46.CAMPO_SUPL_IMP_CONVOCATORIA);
                    xmlSalida.append(bd.toPlainString().replaceAll("\\.", ","));
                }
                xmlSalida.append("</IMP_CONV>");

                xmlSalida.append("<IMP_PREV_CON>");
                if(importes.containsKey(ConstantesMeLanbide46.CAMPO_SUPL_IMP_PREV_CON))
                {
                    BigDecimal bd = importes.get(ConstantesMeLanbide46.CAMPO_SUPL_IMP_PREV_CON);
                    xmlSalida.append(bd.toPlainString().replaceAll("\\.", ","));
                }
                xmlSalida.append("</IMP_PREV_CON>");

                xmlSalida.append("<IMP_CON>");
                if(importes.containsKey(ConstantesMeLanbide46.CAMPO_SUPL_IMP_CON))
                {
                    BigDecimal bd = importes.get(ConstantesMeLanbide46.CAMPO_SUPL_IMP_CON);
                    xmlSalida.append(bd.toPlainString().replaceAll("\\.", ","));
                }
                xmlSalida.append("</IMP_CON>");

                xmlSalida.append("<IMP_JUS>");
//                if(importes.containsKey(ConstantesMeLanbide46.CAMPO_SUPL_IMP_JUS))
//                {
//                    BigDecimal bd = importes.get(ConstantesMeLanbide46.CAMPO_SUPL_IMP_JUS);
//                    xmlSalida.append(bd.toPlainString().replaceAll("\\.", ","));
//                }
                xmlSalida.append(datos.getImpJustificado().replaceAll("\\.", ","));
                xmlSalida.append("</IMP_JUS>");
                
                xmlSalida.append("<IMP_REN_RES>");
                if(importes.containsKey(ConstantesMeLanbide46.CAMPO_SUPL_IMP_REN_RES))
                {
                    BigDecimal bd = importes.get(ConstantesMeLanbide46.CAMPO_SUPL_IMP_REN_RES);
                    xmlSalida.append(bd.toPlainString().replaceAll("\\.", ","));
                }
                xmlSalida.append("</IMP_REN_RES>");

                xmlSalida.append("<IMP_REN>");
                if(importes.containsKey(ConstantesMeLanbide46.CAMPO_SUPL_IMP_REN))
                {
                    BigDecimal bd = importes.get(ConstantesMeLanbide46.CAMPO_SUPL_IMP_REN);
                    xmlSalida.append(bd.toPlainString().replaceAll("\\.", ","));
                }
                xmlSalida.append("</IMP_REN>");

                xmlSalida.append("<IMP_PAG>");
                if(importes.containsKey(ConstantesMeLanbide46.CAMPO_SUPL_IMP_PAG))
                {
                    BigDecimal bd = importes.get(ConstantesMeLanbide46.CAMPO_SUPL_IMP_PAG);
                    xmlSalida.append(bd.toPlainString().replaceAll("\\.", ","));
                }
                xmlSalida.append("</IMP_PAG>");

                xmlSalida.append("<IMP_PAG_2>");
                if(importes.containsKey(ConstantesMeLanbide46.CAMPO_SUPL_IMP_PAG_2))
                {
                    BigDecimal bd = importes.get(ConstantesMeLanbide46.CAMPO_SUPL_IMP_PAG_2);
                    xmlSalida.append(bd.toPlainString().replaceAll("\\.", ","));
                }
                xmlSalida.append("</IMP_PAG_2>");

                xmlSalida.append("<IMP_REI>");
//                if(importes.containsKey(ConstantesMeLanbide46.CAMPO_SUPL_IMP_REI))
//                {
//                    BigDecimal bd = importes.get(ConstantesMeLanbide46.CAMPO_SUPL_IMP_REI);
//                    xmlSalida.append(bd.toPlainString().replaceAll("\\.", ","));
//                }
                xmlSalida.append(datos.getReintegro().replaceAll("\\.", ","));
                xmlSalida.append("</IMP_REI>");

                xmlSalida.append("<OTRAS_AYUDAS_SOLIC>");
                if(importes.containsKey(ConstantesMeLanbide46.CAMPO_SUPL_OTRAS_AYUDAS_SOLIC))
                {
                    BigDecimal bd = importes.get(ConstantesMeLanbide46.CAMPO_SUPL_OTRAS_AYUDAS_SOLIC);
                    xmlSalida.append(bd.toPlainString().replaceAll("\\.", ","));
                }
                xmlSalida.append("</OTRAS_AYUDAS_SOLIC>");

                xmlSalida.append("<OTRAS_AYUDAS_CONCE>");
                if(importes.containsKey(ConstantesMeLanbide46.CAMPO_SUPL_OTRAS_AYUDAS_CONCE))
                {
                    BigDecimal bd = importes.get(ConstantesMeLanbide46.CAMPO_SUPL_OTRAS_AYUDAS_CONCE);
                    xmlSalida.append(bd.toPlainString().replaceAll("\\.", ","));
                }
                xmlSalida.append("</OTRAS_AYUDAS_CONCE>");

                xmlSalida.append("<MINIMIS_SOLIC>");
                if(importes.containsKey(ConstantesMeLanbide46.CAMPO_SUPL_MINIMIS_SOLIC))
                {
                    BigDecimal bd = importes.get(ConstantesMeLanbide46.CAMPO_SUPL_MINIMIS_SOLIC);
                    xmlSalida.append(bd.toPlainString().replaceAll("\\.", ","));
                }
                xmlSalida.append("</MINIMIS_SOLIC>");

                xmlSalida.append("<MINIMIS_CONCE>");
                if(importes.containsKey(ConstantesMeLanbide46.CAMPO_SUPL_MINIMIS_CONCE))
                {
                    BigDecimal bd = importes.get(ConstantesMeLanbide46.CAMPO_SUPL_MINIMIS_CONCE);
                    xmlSalida.append(bd.toPlainString().replaceAll("\\.", ","));
                }
                xmlSalida.append("</MINIMIS_CONCE>");

                xmlSalida.append("<IMP_DESP>");
                if(importes.containsKey(ConstantesMeLanbide46.CAMPO_SUPL_IMP_REN))
                {
                    BigDecimal bd = importes.get(ConstantesMeLanbide46.CAMPO_SUPL_IMP_DESP);
                    xmlSalida.append(bd.toPlainString().replaceAll("\\.", ","));
                }
                xmlSalida.append("</IMP_DESP>");

                xmlSalida.append("<IMP_BAJA>");
                if(importes.containsKey(ConstantesMeLanbide46.CAMPO_SUPL_IMP_BAJA))
                {
                    BigDecimal bd = importes.get(ConstantesMeLanbide46.CAMPO_SUPL_IMP_BAJA);
                    xmlSalida.append(bd.toPlainString().replaceAll("\\.", ","));
                }
                xmlSalida.append("</IMP_BAJA>");

                xmlSalida.append("<IMP_BONIF>");
                if(importes.containsKey(ConstantesMeLanbide46.CAMPO_SUPL_IMP_BONIF))
                {
                    BigDecimal bd = importes.get(ConstantesMeLanbide46.CAMPO_SUPL_IMP_BONIF);
                    xmlSalida.append(bd.toPlainString().replaceAll("\\.", ","));
                }
                xmlSalida.append("</IMP_BONIF>");
                xmlSalida.append("</IMPORTES>");

                xmlSalida.append("<PUESTOS>");
            
                xmlSalida.append("<PUESTOS_SOLICITADOS>");
                if(puestos.containsKey(ConstantesMeLanbide46.CAMPO_SUPL_PUESTOS_SOLICITADOS))
                {
                    Integer i = puestos.get(ConstantesMeLanbide46.CAMPO_SUPL_PUESTOS_SOLICITADOS);
                    xmlSalida.append(i.toString());
                }
                xmlSalida.append("</PUESTOS_SOLICITADOS>");
            
                xmlSalida.append("<PUESTOS_DENEGADOS>");
                if(puestos.containsKey(ConstantesMeLanbide46.CAMPO_SUPL_PUESTOS_DENEGADOS))
                {
                    Integer i = puestos.get(ConstantesMeLanbide46.CAMPO_SUPL_PUESTOS_DENEGADOS);
                    xmlSalida.append(i.toString());
                }
                xmlSalida.append("</PUESTOS_DENEGADOS>");
            
                xmlSalida.append("<PUESTOS_CONTRATADOS>");
                if(puestos.containsKey(ConstantesMeLanbide46.CAMPO_SUPL_PUESTOS_CONTRATADOS))
                {
                    Integer i = puestos.get(ConstantesMeLanbide46.CAMPO_SUPL_PUESTOS_CONTRATADOS);
                    xmlSalida.append(i.toString());
                }
                xmlSalida.append("</PUESTOS_CONTRATADOS>");
            
                xmlSalida.append("<PERSONAS_CONTRATADAS>");
                if(puestos.containsKey(ConstantesMeLanbide46.CAMPO_SUPL_PERSONAS_CONTRATADAS))
                {
                    Integer i = puestos.get(ConstantesMeLanbide46.CAMPO_SUPL_PERSONAS_CONTRATADAS);
                    xmlSalida.append(i.toString());
                }
                xmlSalida.append("</PERSONAS_CONTRATADAS>");
            
                xmlSalida.append("<PUESTOS_DESPIDO>");
                if(puestos.containsKey(ConstantesMeLanbide46.CAMPO_SUPL_PUESTOS_DESPIDO))
                {
                    Integer i = puestos.get(ConstantesMeLanbide46.CAMPO_SUPL_PUESTOS_DESPIDO);
                    xmlSalida.append(i.toString());
                }
                xmlSalida.append("</PUESTOS_DESPIDO>");
            
                xmlSalida.append("<PERSONAS_DESPIDO>");
                if(puestos.containsKey(ConstantesMeLanbide46.CAMPO_SUPL_PERSONAS_DESPIDO))
                {
                    Integer i = puestos.get(ConstantesMeLanbide46.CAMPO_SUPL_PERSONAS_DESPIDO);
                    xmlSalida.append(i.toString());
                }
                xmlSalida.append("</PERSONAS_DESPIDO>");
            
                xmlSalida.append("<PUESTOS_BAJA>");
                if(puestos.containsKey(ConstantesMeLanbide46.CAMPO_SUPL_PUESTOS_BAJA))
                {
                    Integer i = puestos.get(ConstantesMeLanbide46.CAMPO_SUPL_PUESTOS_BAJA);
                    xmlSalida.append(i.toString());
                }
                xmlSalida.append("</PUESTOS_BAJA>");
            
                xmlSalida.append("<PERSONAS_BAJA>");
                if(puestos.containsKey(ConstantesMeLanbide46.CAMPO_SUPL_PERSONAS_BAJA))
                {
                    Integer i = puestos.get(ConstantesMeLanbide46.CAMPO_SUPL_PERSONAS_BAJA);
                    xmlSalida.append(i.toString());
                }
                xmlSalida.append("</PERSONAS_BAJA>");
            
                xmlSalida.append("<PUESTOS_RENUNCIADOS>");
                if(puestos.containsKey(ConstantesMeLanbide46.CAMPO_SUPL_PUESTOS_RENUNCIADOS))
                {
                    Integer i = puestos.get(ConstantesMeLanbide46.CAMPO_SUPL_PUESTOS_RENUNCIADOS);
                    xmlSalida.append(i.toString());
                }
                xmlSalida.append("</PUESTOS_RENUNCIADOS>");
                
                xmlSalida.append("</PUESTOS>");
            
                xmlSalida.append("<OTROS>");
            
                xmlSalida.append("<GESTOR_TRAMITADOR>");
                if(gestor != null)
                {
                    xmlSalida.append(gestor.toUpperCase());
                }
                xmlSalida.append("</GESTOR_TRAMITADOR>");
            
                xmlSalida.append("<EMPRESA>");
                if(empresa != null)
                {
                    xmlSalida.append(empresa.toUpperCase());
                }
                xmlSalida.append("</EMPRESA>");
                
                xmlSalida.append("</OTROS>");
            }
            xmlSalida.append("</RESPUESTA>");
                
            response.setContentType("text/xml");
            response.setCharacterEncoding("UTF-8");
            PrintWriter out = response.getWriter();
            out.print(xmlSalida.toString());
            out.flush();
            out.close();
        }catch(Exception e){
            e.printStackTrace();
        }//try-catch
    }
    
     public void grabarImportesResolucionCme(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente) throws Exception
     {
        Integer ejercicio = MeLanbide46Utils.getEjercicioDeExpediente(numExpediente);
        MeLanbide46Manager.getInstance().grabarImportesResolucion(codOrganizacion, ejercicio, numExpediente, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
     }
     
     public void grabarImportesResolucionModifCme(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente) throws Exception
     {
        Integer ejercicio = MeLanbide46Utils.getEjercicioDeExpediente(numExpediente);
        MeLanbide46Manager.getInstance().grabarImportesResolucionModif(codOrganizacion, ejercicio, numExpediente, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
     }
     
     public String cargarImportesResolucion(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response)
     {
         Integer ejercicio = null;
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
            ejercicio = MeLanbide46Utils.getEjercicioDeExpediente(numExpediente);
            String res = (String)request.getParameter("res");
            if(ejercicio != null)
            {
                if(res != null && !res.equals(""))
                {
                    request.setAttribute("res", res);
                    AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
                    HashMap<String, Object> valores = MeLanbide46Manager.getInstance().cargarImportesResolucion(codOrganizacion, ejercicio, numExpediente, res, adapt);
                    Iterator<String> it = valores.keySet().iterator();
                    String key = null;
                    Object valor = null;
                    String str = null;
                    SimpleDateFormat format = new SimpleDateFormat(ConstantesMeLanbide46.FORMATO_FECHA);
                    while(it.hasNext())
                    {
                        key = it.next();
                        valor = valores.get(key);
                        if(valor instanceof BigDecimal)
                        {
                            str = MeLanbide46Utils.redondearDecimalesString((BigDecimal)valor, 2);
                        }
                        else if(valor instanceof Date)
                        {
                            str = format.format((Date)valor);
                        }
                        request.setAttribute(key, str);
                    }
                }
                else
                {
                    request.setAttribute("error", MeLanbide46I18n.getInstance().getMensaje(codIdioma, "error.datosRes.resNoDeterminada"));
                }
            }
            else
            {
                request.setAttribute("error", MeLanbide46I18n.getInstance().getMensaje(codIdioma, "error.datosRes.ejercicioNoDeterminado"));
            }
         }
         catch(Exception ex)
         {
            
         }
         return "/jsp/extension/melanbide46/datosResolucion.jsp";
     }
     
     public String cargarHistorico(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response)
     {
         //cargarDatosCme(codOrganizacion, numExpediente, request);
        
        //Cargo los datos de las pestaÃ±as
        AdaptadorSQLBD adapt = null;
        String codTra ="";
        String url = null;
        try
        {
            adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
            if(request != null && request.getParameter("tram") != null)
                codTra = request.getParameter("tram").toString();
            
            if (request !=null){
                url = cargarSubpestanaSolicitudHist_DatosCme(codOrganizacion, numExpediente, codTra, adapt, request);
                if(url != null)
                {
                    request.setAttribute("urlPestanaDatos_solicitud", url);
                }

                url = cargarSubpestanaOfertaHist_DatosCme(codOrganizacion, numExpediente, codTra, adapt, request);
                if(url != null)
                {
                    request.setAttribute("urlPestanaDatos_oferta", url);
                }

                url = cargarSubpestanaJustificacionHist_DatosCme(codOrganizacion, numExpediente, codTra, adapt, request);
                if(url != null)
                {
                    request.setAttribute("urlPestanaDatos_justif", url);
                }
            }
        }
        catch(Exception ex)
        {

        }
        return "/jsp/extension/melanbide46/datosCmeHist.jsp";
     }
    
    public String refrescarDatosCme(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente)
    {
        return "0";
    }
     
    /**
     * OperaciÃ³n que recupera los datos de conexiÃ³n a la BBDD
     * @param codOrganizacion
     * @return AdaptadorSQLBD
     */
    private AdaptadorSQLBD getAdaptSQLBD(String codOrganizacion) throws SQLException{
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
                // ConexiÃ³n al esquema genÃ©rico
                conGenerico = ds.getConnection();

                String sql = "SELECT EEA_BDE FROM A_EEA WHERE EEA_APL=" + ConstantesDatos.APP_GESTION_EXPEDIENTES + " AND AAE_ORG=" + codOrganizacion;
                st = conGenerico.createStatement();
                rs = st.executeQuery(sql);
                String jndi = null;
                while(rs.next()){
                    jndi = rs.getString("EEA_BDE");
                }//while(rs.next())

                //st.close();
                //rs.close();
                //conGenerico.close();

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
                te.printStackTrace();
                log.error("*** AdaptadorSQLBD: " + te.toString());
            }catch(SQLException e){
                e.printStackTrace();
            }finally{
                if(st!=null) st.close();
                if(rs!=null) rs.close();
                if(conGenerico!=null && !conGenerico.isClosed())
                conGenerico.close();
            
            }// finally
            if(log.isDebugEnabled()) log.debug("getConnection() : END");
         }// synchronized
        return adapt;
     }//getConnection
    
    
    public void generarInformeResumenEconomico2016(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response)
    {
        String rutaArchivoSalida = null;
        try
        {
            Integer ano = Integer.parseInt(request.getParameter("ano"));
            AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
            
            int idioma = 1;
            HttpSession session = request.getSession();
            UsuarioValueObject usuario = new UsuarioValueObject();
            try
            {
                if (session != null) 
                {
                    usuario = (UsuarioValueObject) session.getAttribute("usuario");
                    if (usuario != null) 
                    {
                        idioma = usuario.getIdioma();
                    }
                }
            }
            catch(Exception ex)
            {

            }
            
            MeLanbide46I18n meLanbide46I18n = MeLanbide46I18n.getInstance();
            MeLanbide46Manager manager = MeLanbide46Manager.getInstance();
            
            try 
            {
                HSSFWorkbook libro = new HSSFWorkbook();
            
                //se escribe el fichero
                DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
                //get current date time with Date()
                Date date = new Date();
                String fechaAct=dateFormat.format(date);
           
                File informe= new File("resumenEconomico_"+fechaAct+".xls");

                FileOutputStream archivoSalida = new FileOutputStream(informe);
                    
                HSSFPalette palette = libro.getCustomPalette();
                HSSFColor hssfColor = null;
                try 
                {
                    hssfColor= palette.findColor((byte)75, (byte)149, (byte)211); 
                    if (hssfColor == null )
                    {
                        hssfColor = palette.getColor(HSSFColor.ROYAL_BLUE.index);
                    }
                }
                catch (Exception e) 
                {
                    e.printStackTrace();
                }
                
                HSSFFont negrita = libro.createFont();
                negrita.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
                negrita.setFontHeight((short)150);
                
                HSSFFont normal = libro.createFont();
                normal.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
                normal.setFontHeight((short)150);
            
                HSSFFont negritaTitulo = libro.createFont();
                negritaTitulo.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
                negritaTitulo.setColor(HSSFColor.WHITE.index);
                negritaTitulo.setFontHeight((short)170);
            
                HSSFSheet hoja = null;                
                
                int numFila = 0;
                int numFila2 = 0;

                HSSFRow fila = null;
                HSSFCell celda = null;
                HSSFCellStyle estiloCelda = null;
                
                HSSFRow fila2 = null;
                HSSFCell celda2 = null;
                HSSFCellStyle estiloCelda2 = null;

                hoja = libro.createSheet();
                HSSFSheet hoja2 = libro.createSheet("sin formato");

                //Se establece el ancho de cada columnas
                hoja.setColumnWidth(0, 6000);//Num Expediente
                hoja.setColumnWidth(1, 10000);//Entidad
                hoja.setColumnWidth(2, 8000);//Territorio historico
                hoja.setColumnWidth(3, 4000);//CNAE
                hoja.setColumnWidth(4, 5000);//Num puestos solic
                hoja.setColumnWidth(5, 10000);//puesto
                hoja.setColumnWidth(6, 6000);//resultado
                hoja.setColumnWidth(7, 5000);//fecha inicio
                hoja.setColumnWidth(8, 5000);//fecha fin
                hoja.setColumnWidth(9, 4000);//periodo meses
                hoja.setColumnWidth(10, 5000);//rango edad
                hoja.setColumnWidth(11, 8000);//pais
                hoja.setColumnWidth(12, 5000);//costes contrato salario + ss
                hoja.setColumnWidth(13, 5000);//dietas convenio
                hoja.setColumnWidth(14, 5000);//coste de la contratacion
                hoja.setColumnWidth(15, 5000);//salario anual bruto
                hoja.setColumnWidth(16, 5000);//dietas alojamiento y manutencion
                hoja.setColumnWidth(17, 5000);//tramitacion de permisos y seguros
                hoja.setColumnWidth(18, 5000);//importe total solicitado
                hoja.setColumnWidth(19, 5000);//dotacion anual
                hoja.setColumnWidth(20, 5000);//meses subvencionables
                hoja.setColumnWidth(21, 5000);//dietas subvencionables
                hoja.setColumnWidth(22, 5000);//tramitacion de permisos y seguros
                hoja.setColumnWidth(23, 2700);//minimis
                hoja.setColumnWidth(24, 4000);//importe maximo subvencionable
                hoja.setColumnWidth(25, 3500);//importe concedido
                hoja.setColumnWidth(26, 4500);//num renuncias
                hoja.setColumnWidth(27, 3000);//importe renuncia
                hoja.setColumnWidth(28, 5000);//motivo renuncia
                hoja.setColumnWidth(29, 5000);//importe concedido tras res modif
                hoja.setColumnWidth(30, 5000);//importe concedido tras res modif
                
                
                //Se establece el ancho de cada columnas
                hoja2.setColumnWidth(0, 6000);//Num Expediente
                hoja2.setColumnWidth(1, 10000);//Entidad
                hoja2.setColumnWidth(2, 8000);//Territorio historico
                hoja2.setColumnWidth(3, 4000);//CNAE
                hoja2.setColumnWidth(4, 5000);//Num puestos solic
                hoja2.setColumnWidth(5, 10000);//puesto
                hoja2.setColumnWidth(6, 6000);//resultado
                hoja2.setColumnWidth(7, 5000);//fecha inicio
                hoja2.setColumnWidth(8, 5000);//fecha fin
                hoja2.setColumnWidth(9, 4000);//periodo meses
                hoja2.setColumnWidth(10, 5000);//rango edad
                hoja2.setColumnWidth(11, 8000);//pais
                hoja2.setColumnWidth(12, 5000);//costes contrato salario + ss
                hoja2.setColumnWidth(13, 5000);//dietas convenio
                hoja2.setColumnWidth(14, 5000);//coste de la contratacion
                hoja2.setColumnWidth(15, 5000);//salario anual bruto
                hoja2.setColumnWidth(16, 5000);//dietas alojamiento y manutencion
                hoja2.setColumnWidth(17, 5000);//tramitacion de permisos y seguros
                hoja2.setColumnWidth(18, 5000);//importe total solicitado
                hoja2.setColumnWidth(19, 5000);//dotacion anual
                hoja2.setColumnWidth(20, 5000);//meses subvencionables
                hoja2.setColumnWidth(21, 5000);//dietas subvencionables
                hoja2.setColumnWidth(22, 5000);//tramitacion de permisos y seguros
                hoja2.setColumnWidth(23, 2700);//minimis
                hoja2.setColumnWidth(24, 4000);//importe maximo subvencionable
                hoja2.setColumnWidth(25, 3500);//importe concedido
                hoja2.setColumnWidth(26, 4500);//num renuncias
                hoja2.setColumnWidth(27, 3000);//importe renuncia
                hoja2.setColumnWidth(28, 5000);//motivo renuncia
                hoja2.setColumnWidth(29, 5000);//importe concedido tras res modif
                hoja2.setColumnWidth(30, 5000);//importe concedido tras res modif

                //Se añaden los titulos de las columnas
                fila = hoja.createRow(numFila);
                fila2 = hoja2.createRow(numFila2);

                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(0);
                celda.setCellValue(meLanbide46I18n.getMensaje(idioma, "doc.informeEconomico2016.col1").toUpperCase());
                celda.setCellStyle(estiloCelda);

                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(1);
                celda.setCellValue(meLanbide46I18n.getMensaje(idioma, "doc.informeEconomico2016.col2").toUpperCase());
                celda.setCellStyle(estiloCelda);

                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(2);
                celda.setCellValue(meLanbide46I18n.getMensaje(idioma, "doc.informeEconomico2016.col31").toUpperCase());
                celda.setCellStyle(estiloCelda);

                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(3);
                celda.setCellValue(meLanbide46I18n.getMensaje(idioma, "doc.informeEconomico2016.col3").toUpperCase());
                celda.setCellStyle(estiloCelda);

                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(4);
                celda.setCellValue(meLanbide46I18n.getMensaje(idioma, "doc.informeEconomico2016.col4").toUpperCase());
                celda.setCellStyle(estiloCelda);

                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(5);
                celda.setCellValue(meLanbide46I18n.getMensaje(idioma, "doc.informeEconomico2016.col5").toUpperCase());
                celda.setCellStyle(estiloCelda);

                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(6);
                celda.setCellValue(meLanbide46I18n.getMensaje(idioma, "doc.informeEconomico2016.col6").toUpperCase());
                celda.setCellStyle(estiloCelda);

                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(7);
                celda.setCellValue(meLanbide46I18n.getMensaje(idioma, "doc.informeEconomico2016.col7").toUpperCase());
                celda.setCellStyle(estiloCelda);

                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(8);
                celda.setCellValue(meLanbide46I18n.getMensaje(idioma, "doc.informeEconomico2016.col8").toUpperCase());
                celda.setCellStyle(estiloCelda);

                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(9);
                celda.setCellValue(meLanbide46I18n.getMensaje(idioma, "doc.informeEconomico2016.col9").toUpperCase());
                celda.setCellStyle(estiloCelda);

                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(10);
                celda.setCellValue(meLanbide46I18n.getMensaje(idioma, "doc.informeEconomico2016.col10").toUpperCase());
                celda.setCellStyle(estiloCelda);

                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(11);
                celda.setCellValue(meLanbide46I18n.getMensaje(idioma, "doc.informeEconomico2016.col11").toUpperCase());
                celda.setCellStyle(estiloCelda);
                
                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(12);
                celda.setCellValue(meLanbide46I18n.getMensaje(idioma, "doc.informeEconomico2016.col12").toUpperCase());
                celda.setCellStyle(estiloCelda);

                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(13);
                celda.setCellValue(meLanbide46I18n.getMensaje(idioma, "doc.informeEconomico2016.col13").toUpperCase());
                celda.setCellStyle(estiloCelda);

                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(14);
                celda.setCellValue(meLanbide46I18n.getMensaje(idioma, "doc.informeEconomico2016.col14").toUpperCase());
                celda.setCellStyle(estiloCelda);

                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(15);
                celda.setCellValue(meLanbide46I18n.getMensaje(idioma, "doc.informeEconomico2016.col15").toUpperCase());
                celda.setCellStyle(estiloCelda);

                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(16);
                celda.setCellValue(meLanbide46I18n.getMensaje(idioma, "doc.informeEconomico2016.col16").toUpperCase());
                celda.setCellStyle(estiloCelda);

                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(17);
                celda.setCellValue(meLanbide46I18n.getMensaje(idioma, "doc.informeEconomico2016.col17").toUpperCase());
                celda.setCellStyle(estiloCelda);

                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(18);
                celda.setCellValue(meLanbide46I18n.getMensaje(idioma, "doc.informeEconomico2016.col18").toUpperCase());
                celda.setCellStyle(estiloCelda);

                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(19);
                celda.setCellValue(meLanbide46I18n.getMensaje(idioma, "doc.informeEconomico2016.col19").toUpperCase());
                celda.setCellStyle(estiloCelda);

                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(20);
                celda.setCellValue(meLanbide46I18n.getMensaje(idioma, "doc.informeEconomico2016.col20").toUpperCase());
                celda.setCellStyle(estiloCelda);

                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(21);
                celda.setCellValue(meLanbide46I18n.getMensaje(idioma, "doc.informeEconomico2016.col21").toUpperCase());
                celda.setCellStyle(estiloCelda);
                
                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(22);
                celda.setCellValue(meLanbide46I18n.getMensaje(idioma, "doc.informeEconomico2016.col22").toUpperCase());
                celda.setCellStyle(estiloCelda);

                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(23);
                celda.setCellValue(meLanbide46I18n.getMensaje(idioma, "doc.informeEconomico2016.col23").toUpperCase());
                celda.setCellStyle(estiloCelda);

                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(24);
                celda.setCellValue(meLanbide46I18n.getMensaje(idioma, "doc.informeEconomico2016.col24").toUpperCase());
                celda.setCellStyle(estiloCelda);

                
                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderRight(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(25);
                celda.setCellValue(meLanbide46I18n.getMensaje(idioma, "doc.informeEconomico2016.col25").toUpperCase());
                celda.setCellStyle(estiloCelda);
                
                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderRight(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(26);
                celda.setCellValue(meLanbide46I18n.getMensaje(idioma, "doc.informeEconomico2016.col26").toUpperCase());
                celda.setCellStyle(estiloCelda);
                
                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderRight(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(27);
                celda.setCellValue(meLanbide46I18n.getMensaje(idioma, "doc.informeEconomico2016.col27").toUpperCase());
                celda.setCellStyle(estiloCelda);
                
                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderRight(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(28);
                celda.setCellValue(meLanbide46I18n.getMensaje(idioma, "doc.informeEconomico2016.col28").toUpperCase());
                celda.setCellStyle(estiloCelda);
                
                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderRight(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(29);
                celda.setCellValue(meLanbide46I18n.getMensaje(idioma, "doc.informeEconomico2016.col29").toUpperCase());
                celda.setCellStyle(estiloCelda);
                
                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderRight(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(30);
                celda.setCellValue(meLanbide46I18n.getMensaje(idioma, "doc.informeEconomico2016.col30").toUpperCase());
                celda.setCellStyle(estiloCelda);
                
                crearEstiloHoja2Economico2016(libro, fila2, celda2, estiloCelda2, idioma);
                
                //Estilos celdas datos
                    //celdas primera columna -> 3 Estilos
                HSSFCellStyle estiloCeldaColumnaPrimeraFilaPrimera = libro.createCellStyle();
                estiloCeldaColumnaPrimeraFilaPrimera.setWrapText(true);
                estiloCeldaColumnaPrimeraFilaPrimera.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCeldaColumnaPrimeraFilaPrimera.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCeldaColumnaPrimeraFilaPrimera.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCeldaColumnaPrimeraFilaPrimera.setFont(normal);                     

                HSSFCellStyle estiloCeldaColumnaPrimeraFilaIntermedia = libro.createCellStyle();
                estiloCeldaColumnaPrimeraFilaIntermedia.setWrapText(true);
                estiloCeldaColumnaPrimeraFilaIntermedia.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCeldaColumnaPrimeraFilaIntermedia.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCeldaColumnaPrimeraFilaIntermedia.setFont(normal);

                HSSFCellStyle estiloCeldaColumnaPrimeraFilaUltima = libro.createCellStyle();
                estiloCeldaColumnaPrimeraFilaUltima.setWrapText(true);
                estiloCeldaColumnaPrimeraFilaUltima.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCeldaColumnaPrimeraFilaUltima.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCeldaColumnaPrimeraFilaUltima.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCeldaColumnaPrimeraFilaUltima.setFont(normal);                        

                    //celdas segunda y tercera columna (antes de totales) -> 3 Estilos
                HSSFCellStyle estiloCeldaColumnasDosyTresFilaPrimera = libro.createCellStyle();
                estiloCeldaColumnasDosyTresFilaPrimera.setWrapText(true);
                estiloCeldaColumnasDosyTresFilaPrimera.setBorderLeft(HSSFCellStyle.BORDER_THIN);
                estiloCeldaColumnasDosyTresFilaPrimera.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCeldaColumnasDosyTresFilaPrimera.setFont(normal);
                estiloCeldaColumnasDosyTresFilaPrimera.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);

                HSSFCellStyle estiloCeldaColumnasDosyTresFilaIntermedia = libro.createCellStyle();
                estiloCeldaColumnasDosyTresFilaIntermedia.setWrapText(true);
                estiloCeldaColumnasDosyTresFilaIntermedia.setBorderLeft(HSSFCellStyle.BORDER_THIN);                    
                estiloCeldaColumnasDosyTresFilaIntermedia.setFont(normal);
                estiloCeldaColumnasDosyTresFilaIntermedia.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);

                HSSFCellStyle estiloCeldaColumnasDosyTresFilaUltima = libro.createCellStyle();
                estiloCeldaColumnasDosyTresFilaUltima.setWrapText(true);
                estiloCeldaColumnasDosyTresFilaUltima.setBorderLeft(HSSFCellStyle.BORDER_THIN);
                estiloCeldaColumnasDosyTresFilaUltima.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCeldaColumnasDosyTresFilaUltima.setFont(normal);
                estiloCeldaColumnasDosyTresFilaUltima.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);

                    //celdas siguientes Texto (pueden tener totales) ->  3 Estilos
                HSSFCellStyle estiloCeldaTextoFilaPrimera = libro.createCellStyle();
                estiloCeldaTextoFilaPrimera.setWrapText(true);
                estiloCeldaTextoFilaPrimera.setBorderLeft(HSSFCellStyle.BORDER_THIN);
                estiloCeldaTextoFilaPrimera.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCeldaTextoFilaPrimera.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCeldaTextoFilaPrimera.setFont(normal);

                HSSFCellStyle estiloCeldaTextoFilaIntermedia = libro.createCellStyle();
                estiloCeldaTextoFilaIntermedia.setWrapText(true);
                estiloCeldaTextoFilaIntermedia.setBorderLeft(HSSFCellStyle.BORDER_THIN);
                estiloCeldaTextoFilaIntermedia.setBorderTop(HSSFCellStyle.BORDER_THIN);
                estiloCeldaTextoFilaIntermedia.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCeldaTextoFilaIntermedia.setFont(normal);

                HSSFCellStyle estiloCeldaTextoFilaUltima = libro.createCellStyle();
                estiloCeldaTextoFilaUltima.setWrapText(true);
                estiloCeldaTextoFilaUltima.setBorderLeft(HSSFCellStyle.BORDER_THIN);
                estiloCeldaTextoFilaUltima.setBorderTop(HSSFCellStyle.BORDER_DOUBLE);
                estiloCeldaTextoFilaUltima.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCeldaTextoFilaUltima.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCeldaTextoFilaUltima.setFont(negrita);                       

                 //celdas siguientes Numericos (pueden tener totales) ->  3 Estilos
                HSSFCellStyle estiloCeldaNumericoFilaPrimera = libro.createCellStyle();
                estiloCeldaNumericoFilaPrimera.setWrapText(true);
                estiloCeldaNumericoFilaPrimera.setBorderLeft(HSSFCellStyle.BORDER_THIN);
                estiloCeldaNumericoFilaPrimera.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCeldaNumericoFilaPrimera.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCeldaNumericoFilaPrimera.setFont(normal);
                estiloCeldaNumericoFilaPrimera.setAlignment(HSSFCellStyle.ALIGN_RIGHT);

                HSSFCellStyle estiloCeldaNumericoFilaIntermedia = libro.createCellStyle();
                estiloCeldaNumericoFilaIntermedia.setWrapText(true);
                estiloCeldaNumericoFilaIntermedia.setBorderLeft(HSSFCellStyle.BORDER_THIN);
                estiloCeldaNumericoFilaIntermedia.setBorderTop(HSSFCellStyle.BORDER_THIN);
                estiloCeldaNumericoFilaIntermedia.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCeldaNumericoFilaIntermedia.setFont(normal);
                estiloCeldaNumericoFilaIntermedia.setAlignment(HSSFCellStyle.ALIGN_RIGHT);

                HSSFCellStyle estiloCeldaNumericoFilaUltima = libro.createCellStyle();
                estiloCeldaNumericoFilaUltima.setWrapText(true);
                estiloCeldaNumericoFilaUltima.setBorderLeft(HSSFCellStyle.BORDER_THIN);
                estiloCeldaNumericoFilaUltima.setBorderTop(HSSFCellStyle.BORDER_DOUBLE);
                estiloCeldaNumericoFilaUltima.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCeldaNumericoFilaUltima.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCeldaNumericoFilaUltima.setFont(negrita);
                estiloCeldaNumericoFilaUltima.setAlignment(HSSFCellStyle.ALIGN_RIGHT);

                //celdas ultima columna -> 3 Estilos
                HSSFCellStyle estiloCeldaColumnaUltimaFilaPrimera = libro.createCellStyle();
                estiloCeldaColumnaUltimaFilaPrimera.setWrapText(true);
                estiloCeldaColumnaUltimaFilaPrimera.setBorderLeft(HSSFCellStyle.BORDER_THIN);
                estiloCeldaColumnaUltimaFilaPrimera.setBorderRight(HSSFCellStyle.BORDER_THICK);                                          
                estiloCeldaColumnaUltimaFilaPrimera.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCeldaColumnaUltimaFilaPrimera.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCeldaColumnaUltimaFilaPrimera.setFont(normal); 
                estiloCeldaColumnaUltimaFilaPrimera.setAlignment(HSSFCellStyle.ALIGN_RIGHT);

                HSSFCellStyle estiloCeldaColumnaUltimaFilaIntermedia = libro.createCellStyle();
                estiloCeldaColumnaUltimaFilaIntermedia.setWrapText(true);
                estiloCeldaColumnaUltimaFilaIntermedia.setBorderLeft(HSSFCellStyle.BORDER_THIN);
                estiloCeldaColumnaUltimaFilaIntermedia.setBorderRight(HSSFCellStyle.BORDER_THICK);
                estiloCeldaColumnaUltimaFilaIntermedia.setBorderTop(HSSFCellStyle.BORDER_THIN);
                estiloCeldaColumnaUltimaFilaIntermedia.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCeldaColumnaUltimaFilaIntermedia.setFont(normal);
                estiloCeldaColumnaUltimaFilaIntermedia.setAlignment(HSSFCellStyle.ALIGN_RIGHT);

                HSSFCellStyle estiloCeldaColumnaUltimaFilaUltima = libro.createCellStyle();
                estiloCeldaColumnaUltimaFilaUltima.setWrapText(true);
                estiloCeldaColumnaUltimaFilaUltima.setBorderLeft(HSSFCellStyle.BORDER_THIN);
                estiloCeldaColumnaUltimaFilaUltima.setBorderRight(HSSFCellStyle.BORDER_THICK);
                estiloCeldaColumnaUltimaFilaUltima.setBorderTop(HSSFCellStyle.BORDER_DOUBLE);
                estiloCeldaColumnaUltimaFilaUltima.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCeldaColumnaUltimaFilaUltima.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCeldaColumnaUltimaFilaUltima.setFont(negrita);
                estiloCeldaColumnaUltimaFilaUltima.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
                
                boolean nuevo = true;
                HashMap<String, String> datosTercero = null;
                String empresa = null;
                String territorio = null;
                String pais = null;
                List<FilaInformeResumenEconomicoVO> listaPuestos = null;
                int n = 0;
                int p = 0;
                int numPaises = 0;
                int height = 0;
                
                String numExp = null;
                
                Double totalMinimis=0.00;
                Double costeContTot=0.00;
                Double costeCont=0.00;
                Double importeSolTot=0.00;
                Double importeSol=0.00;
                
                listaPuestos = manager.getDatosInformeResumenEconomico2016(ano, adapt);
                
                //Insertamos los datos, fila a fila
                for(FilaInformeResumenEconomicoVO filaI : listaPuestos)
                {
                    if(filaI.getNumExpediente() != null && !filaI.getNumExpediente().equals(numExp))
                    {
                        if(p != 0)
                        {
                            //poi 3 alpha
                            /*hoja.addMergedRegion(new Region(numFila-p, 0, numFila, 0));
                            hoja.addMergedRegion(new Region(numFila-p, 1, numFila, 1));
                            hoja.addMergedRegion(new Region(numFila-p, 2, numFila, 2));
                            hoja.addMergedRegion(new Region(numFila-p, 3, numFila-1, 3));*/
                            //poi 3.10                            
                            hoja.addMergedRegion(new CellRangeAddress(numFila-p,  numFila, 0, 0));
                            hoja.addMergedRegion(new CellRangeAddress(numFila-p,  numFila, 1, 1));
                            hoja.addMergedRegion(new CellRangeAddress(numFila-p,  numFila, 2, 2));
                            hoja.addMergedRegion(new CellRangeAddress(numFila-p,  numFila-1, 3, 3));
                            hoja.addMergedRegion(new CellRangeAddress(numFila-p,  numFila-1, 24, 24));

                        }
                        numExp = filaI.getNumExpediente();
                        nuevo = true;
                        p = 0;
                        n++;
                        numFila++;
                    }
                    else
                    {
                        numFila++; 
                        nuevo = false;
                        p++;
                    }
                    
                    fila = hoja.createRow(numFila);
                    
                    numPaises = 0;

                    if(filaI.getPais1() != null)
                    {
                        numPaises++;
                    }
                    if(filaI.getPais2() != null)
                    {
                        numPaises++;
                    }
                    if(filaI.getPais3() != null)
                    {
                        numPaises++;
                    }

                    height = 0;
                    height = Math.max(numPaises, 1);
                    height = height * fila.getHeight();

                    fila.setHeight((short)height);
                    
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

                    //COLUMNA: NUM EXPEDIENTE
                    celda = fila.createCell(0);
                    if(nuevo){
                        celda.setCellStyle(estiloCeldaColumnaPrimeraFilaPrimera); 
                    } else if(numFila == listaPuestos.size()){
                        celda.setCellStyle(estiloCeldaColumnaPrimeraFilaUltima); 
                    } else {
                        celda.setCellStyle(estiloCeldaColumnaPrimeraFilaIntermedia);
                    }
                    
                    if(nuevo){
                        celda.setCellValue(numExp);
                    }

                    //COLUMNA: ENTIDAD
                    if(filaI.getEntidad() != null){
                        empresa = filaI.getEntidad();
                    }
                    else {
                        datosTercero = MeLanbide46Manager.getInstance().getDatosTercero(codOrganizacion, numExp, String.valueOf(ano), ConstantesMeLanbide46.CODIGO_ROL_ENTIDAD_SOLICITANTE, adapt);
                        if(datosTercero.containsKey("TER_NOC")){
                            empresa = datosTercero.get("TER_NOC");
                        }
                        else {
                            empresa = "";
                        }
                    }                    
                    
                    celda = fila.createCell(1);
                    if(nuevo) {
                        celda.setCellStyle(estiloCeldaColumnasDosyTresFilaPrimera);
                    } else if(numFila == listaPuestos.size()) {
                        celda.setCellStyle(estiloCeldaColumnasDosyTresFilaUltima);
                    } else {
                        celda.setCellStyle(estiloCeldaColumnasDosyTresFilaIntermedia);
                    }                       

                    if(nuevo){
                        celda.setCellValue(empresa != null ? empresa.toUpperCase() : "");
                    } 

                    //COLUMNA: TERRITORIO HISTORICO
                    celda = fila.createCell(2);                    
                    if(nuevo) {
                        celda.setCellStyle(estiloCeldaColumnasDosyTresFilaPrimera);
                    } else if(numFila == listaPuestos.size()){
                        celda.setCellStyle(estiloCeldaColumnasDosyTresFilaUltima);
                    } else {
                        celda.setCellStyle(estiloCeldaColumnasDosyTresFilaIntermedia);
                    }                 
                    
                    if(nuevo)
                    {
                        territorio = filaI.getTerritorioHistorico() != null ? filaI.getTerritorioHistorico().toUpperCase() : "";
                        celda.setCellValue(territorio);
                    }

                    //COLUMNA: CNAE09
                    celda = fila.createCell(3);
                    
                    if(nuevo) {
                        celda.setCellStyle(estiloCeldaTextoFilaPrimera);
                    } else if(filaI.getOrden() != null && filaI.getOrden() == 2){
                        celda.setCellStyle(estiloCeldaTextoFilaUltima);
                    } else if(p > 0){
                        celda.setCellStyle(estiloCeldaTextoFilaIntermedia);
                    }
                    
                    String valor = filaI.getCnae09() != null ? filaI.getCnae09().toUpperCase() : "";
                    if(!valor.equals(""))
                        valor = valor.replaceAll("TOTAL", meLanbide46I18n.getMensaje(idioma, "doc.informeEconomico.total").toUpperCase());
                    if(valor.equals(meLanbide46I18n.getMensaje(idioma, "doc.informeEconomico.total").toUpperCase()) || nuevo)
                    {
                        celda.setCellValue(valor);
                    }
                    
                    //COLUMNA: Nº PUESTOS SOLIC
                    
                    celda = fila.createCell(4);
                    if(nuevo) {
                        celda.setCellStyle(estiloCeldaNumericoFilaPrimera);
                    } else if(filaI.getOrden() != null && filaI.getOrden() == 2){
                        celda.setCellStyle(estiloCeldaNumericoFilaUltima);
                    } else if(p > 0){
                        celda.setCellStyle(estiloCeldaNumericoFilaIntermedia);
                    }    
                    
                    celda.setCellValue(filaI.getnPuestosSolicitados() != null ? filaI.getnPuestosSolicitados() : 0);
                    celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                    
                    //COLUMNA: PUESTO
                    celda = fila.createCell(5);
                    if(nuevo) {
                        celda.setCellStyle(estiloCeldaTextoFilaPrimera);
                    } else if(filaI.getOrden() != null && filaI.getOrden() == 2){
                        celda.setCellStyle(estiloCeldaTextoFilaUltima);
                    } else if(p > 0){
                        celda.setCellStyle(estiloCeldaTextoFilaIntermedia);
                    }
                    celda.setCellValue(filaI.getPuesto() != null ? filaI.getCodPuesto()+"-"+filaI.getPuesto().toUpperCase() : "");
                    
                    //COLUMNA: RESULTADO
                    celda = fila.createCell(6);
                    if(nuevo) {
                        celda.setCellStyle(estiloCeldaNumericoFilaPrimera);
                    } else if(filaI.getOrden() != null && filaI.getOrden() == 2){
                        celda.setCellStyle(estiloCeldaNumericoFilaUltima);
                    } else if(p > 0){
                        celda.setCellStyle(estiloCeldaNumericoFilaIntermedia);
                    }
                    celda.setCellValue(filaI.getResultado() != null ? filaI.getResultado() : "");                    

                    //COLUMNA: FECHA INI
                    celda = fila.createCell(7);
                    if(nuevo) {
                        celda.setCellStyle(estiloCeldaTextoFilaPrimera);
                    } else if(filaI.getOrden() != null && filaI.getOrden() == 2){
                        celda.setCellStyle(estiloCeldaTextoFilaUltima);
                    } else if(p > 0){
                        celda.setCellStyle(estiloCeldaTextoFilaIntermedia);
                    }
                    celda.setCellValue(filaI.getFecIni() != null ? filaI.getFecIni() : "");
                    
                    //COLUMNA: FECHA FIN
                    celda = fila.createCell(8);
                    if(nuevo) {
                        celda.setCellStyle(estiloCeldaTextoFilaPrimera);
                    } else if(filaI.getOrden() != null && filaI.getOrden() == 2){
                        celda.setCellStyle(estiloCeldaTextoFilaUltima);
                    } else if(p > 0){
                        celda.setCellStyle(estiloCeldaTextoFilaIntermedia);
                    }
                    celda.setCellValue(filaI.getFecFin() != null ? filaI.getFecFin() : "");
                    
                    //COLUMNA: PERIODO MESES SUBV                    
                    celda = fila.createCell(9);
                    if(nuevo) {
                        celda.setCellStyle(estiloCeldaNumericoFilaPrimera);
                    } else if(filaI.getOrden() != null && filaI.getOrden() == 2){
                        celda.setCellStyle(estiloCeldaNumericoFilaUltima);
                    } else if(p > 0){
                        celda.setCellStyle(estiloCeldaNumericoFilaIntermedia);
                    }
                    
                    if (filaI.getOrden()!=2) {
                        celda.setCellValue(filaI.getPeriodoMesesSubv() != null ? filaI.getPeriodoMesesSubv() : 0);
                        celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                    }
                    
                    //COLUMNA: RANGO EDAD                    
                    celda = fila.createCell(10);
                    if(nuevo) {
                        celda.setCellStyle(estiloCeldaTextoFilaPrimera);
                    } else if(filaI.getOrden() != null && filaI.getOrden() == 2){
                        celda.setCellStyle(estiloCeldaTextoFilaUltima);
                    } else if(p > 0){
                        celda.setCellStyle(estiloCeldaTextoFilaIntermedia);
                    }
                    celda.setCellValue(filaI.getTitulacionAB() != null ? filaI.getTitulacionAB() : "");
                    
                    //COLUMNA: PAIS
                    celda = fila.createCell(11);
                    if(nuevo) {
                        celda.setCellStyle(estiloCeldaTextoFilaPrimera);
                    } else if(filaI.getOrden() != null && filaI.getOrden() == 2){
                        celda.setCellStyle(estiloCeldaTextoFilaUltima);
                    } else if(p > 0){
                        celda.setCellStyle(estiloCeldaTextoFilaIntermedia);
                    }

                    pais = filaI.getPais1() != null ? filaI.getPais1().toUpperCase() : "";
                    pais += filaI.getPais2() != null && !filaI.getPais2().equals("") ? (pais != null && !pais.equals("") ? System.getProperty("line.separator") : "") : "";
                    pais += filaI.getPais2() != null && !filaI.getPais2().equals("") ? filaI.getPais2().toUpperCase() : "";
                    pais += filaI.getPais3() != null && !filaI.getPais3().equals("") ? (pais != null && !pais.equals("") ? System.getProperty("line.separator") : "") : "";
                    pais += filaI.getPais3() != null && !filaI.getPais3().equals("") ? filaI.getPais3().toUpperCase() : "";

                    celda.setCellValue(pais);

                    //COLUMNA: COSTES CONTRATO SALARIO
                    celda = fila.createCell(12);
                    if(nuevo) {
                        celda.setCellStyle(estiloCeldaNumericoFilaPrimera);
                    } else if(filaI.getOrden() != null && filaI.getOrden() == 2){
                        celda.setCellStyle(estiloCeldaNumericoFilaUltima);
                    } else if(p > 0){
                        celda.setCellStyle(estiloCeldaNumericoFilaIntermedia);
                    }
                    
                    if (filaI.getOrden()!=2) {
                        celda.setCellValue(filaI.getCostesContSalarioSS() != null ? MeLanbide46Utils.redondearDecimalesBigDecimal(filaI.getCostesContSalarioSS(), 2).doubleValue() : new BigDecimal(ConstantesMeLanbide46.CERO_CON_DECIMALES).doubleValue());
                        celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                    }

                    //COLUMNA: GASTOS SEGURO
                    celda = fila.createCell(13);
                    if(nuevo) {
                        celda.setCellStyle(estiloCeldaNumericoFilaPrimera);
                    } else if(filaI.getOrden() != null && filaI.getOrden() == 2){
                        celda.setCellStyle(estiloCeldaNumericoFilaUltima);
                    } else if(p > 0){
                        celda.setCellStyle(estiloCeldaNumericoFilaIntermedia);
                    }                    
                    if (filaI.getOrden()!=2) {
                        celda.setCellValue(filaI.getGastosSeguroCon()!= null ? MeLanbide46Utils.redondearDecimalesBigDecimal(filaI.getGastosSeguroCon(), 2).doubleValue() : new BigDecimal(ConstantesMeLanbide46.CERO_CON_DECIMALES).doubleValue());
                        celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                    }

                    //COLUMNA: GASTOS VISADO
                    celda = fila.createCell(14);
                    if(nuevo) {
                        celda.setCellStyle(estiloCeldaNumericoFilaPrimera);
                    } else if(filaI.getOrden() != null && filaI.getOrden() == 2){
                        celda.setCellStyle(estiloCeldaNumericoFilaUltima);
                    } else if(p > 0){
                        celda.setCellStyle(estiloCeldaNumericoFilaIntermedia);
                    } 
                    if (filaI.getOrden()!=2) {
                        celda.setCellValue(filaI.getGastosVisadoCon() != null ? MeLanbide46Utils.redondearDecimalesBigDecimal(filaI.getGastosVisadoCon(), 2).doubleValue() : new BigDecimal(ConstantesMeLanbide46.CERO_CON_DECIMALES).doubleValue());
                        celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                    }

                    //COLUMNA: COSTES CONTRATACION
                    celda = fila.createCell(15);
                    BigDecimal visado = new BigDecimal("0");
                    BigDecimal seguro = new BigDecimal("0");
                    BigDecimal gastos = new BigDecimal("0");
                    if(filaI.getGastosVisadoCon()!= null)
                        visado = visado.add(filaI.getGastosVisadoCon());
                    if(filaI.getGastosSeguroCon()!= null)
                        seguro = seguro.add(filaI.getGastosSeguroCon());
                    if(filaI.getCostesContSalarioSS()!= null)
                        gastos = gastos.add(filaI.getCostesContSalarioSS());
                    visado = visado.add(seguro);
                    visado = visado.add(gastos);
                    costeCont = visado!= null ? MeLanbide46Utils.redondearDecimalesBigDecimal(visado, 2).doubleValue() : new BigDecimal(ConstantesMeLanbide46.CERO_CON_DECIMALES).doubleValue();
                    if(nuevo)
                    {
                        celda.setCellStyle(estiloCeldaNumericoFilaPrimera);
                        costeContTot=costeCont;
                    }
                    else if(filaI.getOrden() != null && filaI.getOrden() == 2)
                    {
                        celda.setCellStyle(estiloCeldaNumericoFilaUltima);
                        //costeContTot=costeContTot+costeCont;
                    }
                    else if(p > 0)
                    {
                        celda.setCellStyle(estiloCeldaNumericoFilaIntermedia);
                        costeContTot=costeContTot+costeCont;
                    }                    
                    
                    celda.setCellValue(filaI.getOrden()!=2 ? costeCont:costeContTot);
                    celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                        
                    //COLUMNA: SALARIO ANUAL BRUTO
                    
                    celda = fila.createCell(16);                    
                    if(nuevo) {
                        celda.setCellStyle(estiloCeldaNumericoFilaPrimera);
                    } else if(filaI.getOrden() != null && filaI.getOrden() == 2){
                        celda.setCellStyle(estiloCeldaNumericoFilaUltima);
                    } else if(p > 0){
                        celda.setCellStyle(estiloCeldaNumericoFilaIntermedia);
                    }

                    //salarioAnualBruto=filaI.getSolSalarioSS() != null ? MeLanbide39Utils.redondearDecimalesBigDecimal(filaI.getSolSalarioSS(), 2).doubleValue() : new BigDecimal(ConstantesMeLanbide39.CERO_CON_DECIMALES).doubleValue();
                    if (filaI.getOrden()!=2) {
                    //    celda.setCellValue(salarioAnualBruto);
                        celda.setCellValue(filaI.getSolSalarioSS() != null ? MeLanbide46Utils.redondearDecimalesBigDecimal(filaI.getSolSalarioSS(), 2).doubleValue() : new BigDecimal(ConstantesMeLanbide46.CERO_CON_DECIMALES).doubleValue());
                        celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                    }

                    //COLUMNA: GASTOS SEGURO SOLICITADO                    
                    celda = fila.createCell(17);
                    if(nuevo) {
                        celda.setCellStyle(estiloCeldaNumericoFilaPrimera);
                    } else if(filaI.getOrden() != null && filaI.getOrden() == 2){
                        celda.setCellStyle(estiloCeldaNumericoFilaUltima);
                    } else if(p > 0){
                        celda.setCellStyle(estiloCeldaNumericoFilaIntermedia);
                    }

                    //gastosSegSol=filaI.getSolDietasConvSS() != null ? MeLanbide39Utils.redondearDecimalesBigDecimal(filaI.getSolDietasConvSS(), 2).doubleValue() : new BigDecimal(ConstantesMeLanbide39.CERO_CON_DECIMALES).doubleValue();    
                    if (filaI.getOrden()!=2) {
                        //celda.setCellValue(gastosSegSol);
                        celda.setCellValue(filaI.getGastosSeguroSol() != null ? MeLanbide46Utils.redondearDecimalesBigDecimal(filaI.getGastosSeguroSol(), 2).doubleValue() : new BigDecimal(ConstantesMeLanbide46.CERO_CON_DECIMALES).doubleValue());
                        celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                    }
                                                           

                    //COLUMNA: GASTOS VISADO SOLICITADO                    
                    celda = fila.createCell(18);
                    if(nuevo) {
                        celda.setCellStyle(estiloCeldaNumericoFilaPrimera);
                    } else if(filaI.getOrden() != null && filaI.getOrden() == 2){
                        celda.setCellStyle(estiloCeldaNumericoFilaUltima);
                    } else if(p > 0){
                        celda.setCellStyle(estiloCeldaNumericoFilaIntermedia);
                    }
                    
                    if (filaI.getOrden()!=2) {
                        celda.setCellValue(filaI.getGastosVisadoSol() != null ? MeLanbide46Utils.redondearDecimalesBigDecimal(filaI.getGastosVisadoSol(), 2).doubleValue() : new BigDecimal(ConstantesMeLanbide46.CERO_CON_DECIMALES).doubleValue());
                        celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                    }
                    
                    //COLUMNA: IMPORTE SOLICITADO                    
                    celda = fila.createCell(19);
                    visado = new BigDecimal(0);
                    seguro = new BigDecimal(0);
                    gastos = new BigDecimal(0);
                    if(filaI.getGastosVisadoSol()!= null)
                        visado = visado.add(filaI.getGastosVisadoSol());
                    if(filaI.getGastosSeguroSol()!= null)
                        seguro = seguro.add(filaI.getGastosSeguroSol());
                    if(filaI.getSolSalarioSS()!= null)
                        gastos = gastos.add(filaI.getSolSalarioSS());
                    visado = visado.add(seguro);
                    visado = visado.add(gastos);
                    importeSol= visado != null ? MeLanbide46Utils.redondearDecimalesBigDecimal(visado, 2).doubleValue() : new BigDecimal(ConstantesMeLanbide46.CERO_CON_DECIMALES).doubleValue();
                    if(nuevo)
                    {
                        celda.setCellStyle(estiloCeldaNumericoFilaPrimera);
                        importeSolTot=importeSol;
                    }
                    else if(filaI.getOrden() != null && filaI.getOrden() == 2)
                    {
                        celda.setCellStyle(estiloCeldaNumericoFilaUltima);
                        //importeSolTot=importeSolTot+importeSol;
                    }
                    else if(p > 0)
                    {
                        celda.setCellStyle(estiloCeldaNumericoFilaIntermedia);
                        importeSolTot=importeSolTot+importeSol;
                    }                    
                    
                    celda.setCellValue(filaI.getOrden() != 2 ? importeSol : importeSolTot);
                    celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);

                    //COLUMNA: DOTACION ANUAL MAXIMA                    
                    celda = fila.createCell(20);
                    if(nuevo) {
                        celda.setCellStyle(estiloCeldaNumericoFilaPrimera);
                    } else if(filaI.getOrden() != null && filaI.getOrden() == 2){
                        celda.setCellStyle(estiloCeldaNumericoFilaUltima);
                    } else if(p > 0){
                        celda.setCellStyle(estiloCeldaNumericoFilaIntermedia);
                    }
                    if (filaI.getOrden()!=2) {
                        celda.setCellValue(filaI.getDotacionAnualMaxima() != null ? MeLanbide46Utils.redondearDecimalesBigDecimal(filaI.getDotacionAnualMaxima(), 2).doubleValue() : new BigDecimal(ConstantesMeLanbide46.CERO_CON_DECIMALES).doubleValue());
                        celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                    }

                    //COLUMNA: MESES SUBVENCIONABLES
                    
                    celda = fila.createCell(21);
                    if(nuevo) {
                        celda.setCellStyle(estiloCeldaNumericoFilaPrimera);
                    } else if(filaI.getOrden() != null && filaI.getOrden() == 2){
                        celda.setCellStyle(estiloCeldaNumericoFilaUltima);
                    } else if(p > 0){
                        celda.setCellStyle(estiloCeldaNumericoFilaIntermedia);
                    }
                    if (filaI.getOrden()!=2) {
                        celda.setCellValue(filaI.getMesesSubvencionables() != null ? filaI.getMesesSubvencionables() : 0);
                        celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                    }
                    
                    //COLUMNA: GASTOS SEGURO SUBVENCIONABLES
                    celda = fila.createCell(22);
                    if(nuevo) {
                        celda.setCellStyle(estiloCeldaNumericoFilaPrimera);
                    } else if(filaI.getOrden() != null && filaI.getOrden() == 2){
                        celda.setCellStyle(estiloCeldaNumericoFilaUltima);
                    } else if(p > 0){
                        celda.setCellStyle(estiloCeldaNumericoFilaIntermedia);
                    }
                    celda.setCellValue(filaI.getGastosSeguroPto()!= null ? MeLanbide46Utils.redondearDecimalesBigDecimal(filaI.getGastosSeguroPto(), 2).doubleValue() : new BigDecimal(ConstantesMeLanbide46.CERO_CON_DECIMALES).doubleValue());
                    celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);

                    //COLUMNA: GASTOS VISADO SUBVENCIONABLE                    
                    celda = fila.createCell(23);
                    if(nuevo) {
                        celda.setCellStyle(estiloCeldaNumericoFilaPrimera);
                    } else if(filaI.getOrden() != null && filaI.getOrden() == 2){
                        celda.setCellStyle(estiloCeldaNumericoFilaUltima);
                    } else if(p > 0){
                        celda.setCellStyle(estiloCeldaNumericoFilaIntermedia);
                    }
                    celda.setCellValue(filaI.getGastosVisadoPto() != null ? MeLanbide46Utils.redondearDecimalesBigDecimal(filaI.getGastosVisadoPto(), 2).doubleValue() : new BigDecimal(ConstantesMeLanbide46.CERO_CON_DECIMALES).doubleValue());
                    celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);

                    //COLUMNA: MINIMIS CONCEDIDOS
                    celda = fila.createCell(24);
                    int colMinimis =24;
                    if(nuevo) {
                        celda.setCellStyle(estiloCeldaNumericoFilaPrimera);
                    } else if(filaI.getOrden() != null && filaI.getOrden() == 2){
                        celda.setCellStyle(estiloCeldaNumericoFilaUltima);
                    } else if(p > 0){
                        celda.setCellStyle(estiloCeldaNumericoFilaIntermedia);
                    }
                    if(nuevo) {
                        totalMinimis = filaI.getMinimisConcedidos() != null ? MeLanbide46Utils.redondearDecimalesBigDecimal(filaI.getMinimisConcedidos(), 2).doubleValue() : new BigDecimal(ConstantesMeLanbide46.CERO_CON_DECIMALES).doubleValue();
                        celda.setCellValue(totalMinimis);                            
                    }
                    if (filaI.getOrden()!=2) {
                        celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC); 
                    }
                    
                    //COLUMNA: IMPORTE MAX SUBVENCIONABLE
                    celda = fila.createCell(25);
                    if(nuevo) {
                        celda.setCellStyle(estiloCeldaNumericoFilaPrimera);
                    } else if(filaI.getOrden() != null && filaI.getOrden() == 2){
                        celda.setCellStyle(estiloCeldaNumericoFilaUltima);
                    } else if(p > 0){
                        celda.setCellStyle(estiloCeldaNumericoFilaIntermedia);
                    }
                    celda.setCellValue(filaI.getImpMaxSubvencionable() != null ? MeLanbide46Utils.redondearDecimalesBigDecimal(filaI.getImpMaxSubvencionable(), 2).doubleValue() : new BigDecimal(ConstantesMeLanbide46.CERO_CON_DECIMALES).doubleValue());
                    celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);

                    //COLUMNA: IMPORTE CONCEDIDO
                    celda = fila.createCell(26);
                    if(nuevo) {
                        celda.setCellStyle(estiloCeldaNumericoFilaPrimera);
                    } else if(filaI.getOrden() != null && filaI.getOrden() == 2){
                        celda.setCellStyle(estiloCeldaNumericoFilaUltima);
                    } else if(p > 0){
                        celda.setCellStyle(estiloCeldaNumericoFilaIntermedia);
                    }
                    celda.setCellValue(filaI.getImporteConcedido() != null ? MeLanbide46Utils.redondearDecimalesBigDecimal(filaI.getImporteConcedido(), 2).doubleValue() : new BigDecimal(ConstantesMeLanbide46.CERO_CON_DECIMALES).doubleValue());
                    celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);

                    //COLUMNA: NUM RENUNCIAS
                    celda = fila.createCell(27);
                    if(nuevo) {
                        celda.setCellStyle(estiloCeldaNumericoFilaPrimera);
                    } else if(filaI.getOrden() != null && filaI.getOrden() == 2){
                        celda.setCellStyle(estiloCeldaNumericoFilaUltima);
                    } else if(p > 0){
                        celda.setCellStyle(estiloCeldaNumericoFilaIntermedia);
                    }
                    if (filaI.getOrden()!=2) {
                        celda.setCellValue(filaI.getNumRenuncias() != null ? filaI.getNumRenuncias() : 0);
                        celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                    }
                    
                    //COLUMNA: IMPORTE RENUNCIA
                    celda = fila.createCell(28);
                    if(nuevo) {
                        celda.setCellStyle(estiloCeldaNumericoFilaPrimera);
                    } else if(filaI.getOrden() != null && filaI.getOrden() == 2){
                        celda.setCellStyle(estiloCeldaNumericoFilaUltima);
                    } else if(p > 0){
                        celda.setCellStyle(estiloCeldaNumericoFilaIntermedia);
                    }
                    celda.setCellValue(filaI.getImporteRenuncia() != null ? MeLanbide46Utils.redondearDecimalesBigDecimal(filaI.getImporteRenuncia(), 2).doubleValue() : new BigDecimal(ConstantesMeLanbide46.CERO_CON_DECIMALES).doubleValue());
                    celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);

                    //COLUMNA: MOTIVO RENUNCIA
                    celda = fila.createCell(29);
                    if(nuevo) {
                        celda.setCellStyle(estiloCeldaTextoFilaPrimera);
                    } else if(filaI.getOrden() != null && filaI.getOrden() == 2){
                        celda.setCellStyle(estiloCeldaTextoFilaUltima);
                    } else if(p > 0){
                        celda.setCellStyle(estiloCeldaTextoFilaIntermedia);
                    }
                    
                    valor = filaI.getMotivo() != null ? filaI.getMotivo().toUpperCase() : "";
                    if(valor != null && !valor.equals(""))
                    {
                        valor = valor.replaceAll("-OBSERVACIONES:", " - "+meLanbide46I18n.getMensaje(idioma, "doc.informeEconomico.observaciones").toUpperCase()+":");
                    }
                    celda.setCellValue(valor);

                    //COLUMNA: IMPORTE CONCEDIDO TRAS RESOLUCION MODIF
                    celda = fila.createCell(30);
                    if(nuevo) {
                        celda.setCellStyle(estiloCeldaColumnaUltimaFilaPrimera);
                    } else if(filaI.getOrden() != null && filaI.getOrden() == 2){
                        celda.setCellStyle(estiloCeldaColumnaUltimaFilaUltima);
                    } else if(p > 0){
                        celda.setCellStyle(estiloCeldaColumnaUltimaFilaIntermedia);
                    }
                    celda.setCellValue(filaI.getImpConcedidoTrasResol() != null ? MeLanbide46Utils.redondearDecimalesBigDecimal(filaI.getImpConcedidoTrasResol(), 2).doubleValue() : new BigDecimal(ConstantesMeLanbide46.CERO_CON_DECIMALES).doubleValue());
                    celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                    
                    //if(!"TOTAL".equals(filaI.getCnae09()))
                    //    crearDatosHoja2Economico2016(libro, fila2, celda2, estiloCelda2, idioma, filaI,numExp, numFila2, nuevo, listaPuestos, datosTercero,  normal, codOrganizacion, ano,  adapt, p);
                    if(!"TOTAL".equals(filaI.getCnae09())){
                        numFila2++;
                        fila2 = hoja2.createRow(numFila2);
                        crearDatosHoja2Economico(libro, fila2, fila, normal, numExp, empresa, territorio, colMinimis, totalMinimis);
                    }
                }
                
                //Esto añade un merged region a la ultima fila, columnas de num expediente, entidad y territorio historico
                if(p != 0)
                {
                    //poi alpha
                    /*hoja.addMergedRegion(new Region(numFila-p, 0, numFila, 0));
                    hoja.addMergedRegion(new Region(numFila-p, 1, numFila, 1));
                    hoja.addMergedRegion(new Region(numFila-p, 2, numFila, 2));
                    hoja.addMergedRegion(new Region(numFila-p, 3, numFila-1, 3));*/
                    //poi 3.10
                    hoja.addMergedRegion(new CellRangeAddress(numFila-p,  numFila, 0, 0));
                    hoja.addMergedRegion(new CellRangeAddress(numFila-p,  numFila, 1, 1));
                    hoja.addMergedRegion(new CellRangeAddress(numFila-p,  numFila, 2, 2));
                    hoja.addMergedRegion(new CellRangeAddress(numFila-p,  numFila-1, 3, 3));
                    hoja.addMergedRegion(new CellRangeAddress(numFila-p,  numFila-1, 24, 24));
                    
                    estiloCelda = libro.createCellStyle();
                    estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                    estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                    estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                    estiloCelda.setWrapText(true);
                    estiloCelda.setFont(normal);

                    hoja.getRow(numFila-p).getCell(0).setCellStyle(estiloCelda);

                    estiloCelda = libro.createCellStyle();
                    estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                    estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THIN);
                    estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                    estiloCelda.setWrapText(true);
                    estiloCelda.setFont(normal);

                    hoja.getRow(numFila-p).getCell(1).setCellStyle(estiloCelda);
                    
                    hoja.getRow(numFila-p).getCell(2).setCellStyle(estiloCelda);

                    hoja.getRow(numFila-p).getCell(24).setCellStyle(estiloCelda); 
                }
                
                //se escribe el fichero
                /*File directorioTemp = new File(m_Conf.getString("PDF.base_dir"));
                File informe = File.createTempFile("resumenEconomico", ".xls", directorioTemp);

                FileOutputStream archivoSalida = new FileOutputStream(informe);*/
                libro.write(archivoSalida);
                archivoSalida.close();

                rutaArchivoSalida = informe.getAbsolutePath();
                
                FileInputStream istr = new FileInputStream(rutaArchivoSalida);

                BufferedInputStream bstr = new BufferedInputStream( istr ); // promote

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
            }
            catch (Exception ioe) 
            {
                ioe.printStackTrace();
            }
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }       
    
    private void crearDatosHoja2Economico2016(HSSFWorkbook libro, HSSFRow fila, HSSFCell celda, HSSFCellStyle estiloCelda, int idioma, FilaInformeResumenEconomicoVO filaI,
    String numExpediente, int numFila, boolean nuevo, List<FilaInformeResumenEconomicoVO> listaPuestos, HashMap<String, String> datosTercero,
    HSSFFont normal, int codOrganizacion, int ano, AdaptadorSQLBD adapt, int p)
    {
        try
        {
            int numIdiomas = 0;
            int numPaises = 0;
            int numTitulaciones = 0;
            int height = 0;
            String empresa = null;
            String pais = null;

            MeLanbide46I18n meLanbide46I18n = MeLanbide46I18n.getInstance();

           //COLUMNA: NUM EXPEDIENTE
            estiloCelda = libro.createCellStyle();
            estiloCelda.setWrapText(true);
//            estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
//            if(nuevo)
//            {
//                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
//            }
//            if(numFila == listaPuestos.size())
//            {
//                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
//            }
//            //estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
//            estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
            estiloCelda.setFont(normal);
            celda = fila.createCell(0);
            celda.setCellValue(numExpediente);
            celda.setCellStyle(estiloCelda);

            //COLUMNA: ENTIDAD
            if(filaI.getEntidad() != null)
            {
                empresa = filaI.getEntidad();
            }
            else
            {
                datosTercero = MeLanbide46Manager.getInstance().getDatosTercero(codOrganizacion, numExpediente, String.valueOf(ano), ConstantesMeLanbide46.CODIGO_ROL_ENTIDAD_SOLICITANTE, adapt);
                if(datosTercero.containsKey("TER_NOC"))
                {
                    empresa = datosTercero.get("TER_NOC");
                }
                else
                {
                    empresa = "";
                }
            }
            estiloCelda = libro.createCellStyle();
            estiloCelda.setWrapText(true);
//            estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THIN);
//            if(nuevo)
//            {
//                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
//            }
//            if(numFila == listaPuestos.size())
//            {
//                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
//            }
//            estiloCelda.setFont(normal);
//            //estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
//            estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
            celda = fila.createCell(1);
            
            celda.setCellValue(empresa != null ? empresa.toUpperCase() : "");

            celda.setCellStyle(estiloCelda);

            //COLUMNA: TERRITORIO HISTORICO
            estiloCelda = libro.createCellStyle();
            estiloCelda.setWrapText(true);
//            estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THIN);
//            if(nuevo)
//            {
//                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
//            }
//            if(numFila == listaPuestos.size())
//            {
//                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
//            }
//            estiloCelda.setFont(normal);
//            //estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
//            estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
            celda = fila.createCell(2);
            
            celda.setCellValue(filaI.getTerritorioHistorico() != null ? filaI.getTerritorioHistorico().toUpperCase() : "");

            celda.setCellStyle(estiloCelda);

            //COLUMNA: CNAE09
            estiloCelda = libro.createCellStyle();
            estiloCelda.setWrapText(true);
//            estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THIN);
//            if(nuevo)
//            {
//                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
//            }
//            else if(filaI.getOrden() != null && filaI.getOrden() == 2)
//            {
//                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_DOUBLE);
//            }
//            else if(p > 0)
//            {
//                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THIN);
//            }
//            if(numFila == listaPuestos.size())
//            {
//                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
//            }
            estiloCelda.setFont(normal);
            celda = fila.createCell(3);
            String valor = filaI.getCnae09() != null ? filaI.getCnae09().toUpperCase() : "";
            valor = valor.replaceAll("TOTAL", meLanbide46I18n.getMensaje(idioma, "doc.informeEconomico.total").toUpperCase());

                celda.setCellValue(valor);

            celda.setCellStyle(estiloCelda);

            //COLUMNA: Nº PUESTOS SOLIC
            estiloCelda = libro.createCellStyle();
            estiloCelda.setWrapText(true);
//            estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THIN);
//            if(nuevo)
//            {
//                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
//            }
//            else if(filaI.getOrden() != null && filaI.getOrden() == 2)
//            {
//                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_DOUBLE);
//            }
//            else if(p > 0)
//            {
//                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THIN);
//            }
//            if(numFila == listaPuestos.size())
//            {
//                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
//            }
            estiloCelda.setFont(normal);
            estiloCelda.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
            celda = fila.createCell(4);
            celda.setCellValue(filaI.getnPuestosSolicitados() != null ? filaI.getnPuestosSolicitados() : 0);
            celda.setCellStyle(estiloCelda);
            celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);

            //COLUMNA: PUESTO
            estiloCelda = libro.createCellStyle();
            estiloCelda.setWrapText(true);
//            estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THIN);
//            if(nuevo)
//            {
//                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
//            }
//            else if(filaI.getOrden() != null && filaI.getOrden() == 2)
//            {
//                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_DOUBLE);
//            }
//            else if(p > 0)
//            {
//                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THIN);
//            }
//            if(numFila == listaPuestos.size())
//            {
//                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
//            }
            estiloCelda.setFont(normal);
            celda = fila.createCell(5);
            celda.setCellValue(filaI.getPuesto() != null ? filaI.getCodPuesto()+"-"+filaI.getPuesto().toUpperCase() : "");
            celda.setCellStyle(estiloCelda);

            //COLUMNA: RESULTADO
            estiloCelda = libro.createCellStyle();
            estiloCelda.setWrapText(true);
//            estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THIN);
//            if(nuevo)
//            {
//                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
//            }
//            else if(filaI.getOrden() != null && filaI.getOrden() == 2)
//            {
//                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_DOUBLE);
//            }
//            if(numFila == listaPuestos.size())
//            {
//                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
//            }
            estiloCelda.setFont(normal);
            celda = fila.createCell(6);
            celda.setCellValue(filaI.getResultado() != null ? filaI.getResultado() : "");
            celda.setCellStyle(estiloCelda);

            //COLUMNA: FECHA INI
            estiloCelda = libro.createCellStyle();
            estiloCelda.setWrapText(true);
//            estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THIN);
//            if(nuevo)
//            {
//                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
//            }
//            else if(filaI.getOrden() != null && filaI.getOrden() == 2)
//            {
//                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_DOUBLE);
//            }
//            if(numFila == listaPuestos.size())
//            {
//                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
//            }
            estiloCelda.setFont(normal);
            celda = fila.createCell(7);
            celda.setCellValue(filaI.getFecIni() != null ? filaI.getFecIni() : "");
            celda.setCellStyle(estiloCelda);

            //COLUMNA: FECHA FIN
            estiloCelda = libro.createCellStyle();
            estiloCelda.setWrapText(true);
//            estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THIN);
//            if(nuevo)
//            {
//                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
//            }
//            else if(filaI.getOrden() != null && filaI.getOrden() == 2)
//            {
//                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_DOUBLE);
//            }
//            if(numFila == listaPuestos.size())
//            {
//                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
//            }
            estiloCelda.setFont(normal);
            celda = fila.createCell(8);
            celda.setCellValue(filaI.getFecFin() != null ? filaI.getFecFin() : "");
            celda.setCellStyle(estiloCelda);

            //COLUMNA: PERIODO MESES SUBV
            estiloCelda = libro.createCellStyle();
            estiloCelda.setWrapText(true);
            estiloCelda.setFont(normal);
            estiloCelda.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
            celda = fila.createCell(9);
            celda.setCellValue(filaI.getPeriodoMesesSubv() != null ? filaI.getPeriodoMesesSubv() : 0);
            celda.setCellStyle(estiloCelda);
            celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);

            //COLUMNA: TITULACION AB
            estiloCelda = libro.createCellStyle();
            estiloCelda.setWrapText(true);
            estiloCelda.setFont(normal);
            celda = fila.createCell(10);
            celda.setCellValue(filaI.getTitulacionAB() != null ? filaI.getTitulacionAB() : "");
            celda.setCellStyle(estiloCelda);

            //COLUMNA: PAIS
            estiloCelda = libro.createCellStyle();
            estiloCelda.setWrapText(true);
            estiloCelda.setFont(normal);
            celda = fila.createCell(11);

            pais = filaI.getPais1() != null ? filaI.getPais1().toUpperCase() : "";
            pais += filaI.getPais2() != null && !filaI.getPais2().equals("") ? (pais != null && !pais.equals("") ? System.getProperty("line.separator") : "") : "";
            pais += filaI.getPais2() != null && !filaI.getPais2().equals("") ? filaI.getPais2().toUpperCase() : "";
            pais += filaI.getPais3() != null && !filaI.getPais3().equals("") ? (pais != null && !pais.equals("") ? System.getProperty("line.separator") : "") : "";
            pais += filaI.getPais3() != null && !filaI.getPais3().equals("") ? filaI.getPais3().toUpperCase() : "";

            celda.setCellValue(pais);
            celda.setCellStyle(estiloCelda);

            //COLUMNA: COSTES CONTRATO SALARIO
            estiloCelda = libro.createCellStyle();
            estiloCelda.setWrapText(true);
            estiloCelda.setFont(normal);
            estiloCelda.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
            celda = fila.createCell(12);
            celda.setCellValue(filaI.getCostesContSalarioSS() != null ? MeLanbide46Utils.redondearDecimalesBigDecimal(filaI.getCostesContSalarioSS(), 2).doubleValue() : new BigDecimal(ConstantesMeLanbide46.CERO_CON_DECIMALES).doubleValue());
            celda.setCellStyle(estiloCelda);
            celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);

            //COLUMNA: GASTOS SEGURO
            estiloCelda = libro.createCellStyle();
            estiloCelda.setWrapText(true);
            estiloCelda.setFont(normal);
            estiloCelda.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
            celda = fila.createCell(13);
            celda.setCellValue(filaI.getGastosSeguroSol()!= null ? MeLanbide46Utils.redondearDecimalesBigDecimal(filaI.getGastosSeguroSol(), 2).doubleValue() : new BigDecimal(ConstantesMeLanbide46.CERO_CON_DECIMALES).doubleValue());
            celda.setCellStyle(estiloCelda);
            celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);

            //COLUMNA: GASTOS VISADO
            estiloCelda = libro.createCellStyle();
            estiloCelda.setWrapText(true);
            estiloCelda.setFont(normal);
            estiloCelda.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
            celda = fila.createCell(14);
            celda.setCellValue(filaI.getGastosVisadoSol() != null ? MeLanbide46Utils.redondearDecimalesBigDecimal(filaI.getGastosVisadoSol(), 2).doubleValue() : new BigDecimal(ConstantesMeLanbide46.CERO_CON_DECIMALES).doubleValue());
            celda.setCellStyle(estiloCelda);
            celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);

            //COLUMNA: COSTES CONTRATACION
            estiloCelda = libro.createCellStyle();
            estiloCelda.setWrapText(true);
            estiloCelda.setFont(normal);
            estiloCelda.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
            celda = fila.createCell(15);
            BigDecimal visado = new BigDecimal("0");
            BigDecimal seguro = new BigDecimal("0");
            BigDecimal gastos = new BigDecimal("0");
            if(filaI.getGastosVisadoSol()!= null)
                visado = visado.add(filaI.getGastosVisadoSol());
            if(filaI.getGastosSeguroSol()!= null)
                seguro = seguro.add(filaI.getGastosSeguroSol());
            if(filaI.getCostesContSalarioSS()!= null)
                gastos = gastos.add(filaI.getCostesContSalarioSS());
            visado = visado.add(seguro);
            visado = visado.add(gastos);

            celda.setCellValue(visado!= null ? MeLanbide46Utils.redondearDecimalesBigDecimal(visado, 2).doubleValue() : new BigDecimal(ConstantesMeLanbide46.CERO_CON_DECIMALES).doubleValue());
            celda.setCellStyle(estiloCelda);
            celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);

            //COLUMNA: SALARIO ANUAL BRUTO
            estiloCelda = libro.createCellStyle();
            estiloCelda.setWrapText(true);
            estiloCelda.setFont(normal);
            estiloCelda.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
            celda = fila.createCell(16);
            celda.setCellValue(filaI.getSolSalarioSS() != null ? MeLanbide46Utils.redondearDecimalesBigDecimal(filaI.getSolSalarioSS(), 2).doubleValue() : new BigDecimal(ConstantesMeLanbide46.CERO_CON_DECIMALES).doubleValue());
            celda.setCellStyle(estiloCelda);
            celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);

            //COLUMNA: GASTOS SEGURO
            estiloCelda = libro.createCellStyle();
            estiloCelda.setWrapText(true);
            estiloCelda.setFont(normal);
            estiloCelda.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
            celda = fila.createCell(17);
            celda.setCellValue(filaI.getGastosSeguroPto() != null ? MeLanbide46Utils.redondearDecimalesBigDecimal(filaI.getGastosSeguroPto(), 2).doubleValue() : new BigDecimal(ConstantesMeLanbide46.CERO_CON_DECIMALES).doubleValue());
            celda.setCellStyle(estiloCelda);
            celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);

            //COLUMNA: GASTOS VISADO
            estiloCelda = libro.createCellStyle();
            estiloCelda.setWrapText(true);
            estiloCelda.setFont(normal);
            estiloCelda.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
            celda = fila.createCell(18);
            celda.setCellValue(filaI.getGastosVisadoPto()!= null ? MeLanbide46Utils.redondearDecimalesBigDecimal(filaI.getGastosVisadoPto(), 2).doubleValue() : new BigDecimal(ConstantesMeLanbide46.CERO_CON_DECIMALES).doubleValue());
            celda.setCellStyle(estiloCelda);
            celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);

            //COLUMNA: IMPORTE SOLICITADO
            estiloCelda = libro.createCellStyle();
            estiloCelda.setWrapText(true);
            estiloCelda.setFont(normal);
            estiloCelda.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
            celda = fila.createCell(19);
            BigDecimal visado2 = new BigDecimal("0");
            BigDecimal seguro2 = new BigDecimal("0");
            BigDecimal gastos2 = new BigDecimal("0");
            if(filaI.getGastosVisadoPto()!= null)
                visado2 = visado2.add(filaI.getGastosVisadoPto());
            if(filaI.getGastosSeguroPto()!= null)
                seguro2 = seguro2.add(filaI.getGastosSeguroPto());
            if(filaI.getSolSalarioSS()!= null)
                gastos2 = gastos2.add(filaI.getSolSalarioSS());
            visado2 = visado2.add(seguro2);
            visado2 = visado2.add(gastos2);

            celda.setCellValue(visado!= null ? MeLanbide46Utils.redondearDecimalesBigDecimal(visado, 2).doubleValue() : new BigDecimal(ConstantesMeLanbide46.CERO_CON_DECIMALES).doubleValue());

            //celda.setCellValue(filaI.getSolicitado() != null ? MeLanbide46Utils.redondearDecimalesBigDecimal(filaI.getSolicitado(), 2).doubleValue() : new BigDecimal(ConstantesMeLanbide46.CERO_CON_DECIMALES).doubleValue());
            celda.setCellStyle(estiloCelda);
            celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);

            //COLUMNA: DOTACION ANUAL MAXIMA
            estiloCelda = libro.createCellStyle();
            estiloCelda.setWrapText(true);
            estiloCelda.setFont(normal);
            estiloCelda.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
            celda = fila.createCell(20);
            celda.setCellValue(filaI.getDotacionAnualMaxima() != null ? MeLanbide46Utils.redondearDecimalesBigDecimal(filaI.getDotacionAnualMaxima(), 2).doubleValue() : new BigDecimal(ConstantesMeLanbide46.CERO_CON_DECIMALES).doubleValue());
            celda.setCellStyle(estiloCelda);
            celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);

            //COLUMNA: MESES SUBVENCIONABLES
            estiloCelda = libro.createCellStyle();
            estiloCelda.setWrapText(true);
            estiloCelda.setFont(normal);
            estiloCelda.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
            celda = fila.createCell(21);
            celda.setCellValue(filaI.getMesesSubvencionables() != null ? filaI.getMesesSubvencionables() : 0);
            celda.setCellStyle(estiloCelda);
            celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);

            //COLUMNA: GASTOS SEGURO SUBVENCIONABLES
            estiloCelda = libro.createCellStyle();
            estiloCelda.setWrapText(true);
            estiloCelda.setFont(normal);
            estiloCelda.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
            celda = fila.createCell(22);
            celda.setCellValue(filaI.getGastosSeguroSub()!= null ? MeLanbide46Utils.redondearDecimalesBigDecimal(filaI.getGastosSeguroSub(), 2).doubleValue() : new BigDecimal(ConstantesMeLanbide46.CERO_CON_DECIMALES).doubleValue());
            celda.setCellStyle(estiloCelda);
            celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);

            //COLUMNA: GASTOS VISADO
            estiloCelda = libro.createCellStyle();
            estiloCelda.setWrapText(true);
            estiloCelda.setFont(normal);
            estiloCelda.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
            celda = fila.createCell(23);
            celda.setCellValue(filaI.getGastosVisadoSub() != null ? MeLanbide46Utils.redondearDecimalesBigDecimal(filaI.getGastosVisadoSub(), 2).doubleValue() : new BigDecimal(ConstantesMeLanbide46.CERO_CON_DECIMALES).doubleValue());
            celda.setCellStyle(estiloCelda);
            celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);

            //COLUMNA: MINIMIS CONCEDIDOS
            estiloCelda = libro.createCellStyle();
            estiloCelda.setWrapText(true);
            estiloCelda.setFont(normal);
            estiloCelda.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
            celda = fila.createCell(24);
            celda.setCellValue(filaI.getMinimisConcedidos() != null ? MeLanbide46Utils.redondearDecimalesBigDecimal(filaI.getMinimisConcedidos(), 2).doubleValue() : new BigDecimal(ConstantesMeLanbide46.CERO_CON_DECIMALES).doubleValue());
            celda.setCellStyle(estiloCelda);
            celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);

            //COLUMNA: IMPORTE MAX SUBVENCIONABLE
            estiloCelda = libro.createCellStyle();
            estiloCelda.setWrapText(true);
            estiloCelda.setFont(normal);
            estiloCelda.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
            celda = fila.createCell(25);
            celda.setCellValue(filaI.getImpMaxSubvencionable() != null ? MeLanbide46Utils.redondearDecimalesBigDecimal(filaI.getImpMaxSubvencionable(), 2).doubleValue() : new BigDecimal(ConstantesMeLanbide46.CERO_CON_DECIMALES).doubleValue());
            celda.setCellStyle(estiloCelda);
            celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);

            //COLUMNA: IMPORTE CONCEDIDO
            estiloCelda = libro.createCellStyle();
            estiloCelda.setWrapText(true);
            estiloCelda.setFont(normal);
            estiloCelda.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
            celda = fila.createCell(26);
            celda.setCellValue(filaI.getImporteConcedido() != null ? MeLanbide46Utils.redondearDecimalesBigDecimal(filaI.getImporteConcedido(), 2).doubleValue() : new BigDecimal(ConstantesMeLanbide46.CERO_CON_DECIMALES).doubleValue());
            celda.setCellStyle(estiloCelda);
            celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);

            //COLUMNA: NUM RENUNCIAS
            estiloCelda = libro.createCellStyle();
            estiloCelda.setWrapText(true);
            estiloCelda.setFont(normal);
            estiloCelda.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
            celda = fila.createCell(27);
            celda.setCellValue(filaI.getNumRenuncias() != null ? filaI.getNumRenuncias() : 0);
            celda.setCellStyle(estiloCelda);
            celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);

            //COLUMNA: IMPORTE RENUNCIA
            estiloCelda = libro.createCellStyle();
            estiloCelda.setWrapText(true);
            estiloCelda.setFont(normal);
            estiloCelda.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
            celda = fila.createCell(28);
            celda.setCellValue(filaI.getImporteRenuncia() != null ? MeLanbide46Utils.redondearDecimalesBigDecimal(filaI.getImporteRenuncia(), 2).doubleValue() : new BigDecimal(ConstantesMeLanbide46.CERO_CON_DECIMALES).doubleValue());
            celda.setCellStyle(estiloCelda);
            celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);

            //COLUMNA: MOTIVO RENUNCIA
            estiloCelda = libro.createCellStyle();
            estiloCelda.setWrapText(true);
            estiloCelda.setFont(normal);
            celda = fila.createCell(29);

            valor = filaI.getMotivo() != null ? filaI.getMotivo().toUpperCase() : "";
            if(valor != null && !valor.equals(""))
            {
                valor = valor.replaceAll("-OBSERVACIONES:", " - "+meLanbide46I18n.getMensaje(idioma, "doc.informeEconomico.observaciones").toUpperCase()+":");
            }
            celda.setCellValue(valor);

            celda.setCellStyle(estiloCelda);

            //COLUMNA: IMPORTE CONCEDIDO TRAS RESOLUCION MODIF
            estiloCelda = libro.createCellStyle();
            estiloCelda.setWrapText(true);
            estiloCelda.setFont(normal);
            estiloCelda.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
            celda = fila.createCell(30);
            celda.setCellValue(filaI.getImpConcedidoTrasResol() != null ? MeLanbide46Utils.redondearDecimalesBigDecimal(filaI.getImpConcedidoTrasResol(), 2).doubleValue() : new BigDecimal(ConstantesMeLanbide46.CERO_CON_DECIMALES).doubleValue());
            celda.setCellStyle(estiloCelda);
            celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                
        
        }catch(Exception ex){
            
        }
        
    }
    
    private void crearEstiloHoja2Economico2016(HSSFWorkbook libro, HSSFRow fila, HSSFCell celda, HSSFCellStyle estiloCelda,  int idioma)
    {
        try
        {
            MeLanbide46I18n meLanbide46I18n = MeLanbide46I18n.getInstance();
            
            HSSFPalette palette = libro.getCustomPalette();
            HSSFFont negritaTitulo = libro.createFont();
            negritaTitulo.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
            negritaTitulo.setColor(HSSFColor.WHITE.index);
            negritaTitulo.setFontHeight((short)200);
            HSSFColor hssfColor = null;
            try 
            {
                hssfColor= palette.findColor((byte)75, (byte)149, (byte)211); 
                if (hssfColor == null )
                {
                    hssfColor = palette.getColor(HSSFColor.ROYAL_BLUE.index);
                }
                
                //Se añaden los titulos de las columnas
                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(0);
                celda.setCellValue(meLanbide46I18n.getMensaje(idioma, "doc.informeEconomico2016.col1").toUpperCase());
                celda.setCellStyle(estiloCelda);

                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(1);
                celda.setCellValue(meLanbide46I18n.getMensaje(idioma, "doc.informeEconomico2016.col2").toUpperCase());
                celda.setCellStyle(estiloCelda);

                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(2);
                celda.setCellValue(meLanbide46I18n.getMensaje(idioma, "doc.informeEconomico2016.col31").toUpperCase());
                celda.setCellStyle(estiloCelda);

                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(3);
                celda.setCellValue(meLanbide46I18n.getMensaje(idioma, "doc.informeEconomico2016.col3").toUpperCase());
                celda.setCellStyle(estiloCelda);

                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(4);
                celda.setCellValue(meLanbide46I18n.getMensaje(idioma, "doc.informeEconomico2016.col4").toUpperCase());
                celda.setCellStyle(estiloCelda);

                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(5);
                celda.setCellValue(meLanbide46I18n.getMensaje(idioma, "doc.informeEconomico2016.col5").toUpperCase());
                celda.setCellStyle(estiloCelda);

                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(6);
                celda.setCellValue(meLanbide46I18n.getMensaje(idioma, "doc.informeEconomico2016.col6").toUpperCase());
                celda.setCellStyle(estiloCelda);

                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(7);
                celda.setCellValue(meLanbide46I18n.getMensaje(idioma, "doc.informeEconomico2016.col7").toUpperCase());
                celda.setCellStyle(estiloCelda);

                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(8);
                celda.setCellValue(meLanbide46I18n.getMensaje(idioma, "doc.informeEconomico2016.col8").toUpperCase());
                celda.setCellStyle(estiloCelda);

                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(9);
                celda.setCellValue(meLanbide46I18n.getMensaje(idioma, "doc.informeEconomico2016.col9").toUpperCase());
                celda.setCellStyle(estiloCelda);

                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(10);
                celda.setCellValue(meLanbide46I18n.getMensaje(idioma, "doc.informeEconomico2016.col10").toUpperCase());
                celda.setCellStyle(estiloCelda);

                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(11);
                celda.setCellValue(meLanbide46I18n.getMensaje(idioma, "doc.informeEconomico2016.col11").toUpperCase());
                celda.setCellStyle(estiloCelda);
                
                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(12);
                celda.setCellValue(meLanbide46I18n.getMensaje(idioma, "doc.informeEconomico2016.col12").toUpperCase());
                celda.setCellStyle(estiloCelda);

                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(13);
                celda.setCellValue(meLanbide46I18n.getMensaje(idioma, "doc.informeEconomico2016.col13").toUpperCase());
                celda.setCellStyle(estiloCelda);

                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(14);
                celda.setCellValue(meLanbide46I18n.getMensaje(idioma, "doc.informeEconomico2016.col14").toUpperCase());
                celda.setCellStyle(estiloCelda);

                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(15);
                celda.setCellValue(meLanbide46I18n.getMensaje(idioma, "doc.informeEconomico2016.col15").toUpperCase());
                celda.setCellStyle(estiloCelda);

                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(16);
                celda.setCellValue(meLanbide46I18n.getMensaje(idioma, "doc.informeEconomico2016.col16").toUpperCase());
                celda.setCellStyle(estiloCelda);

                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(17);
                celda.setCellValue(meLanbide46I18n.getMensaje(idioma, "doc.informeEconomico2016.col17").toUpperCase());
                celda.setCellStyle(estiloCelda);

                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(18);
                celda.setCellValue(meLanbide46I18n.getMensaje(idioma, "doc.informeEconomico2016.col18").toUpperCase());
                celda.setCellStyle(estiloCelda);

                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(19);
                celda.setCellValue(meLanbide46I18n.getMensaje(idioma, "doc.informeEconomico2016.col19").toUpperCase());
                celda.setCellStyle(estiloCelda);

                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(20);
                celda.setCellValue(meLanbide46I18n.getMensaje(idioma, "doc.informeEconomico2016.col20").toUpperCase());
                celda.setCellStyle(estiloCelda);

                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(21);
                celda.setCellValue(meLanbide46I18n.getMensaje(idioma, "doc.informeEconomico2016.col21").toUpperCase());
                celda.setCellStyle(estiloCelda);
                
                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(22);
                celda.setCellValue(meLanbide46I18n.getMensaje(idioma, "doc.informeEconomico2016.col22").toUpperCase());
                celda.setCellStyle(estiloCelda);

                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(23);
                celda.setCellValue(meLanbide46I18n.getMensaje(idioma, "doc.informeEconomico2016.col23").toUpperCase());
                celda.setCellStyle(estiloCelda);

                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(24);
                celda.setCellValue(meLanbide46I18n.getMensaje(idioma, "doc.informeEconomico2016.col24").toUpperCase());
                celda.setCellStyle(estiloCelda);

                
                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderRight(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(25);
                celda.setCellValue(meLanbide46I18n.getMensaje(idioma, "doc.informeEconomico2016.col25").toUpperCase());
                celda.setCellStyle(estiloCelda);
                
                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderRight(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(26);
                celda.setCellValue(meLanbide46I18n.getMensaje(idioma, "doc.informeEconomico2016.col26").toUpperCase());
                celda.setCellStyle(estiloCelda);
                
                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderRight(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(27);
                celda.setCellValue(meLanbide46I18n.getMensaje(idioma, "doc.informeEconomico2016.col27").toUpperCase());
                celda.setCellStyle(estiloCelda);
                
                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderRight(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(28);
                celda.setCellValue(meLanbide46I18n.getMensaje(idioma, "doc.informeEconomico2016.col28").toUpperCase());
                celda.setCellStyle(estiloCelda);
                
                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderRight(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(29);
                celda.setCellValue(meLanbide46I18n.getMensaje(idioma, "doc.informeEconomico2016.col29").toUpperCase());
                celda.setCellStyle(estiloCelda);
                
                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderRight(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(30);
                celda.setCellValue(meLanbide46I18n.getMensaje(idioma, "doc.informeEconomico2016.col30").toUpperCase());
                celda.setCellStyle(estiloCelda);
            }
            catch (Exception e) 
            {
                e.printStackTrace();
            }

                
        }catch(Exception ex){
            
        }
        
    }
    
    
    public void generarInformeDatosPuestos2016(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response)
    {
        String codigoOperacion = "0";
        String rutaArchivoSalida = null;
        try
        {
            Integer ano = Integer.parseInt(request.getParameter("ano"));
            AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
            
            int idioma = 1;
            HttpSession session = request.getSession();
            UsuarioValueObject usuario = new UsuarioValueObject();
            try
            {
                if (session != null) 
                {
                    usuario = (UsuarioValueObject) session.getAttribute("usuario");
                    if (usuario != null) 
                    {
                        idioma = usuario.getIdioma();
                    }
                }
            }
            catch(Exception ex)
            {

            }
            
            MeLanbide46I18n meLanbide46I18n = MeLanbide46I18n.getInstance();
            MeLanbide46Manager manager = MeLanbide46Manager.getInstance();
            SimpleDateFormat format = new SimpleDateFormat(ConstantesMeLanbide46.FORMATO_FECHA);
            FileOutputStream archivoSalida = null;
            FileInputStream istr = null;
            
            try 
            {
                HSSFWorkbook libro = new HSSFWorkbook();
            
                //se escribe el fichero
                DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
                //get current date time with Date()
                Date date = new Date();
                String fechaAct=dateFormat.format(date);
           
                File informe= new File("justificacionFinal_"+fechaAct+".xls");

                archivoSalida = new FileOutputStream(informe);
                
                HSSFPalette palette = libro.getCustomPalette();
                HSSFColor hssfColor = null;
                try 
                {
                    hssfColor= palette.findColor((byte)75, (byte)149, (byte)211); 
                    if (hssfColor == null )
                    {
                        hssfColor = palette.getColor(HSSFColor.ROYAL_BLUE.index);
                    }
                }
                catch (Exception e) 
                {
                    e.printStackTrace();
                }
                List<FilaInformeDatosPuestosVO> listaPuestos = null;
                List<FilaInformeHoja2DatosPuestosVO> listaPuestosH = null;
                HSSFFont negrita = libro.createFont();
                negrita.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
                
                HSSFFont normal = libro.createFont();
                normal.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
                normal.setFontHeight((short)150);
            
                HSSFFont negritaTitulo = libro.createFont();
                negritaTitulo.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
                negritaTitulo.setColor(HSSFColor.WHITE.index);
                negritaTitulo.setFontHeight((short)200);
                int numFila = 0;
                int numFila2 = 0;

                HSSFRow fila = null;
                HSSFCell celda = null;
                HSSFCellStyle estiloCelda = null;

                HSSFRow fila2 = null;
                HSSFCell celda2 = null;
                HSSFCellStyle estiloCelda2 = null;

                HSSFSheet hoja = libro.createSheet("Datos puestos");
                HSSFSheet hoja2 = libro.createSheet("Datos pagos");
                //hoja2 = libro.createSheet();


                //Se establece el ancho de cada columnas
                hoja.setColumnWidth(0, 6000);//Num. expediente
                hoja.setColumnWidth(1, 10000);//empresa
                hoja.setColumnWidth(2, 8000);//persona contratada
                hoja.setColumnWidth(3, 3000);//sexo
                hoja.setColumnWidth(4, 3000);//nivel formativo
                hoja.setColumnWidth(5, 8000);//país
                hoja.setColumnWidth(6, 5000);//salario anual bruto + ss
                hoja.setColumnWidth(7, 5000);//dietas aloj y manutención
                hoja.setColumnWidth(8, 3000);//meses exterior
                hoja.setColumnWidth(9, 4000);//tramitación permisos
                hoja.setColumnWidth(10, 6000);//importe concedido
                hoja.setColumnWidth(11, 6000);//país final
                hoja.setColumnWidth(12, 5000);//salario anexo
                hoja.setColumnWidth(13, 4500);//contrato inicio
                hoja.setColumnWidth(14, 3000);//fin periodo subv
                hoja.setColumnWidth(15, 5000);//días subv contrato
                hoja.setColumnWidth(16, 5000);//meses ext subv
                hoja.setColumnWidth(17, 5000);//motivo baja
                hoja.setColumnWidth(18, 5000);//Salario anual bruto + ss emp jus
                hoja.setColumnWidth(19, 5000);//Bonificaciones jus
                hoja.setColumnWidth(20, 5000);//Salario + SS bonif
                hoja.setColumnWidth(21, 5000);//dietas aloj y manu jus
                hoja.setColumnWidth(22, 5000);//tram permisos jus
                hoja.setColumnWidth(23, 5000);//total justi
                hoja.setColumnWidth(24, 5000);//max subv
                hoja.setColumnWidth(25, 5000);//bonificaciones
                hoja.setColumnWidth(26, 5000);//dietas aloj y manu subv
                hoja.setColumnWidth(27, 5000);//tram permisos subv
                hoja.setColumnWidth(28, 5000);//total subv
                
                hoja2.setColumnWidth(0, 6000);//Num. expediente
                hoja2.setColumnWidth(1, 10000);//empresa
                hoja2.setColumnWidth(2, 8000);//persona contratada
                hoja2.setColumnWidth(3, 5000);//sexo
                hoja2.setColumnWidth(4, 10000);//nivel formativo
                hoja2.setColumnWidth(5, 8000);//país
                hoja2.setColumnWidth(6, 12000);//salario anual bruto + ss
                
                
                fila = hoja.createRow(numFila);
                
                //Se añaden los titulos de las columnas
                // poi alpha
                /*hoja.addMergedRegion(new Region(numFila, 7, numFila, 11));
                hoja.addMergedRegion(new Region(numFila, 18, numFila, 23));
                hoja.addMergedRegion(new Region(numFila, 24, numFila, 28));*/
                //poi 3.10
                hoja.addMergedRegion(new CellRangeAddress(numFila,  numFila, 7,11));
                hoja.addMergedRegion(new CellRangeAddress(numFila,  numFila, 18,23));
                hoja.addMergedRegion(new CellRangeAddress(numFila,  numFila, 24, 28));

                crearEstiloCabeceraInformeDatosPuestos(libro, fila, celda, estiloCelda, idioma);
                numFila ++;
                
                fila = hoja.createRow(numFila);
                fila2 = hoja2.createRow(numFila2);
                crearEstiloInformeDatosPuestos2016(libro, fila, celda, estiloCelda, idioma);
                crearEstiloInformeDatosPuestosHoja2_2016(libro, fila2, celda2, estiloCelda2, idioma);
                
                boolean nuevo = true;
                int p = 0;
                int n = 0;
                int height = 0;                
                
                String numExp = null;
                listaPuestos = manager.getDatosInformePuestos(ano, adapt);
                
                int maxLengthNombre = 0;
                //Insertamos los datos, fila a fila
                for(FilaInformeDatosPuestosVO filaI : listaPuestos)
                {
                    if(filaI.getNumExpediente() != null && !filaI.getNumExpediente().equals(numExp))
                    {
                        numExp = filaI.getNumExpediente();
                        nuevo = true;
                        p = 0;
                        n++;
                        numFila++;
                    }
                    else
                    {
                        numFila++;   
                        nuevo = false;
                        p++;
                    }
                    
                    fila = hoja.createRow(numFila);
                    
                    crearDatosInformePuestos2016(libro, fila, celda, estiloCelda, idioma, filaI, numExp, numFila, nuevo, n, listaPuestos, normal, codOrganizacion, ano,  adapt,  format);

                }
                
                listaPuestosH = manager.getDatosInformePuestosHoja2(ano, adapt);
                nuevo = true; numExp = null;
                for(FilaInformeHoja2DatosPuestosVO filaI : listaPuestosH)
                {
                    if(filaI.getNumExpediente() != null && !filaI.getNumExpediente().equals(numExp))
                    {
//                        }
                        numExp = filaI.getNumExpediente();
                        nuevo = true;
                        p = 0;
                        n++;
                        numFila2++;
                    }
                    else
                    {
                        numFila2++;   
                        nuevo = false;
                        p++;
                    }
                    
                    fila2 = hoja2.createRow(numFila2);               
                    crearDatosInformePuestosHoja2_2016(libro, fila2, celda2, estiloCelda2, idioma, filaI, numExp, numFila, nuevo, n, listaPuestosH, normal, codOrganizacion, ano,  adapt,  format);

                }
                
                hoja.setColumnWidth(16, (short)(maxLengthNombre*400));
                
                //Añade sumatorios a la ultima fila
                numFila++;
                    
                /*File directorioTemp = new File(m_Conf.getString("PDF.base_dir"));
                File informe = File.createTempFile("resumenPuestosContratados", ".xls", directorioTemp);

                FileOutputStream archivoSalida = new FileOutputStream(informe);*/
                libro.write(archivoSalida);
                //archivoSalida.close();

                rutaArchivoSalida = informe.getAbsolutePath();
                
                istr = new FileInputStream(rutaArchivoSalida);

                BufferedInputStream bstr = new BufferedInputStream( istr ); // promote

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
            } 
            catch (Exception ioe) 
            {
                log.debug("EXCEPCION informe resumenPuestosContratados");
                ioe.printStackTrace();
                
            }
            finally {
                if (archivoSalida !=null) archivoSalida.close();
                if (istr !=null) istr.close();
            }
        }
        catch(Exception ex)
        {
            log.debug("EXCEPCION informe resumenPuestosContratados");
            ex.printStackTrace();
            
        }
    }
    
    private void crearEstiloInformeDatosPuestos2016(HSSFWorkbook libro, HSSFRow fila, HSSFCell celda, HSSFCellStyle estiloCelda, int idioma)
    {
        try
        {
            MeLanbide46I18n meLanbide46I18n = MeLanbide46I18n.getInstance();
            
            HSSFPalette palette = libro.getCustomPalette();
            HSSFColor hssfColor = null;
            HSSFColor hssfColor2 = null;
            try 
            {
                hssfColor= palette.findColor((byte)75, (byte)149, (byte)211); 
                if (hssfColor == null )
                {
                    hssfColor = palette.getColor(HSSFColor.ROYAL_BLUE.index);
                }
                hssfColor2 = palette.getColor(HSSFColor.GREY_50_PERCENT.index);
                
                HSSFFont negrita = libro.createFont();
                negrita.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);

                HSSFFont normal = libro.createFont();
                normal.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
                normal.setFontHeight((short)150);

                HSSFFont negritaTitulo = libro.createFont();
                negritaTitulo.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
                negritaTitulo.setFontHeight((short)170);
                negritaTitulo.setColor(HSSFColor.WHITE.index);
                //celda = fila.

                //cabeceras
                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(0);
                celda.setCellValue(meLanbide46I18n.getMensaje(idioma, "doc.informeDatosPuestos2016.col1").toUpperCase());
                celda.setCellStyle(estiloCelda);

                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(1);
                celda.setCellValue(meLanbide46I18n.getMensaje(idioma, "doc.informeDatosPuestos2016.col2").toUpperCase());
                celda.setCellStyle(estiloCelda);

                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(2);
                celda.setCellValue(meLanbide46I18n.getMensaje(idioma, "doc.informeDatosPuestos2016.col3").toUpperCase());
                celda.setCellStyle(estiloCelda);

                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(3);
                celda.setCellValue(meLanbide46I18n.getMensaje(idioma, "doc.informeDatosPuestos2016.col4").toUpperCase());
                celda.setCellStyle(estiloCelda);

                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(4);
                celda.setCellValue(meLanbide46I18n.getMensaje(idioma, "doc.informeDatosPuestos2016.col5").toUpperCase());
                celda.setCellStyle(estiloCelda);

                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(5);
                celda.setCellValue(meLanbide46I18n.getMensaje(idioma, "doc.informeDatosPuestos2016.col6").toUpperCase());
                celda.setCellStyle(estiloCelda);

                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(6);
                celda.setCellValue(meLanbide46I18n.getMensaje(idioma, "doc.informeDatosPuestos2016.col7").toUpperCase());
                celda.setCellStyle(estiloCelda);

                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor2.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(7);
                celda.setCellValue(meLanbide46I18n.getMensaje(idioma, "doc.informeDatosPuestos2016.col8").toUpperCase());
                celda.setCellStyle(estiloCelda);

                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor2.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(8);
                celda.setCellValue(meLanbide46I18n.getMensaje(idioma, "doc.informeDatosPuestos2016.col9").toUpperCase());
                celda.setCellStyle(estiloCelda);

                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor2.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(9);
                celda.setCellValue(meLanbide46I18n.getMensaje(idioma, "doc.informeDatosPuestos2016.col10").toUpperCase());
                celda.setCellStyle(estiloCelda);

                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor2.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(10);
                celda.setCellValue(meLanbide46I18n.getMensaje(idioma, "doc.informeDatosPuestos2016.col11").toUpperCase());
                celda.setCellStyle(estiloCelda);

                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor2.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(11);
                celda.setCellValue(meLanbide46I18n.getMensaje(idioma, "doc.informeDatosPuestos2016.col12").toUpperCase());
                celda.setCellStyle(estiloCelda);

                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(12);
                celda.setCellValue(meLanbide46I18n.getMensaje(idioma, "doc.informeDatosPuestos2016.col13").toUpperCase());
                celda.setCellStyle(estiloCelda);

                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(13);
                celda.setCellValue(meLanbide46I18n.getMensaje(idioma, "doc.informeDatosPuestos2016.col14").toUpperCase());
                celda.setCellStyle(estiloCelda);

                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(14);
                celda.setCellValue(meLanbide46I18n.getMensaje(idioma, "doc.informeDatosPuestos2016.col15").toUpperCase());
                celda.setCellStyle(estiloCelda);

                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(15);
                celda.setCellValue(meLanbide46I18n.getMensaje(idioma, "doc.informeDatosPuestos2016.col16").toUpperCase());
                celda.setCellStyle(estiloCelda);

                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderRight(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(16);
                celda.setCellValue(meLanbide46I18n.getMensaje(idioma, "doc.informeDatosPuestos2016.col17").toUpperCase());
                celda.setCellStyle(estiloCelda);

                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(17);
                celda.setCellValue(meLanbide46I18n.getMensaje(idioma, "doc.informeDatosPuestos2016.col18").toUpperCase());
                celda.setCellStyle(estiloCelda);

                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(18);
                celda.setCellValue(meLanbide46I18n.getMensaje(idioma, "doc.informeDatosPuestos2016.col19").toUpperCase());
                celda.setCellStyle(estiloCelda);

                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(19);
                celda.setCellValue(meLanbide46I18n.getMensaje(idioma, "doc.informeDatosPuestos2016.col20").toUpperCase());
                celda.setCellStyle(estiloCelda);

                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(20);
                celda.setCellValue(meLanbide46I18n.getMensaje(idioma, "doc.informeDatosPuestos2016.col21").toUpperCase());
                celda.setCellStyle(estiloCelda);

                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(21);
                celda.setCellValue(meLanbide46I18n.getMensaje(idioma, "doc.informeDatosPuestos2016.col22").toUpperCase());
                celda.setCellStyle(estiloCelda);

                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(22);
                celda.setCellValue(meLanbide46I18n.getMensaje(idioma, "doc.informeDatosPuestos2016.col23").toUpperCase());
                celda.setCellStyle(estiloCelda);

                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(23);
                celda.setCellValue(meLanbide46I18n.getMensaje(idioma, "doc.informeDatosPuestos2016.col24").toUpperCase());
                celda.setCellStyle(estiloCelda);

                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor2.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(24);
                celda.setCellValue(meLanbide46I18n.getMensaje(idioma, "doc.informeDatosPuestos2016.col25").toUpperCase());
                celda.setCellStyle(estiloCelda);

                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor2.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(25);
                celda.setCellValue(meLanbide46I18n.getMensaje(idioma, "doc.informeDatosPuestos2016.col26").toUpperCase());
                celda.setCellStyle(estiloCelda);

                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor2.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(26);
                celda.setCellValue(meLanbide46I18n.getMensaje(idioma, "doc.informeDatosPuestos2016.col27").toUpperCase());
                celda.setCellStyle(estiloCelda);

                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor2.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(27);
                celda.setCellValue(meLanbide46I18n.getMensaje(idioma, "doc.informeDatosPuestos2016.col28").toUpperCase());
                celda.setCellStyle(estiloCelda);

                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor2.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(28);
                celda.setCellValue(meLanbide46I18n.getMensaje(idioma, "doc.informeDatosPuestos2016.col29").toUpperCase());
                celda.setCellStyle(estiloCelda);
            }
            catch (Exception e) 
            {
                e.printStackTrace();
            }
            
        }catch(Exception ex){
            
        }
        
    }
    private void crearEstiloCabeceraInformeDatosPuestos2016(HSSFWorkbook libro, HSSFRow fila, HSSFCell celda, HSSFCellStyle estiloCelda, int idioma)
    {
        try
        {
            //MeLanbide46I18n meLanbide46I18n = MeLanbide46I18n.getInstance();
            
            HSSFPalette palette = libro.getCustomPalette();
            HSSFColor hssfColor = null;
            HSSFColor hssfColor2 = null;
            try 
            {
                hssfColor= palette.findColor((byte)75, (byte)149, (byte)211); 
                if (hssfColor == null )
                {
                    hssfColor = palette.getColor(HSSFColor.GREY_50_PERCENT.index);
                }
                hssfColor2 = palette.getColor(HSSFColor.ROYAL_BLUE.index);
                
                HSSFFont negrita = libro.createFont();
                negrita.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);

                HSSFFont normal = libro.createFont();
                normal.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
                normal.setFontHeight((short)150);

                HSSFFont negritaTitulo = libro.createFont();
                negritaTitulo.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
                negritaTitulo.setFontHeight((short)170);
                negritaTitulo.setColor(HSSFColor.WHITE.index);


                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(7);
                celda.setCellValue("SUBVENCIÓN CONCEDIDA");
                celda.setCellStyle(estiloCelda);

                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor2.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(18);
                celda.setCellValue("JUSTIFICADO");
                celda.setCellStyle(estiloCelda);


                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(24);
                celda.setCellValue("SUBVENCIÓN FINAL");
                celda.setCellStyle(estiloCelda);
            }
            catch (Exception e) 
            {
                e.printStackTrace();
            }

            
        }catch(Exception ex){
            
        }
        
    }
    
    private void crearDatosInformePuestos2016 (HSSFWorkbook libro, HSSFRow fila, HSSFCell celda, HSSFCellStyle estiloCelda, int idioma, FilaInformeDatosPuestosVO filaI,
    String numExp, int numFila, boolean nuevo, int n, List<FilaInformeDatosPuestosVO> listaPuestos, 
    HSSFFont normal, int codOrganizacion, int ano, AdaptadorSQLBD adapt, SimpleDateFormat format)
    {
        try
        {
            String empresa = null;
            String idiomas = null;
            String pais = null;
            String titulacion = null;
                
            //COLUMNA: NUM_EXPEDIENTE
            estiloCelda = libro.createCellStyle();
            estiloCelda.setWrapText(true);
            estiloCelda.setFont(normal);
            //estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
            estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);

            if(n == 1)
            {
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
            }
            else
            {
                //estiloCelda.setBorderTop(HSSFCellStyle.BORDER_DOUBLE);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
            }
            if(numFila == listaPuestos.size())
            {
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
            }
            celda = fila.createCell(0);
            celda.setCellValue(filaI.getNumExpediente() != null ? filaI.getNumExpediente().toUpperCase() : "");
            celda.setCellStyle(estiloCelda);

            //COLUMNA: EMPRESA
            estiloCelda = libro.createCellStyle();
            estiloCelda.setWrapText(true);
            estiloCelda.setFont(normal);
            estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THIN);
            //estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);

            estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);

            if(numFila == listaPuestos.size())
            {
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
            }
            celda = fila.createCell(1);

            celda.setCellValue(filaI.getEmpresa() != null ? filaI.getEmpresa().toUpperCase() : "");
            celda.setCellStyle(estiloCelda);
            
            
            //COLUMNA: ACTIVIDAD
            estiloCelda = libro.createCellStyle();
            estiloCelda.setWrapText(true);
            estiloCelda.setFont(normal);
            estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THIN);
            //estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);

            estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);

            if(numFila == listaPuestos.size())
            {
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
            }
            celda = fila.createCell(2);

            celda.setCellValue(filaI.getActividad()!= null ? filaI.getActividad().toUpperCase() : "");
            celda.setCellStyle(estiloCelda);

            //COLUMNA: PERSONA CONTRATADA
            estiloCelda = libro.createCellStyle();
            estiloCelda.setWrapText(true);
            estiloCelda.setFont(normal);
            estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THIN);

            if(n == 1)
            {
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
            }
            else
            {
                //estiloCelda.setBorderTop(HSSFCellStyle.BORDER_DOUBLE);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
            }

            if(numFila == listaPuestos.size())
            {
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
            }
            celda = fila.createCell(3);
            celda.setCellValue(filaI.getPersContratada() != null ? filaI.getPersContratada().toUpperCase() : "");
            celda.setCellStyle(estiloCelda);

            //COLUMNA: SEXO
            estiloCelda = libro.createCellStyle();
            estiloCelda.setWrapText(true);
            estiloCelda.setFont(normal);
            estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THIN);
            estiloCelda.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
            //estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
            if(n == 1)
            {
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
            }
            else
            {
                //estiloCelda.setBorderTop(HSSFCellStyle.BORDER_DOUBLE);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
            }
            if(numFila == listaPuestos.size())
            {
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
            }
            celda = fila.createCell(4);
            celda.setCellValue(filaI.getSexo() != null ? filaI.getSexo() : "");
            //celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
            celda.setCellStyle(estiloCelda);


            //COLUMNA: NIVEL FORMATIVO
            estiloCelda = libro.createCellStyle();
            estiloCelda.setWrapText(true);
            estiloCelda.setFont(normal);
            estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THIN);
            if(n == 1)
            {
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
            }
            else
            {
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
            }
            if(numFila == listaPuestos.size())
            {
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
            }
            celda = fila.createCell(5);
            celda.setCellValue(filaI.getNivelFormativo() != null ? filaI.getNivelFormativo().toUpperCase() : "");
            celda.setCellStyle(estiloCelda);

            //COLUMNA: PAIS
            estiloCelda = libro.createCellStyle();
            estiloCelda.setWrapText(true);
            estiloCelda.setFont(normal);
            estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THIN);
            if(n == 1)
            {
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
            }
            else
            {
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
            }
            if(numFila == listaPuestos.size())
            {
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
            }
            celda = fila.createCell(6);
            celda.setCellValue(filaI.getPais() != null ? filaI.getPais().toString() : "");
            celda.setCellStyle(estiloCelda);

            //COLUMNA: SALARIO ANUAL BRUTO + SS
            estiloCelda = libro.createCellStyle();
            estiloCelda.setWrapText(true);
            estiloCelda.setFont(normal);
            estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THIN);

            if(n == 1)
            {
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
            }
            else
            {
                //estiloCelda.setBorderTop(HSSFCellStyle.BORDER_DOUBLE);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
            }
            if(numFila == listaPuestos.size())
            {
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
            }
            celda = fila.createCell(7);
            //celda.setCellValue(filaI.getSalarioAnualBruto() != null ? filaI.getSalarioAnualBruto().toUpperCase() : "");
            celda.setCellStyle(estiloCelda);
            if (filaI.getSalarioAnualBruto()!= null){
                if (filaI.getSalarioAnualBruto()!= "-"){
                    Double aux=Double.parseDouble(filaI.getSalarioAnualBruto().replace(",","."));
                    celda.setCellValue(aux);
                    celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                }else{
                    celda.setCellValue(filaI.getSalarioAnualBruto());
                }
            }

            //COLUMNA: GASTOS SEGURO
            estiloCelda = libro.createCellStyle();
            estiloCelda.setWrapText(true);
            estiloCelda.setFont(normal);
            estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THIN);
            if(n == 1)
            {
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
            }
            else
            {
                //estiloCelda.setBorderTop(HSSFCellStyle.BORDER_DOUBLE);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
            }
            if(numFila == listaPuestos.size())
            {
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
            }
            celda = fila.createCell(8);
            //celda.setCellValue(filaI.getGastosVisadoPto() != null ? String.valueOf(filaI.getGastosVisadoPto()) : "");
            celda.setCellStyle(estiloCelda);
            if (filaI.getGastosVisadoPto()!= null){
                if (String.valueOf(filaI.getGastosVisadoPto())!= "-"){
                    Double aux=Double.parseDouble(String.valueOf(filaI.getGastosVisadoPto()).replace(",","."));
                    celda.setCellValue(aux);
                    celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                }else{
                    celda.setCellValue(String.valueOf(filaI.getGastosVisadoPto()));
                }
            }

            //COLUMNA: MESES EXTERIOR
            estiloCelda = libro.createCellStyle();
            estiloCelda.setWrapText(true);
            estiloCelda.setFont(normal);
            estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THIN);
            if(n == 1)
            {
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
            }
            else
            {
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
            }
            if(numFila == listaPuestos.size())
            {
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
            }
            celda = fila.createCell(9);
            //celda.setCellValue(filaI.getMesesExt()!= null ? filaI.getMesesExt().toUpperCase() : "");
            celda.setCellStyle(estiloCelda);
            if (filaI.getMesesExt()!= null){
                if (filaI.getMesesExt()!= "-"){
                    Double aux=Double.parseDouble(filaI.getMesesExt().replace(",","."));
                    celda.setCellValue(aux);
                    celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                }else{
                    celda.setCellValue(filaI.getMesesExt());
                }
            }

            //COLUMNA: GASTOS VISADO
            estiloCelda = libro.createCellStyle();
            estiloCelda.setWrapText(true);
            estiloCelda.setFont(normal);
            estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THIN);
            estiloCelda.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
            if(n == 1)
            {
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
            }
            else
            {
                //estiloCelda.setBorderTop(HSSFCellStyle.BORDER_DOUBLE);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
            }
            if(numFila == listaPuestos.size())
            {
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
            }
            celda = fila.createCell(10);
            celda.setCellValue(filaI.getGastosSeguroPto()!= null ? String.valueOf(filaI.getGastosSeguroPto()) : "");
            celda.setCellStyle(estiloCelda);
            //celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
            if (filaI.getGastosSeguroPto()!= null){
                if (String.valueOf(filaI.getGastosSeguroPto())!= "-"){
                    Double aux=Double.parseDouble(String.valueOf(filaI.getGastosSeguroPto()).replace(",","."));
                    celda.setCellValue(aux);
                    celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                }else{
                    celda.setCellValue(String.valueOf(filaI.getGastosSeguroPto()));
                }
            }

            //COLUMNA: IMP TOTAL CONCEDIDO
            estiloCelda = libro.createCellStyle();
            estiloCelda.setWrapText(true);
            estiloCelda.setFont(normal);
            estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THIN);
            estiloCelda.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
            if(n == 1)
            {
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
            }
            else
            {
                //estiloCelda.setBorderTop(HSSFCellStyle.BORDER_DOUBLE);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
            }
            if(numFila == listaPuestos.size())
            {
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
            }
            celda = fila.createCell(11);
            celda.setCellValue(filaI.getImporteConce() != null ? filaI.getImporteConce() : "");
            celda.setCellStyle(estiloCelda);
            //celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);

            //COLUMNA: PAIS FINAL
            estiloCelda = libro.createCellStyle();
            estiloCelda.setWrapText(true);
            estiloCelda.setFont(normal);
            estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THIN);
            if(n == 1)
            {
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
            }
            else
            {
                //estiloCelda.setBorderTop(HSSFCellStyle.BORDER_DOUBLE);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
            }
            if(numFila == listaPuestos.size())
            {
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
            }
            celda = fila.createCell(12);
            celda.setCellValue(filaI.getPaisDestino() != null ? filaI.getPaisDestino() : "");
            celda.setCellStyle(estiloCelda);

            //COLUMNA: CONTRATO INICIO
            estiloCelda = libro.createCellStyle();
            estiloCelda.setWrapText(true);
            estiloCelda.setFont(normal);
            estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THIN);
            if(n == 1)
            {
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
            }
            else
            {
                //estiloCelda.setBorderTop(HSSFCellStyle.BORDER_DOUBLE);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
            }
            if(numFila == listaPuestos.size())
            {
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
            }
            celda = fila.createCell(13);
            celda.setCellValue(filaI.getContratoInicio() != null ? filaI.getContratoInicio().toUpperCase() : "");
            celda.setCellStyle(estiloCelda);

            //COLUMNA: FIN PERIODO SUBVEN
            estiloCelda = libro.createCellStyle();
            estiloCelda.setWrapText(true);
            estiloCelda.setFont(normal);
            estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THIN);
            if(n == 1)
            {
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
            }
            else
            {
                //estiloCelda.setBorderTop(HSSFCellStyle.BORDER_DOUBLE);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
            }
            if(numFila == listaPuestos.size())
            {
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
            }
            celda = fila.createCell(14);
            celda.setCellValue(filaI.getFinPeriodoSubv() != null ? filaI.getFinPeriodoSubv().toUpperCase() : "");
            celda.setCellStyle(estiloCelda);

            //COLUMNA: DIAS SUBVENCIONABLES CONTRATO 
            estiloCelda = libro.createCellStyle();
            estiloCelda.setWrapText(true);
            estiloCelda.setFont(normal);
            estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THIN);

            if(n == 1)
            {
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
            }
            else
            {
                //estiloCelda.setBorderTop(HSSFCellStyle.BORDER_DOUBLE);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
            }
            if(numFila == listaPuestos.size())
            {
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
            }
            celda = fila.createCell(15);
            //celda.setCellValue(filaI.getDiasSubv() != null ? filaI.getDiasSubv() : "");
            celda.setCellStyle(estiloCelda);
            if (filaI.getDiasSubv()!= null){
                if (filaI.getDiasSubv()!= "-"){
                    Double aux=Double.parseDouble(filaI.getDiasSubv().replace(",","."));
                    celda.setCellValue(aux);
                    celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                }else{
                    celda.setCellValue(filaI.getDiasSubv());
                }
            }

            //COLUMNA: MESES EN EL EXTERIOR SUBV
            estiloCelda = libro.createCellStyle();
            estiloCelda.setWrapText(true);
            estiloCelda.setFont(normal);
            estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THIN);
            estiloCelda.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
            if(n == 1)
            {
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
            }
            else
            {
                //estiloCelda.setBorderTop(HSSFCellStyle.BORDER_DOUBLE);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
            }
            if(numFila == listaPuestos.size())
            {
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
            }
            celda = fila.createCell(16);
            celda.setCellValue(filaI.getMesesSubv() != null ? filaI.getMesesSubv() : "");
            celda.setCellStyle(estiloCelda);
            //celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);

            //COLUMNA: MOTIVO BAJA/DESPIDO
            estiloCelda = libro.createCellStyle();
            estiloCelda.setWrapText(true);
            estiloCelda.setFont(normal);
            estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THIN);
            estiloCelda.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
            if(n == 1)
            {
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
            }
            else
            {
                //estiloCelda.setBorderTop(HSSFCellStyle.BORDER_DOUBLE);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
            }
            if(numFila == listaPuestos.size())
            {
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
            }
            celda = fila.createCell(17);
            celda.setCellValue(filaI.getMotivoBaja()!= null ? filaI.getMotivoBaja() : "");
            celda.setCellStyle(estiloCelda);
            //celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);

            //COLUMNA: SALARIO ANUAL BRUTO +SS EMPRESA
            estiloCelda = libro.createCellStyle();
            estiloCelda.setWrapText(true);
            estiloCelda.setFont(normal);
            estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THIN);
            if(n == 1)
            {
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
            }
            else
            {
                //estiloCelda.setBorderTop(HSSFCellStyle.BORDER_DOUBLE);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
            }
            if(numFila == listaPuestos.size())
            {
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
            }
            celda = fila.createCell(18);
            //celda.setCellValue(filaI.getSalarioAnualBruto() != null ? filaI.getSalarioAnualBruto().toUpperCase() : "");
            celda.setCellStyle(estiloCelda);
            if (filaI.getSalarioAnualBruto()!= null){
                if (filaI.getSalarioAnualBruto()!= "-"){
                    Double aux=Double.parseDouble(filaI.getSalarioAnualBruto().replace(",","."));
                    celda.setCellValue(aux);
                    celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                }else{
                    celda.setCellValue(filaI.getSalarioAnualBruto());
                }
            }

            //COLUMNA: BONIFICACIONES
            estiloCelda = libro.createCellStyle();
            estiloCelda.setWrapText(true);
            estiloCelda.setFont(normal);
            estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THIN);
            if(n == 1)
            {
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
            }
            else
            {
                //estiloCelda.setBorderTop(HSSFCellStyle.BORDER_DOUBLE);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
            }
            if(numFila == listaPuestos.size())
            {
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
            }
            celda = fila.createCell(19);
            //celda.setCellValue(filaI.getImporteJus() != null ? filaI.getImporteJus() : "");
            celda.setCellStyle(estiloCelda);
            if (filaI.getImporteJus()!= null){
                if (filaI.getImporteJus()!= "-"){
                    Double aux=Double.parseDouble(filaI.getImporteJus().replace(",","."));
                    celda.setCellValue(aux);
                    celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                }else{
                    celda.setCellValue(filaI.getImporteJus());
                }
            }

            //COLUMNA: SALARIO ANUAL BRUTO + SS-EMPRESA
            estiloCelda = libro.createCellStyle();
            estiloCelda.setWrapText(true);
            estiloCelda.setFont(normal);
            estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THIN);
            if(n == 1)
            {
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
            }
            else
            {
                //estiloCelda.setBorderTop(HSSFCellStyle.BORDER_DOUBLE);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
            }
            if(numFila == listaPuestos.size())
            {
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
            }
            celda = fila.createCell(20);
            //celda.setCellValue(filaI.getSalarioSSBonif()!= null ? filaI.getSalarioSSBonif().replaceAll("\\.", ",") : "");
            celda.setCellStyle(estiloCelda);
            if (filaI.getSalarioSSBonif()!= null){
                if (filaI.getSalarioSSBonif()!= "-"){
                    Double aux=Double.parseDouble(filaI.getSalarioSSBonif().replace(",","."));
                    celda.setCellValue(aux);
                    celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                }else{
                    celda.setCellValue(filaI.getSalarioSSBonif());
                }
            }

            //COLUMNA: DIETAS ALOJAMIENTO Y MANUTENCIÃ?N
            estiloCelda = libro.createCellStyle();
            estiloCelda.setWrapText(true);
            estiloCelda.setFont(normal);
            estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THIN);
            estiloCelda.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
            if(n == 1)
            {
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
            }
            else
            {
                //estiloCelda.setBorderTop(HSSFCellStyle.BORDER_DOUBLE);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
            }
            if(numFila == listaPuestos.size())
            {
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
            }
            celda = fila.createCell(21);
           //celda.setCellValue(filaI.getGastosVisadoSub()!= null ? String.valueOf(filaI.getGastosVisadoSub()) : "");
            celda.setCellStyle(estiloCelda);
            //celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
            if (filaI.getGastosVisadoSub()!= null){
                if (String.valueOf(filaI.getGastosVisadoSub())!= "-"){
                    Double aux=Double.parseDouble(String.valueOf(filaI.getGastosVisadoSub()).replace(",","."));
                    celda.setCellValue(aux);
                    celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                }else{
                    celda.setCellValue(String.valueOf(filaI.getGastosVisadoSub()));
                }
            }

            //COLUMNA: TRAMITTACIÃ?N DE PERMISOS Y SEG MÃ?DICOS
            estiloCelda = libro.createCellStyle();
            estiloCelda.setWrapText(true);
            estiloCelda.setFont(normal);
            estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THIN);
            if(n == 1)
            {
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
            }
            else
            {
                //estiloCelda.setBorderTop(HSSFCellStyle.BORDER_DOUBLE);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
            }
            if(numFila == listaPuestos.size())
            {
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
            }
            celda = fila.createCell(22);
            //celda.setCellValue(filaI.getGastosSeguroSub()!= null ? String.valueOf(filaI.getGastosSeguroSub()) : "");
            celda.setCellStyle(estiloCelda);
            if (filaI.getGastosSeguroSub()!= null){
                if (String.valueOf(filaI.getGastosSeguroSub())!= "-"){
                    Double aux=Double.parseDouble(String.valueOf(filaI.getGastosSeguroSub()).replace(",","."));
                    celda.setCellValue(aux);
                    celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                }else{
                    celda.setCellValue(String.valueOf(filaI.getGastosSeguroSub()));
                }
            }

            //COLUMNA: TOTAL JUSTIFICADO
            estiloCelda = libro.createCellStyle();
            estiloCelda.setWrapText(true);
            estiloCelda.setFont(normal);
            estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THIN);
            if(n == 1)
            {
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
            }
            else
            {
                //estiloCelda.setBorderTop(HSSFCellStyle.BORDER_DOUBLE);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
            }
            if(numFila == listaPuestos.size())
            {
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
            }
            celda = fila.createCell(23);
            //celda.setCellValue(filaI.getImporteJus() != null ? filaI.getImporteJus().toUpperCase() : "");
            celda.setCellStyle(estiloCelda);
            if (filaI.getImporteJus()!= null){
                if (filaI.getImporteJus()!= "-"){
                    Double aux=Double.parseDouble(filaI.getImporteJus().replace(",","."));
                    celda.setCellValue(aux);
                    celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                }else{
                    celda.setCellValue(filaI.getImporteJus());
                }
            }
            

            //COLUMNA: MAXIMO SUBV SALARIO + SS (A)
            estiloCelda = libro.createCellStyle();
            estiloCelda.setWrapText(true);
            estiloCelda.setFont(normal);
            estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THIN);
            if(n == 1)
            {
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
            }
            else
            {
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
            }
            if(numFila == listaPuestos.size())
            {
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
            }
            celda = fila.createCell(24);
            //celda.setCellValue(filaI.getMaxSubvencion() != null ? filaI.getMaxSubvencion().toUpperCase() : "");
            celda.setCellStyle(estiloCelda);
            if (filaI.getMaxSubvencion()!= null){
                if (filaI.getMaxSubvencion()!= "-"){
                    Double aux=Double.parseDouble(filaI.getMaxSubvencion().replace(",","."));
                    celda.setCellValue(aux);
                    celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                }else{
                    celda.setCellValue(filaI.getMaxSubvencion());
                }
            }

            //COLUMNA: BONIFICACIONES(B)
            estiloCelda = libro.createCellStyle();
            estiloCelda.setWrapText(true);
            estiloCelda.setFont(normal);
            estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THIN);
            if(n == 1)
            {
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
            }
            else
            {
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
            }
            if(numFila == listaPuestos.size())
            {
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
            }
            celda = fila.createCell(25);
            //celda.setCellValue(filaI.getContratoBonif() != null ? filaI.getContratoBonif().toUpperCase().replaceAll("\\.", ",") : "");
            celda.setCellStyle(estiloCelda);
            if (filaI.getContratoBonif()!= null){
                if (filaI.getContratoBonif()!= "-"){
                    Double aux=Double.parseDouble(filaI.getContratoBonif().replace(",","."));
                    celda.setCellValue(aux);
                    celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                }else{
                    celda.setCellValue(filaI.getContratoBonif());
                }
            }
            
            //COLUMNA: GASTOS VISADO (C)
            estiloCelda = libro.createCellStyle();
            estiloCelda.setWrapText(true);
            estiloCelda.setFont(normal);
            estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THIN);
            if(n == 1)
            {
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
            }
            else
            {
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
            }
            if(numFila == listaPuestos.size())
            {
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
            }
            celda = fila.createCell(26);
            //celda.setCellValue(filaI.getGastosVisado()!= null ? filaI.getGastosVisado().toString() : "");
            celda.setCellStyle(estiloCelda);
            if (filaI.getGastosVisado()!= null){
                if (String.valueOf(filaI.getGastosVisado())!= "-"){
                    Double aux=Double.parseDouble(String.valueOf(filaI.getGastosVisado()).replace(",","."));
                    celda.setCellValue(aux);
                    celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                }else{
                    celda.setCellValue(String.valueOf(filaI.getGastosVisado()));
                }
            }

            //COLUMNA: GASTOS SEGURO (D)
            estiloCelda = libro.createCellStyle();
            estiloCelda.setWrapText(true);
            estiloCelda.setFont(normal);
            estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THIN);
            if(n == 1)
            {
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
            }
            else
            {
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
            }
            if(numFila == listaPuestos.size())
            {
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
            }
            celda = fila.createCell(27);
            //celda.setCellValue(filaI.getGastosSeguro()!= null ? filaI.getGastosSeguro().toString() : "");
            celda.setCellStyle(estiloCelda);
            if (filaI.getGastosSeguro()!= null){
                if (String.valueOf(filaI.getGastosSeguro())!= "-"){
                    Double aux=Double.parseDouble(String.valueOf(filaI.getGastosSeguro()).replace(",","."));
                    celda.setCellValue(aux);
                    celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                }else{
                    celda.setCellValue(String.valueOf(filaI.getGastosSeguro()));
                }
            }
            //COLUMNA: TOTAL SUBV ((A-B)+C+D)
            estiloCelda = libro.createCellStyle();
            estiloCelda.setWrapText(true);
            estiloCelda.setFont(normal);
            estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THIN);
            estiloCelda.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
            if(n == 1)
            {
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
            }
            else
            {
                //estiloCelda.setBorderTop(HSSFCellStyle.BORDER_DOUBLE);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
            }
            if(numFila == listaPuestos.size())
            {
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
            }
            celda = fila.createCell(28);
            //celda.setCellValue(filaI.getTotalSubv()!= null ? filaI.getTotalSubv() : "");
            celda.setCellStyle(estiloCelda);
            //celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
            if (filaI.getTotalSubv()!= null){
                if (filaI.getTotalSubv()!= "-"){
                    Double aux=Double.parseDouble(filaI.getTotalSubv().replace(",","."));
                    celda.setCellValue(aux);
                    celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                }else{
                    celda.setCellValue(filaI.getTotalSubv());
                }
            }
        }catch(Exception ex){
            log.error("Error: " + ex);
        }
        
    }
    
    private void crearEstiloInformeDatosPuestosHoja2_2016(HSSFWorkbook libro, HSSFRow fila, HSSFCell celda, HSSFCellStyle estiloCelda, int idioma)
    {
        try
        {
            MeLanbide46I18n meLanbide46I18n = MeLanbide46I18n.getInstance();
            
            HSSFPalette palette = libro.getCustomPalette();
            HSSFColor hssfColor = null;
            try 
            {
                hssfColor= palette.findColor((byte)75, (byte)149, (byte)211); 
                if (hssfColor == null )
                {
                    hssfColor = palette.getColor(HSSFColor.ROYAL_BLUE.index);
                }
                
                HSSFFont negrita = libro.createFont();
                negrita.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);

                HSSFFont normal = libro.createFont();
                normal.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);
                normal.setFontHeight((short)150);

                HSSFFont negritaTitulo = libro.createFont();
                negritaTitulo.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
                negritaTitulo.setFontHeight((short)170);
                negritaTitulo.setColor(HSSFColor.WHITE.index);

                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(0);
                celda.setCellValue(meLanbide46I18n.getMensaje(idioma, "doc.informeDatosPuestosHoja2.col1").toUpperCase());
                celda.setCellStyle(estiloCelda);

                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(1);
                celda.setCellValue(meLanbide46I18n.getMensaje(idioma, "doc.informeDatosPuestosHoja2.col2").toUpperCase());
                celda.setCellStyle(estiloCelda);

                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(2);
                celda.setCellValue(meLanbide46I18n.getMensaje(idioma, "doc.informeDatosPuestosHoja2.col3").toUpperCase());
                celda.setCellStyle(estiloCelda);

                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(3);
                celda.setCellValue(meLanbide46I18n.getMensaje(idioma, "doc.informeDatosPuestosHoja2.col4").toUpperCase());
                celda.setCellStyle(estiloCelda);

                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(4);
                celda.setCellValue(meLanbide46I18n.getMensaje(idioma, "doc.informeDatosPuestosHoja2.col5").toUpperCase());
                celda.setCellStyle(estiloCelda);

                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(5);
                celda.setCellValue(meLanbide46I18n.getMensaje(idioma, "doc.informeDatosPuestosHoja2.col6").toUpperCase());
                celda.setCellStyle(estiloCelda);

                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(6);
                celda.setCellValue(meLanbide46I18n.getMensaje(idioma, "doc.informeDatosPuestosHoja2.col7").toUpperCase());
                celda.setCellStyle(estiloCelda);

                estiloCelda = libro.createCellStyle();
                estiloCelda.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estiloCelda.setFillForegroundColor(hssfColor.getIndex());
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estiloCelda.setWrapText(true);
                estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estiloCelda.setFont(negritaTitulo);
                celda = fila.createCell(7);
                celda.setCellValue(meLanbide46I18n.getMensaje(idioma, "doc.informeDatosPuestosHoja2.col8").toUpperCase());
                celda.setCellStyle(estiloCelda);
            }
            catch (Exception e) 
            {
                e.printStackTrace();
            }

            
        }catch(Exception ex){
            
        }
        
    }
    
    private void crearDatosInformePuestosHoja2_2016 (HSSFWorkbook libro, HSSFRow fila, HSSFCell celda, HSSFCellStyle estiloCelda, int idioma, FilaInformeHoja2DatosPuestosVO filaI,
    String numExp, int numFila, boolean nuevo, int n, List<FilaInformeHoja2DatosPuestosVO> listaPuestos, 
    HSSFFont normal, int codOrganizacion, int ano, AdaptadorSQLBD adapt, SimpleDateFormat format)
    {
        try
        {
            String empresa = null;
            String idiomas = null;
            String pais = null;
            String titulacion = null;
                
            //COLUMNA: NUM_EXPEDIENTE
            estiloCelda = libro.createCellStyle();
            estiloCelda.setWrapText(true);
            estiloCelda.setFont(normal);
            //estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
            estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);

            if(n == 1)
            {
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
            }
            else
            {
                //estiloCelda.setBorderTop(HSSFCellStyle.BORDER_DOUBLE);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
            }
            if(numFila == listaPuestos.size())
            {
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
            }
            celda = fila.createCell(0);
            celda.setCellValue(filaI.getNumExpediente() != null ? filaI.getNumExpediente().toUpperCase() : "");
            celda.setCellStyle(estiloCelda);

            //COLUMNA: EMPRESA
            estiloCelda = libro.createCellStyle();
            estiloCelda.setWrapText(true);
            estiloCelda.setFont(normal);
            estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THIN);
            //estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);

            estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);

            if(numFila == listaPuestos.size())
            {
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
            }
            celda = fila.createCell(1);

            celda.setCellValue(filaI.getEmpresa() != null ? filaI.getEmpresa() : "");
            celda.setCellStyle(estiloCelda);

            //COLUMNA: IMPORTE CONCEDIDO
            estiloCelda = libro.createCellStyle();
            estiloCelda.setWrapText(true);
            estiloCelda.setFont(normal);
            estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THIN);

            if(n == 1)
            {
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
            }
            else
            {
                //estiloCelda.setBorderTop(HSSFCellStyle.BORDER_DOUBLE);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
            }

            if(numFila == listaPuestos.size())
            {
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
            }
            celda = fila.createCell(2);
            celda.setCellValue(filaI.getImporteConce()!= null ? filaI.getImporteConce().toUpperCase() : "");
            celda.setCellStyle(estiloCelda);

            //COLUMNA: IMPORTE SUBVENCIONABLE FINAL
            estiloCelda = libro.createCellStyle();
            estiloCelda.setWrapText(true);
            estiloCelda.setFont(normal);
            estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THIN);
            estiloCelda.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
            //estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
            if(n == 1)
            {
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
            }
            else
            {
                //estiloCelda.setBorderTop(HSSFCellStyle.BORDER_DOUBLE);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
            }
            if(numFila == listaPuestos.size())
            {
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
            }
            celda = fila.createCell(3);
            celda.setCellValue(filaI.getImporteSubv()!= null ? filaI.getImporteSubv() : "");
            //celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
            celda.setCellStyle(estiloCelda);


            //COLUMNA: PRIMER PAGO
            estiloCelda = libro.createCellStyle();
            estiloCelda.setWrapText(true);
            estiloCelda.setFont(normal);
            estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THIN);
            if(n == 1)
            {
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
            }
            else
            {
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
            }
            if(numFila == listaPuestos.size())
            {
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
            }
            celda = fila.createCell(4);
            celda.setCellValue(filaI.getPrimerPago()!= null ? filaI.getPrimerPago().toUpperCase() : "");
            celda.setCellStyle(estiloCelda);

            //COLUMNA: SEGUNDO PAGO
            estiloCelda = libro.createCellStyle();
            estiloCelda.setWrapText(true);
            estiloCelda.setFont(normal);
            estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THIN);
            if(n == 1)
            {
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
            }
            else
            {
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
            }
            if(numFila == listaPuestos.size())
            {
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
            }
            celda = fila.createCell(5);
            celda.setCellValue(filaI.getSegundoPago()!= null ? filaI.getSegundoPago().toString() : "");
            celda.setCellStyle(estiloCelda);

            //COLUMNA: D/
            estiloCelda = libro.createCellStyle();
            estiloCelda.setWrapText(true);
            estiloCelda.setFont(normal);
            estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THIN);

            if(n == 1)
            {
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
            }
            else
            {
                //estiloCelda.setBorderTop(HSSFCellStyle.BORDER_DOUBLE);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
            }
            if(numFila == listaPuestos.size())
            {
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
            }
            celda = fila.createCell(6);
            Double dato2 = 0.0;
            
            String impSubv = filaI.getImporteSubv().replace(",", ".");
            String impConce = filaI.getImporteConce().replace(",", ".");
            String pPago = filaI.getPrimerPago().replace(",", ".");
            String sPago = filaI.getSegundoPago().replace(",", ".");
            
            if(sPago.equals(""))
                sPago = "0";
            if(impConce.equals(""))
                impConce = "0";
            if(null != filaI.getImporteSubv() && null != filaI.getPrimerPago())
            {
                if(Double.valueOf(impSubv) <= Double.valueOf(pPago))
                {
                    dato2 = Double.valueOf(impConce) - Double.valueOf(pPago);
                }else{
                    dato2 = Double.valueOf(impConce) - Double.valueOf(pPago) - Double.valueOf(sPago);
                }
            }
            celda.setCellValue(dato2);
            celda.setCellStyle(estiloCelda);
            
            //COLUMNA: REINTEGRO
            estiloCelda = libro.createCellStyle();
            estiloCelda.setWrapText(true);
            estiloCelda.setFont(normal);
            estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THIN);

            if(n == 1)
            {
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
            }
            else
            {
                //estiloCelda.setBorderTop(HSSFCellStyle.BORDER_DOUBLE);
                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
            }
            if(numFila == listaPuestos.size())
            {
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
            }
            celda = fila.createCell(7);
            Double dato = 0.0;
            if(null != filaI.getImporteSubv() && null != filaI.getPrimerPago())
            {
                if(Double.valueOf(pPago) > Double.valueOf(impSubv))
                {
                    dato = Double.valueOf(pPago) - Double.valueOf(impSubv);
                }
            }
            celda.setCellValue(dato);
            celda.setCellStyle(estiloCelda);
        }catch(Exception ex){
            log.error("Error: " + ex);
        }
        
    }
}

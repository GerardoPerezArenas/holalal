/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
//prueba
package es.altia.flexia.integracion.moduloexterno.melanbide59;

import es.altia.agora.business.escritorio.UsuarioValueObject;
import es.altia.agora.business.terceros.CondicionesBusquedaTerceroVO;
import es.altia.agora.business.terceros.TercerosValueObject;
import es.altia.agora.business.terceros.persistence.TercerosManager;
import es.altia.agora.technical.ConstantesDatos;
import es.altia.common.exception.TechnicalException;
import es.altia.flexia.integracion.moduloexterno.melanbide59.i18n.MeLanbide59I18n;
import es.altia.flexia.integracion.moduloexterno.melanbide59.manager.MeLanbide59Manager;
import es.altia.flexia.integracion.moduloexterno.melanbide59.util.ConfigurationParameter;
import es.altia.flexia.integracion.moduloexterno.melanbide59.util.ConstantesMeLanbide59;
import es.altia.flexia.integracion.moduloexterno.melanbide59.util.MeLanbide59MappingUtils;
import es.altia.flexia.integracion.moduloexterno.melanbide59.util.MeLanbide59Utils;
import es.altia.flexia.integracion.moduloexterno.melanbide59.util.MeLanbide59Validator;
import es.altia.flexia.integracion.moduloexterno.melanbide59.vo.CpeJustificacionVO;
import es.altia.flexia.integracion.moduloexterno.melanbide59.vo.CpeOfertaVO;
import es.altia.flexia.integracion.moduloexterno.melanbide59.vo.CpePuestoVO;
import es.altia.flexia.integracion.moduloexterno.melanbide59.vo.FilaInformeDatosPuestosVO;
import es.altia.flexia.integracion.moduloexterno.melanbide59.vo.FilaInformeHoja2DatosPuestosVO;
import es.altia.flexia.integracion.moduloexterno.melanbide59.vo.FilaInformeResumenEconomicoVO;
import es.altia.flexia.integracion.moduloexterno.melanbide59.vo.FilaInformeResumenPuestosContratadosVO;
import es.altia.flexia.integracion.moduloexterno.melanbide59.vo.FilaJustificacionVO;
import es.altia.flexia.integracion.moduloexterno.melanbide59.vo.FilaOfertaVO;
import es.altia.flexia.integracion.moduloexterno.melanbide59.vo.FilaPersonaContratadaVO;
import es.altia.flexia.integracion.moduloexterno.melanbide59.vo.FilaPuestoVO;
import es.altia.flexia.integracion.moduloexterno.melanbide59.vo.FilaResultadoBusqTitulaciones;
import es.altia.flexia.integracion.moduloexterno.melanbide59.vo.FilaResumenVO;
import es.altia.flexia.integracion.moduloexterno.melanbide59.vo.SalarioVO;
import es.altia.flexia.integracion.moduloexterno.melanbide59.vo.SelectItem;
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
import javax.swing.text.html.CSS;
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
public class MELANBIDE59 extends ModuloIntegracionExterno 
{
    //Logger
    private static Logger log = LogManager.getLogger(MELANBIDE59.class);
    ResourceBundle m_Conf = ResourceBundle.getBundle("common");
    
    public String lanzaHistorico (int codOrganizacion,int codTramite,int ocurrenciaTramite, String numExpediente)
   {
        if(log.isDebugEnabled()) log.debug("lanzaHistorico ( codOrganizacion = " + codOrganizacion + " codTramite = " + codTramite + " ocurrenciaTramite = " + ocurrenciaTramite + " numExpediente = " + numExpediente  + " ) : BEGIN");
       String clave= null;
        String codigoOperacion = "0";
        String mensaje = "";
        
        try{           
            if(numExpediente!=null && !"".equals(numExpediente)){
                AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
                                                     
                 MeLanbide59Manager.getInstance().guardaHistorico(numExpediente, String.valueOf(codTramite), adapt);
            }
        }
        catch(Exception ex)
        {
            codigoOperacion = "2";
        }
        if(log.isDebugEnabled()) log.debug("lanzaHistorico() : END");
        return codigoOperacion;
    }
    
    public String borrarHistorico (int codOrganizacion,int codTramite,int ocurrenciaTramite, String numExpediente)
   {
        if(log.isDebugEnabled()) log.debug("borrarHistorico ( codOrganizacion = " + codOrganizacion + " codTramite = " + codTramite + " ocurrenciaTramite = " + ocurrenciaTramite + " numExpediente = " + numExpediente  + " ) : BEGIN");
       String clave= null;
        String codigoOperacion = "0";
        String mensaje = "";
        
        try{           
            if(numExpediente!=null && !"".equals(numExpediente)){
                AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
                                                     
                 MeLanbide59Manager.getInstance().borraHistorico(numExpediente, String.valueOf(codTramite), adapt);
            }
        }
        catch(Exception ex)
        {
            codigoOperacion = "2";
        }
        if(log.isDebugEnabled()) log.debug("borrarHistorico() : END");
        return codigoOperacion;
    }
    
    public String cargarPantallaDatosCpe(int codOrganizacion,  int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response)
    {
        //Cargo los datos CPE
        cargarDatosCpe(codOrganizacion, numExpediente, request);
        
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
            url = cargarSubpestanaSolicitud_DatosCpe(numExpediente, adapt, request);
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
            url = cargarSubpestanaOferta_DatosCpe(numExpediente, adapt, request);
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
            url = cargarSubpestanaJustificacion_DatosCpe(numExpediente, adapt, request);
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
            url = cargarSubpestanaResumen_DatosCpe(numExpediente, adapt, request);
            if(url != null)
            {
                request.setAttribute("urlPestanaDatos_resumen", url);
            }
        }
        catch(Exception ex)
        {

        }
        return "/jsp/extension/melanbide59/datosCpe.jsp";
    }
    
    //Guarda los importes de la parte superior
    public void guardarDatosCpe(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response)
    {
        String codigoOperacion = "0";
        Map<String, BigDecimal> calculos = new HashMap<String, BigDecimal>();
        try
        {
            if(validarDatosCpe(request))
            {
                Integer ejercicio = null;
                try
                {
                    ejercicio = MeLanbide59Utils.getEjercicioDeExpediente(numExpediente);
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
                        BigDecimal importeRenunciaRes = null;
                        BigDecimal impReiNum = null;
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
                            importeRenunciaRes = impRenRes != null && !impRenRes.equals("") ? new BigDecimal(impRenRes.replaceAll(",", "\\.")) : null;
                            impReiNum = impRei != null && !impRei.equals("") ? new BigDecimal(impRei.replaceAll(",", "\\.")) : null;
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
                            res = MeLanbide59Manager.getInstance().guardarDatosCpe(codOrganizacion, ejeStr, numExpediente, gestor, empresa, impPagadoNum, impPagadoNum2, otrasAyudasSolicNum, otrasAyudasConceNum, minimisSolicNum, minimisConceNum, importeConcedidoNum,importeRenunciaRes, impReiNum, adaptador);
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
                    
                    calculos = MeLanbide59Manager.getInstance().cargarCalculosCpe(codOrganizacion, ejercicio, numExpediente, adaptador);
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
        
        escribirCalculosCpeRequest(codigoOperacion, calculos, response);
        
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
        return "/jsp/extension/melanbide59/nuevoPuesto.jsp?codOrganizacionModulo="+codOrganizacion;
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
                    Integer ejercicio = MeLanbide59Utils.getEjercicioDeExpediente(numExpediente);
                    CpePuestoVO puesto = new CpePuestoVO();
                    puesto.setCodPuesto(codPuesto);
                    puesto.setEjercicio(ejercicio);
                    puesto.setNumExp(numExpediente);
                    puesto = MeLanbide59Manager.getInstance().getPuestoPorCodigoYExpediente(puesto, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                    if(puesto != null)
                    {
                        request.setAttribute("puestoModif", puesto);
                        
                        AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
                        String desc = null;
                        if(puesto.getCodTit1() != null && !puesto.getCodTit1().equals(""))
                        {
                            desc = MeLanbide59Manager.getInstance().getDescripcionTitulacion(puesto.getCodTit1(), adapt);
                            if(desc != null)
                            {
                                request.setAttribute("descTit1", desc);
                            }
                        }
                        if(puesto.getCodTit2() != null && !puesto.getCodTit2().equals(""))
                        {
                            desc = MeLanbide59Manager.getInstance().getDescripcionTitulacion(puesto.getCodTit2(), adapt);
                            if(desc != null)
                            {
                                request.setAttribute("descTit2", desc);
                            }
                        }
                        if(puesto.getCodTit3() != null && !puesto.getCodTit3().equals(""))
                        {
                            desc = MeLanbide59Manager.getInstance().getDescripcionTitulacion( puesto.getCodTit3(), adapt);
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
        return "/jsp/extension/melanbide59/nuevoPuesto.jsp?codOrganizacionModulo="+codOrganizacion;
    }
    
    public void cargarSalarioAnexo1(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response)
    {
        String codGrCot = request.getParameter("codGrCot");
        String codPaisStr = request.getParameter("codPais");
        Integer codPais = null;
        SalarioVO salario  = null;
        if(codGrCot != null && !codGrCot.equals("") && codPaisStr != null && !codPaisStr.equals(""))
        {
            try
            {
                codPais = Integer.parseInt(codPaisStr);
                salario = MeLanbide59Manager.getInstance().getSalarioAnexo1PorPaisGrupoCot(codPais, codGrCot, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
            }
            catch(Exception ex)
            {
                ex.printStackTrace();
            }
        }
        /*if(salario == null)
        {
            salario = new SalarioVO();
            salario.setCodGrCot(codGrCot);
            salario.setCodPai(codPais);
            salario.setDescripcion("");
            salario.setImporte(new BigDecimal("0"));
        }*/
        StringBuffer xmlSalida = new StringBuffer();
        xmlSalida.append("<RESPUESTA>");
            xmlSalida.append("<CODIGO_OPERACION>");
                xmlSalida.append("0");
            xmlSalida.append("</CODIGO_OPERACION>");
            if(salario != null)
            {
                xmlSalida.append("<SALARIO_ANEXO_1>");
                    xmlSalida.append("<COD_PAIS>");
                            xmlSalida.append(salario.getCodPai() != null ? salario.getCodPai().toString() : "");
                    xmlSalida.append("</COD_PAIS>");
                    xmlSalida.append("<COD_GR_COT>");
                            xmlSalida.append(salario.getCodGrCot());
                    xmlSalida.append("</COD_GR_COT>");
                    xmlSalida.append("<DESCRIPCION>");
                            xmlSalida.append(salario.getDescripcion());
                    xmlSalida.append("</DESCRIPCION>");
                    xmlSalida.append("<IMPORTE>");
                            xmlSalida.append(salario.getImporte() != null ? salario.getImporte().toPlainString() : "0");
                    xmlSalida.append("</IMPORTE>");
                xmlSalida.append("</SALARIO_ANEXO_1>");
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
    
    public void guardarPuesto(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response)
    {
        String codigoOperacion = "0";
        List<FilaPuestoVO> puestos = new ArrayList<FilaPuestoVO>();
        Map<String, BigDecimal> calculos = new HashMap<String, BigDecimal>();
        
        AdaptadorSQLBD adaptador = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
        try
        {
            
            Integer ejercicio = MeLanbide59Utils.getEjercicioDeExpediente(numExpediente);
            if(ejercicio != null)
            {
                SimpleDateFormat format = new SimpleDateFormat(ConstantesMeLanbide59.FORMATO_FECHA);
                
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
                //String dietasManutencion = (String)request.getParameter("dietasManutencion");
                String mesesManutencion = (String)request.getParameter("mesesManutencion");
                //String tramSeguros = (String)request.getParameter("tramSeguros");
                String impSolic = (String)request.getParameter("impSolic");
                String fechaIni = (String)request.getParameter("fechaIni");
                String fechaFin = (String)request.getParameter("fechaFin");
                String mesesContrato = (String)request.getParameter("mesesContrato");
                String grupoCot = (String)request.getParameter("codGrupoCot");
                String convenioCol = (String)request.getParameter("convenioCol");
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
                
                //nuevos campos
                String gastosSeguro = (String)request.getParameter("gastosSeguro");
                String gastosVisado = (String)request.getParameter("gastosVisado");
                String gastosSeguroCon = (String)request.getParameter("gastosSeguroCon");
                String gastosVisadoCon = (String)request.getParameter("gastosVisadoCon");

                CpePuestoVO puesto = null;
                if(codPuesto != null && !codPuesto.equals(""))
                {
                    puesto = new CpePuestoVO();
                    puesto.setCodPuesto(codPuesto);
                    puesto.setNumExp(numExpediente);
                    puesto.setEjercicio(ejercicio);
                    puesto = MeLanbide59Manager.getInstance().getPuestoPorCodigoYExpediente(puesto, adaptador);
                }
                else
                {
                    puesto = new CpePuestoVO();
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
                    
                    if(resultado != null && (resultado.equalsIgnoreCase(ConstantesMeLanbide59.CODIGO_RESULTADO_DENEGADO) || resultado.equalsIgnoreCase(ConstantesMeLanbide59.CODIGO_RESULTADO_RENUNCIA)))
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
                    puesto.setGastosSeguroCon(gastosSeguroCon != null && !gastosSeguroCon.equals("") ? new BigDecimal(gastosSeguroCon.replaceAll(",", "\\.")) : null);
                    puesto.setGastosVisadoCon(gastosVisadoCon != null && !gastosVisadoCon.equals("") ? new BigDecimal(gastosVisadoCon.replaceAll(",", "\\.")) : null);
                    //puesto.setDietasManut(dietasManutencion != null && !dietasManutencion.equals("") ? new BigDecimal(dietasManutencion.replaceAll(",", "\\.")) : null);
                    puesto.setGastosSeguro(gastosSeguro != null && !gastosSeguro.equals("") ? new BigDecimal(gastosSeguro.replaceAll(",", "\\.")) : null);
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
                    puesto.setSalBruto(salarioBruto != null && !salarioBruto.equals("") ? new BigDecimal(salarioBruto.replaceAll(",", "\\.")) : null);
                    puesto.setSalarioAnex1(salarioAnexo1 != null && !salarioAnexo1.equals("") ? new BigDecimal(salarioAnexo1.replaceAll(",", "\\.")) : null);
                    puesto.setSalarioAnex2(salarioAnexo2 != null && !salarioAnexo2.equals("") ? new BigDecimal(salarioAnexo2.replaceAll(",", "\\.")) : null);
                    puesto.setSalarioAnex3(salarioAnexo3 != null && !salarioAnexo3.equals("") ? new BigDecimal(salarioAnexo3.replaceAll(",", "\\.")) : null);
                    puesto.setSalarioAnexTot(salarioAnexoTot != null && !salarioAnexoTot.equals("") ? new BigDecimal(salarioAnexoTot.replaceAll(",", "\\.")) : null);
                    //puesto.setTramSeguros(tramSeguros != null && !tramSeguros.equals("") ? new BigDecimal(tramSeguros.replaceAll(",", "\\.")) : null);
                    puesto.setGastosVisado(gastosVisado != null && !gastosVisado.equals("") ? new BigDecimal(gastosVisado.replaceAll(",", "\\.")) : null);
                    
                    
                    puesto.setSalSolic(salarioSolicitud != null && !salarioSolicitud.equals("") ? new BigDecimal(salarioSolicitud.replaceAll(",", "\\.")) : null);
                    puesto.setDietasSolic(dietasSolicitud != null && !dietasSolicitud.equals("") ? new BigDecimal(dietasSolicitud.replaceAll(",", "\\.")) : null);
                    puesto.setTramSolic(tramitacionSolicitud != null && !tramitacionSolicitud.equals("") ? new BigDecimal(tramitacionSolicitud.replaceAll(",", "\\.")) : null);
                    puesto.setImpTotSolic(impTotSolicitud != null && !impTotSolicitud.equals("") ? new BigDecimal(impTotSolicitud.replaceAll(",", "\\.")) : null);
                    
                    
                    //TODO: se podria validar los datos del puesto
                    
                    
                    MeLanbide59Manager.getInstance().guardarCpePuestoVO(codOrganizacion, puesto, impAnterior, estadoAnterior, adaptador);
                }
                calculos = MeLanbide59Manager.getInstance().cargarCalculosCpe(codOrganizacion, ejercicio, numExpediente, adaptador);
                puestos = MeLanbide59Manager.getInstance().getListaPuestosPorExpediente(numExpediente, adaptador);
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
        escribirListaPuestosRequest(codigoOperacion, puestos, calculos, response);
    }
    
    public void eliminarPuesto(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response)
    {
        String codigoOperacion = "0";
        List<FilaPuestoVO> puestos = new ArrayList<FilaPuestoVO>();
        Map<String, BigDecimal> calculos = new HashMap<String, BigDecimal>();
        
        AdaptadorSQLBD adaptador = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
        try
        {
            
            Integer ejercicio = MeLanbide59Utils.getEjercicioDeExpediente(numExpediente);
            if(ejercicio != null)
            {
                
                String codPuesto = (String)request.getParameter("idPuesto");

                CpePuestoVO puesto = null;
                if(codPuesto != null && !codPuesto.equals(""))
                {
                    puesto = new CpePuestoVO();
                    puesto.setCodPuesto(codPuesto);
                    puesto.setNumExp(numExpediente);
                    puesto.setEjercicio(ejercicio);
                    puesto = MeLanbide59Manager.getInstance().getPuestoPorCodigoYExpediente(puesto, adaptador);
                }
                if(puesto == null)
                {
                    codigoOperacion = "3";
                }
                else
                {
                    MeLanbide59Manager.getInstance().eliminarCpePuestoVO(codOrganizacion, puesto, adaptador);
                }
                calculos = MeLanbide59Manager.getInstance().cargarCalculosCpe(codOrganizacion, ejercicio, numExpediente, adaptador);
                puestos = MeLanbide59Manager.getInstance().getListaPuestosPorExpediente(numExpediente, adaptador);
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
        escribirListaPuestosRequest(codigoOperacion, puestos, calculos, response);
    }
    
    public String cargarNuevaOferta(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response)
    {
        cargarCombosNuevaOferta(codOrganizacion, request);
        return "/jsp/extension/melanbide59/nuevaOferta.jsp?codOrganizacionModulo="+codOrganizacion;
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
                    Integer ejercicio = MeLanbide59Utils.getEjercicioDeExpediente(numExpediente);
                    CpeOfertaVO oferta = new CpeOfertaVO();
                    oferta.setIdOferta(Integer.parseInt(codOferta));
                    oferta.setCodPuesto(codPuesto);
                    oferta.setExpEje(ejercicio);
                    oferta.setNumExp(numExpediente);
                    oferta = MeLanbide59Manager.getInstance().getOfertaPorCodigoPuestoYExpediente(oferta, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                    if(oferta != null)
                    {
                         //pruebas copiardatos
                       /* String copiardatos = (String)request.getParameter("copiardatos");
                        CpePuestoVO origenpuesto = new CpePuestoVO();                
                        origenpuesto.setCodPuesto(codPuesto);
                        origenpuesto.setEjercicio(ejercicio);
                        origenpuesto.setNumExp(numExpediente);
                        origenpuesto = MeLanbide59Manager.getInstance().getPuestoPorCodigoYExpediente(origenpuesto, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                                              
                        if (copiardatos!=null)       
                                oferta = MeLanbide59Manager.getInstance().copiarDatosPuesto(codOrganizacion, origenpuesto, oferta, this.getAdaptSQLBD(String.valueOf(codOrganizacion))); 
                        //fin copiardatos
                        */
                        
                        request.setAttribute("ofertaModif", oferta);
                        if(oferta.getCodPuesto() != null && !oferta.getCodPuesto().equals(""))
                        {
                            CpePuestoVO puesto = new CpePuestoVO();
                            puesto.setCodPuesto(oferta.getCodPuesto());
                            puesto.setEjercicio(ejercicio);
                            puesto.setNumExp(numExpediente);
                            puesto = MeLanbide59Manager.getInstance().getPuestoPorCodigoYExpediente(puesto, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                            if(puesto != null)
                            {
                                request.setAttribute("puestoConsulta", puesto);
                            }
                        }
                        if(oferta.getIdOfertaOrigen() != null)
                        {
                            CpeOfertaVO ofertaOrigen = new CpeOfertaVO();
                            ofertaOrigen.setIdOferta(oferta.getIdOfertaOrigen());
                            ofertaOrigen.setExpEje(ejercicio);
                            ofertaOrigen.setNumExp(numExpediente);
                            ofertaOrigen = MeLanbide59Manager.getInstance().getOfertaPorCodigoPuestoYExpediente(ofertaOrigen, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                            if(ofertaOrigen != null)
                            {
                                request.setAttribute("ofertaOrigen", ofertaOrigen);
                            }
                        }
                        
                        AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
                        String desc = null;
                        if(oferta.getCodTit1() != null && !oferta.getCodTit1().equals(""))
                        {
                            desc = MeLanbide59Manager.getInstance().getDescripcionTitulacion( oferta.getCodTit1(), adapt);
                            if(desc != null)
                            {
                                request.setAttribute("descTit1", desc);
                            }
                        }
                        if(oferta.getCodTit2() != null && !oferta.getCodTit2().equals(""))
                        {
                            desc = MeLanbide59Manager.getInstance().getDescripcionTitulacion( oferta.getCodTit2(), adapt);
                            if(desc != null)
                            {
                                request.setAttribute("descTit2", desc);
                            }
                        }
                        if(oferta.getCodTit3() != null && !oferta.getCodTit3().equals(""))
                        {
                            desc = MeLanbide59Manager.getInstance().getDescripcionTitulacion(oferta.getCodTit3(), adapt);
                            if(desc != null)
                            {
                                request.setAttribute("descTit3", desc);
                            }
                        }
                        if(oferta.getCodTitulacion() != null && !oferta.getCodTitulacion().equals(""))
                        {
                            desc = MeLanbide59Manager.getInstance().getDescripcionTitulacion( oferta.getCodTitulacion(), adapt);
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
        return "/jsp/extension/melanbide59/nuevaOferta.jsp?codOrganizacionModulo="+codOrganizacion;
    }
    
    public void getListaOfertasNoDenegadasPorExpediente(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response)
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
        List<FilaOfertaVO> ofertas = new ArrayList<FilaOfertaVO>();
        Map<String, BigDecimal> calculos = new HashMap<String, BigDecimal>();
        try
        {
            Integer ejercicio = MeLanbide59Utils.getEjercicioDeExpediente(numExpediente);
            if(ejercicio != null)
            {
                AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
                ofertas = MeLanbide59Manager.getInstance().getListaOfertasNoDenegadasPorExpediente(numExpediente, adapt);
                calculos = MeLanbide59Manager.getInstance().cargarCalculosCpe(codOrganizacion, ejercicio, numExpediente, adapt);
            }
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        
        escribirListaOfertasRequest(codigoOperacion, ofertas, calculos, response);
    }
    
    public void getListaJustificacionesNoDenegadasPorExpediente(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response)
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
        List<FilaJustificacionVO> justificaciones = new ArrayList<FilaJustificacionVO>();
        Map<String, BigDecimal> calculos = new HashMap<String, BigDecimal>();
        try
        {
            Integer ejercicio = MeLanbide59Utils.getEjercicioDeExpediente(numExpediente);
            if(ejercicio != null)
            {
                AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
                justificaciones = MeLanbide59Manager.getInstance().getListaJustificacionesNoDenegadasPorExpediente(numExpediente, adapt);
                calculos = MeLanbide59Manager.getInstance().cargarCalculosCpe(codOrganizacion, ejercicio, numExpediente, adapt);
            }
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        
        BigDecimal justificado = new BigDecimal(0);
        BigDecimal jus = null;
        try
        {
            String coste="";
            AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
            List<FilaResumenVO> filasResumen = MeLanbide59Manager.getInstance().getListaResumenPorExpediente(numExpediente, adapt);
            for(FilaResumenVO fila : filasResumen)
            {
                coste = fila.getCosteSubvecionable();
                coste = coste.replace(",", ".");
                if(!coste.equals("")){
                    jus = new BigDecimal(coste);
                }
                justificado=justificado.add(jus);
            }
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
            log.error("Error al actualizar el importe justificado del campo suplementario: " + ex.getMessage());
        }
        calculos.put(ConstantesMeLanbide59.CAMPO_SUPL_IMP_JUS2, justificado);
        
        escribirListaJustificacionesRequest(codigoOperacion, justificaciones, calculos, response);
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
            Integer ejercicio = MeLanbide59Utils.getEjercicioDeExpediente(numExpediente);
            if(ejercicio != null)
            {
                AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
                filasResumen = MeLanbide59Manager.getInstance().getListaResumenPorExpediente(numExpediente, adapt);
                //calculos = MeLanbide59Manager.getInstance().cargarCalculosCpe(codOrganizacion, ejercicio, numExpediente, adapt);
            }
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        
        escribirListaResumenRequest(codigoOperacion, filasResumen, response);
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
        return "/jsp/extension/melanbide59/nuevaJustificacion.jsp?codOrganizacionModulo="+codOrganizacion;
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
                Integer ejercicio = MeLanbide59Utils.getEjercicioDeExpediente(numExpediente);
                CpePuestoVO puesto = null;
                if(codPuesto != null && !codPuesto.equals(""))
                {
                    puesto = new CpePuestoVO();
                    puesto.setCodPuesto(codPuesto);
                    puesto.setEjercicio(ejercicio);
                    puesto.setNumExp(numExpediente);
                    puesto = MeLanbide59Manager.getInstance().getPuestoPorCodigoYExpediente(puesto, adapt);
                    if(puesto != null)
                    {
                        request.setAttribute("puestoConsulta", puesto);
                        
                        List<FilaPersonaContratadaVO> contrataciones = MeLanbide59Manager.getInstance().getListaContratacionesPuesto(puesto, adapt);
                        //int resultado = MeLanbide59Manager.getInstance().guardarValorCampoNumerico(codOrganizacion, ejercicio.toString(), numExpediente, "IMPJUS", calculos.get(ConstantesMeLanbide59.CAMPO_SUPL_IMP_JUS), adaptador);
                        if(contrataciones != null)
                        {
                            request.setAttribute("listaContrataciones", contrataciones);
                        }
                    }
                    CpeOfertaVO ofertaConsulta = new CpeOfertaVO();
                    ofertaConsulta.setExpEje(ejercicio);
                    ofertaConsulta.setCodPuesto(codPuesto);
                    ofertaConsulta.setNumExp(numExpediente);
                    ofertaConsulta = MeLanbide59Manager.getInstance().getUltimaOfertaPorPuestoYExpediente(ofertaConsulta, adapt);
                    if(ofertaConsulta != null)
                    {
                        request.setAttribute("ofertaConsulta", ofertaConsulta);
                        
                        String desc = null;
                        if(ofertaConsulta.getCodTit1() != null && !ofertaConsulta.getCodTit1().equals(""))
                        {
                            desc = MeLanbide59Manager.getInstance().getDescripcionTitulacion( ofertaConsulta.getCodTit1(), adapt);
                            if(desc != null)
                            {
                                request.setAttribute("descTit1", desc);
                            }
                        }
                        if(ofertaConsulta.getCodTit2() != null && !ofertaConsulta.getCodTit2().equals(""))
                        {
                            desc = MeLanbide59Manager.getInstance().getDescripcionTitulacion(ofertaConsulta.getCodTit2(), adapt);
                            if(desc != null)
                            {
                                request.setAttribute("descTit2", desc);
                            }
                        }
                        if(ofertaConsulta.getCodTit3() != null && !ofertaConsulta.getCodTit3().equals(""))
                        {
                            desc = MeLanbide59Manager.getInstance().getDescripcionTitulacion(ofertaConsulta.getCodTit3(), adapt);
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
        return "/jsp/extension/melanbide59/nuevaJustificacion.jsp?codOrganizacionModulo="+codOrganizacion;
    }
    
    public void generarInformeDatosPuestos(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response)
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
            
            MeLanbide59I18n meLanbide59I18n = MeLanbide59I18n.getInstance();
            MeLanbide59Manager manager = MeLanbide59Manager.getInstance();
            SimpleDateFormat format = new SimpleDateFormat(ConstantesMeLanbide59.FORMATO_FECHA);
            FileOutputStream archivoSalida = null;
            FileInputStream istr = null;
            try 
            {
                HSSFWorkbook libro = new HSSFWorkbook();
                //se escribe el fichero
                File directorioTemp = new File(m_Conf.getString("PDF.base_dir"));
                log.debug("directorioTemp" + directorioTemp.toString());
                DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
                //get current date time with Date()
                Date date = new Date();
                String fechaAct=dateFormat.format(date);
           
                File informe= new File("resumenDatosPuestos_"+fechaAct+".xls");

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
                hoja.setColumnWidth((short)0, (short)6000);//Num. expediente
                hoja.setColumnWidth((short)1, (short)10000);//empresa
                hoja.setColumnWidth((short)2, (short)8000);//persona contratada
                hoja.setColumnWidth((short)3, (short)3000);//sexo
                hoja.setColumnWidth((short)4, (short)3000);//nivel formativo
                hoja.setColumnWidth((short)5, (short)8000);//país
                hoja.setColumnWidth((short)6, (short)5000);//salario anual bruto + ss
                hoja.setColumnWidth((short)7, (short)5000);//dietas aloj y manutención
                hoja.setColumnWidth((short)8, (short)3000);//meses exterior
                hoja.setColumnWidth((short)9, (short)4000);//tramitación permisos
                hoja.setColumnWidth((short)10, (short)6000);//importe concedido
                hoja.setColumnWidth((short)11, (short)6000);//país final
                hoja.setColumnWidth((short)12, (short)5000);//salario anexo
                hoja.setColumnWidth((short)13, (short)4500);//contrato inicio
                hoja.setColumnWidth((short)14, (short)3000);//fin periodo subv
                hoja.setColumnWidth((short)15, (short)5000);//días subv contrato
                hoja.setColumnWidth((short)16, (short)5000);//meses ext subv
                hoja.setColumnWidth((short)17, (short)5000);//motivo baja
                hoja.setColumnWidth((short)18, (short)5000);//Salario anual bruto + ss emp jus
                hoja.setColumnWidth((short)19, (short)5000);//Bonificaciones jus
                hoja.setColumnWidth((short)20, (short)5000);//Salario + SS bonif
                hoja.setColumnWidth((short)21, (short)5000);//dietas aloj y manu jus
                hoja.setColumnWidth((short)22, (short)5000);//tram permisos jus
                hoja.setColumnWidth((short)23, (short)5000);//total justi
                hoja.setColumnWidth((short)24, (short)5000);//max subv
                hoja.setColumnWidth((short)25, (short)5000);//bonificaciones
                hoja.setColumnWidth((short)26, (short)5000);//dietas aloj y manu subv
                hoja.setColumnWidth((short)27, (short)5000);//tram permisos subv
                hoja.setColumnWidth((short)28, (short)5000);//total subv
                
                hoja2.setColumnWidth((short)0, (short)6000);//Num. expediente
                hoja2.setColumnWidth((short)1, (short)10000);//empresa
                hoja2.setColumnWidth((short)2, (short)8000);//persona contratada
                hoja2.setColumnWidth((short)3, (short)5000);//sexo
                hoja2.setColumnWidth((short)4, (short)10000);//nivel formativo
                hoja2.setColumnWidth((short)5, (short)8000);//país
                hoja2.setColumnWidth((short)6, (short)12000);//salario anual bruto + ss
                
                
                fila = hoja.createRow(numFila);
                
                //Se añaden los titulos de las columnas
                    //cambiar poi 3.10
                    /*hoja.addMergedRegion(new Region(numFila, (short)7, numFila, (short)11));
                hoja.addMergedRegion(new Region(numFila, (short)18, numFila, (short)23));
                hoja.addMergedRegion(new Region(numFila, (short)24, numFila, (short)28));
                    */
                    hoja.addMergedRegion(new CellRangeAddress(numFila,  numFila, 7, 11));
                    hoja.addMergedRegion(new CellRangeAddress(numFila,  numFila, 18, 23));
                    hoja.addMergedRegion(new CellRangeAddress(numFila,  numFila, 24, 28));
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
                listaPuestos = manager.getDatosInformePuestos(ano, adapt);
                
                int maxLengthNombre = 0;
                 //numFila++;
                //Insertamos los datos, fila a fila
                for(FilaInformeDatosPuestosVO filaI : listaPuestos)
                {
                    if(filaI.getNumExpediente() != null && !filaI.getNumExpediente().equals(numExp))
                    {
//                        if(numExp != null && !numExp.equals(""))
//                        {
//                            //se añade el sumatorio parcial
//                            numFila++;
//                            //numFila2++;
//                            fila = hoja.createRow(numFila);                            
//                        }
                        //se añaden los merged region
//                        if (numFila >1){
//                            hoja.addMergedRegion(new Region(numFila-p-1, (short)0, numFila, (short)0));
//                            hoja.addMergedRegion(new Region(numFila-p-1, (short)1, numFila, (short)1));
//                            hoja.addMergedRegion(new Region(numFila-p-1, (short)2, numFila-1, (short)2));
//                        }
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

//                    height = 0;
//                    height = Math.max(numIdiomas, numTitulaciones);
//                    height = Math.max(height, numPaises);
//                    height = Math.max(height, 1);
//                    height = height * fila.getHeight();

                    //fila.setHeight((short)height);

                    
                    crearDatosInformePuestos(libro, fila, celda, estiloCelda, idioma, filaI, numExp, numFila, nuevo, n, listaPuestos, normal, codOrganizacion, ano,  adapt,  format);

                }
                
                listaPuestosH = manager.getDatosInformePuestosHoja2(ano, adapt);
                nuevo = true; numExp = null;
                for(FilaInformeHoja2DatosPuestosVO filaI : listaPuestosH)
                {
                    if(filaI.getNumExpediente() != null && !filaI.getNumExpediente().equals(numExp))
                    {
//                        if(numExp != null && !numExp.equals(""))
//                        {
//                            //se añade el sumatorio parcial
//                            numFila2++;
//                            //numFila2++;
//                            fila2 = hoja2.createRow(numFila2);                            
//                        }
                        //se añaden los merged region
//                        if (numFila >1){
//                            hoja.addMergedRegion(new Region(numFila-p-1, (short)0, numFila, (short)0));
//                            hoja.addMergedRegion(new Region(numFila-p-1, (short)1, numFila, (short)1));
//                            hoja.addMergedRegion(new Region(numFila-p-1, (short)2, numFila-1, (short)2));
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
                
                hoja.setColumnWidth((short)16, (short)(maxLengthNombre*400));
                
                //Añade sumatorios a la ultima fila
                numFila++;
//                fila = hoja.createRow(numFila);        
//                anadirSumatoriosParcialesResumenPuestos(idioma, libro, fila, normal, totalPuestosSolic, totalContRealizados, totalPuestosDenegados, totalPuestosRenAntesRes, totalPersCandidatas, totalPuestosRenAntesPag1);
//                  
//                    hoja.addMergedRegion(new Region(numFila-p-1, (short)0, numFila, (short)0));
//                    hoja.addMergedRegion(new Region(numFila-p-1, (short)1, numFila, (short)1));
//                    hoja.addMergedRegion(new Region(numFila-p-1, (short)2, numFila-1, (short)2));
                    
                /*File directorioTemp = new File(m_Conf.getString("PDF.base_dir"));
                File informe = File.createTempFile("resumenDatosPuestos", ".xls", directorioTemp);

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
            MeLanbide59I18n meLanbide59I18n = MeLanbide59I18n.getInstance();
            
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
                celda = fila.createCell((short)0);
                celda.setCellValue(meLanbide59I18n.getMensaje(idioma, "doc.informeDatosPuestos.col1").toUpperCase());
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
                celda = fila.createCell((short)1);
                celda.setCellValue(meLanbide59I18n.getMensaje(idioma, "doc.informeDatosPuestos.col2").toUpperCase());
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
                celda = fila.createCell((short)2);
                celda.setCellValue(meLanbide59I18n.getMensaje(idioma, "doc.informeDatosPuestos.col3").toUpperCase());
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
                celda = fila.createCell((short)3);
                celda.setCellValue(meLanbide59I18n.getMensaje(idioma, "doc.informeDatosPuestos.col4").toUpperCase());
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
                celda = fila.createCell((short)4);
                celda.setCellValue(meLanbide59I18n.getMensaje(idioma, "doc.informeDatosPuestos.col5").toUpperCase());
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
                celda = fila.createCell((short)5);
                celda.setCellValue(meLanbide59I18n.getMensaje(idioma, "doc.informeDatosPuestos.col6").toUpperCase());
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
                celda = fila.createCell((short)6);
                celda.setCellValue(meLanbide59I18n.getMensaje(idioma, "doc.informeDatosPuestos.col7").toUpperCase());
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
                celda = fila.createCell((short)7);
                celda.setCellValue(meLanbide59I18n.getMensaje(idioma, "doc.informeDatosPuestos.col8").toUpperCase());
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
                celda = fila.createCell((short)8);
                celda.setCellValue(meLanbide59I18n.getMensaje(idioma, "doc.informeDatosPuestos.col9").toUpperCase());
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
                celda = fila.createCell((short)9);
                celda.setCellValue(meLanbide59I18n.getMensaje(idioma, "doc.informeDatosPuestos.col10").toUpperCase());
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
                celda = fila.createCell((short)10);
                celda.setCellValue(meLanbide59I18n.getMensaje(idioma, "doc.informeDatosPuestos.col11").toUpperCase());
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
                celda = fila.createCell((short)11);
                celda.setCellValue(meLanbide59I18n.getMensaje(idioma, "doc.informeDatosPuestos.col12").toUpperCase());
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
                celda = fila.createCell((short)12);
                celda.setCellValue(meLanbide59I18n.getMensaje(idioma, "doc.informeDatosPuestos.col13").toUpperCase());
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
                celda = fila.createCell((short)13);
                celda.setCellValue(meLanbide59I18n.getMensaje(idioma, "doc.informeDatosPuestos.col14").toUpperCase());
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
                celda = fila.createCell((short)14);
                celda.setCellValue(meLanbide59I18n.getMensaje(idioma, "doc.informeDatosPuestos.col15").toUpperCase());
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
                celda = fila.createCell((short)15);
                celda.setCellValue(meLanbide59I18n.getMensaje(idioma, "doc.informeDatosPuestos.col16").toUpperCase());
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
                celda = fila.createCell((short)16);
                celda.setCellValue(meLanbide59I18n.getMensaje(idioma, "doc.informeDatosPuestos.col17").toUpperCase());
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
                celda = fila.createCell((short)17);
                celda.setCellValue(meLanbide59I18n.getMensaje(idioma, "doc.informeDatosPuestos.col18").toUpperCase());
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
                celda = fila.createCell((short)18);
                celda.setCellValue(meLanbide59I18n.getMensaje(idioma, "doc.informeDatosPuestos.col19").toUpperCase());
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
                celda = fila.createCell((short)19);
                celda.setCellValue(meLanbide59I18n.getMensaje(idioma, "doc.informeDatosPuestos.col20").toUpperCase());
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
                celda = fila.createCell((short)20);
                celda.setCellValue(meLanbide59I18n.getMensaje(idioma, "doc.informeDatosPuestos.col21").toUpperCase());
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
                celda = fila.createCell((short)21);
                celda.setCellValue(meLanbide59I18n.getMensaje(idioma, "doc.informeDatosPuestos.col22").toUpperCase());
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
                celda = fila.createCell((short)22);
                celda.setCellValue(meLanbide59I18n.getMensaje(idioma, "doc.informeDatosPuestos.col23").toUpperCase());
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
                celda = fila.createCell((short)23);
                celda.setCellValue(meLanbide59I18n.getMensaje(idioma, "doc.informeDatosPuestos.col24").toUpperCase());
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
                celda = fila.createCell((short)24);
                celda.setCellValue(meLanbide59I18n.getMensaje(idioma, "doc.informeDatosPuestos.col25").toUpperCase());
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
                celda = fila.createCell((short)25);
                celda.setCellValue(meLanbide59I18n.getMensaje(idioma, "doc.informeDatosPuestos.col26").toUpperCase());
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
                celda = fila.createCell((short)26);
                celda.setCellValue(meLanbide59I18n.getMensaje(idioma, "doc.informeDatosPuestos.col27").toUpperCase());
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
                celda = fila.createCell((short)27);
                celda.setCellValue(meLanbide59I18n.getMensaje(idioma, "doc.informeDatosPuestos.col28").toUpperCase());
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
                celda = fila.createCell((short)28);
                celda.setCellValue(meLanbide59I18n.getMensaje(idioma, "doc.informeDatosPuestos.col29").toUpperCase());
                celda.setCellStyle(estiloCelda);
            }
            catch (Exception e) 
            {
                e.printStackTrace();
            }

            
        }catch(Exception ex){
            
        }
        
    }
    private void crearEstiloCabeceraInformeDatosPuestos(HSSFWorkbook libro, HSSFRow fila, HSSFCell celda, HSSFCellStyle estiloCelda, int idioma)
    {
        try
        {
            MeLanbide59I18n meLanbide59I18n = MeLanbide59I18n.getInstance();
            
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
                celda = fila.createCell((short)7);
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
                celda = fila.createCell((short)18);
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
                celda = fila.createCell((short)24);
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
            celda = fila.createCell((short)0);
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
            celda = fila.createCell((short)1);

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
            celda = fila.createCell((short)2);

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
            celda = fila.createCell((short)3);
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
            celda = fila.createCell((short)4);
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
            celda = fila.createCell((short)5);
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
            celda = fila.createCell((short)6);
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
            celda = fila.createCell((short)7);
            celda.setCellValue(filaI.getSalarioAnualBruto() != null ? filaI.getSalarioAnualBruto().toUpperCase().replaceAll(",", "\\.") : "");
            celda.setCellStyle(estiloCelda);

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
            celda = fila.createCell((short)8);
            celda.setCellValue(filaI.getDietasAloj() != null ? filaI.getDietasAloj().toUpperCase() : "");
            celda.setCellStyle(estiloCelda);

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
            celda = fila.createCell((short)9);
            celda.setCellValue(filaI.getMesesExt()!= null ? filaI.getMesesExt().toUpperCase() : "");
            celda.setCellStyle(estiloCelda);

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
            celda = fila.createCell((short)10);
            celda.setCellValue(filaI.getTramitacionPer() != null ? filaI.getTramitacionPer() : "");
            celda.setCellStyle(estiloCelda);
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
            celda = fila.createCell((short)11);
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
            celda = fila.createCell((short)12);
            celda.setCellValue(filaI.getPaisDestino() != null ? filaI.getPaisDestino() : "");
            celda.setCellStyle(estiloCelda);

//            //COLUMNA: SALARIO ANEXO
//            estiloCelda = libro.createCellStyle();
//            estiloCelda.setWrapText(true);
//            estiloCelda.setFont(normal);
//            estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THIN);
//            if(n == 1)
//            {
//                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
//            }
//            else
//            {
//                //estiloCelda.setBorderTop(HSSFCellStyle.BORDER_DOUBLE);
//                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
//            }
//            if(numFila == listaPuestos.size())
//            {
//                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
//            }
//            celda = fila.createCell((short)12);
//            celda.setCellValue(filaI.getSalarioAnexo()!= null ? filaI.getSalarioAnexo() : "");
//            celda.setCellStyle(estiloCelda);

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
            celda = fila.createCell((short)13);
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
            celda = fila.createCell((short)14);
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
            celda = fila.createCell((short)15);
            celda.setCellValue(filaI.getDiasSubv() != null ? filaI.getDiasSubv() : "");
            celda.setCellStyle(estiloCelda);

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
            celda = fila.createCell((short)16);
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
            celda = fila.createCell((short)17);
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
            celda = fila.createCell((short)18);
            celda.setCellValue(filaI.getSalarioAnualBruto() != null ? filaI.getSalarioAnualBruto().toUpperCase().replaceAll(",", "\\.") : "");
            celda.setCellStyle(estiloCelda);

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
            celda = fila.createCell((short)19);
            celda.setCellValue(filaI.getImporteJus() != null ? filaI.getImporteJus() : "");
            celda.setCellStyle(estiloCelda);

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
            celda = fila.createCell((short)20);
            celda.setCellValue(filaI.getSalarioSSBonif()!= null ? filaI.getSalarioSSBonif().replaceAll(",", "\\.") : "");
            celda.setCellStyle(estiloCelda);

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
            celda = fila.createCell((short)21);
            celda.setCellValue(filaI.getDietasJus()!= null ? filaI.getDietasJus() : "");
            celda.setCellStyle(estiloCelda);
            //celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);

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
            celda = fila.createCell((short)22);
            celda.setCellValue(filaI.getGastosAbonados()!= null ? filaI.getGastosAbonados().toUpperCase() : "");
            celda.setCellStyle(estiloCelda);

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
            celda = fila.createCell((short)23);
            celda.setCellValue(filaI.getImporteJus() != null ? filaI.getImporteJus().toUpperCase() : "");
            celda.setCellStyle(estiloCelda);

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
            celda = fila.createCell((short)24);
            celda.setCellValue(filaI.getMaxSubvencion() != null ? filaI.getMaxSubvencion().toUpperCase() : "");
            celda.setCellStyle(estiloCelda);

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
            celda = fila.createCell((short)25);
            celda.setCellValue(filaI.getContratoBonif() != null ? filaI.getContratoBonif().toUpperCase().replaceAll(",", "\\.") : "");
            celda.setCellStyle(estiloCelda);

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
            celda = fila.createCell((short)26);
            celda.setCellValue(filaI.getDietasMaxSubv() != null ? filaI.getDietasMaxSubv() : "");
            celda.setCellStyle(estiloCelda);

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
            celda = fila.createCell((short)27);
            celda.setCellValue(filaI.getSeguroMedico()!= null ? filaI.getSeguroMedico() : "");
            celda.setCellStyle(estiloCelda);

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
            celda = fila.createCell((short)28);
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
            MeLanbide59I18n meLanbide59I18n = MeLanbide59I18n.getInstance();
            
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
                celda = fila.createCell((short)0);
                celda.setCellValue(meLanbide59I18n.getMensaje(idioma, "doc.informeDatosPuestosHoja2.col1").toUpperCase());
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
                celda = fila.createCell((short)1);
                celda.setCellValue(meLanbide59I18n.getMensaje(idioma, "doc.informeDatosPuestosHoja2.col2").toUpperCase());
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
                celda = fila.createCell((short)2);
                celda.setCellValue(meLanbide59I18n.getMensaje(idioma, "doc.informeDatosPuestosHoja2.col3").toUpperCase());
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
                celda = fila.createCell((short)3);
                celda.setCellValue(meLanbide59I18n.getMensaje(idioma, "doc.informeDatosPuestosHoja2.col4").toUpperCase());
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
                celda = fila.createCell((short)4);
                celda.setCellValue(meLanbide59I18n.getMensaje(idioma, "doc.informeDatosPuestosHoja2.col5").toUpperCase());
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
                celda = fila.createCell((short)5);
                celda.setCellValue(meLanbide59I18n.getMensaje(idioma, "doc.informeDatosPuestosHoja2.col6").toUpperCase());
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
                celda = fila.createCell((short)6);
                celda.setCellValue(meLanbide59I18n.getMensaje(idioma, "doc.informeDatosPuestosHoja2.col7").toUpperCase());
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
                celda = fila.createCell((short)7);
                celda.setCellValue(meLanbide59I18n.getMensaje(idioma, "doc.informeDatosPuestosHoja2.col8").toUpperCase());
                celda.setCellStyle(estiloCelda);
            }
            catch (Exception e) 
            {
                e.printStackTrace();
            }
            
        }catch(Exception ex){
            
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
            celda = fila.createCell((short)0);
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
            celda = fila.createCell((short)1);

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
            celda = fila.createCell((short)2);
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
            celda = fila.createCell((short)3);
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
            celda = fila.createCell((short)4);
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
            celda = fila.createCell((short)5);
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
            celda = fila.createCell((short)6);
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
            celda = fila.createCell((short)7);
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
            
            MeLanbide59I18n meLanbide59I18n = MeLanbide59I18n.getInstance();
            MeLanbide59Manager manager = MeLanbide59Manager.getInstance();
            SimpleDateFormat format = new SimpleDateFormat(ConstantesMeLanbide59.FORMATO_FECHA);
            
            try 
            {
                HSSFWorkbook libro = new HSSFWorkbook();
            
                /*File directorioTemp = new File(m_Conf.getString("PDF.base_dir"));
                File informe = File.createTempFile("resumenPuestosContratados", ".xls", directorioTemp);
                FileOutputStream archivoSalida = new FileOutputStream(informe);*/
                
                //se escribe el fichero
                File directorioTemp = new File(m_Conf.getString("PDF.base_dir"));
                log.debug("directorioTemp" + directorioTemp.toString());
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
                hoja.setColumnWidth((short)0, (short)6000);//Num. expediente
                hoja.setColumnWidth((short)1, (short)10000);//empresa
                hoja.setColumnWidth((short)2, (short)8000);//territorio historico
                hoja.setColumnWidth((short)3, (short)5000);//puestos solic
                hoja.setColumnWidth((short)4, (short)10000);//puesto
                hoja.setColumnWidth((short)5, (short)8000);//pais solicitud
                hoja.setColumnWidth((short)6, (short)12000);//titulacion
                hoja.setColumnWidth((short)7, (short)8000);//idiomas
                hoja.setColumnWidth((short)8, (short)6000);//resultado
                hoja.setColumnWidth((short)9, (short)4000);//puestos denegados
                hoja.setColumnWidth((short)10, (short)6000);//puestos ren antes res
                hoja.setColumnWidth((short)11, (short)6000);//motivo
                hoja.setColumnWidth((short)12, (short)3600);//oferta
                hoja.setColumnWidth((short)13, (short)4500);//pers precandidatas
                hoja.setColumnWidth((short)14, (short)3000);//difusion
                hoja.setColumnWidth((short)15, (short)5000);//fecha envio pers candidatas
                hoja.setColumnWidth((short)16, (short)5000);//num pers candidatas
                hoja.setColumnWidth((short)17, (short)5000);//num. puestos ren antes primer pago
                hoja.setColumnWidth((short)18, (short)5000);//Causa renuncia
                hoja.setColumnWidth((short)19, (short)10000);//persona seleccionada
                hoja.setColumnWidth((short)20, (short)5000);//fecha nacimiento
                hoja.setColumnWidth((short)21, (short)5000);//edad fecha contrato
                hoja.setColumnWidth((short)22, (short)2500);//sexo
                hoja.setColumnWidth((short)23, (short)8000);//pais contratacion
                hoja.setColumnWidth((short)24, (short)8000);//titulacion pers candidata
                hoja.setColumnWidth((short)25, (short)6000);//nivel formativo
                hoja.setColumnWidth((short)26, (short)5000);//fecha contratacion
                hoja.setColumnWidth((short)27, (short)5000);//fin contrato
                hoja.setColumnWidth((short)28, (short)4000);//tiempo contratacion
                hoja.setColumnWidth((short)29, (short)5000);//num contratos realizados
                
                hoja2.setColumnWidth((short)0, (short)6000);//Num. expediente
                hoja2.setColumnWidth((short)1, (short)10000);//empresa
                hoja2.setColumnWidth((short)2, (short)8000);//territorio historico
                hoja2.setColumnWidth((short)3, (short)5000);//puestos solic
                hoja2.setColumnWidth((short)4, (short)10000);//puesto
                hoja2.setColumnWidth((short)5, (short)8000);//pais solicitud
                hoja2.setColumnWidth((short)6, (short)12000);//titulacion
                hoja2.setColumnWidth((short)7, (short)8000);//idiomas
                hoja2.setColumnWidth((short)8, (short)6000);//resultado
                hoja2.setColumnWidth((short)9, (short)4000);//puestos denegados
                hoja2.setColumnWidth((short)10, (short)6000);//puestos ren antes res
                hoja2.setColumnWidth((short)11, (short)6000);//motivo
                hoja2.setColumnWidth((short)12, (short)3600);//oferta
                hoja2.setColumnWidth((short)13, (short)4500);//pers precandidatas
                hoja2.setColumnWidth((short)14, (short)3000);//difusion
                hoja2.setColumnWidth((short)15, (short)5000);//fecha envio pers candidatas
                hoja2.setColumnWidth((short)16, (short)5000);//num pers candidatas
                hoja2.setColumnWidth((short)17, (short)5000);//num. puestos ren antes primer pago
                hoja2.setColumnWidth((short)18, (short)5000);//Causa renuncia
                hoja2.setColumnWidth((short)19, (short)10000);//persona seleccionada
                hoja2.setColumnWidth((short)20, (short)5000);//fecha nacimiento
                hoja2.setColumnWidth((short)21, (short)5000);//edad fecha contrato
                hoja2.setColumnWidth((short)22, (short)2500);//sexo
                hoja2.setColumnWidth((short)23, (short)8000);//pais contratacion
                hoja2.setColumnWidth((short)24, (short)8000);//titulacion pers candidata
                hoja2.setColumnWidth((short)25, (short)6000);//nivel formativo
                hoja2.setColumnWidth((short)26, (short)5000);//fecha contratacion
                hoja2.setColumnWidth((short)27, (short)5000);//fin contrato
                hoja2.setColumnWidth((short)28, (short)4000);//tiempo contratacion
                hoja2.setColumnWidth((short)29, (short)5000);//num contratos realizados
                //hoja.setColumnWidth((short)27, (short)5000);//motivo renuncia
                
                
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
                celda = fila.createCell((short)0);
                celda.setCellValue(meLanbide59I18n.getMensaje(idioma, "doc.informePuestosContratados.col1").toUpperCase());
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
                celda = fila.createCell((short)1);
                celda.setCellValue(meLanbide59I18n.getMensaje(idioma, "doc.informePuestosContratados.col2").toUpperCase());
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
                celda = fila.createCell((short)2);
                celda.setCellValue(meLanbide59I18n.getMensaje(idioma, "doc.informePuestosContratados.col31").toUpperCase());
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
                celda = fila.createCell((short)3);
                celda.setCellValue(meLanbide59I18n.getMensaje(idioma, "doc.informePuestosContratados.col3").toUpperCase());
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
                celda = fila.createCell((short)4);
                celda.setCellValue(meLanbide59I18n.getMensaje(idioma, "doc.informePuestosContratados.col4").toUpperCase());
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
                celda = fila.createCell((short)5);
                celda.setCellValue(meLanbide59I18n.getMensaje(idioma, "doc.informePuestosContratados.col5").toUpperCase());
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
                celda = fila.createCell((short)6);
                celda.setCellValue(meLanbide59I18n.getMensaje(idioma, "doc.informePuestosContratados.col6").toUpperCase());
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
                celda = fila.createCell((short)7);
                celda.setCellValue(meLanbide59I18n.getMensaje(idioma, "doc.informePuestosContratados.col7").toUpperCase());
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
                celda = fila.createCell((short)8);
                celda.setCellValue(meLanbide59I18n.getMensaje(idioma, "doc.informePuestosContratados.col8").toUpperCase());
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
                celda = fila.createCell((short)9);
                celda.setCellValue(meLanbide59I18n.getMensaje(idioma, "doc.informePuestosContratados.col9").toUpperCase());
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
                celda = fila.createCell((short)10);
                celda.setCellValue(meLanbide59I18n.getMensaje(idioma, "doc.informePuestosContratados.col10").toUpperCase());
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
                celda = fila.createCell((short)11);
                celda.setCellValue(meLanbide59I18n.getMensaje(idioma, "doc.informePuestosContratados.col11").toUpperCase());
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
                celda = fila.createCell((short)12);
                celda.setCellValue(meLanbide59I18n.getMensaje(idioma, "doc.informePuestosContratados.col12").toUpperCase());
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
                celda = fila.createCell((short)13);
                celda.setCellValue(meLanbide59I18n.getMensaje(idioma, "doc.informePuestosContratados.col13").toUpperCase());
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
                celda = fila.createCell((short)14);
                celda.setCellValue(meLanbide59I18n.getMensaje(idioma, "doc.informePuestosContratados.col14").toUpperCase());
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
                celda = fila.createCell((short)15);
                celda.setCellValue(meLanbide59I18n.getMensaje(idioma, "doc.informePuestosContratados.col15").toUpperCase());
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
                celda = fila.createCell((short)16);
                celda.setCellValue(meLanbide59I18n.getMensaje(idioma, "doc.informePuestosContratados.col16").toUpperCase());
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
                celda = fila.createCell((short)17);
                celda.setCellValue(meLanbide59I18n.getMensaje(idioma, "doc.informePuestosContratados.col29").toUpperCase());
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
                celda = fila.createCell((short)18);
                celda.setCellValue(meLanbide59I18n.getMensaje(idioma, "doc.informePuestosContratados.col30").toUpperCase());
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
                celda = fila.createCell((short)19);
                celda.setCellValue(meLanbide59I18n.getMensaje(idioma, "doc.informePuestosContratados.col17").toUpperCase());
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
                celda = fila.createCell((short)20);
                celda.setCellValue(meLanbide59I18n.getMensaje(idioma, "doc.informePuestosContratados.col18").toUpperCase());
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
                celda = fila.createCell((short)21);
                celda.setCellValue(meLanbide59I18n.getMensaje(idioma, "doc.informePuestosContratados.col19").toUpperCase());
                celda.setCellStyle(estiloCelda);

                estilocelda2 = libro.createCellStyle();
                estilocelda2.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estilocelda2.setFillForegroundColor(hssfColor.getIndex());
                estilocelda2.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estilocelda2.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estilocelda2.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estilocelda2.setWrapText(true);
                estilocelda2.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estilocelda2.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estilocelda2.setFont(negritaTitulo);
                celda2 = fila2.createCell((short)21);
                celda2.setCellValue(meLanbide59I18n.getMensaje(idioma, "doc.informePuestosContratados.col19").toUpperCase());
                celda2.setCellStyle(estilocelda2);

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
                celda = fila.createCell((short)22);
                celda.setCellValue(meLanbide59I18n.getMensaje(idioma, "doc.informePuestosContratados.col20").toUpperCase());
                celda.setCellStyle(estiloCelda);

                estilocelda2 = libro.createCellStyle();
                estilocelda2.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estilocelda2.setFillForegroundColor(hssfColor.getIndex());
                estilocelda2.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estilocelda2.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estilocelda2.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estilocelda2.setWrapText(true);
                estilocelda2.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estilocelda2.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estilocelda2.setFont(negritaTitulo);
                celda2 = fila2.createCell((short)22);
                celda2.setCellValue(meLanbide59I18n.getMensaje(idioma, "doc.informePuestosContratados.col20").toUpperCase());
                celda2.setCellStyle(estilocelda2);

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
                celda = fila.createCell((short)23);
                celda.setCellValue(meLanbide59I18n.getMensaje(idioma, "doc.informePuestosContratados.col21").toUpperCase());
                celda.setCellStyle(estiloCelda);

                estilocelda2 = libro.createCellStyle();
                estilocelda2.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estilocelda2.setFillForegroundColor(hssfColor.getIndex());
                estilocelda2.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estilocelda2.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estilocelda2.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estilocelda2.setWrapText(true);
                estilocelda2.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estilocelda2.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estilocelda2.setFont(negritaTitulo);
                celda2 = fila2.createCell((short)23);
                celda2.setCellValue(meLanbide59I18n.getMensaje(idioma, "doc.informePuestosContratados.col21").toUpperCase());
                celda2.setCellStyle(estilocelda2);

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
                celda = fila.createCell((short)24);
                celda.setCellValue(meLanbide59I18n.getMensaje(idioma, "doc.informePuestosContratados.col22").toUpperCase());
                celda.setCellStyle(estiloCelda);

                estilocelda2 = libro.createCellStyle();
                estilocelda2.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estilocelda2.setFillForegroundColor(hssfColor.getIndex());
                estilocelda2.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estilocelda2.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estilocelda2.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estilocelda2.setWrapText(true);
                estilocelda2.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estilocelda2.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estilocelda2.setFont(negritaTitulo);
                celda2 = fila2.createCell((short)24);
                celda2.setCellValue(meLanbide59I18n.getMensaje(idioma, "doc.informePuestosContratados.col22").toUpperCase());
                celda2.setCellStyle(estilocelda2);

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
                celda = fila.createCell((short)25);
                celda.setCellValue(meLanbide59I18n.getMensaje(idioma, "doc.informePuestosContratados.col23").toUpperCase());
                celda.setCellStyle(estiloCelda);

                estilocelda2 = libro.createCellStyle();
                estilocelda2.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estilocelda2.setFillForegroundColor(hssfColor.getIndex());
                estilocelda2.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estilocelda2.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estilocelda2.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estilocelda2.setWrapText(true);
                estilocelda2.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estilocelda2.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estilocelda2.setFont(negritaTitulo);
                celda2 = fila2.createCell((short)25);
                celda2.setCellValue(meLanbide59I18n.getMensaje(idioma, "doc.informePuestosContratados.col23").toUpperCase());
                celda2.setCellStyle(estilocelda2);

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
                celda = fila.createCell((short)26);
                celda.setCellValue(meLanbide59I18n.getMensaje(idioma, "doc.informePuestosContratados.col24").toUpperCase());
                celda.setCellStyle(estiloCelda);

                estilocelda2 = libro.createCellStyle();
                estilocelda2.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estilocelda2.setFillForegroundColor(hssfColor.getIndex());
                estilocelda2.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estilocelda2.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estilocelda2.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estilocelda2.setWrapText(true);
                estilocelda2.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estilocelda2.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estilocelda2.setFont(negritaTitulo);
                celda2 = fila2.createCell((short)26);
                celda2.setCellValue(meLanbide59I18n.getMensaje(idioma, "doc.informePuestosContratados.col24").toUpperCase());
                celda2.setCellStyle(estilocelda2);

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
                celda = fila.createCell((short)27);
                celda.setCellValue(meLanbide59I18n.getMensaje(idioma, "doc.informePuestosContratados.col25").toUpperCase());
                celda.setCellStyle(estiloCelda);

                estilocelda2 = libro.createCellStyle();
                estilocelda2.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estilocelda2.setFillForegroundColor(hssfColor.getIndex());
                estilocelda2.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estilocelda2.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estilocelda2.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estilocelda2.setWrapText(true);
                estilocelda2.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estilocelda2.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estilocelda2.setFont(negritaTitulo);
                celda2 = fila2.createCell((short)27);
                celda2.setCellValue(meLanbide59I18n.getMensaje(idioma, "doc.informePuestosContratados.col25").toUpperCase());
                celda2.setCellStyle(estilocelda2);

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
                celda = fila.createCell((short)28);
                celda.setCellValue(meLanbide59I18n.getMensaje(idioma, "doc.informePuestosContratados.col26").toUpperCase());
                celda.setCellStyle(estiloCelda);

                estilocelda2 = libro.createCellStyle();
                estilocelda2.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estilocelda2.setFillForegroundColor(hssfColor.getIndex());
                estilocelda2.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estilocelda2.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estilocelda2.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estilocelda2.setWrapText(true);
                estilocelda2.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estilocelda2.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estilocelda2.setFont(negritaTitulo);
                celda2 = fila2.createCell((short)28);
                celda2.setCellValue(meLanbide59I18n.getMensaje(idioma, "doc.informePuestosContratados.col26").toUpperCase());
                celda2.setCellStyle(estilocelda2);

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
                celda = fila.createCell((short)29);
                celda.setCellValue(meLanbide59I18n.getMensaje(idioma, "doc.informePuestosContratados.col27").toUpperCase());
                celda.setCellStyle(estiloCelda);
                
                estilocelda2 = libro.createCellStyle();
                estilocelda2.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
                estilocelda2.setFillForegroundColor(hssfColor.getIndex());
                estilocelda2.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                estilocelda2.setBorderTop(HSSFCellStyle.BORDER_THICK);
                estilocelda2.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                estilocelda2.setBorderRight(HSSFCellStyle.BORDER_THICK);
                estilocelda2.setWrapText(true);
                estilocelda2.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                estilocelda2.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                estilocelda2.setFont(negritaTitulo);
                celda2 = fila2.createCell((short)29);
                celda2.setCellValue(meLanbide59I18n.getMensaje(idioma, "doc.informePuestosContratados.col27").toUpperCase());
                celda2.setCellStyle(estilocelda2);

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
                celda = fila.createCell((short)27);
                celda.setCellValue(meLanbide59I18n.getMensaje(idioma, "doc.informePuestosContratados.col28").toUpperCase());
                celda.setCellStyle(estiloCelda);*/
                
                crearEstiloHoja2(libro, fila2, celda, estilocelda2, idioma);
                
                
            
                
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
                 //numFila++;
                //Insertamos los datos, fila a fila
                for(FilaInformeResumenPuestosContratadosVO filaI : listaPuestos)
                {
                    if(filaI.getNumExpediente() != null && !filaI.getNumExpediente().equals(numExp))
                    {
                        if(numExp != null && !numExp.equals(""))
                        {
                            //se añade el sumatorio parcial
                            numFila++;
                            //numFila2++;
                            fila = hoja.createRow(numFila);
                            fila2 = hoja2.createRow(numFila2);
                            
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
                        if (numFila >1){
                            /*hoja.addMergedRegion(new Region(numFila-p-1, (short)0, numFila, (short)0));
                            hoja.addMergedRegion(new Region(numFila-p-1, (short)1, numFila, (short)1));
                            hoja.addMergedRegion(new Region(numFila-p-1, (short)2, numFila-1, (short)2));*/
                                //poi 3.10
                                hoja.addMergedRegion(new CellRangeAddress(numFila-p-1, numFila, 0, 0));
                                hoja.addMergedRegion(new CellRangeAddress(numFila-p-1, numFila, 1, 1));
                                hoja.addMergedRegion(new CellRangeAddress(numFila-p-1, numFila, 2, 2));
                        }
                        numExp = filaI.getNumExpediente();
                        nuevo = true;
                        p = 0;
                        n++;
                        numFila++;
                        numFila2++;
                    }
                    else
                    {
                        numFila++;  
                        numFila2++;    
                        nuevo = false;
                        p++;
                    }
                    
                    fila = hoja.createRow(numFila);
                    fila2 = hoja2.createRow(numFila2);
                    
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
                    estiloCelda = libro.createCellStyle();
                    estiloCelda.setWrapText(true);
                    estiloCelda.setFont(normal);
                    estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                    estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
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
                    if(numFila == listaPuestos.size())
                    {
                        estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                    }
                    celda = fila.createCell((short)0);
                    if(nuevo)
                    {
                        celda.setCellValue(numExp != null ? numExp.toUpperCase() : "");
                    }
                    celda.setCellStyle(estiloCelda);

                    //COLUMNA: EMPRESA
                    if(filaI.getEmpresa() != null)
                    {
                        empresa = filaI.getEmpresa();
                    }
                    else
                    {
                        datosTercero = MeLanbide59Manager.getInstance().getDatosTercero(codOrganizacion, numExp, String.valueOf(ano), ConstantesMeLanbide59.CODIGO_ROL_ENTIDAD_SOLICITANTE, adapt);
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
                    estiloCelda.setFont(normal);
                    estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THIN);
                    estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
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
                    if(numFila == listaPuestos.size())
                    {
                        estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                    }
                    celda = fila.createCell((short)1);
                    if(nuevo)
                    {
                        celda.setCellValue(empresa != null ? empresa.toUpperCase() : "");
                    }
                    celda.setCellStyle(estiloCelda);

                    //COLUMNA: TERRITORIO HISTORICO
                    estiloCelda = libro.createCellStyle();
                    estiloCelda.setWrapText(true);
                    estiloCelda.setFont(normal);
                    estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THIN);
                    estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
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
                    if(numFila == listaPuestos.size())
                    {
                        estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                    }
                    celda = fila.createCell((short)2);
                    if(nuevo)
                    {
                        celda.setCellValue(filaI.getTerritorioHistorico() != null ? filaI.getTerritorioHistorico().toUpperCase() : "");
                    }
                    celda.setCellStyle(estiloCelda);

                    //COLUMNA: PUESTOS SOLICITADOS
                    estiloCelda = libro.createCellStyle();
                    estiloCelda.setWrapText(true);
                    estiloCelda.setFont(normal);
                    estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THIN);
                    estiloCelda.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
                    estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
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
                    if(numFila == listaPuestos.size())
                    {
                        estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                    }
                    celda = fila.createCell((short)3);
                    //celda.setCellType(HSSFCell.CELL_TYPE_STRING);
                    //if(nuevo)
                    //{
                        celda.setCellValue(filaI.getPuestosSolic() != null ? filaI.getPuestosSolic() : 0);
                        totalPuestosSolic += filaI.getPuestosSolic() != null ? filaI.getPuestosSolic() : 0;
                        celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                    //}
                    celda.setCellStyle(estiloCelda);
                    

                    //COLUMNA: PUESTO
                    estiloCelda = libro.createCellStyle();
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
                    celda = fila.createCell((short)4);
                    celda.setCellValue(filaI.getDescPuesto() != null ? filaI.getCodPuesto()+"-"+filaI.getDescPuesto().toUpperCase() : "");
                    celda.setCellStyle(estiloCelda);

                    //COLUMNA: PAIS_SOLICITUD
                    pais = filaI.getPaisSol1() != null ? filaI.getPaisSol1().toUpperCase() : "";
                    pais += filaI.getPaisSol2() != null && !filaI.getPaisSol2().equals("") ? (pais != null && !pais.equals("") ? System.getProperty("line.separator") : "") : "";
                    pais += filaI.getPaisSol2() != null && !filaI.getPaisSol2().equals("") ? filaI.getPaisSol2().toUpperCase() : "";
                    pais += filaI.getPaisSol3() != null && !filaI.getPaisSol3().equals("") ? (pais != null && !pais.equals("") ? System.getProperty("line.separator") : "") : "";
                    pais += filaI.getPaisSol3() != null && !filaI.getPaisSol3().equals("") ? filaI.getPaisSol3().toUpperCase() : "";


                    estiloCelda = libro.createCellStyle();
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
                    celda = fila.createCell((short)5);
                    celda.setCellValue(pais != null ? pais.toString() : "");
                    celda.setCellStyle(estiloCelda);

                    //COLUMNA: TITULACION
                    titulacion = filaI.getTitulacion1() != null ? filaI.getTitulacion1().toUpperCase() : "";
                    titulacion += filaI.getTitulacion2() != null && !filaI.getTitulacion2().equals("") ? (titulacion != null && !titulacion.equals("") ? System.getProperty("line.separator") : "") : "";
                    titulacion += filaI.getTitulacion2() != null && !filaI.getTitulacion2().equals("") ? filaI.getTitulacion2().toUpperCase() : "";
                    titulacion += filaI.getTitulacion3() != null && !filaI.getTitulacion3().equals("") ? (titulacion != null && !titulacion.equals("") ? System.getProperty("line.separator") : "") : "";
                    titulacion += filaI.getTitulacion3() != null && !filaI.getTitulacion3().equals("") ? filaI.getTitulacion3().toUpperCase() : "";
                    
                    estiloCelda = libro.createCellStyle();
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
                    celda = fila.createCell((short)6);
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


                    estiloCelda = libro.createCellStyle();
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
                    celda = fila.createCell((short)7);
                    celda.setCellValue(idiomas != null ? idiomas.toUpperCase() : "");
                    celda.setCellStyle(estiloCelda);

                    //COLUMNA: RESULTADO
                    estiloCelda = libro.createCellStyle();
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
                    celda = fila.createCell((short)8);
                    celda.setCellValue(filaI.getResultado() != null ? filaI.getResultado().toUpperCase() : "");
                    celda.setCellStyle(estiloCelda);

                    //COLUMNA: PUESTOS_DENEGADOS
                    estiloCelda = libro.createCellStyle();
                    estiloCelda.setWrapText(true);
                    estiloCelda.setFont(normal);
                    estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THIN);
                    estiloCelda.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
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
                    celda = fila.createCell((short)9);
                    celda.setCellValue(filaI.getPuestosDen() != null ? filaI.getPuestosDen() : 0);
                    totalPuestosDenegados += filaI.getPuestosDen() != null ? filaI.getPuestosDen() : 0;
                    celda.setCellStyle(estiloCelda);
                    celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);

                    //COLUMNA: PUESTOS_REN_ANTES_RES
                    estiloCelda = libro.createCellStyle();
                    estiloCelda.setWrapText(true);
                    estiloCelda.setFont(normal);
                    estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THIN);
                    estiloCelda.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
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
                    celda = fila.createCell((short)10);
                    celda.setCellValue(filaI.getPuestosRenAntesRes() != null ? filaI.getPuestosRenAntesRes() : 0);
                    totalPuestosRenAntesRes += filaI.getPuestosRenAntesRes() != null ? filaI.getPuestosRenAntesRes() : 0;
                    celda.setCellStyle(estiloCelda);
                    celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);

                    //COLUMNA: MOTIVO
                    estiloCelda = libro.createCellStyle();
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
                    celda = fila.createCell((short)11);
                    celda.setCellValue(filaI.getMotivo() != null ? filaI.getMotivo() : "");
                    celda.setCellStyle(estiloCelda);
                    
                    //COLUMNA: OFERTA
                    estiloCelda = libro.createCellStyle();
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
                    celda = fila.createCell((short)12);
                    celda.setCellValue(filaI.getOferta() != null ? filaI.getOferta() : "");
                    celda.setCellStyle(estiloCelda);
                    
                    //COLUMNA: PERSONAS PRECANDIDATAS
                    estiloCelda = libro.createCellStyle();
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
                    celda = fila.createCell((short)13);
                    celda.setCellValue(filaI.getPersPrecandidatas() != null ? filaI.getPersPrecandidatas().toUpperCase() : "");
                    celda.setCellStyle(estiloCelda);

                    //COLUMNA: DIFUSION
                    estiloCelda = libro.createCellStyle();
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
                    celda = fila.createCell((short)14);
                    celda.setCellValue(filaI.getDifusion() != null ? filaI.getDifusion().toUpperCase() : "");
                    celda.setCellStyle(estiloCelda);

                    //COLUMNA: FECHA_ENV_PERS_CANDIDATAS
                    estiloCelda = libro.createCellStyle();
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
                    celda = fila.createCell((short)15);
                    celda.setCellValue(filaI.getFecEnvPersPrecandidatas() != null ? filaI.getFecEnvPersPrecandidatas() : "");
                    celda.setCellStyle(estiloCelda);

                    //COLUMNA: NUMERO_CANDIDATOS
                    estiloCelda = libro.createCellStyle();
                    estiloCelda.setWrapText(true);
                    estiloCelda.setFont(normal);
                    estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THIN);
                    estiloCelda.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
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
                    celda = fila.createCell((short)16);
                    celda.setCellValue(filaI.getNumCandidatos() != null ? filaI.getNumCandidatos() : 0);
                    totalPersCandidatas += filaI.getNumCandidatos() != null ? filaI.getNumCandidatos() : 0;
                    celda.setCellStyle(estiloCelda);
                    celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);

                    //COLUMNA: PUESTOS_REN_ANTES_1PAGO
                    estiloCelda = libro.createCellStyle();
                    estiloCelda.setWrapText(true);
                    estiloCelda.setFont(normal);
                    estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THIN);
                    estiloCelda.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
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
                    celda = fila.createCell((short)17);
                    celda.setCellValue(filaI.getPuestosRenAntPrimPago() != null ? filaI.getPuestosRenAntPrimPago() : 0);
                    totalPuestosRenAntesPag1 += filaI.getPuestosRenAntPrimPago() != null ? filaI.getPuestosRenAntPrimPago() : 0;
                    celda.setCellStyle(estiloCelda);
                    celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);

                    //COLUMNA: CAUSA RENUNCIA
                    estiloCelda = libro.createCellStyle();
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
                    celda = fila.createCell((short)18);
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

                    estiloCelda = libro.createCellStyle();
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
                    celda = fila.createCell((short)19);
                    celda.setCellValue(nomApe != null ? nomApe.toUpperCase() : "");
                    celda.setCellStyle(estiloCelda);

                    //COLUMNA: FECHA_NACIMIENTO
                    estiloCelda = libro.createCellStyle();
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
                    celda = fila.createCell((short)20);
                    celda.setCellValue(filaI.getFecNac() != null ? filaI.getFecNac() : "");
                    celda.setCellStyle(estiloCelda);

                    //COLUMNA: EDAD
                    Integer edad = MeLanbide59Utils.calcularEdadFormulaLanb(filaI.getFecNac() != null ? format.parse(filaI.getFecNac()) : null, filaI.getFecIni()!= null ? format.parse(filaI.getFecIni()) : null);
                    
                    estiloCelda = libro.createCellStyle();
                    estiloCelda.setWrapText(true);
                    estiloCelda.setFont(normal);
                    estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THIN);
                    estiloCelda.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
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
                    celda = fila.createCell((short)21);
                    celda.setCellValue(edad!= null ? edad : 0);
                    celda.setCellStyle(estiloCelda);
                    celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);

                    //COLUMNA: SEXO
                    estiloCelda = libro.createCellStyle();
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
                    celda = fila.createCell((short)22);
                    celda.setCellValue(filaI.getSexo() != null ? filaI.getSexo().toUpperCase() : "");
                    celda.setCellStyle(estiloCelda);

                    //COLUMNA: PAIS_CONTRATACION
                    
                    String paisCont = filaI.getPaisCont1() != null ? filaI.getPaisCont1().toUpperCase() : "";
                    paisCont += filaI.getPaisCont2() != null && !filaI.getPaisCont2().equals("") ? (paisCont != null && !paisCont.equals("") ? System.getProperty("line.separator") : "") : "";
                    paisCont += filaI.getPaisCont2() != null && !filaI.getPaisCont2().equals("") ? filaI.getPaisCont2().toUpperCase() : "";
                    paisCont += filaI.getPaisCont3() != null && !filaI.getPaisCont3().equals("") ? (paisCont != null && !paisCont.equals("") ? System.getProperty("line.separator") : "") : "";
                    paisCont += filaI.getPaisCont3() != null && !filaI.getPaisCont3().equals("") ? filaI.getPaisCont3().toUpperCase() : "";
                    
                    estiloCelda = libro.createCellStyle();
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
                    celda = fila.createCell((short)23);
                    celda.setCellValue(paisCont != null ? paisCont.toUpperCase() : "");
                    celda.setCellStyle(estiloCelda);

                    //COLUMNA: TITULACION_PERS_CANDIDATA
                    estiloCelda = libro.createCellStyle();
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
                    celda = fila.createCell((short)24);
                    celda.setCellValue(filaI.getTitulacionSelec() != null ? filaI.getTitulacionSelec().toUpperCase() : "");
                    celda.setCellStyle(estiloCelda);
                    
                    //COLUMNA: NIVEL FORMATIVO
                    estiloCelda = libro.createCellStyle();
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
                    celda = fila.createCell((short)25);
                    celda.setCellValue(filaI.getNivelFormativo() != null ? filaI.getNivelFormativo().toUpperCase() : "");
                    celda.setCellStyle(estiloCelda);
                    
                    //COLUMNA: FECHA_INICIO
                    estiloCelda = libro.createCellStyle();
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
                    celda = fila.createCell((short)26);
                    celda.setCellValue(filaI.getFecIni() != null ? filaI.getFecIni() : "");
                    celda.setCellStyle(estiloCelda);
                    
                    //COLUMNA: FECHA_FIN
                    estiloCelda = libro.createCellStyle();
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
                    celda = fila.createCell((short)27);
                    celda.setCellValue(filaI.getFecFin() != null ? filaI.getFecFin() : "");
                    celda.setCellStyle(estiloCelda);

                    //COLUMNA: TIEMPO CONTRATACION
                    estiloCelda = libro.createCellStyle();
                    estiloCelda.setWrapText(true);
                    estiloCelda.setFont(normal);
                    estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THIN);
                    estiloCelda.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
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
                    celda = fila.createCell((short)28);
                    celda.setCellValue(filaI.getTiempoContratacion() != null ? filaI.getTiempoContratacion() : 0);
                    celda.setCellStyle(estiloCelda);
                    celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);

                    //COLUMNA: PUESTOS CONTRATADOS
                    estiloCelda = libro.createCellStyle();
                    estiloCelda.setWrapText(true);
                    estiloCelda.setFont(normal);
                    estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THIN);
                    estiloCelda.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
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
                    estiloCelda.setBorderRight(HSSFCellStyle.BORDER_THICK);
                    celda = fila.createCell((short)29);
                    celda.setCellValue(filaI.getPuestosCont() != null ? filaI.getPuestosCont() : 0);
                    totalContRealizados += filaI.getPuestosCont() != null ? filaI.getPuestosCont() : 0;
                    celda.setCellStyle(estiloCelda);
                    celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                    
                    
                    //HOJA2
                    
                    crearDatosHoja2(libro, fila2, celda2, estilocelda2, idioma, filaI,numExp, numFila2, nuevo, n, listaPuestos, datosTercero,  normal, codOrganizacion, ano,  adapt,  format);

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
                    celda = fila.createCell((short)27);
                    celda.setCellValue(filaI.getMotivoRenuncia() != null ? filaI.getMotivoRenuncia() : "");
                    celda.setCellStyle(estiloCelda);*/
                }
                
                hoja.setColumnWidth((short)16, (short)(maxLengthNombre*400));
                hoja2.setColumnWidth((short)16, (short)(maxLengthNombre*400));
                
                //Añade sumatorios a la ultima fila
                numFila++;
                fila = hoja.createRow(numFila);        
                anadirSumatoriosParcialesResumenPuestos(idioma, libro, fila, normal, totalPuestosSolic, totalContRealizados, totalPuestosDenegados, totalPuestosRenAntesRes, totalPersCandidatas, totalPuestosRenAntesPag1);
                        //cambiar poi 3.10
                       /* hoja.addMergedRegion(new Region(numFila-p-1, (short)0, numFila, (short)0));
                    hoja.addMergedRegion(new Region(numFila-p-1, (short)1, numFila, (short)1));
                    hoja.addMergedRegion(new Region(numFila-p-1, (short)2, numFila-1, (short)2));
                        */
                       hoja.addMergedRegion(new CellRangeAddress(numFila-p-1,  numFila, 0, 0));
                       hoja.addMergedRegion(new CellRangeAddress(numFila-p-1,  numFila, 1, 1));
                       hoja.addMergedRegion(new CellRangeAddress(numFila-p-1,  numFila-1, 2,2));
                    
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

            //Sumatorios parciales
            int totalPuestosSolic = 0;
            int totalContRealizados = 0;
            int totalPuestosDenegados = 0;
            int totalPuestosRenAntesRes = 0;
            int totalPersCandidatas = 0;
            int totalPuestosRenAntesPag1 = 0;
                
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
            celda = fila.createCell((short)0);

            celda.setCellValue(numExp != null ? numExp.toUpperCase() : "");

            celda.setCellStyle(estiloCelda);

            //COLUMNA: EMPRESA
            if(filaI.getEmpresa() != null)
            {
                empresa = filaI.getEmpresa();
            }
            else
            {
                datosTercero = MeLanbide59Manager.getInstance().getDatosTercero(codOrganizacion, numExp, String.valueOf(ano), ConstantesMeLanbide59.CODIGO_ROL_ENTIDAD_SOLICITANTE, adapt);
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
            estiloCelda.setFont(normal);
            estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THIN);
            //estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);

            estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);

            if(numFila == listaPuestos.size())
            {
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
            }
            celda = fila.createCell((short)1);

            celda.setCellValue(empresa != null ? empresa.toUpperCase() : "");
            celda.setCellStyle(estiloCelda);

            //COLUMNA: TERRITORIO HISTORICO
            estiloCelda = libro.createCellStyle();
            estiloCelda.setWrapText(true);
            estiloCelda.setFont(normal);
            estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THIN);
           // estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);

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
            celda = fila.createCell((short)2);
            celda.setCellValue(filaI.getTerritorioHistorico() != null ? filaI.getTerritorioHistorico().toUpperCase() : "");
            celda.setCellStyle(estiloCelda);

            //COLUMNA: PUESTOS SOLICITADOS
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
            celda = fila.createCell((short)3);
            //celda.setCellType(HSSFCell.CELL_TYPE_STRING);
            //if(nuevo)
            //{
            celda.setCellValue(filaI.getPuestosSolic() != null ? filaI.getPuestosSolic() : 0);
            totalPuestosSolic += filaI.getPuestosSolic() != null ? filaI.getPuestosSolic() : 0;
            celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
            //}
            celda.setCellStyle(estiloCelda);


            //COLUMNA: PUESTO
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
            celda = fila.createCell((short)4);
            celda.setCellValue(filaI.getDescPuesto() != null ? filaI.getCodPuesto()+"-"+filaI.getDescPuesto().toUpperCase() : "");
            celda.setCellStyle(estiloCelda);

            //COLUMNA: PAIS_SOLICITUD
            pais = filaI.getPaisSol1() != null ? filaI.getPaisSol1().toUpperCase() : "";
            pais += filaI.getPaisSol2() != null && !filaI.getPaisSol2().equals("") ? (pais != null && !pais.equals("") ? System.getProperty("line.separator") : "") : "";
            pais += filaI.getPaisSol2() != null && !filaI.getPaisSol2().equals("") ? filaI.getPaisSol2().toUpperCase() : "";
            pais += filaI.getPaisSol3() != null && !filaI.getPaisSol3().equals("") ? (pais != null && !pais.equals("") ? System.getProperty("line.separator") : "") : "";
            pais += filaI.getPaisSol3() != null && !filaI.getPaisSol3().equals("") ? filaI.getPaisSol3().toUpperCase() : "";


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
            celda = fila.createCell((short)5);
            celda.setCellValue(pais != null ? pais.toString() : "");
            celda.setCellStyle(estiloCelda);

            //COLUMNA: TITULACION
            titulacion = filaI.getTitulacion1() != null ? filaI.getTitulacion1().toUpperCase() : "";
            titulacion += filaI.getTitulacion2() != null && !filaI.getTitulacion2().equals("") ? (titulacion != null && !titulacion.equals("") ? System.getProperty("line.separator") : "") : "";
            titulacion += filaI.getTitulacion2() != null && !filaI.getTitulacion2().equals("") ? filaI.getTitulacion2().toUpperCase() : "";
            titulacion += filaI.getTitulacion3() != null && !filaI.getTitulacion3().equals("") ? (titulacion != null && !titulacion.equals("") ? System.getProperty("line.separator") : "") : "";
            titulacion += filaI.getTitulacion3() != null && !filaI.getTitulacion3().equals("") ? filaI.getTitulacion3().toUpperCase() : "";

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
            celda = fila.createCell((short)6);
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
            celda = fila.createCell((short)7);
            celda.setCellValue(idiomas != null ? idiomas.toUpperCase() : "");
            celda.setCellStyle(estiloCelda);

            //COLUMNA: RESULTADO
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
            celda = fila.createCell((short)8);
            celda.setCellValue(filaI.getResultado() != null ? filaI.getResultado().toUpperCase() : "");
            celda.setCellStyle(estiloCelda);

            //COLUMNA: PUESTOS_DENEGADOS
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
            celda = fila.createCell((short)9);
            celda.setCellValue(filaI.getPuestosDen() != null ? filaI.getPuestosDen() : 0);
            totalPuestosDenegados += filaI.getPuestosDen() != null ? filaI.getPuestosDen() : 0;
            celda.setCellStyle(estiloCelda);
            celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);

            //COLUMNA: PUESTOS_REN_ANTES_RES
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
            celda = fila.createCell((short)10);
            celda.setCellValue(filaI.getPuestosRenAntesRes() != null ? filaI.getPuestosRenAntesRes() : 0);
            totalPuestosRenAntesRes += filaI.getPuestosRenAntesRes() != null ? filaI.getPuestosRenAntesRes() : 0;
            celda.setCellStyle(estiloCelda);
            celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);

            //COLUMNA: MOTIVO
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
            celda = fila.createCell((short)11);
            celda.setCellValue(filaI.getMotivo() != null ? filaI.getMotivo() : "");
            celda.setCellStyle(estiloCelda);

            //COLUMNA: OFERTA
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
            celda = fila.createCell((short)12);
            celda.setCellValue(filaI.getOferta() != null ? filaI.getOferta() : "");
            celda.setCellStyle(estiloCelda);

            //COLUMNA: PERSONAS PRECANDIDATAS
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
            celda = fila.createCell((short)13);
            celda.setCellValue(filaI.getPersPrecandidatas() != null ? filaI.getPersPrecandidatas().toUpperCase() : "");
            celda.setCellStyle(estiloCelda);

            //COLUMNA: DIFUSION
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
            celda = fila.createCell((short)14);
            celda.setCellValue(filaI.getDifusion() != null ? filaI.getDifusion().toUpperCase() : "");
            celda.setCellStyle(estiloCelda);

            //COLUMNA: FECHA_ENV_PERS_CANDIDATAS
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
            celda = fila.createCell((short)15);
            celda.setCellValue(filaI.getFecEnvPersPrecandidatas() != null ? filaI.getFecEnvPersPrecandidatas() : "");
            celda.setCellStyle(estiloCelda);

            //COLUMNA: NUMERO_CANDIDATOS
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
            celda = fila.createCell((short)16);
            celda.setCellValue(filaI.getNumCandidatos() != null ? filaI.getNumCandidatos() : 0);
            totalPersCandidatas += filaI.getNumCandidatos() != null ? filaI.getNumCandidatos() : 0;
            celda.setCellStyle(estiloCelda);
            celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);

            //COLUMNA: PUESTOS_REN_ANTES_1PAGO
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
            celda = fila.createCell((short)17);
            celda.setCellValue(filaI.getPuestosRenAntPrimPago() != null ? filaI.getPuestosRenAntPrimPago() : 0);
            totalPuestosRenAntesPag1 += filaI.getPuestosRenAntPrimPago() != null ? filaI.getPuestosRenAntPrimPago() : 0;
            celda.setCellStyle(estiloCelda);
            celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);

            //COLUMNA: CAUSA RENUNCIA
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
            celda = fila.createCell((short)18);
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
            int maxLengthNombre = 0;
            if(nomApe.length() > maxLengthNombre)
            {
                maxLengthNombre = nomApe.length();
            }

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
            celda = fila.createCell((short)19);
            celda.setCellValue(nomApe != null ? nomApe.toUpperCase() : "");
            celda.setCellStyle(estiloCelda);

            //COLUMNA: FECHA_NACIMIENTO
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
            celda = fila.createCell((short)20);
            celda.setCellValue(filaI.getFecNac() != null ? filaI.getFecNac() : "");
            celda.setCellStyle(estiloCelda);

            //COLUMNA: EDAD
            Integer edad = MeLanbide59Utils.calcularEdadFormulaLanb(filaI.getFecNac() != null ? format.parse(filaI.getFecNac()) : null, filaI.getFecIni()!= null ? format.parse(filaI.getFecIni()) : null);

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
            celda = fila.createCell((short)21);
            celda.setCellValue(edad!= null ? edad : 0);
            celda.setCellStyle(estiloCelda);
            celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);

            //COLUMNA: SEXO
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
            celda = fila.createCell((short)22);
            celda.setCellValue(filaI.getSexo() != null ? filaI.getSexo().toUpperCase() : "");
            celda.setCellStyle(estiloCelda);

            //COLUMNA: PAIS_CONTRATACION

            String paisCont = filaI.getPaisCont1() != null ? filaI.getPaisCont1().toUpperCase() : "";
            paisCont += filaI.getPaisCont2() != null && !filaI.getPaisCont2().equals("") ? (paisCont != null && !paisCont.equals("") ? System.getProperty("line.separator") : "") : "";
            paisCont += filaI.getPaisCont2() != null && !filaI.getPaisCont2().equals("") ? filaI.getPaisCont2().toUpperCase() : "";
            paisCont += filaI.getPaisCont3() != null && !filaI.getPaisCont3().equals("") ? (paisCont != null && !paisCont.equals("") ? System.getProperty("line.separator") : "") : "";
            paisCont += filaI.getPaisCont3() != null && !filaI.getPaisCont3().equals("") ? filaI.getPaisCont3().toUpperCase() : "";

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
            celda = fila.createCell((short)23);
            celda.setCellValue(paisCont != null ? paisCont.toUpperCase() : "");
            celda.setCellStyle(estiloCelda);

            //COLUMNA: TITULACION_PERS_CANDIDATA
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
            celda = fila.createCell((short)24);
            celda.setCellValue(filaI.getTitulacionSelec() != null ? filaI.getTitulacionSelec().toUpperCase() : "");
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
                    //estiloCelda.setBorderTop(HSSFCellStyle.BORDER_DOUBLE);
                    estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                }
            if(numFila == listaPuestos.size())
            {
                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
            }
            celda = fila.createCell((short)25);
            celda.setCellValue(filaI.getNivelFormativo() != null ? filaI.getNivelFormativo().toUpperCase() : "");
            celda.setCellStyle(estiloCelda);

            //COLUMNA: FECHA_INICIO
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
            celda = fila.createCell((short)26);
            celda.setCellValue(filaI.getFecIni() != null ? filaI.getFecIni() : "");
            celda.setCellStyle(estiloCelda);

            //COLUMNA: FECHA_FIN
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
            celda = fila.createCell((short)27);
            celda.setCellValue(filaI.getFecFin() != null ? filaI.getFecFin() : "");
            celda.setCellStyle(estiloCelda);

            //COLUMNA: TIEMPO CONTRATACION
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
            celda = fila.createCell((short)28);
            celda.setCellValue(filaI.getTiempoContratacion() != null ? filaI.getTiempoContratacion() : 0);
            celda.setCellStyle(estiloCelda);
            celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);

            //COLUMNA: PUESTOS CONTRATADOS
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
            estiloCelda.setBorderRight(HSSFCellStyle.BORDER_THICK);
            celda = fila.createCell((short)29);
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
            MeLanbide59I18n meLanbide59I18n = MeLanbide59I18n.getInstance();
            
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
                celda = fila.createCell((short)0);
                celda.setCellValue(meLanbide59I18n.getMensaje(idioma, "doc.informePuestosContratados.col1").toUpperCase());
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
                celda = fila.createCell((short)1);
                celda.setCellValue(meLanbide59I18n.getMensaje(idioma, "doc.informePuestosContratados.col2").toUpperCase());
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
                celda = fila.createCell((short)2);
                celda.setCellValue(meLanbide59I18n.getMensaje(idioma, "doc.informePuestosContratados.col31").toUpperCase());
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
                celda = fila.createCell((short)3);
                celda.setCellValue(meLanbide59I18n.getMensaje(idioma, "doc.informePuestosContratados.col3").toUpperCase());
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
                celda = fila.createCell((short)4);
                celda.setCellValue(meLanbide59I18n.getMensaje(idioma, "doc.informePuestosContratados.col4").toUpperCase());
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
                celda = fila.createCell((short)5);
                celda.setCellValue(meLanbide59I18n.getMensaje(idioma, "doc.informePuestosContratados.col5").toUpperCase());
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
                celda = fila.createCell((short)6);
                celda.setCellValue(meLanbide59I18n.getMensaje(idioma, "doc.informePuestosContratados.col6").toUpperCase());
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
                celda = fila.createCell((short)7);
                celda.setCellValue(meLanbide59I18n.getMensaje(idioma, "doc.informePuestosContratados.col7").toUpperCase());
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
                celda = fila.createCell((short)8);
                celda.setCellValue(meLanbide59I18n.getMensaje(idioma, "doc.informePuestosContratados.col8").toUpperCase());
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
                celda = fila.createCell((short)9);
                celda.setCellValue(meLanbide59I18n.getMensaje(idioma, "doc.informePuestosContratados.col9").toUpperCase());
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
                celda = fila.createCell((short)10);
                celda.setCellValue(meLanbide59I18n.getMensaje(idioma, "doc.informePuestosContratados.col10").toUpperCase());
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
                celda = fila.createCell((short)11);
                celda.setCellValue(meLanbide59I18n.getMensaje(idioma, "doc.informePuestosContratados.col11").toUpperCase());
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
                celda = fila.createCell((short)12);
                celda.setCellValue(meLanbide59I18n.getMensaje(idioma, "doc.informePuestosContratados.col12").toUpperCase());
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
                celda = fila.createCell((short)13);
                celda.setCellValue(meLanbide59I18n.getMensaje(idioma, "doc.informePuestosContratados.col13").toUpperCase());
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
                celda = fila.createCell((short)14);
                celda.setCellValue(meLanbide59I18n.getMensaje(idioma, "doc.informePuestosContratados.col14").toUpperCase());
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
                celda = fila.createCell((short)15);
                celda.setCellValue(meLanbide59I18n.getMensaje(idioma, "doc.informePuestosContratados.col15").toUpperCase());
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
                celda = fila.createCell((short)16);
                celda.setCellValue(meLanbide59I18n.getMensaje(idioma, "doc.informePuestosContratados.col16").toUpperCase());
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
                celda = fila.createCell((short)17);
                celda.setCellValue(meLanbide59I18n.getMensaje(idioma, "doc.informePuestosContratados.col29").toUpperCase());
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
                celda = fila.createCell((short)18);
                celda.setCellValue(meLanbide59I18n.getMensaje(idioma, "doc.informePuestosContratados.col30").toUpperCase());
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
                celda = fila.createCell((short)19);
                celda.setCellValue(meLanbide59I18n.getMensaje(idioma, "doc.informePuestosContratados.col17").toUpperCase());
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
                celda = fila.createCell((short)20);
                celda.setCellValue(meLanbide59I18n.getMensaje(idioma, "doc.informePuestosContratados.col18").toUpperCase());
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
        MeLanbide59I18n meLanbide59I18n = MeLanbide59I18n.getInstance();
        
        //COLUMNA: NUM. EXPEDIENTE
        estiloCelda = libro.createCellStyle();
        estiloCelda.setWrapText(true);
        estiloCelda.setFont(fuente);
        estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
        estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);

        celda = fila.createCell((short)0);
        celda.setCellStyle(estiloCelda);
        
        //COLUMNA: EMPRESA
        estiloCelda = libro.createCellStyle();
        estiloCelda.setWrapText(true);
        estiloCelda.setFont(fuente);
        estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);

        celda = fila.createCell((short)1);
        celda.setCellStyle(estiloCelda);

        //COLUMNA: TERRITORIO HISTORICO
        estiloCelda = libro.createCellStyle();
        estiloCelda.setWrapText(true);
        estiloCelda.setFont(fuente);
        estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        estiloCelda.setBorderTop(HSSFCellStyle.BORDER_DOUBLE);
        estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);

        celda = fila.createCell((short)2);
        celda.setCellValue(meLanbide59I18n.getMensaje(idioma, "doc.informePuestosContratados.total").toUpperCase());
        celda.setCellStyle(estiloCelda);

        //COLUMNA: PUESTOS SOLICITADOS
        estiloCelda = libro.createCellStyle();
        estiloCelda.setWrapText(true);
        estiloCelda.setFont(fuente);
        estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        estiloCelda.setBorderTop(HSSFCellStyle.BORDER_DOUBLE);
        estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);

        celda = fila.createCell((short)3);
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

        celda = fila.createCell((short)4);
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

        celda = fila.createCell((short)5);
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

        celda = fila.createCell((short)6);
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

        celda = fila.createCell((short)7);
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

        celda = fila.createCell((short)8);
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

        celda = fila.createCell((short)9);
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

        celda = fila.createCell((short)10);
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

        celda = fila.createCell((short)11);
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

        celda = fila.createCell((short)12);
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

        celda = fila.createCell((short)13);
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

        celda = fila.createCell((short)14);
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

        celda = fila.createCell((short)15);
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

        celda = fila.createCell((short)16);
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

        celda = fila.createCell((short)17);
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

        celda = fila.createCell((short)18);
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

        celda = fila.createCell((short)19);
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

        celda = fila.createCell((short)20);
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

        celda = fila.createCell((short)21);
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

        celda = fila.createCell((short)22);
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

        celda = fila.createCell((short)23);
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

        celda = fila.createCell((short)24);
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

        celda = fila.createCell((short)25);
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

        celda = fila.createCell((short)26);
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

        celda = fila.createCell((short)27);
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

        celda = fila.createCell((short)28);
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

        celda = fila.createCell((short)29);
        celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
        celda.setCellValue(totalContRealizados);
        celda.setCellStyle(estiloCelda);
    }
    
    public void generarInformeResumenEconomico(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response)
    {
        String rutaArchivoSalida = null;
        int i = 0;
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
            
            MeLanbide59I18n meLanbide59I18n = MeLanbide59I18n.getInstance();
            MeLanbide59Manager manager = MeLanbide59Manager.getInstance();
            
            try 
            {
                HSSFWorkbook libro = new HSSFWorkbook();
           
                //se escribe el fichero
                File directorioTemp = new File(m_Conf.getString("PDF.base_dir"));
                log.debug("directorioTemp" + directorioTemp.toString());
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
                HSSFCellStyle estilocelda2 = null;

                hoja = libro.createSheet();
                HSSFSheet hoja2 = libro.createSheet("sin formato");

                //Se establece el ancho de cada columnas
                hoja.setColumnWidth((short)0, (short)6000);//Num Expediente
                hoja.setColumnWidth((short)1, (short)10000);//Entidad
                hoja.setColumnWidth((short)2, (short)8000);//Territorio historico
                hoja.setColumnWidth((short)3, (short)4000);//CNAE
                hoja.setColumnWidth((short)4, (short)5000);//Num puestos solic
                hoja.setColumnWidth((short)5, (short)10000);//puesto
                hoja.setColumnWidth((short)6, (short)6000);//resultado
                hoja.setColumnWidth((short)7, (short)5000);//fecha inicio
                hoja.setColumnWidth((short)8, (short)5000);//fecha fin
                hoja.setColumnWidth((short)9, (short)4000);//periodo meses
                hoja.setColumnWidth((short)10, (short)12000);//titulacion
                hoja.setColumnWidth((short)11, (short)8000);//pais
                hoja.setColumnWidth((short)12, (short)5000);//costes contrato salario + ss
                hoja.setColumnWidth((short)13, (short)5000);//dietas convenio
                hoja.setColumnWidth((short)14, (short)5000);//coste de la contratacion
                hoja.setColumnWidth((short)15, (short)5000);//salario anual bruto
                hoja.setColumnWidth((short)16, (short)5000);//dietas alojamiento y manutencion
                hoja.setColumnWidth((short)17, (short)5000);//tramitacion de permisos y seguros
                hoja.setColumnWidth((short)18, (short)5000);//importe total solicitado
                hoja.setColumnWidth((short)19, (short)5000);//dotacion anual
                hoja.setColumnWidth((short)20, (short)5000);//meses subvencionables
                hoja.setColumnWidth((short)21, (short)5000);//dietas subvencionables
                hoja.setColumnWidth((short)22, (short)5000);//tramitacion de permisos y seguros
                hoja.setColumnWidth((short)23, (short)2700);//minimis
                hoja.setColumnWidth((short)24, (short)4000);//importe maximo subvencionable
                hoja.setColumnWidth((short)25, (short)3500);//importe concedido
                hoja.setColumnWidth((short)26, (short)4500);//num renuncias
                hoja.setColumnWidth((short)27, (short)3000);//importe renuncia
                hoja.setColumnWidth((short)28, (short)5000);//motivo renuncia
                hoja.setColumnWidth((short)29, (short)5000);//importe concedido tras res modif
                
                hoja2.setColumnWidth((short)0, (short)6000);//Num Expediente
                hoja2.setColumnWidth((short)1, (short)10000);//Entidad
                hoja2.setColumnWidth((short)2, (short)8000);//Territorio historico
                hoja2.setColumnWidth((short)3, (short)4000);//CNAE
                hoja2.setColumnWidth((short)4, (short)5000);//Num puestos solic
                hoja2.setColumnWidth((short)5, (short)10000);//puesto
                hoja2.setColumnWidth((short)6, (short)6000);//resultado
                hoja2.setColumnWidth((short)7, (short)5000);//fecha inicio
                hoja2.setColumnWidth((short)8, (short)5000);//fecha fin
                hoja2.setColumnWidth((short)9, (short)4000);//periodo meses
                hoja2.setColumnWidth((short)10, (short)12000);//titulacion
                hoja2.setColumnWidth((short)11, (short)8000);//pais
                hoja2.setColumnWidth((short)12, (short)5000);//costes contrato salario + ss
                hoja2.setColumnWidth((short)13, (short)5000);//dietas convenio
                hoja2.setColumnWidth((short)14, (short)5000);//coste de la contratacion
                hoja2.setColumnWidth((short)15, (short)5000);//salario anual bruto
                hoja2.setColumnWidth((short)16, (short)5000);//dietas alojamiento y manutencion
                hoja2.setColumnWidth((short)17, (short)5000);//tramitacion de permisos y seguros
                hoja2.setColumnWidth((short)18, (short)5000);//importe total solicitado
                hoja2.setColumnWidth((short)19, (short)5000);//dotacion anual
                hoja2.setColumnWidth((short)20, (short)5000);//meses subvencionables
                hoja2.setColumnWidth((short)21, (short)5000);//dietas subvencionables
                hoja2.setColumnWidth((short)22, (short)5000);//tramitacion de permisos y seguros
                hoja2.setColumnWidth((short)23, (short)2700);//minimis
                hoja2.setColumnWidth((short)24, (short)4000);//importe maximo subvencionable
                hoja2.setColumnWidth((short)25, (short)3500);//importe concedido
                hoja2.setColumnWidth((short)26, (short)4500);//num renuncias
                hoja2.setColumnWidth((short)27, (short)3000);//importe renuncia
                hoja2.setColumnWidth((short)28, (short)5000);//motivo renuncia
                hoja2.setColumnWidth((short)29, (short)5000);//importe concedido tras res modif

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
                celda = fila.createCell((short)0);
                celda.setCellValue(meLanbide59I18n.getMensaje(idioma, "doc.informeEconomico.col1").toUpperCase());
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
                celda = fila.createCell((short)1);
                celda.setCellValue(meLanbide59I18n.getMensaje(idioma, "doc.informeEconomico.col2").toUpperCase());
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
                celda = fila.createCell((short)2);
                celda.setCellValue(meLanbide59I18n.getMensaje(idioma, "doc.informeEconomico.col30").toUpperCase());
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
                celda = fila.createCell((short)3);
                celda.setCellValue(meLanbide59I18n.getMensaje(idioma, "doc.informeEconomico.col3").toUpperCase());
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
                celda = fila.createCell((short)4);
                celda.setCellValue(meLanbide59I18n.getMensaje(idioma, "doc.informeEconomico.col4").toUpperCase());
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
                celda = fila.createCell((short)5);
                celda.setCellValue(meLanbide59I18n.getMensaje(idioma, "doc.informeEconomico.col5").toUpperCase());
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
                celda = fila.createCell((short)6);
                celda.setCellValue(meLanbide59I18n.getMensaje(idioma, "doc.informeEconomico.col6").toUpperCase());
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
                celda = fila.createCell((short)7);
                celda.setCellValue(meLanbide59I18n.getMensaje(idioma, "doc.informeEconomico.col7").toUpperCase());
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
                celda = fila.createCell((short)8);
                celda.setCellValue(meLanbide59I18n.getMensaje(idioma, "doc.informeEconomico.col8").toUpperCase());
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
                celda = fila.createCell((short)9);
                celda.setCellValue(meLanbide59I18n.getMensaje(idioma, "doc.informeEconomico.col9").toUpperCase());
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
                celda = fila.createCell((short)10);
                celda.setCellValue(meLanbide59I18n.getMensaje(idioma, "doc.informeEconomico.col10").toUpperCase());
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
                celda = fila.createCell((short)11);
                celda.setCellValue(meLanbide59I18n.getMensaje(idioma, "doc.informeEconomico.col11").toUpperCase());
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
                celda = fila.createCell((short)12);
                celda.setCellValue(meLanbide59I18n.getMensaje(idioma, "doc.informeEconomico.col12").toUpperCase());
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
                celda = fila.createCell((short)13);
                celda.setCellValue(meLanbide59I18n.getMensaje(idioma, "doc.informeEconomico.col13").toUpperCase());
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
                celda = fila.createCell((short)14);
                celda.setCellValue(meLanbide59I18n.getMensaje(idioma, "doc.informeEconomico.col14").toUpperCase());
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
                celda = fila.createCell((short)15);
                celda.setCellValue(meLanbide59I18n.getMensaje(idioma, "doc.informeEconomico.col15").toUpperCase());
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
                celda = fila.createCell((short)16);
                celda.setCellValue(meLanbide59I18n.getMensaje(idioma, "doc.informeEconomico.col16").toUpperCase());
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
                celda = fila.createCell((short)17);
                celda.setCellValue(meLanbide59I18n.getMensaje(idioma, "doc.informeEconomico.col17").toUpperCase());
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
                celda = fila.createCell((short)18);
                celda.setCellValue(meLanbide59I18n.getMensaje(idioma, "doc.informeEconomico.col18").toUpperCase());
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
                celda = fila.createCell((short)19);
                celda.setCellValue(meLanbide59I18n.getMensaje(idioma, "doc.informeEconomico.col19").toUpperCase());
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
                celda = fila.createCell((short)20);
                celda.setCellValue(meLanbide59I18n.getMensaje(idioma, "doc.informeEconomico.col20").toUpperCase());
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
                celda = fila.createCell((short)21);
                celda.setCellValue(meLanbide59I18n.getMensaje(idioma, "doc.informeEconomico.col21").toUpperCase());
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
                celda = fila.createCell((short)22);
                celda.setCellValue(meLanbide59I18n.getMensaje(idioma, "doc.informeEconomico.col22").toUpperCase());
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
                celda = fila.createCell((short)23);
                celda.setCellValue(meLanbide59I18n.getMensaje(idioma, "doc.informeEconomico.col23").toUpperCase());
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
                celda = fila.createCell((short)24);
                celda.setCellValue(meLanbide59I18n.getMensaje(idioma, "doc.informeEconomico.col24").toUpperCase());
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
                celda = fila.createCell((short)25);
                celda.setCellValue(meLanbide59I18n.getMensaje(idioma, "doc.informeEconomico.col25").toUpperCase());
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
                celda = fila.createCell((short)26);
                celda.setCellValue(meLanbide59I18n.getMensaje(idioma, "doc.informeEconomico.col26").toUpperCase());
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
                celda = fila.createCell((short)27);
                celda.setCellValue(meLanbide59I18n.getMensaje(idioma, "doc.informeEconomico.col27").toUpperCase());
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
                celda = fila.createCell((short)28);
                celda.setCellValue(meLanbide59I18n.getMensaje(idioma, "doc.informeEconomico.col28").toUpperCase());
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
                celda = fila.createCell((short)29);
                celda.setCellValue(meLanbide59I18n.getMensaje(idioma, "doc.informeEconomico.col29").toUpperCase());
                celda.setCellStyle(estiloCelda);
                
                crearEstiloHoja2Economico(libro, fila2, celda2, estilocelda2, idioma);
                
                
                
                boolean nuevo = true;
                HashMap<String, String> datosTercero = null;
                String empresa = null;
                String titulacion = null;
                String pais = null;
                List<FilaInformeResumenEconomicoVO> listaPuestos = null;
                int n = 0;
                int p = 0;
                int numPaises = 0;
                int height = 0;
                
                String numExp = null;
                
                listaPuestos = manager.getDatosInformeResumenEconomico(ano, adapt);
                
                
                //Insertamos los datos, fila a fila
                for(FilaInformeResumenEconomicoVO filaI : listaPuestos)
                {
                    i++;
                    if(filaI.getNumExpediente() != null && !filaI.getNumExpediente().equals(numExp))
                    {
                        if(p != 0)
                        {
                                //cambiar a poi 3.10
                                /*hoja.addMergedRegion(new Region(numFila-p, (short)0, numFila, (short)0));
                            hoja.addMergedRegion(new Region(numFila-p, (short)1, numFila, (short)1));
                            hoja.addMergedRegion(new Region(numFila-p, (short)2, numFila, (short)2));
                            hoja.addMergedRegion(new Region(numFila-p, (short)3, numFila-1, (short)3));
                                */
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
                        if(!"TOTAL".equals(filaI.getCnae09()))
                            numFila2++;    
                    }
                    else
                    {
                        numFila++;    
                        if(!"TOTAL".equals(filaI.getCnae09()))
                            numFila2++;    
                        nuevo = false;
                        p++;
                    }
                    
                    fila = hoja.createRow(numFila);
                    fila2 = hoja2.createRow(numFila2);
                    
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
                    //fila2.setHeight((short)height);
                    
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

                    //COLUMNA: NUM EXPEDIENTE
                    estiloCelda = libro.createCellStyle();
                    estiloCelda.setWrapText(true);
                    estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                    if(nuevo)
                    {
                        estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                    }
                    if(numFila == listaPuestos.size())
                    {
                        estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                    }
                    //estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                    estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                    estiloCelda.setFont(normal);
                    celda = fila.createCell((short)0);
                    if(nuevo)
                    {
                        celda.setCellValue(numExp);
                    }
                    celda.setCellStyle(estiloCelda);

                    //COLUMNA: ENTIDAD
                    if(filaI.getEntidad() != null)
                    {
                        empresa = filaI.getEntidad();
                    }
                    else
                    {
                        datosTercero = MeLanbide59Manager.getInstance().getDatosTercero(codOrganizacion, numExpediente, String.valueOf(ano), ConstantesMeLanbide59.CODIGO_ROL_ENTIDAD_SOLICITANTE, adapt);
                        if(datosTercero.containsKey("TER_NOC"))
                        {
                            empresa = datosTercero.get("TER_NOC");
                        }
                        else
                        {
                            empresa = "";
                        }
                    }
                    log.debug("empresa");
                    estiloCelda = libro.createCellStyle();
                    estiloCelda.setWrapText(true);
                    estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THIN);
                    if(nuevo)
                    {
                        estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                    }
                    if(numFila == listaPuestos.size())
                    {
                        estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                    }
                    estiloCelda.setFont(normal);
                    //estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                    estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                    celda = fila.createCell((short)1);
                    if(nuevo)
                    {
                        celda.setCellValue(empresa != null ? empresa.toUpperCase() : "");
                    }
                    celda.setCellStyle(estiloCelda);

                    //COLUMNA: TERRITORIO HISTORICO
                    estiloCelda = libro.createCellStyle();
                    estiloCelda.setWrapText(true);
                    estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THIN);
                    if(nuevo)
                    {
                        estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                    }
                    if(numFila == listaPuestos.size())
                    {
                        estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                    }
                    estiloCelda.setFont(normal);
                    //estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
                    estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                    celda = fila.createCell((short)2);
                    if(nuevo)
                    {
                        celda.setCellValue(filaI.getTerritorioHistorico() != null ? filaI.getTerritorioHistorico().toUpperCase() : "");
                    }
                    celda.setCellStyle(estiloCelda);
                    log.debug("territorio historico");
                    //COLUMNA: CNAE09
                    estiloCelda = libro.createCellStyle();
                    estiloCelda.setWrapText(true);
                    estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THIN);
                    if(nuevo)
                    {
                        estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                    }
                    else if(filaI.getOrden() != null && filaI.getOrden() == 2)
                    {
                        estiloCelda.setBorderTop(HSSFCellStyle.BORDER_DOUBLE);
                    }
                    else if(p > 0)
                    {
                        estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THIN);
                    }
                    if(numFila == listaPuestos.size())
                    {
                        estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                    }
                    estiloCelda.setFont(normal);
                    celda = fila.createCell((short)3);
                    String valor = filaI.getCnae09() != null ? filaI.getCnae09().toUpperCase() : "";
                    if(!valor.equals(""))
                        valor = valor.replaceAll("TOTAL", meLanbide59I18n.getMensaje(idioma, "doc.informeEconomico.total").toUpperCase());
                    if(valor.equals(meLanbide59I18n.getMensaje(idioma, "doc.informeEconomico.total").toUpperCase()) || nuevo)
                    {
                        celda.setCellValue(valor);
                    }
                    celda.setCellStyle(estiloCelda);

                    //COLUMNA: Nº PUESTOS SOLIC
                    estiloCelda = libro.createCellStyle();
                    estiloCelda.setWrapText(true);
                    estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THIN);
                    if(nuevo)
                    {
                        estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                    }
                    else if(filaI.getOrden() != null && filaI.getOrden() == 2)
                    {
                        estiloCelda.setBorderTop(HSSFCellStyle.BORDER_DOUBLE);
                    }
                    else if(p > 0)
                    {
                        estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THIN);
                    }
                    if(numFila == listaPuestos.size())
                    {
                        estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                    }
                    estiloCelda.setFont(normal);
                    estiloCelda.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
                    celda = fila.createCell((short)4);
                    celda.setCellValue(filaI.getnPuestosSolicitados() != null ? filaI.getnPuestosSolicitados() : 0);
                    celda.setCellStyle(estiloCelda);
                    celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                    
                    //COLUMNA: PUESTO
                    estiloCelda = libro.createCellStyle();
                    estiloCelda.setWrapText(true);
                    estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THIN);
                    if(nuevo)
                    {
                        estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                    }
                    else if(filaI.getOrden() != null && filaI.getOrden() == 2)
                    {
                        estiloCelda.setBorderTop(HSSFCellStyle.BORDER_DOUBLE);
                    }
                    else if(p > 0)
                    {
                        estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THIN);
                    }
                    if(numFila == listaPuestos.size())
                    {
                        estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                    }
                    estiloCelda.setFont(normal);
                    celda = fila.createCell((short)5);
                    celda.setCellValue(filaI.getPuesto() != null ? filaI.getCodPuesto()+"-"+filaI.getPuesto().toUpperCase() : "");
                    celda.setCellStyle(estiloCelda);
                    
                    //COLUMNA: RESULTADO
                    estiloCelda = libro.createCellStyle();
                    estiloCelda.setWrapText(true);
                    estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THIN);
                    if(nuevo)
                    {
                        estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                    }
                    else if(filaI.getOrden() != null && filaI.getOrden() == 2)
                    {
                        estiloCelda.setBorderTop(HSSFCellStyle.BORDER_DOUBLE);
                    }
                    if(numFila == listaPuestos.size())
                    {
                        estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                    }
                    estiloCelda.setFont(normal);
                    celda = fila.createCell((short)6);
                    celda.setCellValue(filaI.getResultado() != null ? filaI.getResultado() : "");
                    celda.setCellStyle(estiloCelda);
                    

                    //COLUMNA: FECHA INI
                    estiloCelda = libro.createCellStyle();
                    estiloCelda.setWrapText(true);
                    estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THIN);
                    if(nuevo)
                    {
                        estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                    }
                    else if(filaI.getOrden() != null && filaI.getOrden() == 2)
                    {
                        estiloCelda.setBorderTop(HSSFCellStyle.BORDER_DOUBLE);
                    }
                    if(numFila == listaPuestos.size())
                    {
                        estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                    }
                    estiloCelda.setFont(normal);
                    celda = fila.createCell((short)7);
                    celda.setCellValue(filaI.getFecIni() != null ? filaI.getFecIni() : "");
                    celda.setCellStyle(estiloCelda);
                   
                    
                    //COLUMNA: FECHA FIN
                    estiloCelda = libro.createCellStyle();
                    estiloCelda.setWrapText(true);
                    estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THIN);
                    if(nuevo)
                    {
                        estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                    }
                    else if(filaI.getOrden() != null && filaI.getOrden() == 2)
                    {
                        estiloCelda.setBorderTop(HSSFCellStyle.BORDER_DOUBLE);
                    }
                    if(numFila == listaPuestos.size())
                    {
                        estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                    }
                    estiloCelda.setFont(normal);
                    celda = fila.createCell((short)8);
                    celda.setCellValue(filaI.getFecFin() != null ? filaI.getFecFin() : "");
                    celda.setCellStyle(estiloCelda);
                    
                    
                    //COLUMNA: PERIODO MESES SUBV
                    estiloCelda = libro.createCellStyle();
                    estiloCelda.setWrapText(true);
                    estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THIN);
                    if(nuevo)
                    {
                        estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                    }
                    else if(filaI.getOrden() != null && filaI.getOrden() == 2)
                    {
                        estiloCelda.setBorderTop(HSSFCellStyle.BORDER_DOUBLE);
                    }
                    if(numFila == listaPuestos.size())
                    {
                        estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                    }
                    estiloCelda.setFont(normal);
                    estiloCelda.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
                    celda = fila.createCell((short)9);
                    celda.setCellValue(filaI.getPeriodoMesesSubv() != null ? filaI.getPeriodoMesesSubv() : 0);
                    celda.setCellStyle(estiloCelda);
                    celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                    //COLUMNA: TITULACION AB
                    estiloCelda = libro.createCellStyle();
                    estiloCelda.setWrapText(true);
                    estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THIN);
                    if(nuevo)
                    {
                        estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                    }
                    else if(filaI.getOrden() != null && filaI.getOrden() == 2)
                    {
                        estiloCelda.setBorderTop(HSSFCellStyle.BORDER_DOUBLE);
                    }
                    if(numFila == listaPuestos.size())
                    {
                        estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                    }
                    estiloCelda.setFont(normal);
                    celda = fila.createCell((short)10);
                    celda.setCellValue(filaI.getTitulacionAB() != null ? filaI.getTitulacionAB() : "");
                    celda.setCellStyle(estiloCelda);
                    
                    //COLUMNA: PAIS
                    estiloCelda = libro.createCellStyle();
                    estiloCelda.setWrapText(true);
                    estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THIN);
                    if(nuevo)
                    {
                        estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                    }
                    else if(filaI.getOrden() != null && filaI.getOrden() == 2)
                    {
                        estiloCelda.setBorderTop(HSSFCellStyle.BORDER_DOUBLE);
                    }
                    if(numFila == listaPuestos.size())
                    {
                        estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                    }
                    estiloCelda.setFont(normal);
                    celda = fila.createCell((short)11);

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
                    estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THIN);
                    if(nuevo)
                    {
                        estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                    }
                    else if(filaI.getOrden() != null && filaI.getOrden() == 2)
                    {
                        estiloCelda.setBorderTop(HSSFCellStyle.BORDER_DOUBLE);
                    }
                    if(numFila == listaPuestos.size())
                    {
                        estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                    }
                    estiloCelda.setFont(normal);
                    estiloCelda.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
                    celda = fila.createCell((short)12);
                    celda.setCellValue(filaI.getCostesContSalarioSS() != null ? MeLanbide59Utils.redondearDecimalesBigDecimal(filaI.getCostesContSalarioSS(), 2).doubleValue() : new BigDecimal(ConstantesMeLanbide59.CERO_CON_DECIMALES).doubleValue());
                    celda.setCellStyle(estiloCelda);
                    celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                    
                    //COLUMNA: DIETAS CONVENIO
                    estiloCelda = libro.createCellStyle();
                    estiloCelda.setWrapText(true);
                    estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THIN);
                    if(nuevo)
                    {
                        estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                    }
                    else if(filaI.getOrden() != null && filaI.getOrden() == 2)
                    {
                        estiloCelda.setBorderTop(HSSFCellStyle.BORDER_DOUBLE);
                    }
                    if(numFila == listaPuestos.size())
                    {
                        estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                    }
                    estiloCelda.setFont(normal);
                    estiloCelda.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
                    celda = fila.createCell((short)13);
                    celda.setCellValue(filaI.getCostesContDietasConve() != null ? MeLanbide59Utils.redondearDecimalesBigDecimal(filaI.getCostesContDietasConve(), 2).doubleValue() : new BigDecimal(ConstantesMeLanbide59.CERO_CON_DECIMALES).doubleValue());
                    celda.setCellStyle(estiloCelda);
                    celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                    
                    //COLUMNA: COSTES CONTRATACION
                    estiloCelda = libro.createCellStyle();
                    estiloCelda.setWrapText(true);
                    estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THIN);
                    if(nuevo)
                    {
                        estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                    }
                    else if(filaI.getOrden() != null && filaI.getOrden() == 2)
                    {
                        estiloCelda.setBorderTop(HSSFCellStyle.BORDER_DOUBLE);
                    }
                    if(numFila == listaPuestos.size())
                    {
                        estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                    }
                    estiloCelda.setFont(normal);
                    estiloCelda.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
                    celda = fila.createCell((short)14);
                    celda.setCellValue(filaI.getCostesCont() != null ? MeLanbide59Utils.redondearDecimalesBigDecimal(filaI.getCostesCont(), 2).doubleValue() : new BigDecimal(ConstantesMeLanbide59.CERO_CON_DECIMALES).doubleValue());
                    celda.setCellStyle(estiloCelda);
                    celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);

                    //COLUMNA: SALARIO ANUAL BRUTO
                    estiloCelda = libro.createCellStyle();
                    estiloCelda.setWrapText(true);
                    estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THIN);
                    if(nuevo)
                    {
                        estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                    }
                    else if(filaI.getOrden() != null && filaI.getOrden() == 2)
                    {
                        estiloCelda.setBorderTop(HSSFCellStyle.BORDER_DOUBLE);
                    }
                    if(numFila == listaPuestos.size())
                    {
                        estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                    }
                    estiloCelda.setFont(normal);
                    estiloCelda.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
                    celda = fila.createCell((short)15);
                    celda.setCellValue(filaI.getSolSalarioSS() != null ? MeLanbide59Utils.redondearDecimalesBigDecimal(filaI.getSolSalarioSS(), 2).doubleValue() : new BigDecimal(ConstantesMeLanbide59.CERO_CON_DECIMALES).doubleValue());
                    celda.setCellStyle(estiloCelda);
                    celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);

                    //COLUMNA: DIETAS ALOJAMIENTO Y MANUTENCION
                    estiloCelda = libro.createCellStyle();
                    estiloCelda.setWrapText(true);
                    estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THIN);
                    if(nuevo)
                    {
                        estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                    }
                    else if(filaI.getOrden() != null && filaI.getOrden() == 2)
                    {
                        estiloCelda.setBorderTop(HSSFCellStyle.BORDER_DOUBLE);
                    }
                    if(numFila == listaPuestos.size())
                    {
                        estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                    }
                    estiloCelda.setFont(normal);
                    estiloCelda.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
                    celda = fila.createCell((short)16);
                    celda.setCellValue(filaI.getSolDietasConvSS() != null ? MeLanbide59Utils.redondearDecimalesBigDecimal(filaI.getSolDietasConvSS(), 2).doubleValue() : new BigDecimal(ConstantesMeLanbide59.CERO_CON_DECIMALES).doubleValue());
                    celda.setCellStyle(estiloCelda);
                    celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);

                    //COLUMNA: TRAMITACION DE PERMISOS Y SEGUROS MEDICOS
                    estiloCelda = libro.createCellStyle();
                    estiloCelda.setWrapText(true);
                    estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THIN);
                    if(nuevo)
                    {
                        estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                    }
                    else if(filaI.getOrden() != null && filaI.getOrden() == 2)
                    {
                        estiloCelda.setBorderTop(HSSFCellStyle.BORDER_DOUBLE);
                    }
                    if(numFila == listaPuestos.size())
                    {
                        estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                    }
                    estiloCelda.setFont(normal);
                    estiloCelda.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
                    celda = fila.createCell((short)17);
                    celda.setCellValue(filaI.getSolGastosTra() != null ? MeLanbide59Utils.redondearDecimalesBigDecimal(filaI.getSolGastosTra(), 2).doubleValue() : new BigDecimal(ConstantesMeLanbide59.CERO_CON_DECIMALES).doubleValue());
                    celda.setCellStyle(estiloCelda);
                    celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);

                    //COLUMNA: IMPORTE SOLICITADO
                    estiloCelda = libro.createCellStyle();
                    estiloCelda.setWrapText(true);
                    estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THIN);
                    if(nuevo)
                    {
                        estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                    }
                    else if(filaI.getOrden() != null && filaI.getOrden() == 2)
                    {
                        estiloCelda.setBorderTop(HSSFCellStyle.BORDER_DOUBLE);
                    }
                    if(numFila == listaPuestos.size())
                    {
                        estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                    }
                    estiloCelda.setFont(normal);
                    estiloCelda.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
                    celda = fila.createCell((short)18);
                    celda.setCellValue(filaI.getSolicitado() != null ? MeLanbide59Utils.redondearDecimalesBigDecimal(filaI.getSolicitado(), 2).doubleValue() : new BigDecimal(ConstantesMeLanbide59.CERO_CON_DECIMALES).doubleValue());
                    celda.setCellStyle(estiloCelda);
                    celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);

                    //COLUMNA: DOTACION ANUAL MAXIMA
                    estiloCelda = libro.createCellStyle();
                    estiloCelda.setWrapText(true);
                    estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THIN);
                    if(nuevo)
                    {
                        estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                    }
                    else if(filaI.getOrden() != null && filaI.getOrden() == 2)
                    {
                        estiloCelda.setBorderTop(HSSFCellStyle.BORDER_DOUBLE);
                    }
                    if(numFila == listaPuestos.size())
                    {
                        estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                    }
                    estiloCelda.setFont(normal);
                    estiloCelda.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
                    celda = fila.createCell((short)19);
                    celda.setCellValue(filaI.getDotacionAnualMaxima() != null ? MeLanbide59Utils.redondearDecimalesBigDecimal(filaI.getDotacionAnualMaxima(), 2).doubleValue() : new BigDecimal(ConstantesMeLanbide59.CERO_CON_DECIMALES).doubleValue());
                    celda.setCellStyle(estiloCelda);
                    celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);

                    //COLUMNA: MESES SUBVENCIONABLES
                    estiloCelda = libro.createCellStyle();
                    estiloCelda.setWrapText(true);
                    estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THIN);
                    if(nuevo)
                    {
                        estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                    }
                    else if(filaI.getOrden() != null && filaI.getOrden() == 2)
                    {
                        estiloCelda.setBorderTop(HSSFCellStyle.BORDER_DOUBLE);
                    }
                    if(numFila == listaPuestos.size())
                    {
                        estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                    }
                    estiloCelda.setFont(normal);
                    estiloCelda.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
                    celda = fila.createCell((short)20);
                    celda.setCellValue(filaI.getMesesSubvencionables() != null ? filaI.getMesesSubvencionables() : 0);
                    celda.setCellStyle(estiloCelda);
                    celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);

                    //COLUMNA: DIETAS SUBVENCIONABLES
                    estiloCelda = libro.createCellStyle();
                    estiloCelda.setWrapText(true);
                    estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THIN);
                    if(nuevo)
                    {
                        estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                    }
                    else if(filaI.getOrden() != null && filaI.getOrden() == 2)
                    {
                        estiloCelda.setBorderTop(HSSFCellStyle.BORDER_DOUBLE);
                    }
                    if(numFila == listaPuestos.size())
                    {
                        estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                    }
                    estiloCelda.setFont(normal);
                    estiloCelda.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
                    celda = fila.createCell((short)21);
                    celda.setCellValue(filaI.getDietasSubv() != null ? MeLanbide59Utils.redondearDecimalesBigDecimal(filaI.getDietasSubv(), 2).doubleValue() : new BigDecimal(ConstantesMeLanbide59.CERO_CON_DECIMALES).doubleValue());
                    celda.setCellStyle(estiloCelda);
                    celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);

                    //COLUMNA: TRAMITACION DE PERMISOS Y SEGUROS MEDICOS MAX 450
                    estiloCelda = libro.createCellStyle();
                    estiloCelda.setWrapText(true);
                    estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THIN);
                    if(nuevo)
                    {
                        estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                    }
                    else if(filaI.getOrden() != null && filaI.getOrden() == 2)
                    {
                        estiloCelda.setBorderTop(HSSFCellStyle.BORDER_DOUBLE);
                    }
                    if(numFila == listaPuestos.size())
                    {
                        estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                    }
                    estiloCelda.setFont(normal);
                    estiloCelda.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
                    celda = fila.createCell((short)22);
                    celda.setCellValue(filaI.getGastosTram() != null ? MeLanbide59Utils.redondearDecimalesBigDecimal(filaI.getGastosTram(), 2).doubleValue() : new BigDecimal(ConstantesMeLanbide59.CERO_CON_DECIMALES).doubleValue());
                    celda.setCellStyle(estiloCelda);
                    celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);

                    //COLUMNA: MINIMIS CONCEDIDOS
                    estiloCelda = libro.createCellStyle();
                    estiloCelda.setWrapText(true);
                    estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THIN);
                    if(nuevo)
                    {
                        estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                    }
                    else if(filaI.getOrden() != null && filaI.getOrden() == 2)
                    {
                        estiloCelda.setBorderTop(HSSFCellStyle.BORDER_DOUBLE);
                    }
                    if(numFila == listaPuestos.size())
                    {
                        estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                    }
                    estiloCelda.setFont(normal);
                    estiloCelda.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
                    celda = fila.createCell((short)23);
                    celda.setCellValue(filaI.getMinimisConcedidos() != null ? MeLanbide59Utils.redondearDecimalesBigDecimal(filaI.getMinimisConcedidos(), 2).doubleValue() : new BigDecimal(ConstantesMeLanbide59.CERO_CON_DECIMALES).doubleValue());
                    celda.setCellStyle(estiloCelda);
                    celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);

                    //COLUMNA: IMPORTE MAX SUBVENCIONABLE
                    estiloCelda = libro.createCellStyle();
                    estiloCelda.setWrapText(true);
                    estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THIN);
                    if(nuevo)
                    {
                        estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                    }
                    else if(filaI.getOrden() != null && filaI.getOrden() == 2)
                    {
                        estiloCelda.setBorderTop(HSSFCellStyle.BORDER_DOUBLE);
                    }
                    if(numFila == listaPuestos.size())
                    {
                        estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                    }
                    estiloCelda.setFont(normal);
                    estiloCelda.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
                    celda = fila.createCell((short)24);
                    celda.setCellValue(filaI.getImpMaxSubvencionable() != null ? MeLanbide59Utils.redondearDecimalesBigDecimal(filaI.getImpMaxSubvencionable(), 2).doubleValue() : new BigDecimal(ConstantesMeLanbide59.CERO_CON_DECIMALES).doubleValue());
                    celda.setCellStyle(estiloCelda);
                    celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);

                    //COLUMNA: IMPORTE CONCEDIDO
                    estiloCelda = libro.createCellStyle();
                    estiloCelda.setWrapText(true);
                    estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THIN);
                    if(nuevo)
                    {
                        estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                    }
                    else if(filaI.getOrden() != null && filaI.getOrden() == 2)
                    {
                        estiloCelda.setBorderTop(HSSFCellStyle.BORDER_DOUBLE);
                    }
                    if(numFila == listaPuestos.size())
                    {
                        estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                    }
                    estiloCelda.setFont(normal);
                    estiloCelda.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
                    celda = fila.createCell((short)25);
                    celda.setCellValue(filaI.getImporteConcedido() != null ? MeLanbide59Utils.redondearDecimalesBigDecimal(filaI.getImporteConcedido(), 2).doubleValue() : new BigDecimal(ConstantesMeLanbide59.CERO_CON_DECIMALES).doubleValue());
                    celda.setCellStyle(estiloCelda);
                    celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);

                    //COLUMNA: NUM RENUNCIAS
                    estiloCelda = libro.createCellStyle();
                    estiloCelda.setWrapText(true);
                    estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THIN);
                    if(nuevo)
                    {
                        estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                    }
                    else if(filaI.getOrden() != null && filaI.getOrden() == 2)
                    {
                        estiloCelda.setBorderTop(HSSFCellStyle.BORDER_DOUBLE);
                    }
                    if(numFila == listaPuestos.size())
                    {
                        estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                    }
                    estiloCelda.setFont(normal);
                    estiloCelda.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
                    celda = fila.createCell((short)26);
                    celda.setCellValue(filaI.getNumRenuncias() != null ? filaI.getNumRenuncias() : 0);
                    celda.setCellStyle(estiloCelda);
                    celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);

                    //COLUMNA: IMPORTE RENUNCIA
                    estiloCelda = libro.createCellStyle();
                    estiloCelda.setWrapText(true);
                    estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THIN);
                    if(nuevo)
                    {
                        estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                    }
                    else if(filaI.getOrden() != null && filaI.getOrden() == 2)
                    {
                        estiloCelda.setBorderTop(HSSFCellStyle.BORDER_DOUBLE);
                    }
                    if(numFila == listaPuestos.size())
                    {
                        estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                    }
                    estiloCelda.setFont(normal);
                    estiloCelda.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
                    celda = fila.createCell((short)27);
                    celda.setCellValue(filaI.getImporteRenuncia() != null ? MeLanbide59Utils.redondearDecimalesBigDecimal(filaI.getImporteRenuncia(), 2).doubleValue() : new BigDecimal(ConstantesMeLanbide59.CERO_CON_DECIMALES).doubleValue());
                    celda.setCellStyle(estiloCelda);
                    celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);

                    //COLUMNA: MOTIVO RENUNCIA
                    estiloCelda = libro.createCellStyle();
                    estiloCelda.setWrapText(true);
                    estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THIN);
                    if(nuevo)
                    {
                        estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                    }
                    else if(filaI.getOrden() != null && filaI.getOrden() == 2)
                    {
                        estiloCelda.setBorderTop(HSSFCellStyle.BORDER_DOUBLE);
                    }
                    if(numFila == listaPuestos.size())
                    {
                        estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                    }
                    estiloCelda.setFont(normal);
                    celda = fila.createCell((short)28);
                    
                    valor = filaI.getMotivo() != null ? filaI.getMotivo().toUpperCase() : "";
                    if(valor != null && !valor.equals(""))
                    {
                        valor = valor.replaceAll("-OBSERVACIONES:", " - "+meLanbide59I18n.getMensaje(idioma, "doc.informeEconomico.observaciones").toUpperCase()+":");
                    }
                    celda.setCellValue(valor);
                    
                    celda.setCellStyle(estiloCelda);

                    //COLUMNA: IMPORTE CONCEDIDO TRAS RESOLUCION MODIF
                    estiloCelda = libro.createCellStyle();
                    estiloCelda.setWrapText(true);
                    estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THIN);
                    if(nuevo)
                    {
                        estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
                    }
                    else if(filaI.getOrden() != null && filaI.getOrden() == 2)
                    {
                        estiloCelda.setBorderTop(HSSFCellStyle.BORDER_DOUBLE);
                    }
                    if(numFila == listaPuestos.size())
                    {
                        estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
                    }
                    estiloCelda.setBorderRight(HSSFCellStyle.BORDER_THICK);
                    estiloCelda.setFont(normal);
                    estiloCelda.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
                    celda = fila.createCell((short)29);
                    celda.setCellValue(filaI.getImpConcedidoTrasResol() != null ? MeLanbide59Utils.redondearDecimalesBigDecimal(filaI.getImpConcedidoTrasResol(), 2).doubleValue() : new BigDecimal(ConstantesMeLanbide59.CERO_CON_DECIMALES).doubleValue());
                    celda.setCellStyle(estiloCelda);
                    celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                    
                    if(!"TOTAL".equals(filaI.getCnae09()))
                        crearDatosHoja2Economico(libro, fila2, celda2, estilocelda2, idioma, filaI,numExp, numFila2, nuevo,n, listaPuestos, datosTercero,  normal, codOrganizacion, ano,  adapt, p);
                    
                }
                
                //Esto añade un merged region a la ultima fila, columnas de num expediente, entidad y territorio historico
                if(p != 0)
                {
                        //cambiar poi 3.10
                        /*hoja.addMergedRegion(new Region(numFila-p, (short)0, numFila, (short)0));
                    hoja.addMergedRegion(new Region(numFila-p, (short)1, numFila, (short)1));
                    hoja.addMergedRegion(new Region(numFila-p, (short)2, numFila, (short)2));
                    hoja.addMergedRegion(new Region(numFila-p, (short)3, numFila-1, (short)3));
                        **/
                        hoja.addMergedRegion(new CellRangeAddress(numFila-p,  numFila, 0, 0));
                        hoja.addMergedRegion(new CellRangeAddress(numFila-p,  numFila, 1, 1));
                        hoja.addMergedRegion(new CellRangeAddress(numFila-p,  numFila, 2, 2));
                        hoja.addMergedRegion(new CellRangeAddress(numFila-p,  numFila-1, 3, 3));
                    
                    estiloCelda = libro.createCellStyle();
                    estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                    estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                    estiloCelda.setWrapText(true);
                    estiloCelda.setFont(normal);
                    log.debug("paso11: numFila" + numFila);
                    log.debug("hoja.getRow(numFila-p): " + hoja.getRow(numFila-p));
                    log.debug("hoja.getRow(numFila-p).getCell((short)0): " + hoja.getRow(numFila-p).getCell((short)0));
                    
                    hoja.getRow(numFila-p).getCell((short)0).setCellStyle(estiloCelda);
                    log.debug("paso12");
                    estiloCelda = libro.createCellStyle();
                    estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                    estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THIN);
                    estiloCelda.setWrapText(true);
                    estiloCelda.setFont(normal);
                    
                    hoja.getRow(numFila-p).getCell((short)1).setCellStyle(estiloCelda);
                    
                    
                    estilocelda2 = libro.createCellStyle();
                    estilocelda2.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                    estilocelda2.setBorderLeft(HSSFCellStyle.BORDER_THICK);
                    estilocelda2.setWrapText(true);
                    estilocelda2.setFont(normal);
                    log.debug("paso2");
                    hoja.getRow(numFila-p).getCell((short)0).setCellStyle(estilocelda2);
                    
                    estilocelda2 = libro.createCellStyle();
                    estilocelda2.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
                    estilocelda2.setBorderLeft(HSSFCellStyle.BORDER_THIN);
                    estilocelda2.setWrapText(true);
                    estilocelda2.setFont(normal);
                    
                    hoja.getRow(numFila-p).getCell((short)1).setCellStyle(estiloCelda);
                    log.debug("paso3");
                    
                }
                
                //se escribe el fichero
                /*File directorioTemp = new File(m_Conf.getString("PDF.base_dir"));
                log.debug("directorioTemp" + directorioTemp.toString());
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
                log.error("Error en generarInformeResumenEconomico - i = "+ i +": " + ioe.toString());
                ioe.printStackTrace();
            }
        }
        catch(Exception ex)
        {
            log.error("Error en generarInformeResumenEconomico - i = "+ i +": " + ex.toString());
            ex.printStackTrace();
        }
    }
    
    private void crearDatosHoja2Economico(HSSFWorkbook libro, HSSFRow fila, HSSFCell celda, HSSFCellStyle estiloCelda, int idioma, FilaInformeResumenEconomicoVO filaI,
    String numExp, int numFila, boolean nuevo, int n, List<FilaInformeResumenEconomicoVO> listaPuestos, HashMap<String, String> datosTercero,
    HSSFFont normal, int codOrganizacion, int ano, AdaptadorSQLBD adapt, int p)
    {
        try
        {
            String empresa = null;
            String pais = null;
            
            MeLanbide59I18n meLanbide59I18n = MeLanbide59I18n.getInstance();
                
            //COLUMNA: NUM EXPEDIENTE
            estiloCelda = libro.createCellStyle();
//            estiloCelda.setWrapText(true);
//            estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THICK);
//            if(nuevo)
//            {
//                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
//            }
//            if(numFila == listaPuestos.size())
//            {
//                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
//            }
            //estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
            estiloCelda.setFont(normal);
            celda = fila.createCell((short)0);
                celda.setCellValue(numExp);
            celda.setCellStyle(estiloCelda);

            //COLUMNA: ENTIDAD
            if(filaI.getEntidad() != null)
            {
                empresa = filaI.getEntidad();
            }
            else
            {
                datosTercero = MeLanbide59Manager.getInstance().getDatosTercero(codOrganizacion, numExp, String.valueOf(ano), ConstantesMeLanbide59.CODIGO_ROL_ENTIDAD_SOLICITANTE, adapt);
                if(datosTercero.containsKey("TER_NOC"))
                {
                    empresa = datosTercero.get("TER_NOC");
                }
                else
                {
                    empresa = "";
                }
            }
//            log.debug("empresa");
            estiloCelda = libro.createCellStyle();
//            estiloCelda.setWrapText(true);
//            estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THIN);
//            if(nuevo)
//            {
//                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
//            }
//            if(numFila == listaPuestos.size())
//            {
//                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
//            }
            estiloCelda.setFont(normal);
            //estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
            celda = fila.createCell((short)1);

                celda.setCellValue(empresa != null ? empresa.toUpperCase() : "");

            celda.setCellStyle(estiloCelda);

            //COLUMNA: TERRITORIO HISTORICO
            estiloCelda = libro.createCellStyle();
//            estiloCelda.setWrapText(true);
//            estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THIN);
//            if(nuevo)
//            {
//                estiloCelda.setBorderTop(HSSFCellStyle.BORDER_THICK);
//            }
//            if(numFila == listaPuestos.size())
//            {
//                estiloCelda.setBorderBottom(HSSFCellStyle.BORDER_THICK);
//            }
            estiloCelda.setFont(normal);
            //estiloCelda.setAlignment(HSSFCellStyle.ALIGN_CENTER);
            estiloCelda.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
            celda = fila.createCell((short)2);

                celda.setCellValue(filaI.getTerritorioHistorico() != null ? filaI.getTerritorioHistorico().toUpperCase() : "");

            celda.setCellStyle(estiloCelda);
            log.debug("territorio historico");
            //COLUMNA: CNAE09
            estiloCelda = libro.createCellStyle();
//            estiloCelda.setWrapText(true);
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
            celda = fila.createCell((short)3);
            String valor = filaI.getCnae09() != null ? filaI.getCnae09().toUpperCase() : "";
            if(!valor.equals(""))
                valor = valor.replaceAll("TOTAL", meLanbide59I18n.getMensaje(idioma, "doc.informeEconomico.total").toUpperCase());
            if(valor.equals(meLanbide59I18n.getMensaje(idioma, "doc.informeEconomico.total").toUpperCase()) || nuevo)
            {
                celda.setCellValue(valor);
            }
            celda.setCellStyle(estiloCelda);

            //COLUMNA: Nº PUESTOS SOLIC
            estiloCelda = libro.createCellStyle();
//            estiloCelda.setWrapText(true);
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
            celda = fila.createCell((short)4);
            celda.setCellValue(filaI.getnPuestosSolicitados() != null ? filaI.getnPuestosSolicitados() : 0);
            celda.setCellStyle(estiloCelda);
            celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);

            //COLUMNA: PUESTO
            estiloCelda = libro.createCellStyle();
            
            estiloCelda.setFont(normal);
            celda = fila.createCell((short)5);
            celda.setCellValue(filaI.getPuesto() != null ? filaI.getCodPuesto()+"-"+filaI.getPuesto().toUpperCase() : "");
            celda.setCellStyle(estiloCelda);

            //COLUMNA: RESULTADO
            estiloCelda = libro.createCellStyle();
            
            estiloCelda.setFont(normal);
            celda = fila.createCell((short)6);
            celda.setCellValue(filaI.getResultado() != null ? filaI.getResultado() : "");
            celda.setCellStyle(estiloCelda);


            //COLUMNA: FECHA INI
            estiloCelda = libro.createCellStyle();
            estiloCelda.setWrapText(true);
            
            estiloCelda.setFont(normal);
            celda = fila.createCell((short)7);
            celda.setCellValue(filaI.getFecIni() != null ? filaI.getFecIni() : "");
            celda.setCellStyle(estiloCelda);


            //COLUMNA: FECHA FIN
            estiloCelda = libro.createCellStyle();
            estiloCelda.setWrapText(true);
            
            estiloCelda.setFont(normal);
            celda = fila.createCell((short)8);
            celda.setCellValue(filaI.getFecFin() != null ? filaI.getFecFin() : "");
            celda.setCellStyle(estiloCelda);


            //COLUMNA: PERIODO MESES SUBV
            estiloCelda = libro.createCellStyle();
            estiloCelda.setWrapText(true);
            estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THIN);
            
            estiloCelda.setFont(normal);
            estiloCelda.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
            celda = fila.createCell((short)9);
            celda.setCellValue(filaI.getPeriodoMesesSubv() != null ? filaI.getPeriodoMesesSubv() : 0);
            celda.setCellStyle(estiloCelda);
            celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
            //COLUMNA: TITULACION AB
            estiloCelda = libro.createCellStyle();
            estiloCelda.setWrapText(true);
            estiloCelda.setBorderLeft(HSSFCellStyle.BORDER_THIN);
            
            estiloCelda.setFont(normal);
            celda = fila.createCell((short)10);
            celda.setCellValue(filaI.getTitulacionAB() != null ? filaI.getTitulacionAB() : "");
            celda.setCellStyle(estiloCelda);

            //COLUMNA: PAIS
            estiloCelda = libro.createCellStyle();
            estiloCelda.setWrapText(true);
           
            estiloCelda.setFont(normal);
            celda = fila.createCell((short)11);

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
            celda = fila.createCell((short)12);
            celda.setCellValue(filaI.getCostesContSalarioSS() != null ? MeLanbide59Utils.redondearDecimalesBigDecimal(filaI.getCostesContSalarioSS(), 2).doubleValue() : new BigDecimal(ConstantesMeLanbide59.CERO_CON_DECIMALES).doubleValue());
            celda.setCellStyle(estiloCelda);
            celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);

            //COLUMNA: DIETAS CONVENIO
            estiloCelda = libro.createCellStyle();
            estiloCelda.setWrapText(true);
            
            estiloCelda.setFont(normal);
            estiloCelda.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
            celda = fila.createCell((short)13);
            celda.setCellValue(filaI.getCostesContDietasConve() != null ? MeLanbide59Utils.redondearDecimalesBigDecimal(filaI.getCostesContDietasConve(), 2).doubleValue() : new BigDecimal(ConstantesMeLanbide59.CERO_CON_DECIMALES).doubleValue());
            celda.setCellStyle(estiloCelda);
            celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);

            //COLUMNA: COSTES CONTRATACION
            estiloCelda = libro.createCellStyle();
            estiloCelda.setWrapText(true);
            
            estiloCelda.setFont(normal);
            estiloCelda.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
            celda = fila.createCell((short)14);
            celda.setCellValue(filaI.getCostesCont() != null ? MeLanbide59Utils.redondearDecimalesBigDecimal(filaI.getCostesCont(), 2).doubleValue() : new BigDecimal(ConstantesMeLanbide59.CERO_CON_DECIMALES).doubleValue());
            celda.setCellStyle(estiloCelda);
            celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);

            //COLUMNA: SALARIO ANUAL BRUTO
            estiloCelda = libro.createCellStyle();
            estiloCelda.setWrapText(true);
            
            estiloCelda.setFont(normal);
            estiloCelda.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
            celda = fila.createCell((short)15);
            celda.setCellValue(filaI.getSolSalarioSS() != null ? MeLanbide59Utils.redondearDecimalesBigDecimal(filaI.getSolSalarioSS(), 2).doubleValue() : new BigDecimal(ConstantesMeLanbide59.CERO_CON_DECIMALES).doubleValue());
            celda.setCellStyle(estiloCelda);
            celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);

            //COLUMNA: DIETAS ALOJAMIENTO Y MANUTENCION
            estiloCelda = libro.createCellStyle();
            estiloCelda.setWrapText(true);
            
            estiloCelda.setFont(normal);
            estiloCelda.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
            celda = fila.createCell((short)16);
            celda.setCellValue(filaI.getSolDietasConvSS() != null ? MeLanbide59Utils.redondearDecimalesBigDecimal(filaI.getSolDietasConvSS(), 2).doubleValue() : new BigDecimal(ConstantesMeLanbide59.CERO_CON_DECIMALES).doubleValue());
            celda.setCellStyle(estiloCelda);
            celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);

            //COLUMNA: TRAMITACION DE PERMISOS Y SEGUROS MEDICOS
            estiloCelda = libro.createCellStyle();
            estiloCelda.setWrapText(true);
            
            estiloCelda.setFont(normal);
            estiloCelda.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
            celda = fila.createCell((short)17);
            celda.setCellValue(filaI.getSolGastosTra() != null ? MeLanbide59Utils.redondearDecimalesBigDecimal(filaI.getSolGastosTra(), 2).doubleValue() : new BigDecimal(ConstantesMeLanbide59.CERO_CON_DECIMALES).doubleValue());
            celda.setCellStyle(estiloCelda);
            celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);

            //COLUMNA: IMPORTE SOLICITADO
            estiloCelda = libro.createCellStyle();
            estiloCelda.setWrapText(true);
            
            estiloCelda.setFont(normal);
            estiloCelda.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
            celda = fila.createCell((short)18);
            celda.setCellValue(filaI.getSolicitado() != null ? MeLanbide59Utils.redondearDecimalesBigDecimal(filaI.getSolicitado(), 2).doubleValue() : new BigDecimal(ConstantesMeLanbide59.CERO_CON_DECIMALES).doubleValue());
            celda.setCellStyle(estiloCelda);
            celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);

            //COLUMNA: DOTACION ANUAL MAXIMA
            estiloCelda = libro.createCellStyle();
            estiloCelda.setWrapText(true);
            
            estiloCelda.setFont(normal);
            estiloCelda.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
            celda = fila.createCell((short)19);
            celda.setCellValue(filaI.getDotacionAnualMaxima() != null ? MeLanbide59Utils.redondearDecimalesBigDecimal(filaI.getDotacionAnualMaxima(), 2).doubleValue() : new BigDecimal(ConstantesMeLanbide59.CERO_CON_DECIMALES).doubleValue());
            celda.setCellStyle(estiloCelda);
            celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);

            //COLUMNA: MESES SUBVENCIONABLES
            estiloCelda = libro.createCellStyle();
            estiloCelda.setWrapText(true);
            
            estiloCelda.setFont(normal);
            estiloCelda.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
            celda = fila.createCell((short)20);
            celda.setCellValue(filaI.getMesesSubvencionables() != null ? filaI.getMesesSubvencionables() : 0);
            celda.setCellStyle(estiloCelda);
            celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);

            //COLUMNA: DIETAS SUBVENCIONABLES
            estiloCelda = libro.createCellStyle();
            estiloCelda.setWrapText(true);
            
            estiloCelda.setFont(normal);
            estiloCelda.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
            celda = fila.createCell((short)21);
            celda.setCellValue(filaI.getDietasSubv() != null ? MeLanbide59Utils.redondearDecimalesBigDecimal(filaI.getDietasSubv(), 2).doubleValue() : new BigDecimal(ConstantesMeLanbide59.CERO_CON_DECIMALES).doubleValue());
            celda.setCellStyle(estiloCelda);
            celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);

            //COLUMNA: TRAMITACION DE PERMISOS Y SEGUROS MEDICOS MAX 450
            estiloCelda = libro.createCellStyle();
            estiloCelda.setWrapText(true);
            
            estiloCelda.setFont(normal);
            estiloCelda.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
            celda = fila.createCell((short)22);
            celda.setCellValue(filaI.getGastosTram() != null ? MeLanbide59Utils.redondearDecimalesBigDecimal(filaI.getGastosTram(), 2).doubleValue() : new BigDecimal(ConstantesMeLanbide59.CERO_CON_DECIMALES).doubleValue());
            celda.setCellStyle(estiloCelda);
            celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);

            //COLUMNA: MINIMIS CONCEDIDOS
            estiloCelda = libro.createCellStyle();
            estiloCelda.setWrapText(true);
            
            estiloCelda.setFont(normal);
            estiloCelda.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
            celda = fila.createCell((short)23);
            celda.setCellValue(filaI.getMinimisConcedidos() != null ? MeLanbide59Utils.redondearDecimalesBigDecimal(filaI.getMinimisConcedidos(), 2).doubleValue() : new BigDecimal(ConstantesMeLanbide59.CERO_CON_DECIMALES).doubleValue());
            celda.setCellStyle(estiloCelda);
            celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);

            //COLUMNA: IMPORTE MAX SUBVENCIONABLE
            estiloCelda = libro.createCellStyle();
            estiloCelda.setWrapText(true);
            
            estiloCelda.setFont(normal);
            estiloCelda.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
            celda = fila.createCell((short)24);
            celda.setCellValue(filaI.getImpMaxSubvencionable() != null ? MeLanbide59Utils.redondearDecimalesBigDecimal(filaI.getImpMaxSubvencionable(), 2).doubleValue() : new BigDecimal(ConstantesMeLanbide59.CERO_CON_DECIMALES).doubleValue());
            celda.setCellStyle(estiloCelda);
            celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);

            //COLUMNA: IMPORTE CONCEDIDO
            estiloCelda = libro.createCellStyle();
            estiloCelda.setWrapText(true);
            
            estiloCelda.setFont(normal);
            estiloCelda.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
            celda = fila.createCell((short)25);
            celda.setCellValue(filaI.getImporteConcedido() != null ? MeLanbide59Utils.redondearDecimalesBigDecimal(filaI.getImporteConcedido(), 2).doubleValue() : new BigDecimal(ConstantesMeLanbide59.CERO_CON_DECIMALES).doubleValue());
            celda.setCellStyle(estiloCelda);
            celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);

            //COLUMNA: NUM RENUNCIAS
            estiloCelda = libro.createCellStyle();
            estiloCelda.setWrapText(true);
            
            estiloCelda.setFont(normal);
            estiloCelda.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
            celda = fila.createCell((short)26);
            celda.setCellValue(filaI.getNumRenuncias() != null ? filaI.getNumRenuncias() : 0);
            celda.setCellStyle(estiloCelda);
            celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);

            //COLUMNA: IMPORTE RENUNCIA
            estiloCelda = libro.createCellStyle();
            estiloCelda.setWrapText(true);
            
            estiloCelda.setFont(normal);
            estiloCelda.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
            celda = fila.createCell((short)27);
            celda.setCellValue(filaI.getImporteRenuncia() != null ? MeLanbide59Utils.redondearDecimalesBigDecimal(filaI.getImporteRenuncia(), 2).doubleValue() : new BigDecimal(ConstantesMeLanbide59.CERO_CON_DECIMALES).doubleValue());
            celda.setCellStyle(estiloCelda);
            celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);

            //COLUMNA: MOTIVO RENUNCIA
            estiloCelda = libro.createCellStyle();
            estiloCelda.setWrapText(true);
            
            estiloCelda.setFont(normal);
            celda = fila.createCell((short)28);

            valor = filaI.getMotivo() != null ? filaI.getMotivo().toUpperCase() : "";
            if(valor != null && !valor.equals(""))
            {
                valor = valor.replaceAll("-OBSERVACIONES:", " - "+meLanbide59I18n.getMensaje(idioma, "doc.informeEconomico.observaciones").toUpperCase()+":");
            }
            celda.setCellValue(valor);

            celda.setCellStyle(estiloCelda);

            //COLUMNA: IMPORTE CONCEDIDO TRAS RESOLUCION MODIF
            estiloCelda = libro.createCellStyle();
            estiloCelda.setWrapText(true);
            
            //estiloCelda.setBorderRight(HSSFCellStyle.BORDER_THICK);
            estiloCelda.setFont(normal);
            estiloCelda.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
            celda = fila.createCell((short)29);
            celda.setCellValue(filaI.getImpConcedidoTrasResol() != null ? MeLanbide59Utils.redondearDecimalesBigDecimal(filaI.getImpConcedidoTrasResol(), 2).doubleValue() : new BigDecimal(ConstantesMeLanbide59.CERO_CON_DECIMALES).doubleValue());
            celda.setCellStyle(estiloCelda);
            celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
        }catch(Exception ex){
            
        }
        
    }
    private void crearEstiloHoja2Economico(HSSFWorkbook libro, HSSFRow fila, HSSFCell celda, HSSFCellStyle estiloCelda, int idioma)
    {
        try
        {
            MeLanbide59I18n meLanbide59I18n = MeLanbide59I18n.getInstance();
            
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
                celda = fila.createCell((short)0);
                celda.setCellValue(meLanbide59I18n.getMensaje(idioma, "doc.informeEconomico.col1").toUpperCase());
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
                celda = fila.createCell((short)1);
                celda.setCellValue(meLanbide59I18n.getMensaje(idioma, "doc.informeEconomico.col2").toUpperCase());
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
                celda = fila.createCell((short)2);
                celda.setCellValue(meLanbide59I18n.getMensaje(idioma, "doc.informeEconomico.col30").toUpperCase());
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
                celda = fila.createCell((short)3);
                celda.setCellValue(meLanbide59I18n.getMensaje(idioma, "doc.informeEconomico.col3").toUpperCase());
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
                celda = fila.createCell((short)4);
                celda.setCellValue(meLanbide59I18n.getMensaje(idioma, "doc.informeEconomico.col4").toUpperCase());
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
                celda = fila.createCell((short)5);
                celda.setCellValue(meLanbide59I18n.getMensaje(idioma, "doc.informeEconomico.col5").toUpperCase());
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
                celda = fila.createCell((short)6);
                celda.setCellValue(meLanbide59I18n.getMensaje(idioma, "doc.informeEconomico.col6").toUpperCase());
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
                celda = fila.createCell((short)7);
                celda.setCellValue(meLanbide59I18n.getMensaje(idioma, "doc.informeEconomico.col7").toUpperCase());
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
                celda = fila.createCell((short)8);
                celda.setCellValue(meLanbide59I18n.getMensaje(idioma, "doc.informeEconomico.col8").toUpperCase());
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
                celda = fila.createCell((short)9);
                celda.setCellValue(meLanbide59I18n.getMensaje(idioma, "doc.informeEconomico.col9").toUpperCase());
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
                celda = fila.createCell((short)10);
                celda.setCellValue(meLanbide59I18n.getMensaje(idioma, "doc.informeEconomico.col10").toUpperCase());
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
                celda = fila.createCell((short)11);
                celda.setCellValue(meLanbide59I18n.getMensaje(idioma, "doc.informeEconomico.col11").toUpperCase());
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
                celda = fila.createCell((short)12);
                celda.setCellValue(meLanbide59I18n.getMensaje(idioma, "doc.informeEconomico.col12").toUpperCase());
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
                celda = fila.createCell((short)13);
                celda.setCellValue(meLanbide59I18n.getMensaje(idioma, "doc.informeEconomico.col13").toUpperCase());
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
                celda = fila.createCell((short)14);
                celda.setCellValue(meLanbide59I18n.getMensaje(idioma, "doc.informeEconomico.col14").toUpperCase());
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
                celda = fila.createCell((short)15);
                celda.setCellValue(meLanbide59I18n.getMensaje(idioma, "doc.informeEconomico.col15").toUpperCase());
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
                celda = fila.createCell((short)16);
                celda.setCellValue(meLanbide59I18n.getMensaje(idioma, "doc.informeEconomico.col16").toUpperCase());
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
                celda = fila.createCell((short)17);
                celda.setCellValue(meLanbide59I18n.getMensaje(idioma, "doc.informeEconomico.col17").toUpperCase());
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
                celda = fila.createCell((short)18);
                celda.setCellValue(meLanbide59I18n.getMensaje(idioma, "doc.informeEconomico.col18").toUpperCase());
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
                celda = fila.createCell((short)19);
                celda.setCellValue(meLanbide59I18n.getMensaje(idioma, "doc.informeEconomico.col19").toUpperCase());
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
                celda = fila.createCell((short)20);
                celda.setCellValue(meLanbide59I18n.getMensaje(idioma, "doc.informeEconomico.col20").toUpperCase());
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
                celda = fila.createCell((short)21);
                celda.setCellValue(meLanbide59I18n.getMensaje(idioma, "doc.informeEconomico.col21").toUpperCase());
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
                celda = fila.createCell((short)22);
                celda.setCellValue(meLanbide59I18n.getMensaje(idioma, "doc.informeEconomico.col22").toUpperCase());
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
                celda = fila.createCell((short)23);
                celda.setCellValue(meLanbide59I18n.getMensaje(idioma, "doc.informeEconomico.col23").toUpperCase());
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
                celda = fila.createCell((short)24);
                celda.setCellValue(meLanbide59I18n.getMensaje(idioma, "doc.informeEconomico.col24").toUpperCase());
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
                celda = fila.createCell((short)25);
                celda.setCellValue(meLanbide59I18n.getMensaje(idioma, "doc.informeEconomico.col25").toUpperCase());
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
                celda = fila.createCell((short)26);
                celda.setCellValue(meLanbide59I18n.getMensaje(idioma, "doc.informeEconomico.col26").toUpperCase());
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
                celda = fila.createCell((short)27);
                celda.setCellValue(meLanbide59I18n.getMensaje(idioma, "doc.informeEconomico.col27").toUpperCase());
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
                celda = fila.createCell((short)28);
                celda.setCellValue(meLanbide59I18n.getMensaje(idioma, "doc.informeEconomico.col28").toUpperCase());
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
                celda = fila.createCell((short)29);
                celda.setCellValue(meLanbide59I18n.getMensaje(idioma, "doc.informeEconomico.col29").toUpperCase());
                celda.setCellStyle(estiloCelda);
            }
            catch (Exception e) 
            {
                e.printStackTrace();
            }

            
        }catch(Exception ex){
            
        }
        
    }
    
    private String cargarSubpestanaSolicitud_DatosCpe(String numExpediente, AdaptadorSQLBD adapt, HttpServletRequest request)
    {
        try
        {
            List<FilaPuestoVO> puestos = MeLanbide59Manager.getInstance().getListaPuestosPorExpediente(numExpediente, adapt);
            request.setAttribute("puestos", puestos);
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return "/jsp/extension/melanbide59/solicitud.jsp";
    }
    
    private String cargarSubpestanaOferta_DatosCpe(String numExpediente, AdaptadorSQLBD adapt, HttpServletRequest request)
    {
        try
        {
            List<FilaOfertaVO> ofertas = MeLanbide59Manager.getInstance().getListaOfertasNoDenegadasPorExpediente(numExpediente, adapt);
            request.setAttribute("ofertas", ofertas);
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return "/jsp/extension/melanbide59/oferta.jsp";
    }
    
    private String cargarSubpestanaJustificacion_DatosCpe(String numExpediente, AdaptadorSQLBD adapt, HttpServletRequest request)
    {
        try
        {
            List<FilaJustificacionVO> justificaciones = MeLanbide59Manager.getInstance().getListaJustificacionesNoDenegadasPorExpediente(numExpediente, adapt);
            request.setAttribute("justificaciones", justificaciones);
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return "/jsp/extension/melanbide59/justificacion.jsp";
    }
    
    private String cargarSubpestanaResumen_DatosCpe(String numExpediente, AdaptadorSQLBD adapt, HttpServletRequest request)
    {
        try
        {
            List<FilaResumenVO> filasResumen = MeLanbide59Manager.getInstance().getListaResumenPorExpediente(numExpediente, adapt);
            log.error("antes de establecer el atributo resumen del request");
            request.setAttribute("resumen", filasResumen);
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
            log.error("Error en cargarSubpestanaResumen_DatosCpe: " + ex);
        }
        return "/jsp/extension/melanbide59/resumen.jsp";
    }
    
    private String cargarSubpestanaSolicitudHist_DatosCpe(int codOrganizacion, String numExpediente, String codTramite, AdaptadorSQLBD adapt, HttpServletRequest request)
    {
        try
        {
            List<FilaPuestoVO> puestos = MeLanbide59Manager.getInstance().getListaPuestosHistPorExpediente(codOrganizacion, numExpediente, codTramite, adapt);
            request.setAttribute("puestos", puestos);
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return "/jsp/extension/melanbide59/solicitudHist.jsp";
    }
    
    private String cargarSubpestanaOfertaHist_DatosCpe(int codOrganizacion, String numExpediente, String codTramite, AdaptadorSQLBD adapt, HttpServletRequest request)
    {
        try
        {
            List<FilaOfertaVO> ofertas = MeLanbide59Manager.getInstance().getListaOfertasHistNoDenegadasPorExpediente(codOrganizacion, numExpediente, codTramite, adapt);
            request.setAttribute("ofertas", ofertas);
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return "/jsp/extension/melanbide59/ofertaHist.jsp";
    }
    
    private String cargarSubpestanaJustificacionHist_DatosCpe(int codOrganizacion, String numExpediente, String codTramite, AdaptadorSQLBD adapt, HttpServletRequest request)
    {
        try
        {
            List<FilaJustificacionVO> justificaciones = MeLanbide59Manager.getInstance().getListaJustificacionesHistNoDenegadasPorExpediente(codOrganizacion, numExpediente, codTramite, adapt);
            request.setAttribute("justificaciones", justificaciones);
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        return "/jsp/extension/melanbide59/justificacionHist.jsp";
    }
    
    private void cargarDatosCpe(int codOrganizacion, String numExpediente, HttpServletRequest request)
    {
        IModuloIntegracionExternoCamposFlexia el = ModuloIntegracionExternoCampoSupFactoria.getInstance().getImplClass();
        
        List<ValorCampoDesplegableModuloIntegracionVO> list = new ArrayList<ValorCampoDesplegableModuloIntegracionVO>();
        List<SelectItem> listaGestores = new ArrayList<SelectItem>();
        
        //Combo GESTOR TRAMITADOR
        try
        {
            String campoDesplegable = ConfigurationParameter.getParameter(ConstantesMeLanbide59.CAMPO_SUPL_GESTOR_TRAMITADOR, ConstantesMeLanbide59.FICHERO_PROPIEDADES);
        
            SalidaIntegracionVO salidaIntegracion = el.getCampoDesplegable(String.valueOf(codOrganizacion), campoDesplegable);
            list = salidaIntegracion.getCampoDesplegable().getValores();
            listaGestores = MeLanbide59MappingUtils.getInstance().fromCampoDesplegableToSelectItemVO(list);
        }
        catch(Exception ex)
        {
            
        }
        request.setAttribute("listaGestores", listaGestores);
        
        
        
        Integer ejercicio = null;
        try
        {
            ejercicio = MeLanbide59Utils.getEjercicioDeExpediente(numExpediente);
        }
        catch(Exception ex)
        {
            
        }
        
        if(ejercicio != null)
        {
            AdaptadorSQLBD adaptador = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
            //Cargo el gestor tramitador, empresa e importes de la subvencion (camposSuplementarios)
            try
            {
                String gestor = MeLanbide59Manager.getInstance().getValorCampoDesplegable(codOrganizacion, String.valueOf(ejercicio), numExpediente, ConfigurationParameter.getParameter(ConstantesMeLanbide59.CAMPO_SUPL_GESTOR_TRAMITADOR, ConstantesMeLanbide59.FICHERO_PROPIEDADES), adaptador);
                if(gestor != null && !gestor.equals(""))
                {
                    request.setAttribute("gestor", gestor);
                }
            }
            catch(Exception ex)
            {
                ex.printStackTrace();
            }
            
            try
            {
                String empresa = MeLanbide59Manager.getInstance().getValorCampoTexto(codOrganizacion, String.valueOf(ejercicio), numExpediente, ConfigurationParameter.getParameter(ConstantesMeLanbide59.CAMPO_SUPL_EMPRESA, ConstantesMeLanbide59.FICHERO_PROPIEDADES), adaptador);
                if(empresa != null && !empresa.equals(""))
                {
                    request.setAttribute("empresa", empresa);
                }
                else
                {
                    //Si no ha guardado en campo suplementario, precargamos con el tercero del expediente
                    HashMap<String, String> datosTercero = MeLanbide59Manager.getInstance().getDatosTercero(codOrganizacion, numExpediente, String.valueOf(ejercicio), ConstantesMeLanbide59.CODIGO_ROL_ENTIDAD_SOLICITANTE, adaptador);
                    if(datosTercero != null)
                    {
                        if(datosTercero.containsKey("TER_NOC"))
                        {
                            request.setAttribute("empresa", datosTercero.get("TER_NOC").toUpperCase());
                        }
                    }
                }
            }
            catch(Exception ex)
            {
                ex.printStackTrace();
            }
            
            //Los calculos los realizo cada vez que se cargan. Cuando se guarde se guardarÃ¡ en campos supl.
            
            Map<String, BigDecimal> calculos = null;
            
            try
            {
                calculos = MeLanbide59Manager.getInstance().cargarCalculosCpe(codOrganizacion, ejercicio, numExpediente, adaptador);
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
                    impSolicitado = calculos.get(ConstantesMeLanbide59.CAMPO_SUPL_IMP_SOLICITADO);
                    if(impSolicitado != null)
                    {
                        request.setAttribute("solicitado", impSolicitado.toPlainString());
                    }
                    else
                    {
                        request.setAttribute("solicitado", new BigDecimal(ConstantesMeLanbide59.CERO_CON_DECIMALES).toPlainString());
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
                    impConv = calculos.get(ConstantesMeLanbide59.CAMPO_SUPL_IMP_CONVOCATORIA);
                    if(impConv != null)
                    {
                        request.setAttribute("convocatoria", impConv.toPlainString());
                    }
                    else
                    {
                        request.setAttribute("convocatoria", new BigDecimal(ConstantesMeLanbide59.CERO_CON_DECIMALES).toPlainString());
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
                    impPrevCon = calculos.get(ConstantesMeLanbide59.CAMPO_SUPL_IMP_PREV_CON);
                    if(impPrevCon != null)
                    {
                        request.setAttribute("previsto", impPrevCon.toPlainString());
                    }
                    else
                    {
                        request.setAttribute("previsto", new BigDecimal(ConstantesMeLanbide59.CERO_CON_DECIMALES).toPlainString());
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
                    concedido = calculos.get(ConstantesMeLanbide59.CAMPO_SUPL_IMP_CON);
                    if(concedido != null)
                    {
                        request.setAttribute("concedido", concedido.toPlainString());
                    }
                    else
                    {
                        request.setAttribute("concedido", new BigDecimal(ConstantesMeLanbide59.CERO_CON_DECIMALES).toPlainString());
                    }
                }
                catch(Exception ex)
                {
                    ex.printStackTrace();
                }
                
                //importe justificado
                BigDecimal justificado = new BigDecimal(0);
                BigDecimal justif = null;
                try
                {
                    String coste="";
                    List<FilaResumenVO> filasResumen = MeLanbide59Manager.getInstance().getListaResumenPorExpediente(numExpediente, adaptador);
                    for(FilaResumenVO fila : filasResumen)
                    {
                        coste = fila.getCosteSubvecionable();
                        coste = coste.replace(",", ".");
                        if(!coste.equals("")){
                            justif = new BigDecimal(coste);
                        }
                        justificado=justificado.add(justif);
                    }
                    //justificado = calculos.get(ConstantesMeLanbide59.CAMPO_SUPL_IMP_JUS);
                    if(justificado != null)
                    {
                        request.setAttribute("justificado", justificado.toPlainString());
                    }
                    else
                    {
                        request.setAttribute("justificado", new BigDecimal(ConstantesMeLanbide59.CERO_CON_DECIMALES).toPlainString());
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
                    noJustificado = calculos.get(ConstantesMeLanbide59.CAMPO_SUPL_IMP_NO_JUS);
                    if(noJustificado != null)
                    {
                        request.setAttribute("noJustificado", noJustificado.toPlainString());
                    }
                    else
                    {
                        request.setAttribute("noJustificado", new BigDecimal(ConstantesMeLanbide59.CERO_CON_DECIMALES).toPlainString());
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
                    renunciado = calculos.get(ConstantesMeLanbide59.CAMPO_SUPL_IMP_REN);
                    if(renunciado != null)
                    {
                        request.setAttribute("renunciado", renunciado.toPlainString());
                    }
                    else
                    {
                        request.setAttribute("renunciado", new BigDecimal(ConstantesMeLanbide59.CERO_CON_DECIMALES).toPlainString());
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
                    renunciadoRes = calculos.get(ConstantesMeLanbide59.CAMPO_SUPL_IMP_REN_RES);
                    if(renunciadoRes != null)
                    {
                        request.setAttribute("renunciadoRes", renunciadoRes.toPlainString());
                    }
                    else
                    {
                        request.setAttribute("renunciadoRes", new BigDecimal(ConstantesMeLanbide59.CERO_CON_DECIMALES).toPlainString());
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
                    impPag = calculos.get(ConstantesMeLanbide59.CAMPO_SUPL_IMP_PAG);
                    if(impPag != null)
                    {
                        request.setAttribute("pagado", impPag.toPlainString());
                    }
                    else
                    {
                        request.setAttribute("pagado", new BigDecimal(ConstantesMeLanbide59.CERO_CON_DECIMALES).toPlainString());
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
                    impPagRes = calculos.get(ConstantesMeLanbide59.CAMPO_SUPL_IMP_PAG_RES);
                    if(impPagRes != null)
                    {
                        request.setAttribute("pagadoRes", impPagRes.toPlainString());
                    }
                    else
                    {
                        request.setAttribute("pagadoRes", new BigDecimal(ConstantesMeLanbide59.CERO_CON_DECIMALES).toPlainString());
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
                    impPag2 = calculos.get(ConstantesMeLanbide59.CAMPO_SUPL_IMP_PAG_2);
                    if(impPag2 != null)
                    {
                        request.setAttribute("pagado2", impPag2.toPlainString());
                    }
                    else
                    {
                        request.setAttribute("pagado2", new BigDecimal(ConstantesMeLanbide59.CERO_CON_DECIMALES).toPlainString());
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
                    reint = calculos.get(ConstantesMeLanbide59.CAMPO_SUPL_IMP_REI);
                    if(reint != null)
                    {
                        request.setAttribute("reintegrar", reint.toPlainString());
                    }
                    else
                    {
                        request.setAttribute("reintegrar", new BigDecimal(ConstantesMeLanbide59.CERO_CON_DECIMALES).toPlainString());
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
                    oayuS = calculos.get(ConstantesMeLanbide59.CAMPO_SUPL_OTRAS_AYUDAS_SOLIC);
                    if(oayuS != null)
                    {
                        request.setAttribute("otrasAyudasSolic", oayuS.toPlainString());
                    }
                    else
                    {
                        request.setAttribute("otrasAyudasSolic", new BigDecimal(ConstantesMeLanbide59.CERO_CON_DECIMALES).toPlainString());
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
                    oayuC = calculos.get(ConstantesMeLanbide59.CAMPO_SUPL_OTRAS_AYUDAS_CONCE);
                    if(oayuC != null)
                    {
                        request.setAttribute("otrasAyudasConce", oayuC.toPlainString());
                    }
                    else
                    {
                        request.setAttribute("otrasAyudasConce", new BigDecimal(ConstantesMeLanbide59.CERO_CON_DECIMALES).toPlainString());
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
                    minS = calculos.get(ConstantesMeLanbide59.CAMPO_SUPL_MINIMIS_SOLIC);
                    if(minS != null)
                    {
                        request.setAttribute("minimisSolic", minS.toPlainString());
                    }
                    else
                    {
                        request.setAttribute("minimisSolic", new BigDecimal(ConstantesMeLanbide59.CERO_CON_DECIMALES).toPlainString());
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
                    minC = calculos.get(ConstantesMeLanbide59.CAMPO_SUPL_MINIMIS_CONCE);
                    if(minC != null)
                    {
                        request.setAttribute("minimisConce", minC.toPlainString());
                    }
                    else
                    {
                        request.setAttribute("minimisConce", new BigDecimal(ConstantesMeLanbide59.CERO_CON_DECIMALES).toPlainString());
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
                    impDesp = calculos.get(ConstantesMeLanbide59.CAMPO_SUPL_IMP_DESP);
                    if(impDesp != null)
                    {
                        request.setAttribute("importeDespido", impDesp.toPlainString());
                    }
                    else
                    {
                        request.setAttribute("importeDespido", new BigDecimal(ConstantesMeLanbide59.CERO_CON_DECIMALES).toPlainString());
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
                    impBaj = calculos.get(ConstantesMeLanbide59.CAMPO_SUPL_IMP_BAJA);
                    if(impBaj != null)
                    {
                        request.setAttribute("importeBajas", impBaj.toPlainString());
                    }
                    else
                    {
                        request.setAttribute("importeBajas", new BigDecimal(ConstantesMeLanbide59.CERO_CON_DECIMALES).toPlainString());
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
                        request.setAttribute("concedidoReal", new BigDecimal(ConstantesMeLanbide59.CERO_CON_DECIMALES).toPlainString());
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
                        request.setAttribute("pagadoReal", new BigDecimal(ConstantesMeLanbide59.CERO_CON_DECIMALES).toPlainString());
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
                        request.setAttribute("pagadoReal2", new BigDecimal(ConstantesMeLanbide59.CERO_CON_DECIMALES).toPlainString());
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
            Long codTramRes = MeLanbide59Manager.getInstance().obtenerCodigoInternoTramite(codOrganizacion, ConstantesMeLanbide59.CODIGO_TRAMITE_RESOLUCION, adapt);
            if(codTramRes != null)
            {
                if(MeLanbide59Manager.getInstance().tieneTramiteFinalizado(codOrganizacion, ejercicio, numExpediente, codTramRes, this.getAdaptSQLBD(String.valueOf(codOrganizacion))))
                {
                    request.setAttribute("tramRes", true);
                    tramEncontrado = true;
                }
            }
            
            if(tramEncontrado)
            {
                //Miro a ver si tiene iniciado el tramite de "Resolucion de concesion o denegacion"
                Long codTramResModif = MeLanbide59Manager.getInstance().obtenerCodigoInternoTramite(codOrganizacion, ConstantesMeLanbide59.CODIGO_TRAMITE_RESOLUCION_MODIF, adapt);
                if(codTramResModif != null)
                {
                    if(MeLanbide59Manager.getInstance().tieneTramiteFinalizado(codOrganizacion, ejercicio, numExpediente, codTramResModif, this.getAdaptSQLBD(String.valueOf(codOrganizacion))))
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
    
    public String guardarOferta(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response)
    {
        String codigoOperacion = "0";
        
        AdaptadorSQLBD adaptador = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
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
            
            Integer ejercicio = MeLanbide59Utils.getEjercicioDeExpediente(numExpediente);
            if(ejercicio != null)
            {
                SimpleDateFormat format = new SimpleDateFormat(ConstantesMeLanbide59.FORMATO_FECHA);
                
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
                

                CpeOfertaVO oferta = null;
                if(codOferta != null && !codOferta.equals("") && codPuesto != null && !codPuesto.equals(""))
                {
                    oferta = new CpeOfertaVO();
                    oferta.setIdOferta(Integer.parseInt(codOferta));
                    oferta.setNumExp(numExpediente);
                    oferta.setExpEje(ejercicio);
                    oferta.setCodPuesto(codPuesto);
                    oferta = MeLanbide59Manager.getInstance().getOfertaPorCodigoPuestoYExpediente(oferta, adaptador);
                }
                else
                {
                    oferta = new CpeOfertaVO();
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
                    if(difu != null && difu.equalsIgnoreCase(ConstantesMeLanbide59.CIERTO))
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
                    if(contratacion != null && contratacion.equalsIgnoreCase(ConstantesMeLanbide59.FALSO))
                    {
                        oferta.setMotContratacion(motivo != null && !motivo.equals("") ? motivo.toUpperCase() : null);
                        oferta.setCodCausaRenuncia(codCausaRenuncia);
                    }
                    else
                    {
                        oferta.setMotContratacion(null);
                        oferta.setCodCausaRenuncia(null);
                    }
                    
                    if(contratacionPresOferta != null && contratacionPresOferta.equalsIgnoreCase(ConstantesMeLanbide59.FALSO))
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
                    if(prec != null && prec.equalsIgnoreCase(ConstantesMeLanbide59.CIERTO))
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
                    
                    
                    MeLanbide59Manager.getInstance().guardarCpeOfertaVO(codOrganizacion, oferta, alta, copiar, adaptador);
                    
                    //#241414
                    //al modificar las fechas de contrato se debe recalcular el núm días de justificación (si hay fila de justificación)
                    if ( oferta.getNif()!=null && !oferta.getNif().isEmpty() && oferta.getFecIni()!=null && oferta.getFecFin()!=null){
                        CpeJustificacionVO justifModif = new CpeJustificacionVO();
                        justifModif.setCodPuesto(oferta.getCodPuesto());
                        justifModif.setEjercicio(ejercicio);
                        //justifModif.setIdJustificacion(Integer.parseInt(idJustif));                       
                        justifModif.setIdOferta(oferta.getIdOferta());
                        justifModif.setNumExpediente(numExpediente);
                        justifModif = MeLanbide59Manager.getInstance().getJustificacionPorCodigoPuestoOfertaYExpediente(justifModif, adaptador);                    
                        int dias=0;
                        //String rdo = MeLanbide59Manager.getInstance().obtenerDiasTrabajados(numExpediente, oferta.getNif(), adaptador);
                        String rdo = MeLanbide59Manager.getInstance().obtenerDiasTrabajados(numExpediente, oferta.getCodPuesto(),oferta.getIdOferta(), adaptador);
                        if(rdo != null && !rdo.equals(""))
                            dias = Integer.parseInt(rdo);
                        else
                        dias = 0;
                        justifModif.setDiasTrab(dias);
                        justifModif.setDiasSegSoc(dias);
                        //guardar dias justificacion
                        MeLanbide59Manager.getInstance().guardarCpeJustificacionVO(codOrganizacion,justifModif, adaptador) ;
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
        request.getSession().setAttribute("codOperacionGuardarOfertaCpe", codigoOperacion);
        return "/jsp/extension/melanbide59/recargarListaOfertas.jsp?numExp="+numExpediente;
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
                Integer ejercicio = MeLanbide59Utils.getEjercicioDeExpediente(numExpediente);
                CpeOfertaVO oferta = new CpeOfertaVO();
                oferta.setNumExp(numExpediente);
                oferta.setExpEje(ejercicio);
                oferta.setIdOferta(id);
                oferta.setCodPuesto(codPuesto);
                AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
                oferta = MeLanbide59Manager.getInstance().getOfertaPorCodigoPuestoYExpediente(oferta, adapt);
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
        
        AdaptadorSQLBD adaptador = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
        try
        {
            
            Integer ejercicio = MeLanbide59Utils.getEjercicioDeExpediente(numExpediente);
            if(ejercicio != null)
            {
                
                String codOferta = (String)request.getParameter("idOferta");
                String codPuesto = (String)request.getParameter("idPuesto");

                CpeOfertaVO oferta = null;
                if(codOferta != null && !codOferta.equals(""))
                {
                    oferta = new CpeOfertaVO();
                    oferta.setIdOferta(Integer.parseInt(codOferta));
                    oferta.setNumExp(numExpediente);
                    oferta.setExpEje(ejercicio);
                    oferta.setCodPuesto(codPuesto);
                    oferta = MeLanbide59Manager.getInstance().getOfertaPorCodigoPuestoYExpediente(oferta, adaptador);
                }
                if(oferta == null)
                {
                    codigoOperacion = "3";
                }
                else
                {
                    MeLanbide59Manager.getInstance().eliminarCpeOfertaVO(codOrganizacion, oferta, adaptador);
                }
                ofertas = MeLanbide59Manager.getInstance().getListaOfertasNoDenegadasPorExpediente(numExpediente, adaptador);
                calculos = MeLanbide59Manager.getInstance().cargarCalculosCpe(codOrganizacion, ejercicio, numExpediente, adaptador);
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
        escribirListaOfertasRequest(codigoOperacion, ofertas, calculos, response);
    }
    
    public void altaSustituto(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response)
    {
        String codigoOperacion = "0";
        List<FilaOfertaVO> ofertas = new ArrayList<FilaOfertaVO>();
        Map<String, BigDecimal> calculos = new HashMap<String, BigDecimal>();
        try
        {
            Integer ejercicio = null;
            Integer idOfertaOrigen = null;
            String copiar = null;
            String idPuesto = null;
            try
            {
                ejercicio = MeLanbide59Utils.getEjercicioDeExpediente(numExpediente);
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
                CpeOfertaVO ofertaOrigen = new CpeOfertaVO();
                ofertaOrigen.setIdOferta(idOfertaOrigen);
                ofertaOrigen.setExpEje(ejercicio);
                ofertaOrigen.setNumExp(numExpediente);
                ofertaOrigen.setCodPuesto(idPuesto);
                ofertaOrigen = MeLanbide59Manager.getInstance().getOfertaPorCodigoPuestoYExpediente(ofertaOrigen, adapt);
                if(ofertaOrigen != null)
                {
                    CpeOfertaVO ofertaNueva = new CpeOfertaVO();
                    ofertaNueva.setExpEje(ejercicio);
                    ofertaNueva.setNumExp(numExpediente);  
                    ofertaNueva.setCodPuesto(ofertaOrigen.getCodPuesto());
                    if(copiar != null && copiar.equalsIgnoreCase("1"))
                    {
                        ofertaNueva.setIdOfertaOrigen(idOfertaOrigen);
                    }
                    MeLanbide59Manager.getInstance().guardarCpeOfertaVO(codOrganizacion, ofertaNueva, "1", copiar, adapt);
                    ofertas = MeLanbide59Manager.getInstance().getListaOfertasNoDenegadasPorExpediente(numExpediente, adapt);
                    calculos = MeLanbide59Manager.getInstance().cargarCalculosCpe(codOrganizacion, ejercicio, numExpediente, adapt);
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
        escribirListaOfertasRequest(codigoOperacion, ofertas, calculos, response);
    }
    
    public void guardarDatosContratacion(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response)
    {
        String codigoOperacion = "0";
        List<FilaPersonaContratadaVO> personasContratadas = new ArrayList<FilaPersonaContratadaVO>();
        Map<String, BigDecimal> calculos = new HashMap<String, BigDecimal>();
        
        AdaptadorSQLBD adaptador = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
        try
        {
            
            Integer ejercicio = MeLanbide59Utils.getEjercicioDeExpediente(numExpediente);
            if(ejercicio != null)
            {
                SimpleDateFormat format = new SimpleDateFormat(ConstantesMeLanbide59.FORMATO_FECHA);
                String idJustif = (String)request.getParameter("idJustif");
                String codPuesto = (String)request.getParameter("idPuesto");
                String radioVariosTrab = (String)request.getParameter("radioVariosTrab");
                String impJustificado = (String)request.getParameter("impJustificado");
                String estado = (String)request.getParameter("estado");
                String salarioSub = (String)request.getParameter("salarioSub");
//                String dietasJusti = (String)request.getParameter("dietasJusti");
//                String gastosGestion = (String)request.getParameter("gastosGestion");
                String gastosVisado = (String)request.getParameter("gastosVisado");
                String gastosSeguro = (String)request.getParameter("gastosSeguro");
                String bonif = (String)request.getParameter("bonif");
                String mesesExt = (String)request.getParameter("mesesExt");

                String minoracion = (String)request.getParameter("minoracion");
                String salario = (String)request.getParameter("salario");
                String baseCC = (String)request.getParameter("baseCC");
                String baseAT = (String)request.getParameter("baseAT");
                String coeficienteApli = (String)request.getParameter("coeficienteApli");
                String porcFogasa = (String)request.getParameter("porcFogasa");
                String porcCoeficiente = (String)request.getParameter("porcCoeficiente");
                String porcAportacion = (String)request.getParameter("porcAportacion");
                String diasTrab = (String)request.getParameter("diasTrab");
                String diasSeg = (String)request.getParameter("diasSegSoc");
                String aportEpsv = (String)request.getParameter("aportacionEPSV");
                
                BigDecimal justificado = null;

                CpeJustificacionVO justif = null;
                if(idJustif != null && !idJustif.equals("") && codPuesto != null && !codPuesto.equals(""))
                {
                    justif = new CpeJustificacionVO();
                    justif.setIdJustificacion(Integer.parseInt(idJustif));
                    justif.setCodPuesto(codPuesto);
                    justif.setNumExpediente(numExpediente);
                    justif.setEjercicio(ejercicio);
                    justif = MeLanbide59Manager.getInstance().getJustificacionPorCodigoPuestoYExpediente(justif, adaptador);
                }
                else
                {
                    justif = new CpeJustificacionVO();
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
                        if(gastosVisado != null && !gastosVisado.equals(""))
                        {
                            bd2 = new BigDecimal(gastosVisado.replaceAll(",", "\\."));
                        }
                        if(gastosSeguro != null && !gastosSeguro.equals(""))
                        {
                            bd3 = new BigDecimal(gastosSeguro.replaceAll(",", "\\."));
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
                        log.error(ex.getMessage());
                        codigoOperacion = "3";
                    }
                    justif.setImpJustificado(bd);
                    justif.setSalarioSub(bd1);
                    justif.setGastosVisado(bd2);
                    justif.setMesesExt(bd12);
                    justif.setGastosSeguro(bd3);
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
                    
                    MeLanbide59Manager.getInstance().guardarCpeJustificacionVO(codOrganizacion, justif, adaptador);
                    
                    //actualizamos el importe justificado de los datos suplementarios
                    justificado = new BigDecimal(0);
                    BigDecimal jus = null;
                    try
                    {
                        String coste="";
                        List<FilaResumenVO> filasResumen = MeLanbide59Manager.getInstance().getListaResumenPorExpediente(numExpediente, adaptador);
                        for(FilaResumenVO fila : filasResumen)
                        {
                            coste = fila.getCosteSubvecionable();
                            coste = coste.replace(",", ".");
                            if(!coste.equals("")){
                                jus = new BigDecimal(coste);
                            }
                            justificado=justificado.add(jus);
                        }
                        String[] pepe = null;
                        if(numExpediente != null)
                            pepe = numExpediente.split("/");
                        
                        MeLanbide59Manager.getInstance().actualizaImporteJusti(codOrganizacion, numExpediente, pepe[0], justificado, adaptador);
                        
                    }
                    catch(Exception ex)
                    {
                        ex.printStackTrace();
                        log.error("Error al actualizar el importe justificado del campo suplementario: " + ex.getMessage());
                    }
                }
                calculos = MeLanbide59Manager.getInstance().cargarCalculosCpe(codOrganizacion, ejercicio, numExpediente, adaptador);
                
                if(justificado != null)
                {
                    calculos.put(ConstantesMeLanbide59.CAMPO_SUPL_IMP_JUS2, justificado);
                }

                //int resultado = MeLanbide59Manager.getInstance().guardarValorCampoNumerico(codOrganizacion, ejercicio.toString(), numExpediente, "IMPJUS", calculos.get(ConstantesMeLanbide59.CAMPO_SUPL_IMP_JUS), adaptador);
                        
                CpePuestoVO puesto = new CpePuestoVO();
                puesto.setEjercicio(ejercicio);
                puesto.setNumExp(numExpediente);
                puesto.setCodPuesto(codPuesto);
                puesto = MeLanbide59Manager.getInstance().getPuestoPorCodigoYExpediente(puesto, adaptador);
                if(puesto != null)
                {
                    personasContratadas = MeLanbide59Manager.getInstance().getListaContratacionesPuesto(puesto, adaptador);
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
        escribirListaContratacionesRequest(codigoOperacion, personasContratadas, calculos, response);
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
    
    private boolean validarDatosCpe(HttpServletRequest request)
    {
        try
        {
            String empresa = request.getParameter("empresa");
            String impSubvAprobadaExp = request.getParameter("impSubvAprobadaExp");
            String impSubvAprobadaConv = request.getParameter("impSubvAprobadaConv");
            String otrasAyudas = request.getParameter("otrasAyudas");
            String impSubvFinal = request.getParameter("impSubvFinal");
            
            if(!MeLanbide59Validator.validarTexto(empresa, 500))
            {
                return false;
            }
            if(!MeLanbide59Validator.validarNumericoDecimal(impSubvAprobadaExp, 10, 2))
            {
                return false;
            }
            if(!MeLanbide59Validator.validarNumericoDecimal(impSubvAprobadaConv, 10, 2))
            {
                return false;
            }
            if(!MeLanbide59Validator.validarNumericoDecimal(otrasAyudas, 10, 2))
            {
                return false;
            }
            if(!MeLanbide59Validator.validarNumericoDecimal(impSubvFinal, 10, 2))
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
            listaPaises = MeLanbide59Manager.getInstance().getListaPaises(this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
        }
        catch(Exception ex)
        {
            
        }
        request.setAttribute("listaPaises", listaPaises);
        list = new ArrayList<ValorCampoDesplegableModuloIntegracionVO>();
        
        //Combo IDIOMA
        try
        {
            String campoDesplegable = ConfigurationParameter.getParameter(ConstantesMeLanbide59.CAMPO_SUPL_IDIOMA, ConstantesMeLanbide59.FICHERO_PROPIEDADES);
        
            SalidaIntegracionVO salidaIntegracion = el.getCampoDesplegable(String.valueOf(codOrganizacion), campoDesplegable);
            list = salidaIntegracion.getCampoDesplegable().getValores();
            listaIdiomas = MeLanbide59MappingUtils.getInstance().fromCampoDesplegableToSelectItemVO(list);
        }
        catch(Exception ex)
        {
            
        }
        request.setAttribute("listaIdiomas", listaIdiomas);
        list = new ArrayList<ValorCampoDesplegableModuloIntegracionVO>();
        
        //Combo NIVEL IDIOMA
        try
        {
            String campoDesplegable = ConfigurationParameter.getParameter(ConstantesMeLanbide59.CAMPO_SUPL_NIVEL_IDIOMA, ConstantesMeLanbide59.FICHERO_PROPIEDADES);
        
            SalidaIntegracionVO salidaIntegracion = el.getCampoDesplegable(String.valueOf(codOrganizacion), campoDesplegable);
            list = salidaIntegracion.getCampoDesplegable().getValores();
            listaNivelIdiomas = MeLanbide59MappingUtils.getInstance().fromCampoDesplegableToSelectItemVO(list);
        }
        catch(Exception ex)
        {
            
        }
        request.setAttribute("listaNivelIdiomas", listaNivelIdiomas);
        list = new ArrayList<ValorCampoDesplegableModuloIntegracionVO>();
        
        //Combo NIVEL FORMATIVO
        try
        {
            String campoDesplegable = ConfigurationParameter.getParameter(ConstantesMeLanbide59.CAMPO_SUPL_NIVEL_FORMATIVO, ConstantesMeLanbide59.FICHERO_PROPIEDADES);
        
            SalidaIntegracionVO salidaIntegracion = el.getCampoDesplegable(String.valueOf(codOrganizacion), campoDesplegable);
            list = salidaIntegracion.getCampoDesplegable().getValores();
            listaNivelFormativo = MeLanbide59MappingUtils.getInstance().fromCampoDesplegableToSelectItemVO(list);
        }
        catch(Exception ex)
        {
            
        }
        request.setAttribute("listaNivelFormativo", listaNivelFormativo);
        list = new ArrayList<ValorCampoDesplegableModuloIntegracionVO>();
        
        //Combo GRUPO COTIZACION
        try
        {
            String campoDesplegable = ConfigurationParameter.getParameter(ConstantesMeLanbide59.CAMPO_SUPL_GRUPO_COTIZACION, ConstantesMeLanbide59.FICHERO_PROPIEDADES);
        
            SalidaIntegracionVO salidaIntegracion = el.getCampoDesplegable(String.valueOf(codOrganizacion), campoDesplegable);
            list = salidaIntegracion.getCampoDesplegable().getValores();
            listaGrupoCotizacion = MeLanbide59MappingUtils.getInstance().fromCampoDesplegableToSelectItemVO(list, SelectItem.ORDENAR_POR_CODIGO);
        }
        catch(Exception ex)
        {
            
        }
        request.setAttribute("listaGrupoCotizacion", listaGrupoCotizacion);
        list = new ArrayList<ValorCampoDesplegableModuloIntegracionVO>();
        
        //Combo RESULTADO
        try
        {
            String campoDesplegable = ConfigurationParameter.getParameter(ConstantesMeLanbide59.CAMPO_SUPL_RESULTADO, ConstantesMeLanbide59.FICHERO_PROPIEDADES);
        
            SalidaIntegracionVO salidaIntegracion = el.getCampoDesplegable(String.valueOf(codOrganizacion), campoDesplegable);
            list = salidaIntegracion.getCampoDesplegable().getValores();
            listaResultado = MeLanbide59MappingUtils.getInstance().fromCampoDesplegableToSelectItemVO(list);
        }
        catch(Exception ex)
        {
            
        }
        request.setAttribute("listaResultado", listaResultado);
        list = new ArrayList<ValorCampoDesplegableModuloIntegracionVO>();
        
        //Combo MOTIVO
        try
        {
            String campoDesplegable = ConfigurationParameter.getParameter(ConstantesMeLanbide59.CAMPO_SUPL_MOTIVO, ConstantesMeLanbide59.FICHERO_PROPIEDADES);
        
            SalidaIntegracionVO salidaIntegracion = el.getCampoDesplegable(String.valueOf(codOrganizacion), campoDesplegable);
            list = salidaIntegracion.getCampoDesplegable().getValores();
            listaMotivos = MeLanbide59MappingUtils.getInstance().fromCampoDesplegableToSelectItemVO(list);
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
            listaPaises = MeLanbide59Manager.getInstance().getListaPaises(this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
        }
        catch(Exception ex)
        {
            
        }
        request.setAttribute("listaPaises", listaPaises);
        list = new ArrayList<ValorCampoDesplegableModuloIntegracionVO>();
        
        //Combo IDIOMA
        try
        {
            String campoDesplegable = ConfigurationParameter.getParameter(ConstantesMeLanbide59.CAMPO_SUPL_IDIOMA, ConstantesMeLanbide59.FICHERO_PROPIEDADES);
        
            SalidaIntegracionVO salidaIntegracion = el.getCampoDesplegable(String.valueOf(codOrganizacion), campoDesplegable);
            list = salidaIntegracion.getCampoDesplegable().getValores();
            listaIdiomas = MeLanbide59MappingUtils.getInstance().fromCampoDesplegableToSelectItemVO(list);
        }
        catch(Exception ex)
        {
            
        }
        request.setAttribute("listaIdiomas", listaIdiomas);
        list = new ArrayList<ValorCampoDesplegableModuloIntegracionVO>();
        
        //Combo NIVEL IDIOMA
        try
        {
            String campoDesplegable = ConfigurationParameter.getParameter(ConstantesMeLanbide59.CAMPO_SUPL_NIVEL_IDIOMA, ConstantesMeLanbide59.FICHERO_PROPIEDADES);
        
            SalidaIntegracionVO salidaIntegracion = el.getCampoDesplegable(String.valueOf(codOrganizacion), campoDesplegable);
            list = salidaIntegracion.getCampoDesplegable().getValores();
            listaNivelIdiomas = MeLanbide59MappingUtils.getInstance().fromCampoDesplegableToSelectItemVO(list);
        }
        catch(Exception ex)
        {
            
        }
        request.setAttribute("listaNivelIdiomas", listaNivelIdiomas);
        list = new ArrayList<ValorCampoDesplegableModuloIntegracionVO>();
        
        //Combo NIVEL FORMATIVO
        try
        {
            String campoDesplegable = ConfigurationParameter.getParameter(ConstantesMeLanbide59.CAMPO_SUPL_NIVEL_FORMATIVO, ConstantesMeLanbide59.FICHERO_PROPIEDADES);
        
            SalidaIntegracionVO salidaIntegracion = el.getCampoDesplegable(String.valueOf(codOrganizacion), campoDesplegable);
            list = salidaIntegracion.getCampoDesplegable().getValores();
            listaNivelFormativo = MeLanbide59MappingUtils.getInstance().fromCampoDesplegableToSelectItemVO(list);
        }
        catch(Exception ex)
        {
            
        }
        request.setAttribute("listaNivelFormativo", listaNivelFormativo);
        list = new ArrayList<ValorCampoDesplegableModuloIntegracionVO>();
        
        //Combo OFICINAS LANBIDE
        try
        {
            String campoDesplegable = ConfigurationParameter.getParameter(ConstantesMeLanbide59.CAMPO_SUPL_OFICINAS_LANBIDE, ConstantesMeLanbide59.FICHERO_PROPIEDADES);
        
            SalidaIntegracionVO salidaIntegracion = el.getCampoDesplegable(String.valueOf(codOrganizacion), campoDesplegable);
            list = salidaIntegracion.getCampoDesplegable().getValores();
            listaOficinasLanbide = MeLanbide59MappingUtils.getInstance().fromCampoDesplegableToSelectItemVO(list);
        }
        catch(Exception ex)
        {
            
        }
        request.setAttribute("listaOficinasGestion", listaOficinasLanbide);
        list = new ArrayList<ValorCampoDesplegableModuloIntegracionVO>();
        
        //Combo GRUPO COTIZACION
        try
        {
            String campoDesplegable = ConfigurationParameter.getParameter(ConstantesMeLanbide59.CAMPO_SUPL_GRUPO_COTIZACION, ConstantesMeLanbide59.FICHERO_PROPIEDADES);
        
            SalidaIntegracionVO salidaIntegracion = el.getCampoDesplegable(String.valueOf(codOrganizacion), campoDesplegable);
            list = salidaIntegracion.getCampoDesplegable().getValores();
            listaGrupoCot = MeLanbide59MappingUtils.getInstance().fromCampoDesplegableToSelectItemVO(list, SelectItem.ORDENAR_POR_CODIGO);
        }
        catch(Exception ex)
        {
            
        }
        request.setAttribute("listaGrupoCotizacion", listaGrupoCot);
        list = new ArrayList<ValorCampoDesplegableModuloIntegracionVO>();
        
        //Combo CAUSA BAJA
        try
        {
            String campoDesplegable = ConfigurationParameter.getParameter(ConstantesMeLanbide59.CAMPO_SUPL_CAUSA_BAJA, ConstantesMeLanbide59.FICHERO_PROPIEDADES);
        
            SalidaIntegracionVO salidaIntegracion = el.getCampoDesplegable(String.valueOf(codOrganizacion), campoDesplegable);
            list = salidaIntegracion.getCampoDesplegable().getValores();
            listaCausaBaja = MeLanbide59MappingUtils.getInstance().fromCampoDesplegableToSelectItemVO(list);
        }
        catch(Exception ex)
        {
            
        }
        request.setAttribute("listaCausaBaja", listaCausaBaja);
        list = new ArrayList<ValorCampoDesplegableModuloIntegracionVO>();
        
        //Combo TIPO DOCUMENTO
        try
        {
            listaTipoDoc = MeLanbide59Manager.getInstance().getTiposDocumento(this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
        }
        catch(Exception ex)
        {
            
        }
        request.setAttribute("listaNif", listaTipoDoc);
        
        //Combo CAUSA RENUNCIA
        try
        {
            String campoDesplegable = ConfigurationParameter.getParameter(ConstantesMeLanbide59.CAMPO_SUPL_CAUSA_RENUNCIA, ConstantesMeLanbide59.FICHERO_PROPIEDADES);
        
            SalidaIntegracionVO salidaIntegracion = el.getCampoDesplegable(String.valueOf(codOrganizacion), campoDesplegable);
            list = salidaIntegracion.getCampoDesplegable().getValores();
            listaCausaRenuncia = MeLanbide59MappingUtils.getInstance().fromCampoDesplegableToSelectItemVO(list);
        }
        catch(Exception ex)
        {
            
        }
        request.setAttribute("listaCausaRenuncia", listaCausaRenuncia);
        
        //Combo CAUSA RENUNCIA PRESENTA OFERTA
        try
        {
            String campoDesplegablePresOferta = ConfigurationParameter.getParameter(ConstantesMeLanbide59.CAMPO_SUPL_CAUSA_RENUNCIA, ConstantesMeLanbide59.FICHERO_PROPIEDADES);
        
            SalidaIntegracionVO salidaIntegracion = el.getCampoDesplegable(String.valueOf(codOrganizacion), campoDesplegablePresOferta);
            list = salidaIntegracion.getCampoDesplegable().getValores();
            listaCausaRenunciaPresOferta = MeLanbide59MappingUtils.getInstance().fromCampoDesplegableToSelectItemVO(list);
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
            String campoDesplegable = ConfigurationParameter.getParameter(ConstantesMeLanbide59.CAMPO_SUPL_GRUPO_COTIZACION, ConstantesMeLanbide59.FICHERO_PROPIEDADES);
        
            SalidaIntegracionVO salidaIntegracion = el.getCampoDesplegable(String.valueOf(codOrganizacion), campoDesplegable);
            list = salidaIntegracion.getCampoDesplegable().getValores();
            listaGrupoCot = MeLanbide59MappingUtils.getInstance().fromCampoDesplegableToSelectItemVO(list, SelectItem.ORDENAR_POR_CODIGO);
        }
        catch(Exception ex)
        {
            
        }
        request.setAttribute("listaGrupoCotizacion", listaGrupoCot);
        list = new ArrayList<ValorCampoDesplegableModuloIntegracionVO>();
        
        //Combo CAUSA BAJA
        try
        {
            String campoDesplegable = ConfigurationParameter.getParameter(ConstantesMeLanbide59.CAMPO_SUPL_CAUSA_BAJA, ConstantesMeLanbide59.FICHERO_PROPIEDADES);
        
            SalidaIntegracionVO salidaIntegracion = el.getCampoDesplegable(String.valueOf(codOrganizacion), campoDesplegable);
            list = salidaIntegracion.getCampoDesplegable().getValores();
            listaCausaBaja = MeLanbide59MappingUtils.getInstance().fromCampoDesplegableToSelectItemVO(list);
        }
        catch(Exception ex)
        {
            
        }
        request.setAttribute("listaCausaBaja", listaCausaBaja);
        list = new ArrayList<ValorCampoDesplegableModuloIntegracionVO>();
        
        //Combo TIPO DOCUMENTO
        try
        {
            listaTipoDoc = MeLanbide59Manager.getInstance().getTiposDocumento(this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
        }
        catch(Exception ex)
        {
            
        }
        request.setAttribute("listaNif", listaTipoDoc);
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
            listaPaises = MeLanbide59Manager.getInstance().getListaPaises(this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
        }
        catch(Exception ex)
        {
            
        }
        request.setAttribute("listaPaises", listaPaises);
        list = new ArrayList<ValorCampoDesplegableModuloIntegracionVO>();
        
        //Combo IDIOMA
        try
        {
            String campoDesplegable = ConfigurationParameter.getParameter(ConstantesMeLanbide59.CAMPO_SUPL_IDIOMA, ConstantesMeLanbide59.FICHERO_PROPIEDADES);
        
            SalidaIntegracionVO salidaIntegracion = el.getCampoDesplegable(String.valueOf(codOrganizacion), campoDesplegable);
            list = salidaIntegracion.getCampoDesplegable().getValores();
            listaIdiomas = MeLanbide59MappingUtils.getInstance().fromCampoDesplegableToSelectItemVO(list);
        }
        catch(Exception ex)
        {
            
        }
        request.setAttribute("listaIdiomas", listaIdiomas);
        list = new ArrayList<ValorCampoDesplegableModuloIntegracionVO>();
        
        //Combo NIVEL IDIOMA
        try
        {
            String campoDesplegable = ConfigurationParameter.getParameter(ConstantesMeLanbide59.CAMPO_SUPL_NIVEL_IDIOMA, ConstantesMeLanbide59.FICHERO_PROPIEDADES);
        
            SalidaIntegracionVO salidaIntegracion = el.getCampoDesplegable(String.valueOf(codOrganizacion), campoDesplegable);
            list = salidaIntegracion.getCampoDesplegable().getValores();
            listaNivelIdiomas = MeLanbide59MappingUtils.getInstance().fromCampoDesplegableToSelectItemVO(list);
        }
        catch(Exception ex)
        {
            
        }
        request.setAttribute("listaNivelIdiomas", listaNivelIdiomas);
        list = new ArrayList<ValorCampoDesplegableModuloIntegracionVO>();
        
        //Combo NIVEL FORMATIVO
        try
        {
            String campoDesplegable = ConfigurationParameter.getParameter(ConstantesMeLanbide59.CAMPO_SUPL_NIVEL_FORMATIVO, ConstantesMeLanbide59.FICHERO_PROPIEDADES);
        
            SalidaIntegracionVO salidaIntegracion = el.getCampoDesplegable(String.valueOf(codOrganizacion), campoDesplegable);
            list = salidaIntegracion.getCampoDesplegable().getValores();
            listaNivelFormativo = MeLanbide59MappingUtils.getInstance().fromCampoDesplegableToSelectItemVO(list);
        }
        catch(Exception ex)
        {
            
        }
        request.setAttribute("listaNivelFormativo", listaNivelFormativo);
        list = new ArrayList<ValorCampoDesplegableModuloIntegracionVO>();
    }
    
    private void cargarImportesJustificacion(String numExpediente, int codOrganizacion, CpePuestoVO puesto, HttpServletRequest request)
    {
        
            Integer ejercicio = MeLanbide59Utils.getEjercicioDeExpediente(numExpediente);
            AdaptadorSQLBD adapt =this.getAdaptSQLBD(String.valueOf(codOrganizacion));
                          
            String codigoCampo = null;
            //importe concedido
            BigDecimal concedido = null;
            try
            {
                codigoCampo = ConfigurationParameter.getParameter(ConstantesMeLanbide59.CAMPO_SUPL_IMP_CON, ConstantesMeLanbide59.FICHERO_PROPIEDADES);
                /*concedido = MeLanbide59Manager.getInstance().getValorCampoNumerico(codOrganizacion, String.valueOf(ejercicio), numExpediente, codigoCampo, adapt);
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
                    if(puesto.getCodResult() != null && puesto.getCodResult().equalsIgnoreCase(ConstantesMeLanbide59.CODIGO_RESULTADO_CONCEDIDO))
                    {
                        concedido = puesto.getImpMaxSuv() != null ? puesto.getImpMaxSuv().add(new BigDecimal(ConstantesMeLanbide59.CERO_CON_DECIMALES)) : new BigDecimal(ConstantesMeLanbide59.CERO_CON_DECIMALES);
                    }
                }
                if(concedido == null)
                {
                    concedido = new BigDecimal(ConstantesMeLanbide59.CERO_CON_DECIMALES);
                }
                
                request.setAttribute("concedido", concedido.toPlainString());
            }
            catch(Exception ex)
            {
                ex.printStackTrace();
            }

            //importe renunciado
            BigDecimal renunciado = null;
            try
            {
                codigoCampo = ConfigurationParameter.getParameter(ConstantesMeLanbide59.CAMPO_SUPL_IMP_REN, ConstantesMeLanbide59.FICHERO_PROPIEDADES);
                renunciado = MeLanbide59Manager.getInstance().getValorCampoNumerico(codOrganizacion, String.valueOf(ejercicio), numExpediente, codigoCampo, adapt);
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
    
    private void escribirListaPuestosRequest(String codigoOperacion, List<FilaPuestoVO> puestos, Map<String, BigDecimal> calculos, HttpServletResponse response)
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
            if(calculos.containsKey(ConstantesMeLanbide59.CAMPO_SUPL_IMP_SOLICITADO))
            {
                BigDecimal bd = calculos.get(ConstantesMeLanbide59.CAMPO_SUPL_IMP_SOLICITADO);
                xmlSalida.append(bd.toPlainString().replaceAll("\\.", ","));
            }
            xmlSalida.append("</IMP_SOL>");
            
            xmlSalida.append("<IMP_CONV>");
            if(calculos.containsKey(ConstantesMeLanbide59.CAMPO_SUPL_IMP_CONVOCATORIA))
            {
                BigDecimal bd = calculos.get(ConstantesMeLanbide59.CAMPO_SUPL_IMP_CONVOCATORIA);
                xmlSalida.append(bd.toPlainString().replaceAll("\\.", ","));
            }
            xmlSalida.append("</IMP_CONV>");
            
            xmlSalida.append("<IMP_PREV_CON>");
            if(calculos.containsKey(ConstantesMeLanbide59.CAMPO_SUPL_IMP_PREV_CON))
            {
                BigDecimal bd = calculos.get(ConstantesMeLanbide59.CAMPO_SUPL_IMP_PREV_CON);
                xmlSalida.append(bd.toPlainString().replaceAll("\\.", ","));
            }
            xmlSalida.append("</IMP_PREV_CON>");
            
            xmlSalida.append("<IMP_CON>");
            if(calculos.containsKey(ConstantesMeLanbide59.CAMPO_SUPL_IMP_CON))
            {
                BigDecimal bd = calculos.get(ConstantesMeLanbide59.CAMPO_SUPL_IMP_CON);
                xmlSalida.append(bd.toPlainString().replaceAll("\\.", ","));
            }
            xmlSalida.append("</IMP_CON>");
            
            xmlSalida.append("<IMP_JUS>");
            if(calculos.containsKey(ConstantesMeLanbide59.CAMPO_SUPL_IMP_JUS))
            {
                BigDecimal bd = calculos.get(ConstantesMeLanbide59.CAMPO_SUPL_IMP_JUS);
                xmlSalida.append(bd.toPlainString().replaceAll("\\.", ","));
            }
            xmlSalida.append("</IMP_JUS>");
            
            xmlSalida.append("<IMP_REN>");
            if(calculos.containsKey(ConstantesMeLanbide59.CAMPO_SUPL_IMP_REN))
            {
                BigDecimal bd = calculos.get(ConstantesMeLanbide59.CAMPO_SUPL_IMP_REN);
                xmlSalida.append(bd.toPlainString().replaceAll("\\.", ","));
            }
            xmlSalida.append("</IMP_REN>");
            
            xmlSalida.append("<IMP_PAG>");
            if(calculos.containsKey(ConstantesMeLanbide59.CAMPO_SUPL_IMP_PAG))
            {
                BigDecimal bd = calculos.get(ConstantesMeLanbide59.CAMPO_SUPL_IMP_PAG);
                xmlSalida.append(bd.toPlainString().replaceAll("\\.", ","));
            }
            xmlSalida.append("</IMP_PAG>");
            
            xmlSalida.append("<IMP_PAG_2>");
            if(calculos.containsKey(ConstantesMeLanbide59.CAMPO_SUPL_IMP_PAG_2))
            {
                BigDecimal bd = calculos.get(ConstantesMeLanbide59.CAMPO_SUPL_IMP_PAG_2);
                xmlSalida.append(bd.toPlainString().replaceAll("\\.", ","));
            }
            xmlSalida.append("</IMP_PAG_2>");
            
            xmlSalida.append("<IMP_REI>");
            if(calculos.containsKey(ConstantesMeLanbide59.CAMPO_SUPL_IMP_REI))
            {
                BigDecimal bd = calculos.get(ConstantesMeLanbide59.CAMPO_SUPL_IMP_REI);
                xmlSalida.append(bd.toPlainString().replaceAll("\\.", ","));
            }
            xmlSalida.append("</IMP_REI>");
            
            xmlSalida.append("<OTRAS_AYUDAS_SOLIC>");
            if(calculos.containsKey(ConstantesMeLanbide59.CAMPO_SUPL_OTRAS_AYUDAS_SOLIC))
            {
                BigDecimal bd = calculos.get(ConstantesMeLanbide59.CAMPO_SUPL_OTRAS_AYUDAS_SOLIC);
                xmlSalida.append(bd.toPlainString().replaceAll("\\.", ","));
            }
            xmlSalida.append("</OTRAS_AYUDAS_SOLIC>");
            
            xmlSalida.append("<OTRAS_AYUDAS_CONCE>");
            if(calculos.containsKey(ConstantesMeLanbide59.CAMPO_SUPL_OTRAS_AYUDAS_CONCE))
            {
                BigDecimal bd = calculos.get(ConstantesMeLanbide59.CAMPO_SUPL_OTRAS_AYUDAS_CONCE);
                xmlSalida.append(bd.toPlainString().replaceAll("\\.", ","));
            }
            xmlSalida.append("</OTRAS_AYUDAS_CONCE>");
            
            xmlSalida.append("<MINIMIS_SOLIC>");
            if(calculos.containsKey(ConstantesMeLanbide59.CAMPO_SUPL_MINIMIS_SOLIC))
            {
                BigDecimal bd = calculos.get(ConstantesMeLanbide59.CAMPO_SUPL_MINIMIS_SOLIC);
                xmlSalida.append(bd.toPlainString().replaceAll("\\.", ","));
            }
            xmlSalida.append("</MINIMIS_SOLIC>");
            
            xmlSalida.append("<MINIMIS_CONCE>");
            if(calculos.containsKey(ConstantesMeLanbide59.CAMPO_SUPL_MINIMIS_CONCE))
            {
                BigDecimal bd = calculos.get(ConstantesMeLanbide59.CAMPO_SUPL_MINIMIS_CONCE);
                xmlSalida.append(bd.toPlainString().replaceAll("\\.", ","));
            }
            xmlSalida.append("</MINIMIS_CONCE>");
            
            xmlSalida.append("<IMP_DESP>");
            if(calculos.containsKey(ConstantesMeLanbide59.CAMPO_SUPL_IMP_REN))
            {
                BigDecimal bd = calculos.get(ConstantesMeLanbide59.CAMPO_SUPL_IMP_DESP);
                xmlSalida.append(bd.toPlainString().replaceAll("\\.", ","));
            }
            xmlSalida.append("</IMP_DESP>");
            
            xmlSalida.append("<IMP_BAJA>");
            if(calculos.containsKey(ConstantesMeLanbide59.CAMPO_SUPL_IMP_BAJA))
            {
                BigDecimal bd = calculos.get(ConstantesMeLanbide59.CAMPO_SUPL_IMP_BAJA);
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
            if(calculos.containsKey(ConstantesMeLanbide59.CAMPO_SUPL_IMP_NO_JUS))
            {
                BigDecimal bd = calculos.get(ConstantesMeLanbide59.CAMPO_SUPL_IMP_NO_JUS);
                xmlSalida.append(bd.toPlainString().replaceAll("\\.", ","));
            }
            xmlSalida.append("</IMP_NO_JUS>");
            
            xmlSalida.append("<IMP_REN_RES>");
            if(calculos.containsKey(ConstantesMeLanbide59.CAMPO_SUPL_IMP_REN_RES))
            {
                BigDecimal bd = calculos.get(ConstantesMeLanbide59.CAMPO_SUPL_IMP_REN_RES);
                xmlSalida.append(bd.toPlainString().replaceAll("\\.", ","));
            }
            xmlSalida.append("</IMP_REN_RES>");
            
            xmlSalida.append("<IMP_PAG_RES>");
            if(calculos.containsKey(ConstantesMeLanbide59.CAMPO_SUPL_IMP_PAG_RES))
            {
                BigDecimal bd = calculos.get(ConstantesMeLanbide59.CAMPO_SUPL_IMP_PAG_RES);
                xmlSalida.append(bd.toPlainString().replaceAll("\\.", ","));
            }
            xmlSalida.append("</IMP_PAG_RES>");
            
            xmlSalida.append("<IMP_JUS2>");
            if(calculos.containsKey(ConstantesMeLanbide59.CAMPO_SUPL_IMP_JUS2))
            {
                BigDecimal bd = calculos.get(ConstantesMeLanbide59.CAMPO_SUPL_IMP_JUS2);
                xmlSalida.append(bd.toPlainString().replaceAll("\\.", ","));
            }
            xmlSalida.append("</IMP_JUS2>");
            
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
    
    private void escribirListaOfertasRequest(String codigoOperacion, List<FilaOfertaVO> ofertas, Map<String, BigDecimal> calculos, HttpServletResponse response)
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
            if(calculos.containsKey(ConstantesMeLanbide59.CAMPO_SUPL_IMP_SOLICITADO))
            {
                BigDecimal bd = calculos.get(ConstantesMeLanbide59.CAMPO_SUPL_IMP_SOLICITADO);
                xmlSalida.append(bd.toPlainString().replaceAll("\\.", ","));
            }
            xmlSalida.append("</IMP_SOL>");
            
            xmlSalida.append("<IMP_CONV>");
            if(calculos.containsKey(ConstantesMeLanbide59.CAMPO_SUPL_IMP_CONVOCATORIA))
            {
                BigDecimal bd = calculos.get(ConstantesMeLanbide59.CAMPO_SUPL_IMP_CONVOCATORIA);
                xmlSalida.append(bd.toPlainString().replaceAll("\\.", ","));
            }
            xmlSalida.append("</IMP_CONV>");
            
            xmlSalida.append("<IMP_PREV_CON>");
            if(calculos.containsKey(ConstantesMeLanbide59.CAMPO_SUPL_IMP_PREV_CON))
            {
                BigDecimal bd = calculos.get(ConstantesMeLanbide59.CAMPO_SUPL_IMP_PREV_CON);
                xmlSalida.append(bd.toPlainString().replaceAll("\\.", ","));
            }
            xmlSalida.append("</IMP_PREV_CON>");
            
            xmlSalida.append("<IMP_CON>");
            if(calculos.containsKey(ConstantesMeLanbide59.CAMPO_SUPL_IMP_CON))
            {
                BigDecimal bd = calculos.get(ConstantesMeLanbide59.CAMPO_SUPL_IMP_CON);
                xmlSalida.append(bd.toPlainString().replaceAll("\\.", ","));
            }
            xmlSalida.append("</IMP_CON>");
            
            xmlSalida.append("<IMP_JUS>");
            if(calculos.containsKey(ConstantesMeLanbide59.CAMPO_SUPL_IMP_JUS))
            {
                BigDecimal bd = calculos.get(ConstantesMeLanbide59.CAMPO_SUPL_IMP_JUS);
                xmlSalida.append(bd.toPlainString().replaceAll("\\.", ","));
            }
            xmlSalida.append("</IMP_JUS>");
            
            xmlSalida.append("<IMP_REN>");
            if(calculos.containsKey(ConstantesMeLanbide59.CAMPO_SUPL_IMP_REN))
            {
                BigDecimal bd = calculos.get(ConstantesMeLanbide59.CAMPO_SUPL_IMP_REN);
                xmlSalida.append(bd.toPlainString().replaceAll("\\.", ","));
            }
            xmlSalida.append("</IMP_REN>");
            
            xmlSalida.append("<IMP_PAG>");
            if(calculos.containsKey(ConstantesMeLanbide59.CAMPO_SUPL_IMP_PAG))
            {
                BigDecimal bd = calculos.get(ConstantesMeLanbide59.CAMPO_SUPL_IMP_PAG);
                xmlSalida.append(bd.toPlainString().replaceAll("\\.", ","));
            }
            xmlSalida.append("</IMP_PAG>");
            
            xmlSalida.append("<IMP_PAG_2>");
            if(calculos.containsKey(ConstantesMeLanbide59.CAMPO_SUPL_IMP_PAG_2))
            {
                BigDecimal bd = calculos.get(ConstantesMeLanbide59.CAMPO_SUPL_IMP_PAG_2);
                xmlSalida.append(bd.toPlainString().replaceAll("\\.", ","));
            }
            xmlSalida.append("</IMP_PAG_2>");
            
            xmlSalida.append("<IMP_REI>");
            if(calculos.containsKey(ConstantesMeLanbide59.CAMPO_SUPL_IMP_REI))
            {
                BigDecimal bd = calculos.get(ConstantesMeLanbide59.CAMPO_SUPL_IMP_REI);
                xmlSalida.append(bd.toPlainString().replaceAll("\\.", ","));
            }
            xmlSalida.append("</IMP_REI>");
            
            xmlSalida.append("<OTRAS_AYUDAS_SOLIC>");
            if(calculos.containsKey(ConstantesMeLanbide59.CAMPO_SUPL_OTRAS_AYUDAS_SOLIC))
            {
                BigDecimal bd = calculos.get(ConstantesMeLanbide59.CAMPO_SUPL_OTRAS_AYUDAS_SOLIC);
                xmlSalida.append(bd.toPlainString().replaceAll("\\.", ","));
            }
            xmlSalida.append("</OTRAS_AYUDAS_SOLIC>");
            
            xmlSalida.append("<OTRAS_AYUDAS_CONCE>");
            if(calculos.containsKey(ConstantesMeLanbide59.CAMPO_SUPL_OTRAS_AYUDAS_CONCE))
            {
                BigDecimal bd = calculos.get(ConstantesMeLanbide59.CAMPO_SUPL_OTRAS_AYUDAS_CONCE);
                xmlSalida.append(bd.toPlainString().replaceAll("\\.", ","));
            }
            xmlSalida.append("</OTRAS_AYUDAS_CONCE>");
            
            xmlSalida.append("<MINIMIS_SOLIC>");
            if(calculos.containsKey(ConstantesMeLanbide59.CAMPO_SUPL_MINIMIS_SOLIC))
            {
                BigDecimal bd = calculos.get(ConstantesMeLanbide59.CAMPO_SUPL_MINIMIS_SOLIC);
                xmlSalida.append(bd.toPlainString().replaceAll("\\.", ","));
            }
            xmlSalida.append("</MINIMIS_SOLIC>");
            
            xmlSalida.append("<MINIMIS_CONCE>");
            if(calculos.containsKey(ConstantesMeLanbide59.CAMPO_SUPL_MINIMIS_CONCE))
            {
                BigDecimal bd = calculos.get(ConstantesMeLanbide59.CAMPO_SUPL_MINIMIS_CONCE);
                xmlSalida.append(bd.toPlainString().replaceAll("\\.", ","));
            }
            xmlSalida.append("</MINIMIS_CONCE>");
            
            xmlSalida.append("<IMP_DESP>");
            if(calculos.containsKey(ConstantesMeLanbide59.CAMPO_SUPL_IMP_REN))
            {
                BigDecimal bd = calculos.get(ConstantesMeLanbide59.CAMPO_SUPL_IMP_DESP);
                xmlSalida.append(bd.toPlainString().replaceAll("\\.", ","));
            }
            xmlSalida.append("</IMP_DESP>");
            
            xmlSalida.append("<IMP_BAJA>");
            if(calculos.containsKey(ConstantesMeLanbide59.CAMPO_SUPL_IMP_BAJA))
            {
                BigDecimal bd = calculos.get(ConstantesMeLanbide59.CAMPO_SUPL_IMP_BAJA);
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
            if(calculos.containsKey(ConstantesMeLanbide59.CAMPO_SUPL_IMP_NO_JUS))
            {
                BigDecimal bd = calculos.get(ConstantesMeLanbide59.CAMPO_SUPL_IMP_NO_JUS);
                xmlSalida.append(bd.toPlainString().replaceAll("\\.", ","));
            }
            xmlSalida.append("</IMP_NO_JUS>");
            
            xmlSalida.append("<IMP_REN_RES>");
            if(calculos.containsKey(ConstantesMeLanbide59.CAMPO_SUPL_IMP_REN_RES))
            {
                BigDecimal bd = calculos.get(ConstantesMeLanbide59.CAMPO_SUPL_IMP_REN_RES);
                xmlSalida.append(bd.toPlainString().replaceAll("\\.", ","));
            }
            xmlSalida.append("</IMP_REN_RES>");
            
            xmlSalida.append("<IMP_PAG_RES>");
            if(calculos.containsKey(ConstantesMeLanbide59.CAMPO_SUPL_IMP_PAG_RES))
            {
                BigDecimal bd = calculos.get(ConstantesMeLanbide59.CAMPO_SUPL_IMP_PAG_RES);
                xmlSalida.append(bd.toPlainString().replaceAll("\\.", ","));
            }
            xmlSalida.append("</IMP_PAG_RES>");
            
            xmlSalida.append("<IMP_JUS2>");
            if(calculos.containsKey(ConstantesMeLanbide59.CAMPO_SUPL_IMP_JUS2))
            {
                BigDecimal bd = calculos.get(ConstantesMeLanbide59.CAMPO_SUPL_IMP_JUS2);
                xmlSalida.append(bd.toPlainString().replaceAll("\\.", ","));
            }
            xmlSalida.append("</IMP_JUS2>");
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
    
    private void escribirListaJustificacionesRequest(String codigoOperacion, List<FilaJustificacionVO> justificaciones, Map<String, BigDecimal> calculos, HttpServletResponse response)
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
            if(calculos.containsKey(ConstantesMeLanbide59.CAMPO_SUPL_IMP_SOLICITADO))
            {
                BigDecimal bd = calculos.get(ConstantesMeLanbide59.CAMPO_SUPL_IMP_SOLICITADO);
                xmlSalida.append(bd.toPlainString().replaceAll("\\.", ","));
            }
            xmlSalida.append("</IMP_SOL>");
            
            xmlSalida.append("<IMP_CONV>");
            if(calculos.containsKey(ConstantesMeLanbide59.CAMPO_SUPL_IMP_CONVOCATORIA))
            {
                BigDecimal bd = calculos.get(ConstantesMeLanbide59.CAMPO_SUPL_IMP_CONVOCATORIA);
                xmlSalida.append(bd.toPlainString().replaceAll("\\.", ","));
            }
            xmlSalida.append("</IMP_CONV>");
            
            xmlSalida.append("<IMP_PREV_CON>");
            if(calculos.containsKey(ConstantesMeLanbide59.CAMPO_SUPL_IMP_PREV_CON))
            {
                BigDecimal bd = calculos.get(ConstantesMeLanbide59.CAMPO_SUPL_IMP_PREV_CON);
                xmlSalida.append(bd.toPlainString().replaceAll("\\.", ","));
            }
            xmlSalida.append("</IMP_PREV_CON>");
            
            xmlSalida.append("<IMP_CON>");
            if(calculos.containsKey(ConstantesMeLanbide59.CAMPO_SUPL_IMP_CON))
            {
                BigDecimal bd = calculos.get(ConstantesMeLanbide59.CAMPO_SUPL_IMP_CON);
                xmlSalida.append(bd.toPlainString().replaceAll("\\.", ","));
            }
            xmlSalida.append("</IMP_CON>");
            
            xmlSalida.append("<IMP_JUS>");
            if(calculos.containsKey(ConstantesMeLanbide59.CAMPO_SUPL_IMP_JUS))
            {
                BigDecimal bd = calculos.get(ConstantesMeLanbide59.CAMPO_SUPL_IMP_JUS);
                xmlSalida.append(bd.toPlainString().replaceAll("\\.", ","));
            }
            xmlSalida.append("</IMP_JUS>");
            
            xmlSalida.append("<IMP_REN>");
            if(calculos.containsKey(ConstantesMeLanbide59.CAMPO_SUPL_IMP_REN))
            {
                BigDecimal bd = calculos.get(ConstantesMeLanbide59.CAMPO_SUPL_IMP_REN);
                xmlSalida.append(bd.toPlainString().replaceAll("\\.", ","));
            }
            xmlSalida.append("</IMP_REN>");
            
            xmlSalida.append("<IMP_PAG>");
            if(calculos.containsKey(ConstantesMeLanbide59.CAMPO_SUPL_IMP_PAG))
            {
                BigDecimal bd = calculos.get(ConstantesMeLanbide59.CAMPO_SUPL_IMP_PAG);
                xmlSalida.append(bd.toPlainString().replaceAll("\\.", ","));
            }
            xmlSalida.append("</IMP_PAG>");
            
            xmlSalida.append("<IMP_PAG_2>");
            if(calculos.containsKey(ConstantesMeLanbide59.CAMPO_SUPL_IMP_PAG_2))
            {
                BigDecimal bd = calculos.get(ConstantesMeLanbide59.CAMPO_SUPL_IMP_PAG_2);
                xmlSalida.append(bd.toPlainString().replaceAll("\\.", ","));
            }
            xmlSalida.append("</IMP_PAG_2>");
            
            xmlSalida.append("<IMP_REI>");
            if(calculos.containsKey(ConstantesMeLanbide59.CAMPO_SUPL_IMP_REI))
            {
                BigDecimal bd = calculos.get(ConstantesMeLanbide59.CAMPO_SUPL_IMP_REI);
                xmlSalida.append(bd.toPlainString().replaceAll("\\.", ","));
            }
            xmlSalida.append("</IMP_REI>");
            
            xmlSalida.append("<OTRAS_AYUDAS_SOLIC>");
            if(calculos.containsKey(ConstantesMeLanbide59.CAMPO_SUPL_OTRAS_AYUDAS_SOLIC))
            {
                BigDecimal bd = calculos.get(ConstantesMeLanbide59.CAMPO_SUPL_OTRAS_AYUDAS_SOLIC);
                xmlSalida.append(bd.toPlainString().replaceAll("\\.", ","));
            }
            xmlSalida.append("</OTRAS_AYUDAS_SOLIC>");
            
            xmlSalida.append("<OTRAS_AYUDAS_CONCE>");
            if(calculos.containsKey(ConstantesMeLanbide59.CAMPO_SUPL_OTRAS_AYUDAS_CONCE))
            {
                BigDecimal bd = calculos.get(ConstantesMeLanbide59.CAMPO_SUPL_OTRAS_AYUDAS_CONCE);
                xmlSalida.append(bd.toPlainString().replaceAll("\\.", ","));
            }
            xmlSalida.append("</OTRAS_AYUDAS_CONCE>");
            
            xmlSalida.append("<MINIMIS_SOLIC>");
            if(calculos.containsKey(ConstantesMeLanbide59.CAMPO_SUPL_MINIMIS_SOLIC))
            {
                BigDecimal bd = calculos.get(ConstantesMeLanbide59.CAMPO_SUPL_MINIMIS_SOLIC);
                xmlSalida.append(bd.toPlainString().replaceAll("\\.", ","));
            }
            xmlSalida.append("</MINIMIS_SOLIC>");
            
            xmlSalida.append("<MINIMIS_CONCE>");
            if(calculos.containsKey(ConstantesMeLanbide59.CAMPO_SUPL_MINIMIS_CONCE))
            {
                BigDecimal bd = calculos.get(ConstantesMeLanbide59.CAMPO_SUPL_MINIMIS_CONCE);
                xmlSalida.append(bd.toPlainString().replaceAll("\\.", ","));
            }
            xmlSalida.append("</MINIMIS_CONCE>");
            
            xmlSalida.append("<IMP_DESP>");
            if(calculos.containsKey(ConstantesMeLanbide59.CAMPO_SUPL_IMP_REN))
            {
                BigDecimal bd = calculos.get(ConstantesMeLanbide59.CAMPO_SUPL_IMP_DESP);
                xmlSalida.append(bd.toPlainString().replaceAll("\\.", ","));
            }
            xmlSalida.append("</IMP_DESP>");
            
            xmlSalida.append("<IMP_BAJA>");
            if(calculos.containsKey(ConstantesMeLanbide59.CAMPO_SUPL_IMP_BAJA))
            {
                BigDecimal bd = calculos.get(ConstantesMeLanbide59.CAMPO_SUPL_IMP_BAJA);
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
            if(calculos.containsKey(ConstantesMeLanbide59.CAMPO_SUPL_IMP_NO_JUS))
            {
                BigDecimal bd = calculos.get(ConstantesMeLanbide59.CAMPO_SUPL_IMP_NO_JUS);
                xmlSalida.append(bd.toPlainString().replaceAll("\\.", ","));
            }
            xmlSalida.append("</IMP_NO_JUS>");
            
            xmlSalida.append("<IMP_REN_RES>");
            if(calculos.containsKey(ConstantesMeLanbide59.CAMPO_SUPL_IMP_REN_RES))
            {
                BigDecimal bd = calculos.get(ConstantesMeLanbide59.CAMPO_SUPL_IMP_REN_RES);
                xmlSalida.append(bd.toPlainString().replaceAll("\\.", ","));
            }
            xmlSalida.append("</IMP_REN_RES>");
            
            xmlSalida.append("<IMP_PAG_RES>");
            if(calculos.containsKey(ConstantesMeLanbide59.CAMPO_SUPL_IMP_PAG_RES))
            {
                BigDecimal bd = calculos.get(ConstantesMeLanbide59.CAMPO_SUPL_IMP_PAG_RES);
                xmlSalida.append(bd.toPlainString().replaceAll("\\.", ","));
            }
            xmlSalida.append("</IMP_PAG_RES>");
            
            xmlSalida.append("<IMP_JUS2>");
            if(calculos.containsKey(ConstantesMeLanbide59.CAMPO_SUPL_IMP_JUS2))
            {
                BigDecimal bd = calculos.get(ConstantesMeLanbide59.CAMPO_SUPL_IMP_JUS2);
                xmlSalida.append(bd.toPlainString().replaceAll("\\.", ","));
            }
            xmlSalida.append("</IMP_JUS2>");
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
                xmlSalida.append("<GRUPO>");
                        xmlSalida.append(filaResumen.getGrupo());
                xmlSalida.append("</GRUPO>");
                xmlSalida.append("<DNI>");
                        xmlSalida.append(filaResumen.getDni());
                xmlSalida.append("</DNI>");
                xmlSalida.append("<PAIS_SOLICITADO>");
                        xmlSalida.append(filaResumen.getPaisSolicitado());
                xmlSalida.append("</PAIS_SOLICITADO>");
                xmlSalida.append("<PAIS_FINAL>");
                    xmlSalida.append(filaResumen.getPaisFinal());
                xmlSalida.append("</PAIS_FINAL>");
                xmlSalida.append("<MESES_CONTRATO>");
                    xmlSalida.append(filaResumen.getMesesContrato());
                xmlSalida.append("</MESES_CONTRATO>");
                xmlSalida.append("<FEC_INI>");
                    xmlSalida.append(filaResumen.getFecIni());
                xmlSalida.append("</FEC_INI>");
                xmlSalida.append("<FEC_FIN>");
                        xmlSalida.append(filaResumen.getFecFin());
                xmlSalida.append("</FEC_FIN>");
                xmlSalida.append("<TOTAL_DIAS>");
                        xmlSalida.append(filaResumen.getTotalDias());
                xmlSalida.append("</TOTAL_DIAS>");
                xmlSalida.append("<DIAS_SEG>");
                        xmlSalida.append(filaResumen.getTotalDiasSeg());
                xmlSalida.append("</DIAS_SEG>");
                xmlSalida.append("<SALARIO_ANEXO>");
                        xmlSalida.append(filaResumen.getSalarioAnexo());
                xmlSalida.append("</SALARIO_ANEXO>");
                xmlSalida.append("<MAXIMO_SUBV>");
                        xmlSalida.append(filaResumen.getMaximoSubv());
                xmlSalida.append("</MAXIMO_SUBV>");
                xmlSalida.append("<MINORACION>");
                        xmlSalida.append(filaResumen.getMinoracion());
                xmlSalida.append("</MINORACION>");
                xmlSalida.append("<DEVENGADO>");
                        xmlSalida.append(filaResumen.getDevengado());
                xmlSalida.append("</DEVENGADO>");
                xmlSalida.append("<BC_PERIODO>");
                        xmlSalida.append(filaResumen.getBcPeriodo());
                xmlSalida.append("</BC_PERIODO>");
                xmlSalida.append("<COEFICIENTE_TGSS>");
                        xmlSalida.append(filaResumen.getCoeficienteTGSS());
                xmlSalida.append("</COEFICIENTE_TGSS>");
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
                xmlSalida.append("<TOTAL_PERIODO>");
                        xmlSalida.append(filaResumen.getTotalPeriodo());
                xmlSalida.append("</TOTAL_PERIODO>");
                xmlSalida.append("<SUBV_MINORADA>");
                        xmlSalida.append(filaResumen.getSubvMinorada());
                xmlSalida.append("</SUBV_MINORADA>");
                xmlSalida.append("<MAXIMO_PERIODO_SUBV>");
                        xmlSalida.append(filaResumen.getMaximoPeriodoSubv());
                xmlSalida.append("</MAXIMO_PERIODO_SUBV>");
                xmlSalida.append("<COSTE_PERIODO_SUBV>");
                        xmlSalida.append(filaResumen.getCostePeriodoSubv());
                xmlSalida.append("</COSTE_PERIODO_SUBV>");
                xmlSalida.append("<COSTE_JUSTIFICACION>");
                        xmlSalida.append(filaResumen.getCosteJustificacion());
                xmlSalida.append("</COSTE_JUSTIFICACION>");
                xmlSalida.append("<DIETAS_PERIODO_SUBV>");
                        xmlSalida.append(filaResumen.getSeguroSubv());
                xmlSalida.append("</DIETAS_PERIODO_SUBV>");
                xmlSalida.append("<DIETAS_CONCEDIDAS>");
                        xmlSalida.append(filaResumen.getSeguroJus());
                xmlSalida.append("</DIETAS_CONCEDIDAS>");
                xmlSalida.append("<MAXIMO_DIETAS>");
                        xmlSalida.append(filaResumen.getSeguroMax());
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
                xmlSalida.append("<CONTRATO_BONIF>");
                        xmlSalida.append(filaResumen.getContratoBonif());
                xmlSalida.append("</CONTRATO_BONIF>");
                xmlSalida.append("<COSTE_SUBVENCIONABLE>");
                        xmlSalida.append(filaResumen.getCosteSubvecionable());
                xmlSalida.append("</COSTE_SUBVENCIONABLE>");
                xmlSalida.append("<MESES_EXT>");
                        xmlSalida.append(filaResumen.getMesesExt());
                xmlSalida.append("</MESES_EXT>");
                xmlSalida.append("<SALARIO_P>");
                        xmlSalida.append(filaResumen.getSalario());
                xmlSalida.append("</SALARIO_P>");
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

    
    private void escribirListaContratacionesRequest(String codigoOperacion, List<FilaPersonaContratadaVO> contrataciones, Map<String, BigDecimal> calculos, HttpServletResponse response)
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
            if(calculos.containsKey(ConstantesMeLanbide59.CAMPO_SUPL_IMP_SOLICITADO))
            {
                BigDecimal bd = calculos.get(ConstantesMeLanbide59.CAMPO_SUPL_IMP_SOLICITADO);
                xmlSalida.append(bd.toPlainString().replaceAll("\\.", ","));
            }
            xmlSalida.append("</IMP_SOL>");
            
            xmlSalida.append("<IMP_CONV>");
            if(calculos.containsKey(ConstantesMeLanbide59.CAMPO_SUPL_IMP_CONVOCATORIA))
            {
                BigDecimal bd = calculos.get(ConstantesMeLanbide59.CAMPO_SUPL_IMP_CONVOCATORIA);
                xmlSalida.append(bd.toPlainString().replaceAll("\\.", ","));
            }
            xmlSalida.append("</IMP_CONV>");
            
            xmlSalida.append("<IMP_PREV_CON>");
            if(calculos.containsKey(ConstantesMeLanbide59.CAMPO_SUPL_IMP_PREV_CON))
            {
                BigDecimal bd = calculos.get(ConstantesMeLanbide59.CAMPO_SUPL_IMP_PREV_CON);
                xmlSalida.append(bd.toPlainString().replaceAll("\\.", ","));
            }
            xmlSalida.append("</IMP_PREV_CON>");
            
            xmlSalida.append("<IMP_CON>");
            if(calculos.containsKey(ConstantesMeLanbide59.CAMPO_SUPL_IMP_CON))
            {
                BigDecimal bd = calculos.get(ConstantesMeLanbide59.CAMPO_SUPL_IMP_CON);
                xmlSalida.append(bd.toPlainString().replaceAll("\\.", ","));
            }
            xmlSalida.append("</IMP_CON>");
            
            xmlSalida.append("<IMP_JUS>");
            if(calculos.containsKey(ConstantesMeLanbide59.CAMPO_SUPL_IMP_JUS))
            {
                BigDecimal bd = calculos.get(ConstantesMeLanbide59.CAMPO_SUPL_IMP_JUS);
                xmlSalida.append(bd.toPlainString().replaceAll("\\.", ","));
            }
            xmlSalida.append("</IMP_JUS>");
            
            xmlSalida.append("<IMP_REN>");
            if(calculos.containsKey(ConstantesMeLanbide59.CAMPO_SUPL_IMP_REN))
            {
                BigDecimal bd = calculos.get(ConstantesMeLanbide59.CAMPO_SUPL_IMP_REN);
                xmlSalida.append(bd.toPlainString().replaceAll("\\.", ","));
            }
            xmlSalida.append("</IMP_REN>");
            
            xmlSalida.append("<IMP_PAG>");
            if(calculos.containsKey(ConstantesMeLanbide59.CAMPO_SUPL_IMP_PAG))
            {
                BigDecimal bd = calculos.get(ConstantesMeLanbide59.CAMPO_SUPL_IMP_PAG);
                xmlSalida.append(bd.toPlainString().replaceAll("\\.", ","));
            }
            xmlSalida.append("</IMP_PAG>");
            
            xmlSalida.append("<IMP_PAG_2>");
            if(calculos.containsKey(ConstantesMeLanbide59.CAMPO_SUPL_IMP_PAG_2))
            {
                BigDecimal bd = calculos.get(ConstantesMeLanbide59.CAMPO_SUPL_IMP_PAG_2);
                xmlSalida.append(bd.toPlainString().replaceAll("\\.", ","));
            }
            xmlSalida.append("</IMP_PAG_2>");
            
            xmlSalida.append("<IMP_REI>");
            if(calculos.containsKey(ConstantesMeLanbide59.CAMPO_SUPL_IMP_REI))
            {
                BigDecimal bd = calculos.get(ConstantesMeLanbide59.CAMPO_SUPL_IMP_REI);
                xmlSalida.append(bd.toPlainString().replaceAll("\\.", ","));
            }
            xmlSalida.append("</IMP_REI>");
            
            xmlSalida.append("<OTRAS_AYUDAS_SOLIC>");
            if(calculos.containsKey(ConstantesMeLanbide59.CAMPO_SUPL_OTRAS_AYUDAS_SOLIC))
            {
                BigDecimal bd = calculos.get(ConstantesMeLanbide59.CAMPO_SUPL_OTRAS_AYUDAS_SOLIC);
                xmlSalida.append(bd.toPlainString().replaceAll("\\.", ","));
            }
            xmlSalida.append("</OTRAS_AYUDAS_SOLIC>");
            
            xmlSalida.append("<OTRAS_AYUDAS_CONCE>");
            if(calculos.containsKey(ConstantesMeLanbide59.CAMPO_SUPL_OTRAS_AYUDAS_CONCE))
            {
                BigDecimal bd = calculos.get(ConstantesMeLanbide59.CAMPO_SUPL_OTRAS_AYUDAS_CONCE);
                xmlSalida.append(bd.toPlainString().replaceAll("\\.", ","));
            }
            xmlSalida.append("</OTRAS_AYUDAS_CONCE>");
            
            xmlSalida.append("<MINIMIS_SOLIC>");
            if(calculos.containsKey(ConstantesMeLanbide59.CAMPO_SUPL_MINIMIS_SOLIC))
            {
                BigDecimal bd = calculos.get(ConstantesMeLanbide59.CAMPO_SUPL_MINIMIS_SOLIC);
                xmlSalida.append(bd.toPlainString().replaceAll("\\.", ","));
            }
            xmlSalida.append("</MINIMIS_SOLIC>");
            
            xmlSalida.append("<MINIMIS_CONCE>");
            if(calculos.containsKey(ConstantesMeLanbide59.CAMPO_SUPL_MINIMIS_CONCE))
            {
                BigDecimal bd = calculos.get(ConstantesMeLanbide59.CAMPO_SUPL_MINIMIS_CONCE);
                xmlSalida.append(bd.toPlainString().replaceAll("\\.", ","));
            }
            xmlSalida.append("</MINIMIS_CONCE>");
            
            xmlSalida.append("<IMP_DESP>");
            if(calculos.containsKey(ConstantesMeLanbide59.CAMPO_SUPL_IMP_REN))
            {
                BigDecimal bd = calculos.get(ConstantesMeLanbide59.CAMPO_SUPL_IMP_DESP);
                xmlSalida.append(bd.toPlainString().replaceAll("\\.", ","));
            }
            xmlSalida.append("</IMP_DESP>");
            
            xmlSalida.append("<IMP_BAJA>");
            if(calculos.containsKey(ConstantesMeLanbide59.CAMPO_SUPL_IMP_BAJA))
            {
                BigDecimal bd = calculos.get(ConstantesMeLanbide59.CAMPO_SUPL_IMP_BAJA);
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
            if(calculos.containsKey(ConstantesMeLanbide59.CAMPO_SUPL_IMP_NO_JUS))
            {
                BigDecimal bd = calculos.get(ConstantesMeLanbide59.CAMPO_SUPL_IMP_NO_JUS);
                xmlSalida.append(bd.toPlainString().replaceAll("\\.", ","));
            }
            xmlSalida.append("</IMP_NO_JUS>");
            
            xmlSalida.append("<IMP_REN_RES>");
            if(calculos.containsKey(ConstantesMeLanbide59.CAMPO_SUPL_IMP_REN_RES))
            {
                BigDecimal bd = calculos.get(ConstantesMeLanbide59.CAMPO_SUPL_IMP_REN_RES);
                xmlSalida.append(bd.toPlainString().replaceAll("\\.", ","));
            }
            xmlSalida.append("</IMP_REN_RES>");
            
            xmlSalida.append("<IMP_PAG_RES>");
            if(calculos.containsKey(ConstantesMeLanbide59.CAMPO_SUPL_IMP_PAG_RES))
            {
                BigDecimal bd = calculos.get(ConstantesMeLanbide59.CAMPO_SUPL_IMP_PAG_RES);
                xmlSalida.append(bd.toPlainString().replaceAll("\\.", ","));
            }
            xmlSalida.append("</IMP_PAG_RES>");
            
            xmlSalida.append("<IMP_JUS2>");
            if(calculos.containsKey(ConstantesMeLanbide59.CAMPO_SUPL_IMP_JUS2))
            {
                BigDecimal bd = calculos.get(ConstantesMeLanbide59.CAMPO_SUPL_IMP_JUS2);
                xmlSalida.append(bd.toPlainString().replaceAll("\\.", ","));
            }
            xmlSalida.append("</IMP_JUS2>");
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
    
    private void escribirCalculosCpeRequest(String codigoOperacion, Map<String, BigDecimal> calculos, HttpServletResponse response)
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
            if(calculos.containsKey(ConstantesMeLanbide59.CAMPO_SUPL_IMP_SOLICITADO))
            {
                BigDecimal bd = calculos.get(ConstantesMeLanbide59.CAMPO_SUPL_IMP_SOLICITADO);
                xmlSalida.append(bd.toPlainString().replaceAll("\\.", ","));
            }
            xmlSalida.append("</IMP_SOL>");
            
            xmlSalida.append("<IMP_CONV>");
            if(calculos.containsKey(ConstantesMeLanbide59.CAMPO_SUPL_IMP_CONVOCATORIA))
            {
                BigDecimal bd = calculos.get(ConstantesMeLanbide59.CAMPO_SUPL_IMP_CONVOCATORIA);
                xmlSalida.append(bd.toPlainString().replaceAll("\\.", ","));
            }
            xmlSalida.append("</IMP_CONV>");
            
            xmlSalida.append("<IMP_PREV_CON>");
            if(calculos.containsKey(ConstantesMeLanbide59.CAMPO_SUPL_IMP_PREV_CON))
            {
                BigDecimal bd = calculos.get(ConstantesMeLanbide59.CAMPO_SUPL_IMP_PREV_CON);
                xmlSalida.append(bd.toPlainString().replaceAll("\\.", ","));
            }
            xmlSalida.append("</IMP_PREV_CON>");
            
            xmlSalida.append("<IMP_CON>");
            if(calculos.containsKey(ConstantesMeLanbide59.CAMPO_SUPL_IMP_CON))
            {
                BigDecimal bd = calculos.get(ConstantesMeLanbide59.CAMPO_SUPL_IMP_CON);
                xmlSalida.append(bd.toPlainString().replaceAll("\\.", ","));
            }
            xmlSalida.append("</IMP_CON>");
            
            xmlSalida.append("<IMP_JUS>");
            if(calculos.containsKey(ConstantesMeLanbide59.CAMPO_SUPL_IMP_JUS))
            {
                BigDecimal bd = calculos.get(ConstantesMeLanbide59.CAMPO_SUPL_IMP_JUS);
                xmlSalida.append(bd.toPlainString().replaceAll("\\.", ","));
            }
            xmlSalida.append("</IMP_JUS>");
            
            xmlSalida.append("<IMP_REN>");
            if(calculos.containsKey(ConstantesMeLanbide59.CAMPO_SUPL_IMP_REN))
            {
                BigDecimal bd = calculos.get(ConstantesMeLanbide59.CAMPO_SUPL_IMP_REN);
                xmlSalida.append(bd.toPlainString().replaceAll("\\.", ","));
            }
            xmlSalida.append("</IMP_REN>");
            
            xmlSalida.append("<IMP_PAG>");
            if(calculos.containsKey(ConstantesMeLanbide59.CAMPO_SUPL_IMP_PAG))
            {
                BigDecimal bd = calculos.get(ConstantesMeLanbide59.CAMPO_SUPL_IMP_PAG);
                xmlSalida.append(bd.toPlainString().replaceAll("\\.", ","));
            }
            xmlSalida.append("</IMP_PAG>");
            
            xmlSalida.append("<IMP_PAG_2>");
            if(calculos.containsKey(ConstantesMeLanbide59.CAMPO_SUPL_IMP_PAG_2))
            {
                BigDecimal bd = calculos.get(ConstantesMeLanbide59.CAMPO_SUPL_IMP_PAG_2);
                xmlSalida.append(bd.toPlainString().replaceAll("\\.", ","));
            }
            xmlSalida.append("</IMP_PAG_2>");
            
            xmlSalida.append("<IMP_REI>");
            if(calculos.containsKey(ConstantesMeLanbide59.CAMPO_SUPL_IMP_REI))
            {
                BigDecimal bd = calculos.get(ConstantesMeLanbide59.CAMPO_SUPL_IMP_REI);
                xmlSalida.append(bd.toPlainString().replaceAll("\\.", ","));
            }
            xmlSalida.append("</IMP_REI>");
            
            xmlSalida.append("<OTRAS_AYUDAS_SOLIC>");
            if(calculos.containsKey(ConstantesMeLanbide59.CAMPO_SUPL_OTRAS_AYUDAS_SOLIC))
            {
                BigDecimal bd = calculos.get(ConstantesMeLanbide59.CAMPO_SUPL_OTRAS_AYUDAS_SOLIC);
                xmlSalida.append(bd.toPlainString().replaceAll("\\.", ","));
            }
            xmlSalida.append("</OTRAS_AYUDAS_SOLIC>");
            
            xmlSalida.append("<OTRAS_AYUDAS_CONCE>");
            if(calculos.containsKey(ConstantesMeLanbide59.CAMPO_SUPL_OTRAS_AYUDAS_CONCE))
            {
                BigDecimal bd = calculos.get(ConstantesMeLanbide59.CAMPO_SUPL_OTRAS_AYUDAS_CONCE);
                xmlSalida.append(bd.toPlainString().replaceAll("\\.", ","));
            }
            xmlSalida.append("</OTRAS_AYUDAS_CONCE>");
            
            xmlSalida.append("<MINIMIS_SOLIC>");
            if(calculos.containsKey(ConstantesMeLanbide59.CAMPO_SUPL_MINIMIS_SOLIC))
            {
                BigDecimal bd = calculos.get(ConstantesMeLanbide59.CAMPO_SUPL_MINIMIS_SOLIC);
                xmlSalida.append(bd.toPlainString().replaceAll("\\.", ","));
            }
            xmlSalida.append("</MINIMIS_SOLIC>");
            
            xmlSalida.append("<MINIMIS_CONCE>");
            if(calculos.containsKey(ConstantesMeLanbide59.CAMPO_SUPL_MINIMIS_CONCE))
            {
                BigDecimal bd = calculos.get(ConstantesMeLanbide59.CAMPO_SUPL_MINIMIS_CONCE);
                xmlSalida.append(bd.toPlainString().replaceAll("\\.", ","));
            }
            xmlSalida.append("</MINIMIS_CONCE>");
            
            xmlSalida.append("<IMP_DESP>");
            if(calculos.containsKey(ConstantesMeLanbide59.CAMPO_SUPL_IMP_REN))
            {
                BigDecimal bd = calculos.get(ConstantesMeLanbide59.CAMPO_SUPL_IMP_DESP);
                xmlSalida.append(bd.toPlainString().replaceAll("\\.", ","));
            }
            xmlSalida.append("</IMP_DESP>");
            
            xmlSalida.append("<IMP_BAJA>");
            if(calculos.containsKey(ConstantesMeLanbide59.CAMPO_SUPL_IMP_BAJA))
            {
                BigDecimal bd = calculos.get(ConstantesMeLanbide59.CAMPO_SUPL_IMP_BAJA);
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
            if(calculos.containsKey(ConstantesMeLanbide59.CAMPO_SUPL_IMP_NO_JUS))
            {
                BigDecimal bd = calculos.get(ConstantesMeLanbide59.CAMPO_SUPL_IMP_NO_JUS);
                xmlSalida.append(bd.toPlainString().replaceAll("\\.", ","));
            }
            xmlSalida.append("</IMP_NO_JUS>");
            
            xmlSalida.append("<IMP_REN_RES>");
            if(calculos.containsKey(ConstantesMeLanbide59.CAMPO_SUPL_IMP_REN_RES))
            {
                BigDecimal bd = calculos.get(ConstantesMeLanbide59.CAMPO_SUPL_IMP_REN_RES);
                xmlSalida.append(bd.toPlainString().replaceAll("\\.", ","));
            }
            xmlSalida.append("</IMP_REN_RES>");
            
            xmlSalida.append("<IMP_PAG_RES>");
            if(calculos.containsKey(ConstantesMeLanbide59.CAMPO_SUPL_IMP_PAG_RES))
            {
                BigDecimal bd = calculos.get(ConstantesMeLanbide59.CAMPO_SUPL_IMP_PAG_RES);
                xmlSalida.append(bd.toPlainString().replaceAll("\\.", ","));
            }
            xmlSalida.append("</IMP_PAG_RES>");
            xmlSalida.append("<IMP_JUS2>");
            if(calculos.containsKey(ConstantesMeLanbide59.CAMPO_SUPL_IMP_JUS2))
            {
                BigDecimal bd = calculos.get(ConstantesMeLanbide59.CAMPO_SUPL_IMP_JUS2);
                xmlSalida.append(bd.toPlainString().replaceAll("\\.", ","));
            }
            xmlSalida.append("</IMP_JUS2>");
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
        
        AdaptadorSQLBD adaptador = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
        try
        {
            String fini = (String)request.getParameter("fini");
            String ffin = (String)request.getParameter("ffin");
            
            meses = MeLanbide59Manager.getInstance().getNumMeses(fini, ffin, adaptador);
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
        
        AdaptadorSQLBD adaptador = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
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
            
            Integer ejercicio = MeLanbide59Utils.getEjercicioDeExpediente(numExpediente);
            if(ejercicio != null)
            {
                SimpleDateFormat format = new SimpleDateFormat(ConstantesMeLanbide59.FORMATO_FECHA);
                
                MultipartRequestWrapper wrapper = new MultipartRequestWrapper(request);
            
                wrapper.getParameterMap();
                
                String codOferta = (String)request.getParameter("idOferta");
                String codPuesto = (String)request.getParameter("idPuesto");

                cargarCombosNuevaOferta(codOrganizacion, request);

                CpeOfertaVO oferta = null;
                CpePuestoVO puesto = new CpePuestoVO();
                
                    puesto.setCodPuesto(codPuesto);
                    puesto.setEjercicio(ejercicio);
                    puesto.setNumExp(numExpediente);
                    puesto = MeLanbide59Manager.getInstance().getPuestoPorCodigoYExpediente(puesto, adaptador);
                         
                    oferta = new CpeOfertaVO();
                    oferta.setIdOferta(Integer.parseInt(codOferta));
                    oferta.setCodPuesto(codPuesto);
                    oferta.setExpEje(ejercicio);
                    oferta.setNumExp(numExpediente);
                    oferta = MeLanbide59Manager.getInstance().getOfertaPorCodigoPuestoYExpediente(oferta, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
                                        
                    oferta = MeLanbide59Manager.getInstance().copiarDatosPuesto(codOrganizacion, puesto, oferta, adaptador);
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
                             desc = MeLanbide59Manager.getInstance().getDescripcionTitulacion(oferta.getCodTit1(), adaptador);
                             xmlSalida.append(oferta.getCodTit1()!=null?desc:"");
                        xmlSalida.append("</DESCTITU1>");
                        xmlSalida.append("<DESCTITU2>");
                            desc = MeLanbide59Manager.getInstance().getDescripcionTitulacion(oferta.getCodTit2(), adaptador);
                            xmlSalida.append(oferta.getCodTit2()!=null?desc:"");
                        xmlSalida.append("</DESCTITU2>");
                        xmlSalida.append("<DESCTITU3>");
                            desc = MeLanbide59Manager.getInstance().getDescripcionTitulacion(oferta.getCodTit3(), adaptador);
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
                            CpeOfertaVO ofertaOrigen = new CpeOfertaVO();
                            ofertaOrigen.setIdOferta(oferta.getIdOfertaOrigen());
                            ofertaOrigen.setExpEje(ejercicio);
                            ofertaOrigen.setNumExp(numExpediente);
                            ofertaOrigen = MeLanbide59Manager.getInstance().getOfertaPorCodigoPuestoYExpediente(ofertaOrigen, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
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
        //return "/jsp/extension/melanbide59/nuevaOferta.jsp?codOrganizacionModulo="+codOrganizacion;
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
                    Integer ejercicio = MeLanbide59Utils.getEjercicioDeExpediente(numExpediente);
                    AdaptadorSQLBD adaptador = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
                    CpePuestoVO puesto = new CpePuestoVO();
                    puesto.setCodPuesto(idPuesto);
                    puesto.setEjercicio(ejercicio);
                    puesto.setNumExp(numExpediente);
                    puesto = MeLanbide59Manager.getInstance().getPuestoPorCodigoYExpediente(puesto, adaptador);
                    if(puesto != null)
                    {
                        request.setAttribute("puestoConsulta", puesto);
                    }
                    
                    CpeOfertaVO oferta = new CpeOfertaVO();
                    oferta.setIdOferta(Integer.parseInt(idOferta));
                    oferta.setCodPuesto(idPuesto);
                    oferta.setExpEje(ejercicio);
                    oferta.setNumExp(numExpediente);
                    oferta = MeLanbide59Manager.getInstance().getOfertaPorCodigoPuestoYExpediente(oferta, adaptador);
                    String rdo = "";
                    if(oferta != null)
                    {
                        request.setAttribute("ofertaConsulta", oferta);
                        String desc = null;
                        if(oferta.getCodTitulacion() != null && !oferta.getCodTitulacion().equals(""))
                        {
                            desc = MeLanbide59Manager.getInstance().getDescripcionTitulacion(oferta.getCodTitulacion(), adaptador);
                            if(desc != null)
                            {
                                request.setAttribute("descTitContratado", desc);
                            }
                        }
                        //Leire, cálculo de días
                        // String rdo = MeLanbide59Manager.getInstance().obtenerDiasTrabajados(numExpediente, oferta.getNif(), adaptador);
                        rdo = MeLanbide59Manager.getInstance().obtenerDiasTrabajados(numExpediente, oferta.getCodPuesto(),oferta.getIdOferta(), adaptador);
                                           
                    }
                    
                    CpeJustificacionVO justifModif = new CpeJustificacionVO();
                    justifModif.setCodPuesto(idPuesto);
                    justifModif.setEjercicio(ejercicio);
                    justifModif.setIdJustificacion(Integer.parseInt(idJustif));
                    justifModif.setIdOferta(Integer.parseInt(idOferta));
                    justifModif.setNumExpediente(numExpediente);
                    justifModif = MeLanbide59Manager.getInstance().getJustificacionPorCodigoPuestoYExpediente(justifModif, adaptador);
                    
                    int dias=0;
                    if(rdo != null && !rdo.equals(""))
                        dias = Integer.parseInt(rdo);
                    else
                        dias = 0;
                    if(justifModif.getDiasTrab() == null)
                    {
                        justifModif.setDiasTrab(dias);
                    }
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
        return "/jsp/extension/melanbide59/datosPersonaContratada.jsp";
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
            listaTitulaciones = MeLanbide59Manager.getInstance().buscarTitulaciones(codigo, desc, adapt);
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
            ejercicio = MeLanbide59Utils.getEjercicioDeExpediente(numExpediente);
        }
        catch(Exception ex)
        {

        }
        Map<String, BigDecimal> importes = null;
        Map<String, Integer> puestos = null;
        String gestor = null;
        String empresa = null;
        AdaptadorSQLBD adaptador = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
            
        try
        {
            importes = MeLanbide59Manager.getInstance().cargarCalculosCpe(codOrganizacion, ejercicio, numExpediente, adaptador);
        
            puestos = MeLanbide59Manager.getInstance().cargarCalculosPuestos(codOrganizacion, ejercicio, numExpediente, adaptador);
       
            //Cargo el gestor tramitador, empresa e importes de la subvencion (camposSuplementarios)
            gestor = MeLanbide59Manager.getInstance().getValorCampoDesplegable(codOrganizacion, String.valueOf(ejercicio), numExpediente, ConfigurationParameter.getParameter(ConstantesMeLanbide59.CAMPO_SUPL_GESTOR_TRAMITADOR, ConstantesMeLanbide59.FICHERO_PROPIEDADES), adaptador);
        
            empresa = MeLanbide59Manager.getInstance().getValorCampoTexto(codOrganizacion, String.valueOf(ejercicio), numExpediente, ConfigurationParameter.getParameter(ConstantesMeLanbide59.CAMPO_SUPL_EMPRESA, ConstantesMeLanbide59.FICHERO_PROPIEDADES), adaptador);
        
            StringBuffer xmlSalida = new StringBuffer();
            xmlSalida.append("<RESPUESTA>");
            xmlSalida.append("<CODIGO_OPERACION>");
                xmlSalida.append("0");
            xmlSalida.append("</CODIGO_OPERACION>");
            
            if(importes != null && !importes.isEmpty())
            {
                xmlSalida.append("<IMPORTES>");

                xmlSalida.append("<IMP_SOL>");
                if(importes.containsKey(ConstantesMeLanbide59.CAMPO_SUPL_IMP_SOLICITADO))
                {
                    BigDecimal bd = importes.get(ConstantesMeLanbide59.CAMPO_SUPL_IMP_SOLICITADO);
                    xmlSalida.append(bd.toPlainString().replaceAll("\\.", ","));
                }
                xmlSalida.append("</IMP_SOL>");

                xmlSalida.append("<IMP_CONV>");
                if(importes.containsKey(ConstantesMeLanbide59.CAMPO_SUPL_IMP_CONVOCATORIA))
                {
                    BigDecimal bd = importes.get(ConstantesMeLanbide59.CAMPO_SUPL_IMP_CONVOCATORIA);
                    xmlSalida.append(bd.toPlainString().replaceAll("\\.", ","));
                }
                xmlSalida.append("</IMP_CONV>");

                xmlSalida.append("<IMP_PREV_CON>");
                if(importes.containsKey(ConstantesMeLanbide59.CAMPO_SUPL_IMP_PREV_CON))
                {
                    BigDecimal bd = importes.get(ConstantesMeLanbide59.CAMPO_SUPL_IMP_PREV_CON);
                    xmlSalida.append(bd.toPlainString().replaceAll("\\.", ","));
                }
                xmlSalida.append("</IMP_PREV_CON>");

                xmlSalida.append("<IMP_CON>");
                if(importes.containsKey(ConstantesMeLanbide59.CAMPO_SUPL_IMP_CON))
                {
                    BigDecimal bd = importes.get(ConstantesMeLanbide59.CAMPO_SUPL_IMP_CON);
                    xmlSalida.append(bd.toPlainString().replaceAll("\\.", ","));
                }
                xmlSalida.append("</IMP_CON>");

                xmlSalida.append("<IMP_JUS>");
                if(importes.containsKey(ConstantesMeLanbide59.CAMPO_SUPL_IMP_JUS))
                {
                    BigDecimal bd = importes.get(ConstantesMeLanbide59.CAMPO_SUPL_IMP_JUS);
                    xmlSalida.append(bd.toPlainString().replaceAll("\\.", ","));
                }
                xmlSalida.append("</IMP_JUS>");

                xmlSalida.append("<IMP_REN_RES>");
                if(importes.containsKey(ConstantesMeLanbide59.CAMPO_SUPL_IMP_REN_RES))
                {
                    BigDecimal bd = importes.get(ConstantesMeLanbide59.CAMPO_SUPL_IMP_REN_RES);
                    xmlSalida.append(bd.toPlainString().replaceAll("\\.", ","));
                }
                xmlSalida.append("</IMP_REN_RES>");
                
                xmlSalida.append("<IMP_REN>");
                if(importes.containsKey(ConstantesMeLanbide59.CAMPO_SUPL_IMP_REN))
                {
                    BigDecimal bd = importes.get(ConstantesMeLanbide59.CAMPO_SUPL_IMP_REN);
                    xmlSalida.append(bd.toPlainString().replaceAll("\\.", ","));
                }
                xmlSalida.append("</IMP_REN>");

                xmlSalida.append("<IMP_PAG>");
                if(importes.containsKey(ConstantesMeLanbide59.CAMPO_SUPL_IMP_PAG))
                {
                    BigDecimal bd = importes.get(ConstantesMeLanbide59.CAMPO_SUPL_IMP_PAG);
                    xmlSalida.append(bd.toPlainString().replaceAll("\\.", ","));
                }
                xmlSalida.append("</IMP_PAG>");

                xmlSalida.append("<IMP_PAG_2>");
                if(importes.containsKey(ConstantesMeLanbide59.CAMPO_SUPL_IMP_PAG_2))
                {
                    BigDecimal bd = importes.get(ConstantesMeLanbide59.CAMPO_SUPL_IMP_PAG_2);
                    xmlSalida.append(bd.toPlainString().replaceAll("\\.", ","));
                }
                xmlSalida.append("</IMP_PAG_2>");

                xmlSalida.append("<IMP_REI>");
                if(importes.containsKey(ConstantesMeLanbide59.CAMPO_SUPL_IMP_REI))
                {
                    BigDecimal bd = importes.get(ConstantesMeLanbide59.CAMPO_SUPL_IMP_REI);
                    xmlSalida.append(bd.toPlainString().replaceAll("\\.", ","));
                }
                xmlSalida.append("</IMP_REI>");

                xmlSalida.append("<OTRAS_AYUDAS_SOLIC>");
                if(importes.containsKey(ConstantesMeLanbide59.CAMPO_SUPL_OTRAS_AYUDAS_SOLIC))
                {
                    BigDecimal bd = importes.get(ConstantesMeLanbide59.CAMPO_SUPL_OTRAS_AYUDAS_SOLIC);
                    xmlSalida.append(bd.toPlainString().replaceAll("\\.", ","));
                }
                xmlSalida.append("</OTRAS_AYUDAS_SOLIC>");

                xmlSalida.append("<OTRAS_AYUDAS_CONCE>");
                if(importes.containsKey(ConstantesMeLanbide59.CAMPO_SUPL_OTRAS_AYUDAS_CONCE))
                {
                    BigDecimal bd = importes.get(ConstantesMeLanbide59.CAMPO_SUPL_OTRAS_AYUDAS_CONCE);
                    xmlSalida.append(bd.toPlainString().replaceAll("\\.", ","));
                }
                xmlSalida.append("</OTRAS_AYUDAS_CONCE>");

                xmlSalida.append("<MINIMIS_SOLIC>");
                if(importes.containsKey(ConstantesMeLanbide59.CAMPO_SUPL_MINIMIS_SOLIC))
                {
                    BigDecimal bd = importes.get(ConstantesMeLanbide59.CAMPO_SUPL_MINIMIS_SOLIC);
                    xmlSalida.append(bd.toPlainString().replaceAll("\\.", ","));
                }
                xmlSalida.append("</MINIMIS_SOLIC>");

                xmlSalida.append("<MINIMIS_CONCE>");
                if(importes.containsKey(ConstantesMeLanbide59.CAMPO_SUPL_MINIMIS_CONCE))
                {
                    BigDecimal bd = importes.get(ConstantesMeLanbide59.CAMPO_SUPL_MINIMIS_CONCE);
                    xmlSalida.append(bd.toPlainString().replaceAll("\\.", ","));
                }
                xmlSalida.append("</MINIMIS_CONCE>");

                xmlSalida.append("<IMP_DESP>");
                if(importes.containsKey(ConstantesMeLanbide59.CAMPO_SUPL_IMP_REN))
                {
                    BigDecimal bd = importes.get(ConstantesMeLanbide59.CAMPO_SUPL_IMP_DESP);
                    xmlSalida.append(bd.toPlainString().replaceAll("\\.", ","));
                }
                xmlSalida.append("</IMP_DESP>");

                xmlSalida.append("<IMP_BAJA>");
                if(importes.containsKey(ConstantesMeLanbide59.CAMPO_SUPL_IMP_BAJA))
                {
                    BigDecimal bd = importes.get(ConstantesMeLanbide59.CAMPO_SUPL_IMP_BAJA);
                    xmlSalida.append(bd.toPlainString().replaceAll("\\.", ","));
                }
                xmlSalida.append("</IMP_BAJA>");

                xmlSalida.append("<IMP_BONIF>");
                if(importes.containsKey(ConstantesMeLanbide59.CAMPO_SUPL_IMP_BONIF))
                {
                    BigDecimal bd = importes.get(ConstantesMeLanbide59.CAMPO_SUPL_IMP_BONIF);
                    xmlSalida.append(bd.toPlainString().replaceAll("\\.", ","));
                }
                xmlSalida.append("</IMP_BONIF>");
                xmlSalida.append("<IMP_JUS2>");
                if(importes.containsKey(ConstantesMeLanbide59.CAMPO_SUPL_IMP_JUS2))
                {
                    BigDecimal bd = importes.get(ConstantesMeLanbide59.CAMPO_SUPL_IMP_JUS2);
                    xmlSalida.append(bd.toPlainString().replaceAll("\\.", ","));
                }
                xmlSalida.append("</IMP_JUS2>");
                xmlSalida.append("</IMPORTES>");

                xmlSalida.append("<PUESTOS>");
            
                xmlSalida.append("<PUESTOS_SOLICITADOS>");
                if(puestos.containsKey(ConstantesMeLanbide59.CAMPO_SUPL_PUESTOS_SOLICITADOS))
                {
                    Integer i = puestos.get(ConstantesMeLanbide59.CAMPO_SUPL_PUESTOS_SOLICITADOS);
                    xmlSalida.append(i.toString());
                }
                xmlSalida.append("</PUESTOS_SOLICITADOS>");
            
                xmlSalida.append("<PUESTOS_DENEGADOS>");
                if(puestos.containsKey(ConstantesMeLanbide59.CAMPO_SUPL_PUESTOS_DENEGADOS))
                {
                    Integer i = puestos.get(ConstantesMeLanbide59.CAMPO_SUPL_PUESTOS_DENEGADOS);
                    xmlSalida.append(i.toString());
                }
                xmlSalida.append("</PUESTOS_DENEGADOS>");
            
                xmlSalida.append("<PUESTOS_CONTRATADOS>");
                if(puestos.containsKey(ConstantesMeLanbide59.CAMPO_SUPL_PUESTOS_CONTRATADOS))
                {
                    Integer i = puestos.get(ConstantesMeLanbide59.CAMPO_SUPL_PUESTOS_CONTRATADOS);
                    xmlSalida.append(i.toString());
                }
                xmlSalida.append("</PUESTOS_CONTRATADOS>");
            
                xmlSalida.append("<PERSONAS_CONTRATADAS>");
                if(puestos.containsKey(ConstantesMeLanbide59.CAMPO_SUPL_PERSONAS_CONTRATADAS))
                {
                    Integer i = puestos.get(ConstantesMeLanbide59.CAMPO_SUPL_PERSONAS_CONTRATADAS);
                    xmlSalida.append(i.toString());
                }
                xmlSalida.append("</PERSONAS_CONTRATADAS>");
            
                xmlSalida.append("<PUESTOS_DESPIDO>");
                if(puestos.containsKey(ConstantesMeLanbide59.CAMPO_SUPL_PUESTOS_DESPIDO))
                {
                    Integer i = puestos.get(ConstantesMeLanbide59.CAMPO_SUPL_PUESTOS_DESPIDO);
                    xmlSalida.append(i.toString());
                }
                xmlSalida.append("</PUESTOS_DESPIDO>");
            
                xmlSalida.append("<PERSONAS_DESPIDO>");
                if(puestos.containsKey(ConstantesMeLanbide59.CAMPO_SUPL_PERSONAS_DESPIDO))
                {
                    Integer i = puestos.get(ConstantesMeLanbide59.CAMPO_SUPL_PERSONAS_DESPIDO);
                    xmlSalida.append(i.toString());
                }
                xmlSalida.append("</PERSONAS_DESPIDO>");
            
                xmlSalida.append("<PUESTOS_BAJA>");
                if(puestos.containsKey(ConstantesMeLanbide59.CAMPO_SUPL_PUESTOS_BAJA))
                {
                    Integer i = puestos.get(ConstantesMeLanbide59.CAMPO_SUPL_PUESTOS_BAJA);
                    xmlSalida.append(i.toString());
                }
                xmlSalida.append("</PUESTOS_BAJA>");
            
                xmlSalida.append("<PERSONAS_BAJA>");
                if(puestos.containsKey(ConstantesMeLanbide59.CAMPO_SUPL_PERSONAS_BAJA))
                {
                    Integer i = puestos.get(ConstantesMeLanbide59.CAMPO_SUPL_PERSONAS_BAJA);
                    xmlSalida.append(i.toString());
                }
                xmlSalida.append("</PERSONAS_BAJA>");
            
                xmlSalida.append("<PUESTOS_RENUNCIADOS>");
                if(puestos.containsKey(ConstantesMeLanbide59.CAMPO_SUPL_PUESTOS_RENUNCIADOS))
                {
                    Integer i = puestos.get(ConstantesMeLanbide59.CAMPO_SUPL_PUESTOS_RENUNCIADOS);
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
     
     public void grabarImportesResolucion(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente) throws Exception
     {
        Integer ejercicio = MeLanbide59Utils.getEjercicioDeExpediente(numExpediente);
        MeLanbide59Manager.getInstance().grabarImportesResolucion(codOrganizacion, ejercicio, numExpediente, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
     }
     
     public void grabarImportesResolucionModif(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente) throws Exception
     {
        Integer ejercicio = MeLanbide59Utils.getEjercicioDeExpediente(numExpediente);
        MeLanbide59Manager.getInstance().grabarImportesResolucionModif(codOrganizacion, ejercicio, numExpediente, this.getAdaptSQLBD(String.valueOf(codOrganizacion)));
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
            ejercicio = MeLanbide59Utils.getEjercicioDeExpediente(numExpediente);
            String res = (String)request.getParameter("res");
            if(ejercicio != null)
            {
                if(res != null && !res.equals(""))
                {
                    request.setAttribute("res", res);
                    AdaptadorSQLBD adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
                    HashMap<String, Object> valores = MeLanbide59Manager.getInstance().cargarImportesResolucion(codOrganizacion, ejercicio, numExpediente, res, adapt);
                    Iterator<String> it = valores.keySet().iterator();
                    String key = null;
                    Object valor = null;
                    String str = null;
                    SimpleDateFormat format = new SimpleDateFormat(ConstantesMeLanbide59.FORMATO_FECHA);
                    while(it.hasNext())
                    {
                        key = it.next();
                        valor = valores.get(key);
                        if(valor instanceof BigDecimal)
                        {
                            str = MeLanbide59Utils.redondearDecimalesString((BigDecimal)valor, 2);
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
                    request.setAttribute("error", MeLanbide59I18n.getInstance().getMensaje(codIdioma, "error.datosRes.resNoDeterminada"));
                }
            }
            else
            {
                request.setAttribute("error", MeLanbide59I18n.getInstance().getMensaje(codIdioma, "error.datosRes.ejercicioNoDeterminado"));
            }
         }
         catch(Exception ex)
         {
            
         }
         return "/jsp/extension/melanbide59/datosResolucion.jsp";
     }
     
     public String cargarHistorico(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente, HttpServletRequest request, HttpServletResponse response)
     {
         //cargarDatosCpe(codOrganizacion, numExpediente, request);
        
        //Cargo los datos de las pestaÃ±as
        AdaptadorSQLBD adapt = null;
        String codTra ="";
        String url = null;
        try
        {
            adapt = this.getAdaptSQLBD(String.valueOf(codOrganizacion));
            if(request != null && request.getParameter("tram") != null)
                codTra = request.getParameter("tram").toString();
                
            if (request != null) {
                url = cargarSubpestanaSolicitudHist_DatosCpe(codOrganizacion, numExpediente, codTra, adapt, request);
                if(url != null)
                {
                    request.setAttribute("urlPestanaDatos_solicitud", url);
                }

                url = cargarSubpestanaOfertaHist_DatosCpe(codOrganizacion, numExpediente, codTra, adapt, request);
                if(url != null)
                {
                    request.setAttribute("urlPestanaDatos_oferta", url);
                }

                url = cargarSubpestanaJustificacionHist_DatosCpe(codOrganizacion, numExpediente, codTra, adapt, request);
                if(url != null)
                {
                    request.setAttribute("urlPestanaDatos_justif", url);
                }
            }
        }
        catch(Exception ex)
        {

        }
        return "/jsp/extension/melanbide59/datosCepHist.jsp";
     }
    
    public String refrescarDatosCpe(int codOrganizacion, int codTramite, int ocurrenciaTramite, String numExpediente)
    {
        return "0";
    }
     
    /**
     * OperaciÃ³n que recupera los datos de conexiÃ³n a la BBDD
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
                // ConexiÃ³n al esquema genÃ©rico
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
                te.printStackTrace();
                log.error("*** AdaptadorSQLBD: " + te.toString());
            }catch(SQLException e){
                e.printStackTrace();
            }finally{
                try{
                    if(st!=null) st.close();
                    if(rs!=null) rs.close();
                    if(conGenerico!=null && !conGenerico.isClosed())
                    conGenerico.close();
                }catch(SQLException e){
                    e.printStackTrace();
                }//try-catch
            }// finally
            if(log.isDebugEnabled()) log.debug("getConnection() : END");
         }// synchronized
        return adapt;
     }//getConnection
}

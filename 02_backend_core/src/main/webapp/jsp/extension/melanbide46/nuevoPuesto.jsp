<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>

<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide46.i18n.MeLanbide46I18n" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide46.vo.CmePuestoVO" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide46.util.ConstantesMeLanbide46" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide46.vo.SelectItem"%>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.agora.business.util.GeneralValueObject"%>
<%@page import="es.altia.common.service.config.Config"%>
<%@page import="es.altia.common.service.config.ConfigServiceHelper"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Date"%>
<%@page import="java.util.Vector"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="org.apache.commons.lang.StringEscapeUtils"%>
<!doctype html>
<html>
    <head> 
	<meta http-equiv="X-UA-Compatible" content="IE=edge" />
        <%
            int idiomaUsuario = 1;
 int apl = 5;
 String css = "";
            UsuarioValueObject usuario = new UsuarioValueObject();
            try
            {
                if (session != null) 
                {
                    if (usuario != null) 
                    {
                        usuario = (UsuarioValueObject) session.getAttribute("usuario");
                        idiomaUsuario = usuario.getIdioma();
	 apl = usuario.getAppCod();
	 css = usuario.getCss();
                    }
                }
            }
            catch(Exception ex)
            {

            }
            //Clase para internacionalizar los mensajes de la aplicación.
            MeLanbide46I18n meLanbide46I18n = MeLanbide46I18n.getInstance();

            Config m_Config = ConfigServiceHelper.getConfig("common");
            String statusBar = m_Config.getString("JSP.StatusBar");
            String nombreModulo     = request.getParameter("nombreModulo");
            String codOrganizacion  = request.getParameter("codOrganizacionModulo");
            String numExpediente    = request.getParameter("numero");
            
            CmePuestoVO puestoModif = (CmePuestoVO)request.getAttribute("puestoModif");
            String descTit1 = (String)request.getAttribute("descTit1");
            String descTit2 = (String)request.getAttribute("descTit2");
            String descTit3 = (String)request.getAttribute("descTit3");
            Boolean consulta = request.getAttribute("consulta") != null ? (Boolean)request.getAttribute("consulta") : false;
    
    
            String tituloPagina = "";
            if(consulta)
            {
                tituloPagina = meLanbide46I18n.getMensaje(idiomaUsuario,"label.puesto.titulo.consultaPuesto");
            }
            else
            {
                if(puestoModif != null)
                {
                    tituloPagina = meLanbide46I18n.getMensaje(idiomaUsuario,"label.puesto.titulo.modifPuesto");
                }
                else
                {
                    tituloPagina = meLanbide46I18n.getMensaje(idiomaUsuario, "label.puesto.titulo.nuevoPuesto");
                }
            }
            
    
            //COMBOS

            // Paises
            List<SelectItem> listaPaises = new ArrayList<SelectItem>();
            if(request.getAttribute("listaPaises") != null)
                listaPaises = (List<SelectItem>)request.getAttribute("listaPaises");

            // Idiomas
            List<SelectItem> listaIdiomas = new ArrayList<SelectItem>();
            if(request.getAttribute("listaIdiomas") != null)
                listaIdiomas = (List<SelectItem>)request.getAttribute("listaIdiomas");

            // Nivel idiomas
            List<SelectItem> listaNivelIdiomas = new ArrayList<SelectItem>();
            if(request.getAttribute("listaNivelIdiomas") != null)
                listaNivelIdiomas = (List<SelectItem>)request.getAttribute("listaNivelIdiomas");

            // Nivel formativo
            List<SelectItem> listaNivelFormativo = new ArrayList<SelectItem>();
            if(request.getAttribute("listaNivelFormativo") != null)
                listaNivelFormativo = (List<SelectItem>)request.getAttribute("listaNivelFormativo");

            // Grupo cotizacion
            List<SelectItem> listaGrupoCotizacion = new ArrayList<SelectItem>();
            if(request.getAttribute("listaGrupoCotizacion") != null)
                listaGrupoCotizacion = (List<SelectItem>)request.getAttribute("listaGrupoCotizacion");

            // Resultado
            List<SelectItem> listaResultado = new ArrayList<SelectItem>();
            if(request.getAttribute("listaResultado") != null)
                listaResultado = (List<SelectItem>)request.getAttribute("listaResultado");

            // Motivo
            List<SelectItem> listaMotivos = new ArrayList<SelectItem>();
            if(request.getAttribute("listaMotivos") != null)
                listaMotivos = (List<SelectItem>)request.getAttribute("listaMotivos");


            
            String lcodPais = "";
            String ldescPais = "";
            
            String lcodIdioma = "";
            String ldescIdioma = "";
            
            String lcodNivelIdioma = "";
            String ldescNivelIdioma = "";
            
            String lcodNivelFormativo = "";
            String ldescNivelFormativo = "";
            
            String lcodGrupoCot = "";
            String ldescGrupoCot = "";
            
            String lcodResultado = "";
            String ldescResultado = "";
            
            String lcodMotivo = "";
            String ldescMotivo = "";


            if (listaPaises != null && listaPaises.size() > 0) 
            {
                int i;
                SelectItem pais = null;
                for (i = 0; i < listaPaises.size() - 1; i++) 
                {
                    pais = (SelectItem) listaPaises.get(i);
                    lcodPais += "\"" + pais.getCodigo() + "\",";
                    ldescPais += "\"" + escape(pais.getDescripcion()) + "\",";
                }
                pais = (SelectItem) listaPaises.get(i);
                lcodPais += "\"" + pais.getCodigo() + "\"";
                ldescPais += "\"" + escape(pais.getDescripcion()) + "\"";
            }


            if (listaIdiomas != null && listaIdiomas.size() > 0) 
            {
                int i;
                SelectItem idi = null;
                for (i = 0; i < listaIdiomas.size() - 1; i++) 
                {
                    idi = (SelectItem) listaIdiomas.get(i);
                    lcodIdioma += "\"" + idi.getCodigo() + "\",";
                    ldescIdioma += "\"" + escape(idi.getDescripcion()) + "\",";
                }
                idi = (SelectItem) listaIdiomas.get(i);
                lcodIdioma += "\"" + idi.getCodigo() + "\"";
                ldescIdioma += "\"" + escape(idi.getDescripcion()) + "\"";
            }

            if (listaNivelFormativo != null && listaNivelFormativo.size() > 0) 
            {
                int i;
                SelectItem nf = null;
                for (i = 0; i < listaNivelFormativo.size() - 1; i++) 
                {
                    nf = (SelectItem) listaNivelFormativo.get(i);
                    lcodNivelFormativo += "\"" + nf.getCodigo() + "\",";
                    ldescNivelFormativo += "\"" + escape(nf.getDescripcion()) + "\",";
                }
                nf = (SelectItem) listaNivelFormativo.get(i);
                lcodNivelFormativo += "\"" + nf.getCodigo() + "\"";
                ldescNivelFormativo += "\"" + escape(nf.getDescripcion()) + "\"";
            }
                
            if (listaNivelIdiomas != null && listaNivelIdiomas.size() > 0) 
            {
                int i;
                SelectItem ni = null;
                for (i = 0; i < listaNivelIdiomas.size() - 1; i++) 
                {
                    ni = (SelectItem) listaNivelIdiomas.get(i);
                    lcodNivelIdioma += "\"" + ni.getCodigo() + "\",";
                    ldescNivelIdioma += "\"" + escape(ni.getDescripcion()) + "\",";
                }
                ni = (SelectItem) listaNivelIdiomas.get(i);
                lcodNivelIdioma += "\"" + ni.getCodigo() + "\"";
                ldescNivelIdioma += "\"" + escape(ni.getDescripcion()) + "\"";
            }


            if (listaGrupoCotizacion != null && listaGrupoCotizacion.size() > 0) 
            {
                int i;
                SelectItem gc = null;
                for (i = 0; i < listaGrupoCotizacion.size() - 1; i++) 
                {
                    gc = (SelectItem) listaGrupoCotizacion.get(i);
                    lcodGrupoCot += "\"" + gc.getCodigo() + "\",";
                    ldescGrupoCot += "\"" + escape(gc.getDescripcion()) + "\",";
                }
                gc = (SelectItem) listaGrupoCotizacion.get(i);
                lcodGrupoCot += "\"" + gc.getCodigo() + "\"";
                ldescGrupoCot += "\"" + escape(gc.getDescripcion()) + "\"";
            }


            if (listaResultado != null && listaResultado.size() > 0) 
            {
                int i;
                SelectItem res = null;
                for (i = 0; i < listaResultado.size() - 1; i++) 
                {
                    res = (SelectItem) listaResultado.get(i);
                    lcodResultado += "\"" + res.getCodigo() + "\",";
                    ldescResultado += "\"" + escape(res.getDescripcion()) + "\",";
                }
                res = (SelectItem) listaResultado.get(i);
                lcodResultado += "\"" + res.getCodigo() + "\"";
                ldescResultado += "\"" + escape(res.getDescripcion()) + "\"";
            }


            if (listaMotivos != null && listaMotivos.size() > 0) 
            {
                int i;
                SelectItem mot = null;
                for (i = 0; i < listaMotivos.size() - 1; i++) 
                {
                    mot = (SelectItem) listaMotivos.get(i);
                    lcodMotivo += "\"" + mot.getCodigo() + "\",";
                    ldescMotivo += "\"" + escape(mot.getDescripcion()) + "\",";
                }
                mot = (SelectItem) listaMotivos.get(i);
                lcodMotivo += "\"" + mot.getCodigo() + "\"";
                ldescMotivo += "\"" + escape(mot.getDescripcion()) + "\"";
            }
        %>

        <%!
            // Funcion para escapar strings para javascript
            private String escape(String str) 
            {
                return StringEscapeUtils.escapeJavaScript(str);
            }
        %>
        <title><%=tituloPagina%></title>

        
        <jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
<jsp:setProperty name="descriptor"  property="idi_cod" value="<%=idiomaUsuario%>" />
<jsp:setProperty name="descriptor"  property="apl_cod" value="<%=apl%>" />
<link rel="StyleSheet" media="screen" type="text/css" href="<%=request.getContextPath()%><%=css%>">
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/css/font-awesome.min.css'/>" media="screen">
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/less/animated.less'/>" media="screen">
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/>
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/extension/melanbide46/melanbide46.css"/>
        
        <script type="text/javascript" src="<c:url value='/scripts/general.js'/>"></script>
        <script type="text/javascript">
            var APP_CONTEXT_PATH = '<%=request.getContextPath()%>';
        </script>
        <script type="text/javascript" src="<c:url value='/scripts/calendario.js'/>"></script>
        <script type="text/javascript" src="<c:url value='/scripts/validaciones.js'/>"></script>
        <script type="text/javascript" src="<c:url value='/scripts/listaComboBox.js'/>"></script>
        <script type="text/javascript" src="<c:url value='/scripts/popup.js'/>"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/melanbide46/cmeUtils.js"></script>
        
        <script type="text/javascript">
            var mensajeValidacion = '';
            var nuevo = true;
    
            //LISTAS DE VALORES PARA LOS COMBOS
            var codPais = [<%=lcodPais%>];
            var descPais = [<%=ldescPais%>];
            
            var codIdioma = [<%=lcodIdioma%>];
            var descIdioma = [<%=ldescIdioma%>];
            
            var codNivelIdioma = [<%=lcodNivelIdioma%>];
            var descNivelIdioma = [<%=ldescNivelIdioma%>];
            
            var codNivelFormativo = [<%=lcodNivelFormativo%>];
            var descNivelFormativo = [<%=ldescNivelFormativo%>];
            
            var codGrupoCot = [<%=lcodGrupoCot%>];
            var descGrupoCot = [<%=ldescGrupoCot%>];
            
            var codResultado = [<%=lcodResultado%>];
            var descResultado = [<%=ldescResultado%>];
            
            var codMotivo = [<%=lcodMotivo%>];
            var descMotivo = [<%=ldescMotivo%>];
            
            function getXMLHttpRequest(){
                var aVersions = [ "MSXML2.XMLHttp.5.0",
                    "MSXML2.XMLHttp.4.0","MSXML2.XMLHttp.3.0",
                    "MSXML2.XMLHttp","Microsoft.XMLHttp"
                    ];

                if (window.XMLHttpRequest){
                    // para IE7, Mozilla, Safari, etc: que usen el objeto nativo
                    return new XMLHttpRequest();
                }else if (window.ActiveXObject){
                    // de lo contrario utilizar el control ActiveX para IE5.x y IE6.x
                    for (var i = 0; i < aVersions.length; i++) {
                        try {
                            var oXmlHttp = new ActiveXObject(aVersions[i]);
                            return oXmlHttp;
                        }catch (error) {
                        //no necesitamos hacer nada especial
                        }
                    }
                }else{
                    return null;
                }
            }
            
            function inicio(){
                cargarDescripcionesCombos();
                
                <%
                if(puestoModif != null)
                {
                    SimpleDateFormat format = new SimpleDateFormat(ConstantesMeLanbide46.FORMATO_FECHA);
                %>
                        //Los textarea se inicializan directamente en la etiqueta, para evitar problemas con los saltos de linea
                    nuevo = false;
                    document.getElementById('codPuesto').value = '<%=puestoModif.getCodPuesto() != null ? puestoModif.getCodPuesto().toUpperCase() : "" %>';
                    document.getElementById('descPuesto').value = '<%=puestoModif.getDescPuesto() != null ? puestoModif.getDescPuesto().toUpperCase() : "" %>';
                    document.getElementById('codPaisPuesto1').value = '<%=puestoModif.getPaiCod1() != null ? puestoModif.getPaiCod1().toString() : "" %>';
                    document.getElementById('codPaisPuesto2').value = '<%=puestoModif.getPaiCod2() != null ? puestoModif.getPaiCod2().toString() : "" %>';
                    document.getElementById('codPaisPuesto3').value = '<%=puestoModif.getPaiCod3() != null ? puestoModif.getPaiCod3().toString() : "" %>';
                    document.getElementById('ciudadDestino').value = '<%=puestoModif.getCiudadDestino() != null ? puestoModif.getCiudadDestino() : "" %>';
                    document.getElementById('dpto').value = '<%=puestoModif.getDpto() != null ? puestoModif.getDpto() : "" %>';
                    
                    
                    document.getElementById('codTitulacionPuesto1').value = '<%=puestoModif.getCodTit1() != null ? puestoModif.getCodTit1() : ""%>';
                    document.getElementById('descTitulacionPuesto1').value = '<%=descTit1 != null ? descTit1.toUpperCase() : "" %>';
                    document.getElementById('codTitulacionPuesto2').value = '<%=puestoModif.getCodTit2() != null ? puestoModif.getCodTit2() : ""%>';
                    document.getElementById('descTitulacionPuesto2').value = '<%=descTit2 != null ? descTit2.toUpperCase() : "" %>';
                    document.getElementById('codTitulacionPuesto3').value = '<%=puestoModif.getCodTit3() != null ? puestoModif.getCodTit3() : ""%>';
                    document.getElementById('descTitulacionPuesto3').value = '<%=descTit3 != null ? descTit3.toUpperCase() : "" %>';
                    document.getElementById('codIdiomaPuesto1').value = '<%=puestoModif.getCodIdioma1() != null ? puestoModif.getCodIdioma1().toUpperCase() : "" %>';
                    document.getElementById('codNivelIdiomaPuesto1').value = '<%=puestoModif.getCodNivIdi1() != null ? puestoModif.getCodNivIdi1().toUpperCase() : "" %>';
                    document.getElementById('codIdiomaPuesto2').value = '<%=puestoModif.getCodIdioma2() != null ? puestoModif.getCodIdioma2().toUpperCase() : "" %>';
                    document.getElementById('codNivelIdiomaPuesto2').value = '<%=puestoModif.getCodNivIdi2() != null ? puestoModif.getCodNivIdi2().toUpperCase() : "" %>';
                    document.getElementById('codIdiomaPuesto3').value = '<%=puestoModif.getCodIdioma3() != null ? puestoModif.getCodIdioma3().toUpperCase() : "" %>';
                    document.getElementById('codNivelIdiomaPuesto3').value = '<%=puestoModif.getCodNivIdi3() != null ? puestoModif.getCodNivIdi3().toUpperCase() : "" %>';
                    document.getElementById('codNivelFormativo').value = '<%=puestoModif.getCodNivForm() != null ? puestoModif.getCodNivForm().toUpperCase() : "" %>';
                    
                    var rangoEdad = '<%=puestoModif.getRangoEdadPrevisto() != null ? puestoModif.getRangoEdadPrevisto() : "" %>';
                    
                    if (rangoEdad == '35') {
                        document.getElementById('radioRangoEdadPrevisto1').checked = true;
                    } else if (rangoEdad == '45') {
                        document.getElementById('radioRangoEdadPrevisto2').checked = true;
                    }
                    
                    document.getElementById('txtTotSalarioSolicitables').value = '<%=puestoModif.getSalarioAnexTot() != null ? puestoModif.getSalarioAnexTot().toPlainString() : "" %>';
                    document.getElementById('txtDietasSolicitables').value = '<%=puestoModif.getDietasManut() != null ? puestoModif.getDietasManut().toPlainString() : "" %>';
                    document.getElementById('txtMesesSolicitables').value = '<%=puestoModif.getMesesManut() != null ? puestoModif.getMesesManut().toString() : "" %>';
                    document.getElementById('txtSegurosSolicitables').value = '<%=puestoModif.getTramSeguros() != null ? puestoModif.getTramSeguros().toPlainString() : "" %>';
                    document.getElementById('txtImporteSolicitables').value = '<%=puestoModif.getImpMaxSuv() != null ? puestoModif.getImpMaxSuv().toPlainString() : "" %>';
                    document.getElementById('fechaIniContrato').value = '<%=puestoModif.getFecIni() != null ? format.format(puestoModif.getFecIni()) : "" %>';
                    document.getElementById('fechaFinContrato').value = '<%=puestoModif.getFecFin() != null ? format.format(puestoModif.getFecFin()) : "" %>';
                    document.getElementById('txtMesesContratado').value = '<%=puestoModif.getMesesContrato() != null ? puestoModif.getMesesContrato().toString() : "" %>';
                    document.getElementById('codGrupoContrato').value = '<%=puestoModif.getCodGrCot() != null ? puestoModif.getCodGrCot().toUpperCase() : "" %>';
                    document.getElementById('txtSalarioContrato').value = '<%=puestoModif.getSalBruto() != null ? puestoModif.getSalBruto().toPlainString() : "" %>';
                    document.getElementById('txtDietasContrato').value = '<%=puestoModif.getDietasConv() != null ? puestoModif.getDietasConv().toPlainString() : "" %>';
                    document.getElementById('txtCosteContrato').value = '<%=puestoModif.getCosteCont() != null ? puestoModif.getCosteCont().toPlainString() : "" %>';
                    document.getElementById('codResultadoResumen').value = '<%=puestoModif.getCodResult() != null ? puestoModif.getCodResult().toUpperCase() : "" %>';
                    document.getElementById('codMotivoResumen').value = '<%=puestoModif.getCodMotivo() != null ? puestoModif.getCodMotivo().toUpperCase() : "" %>';
                    
                    //Datos solicitud
                    document.getElementById('txtSalarioSolicitud').value = '<%=puestoModif.getSalSolic() != null ? puestoModif.getSalSolic().toPlainString() : "" %>';
                    document.getElementById('txtDietasSolicitud').value = '<%=puestoModif.getDietasSolic() != null ? puestoModif.getDietasSolic().toPlainString() : "" %>';
                    document.getElementById('txtTramitacionSolicitud').value = '<%=puestoModif.getTramSolic() != null ? puestoModif.getTramSolic().toPlainString() : "" %>';
                    document.getElementById('txtImpTotalSolicitud').value = '<%=puestoModif.getImpTotSolic() != null ? puestoModif.getImpTotSolic().toPlainString() : "" %>';
                <%
                }
                else
                {
                %>
                    //document.getElementById('codResultadoResumen').value = '<%=ConstantesMeLanbide46.CODIGO_RESULTADO_NO_EVALUADO%>';
					document.getElementById('codResultadoResumen').value = '';
                <%
                }
                %>
                cambioResultado();    
                        
                reemplazarPuntosCme(document.getElementById('txtTotSalarioSolicitables'));
                reemplazarPuntosCme(document.getElementById('txtMesesSolicitables'));
                reemplazarPuntosCme(document.getElementById('txtSegurosSolicitables'));
                reemplazarPuntosCme(document.getElementById('txtImporteSolicitables'));
                reemplazarPuntosCme(document.getElementById('txtSalarioContrato'));
                reemplazarPuntosCme(document.getElementById('txtDietasContrato'));
                reemplazarPuntosCme(document.getElementById('txtCosteContrato'));
                reemplazarPuntosCme(document.getElementById('txtDietasSolicitables'));
                
                reemplazarPuntosCme(document.getElementById('txtSalarioSolicitud'));
                reemplazarPuntosCme(document.getElementById('txtDietasSolicitud'));
                reemplazarPuntosCme(document.getElementById('txtTramitacionSolicitud'));
                reemplazarPuntosCme(document.getElementById('txtImpTotalSolicitud'));
                
                <%
                if(consulta == true)
                {
                %>
                    //Deshabilito todos los campos
                    
                    //Datos del puesto
                    document.getElementById('codPuesto').readOnly = true;
                    document.getElementById('codPuesto').className = 'inputTexto readOnly';
                    document.getElementById('descPuesto').readOnly = true;
                    document.getElementById('descPuesto').className = 'inputTexto readOnly';
                    document.getElementById('codPaisPuesto1').readOnly = true;
                    document.getElementById('codPaisPuesto1').className = 'inputTexto readOnly';
                    document.getElementById('descPaisPuesto1').readOnly = true;
                    document.getElementById('descPaisPuesto1').className = 'inputTexto readOnly';
                    document.getElementById('anchorPaisPuesto1').style.display = 'none';
                    document.getElementById('codPaisPuesto2').readOnly = true;
                    document.getElementById('codPaisPuesto2').className = 'inputTexto readOnly';
                    document.getElementById('descPaisPuesto2').readOnly = true;
                    document.getElementById('descPaisPuesto2').className = 'inputTexto readOnly';
                    document.getElementById('anchorPaisPuesto2').style.display = 'none';
                    document.getElementById('codPaisPuesto3').readOnly = true;
                    document.getElementById('codPaisPuesto3').className = 'inputTexto readOnly';
                    document.getElementById('descPaisPuesto3').readOnly = true;
                    document.getElementById('descPaisPuesto3').className = 'inputTexto readOnly';
                    document.getElementById('anchorPaisPuesto3').style.display = 'none';
                    document.getElementById('descTitulacionPuesto1').readOnly = true;
                    document.getElementById('descTitulacionPuesto1').className = 'inputTexto readOnly';
                    document.getElementById('descTitulacionPuesto2').readOnly = true;
                    document.getElementById('descTitulacionPuesto2').className = 'inputTexto readOnly';
                    document.getElementById('descTitulacionPuesto3').readOnly = true;
                    document.getElementById('descTitulacionPuesto3').className = 'inputTexto readOnly';
                    document.getElementById('funcionesPuesto').readOnly = true;
                    document.getElementById('funcionesPuesto').className = 'inputTexto readOnly';
                    document.getElementById('codIdiomaPuesto1').readOnly = true;
                    document.getElementById('codIdiomaPuesto1').className = 'inputTexto readOnly';
                    document.getElementById('descIdiomaPuesto1').readOnly = true;
                    document.getElementById('descIdiomaPuesto1').className = 'inputTexto readOnly';
                    document.getElementById('anchorIdiomaPuesto1').style.display = 'none';
                    document.getElementById('codIdiomaPuesto2').readOnly = true;
                    document.getElementById('codIdiomaPuesto2').className = 'inputTexto readOnly';
                    document.getElementById('descIdiomaPuesto2').readOnly = true;
                    document.getElementById('descIdiomaPuesto2').className = 'inputTexto readOnly';
                    document.getElementById('anchorIdiomaPuesto2').style.display = 'none';
                    document.getElementById('codIdiomaPuesto3').readOnly = true;
                    document.getElementById('codIdiomaPuesto3').className = 'inputTexto readOnly';
                    document.getElementById('descIdiomaPuesto3').readOnly = true;
                    document.getElementById('descIdiomaPuesto3').className = 'inputTexto readOnly';
                    document.getElementById('anchorIdiomaPuesto3').style.display = 'none';
                    document.getElementById('codNivelIdiomaPuesto1').readOnly = true;
                    document.getElementById('codNivelIdiomaPuesto1').className = 'inputTexto readOnly';
                    document.getElementById('descNivelIdiomaPuesto1').readOnly = true;
                    document.getElementById('descNivelIdiomaPuesto1').className = 'inputTexto readOnly';
                    document.getElementById('anchorNivelIdiomaPuesto1').style.display = 'none';
                    document.getElementById('codNivelIdiomaPuesto2').readOnly = true;
                    document.getElementById('codNivelIdiomaPuesto2').className = 'inputTexto readOnly';
                    document.getElementById('descNivelIdiomaPuesto2').readOnly = true;
                    document.getElementById('descNivelIdiomaPuesto2').className = 'inputTexto readOnly';
                    document.getElementById('anchorNivelIdiomaPuesto2').style.display = 'none';
                    document.getElementById('codNivelIdiomaPuesto3').readOnly = true;
                    document.getElementById('codNivelIdiomaPuesto3').className = 'inputTexto readOnly';
                    document.getElementById('descNivelIdiomaPuesto3').readOnly = true;
                    document.getElementById('descNivelIdiomaPuesto3').className = 'inputTexto readOnly';
                    document.getElementById('anchorNivelIdiomaPuesto3').style.display = 'none';
                    document.getElementById('codNivelFormativo').readOnly = true;
                    document.getElementById('codNivelFormativo').className = 'inputTexto readOnly';
                    document.getElementById('descNivelFormativo').readOnly = true;
                    document.getElementById('descNivelFormativo').className = 'inputTexto readOnly';
                    document.getElementById('anchorNivelFormativo').style.display = 'none';
                    document.getElementById('ciudadDestino').readOnly = true;
                    document.getElementById('ciudadDestino').className = 'inputTexto readOnly';
                    document.getElementById('dpto').readOnly = true;
                    document.getElementById('dpto').className = 'inputTexto readOnly';
                    
                    //Datos solicitables
                    document.getElementById('txtMesesSolicitables').readOnly = true;
                    document.getElementById('txtMesesSolicitables').className = 'inputTexto readOnly textoNumerico';
                    document.getElementById('txtSegurosSolicitables').readOnly = true;
                    document.getElementById('txtSegurosSolicitables').className = 'inputTexto readOnly textoNumerico';
                    
                    //Datos contrato
                    document.getElementById('fechaIniContrato').readOnly = true;
                    document.getElementById('fechaIniContrato').className = 'inputTexto readOnly';
                    document.getElementById('calfechaIniContrato').style.display = 'none';
                    document.getElementById('fechaFinContrato').readOnly = true;
                    document.getElementById('fechaFinContrato').className = 'inputTexto readOnly';
                    document.getElementById('calfechaFinContrato').style.display = 'none';
                    document.getElementById('codGrupoContrato').readOnly = true;
                    document.getElementById('codGrupoContrato').className = 'inputTexto readOnly';
                    document.getElementById('descGrupoContrato').readOnly = true;
                    document.getElementById('descGrupoContrato').className = 'inputTexto readOnly';
                    document.getElementById('anchorGrupoContrato').style.display = 'none';
                    document.getElementById('txtConvenioContrato').readOnly = true;
                    document.getElementById('txtConvenioContrato').className = 'inputTexto readOnly';
                    document.getElementById('txtSalarioContrato').readOnly = true;
                    document.getElementById('txtSalarioContrato').className = 'inputTexto readOnly textoNumerico';
                    document.getElementById('txtDietasContrato').readOnly = true;
                    document.getElementById('txtDietasContrato').className = 'inputTexto readOnly textoNumerico';
                    document.getElementById('otrosBeneficiosContrato').readOnly = true;
                    document.getElementById('otrosBeneficiosContrato').className = 'inputTexto readOnly';
                    document.getElementById('codResultadoResumen').readOnly = true;
                    document.getElementById('codResultadoResumen').className = 'inputTexto readOnly';
                    document.getElementById('descResultadoResumen').readOnly = true;
                    document.getElementById('descResultadoResumen').className = 'inputTexto readOnly';
                    document.getElementById('anchorResultadoResumen').style.display = 'none';
                    document.getElementById('codMotivoResumen').readOnly = true;
                    document.getElementById('codMotivoResumen').className = 'inputTexto readOnly';
                    document.getElementById('descMotivoResumen').readOnly = true;
                    document.getElementById('descMotivoResumen').className = 'inputTexto readOnly';
                    document.getElementById('anchorMotivoResumen').style.display = 'none';
                    document.getElementById('observacionesResumen').readOnly = true;
                    document.getElementById('observacionesResumen').className = 'inputTexto readOnly';
                    document.getElementById('txtMesesContratado').readOnly = true;
                    document.getElementById('txtMesesContratado').className = 'inputTexto readOnly textoNumerico';

                    document.getElementById('btnGuardarContrato').style.display = 'none';
                    document.getElementById('btnCancelarContrato').style.display = 'none';
                    document.getElementById('btnCerrar').style.display = 'inline';
                    
                    //Datos solicitud
                    
                    document.getElementById('txtSalarioSolicitud').readOnly = true;
                    document.getElementById('txtSalarioSolicitud').className = 'inputTexto readOnly textoNumerico';
                    document.getElementById('txtDietasSolicitud').readOnly = true;
                    document.getElementById('txtDietasSolicitud').className = 'inputTexto readOnly textoNumerico';
                    document.getElementById('txtTramitacionSolicitud').readOnly = true;
                    document.getElementById('txtTramitacionSolicitud').className = 'inputTexto readOnly textoNumerico';
                <%
                }
                else
                {
                %>  
                        
                    comboPaisPuesto1 = new Combo('PaisPuesto1');
                    comboPaisPuesto1.change = cambioPais1;

                    comboPaisPuesto2 = new Combo('PaisPuesto2');
                    comboPaisPuesto2.change = cambioPais2;
                    
                    comboPaisPuesto3 = new Combo('PaisPuesto3');
                    comboPaisPuesto3.change = cambioPais3;
                    
                    comboIdiomaPuesto1 = new Combo('IdiomaPuesto1');
                    comboNivelIdiomaPuesto1 = new Combo('NivelIdiomaPuesto1');
                    comboIdiomaPuesto2 = new Combo('IdiomaPuesto2');
                    comboNivelIdiomaPuesto2 = new Combo('NivelIdiomaPuesto2');
                    comboIdiomaPuesto3 = new Combo('IdiomaPuesto3');
                    comboNivelIdiomaPuesto3 = new Combo('NivelIdiomaPuesto3');
                    comboNivelFormativo = new Combo('NivelFormativo');
                    comboNivelFormativo.change = cambioNivelFormativo;

                    //Combos datos contrato
                    comboGrupoContrato = new Combo('GrupoContrato');
                    //comboGrupoContrato.change = cambioGrupoCot;

                    //Combos resumen puesto
                    comboResultadoResumen = new Combo('ResultadoResumen');
                    comboResultadoResumen.change = cambioResultado;
                    comboMotivoResumen = new Combo('MotivoResumen');


                    cargarCombos();
                <%
                }
                %>
                        
                
                document.getElementById('txtDietasSolicitables').readOnly = true;
                document.getElementById('txtDietasSolicitables').className = 'inputTexto readOnly';
                document.getElementById('txtImporteSolicitables').readOnly = true;
                document.getElementById('txtImporteSolicitables').className = 'inputTexto readOnly';
                document.getElementById('codPuesto').readOnly = true;
                document.getElementById('codPuesto').className = 'inputTexto readOnly';
                document.getElementById('txtCosteContrato').readOnly = true;
                document.getElementById('txtCosteContrato').className = 'inputTexto readOnly textoNumerico';
                    
                ajustarDecimalesImportes();
                resizeForFF();
            }
            
            function cargarCombos(){
                comboPaisPuesto1.addItems(codPais, descPais);
                comboPaisPuesto2.addItems(codPais, descPais);
                comboPaisPuesto3.addItems(codPais, descPais);
                comboIdiomaPuesto1.addItems(codIdioma, descIdioma);
                comboNivelIdiomaPuesto1.addItems(codNivelIdioma, descNivelIdioma);
                comboIdiomaPuesto2.addItems(codIdioma, descIdioma);
                comboNivelIdiomaPuesto2.addItems(codNivelIdioma, descNivelIdioma);
                comboIdiomaPuesto3.addItems(codIdioma, descIdioma);
                comboNivelIdiomaPuesto3.addItems(codNivelIdioma, descNivelIdioma);
                comboNivelFormativo.addItems(codNivelFormativo, descNivelFormativo);
                comboGrupoContrato.addItems(codGrupoCot, descGrupoCot);
                comboResultadoResumen.addItems(codResultado, descResultado);
                comboMotivoResumen.addItems(codMotivo, descMotivo);
            }
        
            function cargarDescripcionesCombos(){
                var desc = "";
                var codAct = "";
                var codigo = "";

                //Pais 1
                codigo = '<%=puestoModif != null && puestoModif.getPaiCod1() != null ? puestoModif.getPaiCod1() : ""%>';
                desc = '';
                var encontrado = false;
                var i = 0; 
                if(codigo != null && codigo != '')
                {
                    while(i<codPais.length && !encontrado)
                    {
                        codAct = codPais[i];
                        if(codAct == codigo)
                        {
                            encontrado = true;
                            desc = descPais[i];
                        }else{
                            i++;
                        }
                    }
                }
                document.getElementById('descPaisPuesto1').value = desc;

                //Pais 2
                codigo = '<%=puestoModif != null && puestoModif.getPaiCod2() != null ? puestoModif.getPaiCod2() : ""%>';
                desc = '';
                encontrado = false;
                i = 0; 
                if(codigo != null && codigo != '')
                {
                    while(i<codPais.length && !encontrado)
                    {
                        codAct = codPais[i];
                        if(codAct == codigo)
                        {
                            encontrado = true;
                            desc = descPais[i];
                        }else{
                            i++;
                        }
                    }
                }
                document.getElementById('descPaisPuesto2').value = desc;

                //Pais 3
                codigo = '<%=puestoModif != null && puestoModif.getPaiCod3() != null ? puestoModif.getPaiCod3() : ""%>';
                desc = '';
                encontrado = false;
                i = 0; 
                if(codigo != null && codigo != '')
                {
                    while(i<codPais.length && !encontrado)
                    {
                        codAct = codPais[i];
                        if(codAct == codigo)
                        {
                            encontrado = true;
                            desc = descPais[i];
                        }else{
                            i++;
                        }
                    }
                }
                document.getElementById('descPaisPuesto3').value = desc;

                //Idioma 1
                codigo = '<%=puestoModif != null && puestoModif.getCodIdioma1() != null ? puestoModif.getCodIdioma1() : ""%>';
                desc = '';
                encontrado = false;
                i = 0; 
                if(codigo != null && codigo != '')
                {
                    while(i<codIdioma.length && !encontrado)
                    {
                        codAct = codIdioma[i];
                        if(codAct == codigo)
                        {
                            encontrado = true;
                            desc = descIdioma[i];
                        }else{
                            i++;
                        }
                    }
                }
                document.getElementById('descIdiomaPuesto1').value = desc;

                //Idioma 2
                codigo = '<%=puestoModif != null && puestoModif.getCodIdioma2() != null ? puestoModif.getCodIdioma2() : ""%>';
                desc = '';
                encontrado = false;
                i = 0; 
                if(codigo != null && codigo != '')
                {
                    while(i<codIdioma.length && !encontrado)
                    {
                        codAct = codIdioma[i];
                        if(codAct == codigo)
                        {
                            encontrado = true;
                            desc = descIdioma[i];
                        }else{
                            i++;
                        }
                    }
                }
                document.getElementById('descIdiomaPuesto2').value = desc;

                //Idioma 3
                codigo = '<%=puestoModif != null && puestoModif.getCodIdioma3() != null ? puestoModif.getCodIdioma3() : ""%>';
                desc = '';
                encontrado = false;
                i = 0; 
                if(codigo != null && codigo != '')
                {
                    while(i<codIdioma.length && !encontrado)
                    {
                        codAct = codIdioma[i];
                        if(codAct == codigo)
                        {
                            encontrado = true;
                            desc = descIdioma[i];
                        }else{
                            i++;
                        }
                    }
                }
                document.getElementById('descIdiomaPuesto3').value = desc;

                //Nivel Idioma 1
                codigo = '<%=puestoModif != null && puestoModif.getCodNivIdi1() != null ? puestoModif.getCodNivIdi1() : ""%>';
                desc = '';
                encontrado = false;
                i = 0; 
                if(codigo != null && codigo != '')
                {
                    while(i<codNivelIdioma.length && !encontrado)
                    {
                        codAct = codNivelIdioma[i];
                        if(codAct == codigo)
                        {
                            encontrado = true;
                            desc = descNivelIdioma[i];
                        }else{
                            i++;
                        }
                    }
                }
                document.getElementById('descNivelIdiomaPuesto1').value = desc;

                //Nivel Idioma 2
                codigo = '<%=puestoModif != null && puestoModif.getCodNivIdi2() != null ? puestoModif.getCodNivIdi2() : ""%>';
                desc = '';
                encontrado = false;
                i = 0; 
                if(codigo != null && codigo != '')
                {
                    while(i<codNivelIdioma.length && !encontrado)
                    {
                        codAct = codNivelIdioma[i];
                        if(codAct == codigo)
                        {
                            encontrado = true;
                            desc = descNivelIdioma[i];
                        }else{
                            i++;
                        }
                    }
                }
                document.getElementById('descNivelIdiomaPuesto2').value = desc;

                //Nivel Idioma 3
                codigo = '<%=puestoModif != null && puestoModif.getCodNivIdi3() != null ? puestoModif.getCodNivIdi3() : ""%>';
                desc = '';
                encontrado = false;
                i = 0; 
                if(codigo != null && codigo != '')
                {
                    while(i<codNivelIdioma.length && !encontrado)
                    {
                        codAct = codNivelIdioma[i];
                        if(codAct == codigo)
                        {
                            encontrado = true;
                            desc = descNivelIdioma[i];
                        }else{
                            i++;
                        }
                    }
                }
                document.getElementById('descNivelIdiomaPuesto3').value = desc;

                //Nivel Formativo
                codigo = '<%=puestoModif != null && puestoModif.getCodNivForm() != null ? puestoModif.getCodNivForm() : ""%>';
                desc = '';
                encontrado = false;
                i = 0; 
                if(codigo != null && codigo != '')
                {
                    while(i<codNivelFormativo.length && !encontrado)
                    {
                        codAct = codNivelFormativo[i];
                        if(codAct == codigo)
                        {
                            encontrado = true;
                            desc = descNivelFormativo[i];
                        }
                        else
                            {
                                i++;
                            }
                    }
                }
                document.getElementById('descNivelFormativo').value = desc;

                //Grupo Cotizacion
                codigo = '<%=puestoModif != null && puestoModif.getCodGrCot() != null ? puestoModif.getCodGrCot() : ""%>';
                desc = '';
                encontrado = false;
                i = 0; 
                if(codigo != null && codigo != '')
                {
                    while(i<codGrupoCot.length && !encontrado)
                    {
                        codAct = codGrupoCot[i];
                        if(codAct == codigo)
                        {
                            encontrado = true;
                            desc = descGrupoCot[i];
                        }
                        else{
                            i++;
                        }
                    }
                }
                document.getElementById('descGrupoContrato').value = desc;

                //Resultado
                codigo = '<%=puestoModif != null ? puestoModif.getCodResult() != null ? puestoModif.getCodResult() : "" : ""/*ConstantesMeLanbide46.CODIGO_RESULTADO_NO_EVALUADO*/%>';
                desc = '';
                encontrado = false;
                i = 0; 
                if(codigo != null && codigo != '')
                {
                    while(i<codResultado.length && !encontrado)
                    {
                        codAct = codResultado[i];
                        if(codAct == codigo)
                        {
                            encontrado = true;
                            desc = descResultado[i];
                        }else{
                            i++;
                        }
                    }
                }
                document.getElementById('descResultadoResumen').value = desc;

                //Motivo
                codigo = '<%=puestoModif != null && puestoModif.getCodMotivo() != null ? puestoModif.getCodMotivo() : ""%>';
                desc = '';
                encontrado = false;
                i = 0; 
                if(codigo != null && codigo != '')
                {
                    while(i<codMotivo.length && !encontrado)
                    {
                        codAct = codMotivo[i];
                        if(codAct == codigo)
                        {
                            encontrado = true;
                            desc = descMotivo[i];
                        }else{
                            i++;
                        }
                    }
                }
                document.getElementById('descMotivoResumen').value = desc;
            }
            
            function resizeForFF(){
                if(navigator.appName.indexOf("Internet Explorer")==-1){
                    document.getElementById('cuerpoNuevaSolicitud').style.width = '99%';
                    document.getElementById('divCuerpo').style.width = '98%';
                    document.getElementById('fieldsetDatosPuesto').style.width = '98%';
                    document.getElementById('fieldsetDatosSolicitables').style.width = '98%';
                    document.getElementById('fieldsetDatosContrato').style.width = '98%';
                    document.getElementById('fieldsetResumenPuesto').style.width = '98%';
                }
            }
            
            function cerrarVentana(){
                if(navigator.appName=='Microsoft Internet Explorer') { 
                      window.parent.window.opener=null; 
                      window.parent.window.close(); 
                } else if(navigator.appName=="Netscape") { 
                      top.window.opener = top; 
                      top.window.open('','_parent',''); 
                      top.window.close(); 
                 }else{
                     window.close(); 
                 } 
            }
            
            function cancelar(){
                var resultado = jsp_alerta('','<%=meLanbide46I18n.getMensaje(idiomaUsuario, "msg.preguntaCancelar")%>');
                if (resultado == 1){
                    cerrarVentana();
                }
            }


            function mostrarCalFechaEnvioCandidatosOferta(evento) {
                if(window.event) 
                    evento = window.event;
                if (document.getElementById('calfechaEnvioCandidatosOferta').src.indexOf('icono.gif') != -1 )
                    showCalendar('forms[0]','fechaEnvioCandidatosOferta',null,null,null,'','calfechaEnvioCandidatosOferta','',null,null,null,null,null,null,null,null,evento);
            }
            
            function mostrarCalFechaNacContratado(evento) {
                if(window.event) 
                    evento = window.event;
                if (document.getElementById('calfechaNacContratado').src.indexOf('icono.gif') != -1 )
                    showCalendar('forms[0]','fechaNacContratado',null,null,null,'','calfechaNacContratado','',null,null,null,null,null,null,null,null,evento);
            }
            
            function mostrarCalFechaIniContrato(evento) {
                if(window.event) 
                    evento = window.event;
                if (document.getElementById('calfechaIniContrato').src.indexOf('icono.gif') != -1 )
                    showCalendar('forms[0]','fechaIniContrato',null,null,null,'','calfechaIniContrato','',null,null,null,null,null,null,null,'calcularMesesContratado()',evento);
            }
            
            function mostrarCalFechaFinContrato(evento) {
                if(window.event) 
                    evento = window.event;
                if (document.getElementById('calfechaFinContrato').src.indexOf('icono.gif') != -1 )
                    showCalendar('forms[0]','fechaFinContrato',null,null,null,'','calfechaFinContrato','',null,null,null,null,null,null,null,'calcularMesesContratado()',evento);
            }
        
            function cambioValorCheck(check){
                check.blur();
                check.focus();
                if(check.checked){
                    check.value="S";
                }
                else{
                    check.value="N";
                }
            }
            
            function guardar(){
                var resultado=9;
                if(document.getElementById('codResultadoResumen').value=='002' || document.getElementById('codResultadoResumen').value=='003'){
                     resultado= jsp_alerta('', '<%=meLanbide46I18n.getMensaje(idiomaUsuario, "msg.preguntaEliminarCambioRenuncia")%>');  
                }
                if (resultado == 1 || resultado==9){
                    if(validarDatos()){
                        document.getElementById('msgGuardandoDatos').style.display="inline";
                        barraProgresoCme('on', 'barraProgresoNuevoPuesto');
                        var ajax = getXMLHttpRequest();
                        var nodos = null;
                        var CONTEXT_PATH = '<%=request.getContextPath()%>'
                        var url = CONTEXT_PATH + "/PeticionModuloIntegracion.do";
                        var parametros = "";
                        var control = new Date();

                        var rangoEdad = "0";
                        if (document.getElementById('radioRangoEdadPrevisto1').checked) {
                            rangoEdad = document.getElementById('radioRangoEdadPrevisto1').value;
                        } else if (document.getElementById('radioRangoEdadPrevisto2').checked) {
                            rangoEdad = document.getElementById('radioRangoEdadPrevisto2').value;
                        }

                        parametros = 'tarea=preparar&modulo=MELANBIDE46&operacion=guardarPuesto&tipo=0&numero=<%=numExpediente%>'
                            +'&idPuesto=<%=puestoModif != null && puestoModif.getCodPuesto() != null ? puestoModif.getCodPuesto() : ""%>'
                            +'&descPuesto='+escape(document.getElementById('descPuesto').value)
                            +'&ciudadDestino='+escape(document.getElementById('ciudadDestino').value)
                            +'&dpto='+escape(document.getElementById('dpto').value)
                            +'&codPais1='+document.getElementById('codPaisPuesto1').value
                            +'&codPais2='+document.getElementById('codPaisPuesto2').value
                            +'&codPais3='+document.getElementById('codPaisPuesto3').value
                            +'&codTitulacion1='+document.getElementById('codTitulacionPuesto1').value
                            +'&codTitulacion2='+document.getElementById('codTitulacionPuesto2').value
                            +'&codTitulacion3='+document.getElementById('codTitulacionPuesto3').value
                            +'&funciones='+escape(document.getElementById('funcionesPuesto').value)
                            +'&codIdioma1='+document.getElementById('codIdiomaPuesto1').value
                            +'&codIdioma2='+document.getElementById('codIdiomaPuesto2').value
                            +'&codIdioma3='+document.getElementById('codIdiomaPuesto3').value
                            +'&codNivelIdioma1='+document.getElementById('codNivelIdiomaPuesto1').value
                            +'&codNivelIdioma2='+document.getElementById('codNivelIdiomaPuesto2').value
                            +'&codNivelIdioma3='+document.getElementById('codNivelIdiomaPuesto3').value
                            +'&codNivelFormativo='+document.getElementById('codNivelFormativo').value
                            +'&rangoEdadPrevisto='+rangoEdad
                            +'&salarioAnexoTot='+document.getElementById('txtTotSalarioSolicitables').value
                            +'&dietasManutencion='+document.getElementById('txtDietasSolicitables').value
                            +'&mesesManutencion='+document.getElementById('txtMesesSolicitables').value
                            +'&tramSeguros='+document.getElementById('txtSegurosSolicitables').value
                            +'&impSolic='+document.getElementById('txtImporteSolicitables').value
                            +'&fechaIni='+document.getElementById('fechaIniContrato').value
                            +'&fechaFin='+document.getElementById('fechaFinContrato').value
                            +'&mesesContrato='+document.getElementById('txtMesesContratado').value
                            +'&codGrupoCot='+document.getElementById('codGrupoContrato').value
                            +'&convenioCol='+escape(document.getElementById('txtConvenioContrato').value)
                            +'&salarioBruto='+document.getElementById('txtSalarioContrato').value
                            +'&dietasConvenio='+document.getElementById('txtDietasContrato').value
                            +'&costeContrato='+document.getElementById('txtCosteContrato').value
                            +'&otrosBenef='+escape(document.getElementById('otrosBeneficiosContrato').value)
                            +'&codResultado='+document.getElementById('codResultadoResumen').value
                            +'&codMotivo='+document.getElementById('codMotivoResumen').value
                            +'&salarioSolic='+document.getElementById('txtSalarioSolicitud').value
                            +'&dietasSolic='+document.getElementById('txtDietasSolicitud').value
                            +'&tramitacionSolic='+document.getElementById('txtTramitacionSolicitud').value
                            +'&impTotSolic='+document.getElementById('txtImpTotalSolicitud').value
                            +'&observaciones='+escape(document.getElementById('observacionesResumen').value)
                            +'&control='+control.getTime();
                        try{
                            ajax.open("POST",url,false);
                            ajax.setRequestHeader("Content-Type","application/x-www-form-urlencoded; charset=iso-8859-15");
                            ajax.setRequestHeader("Accept", "text/xml, application/xml, text/plain");     
                            //var formData = new FormData(document.getElementById('formContrato'));
                            ajax.send(parametros);
                            if (ajax.readyState==4 && ajax.status==200){
                                var xmlDoc = null;
                                if(navigator.appName.indexOf("Internet Explorer")!=-1){
                                    // En IE el XML viene en responseText y no en la propiedad responseXML
                                    var text = ajax.responseText;
                                    xmlDoc=new ActiveXObject("Microsoft.XMLDOM");
                                    xmlDoc.async="false";
                                    xmlDoc.loadXML(text);
                                }else{
                                    // En el resto de navegadores el XML se recupera de la propiedad responseXML
                                    xmlDoc = ajax.responseXML;
                                }//if(navigator.appName.indexOf("Internet Explorer")!=-1)
                            }//if (ajax.readyState==4 && ajax.status==200)
                            nodos = xmlDoc.getElementsByTagName("RESPUESTA");
                            var elemento = nodos[0];
                        var hijos = elemento.childNodes;
                        var codigoOperacion = null;
                        var listaPuestos = new Array();
                        var fila = new Array();
                        var nodoFila;
                        var hijosFila;
                        var nodoCampo;
                        var filaTitulaciones = new Array();
                        var j;

                        for(j=0;hijos!=null && j<hijos.length;j++){
                            if(hijos[j].nodeName=="CODIGO_OPERACION"){                            
                                codigoOperacion = hijos[j].childNodes[0].nodeValue;
                                listaPuestos[j] = codigoOperacion;
                            }else if(hijos[j].nodeName=="CALCULOS"){      
                                nodoFila = hijos[j];
                                hijosFila = nodoFila.childNodes;
                                for(var cont = 0; cont < hijosFila.length; cont++){
                                    if(hijosFila[cont].nodeName=="IMP_SOL"){
                                        nodoCampo = hijosFila[cont];
                                        if(nodoCampo.childNodes.length > 0){
                                            fila[0] = nodoCampo.childNodes[0].nodeValue;
                                        }
                                        else{
                                            fila[0] = '0';
                                        }
                                    }
                                    else if(hijosFila[cont].nodeName=="IMP_CONV"){
                                        nodoCampo = hijosFila[cont];
                                        if(nodoCampo.childNodes.length > 0){
                                            fila[1] = nodoCampo.childNodes[0].nodeValue;
                                        }
                                        else{
                                            fila[1] = '0';
                                        }
                                    }
                                    else if(hijosFila[cont].nodeName=="IMP_PREV_CON"){
                                        nodoCampo = hijosFila[cont];
                                        if(nodoCampo.childNodes.length > 0){
                                            fila[2] = nodoCampo.childNodes[0].nodeValue;
                                        }
                                        else{
                                            fila[2] = '0';
                                        }
                                    }
                                    else if(hijosFila[cont].nodeName=="IMP_CON"){
                                        nodoCampo = hijosFila[cont];
                                        if(nodoCampo.childNodes.length > 0){
                                            fila[3] = nodoCampo.childNodes[0].nodeValue;
                                        }
                                        else{
                                            fila[3] = '0';
                                        }
                                    }
                                    else if(hijosFila[cont].nodeName=="IMP_JUS"){
                                        nodoCampo = hijosFila[cont];
                                        if(nodoCampo.childNodes.length > 0){
                                            fila[4] = nodoCampo.childNodes[0].nodeValue;
                                        }
                                        else{
                                            fila[4] = '0';
                                        }
                                    }
                                    else if(hijosFila[cont].nodeName=="IMP_REN"){
                                        nodoCampo = hijosFila[cont];
                                        if(nodoCampo.childNodes.length > 0){
                                            fila[5] = nodoCampo.childNodes[0].nodeValue;
                                        }
                                        else{
                                            fila[5] = '0';
                                        }
                                    }
                                    else if(hijosFila[cont].nodeName=="IMP_PAG"){
                                        nodoCampo = hijosFila[cont];
                                        if(nodoCampo.childNodes.length > 0){
                                            fila[6] = nodoCampo.childNodes[0].nodeValue;
                                        }
                                        else{
                                            fila[6] = '0';
                                        }
                                    }
                                    else if(hijosFila[cont].nodeName=="IMP_PAG_2"){
                                        nodoCampo = hijosFila[cont];
                                        if(nodoCampo.childNodes.length > 0){
                                            fila[7] = nodoCampo.childNodes[0].nodeValue;
                                        }
                                        else{
                                            fila[7] = '0';
                                        }
                                    }
                                    else if(hijosFila[cont].nodeName=="IMP_REI"){
                                        nodoCampo = hijosFila[cont];
                                        if(nodoCampo.childNodes.length > 0){
                                            fila[8] = nodoCampo.childNodes[0].nodeValue;
                                        }
                                        else{
                                            fila[8] = '0';
                                        }
                                    }
                                    else if(hijosFila[cont].nodeName=="OTRAS_AYUDAS_SOLIC"){
                                        nodoCampo = hijosFila[cont];
                                        if(nodoCampo.childNodes.length > 0){
                                            fila[9] = nodoCampo.childNodes[0].nodeValue;
                                        }
                                        else{
                                            fila[9] = '0';
                                        }
                                    }
                                    else if(hijosFila[cont].nodeName=="OTRAS_AYUDAS_CONCE"){
                                        nodoCampo = hijosFila[cont];
                                        if(nodoCampo.childNodes.length > 0){
                                            fila[10] = nodoCampo.childNodes[0].nodeValue;
                                        }
                                        else{
                                            fila[10] = '0';
                                        }
                                    }
                                    else if(hijosFila[cont].nodeName=="MINIMIS_SOLIC"){
                                        nodoCampo = hijosFila[cont];
                                        if(nodoCampo.childNodes.length > 0){
                                            fila[11] = nodoCampo.childNodes[0].nodeValue;
                                        }
                                        else{
                                            fila[11] = '0';
                                        }
                                    }
                                    else if(hijosFila[cont].nodeName=="MINIMIS_CONCE"){
                                        nodoCampo = hijosFila[cont];
                                        if(nodoCampo.childNodes.length > 0){
                                            fila[12] = nodoCampo.childNodes[0].nodeValue;
                                        }
                                        else{
                                            fila[12] = '0';
                                        }
                                    }
                                    else if(hijosFila[cont].nodeName=="IMP_DESP"){
                                        nodoCampo = hijosFila[cont];
                                        if(nodoCampo.childNodes.length > 0){
                                            fila[13] = nodoCampo.childNodes[0].nodeValue;
                                        }
                                        else{
                                            fila[13] = '0';
                                        }
                                    }
                                    else if(hijosFila[cont].nodeName=="IMP_BAJA"){
                                        nodoCampo = hijosFila[cont];
                                        if(nodoCampo.childNodes.length > 0){
                                            fila[14] = nodoCampo.childNodes[0].nodeValue;
                                        }
                                        else{
                                            fila[14] = '0';
                                        }
                                    }
                                    else if(hijosFila[cont].nodeName=="CONCEDIDO_REAL"){
                                        nodoCampo = hijosFila[cont];
                                        if(nodoCampo.childNodes.length > 0){
                                            fila[15] = nodoCampo.childNodes[0].nodeValue;
                                        }
                                        else{
                                            fila[15] = '0';
                                        }
                                    }
                                    else if(hijosFila[cont].nodeName=="PAGADO_REAL"){
                                        nodoCampo = hijosFila[cont];
                                        if(nodoCampo.childNodes.length > 0){
                                            fila[16] = nodoCampo.childNodes[0].nodeValue;
                                        }
                                        else{
                                            fila[16] = '0';
                                        }
                                    }
                                    else if(hijosFila[cont].nodeName=="PAGADO_REAL_2"){
                                        nodoCampo = hijosFila[cont];
                                        if(nodoCampo.childNodes.length > 0){
                                            fila[17] = nodoCampo.childNodes[0].nodeValue;
                                        }
                                        else{
                                            fila[17] = '0';
                                        }
                                    }
                                    else if(hijosFila[cont].nodeName=="IMP_NO_JUS"){
                                        nodoCampo = hijosFila[cont];
                                        if(nodoCampo.childNodes.length > 0){
                                            fila[18] = nodoCampo.childNodes[0].nodeValue;
                                        }
                                        else{
                                            fila[18] = '0';
                                        }
                                    }
                                    else if(hijosFila[cont].nodeName=="IMP_REN_RES"){
                                        nodoCampo = hijosFila[cont];
                                        if(nodoCampo.childNodes.length > 0){
                                            fila[19] = nodoCampo.childNodes[0].nodeValue;
                                        }
                                        else{
                                            fila[19] = '0';
                                        }
                                    }
                                    else if(hijosFila[cont].nodeName=="IMP_PAG_RES"){
                                        nodoCampo = hijosFila[cont];
                                        if(nodoCampo.childNodes.length > 0){
                                            fila[20] = nodoCampo.childNodes[0].nodeValue;
                                        }
                                        else{
                                            fila[20] = '0';
                                        }
                                    }
                                }
                                listaPuestos[j] = fila;
                                fila = new Array();
                            }else if(hijos[j].nodeName=="FILA"){
                                nodoFila = hijos[j];
                                hijosFila = nodoFila.childNodes;
                                for(var cont = 0; cont < hijosFila.length; cont++){
                                    if(hijosFila[cont].nodeName=="ID"){
                                        nodoCampo = hijosFila[cont];
                                        if(nodoCampo.childNodes.length > 0){
                                            fila[0] = nodoCampo.childNodes[0].nodeValue;
                                        }
                                        else{
                                            fila[0] = '-';
                                        }
                                    }
                                    else if(hijosFila[cont].nodeName=="DESC_PUESTO"){
                                        nodoCampo = hijosFila[cont];
                                        if(nodoCampo.childNodes.length > 0){
                                            fila[1] = nodoCampo.childNodes[0].nodeValue;
                                        }
                                        else{
                                            fila[1] = '-';
                                        }
                                    }
                                    else if(hijosFila[cont].nodeName=="RANGO_EDAD"){
                                        nodoCampo = hijosFila[cont];
                                        if(nodoCampo.childNodes.length > 0){
                                            fila[2] = nodoCampo.childNodes[0].nodeValue;
                                        }
                                        else{
                                            fila[2] = '-';
                                        }
                                    }
                                    else if(hijosFila[cont].nodeName=="PAIS"){
                                        nodoCampo = hijosFila[cont];
                                        if(nodoCampo.childNodes.length > 0){
                                            fila[3] = nodoCampo.childNodes[0].nodeValue;
                                        }
                                        else{
                                            fila[3] = '-';
                                        }
                                    }
                                    else if(hijosFila[cont].nodeName=="NIVEL_FORMATIVO"){
                                        nodoCampo = hijosFila[cont];
                                        if(nodoCampo.childNodes.length > 0){
                                            fila[4] = nodoCampo.childNodes[0].nodeValue;
                                        }
                                        else{
                                            fila[4] = '-';
                                        }
                                    }
                                    else if(hijosFila[cont].nodeName=="TITULACION_1"){
                                        nodoCampo = hijosFila[cont];
                                        if(nodoCampo.childNodes.length > 0){
                                            filaTitulaciones[0] = nodoCampo.childNodes[0].nodeValue;
                                        }
                                        else{
                                            filaTitulaciones[0] = '';
                                        }
                                    }
                                    else if(hijosFila[cont].nodeName=="TITULACION_2"){
                                        nodoCampo = hijosFila[cont];
                                        if(nodoCampo.childNodes.length > 0){
                                            filaTitulaciones[1] = nodoCampo.childNodes[0].nodeValue;
                                        }
                                        else{
                                            filaTitulaciones[1] = '';
                                        }
                                    }
                                    else if(hijosFila[cont].nodeName=="TITULACION_3"){
                                        nodoCampo = hijosFila[cont];
                                        if(nodoCampo.childNodes.length > 0){
                                            filaTitulaciones[2] = nodoCampo.childNodes[0].nodeValue;
                                        }
                                        else{
                                            filaTitulaciones[2] = '';
                                        }
                                        fila[5] = filaTitulaciones;
                                    }
                                    else if(hijosFila[cont].nodeName=="SUBV_SOLIC"){
                                        nodoCampo = hijosFila[cont];
                                        if(nodoCampo.childNodes.length > 0){
                                            fila[6] = nodoCampo.childNodes[0].nodeValue;
                                        }
                                        else{
                                            fila[6] = '-';
                                        }
                                    }
                                    else if(hijosFila[cont].nodeName=="SUBV_APROB"){
                                        nodoCampo = hijosFila[cont];
                                        if(nodoCampo.childNodes.length > 0){
                                            fila[7] = nodoCampo.childNodes[0].nodeValue;
                                        }
                                        else{
                                            fila[7] = '-';
                                        }
                                    }
                                    else if(hijosFila[cont].nodeName=="RESULTADO"){
                                        nodoCampo = hijosFila[cont];
                                        if(nodoCampo.childNodes.length > 0){
                                            fila[8] = nodoCampo.childNodes[0].nodeValue;
                                        }
                                        else{
                                            fila[8] = '-';
                                        }
                                    }
                                    else if(hijosFila[cont].nodeName=="MOTIVO"){
                                        nodoCampo = hijosFila[cont];
                                        if(nodoCampo.childNodes.length > 0){
                                            fila[9] = nodoCampo.childNodes[0].nodeValue;
                                        }
                                        else{
                                            fila[9] = '-';
                                        }
                                    }
                                    else if(hijosFila[cont].nodeName=="OBSERVACIONES"){
                                        nodoCampo = hijosFila[cont];
                                        if(nodoCampo.childNodes.length > 0){
                                            var posDentroNodo = 0;
                                            var texto = '';
                                            for(var posDentroNodo = 0; posDentroNodo < nodoCampo.childNodes.length; posDentroNodo++){
                                                if(nodoCampo.childNodes[posDentroNodo].nodeName == 'BR'){
                                                    texto = texto+'<br/>';
                                                }else{
                                                    texto = texto+nodoCampo.childNodes[posDentroNodo].nodeValue;
                                                }
                                            }
                                            fila[10] = texto;
                                        }
                                        else{
                                            fila[10] = '-';
                                        }
                                    }
                                }
                                listaPuestos[j] = fila;
                                filaTitulaciones = new Array();
                                fila = new Array();
                            }   
                        }//for(j=0;hijos!=null && j<hijos.length;j++)
                            if(codigoOperacion=="0"){
                                //window.returnValue =  listaPuestos;
                                self.parent.opener.retornoXanelaAuxiliar(listaPuestos);
								barraProgresoCme('off', 'barraProgresoNuevoPuesto');
                                jsp_alerta("A",'<%=meLanbide46I18n.getMensaje(idiomaUsuario,"msg.puestoGuardadoOK")%>');
                                cerrarVentana();
                            }else if(codigoOperacion=="1"){
                                jsp_alerta("A",'<%=meLanbide46I18n.getMensaje(idiomaUsuario,"error.errorBD")%>');
                            }else if(codigoOperacion=="2"){
                                jsp_alerta("A",'<%=meLanbide46I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                            }else if(codigoOperacion=="3"){
                                jsp_alerta("A",'<%=meLanbide46I18n.getMensaje(idiomaUsuario,"error.pasoParametros")%>');
                            }else if(codigoOperacion=="4"){
                                jsp_alerta("A",'<%=meLanbide46I18n.getMensaje(idiomaUsuario,"error.datosNoValidos")%>');
                            }else{
                                jsp_alerta("A",'<%=meLanbide46I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                            }//if(
                        }
                        catch(Err){
                            jsp_alerta("A",'<%=meLanbide46I18n.getMensaje(idiomaUsuario,"error.errorProcesarRespuesta")%>');
                        }//try-catch

                        barraProgresoCme('off', 'barraProgresoNuevoPuesto');
                    }else{
                        jsp_alerta("A", escape(mensajeValidacion));
                    }
                }
            }
            
            function validarDatos(){
                mensajeValidacion = '';
                var correcto = true;
                try{
                    if(!validarDatosPuesto()){
                        correcto = false;
                    }
                    if(!validarDatosSolicitables()){
                        correcto = false;
                    }
                    if(!validarDatosContrato()){
                        correcto = false;
                    }
                    if(!validarDatosResumen()){
                        correcto = false;
                    }
                    if(!validarDatosSolicitud()){
                        correcto = false;
                    }
                }catch(err){
                    correcto = false;
                    if(mensajeValidacion == ''){
                        mensajeValidacion = '<%=meLanbide46I18n.getMensaje(idiomaUsuario, "msg.errorValidarDatos")%>';
                    }
                }
                return correcto;
            }
            
            function validarDatosPuesto(){
                var correcto = true;
                //Puesto
                <%
                if(puestoModif != null)
                {
                %>
                    var codPuesto = document.getElementById('codPuesto').value;
                    if(codPuesto == null || codPuesto == ''){
                        document.getElementById('codPuesto').style.border = '1px solid red';
                        mensajeValidacion = '<%=meLanbide46I18n.getMensaje(idiomaUsuario, "msg.puesto.datosPuesto.codPuestoVacio")%>';
                        return false;
                    }
                <%
                }
                %>
                var descPuesto = document.getElementById('descPuesto').value;
                if(descPuesto == null || descPuesto == ''){
                    document.getElementById('descPuesto').style.border = '1px solid red';
                    mensajeValidacion = '<%=meLanbide46I18n.getMensaje(idiomaUsuario, "msg.puesto.datosPuesto.descPuestoVacio")%>';
                    return false;
                }else{
                    if(!comprobarCaracteresEspecialesCme(descPuesto)){
                        document.getElementById('descPuesto').style.border = '1px solid red';
                        mensajeValidacion = '<%=meLanbide46I18n.getMensaje(idiomaUsuario, "msg.puesto.datosPuesto.descPuestoCaracteresNoValidos")%>';
                        return false;
                    }else{
                        document.getElementById('descPuesto').removeAttribute("style");
                    }
                }
                
                //Pais 1
                var pais1 = document.getElementById('codPaisPuesto1').value;
                if(!existeCodigoCombo(pais1, codPais)){
                    correcto = false;
                    document.getElementById('codPaisPuesto1').style.border = '1px solid red';
                    document.getElementById('descPaisPuesto1').style.border = '1px solid red';
                    if(mensajeValidacion == ''){
                        mensajeValidacion = '<%=meLanbide46I18n.getMensaje(idiomaUsuario, "msg.puesto.datosPuesto.pais1NoExiste")%>';
                    }
                }else{
                    document.getElementById('codPaisPuesto1').removeAttribute("style");
                    document.getElementById('descPaisPuesto1').removeAttribute("style");
                }
                
                //Pais 2
                var pais2 = document.getElementById('codPaisPuesto2').value;
                if(!existeCodigoCombo(pais2, codPais)){
                    correcto = false;
                    document.getElementById('codPaisPuesto2').style.border = '1px solid red';
                    document.getElementById('descPaisPuesto2').style.border = '1px solid red';
                    if(mensajeValidacion == ''){
                        mensajeValidacion = '<%=meLanbide46I18n.getMensaje(idiomaUsuario, "msg.puesto.datosPuesto.pais2NoExiste")%>';
                    }
                }else{
                    document.getElementById('codPaisPuesto2').removeAttribute("style");
                    document.getElementById('descPaisPuesto2').removeAttribute("style");
                }
                
                //Pais 3
                var pais3 = document.getElementById('codPaisPuesto3').value;
                if(!existeCodigoCombo(pais3, codPais)){
                    correcto = false;
                    document.getElementById('codPaisPuesto3').style.border = '1px solid red';
                    document.getElementById('descPaisPuesto3').style.border = '1px solid red';
                    if(mensajeValidacion == ''){
                        mensajeValidacion = '<%=meLanbide46I18n.getMensaje(idiomaUsuario, "msg.puesto.datosPuesto.pais3NoExiste")%>';
                    }
                }else{
                    document.getElementById('codPaisPuesto3').removeAttribute("style");
                    document.getElementById('descPaisPuesto3').removeAttribute("style");
                }
                
                //Ciudad destino
                var ciudadDest = document.getElementById('ciudadDestino').value;
                if(ciudadDest != null && ciudadDest != ''){
                    if(!comprobarCaracteresEspecialesCme(ciudadDest)){
                        if(mensajeValidacion == ''){
                            mensajeValidacion = '<%=meLanbide46I18n.getMensaje(idiomaUsuario, "msg.puesto.datosPuesto.ciudadDestinoCaracteresNoValidos")%>';
                        }
                        document.getElementById('ciudadDestino').style.border = '1px solid red';
                        correcto = false;
                    }else{
                        document.getElementById('ciudadDestino').removeAttribute("style");
                    }
                }
                
                //Dpto
                var dpto = document.getElementById('dpto').value;
                if(dpto != null && dpto != ''){
                    if(!comprobarCaracteresEspecialesCme(dpto)){
                        if(mensajeValidacion == ''){
                            mensajeValidacion = '<%=meLanbide46I18n.getMensaje(idiomaUsuario, "msg.puesto.datosPuesto.dptoCaracteresNoValidos")%>';
                        }
                        document.getElementById('dpto').style.border = '1px solid red';
                        correcto = false;
                    }else{
                        document.getElementById('dpto').removeAttribute("style");
                    }
                }
                
                //Funciones
                var funciones = document.getElementById('funcionesPuesto').value;
                if(funciones != null && funciones != ''){
                    if(!comprobarCaracteresEspecialesCme(funciones)){
                        if(mensajeValidacion == ''){
                            mensajeValidacion = '<%=meLanbide46I18n.getMensaje(idiomaUsuario, "msg.puesto.datosPuesto.funcionesCaracteresNoValidos")%>';
                        }
                        document.getElementById('funcionesPuesto').style.border = '1px solid red';
                        correcto = false;
                    }else{
                        if(funciones.length > 1000){
                            if(mensajeValidacion == ''){
                                mensajeValidacion = '<%=meLanbide46I18n.getMensaje(idiomaUsuario, "msg.puesto.datosPuesto.funcionesDemasiadoLargo")%>';
                            }
                            document.getElementById('funcionesPuesto').style.border = '1px solid red';
                            correcto = false;
                        }else{
                            document.getElementById('funcionesPuesto').removeAttribute("style");
                        }
                    }
                }
                
                //Idioma 1
                var idi1 = document.getElementById('codIdiomaPuesto1').value;
                if(!existeCodigoCombo(idi1, codIdioma)){
                    correcto = false;
                    document.getElementById('codIdiomaPuesto1').style.border = '1px solid red';
                    document.getElementById('descIdiomaPuesto1').style.border = '1px solid red';
                    if(mensajeValidacion == ''){
                        mensajeValidacion = '<%=meLanbide46I18n.getMensaje(idiomaUsuario, "msg.puesto.datosPuesto.idiomaNoExiste")%>';
                    }
                }else{
                    document.getElementById('codIdiomaPuesto1').removeAttribute("style");
                    document.getElementById('descIdiomaPuesto1').removeAttribute("style");
                }
                
                //Idioma 2
                var idi2 = document.getElementById('codIdiomaPuesto2').value;
                if(!existeCodigoCombo(idi2, codIdioma)){
                    correcto = false;
                    document.getElementById('codIdiomaPuesto2').style.border = '1px solid red';
                    document.getElementById('descIdiomaPuesto2').style.border = '1px solid red';
                    if(mensajeValidacion == ''){
                        mensajeValidacion = '<%=meLanbide46I18n.getMensaje(idiomaUsuario, "msg.puesto.datosPuesto.idiomaNoExiste")%>';
                    }
                }else{
                    document.getElementById('codIdiomaPuesto2').removeAttribute("style");
                    document.getElementById('descIdiomaPuesto2').removeAttribute("style");
                }
                
                //Idioma 2
                var idi3 = document.getElementById('codIdiomaPuesto3').value;
                if(!existeCodigoCombo(idi3, codIdioma)){
                    correcto = false;
                    document.getElementById('codIdiomaPuesto3').style.border = '1px solid red';
                    document.getElementById('descIdiomaPuesto3').style.border = '1px solid red';
                    if(mensajeValidacion == ''){
                        mensajeValidacion = '<%=meLanbide46I18n.getMensaje(idiomaUsuario, "msg.puesto.datosPuesto.idiomaNoExiste")%>';
                    }
                }else{
                    document.getElementById('codIdiomaPuesto3').removeAttribute("style");
                    document.getElementById('descIdiomaPuesto3').removeAttribute("style");
                }
                
                //Nivel Idioma 1
                var nidi1 = document.getElementById('codNivelIdiomaPuesto1').value;
                if(!existeCodigoCombo(nidi1, codNivelIdioma)){
                    correcto = false;
                    document.getElementById('codNivelIdiomaPuesto1').style.border = '1px solid red';
                    document.getElementById('descNivelIdiomaPuesto1').style.border = '1px solid red';
                    if(mensajeValidacion == ''){
                        mensajeValidacion = '<%=meLanbide46I18n.getMensaje(idiomaUsuario, "msg.puesto.datosPuesto.nivelIdiomaNoExiste")%>';
                    }
                }else{
                    document.getElementById('codNivelIdiomaPuesto1').removeAttribute("style");
                    document.getElementById('descNivelIdiomaPuesto1').removeAttribute("style");
                }
                
                //Nivel Idioma 2
                var nidi2 = document.getElementById('codNivelIdiomaPuesto2').value;
                if(!existeCodigoCombo(nidi2, codNivelIdioma)){
                    correcto = false;
                    document.getElementById('codNivelIdiomaPuesto2').style.border = '1px solid red';
                    document.getElementById('descNivelIdiomaPuesto2').style.border = '1px solid red';
                    if(mensajeValidacion == ''){
                        mensajeValidacion = '<%=meLanbide46I18n.getMensaje(idiomaUsuario, "msg.puesto.datosPuesto.nivelIdiomaNoExiste")%>';
                    }
                }else{
                    document.getElementById('codNivelIdiomaPuesto2').removeAttribute("style");
                    document.getElementById('descNivelIdiomaPuesto2').removeAttribute("style");
                }
                
                //Nivel Idioma 3
                var nidi3 = document.getElementById('codNivelIdiomaPuesto3').value;
                if(!existeCodigoCombo(nidi3, codNivelIdioma)){
                    correcto = false;
                    document.getElementById('codNivelIdiomaPuesto3').style.border = '1px solid red';
                    document.getElementById('descNivelIdiomaPuesto3').style.border = '1px solid red';
                    if(mensajeValidacion == ''){
                        mensajeValidacion = '<%=meLanbide46I18n.getMensaje(idiomaUsuario, "msg.puesto.datosPuesto.nivelIdiomaNoExiste")%>';
                    }
                }else{
                    document.getElementById('codNivelIdiomaPuesto3').removeAttribute("style");
                    document.getElementById('descNivelIdiomaPuesto3').removeAttribute("style");
                }
                
                //Nivel Formativo
                var nFor = document.getElementById('codNivelFormativo').value;
                if(!existeCodigoCombo(nFor, codNivelFormativo)){
                    correcto = false;
                    document.getElementById('codNivelFormativo').style.border = '1px solid red';
                    document.getElementById('descNivelFormativo').style.border = '1px solid red';
                    
                    if(mensajeValidacion == ''){
                        mensajeValidacion = '<%=meLanbide46I18n.getMensaje(idiomaUsuario, "msg.puesto.datosPuesto.nivelForNoExiste")%>';
                    }
                }else{
                    document.getElementById('codNivelFormativo').removeAttribute("style");
                    document.getElementById('descNivelFormativo').removeAttribute("style");
                }
                return correcto;
            }
            
            function validarDatosSolicitables(){
                var combo = document.getElementById('codPaisPuesto1');
                var correcto = true;
                
                //Salario total
                if(document.getElementById('txtTotSalarioSolicitables').value != ''){
                    try{
                        if(!validarNumericoDecimalCme(document.getElementById('txtTotSalarioSolicitables'), 10, 2)){
                            correcto = false;
                            document.getElementById('txtTotSalarioSolicitables').style.border = '1px solid red';
                            if(mensajeValidacion == '')
                                mensajeValidacion = '<%=meLanbide46I18n.getMensaje(idiomaUsuario, "msg.puesto.datosSolicitables.salarioAnexoTotIncorrecto")%>';
                        }else{
                            document.getElementById('txtTotSalarioSolicitables').removeAttribute('style');
                        }
                    }catch(err){
                        correcto = false;
                        document.getElementById('txtTotSalarioSolicitables').style.border = '1px solid red';
                        if(mensajeValidacion == '')
                            mensajeValidacion = '<%=meLanbide46I18n.getMensaje(idiomaUsuario, "msg.puesto.datosSolicitables.salarioAnexoTotIncorrecto")%>';
                    }
                }
                
                //Dietas manutenci�n
                if(document.getElementById('txtDietasSolicitables').value != ''){
                    try{
                        if(!validarNumericoDecimalCme(document.getElementById('txtDietasSolicitables'), 10, 2)){
                            correcto = false;
                            document.getElementById('txtDietasSolicitables').style.border = '1px solid red';
                            if(mensajeValidacion == '')
                                mensajeValidacion = '<%=meLanbide46I18n.getMensaje(idiomaUsuario, "msg.puesto.datosSolicitables.dietasManutIncorrecto")%>';
                        }else{
                            document.getElementById('txtDietasSolicitables').removeAttribute('style');
                        }
                    }catch(err){
                        correcto = false;
                        document.getElementById('txtDietasSolicitables').style.border = '1px solid red';
                        if(mensajeValidacion == '')
                            mensajeValidacion = '<%=meLanbide46I18n.getMensaje(idiomaUsuario, "msg.puesto.datosSolicitables.dietasManutIncorrecto")%>';
                    }
                }
                
                //Meses manutenci�n
                if(document.getElementById('txtMesesSolicitables').value != ''){
                    try{
                        var estado = 0;
                        if(!validarNumericoCme(document.getElementById('txtMesesSolicitables'), 4)){
                            correcto = false;
                            document.getElementById('txtMesesSolicitables').style.border = '1px solid red';
                            if(mensajeValidacion == '')
                                mensajeValidacion = '<%=meLanbide46I18n.getMensaje(idiomaUsuario, "msg.puesto.datosSolicitables.mesesManutIncorrecto2")%>';
                        }else{
                            var meses = parseInt(document.getElementById('txtMesesSolicitables').value);
                            if(document.getElementById('codResultadoResumen').value != '')
                                var estado = parseInt(document.getElementById('codResultadoResumen').value);
                            else 
                                var estado = 0;
                            if(meses > 12)
                                correcto = false;
                            if(estado == 1 && meses < 7)
                                correcto = false;
                            if(estado != 1 && meses < 7)
                            {
                                
                                document.getElementById('txtMesesSolicitables').style.border = '1px solid red';
                                if(mensajeValidacion == ''){
                                    mensajeValidacion = '<%=meLanbide46I18n.getMensaje(idiomaUsuario, "msg.puesto.datosSolicitables.mesesManutIncorrecto3")%>';
                                
                                    jsp_alerta("A", escape(mensajeValidacion));
                                }
                            }                            
                            if(!correcto)
                            {
                                document.getElementById('txtMesesSolicitables').style.border = '1px solid red';
                                if(mensajeValidacion == '')
                                    mensajeValidacion = '<%=meLanbide46I18n.getMensaje(idiomaUsuario, "msg.puesto.datosSolicitables.mesesManutIncorrecto2")%>';                                                       
                            }                                                        
                        }                     
                    }catch(err){
                        correcto = false;
                        document.getElementById('txtMesesSolicitables').style.border = '1px solid red';
                        if(mensajeValidacion == '')
                            mensajeValidacion = '<%=meLanbide46I18n.getMensaje(idiomaUsuario, "msg.puesto.datosSolicitables.mesesManutIncorrecto2")%>';
                    }
                }
                
                
                //Tramitaci�n seguros m�dicos y permisos
                if(document.getElementById('txtSegurosSolicitables').value != ''){
                    try{
                        if(!validarNumericoDecimalCme(document.getElementById('txtSegurosSolicitables'), 10, 2)){
                            correcto = false;
                            document.getElementById('txtSegurosSolicitables').style.border = '1px solid red';
                            if(mensajeValidacion == '')
                                mensajeValidacion = '<%=meLanbide46I18n.getMensaje(idiomaUsuario, "msg.puesto.datosSolicitables.tramSegurosIncorrecto")%>';
                        }else{
                            var valor = document.getElementById('txtSegurosSolicitables').value;
                            valor = reemplazarTextoCme(valor, /,/g, '.');
                            var importe = parseFloat(valor);
                            if(importe > 450){
                                correcto = false;
                                document.getElementById('txtSegurosSolicitables').style.border = '1px solid red';
                                if(mensajeValidacion == '')
                                    mensajeValidacion = '<%=meLanbide46I18n.getMensaje(idiomaUsuario, "msg.puesto.datosSolicitables.tramSegurosSuperaMaximo")%>';
                            }else{
                                document.getElementById('txtSegurosSolicitables').removeAttribute('style');
                            }
                        }
                    }catch(err){
                        correcto = false;
                        document.getElementById('txtSegurosSolicitables').style.border = '1px solid red';
                        if(mensajeValidacion == '')
                            mensajeValidacion = '<%=meLanbide46I18n.getMensaje(idiomaUsuario, "msg.puesto.datosSolicitables.tramSegurosIncorrecto")%>';
                    }
                }
                
                //Importe solicitado
                if(document.getElementById('txtImporteSolicitables').value != ''){
                    try{
                        if(!validarNumericoDecimalCme(document.getElementById('txtImporteSolicitables'), 10, 2)){
                            correcto = false;
                            document.getElementById('txtImporteSolicitables').style.border = '1px solid red';
                            if(mensajeValidacion == '')
                                mensajeValidacion = '<%=meLanbide46I18n.getMensaje(idiomaUsuario, "msg.puesto.datosSolicitables.impSolicIncorrecto")%>';
                        }else{
                            document.getElementById('txtImporteSolicitables').removeAttribute('style');
                        }
                    }catch(err){
                        correcto = false;
                        document.getElementById('txtImporteSolicitables').style.border = '1px solid red';
                        if(mensajeValidacion == '')
                            mensajeValidacion = '<%=meLanbide46I18n.getMensaje(idiomaUsuario, "msg.puesto.datosSolicitables.impSolicIncorrecto")%>';
                    }
                }
                
                
                return correcto;
            }
            
            function validarDatosContrato(){
                var correcto = true;
        
                //Fecha inicio
                if(!validarFechaCme(document.forms[0], document.getElementById('fechaIniContrato'))){
                    document.getElementById('fechaIniContrato').style.border = '1px solid red';
                    if(mensajeValidacion == '')
                        mensajeValidacion = '<%=meLanbide46I18n.getMensaje(idiomaUsuario, "msg.puesto.datosContrato.fechaIniIncorrecto")%>';
                    correcto = false;
                }else{
                    document.getElementById('fechaIniContrato').removeAttribute('style');
                }
        
                //Fecha fin
                if(!validarFechaCme(document.forms[0], document.getElementById('fechaFinContrato'))){
                    document.getElementById('fechaFinContrato').style.border = '1px solid red';
                    if(mensajeValidacion == '')
                        mensajeValidacion = '<%=meLanbide46I18n.getMensaje(idiomaUsuario, "msg.puesto.datosContrato.fechaFinIncorrecto")%>';
                    correcto = false;
                }else{
                    document.getElementById('fechaFinContrato').removeAttribute('style');
                }
                
                if(correcto){
                    //La fecha de nacimiento no puede ser mayor que la actual
                    var array_fecha_ini = document.getElementById('fechaIniContrato').value.split("/");
                    var array_fecha_fin = document.getElementById('fechaFinContrato').value.split("/");
                    if(array_fecha_ini.length == 3 && array_fecha_fin.length == 3)
                    {
                        var dia_ini = array_fecha_ini[0];
                        var mes_ini = array_fecha_ini[1];
                        var ano_ini = array_fecha_ini[2];
                        var dia_fin = array_fecha_fin[0];
                        var mes_fin = array_fecha_fin[1];
                        var ano_fin = array_fecha_fin[2];
                        var d_ini = new Date(ano_ini, mes_ini-1, dia_ini, 0, 0, 0, 0);
                        var d_fin = new Date(ano_fin, mes_fin-1, dia_fin, 0, 0, 0, 0);
                        var n1 = d_fin.getTime();
                        var n2 = d_ini.getTime();
                        var result = n1 - n2;
                        if(result < 0){
                            correcto = false;
                            document.getElementById('fechaIniContrato').style.border = '1px solid red';
                            document.getElementById('fechaFinContrato').style.border = '1px solid red';
                            if(mensajeValidacion == ''){
                                mensajeValidacion = '<%=meLanbide46I18n.getMensaje(idiomaUsuario, "msg.puesto.datosContrato.fechaIniPosteriorFechaFin")%>';
                            }
                        }else{
                            document.getElementById('fechaIniContrato').removeAttribute('style');
                            document.getElementById('fechaFinContrato').removeAttribute('style');
                        }
                    }
                }
                
                //Duracion (meses)
                if(document.getElementById('txtMesesContratado').value != ''){
                    try{
                        if(!validarNumericoCme(document.getElementById('txtMesesContratado'), 6)
                            || document.getElementById('txtMesesContratado').value < 12){
                            correcto = false;
                            document.getElementById('txtMesesContratado').style.border = '1px solid red';
                            if(mensajeValidacion == '')
                                mensajeValidacion = '<%=meLanbide46I18n.getMensaje(idiomaUsuario, "msg.puesto.datosContrato.duracionMesesIncorrecto")%>';
                        }else{
                            document.getElementById('txtMesesContratado').removeAttribute('style');
                        }
                    }catch(err){
                        correcto = false;
                        document.getElementById('txtMesesContratado').style.border = '1px solid red';
                        if(mensajeValidacion == '')
                            mensajeValidacion = '<%=meLanbide46I18n.getMensaje(idiomaUsuario, "msg.puesto.datosContrato.duracionMesesIncorrecto")%>';
                    }
                }
                
                //Grupo cotizaci�n
                var gCot = document.getElementById('codGrupoContrato').value;
                if(!existeCodigoCombo(gCot, codGrupoCot)){
                    correcto = false;
                    document.getElementById('codGrupoContrato').style.border = '1px solid red';
                    document.getElementById('descGrupoContrato').style.border = '1px solid red';
                    if(mensajeValidacion == ''){
                        mensajeValidacion = '<%=meLanbide46I18n.getMensaje(idiomaUsuario, "msg.puesto.datosContrato.grupoCotIncorrecto")%>';
                    }
                }else{
                    document.getElementById('codGrupoContrato').removeAttribute("style");
                    document.getElementById('descGrupoContrato').removeAttribute("style");
                }
        
                //convenio colectivo
                var convenio = document.getElementById('txtConvenioContrato').value;
                if(!comprobarCaracteresEspecialesCme(convenio)){
                    document.getElementById('txtConvenioContrato').style.border = '1px solid red';
                    mensajeValidacion = '<%=meLanbide46I18n.getMensaje(idiomaUsuario, "msg.puesto.datosContrato.convenioCaracteresNoValidos")%>';
                    return false;
                }else{
                    if(convenio.length > 500){
                        if(mensajeValidacion == ''){
                            mensajeValidacion = '<%=meLanbide46I18n.getMensaje(idiomaUsuario, "msg.puesto.datosPuesto.convenioColDemasiadoLargo")%>';
                        }
                        document.getElementById('txtConvenioContrato').style.border = '1px solid red';
                        correcto = false;
                    }else{
                        document.getElementById('txtConvenioContrato').removeAttribute("style");
                    }
                }
                
                //Salario bruto
                if(document.getElementById('txtSalarioContrato').value != ''){
                    try{
                        if(!validarNumericoDecimalCme(document.getElementById('txtSalarioContrato'), 10, 2)){
                            correcto = false;
                            document.getElementById('txtSalarioContrato').style.border = '1px solid red';
                            if(mensajeValidacion == '')
                                mensajeValidacion = '<%=meLanbide46I18n.getMensaje(idiomaUsuario, "msg.puesto.datosContrato.salarioContratoIncorrecto")%>';
                        }else{
                            document.getElementById('txtSalarioContrato').removeAttribute('style');
                        }
                    }catch(err){
                        correcto = false;
                        document.getElementById('txtSalarioContrato').style.border = '1px solid red';
                        if(mensajeValidacion == '')
                            mensajeValidacion = '<%=meLanbide46I18n.getMensaje(idiomaUsuario, "msg.puesto.datosContrato.salarioContratoIncorrecto")%>';
                    }
                }
                
                //Dietas convenio
                if(document.getElementById('txtDietasContrato').value != ''){
                    try{
                        if(!validarNumericoDecimalCme(document.getElementById('txtDietasContrato'), 10, 2)){
                            correcto = false;
                            document.getElementById('txtDietasContrato').style.border = '1px solid red';
                            if(mensajeValidacion == '')
                                mensajeValidacion = '<%=meLanbide46I18n.getMensaje(idiomaUsuario, "msg.puesto.datosContrato.dietasConvenioIncorrecto")%>';
                        }else{
                            document.getElementById('txtDietasContrato').removeAttribute('style');
                        }
                    }catch(err){
                        correcto = false;
                        document.getElementById('txtDietasContrato').style.border = '1px solid red';
                        if(mensajeValidacion == '')
                            mensajeValidacion = '<%=meLanbide46I18n.getMensaje(idiomaUsuario, "msg.puesto.datosContrato.dietasConvenioIncorrecto")%>';
                    }
                }
                
                //Coste contratacion
                if(document.getElementById('txtCosteContrato').value != ''){
                    try{
                        if(!validarNumericoDecimalCme(document.getElementById('txtCosteContrato'), 10, 2)){
                            correcto = false;
                            document.getElementById('txtCosteContrato').style.border = '1px solid red';
                            if(mensajeValidacion == '')
                                mensajeValidacion = '<%=meLanbide46I18n.getMensaje(idiomaUsuario, "msg.puesto.datosContrato.costeContratacionIncorrecto")%>';
                        }else{
                            document.getElementById('txtCosteContrato').removeAttribute('style');
                        }
                    }catch(err){
                        correcto = false;
                        document.getElementById('txtCosteContrato').style.border = '1px solid red';
                        if(mensajeValidacion == '')
                            mensajeValidacion = '<%=meLanbide46I18n.getMensaje(idiomaUsuario, "msg.puesto.datosContrato.costeContratacionIncorrecto")%>';
                    }
                }
                
                //Otros beneficios
                var benef = document.getElementById('otrosBeneficiosContrato').value;
                if(!comprobarCaracteresEspecialesCme(benef)){
                    document.getElementById('otrosBeneficiosContrato').style.border = '1px solid red';
                    mensajeValidacion = '<%=meLanbide46I18n.getMensaje(idiomaUsuario, "msg.puesto.datosContrato.otrosBenefCaracteresNoValidos")%>';
                    return false;
                }else{
                    if(benef.length > 1000){
                        if(mensajeValidacion == ''){
                            mensajeValidacion = '<%=meLanbide46I18n.getMensaje(idiomaUsuario, "msg.puesto.datosPuesto.otrosBenefDemasiadoLargo")%>';
                        }
                        document.getElementById('otrosBeneficiosContrato').style.border = '1px solid red';
                        correcto = false;
                    }else{
                        document.getElementById('otrosBeneficiosContrato').removeAttribute("style");
                    }
                }
                
                return correcto;
            }
            
            function validarDatosResumen() {
                var correcto = true;
                
                //Resultado
                var gCot = document.getElementById('codResultadoResumen').value;
                if(!existeCodigoCombo(gCot, codResultado)){
                    correcto = false;
                    document.getElementById('codResultadoResumen').style.border = '1px solid red';
                    document.getElementById('descResultadoResumen').style.border = '1px solid red';
                    if(mensajeValidacion == ''){
                        mensajeValidacion = '<%=meLanbide46I18n.getMensaje(idiomaUsuario, "msg.puesto.datosResumenPuesto.resultadoNoExiste")%>';
                    }
                }else if (gCot == '' || gCot.value == '000'){
                    correcto = false;
                    document.getElementById('codResultadoResumen').style.border = '1px solid red';
                    document.getElementById('descResultadoResumen').style.border = '1px solid red';
                    if(mensajeValidacion == '')
                                mensajeValidacion = '<%=meLanbide46I18n.getMensaje(idiomaUsuario, "msg.puesto.datosResumenPuesto.estado")%>';
                }else{
                    document.getElementById('codResultadoResumen').removeAttribute("style");
                    document.getElementById('descResultadoResumen').removeAttribute("style");
                }
                
                //Motivo
                var gCot = document.getElementById('codMotivoResumen').value;
                if(!existeCodigoCombo(gCot, codMotivo)){
                    correcto = false;
                    document.getElementById('codMotivoResumen').style.border = '1px solid red';
                    document.getElementById('descMotivoResumen').style.border = '1px solid red';
                    if(mensajeValidacion == ''){
                        mensajeValidacion = '<%=meLanbide46I18n.getMensaje(idiomaUsuario, "msg.puesto.datosResumenPuesto.motivoNoExiste")%>';
                    }
                }else{
                    document.getElementById('codMotivoResumen').removeAttribute("style");
                    document.getElementById('descMotivoResumen').removeAttribute("style");
                }
                
                //Observaciones
                var obs = document.getElementById('observacionesResumen').value;
                if(!comprobarCaracteresEspecialesCme(obs)){
                    document.getElementById('observacionesResumen').style.border = '1px solid red';
                    mensajeValidacion = '<%=meLanbide46I18n.getMensaje(idiomaUsuario, "msg.puesto.datosResumenPuesto.observacionesCaracteresNoValidos")%>';
                    return false;
                }else{
                    if(obs.length > 1000){
                        if(mensajeValidacion == ''){
                            mensajeValidacion = '<%=meLanbide46I18n.getMensaje(idiomaUsuario, "msg.puesto.datosPuesto.observacionesDemasiadoLargo")%>';
                        }
                        document.getElementById('observacionesResumen').style.border = '1px solid red';
                        correcto = false;
                    }else{
                        document.getElementById('observacionesResumen').removeAttribute("style");
                    }
                }
                
                return correcto;
            }
            
            function validarDatosSolicitud(){
                var correcto = true;
                
                //Salario solicitud
                if(document.getElementById('txtSalarioSolicitud').value != ''){
                    try{
                        if(!validarNumericoDecimalCme(document.getElementById('txtSalarioSolicitud'), 10, 2)){
                            correcto = false;
                            document.getElementById('txtSalarioSolicitud').style.border = '1px solid red';
                            if(mensajeValidacion == '')
                                mensajeValidacion = '<%=meLanbide46I18n.getMensaje(idiomaUsuario, "msg.puesto.datosSolicitud.salarioIncorrecto")%>';
                        }else{
                            document.getElementById('txtSalarioSolicitud').removeAttribute('style');
                        }
                    }catch(err){
                        correcto = false;
                        document.getElementById('txtSalarioSolicitud').style.border = '1px solid red';
                        if(mensajeValidacion == '')
                            mensajeValidacion = '<%=meLanbide46I18n.getMensaje(idiomaUsuario, "msg.puesto.datosSolicitud.salarioIncorrecto")%>';
                    }
                }
                
                //Dietas solicitud
                if(document.getElementById('txtDietasSolicitud').value != ''){
                    try{
                        if(!validarNumericoDecimalCme(document.getElementById('txtDietasSolicitud'), 10, 2)){
                            correcto = false;
                            document.getElementById('txtDietasSolicitud').style.border = '1px solid red';
                            if(mensajeValidacion == '')
                                mensajeValidacion = '<%=meLanbide46I18n.getMensaje(idiomaUsuario, "msg.puesto.datosSolicitud.dietasIncorrecto")%>';
                        }else{
                            document.getElementById('txtDietasSolicitud').removeAttribute('style');
                        }
                    }catch(err){
                        correcto = false;
                        document.getElementById('txtDietasSolicitud').style.border = '1px solid red';
                        if(mensajeValidacion == '')
                            mensajeValidacion = '<%=meLanbide46I18n.getMensaje(idiomaUsuario, "msg.puesto.datosSolicitud.dietasIncorrecto")%>';
                    }
                }
                
                //Tramitacion solicitud
                if(document.getElementById('txtTramitacionSolicitud').value != ''){
                    try{
                        if(!validarNumericoDecimalCme(document.getElementById('txtTramitacionSolicitud'), 10, 2)){
                            correcto = false;
                            document.getElementById('txtTramitacionSolicitud').style.border = '1px solid red';
                            if(mensajeValidacion == '')
                                mensajeValidacion = '<%=meLanbide46I18n.getMensaje(idiomaUsuario, "msg.puesto.datosSolicitud.tramitacionIncorrecto")%>';
                        }else{
                            document.getElementById('txtTramitacionSolicitud').removeAttribute('style');
                        }
                    }catch(err){
                        correcto = false;
                        document.getElementById('txtTramitacionSolicitud').style.border = '1px solid red';
                        if(mensajeValidacion == '')
                            mensajeValidacion = '<%=meLanbide46I18n.getMensaje(idiomaUsuario, "msg.puesto.datosSolicitud.tramitacionIncorrecto")%>';
                    }
                }
                
                //Imp tot solicitud
                if(document.getElementById('txtImpTotalSolicitud').value != ''){
                    try{
                        if(!validarNumericoDecimalCme(document.getElementById('txtImpTotalSolicitud'), 10, 2)){
                            correcto = false;
                            document.getElementById('txtImpTotalSolicitud').style.border = '1px solid red';
                            if(mensajeValidacion == '')
                                mensajeValidacion = '<%=meLanbide46I18n.getMensaje(idiomaUsuario, "msg.puesto.datosSolicitud.impTotIncorrecto")%>';
                        }else{
                            document.getElementById('txtImpTotalSolicitud').removeAttribute('style');
                        }
                    }catch(err){
                        correcto = false;
                        document.getElementById('txtImpTotalSolicitud').style.border = '1px solid red';
                        if(mensajeValidacion == '')
                            mensajeValidacion = '<%=meLanbide46I18n.getMensaje(idiomaUsuario, "msg.puesto.datosSolicitud.impTotIncorrecto")%>';
                    }
                }
                return correcto;
            }
            
            function existeCodigoCombo(seleccionado, listaCodigos){
                if(seleccionado != undefined && seleccionado != null && listaCodigos != undefined && listaCodigos != null){
                    if(trim(seleccionado) != ''){
                        var encontrado = false;
                        var i = 0;
                        while(!encontrado && i < listaCodigos.length){
                            if(listaCodigos[i] == seleccionado){
                                encontrado = true;
                            }else{
                                i++;
                            }
                        }
                        return encontrado;
                    }else{
                        return true;
                    }
                }else{
                    return true;
                }
            }
            
            function cambioPais1(){
                var codigo = document.getElementById('codPaisPuesto1').value;
                var desc = '';
                var encontrado = false;
                var i = 0; 
                var codAct;
                if(codigo != null && codigo != '')
                {
                    while(i<codPais.length && !encontrado)
                    {
                        codAct = codPais[i];
                        if(codAct == codigo)
                        {
                            encontrado = true;
                            desc = descPais[i];
                        }else{
                            i++;
                        }
                    }
                }
                document.getElementById('descPaisPuesto1').value = desc;
                
            }
            
            function cambioPais2(){
                var codigo = document.getElementById('codPaisPuesto2').value;
                var desc = '';
                var encontrado = false;
                var i = 0; 
                var codAct;
                if(codigo != null && codigo != '')
                {
                    while(i<codPais.length && !encontrado)
                    {
                        codAct = codPais[i];
                        if(codAct == codigo)
                        {
                            encontrado = true;
                            desc = descPais[i];
                        }else{
                            i++;
                        }
                    }
                }
                document.getElementById('descPaisPuesto2').value = desc;
                
            }
            
            function cambioPais3(){
                var codigo = document.getElementById('codPaisPuesto3').value;
                var desc = '';
                var encontrado = false;
                var i = 0; 
                var codAct;
                if(codigo != null && codigo != '')
                {
                    while(i<codPais.length && !encontrado)
                    {
                        codAct = codPais[i];
                        if(codAct == codigo)
                        {
                            encontrado = true;
                            desc = descPais[i];
                        }else{
                            i++;
                        }
                    }
                }
                document.getElementById('descPaisPuesto3').value = desc;
                
            }
            
            function cambioNivelFormativo(){
                var codigo = document.getElementById('codNivelFormativo').value;
                var desc = '';
                var encontrado = false;
                var i = 0; 
                var codAct;
                if(codigo != null && codigo != '')
                {
                    while(i<codNivelFormativo.length && !encontrado)
                    {
                        codAct = codNivelFormativo[i];
                        if(codAct == codigo)
                        {
                            encontrado = true;
                            desc = descNivelFormativo[i];
                        }else{
                            i++;
                        }
                    }
                }
                
                ajustarDecimalesImportes();
            }
            
            function cambioGrupoCot(){
                var codigo = document.getElementById('codGrupoContrato').value;
                var desc = '';
                var encontrado = false;
                var i = 0; 
                var codAct;
                if(codigo != null && codigo != '')
                {
                    while(i<codGrupoCot.length && !encontrado)
                    {
                        codAct = codGrupoCot[i];
                        if(codAct == codigo)
                        {
                            encontrado = true;
                            desc = descGrupoCot[i];
                        }else{
                            i++;
                        }
                    }
                }
                document.getElementById('descGrupoContrato').value = desc;
                
            }
            
            function cambioResultado(){
                var codigo = document.getElementById('codResultadoResumen').value;
                if(codigo != null && codigo != '')
                {
                    if(codigo == '<%=ConstantesMeLanbide46.CODIGO_RESULTADO_RENUNCIA%>' || codigo == '<%=ConstantesMeLanbide46.CODIGO_RESULTADO_DENEGADO%>'){
                        document.getElementById('divLabelMotivoResumen').style.display = 'inline';
                        document.getElementById('divComboMotivoResumen').style.display = 'inline';
                    }else{
                        document.getElementById('divLabelMotivoResumen').style.display = 'none';
                        document.getElementById('divComboMotivoResumen').style.display = 'none';
                    }
                }
            }
            
            function calcularDietasManutencion(){
                var anchor = document.getElementById('');
                var correcto = true;
                reemplazarPuntosCme(document.getElementById('txtMesesSolicitables'));
                try{
                    if(!validarNumericoCme(document.getElementById('txtMesesSolicitables'), 4)){
                        correcto = false;
                        document.getElementById('txtMesesSolicitables').style.border = '1px solid red';
                    }else{
                        document.getElementById('txtMesesSolicitables').removeAttribute('style');
                    }
                }catch(err){
                    correcto = false;
                    document.getElementById('txtMesesSolicitables').style.border = '1px solid red';
                }
                
                if(correcto){
                    var mStr = document.getElementById('txtMesesSolicitables').value;
                    if(mStr != ''){
                        var meses = parseInt(mStr);
                        meses = meses * 350;
                        meses = meses.toFixed(2);
                        document.getElementById('txtDietasSolicitables').value = meses;
                    }else{
                        document.getElementById('txtDietasSolicitables').value = '';
                    }
                }else{
                    document.getElementById('txtDietasSolicitables').value = '';
                }
                calcularImporteMaxSubv();
            }
            
            function calcularCosteContratacion(campo){
                var dStr = document.getElementById('txtDietasContrato').value;
                var sStr = document.getElementById('txtSalarioContrato').value;
                
                var correcto = true;
                
                if(dStr != '' || sStr != ''){
                    reemplazarPuntosCme(campo);

                    try
                    {
                        if(!validarNumericoDecimalCme(document.getElementById('txtSalarioContrato'), 10, 2)){
                            correcto = false;
                            document.getElementById('txtSalarioContrato').style.border = '1px solid red';
                        }else{
                            document.getElementById('txtSalarioContrato').removeAttribute('style');
                        }
                    }catch(err){
                        correcto = false;
                        document.getElementById('txtSalarioContrato').style.border = '1px solid red';
                    }

                    try{
                        if(!validarNumericoDecimalCme(document.getElementById('txtDietasContrato'), 10, 2)){
                            correcto = false;
                            document.getElementById('txtDietasContrato').style.border = '1px solid red';
                        }else{
                            document.getElementById('txtDietasContrato').removeAttribute('style');
                        }
                    }catch(err){
                        correcto = false;
                        document.getElementById('txtDietasContrato').style.border = '1px solid red';
                    }

                    if(correcto){
                        dStr = reemplazarTextoCme(dStr, /,/g, '.');
                        sStr = reemplazarTextoCme(sStr, /,/g, '.');
                        var dietas = 0.0;
                        var salario = 0.0;

                        if(dStr != ''){
                            dietas = parseFloat(dStr);
                        }

                        if(sStr != ''){
                            salario = parseFloat(sStr);
                        }

                        var coste = dietas + salario;
                        coste = coste.toFixed(2);
                        document.getElementById('txtCosteContrato').value = coste;
                        reemplazarPuntosCme(document.getElementById('txtCosteContrato'));
                    }else{
                        document.getElementById('txtCosteContrato').value = '';
                    }
                }else{
                    document.getElementById('txtCosteContrato').value = '';
                }
                
                //MCG - CME
                document.getElementById('txtTotSalarioSolicitables').value = document.getElementById('txtSalarioContrato').value;
                calcularImporteMaxSubv();
            }
            
            function calcularImporteMaxSubv(){
                var pStr = document.getElementById('txtSegurosSolicitables').value;
                var mStr = document.getElementById('txtDietasSolicitables').value;
                var sStr = document.getElementById('txtTotSalarioSolicitables').value;
                
                if(pStr != '' || mStr != '' || sStr != ''){
                
                    reemplazarPuntosCme(document.getElementById('txtSegurosSolicitables'));
                    reemplazarPuntosCme(document.getElementById('txtDietasSolicitables'));
                    reemplazarPuntosCme(document.getElementById('txtTotSalarioSolicitables'));

                    var correcto = true;
                   /* try{
                        if(!validarNumericoDecimalCme(campo.value, 10, 2)){
                            correcto = false;
                            campo.style.border = '1px solid red';
                        }else{
                            campo.removeAttribute('style');
                        }
                    }catch(err){
                        correcto = false;
                        campo.style.border = '1px solid red';
                    }*/

                    try
                    {
                        if(!validarNumericoDecimalCme(document.getElementById('txtSegurosSolicitables'), 10, 2)){
                            correcto = false;
                            document.getElementById('txtSegurosSolicitables').style.border = '1px solid red';
                        }else{
                            document.getElementById('txtSegurosSolicitables').removeAttribute('style');
                        }
                    }catch(err){
                        correcto = false;
                        document.getElementById('txtSegurosSolicitables').style.border = '1px solid red';
                    }

                    try{
                        if(!validarNumericoCme(document.getElementById('txtMesesSolicitables'), 4)){
                            correcto = false;
                            document.getElementById('txtMesesSolicitables').style.border = '1px solid red';
                        }else{
                            document.getElementById('txtMesesSolicitables').removeAttribute('style');
                        }
                    }catch(err){
                        correcto = false;
                        document.getElementById('txtMesesSolicitables').style.border = '1px solid red';
                    }

                    try{
                        if(!validarNumericoDecimalCme(document.getElementById('txtTotSalarioSolicitables'), 10, 2)){
                            correcto = false;
                            document.getElementById('txtTotSalarioSolicitables').style.border = '1px solid red';
                        }else{
                            document.getElementById('txtTotSalarioSolicitables').removeAttribute('style');
                        }
                    }catch(err){
                        correcto = false;
                        document.getElementById('txtTotSalarioSolicitables').style.border = '1px solid red';
                    }

                    if(correcto){
                        pStr = reemplazarTextoCme(pStr, /,/g, '.');
                        mStr = reemplazarTextoCme(mStr, /,/g, '.');
                        sStr = reemplazarTextoCme(sStr, /,/g, '.');
                        var permisos = 0.0;
                        var manu = 0.0;
                        var salario = 0.0;

                        if(pStr != ''){
                            permisos = parseFloat(pStr);
                        }

                        if(mStr != ''){
                            manu = parseFloat(mStr);
                        }

                        if(sStr != ''){
                            salario = parseFloat(sStr);
                        }

                        if (document.getElementById('radioRangoEdadPrevisto1').checked) {
                            salario = salario * 0.85;
                            if (salario > 35700) {
                                salario = 35700;
                            }
                        } else if (document.getElementById('radioRangoEdadPrevisto2').checked) {
                            salario = salario * 0.95;
                            if (salario > 39900) {
                                salario = 39900;
                            }
                        }

                        var solic = permisos + manu + salario;
                        solic = solic.toFixed(2);
                        document.getElementById('txtImporteSolicitables').value = solic;
                        reemplazarPuntosCme(document.getElementById('txtImporteSolicitables'));
                    }else{
                        document.getElementById('txtImporteSolicitables').value = '';
                    }
                }else{
                    document.getElementById('txtImporteSolicitables').value = '';
                }
            }
            
            function calcularMesesContratado(){
                try{
                    var  numMeses=0;
                    //Se calcula restando la fecha de alta de la persona contratada y la fecha de baja de la persona sustituida
                   var feIni = document.getElementById('fechaIniContrato').value;
                   var feFin = document.getElementById('fechaFinContrato').value;
                   if(feIni != null && feIni != '' && feFin != null && feFin != ''){
                      //nuevo
                   var ajax = getXMLHttpRequest();
                    var nodos = null;
                    var CONTEXT_PATH = '<%=request.getContextPath()%>'
                    var url = CONTEXT_PATH + '/PeticionModuloIntegracion.do';
                
                    var parametros = '';
                    var control = new Date();
                    parametros = 'tarea=preparar&modulo=MELANBIDE46&operacion=calcularMeses&numero=<%=numExpediente%>&fini='+feIni+'&ffin='+feFin+'&tipo=0&control='+control.getTime();
                    try{
                        ajax.open("POST",url,false);
                        ajax.setRequestHeader("Content-Type","application/x-www-form-urlencoded; charset=iso-8859-15");
                        ajax.setRequestHeader("Accept", "text/xml, application/xml, text/plain");     
                        //var formData = new FormData(document.getElementById('formContrato'));
                        ajax.send(parametros);
                        if (ajax.readyState==4 && ajax.status==200){
                            var xmlDoc = null;
                            if(navigator.appName.indexOf("Internet Explorer")!=-1){
                                // En IE el XML viene en responseText y no en la propiedad responseXML
                                var text = ajax.responseText;
                                xmlDoc=new ActiveXObject("Microsoft.XMLDOM");
                                xmlDoc.async="false";
                                xmlDoc.loadXML(text);
                            }else{
                                // En el resto de navegadores el XML se recupera de la propiedad responseXML
                                xmlDoc = ajax.responseXML;
                            }//if(navigator.appName.indexOf("Internet Explorer")!=-1)
                        }//if (ajax.readyState==4 && ajax.status==200)
                        nodos = xmlDoc.getElementsByTagName("RESPUESTA");
                        var hijos = nodos[0].childNodes;
                        for(j=0;hijos!=null && j<hijos.length;j++){
                            if(hijos[j].nodeName=="MESES")                            
                               numMeses = hijos[j].childNodes[0].nodeValue;                        
                        }

                    }
                    catch(err){
                        jsp_alerta("A",'<%=meLanbide46I18n.getMensaje(idiomaUsuario,"error.errorProcesarRespuesta")%>');
                    }//try-catch
                    if(!isNaN(numMeses) && numMeses != null && numMeses >= 0 && numMeses <= 99999)
                        document.getElementById('txtMesesContratado').value = numMeses;
                    else
                        document.getElementById('txtMesesContratado').value = '';
                   }
                   else{
                       document.getElementById('txtMesesContratado').value = '';
                   }
                }catch(err){
                    document.getElementById('txtMesesContratado').value = '';
                }
            }
            
            /*function calcularMesesContratado(){
                try{
                    //Se calcula restando la fecha de alta de la persona contratada y la fecha de baja de la persona sustituida
                   var feIni = document.getElementById('fechaIniContrato').value;
                   var feFin = document.getElementById('fechaFinContrato').value;
                   if(feIni != null && feIni != '' && feFin != null && feFin != ''){
                       var numMeses = mesesDiferencia(feIni, feFin);
                       if(!isNaN(numMeses) && numMeses != null && numMeses >= 0 && numMeses <= 99999)
                           document.getElementById('txtMesesContratado').value = numMeses;
                       else
                           document.getElementById('txtMesesContratado').value = '';
                   }
                   else{
                       document.getElementById('txtMesesContratado').value = '';
                   }
                }catch(err){
                    document.getElementById('txtMesesContratado').value = '';
                }
            }*/
            
            function calcularImpTotalSolicitado(campo){
                
                var sStr = document.getElementById('txtSalarioSolicitud').value;
                var dStr = document.getElementById('txtDietasSolicitud').value;
                var tStr = document.getElementById('txtTramitacionSolicitud').value;
                
                if(sStr != '' || dStr != '' || tStr != ''){
                
                    reemplazarPuntosCme(campo);

                    var correcto = true;
                   /* try{
                        if(!validarNumericoDecimalCme(campo.value, 10, 2)){
                            correcto = false;
                            campo.style.border = '1px solid red';
                        }else{
                            campo.removeAttribute('style');
                        }
                    }catch(err){
                        correcto = false;
                        campo.style.border = '1px solid red';
                    }*/

                    try
                    {
                        if(!validarNumericoDecimalCme(document.getElementById('txtSalarioSolicitud'), 10, 2)){
                            correcto = false;
                            document.getElementById('txtSalarioSolicitud').style.border = '1px solid red';
                        }else{
                            document.getElementById('txtSalarioSolicitud').removeAttribute('style');
                        }
                    }catch(err){
                        correcto = false;
                        document.getElementById('txtSalarioSolicitud').style.border = '1px solid red';
                    }

                    try{
                        if(!validarNumericoDecimalCme(document.getElementById('txtDietasSolicitud'), 10, 2)){
                            correcto = false;
                            document.getElementById('txtDietasSolicitud').style.border = '1px solid red';
                        }else{
                            document.getElementById('txtDietasSolicitud').removeAttribute('style');
                        }
                    }catch(err){
                        correcto = false;
                        document.getElementById('txtDietasSolicitud').style.border = '1px solid red';
                    }

                    try{
                        if(!validarNumericoDecimalCme(document.getElementById('txtTramitacionSolicitud'), 10, 2)){
                            correcto = false;
                            document.getElementById('txtTramitacionSolicitud').style.border = '1px solid red';
                        }else{
                            document.getElementById('txtTramitacionSolicitud').removeAttribute('style');
                        }
                    }catch(err){
                        correcto = false;
                        document.getElementById('txtTramitacionSolicitud').style.border = '1px solid red';
                    }

                    if(correcto){
                        sStr = reemplazarTextoCme(sStr, /,/g, '.');
                        dStr = reemplazarTextoCme(dStr, /,/g, '.');
                        tStr = reemplazarTextoCme(tStr, /,/g, '.');
                        var salario = 0.0;
                        var dietas = 0.0;
                        var tramitacion = 0.0;

                        if(sStr != ''){
                            salario = parseFloat(sStr);
                        }

                        if(dStr != ''){
                            dietas = parseFloat(dStr);
                        }

                        if(tStr != ''){
                            tramitacion = parseFloat(tStr);
                        }

                        var solic = salario + dietas + tramitacion;
                        solic = solic.toFixed(2);
                        document.getElementById('txtImpTotalSolicitud').value = solic;
                        reemplazarPuntosCme(document.getElementById('txtImpTotalSolicitud'));
                    }else{
                        document.getElementById('txtImpTotalSolicitud').value = '';
                    }
                }else{
                    document.getElementById('txtImpTotalSolicitud').value = '';
                }
            }
            
            function ajustarDecimalesImportes(){
                var f;
                var v;
                //Salario bruto + SS empresa
                v = document.getElementById('txtSalarioContrato').value;
                v = reemplazarTextoCme(v, /,/g, '.');
                f = parseFloat(v);
                f = ajustarDecimalesCme(f, 2);
                if(!isNaN(f)){
                    document.getElementById('txtSalarioContrato').value = f;
                    reemplazarPuntosCme(document.getElementById('txtSalarioContrato'));
                    document.getElementById('txtSalarioContrato').removeAttribute("style");
                }
                
                //Dietas segun convenio
                v = document.getElementById('txtDietasContrato').value;
                v = reemplazarTextoCme(v, /,/g, '.');
                f = parseFloat(v);
                f = ajustarDecimalesCme(f, 2);
                if(!isNaN(f)){
                    document.getElementById('txtDietasContrato').value = f;
                    reemplazarPuntosCme(document.getElementById('txtDietasContrato'));
                    document.getElementById('txtDietasContrato').removeAttribute("style");
                }
                
                //Coste contratacion
                v = document.getElementById('txtCosteContrato').value;
                v = reemplazarTextoCme(v, /,/g, '.');
                f = parseFloat(v);
                f = ajustarDecimalesCme(f, 2);
                if(!isNaN(f)){
                    document.getElementById('txtCosteContrato').value = f;
                    reemplazarPuntosCme(document.getElementById('txtCosteContrato'));
                    document.getElementById('txtCosteContrato').removeAttribute("style");
                }
                
                //Salario
                v = document.getElementById('txtSalarioSolicitud').value;
                v = reemplazarTextoCme(v, /,/g, '.');
                f = parseFloat(v);
                f = ajustarDecimalesCme(f, 2);
                if(!isNaN(f)){
                    document.getElementById('txtSalarioSolicitud').value = f;
                    reemplazarPuntosCme(document.getElementById('txtSalarioSolicitud'));
                    document.getElementById('txtSalarioSolicitud').removeAttribute("style");
                }
                
                //Dietas
                v = document.getElementById('txtDietasSolicitud').value;
                v = reemplazarTextoCme(v, /,/g, '.');
                f = parseFloat(v);
                f = ajustarDecimalesCme(f, 2);
                if(!isNaN(f)){
                    document.getElementById('txtDietasSolicitud').value = f;
                    reemplazarPuntosCme(document.getElementById('txtDietasSolicitud'));
                    document.getElementById('txtDietasSolicitud').removeAttribute("style");
                }
                
                //Tramitacion seguros medicos y permisos
                v = document.getElementById('txtTramitacionSolicitud').value;
                v = reemplazarTextoCme(v, /,/g, '.');
                f = parseFloat(v);
                f = ajustarDecimalesCme(f, 2);
                if(!isNaN(f)){
                    document.getElementById('txtTramitacionSolicitud').value = f;
                    reemplazarPuntosCme(document.getElementById('txtTramitacionSolicitud'));
                    document.getElementById('txtTramitacionSolicitud').removeAttribute("style");
                }
                
                //Total solicitado
                v = document.getElementById('txtImpTotalSolicitud').value;
                v = reemplazarTextoCme(v, /,/g, '.');
                f = parseFloat(v);
                f = ajustarDecimalesCme(f, 2);
                if(!isNaN(f)){
                    document.getElementById('txtImpTotalSolicitud').value = f;
                    reemplazarPuntosCme(document.getElementById('txtImpTotalSolicitud'));
                    document.getElementById('txtImpTotalSolicitud').removeAttribute("style");
                }
                
                //Total salario
                v = document.getElementById('txtTotSalarioSolicitables').value;
                v = reemplazarTextoCme(v, /,/g, '.');
                f = parseFloat(v);
                f = ajustarDecimalesCme(f, 2);
                if(!isNaN(f)){
                    document.getElementById('txtTotSalarioSolicitables').value = f;
                    reemplazarPuntosCme(document.getElementById('txtTotSalarioSolicitables'));
                    document.getElementById('txtTotSalarioSolicitables').removeAttribute("style");
                }
                
                //Dietas manutencion
                v = document.getElementById('txtDietasSolicitables').value;
                v = reemplazarTextoCme(v, /,/g, '.');
                f = parseFloat(v);
                f = ajustarDecimalesCme(f, 2);
                if(!isNaN(f)){
                    document.getElementById('txtDietasSolicitables').value = f;
                    reemplazarPuntosCme(document.getElementById('txtDietasSolicitables'));
                    document.getElementById('txtDietasSolicitables').removeAttribute("style");
                }
                
                //Tramitacion seguros medicos y permisos
                v = document.getElementById('txtSegurosSolicitables').value;
                v = reemplazarTextoCme(v, /,/g, '.');
                f = parseFloat(v);
                f = ajustarDecimalesCme(f, 2);
                if(!isNaN(f)){
                    document.getElementById('txtSegurosSolicitables').value = f;
                    reemplazarPuntosCme(document.getElementById('txtSegurosSolicitables'));
                    document.getElementById('txtSegurosSolicitables').removeAttribute("style");
                }
                
                //Importe máx. subvencionable
                v = document.getElementById('txtImporteSolicitables').value;
                v = reemplazarTextoCme(v, /,/g, '.');
                f = parseFloat(v);
                f = ajustarDecimalesCme(f, 2);
                if(!isNaN(f)){
                    document.getElementById('txtImporteSolicitables').value = f;
                    reemplazarPuntosCme(document.getElementById('txtImporteSolicitables'));
                    document.getElementById('txtImporteSolicitables').removeAttribute("style");
                }
            }
            
            function buscarTitulacion(id){
                var control = new Date();
                lanzarPopUpModal('<%=request.getContextPath()%>/jsp/extension/melanbide46/buscadorTitulaciones.jsp?codOrganizacionModulo=<%=codOrganizacion%>&control='+control.getTime(),520,960,'yes','no', function(result){
					if (result != undefined && result.length == 3){
						document.getElementById('cod'+id).value = result[0];
						var desc = result[1] != undefined && result[1] != '' ? result[1] : '';
						desc += desc != '' && result[2] != undefined && result[2] != '' ? '-' : '';
						desc += result[2] != undefined && result[2] != '' ? result[2] : '';
						document.getElementById('desc'+id).value = desc.toUpperCase();
					}
				});
            }
        </script>
    </head>
    <body id="cuerpoNuevaSolicitud" style="text-align: left;" >
        <div id="divCuerpo" class="contenidoPantalla" style="overflow-y: auto; padding: 10px;">
        <form  id="formNuevoPuesto">
                <div id="barraProgresoNuevoPuesto" style="position: absolute; z-index:10; visibility: hidden; top: 30%; left: 30%;">
                    <table width="100%" border="0px" cellpadding="0px" cellspacing="0px" border="0px">
                        <tr>
                            <td align="center" valign="middle">
                                <table class="contenedorHidepage" cellpadding="0px" cellspacing="0px" border="0px">
                                    <tr>
                                        <td>
                                            <table width="349px" height="100%">
                                                <tr>
                                                    <td colspan="3" style="height:70%;text-align:center;valign:middle;">
                                                        <span id="msgGuardandoDatos">
                                                            <%=meLanbide46I18n.getMensaje(idiomaUsuario, "msg.guardandoDatos")%>
                                                        </span>
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td style="width:5%;height:20%;"></td>
                                                    <td class="imagenHide"></td>
                                                    <td style="width:5%;height:20%;"></td>
                                                </tr>
                                                <tr>
                                                    <td colspan="3" style="height:10%" ></td>
                                                </tr>
                                            </table>
                                        </td>
                                    </tr>
                                </table>
                            </td>
                        </tr>
                    </table>
                </div>
                <fieldset id="fieldsetDatosPuesto" name="fieldsetDatosPuesto" style="width: 100%;">
                    <legend class="legendAzul"><%=meLanbide46I18n.getMensaje(idiomaUsuario,"legend.puesto.datosPuesto")%></legend>
                    <div class="lineaFormulario">
                        <div style="float: left; width: 42px;">
                            <%=meLanbide46I18n.getMensaje(idiomaUsuario,"label.puesto.datosPuesto.puesto")%>
                        </div>
                        <div style="width: auto; float: left; margin-left: 11px; margin-right: 25px;">
                            <input type="text" maxlength="5" size="5" id="codPuesto" name="codPuesto" value="" class="inputTexto" disabled="true"/>
                            <input type="text" maxlength="200" size="78" id="descPuesto" name="descPuesto" value="" class="inputTexto"/>
                        </div>
                    </div>
                    <div class="lineaFormulario">    
                        <div style="float: left; width: 42px;">
                            <%=meLanbide46I18n.getMensaje(idiomaUsuario,"label.puesto.datosPuesto.pais1")%>
                        </div>
                        <div style="width: auto; float: left; margin-left: 12px; margin-right: 25px;">
                            <input id="codPaisPuesto1" name="codPaisPuesto1" type="text" class="inputTexto" size="2" maxlength="3" 
                                   onkeypress="javascript:return SoloDigitosConsulta(event);" value="">
                            <input id="descPaisPuesto1" name="descPaisPuesto1" type="text" class="inputTexto" size="22" readonly >
                             <a id="anchorPaisPuesto1" name="anchorPaisPuesto1" href=""><span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonPaisPuesto1" name="botonPaisPuesto1" style="cursor:hand;" alt="<%=meLanbide46I18n.getMensaje(idiomaUsuario, "altDesplegable")%>" title="<%=meLanbide46I18n.getMensaje(idiomaUsuario, "altDesplegable")%>"></span></a>
                        </div>
                        <div style="float: left; width: 42px;">
                            <%=meLanbide46I18n.getMensaje(idiomaUsuario,"label.puesto.datosPuesto.pais2")%>
                        </div>
                        <div style="width: auto; float: left; margin-left: 10px; margin-right: 25px;">
                            <input id="codPaisPuesto2" name="codPaisPuesto2" type="text" class="inputTexto" size="2" maxlength="3" 
                                   onkeypress="javascript:return SoloDigitosConsulta(event);" value="" >
                            <input id="descPaisPuesto2" name="descPaisPuesto2" type="text" class="inputTexto" size="22" readonly >
                             <a id="anchorPaisPuesto2" name="anchorPaisPuesto2" href=""><span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonPaisPuesto2" name="botonPaisPuesto2" style="cursor:hand;" alt="<%=meLanbide46I18n.getMensaje(idiomaUsuario, "altDesplegable")%>" title="<%=meLanbide46I18n.getMensaje(idiomaUsuario, "altDesplegable")%>"></span></a>
                        </div>
                        <div style="float: left; width: 42px;">
                            <%=meLanbide46I18n.getMensaje(idiomaUsuario,"label.puesto.datosPuesto.pais3")%>
                        </div>
                        <div style="width: auto; float: left; margin-left: 10px;">
                            <input id="codPaisPuesto3" name="codPaisPuesto3" type="text" class="inputTexto" size="2" maxlength="3" 
                                   onkeypress="javascript:return SoloDigitosConsulta(event);" value="" >
                            <input id="descPaisPuesto3" name="descPaisPuesto3" type="text" class="inputTexto" size="22" readonly >
                             <a id="anchorPaisPuesto3" name="anchorPaisPuesto3" href=""><span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonPaisPuesto3" name="botonPaisPuesto3" style="cursor:hand;" alt="<%=meLanbide46I18n.getMensaje(idiomaUsuario, "altDesplegable")%>" title="<%=meLanbide46I18n.getMensaje(idiomaUsuario, "altDesplegable")%>"></span></a>
                        </div>
                    </div>
                    <div class="lineaFormulario">
                        <div style="float: left; width: 42px;">
                            <%=meLanbide46I18n.getMensaje(idiomaUsuario,"label.puesto.datosPuesto.ciudadDestino")%>
                        </div>
                        <div style="width: auto; float: left; margin-left: 10px; margin-right: 25px;">
                            <input type="text" maxlength="200" size="53" id="ciudadDestino" name="ciudadDestino" value="" class="inputTexto"/>
                        </div>
                        <div style="float: left; width: 42px; margin-right: 9px;">
                            <%=meLanbide46I18n.getMensaje(idiomaUsuario,"label.puesto.datosPuesto.dpto")%>
                        </div>
                        <div style="width: auto; float: left; margin-left: 10px; margin-right: 25px;">
                            <input type="text" maxlength="200" size="53" id="dpto" name="dpto" value="" class="inputTexto"/>
                        </div>
                    </div>
                    <div class="lineaFormulario">        
                        <div style="float: left; width: 78px;">
                            <%=meLanbide46I18n.getMensaje(idiomaUsuario,"label.puesto.datosPuesto.titulacion1")%>
                        </div>
                        <div style="width: auto; float: left; margin-left: 10px; margin-right: 25px;">
                            <input id="descTitulacionPuesto1" name="descTitulacionPuesto1" type="text" class="inputTexto" size="80" readonly >
                            <input type="button" id="btnBuscarTitulacionPuesto1" name="btnBuscarTitulacionPuesto1" class="botonGeneral" value="<%=meLanbide46I18n.getMensaje(idiomaUsuario,"btn.buscar")%>..." onclick="buscarTitulacion('TitulacionPuesto1');">
                        </div>  
                        <div style="float: left; width: 94px; display: none;">
                            <%=meLanbide46I18n.getMensaje(idiomaUsuario,"label.puesto.datosPuesto.nivelFormativo")%>
                        </div>
                        <div style="width: auto; float: left; margin-left: 2px; display: none;">
                            <input id="codNivelFormativo" name="codNivelFormativo" type="text" class="inputTexto" size="2" maxlength="8" 
                                   onkeypress="javascript:return SoloDigitosConsulta(event);" value="" >
                            <input id="descNivelFormativo" name="descNivelFormativo" type="text" class="inputTexto" size="1" readonly >
                             <a id="anchorNivelFormativo" name="anchorNivelFormativo" href=""><span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonNivelFormativo" name="botonNivelFormativo" style="cursor:hand;" alt="<%=meLanbide46I18n.getMensaje(idiomaUsuario, "altDesplegable")%>" title="<%=meLanbide46I18n.getMensaje(idiomaUsuario, "altDesplegable")%>"></span></a>
                        </div> 
                    </div>
                    <div class="lineaFormulario">   
                        <div style="float: left; width: 78px;">
                            <%=meLanbide46I18n.getMensaje(idiomaUsuario,"label.puesto.datosPuesto.titulacion2")%>
                        </div>
                        <div style="width: auto; float: left; margin-left: 10px; margin-right: 25px;">
                            <input id="descTitulacionPuesto2" name="descTitulacionPuesto2" type="text" class="inputTexto" size="80" readonly >
                            <input type="button" id="btnBuscarTitulacionPuesto2" name="btnBuscarTitulacionPuesto2" class="botonGeneral" value="<%=meLanbide46I18n.getMensaje(idiomaUsuario,"btn.buscar")%>..." onclick="buscarTitulacion('TitulacionPuesto2');">
                        </div>   
                    </div>
                    <div class="lineaFormulario">   
                        <div style="float: left; width: 78px;">
                            <%=meLanbide46I18n.getMensaje(idiomaUsuario,"label.puesto.datosPuesto.titulacion3")%>
                        </div>
                        <div style="width: auto; float: left; margin-left: 10px;">
                            <input id="descTitulacionPuesto3" name="descTitulacionPuesto3" type="text" class="inputTexto" size="80" readonly >
                            <input type="button" id="btnBuscarTitulacionPuesto3" name="btnBuscarTitulacionPuesto3" class="botonGeneral" value="<%=meLanbide46I18n.getMensaje(idiomaUsuario,"btn.buscar")%>..." onclick="buscarTitulacion('TitulacionPuesto3');">
                        </div>
                    </div>
                    <div class="lineaFormulario">      
                        <div style="float: left; width: 78px;">
                            <%=meLanbide46I18n.getMensaje(idiomaUsuario,"label.puesto.datosPuesto.funciones")%>
                        </div>
                        <div style="width: auto; float: left; margin-left: 10px;">
                            <textarea rows="4" cols="115" id="funcionesPuesto" name="funcionesPuesto" maxlength="1000" class="inputTexto"><%=puestoModif != null && puestoModif.getFunciones() != null ? puestoModif.getFunciones().toUpperCase() : "" %></textarea>
                        </div>
                    </div>
                    <div class="lineaFormulario">
                        <div style="float: left; width: 78px;">
                            <%=meLanbide46I18n.getMensaje(idiomaUsuario,"label.puesto.datosPuesto.idioma")%>
                        </div>
                        <div style="width: auto; float: left; margin-left: 10px; margin-right: 25px;">
                            <input id="codIdiomaPuesto1" name="codIdiomaPuesto1" type="text" class="inputTexto" size="2" maxlength="8" 
                                   onkeypress="javascript:return SoloDigitosConsulta(event);" value="" >
                            <input id="descIdiomaPuesto1" name="descIdiomaPuesto1" type="text" class="inputTexto" size="17" readonly >
                             <a id="anchorIdiomaPuesto1" name="anchorIdiomaPuesto1" href=""><span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonIdiomaPuesto1" name="botonIdiomaPuesto1" style="cursor:hand;" alt="<%=meLanbide46I18n.getMensaje(idiomaUsuario, "altDesplegable")%>" title="<%=meLanbide46I18n.getMensaje(idiomaUsuario, "altDesplegable")%>"></span></a>
                        </div>
                        <div style="float: left; width: 78px;">
                            <%=meLanbide46I18n.getMensaje(idiomaUsuario,"label.puesto.datosPuesto.nivelIdioma")%>
                        </div>
                        <div style="width: auto; float: left; margin-left: 7px;">
                            <input id="codNivelIdiomaPuesto1" name="codNivelIdiomaPuesto1" type="text" class="inputTexto" size="2" maxlength="8" 
                                   onkeypress="javascript:return SoloDigitosConsulta(event);" value="">
                            <input id="descNivelIdiomaPuesto1" name="descNivelIdiomaPuesto1" type="text" class="inputTexto" size="17" readonly >
                             <a id="anchorNivelIdiomaPuesto1" name="anchorNivelIdiomaPuesto1" href=""><span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonNivelIdiomaPuesto1" name="botonNivelIdiomaPuesto1" style="cursor:hand;" alt="<%=meLanbide46I18n.getMensaje(idiomaUsuario, "altDesplegable")%>" title="<%=meLanbide46I18n.getMensaje(idiomaUsuario, "altDesplegable")%>"></span></a>
                        </div>
                    </div>
                    <div class="lineaFormulario">
                        <div style="float: left; width: 78px;">
                            <%=meLanbide46I18n.getMensaje(idiomaUsuario,"label.puesto.datosPuesto.idioma")%>
                        </div>
                        <div style="width: auto; float: left; margin-left: 10px; margin-right: 25px;">
                            <input id="codIdiomaPuesto2" name="codIdiomaPuesto2" type="text" class="inputTexto" size="2" maxlength="8" 
                                   onkeypress="javascript:return SoloDigitosConsulta(event);" value="" >
                            <input id="descIdiomaPuesto2" name="descIdiomaPuesto2" type="text" class="inputTexto" size="17" readonly >
                             <a id="anchorIdiomaPuesto2" name="anchorIdiomaPuesto2" href=""><span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonIdiomaPuesto2" name="botonIdiomaPuesto2" style="cursor:hand;" alt="<%=meLanbide46I18n.getMensaje(idiomaUsuario, "altDesplegable")%>" title="<%=meLanbide46I18n.getMensaje(idiomaUsuario, "altDesplegable")%>"></span></a>
                        </div>
                        <div style="float: left; width: 78px;">
                            <%=meLanbide46I18n.getMensaje(idiomaUsuario,"label.puesto.datosPuesto.nivelIdioma")%>
                        </div>
                        <div style="width: auto; float: left; margin-left: 7px;">
                            <input id="codNivelIdiomaPuesto2" name="codNivelIdiomaPuesto2" type="text" class="inputTexto" size="2" maxlength="8" 
                                   onkeypress="javascript:return SoloDigitosConsulta(event);" value="" >
                            <input id="descNivelIdiomaPuesto2" name="descNivelIdiomaPuesto2" type="text" class="inputTexto" size="17" readonly >
                             <a id="anchorNivelIdiomaPuesto2" name="anchorNivelIdiomaPuesto2" href=""><span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonNivelIdiomaPuesto2" name="botonNivelIdiomaPuesto2" style="cursor:hand;" alt="<%=meLanbide46I18n.getMensaje(idiomaUsuario, "altDesplegable")%>" title="<%=meLanbide46I18n.getMensaje(idiomaUsuario, "altDesplegable")%>"></span></a>
                        </div>
                    </div>
                    <div class="lineaFormulario">
                        <div style="float: left; width: 78px;">
                            <%=meLanbide46I18n.getMensaje(idiomaUsuario,"label.puesto.datosPuesto.idioma")%>
                        </div>
                        <div style="width: auto; float: left; margin-left: 10px; margin-right: 25px;">
                            <input id="codIdiomaPuesto3" name="codIdiomaPuesto3" type="text" class="inputTexto" size="2" maxlength="8" 
                                   onkeypress="javascript:return SoloDigitosConsulta(event);" value="" >
                            <input id="descIdiomaPuesto3" name="descIdiomaPuesto3" type="text" class="inputTexto" size="17" readonly >
                             <a id="anchorIdiomaPuesto3" name="anchorIdiomaPuesto3" href=""><span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonIdiomaPuesto3" name="botonIdiomaPuesto3" style="cursor:hand;" alt="<%=meLanbide46I18n.getMensaje(idiomaUsuario, "altDesplegable")%>" title="<%=meLanbide46I18n.getMensaje(idiomaUsuario, "altDesplegable")%>"></span></a>
                        </div>
                        <div style="float: left; width: 78px;">
                            <%=meLanbide46I18n.getMensaje(idiomaUsuario,"label.puesto.datosPuesto.nivelIdioma")%>
                        </div>
                        <div style="width: auto; float: left; margin-left: 7px;">
                            <input id="codNivelIdiomaPuesto3" name="codNivelIdiomaPuesto3" type="text" class="inputTexto" size="2" maxlength="8" 
                                   onkeypress="javascript:return SoloDigitosConsulta(event);" value="" >
                            <input id="descNivelIdiomaPuesto3" name="descNivelIdiomaPuesto3" type="text" class="inputTexto" size="17" readonly >
                             <a id="anchorNivelIdiomaPuesto3" name="anchorNivelIdiomaPuesto3" href=""><span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonNivelIdiomaPuesto3" name="botonNivelIdiomaPuesto3" style="cursor:hand;" alt="<%=meLanbide46I18n.getMensaje(idiomaUsuario, "altDesplegable")%>" title="<%=meLanbide46I18n.getMensaje(idiomaUsuario, "altDesplegable")%>"></span></a>
                        </div>
                    </div>
                </fieldset>
                <fieldset id="fieldsetDatosContrato" name="fieldsetDatosContrato" style="width: 100%;">
                    <legend class="legendAzul"><%=meLanbide46I18n.getMensaje(idiomaUsuario,"legend.puesto.datosContrato")%></legend>
                    <div class="lineaFormulario">
                        <div style="float: left; width: 90px;">
                            <%=meLanbide46I18n.getMensaje(idiomaUsuario,"label.puesto.datosContrato.fecIni")%>
                        </div>
                        <div style="width: auto; float: left; margin-left: 10px; margin-right: 107px;">
                            <input type="text" class="inputTxtFecha" size="10" maxlength="10" id="fechaIniContrato" name="fechaIniContrato" onkeyup="return SoloCaracteresFecha(this);" onblur="if(comprobarFechaCme(this, '<%=meLanbide46I18n.getMensaje(idiomaUsuario, "msg.fechaNoVal")%>')){calcularMesesContratado();}" onfocus="this.select();" value=""/>
                            <A href="javascript:calClick(event);" onclick="mostrarCalFechaIniContrato(event);return false;" style="text-decoration:none;" >
                                <IMG style="border: 0" height="17" id="calfechaIniContrato" name="calfechaIniContrato" alt="<%=meLanbide46I18n.getMensaje(idiomaUsuario,"label.puesto.datosContrato.fecIni")%>" src="<c:url value='/images/calendario/icono.gif'/>"/>
                            </A>
                        </div>
                        <div style="float: left; width: 90px;">
                            <%=meLanbide46I18n.getMensaje(idiomaUsuario,"label.puesto.datosContrato.fecFin")%>
                        </div>
                        <div style="width: auto; float: left; margin-left: 10px;">
                            <input type="text" class="inputTxtFecha" size="10" maxlength="10" id="fechaFinContrato" name="fechaFinContrato" onkeyup="return SoloCaracteresFecha(this);" onblur="if(comprobarFechaCme(this, '<%=meLanbide46I18n.getMensaje(idiomaUsuario, "msg.fechaNoVal")%>')){calcularMesesContratado();}" onfocus="this.select();" value=""/>
                            <A href="javascript:calClick(event);" onclick="mostrarCalFechaFinContrato(event);return false;" style="text-decoration:none;" >
                                <IMG style="border: 0" height="17" id="calfechaFinContrato" name="calfechaFinContrato" alt="<%=meLanbide46I18n.getMensaje(idiomaUsuario,"label.puesto.datosContrato.fecFin")%>" src="<c:url value='/images/calendario/icono.gif'/>"/>
                            </A>
                        </div>
                        <div style="float: left; width: 80px; margin-left: 60px;">
                            <%=meLanbide46I18n.getMensaje(idiomaUsuario,"label.puesto.datosContrato.duracionMeses")%>
                        </div>
                        <div style="width: auto; float: left; margin-left: 10px;">
                            <input type="text" class="inputTexto textoNumerico" size="10" maxlength="10" id="txtMesesContratado" name="txtMesesContratado" value="" onChange="calcularMesesContratado();"/>
                        </div>
                    </div>
                    <div class="lineaFormulario">
                        <div style="float: left; width: 90px;">
                            <%=meLanbide46I18n.getMensaje(idiomaUsuario,"label.puesto.datosContrato.grupoCot")%>
                        </div>
                        <div style="width: auto; float: left; margin-left: 10px; margin-right: 20px;">
                            <input id="codGrupoContrato" name="codGrupoContrato" type="text" class="inputTexto" size="2" maxlength="8" 
                                   onkeypress="javascript:return SoloDigitosConsulta(event);" value="" >
                            <input id="descGrupoContrato" name="descGrupoContrato" type="text" class="inputTexto" size="20" readonly >
                             <a id="anchorGrupoContrato" name="anchorGrupoContrato" href=""><span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonGrupoContrato" name="botonGrupoContrato" style="cursor:hand;" alt="<%=meLanbide46I18n.getMensaje(idiomaUsuario, "altDesplegable")%>" title="<%=meLanbide46I18n.getMensaje(idiomaUsuario, "altDesplegable")%>"></span></a>
                        </div>
                    </div>
                    <div class="lineaFormulario">
                        <div style="float: left; width: 90px;">
                            <%=meLanbide46I18n.getMensaje(idiomaUsuario,"label.puesto.datosContrato.convenio")%>
                        </div>
                        <div style="width: auto; float: left; margin-left: 10px;">
                            <textarea rows="4" cols="110" id="txtConvenioContrato" name="txtConvenioContrato" maxlength="500" class="inputTexto"><%=puestoModif != null && puestoModif.getConvenioCol() != null ? puestoModif.getConvenioCol().toUpperCase() : "" %></textarea>
                        </div>
                    </div>
                    <div class="lineaFormulario">
                        <div style="float: left; width: 128px;">
                            <%=meLanbide46I18n.getMensaje(idiomaUsuario,"label.puesto.datosContrato.salario")%>
                        </div>
                        <div style="width: auto; float: left; margin-left: 10px; margin-right: 10px;">
                            <input type="text" maxlength="11" size="18" id="txtSalarioContrato" name="txtSalarioContrato" value="" class="inputTexto textoNumerico" onkeyup="calcularCosteContratacion(this);" onblur="calcularCosteContratacion(this);ajustarDecimalesImportes();"/>
                        </div>
                        <div style="float: left; width: 90px;">
                            <%=meLanbide46I18n.getMensaje(idiomaUsuario,"label.puesto.datosContrato.dietas")%>
                        </div>
                        <div style="width: auto; float: left; margin-left: 10px; margin-right: 20px;">
                            <input type="text" maxlength="11" size="18" id="txtDietasContrato" name="txtDietasContrato" value="" class="inputTexto textoNumerico" onkeyup="calcularCosteContratacion(this);" onblur="calcularCosteContratacion(this);ajustarDecimalesImportes();"/>
                        </div>
                        <div style="float: left; width: 90px; margin-right: 2px;">
                            <%=meLanbide46I18n.getMensaje(idiomaUsuario,"label.puesto.datosContrato.coste")%>
                        </div>
                        <div style="width: auto; float: left; margin-left: 10px;">
                            <input type="text" maxlength="11" size="18" id="txtCosteContrato" name="txtCosteContrato" value="" disabled="true" class="inputTexto textoNumerico"/>
                        </div>
                    </div>
                    <div class="lineaFormulario">      
                        <div style="float: left; width: 90px;">
                            <%=meLanbide46I18n.getMensaje(idiomaUsuario,"label.puesto.datosContrato.otrosBeneficios")%>
                        </div>
                        <div style="width: auto; float: left; margin-left: 10px;">
                            <textarea rows="4" cols="110" id="otrosBeneficiosContrato" name="otrosBeneficiosContrato" maxlength="1000" class="inputTexto"><%=puestoModif != null && puestoModif.getOtrosBenef() != null ? puestoModif.getOtrosBenef().toUpperCase() : "" %></textarea>
                        </div>
                    </div>
                </fieldset>
                <fieldset id="fieldsetDatosSolicitud" name="fieldsetDatosSolicitud" style="width: 100%;">
                    <legend class="legendAzul"><%=meLanbide46I18n.getMensaje(idiomaUsuario,"legend.puesto.datosSolicitud")%></legend>
                    <div class="lineaFormulario">
                        <div style="float: left; width: 180px;">
                            <%=meLanbide46I18n.getMensaje(idiomaUsuario,"label.puesto.datosSolicitud.salario")%>
                        </div>
                        <div style="width: auto; float: left; margin-left: 10px; margin-right: 42px;">
                            <input type="text" maxlength="11" size="8" id="txtSalarioSolicitud" name="txtSalarioSolicitud" value="" class="inputTexto textoNumerico" onkeyup="calcularImpTotalSolicitado(this);" onblur="calcularImpTotalSolicitado(this); ajustarDecimalesImportes();"/>
                        </div>
                        <div style="float: left; width: 100px;">
                            <%=meLanbide46I18n.getMensaje(idiomaUsuario,"label.puesto.datosSolicitud.dietas")%>
                        </div>
                        <div style="width: auto; float: left; margin-left: 10px; margin-right: 42px;">
                            <input type="text" maxlength="11" size="8" id="txtDietasSolicitud" name="txtDietasSolicitud" value="" class="inputTexto textoNumerico" onkeyup="calcularImpTotalSolicitado(this);" onblur="calcularImpTotalSolicitado(this); ajustarDecimalesImportes();"/>
                        </div>
                        <div style="float: left; width: 130px;">
                            <%=meLanbide46I18n.getMensaje(idiomaUsuario,"label.puesto.datosSolicitud.tramitacion")%>
                        </div>
                        <div style="width: auto; float: left; margin-left: 10px; margin-right: 42px;">
                            <input type="text" maxlength="11" size="8" id="txtTramitacionSolicitud" name="txtTramitacionSolicitud" value="" class="inputTexto textoNumerico" onkeyup="calcularImpTotalSolicitado(this);" onblur="calcularImpTotalSolicitado(this); ajustarDecimalesImportes();"/>
                        </div>
                    </div>
                    <div class="lineaFormulario">
                        <div style="float: left; width: 180px;">
                            <%=meLanbide46I18n.getMensaje(idiomaUsuario,"label.puesto.datosSolicitud.impTotal")%>
                        </div>
                        <div style="width: auto; float: left; margin-left: 10px; margin-right: 42px;">
                            <input type="text" maxlength="11" size="8" id="txtImpTotalSolicitud" name="txtImpTotalSolicitud" value="" class="inputTexto readOnly textoNumerico" readonly="true"/>
                        </div>
                    </div>
                </fieldset>
                <fieldset id="fieldsetDatosSolicitables" name="fieldsetDatosSolicitables" style="width: 100%;">
                    <legend class="legendAzul"><%=meLanbide46I18n.getMensaje(idiomaUsuario,"legend.puesto.datosSubvencionables")%></legend>
<%--
                    <div class="lineaFormulario">
                        <div style="float: left; width: 90px;">
                            <%=meLanbide46I18n.getMensaje(idiomaUsuario,"label.puesto.datosSolicitables.nivelFormativo")%>
                        </div>
                        <div style="width: auto; float: left; margin-left: 10px; margin-right: 22px;">
                            <input id="codGrupoSolicitables" name="codGrupoSolicitables" type="text" class="inputTexto" size="2" maxlength="3" 
                                   onkeypress="javascript:return SoloDigitosConsulta(event);" value="" disabled="true">
                            <input id="descGrupoSolicitables" name="descGrupoSolicitables" type="text" class="inputTexto" size="17" readonly disabled="true">
                        </div>
                        <div style="float: left; width: 42px;">
                            <%=meLanbide46I18n.getMensaje(idiomaUsuario,"label.puesto.datosSolicitables.pais1")%>
                        </div>
                        <div style="width: auto; float: left; margin-left: 10px; margin-right: 42px;">
                            <input id="codPaisSolicitables1" name="codPaisSolicitables1" type="text" class="inputTexto" size="2" maxlength="3" 
                                   onkeypress="javascript:return SoloDigitosConsulta(event);" value="" disabled="true">
                            <input id="descPaisSolicitables1" name="descPaisSolicitables1" type="text" class="inputTexto" size="25" readonly disabled="true">
                        </div>
                        <div style="float: left; width: 150px; margin-right: 2px;">
                            <%=meLanbide46I18n.getMensaje(idiomaUsuario,"label.puesto.datosSolicitables.salario")%>
                        </div>
                        <div style="width: auto; float: left; margin-left: 10px;">
                            <input type="text" maxlength="15" size="8" id="txtSalarioSolicitables1" name="txtSalarioSolicitables1" value="" class="inputTexto textoNumerico" onkeyup="calcularTotalSalarioAnexo1();" onblur="calcularTotalSalarioAnexo1(); ajustarDecimalesImportes();"/>
                        </div>
                    </div>
                    <div class="lineaFormulario">
                        <div style="float: left; width: 42px; margin-left: 312px;">
                            <%=meLanbide46I18n.getMensaje(idiomaUsuario,"label.puesto.datosSolicitables.pais2")%>
                        </div>
                        <div style="width: auto; float: left; margin-left: 10px; margin-right: 42px;">
                            <input id="codPaisSolicitables2" name="codPaisSolicitables2" type="text" class="inputTexto" size="2" maxlength="3" 
                                   onkeypress="javascript:return SoloDigitosConsulta(event);" value="" disabled="true">
                            <input id="descPaisSolicitables2" name="descPaisSolicitables2" type="text" class="inputTexto" size="25" readonly disabled="true">
                        </div>
                        <div style="float: left; width: 150px; margin-right: 2px;">
                            <%=meLanbide46I18n.getMensaje(idiomaUsuario,"label.puesto.datosSolicitables.salario")%>
                        </div>
                        <div style="width: auto; float: left; margin-left: 10px;">
                            <input type="text" maxlength="15" size="8" id="txtSalarioSolicitables2" name="txtSalarioSolicitables2" value="" class="inputTexto textoNumerico" onkeyup="calcularTotalSalarioAnexo1();" onblur="calcularTotalSalarioAnexo1(); ajustarDecimalesImportes();"/>
                        </div>
                    </div>
                    <div class="lineaFormulario">
                        <div style="float: left; width: 42px; margin-left: 312px;">
                            <%=meLanbide46I18n.getMensaje(idiomaUsuario,"label.puesto.datosSolicitables.pais3")%>
                        </div>
                        <div style="width: auto; float: left; margin-left: 10px; margin-right: 42px;">
                            <input id="codPaisSolicitables3" name="codPaisSolicitables3" type="text" class="inputTexto" size="2" maxlength="3" 
                                   onkeypress="javascript:return SoloDigitosConsulta(event);" value="" disabled="true">
                            <input id="descPaisSolicitables3" name="descPaisSolicitables3" type="text" class="inputTexto" size="25" readonly disabled="true">
                        </div>
                        <div style="float: left; width: 150px; margin-right: 2px;">
                            <%=meLanbide46I18n.getMensaje(idiomaUsuario,"label.puesto.datosSolicitables.salario")%>
                        </div>
                        <div style="width: auto; float: left; margin-left: 10px;">
                            <input type="text" maxlength="15" size="8" id="txtSalarioSolicitables3" name="txtSalarioSolicitables3" value="" class="inputTexto textoNumerico" onkeyup="calcularTotalSalarioAnexo1();" onblur="calcularTotalSalarioAnexo1(); ajustarDecimalesImportes();"/>
                        </div>
                    </div>
                    <div class="lineaFormulario">
                        <div style="float: left; width: 150px; margin-right: 2px;">
                            <%=meLanbide46I18n.getMensaje(idiomaUsuario,"label.puesto.datosSolicitables.totSalario")%>
                        </div>
                        <div style="width: auto; float: left; margin-left: 24px;">
                            <input type="text" maxlength="15" size="8" id="txtTotSalarioSolicitables" name="txtTotSalarioSolicitables" value="" class="inputTexto readOnly textoNumerico" readonly="true"/>
                        </div>
                    </div>
--%>
                    <div class="lineaFormulario">
                        <div style="float: left; width: 180px;">
                            <%=meLanbide46I18n.getMensaje(idiomaUsuario,"label.puesto.datosSolicitables.rangoEdadPrevisto")%>
                        </div>
                        <div style="width: auto; float: left; margin-left: 35px; margin-right: 45px;">
                            <%=meLanbide46I18n.getMensaje(idiomaUsuario,"label.puesto.datosSolicitables.rangoEdadPrevisto35")%>
                            <input type="radio" id="radioRangoEdadPrevisto1" name="radioRangoEdadPrevisto" value="35" onclick="calcularImporteMaxSubv()" onchange="calcularImporteMaxSubv()"/>
                        </div>
                        <div style="float: left; width: 182px;">
                            <%=meLanbide46I18n.getMensaje(idiomaUsuario,"label.puesto.datosSolicitables.rangoEdadPrevisto45")%>
                            <input type="radio" id="radioRangoEdadPrevisto2" name="radioRangoEdadPrevisto" value="45" onclick="calcularImporteMaxSubv()" onchange="calcularImporteMaxSubv()"/>
                        </div>
                        <div style="float: left; width: 204px; text-align: right;">
                            <%=meLanbide46I18n.getMensaje(idiomaUsuario,"label.puesto.datosSolicitables.impBrutoSegSocial")%>
                        </div>
                        <div style="width: auto; float: left; margin-left: 10px;">
                            <input type="text" maxlength="15" size="8" id="txtTotSalarioSolicitables" name="txtTotSalarioSolicitables" value="" disabled="true" class="inputTexto textoNumerico"/>
                        </div>
                    </div>
                    <div class="lineaFormulario">
                    </div>
                    <div class="lineaFormulario">
                        <div style="float: left; width: 180px;">
                            <%=meLanbide46I18n.getMensaje(idiomaUsuario,"label.puesto.datosSolicitables.meses")%>
                        </div>
                        <div style="width: auto; float: left; margin-left: 10px; margin-right: 22px;">
                            <input type="text" maxlength="4" size="8" id="txtMesesSolicitables" name="txtMesesSolicitables" value="" class="inputTexto textoNumerico" onkeyup="calcularDietasManutencion();" onblur="calcularDietasManutencion(); ajustarDecimalesImportes();"/>
                        </div>
                        <div style="float: left; width: 182px;">
                            <%=meLanbide46I18n.getMensaje(idiomaUsuario,"label.puesto.datosSolicitables.dietas")%>
                        </div>
                        <div style="width: auto; float: left; margin-left: 9px; margin-right: 42px;">
                            <input type="text" maxlength="15" size="8" id="txtDietasSolicitables" name="txtDietasSolicitables" value="" class="inputTexto textoNumerico" disabled="true"/>
                        </div>
                        <div style="float: left; width: 152px;">
                            <%=meLanbide46I18n.getMensaje(idiomaUsuario,"label.puesto.datosSolicitables.tramSeguros")%>
                        </div>
                        <div style="width: auto; float: left; margin-left: 10px;">
                            <input type="text" maxlength="15" size="8" id="txtSegurosSolicitables" name="txtSegurosSolicitables" value="" class="inputTexto textoNumerico" onkeyup="calcularImporteMaxSubv();" onblur="calcularImporteMaxSubv(); ajustarDecimalesImportes();"/>
                        </div>
                    </div>
                    <div class="lineaFormulario" style="text-align: right;">
                        <div style="float: left; width: 180px; text-align: left;">
                            <%=meLanbide46I18n.getMensaje(idiomaUsuario,"label.puesto.datosSolicitables.impMaxSubv")%>
                        </div>
                        <div style="width: auto; float: left; margin-left: 10px;">
                            <input type="text" maxlength="15" size="8" id="txtImporteSolicitables" name="txtImporteSolicitables" value="" disabled="true" class="inputTexto textoNumerico"/>
                        </div>
                    </div>
                </fieldset>
                <fieldset id="fieldsetResumenPuesto" name="fieldsetResumenPuesto" style="width: 100%;">
                    <legend class="legendAzul"><%=meLanbide46I18n.getMensaje(idiomaUsuario,"legend.puesto.datosResumenPuesto")%></legend>
                    <div class="lineaFormulario">
                        <div style="float: left; width: 90px;">
                            <%=meLanbide46I18n.getMensaje(idiomaUsuario,"label.puesto.datosResumenPuesto.resultado")%>
                        </div>
                        <div style="width: auto; float: left; margin-left: 11px; margin-right: 20px;">
                            <input id="codResultadoResumen" name="codResultadoResumen" type="text" class="inputTexto" size="2" maxlength="8" 
                                   onkeypress="javascript:return SoloDigitosConsulta(event);" value="" >
                            <input id="descResultadoResumen" name="descResultadoResumen" type="text" class="inputTexto" size="20" readonly >
                             <a id="anchorResultadoResumen" name="anchorResultadoResumen" href=""><span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonResultadoResumen" name="botonResultadoResumen" style="cursor:hand;" alt="<%=meLanbide46I18n.getMensaje(idiomaUsuario, "altDesplegable")%>" title="<%=meLanbide46I18n.getMensaje(idiomaUsuario, "altDesplegable")%>"></span></a>
                        </div>
                        <div id="divLabelMotivoResumen" style="float: left; width: 40px;">
                            <%=meLanbide46I18n.getMensaje(idiomaUsuario,"label.puesto.datosResumenPuesto.motivo")%>
                        </div>
                        <div id="divComboMotivoResumen" style="width: auto; float: left; margin-left: 10px; margin-right: 65px;">
                            <input id="codMotivoResumen" name="codMotivoResumen" type="text" class="inputTexto" size="2" maxlength="8" 
                                   onkeypress="javascript:return SoloDigitosConsulta(event);" value="" >
                            <input id="descMotivoResumen" name="descMotivoResumen" type="text" class="inputTexto" size="20" readonly >
                             <a id="anchorMotivoResumen" name="anchorMotivoResumen" href=""><span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonMotivoResumen" name="botonMotivoResumen" style="cursor:hand;" alt="<%=meLanbide46I18n.getMensaje(idiomaUsuario, "altDesplegable")%>" title="<%=meLanbide46I18n.getMensaje(idiomaUsuario, "altDesplegable")%>"></span></a>
                        </div>
                    </div>
                    <div class="lineaFormulario">      
                        <div style="float: left; width: 90px;">
                            <%=meLanbide46I18n.getMensaje(idiomaUsuario,"label.puesto.datosResumenPuesto.observaciones")%>
                        </div>
                        <div style="width: auto; float: left; margin-left: 10px;">
                            <textarea rows="4" cols="110" id="observacionesResumen" name="observacionesResumen" maxlength="1000" class="inputTexto"><%=puestoModif != null && puestoModif.getObservaciones() != null ? puestoModif.getObservaciones().toUpperCase() : "" %></textarea>
                        </div>
                    </div>
                </fieldset>
                <div class="botonera">
                    <input type="button" id="btnGuardarContrato" name="btnGuardarContrato" class="botonGeneral" value="<%=meLanbide46I18n.getMensaje(idiomaUsuario,"btn.aceptar")%>" onclick="guardar();">
                    <input type="button" id="btnCancelarContrato" name="btnCancelarContrato" class="botonGeneral" value="<%=meLanbide46I18n.getMensaje(idiomaUsuario,"btn.cancelar")%>" onclick="cancelar();">
                    <input type="button" id="btnCerrar" name="btnCerrar" class="botonGeneral" value="<%=meLanbide46I18n.getMensaje(idiomaUsuario,"btn.cerrar")%>" onclick="cerrarVentana();" style="display: none;">
                </div>
                <input type="hidden" id="codTitulacionPuesto1" name="codTitulacionPuesto1"/>
                <input type="hidden" id="codTitulacionPuesto2" name="codTitulacionPuesto2"/>
                <input type="hidden" id="codTitulacionPuesto3" name="codTitulacionPuesto3"/>
        </form>
        <div id="popupcalendar" class="text"></div>
        </div>
        <script type="text/javascript">
            //Combos datos puesto
            var comboPaisPuesto1;
            var comboPaisPuesto2;
            var comboPaisPuesto3;
            var comboIdiomaPuesto1;
            var comboNivelIdiomaPuesto1;
            var comboIdiomaPuesto2;
            var comboNivelIdiomaPuesto2;
            var comboIdiomaPuesto3;
            var comboNivelIdiomaPuesto3;
            var comboNivelFormativo;
            
            //Combos datos contrato
            var comboGrupoContrato;
            
            //Combos resumen puesto
            var comboResultadoResumen;
            var comboMotivoResumen;
            
            
            inicio();
        </script>
    </body>
</html>

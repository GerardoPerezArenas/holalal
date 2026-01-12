<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>

<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide59.i18n.MeLanbide59I18n" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide59.vo.CpePuestoVO" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide59.vo.CpeOfertaVO" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide59.vo.CpeJustificacionVO" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide59.vo.FilaPersonaContratadaVO" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide59.util.ConstantesMeLanbide59" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide59.vo.SelectItem"%>
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
            //Clase para internacionalizar los mensajes de la aplicaci�n.
            MeLanbide59I18n meLanbide59I18n = MeLanbide59I18n.getInstance();

            Config m_Config = ConfigServiceHelper.getConfig("common");
            String statusBar = m_Config.getString("JSP.StatusBar");
            String nombreModulo     = request.getParameter("nombreModulo");
            String codOrganizacion  = request.getParameter("codOrganizacionModulo");
            String numExpediente    = request.getParameter("numero");

            CpePuestoVO puestoConsulta = (CpePuestoVO)request.getAttribute("puestoConsulta");
            CpeOfertaVO ofertaConsulta = (CpeOfertaVO)request.getAttribute("ofertaConsulta");
            CpeJustificacionVO justifModif = (CpeJustificacionVO)request.getAttribute("justifModif");
            String concedido = (String)request.getAttribute("concedido");
            String renunciado = (String)request.getAttribute("renunciado");
            
            String descTit1 = (String)request.getAttribute("descTit1");
            String descTit2 = (String)request.getAttribute("descTit2");
            String descTit3 = (String)request.getAttribute("descTit3");
            
            Boolean consulta = request.getAttribute("consulta") != null ? (Boolean)request.getAttribute("consulta") : false;
            SimpleDateFormat format = new SimpleDateFormat(ConstantesMeLanbide59.FORMATO_FECHA);

            String tituloPagina = "";
            /*if(consulta)
            {
                tituloPagina = meLanbide59I18n.getMensaje(idiomaUsuario,"label.justif.titulo.consultaJustif");
            }
            else
            {
                tituloPagina = meLanbide59I18n.getMensaje(idiomaUsuario, "label.justif.titulo.nuevaJustif");
            }*/
            
            tituloPagina = meLanbide59I18n.getMensaje(idiomaUsuario,"label.justif.titulo.datosJustif");
            
            //COMBOS

            // Paises
            List<SelectItem> listaPaises = new ArrayList<SelectItem>();
            if(request.getAttribute("listaPaises") != null)
                listaPaises = (List<SelectItem>)request.getAttribute("listaPaises");

            // Titulaciones
            List<SelectItem> listaTitulaciones = new ArrayList<SelectItem>();
            if(request.getAttribute("listaTitulaciones") != null)
                listaTitulaciones = (List<SelectItem>)request.getAttribute("listaTitulaciones");

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

            // Tipo documento
            List<SelectItem> listaNif = new ArrayList<SelectItem>();
            if(request.getAttribute("listaNif") != null)
                listaNif = (List<SelectItem>)request.getAttribute("listaNif");

            // Estado
            List<SelectItem> listaEstado = new ArrayList<SelectItem>();
            if(request.getAttribute("listaEstado") != null)
                listaEstado = (List<SelectItem>)request.getAttribute("listaEstado");

            
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
            
            String lcodNif = "";
            String ldescNif = "";
            
            String lcodEstado = "";
            String ldescEstado = "";


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

            if (listaNif != null && listaNif.size() > 0) 
            {
                int i;
                SelectItem gc = null;
                for (i = 0; i < listaNif.size() - 1; i++) 
                {
                    gc = (SelectItem) listaNif.get(i);
                    lcodNif += "\"" + gc.getCodigo() + "\",";
                    ldescNif += "\"" + escape(gc.getDescripcion()) + "\",";
                }
                gc = (SelectItem) listaNif.get(i);
                lcodNif += "\"" + gc.getCodigo() + "\"";
                ldescNif += "\"" + escape(gc.getDescripcion()) + "\"";
            }

            if (listaEstado != null && listaEstado.size() > 0) 
            {
                int i;
                SelectItem est = null;
                for (i = 0; i < listaEstado.size() - 1; i++) 
                {
                    est = (SelectItem) listaEstado.get(i);
                    lcodEstado += "\"" + est.getCodigo() + "\",";
                    ldescEstado += "\"" + escape(est.getDescripcion()) + "\",";
                }
                est = (SelectItem) listaEstado.get(i);
                lcodEstado += "\"" + est.getCodigo() + "\"";
                ldescEstado += "\"" + escape(est.getDescripcion()) + "\"";
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
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/>
            <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/extension/melanbide59/melanbide59.css"/>
            <link rel="stylesheet" type="text/css" href="<c:url value='/css/table.css'/>">

            <script type="text/javascript" src="<c:url value='/scripts/general.js'/>"></script>
            <script type="text/javascript">
                var APP_CONTEXT_PATH = '<%=request.getContextPath()%>';
            </script>
            <script type="text/javascript" src="<c:url value='/scripts/calendario.js'/>"></script>
            <script type="text/javascript" src="<c:url value='/scripts/validaciones.js'/>"></script>
            <script type="text/javascript" src="<c:url value='/scripts/listaComboBox.js'/>"></script>
            <script type="text/javascript" src="<c:url value='/scripts/popup.js'/>"></script>
            <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/melanbide59/cpeUtils.js"></script>
            <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/melanbide59/FixedColumnsTable.js"></script>
        
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
            
            var codNif = [<%=lcodNif%>];
            var descNif = [<%=ldescNif%>];
            
            var codGrupoCot = [<%=lcodGrupoCot%>];
            var descGrupoCot = [<%=ldescGrupoCot%>];
            
            var codEstado = [<%=lcodEstado%>];
            var descEstado = [<%=ldescEstado%>];
            
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
                if(ofertaConsulta != null)
                {
                %>
                    //Datos puesto    
                    //Los textarea se inicializan directamente en la etiqueta, para evitar problemas con los saltos de linea
                    document.getElementById('codPuesto').value = '<%=ofertaConsulta.getCodPuesto() != null ? ofertaConsulta.getCodPuesto().toUpperCase() : "" %>';
                    document.getElementById('descPuesto').value = '<%=ofertaConsulta.getDescPuesto() != null ? ofertaConsulta.getDescPuesto().toUpperCase() : "" %>';
                    document.getElementById('codPaisPuesto1').value = '<%=ofertaConsulta.getPaiCod1() != null ? ofertaConsulta.getPaiCod1().toString() : "" %>';
                    document.getElementById('codPaisPuesto2').value = '<%=ofertaConsulta.getPaiCod2() != null ? ofertaConsulta.getPaiCod2().toString() : "" %>';
                    document.getElementById('codPaisPuesto3').value = '<%=ofertaConsulta.getPaiCod3() != null ? ofertaConsulta.getPaiCod3().toString() : "" %>';
                    document.getElementById('descTitulacionPuesto1').value = '<%=descTit1 != null ? descTit1.toUpperCase() : "" %>';
                    document.getElementById('descTitulacionPuesto2').value = '<%=descTit2 != null ? descTit2.toUpperCase() : "" %>';
                    document.getElementById('descTitulacionPuesto3').value = '<%=descTit3 != null ? descTit3.toUpperCase() : "" %>';
                    document.getElementById('codIdiomaPuesto1').value = '<%=ofertaConsulta.getCodIdioma1() != null ? ofertaConsulta.getCodIdioma1().toUpperCase() : "" %>';
                    document.getElementById('codNivelIdiomaPuesto1').value = '<%=ofertaConsulta.getCodNivIdi1() != null ? ofertaConsulta.getCodNivIdi1().toUpperCase() : "" %>';
                    document.getElementById('codIdiomaPuesto2').value = '<%=ofertaConsulta.getCodIdioma2() != null ? ofertaConsulta.getCodIdioma2().toUpperCase() : "" %>';
                    document.getElementById('codNivelIdiomaPuesto2').value = '<%=ofertaConsulta.getCodNivIdi2() != null ? ofertaConsulta.getCodNivIdi2().toUpperCase() : "" %>';
                    document.getElementById('codIdiomaPuesto3').value = '<%=ofertaConsulta.getCodIdioma3() != null ? ofertaConsulta.getCodIdioma3().toUpperCase() : "" %>';
                    document.getElementById('codNivelIdiomaPuesto3').value = '<%=ofertaConsulta.getCodNivIdi3() != null ? ofertaConsulta.getCodNivIdi3().toUpperCase() : "" %>';
                    document.getElementById('codNivelFormativo').value = '<%=ofertaConsulta.getCodNivForm() != null ? ofertaConsulta.getCodNivForm().toUpperCase() : "" %>';
                    document.getElementById('ciudadDestino').value = '<%=ofertaConsulta.getCiudadDestino() != null ? ofertaConsulta.getCiudadDestino().toUpperCase() : "" %>';
                    document.getElementById('dpto').value = '<%=ofertaConsulta.getDpto() != null ? ofertaConsulta.getDpto().toUpperCase() : "" %>';
                <%
                }
                %>
                       
                document.getElementById('txtTotalConcedido').value = '<%=concedido != null ? concedido.toUpperCase() : "" %>';
                reemplazarPuntosCpe(document.getElementById('txtTotalConcedido'));
                
                <%
                if(consulta == true || (puestoConsulta != null && puestoConsulta.getCodResult() != null && puestoConsulta.getCodResult().equalsIgnoreCase(ConstantesMeLanbide59.CODIGO_RESULTADO_RENUNCIA)))
                {
                %>
                    //Deshabilito todos los campos
                    document.getElementById('btnModifJustif').style.display = 'none';
                    document.getElementById('btnConsultarJustif').style.display = 'inline';
                <%
                }
                else
                {
                %>
                    document.getElementById('btnModifJustif').style.display = 'inline';
                    document.getElementById('btnConsultarJustif').style.display = 'none';
                <%
                }
                %>
                
                if(document.getElementById('ff-horizontal-fill')){
                    document.getElementById('ff-horizontal-fill').style.display = 'none';
                }
                if(document.getElementById('ff-vertical-fill')){
                    document.getElementById('ff-vertical-fill').style.display = 'none';
                }
            }
        
            function cargarDescripcionesCombos(){
                var desc = "";
                var codAct = "";
                var codigo = "";

                //Pais 1
                codigo = '<%=ofertaConsulta != null && ofertaConsulta.getPaiCod1() != null ? ofertaConsulta.getPaiCod1() : ""%>';
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
                codigo = '<%=ofertaConsulta != null && ofertaConsulta.getPaiCod2() != null ? ofertaConsulta.getPaiCod2() : ""%>';
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
                codigo = '<%=ofertaConsulta != null && ofertaConsulta.getPaiCod3() != null ? ofertaConsulta.getPaiCod3() : ""%>';
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
                codigo = '<%=ofertaConsulta != null && ofertaConsulta.getCodIdioma1() != null ? ofertaConsulta.getCodIdioma1() : ""%>';
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
                codigo = '<%=ofertaConsulta != null && ofertaConsulta.getCodIdioma2() != null ? ofertaConsulta.getCodIdioma2() : ""%>';
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
                codigo = '<%=ofertaConsulta != null && ofertaConsulta.getCodIdioma3() != null ? ofertaConsulta.getCodIdioma3() : ""%>';
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
                codigo = '<%=ofertaConsulta != null && ofertaConsulta.getCodNivIdi1() != null ? ofertaConsulta.getCodNivIdi1() : ""%>';
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
                codigo = '<%=ofertaConsulta != null && ofertaConsulta.getCodNivIdi2() != null ? ofertaConsulta.getCodNivIdi2() : ""%>';
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
                codigo = '<%=ofertaConsulta != null && ofertaConsulta.getCodNivIdi3() != null ? ofertaConsulta.getCodNivIdi3() : ""%>';
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
                codigo = '<%=ofertaConsulta != null && ofertaConsulta.getCodNivForm() != null ? ofertaConsulta.getCodNivForm() : ""%>';
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
            
            function verDocumento(documento){
                var CONTEXT_PATH = '<%=request.getContextPath()%>'
                var url = CONTEXT_PATH + "/PeticionModuloIntegracion.do";
                var parametros = "";
                var control = new Date();
                parametros = '?tarea=preparar&modulo=MELANBIDE59&operacion=descargarDocumentoOferta&tipo=0&idOferta=<%=ofertaConsulta != null && ofertaConsulta.getIdOferta() != null ? ofertaConsulta.getIdOferta() : ""%>&idPuesto=<%=ofertaConsulta != null && ofertaConsulta.getCodPuesto() != null ? ofertaConsulta.getCodPuesto() : ""%>&numero=<%=numExpediente%>&documento='+documento+'&control='+control.getTime();
                inicio = false;
                window.open(url+parametros, "_blank");
            }
    
            function dblClckTablaPersonasJustifCpe(rowID,tableName){
                pulsarConsultarPersonaContratada();
            }
            
            function pulsarConsultarPersonaContratada(){
                var fila;
                if(tabPersonasJustifCpe.selectedIndex != -1) {
                    fila = tabPersonasJustifCpe.selectedIndex;
                    var control = new Date();
                    var result = null;
                    var opcion = '<%=consulta == true ? "consultar" : "modificar"%>';
                    if(navigator.appName.indexOf("Internet Explorer")!=-1){
                        lanzarPopUpModal('<%=request.getContextPath()%>/PeticionModuloIntegracion.do?tarea=preparar&modulo=MELANBIDE59&operacion=cargarDatosPersonaContratada&tipo=0&numero=<%=numExpediente%>&opcion='+opcion+'&idJustif='+listaPersonasJustifCpe[fila][1]+'&idOferta='+listaPersonasJustifCpe[fila][0]+'&idPuesto=<%=puestoConsulta.getCodPuesto()%>&control='+control.getTime(),380,980,'yes','no',function(result){
							if (result != undefined){
								if(result[0] == '0'){
									recargarTablaContratacionesCpe(result);
								}
							}
						});
					}else{
                        lanzarPopUpModal('<%=request.getContextPath()%>/PeticionModuloIntegracion.do?tarea=preparar&modulo=MELANBIDE59&operacion=cargarDatosPersonaContratada&tipo=0&numero=<%=numExpediente%>&opcion='+opcion+'&idJustif='+listaPersonasJustifCpe[fila][1]+'&idOferta='+listaPersonasJustifCpe[fila][0]+'&idPuesto=<%=puestoConsulta.getCodPuesto()%>&control='+control.getTime(),440,980,'yes','no',function(result){
							if (result != undefined){
								if(result[0] == '0'){
									recargarTablaContratacionesCpe(result);
								}
							}
						});
					}
                    
                }else{
                        jsp_alerta('A', '<%=meLanbide59I18n.getMensaje(idiomaUsuario, "msg.msjNoSelecFila")%>');
                }
            }
    
            function recargarTablaContratacionesCpe(result){
                var fila;
                tabPersonasJustifCpe = new Array();
                listaPersonasJustifCpe = new Array();
                listaPersonasJustifCpeTabla = new Array();
                listaPersonasJustifCpeTabla_titulos = new Array();
                listaPersonasJustifCpeTabla_estilos = new Array();
                totalJustificado = 0;
                fila = result[1];
                
                //this.parent.recargarCalculosCpe(fila);
                //window.returnValue = fila;
self.parent.opener.retornoXanelaAuxiliar(fila);
                //this.parent.parent.opener.recargarCalculosCpe(fila);
                //this.parent.parent.opener.actualizarPestanaJustificacion();
                var justifAct;
                for(var i = 2;i< result.length; i++){
                    fila = result[i];
                    listaPersonasJustifCpe[i-2] = fila;
                    listaPersonasJustifCpeTabla[i-2] = [fila[2], fila[3], fila[4], fila[5], fila[6]];
                    listaPersonasJustifCpeTabla_titulos[i-2] = [fila[2], fila[3], fila[4], fila[5], fila[6]];
                    try{
                        if(fila[6] != null){
                            justifAct = fila[6];
                            justifAct = reemplazarTextoCpe(justifAct, /,/g, '.');
                            if(!isNaN(justifAct)){
                                totalJustificado += parseFloat(justifAct);
                            }
                        }
                    }catch(err){
                        
                    }
                }

                tabPersonasJustifCpe = new FixedColumnTable(document.getElementById('justificacionesCpe'), 880, 880, 'justificacionesCpe');
                tabPersonasJustifCpe.addColumna('80','left',"<%= meLanbide59I18n.getMensaje(idiomaUsuario,"label.nuevaJustificacion.tabla.personas.col1")%>");
                tabPersonasJustifCpe.addColumna('490','left',"<%= meLanbide59I18n.getMensaje(idiomaUsuario,"label.nuevaJustificacion.tabla.personas.col2")%>");    
                tabPersonasJustifCpe.addColumna('90','left',"<%= meLanbide59I18n.getMensaje(idiomaUsuario,"label.nuevaJustificacion.tabla.personas.col3")%>");   
                tabPersonasJustifCpe.addColumna('90','left',"<%= meLanbide59I18n.getMensaje(idiomaUsuario,"label.nuevaJustificacion.tabla.personas.col4")%>");     
                tabPersonasJustifCpe.addColumna('97','right',"<%= meLanbide59I18n.getMensaje(idiomaUsuario,"label.nuevaJustificacion.tabla.personas.col5")%>");     

                tabPersonasJustifCpe.numColumnasFijas = 0;

                tabPersonasJustifCpe.displayCabecera=true;

                for(var cont = 0; cont < listaPersonasJustifCpeTabla.length; cont++){
                    tabPersonasJustifCpe.addFilaConFormato(listaPersonasJustifCpeTabla[cont], listaPersonasJustifCpeTabla_titulos[cont], listaPersonasJustifCpeTabla_estilos[cont])
                }

                tabPersonasJustifCpe.displayCabecera=true;

                tabPersonasJustifCpe.height = '246';

                tabPersonasJustifCpe.altoCabecera = 50;

                tabPersonasJustifCpe.scrollWidth = 850;

                tabPersonasJustifCpe.dblClkFunction = 'dblClckTablaPersonasJustifCpe';

                tabPersonasJustifCpe.displayTabla();

                tabPersonasJustifCpe.pack();
                
                if(!isNaN(totalJustificado)){
                    totalJustificado = totalJustificado.toFixed(2);
                    document.getElementById('txtTotalJustificado').value = totalJustificado;
                    reemplazarPuntosCpe(document.getElementById('txtTotalJustificado'));
                }
            }
        </script>
    </head>
     <body id="cuerpoNuevaJustificacion" style="text-align: left;" >
        <div id="divCuerpo" class="contenidoPantalla" style="overflow-y: auto; padding: 10px;">
            <form  id="formNuevaOferta">
                <div id="barraProgresoNuevaJustificacion" style="position: absolute; z-index:10; visibility: hidden; top: 30%; left: 30%;">
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
                                                            <%=meLanbide59I18n.getMensaje(idiomaUsuario, "msg.guardandoDatos")%>
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
                <fieldset  style="width: 100%;">
                        <legend class="legendAzul"><%=meLanbide59I18n.getMensaje(idiomaUsuario,"legend.puesto.datosPuesto")%></legend>
                        <div class="lineaFormulario">
                            <div style="float: left; width: 42px;">
                                <%=meLanbide59I18n.getMensaje(idiomaUsuario,"label.puesto.datosPuesto.puesto")%>
                            </div>
                            <div style="width: auto; float: left; margin-left: 10px;">
                                <input type="text" maxlength="5" size="5" id="codPuesto" name="codPuesto" value="" class="inputTexto readOnly" readonly="true"/>
                                <input type="text" maxlength="200" size="78" id="descPuesto" name="descPuesto" value="" class="inputTexto readOnly" readonly="true"/>
                            </div>
                        </div>
                        <div class="lineaFormulario">    
                            <div style="float: left; width: 42px;">
                                <%=meLanbide59I18n.getMensaje(idiomaUsuario,"label.puesto.datosPuesto.pais1")%>
                            </div>
                            <div style="width: auto; float: left; margin-left: 10px; margin-right: 25px;">
                                <input id="codPaisPuesto1" name="codPaisPuesto1" type="text" class="inputTexto readOnly" size="2" maxlength="3" 
                                       onkeypress="javascript:return SoloDigitosConsulta(event);" value="" readonly="true">
                                <input id="descPaisPuesto1" name="descPaisPuesto1" type="text" class="inputTexto readOnly" size="22" readonly="true">
                            </div>
                            <div style="float: left; width: 42px;">
                                <%=meLanbide59I18n.getMensaje(idiomaUsuario,"label.puesto.datosPuesto.pais2")%>
                            </div>
                            <div style="width: auto; float: left; margin-left: 10px; margin-right: 25px;">
                                <input id="codPaisPuesto2" name="codPaisPuesto2" type="text" class="inputTexto readOnly" size="2" maxlength="3" 
                                       onkeypress="javascript:return SoloDigitosConsulta(event);" value=""  readonly="true">
                                <input id="descPaisPuesto2" name="descPaisPuesto2" type="text" class="inputTexto readOnly" size="22"  readonly="true">
                            </div>
                            <div style="float: left; width: 42px;">
                                <%=meLanbide59I18n.getMensaje(idiomaUsuario,"label.puesto.datosPuesto.pais3")%>
                            </div>
                            <div style="width: auto; float: left; margin-left: 10px;">
                                <input id="codPaisPuesto3" name="codPaisPuesto3" type="text" class="inputTexto readOnly" size="2" maxlength="3" 
                                       onkeypress="javascript:return SoloDigitosConsulta(event);" value="" readonly="true">
                                <input id="descPaisPuesto3" name="descPaisPuesto3" type="text" class="inputTexto readOnly" size="22" readonly="true">

                            </div>
                        </div>
                        <div class="lineaFormulario">
                            <div style="float: left; width: 42px;">
                                <%=meLanbide59I18n.getMensaje(idiomaUsuario,"label.puesto.datosPuesto.ciudadDestino")%>
                            </div>
                            <div style="width: auto; float: left; margin-left: 10px; margin-right: 25px;">
                                <input type="text" maxlength="200" size="53" id="ciudadDestino" name="ciudadDestino" value="" class="inputTexto readOnly" readonly="true"/>
                            </div>
                            <div style="float: left; width: 42px; margin-right: 9px;">
                                <%=meLanbide59I18n.getMensaje(idiomaUsuario,"label.puesto.datosPuesto.dpto")%>
                            </div>
                            <div style="width: auto; float: left; margin-left: 10px; margin-right: 25px;">
                                <input type="text" maxlength="200" size="53" id="dpto" name="dpto" value="" class="inputTexto readOnly" readonly="true"/>
                            </div>
                        </div>
                        <div class="lineaFormulario">        
                            <div style="float: left; width: 78px;">
                                <%=meLanbide59I18n.getMensaje(idiomaUsuario,"label.puesto.datosPuesto.titulacion1")%>
                            </div>
                            <div style="width: auto; float: left; margin-left: 10px; margin-right: 25px;">
                                <input id="descTitulacionPuesto1" name="descTitulacionPuesto1" type="text" class="inputTexto readOnly" size="80" readonly="true">
                            </div>   
                            <div style="float: left; width: 94px;">
                                <%=meLanbide59I18n.getMensaje(idiomaUsuario,"label.puesto.datosPuesto.nivelFormativo")%>
                            </div>
                            <div style="width: auto; float: left; margin-left: 2px;">
                                <input id="codNivelFormativo" name="codNivelFormativo" type="text" class="inputTexto readOnly" size="2" maxlength="3" 
                                       onkeypress="javascript:return SoloDigitosConsulta(event);" value="" readonly="true">
                                <input id="descNivelFormativo" name="descNivelFormativo" type="text" class="inputTexto readOnly" size="1" readonly="true">
                            </div> 
                        </div>
                        <div class="lineaFormulario">
                            <div style="float: left; width: 78px;">
                                <%=meLanbide59I18n.getMensaje(idiomaUsuario,"label.puesto.datosPuesto.titulacion2")%>
                            </div>
                            <div style="width: auto; float: left; margin-left: 10px; margin-right: 25px;">
                                <input id="descTitulacionPuesto2" name="descTitulacionPuesto2" type="text" class="inputTexto readOnly" size="80" readonly="true">
                            </div> 
                        </div>
                        <div class="lineaFormulario">
                            <div style="float: left; width: 78px;">
                                <%=meLanbide59I18n.getMensaje(idiomaUsuario,"label.puesto.datosPuesto.titulacion3")%>
                            </div>
                            <div style="width: auto; float: left; margin-left: 10px;">
                                <input id="descTitulacionPuesto3" name="descTitulacionPuesto3" type="text" class="inputTexto readOnly" size="80" readonly="true">
                            </div>
                        </div>
                        <div class="lineaFormulario">      
                            <div style="float: left; width: 78px;">
                                <%=meLanbide59I18n.getMensaje(idiomaUsuario,"label.puesto.datosPuesto.funciones")%>
                            </div>
                            <div style="width: auto; float: left; margin-left: 10px;">
                                <textarea rows="4" cols="131" id="funcionesPuesto" name="funcionesPuesto" maxlength="1000" class="inputTexto readOnly" readonly="true"><%=ofertaConsulta != null && ofertaConsulta.getFunciones() != null ? ofertaConsulta.getFunciones().toUpperCase() : "" %></textarea>
                            </div>
                        </div>
                        <div class="lineaFormulario">
                            <div style="float: left; width: 78px;">
                                <%=meLanbide59I18n.getMensaje(idiomaUsuario,"label.puesto.datosPuesto.idioma")%>
                            </div>
                            <div style="width: auto; float: left; margin-left: 10px; margin-right: 25px;">
                                <input id="codIdiomaPuesto1" name="codIdiomaPuesto1" type="text" class="inputTexto readOnly" size="2" maxlength="3" 
                                       onkeypress="javascript:return SoloDigitosConsulta(event);" value="" readonly="true">
                                <input id="descIdiomaPuesto1" name="descIdiomaPuesto1" type="text" class="inputTexto readOnly" size="17" readonly="true">
                            </div>
                            <div style="float: left; width: 78px;">
                                <%=meLanbide59I18n.getMensaje(idiomaUsuario,"label.puesto.datosPuesto.nivelIdioma")%>
                            </div>
                            <div style="width: auto; float: left; margin-left: 7px;">
                                <input id="codNivelIdiomaPuesto1" name="codNivelIdiomaPuesto1" type="text" class="inputTexto readOnly" size="2" maxlength="3" 
                                       onkeypress="javascript:return SoloDigitosConsulta(event);" readonly="true">
                                <input id="descNivelIdiomaPuesto1" name="descNivelIdiomaPuesto1" type="text" class="inputTexto readOnly" size="17" readonly="true">
                            </div>
                        </div>
                        <div class="lineaFormulario">
                            <div style="float: left; width: 78px;">
                                <%=meLanbide59I18n.getMensaje(idiomaUsuario,"label.puesto.datosPuesto.idioma")%>
                            </div>
                            <div style="width: auto; float: left; margin-left: 10px; margin-right: 25px;">
                                <input id="codIdiomaPuesto2" name="codIdiomaPuesto2" type="text" class="inputTexto readOnly" size="2" maxlength="3" 
                                       onkeypress="javascript:return SoloDigitosConsulta(event);" value="" readonly="true">
                                <input id="descIdiomaPuesto2" name="descIdiomaPuesto2" type="text" class="inputTexto readOnly" size="17" readonly="true">
                            </div>
                            <div style="float: left; width: 78px;">
                                <%=meLanbide59I18n.getMensaje(idiomaUsuario,"label.puesto.datosPuesto.nivelIdioma")%>
                            </div>
                            <div style="width: auto; float: left; margin-left: 7px;">
                                <input id="codNivelIdiomaPuesto2" name="codNivelIdiomaPuesto2" type="text" class="inputTexto readOnly" size="2" maxlength="3" 
                                       onkeypress="javascript:return SoloDigitosConsulta(event);" value="" readonly="true">
                                <input id="descNivelIdiomaPuesto2" name="descNivelIdiomaPuesto2" type="text" class="inputTexto readOnly" size="17" readonly="true">
                            </div>
                        </div>
                        <div class="lineaFormulario">
                            <div style="float: left; width: 78px;">
                                <%=meLanbide59I18n.getMensaje(idiomaUsuario,"label.puesto.datosPuesto.idioma")%>
                            </div>
                            <div style="width: auto; float: left; margin-left: 10px; margin-right: 25px;">
                                <input id="codIdiomaPuesto3" name="codIdiomaPuesto3" type="text" class="inputTexto readOnly" size="2" maxlength="3" 
                                       onkeypress="javascript:return SoloDigitosConsulta(event);" value="" readonly="true">
                                <input id="descIdiomaPuesto3" name="descIdiomaPuesto3" type="text" class="inputTexto readOnly" size="17" readonly="true">
                            </div>
                            <div style="float: left; width: 78px;">
                                <%=meLanbide59I18n.getMensaje(idiomaUsuario,"label.puesto.datosPuesto.nivelIdioma")%>
                            </div>
                            <div style="width: auto; float: left; margin-left: 7px;">
                                <input id="codNivelIdiomaPuesto3" name="codNivelIdiomaPuesto3" type="text" class="inputTexto readOnly" size="2" maxlength="3" 
                                       onkeypress="javascript:return SoloDigitosConsulta(event);" readonly="true">
                                <input id="descNivelIdiomaPuesto3" name="descNivelIdiomaPuesto3" type="text" class="inputTexto readOnly" size="17" readonly="true">
                            </div>
                        </div>
                    </fieldset>
                    <fieldset  style="width: 100%;">
                        <legend class="legendAzul"><%=meLanbide59I18n.getMensaje(idiomaUsuario,"legend.justificacion.personasContratadas")%></legend>
                        <div id="justificacionesCpe" style="padding: 5px; width:906px; height: 269px; text-align: center; margin:0px;margin-top:0px;" align="center"></div>
                        <div class="lineaFormulario">
                            <div style="float: right; padding-right: 50px;">
                                <div style="float: left; width: 125px;">
                                    <%=meLanbide59I18n.getMensaje(idiomaUsuario,"label.justificacion.personasContratadas.totalJustificado")%>
                                </div>
                                <div style="width: auto; float: left; margin-left: 10px;">
                                    <input type="text" maxlength="11" size="13" id="txtTotalJustificado" name="txtTotalJustificado" class="inputTexto readOnly" readonly="true" style="text-align: right;"/>
                                </div>
                            </div>
                        </div>
                        <div class="lineaFormulario">
                            <div style="float: right; padding-right: 50px;">
                                <div style="float: left; width: 125px;">
                                    <%=meLanbide59I18n.getMensaje(idiomaUsuario,"label.justificacion.personasContratadas.totalConcedido")%>
                                </div>
                                <div style="width: auto; float: left; margin-left: 10px;">
                                    <input type="text" maxlength="11" size="13" id="txtTotalConcedido" name="txtTotalConcedido" class="inputTexto readOnly" readonly="true" style="text-align: right;"/>
                                </div>
                            </div>
                        </div>
                        <div class="botonera" id="botoneraJustificaciones">
                            <input type="button" id="btnModifJustif" name="btnModifJustif" class="botonGeneral" value="<%=meLanbide59I18n.getMensaje(idiomaUsuario, "btn.modificar")%>" onclick="pulsarConsultarPersonaContratada();">
                            <input type="button" id="btnConsultarJustif" name="btnConsultarJustif" class="botonGeneral" value="<%=meLanbide59I18n.getMensaje(idiomaUsuario, "btn.consultar")%>" onclick="pulsarConsultarPersonaContratada();">
                        </div>
                    </fieldset>
                    <div class="botonera">
                        <input type="button" id="btnCerrar" name="btnCerrar" class="botonGeneral" value="<%=meLanbide59I18n.getMensaje(idiomaUsuario,"btn.cerrar")%>" onclick="cerrarVentana();">
                    </div>
                </form>
        </div>
        <script type="text/javascript">            
            var tabPersonasJustifCpe;
            var listaPersonasJustifCpe = new Array();
            var listaPersonasJustifCpeTabla = new Array();
            var listaPersonasJustifCpeTabla_titulos = new Array();
            var listaPersonasJustifCpeTabla_estilos = new Array();
            
            var totalJustificado = 0;

            //tabPersonasJustifCpe = new FixedColumnTable(document.getElementById('justificacionesCpe'), 850, 876, 'justificacionesCpe');
            tabPersonasJustifCpe = new FixedColumnTable(document.getElementById('justificacionesCpe'), 880, 880, 'justificacionesCpe');            
            tabPersonasJustifCpe.addColumna('80','left',"<%= meLanbide59I18n.getMensaje(idiomaUsuario,"label.nuevaJustificacion.tabla.personas.col1")%>");
            tabPersonasJustifCpe.addColumna('490','left',"<%= meLanbide59I18n.getMensaje(idiomaUsuario,"label.nuevaJustificacion.tabla.personas.col2")%>");    
            tabPersonasJustifCpe.addColumna('90','left',"<%= meLanbide59I18n.getMensaje(idiomaUsuario,"label.nuevaJustificacion.tabla.personas.col3")%>");   
            tabPersonasJustifCpe.addColumna('90','left',"<%= meLanbide59I18n.getMensaje(idiomaUsuario,"label.nuevaJustificacion.tabla.personas.col4")%>");     
            tabPersonasJustifCpe.addColumna('97','right',"<%= meLanbide59I18n.getMensaje(idiomaUsuario,"label.nuevaJustificacion.tabla.personas.col5")%>");     

            tabPersonasJustifCpe.numColumnasFijas = 0;
            
            <%  		
                FilaPersonaContratadaVO contrat = null;
                List<FilaPersonaContratadaVO> listaContrataciones = (List<FilaPersonaContratadaVO>)request.getAttribute("listaContrataciones");													
                if (listaContrataciones != null && listaContrataciones.size() >0){
                    for(int i = 0;i < listaContrataciones.size();i++)
                    {
                        contrat = listaContrataciones.get(i);
            %>
                listaPersonasJustifCpe[<%=i%>] = ['<%=contrat.getIdOferta()%>', '<%=contrat.getIdJustificacion()%>','<%=contrat.getNif()%>', '<%=contrat.getNomApel()%>','<%=contrat.getFeDesde()%>', '<%=contrat.getFeHasta()%>', '<%=contrat.getImpJustif()%>'];
                listaPersonasJustifCpeTabla[<%=i%>] = ['<%=contrat.getNif()%>', '<%=contrat.getNomApel()%>','<%=contrat.getFeDesde()%>', '<%=contrat.getFeHasta()%>', '<%=contrat.getImpJustif()%>'];
                listaPersonasJustifCpeTabla_titulos[<%=i%>] = ['<%=contrat.getNif()%>', '<%=contrat.getNomApel()%>','<%=contrat.getFeDesde()%>', '<%=contrat.getFeHasta()%>', '<%=contrat.getImpJustif()%>'];
                var txtimpJust='<%=contrat.getImpJustif() != null && !contrat.getImpJustif().equals("") && !contrat.getImpJustif().equals("-") ? contrat.getImpJustif() : "0"%>';
                var impjus = reemplazarTextoCpe(txtimpJust, /,/g, '.');                
                totalJustificado += parseFloat(impjus);
            <%
                    }// for
                }// if
            %>

            for(var cont = 0; cont < listaPersonasJustifCpeTabla.length; cont++){
                tabPersonasJustifCpe.addFilaConFormato(listaPersonasJustifCpeTabla[cont], listaPersonasJustifCpeTabla_titulos[cont], listaPersonasJustifCpeTabla_estilos[cont])
            }

            tabPersonasJustifCpe.displayCabecera=true;
            
            tabPersonasJustifCpe.height = '246';
    
            tabPersonasJustifCpe.altoCabecera = 50;

            tabPersonasJustifCpe.scrollWidth = 850;

            tabPersonasJustifCpe.dblClkFunction = 'dblClckTablaPersonasJustifCpe';

            tabPersonasJustifCpe.displayTabla();

            tabPersonasJustifCpe.pack();
            
            if(!isNaN(totalJustificado)){
                totalJustificado = totalJustificado.toFixed(2);
                document.getElementById('txtTotalJustificado').value = totalJustificado;
                reemplazarPuntosCpe(document.getElementById('txtTotalJustificado'));
            }
            
            //Datos oferta
            var comboEstado;
            inicio();
        </script>    
    </body>
</html>

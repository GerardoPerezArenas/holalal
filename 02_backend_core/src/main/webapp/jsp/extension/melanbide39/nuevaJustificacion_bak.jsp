<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>

<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide39.i18n.MeLanbide39I18n" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide39.vo.CpePuestoVO" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide39.vo.CpeOfertaVO" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide39.vo.CpeJustificacionVO" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide39.util.ConstantesMeLanbide39" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.plugin.persistence.vo.ValorCampoDesplegableModuloIntegracionVO"%>
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
<html>
    <head> 
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
            MeLanbide39I18n meLanbide39I18n = MeLanbide39I18n.getInstance();

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
            Boolean consulta = request.getAttribute("consulta") != null ? (Boolean)request.getAttribute("consulta") : false;
            SimpleDateFormat format = new SimpleDateFormat(ConstantesMeLanbide39.FORMATO_FECHA);

            String tituloPagina = "";
            if(consulta)
            {
                tituloPagina = meLanbide39I18n.getMensaje(idiomaUsuario,"label.justif.titulo.consultaJustif");
            }
            else
            {
                if(justifModif != null)
                {
                    tituloPagina = meLanbide39I18n.getMensaje(idiomaUsuario,"label.justif.titulo.modifJustif");
                }
                else
                {
                    tituloPagina = meLanbide39I18n.getMensaje(idiomaUsuario, "label.justif.titulo.nuevaJustif");
                }
            }
            
            //COMBOS

            // Paises
            List<ValorCampoDesplegableModuloIntegracionVO> listaPaises = new ArrayList<ValorCampoDesplegableModuloIntegracionVO>();
            if(request.getAttribute("listaPaises") != null)
                listaPaises = (List<ValorCampoDesplegableModuloIntegracionVO>)request.getAttribute("listaPaises");

            // Titulaciones
            List<ValorCampoDesplegableModuloIntegracionVO> listaTitulaciones = new ArrayList<ValorCampoDesplegableModuloIntegracionVO>();
            if(request.getAttribute("listaTitulaciones") != null)
                listaTitulaciones = (List<ValorCampoDesplegableModuloIntegracionVO>)request.getAttribute("listaTitulaciones");

            // Idiomas
            List<ValorCampoDesplegableModuloIntegracionVO> listaIdiomas = new ArrayList<ValorCampoDesplegableModuloIntegracionVO>();
            if(request.getAttribute("listaIdiomas") != null)
                listaIdiomas = (List<ValorCampoDesplegableModuloIntegracionVO>)request.getAttribute("listaIdiomas");

            // Nivel idiomas
            List<ValorCampoDesplegableModuloIntegracionVO> listaNivelIdiomas = new ArrayList<ValorCampoDesplegableModuloIntegracionVO>();
            if(request.getAttribute("listaNivelIdiomas") != null)
                listaNivelIdiomas = (List<ValorCampoDesplegableModuloIntegracionVO>)request.getAttribute("listaNivelIdiomas");

            // Nivel formativo
            List<ValorCampoDesplegableModuloIntegracionVO> listaNivelFormativo = new ArrayList<ValorCampoDesplegableModuloIntegracionVO>();
            if(request.getAttribute("listaNivelFormativo") != null)
                listaNivelFormativo = (List<ValorCampoDesplegableModuloIntegracionVO>)request.getAttribute("listaNivelFormativo");

            // Grupo cotizacion
            List<ValorCampoDesplegableModuloIntegracionVO> listaGrupoCotizacion = new ArrayList<ValorCampoDesplegableModuloIntegracionVO>();
            if(request.getAttribute("listaGrupoCotizacion") != null)
                listaGrupoCotizacion = (List<ValorCampoDesplegableModuloIntegracionVO>)request.getAttribute("listaGrupoCotizacion");

            // Tipo documento
            List<ValorCampoDesplegableModuloIntegracionVO> listaNif = new ArrayList<ValorCampoDesplegableModuloIntegracionVO>();
            if(request.getAttribute("listaNif") != null)
                listaNif = (List<ValorCampoDesplegableModuloIntegracionVO>)request.getAttribute("listaNif");

            // Estado
            List<ValorCampoDesplegableModuloIntegracionVO> listaEstado = new ArrayList<ValorCampoDesplegableModuloIntegracionVO>();
            if(request.getAttribute("listaEstado") != null)
                listaEstado = (List<ValorCampoDesplegableModuloIntegracionVO>)request.getAttribute("listaEstado");

            
            String lcodPais = "";
            String ldescPais = "";
            
            String lcodTitulacion = "";
            String ldescTitulacion = "";
            
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
                ValorCampoDesplegableModuloIntegracionVO pais = null;
                for (i = 0; i < listaPaises.size() - 1; i++) 
                {
                    pais = (ValorCampoDesplegableModuloIntegracionVO) listaPaises.get(i);
                    lcodPais += "\"" + pais.getCodigo() + "\",";
                    ldescPais += "\"" + escape(pais.getDescripcion()) + "\",";
                }
                pais = (ValorCampoDesplegableModuloIntegracionVO) listaPaises.get(i);
                lcodPais += "\"" + pais.getCodigo() + "\"";
                ldescPais += "\"" + escape(pais.getDescripcion()) + "\"";
            }


            if (listaTitulaciones != null && listaTitulaciones.size() > 0) 
            {
                int i;
                ValorCampoDesplegableModuloIntegracionVO titu = null;
                for (i = 0; i < listaTitulaciones.size() - 1; i++) 
                {
                    titu = (ValorCampoDesplegableModuloIntegracionVO) listaTitulaciones.get(i);
                    lcodTitulacion += "\"" + titu.getCodigo() + "\",";
                    ldescTitulacion += "\"" + escape(titu.getDescripcion()) + "\",";
                }
                titu = (ValorCampoDesplegableModuloIntegracionVO) listaTitulaciones.get(i);
                lcodTitulacion += "\"" + titu.getCodigo() + "\"";
                ldescTitulacion += "\"" + escape(titu.getDescripcion()) + "\"";
            }


            if (listaIdiomas != null && listaIdiomas.size() > 0) 
            {
                int i;
                ValorCampoDesplegableModuloIntegracionVO idi = null;
                for (i = 0; i < listaIdiomas.size() - 1; i++) 
                {
                    idi = (ValorCampoDesplegableModuloIntegracionVO) listaIdiomas.get(i);
                    lcodIdioma += "\"" + idi.getCodigo() + "\",";
                    ldescIdioma += "\"" + escape(idi.getDescripcion()) + "\",";
                }
                idi = (ValorCampoDesplegableModuloIntegracionVO) listaIdiomas.get(i);
                lcodIdioma += "\"" + idi.getCodigo() + "\"";
                ldescIdioma += "\"" + escape(idi.getDescripcion()) + "\"";
            }

            if (listaNivelFormativo != null && listaNivelFormativo.size() > 0) 
            {
                int i;
                ValorCampoDesplegableModuloIntegracionVO nf = null;
                for (i = 0; i < listaNivelFormativo.size() - 1; i++) 
                {
                    nf = (ValorCampoDesplegableModuloIntegracionVO) listaNivelFormativo.get(i);
                    lcodNivelFormativo += "\"" + nf.getCodigo() + "\",";
                    ldescNivelFormativo += "\"" + escape(nf.getDescripcion()) + "\",";
                }
                nf = (ValorCampoDesplegableModuloIntegracionVO) listaNivelFormativo.get(i);
                lcodNivelFormativo += "\"" + nf.getCodigo() + "\"";
                ldescNivelFormativo += "\"" + escape(nf.getDescripcion()) + "\"";
            }
                
            if (listaNivelIdiomas != null && listaNivelIdiomas.size() > 0) 
            {
                int i;
                ValorCampoDesplegableModuloIntegracionVO ni = null;
                for (i = 0; i < listaNivelIdiomas.size() - 1; i++) 
                {
                    ni = (ValorCampoDesplegableModuloIntegracionVO) listaNivelIdiomas.get(i);
                    lcodNivelIdioma += "\"" + ni.getCodigo() + "\",";
                    ldescNivelIdioma += "\"" + escape(ni.getDescripcion()) + "\",";
                }
                ni = (ValorCampoDesplegableModuloIntegracionVO) listaNivelIdiomas.get(i);
                lcodNivelIdioma += "\"" + ni.getCodigo() + "\"";
                ldescNivelIdioma += "\"" + escape(ni.getDescripcion()) + "\"";
            }

            if (listaGrupoCotizacion != null && listaGrupoCotizacion.size() > 0) 
            {
                int i;
                ValorCampoDesplegableModuloIntegracionVO gc = null;
                for (i = 0; i < listaGrupoCotizacion.size() - 1; i++) 
                {
                    gc = (ValorCampoDesplegableModuloIntegracionVO) listaGrupoCotizacion.get(i);
                    lcodGrupoCot += "\"" + gc.getCodigo() + "\",";
                    ldescGrupoCot += "\"" + escape(gc.getDescripcion()) + "\",";
                }
                gc = (ValorCampoDesplegableModuloIntegracionVO) listaGrupoCotizacion.get(i);
                lcodGrupoCot += "\"" + gc.getCodigo() + "\"";
                ldescGrupoCot += "\"" + escape(gc.getDescripcion()) + "\"";
            }

            if (listaNif != null && listaNif.size() > 0) 
            {
                int i;
                ValorCampoDesplegableModuloIntegracionVO gc = null;
                for (i = 0; i < listaNif.size() - 1; i++) 
                {
                    gc = (ValorCampoDesplegableModuloIntegracionVO) listaNif.get(i);
                    lcodNif += "\"" + gc.getCodigo() + "\",";
                    ldescNif += "\"" + escape(gc.getDescripcion()) + "\",";
                }
                gc = (ValorCampoDesplegableModuloIntegracionVO) listaNif.get(i);
                lcodNif += "\"" + gc.getCodigo() + "\"";
                ldescNif += "\"" + escape(gc.getDescripcion()) + "\"";
            }

            if (listaEstado != null && listaEstado.size() > 0) 
            {
                int i;
                ValorCampoDesplegableModuloIntegracionVO est = null;
                for (i = 0; i < listaEstado.size() - 1; i++) 
                {
                    est = (ValorCampoDesplegableModuloIntegracionVO) listaEstado.get(i);
                    lcodEstado += "\"" + est.getCodigo() + "\",";
                    ldescEstado += "\"" + escape(est.getDescripcion()) + "\",";
                }
                est = (ValorCampoDesplegableModuloIntegracionVO) listaEstado.get(i);
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
<link rel="StyleSheet" media="screen" type="text/css" href="<%=request.getContextPath()%><%=css%>">    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/css/font-awesome.min.css'/>" media="screen">
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/less/animated.less'/>" media="screen">
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/>
            <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/extension/melanbide39/melanbide39.css"/>

            <script type="text/javascript" src="<c:url value='/scripts/general.js'/>"></script>
            <script type="text/javascript">
                var APP_CONTEXT_PATH = '<%=request.getContextPath()%>';
            </script>
            <script type="text/javascript" src="<c:url value='/scripts/calendario.js'/>"></script>
            <script type="text/javascript" src="<c:url value='/scripts/validaciones.js'/>"></script>
            <script type="text/javascript" src="<c:url value='/scripts/listaComboBox.js'/>"></script>
            <script type="text/javascript" src="<c:url value='/scripts/popup.js'/>"></script>
            <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/melanbide39/cpeUtils.js"></script>
        
        <script type="text/javascript">
            var mensajeValidacion = '';
            var nuevo = true;
    
            //LISTAS DE VALORES PARA LOS COMBOS
            var codPais = [<%=lcodPais%>];
            var descPais = [<%=ldescPais%>];
            
            var codTitulacion = [<%=lcodTitulacion%>];
            var descTitulacion = [<%=ldescTitulacion%>];
            
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
                    document.getElementById('codTitulacionPuesto1').value = '<%=ofertaConsulta.getCodTit1() != null ? ofertaConsulta.getCodTit1().toUpperCase() : "" %>';
                    document.getElementById('codTitulacionPuesto2').value = '<%=ofertaConsulta.getCodTit2() != null ? ofertaConsulta.getCodTit2().toUpperCase() : "" %>';
                    document.getElementById('codTitulacionPuesto3').value = '<%=ofertaConsulta.getCodTit3() != null ? ofertaConsulta.getCodTit3().toUpperCase() : "" %>';
                    document.getElementById('codIdiomaPuesto1').value = '<%=ofertaConsulta.getCodIdioma1() != null ? ofertaConsulta.getCodIdioma1().toUpperCase() : "" %>';
                    document.getElementById('codNivelIdiomaPuesto1').value = '<%=ofertaConsulta.getCodNivIdi1() != null ? ofertaConsulta.getCodNivIdi1().toUpperCase() : "" %>';
                    document.getElementById('codIdiomaPuesto2').value = '<%=ofertaConsulta.getCodIdioma2() != null ? ofertaConsulta.getCodIdioma2().toUpperCase() : "" %>';
                    document.getElementById('codNivelIdiomaPuesto2').value = '<%=ofertaConsulta.getCodNivIdi2() != null ? ofertaConsulta.getCodNivIdi2().toUpperCase() : "" %>';
                    document.getElementById('codIdiomaPuesto3').value = '<%=ofertaConsulta.getCodIdioma3() != null ? ofertaConsulta.getCodIdioma3().toUpperCase() : "" %>';
                    document.getElementById('codNivelIdiomaPuesto3').value = '<%=ofertaConsulta.getCodNivIdi3() != null ? ofertaConsulta.getCodNivIdi3().toUpperCase() : "" %>';
                    document.getElementById('codNivelFormativo').value = '<%=ofertaConsulta.getCodNivForm() != null ? ofertaConsulta.getCodNivForm().toUpperCase() : "" %>';
                    document.getElementById('ciudadDestino').value = '<%=ofertaConsulta.getCiudadDestino() != null ? ofertaConsulta.getCiudadDestino().toUpperCase() : "" %>';
                    document.getElementById('dpto').value = '<%=ofertaConsulta.getDpto() != null ? ofertaConsulta.getDpto().toUpperCase() : "" %>';
                    
                    //Datos contratado
                    document.getElementById('codNifContratado').value = '<%=ofertaConsulta.getCodTipoNif() != null ? ofertaConsulta.getCodTipoNif().toUpperCase() : "" %>';
                    document.getElementById('txtNifOferta').value = '<%=ofertaConsulta.getNif() != null ? ofertaConsulta.getNif().toUpperCase() : "" %>';
                    document.getElementById('txtNombre').value = '<%=ofertaConsulta.getNombre() != null ? ofertaConsulta.getNombre().toUpperCase() : "" %>';
                    document.getElementById('txtApe1').value = '<%=ofertaConsulta.getApe1() != null ? ofertaConsulta.getApe1().toUpperCase() : "" %>';
                    document.getElementById('txtApe2').value = '<%=ofertaConsulta.getApe2() != null ? ofertaConsulta.getApe2().toUpperCase() : "" %>';
                    document.getElementById('txtEmail').value = '<%=ofertaConsulta.getEmail() != null ? ofertaConsulta.getEmail().toUpperCase() : "" %>';
                    document.getElementById('txtTfno').value = '<%=ofertaConsulta.getTlf() != null ? ofertaConsulta.getTlf().toUpperCase() : "" %>';
                    document.getElementById('fechaNacContratado').value = '<%=ofertaConsulta.getFecNac() != null ? format.format(ofertaConsulta.getFecNac()) : "" %>';
                    document.getElementById('txtSexo').value = '<%=ofertaConsulta.getSexo() != null ? ofertaConsulta.getSexo().toUpperCase() : "" %>';
                    document.getElementById('codTitulacionContratado').value = '<%=ofertaConsulta.getCodTitulacion() != null ? ofertaConsulta.getCodTitulacion().toUpperCase() : "" %>';
                    document.getElementById('anoTitulacion').value = '<%=ofertaConsulta.getAnoTitulacion() != null ? ofertaConsulta.getAnoTitulacion().toString() : "" %>';
                    document.getElementById('fechaIniContratado').value = '<%=ofertaConsulta.getFecIni() != null ? format.format(ofertaConsulta.getFecIni()) : "" %>';
                    document.getElementById('fechaFinContratado').value = '<%=ofertaConsulta.getFecFin() != null ? format.format(ofertaConsulta.getFecFin()) : "" %>';
                    document.getElementById('codGrupoCotContratado').value = '<%=ofertaConsulta.getCodGrCot() != null ? ofertaConsulta.getCodGrCot().toUpperCase() : "" %>';
                    document.getElementById('txtSalarioContratado').value = '<%=ofertaConsulta.getSalarioB() != null ? ofertaConsulta.getSalarioB().toPlainString() : "" %>';
                    document.getElementById('txtDietasConv').value = '<%=ofertaConsulta.getDietasConvenio() != null ? ofertaConsulta.getDietasConvenio().toPlainString() : "" %>';
                    document.getElementById('txtDietasConvoc').value = '<%=ofertaConsulta.getDietasConvoc() != null ? ofertaConsulta.getDietasConvoc().toPlainString() : "" %>';

                    reemplazarPuntosCpe(document.getElementById('txtSalarioContratado'));
                    reemplazarPuntosCpe(document.getElementById('txtDietasConv'));
                    reemplazarPuntosCpe(document.getElementById('txtDietasConvoc'));
                    
                    
                    <%
                    if(ofertaConsulta.getFecBaja() != null)
                    {
                    %>    
                        document.getElementById('txtImpJustificado').readOnly = true;
                        document.getElementById('txtImpJustificado').className = 'inputTexto readOnly';
                        document.getElementById('codEstado').readOnly = true;
                        document.getElementById('codEstado').className = 'inputTexto readOnly';
                        document.getElementById('descEstado').readOnly = true;
                        document.getElementById('descEstado').className = 'inputTexto readOnly';
                        document.getElementById('botonEstado').style.display = 'none';
                    <%
                    }
                    %>
                <%
                }
                %>
                        
                //Resumen
                document.getElementById('codEstado').value = '<%=justifModif != null && justifModif.getCodEstado() != null ? justifModif.getCodEstado() : ""%>';
                
                <%
                if(justifModif != null)
                {
                    if(justifModif.getFlVariosTrab() != null && justifModif.getFlVariosTrab().equalsIgnoreCase(ConstantesMeLanbide39.FALSO))
                    {
                %>
                        document.getElementById('radioVariosTrabN').checked = 'true';
                <%
                    }
                    else
                    {
                        if(justifModif.getFlVariosTrab() != null && justifModif.getFlVariosTrab().equalsIgnoreCase(ConstantesMeLanbide39.CIERTO))
                        {
                %>
                            document.getElementById('radioVariosTrabS').checked = 'true';
                <%
                        }
                    }
                }
                %>
                
                document.getElementById('txtImpConcedido').value = '<%=concedido != null ? concedido.toUpperCase() : "" %>';
                document.getElementById('txtImpJustificado').value = '<%=justifModif != null && justifModif.getImpJustificado() != null ? justifModif.getImpJustificado() : concedido != null ? concedido.toUpperCase() : "" %>';
                document.getElementById('txtImpRenuncia').value = '<%=renunciado != null ? renunciado.toUpperCase() : "" %>';
                
                reemplazarPuntosCpe(document.getElementById('txtImpConcedido'));
                reemplazarPuntosCpe(document.getElementById('txtImpJustificado'));
                reemplazarPuntosCpe(document.getElementById('txtImpRenuncia'));
                <%
                if(consulta == true)
                {
                %>
                        //Deshabilito todos los campos
                        
                        document.getElementById('txtImpJustificado').readOnly = true;
                        document.getElementById('txtImpJustificado').className = 'inputTexto readOnly';
                        document.getElementById('codEstado').readOnly = true;
                        document.getElementById('codEstado').className = 'inputTexto readOnly';
                        document.getElementById('descEstado').readOnly = true;
                        document.getElementById('descEstado').className = 'inputTexto readOnly';
                        document.getElementById('botonEstado').style.display = 'none';
                        document.getElementById('radioVariosTrabS').disabled = true;
                        document.getElementById('radioVariosTrabN').disabled = true;
                <%
                }
                else
                {
                %>
                    //Datos resumen
                    comboEstado = new Combo('Estado');
                    comboEstado.change = cambioEstado;
                    cargarCombos();
                <%
                }
                %>
                        
                mostrarImporteRenuncia();
            }
        
            function cargarDescripcionesCombos(){
                var desc = "";
                var codAct = "";
                var codigo = "";

                //Pais 1
                codigo = '<%=puestoConsulta != null && puestoConsulta.getPaiCod1() != null ? puestoConsulta.getPaiCod1() : ""%>';
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
                codigo = '<%=puestoConsulta != null && puestoConsulta.getPaiCod2() != null ? puestoConsulta.getPaiCod2() : ""%>';
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
                codigo = '<%=puestoConsulta != null && puestoConsulta.getPaiCod3() != null ? puestoConsulta.getPaiCod3() : ""%>';
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

                //Titulacion 1
                codigo = '<%=puestoConsulta != null && puestoConsulta.getCodTit1() != null ? puestoConsulta.getCodTit1() : ""%>';
                desc = '';
                encontrado = false;
                i = 0; 
                if(codigo != null && codigo != '')
                {
                    while(i<codTitulacion.length && !encontrado)
                    {
                        codAct = codTitulacion[i];
                        if(codAct == codigo)
                        {
                            encontrado = true;
                            desc = descTitulacion[i];
                        }else{
                            i++;
                        }
                    }
                }
                document.getElementById('descTitulacionPuesto1').value = desc;

                //Titulacion 2
                codigo = '<%=puestoConsulta != null && puestoConsulta.getCodTit2() != null ? puestoConsulta.getCodTit2() : ""%>';
                desc = '';
                encontrado = false;
                i = 0; 
                if(codigo != null && codigo != '')
                {
                    while(i<codTitulacion.length && !encontrado)
                    {
                        codAct = codTitulacion[i];
                        if(codAct == codigo)
                        {
                            encontrado = true;
                            desc = descTitulacion[i];
                        }else{
                            i++;
                        }
                    }
                }
                document.getElementById('descTitulacionPuesto2').value = desc;

                //Titulacion 3
                codigo = '<%=puestoConsulta != null && puestoConsulta.getCodTit3() != null ? puestoConsulta.getCodTit3() : ""%>';
                desc = '';
                encontrado = false;
                i = 0; 
                if(codigo != null && codigo != '')
                {
                    while(i<codTitulacion.length && !encontrado)
                    {
                        codAct = codTitulacion[i];
                        if(codAct == codigo)
                        {
                            encontrado = true;
                            desc = descTitulacion[i];
                        }else{
                            i++;
                        }
                    }
                }
                document.getElementById('descTitulacionPuesto3').value = desc;

                //Idioma 1
                codigo = '<%=puestoConsulta != null && puestoConsulta.getCodIdioma1() != null ? puestoConsulta.getCodIdioma1() : ""%>';
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
                codigo = '<%=puestoConsulta != null && puestoConsulta.getCodIdioma2() != null ? puestoConsulta.getCodIdioma2() : ""%>';
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
                codigo = '<%=puestoConsulta != null && puestoConsulta.getCodIdioma3() != null ? puestoConsulta.getCodIdioma3() : ""%>';
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
                codigo = '<%=puestoConsulta != null && puestoConsulta.getCodNivIdi1() != null ? puestoConsulta.getCodNivIdi1() : ""%>';
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
                codigo = '<%=puestoConsulta != null && puestoConsulta.getCodNivIdi2() != null ? puestoConsulta.getCodNivIdi2() : ""%>';
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
                codigo = '<%=puestoConsulta != null && puestoConsulta.getCodNivIdi3() != null ? puestoConsulta.getCodNivIdi3() : ""%>';
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
                codigo = '<%=puestoConsulta != null && puestoConsulta.getCodNivForm() != null ? puestoConsulta.getCodNivForm() : ""%>';
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
                
                //Nif
                codigo = '<%=ofertaConsulta != null && ofertaConsulta.getCodTipoNif() != null ? ofertaConsulta.getCodTipoNif() : ""%>';
                desc = '';
                encontrado = false;
                i = 0; 
                if(codigo != null && codigo != '')
                {
                    while(i<codNif.length && !encontrado)
                    {
                        codAct = codNif[i];
                        if(codAct == codigo)
                        {
                            encontrado = true;
                            desc = descNif[i];
                        }
                        else
                            {
                                i++;
                            }
                    }
                }
                document.getElementById('descNifContratado').value = desc;
                
                //Titulacion contratado
                codigo = '<%=ofertaConsulta != null && ofertaConsulta.getCodTitulacion() != null ? ofertaConsulta.getCodTitulacion() : ""%>';
                desc = '';
                encontrado = false;
                i = 0; 
                if(codigo != null && codigo != '')
                {
                    while(i<codTitulacion.length && !encontrado)
                    {
                        codAct = codTitulacion[i];
                        if(codAct == codigo)
                        {
                            encontrado = true;
                            desc = descTitulacion[i];
                        }
                        else
                            {
                                i++;
                            }
                    }
                }
                document.getElementById('descTitulacionContratado').value = desc;
                
                //Grupo cotización
                codigo = '<%=ofertaConsulta != null && ofertaConsulta.getCodGrCot() != null ? ofertaConsulta.getCodGrCot() : ""%>';
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
                        else
                            {
                                i++;
                            }
                    }
                }
                document.getElementById('descGrupoCotContratado').value = desc;
                
                //Estado
                codigo = '<%=justifModif != null && justifModif.getCodEstado() != null ? justifModif.getCodEstado() : ""%>';
                desc = '';
                encontrado = false;
                i = 0; 
                if(codigo != null && codigo != '')
                {
                    while(i<codEstado.length && !encontrado)
                    {
                        codAct = codEstado[i];
                        if(codAct == codigo)
                        {
                            encontrado = true;
                            desc = descEstado[i];
                        }
                        else
                            {
                                i++;
                            }
                    }
                }
                document.getElementById('descEstado').value = desc;
            }
            
            function cargarCombos(){
                comboEstado.addItems(codEstado, descEstado);
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
                var resultado = jsp_alerta('','<%=meLanbide39I18n.getMensaje(idiomaUsuario, "msg.preguntaCancelar")%>');
                if (resultado == 1){
                    cerrarVentana();
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
                parametros = '?tarea=preparar&modulo=MELANBIDE39&operacion=descargarDocumentoOferta&tipo=0&idOferta=<%=ofertaConsulta != null && ofertaConsulta.getIdOferta() != null ? ofertaConsulta.getIdOferta() : ""%>&idPuesto=<%=ofertaConsulta != null && ofertaConsulta.getCodPuesto() != null ? ofertaConsulta.getCodPuesto() : ""%>&numero=<%=numExpediente%>&documento='+documento+'&control='+control.getTime();
                inicio = false;
                window.open(url+parametros, "_blank");
            }
            
            function mostrarImporteRenuncia(){
                var visible = false;
                var estadoSelec = document.getElementById('codEstado').value;
                if(estadoSelec != undefined && estadoSelec != ''){
                    var codigoEstRenuncia = '<%=ConstantesMeLanbide39.CODIGO_ESTADO_RENUNCIA%>';
                    if(estadoSelec == codigoEstRenuncia){
                        var impConcedido = document.getElementById('txtImpConcedido').value;
                        var impRenuncia = document.getElementById('txtImpRenuncia').value;
                        if(impConcedido == impRenuncia){
                            visible = true;
                        }
                    }
                }
                if(visible){
                    document.getElementById('divImporteRenuncia').style.display = 'inline';
                }else{
                    document.getElementById('divImporteRenuncia').style.display = 'none';
                }
            }
            
            function cambioEstado(){
                mostrarImporteRenuncia();
            }
            
            function guardar(){
                if(validarDatos()){
                    document.getElementById('msgGuardandoDatos').style.display="inline";
                    barraProgresoCpe('on', 'barraProgresoNuevaJustificacion');
                    var ajax = getXMLHttpRequest();
                    var CONTEXT_PATH = '<%=request.getContextPath()%>'
                    var nodos = null;
                    var url = CONTEXT_PATH + "/PeticionModuloIntegracion.do";
                    var parametros = "";
                    var control = new Date();
                    var parametros = 'tarea=preparar&modulo=MELANBIDE39&operacion=guardarJustificacion&tipo=0&numero=<%=numExpediente%>'
                        +'&idJustif=<%=justifModif != null && justifModif.getIdJustificacion() != null ? justifModif.getIdJustificacion() : ""%>'
                        +'&idPuesto=<%=justifModif != null && justifModif.getCodPuesto() != null ? justifModif.getCodPuesto() : ""%>'    
                        +'&radioVariosTrab='+(document.getElementById('radioVariosTrabS').checked ? '<%=ConstantesMeLanbide39.CIERTO%>' : document.getElementById('radioVariosTrabN').checked ? '<%=ConstantesMeLanbide39.FALSO%>' : '')
                        +'&impJustificado='+document.getElementById('txtImpJustificado').value
                        +'&estado='+document.getElementById('codEstado').value
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
                        var listaJustificaciones = new Array();
                        var fila = new Array();
                        var nodoFila;
                        var hijosFila;
                        var nodoCampo;
                        var j;

                        for(j=0;hijos!=null && j<hijos.length;j++){
                            if(hijos[j].nodeName=="CODIGO_OPERACION"){                            
                                codigoOperacion = hijos[j].childNodes[0].nodeValue;
                                listaJustificaciones[j] = codigoOperacion;
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
                                }
                                listaJustificaciones[j] = fila;
                                fila = new Array();
                            }else if(hijos[j].nodeName=="FILA"){
                                nodoFila = hijos[j];
                                hijosFila = nodoFila.childNodes;
                                for(var cont = 0; cont < hijosFila.length; cont++){
                                    if(hijosFila[cont].nodeName=="ID_JUSTIF"){
                                        nodoCampo = hijosFila[cont];
                                        if(nodoCampo.childNodes.length > 0){
                                            fila[0] = nodoCampo.childNodes[0].nodeValue;
                                        }
                                        else{
                                            fila[0] = '-';
                                        }
                                    }else if(hijosFila[cont].nodeName=="COD_PUESTO"){
                                        nodoCampo = hijosFila[cont];
                                        if(nodoCampo.childNodes.length > 0){
                                            fila[1] = nodoCampo.childNodes[0].nodeValue;
                                        }
                                        else{
                                            fila[1] = '-';
                                        }
                                    }
                                    else if(hijosFila[cont].nodeName=="ID_OFERTA"){
                                        nodoCampo = hijosFila[cont];
                                        if(nodoCampo.childNodes.length > 0){
                                            fila[2] = nodoCampo.childNodes[0].nodeValue;
                                        }
                                        else{
                                            fila[2] = '-';
                                        }
                                    }
                                    else if(hijosFila[cont].nodeName=="DESC_PUESTO"){
                                        nodoCampo = hijosFila[cont];
                                        if(nodoCampo.childNodes.length > 0){
                                            fila[3] = nodoCampo.childNodes[0].nodeValue;
                                        }
                                        else{
                                            fila[3] = '-';
                                        }
                                    }
                                    else if(hijosFila[cont].nodeName=="NOMAPEL"){
                                        nodoCampo = hijosFila[cont];
                                        if(nodoCampo.childNodes.length > 0){
                                            fila[4] = nodoCampo.childNodes[0].nodeValue;
                                        }
                                        else{
                                            fila[4] = '-';
                                        }
                                    }
                                    else if(hijosFila[cont].nodeName=="DNI"){
                                        nodoCampo = hijosFila[cont];
                                        if(nodoCampo.childNodes.length > 0){
                                            fila[5] = nodoCampo.childNodes[0].nodeValue;
                                        }
                                        else{
                                            fila[5] = '-';
                                        }
                                    }
                                    else if(hijosFila[cont].nodeName=="FEC_INI"){
                                        nodoCampo = hijosFila[cont];
                                        if(nodoCampo.childNodes.length > 0){
                                            fila[6] = nodoCampo.childNodes[0].nodeValue;
                                        }
                                        else{
                                            fila[6] = '';
                                        }
                                    }
                                    else if(hijosFila[cont].nodeName=="FEC_FIN"){
                                        nodoCampo = hijosFila[cont];
                                        if(nodoCampo.childNodes.length > 0){
                                            fila[7] = nodoCampo.childNodes[0].nodeValue;
                                        }
                                        else{
                                            fila[7] = '';
                                        }
                                    }
                                    else if(hijosFila[cont].nodeName=="FEC_BAJA"){
                                        nodoCampo = hijosFila[cont];
                                        if(nodoCampo.childNodes.length > 0){
                                            fila[8] = nodoCampo.childNodes[0].nodeValue;
                                        }
                                        else{
                                            fila[8] = '';
                                        }
                                    }
                                    else if(hijosFila[cont].nodeName=="CAUSA_BAJA"){
                                        nodoCampo = hijosFila[cont];
                                        if(nodoCampo.childNodes.length > 0){
                                            fila[9] = nodoCampo.childNodes[0].nodeValue;
                                        }
                                        else{
                                            fila[9] = '-';
                                        }
                                    }
                                    else if(hijosFila[cont].nodeName=="ESTADO"){
                                        nodoCampo = hijosFila[cont];
                                        if(nodoCampo.childNodes.length > 0){
                                            fila[10] = nodoCampo.childNodes[0].nodeValue;
                                        }
                                        else{
                                            fila[10] = '-';
                                        }
                                    }
                                }
                                listaJustificaciones[j] = fila;
                                fila = new Array();
                            }   
                        }//for(j=0;hijos!=null && j<hijos.length;j++)
                        if(codigoOperacion=="0"){
                            window.returnValue =  listaJustificaciones;
                            barraProgresoCpe('on', 'barraProgresoNuevaJustificacion');
                            jsp_alerta("A",'<%=meLanbide39I18n.getMensaje(idiomaUsuario,"msg.datosGuardadosOK")%>');
                            cerrarVentana();
                        }else if(codigoOperacion=="1"){
                            jsp_alerta("A",'<%=meLanbide39I18n.getMensaje(idiomaUsuario,"error.errorBD")%>');
                        }else if(codigoOperacion=="2"){
                            jsp_alerta("A",'<%=meLanbide39I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                        }else if(codigoOperacion=="3"){
                            jsp_alerta("A",'<%=meLanbide39I18n.getMensaje(idiomaUsuario,"error.pasoParametros")%>');
                        }else if(codigoOperacion=="4"){
                            jsp_alerta("A",'<%=meLanbide39I18n.getMensaje(idiomaUsuario,"error.datosNoValidos")%>');
                        }else{
                            jsp_alerta("A",'<%=meLanbide39I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                        }//if(
                    }
                    catch(Err){
                        jsp_alerta("A",'<%=meLanbide39I18n.getMensaje(idiomaUsuario,"error.errorProcesarRespuesta")%>');
                    }//try-catch
                    barraProgresoCpe('off', 'barraProgresoNuevaJustificacion');
                }else{
                    jsp_alerta("A", escape(mensajeValidacion));
                }
            }
            
            function validarDatos(){
                var correcto = true;
                try
                {
                    //Importe justificado
                    if(document.getElementById('txtImpJustificado').value != ''){
                        try{
                            if(!validarNumericoDecimalCpe(document.getElementById('txtImpJustificado'), 10, 2)){
                                correcto = false;
                                document.getElementById('txtImpJustificado').style.border = '1px solid red';
                                if(mensajeValidacion == '')
                                    mensajeValidacion = '<%=meLanbide39I18n.getMensaje(idiomaUsuario, "msg.justificacion.resumen.impJustificadoIncorrecto")%>';
                            }else{
                                document.getElementById('txtImpJustificado').removeAttribute('style');
                            }
                        }catch(err){
                            correcto = false;
                            document.getElementById('txtImpJustificado').style.border = '1px solid red';
                            if(mensajeValidacion == '')
                                mensajeValidacion = '<%=meLanbide39I18n.getMensaje(idiomaUsuario, "msg.justificacion.resumen.impJustificadoIncorrecto")%>';
                        }
                    }

                    //Combo estado
                    var est = document.getElementById('codEstado').value;
                    if(!existeCodigoCombo(est, codEstado)){
                        correcto = false;
                        document.getElementById('codEstado').style.border = '1px solid red';
                        document.getElementById('descEstado').style.border = '1px solid red';
                        if(mensajeValidacion == ''){
                            mensajeValidacion = '<%=meLanbide39I18n.getMensaje(idiomaUsuario, "msg.oferta.datosBaja.codigoCausaBajaNoExiste")%>';
                        }
                    }else{
                        document.getElementById('codEstado').removeAttribute("style");
                        document.getElementById('descEstado').removeAttribute("style");
                    }
                }
                catch(err)
                {
                    correcto = false; 
                    if(mensajeValidacion == ''){
                        mensajeValidacion = '<%=meLanbide39I18n.getMensaje(idiomaUsuario, "msg.errorValidarDatos")%>';
                    }
                }
                return correcto;
            }
        </script>
    </head>
    <body id="cuerpoNuevaJustificacion" style="text-align: left;" class="contenidoPantalla">
        <div id="divCuerpo" style="overflow-y: auto; padding: 10px;">
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
                                                            <%=meLanbide39I18n.getMensaje(idiomaUsuario, "msg.guardandoDatos")%>
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
                        <legend class="legendAzul"><%=meLanbide39I18n.getMensaje(idiomaUsuario,"legend.puesto.datosPuesto")%></legend>
                        <div class="lineaFormulario">
                            <div style="float: left; width: 42px;">
                                <%=meLanbide39I18n.getMensaje(idiomaUsuario,"label.puesto.datosPuesto.puesto")%>
                            </div>
                            <div style="width: auto; float: left; margin-left: 10px;">
                                <input type="text" maxlength="5" size="5" id="codPuesto" name="codPuesto" value="" class="inputTexto readOnly" readonly="true"/>
                                <input type="text" maxlength="200" size="78" id="descPuesto" name="descPuesto" value="" class="inputTexto readOnly" readonly="true"/>
                            </div>
                        </div>
                        <div class="lineaFormulario">    
                            <div style="float: left; width: 42px;">
                                <%=meLanbide39I18n.getMensaje(idiomaUsuario,"label.puesto.datosPuesto.pais1")%>
                            </div>
                            <div style="width: auto; float: left; margin-left: 10px; margin-right: 25px;">
                                <input id="codPaisPuesto1" name="codPaisPuesto1" type="text" class="inputTexto readOnly" size="2" maxlength="3" 
                                       onkeypress="javascript:return SoloDigitosConsulta(event);" value="" readonly="true">
                                <input id="descPaisPuesto1" name="descPaisPuesto1" type="text" class="inputTexto readOnly" size="22" readonly="true">
                            </div>
                            <div style="float: left; width: 42px;">
                                <%=meLanbide39I18n.getMensaje(idiomaUsuario,"label.puesto.datosPuesto.pais2")%>
                            </div>
                            <div style="width: auto; float: left; margin-left: 10px; margin-right: 25px;">
                                <input id="codPaisPuesto2" name="codPaisPuesto2" type="text" class="inputTexto readOnly" size="2" maxlength="3" 
                                       onkeypress="javascript:return SoloDigitosConsulta(event);" value=""  readonly="true">
                                <input id="descPaisPuesto2" name="descPaisPuesto2" type="text" class="inputTexto readOnly" size="22"  readonly="true">
                            </div>
                            <div style="float: left; width: 42px;">
                                <%=meLanbide39I18n.getMensaje(idiomaUsuario,"label.puesto.datosPuesto.pais3")%>
                            </div>
                            <div style="width: auto; float: left; margin-left: 10px;">
                                <input id="codPaisPuesto3" name="codPaisPuesto3" type="text" class="inputTexto readOnly" size="2" maxlength="3" 
                                       onkeypress="javascript:return SoloDigitosConsulta(event);" value="" readonly="true">
                                <input id="descPaisPuesto3" name="descPaisPuesto3" type="text" class="inputTexto readOnly" size="22" readonly="true">

                            </div>
                        </div>
                        <div class="lineaFormulario">
                            <div style="float: left; width: 42px;">
                                <%=meLanbide39I18n.getMensaje(idiomaUsuario,"label.puesto.datosPuesto.ciudadDestino")%>
                            </div>
                            <div style="width: auto; float: left; margin-left: 10px; margin-right: 25px;">
                                <input type="text" maxlength="200" size="53" id="ciudadDestino" name="ciudadDestino" value="" class="inputTexto readOnly" readonly="true"/>
                            </div>
                            <div style="float: left; width: 42px; margin-right: 9px;">
                                <%=meLanbide39I18n.getMensaje(idiomaUsuario,"label.puesto.datosPuesto.dpto")%>
                            </div>
                            <div style="width: auto; float: left; margin-left: 10px; margin-right: 25px;">
                                <input type="text" maxlength="200" size="53" id="dpto" name="dpto" value="" class="inputTexto readOnly" readonly="true"/>
                            </div>
                        </div>
                        <div class="lineaFormulario">        
                            <div style="float: left; width: 78px;">
                                <%=meLanbide39I18n.getMensaje(idiomaUsuario,"label.puesto.datosPuesto.titulacion1")%>
                            </div>
                            <div style="width: auto; float: left; margin-left: 10px; margin-right: 25px;">
                                <input id="codTitulacionPuesto1" name="codTitulacionPuesto1" type="text" class="inputTexto readOnly" size="2" maxlength="3" 
                                       onkeypress="javascript:return SoloDigitosConsulta(event);" value="" readonly="true">
                                <input id="descTitulacionPuesto1" name="descTitulacionPuesto1" type="text" class="inputTexto readOnly" size="16" readonly="true">
                            </div>    
                            <div style="float: left; width: 78px;">
                                <%=meLanbide39I18n.getMensaje(idiomaUsuario,"label.puesto.datosPuesto.titulacion2")%>
                            </div>
                            <div style="width: auto; float: left; margin-left: 10px; margin-right: 25px;">
                                <input id="codTitulacionPuesto2" name="codTitulacionPuesto2" type="text" class="inputTexto readOnly" size="2" maxlength="3" 
                                       onkeypress="javascript:return SoloDigitosConsulta(event);" value="" readonly="true">
                                <input id="descTitulacionPuesto2" name="descTitulacionPuesto2" type="text" class="inputTexto readOnly" size="16" readonly="true">
                            </div>    
                            <div style="float: left; width: 78px;">
                                <%=meLanbide39I18n.getMensaje(idiomaUsuario,"label.puesto.datosPuesto.titulacion3")%>
                            </div>
                            <div style="width: auto; float: left; margin-left: 10px;">
                                <input id="codTitulacionPuesto3" name="codTitulacionPuesto3" type="text" class="inputTexto readOnly" size="2" maxlength="3" 
                                       onkeypress="javascript:return SoloDigitosConsulta(event);" value="" readonly="true">
                                <input id="descTitulacionPuesto3" name="descTitulacionPuesto3" type="text" class="inputTexto readOnly" size="16" readonly="true">
                            </div>
                        </div>
                        <div class="lineaFormulario">      
                            <div style="float: left; width: 78px;">
                                <%=meLanbide39I18n.getMensaje(idiomaUsuario,"label.puesto.datosPuesto.funciones")%>
                            </div>
                            <div style="width: auto; float: left; margin-left: 10px;">
                                <textarea rows="4" cols="131" id="funcionesPuesto" name="funcionesPuesto" maxlength="1000" class="inputTexto readOnly" readonly="true"><%=ofertaConsulta != null && ofertaConsulta.getFunciones() != null ? ofertaConsulta.getFunciones().toUpperCase() : "" %></textarea>
                            </div>
                        </div>
                        <div class="lineaFormulario">
                            <div style="float: left; width: 78px;">
                                <%=meLanbide39I18n.getMensaje(idiomaUsuario,"label.puesto.datosPuesto.idioma")%>
                            </div>
                            <div style="width: auto; float: left; margin-left: 10px; margin-right: 25px;">
                                <input id="codIdiomaPuesto1" name="codIdiomaPuesto1" type="text" class="inputTexto readOnly" size="2" maxlength="3" 
                                       onkeypress="javascript:return SoloDigitosConsulta(event);" value="" readonly="true">
                                <input id="descIdiomaPuesto1" name="descIdiomaPuesto1" type="text" class="inputTexto readOnly" size="17" readonly="true">
                            </div>
                            <div style="float: left; width: 78px;">
                                <%=meLanbide39I18n.getMensaje(idiomaUsuario,"label.puesto.datosPuesto.nivelIdioma")%>
                            </div>
                            <div style="width: auto; float: left; margin-left: 7px;">
                                <input id="codNivelIdiomaPuesto1" name="codNivelIdiomaPuesto1" type="text" class="inputTexto readOnly" size="2" maxlength="3" 
                                       onkeypress="javascript:return SoloDigitosConsulta(event);" readonly="true">
                                <input id="descNivelIdiomaPuesto1" name="descNivelIdiomaPuesto1" type="text" class="inputTexto readOnly" size="17" readonly="true">
                            </div>
                        </div>
                        <div class="lineaFormulario">
                            <div style="float: left; width: 78px;">
                                <%=meLanbide39I18n.getMensaje(idiomaUsuario,"label.puesto.datosPuesto.idioma")%>
                            </div>
                            <div style="width: auto; float: left; margin-left: 10px; margin-right: 25px;">
                                <input id="codIdiomaPuesto2" name="codIdiomaPuesto2" type="text" class="inputTexto readOnly" size="2" maxlength="3" 
                                       onkeypress="javascript:return SoloDigitosConsulta(event);" value="" readonly="true">
                                <input id="descIdiomaPuesto2" name="descIdiomaPuesto2" type="text" class="inputTexto readOnly" size="17" readonly="true">
                            </div>
                            <div style="float: left; width: 78px;">
                                <%=meLanbide39I18n.getMensaje(idiomaUsuario,"label.puesto.datosPuesto.nivelIdioma")%>
                            </div>
                            <div style="width: auto; float: left; margin-left: 7px;">
                                <input id="codNivelIdiomaPuesto2" name="codNivelIdiomaPuesto2" type="text" class="inputTexto readOnly" size="2" maxlength="3" 
                                       onkeypress="javascript:return SoloDigitosConsulta(event);" value="" readonly="true">
                                <input id="descNivelIdiomaPuesto2" name="descNivelIdiomaPuesto2" type="text" class="inputTexto readOnly" size="17" readonly="true">
                            </div>
                        </div>
                        <div class="lineaFormulario">
                            <div style="float: left; width: 78px;">
                                <%=meLanbide39I18n.getMensaje(idiomaUsuario,"label.puesto.datosPuesto.idioma")%>
                            </div>
                            <div style="width: auto; float: left; margin-left: 10px; margin-right: 25px;">
                                <input id="codIdiomaPuesto3" name="codIdiomaPuesto3" type="text" class="inputTexto readOnly" size="2" maxlength="3" 
                                       onkeypress="javascript:return SoloDigitosConsulta(event);" value="" readonly="true">
                                <input id="descIdiomaPuesto3" name="descIdiomaPuesto3" type="text" class="inputTexto readOnly" size="17" readonly="true">
                            </div>
                            <div style="float: left; width: 78px;">
                                <%=meLanbide39I18n.getMensaje(idiomaUsuario,"label.puesto.datosPuesto.nivelIdioma")%>
                            </div>
                            <div style="width: auto; float: left; margin-left: 7px;">
                                <input id="codNivelIdiomaPuesto3" name="codNivelIdiomaPuesto3" type="text" class="inputTexto readOnly" size="2" maxlength="3" 
                                       onkeypress="javascript:return SoloDigitosConsulta(event);" readonly="true">
                                <input id="descNivelIdiomaPuesto3" name="descNivelIdiomaPuesto3" type="text" class="inputTexto readOnly" size="17" readonly="true">
                            </div>
                        </div>
                        <div class="lineaFormulario">
                            <div style="float: left; width: 94px;">
                                <%=meLanbide39I18n.getMensaje(idiomaUsuario,"label.puesto.datosPuesto.nivelFormativo")%>
                            </div>
                            <div style="width: auto; float: left; margin-left: 2px;">
                                <input id="codNivelFormativo" name="codNivelFormativo" type="text" class="inputTexto readOnly" size="2" maxlength="3" 
                                       onkeypress="javascript:return SoloDigitosConsulta(event);" value="" readonly="true">
                                <input id="descNivelFormativo" name="descNivelFormativo" type="text" class="inputTexto readOnly" size="17" readonly="true">
                            </div>
                        </div>
                    </fieldset>
                    <fieldset  style="width: 100%;">
                        <legend class="legendAzul"><%=meLanbide39I18n.getMensaje(idiomaUsuario,"legend.oferta.datosContratado")%></legend>
                        <div class="lineaFormulario">     
                            <div style="float: left; width: 90px;">
                                <%=meLanbide39I18n.getMensaje(idiomaUsuario,"label.oferta.datosContratado.nif")%>
                            </div>
                            <div style="width: auto; float: left; margin-left: 10px;">
                                <input id="codNifContratado" name="codNifContratado" type="text" class="inputTexto readOnly" size="2" maxlength="3" 
                                       onkeypress="javascript:return SoloDigitosConsulta(event);" value="" readonly="true">
                                <input id="descNifContratado" name="descNifContratado" type="text" class="inputTexto readOnly" size="20" readonly >
                            </div>
                            <div style="width: auto; float: left; margin-left: 20px;">
                                <input type="text" class="inputTexto readOnly" maxlength="15" size="10" id="txtNifOferta" name="txtNifOferta" value="" readOnly="true"/>
                            </div>
                        </div>
                        <div class="lineaFormulario"> 
                            <div style="float: left; width: 90px;">
                                <%=meLanbide39I18n.getMensaje(idiomaUsuario,"label.oferta.datosContratado.nombre")%>
                            </div>
                            <div style="width: auto; float: left; margin-left: 10px;">
                                <input type="text" class="inputTexto readOnly" maxlength="200" size="30" id="txtNombre" name="txtNombre" value="" readonly="true"/>
                            </div>
                            <div style="float: left; width: 80px; margin-left: 20px;">
                                <%=meLanbide39I18n.getMensaje(idiomaUsuario,"label.oferta.datosContratado.ape1")%>
                            </div>
                            <div style="width: auto; float: left; margin-left: 5px;">
                                <input type="text" class="inputTexto readOnly" maxlength="200" size="30" id="txtApe1" name="txtApe1" value="" readonly="true"/>
                            </div>
                            <div style="float: left; width: 80px; margin-left: 20px;">
                                <%=meLanbide39I18n.getMensaje(idiomaUsuario,"label.oferta.datosContratado.ape2")%>
                            </div>
                            <div style="width: auto; float: left; margin-left: 5px;">
                                <input type="text" class="inputTexto readOnly" maxlength="200" size="30" id="txtApe2" name="txtApe2" value="" readonly="true"/>
                            </div>
                        </div>
                        <div class="lineaFormulario"> 
                            <div style="float: left; width: 90px;">
                                <%=meLanbide39I18n.getMensaje(idiomaUsuario,"label.oferta.datosContratado.email")%>
                            </div>
                            <div style="width: auto; float: left; margin-left: 10px;">
                                <input type="text" class="inputTexto readOnly" maxlength="200" size="30" id="txtEmail" name="txtEmail" value="" readonly="true"/>
                            </div>
                            <div style="float: left; width: 80px; margin-left: 20px;">
                                <%=meLanbide39I18n.getMensaje(idiomaUsuario,"label.oferta.datosContratado.tfno")%>
                            </div>
                            <div style="width: auto; float: left; margin-left: 5px;">
                                <input type="text" class="inputTexto readOnly" maxlength="15" size="15" id="txtTfno" name="txtTfno" value="" readonly="true"/>
                            </div>
                        </div>
                        <div class="lineaFormulario">
                            <div style="float: left; width: 90px;">
                                <%=meLanbide39I18n.getMensaje(idiomaUsuario,"label.oferta.datosContratado.fechaNac")%>
                            </div>
                            <div style="width: auto; float: left; margin-left: 10px;">
                                <input type="text" class="inputTxtFecha readOnly" size="10" maxlength="10" id="fechaNacContratado" name="fechaNacContratado" value="" readonly="true"/>
                            </div>
                            <div style="float: left; width: 80px; margin-left: 142px;">
                                <%=meLanbide39I18n.getMensaje(idiomaUsuario,"label.oferta.datosContratado.sexo")%>
                            </div>
                            <div style="width: auto; float: left; margin-left: 4px;">
                                <input type="text" class="inputTexto readOnly" maxlength="1" size="3" id="txtSexo" name="txtSexo" value="" readonly="true"/>
                            </div>
                        </div>
                        <div class="lineaFormulario">
                            <div style="float: left; width: 90px;">
                                <%=meLanbide39I18n.getMensaje(idiomaUsuario,"label.oferta.datosContratado.titulacion")%>
                            </div>
                            <div style="width: auto; float: left; margin-left: 10px;">
                                <input id="codTitulacionContratado" name="codTitulacionContratado" type="text" class="inputTexto readOnly" size="2" maxlength="3" 
                                       onkeypress="javascript:return SoloDigitosConsulta(event);" value="" readonly="true">
                                <input id="descTitulacionContratado" name="descTitulacionContratado" type="text" class="inputTexto readOnly" size="20" readonly >
                            </div>
                            <div style="float: left; width: 80px; margin-left: 23px;">
                                <%=meLanbide39I18n.getMensaje(idiomaUsuario,"label.oferta.datosContratado.anoTitulacion")%>
                            </div>
                            <div style="width: auto; float: left; margin-left: 10px; margin-right: 150px;">
                                <input type="text" class="inputTexto readOnly" maxlength="4" size="3" id="anoTitulacion" name="anoTitulacion" value="" readonly="true"/>
                            </div>
                        </div>
                        <div class="lineaFormulario">
                            <div style="float: left; width: 765px;">
                                <%=meLanbide39I18n.getMensaje(idiomaUsuario,"label.oferta.datosContratado.docContratacion")%>:&nbsp;
                                <input type="button" id="btnVerDocContratacion" name="btnDocContratacion" class="botonGeneral" value="<%=meLanbide39I18n.getMensaje(idiomaUsuario,"btn.ver")%>" onclick="verDocumento('contratacionOferta');">
                            </div>
                        </div>
                        <div class="lineaFormulario">
                            <div style="float: left; width: 90px;">
                                <%=meLanbide39I18n.getMensaje(idiomaUsuario,"label.oferta.datosContratado.feIni")%>
                            </div>
                            <div style="width: auto; float: left; margin-left: 10px;">
                                <input type="text" class="inputTxtFecha readOnly" size="10" maxlength="10" id="fechaIniContratado" name="fechaIniContratado" readonly="true"/>
                            </div>
                            <div style="float: left; width: 80px; margin-left: 110px;">
                                <%=meLanbide39I18n.getMensaje(idiomaUsuario,"label.oferta.datosContratado.feFin")%>
                            </div>
                            <div style="width: auto; float: left; margin-left: 10px; margin-right: 150px;">
                                <input type="text" class="inputTxtFecha readOnly" size="10" maxlength="10" id="fechaFinContratado" name="fechaFinContratado" readonly="true"/>
                            </div>
                        </div>
                        <div class="lineaFormulario">
                            <div style="float: left; width: 90px;">
                                <%=meLanbide39I18n.getMensaje(idiomaUsuario,"label.oferta.datosContratado.grCot")%>
                            </div>
                            <div style="width: auto; float: left; margin-left: 10px; margin-right: 22px;">
                                <input id="codGrupoCotContratado" name="codGrupoCotContratado" type="text" class="inputTexto readOnly" size="2" maxlength="3" 
                                        value="" readonly="true">
                                <input id="descGrupoCotContratado" name="descGrupoCotContratado" type="text" class="inputTexto readOnly" size="20" readonly >
                            </div>
                        </div>
                        <div class="lineaFormulario">
                            <div style="float: left; width: 125px; margin-right: 2px;">
                                <%=meLanbide39I18n.getMensaje(idiomaUsuario,"label.oferta.datosContratado.salarioB")%>
                            </div>
                            <div style="width: auto; float: left; margin-left: 10px;">
                                <input type="text" maxlength="11" size="8" id="txtSalarioContratado" name="txtSalarioContratado" value="" class="inputTexto readOnly" readonly="true"/>
                            </div>
                            <div style="float: left; width: 150px; margin-left: 20px;">
                                <%=meLanbide39I18n.getMensaje(idiomaUsuario,"label.oferta.datosContratado.dietasConv")%>
                            </div>
                            <div style="width: auto; float: left; margin-left: 10px;">
                                <input type="text" maxlength="11" size="8" id="txtDietasConv" name="txtDietasConv" value="" class="inputTexto readOnly" readonly="true"/>
                            </div>
                            <div style="float: left; width: 150px; margin-left: 20px;">
                                <%=meLanbide39I18n.getMensaje(idiomaUsuario,"label.oferta.datosContratado.dietasConvoc")%>
                            </div>
                            <div style="width: auto; float: left; margin-left: 10px;">
                                <input type="text" maxlength="11" size="8" id="txtDietasConvoc" name="txtDietasConvoc" value="" class="inputTexto readOnly" readonly="true"/>
                            </div>
                        </div>
                    </fieldset>
                    <fieldset  style="width: 100%;">
                        <legend class="legendAzul"><%=meLanbide39I18n.getMensaje(idiomaUsuario,"legend.oferta.resumenJustif")%></legend>
                        <!-- En teoria desaparece, pero por si acaso, en lugar de quitarlo se oculta -->
                        <div class="lineaFormulario" style="display: none;">
                            <div style="float: left; width: 245px;">
                                <%=meLanbide39I18n.getMensaje(idiomaUsuario,"label.justificacion.datosResumen.variosTrab")%>
                            </div>
                            <div style="width: auto; float: left; margin-left: 10px;">
                                <div style="float: left;">
                                    <input type="radio" name="radioVariosTrab" id="radioVariosTrabS" value="S"><%=meLanbide39I18n.getMensaje(idiomaUsuario, "label.si")%>
                                    <input type="radio" name="radioVariosTrab" id="radioVariosTrabN" value="N"><%=meLanbide39I18n.getMensaje(idiomaUsuario, "label.no")%>
                                </div>
                            </div>
                        </div>
                        <!-- En teoria desaparece, pero por si acaso, en lugar de quitarlo se oculta -->
                        <hr style="width: 98%; float: left; margin-top: 5px; margin-bottom: 5px; display: none;"/>
                        <div class="lineaFormulario">   
                            <div style="float: left; width: 150px;">
                                <%=meLanbide39I18n.getMensaje(idiomaUsuario,"label.justificacion.datosResumen.impConcedido")%>
                            </div>
                            <div style="width: auto; float: left; margin-left: 10px;">
                                <input type="text" maxlength="11" size="8" id="txtImpConcedido" name="txtImpConcedido" value="" class="inputTexto readOnly" readonly="true"/>
                            </div>
                            <div style="float: left; width: 150px; margin-left: 20px;">
                                <%=meLanbide39I18n.getMensaje(idiomaUsuario,"label.justificacion.datosResumen.impJustificado")%>
                            </div>
                            <div style="width: auto; float: left; margin-left: 10px;">
                                <input type="text" maxlength="11" size="8" id="txtImpJustificado" name="txtImpJustificado" value="" class="inputTexto"/>
                            </div>
                            <div id="divImporteRenuncia">
                                <div style="float: left; width: 150px; margin-left: 20px;">
                                    <%=meLanbide39I18n.getMensaje(idiomaUsuario,"label.justificacion.datosResumen.impRenuncia")%>
                                </div>
                                <div style="width: auto; float: left; margin-left: 10px;">
                                    <input type="text" maxlength="11" size="8" id="txtImpRenuncia" name="txtImpRenuncia" value="" class="inputTexto readOnly" readonly="true"/>
                                </div>
                            </div>
                        </div>
                        <!-- En teoria desaparece, pero por si acaso, en lugar de quitarlo se oculta -->
                        <div class="lineaFormulario" style="display: none;">
                            <div style="float: left; width: 90px;">
                                <%=meLanbide39I18n.getMensaje(idiomaUsuario,"label.justificacion.datosResumen.estado")%>
                            </div>
                            <div style="width: auto; float: left; margin-left: 10px;">
                                <input id="codEstado" name="codEstado" type="text" class="inputTexto" size="2" maxlength="8" 
                                       onkeypress="javascript:return SoloDigitosConsulta(event);" value="">
                                <input id="descEstado" name="descEstado" type="text" class="inputTexto" size="20" readonly >
                                <a id="anchorEstado" name="anchorEstado" href=""><span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonEstado" name="botonEstado" style="cursor:hand;" alt="<%=meLanbide39I18n.getMensaje(idiomaUsuario, "altDesplegable")%>" title="<%=meLanbide39I18n.getMensaje(idiomaUsuario, "altDesplegable")%>"></span></a>
                            </div>
                        </div>
                    </fieldset>
                    <div class="botonera">
                        <input type="button" id="btnGuardarContrato" name="btnGuardarContrato" class="botonGeneral" value="<%=meLanbide39I18n.getMensaje(idiomaUsuario,"btn.aceptar")%>" onclick="guardar();">
                        <input type="button" id="btnCancelarContrato" name="btnCancelarContrato" class="botonGeneral" value="<%=meLanbide39I18n.getMensaje(idiomaUsuario,"btn.cancelar")%>" onclick="cancelar();">
                        <input type="button" id="btnCerrar" name="btnCerrar" class="botonGeneral" value="<%=meLanbide39I18n.getMensaje(idiomaUsuario,"btn.cerrar")%>" onclick="cerrarVentana();" style="display: none;">
                    </div>
                </form>
        </div>
        <script type="text/javascript">            
            //Datos oferta
            var comboEstado;
            inicio();
        </script>    
    </body>
</html>

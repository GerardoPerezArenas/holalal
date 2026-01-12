<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>

<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide60.i18n.MeLanbide60I18n" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide60.util.MeLanbide60Utils" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide60.vo.CmePuestoVO" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide60.vo.CmeOfertaVO" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide60.util.ConstantesMeLanbide60" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide60.vo.SelectItem"%>
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
            MeLanbide60I18n meLanbide60I18n = MeLanbide60I18n.getInstance();

            Config m_Config = ConfigServiceHelper.getConfig("common");
            String statusBar = m_Config.getString("JSP.StatusBar");
            String nombreModulo     = request.getParameter("nombreModulo");
            String codOrganizacion  = request.getParameter("codOrganizacionModulo");
            String numExpediente    = request.getParameter("numero");
            
            String descTit1 = (String)request.getAttribute("descTit1");
            String descTit2 = (String)request.getAttribute("descTit2");
            String descTit3 = (String)request.getAttribute("descTit3");
            String descTitContratado = (String)request.getAttribute("descTitContratado");
            
            CmePuestoVO puestoConsulta = (CmePuestoVO)request.getAttribute("puestoConsulta");
            //out.println("puestoConsulta es null?:"+puestoConsulta.getCodPuesto());
            CmeOfertaVO ofertaModif = (CmeOfertaVO)request.getAttribute("ofertaModif");
            //out.println("ofertaModif es null?:"+ofertaModif);
           
            CmeOfertaVO ofertaOrigen = (CmeOfertaVO)request.getAttribute("ofertaOrigen");
            //out.println("ofertaOrigen es null?:"+ofertaOrigen==null);
            
            //System.out.println("PAGINA OFERTA- ofertaModif:"+ofertaModif.getDescPuesto());
            CmeOfertaVO ofertaConDatos = null;
            Boolean consulta = request.getAttribute("consulta") != null ? (Boolean)request.getAttribute("consulta") : false;
            SimpleDateFormat format = new SimpleDateFormat(ConstantesMeLanbide60.FORMATO_FECHA);
    
            String tituloPagina = "";
            if(consulta)
            {
                tituloPagina = meLanbide60I18n.getMensaje(idiomaUsuario,"label.oferta.titulo.consultaOferta");
            }
            else
            {
                if(ofertaModif != null)
                {
                    tituloPagina = meLanbide60I18n.getMensaje(idiomaUsuario,"label.oferta.titulo.modifOferta");
                }
                else
                {
                    tituloPagina = meLanbide60I18n.getMensaje(idiomaUsuario, "label.oferta.titulo.nuevaOferta");
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

            // Oficinas Lanbide
            List<SelectItem> listaOficinasGestion = new ArrayList<SelectItem>();
            if(request.getAttribute("listaOficinasGestion") != null)
                listaOficinasGestion = (List<SelectItem>)request.getAttribute("listaOficinasGestion");

            // Causas renuncia
            List<SelectItem> listaCausasRenuncia = new ArrayList<SelectItem>();
            if(request.getAttribute("listaCausaRenuncia") != null)
                listaCausasRenuncia = (List<SelectItem>)request.getAttribute("listaCausaRenuncia");

            // Causas renuncia presenta oferta
            List<SelectItem> listaCausasRenunciaPresOferta = new ArrayList<SelectItem>();
            if(request.getAttribute("listaCausaRenunciaPresOferta") != null)
                listaCausasRenunciaPresOferta = (List<SelectItem>)request.getAttribute("listaCausaRenunciaPresOferta");

            // Tipo documento
            List<SelectItem> listaNif = new ArrayList<SelectItem>();
            if(request.getAttribute("listaNif") != null)
                listaNif = (List<SelectItem>)request.getAttribute("listaNif");

            // Causa baja
            List<SelectItem> listaCausaBaja = new ArrayList<SelectItem>();
            if(request.getAttribute("listaCausaBaja") != null)
                listaCausaBaja = (List<SelectItem>)request.getAttribute("listaCausaBaja");

            
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
            
            String lcodOfiGestion = "";
            String ldescOfiGestion = "";
            
            String lcodCausaRenuncia = "";
            String ldescCausaRenuncia = "";

            String lcodCausaRenunciaPresOferta = "";
            String ldescCausaRenunciaPresOferta = "";
            
            String lcodNif = "";
            String ldescNif = "";
            
            String lcodCausaBaja = "";
            String ldescCausaBaja = "";


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

            if (listaOficinasGestion != null && listaOficinasGestion.size() > 0) 
            {
                int i;
                SelectItem gc = null;
                for (i = 0; i < listaOficinasGestion.size() - 1; i++) 
                {
                    gc = (SelectItem) listaOficinasGestion.get(i);
                    lcodOfiGestion += "\"" + gc.getCodigo() + "\",";
                    ldescOfiGestion += "\"" + escape(gc.getDescripcion()) + "\",";
                }
                gc = (SelectItem) listaOficinasGestion.get(i);
                lcodOfiGestion += "\"" + gc.getCodigo() + "\"";
                ldescOfiGestion += "\"" + escape(gc.getDescripcion()) + "\"";
            }

            if (listaCausasRenuncia != null && listaCausasRenuncia.size() > 0) 
            {
                int i;
                SelectItem gc = null;
                for (i = 0; i < listaCausasRenuncia.size() - 1; i++) 
                {
                    gc = (SelectItem) listaCausasRenuncia.get(i);
                    lcodCausaRenuncia += "\"" + gc.getCodigo() + "\",";
                    ldescCausaRenuncia += "\"" + escape(gc.getDescripcion()) + "\",";
                }
                gc = (SelectItem) listaCausasRenuncia.get(i);
                lcodCausaRenuncia += "\"" + gc.getCodigo() + "\"";
                ldescCausaRenuncia += "\"" + escape(gc.getDescripcion()) + "\"";
            }

            if (listaCausasRenunciaPresOferta != null && listaCausasRenunciaPresOferta.size() > 0) 
            {
                int i;
                SelectItem gc = null;
                for (i = 0; i < listaCausasRenunciaPresOferta.size() - 1; i++) 
                {
                    gc = (SelectItem) listaCausasRenunciaPresOferta.get(i);
                    lcodCausaRenunciaPresOferta += "\"" + gc.getCodigo() + "\",";
                    ldescCausaRenunciaPresOferta += "\"" + escape(gc.getDescripcion()) + "\",";
                }
                gc = (SelectItem) listaCausasRenunciaPresOferta.get(i);
                lcodCausaRenunciaPresOferta += "\"" + gc.getCodigo() + "\"";
                ldescCausaRenunciaPresOferta += "\"" + escape(gc.getDescripcion()) + "\"";
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

            if (listaCausaBaja != null && listaCausaBaja.size() > 0) 
            {
                int i;
                SelectItem gc = null;
                for (i = 0; i < listaCausaBaja.size() - 1; i++) 
                {
                    gc = (SelectItem) listaCausaBaja.get(i);
                    lcodCausaBaja += "\"" + gc.getCodigo() + "\",";
                    ldescCausaBaja += "\"" + escape(gc.getDescripcion()) + "\",";
                }
                gc = (SelectItem) listaCausaBaja.get(i);
                lcodCausaBaja += "\"" + gc.getCodigo() + "\"";
                ldescCausaBaja += "\"" + escape(gc.getDescripcion()) + "\"";
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
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/extension/melanbide60/melanbide60.css"/>
        
        <script type="text/javascript" src="<c:url value='/scripts/general.js'/>"></script>
        <script type="text/javascript">
            var APP_CONTEXT_PATH = '<%=request.getContextPath()%>';
        </script>
        <script type="text/javascript" src="<c:url value='/scripts/calendario.js'/>"></script>
        <script type="text/javascript" src="<c:url value='/scripts/validaciones.js'/>"></script>
        <script type="text/javascript" src="<c:url value='/scripts/listaComboBox.js'/>"></script>
        <script type="text/javascript" src="<c:url value='/scripts/popup.js'/>"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/melanbide60/cmeUtils.js"></script>
        
        <script type="text/javascript">
            var mensajeValidacion = '';
            var nuevo = true;
            var alta = '0';
            var copiar = '0';
    
            //LISTAS DE VALORES PARA LOS COMBOS
            var codPais = [<%=lcodPais%>];
            var descPais = [<%=ldescPais%>];
            
            var codIdioma = [<%=lcodIdioma%>];
            var descIdioma = [<%=ldescIdioma%>];
            
            var codNivelIdioma = [<%=lcodNivelIdioma%>];
            var descNivelIdioma = [<%=ldescNivelIdioma%>];
            
            var codNivelFormativo = [<%=lcodNivelFormativo%>];
            var descNivelFormativo = [<%=ldescNivelFormativo%>];
            
            var codOfiGestion = [<%=lcodOfiGestion%>];
            var descOfiGestion = [<%=ldescOfiGestion%>];
            
            var codCausaRenuncia = [<%=lcodCausaRenuncia%>];
            var descCausaRenuncia = [<%=ldescCausaRenuncia%>];
            
            var codCausaRenunciaPresOferta = [<%=lcodCausaRenunciaPresOferta%>];
            var descCausaRenunciaPresOferta = [<%=ldescCausaRenunciaPresOferta%>];
            
            var codNif = [<%=lcodNif%>];
            var descNif = [<%=ldescNif%>];
            
            var codCausaBaja = [<%=lcodCausaBaja%>];
            var descCausaBaja = [<%=ldescCausaBaja%>];
            
            var codGrupoCot = [<%=lcodGrupoCot%>];
            var descGrupoCot = [<%=ldescGrupoCot%>];
            
            
            function inicio(){
                cargarDescripcionesCombos();
                <%
                if(puestoConsulta != null)
                {
                %>
                        //Los textarea se inicializan directamente en la etiqueta, para evitar problemas con los saltos de linea
                    nuevo = false;
                    document.getElementById('codPuesto').value = '<%=puestoConsulta.getCodPuesto() != null ? puestoConsulta.getCodPuesto().toUpperCase() : "" %>';
                    <% if (ofertaModif != null){ %>
                    document.getElementById('descPuesto').value = '<%=ofertaModif.getDescPuesto() != null ? ofertaModif.getDescPuesto().toUpperCase() : "" %>';
                    document.getElementById('codPaisPuesto1').value = '<%=ofertaModif.getPaiCod1() != null ? ofertaModif.getPaiCod1().toString() : "" %>';
                    document.getElementById('codPaisPuesto2').value = '<%=ofertaModif.getPaiCod2() != null ? ofertaModif.getPaiCod2().toString() : "" %>';
                    document.getElementById('codPaisPuesto3').value = '<%=ofertaModif.getPaiCod3() != null ? ofertaModif.getPaiCod3().toString() : "" %>';
                    document.getElementById('codTitulacionPuesto1').value = '<%=ofertaModif.getCodTit1() != null ? ofertaModif.getCodTit1().toUpperCase() : "" %>';
                    document.getElementById('descTitulacionPuesto1').value = '<%=descTit1 != null ? descTit1.toUpperCase() : "" %>';
                    document.getElementById('codTitulacionPuesto2').value = '<%=ofertaModif.getCodTit2() != null ? ofertaModif.getCodTit2().toUpperCase() : "" %>';
                    document.getElementById('descTitulacionPuesto2').value = '<%=descTit2 != null ? descTit2.toUpperCase() : "" %>';
                    document.getElementById('codTitulacionPuesto3').value = '<%=ofertaModif.getCodTit3() != null ? ofertaModif.getCodTit3().toUpperCase() : "" %>';
                    document.getElementById('descTitulacionPuesto3').value = '<%=descTit3 != null ? descTit3.toUpperCase() : "" %>';
                    document.getElementById('codIdiomaPuesto1').value = '<%=ofertaModif.getCodIdioma1() != null ? ofertaModif.getCodIdioma1().toUpperCase() : "" %>';
                    document.getElementById('codNivelIdiomaPuesto1').value = '<%=ofertaModif.getCodNivIdi1() != null ? ofertaModif.getCodNivIdi1().toUpperCase() : "" %>';
                    document.getElementById('codIdiomaPuesto2').value = '<%=ofertaModif.getCodIdioma2() != null ? ofertaModif.getCodIdioma2().toUpperCase() : "" %>';
                    document.getElementById('codNivelIdiomaPuesto2').value = '<%=ofertaModif.getCodNivIdi2() != null ? ofertaModif.getCodNivIdi2().toUpperCase() : "" %>';
                    document.getElementById('codIdiomaPuesto3').value = '<%=ofertaModif.getCodIdioma3() != null ? ofertaModif.getCodIdioma3().toUpperCase() : "" %>';
                    document.getElementById('codNivelIdiomaPuesto3').value = '<%=ofertaModif.getCodNivIdi3() != null ? ofertaModif.getCodNivIdi3().toUpperCase() : "" %>';
                    document.getElementById('codNivelFormativo').value = '<%=ofertaModif.getCodNivForm() != null ? ofertaModif.getCodNivForm().toUpperCase() : "" %>';
                    document.getElementById('ciudadDestino').value = '<%=ofertaModif.getCiudadDestino() != null ? ofertaModif.getCiudadDestino().toUpperCase() : "" %>';
                    document.getElementById('dpto').value = '<%=ofertaModif.getDpto() != null ? ofertaModif.getDpto().toUpperCase() : "" %>';
                    
                    <%
                    }
                    if(puestoConsulta.getCodResult() != null && puestoConsulta.getCodResult().equalsIgnoreCase(ConstantesMeLanbide60.CODIGO_RESULTADO_RENUNCIA))
                    {
                    %>    
                        document.getElementById('fechaBaja').readOnly = true;
                        document.getElementById('fechaBaja').className = 'inputTexto readOnly';
                        document.getElementById('calFechaBaja').style.display = 'none';
                        document.getElementById('codCausaBaja').readOnly = true;
                        document.getElementById('codCausaBaja').className = 'inputTexto readOnly';
                        document.getElementById('descCausaBaja').readOnly = true;
                        document.getElementById('descCausaBaja').className = 'inputTexto readOnly';
                        document.getElementById('botonCausaBaja').style.display = 'none';
                        document.getElementById('radioSustituyeS').disabled = true;
                        document.getElementById('radioSustituyeN').disabled = true;
                        document.getElementById('btnAltaSustituto').style.display = 'none';
                        document.getElementById('observacionesBaja').readOnly = true;
                        document.getElementById('observacionesBaja').className = 'inputTexto readOnly';
                    <%
                    }
                    %>
                <%
                }
                if(ofertaModif != null)
                {
                    ofertaConDatos = ofertaModif;
                    if(ofertaOrigen != null)
                    {
                        ofertaConDatos = ofertaOrigen;
                    }
                %>
                        //Datos oferta
                        document.getElementById('txtNumOferta').value = '<%=ofertaConDatos.getCodOferta() != null ? ofertaConDatos.getCodOferta().toUpperCase() : "" %>';
                        document.getElementById('codOfiGestionOferta').value = '<%=ofertaConDatos.getCodOfiGest() != null ? ofertaConDatos.getCodOfiGest().toUpperCase() : "" %>';
                        document.getElementById('codCausaRenuncia').value = '<%=ofertaConDatos.getCodCausaRenuncia() != null ? ofertaConDatos.getCodCausaRenuncia().toUpperCase() : "" %>';
                        document.getElementById('codCausaRenunciaPresOferta').value = '<%=ofertaConDatos.getCodCausaRenunciaPresOferta() != null ? ofertaConDatos.getCodCausaRenunciaPresOferta().toUpperCase() : "" %>';
                        
                        <%
                        if(ofertaConDatos.getPrec() != null && ofertaConDatos.getPrec().equalsIgnoreCase(ConstantesMeLanbide60.CIERTO))
                        {
                        %>
                            document.getElementById('checkPrecandidatosOferta').checked = 'true';
                        <%
                        }
                        else
                        {
                        %>
                            document.getElementById('divNomPrecandidatos').style.display = 'none';
                        <%
                        }
                        %>
                        cambioValorCheckPrecandidatos(document.getElementById('checkPrecandidatosOferta'));
                        <%
                        if(ofertaConDatos.getDifusion() != null && ofertaConDatos.getDifusion().equalsIgnoreCase(ConstantesMeLanbide60.CIERTO))
                        {
                        %>
                            document.getElementById('checkDifusionOferta').checked = 'true';
                            document.getElementById('fechaDifusionOferta').value = '<%=ofertaConDatos.getFecDifusion() != null ? format.format(ofertaConDatos.getFecDifusion()) : "" %>';
                        <%
                        }
                        else                               
                        {
                        %>
                            document.getElementById('divFechaDifusion').style.display = 'none';  
                        <%
                        }
                        %>
                        
                        cambioValorCheckDifusion(document.getElementById('checkDifusionOferta'));
                        
                        document.getElementById('fechaEnvioCandidatosOferta').value = '<%=ofertaConDatos.getFecEnvCand() != null ? format.format(ofertaConDatos.getFecEnvCand()) : "" %>';
                        document.getElementById('txtNumCandidatosEnv').value = '<%=ofertaConDatos.getNumTotCand() != null ? ofertaConDatos.getNumTotCand().toString() : "" %>';
                        
                        <%
                        if(ofertaConDatos.getContratacion() != null && ofertaConDatos.getContratacion().equalsIgnoreCase(ConstantesMeLanbide60.FALSO))
                        {
                        %>
                                document.getElementById('radioContN').checked = 'true';
                                document.getElementById('divMotNoContratacion').style.display = 'inline';
                                document.getElementById('divRenuncia').style.display = 'inline';
                                document.getElementById('divBaja').style.display = 'none';
                        <%
                        }
                        else
                        {
                            if(ofertaConDatos.getContratacion() != null && ofertaConDatos.getContratacion().equalsIgnoreCase(ConstantesMeLanbide60.CIERTO))
                            {
                        %>
                                document.getElementById('radioContS').checked = 'true';
                            <%
                            }
                            %>
                                document.getElementById('divMotNoContratacion').style.display = 'none';
                                document.getElementById('divRenuncia').style.display = 'none';
                                document.getElementById('divBaja').style.display = 'inline';
                        <%
                        }
                        %>
                                
                        <%
                        if(ofertaConDatos.getContratacionPresOferta() != null && ofertaConDatos.getContratacionPresOferta().equalsIgnoreCase(ConstantesMeLanbide60.FALSO))
                        {
                        %>
                            document.getElementById('radioPresOfertaN').checked = 'true';
                            document.getElementById('divMotNoContratacion').style.display = 'inline';
                            document.getElementById('divRenuncia').style.display = 'inline';
                            document.getElementById('divBaja').style.display = 'none';
                        <%
                        }
                        else
                        {
                            if(ofertaConDatos.getContratacionPresOferta() != null && ofertaConDatos.getContratacionPresOferta().equalsIgnoreCase(ConstantesMeLanbide60.CIERTO))
                            {
                        %>
                                document.getElementById('radioPresOfertaS').checked = 'true';
                            <%
                            }
                            %>
                                document.getElementById('divMotNoContratacion').style.display = 'none';
                                document.getElementById('divRenuncia').style.display = 'none';
                                document.getElementById('divBaja').style.display = 'inline';
                        <%
                        }
                        %>
                                
                        if(document.getElementById('FECRES')){
                            var valor = document.getElementById('FECRES').value;
                            if(valor != undefined && valor != ''){
                                document.getElementById('fechaRenuncia').value = valor;
                                document.getElementById('divFechaRenuncia').style.display = 'inline';
                                document.getElementById('divTxtFechaRenuncia').style.display = 'inline';
                                document.getElementById('divTxtCausaRenuncia').style.marginLeft = '0px';
                                document.getElementById('divTxtCausaRenunciaPresOferta').style.marginLeft = '0px';
                            }else{
                                document.getElementById('fechaRenuncia').value = '';
                                document.getElementById('divFechaRenuncia').style.display = 'none';
                                document.getElementById('divTxtFechaRenuncia').style.display = 'none';
                                document.getElementById('divTxtCausaRenuncia').style.marginLeft = '194px';
                                document.getElementById('divTxtCausaRenunciaPresOferta').style.marginLeft = '194px';
                            }
                        }else{
                            document.getElementById('fechaRenuncia').value = '';
                            document.getElementById('divFechaRenuncia').style.display = 'none';
                            document.getElementById('divTxtFechaRenuncia').style.display = 'none';
                            document.getElementById('divTxtCausaRenuncia').style.marginLeft = '194px';
                            document.getElementById('divTxtCausaRenunciaPresOferta').style.marginLeft = '194px';
                        }
                                
                        //Datos contratado
                        document.getElementById('codNifContratado').value = '<%=ofertaModif.getCodTipoNif() != null ? ofertaModif.getCodTipoNif().toUpperCase() : "" %>';
                        document.getElementById('txtNifOferta').value = '<%=ofertaModif.getNif() != null ? ofertaModif.getNif().toUpperCase() : "" %>';
                        document.getElementById('txtNombre').value = '<%=ofertaModif.getNombre() != null ? ofertaModif.getNombre().toUpperCase() : "" %>';
                        document.getElementById('txtApe1').value = '<%=ofertaModif.getApe1() != null ? ofertaModif.getApe1().toUpperCase() : "" %>';
                        document.getElementById('txtApe2').value = '<%=ofertaModif.getApe2() != null ? ofertaModif.getApe2().toUpperCase() : "" %>';
                        document.getElementById('txtEmail').value = '<%=ofertaModif.getEmail() != null ? ofertaModif.getEmail().toUpperCase() : "" %>';
                        document.getElementById('txtTfno').value = '<%=ofertaModif.getTlf() != null ? ofertaModif.getTlf().toUpperCase() : "" %>';
                        document.getElementById('fechaNacContratado').value = '<%=ofertaModif.getFecNac() != null ? format.format(ofertaModif.getFecNac()) : "" %>';
                        document.getElementById('txtSexo').value = '<%=ofertaModif.getSexo() != null ? ofertaModif.getSexo().toUpperCase() : "" %>';
                        document.getElementById('codTitulacionContratado').value = '<%=ofertaModif.getCodTitulacion() != null ? ofertaModif.getCodTitulacion().toUpperCase() : "" %>';
                        document.getElementById('descTitulacionContratado').value = '<%=descTitContratado != null ? descTitContratado.toUpperCase() : "" %>';
                        document.getElementById('anoTitulacion').value = '<%=ofertaModif.getAnoTitulacion() != null ? ofertaModif.getAnoTitulacion().toString() : "" %>';
                        document.getElementById('fechaIniContratado').value = '<%=ofertaModif.getFecIni() != null ? format.format(ofertaModif.getFecIni()) : "" %>';
                        document.getElementById('fechaFinContratado').value = '<%=ofertaModif.getFecFin() != null ? format.format(ofertaModif.getFecFin()) : "" %>';
                        document.getElementById('txtMesesContratado').value = '<%=ofertaModif.getMesesContrato() != null ? ofertaModif.getMesesContrato().toString() : "" %>';
                        document.getElementById('codGrupoCotContratado').value = '<%=ofertaModif.getCodGrCot() != null ? ofertaModif.getCodGrCot().toUpperCase() : "" %>';
                        document.getElementById('txtSalarioContratado').value = '<%=ofertaModif.getSalarioB() != null ? ofertaModif.getSalarioB().toPlainString() : "" %>';
                        document.getElementById('txtDietasConv').value = '<%=ofertaModif.getDietasConvenio() != null ? ofertaModif.getDietasConvenio().toPlainString() : "" %>';
                        document.getElementById('txtDietasConvoc').value = '<%=ofertaModif.getDietasConvoc() != null ? ofertaModif.getDietasConvoc().toPlainString() : "" %>';
                        
                        reemplazarPuntosCme(document.getElementById('txtSalarioContratado'));
                        reemplazarPuntosCme(document.getElementById('txtDietasConv'));
                        reemplazarPuntosCme(document.getElementById('txtDietasConvoc'));
                        
                        //Datos baja
                        document.getElementById('fechaBaja').value = '<%=ofertaModif.getFecBaja() != null ? format.format(ofertaModif.getFecBaja()) : "" %>';
                        document.getElementById('codCausaBaja').value = '<%=ofertaModif.getCodCausaBaja() != null ? ofertaModif.getCodCausaBaja().toUpperCase() : "" %>';
                        <%
                        if(ofertaModif.getFlSustituto() != null && ofertaModif.getFlSustituto().equalsIgnoreCase(ConstantesMeLanbide60.CIERTO))
                        {
                        %>
                            document.getElementById('radioSustituyeS').checked = 'true';                                
                        <%
                        }
                        else if(ofertaModif.getFlSustituto() != null && ofertaModif.getFlSustituto().equalsIgnoreCase(ConstantesMeLanbide60.FALSO))
                        {
                        %>
                            document.getElementById('radioSustituyeN').checked = 'true'; 
                            //document.getElementById('divAltaSustituto').style.display = 'none';
                        <%
                        }
                        else
                        {
                        %>
                            //document.getElementById('divAltaSustituto').style.display = 'none';
                        <%
                        }
                    
                        if(ofertaModif.getDocGestOfe() != null)
                        {
                        %>
                            document.getElementById('btnVerDocGestionOferta').style.display = 'inline';
                            document.getElementById('btnModifDocGestionOferta').style.display = 'inline';
                            document.getElementById('docGestionOferta').style.display = 'none';
                            document.getElementById('btnCancelarModifDocGestionOferta').style.display = 'none';
                            
                        <%
                        }
                        else
                        {
                        %>
                            document.getElementById('btnVerDocGestionOferta').style.display = 'none';
                            document.getElementById('btnModifDocGestionOferta').style.display = 'none';
                            document.getElementById('docGestionOferta').style.display = 'inline';
                            document.getElementById('btnCancelarModifDocGestionOferta').style.display = 'none';
                        <%
                        }
                        
                        if(ofertaModif.getDocContratacion() != null)
                        {
                        %>
                            document.getElementById('btnVerDocContratacion').style.display = 'inline';
                            document.getElementById('btnModifDocContratacion').style.display = 'inline';
                            document.getElementById('docContratacion').style.display = 'none';
                            document.getElementById('btnCancelarModifDocContratacion').style.display = 'none';
                        <%
                        }
                        else
                        {
                        %>
                            document.getElementById('btnVerDocContratacion').style.display = 'none';
                            document.getElementById('btnModifDocContratacion').style.display = 'none';
                            document.getElementById('docContratacion').style.display = 'inline';
                            document.getElementById('btnCancelarModifDocContratacion').style.display = 'none';
                        <%
                        }
                        %>
                <%
                }
                else
                {
                %>
                    document.getElementById('divNomPrecandidatos').style.display = 'none';
                    document.getElementById('divFechaDifusion').style.display = 'none';
                    document.getElementById('divMotNoContratacion').style.display = 'none';
                    //document.getElementById('divAltaSustituto').style.display = 'none';
                    document.getElementById('btnVerDocContratacion').style.display = 'none';
                    document.getElementById('btnVerDocGestionOferta').style.display = 'none';
                <%
                }
                %>
                
                <%
                if(consulta == true)
                {
                %>
                    //Deshabilito todos los campos
                    
                    //Datos puesto
                    document.getElementById('descPuesto').readOnly = true;
                    document.getElementById('descPuesto').className = 'inputTexto readOnly';
                    document.getElementById('codPaisPuesto1').readOnly = true;
                    document.getElementById('codPaisPuesto1').className = 'inputTexto readOnly';
                    document.getElementById('descPaisPuesto1').readOnly = true;
                    document.getElementById('descPaisPuesto1').className = 'inputTexto readOnly';
                    document.getElementById('codPaisPuesto2').readOnly = true;
                    document.getElementById('codPaisPuesto2').className = 'inputTexto readOnly';
                    document.getElementById('descPaisPuesto2').readOnly = true;
                    document.getElementById('descPaisPuesto2').className = 'inputTexto readOnly';
                    document.getElementById('codPaisPuesto3').readOnly = true;
                    document.getElementById('codPaisPuesto3').className = 'inputTexto readOnly';
                    document.getElementById('descPaisPuesto3').readOnly = true;
                    document.getElementById('descPaisPuesto3').className = 'inputTexto readOnly';
                    document.getElementById('ciudadDestino').readOnly = true;
                    document.getElementById('ciudadDestino').className = 'inputTexto readOnly';
                    document.getElementById('dpto').readOnly = true;
                    document.getElementById('dpto').className = 'inputTexto readOnly';
                    document.getElementById('codTitulacionPuesto1').readOnly = true;
                    document.getElementById('codTitulacionPuesto1').className = 'inputTexto readOnly';
                    document.getElementById('descTitulacionPuesto1').readOnly = true;
                    document.getElementById('descTitulacionPuesto1').className = 'inputTexto readOnly';
                    document.getElementById('codTitulacionPuesto2').readOnly = true;
                    document.getElementById('codTitulacionPuesto2').className = 'inputTexto readOnly';
                    document.getElementById('descTitulacionPuesto2').readOnly = true;
                    document.getElementById('descTitulacionPuesto2').className = 'inputTexto readOnly';
                    document.getElementById('codTitulacionPuesto3').readOnly = true;
                    document.getElementById('codTitulacionPuesto3').className = 'inputTexto readOnly';
                    document.getElementById('descTitulacionPuesto3').readOnly = true;
                    document.getElementById('descTitulacionPuesto3').className = 'inputTexto readOnly';
                    document.getElementById('funcionesPuesto').readOnly = true;
                    document.getElementById('funcionesPuesto').className = 'inputTexto readOnly';
                    document.getElementById('codIdiomaPuesto1').readOnly = true;
                    document.getElementById('codIdiomaPuesto1').className = 'inputTexto readOnly';
                    document.getElementById('descIdiomaPuesto1').readOnly = true;
                    document.getElementById('descIdiomaPuesto1').className = 'inputTexto readOnly';
                    document.getElementById('codIdiomaPuesto2').readOnly = true;
                    document.getElementById('codIdiomaPuesto2').className = 'inputTexto readOnly';
                    document.getElementById('descIdiomaPuesto2').readOnly = true;
                    document.getElementById('descIdiomaPuesto2').className = 'inputTexto readOnly';
                    document.getElementById('codIdiomaPuesto3').readOnly = true;
                    document.getElementById('codIdiomaPuesto3').className = 'inputTexto readOnly';
                    document.getElementById('descIdiomaPuesto3').readOnly = true;
                    document.getElementById('descIdiomaPuesto3').className = 'inputTexto readOnly';
                    document.getElementById('codNivelIdiomaPuesto1').readOnly = true;
                    document.getElementById('codNivelIdiomaPuesto1').className = 'inputTexto readOnly';
                    document.getElementById('descNivelIdiomaPuesto1').readOnly = true;
                    document.getElementById('descNivelIdiomaPuesto1').className = 'inputTexto readOnly';
                    document.getElementById('codNivelIdiomaPuesto2').readOnly = true;
                    document.getElementById('codNivelIdiomaPuesto2').className = 'inputTexto readOnly';
                    document.getElementById('descNivelIdiomaPuesto2').readOnly = true;
                    document.getElementById('descNivelIdiomaPuesto2').className = 'inputTexto readOnly';
                    document.getElementById('codNivelIdiomaPuesto3').readOnly = true;
                    document.getElementById('codNivelIdiomaPuesto3').className = 'inputTexto readOnly';
                    document.getElementById('descNivelIdiomaPuesto3').readOnly = true;
                    document.getElementById('descNivelIdiomaPuesto3').className = 'inputTexto readOnly';
                    document.getElementById('codNivelFormativo').readOnly = true;
                    document.getElementById('codNivelFormativo').className = 'inputTexto readOnly';
                    document.getElementById('descNivelFormativo').readOnly = true;
                    document.getElementById('descNivelFormativo').className = 'inputTexto readOnly';
                    document.getElementById('btnCopiarDatosSoli').style.display = 'none';
                    document.getElementById('botonPaisPuesto1').style.display = 'none';
                    document.getElementById('botonPaisPuesto2').style.display = 'none';
                    document.getElementById('botonPaisPuesto3').style.display = 'none';
                    document.getElementById('btnBuscarTitulacionPuesto1').style.display = 'none';
                    document.getElementById('btnBuscarTitulacionPuesto2').style.display = 'none';
                    document.getElementById('btnBuscarTitulacionPuesto3').style.display = 'none';
                    document.getElementById('botonIdiomaPuesto1').style.display = 'none';
                    document.getElementById('botonIdiomaPuesto2').style.display = 'none';
                    document.getElementById('botonIdiomaPuesto3').style.display = 'none';
                    document.getElementById('botonNivelIdiomaPuesto1').style.display = 'none';
                    document.getElementById('botonNivelIdiomaPuesto2').style.display = 'none';
                    document.getElementById('botonNivelIdiomaPuesto3').style.display = 'none';
                    document.getElementById('botonNivelFormativo').style.display = 'none';
                    
                    
                    //Datos oferta
                    document.getElementById('txtNumOferta').readOnly = true;
                    document.getElementById('txtNumOferta').className = 'inputTexto readOnly';
                    document.getElementById('codOfiGestionOferta').readOnly = true;
                    document.getElementById('codOfiGestionOferta').className = 'inputTexto readOnly';
                    document.getElementById('descOfiGestionOferta').readOnly = true;
                    document.getElementById('descOfiGestionOferta').className = 'inputTexto readOnly';
                    document.getElementById('botonOfiGestionOferta').style.display = 'none';
                    document.getElementById('checkPrecandidatosOferta').disabled = true;
                    document.getElementById('txtNomPrecandidatos').readOnly = true;
                    document.getElementById('txtNomPrecandidatos').className = 'inputTexto readOnly';
                    document.getElementById('checkDifusionOferta').disabled = true;
                    document.getElementById('fechaEnvioCandidatosOferta').readOnly = true;
                    document.getElementById('fechaEnvioCandidatosOferta').className = 'inputTexto readOnly';
                    document.getElementById('txtNumCandidatosEnv').readOnly = true;
                    document.getElementById('txtNumCandidatosEnv').className = 'inputTexto readOnly textoNumerico';
                    document.getElementById('radioContS').disabled = true;
                    document.getElementById('radioContN').disabled = true;
                    document.getElementById('radioPresOfertaS').disabled = true;
                    document.getElementById('radioPresOfertaN').disabled = true;
                    document.getElementById('motivoNoCont').readOnly = true;
                    document.getElementById('motivoNoCont').className = 'inputTexto readOnly';
                    document.getElementById('btnModifDocGestionOferta').style.display = 'none';
                    document.getElementById('docGestionOferta').style.display = 'none';
                    document.getElementById('btnCancelarModifDocGestionOferta').style.display = 'none';
                    document.getElementById('calfechaEnvioCandidatosOferta').style.display = 'none';
                    document.getElementById('btnModifDocGestionOferta').style.display = 'none';
                    document.getElementById('docGestionOferta').style.display = 'none';
                    document.getElementById('btnCancelarModifDocGestionOferta').style.display = 'none';
                    document.getElementById('codCausaRenuncia').readOnly = true;
                    document.getElementById('codCausaRenuncia').className = 'inputTexto readOnly';
                    document.getElementById('descCausaRenuncia').readOnly = true;
                    document.getElementById('descCausaRenuncia').className = 'inputTexto readOnly';
                    document.getElementById('codCausaRenunciaPresOferta').readOnly = true;
                    document.getElementById('codCausaRenunciaPresOferta').className = 'inputTexto readOnly';
                    document.getElementById('descCausaRenunciaPresOferta').readOnly = true;
                    document.getElementById('descCausaRenunciaPresOferta').className = 'inputTexto readOnly';
                    
                    //Datos contratado
                    document.getElementById('codNifContratado').readOnly = true;
                    document.getElementById('codNifContratado').className = 'inputTexto readOnly';
                    document.getElementById('descNifContratado').readOnly = true;
                    document.getElementById('descNifContratado').className = 'inputTexto readOnly';
                    document.getElementById('botonNifContratado').style.display = 'none';
                    document.getElementById('txtNifOferta').readOnly = true;
                    document.getElementById('txtNifOferta').className = 'inputTexto readOnly';
                    document.getElementById('txtNombre').readOnly = true;
                    document.getElementById('txtNombre').className = 'inputTexto readOnly';
                    document.getElementById('txtApe1').readOnly = true;
                    document.getElementById('txtApe1').className = 'inputTexto readOnly';
                    document.getElementById('txtApe2').readOnly = true;
                    document.getElementById('txtApe2').className = 'inputTexto readOnly';
                    document.getElementById('txtEmail').readOnly = true;
                    document.getElementById('txtEmail').className = 'inputTexto readOnly';
                    document.getElementById('txtTfno').readOnly = true;
                    document.getElementById('txtTfno').className = 'inputTexto readOnly';
                    document.getElementById('fechaNacContratado').readOnly = true;
                    document.getElementById('fechaNacContratado').className = 'inputTexto readOnly';
                    document.getElementById('calfechaNacContratado').style.display = 'none';
                    document.getElementById('txtSexo').readOnly = true;
                    document.getElementById('txtSexo').className = 'inputTexto readOnly';
                    document.getElementById('codTitulacionContratado').readOnly = true;
                    document.getElementById('codTitulacionContratado').className = 'inputTexto readOnly';
                    document.getElementById('descTitulacionContratado').readOnly = true;
                    document.getElementById('descTitulacionContratado').className = 'inputTexto readOnly';
                    document.getElementById('btnBuscarTitulacionContratado').style.display = 'none';
                    document.getElementById('anoTitulacion').readOnly = true;
                    document.getElementById('anoTitulacion').className = 'inputTexto readOnly';
                    document.getElementById('fechaIniContratado').readOnly = true;
                    document.getElementById('fechaIniContratado').className = 'inputTexto readOnly';
                    document.getElementById('calfechaIniContratado').style.display = 'none';
                    document.getElementById('fechaFinContratado').readOnly = true;
                    document.getElementById('fechaFinContratado').className = 'inputTexto readOnly';
                    document.getElementById('calfechaFinContratado').style.display = 'none';
                    document.getElementById('codGrupoCotContratado').readOnly = true;
                    document.getElementById('codGrupoCotContratado').className = 'inputTexto readOnly';
                    document.getElementById('descGrupoCotContratado').readOnly = true;
                    document.getElementById('descGrupoCotContratado').className = 'inputTexto readOnly';
                    document.getElementById('botonGrupoCotContratado').style.display = 'none';
                    document.getElementById('txtSalarioContratado').readOnly = true;
                    document.getElementById('txtSalarioContratado').className = 'inputTexto readOnly textoNumerico';
                    document.getElementById('txtDietasConv').readOnly = true;
                    document.getElementById('txtDietasConv').className = 'inputTexto readOnly textoNumerico';
                    document.getElementById('txtDietasConvoc').readOnly = true;
                    document.getElementById('txtDietasConvoc').className = 'inputTexto readOnly textoNumerico';
                    document.getElementById('btnModifDocContratacion').style.display = 'none';
                    document.getElementById('docContratacion').style.display = 'none';
                    document.getElementById('btnCancelarModifDocContratacion').style.display = 'none';
                    document.getElementById('txtMesesContratado').readOnly = true;
                    document.getElementById('txtMesesContratado').className = 'inputTexto readOnly textoNumerico';
                    
                    //Datos baja
                    document.getElementById('fechaBaja').readOnly = true;
                    document.getElementById('fechaBaja').className = 'inputTexto readOnly';
                    document.getElementById('calFechaBaja').style.display = 'none';
                    document.getElementById('codCausaBaja').readOnly = true;
                    document.getElementById('codCausaBaja').className = 'inputTexto readOnly';
                    document.getElementById('descCausaBaja').readOnly = true;
                    document.getElementById('descCausaBaja').className = 'inputTexto readOnly';
                    document.getElementById('botonCausaBaja').style.display = 'none';
                    document.getElementById('radioSustituyeS').disabled = true;
                    document.getElementById('radioSustituyeN').disabled = true;
                    document.getElementById('observacionesBaja').readOnly = true;
                    document.getElementById('observacionesBaja').className = 'inputTexto readOnly';
                    document.getElementById('btnAltaSustituto').style.display = 'none';
                    
                    //Botones
                    document.getElementById('btnGuardarContrato').style.display = 'none';
                    document.getElementById('btnCancelarContrato').style.display = 'none';
                    document.getElementById('btnCerrar').style.display = 'inline';
					document.getElementById('anchorCausaRenunciaPresOferta').style.display = 'none';
                <%
                }
                else
                {
                    if(ofertaModif != null && ofertaModif.getIdOfertaOrigen() != null)
                    {
                %>
                        //Como es una oferta copiada de otra, los datos de la oferta aparecen a modo consulta
                       /* document.getElementById('txtNumOferta').readOnly = true;
                        document.getElementById('txtNumOferta').className = 'inputTexto readOnly';
                        document.getElementById('codOfiGestionOferta').readOnly = true;
                        document.getElementById('codOfiGestionOferta').className = 'inputTexto readOnly';
                        document.getElementById('descOfiGestionOferta').readOnly = true;
                        document.getElementById('descOfiGestionOferta').className = 'inputTexto readOnly';
                        document.getElementById('botonOfiGestionOferta').style.display = 'none';
                        document.getElementById('checkPrecandidatosOferta').disabled = true;
                        document.getElementById('txtNomPrecandidatos').readOnly = true;
                        document.getElementById('txtNomPrecandidatos').className = 'inputTexto readOnly';
                        document.getElementById('checkDifusionOferta').disabled = true;
                        document.getElementById('fechaDifusionOferta').readOnly = true;
                        document.getElementById('fechaDifusionOferta').className = 'inputTexto readOnly';
                        document.getElementById('fechaEnvioCandidatosOferta').readOnly = true;
                        document.getElementById('fechaEnvioCandidatosOferta').className = 'inputTexto readOnly';
                        document.getElementById('txtNumCandidatosEnv').readOnly = true;
                        document.getElementById('txtNumCandidatosEnv').className = 'inputTexto readOnly';
                        document.getElementById('radioContS').disabled = true;
                        document.getElementById('radioContN').disabled = true;
                        document.getElementById('motivoNoCont').readOnly = true;
                        document.getElementById('motivoNoCont').className = 'inputTexto readOnly';
                        document.getElementById('btnModifDocGestionOferta').style.display = 'none';
                        document.getElementById('docGestionOferta').style.display = 'none';
                        document.getElementById('btnCancelarModifDocGestionOferta').style.display = 'none';
                        document.getElementById('calfechaDifusionOferta').style.display = 'none';
                        document.getElementById('calfechaEnvioCandidatosOferta').style.display = 'none';
                        */
                      /* datos puesto se copian de la otra oferta, pero modificables
                       *   
                       *       document.getElementById('descPuesto').readOnly = true;
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
                    document.getElementById('codTitulacionPuesto1').readOnly = true;
                    document.getElementById('codTitulacionPuesto1').className = 'inputTexto readOnly';
                    document.getElementById('descTitulacionPuesto1').readOnly = true;
                    document.getElementById('descTitulacionPuesto1').className = 'inputTexto readOnly';
                    document.getElementById('anchorTitulacionPuesto1').style.display = 'none';
                    document.getElementById('codTitulacionPuesto2').readOnly = true;
                    document.getElementById('codTitulacionPuesto2').className = 'inputTexto readOnly';
                    document.getElementById('descTitulacionPuesto2').readOnly = true;
                    document.getElementById('descTitulacionPuesto2').className = 'inputTexto readOnly';
                    document.getElementById('anchorTitulacionPuesto2').style.display = 'none';
                    document.getElementById('codTitulacionPuesto3').readOnly = true;
                    document.getElementById('codTitulacionPuesto3').className = 'inputTexto readOnly';
                    document.getElementById('descTitulacionPuesto3').readOnly = true;
                    document.getElementById('descTitulacionPuesto3').className = 'inputTexto readOnly';
                    document.getElementById('anchorTitulacionPuesto3').style.display = 'none';
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
                    
                        */
                        
                <%
                        if(ofertaModif.getDocGestOfe() == null)
                        {
                            %>
                                document.getElementById('btnVerDocGestionOferta').style.display = 'none';
                            <%
                        }
                    }
                    else
                    {
                %>
                  /*  comboPaisPuesto1 = new Combo('PaisPuesto1');
                    comboPaisPuesto1.change = cambioPais1;

                    comboPaisPuesto2 = new Combo('PaisPuesto2');
                    comboPaisPuesto2.change = cambioPais2;
                    
                    comboPaisPuesto3 = new Combo('PaisPuesto3');
                    comboPaisPuesto3.change = cambioPais3;
                    
                    comboTitulacionPuesto1 = new Combo('TitulacionPuesto1');
                    comboTitulacionPuesto2 = new Combo('TitulacionPuesto2');
                    comboTitulacionPuesto3 = new Combo('TitulacionPuesto3');
                    comboIdiomaPuesto1 = new Combo('IdiomaPuesto1');
                    comboNivelIdiomaPuesto1 = new Combo('NivelIdiomaPuesto1');
                    comboIdiomaPuesto2 = new Combo('IdiomaPuesto2');
                    comboNivelIdiomaPuesto2 = new Combo('NivelIdiomaPuesto2');
                    comboIdiomaPuesto3 = new Combo('IdiomaPuesto3');
                    comboNivelIdiomaPuesto3 = new Combo('NivelIdiomaPuesto3');
                    comboNivelFormativo = new Combo('NivelFormativo');
                    //comboNivelFormativo.change = cambioNivelFormativo;    
                        
                    //Datos oferta
                    comboOfiGestionOferta = new Combo('OfiGestionOferta');*/
                <%
                    }
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
                    //comboNivelFormativo.change = cambioNivelFormativo;    
                        
                    //Datos oferta
                    comboOfiGestionOferta = new Combo('OfiGestionOferta');
                    comboCausaRenuncia = new Combo('CausaRenuncia');
                    comboCausaRenunciaPresOferta = new Combo('CausaRenunciaPresOferta');

                    //Datos contratado
                    comboNifContratado = new Combo('NifContratado');
                    comboNifContratado.change = buscarTercero;
                    comboGrupoCotContratado = new Combo('GrupoCotContratado');

                    //Datos baja
                    comboCausaBaja = new Combo('CausaBaja');
                    
                    cargarCombos();
                <%
                }
                %>
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
                var resultado = jsp_alerta('','<%=meLanbide60I18n.getMensaje(idiomaUsuario, "msg.preguntaCancelar")%>');
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

            function mostrarCalFechaBaja(evento) {
                if(window.event) 
                    evento = window.event;
                if (document.getElementById('calFechaBaja').src.indexOf('icono.gif') != -1 )
                    showCalendar('forms[0]','fechaBaja',null,null,null,'','calFechaBaja','',null,null,null,null,null,null,null,null,evento);
            }

            function mostrarCalFechaDifusionOferta(evento) {
                if(window.event) 
                    evento = window.event;
                if (document.getElementById('calfechaDifusionOferta').src.indexOf('icono.gif') != -1 )
                    showCalendar('forms[0]','fechaDifusionOferta',null,null,null,'','calfechaDifusionOferta','',null,null,null,null,null,null,null,null,evento);
            }

            function mostrarCalFechaIniContratado(evento) {
                if(window.event) 
                    evento = window.event;
                if (document.getElementById('calfechaIniContratado').src.indexOf('icono.gif') != -1 )
                    showCalendar('forms[0]','fechaIniContratado',null,null,null,'','calfechaIniContratado','',null,null,null,null,null,null,null,'calcularMesesContratado()',evento);
            }

            function mostrarCalFechaFinContratado(evento) {
                if(window.event) 
                    evento = window.event;
                if (document.getElementById('calfechaFinContratado').src.indexOf('icono.gif') != -1 )
                    showCalendar('forms[0]','fechaFinContratado',null,null,null,'','calfechaFinContratado','',null,null,null,null,null,null,null,'calcularMesesContratado()',evento);
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
        
            function cambioValorCheckPrecandidatos(check){
                cambioValorCheck(check);
                if(check.checked){
                    document.getElementById('divNomPrecandidatos').style.display = 'inline';
                }
                else{
                    document.getElementById('divNomPrecandidatos').style.display = 'none';
                }
            }
        
            function cambioValorCheckDifusion(check){
                cambioValorCheck(check);
                if(check.checked){
                    document.getElementById('divFechaDifusion').style.display = 'inline';
                }
                else{
                    document.getElementById('divFechaDifusion').style.display = 'none';
                }
            }
            
            function cambioRadioContratacion(radio){
                radio.blur();
                radio.focus();
                var radioS = document.getElementById('radioContS');
                if(radioS.checked){
                    document.getElementById('divMotNoContratacion').style.display = 'none';
                    document.getElementById('divRenuncia').style.display = 'none';
                    document.getElementById('codNifContratado').readOnly = false;                    
                    document.getElementById('codNifContratado').className = 'inputTexto';
                    document.getElementById('descNifContratado').readOnly = false;                    
                    document.getElementById('descNifContratado').className = 'inputTexto';
                    document.getElementById('txtNifOferta').readOnly = false;                    
                    document.getElementById('txtNifOferta').className = 'inputTexto';
                    document.getElementById('txtNombre').readOnly = false;                    
                    document.getElementById('txtNombre').className = 'inputTexto';
                    document.getElementById('txtApe1').readOnly = false;                    
                    document.getElementById('txtApe1').className = 'inputTexto';
                    document.getElementById('txtApe2').readOnly = false;                    
                    document.getElementById('txtApe2').className = 'inputTexto';
                    document.getElementById('txtEmail').readOnly = false;                    
                    document.getElementById('txtEmail').className = 'inputTexto';
                    document.getElementById('txtTfno').readOnly = false;                    
                    document.getElementById('txtTfno').className = 'inputTexto';
                    document.getElementById('fechaNacContratado').readOnly = false;                    
                    document.getElementById('fechaNacContratado').className = 'inputTexto';
                    document.getElementById('calfechaNacContratado').style.display = 'inline'; 
                    document.getElementById('txtSexo').readOnly = false;                    
                    document.getElementById('txtSexo').className = 'inputTexto';
                    document.getElementById('codTitulacionContratado').readOnly = false;                    
                    document.getElementById('codTitulacionContratado').className = 'inputTexto';
                    document.getElementById('descTitulacionContratado').readOnly = false;                    
                    document.getElementById('descTitulacionContratado').className = 'inputTexto';
                    document.getElementById('botonNifContratado').style.display = 'inline'; 
                    document.getElementById('anoTitulacion').readOnly = false;                    
                    document.getElementById('anoTitulacion').className = 'inputTexto ';
                    document.getElementById('docContratacion').disabled = false;                    
                    document.getElementById('txtSexo').className = 'inputTexto ';
                    document.getElementById('fechaIniContratado').readOnly = false;                    
                    document.getElementById('fechaIniContratado').className = 'inputTexto ';
                    document.getElementById('calfechaIniContratado').style.display = 'inline'; 
                    document.getElementById('fechaFinContratado').readOnly = false;                    
                    document.getElementById('fechaFinContratado').className = 'inputTexto ';
                    document.getElementById('calfechaFinContratado').style.display = 'inline'; 
                    document.getElementById('codGrupoCotContratado').readOnly = false;                    
                    document.getElementById('codGrupoCotContratado').className = 'inputTexto ';
                    document.getElementById('descGrupoCotContratado').readOnly = false;                    
                    document.getElementById('descGrupoCotContratado').className = 'inputTexto ';
                    document.getElementById('botonGrupoCotContratado').style.display = 'inline'; 
                    document.getElementById('txtSalarioContratado').readOnly = false;  
                    document.getElementById('txtSalarioContratado').className = 'inputTexto textoNumerico';
                    document.getElementById('txtDietasConv').readOnly = false;                    
                    document.getElementById('txtDietasConv').className = 'inputTexto textoNumerico';
                    document.getElementById('txtDietasConvoc').readOnly = false;                    
                    document.getElementById('txtDietasConvoc').className = 'inputTexto textoNumerico';
                    /*document.getElementById('fechaBaja').value='';
                    document.getElementById('codCausaBaja').value='';
                    document.getElementById('descCausaBaja').value='';    
                    document.getElementsByName('radioSustituye').value='';
                    document.getElementById('radioSustituyeN').checked=false;
                    document.getElementById('radioSustituyeS').checked=false;*/
                    
                    
                    document.getElementById('fieldsetDatosPersonaContratada').style.display = 'inline';
                    document.getElementById('fieldsetDatosBaja').style.display = 'inline';
                    document.getElementById('divBaja').style.display = 'inline';
                }else{                    
                    document.getElementById('divMotNoContratacion').style.display = 'inline';
                    document.getElementById('divRenuncia').style.display = 'inline';
                    document.getElementById('codNifContratado').readOnly = true;                    
                    document.getElementById('codNifContratado').className = 'inputTexto readOnly';
                    document.getElementById('codNifContratado').value='';
                    document.getElementById('descNifContratado').readOnly = true;                    
                    document.getElementById('descNifContratado').className = 'inputTexto readOnly';
                    document.getElementById('descNifContratado').value='';
                    document.getElementById('txtNifOferta').readOnly = true;                    
                    document.getElementById('txtNifOferta').className = 'inputTexto readOnly';
                    document.getElementById('txtNifOferta').value='';
                    document.getElementById('txtNombre').readOnly = true;                    
                    document.getElementById('txtNombre').className = 'inputTexto readOnly';
                    document.getElementById('txtNombre').value='';
                    document.getElementById('txtApe1').readOnly = true;                    
                    document.getElementById('txtApe1').className = 'inputTexto readOnly';
                    document.getElementById('txtApe1').value='';
                    document.getElementById('txtApe2').readOnly = true;                    
                    document.getElementById('txtApe2').className = 'inputTexto readOnly';
                    document.getElementById('txtApe2').value='';
                    document.getElementById('txtEmail').readOnly = true;                    
                    document.getElementById('txtEmail').className = 'inputTexto readOnly';
                    document.getElementById('txtEmail').value='';
                    document.getElementById('txtTfno').readOnly = true;                    
                    document.getElementById('txtTfno').className = 'inputTexto readOnly';
                    document.getElementById('txtTfno').value='';
                    document.getElementById('fechaNacContratado').readOnly = true;                    
                    document.getElementById('fechaNacContratado').className = 'inputTexto readOnly';
                    document.getElementById('calfechaNacContratado').style.display = 'none'; 
                    document.getElementById('fechaNacContratado').value='';
                    document.getElementById('txtSexo').readOnly = true;                    
                    document.getElementById('txtSexo').className = 'inputTexto readOnly';
                    document.getElementById('txtSexo').value='';
                    document.getElementById('codTitulacionContratado').readOnly = true;                    
                    document.getElementById('codTitulacionContratado').className = 'inputTexto readOnly';
                     document.getElementById('codTitulacionContratado').value='';
                    document.getElementById('descTitulacionContratado').readOnly = true;                    
                    document.getElementById('descTitulacionContratado').className = 'inputTexto readOnly';
                     document.getElementById('descTitulacionContratado').value='';
                    document.getElementById('botonNifContratado').style.display = 'none'; 
                    document.getElementById('anoTitulacion').readOnly = true;                    
                    document.getElementById('anoTitulacion').className = 'inputTexto readOnly';
                    document.getElementById('anoTitulacion').value='';
                    document.getElementById('docContratacion').disabled = true;                  
                    
                    document.getElementById('fechaIniContratado').readOnly = true;                    
                    document.getElementById('fechaIniContratado').className = 'inputTexto readOnly';
                    document.getElementById('fechaIniContratado').value='';
                    document.getElementById('calfechaIniContratado').style.display = 'none'; 
                    document.getElementById('fechaFinContratado').readOnly = true;                    
                    document.getElementById('fechaFinContratado').className = 'inputTexto readOnly';
                    document.getElementById('fechaFinContratado').value='';
                    document.getElementById('calfechaFinContratado').style.display = 'none'; 
                    document.getElementById('codGrupoCotContratado').readOnly = true;                    
                    document.getElementById('codGrupoCotContratado').className = 'inputTexto readOnly';
                    document.getElementById('codGrupoCotContratado').value='';
                    document.getElementById('descGrupoCotContratado').readOnly = true;                    
                    document.getElementById('descGrupoCotContratado').className = 'inputTexto readOnly';
                    document.getElementById('descGrupoCotContratado').value='';
                    document.getElementById('botonGrupoCotContratado').style.display = 'none'; 
                    document.getElementById('txtSalarioContratado').readOnly = true;  
                    document.getElementById('txtSalarioContratado').className = 'inputTexto readOnly textoNumerico';
                    document.getElementById('txtSalarioContratado').value='';
                    document.getElementById('txtDietasConv').readOnly = true;                    
                    document.getElementById('txtDietasConv').className = 'inputTexto readOnly textoNumerico';
                    document.getElementById('txtDietasConv').value='';
                    document.getElementById('txtDietasConvoc').readOnly = true;                    
                    document.getElementById('txtDietasConvoc').className = 'inputTexto readOnly textoNumerico';
                    document.getElementById('txtDietasConvoc').value='';
                    /*var hoy = new Date();
                    var dd = hoy.getDate(); 
                    var mm = hoy.getMonth()+1;//January is 0! 
                    var yyyy = hoy.getFullYear(); 
                    if(dd<10){dd='0'+dd} 
                    if(mm<10){mm='0'+mm} 
                    document.getElementById('fechaBaja').value=dd+'/'+mm+'/'+yyyy;
                    document.getElementById('codCausaBaja').value='007';
                    document.getElementById('descCausaBaja').value='NO CONTRATADO';    
                    document.getElementById('radioSustituyeN').checked=true;
                    document.getElementById('radioSustituyeS').checked=false;*/
                    
                    
                    document.getElementById('fieldsetDatosPersonaContratada').style.display = 'none';
                    document.getElementById('fieldsetDatosBaja').style.display = 'none';
                    document.getElementById('divBaja').style.display = 'none';
                }
            }
            
            function cambioRadioPresOferta(radio){
                radio.blur();
                radio.focus();
                var radioS = document.getElementById('radioPresOfertaS');
                if(radioS.checked){
                    //document.getElementById('divDatosOferta').style.display = 'none';
                    document.getElementById('divTxtCausaRenunciaPresOferta').style.display = 'none';
                    document.getElementById('divMotNoContratacion').style.display = 'none';
                    document.getElementById('divRenuncia').style.display = 'none';
                    document.getElementById('codNifContratado').readOnly = false;                    
                    document.getElementById('codNifContratado').className = 'inputTexto';
                    document.getElementById('descNifContratado').readOnly = false;                    
                    document.getElementById('descNifContratado').className = 'inputTexto';
                    document.getElementById('txtNifOferta').readOnly = false;                    
                    document.getElementById('txtNifOferta').className = 'inputTexto';
                    document.getElementById('txtNombre').readOnly = false;                    
                    document.getElementById('txtNombre').className = 'inputTexto';
                    document.getElementById('txtApe1').readOnly = false;                    
                    document.getElementById('txtApe1').className = 'inputTexto';
                    document.getElementById('txtApe2').readOnly = false;                    
                    document.getElementById('txtApe2').className = 'inputTexto';
                    document.getElementById('txtEmail').readOnly = false;                    
                    document.getElementById('txtEmail').className = 'inputTexto';
                    document.getElementById('txtTfno').readOnly = false;                    
                    document.getElementById('txtTfno').className = 'inputTexto';
                    document.getElementById('fechaNacContratado').readOnly = false;                    
                    document.getElementById('fechaNacContratado').className = 'inputTexto';
                    document.getElementById('calfechaNacContratado').style.display = 'inline'; 
                    document.getElementById('txtSexo').readOnly = false;                    
                    document.getElementById('txtSexo').className = 'inputTexto';
                    document.getElementById('codTitulacionContratado').readOnly = false;                    
                    document.getElementById('codTitulacionContratado').className = 'inputTexto';
                    document.getElementById('descTitulacionContratado').readOnly = false;                    
                    document.getElementById('descTitulacionContratado').className = 'inputTexto';
                    document.getElementById('botonNifContratado').style.display = 'inline'; 
                    document.getElementById('anoTitulacion').readOnly = false;                    
                    document.getElementById('anoTitulacion').className = 'inputTexto ';
                    document.getElementById('docContratacion').disabled = false;                    
                    document.getElementById('txtSexo').className = 'inputTexto ';
                    document.getElementById('fechaIniContratado').readOnly = false;                    
                    document.getElementById('fechaIniContratado').className = 'inputTexto ';
                    document.getElementById('calfechaIniContratado').style.display = 'inline'; 
                    document.getElementById('fechaFinContratado').readOnly = false;                    
                    document.getElementById('fechaFinContratado').className = 'inputTexto ';
                    document.getElementById('calfechaFinContratado').style.display = 'inline'; 
                    document.getElementById('codGrupoCotContratado').readOnly = false;                    
                    document.getElementById('codGrupoCotContratado').className = 'inputTexto ';
                    document.getElementById('descGrupoCotContratado').readOnly = false;                    
                    document.getElementById('descGrupoCotContratado').className = 'inputTexto ';
                    document.getElementById('botonGrupoCotContratado').style.display = 'inline'; 
                    document.getElementById('txtSalarioContratado').readOnly = false;  
                    document.getElementById('txtSalarioContratado').className = 'inputTexto textoNumerico';
                    document.getElementById('txtDietasConv').readOnly = false;                    
                    document.getElementById('txtDietasConv').className = 'inputTexto textoNumerico';
                    document.getElementById('txtDietasConvoc').readOnly = false;                    
                    document.getElementById('txtDietasConvoc').className = 'inputTexto textoNumerico';
                    /*document.getElementById('fechaBaja').value='';
                    document.getElementById('codCausaBaja').value='';
                    document.getElementById('descCausaBaja').value='';    
                    document.getElementsByName('radioSustituye').value='';
                    document.getElementById('radioSustituyeN').checked=false;
                    document.getElementById('radioSustituyeS').checked=false;*/
                    
                    document.getElementById('fieldsetDatosOferta').style.display = 'inline';
                    document.getElementById('fieldsetDatosPersonaContratada').style.display = 'inline';
                    document.getElementById('fieldsetDatosBaja').style.display = 'inline';
                    document.getElementById('divBaja').style.display = 'inline';
                }else{
                    //document.getElementById('divDatosOferta').style.display = 'inline';
                    document.getElementById('divTxtCausaRenunciaPresOferta').style.display = 'inline';
                    document.getElementById('divMotNoContratacion').style.display = 'inline';
                    document.getElementById('divRenuncia').style.display = 'inline';
                    //document.getElementById('codOfiGestionOferta').readOnly = true;                    
                    //document.getElementById('codOfiGestionOferta').className = 'inputTexto readOnly';
                    document.getElementById('codOfiGestionOferta').value='';
                    //document.getElementById('descOfiGestionOferta').readOnly = true;                    
                    //document.getElementById('descOfiGestionOferta').className = 'inputTexto readOnly';
                    document.getElementById('descOfiGestionOferta').value='';
                    //document.getElementById('txtNumOferta').readOnly = true;                    
                    //document.getElementById('txtNumOferta').className = 'inputTexto readOnly';
                    document.getElementById('txtNomPrecandidatos').value='';
                    document.getElementById('checkPrecandidatosOferta').checked = false;
                    document.getElementById('txtNumOferta').value='';
                    document.getElementById('codNifContratado').readOnly = true;                    
                    document.getElementById('codNifContratado').className = 'inputTexto readOnly';
                    document.getElementById('codNifContratado').value='';
                    document.getElementById('descNifContratado').readOnly = true;                    
                    document.getElementById('descNifContratado').className = 'inputTexto readOnly';
                    document.getElementById('descNifContratado').value='';
                    document.getElementById('txtNifOferta').readOnly = true;                    
                    document.getElementById('txtNifOferta').className = 'inputTexto readOnly';
                    document.getElementById('txtNifOferta').value='';
                    document.getElementById('txtNombre').readOnly = true;                    
                    document.getElementById('txtNombre').className = 'inputTexto readOnly';
                    document.getElementById('txtNombre').value='';
                    document.getElementById('txtApe1').readOnly = true;                    
                    document.getElementById('txtApe1').className = 'inputTexto readOnly';
                    document.getElementById('txtApe1').value='';
                    document.getElementById('txtApe2').readOnly = true;                    
                    document.getElementById('txtApe2').className = 'inputTexto readOnly';
                    document.getElementById('txtApe2').value='';
                    document.getElementById('txtEmail').readOnly = true;                    
                    document.getElementById('txtEmail').className = 'inputTexto readOnly';
                    document.getElementById('txtEmail').value='';
                    document.getElementById('txtTfno').readOnly = true;                    
                    document.getElementById('txtTfno').className = 'inputTexto readOnly';
                    document.getElementById('txtTfno').value='';
                    document.getElementById('fechaNacContratado').readOnly = true;                    
                    document.getElementById('fechaNacContratado').className = 'inputTexto readOnly';
                    document.getElementById('calfechaNacContratado').style.display = 'none'; 
                    document.getElementById('fechaNacContratado').value='';
                    document.getElementById('txtSexo').readOnly = true;                    
                    document.getElementById('txtSexo').className = 'inputTexto readOnly';
                    document.getElementById('txtSexo').value='';
                    document.getElementById('codTitulacionContratado').readOnly = true;                    
                    document.getElementById('codTitulacionContratado').className = 'inputTexto readOnly';
                    document.getElementById('codTitulacionContratado').value='';
                    document.getElementById('descTitulacionContratado').readOnly = true;                    
                    document.getElementById('descTitulacionContratado').className = 'inputTexto readOnly';
                    document.getElementById('descTitulacionContratado').value='';
                    document.getElementById('botonNifContratado').style.display = 'none'; 
                    document.getElementById('anoTitulacion').readOnly = true;                    
                    document.getElementById('anoTitulacion').className = 'inputTexto readOnly';
                    document.getElementById('anoTitulacion').value='';
                    document.getElementById('docContratacion').disabled = true;                  
                    
                    document.getElementById('fechaIniContratado').readOnly = true;                    
                    document.getElementById('fechaIniContratado').className = 'inputTexto readOnly';
                    document.getElementById('fechaIniContratado').value='';
                    document.getElementById('calfechaIniContratado').style.display = 'none'; 
                    document.getElementById('fechaFinContratado').readOnly = true;                    
                    document.getElementById('fechaFinContratado').className = 'inputTexto readOnly';
                    document.getElementById('fechaFinContratado').value='';
                    document.getElementById('calfechaFinContratado').style.display = 'none'; 
                    document.getElementById('codGrupoCotContratado').readOnly = true;                    
                    document.getElementById('codGrupoCotContratado').className = 'inputTexto readOnly';
                    document.getElementById('codGrupoCotContratado').value='';
                    document.getElementById('descGrupoCotContratado').readOnly = true;                    
                    document.getElementById('descGrupoCotContratado').className = 'inputTexto readOnly';
                    document.getElementById('descGrupoCotContratado').value='';
                    document.getElementById('botonGrupoCotContratado').style.display = 'none'; 
                    document.getElementById('txtSalarioContratado').readOnly = true;  
                    document.getElementById('txtSalarioContratado').className = 'inputTexto readOnly textoNumerico';
                    document.getElementById('txtSalarioContratado').value='';
                    document.getElementById('txtDietasConv').readOnly = true;                    
                    document.getElementById('txtDietasConv').className = 'inputTexto readOnly textoNumerico';
                    document.getElementById('txtDietasConv').value='';
                    document.getElementById('txtDietasConvoc').readOnly = true;                    
                    document.getElementById('txtDietasConvoc').className = 'inputTexto readOnly textoNumerico';
                    document.getElementById('txtDietasConvoc').value='';
                    /*var hoy = new Date();
                    var dd = hoy.getDate(); 
                    var mm = hoy.getMonth()+1;//January is 0! 
                    var yyyy = hoy.getFullYear(); 
                    if(dd<10){dd='0'+dd} 
                    if(mm<10){mm='0'+mm} 
                    document.getElementById('fechaBaja').value=dd+'/'+mm+'/'+yyyy;
                    document.getElementById('codCausaBaja').value='007';
                    document.getElementById('descCausaBaja').value='NO CONTRATADO';    
                    document.getElementById('radioSustituyeN').checked=true;
                    document.getElementById('radioSustituyeS').checked=false;*/
                    
                    document.getElementById('fieldsetDatosOferta').style.display = 'none';
                    document.getElementById('fieldsetDatosPersonaContratada').style.display = 'none';
                    document.getElementById('fieldsetDatosBaja').style.display = 'none';
                    document.getElementById('divBaja').style.display = 'none';
                }
            }
            
            function cambioRadioSustituye(radio){
                radio.blur();
                radio.focus();
                var radioN = document.getElementById('radioSustituyeN');
                if(radioN.checked){
                    //document.getElementById('divAltaSustituto').style.display = 'inline';
                    document.getElementById('btnAltaSustituto').style.display = 'none';
                }else if(document.getElementById('fechaBaja').value==''){
                    document.getElementById('btnAltaSustituto').style.display = 'none';
                }else if(document.getElementById('descCausaBaja').value==''){
                    document.getElementById('btnAltaSustituto').style.display = 'none';
                }else{
                    document.getElementById('btnAltaSustituto').style.display = 'inline';
                }
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
                if(comboOfiGestionOferta){
                    comboOfiGestionOferta.addItems(codOfiGestion, descOfiGestion);
                }
                if(comboCausaRenuncia){
                    comboCausaRenuncia.addItems(codCausaRenuncia, descCausaRenuncia);
                }
                
                if(comboCausaRenunciaPresOferta){
                    comboCausaRenunciaPresOferta.addItems(codCausaRenunciaPresOferta, descCausaRenunciaPresOferta);
                }
                comboNifContratado.addItems(codNif, descNif);
                comboCausaBaja.addItems(codCausaBaja, descCausaBaja);
                comboGrupoCotContratado.addItems(codGrupoCot, descGrupoCot);
            }
        
            function  cargarDescripcionPais1(codigo){                
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
            }
            
            function  cargarDescripcionPais2(codigo){
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
            }
            
            function cargarDescripcionPais3(codigo){
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
            }
            
            function cargarDescripcionIdioma(codigo, campo){
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
                document.getElementById(campo).value = desc;
            }
            
            function cargarDescripcionNivelIdioma(codigo, campo){
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
                document.getElementById(campo).value = desc;
            }
            
            function cargarDescripcionNivelForm(codigo, campo){
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
                document.getElementById(campo).value = desc;
            }
            
            function cargarDescripcionesCombos(){
                var desc = "";
                var codAct = "";
                var codigo = "";

                //Pais 1
                codigo = '<%=ofertaModif != null && ofertaModif.getPaiCod1() != null ? ofertaModif.getPaiCod1() : ""%>';
                cargarDescripcionPais1(codigo);               

                //Pais 2
                codigo = '<%=ofertaModif != null && ofertaModif.getPaiCod2() != null ? ofertaModif.getPaiCod2() : ""%>';
                cargarDescripcionPais2(codigo);

                //Pais 3
                codigo = '<%=ofertaModif != null && ofertaModif.getPaiCod3() != null ? ofertaModif.getPaiCod3() : ""%>';
                cargarDescripcionPais3(codigo);

                //Idioma 1
                codigo = '<%=ofertaModif != null && ofertaModif.getCodIdioma1() != null ? ofertaModif.getCodIdioma1() : ""%>';
                cargarDescripcionIdioma(codigo, 'descIdiomaPuesto1');                               

                //Idioma 2
                codigo = '<%=ofertaModif != null && ofertaModif.getCodIdioma2() != null ? ofertaModif.getCodIdioma2() : ""%>';
                cargarDescripcionIdioma(codigo, 'descIdiomaPuesto2');    
                
                //Idioma 3
                codigo = '<%=ofertaModif != null && ofertaModif.getCodIdioma3() != null ? ofertaModif.getCodIdioma3() : ""%>';
                cargarDescripcionIdioma(codigo, 'descIdiomaPuesto3');    

                //Nivel Idioma 1
                codigo = '<%=ofertaModif != null && ofertaModif.getCodNivIdi1() != null ? ofertaModif.getCodNivIdi1() : ""%>';
                cargarDescripcionNivelIdioma(codigo,'descNivelIdiomaPuesto1');                

                //Nivel Idioma 2
                codigo = '<%=ofertaModif != null && ofertaModif.getCodNivIdi2() != null ? ofertaModif.getCodNivIdi2() : ""%>';
                cargarDescripcionNivelIdioma(codigo,'descNivelIdiomaPuesto2');            

                //Nivel Idioma 3
                codigo = '<%=ofertaModif != null && ofertaModif.getCodNivIdi3() != null ? ofertaModif.getCodNivIdi3() : ""%>';
                cargarDescripcionNivelIdioma(codigo,'descNivelIdiomaPuesto3');            

                //Nivel Formativo
                codigo = '<%=ofertaModif != null && ofertaModif.getCodNivForm() != null ? ofertaModif.getCodNivForm() : ""%>';
                cargarDescripcionNivelForm(codigo, 'descNivelFormativo');
                
                //Oficina Gestion
                codigo = '<%=ofertaModif != null && ofertaModif.getCodOfiGest() != null ? ofertaModif.getCodOfiGest() : ofertaOrigen != null && ofertaOrigen.getCodOfiGest() != null ? ofertaOrigen.getCodOfiGest() : ""%>';
                desc = '';
                encontrado = false;
                i = 0; 
                if(codigo != null && codigo != '')
                {
                    while(i<codOfiGestion.length && !encontrado)
                    {
                        codAct = codOfiGestion[i];
                        if(codAct == codigo)
                        {
                            encontrado = true;
                            desc = descOfiGestion[i];
                        }
                        else
                            {
                                i++;
                            }
                    }
                }
                document.getElementById('descOfiGestionOferta').value = desc;
                
                //Causa Renuncia
                codigo = '<%=ofertaModif != null && ofertaModif.getCodCausaRenuncia() != null ? ofertaModif.getCodCausaRenuncia() : ""%>';
                desc = '';
                encontrado = false;
                i = 0; 
                if(codigo != null && codigo != '')
                {
                    while(i<codCausaRenuncia.length && !encontrado)
                    {
                        codAct = codCausaRenuncia[i];
                        if(codAct == codigo)
                        {
                            encontrado = true;
                            desc = descCausaRenuncia[i];
                        }
                        else
                            {
                                i++;
                            }
                    }
                }
                document.getElementById('descCausaRenuncia').value = desc;
                
                //Causa Renuncia Presenta Oferta
                codigo = '<%=ofertaModif != null && ofertaModif.getCodCausaRenunciaPresOferta() != null ? ofertaModif.getCodCausaRenunciaPresOferta() : ""%>';
                desc = '';
                encontrado = false;
                i = 0; 
                if(codigo != null && codigo != '')
                {
                    while(i<codCausaRenunciaPresOferta.length && !encontrado)
                    {
                        codAct = codCausaRenunciaPresOferta[i];
                        if(codAct == codigo)
                        {
                            encontrado = true;
                            desc = descCausaRenunciaPresOferta[i];
                        }
                        else
                            {
                                i++;
                            }
                    }
                }
                document.getElementById('descCausaRenunciaPresOferta').value = desc;
                
                //Nif
                codigo = '<%=ofertaModif != null && ofertaModif.getCodTipoNif() != null ? ofertaModif.getCodTipoNif() : ""%>';
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
                
                //Grupo cotizaci�n
                codigo = '<%=ofertaModif != null && ofertaModif.getCodGrCot() != null ? ofertaModif.getCodGrCot() : ""%>';
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
                
                //Causa baja
                codigo = '<%=ofertaModif != null && ofertaModif.getCodCausaBaja() != null ? ofertaModif.getCodCausaBaja() : ""%>';
                desc = '';
                encontrado = false;
                i = 0; 
                if(codigo != null && codigo != '')
                {
                    while(i<codCausaBaja.length && !encontrado)
                    {
                        codAct = codCausaBaja[i];
                        if(codAct == codigo)
                        {
                            encontrado = true;
                            desc = descCausaBaja[i];
                        }
                        else
                            {
                                i++;
                            }
                    }
                }
                document.getElementById('descCausaBaja').value = desc;
            }
            
            function guardar(){
                if(validarDatos()){
                document.getElementById('msgBuscandoDatosTercero').style.display="none";
                document.getElementById('msgGuardandoDatos').style.display="inline";
                    barraProgresoCme('on', 'barraProgresoNuevaOferta');
                    var CONTEXT_PATH = '<%=request.getContextPath()%>'
                    var url = CONTEXT_PATH + "/PeticionModuloIntegracion.do";
                    var parametros = "";
                    var control = new Date();
                    var parametros = 'tarea=preparar&modulo=MELANBIDE60&operacion=guardarOferta&tipo=0&numero=<%=numExpediente%>'
                        +'&idOferta=<%=ofertaModif != null && ofertaModif.getIdOferta() != null ? ofertaModif.getIdOferta() : ""%>'
                        +'&idPuesto=<%=ofertaModif != null && ofertaModif.getCodPuesto() != null ? ofertaModif.getCodPuesto() : ""%>'    
                        +'&checkPrecandidatosOferta='+document.getElementById('checkPrecandidatosOferta').value
                        +'&checkDifusionOferta='+document.getElementById('checkDifusionOferta').value
                        +'&radioCont='+(document.getElementById('radioContS').checked ? '<%=ConstantesMeLanbide60.CIERTO%>' : document.getElementById('radioContN').checked ? '<%=ConstantesMeLanbide60.FALSO%>' : '')
                        +'&radioPresOferta='+(document.getElementById('radioPresOfertaS').checked ? '<%=ConstantesMeLanbide60.CIERTO%>' : document.getElementById('radioPresOfertaN').checked ? '<%=ConstantesMeLanbide60.FALSO%>' : '')
                        +'&radioSustituye='+(document.getElementById('radioSustituyeS').checked ? '<%=ConstantesMeLanbide60.CIERTO%>' : document.getElementById('radioSustituyeN').checked ? '<%=ConstantesMeLanbide60.FALSO%>' : '')
                        +'&alta='+alta
                        +'&copiar='+copiar
                        +'&control='+control.getTime();
                   
                            
                    document.forms[0].action = url+'?'+parametros;
                    document.forms[0].enctype = 'multipart/form-data';
                    document.forms[0].method = 'POST';
                    document.forms[0].target = 'nuevaOfertaFrame';
                    document.forms[0].submit();
                }else{
                    jsp_alerta("A", escape(mensajeValidacion));
                }
            }
            
            function validarDatos(){
                mensajeValidacion = '';
                var correcto = true;
                try{
                    if(!validarDatosPuesto()){
                        correcto = false;
                    }
                    if(!validarDatosOferta()){
                        correcto = false;
                    }
                    if(!validarDatosContratado()){
                        correcto = false;
                    }
                    if(!validarDatosBaja()){
                        correcto = false;
                    }
                }catch(err){
                    correcto = false;
                    if(mensajeValidacion == ''){
                        mensajeValidacion = '<%=meLanbide60I18n.getMensaje(idiomaUsuario, "msg.errorValidarDatos")%>';
                    }
                }
                return correcto;
            }
            
            function validarDatosPuesto(){
                var correcto = true;
                //Puesto
                <%
                if(puestoConsulta != null)
                {
                %>
                    var codPuesto = document.getElementById('codPuesto').value;
                    if(codPuesto == null || codPuesto == ''){
                        document.getElementById('codPuesto').style.border = '1px solid red';
                        mensajeValidacion = '<%=meLanbide60I18n.getMensaje(idiomaUsuario, "msg.puesto.datosPuesto.codPuestoVacio")%>';
                        return false;
                    }
                <%
                }
                %>
                var descPuesto = document.getElementById('descPuesto').value;
                if(descPuesto == null || descPuesto == ''){
                    document.getElementById('descPuesto').style.border = '1px solid red';
                    mensajeValidacion = '<%=meLanbide60I18n.getMensaje(idiomaUsuario, "msg.puesto.datosPuesto.descPuestoVacio")%>';
                    return false;
                }else{
                    if(!comprobarCaracteresEspecialesCme(descPuesto)){
                        document.getElementById('descPuesto').style.border = '1px solid red';
                        mensajeValidacion = '<%=meLanbide60I18n.getMensaje(idiomaUsuario, "msg.puesto.datosPuesto.descPuestoCaracteresNoValidos")%>';
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
                        mensajeValidacion = '<%=meLanbide60I18n.getMensaje(idiomaUsuario, "msg.puesto.datosPuesto.pais1NoExiste")%>';
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
                        mensajeValidacion = '<%=meLanbide60I18n.getMensaje(idiomaUsuario, "msg.puesto.datosPuesto.pais2NoExiste")%>';
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
                        mensajeValidacion = '<%=meLanbide60I18n.getMensaje(idiomaUsuario, "msg.puesto.datosPuesto.pais3NoExiste")%>';
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
                            mensajeValidacion = '<%=meLanbide60I18n.getMensaje(idiomaUsuario, "msg.puesto.datosPuesto.ciudadDestinoCaracteresNoValidos")%>';
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
                            mensajeValidacion = '<%=meLanbide60I18n.getMensaje(idiomaUsuario, "msg.puesto.datosPuesto.dptoCaracteresNoValidos")%>';
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
                            mensajeValidacion = '<%=meLanbide60I18n.getMensaje(idiomaUsuario, "msg.puesto.datosPuesto.funcionesCaracteresNoValidos")%>';
                        }
                        document.getElementById('funcionesPuesto').style.border = '1px solid red';
                        correcto = false;
                    }else{
                        if(funciones.length > 1000){
                            if(mensajeValidacion == ''){
                                mensajeValidacion = '<%=meLanbide60I18n.getMensaje(idiomaUsuario, "msg.puesto.datosPuesto.funcionesDemasiadoLargo")%>';
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
                        mensajeValidacion = '<%=meLanbide60I18n.getMensaje(idiomaUsuario, "msg.puesto.datosPuesto.idiomaNoExiste")%>';
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
                        mensajeValidacion = '<%=meLanbide60I18n.getMensaje(idiomaUsuario, "msg.puesto.datosPuesto.idiomaNoExiste")%>';
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
                        mensajeValidacion = '<%=meLanbide60I18n.getMensaje(idiomaUsuario, "msg.puesto.datosPuesto.idiomaNoExiste")%>';
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
                        mensajeValidacion = '<%=meLanbide60I18n.getMensaje(idiomaUsuario, "msg.puesto.datosPuesto.nivelIdiomaNoExiste")%>';
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
                        mensajeValidacion = '<%=meLanbide60I18n.getMensaje(idiomaUsuario, "msg.puesto.datosPuesto.nivelIdiomaNoExiste")%>';
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
                        mensajeValidacion = '<%=meLanbide60I18n.getMensaje(idiomaUsuario, "msg.puesto.datosPuesto.nivelIdiomaNoExiste")%>';
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
                        mensajeValidacion = '<%=meLanbide60I18n.getMensaje(idiomaUsuario, "msg.puesto.datosPuesto.nivelForNoExiste")%>';
                    }
                }else{
                    document.getElementById('codNivelFormativo').removeAttribute("style");
                    document.getElementById('descNivelFormativo').removeAttribute("style");
                }
                return correcto;
            }
            
            function validarDatosOferta(){
                var correcto = true;
                //Numero de oferta
                var numOferta = document.getElementById('txtNumOferta').value;
                if(numOferta != null && numOferta != ''){
                    if(!comprobarCaracteresEspecialesCme(numOferta)){
                        if(mensajeValidacion == ''){
                            mensajeValidacion = '<%=meLanbide60I18n.getMensaje(idiomaUsuario, "msg.oferta.datosOferta.numeroOfertaCaracteresNoValidos")%>';
                        }
                        document.getElementById('txtNumOferta').style.border = '1px solid red';
                        correcto = false;
                    }else{
                        document.getElementById('txtNumOferta').removeAttribute("style");
                    }
                }
                
                //Oficina de gesti�n
                var ofi = document.getElementById('codOfiGestionOferta').value;
                if(!existeCodigoCombo(ofi, codOfiGestion)){
                    correcto = false;
                    document.getElementById('codOfiGestionOferta').style.border = '1px solid red';
                    document.getElementById('descOfiGestionOferta').style.border = '1px solid red';
                    if(mensajeValidacion == ''){
                        mensajeValidacion = '<%=meLanbide60I18n.getMensaje(idiomaUsuario, "msg.oferta.datosOferta.ofiGestNoExiste")%>';
                    }
                }else{
                    document.getElementById('codOfiGestionOferta').removeAttribute("style");
                    document.getElementById('descOfiGestionOferta').removeAttribute("style");
                }
                
                //Nombre Precandidatos
                var nomPrec = document.getElementById('txtNomPrecandidatos').value;
                if(nomPrec != null && nomPrec != ''){
                    if(nomPrec.length <= 200){
                        if(!comprobarCaracteresEspecialesCme(nomPrec)){
                            if(mensajeValidacion == ''){
                                mensajeValidacion = '<%=meLanbide60I18n.getMensaje(idiomaUsuario, "msg.oferta.datosOferta.precandidatosCaracteresNoValidos")%>';
                            }
                            document.getElementById('txtNomPrecandidatos').style.border = '1px solid red';
                            correcto = false;
                        }else{
                            document.getElementById('txtNomPrecandidatos').removeAttribute("style");
                        }
                    }else{
                        if(mensajeValidacion == ''){
                            mensajeValidacion = '<%=meLanbide60I18n.getMensaje(idiomaUsuario, "msg.oferta.datosOferta.precandidatosDemasiadoLargo")%>';
                        }
                        document.getElementById('txtNomPrecandidatos').style.border = '1px solid red';
                        correcto = false;
                    }
                }
                
                //Fecha difusi�n
                if(!validarFechaCme(document.forms[0], document.getElementById('fechaDifusionOferta'))){
                    document.getElementById('fechaDifusionOferta').style.border = '1px solid red';
                    if(mensajeValidacion == '')
                        mensajeValidacion = '<%=meLanbide60I18n.getMensaje(idiomaUsuario, "msg.oferta.datosOferta.fechaDifusionIncorrecto")%>';
                    correcto = false;
                }else{
                    document.getElementById('fechaDifusionOferta').removeAttribute('style');
                }
                
                //Fecha envio candidatos
                if(!validarFechaCme(document.forms[0], document.getElementById('fechaEnvioCandidatosOferta'))){
                    document.getElementById('fechaEnvioCandidatosOferta').style.border = '1px solid red';
                    if(mensajeValidacion == '')
                        mensajeValidacion = '<%=meLanbide60I18n.getMensaje(idiomaUsuario, "msg.oferta.datosOferta.fechaEnvioCandidatosIncorrecto")%>';
                    correcto = false;
                }else{
                    document.getElementById('fechaEnvioCandidatosOferta').removeAttribute('style');
                }
                
                //Num. total candidatos
                try{
                    if(!validarNumericoCme(document.getElementById('txtNumCandidatosEnv'), 3)){
                        correcto = false;
                        document.getElementById('txtNumCandidatosEnv').style.border = '1px solid red';
                        if(mensajeValidacion == ''){
                            mensajeValidacion = '<%=meLanbide60I18n.getMensaje(idiomaUsuario, "msg.oferta.datosOferta.numCandidatosIncorrecto")%>';
                        }
                    }else{
                        document.getElementById('txtNumCandidatosEnv').removeAttribute('style');
                    }
                }catch(err){
                    correcto = false;
                    document.getElementById('txtNumCandidatosEnv').style.border = '1px solid red';
                }
                
                //Motivo
                if(document.getElementById('radioContN').checked){
                    var motivo = document.getElementById('motivoNoCont').value;
                    if(motivo != null && motivo != ''){
                        if(!comprobarCaracteresEspecialesCme(motivo)){
                            if(mensajeValidacion == ''){
                                mensajeValidacion = '<%=meLanbide60I18n.getMensaje(idiomaUsuario, "msg.oferta.datosOferta.motivoContCaracteresNoValidos")%>';
                            }
                            document.getElementById('motivoNoCont').style.border = '1px solid red';
                            correcto = false;
                        }else{
                            if(motivo.length > 1000){
                                if(mensajeValidacion == ''){
                                    mensajeValidacion = '<%=meLanbide60I18n.getMensaje(idiomaUsuario, "msg.oferta.datosOferta.motivoContDemasiadoLargo")%>';
                                }
                                document.getElementById('motivoNoCont').style.border = '1px solid red';
                                correcto = false;
                            }else{
                                document.getElementById('motivoNoCont').removeAttribute("style");
                            }
                        }
                    }
                }
                
                return correcto;
            }
            
            function validarDatosContratado(){
                var correcto = true;
                //Tipo de documento
                var tipoDoc = document.getElementById('codNifContratado').value;
                if(!existeCodigoCombo(tipoDoc, codNif)){
                    correcto = false;
                    document.getElementById('codNifContratado').style.border = '1px solid red';
                    document.getElementById('descNifContratado').style.border = '1px solid red';
                    if(mensajeValidacion == ''){
                        mensajeValidacion = '<%=meLanbide60I18n.getMensaje(idiomaUsuario, "msg.oferta.datosContratado.nifNoExiste")%>';
                    }
                }else{
                    document.getElementById('codNifContratado').removeAttribute("style");
                    document.getElementById('descNifContratado').removeAttribute("style");
                }
                
                //N�mero de documento
                var nif = document.getElementById('txtNifOferta').value;
                if(nif != null && nif != ''){
                    if(!comprobarCaracteresEspecialesCme(nif)){
                        if(mensajeValidacion == ''){
                            mensajeValidacion = '<%=meLanbide60I18n.getMensaje(idiomaUsuario, "msg.oferta.datosContratado.nifCaracteresNoValidos")%>';
                        }
                        document.getElementById('txtNifOferta').style.border = '1px solid red';
                        correcto = false;
                    }else{
                        if(!tratarDatosDocumento(tipoDoc, nif)){
                            if(mensajeValidacion == ''){
                                mensajeValidacion = '<%=meLanbide60I18n.getMensaje(idiomaUsuario, "msg.oferta.datosContratado.documentoIncorrecto")%>';
                            }
                            document.getElementById('txtNifOferta').style.border = '1px solid red';
                            correcto = false;   
                        }else{
                            document.getElementById('txtNifOferta').removeAttribute("style");
                        }
                    }
                }
                
                //Nombre
                var nombre = document.getElementById('txtNombre').value;
                if(nombre != null && nombre != ''){
                    if(!comprobarCaracteresEspecialesCme(nombre)){
                        if(mensajeValidacion == ''){
                            mensajeValidacion = '<%=meLanbide60I18n.getMensaje(idiomaUsuario, "msg.oferta.datosContratado.nombreCaracteresNoValidos")%>';
                        }
                        document.getElementById('txtNombre').style.border = '1px solid red';
                        correcto = false;
                    }else{
                        document.getElementById('txtNombre').removeAttribute("style");
                    }
                }
                
                //Ape 1
                var ape1 = document.getElementById('txtApe1').value;
                if(ape1 != null && ape1 != ''){
                    if(!comprobarCaracteresEspecialesCme(ape1)){
                        if(mensajeValidacion == ''){
                            mensajeValidacion = '<%=meLanbide60I18n.getMensaje(idiomaUsuario, "msg.oferta.datosContratado.ape1CaracteresNoValidos")%>';
                        }
                        document.getElementById('txtApe1').style.border = '1px solid red';
                        correcto = false;
                    }else{
                        document.getElementById('txtApe1').removeAttribute("style");
                    }
                }
                
                //Ape 2
                var ape2 = document.getElementById('txtApe2').value;
                if(ape2 != null && ape2 != ''){
                    if(!comprobarCaracteresEspecialesCme(ape2)){
                        if(mensajeValidacion == ''){
                            mensajeValidacion = '<%=meLanbide60I18n.getMensaje(idiomaUsuario, "msg.oferta.datosContratado.ape2CaracteresNoValidos")%>';
                        }
                        document.getElementById('txtApe2').style.border = '1px solid red';
                        correcto = false;
                    }else{
                        document.getElementById('txtApe2').removeAttribute("style");
                    }
                }
                
                //email
                var email = document.getElementById('txtEmail').value;
                if(email != null && email != ''){
                    if(!comprobarCaracteresEspecialesCme(email)){
                        if(mensajeValidacion == ''){
                            mensajeValidacion = '<%=meLanbide60I18n.getMensaje(idiomaUsuario, "msg.oferta.datosContratado.emailCaracteresNoValidos")%>';
                        }
                        document.getElementById('txtEmail').style.border = '1px solid red';
                        correcto = false;
                    }else{
                        document.getElementById('txtEmail').removeAttribute("style");
                    }
                }
                
                //Tfno
                try{
                    if(!validarNumericoCme(document.getElementById('txtTfno'), 15)){
                        correcto = false;
                        document.getElementById('txtTfno').style.border = '1px solid red';
                        if(mensajeValidacion == ''){
                            mensajeValidacion = '<%=meLanbide60I18n.getMensaje(idiomaUsuario, "msg.oferta.datosContratado.tfnoIncorrecto")%>';
                        }
                    }else{
                        document.getElementById('txtTfno').removeAttribute('style');
                    }
                }catch(err){
                    correcto = false;
                    document.getElementById('txtTfno').style.border = '1px solid red';
                }
                
                //Fecha nac contratado
                if(!validarFechaCme(document.forms[0], document.getElementById('fechaNacContratado'))){
                    document.getElementById('fechaNacContratado').style.border = '1px solid red';
                    if(mensajeValidacion == '')
                        mensajeValidacion = '<%=meLanbide60I18n.getMensaje(idiomaUsuario, "msg.oferta.datosContratado.fechaNacIncorrecto")%>';
                    correcto = false;
                }else{
                    document.getElementById('fechaNacContratado').removeAttribute('style');
                }
                
                //A�o titulaci�n
                try{
                    if(!validarNumericoCme(document.getElementById('anoTitulacion'), 15)){
                        correcto = false;
                        document.getElementById('anoTitulacion').style.border = '1px solid red';
                        if(mensajeValidacion == ''){
                            mensajeValidacion = '<%=meLanbide60I18n.getMensaje(idiomaUsuario, "msg.oferta.datosContratado.anoTitulacionIncorrecto")%>';
                        }
                    }else{
                        //El a�o de titulaci�n no puede ser posterior al a�o en curso
                        var today = new Date();
                        var ano_ini = document.getElementById('anoTitulacion').value;
                        var ano_act = today.getFullYear();
                        if(ano_act < ano_ini){
                            correcto = false;
                            document.getElementById('anoTitulacion').style.border = '1px solid red';
                            if(mensajeValidacion == ''){
                                mensajeValidacion = '<%=meLanbide60I18n.getMensaje(idiomaUsuario, "msg.oferta.datosContratado.anoTitulacionPosteriorAnoAct")%>';
                            }
                        }else{
                            document.getElementById('anoTitulacion').removeAttribute('style');
                        }
                    }
                }catch(err){
                    correcto = false;
                    document.getElementById('anoTitulacion').style.border = '1px solid red';
                }
                
                var hayFechaIni = false;
                var hayFechaFin = false;
                
                //Fecha inicio
                if(!validarFechaCme(document.forms[0], document.getElementById('fechaIniContratado'))){
                    document.getElementById('fechaIniContratado').style.border = '1px solid red';
                    if(mensajeValidacion == '')
                        mensajeValidacion = '<%=meLanbide60I18n.getMensaje(idiomaUsuario, "msg.oferta.datosContratado.fechaIniIncorrecto")%>';
                    correcto = false;
                }else{
                    document.getElementById('fechaIniContratado').removeAttribute('style');
                    hayFechaIni = true;
                }
                
                //Fecha fin
                if(!validarFechaCme(document.forms[0], document.getElementById('fechaFinContratado'))){
                    document.getElementById('fechaFinContratado').style.border = '1px solid red';
                    if(mensajeValidacion == '')
                        mensajeValidacion = '<%=meLanbide60I18n.getMensaje(idiomaUsuario, "msg.oferta.datosContratado.fechaFinIncorrecto")%>';
                    correcto = false;
                }else{
                    document.getElementById('fechaFinContratado').removeAttribute('style');
                    hayFechaFin = true;
                }
                
                //Duracion (meses)
                if(document.getElementById('txtMesesContratado').value != ''){
                    try{
                        if(!validarNumericoCme(document.getElementById('txtMesesContratado'), 6)){
                            correcto = false;
                            document.getElementById('txtMesesContratado').style.border = '1px solid red';
                            if(mensajeValidacion == '')
                                mensajeValidacion = '<%=meLanbide60I18n.getMensaje(idiomaUsuario, "msg.puesto.datosContrato.duracionMesesIncorrecto")%>';
                        }else{
                            document.getElementById('txtMesesContratado').removeAttribute('style');
                        }
                    }catch(err){
                        correcto = false;
                        document.getElementById('txtMesesContratado').style.border = '1px solid red';
                        if(mensajeValidacion == '')
                            mensajeValidacion = '<%=meLanbide60I18n.getMensaje(idiomaUsuario, "msg.puesto.datosContrato.duracionMesesIncorrecto")%>';
                    }
                }
                
                if(hayFechaIni && hayFechaFin){
                    var txtIni = document.getElementById('fechaIniContratado').value;
                    var txtFin = document.getElementById('fechaFinContratado').value;
                    var array_fecha_ini = txtIni.split("/");
                    var array_fecha_fin = txtFin.split("/");
                    var dia_ini = array_fecha_ini[0];
                    var mes_ini = array_fecha_ini[1];
                    var ano_ini = array_fecha_ini[2];
                    var dia_fin = array_fecha_fin[0];
                    var mes_fin = array_fecha_fin[1];
                    var ano_fin = array_fecha_fin[2];

                    var ini = new Date(ano_ini, mes_ini-1, dia_ini, 0, 0, 0, 0);
                    var fin = new Date(ano_fin, mes_fin-1, dia_fin, 0, 0, 0, 0);
                    var n1 = ini.getTime();
                    var n2 = fin.getTime();
                    var result = n2 - n1;
                    if(result < 0){
                        mensajeValidacion = '<%=meLanbide60I18n.getMensaje(idiomaUsuario, "msg.oferta.datosContratado.fechaFinAnteriorFechaIni")%>';
                        correcto = false;
                        document.getElementById('fechaIniContratado').style.border = '1px solid red';
                        document.getElementById('fechaFinContratado').style.border = '1px solid red';
                    }else{
                        
                        document.getElementById('fechaIniContratado').removeAttribute('style');
                        document.getElementById('fechaFinContratado').removeAttribute('style');
                    }
                }
                
                //Grupo de cotizaci�n
                var grCot = document.getElementById('codGrupoCotContratado').value;
                if(!existeCodigoCombo(grCot, codGrupoCot)){
                    correcto = false;
                    document.getElementById('codGrupoCotContratado').style.border = '1px solid red';
                    document.getElementById('descGrupoCotContratado').style.border = '1px solid red';
                    if(mensajeValidacion == ''){
                        mensajeValidacion = '<%=meLanbide60I18n.getMensaje(idiomaUsuario, "msg.oferta.datosContratado.codigoGrCotNoExiste")%>';
                    }
                }else{
                    document.getElementById('codGrupoCotContratado').removeAttribute("style");
                    document.getElementById('descGrupoCotContratado').removeAttribute("style");
                }
                
                //Salario bruto
                if(document.getElementById('txtSalarioContratado').value != ''){
                    try{
                        if(!validarNumericoDecimalCme(document.getElementById('txtSalarioContratado'), 10, 2)){
                            correcto = false;
                            document.getElementById('txtSalarioContratado').style.border = '1px solid red';
                            if(mensajeValidacion == '')
                                mensajeValidacion = '<%=meLanbide60I18n.getMensaje(idiomaUsuario, "msg.oferta.datosContratado.salarioBrutoIncorrecto")%>';
                        }else{
                            document.getElementById('txtSalarioContratado').removeAttribute('style');
                        }
                    }catch(err){
                        correcto = false;
                        document.getElementById('txtSalarioContratado').style.border = '1px solid red';
                        if(mensajeValidacion == '')
                            mensajeValidacion = '<%=meLanbide60I18n.getMensaje(idiomaUsuario, "msg.oferta.datosContratado.salarioBrutoIncorrecto")%>';
                    }
                }
                
                //Dietas convenio
                if(document.getElementById('txtDietasConv').value != ''){
                    try{
                        if(!validarNumericoDecimalCme(document.getElementById('txtDietasConv'), 10, 2)){
                            correcto = false;
                            document.getElementById('txtDietasConv').style.border = '1px solid red';
                            if(mensajeValidacion == '')
                                mensajeValidacion = '<%=meLanbide60I18n.getMensaje(idiomaUsuario, "msg.oferta.datosContratado.dietasConvenioIncorrecto")%>';
                        }else{
                            document.getElementById('txtDietasConv').removeAttribute('style');
                        }
                    }catch(err){
                        correcto = false;
                        document.getElementById('txtDietasConv').style.border = '1px solid red';
                        if(mensajeValidacion == '')
                            mensajeValidacion = '<%=meLanbide60I18n.getMensaje(idiomaUsuario, "msg.oferta.datosContratado.dietasConvenioIncorrecto")%>';
                    }
                }
                
                //Dietas convocatoria
                if(document.getElementById('txtDietasConvoc').value != ''){
                    try{
                        if(!validarNumericoDecimalCme(document.getElementById('txtDietasConvoc'), 10, 2)){
                            correcto = false;
                            document.getElementById('txtDietasConvoc').style.border = '1px solid red';
                            if(mensajeValidacion == '')
                                mensajeValidacion = '<%=meLanbide60I18n.getMensaje(idiomaUsuario, "msg.oferta.datosContratado.dietasConvenioIncorrecto")%>';
                        }else{
                            document.getElementById('txtDietasConvoc').removeAttribute('style');
                        }
                    }catch(err){
                        correcto = false;
                        document.getElementById('txtDietasConvoc').style.border = '1px solid red';
                        if(mensajeValidacion == '')
                            mensajeValidacion = '<%=meLanbide60I18n.getMensaje(idiomaUsuario, "msg.oferta.datosContratado.dietasConvenioIncorrecto")%>';
                    }
                }
                
                //Sexo
                var sexo = Trim(document.getElementById('txtSexo').value);
                sexo = sexo.toUpperCase();
                if(sexo != null && sexo != ''){
                    if(sexo != '<%=meLanbide60I18n.getMensaje(idiomaUsuario, "label.sexoH")%>' && sexo != '<%=meLanbide60I18n.getMensaje(idiomaUsuario, "label.sexoM")%>'){
                        correcto = false;
                        document.getElementById('txtSexo').style.border = '1px solid red';
                        if(mensajeValidacion == '')
                            mensajeValidacion = '<%=meLanbide60I18n.getMensaje(idiomaUsuario, "msg.oferta.datosContratado.sexoIncorrecto")%>';
                    }
                    else{
                        document.getElementById('txtSexo').removeAttribute('style');
                    }
                }
                
                return correcto;
            }
            
            function validarDatosBaja(){
                var correcto  = true;
                
                //Fecha inicio
                if(!validarFechaCme(document.forms[0], document.getElementById('fechaBaja'))){
                    document.getElementById('fechaBaja').style.border = '1px solid red';
                    if(mensajeValidacion == '')
                        mensajeValidacion = '<%=meLanbide60I18n.getMensaje(idiomaUsuario, "msg.oferta.datosBaja.fechaBajaIncorrecto")%>';
                    correcto = false;
                }else{
                    document.getElementById('fechaBaja').removeAttribute('style');
                    hayFechaIni = true;
                }
                
                //Causa baja
                var grCot = document.getElementById('codCausaBaja').value;
                if(!existeCodigoCombo(grCot, codCausaBaja)){
                    correcto = false;
                    document.getElementById('codCausaBaja').style.border = '1px solid red';
                    document.getElementById('descCausaBaja').style.border = '1px solid red';
                    if(mensajeValidacion == ''){
                        mensajeValidacion = '<%=meLanbide60I18n.getMensaje(idiomaUsuario, "msg.oferta.datosBaja.codigoCausaBajaNoExiste")%>';
                    }
                }else{
                    document.getElementById('codCausaBaja').removeAttribute("style");
                    document.getElementById('descCausaBaja').removeAttribute("style");
                }
                
                //Observaciones
                var obs = document.getElementById('observacionesBaja').value;
                if(obs != null && obs != ''){
                    if(!comprobarCaracteresEspecialesCme(obs)){
                        if(mensajeValidacion == ''){
                            mensajeValidacion = '<%=meLanbide60I18n.getMensaje(idiomaUsuario, "msg.puesto.datosBaja.observacionesCaracteresNoValidos")%>';
                        }
                        document.getElementById('observacionesBaja').style.border = '1px solid red';
                        correcto = false;
                    }else{
                        if(obs.length > 1000){
                            if(mensajeValidacion == ''){
                                mensajeValidacion = '<%=meLanbide60I18n.getMensaje(idiomaUsuario, "msg.puesto.datosBaja.observacionesDemasiadoLargo")%>';
                            }
                            document.getElementById('observacionesBaja').style.border = '1px solid red';
                            correcto = false;
                        }else{
                            document.getElementById('observacionesBaja').removeAttribute("style");
                        }
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
            
            function comprobarSexo(campo){
                var sexo = Trim(campo.value);
                sexo = sexo.toUpperCase();
                if(sexo != null && sexo != ''){
                    if(sexo != '<%=meLanbide60I18n.getMensaje(idiomaUsuario, "label.sexoH")%>' && sexo != '<%=meLanbide60I18n.getMensaje(idiomaUsuario, "label.sexoM")%>'){
                        campo.value = '';
                    }
                    else{
                        campo.value = sexo;
                    }
                }
            }
            
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
    
            function getListaOfertasCme(){
                var ajax = getXMLHttpRequest();
                var nodos = null;
                var CONTEXT_PATH = '<%=request.getContextPath()%>'
                var url = CONTEXT_PATH + '/PeticionModuloIntegracion.do';
                var parametros = '';
                var control = new Date();
                parametros = 'tarea=preparar&modulo=MELANBIDE60&operacion=getListaOfertasNoDenegadasPorExpediente&tipo=0&numero=<%=numExpediente%>&control='+control.getTime();
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

                }
                catch(err){
                    jsp_alerta("A",'<%=meLanbide60I18n.getMensaje(idiomaUsuario,"error.errorProcesarRespuesta")%>');
                }//try-catch
                return nodos;
            }
            
            function verDocumento(documento){
                var CONTEXT_PATH = '<%=request.getContextPath()%>'
                var url = CONTEXT_PATH + "/PeticionModuloIntegracion.do";
                var parametros = "";
                var control = new Date();
                parametros = '?tarea=preparar&modulo=MELANBIDE60&operacion=descargarDocumentoOferta&tipo=0&idOferta=<%=ofertaModif != null && ofertaModif.getIdOferta() != null ? ofertaModif.getIdOferta() : ""%>&idPuesto=<%=ofertaModif != null && ofertaModif.getCodPuesto() != null ? ofertaModif.getCodPuesto() : ""%>&numero=<%=numExpediente%>&documento='+documento+'&control='+control.getTime();
                inicio = false;
                window.open(url+parametros, "_blank");
            }
            
            function mostrarModificarDocumentoGestion(){
                document.getElementById('btnVerDocGestionOferta').style.display = 'none';
                document.getElementById('btnModifDocGestionOferta').style.display = 'none';
                document.getElementById('docGestionOferta').style.display = 'inline';
                document.getElementById('btnCancelarModifDocGestionOferta').style.display = 'inline';
            }
            
            function cancelarModificarDocumentoGestion(){
                document.getElementById('btnVerDocGestionOferta').style.display = 'inline';
                document.getElementById('btnModifDocGestionOferta').style.display = 'inline';
                document.getElementById('docGestionOferta').style.display = 'none';
                document.getElementById('btnCancelarModifDocGestionOferta').style.display = 'none';
                document.getElementById('docGestionOferta').value='';
            }
            
            function mostrarModificarDocumentoContratacion(){
                document.getElementById('btnVerDocContratacion').style.display = 'none';
                document.getElementById('btnModifDocContratacion').style.display = 'none';
                document.getElementById('docContratacion').style.display = 'inline';
                document.getElementById('btnCancelarModifDocContratacion').style.display = 'inline';
            }
            function cancelarModificarDocumentoContratacion(){
                document.getElementById('btnVerDocContratacion').style.display = 'inline';
                document.getElementById('btnModifDocContratacion').style.display = 'inline';
                document.getElementById('docContratacion').style.display = 'none';
                document.getElementById('btnCancelarModifDocContratacion').style.display = 'none';
                document.getElementById('docContratacion').value='';
            }
            
            function altaSustituto(){
                
                var resultado = jsp_alerta('','<%=meLanbide60I18n.getMensaje(idiomaUsuario, "msg.oferta.preguntaCopiarOferta")%>');
                if (resultado == 1){
                    copiar = 1;
                }else{
                    copiar = 0;
                }
                alta = 1;
                guardar();
            }
            
            function calcularMesesContratado(){
                try{
                    var  numMeses=0;
                    //Se calcula restando la fecha de alta de la persona contratada y la fecha de baja de la persona sustituida
                   var feIni = document.getElementById('fechaIniContratado').value;
                   var feFin = document.getElementById('fechaFinContratado').value;
                   if(feIni != null && feIni != '' && feFin != null && feFin != ''){
                      //nuevo
                   var ajax = getXMLHttpRequest();
                    var nodos = null;
                    var CONTEXT_PATH = '<%=request.getContextPath()%>'
                    var url = CONTEXT_PATH + '/PeticionModuloIntegracion.do';
                
                    var parametros = '';
                    var control = new Date();
                    parametros = 'tarea=preparar&modulo=MELANBIDE60&operacion=calcularMeses&numero=<%=numExpediente%>&fini='+feIni+'&ffin='+feFin+'&tipo=0&control='+control.getTime();
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
                        jsp_alerta("A",'<%=meLanbide60I18n.getMensaje(idiomaUsuario,"error.errorProcesarRespuesta")%>');
                    }//try-catch
                   
                    
                       
                       
                      // var numMeses = mesesDiferencia(feIni, feFin);
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
            
            
            function copiarDatos(){               
                    document.getElementById('msgBuscandoDatosTercero').style.display="none";
                    document.getElementById('msgGuardandoDatos').style.display="inline";
                    barraProgresoCme('on', 'barraProgresoNuevaOferta');
                  
                  var ajax = getXMLHttpRequest();
                    var nodos = null;
                    var CONTEXT_PATH = '<%=request.getContextPath()%>'
                    var url = CONTEXT_PATH + '/PeticionModuloIntegracion.do';
                
                    var parametros = '';
                    var control = new Date();
                    parametros = 'tarea=preparar&modulo=MELANBIDE60&operacion=copiarDatosPuesto&numero=<%=numExpediente%>'
                        +'&idOferta=<%=ofertaModif != null && ofertaModif.getIdOferta() != null ? ofertaModif.getIdOferta() : ""%>'
                        +'&idPuesto=<%=ofertaModif != null && ofertaModif.getCodPuesto() != null ? ofertaModif.getCodPuesto() : ""%>&tipo=0&control='+control.getTime();
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
                        
                        var  dato="";
                        nodos = xmlDoc.getElementsByTagName("RESPUESTA");
                        var hijos = nodos[0].childNodes;
                        for(j=0;hijos!=null && j<hijos.length;j++){
                            if(hijos[j].nodeName=="DESCPUESTO") {                           
                               dato = hijos[j].childNodes[0].nodeValue;                        
                               document.getElementById('descPuesto').value = dato;
                            }
                            if(hijos[j].nodeName=="CODPAIS1") {                           
                               if(hijos[j].childNodes.length > 0)                                   
                                var pais1 = hijos[j].childNodes[0].nodeValue;                        
                               else var pais1="";                              
                               document.getElementById('codPaisPuesto1').value = pais1;
                            }
                            cargarDescripcionPais1(pais1);
                            if(hijos[j].nodeName=="CODPAIS2") {                           
                               if(hijos[j].childNodes.length > 0)                                   
                                var pais2 = hijos[j].childNodes[0].nodeValue;                        
                               else  var pais2="";
                               document.getElementById('codPaisPuesto2').value = pais2;
                            }
                             cargarDescripcionPais2(pais2);
                            if(hijos[j].nodeName=="CODPAIS3") {                           
                               if(hijos[j].childNodes.length > 0)                                   
                                var pais3 = hijos[j].childNodes[0].nodeValue;                        
                               else var pais3="";                       
                               document.getElementById('codPaisPuesto3').value = pais3;
                            }
                            cargarDescripcionPais3(pais3);
                            if(hijos[j].nodeName=="CODTITU1") {                           
                               if(hijos[j].childNodes.length > 0)                                   
                                var titu1 = hijos[j].childNodes[0].nodeValue;                        
                               else var titu1="";                            
                               document.getElementById('codTitulacionPuesto1').value = titu1;
                            }                            
                            if(hijos[j].nodeName=="CODTITU2") {                           
                               if(hijos[j].childNodes.length > 0)                                   
                                var titu2 = hijos[j].childNodes[0].nodeValue;                        
                               else var titu2="";                       
                               document.getElementById('codTitulacionPuesto2').value = titu2;
                            }
                            if(hijos[j].nodeName=="CODTITU3") {                           
                               if(hijos[j].childNodes.length > 0)                                   
                                var titu3 = hijos[j].childNodes[0].nodeValue;                        
                               else var titu3="";                     
                               document.getElementById('codTitulacionPuesto3').value = titu3;
                            }
                            if(hijos[j].nodeName=="DESCTITU1") {                           
                               if(hijos[j].childNodes.length > 0)                                   
                                var titu1 = hijos[j].childNodes[0].nodeValue;                        
                               else var titu1="";                            
                               document.getElementById('descTitulacionPuesto1').value = titu1;
                            }                            
                            if(hijos[j].nodeName=="DESCTITU2") {                           
                               if(hijos[j].childNodes.length > 0)                                   
                                var titu2 = hijos[j].childNodes[0].nodeValue;                        
                               else var titu2="";                       
                               document.getElementById('descTitulacionPuesto2').value = titu2;
                            }
                            if(hijos[j].nodeName=="DESCTITU3") {                           
                               if(hijos[j].childNodes.length > 0)                                   
                                var titu3 = hijos[j].childNodes[0].nodeValue;                        
                               else var titu3="";                     
                               document.getElementById('descTitulacionPuesto3').value = titu3;
                            }
                            if(hijos[j].nodeName=="FUNCIONES") {                           
                               if(hijos[j].childNodes.length > 0) 
                                dato = hijos[j].childNodes[0].nodeValue;    
                                else dato="";
                               document.getElementById('funcionesPuesto').value = dato;
                            }
                            if(hijos[j].nodeName=="CODIDIOMA1") {                           
                               if(hijos[j].childNodes.length > 0)                                   
                                    var idi1 = hijos[j].childNodes[0].nodeValue;                        
                               else var idi1="";                     
                               document.getElementById('codIdiomaPuesto1').value = idi1;
                            }
                            cargarDescripcionIdioma(idi1, 'descIdiomaPuesto1');
                            if(hijos[j].nodeName=="CODIDIOMA2") {                           
                               if(hijos[j].childNodes.length > 0)                                   
                                    var idi2 = hijos[j].childNodes[0].nodeValue;                        
                               else var idi2="";                     
                               document.getElementById('codIdiomaPuesto2').value = idi2;
                            }
                            cargarDescripcionIdioma(idi2, 'descIdiomaPuesto2');
                            if(hijos[j].nodeName=="CODIDIOMA3") {                           
                               if(hijos[j].childNodes.length > 0)                                   
                                    var idi3 = hijos[j].childNodes[0].nodeValue;                        
                               else var idi3="";                     
                               document.getElementById('codIdiomaPuesto3').value = idi3;
                            }
                            cargarDescripcionIdioma(idi3, 'descIdiomaPuesto3');
                            
                            if(hijos[j].nodeName=="CODNIVIDI1") {                           
                               if(hijos[j].childNodes.length > 0)                                   
                                    var nidi1 = hijos[j].childNodes[0].nodeValue;                        
                               else var nidi1="";                     
                               document.getElementById('codNivelIdiomaPuesto1').value = nidi1;                                            
                            }
                            cargarDescripcionNivelIdioma(nidi1, 'descNivelIdiomaPuesto1');
                            if(hijos[j].nodeName=="CODNIVIDI2") {                           
                               if(hijos[j].childNodes.length > 0)                                   
                                    var nidi2 = hijos[j].childNodes[0].nodeValue;                        
                               else var nidi2="";                  
                               document.getElementById('codNivelIdiomaPuesto2').value = nidi2;
                               cargarDescripcionNivelIdioma(nidi2, 'descNivelIdiomaPuesto2');
                            }
                            if(hijos[j].nodeName=="CODNIVIDI3") {                           
                               if(hijos[j].childNodes.length > 0)                                   
                                    var nidi3 = hijos[j].childNodes[0].nodeValue;                        
                               else var nidi3="";                  
                               document.getElementById('codNivelIdiomaPuesto3').value = nidi3;
                               cargarDescripcionNivelIdioma(nidi3, 'descNivelIdiomaPuesto3');
                            }
                            if(hijos[j].nodeName=="CODNIVFORM") {                           
                                if(hijos[j].childNodes.length > 0)                                   
                                    var nform = hijos[j].childNodes[0].nodeValue;                        
                               else var nform="";                         
                               document.getElementById('codNivelFormativo').value = nform;
                               cargarDescripcionNivelForm(nform, 'descNivelFormativo');
                            }    
                            if(hijos[j].nodeName=="CIUDAD") {                           
                               if(hijos[j].childNodes.length > 0) 
                                dato = hijos[j].childNodes[0].nodeValue;    
                               else dato="";                  
                               document.getElementById('ciudadDestino').value = dato;
                            }
                            if(hijos[j].nodeName=="DEPARTAMENTO") {                           
                              if(hijos[j].childNodes.length > 0) 
                                dato = hijos[j].childNodes[0].nodeValue;    
                              else dato="";          
                              document.getElementById('dpto').value = dato;
                            }                        
                        }

                    }
                    catch(err){
                        jsp_alerta("A",'<%=meLanbide60I18n.getMensaje(idiomaUsuario,"error.errorProcesarRespuesta")%>');
                    }//try-catch
                  
                  <%--  var CONTEXT_PATH = '<%=request.getContextPath()%>'
                    var url = CONTEXT_PATH + "/PeticionModuloIntegracion.do";
                    var parametros = "";
                    var control = new Date();
                    var parametros = 'tarea=preparar&modulo=MELANBIDE60&operacion=copiarDatosPuesto&tipo=0&numero=<%=numExpediente%>'
                        +'&idOferta=<%=ofertaModif != null && ofertaModif.getIdOferta() != null ? ofertaModif.getIdOferta() : ""%>'
                        +'&idPuesto=<%=ofertaModif != null && ofertaModif.getCodPuesto() != null ? ofertaModif.getCodPuesto() : ""%>'    
                        +'&checkPrecandidatosOferta='+document.getElementById('checkPrecandidatosOferta').value
                        +'&checkDifusionOferta='+document.getElementById('checkDifusionOferta').value
                        +'&radioCont='+(document.getElementById('radioContS').checked ? '<%=ConstantesMeLanbide60.CIERTO%>' : document.getElementById('radioContN').checked ? '<%=ConstantesMeLanbide60.FALSO%>' : '')
                        +'&radioSustituye='+(document.getElementById('radioSustituyeS').checked ? '<%=ConstantesMeLanbide60.CIERTO%>' : document.getElementById('radioSustituyeN').checked ? '<%=ConstantesMeLanbide60.FALSO%>' : '')                       
                        +'&control='+control.getTime();                  
                            
                    document.forms[0].action = url+'?'+parametros;
                    document.forms[0].enctype = 'multipart/form-data';
                    document.forms[0].method = 'POST';
                    //document.forms[0].target = 'copiarDatosPuestoFrame';
                    //document.forms[0].target = '_self';
                    document.forms[0].target = '_parent';
                    document.forms[0].submit();
                        --%>
                    barraProgresoCme('off', 'barraProgresoNuevaOferta');
                    //cerrarVentana();
                
            }
            
            
            function buscarTercero(){
                if(tipoDoc != '' && numDoc != ''){
                    document.getElementById('msgBuscandoDatosTercero').style.display="inline";
                    document.getElementById('msgGuardandoDatos').style.display="none";
                    barraProgresoCme('on', 'barraProgresoNuevaOferta');

                    var ajax = getXMLHttpRequest();
                    var nodos = null;
                    var CONTEXT_PATH = '<%=request.getContextPath()%>'
                    var url = CONTEXT_PATH + '/PeticionModuloIntegracion.do';

                    var parametros = '';
                    var tipoDoc = document.getElementById('codNifContratado').value;
                    var numDoc = document.getElementById('txtNifOferta').value;
                    var control = new Date();
                    parametros = 'tarea=preparar&modulo=MELANBIDE60&operacion=busquedaTercero&tipo=0&numero=<%=numExpediente%>'
                        +'&tipoDoc='+tipoDoc+'&numDoc='+numDoc+'&control='+control.getTime();
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
                        var j;
                        nodos = xmlDoc.getElementsByTagName("RESPUESTA");
                        var elemento = nodos[0];
                        var hijos = elemento.childNodes;
                        var codigoOperacion = null;
                        var ape1 = null;
                        var ape2 = null;
                        var nombre = null;
                        var email = null;
                        var telf = null;
                        var hayDatosTercero = false;
                        var hayErrores = false;
                        var nodoTerceros = null;
                        var terceros = null;
                        var hijosTercero = null;
                        var nodoErrores = null;
                        var hijosErrores = null;
                        var nodoCampo = null;
                        var errores = null;

                        for(j=0;hijos!=null && j<hijos.length;j++){
                            if(hijos[j].nodeName=="CODIGO_OPERACION"){                            
                                codigoOperacion = hijos[j].childNodes[0].nodeValue;
                            }else if(hijos[j].nodeName=="RESULTADOS"){
                                hayDatosTercero = true;
                                nodoTerceros = hijos[j];
                                terceros = nodoTerceros.childNodes;
                                for(var cont2 = 0; cont2 < terceros.length; cont2++){
                                    hijosTercero = terceros[cont2].childNodes;
                                    for(var cont = 0; cont < hijosTercero.length; cont++){
                                        if(hijosTercero[cont].nodeName=="APE1"){
                                            nodoCampo = hijosTercero[cont];
                                            if(nodoCampo.childNodes.length > 0){
                                                ape1 = nodoCampo.childNodes[0].nodeValue;
                                            }
                                            else{
                                                ape1 = '';
                                            }
                                        }else if(hijosTercero[cont].nodeName=="APE2"){
                                            nodoCampo = hijosTercero[cont];
                                            if(nodoCampo.childNodes.length > 0){
                                                ape2 = nodoCampo.childNodes[0].nodeValue;
                                            }
                                            else{
                                                ape2 = '';
                                            }
                                        }else if(hijosTercero[cont].nodeName=="NOMBRE"){
                                            nodoCampo = hijosTercero[cont];
                                            if(nodoCampo.childNodes.length > 0){
                                                nombre = nodoCampo.childNodes[0].nodeValue;
                                            }
                                            else{
                                                nombre = '';
                                            }
                                        }else if(hijosTercero[cont].nodeName=="EMAIL"){
                                            nodoCampo = hijosTercero[cont];
                                            if(nodoCampo.childNodes.length > 0){
                                                email = nodoCampo.childNodes[0].nodeValue;
                                            }
                                            else{
                                                email = '';
                                            }
                                        }else if(hijosTercero[cont].nodeName=="TELF"){
                                            nodoCampo = hijosTercero[cont];
                                            if(nodoCampo.childNodes.length > 0){
                                                telf = nodoCampo.childNodes[0].nodeValue;
                                            }
                                            else{
                                                telf = '';
                                            }
                                        }
                                    }
                                }
                            }else if(hijos[j].nodeName=="ERRORES"){
                                hayErrores = true;
                                nodoErrores = hijos[j];
                                hijosErrores = nodoErrores.childNodes;
                                errores = "Ha habido errores al buscar terceros:\n\n"
                                for(var cont = 0; cont < hijosErrores.length; cont++){
                                    if(hijosErrores[cont].nodeName=="ERROR"){
                                        nodoCampo = hijosErrores[cont];
                                        if(nodoCampo.childNodes.length > 0){
                                            errores += "- "+nodoCampo.childNodes[0].nodeValue + '\n';
                                        }
                                    }
                                }
                            }
                        }

                        barraProgresoCme('off', 'barraProgresoNuevaOferta');

                        if(codigoOperacion=="0"){
                            if(!hayErrores){
                                if(hayDatosTercero){
                                    //Establecemos los valores que nos ha devuelto el WS
                                    document.getElementById('txtNombre').value = nombre;
                                    document.getElementById('txtApe1').value = ape1;
                                    document.getElementById('txtApe2').value = ape2;
                                    document.getElementById('txtEmail').value = email;
                                    document.getElementById('txtTfno').value = telf;
                                }else{
                                    //Establecemos los valores que nos ha devuelto el WS
                                    document.getElementById('txtNombre').value = '';
                                    document.getElementById('txtApe1').value = '';
                                    document.getElementById('txtApe2').value = '';
                                    document.getElementById('txtEmail').value = '';
                                    document.getElementById('txtTfno').value = '';
                                }

                                //Limpiamos el resto
                                document.getElementById('fechaNacContratado').value = '';
                                document.getElementById('txtSexo').value = '';
                                document.getElementById('codTitulacionContratado').value = '';
                                document.getElementById('descTitulacionContratado').value = '';
                                document.getElementById('anoTitulacion').value = '';
                                document.getElementById('docContratacion').value = '';
                                document.getElementById('fechaIniContratado').value = '';
                                document.getElementById('fechaFinContratado').value = '';
                                document.getElementById('txtMesesContratado').value = '';
                                document.getElementById('codGrupoCotContratado').value = '';
                                document.getElementById('descGrupoCotContratado').value = '';
                                document.getElementById('txtSalarioContratado').value = '';
                                document.getElementById('txtDietasConv').value = '';
                                document.getElementById('txtDietasConvoc').value = '';
                            }else{
                                jsp_alerta("A",errores);
                            }
                        }else{
                            jsp_alerta("A",'<%=meLanbide60I18n.getMensaje(idiomaUsuario,"error.errorBusqTercero")%>');
                        }
                    }
                    catch(err){
                        jsp_alerta("A",'<%=meLanbide60I18n.getMensaje(idiomaUsuario,"error.errorProcesarRespuestaGen")%>');
                    }//try-catch
                }else{
                    //Limpiamos el resto
                    document.getElementById('txtNombre').value = '';
                    document.getElementById('txtApe1').value = '';
                    document.getElementById('txtApe2').value = '';
                    document.getElementById('txtEmail').value = '';
                    document.getElementById('txtTfno').value = '';

                
                    document.getElementById('fechaNacContratado').value = '';
                    document.getElementById('txtSexo').value = '';
                    document.getElementById('codTitulacionContratado').value = '';
                    document.getElementById('descTitulacionContratado').value = '';
                    document.getElementById('anoTitulacion').value = '';
                    document.getElementById('docContratacion').value = '';
                    document.getElementById('fechaIniContratado').value = '';
                    document.getElementById('fechaFinContratado').value = '';
                    document.getElementById('txtMesesContratado').value = '';
                    document.getElementById('codGrupoCotContratado').value = '';
                    document.getElementById('descGrupoCotContratado').value = '';
                    document.getElementById('txtSalarioContratado').value = '';
                    document.getElementById('txtDietasConv').value = '';
                    document.getElementById('txtDietasConvoc').value = '';
                }
            }
            
            function ajustarDecimalesImportes(){
                var f;
                var v;
                
                //Salario bruto
                v = document.getElementById('txtSalarioContratado').value;
                v = reemplazarTextoCme(v, /,/g, '.');
                f = parseFloat(v);
                f = ajustarDecimalesCme(f, 2);
                if(!isNaN(f)){
                    document.getElementById('txtSalarioContratado').value = f;
                    reemplazarPuntosCme(document.getElementById('txtSalarioContratado'));
                    document.getElementById('txtSalarioContratado').removeAttribute("style");
                }
                
                //Dietas seg�n convenio
                v = document.getElementById('txtDietasConv').value;
                v = reemplazarTextoCme(v, /,/g, '.');
                f = parseFloat(v);
                f = ajustarDecimalesCme(f, 2);
                if(!isNaN(f)){
                    document.getElementById('txtDietasConv').value = f;
                    reemplazarPuntosCme(document.getElementById('txtDietasConv'));
                    document.getElementById('txtDietasConv').removeAttribute("style");
                }
                
                //Dietas convocatoria
                v = document.getElementById('txtDietasConvoc').value;
                v = reemplazarTextoCme(v, /,/g, '.');
                f = parseFloat(v);
                f = ajustarDecimalesCme(f, 2);
                if(!isNaN(f)){
                    document.getElementById('txtDietasConvoc').value = f;
                    reemplazarPuntosCme(document.getElementById('txtDietasConvoc'));
                    document.getElementById('txtDietasConvoc').removeAttribute("style");
                }
            }
            
            function buscarTitulacion(id){
                var control = new Date();
                lanzarPopUpModal('<%=request.getContextPath()%>/jsp/extension/melanbide60/buscadorTitulaciones.jsp?codOrganizacionModulo=<%=codOrganizacion%>&control='+control.getTime(),450,980,'yes','no', function(result){
                if (result != undefined && result.length == 3){
                    document.getElementById('cod'+id).value = result[0];
                    var desc = result[1] != undefined && result[1] != '' ? result[1] : '';
                    desc += desc != '' && result[2] != undefined && result[2] != '' ? '-' : '';
                    desc += result[2] != undefined && result[2] != '' ? result[2] : '';
                    document.getElementById('desc'+id).value = desc.toUpperCase();
                }
				});
            }
            
            function load(){
             if(document.getElementById('radioPresOfertaS').checked)
             {
                document.getElementById('divTxtCausaRenunciaPresOferta').style.display = 'none';
                //si presenta oferta se ven datos de oferta, sino no
                var radioS = document.getElementById('radioContS');
                if(radioS.checked){
                    document.getElementById('divMotNoContratacion').style.display = 'none';
                    document.getElementById('divRenuncia').style.display = 'none';
                    document.getElementById('codNifContratado').readOnly = false;                    
                    document.getElementById('codNifContratado').className = 'inputTexto';
                    document.getElementById('descNifContratado').readOnly = false;                    
                    document.getElementById('descNifContratado').className = 'inputTexto';
                    document.getElementById('txtNifOferta').readOnly = false;                    
                    document.getElementById('txtNifOferta').className = 'inputTexto';
                    document.getElementById('txtNombre').readOnly = false;                    
                    document.getElementById('txtNombre').className = 'inputTexto';
                    document.getElementById('txtApe1').readOnly = false;                    
                    document.getElementById('txtApe1').className = 'inputTexto';
                    document.getElementById('txtApe2').readOnly = false;                    
                    document.getElementById('txtApe2').className = 'inputTexto';
                    document.getElementById('txtEmail').readOnly = false;                    
                    document.getElementById('txtEmail').className = 'inputTexto';
                    document.getElementById('txtTfno').readOnly = false;                    
                    document.getElementById('txtTfno').className = 'inputTexto';
                    document.getElementById('fechaNacContratado').readOnly = false;                    
                    document.getElementById('fechaNacContratado').className = 'inputTexto';
                    document.getElementById('calfechaNacContratado').style.display = 'inline'; 
                    document.getElementById('txtSexo').readOnly = false;                    
                    document.getElementById('txtSexo').className = 'inputTexto';
                    document.getElementById('codTitulacionContratado').readOnly = false;                    
                    document.getElementById('codTitulacionContratado').className = 'inputTexto';
                    document.getElementById('descTitulacionContratado').readOnly = false;                    
                    document.getElementById('descTitulacionContratado').className = 'inputTexto';
                    document.getElementById('botonNifContratado').style.display = 'inline'; 
                    document.getElementById('anoTitulacion').readOnly = false;                    
                    document.getElementById('anoTitulacion').className = 'inputTexto ';
                    document.getElementById('docContratacion').disabled = false;                    
                    document.getElementById('txtSexo').className = 'inputTexto ';
                    document.getElementById('fechaIniContratado').readOnly = false;                    
                    document.getElementById('fechaIniContratado').className = 'inputTexto ';
                    document.getElementById('calfechaIniContratado').style.display = 'inline'; 
                    document.getElementById('fechaFinContratado').readOnly = false;                    
                    document.getElementById('fechaFinContratado').className = 'inputTexto ';
                    document.getElementById('calfechaFinContratado').style.display = 'inline'; 
                    document.getElementById('codGrupoCotContratado').readOnly = false;                    
                    document.getElementById('codGrupoCotContratado').className = 'inputTexto ';
                    document.getElementById('descGrupoCotContratado').readOnly = false;                    
                    document.getElementById('descGrupoCotContratado').className = 'inputTexto ';
                    document.getElementById('botonGrupoCotContratado').style.display = 'inline'; 
                    document.getElementById('txtSalarioContratado').readOnly = false;  
                    document.getElementById('txtSalarioContratado').className = 'inputTexto textoNumerico';
                    document.getElementById('txtDietasConv').readOnly = false;                    
                    document.getElementById('txtDietasConv').className = 'inputTexto textoNumerico';
                    document.getElementById('txtDietasConvoc').readOnly = false;                    
                    document.getElementById('txtDietasConvoc').className = 'inputTexto textoNumerico';          
                    document.getElementById('fieldsetDatosPersonaContratada').style.display = 'inline';
                    document.getElementById('fieldsetDatosBaja').style.display = 'inline';
                    document.getElementById('divBaja').style.display = 'inline';
                }else{                    
                    document.getElementById('divMotNoContratacion').style.display = 'inline';
                    document.getElementById('divRenuncia').style.display = 'inline';
                    document.getElementById('codNifContratado').readOnly = true;                    
                    document.getElementById('codNifContratado').className = 'inputTexto readOnly';
                    document.getElementById('codNifContratado').value='';
                    document.getElementById('descNifContratado').readOnly = true;                    
                    document.getElementById('descNifContratado').className = 'inputTexto readOnly';
                    document.getElementById('descNifContratado').value='';
                    document.getElementById('txtNifOferta').readOnly = true;                    
                    document.getElementById('txtNifOferta').className = 'inputTexto readOnly';
                    document.getElementById('txtNifOferta').value='';
                    document.getElementById('txtNombre').readOnly = true;                    
                    document.getElementById('txtNombre').className = 'inputTexto readOnly';
                    document.getElementById('txtNombre').value='';
                    document.getElementById('txtApe1').readOnly = true;                    
                    document.getElementById('txtApe1').className = 'inputTexto readOnly';
                    document.getElementById('txtApe1').value='';
                    document.getElementById('txtApe2').readOnly = true;                    
                    document.getElementById('txtApe2').className = 'inputTexto readOnly';
                    document.getElementById('txtApe2').value='';
                    document.getElementById('txtEmail').readOnly = true;                    
                    document.getElementById('txtEmail').className = 'inputTexto readOnly';
                    document.getElementById('txtEmail').value='';
                    document.getElementById('txtTfno').readOnly = true;                    
                    document.getElementById('txtTfno').className = 'inputTexto readOnly';
                    document.getElementById('txtTfno').value='';
                    document.getElementById('fechaNacContratado').readOnly = true;                    
                    document.getElementById('fechaNacContratado').className = 'inputTexto readOnly';
                    document.getElementById('calfechaNacContratado').style.display = 'none'; 
                    document.getElementById('fechaNacContratado').value='';
                    document.getElementById('txtSexo').readOnly = true;                    
                    document.getElementById('txtSexo').className = 'inputTexto readOnly';
                    document.getElementById('txtSexo').value='';
                    document.getElementById('codTitulacionContratado').readOnly = true;                    
                    document.getElementById('codTitulacionContratado').className = 'inputTexto readOnly';
                     document.getElementById('codTitulacionContratado').value='';
                    document.getElementById('descTitulacionContratado').readOnly = true;                    
                    document.getElementById('descTitulacionContratado').className = 'inputTexto readOnly';
                     document.getElementById('descTitulacionContratado').value='';
                    document.getElementById('botonNifContratado').style.display = 'none'; 
                    document.getElementById('anoTitulacion').readOnly = true;                    
                    document.getElementById('anoTitulacion').className = 'inputTexto readOnly';
                    document.getElementById('anoTitulacion').value='';
                    document.getElementById('docContratacion').disabled = true;                  
                    document.getElementById('fechaIniContratado').readOnly = true;                    
                    document.getElementById('fechaIniContratado').className = 'inputTexto readOnly';
                    document.getElementById('fechaIniContratado').value='';
                    document.getElementById('calfechaIniContratado').style.display = 'none'; 
                    document.getElementById('fechaFinContratado').readOnly = true;                    
                    document.getElementById('fechaFinContratado').className = 'inputTexto readOnly';
                    document.getElementById('fechaFinContratado').value='';
                    document.getElementById('calfechaFinContratado').style.display = 'none'; 
                    document.getElementById('codGrupoCotContratado').readOnly = true;                    
                    document.getElementById('codGrupoCotContratado').className = 'inputTexto readOnly';
                    document.getElementById('codGrupoCotContratado').value='';
                    document.getElementById('descGrupoCotContratado').readOnly = true;                    
                    document.getElementById('descGrupoCotContratado').className = 'inputTexto readOnly';
                    document.getElementById('descGrupoCotContratado').value='';
                    document.getElementById('botonGrupoCotContratado').style.display = 'none'; 
                    document.getElementById('txtSalarioContratado').readOnly = true;  
                    document.getElementById('txtSalarioContratado').className = 'inputTexto readOnly textoNumerico';
                    document.getElementById('txtSalarioContratado').value='';
                    document.getElementById('txtDietasConv').readOnly = true;                    
                    document.getElementById('txtDietasConv').className = 'inputTexto readOnly textoNumerico';
                    document.getElementById('txtDietasConv').value='';
                    document.getElementById('txtDietasConvoc').readOnly = true;                    
                    document.getElementById('txtDietasConvoc').className = 'inputTexto readOnly textoNumerico';
                    document.getElementById('txtDietasConvoc').value='';
                    document.getElementById('fieldsetDatosPersonaContratada').style.display = 'none';
                    document.getElementById('fieldsetDatosBaja').style.display = 'none';
                    document.getElementById('divBaja').style.display = 'none';
                }
             
             /*if(document.getElementById('radioSustituyeS').checked && document.getElementById('fechaBaja').value=='' && document.getElementById('descCausaBaja').value=='')
             {
                document.getElementById('divAltaSustituto').style.display = 'none';
                document.getElementById('btnAltaSustituto').style.display = 'none';
             }else if(!document.getElementById('radioSustituyeS').checked && !document.getElementById('radioSustituyeN').checked)
             {
                document.getElementById('btnAltaSustituto').style.display = 'none';
             }else{
                document.getElementById('btnAltaSustituto').style.display = 'inline';
             }
             var radioS = document.getElementById('radioSustituyeS');
                if(radioS.checked){
                    //document.getElementById('divAltaSustituto').style.display = 'inline';
                    document.getElementById('btnAltaSustituto').style.display = 'none';
                }else if(document.getElementById('fechaBaja').value==''){
                    document.getElementById('btnAltaSustituto').style.display = 'none';
                }else if(document.getElementById('descCausaBaja').value==''){
                    document.getElementById('btnAltaSustituto').style.display = 'none';
                }else{
                    document.getElementById('btnAltaSustituto').style.display = 'inline';
                }*/
                
                var radioN = document.getElementById('radioSustituyeN');
                if(radioN.checked){
                    //document.getElementById('divAltaSustituto').style.display = 'inline';
                    document.getElementById('btnAltaSustituto').style.display = 'none';
                }else if(document.getElementById('fechaBaja').value==''){
                    document.getElementById('btnAltaSustituto').style.display = 'none';
                }else if(document.getElementById('descCausaBaja').value==''){
                    document.getElementById('btnAltaSustituto').style.display = 'none';
                }else if(!document.getElementById('radioSustituyeS').checked && !document.getElementById('radioSustituyeN').checked){
                    document.getElementById('btnAltaSustituto').style.display = 'none';
                }else{
                    document.getElementById('btnAltaSustituto').style.display = 'inline';
                }
                
             }else if(document.getElementById('radioPresOfertaN').checked){
                document.getElementById('fieldsetDatosOferta').style.display = 'none';
             }
             
             /*var radioN = document.getElementById('radioContN');
                if(radioN.checked){
                   document.getElementById('fieldsetDatosPersonaContratada').style.display = 'none';
                   document.getElementById('fieldsetDatosBaja').style.display = 'none';

                }*/
             
            }
            
            function checkDescSustituto(){
               
                /*if(document.getElementById('descCausaBaja').value=='' ){
                    document.getElementById('btnAltaSustituto').style.display = 'none';
                }else if(document.getElementById('radioSustituyeS').checked){
                    document.getElementById('btnAltaSustituto').style.display = 'none';
                }else if(document.getElementById('fechaBaja').value==''){
                    document.getElementById('btnAltaSustituto').style.display = 'none';
                }else{
                    document.getElementById('btnAltaSustituto').style.display = 'inline';
                }*/
                if(document.getElementById('descCausaBaja').value=='' ){
                    document.getElementById('btnAltaSustituto').style.display = 'none';
                }else if(document.getElementById('radioSustituyeN').checked){
                    document.getElementById('btnAltaSustituto').style.display = 'none';
                }else if(document.getElementById('fechaBaja').value==''){
                    document.getElementById('btnAltaSustituto').style.display = 'none';
                }else if(!document.getElementById('radioSustituyeS').checked && !document.getElementById('radioSustituyeN').checked){
                    document.getElementById('btnAltaSustituto').style.display = 'none';
                }else{
                    document.getElementById('btnAltaSustituto').style.display = 'inline';
                }
       
            }
            
            function checkSustituto(){
                
                
                if(document.getElementById('descCausaBaja').value==''){
                    document.getElementById('btnAltaSustituto').style.display = 'none';
                }else{
                    document.getElementById('btnAltaSustituto').style.display = 'inline';
                }
                
                if(document.getElementById('fechaBaja').value==''){
                    document.getElementById('btnAltaSustituto').style.display = 'none';
                }else{
                    document.getElementById('btnAltaSustituto').style.display = 'inline';
                }
  
            }
        </script>
    </head>
	<body id="cuerpoNuevaSolicitud" style="text-align: left;" onload="load();">
        <div id="divCuerpo" class="contenidoPantalla" style="overflow-y: auto; padding: 10px;">
            <form  id="formNuevaOferta" enctype="multipart/form-data">
                <div id="barraProgresoNuevaOferta" style="position: absolute; z-index:10; visibility: hidden; top: 85%; left: 30%;">
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
                                                            <%=meLanbide60I18n.getMensaje(idiomaUsuario, "msg.guardandoDatos")%>
                                                        </span>
                                                        <span id="msgBuscandoDatosTercero">
                                                            <%=meLanbide60I18n.getMensaje(idiomaUsuario, "msg.buscandoDatosTercero")%>
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
                    <legend class="legendAzul"><%=meLanbide60I18n.getMensaje(idiomaUsuario,"legend.puesto.datosPuesto")%></legend>
                    <div class="lineaFormulario">
                        <div style="float: left; width: 42px;">
                            <%=meLanbide60I18n.getMensaje(idiomaUsuario,"label.puesto.datosPuesto.puesto")%>
                        </div>
                        <div style="width: auto; float: left; margin-left: 11px;">
                            <input type="text" maxlength="5" size="5" id="codPuesto" name="codPuesto" value="" class="inputTexto readOnly" readonly="true"/>
                            <input type="text" maxlength="200" size="78" id="descPuesto" name="descPuesto" value="" class="inputTexto" />
                            <input type="button" id="btnCopiarDatosSoli" name="btnCopiarDatosSoli" class="botonGeneral"  value="<%=meLanbide60I18n.getMensaje(idiomaUsuario,"btn.oferta.copiarDatosSol")%>" onclick="copiarDatos();">
                        </div>
                    </div>
                    <div class="lineaFormulario">    
                        <div style="float: left; width: 42px;">
                            <%=meLanbide60I18n.getMensaje(idiomaUsuario,"label.puesto.datosPuesto.pais1")%>
                        </div>
                        <div style="width: auto; float: left; margin-left: 12px; margin-right: 25px;">
                            <input id="codPaisPuesto1" name="codPaisPuesto1" type="text" class="inputTexto" size="2" maxlength="3" 
                                   onkeypress="javascript:return SoloDigitosConsulta(event);" value="">
                            <input id="descPaisPuesto1" name="descPaisPuesto1" type="text" class="inputTexto" size="22" readonly >
                             <a id="anchorPaisPuesto1" name="anchorPaisPuesto1" href=""><span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonPaisPuesto1" name="botonPaisPuesto1" style="cursor:hand;" alt="<%=meLanbide60I18n.getMensaje(idiomaUsuario, "altDesplegable")%>" title="<%=meLanbide60I18n.getMensaje(idiomaUsuario, "altDesplegable")%>"></span></a>
                        </div>
                        <div style="float: left; width: 42px;">
                            <%=meLanbide60I18n.getMensaje(idiomaUsuario,"label.puesto.datosPuesto.pais2")%>
                        </div>
                        <div style="width: auto; float: left; margin-left: 10px; margin-right: 25px;">
                             <input id="codPaisPuesto2" name="codPaisPuesto2" type="text" class="inputTexto" size="2" maxlength="3" 
                                   onkeypress="javascript:return SoloDigitosConsulta(event);" value="" >
                            <input id="descPaisPuesto2" name="descPaisPuesto2" type="text" class="inputTexto" size="22" readonly >
                             <a id="anchorPaisPuesto2" name="anchorPaisPuesto2" href=""><span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonPaisPuesto2" name="botonPaisPuesto2" style="cursor:hand;" alt="<%=meLanbide60I18n.getMensaje(idiomaUsuario, "altDesplegable")%>" title="<%=meLanbide60I18n.getMensaje(idiomaUsuario, "altDesplegable")%>"></span></a>
                        </div>
                        <div style="float: left; width: 42px;">
                            <%=meLanbide60I18n.getMensaje(idiomaUsuario,"label.puesto.datosPuesto.pais3")%>
                        </div>
                        <div style="width: auto; float: left; margin-left: 10px;">
                            <input id="codPaisPuesto3" name="codPaisPuesto3" type="text" class="inputTexto" size="2" maxlength="3" 
                                   onkeypress="javascript:return SoloDigitosConsulta(event);" value="" >
                            <input id="descPaisPuesto3" name="descPaisPuesto3" type="text" class="inputTexto" size="22" readonly >
                             <a id="anchorPaisPuesto3" name="anchorPaisPuesto3" href=""><span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonPaisPuesto3" name="botonPaisPuesto3" style="cursor:hand;" alt="<%=meLanbide60I18n.getMensaje(idiomaUsuario, "altDesplegable")%>" title="<%=meLanbide60I18n.getMensaje(idiomaUsuario, "altDesplegable")%>"></span></a>
                        </div>
                    </div>
                    <div class="lineaFormulario">
                        <div style="float: left; width: 42px;">
                            <%=meLanbide60I18n.getMensaje(idiomaUsuario,"label.puesto.datosPuesto.ciudadDestino")%>
                        </div>
                        <div style="width: auto; float: left; margin-left: 10px; margin-right: 25px;">
                            <input type="text" maxlength="200" size="53" id="ciudadDestino" name="ciudadDestino" value="" class="inputTexto" />
                        </div>
                        <div style="float: left; width: 42px; margin-right: 9px;">
                            <%=meLanbide60I18n.getMensaje(idiomaUsuario,"label.puesto.datosPuesto.dpto")%>
                        </div>
                        <div style="width: auto; float: left; margin-left: 10px; margin-right: 25px;">
                            <input type="text" maxlength="200" size="53" id="dpto" name="dpto" value="" class="inputTexto" />
                        </div>
                    </div>
                    <div class="lineaFormulario">        
                        <div style="float: left; width: 78px;">
                            <%=meLanbide60I18n.getMensaje(idiomaUsuario,"label.puesto.datosPuesto.titulacion1")%>
                        </div>
                        <div style="width: auto; float: left; margin-left: 10px; margin-right: 25px;">
                            <input id="descTitulacionPuesto1" name="descTitulacionPuesto1" type="text" class="inputTexto" size="80" readonly >
                            <input type="button" id="btnBuscarTitulacionPuesto1" name="btnBuscarTitulacionPuesto1" class="botonGeneral" value="<%=meLanbide60I18n.getMensaje(idiomaUsuario,"btn.buscar")%>..." onclick="buscarTitulacion('TitulacionPuesto1');">
                        </div>
                        <div style="float: left; width: 94px;">
                            <%=meLanbide60I18n.getMensaje(idiomaUsuario,"label.puesto.datosPuesto.nivelFormativo")%>
                        </div>
                        <div style="width: auto; float: left; margin-left: 2px;">
                            <input id="codNivelFormativo" name="codNivelFormativo" type="text" class="inputTexto" size="2" maxlength="8" 
                                   onkeypress="javascript:return SoloDigitosConsulta(event);" value="" >
                            <input id="descNivelFormativo" name="descNivelFormativo" type="text" class="inputTexto" size="1" readonly >
                             <a id="anchorNivelFormativo" name="anchorNivelFormativo" href=""><span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonNivelFormativo" name="botonNivelFormativo" style="cursor:hand;" alt="<%=meLanbide60I18n.getMensaje(idiomaUsuario, "altDesplegable")%>" title="<%=meLanbide60I18n.getMensaje(idiomaUsuario, "altDesplegable")%>"></span></a>
                        </div>
                    </div>
                    <div class="lineaFormulario">
                        <div style="float: left; width: 78px;">
                            <%=meLanbide60I18n.getMensaje(idiomaUsuario,"label.puesto.datosPuesto.titulacion2")%>
                        </div>
                        <div style="width: auto; float: left; margin-left: 10px; margin-right: 25px;">
                            <input id="descTitulacionPuesto2" name="descTitulacionPuesto2" type="text" class="inputTexto" size="80" readonly >
                            <input type="button" id="btnBuscarTitulacionPuesto2" name="btnBuscarTitulacionPuesto2" class="botonGeneral" value="<%=meLanbide60I18n.getMensaje(idiomaUsuario,"btn.buscar")%>..." onclick="buscarTitulacion('TitulacionPuesto2');">
                        </div>  
                    </div>
                    <div class="lineaFormulario">
                        <div style="float: left; width: 78px;">
                            <%=meLanbide60I18n.getMensaje(idiomaUsuario,"label.puesto.datosPuesto.titulacion3")%>
                        </div>
                        <div style="width: auto; float: left; margin-left: 10px;">
                            <input id="descTitulacionPuesto3" name="descTitulacionPuesto3" type="text" class="inputTexto" size="80" readonly >
                            <input type="button" id="btnBuscarTitulacionPuesto3" name="btnBuscarTitulacionPuesto3" class="botonGeneral" value="<%=meLanbide60I18n.getMensaje(idiomaUsuario,"btn.buscar")%>..." onclick="buscarTitulacion('TitulacionPuesto3');">
                        </div>
                    </div>
                    <div class="lineaFormulario">      
                        <div style="float: left; width: 78px;">
                            <%=meLanbide60I18n.getMensaje(idiomaUsuario,"label.puesto.datosPuesto.funciones")%>
                        </div>
                        <div style="width: auto; float: left; margin-left: 10px;">
                            <textarea rows="4" cols="131" id="funcionesPuesto" name="funcionesPuesto" maxlength="1000" class="inputTexto " ><%=puestoConsulta != null && puestoConsulta.getFunciones() != null ? puestoConsulta.getFunciones().toUpperCase() : "" %></textarea>
                        </div>
                    </div>
                    <div class="lineaFormulario">
                        <div style="float: left; width: 78px;">
                            <%=meLanbide60I18n.getMensaje(idiomaUsuario,"label.puesto.datosPuesto.idioma")%>
                        </div>
                        <div style="width: auto; float: left; margin-left: 10px; margin-right: 25px;">
                            <input id="codIdiomaPuesto1" name="codIdiomaPuesto1" type="text" class="inputTexto" size="2" maxlength="8" 
                                   onkeypress="javascript:return SoloDigitosConsulta(event);" value="" >
                            <input id="descIdiomaPuesto1" name="descIdiomaPuesto1" type="text" class="inputTexto" size="17" readonly >
                             <a id="anchorIdiomaPuesto1" name="anchorIdiomaPuesto1" href=""><span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonIdiomaPuesto1" name="botonIdiomaPuesto1" style="cursor:hand;" alt="<%=meLanbide60I18n.getMensaje(idiomaUsuario, "altDesplegable")%>" title="<%=meLanbide60I18n.getMensaje(idiomaUsuario, "altDesplegable")%>"></span></a>
                        </div>
                        <div style="float: left; width: 78px;">
                            <%=meLanbide60I18n.getMensaje(idiomaUsuario,"label.puesto.datosPuesto.nivelIdioma")%>
                        </div>
                        <div style="width: auto; float: left; margin-left: 7px;">
                            <input id="codNivelIdiomaPuesto1" name="codNivelIdiomaPuesto1" type="text" class="inputTexto" size="2" maxlength="8" 
                                   onkeypress="javascript:return SoloDigitosConsulta(event);" value="" >
                            <input id="descNivelIdiomaPuesto1" name="descNivelIdiomaPuesto1" type="text" class="inputTexto" size="17" readonly >
                             <a id="anchorNivelIdiomaPuesto1" name="anchorNivelIdiomaPuesto1" href=""><span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonNivelIdiomaPuesto1" name="botonNivelIdiomaPuesto1" style="cursor:hand;" alt="<%=meLanbide60I18n.getMensaje(idiomaUsuario, "altDesplegable")%>" title="<%=meLanbide60I18n.getMensaje(idiomaUsuario, "altDesplegable")%>"></span></a>
                        </div>
                    </div>
                    <div class="lineaFormulario">
                        <div style="float: left; width: 78px;">
                            <%=meLanbide60I18n.getMensaje(idiomaUsuario,"label.puesto.datosPuesto.idioma")%>
                        </div>
                        <div style="width: auto; float: left; margin-left: 10px; margin-right: 25px;">
                            <input id="codIdiomaPuesto2" name="codIdiomaPuesto2" type="text" class="inputTexto" size="2" maxlength="8" 
                                   onkeypress="javascript:return SoloDigitosConsulta(event);" value="" >
                            <input id="descIdiomaPuesto2" name="descIdiomaPuesto2" type="text" class="inputTexto" size="17" readonly >
                             <a id="anchorIdiomaPuesto2" name="anchorIdiomaPuesto2" href=""><span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonIdiomaPuesto2" name="botonIdiomaPuesto2" style="cursor:hand;" alt="<%=meLanbide60I18n.getMensaje(idiomaUsuario, "altDesplegable")%>" title="<%=meLanbide60I18n.getMensaje(idiomaUsuario, "altDesplegable")%>"></span></a>
                        </div>
                        <div style="float: left; width: 78px;">
                            <%=meLanbide60I18n.getMensaje(idiomaUsuario,"label.puesto.datosPuesto.nivelIdioma")%>
                        </div>
                        <div style="width: auto; float: left; margin-left: 7px;">
                            <input id="codNivelIdiomaPuesto2" name="codNivelIdiomaPuesto2" type="text" class="inputTexto" size="2" maxlength="8" 
                                   onkeypress="javascript:return SoloDigitosConsulta(event);" value="" >
                            <input id="descNivelIdiomaPuesto2" name="descNivelIdiomaPuesto2" type="text" class="inputTexto" size="17" readonly >
                             <a id="anchorNivelIdiomaPuesto2" name="anchorNivelIdiomaPuesto2" href=""><span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonNivelIdiomaPuesto2" name="botonNivelIdiomaPuesto2" style="cursor:hand;" alt="<%=meLanbide60I18n.getMensaje(idiomaUsuario, "altDesplegable")%>" title="<%=meLanbide60I18n.getMensaje(idiomaUsuario, "altDesplegable")%>"></span></a>
                        </div>
                    </div>
                    <div class="lineaFormulario">
                        <div style="float: left; width: 78px;">
                            <%=meLanbide60I18n.getMensaje(idiomaUsuario,"label.puesto.datosPuesto.idioma")%>
                        </div>
                        <div style="width: auto; float: left; margin-left: 10px; margin-right: 25px;">
                            <input id="codIdiomaPuesto3" name="codIdiomaPuesto3" type="text" class="inputTexto" size="2" maxlength="8" 
                                   onkeypress="javascript:return SoloDigitosConsulta(event);" value="" >
                            <input id="descIdiomaPuesto3" name="descIdiomaPuesto3" type="text" class="inputTexto" size="17" readonly >
                             <a id="anchorIdiomaPuesto3" name="anchorIdiomaPuesto3" href=""><span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonIdiomaPuesto3" name="botonIdiomaPuesto3" style="cursor:hand;" alt="<%=meLanbide60I18n.getMensaje(idiomaUsuario, "altDesplegable")%>" title="<%=meLanbide60I18n.getMensaje(idiomaUsuario, "altDesplegable")%>"></span></a>
                        </div>
                        <div style="float: left; width: 78px;">
                            <%=meLanbide60I18n.getMensaje(idiomaUsuario,"label.puesto.datosPuesto.nivelIdioma")%>
                        </div>
                        <div style="width: auto; float: left; margin-left: 7px;">
                           <input id="codNivelIdiomaPuesto3" name="codNivelIdiomaPuesto3" type="text" class="inputTexto" size="2" maxlength="8" 
                                   onkeypress="javascript:return SoloDigitosConsulta(event);" value="" >
                            <input id="descNivelIdiomaPuesto3" name="descNivelIdiomaPuesto3" type="text" class="inputTexto" size="17" readonly >
                             <a id="anchorNivelIdiomaPuesto3" name="anchorNivelIdiomaPuesto3" href=""><span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonNivelIdiomaPuesto3" name="botonNivelIdiomaPuesto3" style="cursor:hand;" alt="<%=meLanbide60I18n.getMensaje(idiomaUsuario, "altDesplegable")%>" title="<%=meLanbide60I18n.getMensaje(idiomaUsuario, "altDesplegable")%>"></span></a>
                        </div>
                    </div>
                    <div class="lineaFormulario">
                        <div style="float: left; width: 90px;">
                            <%=meLanbide60I18n.getMensaje(idiomaUsuario,"label.oferta.datosOferta.presentaOferta")%>
                        </div>
                        <div style="width: auto; float: left; margin-left: 10px;">
                            <div style="float: left;">
                                <input type="radio" name="radioPresOferta" id="radioPresOfertaS" value="S" onclick="cambioRadioPresOferta(this);"><%=meLanbide60I18n.getMensaje(idiomaUsuario, "label.si")%>
                                <input type="radio" name="radioPresOferta" id="radioPresOfertaN" value="N" onclick="cambioRadioPresOferta(this);"><%=meLanbide60I18n.getMensaje(idiomaUsuario, "label.no")%>
                            </div>
                        </div>
                        <div style="float: left;" id="divTxtCausaRenunciaPresOferta" name="divTxtCausaRenunciaPresOferta">    
                            <div style="float: left; width: 110px;">
                                <%=meLanbide60I18n.getMensaje(idiomaUsuario,"label.oferta.datosOferta.causaRenuncia")%>
                            </div>
                            <div style="float: left;">
                                <input id="codCausaRenunciaPresOferta" name="codCausaRenunciaPresOferta" type="text" class="inputTexto" size="7" maxlength="8" 
                                       onkeypress="javascript:return SoloDigitosConsulta(event);" value="" >
                                <input id="descCausaRenunciaPresOferta" name="descCausaRenunciaPresOferta" type="text" class="inputTexto" size="20" readonly >
                                 <a id="anchorCausaRenunciaPresOferta" name="anchorCausaRenunciaPresOferta" href=""><span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonCausaRenunciaPresOferta" name="botonCausaRenunciaPresOferta" style="cursor:hand;" alt="<%=meLanbide60I18n.getMensaje(idiomaUsuario, "altDesplegable")%>" title="<%=meLanbide60I18n.getMensaje(idiomaUsuario, "altDesplegable")%>"></span></a>
                            </div>
                        </div>
                       
                    </div>
                </fieldset>
                            
                <div class="lineaFormulario" id="divDatosOferta" name="divDatosOferta">            
                <fieldset  style="width: 100%;" id="fieldsetDatosOferta" name="fieldsetDatosOferta">
                    <legend class="legendAzul"><%=meLanbide60I18n.getMensaje(idiomaUsuario,"legend.oferta.datosOferta")%></legend>
                    <div class="lineaFormulario">
                        <div style="float: left; width: 90px;">
                            <%=meLanbide60I18n.getMensaje(idiomaUsuario,"label.oferta.datosOferta.numOferta")%>
                        </div>
                        <div style="width: auto; float: left; margin-left: 10px; margin-right: 20px;">
                            <input type="text" class="inputTexto" maxlength="15" size="15" id="txtNumOferta" name="txtNumOferta" value=""/>
                        </div>
                        <div style="float: left; width: 120px;">
                            <%=meLanbide60I18n.getMensaje(idiomaUsuario,"label.oferta.datosOferta.ofiGestion")%>
                        </div>
                        <div style="width: auto; float: left; margin-left: 10px;">
                            <input id="codOfiGestionOferta" name="codOfiGestionOferta" type="text" class="inputTexto" size="7" maxlength="8" 
                                   onkeypress="javascript:return SoloDigitosConsulta(event);" value="" >
                            <input id="descOfiGestionOferta" name="descOfiGestionOferta" type="text" class="inputTexto" size="20" readonly >
                             <a id="anchorOfiGestionOferta" name="anchorOfiGestionOferta" href=""><span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonOfiGestionOferta" name="botonOfiGestionOferta" style="cursor:hand;" alt="<%=meLanbide60I18n.getMensaje(idiomaUsuario, "altDesplegable")%>" title="<%=meLanbide60I18n.getMensaje(idiomaUsuario, "altDesplegable")%>"></span></a>
                        </div>
                    </div>
                    
                    <div class="lineaFormulario">
                        <div style="float: left; width: 90px;">
                            <%=meLanbide60I18n.getMensaje(idiomaUsuario,"label.oferta.datosOferta.difuOferta")%>
                        </div>
                        <div style="width: auto; float: left; margin-left: 10px; margin-right: 15px;">
                            <input style="margin-right: 2px" type="checkBox" id="checkDifusionOferta" name="checkDifusionOferta" value="" onclick="cambioValorCheckDifusion(this);"></input>
                        </div>
                        <div id="divFechaDifusion" name="divFechaDifusion">
                            <div style="float: left; width: 90px;">
                                <%=meLanbide60I18n.getMensaje(idiomaUsuario,"label.oferta.datosOferta.fechaDifuOferta")%>
                            </div>
                            <div style="width: auto; float: left; margin-left: 10px; margin-right: 150px;">
                                <input type="text" class="inputTxtFecha" size="10" maxlength="10" id="fechaDifusionOferta" name="fechaDifusionOferta" onkeyup="return SoloCaracteresFecha(this);" onblur="javascript:return comprobarFechaCme(this, '<%=meLanbide60I18n.getMensaje(idiomaUsuario, "msg.fechaNoVal")%>');" onblur="javascript:return comprobarFechaCme(this, '<%=meLanbide60I18n.getMensaje(idiomaUsuario, "msg.fechaNoVal")%>');" onfocus="this.select();" value=""/>
                                <A href="javascript:calClick(event);" onclick="mostrarCalFechaDifusionOferta(event);return false;" style="text-decoration:none;" >
                                    <IMG style="border: 0" height="17" id="calfechaDifusionOferta" name="calfechaDifusionOferta" alt="<%=meLanbide60I18n.getMensaje(idiomaUsuario,"label.oferta.datosOferta.difuOferta")%>" src="<c:url value='/images/calendario/icono.gif'/>"/>
                                </A>
                            </div>
                        </div>
                    </div>
                    <div class="lineaFormulario">
                        <div style="float: left; width: 224px;">
                            <%=meLanbide60I18n.getMensaje(idiomaUsuario,"label.oferta.datosOferta.fecEnvioCandidatos")%>
                        </div>
                        <div style="width: auto; float: left; margin-left: 12px; margin-right: 20px;">
                            
                            <input type="text" class="inputTxtFecha" size="10" maxlength="10" id="fechaEnvioCandidatosOferta" name="fechaEnvioCandidatosOferta" onkeyup="return SoloCaracteresFecha(this);" onblur="javascript:return comprobarFechaCme(this, '<%=meLanbide60I18n.getMensaje(idiomaUsuario, "msg.fechaNoVal")%>');" onfocus="this.select();" value=""/>
                            <A href="javascript:calClick(event);" onclick="mostrarCalFechaEnvioCandidatosOferta(event);return false;" style="text-decoration:none;" >
                                <IMG style="border: 0" height="17" id="calfechaEnvioCandidatosOferta" name="calfechaEnvioCandidatosOferta" alt="<%=meLanbide60I18n.getMensaje(idiomaUsuario,"label.oferta.datosOferta.fecEnvioCandidatos")%>" src="<c:url value='/images/calendario/icono.gif'/>"/>
                            </A>
                        </div>
                        <div style="float: left; width: 200px;">
                            <%=meLanbide60I18n.getMensaje(idiomaUsuario,"label.oferta.datosOferta.numTotCandidatosEnv")%>
                        </div>
                        <div style="width: auto; float: left; margin-left: 10px;">
                            <input type="text" class="inputTexto textoNumerico" maxlength="3" size="5" id="txtNumCandidatosEnv" name="txtNumCandidatosEnv" value=""/>
                        </div>
                    </div>
                    <div class="lineaFormulario">
                        <div style="float: left; width: 765px;">
                            <%=meLanbide60I18n.getMensaje(idiomaUsuario,"label.oferta.datosContratado.docGetionOferta")%>:&nbsp;
                            <input type="file"  name="docGestionOferta" id="docGestionOferta" class="inputTexto" size="57">
                            <input type="button" id="btnVerDocGestionOferta" name="btnVerDocGestionOferta" class="botonGeneral" value="<%=meLanbide60I18n.getMensaje(idiomaUsuario,"btn.ver")%>" onclick="verDocumento('gestionOferta');">
                            <input type="button" id="btnModifDocGestionOferta" name="btnModifDocGestionOferta" class="botonGeneral" value="<%=meLanbide60I18n.getMensaje(idiomaUsuario,"btn.modificar")%>" onclick="mostrarModificarDocumentoGestion();">
                            <input type="button" id="btnCancelarModifDocGestionOferta" name="btnCancelarModifDocGestionOferta" class="botonGeneral" value="<%=meLanbide60I18n.getMensaje(idiomaUsuario,"btn.cancelar")%>" onclick="cancelarModificarDocumentoGestion();">
                        </div>
                    </div>
                    <div class="lineaFormulario">
                        <div style="float: left; width: 90px;">
                            <%=meLanbide60I18n.getMensaje(idiomaUsuario,"label.oferta.datosOferta.precandidatos")%>
                        </div>
                        <div style="width: auto; float: left; margin-left: 10px; margin-right: 15px;">
                            <input style="margin-right: 2px" type="checkBox" id="checkPrecandidatosOferta" name="checkPrecandidatosOferta" value="" onclick="cambioValorCheckPrecandidatos(this);"></input>
                        </div>
                        <div style="width: auto; float: left;" id="divNomPrecandidatos" name="divNomPrecandidatos">
                            
                            <%
                                if(ofertaConDatos.getPrec() != null && ofertaConDatos.getPrec().equalsIgnoreCase(ConstantesMeLanbide60.CIERTO))
                                {
                            %>
                            <textarea rows="4" cols="122" id="txtNomPrecandidatos" name="txtNomPrecandidatos" maxlength="200" class="inputTexto"><%=ofertaConDatos.getPrecNom() != null ? ofertaConDatos.getPrecNom().toUpperCase() : "" %></textarea>
                            <%
                                }
                                else
                               {
                            %>
                            <textarea rows="4" cols="122" id="txtNomPrecandidatos" name="txtNomPrecandidatos" maxlength="200" class="inputTexto"></textarea>
                            <%
                               }
                            %>
                        </div>
                    </div>
                    <div class="lineaFormulario">
                        <div style="float: left; width: 90px;">
                            <%=meLanbide60I18n.getMensaje(idiomaUsuario,"label.oferta.datosOferta.contratacion")%>
                        </div>
                        <div style="width: auto; float: left; margin-left: 10px;">
                            <div style="float: left;">
                                <input type="radio" name="radioCont" id="radioContS" value="S" onclick="cambioRadioContratacion(this);"><%=meLanbide60I18n.getMensaje(idiomaUsuario, "label.si")%>
                                <input type="radio" name="radioCont" id="radioContN" value="N" onclick="cambioRadioContratacion(this);"><%=meLanbide60I18n.getMensaje(idiomaUsuario, "label.no")%>
                            </div>
                        </div>
                        <div id="divMotNoContratacion" name="divMotNoContratacion">
                            <div style="float: left; width: 35px; margin-left: 20px;">
                                <%=meLanbide60I18n.getMensaje(idiomaUsuario,"label.oferta.datosOferta.motNoContratacion")%>
                            </div>
                            <div style="width: auto; float: left; margin-left: 6px;">
                                <textarea rows="4" cols="105" id="motivoNoCont" name="motivoNoCont" maxlength="1000" class="inputTexto"><%if(ofertaConDatos.getContratacion() != null && ofertaConDatos.getContratacion().equalsIgnoreCase(ConstantesMeLanbide60.FALSO)){%><%=ofertaConDatos.getMotContratacion() != null ? ofertaConDatos.getMotContratacion().toUpperCase() : "" %><%}%></textarea>
                            </div>
                        </div>
                    </div>
                    <div class="lineaFormulario" id="divRenuncia" name="divRenuncia">
                        <div style="float: left; width: 90px;" id="divTxtFechaRenuncia" name="divTxtFechaRenuncia">
                            <%=meLanbide60I18n.getMensaje(idiomaUsuario,"label.oferta.datosOferta.fechaRenuncia")%>
                        </div>
                        <div style="float: left; margin-left: 10px;" id="divFechaRenuncia" name="divFechaRenuncia">
                            <input type="text" name="fechaRenuncia" id="fechaRenuncia" value="" size="8"/>
                        </div>
                        <div style="float: left; width: 110px;" id="divTxtCausaRenuncia" name="divTxtCausaRenuncia">
                            <%=meLanbide60I18n.getMensaje(idiomaUsuario,"label.oferta.datosOferta.causaRenuncia")%>
                        </div>
                        <div style="float: left;">
                            <input id="codCausaRenuncia" name="codCausaRenuncia" type="text" class="inputTexto" size="7" maxlength="8" 
                                   onkeypress="javascript:return SoloDigitosConsulta(event);" value="" >
                            <input id="descCausaRenuncia" name="descCausaRenuncia" type="text" class="inputTexto" size="20" readonly >
                             <a id="anchorCausaRenuncia" name="anchorCausaRenuncia" href=""><span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonCausaRenuncia" name="botonCausaRenuncia" style="cursor:hand;" alt="<%=meLanbide60I18n.getMensaje(idiomaUsuario, "altDesplegable")%>" title="<%=meLanbide60I18n.getMensaje(idiomaUsuario, "altDesplegable")%>"></span></a>
                        </div>
                    </div>
                </fieldset>
                </div>
                        
                <div id="divBaja" name="divBaja">
                <fieldset  style="width: 100%;" id="fieldsetDatosPersonaContratada" name="fieldsetDatosPersonaContratada">
                    <legend class="legendAzul"><%=meLanbide60I18n.getMensaje(idiomaUsuario,"legend.oferta.datosContratado")%></legend>
                    
                    <div class="lineaFormulario">     
                        <div style="float: left; width: 90px;">
                            <%=meLanbide60I18n.getMensaje(idiomaUsuario,"label.oferta.datosContratado.nif")%>
                        </div>
                        <div style="width: auto; float: left; margin-left: 10px;">
                            <input id="codNifContratado" name="codNifContratado" type="text" class="inputTexto" size="7" maxlength="8" 
                                   onkeypress="javascript:return SoloDigitosConsulta(event);" value="" onblur="buscarTercero();">
                            <input id="descNifContratado" name="descNifContratado" type="text" class="inputTexto" size="19" readonly >
                             <a id="anchorNifContratado" name="anchorNifContratado" href=""><span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonNifContratado" name="botonNifContratado" style="cursor:hand;" alt="<%=meLanbide60I18n.getMensaje(idiomaUsuario, "altDesplegable")%>" title="<%=meLanbide60I18n.getMensaje(idiomaUsuario, "altDesplegable")%>"></span></a>
                        </div>
                        <div style="width: auto; float: left; margin-left: 20px;">
                            <input type="text" class="inputTexto" maxlength="15" size="10" id="txtNifOferta" name="txtNifOferta" value="" onchange="buscarTercero();"/>
                        </div>
                    </div>
                    <div class="lineaFormulario"> 
                        <div style="float: left; width: 90px;">
                            <%=meLanbide60I18n.getMensaje(idiomaUsuario,"label.oferta.datosContratado.nombre")%>
                        </div>
                        <div style="width: auto; float: left; margin-left: 10px;">
                            <input type="text" class="inputTexto" maxlength="200" size="30" id="txtNombre" name="txtNombre" value=""/>
                        </div>
                        <div style="float: left; width: 80px; margin-left: 20px;">
                            <%=meLanbide60I18n.getMensaje(idiomaUsuario,"label.oferta.datosContratado.ape1")%>
                        </div>
                        <div style="width: auto; float: left; margin-left: 10px;">
                            <input type="text" class="inputTexto" maxlength="200" size="30" id="txtApe1" name="txtApe1" value=""/>
                        </div>
                        <div style="float: left; width: 80px; margin-left: 20px;">
                            <%=meLanbide60I18n.getMensaje(idiomaUsuario,"label.oferta.datosContratado.ape2")%>
                        </div>
                        <div style="width: auto; float: left">
                            <input type="text" class="inputTexto" maxlength="200" size="30" id="txtApe2" name="txtApe2" value=""/>
                        </div>
                    </div>
                    <div class="lineaFormulario"> 
                        <div style="float: left; width: 90px;">
                            <%=meLanbide60I18n.getMensaje(idiomaUsuario,"label.oferta.datosContratado.email")%>
                        </div>
                        <div style="width: auto; float: left; margin-left: 10px;">
                            <input type="text" class="inputTexto" maxlength="200" size="30" id="txtEmail" name="txtEmail" value=""/>
                        </div>
                        <div style="float: left; width: 80px; margin-left: 20px;">
                            <%=meLanbide60I18n.getMensaje(idiomaUsuario,"label.oferta.datosContratado.tfno")%>
                        </div>
                        <div style="width: auto; float: left; margin-left: 10px;">
                            <input type="text" class="inputTexto" maxlength="15" size="15" id="txtTfno" name="txtTfno" value=""/>
                        </div>
                    </div>
                    <div class="lineaFormulario">
                        <div style="float: left; width: 90px;">
                            <%=meLanbide60I18n.getMensaje(idiomaUsuario,"label.oferta.datosContratado.fechaNac")%>
                        </div>
                        <div style="width: auto; float: left; margin-left: 10px;">
                            <input type="text" class="inputTxtFecha" size="10" maxlength="10" id="fechaNacContratado" name="fechaNacContratado" onkeyup="return SoloCaracteresFecha(this);" onblur="javascript:return comprobarFechaCme(this, '<%=meLanbide60I18n.getMensaje(idiomaUsuario, "msg.fechaNoVal")%>');" onfocus="this.select();" value=""/>
                            <A href="javascript:calClick(event);" onclick="mostrarCalFechaNacContratado(event);return false;" style="text-decoration:none;" >
                                <IMG style="border: 0" height="17" id="calfechaNacContratado" name="calfechaNacContratado" alt="<%=meLanbide60I18n.getMensaje(idiomaUsuario,"label.oferta.datosContratado.fechaNac")%>" src="<c:url value='/images/calendario/icono.gif'/>"/>
                            </A>
                        </div>
                        <div style="float: left; width: 80px; margin-left: 111px;">
                            <%=meLanbide60I18n.getMensaje(idiomaUsuario,"label.oferta.datosContratado.sexo")%>
                        </div>
                        <div style="width: auto; float: left; margin-left: 10px;">
                            <input type="text" class="inputTexto" maxlength="1" size="3" id="txtSexo" name="txtSexo" value="" onkeyup="comprobarSexo(this);" onblur="comprobarSexo(this);"/>
                        </div>
                    </div>
                    <div class="lineaFormulario">
                        <div style="float: left; width: 90px;">
                            <%=meLanbide60I18n.getMensaje(idiomaUsuario,"label.oferta.datosContratado.titulacion")%>
                        </div>
                        <div style="width: auto; float: left; margin-left: 10px;">
                            <input id="descTitulacionContratado" name="descTitulacionContratado" type="text" class="inputTexto" size="70" readonly >
                            <input type="button" id="btnBuscarTitulacionContratado" name="btnBuscarTitulacionContratado" class="botonGeneral" value="<%=meLanbide60I18n.getMensaje(idiomaUsuario,"btn.buscar")%>..." onclick="buscarTitulacion('TitulacionContratado');">
                        </div>
                        <div style="float: left; width: 40px; margin-left: 26px;">
                            <%=meLanbide60I18n.getMensaje(idiomaUsuario,"label.oferta.datosContratado.anoTitulacion")%>
                        </div>
                        <div style="width: auto; float: left; margin-left: 10px; ">
                            <input type="text" class="inputTexto" maxlength="4" size="3" id="anoTitulacion" name="anoTitulacion" value=""/>
                        </div>
                    </div>
                    <div class="lineaFormulario">
                        <div style="float: left; width: 765px;">
                            <%=meLanbide60I18n.getMensaje(idiomaUsuario,"label.oferta.datosContratado.docContratacion")%>:&nbsp;
                            <input type="file"  name="docContratacion" id="docContratacion" class="inputTexto" size="57">
                            <input type="button" id="btnVerDocContratacion" name="btnDocContratacion" class="botonGeneral" value="<%=meLanbide60I18n.getMensaje(idiomaUsuario,"btn.ver")%>" onclick="verDocumento('contratacionOferta');">
                            <input type="button" id="btnModifDocContratacion" name="btnDocContratacion" class="botonGeneral" value="<%=meLanbide60I18n.getMensaje(idiomaUsuario,"btn.modificar")%>" onclick="mostrarModificarDocumentoContratacion();">
                            <input type="button" id="btnCancelarModifDocContratacion" name="btnCancelarModifDocContratacion" class="botonGeneral" value="<%=meLanbide60I18n.getMensaje(idiomaUsuario,"btn.cancelar")%>" onclick="cancelarModificarDocumentoContratacion();">
                        </div>
                    </div>
                    <div class="lineaFormulario">
                        <div style="float: left; width: 90px;">
                            <%=meLanbide60I18n.getMensaje(idiomaUsuario,"label.oferta.datosContratado.feIni")%>
                        </div>
                        <div style="width: auto; float: left; margin-left: 10px;">
                            <input type="text" class="inputTxtFecha" size="10" maxlength="10" id="fechaIniContratado" name="fechaIniContratado" onkeyup="return SoloCaracteresFecha(this);" onblur="if(comprobarFechaCme(this, '<%=meLanbide60I18n.getMensaje(idiomaUsuario, "msg.fechaNoVal")%>')){calcularMesesContratado();}" onfocus="this.select();" value=""/>
                            <A href="javascript:calClick(event);" onclick="mostrarCalFechaIniContratado(event);return false;" style="text-decoration:none;" >
                                <IMG style="border: 0" height="17" id="calfechaIniContratado" name="calfechaIniContratado" alt="<%=meLanbide60I18n.getMensaje(idiomaUsuario,"label.oferta.datosContratado.feIni")%>" src="<c:url value='/images/calendario/icono.gif'/>"/>
                            </A>
                        </div>
                        <div style="float: left; width: 80px; margin-left: 110px;">
                            <%=meLanbide60I18n.getMensaje(idiomaUsuario,"label.oferta.datosContratado.feFin")%>
                        </div>
                        <div style="width: auto; float: left; margin-left: 10px; margin-right: 150px;">
                            <input type="text" class="inputTxtFecha" size="10" maxlength="10" id="fechaFinContratado" name="fechaFinContratado" onkeyup="return SoloCaracteresFecha(this);" onblur="if(comprobarFechaCme(this, '<%=meLanbide60I18n.getMensaje(idiomaUsuario, "msg.fechaNoVal")%>')){calcularMesesContratado();}" onfocus="this.select();" value=""/>
                            <A href="javascript:calClick(event);" onclick="mostrarCalFechaFinContratado(event);return false;" style="text-decoration:none;" >
                                <IMG style="border: 0" height="17" id="calfechaFinContratado" name="calfechaFinContratado" alt="<%=meLanbide60I18n.getMensaje(idiomaUsuario,"label.oferta.datosContratado.feFin")%>" src="<c:url value='/images/calendario/icono.gif'/>"/>
                            </A>
                        </div>
                        <div style="float: left; width: 80px; margin-left: 60px;">
                            <%=meLanbide60I18n.getMensaje(idiomaUsuario,"label.puesto.datosContrato.duracionMeses")%>
                        </div>
                        <div style="width: auto; float: left; margin-left: 10px;">
                            <input type="text" class="inputTexto textoNumerico" size="10" maxlength="10" id="txtMesesContratado" name="txtMesesContratado" value=""/>
                        </div>
                    </div>
                    <div class="lineaFormulario">
                        <div style="float: left; width: 90px;">
                            <%=meLanbide60I18n.getMensaje(idiomaUsuario,"label.oferta.datosContratado.grCot")%>
                        </div>
                        <div style="width: auto; float: left; margin-left: 10px; margin-right: 22px;">
                            <input id="codGrupoCotContratado" name="codGrupoCotContratado" type="text" class="inputTexto" size="2" maxlength="8" 
                                   onkeypress="javascript:return SoloDigitosConsulta(event);" value="">
                            <input id="descGrupoCotContratado" name="descGrupoCotContratado" type="text" class="inputTexto" size="20" readonly >
                             <a id="anchorGrupoCotContratado" name="anchorGrupoCotContratado" href=""><span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonGrupoCotContratado" name="botonGrupoCotContratado" style="cursor:hand;" alt="<%=meLanbide60I18n.getMensaje(idiomaUsuario, "altDesplegable")%>" title="<%=meLanbide60I18n.getMensaje(idiomaUsuario, "altDesplegable")%>"></span></a>
                        </div>
                    </div>
                    <div class="lineaFormulario">
                        <div style="float: left; width: 125px; margin-right: 2px;">
                            <%=meLanbide60I18n.getMensaje(idiomaUsuario,"label.oferta.datosContratado.salarioB")%>
                        </div>
                        <div style="width: auto; float: left; margin-left: 10px;">
                            <input type="text" maxlength="11" size="8" id="txtSalarioContratado" name="txtSalarioContratado" value="" class="inputTexto textoNumerico" onchange="reemplazarPuntosCme(this);" onblur="reemplazarPuntosCme(this); ajustarDecimalesImportes();"/>
                        </div>
                        <div style="float: left; width: 150px; margin-left: 20px;">
                            <%=meLanbide60I18n.getMensaje(idiomaUsuario,"label.oferta.datosContratado.dietasConv")%>
                        </div>
                        <div style="width: auto; float: left; margin-left: 10px;">
                            <input type="text" maxlength="11" size="8" id="txtDietasConv" name="txtDietasConv" value="" class="inputTexto textoNumerico" onchange="reemplazarPuntosCme(this);" onblur="reemplazarPuntosCme(this); ajustarDecimalesImportes();"/>
                        </div>
                        <div style="float: left; width: 150px; margin-left: 20px;">
                            <%=meLanbide60I18n.getMensaje(idiomaUsuario,"label.oferta.datosContratado.dietasConvoc")%>
                        </div>
                        <div style="width: auto; float: left; margin-left: 10px;">
                            <input type="text" maxlength="11" size="8" id="txtDietasConvoc" name="txtDietasConvoc" value="" class="inputTexto textoNumerico" onchange="reemplazarPuntosCme(this);" onblur="reemplazarPuntosCme(this); ajustarDecimalesImportes();"/>
                        </div>
                    </div>
                </fieldset>
                <fieldset  style="width: 100%;" id="fieldsetDatosBaja" name="fieldsetDatosBaja">
                    <legend class="legendAzul"><%=meLanbide60I18n.getMensaje(idiomaUsuario,"legend.oferta.datosBaja")%></legend> 
                    <div class="lineaFormulario">
                        <div style="float: left; width: 90px;">
                            <%=meLanbide60I18n.getMensaje(idiomaUsuario,"label.oferta.datosBaja.fecha")%>
                        </div>
                        <div style="width: auto; float: left; margin-left: 10px; margin-right: 25px;">
                            <input type="text" class="inputTxtFecha" size="10" maxlength="10" id="fechaBaja" name="fechaBaja" onkeyup="checkSustituto();return SoloCaracteresFecha(this);" onblur="javascript:return comprobarFechaCme(this, '<%=meLanbide60I18n.getMensaje(idiomaUsuario, "msg.fechaNoVal")%>');" onfocus="this.select();" onchange="checkSustituto();" value=""/>
                            <A href="javascript:calClick(event);" onclick="mostrarCalFechaBaja(event);return false;" style="text-decoration:none;" >
                                <IMG style="border: 0" height="17" id="calFechaBaja" name="calFechaBaja" alt="<%=meLanbide60I18n.getMensaje(idiomaUsuario,"label.oferta.datosBaja.fecha")%>" src="<c:url value='/images/calendario/icono.gif'/>"/>
                            </A>
                        </div>
                        <div style="float: left; width: 90px;">
                            <%=meLanbide60I18n.getMensaje(idiomaUsuario,"label.oferta.datosBaja.causa")%>
                        </div>
                        <div style="width: auto; float: left; margin-left: 10px; margin-right: 25px;">
                            <input id="codCausaBaja" name="codCausaBaja" type="text" class="inputTexto" size="2" maxlength="8" 
                                   onkeypress="javascript:return SoloDigitosConsulta(event);" value="">
                            <input id="descCausaBaja" name="descCausaBaja" type="text" class="inputTexto" size="20" readonly onchange="checkDescSustituto();" >
                             <a id="anchorCausaBaja" name="anchorCausaBaja" href="" ><span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonCausaBaja" name="botonCausaBaja" style="cursor:hand;" alt="<%=meLanbide60I18n.getMensaje(idiomaUsuario, "altDesplegable")%>" title="<%=meLanbide60I18n.getMensaje(idiomaUsuario, "altDesplegable")%>" ></span></a>
                        </div>
                    </div>
                    <div class="lineaFormulario" id="divSustitucion">
                        <div style="float: left; width: 90px;">
                            <%=meLanbide60I18n.getMensaje(idiomaUsuario,"label.oferta.datosBaja.sustituye")%>
                        </div>
                        <div style="width: auto; float: left; margin-left: 10px;">
                            <div style="float: left;">
                                <input type="radio" name="radioSustituye" id="radioSustituyeS" value="S" onclick="cambioRadioSustituye(this);"><%=meLanbide60I18n.getMensaje(idiomaUsuario, "label.si")%>
                                <input type="radio" name="radioSustituye" id="radioSustituyeN" value="N" onclick="cambioRadioSustituye(this);"><%=meLanbide60I18n.getMensaje(idiomaUsuario, "label.no")%>
                            </div>
                        </div>
                        <div style="width: auto; float: left; margin-left: 10px;" id="divAltaSustituto" name="divAltaSustituto">
                            <input type="button" id="btnAltaSustituto" name="btnAltaSustituto" class="botonMasLargo" value="<%=meLanbide60I18n.getMensaje(idiomaUsuario,"btn.oferta.datosBaja.altaSustituto")%>" onclick="altaSustituto();">
                        </div>
                    </div>
                    <div class="lineaFormulario">      
                        <div style="float: left; width: 90px;">
                            <%=meLanbide60I18n.getMensaje(idiomaUsuario,"label.puesto.datosBaja.observaciones")%>
                        </div>
                        <div style="width: auto; float: left; margin-left: 10px;">
                            <textarea rows="4" cols="129" id="observacionesBaja" name="observacionesBaja" maxlength="1000" class="inputTexto"><%=ofertaModif != null && ofertaModif.getObservaciones() != null ? ofertaModif.getObservaciones().toUpperCase() : "" %></textarea>
                        </div>
                    </div>
                </fieldset>
                </div>
                <div class="botonera">
                    <input type="button" id="btnGuardarContrato" name="btnGuardarContrato" class="botonGeneral" value="<%=meLanbide60I18n.getMensaje(idiomaUsuario,"btn.aceptar")%>" onclick="guardar();">
                    <input type="button" id="btnCancelarContrato" name="btnCancelarContrato" class="botonGeneral" value="<%=meLanbide60I18n.getMensaje(idiomaUsuario,"btn.cancelar")%>" onclick="cancelar();">
                    <input type="button" id="btnCerrar" name="btnCerrar" class="botonGeneral" value="<%=meLanbide60I18n.getMensaje(idiomaUsuario,"btn.cerrar")%>" onclick="cerrarVentana();" style="display: none;">
                </div>
                <input type="hidden" id="codTitulacionPuesto1" name="codTitulacionPuesto1"/>
                <input type="hidden" id="codTitulacionPuesto2" name="codTitulacionPuesto2"/>
                <input type="hidden" id="codTitulacionPuesto3" name="codTitulacionPuesto3"/>
                <input type="hidden" id="codTitulacionContratado" name="codTitulacionContratado"/>
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
            
            //Datos oferta
            var comboOfiGestionOferta;
            var comboCausaRenuncia;
            var comboCausaRenunciaPresOferta;
            
            //Datos contratado
            var comboNifContratado;
            var comboGrupoCotContratado;
            
            //Datos baja
            var comboCausaBaja;
            inicio();
            
            //comboCausaBaja.change = checkDescSustituto;
            
            /*Cuando se clica en el calendario un dia, se comprueba si la fecha est� en blanco*/
            function recargarCamposCalculados(){
                if(document.getElementById('fechaBaja').value!='' && document.getElementById('radioSustituyeS').checked &&  document.getElementById('descCausaBaja').value!=''){
                    document.getElementById('btnAltaSustituto').style.display = 'inline';
                }else{
                    document.getElementById('btnAltaSustituto').style.display = 'none';
                }
                
            }
        </script>
        <iframe id="nuevaOfertaFrame" name="nuevaOfertaFrame" height="0" width="0" 
            frameborder="0" scrolling="yes"></iframe>
        <iframe id="copiarDatosPuestoFrame" name="copiarDatosPuestoFrame" height="0" width="0" 
            frameborder="0" scrolling="yes"><% out.println("puestoConsulta es null?:"+puestoConsulta);%></iframe>
    </body>
</html>

<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>

<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide59.i18n.MeLanbide59I18n" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide59.vo.CpePuestoVO" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide59.vo.CpeOfertaVO" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide59.vo.CpeJustificacionVO" %>
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
<html>
    <head> 
        <%
            int idiomaUsuario = 1;
            UsuarioValueObject usuario = new UsuarioValueObject();
            try
            {
                if (session != null) 
                {
                    if (usuario != null) 
                    {
                        usuario = (UsuarioValueObject) session.getAttribute("usuario");
                        idiomaUsuario = usuario.getIdioma();
                    }
                }
            }
            catch(Exception ex)
            {

            }
            //Clase para internacionalizar los mensajes de la aplicación.
//pruebas
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
            
            String descTitContratado = (String)request.getAttribute("descTitContratado");
            
            Boolean consulta = request.getAttribute("consulta") != null ? (Boolean)request.getAttribute("consulta") : false;
            SimpleDateFormat format = new SimpleDateFormat(ConstantesMeLanbide59.FORMATO_FECHA);

            String tituloPagina = "";
            if(consulta)
            {
                tituloPagina = meLanbide59I18n.getMensaje(idiomaUsuario,"label.justif.titulo.consultaJustif");
            }
            else
            {
                if(justifModif != null)
                {
                    tituloPagina = meLanbide59I18n.getMensaje(idiomaUsuario,"label.justif.titulo.modifJustif");
                }
                else
                {
                    tituloPagina = meLanbide59I18n.getMensaje(idiomaUsuario, "label.justif.titulo.nuevaJustif");
                }
            }
            
            //COMBOS

            // Grupo cotizacion
            List<SelectItem> listaGrupoCotizacion = new ArrayList<SelectItem>();
            if(request.getAttribute("listaGrupoCotizacion") != null)
                listaGrupoCotizacion = (List<SelectItem>)request.getAttribute("listaGrupoCotizacion");

            // Tipo documento
            List<SelectItem> listaNif = new ArrayList<SelectItem>();
            if(request.getAttribute("listaNif") != null)
                listaNif = (List<SelectItem>)request.getAttribute("listaNif");
            
            //Causa baja
            List<SelectItem> listaCausaBaja = new ArrayList<SelectItem>();
            if(request.getAttribute("listaCausaBaja") != null)
                listaCausaBaja = (List<SelectItem>)request.getAttribute("listaCausaBaja");
            
            String lcodGrupoCot = "";
            String ldescGrupoCot = "";
            
            String lcodNif = "";
            String ldescNif = "";
            
            String lcodCausaBaja = "";
            String ldescCausaBaja = "";

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


            <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/estilo.css"/>
            <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/extension/melanbide59/melanbide59.css"/>

            <script type="text/javascript" src="<c:url value='/scripts/general.js'/>"></script>
            <script type="text/javascript">
                var APP_CONTEXT_PATH = '<%=request.getContextPath()%>';
            </script>
            <script type="text/javascript" src="<c:url value='/scripts/calendario.js'/>"></script>
            <script type="text/javascript" src="<c:url value='/scripts/validaciones.js'/>"></script>
            <script type="text/javascript" src="<c:url value='/scripts/listaComboBox.js'/>"></script>
            <script type="text/javascript" src="<c:url value='/scripts/popup.js'/>"></script>
            <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/melanbide59/cpeUtils.js"></script>
        
        <script type="text/javascript">
            var mensajeValidacion = '';
            var nuevo = true;
    
            //LISTAS DE VALORES PARA LOS COMBOS
            
            var codNif = [<%=lcodNif%>];
            var descNif = [<%=ldescNif%>];
            
            var codGrupoCot = [<%=lcodGrupoCot%>];
            var descGrupoCot = [<%=ldescGrupoCot%>];
            
            var codCausaBaja = [<%=lcodCausaBaja%>];
            var descCausaBaja = [<%=ldescCausaBaja%>];
            
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
                    document.getElementById('descTitulacionContratado').value = '<%=descTitContratado != null ? descTitContratado.toUpperCase() : "" %>';
                    document.getElementById('anoTitulacion').value = '<%=ofertaConsulta.getAnoTitulacion() != null ? ofertaConsulta.getAnoTitulacion().toString() : "" %>';
                    document.getElementById('fechaIniContratado').value = '<%=ofertaConsulta.getFecIni() != null ? format.format(ofertaConsulta.getFecIni()) : "" %>';
                    document.getElementById('fechaFinContratado').value = '<%=ofertaConsulta.getFecFin() != null ? format.format(ofertaConsulta.getFecFin()) : "" %>';
                    document.getElementById('codGrupoCotContratado').value = '<%=ofertaConsulta.getCodGrCot() != null ? ofertaConsulta.getCodGrCot().toUpperCase() : "" %>';
                    document.getElementById('txtSalarioContratado').value = '<%=ofertaConsulta.getSalarioB() != null ? ofertaConsulta.getSalarioB().toPlainString() : "" %>';
                    document.getElementById('txtDietasConv').value = '<%=ofertaConsulta.getDietasConvenio() != null ? ofertaConsulta.getDietasConvenio().toPlainString() : "" %>';
                    document.getElementById('txtDietasConvoc').value = '<%=ofertaConsulta.getDietasConvoc() != null ? ofertaConsulta.getDietasConvoc().toPlainString() : "" %>';
                    document.getElementById('txtAportacion').value = '<%=justifModif != null && justifModif.getAportEpsv() != null ? justifModif.getAportEpsv() : ""%>';

                    reemplazarPuntosCpe(document.getElementById('txtSalarioContratado'));
                    reemplazarPuntosCpe(document.getElementById('txtDietasConv'));
                    reemplazarPuntosCpe(document.getElementById('txtDietasConvoc'));
                    
                    //Datos baja
                    
                    document.getElementById('fechaBaja').value = '<%=ofertaConsulta.getFecBaja() != null ? format.format(ofertaConsulta.getFecBaja()) : "" %>';
                    document.getElementById('codCausaBaja').value = '<%=ofertaConsulta.getCodCausaBaja() != null ? ofertaConsulta.getCodCausaBaja().toUpperCase() : "" %>';
                    
                    
                <%
                }
                %>
                        
                //Resumen
                document.getElementById('codEstado').value = '<%=justifModif != null && justifModif.getCodEstado() != null ? justifModif.getCodEstado() : ""%>';
                
                <%
                if(justifModif != null)
                {
                    if(justifModif.getFlVariosTrab() != null && justifModif.getFlVariosTrab().equalsIgnoreCase(ConstantesMeLanbide59.FALSO))
                    {
                %>
                        document.getElementById('radioVariosTrabN').checked = 'true';
                <%
                    }
                    else
                    {
                        if(justifModif.getFlVariosTrab() != null && justifModif.getFlVariosTrab().equalsIgnoreCase(ConstantesMeLanbide59.CIERTO))
                        {
                %>
                            document.getElementById('radioVariosTrabS').checked = 'true';
                <%
                        }
                    }
                }
                %>
                
                <%--MCG--%>
                document.getElementById('txtMinoracion').value = '<%=justifModif != null && justifModif.getMinoracion() != null ? justifModif.getMinoracion() : concedido != null ? concedido.toUpperCase() : "" %>';
                document.getElementById('txtNumDiasTrab').value = '<%=justifModif != null && justifModif.getDiasTrab() != null ? justifModif.getDiasTrab() : "0" %>';
                document.getElementById('txtNumDiasSeg').value = '<%=justifModif != null && justifModif.getDiasSegSoc() != null ? justifModif.getDiasSegSoc() : "0" %>';
                
                document.getElementById('txtBaseCC').value = '<%=justifModif != null && justifModif.getBaseCC() != null ? justifModif.getBaseCC() : concedido != null ? concedido.toUpperCase() : "" %>';
                document.getElementById('txtBaseAETP').value = '<%=justifModif != null && justifModif.getBaseAT() != null ? justifModif.getBaseAT() : concedido != null ? concedido.toUpperCase() : "" %>';
                document.getElementById('txtCoeficienteApli').value = '<%=justifModif != null && justifModif.getCoeficienteApli() != null ? justifModif.getCoeficienteApli() : concedido != null ? concedido.toUpperCase() : "" %>';
                document.getElementById('txtPorcFogasa').value = '<%=justifModif != null && justifModif.getPorcFogasa() != null ? justifModif.getPorcFogasa() : concedido != null ? concedido.toUpperCase() : "" %>';
                document.getElementById('txtPorcCoeficiente').value = '<%=justifModif != null && justifModif.getPorcCoeficiente() != null ? justifModif.getPorcCoeficiente() : concedido != null ? concedido.toUpperCase() : "" %>';
                document.getElementById('txtPorcAportacion').value = '<%=justifModif != null && justifModif.getPorcAportacion() != null ? justifModif.getPorcAportacion() : concedido != null ? concedido.toUpperCase() : "" %>';
                <%--MCG--%>
                //document.getElementById('txtSalario').value =  document.getElementById('txtBaseCC').value;
                
                document.getElementById('txtImpConcedido').value = '<%=concedido != null ? concedido.toUpperCase() : "" %>';
                document.getElementById('txtImpJustificado').value = '<%=justifModif != null && justifModif.getImpJustificado() != null ? justifModif.getImpJustificado() : concedido != null ? concedido.toUpperCase() : "" %>';
                document.getElementById('txtImpRenuncia').value = '<%=renunciado != null ? renunciado.toUpperCase() : "" %>';
                document.getElementById('txtSalarioSub').value = '<%=justifModif != null && justifModif.getSalarioSub() != null ? justifModif.getSalarioSub() : concedido != null ? concedido.toUpperCase() : "" %>';
                document.getElementById('txtDietasJusti').value = '<%=justifModif != null && justifModif.getGastosVisado() != null ? justifModif.getGastosVisado() : concedido != null ? concedido.toUpperCase() : "" %>';
                document.getElementById('txtMeses').value = '<%=justifModif != null && justifModif.getMesesExt() != null ? justifModif.getMesesExt() : concedido != null ? concedido.toUpperCase() : "" %>';
                document.getElementById('txtGastosGestion').value = '<%=justifModif != null && justifModif.getGastosSeguro() != null ? justifModif.getGastosSeguro() : concedido != null ? concedido.toUpperCase() : "" %>';
                document.getElementById('txtBonif').value = '<%=justifModif != null && justifModif.getBonif() != null ? justifModif.getBonif() : concedido != null ? concedido.toUpperCase() : "" %>';
                document.getElementById('txtSalario').value = '<%=justifModif != null && justifModif.getSalario() != null ? justifModif.getSalario() : concedido != null ? concedido.toUpperCase() : "" %>';
                if(<%=justifModif.getSalario()%> == null)
                    document.getElementById('txtSalario').value =  document.getElementById('txtBaseCC').value;
                reemplazarPuntosCpe(document.getElementById('txtImpConcedido'));
                reemplazarPuntosCpe(document.getElementById('txtImpRenuncia'));
                

                <%--MCG--%>
                reemplazarPuntosCpe(document.getElementById('txtMinoracion'));
                reemplazarPuntosCpe(document.getElementById('txtBaseCC'));
                reemplazarPuntosCpe(document.getElementById('txtCoeficienteApli'));
                reemplazarPuntosCpe(document.getElementById('txtBaseAETP'));
                reemplazarPuntosCpe(document.getElementById('txtPorcFogasa'));
                reemplazarPuntosCpe(document.getElementById('txtPorcCoeficiente'));
                reemplazarPuntosCpe(document.getElementById('txtPorcAportacion'));
                <%--MCG--%>
                    
                reemplazarPuntosCpe(document.getElementById('txtSalarioSub'));
                reemplazarPuntosCpe(document.getElementById('txtDietasJusti'));
                reemplazarPuntosCpe(document.getElementById('txtMeses'));
                reemplazarPuntosCpe(document.getElementById('txtGastosGestion'));
                reemplazarPuntosCpe(document.getElementById('txtBonif'));
                
                suma();
                        
                        
                <%
                if(consulta == true || (puestoConsulta != null && puestoConsulta.getCodResult() != null && puestoConsulta.getCodResult().equalsIgnoreCase(ConstantesMeLanbide59.CODIGO_RESULTADO_RENUNCIA)))
                {
                %>
                        //Deshabilito todos los campos
                        
                        
                        document.getElementById('txtImpJustificado').readOnly = true;
                        document.getElementById('txtImpJustificado').className = 'inputTexto readOnly textoNumerico';
                        document.getElementById('codEstado').readOnly = true;
                        document.getElementById('codEstado').className = 'inputTexto readOnly';
                        document.getElementById('descEstado').readOnly = true;
                        document.getElementById('descEstado').className = 'inputTexto readOnly';
                        document.getElementById('botonEstado').style.display = 'none';
                        document.getElementById('radioVariosTrabS').disabled = true;
                        document.getElementById('radioVariosTrabN').disabled = true;
                        
                        document.getElementById('btnGuardar').style.display = 'none';
                        document.getElementById('btnCancelar').style.display = 'none';
                        document.getElementById('btnCerrar').style.display = 'inline';
                <%
                }
                else
                {
                %>
                        document.getElementById('btnGuardar').style.display = 'inline';
                        document.getElementById('btnCancelar').style.display = 'inline';
                        document.getElementById('btnCerrar').style.display = 'none';
                <%
                }
                %>        
                //mostrarImporteRenuncia();
            }
            
            function suma(){
                var a1 = document.getElementById('txtBaseCC');
                var a2 = document.getElementById('txtCoeficienteApli');
                var a3 = document.getElementById('txtPorcFogasa');
                var a4 = document.getElementById('txtPorcCoeficiente');
                var a5 = document.getElementById('txtPorcAportacion');
                var a10 = document.getElementById('txtDietasJusti');
                var a11 = document.getElementById('txtGastosGestion');
                var a12 = document.getElementById('txtBonif');
                var a13 = document.getElementById('txtBaseAETP');
                var result = 0.0;
                var valor = 0.0;
                try{
                    if(a1.value != '' && validarNumericoDecimalCpe(a1, 50, 25)){
                        a1 = convertirANumero(a1.value);
                    } else {
                        a1=0;
                    }
                    if(a2.value != '' && validarNumericoDecimalCpe(a2, 50, 25)){
                        a2 = convertirANumero(a2.value);
                    } else {
                        a2=0;
                    }
                    if(a3.value != '' && validarNumericoDecimalCpe(a3, 50, 25)){
                        a3 = convertirANumero(a3.value);
                    } else {
                        a3=0;
                    }
                    if(a4.value != '' && validarNumericoDecimalCpe(a4, 50, 25)){
                        a4 = convertirANumero(a4.value);
                    } else {
                        a4=0;
                    }
                    if(a5.value != '' && validarNumericoDecimalCpe(a5, 50, 25)){
                        a5 = convertirANumero(a5.value);
                    } else {
                        a5=0;
                    }
                    if(a10.value != '' && validarNumericoDecimalCpe(a10, 50, 25)){
                        a10 = convertirANumero(a10.value);
                    } else {
                        a10=0;
                    }
                    if(a11.value != '' && validarNumericoDecimalCpe(a11, 50, 25)){
                        a11 = convertirANumero(a11.value);
                    } else {
                        a11=0;
                    }
                    if(a12.value != '' && validarNumericoDecimalCpe(a12, 50, 25)){
                        a12 = convertirANumero(a12.value);
                    } else {
                        a12=0;
                    }
                    if(a13.value != '' && validarNumericoDecimalCpe(a13, 50, 25)){
                        a13 = convertirANumero(a13.value);
                    } else {
                        a13=0;
                    }
                        
                    //SS a cargo empresa
                    valor = parseFloat(a1)*parseFloat(a2)/100;
                    result = parseFloat(valor);
                    result = result.toFixed(2);
                    document.getElementById('txtSegSocial').value = result;
                    reemplazarPuntosCpe(document.getElementById('txtSegSocial'));
                    var a8 = document.getElementById('txtSegSocial');
                    if(a8.value != '' && validarNumericoDecimalCpe(a8, 50, 25)){
                        a8 = convertirANumero(a8.value);
                    } else {
                        a8=0;
                    }
                
                    //Base AT/EP
                    valor = parseFloat(a1);
                    result = parseFloat(valor);
                    result = result.toFixed(2);
                    /*if(document.getElementById('txtBaseAETP').value == '')
                        document.getElementById('txtBaseAETP').value = result;*/
                    reemplazarPuntosCpe(document.getElementById('txtBaseAETP'));
                    
                    //Fogasa
                    valor = (parseFloat(a3)+parseFloat(a4))*parseFloat(a13)/100;
                        result = parseFloat(valor);
                    result = result.toFixed(2);
                    document.getElementById('txtFogasa').value = result;
                    reemplazarPuntosCpe(document.getElementById('txtFogasa'));
                    var a6 = document.getElementById('txtFogasa');
                    if(a6.value != '' && validarNumericoDecimalCpe(a6, 50, 25)){
                        a6 = convertirANumero(a6.value);
                    } else {
                        a6=0;
                    }
                    
                    //Aportacion EPSV
                    if(document.getElementById('txtAportacion').value == '' || document.getElementById('txtAportacion').value == '0,00')
                    {
                    valor = parseFloat(a1)*parseFloat(a5)/100;
                    result = parseFloat(valor);
                    result = result.toFixed(2);
                    document.getElementById('txtAportacion').value = result;
                    reemplazarPuntosCpe(document.getElementById('txtAportacion'));
}
                    var a7 = document.getElementById('txtAportacion');
                    if(a7.value != '' && validarNumericoDecimalCpe(a7, 50, 25)){
                        a7 = convertirANumero(a7.value);
                    } else {
                        a7=0;
                    }               

                    //COSTE SS
                    valor = parseFloat(a6)+parseFloat(a7)+parseFloat(a8);
                    result = parseFloat(valor);
                        result = result.toFixed(2);
                    document.getElementById('txtCosteCC').value = result;
                    reemplazarPuntosCpe(document.getElementById('txtCosteCC'));

                    
                    //COSTE TOTAL
                    /*valor = parseFloat(a1)+parseFloat(a6)+parseFloat(a7)+parseFloat(a8);
                    result = parseFloat(valor);
                    result = result.toFixed(2);
                    document.getElementById('txtCosteTotal').value = result;
                    reemplazarPuntosCpe(document.getElementById('txtCosteTotal'));
                    var a9 = document.getElementById('txtCosteTotal');
                    if(a9.value != '' && validarNumericoDecimalCpe(a9, 50, 25)){
                        a9 = convertirANumero(a9.value);
                    } else {
                        a9;
                    }*/
                    
                    
                    
                    
                    //meses exterior
                    valor = parseFloat(a10)/350;
                    result = parseFloat(valor);
                    result = result.toFixed(2); 
                    if(document.getElementById('txtMeses').value == "")
                        document.getElementById('txtMeses').value = result;
                    reemplazarPuntosCpe(document.getElementById('txtMeses'));
                    
                    //coste ss+at...
                    /*alert("paso 4");
                    var val1 = document.getElementById('txtAportacion');
                    var val2 = document.getElementById('txtFogasa');
                    var val3 = document.getElementById('txtSegSocial');
                    
                    if(val1 != '')
                        val1 = convertirANumero(val1.value);
                    else 
                        val1 = 0;
                    if(val2 != '')
                        val2 = convertirANumero(val2.value);
                    else 
                        val2 = 0;
                    if(val3 != '')
                        val3 = convertirANumero(val3.value);
                    else 
                        val3 = 0;
                    valor = parseFloat(val1) + parseFloat(val2) + parseFloat(val2);
                    document.getElementById('txtCosteCC').value = valor.toFixed(2);
                    alert("paso 3");
                    reemplazarPuntosCpe(document.getElementById('txtCosteCC'));*/
                    reemplazarPuntosCpe(document.getElementById('txtSalario'));
                    //COSTE TOTAL
                    var val4 = document.getElementById('txtSalario');
                    if(val4 != '')
                        val4 = convertirANumero(val4.value);
                    else 
                        val4 = 0;
                    var val5 = document.getElementById('txtCosteCC');
                    if(val5 != '')
                        val5 = convertirANumero(val5.value);
                    else 
                        val5 = 0;
                    valor = parseFloat(val5) + parseFloat(val4);
                    document.getElementById('txtCosteTotal').value = valor.toFixed(2);
                    reemplazarPuntosCpe(document.getElementById('txtCosteTotal'));
                    reemplazarPuntosCpe(document.getElementById('txtImpJustificado'));
                    
                    //IMPORTE JUSTIFICADO
                    val4 = document.getElementById('txtBonif').value;
                    if(val4 != '')
                        val4 = convertirANumero(val4);
                    else 
                        val4 = 0;
                    
                    val5 = document.getElementById('txtGastosGestion').value;
                    
                    if(val5 != '')
                        val5 = convertirANumero(val5);
                    else 
                        val5 = 0;
                    var val6 = document.getElementById('txtDietasJusti').value;
                    if(val6 != '')
                        val6 = convertirANumero(val6);
                    else 
                        val6 = 0;
                    
                    var val7 = document.getElementById('txtCosteTotal').value;
                    if(val7 != '')
                        val7 = convertirANumero(val7);
                    else 
                        val7 = 0;
                    valor = parseFloat(val5) + parseFloat(val6) + parseFloat(val7) - parseFloat(val4);
                    
                    document.getElementById('txtImpJustificado').value = valor.toFixed(2);
                    reemplazarPuntosCpe(document.getElementById('txtImpJustificado'));
                    
                }catch(err){

                }
            }
            
            function cargarDescripcionesCombos(){
                var desc = "";
                var codAct = "";
                var codigo = "";
                
                //Nif
                codigo = '<%=ofertaConsulta != null && ofertaConsulta.getCodTipoNif() != null ? ofertaConsulta.getCodTipoNif() : ""%>';
                desc = '';
                var encontrado = false;
                var i = 0; 
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
                
                //Causa baja
                codigo = '<%=ofertaConsulta != null && ofertaConsulta.getCodCausaBaja() != null ? ofertaConsulta.getCodCausaBaja() : ""%>';
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
                var resultado = jsp_alerta('','<%=meLanbide59I18n.getMensaje(idiomaUsuario, "msg.preguntaCancelar")%>');
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
                    var parametros = 'tarea=preparar&modulo=MELANBIDE59&operacion=guardarDatosContratacion&tipo=0&numero=<%=numExpediente%>'
                        +'&idJustif=<%=justifModif != null && justifModif.getIdJustificacion() != null ? justifModif.getIdJustificacion() : ""%>'
                        +'&idPuesto=<%=justifModif != null && justifModif.getCodPuesto() != null ? justifModif.getCodPuesto() : ""%>'    
                        +'&impJustificado='+document.getElementById('txtImpJustificado').value +'&salarioSub='+document.getElementById('txtSalarioSub').value
                        +'&gastosVisado='+document.getElementById('txtDietasJusti').value +'&gastosSeguro='+document.getElementById('txtGastosGestion').value
                        +'&bonif='+document.getElementById('txtBonif').value
                        +'&mesesExt='+document.getElementById('txtMeses').value
                    <%-- MCG --%>
                        +'&minoracion='+document.getElementById('txtMinoracion').value   
                        +'&diasTrab='+document.getElementById('txtNumDiasTrab').value    
                        +'&diasSegSoc='+document.getElementById('txtNumDiasSeg').value            
                        +'&salario='+document.getElementById('txtSalario').value                
                        +'&baseCC='+document.getElementById('txtBaseCC').value             
                        +'&baseAT='+document.getElementById('txtBaseAETP').value  
                        +'&coeficienteApli='+document.getElementById('txtCoeficienteApli').value            
                        +'&porcFogasa='+document.getElementById('txtPorcFogasa').value            
                        +'&porcCoeficiente='+document.getElementById('txtPorcCoeficiente').value            
                        +'&porcAportacion='+document.getElementById('txtPorcAportacion').value             
                        +'&aportacionEPSV='+document.getElementById('txtAportacion').value              
                    <%-- MCG --%>
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
                        var listaContrataciones = new Array();
                        var fila = new Array();
                        var nodoFila;
                        var hijosFila;
                        var nodoCampo;
                        var j;
                        
                        for(j=0;hijos!=null && j<hijos.length;j++){
                            if(hijos[j].nodeName=="CODIGO_OPERACION"){                            
                                codigoOperacion = hijos[j].childNodes[0].nodeValue;
                                listaContrataciones[j] = codigoOperacion;
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
                                    else if(hijosFila[cont].nodeName=="IMP_JUS2"){
                                        nodoCampo = hijosFila[cont];
                                        if(nodoCampo.childNodes.length > 0){
                                            fila[21] = nodoCampo.childNodes[0].nodeValue;
                                        }
                                        else{
                                            fila[21] = '0';
                                        }
                                    }
                                }
                                listaContrataciones[j] = fila;
                                fila = new Array();
                            }else if(hijos[j].nodeName=="FILA"){
                                nodoFila = hijos[j];
                                hijosFila = nodoFila.childNodes;
                                for(var cont = 0; cont < hijosFila.length; cont++){
                                    if(hijosFila[cont].nodeName=="ID_OFERTA"){
                                        nodoCampo = hijosFila[cont];
                                        if(nodoCampo.childNodes.length > 0){
                                            fila[0] = nodoCampo.childNodes[0].nodeValue;
                                        }
                                        else{
                                            fila[0] = '-';
                                        }
                                    }else if(hijosFila[cont].nodeName=="ID_JUSTIF"){
                                        nodoCampo = hijosFila[cont];
                                        if(nodoCampo.childNodes.length > 0){
                                            fila[1] = nodoCampo.childNodes[0].nodeValue;
                                        }
                                        else{
                                            fila[1] = '-';
                                        }
                                    }else if(hijosFila[cont].nodeName=="NIF"){
                                        nodoCampo = hijosFila[cont];
                                        if(nodoCampo.childNodes.length > 0){
                                            fila[2] = nodoCampo.childNodes[0].nodeValue;
                                        }
                                        else{
                                            fila[2] = '-';
                                        }
                                    }
                                    else if(hijosFila[cont].nodeName=="NOMAPEL"){
                                        nodoCampo = hijosFila[cont];
                                        if(nodoCampo.childNodes.length > 0){
                                            fila[3] = nodoCampo.childNodes[0].nodeValue;
                                        }
                                        else{
                                            fila[3] = '-';
                                        }
                                    }
                                    else if(hijosFila[cont].nodeName=="FE_DESDE"){
                                        nodoCampo = hijosFila[cont];
                                        if(nodoCampo.childNodes.length > 0){
                                            fila[4] = nodoCampo.childNodes[0].nodeValue;
                                        }
                                        else{
                                            fila[4] = '-';
                                        }
                                    }
                                    else if(hijosFila[cont].nodeName=="FE_HASTA"){
                                        nodoCampo = hijosFila[cont];
                                        if(nodoCampo.childNodes.length > 0){
                                            fila[5] = nodoCampo.childNodes[0].nodeValue;
                                        }
                                        else{
                                            fila[5] = '-';
                                        }
                                    }
                                    else if(hijosFila[cont].nodeName=="IMP_JUSTIF"){
                                        nodoCampo = hijosFila[cont];
                                        if(nodoCampo.childNodes.length > 0){
                                            fila[6] = nodoCampo.childNodes[0].nodeValue;
                                        }
                                        else{
                                            fila[6] = '-';
                                        }
                                    }
                                }
                                listaContrataciones[j] = fila;
                                fila = new Array();
                            }   
                        }//for(j=0;hijos!=null && j<hijos.length;j++)
                        if(codigoOperacion=="0"){
                            window.returnValue =  listaContrataciones;
                            barraProgresoCpe('on', 'barraProgresoNuevaJustificacion');
                            jsp_alerta("A",'<%=meLanbide59I18n.getMensaje(idiomaUsuario,"msg.datosGuardadosOK")%>');
                            cerrarVentana();
                        }else if(codigoOperacion=="1"){
                            jsp_alerta("A",'<%=meLanbide59I18n.getMensaje(idiomaUsuario,"error.errorBD")%>');
                        }else if(codigoOperacion=="2"){
                            jsp_alerta("A",'<%=meLanbide59I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                        }else if(codigoOperacion=="3"){
                            jsp_alerta("A",'<%=meLanbide59I18n.getMensaje(idiomaUsuario,"error.pasoParametros")%>');
                        }else if(codigoOperacion=="4"){
                            jsp_alerta("A",'<%=meLanbide59I18n.getMensaje(idiomaUsuario,"error.datosNoValidos")%>');
                        }else{
                            jsp_alerta("A",'<%=meLanbide59I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                        }//if(
                    }catch(Err){
                        jsp_alerta("A",'<%=meLanbide59I18n.getMensaje(idiomaUsuario,"error.errorProcesarRespuesta")%>');
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
                                    mensajeValidacion = '<%=meLanbide59I18n.getMensaje(idiomaUsuario, "msg.justificacion.resumen.impJustificadoIncorrecto")%>';
                            }else{
                                document.getElementById('txtImpJustificado').removeAttribute('style');
                            }
                        }catch(err){
                            correcto = false;
                            document.getElementById('txtImpJustificado').style.border = '1px solid red';
                            if(mensajeValidacion == '')
                                mensajeValidacion = '<%=meLanbide59I18n.getMensaje(idiomaUsuario, "msg.justificacion.resumen.impJustificadoIncorrecto")%>';
                        }
                    }
                }
                catch(err)
                {
                    correcto = false; 
                    if(mensajeValidacion == ''){
                        mensajeValidacion = '<%=meLanbide59I18n.getMensaje(idiomaUsuario, "msg.errorValidarDatos")%>';
                    }
                }
                return correcto;
            }
            
            function mostrarImporteRenuncia(){
                var visible = false;
                var estadoSelec = document.getElementById('codEstado').value;
                if(estadoSelec != undefined && estadoSelec != ''){
                    var codigoEstRenuncia = '<%=ConstantesMeLanbide59.CODIGO_ESTADO_RENUNCIA%>';
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
            
            function ajustarDecimalesImportes(){
                var f;
                var v;
                //Salario bruto
                v = document.getElementById('txtSalarioContratado').value;
                vAnt = v;
                v = reemplazarTextoCpe(v, /,/g, '.');
                f = parseFloat(v);
                f = ajustarDecimalesCpe(f, 2);
                if(!isNaN(f)){
                    document.getElementById('txtSalarioContratado').value = f;
                    reemplazarPuntosCpe(document.getElementById('txtSalarioContratado'));
                    document.getElementById('txtSalarioContratado').removeAttribute("style");
                }
                
                //Dietas según convenio
                v = document.getElementById('txtDietasConv').value;
                v = reemplazarTextoCpe(v, /,/g, '.');
                f = parseFloat(v);
                f = ajustarDecimalesCpe(f, 2);
                if(!isNaN(f)){
                    document.getElementById('txtDietasConv').value = f;
                    reemplazarPuntosCpe(document.getElementById('txtDietasConv'));
                    document.getElementById('txtDietasConv').removeAttribute("style");
                }
                
                //Dietas convocatoria
                v = document.getElementById('txtDietasConvoc').value;
                v = reemplazarTextoCpe(v, /,/g, '.');
                f = parseFloat(v);
                f = ajustarDecimalesCpe(f, 2);
                if(!isNaN(f)){
                    document.getElementById('txtDietasConvoc').value = f;
                    reemplazarPuntosCpe(document.getElementById('txtDietasConvoc'));
                    document.getElementById('txtDietasConvoc').removeAttribute("style");
                }
                
                //Importe concedido
                v = document.getElementById('txtImpConcedido').value;
                v = reemplazarTextoCpe(v, /,/g, '.');
                f = parseFloat(v);
                f = ajustarDecimalesCpe(f, 2);
                if(!isNaN(f)){
                    document.getElementById('txtImpConcedido').value = f;
                    reemplazarPuntosCpe(document.getElementById('txtImpConcedido'));
                    document.getElementById('txtImpConcedido').removeAttribute("style");
                }
                
                //Importe justificado
                v = document.getElementById('txtImpJustificado').value;
                v = reemplazarTextoCpe(v, /,/g, '.');
                f = parseFloat(v);
                f = ajustarDecimalesCpe(f, 2);
                if(!isNaN(f)){
                    document.getElementById('txtImpJustificado').value = f;
                    reemplazarPuntosCpe(document.getElementById('txtImpJustificado'));
                    document.getElementById('txtImpJustificado').removeAttribute("style");
                }
                
                //Importe renuncia
                v = document.getElementById('txtImpRenuncia').value;
                v = reemplazarTextoCpe(v, /,/g, '.');
                f = parseFloat(v);
                f = ajustarDecimalesCpe(f, 2);
                if(!isNaN(f)){
                    document.getElementById('txtImpRenuncia').value = f;
                    reemplazarPuntosCpe(document.getElementById('txtImpRenuncia'));
                    document.getElementById('txtImpRenuncia').removeAttribute("style");
                }
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
                    <legend class="legendAzul"><%=meLanbide59I18n.getMensaje(idiomaUsuario,"legend.oferta.datosContratado")%></legend>
                    <div class="lineaFormulario">     
                        <div style="float: left; width: 90px;">
                            <%=meLanbide59I18n.getMensaje(idiomaUsuario,"label.oferta.datosContratado.nif")%>
                        </div>
                        <div style="width: auto; float: left; margin-left: 10px;">
                            <input id="codNifContratado" name="codNifContratado" type="text" class="inputTexto readOnly" size="2" maxlength="3" 
                                   onkeypress="javascript:return SoloDigitosConsulta(event);" value="" readonly="true">
                            <input id="descNifContratado" name="descNifContratado" type="text" class="inputTexto readOnly" size="24" readonly >
                        </div>
                        <div style="width: auto; float: left; margin-left: 11px;">
                            <input type="text" class="inputTexto readOnly" maxlength="15" size="10" id="txtNifOferta" name="txtNifOferta" value="" readOnly="true"/>
                        </div>
                    </div>
                    <div class="lineaFormulario"> 
                        <div style="float: left; width: 90px;">
                            <%=meLanbide59I18n.getMensaje(idiomaUsuario,"label.oferta.datosContratado.nombre")%>
                        </div>
                        <div style="width: auto; float: left; margin-left: 10px;">
                            <input type="text" class="inputTexto readOnly" maxlength="200" size="30" id="txtNombre" name="txtNombre" value="" readonly="true"/>
                        </div>
                        <div style="float: left; width: 80px; margin-left: 20px;">
                            <%=meLanbide59I18n.getMensaje(idiomaUsuario,"label.oferta.datosContratado.ape1")%>
                        </div>
                        <div style="width: auto; float: left; margin-left: 5px;">
                            <input type="text" class="inputTexto readOnly" maxlength="200" size="30" id="txtApe1" name="txtApe1" value="" readonly="true"/>
                        </div>
                        <div style="float: left; width: 80px; margin-left: 20px;">
                            <%=meLanbide59I18n.getMensaje(idiomaUsuario,"label.oferta.datosContratado.ape2")%>
                        </div>
                        <div style="width: auto; float: left; margin-left: 5px;">
                            <input type="text" class="inputTexto readOnly" maxlength="200" size="30" id="txtApe2" name="txtApe2" value="" readonly="true"/>
                        </div>
                    </div>
                    <div class="lineaFormulario"> 
                        <div style="float: left; width: 90px;">
                            <%=meLanbide59I18n.getMensaje(idiomaUsuario,"label.oferta.datosContratado.email")%>
                        </div>
                        <div style="width: auto; float: left; margin-left: 10px;">
                            <input type="text" class="inputTexto readOnly" maxlength="200" size="30" id="txtEmail" name="txtEmail" value="" readonly="true"/>
                        </div>
                        <div style="float: left; width: 80px; margin-left: 20px;">
                            <%=meLanbide59I18n.getMensaje(idiomaUsuario,"label.oferta.datosContratado.tfno")%>
                        </div>
                        <div style="width: auto; float: left; margin-left: 5px;">
                            <input type="text" class="inputTexto readOnly" maxlength="15" size="15" id="txtTfno" name="txtTfno" value="" readonly="true"/>
                        </div>
                    </div>
                    <div class="lineaFormulario">
                        <div style="float: left; width: 90px;">
                            <%=meLanbide59I18n.getMensaje(idiomaUsuario,"label.oferta.datosContratado.fechaNac")%>
                        </div>
                        <div style="width: auto; float: left; margin-left: 10px;">
                            <input type="text" class="inputTxtFecha readOnly" size="10" maxlength="10" id="fechaNacContratado" name="fechaNacContratado" value="" readonly="true"/>
                        </div>
                        <div style="float: left; width: 80px; margin-left: 142px;">
                            <%=meLanbide59I18n.getMensaje(idiomaUsuario,"label.oferta.datosContratado.sexo")%>
                        </div>
                        <div style="width: auto; float: left; margin-left: 4px;">
                            <input type="text" class="inputTexto readOnly" maxlength="1" size="3" id="txtSexo" name="txtSexo" value="" readonly="true"/>
                        </div>
                    </div>
                    <div class="lineaFormulario">
                        <div style="float: left; width: 90px;">
                            <%=meLanbide59I18n.getMensaje(idiomaUsuario,"label.oferta.datosContratado.titulacion")%>
                        </div>
                        <div style="width: auto; float: left; margin-left: 10px;">
                            <input id="descTitulacionContratado" name="descTitulacionContratado" type="text" class="inputTexto readOnly" size="75" readonly >
                        </div>
                        <div style="float: left; width: 67px; margin-left: 23px;">
                            <%=meLanbide59I18n.getMensaje(idiomaUsuario,"label.oferta.datosContratado.anoTitulacion")%>
                        </div>
                        <div style="width: auto; float: left; margin-left: 4px;">
                            <input type="text" class="inputTexto readOnly" maxlength="4" size="3" id="anoTitulacion" name="anoTitulacion" value="" readonly="true"/>
                        </div>
                    </div>
                    <!--En teoria desaparece, pero por si acaso de momento se comenta-->
                    <!--
                    <div class="lineaFormulario" display="none">
                        <div style="float: left; width: 765px;">
                            <%=meLanbide59I18n.getMensaje(idiomaUsuario,"label.oferta.datosContratado.docContratacion")%>:&nbsp;
                            <input type="button" id="btnVerDocContratacion" name="btnDocContratacion" class="botonGeneral" value="<%=meLanbide59I18n.getMensaje(idiomaUsuario,"btn.ver")%>" onclick="verDocumento('contratacionOferta');">
                        </div>
                    </div>
                    -->
                    <div class="lineaFormulario">
                        <div style="float: left; width: 90px;">
                            <%=meLanbide59I18n.getMensaje(idiomaUsuario,"label.oferta.datosContratado.feIni")%>
                        </div>
                        <div style="width: auto; float: left; margin-left: 10px;">
                            <input type="text" class="inputTxtFecha readOnly" size="10" maxlength="10" id="fechaIniContratado" name="fechaIniContratado" readonly="true"/>
                        </div>
                        <div style="float: left; width: 80px; margin-left: 143px;">
                            <%=meLanbide59I18n.getMensaje(idiomaUsuario,"label.oferta.datosContratado.feFin")%>
                        </div>
                        <div style="width: auto; float: left; margin-left: 3px;">
                            <input type="text" class="inputTxtFecha readOnly" size="10" maxlength="10" id="fechaFinContratado" name="fechaFinContratado" readonly="true"/>
                        </div>
                    </div>
                    <div class="lineaFormulario">
                        <div style="float: left; width: 90px;">
                            <%=meLanbide59I18n.getMensaje(idiomaUsuario,"label.oferta.datosContratado.grCot")%>
                        </div>
                        <div style="width: auto; float: left; margin-left: 10px; margin-right: 22px;">
                            <input id="codGrupoCotContratado" name="codGrupoCotContratado" type="text" class="inputTexto readOnly" size="2" maxlength="3" 
                                    value="" readonly="true">
                            <input id="descGrupoCotContratado" name="descGrupoCotContratado" type="text" class="inputTexto readOnly" size="24" readonly >
                        </div>
                    </div>
                    <div class="lineaFormulario">
                        <div style="float: left; width: 90px;">
                            <%=meLanbide59I18n.getMensaje(idiomaUsuario,"label.oferta.datosContratado.salarioB")%>
                        </div>
                        <div style="width: auto; float: left; margin-left: 10px;">
                            <input type="text" maxlength="11" size="9" id="txtSalarioContratado" name="txtSalarioContratado" value="" class="inputTexto readOnly textoNumerico" readonly="true"/>
                        </div>
                        <div style="width: auto; float: left; margin-left: 10px;">
                            <input type="text" maxlength="11" size="8" id="txtDietasConv" name="txtDietasConv" style="display:none" value="" class="inputTexto readOnly textoNumerico" readonly="true"/>
                        </div>
                        <div style="width: auto; float: left; margin-left: 10px;">
                            <input type="text" maxlength="11" size="8" id="txtDietasConvoc" name="txtDietasConvoc" value="" style="display:none" class="inputTexto readOnly textoNumerico" readonly="true"/>
                        </div>
                    </div>
                    <div class="lineaFormulario">
                        <div style="float: left; width: 90px;">
                            <%=meLanbide59I18n.getMensaje(idiomaUsuario,"label.justif.personaContratada.datosBaja.fecha")%>
                        </div>
                        <div style="width: auto; float: left; margin-left: 10px;">
                            <input type="text" size="9" maxlength="10" id="fechaBaja" name="fechaBaja" class="inputTexto readOnly" readonly="true"/>
                        </div>
                        <div style="float: left; width: 90px; margin-left: 20px;">
                            <%=meLanbide59I18n.getMensaje(idiomaUsuario,"label.justif.personaContratada.datosBaja.causa")%>
                        </div>
                        <div style="width: auto; float: left; margin-left: 10px; margin-right: 25px;">
                            <input id="codCausaBaja" name="codCausaBaja" type="text" class="inputTexto readOnly" readonly="true" size="2" maxlength="8">
                            <input id="descCausaBaja" name="descCausaBaja" type="text" class="inputTexto readOnly" readonly="true" size="24">
                        </div>
                    </div>
                </fieldset>
                <fieldset  style="width: 100%;">
                    <legend class="legendAzul"><%=meLanbide59I18n.getMensaje(idiomaUsuario,"legend.justif.datosJustificacion")%></legend>
                    <!-- En teoria desaparece, pero por si acaso, en lugar de quitarlo se oculta -->
                    <div class="lineaFormulario" style="display: none;">
                        <div style="float: left; width: 245px;">
                            <%=meLanbide59I18n.getMensaje(idiomaUsuario,"label.justificacion.datosResumen.variosTrab")%>
                        </div>
                        <div style="width: auto; float: left; margin-left: 10px;">
                            <div style="float: left;">
                                <input type="radio" name="radioVariosTrab" id="radioVariosTrabS" value="S"><%=meLanbide59I18n.getMensaje(idiomaUsuario, "label.si")%>
                                <input type="radio" name="radioVariosTrab" id="radioVariosTrabN" value="N"><%=meLanbide59I18n.getMensaje(idiomaUsuario, "label.no")%>
                            </div>
                        </div>
                    </div>
                    <!-- En teoria desaparece, pero por si acaso, en lugar de quitarlo se oculta -->
                    <hr style="width: 98%; float: left; margin-top: 5px; margin-bottom: 5px; display: none;"/>
                    <div class="lineaFormulario">   
                        <!-- En teoria desaparece, pero por si acaso, en lugar de quitarlo se oculta -->
                        <div style="float: left; width: 150px; display: none;">
                            <%=meLanbide59I18n.getMensaje(idiomaUsuario,"label.justificacion.datosResumen.impConcedido")%>
                        </div>
                        
                        
                        <div style="width: auto; float: left; margin-left: 10px; display: none;">
                            <input type="text" maxlength="11" size="8" id="txtImpConcedido" name="txtImpConcedido" value="" class="inputTexto readOnly textoNumerico" readonly="true"/>
                        </div>
                        
                        
                        
                        <!-- MCG -->
                        <div style="float: left; width: 20%;">
                            <%=meLanbide59I18n.getMensaje(idiomaUsuario,"label.justificacion.datosResumen.minoracion")%>
                        </div>                             
                        <div style="width: auto; float: left; margin-left: 5px; margin-right: 7px;">
                            <input type="text" maxlength="11" size="8" id="txtMinoracion" name="txtMinoracion" value="" class="inputTexto textoNumerico" onchange="reemplazarPuntosCpe(this);suma();" onblur="reemplazarPuntosCpe(this); ajustarDecimalesImportes();"/>
                        </div>  
                        
                        <div style="float: left; width: 20%;">
                            <%=meLanbide59I18n.getMensaje(idiomaUsuario,"label.justificacion.datosResumen.diasTrabajados")%>
                        </div>                         
                        <div style="width: auto; float: left; margin-left: 5px; margin-right: 7px;">
                            <input type="text" maxlength="3" size="8" id="txtNumDiasTrab" name="txtNumDiasTrab" value="" class="inputTexto textoNumerico" onchange="reemplazarPuntosCpe(this);;" onblur="reemplazarPuntosCpe(this);"/>
                        </div>  
                        
                        <div style="float: left; width: 30%;">
                            <%=meLanbide59I18n.getMensaje(idiomaUsuario,"label.justificacion.datosResumen.diasSegSoc")%>
                        </div>  
                        <div style="width: auto; float: left; margin-left: 5px; margin-right: 7px;">
                            <input type="text" maxlength="3" size="8" id="txtNumDiasSeg" name="txtNumDiasSeg" value="" class="inputTexto textoNumerico" onchange="reemplazarPuntosCpe(this);;" onblur="reemplazarPuntosCpe(this); "/>
                        </div>
                        <div style="clear:both;"><br /></div>
                        
                        <div style="float: left; width: 20%;">
                            <%=meLanbide59I18n.getMensaje(idiomaUsuario,"label.justificacion.datosResumen.baseCotizacionCC")%>
                        </div>                             
                        <div style="width: auto; float: left; margin-left: 5px; margin-right: 7px;">
                            <input type="text" maxlength="11" size="8" id="txtBaseCC" name="txtBaseCC" value="" class="inputTexto textoNumerico" onchange="reemplazarPuntosCpe(this);suma();" onblur="reemplazarPuntosCpe(this); ajustarDecimalesImportes();"/>
                        </div>
                        <div style="float: left; width: 20%;">
                            <%=meLanbide59I18n.getMensaje(idiomaUsuario,"label.justificacion.datosResumen.coeficienteAplicado")%>
                        </div>                             
                        <div style="width: auto; float: left; margin-left: 5px; margin-right: 7px;">
                            <input type="text" maxlength="11" size="8" id="txtCoeficienteApli" name="txtCoeficienteApli" value="" class="inputTexto textoNumerico" onchange="reemplazarPuntosCpe(this);suma();" onblur="reemplazarPuntosCpe(this); ajustarDecimalesImportes();"/>
                        </div>
                        <div style="float: left; width: 30%;">
                            <%=meLanbide59I18n.getMensaje(idiomaUsuario,"label.justificacion.datosResumen.segSocialEmpresa")%>
                        </div>                             
                        <div style="width: auto; float: left; margin-left: 5px; margin-right: 7px;">
                            <input type="text" maxlength="11" size="8" id="txtSegSocial" name="txtSegSocial" value="" class="inputTexto readOnly" readonly="true" onchange="reemplazarPuntosCpe(this);suma();" onblur="reemplazarPuntosCpe(this); ajustarDecimalesImportes();"/>
                        </div>
                        <div style="clear:both;"><br /></div>
                        <div style="float: left; width: 20%;">
                            <%=meLanbide59I18n.getMensaje(idiomaUsuario,"label.justificacion.datosResumen.salario")%>
                        </div>                             
                        <div style="width: auto; float: left; margin-left: 5px; margin-right: 7px;">
                            <input type="text" maxlength="11" size="8" id="txtSalario" name="txtSalario" value="" class="inputTexto textoNumerico" onchange="reemplazarPuntosCpe(this);suma();" onblur="reemplazarPuntosCpe(this); ajustarDecimalesImportes();"/>
                        </div>
                        <div style="clear:both;"><br /></div>

                        <div style="float: left; width: 20%;">
                            <%=meLanbide59I18n.getMensaje(idiomaUsuario,"label.justificacion.datosResumen.baseCotizacionATEP")%>
                        </div>                             
                        <div style="width: auto; float: left; margin-left: 5px; margin-right: 7px;">
                            <input type="text" maxlength="11" size="8" id="txtBaseAETP" name="txtBaseAETP" value="" class="inputTexto textoNumerico" onchange="reemplazarPuntosCpe(this);suma();" onblur="reemplazarPuntosCpe(this); ajustarDecimalesImportes();"/>
                        </div>
<div style="clear:both;"><br /></div>
                        
                        <div style="float: left; width: 20%;">
                            <%=meLanbide59I18n.getMensaje(idiomaUsuario,"label.justificacion.datosResumen.porcFogasa")%>
                        </div>                             
                        <div style="width: auto; float: left; margin-left: 5px; margin-right: 7px;">
                            <input type="text" maxlength="11" size="8" id="txtPorcFogasa" name="txtPorcFogasa" value="" class="inputTexto textoNumerico" onchange="reemplazarPuntosCpe(this);suma();" onblur="reemplazarPuntosCpe(this); ajustarDecimalesImportes();"/>
                        </div>
<div style="clear:both;"><br /></div>
                        <div style="float: left; width: 20%;">
                            <%=meLanbide59I18n.getMensaje(idiomaUsuario,"label.justificacion.datosResumen.porcCoeficiente")%>
                        </div>                             
                        <div style="width: auto; float: left; margin-left: 5px; margin-right: 7px;">
                            <input type="text" maxlength="11" size="8" id="txtPorcCoeficiente" name="txtPorcCoeficiente" value="" class="inputTexto textoNumerico" onchange="reemplazarPuntosCpe(this);suma();" onblur="reemplazarPuntosCpe(this); ajustarDecimalesImportes();"/>
                        </div>
                        <div style="float: left; width: 29%;">&nbsp;</div>
                        <div style="float: left; width: 30%;">
                            <%=meLanbide59I18n.getMensaje(idiomaUsuario,"label.justificacion.datosResumen.fogasa")%>
                        </div>                             
                        <div style="width: auto; float: left; margin-left: 5px; margin-right: 7px;">
                            <input type="text" maxlength="11" size="8" id="txtFogasa" name="txtFogasa" value="" class="inputTexto readOnly" readonly="true" onchange="reemplazarPuntosCpe(this);suma();" onblur="reemplazarPuntosCpe(this); ajustarDecimalesImportes();"/>
                        </div>
<div style="clear:both;"><br /></div>
                        <div style="float: left; width: 20%;">
                            <%=meLanbide59I18n.getMensaje(idiomaUsuario,"label.justificacion.datosResumen.pocAportacion")%>
                        </div>                             
                        <div style="width: auto; float: left; margin-left: 5px; margin-right: 7px;">
                            <input type="text" maxlength="11" size="8" id="txtPorcAportacion" name="txtPorcAportacion" value="" class="inputTexto textoNumerico" onchange="reemplazarPuntosCpe(this);suma();" onblur="reemplazarPuntosCpe(this); ajustarDecimalesImportes();"/>
                        </div>
                        <div style="float: left; width: 29%;">&nbsp;</div>
                        <div style="float: left; width: 30%;">
                            <%=meLanbide59I18n.getMensaje(idiomaUsuario,"label.justificacion.datosResumen.aportacion")%>
                        </div>                             
                        <div style="width: auto; float: left; margin-left: 5px; margin-right: 7px;">
                            <input type="text" maxlength="11" size="8" id="txtAportacion" name="txtAportacion" value="" class="inputTexto" onchange="reemplazarPuntosCpe(this);suma();" onblur="reemplazarPuntosCpe(this); ajustarDecimalesImportes();"/>
                        </div>
<div style="clear:both;"><br /></div>
                        <div style="float: left; width: 20%;">
                            <%=meLanbide59I18n.getMensaje(idiomaUsuario,"label.justificacion.datosResumen.costeSS")%>
                        </div>                             
                        <div style="width: auto; float: left; margin-left: 5px; margin-right: 7px;">
                            <input type="text" maxlength="11" size="8" id="txtCosteCC" name="txtCosteCC" value="" class="inputTexto readOnly" readonly="true" onchange="reemplazarPuntosCpe(this);" onblur="reemplazarPuntosCpe(this); ajustarDecimalesImportes();"/>
                        </div>
                        <div style="float: left; width: 29%;">&nbsp;</div>
                        <div style="float: left; width: 30%;">
                            <%=meLanbide59I18n.getMensaje(idiomaUsuario,"label.justificacion.datosResumen.costeTotal")%>
                        </div>                             
                        <div style="width: auto; float: left; margin-left: 5px; margin-right: 7px;">
                            <input type="text" maxlength="11" size="8" id="txtCosteTotal" name="txtCosteTotal" value="" class="inputTexto readOnly" readonly="true" onchange="reemplazarPuntosCpe(this);" onblur="reemplazarPuntosCpe(this); ajustarDecimalesImportes();"/>
                        </div>
<div style="clear:both;"><br /></div>
                        <div style="float: left; width: 20%;">
                            <%=meLanbide59I18n.getMensaje(idiomaUsuario,"label.justificacion.datosResumen.dietasAbonadas")%>
                        </div>     
                        <div style="width: auto; float: left; margin-left: 5px; margin-right: 7px;">
                            <input type="text" maxlength="11" size="8" id="txtDietasJusti" name="txtDietasJusti" value="" class="inputTexto textoNumerico" onchange="reemplazarPuntosCpe(this);suma();" onblur="reemplazarPuntosCpe(this); ajustarDecimalesImportes();"/>
                        </div>
                        <div style="float: left; width: 10%;">&nbsp;</div>
                        <div style="float: left; width: 20%;">
                            <%=meLanbide59I18n.getMensaje(idiomaUsuario,"label.justificacion.datosResumen.mesesExterior")%>
                        </div>     
                        <div style="width: auto; float: left; margin-left: 5px; margin-right: 7px;">
                            <input type="text" maxlength="11" size="8" id="txtMeses" name="txtMeses" value="" class="inputTexto textoNumerico" onchange="reemplazarPuntosCpe(this);" onblur="reemplazarPuntosCpe(this); ajustarDecimalesImportes();"/>
                        </div>
<div style="clear:both;"><br /></div>
                        <div style="float: left; width: 20%;">
                            <%=meLanbide59I18n.getMensaje(idiomaUsuario,"label.justificacion.datosResumen.gastosAbonados")%>
                        </div>                             
                        <div style="width: auto; float: left; margin-left: 5px; margin-right: 7px;">
                            <input type="text" maxlength="11" size="8" id="txtGastosGestion" name="txtGastosGestion" value="" class="inputTexto textoNumerico" onchange="reemplazarPuntosCpe(this);suma();" onblur="reemplazarPuntosCpe(this); ajustarDecimalesImportes();"/>
                        </div>
<div style="clear:both;"><br /></div>
                        
                        
                        <div style="float: left; width: 20%;">
                            <%=meLanbide59I18n.getMensaje(idiomaUsuario,"label.justificacion.datosResumen.importeBonificado")%>
                        </div>     
                        <div style="width: auto; float: left; margin-left: 5px; margin-right: 7px;">
                            <input type="text" maxlength="11" size="8" id="txtBonif" name="txtBonif" value="" class="inputTexto textoNumerico" onchange="reemplazarPuntosCpe(this);suma();" onblur="reemplazarPuntosCpe(this); ajustarDecimalesImportes();"/>
                        </div>
                        <div style="float: left; width: 10%;">&nbsp;</div>
                        <div style="float: left; width: 20%;">
                            <%=meLanbide59I18n.getMensaje(idiomaUsuario,"label.justificacion.datosResumen.importeJustificado")%>
                        </div>     
                        <div style="width: auto; float: left; margin-left: 5px; margin-right: 7px;">
                            <input type="text" maxlength="11" size="8" id="txtImpJustificado" name="txtImpJustificado" value="" class="inputTexto readOnly" readonly="true" onchange="reemplazarPuntosCpe(this);" onblur="reemplazarPuntosCpe(this); ajustarDecimalesImportes();"/>
                        </div>
<div style="clear:both;"><br /></div>
                        
                        <!-- MCG -->
                        
                        
                        
                        
                        <div style="float: left; width: 13%;display: none;">
                            <%=meLanbide59I18n.getMensaje(idiomaUsuario,"label.justificacion.datosResumen.salarioSub")%>
                        </div>                        
                        <div style="width: auto; float: left; margin-left: 5px; margin-right: 7px;display: none;">
                            <input type="text" maxlength="11" size="8" id="txtSalarioSub" name="txtSalarioSub" value="" class="inputTexto textoNumerico" onchange="reemplazarPuntosCpe(this);suma();" onblur="reemplazarPuntosCpe(this); ajustarDecimalesImportes();"/>
                        </div>
                        
                        <!-- En teoria desaparece, pero por si acaso, en lugar de quitarlo se oculta -->
                        <div id="divImporteRenuncia" style="display: none;">
                            <div style="float: left; width: 150px; margin-left: 20px;">
                                <%=meLanbide59I18n.getMensaje(idiomaUsuario,"label.justificacion.datosResumen.impRenuncia")%>
                            </div>
                            <div style="width: auto; float: left; margin-left: 10px;">
                                <input type="text" maxlength="11" size="8" id="txtImpRenuncia" name="txtImpRenuncia" value="" class="inputTexto readOnly textoNumerico" readonly="true"/>
                            </div>
                        </div>
                    </div>
                    <!-- En teoria desaparece, pero por si acaso, en lugar de quitarlo se oculta -->
                    <div class="lineaFormulario" style="display: none;">
                        <div style="float: left; width: 90px;">
                            <%=meLanbide59I18n.getMensaje(idiomaUsuario,"label.justificacion.datosResumen.estado")%>
                        </div>
                        <div style="width: auto; float: left; margin-left: 10px;">
                            <input id="codEstado" name="codEstado" type="text" class="inputTexto" size="2" maxlength="8" 
                                   onkeypress="javascript:return SoloDigitosConsulta(event);" value="">
                            <input id="descEstado" name="descEstado" type="text" class="inputTexto" size="20" readonly >
                            <a id="anchorEstado" name="anchorEstado" href=""><img src="<%=request.getContextPath()%>/images/listas/botondesplegable.gif" id="botonEstado" name="botonEstado" border="0" width="14" height="14" style="cursor:hand;" alt="<%=meLanbide59I18n.getMensaje(idiomaUsuario, "altDesplegable")%>" title="<%=meLanbide59I18n.getMensaje(idiomaUsuario, "altDesplegable")%>"></a>
                        </div>
                    </div>
                </fieldset>
                <div class="botonera">
                        <input type="button" id="btnGuardar" name="btnGuardar" class="botonGeneral" value="<%=meLanbide59I18n.getMensaje(idiomaUsuario,"btn.aceptar")%>" onclick="guardar();">
                        <input type="button" id="btnCancelar" name="btnCancelar" class="botonGeneral" value="<%=meLanbide59I18n.getMensaje(idiomaUsuario,"btn.cancelar")%>" onclick="cancelar();">
                    <input type="button" id="btnCerrar" name="btnCerrar" class="botonGeneral" value="<%=meLanbide59I18n.getMensaje(idiomaUsuario,"btn.cerrar")%>" onclick="cerrarVentana();" style="display: none;">
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
<%-- 
    Document   : m81ListasxProyectos
    Created on : 23-ago-2022, 9:03:18
    Author     : kepa
--%>
<%@taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide81.i18n.MeLanbide81I18n"%>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject"%>
<%@page import="es.altia.common.service.config.Config"%>
<%@page import="es.altia.common.service.config.ConfigServiceHelper"%>
<%@page import="java.util.ArrayList" %>
<%@page import="java.util.List" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="X-UA-Compatible" content="IE=Edge" />
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <title>Detalles por proyecto</title>
        <%
                    UsuarioValueObject usuarioVO = new UsuarioValueObject();
                    int idiomaUsuario = 1;
                    int apl = 5;
                    String css = "";
                    if (session.getAttribute("usuario") != null){
                        usuarioVO = (UsuarioValueObject) session.getAttribute("usuario");
                        apl = usuarioVO.getAppCod();
                        idiomaUsuario = usuarioVO.getIdioma();
                        css = usuarioVO.getCss();
                    }
        
                    //Clase para internacionalizar los mensajes de la aplicación.
                    MeLanbide81I18n meLanbide81I18n = MeLanbide81I18n.getInstance();
                    String numExpediente = (String)request.getAttribute("numExp");
                    String idProyecto = (String)request.getAttribute("idProyecto");               
                    String prioridad = (String)request.getAttribute("prioridad");               
                    String denomProyecto = (String)request.getAttribute("denomProyecto");               
                    String tipoProyecto = (String)request.getAttribute("tipoProyecto");               

                    String urlPestanaContrataciones = (String)request.getAttribute("urlPestanaContrataciones");        
                    String urlPestanaPuestos = (String)request.getAttribute("urlPestanaPuestos");
        %>
        <jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
        <jsp:setProperty name="descriptor"  property="idi_cod" value="<%=idiomaUsuario%>" />
        <jsp:setProperty name="descriptor"  property="apl_cod" value="<%=apl%>" />
        <link rel="StyleSheet" media="screen" type="text/css" href="<%=request.getContextPath()%><%=css%>"/>
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/>
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/extension/melanbide81/melanbide81.css"/>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/general.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/listaComboBox.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/JavaScriptUtil.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/popup.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/tabpane.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/melanbide81/LpeelUtils.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/melanbide81/LpeelTablaNueva.js"></script>
        <script type="text/javascript">
            var tpM81;
            var mensajeValidacion = '';
            var prioridad = '<%=prioridad%>';
            var denom = '<%=denomProyecto%>';
            var APP_CONTEXT_PATH = '<%=request.getContextPath()%>';
            var url = APP_CONTEXT_PATH + "/PeticionModuloIntegracion.do";
        </script>
    </head>
    <body class="bandaBody">
        <div class="tab-page" id="tabPageM81" style="width: 100%; margin-left:5px;">
            <!-- Datos Comunes para las pestanas -->
            <input type="hidden" id="idProyecto" name="idProyecto" value="<%=idProyecto%>"/>
            <input type="hidden" id="prioridad" name="prioridad" value="<%=prioridad%>"/>
            <input type="hidden" id="denomProyecto" name="denomProyecto" value="<%=denomProyecto%>"/>
            <input type="hidden" id="tipoProyecto" name="tipoProyecto" value="<%=tipoProyecto%>"/>
            <input type="hidden" id="numExpediente" name="numExpediente" value="<%=numExpediente%>"/>
            <input type="hidden" id="errorBD" name="errorBD" value="<%=meLanbide81I18n.getMensaje(idiomaUsuario,"error.errorBD")%>"/>
            <input type="hidden" id="generico" name="generico" value="<%=meLanbide81I18n.getMensaje(idiomaUsuario,"error.generico")%>"/>
            <input type="hidden" id="pasoParametros" name="pasoParametros" value="<%=meLanbide81I18n.getMensaje(idiomaUsuario,"error.pasoParametros")%>"/>
            <input type="hidden" id="listaVacia" name="listaVacia" value="<%=meLanbide81I18n.getMensaje(idiomaUsuario,"error.listaVacia")%>"/>
            <input type="hidden" id="obtenerDatos" name="obtenerDatos" value="<%=meLanbide81I18n.getMensaje(idiomaUsuario,"error.obtenerDatos")%>"/>
            <input type="hidden" id="borrarDatos" name="borrarDatos" value="<%=meLanbide81I18n.getMensaje(idiomaUsuario,"error.borrarDatos")%>"/>
            <input type="hidden" id="operacionIncorrecta" name="operacionIncorrecta" value="<%=meLanbide81I18n.getMensaje(idiomaUsuario,"error.operacionIncorrecta")%>"/>



            <h1 class="legendAzul" id="pestanaM81"></h1>
            <div id="barraProgresoLPEEL" style="visibility: hidden;">
                <div class="contenedorHidepage">
                    <div class="textoHide">
                        <span>
                            <%=meLanbide81I18n.getMensaje(idiomaUsuario, "msg.procesando")%>
                        </span>
                    </div>
                    <div class="imagenHide">
                        <span id="disco" class="fa fa-spinner fa-spin" aria-hidden="true"/>
                    </div>
                </div>
            </div>
            <div style="clear: both; width: 100%; text-align: left;">
                <div id="tab-panel-m81" class="tab-pane" style="float: left; font-size: 13px;" align="center"></div>
                <script type="text/javascript">
                    tpM81 = new WebFXTabPane(document.getElementById("tab-panel-m81"));
                    if (<%=tipoProyecto%> != null && <%=tipoProyecto%> == 2) {
                        tpM81.selectedIndex = 1;
                    } else {
                        tpM81.selectedIndex = 0;
                    }
                </script>
                <div class="tab-page" id="tabPageM81_1" style="width:100%; font-size: 13px;">
                    <h2 class="tab" id="pestanaM81_1"> <%=meLanbide81I18n.getMensaje(idiomaUsuario,"label.titulo.pestanaPuestos")%></h2>
                    <script type="text/javascript">tpM81.addTabPage(document.getElementById("tabPageM81_1"));</script>
                    <div style="clear: both;border:1px solid; border-color: gray; height:100%; width:98%;">
                        <jsp:include page="<%=urlPestanaPuestos%>" flush="true"/>
                    </div>
                </div>
                <div class="tab-page" id="tabPageM81_2" style="width:100%; font-size: 13px; ">
                    <h2 class="tab" id="pestanaM81_2"> <%=meLanbide81I18n.getMensaje(idiomaUsuario,"label.titulo.pestanaContrataciones")%></h2>
                    <script type="text/javascript">tpM81.addTabPage(document.getElementById("tabPageM81_2"));</script>
                    <div style="clear: both;border:1px solid; border-color: gray; height:100%; width:98%;">
                        <jsp:include page="<%=urlPestanaContrataciones%>" flush="true"/>
                    </div>
                </div>
                <div class="botonera" style="width: 100%; float: left; text-align: center;margin-top: 25px;">
                    <input type="button" id="btnVolverEspacio" name="btnVolverEspacio" class="botonLargo" value="<%=meLanbide81I18n.getMensaje(idiomaUsuario, "btn.volver")%>" onclick="cerrarVentana();">
                </div>
            </div>
        </div>
        <script  type="text/javascript">

            document.getElementById('pestanaM81').innerHTML = prioridad + ' - ' + denom;
        </script>
    </body>
</html>
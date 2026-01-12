<%-- 
    Document   : solicitud23
    Created on : 29-sep-2023, 13:58:12
    Author     : kepa
--%>
<%@taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>

<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide35.i18n.MeLanbide35I18n" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide35.vo.solicitud.EcaSolicitud23VO" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.common.service.config.Config"%>
<%@page import="es.altia.common.service.config.ConfigServiceHelper"%>
<%@page import="java.util.ArrayList" %>
<%@page import="java.util.List" %>
<%
    int idiomaUsuario = 1;
    int apl = 5;
    String css = "";
    UsuarioValueObject usuario = new UsuarioValueObject();
    
    if (session.getAttribute("usuario") != null) {
        usuario = (UsuarioValueObject) session.getAttribute("usuario");
        apl = usuario.getAppCod();
        idiomaUsuario = usuario.getIdioma();
        css = usuario.getCss();
    }
    
//Clase para internacionalizar los mensajes de la aplicación.
    MeLanbide35I18n meLanbide35I18n = MeLanbide35I18n.getInstance();
    
    Config m_Config = ConfigServiceHelper.getConfig("common");
    String statusBar = m_Config.getString("JSP.StatusBar");
    String nombreModulo     = request.getParameter("nombreModulo");
 //   String codOrganizacion  = request.getParameter("codOrganizacionModulo");
    String numExpediente    = request.getParameter("numero");
    
    String [] datosExp = numExpediente!=null ? numExpediente.split("/"):null;
    int ejercicioExpediente = (datosExp!=null && datosExp.length>0 ?  Integer.valueOf(datosExp[0]) : 2023);  // A partir de 2023 son los cambios

    String urlPestanaInsercionECA23 = (String)request.getAttribute("urlPestanaInsercion_solicitudECA23");
    String urlPestanaProspectorECA23 = (String)request.getAttribute("urlPestanaProspector_solicitudECA23");
    String urlPestanaPreparadorECA23 = (String)request.getAttribute("urlPestanaPreparador_solicitudECA23");
   // String urlPestanaSeguimientoECA23 = (String)request.getAttribute("urlPestanaSeguimiento_solicitudECA23");
    String urlPestanaResumenECA23 = (String)request.getAttribute("urlPestanaResumen_solicitudECA23");


    String userAgent = request.getHeader("user-agent");
    String margenIzqPestanasSolicitud = "";

    if(userAgent.indexOf("MSIE")!=-1) {
        // Internet Explorer
        margenIzqPestanasSolicitud = "";
    }else{
        // Firefox u otro navegador
        margenIzqPestanasSolicitud = "margin-left:10px;";
    }
%>
<jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
<jsp:setProperty name="descriptor"  property="idi_cod" value="<%=idiomaUsuario%>" />
<jsp:setProperty name="descriptor"  property="apl_cod" value="<%=apl%>" />
<link rel="StyleSheet" media="screen" type="text/css" href="<%=request.getContextPath()%><%=css%>">
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/melanbide35/TablaNuevaEca.js"></script>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/extension/melanbide35/melanbide35.css"/>
<script type="text/javascript">
    var APP_CONTEXT_PATH = '<%=request.getContextPath()%>';
    var baseUrl = APP_CONTEXT_PATH + '/PeticionModuloIntegracion.do';
    var tp35_esprec;

    function ocultarPestanasSolicitudEca() {
        tp35_esprec.hideTabPage(1);
        tp35_esprec.hideTabPage(2);
        tp35_esprec.hideTabPage(3);
    }

    function mostrarPestanasSolicitudEca() {
        tp35_esprec.showTabPage(1);
        tp35_esprec.showTabPage(2);
        tp35_esprec.showTabPage(3);
    }

    function actualizarOtrasPestanasEca23(pestana) {
        //el parametro pestana hace referencia a la pestaña que llama. 
        //Se supone que esa pestaña actualiza sus propios datos y llama aqui para avisar a las demás de que se actualicen tb.

        //Numeración de las pestañas:

        //1) Solicitud - Solicitud
        //2) Solicitud - Preparadores
        //3) Solicitud - Prospectores
        //4) Solicitud - Resumen
    }
</script>
<body>
    <div class="tab-page" id="tabPage351" style="height:520px; width: 100%;">
        <h2 class="tab" id="pestana351"><%=meLanbide35I18n.getMensaje(idiomaUsuario,"label.solicitud.solicitud.tituloPestana")%></h2>
        <script type="text/javascript">tp1.addTabPage(document.getElementById("tabPage351"));</script>
        <div style="clear: both;">
            <div id="tab-panel-35_sol" class="tab-pane" style="float: left;" align="center"></div>
            <script type="text/javascript">
                tp35_esprec = new WebFXTabPane(document.getElementById("tab-panel-35_sol"));
                tp35_esprec.selectedIndex = 0;
            </script>
            <div class="tab-page" id="tabPage35_sol_10" style="height: 480px; width: 98%;">
                <h2 class="tab" id="pestana35_sol_10" style="<%=margenIzqPestanasSolicitud%>"><%=meLanbide35I18n.getMensaje(idiomaUsuario,"label.solicitud.solicitud.tituloPestana")%></h2>
                <script type="text/javascript">tp35_esprec.addTabPage(document.getElementById("tabPage35_sol_10"));</script>
                <div style="clear: both; padding-top: 14px;">
                    <jsp:include page="<%=urlPestanaInsercionECA23%>" flush="true"/>
                </div>
            </div>
            <div class="tab-page" id="tabPage35_sol_11" style="height: 480px;width: 98%;">
                <h2 class="tab" id="pestana35_sol_11"><%=meLanbide35I18n.getMensaje(idiomaUsuario,"label.solicitud.preparadores.tituloPestana")%></h2>
                <script type="text/javascript">tp35_esprec.addTabPage(document.getElementById("tabPage35_sol_11"));</script>
                <div style="clear: both; padding-top: 14px;">              
                    <jsp:include page="<%=urlPestanaPreparadorECA23%>" flush="true"/>
                </div>
            </div>
            <div class="tab-page" id="tabPage35_sol_12" style="height: 480px;width: 98%;">
                <h2 class="tab" id="pestana35_sol_12"><%=meLanbide35I18n.getMensaje(idiomaUsuario,"label.solicitud.prospectores.tituloPestana")%></h2>
                <script type="text/javascript">tp35_esprec.addTabPage(document.getElementById("tabPage35_sol_12"));</script> 
                <div style="clear: both; padding-top: 14px;">
                    <jsp:include page="<%=urlPestanaProspectorECA23%>" flush="true"/>
                </div>
            </div>
        </div>
    </div>
</body>
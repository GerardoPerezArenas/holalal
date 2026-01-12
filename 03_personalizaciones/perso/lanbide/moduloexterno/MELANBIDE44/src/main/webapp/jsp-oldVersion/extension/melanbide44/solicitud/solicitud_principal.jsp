<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>

<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide44.i18n.MeLanbide44I18n" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide44.vo.solicitud.EcaSolicitudVO" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@ page import="es.altia.common.service.config.Config"%>
<%@ page import="es.altia.common.service.config.ConfigServiceHelper"%>
<%@page import="java.util.ArrayList" %>
<%@page import="java.util.List" %>

<%
    String sIdioma = request.getParameter("idioma");
    int idiomaUsuario = Integer.parseInt(sIdioma);
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
    MeLanbide44I18n meLanbide44I18n = MeLanbide44I18n.getInstance();
    
    Config m_Config = ConfigServiceHelper.getConfig("common");
    String statusBar = m_Config.getString("JSP.StatusBar");
    String nombreModulo     = request.getParameter("nombreModulo");
    String codOrganizacion  = request.getParameter("codOrganizacionModulo");
    String numExpediente    = request.getParameter("numero");
    
    String urlPestanaSolicitud_solicitud = (String)request.getAttribute("urlPestanaSolicitud_solicitud");
    String urlPestanaPreparadores_solicitud = (String)request.getAttribute("urlPestanaPreparadores_solicitud");
    String urlPestanaProspectores_solicitud = (String)request.getAttribute("urlPestanaProspectores_solicitud");
    String urlPestanaValoracion_solicitud = (String)request.getAttribute("urlPestanaValoracion_solicitud");
    String urlPestanaResumen_solicitud = (String)request.getAttribute("urlPestanaResumen_solicitud");
    
    EcaSolicitudVO ecaSolicitud = (EcaSolicitudVO)request.getAttribute("ecaSolicitud");
    
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

<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/estilo.css"/>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/extension/melanbide44/melanbide44.css"/>

<script type="text/javascript">
    var tp35_sol;

    function configurarPestanas() {
    <%
            if(ecaSolicitud != null)
            {
    %>
        mostrarPestanasSolicitudEca();
    <%
            }
            else
            {
    %>
        ocultarPestanasSolicitudEca();
    <%
            }
    %>
    }

    function ocultarPestanasSolicitudEca() {
        tp35_sol.hideTabPage(1);
        tp35_sol.hideTabPage(2);
        tp35_sol.hideTabPage(3);
    }

    function mostrarPestanasSolicitudEca() {
        tp35_sol.showTabPage(1);
        tp35_sol.showTabPage(2);
        tp35_sol.showTabPage(3);
    }

    function descargarPlantillaEca(nombrePlantilla) {
        var CONTEXT_PATH = '<%=request.getContextPath()%>'
        var url = CONTEXT_PATH + "/PeticionModuloIntegracion.do";
        var parametros = "";
        var control = new Date();
        parametros = '?tarea=preparar&modulo=MELANBIDE44&operacion=descargarPlantilla&tipo=0&nombrePlantilla=' + nombrePlantilla + '&control=' + control.getTime();
        inicio = false;
        window.open(url + parametros, "_blank");
    }

    function actualizarOtrasPestanasEca(pestana) {
        //el parametro pestana hace referencia a la pestaña que llama. 
        //Se supone que esa pestaña actualiza sus propios datos y 
        //llama aqui para avisar a las demás de que se actualicen tb.

        //Numeración de las pestañas:

        //1) Solicitud - Solicitud
        //2) Solicitud - Preparadores
        //3) Solicitud - Prospectores
        //4) Solicitud - Valoración
        //5) Justificación - Preparadores
        //6) Justificación - Prospectores

        if (pestana != 1) {
            actualizarDatosAnexosSolicitud(false);
        }

        if (pestana != 2) {
            actualizarTablaPreparadoresSolicitudEca(false);
        }

        if (pestana != 3) {
            actualizarTablaProspectoresSolicitudEca(false);
        }

        if (pestana != 4) {

        }

        if (pestana != 5) {
            actualizarTablaPreparadoresJustificacionEca(false);
        }

        if (pestana != 6) {
            actualizarTablaProspectoresJustificacionEca(false);
        }

        if (pestana != 7) {
            refrescarPantallaResumen();
        }
    }
</script>

<body>
    <div class="tab-page" id="tabPage351" style="height:520px; width: 100%;">
        <h2 class="tab" id="pestana351"><%=meLanbide44I18n.getMensaje(idiomaUsuario,"label.solicitud.solicitud.tituloPestana")%></h2>
        <script type="text/javascript">tp1.addTabPage(document.getElementById("tabPage351"));</script>
        <div style="clear: both;">
            <div id="tab-panel-35_sol" class="tab-pane" style="float: left;" align="center"></div>
            <script type="text/javascript">
                tp35_sol = new WebFXTabPane(document.getElementById("tab-panel-35_sol"));
                tp35_sol.selectedIndex = 0;
            </script>
            <div class="tab-page" id="tabPage35_sol_1" style="height: 480px;">
                <h2 class="tab" id="pestana35_sol_1" style="<%=margenIzqPestanasSolicitud%>"><%=meLanbide44I18n.getMensaje(idiomaUsuario,"label.solicitud.solicitud.tituloPestana")%></h2>
                <script type="text/javascript">tp35_sol.addTabPage(document.getElementById("tabPage35_sol_1"));</script>
                <div style="clear: both; padding-top: 14px;">
                    <jsp:include page="<%=urlPestanaSolicitud_solicitud%>" flush="true"/>
                </div>
            </div>
            <div class="tab-page" id="tabPage35_sol_2" style="height: 480px;">
                <h2 class="tab" id="pestana35_sol_2"><%=meLanbide44I18n.getMensaje(idiomaUsuario,"label.solicitud.preparadores.tituloPestana")%></h2>
                <script type="text/javascript">tp35_sol.addTabPage(document.getElementById("tabPage35_sol_2"));</script>
                <div style="clear: both; padding-top: 14px;">
                    <jsp:include page="<%=urlPestanaPreparadores_solicitud%>" flush="true"/>
                </div>
            </div>
            <div class="tab-page" id="tabPage35_sol_3" style="height: 480px;">
                <h2 class="tab" id="pestana35_sol_3"><%=meLanbide44I18n.getMensaje(idiomaUsuario,"label.solicitud.prospectores.tituloPestana")%></h2>
                <script type="text/javascript">tp35_sol.addTabPage(document.getElementById("tabPage35_sol_3"));</script>
                <div style="clear: both; padding-top: 14px;">
                    <jsp:include page="<%=urlPestanaProspectores_solicitud%>" flush="true"/>
                </div>
            </div>
            <div class="tab-page" id="tabPage35_sol_4" style="height: 480px;">
                <h2 class="tab" id="pestana35_sol_4"><%=meLanbide44I18n.getMensaje(idiomaUsuario,"label.solicitud.valoracion.tituloPestana")%></h2>
                <script type="text/javascript">tp35_sol.addTabPage(document.getElementById("tabPage35_sol_4"));</script>
                <div style="clear: both; padding-top: 14px;">
                    <jsp:include page="<%=urlPestanaValoracion_solicitud%>" flush="true"/>
                </div>
            </div>
        </div>
    </div>
</body>

<script type="text/javascript">
    configurarPestanas();
</script>
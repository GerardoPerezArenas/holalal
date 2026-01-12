<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>

<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide35.i18n.MeLanbide35I18n" %>
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
    MeLanbide35I18n meLanbide35I18n = MeLanbide35I18n.getInstance();
    
    Config m_Config = ConfigServiceHelper.getConfig("common");
    String statusBar = m_Config.getString("JSP.StatusBar");
    String nombreModulo     = request.getParameter("nombreModulo");
    String codOrganizacion  = request.getParameter("codOrganizacionModulo");
    String numExpediente    = request.getParameter("numero");
    
    String urlPestanaJustificacion = (String)request.getAttribute("urlPestanaJustificacion");
    String urlPestanaPreparadores_justificacion = (String)request.getAttribute("urlPestanaPreparadores_justificacion");
    String urlPestanaProspectores_justificacion = (String)request.getAttribute("urlPestanaProspectores_justificacion");
    
    //EcaSolicitudVO ecaSolicitud = (EcaSolicitudVO)request.getAttribute("ecaSolicitud");
    
    String userAgent = request.getHeader("user-agent");
    String margenIzqPestanasJustificacion = "";

    if(userAgent.indexOf("MSIE")!=-1) {
        // Internet Explorer
        margenIzqPestanasJustificacion = "";
    }else{
        // Firefox u otro navegador
        margenIzqPestanasJustificacion = "margin-left:10px;";
    }
%>

<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/estilo.css"/>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/extension/melanbide35/melanbide35.css"/>

<script type="text/javascript">   
    var tp35_jus;
    
    function configurarPestanas(){        
        mostrarPestanasJustificacionEca();        
    }
    
    function ocultarPestanasJustificacionEca(){
        tp35_jus.hideTabPage(1);
        //tp35_jus.hideTabPage(2);
    }
    
    function mostrarPestanasJustificacionEca(){
        tp35_jus.showTabPage(1);
        //tp35_jus.showTabPage(2);
    }
</script>

<body>
    <div class="tab-page" id="tabPage352" style="height:520px; width: 100%;">
        <h2 class="tab" id="pestana352"><%=meLanbide35I18n.getMensaje(idiomaUsuario,"label.justificacion.tituloPestana")%></h2>
        <script type="text/javascript">tp1.addTabPage( document.getElementById( "tabPage352" ) );</script>
        <div style="clear: both;">
            <div id="tab-panel-35_jus" class="tab-pane" style="float: left;" align="center"></div>
            <script type="text/javascript"> 
                tp35_jus = new WebFXTabPane(document.getElementById("tab-panel-35_jus"));
                tp35_jus.selectedIndex = 0;
            </script>            
            <div class="tab-page" id="tabPage35_jus_1" style="height: 480px;">
                <h2 class="tab" id="pestana35_jus_1" style="<%=margenIzqPestanasJustificacion%>"><%=meLanbide35I18n.getMensaje(idiomaUsuario,"label.justificacion.preparadores.tituloPestana")%></h2>
                <script type="text/javascript">tp35_jus.addTabPage( document.getElementById( "tabPage35_jus_1" ) );</script>
                <div style="clear: both; padding-top: 14px;">
                    <jsp:include page="<%=urlPestanaPreparadores_justificacion%>" flush="true"/>
                </div>
            </div>
            <div class="tab-page" id="tabPage35_jus_2" style="height: 480px;">
                <h2 class="tab" id="pestana35_jus_2"><%=meLanbide35I18n.getMensaje(idiomaUsuario,"label.justificacion.prospectores.tituloPestana")%></h2>
                <script type="text/javascript">tp35_jus.addTabPage( document.getElementById( "tabPage35_jus_2" ) );</script>
                <div style="clear: both; padding-top: 14px;">
                    <jsp:include page="<%=urlPestanaProspectores_justificacion%>" flush="true"/>
                </div>
            </div>
            
            
        </div>
    </div>
</body>

<script type="text/javascript">   
    configurarPestanas();
</script>
<%@taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>

<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide45.i18n.MeLanbide45I18n" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.common.service.config.Config"%>
<%@page import="es.altia.common.service.config.ConfigServiceHelper"%>
<%@page import="java.util.ArrayList" %>
<%@page import="java.util.List" %>
<html>
    <head>
<%
    int idiomaUsuario = 1;
 int apl = 5;
 String css = "";
    if(request.getParameter("idioma") != null)
    {
        try
        {
            idiomaUsuario = Integer.parseInt(request.getParameter("idioma"));
        }
        catch(Exception ex)
        {}
    }
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
    MeLanbide45I18n meLanbide45I18n = MeLanbide45I18n.getInstance();
    
    Config m_Config = ConfigServiceHelper.getConfig("common");
    String statusBar = m_Config.getString("JSP.StatusBar");
    String nombreModulo     = request.getParameter("nombreModulo");
    String codOrganizacion  = request.getParameter("codOrganizacionModulo");
    String numExpediente    = request.getParameter("numero");
    
    // subpestañas - listas
    String urlPestanaModulosForm = (String)request.getAttribute("urlPestanaModulosForm");
    //String urlPestanaEspaciosFor = (String)request.getAttribute("urlPestanaEspaciosFor");
    //String urlPestanaMaterialConsu = (String)request.getAttribute("urlPestanaMaterialConsu");


    
    String userAgent = request.getHeader("user-agent");
    String margenIzqPestanaEpecialidadesRecursos = "";

    if(userAgent.indexOf("MSIE")!=-1) {
        // Internet Explorer
        margenIzqPestanaEpecialidadesRecursos = "";
    }else{
        // Firefox u otro navegador
        margenIzqPestanaEpecialidadesRecursos = "margin-left:10px;";
    }
%>

<jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
<jsp:setProperty name="descriptor"  property="idi_cod" value="<%=idiomaUsuario%>" />
<jsp:setProperty name="descriptor"  property="apl_cod" value="<%=apl%>" />
<link rel="StyleSheet" media="screen" type="text/css" href="<%=request.getContextPath()%><%=css%>">
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/extension/melanbide45/melanbide45.css"/>

<script type="text/javascript">   
    var tp41_esprec;
    
    function configurarPestanas(){        
        mostrarPestanaEpecialidadesRecursos();        
    }
    
    function ocultarPestanaEpecialidadesRecursos(){
        tp41_esprec.hideTabPage(1);
    }
    
    function mostrarPestanaEpecialidadesRecursos(){
        tp41_esprec.showTabPage(1);
    }
</script>

<body>
    <div class="tab-page" id="tabPage125" style="height:520px; width: 100%;">
        <h2 class="tab" id="pestana125"><%=meLanbide45I18n.getMensaje(idiomaUsuario,"label.tituloPestanaModulo")%></h2>
        <script type="text/javascript">tp1.addTabPage( document.getElementById( "tabPage125" ) );</script>
        <div style="clear: both;">
            <div id="tab-panel-45_modespform" class="tab-pane" style="float: left;" align="center"></div>
            <script type="text/javascript"> 
                tp41_esprec = new WebFXTabPane(document.getElementById("tab-panel-45_modespform"));
                tp41_esprec.selectedIndex = 0;
            </script>            
            <div class="tab-page" id="tabPage45_lismodform" style="height: 480px;">
                <h2 class="tab" id="pestana45_lismodform" style="<%=margenIzqPestanaEpecialidadesRecursos%>"><%=meLanbide45I18n.getMensaje(idiomaUsuario,"label.moduloformativo.tituloPestana")%></h2>
                <script type="text/javascript">tp41_esprec.addTabPage( document.getElementById( "tabPage45_lismodform" ) );</script>
                <div style="clear: both; padding-top: 14px;">
                    <jsp:include page="<%=urlPestanaModulosForm%>" flush="true"/>
                </div>
            </div>                                
            <div class="tab-page" id="tabPage45_ubicacion" style="height: 280px;">
                <h2 class="tab" id="pestana45_ubicacion"><%=meLanbide45I18n.getMensaje(idiomaUsuario,"label.seleccionUbicacionCentros.tituloPestana")%></h2>
                <script type="text/javascript">tp41_esprec.addTabPage( document.getElementById( "tabPage45_ubicacion" ) );</script>
                <div style="clear: both; padding-top: 14px;">
                    <jsp:include page="/jsp/extension/melanbide45/seleccionUbicacionCentro.jsp" flush="true"/>
                </div>
            </div>    
        </div>
    </div>
	<script type="text/javascript">   
		configurarPestanas();
	</script>    
    </body>
</html>

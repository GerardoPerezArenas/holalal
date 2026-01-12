<%@taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>

<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide41.i18n.MeLanbide41I18n" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.common.service.config.Config"%>
<%@page import="es.altia.common.service.config.ConfigServiceHelper"%>
<%@page import="java.util.ArrayList" %>
<%@page import="java.util.List" %>
<meta http-equiv="X-UA-Compatible" content="IE=Edge" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
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
    MeLanbide41I18n meLanbide41I18n = MeLanbide41I18n.getInstance();
    
    Config m_Config = ConfigServiceHelper.getConfig("common");
    String statusBar = m_Config.getString("JSP.StatusBar");
    String nombreModulo     = request.getParameter("nombreModulo");
    String codOrganizacion  = request.getParameter("codOrganizacionModulo");
    String numExpediente    = request.getParameter("numero");
    
    // pestaña principal
    //String urlPestanaEpecialidadesRecursos = (String)request.getAttribute("urlPestanaEpecialidadesRecursos");   
    // subpestañas - listas
    String urlPestanaEspecialidades = (String)request.getAttribute("urlPestanaEspecialidades");
    String urlPestanaServicios = (String)request.getAttribute("urlPestanaServicios");
    String urlPestanaDisponibilidad = (String)request.getAttribute("urlPestanaDisponibilidad");
    String urlPestanaCapacidad = (String)request.getAttribute("urlPestanaCapacidad");
    String urlPestanaDotacion = (String)request.getAttribute("urlPestanaDotacion");
    String urlPestanaMaterial = (String)request.getAttribute("urlPestanaMaterial");
    String urlPestanaIdentificacionEsp = (String)request.getAttribute("urlPestanaIdentificacionEsp");
   
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
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/extension/melanbide41/melanbide41.css"/>
<script type="text/javascript">
    var tp41_esprec;

    function configurarPestanas() {
        mostrarPestanaEpecialidadesRecursos();
    }

    function ocultarPestanaEpecialidadesRecursos() {
        tp41_esprec.hideTabPage(1);
        //tp41_esprec.hideTabPage(2);
    }

    function mostrarPestanaEpecialidadesRecursos() {
        tp41_esprec.showTabPage(1);
        //tp41_esprec.showTabPage(2);
    }
</script>
<body>
    <div class="tab-page" id="tabPage352" style="height:520px; width: 100%;">
        <h2 class="tab" id="pestana352"><%=meLanbide41I18n.getMensaje(idiomaUsuario,"label.especialidadesRecursos.tituloPestana")%></h2>
        <script type="text/javascript">tp1.addTabPage(document.getElementById("tabPage352"));</script>
        <div style="clear: both;">
            <div id="tab-panel-41_esprec" class="tab-pane" style="float: left;" align="center"></div>
            <script type="text/javascript">
                tp41_esprec = new WebFXTabPane(document.getElementById("tab-panel-41_esprec"));
                tp41_esprec.selectedIndex = 0;
            </script>            
            <div class="tab-page" id="tabPage41_lisesp" style="height: 480px;">
                <h2 class="tab" id="pestana41_lisesp" style="<%=margenIzqPestanaEpecialidadesRecursos%>"><%=meLanbide41I18n.getMensaje(idiomaUsuario,"label.especialidadesRecursos.listaespecialidades.tituloPestana")%></h2>
                <script type="text/javascript">tp41_esprec.addTabPage(document.getElementById("tabPage41_lisesp"));</script>
                <div style="clear: both; padding-top: 14px;">
                    <jsp:include page="<%=urlPestanaEspecialidades%>" flush="true"/>
                </div>
            </div>
            <div class="tab-page" id="tabPage41_lisser" style="height: 480px;">
                <h2 class="tab" id="pestana41_lisser"><%=meLanbide41I18n.getMensaje(idiomaUsuario,"label.especialidadesRecursos.listaservicios.tituloPestana")%></h2>
                <script type="text/javascript">tp41_esprec.addTabPage(document.getElementById("tabPage41_lisser"));</script>
                <div style="clear: both; padding-top: 14px;">
                    <jsp:include page="<%=urlPestanaServicios%>" flush="true"/>
                </div>
            </div>
            <div class="tab-page" id="tabPage41_lisdisp" style="height: 480px;">
                <h2 class="tab" id="pestana41_lisdisp"><%=meLanbide41I18n.getMensaje(idiomaUsuario,"label.especialidadesRecursos.listadisponibilidad.tituloPestana")%></h2>
                <script type="text/javascript">tp41_esprec.addTabPage(document.getElementById("tabPage41_lisdisp"));</script>
                <div style="clear: both; padding-top: 14px;">
                    <jsp:include page="<%=urlPestanaDisponibilidad%>" flush="true"/>
                </div>
            </div>
        </div>
    </div>
</body>
<script type="text/javascript">
    configurarPestanas();
</script>

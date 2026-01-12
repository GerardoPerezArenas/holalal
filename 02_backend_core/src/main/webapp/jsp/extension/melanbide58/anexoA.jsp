<%@taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>

<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide58.i18n.MeLanbide58I18n" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.common.service.config.Config"%>
<%@page import="es.altia.common.service.config.ConfigServiceHelper"%>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide58.util.ConstantesMeLanbide58"%>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide58.util.ConfigurationParameter"%>
<%@page import="java.util.ArrayList" %>
<%@page import="java.util.List" %>
<%
    int idiomaUsuario = 1;
    if(request.getParameter("idioma") != null) {
        try {
            idiomaUsuario = Integer.parseInt(request.getParameter("idioma"));
        }
        catch(Exception ex)
        {}
    }
    UsuarioValueObject usuario = new UsuarioValueObject();

        //nuevas
            UsuarioValueObject usuarioVO = new UsuarioValueObject();
            int apl = 5;
            String css = "";
            if (session.getAttribute("usuario") != null) {
                usuarioVO = (UsuarioValueObject) session.getAttribute("usuario");
                apl = usuarioVO.getAppCod();
                idiomaUsuario = usuarioVO.getIdioma();
                css = usuarioVO.getCss();
            }
	
    //Clase para internacionalizar los mensajes de la aplicaci�n.
    MeLanbide58I18n meLanbide58I18n = MeLanbide58I18n.getInstance();
    
    Config m_Config = ConfigServiceHelper.getConfig("common");
    String statusBar = m_Config.getString("JSP.StatusBar");
    String nombreModulo     = request.getParameter("nombreModulo");
    String codOrganizacion  = request.getParameter("codOrganizacionModulo");
    String numExpediente    = request.getParameter("numero");

    String urlPestanaAltas = (String)request.getAttribute("urlPestanaAltas");
    String urlPestanaBajas = (String)request.getAttribute("urlPestanaBajas");
    
    String userAgent = request.getHeader("user-agent");
    String margenIzqPestanaPrincipal = "";

    if(userAgent.indexOf("MSIE")!=-1) {
        // Internet Explorer
        margenIzqPestanaPrincipal = "";
    }else{
        // Firefox u otro navegador
        margenIzqPestanaPrincipal = "margin-left:10px;";
    }
%>

<jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
<jsp:setProperty name="descriptor"  property="idi_cod" value="<%=idiomaUsuario%>" />
<jsp:setProperty name="descriptor"  property="apl_cod" value="<%=apl%>" />
<link rel="StyleSheet" media="screen" type="text/css" href="<%=request.getContextPath()%><%=css%>">
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/extension/melanbide58/melanbide58.css"/>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/meLanbide58/ceescUtils.js"></script>

<script type="text/javascript">
    var APP_CONTEXT_PATH = '<%=request.getContextPath()%>';
    var baseUrl = APP_CONTEXT_PATH + '/PeticionModuloIntegracion.do';
    var tp58_esprec;

    function configurarPestanas() {
        mostrarPestanaPrincipal();
    }

    function ocultarPestanaPrincipal() {
        tp58_esprec.hideTabPage(1);
        //tp58_esprec.hideTabPage(2);
    }

    function mostrarPestanaPrincipal() {
        tp58_esprec.showTabPage(1);
        //tp58_esprec.showTabPage(2);
    }
</script>
<div class="tab-page" id="tabPage353" style="height:520px; width: 100%;">
    <h2 class="tab" id="pestana353"><%=meLanbide58I18n.getMensaje(idiomaUsuario,"label.titulo.pestanaAnexoA")%></h2>
    <script type="text/javascript">tp1.addTabPage(document.getElementById("tabPage353"));</script>
    <div style="clear: both;">
        <div id="tab-panel-58_esprec" class="tab-pane" style="float: left;" align="center"></div>
        <script type="text/javascript">
            tp58_esprec = new WebFXTabPane(document.getElementById("tab-panel-58_esprec"));
            tp58_esprec.selectedIndex = 0;
        </script>            
        <div class="tab-page" id="tabPage58_lisaltas" style="height: 480px;">
            <h2 class="tab" id="pestana58_lisaltas" style="<%=margenIzqPestanaPrincipal%>"><%=meLanbide58I18n.getMensaje(idiomaUsuario,"label.titulo.pestanaAltas")%></h2>
            <script type="text/javascript">tp58_esprec.addTabPage(document.getElementById("tabPage58_lisaltas"));</script>
            <div style="clear: both; padding-top: 14px;">
                <jsp:include page="<%=urlPestanaAltas%>" flush="true"/>
            </div>
        </div>
        <div class="tab-page" id="tabPage58_lisbajas" style="height: 480px;">
            <h2 class="tab" id="pestana58_lisbajas"><%=meLanbide58I18n.getMensaje(idiomaUsuario,"label.titulo.pestanaBajas")%></h2>
            <script type="text/javascript">tp58_esprec.addTabPage(document.getElementById("tabPage58_lisbajas"));</script>
            <div style="clear: both; padding-top: 14px;">
                <jsp:include page="<%=urlPestanaBajas%>" flush="true"/>
            </div>
        </div>
    </div>
</div>
<script type="text/javascript">
    configurarPestanas();
</script>

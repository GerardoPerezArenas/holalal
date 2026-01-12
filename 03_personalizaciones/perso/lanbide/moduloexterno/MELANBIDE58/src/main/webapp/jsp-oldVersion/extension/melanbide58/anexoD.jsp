<%@taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>

<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide58.i18n.MeLanbide58I18n" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.common.service.config.Config"%>
<%@page import="es.altia.common.service.config.ConfigServiceHelper"%>
<%@page import="java.util.ArrayList" %>
<%@page import="java.util.List" %>
<%
    int idiomaUsuario = 1;
    if(request.getParameter("idioma") != null) {
        try {
            idiomaUsuario = Integer.parseInt(request.getParameter("idioma"));
        } catch(Exception ex) {
        }
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
            }
        }
    }
    catch(Exception ex) {      
    }
    //nuevas
        UsuarioValueObject usuarioVO = new UsuarioValueObject();
        int idioma = 1;
        int apl = 5;
        String css = "";
        if (session.getAttribute("usuario") != null) {
            usuarioVO = (UsuarioValueObject) session.getAttribute("usuario");
            apl = usuarioVO.getAppCod();
            idioma = usuarioVO.getIdioma();
            css = usuarioVO.getCss();
        }
	
    //Clase para internacionalizar los mensajes de la aplicaci�n.
    MeLanbide58I18n meLanbide58I18n = MeLanbide58I18n.getInstance();
    
    Config m_Config = ConfigServiceHelper.getConfig("common");
    String statusBar = m_Config.getString("JSP.StatusBar");
    String nombreModulo     = request.getParameter("nombreModulo");
    String codOrganizacion  = request.getParameter("codOrganizacionModulo");
    String numExpediente    = request.getParameter("numero");
    
    // pestana principal
    //String urlPestanaAnexoD = (String)request.getAttribute("urlPestanaAnexoD");   
    // subpestanas - listas
    String urlPestanaAltasD = (String)request.getAttribute("urlPestanaAltasD");
    String urlPestanaBajasD = (String)request.getAttribute("urlPestanaBajasD");
    String urlPestanaModifD = (String)request.getAttribute("urlPestanaModifD");
    
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
<jsp:setProperty name="descriptor"  property="idi_cod" value="<%=idioma%>" />
<jsp:setProperty name="descriptor"  property="apl_cod" value="<%=apl%>" />
<link rel="StyleSheet" media="screen" type="text/css" href="<%=request.getContextPath()%><%=css%>">
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/extension/melanbide58/melanbide58.css"/>

<script type="text/javascript">   
    
    var tp581_esprec;
    
    function configurarPestanas(){        
        mostrarPestanaAnexoD();        
    }
    
    function ocultarPestanaAnexoD(){
        tp581_esprec.hideTabPage(1);
        //tp581_esprec.hideTabPage(2);
    }
    
    function mostrarPestanaAnexoD(){
        tp581_esprec.showTabPage(1);
        //tp581_esprec.showTabPage(2);
    }
</script>
<div class="tab-page" id="tabPage357" style="height:520px; width: 100%;">
    <h2 class="tab" id="pestana357"><%=meLanbide58I18n.getMensaje(idiomaUsuario,"label.titulo.pestanaAnexoD")%></h2>
    <script type="text/javascript">tp1.addTabPage(document.getElementById("tabPage357"));</script>
    <div style="clear: both;">
        <div id="tab-panel-581_esprec" class="tab-pane" style="float: left;" align="center"></div> 
        <script type="text/javascript"> 
            tp581_esprec = new WebFXTabPane(document.getElementById("tab-panel-581_esprec"));
            tp581_esprec.selectedIndex = 0;
        </script> 
        <div class="tab-page" id="tabPage58_lisaltasD" style="height: 480px;">
            <h2 class="tab" id="pestana58_lisaltasD" style="<%=margenIzqPestanaPrincipal%>"><%=meLanbide58I18n.getMensaje(idiomaUsuario,"label.titulo.pestanaAltasD")%></h2>
            <script type="text/javascript">tp581_esprec.addTabPage( document.getElementById( "tabPage58_lisaltasD" ) );</script>
            <div style="clear: both; padding-top: 14px;">
                <jsp:include page="<%=urlPestanaAltasD%>" flush="true"/>
            </div>
        </div>
        <div class="tab-page" id="tabPage58_lisbajasD" style="height: 480px;">
            <h2 class="tab" id="pestana58_lisbajasD"><%=meLanbide58I18n.getMensaje(idiomaUsuario,"label.titulo.pestanaBajasD")%></h2>
            <script type="text/javascript">tp581_esprec.addTabPage( document.getElementById( "tabPage58_lisbajasD" ) );</script>
            <div style="clear: both; padding-top: 14px;">
                <jsp:include page="<%=urlPestanaBajasD%>" flush="true"/>
            </div>
        </div>
        <div class="tab-page" id="tabPage58_lismodifD" style="height: 480px;">
            <h2 class="tab" id="pestana58_lismodifD"><%=meLanbide58I18n.getMensaje(idiomaUsuario,"label.titulo.pestanaModifD")%></h2>
            <script type="text/javascript">tp581_esprec.addTabPage( document.getElementById( "tabPage58_lismodifD" ) );</script>
            <div style="clear: both; padding-top: 14px;">
                <jsp:include page="<%=urlPestanaModifD%>" flush="true"/>
            </div>
        </div>
    </div>
</div>
<script type="text/javascript">
    configurarPestanas();
</script>

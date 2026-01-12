<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>

<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide48.i18n.MeLanbide48I18n" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide48.vo.SelectItem"%>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide48.util.ConfigurationParameter"%>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide48.util.ConstantesMeLanbide48"%>
<%@ page import="es.altia.common.service.config.Config"%>
<%@ page import="es.altia.common.service.config.ConfigServiceHelper"%>
<%@page import="java.util.ArrayList" %>
<%@page import="java.util.List" %>
<%@page import="org.apache.commons.lang.StringEscapeUtils"%>

<%@page import="es.altia.agora.business.util.GeneralValueObject"%>
<%@page import="es.altia.agora.interfaces.user.web.sge.FichaExpedienteForm"%>

<%
    String sIdioma = request.getParameter("idioma");
    int idiomaUsuario = (sIdioma!=null && sIdioma!="" ? Integer.parseInt(sIdioma) : 1);
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
    MeLanbide48I18n meLanbide48I18n = MeLanbide48I18n.getInstance();
    
    Config m_Config = ConfigServiceHelper.getConfig("common");
    String statusBar = m_Config.getString("JSP.StatusBar");
    String nombreModulo     = request.getParameter("nombreModulo");
    String codOrganizacion  = request.getParameter("codOrganizacionModulo");
    String numExpediente    = request.getParameter("numero");
    String mensajeProgreso = "";
    
    String urlPestanaDatos_colectivosSolicitados = (String)request.getAttribute("urlPestanaDatos_colectivosSolicitados");
    String urlPestanaDatos_compromisoRealxColeYTTHH = (String)request.getAttribute("urlPestanaDatos_compromisoRealxColeYTTHH");

 
    String userAgent = request.getHeader("user-agent");
    String margenIzqPestanasDatosColec = "";
    if(userAgent.indexOf("MSIE")!=-1) {
        // Internet Explorer
        margenIzqPestanasDatosColec = "";
    }else{
        // Firefox u otro navegador
        margenIzqPestanasDatosColec = "margin-left:10px;";
    }
%>

<%!
    // Funcion para escapar strings para javascript
    private String escape(String str) 
    {
        return StringEscapeUtils.escapeJavaScript(str);
    }
%>
<script type="text/javascript">   
    var tp48solgen;
</script>

<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/estilo.css"/>
<script type="text/javascript" src="<c:url value='/scripts/popup.js'/>"></script>

<div>
    <div class="tab-page" id="tabPage482_solGen" style="height:100%; width: 100%; font-size: 13px;">
        <div style="clear: both;">
            <!--<h2 class="tab"  id="pestana482"><%=meLanbide48I18n.getMensaje(idiomaUsuario,"label.trayectoria.general.textolargo")%></h2>-->
            <div id="tab-panel-482_solGen" class="tab-pane" style="float: left; font-size: 13px;" align="center"></div>
            <script type="text/javascript"> 
                tp48solgen = new WebFXTabPane(document.getElementById("tab-panel-482_solGen"));
                tp48solgen.selectedIndex = 0;
            </script>
            <div class="tab-page" id="tabPage482_solGen_1" style="height: 95%px; font-size: 13px;">
                <h2 class="tab" id="pestana482_solGen_1" style="<%=margenIzqPestanasDatosColec%>"><%=meLanbide48I18n.getMensaje(idiomaUsuario,"label.colectivoyTH.solicitado.colectivo")%></h2>
                <script type="text/javascript">tp48solgen.addTabPage(document.getElementById( "tabPage482_solGen_1" ) );</script>
                <div style="clear: both;">
                    <jsp:include page="<%=urlPestanaDatos_colectivosSolicitados%>" flush="true"/>
                </div>
            </div>
            <div class="tab-page" id="tabPage482_solGen_2" style="height: 95%px; font-size: 13px;">
                <h2 class="tab" id="pestana482_solGen_2" style="<%=margenIzqPestanasDatosColec%>"><%=meLanbide48I18n.getMensaje(idiomaUsuario,"label.colectivoyTH.solicitado.colectivo.compromiso.realizacion")%></h2>
                <script type="text/javascript">tp48solgen.addTabPage(document.getElementById( "tabPage482_solGen_2" ) );</script>
                <div style="clear: both;">
                    <jsp:include page="<%=urlPestanaDatos_compromisoRealxColeYTTHH%>" flush="true"/>
                </div>
            </div>
        </div>
    </div>
</div>
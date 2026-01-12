<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>

<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide47.i18n.MeLanbide47I18n" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide47.vo.comun.SelectItem"%>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide47.util.ConfigurationParameter"%>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide47.util.ConstantesMeLanbide47"%>
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
    MeLanbide47I18n meLanbide47I18n = MeLanbide47I18n.getInstance();
    
    Config m_Config = ConfigServiceHelper.getConfig("common");
    String statusBar = m_Config.getString("JSP.StatusBar");
    String nombreModulo     = request.getParameter("nombreModulo");
    String codOrganizacion  = request.getParameter("codOrganizacionModulo");
    String numExpediente    = request.getParameter("numero");
    String mensajeProgreso = "";
    int anionumExpediente =0;
    anionumExpediente = numExpediente != null ? Integer.parseInt(numExpediente.substring(0,4)):0;

    String urlPestanaDatos_trayectoriaGeneralPresentada = (String)request.getAttribute("urlPestanaDatos_trayectoriaGeneralPresentada");
    String urlPestanaDatos_trayectoriaGeneralValidada = (String)request.getAttribute("urlPestanaDatos_trayectoriaGeneralValidada");
    String urlPestanaDatos_trayectoriaOtrosProgramas = (String)request.getAttribute ("urlPestanaDatos_trayectoriaOtrosProgramas");
    String urlPestanaDatos_trayectoriaActividades = (String)request.getAttribute("urlPestanaDatos_trayectoriaActividades");
 
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
    var tp47trygen;
</script>

<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/estilo.css"/>
<script type="text/javascript" src="<c:url value='/scripts/popup.js'/>"></script>

<body>
    <div class="tab-page" id="tabPageM47trayGen" style="height:480px; width: 100%; font-size: 13px;">
        <div style="clear: both;">
            <div id="tab-panel-M47trayGen" class="tab-pane" style="float: left; font-size: 13px;" align="center"></div>
            <script type="text/javascript">
    tp47trygen = new WebFXTabPane(document.getElementById("tab-panel-M47trayGen"));
    tp47trygen.selectedIndex = 0;
            </script>
            <div class="tab-page" id="tabPageM47trayGen_Pres" style="height: 194px; font-size: 13px;">
                <h2 class="tab" id="pestanaM47trayGen_Pres" style="<%=margenIzqPestanasDatosColec%>"><%=meLanbide47I18n.getMensaje(idiomaUsuario,"label.trayectoria.tituloPestana.presenetado")%></h2>
                <script type="text/javascript">tp47trygen.addTabPage(document.getElementById( "tabPageM47trayGen_Pres" ) );</script>
                <div style="clear: both;">
                    <jsp:include page="<%=urlPestanaDatos_trayectoriaGeneralPresentada%>" flush="true"/>
                </div>
            </div>
            <% if(anionumExpediente>=2018){ %>
            <div class="tab-page" id="tabPageM47trayOtr_Prog" style="height: 194px; font-size: 13px;">
                 <h2 class="tab" id="pestanaM47trayOtr_Prog" style="<%=margenIzqPestanasDatosColec%>"><%=meLanbide47I18n.getMensaje(idiomaUsuario,"label.trayectoria.tituloPestana.otrosProgramas")%></h2>
                 <script type="text/javascript">tp47trygen.addTabPage(document.getElementById( "tabPageM47trayOtr_Prog" ) );</script>
                 <div style="clear: both;">
                    <jsp:include page="<%=urlPestanaDatos_trayectoriaOtrosProgramas%>" flush="true"/>
                </div>
            </div> 
            <div class="tab-page" id="tabPageM47trayActividades" style="height: 194px; font-size: 13px;">
                <h2 class="tab" id="tabPageM47trayActividades" style="<%=margenIzqPestanasDatosColec%>"><%=meLanbide47I18n.getMensaje(idiomaUsuario,"label.trayectoria.tituloPestana.actividades")%></h2>
                <script type="text/javascript">tp47trygen.addTabPage(document.getElementById( "tabPageM47trayActividades" ) );</script>
                <div style="clear: both;">
                    <jsp:include page="<%=urlPestanaDatos_trayectoriaActividades%>" flush="true"/>
                </div>
            </div>      
            <% }%>
            <div class="tab-page" id="tabPageM47trayGen_Val" style="height: 194px; font-size: 13px;">
                <h2 class="tab" id="pestanaM47trayGen_Val" style="<%=margenIzqPestanasDatosColec%>"><%=meLanbide47I18n.getMensaje(idiomaUsuario,"label.trayectoria.tituloPestana.validado")%></h2>
                <script type="text/javascript">tp47trygen.addTabPage(document.getElementById( "tabPageM47trayGen_Val" ) );</script>
                <div style="clear: both;">
                    <jsp:include page="<%=urlPestanaDatos_trayectoriaGeneralValidada%>" flush="true"/>
                </div>
            </div>

        </div>
    </div>
</body>
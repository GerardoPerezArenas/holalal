<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>

<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide47.i18n.MeLanbide47I18n" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide47.vo.comun.SelectItem"%>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide47.util.ConfigurationParameter"%>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide47.util.ConstantesMeLanbide47"%>
<%@page import="es.altia.common.service.config.Config"%>
<%@page import="es.altia.common.service.config.ConfigServiceHelper"%>
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

    String urlPestanaDatos_trayectoriaGrupo1 = (String)request.getAttribute("urlPestanaDatos_trayectoriaGrupo1");
    String urlPestanaDatos_trayectoriaGrupo2 = (String)request.getAttribute("urlPestanaDatos_trayectoriaGrupo2");
    String urlPestanaDatos_trayectoriaGrupo3 = (String)request.getAttribute("urlPestanaDatos_trayectoriaGrupo3");
    String urlPestanaDatos_trayectoriaGrupo4 = (String)request.getAttribute("urlPestanaDatos_trayectoriaGrupo4");
    String urlPestanaDatos_trayectoriaGrupo5 = (String)request.getAttribute("urlPestanaDatos_trayectoriaGrupo5");
    String urlPestanaDatos_trayectoriaGrupo6 = (String)request.getAttribute("urlPestanaDatos_trayectoriaGrupo6");
    String urlPestanaDatos_trayectoriaResumen = (String)request.getAttribute("urlPestanaDatos_trayectoriaResumen");
    
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
    var mensajeValidacion = '';

    function refrescarPestanaTrayectoria() {
        refrescarPestanaGrupo1();
        refrescarPestanaGrupo2();
        refrescarPestanaGrupo3();
        refrescarPestanaGrupo4();
        refrescarPestanaGrupo5();
        refrescarPestanaGrupo6();
    }
    function refrescarPestanaTrayectoria_V() {
        refrescarPestanaResumen();
    }
</script>

<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/estilo.css"/>
<script type="text/javascript" src="<c:url value='/scripts/popup.js'/>"></script>

<body>
    <div id="barraProgresoTrayectoria" style="position: absolute; z-index:10; visibility: hidden; top: 30%; left: 30%;">
        <table width="100%" border="0px" cellpadding="0px" cellspacing="0px" border="0px">
            <tr>
                <td align="center" valign="middle">
                    <table class="contenedorHidePage" cellpadding="0px" cellspacing="0px" border="0px">
                        <tr>
                            <td>
                                <table width="349px" height="100%">
                                    <tr>
                                        <td colspan="3" style="height:70%;text-align:center;valign:middle;">
                                            <span id="msgCopiandoTrayectoria">
                                                <%=meLanbide47I18n.getMensaje(idiomaUsuario, "msg.copiandoDatos")%>
                                            </span>
                                            <span id="msgEliminandoTrayectoria">
                                                <%=meLanbide47I18n.getMensaje(idiomaUsuario, "msg.eliminandoDatos")%>
                                            </span>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td style="width:5%;height:20%;"></td>
                                        <td class="imagenHide "style="height:152px; width: 275px;">
                                            <span id="disco" class="fa fa-spinner fa-spin" aria-hidden="true"/>
                                        </td>
                                        <td style="width:5%;height:20%;"></td>
                                    </tr>
                                    <tr>
                                        <td colspan="3" style="height:10%" ></td>
                                    </tr>
                                </table>
                            </td>
                        </tr>
                    </table>
                </td>
            </tr>
        </table>
    </div>    
    <div class="tab-page" id="tabPageM47trayGen" style="height:480px; width: 100%; font-size: 13px;">
        <div style="text-align: center">
            <label align="center" class="legendAzul" style="text-align: center;height: 20px; margin-top: 10px;"><b><%=meLanbide47I18n.getMensaje(idiomaUsuario,"legend.trayectoria21.general.textoLargo")%></b></label>    
        </div>
        <div style="clear: both;margin-top: 10px;">
            <div id="tab-panel-M47trayGen" class="tab-pane" style="float: left; font-size: 13px;" align="center"></div>
            <script type="text/javascript">
    tp47trygen = new WebFXTabPane(document.getElementById("tab-panel-M47trayGen"));
    tp47trygen.selectedIndex = 0;
            </script>
            <div class="tab-page" id="tabPageM47trayGen_G1" style="height: 194px; font-size: 13px;">
                <h2 class="tab" id="pestanaM47trayGen_G1" style="<%=margenIzqPestanasDatosColec%>"><%=meLanbide47I18n.getMensaje(idiomaUsuario,"label.trayectoria21.tituloPestana.grupo1")%></h2>
                <script type="text/javascript">tp47trygen.addTabPage(document.getElementById("tabPageM47trayGen_G1"));</script>
                <div style="clear: both;">
                    <jsp:include page="<%=urlPestanaDatos_trayectoriaGrupo1%>" flush="true"/>
                </div>
            </div>
            <div class="tab-page" id="tabPageM47trayGen_G2" style="height: 194px; font-size: 13px;">
                <h2 class="tab" id="pestanaM47trayGen_G2" style="<%=margenIzqPestanasDatosColec%>"><%=meLanbide47I18n.getMensaje(idiomaUsuario,"label.trayectoria21.tituloPestana.grupo2")%></h2>
                <script type="text/javascript">tp47trygen.addTabPage(document.getElementById("tabPageM47trayGen_G2"));</script>
                <div style="clear: both;">
                    <jsp:include page="<%=urlPestanaDatos_trayectoriaGrupo2%>" flush="true"/>
                </div>
            </div>
            <div class="tab-page" id="tabPageM47trayGen_G3" style="height: 194px; font-size: 13px;">
                <h2 class="tab" id="pestanaM47trayGen_G3" style="<%=margenIzqPestanasDatosColec%>"><%=meLanbide47I18n.getMensaje(idiomaUsuario,"label.trayectoria21.tituloPestana.grupo3")%></h2>
                <script type="text/javascript">tp47trygen.addTabPage(document.getElementById("tabPageM47trayGen_G3"));</script>
                <div style="clear: both;">
                    <jsp:include page="<%=urlPestanaDatos_trayectoriaGrupo3%>" flush="true"/>
                </div>
            </div>
            <!-- ACTIVIDADES -->  
            <div class="tab-page" id="tabPageM47trayGen_G4" style="height: 194px; font-size: 13px;">
                <h2 class="tab" id="pestanaM47trayGen_G4" style="<%=margenIzqPestanasDatosColec%>"><%=meLanbide47I18n.getMensaje(idiomaUsuario,"label.trayectoria21.tituloPestana.grupo4")%></h2>
                <script type="text/javascript">tp47trygen.addTabPage(document.getElementById("tabPageM47trayGen_G4"));</script>
                <div style="clear: both;">
                    <jsp:include page="<%=urlPestanaDatos_trayectoriaGrupo4%>" flush="true"/>
                </div>
            </div> 
            <div class="tab-page" id="tabPageM47trayGen_G5" style="height: 194px; font-size: 13px;">
                <h2 class="tab" id="pestanaM47trayGen_G5" style="<%=margenIzqPestanasDatosColec%>"><%=meLanbide47I18n.getMensaje(idiomaUsuario,"label.trayectoria21.tituloPestana.grupo5")%></h2>
                <script type="text/javascript">tp47trygen.addTabPage(document.getElementById("tabPageM47trayGen_G5"));</script>
                <div style="clear: both;">
                    <jsp:include page="<%=urlPestanaDatos_trayectoriaGrupo5%>" flush="true"/>
                </div>
            </div>
            <div class="tab-page" id="tabPageM47trayGen_G6" style="height: 194px; font-size: 13px;">
                <h2 class="tab" id="pestanaM47trayGen_G6" style="<%=margenIzqPestanasDatosColec%>"><%=meLanbide47I18n.getMensaje(idiomaUsuario,"label.trayectoria21.tituloPestana.grupo6")%></h2>
                <script type="text/javascript">tp47trygen.addTabPage(document.getElementById("tabPageM47trayGen_G6"));</script>
                <div style="clear: both;">
                    <jsp:include page="<%=urlPestanaDatos_trayectoriaGrupo6%>" flush="true"/>
                </div>
            </div>
            <div class="tab-page" id="tabPageM47trayGen_Resumen" style="height: 194px; font-size: 13px;">
                <h2 class="tab" id="pestanaM47trayGen_Resumen" style="<%=margenIzqPestanasDatosColec%>"><b><%=meLanbide47I18n.getMensaje(idiomaUsuario,"label.trayectoria21.tituloPestana.resumen")%></b></h2>
                <script type="text/javascript">tp47trygen.addTabPage(document.getElementById("tabPageM47trayGen_Resumen"));</script>
                <div style="clear: both;">
                    <jsp:include page="<%=urlPestanaDatos_trayectoriaResumen%>" flush="true"/>
                </div>
            </div>
        </div>
    </div>
</body>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>

<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide47.i18n.MeLanbide47I18n" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide47.vo.comun.SelectItem"%>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide47.vo.orientacion.FilaAsociacionVO"%>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide47.vo.comun.MeLanbideConvocatorias"%>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide47.util.ConfigurationParameter"%>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide47.util.ConstantesMeLanbide47"%>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide47.util.MeLanbide47Utils"%>
<%@page import="es.altia.common.service.config.Config"%>
<%@page import="es.altia.common.service.config.ConfigServiceHelper"%>
<%@page import="java.util.ArrayList" %>
<%@page import="java.util.List" %>
<%@page import="org.apache.commons.lang.StringEscapeUtils"%>

<%
    String sIdioma = request.getParameter("idioma");
    int idiomaUsuario = 1;
    if(sIdioma!=null && sIdioma!="")
        idiomaUsuario=Integer.parseInt(sIdioma);
    int apl = 5;
    String css = "";
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
    MeLanbide47I18n meLanbide47I18n = MeLanbide47I18n.getInstance();
    
    Config m_Config = ConfigServiceHelper.getConfig("common");
    String statusBar = m_Config.getString("JSP.StatusBar");
    String nombreModulo     = request.getParameter("nombreModulo");
    String codOrganizacion  = request.getParameter("codOrganizacionModulo");
    String numExpediente    = request.getParameter("numero");
    String codProcedimiento    = MeLanbide47Utils.getCodProcedimientoDeExpediente(numExpediente);
    Integer ejercicioExpediente    = MeLanbide47Utils.getEjercicioDeExpediente(numExpediente);
    String mensajeProgreso = "";
    
    String urlPestanaDatos_entidad = (String)request.getAttribute("urlPestanaDatos_entidad");
    String urlPestanaDatos_ambitos = (String)request.getAttribute("urlPestanaDatos_ambitos");
    String urlPestanaDatos_ambitos_solicitados = (String)request.getAttribute("urlPestanaDatos_ambitos_solicitados");
    String urlPestanaDatos_trayectoria = (String)request.getAttribute("urlPestanaDatos_trayectoria");
    
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


<jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
<jsp:setProperty name="descriptor"  property="idi_cod" value="<%=idiomaUsuario%>" />
<jsp:setProperty name="descriptor"  property="apl_cod" value="<%=apl%>" />

<link rel="StyleSheet" media="screen" type="text/css" href="<%=request.getContextPath()%><%=css%>">
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/bootstrap413/bootstrap.min.css" media="all"/>
<link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/css/font-awesome.min.css'/>" media="screen" />
<link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/less/animated.less'/>" media="screen" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/>
<link rel="stylesheet" type="text/css" href="<c:url value='/css/extension/melanbide47/melanbide47.css'/>">

<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/bootstrap413/bootstrap.min.js"></script>
<script type="text/javascript" src="<c:url value='/scripts/popup.js'/>"></script>

<script type="text/javascript">
    var tp47;
    var mensajeValidacion = '';

    function refrescarPestanasORI14(pestana) {
        if (pestana != 1) {
            refrescarPestanaEntidad();
        }

        if (pestana != 2) {
            refrescarPestanaAmbitos();
        }

        if (pestana != 3) {
            refrescarPestanaTrayectoria();
            refrescarPestanaTrayectoria_V();
        }

    }
</script>

<body>
    <div id="barraProgresoSolicitudColec" style="position: absolute; z-index:10; visibility: hidden; top: 30%; left: 30%;">
        <table width="100%" border="0px" cellpadding="0px" cellspacing="0px" border="0px">
            <tr>
                <td align="center" valign="middle">
                    <table class="contenedorHidepage" cellpadding="0px" cellspacing="0px" border="0px">
                        <tr>
                            <td>
                                <table width="349px" height="100%">
                                    <tr>
                                        <td colspan="3" style="height:70%;text-align:center;valign:middle;">
                                            <span id="msgGuardandoDatos">
                                                <%=meLanbide47I18n.getMensaje(idiomaUsuario, "msg.guardandoDatos")%>
                                            </span>
                                            <span id="msgCopiandoDatos">
                                                <%=meLanbide47I18n.getMensaje(idiomaUsuario, "msg.copiandoDatos")%>
                                            </span>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td style="width:5%;height:20%;"></td>
                                        <td class="imagenHide"></td>
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
    <div class="tab-page" id="tabPage471" style="height:480px; width: 100%;">
        <h2 class="tab" id="pestana471"><%=meLanbide47I18n.getMensaje(idiomaUsuario,"ori.label.tituloPestana")%></h2>
        <script type="text/javascript">tp1_p471 = tp1.addTabPage(document.getElementById("tabPage471"));</script>
        <div style="clear: both;">
            <div>
                <div style="clear: both; width: 100%; text-align: left; padding-top: 10px;">
                    <div id="tab-panel-471" class="tab-pane" style="float: left;" align="center"></div>
                    <script type="text/javascript">
                        tp471 = new WebFXTabPane(document.getElementById("tab-panel-471"));
                        tp471.selectedIndex = 0;
                    </script>
                    <div class="tab-page" id="tabPage4711" style="height: 194px; font-size: 13px;">
                        <h2 class="tab" id="pestana4711" style="<%=margenIzqPestanasDatosColec%>"><%=meLanbide47I18n.getMensaje(idiomaUsuario,"label.asociacion.tituloPestana")%></h2>
                        <script type="text/javascript">tp471.addTabPage(document.getElementById("tabPage4711"));</script>
                        <div style="clear: both;">
                            <jsp:include page="<%=urlPestanaDatos_entidad%>" flush="true"/>
                        </div>
                    </div>
                    <div class="tab-page" id="tabPage4714" style="height: 194px; font-size: 13px;">
                        <h2 class="tab" id="pestana4714" style="<%=margenIzqPestanasDatosColec%>"><%=meLanbide47I18n.getMensaje(idiomaUsuario,"label.ambitos.solicitados.tituloPestana")%></h2>
                        <script type="text/javascript">tp471.addTabPage(document.getElementById("tabPage4714"));</script>
                        <div style="clear: both;">
                           <jsp:include page="<%=urlPestanaDatos_ambitos_solicitados%>" flush="true"/>
                        </div>
                    </div>
                    <div class="tab-page" id="tabPage4712" style="height: 194px; font-size: 13px;">
                        <h2 class="tab" id="pestana4712" style="<%=margenIzqPestanasDatosColec%>"><%=meLanbide47I18n.getMensaje(idiomaUsuario,"label.ambitos.tituloPestana")%></h2>
                        <script type="text/javascript">tp471.addTabPage(document.getElementById("tabPage4712"));</script>
                        <div style="clear: both;">
                            <jsp:include page="<%=urlPestanaDatos_ambitos%>" flush="true"/>
                        </div>
                    </div>
                    <div class="tab-page" id="tabPage4713" style="height: 194px; font-size: 13px;">
                        <h2 class="tab" id="pestana4713" style="<%=margenIzqPestanasDatosColec%>"><%=meLanbide47I18n.getMensaje(idiomaUsuario,"label.trayectoria.tituloPestana")%></h2>
                        <script type="text/javascript">tp471.addTabPage(document.getElementById("tabPage4713"));</script>
                        <div style="clear: both;">
                            <jsp:include page="<%=urlPestanaDatos_trayectoria%>" flush="true"/>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <!-- Campos Comunes para gestionar desde JS -->
        <input type="hidden" id="urlBaseLlamadaM47" name="urlBaseLlamadaM47" value="<%=request.getContextPath()+"/PeticionModuloIntegracion.do"%>"/>
        <input type="hidden" id="idBDConvocatoriaExpediente" name="idBDConvocatoriaExpediente" value="<c:if test="${convocatoriaActiva!=null && convocatoriaActiva.id!=null}"><c:out value = "${convocatoriaActiva.id}"/></c:if>"/>
        <input type="hidden" id="codigoProcedimiento" name="codigoProcedimiento" value="<%=codProcedimiento%>"/>
        <input type="hidden" id="codigoOrganizacion" name="codigoOrganizacion" value="<%=codOrganizacion%>"/>

    </div>
</body>

<script type="text/javascript">

</script>

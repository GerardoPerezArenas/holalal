<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>

<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide35.i18n.MeLanbide35I18n" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide35.vo.solicitud.EcaSolicitudVO" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@ page import="es.altia.common.service.config.Config"%>
<%@ page import="es.altia.common.service.config.ConfigServiceHelper"%>
<%@page import="java.util.ArrayList" %>
<%@page import="java.util.List" %>

<%
    String sIdioma = request.getParameter("idioma");
    int idiomaUsuario = Integer.parseInt(sIdioma);
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
    MeLanbide35I18n meLanbide35I18n = MeLanbide35I18n.getInstance();
    
    Config m_Config = ConfigServiceHelper.getConfig("common");
    String statusBar = m_Config.getString("JSP.StatusBar");
    String nombreModulo     = request.getParameter("nombreModulo");
    String codOrganizacion  = request.getParameter("codOrganizacionModulo");
    String numExpediente    = request.getParameter("numero");
    
     String [] datosExp = numExpediente!=null ? numExpediente.split("/"):null;
    int ejercicioExpediente = (datosExp!=null && datosExp.length>0 ?  Integer.valueOf(datosExp[0]) : 2019);  // A partir de 2019 son los cambios

    
    String urlPestanaSolicitud_solicitud = (String)request.getAttribute("urlPestanaSolicitud_solicitud");
    String urlPestanaPreparadores_solicitud = (String)request.getAttribute("urlPestanaPreparadores_solicitud");
    String urlPestanaProspectores_solicitud = (String)request.getAttribute("urlPestanaProspectores_solicitud");
    String urlPestanaValoracion_solicitud = (String)request.getAttribute("urlPestanaValoracion_solicitud");
    String urlPestanaResumen_solicitud = (String)request.getAttribute("urlPestanaResumen_solicitud");
    String urlPestanaInsercion_solicitudECA23 = (String)request.getAttribute("urlPestanaInsercion_solicitudECA23");
    String urlPestanaProspector_solicitudECA23 = (String)request.getAttribute("urlPestanaProspector_solicitudECA23");
    String urlPestanaPreparador_solicitudECA23 = (String)request.getAttribute("urlPestanaPreparador_solicitudECA23");
    String urlPestanaSeguimiento_solicitudECA23 = (String)request.getAttribute("urlPestanaSeguimiento_solicitudECA23");
 String urlPestanaResumen_solicitudECA23 = (String)request.getAttribute("urlPestanaResumen_solicitudECA23");
    //EcaSolicitudVO ecaSolicitud = (EcaSolicitudVO)request.getAttribute("ecaSolicitud");
    
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

<jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
<jsp:setProperty name="descriptor"  property="idi_cod" value="<%=idiomaUsuario%>" />
<jsp:setProperty name="descriptor"  property="apl_cod" value="<%=apl%>" />
<link rel="StyleSheet" media="screen" type="text/css" href="<%=request.getContextPath()%><%=css%>">
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/extension/melanbide35/melanbide35.css"/>

<script type="text/javascript">   
    var tp35_sol;
    
        
    
    function ocultarPestanasSolicitudEca(){
        tp35_sol.hideTabPage(1);
        tp35_sol.hideTabPage(2);
        tp35_sol.hideTabPage(3);
    }
    
    function mostrarPestanasSolicitudEca(){
        tp35_sol.showTabPage(1);
        tp35_sol.showTatbPage(2);
        tp35_sol.showTabPage(3);
    }
    
    function descargarPlantillaEca(nombrePlantilla){
        var CONTEXT_PATH = '<%=request.getContextPath()%>'
        var url = CONTEXT_PATH + "/PeticionModuloIntegracion.do";
        var parametros = "";
        var control = new Date();
        parametros = '?tarea=preparar&modulo=MELANBIDE35&operacion=descargarPlantilla&tipo=0&nombrePlantilla='+nombrePlantilla+'&control='+control.getTime();
        inicio = false;
        window.open(url+parametros, "_blank");
    }
    
    function actualizarOtrasPestanasEca(pestana){
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
        
        if(pestana != 1){
            actualizarDatosAnexosSolicitud(false);
        }
        
        if(pestana != 2){
            actualizarTablaPreparadoresSolicitudEca(false);
        }
        
        if(pestana != 3){
            actualizarTablaProspectoresSolicitudEca(false);
        }
        
        if(pestana != 4){
            
        }
        
        if(pestana != 5){
            actualizarTablaPreparadoresJustificacionEca(false);
        }
        
        if(pestana != 6){
            actualizarTablaProspectoresJustificacionEca(false);
        }
        
        if(pestana != 7){
            refrescarPantallaResumen(); 
        }
    }
</script>

<body>
    <div class="tab-page" id="tabPage351" style="height:520px; width: 100%;">
        <h2 class="tab" id="pestana351"><%=meLanbide35I18n.getMensaje(idiomaUsuario,"label.solicitud.solicitud.tituloPestana")%></h2>
        <script type="text/javascript">tp1.addTabPage( document.getElementById( "tabPage351" ) );</script>
        <div style="clear: both;">
            <div id="tab-panel-35_sol" class="tab-pane" style="float: left;" align="center"></div>
            <script type="text/javascript"> 
                tp35_sol = new WebFXTabPane(document.getElementById("tab-panel-35_sol"));
                tp35_sol.selectedIndex = 0;
            </script>
            <div class="tab-page" id="tabPage35_sol_1" style="height: 480px;">
                <h2 class="tab" id="pestana35_sol_1" style="<%=margenIzqPestanasSolicitud%>"><%=meLanbide35I18n.getMensaje(idiomaUsuario,"label.solicitud.solicitud.tituloPestana")%></h2>
                <script type="text/javascript">tp35_sol.addTabPage( document.getElementById( "tabPage35_sol_1" ) );</script>
                <div style="clear: both; padding-top: 14px;">
                    <jsp:include page="<%=urlPestanaSolicitud_solicitud%>" flush="true"/>
                </div>
            </div>
            <div class="tab-page" id="tabPage35_sol_2" style="height: 480px;">
                <h2 class="tab" id="pestana35_sol_2"><%=meLanbide35I18n.getMensaje(idiomaUsuario,"label.solicitud.preparadores.tituloPestana")%></h2>
                <script type="text/javascript">tp35_sol.addTabPage( document.getElementById( "tabPage35_sol_2" ) );</script>
                <div style="clear: both; padding-top: 14px;">
                    <jsp:include page="<%=urlPestanaPreparadores_solicitud%>" flush="true"/>
                </div>
            </div>
            <div class="tab-page" id="tabPage35_sol_3" style="height: 480px;">
                <h2 class="tab" id="pestana35_sol_3"><%=meLanbide35I18n.getMensaje(idiomaUsuario,"label.solicitud.prospectores.tituloPestana")%></h2>
                <script type="text/javascript">tp35_sol.addTabPage( document.getElementById( "tabPage35_sol_3" ) );</script>
                <div style="clear: both; padding-top: 14px;">
                    <jsp:include page="<%=urlPestanaProspectores_solicitud%>" flush="true"/>
                </div>
            </div>
            <% if(ejercicioExpediente<2019){ %>  
            <div class="tab-page" id="tabPage35_sol_4" style="height: 480px;">
                <h2 class="tab" id="pestana35_sol_4"><%=meLanbide35I18n.getMensaje(idiomaUsuario,"label.solicitud.valoracion.tituloPestana")%></h2>
                <script type="text/javascript">tp35_sol.addTabPage( document.getElementById( "tabPage35_sol_4" ) );</script>
                <div style="clear: both; padding-top: 14px;">
                    <jsp:include page="<%=urlPestanaValoracion_solicitud%>" flush="true"/>
                </div>
            </div>
            <% } %>   
        </div>
    </div>
</body>

<script type="text/javascript">   
    //  configurarPestanas();
</script>

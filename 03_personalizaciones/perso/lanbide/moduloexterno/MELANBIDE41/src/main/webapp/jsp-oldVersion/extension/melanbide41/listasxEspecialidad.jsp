<%@taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@taglib uri="http://java.sun.com/jstl/core" prefix="c" %>

<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide41.i18n.MeLanbide41I18n" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.common.service.config.Config"%>
<%@page import="es.altia.common.service.config.ConfigServiceHelper"%>
<%@page import="java.util.ArrayList" %>
<%@page import="java.util.List" %>

<%
    int idiomaUsuario = 1;
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
    String urlPestanaIdentificacionEsp = (String)request.getAttribute("urlPestanaIdentificacionEsp");
    String urlPestanaCapacidad = (String)request.getAttribute("urlPestanaCapacidad");
    String urlPestanaDotacion = (String)request.getAttribute("urlPestanaDotacion");
    String urlPestanaMaterial = (String)request.getAttribute("urlPestanaMaterial");
    
    
    //EcaSolicitudVO ecaSolicitud = (EcaSolicitudVO)request.getAttribute("ecaSolicitud");
    
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

<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/estilo.css"/>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/extension/melanbide41/melanbide41.css"/>

<script type="text/javascript" src="<c:url value='/scripts/general.js'/>"></script>
<script type="text/javascript" src="<c:url value='/scripts/validaciones.js'/>"></script>
<script type="text/javascript" src="<c:url value='/scripts/calendario.js'/>"></script>
<script type="text/javascript" src="<c:url value='/scripts/listas.js'/>"></script>
<script type="text/javascript" src="<c:url value='/scripts/TablaNueva.js'/>"></script>
<script type="text/javascript" src="<c:url value='/scripts/tabpane.js'/>"></script>
<script type="text/javascript" src="<c:url value='/scripts/listaComboBoxScroll.js'/>"></script>
<script type="text/javascript" src="<c:url value='/scripts/gestionTerceros.js'/>"></script>

<script type="text/javascript">   
    
    var tp41_esprec;
    
    function configurarPestanas(){        
        mostrarPestanaEpecialidadesRecursos();        
    }
    
    function ocultarPestanaEpecialidadesRecursos(){
        tp41_esprec.hideTabPage(1);
        //tp41_esprec.hideTabPage(2);
    }
    
    function mostrarPestanaEpecialidadesRecursos(){
        tp41_esprec.showTabPage(1);
        //tp41_esprec.showTabPage(2);
    }
</script>

<body>
    <div class="tab-page" id="tabPage353" style="height:480px; width: 100%; margin-left:5px;">
        <h2 class="legendAzul" id="pestana353" style="clear: both; text-align: center;" ><%=meLanbide41I18n.getMensaje(idiomaUsuario,"label.especialidadades.tituloListas")%></h2>
        <div style="clear: both;">
            <div id="tab-panel-41_esprec" class="tab-pane" style="float: left;" align="center"></div>
            <script type="text/javascript"> 
                tp41_esprec = new WebFXTabPane(document.getElementById("tab-panel-41_esprec"));
                tp41_esprec.selectedIndex = 0;
            </script>            
            <div class="tab-page" id="tabPage41_lisidesp" style="height: 360px;">
                <h2 class="tab" id="pestana41_lisidesp"><%=meLanbide41I18n.getMensaje(idiomaUsuario,"label.especialidadesRecursos.listaidentificacionesp.tituloPestana")%></h2>
                <script type="text/javascript">tp41_esprec.addTabPage( document.getElementById( "tabPage41_lisidesp" ) );</script>
                <div style="clear: both; padding-top: 14px; border:1px solid; border-color: gray; height:100%; width:95%;">
                    <jsp:include page="<%=urlPestanaIdentificacionEsp%>" flush="true"/>
                </div>
            </div>
            <div class="tab-page" id="tabPage41_lisCap" style="height: 310px;">
                <h2 class="tab" id="pestana41_liscap"><%=meLanbide41I18n.getMensaje(idiomaUsuario,"label.especialidadesRecursos.listacapacidad.tituloPestana")%></h2>
                <script type="text/javascript">tp41_esprec.addTabPage( document.getElementById( "tabPage41_lisCap" ) );</script>
                <div style="clear: both; padding-top: 14px; border:1px solid; border-color: gray; height:100%; width:95%;">
                    <jsp:include page="<%=urlPestanaCapacidad%>" flush="true"/>
                </div>
            </div>
            <div class="tab-page" id="tabPage41_lisdot" style="height: 310px;">
                <h2 class="tab" id="pestana41_lisdot"><%=meLanbide41I18n.getMensaje(idiomaUsuario,"label.especialidadesRecursos.listadotacion.tituloPestana")%></h2>
                <script type="text/javascript">tp41_esprec.addTabPage( document.getElementById( "tabPage41_lisdot" ) );</script>
                <div style="clear: both; padding-top: 14px; border:1px solid; border-color: gray; height:100%; width:95%;">
                    <jsp:include page="<%=urlPestanaDotacion%>" flush="true"/>
                </div>
            </div>
            <!--div class="tab-page" id="tabPage41_lismat" style="display: none">
                <h2 class="tab" id="pestana41_lismat"><%=meLanbide41I18n.getMensaje(idiomaUsuario,"label.especialidadesRecursos.listamaterial.tituloPestana")%></h2>
                <script type="text/javascript">tp41_esprec.addTabPage( document.getElementById( "tabPage41_lismat" ) );</script>
                <div style="clear: both; padding-top: 14px; border:1px solid; border-color: gray; height:100%; width:95%;">
                    
                </div>
            </div-->
        </div>
    </div>
</body>

<script type="text/javascript">   
    configurarPestanas();
</script>
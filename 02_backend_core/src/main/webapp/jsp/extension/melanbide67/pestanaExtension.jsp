<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>

<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide67.i18n.MeLanbide67I18n" %>

<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide67.vo.SelectItem"%>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide67.vo.DatosPestanaVO" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide67.util.ConfigurationParameter" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide67.util.ConstantesMeLanbide67" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide67.lanbide.wsclients.langaivision360.enums.GeneratedDocuments" %>
<%@page import="es.altia.common.service.config.Config"%>
<%@page import="es.altia.common.service.config.ConfigServiceHelper"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.ArrayList" %>
<%@page import="java.util.List" %>
<%@page import="org.apache.commons.lang.StringEscapeUtils"%>
<%@page import="es.altia.agora.business.util.GeneralValueObject"%>
<%@page import="java.util.*" %>
<%@page import="java.math.*" %>
<%@page import="java.text.*" %>

<%

    String sIdioma = request.getParameter("idioma");
    int idiomaUsuario = Integer.parseInt(sIdioma);
    int apl = 5;
    String css = "";
    UsuarioValueObject usuario = new UsuarioValueObject();
    try{
        if (session != null){
            if (usuario != null){
                usuario = (UsuarioValueObject) session.getAttribute("usuario");
                idiomaUsuario = usuario.getIdioma();
                apl = usuario.getAppCod();
                css = usuario.getCss();
            }
        }
    }catch(Exception ex){
        
    }    

    //Clase para internacionalizar los mensajes de la aplicacion.
    MeLanbide67I18n meLanbide67I18n = MeLanbide67I18n.getInstance();
    
    Config m_Config = ConfigServiceHelper.getConfig("common");
    String statusBar = m_Config.getString("JSP.StatusBar");
    String nombreModulo     = request.getParameter("nombreModulo");
    String codOrganizacion  = request.getParameter("codOrganizacionModulo");
    String numExpediente    = request.getParameter("numero");
%>

<jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
<jsp:setProperty name="descriptor"  property="idi_cod" value="<%=idiomaUsuario%>" />
<jsp:setProperty name="descriptor"  property="apl_cod" value="<%=apl%>" />
<link rel="StyleSheet" media="screen" type="text/css" href="<%=request.getContextPath()%><%=css%>">
<link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/css/font-awesome.min.css'/>" media="screen">
<link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/less/animated.less'/>" media="screen">
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/>
<link rel="stylesheet" type="text/css" href="<c:url value='/css/extension/melanbide67/melanbide67.css'/>">
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script>
<script type="text/javascript" src="<c:url value='/scripts/popup.js'/>"></script>
<script type="text/javascript" src="<c:url value='/scripts/listaComboBox.js'/>"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/melanbide67/leaukUtils.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/melanbide67/lanbide.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/general.js"></script>

<script type="text/javascript">
    var APP_CONTEXT_PATH = '<%=request.getContextPath()%>';

    var parametrosLLamadaM67 = {
        tarea: 'preparar'
        , modulo: 'MELANBIDE67'
        , operacion: null
        , tipo: 0
        , numero: null
    };
    
    function getXMLHttpRequest() {
        var aVersions = ["MSXML2.XMLHttp.5.0",
            "MSXML2.XMLHttp.4.0", "MSXML2.XMLHttp.3.0",
            "MSXML2.XMLHttp", "Microsoft.XMLHttp"
        ];

        if (window.XMLHttpRequest) {
            // para IE7, Mozilla, Safari, etc: que usen el objeto nativo
            return new XMLHttpRequest();
        } else if (window.ActiveXObject) {
            // de lo contrario utilizar el control ActiveX para IE5.x y IE6.x
            for (var i = 0; i < aVersions.length; i++) {
                try {
                    var oXmlHttp = new ActiveXObject(aVersions[i]);
                    return oXmlHttp;
                } catch (error) {
                    //no necesitamos hacer nada especial
                }
            }
        }
    }

    function pulsarDatosIntermediacion() {
        pleaseWait('on');

        var dataParameter = $.extend({}, parametrosLLamadaM67);

        dataParameter.numero = document.forms[0].numero.value;
        dataParameter.control = new Date().getTime();
        dataParameter.operacion = 'recuperarDatosIntermediacion';

        var urlBaseLlamada = APP_CONTEXT_PATH + "/PeticionModuloIntegracion.do";

        $.ajax({
            type: 'POST',
            url: urlBaseLlamada,
            data: dataParameter,
            success: function (respuesta) {
                if (respuesta !== null) {
                    pleaseWait('off');

                    if (respuesta == "0") {
                        recargarDatosExpediente();
                    } else if (respuesta == "1") {
                        jsp_alerta("A", '<%=meLanbide67I18n.getMensaje(idiomaUsuario,"error.errorPersonaContratada.noNIF")%>');
                    } else if (respuesta == "2") {
                        jsp_alerta("A", '<%=meLanbide67I18n.getMensaje(idiomaUsuario,"error.errorPersonaContratada.noNumOferta")%>');
                    } else if (respuesta == "3") {
                        jsp_alerta("A", '<%=meLanbide67I18n.getMensaje(idiomaUsuario,"error.errorPersonaContratada.noNIF_noNumOferta")%>');
                    } else if (respuesta == "4") {
                        jsp_alerta("A", '<%=meLanbide67I18n.getMensaje(idiomaUsuario,"error.errorPersonaContratada.noCorrespondencia")%>');
                    }
                } else {
                    jsp_alerta("A", '<%=meLanbide67I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                }
            },
            error: function (jqXHR, textStatus, errorThrown) {
                jsp_alerta("A", '<%=meLanbide67I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
            },
            async: true
        });
    }
    
    function pulsarDatosCVIntermediacion() {
        pleaseWait('on');

        var dataParameter = $.extend({}, parametrosLLamadaM67);

        dataParameter.numero = document.forms[0].numero.value;
        dataParameter.control = new Date().getTime();
        dataParameter.operacion = 'recuperarDatosCVIntermediacion';

        var urlBaseLlamada = APP_CONTEXT_PATH + "/PeticionModuloIntegracion.do";

        $.ajax({
            type: 'POST',
            url: urlBaseLlamada,
            data: dataParameter,
            success: function (respuesta) {
                console.log("pulsarDatosCVIntermediacion respuesta = " + respuesta);
                if (respuesta !== null) {
                    pleaseWait('off');
                    if (respuesta.startsWith("https")) {
                        recargarDatosExpediente();
                    } else if (respuesta == "1") {
                        jsp_alerta("A", '<%=meLanbide67I18n.getMensaje(idiomaUsuario,"error.NotAvailableCV")%>'); 
                    } else if (respuesta == "-2") {
                        jsp_alerta("A", '<%=meLanbide67I18n.getMensaje(idiomaUsuario,"error.WSCVServiceNotAvailable")%>');
                    }
                } else {
                    jsp_alerta("A", '<%=meLanbide67I18n.getMensaje(idiomaUsuario,"error.WSCVServiceNotAvailable")%>'); 
                    pleaseWait('off');
                } 
            },
            error: function (jqXHR, textStatus, errorThrown) {
                jsp_alerta("A", '<%=meLanbide67I18n.getMensaje(idiomaUsuario,"error.WSCVServiceNotAvailable")%>');
                pleaseWait('off');
            },
            async: true
        });
    }
    
    function pulsarCertificadoDemanda() {
        pleaseWait('on');

        var dataParameter = $.extend({}, parametrosLLamadaM67);

        dataParameter.numero = document.forms[0].numero.value;
        dataParameter.control = new Date().getTime();
        dataParameter.operacion = 'recuperarDatosDemandaIntermediacion';

        var urlBaseLlamada = APP_CONTEXT_PATH + "/PeticionModuloIntegracion.do";

        $.ajax({
            type: 'POST',
            url: urlBaseLlamada,
            data: dataParameter,
            success: function (respuesta) {
                console.log("pulsarCertificadoDemanda respuesta = " + respuesta);
                if (respuesta !== null) {
                    pleaseWait('off');
                    if (respuesta.startsWith("https")) {
                        recargarDatosExpediente();
                    } else if (respuesta == "1") {
                        jsp_alerta("A", '<%=meLanbide67I18n.getMensaje(idiomaUsuario,"error.NotAvailableDemand")%>'); 
                    } else if (respuesta == "-2") {
                        jsp_alerta("A", '<%=meLanbide67I18n.getMensaje(idiomaUsuario,"error.WSDemandServiceNotAvailable")%>');
                    }
                } else {
                    jsp_alerta("A", '<%=meLanbide67I18n.getMensaje(idiomaUsuario,"error.WSDemandServiceNotAvailable")%>'); 
                    pleaseWait('off');
                } 
            },
            error: function (jqXHR, textStatus, errorThrown) {
                jsp_alerta("A", '<%=meLanbide67I18n.getMensaje(idiomaUsuario,"error.WSDemandServiceNotAvailable")%>');
                pleaseWait('off');
            },
            async: true
        });
    }    

    function recargarDatosExpediente() {
        document.forms[0].opcion.value = "cargarPestTram";
        document.forms[0].target = "mainFrame";
        document.forms[0].action = "<c:url value='/sge/FichaExpediente.do'/>";
        document.forms[0].submit();
    }

</script>

<body>
    <div class="tab-page" id="tabPageExtension" style="height:480px; width: 100%;">
        <h2 class="tab" id="pestanaDatosEntidadColaboradora"><%=meLanbide67I18n.getMensaje(idiomaUsuario,"label.extension.tituloPestana")%></h2>
        <script type="text/javascript">tp1.addTabPage(document.getElementById("tabPageExtension"));</script>        

        <div id="spin" style="display: none;">
            <i class="fa fa-spinner fa-spin" style="font-size:24px"></i>
        </div>

        <div id="divCuerpo" style="overflow-y: auto;">
            <form  id="formBusqueda">

                <div id="barraProgresoBusqueda" style="position: absolute; z-index:10; visibility: hidden; top: 30%; left: 30%;">
                    <table width="100%" border="0px" cellpadding="0px" cellspacing="0px" border="0px">
                        <tr>
                            <td align="center" valign="middle">
                                <table class="contenedorHidepage" cellpadding="0px" cellspacing="0px" border="0px">
                                    <tr>
                                        <td>
                                            <table width="349px" height="100%">
                                                <tr>
                                                    <td colspan="3" style="height:70%;text-align:center;vertical-align: middle ;text-height: max-size">
                                                        <span id="msgBuscando">
                                                            <%=meLanbide67I18n.getMensaje(idiomaUsuario, "msg.procesando")%>
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

                <div style="clear: both;">
                    <label><%=meLanbide67I18n.getMensaje(idiomaUsuario,"label.lak.entidadColaboradora.titulo")%></label>
                    </br>
                    <div style="text-align: left; margin-left: 50px" class="etiqueta">
                        <input type= "button" id="botonDatosIntermediacion" name="botonDatosIntermediacion" class="botonMasLargo" value="<%=meLanbide67I18n.getMensaje(idiomaUsuario,"label.extension.boton.datosIntermediacion")%>" onclick="pulsarDatosIntermediacion();" >
                        </input>
                    </div>
                </div>        
                <div style="clear: both;">
                    <label><%=meLanbide67I18n.getMensaje(idiomaUsuario,"label.lak.entidadColaboradora.titulo")%></label>
                    </br>
                    <div style="text-align: left; margin-left: 50px" class="etiqueta">
                        <input type= "button" id="botonDatosCVIntermediacion" name="botonDatosCVIntermediacion" class="botonMasLargo" value="<%=meLanbide67I18n.getMensaje(idiomaUsuario,"label.extension.boton.datosCVIntermediacion")%>" onclick="pulsarDatosCVIntermediacion();" >
                        </input>
                    </div>
                </div>  
                <div style="clear: both;">
                    <label><%=meLanbide67I18n.getMensaje(idiomaUsuario,"label.lak.entidadColaboradora.titulo")%></label>
                    </br>
                    <div style="text-align: left; margin-left: 50px" class="etiqueta">
                        <input type= "button" id="botonCertificadoDemanda" name="botonCertificadoDemanda"  class="botonMasLargo" value="<%=meLanbide67I18n.getMensaje(idiomaUsuario,"label.extension.boton.botonCertificadoDemanda")%>" onclick="pulsarCertificadoDemanda();" >
                        </input>
                    </div>
                </div>                         
            </form>
        </div>

    </div>   
</body>

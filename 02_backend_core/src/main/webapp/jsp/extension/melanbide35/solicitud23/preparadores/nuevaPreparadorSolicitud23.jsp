<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>

<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide35.i18n.MeLanbide35I18n" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide35.vo.solicitud.EcaSolPreparadoresVO" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide35.vo.comun.EcaConfiguracionVO" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide35.util.ConstantesMeLanbide35" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide35.vo.solicitud.FilaPreparadorSolicitudVO" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide35.util.MeLanbide35Utils" %>
<%@page import="java.text.SimpleDateFormat" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>

<%
    int idiomaUsuario = 1;
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
    catch(Exception ex) {        
    }
    
    SimpleDateFormat format = new SimpleDateFormat(ConstantesMeLanbide35.FORMATO_FECHA);
    
    //Clase para internacionalizar los mensajes de la aplicacion.
    MeLanbide35I18n meLanbide35I18n = MeLanbide35I18n.getInstance();
    String numExpediente    = request.getParameter("numero");
    
    
    Boolean consulta = false;
    String tituloPagina = "";
    if(consulta) {
        tituloPagina = meLanbide35I18n.getMensaje(idiomaUsuario,"label.solicitud.preparadores.consultaPreparador.tituloPagina");
    } else {        
        tituloPagina = meLanbide35I18n.getMensaje(idiomaUsuario,"label.solicitud.preparadores.nuevoPreparador.tituloPagina");        
    }
    
    Integer anoExpediente = MeLanbide35Utils.getEjercicioDeExpediente(numExpediente);
%>
<title><%=tituloPagina%></title>
<jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
<jsp:setProperty name="descriptor"  property="idi_cod" value="<%=idiomaUsuario%>" />
<jsp:setProperty name="descriptor"  property="apl_cod" value="<%=apl%>" />
<link rel="StyleSheet" media="screen" type="text/css" href="<%=request.getContextPath()%><%=css%>">
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/>
<link rel="stylesheet" type="text/css" href="<c:url value='/css/extension/melanbide35/melanbide35.css'/>">

<script type="text/javascript">
    var APP_CONTEXT_PATH = '<%=request.getContextPath()%>';
</script>


<script type="text/javascript" src="<c:url value='/scripts/popup.js'/>"></script>
<script type="text/javascript" src="<c:url value='/scripts/general.js'/>"></script>
<script type="text/javascript" src="<c:url value='/scripts/validaciones.js'/>"></script>
<script type="text/javascript" src="<c:url value='/scripts/calendario.js'/>"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/melanbide35/ecaUtils.js"></script>

<script type="text/javascript">

    var mensajeValidacion = '';
    var nuevo = true;

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
        } else {
            return null;
        }
    }

    function inicio() {

    }

    function cancelar() {
        var resultado = jsp_alerta("", "<%=meLanbide35I18n.getMensaje(idiomaUsuario, "mens.preguntaCancelar")%>");
        if (resultado == 1) {
            cerrarVentana();
        }
    }


    function cerrarVentana() {
        if (navigator.appName == "Microsoft Internet Explorer") {
            window.parent.window.opener = null;
            window.parent.window.close();
        } else if (navigator.appName == "Netscape") {
            top.window.opener = top;
            top.window.open('', '_parent', '');
            top.window.close();
        } else {
            window.close();
        }
    }

    function guardar() {
        if (validarDatos()) {

            document.getElementById('msgGuardandoDatos').style.display = "inline";
            barraProgresoEca('on', 'barraProgresoNuevoPreparadorSolicitud');
            var ajax = getXMLHttpRequest();
            var nodos = null;
            var CONTEXT_PATH = '<%=request.getContextPath()%>';
            var url = CONTEXT_PATH + "/PeticionModuloIntegracion.do";
            var parametros = "";
            var control = new Date();
            parametros = 'tarea=preparar&modulo=MELANBIDE35&operacion=guardarPreparadorSolicitud23&tipo=0&numero=<%=numExpediente%>'
                    + '&numeroExpediente=<%=numExpediente%>'
                    + '&nombre=' + document.getElementById('nombre').value
                    + '&dni=' + document.getElementById('dni').value
                    + '&horaseca=' + document.getElementById('horaseca').value
                    + '&control=' + control.getTime();
            try {
                ajax.open("POST", url, false);
                ajax.setRequestHeader("Content-Type", "application/x-www-form-urlencoded; charset=iso-8859-15");
                ajax.setRequestHeader("Accept", "text/xml, application/xml, text/plain");
                //var formData = new FormData(document.getElementById('formContrato'));
                ajax.send(parametros);
                if (ajax.readyState == 4 && ajax.status == 200) {
                    var xmlDoc = null;
                    if (navigator.appName.indexOf("Internet Explorer") != -1) {
                        // En IE el XML viene en responseText y no en la propiedad responseXML
                        var text = ajax.responseText;
                        xmlDoc = new ActiveXObject("Microsoft.XMLDOM");
                        xmlDoc.async = "false";
                        xmlDoc.loadXML(text);
                    } else {
                        // En el resto de navegadores el XML se recupera de la propiedad responseXML
                        xmlDoc = ajax.responseXML;
                    }//if(navigator.appName.indexOf("Internet Explorer")!=-1)
                }//if (ajax.readyState==4 && ajax.status==200)
                nodos = xmlDoc.getElementsByTagName("RESPUESTA");
                var elemento = nodos[0];
                var codigoOperacion = null;
                var msgValidacion = '';
                nodos = xmlDoc.getElementsByTagName("RESPUESTA");
                var codigoOperacion = nodos[0].firstChild.textContent;

                //for(j=0;hijos!=null && j<hijos.length;j++)
                if (codigoOperacion == "0") {
                    self.parent.opener.retornoXanelaAuxiliar();
                    barraProgresoEca('off', 'barraProgresoNuevoPreparadorSolicitud');
                    jsp_alerta("A", '<%=meLanbide35I18n.getMensaje(idiomaUsuario,"msg.datosGuardadosOK")%>');
                    cerrarVentana();
                } else if (codigoOperacion == "1") {
                    jsp_alerta("A", '<%=meLanbide35I18n.getMensaje(idiomaUsuario,"error.errorBD")%>');
                } else if (codigoOperacion == "2") {
                    jsp_alerta("A", '<%=meLanbide35I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                } else if (codigoOperacion == "3") {
                    jsp_alerta("A", '<%=meLanbide35I18n.getMensaje(idiomaUsuario,"error.pasoParametros")%>');
                } else if (codigoOperacion == "4") {
                    jsp_alerta("A", msgValidacion);
                } else if (codigoOperacion == "5") {
                    jsp_alerta("A", '<%=meLanbide35I18n.getMensaje(idiomaUsuario,"error.nifPreparadorRepetido")%>');
                } else {
                    jsp_alerta("A", '<%=meLanbide35I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                }//if(
            } catch (Err) {
                jsp_alerta("A", '<%=meLanbide35I18n.getMensaje(idiomaUsuario,"error.errorProcesarRespuesta")%>');
            }//try-catch

            barraProgresoEca('off', 'barraProgresoNuevoPreparadorSolicitud');
            cerrarVentana();
        } else {
            jsp_alerta("A", mensajeValidacion);
        }
    }

    function validarDatos() {
        var correcto = true;
        mensajeValidacion = '';
        if (document.getElementById('nombre').value != '') {

        } else {
            correcto = false;
            document.getElementById('nombre').style.border = '1px solid red';
            if (mensajeValidacion == '')
                mensajeValidacion = '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "msg.solicitud.preparador.nombre")%>';

        }
        if (document.getElementById('dni').value != '') {
            if (!validarNIFEca(document.getElementById('dni'))) {
                correcto = false;
                if (mensajeValidacion == '')
                    mensajeValidacion = '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "msg.solicitud.preparadores.nifIncorrecto")%>';
            }
        } else {
            correcto = false;
            document.getElementById('dni').style.border = '1px solid red';
            if (mensajeValidacion == '')
                mensajeValidacion = '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "msg.solicitud.preparador.dni")%>';
        }

        if (document.getElementById('horaseca').value != '') {
            try {
                if (!validarNumericoEntero(document.getElementById('horaseca'))) {
                    correcto = false;
                    document.getElementById('horaseca').style.border = '1px solid red';
                    if (mensajeValidacion == '')
                        mensajeValidacion = '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "msg.solicitud.preparadores.horasContratoIncorrecto")%>';
                }
            } catch (err) {
                correcto = false;
                document.getElementById('horaseca').style.border = '1px solid red';
                if (mensajeValidacion == '')
                    mensajeValidacion = '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "msg.solicitud.preparadores.horasContratoIncorrecto")%>';
            }
        }
        return correcto;
    }

</script>
<body onload="inicio();" class="contenidoPantalla">
    <form>
        <div id="barraProgresoNuevoPreparadorSolicitud" style="position: absolute; z-index:10; visibility: hidden; top: 30%; left: 30%;">
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
                                                    <%=meLanbide35I18n.getMensaje(idiomaUsuario, "msg.guardandoDatos")%>
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
        <div style="width: 100%; padding: 10px; text-align: left;">
            <div class="sub3titulo" style="text-align: left;width: 95%;">
                <span>
                    <%=meLanbide35I18n.getMensaje(idiomaUsuario,"label.solicitud.preparadores.datosPreparador")%>
                </span>
            </div>
            <fieldset style="width: 94%;">    
                <legend><%=meLanbide35I18n.getMensaje(idiomaUsuario,"label.resumen.detalle.inserciones.preparador")%></legend>
                <div class="lineaFormulario">
                    <div style="float: left; width: 150px;"class="etiqueta">
                        <%=meLanbide35I18n.getMensaje(idiomaUsuario,"label.solicitud.preparadores.nomApel")%><font color="red">*</font>
                    </div>
                    <div style="float: left;">
                        <input type="text" id="nombre" name="nombre" size="50" maxlength="150" class="inputTexto"/>
                    </div>
                </div>
                <div class="lineaFormulario">
                    <div style="float: left; width: 150px;"class="etiqueta">
                        <%=meLanbide35I18n.getMensaje(idiomaUsuario,"label.solicitud.preparadores.nif")%><font color="red">*</font>
                    </div>
                    <div style="float: left;">
                        <input type="text" id="dni" name="dni" size="10" maxlength="10" class="inputTexto"/>
                    </div>
                </div>               

                <div class="lineaFormulario">
                    <div style="float: left; width: 150px;"class="etiqueta">
                        <%=meLanbide35I18n.getMensaje(idiomaUsuario,"label.solicitud.preparadores.horasDedicacionEca")%><font color="red">*</font>
                    </div>
                    <div style=" float: left;">
                        <input type="text" id="horaseca" name="horaseca" size="10" maxlength="10" class="inputTexto"/>
                    </div>
                </div>               
            </fieldset>
        </div>
        <div class="botonera" style="padding-top: 20px;">
            <input type="button" id="btnAceptar" name="btnAceptar" class="botonGeneral" value="<%=meLanbide35I18n.getMensaje(idiomaUsuario, "btn.general.aceptar")%>" onclick="guardar();">
            <input type="button" id="btnCerrar" name="btnCerrar" class="botonGeneral" value="<%=meLanbide35I18n.getMensaje(idiomaUsuario, "btn.general.cerrar")%>" onclick="cerrarVentana();">
        </div>
    </form>
</body>

<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>

<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide47.i18n.MeLanbide47I18n" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.common.service.config.Config"%>
<%@page import="es.altia.common.service.config.ConfigServiceHelper"%>
<%@page import="java.util.ArrayList" %>
<%@page import="java.util.List" %>
<%@page import="java.util.Iterator" %>
<%@page import="java.util.Map" %>
<%@page import="java.text.DateFormat" %>
<%@page import="java.text.SimpleDateFormat" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide47.vo.comun.SelectItem"%>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide47.vo.orientacion.OriTrayectoriaEntidadVO"%>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide47.util.ConstantesMeLanbide47"%>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide47.util.MeLanbide47Utils"%>
<%@page import="org.apache.commons.lang.StringEscapeUtils"%>

<%
    String sIdioma = request.getParameter("idioma");
    int idiomaUsuario = (sIdioma!=null && sIdioma!="" ? Integer.parseInt(sIdioma) : 1);
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
    String mensajeProgreso = "";
    
    String tituloPagina = (String)request.getAttribute("tituloPagina") != null ? (String)request.getAttribute("tituloPagina") : "";
    String tituloCaja = (String)request.getAttribute("tituloCaja") != null ? (String)request.getAttribute("tituloCaja") : "";
    OriTrayectoriaEntidadVO trayModif =(OriTrayectoriaEntidadVO)request.getAttribute("trayModif");
    String modoDatos = (String)request.getAttribute("modoDatos") != null ? (String)request.getAttribute("modoDatos") : "";
    String fecIni = "";
    String fecFin = "";
    String fecIniVal = "";
    String fecFinVal = "";
    
        if (request.getAttribute("trayModif")!=null) {
            SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
            if (trayModif.getFechaInicio()!=null) {
                fecIni = formatoFecha.format(trayModif.getFechaInicio());
            }
            if (trayModif.getFechaFin()!=null) {
                fecFin = formatoFecha.format(trayModif.getFechaFin());
            }
            if (trayModif.getFechaInicioVal()!=null) {
                fecIniVal = formatoFecha.format(trayModif.getFechaInicioVal());
            }
            if (trayModif.getFechaFinVal()!=null) {
                fecFinVal = formatoFecha.format(trayModif.getFechaFinVal());
            }
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
<script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.2/jquery.min.js"></script>
<!--<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script>-->
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/general.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/validaciones.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/JavaScriptUtil.js"></script>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/>

<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/estilo.css"/>
<link rel="stylesheet" type="text/css" href="<c:url value='/css/extension/melanbide47/melanbide47.css'/>">
<link rel="stylesheet" type="text/css" href="<c:url value='/css/table.css'/>">
<script type="text/javascript">
    var APP_CONTEXT_PATH = '<%=request.getContextPath()%>';
</script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/melanbide47/moment-with-locales.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/melanbide47/ori14Utils.js"></script>
<script type="text/javascript" src="<c:url value='/scripts/popup.js'/>"></script>
<script type="text/javascript" src="<c:url value='/scripts/listaComboBox.js'/>"></script>
<script type="text/javascript" src="<c:url value='/scripts/general.js'/>"></script>
<script type="text/javascript" src="<c:url value='/scripts/calendario.js'/>"></script>
<script type="text/javascript">
    var mensajeValidacion;
    var validado = false;

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
        comprobarFechasT();
        if (document.getElementById('fechaIniValT').value != '' && document.getElementById('fechaFinValT').value != '') {
            document.getElementById('botonCopiaT').style.visibility = "hidden";
            document.getElementById('btnCopiarDatosValT').disabled = true;
            //comprobarFechasValT();
        }
    }

    function guardarTrayectoria21() {
        if (validarTrayectoria21()) {
            var idTrayEntidad = '<%=trayModif!=null && trayModif.getIdTrayEntidad()!=null?trayModif.getIdTrayEntidad():""%>';
            var codEntidadT = document.getElementById('codEntidadT').value;
            var descEntidadT = escape(document.getElementById('descEntidadT').value);
            var actividad = escape(document.getElementById('descTrayectoria21').value);
            var feInicio = document.getElementById('fechaIniT').value;
            var feFin = document.getElementById('fechaFinT').value;
            var nuMeses = document.getElementById('numMesesT').value;
            var feInicioV = document.getElementById('fechaIniValT').value;
            var feFinV = document.getElementById('fechaFinValT').value;
            var nuMesesV = document.getElementById('numMesesValT').value;
            var idGrupo = '<%=trayModif!=null && trayModif.getIdConActGrupo()!=null?trayModif.getIdConActGrupo():""%>';
            var ajax = getXMLHttpRequest();
            var nodos = null;
            var CONTEXT_PATH = '<%=request.getContextPath()%>'
            var url = CONTEXT_PATH + "/PeticionModuloIntegracion.do";
            var parametros = 'tarea=preparar&modulo=MELANBIDE47&operacion=guardarTrayectoriaEntidad&tipo=0&modoDatos=<%=modoDatos%>&numero=<%=numExpediente%>'
                    + '&idTrayEntidad=' + idTrayEntidad
                    + '&idGrupo=' + idGrupo
                    + '&codEntidad=' + codEntidadT
                    + '&actividad=' + actividad
                    + '&fecIni=' + feInicio
                    + '&fecFin=' + feFin
                    + '&numMeses=' + nuMeses
                    + '&fecIniVal=' + feInicioV
                    + '&fecFinVal=' + feFinV
                    + '&numMesesVal=' + nuMesesV
                    + '&validado=' + validado;

            try {
                ajax.open("POST", url, false);
                ajax.setRequestHeader("Content-Type", "application/x-www-form-urlencoded; charset=iso-8859-15");
                ajax.setRequestHeader("Accept", "text/xml, application/xml, text/plain");
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
                var hijos = elemento.childNodes;
                var codigoOperacion = null;
                var listaDatosRespuesta = new Array();
                var fila = new Array();
                var nodoFila;
                var hijosFila;
                var nodoCampo;
                var j;
                for (j = 0; hijos != null && j < hijos.length; j++) {
                    if (hijos[j].nodeName == "CODIGO_OPERACION") {
                        codigoOperacion = hijos[j].childNodes[0].nodeValue;
                        listaDatosRespuesta[j] = codigoOperacion;
                    } else if (hijos[j].nodeName == "TRAYECTORIAS") {
                        var nodoFila1 = hijos[j];
                        var hijosFila1 = nodoFila1.childNodes;
                        for (var cont1 = 0; cont1 < hijosFila1.length; cont1++) {
                            nodoFila = hijosFila1[cont1];
                            hijosFila = nodoFila.childNodes;
                            for (var cont = 0; cont < hijosFila.length; cont++) {      // recorremos los nodos ACTIVIDAD
                                if (hijosFila[cont].nodeName == "ID") {
                                    nodoCampo = hijosFila[cont];
                                    if (nodoCampo.childNodes.length > 0) {
                                        fila[0] = nodoCampo.childNodes[0].nodeValue;
                                    } else {
                                        fila[0] = '-';
                                    }
                                } else if (hijosFila[cont].nodeName == "TRAYIDFKORIPROGCONVACTGRUPO") {
                                    nodoCampo = hijosFila[cont];
                                    if (nodoCampo.childNodes.length > 0) {
                                        fila[1] = nodoCampo.childNodes[0].nodeValue;
                                    } else {
                                        fila[1] = '-';
                                    }
                                } else if (hijosFila[cont].nodeName == "TRAYIDFKORIPROGCONVACTSUBGRPRE") {
                                    nodoCampo = hijosFila[cont];
                                    if (nodoCampo.childNodes.length > 0) {
                                        fila[2] = nodoCampo.childNodes[0].nodeValue;
                                    } else {
                                        fila[2] = '-';
                                    }
                                } else if (hijosFila[cont].nodeName == "TRAYCODIGOENTIDAD") {
                                    nodoCampo = hijosFila[cont];
                                    if (nodoCampo.childNodes.length > 0) {
                                        fila[3] = nodoCampo.childNodes[0].nodeValue;
                                    } else {
                                        fila[3] = '-';
                                    }
                                } else if (hijosFila[cont].nodeName == "TRAYDESCRIPCION") {
                                    nodoCampo = hijosFila[cont];
                                    if (nodoCampo.childNodes.length > 0) {
                                        fila[4] = nodoCampo.childNodes[0].nodeValue;
                                    } else {
                                        fila[4] = '-';
                                    }
                                } else if (hijosFila[cont].nodeName == "TRAYTIENEEXPERIENCIA") {
                                    nodoCampo = hijosFila[cont];
                                    if (nodoCampo.childNodes.length > 0) {
                                        fila[5] = nodoCampo.childNodes[0].nodeValue;
                                        if (fila[5] == '1') {
                                            fila[5] = '<%=meLanbide47I18n.getMensaje(idiomaUsuario, "label.si").toUpperCase()%>';
                                        } else if (fila[8] == '0') {
                                            fila[5] = '<%=meLanbide47I18n.getMensaje(idiomaUsuario, "label.no").toUpperCase()%>';
                                        }
                                    } else {
                                        fila[5] = '-';
                                    }
                                } else if (hijosFila[cont].nodeName == "TRAYFECHAINICIO") {
                                    nodoCampo = hijosFila[cont];
                                    if (nodoCampo.childNodes.length > 0) {
                                        fila[6] = nodoCampo.childNodes[0].nodeValue;
                                    } else {
                                        fila[6] = '-';
                                    }
                                } else if (hijosFila[cont].nodeName == "TRAYFECHAFIN") {
                                    nodoCampo = hijosFila[cont];
                                    if (nodoCampo.childNodes.length > 0) {
                                        fila[7] = nodoCampo.childNodes[0].nodeValue;
                                    } else {
                                        fila[7] = '-';
                                    }
                                } else if (hijosFila[cont].nodeName == "TRAYNUMEROMESES") {
                                    nodoCampo = hijosFila[cont];
                                    if (nodoCampo.childNodes.length > 0) {
                                        fila[8] = nodoCampo.childNodes[0].nodeValue;
                                    } else {
                                        fila[8] = '-';
                                    }
                                } else if (hijosFila[cont].nodeName == "TRAYTIENEEXPERIENCIAVAL") {
                                    nodoCampo = hijosFila[cont];
                                    if (nodoCampo.childNodes.length > 0) {
                                        fila[5] = nodoCampo.childNodes[0].nodeValue;
                                        if (fila[9] == '1') {
                                            fila[9] = '<%=meLanbide47I18n.getMensaje(idiomaUsuario, "label.si").toUpperCase()%>';
                                        } else if (fila[8] == '0') {
                                            fila[9] = '<%=meLanbide47I18n.getMensaje(idiomaUsuario, "label.no").toUpperCase()%>';
                                        }
                                    } else {
                                        fila[9] = '-';
                                    }
                                } else if (hijosFila[cont].nodeName == "TRAYFECHAINICIOVAL") {
                                    nodoCampo = hijosFila[cont];
                                    if (nodoCampo.childNodes.length > 0) {
                                        fila[10] = nodoCampo.childNodes[0].nodeValue;
                                    } else {
                                        fila[10] = '-';
                                    }
                                } else if (hijosFila[cont].nodeName == "TRAYFECHAFINVAL") {
                                    nodoCampo = hijosFila[cont];
                                    if (nodoCampo.childNodes.length > 0) {
                                        fila[11] = nodoCampo.childNodes[0].nodeValue;
                                    } else {
                                        fila[11] = '-';
                                    }
                                } else if (hijosFila[cont].nodeName == "TRAYNUMEROMESESVAL") {
                                    nodoCampo = hijosFila[cont];
                                    if (nodoCampo.childNodes.length > 0) {
                                        fila[12] = nodoCampo.childNodes[0].nodeValue;
                                    } else {
                                        fila[12] = '-';
                                    }
                                } else if (hijosFila[cont].nodeName == "ORI_ENT_CIF") {
                                    nodoCampo = hijosFila[cont];
                                    if (nodoCampo.childNodes.length > 0) {
                                        fila[13] = nodoCampo.childNodes[0].nodeValue;
                                    } else {
                                        fila[13] = '-';
                                    }
                                } else if (hijosFila[cont].nodeName == "ORI_ENT_NOM") {
                                    nodoCampo = hijosFila[cont];
                                    if (nodoCampo.childNodes.length > 0) {
                                        fila[14] = nodoCampo.childNodes[0].nodeValue;
                                    } else {
                                        fila[14] = '-';
                                    }
                                }
                            }// nodo ACTIVIDAD
                            listaDatosRespuesta[cont1 + 1] = fila;
                            fila = new Array();
                        } //for(var cont1 = 0; cont1 < hijosFila1.length; cont1++)
                    }// if TRAYECTORIAS

                }//for(j=0;hijos!=null && j<hijos.length;j++)
                if (codigoOperacion == "0") {
                    jsp_alerta("A", '<%=meLanbide47I18n.getMensaje(idiomaUsuario,"msg.datosGuardadosOK")%>');
                    self.parent.opener.retornoXanelaAuxiliar(listaDatosRespuesta);
                    cerrarVentana();
                } else if (codigoOperacion == "1") {
                    jsp_alerta("A", '<%=meLanbide47I18n.getMensaje(idiomaUsuario,"error.errorBD")%>');
                } else if (codigoOperacion == "2") {
                    jsp_alerta("A", '<%=meLanbide47I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                } else if (codigoOperacion == "3") {
                    jsp_alerta("A", '<%=meLanbide47I18n.getMensaje(idiomaUsuario,"error.pasoParametros")%>');
                } else if (codigoOperacion == "4") {
                    jsp_alerta("A", '<%=meLanbide47I18n.getMensaje(idiomaUsuario,"error.datosNoValidos")%>');
                } else {
                    jsp_alerta("A", '<%=meLanbide47I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                }//if

            } catch (Err) {
                jsp_alerta("A", '<%=meLanbide47I18n.getMensaje(idiomaUsuario,"error.errorProcesarRespuesta")%>');
            }//try-catch
        } else {
            jsp_alerta("A", mensajeValidacion);
        }
    }

    function validarTrayectoria21() {
        mensajeValidacion = '';
        var codigo = '';
        codigo = document.getElementById('codEntidadT').value;
        if (codigo == '') {
            mensajeValidacion = '<%=meLanbide47I18n.getMensaje(idiomaUsuario,"msg.generico.campos.obligatorios")%>';
            return false;
        }
        var variable = document.getElementById('descTrayectoria21').value;
        if (variable == '') {
            mensajeValidacion = '<%=meLanbide47I18n.getMensaje(idiomaUsuario,"msg.generico.campos.obligatorios")%>';
            return false;
        } else {
            if (!comprobarCaracteresEspecialesOri14(variable)) {
                mensajeValidacion = '<%= meLanbide47I18n.getMensaje(idiomaUsuario,"msg.actividades.caracteres")%>';
                return false;
            }
        }
        variable = document.getElementById('fechaIniT').value;
        if (variable == '') {
            mensajeValidacion = '<%=meLanbide47I18n.getMensaje(idiomaUsuario,"msg.generico.campos.obligatorios")%>';
            return false;
        }
        variable = document.getElementById('fechaFinT').value;
        if (variable == '') {
            mensajeValidacion = '<%=meLanbide47I18n.getMensaje(idiomaUsuario,"msg.generico.campos.obligatorios")%>';
            return false;
        }
        variable = document.getElementById('numMesesT').value;
        if (variable == '') {
            mensajeValidacion = '<%=meLanbide47I18n.getMensaje(idiomaUsuario,"msg.generico.campos.obligatorios")%>';
            return false;
        } else {
            if (isNaN(variable)) {
                mensajeValidacion = '<%=meLanbide47I18n.getMensaje(idiomaUsuario,"msg.generico.duracionNaN")%>';
                return false;
            }
        }
        variable = document.getElementById('numMesesValT').value;
        if (variable != '') {
            if (isNaN(variable)) {
                mensajeValidacion = '<%=meLanbide47I18n.getMensaje(idiomaUsuario,"msg.actividades.duracionValNaN")%>';
                return false;
            }
        }

        return true;
    }

    function validarDatos() {
        if (document.getElementById('fechaIniT').value != '' && document.getElementById('fechaFinT').value != '') {
            document.getElementById('fechaIniValT').value = document.getElementById('fechaIniT').value;
            document.getElementById('fechaFinValT').value = document.getElementById('fechaFinT').value;
            comprobarFechasValT();
            validado = true;
        } else {
            jsp_alerta('A', '<%=meLanbide47I18n.getMensaje(idiomaUsuario, "msg.actividades.faltanFechas")%>');
        }

    }

    function comprobarFechasT() {
        if (document.getElementById('fechaIniT').value != '' && document.getElementById('fechaFinT').value != '') {
            var diferencia = mesesDiferencia(document.getElementById('fechaIniT').value, document.getElementById('fechaFinT').value);
            document.getElementById('numMesesT').value = diferencia;
        }
    }

    function comprobarFechasValT() {
        if (document.getElementById('fechaIniValT').value != '' && document.getElementById('fechaFinValT').value != '') {
            var diferencia = mesesDiferencia(document.getElementById('fechaIniValT').value, document.getElementById('fechaFinValT').value);
            document.getElementById('numMesesValT').value = diferencia;
        }
    }

//Funciones para el calendario de fecha 
    function mostrarCalFechaIniT(evento) {
        if (window.event)
            evento = window.event;
        if (document.getElementById("calFechaIniT").src.indexOf("icono.gif") != -1)
            showCalendar('forms[0]', 'fechaIniT', null, null, null, '', 'calFechaIniT', '', null, null, null, null, null, null, null, null, evento);
    }

    function mostrarCalFechaIniValT(evento) {
        if (window.event)
            evento = window.event;
        if (document.getElementById("calFechaIniValT").src.indexOf("icono.gif") != -1)
            showCalendar('forms[0]', 'fechaIniValT', null, null, null, '', 'calFechaIniValT', '', null, null, null, null, null, null, null, null, evento);
    }

    function mostrarCalFechaFinT(evento) {
        if (window.event)
            evento = window.event;
        if (document.getElementById("calFechaFinT").src.indexOf("icono.gif") != -1)
            showCalendar('forms[0]', 'fechaFinT', null, null, null, '', 'calFechaFinT', '', null, null, null, null, null, null, null, null, evento);
    }

    function mostrarCalFechaFinValT(evento) {
        if (window.event)
            evento = window.event;
        if (document.getElementById("calFechaFinValT").src.indexOf("icono.gif") != -1)
            showCalendar('forms[0]', 'fechaFinValT', null, null, null, '', 'calFechaFinValT', '', null, null, null, null, null, null, null, null, evento);
    }

    function cancelar() {
        var resultado = jsp_alerta('', '<%=meLanbide47I18n.getMensaje(idiomaUsuario, "msg.preguntaCancelar")%>');
        if (resultado == 1) {
            cerrarVentana();
        }
    }

    function cerrarVentana() {
        window.close();
    }
</script>

<body>
    <div  class="contenidoPantalla">
        <form>
            <div align="center">
                <div style="clear: both; overflow-x: hidden; overflow-y: auto; height: 650px;">
                    <fieldset id="fieldsetTrayectoria21" name="fieldsetTrayectoria21" style="width: 935px">
                        <legend class="legendAzul"><%=tituloPagina%></legend>
                        <div class="lineaFormulario" style="padding-left: 5px; padding-right: 5px; text-align: left;">
                            <!-- ENTIDAD -->
                            <label for="codEntidadT" style="vertical-align: bottom;"><%=meLanbide47I18n.getMensaje(idiomaUsuario,"label.trayectoria.actividades.entidad")%></label>                       
                            <input id="codEntidadT" name="codEntidadT" type="text" class="inputTexto" size="4" maxlength="3" style="margin-left: 19px;" readonly value="<%=trayModif != null && trayModif.getCodEntidad()!=null ? trayModif.getCodEntidad():""%>"/>
                            <input id="descEntidadT" name="descEntidadT" type="text" class="inputTexto" size="70" readonly value="<%=trayModif != null && trayModif.getNombreEntidad()!=null ? trayModif.getNombreEntidad():""%>"/>
                        </div>
                        <div class="lineaFormulario" style="padding-left: 5px; padding-right: 5px; text-align: left;">
                            <!-- ACTIVIDAD -->
                            <label for="descTrayectoria21" style="vertical-align: top;"><%=tituloCaja%></label><font color="red" style="margin-right: 44px; vertical-align: top;">*</font>
                            <textarea id="descTrayectoria21" style="vertical-align: top;" class="textareaTextoObligatorio" rows="4" cols="124" maxlength="500"><%=trayModif != null && trayModif.getDescActividad()!=null ? trayModif.getDescActividad():""%></textarea>
                        </div>                      
                        <div class="lineaFormulario" style="padding-left: 5px; padding-right: 5px; text-align: left;">
                            <!--  FECHA INICIO  -->
                            <div style="float: left; width: 140px; text-align: left;">
                                <%=meLanbide47I18n.getMensaje(idiomaUsuario,"label.trayectoria21.tabla.fechaIni")%><font color="red">*</font>  
                            </div>
                            <div style="width: 140px; float: left;padding-left: 20px">
                                <div style="float: left;">
                                    <input type="text" class="inputTxtFechaObligatorio" 
                                           id="fechaIniT" name="fechaIniT"
                                           maxlength="10"  size="10"
                                           value="<%=fecIni%>"
                                           onkeyup = "return SoloCaracteresFecha(this);"
                                           onblur = "javascript:return comprobarFechaOri14(this);"
                                           onfocus="javascript:this.select();"
                                           onchange="comprobarFechasT();"
                                           oninput="comprobarFechasT();"/>
                                    <A href="javascript:calClick(event);return false;" onClick="mostrarCalFechaIniT(event);
                                            return false;" style="text-decoration:none;">  
                                        <IMG style="border: 0px solid none" height="17" id="calFechaIniT" name="calFechaIniT" border="0" 
                                             src="<c:url value='/images/calendario/icono.gif'/>" >
                                    </A>
                                </div>
                            </div>
                            <!--  FECHA INICIO VALIDADA -->
                            <div style="padding-left: 20px; float: left; width: 140px; text-align: left;">
                                <%=meLanbide47I18n.getMensaje(idiomaUsuario,"label.trayectoria21.tabla.fechaIniVal")%>
                            </div>
                            <div style="width: 140px; float: left;">
                                <div style="float: left;">
                                    <input type="text" class="inputTxtFecha" 
                                           id="fechaIniValT" name="fechaIniValT"
                                           maxlength="10"  size="10"
                                           value="<%=fecIniVal%>"
                                           onkeyup = "return SoloCaracteresFecha(this);"
                                           onblur = "javascript:return comprobarFechaOri14(this);"
                                           onfocus="javascript:this.select();"
                                           onchange="comprobarFechasValT();"
                                           oninput="comprobarFechasValT();"/>
                                    <A href="javascript:calClick(event);return false;" onClick="mostrarCalFechaIniValT(event);
                                            return false;" style="text-decoration:none;"> 
                                        <IMG style="border: 0px solid none" height="17" id="calFechaIniValT" name="calFechaIniValT" border="0" 
                                             src="<c:url value='/images/calendario/icono.gif'/>" > 
                                    </A>
                                </div>
                            </div>
                        </div>
                        <div class="lineaFormulario" style="padding-left: 5px; padding-right: 5px; text-align: left;">
                            <!--  FECHA FIN  -->
                            <div style="float: left; width: 140px; text-align: left;">
                                <%=meLanbide47I18n.getMensaje(idiomaUsuario,"label.trayectoria21.tabla.fechaFin")%><font color="red">*</font>
                            </div>
                            <div style="width: 140px; float: left;padding-left: 20px">
                                <div style="float: left;">
                                    <input type="text" class="inputTxtFechaObligatorio" 
                                           id="fechaFinT" name="fechaFinT"
                                           maxlength="10"  size="10"
                                           value="<%=fecFin%>"
                                           onkeyup = "return SoloCaracteresFecha(this);"
                                           onblur = "javascript:return comprobarFechaOri14(this);"
                                           onfocus="javascript:this.select();"
                                           onchange="comprobarFechasT();"
                                           oninput="comprobarFechasT();"/>
                                    <A href="javascript:calClick(event);return false;" onClick="mostrarCalFechaFinT(event);
                                            return false;" style="text-decoration:none;">
                                        <IMG style="border: 0px solid none" height="17" id="calFechaFinT" name="calFechaFinT" border="0" 
                                             src="<c:url value='/images/calendario/icono.gif'/>" > 
                                    </A>
                                </div>
                            </div>
                            <!--  FECHA FIN VALIDADA  -->
                            <div style="padding-left: 20px; float: left; width: 140px; text-align: left;">
                                <%=meLanbide47I18n.getMensaje(idiomaUsuario,"label.trayectoria21.tabla.fechaFinVal")%>
                            </div>
                            <div style="width: 140px; float: left;">
                                <div style="float: left;">
                                    <input type="text" class="inputTxtFecha" 
                                           id="fechaFinValT" name="fechaFinValT"
                                           maxlength="10"  size="10"
                                           value="<%=fecFinVal%>"
                                           onkeyup = "return SoloCaracteresFecha(this);"
                                           onblur = "javascript:return comprobarFechaOri14(this);"
                                           onfocus="javascript:this.select();"
                                           onchange="comprobarFechasValT();"
                                           oninput="comprobarFechasValT();"/>
                                    <A href="javascript:calClick(event);return false;" onClick="mostrarCalFechaFinValT(event);
                                            return false;" style="text-decoration:none;">
                                        <IMG style="border: 0px solid none" height="17" id="calFechaFinValT" name="calFechaFinValT"
                                             border="0" src="<c:url value='/images/calendario/icono.gif'/>" >
                                    </A>
                                </div>
                            </div>
                        </div>
                        <div class="lineaFormulario" style="padding-left: 5px; padding-right: 5px; text-align: left;">
                            <!--  NUM MESES  -->
                            <label for="numMesesT" style="vertical-align: top;"><%=meLanbide47I18n.getMensaje(idiomaUsuario,"label.trayectoria21.tabla.duracion")%></label><font color="red" style="vertical-align: top;">*</font>
                            <input id="numMesesT" name="numMesesT" type="text" class="inputTextoObligatorio" style="margin-left: 19px;" size="5" maxlength="4" value="<%=trayModif != null && trayModif.getNumMeses()!=null ? trayModif.getNumMeses():""%>">
                            <!--  NUM MESES VALIDADA  -->
                            <label for="numMesesValT" style="padding-left: 35px; vertical-align: top;"><%=meLanbide47I18n.getMensaje(idiomaUsuario,"label.trayectoria21.tabla.duracionVal")%></label>
                            <input id="numMesesValT" name="numMesesValT" type="text" class="inputTexto" style="margin-left: 19px; padding-right: 5" size="5" maxlength="4" value="<%=trayModif != null && trayModif.getNumMesesVal()!=null ? trayModif.getNumMesesVal():""%>">
                        </div>
                        <div class="lineaFormulario" id="botonCopiaT" name="botonCopiaT">
                            <input type="button" id="btnCopiarDatosValT" name="btnCopiarDatosValT" class="botonMasLargo" style="margin-top: 5px" value="<%=meLanbide47I18n.getMensaje(idiomaUsuario,"btn.copiarDatos")%>" onclick="validarDatos()"/>
                        </div>
                        <div class="lineaFormulario">
                            <div class="botonera" style="text-align: center; margin-bottom: 10px">
                                <input type="button" id="btnGuardarTrayectoria" name="btnGuardarTrayectoria" class="botonGeneral" value="<%=meLanbide47I18n.getMensaje(idiomaUsuario,"btn.guardar")%>" onclick="guardarTrayectoria21()"/>
                                <input type="button" id="btnCancelarT" name="btnCancelarT" class="botonGeneral" value="<%=meLanbide47I18n.getMensaje(idiomaUsuario, "btn.cancelar")%>" onclick="cancelar();"/>
                            </div>
                        </div>
                    </fieldset>
                </div> 
            </div>     
        </form>
    </div>
</body>
<script type="text/javascript">
    inicio();
</script>  
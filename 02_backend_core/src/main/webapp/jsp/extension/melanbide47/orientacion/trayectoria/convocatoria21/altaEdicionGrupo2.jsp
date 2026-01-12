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

    var comboSubGrupos;
    var codSubGrupos = new Array();
    var descSubGrupos = new Array();

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
        if ('<%=modoDatos%>' == '1') {
            buscaCodigoSubGrupo('<%=trayModif.getIdConActSubgrupo() != null ? trayModif.getIdConActSubgrupo().toString() :""%>');
        }

    <% 
        if (trayModif.getTieneExperiencia()!= null && trayModif.getTieneExperiencia() == 1){ 
    %>
        document.getElementById('experienciaS').checked = 'true';
    <%  } 
        else if (trayModif.getTieneExperiencia()!= null && trayModif.getTieneExperiencia() == 0){ 
    %>
        document.getElementById('experienciaN').checked = 'true';
    <%  }
        if (trayModif.getTieneExperienciaVal()!= null && trayModif.getTieneExperienciaVal() == 1){ 
    %>
        document.getElementById('experienciaVS').checked = 'true';
    <%  }
        else if (trayModif.getTieneExperiencia()!= null && trayModif.getTieneExperiencia() == 0){
    %>
        document.getElementById('experienciaVN').checked = 'true';
    <%  }%>

        comprobarFechasP();
        if (document.getElementById('fechaIniValP').value != '' && document.getElementById('fechaFinValP').value != '') {
            document.getElementById('botonCopiaP').style.visibility = "hidden";
            document.getElementById('btnCopiarDatosValP').disabled = true;
           // comprobarFechasValP();
        }
    }

    function buscaCodigoSubGrupo(codSubGr) {
        comboSubGrupos.buscaCodigo(codSubGr);
    }

    function cargarDatosSubGrupo() {
        var codSubGrupoSeleccionado = document.getElementById("codSubGrupo").value;
        buscaCodigoSubGrupo(codSubGrupoSeleccionado);
    }

    function guardarGrupo2() {
        if (validarGrupo2()) {
            var idTrayEntidad = '<%=trayModif!=null && trayModif.getIdTrayEntidad()!=null?trayModif.getIdTrayEntidad():""%>';
            var codEntidadP = document.getElementById('codEntidadP').value;
            var descEntidadP = escape(document.getElementById('descEntidadP').value);
            var actividad = escape(document.getElementById('descSubGrupo').value);
            var feInicio = document.getElementById('fechaIniP').value;
            var feFin = document.getElementById('fechaFinP').value;
            var nuMeses = document.getElementById('numMesesP').value;
            var feInicioV = document.getElementById('fechaIniValP').value;
            var feFinV = document.getElementById('fechaFinValP').value;
            var nuMesesV = document.getElementById('numMesesValP').value;
            var expe = (document.getElementById('experienciaS').checked ? 1 : document.getElementById('experienciaN').checked ? 0 : '');
            var expeV = (document.getElementById('experienciaVS').checked ? 1 : document.getElementById('experienciaVN').checked ? 0 : '');

            var ajax = getXMLHttpRequest();
            var nodos = null;
            var CONTEXT_PATH = '<%=request.getContextPath()%>'
            var url = CONTEXT_PATH + "/PeticionModuloIntegracion.do";
            var parametros = 'tarea=preparar&modulo=MELANBIDE47&operacion=guardarTrayectoriaEntidad&tipo=0&modoDatos=<%=modoDatos%>&numero=<%=numExpediente%>'
                    + '&idTrayEntidad=' + idTrayEntidad
                    + '&idGrupo=2'
                    + '&idSubGrupo=' + document.getElementById('codSubGrupo').value
                    + '&codEntidad=' + codEntidadP
                    + '&actividad=' + actividad
                    + '&experiencia=' + expe
                    + '&fecIni=' + feInicio
                    + '&fecFin=' + feFin
                    + '&numMeses=' + nuMeses
                    + '&experienciaVal=' + expeV
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
                                        } else if (fila[5] == '0') {
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
                                        fila[9] = nodoCampo.childNodes[0].nodeValue;
                                        if (fila[9] == '1') {
                                            fila[9] = '<%=meLanbide47I18n.getMensaje(idiomaUsuario, "label.si").toUpperCase()%>';
                                        } else if (fila[9] == '0') {
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

    function validarGrupo2() {
        mensajeValidacion = '';
        var codigo = '';
        codigo = document.getElementById('codEntidadP').value;
        if (codigo == '') {
            mensajeValidacion = '<%=meLanbide47I18n.getMensaje(idiomaUsuario,"msg.generico.campos.obligatorios")%>';
            return false;
        }
        var variable = document.getElementById('descSubGrupo').value;
        if (variable == '') {
            mensajeValidacion = '<%=meLanbide47I18n.getMensaje(idiomaUsuario,"msg.generico.campos.obligatorios")%>';
            return false;
        } else {
            if (!comprobarCaracteresEspecialesOri14(variable)) {
                mensajeValidacion = '<%= meLanbide47I18n.getMensaje(idiomaUsuario,"msg.actividades.caracteres")%>';
                return false;
            }
        }
        if (!document.getElementById('experienciaS').checked && !document.getElementById('experienciaN').checked) {
            mensajeValidacion = '<%=meLanbide47I18n.getMensaje(idiomaUsuario,"msg.trayectoria21.faltaExperiencia")%>';
            return false;
        }
        variable = document.getElementById('fechaIniP').value;
        if (variable == '') {
            mensajeValidacion = '<%=meLanbide47I18n.getMensaje(idiomaUsuario,"msg.generico.campos.obligatorios")%>';
            return false;
        }
        variable = document.getElementById('fechaFinP').value;
        if (variable == '') {
            mensajeValidacion = '<%=meLanbide47I18n.getMensaje(idiomaUsuario,"msg.generico.campos.obligatorios")%>';
            return false;
        }
        variable = document.getElementById('numMesesP').value;
        if (variable == '') {
            mensajeValidacion = '<%=meLanbide47I18n.getMensaje(idiomaUsuario,"msg.generico.campos.obligatorios")%>';
            return false;
        } else {
            if (isNaN(variable)) {
                mensajeValidacion = '<%=meLanbide47I18n.getMensaje(idiomaUsuario,"msg.generico.duracionNaN")%>';
                return false;
            }
        }
        variable = document.getElementById('numMesesValP').value;
        if (variable != '') {
            if (isNaN(variable)) {
                mensajeValidacion = '<%=meLanbide47I18n.getMensaje(idiomaUsuario,"msg.trayectoria21.duracionValNaN")%>';
                return false;
            }
        }
        return true;
    }

    function copiarDatosAValidacion() {
        if (document.getElementById('fechaIniP').value != '' && document.getElementById('fechaFinP').value != '') {
            if (!document.getElementById('experienciaS').checked && !document.getElementById('experienciaN').checked) {
                jsp_alerta('A', '<%=meLanbide47I18n.getMensaje(idiomaUsuario, "msg.trayectoria21.faltaExperiencia")%>');
            } else {
                document.getElementById('fechaIniValP').value = document.getElementById('fechaIniP').value;
                document.getElementById('fechaFinValP').value = document.getElementById('fechaFinP').value;
                document.getElementById('experienciaVN').checked = false;
                document.getElementById('experienciaVS').checked = false;
                if (document.getElementById('experienciaS').checked) {
                    document.getElementById('experienciaVS').checked = 'true';
                } else {
                    document.getElementById('experienciaVS').checked = false;
                }
                if (document.getElementById('experienciaN').checked) {
                    document.getElementById('experienciaVN').checked = 'true';
                } else {
                    document.getElementById('experienciaVN').checked = false;
                }
                comprobarFechasValP();
                validado = true;
            }
        } else {
            jsp_alerta('A', '<%=meLanbide47I18n.getMensaje(idiomaUsuario, "msg.trayectoria21.faltanFechas")%>');
        }

    }

    function comprobarFechasP() {
        if (document.getElementById('fechaIniP').value != '' && document.getElementById('fechaFinP').value != '') {
            var diferencia = mesesDiferencia(document.getElementById('fechaIniP').value, document.getElementById('fechaFinP').value);
            document.getElementById('numMesesP').value = diferencia;
        }
    }

    function comprobarFechasValP() {
        if (document.getElementById('fechaIniValP').value != '' && document.getElementById('fechaFinValP').value != '') {
            var diferencia = mesesDiferencia(document.getElementById('fechaIniValP').value, document.getElementById('fechaFinValP').value);
            document.getElementById('numMesesValP').value = diferencia;
        }
    }

//Funciones para el calendario de fecha 
    function mostrarCalFechaIniP(evento) {
        if (window.event)
            evento = window.event;
        if (document.getElementById("calFechaIniP").src.indexOf("icono.gif") != -1)
            showCalendar('forms[0]', 'fechaIniP', null, null, null, '', 'calFechaIniP', '', null, null, null, null, null, null, null, null, evento);
    }

    function mostrarCalFechaIniValP(evento) {
        if (window.event)
            evento = window.event;
        if (document.getElementById("calFechaIniValP").src.indexOf("icono.gif") != -1)
            showCalendar('forms[0]', 'fechaIniValP', null, null, null, '', 'calFechaIniValP', '', null, null, null, null, null, null, null, null, evento);
    }

    function mostrarCalFechaFinP(evento) {
        if (window.event)
            evento = window.event;
        if (document.getElementById("calFechaFinP").src.indexOf("icono.gif") != -1)
            showCalendar('forms[0]', 'fechaFinP', null, null, null, '', 'calFechaFinP', '', null, null, null, null, null, null, null, null, evento);
    }

    function mostrarCalFechaFinValP(evento) {
        if (window.event)
            evento = window.event;
        if (document.getElementById("calFechaFinValP").src.indexOf("icono.gif") != -1)
            showCalendar('forms[0]', 'fechaFinValP', null, null, null, '', 'calFechaFinValP', '', null, null, null, null, null, null, null, null, evento);
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
                    <fieldset id="fieldsetProgramas" name="fieldsetProgramas" style="width: 935px">
                        <legend class="legendAzul"><%=tituloPagina%></legend>
                        <div class="lineaFormulario" style="padding-left: 5px; padding-right: 5px; text-align: left;">
                            <!-- ENTIDAD -->
                            <label for="codEntidadP" style="vertical-align: bottom;"><%=meLanbide47I18n.getMensaje(idiomaUsuario,"label.trayectoria.actividades.entidad")%></label>                       
                            <input id="codEntidadP" name="codEntidadP" type="text" class="inputTexto" size="4" maxlength="3" style="margin-left: 19px;" readonly value="<%=trayModif != null && trayModif.getCodEntidad()!=null ? trayModif.getCodEntidad():""%>"/>
                            <input id="descEntidadP" name="descEntidadP" type="text" class="inputTexto" size="70" readonly value="<%=trayModif != null && trayModif.getNombreEntidad()!=null ? trayModif.getNombreEntidad():""%>"/>
                        </div>
                        <div class="lineaFormulario" style="padding-left: 5px; padding-right: 5px; text-align: left;">
                            <div style="width: 160px; float: left;">
                                <%=tituloCaja%><font color="red">*</font>  
                            </div>
                            <div style="width: auto; float: left;">                               
                                <input id="codSubGrupo" name="codSubGrupo" type="text" class="inputTextoObligatorio" size="2" maxlength="2" onkeypress="javascript:return SoloDigitosConsulta(event);">
                                <input id="descSubGrupo" name="descSubGrupo" type="text" class="inputTexto" size="140" readonly>
                                <a id="anchorSubGrupo" name="anchorSubGrupo" href=""><img src="<%=request.getContextPath()%>/images/listas/botondesplegable.gif" id="botonSubGrupo" name="botonSubGrupo" border="0" width="14" height="14" style="cursor:hand;" alt="<%=meLanbide47I18n.getMensaje(idiomaUsuario, "altDesplegable")%>" title="<%=meLanbide47I18n.getMensaje(idiomaUsuario, "altDesplegable")%>"></a>                              
                            </div>
                        </div>
                        <div class="lineaFormulario" style="padding-left: 5px; padding-right: 5px; text-align: left;"> 
                            <div style="width: 50%">
                                <div style="float: left; width: 140px; ">
                                    <%=meLanbide47I18n.getMensaje(idiomaUsuario,"label.trayectoria21.tabla.experiencia")%><font color="red">*</font> 
                                </div>
                                <div style="width: 140px; float: left; margin-left: 10px;">
                                    <div style="float: left;">
                                        <input type="radio" name="experiencia" id="experienciaS" value="S"><%=meLanbide47I18n.getMensaje(idiomaUsuario, "label.si")%>
                                        <input type="radio" name="experiencia" id="experienciaN" value="N"><%=meLanbide47I18n.getMensaje(idiomaUsuario, "label.no")%>
                                    </div>
                                </div> 
                            </div>

                            <div style="float: left; width: 200px; ">
                                <%=meLanbide47I18n.getMensaje(idiomaUsuario,"label.trayectoria21.tabla.experienciaVal")%>
                            </div>
                            <div style="width: 80px; float: left; margin-left: 10px;">
                                <div style="float: left;">
                                    <input type="radio" name="experienciaV" id="experienciaVS" value="S"><%=meLanbide47I18n.getMensaje(idiomaUsuario, "label.si")%>
                                    <input type="radio" name="experienciaV" id="experienciaVN" value="N"><%=meLanbide47I18n.getMensaje(idiomaUsuario, "label.no")%>
                                </div>
                            </div>    
                        </div>       
                        <div class="lineaFormulario" style="padding-left: 5px; padding-right: 5px; text-align: left;">
                            <div style="width: 50%">
                                <!--  FECHA INICIO  -->
                                <div style="float: left; width: 140px; text-align: left;">
                                    <%=meLanbide47I18n.getMensaje(idiomaUsuario,"label.trayectoria21.tabla.fechaIni")%><font color="red">*</font>  
                                </div>
                                <div style="width: 140px; float: left;padding-left: 20px">
                                    <div style="float: left;">
                                        <input type="text" class="inputTxtFechaObligatorio" 
                                               id="fechaIniP" name="fechaIniP"
                                               maxlength="10"  size="10"
                                               value="<%=fecIni%>"
                                               onkeyup = "return SoloCaracteresFecha(this);"
                                               onblur = "javascript:return comprobarFechaOri14(this);"
                                               onfocus="javascript:this.select();"
                                               onchange="comprobarFechasP();"
                                               oninput="comprobarFechasP();"/>
                                        <A href="javascript:calClick(event);return false;" onClick="mostrarCalFechaIniP(event);
                                            return false;" style="text-decoration:none;">  
                                            <IMG style="border: 0px solid none" height="17" id="calFechaIniP" name="calFechaIniP" border="0" 
                                                 src="<c:url value='/images/calendario/icono.gif'/>" >
                                        </A>
                                    </div>
                                </div>
                            </div>

                            <!--  FECHA INICIO VALIDADA -->
                            <div style="padding-left: 70px; float: left; width: 140px; text-align: left;">
                                <%=meLanbide47I18n.getMensaje(idiomaUsuario,"label.trayectoria21.tabla.fechaIniVal")%>
                            </div>
                            <div style="width: 140px; float: left;">
                                <div style="float: left;">
                                    <input type="text" class="inputTxtFecha" 
                                           id="fechaIniValP" name="fechaIniValP"
                                           maxlength="10"  size="10"
                                           value="<%=fecIniVal%>"
                                           onkeyup = "return SoloCaracteresFecha(this);"
                                           onblur = "javascript:return comprobarFechaOri14(this);"
                                           onfocus="javascript:this.select();"
                                           onchange="comprobarFechasValP();"
                                           oninput="comprobarFechasValP();"/>
                                    <A href="javascript:calClick(event);return false;" onClick="mostrarCalFechaIniValP(event);
                                            return false;" style="text-decoration:none;"> 
                                        <IMG style="border: 0px solid none" height="17" id="calFechaIniValP" name="calFechaIniValP" border="0" 
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
                                           id="fechaFinP" name="fechaFinP"
                                           maxlength="10"  size="10"
                                           value="<%=fecFin%>"
                                           onkeyup = "return SoloCaracteresFecha(this);"
                                           onblur = "javascript:return comprobarFechaOri14(this);"
                                           onfocus="javascript:this.select();"
                                           onchange="comprobarFechasP();"
                                           oninput="comprobarFechasP();"/>
                                    <A href="javascript:calClick(event);return false;" onClick="mostrarCalFechaFinP(event);
                                            return false;" style="text-decoration:none;">
                                        <IMG style="border: 0px solid none" height="17" id="calFechaFinP" name="calFechaFinP" border="0" 
                                             src="<c:url value='/images/calendario/icono.gif'/>" > 
                                    </A>
                                </div>
                            </div>
                            <!--  FECHA FIN VALIDADA  -->
                            <div style="padding-left: 70px; float: left; width: 140px; text-align: left;">
                                <%=meLanbide47I18n.getMensaje(idiomaUsuario,"label.trayectoria21.tabla.fechaFinVal")%>
                            </div>
                            <div style="width: 140px; float: left;">
                                <div style="float: left;">
                                    <input type="text" class="inputTxtFecha" 
                                           id="fechaFinValP" name="fechaFinValP"
                                           maxlength="10"  size="10"
                                           value="<%=fecFinVal%>"
                                           onkeyup = "return SoloCaracteresFecha(this);"
                                           onblur = "javascript:return comprobarFechaOri14(this);"
                                           onfocus="javascript:this.select();"
                                           onchange="comprobarFechasValP();"
                                           oninput="comprobarFechasValP();"/>
                                    <A href="javascript:calClick(event);return false;" onClick="mostrarCalFechaFinValP(event);
                                            return false;" style="text-decoration:none;">
                                        <IMG style="border: 0px solid none" height="17" id="calFechaFinValP" name="calFechaFinValP"
                                             border="0" src="<c:url value='/images/calendario/icono.gif'/>" >
                                    </A>
                                </div>
                            </div>
                        </div>
                        <div class="lineaFormulario" style="padding-left: 5px; padding-right: 5px; text-align: left;">
                            <!--  NUM MESES  -->
                            <label for="numMesesP" style="vertical-align: top;"><%=meLanbide47I18n.getMensaje(idiomaUsuario,"label.trayectoria21.tabla.duracion")%></label><font color="red" style="vertical-align: top;">*</font>
                            <input id="numMesesP" name="numMesesP" type="text" class="inputTextoObligatorio" style="margin-left: 19px;" size="5" maxlength="4" value="<%=trayModif != null && trayModif.getNumMeses()!=null ? trayModif.getNumMeses():""%>">
                            <!--  NUM MESES VALIDADA  -->
                            <label for="numMesesValP" style="padding-left: 35px; vertical-align: top;"><%=meLanbide47I18n.getMensaje(idiomaUsuario,"label.trayectoria21.tabla.duracionVal")%></label>
                            <input id="numMesesValP" name="numMesesValP" type="text" class="inputTexto" style="margin-left: 19px; padding-right: 5" size="5" maxlength="4" value="<%=trayModif != null && trayModif.getNumMesesVal()!=null ? trayModif.getNumMesesVal():""%>">
                        </div>
                        <div class="lineaFormulario" id="botonCopiaP" name="botonCopiaP">
                            <input type="button" id="btnCopiarDatosValP" name="btnCopiarDatosValP" class="botonMasLargo" style="margin-top: 5px" value="<%=meLanbide47I18n.getMensaje(idiomaUsuario,"btn.validar")%>" onclick="copiarDatosAValidacion()"/>
                        </div>
                        <div class="lineaFormulario">
                            <div class="botonera" style="text-align: center; margin-bottom: 10px">
                                <input type="button" id="btnGuardarGrupo2" name="btnGuardarGrupo2" class="botonGeneral" value="<%=meLanbide47I18n.getMensaje(idiomaUsuario,"btn.guardar")%>" onclick="guardarGrupo2()"/>
                                <input type="button" id="btnCancelarP" name="btnCancelarP" class="botonGeneral" value="<%=meLanbide47I18n.getMensaje(idiomaUsuario, "btn.cancelar")%>" onclick="cancelar();"/>
                            </div>
                        </div>
                    </fieldset>
                </div> 
            </div>     
        </form>
    </div>
    <script type="text/javascript">
        codSubGrupos[0] = "";
        descSubGrupos[0] = "";
        contador = 0;

        <logic:iterate id="subgrupo" name="listaSubgrupos" scope="request">
        codSubGrupos[contador] = ['<bean:write name="subgrupo" property="id" />'];
        descSubGrupos[contador] = ['<bean:write name="subgrupo" property="label" />'];
        contador++;
        </logic:iterate>
        var comboSubGrupos = new Combo("SubGrupo");
        comboSubGrupos.addItems(codSubGrupos, descSubGrupos);
        comboSubGrupos.change = cargarDatosSubGrupo;

        inicio();
    </script>
</body>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>

<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide47.i18n.MeLanbide47I18n" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@ page import="es.altia.common.service.config.Config"%>
<%@ page import="es.altia.common.service.config.ConfigServiceHelper"%>
<%@page import="java.util.ArrayList" %>
<%@page import="java.util.List" %>
<%@page import="java.util.Iterator" %>
<%@page import="java.util.Map" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide47.vo.comun.SelectItem"%>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide47.vo.orientacion.OriTrayOtroProgramaVO"%>
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

    String tituloPagina = meLanbide47I18n.getMensaje(idiomaUsuario, "label.trayectoria.otrosProgramas");
    OriTrayOtroProgramaVO trayecModif =(OriTrayOtroProgramaVO)request.getAttribute("trayecModif");
    String modoDatos = (String)request.getAttribute("modoDatos") != null ? (String)request.getAttribute("modoDatos") : "";

    List<SelectItem> listaEntidades = new ArrayList<SelectItem>();

    String lcodEntidades = "";
    String ldescEntidades = "";
     
    if(request.getAttribute("listaEntidades") != null)
        listaEntidades = (List<SelectItem>)request.getAttribute("listaEntidades");
 // Entidades
    if (listaEntidades != null && listaEntidades.size() > 0) 
    {
       int i;
        SelectItem si = null;
        for (i = 0; i < listaEntidades.size() - 1; i++) 
        {
            si = (SelectItem) listaEntidades.get(i);
            lcodEntidades += "\"" + si.getId() + "\",";
            ldescEntidades += "\"" + escape(si.getLabel()) + "\",";
        }
        si = (SelectItem) listaEntidades.get(i);
        lcodEntidades += "\"" + si.getId() + "\"";
       ldescEntidades += "\"" + escape(si.getLabel()) + "\"";
    }
     int nroItemsDesplegableEntidad = listaEntidades.size();
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

<script type="text/javascript">
    var mensajeValidacion;
    var repetidos = false;

    var codEntidades = [<%=lcodEntidades%>];
    var descEntidades = [<%=ldescEntidades%>];
    // var codEntidades =new Array();
    //  var descEntidades = new Array();

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
        cargarCombos();
        cargarCodigosCombos();
        cargarDescripcionesCombos();
        var nroItems = '<%=nroItemsDesplegableEntidad%>';
        var modoDatos = '<%=modoDatos%>';
        if(modoDatos='0' && nroItems>'0'){
            comboEntidad.selectItem(1);
        }
    }
    function cargarCombos() {
        comboEntidad.addItems(codEntidades, descEntidades);
    }
    function cargarCodigosCombos() {
        document.getElementById('codEntidad').value = '<%=trayecModif != null && trayecModif.getCodEntidad()!=null ? String.valueOf(trayecModif.getCodEntidad()): ""%>';
    }
    function cargarDescripcionesCombos() {
        var desc = "";
        var codAct = "";
        var codigo = "";

        codigo = "<%=trayecModif != null && trayecModif.getCodEntidad()!=null ? String.valueOf(trayecModif.getCodEntidad()) : ""%>";
        if (codigo != null && codigo != "")
        {
            for (var i = 0; i < codEntidades.length; i++)
            {
                codAct = codEntidades[i];
                if (codAct == codigo)
                {
                    desc = descEntidades[i];
                }
            }
        }
        document.getElementById('descEntidad').value = desc;
    }
    function guardarTrayectoriaOtrosProg() {
        if (validarTrayectoriaOtrosProgramas()) {
            var idTray = '<%=trayecModif!=null && trayecModif.getCodIdOtroPrograma()!=null?trayecModif.getCodIdOtroPrograma():""%>';
            var codEntidad = document.getElementById('codEntidad').value;
            var descEntidad = escape(document.getElementById('descEntidad').value);
            var programa = escape(document.getElementById('programa').value);
            var anioOP = document.getElementById('anio').value;
            var duracionOP = document.getElementById('duracion').value;
            var anioOPVal = document.getElementById('anioVal').value;
            var duracionOPVal = document.getElementById('duracionVal').value;

            var ajax = getXMLHttpRequest();
            var nodos = null;
            var CONTEXT_PATH = '<%=request.getContextPath()%>'
            var url = CONTEXT_PATH + "/PeticionModuloIntegracion.do";
            var parametros = "";
            var control = new Date();
            parametros = 'tarea=preparar&modulo=MELANBIDE47&operacion=guardarTrayectoriaOtrosProgramas&tipo=0&modoDatos=<%=modoDatos%>&numero=<%=numExpediente%>'
                    + '&idTray=' + idTray
                    + '&codEntidad=' + codEntidad
                    + '&programa=' + programa
                    + '&anio=' + anioOP
                    + '&duracion=' + duracionOP
                    + '&anioVal=' + anioOPVal
                    + '&duracionVal=' + duracionOPVal
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
                            for (var cont = 0; cont < hijosFila.length; cont++) {      // recorremos los nodos TRAYECTORIA
                                if (hijosFila[cont].nodeName == "ORI_OTRPRO_ID") {
                                    nodoCampo = hijosFila[cont];
                                    if (nodoCampo.childNodes.length > 0) {
                                        fila[0] = nodoCampo.childNodes[0].nodeValue;
                                    } else {
                                        fila[0] = '-';
                                    }
                                } else if (hijosFila[cont].nodeName == "ORI_OTRPRO_EXP_EJE") {
                                    nodoCampo = hijosFila[cont];
                                    if (nodoCampo.childNodes.length > 0) {
                                        fila[1] = nodoCampo.childNodes[0].nodeValue;
                                    } else {
                                        fila[1] = '-';
                                    }
                                } else if (hijosFila[cont].nodeName == "ORI_OTRPRO_NUMEXP") {
                                    nodoCampo = hijosFila[cont];
                                    if (nodoCampo.childNodes.length > 0) {
                                        fila[2] = nodoCampo.childNodes[0].nodeValue;
                                    } else {
                                        fila[2] = '-';
                                    }
                                } else if (hijosFila[cont].nodeName == "ORI_OTRPRO_COD_ENTIDAD") {
                                    nodoCampo = hijosFila[cont];
                                    if (nodoCampo.childNodes.length > 0) {
                                        fila[3] = nodoCampo.childNodes[0].nodeValue;
                                    } else {
                                        fila[3] = '-';
                                    }
                                } else if (hijosFila[cont].nodeName == "ORI_OTRPRO_PROGRAMA") {
                                    nodoCampo = hijosFila[cont];
                                    if (nodoCampo.childNodes.length > 0) {
                                        fila[4] = nodoCampo.childNodes[0].nodeValue;
                                    } else {
                                        fila[4] = '-';
                                    }
                                } else if (hijosFila[cont].nodeName == "ORI_OTRPRO_PROG_EJE") {
                                    nodoCampo = hijosFila[cont];
                                    if (nodoCampo.childNodes.length > 0) {
                                        fila[5] = nodoCampo.childNodes[0].nodeValue;
                                    } else {
                                        fila[5] = '-';
                                    }
                                } else if (hijosFila[cont].nodeName == "ORI_OTRPRO_DURACION") {
                                    nodoCampo = hijosFila[cont];
                                    if (nodoCampo.childNodes.length > 0) {
                                        fila[6] = nodoCampo.childNodes[0].nodeValue;
                                    } else {
                                        fila[6] = '-';
                                    }
                                } else if (hijosFila[cont].nodeName == "ORI_ENT_CIF") {
                                    nodoCampo = hijosFila[cont];
                                    if (nodoCampo.childNodes.length > 0) {
                                        fila[7] = nodoCampo.childNodes[0].nodeValue;
                                    } else {
                                        fila[7] = '-';
                                    }
                                }else if (hijosFila[cont].nodeName == "ORI_ENT_NOM"){
                                    nodoCampo = hijosFila[cont];
                                    if (nodoCampo.childNodes.length > 0) {
                                        fila[8] = nodoCampo.childNodes[0].nodeValue;
                                    } else {
                                        fila[8] = '-';
                                    } 
                                }
                                else if (hijosFila[cont].nodeName == "ORI_OTRPRO_PROG_EJE_VALID"){
                                    nodoCampo = hijosFila[cont];
                                    if (nodoCampo.childNodes.length > 0) {
                                        fila[9] = nodoCampo.childNodes[0].nodeValue;
                                    } else {
                                        fila[9]= '-';
                                    } 
                                }
                                else if (hijosFila[cont].nodeName == "ORI_OTRPRO_DURACION_VALID"){
                                    nodoCampo = hijosFila[cont];
                                    if (nodoCampo.childNodes.length > 0) {
                                        fila[10] = nodoCampo.childNodes[0].nodeValue;
                                    } else {
                                        fila[10] = '-';
                                    } 
                                }

                            }// trayectoria

                            listaDatosRespuesta[cont1 + 1] = fila;
                            fila = new Array();
                        }
                    }
                }//for(j=0;hijos!=null && j<hijos.length;j++)
                if (codigoOperacion == "0") {
                    self.parent.opener.retornoXanelaAuxiliar(listaDatosRespuesta);
                    jsp_alerta("A", '<%=meLanbide47I18n.getMensaje(idiomaUsuario,"msg.datosGuardadosOK")%>');
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
        }else {
            jsp_alerta("A", escape(mensajeValidacion));
        }
    }

    function validarTrayectoriaOtrosProgramas() {
        mensajeValidacion = '';
        var codigo = '';
        codigo = document.getElementById('codEntidad').value;
        if (codigo == '') {
            mensajeValidacion = '<%=meLanbide47I18n.getMensaje(idiomaUsuario,"msg.generico.campos.obligatorios")%>';
            return false;
        }
        var variable = document.getElementById('programa').value;
        if (variable == '') {
            mensajeValidacion = '<%=meLanbide47I18n.getMensaje(idiomaUsuario,"msg.generico.campos.obligatorios")%>';
            return false;
        } else {
            if (!comprobarCaracteresEspecialesOri14(variable)) {
                mensajeValidacion = '<%= meLanbide47I18n.getMensaje(idiomaUsuario,"msg.generico.caracteres.noPermitidos")%>';
                return false;
            }
        }
        variable = document.getElementById('anio').value;
        if (variable == '') {
            mensajeValidacion = '<%=meLanbide47I18n.getMensaje(idiomaUsuario,"msg.generico.campos.obligatorios")%>';
            return false;
        } else {
            if (isNaN(variable)) {
                mensajeValidacion = '<%=meLanbide47I18n.getMensaje(idiomaUsuario,"msg.generico.anioNaN")%>';
                return false;
            }
        }
        variable = document.getElementById('duracion').value;
        if (variable == '') {
            mensajeValidacion = '<%=meLanbide47I18n.getMensaje(idiomaUsuario,"msg.generico.campos.obligatorios")%>';
            return false;
        } else {
            if (isNaN(variable)) {
                mensajeValidacion = '<%=meLanbide47I18n.getMensaje(idiomaUsuario,"msg.generico.duracionNaN")%>';
                return false;
            }
        }
        return true;
    }

    function validarNumerico(numero) {
        try {
            if (Trim(numero.value) != '') {
                return /^([0-9])*$/.test(numero.value);
            }
        } catch (err) {
            return false;
        }
    }

    function cerrarVentana() {
        window.close();
    }

    function confirmarSalida() {
        return '<%=meLanbide47I18n.getMensaje(idiomaUsuario,"msg.confirmarSalida")%>';
    }
    function copiarDatosAValidacion(){
        document.getElementById('anioVal').value=document.getElementById('anio').value;
        document.getElementById('duracionVal').value=document.getElementById('duracion').value;
    }
</script>

<body class="contenidoPantalla">
    <div align="center">
        <div style="clear: both; overflow-x: hidden; overflow-y: auto; height: 650px;">
            <fieldset id="fieldsetOtrosProgramas" name="fieldsetOtrosProgramas" style="width: 935px;">
                <legend class="legendAzul"><%=meLanbide47I18n.getMensaje(idiomaUsuario,"label.trayectoria.tituloPestana.otrosProgramas")%></legend>
                <div class="lineaFormulario" style="padding-left: 5px; padding-right: 5px; text-align: left;">
                    <!-- ENTIDAD -->
                    <label for="codEntidad" style="vertical-align: bottom;"><%=meLanbide47I18n.getMensaje(idiomaUsuario,"label.trayectoria.otrosProgramas.entidad")%></label><font color="red">*</font>                        
                    <input id="codEntidad" name="codEntidad" type="text" class="inputTexto" size="4" maxlength="3" style="margin-left: 19px;"
                           onkeypress="javascript:return SoloDigitosConsulta(event);">
                    <input id="descEntidad" name="descEntidad" type="text" class="inputTexto" size="70" readonly>
                    <!--anchor-->
                    <a id="anchorEntidad" name="anchorEntidad" href=""><img src="<%=request.getContextPath()%>/images/listas/botondesplegable.gif" id="botonEntidad" name="botonEntidad" border="0" width="14" height="14" style="cursor:hand;" alt="<%=meLanbide47I18n.getMensaje(idiomaUsuario, "altDesplegable")%>" title="<%=meLanbide47I18n.getMensaje(idiomaUsuario, "altDesplegable")%>"></a>
                </div>
                <div class="lineaFormulario" style="padding-left: 5px; padding-right: 5px; text-align: left;">
                    <!-- PROGRAMA -->  
                    <label for="programa" style="vertical-align: top;"><%=meLanbide47I18n.getMensaje(idiomaUsuario,"label.trayectoria.otrosProgramas.tabla.programa")%></label><font color="red" style="margin-right: 44px; vertical-align: top;">*</font>
                    <textarea id="programa" name="programa" class="inputTexto" rows="8" cols="124" maxlength="500"><%=trayecModif != null && trayecModif.getPrograma()!=null ? trayecModif.getPrograma() : ""%></textarea>

                </div>
                
                
                <div class="lineaFormulario" style="padding-left: 5px; padding-right: 5px; text-align: left;">
                    <table>
                        <tbody>
                            <tr>
                                <td style="width: 50%">
                                    <div class="lineaFormulario" style="padding-left: 5px; padding-right: 5px; text-align: left;  position: relative;">
                                        <!-- ANIO -->
                                        <label for="anio" style="vertical-align: top;"><%=meLanbide47I18n.getMensaje(idiomaUsuario, "label.trayectoria.otrosProgramas.tabla.anio")%></label><font color="red" style="margin-right: 44px; vertical-align: top;">*</font>
                                        <input id="anio" name="anio" type="text" class="inputTexto" size="5" maxlength="4" style="position: absolute;right: 5px;" value="<%=(trayecModif !=null && trayecModif.getAnioPrograma()!=null ? trayecModif.getAnioPrograma():"")%>">                   
                                    </div>
                                </td>
                                <td align="right">
                                    <div class="lineaFormulario" style="padding-left: 5px; padding-right: 5px; text-align: right; position: relative;">
                                        <!-- ANIO VAL-->
                                        <label for="anioVal" style="vertical-align: top;"><%=meLanbide47I18n.getMensaje(idiomaUsuario, "label.trayectoria.otrosProgramas.tabla.anio.validado")%></label><font color="red" style="margin-right: 55px; vertical-align: top;"></font>
                                        <input id="anioVal" name="anioVal" type="text" class="inputTexto" style="position: absolute; right: 5px;" size="5" maxlength="4" value="<%=(trayecModif !=null && trayecModif.getAnioProgramaVal()!=null ? trayecModif.getAnioProgramaVal():"")%>">                                   
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <td style="width: 50%">
                                    <div class="lineaFormulario" style="padding-left: 5px; padding-right: 5px; text-align: left; position: relative;" >
                                        <!-- DURACION -->
                                        <label for="duracion" style="vertical-align: top;"><%=meLanbide47I18n.getMensaje(idiomaUsuario,"label.trayectoria.otrosProgramas.tabla.duracion")%></label><font color="red" style="margin-right: 44px; vertical-align: top;">*</font>
                                        <input id="duracion" name="duracion" type="text" class="inputTexto" style="position: absolute; right: 5px;" size="5" maxlength="4" value="<%=(trayecModif !=null && trayecModif.getDuracion()!=null ? trayecModif.getDuracion():"")%>">
                                    </div>
                                </td>
                                <td align="right">
                                    <div class="lineaFormulario" style="padding-left: 5px; padding-right: 5px; text-align: right; position: relative;">
                                        <!-- DURACION VAL-->
                                        <label for="duracionVal" style="vertical-align: top;"><%=meLanbide47I18n.getMensaje(idiomaUsuario,"label.trayectoria.otrosProgramas.tabla.duracion.validada")%></label><font color="red" style="margin-right: 55px; vertical-align: top;"></font>
                                        <input id="duracionVal" name="duracionVal" type="text" class="inputTexto" style="position: absolute; right: 5px;" size="5" maxlength="4" value="<%=(trayecModif !=null && trayecModif.getDuracionVal()!=null ? trayecModif.getDuracionVal():"")%>">
                                    </div>
                                </td>
                            </tr>
                            <tr>
                                <td align="right" style="width: 50%">
                                    <div class="lineaFormulario">
                                        <input type="button" id="btnCopiarDatosVal" name="btn.copiarDatos" class="botonLargo" style="margin-top: 5px" value="<%=meLanbide47I18n.getMensaje(idiomaUsuario,"btn.copiarDatos")%>" onclick="copiarDatosAValidacion()"/>
                                    </div>
                                </td>
                                <td></td>
                            </tr>
                        </tbody>
                    </table>
                </div>
                <div class="botonera" style="text-align: right; margin-bottom: 10px">
                    <input type="button" id="btnAnadir" name="btnAnadir" class="botonGeneral" value="<%=meLanbide47I18n.getMensaje(idiomaUsuario,"btn.guardar")%>" onclick="guardarTrayectoriaOtrosProg()"/>
                </div>
            </fieldset>
        </div>
    </div>
</body>

<script type="text/javascript">
    var comboEntidad = new Combo("Entidad");
    inicio();
</script>                    
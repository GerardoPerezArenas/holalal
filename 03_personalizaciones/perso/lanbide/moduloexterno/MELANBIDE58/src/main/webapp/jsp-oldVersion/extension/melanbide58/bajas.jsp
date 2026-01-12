<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>

<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide58.i18n.MeLanbide58I18n" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.common.service.config.Config"%>
<%@page import="es.altia.common.service.config.ConfigServiceHelper"%>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide58.vo.BajaVO"%>
<%@page import="java.util.ArrayList" %>
<%@page import="java.util.List" %>
<%@page import="java.text.DateFormat" %>
<%@page import="java.text.SimpleDateFormat" %>

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
        //nuevas
                UsuarioValueObject usuarioVO = new UsuarioValueObject();
                int idioma = 1;
                int apl = 5;
                String css = "";
                if (session.getAttribute("usuario") != null) {
                        usuarioVO = (UsuarioValueObject) session.getAttribute("usuario");
                        apl = usuarioVO.getAppCod();
                        idioma = usuarioVO.getIdioma();
                        css = usuarioVO.getCss();
                }

    //Clase para internacionalizar los mensajes de la aplicaci�n.
    MeLanbide58I18n meLanbide58I18n = MeLanbide58I18n.getInstance();
    String numExpediente = (String)request.getAttribute("numExp");
    //String codOrganizacion  = request.getParameter("codOrganizacionModulo");
    //String nombreModulo     = request.getParameter("nombreModulo");
    
%>
<jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
<jsp:setProperty name="descriptor"  property="idi_cod" value="<%=idioma%>" />
<jsp:setProperty name="descriptor"  property="apl_cod" value="<%=apl%>" />
<link rel="StyleSheet" media="screen" type="text/css" href="<%=request.getContextPath()%><%=css%>">
<script type="text/javascript">
    function pulsarNuevoBaja() {
        var control = new Date();
        lanzarPopUpModal('<%=request.getContextPath()%>/PeticionModuloIntegracion.do?tarea=preparar&modulo=MELANBIDE58&operacion=cargarNuevoBaja&tipo=0&numExp=<%=numExpediente%>&nuevo=1&control=' + control.getTime(), 650, 1000, 'no', 'no', function (result) {
            if (result != undefined) {
                if (result[0] == '0') {
                    recargarTablaBajas(result);
                }
            }
        });
    }

    function pulsarModificarBaja() {
        if (tabaBajas.selectedIndex != -1) {
            var control = new Date();
            lanzarPopUpModal('<%=request.getContextPath()%>/PeticionModuloIntegracion.do?tarea=preparar&modulo=MELANBIDE58&operacion=cargarModificarBaja&tipo=0&nuevo=0&numExp=<%=numExpediente%>&id=' + listaBajas[tabaBajas.selectedIndex][0] + '&control=' + control.getTime(), 650, 1000, 'no', 'no', function (result) {
                if (result != undefined) {
                    if (result[0] == '0') {
                        recargarTablaBajas(result);
                    }
                }
            });
        } else {
            jsp_alerta('A', '<%=meLanbide58I18n.getMensaje(idiomaUsuario, "msg.msjNoSelecFila")%>');
        }
    }

    function pulsarExportarAnexoAbaja() {
        var apellidosB = "";
        var control = new Date();
        if (document.getElementById('apellidosB').value == null || document.getElementById('apellidosB').value == '') {
            apellidosB = "";
        } else {
            apellidosB = document.getElementById('apellidosB').value.replace(/\'/g, "''");
        }
        var CONTEXT_PATH = '<%=request.getContextPath()%>'
        var url = CONTEXT_PATH + "/PeticionModuloIntegracion.do";
        var parametros = "";
        parametros = '?tarea=preparar&modulo=MELANBIDE58&operacion=generarExcelAnexoAbajas&tipo=0&numExp=<%=numExpediente%>&apellidosB=' + escape(apellidosB) + '&numLineaB=' + document.getElementById('numLineaB').value + '&nifB=' + document.getElementById('nifB').value + '&control=' + control.getTime();
        //alert("parametros: " + parametros);
        window.open(url + parametros, "_blank");
    }

    function pulsarEliminarBaja() {
        if (tabaBajas.selectedIndex != -1) {
            var resultado = jsp_alerta('', '<%=meLanbide58I18n.getMensaje(idiomaUsuario, "msg.preguntaEliminarBaja")%>');
            /*if (resultado == 1) {
             if (resultado[0] == '0') {
             recargarTablaBajas(resultado);
             }
             }*/
            if (resultado == 1) {

                var ajax = getXMLHttpRequest();
                var nodos = null;
                var CONTEXT_PATH = '<%=request.getContextPath()%>'
                var url = CONTEXT_PATH + "/PeticionModuloIntegracion.do";
                var parametros = "";
                var control = new Date();
                parametros = 'tarea=preparar&modulo=MELANBIDE58&operacion=eliminarBaja&tipo=0&numExp=<%=numExpediente%>&id=' + listaBajas[tabaBajas.selectedIndex][0] + '&control=' + control.getTime();
                try {
                    ajax.open("POST", url, false);
                    ajax.setRequestHeader("Content-Type", "application/x-www-form-urlencoded; charset=ISO-8859-1");
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
                    var listaNueva = new Array();
                    var fila = new Array();
                    var nodoFila;
                    var hijosFila;
                    for (j = 0; hijos != null && j < hijos.length; j++) {
                        if (hijos[j].nodeName == "CODIGO_OPERACION") {
                            codigoOperacion = hijos[j].childNodes[0].nodeValue;
                            listaNueva[j] = codigoOperacion;
                        }//if(hijos[j].nodeName=="CODIGO_OPERACION")                      
                        else if (hijos[j].nodeName == "FILA") {
                            nodoFila = hijos[j];
                            hijosFila = nodoFila.childNodes;
                            for (var cont = 0; cont < hijosFila.length; cont++) {
                                if (hijosFila[cont].nodeName == "ID") {
                                    if (hijosFila[cont].childNodes.length > 0) {
                                        fila[0] = hijosFila[cont].childNodes[0].nodeValue;
                                    } else {
                                        fila[0] = '-';
                                    }
                                } else if (hijosFila[cont].nodeName == "NUMLINEA") {
                                    if (hijosFila[cont].childNodes[0].nodeValue != "null") {
                                        fila[1] = hijosFila[cont].childNodes[0].nodeValue;
                                    } else {
                                        fila[1] = '-';
                                    }
                                } else if (hijosFila[cont].nodeName == "APELLIDOS") {
                                    if (hijosFila[cont].childNodes[0].nodeValue != "null") {
                                        fila[2] = hijosFila[cont].childNodes[0].nodeValue;
                                    } else {
                                        fila[2] = '-';
                                    }
                                } else if (hijosFila[cont].nodeName == "NOMBRE") {
                                    if (hijosFila[cont].childNodes[0].nodeValue != "null") {
                                        fila[3] = hijosFila[cont].childNodes[0].nodeValue;
                                    } else {
                                        fila[3] = '-';
                                    }
                                } else if (hijosFila[cont].nodeName == "FECHABAJA") {
                                    if (hijosFila[cont].childNodes.length > 0) {
                                        fila[4] = hijosFila[cont].childNodes[0].nodeValue;
                                    } else {
                                        fila[4] = '-';
                                    }
                                } else if (hijosFila[cont].nodeName == "NIF") {
                                    if (hijosFila[cont].childNodes[0].nodeValue != "null") {
                                        fila[5] = hijosFila[cont].childNodes[0].nodeValue;
                                    } else {
                                        fila[5] = '-';
                                    }
                                } else if (hijosFila[cont].nodeName == "NUMSS") {
                                    if (hijosFila[cont].childNodes[0].nodeValue != "null") {
                                        fila[6] = hijosFila[cont].childNodes[0].nodeValue;
                                    } else {
                                        fila[6] = '-';
                                    }
                                } else if (hijosFila[cont].nodeName == "DES_NOM") {
                                    if (hijosFila[cont].childNodes[0].nodeValue != "null") {
                                        fila[7] = hijosFila[cont].childNodes[0].nodeValue;
                                    } else {
                                        fila[7] = '-';
                                    }
                                }
                            }
                            listaNueva[j] = fila;
                            fila = new Array();
                        }
                    }//for(j=0;hijos!=null && j<hijos.length;j++)
                    if (codigoOperacion == "0") {
                        recargarTablaBajas(listaNueva);
                    } else if (codigoOperacion == "1") {
                        jsp_alerta("A", '<%=meLanbide58I18n.getMensaje(idiomaUsuario,"error.errorBD")%>');
                    } else if (codigoOperacion == "2") {
                        jsp_alerta("A", '<%=meLanbide58I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                    } else if (codigoOperacion == "3") {
                        jsp_alerta("A", '<%=meLanbide58I18n.getMensaje(idiomaUsuario,"error.pasoParametros")%>');
                    } else {
                        jsp_alerta("A", '<%=meLanbide58I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                    }//if(
                } catch (Err) {
                    jsp_alerta("A", '<%=meLanbide58I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                }//try-catch
            }
        } else {
            jsp_alerta('A', '<%=meLanbide58I18n.getMensaje(idiomaUsuario, "msg.msjNoSelecFila")%>');
        }
    }

    function pulsarLimpiarBaja() {
        document.getElementById("numLineaB").value = "";
        document.getElementById("nifB").value = "";
        document.getElementById("apellidosB").value = "";
        pulsarBuscarBaja();
    }

    function pulsarBuscarBaja() {
        mensajeValidacion = "";
        if (validarNumericoVacio(document.getElementById('numLineaB').value)) {
            if (validarTresCaracteresApellido(document.getElementById('apellidosB').value)) {
                var ajax = getXMLHttpRequest();
                var nodos = null;
                var CONTEXT_PATH = '<%=request.getContextPath()%>'
                var url = CONTEXT_PATH + "/PeticionModuloIntegracion.do";
                var parametros = "";
                var control = new Date();
                var apellidosB = "";
                var nifB = "";

                if (document.getElementById('apellidosB').value == null || document.getElementById('apellidosB').value == '') {
                    apellidosB = "";
                } else {
                    apellidosB = document.getElementById('apellidosB').value.replace(/\'/g, "''");
                }

                if (document.getElementById('nifB').value == null || document.getElementById('nifB').value == '') {
                    nifB = "";
                } else {
                    nifB = document.getElementById('nifB').value.toUpperCase();
                }

                parametros = 'tarea=preparar&modulo=MELANBIDE58&operacion=buscarBaja&tipo=0&numExp=<%=numExpediente%>&apellidosB=' + escape(apellidosB) + '&numLineaB=' + document.getElementById('numLineaB').value + '&nifB=' + nifB + '&control=' + control.getTime();

                try {
                    ajax.open("POST", url, false);
                    ajax.setRequestHeader("Content-Type", "application/x-www-form-urlencoded; charset=ISO-8859-1");
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
                    var listaNueva = new Array();
                    var fila = new Array();
                    var nodoFila;
                    var hijosFila;
                    for (j = 0; hijos != null && j < hijos.length; j++) {
                        if (hijos[j].nodeName == "CODIGO_OPERACION") {
                            codigoOperacion = hijos[j].childNodes[0].nodeValue;
                            listaNueva[j] = codigoOperacion;
                        }//if(hijos[j].nodeName=="CODIGO_OPERACION")                      
                        else if (hijos[j].nodeName == "FILA") {
                            nodoFila = hijos[j];
                            hijosFila = nodoFila.childNodes;
                            for (var cont = 0; cont < hijosFila.length; cont++) {
                                if (hijosFila[cont].nodeName == "ID") {
                                    if (hijosFila[cont].childNodes.length > 0) {
                                        fila[0] = hijosFila[cont].childNodes[0].nodeValue;
                                    } else {
                                        fila[0] = '-';
                                    }
                                } else if (hijosFila[cont].nodeName == "NUMLINEA") {
                                    if (hijosFila[cont].childNodes[0].nodeValue != "null") {
                                        fila[1] = hijosFila[cont].childNodes[0].nodeValue;
                                    } else {
                                        fila[1] = '-';
                                    }
                                } else if (hijosFila[cont].nodeName == "APELLIDOS") {
                                    if (hijosFila[cont].childNodes[0].nodeValue != "null") {
                                        fila[2] = hijosFila[cont].childNodes[0].nodeValue;
                                    } else {
                                        fila[2] = '-';
                                    }
                                } else if (hijosFila[cont].nodeName == "NOMBRE") {
                                    if (hijosFila[cont].childNodes[0].nodeValue != "null") {
                                        fila[3] = hijosFila[cont].childNodes[0].nodeValue;
                                    } else {
                                        fila[3] = '-';
                                    }
                                } else if (hijosFila[cont].nodeName == "FECHABAJA") {
                                    if (hijosFila[cont].childNodes.length > 0) {
                                        fila[4] = hijosFila[cont].childNodes[0].nodeValue;
                                    } else {
                                        fila[4] = '-';
                                    }
                                } else if (hijosFila[cont].nodeName == "NIF") {
                                    if (hijosFila[cont].childNodes[0].nodeValue != "null") {
                                        fila[5] = hijosFila[cont].childNodes[0].nodeValue;
                                    } else {
                                        fila[5] = '-';
                                    }
                                } else if (hijosFila[cont].nodeName == "NUMSS") {
                                    if (hijosFila[cont].childNodes[0].nodeValue != "null") {
                                        fila[6] = hijosFila[cont].childNodes[0].nodeValue;
                                    } else {
                                        fila[6] = '-';
                                    }
                                } else if (hijosFila[cont].nodeName == "DES_NOM") {
                                    if (hijosFila[cont].childNodes[0].nodeValue != "null") {
                                        fila[7] = hijosFila[cont].childNodes[0].nodeValue;
                                    } else {
                                        fila[7] = '-';
                                    }
                                }
                            }
                            listaNueva[j] = fila;
                            fila = new Array();
                        }
                    }//for(j=0;hijos!=null && j<hijos.length;j++)
                    if (codigoOperacion == "0") {
                        recargarTablaBajas(listaNueva);
                    } else if (codigoOperacion == "1") {
                        jsp_alerta("A", '<%=meLanbide58I18n.getMensaje(idiomaUsuario,"msg.vaciaBD")%>');
                    } else if (codigoOperacion == "2") {
                        jsp_alerta("A", '<%=meLanbide58I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                    } else if (codigoOperacion == "3") {
                        jsp_alerta("A", '<%=meLanbide58I18n.getMensaje(idiomaUsuario,"error.pasoParametros")%>');
                    } else if (codigoOperacion == "4") {
                        jsp_alerta("A", '<%=meLanbide58I18n.getMensaje(idiomaUsuario,"msg.vaciaBD")%>');
                    } else {
                        jsp_alerta("A", '<%=meLanbide58I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                    }//if(
                } catch (Err) {
                    jsp_alerta("A", '<%=meLanbide58I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                }//try-catch
            } else {
                mensajeValidacion = '<%=meLanbide58I18n.getMensaje(idiomaUsuario, "msg.apellidos.errLongitud")%>';
                jsp_alerta("A", mensajeValidacion);
            }
        } else {
            mensajeValidacion = '<%=meLanbide58I18n.getMensaje(idiomaUsuario, "msg.numLinea.errNumerico")%>';
            jsp_alerta("A", mensajeValidacion);
        }
    }

    function crearTablaBajas() {


        tabaBajas = new Tabla(true, '<%=descriptor.getDescripcion("buscar")%>', '<%=descriptor.getDescripcion("anterior")%>', '<%=descriptor.getDescripcion("siguiente")%>', '<%=descriptor.getDescripcion("mosFilasPag")%>', '<%=descriptor.getDescripcion("msgNoResultBusq")%>', '<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>', '<%=descriptor.getDescripcion("filtrDeTotal")%>', '<%=descriptor.getDescripcion("primero")%>', '<%=descriptor.getDescripcion("ultimo")%>', document.getElementById('listaBajas'));
        tabaBajas.addColumna('0', 'center', "<%=meLanbide58I18n.getMensaje(idiomaUsuario,"bajas.tablaBajas.col1")%>");
        tabaBajas.addColumna('0', 'center', "<%=meLanbide58I18n.getMensaje(idiomaUsuario,"bajas.tablaBajas.col6")%>");
        tabaBajas.addColumna('350', 'center', "<%=meLanbide58I18n.getMensaje(idiomaUsuario,"bajas.tablaBajas.col2")%>");
        tabaBajas.addColumna('150', 'center', "<%=meLanbide58I18n.getMensaje(idiomaUsuario,"bajas.tablaBajas.col3")%>");
        tabaBajas.addColumna('150', 'center', "<%=meLanbide58I18n.getMensaje(idiomaUsuario,"bajas.tablaBajas.col4")%>");
        tabaBajas.addColumna('150', 'center', "<%=meLanbide58I18n.getMensaje(idiomaUsuario,"bajas.tablaBajas.col5")%>");
        tabaBajas.addColumna('150', 'center', "<%=meLanbide58I18n.getMensaje(idiomaUsuario,"bajas.tablaBajas.col7")%>");

        tabaBajas.displayCabecera = true;
        tabaBajas.height = 300;
    }

    function recargarTablaBajas(result) {
        var fila;
        listaBajas = new Array();
        listaBajasTabla = new Array();
        for (var i = 1; i < result.length; i++) {
            fila = result[i];
            //listaBajas[i - 1] = fila;//NO FUNCIONA ie9
            listaBajas[i - 1] = [fila[0], fila[1], fila[2], fila[3], fila[4], fila[5], fila[6], fila[7]];
            listaBajasTabla[i - 1] = [fila[0], fila[1], fila[2] + ", " + fila[3],
                fila[4], fila[5], fila[6], fila[7]];
        }

        crearTablaBajas();

        tabaBajas.lineas = listaBajasTabla;
        tabaBajas.displayTabla();
        //document.getElementById('listaBajas').children[0].children[1].children[0].children[0].ondblclick = function(event){
        //        pulsarModificarBaja(event);
        if (navigator.appName.indexOf("Internet Explorer") != -1 || (navigator.appName.indexOf("Netscape") != -1 && navigator.appCodeName.match(/Mozilla/))) {
            try {
                var div = document.getElementById('listaBajas');
                //div.children[0].children[0].children[0].children[0].children[0].children[0].children[0].children[0].children[0].children[0].children[0].children[0].children[0].children[1].style.width = '100%';
                //div.children[0].children[1].children[0].children[0].children[0].children[0].children[0].children[0].children[0].children[0].children[0].children[1].style.width = '100%';
                if (navigator.appName.indexOf("Internet Explorer") != -1 && !(navigator.userAgent.match(/compatible/))) {
                    div.children[0].children[0].children[0].children[0].children[0].children[0].children[1].style.width = '100%';
                    div.children[0].children[1].children[0].children[0].children[0].children[0].style.width = '100%';
                } else {
                    div.children[0].children[0].children[0].children[0].children[0].children[0].style.width = '100%';
                    div.children[0].children[1].children[0].children[0].style.width = '100%';
                }
            } catch (err) {

            }
        }
    }

    function validarNumericoVacio(numero) {
        try {
            if (numero == null || numero == '') {
                return true;
            } else {
                if (Trim(numero) != '') {
                    return /^([0-9])*$/.test(numero);
                }
            }
        } catch (err) {
            return false;
        }
    }

    function validarTresCaracteresApellido(apellido) {
        try {
            var numCarac;
            if (apellido == null || apellido == "") {
                return true;
            } else {
                if (Trim(apellido) != "") {
                    numCarac = apellido.length;
                    if (numCarac < 3) {
                        return false;
                    } else {
                        return true;
                    }
                }
            }
        } catch (err) {
            return false;
        }
    }

</script>


<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/extension/melanbide58/melanbide58.css"/>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/validaciones.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/popup.js"></script>

<script type="text/javascript">
    var APP_CONTEXT_PATH = '<%=request.getContextPath()%>';
</script>
<!-- Eventos onKeyPress compatibilidad firefox  -->
<!--<script type="text/javascript" src="http://code.jquery.com/jquery-1.4.4.js"></script>-->
<div class="tab-page" id="tabPage356" style="height:520px; width: 100%;">
    <h2 class="legendAzul" id="pestanaPrinc"><%=meLanbide58I18n.getMensaje(idiomaUsuario,"label.tituloPrincipalBajas")%></h2>
    <div> 
        <br/>
        <div style="height:8px; width: 900px;">
            <div class="lineaFormulario">
                <div style="width: 120px; float: left;" class="etiqueta">
                    <%=meLanbide58I18n.getMensaje(idiomaUsuario,"label.numLinea")%>
                </div>
                <div style="width: 120px; float: left;">
                    <div style="float: left;">
                        <input id="numLineaB" name="numLineaB" type="text" class="inputTexto" size="15" maxlength="5" 
                               value=""/>
                    </div>
                </div>
            </div>
            <div class="lineaFormulario">
                <div style="width: 50px; float: left;" class="etiqueta">
                    <%=meLanbide58I18n.getMensaje(idiomaUsuario,"label.nif")%>
                </div>
                <div style="width: 120px; float: left;">
                    <div style="float: left;">
                        <input id="nifB" name="nifB" type="text" class="inputTexto" size="15" maxlength="15" 
                               onblur="//comprobarCampoBusq();"
                               value=""/>
                    </div>
                </div>
            </div>
            <div class="lineaFormulario">
                <div style="width: 70px; float: left;" class="etiqueta">
                    <%=meLanbide58I18n.getMensaje(idiomaUsuario,"label.apellidos")%>
                </div>
                <div style="width: 120px; float: left;">
                    <div style="float: left;">
                        <input id="apellidosB" name="apellidosB" type="text" class="inputTexto" size="30" maxlength="50" 
                               value=""/>
                    </div>
                </div>
            </div>
            <div class="botonera" style="text-align: right;">
                <input type="button" id="btnLimpiar" name="btnLimpiar" class="botonGeneral"  value="<%=meLanbide58I18n.getMensaje(idiomaUsuario, "btn.limpiar")%>" onclick="pulsarLimpiarBaja();">
                <input type="button" id="btnBuscar" name="btnBuscar" class="botonGeneral"  value="<%=meLanbide58I18n.getMensaje(idiomaUsuario, "btn.buscar")%>" onclick="pulsarBuscarBaja();">
            </div>
        </div>
        <br/>
        <div id="divGeneral">
            <div id="listaBajas"  align="center"></div>
        </div>
        <br/><br>
        <div class="botonera" style="text-align: center;">
            <input type="button" id="btnNuevoBaja" name="btnNuevoBaja" class="botonGeneral"  value="<%=meLanbide58I18n.getMensaje(idiomaUsuario, "btn.nuevo")%>" onclick="pulsarNuevoBaja();">
            <input type="button" id="btnModificarBaja" name="btnModificarBaja" class="botonGeneral" value="<%=meLanbide58I18n.getMensaje(idiomaUsuario, "btn.modificar")%>" onclick="pulsarModificarBaja();">
            <input type="button" id="btnEliminarBaja" name="btnEliminarBaja"   class="botonGeneral" value="<%=meLanbide58I18n.getMensaje(idiomaUsuario, "btn.eliminar")%>" onclick="pulsarEliminarBaja();">
            <input type="button" id="btnExportar" name="btnExportar"   class="botonMasLargo" value="<%=meLanbide58I18n.getMensaje(idiomaUsuario, "btn.Exportar")%>" onclick="pulsarExportarAnexoAbaja();">
        </div>
    </div>  
</div>
<script  type="text/javascript">
    //Tabla Bajas
    var tabaBajas;
    var listaBajas = new Array();
    var listaBajasTabla = new Array();

    crearTablaBajas();

    <%  		
       BajaVO objectVO = null;
       List<BajaVO> List = null;
       if(request.getAttribute("listaBajas")!=null){
           List = (List<BajaVO>)request.getAttribute("listaBajas");
       }													
       if (List!= null && List.size() >0){
           for (int indice=0;indice<List.size();indice++)
           {
               objectVO = List.get(indice);
               DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
               String fecBajaFormat="";
               if(objectVO.getFechaBaja()!=null){
                   fecBajaFormat=dateFormat.format(objectVO.getFechaBaja());
                
               }else{
                   fecBajaFormat="-";
               }

               String numLinea="";
               if(objectVO.getNumLinea()!=null && !"".equals(objectVO.getNumLinea())){
                   numLinea=Integer.toString(objectVO.getNumLinea());
                
               }else{
                   numLinea="-";
               }

               String apellidos="";
               if(objectVO.getApellidos()!=null){
                   apellidos=objectVO.getApellidos();
               }else{
                   apellidos="-";
               }

               String nombre="";
               if(objectVO.getNombre()!=null){
                   nombre=objectVO.getNombre();
               }else{
                   nombre="-";
               }

               String nif="";
               if(objectVO.getNif()!=null){
                   nif=objectVO.getNif();
               }else{
                   nif="-";
               }

               String nSS="";
               if(objectVO.getNumSS()!=null){
                   nSS=objectVO.getNumSS();
               }else{
                   nSS="-";
               }

               String causa="";
               if(objectVO.getCausaDesc()!=null){
                   causa=objectVO.getCausaDesc();
               }else{
                   causa="-";
               }

               String causaDesc="";
               if(objectVO.getCausaDesc()!=null){
                   causaDesc=objectVO.getCausaDesc();
               }else{
                   causaDesc="-";
               }

    %>
    listaBajas[<%=indice%>] = ['<%=objectVO.getId()%>', '<%=numLinea%>', "<%=apellidos%>" + ", " + "<%=nombre%>",
        '<%=fecBajaFormat%>', '<%=nif%>', '<%=nSS%>', '<%=causaDesc%>'];
    listaBajasTabla[<%=indice%>] = ['<%=objectVO.getId()%>', '<%=numLinea%>', "<%=apellidos%>" + ", " + "<%=nombre%>",
        '<%=fecBajaFormat%>', '<%=nif%>', '<%=nSS%>', '<%=causaDesc%>'];
    <%
           }// for
       }// if
    %>

    tabaBajas.lineas = listaBajasTabla;
    tabaBajas.displayTabla();
    //document.getElementById('listaBajas').children[0].children[1].children[0].children[0].ondblclick = function(event){
    //            pulsarModificarBaja(event);
    if (navigator.appName.indexOf("Internet Explorer") != -1 || (navigator.appName.indexOf("Netscape") != -1 && navigator.appCodeName.match(/Mozilla/))) {
        try {
            var div = document.getElementById('listaBajas');
            //div.children[0].children[0].children[0].children[0].children[0].children[0].children[0].children[0].children[0].children[0].children[0].children[0].children[0].children[1].style.width = '100%';
            //div.children[0].children[1].children[0].children[0].children[0].children[0].children[0].children[0].children[0].children[0].children[0].children[1].style.width = '100%';
            if (navigator.appName.indexOf("Internet Explorer") != -1 && !(navigator.userAgent.match(/compatible/))) {
                div.children[0].children[0].children[0].children[0].children[0].children[0].children[1].style.width = '100%';
                div.children[0].children[1].children[0].children[0].children[0].children[0].style.width = '100%';
            } else {
                div.children[0].children[0].children[0].children[0].children[0].children[0].style.width = '100%';
                div.children[0].children[1].children[0].children[0].style.width = '100%';
            }
        } catch (err) {

        }
    }
</script>
<div id="popupcalendar" class="text"></div>         

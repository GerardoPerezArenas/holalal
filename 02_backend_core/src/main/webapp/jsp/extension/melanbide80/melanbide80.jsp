<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>


<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide80.i18n.MeLanbide80I18n" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.common.service.config.Config"%>
<%@page import="es.altia.common.service.config.ConfigServiceHelper"%>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide80.vo.PersonaVO"%>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide80.util.ConfigurationParameter"%>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide80.util.ConstantesMeLanbide80"%>
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
    MeLanbide80I18n meLanbide80I18n = MeLanbide80I18n.getInstance();
    String numExpediente = (String)request.getAttribute("numExp");
    
%>
<jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
<jsp:setProperty name="descriptor"  property="idi_cod" value="<%=idioma%>" />
<jsp:setProperty name="descriptor"  property="apl_cod" value="<%=apl%>" />
<link rel="StyleSheet" media="screen" type="text/css" href="<%=request.getContextPath()%><%=css%>"/>
<script type="text/javascript">

    function pulsarNuevaPersona() {
        lanzarPopUpModal('<%=request.getContextPath()%>/PeticionModuloIntegracion.do?tarea=preparar&modulo=MELANBIDE80&operacion=cargarNuevaPersona&tipo=0&numExp=<%=numExpediente%>&nuevo=1', 680, 800, 'no', 'no', function (result) {
            if (result != undefined) {
                if (result[0] == '0') {
                    recargarTablaPersonas(result);
                }
            }
        });

    }

    function pulsarModificarPersona() {
        if (tablaPersonas.selectedIndex != -1) {
            lanzarPopUpModal('<%=request.getContextPath()%>/PeticionModuloIntegracion.do?tarea=preparar&modulo=MELANBIDE80&operacion=cargarModificarPersona&tipo=0&nuevo=0&numExp=<%=numExpediente%>&id=' + listaPersonas[tablaPersonas.selectedIndex][0], 680, 800, 'no', 'no', function (result) {
                if (result != undefined) {
                    if (result[0] == '0') {
                        recargarTablaPersonas(result);
                    }
                }
            });

        } else {
            jsp_alerta('A', '<%=meLanbide80I18n.getMensaje(idiomaUsuario, "msg.msjNoSelecFila")%>');
        }
    }

    function extraerListaPersonas(nodos) {

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
                    } else if (hijosFila[cont].nodeName == "DNI") {
                        if (hijosFila[cont].childNodes.length > 0) {
                            fila[1] = hijosFila[cont].childNodes[0].nodeValue;
                        } else {
                            fila[1] = '-';
                        }
                    } else if (hijosFila[cont].nodeName == "NOMBRE") {
                        if (hijosFila[cont].childNodes[0].nodeValue != "null") {
                            fila[2] = hijosFila[cont].childNodes[0].nodeValue;
                        } else {
                            fila[2] = '-';
                        }
                    } else if (hijosFila[cont].nodeName == "APEL1") {
                        if (hijosFila[cont].childNodes[0].nodeValue != "null") {
                            fila[3] = hijosFila[cont].childNodes[0].nodeValue;
                        } else {
                            fila[3] = '-';
                        }
                    } else if (hijosFila[cont].nodeName == "APEL2") {
                        if (hijosFila[cont].childNodes[0].nodeValue != "null") {
                            fila[4] = hijosFila[cont].childNodes[0].nodeValue;
                        } else {
                            fila[4] = '-';
                        }
                    } else if (hijosFila[cont].nodeName == "DESTIPCONTA") {
                        if (hijosFila[cont].childNodes[0].nodeValue != "null") {
                            fila[5] = hijosFila[cont].childNodes[0].nodeValue;
                        } else {
                            fila[5] = '-';
                        }
                    } else if (hijosFila[cont].nodeName == "TIPCONTB") {
                        if (hijosFila[cont].childNodes[0].nodeValue != "null") {
                            fila[6] = hijosFila[cont].childNodes[0].nodeValue;
                            var tex = fila[6].toString();
                            tex = tex.replace(".", ",");
                            fila[6] = tex;
                        } else {
                            fila[6] = '-';
                        }
                    } else if (hijosFila[cont].nodeName == "DESJORNADA") {
                        if (hijosFila[cont].childNodes[0].nodeValue != "null") {
                            fila[7] = hijosFila[cont].childNodes[0].nodeValue;
                        } else {
                            fila[7] = '-';
                        }
                    } else if (hijosFila[cont].nodeName == "PORJORPAR") {
                        if (hijosFila[cont].childNodes[0].nodeValue != "null") {
                            fila[8] = hijosFila[cont].childNodes[0].nodeValue;
                            var tex = fila[8].toString();
                            tex = tex.replace(".", ",");
                            fila[8] = tex;
                        } else {
                            fila[8] = '-';
                        }
                    } else if (hijosFila[cont].nodeName == "DESSITUACION") {
                        if (hijosFila[cont].childNodes[0].nodeValue != "null") {
                            fila[9] = hijosFila[cont].childNodes[0].nodeValue;
                        } else {
                            fila[9] = '-';
                        }
                    } else if (hijosFila[cont].nodeName == "REDUCJORN") {
                        if (hijosFila[cont].childNodes[0].nodeValue != "null") {
                            fila[10] = hijosFila[cont].childNodes[0].nodeValue;
                            var tex = fila[10].toString();
                            tex = tex.replace(".", ",");
                            fila[10] = tex;
                        } else {
                            fila[10] = '-';
                        }
                    } else if (hijosFila[cont].nodeName == "FECINISIT") {
                        if (hijosFila[cont].childNodes.length > 0) {
                            fila[11] = hijosFila[cont].childNodes[0].nodeValue;
                        } else {
                            fila[11] = '-';
                        }
                    } else if (hijosFila[cont].nodeName == "FECFINSIT") {
                        if (hijosFila[cont].childNodes.length > 0) {
                            fila[12] = hijosFila[cont].childNodes[0].nodeValue;
                        } else {
                            fila[12] = '-';
                        }
                    } else if (hijosFila[cont].nodeName == "NUMDIASIT") {
                        if (hijosFila[cont].childNodes[0].nodeValue != "null") {
                            fila[13] = hijosFila[cont].childNodes[0].nodeValue;
                        } else {
                            fila[13] = '-';
                        }
                    } else if (hijosFila[cont].nodeName == "BASEREGUL") {
                        if (hijosFila[cont].childNodes[0].nodeValue != "null") {
                            fila[14] = hijosFila[cont].childNodes[0].nodeValue;
                            var tex = fila[14].toString();
                            tex = tex.replace(".", ",");
                            fila[14] = tex;
                        } else {
                            fila[14] = '-';
                        }
                    } else if (hijosFila[cont].nodeName == "IMPPREST") {
                        if (hijosFila[cont].childNodes[0].nodeValue != "null") {
                            fila[15] = hijosFila[cont].childNodes[0].nodeValue;
                            var tex = fila[15].toString();
                            tex = tex.replace(".", ",");
                            fila[15] = tex;
                        } else {
                            fila[15] = '-';
                        }
                    } else if (hijosFila[cont].nodeName == "COMPLSAL") {
                        if (hijosFila[cont].childNodes[0].nodeValue != "null") {
                            fila[16] = hijosFila[cont].childNodes[0].nodeValue;
                            var tex = fila[16].toString();
                            tex = tex.replace(".", ",");
                            fila[16] = tex;
                        } else {
                            fila[16] = '-';
                        }
                    } else if (hijosFila[cont].nodeName == "IMPSUBVSOL") {
                        if (hijosFila[cont].childNodes[0].nodeValue != "null") {
                            fila[17] = hijosFila[cont].childNodes[0].nodeValue;
                            var tex = fila[17].toString();
                            tex = tex.replace(".", ",");
                            fila[17] = tex;
                        } else {
                            fila[17] = '-';
                        }
                    }
                }
                listaNueva[j] = fila;
                fila = new Array();
            }
        }//for(j=0;hijos!=null && j<hijos.length;j++)


        return listaNueva;
    }

    function pulsarEliminarPersona() {
        if (tablaPersonas.selectedIndex != -1) {
            var resultado = jsp_alerta('', '<%=meLanbide80I18n.getMensaje(idiomaUsuario, "msg.preguntaEliminar")%>');
            if (resultado == 1) {

                var ajax = getXMLHttpRequest();
                var nodos = null;
                var CONTEXT_PATH = '<%=request.getContextPath()%>'
                var url = CONTEXT_PATH + "/PeticionModuloIntegracion.do";
                var parametros = "";
                parametros = 'tarea=preparar&modulo=MELANBIDE80&operacion=eliminarPersona&tipo=0&numExp=<%=numExpediente%>&id=' + listaPersonas[tablaPersonas.selectedIndex][0];
                try {
                    ajax.open("POST", url, false);
                    ajax.setRequestHeader("Content-Type", "application/x-www-form-urlencoded; charset=ISO-8859-1");
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
                    var listaPersonasNueva = extraerListaPersonas(nodos);
                    var codigoOperacion = listaPersonasNueva[0];

                    if (codigoOperacion == "0") {
                        recargarTablaPersonas(listaPersonasNueva);
                    } else if (codigoOperacion == "1") {
                        jsp_alerta("A", '<%=meLanbide80I18n.getMensaje(idiomaUsuario,"error.errorBD")%>');
                    } else if (codigoOperacion == "2") {
                        jsp_alerta("A", '<%=meLanbide80I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                    } else if (codigoOperacion == "3") {
                        jsp_alerta("A", '<%=meLanbide80I18n.getMensaje(idiomaUsuario,"error.pasoParametros")%>');
                    } else {
                        jsp_alerta("A", '<%=meLanbide80I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                    }//if(
                } catch (Err) {
                    jsp_alerta("A", '<%=meLanbide80I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                }//try-catch
            }
        } else {
            jsp_alerta('A', '<%=meLanbide80I18n.getMensaje(idiomaUsuario, "msg.msjNoSelecFila")%>');
        }
    }

    function recargarTablaPersonas(result) {
        var fila;
        listaPersonas = new Array();
        listaPersonasTabla = new Array();
        for (var i = 1; i < result.length; i++) {
            fila = result[i];
            //listaPersonas[i - 1] = fila;//NO FUNCIONA IE9
            listaPersonas[i - 1] = [fila[0], fila[1], fila[2], fila[3], fila[4], fila[5], fila[6], fila[7], fila[8], fila[9], fila[10], fila[11], fila[12],
                fila[13], fila[14], fila[15], fila[16], fila[17]];
            listaPersonasTabla[i - 1] = [fila[0], fila[1], fila[2], fila[3], fila[4], fila[5], fila[6], fila[7], fila[8], fila[9], fila[10], fila[11], fila[12],
                fila[13], fila[14], fila[15], fila[16], fila[17]];
        }

        inicializarTabla();
        tablaPersonas.lineas = listaPersonasTabla;
        tablaPersonas.displayTabla();

    }

    function dblClckTablaPersonas(rowID, tableName) {
        pulsarModificarPersona();
    }

    function inicializarTabla() {

        tablaPersonas = new FixedColumnTable(document.getElementById('listaPersonas'), 1350, 1400, 'listaPersonas');

        tablaPersonas.addColumna('50', 'center', "<%=meLanbide80I18n.getMensaje(idiomaUsuario,"persona.id")%>");
        tablaPersonas.addColumna('100', 'center', "<%=meLanbide80I18n.getMensaje(idiomaUsuario,"persona.dni")%>");
        tablaPersonas.addColumna('100', 'center', "<%=meLanbide80I18n.getMensaje(idiomaUsuario,"persona.nombre")%>");
        tablaPersonas.addColumna('100', 'center', "<%=meLanbide80I18n.getMensaje(idiomaUsuario,"persona.apel1")%>");
        tablaPersonas.addColumna('100', 'center', "<%=meLanbide80I18n.getMensaje(idiomaUsuario,"persona.apel2")%>");
        tablaPersonas.addColumna('200', 'center', "<%=meLanbide80I18n.getMensaje(idiomaUsuario,"persona.tipcontA")%>");
        tablaPersonas.addColumna('100', 'center', "<%=meLanbide80I18n.getMensaje(idiomaUsuario,"persona.tipcontB")%>");
        tablaPersonas.addColumna('100', 'center', "<%=meLanbide80I18n.getMensaje(idiomaUsuario,"persona.jornada")%>");
        tablaPersonas.addColumna('100', 'center', "<%=meLanbide80I18n.getMensaje(idiomaUsuario,"persona.porjorpar")%>");
        tablaPersonas.addColumna('200', 'center', "<%=meLanbide80I18n.getMensaje(idiomaUsuario,"persona.situacion")%>");
        tablaPersonas.addColumna('100', 'center', "<%=meLanbide80I18n.getMensaje(idiomaUsuario,"persona.reducjorn")%>");
        tablaPersonas.addColumna('150', 'center', "<%=meLanbide80I18n.getMensaje(idiomaUsuario,"persona.fecinisit")%>");
        tablaPersonas.addColumna('150', 'center', "<%=meLanbide80I18n.getMensaje(idiomaUsuario,"persona.fecfinsit")%>");
        tablaPersonas.addColumna('100', 'center', "<%=meLanbide80I18n.getMensaje(idiomaUsuario,"persona.numdiasit")%>");
        tablaPersonas.addColumna('100', 'center', "<%=meLanbide80I18n.getMensaje(idiomaUsuario,"persona.baseregul")%>");
        tablaPersonas.addColumna('150', 'center', "<%=meLanbide80I18n.getMensaje(idiomaUsuario,"persona.impprest")%>");
        tablaPersonas.addColumna('150', 'center', "<%=meLanbide80I18n.getMensaje(idiomaUsuario,"persona.complsal")%>");
        tablaPersonas.addColumna('100', 'center', "<%=meLanbide80I18n.getMensaje(idiomaUsuario,"persona.impsubvsol")%>");

        tablaPersonas.displayCabecera = true;
        tablaPersonas.height = 360;

        tablaPersonas.altoCabecera = 50;

        tablaPersonas.scrollWidth = 4200;

        tablaPersonas.dblClkFunction = 'dblClckTablaPersonas';

        tablaPersonas.displayTabla();

        tablaPersonas.pack();
    }

    function pulsarCargarXML() {
        var hayFicheroSeleccionado = false;
        if (document.getElementById('fichero_xml').files) {
            if (document.getElementById('fichero_xml').files[0]) {
                hayFicheroSeleccionado = true;
            }
        } else if (document.getElementById('fichero_xml').value != '') {
            hayFicheroSeleccionado = true;
        }
        if (hayFicheroSeleccionado) {
            var extension = document.getElementById('fichero_xml').value.split('.').pop().toLowerCase();
            if (extension != 'xml') {
                var resultado = jsp_alerta('A', '<%=meLanbide80I18n.getMensaje(idiomaUsuario, "msg.extensionIncorrecta")%>');
                return false;
            }
            var resultado = jsp_alerta('', '<%=meLanbide80I18n.getMensaje(idiomaUsuario, "msg.preguntaImportar")%>');
            if (resultado == 1) {
                elementoVisible('on', 'barraProgresoCargaXML');
                var CONTEXT_PATH = '<%=request.getContextPath()%>';
                var url = CONTEXT_PATH + '/PeticionModuloIntegracion.do';
                var parametros = 'tarea=preparar&modulo=MELANBIDE80&operacion=procesarXML&tipo=0&numero=<%=numExpediente%>';
                document.forms[0].action = url + '?' + parametros;
                document.forms[0].enctype = 'multipart/form-data';
                document.forms[0].encoding = 'multipart/form-data';
                document.forms[0].method = 'POST';
                document.forms[0].target = 'uploadFrameCarga';
                document.forms[0].submit();
            }
            return false;
        } else {
            jsp_alerta("A", '<%=meLanbide80I18n.getMensaje(idiomaUsuario,"msg.ficheroNoSeleccionado")%>');
            return false;
        }
    }

    function pulsarCargarXmlSolicitud() {
        var resultado = jsp_alerta('', '<%=meLanbide80I18n.getMensaje(idiomaUsuario, "msg.preguntaImportar")%>');
        if (resultado == 1) {
            elementoVisible('on', 'barraProgresoCargaXML');
            var CONTEXT_PATH = '<%=request.getContextPath()%>';
            var url = CONTEXT_PATH + '/PeticionModuloIntegracion.do';
            var parametros = 'tarea=preparar&modulo=MELANBIDE80&operacion=cargarXMLsolicitud&tipo=0&numero=<%=numExpediente%>';
            document.forms[0].action = url + '?' + parametros;
            document.forms[0].enctype = 'multipart/form-data';
            document.forms[0].encoding = 'multipart/form-data';
            document.forms[0].method = 'POST';
            document.forms[0].target = 'uploadFrameCarga';
            document.forms[0].submit();
        }
        return false;
    }

    function actualizarTablaRegistros() {
        try {
            getListaRegistros();
            limpiarFormulario();
        } catch (err) {
        }
        elementoVisible('off', 'barraProgresoCargaXML');
    }

    function getListaRegistros() {
        var ajax = getXMLHttpRequest();
        var nodos = null;
        var CONTEXT_PATH = '<%=request.getContextPath()%>'
        var url = CONTEXT_PATH + '/PeticionModuloIntegracion.do';
        var parametros = '';
        parametros = 'tarea=preparar&modulo=MELANBIDE80&operacion=getListaRegistros&tipo=0&numExp=<%=numExpediente%>';
        try {
            ajax.open("POST", url, false);
            ajax.setRequestHeader("Content-Type", "application/x-www-form-urlencoded; charset=ISO-8859-1");
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
            var listaPersonasNueva = extraerListaPersonas(nodos);
            var codigoOperacion = listaPersonasNueva[0];

            if (codigoOperacion == "0") {
                recargarTablaPersonas(listaPersonasNueva);
            } else if (codigoOperacion == "1") {
                jsp_alerta("A", '<%=meLanbide80I18n.getMensaje(idiomaUsuario,"error.errorBD")%>');
            } else if (codigoOperacion == "2") {
                jsp_alerta("A", '<%=meLanbide80I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
            } else if (codigoOperacion == "3") {
                jsp_alerta("A", '<%=meLanbide80I18n.getMensaje(idiomaUsuario,"error.listaVacia")%>');
            } else {
                jsp_alerta("A", '<%=meLanbide80I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
            }//if(
        } catch (Err) {
            jsp_alerta("A", '<%=meLanbide80I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
        }//try-catch      
    }

    function pulsarExportarExcel() {
        var CONTEXT_PATH = '<%=request.getContextPath()%>'
        var url = CONTEXT_PATH + "/PeticionModuloIntegracion.do";
        var parametros = '';
        parametros = '?tarea=preparar&modulo=MELANBIDE80&operacion=generarExcelAnexo&tipo=0&numExp=<%=numExpediente%>';
        window.open(url + parametros, "_blank");
    }

    function limpiarFormulario() {
        $fileupload = $('#fichero_xml');
        $fileupload.replaceWith($fileupload.clone(true));
    }

    function elementoVisible(valor, idBarra) {
        if (valor == 'on') {
            document.getElementById(idBarra).style.visibility = 'inherit';
        } else if (valor == 'off') {
            document.getElementById(idBarra).style.visibility = 'hidden';
        }
    }
</script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/extension/melanbide80/melanbide80.css"/>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/validaciones.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/popup.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/melanbide80/FixedColumnsTable.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/melanbide80/JavaScriptUtil.js"></script>
<script type="text/javascript">
    var APP_CONTEXT_PATH = '<%=request.getContextPath()%>';
</script>
<!-- Eventos onKeyPress compatibilidad firefox  -->
<!--<script type="text/javascript" src="http://code.jquery.com/jquery-1.4.4.js"></script>-->
<div class="tab-page" id="tabPage352" style="height:520px; width: 100%;">
    <h2 class="tab" id="pestana352"><%=meLanbide80I18n.getMensaje(idiomaUsuario,"label.titulo.pestana")%></h2> 
    <script type="text/javascript">tp1.addTabPage(document.getElementById("tabPage352"));</script>
    <div id="barraProgresoCargaXML" style="position: absolute; z-index:10; visibility: hidden; top: 30%; left: 30%;">
        <table width="100%" border="0px" cellpadding="0px" cellspacing="0px" border="0px">
            <tr>
                <td align="center" valign="middle">
                    <table class="contenedorHidepage" cellpadding="0px" cellspacing="0px" border="0px">
                        <tr>
                            <td>
                                <table width="349px" height="100%">
                                    <tr>
                                        <td colspan="3" style="height:70%;text-align:center;valign:middle;">
                                            <span id="msgImportando">
                                                <%=meLanbide80I18n.getMensaje(idiomaUsuario, "msg.cargandoDatos")%>
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
    <br/>
    <h2 class="legendAzul" id="pestanaPrinc"><%=meLanbide80I18n.getMensaje(idiomaUsuario,"label.tituloPrincipal")%></h2>
    <div>    
        <br>
        <div id="divGeneral">     
            <div id="listaPersonas"  align="center"></div>
        </div>
        <br/><br>
        <div class="botonera" style="text-align: center;">
            <input type="button" id="btnNuevaPersona" name="btnNuevaPersona" class="botonGeneral"  value="<%=meLanbide80I18n.getMensaje(idiomaUsuario, "btn.nuevo")%>" onclick="pulsarNuevaPersona();">
            <input type="button" id="btnModificarPersona" name="btnModificarPersona" class="botonGeneral" value="<%=meLanbide80I18n.getMensaje(idiomaUsuario, "btn.modificar")%>" onclick="pulsarModificarPersona();">
            <input type="button" id="btnEliminarPersona" name="btnEliminarPersona"   class="botonGeneral" value="<%=meLanbide80I18n.getMensaje(idiomaUsuario, "btn.eliminar")%>" onclick="pulsarEliminarPersona();">
        </div>
        <br>
        <div class="botonera" style="text-align: center;">
            <input type="file"  name="fichero_xml" id="fichero_xml" class="inputTexto" size="60" accept=".xml">
            <input type="button" id="btnCargarRegistros" name="btnCargarRegistros" class="botonMasLargo" value="<%=meLanbide80I18n.getMensaje(idiomaUsuario, "btn.cargarXML")%>" onclick="pulsarCargarXML();">
            <input type="button" id="btnCargarXmlSolicitud" name="btnCargarXmlSolicitud" class="botonMasLargo" value="<%=meLanbide80I18n.getMensaje(idiomaUsuario, "btn.cargarXmlSolicitud")%>" onclick="pulsarCargarXmlSolicitud();">
            <input type="button" id="btnExportar" name="btnExportar"   class="botonMasLargo" value="<%=meLanbide80I18n.getMensaje(idiomaUsuario, "btn.Exportar")%>" onclick="pulsarExportarExcel();"  >
        </div>
    </div>  
</div>
<iframe id="uploadFrameCarga" name="uploadFrameCarga" height="0" width="0" frameborder="0" scrolling="yes"></iframe> 
<script  type="text/javascript">
    //Tabla Personas
    var tablaPersonas;
    var listaPersonas = new Array();
    var listaPersonasTabla = new Array();

    inicializarTabla();

    <%  		
       PersonaVO objectVO = null;
       List<PersonaVO> List = null;
       if(request.getAttribute("listaPersonas")!=null){
           List = (List<PersonaVO>)request.getAttribute("listaPersonas");
       }													
       if (List!= null && List.size() >0){
           for (int indice=0;indice<List.size();indice++)
           {
               objectVO = List.get(indice);
               DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                
               String dni="";
               if(objectVO.getDni()!=null){
                   dni=objectVO.getDni();
               }else{
                   dni="-";
               }
               String nombre="";
               if(objectVO.getNombre()!=null){
                   nombre=objectVO.getNombre();
               }else{
                   nombre="-";
               }
               String apel1="";
               if(objectVO.getApel1()!=null){
                   apel1=objectVO.getApel1();
               }else{
                   apel1="-";
               }
               String apel2="";
               if(objectVO.getApel2()!=null){
                   apel2=objectVO.getApel2();
               }else{
                   apel2="-";
               }
                    
               String destipcontA="";
               if(objectVO.getDesTipcontA()!=null){
                   String descripcion = objectVO.getDesTipcontA();
                        
                   String barraSeparadoraDobleIdiomaDesple = ConfigurationParameter.getParameter(ConstantesMeLanbide80.BARRA_SEPARADORA_IDIOMA_DESPLEGABLES, ConstantesMeLanbide80.FICHERO_PROPIEDADES);
                   String[] descripcionDobleIdioma =  (descripcion!=null?descripcion.split(barraSeparadoraDobleIdiomaDesple):null);
                   if(descripcionDobleIdioma!=null && descripcionDobleIdioma.length>1){
                       if(idiomaUsuario==ConstantesMeLanbide80.CODIGO_IDIOMA_EUSKERA){
                           descripcion=descripcionDobleIdioma[1];
                       }else{
                           // Cogemos la primera posicion que deberia ser castellano
                           descripcion=descripcionDobleIdioma[0];
                       }
                   }
                   destipcontA = descripcion;
               }else{
                   destipcontA="-";
               }
                    
               String tipcontB="";
               if(objectVO.getTipcontB()!=null){
                   tipcontB=String.valueOf((objectVO.getTipcontB().toString()).replace(".",","));
               }else{
                   tipcontB="-";
               }
                    
               String desjornada="";
               if(objectVO.getDesJornada()!=null){
                   String descripcion = objectVO.getDesJornada();
                        
                   String barraSeparadoraDobleIdiomaDesple = ConfigurationParameter.getParameter(ConstantesMeLanbide80.BARRA_SEPARADORA_IDIOMA_DESPLEGABLES, ConstantesMeLanbide80.FICHERO_PROPIEDADES);
                   String[] descripcionDobleIdioma =  (descripcion!=null?descripcion.split(barraSeparadoraDobleIdiomaDesple):null);
                   if(descripcionDobleIdioma!=null && descripcionDobleIdioma.length>1){
                       if(idiomaUsuario==ConstantesMeLanbide80.CODIGO_IDIOMA_EUSKERA){
                           descripcion=descripcionDobleIdioma[1];
                       }else{
                           // Cogemos la primera posicion que deberia ser castellano
                           descripcion=descripcionDobleIdioma[0];
                       }
                   }
                   desjornada = descripcion;
               }else{
                   desjornada="-";
               }
                    
               String porjorpar="";
               if(objectVO.getPorjorpar()!=null){
                   porjorpar=String.valueOf((objectVO.getPorjorpar().toString()).replace(".",","));
               }else{
                   porjorpar="-";
               }
                    
               String dessituacion="";
               if(objectVO.getDesSituacion()!=null){
                   String descripcion = objectVO.getDesSituacion();
                        
                   String barraSeparadoraDobleIdiomaDesple = ConfigurationParameter.getParameter(ConstantesMeLanbide80.BARRA_SEPARADORA_IDIOMA_DESPLEGABLES, ConstantesMeLanbide80.FICHERO_PROPIEDADES);
                   String[] descripcionDobleIdioma =  (descripcion!=null?descripcion.split(barraSeparadoraDobleIdiomaDesple):null);
                   if(descripcionDobleIdioma!=null && descripcionDobleIdioma.length>1){
                       if(idiomaUsuario==ConstantesMeLanbide80.CODIGO_IDIOMA_EUSKERA){
                           descripcion=descripcionDobleIdioma[1];
                       }else{
                           // Cogemos la primera posicion que deberia ser castellano
                           descripcion=descripcionDobleIdioma[0];
                       }
                   }
                   dessituacion = descripcion;
               }else{
                   dessituacion="-";
               }
                    
               String reducjorn="";
               if(objectVO.getReducjorn()!=null){
                   reducjorn=String.valueOf((objectVO.getReducjorn().toString()).replace(".",","));
               }else{
                   reducjorn="-";
               }
                    
               String fecinisit="";
               if(objectVO.getFecinisit()!=null){
                   fecinisit=dateFormat.format(objectVO.getFecinisit());
                
               }else{
                   fecinisit="-";
               }

               String fecfinsit="";
               if(objectVO.getFecfinsit()!=null){
                   fecfinsit=dateFormat.format(objectVO.getFecfinsit());
                
               }else{
                   fecfinsit="-";
               }

               String numdiasit="";
               if(objectVO.getNumdiasit()!=null && !"".equals(objectVO.getNumdiasit())){
                   numdiasit=Integer.toString(objectVO.getNumdiasit());
               }else{
                   numdiasit="-";
               }
                    
               String baseregul="";
               if(objectVO.getBaseregul()!=null){
                   baseregul=String.valueOf((objectVO.getBaseregul().toString()).replace(".",","));
               }else{
                   baseregul="-";
               }
                    
               String impprest="";
               if(objectVO.getImpprest()!=null){
                   impprest=String.valueOf((objectVO.getImpprest().toString()).replace(".",","));
               }else{
                   impprest="-";
               }
                    
               String complsal="";
               if(objectVO.getComplsal()!=null){
                   complsal=String.valueOf((objectVO.getComplsal().toString()).replace(".",","));
               }else{
                   complsal="-";
               }
                    
               String impsubvsol="";
               if(objectVO.getImpsubvsol()!=null){
                   impsubvsol=String.valueOf((objectVO.getImpsubvsol().toString()).replace(".",","));
               }else{
                   impsubvsol="-";
               }

    %>
    listaPersonas[<%=indice%>] = ['<%=objectVO.getId()%>', '<%=dni%>', '<%=nombre%>', '<%=apel1%>', '<%=apel2%>', '<%=destipcontA%>', '<%=tipcontB%>', '<%=desjornada%>', '<%=porjorpar%>',
        '<%=dessituacion%>', '<%=reducjorn%>', '<%=fecinisit%>', '<%=fecfinsit%>', '<%=numdiasit%>', '<%=baseregul%>', '<%=impprest%>', '<%=complsal%>', '<%=impsubvsol%>'];
    listaPersonasTabla[<%=indice%>] = ['<%=objectVO.getId()%>', '<%=dni%>', '<%=nombre%>', '<%=apel1%>', '<%=apel2%>', '<%=destipcontA%>', '<%=tipcontB%>', '<%=desjornada%>', '<%=porjorpar%>',
        '<%=dessituacion%>', '<%=reducjorn%>', '<%=fecinisit%>', '<%=fecfinsit%>', '<%=numdiasit%>', '<%=baseregul%>', '<%=impprest%>', '<%=complsal%>', '<%=impsubvsol%>'];
    <%
           }// for
       }// if
    %>

    tablaPersonas.lineas = listaPersonasTabla;
    tablaPersonas.displayTabla();

    if (navigator.appName.indexOf("Internet Explorer") != -1 || (navigator.appName.indexOf("Netscape") != -1 && navigator.appCodeName.match(/Mozilla/))) {
        try {
            var div = document.getElementById('listaPersonas');
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


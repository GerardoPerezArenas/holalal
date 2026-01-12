<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>


<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.agora.business.util.GeneralValueObject"%>
<%@page import="java.util.List"%>
<%@page import="java.lang.Integer"%>
<%@page import="es.altia.common.service.config.Config"%>
<%@page import="es.altia.common.service.config.ConfigServiceHelper"%>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide03.i18n.MeLanbide03I18n" %>

<%
    int idiomaUsuario = 1;
    int apl=4;    
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
            }
        }
    }
    catch(Exception ex)
    {
        
    }
        //Clase para internacionalizar los mensajes de la aplicación.
    MeLanbide03I18n meLanbide03I18n = MeLanbide03I18n.getInstance();

    String codProcedimiento = request.getParameter("codProcedimiento");
    String codOrganizacion  = request.getParameter("codOrganizacionModulo");
    String nombreModulo     = request.getParameter("nombreModulo");
    String numExpediente    = request.getParameter("numero");
    
    int maxSeleccionables = 50;
%>

<html>
    <head>
        <meta http-equiv="X-UA-Compatible" content="IE=Edge" />
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <jsp:include page="/jsp/administracion/tpls/app-constants.jsp" />
        <title><%=meLanbide03I18n.getMensaje(idiomaUsuario, "tituloPantalla.listadoExpedientesPendientes")%></title>
        <!-- Beans -->
        <jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
        <jsp:setProperty name="descriptor"  property="idi_cod" value= "<%= idiomaUsuario%>" />
        <jsp:setProperty name="descriptor"  property="apl_cod" value="<%= apl %>" />

        <!-- Estilos -->
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/estilo.css"/>
        <link rel="stylesheet" type="text/css" href="<c:url value='/css/extension/melanbide03/melanbide03.css'/>">
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/general.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/tabpane.js"></script>
        <script type="text/javascript"  src="<%=request.getContextPath()%>/scripts/listas.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/TablaNueva.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/validaciones.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/popup.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script>
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/>

        <script type="text/javascript">
            var lista = new Array();
            var lineasExpedienteAPA = new Array();
            var lineas = new Array();
            var datosExpedienteAPA = new Array();
            var datosFicheroAPA = new Array();

            function getXMLHttpRequest() {
                var aVersions = ["MSXML2.XMLHttp.5.0",
                    "MSXML2.XMLHttp.4.0", "MSXML2.XMLHttp.3.0",
                    "MSXML2.XMLHttp", "Microsoft.XMLHttp"
                ];

                if (window.XMLHttpRequest) {
                    // para IE7, Mozilla, Safari, etc: que usen el objeto nativo
                    return new XMLHttpRequest();
                } else {
                    if (window.ActiveXObject) {
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
            }

            function inicializar() {
                window.focus();
                recuperaExpedientesCEPAP();
                recuperaFicherosGenerados();
            }

            function recuperaExpedientesCEPAP() {

            <%
                    // PARA EXPEDIENTES CEPAP
                    List<GeneralValueObject> relacionExpedientesAPA = (List<GeneralValueObject>) request.getAttribute("RelacionExpedientesAPA");
                    int numRelacionExpedientesAPA = 0;
                    int i = 0;
                    if ( relacionExpedientesAPA != null ) numRelacionExpedientesAPA = relacionExpedientesAPA.size();
            %>
                var j = 0;
                var contador = 0;
                var seleccionado = "false";
            <%
                GeneralValueObject expedienteAPA = null;
                for(i=0;i<numRelacionExpedientesAPA;i++)
                {
                    expedienteAPA = (GeneralValueObject)relacionExpedientesAPA.get(i);
            %>
                datosExpedienteAPA[j] = [check(seleccionado, contador),
                    '<%=(String)expedienteAPA.getAtributo("numExpediente")%>', '<%=(String)expedienteAPA.getAtributo("interesados")%>', '<%=(String)expedienteAPA.getAtributo("oficina")%>', '<%=(String)expedienteAPA.getAtributo("fechaEntrada")%>', '<%=(String)expedienteAPA.getAtributo("fecPres")%>'];
                lineasExpedienteAPA[j] = datosExpedienteAPA[j];
                j++;
                contador++;
            <%
                }
            %>

                tabImpresionExpAPA.lineas = lineasExpedienteAPA;
                refresca(tabImpresionExpAPA);

            }
            function listadoExpedientesImpresion(indice) {
                if (indice != null && indice != -1) {
                    var dato = datosFicheroAPA[indice][0];
                    var control = new Date();
                    window.open("<%=request.getContextPath()%>/PeticionModuloIntegracion.do?tarea=preparar&modulo=MELANBIDE03&fichero=" + dato + "&operacion=listadoExpedientes&tipo=0&control=" + control.getTime(), "ventana1", "left=10, top=10, width=700, height=310, scrollbars=no, menubar=no, location=no, resizable=no");

                }

            }

            function imprimeEtiq(indice) {
                if (indice != null && indice != -1) {
                    var dato = datosFicheroAPA[indice][0];
                    var control = new Date();
                    window.open("<%=request.getContextPath()%>/PeticionModuloIntegracion.do?tarea=preparar&modulo=MELANBIDE03&nombreFichero=" + dato + "&operacion=imprimirEtiquetasAPA&tipo=0&control=" + control.getTime(), "ventana1", "left=10, top=10, width=700, height=310, scrollbars=no, menubar=no, location=no, resizable=no");

                }

            }

            function recuperaFicherosGenerados() {
            <%
                    // PARA fichero generados expediente CEPAP
                    List<GeneralValueObject> relacionFicherosExcelAPA = (List<GeneralValueObject>) request.getAttribute("RelacionFicherosImpresionGenerados");
                    int numRelacionFicherosExcelAPA = 0;
                    int z = 0;
                    if ( relacionFicherosExcelAPA != null ) numRelacionFicherosExcelAPA = relacionFicherosExcelAPA.size();
            %>
                var k = 0;
                var contexto = "<%=request.getContextPath()%>";

            <%
                
                GeneralValueObject ficheroGenerado = null;
                for(z=0;z<numRelacionFicherosExcelAPA;z++)
                {
                    ficheroGenerado = (GeneralValueObject)relacionFicherosExcelAPA.get(z);
            %>
                datosFicheroAPA[k] = ['<%=(String)ficheroGenerado.getAtributo("numExpediente")%>',
                    '<%=(String)ficheroGenerado.getAtributo("fechaGeneracion")%>', '<a href=\"#\" title=\"' + '<%=meLanbide03I18n.getMensaje(idiomaUsuario, "label.verExpFicheroImpresion")%>' + '\" onclick=\"listadoExpedientesImpresion(' + k + ');\">' + '<%=meLanbide03I18n.getMensaje(idiomaUsuario, "label.ver")%>' + '</a>',
                    '<a href=\"#\" title=\"' + 'Imprimir etiquetas' + '\" onclick=\"imprimeEtiq(' + k + ');\">' + 'Imprimir' + '</a>'];

                lineas[k] = datosFicheroAPA[k];
                k++;
            <%
                }
            %>
                tabFichExelAPA.lineas = lineas;
                refresca(tabFichExelAPA);
            }

            function pulsarSeleccTodos() {

                var nodoCheck = document.getElementsByTagName("input");

                //if(nodoCheck.length <= <//%=maxSeleccionables%>){
                for (i = 0; i < nodoCheck.length; i++) {
                    if (nodoCheck[i].type == "checkbox" && nodoCheck[i].disabled == false) {
                        nodoCheck[i].checked = true;
                    }
                }
                /*}else{
                 jsp_alerta("A","<%=String.format(meLanbide03I18n.getMensaje(idiomaUsuario, "msg.demasiadosExpSeleccionados"), maxSeleccionables)%>");
                 }*/

                document.getElementById('btnSeleccTodo').style.display = 'none';
                document.getElementById('btnDeseleccTodo').style.display = 'inline';
            }

            function pulsarDeseleccTodos() {

                var nodoCheck = document.getElementsByTagName("input");

                for (i = 0; i < nodoCheck.length; i++) {
                    if (nodoCheck[i].type == "checkbox" && nodoCheck[i].disabled == false) {
                        nodoCheck[i].checked = false;
                    }
                }

                document.getElementById('btnSeleccTodo').style.display = 'inline';
                document.getElementById('btnDeseleccTodo').style.display = 'none';
            }

            function getSeleccionados() {

                var c = 0;
                var j = 0;

                listaSeleccionados = new Array();
                listaNumExpSeleccionados = new Array();
                for (c = 0; lineasExpedienteAPA != null && c < lineasExpedienteAPA.length; c++) {
                    if (document.getElementById("Seleccionado" + c) != null) {
                        if (document.getElementById("Seleccionado" + c).checked) {
                            var vExp = lineasExpedienteAPA[c].toString().substring(lineasExpedienteAPA[c].toString().indexOf(",") + 1);

                            listaNumExpSeleccionados[j] = vExp;
                            listaSeleccionados[j] = "1";
                            j++;

                        }
                    }
                }
            }

            function check(seleccionado, contador) {
                var fila = '';
                if (seleccionado == "true") {
                    fila += '<input type="checkbox" onclick="comprobarSeleccionados();" value="true" checked=' + seleccionado + ' id="Seleccionado' + contador + '" />';
                } else {
                    fila += '<input type="checkbox" onclick="comprobarSeleccionados();" value="true" id="Seleccionado' + contador + '" />';
                }
                fila += '&nbsp;';
                return fila;
            }

            function comprobarSeleccionados() {
                getSeleccionados();
                if (listaSeleccionados.length >= <%=maxSeleccionables%>) {
                    deshabilitarNoSeleccionados();
                } else {
                    habilitarNoSeleccionados();
                }
            }

            function deshabilitarNoSeleccionados() {
                var inputs = document.getElementsByTagName("input");
                for (var i = 0; i < inputs.length; i++) {
                    if (inputs[i].type == "checkbox") {
                        if (inputs[i].checked != true) {
                            inputs[i].disabled = true;
                        }
                    }
                }
            }

            function habilitarNoSeleccionados() {
                var inputs = document.getElementsByTagName("input");
                for (var i = 0; i < inputs.length; i++) {
                    if (inputs[i].type == "checkbox") {
                        if (inputs[i].checked != true) {
                            inputs[i].disabled = false;
                        }
                    }
                }
            }

            function pulsarGenerarFichero() {
                getSeleccionados();
                if (listaSeleccionados.length > 0) {
                    //if(listaSeleccionados.length <= <//%=maxSeleccionables%>){
                    var ajax = getXMLHttpRequest();
                    var nodos = null;
                    var CONTEXT_PATH = '<%=request.getContextPath()%>';
                    var url = CONTEXT_PATH + "/PeticionModuloIntegracion.do";
                    var parametros = "";
                    var control = new Date();
                    var nomDoc = null;
                    parametros = 'tarea=preparar&modulo=MELANBIDE03&operacion=generarPdfApaPendientes&tipo=0&listaExpedientesSeleccionados=';
                    var string = "";
                    for (c = 0; listaSeleccionados != null && c < listaSeleccionados.length; c++) {
                        var numExp = listaNumExpSeleccionados[c];
                        var datos = numExp.split(",");
                        //string += numExp + "-" ;
                        string += datos[0] + "-";
                    }
                    parametros += string;
                    parametros += '&control=' + control.getTime();
                    try {
                        ajax.open("POST", url, false);
                        ajax.setRequestHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
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
                        var listaExpedientes = new Array();
                        var listaDocumentos = new Array();
                        var fila = new Array();
                        var nodoFila;
                        var hijosFila;

                        var nodoExpediente;
                        var hijosExpediente;
                        var contExpedientes = 0;

                        var nodoDocumento;
                        var hijosDocumento;
                        var contDocumentos = 0;
                        for (var j = 0; hijos != null && j < hijos.length; j++) {
                            if (hijos[j].nodeName == "CODIGO_OPERACION") {
                                codigoOperacion = hijos[j].childNodes[0].nodeValue;
                            }//if(hijos[j].nodeName=="CODIGO_OPERACION")   
                            else if (hijos[j].nodeName == "EXPEDIENTES") {
                                nodoFila = hijos[j];
                                hijosFila = nodoFila.childNodes;
                                for (var cont = 0; cont < hijosFila.length; cont++) {
                                    if (hijosFila[cont].nodeName == "EXPEDIENTE") {
                                        nodoExpediente = hijosFila[cont];
                                        hijosExpediente = nodoExpediente.childNodes;
                                        for (var cont2 = 0; cont2 < hijosExpediente.length; cont2++) {
                                            if (hijosExpediente[cont2].nodeName == "NUM_EXPEDIENTE") {
                                                if (hijosExpediente[cont2].childNodes.length > 0) {
                                                    fila[0] = hijosExpediente[cont2].childNodes[0].nodeValue;
                                                } else {
                                                    fila[0] = '-';
                                                }
                                            } else if (hijosExpediente[cont2].nodeName == "INTERESADOS") {
                                                if (hijosExpediente[cont2].childNodes.length > 0) {
                                                    fila[1] = hijosExpediente[cont2].childNodes[0].nodeValue;
                                                } else {
                                                    fila[1] = '-';
                                                }
                                            } else if (hijosExpediente[cont2].nodeName == "NUM_REGISTRO") {
                                                if (hijosExpediente[cont2].childNodes.length > 0) {
                                                    fila[2] = hijosExpediente[cont2].childNodes[0].nodeValue;
                                                } else {
                                                    fila[2] = '-';
                                                }
                                            } else if (hijosExpediente[cont2].nodeName == "FECHA_ENTRADA") {
                                                if (hijosExpediente[cont2].childNodes.length > 0) {
                                                    fila[3] = hijosExpediente[cont2].childNodes[0].nodeValue;
                                                } else {
                                                    fila[3] = '-';
                                                }
                                            } else if (hijosExpediente[cont2].nodeName == "FECHA_SOLICITUD") {
                                                if (hijosExpediente[cont2].childNodes.length > 0) {
                                                    fila[4] = hijosExpediente[cont2].childNodes[0].nodeValue;
                                                } else {
                                                    fila[4] = '-';
                                                }
                                            }
                                        }
                                        listaExpedientes[contExpedientes] = fila;
                                        contExpedientes++;
                                        fila = new Array();
                                    }
                                }
                            } else if (hijos[j].nodeName == "DOCUMENTOS") {
                                nodoFila = hijos[j];
                                hijosFila = nodoFila.childNodes;
                                for (var cont = 0; cont < hijosFila.length; cont++) {
                                    if (hijosFila[cont].nodeName == "DOCUMENTO") {
                                        nodoDocumento = hijosFila[cont];
                                        hijosDocumento = nodoDocumento.childNodes;
                                        for (var cont2 = 0; cont2 < hijosDocumento.length; cont2++) {
                                            if (hijosDocumento[cont2].nodeName == "NOMBRE_FICHERO") {
                                                if (hijosDocumento[cont2].childNodes.length > 0) {
                                                    fila[0] = hijosDocumento[cont2].childNodes[0].nodeValue;
                                                } else {
                                                    fila[0] = '-';
                                                }
                                            } else if (hijosDocumento[cont2].nodeName == "FECHA_GENERACION") {
                                                if (hijosDocumento[cont2].childNodes.length > 0) {
                                                    fila[1] = hijosDocumento[cont2].childNodes[0].nodeValue;
                                                } else {
                                                    fila[1] = '-';
                                                }
                                            }
                                        }
                                        listaDocumentos[contDocumentos] = fila;
                                        contDocumentos++;
                                        fila = new Array();
                                    }
                                }
                            } else if (hijos[j].nodeName == "FICHERO_GENERADO") {
                                nomDoc = hijos[j].childNodes[0].nodeValue;
                            }
                        }//for(j=0;hijos!=null && j<hijos.length;j++)
                        if (codigoOperacion == "0") {
                            var filaAct = new Array();
                            var lineas = new Array();

                            for (var i = 0; i < listaDocumentos.length; i++) {
                                filaAct = listaDocumentos[i];
                                datosFicheroAPA[i] = [filaAct[0], filaAct[1], '<a href=\"#\" title=\"' + '<%=meLanbide03I18n.getMensaje(idiomaUsuario, "label.verExpFicheroImpresion")%>' + '\" onclick=\"listadoExpedientesImpresion(' + i + ');\">' + '<%=meLanbide03I18n.getMensaje(idiomaUsuario, "label.ver")%>' + '</a>'];
                                lineas[i] = datosFicheroAPA[i];
                            }
                            tabFichExelAPA.lineas = lineas;
                            refresca(tabFichExelAPA);
                            lineas = new Array();
                            for (var i = 0; i < listaExpedientes.length; i++) {
                                filaAct = listaExpedientes[i];
                                lineas[i] = [check("false", i), filaAct[0], filaAct[1], filaAct[2], filaAct[3], filaAct[4]];
                            }
                            tabImpresionExpAPA.lineas = lineas;
                            refresca(tabImpresionExpAPA);

                            pulsarDeseleccTodos();

                            if (nomDoc != null && nomDoc != '') {
                                descargarDocumento(nomDoc);
                            }
                        } else if (codigoOperacion == "1") {
                            jsp_alerta("A", '<%=meLanbide03I18n.getMensaje(idiomaUsuario,"error.errorBD")%>');
                        } else if (codigoOperacion == "2") {
                            jsp_alerta("A", '<%=meLanbide03I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                        } else if (codigoOperacion == "3") {
                            jsp_alerta("A", '<%=meLanbide03I18n.getMensaje(idiomaUsuario,"error.pasoParametros")%>');
                        } else {
                            jsp_alerta("A", '<%=meLanbide03I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                        }//if(
                    } catch (Err) {

                    }//try-catch
                    //}else{
                    //  jsp_alerta("A","<//%=String.format(meLanbide03I18n.getMensaje(idiomaUsuario, "msg.demasiadosExpSeleccionados"), maxSeleccionables)%>");
                    //}
                } else {
                    jsp_alerta("A", "<%=meLanbide03I18n.getMensaje(idiomaUsuario, "msg.noExpSeleccionado")%>");
                }
            }

            function pulsarVerDocumento() {
                if (tabFichExelAPA.selectedIndex != -1) {
                    var nomDoc = tabFichExelAPA.lineas[tabFichExelAPA.selectedIndex][0];
                    descargarDocumento(nomDoc);
                } else {
                    jsp_alerta("A", '<%=meLanbide03I18n.getMensaje(idiomaUsuario,"error.noSelecDoc")%>');
                }
            }

            function descargarDocumento(nomDoc) {
                try {
                    var url = '<%=request.getContextPath()%>/PeticionModuloIntegracion.do';
                    var parametros = "";

                    //Procesamos las selecciones
                    parametros = "?tarea=preparar&modulo=MELANBIDE03&operacion=descargarPdfApaPendientes&tipo=0&nomDoc=" + nomDoc;

                    document.forms[0].target = "ocultoPendientesAPA";
                    document.forms[0].action = url + parametros;
                    document.forms[0].submit();
                } catch (err) {
                    alert('<%=meLanbide03I18n.getMensaje(idiomaUsuario, "error.abrirDocumento")%>');
                }
            }
        </script>
    </head>

    <body class="bandaBody" onload="javascript:{
                pleaseWait('off');inicializar();
            }">
        <div id="hidepage" style="top:150px; z-index:10; visibility: inherit;">
            <table width="80%" border="0px" cellpadding="0px" cellspacing="0px" border="0px" >
                <tr>
                    <td valign="middle">
                        <table class="contenedorHidepage" cellpadding="0px" cellspacing="0px" border="0px">
                            <tr>
                                <td>
                                    <table width="349px" height="100%">
                                        <tr>
                                            <td colspan="3" style="height:70%;text-align:center;valign:middle;">
                                                <span><%=meLanbide03I18n.getMensaje(idiomaUsuario, "msg.cargandoDatos")%></span>
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

        <form name="formulario" method="post">
            <input  type="hidden"  name="opcion" id="opcion">
            <input  type="hidden"  name="identificador" id="identificador">
            <div class="txttitblanco"><%= meLanbide03I18n.getMensaje(idiomaUsuario, "tituloPantalla.listadoExpedientesPendientes")%></div>
            <div class="contenidoPantalla">
                <table cellspacing="0px" class="cuadroFondoBlanco" cellpadding="5px">
                    <tr>
                        <td>
                            <table height="10px" cellpadding="0px" cellspacing="0px" border="0px">
                                <tr>
                                    <td></td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <table width="100%" rules="cols"  border="0" cellspacing="0" cellpadding="0" class="fondoCab">
                                <tr>
                                    <td colspan="2">
                                        <div id="tablaImpresionExpCEPAP"></div>
                                    </td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <table width="100%" rules="cols"  border="0" cellspacing="0" cellpadding="0" class="fondoCab">
                                <tr>
                                    <td colspan="2">
                                        <div id="tablaFichExelCEPAP"></div>
                                    </td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                </table>


                <!-------------------------------------- BOTONES. ------------------------------------------>
                <table id="tablaBotones" cellpadding="0px" cellspacing="0px" border="0px" align="right">
                    <tr>
                        <td>
                            <div style="border: 0; text-align: right">
                                <table cellpadding="0px" cellspacing="0px" style="border: 0;">
                                    <tr>
                                        <td>
                                            <table cellpadding="0px" cellspacing="0px" border="0px">
                                                <tr>
                                                    <td style="width:2px"></TD>
                                                    <td>
                                                        <input type= "button" class="botonLargo" value="<%=meLanbide03I18n.getMensaje(idiomaUsuario, "btn.seleccionarTodos")%>" name="btnSeleccTodo" id="btnSeleccTodo" onClick="pulsarSeleccTodos();">
                                                        <input type= "button" class="botonLargo" value="<%=meLanbide03I18n.getMensaje(idiomaUsuario, "btn.deseleccionarTodos")%>" name="btnDeseleccTodo" id="btnDeseleccTodo" onClick="pulsarDeseleccTodos();" style="display: none;">
                                                    </td>
                                                    <td>
                                                        &nbsp;
                                                    </td>    
                                                    <td>
                                                        <input type= "button" class="botonLargo" value="<%=meLanbide03I18n.getMensaje(idiomaUsuario, "btn.generarFichero")%>" name="generarFichero" onClick="pulsarGenerarFichero();">
                                                    </td>
                                                    <td>
                                                        &nbsp;
                                                    </td>    
                                                    <td>
                                                        <input type= "button" class="botonLargo" value="<%=meLanbide03I18n.getMensaje(idiomaUsuario, "btn.verDocumento")%>" name="cmdVerOtroDocumento" id="cmdVerOtroDocumento" onClick="pulsarVerDocumento();
                                                                return false;">
                                                    </td>
                                                </tr>
                                            </table>
                                        </td>
                                    </tr>
                                </table>
                            </div>
                        </td>
                    </tr>
                </table>
            </div>
        </form>

        <script type="text/javascript">
            var tabImpresionExpAPA;
            var tabFichExelAPA;

            //Tabla de expedientes estado pendiente  del procedimiento CEPAP
            /*if(document.all) tabImpresionExpAPA = new Tabla(document.all.tablaImpresionExpCEPAP);
             else tabImpresionExpAPA = new Tabla(document.getElementById('tablaImpresionExpCEPAP'));*/
            tabImpresionExpAPA = new Tabla(true, '<%=descriptor.getDescripcion("buscar")%>', '<%=descriptor.getDescripcion("anterior")%>', '<%=descriptor.getDescripcion("siguiente")%>', '<%=descriptor.getDescripcion("mosFilasPag")%>', '<%=descriptor.getDescripcion("msgNoResultBusq")%>', '<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>', '<%=descriptor.getDescripcion("filtrDeTotal")%>', '<%=descriptor.getDescripcion("primero")%>', '<%=descriptor.getDescripcion("ultimo")%>', document.getElementById('tablaImpresionExpCEPAP'));
            tabImpresionExpAPA.addColumna('35', 'center', "");
            tabImpresionExpAPA.addColumna('150', 'center', '<%=meLanbide03I18n.getMensaje(idiomaUsuario, "lbl.numExpediente")%>');
            tabImpresionExpAPA.addColumna('250', 'center', '<%=meLanbide03I18n.getMensaje(idiomaUsuario, "lbl.solicitante")%>');
            tabImpresionExpAPA.addColumna('285', 'center', '<%=meLanbide03I18n.getMensaje(idiomaUsuario, "lbl.oficina")%>');
            tabImpresionExpAPA.addColumna('100', 'center', '<%=meLanbide03I18n.getMensaje(idiomaUsuario, "lbl.fecPresRegistro")%>');
            tabImpresionExpAPA.addColumna('100', 'center', '<%=meLanbide03I18n.getMensaje(idiomaUsuario, "lbl.fecSolicitud")%>');


            tabImpresionExpAPA.height = 150;
            tabImpresionExpAPA.displayCabecera = true;

            //Tabla MELANBIDE03_IMPRESION_CEPAP, fichero excel con expedientes procedim CEPAP
            /*if(document.all) tabFichExelAPA = new Tabla(document.all.tablaFichExelCEPAP);
             else tabFichExelAPA = new Tabla(document.getElementById('tablaFichExelCEPAP'));*/
            tabFichExelAPA = new Tabla(true, '<%=descriptor.getDescripcion("buscar")%>', '<%=descriptor.getDescripcion("anterior")%>', '<%=descriptor.getDescripcion("siguiente")%>', '<%=descriptor.getDescripcion("mosFilasPag")%>', '<%=descriptor.getDescripcion("msgNoResultBusq")%>', '<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>', '<%=descriptor.getDescripcion("filtrDeTotal")%>', '<%=descriptor.getDescripcion("primero")%>', '<%=descriptor.getDescripcion("ultimo")%>', document.getElementById('tablaFichExelCEPAP'));
            tabFichExelAPA.addColumna('400', 'center', '<%=meLanbide03I18n.getMensaje(idiomaUsuario, "lbl.nomFicheroGenerado")%>');
            tabFichExelAPA.addColumna('200', 'center', '<%=meLanbide03I18n.getMensaje(idiomaUsuario, "lbl.fechaGeneracion")%>');
            tabFichExelAPA.addColumna('200', 'center', '<%=meLanbide03I18n.getMensaje(idiomaUsuario, "lbl.expedientesRelacionados")%>');
            tabFichExelAPA.addColumna('150', 'center', 'Etiquetas');


            tabFichExelAPA.height = 150;
            tabFichExelAPA.displayCabecera = true;

            function refresca(tablaCampoSup) {
                tablaCampoSup.displayTabla();
            }

        </script>

        <iframe name="ocultoPendientesAPA" id="ocultoPendientesAPA" src="about:blank" marginwidth="0" height="0" width="0" 
                frameborder="0" scrolling="yes" onunload="alert('focus');"></iframe>
    </body>
</html>
<%-- 
    Document   : modifD
    Created on : 30-ago-2019, 9:37:48
    Author     : kepa
--%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>

<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide58.i18n.MeLanbide58I18n" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.common.service.config.Config"%>
<%@page import="es.altia.common.service.config.ConfigServiceHelper"%>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide58.vo.PensionistaVO"%>
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
    function pulsarNuevoModiPen() {
        var control = new Date();
        lanzarPopUpModal('<%=request.getContextPath()%>/PeticionModuloIntegracion.do?tarea=preparar&modulo=MELANBIDE58&operacion=cargarNuevoModifD&tipo=0&numExp=<%=numExpediente%>&nuevo=1&control=' + control.getTime(), 650, 1000, 'no', 'no', function (result) {
            if (result != undefined) {
                if (result[0] == '0') {
                    recargarTablaModifP(result);
                }
            }
        });
    }

    function pulsarModificarModiPen() {
        if (tablaModif.selectedIndex != -1) {
            var control = new Date();
            lanzarPopUpModal('<%=request.getContextPath()%>/PeticionModuloIntegracion.do?tarea=preparar&modulo=MELANBIDE58&operacion=cargarModificarModifD&tipo=0&nuevo=0&numExp=<%=numExpediente%>&id=' + listaModif[tablaModif.selectedIndex][0] + '&control=' + control.getTime(), 650, 1000, 'no', 'no', function (result) {
                if (result != undefined) {
                    if (result[0] == '0') {
                        recargarTablaModifP(result);
                    }
                }
            });
        } else {
            jsp_alerta('A', '<%=meLanbide58I18n.getMensaje(idiomaUsuario, "msg.msjNoSelecFila")%>');
        }
    }

    function pulsarEliminarModiPen() {
        if (tablaModif.selectedIndex != -1) {
            var resultado = jsp_alerta('', '<%=meLanbide58I18n.getMensaje(idiomaUsuario, "msg.preguntaEliminarModif")%>');
            if (resultado == 1) {
                var ajax = getXMLHttpRequest();
                var nodos = null;
                var CONTEXT_PATH = '<%=request.getContextPath()%>'
                var url = CONTEXT_PATH + "/PeticionModuloIntegracion.do";
                var parametros = "";
                var control = new Date();
                parametros = 'tarea=preparar&modulo=MELANBIDE58&operacion=eliminarPensionista&tipo=0&numExp=<%=numExpediente%>&id=' + listaModif[tablaModif.selectedIndex][0] + '&control=' + control.getTime()+ '&tabla=3';
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
                                } else if (hijosFila[cont].nodeName == "DNI") {
                                    if (hijosFila[cont].childNodes[0].nodeValue != "null") {
                                        fila[4] = hijosFila[cont].childNodes[0].nodeValue;
                                    } else {
                                        fila[4] = '-';
                                    }
                                } else if (hijosFila[cont].nodeName == "TIPOCON") {
                                    if (hijosFila[cont].childNodes[0].nodeValue != "null") {
                                        fila[5] = hijosFila[cont].childNodes[0].nodeValue;
                                    } else {
                                        fila[5] = '-';
                                    }
                                } else if (hijosFila[cont].nodeName == "TIPOCON_DESC") {
                                    if (hijosFila[cont].childNodes[0].nodeValue != "null") {
                                        fila[6] = hijosFila[cont].childNodes[0].nodeValue;
                                    } else {
                                        fila[6] = '-';
                                    }
                                } else if (hijosFila[cont].nodeName == "TIPOJOR") {
                                    if (hijosFila[cont].childNodes[0].nodeValue != "null") {
                                        fila[7] = hijosFila[cont].childNodes[0].nodeValue;
                                    } else {
                                        fila[7] = '-';
                                    }
                                } else if (hijosFila[cont].nodeName == "TIPOJOR_DESC") {
                                    if (hijosFila[cont].childNodes[0].nodeValue != "null") {
                                        fila[8] = hijosFila[cont].childNodes[0].nodeValue;
                                    } else {
                                        fila[8] = '-';
                                    }
                                } else if (hijosFila[cont].nodeName == "FECINICIO") {
                                    if (hijosFila[cont].childNodes.length > 0) {
                                        fila[9] = hijosFila[cont].childNodes[0].nodeValue;
                                    } else {
                                        fila[9] = '-';
                                    }
                                } else if (hijosFila[cont].nodeName == "FECFIN") {
                                    if (hijosFila[cont].childNodes.length > 0) {
                                        fila[10] = hijosFila[cont].childNodes[0].nodeValue;
                                    } else {
                                        fila[10] = '-';
                                    }
                                } else if (hijosFila[cont].nodeName == "FECINSS") {
                                    if (hijosFila[cont].childNodes.length > 0) {
                                        fila[11] = hijosFila[cont].childNodes[0].nodeValue;
                                    } else {
                                        fila[11] = '-';
                                    }
                                } else if (hijosFila[cont].nodeName == "FECSIN") {
                                    if (hijosFila[cont].childNodes.length > 0) {
                                        fila[12] = hijosFila[cont].childNodes[0].nodeValue;
                                    } else {
                                        fila[12] = '-';
                                    }
                                } else if (hijosFila[cont].nodeName == "FECCON") {
                                    if (hijosFila[cont].childNodes.length > 0) {
                                        fila[13] = hijosFila[cont].childNodes[0].nodeValue;
                                    } else {
                                        fila[13] = '-';
                                    }
                                } else if (hijosFila[cont].nodeName == "GRADO") {
                                    if (hijosFila[cont].childNodes[0].nodeValue != "null" && hijosFila[cont].childNodes[0].nodeValue != "") {
                                        fila[14] = hijosFila[cont].childNodes[0].nodeValue;
                                        var tex = fila[14].toString();
                                        tex = tex.replace(".", ",");
                                        fila[14] = tex;

                                    } else {
                                        fila[14] = '-';
                                    }
                                } else if (hijosFila[cont].nodeName == "PORCJORNADA") {
                                    if (hijosFila[cont].childNodes[0].nodeValue != "null" && hijosFila[cont].childNodes[0].nodeValue != "") {
                                        fila[15] = hijosFila[cont].childNodes[0].nodeValue;
                                        var text = fila[15].toString();
                                        text = text.replace(".", ",");
                                        fila[15] = text;

                                    } else {
                                        fila[15] = '-';
                                    }
                                }
                            }// for elementos de la fila
                            listaNueva[j] = fila;
                            fila = new Array();
                        }
                    }//for(j=0;hijos!=null && j<hijos.length;j++)
                    if (codigoOperacion == "0") {
                        recargarTablaModifP(listaNueva);
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
                    jsp_alerta("A", 'Error: ' + Err.toString());
                }//trt-catch
            }

        } else {
            jsp_alerta('A', '<%=meLanbide58I18n.getMensaje(idiomaUsuario, "msg.msjNoSelecFila")%>');
        }
    }

    function pulsarExportarAnexoModiPen() {
        var apellidos = "";
        var control = new Date();
        if (document.getElementById('apellidosF').value == null || document.getElementById('apellidosF').value == '') {
            apellidos = "";
        } else {
            apellidos = document.getElementById('apellidosF').value.replace(/\'/g, "''");
        }
        var CONTEXT_PATH = '<%=request.getContextPath()%>'
        var url = CONTEXT_PATH + "/PeticionModuloIntegracion.do";
        var parametros = "";
        parametros = '?tarea=preparar&modulo=MELANBIDE58&operacion=generarExcelPensionistas&tipo=0&numExp=<%=numExpediente%>&apellidos=' + escape(apellidos) + '&numLinea=' + document.getElementById('numLineaF').value + '&nif=' + document.getElementById('nifF').value + '&control=' + control.getTime() + '&tabla=3';
        window.open(url + parametros, "_blank");
    }

    function pulsarLimpiarModiPen() {
        document.getElementById("numLineaF").value = "";
        document.getElementById("nifF").value = "";
        document.getElementById("apellidosF").value = "";
        pulsarBuscarModiPen();
    }

    function pulsarBuscarModiPen() {
        mensajeValidacion = "";
        if (validarNumericoVacio(document.getElementById('numLineaF').value)) {
            if (validarTresCaracteresApellido(document.getElementById('apellidosF').value)) {
                var ajax = getXMLHttpRequest();
                var nodos = null;
                var CONTEXT_PATH = '<%=request.getContextPath()%>'
                var url = CONTEXT_PATH + "/PeticionModuloIntegracion.do";
                var parametros = "";
                var control = new Date();
                var apellidos = "";
                var nif = "";
                if (document.getElementById('apellidosF').value == null || document.getElementById('apellidosF').value == '') {
                    apellidos = "";
                } else {
                    apellidos = document.getElementById('apellidosF').value.replace(/\'/g, "''");
                }
                if (document.getElementById('nifF').value == null || document.getElementById('nifF').value == '') {
                    nif = "";
                } else {
                    nif = document.getElementById('nifF').value.toUpperCase();
                }
                parametros = 'tarea=preparar&modulo=MELANBIDE58&operacion=buscarPensionista&tipo=0&numExp=<%=numExpediente%>&apellidos=' + escape(apellidos) + '&numLinea=' + document.getElementById('numLineaF').value + '&nif=' + nif + '&control=' + control.getTime() + '&tabla=3';
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
                                } else if (hijosFila[cont].nodeName == "DNI") {
                                    if (hijosFila[cont].childNodes[0].nodeValue != "null") {
                                        fila[4] = hijosFila[cont].childNodes[0].nodeValue;
                                    } else {
                                        fila[4] = '-';
                                    }
                                } else if (hijosFila[cont].nodeName == "TIPOCON") {
                                    if (hijosFila[cont].childNodes[0].nodeValue != "null") {
                                        fila[5] = hijosFila[cont].childNodes[0].nodeValue;
                                    } else {
                                        fila[5] = '-';
                                    }
                                } else if (hijosFila[cont].nodeName == "TIPOCON_DESC") {
                                    if (hijosFila[cont].childNodes[0].nodeValue != "null") {
                                        fila[6] = hijosFila[cont].childNodes[0].nodeValue;
                                    } else {
                                        fila[6] = '-';
                                    }
                                } else if (hijosFila[cont].nodeName == "TIPOJOR") {
                                    if (hijosFila[cont].childNodes[0].nodeValue != "null") {
                                        fila[7] = hijosFila[cont].childNodes[0].nodeValue;
                                    } else {
                                        fila[7] = '-';
                                    }
                                } else if (hijosFila[cont].nodeName == "TIPOJOR_DESC") {
                                    if (hijosFila[cont].childNodes[0].nodeValue != "null") {
                                        fila[8] = hijosFila[cont].childNodes[0].nodeValue;
                                    } else {
                                        fila[8] = '-';
                                    }
                                } else if (hijosFila[cont].nodeName == "FECINICIO") {
                                    if (hijosFila[cont].childNodes.length > 0) {
                                        fila[9] = hijosFila[cont].childNodes[0].nodeValue;
                                    } else {
                                        fila[9] = '-';
                                    }
                                } else if (hijosFila[cont].nodeName == "FECFIN") {
                                    if (hijosFila[cont].childNodes.length > 0) {
                                        fila[10] = hijosFila[cont].childNodes[0].nodeValue;
                                    } else {
                                        fila[10] = '-';
                                    }
                                } else if (hijosFila[cont].nodeName == "FECINSS") {
                                    if (hijosFila[cont].childNodes.length > 0) {
                                        fila[11] = hijosFila[cont].childNodes[0].nodeValue;
                                    } else {
                                        fila[11] = '-';
                                    }
                                } else if (hijosFila[cont].nodeName == "FECSIN") {
                                    if (hijosFila[cont].childNodes.length > 0) {
                                        fila[12] = hijosFila[cont].childNodes[0].nodeValue;
                                    } else {
                                        fila[12] = '-';
                                    }
                                } else if (hijosFila[cont].nodeName == "FECCON") {
                                    if (hijosFila[cont].childNodes.length > 0) {
                                        fila[13] = hijosFila[cont].childNodes[0].nodeValue;
                                    } else {
                                        fila[13] = '-';
                                    }
                                } else if (hijosFila[cont].nodeName == "GRADO") {
                                    if (hijosFila[cont].childNodes[0].nodeValue != "null" && hijosFila[cont].childNodes[0].nodeValue != "") {
                                        fila[14] = hijosFila[cont].childNodes[0].nodeValue;
                                        var tex = fila[14].toString();
                                        tex = tex.replace(".", ",");
                                        fila[14] = tex;
                                    } else {
                                        fila[14] = '-';
                                    }
                                } else if (hijosFila[cont].nodeName == "PORCJORNADA") {
                                    if (hijosFila[cont].childNodes[0].nodeValue != "null" && hijosFila[cont].childNodes[0].nodeValue != "") {
                                        fila[15] = hijosFila[cont].childNodes[0].nodeValue;
                                        var text = fila[15].toString();
                                        text = text.replace(".", ",");
                                        fila[15] = text;
                                    } else {
                                        fila[15] = '-';
                                    }
                                }
                            }// for elementos de la fila
                            listaNueva[j] = fila;
                            fila = new Array();
                        }
                    }//for(j=0;hijos!=null && j<hijos.length;j++)
                    if (codigoOperacion == "0") {
                        recargarTablaModifP(listaNueva);
                    } else if (codigoOperacion == "1") {
                        jsp_alerta("A", '<%=meLanbide58I18n.getMensaje(idiomaUsuario,"error.errorBD")%>');
                    } else if (codigoOperacion == "2") {
                        jsp_alerta("A", '<%=meLanbide58I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                    } else if (codigoOperacion == "3") {
                        jsp_alerta("A", '<%=meLanbide58I18n.getMensaje(idiomaUsuario,"error.pasoParametros")%>');
                    } else if (codigoOperacion == "4") {
                        jsp_alerta("A", '<%=meLanbide58I18n.getMensaje(idiomaUsuario,"msg.vaciaBD")%>');
                    } else {
                        jsp_alerta("A", '<%=meLanbide58I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                    }//if
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

    function recargarTablaModifP(result) {
        var fila;
        listaModif = new Array();
        listaModifTabla = new Array();
        for (var i = 1; i < result.length; i++) {
            fila = result[i];
            listaModif[i - 1] = [fila[0], fila[1], fila[2], fila[3], fila[4], fila[5], fila[6], fila[7], fila[8], fila[9], fila[10], fila[11], fila[12], fila[13], fila[14], fila[15]];
            listaModifTabla[i - 1] = [fila[0], fila[1], fila[2] + ", " + fila[3], fila[4], fila[6], fila[8], fila[15], fila[9], fila[10], fila[11], fila[12], fila[13], fila[14]];
        }
        crearTablaModifPen();
        tablaModif.lineas = listaModifTabla;
        tablaModif.displayTabla();
    }

    function crearTablaModifPen() {
        tablaModif = new Tabla(true, '<%=descriptor.getDescripcion("buscar")%>', '<%=descriptor.getDescripcion("anterior")%>', '<%=descriptor.getDescripcion("siguiente")%>', '<%=descriptor.getDescripcion("mosFilasPag")%>', '<%=descriptor.getDescripcion("msgNoResultBusq")%>', '<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>', '<%=descriptor.getDescripcion("filtrDeTotal")%>', '<%=descriptor.getDescripcion("primero")%>', '<%=descriptor.getDescripcion("ultimo")%>', document.getElementById('listaModif'), 1070);
        tablaModif.addColumna('0', 'center', "<%=meLanbide58I18n.getMensaje(idiomaUsuario,"modif.tablaPensionistas.col0")%>");
        tablaModif.addColumna('0', 'center', "<%=meLanbide58I18n.getMensaje(idiomaUsuario,"modif.tablaPensionistas.col1")%>");
        tablaModif.addColumna('200', 'center', "<%=meLanbide58I18n.getMensaje(idiomaUsuario,"modif.tablaPensionistas.col2")%>");
        tablaModif.addColumna('75', 'center', "<%=meLanbide58I18n.getMensaje(idiomaUsuario,"modif.tablaPensionistas.col3")%>");
        tablaModif.addColumna('70', 'center', "<%=meLanbide58I18n.getMensaje(idiomaUsuario,"modif.tablaPensionistas.col4")%>");
        tablaModif.addColumna('70', 'center', "<%=meLanbide58I18n.getMensaje(idiomaUsuario,"modif.tablaPensionistas.col5")%>");
        tablaModif.addColumna('85', 'center', "<%=meLanbide58I18n.getMensaje(idiomaUsuario,"altas.tablaPensionistas.col12")%>");
        tablaModif.addColumna('120', 'center', "<%=meLanbide58I18n.getMensaje(idiomaUsuario,"modif.tablaPensionistas.col6")%>");
        tablaModif.addColumna('120', 'center', "<%=meLanbide58I18n.getMensaje(idiomaUsuario,"modif.tablaPensionistas.col7")%>");
        tablaModif.addColumna('120', 'center', "<%=meLanbide58I18n.getMensaje(idiomaUsuario,"modif.tablaPensionistas.col8")%>");
        tablaModif.addColumna('120', 'center', "<%=meLanbide58I18n.getMensaje(idiomaUsuario,"modif.tablaPensionistas.col9")%>");
        tablaModif.addColumna('120', 'center', "<%=meLanbide58I18n.getMensaje(idiomaUsuario,"modif.tablaPensionistas.col10")%>");
        tablaModif.addColumna('90', 'center', "<%=meLanbide58I18n.getMensaje(idiomaUsuario,"modif.tablaPensionistas.col11")%>");

        tablaModif.displayCabecera = true;
        tablaModif.height = 300;
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
<div class="tab-page" id="tabPage359" style="height:520px; width: 100%;">
    <h2 class="legendAzul" id="pestanaPrinc"><%=meLanbide58I18n.getMensaje(idiomaUsuario,"label.titulo.pensionistasModif")%></h2>
    <div>
        <br/>
        <div style="height:8px; width: 900px;">
            <div class="lineaFormulario">
                <div style="width: 120px; float: left;" class="etiqueta">
                    <%=meLanbide58I18n.getMensaje(idiomaUsuario,"label.numLinea")%>
                </div>
                <div style="width: 120px; float: left;">
                    <div style="float: left;">
                        <input id="numLineaF" name="numLineaF" type="text" class="inputTexto" size="15" maxlength="5" 
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
                        <input id="nifF" name="nifF" type="text" class="inputTexto" size="15" maxlength="15" 
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
                        <input id="apellidosF" name="apellidosF" type="text" class="inputTexto" size="30" maxlength="50" 
                               value=""/>
                    </div>
                </div>
            </div>  
            <div class="botonera" style="text-align: right;">
                <input type="button" id="btnLimpiarF" name="btnLimpiarF" class="botonGeneral"  value="<%=meLanbide58I18n.getMensaje(idiomaUsuario, "btn.limpiar")%>" onclick="pulsarLimpiarModiPen();">
                <input type="button" id="btnBuscarF" name="btnBuscarF" class="botonGeneral"  value="<%=meLanbide58I18n.getMensaje(idiomaUsuario, "btn.buscar")%>" onclick="pulsarBuscarModiPen();">
            </div>
        </div>
        <br/>
        <div id="divGeneral" >
            <div id="listaModif"  align="center"></div>
        </div>
        <br/><br>
        <div class="botonera" style="text-align: center;">
            <input type="button" id="btnNuevoBajaF" name="btnNuevoBajaF" class="botonGeneral"  value="<%=meLanbide58I18n.getMensaje(idiomaUsuario, "btn.nuevo")%>" onclick="pulsarNuevoModiPen();">
            <input type="button" id="btnModificarAltaF" name="btnModificarAltaF" class="botonGeneral" value="<%=meLanbide58I18n.getMensaje(idiomaUsuario, "btn.modificar")%>" onclick="pulsarModificarModiPen();">
            <input type="button" id="btnEliminarAltaF" name="btnEliminarAltaF"   class="botonGeneral" value="<%=meLanbide58I18n.getMensaje(idiomaUsuario, "btn.eliminar")%>" onclick="pulsarEliminarModiPen();">
             <input type="button" id="btnExportarF" name="btnExportarF"   class="botonMasLargo" value="<%=meLanbide58I18n.getMensaje(idiomaUsuario, "btn.Exportar")%>" onclick="pulsarExportarAnexoModiPen();"> 
        </div>
    </div>
</div>

<script  type="text/javascript">
    var tablaModif;
    var listaModif = new Array();
    var listaModifTabla = new Array();
    crearTablaModifPen();
    <%
        PensionistaVO objectVO = null;
        List<PensionistaVO> List = null;
        if(request.getAttribute("listaModifD")!=null){
            List = (List<PensionistaVO>)request.getAttribute("listaModifD");
        }
        if (List!= null && List.size() >0){
            for (int indice=0;indice<List.size();indice++){
                objectVO = List.get(indice);
                DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
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
                String tipoA="";
                if(objectVO.getTipoContrato()!=null){
                    tipoA=objectVO.getTipoContrato();
                }
                String tipoADesc="";
                if(objectVO.getTipoContratoDesc()!=null){
                    tipoADesc=objectVO.getTipoContratoDesc();
                }
                String tipoB="";
                if(objectVO.getTipoContDurac()!=null){
                    tipoB=objectVO.getTipoContDurac();
                }
                String tipoBDesc="";
                if(objectVO.getTipoContDuracDesc()!=null){
                    tipoBDesc=objectVO.getTipoContDuracDesc();
                }
                String porcJornada="";
                if (objectVO.getPorcJornada()!=null) {
                    porcJornada=String.valueOf((objectVO.getPorcJornada().toString()).replace(".",","));
                }else{
                    porcJornada = "-";
                }                
                String fecIniFormat="";
                if(objectVO.getFechaIni()!=null){
                    fecIniFormat=dateFormat.format(objectVO.getFechaIni());                
                }else{
                    fecIniFormat="-";
                }
                dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                String fecFinFormat="";
                if(objectVO.getFechaFin()!=null){
                    fecFinFormat=dateFormat.format(objectVO.getFechaFin());                
                }else{
                    fecFinFormat="-";
                }
                String fecEfecInss="";
                if(objectVO.getFechaEfecInss()!=null){
                    fecEfecInss=dateFormat.format(objectVO.getFechaEfecInss());                
                }else{
                    fecEfecInss="-";
                }
                String fechaEfecSinE="";
                if(objectVO.getFechaEfecSinE()!=null){
                    fechaEfecSinE=dateFormat.format(objectVO.getFechaEfecSinE());                
                }else{
                    fechaEfecSinE="-";
                }
                String fechaEfecConE="";
                if(objectVO.getFechaEfecConE()!=null){
                    fechaEfecConE=dateFormat.format(objectVO.getFechaEfecConE());                
                }else{
                    fechaEfecConE="-";
                }
                String grado="";
                if(objectVO.getGradoDisc()!=null){
                    grado=String.valueOf((objectVO.getGradoDisc().toString()).replace(".",","));
                }else{
                    grado="-";
                }
    %>
    listaModif[<%=indice%>] = ['<%=objectVO.getId()%>', '<%=numLinea%>', "<%=apellidos%>" + ", " + "<%=nombre%>", '<%=nif%>', '<%=tipoA%>', '<%=tipoADesc%>', '<%=tipoB%>', '<%=tipoBDesc%>', '<%=fecIniFormat%>', '<%=fecFinFormat%>', '<%=fecEfecInss%>', '<%=fechaEfecSinE%>', '<%=fechaEfecConE%>', '<%=grado%>', '<%=porcJornada%>'];
    listaModifTabla[<%=indice%>] = ['<%=objectVO.getId()%>', '<%=numLinea%>', "<%=apellidos%>" + ", " + "<%=nombre%>", '<%=nif%>', '<%=tipoADesc%>', '<%=tipoBDesc%>', '<%=porcJornada%>', '<%=fecIniFormat%>', '<%=fecFinFormat%>', '<%=fecEfecInss%>', '<%=fechaEfecSinE%>', '<%=fechaEfecConE%>', '<%=grado%>'];
    <%
            }//for
        }//if        
    %>
    tablaModif.lineas = listaModifTabla;
    tablaModif.displayTabla();

</script> 

<div id="popupcalendar" class="text"></div>  
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>

<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide72.i18n.MeLanbide72I18n" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.common.service.config.Config"%>
<%@page import="es.altia.common.service.config.ConfigServiceHelper"%>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide72.vo.MedidaA1BCVO"%>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide72.vo.MedidaA2VO"%>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide72.util.ConfigurationParameter"%>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide72.util.ConstantesMeLanbide72"%>
<%@page import="java.util.ArrayList" %>
<%@page import="java.util.List" %>
<%@page import="java.text.DateFormat" %>
<%@page import="java.text.SimpleDateFormat" %>
<%@page import="org.apache.commons.lang.StringEscapeUtils"%>
<html>
    <head>
        <meta http-equiv="X-UA-Compatible" content="IE=Edge" />
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <%
            int idiomaUsuario = 1;
            if(request.getParameter("idioma") != null){
                try{
                    idiomaUsuario = Integer.parseInt(request.getParameter("idioma"));
                }
                catch(Exception ex){
        
                }
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
            MeLanbide72I18n meLanbide72I18n = MeLanbide72I18n.getInstance();
            String numExpediente = (String)request.getAttribute("numExp");

        %>


        <%!
            // Funcion para escapar strings para javascript
            private String escape(String str) 
            {
                return StringEscapeUtils.escapeJavaScript(str);
            }
        %>
        <jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
        <jsp:setProperty name="descriptor"  property="idi_cod" value="<%=idioma%>" />
        <jsp:setProperty name="descriptor"  property="apl_cod" value="<%=apl%>" />
        <link rel="StyleSheet" media="screen" type="text/css" href="<%=request.getContextPath()%><%=css%>"/>
        <script type="text/javascript">

            function pulsarNuevaMedidaA1() {
                var control = new Date();
                lanzarPopUpModal('<%=request.getContextPath()%>/PeticionModuloIntegracion.do?tarea=preparar&modulo=MELANBIDE72&operacion=cargarNuevaMedidaA1&tipo=0&numExp=<%=numExpediente%>&nuevo=1&control=' + control.getTime(), 680, 800, 'no', 'no', function (result) {
                    if (result != undefined) {
                        if (result[0] == '0') {
                            recargarTablaMedidasA1(result);
                        }
                    }
                });
            }

            function pulsarModificarMedidaA1() {
                if (tablaMedidasA1.selectedIndex != -1) {
                    var control = new Date();
                    lanzarPopUpModal('<%=request.getContextPath()%>/PeticionModuloIntegracion.do?tarea=preparar&modulo=MELANBIDE72&operacion=cargarModificarMedidaA1&tipo=0&nuevo=0&numExp=<%=numExpediente%>&id=' + listaMedidasA1[tablaMedidasA1.selectedIndex][0] + '&control=' + control.getTime(), 680, 800, 'no', 'no', function (result) {
                        if (result != undefined) {
                            if (result[0] == '0') {
                                recargarTablaMedidasA1(result);
                            }
                        }
                    });

                } else {
                    jsp_alerta('A', '<%=meLanbide72I18n.getMensaje(idiomaUsuario, "msg.msjNoSelecFila")%>');
                }
            }

            function pulsarEliminarMedidaA1() {
                if (tablaMedidasA1.selectedIndex != -1) {
                    var resultado = jsp_alerta('', '<%=meLanbide72I18n.getMensaje(idiomaUsuario, "msg.preguntaEliminar")%>');
                    if (resultado == 1) {

                        var ajax = getXMLHttpRequest();
                        var nodos = null;
                        var CONTEXT_PATH = '<%=request.getContextPath()%>';
                        var url = CONTEXT_PATH + "/PeticionModuloIntegracion.do";
                        var parametros = "";
                        var control = new Date();
                        parametros = 'tarea=preparar&modulo=MELANBIDE72&operacion=eliminarMedidaA1&tipo=0&numExp=<%=numExpediente%>&id=' + listaMedidasA1[tablaMedidasA1.selectedIndex][0] + '&control=' + control.getTime();
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
                                        } else if (hijosFila[cont].nodeName == "NOMBRE") {
                                            if (hijosFila[cont].childNodes.length > 0) {
                                                fila[1] = hijosFila[cont].childNodes[0].nodeValue;
                                            } else {
                                                fila[1] = '-';
                                            }
                                        } else if (hijosFila[cont].nodeName == "NIF_CIF") {
                                            if (hijosFila[cont].childNodes[0].nodeValue != "null") {
                                                fila[2] = hijosFila[cont].childNodes[0].nodeValue;
                                            } else {
                                                fila[2] = '-';
                                            }
                                        } else if (hijosFila[cont].nodeName == "IMPORTE_ANUAL") {
                                            if (hijosFila[cont].childNodes[0].nodeValue != "null") {
                                                fila[3] = hijosFila[cont].childNodes[0].nodeValue;
                                                var tex = fila[3].toString();
                                                tex = tex.replace(".", ",");
                                                fila[3] = tex;
                                            } else {
                                                fila[3] = '-';
                                            }
                                        } else if (hijosFila[cont].nodeName == "OBJETO_CONTRATO") {
                                            if (hijosFila[cont].childNodes[0].nodeValue != "null") {
                                                fila[4] = hijosFila[cont].childNodes[0].nodeValue;
                                            } else {
                                                fila[4] = '-';
                                            }
                                        }
                                    }
                                    listaNueva[j] = fila;
                                    fila = new Array();
                                }
                            }//for(j=0;hijos!=null && j<hijos.length;j++)
                            if (codigoOperacion == "0") {
                                recargarTablaMedidasA1(listaNueva);
                            } else if (codigoOperacion == "1") {
                                jsp_alerta("A", '<%=meLanbide72I18n.getMensaje(idiomaUsuario,"error.errorBD")%>');
                            } else if (codigoOperacion == "2") {
                                jsp_alerta("A", '<%=meLanbide72I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                            } else if (codigoOperacion == "3") {
                                jsp_alerta("A", '<%=meLanbide72I18n.getMensaje(idiomaUsuario,"error.pasoParametros")%>');
                            } else {
                                jsp_alerta("A", '<%=meLanbide72I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                            }//if(
                        } catch (Err) {
                            jsp_alerta("A", '<%=meLanbide72I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                        }//try-catch
                    }
                } else {
                    jsp_alerta('A', '<%=meLanbide72I18n.getMensaje(idiomaUsuario, "msg.msjNoSelecFila")%>');
                }
            }

            function pulsarNuevaMedidaA2() {
                var control = new Date();
                lanzarPopUpModal('<%=request.getContextPath()%>/PeticionModuloIntegracion.do?tarea=preparar&modulo=MELANBIDE72&operacion=cargarNuevaMedidaA2&tipo=0&numExp=<%=numExpediente%>&nuevo=1&control=' + control.getTime(), 700, 1000, 'no', 'no', function (result) {
                    if (result != undefined) {
                        if (result[0] == '0') {
                            recargarTablaMedidasA2(result);
                        }
                    }
                });
            }

            function pulsarModificarMedidaA2() {
                if (tablaMedidasA2.selectedIndex != -1) {
                    var control = new Date();
                    lanzarPopUpModal('<%=request.getContextPath()%>/PeticionModuloIntegracion.do?tarea=preparar&modulo=MELANBIDE72&operacion=cargarModificarMedidaA2&tipo=0&nuevo=0&numExp=<%=numExpediente%>&id=' + listaMedidasA2[tablaMedidasA2.selectedIndex][0] + '&control=' + control.getTime(), 700, 1000, 'no', 'no', function (result) {
                        if (result != undefined) {
                            if (result[0] == '0') {
                                recargarTablaMedidasA2(result);
                            }
                        }
                    });

                } else {
                    jsp_alerta('A', '<%=meLanbide72I18n.getMensaje(idiomaUsuario, "msg.msjNoSelecFila")%>');
                }
            }

            function pulsarEliminarMedidaA2() {
                if (tablaMedidasA2.selectedIndex != -1) {
                    var resultado = jsp_alerta('', '<%=meLanbide72I18n.getMensaje(idiomaUsuario, "msg.preguntaEliminar")%>');
                    if (resultado == 1) {

                        var ajax = getXMLHttpRequest();
                        var nodos = null;
                        var CONTEXT_PATH = '<%=request.getContextPath()%>';
                        var url = CONTEXT_PATH + "/PeticionModuloIntegracion.do";
                        var parametros = "";
                        var control = new Date();
                        parametros = 'tarea=preparar&modulo=MELANBIDE72&operacion=eliminarMedidaA2&tipo=0&numExp=<%=numExpediente%>&id=' + listaMedidasA2[tablaMedidasA2.selectedIndex][0] + '&control=' + control.getTime();
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
                                        } else if (hijosFila[cont].nodeName == "NOMBRE") {
                                            if (hijosFila[cont].childNodes[0].nodeValue != "null") {
                                                fila[1] = hijosFila[cont].childNodes[0].nodeValue;
                                            } else {
                                                fila[1] = '-';
                                            }
                                        } else if (hijosFila[cont].nodeName == "APELLIDO1") {
                                            if (hijosFila[cont].childNodes[0].nodeValue != "null") {
                                                fila[2] = hijosFila[cont].childNodes[0].nodeValue;
                                            } else {
                                                fila[2] = '-';
                                            }
                                        } else if (hijosFila[cont].nodeName == "APELLIDO2") {
                                            if (hijosFila[cont].childNodes[0].nodeValue != "null") {
                                                fila[3] = hijosFila[cont].childNodes[0].nodeValue;
                                            } else {
                                                fila[3] = '-';
                                            }
                                        } else if (hijosFila[cont].nodeName == "TIPO_DOCUMENTO") {
                                            if (hijosFila[cont].childNodes[0].nodeValue != "null") {
                                                fila[4] = hijosFila[cont].childNodes[0].nodeValue;
                                            } else {
                                                fila[4] = '-';
                                            }
                                        } else if (hijosFila[cont].nodeName == "DNI_NIE") {
                                            if (hijosFila[cont].childNodes[0].nodeValue != "null") {
                                                fila[5] = hijosFila[cont].childNodes[0].nodeValue;
                                            } else {
                                                fila[5] = '-';
                                            }
                                        } else if (hijosFila[cont].nodeName == "TFNO") {
                                            if (hijosFila[cont].childNodes[0].nodeValue != "null") {
                                                fila[6] = hijosFila[cont].childNodes[0].nodeValue;
                                            } else {
                                                fila[6] = '-';
                                            }
                                        } else if (hijosFila[cont].nodeName == "EMAIL") {
                                            if (hijosFila[cont].childNodes[0].nodeValue != "null") {
                                                fila[7] = hijosFila[cont].childNodes[0].nodeValue;
                                            } else {
                                                fila[7] = '-';
                                            }
                                        } else if (hijosFila[cont].nodeName == "PROVINCIA") {
                                            if (hijosFila[cont].childNodes[0].nodeValue != "null") {
                                                fila[8] = hijosFila[cont].childNodes[0].nodeValue;
                                            } else {
                                                fila[8] = '-';
                                            }
                                        } else if (hijosFila[cont].nodeName == "MUNICIPIO") {
                                            if (hijosFila[cont].childNodes[0].nodeValue != "null") {
                                                fila[9] = hijosFila[cont].childNodes[0].nodeValue;
                                            } else {
                                                fila[9] = '-';
                                            }
                                        } else if (hijosFila[cont].nodeName == "LOCALIDAD") {
                                            if (hijosFila[cont].childNodes[0].nodeValue != "null") {
                                                fila[10] = hijosFila[cont].childNodes[0].nodeValue;
                                            } else {
                                                fila[10] = '-';
                                            }
                                        } else if (hijosFila[cont].nodeName == "DIRECCION") {
                                            if (hijosFila[cont].childNodes[0].nodeValue != "null") {
                                                fila[11] = hijosFila[cont].childNodes[0].nodeValue;
                                            } else {
                                                fila[11] = '-';
                                            }
                                        } else if (hijosFila[cont].nodeName == "PORTAL") {
                                            if (hijosFila[cont].childNodes[0].nodeValue != "null") {
                                                fila[12] = hijosFila[cont].childNodes[0].nodeValue;
                                            } else {
                                                fila[12] = '-';
                                            }
                                        } else if (hijosFila[cont].nodeName == "PISO") {
                                            if (hijosFila[cont].childNodes[0].nodeValue != "null") {
                                                fila[13] = hijosFila[cont].childNodes[0].nodeValue;
                                            } else {
                                                fila[13] = '-';
                                            }
                                        } else if (hijosFila[cont].nodeName == "LETRA") {
                                            if (hijosFila[cont].childNodes[0].nodeValue != "null") {
                                                fila[14] = hijosFila[cont].childNodes[0].nodeValue;
                                            } else {
                                                fila[14] = '-';
                                            }
                                        } else if (hijosFila[cont].nodeName == "C_POSTAL") {
                                            if (hijosFila[cont].childNodes[0].nodeValue != "null") {
                                                fila[15] = hijosFila[cont].childNodes[0].nodeValue;
                                            } else {
                                                fila[15] = '-';
                                            }
                                        } else if (hijosFila[cont].nodeName == "IMPORTE_ANUAL") {
                                            if (hijosFila[cont].childNodes[0].nodeValue != "null") {
                                                fila[16] = hijosFila[cont].childNodes[0].nodeValue;
                                                var tex = fila[16].toString();
                                                tex = tex.replace(".", ",");
                                                fila[16] = tex;
                                            } else {
                                                fila[16] = '-';
                                            }
                                        } else if (hijosFila[cont].nodeName == "OBJETO_CONTRATO") {
                                            if (hijosFila[cont].childNodes[0].nodeValue != "null") {
                                                fila[17] = hijosFila[cont].childNodes[0].nodeValue;
                                            } else {
                                                fila[17] = '-';
                                            }
                                        }
                                    }
                                    listaNueva[j] = fila;
                                    fila = new Array();
                                }
                            }//for(j=0;hijos!=null && j<hijos.length;j++)
                            if (codigoOperacion == "0") {
                                recargarTablaMedidasA2(listaNueva);
                            } else if (codigoOperacion == "1") {
                                jsp_alerta("A", '<%=meLanbide72I18n.getMensaje(idiomaUsuario,"error.errorBD")%>');
                            } else if (codigoOperacion == "2") {
                                jsp_alerta("A", '<%=meLanbide72I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                            } else if (codigoOperacion == "3") {
                                jsp_alerta("A", '<%=meLanbide72I18n.getMensaje(idiomaUsuario,"error.pasoParametros")%>');
                            } else {
                                jsp_alerta("A", '<%=meLanbide72I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                            }//if(
                        } catch (Err) {
                            jsp_alerta("A", '<%=meLanbide72I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                        }//try-catch
                    }
                } else {
                    jsp_alerta('A', '<%=meLanbide72I18n.getMensaje(idiomaUsuario, "msg.msjNoSelecFila")%>');
                }
            }

            function pulsarNuevaMedidaB() {
                var control = new Date();
                lanzarPopUpModal('<%=request.getContextPath()%>/PeticionModuloIntegracion.do?tarea=preparar&modulo=MELANBIDE72&operacion=cargarNuevaMedidaB&tipo=0&numExp=<%=numExpediente%>&nuevo=1&control=' + control.getTime(), 680, 800, 'no', 'no', function (result) {
                    if (result != undefined) {
                        if (result[0] == '0') {
                            recargarTablaMedidasB(result);
                        }
                    }
                });
            }

            function pulsarModificarMedidaB() {
                if (tablaMedidasB.selectedIndex != -1) {
                    var control = new Date();
                    lanzarPopUpModal('<%=request.getContextPath()%>/PeticionModuloIntegracion.do?tarea=preparar&modulo=MELANBIDE72&operacion=cargarModificarMedidaB&tipo=0&nuevo=0&numExp=<%=numExpediente%>&id=' + listaMedidasB[tablaMedidasB.selectedIndex][0] + '&control=' + control.getTime(), 680, 800, 'no', 'no', function (result) {
                        if (result != undefined) {
                            if (result[0] == '0') {
                                recargarTablaMedidasB(result);
                            }
                        }
                    });

                } else {
                    jsp_alerta('A', '<%=meLanbide72I18n.getMensaje(idiomaUsuario, "msg.msjNoSelecFila")%>');
                }
            }

            function pulsarEliminarMedidaB() {
                if (tablaMedidasB.selectedIndex != -1) {
                    var resultado = jsp_alerta('', '<%=meLanbide72I18n.getMensaje(idiomaUsuario, "msg.preguntaEliminar")%>');
                    if (resultado == 1) {

                        var ajax = getXMLHttpRequest();
                        var nodos = null;
                        var CONTEXT_PATH = '<%=request.getContextPath()%>';
                        var url = CONTEXT_PATH + "/PeticionModuloIntegracion.do";
                        var parametros = "";
                        var control = new Date();
                        parametros = 'tarea=preparar&modulo=MELANBIDE72&operacion=eliminarMedidaB&tipo=0&numExp=<%=numExpediente%>&id=' + listaMedidasB[tablaMedidasB.selectedIndex][0] + '&control=' + control.getTime();
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
                                        } else if (hijosFila[cont].nodeName == "NOMBRE") {
                                            if (hijosFila[cont].childNodes.length > 0) {
                                                fila[1] = hijosFila[cont].childNodes[0].nodeValue;
                                            } else {
                                                fila[1] = '-';
                                            }
                                        } else if (hijosFila[cont].nodeName == "NIF_CIF") {
                                            if (hijosFila[cont].childNodes[0].nodeValue != "null") {
                                                fila[2] = hijosFila[cont].childNodes[0].nodeValue;
                                            } else {
                                                fila[2] = '-';
                                            }
                                        } else if (hijosFila[cont].nodeName == "IMPORTE_ANUAL") {
                                            if (hijosFila[cont].childNodes[0].nodeValue != "null") {
                                                fila[3] = hijosFila[cont].childNodes[0].nodeValue;
                                                var tex = fila[3].toString();
                                                tex = tex.replace(".", ",");
                                                fila[3] = tex;
                                            } else {
                                                fila[3] = '-';
                                            }
                                        }
                                    }
                                    listaNueva[j] = fila;
                                    fila = new Array();
                                }
                            }//for(j=0;hijos!=null && j<hijos.length;j++)
                            if (codigoOperacion == "0") {
                                recargarTablaMedidasB(listaNueva);
                            } else if (codigoOperacion == "1") {
                                jsp_alerta("A", '<%=meLanbide72I18n.getMensaje(idiomaUsuario,"error.errorBD")%>');
                            } else if (codigoOperacion == "2") {
                                jsp_alerta("A", '<%=meLanbide72I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                            } else if (codigoOperacion == "3") {
                                jsp_alerta("A", '<%=meLanbide72I18n.getMensaje(idiomaUsuario,"error.pasoParametros")%>');
                            } else {
                                jsp_alerta("A", '<%=meLanbide72I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                            }//if(
                        } catch (Err) {
                            jsp_alerta("A", '<%=meLanbide72I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                        }//try-catch
                    }
                } else {
                    jsp_alerta('A', '<%=meLanbide72I18n.getMensaje(idiomaUsuario, "msg.msjNoSelecFila")%>');
                }
            }

            function pulsarNuevaMedidaC() {
                var control = new Date();
                lanzarPopUpModal('<%=request.getContextPath()%>/PeticionModuloIntegracion.do?tarea=preparar&modulo=MELANBIDE72&operacion=cargarNuevaMedidaC&tipo=0&numExp=<%=numExpediente%>&nuevo=1&control=' + control.getTime(), 680, 800, 'no', 'no', function (result) {
                    if (result != undefined) {
                        if (result[0] == '0') {
                            recargarTablaMedidasC(result);
                        }
                    }
                });
            }

            function pulsarModificarMedidaC() {
                if (tablaMedidasC.selectedIndex != -1) {
                    var control = new Date();
                    lanzarPopUpModal('<%=request.getContextPath()%>/PeticionModuloIntegracion.do?tarea=preparar&modulo=MELANBIDE72&operacion=cargarModificarMedidaC&tipo=0&nuevo=0&numExp=<%=numExpediente%>&id=' + listaMedidasC[tablaMedidasC.selectedIndex][0] + '&control=' + control.getTime(), 680, 800, 'no', 'no', function (result) {
                        if (result != undefined) {
                            if (result[0] == '0') {
                                recargarTablaMedidasC(result);
                            }
                        }
                    });

                } else {
                    jsp_alerta('A', '<%=meLanbide72I18n.getMensaje(idiomaUsuario, "msg.msjNoSelecFila")%>');
                }
            }

            function pulsarEliminarMedidaC() {
                if (tablaMedidasC.selectedIndex != -1) {
                    var resultado = jsp_alerta('', '<%=meLanbide72I18n.getMensaje(idiomaUsuario, "msg.preguntaEliminar")%>');
                    if (resultado == 1) {

                        var ajax = getXMLHttpRequest();
                        var nodos = null;
                        var CONTEXT_PATH = '<%=request.getContextPath()%>';
                        var url = CONTEXT_PATH + "/PeticionModuloIntegracion.do";
                        var parametros = "";
                        var control = new Date();
                        parametros = 'tarea=preparar&modulo=MELANBIDE72&operacion=eliminarMedidaC&tipo=0&numExp=<%=numExpediente%>&id=' + listaMedidasC[tablaMedidasC.selectedIndex][0] + '&control=' + control.getTime();
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
                                        } else if (hijosFila[cont].nodeName == "NOMBRE") {
                                            if (hijosFila[cont].childNodes.length > 0) {
                                                fila[1] = hijosFila[cont].childNodes[0].nodeValue;
                                            } else {
                                                fila[1] = '-';
                                            }
                                        } else if (hijosFila[cont].nodeName == "NIF_CIF") {
                                            if (hijosFila[cont].childNodes[0].nodeValue != "null") {
                                                fila[2] = hijosFila[cont].childNodes[0].nodeValue;
                                            } else {
                                                fila[2] = '-';
                                            }
                                        } else if (hijosFila[cont].nodeName == "IMPORTE_ANUAL") {
                                            if (hijosFila[cont].childNodes[0].nodeValue != "null") {
                                                fila[3] = hijosFila[cont].childNodes[0].nodeValue;
                                                var tex = fila[3].toString();
                                                tex = tex.replace(".", ",");
                                                fila[3] = tex;
                                            } else {
                                                fila[3] = '-';
                                            }
                                        }
                                    }
                                    listaNueva[j] = fila;
                                    fila = new Array();
                                }
                            }//for(j=0;hijos!=null && j<hijos.length;j++)
                            if (codigoOperacion == "0") {
                                recargarTablaMedidasC(listaNueva);
                            } else if (codigoOperacion == "1") {
                                jsp_alerta("A", '<%=meLanbide72I18n.getMensaje(idiomaUsuario,"error.errorBD")%>');
                            } else if (codigoOperacion == "2") {
                                jsp_alerta("A", '<%=meLanbide72I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                            } else if (codigoOperacion == "3") {
                                jsp_alerta("A", '<%=meLanbide72I18n.getMensaje(idiomaUsuario,"error.pasoParametros")%>');
                            } else {
                                jsp_alerta("A", '<%=meLanbide72I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                            }//if(
                        } catch (Err) {
                            jsp_alerta("A", '<%=meLanbide72I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                        }//try-catch
                    }
                } else {
                    jsp_alerta('A', '<%=meLanbide72I18n.getMensaje(idiomaUsuario, "msg.msjNoSelecFila")%>');
                }
            }

            function cuerpoTablaA1() {
                tablaMedidasA1 = new Tabla(true, '<%=descriptor.getDescripcion("buscar")%>', '<%=descriptor.getDescripcion("anterior")%>', '<%=descriptor.getDescripcion("siguiente")%>', '<%=descriptor.getDescripcion("mosFilasPag")%>', '<%=descriptor.getDescripcion("msgNoResultBusq")%>', '<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>', '<%=descriptor.getDescripcion("filtrDeTotal")%>', '<%=descriptor.getDescripcion("primero")%>', '<%=descriptor.getDescripcion("ultimo")%>', document.getElementById('listaMedidasA1'), 1350);
                tablaMedidasA1.addColumna('0', 'center', "<%=meLanbide72I18n.getMensaje(idiomaUsuario,"medidasAlt.tablaMedidas.id")%>");//indice
                tablaMedidasA1.addColumna('150', 'center', "<%=meLanbide72I18n.getMensaje(idiomaUsuario,"medidasAlt.tablaMedidas.nombre")%>");//nombre
                tablaMedidasA1.addColumna('50', 'center', "<%=meLanbide72I18n.getMensaje(idiomaUsuario,"medidasAlt.tablaMedidas.nif")%>");//cif
                tablaMedidasA1.addColumna('50', 'center', "<%=meLanbide72I18n.getMensaje(idiomaUsuario,"medidasAlt.tablaMedidas.importeAnual")%>");//importe anual
                tablaMedidasA1.addColumna('250', 'left', "<%=meLanbide72I18n.getMensaje(idiomaUsuario,"medidasAlt.tablaMedidas.objetoContrato")%>");//objeto contrato

                tablaMedidasA1.displayCabecera = true;
                tablaMedidasA1.height = 300;
            }

            function cuerpoTablaA2() {
                tablaMedidasA2 = new Tabla(true, '<%=descriptor.getDescripcion("buscar")%>', '<%=descriptor.getDescripcion("anterior")%>', '<%=descriptor.getDescripcion("siguiente")%>', '<%=descriptor.getDescripcion("mosFilasPag")%>', '<%=descriptor.getDescripcion("msgNoResultBusq")%>', '<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>', '<%=descriptor.getDescripcion("filtrDeTotal")%>', '<%=descriptor.getDescripcion("primero")%>', '<%=descriptor.getDescripcion("ultimo")%>', document.getElementById('listaMedidasA2'), 1350);
                tablaMedidasA2.addColumna('0', 'center', "<%=meLanbide72I18n.getMensaje(idiomaUsuario,"medidasAlt.tablaMedidas.id")%>");//indice
                tablaMedidasA2.addColumna('150', 'center', "<%=meLanbide72I18n.getMensaje(idiomaUsuario,"medidasAlt.tablaMedidas.nombre")%>");//nombre

                tablaMedidasA2.addColumna('80', 'center', "<%=meLanbide72I18n.getMensaje(idiomaUsuario,"medidasAlt.tablaMedidas.apellido1")%>");//apellido1
                tablaMedidasA2.addColumna('80', 'center', "<%=meLanbide72I18n.getMensaje(idiomaUsuario,"medidasAlt.tablaMedidas.apellido2")%>");//apellido2
                tablaMedidasA2.addColumna('50', 'center', "<%=meLanbide72I18n.getMensaje(idiomaUsuario,"medidasAlt.tablaMedidas.tipoDocumento")%>");//tipoDocumento
                tablaMedidasA2.addColumna('60', 'center', "<%=meLanbide72I18n.getMensaje(idiomaUsuario,"medidasAlt.tablaMedidas.dni")%>");//dni
                tablaMedidasA2.addColumna('60', 'center', "<%=meLanbide72I18n.getMensaje(idiomaUsuario,"medidasAlt.tablaMedidas.tfno")%>");//tfno
                tablaMedidasA2.addColumna('80', 'center', "<%=meLanbide72I18n.getMensaje(idiomaUsuario,"medidasAlt.tablaMedidas.email")%>");//email
                tablaMedidasA2.addColumna('80', 'center', "<%=meLanbide72I18n.getMensaje(idiomaUsuario,"medidasAlt.tablaMedidas.provincia")%>");//provincia
                tablaMedidasA2.addColumna('80', 'center', "<%=meLanbide72I18n.getMensaje(idiomaUsuario,"medidasAlt.tablaMedidas.municipio")%>");//municipio
                tablaMedidasA2.addColumna('80', 'center', "<%=meLanbide72I18n.getMensaje(idiomaUsuario,"medidasAlt.tablaMedidas.localidad")%>");//localidad
                tablaMedidasA2.addColumna('150', 'center', "<%=meLanbide72I18n.getMensaje(idiomaUsuario,"medidasAlt.tablaMedidas.direccion")%>");//direccion
                tablaMedidasA2.addColumna('40', 'center', "<%=meLanbide72I18n.getMensaje(idiomaUsuario,"medidasAlt.tablaMedidas.portal")%>");//portal
                tablaMedidasA2.addColumna('40', 'center', "<%=meLanbide72I18n.getMensaje(idiomaUsuario,"medidasAlt.tablaMedidas.piso")%>");//piso
                tablaMedidasA2.addColumna('40', 'center', "<%=meLanbide72I18n.getMensaje(idiomaUsuario,"medidasAlt.tablaMedidas.letra")%>");//letra
                tablaMedidasA2.addColumna('40', 'center', "<%=meLanbide72I18n.getMensaje(idiomaUsuario,"medidasAlt.tablaMedidas.cPostal")%>");//c_postal

                tablaMedidasA2.addColumna('50', 'center', "<%=meLanbide72I18n.getMensaje(idiomaUsuario,"medidasAlt.tablaMedidas.importeAnual")%>");//importe anual
                tablaMedidasA2.addColumna('150', 'left', "<%=meLanbide72I18n.getMensaje(idiomaUsuario,"medidasAlt.tablaMedidas.objetoContrato")%>");//objeto contrato

                tablaMedidasA2.displayCabecera = true;
                tablaMedidasA2.height = 300;
            }

            function cuerpoTablaB() {
                tablaMedidasB = new Tabla(true, '<%=descriptor.getDescripcion("buscar")%>', '<%=descriptor.getDescripcion("anterior")%>', '<%=descriptor.getDescripcion("siguiente")%>', '<%=descriptor.getDescripcion("mosFilasPag")%>', '<%=descriptor.getDescripcion("msgNoResultBusq")%>', '<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>', '<%=descriptor.getDescripcion("filtrDeTotal")%>', '<%=descriptor.getDescripcion("primero")%>', '<%=descriptor.getDescripcion("ultimo")%>', document.getElementById('listaMedidasB'), 1350);
                tablaMedidasB.addColumna('0', 'center', "<%=meLanbide72I18n.getMensaje(idiomaUsuario,"medidasAlt.tablaMedidas.id")%>");//indice
                tablaMedidasB.addColumna('150', 'center', "<%=meLanbide72I18n.getMensaje(idiomaUsuario,"medidasAlt.tablaMedidas.nombre")%>");//nombre
                tablaMedidasB.addColumna('50', 'center', "<%=meLanbide72I18n.getMensaje(idiomaUsuario,"medidasAlt.tablaMedidas.nif")%>");//cif
                tablaMedidasB.addColumna('50', 'center', "<%=meLanbide72I18n.getMensaje(idiomaUsuario,"medidasAlt.tablaMedidas.importeAnual")%>");//importe anual

                tablaMedidasB.displayCabecera = true;
                tablaMedidasB.height = 300;
            }

            function cuerpoTablaC() {
                tablaMedidasC = new Tabla(true, '<%=descriptor.getDescripcion("buscar")%>', '<%=descriptor.getDescripcion("anterior")%>', '<%=descriptor.getDescripcion("siguiente")%>', '<%=descriptor.getDescripcion("mosFilasPag")%>', '<%=descriptor.getDescripcion("msgNoResultBusq")%>', '<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>', '<%=descriptor.getDescripcion("filtrDeTotal")%>', '<%=descriptor.getDescripcion("primero")%>', '<%=descriptor.getDescripcion("ultimo")%>', document.getElementById('listaMedidasC'), 1350);
                tablaMedidasC.addColumna('0', 'center', "<%=meLanbide72I18n.getMensaje(idiomaUsuario,"medidasAlt.tablaMedidas.id")%>");//indice
                tablaMedidasC.addColumna('150', 'center', "<%=meLanbide72I18n.getMensaje(idiomaUsuario,"medidasAlt.tablaMedidas.nombre")%>");//nombre
                tablaMedidasC.addColumna('50', 'center', "<%=meLanbide72I18n.getMensaje(idiomaUsuario,"medidasAlt.tablaMedidas.nif")%>");//cif
                tablaMedidasC.addColumna('50', 'center', "<%=meLanbide72I18n.getMensaje(idiomaUsuario,"medidasAlt.tablaMedidas.importeAnual")%>");//importe anual

                tablaMedidasC.displayCabecera = true;
                tablaMedidasC.height = 300;
            }

            function recargarTablaMedidasA1(result) {
                var fila;
                listaMedidasA1 = new Array();
                listaMedidasTablaA1 = new Array();
                for (var i = 1; i < result.length; i++) {
                    fila = result[i];
                    //listaMedidasA1[i - 1] = fila;//NO FUNCIONA IE9
                    listaMedidasA1[i - 1] = [fila[0], fila[1], fila[2], fila[3], fila[4]];
                    listaMedidasTablaA1[i - 1] = [fila[0], fila[1], fila[2], fila[3], fila[4]];
                }

                cuerpoTablaA1();

                tablaMedidasA1.lineas = listaMedidasTablaA1;

                //tablaMedidasA1.displayTabla();
                tablaMedidasA1.displayTablaConTooltips(listaMedidasA1);

            }

            function recargarTablaMedidasA2(result) {
                var fila;
                listaMedidasA2 = new Array();
                listaMedidasTablaA2 = new Array();
                for (var i = 1; i < result.length; i++) {
                    fila = result[i];
                    listaMedidasA2[i - 1] = [fila[0], fila[1], fila[2], fila[3], fila[4], fila[5], fila[6], fila[7], fila[8], fila[9], fila[10], fila[11], fila[12], fila[13], fila[14], fila[15], fila[16], fila[17]];
                    listaMedidasTablaA2[i - 1] = [fila[0], fila[1], fila[2], fila[3], fila[4], fila[5], fila[6], fila[7], fila[8], fila[9], fila[10], fila[11], fila[12], fila[13], fila[14], fila[15], fila[16], fila[17]];
                }

                cuerpoTablaA2();

                tablaMedidasA2.lineas = listaMedidasTablaA2;
                tablaMedidasA2.displayTablaConTooltips(listaMedidasA2);

            }

            function recargarTablaMedidasB(result) {
                var fila;
                listaMedidasB = new Array();
                listaMedidasTablaB = new Array();
                for (var i = 1; i < result.length; i++) {
                    fila = result[i];
                    listaMedidasB[i - 1] = [fila[0], fila[1], fila[2], fila[3]];
                    listaMedidasTablaB[i - 1] = [fila[0], fila[1], fila[2], fila[3]];
                }

                cuerpoTablaB();

                tablaMedidasB.lineas = listaMedidasTablaB;
                tablaMedidasB.displayTablaConTooltips(listaMedidasB);

            }

            function recargarTablaMedidasC(result) {
                var fila;
                listaMedidasC = new Array();
                listaMedidasTablaC = new Array();
                for (var i = 1; i < result.length; i++) {
                    fila = result[i];
                    listaMedidasC[i - 1] = [fila[0], fila[1], fila[2], fila[3]];
                    listaMedidasTablaC[i - 1] = [fila[0], fila[1], fila[2], fila[3]];
                }

                cuerpoTablaC();

                tablaMedidasC.lineas = listaMedidasTablaC;
                tablaMedidasC.displayTablaConTooltips(listaMedidasC);

            }

        </script>


        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script>
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/>
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/extension/melanbide72/melanbide72.css"/>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/validaciones.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/listaComboBox.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/popup.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/melanbide72/JavaScriptUtil.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/melanbide72/InputMask.js"></script>
        <script type="text/javascript">
            var APP_CONTEXT_PATH = '<%=request.getContextPath()%>';
        </script>
    </head>
    <body>
        <div class="tab-page" id="tabPage352" style="height:520px; width: 100%;">
            <h2 class="tab" id="pestana352"><%=meLanbide72I18n.getMensaje(idiomaUsuario,"label.titulo.pestana")%></h2> 
            <script type="text/javascript">tp1.addTabPage(document.getElementById("tabPage352"));</script>
            <br>
            <h2 class="legendAzul" id="pestanaPrinc"><%=meLanbide72I18n.getMensaje(idiomaUsuario,"label.tituloPrincipal")%></h2>
            <div>
                <br>
                <div id="divGeneral">  
                    <!-- <form> -->
                    <div class="lineaFormulario">
                        <input type="checkbox" id="cb_contrato_mercantil" name="cb_contrato_mercantil" value="contrato_mercantil">
                        <label class="etiqueta"><b><%=meLanbide72I18n.getMensaje(idiomaUsuario, "medidasAlt.A")%></b></label>
                    </div>
                    <br>
                    <div id="div_contrato_mercantil">
                        <fieldset>
                            <div class="lineaFormulario" style="margin-left: 10px;">
                                <input type="checkbox" id="cb_centro_especial_empleo" name="cb_centro_especial_empleo" value="centro_especial_empleo">
                                <label class="etiqueta"><b><%=meLanbide72I18n.getMensaje(idiomaUsuario, "medidasAlt.A1")%></b></label>
                            </div>
                            <div id="div_A1">
                                <fieldset>
                                    <br>
                                    <div id="listaMedidasA1" align="center"></div>
                                    <br>
                                    <div class="botonera" style="text-align: center;">
                                        <input type="button" id="btnNuevaMedidaA1" name="btnNuevaMedidaA1" class="botonGeneral"  value="<%=meLanbide72I18n.getMensaje(idiomaUsuario, "btn.nuevo")%>" onclick="pulsarNuevaMedidaA1();">
                                        <input type="button" id="btnModificarMedidaA1" name="btnModificarMedidaA1" class="botonGeneral" value="<%=meLanbide72I18n.getMensaje(idiomaUsuario, "btn.modificar")%>" onclick="pulsarModificarMedidaA1();">
                                        <input type="button" id="btnEliminarMedidaA1" name="btnEliminarMedidaA1"   class="botonGeneral" value="<%=meLanbide72I18n.getMensaje(idiomaUsuario, "btn.eliminar")%>" onclick="pulsarEliminarMedidaA1();">
                                    </div>
                                    <br>
                                    <div class="lineaFormulario" style="margin-left: 10px;">
                                        <label class="etiqueta"><%=meLanbide72I18n.getMensaje(idiomaUsuario, "medidasAlt.num_trab_sust")%></label>
                                        <input id="num_trab_sust_A1" name="num_trab_sust_A1" type="text" class="inputTexto" size="4" maxlength="4" />
                                    </div>
                                    <div class="lineaFormulario" style="margin-left: 10px;">
                                        <label class="etiqueta"><%=meLanbide72I18n.getMensaje(idiomaUsuario, "medidasAlt.periodo")%></label>
                                        <input id="periodo_contr_A1" name="periodo_contr_A1" type="text" class="inputTexto" size="2" maxlength="2" />
                                    </div>
                                </fieldset>
                            </div>
                            <div class="lineaFormulario" style="margin-left: 10px;">
                                <input type="checkbox" id="cb_pers_autonoma_disc" name="cb_pers_autonoma_disc" value="pers_autonoma_disc">
                                <label class="etiqueta"><b><%=meLanbide72I18n.getMensaje(idiomaUsuario, "medidasAlt.A2")%></b></label>
                            </div>
                            <div id="div_A2">
                                <fieldset>
                                    <br>
                                    <div id="listaMedidasA2" align="center"></div>
                                    <br>
                                    <div class="botonera" style="text-align: center;">
                                        <input type="button" id="btnNuevaMedidaA2" name="btnNuevaMedidaA2" class="botonGeneral"  value="<%=meLanbide72I18n.getMensaje(idiomaUsuario, "btn.nuevo")%>" onclick="pulsarNuevaMedidaA2();">
                                        <input type="button" id="btnModificarMedidaA2" name="btnModificarMedidaA2" class="botonGeneral" value="<%=meLanbide72I18n.getMensaje(idiomaUsuario, "btn.modificar")%>" onclick="pulsarModificarMedidaA2();">
                                        <input type="button" id="btnEliminarMedidaA2" name="btnEliminarMedidaA2"   class="botonGeneral" value="<%=meLanbide72I18n.getMensaje(idiomaUsuario, "btn.eliminar")%>" onclick="pulsarEliminarMedidaA2();">
                                    </div>
                                    <br>
                                    <div class="lineaFormulario" style="margin-left: 10px;">
                                        <label class="etiqueta"><%=meLanbide72I18n.getMensaje(idiomaUsuario, "medidasAlt.num_trab_sust")%></label>
                                        <input id="num_trab_sust_A2" name="num_trab_sust_A2" type="text" class="inputTexto" size="4" maxlength="4" />
                                    </div>
                                    <div class="lineaFormulario" style="margin-left: 10px;">
                                        <label class="etiqueta"><%=meLanbide72I18n.getMensaje(idiomaUsuario, "medidasAlt.periodo")%></label>
                                        <input id="periodo_contr_A2" name="periodo_contr_A2" type="text" class="inputTexto" size="2" maxlength="2" />
                                    </div>
                                </fieldset>
                            </div>
                        </fieldset>
                    </div>
                    <br>
                    <div class="lineaFormulario">
                        <input type="checkbox" id="cb_donacion_fundacion" name="cb_donacion_fundacion" value="donacion_fundacion">
                        <label class="etiqueta"><b><%=meLanbide72I18n.getMensaje(idiomaUsuario, "medidasAlt.B")%></b></label>
                    </div>
                    <br>
                    <div id="div_donacion_fundacion">
                        <fieldset>
                            <br>
                            <div id="listaMedidasB" align="center"></div>
                            <br>
                            <div class="botonera" style="text-align: center;">
                                <input type="button" id="btnNuevaMedidaB" name="btnNuevaMedidaB" class="botonGeneral"  value="<%=meLanbide72I18n.getMensaje(idiomaUsuario, "btn.nuevo")%>" onclick="pulsarNuevaMedidaB();">
                                <input type="button" id="btnModificarMedidaB" name="btnModificarMedidaB" class="botonGeneral" value="<%=meLanbide72I18n.getMensaje(idiomaUsuario, "btn.modificar")%>" onclick="pulsarModificarMedidaB();">
                                <input type="button" id="btnEliminarMedidaB" name="btnEliminarMedidaB"   class="botonGeneral" value="<%=meLanbide72I18n.getMensaje(idiomaUsuario, "btn.eliminar")%>" onclick="pulsarEliminarMedidaB();">
                            </div>
                            <br>
                            <div class="lineaFormulario" style="margin-left: 10px;">
                                <label class="etiqueta"><%=meLanbide72I18n.getMensaje(idiomaUsuario, "medidasAlt.num_trab_sust")%></label>
                                <input id="num_trab_sust_B" name="num_trab_sust_B" type="text" class="inputTexto" size="4" maxlength="4" />
                            </div>
                            <div class="lineaFormulario" style="margin-left: 10px;">
                                <label class="etiqueta"><%=meLanbide72I18n.getMensaje(idiomaUsuario, "medidasAlt.periodo")%></label>
                                <input id="periodo_contr_B" name="periodo_contr_B" type="text" class="inputTexto" size="2" maxlength="2" />
                            </div>
                        </fieldset>
                    </div>
                    <br>
                    <div class="lineaFormulario">
                        <input type="checkbox" id="cb_enclave_laboral" name="cb_enclave_laboral" value="enclave_laboral">
                        <label class="etiqueta"><b><%=meLanbide72I18n.getMensaje(idiomaUsuario, "medidasAlt.C")%></b></label>
                    </div>
                    <br>
                    <div id="div_enclave_laboral">
                        <fieldset>
                            <br>
                            <div id="listaMedidasC" align="center"></div>
                            <br>
                            <div class="botonera" style="text-align: center;">
                                <input type="button" id="btnNuevaMedidaC" name="btnNuevaMedidaC" class="botonGeneral"  value="<%=meLanbide72I18n.getMensaje(idiomaUsuario, "btn.nuevo")%>" onclick="pulsarNuevaMedidaC();">
                                <input type="button" id="btnModificarMedidaC" name="btnModificarMedidaC" class="botonGeneral" value="<%=meLanbide72I18n.getMensaje(idiomaUsuario, "btn.modificar")%>" onclick="pulsarModificarMedidaC();">
                                <input type="button" id="btnEliminarMedidaC" name="btnEliminarMedidaC"   class="botonGeneral" value="<%=meLanbide72I18n.getMensaje(idiomaUsuario, "btn.eliminar")%>" onclick="pulsarEliminarMedidaC();">
                            </div>
                            <br>
                            <div class="lineaFormulario" style="margin-left: 10px;">
                                <label class="etiqueta"><%=meLanbide72I18n.getMensaje(idiomaUsuario, "medidasAlt.num_trab_sust")%></label>
                                <input id="num_trab_sust_C" name="num_trab_sust_C" type="text" class="inputTexto" size="4" maxlength="4" />
                            </div>
                            <div class="lineaFormulario" style="margin-left: 10px;">
                                <label class="etiqueta"><%=meLanbide72I18n.getMensaje(idiomaUsuario, "medidasAlt.periodo")%></label>
                                <input id="periodo_contr_C" name="periodo_contr_C" type="text" class="inputTexto" size="2" maxlength="2" />
                            </div>
                            <div class="lineaFormulario" style="margin-left: 10px;">
                                <label class="etiqueta"><%=meLanbide72I18n.getMensaje(idiomaUsuario, "medidasAlt.per_disca_ocup")%></label>
                                <input id="pers_disc_ocup_C" name="pers_disc_ocup_C" type="text" class="inputTexto" size="4" maxlength="4" />
                            </div>
                        </fieldset>
                    </div>
                    <br><br>
                    <div id="div_campos_suplementarios">
                        <div class="lineaFormulario">
                            <label class="etiqueta"><%=meLanbide72I18n.getMensaje(idiomaUsuario, "medidasAlt.cuantificacion_econ_1")%></label>
                            <input id="cuantificacion_econ" name="cuantificacion_econ" type="text" class="inputTexto" size="10" maxlength="10" />
                            <label class="etiqueta"><%=meLanbide72I18n.getMensaje(idiomaUsuario, "medidasAlt.cuantificacion_econ_2")%></label>
                        </div>
                        <br><br>
                        <div class="lineaFormulario">
                            <label class="etiqueta"><b><%=meLanbide72I18n.getMensaje(idiomaUsuario, "medidasAlt.cumplim_solo")%></b></label>
                        </div>
                        <fieldset>
                            <div class="lineaFormulario">
                                <label class="etiqueta"><%=meLanbide72I18n.getMensaje(idiomaUsuario, "medidasAlt.imp_anual_total")%></label>
                                <input id="imp_anual_total" name="imp_anual_total" type="text" class="inputTexto" size="10" maxlength="10" />
                                <label class="etiqueta"><%=meLanbide72I18n.getMensaje(idiomaUsuario, "medidasAlt.contratacion_total_1")%></label>
                                <input id="contratacion_total" name="contratacion_total" type="text" class="inputTexto" size="4" maxlength="4" />
                                <label class="etiqueta"><%=meLanbide72I18n.getMensaje(idiomaUsuario, "medidasAlt.contratacion_total_2")%></label>
                            </div>
                        </fieldset>
                    </div>
                    <!-- </form> -->
                </div>
                <br><br>



                <div class="botonera" style="text-align: center;">
                    <input type="button" id="btnGrabarMedidas" name="btnGrabarMedidas" class="botonGeneral"  value="<%=meLanbide72I18n.getMensaje(idiomaUsuario, "btn.grabar")%>" onclick="guardarDatos();">
                </div>
            </div>  
        </div>


        <script  type="text/javascript">

            //Tablas Medidas Alternativas
            var tablaMedidasA1;
            var listaMedidasA1 = new Array();
            var listaMedidasTablaA1 = new Array();

            var tablaMedidasA2;
            var listaMedidasA2 = new Array();
            var listaMedidasTablaA2 = new Array();

            var tablaMedidasB;
            var listaMedidasB = new Array();
            var listaMedidasTablaB = new Array();

            var tablaMedidasC;
            var listaMedidasC = new Array();
            var listaMedidasTablaC = new Array();

            cuerpoTablaA1();
            cuerpoTablaA2();
            cuerpoTablaB();
            cuerpoTablaC();

            <%  	
            MedidaA1BCVO medA1VO = null;
            List<MedidaA1BCVO> listA1 = null;
            if(request.getAttribute("listaMedidasA1")!=null){
                listA1 = (List<MedidaA1BCVO>)request.getAttribute("listaMedidasA1");
            }													
            if (listA1!= null && listA1.size() >0){
                for (int indice=0;indice<listA1.size();indice++)
                {
                    medA1VO = listA1.get(indice);
                    String nombre="";
                    if(medA1VO.getNombre()!=null){
                        nombre=medA1VO.getNombre();
                    }else{
                        nombre="-";
                    }
                    String nif="";
                    if(medA1VO.getNif()!=null){
                        nif=medA1VO.getNif();
                    }else{
                        nif="-";
                    }
                    String importe="";
                    if(medA1VO.getImporteAnual()!=null){
                        importe=String.valueOf((medA1VO.getImporteAnual().toString()).replace(".",","));
                    }else{
                        importe="-";
                    }
                    String objetoContrato="";
                    if(medA1VO.getObjetoContrato()!=null){
                        //objetoContrato=medA1VO.getObjetoContrato().replaceAll("\\n", "<br>");
                        objetoContrato=medA1VO.getObjetoContrato().replaceAll("\\n", " ");
                    }else{
                        objetoContrato="-";
                    }
            %>
            listaMedidasA1[<%=indice%>] = ['<%=medA1VO.getId()%>', '<%=nombre%>',
                '<%=nif%>', '<%=importe%>', '<%=objetoContrato%>'];
            listaMedidasTablaA1[<%=indice%>] = ['<%=medA1VO.getId()%>', '<%=nombre%>',
                '<%=nif%>', '<%=importe%>', '<%=objetoContrato%>'];
            <%
                }// for
            }// if
    
            MedidaA2VO medA2VO = null;
            List<MedidaA2VO> listA2 = null;
            if(request.getAttribute("listaMedidasA2")!=null){
                listA2 = (List<MedidaA2VO>)request.getAttribute("listaMedidasA2");
            }													
            if (listA2!= null && listA2.size() >0){
                for (int indice=0;indice<listA2.size();indice++)
                {
                    medA2VO = listA2.get(indice);
                    String nombre="";
                    if(medA2VO.getNombre()!=null){
                        nombre=medA2VO.getNombre();
                    }else{
                        nombre="-";
                    }
                    String apellido1="";
                    if(medA2VO.getApellido1()!=null){
                        apellido1=medA2VO.getApellido1();
                    }else{
                        apellido1="-";
                    }
                    String apellido2="";
                    if(medA2VO.getApellido2()!=null){
                        apellido2=medA2VO.getApellido2();
                    }else{
                        apellido2="-";
                    }
                    String tipoDocumento="";
                    if(medA2VO.getTipoDocumento()!=null){
                        tipoDocumento=medA2VO.getTipoDocumento();
                    }else{
                        tipoDocumento="-";
                    }
                    String dni="";
                    if(medA2VO.getDni()!=null){
                        dni=medA2VO.getDni();
                    }else{
                        dni="-";
                    }
                    String tfno="";
                    if(medA2VO.getTfno()!=null){
                        tfno=medA2VO.getTfno();
                    }else{
                        tfno="-";
                    }
                    String email="";
                    if(medA2VO.getEmail()!=null){
                        email=medA2VO.getEmail();
                    }else{
                        email="-";
                    }
                    String provincia="";
                    if(medA2VO.getProvincia()!=null){
                        provincia=medA2VO.getProvincia();
                    }else{
                        provincia="-";
                    }
                    String municipio="";
                    if(medA2VO.getMunicipio()!=null){
                        municipio=medA2VO.getMunicipio();
                    }else{
                        municipio="-";
                    }
                    String localidad="";
                    if(medA2VO.getLocalidad()!=null){
                        localidad=medA2VO.getLocalidad();
                    }else{
                        localidad="-";
                    }
                    String direccion="";
                    if(medA2VO.getDireccion()!=null){
                        direccion=medA2VO.getDireccion();
                    }else{
                        direccion="-";
                    }
                    String portal="";
                    if(medA2VO.getPortal()!=null){
                        portal=medA2VO.getPortal();
                    }else{
                        portal="-";
                    }
                    String piso="";
                    if(medA2VO.getPiso()!=null){
                        piso=medA2VO.getPiso();
                    }else{
                        piso="-";
                    }
                    String letra="";
                    if(medA2VO.getLetra()!=null){
                        letra=medA2VO.getLetra();
                    }else{
                        letra="-";
                    }
                    String cPostal="";
                    if(medA2VO.getC_postal()!=null){
                        cPostal=medA2VO.getC_postal();
                    }else{
                        cPostal="-";
                    }
                    String importe="";
                    if(medA2VO.getImporteAnual()!=null){
                        importe=String.valueOf((medA2VO.getImporteAnual().toString()).replace(".",","));
                    }else{
                        importe="-";
                    }
                    String objetoContrato="";
                    if(medA2VO.getObjetoContrato()!=null){
                        objetoContrato=medA2VO.getObjetoContrato().replaceAll("\\n", " ");
                    }else{
                        objetoContrato="-";
                    }
            %>
            listaMedidasA2[<%=indice%>] = ['<%=medA2VO.getId()%>', '<%=nombre%>', '<%=apellido1%>', '<%=apellido2%>', '<%=tipoDocumento%>', '<%=dni%>',
                '<%=tfno%>', '<%=email%>', '<%=provincia%>', '<%=municipio%>', '<%=localidad%>', '<%=direccion%>', '<%=portal%>', '<%=piso%>',
                '<%=letra%>', '<%=cPostal%>', '<%=importe%>', '<%=objetoContrato%>'];
            listaMedidasTablaA2[<%=indice%>] = ['<%=medA2VO.getId()%>', '<%=nombre%>', '<%=apellido1%>', '<%=apellido2%>', '<%=tipoDocumento%>', '<%=dni%>',
                '<%=tfno%>', '<%=email%>', '<%=provincia%>', '<%=municipio%>', '<%=localidad%>', '<%=direccion%>', '<%=portal%>', '<%=piso%>',
                '<%=letra%>', '<%=cPostal%>', '<%=importe%>', '<%=objetoContrato%>'];
            <%
                }// for
            }// if

            MedidaA1BCVO medBVO = null;
            List<MedidaA1BCVO> listB = null;
            if(request.getAttribute("listaMedidasB")!=null){
                listB= (List<MedidaA1BCVO>)request.getAttribute("listaMedidasB");
            }													
            if (listB!= null && listB.size() >0){
                for (int indice=0;indice<listB.size();indice++)
                {
                    medBVO = listB.get(indice);
                    String nombre="";
                    if(medBVO.getNombre()!=null){
                        nombre=medBVO.getNombre();
                    }else{
                        nombre="-";
                    }
                    String nif="";
                    if(medBVO.getNif()!=null){
                        nif=medBVO.getNif();
                    }else{
                        nif="-";
                    }
                    String importe="";
                    if(medBVO.getImporteAnual()!=null){
                        importe=String.valueOf((medBVO.getImporteAnual().toString()).replace(".",","));
                    }else{
                        importe="-";
                    }
            %>
            listaMedidasB[<%=indice%>] = ['<%=medBVO.getId()%>', '<%=nombre%>',
                '<%=nif%>', '<%=importe%>'];
            listaMedidasTablaB[<%=indice%>] = ['<%=medBVO.getId()%>', '<%=nombre%>',
                '<%=nif%>', '<%=importe%>'];
            <%
                }// for
            }// if

            MedidaA1BCVO medCVO = null;
            List<MedidaA1BCVO> listC = null;
            if(request.getAttribute("listaMedidasC")!=null){
                listC= (List<MedidaA1BCVO>)request.getAttribute("listaMedidasC");
            }													
            if (listC!= null && listC.size() >0){
                for (int indice=0;indice<listC.size();indice++)
                {
                    medCVO = listC.get(indice);
                    String nombre="";
                    if(medCVO.getNombre()!=null){
                        nombre=medCVO.getNombre();
                    }else{
                        nombre="-";
                    }
                    String nif="";
                    if(medCVO.getNif()!=null){
                        nif=medCVO.getNif();
                    }else{
                        nif="-";
                    }
                    String importe="";
                    if(medCVO.getImporteAnual()!=null){
                        importe=String.valueOf((medCVO.getImporteAnual().toString()).replace(".",","));
                    }else{
                        importe="-";
                    }
            %>
            listaMedidasC[<%=indice%>] = ['<%=medCVO.getId()%>', '<%=nombre%>',
                '<%=nif%>', '<%=importe%>'];
            listaMedidasTablaC[<%=indice%>] = ['<%=medCVO.getId()%>', '<%=nombre%>',
                '<%=nif%>', '<%=importe%>'];
            <%
                }// for
            }// if

            //Campos suplementarios
            //Medida A1
            String num_trab_sust_A1 = "";
            if(request.getAttribute("num_trab_sust_A1")!=null){
                num_trab_sust_A1 = (String)request.getAttribute("num_trab_sust_A1");
            }
            String periodo_contr_A1 = "";
            if(request.getAttribute("periodo_contr_A1")!=null){
                periodo_contr_A1 = (String)request.getAttribute("periodo_contr_A1");
            }

            //Medida A2
            String num_trab_sust_A2 = "";
            if(request.getAttribute("num_trab_sust_A2")!=null){
                num_trab_sust_A2 = (String)request.getAttribute("num_trab_sust_A2");
            }
            String periodo_contr_A2 = "";
            if(request.getAttribute("periodo_contr_A2")!=null){
                periodo_contr_A2 = (String)request.getAttribute("periodo_contr_A2");
            }

            //Medida B
            String num_trab_sust_B = "";
            if(request.getAttribute("num_trab_sust_B")!=null){
                num_trab_sust_B = (String)request.getAttribute("num_trab_sust_B");
            }
            String periodo_contr_B = "";
            if(request.getAttribute("periodo_contr_B")!=null){
                periodo_contr_B = (String)request.getAttribute("periodo_contr_B");
            }

            //Medida C
            String num_trab_sust_C = "";
            if(request.getAttribute("num_trab_sust_C")!=null){
                num_trab_sust_C = (String)request.getAttribute("num_trab_sust_C");
            }
            String periodo_contr_C = "";
            if(request.getAttribute("periodo_contr_C")!=null){
                periodo_contr_C = (String)request.getAttribute("periodo_contr_C");
            }
            String pers_disc_ocup_C = "";
            if(request.getAttribute("pers_disc_ocup_C")!=null){
                pers_disc_ocup_C = (String)request.getAttribute("pers_disc_ocup_C");
            }

            //General
            String cuant_econ_anual = "";
            if(request.getAttribute("cuant_econ_anual")!=null){
                cuant_econ_anual = (String)request.getAttribute("cuant_econ_anual");
            }
            String imp_anual_total = "";
            if(request.getAttribute("imp_anual_total")!=null){
                imp_anual_total = (String)request.getAttribute("imp_anual_total");
            }
            String contratacion_total = "";
            if(request.getAttribute("contratacion_total")!=null){
                contratacion_total = (String)request.getAttribute("contratacion_total");
            }
            %>

            tablaMedidasA1.lineas = listaMedidasTablaA1;
            //tablaMedidasA1.displayTabla();
            tablaMedidasA1.displayTablaConTooltips(listaMedidasA1);

            tablaMedidasA2.lineas = listaMedidasTablaA2;
            tablaMedidasA2.displayTablaConTooltips(listaMedidasA2);

            tablaMedidasB.lineas = listaMedidasTablaB;
            tablaMedidasB.displayTablaConTooltips(listaMedidasB);

            tablaMedidasC.lineas = listaMedidasTablaC;
            tablaMedidasC.displayTablaConTooltips(listaMedidasC);


            var num_trab_sust_A1 = '<%=num_trab_sust_A1%>';
            var periodo_contr_A1 = '<%=periodo_contr_A1%>';

            var num_trab_sust_A2 = '<%=num_trab_sust_A2%>';
            var periodo_contr_A2 = '<%=periodo_contr_A2%>';

            var num_trab_sust_B = '<%=num_trab_sust_B%>';
            var periodo_contr_B = '<%=periodo_contr_B%>';

            var num_trab_sust_C = '<%=num_trab_sust_C%>';
            var periodo_contr_C = '<%=periodo_contr_C%>';
            var pers_disc_ocup_C = '<%=pers_disc_ocup_C%>';

            var cuant_econ_anual = '<%=cuant_econ_anual%>';
            var imp_anual_total = '<%=imp_anual_total%>';
            var contratacion_total = '<%=contratacion_total%>';


            $(document).ready(function () {

                //A) CONTRATO MERCANTIL
                if (listaMedidasA1.length > 0) {
                    //A.1 Centro especial empleo
                    $('#cb_contrato_mercantil').prop("checked", true);
                    $('#cb_centro_especial_empleo').prop("checked", true);
                }
                if (listaMedidasA2.length > 0) {
                    //A.2 Persona aut�noma discapacitada
                    $('#cb_contrato_mercantil').prop("checked", true);
                    $('#cb_pers_autonoma_disc').prop("checked", true);
                }

                if (listaMedidasB.length > 0) {
                    //B) DONACI�N FUNDACI�N
                    $('#cb_donacion_fundacion').prop("checked", true);
                }
                if (listaMedidasC.length > 0) {
                    //C) ENCLAVE LABORAL
                    $('#cb_enclave_laboral').prop("checked", true);
                }

                //check box
                check_todos();

                //A) Contrato Mercantil
                function check_contrato_mercantil() {
                    if ($('#cb_contrato_mercantil').prop('checked')) {
                        $('#div_contrato_mercantil').show();
                    } else {
                        $('#div_contrato_mercantil').hide();
                    }
                    check_centro_especial_empleo();
                    check_pers_autonoma_disc();
                }
                $('#cb_contrato_mercantil').on('click', function () {
                    check_contrato_mercantil();
                });

                //A.1) Centro especial empleo
                function check_centro_especial_empleo() {
                    if ($('#cb_centro_especial_empleo').prop('checked')) {
                        $('#div_A1').show();
                    } else {
                        $('#div_A1').hide();
                    }
                }
                $('#cb_centro_especial_empleo').on('click', function () {
                    check_centro_especial_empleo();
                });

                //A.2) Persona aut�noma discapacitada
                function check_pers_autonoma_disc() {
                    if ($('#cb_pers_autonoma_disc').prop('checked')) {
                        $('#div_A2').show();
                    } else {
                        $('#div_A2').hide();
                    }
                }
                $('#cb_pers_autonoma_disc').on('click', function () {
                    check_pers_autonoma_disc();
                });

                //B) Donaci�n Fundaci�n
                function check_donacion_fundacion() {
                    if ($('#cb_donacion_fundacion').prop('checked')) {
                        $('#div_donacion_fundacion').show();
                    } else {
                        $('#div_donacion_fundacion').hide();
                    }
                }
                $('#cb_donacion_fundacion').on('click', function () {
                    check_donacion_fundacion();
                });

                //C) Enclave Laboral
                function check_enclave_laboral() {
                    if ($('#cb_enclave_laboral').prop('checked')) {
                        $('#div_enclave_laboral').show();
                    } else {
                        $('#div_enclave_laboral').hide();
                    }
                }
                $('#cb_enclave_laboral').on('click', function () {
                    check_enclave_laboral();
                });


                function check_todos() {
                    check_contrato_mercantil();
                    check_centro_especial_empleo();
                    check_pers_autonoma_disc();
                    check_donacion_fundacion();
                    check_enclave_laboral();
                }

                //Campos suplementarios
                $('#num_trab_sust_A1').val(num_trab_sust_A1);
                $('#periodo_contr_A1').val(periodo_contr_A1);

                $('#num_trab_sust_A2').val(num_trab_sust_A2);
                $('#periodo_contr_A2').val(periodo_contr_A2);

                $('#num_trab_sust_B').val(num_trab_sust_B);
                $('#periodo_contr_B').val(periodo_contr_B);

                $('#num_trab_sust_C').val(num_trab_sust_C);
                $('#periodo_contr_C').val(periodo_contr_C);
                $('#pers_disc_ocup_C').val(pers_disc_ocup_C);

                $('#cuantificacion_econ').val(cuant_econ_anual.replace('.', ','));
                $('#imp_anual_total').val(imp_anual_total.replace('.', ','));
                $('#contratacion_total').val(contratacion_total);

            });


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


            function guardarDatos() {

                if (validarDatos()) {
                    var ajax = getXMLHttpRequest();
                    var nodos = null;
                    var CONTEXT_PATH = '<%=request.getContextPath()%>';
                    var url = CONTEXT_PATH + "/PeticionModuloIntegracion.do";

                    var parametros = "tarea=preparar&modulo=MELANBIDE72&operacion=guardarSuplementarios&tipo=0"
                            + "&numExpediente=<%=numExpediente%>"
                            //campos suplementarios A1
                            + "&num_trab_sust_A1=" + document.getElementById('num_trab_sust_A1').value
                            + "&periodo_contr_A1=" + document.getElementById('periodo_contr_A1').value
                            //campos suplementarios A2
                            + "&num_trab_sust_A2=" + document.getElementById('num_trab_sust_A2').value
                            + "&periodo_contr_A2=" + document.getElementById('periodo_contr_A2').value
                            //campos suplementarios B
                            + "&num_trab_sust_B=" + document.getElementById('num_trab_sust_B').value
                            + "&periodo_contr_B=" + document.getElementById('periodo_contr_B').value
                            //campos suplementarios C
                            + "&num_trab_sust_C=" + document.getElementById('num_trab_sust_C').value
                            + "&periodo_contr_C=" + document.getElementById('periodo_contr_C').value
                            + "&pers_disc_ocup_C=" + document.getElementById('pers_disc_ocup_C').value
                            //campos suplementarios generales
                            + "&cuantificacion_econ=" + document.getElementById('cuantificacion_econ').value
                            + "&imp_anual_total=" + document.getElementById('imp_anual_total').value
                            + "&contratacion_total=" + document.getElementById('contratacion_total').value
                            ;

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
                        var j;

                        for (j = 0; hijos != null && j < hijos.length; j++) {
                            if (hijos[j].nodeName == "CODIGO_OPERACION") {
                                codigoOperacion = hijos[j].childNodes[0].nodeValue;
                            }
                        }

                        if (codigoOperacion == "0") {
                            jsp_alerta("A", '<%=meLanbide72I18n.getMensaje(idiomaUsuario,"msg.correcto")%>');
                        } else if (codigoOperacion == "1") {
                            jsp_alerta("A", '<%=meLanbide72I18n.getMensaje(idiomaUsuario,"error.errorBD")%>');
                        } else if (codigoOperacion == "2") {
                            jsp_alerta("A", '<%=meLanbide72I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                        } else if (codigoOperacion == "3") {
                            jsp_alerta("A", '<%=meLanbide72I18n.getMensaje(idiomaUsuario,"error.pasoParametros")%>');
                        } else {
                            jsp_alerta("A", '<%=meLanbide72I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                        }
                    } catch (Err) {
                    }//try-catch
                } else {
                    jsp_alerta("A", mensajeValidacion);
                }

            }

            function validarNumericoDecimalPrecision(numero, longTotal, longParteDecimal) {
                try {
                    var longParteEntera = parseInt(longTotal) - parseInt(longParteDecimal);
                    if (Trim(numero) != '') {
                        var valor = numero;
                        var pattern = '^[0-9]{1,' + longParteEntera + '}(,[0-9]{1,' + longParteDecimal + '})?$';
                        var regex = new RegExp(pattern);
                        var result = regex.test(valor);
                        return result;
                    } else {
                        return true;
                    }
                } catch (err) {
                    alert(err);
                    return false;
                }
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

            function validarDatos() {
                mensajeValidacion = "";
                var correcto = true;

                //campos suplemetarios A1
                if ($('#cb_centro_especial_empleo').prop('checked')) {
                    if ($('#num_trab_sust_A1').val() == null || $('#num_trab_sust_A1').val() == '') {
                        mensajeValidacion = '<%=meLanbide72I18n.getMensaje(idiomaUsuario, "msg.numTrabSustituyeObligatorio")%>';
                        return false;
                    } else {
                        if (Trim($('#num_trab_sust_A1').val()) != '') {
                            if (/^([0-9])*$/.test($('#num_trab_sust_A1').val())) {
                            } else {
                                mensajeValidacion = '<%=meLanbide72I18n.getMensaje(idiomaUsuario, "msg.numTrabSustituye.errNumerico")%>';
                                return false;
                            }
                        }
                    }
                    if ($('#periodo_contr_A1').val() == null || $('#periodo_contr_A1').val() == '') {
                        mensajeValidacion = '<%=meLanbide72I18n.getMensaje(idiomaUsuario, "msg.periodoAniosObligatorio")%>';
                        return false;
                    } else {
                        if (Trim($('#periodo_contr_A1').val()) != '') {
                            if (/^([0-9])*$/.test($('#periodo_contr_A1').val())) {
                            } else {
                                mensajeValidacion = '<%=meLanbide72I18n.getMensaje(idiomaUsuario, "msg.periodoAnios.errNumerico")%>';
                                return false;
                            }
                        }
                    }
                }

                //campos suplemetarios A2
                if ($('#cb_pers_autonoma_disc').prop('checked')) {
                    if ($('#num_trab_sust_A2').val() == null || $('#num_trab_sust_A2').val() == '') {
                        mensajeValidacion = '<%=meLanbide72I18n.getMensaje(idiomaUsuario, "msg.numTrabSustituyeObligatorio")%>';
                        return false;
                    } else {
                        if (Trim($('#num_trab_sust_A2').val()) != '') {
                            if (/^([0-9])*$/.test($('#num_trab_sust_A2').val())) {
                            } else {
                                mensajeValidacion = '<%=meLanbide72I18n.getMensaje(idiomaUsuario, "msg.numTrabSustituye.errNumerico")%>';
                                return false;
                            }
                        }
                    }
                    if ($('#periodo_contr_A2').val() == null || $('#periodo_contr_A2').val() == '') {
                        mensajeValidacion = '<%=meLanbide72I18n.getMensaje(idiomaUsuario, "msg.periodoAniosObligatorio")%>';
                        return false;
                    } else {
                        if (Trim($('#periodo_contr_A2').val()) != '') {
                            if (/^([0-9])*$/.test($('#periodo_contr_A2').val())) {
                            } else {
                                mensajeValidacion = '<%=meLanbide72I18n.getMensaje(idiomaUsuario, "msg.periodoAnios.errNumerico")%>';
                                return false;
                            }
                        }
                    }
                }

                //campos suplemetarios B
                if ($('#cb_donacion_fundacion').prop('checked')) {
                    if ($('#num_trab_sust_B').val() == null || $('#num_trab_sust_B').val() == '') {
                        mensajeValidacion = '<%=meLanbide72I18n.getMensaje(idiomaUsuario, "msg.numTrabSustituyeObligatorio")%>';
                        return false;
                    } else {
                        if (Trim($('#num_trab_sust_B').val()) != '') {
                            if (/^([0-9])*$/.test($('#num_trab_sust_B').val())) {
                            } else {
                                mensajeValidacion = '<%=meLanbide72I18n.getMensaje(idiomaUsuario, "msg.numTrabSustituye.errNumerico")%>';
                                return false;
                            }
                        }
                    }
                    if ($('#periodo_contr_B').val() == null || $('#periodo_contr_B').val() == '') {
                        mensajeValidacion = '<%=meLanbide72I18n.getMensaje(idiomaUsuario, "msg.periodoAniosObligatorio")%>';
                        return false;
                    } else {
                        if (Trim($('#periodo_contr_B').val()) != '') {
                            if (/^([0-9])*$/.test($('#periodo_contr_B').val())) {
                            } else {
                                mensajeValidacion = '<%=meLanbide72I18n.getMensaje(idiomaUsuario, "msg.periodoAnios.errNumerico")%>';
                                return false;
                            }
                        }
                    }
                }

                //campos suplemetarios C
                if ($('#cb_enclave_laboral').prop('checked')) {
                    if ($('#num_trab_sust_C').val() == null || $('#num_trab_sust_C').val() == '') {
                        mensajeValidacion = '<%=meLanbide72I18n.getMensaje(idiomaUsuario, "msg.numTrabSustituyeObligatorio")%>';
                        return false;
                    } else {
                        if (Trim($('#num_trab_sust_C').val()) != '') {
                            if (/^([0-9])*$/.test($('#num_trab_sust_C').val())) {
                            } else {
                                mensajeValidacion = '<%=meLanbide72I18n.getMensaje(idiomaUsuario, "msg.numTrabSustituye.errNumerico")%>';
                                return false;
                            }
                        }
                    }
                    if ($('#periodo_contr_C').val() == null || $('#periodo_contr_C').val() == '') {
                        mensajeValidacion = '<%=meLanbide72I18n.getMensaje(idiomaUsuario, "msg.periodoAniosObligatorio")%>';
                        return false;
                    } else {
                        if (Trim($('#periodo_contr_C').val()) != '') {
                            if (/^([0-9])*$/.test($('#periodo_contr_C').val())) {
                            } else {
                                mensajeValidacion = '<%=meLanbide72I18n.getMensaje(idiomaUsuario, "msg.periodoAnios.errNumerico")%>';
                                return false;
                            }
                        }
                    }
                    if ($('#pers_disc_ocup_C').val() == null || $('#pers_disc_ocup_C').val() == '') {
                    } else {
                        if (Trim($('#pers_disc_ocup_C').val()) != '') {
                            if (/^([0-9])*$/.test($('#pers_disc_ocup_C').val())) {
                            } else {
                                mensajeValidacion = '<%=meLanbide72I18n.getMensaje(idiomaUsuario, "msg.personasDiscaOcupa.errNumerico")%>';
                                return false;
                            }
                        }
                    }
                }

                //campos suplementarios generales
                if ($('#cuantificacion_econ').val() == null || $('#cuantificacion_econ').val() == '') {
                } else {
                    if (!validarNumericoDecimalPrecision($('#cuantificacion_econ').val(), 9, 2)) {
                        mensajeValidacion = '<%=meLanbide72I18n.getMensaje(idiomaUsuario, "msg.importeAnual.errNumerico")%>';
                        return false;
                    }
                }
                if ($('#imp_anual_total').val() == null || $('#imp_anual_total').val() == '') {
                } else {
                    if (!validarNumericoDecimalPrecision($('#imp_anual_total').val(), 9, 2)) {
                        mensajeValidacion = '<%=meLanbide72I18n.getMensaje(idiomaUsuario, "msg.importeAnual.errNumerico")%>';
                        return false;
                    }
                }
                if ($('#contratacion_total').val() == null || $('#contratacion_total').val() == '') {
                } else {
                    if (Trim($('#contratacion_total').val()) != '') {
                        if (/^([0-9])*$/.test($('#contratacion_total').val())) {
                        } else {
                            mensajeValidacion = '<%=meLanbide72I18n.getMensaje(idiomaUsuario, "msg.personasDiscaOcupa.errNumerico")%>';
                            return false;
                        }
                    }
                }

                return correcto;
            }

        </script>     

        <div id="popupcalendar" class="text"></div>
    </body>
</html>



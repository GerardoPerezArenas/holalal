<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>

<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide58.i18n.MeLanbide58I18n" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.common.service.config.Config"%>
<%@page import="es.altia.common.service.config.ConfigServiceHelper"%>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide58.vo.SMIVO"%>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide58.vo.RojoVO"%>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide58.vo.AzulVO"%>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide58.util.ConfigurationParameter"%>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide58.util.ConstantesMeLanbide58"%>
<%@page import="java.util.Comparator" %>
<%@page import="java.util.ArrayList" %>
<%@page import="java.util.List" %>
<%@page import="java.text.DateFormat" %>
<%@page import="java.text.SimpleDateFormat" %>

<%
    int idiomaUsuario = 1;
    if(request.getParameter("idioma") != null) {
        try {
            idiomaUsuario = Integer.parseInt(request.getParameter("idioma"));
        } catch(Exception ex){}
    }	
    //nuevas
    UsuarioValueObject usuarioVO = new UsuarioValueObject();
    int apl = 5;
    String css = "";
    if (session.getAttribute("usuario") != null) {
            usuarioVO = (UsuarioValueObject) session.getAttribute("usuario");
            apl = usuarioVO.getAppCod();
            idiomaUsuario = usuarioVO.getIdioma();
            css = usuarioVO.getCss();
    }

    //Clase para internacionalizar los mensajes de la aplicaci�n.
    MeLanbide58I18n meLanbide58I18n = MeLanbide58I18n.getInstance();
    String numExpediente = (String)request.getAttribute("numExp");

    String smiMes = (String)request.getAttribute("smiMes");
    String smiDia = (String)request.getAttribute("smiDia");

    String importe = (String)request.getAttribute("importe");
    String importeConcedido = (String)request.getAttribute("importeConcedido");
   // String importeCopiaLanbide = (String)request.getAttribute("importeCopiaLanbide");    
 
%>
<jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
<jsp:setProperty name="descriptor"  property="idi_cod" value="<%=idiomaUsuario%>" />
<jsp:setProperty name="descriptor"  property="apl_cod" value="<%=apl%>" />
<link rel="StyleSheet" media="screen" type="text/css" href="<%=request.getContextPath()%><%=css%>">
<script type="text/javascript">
    var APP_CONTEXT_PATH = '<%=request.getContextPath()%>';
    var baseUrl = APP_CONTEXT_PATH + '/PeticionModuloIntegracion.do';
    function pulsarNuevoSMI() {
        var control = new Date();
        lanzarPopUpModal('<%=request.getContextPath()%>/PeticionModuloIntegracion.do?tarea=preparar&modulo=MELANBIDE58&operacion=cargarNuevoSMI&tipo=0&smiDia=<%=smiDia%>&numExp=<%=numExpediente%>&nuevo=1&control=' + control.getTime(), 700, 920, 'no', 'no', function (result) {
            if (result != undefined) {
                if (result[0] == '0') {
                    recargarTablaSMI(result);
                }
            }
        });
    }

    function pulsarModificarSMI() {
        var apellidosSMI = "";
        if (document.getElementById('apellidosSMI').value == null || document.getElementById('apellidosSMI').value == '') {
            apellidosSMI = "";
        } else {
            apellidosSMI = document.getElementById('apellidosSMI').value.replace(/\'/g, "''");
        }
        if (tablaSMI.selectedIndex != -1) {
            var control = new Date();
            lanzarPopUpModal('<%=request.getContextPath()%>/PeticionModuloIntegracion.do?tarea=preparar&modulo=MELANBIDE58&operacion=cargarModificarSMI&tipo=0&nuevo=0&smiDia=<%=smiDia%>&apellidosSMI=' + escape(apellidosSMI) + '&numLineaSMI=' + document.getElementById('numLineaSMI').value + '&numExp=<%=numExpediente%>&id=' + listaSMI[tablaSMI.selectedIndex][0] + '&control=' + control.getTime(), 700, 920, 'no', 'no', function (result) {
                if (result != undefined) {
                    if (result[0] == '0') {
                        recargarTablaSMI(result);
                    }
                }
            });

        } else {
            jsp_alerta('A', '<%=meLanbide58I18n.getMensaje(idiomaUsuario, "msg.msjNoSelecFila")%>');
        }
    }

    function pulsarExportarSMI() {
        var apellidosSMI = "";
        var nif = "";
        var control = new Date();
        if (document.getElementById('apellidosSMI').value == null || document.getElementById('apellidosSMI').value == '') {
            apellidosSMI = "";
        } else {
            apellidosSMI = document.getElementById('apellidosSMI').value.replace(/\'/g, "''");
        }
        if (document.getElementById('nif').value == null || document.getElementById('nif').value == '') {
            nif = "";
        } else {
            nif = document.getElementById('nif').value.toUpperCase();
        }
        var parametros = "";
        parametros = '?tarea=preparar&modulo=MELANBIDE58&operacion=generarExcelAnexoB&tipo=0&numExp=<%=numExpediente%>&apellidosSMI=' + escape(apellidosSMI) + '&numLineaSMI=' + document.getElementById('numLineaSMI').value + '&nif=' + nif + '&control=' + control.getTime();
        window.open(baseUrl + parametros, "_blank");
    }

    function pulsarCopiaImportes() {
        var ajax = getXMLHttpRequest();
        var nodos = null;
        var parametros = "";
        var control = new Date();
        parametros = 'tarea=preparar&modulo=MELANBIDE58&operacion=copiaImportesSMI&tipo=0&numExp=<%=numExpediente%>&control=' + control.getTime();
        try {
            ajax.open("POST", baseUrl, false);
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
            var listaSMINueva = extraerListaSMI(nodos);
            var codigoOperacion = listaSMINueva[0];
            if (codigoOperacion == "0") {
                recargarTablaSMI(listaSMINueva);
            } else if (codigoOperacion == "1") {
                jsp_alerta("A", '<%=meLanbide58I18n.getMensaje(idiomaUsuario,"error.errorBD")%>');
            } else {
                jsp_alerta("A", '<%=meLanbide58I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');

            }//if(
        } catch (Err) {
            jsp_alerta("A", '<%=meLanbide58I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');

        }//try-catch
    }

    function pulsarEliminarSMI() {
        if (tablaSMI.selectedIndex != -1) {
            var resultado = jsp_alerta_Mediana('', '<%=meLanbide58I18n.getMensaje(idiomaUsuario, "msg.preguntaEliminarSMI")%>');

            if (resultado == 1) {
                var ajax = getXMLHttpRequest();
                var nodos = null;
                var parametros = "";
                var control = new Date();
                parametros = 'tarea=preparar&modulo=MELANBIDE58&operacion=eliminarSMI&tipo=0&numExp=<%=numExpediente%>&id=' + listaSMI[tablaSMI.selectedIndex][0] + '&control=' + control.getTime();
                try {
                    ajax.open("POST", baseUrl, false);
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
                    var listaSMINueva = extraerListaSMI(nodos);
                    var codigoOperacion = listaSMINueva[0];
                    if (codigoOperacion == "0") {
                        recargarTablaSMI(listaSMINueva);
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

    function pulsarLimpiar() {
        document.getElementById("numLineaSMI").value = "";
        document.getElementById("apellidosSMI").value = "";
        pulsarBuscarSMI();
    }

    function pulsarBuscarSMI() {
        mensajeValidacion = "";
        if (validarNumericoVacio(document.getElementById('numLineaSMI').value)) {
            if (validarTresCaracteresApellido(document.getElementById('apellidosSMI').value)) {
                var ajax = getXMLHttpRequest();
                var nodos = null;
                var parametros = "";
                var control = new Date();
                var apellidosSMI = "";
                var nif = "";
                if (document.getElementById('apellidosSMI').value == null || document.getElementById('apellidosSMI').value == '') {
                    apellidosSMI = "";
                } else {
                    apellidosSMI = document.getElementById('apellidosSMI').value.replace(/\'/g, "''");
                }
                if (document.getElementById('nif').value == null || document.getElementById('nif').value == '') {
                    nif = "";
                } else {
                    nif = document.getElementById('nif').value.toUpperCase();
                }
                parametros = 'tarea=preparar&modulo=MELANBIDE58&operacion=buscarSMI&tipo=0&numExp=<%=numExpediente%>&apellidosSMI=' + escape(apellidosSMI) + '&numLineaSMI=' + document.getElementById('numLineaSMI').value + '&nif=' + nif + '&control=' + control.getTime();

                try {
                    ajax.open("POST", baseUrl, false);
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
                    var listaSMINueva = extraerListaSMI(nodos);
                    var codigoOperacion = listaSMINueva[0];
                    if (codigoOperacion == "0") {
                        recargarTablaSMI(listaSMINueva);
                    } else if (codigoOperacion == "1") {
                        jsp_alerta("A", '<%=meLanbide58I18n.getMensaje(idiomaUsuario,"msg.vaciaBD")%>');
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
            } else {
                mensajeValidacion = '<%=meLanbide58I18n.getMensaje(idiomaUsuario, "msg.apellidos.errLongitud")%>';
                jsp_alerta("A", mensajeValidacion);
            }
        } else {
            mensajeValidacion = '<%=meLanbide58I18n.getMensaje(idiomaUsuario, "msg.numLinea.errNumerico")%>';
            jsp_alerta("A", mensajeValidacion);
        }
    }

    function pulsarSubvImp() {

        mensajeValidacion = "";
        if (validarNumericoVacio(document.getElementById('numLineaSMI').value)) {
            var ajax = getXMLHttpRequest();
            var nodos = null;
            var parametros = "";
            var control = new Date();
            var apellidosSMI = "";

            if (document.getElementById('apellidosSMI').value == null || document.getElementById('apellidosSMI').value == '') {
                apellidosSMI = "";
            } else {
                apellidosSMI = document.getElementById('apellidosSMI').value.replace(/\'/g, "''");
            }

            parametros = 'tarea=preparar&modulo=MELANBIDE58&operacion=buscarSubImpInc&tipo=0&numExp=<%=numExpediente%>&apellidosSMI=' + escape(apellidosSMI) + '&numLineaSMI=' + document.getElementById('numLineaSMI').value + '&control=' + control.getTime();
            try {
                ajax.open("POST", baseUrl, false);
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
                var listaSMINueva = extraerListaSMI(nodos);
                var codigoOperacion = listaSMINueva[0];
                if (codigoOperacion == "0") {
                    recargarTablaSMI(listaSMINueva);
                } else if (codigoOperacion == "1") {
                    jsp_alerta("A", '<%=meLanbide58I18n.getMensaje(idiomaUsuario,"msg.vaciaBD")%>');
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
        } else {
            mensajeValidacion = '<%=meLanbide58I18n.getMensaje(idiomaUsuario, "msg.numLinea.errNumerico")%>';
            jsp_alerta("A", mensajeValidacion);
        }
    }

    function extraerListaSMI(nodos) {
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
                    } else if (hijosFila[cont].nodeName == "NUMDIASSININCIDENCIAS") {
                        if (hijosFila[cont].childNodes[0].nodeValue != "null") {
                            fila[4] = hijosFila[cont].childNodes[0].nodeValue;
                            var tex = fila[4].toString();
                            tex = tex.replace(".", ",");
                            fila[4] = tex;
                        } else {
                            fila[4] = '-';
                        }
                    } else if (hijosFila[cont].nodeName == "NUMDIASINCIDENCIA") {
                        if (hijosFila[cont].childNodes[0].nodeValue != "null") {
                            fila[5] = hijosFila[cont].childNodes[0].nodeValue;
                            var tex = fila[5].toString();
                            tex = tex.replace(".", ",");
                            fila[5] = tex;
                        } else {
                            fila[5] = '-';
                        }
                    } else if (hijosFila[cont].nodeName == "CAUSAINCIDENCIA") {
                        if (hijosFila[cont].childNodes[0].nodeValue != "null") {
                            fila[6] = hijosFila[cont].childNodes[0].nodeValue;
                        } else {
                            fila[6] = '-';
                        }
                    } else if (hijosFila[cont].nodeName == "FECHA") {
                        if (hijosFila[cont].childNodes.length > 0) {
                            fila[7] = hijosFila[cont].childNodes[0].nodeValue;
                        } else {
                            fila[7] = '-';
                        }
                    } else if (hijosFila[cont].nodeName == "PORCJORNADA") {
                        if (hijosFila[cont].childNodes[0].nodeValue != "null") {
                            fila[8] = hijosFila[cont].childNodes[0].nodeValue;
                            var tex = fila[8].toString();
                            tex = tex.replace(".", ",");
                            fila[8] = tex;
                        } else {
                            fila[8] = '-';
                        }
                    } else if (hijosFila[cont].nodeName == "PORCREDUCCION") {
                        if (hijosFila[cont].childNodes[0].nodeValue != "null") {
                            fila[9] = hijosFila[cont].childNodes[0].nodeValue;
                            var tex = fila[9].toString();
                            tex = tex.replace(".", ",");
                            fila[9] = tex;
                        } else {
                            fila[9] = '-';
                        }
                    } else if (hijosFila[cont].nodeName == "IMPORTESUBVENCION") {
                        if (hijosFila[cont].childNodes[0].nodeValue != "null") {
                            fila[10] = hijosFila[cont].childNodes[0].nodeValue;
                            var tex = fila[10].toString();
                            tex = tex.replace(".", ",");
                            fila[10] = tex;
                        } else {
                            fila[10] = '-';
                        }
                    } else if (hijosFila[cont].nodeName == "IMPORTERECALCULO") {
                        if (hijosFila[cont].childNodes[0].nodeValue != "null") {
                            fila[11] = hijosFila[cont].childNodes[0].nodeValue;
                            var tex = fila[11].toString();
                            tex = tex.replace(".", ",");
                            fila[11] = tex;
                        } else {
                            fila[11] = '-';
                        }
                    } else if (hijosFila[cont].nodeName == "OBSERVACIONES") {
                        if (hijosFila[cont].childNodes[0].nodeValue != "null") {
                            fila[12] = hijosFila[cont].childNodes[0].nodeValue;
                            var tex = fila[12].toString();
                            fila[12] = tex;
                        }
                    } else if (hijosFila[cont].nodeName == "NIF") {
                        if (hijosFila[cont].childNodes[0].nodeValue != "null") {
                            fila[13] = hijosFila[cont].childNodes[0].nodeValue;
                        } else {
                            fila[13] = '-';
                        }
                    }
                }
                listaNueva[j] = fila;
                fila = new Array();
            }
        }//for(j=0;hijos!=null && j<hijos.length;j++)
        return listaNueva;
    }

    function crearTabla() {
        tablaSMI = new Tabla(true, '<%=descriptor.getDescripcion("buscar")%>', '<%=descriptor.getDescripcion("anterior")%>', '<%=descriptor.getDescripcion("siguiente")%>', '<%=descriptor.getDescripcion("mosFilasPag")%>', '<%=descriptor.getDescripcion("msgNoResultBusq")%>', '<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>', '<%=descriptor.getDescripcion("filtrDeTotal")%>', '<%=descriptor.getDescripcion("primero")%>', '<%=descriptor.getDescripcion("ultimo")%>', document.getElementById('listaSMI'));
        tablaSMI.addColumna('0', 'center', "<%=meLanbide58I18n.getMensaje(idiomaUsuario,"smi.tablaSMI.col1")%>");// 0 id
        tablaSMI.addColumna('0', 'center', "<%=meLanbide58I18n.getMensaje(idiomaUsuario,"smi.tablaSMI.col2")%>");// 1 num linea
        tablaSMI.addColumna('100', 'center', "<%=meLanbide58I18n.getMensaje(idiomaUsuario,"smi.tablaSMI.col13")%>");// 13 DNI
        tablaSMI.addColumna('172', 'left', "<%=meLanbide58I18n.getMensaje(idiomaUsuario,"smi.tablaSMI.col3")%>");// 3 + 4 ape + nombre
        tablaSMI.addColumna('64', 'center', "<%=meLanbide58I18n.getMensaje(idiomaUsuario,"smi.tablaSMI.col4")%>");// dias sin
        tablaSMI.addColumna('64', 'center', "<%=meLanbide58I18n.getMensaje(idiomaUsuario,"smi.tablaSMI.col5")%>");// dias incidencia
        tablaSMI.addColumna('70', 'center', "<%=meLanbide58I18n.getMensaje(idiomaUsuario,"smi.tablaSMI.col6")%>");// causa
        tablaSMI.addColumna('65', 'center', "<%=meLanbide58I18n.getMensaje(idiomaUsuario,"smi.tablaSMI.col7")%>");// fecha
        tablaSMI.addColumna('80', 'center', "<%=meLanbide58I18n.getMensaje(idiomaUsuario,"smi.tablaSMI.col8")%>");// % jornada
        tablaSMI.addColumna('75', 'center', "<%=meLanbide58I18n.getMensaje(idiomaUsuario,"smi.tablaSMI.col9")%>");// % bajo rendimiento
        tablaSMI.addColumna('75', 'center', "<%=meLanbide58I18n.getMensaje(idiomaUsuario,"smi.tablaSMI.col10")%>");// Solicitado
        tablaSMI.addColumna('75', 'center', "<%=meLanbide58I18n.getMensaje(idiomaUsuario,"smi.tablaSMI.col11")%>");// Concedido

        tablaSMI.displayCabecera = true;
        tablaSMI.height = '300';
    }

    function recargarTablaSMI(result) {
        var fila;
        var fila2;
        var importeSolicitado = 0;
        var importeConcedido = 0;
        var importeSubvTotalLanbide = 0;
        var guion = "-";
        listaSMI = new Array();
        listaSMITabla = new Array();
        listaSMITool = new Array();
        padresRojo = new Array();
        padresAzul = new Array();

        var numLinB = false;
        var numLin = "";
        var numLin2 = "";

        for (var i = 1; i < result.length; i++) {
            fila = result[i];
            if (fila[10].toString() !== guion) {
                importeSolicitado += parseFloat(fila[10].replace(",", "."));
            }
            if (fila[11].toString() !== guion) {
                importeConcedido += parseFloat(fila[11].replace(",", "."));
            }
        }

        for (var i = 1; i < result.length; i++) {
            fila = result[i];

                if (fila[10] != fila[11]) {
                    padresRojo[i] = true;

                    if (i > 1) {
                        if (fila[1] == null) {
                            numLin = "-";
                        } else {
                            numLin = fila[1];
                        }
                        if (numLin == "-") {
                            var cont = 1;
                            do {
                                fila2 = result[i - cont];

                                if (fila2[1] == null) {
                                    numLin2 = "-";
                                } else {
                                    numLin2 = fila2[1];
                                }

                                if (numLin2 != "-") {
                                    padresRojo[i - cont] = true;
                                    numLinB = false;
                                    break;
                                } else {
                                    numLinB = true;
                                }
                                cont += 1;
                            } while (numLinB);
                        }
                    }
                } else {
                    padresRojo[i] = false;
                }           
        }

        for (var i = 1; i < result.length; i++) {
            fila = result[i];
            //listaSMI[i - 1] = fila;//NO FUNCIONAie9
            listaSMI[i - 1] = [fila[0], fila[1], fila[13], fila[2], fila[3],
                fila[4], fila[5], fila[6], fila[7], fila[8], fila[9], fila[10], fila[11], fila[12]];

            var obs = fila[12];
            if (fila[12] != undefined) {
                obs = obs.replace(/nnn/g, "\n");
            }
            listaSMITool[i - 1] = ['', '', '', '', '', '', '', '', '', '', '', '', obs];

            //if(fila[10]!=fila[11] || padresRojo[i]==true){
            if (padresRojo[i]) {
                listaSMITabla[i - 1] = [fila[0], '<span style="color:red">' + fila[1] + "</span>", '<span style="color:red">' + fila[13] + "</span>", '<span style="color:red">' + fila[2] + ", " + fila[3] + "</span>",
                    '<span style="color:red">' + fila[4] + "</span>", '<span style="color:red">' + fila[5] + "</span>", '<span style="color:red">' + fila[6] + "</span>",
                    '<span style="color:red">' + fila[7] + "</span>", '<span style="color:red">' + fila[8] + "</span>", '<span style="color:red">' + fila[9] + "</span>",
                    '<span style="color:red">' + fila[10] + "</span>", '<span style="color:red">' + fila[11] + "</span>"];
            } else if (padresAzul[i]) {
                listaSMITabla[i - 1] = [fila[0], '<span style="color:blue">' + fila[1] + "</span>", '<span style="color:blue">' + fila[13] + "</span>", '<span style="color:blue">' + fila[2] + ", " + fila[3] + "</span>",
                    '<span style="color:blue">' + fila[4] + "</span>", '<span style="color:blue">' + fila[5] + "</span>", '<span style="color:blue">' + fila[6] + "</span>",
                    '<span style="color:blue">' + fila[7] + "</span>", '<span style="color:blue">' + fila[8] + "</span>", '<span style="color:blue">' + fila[9] + "</span>",
                    '<span style="color:blue">' + fila[10] + "</span>", '<span style="color:blue">' + fila[11] + "</span>"];
            } else {
                listaSMITabla[i - 1] = [fila[0], fila[1], fila[13], fila[2] + ", " + fila[3],
                    fila[4], fila[5], fila[6], fila[7], fila[8], fila[9], fila[10], fila[11]];
            }
        }

        document.getElementById('importe').value = importeSolicitado.toFixed(2).toString().replace(".", ",");
        document.getElementById('importeConcedido').value = importeConcedido.toFixed(2).toString().replace(".", ",");

        crearTabla();

        tablaSMI.lineas = listaSMITabla;
        tablaSMI.displayTablaConTooltips(listaSMITool);
    }
</script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/extension/melanbide58/melanbide58.css"/>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/validaciones.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/listaComboBox.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/popup.js"></script>
<div class="tab-page" id="tabPage354" style="height:520px; width: 100%;">
    <h2 class="tab" id="pestana354"><%=meLanbide58I18n.getMensaje(idiomaUsuario,"label.titulo.pestanaSMI")%></h2> 
    <script type="text/javascript">tp1.addTabPage(document.getElementById("tabPage354"));</script>
    <br/>
    <h2 class="legendAzul" id="pestanaAnexoB"><%=meLanbide58I18n.getMensaje(idiomaUsuario,"label.tituloPrincipalSMI")%></h2>
    <div> 
        <div>                
            <div ><!--style="height:8px; width: 875px;"-->
                <div ><!--style="height:8px; width: 200px; float: left"-->
                    <div ><!--style="width: 350px; float: left"-->
                        <fieldset id="datosSMI">
                            <legend align= "left"><strong><%=meLanbide58I18n.getMensaje(idiomaUsuario,"label.SMI")%></strong></legend>
                            <br/>
                            <div style="width: 200px; text-align: center;">
                                <div class="lineaFormulario">
                                    <div style="width: 120px; float: left;" class="etiqueta">
                                        <%=meLanbide58I18n.getMensaje(idiomaUsuario,"label.smiMensual")%>
                                    </div>
                                    <div style="width: 40px; float: left;">
                                        <div style="float: left;">
                                            <input id="smiMes" name="smiMes" type="text" size="9" class="inputTexto"style="text-align:right"  value="<%=smiMes.toString().replaceAll("\\.", ",")%> &euro;" disabled/>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div style="width: 200px; text-align: center;">
                                <div class="lineaFormulario">
                                    <div style="width: 120px; float: left;" class="etiqueta">
                                        &nbsp;&nbsp;&nbsp;<%=meLanbide58I18n.getMensaje(idiomaUsuario,"label.smiDiario")%>
                                    </div>
                                    <div style="width: 40px; float: left;">
                                        <div style="float: left;">
                                            <input id="smiDia" name="smiDia" type="text" size="9" class="inputTexto" style="text-align:right" value="<%=smiDia.toString().replaceAll("\\.", ",")%> &euro;" disabled/>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <br/>
                        </fieldset>
                    </div>
                </div>
            </div>
            <br/>
            <div>
                <div class="lineaFormulario">
                    <div style="width: 120px; float: left;" class="etiqueta">
                        <%=meLanbide58I18n.getMensaje(idiomaUsuario,"label.numLinea")%>
                    </div>
                    <div style="width: 120px; float: left;">
                        <div style="float: left;">
                            <input id="numLineaSMI" name="numLineaSMI" type="text" class="inputTexto" size="15" maxlength="5" value=""/>
                        </div>
                    </div>
                </div>
                <div class="lineaFormulario">
                    <div style="width: 70px; float: left; padding-left: 15px;" class="etiqueta">
                        <%=meLanbide58I18n.getMensaje(idiomaUsuario,"label.apellidos")%>
                    </div>
                    <div style="width: 120px; float: left;">
                        <div style="float: left;">
                            <input id="apellidosSMI" name="apellidosSMI" type="text" class="inputTexto" size="30" maxlength="50" value=""/>
                        </div>
                    </div>
                </div>
                <div class="botonera" style="text-align: right; padding-left: 20px;">
                    <input type="button" id="btnSubvImpInco" name="btnSubvImpInco" class="botonGeneral"  value="<%=meLanbide58I18n.getMensaje(idiomaUsuario, "btn.subvImpInco")%>" onclick="pulsarSubvImp();">
                    <input type="button" id="btnLimpiarSMI" name="btnLimpiarSMI" class="botonGeneral"  value="<%=meLanbide58I18n.getMensaje(idiomaUsuario, "btn.limpiar")%>" onclick="pulsarLimpiar();">
                    <input type="button" id="btnBuscarSMI" name="btnBuscarSMI" class="botonGeneral"  value="<%=meLanbide58I18n.getMensaje(idiomaUsuario, "btn.buscar")%>" onclick="pulsarBuscarSMI();">
                </div>
            </div>
            <br>
            <div id="divGeneral" >     
                <div id="listaSMI" align="center"></div>
            </div>
            <div ><!--style="height:8px; text-align: right;width: 875px;" -->
                <div class="lineaFormulario" style="margin-top: 15px; width: 100%" ><!--style="width: 250px; text-align: left;"-->
                    <div style="width: 81.40%; text-align: right; float: left;" class="etiqueta">
                        <%=meLanbide58I18n.getMensaje(idiomaUsuario,"label.totalSubvencion")%>
                    </div>
                    <div style="width: 7.97%; text-align: right; float: left;">
                        <div >
                            <input id="importe" style="text-align:right" name="importe" type="text" class="inputTexto" size="9" value="<%=importe.toString().replaceAll("\\.", ",")%> &euro;" disabled/>
                        </div>
                    </div>                                            
                    <div style="width: 10.63%; text-align: right; float: left;">
                        <div>
                            <input id="importeConcedido" style="text-align:right" name="importeConcedido" type="text" size="9" class="inputTexto" value="<%=importeConcedido.toString().replaceAll("\\.", ",")%> &euro;" disabled/>
                        </div>
                    </div>                     
                </div>
            </div>           
 
            <div class="botonera" style="text-align: center; margin-top: 15px">
                <input type="button" id="btnNuevoSMI" name="btnNuevoSMI" class="botonGeneral"  value="<%=meLanbide58I18n.getMensaje(idiomaUsuario, "btn.nuevo")%>" onclick="pulsarNuevoSMI();">
                <input type="button" id="btnModificarSMI" name="btnModificarSMI" class="botonGeneral" value="<%=meLanbide58I18n.getMensaje(idiomaUsuario, "btn.modificar")%>" onclick="pulsarModificarSMI();">
                <input type="button" id="btnEliminarSMI" name="btnEliminarSMI"   class="botonGeneral" value="<%=meLanbide58I18n.getMensaje(idiomaUsuario, "btn.eliminar")%>" onclick="pulsarEliminarSMI();">
                <input type="button" id="btnExportarSMI" name="btnExportarSMI"   class="botonMasLargo" value="<%=meLanbide58I18n.getMensaje(idiomaUsuario, "btn.Exportar")%>" onclick="pulsarExportarSMI();">
            </div>
        </div>
    </div> 
</div>
<script  type="text/javascript">
    //Tabla SMI
    var tablaSMI;
    var listaSMI = new Array();
    var listaSMITabla = new Array();
    var listaSMITool = new Array();

    crearTabla();
    <%       		
       SMIVO objectVO = null;
       SMIVO objectVOfila = null;
       SMIVO objectVOfila2 = null;
       List<SMIVO> List = null;            

       if(request.getAttribute("listaSMI")!=null){
           List = (List<SMIVO>)request.getAttribute("listaSMI");
       }

       ArrayList<RojoVO> rojoList = new ArrayList<RojoVO>();
       ArrayList<AzulVO> azulList = new ArrayList<AzulVO>();
            												
       if (List!= null && List.size() >0){ 
                
            for (int num = 0; num < List.size(); num++) {
                    
               objectVOfila = List.get(num);
               int idSMI =objectVOfila.getId(); 
               String ImporteSolicitado="";
               if(objectVOfila.getImporteSolicitado()!=null){
                   ImporteSolicitado=String.valueOf((objectVOfila.getImporteSolicitado().toString()).replace(".",","));
               }else{
                   ImporteSolicitado="-";
               }

               String ImporteConcedido="";
               if(objectVOfila.getImporteRecalculo()!=null){
                   ImporteConcedido=String.valueOf((objectVOfila.getImporteRecalculo().toString()).replace(".",","));
               }else{
                   ImporteConcedido="-";
               }

                  
                if(!ImporteSolicitado.equals(ImporteConcedido)){
                    idSMI =objectVOfila.getId(); 

                    RojoVO rojoObj=new RojoVO();
                    rojoObj.setIDSMI(idSMI);
                    rojoObj.setRojo(true);
                    rojoList.add(rojoObj);

                    if(num>0){
                        String numLineaPadre="";
                        if(objectVOfila.getNumLinea()!=null && !"".equals((new Integer(objectVOfila.getNumLinea()).toString()))){
                           numLineaPadre=(new Integer(objectVOfila.getNumLinea()).toString());  
                        }else{
                           numLineaPadre="-";
                        }
                        if(numLineaPadre.trim().equals("-")){
                            String numLinea="";
                            int cont=1;
                            boolean numLineaB=false;
                            do {  
                                objectVOfila2=List.get(num-cont);

                                if(objectVOfila2.getNumLinea()!=null && !"".equals((new Integer(objectVOfila2.getNumLinea()).toString()))){
                                    numLinea=(new Integer(objectVOfila2.getNumLinea()).toString());  
                                    numLineaB=false;
                                 } else{
                                    numLinea="-";
                                    numLineaB=true;
                                 }

                                if(!numLinea.trim().equals("-")){
                                     int cal=num-cont;

                                     RojoVO rojoObj2=new RojoVO();
                                     rojoObj2=rojoList.get(cal);
                                     rojoObj2.setIDSMI(objectVOfila2.getId());
                                     rojoObj2.setRojo(true);
                                     rojoList.set(cal,rojoObj2);                                        
                                     break;
                                }
                                cont+= 1;
                            } while (numLineaB); 
                        }  
                    }  
                } else {
                    idSMI =objectVOfila.getId(); 
                    RojoVO rojoObj=new RojoVO();
                    rojoObj.setIDSMI(idSMI);
                    rojoObj.setRojo(false);
                    rojoList.add(rojoObj);                    
                }
            } // for
           
                   
            int auxIDSMI2;
            boolean rojoo;

            for (int z = 1; z < rojoList.size(); z++) {
                 RojoVO rojoObject=new RojoVO();
                 rojoObject=rojoList.get(z);
                 if(rojoObject.getIDSMI()==21109){
                     rojoo=rojoObject.getRojo();
                 }
             }

             int auxIDSMI;
             int t2 = rojoList.size();
             for (int i = 1; i < t2; i++) {
                for (int j = t2- 1; j >= i; j--) {
                    RojoVO rojoObj1=new RojoVO();
                    RojoVO rojoObj2=new RojoVO();
                    rojoObj1=rojoList.get(j);
                    rojoObj2=rojoList.get(j-1);
                    if(rojoObj1.getIDSMI() < rojoObj2.getIDSMI()) {
                        rojoList.set(j,rojoObj2);
                        int pos=j-1;
                        rojoList.set(pos,rojoObj1);
                    }
                }
             }


           String barraSeparadoraDobleIdiomaDesple = ConfigurationParameter.getParameter(ConstantesMeLanbide58.BARRA_SEPARADORA_IDIOMA, ConstantesMeLanbide58.FICHERO_PROPIEDADES);
            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

           for (int indice=0;indice<List.size();indice++) {
               objectVO = List.get(indice);
               
               String fecFormat="";
               if(objectVO.getFecha()!=null){
                   fecFormat=dateFormat.format(objectVO.getFecha());                
               }else{
                   fecFormat="-";
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

               String NumDiasSinIncidencias="";
               if(objectVO.getNumDiasSinIncidencias()!=null){
                   NumDiasSinIncidencias=String.valueOf((objectVO.getNumDiasSinIncidencias().toString()).replace(".",","));
                
               }else{
                   NumDiasSinIncidencias="-";
               }// cadena = String.valueOf(valor); 

               String NumDiasIncidencia="";
               if(objectVO.getNumDiasIncidencia()!=null){
                   NumDiasIncidencia=String.valueOf((objectVO.getNumDiasIncidencia().toString()).replace(".",","));
                
               }else{
                   NumDiasIncidencia="-";
               }

               String DesCausaIncidencia="";
               if(objectVO.getDesCausaIncidencia()!=null){
                   String descripcion = objectVO.getDesCausaIncidencia();                  
                   String[] descripcionDobleIdioma =  (descripcion!=null?descripcion.split(barraSeparadoraDobleIdiomaDesple):null);
                   if(descripcionDobleIdioma!=null && descripcionDobleIdioma.length>1){
                       if(idiomaUsuario==4){
                           descripcion=descripcionDobleIdioma[1];
                       }else{
                           descripcion=descripcionDobleIdioma[0];
                       }
                   }
                   DesCausaIncidencia=descripcion;
               }else{
                   DesCausaIncidencia="-";
               }

               String ImporteSolicitado="";
               if(objectVO.getImporteSolicitado()!=null){
                   ImporteSolicitado=String.valueOf((objectVO.getImporteSolicitado().toString()).replace(".",","));
               }else{
                   ImporteSolicitado="-";
               }

               String ImporteConcedido="";
               if(objectVO.getImporteRecalculo()!=null){
                   ImporteConcedido=String.valueOf((objectVO.getImporteRecalculo().toString()).replace(".",","));
               }else{
                   ImporteConcedido="-";
               }
    
               String PorcJornada="";
               if(objectVO.getPorcJornada()!=null){
                   PorcJornada=String.valueOf((objectVO.getPorcJornada().toString()).replace(".",","));
               }else{
                   PorcJornada="-";
               }

               String PorcReduccion="";
               if(objectVO.getPorcReduccion()!=null){
                   PorcReduccion=String.valueOf((objectVO.getPorcReduccion().toString()).replace(".",","));
                
               }else{
                   PorcReduccion="-";
               }

               String id="";
               if(objectVO.getId()!=null){
                   id=String.valueOf((objectVO.getId().toString()).replace(".",","));
                
               }else{
                   id="";
               }
               String nif="";
               if(objectVO.getNif()!=null){
                   nif=objectVO.getNif();
               }else{
                   nif="-";
               }

               
              String idsmi="";
              boolean rojoBoolean=false;
              boolean azulBoolean=false;
                RojoVO rojoOb=new RojoVO();
                rojoOb=rojoList.get(indice);
                if(rojoOb.getIDSMI()!=null){
                     idsmi=String.valueOf((rojoOb.getIDSMI().toString()).replace(".",","));
                     rojoBoolean=rojoOb.getRojo();
                }else{
                     idsmi="";
                }               
    %>

    listaSMI[<%=indice%>] = ['<%=objectVO.getId()%>', '<%=numLinea%>', '<%=nif%>', "<%=apellidos%>" + ", " + "<%=nombre%>",
        '<%=NumDiasSinIncidencias%>', '<%=NumDiasIncidencia%>', '<%=DesCausaIncidencia%>', '<%=fecFormat%>',
        '<%=PorcJornada%>', '<%=PorcReduccion%>', '<%=ImporteSolicitado%>', '<%=ImporteConcedido%>'];
    var obs = '<%=(objectVO.getObservaciones())%>';
    obs = obs.replace(/nnn/g, "\n");
    listaSMITool[<%=indice%>] = ['', '', '', '', '', '', '', '', '', '', '', '', obs];


    if ('<%=idsmi%>' == '<%=id%>') {
        if (<%=rojoBoolean%>) {
            listaSMITabla[<%=indice%>] = ['<%=objectVO.getId()%>', "<span style='color:red'>" + '<%=numLinea%>' + "</span>", "<span style='color:red'>" + '<%=nif%>' + "</span>", "<span style='color:red'><%=apellidos%>" + ", " + "<%=nombre%>" + "</span>",
                "<span style='color:red'>" + '<%=NumDiasSinIncidencias%>' + "</span>", "<span style='color:red'>" + '<%=NumDiasIncidencia%>' + "</span>",
                "<span style='color:red'>" + '<%=DesCausaIncidencia%>' + "</span>", "<span style='color:red'>" + '<%=fecFormat%>' + "</span>",
                "<span style='color:red'>" + '<%=PorcJornada%>' + "</span>", "<span style='color:red'>" + '<%=PorcReduccion%>' + "</span>",
                "<span style='color:red'>" + '<%=ImporteSolicitado%>' + "</span>", "<span style='color:red'>" + '<%=ImporteConcedido%>' + "</span>"];

        } else if (<%=azulBoolean%>) {

            listaSMITabla[<%=indice%>] = ['<%=objectVO.getId()%>', "<span style='color:blue'>" + '<%=numLinea%>' + "</span>", "<span style='color:blue'>" + '<%=nif%>' + "</span>", "<span style='color:blue'><%=apellidos%>" + ", " + "<%=nombre%>" + "</span>",
                "<span style='color:blue'>" + '<%=NumDiasSinIncidencias%>' + "</span>", "<span style='color:blue'>" + '<%=NumDiasIncidencia%>' + "</span>",
                "<span style='color:blue'>" + '<%=DesCausaIncidencia%>' + "</span>", "<span style='color:blue'>" + '<%=fecFormat%>' + "</span>",
                "<span style='color:blue'>" + '<%=PorcJornada%>' + "</span>", "<span style='color:blue'>" + '<%=PorcReduccion%>' + "</span>",
                "<span style='color:blue'>" + '<%=ImporteSolicitado%>' + "</span>", "<span style='color:blue'>" + '<%=ImporteConcedido%>' + "</span>"];

        } else {
            listaSMITabla[<%=indice%>] = ['<%=objectVO.getId()%>', '<%=numLinea%>', '<%=nif%>', "<%=apellidos%>" + ", " + "<%=nombre%>",
                '<%=NumDiasSinIncidencias%>', '<%=NumDiasIncidencia%>', '<%=DesCausaIncidencia%>', '<%=fecFormat%>',
                '<%=PorcJornada%>', '<%=PorcReduccion%>', '<%=ImporteSolicitado%>', '<%=ImporteConcedido%>'];
        }
    } else {
        listaSMITabla[<%=indice%>] = ['<%=objectVO.getId()%>', '<%=numLinea%>', '<%=nif%>', "<%=apellidos%>" + ", " + "<%=nombre%>",
            '<%=NumDiasSinIncidencias%>', '<%=NumDiasIncidencia%>', '<%=DesCausaIncidencia%>', '<%=fecFormat%>',
            '<%=PorcJornada%>', '<%=PorcReduccion%>', '<%=ImporteSolicitado%>', '<%=ImporteConcedido%>'];
    }

    <%
           }// for
       }// if
    %>

    tablaSMI.lineas = listaSMITabla;
    tablaSMI.displayTablaConTooltips(listaSMITool);  
</script>
<div id="popupcalendar" class="text"></div>                

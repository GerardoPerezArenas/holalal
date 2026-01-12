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

    String smiMes = (String)request.getAttribute("smiMes");
    String smiDia = (String)request.getAttribute("smiDia");

    String importe = (String)request.getAttribute("importe");
    String importeCalculado = (String)request.getAttribute("importeCalculado");
    String importeCopiaLanbide = (String)request.getAttribute("importeCopiaLanbide");
    
 
%>
<jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
<jsp:setProperty name="descriptor"  property="idi_cod" value="<%=idioma%>" />
<jsp:setProperty name="descriptor"  property="apl_cod" value="<%=apl%>" />
<link rel="StyleSheet" media="screen" type="text/css" href="<%=request.getContextPath()%><%=css%>">
<script type="text/javascript">

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
                        // recargarDatosExpediente();
                    }
                }
            });

        } else {
            jsp_alerta('A', '<%=meLanbide58I18n.getMensaje(idiomaUsuario, "msg.msjNoSelecFila")%>');
        }
    }

    function pulsarExportarSMI() {
        var apellidosSMI = "";
        var control = new Date();
        if (document.getElementById('apellidosSMI').value == null || document.getElementById('apellidosSMI').value == '') {
            apellidosSMI = "";
        } else {
            apellidosSMI = document.getElementById('apellidosSMI').value.replace(/\'/g, "''");
        }
        var CONTEXT_PATH = '<%=request.getContextPath()%>'
        var url = CONTEXT_PATH + "/PeticionModuloIntegracion.do";
        var parametros = "";
        parametros = '?tarea=preparar&modulo=MELANBIDE58&operacion=generarExcellSMI&tipo=0&numExp=<%=numExpediente%>&apellidosSMI=' + escape(apellidosSMI) + '&numLineaSMI=' + document.getElementById('numLineaSMI').value + '&control=' + control.getTime();
        //alert("parametros: " + parametros);
        window.open(url + parametros, "_blank");
    }

    function pulsarCopiaImportes() {
        var ajax = getXMLHttpRequest();
        var nodos = null;
        var CONTEXT_PATH = '<%=request.getContextPath()%>'
        var url = CONTEXT_PATH + "/PeticionModuloIntegracion.do";
        var parametros = "";
        var control = new Date();
        parametros = 'tarea=preparar&modulo=MELANBIDE58&operacion=copiaImportesSMI&tipo=0&numExp=<%=numExpediente%>&control=' + control.getTime();
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
                var CONTEXT_PATH = '<%=request.getContextPath()%>'
                var url = CONTEXT_PATH + "/PeticionModuloIntegracion.do";
                var parametros = "";
                var control = new Date();
                parametros = 'tarea=preparar&modulo=MELANBIDE58&operacion=eliminarSMI&tipo=0&numExp=<%=numExpediente%>&id=' + listaSMI[tablaSMI.selectedIndex][0] + '&control=' + control.getTime();
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
                var CONTEXT_PATH = '<%=request.getContextPath()%>'
                var url = CONTEXT_PATH + "/PeticionModuloIntegracion.do";
                var parametros = "";
                var control = new Date();
                var apellidosSMI = "";

                if (document.getElementById('apellidosSMI').value == null || document.getElementById('apellidosSMI').value == '') {
                    apellidosSMI = "";
                } else {
                    apellidosSMI = document.getElementById('apellidosSMI').value.replace(/\'/g, "''");
                }

                parametros = 'tarea=preparar&modulo=MELANBIDE58&operacion=buscarSMI&tipo=0&numExp=<%=numExpediente%>&apellidosSMI=' + escape(apellidosSMI) + '&numLineaSMI=' + document.getElementById('numLineaSMI').value + '&control=' + control.getTime();

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
            var CONTEXT_PATH = '<%=request.getContextPath()%>'
            var url = CONTEXT_PATH + "/PeticionModuloIntegracion.do";
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
                    } else if (hijosFila[cont].nodeName == "IMPORTELANBIDE") {
                        if (hijosFila[cont].childNodes[0].nodeValue != "null") {
                            fila[12] = hijosFila[cont].childNodes[0].nodeValue;
                            var tex = fila[12].toString();
                            tex = tex.replace(".", ",");
                            fila[12] = tex;
                        } else {
                            fila[12] = '-';
                        }
                    } else if (hijosFila[cont].nodeName == "OBSERVACIONES") {
                        if (hijosFila[cont].childNodes[0].nodeValue != "null") {
                            fila[13] = hijosFila[cont].childNodes[0].nodeValue;
                            var tex = fila[13].toString();
                            fila[13] = tex;
                        }
                    } else if (hijosFila[cont].nodeName == "NIF") {
                        if (hijosFila[cont].childNodes[0].nodeValue != "null") {
                            fila[14] = hijosFila[cont].childNodes[0].nodeValue;
                        } else {
                            fila[14] = '-';
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
        tablaSMI.addColumna('0', 'center', "<%=meLanbide58I18n.getMensaje(idiomaUsuario,"smi.tablaSMI.col1")%>");
        tablaSMI.addColumna('0', 'center', "<%=meLanbide58I18n.getMensaje(idiomaUsuario,"smi.tablaSMI.col2")%>");
        tablaSMI.addColumna('100', 'center', "<%=meLanbide58I18n.getMensaje(idiomaUsuario,"smi.tablaSMI.col13")%>");
        tablaSMI.addColumna('172', 'center', "<%=meLanbide58I18n.getMensaje(idiomaUsuario,"smi.tablaSMI.col3")%>");
        tablaSMI.addColumna('64', 'center', "<%=meLanbide58I18n.getMensaje(idiomaUsuario,"smi.tablaSMI.col4")%>");
        tablaSMI.addColumna('64', 'center', "<%=meLanbide58I18n.getMensaje(idiomaUsuario,"smi.tablaSMI.col5")%>");
        tablaSMI.addColumna('70', 'center', "<%=meLanbide58I18n.getMensaje(idiomaUsuario,"smi.tablaSMI.col6")%>");
        tablaSMI.addColumna('65', 'center', "<%=meLanbide58I18n.getMensaje(idiomaUsuario,"smi.tablaSMI.col7")%>");
        tablaSMI.addColumna('80', 'center', "<%=meLanbide58I18n.getMensaje(idiomaUsuario,"smi.tablaSMI.col8")%>");
        tablaSMI.addColumna('100', 'center', "<%=meLanbide58I18n.getMensaje(idiomaUsuario,"smi.tablaSMI.col9")%>");
        tablaSMI.addColumna('75', 'center', "<%=meLanbide58I18n.getMensaje(idiomaUsuario,"smi.tablaSMI.col10")%>");
        tablaSMI.addColumna('100', 'center', "<%=meLanbide58I18n.getMensaje(idiomaUsuario,"smi.tablaSMI.col11")%>");
        tablaSMI.addColumna('100', 'center', "<%=meLanbide58I18n.getMensaje(idiomaUsuario,"smi.tablaSMI.col12")%>");


        tablaSMI.displayCabecera = true;
        tablaSMI.height = '300';


    }

    function recargarTablaSMI(result) {
        var fila;
        var fila2;
        var importeSubvTotal = 0;
        var importeSubvTotalCalculado = 0;
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
                importeSubvTotal += parseFloat(fila[10].replace(",", "."));
            }
            if (fila[11].toString() !== guion) {
                importeSubvTotalCalculado += parseFloat(fila[11].replace(",", "."));
            }
            if (fila[12].toString() !== guion) {
                importeSubvTotalLanbide += parseFloat(fila[12].replace(",", "."));
            }
        }

        for (var i = 1; i < result.length; i++) {
            fila = result[i];

            if (importeSubvTotalLanbide == "0") {
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
            } else {

                if (fila[11] != fila[12]) {

                    padresAzul[i] = true;

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
                                    padresAzul[i - cont] = true;
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

                    padresAzul[i] = false;
                }
            }

        }

        for (var i = 1; i < result.length; i++) {
            fila = result[i];
            //listaSMI[i - 1] = fila;//NO FUNCIONAie9
            listaSMI[i - 1] = [fila[0], fila[1], fila[14], fila[2], fila[3],
                fila[4], fila[5], fila[6], fila[7],  fila[8], fila[9], fila[10], fila[11], fila[12]];

            var obs = fila[13];
            if (fila[13] != undefined) {
                obs = obs.replace(/nnn/g, "\n");
            }
            listaSMITool[i - 1] = ['', '', '', '', '', '', '', '', '', '', '', '',  obs];

            //if(fila[10]!=fila[11] || padresRojo[i]==true){
            if (padresRojo[i]) {
                listaSMITabla[i - 1] = [fila[0], '<span style="color:red">' + fila[1] + "</span>", '<span style="color:red">' + fila[14] + "</span>", '<span style="color:red">' + fila[2] + ", " + fila[3] + "</span>",
                    '<span style="color:red">' + fila[4] + "</span>", '<span style="color:red">' + fila[5] + "</span>", '<span style="color:red">' + fila[6] + "</span>",
                    '<span style="color:red">' + fila[7] + "</span>", '<span style="color:red">' + fila[8] + "</span>", '<span style="color:red">' + fila[9] + "</span>",
                    '<span style="color:red">' + fila[10] + "</span>", '<span style="color:red">' + fila[11] + "</span>", '<span style="color:red">' + fila[12] + "</span>"];
            } else if (padresAzul[i]) {
                listaSMITabla[i - 1] = [fila[0], '<span style="color:blue">' + fila[1] + "</span>", '<span style="color:blue">' + fila[14] + "</span>", '<span style="color:blue">' + fila[2] + ", " + fila[3] + "</span>",
                    '<span style="color:blue">' + fila[4] + "</span>", '<span style="color:blue">' + fila[5] + "</span>", '<span style="color:blue">' + fila[6] + "</span>",
                    '<span style="color:blue">' + fila[7] + "</span>",'<span style="color:blue">' + fila[8] + "</span>", '<span style="color:blue">' + fila[9] + "</span>",
                    '<span style="color:blue">' + fila[10] + "</span>", '<span style="color:blue">' + fila[11] + "</span>", '<span style="color:blue">' + fila[12] + "</span>"];
            } else {
                listaSMITabla[i - 1] = [fila[0], fila[1], fila[14], fila[2] + ", " + fila[3],
                    fila[4], fila[5], fila[6], fila[7], fila[8], fila[9], fila[10], fila[11], fila[12]];
            }
        }

        document.getElementById('importe').value = importeSubvTotal.toFixed(2).toString().replace(".", ",");
        document.getElementById('importeCalculado').value = importeSubvTotalCalculado.toFixed(2).toString().replace(".", ",");
        document.getElementById('importeCopiaLanbide').value = importeSubvTotalLanbide.toFixed(2).toString().replace(".", ",");

        if (document.getElementById('importeCopiaLanbide').value != '0,00') {
            document.getElementById('btnSubvImpInco').style.display = 'none';
        }
        crearTabla();


        tablaSMI.lineas = listaSMITabla;
        tablaSMI.displayTablaConTooltips(listaSMITool);

        //tablaSMI.displayTabla();
        if (navigator.appName.indexOf("Internet Explorer") != -1) {
            try {
                var div = document.getElementById('listaSMI');
                div.children[0].children[0].children[0].children[1].style.width = '100%';
                div.children[0].children[1].style.width = '100%';
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

    function recargarDatosExpediente() {
        pleaseWait('on');
        document.forms[0].opcion.value = "cargarPestTram";
        document.forms[0].target = "mainFrame";
        document.forms[0].action = "<c:url value='/sge/FichaExpediente.do'/>";
        document.forms[0].submit();
    }

</script>

<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/extension/melanbide58/melanbide58.css"/>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/validaciones.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/listaComboBox.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/popup.js"></script>
<script type="text/javascript">
    var APP_CONTEXT_PATH = '<%=request.getContextPath()%>';
</script>
<!-- Eventos onKeyPress compatibilidad firefox  -->
<!--<script type="text/javascript" src="http://code.jquery.com/jquery-1.4.4.js"></script>-->
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
                        <fieldset>
                            <legend align= "left"></strong><%=meLanbide58I18n.getMensaje(idiomaUsuario,"label.SMI")%><strong></legend>
                            <br/>
                            <div style="width: 200px; text-align: center;">
                                <div class="lineaFormulario">
                                    <div style="width: 90px; float: left;" class="etiqueta">
                                        <%=meLanbide58I18n.getMensaje(idiomaUsuario,"label.smiMensual")%>
                                    </div>
                                    <div style="width: 40px; float: left;">
                                        <div style="float: left;">
                                            <input id="smiMes" name="smiMes" type="text" size="9" class="inputTexto" value="<%=smiMes.toString().replaceAll("\\.", ",")%>" disabled/>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div style="width: 200px; text-align: center;">
                                <div class="lineaFormulario">
                                    <div style="width: 90px; float: left;" class="etiqueta">
                                        &nbsp;&nbsp;&nbsp;<%=meLanbide58I18n.getMensaje(idiomaUsuario,"label.smiDiario")%>
                                    </div>
                                    <div style="width: 40px; float: left;">
                                        <div style="float: left;">
                                            <input id="smiDia" name="smiDia" type="text" size="9" class="inputTexto" value="<%=smiDia.toString().replaceAll("\\.", ",")%>" disabled/>
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
            <div ><!--style="height:8px; width: 875px;"-->
                <div class="lineaFormulario">
                    <div style="width: 120px; float: left;" class="etiqueta">
                        <%=meLanbide58I18n.getMensaje(idiomaUsuario,"label.numLinea")%>
                    </div>
                    <div style="width: 120px; float: left;">
                        <div style="float: left;">
                            <input id="numLineaSMI" name="numLineaSMI" type="text" class="inputTexto" size="15" maxlength="5" 
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
                            <input id="apellidosSMI" name="apellidosSMI" type="text" class="inputTexto" size="30" maxlength="50" 
                                   value=""/>
                        </div>
                    </div>
                </div>
                <div class="botonera" style="text-align: right;">
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
                    <div style="width: 70.77%; text-align: right; float: left;" class="etiqueta">
                        <%=meLanbide58I18n.getMensaje(idiomaUsuario,"label.totalSubvencion")%>
                    </div>

                    <div style="width: 7.97%; text-align: right; float: left;">
                        <div >
                            <input id="importe" style="text-align:right" name="importe" type="text" class="inputTexto" size="9" value="<%=importe.toString().replaceAll("\\.", ",")%>" disabled/>
                        </div>
                    </div>
                    <div style="width: 10.63%; text-align: right; float: left;">
                        <div>
                            <input id="importeCalculado" style="text-align:right" name="importeCalculado" type="text" size="9" class="inputTexto" value="<%=importeCalculado.toString().replaceAll("\\.", ",")%>" disabled/>
                        </div>
                    </div>    
                    <div style="width: 10.63%; text-align: right; float: left;">
                        <div >
                            <input id="importeCopiaLanbide" style="text-align:right" name="importeCopiaLanbide" type="text" class="inputTexto" size="9" value="<%=importeCopiaLanbide.toString().replaceAll("\\.", ",")%>" disabled/>
                        </div>
                    </div>    
                </div>
            </div>           
            <br/><br>
            <div class="botonera" style="text-align: center; margin-top: 15px">
                <input type="button" id="btnNuevoSMI" name="btnNuevoSMI" class="botonGeneral"  value="<%=meLanbide58I18n.getMensaje(idiomaUsuario, "btn.nuevo")%>" onclick="pulsarNuevoSMI();">
                <input type="button" id="btnModificarSMI" name="btnModificarSMI" class="botonGeneral" value="<%=meLanbide58I18n.getMensaje(idiomaUsuario, "btn.modificar")%>" onclick="pulsarModificarSMI();">
                <input type="button" id="btnEliminarSMI" name="btnEliminarSMI"   class="botonGeneral" value="<%=meLanbide58I18n.getMensaje(idiomaUsuario, "btn.eliminar")%>" onclick="pulsarEliminarSMI();">
                <input type="button" id="btnExportarSMI" name="btnExportarSMI"   class="botonMasLargo" value="<%=meLanbide58I18n.getMensaje(idiomaUsuario, "btn.Exportar")%>" onclick="pulsarExportarSMI();">
                <input type="button" id="btnCopiaImportes" name="btnCopiaImportes"   class="botonMasLargo" value="<%=meLanbide58I18n.getMensaje(idiomaUsuario, "btn.copia")%>" onclick="pulsarCopiaImportes();">
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

    if ('<%=importeCopiaLanbide%>' != '0') {
        document.getElementById('btnSubvImpInco').style.display = 'none';
    }

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
               String ImporteSubvencion="";
               if(objectVOfila.getImporteSubvencion()!=null){
                   ImporteSubvencion=String.valueOf((objectVOfila.getImporteSubvencion().toString()).replace(".",","));
               }else{
                   ImporteSubvencion="-";
               }

               String ImporteRecalculo="";
               if(objectVOfila.getImporteRecalculo()!=null){
                   ImporteRecalculo=String.valueOf((objectVOfila.getImporteRecalculo().toString()).replace(".",","));
               }else{
                   ImporteRecalculo="-";
               }

               String ImporteLanbide="";
               if(objectVOfila.getImporteLanbide()!=null){
                   ImporteLanbide=String.valueOf((objectVOfila.getImporteLanbide().toString()).replace(".",","));
               }else{
                   ImporteLanbide="-";
               }
                   
               if ("0".equals(importeCopiaLanbide)){
                   if(!ImporteSubvencion.equals(ImporteRecalculo)){


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
                               }else{
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
                   }else{

                       idSMI =objectVOfila.getId(); 
                       RojoVO rojoObj=new RojoVO();
                       rojoObj.setIDSMI(idSMI);
                       rojoObj.setRojo(false);
                       rojoList.add(rojoObj);
                       //RojoVO rojoObjNew=new RojoVO();
                       //rojoObjNew=rojoList.get(rojoList.size()-1);                            
                   }
               } else {


                   if(!ImporteLanbide.equals(ImporteRecalculo)){
                            
                       idSMI =objectVOfila.getId(); 

                       AzulVO azulObj=new AzulVO();
                       azulObj.setIDSMI(idSMI);
                       azulObj.setAzul(true);
                       azulList.add(azulObj);
                            

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
                               }else{
                                   numLinea="-";
                                   numLineaB=true;
                               }

                               if(!numLinea.trim().equals("-")){
                                   int cal=num-cont;

                                   AzulVO azulObj2=new AzulVO();
                                   azulObj2=azulList.get(cal);
                                   azulObj2.setIDSMI(objectVOfila2.getId());
                                   azulObj2.setAzul(true);
                                   azulList.set(cal,azulObj2);
                                        
                                   break;
                               }
                               cont+= 1;
                          } while (numLineaB); 
                         }  
                       }  
                   }else{

                       idSMI =objectVOfila.getId(); 
                       AzulVO azulObj=new AzulVO();
                       azulObj.setIDSMI(idSMI);
                       azulObj.setAzul(false);
                       azulList.add(azulObj);
                       //AzulVO azulObj=new AzulVO();
                       //azulObjNew=azulList.get(azulList.size()-1);                                    
                   }
               }
           }
                   
           if ("0".equals(importeCopiaLanbide)){
               int auxIDSMI2;
               boolean rojoo;

               for (int z = 1; z < rojoList.size(); z++) 
                {
                    RojoVO rojoObject=new RojoVO();
                    rojoObject=rojoList.get(z);
                    if(rojoObject.getIDSMI()==21109){
                        rojoo=rojoObject.getRojo();
                    }

                }

                int auxIDSMI;
                int t2 = rojoList.size();
                for (int i = 1; i < t2; i++) 
                {
                            for (int j = t2- 1; j >= i; j--) 
                            {
                                    RojoVO rojoObj1=new RojoVO();
                                    RojoVO rojoObj2=new RojoVO();
                                    rojoObj1=rojoList.get(j);
                                    rojoObj2=rojoList.get(j-1);
                                    if(rojoObj1.getIDSMI() < rojoObj2.getIDSMI())
                                    {
                                            rojoList.set(j,rojoObj2);
                                            int pos=j-1;
                                            rojoList.set(pos,rojoObj1);

                                    }
                            }
                }
           } else {
                    
               int auxIDSMI2;
               boolean azulo;

               for (int z = 1; z < azulList.size(); z++) 
                {
                    AzulVO azulObject=new AzulVO();
                    azulObject=azulList.get(z);
                    if(azulObject.getIDSMI()==21109){
                        azulo=azulObject.getAzul();                             
                    }
                }

                int auxIDSMI;
                int t2 = azulList.size();
                for (int i = 1; i < t2; i++) 
                {
                    for (int j = t2- 1; j >= i; j--) 
                    {
                        AzulVO azulObj1=new AzulVO();
                        AzulVO azulObj2=new AzulVO();
                        azulObj1=azulList.get(j);
                        azulObj2=azulList.get(j-1);
                        if(azulObj1.getIDSMI() < azulObj2.getIDSMI())                                        {

                                azulList.set(j,azulObj2);
                                int pos=j-1;
                                azulList.set(pos,azulObj1);

                        }
                    }
                }
           }                  

           String barraSeparadoraDobleIdiomaDesple = ConfigurationParameter.getParameter(ConstantesMeLanbide58.BARRA_SEPARADORA_IDIOMA, ConstantesMeLanbide58.FICHERO_PROPIEDADES);
           for (int indice=0;indice<List.size();indice++)
           {
               objectVO = List.get(indice);
               DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
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
                   //String barraSeparadoraDobleIdiomaDesple = ConfigurationParameter.getParameter(ConstantesMeLanbide58.BARRA_SEPARADORA_IDIOMA_DESPLEGABLES, ConstantesMeLanbide58.FICHERO_PROPIEDADES);
                   String[] descripcionDobleIdioma =  (descripcion!=null?descripcion.split(barraSeparadoraDobleIdiomaDesple):null);
                   if(descripcionDobleIdioma!=null && descripcionDobleIdioma.length>1){
                       if(idiomaUsuario==4){
                           descripcion=descripcionDobleIdioma[1];
                       }else{
                           // Cogemos la primera posicion que deberia ser castellano
                           descripcion=descripcionDobleIdioma[0];
                       }
                   }
                   DesCausaIncidencia=descripcion;
               }else{
                   DesCausaIncidencia="-";
               }

               String ImporteSubvencion="";
               if(objectVO.getImporteSubvencion()!=null){
                   ImporteSubvencion=String.valueOf((objectVO.getImporteSubvencion().toString()).replace(".",","));
               }else{
                   ImporteSubvencion="-";
               }

               String ImporteRecalculo="";
               if(objectVO.getImporteRecalculo()!=null){
                   ImporteRecalculo=String.valueOf((objectVO.getImporteRecalculo().toString()).replace(".",","));
               }else{
                   ImporteRecalculo="-";
               }
                    
               String ImporteLanbide="";
               if(objectVO.getImporteLanbide()!=null){
                   ImporteLanbide=String.valueOf((objectVO.getImporteLanbide().toString()).replace(".",","));
               }else{
                   ImporteLanbide="-";
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
              if ("0".equals(importeCopiaLanbide)){
                   RojoVO rojoOb=new RojoVO();
                   rojoOb=rojoList.get(indice);
                   if(rojoOb.getIDSMI()!=null){
                        idsmi=String.valueOf((rojoOb.getIDSMI().toString()).replace(".",","));
                        rojoBoolean=rojoOb.getRojo();
                   }else{
                        idsmi="";
                   }
               } else {
                   AzulVO azulOb=new AzulVO();
                   azulOb=azulList.get(indice);
                   if(azulOb.getIDSMI()!=null){
                        idsmi=String.valueOf((azulOb.getIDSMI().toString()).replace(".",","));
                        azulBoolean=azulOb.getAzul();
                   }else{
                        idsmi="";
                   }
               }


    %>

    listaSMI[<%=indice%>] = ['<%=objectVO.getId()%>', '<%=numLinea%>', '<%=nif%>', "<%=apellidos%>" + ", " + "<%=nombre%>",
        '<%=NumDiasSinIncidencias%>', '<%=NumDiasIncidencia%>', '<%=DesCausaIncidencia%>', '<%=fecFormat%>',
        '<%=PorcJornada%>', '<%=PorcReduccion%>', '<%=ImporteSubvencion%>', '<%=ImporteRecalculo%>', '<%=ImporteLanbide%>'];
    var obs = '<%=(objectVO.getObservaciones())%>';
    obs = obs.replace(/nnn/g, "\n");
    listaSMITool[<%=indice%>] = ['', '', '', '', '', '', '', '', '', '', '', '', obs];


    if ('<%=idsmi%>' == '<%=id%>') {
        if (<%=rojoBoolean%>) {
            listaSMITabla[<%=indice%>] = ['<%=objectVO.getId()%>', "<span style='color:red'>" + '<%=numLinea%>' + "</span>", "<span style='color:red'>" + '<%=nif%>' + "</span>", "<span style='color:red'><%=apellidos%>" + ", " + "<%=nombre%>" + "</span>",
                "<span style='color:red'>" + '<%=NumDiasSinIncidencias%>' + "</span>", "<span style='color:red'>" + '<%=NumDiasIncidencia%>' + "</span>",
                "<span style='color:red'>" + '<%=DesCausaIncidencia%>' + "</span>", "<span style='color:red'>" + '<%=fecFormat%>' + "</span>",
                "<span style='color:red'>" + '<%=PorcJornada%>' + "</span>", "<span style='color:red'>" + '<%=PorcReduccion%>' + "</span>",
                "<span style='color:red'>" + '<%=ImporteSubvencion%>' + "</span>", "<span style='color:red'>" + '<%=ImporteRecalculo%>' + "</span>",
                "<span style='color:red'>" + '<%=ImporteLanbide%>' + "</span>"];

        } else if (<%=azulBoolean%>) {

            listaSMITabla[<%=indice%>] = ['<%=objectVO.getId()%>', "<span style='color:blue'>" + '<%=numLinea%>' + "</span>", "<span style='color:blue'>" + '<%=nif%>' + "</span>", "<span style='color:blue'><%=apellidos%>" + ", " + "<%=nombre%>" + "</span>",
                "<span style='color:blue'>" + '<%=NumDiasSinIncidencias%>' + "</span>", "<span style='color:blue'>" + '<%=NumDiasIncidencia%>' + "</span>",
                "<span style='color:blue'>" + '<%=DesCausaIncidencia%>' + "</span>", "<span style='color:blue'>" + '<%=fecFormat%>' + "</span>",
                "<span style='color:blue'>" + '<%=PorcJornada%>' + "</span>", "<span style='color:blue'>" + '<%=PorcReduccion%>' + "</span>",
                "<span style='color:blue'>" + '<%=ImporteSubvencion%>' + "</span>", "<span style='color:blue'>" + '<%=ImporteRecalculo%>' + "</span>",
                "<span style='color:blue'>" + '<%=ImporteLanbide%>' + "</span>"];

        } else {
            listaSMITabla[<%=indice%>] = ['<%=objectVO.getId()%>', '<%=numLinea%>', '<%=nif%>', "<%=apellidos%>" + ", " + "<%=nombre%>",
                '<%=NumDiasSinIncidencias%>', '<%=NumDiasIncidencia%>', '<%=DesCausaIncidencia%>', '<%=fecFormat%>', 
                '<%=PorcJornada%>', '<%=PorcReduccion%>', '<%=ImporteSubvencion%>', '<%=ImporteRecalculo%>', '<%=ImporteLanbide%>'];
        }
    } else {
        listaSMITabla[<%=indice%>] = ['<%=objectVO.getId()%>', '<%=numLinea%>', '<%=nif%>', "<%=apellidos%>" + ", " + "<%=nombre%>",
            '<%=NumDiasSinIncidencias%>', '<%=NumDiasIncidencia%>', '<%=DesCausaIncidencia%>', '<%=fecFormat%>', 
            '<%=PorcJornada%>', '<%=PorcReduccion%>', '<%=ImporteSubvencion%>', '<%=ImporteRecalculo%>', '<%=ImporteLanbide%>'];
    }

    <%
           }// for
       }// if
    %>

    tablaSMI.lineas = listaSMITabla;
    tablaSMI.displayTablaConTooltips(listaSMITool);
    if (navigator.appName.indexOf("Internet Explorer") != -1) {
        try {
            var div = document.getElementById('listaSMI');
            div.children[0].children[0].children[0].children[1].style.width = '100%';
            div.children[0].children[1].style.width = '100%';
        } catch (err) {

        }
    }

</script>
<div id="popupcalendar" class="text"></div>                

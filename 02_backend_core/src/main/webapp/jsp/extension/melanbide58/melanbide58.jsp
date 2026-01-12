<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>

<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide58.i18n.MeLanbide58I18n" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.common.service.config.Config"%>
<%@page import="es.altia.common.service.config.ConfigServiceHelper"%>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide58.vo.PlantillaVO"%>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide58.vo.DesplegableAdmonLocalVO" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide58.util.ConstantesMeLanbide58" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide58.util.ConfigurationParameter" %>
<%@page import="java.util.ArrayList" %>
<%@page import="java.util.List" %>
<%@page import="java.text.DateFormat" %>
<%@page import="java.text.SimpleDateFormat" %>
<head>
    <meta http-equiv="X-UA-Compatible" content="IE=Edge"/>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <%
        int idiomaUsuario = 1;
        if(request.getParameter("idioma") != null){
            try {
                idiomaUsuario = Integer.parseInt(request.getParameter("idioma"));
            } catch(Exception ex) {}
        }

        UsuarioValueObject usuarioVO = new UsuarioValueObject();
        int apl = 5;    
        String css = "";
        if (session.getAttribute("usuario") != null) {               
            usuarioVO = (UsuarioValueObject) session.getAttribute("usuario");    
            apl = usuarioVO.getAppCod();
            idiomaUsuario = usuarioVO.getIdioma();    
            css = usuarioVO.getCss();
        }

        //Clase para internacionalizar los mensajes de la aplicacion.
        MeLanbide58I18n meLanbide58I18n = MeLanbide58I18n.getInstance();
        Config m_Config = ConfigServiceHelper.getConfig("common");
        String statusBar = m_Config.getString("JSP.StatusBar");
        String numExpediente = (String)request.getAttribute("numExp");
        String mensaje = (String)request.getAttribute("mensaje");
        
    %>
    <jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
    <jsp:setProperty name="descriptor"  property="idi_cod" value="<%=idiomaUsuario%>" />
    <jsp:setProperty name="descriptor"  property="apl_cod" value="<%=apl%>" />
    <link rel="StyleSheet" media="screen" type="text/css" href="<%=request.getContextPath()%><%=css%>"/>
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/css/font-awesome.min.css'/>" media="screen">
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/less/animated.less'/>" media="screen">
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/>
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/extension/melanbide58/melanbide58.css"/>
    <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/validaciones.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/listaComboBox.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/popup.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/general.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/meLanbide58/ceescUtils.js"></script>
    <script type="text/javascript">
        var APP_CONTEXT_PATH = '<%=request.getContextPath()%>';
        var baseUrl = APP_CONTEXT_PATH + '/PeticionModuloIntegracion.do';

        function pulsarNuevoAnexoC() {
            document.getElementById('lblMensaje').innerHTML = "";
            var control = new Date();
            lanzarPopUpModal('<%=request.getContextPath()%>/PeticionModuloIntegracion.do?tarea=preparar&modulo=MELANBIDE58&operacion=cargarNuevoAnexoC&tipo=0&numExp=<%=numExpediente%>&nuevo=1&control=' + control.getTime(), 680, 800, 'no', 'no', function (result) {
                if (result != undefined) {
                    if (result[0] == '0') {
                        recargarTablaPlantilla(result);
                    }
                }
            });
        }

        function pulsarModificarAnexoC() {
            document.getElementById('lblMensaje').innerHTML = "";
            if (tablaPlantilla.selectedIndex != -1) {
                var control = new Date();
                lanzarPopUpModal('<%=request.getContextPath()%>/PeticionModuloIntegracion.do?tarea=preparar&modulo=MELANBIDE58&operacion=cargarModificarAnexoC&tipo=0&nuevo=0&numExp=<%=numExpediente%>&id=' + listaPlantilla[tablaPlantilla.selectedIndex][0] + '&control=' + control.getTime(), 680, 800, 'no', 'no', function (result) {
                    if (result != undefined) {
                        if (result[0] == '0') {
                            recargarDatosExpediente();
                        }
                    }
                });
            } else {
                jsp_alerta('A', '<%=meLanbide58I18n.getMensaje(idiomaUsuario, "msg.msjNoSelecFila")%>');
            }
        }

        function pulsarExportarAnexoC() {
            document.getElementById('lblMensaje').innerHTML = "";
            var apellidosC = "";
            var control = new Date();
            if (document.getElementById('apellidosC').value == null || document.getElementById('apellidosC').value == '') {
                apellidosC = "";
            } else {
                apellidosC = document.getElementById('apellidosC').value.replace(/\'/g, "''");
            }

            var parametros = "";
            parametros = '?tarea=preparar&modulo=MELANBIDE58&operacion=generarExcelAnexoC&tipo=0&numExp=<%=numExpediente%>&apellidosC=' + escape(apellidosC) + '&numLineaC=' + document.getElementById('numLineaC').value + '&nifC=' + document.getElementById('nifC').value + '&control=' + control.getTime();
            window.open(baseUrl + parametros, "_blank");
        }

        function pulsarEliminarAnexoC() {
            document.getElementById('lblMensaje').innerHTML = "";
            if (tablaPlantilla.selectedIndex != -1) {
                var resultado = jsp_alerta('', '<%=meLanbide58I18n.getMensaje(idiomaUsuario, "msg.preguntaEliminarPlantilla")%>');
                if (resultado == 1) {

                    var ajax = getXMLHttpRequest();
                    var nodos = null;
                    var parametros = "";
                    var control = new Date();
                    parametros = 'tarea=preparar&modulo=MELANBIDE58&operacion=eliminarAnexoC&tipo=0&numExp=<%=numExpediente%>&id=' + listaPlantilla[tablaPlantilla.selectedIndex][0] + '&control=' + control.getTime();
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
                        var listaNueva = extraerListaAnexoC(nodos);
                        var codigoOperacion = listaNueva[0];

                        if (codigoOperacion == "0") {
                            //   recargarTablaPlantilla(listaNueva);
                            recargarDatosExpediente();
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

        function pulsarLimpiarAnexoC() {
            document.getElementById('lblMensaje').innerHTML = "";
            document.getElementById("numLineaC").value = "";
            document.getElementById("nifC").value = "";
            document.getElementById("apellidosC").value = "";
            pulsarBuscarAnexoC();
        }

        function pulsarBuscarAnexoC() {
            document.getElementById('lblMensaje').innerHTML = "";
            mensajeValidacion = "";
            if (validarNumericoVacio(document.getElementById('numLineaC').value)) {
                if (validarTresCaracteresApellido(document.getElementById('apellidosC').value)) {
                    var ajax = getXMLHttpRequest();
                    var nodos = null;
                    var parametros = "";
                    var control = new Date();
                    var apellidosC = "";
                    var nifC = "";

                    if (document.getElementById('apellidosC').value == null || document.getElementById('apellidosC').value == '') {
                        apellidosC = "";
                    } else {
                        apellidosC = document.getElementById('apellidosC').value.replace(/\'/g, "''");
                    }

                    if (document.getElementById('nifC').value == null || document.getElementById('nifC').value == '') {
                        nifC = "";
                    } else {
                        nifC = document.getElementById('nifC').value.toUpperCase();
                    }

                    parametros = 'tarea=preparar&modulo=MELANBIDE58&operacion=buscarAnexoC&tipo=0&numExp=<%=numExpediente%>&apellidosC=' + escape(apellidosC) + '&numLineaC=' + document.getElementById('numLineaC').value + '&nifC=' + nifC + '&control=' + control.getTime();
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
                        var listaNueva = extraerListaAnexoC(nodos);
                        var codigoOperacion = listaNueva[0];

                        if (codigoOperacion == "0") {
                            recargarTablaPlantilla(listaNueva);
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

        function extraerListaAnexoC(nodos) {
            var elemento = nodos[0];
            var hijos = elemento.childNodes;
            var codigoOperacion = null;
            var codigoIncompleto = null;
            var listaNueva = new Array();
            var fila = new Array();
            var nodoFila;
            var hijosFila;
            for (j = 0; hijos != null && j < hijos.length; j++) {
                if (hijos[j].nodeName == "CODIGO_OPERACION") {
                    codigoOperacion = hijos[j].childNodes[0].nodeValue;
                    listaNueva[j] = codigoOperacion;
                }//if(hijos[j].nodeName=="CODIGO_OPERACION")
                else if (hijos[j].nodeName == "CODIGO_INCOMPLETO") {
                    codigoIncompleto = hijos[j].childNodes[0].nodeValue;
                    listaNueva[j] = codigoIncompleto;
                } else if (hijos[j].nodeName == "FILA") {
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
                        } else if (hijosFila[cont].nodeName == "NIFCIF") {
                            if (hijosFila[cont].childNodes[0].nodeValue != "null") {
                                fila[2] = hijosFila[cont].childNodes[0].nodeValue;
                            } else {
                                fila[2] = '-';
                            }
                        } else if (hijosFila[cont].nodeName == "APELLIDOS") {
                            if (hijosFila[cont].childNodes[0].nodeValue != "null") {
                                fila[3] = hijosFila[cont].childNodes[0].nodeValue;
                            } else {
                                fila[3] = '-';
                            }
                        } else if (hijosFila[cont].nodeName == "NOMBRE") {
                            if (hijosFila[cont].childNodes[0].nodeValue != "null") {
                                fila[4] = hijosFila[cont].childNodes[0].nodeValue;
                            } else {
                                fila[4] = '-';
                            }
                        } else if (hijosFila[cont].nodeName == "SEXO") {
                            if (hijosFila[cont].childNodes[0].nodeValue != "null") {
                                fila[5] = hijosFila[cont].childNodes[0].nodeValue;
                            } else {
                                fila[5] = '-';
                            }
                        } else if (hijosFila[cont].nodeName == "FECNACI") {
                            if (hijosFila[cont].childNodes.length > 0) {
                                fila[6] = hijosFila[cont].childNodes[0].nodeValue;
                            } else {
                                fila[6] = '-';
                            }
                        } else if (hijosFila[cont].nodeName == "NUMSS") {
                            if (hijosFila[cont].childNodes[0].nodeValue != "null") {
                                fila[7] = hijosFila[cont].childNodes[0].nodeValue;
                            } else {
                                fila[7] = '-';
                            }
                        } else if (hijosFila[cont].nodeName == "FECCERTIFICADO") {
                            if (hijosFila[cont].childNodes.length > 0) {
                                fila[8] = hijosFila[cont].childNodes[0].nodeValue;
                            } else {
                                fila[8] = '-';
                            }
                        } else if (hijosFila[cont].nodeName == "TIPODIS") {
                            if (hijosFila[cont].childNodes[0].nodeValue != "null") {
                                fila[9] = hijosFila[cont].childNodes[0].nodeValue;
                            } else {
                                fila[9] = '-';
                            }
                        } else if (hijosFila[cont].nodeName == "GRADO") {
                            if (hijosFila[cont].childNodes[0].nodeValue != "null") {
                                fila[10] = hijosFila[cont].childNodes[0].nodeValue;
                                var tex = fila[10].toString();
                                tex = tex.replace(".", ",");
                                fila[10] = tex + " &#37";
                            } else {
                                fila[10] = '-';
                            }
                        } else if (hijosFila[cont].nodeName == "DES_DISC_SEVERA_EMP") {//11
                            if (hijosFila[cont].childNodes.nodeValue != "null") {
                                fila[11] = hijosFila[cont].childNodes[0].nodeValue;
                            } else {
                                fila[11] = '-';
                            }
                        } else if (hijosFila[cont].nodeName == "DES_DISC_VALIDADA") {//12
                            if (hijosFila[cont].childNodes.nodeValue != "null") {
                                fila[12] = hijosFila[cont].childNodes[0].nodeValue;
                            } else {
                                fila[12] = '-';
                            }
                        } else if (hijosFila[cont].nodeName == "CODCONTRATO") {//13
                            if (hijosFila[cont].childNodes[0].nodeValue != "null") {
                                fila[13] = hijosFila[cont].childNodes[0].nodeValue;
                            } else {
                                fila[13] = '-';
                            }
                        } else if (hijosFila[cont].nodeName == "DATOS_PENDIENTES") { //14
                            if (hijosFila[cont].childNodes[0].nodeValue != "null") {
                                fila[14] = hijosFila[cont].childNodes[0].nodeValue;
                            } else {
                                fila[14] = '-';
                            }
                        } else if (hijosFila[cont].nodeName == "NUEVA_ALTA") {//15
                            if (hijosFila[cont].childNodes[0].nodeValue != "null") {
                                fila[15] = hijosFila[cont].childNodes[0].nodeValue;
                            } else {
                                fila[15] = '-';
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
            tablaPlantilla = new Tabla(true, '<%=descriptor.getDescripcion("buscar")%>', '<%=descriptor.getDescripcion("anterior")%>', '<%=descriptor.getDescripcion("siguiente")%>', '<%=descriptor.getDescripcion("mosFilasPag")%>', '<%=descriptor.getDescripcion("msgNoResultBusq")%>', '<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>', '<%=descriptor.getDescripcion("filtrDeTotal")%>', '<%=descriptor.getDescripcion("primero")%>', '<%=descriptor.getDescripcion("ultimo")%>', document.getElementById('listaPlantilla'), 1100);

            tablaPlantilla.addColumna('0', 'center', "<%=meLanbide58I18n.getMensaje(idiomaUsuario,"anexoC.tablaPlantilla.col1")%>");
            tablaPlantilla.addColumna('0', 'center', "<%=meLanbide58I18n.getMensaje(idiomaUsuario,"anexoC.tablaPlantilla.col13")%>");     // num linea
            tablaPlantilla.addColumna('75', 'center', "<%=meLanbide58I18n.getMensaje(idiomaUsuario,"anexoC.tablaPlantilla.col5")%>");     // DNI
            tablaPlantilla.addColumna('200', 'left', "<%=meLanbide58I18n.getMensaje(idiomaUsuario,"anexoC.tablaPlantilla.col2")%>");    // Ape y nombre
            tablaPlantilla.addColumna('75', 'center', "<%=meLanbide58I18n.getMensaje(idiomaUsuario,"anexoC.tablaPlantilla.col3")%>");    // Sexo
            tablaPlantilla.addColumna('100', 'center', "<%=meLanbide58I18n.getMensaje(idiomaUsuario,"anexoC.tablaPlantilla.col4")%>");    // F nac
            tablaPlantilla.addColumna('100', 'center', "<%=meLanbide58I18n.getMensaje(idiomaUsuario,"anexoC.tablaPlantilla.col6")%>");    // num SS
            tablaPlantilla.addColumna('100', 'center', "<%=meLanbide58I18n.getMensaje(idiomaUsuario,"anexoC.tablaPlantilla.col7")%>");    // f cert disc
            tablaPlantilla.addColumna('100', 'center', "<%=meLanbide58I18n.getMensaje(idiomaUsuario,"anexoC.tablaPlantilla.col8")%>");    // tipo disc
            tablaPlantilla.addColumna('75', 'center', "<%=meLanbide58I18n.getMensaje(idiomaUsuario,"anexoC.tablaPlantilla.col9")%>");     // porc disc
            tablaPlantilla.addColumna('75', 'center', "<%=meLanbide58I18n.getMensaje(idiomaUsuario,"anexoC.tablaPlantilla.col16")%>");    // Severa
            tablaPlantilla.addColumna('75', 'center', "<%=meLanbide58I18n.getMensaje(idiomaUsuario,"anexoC.tablaPlantilla.col17")%>");    // validada
            tablaPlantilla.addColumna('100', 'center', "<%=meLanbide58I18n.getMensaje(idiomaUsuario,"anexoC.tablaPlantilla.col19")%>");   // contrato

            tablaPlantilla.displayCabecera = true;
            tablaPlantilla.height = 300;
        }

        function recargarTablaPlantilla(result) {
            var fila;
            listaPlantilla = new Array();
            listaPlantillaTabla = new Array();
            for (var i = 1; i < result.length - 1; i++) {
                fila = result[i + 1];
                listaPlantilla[i - 1] = [fila[0], fila[1], fila[2], fila[3], fila[4],
                    fila[5], fila[6], fila[7], fila[8], fila[9], fila[10], fila[11], fila[12], fila[13], fila[12], fila[14], fila[15]];
                if (fila[15] == "S") {
                    listaPlantillaTabla[i - 1] = [fila[0], '<span style="color:red">' + fila[1] + "</span>", '<span style="color:red">' + fila[2] + "</span>",
                        '<span style="color:red">' + fila[3] + ", " + fila[4] + "</span>", '<span style="color:red">' + fila[5] + "</span>",
                        '<span style="color:red">' + fila[6] + "</span>", '<span style="color:red">' + fila[7] + "</span>", '<span style="color:red">' + fila[8] + "</span>",
                        '<span style="color:red">' + fila[9] + "</span>", '<span style="color:red">' + fila[10] + "</span>", '<span style="color:red">' + fila[11] + "</span>",
                        '<span style="color:red">' + fila[12] + "</span>", '<span style="color:red">' + fila[13] + "</span>"];
                } else {
                    listaPlantillaTabla[i - 1] = [fila[0], fila[1], fila[2], fila[3] + ", " + fila[4],
                        fila[5], fila[6], fila[7], fila[8], fila[9], fila[10], fila[11], fila[12], fila[13]];
                }
            }

            crearTabla();
            tablaPlantilla.lineas = listaPlantillaTabla;
            tablaPlantilla.displayTabla();
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

        function pulsarConsultarDisc() {
            document.getElementById('lblMensaje').innerHTML = "";
            var parametros = "";
            if (tablaPlantilla.selectedIndex != -1) {
                parametros = '?tarea=preparar&modulo=MELANBIDE58&operacion=cargarConsultaDisc&tipo=0&numExp=<%=numExpediente%>&id=' + listaPlantilla[tablaPlantilla.selectedIndex][0];
            } else {
                parametros = '?tarea=preparar&modulo=MELANBIDE58&operacion=cargarConsultaDisc&tipo=0&numExp=<%=numExpediente%>&id=';
            }
            lanzarPopUpModal(baseUrl + parametros, 1600, 1000, 'yes', 'yes', function (result) {
                if (result != undefined) {
                    if (result[0] == '0') {
                        recargarDatosExpediente();
                    }
                }
            });
        }

        function pulsaValidar() {
            var url = baseUrl + '?tarea=preparar&modulo=MELANBIDE58&operacion=procesarAnexos&tipo=0&numExp=<%=numExpediente%>';
            try {
                $.ajax({
                    url: url,
                    type: 'POST',
                    async: true,
                    beforeSend: antesDeLlamar,
                    success: procesarRespuestaValidar,
                    error: mostrarErrorRespuestaValidar
                });
            } catch (Err) {
                mostrarMensajeError();
            }
        }

        function antesDeLlamar() {
            elementoVisible('on', 'barraProgresoC');
            botones(true);
        }

        function procesarRespuestaValidar(result) {
            elementoVisible('off', 'barraProgresoC');
            botones(false);
            if (result) {
                var datos = JSON.parse(result);
                var msgtitle = "RESULTADO PROCESO";
                var mensaje;
                datos = datos.tabla;
                if (datos.error == "0") {
                    // todo OK
                    msgtitle = "PROCESO CORRECTO";
                    mensaje = '<%=meLanbide58I18n.getMensaje(idiomaUsuario,"msg.resultado")%>';
                } else if (datos.error == "1") {
                    mensaje = '<%=meLanbide58I18n.getMensaje(idiomaUsuario,"error.errorGen")%>';
                    msgtitle = "EXCEPCION";
                } else if (datos.error == "2") {
                    mensaje = '<%=meLanbide58I18n.getMensaje(idiomaUsuario,"error.errorBD")%>' + " " + '<%=meLanbide58I18n.getMensaje(idiomaUsuario,"msg.resultado")%>';
                } else if (datos.error == "3") {
                    mensaje = '<%=meLanbide58I18n.getMensaje(idiomaUsuario,"error.errorGuardarEjecutada")%>';
                } else if (datos.error == "4") {
                    mensaje = '<%=meLanbide58I18n.getMensaje(idiomaUsuario,"error.errorGuardarResultado")%>';
                } else if (datos.error == "5") {
                    mensaje = '<%=meLanbide58I18n.getMensaje(idiomaUsuario,"error.falloOperacion")%>' + " " + '<%=meLanbide58I18n.getMensaje(idiomaUsuario,"msg.resultado")%>';
                } else if (datos.error == "6") {
                    mensaje = '<%=meLanbide58I18n.getMensaje(idiomaUsuario,"error.errorGen")%>' + " " + '<%=meLanbide58I18n.getMensaje(idiomaUsuario,"msg.resultado")%>';
                } else {
                    mensaje = '<%=meLanbide58I18n.getMensaje(idiomaUsuario,"error.errorGen")%>';
                    msgtitle = "ERROR";
                }
                jsp_alerta("A", mensaje, msgtitle);
                recargarDatosExpediente();
            } else {
                mostrarMensajeError();
            }
        }

        function mostrarErrorRespuestaValidar() {
            elementoVisible('off', 'barraProgresoC');
            botones(false);
            mostrarMensajeError();
        }

        function mostrarMensajeError(codigo) {
            elementoVisible('off', 'barraProgresoC');
            botones(true);
            var msgtitle = "ERROR EN EL PROCESO";
            if (codigo == "1") {
                jsp_alerta("A", '<%=meLanbide58I18n.getMensaje(idiomaUsuario,"msg.errorGen")%>', msgtitle);
            } else if (codigo == "2") {
                jsp_alerta("A", '<%=meLanbide58I18n.getMensaje(idiomaUsuario,"error.errorBD")%>', msgtitle);
            } else if (codigo == "3") {
                jsp_alerta("A", '<%=meLanbide58I18n.getMensaje(idiomaUsuario,"error.errorGuardarEjecutada")%>', msgtitle);
            } else if (codigo == "4") {
                jsp_alerta("A", '<%=meLanbide58I18n.getMensaje(idiomaUsuario,"error.errorGuardarResultado")%>', msgtitle);
            } else if (codigo == "5") {
                jsp_alerta("A", '<%=meLanbide58I18n.getMensaje(idiomaUsuario,"error.falloOperacion")%>', "RESULTADO GRABADO");
            } else {
                jsp_alerta("A", '<%=meLanbide58I18n.getMensaje(idiomaUsuario,"error.errorGen")%>', msgtitle);
            }
        }

        function botones(estado) {
            document.getElementById('btnLimpiarC').disabled = estado;
            document.getElementById('btnBuscarC').disabled = estado;
            document.getElementById('btnNuevoAnexoC').disabled = estado;
            document.getElementById('btnModificarAnexoC').disabled = estado;
            document.getElementById('btnEliminarAnexoC').disabled = estado;
            document.getElementById('btnExportarAnexoC').disabled = estado;
            document.getElementById('btnValidarDiscapacidad').disabled = estado;
            document.getElementById('btnConsultarDisc').disabled = estado;
        }

    </script>
</head>
<body>
    <div class="tab-page" id="tabPage352" style="height:520px; width: 100%;">
        <h2 class="tab" id="pestana352"><%=meLanbide58I18n.getMensaje(idiomaUsuario,"label.titulo.pestana")%></h2> 
        <script type="text/javascript">tp1.addTabPage(document.getElementById("tabPage352"));</script>
        <br/>
        <h2 class="legendAzul" id="pestanaPrinc"><%=meLanbide58I18n.getMensaje(idiomaUsuario,"label.tituloPrincipal")%></h2>
        <div> 
            <div id="barraProgresoC" class="barraProgreso">
                <div class="contenedorHidepage">
                    <div class="textoHide">
                        <span>
                            <%=meLanbide58I18n.getMensaje(idiomaUsuario, "msg.procesando")%>
                        </span>
                    </div>
                    <div class="imagenHide" style="height:152px; width: 275px;">
                        <span id="disco" class="fa fa-spinner fa-spin" aria-hidden="true"/>
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
                            <input id="numLineaC" name="numLineaC" type="text" class="inputTexto" size="15" maxlength="5" value=""/>
                        </div>
                    </div>
                    <div style="width: 50px; float: left; padding-left: 15px;" class="etiqueta">
                        <%=meLanbide58I18n.getMensaje(idiomaUsuario,"label.nif")%>
                    </div>
                    <div style="width: 120px; float: left;">
                        <div style="float: left;">
                            <input id="nifC" name="nifC" type="text" class="inputTexto" size="15" maxlength="15" onkeyup="xAMayusculas(this);" value=""/>
                        </div>
                    </div>
                    <div style="width: 70px; float: left;padding-left: 15px;" class="etiqueta">
                        <%=meLanbide58I18n.getMensaje(idiomaUsuario,"label.apellidos")%>
                    </div>
                    <div style="width: 120px; float: left;">
                        <div style="float: left;">
                            <input id="apellidosC" name="apellidosC" type="text" class="inputTexto" size="30" maxlength="50" value=""/>
                        </div>
                    </div>
                    <div class="botonera" style="text-align: right; padding-left: 20px;">
                        <input type="button" id="btnLimpiarC" name="btnLimpiarC" class="botonGeneral"  value="<%=meLanbide58I18n.getMensaje(idiomaUsuario, "btn.limpiar")%>" onclick="pulsarLimpiarAnexoC();">
                        <input type="button" id="btnBuscarC" name="btnBuscarC" class="botonGeneral"  value="<%=meLanbide58I18n.getMensaje(idiomaUsuario, "btn.buscar")%>" onclick="pulsarBuscarAnexoC();">
                    </div>
                </div>
            </div>
            <br>
            <div id="divGeneral">     
                <div id="listaPlantilla"  align="center"></div>
            </div>
            <div class="botonera" style="text-align: center;">
                <input type="button" id="btnNuevoAnexoC" name="btnNuevoAnexoC" class="botonGeneral"  value="<%=meLanbide58I18n.getMensaje(idiomaUsuario, "btn.nuevo")%>" onclick="pulsarNuevoAnexoC();">
                <input type="button" id="btnModificarAnexoC" name="btnModificarAnexoC" class="botonGeneral" value="<%=meLanbide58I18n.getMensaje(idiomaUsuario, "btn.modificar")%>" onclick="pulsarModificarAnexoC();">
                <input type="button" id="btnEliminarAnexoC" name="btnEliminarAnexoC"   class="botonGeneral" value="<%=meLanbide58I18n.getMensaje(idiomaUsuario, "btn.eliminar")%>" onclick="pulsarEliminarAnexoC();">
                <input type="button" id="btnExportarAnexoC" name="btnExportarAnexoC"   class="botonMasLargo" value="<%=meLanbide58I18n.getMensaje(idiomaUsuario, "btn.Exportar")%>" onclick="pulsarExportarAnexoC();">            
            </div>
            <br/><br>
            <div class="botonera" style="text-align: center;">            
                <input type="button" id="btnValidarDiscapacidad" name="btnValidarDiscapacidad"   class="botonMasLargo" value="<%=meLanbide58I18n.getMensaje(idiomaUsuario, "btn.validarDiscapacidad")%>" onclick="pulsaValidar();">
                <input type="button" id="btnConsultarDisc" name="btnConsultarDisc" class="botonMasLargo" value="<%=meLanbide58I18n.getMensaje(idiomaUsuario, "btn.consultaDiscp")%>" onclick="pulsarConsultarDisc();">
            </div>
            <div class="lineaFormulario" style="color: red; padding-top: 50px;">  
                <b>
                    <label id="lblMensaje"><%=mensaje%></label>
                </b>
            </div>
        </div>  
    </div>
</body>        
<script  type="text/javascript">
    elementoVisible('off', 'barraProgresoC');
    //Tabla AnexoC
    var tablaPlantilla;
    var listaPlantilla = new Array();
    var listaPlantillaTabla = new Array();

    crearTabla();

    <%  		
       PlantillaVO objectVO = null;
       List<PlantillaVO> List = null;
       if(request.getAttribute("listaPlantilla")!=null){
           List = (List<PlantillaVO>)request.getAttribute("listaPlantilla");
       }													
       if (List!= null && List.size() >0){
           String barraSeparadoraDobleIdiomaDesple = ConfigurationParameter.getParameter(ConstantesMeLanbide58.BARRA_SEPARADORA_IDIOMA, ConstantesMeLanbide58.FICHERO_PROPIEDADES);
            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            for (int indice=0;indice<List.size();indice++) {
               objectVO = List.get(indice);
              
               String FecNaci="-";
               if(objectVO.getFecNaci()!=null){
                   FecNaci=dateFormat.format(objectVO.getFecNaci());                
               }

               String FecCertificado="-";
               if(objectVO.getFecCertificado()!=null){
                   FecCertificado=dateFormat.format(objectVO.getFecCertificado());                
               }

               String numLinea="-";
               if(objectVO.getNumLinea()!=null && !"".equals(objectVO.getNumLinea())){
                   numLinea=Integer.toString(objectVO.getNumLinea());                
               }

               String Grado="-";
               if(objectVO.getGrado()!=null){
                   Grado=String.valueOf((objectVO.getGrado().toString()).replace(".",","));
               }
               
               String apellidos="-";
               if(objectVO.getApellidos()!=null){
                   apellidos=objectVO.getApellidos();
                }

               String nombre="-";
               if(objectVO.getNombre()!=null){
                   nombre=objectVO.getNombre();
               }

               String sexo="-";
               if(objectVO.getDesSexo()!=null){
                    String descripcion = objectVO.getDesSexo();
                   String[] descripcionDobleIdioma =  (descripcion!=null?descripcion.split(barraSeparadoraDobleIdiomaDesple):null);
                   if(descripcionDobleIdioma!=null && descripcionDobleIdioma.length>1){
                       if(idiomaUsuario==4){
                           descripcion=descripcionDobleIdioma[1];
                       }else{
                           descripcion=descripcionDobleIdioma[0];
                       }
                   }
                sexo=  descripcion; 
               }

               String Nif_Dni="-";
               if(objectVO.getNif_Dni()!=null){
                   Nif_Dni=objectVO.getNif_Dni();
               }

               String NumSS="-";
               if(objectVO.getNumSS()!=null){
                   NumSS=objectVO.getNumSS();
               }

               String TipoDis="-";
               if(objectVO.getDesTipoDis()!=null){
                   String descripcion = objectVO.getDesTipoDis();
                   String[] descripcionDobleIdioma =  (descripcion!=null?descripcion.split(barraSeparadoraDobleIdiomaDesple):null);
                   if(descripcionDobleIdioma!=null && descripcionDobleIdioma.length>1){
                       if(idiomaUsuario==4){
                           descripcion=descripcionDobleIdioma[1];
                       }else{
                           descripcion=descripcionDobleIdioma[0];
                       }
                   }
                   TipoDis=descripcion;
               }

               String CodContrato="-";
               if(objectVO.getCodContrato()!=null) {
                    CodContrato=objectVO.getCodContrato();
                }
               
               String severoEmp="-";
               if(objectVO.getDesDiscSevera()!=null) {
                   String descripcion = objectVO.getDesDiscSevera();
                   String[] descripcionDobleIdioma =  (descripcion!=null?descripcion.split(barraSeparadoraDobleIdiomaDesple):null);
                   if(descripcionDobleIdioma!=null && descripcionDobleIdioma.length>1){
                       if(idiomaUsuario==4){
                           descripcion=descripcionDobleIdioma[1];
                       }else{
                           descripcion=descripcionDobleIdioma[0];
                       }
                   }
                   severoEmp=descripcion;
               } 
               
               String Validado="-";
               if(objectVO.getDesDiscValidada()!=null) {
                    String descripcion = objectVO.getDesDiscValidada();
                   String[] descripcionDobleIdioma =  (descripcion!=null?descripcion.split(barraSeparadoraDobleIdiomaDesple):null);
                   if(descripcionDobleIdioma!=null && descripcionDobleIdioma.length>1){
                       if(idiomaUsuario==4){
                           descripcion=descripcionDobleIdioma[1];
                       }else{
                           descripcion=descripcionDobleIdioma[0];
                       }
                   }
                   Validado=descripcion;
               }
              
               String DatosPendientes="";
               if(objectVO.getDatosPendientes()!=null){
                   DatosPendientes=objectVO.getDatosPendientes();
               } 
               
               String NuevaAlta="";
               if(objectVO.getNuevaAlta()!=null){
                   NuevaAlta=objectVO.getNuevaAlta();
               }
    %>

    listaPlantilla[<%=indice%>] = ['<%=objectVO.getId()%>', '<%=numLinea%>', '<%=Nif_Dni%>', "<%=apellidos%>" + ", " + "<%=nombre%>",
        '<%=sexo%>', '<%=FecNaci%>', '<%=NumSS%>', '<%=FecCertificado%>',
        '<%=TipoDis%>', '<%=Grado%>', '<%=severoEmp%>', '<%=Validado%>', '<%=CodContrato%>', '<%=DatosPendientes%>', '<%=NuevaAlta%>'];
    if ('<%=NuevaAlta%>' == "S") {
        listaPlantillaTabla[<%=indice%>] = ['<%=objectVO.getId()%>', "<span style='color:red'>" + '<%=numLinea%>' + "</span>", "<span style='color:red'>" + '<%=Nif_Dni%>' + "</span>", "<span style='color:red'>" + "<%=apellidos%>" + ", " + "<%=nombre%>" + "</span>",
            "<span style='color:red'>" + '<%=sexo%>' + "</span>", "<span style='color:red'>" + '<%=FecNaci%>' + "</span>", "<span style='color:red'>" + '<%=NumSS%>', "<span style='color:red'>" + '<%=FecCertificado%>',
            "<span style='color:red'>" + '<%=TipoDis%>' + "</span>", "<span style='color:red'>" + '<%=Grado%>' + "</span>",
            "<span style='color:red'>" + '<%=severoEmp%>' + "</span>", "<span style='color:red'>" + '<%=Validado%>' + "</span>", "<span style='color:red'>" + '<%=CodContrato%>' + "</span>"];

    } else {
        listaPlantillaTabla[<%=indice%>] = ['<%=objectVO.getId()%>', '<%=numLinea%>', '<%=Nif_Dni%>', "<%=apellidos%>" + ", " + "<%=nombre%>",
            '<%=sexo%>', '<%=FecNaci%>', '<%=NumSS%>', '<%=FecCertificado%>',
            '<%=TipoDis%>', '<%=Grado%>', '<%=severoEmp%>', '<%=Validado%>', '<%=CodContrato%>'];
    }
    <%
           }// for
       }// if
    %>

    tablaPlantilla.lineas = listaPlantillaTabla;
    tablaPlantilla.displayTabla();

</script>
<div id="popupcalendar" class="text"></div>                


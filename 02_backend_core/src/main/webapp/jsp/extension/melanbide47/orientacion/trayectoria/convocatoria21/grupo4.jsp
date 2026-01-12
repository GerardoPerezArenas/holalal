<%@taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide47.i18n.MeLanbide47I18n" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.common.service.config.Config"%>
<%@page import="es.altia.common.service.config.ConfigServiceHelper"%>
<%@page import="java.util.ArrayList" %>
<%@page import="java.util.List" %>
<%@page import="java.util.Map" %>
<%@page import="java.util.Iterator" %>
<%@page import="java.text.DateFormat" %>
<%@page import="java.text.SimpleDateFormat" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide47.util.ConstantesMeLanbide47"%>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide47.util.MeLanbide47Utils"%>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide47.vo.orientacion.OriTrayectoriaEntidadVO"%>
<%@page import="org.apache.commons.lang.StringEscapeUtils"%>
<%
    String sIdioma = request.getParameter("idioma");
    int idiomaUsuario = sIdioma!=null && sIdioma!= "" ? Integer.parseInt(sIdioma) : 1;
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

    String codEntidad = (String)request.getAttribute("codEntidad") != null ? (String)request.getAttribute("codEntidad") : "";
    String cifEntidad = (String)request.getAttribute("cifEntidad") != null ? (String)request.getAttribute("cifEntidad") : "";
    String nombreEntidad = (String)request.getAttribute("nombreEntidad") != null ? (String)request.getAttribute("nombreEntidad") : "";
    String nombreCompleto = (cifEntidad != null ? cifEntidad + " - " : "") + nombreEntidad;  
    List<OriTrayectoriaEntidadVO> listaGrupo4 = (List<OriTrayectoriaEntidadVO>)request.getAttribute("listaTrayectoriaActividades");
%>
<jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
<jsp:setProperty name="descriptor"  property="idi_cod" value="<%=idiomaUsuario%>" />
<jsp:setProperty name="descriptor"  property="apl_cod" value="<%=apl%>" />
<link rel="StyleSheet" media="screen" type="text/css" href="<%=request.getContextPath()%><%=css%>">
<script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.2/jquery.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/>
<link rel="stylesheet" type="text/css" href="<c:url value='/css/extension/melanbide47/melanbide47.css'/>">
<link rel="stylesheet" type="text/css" href="<c:url value='/css/table.css'/>">
<script type="text/javascript" src="<c:url value='/scripts/popup.js'/>"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/melanbide47/FixedColumnsTable.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/melanbide47/moment-with-locales.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/melanbide47/ori14Utils.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/melanbide47/TablaNuevaORI.js"></script>
<script type="text/javascript">
    var baseUrl = '<%=request.getContextPath()%>' + '/PeticionModuloIntegracion.do';

    function anadirGrupo4() {
        var result = null;
        lanzarPopUpModal('<%=request.getContextPath()%>/PeticionModuloIntegracion.do?tarea=preparar&modulo=MELANBIDE47&operacion=cargarAltaEdicionTrayectoria21&tipo=0&modoDatos=0&numero=<%=numExpediente%>&nombre=<%=nombreEntidad%>&idGrupo=4&codEntidad=<%=codEntidad%>', 500, 980, 'no', 'no', function (result) {
                    if (result != undefined) {
                        if (result[0] == '0') {
                            recargarTablaGrupo4(result);
                            refrescarPestanaTrayectoria_V();
                        }
                    }
                });
    }

    function modificarGrupo4() {
        var result = null;
        var idTrayEntidad = "";
        if ((tablaGrupo4.selectedIndex != -1)) {
            idTrayEntidad = listaGrupo4[tablaGrupo4.selectedIndex][0];
            lanzarPopUpModal('<%=request.getContextPath()%>/PeticionModuloIntegracion.do?tarea=preparar&modulo=MELANBIDE47&operacion=cargarAltaEdicionTrayectoria21&tipo=0&modoDatos=1&numero=<%=numExpediente%>&idTrayEntidad=' + idTrayEntidad + '&idGrupo=4&codEntidad= <%=codEntidad%>', 500, 980, 'no', 'no', function (result) {
                if (result != undefined) {
                    if (result[0] == '0') {
                        recargarTablaGrupo4(result);
                        refrescarPestanaTrayectoria_V();
                    }
                }
            });
        } else {
            jsp_alerta('A', '<%=meLanbide47I18n.getMensaje(idiomaUsuario, "msg.msjNoSelecFila")%>');
        }
    }

    function eliminarGrupo4() {
        var result = null;
        var idActividad = "";
        var codEntidad = "";
        if ((tablaGrupo4.selectedIndex != -1)) {
            idActividad = listaGrupo4[tablaGrupo4.selectedIndex][0];
            codEntidad = listaGrupo4[tablaGrupo4.selectedIndex][3];
            var resultado = jsp_alerta('', '<%=meLanbide47I18n.getMensaje(idiomaUsuario, "msg.preguntaEliminar")%>');
            if (resultado == 1) {
                var ajax = getXMLHttpRequest();
                var nodos = null;
                var CONTEXT_PATH = '<%=request.getContextPath()%>'
                var url = CONTEXT_PATH + "/PeticionModuloIntegracion.do";
                var parametros = "";
                var control = new Date();
                parametros = 'tarea=preparar&modulo=MELANBIDE47&operacion=eliminarTrayectoriaEntidad&tipo=0&numero=<%=numExpediente%>'
                        + '&idTrayEntidad=' + idActividad
                        + '&idGrupo=4'
                        + '&codEntidad=' + codEntidad;
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
                    var listaDatosRespuesta = extraerNodosGrupo4(nodos);
                    var codigoOperacion = listaDatosRespuesta[0];
                    if (codigoOperacion == "0") {
                        jsp_alerta("A", '<%=meLanbide47I18n.getMensaje(idiomaUsuario,"msg.registroEliminadoOK")%>');
                        if (listaDatosRespuesta != null && listaDatosRespuesta != undefined) {
                            if (listaDatosRespuesta[0] == '0') {
                                recargarTablaGrupo4(listaDatosRespuesta);
                                refrescarPestanaTrayectoria_V();
                            }
                        }
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
                    }//if(
                } catch (Err) {
                    jsp_alerta("A", '<%=meLanbide47I18n.getMensaje(idiomaUsuario,"error.errorProcesarRespuesta")%>');
                }
            }
        } else {
            jsp_alerta('A', '<%=meLanbide47I18n.getMensaje(idiomaUsuario, "msg.msjNoSelecFila")%>');
        }
    }

    function recargarTablaGrupo4(result) {
        var codOperacion = result != null ? result[0] : null;
        var fila;
        if (codOperacion != null) {
            listaGrupo4 = new Array();
            listaGrupo4Tabla = new Array();
            for (var i = 1; i < result.length; i++) {
                fila = result[i];
                listaGrupo4[i - 1] = [fila[0], fila[1], fila[2], fila[3], fila[4], fila[5], fila[6], fila[7], fila[8], fila[9], fila[10], fila[11], fila[12]];
                listaGrupo4Tabla[i - 1] = [fila[4], fila[6], fila[7], fila[8], (fila[10] != null && fila[10] != "null" ? fila[10] : "-"), (fila[11] != null && fila[11] != "null" ? fila[11] : "-"), (fila[12] != null && fila[12] != "null" ? fila[12] : "-")];
            }
            crearTablaGrupo4();
            tablaGrupo4.lineas = listaGrupo4Tabla;
            tablaGrupo4.displayCabecera = true;
            tablaGrupo4.height = 250;
            tablaGrupo4.displayTabla();
        } else {
            alert('Datos NO Guardados Correctamente..!!');
        }
    }

    function crearTablaGrupo4() {
        tablaGrupo4 = new TablaOri(true, '<%=descriptor.getDescripcion("buscar")%>', '<%=descriptor.getDescripcion("anterior")%>', '<%=descriptor.getDescripcion("siguiente")%>', '<%=descriptor.getDescripcion("mosFilasPag")%>', '<%=descriptor.getDescripcion("msgNoResultBusq")%>', '<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>', '<%=descriptor.getDescripcion("filtrDeTotal")%>', '<%=descriptor.getDescripcion("primero")%>', '<%=descriptor.getDescripcion("ultimo")%>', document.getElementById('listaGrupo4'), 1200, 25);
        tablaGrupo4.addColumna('600', 'left', "<%= meLanbide47I18n.getMensaje(idiomaUsuario,"label.trayectoria21.tabla.programa")%>");
        tablaGrupo4.addColumna('100', 'center', "<%= meLanbide47I18n.getMensaje(idiomaUsuario,"label.trayectoria21.tabla.fechaIni")%>");
        tablaGrupo4.addColumna('100', 'center', "<%= meLanbide47I18n.getMensaje(idiomaUsuario,"label.trayectoria21.tabla.fechaFin")%>");
        tablaGrupo4.addColumna('100', 'center', "<%= meLanbide47I18n.getMensaje(idiomaUsuario,"label.trayectoria21.tabla.duracion")%>");
        tablaGrupo4.addColumna('100', 'center', "<%= meLanbide47I18n.getMensaje(idiomaUsuario,"label.trayectoria21.tabla.fechaIniVal")%>");
        tablaGrupo4.addColumna('100', 'center', "<%= meLanbide47I18n.getMensaje(idiomaUsuario,"label.trayectoria21.tabla.fechaFinVal")%>");
        tablaGrupo4.addColumna('100', 'center', "<%= meLanbide47I18n.getMensaje(idiomaUsuario,"label.trayectoria21.tabla.duracionVal")%>");
    }

    function refrescarPestanaGrupo4() {
        var ajax = getXMLHttpRequest();
        var nodos = null;
        var CONTEXT_PATH = '<%=request.getContextPath()%>'
        var url = CONTEXT_PATH + "/PeticionModuloIntegracion.do";
        var parametros = "";
        parametros = 'tarea=preparar&modulo=MELANBIDE47&operacion=refrescarPestanaTrayectoria21&tipo=0&numero=<%=numExpediente%>&idGrupo=4&codEntidad=<%=codEntidad%>';
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
            var listaNueva = extraerNodosGrupo4(nodos);
            var codigoOperacion = listaNueva[0];
            if (codigoOperacion == "0") {
                recargarTablaGrupo4(listaNueva);
            } else if (codigoOperacion == "1") {
                jsp_alerta("A", '<%=String.format(meLanbide47I18n.getMensaje(idiomaUsuario,"error.refrescarPestana.errorBD"), meLanbide47I18n.getMensaje(idiomaUsuario, "label.trayectoria21.tituloPestana.grupo4"))%>');
            } else if (codigoOperacion == "2") {
                jsp_alerta("A", '<%=String.format(meLanbide47I18n.getMensaje(idiomaUsuario,"error.refrescarPestana.errorGen"), meLanbide47I18n.getMensaje(idiomaUsuario, "label.trayectoria21.tituloPestana.grupo4"))%>');
            } else if (codigoOperacion == "3") {
                jsp_alerta("A", '<%=String.format(meLanbide47I18n.getMensaje(idiomaUsuario,"error.refrescarPestana.pasoParametros"), meLanbide47I18n.getMensaje(idiomaUsuario, "label.trayectoria21.tituloPestana.grupo4"))%>');
            } else {
                jsp_alerta("A", '<%=String.format(meLanbide47I18n.getMensaje(idiomaUsuario,"error.refrescarPestana.errorGen"), meLanbide47I18n.getMensaje(idiomaUsuario, "label.trayectoria21.tituloPestana.grupo4"))%>');
            }
        } catch (Err) {
            jsp_alerta("A", '<%=String.format(meLanbide47I18n.getMensaje(idiomaUsuario,"error.refrescarPestana.errorGen"), meLanbide47I18n.getMensaje(idiomaUsuario, "label.trayectoria21.tituloPestana.grupo4"))%>');
        }//try-catch
    }


    function validarGrupo4() {
        var urlParam = 'tarea=preparar&modulo=MELANBIDE47&operacion=validarPestanaTrayectoria21&tipo=0&numero=<%=numExpediente%>&idGrupo=4';
        document.getElementById('msgEliminandoTrayectoria').style.display = "none";
        document.getElementById('msgCopiandoTrayectoria').style.display = "inline";
        try {
            $.ajax({
                url: baseUrl,
                type: 'POST',
                async: true,
                data: urlParam,
                beforeSend: antesDeLlamar,
                success: procesarRespuestaValidar,
                error: mostrarMensajeRespuesta4
            });
        } catch (Err) {
            mostrarMensajeRespuesta4();
        }
    }

    function antesDeLlamar() {
        elementoVisible('on', 'barraProgresoTrayectoria');
        botones(true);
    }

    function procesarRespuestaValidar(result) {
        if (result) {
            var datos = JSON.parse(result);
            datos = datos.tabla;
            if (datos.error == "0") {
                refrescarPestanaGrupo4();
                refrescarPestanaResumen();
                refrescarPestanasORI14(3);
            }
            mostrarMensajeRespuesta4(parseInt(datos.error));
        } else {
            mostrarMensajeRespuesta4();
        }
    }

    function mostrarMensajeRespuesta4(codigo) {
        var msgtitle = "ERROR EN EL PROCESO";
        var mensaje;
        switch (codigo) {
            case 0:
                msgtitle = "PROCESO CORRECTO";
                mensaje = '<%=meLanbide47I18n.getMensaje(idiomaUsuario,"msg.datosGuardadosOK")%>';
                break;
            case 1:
                mensaje = '<%=String.format(meLanbide47I18n.getMensaje(idiomaUsuario,"error.refrescarPestana.errorBD"), meLanbide47I18n.getMensaje(idiomaUsuario, "label.trayectoria21.tituloPestana.grupo4"))%>';
                break;
            case 2:
                mensaje = '<%=String.format(meLanbide47I18n.getMensaje(idiomaUsuario,"error.refrescarPestana.errorGen"), meLanbide47I18n.getMensaje(idiomaUsuario, "label.trayectoria21.tituloPestana.grupo4"))%>';
                break;
            case 3:
                mensaje = '<%=String.format(meLanbide47I18n.getMensaje(idiomaUsuario,"error.refrescarPestana.pasoParametros"), meLanbide47I18n.getMensaje(idiomaUsuario, "label.trayectoria21.tituloPestana.grupo4"))%>';
                break;
            default:
                mensaje = '<%=String.format(meLanbide47I18n.getMensaje(idiomaUsuario,"error.refrescarPestana.errorBD"), meLanbide47I18n.getMensaje(idiomaUsuario, "label.trayectoria21.tituloPestana.grupo4"))%>';
        }
        botones(false);
        elementoVisible('off', 'barraProgresoTrayectoria');
        jsp_alerta("A", mensaje, msgtitle);
    }

    function botones(estado) {
        document.getElementById('botonAnadirGrupo4').disabled = estado;
        document.getElementById('botonModificarGrupo4').disabled = estado;
        document.getElementById('botonEliminarGrupo4').disabled = estado;
        document.getElementById('botonValidarGrupo4').disabled = estado;
    }
    function extraerNodosGrupo4(nodos) {
        var elemento = nodos[0];
        var hijos = elemento.childNodes;
        var codigoOperacion = null;
        var listaDatosRespuesta = new Array();
        var fila1 = new Array();
        var nodoFila;
        var hijosFila;
        var nodoCampo;
        var j;
        for (j = 0; hijos != null && j < hijos.length; j++) {
            if (hijos[j].nodeName == "CODIGO_OPERACION") {
                codigoOperacion = hijos[j].childNodes[0].nodeValue;
                listaDatosRespuesta[j] = codigoOperacion;
            } else if (hijos[j].nodeName == "TRAYECTORIAS") {
                var nodoFila11 = hijos[j];
                var hijosFila11 = nodoFila11.childNodes;
                for (var cont1 = 0; cont1 < hijosFila11.length; cont1++) {
                    nodoFila = hijosFila11[cont1];
                    hijosFila = nodoFila.childNodes;
                    for (var cont = 0; cont < hijosFila.length; cont++) {      // recorremos los nodos ACTIVIDAD
                        if (hijosFila[cont].nodeName == "ID") {
                            nodoCampo = hijosFila[cont];
                            if (nodoCampo.childNodes.length > 0) {
                                fila1[0] = nodoCampo.childNodes[0].nodeValue;
                            } else {
                                fila1[0] = '-';
                            }
                        } else if (hijosFila[cont].nodeName == "TRAYIDFKORIPROGCONVACTGRUPO") {
                            nodoCampo = hijosFila[cont];
                            if (nodoCampo.childNodes.length > 0) {
                                fila1[1] = nodoCampo.childNodes[0].nodeValue;
                            } else {
                                fila1[1] = '-';
                            }
                        } else if (hijosFila[cont].nodeName == "TRAYIDFKORIPROGCONVACTSUBGRPRE") {
                            nodoCampo = hijosFila[cont];
                            if (nodoCampo.childNodes.length > 0) {
                                fila1[2] = nodoCampo.childNodes[0].nodeValue;
                            } else {
                                fila1[2] = '-';
                            }
                        } else if (hijosFila[cont].nodeName == "TRAYCODIGOENTIDAD") {
                            nodoCampo = hijosFila[cont];
                            if (nodoCampo.childNodes.length > 0) {
                                fila1[3] = nodoCampo.childNodes[0].nodeValue;
                            } else {
                                fila1[3] = '-';
                            }
                        } else if (hijosFila[cont].nodeName == "TRAYDESCRIPCION") {
                            nodoCampo = hijosFila[cont];
                            if (nodoCampo.childNodes.length > 0) {
                                fila1[4] = nodoCampo.childNodes[0].nodeValue;
                            } else {
                                fila1[4] = '-';
                            }
                        } else if (hijosFila[cont].nodeName == "TRAYTIENEEXPERIENCIA") {
                            nodoCampo = hijosFila[cont];
                            if (nodoCampo.childNodes.length > 0) {
                                fila1[5] = nodoCampo.childNodes[0].nodeValue;
                                if (fila1[5] == '1') {
                                    fila1[5] = '<%=meLanbide47I18n.getMensaje(idiomaUsuario, "label.si").toUpperCase()%>';
                                } else if (fila1[5] == '0') {
                                    fila1[5] = '<%=meLanbide47I18n.getMensaje(idiomaUsuario, "label.no").toUpperCase()%>';
                                }
                            } else {
                                fila1[5] = '-';
                            }
                        } else if (hijosFila[cont].nodeName == "TRAYFECHAINICIO") {
                            nodoCampo = hijosFila[cont];
                            if (nodoCampo.childNodes.length > 0) {
                                fila1[6] = nodoCampo.childNodes[0].nodeValue;
                            } else {
                                fila1[6] = '-';
                            }
                        } else if (hijosFila[cont].nodeName == "TRAYFECHAFIN") {
                            nodoCampo = hijosFila[cont];
                            if (nodoCampo.childNodes.length > 0) {
                                fila1[7] = nodoCampo.childNodes[0].nodeValue;
                            } else {
                                fila1[7] = '-';
                            }
                        } else if (hijosFila[cont].nodeName == "TRAYNUMEROMESES") {
                            nodoCampo = hijosFila[cont];
                            if (nodoCampo.childNodes.length > 0) {
                                fila1[8] = nodoCampo.childNodes[0].nodeValue;
                            } else {
                                fila1[8] = '-';
                            }
                        } else if (hijosFila[cont].nodeName == "TRAYTIENEEXPERIENCIAVAL") {
                            nodoCampo = hijosFila[cont];
                            if (nodoCampo.childNodes.length > 0) {
                                fila1[9] = nodoCampo.childNodes[0].nodeValue;
                                if (fila1[9] == '1') {
                                    fila1[9] = '<%=meLanbide47I18n.getMensaje(idiomaUsuario, "label.si").toUpperCase()%>';
                                } else if (fila1[9] == '0') {
                                    fila1[9] = '<%=meLanbide47I18n.getMensaje(idiomaUsuario, "label.no").toUpperCase()%>';
                                }
                            } else {
                                fila1[9] = '-';
                            }
                        } else if (hijosFila[cont].nodeName == "TRAYFECHAINICIOVAL") {
                            nodoCampo = hijosFila[cont];
                            if (nodoCampo.childNodes.length > 0) {
                                fila1[10] = nodoCampo.childNodes[0].nodeValue;
                            } else {
                                fila1[10] = '-';
                            }
                        } else if (hijosFila[cont].nodeName == "TRAYFECHAFINVAL") {
                            nodoCampo = hijosFila[cont];
                            if (nodoCampo.childNodes.length > 0) {
                                fila1[11] = nodoCampo.childNodes[0].nodeValue;
                            } else {
                                fila1[11] = '-';
                            }
                        } else if (hijosFila[cont].nodeName == "TRAYNUMEROMESESVAL") {
                            nodoCampo = hijosFila[cont];
                            if (nodoCampo.childNodes.length > 0) {
                                fila1[12] = nodoCampo.childNodes[0].nodeValue;
                            } else {
                                fila1[12] = '-';
                            }
                        } else if (hijosFila[cont].nodeName == "ORI_ENT_CIF") {
                            nodoCampo = hijosFila[cont];
                            if (nodoCampo.childNodes.length > 0) {
                                fila1[13] = nodoCampo.childNodes[0].nodeValue;
                            } else {
                                fila1[13] = '-';
                            }
                        } else if (hijosFila[cont].nodeName == "ORI_ENT_NOM") {
                            nodoCampo = hijosFila[cont];
                            if (nodoCampo.childNodes.length > 0) {
                                fila1[14] = nodoCampo.childNodes[0].nodeValue;
                            } else {
                                fila1[14] = '-';
                            }
                        }
                    }// nodo TRAYECTORIA
                    listaDatosRespuesta[cont1 + 1] = fila1;
                    fila1 = new Array();
                } //for(var cont1 = 0; cont1 < hijosFila11.length; cont1++)
            }// if trayectoriaS
        }//for(j=0;hijos!=null && j<hijos.length;j++)
        return listaDatosRespuesta;
    }
</script>
<body class="contenidoPantalla" id="bodyOriTrayectoriaActividades">
    <fieldset id="fieldsetGrupo4" style="height: 350px; margin-top: 20px; ">
        <div id="datosEntidad" style="text-align: center; background-color: #4B95D3; height: 20px;"title="<%=nombreEntidad%>">
            <div style="overflow: hidden; text-overflow: ellipsis; white-space: nowrap; width: 100%;">
                <%=nombreCompleto%>
            </div>
        </div>
        <div style="clear: both; overflow-x: hidden; overflow-y: auto;margin-top: 10px; "> <!--463px-->                   
            <label class="legendAzul"><%=meLanbide47I18n.getMensaje(idiomaUsuario,"legend.trayectoria.actividades.textoLargo")%></label>        
            <div id="divTablaActividades" align="center">
                <div id="divGeneral" style="clear: both; overflow-x: hidden; overflow-y: auto; height: 280px;">
                    <div id="listaGrupo4" style="padding: 5px; width:98%; font-size: 13px; height: 220px; text-align: center; overflow-x: auto; overflow-y: auto;margin:0px;margin-top:0px;" align="center"></div> 
                    <div class="botonera" style="padding-top: 10px;">
                        <input type="button" id="botonAnadirGrupo4" name="botonAnadirGrupo4" class="botonGeneral" value="<%=meLanbide47I18n.getMensaje(idiomaUsuario,"btn.anadir")%>" onclick="anadirGrupo4();"/>
                        <input type="button" id="botonModificarGrupo4" name="botonModificarGrupo4" class="botonGeneral" value="<%=meLanbide47I18n.getMensaje(idiomaUsuario,"btn.modificar")%>" onclick="modificarGrupo4();"/>
                        <input type="button" id="botonEliminarGrupo4" name="botonEliminarGrupo4" class="botonGeneral" value="<%=meLanbide47I18n.getMensaje(idiomaUsuario,"btn.eliminar")%>" onclick="eliminarGrupo4();"/>
                        <input type="button" id="botonValidarGrupo4" name="botonValidarGrupo4" class="botonMasLargo" value="<%=meLanbide47I18n.getMensaje(idiomaUsuario,"btn.copiarDatos")%>" onclick="validarGrupo4();"/>
                    </div>
                </div>
            </div>
        </div>
    </fieldset>
</body>
<script type="text/javascript">
    //  -----------------  GRUPO  4
    var tablaGrupo4;
    var listaGrupo4 = new Array();
    var listaGrupo4Tabla = new Array();
    crearTablaGrupo4();
    <%
        OriTrayectoriaEntidadVO fila= null;
        if(listaGrupo4!=null){
            DateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
            for(int i = 0;i <listaGrupo4.size();i++)
            {
                fila = listaGrupo4.get(i);
                String fecIni="";
                if (fila.getFechaInicio()!=null) {
                    fecIni=formatoFecha.format(fila.getFechaInicio());
                } else {
                    fecIni="-";
                }
                String fecIniVal="";
                if (fila.getFechaInicioVal()!=null) {
                    fecIniVal=formatoFecha.format(fila.getFechaInicioVal());
                } else {
                    fecIniVal="-";
                }
                String fecFin="";
                if (fila.getFechaFin()!=null) {
                    fecFin=formatoFecha.format(fila.getFechaFin());
                } else {
                    fecFin="-";
                }
                String fecFinVal="";
                if (fila.getFechaFinVal()!=null) {
                    fecFinVal=formatoFecha.format(fila.getFechaFinVal());
                } else {
                    fecFinVal="-";
                }
    %>
    listaGrupo4[<%=i%>] = ['<%=fila.getIdTrayEntidad()%>', '<%=fila.getIdConActGrupo()%>', '<%=fila.getIdConActSubgrupo()%>', '<%=fila.getCodEntidad()%>', '<%=fila.getDescActividad()%>', '<%=fecIni%>', '<%=fecFin%>', '<%=fila.getNumMeses()%>', '<%=fecIniVal%>', '<%=fecFinVal%>', '<%=fila.getNumMesesVal()%>', '<%=fila.getCifEntidad()%>', '<%=fila.getNombreEntidad()%>'];
    listaGrupo4Tabla[<%=i%>] = ['<%=fila.getDescActividad()%>', '<%=fecIni%>', '<%=fecFin%>', '<%=fila.getNumMeses()%>', '<%=fecIniVal%>', '<%=fecFinVal%>', '<%=(fila.getNumMesesVal() !=null ? fila.getNumMesesVal() : "-")%>'];
    <%
            }
        }
    %>
    tablaGrupo4.lineas = listaGrupo4Tabla;
    tablaGrupo4.displayCabecera = true;
    tablaGrupo4.height = 250;
    tablaGrupo4.displayTabla();
</script>
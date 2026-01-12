<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>

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
    List<OriTrayectoriaEntidadVO> listaTrayectoriaOtrosProg = (List<OriTrayectoriaEntidadVO>)request.getAttribute("listaTrayectoriaOtrosProg");

%>

<jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
<jsp:setProperty name="descriptor"  property="idi_cod" value="<%=idiomaUsuario%>" />
<jsp:setProperty name="descriptor"  property="apl_cod" value="<%=apl%>" />
<link rel="StyleSheet" media="screen" type="text/css" href="<%=request.getContextPath()%><%=css%>">
<script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.2/jquery.min.js"></script>
<!--<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script>-->
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

    function anadirGrupo3() {
        var result = null;
        lanzarPopUpModal('<%=request.getContextPath()%>/PeticionModuloIntegracion.do?tarea=preparar&modulo=MELANBIDE47&operacion=cargarAltaEdicionTrayectoria21&tipo=0&modoDatos=0&numero=<%=numExpediente%>&idGrupo=3&codEntidad=<%=codEntidad%>&nombre=<%=nombreEntidad%>', 500, 980, 'no', 'no', function (result) {
                    if (result != undefined) {
                        if (result[0] == 0) {
                            recargarTablaGrupo3(result);
                            refrescarPestanaTrayectoria_V();
                        }
                    }
                });
    }

    function modificarGrupo3() {
        var result = null;
        var idTrayEntidad = "";
        var codEntidad = "";
        if (tablaGrupo3.selectedIndex != -1) {
            idTrayEntidad = listaGrupo3[tablaGrupo3.selectedIndex][0];
            codEntidad = listaGrupo3[tablaGrupo3.selectedIndex][3];
            lanzarPopUpModal('<%=request.getContextPath()%>/PeticionModuloIntegracion.do?tarea=preparar&modulo=MELANBIDE47&operacion=cargarAltaEdicionTrayectoria21&tipo=0&modoDatos=1&numero=<%=numExpediente%>&idTrayEntidad=' + idTrayEntidad + '&idGrupo=3&codEntidad= <%=codEntidad%>', 500, 980, 'no', 'no', function (result) {
                if (result != undefined) {
                    if (result[0] == 0) {
                        recargarTablaGrupo3(result);
                        refrescarPestanaTrayectoria_V();
                    }
                }
            });
        } else {
            jsp_alerta('A', '<%=meLanbide47I18n.getMensaje(idiomaUsuario, "msg.msjNoSelecFila")%>');
        }
    }

    function eliminarGrupo3() {
        var result = null;
        var codColectivo = "";
        var idTrayEntidad = "";
        var codEntidad = "";
        if (tablaGrupo3.selectedIndex != -1) {
            idTrayEntidad = listaGrupo3[tablaGrupo3.selectedIndex][0];
            codEntidad = listaGrupo3[tablaGrupo3.selectedIndex][3];
            var resultado = jsp_alerta('', '<%=meLanbide47I18n.getMensaje(idiomaUsuario, "msg.preguntaEliminar")%>');
            if (resultado == 1) {
                var ajax = getXMLHttpRequest();
                var nodos = null;
                var CONTEXT_PATH = '<%=request.getContextPath()%>'
                var url = CONTEXT_PATH + "/PeticionModuloIntegracion.do";
                var parametros = "";
                var control = new Date();
                parametros = 'tarea=preparar&modulo=MELANBIDE47&operacion=eliminarTrayectoriaEntidad&tipo=0&numero=<%=numExpediente%>'
                        + '&idTrayEntidad=' + idTrayEntidad
                        + '&idGrupo=3'
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
                    var listaDatosRespuesta = extraerNodosGrupo3(nodos);
                    var codigoOperacion = listaDatosRespuesta[0];
                    
                    if (codigoOperacion == "0") {
                        jsp_alerta("A", '<%=meLanbide47I18n.getMensaje(idiomaUsuario,"msg.registroEliminadoOK")%>');
                        if (listaDatosRespuesta != null && listaDatosRespuesta != undefined) {
                            if (listaDatosRespuesta[0] == '0') {
                                recargarTablaGrupo3(listaDatosRespuesta);
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

    function recargarTablaGrupo3(result) {
        //TODO
        var codOperacion = result != null ? result[0] : null;
        var fila;
        if (codOperacion != null) {
            listaGrupo3 = new Array();
            listaGrupo3Tabla = new Array();
            for (var i = 1; i < result.length; i++) {
                fila = result[i];
                listaGrupo3[i - 1] = [fila[0], fila[1], fila[2], fila[3], fila[4], fila[5], fila[6], fila[7], fila[8], fila[9], fila[10], fila[11], fila[12]];
                listaGrupo3Tabla[i - 1] =[fila[4], fila[6], fila[7], fila[8], (fila[10] != null && fila[10] != "null" ? fila[10] : "-"), (fila[11] != null && fila[11] != "null" ? fila[11] : "-"), (fila[12] != null && fila[12] != "null" ? fila[12] : "-")];
           }
            crearTablaGrupo3();
            tablaGrupo3.lineas = listaGrupo3Tabla;
            tablaGrupo3.displayCabecera = true;
            tablaGrupo3.height = 250;
            tablaGrupo3.displayTabla();
        } else {
            alert('Datos NO Guardados Correctamente..!!');
        }
    }

    function crearTablaGrupo3() {
        tablaGrupo3 = new TablaOri(true, '<%=descriptor.getDescripcion("buscar")%>', '<%=descriptor.getDescripcion("anterior")%>', '<%=descriptor.getDescripcion("siguiente")%>', '<%=descriptor.getDescripcion("mosFilasPag")%>', '<%=descriptor.getDescripcion("msgNoResultBusq")%>', '<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>', '<%=descriptor.getDescripcion("filtrDeTotal")%>', '<%=descriptor.getDescripcion("primero")%>', '<%=descriptor.getDescripcion("ultimo")%>', document.getElementById('listaGrupo3'), 1200, 25);
        tablaGrupo3.addColumna('600', 'left', "<%=meLanbide47I18n.getMensaje(idiomaUsuario, "label.trayectoria21.tabla.programa")%>");
        tablaGrupo3.addColumna('100', 'center', "<%= meLanbide47I18n.getMensaje(idiomaUsuario,"label.trayectoria21.tabla.fechaIni")%>");
        tablaGrupo3.addColumna('100', 'center', "<%= meLanbide47I18n.getMensaje(idiomaUsuario,"label.trayectoria21.tabla.fechaFin")%>");
        tablaGrupo3.addColumna('100', 'center', "<%= meLanbide47I18n.getMensaje(idiomaUsuario,"label.trayectoria21.tabla.duracion")%>");
        tablaGrupo3.addColumna('100', 'center', "<%= meLanbide47I18n.getMensaje(idiomaUsuario,"label.trayectoria21.tabla.fechaIniVal")%>");
        tablaGrupo3.addColumna('100', 'center', "<%= meLanbide47I18n.getMensaje(idiomaUsuario,"label.trayectoria21.tabla.fechaFinVal")%>");
        tablaGrupo3.addColumna('100', 'center', "<%= meLanbide47I18n.getMensaje(idiomaUsuario,"label.trayectoria21.tabla.duracionVal")%>");
    }

    function refrescarPestanaGrupo3() {
        var ajax = getXMLHttpRequest();
        var nodos = null;
        var CONTEXT_PATH = '<%=request.getContextPath()%>'
        var url = CONTEXT_PATH + "/PeticionModuloIntegracion.do";
        var parametros = "";
        parametros = 'tarea=preparar&modulo=MELANBIDE47&operacion=refrescarPestanaTrayectoria21&tipo=0&numero=<%=numExpediente%>&idGrupo=3&codEntidad=<%=codEntidad%>';
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
            var listaNueva = extraerNodosGrupo3(nodos);
            var codigoOperacion = listaNueva[0];
            if (codigoOperacion == "0") {
                recargarTablaGrupo3(listaNueva);
            } else if (codigoOperacion == "1") {
                jsp_alerta("A", '<%=String.format(meLanbide47I18n.getMensaje(idiomaUsuario,"error.refrescarPestana.errorBD"), meLanbide47I18n.getMensaje(idiomaUsuario, "label.trayectoria21.tituloPestana.grupo3"))%>');
            } else if (codigoOperacion == "2") {
                jsp_alerta("A", '<%=String.format(meLanbide47I18n.getMensaje(idiomaUsuario,"error.refrescarPestana.errorGen"), meLanbide47I18n.getMensaje(idiomaUsuario, "label.trayectoria21.tituloPestana.grupo3"))%>');
            } else if (codigoOperacion == "3") {
                jsp_alerta("A", '<%=String.format(meLanbide47I18n.getMensaje(idiomaUsuario,"error.refrescarPestana.pasoParametros"), meLanbide47I18n.getMensaje(idiomaUsuario, "label.trayectoria21.tituloPestana.grupo3"))%>');
            } else {
                jsp_alerta("A", '<%=String.format(meLanbide47I18n.getMensaje(idiomaUsuario,"error.refrescarPestana.errorGen"), meLanbide47I18n.getMensaje(idiomaUsuario, "label.trayectoria21.tituloPestana.grupo3"))%>');
            }
        } catch (Err) {
            jsp_alerta("A", '<%=String.format(meLanbide47I18n.getMensaje(idiomaUsuario,"error.refrescarPestana.errorGen"), meLanbide47I18n.getMensaje(idiomaUsuario, "label.trayectoria21.tituloPestana.grupo3"))%>');
        }//try-catch
    }
    

    function validarGrupo3() {
        var urlParam = 'tarea=preparar&modulo=MELANBIDE47&operacion=validarPestanaTrayectoria21&tipo=0&numero=<%=numExpediente%>&idGrupo=3';
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
                error: mostrarMensajeRespuesta3
            });
        } catch (Err) {
            mostrarMensajeRespuesta3();
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
                refrescarPestanaGrupo3();
                refrescarPestanaResumen();
                refrescarPestanasORI14(3);
            }
            mostrarMensajeRespuesta3(parseInt(datos.error));
        } else {
            mostrarMensajeRespuesta3();
        }
    }

    function mostrarMensajeRespuesta3(codigo) {
        var msgtitle = "ERROR EN EL PROCESO";
        var mensaje;
        switch (codigo) {
            case 0:
                msgtitle = "PROCESO CORRECTO";
                mensaje = '<%=meLanbide47I18n.getMensaje(idiomaUsuario,"msg.datosGuardadosOK")%>';
                break;
            case 1:
                mensaje = '<%=String.format(meLanbide47I18n.getMensaje(idiomaUsuario,"error.refrescarPestana.errorBD"), meLanbide47I18n.getMensaje(idiomaUsuario, "label.trayectoria21.tituloPestana.grupo3"))%>';
                break;
            case 2:
                mensaje = '<%=String.format(meLanbide47I18n.getMensaje(idiomaUsuario,"error.refrescarPestana.errorGen"), meLanbide47I18n.getMensaje(idiomaUsuario, "label.trayectoria21.tituloPestana.grupo3"))%>';
                break;
            case 3:
                mensaje = '<%=String.format(meLanbide47I18n.getMensaje(idiomaUsuario,"error.refrescarPestana.pasoParametros"), meLanbide47I18n.getMensaje(idiomaUsuario, "label.trayectoria21.tituloPestana.grupo3"))%>';
                break;
            default:
                mensaje = '<%=String.format(meLanbide47I18n.getMensaje(idiomaUsuario,"error.refrescarPestana.errorBD"), meLanbide47I18n.getMensaje(idiomaUsuario, "label.trayectoria21.tituloPestana.grupo3"))%>';
        }
        botones(false);
        elementoVisible('off', 'barraProgresoTrayectoria');
        jsp_alerta("A", mensaje, msgtitle);
    }

    function botones(estado) {
        document.getElementById('botonAnadirGrupo3').disabled = estado;
        document.getElementById('botonModificarGrupo3').disabled = estado;
        document.getElementById('botonEliminarGrupo3').disabled = estado;
        document.getElementById('botonValidarGrupo3').disabled = estado;
    }
    
    function extraerNodosGrupo3(nodos) {
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
                    for (var cont = 0; cont < hijosFila.length; cont++) {      // recorremos los nodos ACTIVIDAD
                        if (hijosFila[cont].nodeName == "ID") {
                            nodoCampo = hijosFila[cont];
                            if (nodoCampo.childNodes.length > 0) {
                                fila[0] = nodoCampo.childNodes[0].nodeValue;
                            } else {
                                fila[0] = '-';
                            }
                        } else if (hijosFila[cont].nodeName == "TRAYIDFKORIPROGCONVACTGRUPO") {
                            nodoCampo = hijosFila[cont];
                            if (nodoCampo.childNodes.length > 0) {
                                fila[1] = nodoCampo.childNodes[0].nodeValue;
                            } else {
                                fila[1] = '-';
                            }
                        } else if (hijosFila[cont].nodeName == "TRAYIDFKORIPROGCONVACTSUBGRPRE") {
                            nodoCampo = hijosFila[cont];
                            if (nodoCampo.childNodes.length > 0) {
                                fila[2] = nodoCampo.childNodes[0].nodeValue;
                            } else {
                                fila[2] = '-';
                            }
                        } else if (hijosFila[cont].nodeName == "TRAYCODIGOENTIDAD") {
                            nodoCampo = hijosFila[cont];
                            if (nodoCampo.childNodes.length > 0) {
                                fila[3] = nodoCampo.childNodes[0].nodeValue;
                            } else {
                                fila[3] = '-';
                            }
                        } else if (hijosFila[cont].nodeName == "TRAYDESCRIPCION") {
                            nodoCampo = hijosFila[cont];
                            if (nodoCampo.childNodes.length > 0) {
                                fila[4] = nodoCampo.childNodes[0].nodeValue;
                            } else {
                                fila[4] = '-';
                            }
                        } else if (hijosFila[cont].nodeName == "TRAYTIENEEXPERIENCIA") {
                            nodoCampo = hijosFila[cont];
                            if (nodoCampo.childNodes.length > 0) {
                                fila[5] = nodoCampo.childNodes[0].nodeValue;
                                if (fila[5] == '1') {
                                    fila[5] = '<%=meLanbide47I18n.getMensaje(idiomaUsuario, "label.si").toUpperCase()%>';
                                } else if (fila[5] == '0') {
                                    fila[5] = '<%=meLanbide47I18n.getMensaje(idiomaUsuario, "label.no").toUpperCase()%>';
                                }
                            } else {
                                fila[5] = '-';
                            }
                        } else if (hijosFila[cont].nodeName == "TRAYFECHAINICIO") {
                            nodoCampo = hijosFila[cont];
                            if (nodoCampo.childNodes.length > 0) {
                                fila[6] = nodoCampo.childNodes[0].nodeValue;
                            } else {
                                fila[6] = '-';
                            }
                        } else if (hijosFila[cont].nodeName == "TRAYFECHAFIN") {
                            nodoCampo = hijosFila[cont];
                            if (nodoCampo.childNodes.length > 0) {
                                fila[7] = nodoCampo.childNodes[0].nodeValue;
                            } else {
                                fila[7] = '-';
                            }
                        } else if (hijosFila[cont].nodeName == "TRAYNUMEROMESES") {
                            nodoCampo = hijosFila[cont];
                            if (nodoCampo.childNodes.length > 0) {
                                fila[8] = nodoCampo.childNodes[0].nodeValue;
                            } else {
                                fila[8] = '-';
                            }
                        } else if (hijosFila[cont].nodeName == "TRAYTIENEEXPERIENCIAVAL") {
                            nodoCampo = hijosFila[cont];
                            if (nodoCampo.childNodes.length > 0) {
                                fila[9] = nodoCampo.childNodes[0].nodeValue;
                                if (fila[9] == '1') {
                                    fila[9] = '<%=meLanbide47I18n.getMensaje(idiomaUsuario, "label.si").toUpperCase()%>';
                                } else if (fila[9] == '0') {
                                    fila[9] = '<%=meLanbide47I18n.getMensaje(idiomaUsuario, "label.no").toUpperCase()%>';
                                }
                            } else {
                                fila[9] = '-';
                            }
                        } else if (hijosFila[cont].nodeName == "TRAYFECHAINICIOVAL") {
                            nodoCampo = hijosFila[cont];
                            if (nodoCampo.childNodes.length > 0) {
                                fila[10] = nodoCampo.childNodes[0].nodeValue;
                            } else {
                                fila[10] = '-';
                            }
                        } else if (hijosFila[cont].nodeName == "TRAYFECHAFINVAL") {
                            nodoCampo = hijosFila[cont];
                            if (nodoCampo.childNodes.length > 0) {
                                fila[11] = nodoCampo.childNodes[0].nodeValue;
                            } else {
                                fila[11] = '-';
                            }
                        } else if (hijosFila[cont].nodeName == "TRAYNUMEROMESESVAL") {
                            nodoCampo = hijosFila[cont];
                            if (nodoCampo.childNodes.length > 0) {
                                fila[12] = nodoCampo.childNodes[0].nodeValue;
                            } else {
                                fila[12] = '-';
                            }
                        } else if (hijosFila[cont].nodeName == "ORI_ENT_CIF") {
                            nodoCampo = hijosFila[cont];
                            if (nodoCampo.childNodes.length > 0) {
                                fila[13] = nodoCampo.childNodes[0].nodeValue;
                            } else {
                                fila[13] = '-';
                            }
                        } else if (hijosFila[cont].nodeName == "ORI_ENT_NOM") {
                            nodoCampo = hijosFila[cont];
                            if (nodoCampo.childNodes.length > 0) {
                                fila[14] = nodoCampo.childNodes[0].nodeValue;
                            } else {
                                fila[14] = '-';
                            }
                        }
                    }// nodo TRAYECTORIA
                    listaDatosRespuesta[cont1 + 1] = fila;
                    fila = new Array();
                } //for(var cont1 = 0; cont1 < hijosFila1.length; cont1++)
            }// if trayectoriaS
        }//for(j=0;hijos!=null && j<hijos.length;j++)
        return listaDatosRespuesta;
    }    
</script>
<body class="contenidoPantalla" id="bodyOriTrayectoriaOtrosProgramas">
    <fieldset id="fieldsetTrayectoriaOP" style="height: 350px; margin-top: 20px; ">
        <div id="datosEntidad" style="text-align: center; background-color: #4B95D3; height: 20px;"title="<%=nombreEntidad%>">
            <div style="overflow: hidden; text-overflow: ellipsis; white-space: nowrap; width: 100%;">
                <%=nombreCompleto%>
            </div>
        </div>
        <div style="clear: both; overflow-x: hidden; overflow-y: auto; margin-top: 10px;"> <!--463px-->                   
            <label class="legendAzul"><%=meLanbide47I18n.getMensaje(idiomaUsuario,"legend.trayectoria.otrosProgramas.textoLargo")%></label>
            <div id="divTablaTrayectoriaOtroProg" align="center">
                <div id="divGeneral" style="clear: both; overflow-x: hidden; overflow-y: auto; height: 280px;">
                    <div id="listaGrupo3" style="padding: 5px; width:98%; font-size: 13px; height: 220px; text-align: center; overflow-x: auto; overflow-y: auto;margin:0px;margin-top:0px;" align="center"></div>
                    <div class="botonera" style="padding-top: 10px;">
                        <input type="button" id="botonAnadirGrupo3" name="botonAnadirGrupo3" class="botonGeneral" value="<%=meLanbide47I18n.getMensaje(idiomaUsuario,"btn.anadir")%>" onclick="anadirGrupo3();"/>
                        <input type="button" id="botonModificarGrupo3" name="botonModificarGrupo3" class="botonGeneral" value="<%=meLanbide47I18n.getMensaje(idiomaUsuario,"btn.modificar")%>" onclick="modificarGrupo3();"/>
                        <input type="button" id="botonEliminarGrupo3" name="botonEliminarGrupo3" class="botonGeneral" value="<%=meLanbide47I18n.getMensaje(idiomaUsuario,"btn.eliminar")%>" onclick="eliminarGrupo3();"/>              
                        <input type="button" id="botonValidarGrupo3" name="botonValidarGrupo3" class="botonMasLargo" value="<%=meLanbide47I18n.getMensaje(idiomaUsuario,"btn.copiarDatos")%>" onclick="validarGrupo3();"/>
                    </div>
                </div>
            </div>
        </div>
    </fieldset>
</body>
<script type="text/javascript">
    //  -----------------  GRUPO  3
    var tablaGrupo3;
    var listaGrupo3 = new Array();
    var listaGrupo3Tabla = new Array();
    crearTablaGrupo3();
    <%
        OriTrayectoriaEntidadVO fila = null;
        if (listaTrayectoriaOtrosProg!=null){
            DateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
            for(int i=0;i<listaTrayectoriaOtrosProg.size();i++){
                fila = listaTrayectoriaOtrosProg.get(i);
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
    listaGrupo3[<%=i%>] = ['<%=fila.getIdTrayEntidad()%>', '<%=fila.getIdConActGrupo()%>', '<%=fila.getIdConActSubgrupo()%>', '<%=fila.getCodEntidad()%>', '<%=fila.getDescActividad()%>', '<%=fecIni%>', '<%=fecFin%>', '<%=fila.getNumMeses()%>', '<%=fecIniVal%>', '<%=fecFinVal%>', '<%=fila.getNumMesesVal()%>', '<%=fila.getCifEntidad()%>', '<%=fila.getNombreEntidad()%>'];
    listaGrupo3Tabla [<%=i%>] = ['<%=fila.getDescActividad()%>', '<%=fecIni%>', '<%=fecFin%>', '<%=fila.getNumMeses()%>', '<%=fecIniVal%>', '<%=fecFinVal%>', '<%=(fila.getNumMesesVal() !=null ? fila.getNumMesesVal() : "-")%>'];
    <%
            }
        }
    %>
    tablaGrupo3.lineas = listaGrupo3Tabla;
    tablaGrupo3.displayCabecera = true;
    tablaGrupo3.height = 250;
    tablaGrupo3.displayTabla();
</script>

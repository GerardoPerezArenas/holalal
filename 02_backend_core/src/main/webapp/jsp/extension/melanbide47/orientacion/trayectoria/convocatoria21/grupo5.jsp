<%@taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide47.i18n.MeLanbide47I18n" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.common.service.config.Config"%>
<%@page import="es.altia.common.service.config.ConfigServiceHelper"%>
<%@page import="java.text.DateFormat" %>
<%@page import="java.text.SimpleDateFormat" %>
<%@page import="java.util.ArrayList" %>
<%@page import="java.util.List" %>
<%@page import="java.util.Map" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide47.vo.orientacion.OriTrayectoriaEntidadVO"%>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide47.util.ConstantesMeLanbide47"%>
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
    List<OriTrayectoriaEntidadVO> listaGrupo5 = (List<OriTrayectoriaEntidadVO>)request.getAttribute("listaGrupo5");
%>
<jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
<jsp:setProperty name="descriptor"  property="idi_cod" value="<%=idiomaUsuario%>" />
<jsp:setProperty name="descriptor"  property="apl_cod" value="<%=apl%>" />
<link rel="StyleSheet" media="screen" type="text/css" href="<%=request.getContextPath()%><%=css%>">
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script>
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

    function anadirGrupo5() {
        var result = null;
        lanzarPopUpModal('<%=request.getContextPath()%>/PeticionModuloIntegracion.do?tarea=preparar&modulo=MELANBIDE47&operacion=cargarAltaEdicionTrayectoria21&tipo=0&modoDatos=0&numero=<%=numExpediente%>&nombre=<%=nombreEntidad%>&idGrupo=5&codEntidad=<%=codEntidad%>', 500, 980, 'no', 'no', function (result) {
                    if (result != undefined) {
                        if (result[0] == '0') {
                            recargarTablaGrupo5(result);
                            refrescarPestanaTrayectoria_V();
                        }
                    }
                });
    }

    function modificarGrupo5() {
        var result = null;
        var idTrayEntidad = "";
        if ((tablaGrupo5.selectedIndex != -1)) {
            idTrayEntidad = listaGrupo5[tablaGrupo5.selectedIndex][0];
            lanzarPopUpModal('<%=request.getContextPath()%>/PeticionModuloIntegracion.do?tarea=preparar&modulo=MELANBIDE47&operacion=cargarAltaEdicionTrayectoria21&tipo=0&modoDatos=1&numero=<%=numExpediente%>&idTrayEntidad=' + idTrayEntidad + '&idGrupo=5&codEntidad= <%=codEntidad%>', 500, 980, 'no', 'no', function (result) {
                if (result != undefined) {
                    if (result[0] == '0') {
                        recargarTablaGrupo5(result);
                        refrescarPestanaTrayectoria_V();
                    }
                }
            });
        } else {
            jsp_alerta('A', '<%=meLanbide47I18n.getMensaje(idiomaUsuario, "msg.msjNoSelecFila")%>');
        }
    }

    function eliminarGrupo5() {
        var result = null;
        var idActividad = "";
        var codEntidad = "";
        if ((tablaGrupo5.selectedIndex != -1)) {
            idActividad = listaGrupo5[tablaGrupo5.selectedIndex][0];
            codEntidad = listaGrupo5[tablaGrupo5.selectedIndex][3];
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
                        + '&idGrupo=5'
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
                    var listaDatosRespuesta = extraerNodosGrupo5(nodos);
                    var codigoOperacion = listaDatosRespuesta[0];
                    if (codigoOperacion == "0") {
                        jsp_alerta("A", '<%=meLanbide47I18n.getMensaje(idiomaUsuario,"msg.registroEliminadoOK")%>');
                        if (listaDatosRespuesta != null && listaDatosRespuesta != undefined) {
                            if (listaDatosRespuesta[0] == '0') {
                                recargarTablaGrupo5(listaDatosRespuesta);
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

    function recargarTablaGrupo5(result) {
        var codOperacion = result != null ? result[0] : null;
        var fila5;
        if (codOperacion != null) {
            listaGrupo5 = new Array();
            listaGrupo5Tabla = new Array();
            for (var i = 1; i < result.length; i++) {
                fila5 = result[i];
                listaGrupo5[i - 1] = [fila5[0], fila5[1], fila5[2], fila5[3], fila5[4], fila5[5], fila5[6], fila5[7], fila5[8], fila5[9], fila5[10], fila5[11], fila5[12], fila5[13], fila5[14]];
                listaGrupo5Tabla[i - 1] = [fila5[4], fila5[6], fila5[7], fila5[8], (fila5[10] != null && fila5[10] != "null" ? fila5[10] : "-"), (fila5[11] != null && fila5[11] != "null" ? fila5[11] : "-"), (fila5[12] != null && fila5[12] != "null" ? fila5[12] : "-")];
            }
            crearTablaGrupo5();
            tablaGrupo5.lineas = listaGrupo5Tabla;
            tablaGrupo5.displayCabecera = true;
            tablaGrupo5.height = 250;
            tablaGrupo5.displayTabla();
        } else {
            alert('Datos NO Guardados Correctamente..!!');
        }
    }

    function crearTablaGrupo5() {
        tablaGrupo5 = new TablaOri(true, '<%=descriptor.getDescripcion("buscar")%>', '<%=descriptor.getDescripcion("anterior")%>', '<%=descriptor.getDescripcion("siguiente")%>', '<%=descriptor.getDescripcion("mosFilasPag")%>', '<%=descriptor.getDescripcion("msgNoResultBusq")%>', '<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>', '<%=descriptor.getDescripcion("filtrDeTotal")%>', '<%=descriptor.getDescripcion("primero")%>', '<%=descriptor.getDescripcion("ultimo")%>', document.getElementById('listaGrupo5'), 1200, 25);
        tablaGrupo5.addColumna('600', 'left', "<%= meLanbide47I18n.getMensaje(idiomaUsuario,"label.trayectoria21.tabla.programa")%>");
        tablaGrupo5.addColumna('100', 'center', "<%= meLanbide47I18n.getMensaje(idiomaUsuario,"label.trayectoria21.tabla.fechaIni")%>");
        tablaGrupo5.addColumna('100', 'center', "<%= meLanbide47I18n.getMensaje(idiomaUsuario,"label.trayectoria21.tabla.fechaFin")%>");
        tablaGrupo5.addColumna('100', 'center', "<%= meLanbide47I18n.getMensaje(idiomaUsuario,"label.trayectoria21.tabla.duracion")%>");
        tablaGrupo5.addColumna('100', 'center', "<%= meLanbide47I18n.getMensaje(idiomaUsuario,"label.trayectoria21.tabla.fechaIniVal")%>");
        tablaGrupo5.addColumna('100', 'center', "<%= meLanbide47I18n.getMensaje(idiomaUsuario,"label.trayectoria21.tabla.fechaFinVal")%>");
        tablaGrupo5.addColumna('100', 'center', "<%= meLanbide47I18n.getMensaje(idiomaUsuario,"label.trayectoria21.tabla.duracionVal")%>");
    }

    function refrescarPestanaGrupo5() {
        var ajax = getXMLHttpRequest();
        var nodos = null;
        var CONTEXT_PATH = '<%=request.getContextPath()%>'
        var url = CONTEXT_PATH + "/PeticionModuloIntegracion.do";
        var parametros = "";
        parametros = 'tarea=preparar&modulo=MELANBIDE47&operacion=refrescarPestanaTrayectoria21&tipo=0&numero=<%=numExpediente%>&idGrupo=5&codEntidad=<%=codEntidad%>';
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
            var listaNueva = extraerNodosGrupo5(nodos);
             var codigoOperacion = listaNueva[0];
            if (codigoOperacion == "0") {
            recargarTablaGrupo5(listaNueva);
            } else if (codigoOperacion == "1") {
                jsp_alerta("A", '<%=String.format(meLanbide47I18n.getMensaje(idiomaUsuario,"error.refrescarPestana.errorBD"), meLanbide47I18n.getMensaje(idiomaUsuario, "label.trayectoria21.tituloPestana.grupo5"))%>');
            } else if (codigoOperacion == "2") {
                jsp_alerta("A", '<%=String.format(meLanbide47I18n.getMensaje(idiomaUsuario,"error.refrescarPestana.errorGen"), meLanbide47I18n.getMensaje(idiomaUsuario, "label.trayectoria21.tituloPestana.grupo5"))%>');
            } else if (codigoOperacion == "3") {
                jsp_alerta("A", '<%=String.format(meLanbide47I18n.getMensaje(idiomaUsuario,"error.refrescarPestana.pasoParametros"), meLanbide47I18n.getMensaje(idiomaUsuario, "label.trayectoria21.tituloPestana.grupo5"))%>');
            } else {
                jsp_alerta("A", '<%=String.format(meLanbide47I18n.getMensaje(idiomaUsuario,"error.refrescarPestana.errorGen"), meLanbide47I18n.getMensaje(idiomaUsuario, "label.trayectoria21.tituloPestana.grupo5"))%>');
            }
        } catch (Err) {
            jsp_alerta("A", '<%=String.format(meLanbide47I18n.getMensaje(idiomaUsuario,"error.refrescarPestana.errorGen"), meLanbide47I18n.getMensaje(idiomaUsuario, "label.trayectoria21.tituloPestana.grupo5"))%>');
        }//try-catch
    }
 
    function validarGrupo5() {
        var urlParam = 'tarea=preparar&modulo=MELANBIDE47&operacion=validarPestanaTrayectoria21&tipo=0&numero=<%=numExpediente%>&idGrupo=5';
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
                error: mostrarMensajeRespuesta5
            });
        } catch (Err) {
            mostrarMensajeRespuesta5();
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
                refrescarPestanaGrupo5();
                refrescarPestanaResumen();
                refrescarPestanasORI14(3);
            }
            mostrarMensajeRespuesta5(parseInt(datos.error));
        } else {
            mostrarMensajeRespuesta5();
        }
    }

    function mostrarMensajeRespuesta5(codigo) {
        var msgtitle = "ERROR EN EL PROCESO";
        var mensaje;
        switch (codigo) {
            case 0:
                msgtitle = "PROCESO CORRECTO";
                mensaje = '<%=meLanbide47I18n.getMensaje(idiomaUsuario,"msg.datosGuardadosOK")%>';
                break;
            case 1:
                mensaje = '<%=String.format(meLanbide47I18n.getMensaje(idiomaUsuario,"error.refrescarPestana.errorBD"), meLanbide47I18n.getMensaje(idiomaUsuario, "label.trayectoria21.tituloPestana.grupo5"))%>';
                break;
            case 2:
                mensaje = '<%=String.format(meLanbide47I18n.getMensaje(idiomaUsuario,"error.refrescarPestana.errorGen"), meLanbide47I18n.getMensaje(idiomaUsuario, "label.trayectoria21.tituloPestana.grupo5"))%>';
                break;
            case 3:
                mensaje = '<%=String.format(meLanbide47I18n.getMensaje(idiomaUsuario,"error.refrescarPestana.pasoParametros"), meLanbide47I18n.getMensaje(idiomaUsuario, "label.trayectoria21.tituloPestana.grupo5"))%>';
                break;
            default:
                mensaje = '<%=String.format(meLanbide47I18n.getMensaje(idiomaUsuario,"error.refrescarPestana.errorBD"), meLanbide47I18n.getMensaje(idiomaUsuario, "label.trayectoria21.tituloPestana.grupo5"))%>';
        }
        botones(false);
        elementoVisible('off', 'barraProgresoTrayectoria');
        jsp_alerta("A", mensaje, msgtitle);
    }

    function botones(estado) {
        document.getElementById('botonAnadirGrupo5').disabled = estado;
        document.getElementById('botonModificarGrupo5').disabled = estado;
        document.getElementById('botonEliminarGrupo5').disabled = estado;
        document.getElementById('botonValidarGrupo5').disabled = estado;
    }
    
    function extraerNodosGrupo5(nodos) {
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
<body>
    <fieldset id="fieldsetGrupo5" style="height: 350px; margin-top: 20px; "> 
        <div>
            <div id="datosEntidad5" style="text-align: center; background-color: #4B95D3; height: 20px;"title="<%=nombreEntidad%>">
                <div style="overflow: hidden; text-overflow: ellipsis; white-space: nowrap; width: 100%;">
                    <%=nombreCompleto%>
                </div>
            </div>
            <div style="clear: both; overflow-x: hidden; overflow-y: auto; margin-top: 10px;"> <!--463px-->                   
                <label class="legendAzul"><%=meLanbide47I18n.getMensaje(idiomaUsuario,"legend.trayectoria21.grupo5.textoLargo")%></label>
                <div id="divTablaGrupo5" align="center">
                    <div id="divGeneral" style="clear: both; overflow-x: hidden; overflow-y: auto; height: 280px;">
                        <div id="listaGrupo5" style="padding: 5px; width:98%; font-size: 13px; height: 220px; text-align: center; overflow-x: auto; overflow-y: auto;margin:0px;margin-top:0px;" align="center"></div> 
                        <div class="botonera" style="padding-top: 10px;">
                            <input type="button" id="botonAnadirGrupo5" name="botonAnadirGrupo5" class="botonGeneral" value="<%=meLanbide47I18n.getMensaje(idiomaUsuario,"btn.anadir")%>" onclick="anadirGrupo5();"/>
                            <input type="button" id="botonModificarGrupo5" name="botonModificarGrupo5" class="botonGeneral" value="<%=meLanbide47I18n.getMensaje(idiomaUsuario,"btn.modificar")%>" onclick="modificarGrupo5();"/>
                            <input type="button" id="botonEliminarGrupo5" name="botonEliminarGrupo5" class="botonGeneral" value="<%=meLanbide47I18n.getMensaje(idiomaUsuario,"btn.eliminar")%>" onclick="eliminarGrupo5();"/>
                            <input type="button" id="botonValidarGrupo5" name="botonValidarGrupo5" class="botonMasLargo" value="<%=meLanbide47I18n.getMensaje(idiomaUsuario,"btn.copiarDatos")%>" onclick="validarGrupo5();"/>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </fieldset>
</body>
<script type="text/javascript">
//  -----------------  GRUPO 5
    var tablaGrupo5;
    var listaGrupo5 = new Array();
    var listaGrupo5Tabla = new Array();
    crearTablaGrupo5();
    <%
        OriTrayectoriaEntidadVO fila5= null;
        if(listaGrupo5!=null){
            DateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
            for(int i = 0;i <listaGrupo5.size();i++)
            {
                fila5 = listaGrupo5.get(i);
                
                String fecIni="";
                if (fila5.getFechaInicio()!=null) {
                    fecIni=formatoFecha.format(fila5.getFechaInicio());
                } else {
                    fecIni="-";
                }
                String fecIniVal="";
                if (fila5.getFechaInicioVal()!=null) {
                    fecIniVal=formatoFecha.format(fila5.getFechaInicioVal());
                } else {
                    fecIniVal="-";
                }
                String fecFin="";
                if (fila5.getFechaFin()!=null) {
                    fecFin=formatoFecha.format(fila5.getFechaFin());
                } else {
                    fecFin="-";
                }
                String fecFinVal="";
                if (fila5.getFechaFinVal()!=null) {
                    fecFinVal=formatoFecha.format(fila5.getFechaFinVal());
                } else {
                    fecFinVal="-";
                }
    %>
    listaGrupo5[<%=i%>] = ['<%=fila5.getIdTrayEntidad()%>', '<%=fila5.getIdConActGrupo()%>', '<%=fila5.getIdConActSubgrupo()%>', '<%=fila5.getCodEntidad()%>', '<%=fila5.getDescActividad()%>', '<%=fecIni%>', '<%=fecFin%>', '<%=fila5.getNumMeses()%>', '<%=fecIniVal%>', '<%=fecFinVal%>', '<%=fila5.getNumMesesVal()%>', '<%=fila5.getCifEntidad()%>', '<%=fila5.getNombreEntidad()%>'];
    listaGrupo5Tabla[<%=i%>] = ['<%=fila5.getDescActividad()%>', '<%=fecIni%>', '<%=fecFin%>', '<%=fila5.getNumMeses()%>', '<%=fecIniVal%>', '<%=fecFinVal%>', '<%=(fila5.getNumMesesVal() !=null ? fila5.getNumMesesVal() : "-")%>'];
    <%
            }
        }
    %>
    tablaGrupo5.lineas = listaGrupo5Tabla;
    tablaGrupo5.displayCabecera = true;
    tablaGrupo5.height = 250;
    tablaGrupo5.displayTabla();
</script>

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
    List<OriTrayectoriaEntidadVO> listaGrupo6 = (List<OriTrayectoriaEntidadVO>)request.getAttribute("listaGrupo6");
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

    function anadirGrupo6() {
        var result = null;
        lanzarPopUpModal('<%=request.getContextPath()%>/PeticionModuloIntegracion.do?tarea=preparar&modulo=MELANBIDE47&operacion=cargarAltaEdicionTrayectoria21&tipo=0&modoDatos=0&numero=<%=numExpediente%>&nombre=<%=nombreEntidad%>&idGrupo=6&codEntidad=<%=codEntidad%>', 500, 980, 'no', 'no', function (result) {
                    if (result != undefined) {
                        if (result[0] == '0') {
                            recargarTablaGrupo6(result);
                            refrescarPestanaTrayectoria_V();
                        }
                    }
                });
            }

    function modificarGrupo6() {
        var result = null;
        var idTrayEntidad = "";
        if ((tablaGrupo6.selectedIndex != -1)) {
            idTrayEntidad = listaGrupo6[tablaGrupo6.selectedIndex][0];
            lanzarPopUpModal('<%=request.getContextPath()%>/PeticionModuloIntegracion.do?tarea=preparar&modulo=MELANBIDE47&operacion=cargarAltaEdicionTrayectoria21&tipo=0&modoDatos=1&numero=<%=numExpediente%>&idTrayEntidad=' + idTrayEntidad + '&idGrupo=6&codEntidad= <%=codEntidad%>', 500, 980, 'no', 'no', function (result) {
                if (result != undefined) {
                    if (result[0] == '0') {
                        recargarTablaGrupo6(result);
                        refrescarPestanaTrayectoria_V();
                    }
                }
            });
        } else {
            jsp_alerta('A', '<%=meLanbide47I18n.getMensaje(idiomaUsuario, "msg.msjNoSelecFila")%>');
        }
    }

    function eliminarGrupo6() {
        var result = null;
        var idActividad = "";
        var codEntidad = "";
        if ((tablaGrupo6.selectedIndex != -1)) {
            idActividad = listaGrupo6[tablaGrupo6.selectedIndex][0];
            codEntidad = listaGrupo6[tablaGrupo6.selectedIndex][3];
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
                        + '&idGrupo=6'
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
                    var listaDatosRespuesta = extraerNodosGrupo6(nodos);
                    var codigoOperacion = listaDatosRespuesta[0];
                    if (codigoOperacion == "0") {
                        jsp_alerta("A", '<%=meLanbide47I18n.getMensaje(idiomaUsuario,"msg.registroEliminadoOK")%>');
                        if (listaDatosRespuesta != null && listaDatosRespuesta != undefined) {
                            if (listaDatosRespuesta[0] == '0') {
                                recargarTablaGrupo6(listaDatosRespuesta);
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

    function recargarTablaGrupo6(result) {
        var codOperacion = result != null ? result[0] : null;
        var fila6;
        if (codOperacion != null) {
            listaGrupo6 = new Array();
            listaGrupo6Tabla = new Array();
            for (var i = 1; i < result.length; i++) {
                fila6 = result[i];
                listaGrupo6[i - 1] = [fila6[0], fila6[1], fila6[2], fila6[3], fila6[4], fila6[5], fila6[6], fila6[7], fila6[8], fila6[9], fila6[10], fila6[11], fila6[12], fila6[13], fila6[14]];
                listaGrupo6Tabla[i - 1] = [fila6[4], fila6[6], fila6[7], fila6[8], (fila6[10] != null && fila6[10] != "null" ? fila6[10] : "-"), (fila6[11] != null && fila6[11] != "null" ? fila6[11] : "-"), (fila6[12] != null && fila6[12] != "null" ? fila6[12] : "-")];
            }
            crearTablaGrupo6();
            tablaGrupo6.lineas = listaGrupo6Tabla;
            tablaGrupo6.displayCabecera = true;
            tablaGrupo6.height = 250;
            tablaGrupo6.displayTabla();
        } else {
            alert('Datos NO Guardados Correctamente..!!');
        }
    }

    function crearTablaGrupo6() {
        tablaGrupo6 = new TablaOri(true, '<%=descriptor.getDescripcion("buscar")%>', '<%=descriptor.getDescripcion("anterior")%>', '<%=descriptor.getDescripcion("siguiente")%>', '<%=descriptor.getDescripcion("mosFilasPag")%>', '<%=descriptor.getDescripcion("msgNoResultBusq")%>', '<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>', '<%=descriptor.getDescripcion("filtrDeTotal")%>', '<%=descriptor.getDescripcion("primero")%>', '<%=descriptor.getDescripcion("ultimo")%>', document.getElementById('listaGrupo6'), 1200, 25);
        tablaGrupo6.addColumna('600', 'left', "<%= meLanbide47I18n.getMensaje(idiomaUsuario,"label.trayectoria21.tabla.programa")%>");
        tablaGrupo6.addColumna('100', 'center', "<%= meLanbide47I18n.getMensaje(idiomaUsuario,"label.trayectoria21.tabla.fechaIni")%>");
        tablaGrupo6.addColumna('100', 'center', "<%= meLanbide47I18n.getMensaje(idiomaUsuario,"label.trayectoria21.tabla.fechaFin")%>");
        tablaGrupo6.addColumna('100', 'center', "<%= meLanbide47I18n.getMensaje(idiomaUsuario,"label.trayectoria21.tabla.duracion")%>");
        tablaGrupo6.addColumna('100', 'center', "<%= meLanbide47I18n.getMensaje(idiomaUsuario,"label.trayectoria21.tabla.fechaIniVal")%>");
        tablaGrupo6.addColumna('100', 'center', "<%= meLanbide47I18n.getMensaje(idiomaUsuario,"label.trayectoria21.tabla.fechaFinVal")%>");
        tablaGrupo6.addColumna('100', 'center', "<%= meLanbide47I18n.getMensaje(idiomaUsuario,"label.trayectoria21.tabla.duracionVal")%>");
    }

    function refrescarPestanaGrupo6() {
        var ajax = getXMLHttpRequest();
        var nodos = null;
        var CONTEXT_PATH = '<%=request.getContextPath()%>'
        var url = CONTEXT_PATH + "/PeticionModuloIntegracion.do";
        var parametros = "";
        parametros = 'tarea=preparar&modulo=MELANBIDE47&operacion=refrescarPestanaTrayectoria21&tipo=0&numero=<%=numExpediente%>&idGrupo=6&codEntidad=<%=codEntidad%>';
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
            var listaNueva = extraerNodosGrupo6(nodos);
            var codigoOperacion = listaNueva[0];
            if (codigoOperacion == "0") {
            recargarTablaGrupo6(listaNueva);
            } else if (codigoOperacion == "1") {
                jsp_alerta("A", '<%=String.format(meLanbide47I18n.getMensaje(idiomaUsuario,"error.refrescarPestana.errorBD"), meLanbide47I18n.getMensaje(idiomaUsuario, "label.trayectoria21.tituloPestana.grupo6"))%>');
            } else if (codigoOperacion == "2") {
                jsp_alerta("A", '<%=String.format(meLanbide47I18n.getMensaje(idiomaUsuario,"error.refrescarPestana.errorGen"), meLanbide47I18n.getMensaje(idiomaUsuario, "label.trayectoria21.tituloPestana.grupo6"))%>');
            } else if (codigoOperacion == "3") {
                jsp_alerta("A", '<%=String.format(meLanbide47I18n.getMensaje(idiomaUsuario,"error.refrescarPestana.pasoParametros"), meLanbide47I18n.getMensaje(idiomaUsuario, "label.trayectoria21.tituloPestana.grupo6"))%>');
            } else {
                jsp_alerta("A", '<%=String.format(meLanbide47I18n.getMensaje(idiomaUsuario,"error.refrescarPestana.errorGen"), meLanbide47I18n.getMensaje(idiomaUsuario, "label.trayectoria21.tituloPestana.grupo6"))%>');
            }
        } catch (Err) {
            jsp_alerta("A", '<%=String.format(meLanbide47I18n.getMensaje(idiomaUsuario,"error.refrescarPestana.errorGen"), meLanbide47I18n.getMensaje(idiomaUsuario, "label.trayectoria21.tituloPestana.grupo6"))%>');
        }//try-catch
    }
    

    function validarGrupo6() {
        var urlParam = 'tarea=preparar&modulo=MELANBIDE47&operacion=validarPestanaTrayectoria21&tipo=0&numero=<%=numExpediente%>&idGrupo=6';
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
                error: mostrarMensajeRespuesta6
            });
        } catch (Err) {
            mostrarMensajeRespuesta6();
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
                refrescarPestanaGrupo6();
                refrescarPestanaResumen();
                refrescarPestanasORI14(3);
            }
            mostrarMensajeRespuesta6(parseInt(datos.error));
        } else {
            mostrarMensajeRespuesta6();
        }
    }

    function mostrarMensajeRespuesta6(codigo) {
        var msgtitle = "ERROR EN EL PROCESO";
        var mensaje;
        switch (codigo) {
            case 0:
                msgtitle = "PROCESO CORRECTO";
                mensaje = '<%=meLanbide47I18n.getMensaje(idiomaUsuario,"msg.datosGuardadosOK")%>';
                break;
            case 1:
                mensaje = '<%=String.format(meLanbide47I18n.getMensaje(idiomaUsuario,"error.refrescarPestana.errorBD"), meLanbide47I18n.getMensaje(idiomaUsuario, "label.trayectoria21.tituloPestana.grupo6"))%>';
                break;
            case 2:
                mensaje = '<%=String.format(meLanbide47I18n.getMensaje(idiomaUsuario,"error.refrescarPestana.errorGen"), meLanbide47I18n.getMensaje(idiomaUsuario, "label.trayectoria21.tituloPestana.grupo6"))%>';
                break;
            case 3:
                mensaje = '<%=String.format(meLanbide47I18n.getMensaje(idiomaUsuario,"error.refrescarPestana.pasoParametros"), meLanbide47I18n.getMensaje(idiomaUsuario, "label.trayectoria21.tituloPestana.grupo6"))%>';
                break;
            default:
                mensaje = '<%=String.format(meLanbide47I18n.getMensaje(idiomaUsuario,"error.refrescarPestana.errorBD"), meLanbide47I18n.getMensaje(idiomaUsuario, "label.trayectoria21.tituloPestana.grupo6"))%>';
        }
        botones(false);
        elementoVisible('off', 'barraProgresoTrayectoria');
        jsp_alerta("A", mensaje, msgtitle);
    }

    function botones(estado) {
        document.getElementById('botonAnadirGrupo6').disabled = estado;
        document.getElementById('botonModificarGrupo6').disabled = estado;
        document.getElementById('botonEliminarGrupo6').disabled = estado;
        document.getElementById('botonValidarGrupo6').disabled = estado;
    }
    
    function extraerNodosGrupo6(nodos) {
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
    <fieldset id="fieldsetGrupo6" style="height: 350px; margin-top: 20px; "> 
        <div>
            <div id="datosEntidad" style="text-align: center; background-color: #4B95D3; height: 20px;"title="<%=nombreEntidad%>">
                <div style="overflow: hidden; text-overflow: ellipsis; white-space: nowrap; width: 100%;">
                    <%=nombreCompleto%>
                </div>
            </div>
            <div style="clear: both; overflow-x: hidden; overflow-y: auto; margin-top: 10px;"> <!--463px-->                   
                <label class="legendAzul"><%=meLanbide47I18n.getMensaje(idiomaUsuario,"legend.trayectoria21.grupo6.textoLargo")%></label>
                <div id="divTablaGrupo6" align="center">
                    <div id="divGeneral" style="clear: both; overflow-x: hidden; overflow-y: auto; height: 280px;">
                        <div id="listaGrupo6" style="padding: 5px; width:98%; font-size: 13px; height: 220px; text-align: center; overflow-x: auto; overflow-y: auto;margin:0px;margin-top:0px;" align="center"></div> 
                        <div class="botonera" style="padding-top: 10px;">
                            <input type="button" id="botonAnadirGrupo6" name="botonAnadirGrupo6" class="botonGeneral" value="<%=meLanbide47I18n.getMensaje(idiomaUsuario,"btn.anadir")%>" onclick="anadirGrupo6();"/>
                            <input type="button" id="botonModificarGrupo6" name="botonModificarGrupo6" class="botonGeneral" value="<%=meLanbide47I18n.getMensaje(idiomaUsuario,"btn.modificar")%>" onclick="modificarGrupo6();"/>
                            <input type="button" id="botonEliminarGrupo6" name="botonEliminarGrupo6" class="botonGeneral" value="<%=meLanbide47I18n.getMensaje(idiomaUsuario,"btn.eliminar")%>" onclick="eliminarGrupo6();"/>
                            <input type="button" id="botonValidarGrupo6" name="botonValidarGrupo6" class="botonMasLargo" value="<%=meLanbide47I18n.getMensaje(idiomaUsuario,"btn.copiarDatos")%>" onclick="validarGrupo6();"/>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </fieldset>
</body>
<script type="text/javascript">
//  -----------------  GRUPO 6
    var tablaGrupo6;
    var listaGrupo6 = new Array();
    var listaGrupo6Tabla = new Array();
    crearTablaGrupo6();
    <%
        OriTrayectoriaEntidadVO fila6= null;
        if(listaGrupo6!=null){
            DateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
            for(int i = 0;i <listaGrupo6.size();i++)
            {
                fila6 = listaGrupo6.get(i);
                
                String fecIni="";
                if (fila6.getFechaInicio()!=null) {
                    fecIni=formatoFecha.format(fila6.getFechaInicio());
                } else {
                    fecIni="-";
                }
                String fecIniVal="";
                if (fila6.getFechaInicioVal()!=null) {
                    fecIniVal=formatoFecha.format(fila6.getFechaInicioVal());
                } else {
                    fecIniVal="-";
                }
                String fecFin="";
                if (fila6.getFechaFin()!=null) {
                    fecFin=formatoFecha.format(fila6.getFechaFin());
                } else {
                    fecFin="-";
                }
                String fecFinVal="";
                if (fila6.getFechaFinVal()!=null) {
                    fecFinVal=formatoFecha.format(fila6.getFechaFinVal());
                } else {
                    fecFinVal="-";
                }
    %>
    listaGrupo6[<%=i%>] = ['<%=fila6.getIdTrayEntidad()%>', '<%=fila6.getIdConActGrupo()%>', '<%=fila6.getIdConActSubgrupo()%>', '<%=fila6.getCodEntidad()%>', '<%=fila6.getDescActividad()%>', '<%=fecIni%>', '<%=fecFin%>', '<%=fila6.getNumMeses()%>', '<%=fecIniVal%>', '<%=fecFinVal%>', '<%=fila6.getNumMesesVal()%>', '<%=fila6.getCifEntidad()%>', '<%=fila6.getNombreEntidad()%>'];
    listaGrupo6Tabla[<%=i%>] = ['<%=fila6.getDescActividad()%>', '<%=fecIni%>', '<%=fecFin%>', '<%=fila6.getNumMeses()%>', '<%=fecIniVal%>', '<%=fecFinVal%>', '<%=(fila6.getNumMesesVal() !=null ? fila6.getNumMesesVal() : "-")%>'];
    <%
            }
        }
    %>
    tablaGrupo6.lineas = listaGrupo6Tabla;
    tablaGrupo6.displayCabecera = true;
    tablaGrupo6.height = 250;
    tablaGrupo6.displayTabla();
</script>

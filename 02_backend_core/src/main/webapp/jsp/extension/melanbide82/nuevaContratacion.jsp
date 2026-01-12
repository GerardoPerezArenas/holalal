<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>

<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide82.i18n.MeLanbide82I18n" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide82.vo.FilaContratacionVO" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide82.vo.DesplegableAdmonLocalVO" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide82.util.ConstantesMeLanbide82" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide82.util.ConfigurationParameter" %>
<%@page import="java.util.ArrayList" %>
<%@page import="java.util.List" %>
<%@page import="java.text.SimpleDateFormat" %>
<!doctype html>
<html lang="es">
    <%@page contentType="text/html; charset=utf-8"%>
    <head>
        <title>Mantenimiento de Contrataciones</title>
        <meta http-equiv="X-UA-Compatible" content="IE=Edge" />
        <!-- <meta charset="utf-8" /> -->
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <%
            FilaContratacionVO datModif = new FilaContratacionVO();
        
            String codOrganizacion = "";
            String nuevo = "";
            String fechaNacimientoInicio = "";
            String fechaInicioInicio = "";
            String fechaInicioFin = "";
            String fechaFinFin = "";

            String expediente = "";
   
            MeLanbide82I18n meLanbide82I18n = MeLanbide82I18n.getInstance();

            expediente = (String)request.getAttribute("numExp");
            int idiomaUsuario = 1;
            int apl = 5;
            String css = "";
            try {
                UsuarioValueObject usuario = new UsuarioValueObject();
                try {
                    if (session != null) {
                        if (usuario != null) {
                            usuario = (UsuarioValueObject) session.getAttribute("usuario");
                            idiomaUsuario = usuario.getIdioma();
                            apl = usuario.getAppCod();
                            css = usuario.getCss();
                        }
                    }
                } catch(Exception ex) {
                }

                codOrganizacion  = request.getParameter("codOrganizacionModulo");
                nuevo = (String)request.getAttribute("nuevo");
                if(request.getAttribute("datModif") != null) {
                    datModif = (FilaContratacionVO)request.getAttribute("datModif");
                    SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
                    fechaNacimientoInicio = formatoFecha.format(datModif.getFechanacimientoInicio());
                    fechaInicioInicio = formatoFecha.format(datModif.getFechainicioInicio());
                    fechaInicioFin = formatoFecha.format(datModif.getFechainicioFin());
                    fechaFinFin = formatoFecha.format(datModif.getFechafinFin());
                }
            }
            catch(Exception ex)
            {
            }
        %>
        <jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
        <jsp:setProperty name="descriptor"  property="idi_cod" value="<%=idiomaUsuario%>" />
        <jsp:setProperty name="descriptor"  property="apl_cod" value="<%=apl%>" />
        <link rel="StyleSheet" media="screen" type="text/css" href="<%=request.getContextPath()%><%=css%>">
        <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/css/font-awesome.min.css'/>" media="screen">
        <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/less/animated.less'/>" media="screen">
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script>
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/>
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/extension/melanbide82/melanbide82.css"/>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/general.js"></script>
        <script type="text/javascript" src="<c:url value='/scripts/listaComboBox.js'/>"></script>
        <script type="text/javascript" src="<c:url value='/scripts/calendario.js'/>"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/melanbide82/GelUtils.js"></script>
        <script type="text/javascript">
            var APP_CONTEXT_PATH = '<%=request.getContextPath()%>';
            var url = APP_CONTEXT_PATH + '/PeticionModuloIntegracion.do';
            var mensajeValidacion = '';

            var comboListaGrupoCotizacion;
            var listaCodigosGrupoCotizacion = new Array();
            var listaDescripcionesGrupoCotizacion = new Array();
            function buscaCodigoGrupoCotizacion(codGrupoCotizacion) {
                comboListaGrupoCotizacion.buscaCodigo(codGrupoCotizacion);
            }
            function cargarDatosGrupoCotizacion() {
                var codGrupoCotizacionSeleccionado = document.getElementById("codListaGrupoCotizacion").value;
                buscaCodigoGrupoCotizacion(codGrupoCotizacionSeleccionado);
            }

            var comboListaGrupoCotizacionInicio;
            var listaCodigosGrupoCotizacionInicio = new Array();
            var listaDescripcionesGrupoCotizacionInicio = new Array();
            function buscaCodigoGrupoCotizacionInicio(codGrupoCotizacionInicio) {
                comboListaGrupoCotizacionInicio.buscaCodigo(codGrupoCotizacionInicio);
            }
            function cargarDatosGrupoCotizacionInicio() {
                var codGrupoCotizacionSeleccionado = document.getElementById("codListaGrupoCotizacionInicio").value;
                buscaCodigoGrupoCotizacionInicio(codGrupoCotizacionSeleccionado);
            }

            var comboListaGrupoCotizacionFin;
            var listaCodigosGrupoCotizacionFin = new Array();
            var listaDescripcionesGrupoCotizacionFin = new Array();
            function buscaCodigoGrupoCotizacionFin(codGrupoCotizacionFin) {
                comboListaGrupoCotizacionFin.buscaCodigo(codGrupoCotizacionFin);
            }
            function cargarDatosGrupoCotizacionFin() {
                var codGrupoCotizacionSeleccionado = document.getElementById("codListaGrupoCotizacionFin").value;
                buscaCodigoGrupoCotizacionFin(codGrupoCotizacionSeleccionado);
            }

            var comboListaNivel;
            var listaCodigosNivel = new Array();
            var listaDescripcionesNivel = new Array();
            function buscaCodigoNivel(codNivel) {
                comboListaNivel.buscaCodigo(codNivel);
            }
            function cargarDatosNivel() {
                var codNivelSeleccionado = document.getElementById("codListaNivel").value;
                buscaCodigoNivel(codNivelSeleccionado);
            }

            var comboListaNivelInicio;
            var listaCodigosNivelInicio = new Array();
            var listaDescripcionesNivelInicio = new Array();
            function buscaCodigoNivelInicio(codNivelInicio) {
                comboListaNivelInicio.buscaCodigo(codNivelInicio);
            }
            function cargarDatosNivelInicio() {
                var codNivelInicioSeleccionado = document.getElementById("codListaNivelInicio").value;
                buscaCodigoNivelInicio(codNivelInicioSeleccionado);
            }
            var comboListaSexoInicio;
            var listaCodigosSexoInicio = new Array();
            var listaDescripcionesSexoInicio = new Array();
            function buscaCodigoSexoInicio(tipo) {
                comboListaSexoInicio.buscaCodigo(tipo);
            }
            function cargarDatosSexoInicio() {
                var tipoSeleccionado = document.getElementById("codListaSexoInicio").value;
                buscaCodigoSexoInicio(tipoSeleccionado);
            }

            var comboListaSexoFin;
            var listaCodigosSexoFin = new Array();
            var listaDescripcionesSexoFin = new Array();
            function buscaCodigoSexoFin(tipo) {
                comboListaSexoFin.buscaCodigo(tipo);
            }
            function cargarDatosSexoFin() {
                var tipoSeleccionado = document.getElementById("codListaSexoFin").value;
                buscaCodigoSexoFin(tipoSeleccionado);
            }

            function rellenarDesplegables() {
                if ('<%=datModif%>' != null) {
                    buscaCodigoNivel('<%=datModif.getNivelCualificacion() != null ? datModif.getNivelCualificacion() : ""%>');
                    buscaCodigoNivelInicio('<%=datModif.getNivelcualificacionInicio() != null ? datModif.getNivelcualificacionInicio() : ""%>');
                    buscaCodigoGrupoCotizacion('<%=datModif.getGrupoCotiz() != null ? datModif.getGrupoCotiz() : ""%>');
                    buscaCodigoGrupoCotizacionInicio('<%=datModif.getGrupocotizInicio() != null ? datModif.getGrupocotizInicio() : ""%>');
                    buscaCodigoGrupoCotizacionFin('<%=datModif.getGrupocotizFin() != null ? datModif.getGrupocotizFin() : ""%>');
                    buscaCodigoSexoInicio('<%=datModif.getSexoInicio() != null ? datModif.getSexoInicio() : ""%>');
                    buscaCodigoSexoFin('<%=datModif.getSexoFin() != null ? datModif.getSexoFin() : ""%>');
                }// else
                // alert('No hemos podido cargar los datos para modificar');
            }

            function guardarDatos() {
                if (validarDatos()) {
                    var ajax = getXMLHttpRequest();
                    var nodos = null;
                    var parametros = "";
                    var llamar = true;
                    if ('<%=nuevo%>' != null && '<%=nuevo%>' == '1') {
                        parametros = "tarea=preparar&modulo=MELANBIDE82&operacion=comprobarPrioridad&tipo=0&expediente=<%=expediente%>&prioridad=" + document.getElementById('prioridad').value;
                        try {
                            ajax.open("POST", url, false);
                            //ajax.setRequestHeader("Content-Type", "application/x-www-form-urlencoded; charset=ISO-8859-1");
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
                            var respuesta = nodos[0].childNodes[0].childNodes[0].nodeValue;
                            if (respuesta != '0') {
                                llamar = false;
                            }
                        } catch (Err) {
                        }

                        parametros = "tarea=preparar&modulo=MELANBIDE82&operacion=crearNuevaContratacion&tipo=0";
                    } else {
                        parametros = "tarea=preparar&modulo=MELANBIDE82&operacion=modificarContratacion&tipo=0"
                                + "&id=<%=datModif != null && datModif.getId() != null ? datModif.getId().toString() : ""%>";
                    }
                    if (llamar) {
                        parametros += "&expediente=<%=expediente%>"
                                + "&prioridad=" + document.getElementById('prioridad').value
                                + "&denomPuesto=" + document.getElementById('denomPuesto').value.replace(/\r?\n|\r/g, " ").trim()
                                + "&nivelCualificacion=" + (document.getElementById('codListaNivel').value != null && document.getElementById('codListaNivel').value != "" ? document.getElementById('codListaNivel').value : "")
                                + "&modContrato=" + document.getElementById('modContrato').value
                                + "&durContrato=" + document.getElementById('durContrato').value
                                + "&grupoCotiz=" + document.getElementById('codListaGrupoCotizacion').value
                                + "&costeSalarial=" + document.getElementById('costeSalarial').value
                                + "&subvSolicitada=" + document.getElementById('subvSolicitada').value
                                //INICIO
                                + "&municipioInicio=" + document.getElementById('municipioInicio').value
                                + "&dniNieInicio=" + document.getElementById('dniNieInicio').value
                                + "&nombreInicio=" + document.getElementById('nombreInicio').value
                                + "&apellido1Inicio=" + document.getElementById('apellido1Inicio').value
                                + "&apellido2Inicio=" + document.getElementById('apellido2Inicio').value
                                + "&edadInicio=" + document.getElementById('edadInicio').value
                                + "&sexoInicio=" + (document.getElementById('codListaSexoInicio').value != null && document.getElementById('codListaSexoInicio').value != "" ? document.getElementById('codListaSexoInicio').value : "")
                                + "&grupoCotizInicio=" + (document.getElementById('codListaGrupoCotizacionInicio').value != null && document.getElementById('codListaGrupoCotizacionInicio').value != "" ? document.getElementById('codListaGrupoCotizacionInicio').value : "")
                                + "&fechaNacimientoInicio=" + (document.getElementById('fechaNacimientoInicio').value != null && document.getElementById('fechaNacimientoInicio').value != "" ? document.getElementById('fechaNacimientoInicio').value : "")
                                + "&nivelCualificacionInicio=" + (document.getElementById('codListaNivelInicio').value != null && document.getElementById('codListaNivelInicio').value != "" ? document.getElementById('codListaNivelInicio').value : "")
                                + "&puestoTrabajoInicio=" + document.getElementById('puestoTrabajoInicio').value.replace(/\r?\n|\r/g, " ").trim()
                                + "&nOfertaInicio=" + document.getElementById('nOfertaInicio').value
                                + "&retribucionBrutaInicio=" + document.getElementById('retribucionBrutaInicio').value
                                + "&fechaInicioInicio=" + document.getElementById('fechaInicioInicio').value
                                + "&durContratoInicio=" + document.getElementById('durContratoInicio').value
                                //FIN
                                + "&municipioFin=" + document.getElementById('municipioFin').value
                                + "&dniNieFin=" + document.getElementById('dniNieFin').value
                                + "&nombreFin=" + document.getElementById('nombreFin').value
                                + "&apellido1Fin=" + document.getElementById('apellido1Fin').value
                                + "&apellido2Fin=" + document.getElementById('apellido2Fin').value
                                + "&sexoFin=" + (document.getElementById('codListaSexoFin').value != null && document.getElementById('codListaSexoFin').value != "" ? document.getElementById('codListaSexoFin').value : "")
                                + "&grupoCotizFin=" + (document.getElementById('codListaGrupoCotizacionFin').value != null && document.getElementById('codListaGrupoCotizacionFin').value != "" ? document.getElementById('codListaGrupoCotizacionFin').value : "")
                                + "&fechaInicioFin=" + document.getElementById('fechaInicioFin').value
                                + "&fechaFinFin=" + document.getElementById('fechaFinFin').value
                                + "&durContratoFin=" + document.getElementById('durContratoFin').value
                                + "&retribucionBrutaFin=" + document.getElementById('retribucionBrutaFin').value
                                + "&costeSalarialFin=" + document.getElementById('costeSalarialFin').value
                                + "&indemFinContratoFin=" + document.getElementById('indemFinContratoFin').value
                                + "&costesSsFin=" + document.getElementById('costesSsFin').value
                                + "&costeTotalRealFin=" + document.getElementById('costeTotalRealFin').value
                                + "&subvConcedidaFin=" + document.getElementById('subvConcedidaFin').value
                                ;
                        try {
                            ajax.open("POST", url, false);
                            //ajax.setRequestHeader("Content-Type", "application/x-www-form-urlencoded; charset=ISO-8859-1");
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
                            var lista = extraerListaContrataciones(nodos);
                            var codigoOperacion = lista[0];

                            if (codigoOperacion == "0") {
                                //jsp_alerta("A",'Correcto');
                                self.parent.opener.retornoXanelaAuxiliar(lista);
                                cerrarVentana();
                            } else if (codigoOperacion == "1") {
                                jsp_alerta("A", '<%=meLanbide82I18n.getMensaje(idiomaUsuario,"error.errorBD")%>');
                            } else if (codigoOperacion == "2") {
                                jsp_alerta("A", '<%=meLanbide82I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                            } else if (codigoOperacion == "3") {
                                jsp_alerta("A", '<%=meLanbide82I18n.getMensaje(idiomaUsuario,"error.pasoParametros")%>');
                            } else {
                                jsp_alerta("A", '<%=meLanbide82I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                            }
                        } catch (Err) {
                        }//try-catch
                    } else {
                        if (respuesta == "1") {
                            jsp_alerta("A", '<%=meLanbide82I18n.getMensaje(idiomaUsuario,"msg.prioridad.duplicada")%>');
                        } else {
                            jsp_alerta("A", '<%=meLanbide82I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                        }
                    }
                } else {
                    jsp_alerta("A", mensajeValidacion);
                }
            }

            function validarDatos() {
                mensajeValidacion = "";
                //CONTRATACION
                var campo = document.getElementById('prioridad');
                if (campo.value == null || campo.value == '') {
                    mensajeValidacion = '<%=meLanbide82I18n.getMensaje(idiomaUsuario, "msg.prioridad")%>';
                    return false;
                } else if (/^([0-9])*$/.test(campo)) {
                    mensajeValidacion = '<%=meLanbide82I18n.getMensaje(idiomaUsuario, "msg.prioridad.errNumerico")%>';
                    return false;
                }
                campo = document.getElementById('denomPuesto').value;
                if (campo == null || campo == '') {
                    mensajeValidacion = '<%=meLanbide82I18n.getMensaje(idiomaUsuario, "msg.denomPuesto")%>';
                    return false;
                }
                campo = document.getElementById('codListaNivel').value;
                if (campo == null || campo == '') {
                    mensajeValidacion = '<%=meLanbide82I18n.getMensaje(idiomaUsuario, "msg.nivelcualificacion")%>';
                    return false;
                }
                campo = document.getElementById('modContrato').value;
                if (campo == null || campo == '') {
                    mensajeValidacion = '<%=meLanbide82I18n.getMensaje(idiomaUsuario, "msg.modContrato")%>';
                    return false;
                }
                campo = document.getElementById('durContrato');
                if (campo.value == null || campo.value == '') {
                    mensajeValidacion = '<%=meLanbide82I18n.getMensaje(idiomaUsuario, "msg.durContrato")%>';
                    return false;
                } else if (/^([0-9])*$/.test(campo)) {
                    mensajeValidacion = '<%=meLanbide82I18n.getMensaje(idiomaUsuario, "msg.durContrato.errNumerico")%>';
                    return false;
                } else if (campo.value != 8 && campo.value != 12) {
                    mensajeValidacion = '<%=meLanbide82I18n.getMensaje(idiomaUsuario, "msg.durContrato.valor")%>';
                    return false;
                }
                campo = document.getElementById('codListaGrupoCotizacion').value;
                if (campo == null || campo == '') {
                    mensajeValidacion = '<%=meLanbide82I18n.getMensaje(idiomaUsuario, "msg.grupoCotiz")%>';
                    return false;
                }
                campo = document.getElementById('costeSalarial').value;
                if (campo == null || campo == '') {
                    mensajeValidacion = '<%=meLanbide82I18n.getMensaje(idiomaUsuario, "msg.costeSalarial")%>';
                    return false;
                } else if (!validarNumericoDecimalPrecision(campo, 9, 2)) {
                    mensajeValidacion = '<%=meLanbide82I18n.getMensaje(idiomaUsuario, "msg.costeSalarial.errNumerico")%>';
                    return false;
                }
                campo = document.getElementById('subvSolicitada').value;
                if (campo == null || campo == '') {
                    mensajeValidacion = '<%=meLanbide82I18n.getMensaje(idiomaUsuario, "msg.subvSolicitada")%>';
                    return false;
                } else if (!validarNumericoDecimalPrecision(campo, 9, 2)) {
                    mensajeValidacion = '<%=meLanbide82I18n.getMensaje(idiomaUsuario, "msg.subvSolicitada.errNumerico")%>';
                    return false;
                }
                /* PUEDE QUE SOLO HAYA DATOS EN LA 1ª TABLA, LOS CAMPOS DE INI Y FIN NO PUEDEN SER OBLIGATORIOS*/
                //CONTRATACION INICIO
                campo = document.getElementById('dniNieInicio');
                if (campo.value != null && campo.value != '') {
                    if (!validarDniNie(campo)) {
                        mensajeValidacion = '<%=meLanbide82I18n.getMensaje(idiomaUsuario, "msg.dni")%>';
                        return false;
                    }
                }
                campo = document.getElementById('edadInicio');
                if (campo.value != null && campo.value != '') {
                    if (/^([0-9])*$/.test(campo)) {
                        mensajeValidacion = '<%=meLanbide82I18n.getMensaje(idiomaUsuario, "msg.edad.errNumerico")%>';
                        return false;
                    }
                }
                campo = document.getElementById('retribucionBrutaInicio').value;
                if (campo != null && campo != '') {
                    if (!validarNumericoDecimalPrecision(campo, 9, 2)) {
                        mensajeValidacion = '<%=meLanbide82I18n.getMensaje(idiomaUsuario, "msg.retribucionBruta.errNumerico")%>';
                        return false;
                    }
                }
                campo = document.getElementById('durContratoInicio');
                if (campo.value != null && campo.value != '') {
                    if (/^([0-9])*$/.test(campo)) {
                        mensajeValidacion = '<%=meLanbide82I18n.getMensaje(idiomaUsuario, "msg.durContrato.errNumerico")%>';
                        return false;
                    } else if (campo.value != "8" && campo.value != "12") {
                        mensajeValidacion = '<%=meLanbide82I18n.getMensaje(idiomaUsuario, "msg.durContrato.valor")%>';
                        return false;
                    }
                }
                //CONTRATACION FIN
                campo = document.getElementById('dniNieFin').value;
                if (campo.value != null && campo.value != '') {
                    if (!validarDniNie(campo)) {
                        mensajeValidacion = '<%=meLanbide82I18n.getMensaje(idiomaUsuario, "msg.dni")%>';
                        return false;
                    }
                }
                campo = document.getElementById('durContratoFin').value;
                if (campo.value != null && campo.value != '') {
                    if (/^([0-9])*$/.test(campo)) {
                        mensajeValidacion = '<%=meLanbide82I18n.getMensaje(idiomaUsuario, "msg.durContrato.errNumerico")%>';
                        return false;
                    } else if (campo.value != "8" && campo.value != "12") {
                        mensajeValidacion = '<%=meLanbide82I18n.getMensaje(idiomaUsuario, "msg.durContrato.valor")%>';
                        return false;
                    }
                }
                campo = document.getElementById('retribucionBrutaFin').value;
                if (campo.value != null && campo.value != '') {
                    if (!validarNumericoDecimalPrecision(campo, 9, 2)) {
                        mensajeValidacion = '<%=meLanbide82I18n.getMensaje(idiomaUsuario, "msg.retribucionBruta.errNumerico")%>';
                        return false;
                    }
                }
                campo = document.getElementById('costeSalarialFin').value;
                if (campo.value != null && campo.value != '') {
                    if (!validarNumericoDecimalPrecision(campo, 9, 2)) {
                        mensajeValidacion = '<%=meLanbide82I18n.getMensaje(idiomaUsuario, "msg.costeSalarial.errNumerico")%>';
                        return false;
                    }
                }
                campo = document.getElementById('costesSsFin').value;
                if (campo.value != null && campo.value != '') {
                    if (!validarNumericoDecimalPrecision(campo, 9, 2)) {
                        mensajeValidacion = '<%=meLanbide82I18n.getMensaje(idiomaUsuario, "msg.costesss.errNumerico")%>';
                        return false;
                    }
                }
                campo = document.getElementById('indemFinContratoFin').value;
                if (campo.value != null && campo.value != '') {
                    if (!validarNumericoDecimalPrecision(campo, 9, 2)) {
                        mensajeValidacion = '<%=meLanbide82I18n.getMensaje(idiomaUsuario, "msg.indemFinContrato.errNumerico")%>';
                        return false;
                    }
                }
                campo = document.getElementById('costeTotalRealFin').value;
                if (campo.value != null && campo.value != '') {
                    if (!validarNumericoDecimalPrecision(campo, 9, 2)) {
                        mensajeValidacion = '<%=meLanbide82I18n.getMensaje(idiomaUsuario, "msg.costeTotalReal.errNumerico")%>';
                        return false;
                    }
                }
                campo = document.getElementById('subvConcedidaFin').value;
                if (campo.value != null && campo.value != '') {
                    if (!validarNumericoDecimalPrecision(campo, 9, 2)) {
                        mensajeValidacion = '<%=meLanbide82I18n.getMensaje(idiomaUsuario, "msg.subvConcedida.errNumerico")%>';
                        return false;
                    }
                }
                return true;
            }

            function cancelar() {
                var resultado = jsp_alerta('', '<%=meLanbide82I18n.getMensaje(idiomaUsuario, "msg.preguntaCancelar")%>');
                if (resultado == 1) {
                    cerrarVentana();
                }
            }

            //Funcion para el calendario de fecha 
            function mostrarCalFechaNacimientoInicio(evento) {
                if (window.event)
                    evento = window.event;
                if (document.getElementById("calFechaNacimientoInicio").src.indexOf("icono.gif") != -1)
                    showCalendar('forms[0]', 'fechaNacimientoInicio', null, null, null, '', 'calFechaNacimientoInicio', '', null, null, null, null, null, null, null, null, evento);
            }
            function mostrarCalFechaInicioInicio(evento) {
                if (window.event)
                    evento = window.event;
                if (document.getElementById("calFechaInicioInicio").src.indexOf("icono.gif") != -1)
                    showCalendar('forms[0]', 'fechaInicioInicio', null, null, null, '', 'calFechaInicioInicio', '', null, null, null, null, null, null, null, null, evento);
            }
            function mostrarCalFechaInicioFin(evento) {
                if (window.event)
                    evento = window.event;
                if (document.getElementById("calFechaInicioFin").src.indexOf("icono.gif") != -1)
                    showCalendar('forms[0]', 'fechaInicioFin', null, null, null, '', 'calFechaInicioFin', '', null, null, null, null, null, null, null, null, evento);
            }
            function mostrarCalFechaFinFin(evento) {
                if (window.event)
                    evento = window.event;
                if (document.getElementById("calFechaFinFin").src.indexOf("icono.gif") != -1)
                    showCalendar('forms[0]', 'fechaFinFin', null, null, null, '', 'calFechaFinFin', '', null, null, null, null, null, null, null, null, evento);
            }
            function comprobarFecha(inputFecha) {
                var formato = 'dd/mm/yyyy';
                if (Trim(inputFecha.value) != '') {
                    var D = ValidarFechaConFormato(inputFecha.value, formato);
                    if (!D[0]) {
                        jsp_alerta("A", "<%=meLanbide82I18n.getMensaje(idiomaUsuario, "msg.fechaNoVal")%>");
                        document.getElementById(inputFecha.name).focus();
                        document.getElementById(inputFecha.name).select();
                        return false;
                    } else {
                        inputFecha.value = D[1];
                        return true;
                    }//if (!D[0])
                }//if (Trim(inputFecha.value)!='')
                return true;
            }

            function desbloquearCampos() {
                document.getElementById('prioridad').disabled = false;
                document.getElementById('denomPuesto').disabled = false;
            }
        </script>
    </head>
    <body class="bandaBody">
        <div class="contenidoPantalla">
            <form>
                <div style="width: 100%; padding: 10px; text-align: left;">
                    <div  class="sub3titulo" style="clear: both; text-align: center; font-size: 14px;">
                        <span id="tituloVentana" style="width: 98%;">
                            <%=nuevo != null && nuevo=="1" ? meLanbide82I18n.getMensaje(idiomaUsuario,"label.nuevaContratacion"):meLanbide82I18n.getMensaje(idiomaUsuario,"label.modifContratacion")%>
                        </span>
                    </div> 
                    <fieldset id="puesto" name="puesto" style="width: 80%;">
                        <legend class="legendAzul" id="titPuesto"><%=meLanbide82I18n.getMensaje(idiomaUsuario,"label.puesto")%></legend>
                        <div class="lineaFormulario">
                            <div class="etiquetaGel" style="width: 60px;">
                                <%=meLanbide82I18n.getMensaje(idiomaUsuario,"label.prioridad")%>
                            </div>
                            <div style="width: 80px; float: left;">
                                <input id="prioridad" name="prioridad" type="text" class="inputTextoObligatorio" size="6" maxlength="4" onkeyup="SoloDigitos(this);" disabled
                                       value="<%=datModif != null && datModif.getPrioridad() != null ? datModif.getPrioridad() : ""%>" />
                            </div>
                            <div class="etiquetaGel" style="width: 110px;">
                                <%=meLanbide82I18n.getMensaje(idiomaUsuario,"label.denomPuesto")%>
                            </div>
                            <div style="width:480px; float: left;">
                                <textarea  id="denomPuesto" name="denomPuesto" type="text" class="inputTextoObligatorio" rows="5" cols="100" maxlength="500" disabled style="text-align: left"><%=datModif != null && datModif.getDenomPuesto() != null ? datModif.getDenomPuesto() : ""%>
                                </textarea>
                            </div>
                        </div>
                    </fieldset>
                    <fieldset id="contratacion" name="contratacion">
                        <legend class="legendAzul" id="titPuesto"><%=meLanbide82I18n.getMensaje(idiomaUsuario,"label.contratacion")%></legend>
                        <div class="lineaFormulario">
                            <div class="etiquetaGel">
                                <%=meLanbide82I18n.getMensaje(idiomaUsuario,"label.nivelCualificacion")%>
                            </div>
                            <div style="width:480px; float: left;">
                                <input type="text" name="codListaNivel" id="codListaNivel" size="3" class="inputComboObligatorio" value="" onkeyup="SoloDigitos(this);" maxlength="1" style="font-size:12px"/>
                                <input type="text" name="descListaNivel"  id="descListaNivel" size="60" class="inputComboObligatorio" readonly value="" style="font-size:12px"/>
                                <a href="" id="anchorListaNivel" name="anchorListaNivel">
                                    <span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonAplicacion"
                                          name="botonAplicacion" style="cursor:hand;"></span>
                                </a>
                            </div>                        
                            <div class="etiquetaGel" style="padding-left: 10px;">
                                <%=meLanbide82I18n.getMensaje(idiomaUsuario,"label.modContrato")%>
                            </div>
                            <div style="width:400px; float: left;">
                                <input id="modContrato" name="modContrato" type="text" class="inputTextoObligatorio" size="40" maxlength="150" 
                                       value="<%=datModif != null && datModif.getModContrato() != null ? datModif.getModContrato() : ""%>"/>
                            </div>
                        </div>
                        <br><br>
                        <div class="lineaFormulario">
                            <div class="etiquetaGel">
                                <%=meLanbide82I18n.getMensaje(idiomaUsuario,"label.durContrato")%>
                            </div>
                            <div style="width:480px; float: left;">
                                <input id="durContrato" name="durContrato" type="text" class="inputTextoObligatorio" size="6" maxlength="4" onkeyup="SoloDigitos(this);"
                                       value="<%=datModif != null && datModif.getDurContrato() != null ? datModif.getDurContrato() : ""%>"/>
                            </div>
                            <div class="etiquetaGel" style="padding-left: 10px;">
                                <%=meLanbide82I18n.getMensaje(idiomaUsuario,"label.grupoCotiz")%>
                            </div>
                            <div style="width:400px; float: left;">
                                <input type="text" name="codListaGrupoCotizacion" id="codListaGrupoCotizacion" size="3" class="inputComboObligatorio" value="" onkeyup="SoloDigitos(this);" maxlength="1"style="font-size:12px"/>
                                <input type="text" name="descListaGrupoCotizacion"  id="descListaGrupoCotizacion" size="15" class="inputComboObligatorio" readonly value="" style="font-size:12px"/>
                                <a href="" id="anchorListaGrupoCotizacion" name="anchorListaGrupoCotizacion">
                                    <span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonAplicacion"
                                          name="botonAplicacion" style="cursor:hand;"></span>
                                </a>
                            </div>
                        </div>
                        <br><br>
                        <div class="lineaFormulario">
                            <div class="etiquetaGel">
                                <%=meLanbide82I18n.getMensaje(idiomaUsuario,"label.costeSalarial")%>
                            </div>
                            <div style="width:480px; float: left;">
                                <input id="costeSalarial" name="costeSalarial" type="text" class="inputTextoObligatorio" size="9" maxlength="9" onchange="reemplazarPuntos(this);"
                                       value="<%=datModif != null && datModif.getCostesalarial() != null ? datModif.getCostesalarial().toString().replaceAll("\\.", ","): ""%>" />
                            </div>                            
                            <div class="etiquetaGel" style="padding-left: 10px;">
                                <%=meLanbide82I18n.getMensaje(idiomaUsuario,"label.subvSolicitada")%>
                            </div>
                            <div style="width:400px; float: left;">
                                <input id="subvSolicitada" name="subvSolicitada" type="text" class="inputTextoObligatorio" size="9" maxlength="9"  onchange="reemplazarPuntos(this);"
                                       value="<%=datModif != null && datModif.getSubvsolicitada() != null ? datModif.getSubvsolicitada().toString().replaceAll("\\.", ","): ""%>"/>
                            </div>
                        </div>
                    </fieldset>
                    <fieldset id="contrataIni" name="contrataIni">
                        <legend class="legendAzul" id="titPuesto"><%=meLanbide82I18n.getMensaje(idiomaUsuario,"label.contrataIni")%></legend>    
                        <div class="lineaFormulario">                            
                            <div class="etiquetaGel">
                                <%=meLanbide82I18n.getMensaje(idiomaUsuario,"label.municipio")%>
                            </div>
                            <div style="width:480px; float: left;">
                                <input id="municipioInicio" name="municipioInicio" type="text" class="inputTexto" size="20" maxlength="150"
                                       value="<%=datModif != null && datModif.getMunicipioInicio() != null ? datModif.getMunicipioInicio() : ""%>" />
                            </div>
                            <div class="etiquetaGel" style="padding-left: 10px;">
                                <%=meLanbide82I18n.getMensaje(idiomaUsuario,"label.dniNie")%>
                            </div>
                            <div style="width:400px; float: left;">
                                <input id="dniNieInicio" name="dniNieInicio" type="text" class="inputTexto" size="10" maxlength="9" onkeyup="xAMayusculas(this);"
                                       value ="<%=datModif != null && datModif.getDninieInicio() != null ? datModif.getDninieInicio() : ""%>" />
                            </div>                       
                        </div>
                        <br><br>
                        <div class="lineaFormulario">
                            <div class="etiquetaGel">
                                <%=meLanbide82I18n.getMensaje(idiomaUsuario,"label.nombre")%>
                            </div>
                            <div style="width:480px; float: left;">
                                <input id="nombreInicio" name="nombreInicio" type="text" class="inputTexto" size="30" maxlength="100"
                                       value="<%=datModif != null && datModif.getNombreInicio() != null ? datModif.getNombreInicio() : ""%>" />
                            </div>    
                            <div class="etiquetaGel" style="padding-left: 10px;">
                                <%=meLanbide82I18n.getMensaje(idiomaUsuario,"label.apellido1")%>
                            </div>
                            <div style="width:400px; float: left;">
                                <input id="apellido1Inicio" name="apellido1Inicio" type="text" class="inputTexto" size="30" maxlength="50" 
                                       value="<%=datModif != null && datModif.getApellido1Inicio() != null ? datModif.getApellido1Inicio() : ""%>" />
                            </div>                        
                        </div>
                        <br><br>
                        <div class="lineaFormulario">                           
                            <div class="etiquetaGel">
                                <%=meLanbide82I18n.getMensaje(idiomaUsuario,"label.apellido2")%>
                            </div>
                            <div style="width:480px; float: left;">
                                <input id="apellido2Inicio" name="apellido2Inicio" type="text" class="inputTexto" size="30" maxlength="50" 
                                       value="<%=datModif != null && datModif.getApellido2Inicio() != null ? datModif.getApellido2Inicio() : ""%>" />
                            </div>   
                            <div class="etiquetaGel"  style="padding-left: 10px;">
                                <%=meLanbide82I18n.getMensaje(idiomaUsuario,"label.sexo")%>
                            </div>
                            <div style="width:400px; float: left;">
                                <input type="text" name="codListaSexoInicio" id="codListaSexoInicio" size="3" class="inputTexto" value="" onkeyup="SoloDigitos(this);" maxlength="1"/>
                                <input type="text" name="descListaSexoInicio"  id="descListaSexoInicio" size="10" class="inputTexto" readonly value="" />
                                <a href="" id="anchorListaSexoInicio">
                                    <span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonAplicacion"
                                          style="cursor:hand;"></span>
                                </a>
                            </div>                                                        
                        </div>
                        <br><br>
                        <div class="lineaFormulario">
                            <div class="etiquetaGel">
                                <%=meLanbide82I18n.getMensaje(idiomaUsuario,"label.fechaNacimiento")%>
                            </div>
                            <div style="width:480px; float: left;">
                                <input type="text" class="inputTxtFecha" 
                                       id="fechaNacimientoInicio" name="fechaNacimientoInicio"
                                       maxlength="10"  size="10"
                                       value="<%=fechaNacimientoInicio%>"
                                       onkeyup = "return SoloCaracteresFecha(this);"
                                       onblur = "javascript:return comprobarFecha(this);"
                                       onfocus="javascript:this.select();"/>
                                <A href="javascript:calClick(event);return false;" onClick="mostrarCalFechaNacimientoInicio(event);
                                        return false;" style="text-decoration: none;">
                                    <IMG style="border: 0px solid none" height="17" id="calFechaNacimientoInicio" border="0" 
                                         src="<c:url value='/images/calendario/icono.gif'/>" > 
                                </A>
                            </div>   
                            <div class="etiquetaGel" style="padding-left: 10px;">
                                <%=meLanbide82I18n.getMensaje(idiomaUsuario,"label.edad")%>
                            </div>
                            <div style="width:400px; float: left;">
                                <input id="edadInicio" name="edadInicio" type="text" class="inputTexto" size="3" maxlength="2" onkeyup="SoloDigitos(this);"
                                       value ="<%=datModif != null && datModif.getEdadInicio() != null ? datModif.getEdadInicio() : ""%>" />
                            </div>       
                        </div> 
                        <br><br>
                        <div class="lineaFormulario"> 
                            <div class="etiquetaGel">
                                <%=meLanbide82I18n.getMensaje(idiomaUsuario,"label.nivelCualificacion")%>
                            </div>
                            <div style="width:480px; float: left;">
                                <input type="text" name="codListaNivelInicio" id="codListaNivelInicio" size="3" class="inputTexto" value="" onkeyup="SoloDigitos(this);" maxlength="1"/>
                                <input type="text" name="descListaNivelInicio"  id="descListaNivelInicio" size="60" class="inputTexto" readonly value="" />
                                <a href="" id="anchorListaNivelInicio" name="anchorListaNivelInicio">
                                    <span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonAplicacion"
                                          name="botonAplicacion" style="cursor:hand;"></span>
                                </a>
                            </div>                            
                        </div>
                        <br><br>
                        <div class="lineaFormulario">
                            <div class="etiquetaGel">
                                <%=meLanbide82I18n.getMensaje(idiomaUsuario,"label.puestoTrabajo")%>
                            </div>
                            <textarea  id="puestoTrabajoInicio" name="puestoTrabajoInicio" type="text" class="inputTexto" rows="5" cols="100" maxlength="500"  style="text-align: left; width: 1000px;"><%=datModif != null && datModif.getPuestotrabajoInicio() != null ? datModif.getPuestotrabajoInicio() : ""%>
                            </textarea>                            
                        </div>
                        <br>
                        <div class="lineaFormulario">
                            <div class="etiquetaGel">
                                <%=meLanbide82I18n.getMensaje(idiomaUsuario,"label.nOferta")%>
                            </div>
                            <div style="width:480px; float: left;">
                                <input id="nOfertaInicio" name="nOfertaInicio" type="text" class="inputTexto" size="11" maxlength="50"
                                       value ="<%=datModif != null && datModif.getNofertaInicio() != null ? datModif.getNofertaInicio() : ""%>" />
                            </div>
                            <div class="etiquetaGel" style="padding-left: 10px;">
                                <%=meLanbide82I18n.getMensaje(idiomaUsuario,"label.grupoCotiz")%>
                            </div>
                            <div style="width:400px; float: left;">
                                <input type="text" name="codListaGrupoCotizacionInicio" id="codListaGrupoCotizacionInicio" size="3" class="inputTexto" value="" onkeyup="SoloDigitos(this);"  maxlength="1"/>
                                <input type="text" name="descListaGrupoCotizacionInicio"  id="descListaGrupoCotizacionInicio" size="15" class="inputTexto" readonly value="" />
                                <a href="" id="anchorListaGrupoCotizacionInicio" name="anchorListaGrupoCotizacionInicio">
                                    <span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonAplicacion"
                                          name="botonAplicacion" style="cursor:hand;"></span>
                                </a>
                            </div>    
                        </div>
                        <br><br>
                        <div class="lineaFormulario">
                            <div class="etiquetaGel">
                                <%=meLanbide82I18n.getMensaje(idiomaUsuario,"label.fechaInicio")%>
                            </div>
                            <div style="width:480px; float: left;">
                                <input type="text" class="inputTxtFecha" 
                                       id="fechaInicioInicio" name="fechaInicioInicio"
                                       maxlength="10"  size="10"
                                       value="<%=fechaInicioInicio%>"
                                       onkeyup = "return SoloCaracteresFecha(this);"
                                       onblur = "javascript:return comprobarFecha(this);"
                                       onfocus="javascript:this.select();"/>
                                <A href="javascript:calClick(event);return false;" onClick="mostrarCalFechaInicioInicio(event);
                                        return false;" style="text-decoration: none;">
                                    <IMG style="border: 0px solid none" height="17" id="calFechaInicioInicio" border="0" 
                                         src="<c:url value='/images/calendario/icono.gif'/>" > 
                                </A>
                            </div>
                            <div class="etiquetaGel" style="padding-left: 10px;">
                                <%=meLanbide82I18n.getMensaje(idiomaUsuario,"label.durContrato")%>
                            </div>
                            <div style="width:400px; float: left;">
                                <input id="durContratoInicio" name="durContratoInicio" type="text" class="inputTexto" size="8" maxlength="4" onkeyup="SoloDigitos(this);"
                                       value="<%=datModif != null && datModif.getDurcontratoInicio() != null ? datModif.getDurcontratoInicio() : ""%>"/>
                            </div>
                        </div> 
                        <br><br>
                        <div class="lineaFormulario">                            
                            <div class="etiquetaGel">
                                <%=meLanbide82I18n.getMensaje(idiomaUsuario,"label.retribucionBruta")%>
                            </div>
                            <div style="width:400px; float: left;">
                                <input id="retribucionBrutaInicio" name="retribucionBrutaInicio" type="text" class="inputTexto" size="9" maxlength="9" onchange="reemplazarPuntos(this);"
                                       value ="<%=datModif != null && datModif.getRetribucionbrutaInicio() != null ? datModif.getRetribucionbrutaInicio().toString().replaceAll("\\.", ",") : ""%>" />
                            </div>
                        </div>
                    </fieldset>
                    <fieldset id="contrataFin" name="contrataFin">
                        <legend class="legendAzul" id="titPuesto"><%=meLanbide82I18n.getMensaje(idiomaUsuario,"label.contrataFin")%></legend>
                        <div class="lineaFormulario">                            
                            <div class="etiquetaGel">
                                <%=meLanbide82I18n.getMensaje(idiomaUsuario,"label.municipio")%>
                            </div>
                            <div style="width:480px; float: left;">
                                <input id="municipioFin" name="municipioFin" type="text" class="inputTexto" size="20" maxlength="150"
                                       value="<%=datModif != null && datModif.getMunicipioFin() != null ? datModif.getMunicipioFin() : ""%>" />
                            </div>
                            <div class="etiquetaGel"  style="padding-left: 10px;">
                                <%=meLanbide82I18n.getMensaje(idiomaUsuario,"label.dniNie")%>
                            </div>
                            <div style="width:400px; float: left;">
                                <input id="dniNieFin" name="dniNieFin" type="text" class="inputTexto" size="10" maxlength="9" onkeyup="xAMayusculas(this);"
                                       value ="<%=datModif != null && datModif.getDninieFin() != null ? datModif.getDninieFin() : ""%>" />
                            </div>
                        </div>
                        <br><br>
                        <div class="lineaFormulario">                           
                            <div class="etiquetaGel">
                                <%=meLanbide82I18n.getMensaje(idiomaUsuario,"label.nombre")%>
                            </div>
                            <div style="width:480px; float: left;">
                                <input id="nombreFin" name="nombreFin" type="text" class="inputTexto" size="30" maxlength="100"
                                       value="<%=datModif != null && datModif.getNombreFin() != null ? datModif.getNombreFin() : ""%>" />
                            </div>
                            <div class="etiquetaGel" style="padding-left: 10px;">
                                <%=meLanbide82I18n.getMensaje(idiomaUsuario,"label.apellido1")%>
                            </div>
                            <div style="width:400px; float: left;">
                                <input id="apellido1Fin" name="apellido1Fin" type="text" class="inputTexto" size="30" maxlength="100" 
                                       value="<%=datModif != null && datModif.getApellido1Fin() != null ? datModif.getApellido1Fin() : ""%>" />
                            </div>
                        </div>
                        <br><br>
                        <div class="lineaFormulario">                            
                            <div class="etiquetaGel">
                                <%=meLanbide82I18n.getMensaje(idiomaUsuario,"label.apellido2")%>
                            </div>
                            <div style="width:480px; float: left;">
                                <input id="apellido2Fin" name="apellido2Fin" type="text" class="inputTexto" size="30" maxlength="100" 
                                       value="<%=datModif != null && datModif.getApellido2Fin() != null ? datModif.getApellido2Fin() : ""%>" />
                            </div>
                            <div class="etiquetaGel" style="padding-left: 10px;">
                                <%=meLanbide82I18n.getMensaje(idiomaUsuario,"label.sexo")%>
                            </div>
                            <div style="width:400px; float: left;">
                                <input type="text" name="codListaSexoFin" id="codListaSexoFin" size="3" class="inputTexto" value="" onkeyup="SoloDigitos(this);" maxlength="1"/>
                                <input type="text" name="descListaSexoFin"  id="descListaSexoFin" size="10" class="inputTexto" readonly value="" />
                                <a href="" id="anchorListaSexoFin">
                                    <span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonAplicacion"
                                          style="cursor:hand;"></span>
                                </a>
                            </div>
                        </div> 
                        <br><br>
                        <div class="lineaFormulario">                                                      
                            <div class="etiquetaGel">
                                <%=meLanbide82I18n.getMensaje(idiomaUsuario,"label.grupoCotiz")%>
                            </div>
                            <div style="width:480px; float: left;">
                                <input type="text" name="codListaGrupoCotizacionFin" id="codListaGrupoCotizacionFin" size="3" class="inputTexto" value="" onkeyup="SoloDigitos(this);" maxlength="1"/>
                                <input type="text" name="descListaGrupoCotizacionFin"  id="descListaGrupoCotizacionFin" size="15" class="inputTexto" readonly value="" />
                                <a href="" id="anchorListaGrupoCotizacionFin" name="anchorListaGrupoCotizacionFin">
                                    <span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonAplicacion"
                                          name="botonAplicacion" style="cursor:hand;"></span>
                                </a>
                            </div>
                            <div class="etiquetaGel" style="padding-left: 10px;">
                                <%=meLanbide82I18n.getMensaje(idiomaUsuario,"label.durContrato")%>
                            </div>
                            <div style="width:400px; float: left;">
                                <input id="durContratoFin" name="durContratoFin" type="text" class="inputTexto" size="8" maxlength="4" onkeyup="SoloDigitos(this);"
                                       value="<%=datModif != null && datModif.getDurcontratoFin() != null ? datModif.getDurcontratoFin() : ""%>"/>
                            </div>
                        </div>
                        <br><br>
                        <div class="lineaFormulario">
                            <div class="etiquetaGel">
                                <%=meLanbide82I18n.getMensaje(idiomaUsuario,"label.fechaInicio")%>
                            </div>
                            <div style="width:480px; float: left;">
                                <input type="text" class="inputTxtFecha" 
                                       id="fechaInicioFin" name="fechaInicioFin"
                                       maxlength="10"  size="10"
                                       value="<%=fechaInicioFin%>"
                                       onkeyup = "return SoloCaracteresFecha(this);"
                                       onblur = "javascript:return comprobarFecha(this);"
                                       onfocus="javascript:this.select();"/>
                                <A href="javascript:calClick(event);return false;" onClick="mostrarCalFechaInicioFin(event);
                                        return false;" style="text-decoration: none;">
                                    <IMG style="border: 0px solid none" height="17" id="calFechaInicioFin" border="0" 
                                         src="<c:url value='/images/calendario/icono.gif'/>" > 
                                </A>
                            </div> 
                            <div class="etiquetaGel" style="padding-left: 10px;">
                                <%=meLanbide82I18n.getMensaje(idiomaUsuario,"label.fechaFin")%>
                            </div>
                            <div style="width:400px; float: left;">
                                <input type="text" class="inputTxtFecha" 
                                       id="fechaFinFin" name="fechaFinFin"
                                       maxlength="10"  size="10"
                                       value="<%=fechaFinFin%>"
                                       onkeyup = "return SoloCaracteresFecha(this);"
                                       onblur = "javascript:return comprobarFecha(this);"
                                       onfocus="javascript:this.select();"/>
                                <A href="javascript:calClick(event);return false;" onClick="mostrarCalFechaFinFin(event);
                                        return false;" style="text-decoration: none;">
                                    <IMG style="border: 0px solid none" height="17" id="calFechaFinFin" border="0" 
                                         src="<c:url value='/images/calendario/icono.gif'/>" > 
                                </A>
                            </div>
                        </div>
                        <br><br>
                        <div class="lineaFormulario">
                            <div class="etiquetaGel">
                                <%=meLanbide82I18n.getMensaje(idiomaUsuario,"label.retribucionBruta")%>
                            </div>
                            <div style="width:480px; float: left;">
                                <input id="retribucionBrutaFin" name="retribucionBrutaFin" type="text" class="inputTexto" size="9" maxlength="9" onchange="reemplazarPuntos(this);" 
                                       value ="<%=datModif != null && datModif.getRetribucionbrutaFin() != null ? datModif.getRetribucionbrutaFin().toString().replaceAll("\\.", ",") : ""%>" />
                            </div>
                            <div class="etiquetaGel" style="padding-left: 10px;">
                                <%=meLanbide82I18n.getMensaje(idiomaUsuario,"label.costeSalarial")%>
                            </div>
                            <div style="width:400px; float: left;">
                                <input id="costeSalarialFin" name="costeSalarialFin" type="text" class="inputTexto" size="9" maxlength="9" onchange="reemplazarPuntos(this);"
                                       value ="<%=datModif != null && datModif.getCostesalarialFin() != null ? datModif.getCostesalarialFin().toString().replaceAll("\\.", ",") : ""%>" />
                            </div>
                        </div>
                        <br><br>
                        <div class="lineaFormulario">
                            <div class="etiquetaGel">
                                <%=meLanbide82I18n.getMensaje(idiomaUsuario,"label.costeSS")%>
                            </div>
                            <div style="width:480px; float: left;">
                                <input id="costesSsFin" name="costesSsFin" type="text" class="inputTexto" size="9" maxlength="9" onchange="reemplazarPuntos(this);"
                                       value ="<%=datModif != null && datModif.getCostesssFin() != null ? datModif.getCostesssFin().toString().replaceAll("\\.", ",") : ""%>" />
                            </div>
                            <div class="etiquetaGel" style="padding-left: 10px;">
                                <%=meLanbide82I18n.getMensaje(idiomaUsuario,"label.indemFinContrato")%>
                            </div>
                            <div style="width:400px; float: left;">
                                <input id="indemFinContratoFin" name="indemFinContratoFin" type="text" class="inputTexto" size="9" maxlength="9" onchange="reemplazarPuntos(this);"
                                       value ="<%=datModif != null && datModif.getIndemfincontratoFin() != null ? datModif.getIndemfincontratoFin().toString().replaceAll("\\.", ",") : ""%>" />
                            </div>
                        </div>
                        <br><br>
                        <div class="lineaFormulario">
                            <div class="etiquetaGel">
                                <%=meLanbide82I18n.getMensaje(idiomaUsuario,"label.costeTotalReal")%>
                            </div>
                            <div style="width:480px; float: left;">
                                <input id="costeTotalRealFin" name="costeTotalRealFin" type="text" class="inputTexto" size="9" maxlength="9" onchange="reemplazarPuntos(this);"
                                       value ="<%=datModif != null && datModif.getCostetotalrealFin() != null ? datModif.getCostetotalrealFin().toString().replaceAll("\\.", ",") : ""%>" />
                            </div>
                            <div class="etiquetaGel" style="padding-left: 10px;">
                                <%=meLanbide82I18n.getMensaje(idiomaUsuario,"label.subvConcedida")%>
                            </div>
                            <div style="width:400px; float: left;">
                                <input id="subvConcedidaFin" name="subvConcedidaFin" type="text" class="inputTexto" size="9" maxlength="9" onchange="reemplazarPuntos(this);"
                                       value ="<%=datModif != null && datModif.getSubvconcedidalanFin() != null ? datModif.getSubvconcedidalanFin().toString().replaceAll("\\.", ",") : ""%>" />
                            </div>
                        </div>
                    </fieldset>
                    <div class="lineaFormulario" style="margin-top: 25px;">
                        <div class="botonera" style="width: 100%; float: left; text-align: center;">
                            <input type="button" id="btnAceptar" name="btnAceptar" class="botonGeneral" value="<%=meLanbide82I18n.getMensaje(idiomaUsuario, "btn.aceptar")%>" onclick="guardarDatos();"/>
                            <input type="button" id="btnCancelar" name="btnCancelar" class="botonGeneral" value="<%=meLanbide82I18n.getMensaje(idiomaUsuario, "btn.cancelar")%>" onclick="cancelar();"/>
                        </div>
                    </div>
                </div>
                <div id="reloj" style="font-size:20px;"></div>
            </form>
            <script type="text/javascript">

                /*desplegable grupoCotizacion*/
                listaCodigosGrupoCotizacion[0] = "";
                listaDescripcionesGrupoCotizacion[0] = "";
                contador = 0;
                <logic:iterate id="grupoCotizacion" name="listaGrupoCotizacion" scope="request">
                listaCodigosGrupoCotizacion[contador] = ['<bean:write name="grupoCotizacion" property="des_val_cod" />'];
                listaDescripcionesGrupoCotizacion[contador] = ['<bean:write name="grupoCotizacion" property="des_nom" />'];
                contador++;
                </logic:iterate>
                comboListaGrupoCotizacion = new Combo("ListaGrupoCotizacion");
                comboListaGrupoCotizacion.addItems(listaCodigosGrupoCotizacion, listaDescripcionesGrupoCotizacion);
                comboListaGrupoCotizacion.change = cargarDatosGrupoCotizacion;

                /*desplegable grupoCotizacion INICIO*/
                listaCodigosGrupoCotizacionInicio[0] = "";
                listaDescripcionesGrupoCotizacionInicio[0] = "";
                contador = 0;
                <logic:iterate id="grupoCotizacionInicio" name="listaGrupoCotizacion" scope="request">
                listaCodigosGrupoCotizacionInicio[contador] = ['<bean:write name="grupoCotizacionInicio" property="des_val_cod" />'];
                listaDescripcionesGrupoCotizacionInicio[contador] = ['<bean:write name="grupoCotizacionInicio" property="des_nom" />'];
                contador++;
                </logic:iterate>
                comboListaGrupoCotizacionInicio = new Combo("ListaGrupoCotizacionInicio");
                comboListaGrupoCotizacionInicio.addItems(listaCodigosGrupoCotizacionInicio, listaDescripcionesGrupoCotizacionInicio);
                comboListaGrupoCotizacionInicio.change = cargarDatosGrupoCotizacionInicio;

                /*desplegable grupoCotizacion FIN*/
                listaCodigosGrupoCotizacionFin[0] = "";
                listaDescripcionesGrupoCotizacionFin[0] = "";
                contador = 0;
                <logic:iterate id="grupoCotizacionFin" name="listaGrupoCotizacion" scope="request">
                listaCodigosGrupoCotizacionFin[contador] = ['<bean:write name="grupoCotizacionFin" property="des_val_cod" />'];
                listaDescripcionesGrupoCotizacionFin[contador] = ['<bean:write name="grupoCotizacionFin" property="des_nom" />'];
                contador++;
                </logic:iterate>
                comboListaGrupoCotizacionFin = new Combo("ListaGrupoCotizacionFin");
                comboListaGrupoCotizacionFin.addItems(listaCodigosGrupoCotizacionFin, listaDescripcionesGrupoCotizacionFin);
                comboListaGrupoCotizacionFin.change = cargarDatosGrupoCotizacionFin;

                /*desplegable Nivel CUalificacion*/
                listaCodigosNivel[0] = "";
                listaDescripcionesNivel[0] = "";
                contador = 0;
                <logic:iterate id="nivel" name="listaNivel" scope="request">
                listaCodigosNivel[contador] = ['<bean:write name="nivel" property="des_val_cod" />'];
                listaDescripcionesNivel[contador] = ['<bean:write name="nivel" property="des_nom" />'];
                contador++;
                </logic:iterate>
                comboListaNivel = new Combo("ListaNivel");
                comboListaNivel.addItems(listaCodigosNivel, listaDescripcionesNivel);
                comboListaNivel.change = cargarDatosNivel;
                /*desplegable Nivel CUalificacion Inicio */
                listaCodigosNivelInicio[0] = "";
                listaDescripcionesNivelInicio[0] = "";
                contador = 0;
                <logic:iterate id="nivelInicio" name="listaNivel" scope="request">
                listaCodigosNivelInicio[contador] = ['<bean:write name="nivelInicio" property="des_val_cod" />'];
                listaDescripcionesNivelInicio[contador] = ['<bean:write name="nivelInicio" property="des_nom" />'];
                contador++;
                </logic:iterate>
                comboListaNivelInicio = new Combo("ListaNivelInicio");
                comboListaNivelInicio.addItems(listaCodigosNivelInicio, listaDescripcionesNivelInicio);
                comboListaNivelInicio.change = cargarDatosNivelInicio;


                /*  SEXO INICIO */
                listaCodigosSexoInicio[0] = "";
                listaDescripcionesSexoInicio[0] = "";
                contador = 0;
                <logic:iterate id="sexoInicio" name="listaSexo" scope="request">
                listaCodigosSexoInicio[contador] = ['<bean:write name="sexoInicio" property="des_val_cod" />'];
                listaDescripcionesSexoInicio[contador] = ['<bean:write name="sexoInicio" property="des_nom" />'];
                contador++;
                </logic:iterate>
                var comboListaSexoInicio = new Combo("ListaSexoInicio");
                comboListaSexoInicio.addItems(listaCodigosSexoInicio, listaDescripcionesSexoInicio);
                comboListaSexoInicio.change = cargarDatosSexoInicio;
                /*  SEXO FIN */
                listaCodigosSexoFin[0] = "";
                listaDescripcionesSexoFin[0] = "";
                contador = 0;
                <logic:iterate id="sexoFin" name="listaSexo" scope="request">
                listaCodigosSexoFin[contador] = ['<bean:write name="sexoFin" property="des_val_cod" />'];
                listaDescripcionesSexoFin[contador] = ['<bean:write name="sexoFin" property="des_nom" />'];
                contador++;
                </logic:iterate>
                var comboListaSexoFin = new Combo("ListaSexoFin");
                comboListaSexoFin.addItems(listaCodigosSexoFin, listaDescripcionesSexoFin);
                comboListaSexoFin.change = cargarDatosSexoFin;

                if (<%=nuevo%> != null && <%=nuevo%> == 0) {
                    rellenarDesplegables();
                } else {
                    desbloquearCampos();
                }
            </script>
            <div id="popupcalendar" class="text"></div>        
        </div>
    </body>
</html> 

<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>

<%@ page import="es.altia.flexia.integracion.moduloexterno.melanbide81.i18n.MeLanbide81I18n" %>
<%@ page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@ page import="es.altia.common.service.config.Config" %>
<%@ page import="es.altia.common.service.config.ConfigServiceHelper" %>
<%@ page import="es.altia.flexia.integracion.moduloexterno.melanbide67.i18n.MeLanbide67I18n" %>
<%@ page import="es.altia.flexia.integracion.moduloexterno.melanbide81.vo.Tipo1VO" %>
<%@ page import="es.altia.flexia.integracion.moduloexterno.melanbide67.util.ConstantesMeLanbide67" %>
<%@ page import="es.altia.flexia.integracion.moduloexterno.melanbide81.util.ConstantesMeLanbide81" %>
<%@ page import="es.altia.flexia.integracion.moduloexterno.melanbide81.util.ConfigurationParameter" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.io.ObjectInputStream" %>
<%@ page import="java.util.List" %>
<%@ page import="java.text.DateFormat" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.text.DecimalFormat" %>
<%@ page import="java.text.DecimalFormatSymbols" %>
<%@ page import="java.util.Date" %>
<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="X-UA-Compatible" content="IE=Edge" />
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <%
        UsuarioValueObject usuarioVO = new UsuarioValueObject();
        int idiomaUsuario = 1;
        int apl = 5;
        String css = "";
        try {
            if (session.getAttribute("usuario") != null) {
                usuarioVO = (UsuarioValueObject) session.getAttribute("usuario");
                apl = usuarioVO.getAppCod();
                idiomaUsuario = usuarioVO.getIdioma();
                css = usuarioVO.getCss();
            }

        } catch (Exception ex) {
        }

        // Clase para internacionalizar los mensajes de la aplicación.
        MeLanbide81I18n meLanbide81I18n = MeLanbide81I18n.getInstance();
        MeLanbide67I18n meLanbide67I18n = MeLanbide67I18n.getInstance();
        final String tabla = ConfigurationParameter.getParameter(ConstantesMeLanbide81.TAB_TIPO1, ConstantesMeLanbide81.FICHERO_PROPIEDADES);
        final String tipoDocCv = es.altia.flexia.integracion.moduloexterno.melanbide67.util.ConfigurationParameter.getParameter(
                ConstantesMeLanbide67.DOCUMENTO_CV,
                ConstantesMeLanbide67.FICHERO_PROPIEDADES);
        final String tipoDocDemanda = es.altia.flexia.integracion.moduloexterno.melanbide67.util.ConfigurationParameter.getParameter(
                ConstantesMeLanbide67.DOCUMENTO_DEMANDA,
                ConstantesMeLanbide67.FICHERO_PROPIEDADES);

        String numExpediente = (String) request.getAttribute("numExp");
        DecimalFormatSymbols simbolo = new DecimalFormatSymbols();
        simbolo.setDecimalSeparator(',');
        simbolo.setGroupingSeparator('.');
        DecimalFormat formateador = new DecimalFormat("###,##0.##", simbolo);
    %>
    <jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
    <jsp:setProperty name="descriptor" property="idi_cod" value="<%=idiomaUsuario%>" />
    <jsp:setProperty name="descriptor" property="apl_cod" value="<%=apl%>" />
    <link rel="StyleSheet" media="screen" type="text/css" href="<%=request.getContextPath()%><%=css%>"/>
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/>
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/extension/melanbide81/melanbide81.css"/>
    <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/melanbide81/FixedColumnsTable.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/melanbide81/LpeelUtils.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/popup.js"></script>
    <script type="text/javascript">
        var mensajeValidacion = '';
        var APP_CONTEXT_PATH = '<%=request.getContextPath()%>';
        var url = APP_CONTEXT_PATH + '/PeticionModuloIntegracion.do';
        var tablaTipo1;
        var divListaTipo1 = new Array();
        var listaTipo1Tabla = new Array();
        var listaTipo1Tabla_titulos = new Array();
        var listaTipo1Tabla_estilos = new Array();
        var parametrosLLamadaM81 = {
            tarea: 'preparar',
            modulo: 'MELANBIDE81',
            operacion: null,
            tipo: 0,
            numero: null,
            documento: null
        };

        let tabla1 = '<%=tabla%>';
        let tipoDocCv = '<%=tipoDocCv%>';
        let tipoDocDemanda = '<%=tipoDocDemanda%>';

        function pulsarNuevoTipo1() {
            lanzarPopUpModal(
                url + '?tarea=preparar&modulo=MELANBIDE81&operacion=cargarNuevoTipo1&tipo=0&nuevo=1&numExp=<%=numExpediente%>',
                900,
                800,
                'no',
                'no',
                function (result) {
                    if (result != undefined) {
                        if (result[0] == '0') {
                            recargarDatosExpediente();
                        }
                    }
                }
            );
        }

        function pulsarModificarTipo1() {
            if (tablaTipo1.selectedIndex != -1) {
                lanzarPopUpModal(
                    url + '?tarea=preparar&modulo=MELANBIDE81&operacion=cargarModificarTipo1&tipo=0&nuevo=0&numExp=<%=numExpediente%>&id=' +
                    divListaTipo1[tablaTipo1.selectedIndex][0],
                    900,
                    800,
                    'si',
                    'no',
                    function (result) {
                        if (result != undefined) {
                            if (result[0] == '0') {
                                recargarTablaTipos1(result[1]);
                            }
                        }
                    }
                );
            } else {
                jsp_alerta('A', '<%=meLanbide81I18n.getMensaje(idiomaUsuario, "msg.msjNoSelecFila")%>');
            }
        }

        function pulsarEliminarTipo1() {
            if (tablaTipo1.selectedIndex != -1) {
                var resultado = jsp_alerta('', '<%=meLanbide81I18n.getMensaje(idiomaUsuario, "msg.preguntaEliminar")%>');
                if (resultado == 1) {
                    elementoVisible('on', 'barraProgresoLPEEL');

                    var parametros = 'tarea=preparar&modulo=MELANBIDE81&operacion=eliminarTipo1&tipo=0&numExp=<%=numExpediente%>&id=' +
                        divListaTipo1[tablaTipo1.selectedIndex][0];

                    try {
                        $.ajax({
                            url: url,
                            type: 'POST',
                            async: true,
                            data: parametros,
                            success: procesarRespuestaActualizarPestana1,
                            error: mostrarErrorEliminarTipo1
                        });
                    } catch (err) {
                        elementoVisible('off', 'barraProgresoLPEEL');
                        mostrarErrorPeticion();
                    }
                }
            } else {
                jsp_alerta('A', '<%=meLanbide81I18n.getMensaje(idiomaUsuario, "msg.msjNoSelecFila")%>');
            }
        }

        function pulsarExportarExcellApor() {
            var parametros = "";
            parametros = '?tarea=preparar&modulo=MELANBIDE81&operacion=generarExcelAporContrataciones&tipo=0&numExp=<%=numExpediente%>';
            window.open(url + parametros, "_blank");

        }
        function crearTablaTipo1() {
            tablaTipo1 = new FixedColumnTable(document.getElementById('divListaTipo1'), 1500, 1500, 'divListaTipo1');

            tablaTipo1.addColumna('200', 'left', "<%=meLanbide81I18n.getMensaje(idiomaUsuario,"tipo.entidadLocalBeneficiaria")%>");
            tablaTipo1.addColumna('150', 'left', "<%=meLanbide81I18n.getMensaje(idiomaUsuario,"tipo.entidadcontratante")%>");
            tablaTipo1.addColumna('75', 'center', "<%=meLanbide81I18n.getMensaje(idiomaUsuario,"tipo.CIF")%>");
            tablaTipo1.addColumna('75', 'center', "<%=meLanbide81I18n.getMensaje(idiomaUsuario,"tipo.CCC")%>");
            tablaTipo1.addColumna('150', 'left', "<%=meLanbide81I18n.getMensaje(idiomaUsuario,"tipo.denominacionDelProyecto")%>");
            tablaTipo1.addColumna('150', 'left', "<%=meLanbide81I18n.getMensaje(idiomaUsuario,"tipo.denominacionDelPuestoDeTrabajo")%>");
            tablaTipo1.addColumna('150', 'left', "<%=meLanbide81I18n.getMensaje(idiomaUsuario,"tipo.nombre")%>");
            tablaTipo1.addColumna('150', 'left', "<%=meLanbide81I18n.getMensaje(idiomaUsuario,"tipo.apellido1")%>");
            tablaTipo1.addColumna('150', 'left', "<%=meLanbide81I18n.getMensaje(idiomaUsuario,"tipo.apellido2")%>");
            tablaTipo1.addColumna('75', 'center', "<%=meLanbide81I18n.getMensaje(idiomaUsuario,"tipo.DNI")%>");
            tablaTipo1.addColumna('200', 'center', "<%=meLanbide81I18n.getMensaje(idiomaUsuario,"label.tipo.cv2")%>");
            tablaTipo1.addColumna('200', 'center', "<%=meLanbide81I18n.getMensaje(idiomaUsuario,"label.tipo.fechaCv2")%>");
            tablaTipo1.addColumna('200', 'center', "<%=meLanbide81I18n.getMensaje(idiomaUsuario,"label.tipo.demanda2")%>");
            tablaTipo1.addColumna('200', 'center', "<%=meLanbide81I18n.getMensaje(idiomaUsuario,"label.tipo.fechaDemanda2")%>");
            tablaTipo1.addColumna('75', 'center', "<%=meLanbide81I18n.getMensaje(idiomaUsuario,"tipo.NAF")%>");
            tablaTipo1.addColumna('75', 'center', "<%=meLanbide81I18n.getMensaje(idiomaUsuario,"tipo.fecnacimiento")%>");
            tablaTipo1.addColumna('75', 'center', "<%=meLanbide81I18n.getMensaje(idiomaUsuario,"tipo.sexo")%>");
            tablaTipo1.addColumna('200', 'left', "<%=meLanbide81I18n.getMensaje(idiomaUsuario,"tipo.grupoDeCotizacion")%>");
            tablaTipo1.addColumna('75', 'center', "<%=meLanbide81I18n.getMensaje(idiomaUsuario,"tipo.fechaInicioContrato")%>");
            tablaTipo1.addColumna('75', 'center', "<%=meLanbide81I18n.getMensaje(idiomaUsuario,"tipo.fechaFinContrato")%>");
            tablaTipo1.addColumna('75', 'center', "<%=meLanbide81I18n.getMensaje(idiomaUsuario,"tabla.porcJorn")%>");
            tablaTipo1.addColumna('150', 'left', "<%=meLanbide81I18n.getMensaje(idiomaUsuario,"tipo.duracionDelContrato")%>");
            tablaTipo1.addColumna('75', 'center', "<%=meLanbide81I18n.getMensaje(idiomaUsuario,"tipo.edad")%>");
            tablaTipo1.addColumna('150', 'left', "<%=meLanbide81I18n.getMensaje(idiomaUsuario,"tipo.municipioDeResidencia")%>");
            tablaTipo1.addColumna('100', 'right', "<%=meLanbide81I18n.getMensaje(idiomaUsuario,"tipo.costeSalarial")%>");
            tablaTipo1.addColumna('100', 'right', "<%=meLanbide81I18n.getMensaje(idiomaUsuario,"tipo.costeSeguridadSocial")%>");
            tablaTipo1.addColumna('100', 'right', "<%=meLanbide81I18n.getMensaje(idiomaUsuario,"tipo.costeTotal")%>");
            tablaTipo1.addColumna('75', 'center', "<%=meLanbide81I18n.getMensaje(idiomaUsuario,"tipo.inscritaDesempleada")%>");
            tablaTipo1.addColumna('150', 'center', "<%=meLanbide81I18n.getMensaje(idiomaUsuario,"tipo.certInterventor")%>");
            tablaTipo1.addColumna('100', 'right', "<%=meLanbide81I18n.getMensaje(idiomaUsuario,"tipo.subvencionConcedida")%>");
            tablaTipo1.addColumna('100', 'right', "<%=meLanbide81I18n.getMensaje(idiomaUsuario,"tipo.priPago")%>");
            tablaTipo1.addColumna('100', 'right', "<%=meLanbide81I18n.getMensaje(idiomaUsuario,"tipo.subLiquidada")%>");
            tablaTipo1.addColumna('100', 'right', "<%=meLanbide81I18n.getMensaje(idiomaUsuario,"tipo.segunPago")%>");
            tablaTipo1.addColumna('250', 'left', "<%=meLanbide81I18n.getMensaje(idiomaUsuario,"tipo.observaciones")%>");

            for (var cont = 0; cont < listaTipo1Tabla.length; cont++) {
                tablaTipo1.addFilaConFormato(listaTipo1Tabla[cont], listaTipo1Tabla_titulos[cont], listaTipo1Tabla_estilos[cont]);
            }

            tablaTipo1.displayCabecera = true;
            tablaTipo1.height = 360;

            tablaTipo1.altoCabecera = 60;
            tablaTipo1.scrollWidth = 5000;
            tablaTipo1.dblClkFunction = 'dblClckTablaTipo1';
            tablaTipo1.lineas = listaTipo1Tabla;
            tablaTipo1.displayTabla();
            tablaTipo1.pack();
        }

        function recargarTablaTipos1(tipos1) {
            function limpiarNombreFichero(valor) {
                if (!valor) return "";
                return valor.replace(/["'<>]/g, '').split(/\s+/)[0];
            }

            var fila;
            divListaTipo1 = [];
            listaTipo1Tabla = [];
            listaTipo1Tabla_titulos = [];
            listaTipo1Tabla_estilos = [];

            for (var i = 0; i < tipos1.length; i++) {
                fila = tipos1[i];

                // limpiar siempre
                let fileCv = limpiarNombreFichero(fila.nombreFicheroCv);
                let fileDemanda = limpiarNombreFichero(fila.nombreFicheroDemanda);

                let lineaCv =
                    '<a href="javascript:obtainDocumentFile(\'' + fila.id +
                    '\', \'' + fileCv + '\', \'' + tipoDocCv + '\', \'' + tabla1 +
                    '\')">' + fileCv + '</a>';
                    '\', \'' + fileCv + '\', \'' + tipoDocCv + '\', \'' + tabla1 +
                let lineaDemanda =
                    '<a href="javascript:obtainDocumentFile(\'' + fila.id +
                    '\', \'' + fileDemanda + '\', \'' + tipoDocDemanda + '\', \'' +
                    tabla1 + '\')">' + fileDemanda + '</a>';

                divListaTipo1[i] = [
                    fila.id, fila.entbene, fila.entcontra, fila.cif, fila.ccc,
                    fila.denomproy, fila.denompuesto, fila.nombre, fila.apellido1,
                    fila.apellido2, fila.dni, fileCv, fila.fechaCvStr,
                    fileDemanda, fila.fechaDemandaStr, fila.naf,
                    fila.fecnacimiento, fila.sexo, fila.descSexo, fila.grupocot,
                    fila.descGrupocot, fila.fecinicio, fila.fecfin, fila.porcJorn,
                    fila.durcontrato, fila.edad, fila.municipio, fila.costesal,
                    fila.costess, fila.costetotal, fila.inscrita, fila.descInscrita,
                    fila.certinter, fila.subconcedida, fila.pago1, fila.subliquidada,
                    fila.pago2, fila.observaciones
                ];

                listaTipo1Tabla[i] = [
                    fila.entbene, fila.entcontra, fila.cif, fila.ccc, fila.denomproy,
                    fila.denompuesto, fila.nombre, fila.apellido1, fila.apellido2,
                    fila.dni, lineaCv, fila.fechaCvStr, lineaDemanda,
                    fila.fechaDemandaStr, fila.naf, fila.fecnacimientoStr,
                    fila.descSexo, fila.descGrupocot, fila.fecIniStr, fila.fecfinStr,
                    fila.porcJorn, fila.durcontrato, fila.edad, fila.municipio,
                    formatNumero(fila.costesal) + ' \u20ac',
                    formatNumero(fila.costess) + ' \u20ac',
                    formatNumero(fila.costetotal) + ' \u20ac',
                    (fila.descInscrita ?? "-"), (fila.certinter ?? "-"),
                    (fila.subconcedida != undefined ? formatNumero(fila.subconcedida) + ' \u20ac' : "-"),
                    (fila.pago1 != undefined ? formatNumero(fila.pago1) + ' \u20ac' : "-"),
                    (fila.subliquidada != undefined ? formatNumero(fila.subliquidada) + ' \u20ac' : "-"),
                    (fila.pago2 != undefined ? formatNumero(fila.pago2) + ' \u20ac' : "-"),
                    (fila.observaciones ?? "-")
                ];

                listaTipo1Tabla_titulos[i] = [
                    fila.entbene, fila.entcontra, fila.cif, fila.ccc, fila.denomproy,
                    fila.denompuesto, fila.nombre, fila.apellido1, fila.apellido2,
                    fila.dni, fileCv, fila.fechaCvStr, fileDemanda, fila.fechaDemandaStr,
                    fila.naf, fila.fecnacimientoStr, fila.descSexo, fila.descGrupocot,
                    fila.fecIniStr, fila.fecfinStr, fila.porcJorn, fila.durcontrato,
                    fila.edad, fila.municipio, formatNumero(fila.costesal) + ' \u20ac',
                    formatNumero(fila.costess) + ' \u20ac',
                    formatNumero(fila.costetotal) + ' \u20ac',
                    (fila.descInscrita ?? "-"), (fila.certinter ?? "-"),
                    (fila.subconcedida != undefined ? formatNumero(fila.subconcedida) + ' \u20ac' : "-"),
                    (fila.pago1 != undefined ? formatNumero(fila.pago1) + ' \u20ac' : "-"),
                    (fila.subliquidada != undefined ? formatNumero(fila.subliquidada) + ' \u20ac' : "-"),
                    (fila.pago2 != undefined ? formatNumero(fila.pago2) + ' \u20ac' : "-"),
                    (fila.observaciones ?? "-")
                ];

                listaTipo1Tabla_estilos[i] = [];
            }

            tablaTipo1.lineas = listaTipo1Tabla;
            tablaTipo1.displayTabla();
        }

        function dblClckTablaTipo1(rowID, tableName) {
            pulsarModificarTipo1();
        }


        function pulsarCargarXMLTipo1() {
            var hayFicheroSeleccionado = false;
            if (document.getElementById('fichero_xml').files) {
                if (document.getElementById('fichero_xml').files[0]) {
                    hayFicheroSeleccionado = true;
                }
            } else if (document.getElementById('fichero_xml').value != '') {
                hayFicheroSeleccionado = true;
            }
            if (hayFicheroSeleccionado) {
                var extension = document.getElementById('fichero_xml').value.split('.').pop().toLowerCase();
                if (extension != 'xml') {
                    var resultado = jsp_alerta('A', '<%=meLanbide81I18n.getMensaje(idiomaUsuario, "msg.extensionIncorrecta")%>');
                    return false;
                }
                var resultado = jsp_alerta('', '<%=meLanbide81I18n.getMensaje(idiomaUsuario, "msg.preguntaImportar")%>');
                if (resultado == 1) {
                    var parametros = 'tarea=preparar&modulo=MELANBIDE81&operacion=procesarXMLTipo1&tipo=0&numero=<%=numExpediente%>';
                    document.forms[0].action = url + '?' + parametros;
                    document.forms[0].enctype = 'multipart/form-data';
                    document.forms[0].encoding = 'multipart/form-data';
                    document.forms[0].method = 'POST';
                    document.forms[0].target = 'uploadFrameCarga1';
                    document.forms[0].submit();
                }
                return true;
            } else {
                jsp_alerta("A", '<%=meLanbide81I18n.getMensaje(idiomaUsuario,"msg.ficheroNoSeleccionado")%>');
                return false;
            }
        }

        function actualizarPestanaTipo1() {
            elementoVisible('on', 'barraProgresoLPEEL');
            var parametros = 'tarea=preparar&modulo=MELANBIDE81&operacion=actualizarPestanaTipo1&tipo=0&numExp=<%=numExpediente%>';
            try {
                $.ajax({
                    url: url,
                    type: 'POST',
                    async: true,
                    data: parametros,
                    success: procesarRespuestaActualizarPestana1,
                    error: mostrarErrorActualizarPestana1
                });
            } catch (Err) {
                elementoVisible('off', 'barraProgresoLPEEL');
                mostrarErrorPeticion();
            }
        }

        function actualizarTablaTipo1() {
            try {
                actualizarPestanaTipo1();
                limpiarFormularioTipo1();
            } catch (err) {
            }
        }

        function limpiarFormularioTipo1() {
            document.getElementById('fichero_xml').value = "";
        }

        // FUNCIONES QUE PROCESAN RESPUESTA O ERROR DE LA PETICION AJAX

        function procesarRespuestaActualizarPestana1(ajaxResult) {
            elementoVisible('off', 'barraProgresoLPEEL');

            try {
                var datos = JSON.parse(ajaxResult);
                var codigoOperacion = datos.tabla.codigoOperacion;

                var codigo = String(codigoOperacion);

                if (codigo === "0" || codigo === "5") {
                    var tipos1 = datos.tabla.lista || [];
                    recargarTablaTipos1(tipos1);
                }
                else if (codigo === "1") {
                    return;
                }
                else {
                    mostrarErrorPeticion(codigoOperacion);
                }

            } catch (err) {
                console.error('Error en procesarRespuestaActualizarPestana1:', err);
                mostrarErrorPeticion();
            }
        }


        function mostrarErrorActualizarPestana1() {
            elementoVisible('off', 'barraProgresoLPEEL');
            mostrarErrorPeticion();
        }

        function mostrarErrorEliminarTipo1() {
            elementoVisible('off', 'barraProgresoLPEEL');
            mostrarErrorPeticion("6");
        }

        function pulsarDatosCVIntermediacion(numPest) {
            pleaseWait('on');

            var dataParameter = $.extend({}, parametrosLLamadaM81);

            dataParameter.numero = document.forms[0].numero.value;
            dataParameter.control = new Date().getTime();
            dataParameter.operacion = 'recuperarDatosCVoDemandaIntermediacion';
            dataParameter.documento = tipoDocCv;

            var urlBaseLlamada = APP_CONTEXT_PATH + "/PeticionModuloIntegracion.do";

            $.ajax({
                type: 'POST',
                url: urlBaseLlamada,
                data: dataParameter,
                success: function (respuesta) {
                    console.log("recuperarDatosCVoDemandaIntermediacion respuesta = " + respuesta);
                    if (respuesta !== null) {
                        pleaseWait('off');
                        if (respuesta == "0") {
                            recargarDatosExpediente();
                        } else if (respuesta == "1") {
                            jsp_alerta("A", '<%=meLanbide67I18n.getMensaje(idiomaUsuario,"error.NotAvailableCV")%>');
                        } else if (respuesta == "-2") {
                            jsp_alerta("A", '<%=meLanbide67I18n.getMensaje(idiomaUsuario,"error.WSCVServiceNotAvailable")%>');
                        }
                    } else {
                        jsp_alerta("A", '<%=meLanbide67I18n.getMensaje(idiomaUsuario,"error.WSCVServiceNotAvailable")%>');
                        pleaseWait('off');
                    }
                },
                error: function (jqXHR, textStatus, errorThrown) {
                    jsp_alerta("A", '<%=meLanbide67I18n.getMensaje(idiomaUsuario,"error.WSCVServiceNotAvailable")%>');
                    pleaseWait('off');
                },
                async: true
            });
        }

        function pulsarCertificadoDemanda(numPest) {
            pleaseWait('on');

            var dataParameter = $.extend({}, parametrosLLamadaM81);

            dataParameter.numero = document.forms[0].numero.value;
            dataParameter.control = new Date().getTime();
            dataParameter.operacion = 'recuperarDatosCVoDemandaIntermediacion';
            dataParameter.documento = tipoDocDemanda;

            var urlBaseLlamada = APP_CONTEXT_PATH + "/PeticionModuloIntegracion.do";

            $.ajax({
                type: 'POST',
                url: urlBaseLlamada,
                data: dataParameter,
                success: function (respuesta) {
                    console.log("recuperarDatosCVoDemandaIntermediacion respuesta = " + respuesta);
                    if (respuesta !== null) {
                        pleaseWait('off');
                        if (respuesta == "0") {
                            recargarDatosExpediente();
                        } else if (respuesta == "1") {
                            jsp_alerta("A", '<%=meLanbide67I18n.getMensaje(idiomaUsuario,"error.NotAvailableDemand")%>');
                        } else if (respuesta == "-2") {
                            jsp_alerta("A", '<%=meLanbide67I18n.getMensaje(idiomaUsuario,"error.WSDemandServiceNotAvailable")%>');
                        }
                    } else {
                        jsp_alerta("A", '<%=meLanbide67I18n.getMensaje(idiomaUsuario,"error.WSDemandServiceNotAvailable")%>');
                        pleaseWait('off');
                    }
                },
                error: function (jqXHR, textStatus, errorThrown) {
                    jsp_alerta("A", '<%=meLanbide67I18n.getMensaje(idiomaUsuario,"error.WSDemandServiceNotAvailable")%>');
                    pleaseWait('off');
                },
                async: true
            });
        }

        function obtainDocumentFile(id, nameFile, documento, taboa) {
            var dataParameter = $.extend({}, parametrosLLamadaM81);
            dataParameter.numero = document.forms[0].numero.value;
            dataParameter.id = id;
            dataParameter.nameFile = nameFile;
            dataParameter.documento = documento;
            dataParameter.tabla = taboa;
            dataParameter.control = new Date().getTime();
            dataParameter.operacion = 'obtainDocumentFile';
            window.open($("#urlBaseLlamadaM81").val() + "?" + $.param(dataParameter), "_blank");
        }

        function recargarDatosExpediente() {
            pleaseWait('on');
            document.forms[0].opcion.value = "cargarPestTram";
            document.forms[0].target = "mainFrame";
            document.forms[0].action = "<c:url value='/sge/FichaExpediente.do'/>";
            document.forms[0].submit();
        }
    </script>
</head>
<body class="bandaBody">
<input type="hidden" id="urlBaseLlamadaM81" name="urlBaseLlamadaM81" value="<%=request.getContextPath()+"/PeticionModuloIntegracion.do"%>"/>
<div class="tab-page" id="tabPage81-3" style="height:520px; width: 100%;">
    <br/>
    <h2 class="legendAzul"><%=meLanbide81I18n.getMensaje(idiomaUsuario, "label.titulo.pestanaAportacionContrataciones1")%></h2>
    <br/>
    <div style="clear: both;">
        <div id="divListaTipo1" name="divListaTipo1" align="center" style="margin: 5px;"></div>
    </div>
    <div class="botonera centrarElementos">
        <input type="button" id="btnNuevoTipo1" name="btnNuevoTipo1" class="botonGeneral" value="<%=meLanbide81I18n.getMensaje(idiomaUsuario, "btn.nuevo")%>" onclick="pulsarNuevoTipo1();">
        <input type="button" id="btnModificarTipo1" name="btnModificarTipo1" class="botonGeneral" value="<%=meLanbide81I18n.getMensaje(idiomaUsuario, "btn.modificar")%>" onclick="pulsarModificarTipo1();">
        <input type="button" id="btnEliminarTipo1" name="btnEliminarTipo1" class="botonGeneral" value="<%=meLanbide81I18n.getMensaje(idiomaUsuario, "btn.eliminar")%>" onclick="pulsarEliminarTipo1();">
        <input type="button" id="btnpulsarExportarExcell" name="btnpulsarExportarExcell" class="botonLargo" value="<%=meLanbide81I18n.getMensaje(idiomaUsuario, "btn.exportar")%>" onclick="pulsarExportarExcellApor();">
    </div>
    <br>
    <div class="botonera" style="text-align: center;">
        <input type="button" id="btnCargarRegistros1" name="btnCargarRegistros1" class="botonGeneral" value="<%=meLanbide81I18n.getMensaje(idiomaUsuario, "btn.cargarXML")%>" onclick="pulsarCargarXMLTipo1();">
        <input type="file" name="fichero_xml" id="fichero_xml" class="inputTexto" size="60" accept=".xml">
    </div>
    <br>
    <div class="botonera" style="text-align: center;">
        <input type="button" id="botonDatosCVIntermediacion" name="botonDatosCVIntermediacion" value="<%=meLanbide67I18n.getMensaje(idiomaUsuario,"label.extension.boton.datosCVIntermediacion")%>" onclick="pulsarDatosCVIntermediacion();">
        </input>
        <input type="button" id="botonCertificadoDemanda" name="botonCertificadoDemanda" value="<%=meLanbide67I18n.getMensaje(idiomaUsuario,"label.extension.boton.botonCertificadoDemanda")%>" onclick="pulsarCertificadoDemanda();">
        </input>
    </div>
</div>
<iframe id="uploadFrameCarga1" name="uploadFrameCarga1" height="0" width="0" frameborder="0" scrolling="yes"></iframe>
<script type="text/javascript">
    <%
        Tipo1VO tipo1VO = null;
        List<Tipo1VO> List = null;
        if (request.getAttribute("divListaTipo1") != null) {
            List = (List<Tipo1VO>) request.getAttribute("divListaTipo1");
        }
        if (List != null && List.size() > 0) {
            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            String barraSeparadoraDobleIdiomaDesple = ConfigurationParameter.getParameter(ConstantesMeLanbide81.BARRA_SEPARADORA_IDIOMA, ConstantesMeLanbide81.FICHERO_PROPIEDADES);

            for (int indice = 0; indice < List.size(); indice++) {
                tipo1VO = List.get(indice);

                String entbene = "";
                if (tipo1VO.getEntbene() != null) {
                    entbene = tipo1VO.getEntbene();
                }

                String entcontra = "";
                if (tipo1VO.getEntcontra() != null) {
                    entcontra = tipo1VO.getEntcontra();
                }

                String cif = "";
                if (tipo1VO.getCif() != null) {
                    cif = tipo1VO.getCif();
                }
                String ccc = "";
                if (tipo1VO.getCcc() != null) {
                    ccc = tipo1VO.getCcc();
                }

                String denomproy = "";
                if (tipo1VO.getDenomproy() != null) {
                    denomproy = tipo1VO.getDenomproy().replaceAll("\\n", " ");
                }

                String denompuesto = "";
                if (tipo1VO.getDenompuesto() != null) {
                    denompuesto = tipo1VO.getDenompuesto().replaceAll("\\n", " ");
                }

                String nombre = "";
                if (tipo1VO.getNombre() != null) {
                    nombre = tipo1VO.getNombre();
                }
                String apellido1 = "";
                if (tipo1VO.getApellido1() != null) {
                    apellido1 = tipo1VO.getApellido1();
                }

                String apellido2 = "";
                if (tipo1VO.getApellido2() != null) {
                    apellido2 = tipo1VO.getApellido2();
                }

                String dni = "";
                if (tipo1VO.getDni() != null) {
                    dni = tipo1VO.getDni();
                }

                String naf = "";
                if (tipo1VO.getNaf() != null) {
                    naf = tipo1VO.getNaf();
                }

                String nombreFicheroCv = "";
                if (tipo1VO.getNombreFicheroCv() != null) {
                    nombreFicheroCv = tipo1VO.getNombreFicheroCv()
                                             .replaceAll("[\"'<>]", "")
                                             .split("\\s+")[0];
                }

                String fechaCv = "";
                if (tipo1VO.getFechaCv() != null) {
                    fechaCv = dateFormat.format(tipo1VO.getFechaCv());
                }

                String nombreFicheroDemanda = "";
                if (tipo1VO.getNombreFicheroDemanda() != null) {
                    nombreFicheroDemanda = tipo1VO.getNombreFicheroDemanda()
                                                  .replaceAll("[\"'<>]", "")
                                                  .split("\\s+")[0];
                }

                String fechaDemanda = "";
                if (tipo1VO.getFechaDemanda() != null) {
                    fechaDemanda = dateFormat.format(tipo1VO.getFechaDemanda());
                }

                String fecnacimiento = "";
                if (tipo1VO.getFecnacimiento() != null) {
                    fecnacimiento = dateFormat.format(tipo1VO.getFecnacimiento());
                }

                String sexo = "";
                if (tipo1VO.getDescSexo() != null) {
                    String descripcion = tipo1VO.getDescSexo();
                    String[] descripcionDobleIdioma = (descripcion != null ? descripcion.split(barraSeparadoraDobleIdiomaDesple) : null);
                    if (descripcionDobleIdioma != null && descripcionDobleIdioma.length > 1) {
                        if (idiomaUsuario == ConstantesMeLanbide81.CODIGO_IDIOMA_CASTELLANO) {
                            descripcion = descripcionDobleIdioma[1];
                        } else {
                            // Cogemos la primera posición que debería ser castellano
                            descripcion = descripcionDobleIdioma[0];
                        }
                    }
                    sexo = descripcion;
                }

                String grupocot = "";
                if (tipo1VO.getDescGrupocot() != null) {
                    String descripcion = tipo1VO.getDescGrupocot();
                    String[] descripcionDobleIdioma = (descripcion != null ? descripcion.split(barraSeparadoraDobleIdiomaDesple) : null);
                    if (descripcionDobleIdioma != null && descripcionDobleIdioma.length > 1) {
                        if (idiomaUsuario == ConstantesMeLanbide81.CODIGO_IDIOMA_CASTELLANO) {
                            descripcion = descripcionDobleIdioma[1];
                        } else {
                            // Cogemos la primera posición que debería ser castellano
                            descripcion = descripcionDobleIdioma[0];
                        }
                    }
                    grupocot = descripcion;
                }

                String fecinicio = "";
                if (tipo1VO.getFecinicio() != null) {
                    fecinicio = dateFormat.format(tipo1VO.getFecinicio());
                }

                String fecfin = "";
                if (tipo1VO.getFecfin() != null) {
                    fecfin = dateFormat.format(tipo1VO.getFecfin());
                }

                String porcJorn = "";
                if (tipo1VO.getPorcJorn() != null) {
                    porcJorn = String.valueOf((tipo1VO.getPorcJorn().toString()).replace(".", ","));
                }

                String durcontrato = "";
                if (tipo1VO.getDurcontrato() != null) {
                    durcontrato = tipo1VO.getDurcontrato();
                }

                String edad = "";
                if (tipo1VO.getEdad() != null && !"".equals(tipo1VO.getEdad())) {
                    edad = Integer.toString(tipo1VO.getEdad());
                }

                String municipio = "";
                if (tipo1VO.getMunicipio() != null) {
                    municipio = tipo1VO.getMunicipio();
                }

                String costesal = "";
                if (tipo1VO.getCostesal() != null) {
                    costesal = formateador.format(tipo1VO.getCostesal());
                }

                String costess = "";
                if (tipo1VO.getCostess() != null) {
                    costess = formateador.format(tipo1VO.getCostess());
                }

                String costetotal = "";
                if (tipo1VO.getCostetotal() != null) {
                    costetotal = formateador.format(tipo1VO.getCostetotal());
                }

                String inscrita = "";
                if (tipo1VO.getDescInscrita() != null) {
                    String descripcion = tipo1VO.getDescInscrita();
                    String[] descripcionDobleIdioma = (descripcion != null ? descripcion.split(barraSeparadoraDobleIdiomaDesple) : null);
                    if (descripcionDobleIdioma != null && descripcionDobleIdioma.length > 1) {
                        if (idiomaUsuario == ConstantesMeLanbide81.CODIGO_IDIOMA_CASTELLANO) {
                            descripcion = descripcionDobleIdioma[1];
                        } else {
                            // Cogemos la primera posición que debería ser castellano
                            descripcion = descripcionDobleIdioma[0];
                        }
                    }
                    inscrita = descripcion;
                } else {
                    inscrita = "-";
                }

                String certinter = "";
                if (tipo1VO.getCertinter() != null) {
                    certinter = tipo1VO.getCertinter();
                }

                String subconcedida = "";
                if (tipo1VO.getSubconcedida() != null) {
                    subconcedida = formateador.format(tipo1VO.getSubconcedida());
                }

                String pago1 = "";
                if (tipo1VO.getPago1() != null) {
                    pago1 = formateador.format(tipo1VO.getPago1());
                }

                String subliquidada = "";
                if (tipo1VO.getSubliquidada() != null) {
                    subliquidada = formateador.format(tipo1VO.getSubliquidada());
                }

                String pago2 = "";
                if (tipo1VO.getPago2() != null) {
                    pago2 = formateador.format(tipo1VO.getPago2());
                }

                String observaciones = "";
                if (tipo1VO.getObservaciones() != null) {
                    observaciones = tipo1VO.getObservaciones().replaceAll("\\n", " ");
                }
    %>
    divListaTipo1[<%=indice%>] = [
        '<%=tipo1VO.getId()%>',
        '<%=entbene%>',
        '<%=entcontra%>',
        '<%=cif%>',
        '<%=ccc%>',
        '<%=denomproy%>',
        '<%=denompuesto%>',
        '<%=nombre%>',
        '<%=apellido1%>',
        '<%=apellido2%>',
        '<%=dni%>',
        '<%=nombreFicheroCv%>',
        '<%=fechaCv%>',
        '<%=nombreFicheroDemanda%>',
        '<%=fechaDemanda%>',
        '<%=naf%>',
        '<%=fecnacimiento%>',
        '<%=sexo%>',
        '<%=grupocot%>',
        '<%=fecinicio%>',
        '<%=fecfin%>',
        '<%=porcJorn%>',
        '<%=durcontrato%>',
        '<%=edad%>',
        '<%=municipio%>',
        '<%=costesal%>',
        '<%=costess%>',
        '<%=costetotal%>',
        '<%=inscrita%>',
        '<%=certinter%>',
        '<%=subconcedida%>',
        '<%=pago1%>',
        '<%=subliquidada%>',
        '<%=pago2%>',
        '<%=observaciones%>'
    ];
    listaTipo1Tabla[<%=indice%>] = [
        '<%=entbene%>',
        '<%=entcontra%>',
        '<%=cif%>',
        '<%=ccc%>',
        '<%=denomproy%>',
        '<%=denompuesto%>',
        '<%=nombre%>',
        '<%=apellido1%>',
        '<%=apellido2%>',
        '<%=dni%>',
        '<a href="javascript:obtainDocumentFile(\'<%=tipo1VO.getId()%>\', \'<%=nombreFicheroCv%>\', \'<%=tipoDocCv%>\', \'<%=tabla%>\');"><%=nombreFicheroCv%></a>',
        '<%=fechaCv%>',
        '<a href="javascript:obtainDocumentFile(\'<%=tipo1VO.getId()%>\', \'<%=nombreFicheroDemanda%>\', \'<%=tipoDocDemanda%>\', \'<%=tabla%>\');"><%=nombreFicheroDemanda%></a>',
        '<%=fechaDemanda%>',
        '<%=naf%>',
        '<%=fecnacimiento%>',
        '<%=sexo%>',
        '<%=grupocot%>',
        '<%=fecinicio%>',
        '<%=fecfin%>',
        '<%=porcJorn%>',
        '<%=durcontrato%>',
        '<%=edad%>',
        '<%=municipio%>',
        '<%=costesal%>' + ' \u20ac',
        '<%=costess%>' + ' \u20ac',
        '<%=costetotal%>' + ' \u20ac',
        '<%=inscrita%>',
        '<%=certinter%>',
        '<%=subconcedida%>' + ' \u20ac',
        '<%=pago1%>' + ' \u20ac',
        '<%=subliquidada%>' + ' \u20ac',
        '<%=pago2%>' + ' \u20ac',
        '<%=observaciones%>'
    ];
    listaTipo1Tabla_titulos[<%=indice%>] = [
        '<%=entbene%>',
        '<%=entcontra%>',
        '<%=cif%>',
        '<%=ccc%>',
        '<%=denomproy%>',
        '<%=denompuesto%>',
        '<%=nombre%>',
        '<%=apellido1%>',
        '<%=apellido2%>',
        '<%=dni%>',
        '<%=nombreFicheroCv%>',
        '<%=fechaCv%>',
        '<%=nombreFicheroDemanda%>',
        '<%=fechaDemanda%>',
        '<%=naf%>',
        '<%=fecnacimiento%>',
        '<%=sexo%>',
        '<%=grupocot%>',
        '<%=fecinicio%>',
        '<%=fecfin%>',
        '<%=porcJorn%>',
        '<%=durcontrato%>',
        '<%=edad%>',
        '<%=municipio%>',
        '<%=costesal%>' + ' \u20ac',
        '<%=costess%>' + ' \u20ac',
        '<%=costetotal%>' + ' \u20ac',
        '<%=inscrita%>',
        '<%=certinter%>',
        '<%=subconcedida%>' + ' \u20ac',
        '<%=pago1%>' + ' \u20ac',
        '<%=subliquidada%>' + ' \u20ac',
        '<%=pago2%>' + ' \u20ac',
        '<%=observaciones%>'
    ];
    <%
            }
        }
    %>
    crearTablaTipo1();
</script>
<div id="popupcalendar" class="text"></div>
</body>
</html>

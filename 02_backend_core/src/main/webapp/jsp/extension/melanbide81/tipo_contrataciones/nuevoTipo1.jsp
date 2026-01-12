<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide81.i18n.MeLanbide81I18n" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.common.service.config.Config"%>
<%@page import="es.altia.common.service.config.ConfigServiceHelper"%>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide81.vo.Tipo1VO"%>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide81.vo.DesplegableVO" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide81.util.ConstantesMeLanbide81" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide81.util.ConfigurationParameter" %>
<%@page import="java.util.ArrayList" %>
<%@page import="java.util.List" %>
<%@page import="java.text.DateFormat" %>
<%@page import="java.text.SimpleDateFormat" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="X-UA-Compatible" content="IE=Edge" />
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <%
        UsuarioValueObject usuarioVO = new UsuarioValueObject();
        Tipo1VO datModif = new Tipo1VO();
        int idiomaUsuario = 1;
        int apl = 5;
        String css = "";
        String nuevo = "";
        String numExpediente = "";
        String idProyecto = "";
        SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
        try {
            if (session.getAttribute("usuario") != null) {
                usuarioVO = (UsuarioValueObject) session.getAttribute("usuario");
                apl = usuarioVO.getAppCod();
                idiomaUsuario = usuarioVO.getIdioma();
                css = usuarioVO.getCss();
            }
            nuevo = (String)request.getAttribute("nuevo");
            numExpediente = (String)request.getAttribute("numExp");
            idProyecto = (String)request.getAttribute("idProyecto");
            if(request.getAttribute("datModif") != null) {
                datModif = (Tipo1VO)request.getAttribute("datModif");
            }
            String fecnacimiento = "";
            if (datModif.getFecnacimiento()!=null){
                fecnacimiento = formatoFecha.format(datModif.getFecnacimiento());
            }

            String fechaInicioContrato = "";
            if (datModif.getFecinicio()!=null){
                fechaInicioContrato = formatoFecha.format(datModif.getFecinicio());
            }

            String fechaFinContrato = "";
            if (datModif.getFecfin()!=null){
                fechaFinContrato = formatoFecha.format(datModif.getFecfin());
            }

        }catch(Exception ex) {
        }

        //Clase para internacionalizar los mensajes de la aplicaciÃ³n.
        MeLanbide81I18n meLanbide81I18n = MeLanbide81I18n.getInstance();
    %>
    <jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
    <jsp:setProperty name="descriptor"  property="idi_cod" value="<%=idiomaUsuario%>" />
    <jsp:setProperty name="descriptor"  property="apl_cod" value="<%=apl%>" />
    <link rel="StyleSheet" media="screen" type="text/css" href="<%=request.getContextPath()%><%=css%>">
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/css/font-awesome.min.css'/>" media="screen">
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/less/animated.less'/>" media="screen">
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/>
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/extension/melanbide81/melanbide81.css"/>
    <script type="text/javascript" src="<c:url value='/scripts/listaComboBox.js'/>"></script>
    <script type="text/javascript" src="<c:url value='/scripts/calendario.js'/>"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/listaComboBox.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/general.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/melanbide81/LpeelUtils.js"></script>
    <script type="text/javascript">
        var APP_CONTEXT_PATH = '<%=request.getContextPath()%>';
        var url = APP_CONTEXT_PATH + '/PeticionModuloIntegracion.do';
        var mensajeValidacion = '';

        //Desplegable  sexo
        var comboListaSexo;
        var listaCodigosSexo = new Array();
        var listaDescripcionesSexo = new Array();
        function buscaCodigoSexo(tipo) {
            comboListaSexo.buscaCodigo(tipo);
        }
        function cargarDatosSexo() {
            var tipoSeleccionado = document.getElementById("codListaSexo").value;
            buscaCodigoSexo(tipoSeleccionado);
        }
        //Desplegable  grupo de cotizacion
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
        //Desplegable  si/no
        var comboListaInscrita;
        var listaCodigosInscrita = new Array();
        var listaDescripcionesInscrita = new Array();
        function buscaCodigoInscrita(tipo) {
            comboListaInscrita.buscaCodigo(tipo);
        }
        function cargarDatosInscrita() {
            var tipoSeleccionado = document.getElementById("codListaInscrita").value;
            buscaCodigoInscrita(tipoSeleccionado);
        }

        function guardarTipo1() {
            if (validarDatosTipo1()) {
                elementoVisible('on', 'barraProgresoLPEEL');
                var parametros = "";
                if ('<%=nuevo%>' != null && '<%=nuevo%>' == "1") {
                    parametros = "tarea=preparar&modulo=MELANBIDE81&operacion=crearNuevoTipo1&tipo=0";
                } else {
                    parametros = "tarea=preparar&modulo=MELANBIDE81&operacion=modificarTipo1&tipo=0"
                        + "&id=<%=datModif != null && datModif.getId() != null ? datModif.getId().toString() : ""%>";
                }
                parametros += "&numExp=<%=numExpediente%>"
                    + "&entbene=" + document.getElementById('entidadLocalBeneficiaria').value
                    + "&entcontra=" + document.getElementById('entidadcontratante').value
                    + "&cif=" + document.getElementById('cif').value
                    + "&ccc=" + document.getElementById('ccc').value
                    + "&denomproy=" + document.getElementById('denominacionDelProyecto').value.replace(/\r?\n|\r/g, " ").trim()
                    + "&denompuesto=" + document.getElementById('denominacionDelPuestoDeTrabajo').value
                    + "&nombre=" + document.getElementById('nombre').value
                    + "&apellido1=" + document.getElementById('apellido1').value
                    + "&apellido2=" + document.getElementById('apellido2').value
                    + "&dni=" + document.getElementById('dni').value
                    + "&naf=" + document.getElementById('naf').value
                    + "&fecnacimiento=" + document.getElementById('fecnacimiento').value
                    + "&sexo=" + document.getElementById('codListaSexo').value
                    + "&grupocot=" + document.getElementById('codListaGrupoCotizacion').value
                    + "&fecinicio=" + document.getElementById('fechaInicioContrato').value
                    + "&fecfin=" + document.getElementById('fechaFinContrato').value
                    + "&porcJorn=" + document.getElementById('porcJorn').value
                    + "&durcontrato=" + document.getElementById('duracionDelContrato').value
                    + "&edad=" + document.getElementById('edad').value
                    + "&municipio=" + document.getElementById('municipioDeResidencia').value
                    + "&costesal=" + document.getElementById('costeSalarial').value
                    + "&costess=" + document.getElementById('costeSeguridadSocial').value
                    + "&costetotal=" + document.getElementById('costeTotal').value
                    + "&inscrita=" + document.getElementById('codListaInscrita').value
                    + "&certinter=" + document.getElementById('certInterventor').value
                    + "&subconcedida=" + document.getElementById('subvencionConcedida').value
                    + "&pago1=" + document.getElementById('priPago').value
                    + "&subliquidada=" + document.getElementById('subLiquidada').value
                    + "&pago2=" + document.getElementById('segunPago').value
                    + "&observaciones=" + document.getElementById('observaciones').value
                ;

                console.log('guardarTipo1 parametros = ' + parametros);
                try {
                    $.ajax({
                        url: url,
                        type: 'POST',
                        async: true,
                        data: parametros,
                        success: procesarRespuestaAltaModificacion,
                        error: mostrarErrorAltaModificacion
                    });
                } catch (Err) {
                    elementoVisible('off', 'barraProgresoLPEEL');
                    mostrarErrorPeticion();
                }
            } else {
                jsp_alerta("A", mensajeValidacion);
            }
        }

        function validarDatosTipo1() {
            mensajeValidacion = "";
            var correcto = true;
            campo = document.getElementById('entidadLocalBeneficiaria');
            if (campo.value == null || campo.value == '') {
                mensajeValidacion = '<%=meLanbide81I18n.getMensaje(idiomaUsuario, "msg.entidadLocalBe")%>';
                return false;
            } else if (!compruebaTamanoCampo(campo, 100)) {
                mensajeValidacion = '<%=meLanbide81I18n.getMensaje(idiomaUsuario, "msg.entidadLocalBe.tamano")%>';
                return false;
            }
            campo = document.getElementById('entidadcontratante');
            if (campo.value == null || campo.value == '') {
                mensajeValidacion = '<%=meLanbide81I18n.getMensaje(idiomaUsuario, "msg.entidadContratante")%>';
                return false;
            } else if (!compruebaTamanoCampo(campo, 100)) {
                mensajeValidacion = '<%=meLanbide81I18n.getMensaje(idiomaUsuario, "msg.entidadContratante.tamano")%>';
                return false;
            }

            //CIF
            if (document.getElementById('cif') != null && (document.getElementById('cif').value == "" || document.getElementById('cif').value == null || document.getElementById('cif').value == undefined)) {
                mensajeValidacion = '<%=meLanbide81I18n.getMensaje(idiomaUsuario, "msg.cifObligatorio")%>';
                return false;
            } else if (!compruebaTamanoCampo(document.getElementById('cif'), (9))) {
                mensajeValidacion = '<%=meLanbide81I18n.getMensaje(idiomaUsuario, "msg.cif.tamano")%>';
                return false;
            }


            campo = document.getElementById('ccc').value;
            if (campo == null || campo == '') {
                mensajeValidacion = '<%=meLanbide81I18n.getMensaje(idiomaUsuario, "msg.ccc")%>';
                return false;
            } else if (!compruebaTamanoCampo(document.getElementById('ccc'), (11))) {
                mensajeValidacion = '<%=meLanbide81I18n.getMensaje(idiomaUsuario, "msg.ccc.tamano")%>';
                return false;
            }
            campo = document.getElementById('denominacionDelProyecto');
            if (campo.value == null || campo.value == '') {
                mensajeValidacion = '<%=meLanbide81I18n.getMensaje(idiomaUsuario, "msg.denomProyecto")%>';
                return false;
            } else if (!compruebaTamanoCampo(campo, 250)) {
                mensajeValidacion = '<%=meLanbide81I18n.getMensaje(idiomaUsuario, "msg.denomProyecto.tamano")%>';
                return false;
            }

            campo = document.getElementById('denominacionDelPuestoDeTrabajo');
            if (campo.value == null || campo.value == '') {
                mensajeValidacion = '<%=meLanbide81I18n.getMensaje(idiomaUsuario, "msg.denomPuesto")%>';
                return false;
            } else if (!compruebaTamanoCampo(campo, 200)) {
                mensajeValidacion = '<%=meLanbide81I18n.getMensaje(idiomaUsuario, "msg.denomPuesto.tamano")%>';
                return false;
            }
            campo = document.getElementById('nombre');
            if (campo.value == null || campo.value == '') {
                mensajeValidacion = '<%=meLanbide81I18n.getMensaje(idiomaUsuario, "msg.nombre")%>';
                return false;
            } else if (!compruebaTamanoCampo(campo, 80)) {
                mensajeValidacion = '<%=meLanbide81I18n.getMensaje(idiomaUsuario, "msg.nombre.tamano")%>';
                return false;
            }
            campo = document.getElementById('apellido1');
            if (campo.value == null || campo.value == '') {
                mensajeValidacion = '<%=meLanbide81I18n.getMensaje(idiomaUsuario, "msg.apellido1")%>';
                return false;
            } else if (!compruebaTamanoCampo(campo, 50)) {
                mensajeValidacion = '<%=meLanbide81I18n.getMensaje(idiomaUsuario, "msg.apellido1.tamano")%>';
                return false;
            }
            campo = document.getElementById('apellido2');
            if (campo.value == null || campo.value == '') {
                mensajeValidacion = '<%=meLanbide81I18n.getMensaje(idiomaUsuario, "msg.apellido2")%>';
                return false;
            } else if (!compruebaTamanoCampo(campo, 50)) {
                mensajeValidacion = '<%=meLanbide81I18n.getMensaje(idiomaUsuario, "msg.apellido2.tamano")%>';
                return false;
            }
            campo = document.getElementById('dni').value;
            if (campo == null || campo == '') {
                mensajeValidacion = '<%=meLanbide81I18n.getMensaje(idiomaUsuario, "msg.dniObligatorio")%>';
                return false;
            } else if (!validarDniNie(document.getElementById('dni'))) {
                mensajeValidacion = '<%=meLanbide81I18n.getMensaje(idiomaUsuario, "msg.dniIncorrecto")%>';
                return false;
            }
            campo = document.getElementById('naf');
            if (campo.value == null || campo.value == '') {
                mensajeValidacion = '<%=meLanbide81I18n.getMensaje(idiomaUsuario, "msg.naf")%>';
                return false;
            } else if (!compruebaTamanoCampo(campo, 12)) {
                mensajeValidacion = '<%=meLanbide81I18n.getMensaje(idiomaUsuario, "msg.naf.tamano")%>';
                return false;
            }
            campo = document.getElementById('fecnacimiento').value;
            if (campo == null || campo == '') {
                mensajeValidacion = '<%=meLanbide81I18n.getMensaje(idiomaUsuario, "msg.fechaNacimiento")%>';
                return false;
            }
            campo = document.getElementById('codListaSexo').value;
            if (campo == null || campo == '') {
                mensajeValidacion = '<%=meLanbide81I18n.getMensaje(idiomaUsuario, "msg.sexo")%>';
                return false;
            }
            campo = document.getElementById('codListaGrupoCotizacion').value;
            if (campo == null || campo == '') {
                mensajeValidacion = '<%=meLanbide81I18n.getMensaje(idiomaUsuario, "msg.listGrupoDeCotizacion")%>';
                return false;
            }
            campo = document.getElementById('fechaInicioContrato').value;
            if (campo == null || campo == '') {
                mensajeValidacion = '<%=meLanbide81I18n.getMensaje(idiomaUsuario, "msg.fechaInicioContrato")%>';
                return false;
            }
            campo = document.getElementById('fechaFinContrato').value;
            if (campo == null || campo == '') {
                mensajeValidacion = '<%=meLanbide81I18n.getMensaje(idiomaUsuario, "msg.fechaFinContrato")%>';
                return false;
            }
            campo = document.getElementById('porcJorn');
            if (campo.value == null || campo.value == '') {
                mensajeValidacion = '<%=meLanbide81I18n.getMensaje(idiomaUsuario, "msg.porcJorn")%>';
                return false;
            } else if (!validarNumericoDecimalPrecision(campo.value, 6, 2)) {
                mensajeValidacion = '<%=meLanbide81I18n.getMensaje(idiomaUsuario, "msg.porcJorn.formato")%>';
                return false;
            } else if (!validarNumericoPorcentajeCien(parseFloat(campo.value.replaceAll("\\.", ",")))) {
                mensajeValidacion = '<%=meLanbide81I18n.getMensaje(idiomaUsuario, "msg.porcJorn.errRango")%>';
                return false;
            }

            campo = document.getElementById('duracionDelContrato');
            if (campo.value == null || campo.value == '') {
                mensajeValidacion = '<%=meLanbide81I18n.getMensaje(idiomaUsuario, "msg.duracionDelContrato")%>';
                return false;
            } else if (!compruebaTamanoCampo(campo, 50)) {
                mensajeValidacion = '<%=meLanbide81I18n.getMensaje(idiomaUsuario, "msg.duracionDelContrato.tamano")%>';
                return false;
            }

            campo = document.getElementById('edad');
            if (campo.value == null || campo.value == '') {
                mensajeValidacion = '<%=meLanbide81I18n.getMensaje(idiomaUsuario, "msg.edad")%>';
                return false;
            } else if (!compruebaTamanoCampo(campo, 2)) {
                mensajeValidacion = '<%=meLanbide81I18n.getMensaje(idiomaUsuario, "msg.edad.tamano")%>';
                return false;
            }

            campo = document.getElementById('costeSalarial');
            if (campo.value == null || campo.value == '') {
                mensajeValidacion = '<%=meLanbide81I18n.getMensaje(idiomaUsuario, "msg.costeSalarial")%>';
                return false;
            } else if (!validarNumericoDecimalPrecision(campo.value, 8, 2)) {
                mensajeValidacion = '<%=meLanbide81I18n.getMensaje(idiomaUsuario, "msg.costeSalarial.formato")%>';
                return false;
            }

            campo = document.getElementById('costeSeguridadSocial');
            if (campo.value == null || campo.value == '') {
                mensajeValidacion = '<%=meLanbide81I18n.getMensaje(idiomaUsuario, "msg.costeSeguridadSocial")%>';
                return false;
            } else if (!validarNumericoDecimalPrecision(campo.value, 8, 2)) {
                mensajeValidacion = '<%=meLanbide81I18n.getMensaje(idiomaUsuario, "msg.costeSeguridadSocial.formato")%>';
                return false;
            }

            campo = document.getElementById('costeTotal');
            if (campo.value == null || campo.value == '') {
                mensajeValidacion = '<%=meLanbide81I18n.getMensaje(idiomaUsuario, "msg.costeTotal")%>';
                return false;
            } else if (!validarNumericoDecimalPrecision(campo.value, 8, 2)) {
                mensajeValidacion = '<%=meLanbide81I18n.getMensaje(idiomaUsuario, "msg.costeTotal.formato")%>';
                return false;
            }

            campo = document.getElementById('certInterventor');
            if (campo.value != null && campo.value != '') {
                if (!compruebaTamanoCampo(campo, 50)) {
                    mensajeValidacion = '<%=meLanbide81I18n.getMensaje(idiomaUsuario, "msg.certInterventor.tamano")%>';
                    return false;
                }
            }

            campo = document.getElementById('subvencionConcedida');
            if (campo.value != null && campo.value != '') {
                if (!validarNumericoDecimalPrecision(campo.value, 8, 2)) {
                    mensajeValidacion = '<%=meLanbide81I18n.getMensaje(idiomaUsuario, "msg.subvencionConcedida.formato")%>';
                    return false;
                }
            }

            campo = document.getElementById('priPago');
            if (campo.value != null && campo.value != '') {
                if (!validarNumericoDecimalPrecision(campo.value, 8, 2)) {
                    mensajeValidacion = '<%=meLanbide81I18n.getMensaje(idiomaUsuario, "msg.priPago.formato")%>';
                    return false;
                }
            }

            campo = document.getElementById('subLiquidada');
            if (campo.value != null && campo.value != '') {
                if (!validarNumericoDecimalPrecision(campo.value, 8, 2)) {
                    mensajeValidacion = '<%=meLanbide81I18n.getMensaje(idiomaUsuario, "msg.subLiquidada.formato")%>';
                    return false;
                }
            }

            campo = document.getElementById('segunPago');
            if (campo.value != null && campo.value != '') {
                if (!validarNumericoDecimalPrecision(campo.value, 8, 2)) {
                    mensajeValidacion = '<%=meLanbide81I18n.getMensaje(idiomaUsuario, "msg.segunPago.formato")%>';
                    return false;
                }
            }

            campo = document.getElementById('observaciones');
            if (campo.value != null && campo.value != '') {
                if (!compruebaTamanoCampo(campo, 400)) {
                    mensajeValidacion = '<%=meLanbide81I18n.getMensaje(idiomaUsuario, "msg.observaciones.tamano")%>';
                    return false;
                }
            }
            return true;
        }

        function cancelarTipo1() {
            var resultado = jsp_alerta('', '<%=meLanbide81I18n.getMensaje(idiomaUsuario, "msg.preguntaCancelar")%>');
            if (resultado == 1) {
                cerrarVentana();
            }
        }

        function rellenarDesplegables() {
            if ('<%=datModif%>' != null) {
                buscaCodigoSexo('<%=datModif.getSexo() != null ? datModif.getSexo() : ""%>');
                buscaCodigoGrupoCotizacion('<%=datModif.getGrupocot() != null ? datModif.getGrupocot() : ""%>');
                buscaCodigoInscrita('<%=datModif.getInscrita() != null ? datModif.getInscrita() : ""%>');
            }
        }

        // FUNCIONES QUE PROCESAN RESPUESTA O ERROR DE LA PETICION AJAX
        function procesarRespuestaAltaModificacion(ajaxResult) {
            elementoVisible('off', 'barraProgresoLPEEL');
            var datos = JSON.parse(ajaxResult);
            var codigoOperacion = datos.tabla.codigoOperacion;
            if (codigoOperacion == "0") {
                var tipos1 = datos.tabla.lista;
                if (tipos1.length > 0) {
                    var respuesta = new Array();
                    respuesta[0] = "0";
                    respuesta[1] = tipos1;
                    self.parent.opener.retornoXanelaAuxiliar(respuesta);
                    cerrarVentana();
                } else {
                    mostrarErrorPeticion("5");
                }
            } else {
                mostrarErrorPeticion(codigoOperacion);
            }
        }

        function mostrarErrorAltaModificacion() {
            var codigo;
            if ('<%=nuevo%>' != null && '<%=nuevo%>' == 0) {
                codigo = "8";
            } else {
                codigo = "7";
            }
            mostrarErrorPeticion(codigo);
        }
        function comprobarFechaLanbide(inputFecha) {
            var formato = 'dd/mm/yyyy';
            if (Trim(inputFecha.value) != '') {
                var D = ValidarFechaConFormatoLanbide(inputFecha.value, formato);
                if (!D[0]) {
                    jsp_alerta("A", "<%=meLanbide81I18n.getMensaje(idiomaUsuario, "msg.fechaNoVal")%>");
                    document.getElementById(inputFecha.name).focus();
                    document.getElementById(inputFecha.name).select();
                    return false;
                } else {
                    inputFecha.value = D[1];
                    return true;
                }//if (!D[0])
            }//if (Trim(inputFecha.value)!='')
            return true;
        }//comprobarFechaLanbide

        //Funcion para el calendario de fecha
        function mostrarCalFecnacimiento(evento) {
            if (window.event)
                evento = window.event;
            if (document.getElementById("calfecnacimiento").src.indexOf("icono.gif") != -1)
                showCalendar('forms[0]', 'fecnacimiento', null, null, null, '', 'calfecnacimiento', '', null, null, null, null, null, null, null, null, evento);

        }
        function mostrarCalFechaInicioContrato(evento) {
            if (window.event)
                evento = window.event;
            if (document.getElementById("calfechaInicioContrato").src.indexOf("icono.gif") != -1)
                showCalendar('forms[0]', 'fechaInicioContrato', null, null, null, '', 'calfechaInicioContrato', '', null, null, null, null, null, null, null, null, evento);

        }
        function mostrarCalFechaFinContrato(evento) {
            if (window.event)
                evento = window.event;
            if (document.getElementById("calfechaFinContrato").src.indexOf("icono.gif") != -1)
                showCalendar('forms[0]', 'fechaFinContrato', null, null, null, '', 'calfechaFinContrato', '', null, null, null, null, null, null, null, null, evento);

        }

    </script>
</head>
<body class="bandaBody"  onload="javascript:{
                elementoVisible('off', 'barraProgresoLPEEL');
            }" >
<div class="contenidoPantalla">
    <input type="hidden" id="errorBD" name="errorBD" value="<%=meLanbide81I18n.getMensaje(idiomaUsuario,"error.errorBD")%>"/>
    <input type="hidden" id="generico" name="generico" value="<%=meLanbide81I18n.getMensaje(idiomaUsuario,"error.generico")%>"/>
    <input type="hidden" id="pasoParametros" name="pasoParametros" value="<%=meLanbide81I18n.getMensaje(idiomaUsuario,"error.pasoParametros")%>"/>
    <input type="hidden" id="listaVacia" name="listaVacia" value="<%=meLanbide81I18n.getMensaje(idiomaUsuario,"error.listaVacia")%>"/>
    <input type="hidden" id="obtenerDatos" name="obtenerDatos" value="<%=meLanbide81I18n.getMensaje(idiomaUsuario,"error.obtenerDatos")%>"/>
    <input type="hidden" id="insertarDatos" name="insertarDatos" value="<%=meLanbide81I18n.getMensaje(idiomaUsuario,"error.insertarDatos")%>"/>
    <input type="hidden" id="actualizarDatos" name="actualizarDatos" value="<%=meLanbide81I18n.getMensaje(idiomaUsuario,"error.actualizarDatos")%>"/>
    <input type="hidden" id="operacionIncorrecta" name="operacionIncorrecta" value="<%=meLanbide81I18n.getMensaje(idiomaUsuario,"error.operacionIncorrecta")%>"/>
    <div id="barraProgresoLPEEL" style="visibility: hidden;">
        <div class="contenedorHidepage">
            <div class="textoHide">
                        <span>
                            <%=meLanbide81I18n.getMensaje(idiomaUsuario, "msg.procesando")%>
                        </span>
            </div>
            <div class="imagenHide">
                <span id="disco" class="fa fa-spinner fa-spin" aria-hidden="true"/>
            </div>
        </div>
    </div>
    <form>
        <div style="width: 100%; padding: 10px; text-align: left;">

            <legend class="legendAzul"  id="titPuesto"><%=meLanbide81I18n.getMensaje(idiomaUsuario,"label.tipo1")%></legend>
            <br>
            <fieldset id="datosEntidades" name="datosEntidades" style="width: 95%;" >
                <legend class="legendAzul"><%=meLanbide81I18n.getMensaje(idiomaUsuario,"legend.datosEntidades")%></legend>
                <div class="lineaFormulario">
                    <div  class="etiquetaLPEEL">
                        <%=meLanbide81I18n.getMensaje(idiomaUsuario,"label.tipo.entidadLocalBeneficiaria")%>
                    </div>
                    <div style="width:450px; float: left;">
                        <input id="entidadLocalBeneficiaria" name="entidadLocalBeneficiaria"  type="text" class="inputTextoObligatorio" size="50" maxlength="100"
                               value="<%=datModif != null && datModif.getEntbene() != null ? datModif.getEntbene() : ""%>"/>
                    </div>
                </div>
                <div class="lineaFormulario">
                    <div  class="etiquetaLPEEL">
                        <%=meLanbide81I18n.getMensaje(idiomaUsuario,"label.tipo.entidadcontratante")%>
                    </div>
                    <div style="width:450px; float: left; ">
                        <input id="entidadcontratante" name="entidadcontratante" type="text" class="inputTextoObligatorio" size="50" maxlength="100"
                               value="<%=datModif != null && datModif.getEntcontra() != null ? datModif.getEntcontra() : ""%>"/>
                    </div>
                </div>
                <div class="lineaFormulario">
                    <div  class="etiquetaLPEEL">
                        <%=meLanbide81I18n.getMensaje(idiomaUsuario,"label.tipo.CIF")%>
                    </div>
                    <div style="width:450px; float: left; ">
                        <input id="cif" name="cif" type="text" class="inputTextoObligatorio" size="10" maxlength="9"
                               value="<%=datModif != null && datModif.getCif() != null ? datModif.getCif() : ""%>"/>
                    </div>
                </div>
                <div class="lineaFormulario">
                    <div  class="etiquetaLPEEL">
                        <%=meLanbide81I18n.getMensaje(idiomaUsuario,"label.tipo.CCC")%>
                    </div>
                    <div style="width:450px; float: left; ">
                        <input id="ccc" name="ccc" type="text" class="inputTextoObligatorio" size="20" maxlength="30"
                               value="<%=datModif != null && datModif.getCcc() != null ? datModif.getCcc() : ""%>"/>
                    </div>
                </div>
                <div class="lineaFormulario">
                    <div  class="etiquetaLPEEL">
                        <%=meLanbide81I18n.getMensaje(idiomaUsuario,"label.tipo.denominacionDelProyecto")%>
                    </div>
                    <div style="width:450px; float: left; ">
                                <textarea  id="denominacionDelProyecto" name="denominacionDelProyecto" type="text" class="inputTextoObligatorio" rows="4" cols="50" maxlength="200">
                                    <%=datModif != null && datModif.getDenomproy() != null ? datModif.getDenomproy() : ""%>
                                </textarea>
                    </div>
                </div>
                <div class="lineaFormulario">
                    <div  class="etiquetaLPEEL">
                        <%=meLanbide81I18n.getMensaje(idiomaUsuario,"label.tipo.denominacionDelPuestoDeTrabajo")%>
                    </div>
                    <div style="width:450px; float: left; ">
                        <input  id="denominacionDelPuestoDeTrabajo" name="denominacionDelPuestoDeTrabajo" type="text" class="inputTextoObligatorio" size="50" maxlength="50"
                                value="<%=datModif != null && datModif.getDenompuesto() != null ? datModif.getDenompuesto() : ""%>"/>
                    </div>
                </div>
                <br/>
                <fieldset id="datosPersona" name="datosPersona">
                    <legend class="legendAzul"><%=meLanbide81I18n.getMensaje(idiomaUsuario,"legend.datosPersona")%></legend>
                    <div class="lineaFormulario">
                        <div  class="etiquetaLPEEL">
                            <%=meLanbide81I18n.getMensaje(idiomaUsuario,"label.tipo.nombre")%>
                        </div>
                        <div style="width:450px; float: left; ">
                            <input id="nombre" name="nombre" type="text" class="inputTextoObligatorio" size="30" maxlength="80"
                                   value="<%=datModif != null && datModif.getNombre() != null ? datModif.getNombre() : ""%>"/>
                        </div>
                    </div>
                    <div class="lineaFormulario">
                        <div  class="etiquetaLPEEL">
                            <%=meLanbide81I18n.getMensaje(idiomaUsuario,"label.tipo.apellido1")%>
                        </div>
                        <div style="width:450px; float: left; ">
                            <input id="apellido1" name="apellido1" type="text" class="inputTextoObligatorio" size="30" maxlength="50"
                                   value="<%=datModif != null && datModif.getApellido1() != null ? datModif.getApellido1() : ""%>"/>
                        </div>
                    </div>
                    <div class="lineaFormulario">
                        <div  class="etiquetaLPEEL">
                            <%=meLanbide81I18n.getMensaje(idiomaUsuario,"label.tipo.apellido2")%>
                        </div>
                        <div style="width:450px; float: left; ">
                            <input id="apellido2" name="apellido2" type="text" class="inputTextoObligatorio" size="30" maxlength="50"
                                   value="<%=datModif != null && datModif.getApellido2() != null ? datModif.getApellido2() : ""%>"/>
                        </div>
                    </div>
                    <div class="lineaFormulario">
                        <div  class="etiquetaLPEEL">
                            <%=meLanbide81I18n.getMensaje(idiomaUsuario,"label.tipo.DNI")%>
                        </div>
                        <div style="width:450px; float: left; ">
                            <input id="dni" name="dni" type="text" class="inputTextoObligatorio" size="12" maxlength="9"
                                   value="<%=datModif != null && datModif.getDni() != null ? datModif.getDni() : ""%>"/>
                        </div>
                    </div>
                    <div class="lineaFormulario">
                        <div  class="etiquetaLPEEL">
                            <%=meLanbide81I18n.getMensaje(idiomaUsuario,"label.tipo.NAF")%>
                        </div>
                        <div style="width:450px; float: left; ">
                            <input id="naf" name="naf" type="text" class="inputTextoObligatorio" size="12" maxlength="12"
                                   value="<%=datModif != null && datModif.getNaf() != null ? datModif.getNaf() : ""%>"/>
                        </div>
                    </div>
                    <div class="lineaFormulario">
                        <div  class="etiquetaLPEEL">
                            <%=meLanbide81I18n.getMensaje(idiomaUsuario,"label.tipo.fecnacimiento")%>
                        </div>
                        <div>
                            <input type="text" id="fecnacimiento" class="inputTxtFechaObligatorio"
                                   value="<%=datModif != null && datModif.getFecnacimiento() != null ? datModif.getFecnacimientoStr() : ""%>" placeholder="dd/mm/aaaa"
                                   maxlength="10" name="fecnacimiento" size="10"
                                   onkeyup = "return SoloCaracteresFechaLanbide(this);"
                                   onfocus="javascript:this.select();"
                                   onblur = "javascript:return comprobarFechaLanbide(this);"/>
                            <A href="javascript:calClick(event);return false;" onClick="mostrarCalFecnacimiento(event);
                                            return false;" style="text-decoration:none;">
                                <IMG style="border: 0px solid" height="17" id="calfecnacimiento" name="calfecnacimiento" border="0" src="<c:url value='/images/calendario/icono.gif'/>" > <!--width="25"-->
                            </A>
                        </div>
                    </div>
                    <div class="lineaFormulario">
                        <div  class="etiquetaLPEEL">
                            <%=meLanbide81I18n.getMensaje(idiomaUsuario,"label.sexo")%>
                        </div>
                        <div style="width:450px; float: left;">
                            <input type="text" name="codListaSexo" id="codListaSexo" size="1" class="inputTextoObligatorio" value="" onkeyup="SoloDigitos(this);" maxlength="1"/>
                            <input type="text" name="descListaSexo"  id="descListaSexo" size="10" class="inputCombo" readonly value="" />
                            <a href="" id="anchorListaSexo" name="anchorListaSexo" title="<%=meLanbide81I18n.getMensaje(idiomaUsuario,"label.sexo")%>">
                                <span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonAplicacion" style="cursor:pointer;"></span>
                            </a>
                        </div>
                    </div>
                    <div class="lineaFormulario">
                        <div  class="etiquetaLPEEL">
                            <%=meLanbide81I18n.getMensaje(idiomaUsuario,"label.tipo.grupoDeCotizacion")%>
                        </div>
                        <div>
                            <input type="text" name="codListaGrupoCotizacion" id="codListaGrupoCotizacion" size="2" class="inputTextoObligatorio" value="" onkeyup="SoloDigitos(this);"  maxlength="2"/>
                            <input type="text" name="descListaGrupoCotizacion"  id="descListaGrupoCotizacion" size="60" class="inputCombo" readonly="true" value="" />
                            <a href="" id="anchorListaGrupoCotizacion" name="anchorListaGrupoCotizacion">
                                <span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonAplicacion" name="botonAplicacion" style="cursor:pointer;"></span>
                            </a>
                        </div>
                    </div>
                    <div class="lineaFormulario">
                        <div  class="etiquetaLPEEL">
                            <%=meLanbide81I18n.getMensaje(idiomaUsuario,"label.tipo.fechaInicioContrato")%>
                        </div>
                        <div>
                            <input type="text" id="fechaInicioContrato" class="inputTxtFechaObligatorio"
                                   value="<%=datModif != null && datModif.getFecinicio() != null ? datModif.getFecIniStr() : ""%>" placeholder="dd/mm/aaaa"
                                   maxlength="10" name="fechaInicioContrato" size="10"
                                   onkeyup = "return SoloCaracteresFechaLanbide(this);"
                                   onfocus="javascript:this.select();"
                                   onblur = "javascript:return comprobarFechaLanbide(this);"/>
                            <A href="javascript:calClick(event);return false;" onClick="mostrarCalFechaInicioContrato(event);
                                            return false;" style="text-decoration:none;">
                                <IMG style="border: 0px solid" height="17" id="calfechaInicioContrato" name="calfechaInicioContrato" border="0" src="<c:url value='/images/calendario/icono.gif'/>" > <!--width="25"-->
                            </A>
                        </div>
                    </div>

                    <div class="lineaFormulario">
                        <div  class="etiquetaLPEEL">
                            <%=meLanbide81I18n.getMensaje(idiomaUsuario,"label.tipo.fechaFinContrato")%>
                        </div>
                        <div>
                            <input type="text" id="fechaFinContrato" class="inputTxtFechaObligatorio"
                                   value="<%=datModif != null && datModif.getFecfin() != null ? datModif.getFecfinStr() : ""%>" placeholder="dd/mm/aaaa"
                                   maxlength="10" name="fechaFinContrato" size="10"
                                   onkeyup = "return SoloCaracteresFechaLanbide(this);"
                                   onfocus="javascript:this.select();"
                                   onblur = "javascript:return comprobarFechaLanbide(this);"/>
                            <A href="javascript:calClick(event);return false;" onClick="mostrarCalFechaFinContrato(event);return false;" style="text-decoration:none;">
                                <IMG style="border: 0px solid" height="17" id="calfechaFinContrato" name="calfechaFinContrato" border="0" src="<c:url value='/images/calendario/icono.gif'/>" > <!--width="25"-->
                            </A>
                        </div>
                    </div>
                    <div class="lineaFormulario">
                        <div  class="etiquetaLPEEL">
                            <%=meLanbide81I18n.getMensaje(idiomaUsuario,"label.porcJorn")%>
                        </div>
                        <div style="width:450px; float: left; ">
                            <input id="porcJorn" name="porcJorn" type="text" class="inputTextoObligatorio" size="5" maxlength="6" onchange="reemplazarPuntos(this);"
                                   value="<%=datModif != null && datModif.getPorcJorn() != null ? datModif.getPorcJorn().toString().replaceAll("\\.", ",") : ""%>" />
                        </div>
                    </div>

                    <div class="lineaFormulario">
                        <div  class="etiquetaLPEEL">
                            <%=meLanbide81I18n.getMensaje(idiomaUsuario,"label.tipo.duracionDelContrato")%>
                        </div>
                        <div style="width:450px; float: left; ">
                            <input id="duracionDelContrato" name="duracionDelContrato" type="text" class="inputTextoObligatorio" size="20" maxlength="100"
                                   value="<%=datModif != null && datModif.getDurcontrato() != null ? datModif.getDurcontrato() : ""%>" />
                        </div>
                    </div>
                    <div class="lineaFormulario">
                        <div  class="etiquetaLPEEL">
                            <%=meLanbide81I18n.getMensaje(idiomaUsuario,"label.tipo.edad")%>
                        </div>
                        <div style="width:450px; float: left; ">
                            <input id="edad" name="edad" type="text" class="inputTextoObligatorio" size="3" maxlength="2" onkeyup="SoloDigitos(this);"
                                   value="<%=datModif != null && datModif.getEdad() != null ? datModif.getEdad() : ""%>"/>
                        </div>
                    </div>
                    <div class="lineaFormulario">
                        <div  class="etiquetaLPEEL">
                            <%=meLanbide81I18n.getMensaje(idiomaUsuario,"label.tipo.municipioDeResidencia")%>
                        </div>
                        <div style="width:450px; float: left; ">
                            <input id="municipioDeResidencia" name="municipioDeResidencia" type="text" class="inputTextoObligatorio" size="30" maxlength="80"
                                   value="<%=datModif != null && datModif.getMunicipio() != null ? datModif.getMunicipio() : ""%>"/>
                        </div>
                    </div>
                    <div class="lineaFormulario">
                        <div  class="etiquetaLPEEL">
                            <%=meLanbide81I18n.getMensaje(idiomaUsuario,"label.tipo.costeSalarial")%>
                        </div>
                        <div style="width:450px; float: left; ">
                            <input id="costeSalarial" name="costeSalarial" type="text" class="inputTextoObligatorio" size="10" maxlength="9"onchange="reemplazarPuntos(this);"
                                   value="<%=datModif != null && datModif.getCostesal() != null ? datModif.getCostesal().toString().replaceAll("\\.",",") : ""%>"/>
                        </div>
                    </div>
                    <div class="lineaFormulario">
                        <div  class="etiquetaLPEEL">
                            <%=meLanbide81I18n.getMensaje(idiomaUsuario,"label.tipo.costeSeguridadSocial")%>
                        </div>
                        <div style="width:450px; float: left; ">
                            <input id="costeSeguridadSocial" name="costeSeguridadSocial" type="text" class="inputTextoObligatorio" size="10" maxlength="9"onchange="reemplazarPuntos(this);"
                                   value="<%=datModif != null && datModif.getCostess() != null ? datModif.getCostess().toString().replaceAll("\\.",",") : ""%>"/>
                        </div>
                    </div>
                    <div class="lineaFormulario">
                        <div  class="etiquetaLPEEL">
                            <%=meLanbide81I18n.getMensaje(idiomaUsuario,"label.tipo.costeTotal")%>
                        </div>
                        <div style="width:450px; float: left; ">
                            <input id="costeTotal" name="costeTotal" type="text" class="inputTextoObligatorio" size="10" maxlength="9"onchange="reemplazarPuntos(this);"
                                   value="<%=datModif != null && datModif.getCostetotal() != null ? datModif.getCostetotal().toString().replaceAll("\\.",",") : ""%>"/>
                        </div>
                    </div>
                </fieldset>
            </fieldset>
            <br/>
            <fieldset id="datosLanbide" name="datosLanbide">
                <legend class="legendAzul textoRojo"><%=meLanbide81I18n.getMensaje(idiomaUsuario,"legend.datosLanbide")%></legend>
                <div class="lineaFormulario">
                    <div  class="etiquetaLPEEL">
                        <%=meLanbide81I18n.getMensaje(idiomaUsuario,"label.tipo.inscritaDesempleada")%>
                    </div>
                    <div style="width:450px; float: left;">
                        <input type="text" name="codListaInscrita" id="codListaInscrita" size="1" class="inputCombo" value="" onkeyup="xAMayusculas(this);" maxlength="1"/>
                        <input type="text" name="descListaInscrita"  id="descListaInscrita" size="10" class="inputCombo" readonly value="" />
                        <a href="" id="anchorListaInscrita" name="anchorListaInscrita" title="<%=meLanbide81I18n.getMensaje(idiomaUsuario,"label.tipo.inscritaDesempleada")%>">
                            <span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonAplicacion" style="cursor:pointer;"></span>
                        </a>
                    </div>
                </div>
                <div class="lineaFormulario">
                    <div  class="etiquetaLPEEL">
                        <%=meLanbide81I18n.getMensaje(idiomaUsuario,"label.tipo.certInterventor")%>
                    </div>
                    <div style="width:450px; float: left; ">
                        <input id="certInterventor" name="certInterventor" type="text" size="30" class="inputTexto"maxlength="50"
                               value="<%=datModif != null && datModif.getCertinter() != null ? datModif.getCertinter() : ""%>"/>
                    </div>
                </div>
                <div class="lineaFormulario">
                    <div  class="etiquetaLPEEL">
                        <%=meLanbide81I18n.getMensaje(idiomaUsuario,"label.tipo.subvencionConcedida")%>
                    </div>
                    <div style="width:450px; float: left; ">
                        <input id="subvencionConcedida" name="subvencionConcedida" type="text" size="10" class="inputTexto" maxlength="9"
                               value="<%=datModif != null && datModif.getSubconcedida() != null ? datModif.getSubconcedida().toString().replaceAll("\\.",",") : ""%>"/>
                    </div>
                </div>
                <div class="lineaFormulario">
                    <div  class="etiquetaLPEEL">
                        <%=meLanbide81I18n.getMensaje(idiomaUsuario,"label.tipo.priPago")%>
                    </div>
                    <div style="width:450px; float: left; ">
                        <input id="priPago" name="priPago" type="text" size="10" class="inputTexto" maxlength="9"
                               value="<%=datModif != null && datModif.getPago1() != null ? datModif.getPago1().toString().replaceAll("\\.",",") : ""%>"/>
                    </div>
                </div>
                <div class="lineaFormulario">
                    <div  class="etiquetaLPEEL">
                        <%=meLanbide81I18n.getMensaje(idiomaUsuario,"label.tipo.subLiquidada")%>
                    </div>
                    <div style="width:450px; float: left; ">
                        <input id="subLiquidada" name="subLiquidada" type="text" size="10" class="inputTexto" maxlength="9"
                               value="<%=datModif != null && datModif.getSubliquidada() != null ? datModif.getSubliquidada().toString().replaceAll("\\.",",") : ""%>"/>
                    </div>
                </div>
                <div class="lineaFormulario">
                    <div  class="etiquetaLPEEL">
                        <%=meLanbide81I18n.getMensaje(idiomaUsuario,"label.tipo.segunPago")%>
                    </div>
                    <div style="width:450px; float: left; ">
                        <input id="segunPago" name="segunPago" type="text" size="10" class="inputTexto" maxlength="9"
                               value="<%=datModif != null && datModif.getPago2() != null ? datModif.getPago2().toString().replaceAll("\\.",",") : ""%>"/>
                    </div>
                </div>
                <div class="lineaFormulario">
                    <div  class="etiquetaLPEEL" >
                        <%=meLanbide81I18n.getMensaje(idiomaUsuario,"label.tipo.observaciones")%>
                    </div>
                    <div style="float:left; ">
                                <textarea style="width: 100%" name="observaciones " id="observaciones" rows="5"  cols="50" maxlength="400" class="melanbide81_txtSinMayusculas">
                                    <%=datModif != null && datModif.getObservaciones() != null ? datModif.getObservaciones() : ""%>
                                </textarea>
                    </div>
                </div>
            </fieldset>

            <div class="lineaFormulario">
                <div class="botonera" style="width: 100%; float: left; text-align: center;">
                    <input type="button" id="btnAceptarTipo1" name="btnAceptarTipo1" class="botonGeneral" value="<%=meLanbide81I18n.getMensaje(idiomaUsuario, "btn.aceptar")%>" onclick="guardarTipo1();"/>
                    <input type="button" id="btnCancelarTipo1" name="btnCancelarTipo1" class="botonGeneral" value="<%=meLanbide81I18n.getMensaje(idiomaUsuario, "btn.cancelar")%>" onclick="cancelarTipo1();"/>
                </div>
            </div>
        </div>
    </form>
    <div id="popupcalendar" class="text"></div>
    <script type="text/javascript">
        /*  SEXO */
        listaCodigosSexo[0] = "";
        listaDescripcionesSexo[0] = "";
        contador = 0;
        <logic:iterate id="Sexo" name="listaSexo" scope="request">
        listaCodigosSexo[contador] = ['<bean:write name="Sexo" property="des_val_cod" />'];
        listaDescripcionesSexo[contador] = ['<bean:write name="Sexo" property="des_nom" />'];
        contador++;
        </logic:iterate>
        var comboListaSexo = new Combo("ListaSexo");
        comboListaSexo.addItems(listaCodigosSexo, listaDescripcionesSexo);
        comboListaSexo.change = cargarDatosSexo;

        /*desplegable grupoCotizacion*/
        listaCodigosGrupoCotizacion[0] = "";
        listaDescripcionesGrupoCotizacion[0] = "";
        contador = 0;
        <logic:iterate id="GrupoCotizacion" name="listaGrupoCotizacion" scope="request">
        listaCodigosGrupoCotizacion[contador] = ['<bean:write name="GrupoCotizacion" property="des_val_cod" />'];
        listaDescripcionesGrupoCotizacion[contador] = ['<bean:write name="GrupoCotizacion" property="des_nom" />'];
        contador++;
        </logic:iterate>
        var comboListaGrupoCotizacion = new Combo("ListaGrupoCotizacion");
        comboListaGrupoCotizacion.addItems(listaCodigosGrupoCotizacion, listaDescripcionesGrupoCotizacion);
        comboListaGrupoCotizacion.change = cargarDatosGrupoCotizacion;


        /* desplegable si/no */
        listaCodigosInscrita[0] = "";
        listaDescripcionesInscrita[0] = "";
        contador = 0;
        <logic:iterate id="Inscrita" name="listaInscrita" scope="request">
        listaCodigosInscrita[contador] = ['<bean:write name="Inscrita" property="des_val_cod" />'];
        listaDescripcionesInscrita[contador] = ['<bean:write name="Inscrita" property="des_nom" />'];
        contador++;
        </logic:iterate>
        var comboListaInscrita = new Combo("ListaInscrita");
        comboListaInscrita.addItems(listaCodigosInscrita, listaDescripcionesInscrita);
        comboListaInscrita.change = cargarDatosInscrita;


        if ('<%=nuevo%>' != null && '<%=nuevo%>' == 0) {
            rellenarDesplegables();
        }

    </script>
</div>
</body>
</html>
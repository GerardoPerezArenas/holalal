<%@taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide86.i18n.MeLanbide86I18n" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide86.vo.FilaContratacionVO" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide86.vo.DesplegableAdmonLocalVO" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide86.util.ConstantesMeLanbide86" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide86.util.ConfigurationParameter" %>
<%@page import="java.util.ArrayList" %>
<%@page import="java.util.List" %>
<%@page import="java.text.SimpleDateFormat" %>
<html>
    <head>
        <meta http-equiv="X-UA-Compatible" content="IE=Edge" />
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <%
            FilaContratacionVO datModif = new FilaContratacionVO();
      
            String codOrganizacion = "";
            String nuevo = "";        
            String numExpediente = "";
            String fechaNacimiento2 = "";
            String fechaInicio2 = "";
            String fechaInicio3 = "";
            String fechaFin3 = "";            
        
            MeLanbide86I18n meLanbide86I18n = MeLanbide86I18n.getInstance();

            numExpediente = (String)request.getAttribute("numExp");
            int idiomaUsuario = 1;
            int apl = 5;
            String css = "";
            try
            {
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

                codOrganizacion  = request.getParameter("codOrganizacionModulo");
                nuevo = (String)request.getAttribute("nuevo");
                if(request.getAttribute("datModif") != null)
                {
                    datModif = (FilaContratacionVO)request.getAttribute("datModif");     
                    SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
                    fechaNacimiento2 = formatoFecha.format(datModif.getFechaNacimiento2());
                    fechaInicio2 = formatoFecha.format(datModif.getFechaInicio2());
                    fechaInicio3 = formatoFecha.format(datModif.getFechaInicio3());
                    fechaFin3 = formatoFecha.format(datModif.getFechaFin3());
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
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/>
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/extension/melanbide86/melanbide86.css"/>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/JavaScriptUtil.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script>        
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/general.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/melanbide86/IkerUtils.js"></script>
        <script type="text/javascript" src="<c:url value='/scripts/listaComboBox.js'/>"></script>
        <script type="text/javascript" src="<c:url value='/scripts/calendario.js'/>"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/validaciones.js"></script>
        <script type="text/javascript">
            var APP_CONTEXT_PATH = '<%=request.getContextPath()%>';
            var url = APP_CONTEXT_PATH + '/PeticionModuloIntegracion.do';
            var mensajeValidacion = '';

//Desplegable  ACT 1
            var comboListaAct1;
            var listaCodigosAct1 = new Array();
            var listaDescripcionesAct1 = new Array();
            function buscaCodigoAct1(tipo) {
                comboListaAct1.buscaCodigo(tipo);
            }
            function cargarDatosAct1() {
                var tipoSeleccionado = document.getElementById("codListaAct1").value;
                buscaCodigoAct1(tipoSeleccionado);
            }
//Desplegable  GrupoCotiz1
            var comboListaGrupo1;
            var listaCodigosGrupo1 = new Array();
            var listaDescripcionesGrupo1 = new Array();
            function buscaCodigoGrupo1(tipo) {
                comboListaGrupo1.buscaCodigo(tipo);
            }
            function cargarDatosGrupo1() {
                var tipoSeleccionado = document.getElementById("codListaGrupo1").value;
                buscaCodigoGrupo1(tipoSeleccionado);
            }
//Desplegable  Sexo2
            var comboListaSexo2;
            var listaCodigosSexo2 = new Array();
            var listaDescripcionesSexo2 = new Array();
            function buscaCodigoSexo2(tipo) {
                comboListaSexo2.buscaCodigo(tipo);
            }
            function cargarDatosSexo2() {
                var tipoSeleccionado = document.getElementById("codListaSexo2").value;
                buscaCodigoSexo2(tipoSeleccionado);
            }
//Desplegable  ActDes2
            var comboListaAct2;
            var listaCodigosAct2 = new Array();
            var listaDescripcionesAct2 = new Array();
            function buscaCodigoAct2(tipo) {
                comboListaAct2.buscaCodigo(tipo);
            }
            function cargarDatosAct2() {
                var tipoSeleccionado = document.getElementById("codListaAct2").value;
                buscaCodigoAct2(tipoSeleccionado);
            }
//Desplegable  GrupoCotiz2
            var comboListaGrupo2;
            var listaCodigosGrupo2 = new Array();
            var listaDescripcionesGrupo2 = new Array();
            function buscaCodigoGrupo2(tipo) {
                comboListaGrupo2.buscaCodigo(tipo);
            }
            function cargarDatosGrupo2() {
                var tipoSeleccionado = document.getElementById("codListaGrupo2").value;
                buscaCodigoGrupo2(tipoSeleccionado);
            }

            //Funcion para el calendario de fecha 
            function mostrarCalFechaNacimiento2(evento) {
                if (window.event)
                    evento = window.event;
                if (document.getElementById("calFechaNacimiento2").src.indexOf("icono.gif") != -1)
                    showCalendar('forms[0]', 'fechaNacimiento2', null, null, null, '', 'calFechaNacimiento2', '', null, null, null, null, null, null, null, 'calcularEdad()', evento);

            }

            function mostrarCalFechaInicio2(evento) {
                if (window.event)
                    evento = window.event;
                if (document.getElementById("calFechaInicio2").src.indexOf("icono.gif") != -1)
                    showCalendar('forms[0]', 'fechaInicio2', null, null, null, '', 'calFechaInicio2', '', null, null, null, null, null, null, null, 'calcularEdad()', evento);
            }

            function mostrarCalFechaInicio3(evento) {
                if (window.event)
                    evento = window.event;
                if (document.getElementById("calFechaInicio3").src.indexOf("icono.gif") != -1)
                    showCalendar('forms[0]', 'fechaInicio3', null, null, null, '', 'calFechaInicio3', '', null, null, null, null, null, null, null, null, evento);
            }

            function mostrarCalFechaFin3(evento) {
                if (window.event)
                    evento = window.event;
                if (document.getElementById("calFechaFin3").src.indexOf("icono.gif") != -1)
                    showCalendar('forms[0]', 'fechaFin3', null, null, null, '', 'calFechaFin3', '', null, null, null, null, null, null, null, null, evento);
            }

            function guardarDatos() {
                if (validarDatos()) {
                    elementoVisible('on', 'barraProgresoIKER');
                    var parametros = "";

                    if ('<%=nuevo%>' != null && '<%=nuevo%>' == "1") {
                        parametros = "tarea=preparar&modulo=MELANBIDE86&operacion=crearNuevaContratacion&tipo=0";
                    } else {
                        parametros = "tarea=preparar&modulo=MELANBIDE86&operacion=modificarContratacion&tipo=0"
                                + "&id=<%=datModif != null && datModif.getId() != null ? datModif.getId().toString() : ""%>";
                    }
                    parametros += '&numExpediente=<%=numExpediente%>'
                            + "&numPuesto=" + document.getElementById('numPuesto').value
                            + "&denomPuesto=" + document.getElementById('denomPuesto').value.replace(/\r?\n|\r/g, " ").trim()
                            + "&actDes1=" + document.getElementById('codListaAct1').value
                            + "&titulacion1=" + document.getElementById('titulacion1').value
                            + "&tipoCont1=" + document.getElementById('tipoCont1').value
                            + "&durContrato1=" + document.getElementById('durContrato1').value
                            + "&grupoCotiz1=" + document.getElementById('codListaGrupo1').value
                            + "&costeSalarial1=" + document.getElementById('costeSalarial1').value
                            + "&subvSolicitada1=" + document.getElementById('subvSolicitada1').value
                            + "&cainVinn1=" + document.getElementById('cainVinn1').value
                            // CONTRATACION_INI
                            + "&nOferta2=" + (document.getElementById('nOferta2').value != null && document.getElementById('nOferta2').value != "" ? document.getElementById('nOferta2').value : "")
                            + "&nombre2=" + (document.getElementById('nombre2').value != null && document.getElementById('nombre2').value != "" ? document.getElementById('nombre2').value : "")
                            + "&apellido12=" + (document.getElementById('apellido12').value != null && document.getElementById('apellido12').value != "" ? document.getElementById('apellido12').value : "")
                            + "&apellido22=" + (document.getElementById('apellido22').value != null && document.getElementById('apellido22').value != "" ? document.getElementById('apellido22').value : "")
                            + "&dniNie2=" + (document.getElementById('dniNie2').value != null && document.getElementById('dniNie2').value != "" ? document.getElementById('dniNie2').value : "")
                            + "&sexo2=" + (document.getElementById('codListaSexo2').value != null && document.getElementById('codListaSexo2').value != "" ? document.getElementById('codListaSexo2').value : "")
                            + "&actDes2=" + (document.getElementById('codListaAct2').value != null && document.getElementById('codListaAct2').value != "" ? document.getElementById('codListaAct2').value : "")
                            + "&titulacion2=" + (document.getElementById('titulacion2').value != null && document.getElementById('titulacion2').value != "" ? document.getElementById('titulacion2').value : "")
                            + "&tipoCont2=" + (document.getElementById('tipoCont2').value != null && document.getElementById('tipoCont2').value != "" ? document.getElementById('tipoCont2').value : "")
                            + "&durContrato2=" + (document.getElementById('durContrato2').value != null && document.getElementById('durContrato2').value != "" ? document.getElementById('durContrato2').value : "")
                            + "&grupoCotiz2=" + (document.getElementById('codListaGrupo2').value != null && document.getElementById('codListaGrupo2').value != "" ? document.getElementById('codListaGrupo2').value : "")
                            + "&fechaNacimiento2=" + (document.getElementById('fechaNacimiento2').value != null && document.getElementById('fechaNacimiento2').value != "" ? document.getElementById('fechaNacimiento2').value : "")
                            + "&fechaInicio2=" + (document.getElementById('fechaInicio2').value != null && document.getElementById('fechaInicio2').value != "" ? document.getElementById('fechaInicio2').value : "")
                            + "&edad2=" + (document.getElementById('edad2').value != null && document.getElementById('edad2').value != "" ? document.getElementById('edad2').value : "")
                            + "&retribucionBruta2=" + (document.getElementById('retribucionBruta2').value != null && document.getElementById('retribucionBruta2').value != "" ? document.getElementById('retribucionBruta2').value : "")
                            + "&cainVinn2=" + (document.getElementById('cainVinn2').value != null && document.getElementById('cainVinn2').value != "" ? document.getElementById('cainVinn2').value : "")
                            // CONTRATACION_FIN
                            + "&nombre3=" + (document.getElementById('nombre3').value != null && document.getElementById('nombre3').value != "" ? document.getElementById('nombre3').value : "")
                            + "&apellido13=" + (document.getElementById('apellido13').value != null && document.getElementById('apellido13').value != "" ? document.getElementById('apellido13').value : "")
                            + "&apellido23=" + (document.getElementById('apellido23').value != null && document.getElementById('apellido23').value != "" ? document.getElementById('apellido23').value : "")
                            + "&dniNie3=" + (document.getElementById('dniNie3').value != null && document.getElementById('dniNie3').value != "" ? document.getElementById('dniNie3').value : "")
                            + "&durContrato3=" + (document.getElementById('durContrato3').value != null && document.getElementById('durContrato3').value != "" ? document.getElementById('durContrato3').value : "")
                            + "&fechaInicio3=" + (document.getElementById('fechaInicio3').value != null && document.getElementById('fechaInicio3').value != "" ? document.getElementById('fechaInicio3').value : "")
                            + "&fechaFin3=" + (document.getElementById('fechaFin3').value != null && document.getElementById('fechaFin3').value != "" ? document.getElementById('fechaFin3').value : "")
                            + "&costeSalarial3=" + (document.getElementById('costeSalarial3').value != null && document.getElementById('costeSalarial3').value != "" ? document.getElementById('costeSalarial3').value : "")
                            + "&costesSS3=" + (document.getElementById('costesSS3').value != null && document.getElementById('costesSS3').value != "" ? document.getElementById('costesSS3').value : "")
                            + "&costeTotalReal=" + (document.getElementById('costeTotalReal').value != null && document.getElementById('costeTotalReal').value != "" ? document.getElementById('costeTotalReal').value : "")
                            + "&subvConcedidaLan3=" + (document.getElementById('subvConcedidaLan3').value != null && document.getElementById('subvConcedidaLan3').value != "" ? document.getElementById('subvConcedidaLan3').value : "")
                            ;

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
                        elementoVisible('off', 'barraProgresoIKER');
                        mostrarError();
                    }
                } else {
                    jsp_alerta("A", mensajeValidacion);
                }
            }

            function validarDatos() {
                mensajeValidacion = "";
                // Obligatorios
                var campo = document.getElementById('numPuesto');
                if (campo.value == null || campo.value == '') {
                    mensajeValidacion = '<%=meLanbide86I18n.getMensaje(idiomaUsuario, "msg.numPuesto")%>';
                    return false;
                } else if (/^([0-9])*$/.test(campo)) {
                    mensajeValidacion = '<%=meLanbide86I18n.getMensaje(idiomaUsuario, "msg.numPuesto.format")%>';
                    return false;
                }
                campo = document.getElementById('denomPuesto').value;
                if (campo == null || campo == '') {
                    mensajeValidacion = '<%=meLanbide86I18n.getMensaje(idiomaUsuario, "msg.denomPuesto")%>';
                    return false;
                }
                campo = document.getElementById('codListaAct1').value;
                if (campo == null || campo == '') {
                    mensajeValidacion = '<%=meLanbide86I18n.getMensaje(idiomaUsuario, "msg.act1")%>';
                    return false;
                }
                campo = document.getElementById('titulacion1').value;
                if (campo == null || campo == '') {
                    mensajeValidacion = '<%=meLanbide86I18n.getMensaje(idiomaUsuario, "msg.titulacion1")%>';
                    return false;
                }
                campo = document.getElementById('tipoCont1').value;
                if (campo == null || campo == '') {
                    mensajeValidacion = '<%=meLanbide86I18n.getMensaje(idiomaUsuario, "msg.tipoCont1")%>';
                    return false;
                }
                campo = document.getElementById('durContrato1');
                if (campo.value == null || campo.value == '') {
                    mensajeValidacion = '<%=meLanbide86I18n.getMensaje(idiomaUsuario, "msg.durContrato1")%>';
                    return false;
                } else if (/^([0-9])*$/.test(campo)) {
                    mensajeValidacion = '<%=meLanbide86I18n.getMensaje(idiomaUsuario, "msg.durContrato1.format")%>';
                    return false;
                }
                campo = document.getElementById('codListaGrupo1').value;
                if (campo == null || campo == '') {
                    mensajeValidacion = '<%=meLanbide86I18n.getMensaje(idiomaUsuario, "msg.grupoCotiz1")%>';
                    return false;
                }
                campo = document.getElementById('costeSalarial1').value;
                if (campo == null || campo == '') {
                    mensajeValidacion = '<%=meLanbide86I18n.getMensaje(idiomaUsuario, "msg.costeSalarial1")%>';
                    return false;
                } else if (!validarNumericoDecimalPrecision(campo, 9, 2)) {
                    mensajeValidacion = '<%=meLanbide86I18n.getMensaje(idiomaUsuario, "msg.costeSalarial1.errNumerico")%>';
                    return false;
                }
                campo = document.getElementById('subvSolicitada1').value;
                if (campo == null || campo == '') {
                    mensajeValidacion = '<%=meLanbide86I18n.getMensaje(idiomaUsuario, "msg.subvSolicitada1")%>';
                    return false;
                } else if (!validarNumericoDecimalPrecision(campo, 9, 2)) {
                    mensajeValidacion = '<%=meLanbide86I18n.getMensaje(idiomaUsuario, "msg.subvSolicitada1.errNumerico")%>';
                    return false;
                }
                campo = document.getElementById('cainVinn1').value;
                if (campo == null || campo == '') {
                    mensajeValidacion = '<%=meLanbide86I18n.getMensaje(idiomaUsuario, "msg.cainVinn1")%>';
                    return false;
                }
                // no obligatorios
                campo = document.getElementById('dniNie2');
                if (campo.value != null && campo.value != '') {
                    if (!validarDniNie(campo)) {
                        mensajeValidacion = '<%=meLanbide86I18n.getMensaje(idiomaUsuario, "msg.documentoIncorrecto")%>';
                        return false;
                    }
                }
                campo = document.getElementById('durContrato2');
                if (campo.value != null && campo.value != '') {
                    if (/^([0-9])*$/.test(campo)) {
                        mensajeValidacion = '<%=meLanbide86I18n.getMensaje(idiomaUsuario, "msg.durContrato2.format")%>';
                        return false;
                    }
                }
                campo = document.getElementById('edad2');
                if (campo.value != null && campo.value != '') {
                    if (/^([0-9])*$/.test(campo)) {
                        mensajeValidacion = '<%=meLanbide86I18n.getMensaje(idiomaUsuario, "msg.edad2.format")%>';
                        return false;
                    }
                }
                campo = document.getElementById('retribucionBruta2').value;
                if (campo != null && campo != '') {
                    if (!validarNumericoDecimalPrecision(campo, 9, 2)) {
                        mensajeValidacion = '<%=meLanbide86I18n.getMensaje(idiomaUsuario, "msg.retribucionBruta2.errNumerico")%>';
                        return false;
                    }
                }
                campo = document.getElementById('dniNie3');
                if (campo.value != null && campo.value != '') {
                    if (!validarDniNie(campo)) {
                        mensajeValidacion = '<%=meLanbide86I18n.getMensaje(idiomaUsuario, "msg.documentoIncorrecto")%>';
                        return false;
                    }
                }
                campo = document.getElementById('durContrato3');
                if (campo.value != null && campo.value != '') {
                    if (/^([0-9])*$/.test(campo)) {
                        mensajeValidacion = '<%=meLanbide86I18n.getMensaje(idiomaUsuario, "msg.durContrato3.format")%>';
                        return false;
                    }
                }
                campo = document.getElementById('costeSalarial3').value;
                if (campo != null && campo != '') {
                    if (!validarNumericoDecimalPrecision(campo, 9, 2)) {
                        mensajeValidacion = '<%=meLanbide86I18n.getMensaje(idiomaUsuario, "msg.costeSalarial3.errNumerico")%>';
                        return false;
                    }
                }
                campo = document.getElementById('costesSS3').value;
                if (campo != null && campo != '') {
                    if (!validarNumericoDecimalPrecision(campo, 9, 2)) {
                        mensajeValidacion = '<%=meLanbide86I18n.getMensaje(idiomaUsuario, "msg.costesSS3.errNumerico")%>';
                        return false;
                    }
                }
                campo = document.getElementById('costeTotalReal').value;
                if (campo != null && campo != '') {
                    if (!validarNumericoDecimalPrecision(campo, 9, 2)) {
                        mensajeValidacion = '<%=meLanbide86I18n.getMensaje(idiomaUsuario, "msg.costeTotalReal.errNumerico")%>';
                        return false;
                    }
                }
                campo = document.getElementById('subvConcedidaLan3').value;
                if (campo != null && campo != '') {
                    if (!validarNumericoDecimalPrecision(campo, 9, 2)) {
                        mensajeValidacion = '<%=meLanbide86I18n.getMensaje(idiomaUsuario, "msg.subvConcedidaLan3.errNumerico")%>';
                        return false;
                    }
                }
                return true;
            }

            function cancelar() {
                var resultado = jsp_alerta('', '<%=meLanbide86I18n.getMensaje(idiomaUsuario, "msg.preguntaCancelar")%>');
                if (resultado == 1) {
                    cerrarVentana();
                }
            }

            function rellenarDesplegables() {
                if ('<%=datModif%>' != null) {
                    buscaCodigoAct1('<%=datModif.getActDes1() != null ? datModif.getActDes1() : ""%>');
                    buscaCodigoGrupo1('<%=datModif.getGrupoCotiz1() != null ? datModif.getGrupoCotiz1() : ""%>');
                    buscaCodigoSexo2('<%=datModif.getSexo2() != null ? datModif.getSexo2() : ""%>');
                    buscaCodigoAct2('<%=datModif.getActDes2() != null ? datModif.getActDes2() : ""%>');
                    buscaCodigoGrupo2('<%=datModif.getGrupoCotiz2() != null ? datModif.getGrupoCotiz2() : ""%>');
                } else
                    alert('No hemos podido cargar los datos para modificar');
            }

            function desbloquearCampos() {
                document.getElementById('numPuesto').disabled = false;
                document.getElementById('denomPuesto').disabled = false;
            }

            // FUNCIONES QUE PROCESAN RESPUESTA O ERROR DE LA PETICION AJAX
            function procesarRespuestaAltaModificacion(ajaxResult) {
                elementoVisible('off', 'barraProgresoIKER');
                var datos = JSON.parse(ajaxResult);
                var codigoOperacion = datos.tabla.codigoOperacion;
                if (codigoOperacion == "0") {
                    var contrataciones = datos.tabla.lista;
                    if (contrataciones.length > 0) {
                        var respuesta = new Array();
                        respuesta[0] = "0";
                        respuesta[1] = contrataciones;
                        self.parent.opener.retornoXanelaAuxiliar(respuesta);
                        cerrarVentana();
                    } else {
                        mostrarError(5);
                    }
                } else {
                    mostrarError(codigoOperacion);
                }
            }

            function mostrarErrorAltaModificacion() {
                var codigo;
                if ('<%=nuevo%>' != null && '<%=nuevo%>' == 0) {
                    codigo = "8";
                } else {
                    codigo = "7";
                }
                mostrarError(codigo);
            }

            function mostrarError(codigo) {
                elementoVisible('off', 'barraProgresoIKER');
                if (codigo == "1") {
                    jsp_alerta("A", '<%=meLanbide86I18n.getMensaje(idiomaUsuario,"error.errorBD")%>');
                } else if (codigo == "2") {
                    jsp_alerta("A", '<%=meLanbide86I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                } else if (codigo == "3") {
                    jsp_alerta("A", '<%=meLanbide86I18n.getMensaje(idiomaUsuario,"error.pasoParametros")%>');
                } else if (codigo == "4") {
                    jsp_alerta("A", '<%=meLanbide86I18n.getMensaje(idiomaUsuario,"error.listaVacia")%>');
                } else if (codigo == "5") {
                    jsp_alerta("A", '<%=meLanbide86I18n.getMensaje(idiomaUsuario,"error.errorObtenerDatos")%>');
                } else if (codigo == "6") {
                    jsp_alerta("A", '<%=meLanbide86I18n.getMensaje(idiomaUsuario,"error.errorBorrarDatos")%>');
                } else if (codigo == "7") {
                    jsp_alerta("A", '<%=meLanbide86I18n.getMensaje(idiomaUsuario,"error.errorInsertarDatos")%>');
                } else if (codigo == "8") {
                    jsp_alerta("A", '<%=meLanbide86I18n.getMensaje(idiomaUsuario,"error.errorActualizarDatos")%>');
                } else if (codigo == "-1") {
                    jsp_alerta("A", '<%=meLanbide86I18n.getMensaje(idiomaUsuario,"error.operacionIncorrecta")%>');
                } else {
                    jsp_alerta("A", '<%=meLanbide86I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                }
            }

            function comprobarFecha(inputFecha) {
                var formato = 'dd/mm/yyyy';
                if (Trim(inputFecha.value) != '') {
                    var D = ValidarFechaConFormatoIker(inputFecha.value, formato);
                    if (!D[0]) {
                        jsp_alerta("A", "<%=meLanbide86I18n.getMensaje(idiomaUsuario, "msg.fechaNoVal")%>");
                        document.getElementById(inputFecha.name).focus();
                        document.getElementById(inputFecha.name).select();
                        return false;
                    } else {
                        inputFecha.value = D[1];
                        return true;
                    }//if (!D[0])
                }//if (Trim(inputFecha.value)!='')
                return true;
            }//comprobarFecha

            function comprobarFechaNac(inputFecha) {
                var formato = 'dd/mm/yyyy';
                if (Trim(inputFecha.value) != '') {
                    var D = ValidarFechaConFormatoIker(inputFecha.value, formato);
                    if (!D[0]) {
                        jsp_alerta("A", "<%=meLanbide86I18n.getMensaje(idiomaUsuario, "msg.fechaNoVal")%>");
                        document.getElementById(inputFecha.name).focus();
                        document.getElementById(inputFecha.name).select();
                        return false;
                    } else {
                        //La fecha de nacimiento no puede ser mayor que la actual
                        var array_fecha = inputFecha.value.split("/");
                        if (array_fecha.length == 3)
                        {
                            var dia = array_fecha[0];
                            var mes = array_fecha[1];
                            var ano = array_fecha[2];
                            var today = new Date();
                            var d = new Date(ano, mes - 1, dia, 0, 0, 0, 0);
                            var n1 = today.getTime();
                            var n2 = d.getTime();
                            var result = n1 - n2;
                            if (result < 0) {
                                jsp_alerta("A", "<%=meLanbide86I18n.getMensaje(idiomaUsuario, "msg.fechaNacPosterior")%>");
                                return false;
                            }
                        }
                    }//if (!D[0])
                }//if (Trim(inputFecha.value)!='')
                return true;
            }//comprobarFechaNac

            function calcularEdad() {
                if (comprobarFechaNac(document.getElementById('fechaNacimiento2')) && comprobarFecha(document.getElementById('fechaInicio2'))) {
                    var txtOutput = 'edad2';
                    var fechaNac = document.getElementById('fechaNacimiento2').value;
                    var fechaIni = document.getElementById('fechaInicio2').value;
                    //  if (comprobarFecha(document.getElementById('fechaInConAd')))
                    calcularAnyos(fechaNac, txtOutput, fechaIni);
                } else {
                    document.getElementById('edad2').value = '';
                }
            }

            function calcularAnyos(feNac, txtOutput, feIni) {
                try {
                    if (feNac != "" && feIni != "") {
                        var dat1 = feNac.split('/');
                        var dat2 = feIni.split('/');
                        var anyos = parseInt(dat2[2]) - parseInt(dat1[2]);
                        var mes = parseInt(dat2[1], 10) - parseInt(dat1[1], 10);//forzar' la base a 10
                        if (mes < 0) {
                            anyos = anyos - 1;
                        } else if (mes == 0) {
                            var dia = parseInt(dat2[0], 10) - parseInt(dat1[0], 10);
                            if (dia < 0) {
                                anyos = anyos - 1;
                            }
                        }
                        document.getElementById(txtOutput).value = anyos;
                    }
                } catch (err) {

                }
            }
        </script>
    </head>
    <body class="bandaBody" onload="javascript:{
                elementoVisible('off', 'barraProgresoIKER');
            }" >
        <div class="contenidoPantalla">
            <div id="barraProgresoIKER" style="visibility: hidden;">
                <div class="contenedorHidepage">
                    <div class="textoHide">
                        <span>
                            <%=meLanbide86I18n.getMensaje(idiomaUsuario, "msg.procesando")%>
                        </span>
                    </div>
                    <div class="imagenHide">
                        <span id="disco" class="fa fa-spinner fa-spin" aria-hidden="true"/>
                    </div>
                </div>
            </div>
            <form>
                <div style="width: 100%; padding: 10px; text-align: left;">
                    <div  class="sub3titulo" style="clear: both; text-align: center; font-size: 14px;">
                        <span id="tituloVentana" style="width: 98%;">      
                            <%=nuevo != null && nuevo=="1" ? meLanbide86I18n.getMensaje(idiomaUsuario,"label.nuevaContratacion"):meLanbide86I18n.getMensaje(idiomaUsuario,"label.modifContratacion")%>
                        </span>
                    </div>
                    <fieldset id="puesto" name="puesto" style="width: 80%;">
                        <legend class="legendAzul" id="titPuesto"><%=meLanbide86I18n.getMensaje(idiomaUsuario,"label.puesto")%></legend>
                        <div class="lineaFormulario">
                            <div class="etiquetaIker" style="width: 60px;">
                                <%=meLanbide86I18n.getMensaje(idiomaUsuario,"label.numPuesto")%>
                            </div>
                            <div style="width: 80px; float: left;">
                                <input id="numPuesto" name="numPuesto" type="text" class="inputTextoObligatorio" size="6" maxlength="4" onkeyup="SoloDigitos(this);" 
                                       value="<%=datModif != null && datModif.getNumPuesto() != null ? datModif.getNumPuesto() : ""%>" disabled/>
                            </div>
                            <div class="etiquetaIker" style="width: 110px;">
                                <%=meLanbide86I18n.getMensaje(idiomaUsuario,"label.denomPuesto")%>
                            </div>
                            <div style="width:350px; float: left; ">
                                <textarea  id="denomPuesto" name="denomPuesto" type="text" class="inputTextoObligatorio" rows="5" cols="100" maxlength="500" disabled
                                           style="text-align: left"><%=datModif != null && datModif.getDenomPuesto() != null ? datModif.getDenomPuesto() : ""%>
                                </textarea>
                            </div>
                        </div>
                    </fieldset>
                    <fieldset id="contratacion" name="contratacion">
                        <legend class="legendAzul" id="titPuesto"><%=meLanbide86I18n.getMensaje(idiomaUsuario,"label.contratacion")%></legend>
                        <div class="lineaFormulario">
                            <div class="etiquetaIker">
                                <%=meLanbide86I18n.getMensaje(idiomaUsuario,"label.actividad")%>
                            </div>
                            <div style="width:350px; float: left;">
                                <input type="text" name="codListaAct1" id="codListaAct1" size="3" class="inputComboObligatorio" value="" onkeyup="xAMayusculas(this);" maxlength="2"/>
                                <input type="text" name="descListaAct1"  id="descListaAct1" size="30" class="inputComboObligatorio" readonly value="" />
                                <a href="" id="anchorListaAct1" name="anchorListaAct1">
                                    <span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonAplicacion"
                                          style="cursor:hand;"></span>
                                </a>
                            </div>
                            <div  class="etiquetaIker">
                                <%=meLanbide86I18n.getMensaje(idiomaUsuario,"label.titulacion")%>
                            </div>
                            <div style="width:350px; float: left;">
                                <input id="titulacion1" name="titulacion1" type="text" class="inputTextoObligatorio" size="30" maxlength="150"
                                       value="<%=datModif != null && datModif.getTitulacion1() != null ? datModif.getTitulacion1() : ""%>" />
                            </div>
                        </div>
                        <br><br>
                        <div class="lineaFormulario">
                            <div  class="etiquetaIker">
                                <%=meLanbide86I18n.getMensaje(idiomaUsuario,"label.tipoContrato")%>
                            </div>
                            <div style="width:350px; float: left;">
                                <input id="tipoCont1" name="tipoCont1" type="text" class="inputTextoObligatorio" size="30" maxlength="150"
                                       value="<%=datModif != null && datModif.getTipoCont1() != null ? datModif.getTipoCont1() : ""%>" />
                            </div>                       
                            <div  class="etiquetaIker">
                                <%=meLanbide86I18n.getMensaje(idiomaUsuario,"label.duracion")%>
                            </div>
                            <div style="width:350px; float: left;">
                                <input id="durContrato1" name="durContrato1" type="text" class="inputTextoObligatorio" size="6" maxlength="4" onkeyup="SoloDigitos(this);"
                                       value="<%=datModif != null && datModif.getDurContrato1() != null ? datModif.getDurContrato1() : ""%>" />
                            </div>
                        </div>
                        <br><br>
                        <div class="lineaFormulario">
                            <div class="etiquetaIker">
                                <%=meLanbide86I18n.getMensaje(idiomaUsuario,"label.grupo")%>
                            </div>
                            <div style="width:350px; float: left;">
                                <input type="text" name="codListaGrupo1" id="codListaGrupo1" size="3" class="inputComboObligatorio" value="" onkeyup="SoloDigitos(this);" maxlength="2"/>
                                <input type="text" name="descListaGrupo1"  id="descListaGrupo1" size="15" class="inputComboObligatorio" readonly value="" />
                                <a href="" id="anchorListaGrupo1" name="anchorListaGrupo1">
                                    <span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonAplicacion"
                                          style="cursor:hand;"></span>
                                </a>
                            </div>
                            <div class="etiquetaIker">
                                <%=meLanbide86I18n.getMensaje(idiomaUsuario,"label.campoInve")%>
                            </div>
                            <div style="width:350px; float: left;">
                                <input id="cainVinn1" name="cainVinn1" type="text" class="inputTextoObligatorio" size="30" maxlength="150" 
                                       value="<%=datModif != null && datModif.getCainVinn1() != null ? datModif.getCainVinn1() : ""%>" />
                            </div>
                        </div>
                        <br><br>
                        <div class="lineaFormulario">
                            <div class="etiquetaIker">
                                <%=meLanbide86I18n.getMensaje(idiomaUsuario,"label.costeSalarial")%>
                            </div>
                            <div style="width:350px; float: left;">
                                <input id="costeSalarial1" name="costeSalarial1" type="text" class="inputTextoObligatorio" size="9" maxlength="9" onchange="reemplazarPuntos(this);"
                                       value="<%=datModif != null && datModif.getCosteSalarial1() != null ? datModif.getCosteSalarial1().toString().replace(".",",") : ""%>" />
                            </div>
                            <div class="etiquetaIker">
                                <%=meLanbide86I18n.getMensaje(idiomaUsuario,"label.subvSolicitada")%>
                            </div>
                            <div style="width: 300px;float: left;">
                                <input id="subvSolicitada1" name="subvSolicitada1" type="text" class="inputTextoObligatorio" size="9" maxlength="9" onchange="reemplazarPuntos(this);"
                                       value="<%=datModif != null && datModif.getSubvSolicitada1() != null ? datModif.getSubvSolicitada1().toString().replace(".",",") : ""%>" />
                            </div>
                        </div>
                    </fieldset>
                    <fieldset id="contrataIni" name="contrataIni">
                        <legend class="legendAzul" id="titPuesto"><%=meLanbide86I18n.getMensaje(idiomaUsuario,"label.contrataIni")%></legend>
                        <div class="lineaFormulario">
                            <div class="etiquetaIker">
                                <%=meLanbide86I18n.getMensaje(idiomaUsuario,"label.oferta")%>
                            </div>
                            <div style="width:350px; float: left;">
                                <input id="nOferta2" name="nOferta2" type="text" class="inputTexto" size="20" maxlength="50" onkeyup="SoloAlfanumericos_espacio(this);"
                                       value="<%=datModif != null && datModif.getnOferta2() != null ? datModif.getnOferta2() : ""%>" />
                            </div>
                        </div> 
                        <br><br>
                        <div class="lineaFormulario">
                            <div class="etiquetaIker">
                                <%=meLanbide86I18n.getMensaje(idiomaUsuario,"label.dni")%>
                            </div>
                            <div style="width:350px; float: left;">
                                <input id="dniNie2" name="dniNie2" type="text" class="inputTexto" size="10" maxlength="9" onkeyup="xAMayusculas(this);"
                                       value="<%=datModif != null && datModif.getDniNie2() != null ? datModif.getDniNie2() : ""%>" />
                            </div>
                            <div class="etiquetaIker">
                                <%=meLanbide86I18n.getMensaje(idiomaUsuario,"label.nombre")%>
                            </div>
                            <div style="width:350px; float: left;">
                                <input id="nombre2" name="nombre2" type="text" class="inputTexto" size="30" maxlength="100"
                                       value="<%=datModif != null && datModif.getNombre2() != null ? datModif.getNombre2() : ""%>" />
                            </div>
                        </div> 
                        <br><br>
                        <div class="lineaFormulario">
                            <div class="etiquetaIker">
                                <%=meLanbide86I18n.getMensaje(idiomaUsuario,"label.ape1")%>
                            </div>
                            <div style="width:350px; float: left;">
                                <input id="apellido12" name="apellido12" type="text" class="inputTexto" size="30" maxlength="50" 
                                       value="<%=datModif != null && datModif.getApellido12() != null ? datModif.getApellido12() : ""%>" />
                            </div>
                            <div class="etiquetaIker">
                                <%=meLanbide86I18n.getMensaje(idiomaUsuario,"label.ape2")%>
                            </div>
                            <div style="width:350px; float: left;">
                                <input id="apellido22" name="apellido22" type="text" class="inputTexto" size="30" maxlength="50" 
                                       value="<%=datModif != null && datModif.getApellido22() != null ? datModif.getApellido22() : ""%>" />
                            </div>                            
                        </div> 
                        <br><br>
                        <div class="lineaFormulario">                            
                            <div class="etiquetaIker">
                                <%=meLanbide86I18n.getMensaje(idiomaUsuario,"label.sexo")%>
                            </div>
                            <div style="width:350px; float: left;">
                                <input type="text" name="codListaSexo2" id="codListaSexo2" size="3" class="inputTexto" value="" onkeyup="SoloDigitos(this);" maxlength="1"/>
                                <input type="text" name="descListaSexo2"  id="descListaSexo2" size="10" class="inputTexto" readonly value="" />
                                <a href="" id="anchorListaSexo2">
                                    <span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonAplicacion"
                                          style="cursor:hand;"></span>
                                </a>
                            </div>
                        </div> 
                        <br><br>
                        <div class="lineaFormulario">
                            <div class="etiquetaIker">
                                <%=meLanbide86I18n.getMensaje(idiomaUsuario,"label.actividad")%>
                            </div>
                            <div style="width:350px; float: left;">
                                <input type="text" name="codListaAct2" id="codListaAct2" size="3" class="inputTexto" value="" onkeyup="xAMayusculas(this);" maxlength="2"/>
                                <input type="text" name="descListaAct2"  id="descListaAct2" size="30" class="inputTexto" readonly value="" />
                                <a href="" id="anchorListaAct2">
                                    <span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonAplicacion"
                                          style="cursor:hand;"></span>
                                </a>
                            </div>
                            <div class="etiquetaIker">
                                <%=meLanbide86I18n.getMensaje(idiomaUsuario,"label.titulacion")%>
                            </div>
                            <div style="width:350px; float: left;">
                                <input id="titulacion2" name="titulacion2" type="text" class="inputTexto" size="30" maxlength="150" onkeyup="SoloAlfanumericos_espacio(this);"
                                       value="<%=datModif != null && datModif.getTitulacion2() != null ? datModif.getTitulacion2() : ""%>" />
                            </div>
                        </div> 
                        <br><br>                        
                        <div class="lineaFormulario">
                            <div class="etiquetaIker">
                                <%=meLanbide86I18n.getMensaje(idiomaUsuario,"label.tipoContrato")%>
                            </div>
                            <div style="width:350px; float: left;">
                                <input id="tipoCont2" name="tipoCont2" type="text" class="inputTexto" size="30" maxlength="150" onkeyup="SoloAlfanumericos_espacio(this);"
                                       value="<%=datModif != null && datModif.getTipoCont2() != null ? datModif.getTipoCont2() : ""%>" />
                            </div>
                            <div class="etiquetaIker">
                                <%=meLanbide86I18n.getMensaje(idiomaUsuario,"label.duracion")%>
                            </div>
                            <div style="width:350px; float: left;">
                                <input id="durContrato2" name="durContrato2" type="text" class="inputTexto" size="6" maxlength="4" onkeyup="SoloDigitos(this);"
                                       value="<%=datModif != null && datModif.getDurContrato2() != null ? datModif.getDurContrato2() : ""%>" />
                            </div>
                        </div> 
                        <br><br>
                        <div class="lineaFormulario">
                            <div class="etiquetaIker">
                                <%=meLanbide86I18n.getMensaje(idiomaUsuario,"label.grupo")%>
                            </div>
                            <div style="width:350px; float: left;">
                                <input type="text" name="codListaGrupo2" id="codListaGrupo2" size="3" class="inputTexto" value="" onkeyup="SoloDigitos(this);" maxlength="2"/>
                                <input type="text" name="descListaGrupo2"  id="descListaGrupo2" size="15" class="inputTexto" readonly value="" />
                                <a href="" id="anchorListaGrupo2">
                                    <span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonAplicacion"
                                          style="cursor:hand;"></span>
                                </a>
                            </div>
                            <div class="etiquetaIker">
                                <%=meLanbide86I18n.getMensaje(idiomaUsuario,"label.campoInve")%>
                            </div>
                            <div style="width:350px; float: left;">
                                <input id="cainVinn2" name="cainVinn2" type="text" class="inputTexto" size="30" maxlength="150" 
                                       value="<%=datModif != null && datModif.getCainVinn2() != null ? datModif.getCainVinn2() : ""%>" />
                            </div>
                        </div>
                        <br><br>
                        <div class="lineaFormulario">
                            <div class="etiquetaIker">
                                <%=meLanbide86I18n.getMensaje(idiomaUsuario,"label.fNac")%>
                            </div>
                            <div style="width:350px; float: left;">
                                <input type="text" class="inputTxtFecha" 
                                       id="fechaNacimiento2" name="fechaNacimiento2"
                                       maxlength="10"  size="10"
                                       value="<%=fechaNacimiento2%>"
                                       onkeyup = "return SoloCaracteresFecha(this);"
                                       onblur = "calcularEdad();"
                                       onfocus="javascript:this.select();"/>
                                <A href="javascript:calClick(event);return false;" onClick="mostrarCalFechaNacimiento2(event);
                                        return false;" style="text-decoration: none;">
                                    <IMG style="border: 0" height="17" id="calFechaNacimiento2" border="0" 
                                         src="<c:url value='/images/calendario/icono.gif'/>" > 
                                </A>
                            </div>
                            <div class="etiquetaIker">
                                <%=meLanbide86I18n.getMensaje(idiomaUsuario,"label.fInicio")%>
                            </div>
                            <div style="width:350px; float: left;">
                                <input type="text" class="inputTxtFecha" 
                                       id="fechaInicio2" name="fechaInicio2"
                                       maxlength="10"  size="10"
                                       value="<%=fechaInicio2%>"
                                       onkeyup = "return SoloCaracteresFecha(this);"
                                       onblur = "calcularEdad();"
                                       onfocus="javascript:this.select();"/>
                                <A href="javascript:calClick(event);return false;" onClick="mostrarCalFechaInicio2(event);
                                        return false;" style="text-decoration: none;">
                                    <IMG style="border: 0" height="17" id="calFechaInicio2" border="0" 
                                         src="<c:url value='/images/calendario/icono.gif'/>" > 
                                </A>
                            </div>
                        </div>
                        <br><br>
                        <div class="lineaFormulario">
                            <div class="etiquetaIker">
                                <%=meLanbide86I18n.getMensaje(idiomaUsuario,"label.edad")%>
                            </div>
                            <div style="width:350px; float: left;">
                                <input id="edad2" name="edad2" type="text" class="inputTexto" size="4" maxlength="2" onkeyup="SoloDigitos(this);"
                                       value="<%=datModif != null && datModif.getEdad2() != null ? datModif.getEdad2() : ""%>" />
                            </div>                       
                            <div class="etiquetaIker">
                                <%=meLanbide86I18n.getMensaje(idiomaUsuario,"label.retribucion")%>
                            </div>
                            <div style="width: 300px;float: left;">
                                <input id="retribucionBruta2" name="retribucionBruta2" type="text" class="inputTexto" size="9" maxlength="9" onchange="reemplazarPuntos(this);"
                                       value="<%=datModif != null && datModif.getRetribucionBruta2() != null ? datModif.getRetribucionBruta2().toString().replace(".",",")  : ""%>" />
                            </div>
                        </div>                       
                    </fieldset>
                    <fieldset id="contrataFin" name="contrataFin">
                        <legend class="legendAzul" id="titPuesto"><%=meLanbide86I18n.getMensaje(idiomaUsuario,"label.contrataFin")%></legend>
                        <div class="lineaFormulario">
                            <div class="etiquetaIker">
                                <%=meLanbide86I18n.getMensaje(idiomaUsuario,"label.dni")%>
                            </div>
                            <div style="width:350px; float: left;">
                                <input id="dniNie3" name="dniNie3" type="text" class="inputTexto" size="10" maxlength="9" onkeyup="xAMayusculas(this);"
                                       value="<%=datModif != null && datModif.getDniNie3() != null ? datModif.getDniNie3() : ""%>" />
                            </div>
                            <div class="etiquetaIker">
                                <%=meLanbide86I18n.getMensaje(idiomaUsuario,"label.nombre")%>
                            </div>
                            <div style="width:350px; float: left;">
                                <input id="nombre3" name="nombre3" type="text" class="inputTexto" size="30" maxlength="100"
                                       value="<%=datModif != null && datModif.getNombre3() != null ? datModif.getNombre3() : ""%>" />
                            </div>
                        </div> 
                        <br><br>
                        <div class="lineaFormulario">
                            <div class="etiquetaIker">
                                <%=meLanbide86I18n.getMensaje(idiomaUsuario,"label.ape1")%>
                            </div>
                            <div style="width:350px; float: left;">
                                <input id="apellido13" name="apellido13" type="text" class="inputTexto" size="30" maxlength="50" 
                                       value="<%=datModif != null && datModif.getApellido13() != null ? datModif.getApellido13() : ""%>" />
                            </div> 

                            <div class="etiquetaIker">
                                <%=meLanbide86I18n.getMensaje(idiomaUsuario,"label.ape2")%>
                            </div>
                            <div style="width:350px; float: left;">
                                <input id="apellido23" name="apellido23" type="text" class="inputTexto" size="30" maxlength="50" 
                                       value="<%=datModif != null && datModif.getApellido23() != null ? datModif.getApellido23() : ""%>" />
                            </div>
                        </div> 
                        <br><br>
                        <div class="lineaFormulario">
                            <div class="etiquetaIker">
                                <%=meLanbide86I18n.getMensaje(idiomaUsuario,"label.duracion")%>
                            </div>
                            <div style="width:350px; float: left;">
                                <input id="durContrato3" name="durContrato3" type="text" class="inputTexto" size="6" maxlength="4" onkeyup="SoloDigitos(this);"
                                       value="<%=datModif != null && datModif.getDurContrato3() != null ? datModif.getDurContrato3() : ""%>" />
                            </div>
                        </div> 
                        <br><br>
                        <div class="lineaFormulario">
                            <div class="etiquetaIker">
                                <%=meLanbide86I18n.getMensaje(idiomaUsuario,"label.fInicio")%>
                            </div> 
                            <div style="width:350px; float: left;">
                                <input type="text" class="inputTxtFecha" 
                                       id="fechaInicio3" name="fechaInicio3"
                                       maxlength="10"  size="10"
                                       value="<%=fechaInicio3%>"
                                       onkeyup = "return SoloCaracteresFecha(this);"
                                       onblur = "javascript:return comprobarFecha(this);"
                                       onfocus="javascript:this.select();"/>
                                <A href="javascript:calClick(event);return false;" onClick="mostrarCalFechaInicio3(event);
                                        return false;" style="text-decoration: none;">
                                    <IMG style="border: 0px solid" height="17" id="calFechaInicio3"  border="0" 
                                         src="<c:url value='/images/calendario/icono.gif'/>" > 
                                </A>
                            </div>
                            <div class="etiquetaIker">
                                <%=meLanbide86I18n.getMensaje(idiomaUsuario,"label.fFin")%>
                            </div>
                            <div style="width:350px; float: left;">
                                <input type="text" class="inputTxtFecha" 
                                       id="fechaFin3" name="fechaFin3"
                                       maxlength="10"  size="10"
                                       value="<%=fechaFin3%>"
                                       onkeyup = "return SoloCaracteresFecha(this);"
                                       onblur = "javascript:return comprobarFecha(this);"
                                       onfocus="javascript:this.select();"/>
                                <A href="javascript:calClick(event);return false;" onClick="mostrarCalFechaFin3(event);
                                        return false;" style="text-decoration: none;">
                                    <IMG style="border: 0px solid" height="17" id="calFechaFin3"  border="0" 
                                         src="<c:url value='/images/calendario/icono.gif'/>" >
                                </A>
                            </div>
                        </div>
                        <br><br>                            
                        <div class="lineaFormulario">
                            <div class="etiquetaIker">
                                <%=meLanbide86I18n.getMensaje(idiomaUsuario,"label.costeSal")%>
                            </div>
                            <div style="width:350px;float:left;">
                                <input id="costeSalarial3" name="costeSalarial3" type="text" class="inputTexto" size="9" maxlength="9" onchange="reemplazarPuntos(this);"
                                       value="<%=datModif != null && datModif.getCosteSalarial3() != null ? datModif.getCosteSalarial3().toString().replace(".",",")  : ""%>" />
                            </div>
                            <div class="etiquetaIker">
                                <%=meLanbide86I18n.getMensaje(idiomaUsuario,"label.costeSS")%>
                            </div>
                            <div style="width:350px;float:left;">
                                <input id="costesSS3" name="costesSS3" type="text" class="inputTexto" size="9" maxlength="9" onchange="reemplazarPuntos(this);"
                                       value="<%=datModif != null && datModif.getCostesSS3() != null ? datModif.getCostesSS3().toString().replace(".",",")  : ""%>" />
                            </div>
                        </div>
                        <br><br>
                        <div class="lineaFormulario">
                            <div class="etiquetaIker">
                                <%=meLanbide86I18n.getMensaje(idiomaUsuario,"label.costeTotal")%>
                            </div>
                            <div style="width:350px;float:left;">
                                <input id="costeTotalReal" name="costeTotalReal" type="text" class="inputTexto" size="9" maxlength="9" onchange="reemplazarPuntos(this);"
                                       value="<%=datModif != null && datModif.getCosteTotalReal() != null ? datModif.getCosteTotalReal().toString().replace(".",",")  : ""%>" />
                            </div>
                            <div class="etiquetaIker">
                                <%=meLanbide86I18n.getMensaje(idiomaUsuario,"label.subvConcedida")%>
                            </div>
                            <div style="width:350px;float:left;">
                                <input id="subvConcedidaLan3" name="subvConcedidaLan3" type="text" class="inputTexto" size="9" maxlength="9" onchange="reemplazarPuntos(this);"
                                       value="<%=datModif != null && datModif.getSubvConcedidaLan3() != null ? datModif.getSubvConcedidaLan3().toString().replace(".",",")  : ""%>" />
                            </div>
                        </div>					
                    </fieldset>
                    <div class="lineaFormulario" style="margin-top: 25px;">
                        <div class="botonera" style="width: 100%; float: left; text-align: center;">
                            <input type="button" id="btnAceptar" name="btnAceptar" class="botonGeneral" value="<%=meLanbide86I18n.getMensaje(idiomaUsuario, "btn.aceptar")%>" onclick="guardarDatos();"/>
                            <input type="button" id="btnCancelar" name="btnCancelar" class="botonGeneral" value="<%=meLanbide86I18n.getMensaje(idiomaUsuario, "btn.cancelar")%>" onclick="cancelar();"/>
                        </div>
                    </div>
                </div>
            </form>
            <script type="text/javascript">
                /*  ACT 1 */
                listaCodigosAct1[0] = "";
                listaDescripcionesAct1[0] = "";
                contador = 0;
                <logic:iterate id="act1" name="listaActDes" scope="request">
                listaCodigosAct1[contador] = ['<bean:write name="act1" property="des_val_cod" />'];
                listaDescripcionesAct1[contador] = ['<bean:write name="act1" property="des_nom" />'];
                contador++;
                </logic:iterate>
                var comboListaAct1 = new Combo("ListaAct1");
                comboListaAct1.addItems(listaCodigosAct1, listaDescripcionesAct1);
                comboListaAct1.change = cargarDatosAct1;

                /*  GRUPO 1 */
                listaCodigosGrupo1[0] = "";
                listaDescripcionesGrupo1[0] = "";
                contador = 0;
                <logic:iterate id="grupo1" name="listaGrupoCot" scope="request">
                listaCodigosGrupo1[contador] = ['<bean:write name="grupo1" property="des_val_cod" />'];
                listaDescripcionesGrupo1[contador] = ['<bean:write name="grupo1" property="des_nom" />'];
                contador++;
                </logic:iterate>
                var comboListaGrupo1 = new Combo("ListaGrupo1");
                comboListaGrupo1.addItems(listaCodigosGrupo1, listaDescripcionesGrupo1);
                comboListaGrupo1.change = cargarDatosGrupo1;

                /*  SEXO 2 */
                listaCodigosSexo2[0] = "";
                listaDescripcionesSexo2[0] = "";
                contador = 0;
                <logic:iterate id="sexo2" name="listaSexo" scope="request">
                listaCodigosSexo2[contador] = ['<bean:write name="sexo2" property="des_val_cod" />'];
                listaDescripcionesSexo2[contador] = ['<bean:write name="sexo2" property="des_nom" />'];
                contador++;
                </logic:iterate>
                var comboListaSexo2 = new Combo("ListaSexo2");
                comboListaSexo2.addItems(listaCodigosSexo2, listaDescripcionesSexo2);
                comboListaSexo2.change = cargarDatosSexo2;

                /*  ACT 2 */
                listaCodigosAct2[0] = "";
                listaDescripcionesAct2[0] = "";
                contador = 0;
                <logic:iterate id="act2" name="listaActDes" scope="request">
                listaCodigosAct2[contador] = ['<bean:write name="act2" property="des_val_cod" />'];
                listaDescripcionesAct2[contador] = ['<bean:write name="act2" property="des_nom" />'];
                contador++;
                </logic:iterate>
                var comboListaAct2 = new Combo("ListaAct2");
                comboListaAct2.addItems(listaCodigosAct2, listaDescripcionesAct2);
                comboListaAct2.change = cargarDatosAct2;

                /*  GRUPO 2 */
                listaCodigosGrupo2[0] = "";
                listaDescripcionesGrupo2[0] = "";
                contador = 0;
                <logic:iterate id="grupo2" name="listaGrupoCot" scope="request">
                listaCodigosGrupo2[contador] = ['<bean:write name="grupo2" property="des_val_cod" />'];
                listaDescripcionesGrupo2[contador] = ['<bean:write name="grupo2" property="des_nom" />'];
                contador++;
                </logic:iterate>
                var comboListaGrupo2 = new Combo("ListaGrupo2");
                comboListaGrupo2.addItems(listaCodigosGrupo2, listaDescripcionesGrupo2);
                comboListaGrupo2.change = cargarDatosGrupo2;

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

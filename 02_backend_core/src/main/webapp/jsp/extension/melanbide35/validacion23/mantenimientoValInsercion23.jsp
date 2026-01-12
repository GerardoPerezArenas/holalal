<%@taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide35.i18n.MeLanbide35I18n" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide35.vo.justificacion.JusInsercionesECA23VO" %>
<%@page import="es.altia.flexia.registro.digitalizacion.lanbide.vo.GeneralComboVO" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide35.util.ConstantesMeLanbide35" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide35.util.ConfigurationParameter" %>
<%@page import="java.util.ArrayList" %>
<%@page import="java.util.List" %>
<%@page import="java.text.SimpleDateFormat" %>
<%@page import="com.google.gson.Gson" %>
<%@page import="com.google.gson.GsonBuilder" %>
<html>
    <head>
        <meta http-equiv="X-UA-Compatible" content="IE=Edge" />
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <%
            JusInsercionesECA23VO datModif = new JusInsercionesECA23VO();
            List<GeneralComboVO> listSexo = null;
			List<GeneralComboVO> listSexoIdioma = null;
            List<GeneralComboVO> listTipoDisc = null;
            List<GeneralComboVO> listColectivo = null;
            List<GeneralComboVO> listTipoContrato = null;
            List<GeneralComboVO> listTipoEdadSexo = null;
            List<GeneralComboVO> listCnae = null;

            String listSexoGA = "";
            String listTipoDiscGA = "";
            String listColectivoGA = "";
            String listTipoContratoGA = "";
            String listTipoEdadSexoGA = "";
            String listCnaeGA = "";

            String codOrganizacion = "";
            String nuevo = "";
			String id = "";
            String numExpediente = "";
			String dni = "";
            String nombre = "";
            String apellido1 = "";
            String apellido2 = "";
			String sexo = ""; // Combo
			String descSexo = "";
			String fecNacimiento = ""; // Calendario
			String tipoDisc = ""; // Combo
			String descTipoDisc = "";
			String grado = "";
			String colectivo = ""; // Combo
			String descColectivo = "";
			String tipoContrato = ""; // Combo
			String descTipoContrato = "";
			String jornada = "";
			String fecInicio = ""; // Calendario
			String tipoEdadSexo = ""; // Combo
			String descTipoEdadSexo = "";
			String fecFin = ""; // Calendario
			String dias = "";
			String edad = "";
			String empresa = "";
			String nifEmpresa = "";
			String cnae = "";
			String descCnae = "";
			String nifPreparador = "";
			String importeInser = "";

            MeLanbide35I18n meLanbide35I18n = MeLanbide35I18n.getInstance();

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
                catch(Exception ex) {}

                codOrganizacion  = request.getParameter("codOrganizacionModulo");
                nuevo = (String)request.getAttribute("nuevo");
                if(request.getAttribute("datModif") != null)
                {
                    datModif = (JusInsercionesECA23VO)request.getAttribute("datModif");
                    SimpleDateFormat formatoFecha = new SimpleDateFormat(ConstantesMeLanbide35.FORMATO_FECHA);
					id = datModif.getId() != null ? datModif.getId().toString() : "";
					dni = datModif.getDni();
					nombre = datModif.getNombre();
					apellido1 = datModif.getApellido1();
					apellido2 = datModif.getApellido2();
					sexo = datModif.getSexo();
					descSexo = datModif.getDescSexo();
                    fecNacimiento = datModif.getfNacimiento() != null ? formatoFecha.format(datModif.getfNacimiento()) : "";
					tipoDisc = datModif.getTipoDisc();
					descTipoDisc = datModif.getDescTipoDisc();
					grado = datModif.getGrado() != null ? datModif.getGrado().toString() : "";
					colectivo = datModif.getColectivo();
					descColectivo = datModif.getDescColectivo();
					tipoContrato = datModif.getTipoContrato();
					descTipoContrato = datModif.getDescTipoContrato();
					jornada = datModif.getJornada() != null ? datModif.getJornada().toString() : "";
                    fecInicio = datModif.getfInicio() != null ? formatoFecha.format(datModif.getfInicio()) : "";
					tipoEdadSexo = datModif.getTipoEdadSexo();
					descTipoEdadSexo = datModif.getDescTipoEdadSexo();
					fecFin = datModif.getfFin() != null ? formatoFecha.format(datModif.getfFin()) : "";
					dias = datModif.getDias() != null && !datModif.getDias().toString().equals("") && !datModif.getDias().toString().equals("0")  ? datModif.getDias().toString() : "";
					edad = datModif.getEdad() != null ? datModif.getEdad().toString() : "";
					empresa = datModif.getEmpresa();
					nifEmpresa = datModif.getNifEmpresa();
					cnae = datModif.getCnae();
					descCnae = datModif.getDescCnaeC();
					nifPreparador = datModif.getNifPreparador();
					importeInser = datModif.getImporteInser() != null ? datModif.getImporteInser().toString() : "";
                }

				//Combos
				Gson gson = new Gson();
				GsonBuilder gsonB = new GsonBuilder().setDateFormat(ConstantesMeLanbide35.FORMATO_FECHA);
				gsonB.serializeNulls();

				gson = gsonB.create();
				listSexo = (List<GeneralComboVO>)request.getAttribute("listaSexo");
				listTipoDisc = (List<GeneralComboVO>)request.getAttribute("listaTipoDisc");
				listColectivo = (List<GeneralComboVO>)request.getAttribute("listaColectivo");
				listTipoContrato = (List<GeneralComboVO>)request.getAttribute("listaTipoContrato");
				listTipoEdadSexo = (List<GeneralComboVO>)request.getAttribute("listaTipoEdadSexo");
				listCnae = (List<GeneralComboVO>)request.getAttribute("listaCnae");

				listSexoGA = gson.toJson(listSexo);
				listTipoDiscGA = gson.toJson(listTipoDisc);
				listColectivoGA = gson.toJson(listColectivo);
				listTipoContratoGA = gson.toJson(listTipoContrato);
			    listTipoEdadSexoGA = gson.toJson(listTipoEdadSexo);
			    listCnaeGA = gson.toJson(listCnae);
            }
            catch(Exception ex) {}
        %>
        <jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
        <jsp:setProperty name="descriptor"  property="idi_cod" value="<%=idiomaUsuario%>" />
        <jsp:setProperty name="descriptor"  property="apl_cod" value="<%=apl%>" />
        <link rel="StyleSheet" media="screen" type="text/css" href="<%=request.getContextPath()%><%=css%>">
        <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/css/font-awesome.min.css'/>" media="screen">
        <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/less/animated.less'/>" media="screen">
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/>
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/extension/melanbide35/melanbide35.css"/>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/JavaScriptUtil.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/general.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/melanbide35/ecaUtils.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/melanbide35/gestionCombos/gestionCombos.js"></script>
        <script type="text/javascript" src="<c:url value='/scripts/listaComboBox.js'/>"></script>
        <script type="text/javascript" src="<c:url value='/scripts/calendario.js'/>"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/validaciones.js"></script>
        <script type="text/javascript">
            var APP_CONTEXT_PATH = '<%=request.getContextPath()%>';
            var url = APP_CONTEXT_PATH + '/PeticionModuloIntegracion.do';
            var mensajeValidacion = '';

            //Funcion para el calendario de fecha
            function mostrarCalFechaNacimiento(evento) {
                //console.log("mostrarCalFechaNacimiento");
                if (window.event)
                    evento = window.event;
                if (document.getElementById("calFechaNacimiento").src.indexOf("icono.gif") != -1)
                    showCalendar('forms[0]', 'fecNacimiento', null, null, null, '', 'calFechaNacimiento', '', null, null, null, null, null, null, null, 'calcularEdad()', evento);

            }

            function mostrarCalFechaInicio(evento) {
                //console.log("mostrarCalFechaInicio");
                if (window.event)
                    evento = window.event;
                if (document.getElementById("calFechaInicio").src.indexOf("icono.gif") != -1)
                    showCalendar('forms[0]', 'fecInicio', null, null, null, '', 'calFechaInicio', '', null, null, null, null, null, null, null, null, evento);
            }

            function mostrarCalFechaFin(evento) {
                //console.log("mostrarCalFechaFin");
                if (window.event)
                    evento = window.event;
                if (document.getElementById("calFechaFin").src.indexOf("icono.gif") != -1)
                    showCalendar('forms[0]', 'fecFin', null, null, null, '', 'calFechaFin', '', null, null, null, null, null, null, null, null, evento);
            }

			function validarDatos() {
                var campo = document.getElementById("dni");
                if (campo.value != null && campo.value != '') {
                    if (!validarDniNie(campo)) {
                        mensajeValidacion = '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "msg.dniIncorrecto")%>';
                        return false;
                    }
                }
				else {
					mensajeValidacion = '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "msg.dniVacio")%>';
                    return false;
				}
                campo = document.getElementById('nombre').value;
                if (campo == null || campo == '') {
                    mensajeValidacion = '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "msg.nombreVacio")%>';
                    return false;
				}

                campo = document.getElementById('apellido1').value;
                if (campo == null || campo == '') {
                    mensajeValidacion = '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "msg.apellido1Vacio")%>';
                    return false;
				}

                campo = document.getElementById('codSexo').value;
                if (campo == null || campo == '') {
                    mensajeValidacion = '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "msg.sexoVacio")%>';
                    return false;
				}
                campo = document.getElementById('fecNacimiento').value;
                if (campo == null || campo == '') {
                    mensajeValidacion = '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "msg.fecNacimientoVacio")%>';
                    return false;
				}

                campo = document.getElementById('codTipoDisc').value;
                if (campo == null || campo == '') {
                    mensajeValidacion = '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "msg.tipoDiscVacio")%>';
                    return false;
				}

                campo = document.getElementById('grado').value;
                if (campo == null || campo == '') {
                    mensajeValidacion = '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "msg.gradoVacio")%>';
                    return false;
                } else if (!validarNumericoDecimalPrecision(campo, 6, 2)) {
                    mensajeValidacion = '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "msg.grado.errNumerico")%>';
                    return false;
                }

                campo = document.getElementById('codColectivo').value;
                if (campo == null || campo == '') {
                    mensajeValidacion = '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "msg.colectivoVacio")%>';
                    return false;
				}
                campo = document.getElementById('codTipoContrato').value;
                if (campo == null || campo == '') {
                    mensajeValidacion = '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "msg.tipoContratoVacio")%>';
                    return false;
				}

                campo = document.getElementById('jornada').value;
                if (campo == null || campo == '') {
                    mensajeValidacion = '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "msg.jornadaVacio")%>';
                    return false;
                } else if (!validarNumericoDecimalPrecision(campo, 6, 2)) {
                    mensajeValidacion = '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "msg.jornada.errNumerico")%>';
                    return false;
                }

                campo = document.getElementById('fecInicio').value;
                if (campo == null || campo == '') {
                    mensajeValidacion = '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "msg.fecInicioVacio")%>';
                    return false;
				}
                campo = document.getElementById('codTipoEdadSexo').value;
                if (campo == null || campo == '') {
                    mensajeValidacion = '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "msg.tipoEdadSexoVacio")%>';
                    return false;
				}

                campo = document.getElementById('dias').value;
                if (campo != null && campo != '' && Number.isInteger(campo)) {
                    mensajeValidacion = '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "msg.diasVacio.errNumerico")%>';
                    return false;
                }

                campo = document.getElementById('empresa').value;
                if (campo == null || campo == '') {
                    mensajeValidacion = '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "msg.EmpresaVacio")%>';
                    return false;
				}

                campo = document.getElementById('codCnae').value;
                if (campo == null || campo == '') {
                    mensajeValidacion = '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "msg.cnaeVacio")%>';
                    return false;
				}

                campo = document.getElementById('nifEmpresa');
                if (campo.value == null || campo.value == '') {
                    mensajeValidacion = '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "msg.nifEmpresaVacio")%>';
                    return false;
				}
				else if (valida_nif_cif_nie(campo.value) != 2) {
                    mensajeValidacion = '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "msg.nifEmpresaVacio.format")%>';
                    return false;
				}
                campo = document.getElementById('nifPreparador');
                if (campo.value == null || campo.value == '') {
                    mensajeValidacion = '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "msg.nifPreparadorVacio")%>';
                    return false;
                }
				else if (!validarDniNie(campo)) {
                    mensajeValidacion = '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "msg.nifPreparadorVacio.format")%>';
                    return false;
				}

                campo = document.getElementById('importeInser').value;
                if (campo == null || campo == '') {
                    mensajeValidacion = '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "msg.importeInserVacio")%>';
                    return false;
                } else if (!validarNumericoDecimalPrecision(campo, 10, 2)) {
                    mensajeValidacion = '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "msg.importeInserVacio.errNumerico")%>';
                    return false;
                }


                return true;
            }

			function cancelar() {
                var resultado = jsp_alerta('', '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "msg.preguntaCancelar")%>');
                if (resultado == 1) {
                    cerrarVentana();
                }
            }

            function guardarDatos() {
                if (validarDatos()) {
                    //elementoVisible('on', 'barraProgresoECA');
                    var parametros = "";

                    if ('<%=nuevo%>' != null && '<%=nuevo%>' == "1") {
                        parametros = "tarea=preparar&modulo=MELANBIDE35&operacion=guardarValInsercion23&tipo=0";
                    } else {
                        parametros = "tarea=preparar&modulo=MELANBIDE35&operacion=modificarValInsercion23&tipo=0"
                                + "&id=<%=datModif != null && datModif.getId() != null ? datModif.getId().toString() : ""%>";
                    }
                    parametros += '&numExpediente=<%=numExpediente%>'
                            + "&dni=" + document.getElementById('dni').value
                            + "&nombre=" + document.getElementById('nombre').value
                            + "&apellido1=" + document.getElementById('apellido1').value
                            + "&apellido2=" + document.getElementById('apellido2').value
                            + "&sexo=" + document.getElementById('codSexo').value
                            + "&fecNac=" + document.getElementById('fecNacimiento').value
                            + "&tipoDisc=" + document.getElementById('codTipoDisc').value
                            + "&grado=" + document.getElementById('grado').value
                            + "&colectivo=" + document.getElementById('codColectivo').value
                            + "&tipoContrato=" + document.getElementById('codTipoContrato').value
                            + "&jornada=" + document.getElementById('jornada').value
                            + "&fecInicio=" + document.getElementById('fecInicio').value
                            + "&tipoEdadSexo=" + document.getElementById('codTipoEdadSexo').value
                            + "&fecFin=" + document.getElementById('fecFin').value
                            + "&dias=" + document.getElementById('dias').value
                            + "&edad=" + document.getElementById('edad').value
                            + "&empresa=" + document.getElementById('empresa').value
                            + "&nifEmpresa=" + document.getElementById('nifEmpresa').value
                            + "&cnae=" + document.getElementById('codCnae').value
                            + "&nifPreparador=" + document.getElementById('nifPreparador').value
                            + "&importeInser=" + document.getElementById('importeInser').value;
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
                        //elementoVisible('off', 'barraProgresoECA');
                        mostrarError();
                    }
                } else {
                    jsp_alerta("A", mensajeValidacion);
                }
            }

            // FUNCIONES QUE PROCESAN RESPUESTA O ERROR DE LA PETICION AJAX
            function procesarRespuestaAltaModificacion(ajaxResult) {
                //console.log("procesarRespuestaAltaModificacion ajaxResult=" + ajaxResult.toString());
                //elementoVisible('off', 'barraProgresoECA');
                var datos = JSON.parse(ajaxResult);
                var codigoOperacion = datos.tabla.codigoOperacion;
                if (codigoOperacion == "0") {
                    var inserciones = datos.tabla.lista;
                    if (inserciones.length > 0) {
                        var respuesta = new Array();
                        respuesta[0] = "0";
                        respuesta[1] = inserciones;
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
                //elementoVisible('off', 'barraProgresoECA');
                console.log("mostrarError codigo=" + codigo);
                if (codigo == "1") {
                    jsp_alerta("A", '<%=meLanbide35I18n.getMensaje(idiomaUsuario,"error.validacion.errorBD")%>');
                } else if (codigo == "2") {
                    jsp_alerta("A", '<%=meLanbide35I18n.getMensaje(idiomaUsuario,"error.validacion.errorGen")%>');
                } else if (codigo == "3") {
                    jsp_alerta("A", '<%=meLanbide35I18n.getMensaje(idiomaUsuario,"error.validacion.pasoParametros")%>');
                } else if (codigo == "4") {
                    jsp_alerta("A", '<%=meLanbide35I18n.getMensaje(idiomaUsuario,"error.validacion.listaVacia")%>');
                } else if (codigo == "5") {
                    jsp_alerta("A", '<%=meLanbide35I18n.getMensaje(idiomaUsuario,"error.validacion.errorObtenerDatos")%>');
                } else if (codigo == "6") {
                    jsp_alerta("A", '<%=meLanbide35I18n.getMensaje(idiomaUsuario,"error.validacion.errorBorrarDatos")%>');
                } else if (codigo == "7") {
                    jsp_alerta("A", '<%=meLanbide35I18n.getMensaje(idiomaUsuario,"error.validacion.errorInsertarDatos")%>');
                } else if (codigo == "8") {
                    jsp_alerta("A", '<%=meLanbide35I18n.getMensaje(idiomaUsuario,"error.validacion.errorActualizarDatos")%>');
                } else if (codigo == "-1") {
                    jsp_alerta("A", '<%=meLanbide35I18n.getMensaje(idiomaUsuario,"error.validacion.operacionIncorrecta")%>');
                } else {
                    jsp_alerta("A", '<%=meLanbide35I18n.getMensaje(idiomaUsuario,"error.validacion.errorGen")%>');
                }
            }

            function calcularEdad() {
                console.log("calcularEdad");
                if (comprobarFechaNac(document.getElementById('fecNacimiento'))){
                    var arrayFechaNac = document.getElementById('fecNacimiento').value.split("/");
                    var stringUTCFecNac = arrayFechaNac[2] + "/" + arrayFechaNac[1] + "/" + arrayFechaNac[0];
                    let fecNac = Date.parse(stringUTCFecNac);
                    let fecActual = new Date();
                    let dif = new Date(fecActual - fecNac);
                    const minute = 1000 * 60;
                    const hour = minute * 60;
                    const day = hour * 24;
                    const year = day * 365;

                    document.getElementById('edad').value = Math.round(dif / year);

                    //calcularAnyos(fechaNac, txtOutput, text);
                } else {
                    document.getElementById('edad').value = '';
                }
            }

            function comprobarFechaNac(inputFecha) {
                console.log("comprobarFechaNac " + inputFecha.value);
                var formato = 'dd/mm/yyyy';
                if (Trim(inputFecha.value) != '') {
                    var D = ValidarFechaConFormatoEca(inputFecha.value, formato);
                    if (!D[0]) {
                        jsp_alerta("A", "<%=meLanbide35I18n.getMensaje(idiomaUsuario, "msg.fechaNoVal")%>");
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
                                document.getElementById(inputFecha.name).value = '';
                                jsp_alerta("A", "<%=meLanbide35I18n.getMensaje(idiomaUsuario, "msg.fechaNacPosterior")%>");
                                return false;
                            }
                        }
                    }//if (!D[0])
                }//if (Trim(inputFecha.value)!='')
                return true;
            }//comprobarFechaNac

            function ponerValorDesplegables() {
                if (<%=nuevo%> != null && <%=nuevo%> == 0) {
                    document.getElementById("codSexo").value = '<%=sexo%>';
                    document.getElementById("codTipoDisc").value = '<%=tipoDisc%>';
                    document.getElementById("codColectivo").value = '<%=colectivo%>';
                    document.getElementById("codTipoContrato").value = '<%=tipoContrato%>';
                    document.getElementById("codTipoEdadSexo").value = '<%=tipoEdadSexo%>';
                    document.getElementById("codCnae").value = '<%=cnae%>';
                    document.getElementById("anchorSexo").click();
                    document.getElementById("anchorSexo").click();
                    document.getElementById("anchorTipoDisc").click();
                    document.getElementById("anchorTipoDisc").click();
                    document.getElementById("anchorColectivo").click();
                    document.getElementById("anchorColectivo").click();
                    document.getElementById("anchorTipoContrato").click();
                    document.getElementById("anchorTipoContrato").click();
                    document.getElementById("anchorTipoEdadSexo").click();
                    document.getElementById("anchorTipoEdadSexo").click();
                    document.getElementById("anchorCnae").click();
                    document.getElementById("anchorCnae").click();
                }
            }
            var idiomaUsuario = <%=idiomaUsuario%>;
        </script>
    </head>
    <body class="bandaBody" onload="javascript:{
            //elementoVisible('off', 'barraProgresoECA');
            ponerValorDesplegables();
            }" >
        <input type="hidden" id="idiomaUsuario" value=""/>
		  <script>
			document.getElementById("idiomaUsuario").value = <%=idiomaUsuario%>;
          </script>
        <input type="hidden" id="listaSexo" value=""/>
            <script>document.getElementById("listaSexo").value = JSON.stringify(<%=listSexoGA%>, function (key, value) {
                return value == null ? "" : value;
        });
            </script>
        <input type="hidden" id="listaTipoDisc" value=""/>
            <script>document.getElementById("listaTipoDisc").value = JSON.stringify(<%=listTipoDiscGA%>, function (key, value) {
                return value == null ? "" : value;
        });
            </script>
        <input type="hidden" id="listaColectivo" value=""/>
            <script>document.getElementById("listaColectivo").value = JSON.stringify(<%=listColectivoGA%>, function (key, value) {
                return value == null ? "" : value;
        });
            </script>
        <input type="hidden" id="listaTipoContrato" value=""/>
            <script>document.getElementById("listaTipoContrato").value = JSON.stringify(<%=listTipoContratoGA%>, function (key, value) {
                return value == null ? "" : value;
        });
            </script>
        <input type="hidden" id="listaTipoEdadSexo" value=""/>
            <script>document.getElementById("listaTipoEdadSexo").value = JSON.stringify(<%=listTipoEdadSexoGA%>, function (key, value) {
                return value == null ? "" : value;
        });
            </script>
        <input type="hidden" id="listaCnae" value=""/>
            <script>document.getElementById("listaCnae").value = JSON.stringify(<%=listCnaeGA%>, function (key, value) {
                return value == null ? "" : value;
        });
            </script>
        <div class="contenidoPantalla">

            <form>
                <div style="width: 100%; padding: 10px; text-align: left;">
                    <div  class="sub3titulo" style="clear: both; text-align: center; font-size: 14px;">
                        <span id="tituloVentana" style="width: 98%;">
                            <%=nuevo != null && nuevo=="1" ? meLanbide35I18n.getMensaje(idiomaUsuario,"label.validacion.inserciones.alta"):meLanbide35I18n.getMensaje(idiomaUsuario,"label.validacion.inserciones.modificar")%>
                        </span>
                    </div>
                    <fieldset id="insercion" name="insercion">
                        <legend class="legendAzul" id="titInsercion"><%=meLanbide35I18n.getMensaje(idiomaUsuario,"label.validacion.insercion")%></legend>
                        <div class="lineaFormulario">
                            <div class="etiquetaEca">
                                <%=meLanbide35I18n.getMensaje(idiomaUsuario,"label.validacion23.mantenimiento.dni")%>
                            </div>
                            <div style="width:350px; float: left;">
                                <input id="dni" name="dni" type="text" class="inputTextoObligatorio" size="30" maxlength="10"
                                       value="<%=dni%>" />
                            </div>
                            <div class="etiquetaEca">
                                <%=meLanbide35I18n.getMensaje(idiomaUsuario,"label.validacion23.mantenimiento.nombre")%>
                            </div>
                            <div style="width:350px; float: left;">
                                <input id="nombre" name="nombre" type="text" class="inputTextoObligatorio" size="30" maxlength="30"
                                       value="<%=nombre%>" />
                            </div>
                            <br><br>
							<div class="etiquetaEca">
                                <%=meLanbide35I18n.getMensaje(idiomaUsuario,"label.validacion23.mantenimiento.apellido1")%>
                            </div>
                            <div style="width:350px; float: left;">
                                <input id="apellido1" name="apellido1" type="text" class="inputTextoObligatorio" size="30" maxlength="50"
                                       value="<%=apellido1%>" />
                            </div>
							<div class="etiquetaEca">
                                <%=meLanbide35I18n.getMensaje(idiomaUsuario,"label.validacion23.mantenimiento.apellido2")%>
                            </div>
                            <div style="width:350px; float: left;">
                                <input id="apellido2" name="apellido2" type="text" class="inputTextoObligatorio" size="50" maxlength="50"
                                       value="<%=apellido2%>" />
                            </div>
                            <br><br>
                            <div class="etiquetaEca">
                                <%=meLanbide35I18n.getMensaje(idiomaUsuario,"label.validacion23.mantenimiento.sexo")%>
                            </div>
                            <div style="width:350px; float: left;">
                                <input type="text" name="codSexo" id="codSexo" size="3" class="inputComboObligatorio" value="" onkeyup="xAMayusculas(this);" maxlength="2" disabled />
                                <input type="text" name="descSexo"  id="descSexo" size="30" class="inputComboObligatorio" readonly value="" />
                                <a href="" id="anchorSexo" name="anchorSexo">
                                    <span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonSexo"
                                          style="cursor:hand;"></span>
                                </a>
                            </div>
                            <div class="etiquetaEca">
                                <%=meLanbide35I18n.getMensaje(idiomaUsuario,"label.validacion23.mantenimiento.fNacimiento")%>
                            </div>
                            <div style="width:350px; float: left;">
                                <input type="text" class="inputTxtFecha"
                                       id="fecNacimiento" name="fecNacimiento"
                                       maxlength="10"  size="10"
                                       value="<%=fecNacimiento%>"
                                       onkeyup = "return SoloCaracteresFecha(this);"
                                       onblur = "calcularEdad();"
                                       onfocus="javascript:this.select();" disabled />
                                <A href="javascript:calClick(event);return false;" onClick="mostrarCalFechaNacimiento(event);
                                        return false;" style="text-decoration: none;">
                                    <IMG style="border: 0" height="17" id="calFechaNacimiento" border="0"
                                         src="<c:url value='/images/calendario/icono.gif'/>" >
                                </A>
                            </div>
                            <br><br>
							<div class="etiquetaEca">
                                <%=meLanbide35I18n.getMensaje(idiomaUsuario,"label.validacion23.mantenimiento.tipoDisc")%>
                            </div>
                            <div style="width:350px; float: left;">
                                <input type="text" name="codTipoDisc" id="codTipoDisc" size="3" class="inputComboObligatorio" value="" onkeyup="xAMayusculas(this);" maxlength="4" disabled />
                                <input type="text" name="descTipoDisc"  id="descTipoDisc" size="30" class="inputComboObligatorio" readonly value="" />
                                <a href="" id="anchorTipoDisc" name="anchorTipoDisc">
                                    <span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonTipoDisc"
                                          style="cursor:hand;"></span>
                                </a>
                            </div>
                            <div  class="etiquetaEca">
                                <%=meLanbide35I18n.getMensaje(idiomaUsuario,"label.validacion23.mantenimiento.grado")%>
                            </div>
                            <div style="width:350px; float: left;">
                                <input id="grado" name="grado" type="text" class="inputTextoObligatorio" size="30" maxlength="150"
                                       value="<%=grado%>" />
                            </div>
                            <br><br>
                            <div  class="etiquetaEca">
                                <%=meLanbide35I18n.getMensaje(idiomaUsuario,"label.validacion23.mantenimiento.colectivo")%>
                            </div>
                            <div style="width:350px; float: left;">
                                <input type="text" name="codColectivo" id="codColectivo" size="3" class="inputComboObligatorio" value="" onkeyup="xAMayusculas(this);" maxlength="1" disabled />
                                <input type="text" name="descColectivo"  id="descColectivo" size="30" class="inputComboObligatorio" readonly value="" />
                                <a href="" id="anchorColectivo" name="anchorColectivo">
                                    <span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonColectivo"
                                          style="cursor:hand;"></span>
                                </a>
                            </div>
                            <div  class="etiquetaEca">
                                <%=meLanbide35I18n.getMensaje(idiomaUsuario,"label.validacion23.mantenimiento.tipoContrato")%>
                            </div>
                            <div style="width:350px; float: left;">
                                <input type="text" name="codTipoContrato" id="codTipoContrato" size="3" class="inputComboObligatorio" value="" onkeyup="xAMayusculas(this);" maxlength="1" disabled />
                                <input type="text" name="descTipoContrato"  id="descTipoContrato" size="30" class="inputComboObligatorio" readonly value="" />
                                <a href="" id="anchorTipoContrato" name="anchorTipoContrato">
                                    <span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonTipoContrato"
                                          style="cursor:hand;"></span>
                                </a>
                            </div>
                            <br><br>
                            <div class="etiquetaEca">
                                <%=meLanbide35I18n.getMensaje(idiomaUsuario,"label.validacion23.mantenimiento.jornada")%>
                            </div>
                            <div style="width:350px; float: left;">
                                <input id="jornada" name="jornada" type="text" class="inputTextoObligatorio" size="30" maxlength="150"
                                           value="<%=jornada%>" />
                            </div>
                            <div class="etiquetaEca">
                                <%=meLanbide35I18n.getMensaje(idiomaUsuario,"label.validacion23.mantenimiento.fInicio")%>
                            </div>
                            <div style="width:350px; float: left;">
                                <input type="text" class="inputTxtFecha"
                                           id="fecInicio" name="fecInicio"
                                           maxlength="10"  size="10"
                                           value="<%=fecInicio%>"
                                           onkeyup = "return SoloCaracteresFecha(this);"
                                           onblur = "javascript:return comprobarFecha(this);"
                                           onfocus="javascript:this.select();" disabled />
                                <A href="javascript:calClick(event);return false;" onClick="mostrarCalFechaInicio(event);
                                            return false;" style="text-decoration: none;">
                                    <IMG style="border: 0px solid" height="17" id="calFechaInicio"  border="0"
                                             src="<c:url value='/images/calendario/icono.gif'/>" >
                                 </A>
                            </div>
                            <br><br>
                            <div class="etiquetaEca">
                                <%=meLanbide35I18n.getMensaje(idiomaUsuario,"label.validacion23.mantenimiento.tipoEdadSexo")%>
                            </div>
                            <div style="width:350px; float: left;">
                                <input type="text" name="codTipoEdadSexo" id="codTipoEdadSexo" size="3" class="inputComboObligatorio" value="" onkeyup="xAMayusculas(this);" maxlength="3" disabled />
                                <input type="text" name="descTipoEdadSexo"  id="descTipoEdadSexo" size="30" class="inputComboObligatorio" readonly value="" />
                                <a href="" id="anchorTipoEdadSexo" name="anchorTipoEdadSexo">
                                        <span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonTipoEdadSexo"
                                              style="cursor:hand;"></span>
                                </a>
                            </div>
                            <div class="etiquetaEca">
                                <%=meLanbide35I18n.getMensaje(idiomaUsuario,"label.validacion23.mantenimiento.fFin")%>
                            </div>
                            <div style="width:350px; float: left;">
                                <input type="text" class="inputTxtFecha"
                                           id="fecFin" name="fecFin"
                                           maxlength="10"  size="10"
                                           value="<%=fecFin%>"
                                           onkeyup = "return SoloCaracteresFecha(this);"
                                           onblur = "javascript:return comprobarFecha(this);"
                                           onfocus="javascript:this.select();" disabled />
                                <A href="javascript:calClick(event);return false;" onClick="mostrarCalFechaFin(event);
                                            return false;" style="text-decoration: none;">
                                    <IMG style="border: 0px solid" height="17" id="calFechaFin"  border="0"
                                             src="<c:url value='/images/calendario/icono.gif'/>" >
                                </A>
                            </div>
							<br><br>
							<div class="etiquetaEca">
                                <%=meLanbide35I18n.getMensaje(idiomaUsuario,"label.validacion23.mantenimiento.dias")%>
                            </div>
                            <div style="width:350px; float: left;">
                                <input id="dias" name="dias" type="text" class="inputTextoObligatorio" size="30" maxlength="10"
                                       value="<%=dias%>" />
                            </div>
                            <div class="etiquetaEca">
                                <%=meLanbide35I18n.getMensaje(idiomaUsuario,"label.validacion23.mantenimiento.edad")%>
                            </div>
                            <div style="width:350px; float: left;">
                                <input id="edad" name="edad" type="text" class="inputTextoObligatorio" size="30" maxlength="30"
                                       value="<%=edad%>" />
                            </div>
							<br><br>
                            <div class="etiquetaEca">
                                <%=meLanbide35I18n.getMensaje(idiomaUsuario,"label.validacion23.mantenimiento.empresa")%>
                            </div>
                            <div style="width:350px; float: left;">
                                <input id="empresa" name="empresa" type="text" class="inputTextoObligatorio" size="30" maxlength="10"
                                       value="<%=empresa%>" />
                            </div>
                            <div class="etiquetaEca">
                                <%=meLanbide35I18n.getMensaje(idiomaUsuario,"label.validacion23.mantenimiento.nifEmpresa")%>
                            </div>
                            <div style="width:350px; float: left;">
                                <input id="nifEmpresa" name="nifEmpresa" type="text" class="inputTextoObligatorio" size="30" maxlength="30"
                                       value="<%=nifEmpresa%>" />
                            </div>
                            <br><br>
                            <div class="etiquetaEca">
                                <%=meLanbide35I18n.getMensaje(idiomaUsuario,"label.validacion23.mantenimiento.cnae")%>
                            </div>
                            <div style="width:350px; float: left;">
                                <input type="text" name="codCnae" id="codCnae" size="4" class="inputComboObligatorio" value="" onkeyup="xAMayusculas(this);" maxlength="4" disabled />
                                <input type="text" name="descCnae"  id="descCnae" size="30" class="inputComboObligatorio" readonly value="" />
                                <a href="" id="anchorCnae" name="anchorCnae">
                                        <span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonCnae"
                                              style="cursor:hand;"></span>
                                </a>
                            </div>
                            <div class="etiquetaEca">
                                <%=meLanbide35I18n.getMensaje(idiomaUsuario,"label.validacion23.mantenimiento.nifPreparador")%>
                            </div>
                            <div style="width:350px; float: left;">
                                <input id="nifPreparador" name="nifPreparador" type="text" class="inputTextoObligatorio" size="30" maxlength="30"
                                       value="<%=nifPreparador%>" />
                            </div>
                            <br><br>
                            <div class="etiquetaEca">
                                <%=meLanbide35I18n.getMensaje(idiomaUsuario,"label.validacion23.mantenimiento.importeInser")%>
                            </div>
                            <div style="width:350px; float: left;">
                                <input id="importeInser" name="importeInser" type="text" class="inputTextoObligatorio" size="30" maxlength="30"
                                       value="<%=importeInser%>" />
                            </div>
                        </div>
                    </fieldset>

                    <div class="lineaFormulario" style="margin-top: 25px;">
                        <div class="botonera" style="width: 100%; float: left; text-align: center;">
                            <input type="button" id="btnAceptar" name="btnAceptar" class="botonGeneral" value="<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.validacion23.btnAceptar")%>" onclick="guardarDatos();"/>
                            <input type="button" id="btnCancelar" name="btnCancelar" class="botonGeneral" value="<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.validacion23.btnCancelar")%>" onclick="cancelar();"/>
                        </div>
                    </div>
                </div>
            </form>

            <div id="popupcalendar" class="text"></div>
        </div>
    </body>
</html>
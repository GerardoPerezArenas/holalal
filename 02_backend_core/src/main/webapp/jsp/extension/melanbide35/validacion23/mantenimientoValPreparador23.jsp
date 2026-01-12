<%@taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide35.i18n.MeLanbide35I18n" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide35.vo.justificacion.JusPreparadoresECA23VO" %>
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
            JusPreparadoresECA23VO datModif = new JusPreparadoresECA23VO();

            String codOrganizacion = "";
            String nuevo = "";
			String id = "";
            String numExpediente = "";
            String nifPreparador = "";
            String jornada = "";
            String permitidos = "";
            String importePrep = "";

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
                    datModif = (JusPreparadoresECA23VO)request.getAttribute("datModif");
					id = datModif.getId() != null ? datModif.getId().toString() : "";
                    nifPreparador = datModif.getNifPreparador();
					jornada = datModif.getJornada() != null ? datModif.getJornada().toString() : "";
					permitidos = datModif.getPermitidos() != null ? datModif.getPermitidos().toString() : "";
					importePrep = datModif.getImportePrep() != null ? datModif.getImportePrep().toString() : "";
                }
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
        <script type="text/javascript" src="<c:url value='/scripts/listaComboBox.js'/>"></script>
        <script type="text/javascript" src="<c:url value='/scripts/calendario.js'/>"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/validaciones.js"></script>
        <script type="text/javascript">
            var APP_CONTEXT_PATH = '<%=request.getContextPath()%>';
            var url = APP_CONTEXT_PATH + '/PeticionModuloIntegracion.do';
            var mensajeValidacion = '';


			function validarDatos() {
                var campo = document.getElementById("nifPreparador");
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

                campo = document.getElementById('jornada').value;
                if (campo == null || campo == '') {
                    mensajeValidacion = '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "msg.jornadaVacio")%>';
                    return false;
                } else if (!validarNumericoDecimalPrecision(campo, 6, 2)) {
                    mensajeValidacion = '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "msg.jornada.errNumerico")%>';
                    return false;
                }

                campo = document.getElementById('permitidos').value;
                if (campo != null && campo != '' && Number.isInteger(campo)) {
                    mensajeValidacion = '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "msg.permitidosVacio.errNumerico")%>';
                    return false;
                }

                campo = document.getElementById('importePrep').value;
                if (campo == null || campo == '') {
                    mensajeValidacion = '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "msg.importePreparadorVacio")%>';
                    return false;
                } else if (!validarNumericoDecimalPrecision(campo, 10, 2)) {
                    mensajeValidacion = '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "msg.importePreparadorVacio.errNumerico")%>';
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
                        parametros = "tarea=preparar&modulo=MELANBIDE35&operacion=guardarValPreparador23&tipo=0";
                    } else {
                        parametros = "tarea=preparar&modulo=MELANBIDE35&operacion=modificarValPreparador23&tipo=0"
                                + "&id=<%=datModif != null && datModif.getId() != null ? datModif.getId().toString() : ""%>";
                    }
                    parametros += '&numExp=<%=numExpediente%>'
                            + "&nifPreparador=" + document.getElementById('nifPreparador').value
                            + "&jornada=" + document.getElementById('jornada').value
                            + "&permitidos=" + document.getElementById('permitidos').value
                            + "&importePrep=" + document.getElementById('importePrep').value;
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
                    var preparadores = datos.tabla.lista;
                    if (preparadores.length > 0) {
                        var respuesta = new Array();
                        respuesta[0] = "0";
                        respuesta[1] = preparadores;
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
        </script>
    </head>
    <body class="bandaBody" onload="javascript:{
            //elementoVisible('off', 'barraProgresoECA');
            }" >
        <div class="contenidoPantalla">
            <form>
                <div style="width: 100%; padding: 10px; text-align: left;">
                    <div  class="sub3titulo" style="clear: both; text-align: center; font-size: 14px;">
                        <span id="tituloVentana" style="width: 98%;">
                            <%=nuevo != null && nuevo=="1" ? meLanbide35I18n.getMensaje(idiomaUsuario,"label.validacion.preparadores.alta") : meLanbide35I18n.getMensaje(idiomaUsuario,"label.validacion.preparadores.modificar")%>
                        </span>
                    </div>
                    <fieldset id="insercion" name="insercion">
                        <legend class="legendAzul" id="titInsercion"></legend>
                        <div class="lineaFormulario">
                            <div class="etiquetaEca">
                                <%=meLanbide35I18n.getMensaje(idiomaUsuario,"label.validacion23.mantenimiento.nifPreparador")%>
                            </div>
                            <div style="width:350px; float: left;">
                                <input id="nifPreparador" name="nifPreparador" type="text" class="inputTextoObligatorio" size="30" maxlength="30"
                                       value="<%=nifPreparador%>" />
                            </div>
                            <div class="etiquetaEca">
                                <%=meLanbide35I18n.getMensaje(idiomaUsuario,"label.validacion23.mantenimiento.jornada")%>
                            </div>
                            <div style="width:350px; float: left;">
                                <input id="jornada" name="jornada" type="text" class="inputTextoObligatorio" size="30" maxlength="9"
                                           value="<%=jornada%>" />
                            </div>
                            <br><br>
                            <div class="etiquetaEca">
                                <%=meLanbide35I18n.getMensaje(idiomaUsuario,"label.validacion23.mantenimiento.permitidos")%>
                            </div>
                            <div style="width:350px; float: left;">
                                <input id="permitidos" name="permitidos" type="text" class="inputTextoObligatorio" size="30" maxlength="2"
                                           value="<%=permitidos%>" />
                            </div>
                            <div class="etiquetaEca">
                                <%=meLanbide35I18n.getMensaje(idiomaUsuario,"label.validacion23.mantenimiento.importePreparador")%>
                            </div>
                            <div style="width:350px; float: left;">
                                <input id="importePrep" name="importePrep" type="text" class="inputTextoObligatorio" size="30" maxlength="30"
                                       value="<%=importePrep%>" />
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

        </div>
    </body>
</html>
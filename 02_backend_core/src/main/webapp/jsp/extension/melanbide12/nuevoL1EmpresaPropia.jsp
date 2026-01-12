<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide12.i18n.MeLanbide12I18n" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide12.vo.FilaL1EmpresaPropiaVO" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide12.vo.DesplegableAdmonLocalVO" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide12.util.ConstantesMeLanbide12" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide12.util.ConfigurationParameter" %>
<%@page import="java.util.ArrayList" %>
<%@page import="java.util.List" %>
<html>
    <head>
        <meta http-equiv="X-UA-Compatible" content="IE=Edge" />
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <%
            FilaL1EmpresaPropiaVO datModif = new FilaL1EmpresaPropiaVO();

            String codOrganizacion = "";
            String nuevo = "";
            String numExpediente = "";

            MeLanbide12I18n meLanbide12I18n = MeLanbide12I18n.getInstance();

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
                    datModif = (FilaL1EmpresaPropiaVO)request.getAttribute("datModif");
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
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/extension/melanbide12/melanbide12.css"/>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/JavaScriptUtil.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/general.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/melanbide12/PractUtils.js"></script>
        <script type="text/javascript" src="<c:url value='/scripts/listaComboBox.js'/>"></script>
        <script type="text/javascript" src="<c:url value='/scripts/calendario.js'/>"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/validaciones.js"></script>
        <script type="text/javascript">
            var APP_CONTEXT_PATH = '<%=request.getContextPath()%>';
            var url = APP_CONTEXT_PATH + '/PeticionModuloIntegracion.do';
            var mensajeValidacion = '';

            function guardarDatos() {
                if (validarDatos()) {
                    elementoVisible('on', 'barraProgresoPRACT');
                    var parametros = "";

                    if ('<%=nuevo%>' != null && '<%=nuevo%>' == "1") {
                        parametros = "tarea=preparar&modulo=MELANBIDE12&operacion=crearNuevoL1EmpresaPropia&tipo=0";
                    } else {
                        parametros = "tarea=preparar&modulo=MELANBIDE12&operacion=modificarL1EmpresaPropia&tipo=0"
                                + "&id=<%=datModif != null && datModif.getId() != null ? datModif.getId().toString() : ""%>";
                    }
                    parametros += '&numExp=<%=numExpediente%>'
                            + '&dni=' + document.getElementById('dni').value
                            + '&nombre=' + document.getElementById('nombre').value
                            + '&ape1=' + document.getElementById('ape1').value
                            + '&ape2=' + document.getElementById('ape2').value
                            + '&retr_anual_bruta=' + document.getElementById('retr_anual_bruta').value
                            + '&cc_cot_ss=' + document.getElementById('cc_cot_ss').value
                            + '&horas_lab_anual=' + document.getElementById('horas_lab_anual').value
                            + '&horas_imput=' + document.getElementById('horas_imput').value
                            + '&imp_gest=' + document.getElementById('imp_gest').value
                            + '&person_pract=' + document.getElementById('person_pract').value
                            + '&imp_solic=' + document.getElementById('imp_solic').value
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
                        elementoVisible('off', 'barraProgresoPRACT');
                        mostrarError();
                    }
                } else {
                    jsp_alerta("A", mensajeValidacion);
                }
            }

            function validarDatos() {
                mensajeValidacion = "";
                // Obligatorios
                campo = document.getElementById('dni').value;
                if (campo == null || campo == '') {
                    mensajeValidacion = '<%=meLanbide12I18n.getMensaje(idiomaUsuario, "msg.dni")%>';
                    return false;
                }
                campo = document.getElementById('nombre').value;
                if (campo == null || campo == '') {
                    mensajeValidacion = '<%=meLanbide12I18n.getMensaje(idiomaUsuario, "msg.nombre")%>';
                    return false;
                }
                campo = document.getElementById('ape1').value;
                if (campo == null || campo == '') {
                    mensajeValidacion = '<%=meLanbide12I18n.getMensaje(idiomaUsuario, "msg.ape1")%>';
                    return false;
                }
                campo = document.getElementById('retr_anual_bruta').value;
                if (campo == null || campo == '') {
                    mensajeValidacion = '<%=meLanbide12I18n.getMensaje(idiomaUsuario, "msg.retr_anual_bruta")%>';
                    return false;
                } else if (!validarNumericoDecimalPrecision(campo, 8, 2)) {
                    mensajeValidacion = '<%=meLanbide12I18n.getMensaje(idiomaUsuario, "msg.importe.errNumerico")%>';
                    return false;
                }
                campo = document.getElementById('cc_cot_ss').value;
                if (campo == null || campo == '') {
                    mensajeValidacion = '<%=meLanbide12I18n.getMensaje(idiomaUsuario, "msg.cc_cot_ss")%>';
                    return false;
                } else if (!validarNumericoDecimalPrecision(campo, 8, 2)) {
                    mensajeValidacion = '<%=meLanbide12I18n.getMensaje(idiomaUsuario, "msg.importe.errNumerico")%>';
                    return false;
                }
                campo = document.getElementById('horas_lab_anual').value;
                if (campo == null || campo == '') {
                    mensajeValidacion = '<%=meLanbide12I18n.getMensaje(idiomaUsuario, "msg.horas_lab_anual")%>';
                    return false;
                } else if (!validarNumericoDecimalPrecision(campo, 8, 2)) {
                    mensajeValidacion = '<%=meLanbide12I18n.getMensaje(idiomaUsuario, "msg.importe.errNumerico")%>';
                    return false;
                }
                campo = document.getElementById('horas_imput').value;
                if (campo == null || campo == '') {
                    mensajeValidacion = '<%=meLanbide12I18n.getMensaje(idiomaUsuario, "msg.horas_imput")%>';
                    return false;
                } else if (!validarNumericoDecimalPrecision(campo, 8, 2)) {
                    mensajeValidacion = '<%=meLanbide12I18n.getMensaje(idiomaUsuario, "msg.importe.errNumerico")%>';
                    return false;
                }
                campo = document.getElementById('imp_gest').value;
                if (campo == null || campo == '') {
                    mensajeValidacion = '<%=meLanbide12I18n.getMensaje(idiomaUsuario, "msg.imp_gest")%>';
                    return false;
                } else if (!validarNumericoDecimalPrecision(campo, 8, 2)) {
                    mensajeValidacion = '<%=meLanbide12I18n.getMensaje(idiomaUsuario, "msg.importe.errNumerico")%>';
                    return false;
                }
                campo = document.getElementById('person_pract').value;
                if (campo == null || campo == '') {
                    mensajeValidacion = '<%=meLanbide12I18n.getMensaje(idiomaUsuario, "msg.person_pract")%>';
                    return false;
                } else if (!validarNumericoDecimalPrecision(campo, 8, 2)) {
                    mensajeValidacion = '<%=meLanbide12I18n.getMensaje(idiomaUsuario, "msg.importe.errNumerico")%>';
                    return false;
                }
                campo = document.getElementById('imp_solic').value;
                if (campo == null || campo == '') {
                    mensajeValidacion = '<%=meLanbide12I18n.getMensaje(idiomaUsuario, "msg.imp_solic")%>';
                    return false;
                } else if (!validarNumericoDecimalPrecision(campo, 8, 2)) {
                    mensajeValidacion = '<%=meLanbide12I18n.getMensaje(idiomaUsuario, "msg.importe.errNumerico")%>';
                    return false;
                }

                return true;
            }

            function cancelar() {
                var resultado = jsp_alerta('', '<%=meLanbide12I18n.getMensaje(idiomaUsuario, "msg.preguntaCancelar")%>');
                if (resultado == 1) {
                    cerrarVentana();
                }
            }

            function procesarRespuestaAltaModificacion(ajaxResult) {
                elementoVisible('off', 'barraProgresoPRACT');
                var datos = JSON.parse(ajaxResult);
                var codigoOperacion = datos.tabla.codigoOperacion;
                if (codigoOperacion == "0") {
                    var result = datos.tabla.lista;
                    if (result.length > 0) {
                        var respuesta = new Array();
                        respuesta[0] = "0";
                        respuesta[1] = result;
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
                elementoVisible('off', 'barraProgresoPRACT');
                if (codigo == "1") {
                    jsp_alerta("A", '<%=meLanbide12I18n.getMensaje(idiomaUsuario,"error.errorBD")%>');
                } else if (codigo == "2") {
                    jsp_alerta("A", '<%=meLanbide12I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                } else if (codigo == "3") {
                    jsp_alerta("A", '<%=meLanbide12I18n.getMensaje(idiomaUsuario,"error.pasoParametros")%>');
                } else if (codigo == "4") {
                    jsp_alerta("A", '<%=meLanbide12I18n.getMensaje(idiomaUsuario,"error.listaVacia")%>');
                } else if (codigo == "5") {
                    jsp_alerta("A", '<%=meLanbide12I18n.getMensaje(idiomaUsuario,"error.errorObtenerDatos")%>');
                } else if (codigo == "6") {
                    jsp_alerta("A", '<%=meLanbide12I18n.getMensaje(idiomaUsuario,"error.errorBorrarDatos")%>');
                } else if (codigo == "7") {
                    jsp_alerta("A", '<%=meLanbide12I18n.getMensaje(idiomaUsuario,"error.errorInsertarDatos")%>');
                } else if (codigo == "8") {
                    jsp_alerta("A", '<%=meLanbide12I18n.getMensaje(idiomaUsuario,"error.errorActualizarDatos")%>');
                } else if (codigo == "-1") {
                    jsp_alerta("A", '<%=meLanbide12I18n.getMensaje(idiomaUsuario,"error.operacionIncorrecta")%>');
                } else {
                    jsp_alerta("A", '<%=meLanbide12I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                }
            }

        </script>
    </head>
    <body class="bandaBody" onload="javascript:{
                elementoVisible('off', 'barraProgresoPRACT');
            }" >
        <div class="contenidoPantalla">
            <div id="barraProgresoPRACT" style="visibility: hidden;">
                <div class="contenedorHidepage">
                    <div class="textoHide">
                        <span>
                            <%=meLanbide12I18n.getMensaje(idiomaUsuario, "msg.procesando")%>
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
                        <span>
                            <%=meLanbide12I18n.getMensaje(idiomaUsuario,"label.nuevoL1EmpresaPropia")%>
                        </span>
                    </div>

                    <div class="lineaFormulario">
                        <div style="width: 250px; float: left;" class="etiqueta">
                            <%=meLanbide12I18n.getMensaje(idiomaUsuario,"label.dni")%>
                        </div>
                        <div>
                            <div style="float: left;">
                                <input id="dni" name="dni" type="text" class="inputTexto" size="100" maxlength="100"
                                       value="<%=datModif != null && datModif.getDni() != null ? datModif.getDni() : ""%>"/>
                            </div>
                        </div>
                    </div>

                    <br><br>
                    <div class="lineaFormulario">
                        <div style="width: 250px; float: left;" class="etiqueta">
                            <%=meLanbide12I18n.getMensaje(idiomaUsuario,"label.nombre")%>
                        </div>
                        <div>
                            <div style="float: left;">
                                <input id="nombre" name="nombre" type="text" class="inputTexto" size="100" maxlength="50"
                                       value="<%=datModif != null && datModif.getNombre() != null ? datModif.getNombre() : ""%>"/>
                            </div>
                        </div>
                    </div>

                    <br><br>
                    <div class="lineaFormulario">
                        <div style="width: 250px; float: left;" class="etiqueta">
                            <%=meLanbide12I18n.getMensaje(idiomaUsuario,"label.ape1")%>
                        </div>
                        <div>
                            <div style="float: left;">
                                <input id="ape1" name="ape1" type="text" class="inputTexto" size="100" maxlength="50"
                                       value="<%=datModif != null && datModif.getApe1() != null ? datModif.getApe1() : ""%>"/>
                            </div>
                        </div>
                    </div>

                    <br><br>
                    <div class="lineaFormulario">
                        <div style="width: 250px; float: left;" class="etiqueta">
                            <%=meLanbide12I18n.getMensaje(idiomaUsuario,"label.ape2")%>
                        </div>
                        <div>
                            <div style="float: left;">
                                <input id="ape2" name="ape2" type="text" class="inputTexto" size="100" maxlength="50"
                                       value="<%=datModif != null && datModif.getApe2() != null ? datModif.getApe2() : ""%>"/>
                            </div>
                        </div>
                    </div>

                    <br><br>
                    <div class="lineaFormulario">
                        <div style="width: 250px; float: left;" class="etiqueta">
                            <%=meLanbide12I18n.getMensaje(idiomaUsuario,"label.retr_anual_bruta")%>
                        </div>
                        <div>
                            <div style="float: left;">
                                <input id="retr_anual_bruta" name="retr_anual_bruta" type="text" class="inputTexto" size="25" maxlength="10"
                                       value="<%=datModif != null && datModif.getRetrAnualBruta() != null ? datModif.getRetrAnualBruta().toString().replaceAll("\\.", ","): ""%>" onchange="reemplazarPuntos(this);"/>
                            </div>
                        </div>
                    </div>

                    <br><br>
                    <div class="lineaFormulario">
                        <div style="width: 250px; float: left;" class="etiqueta">
                            <%=meLanbide12I18n.getMensaje(idiomaUsuario,"label.cc_cot_ss")%>
                        </div>
                        <div>
                            <div style="float: left;">
                                <input id="cc_cot_ss" name="cc_cot_ss" type="text" class="inputTexto" size="25" maxlength="10"
                                       value="<%=datModif != null && datModif.getCcCotSs() != null ? datModif.getCcCotSs().toString().replaceAll("\\.", ","): ""%>" onchange="reemplazarPuntos(this);"/>
                            </div>
                        </div>
                    </div>

                    <br><br>
                    <div class="lineaFormulario">
                        <div style="width: 250px; float: left;" class="etiqueta">
                            <%=meLanbide12I18n.getMensaje(idiomaUsuario,"label.horas_lab_anual")%>
                        </div>
                        <div>
                            <div style="float: left;">
                                <input id="horas_lab_anual" name="horas_lab_anual" type="text" class="inputTexto" size="25" maxlength="10"
                                       value="<%=datModif != null && datModif.getHorasLabAnual() != null ? datModif.getHorasLabAnual().toString().replaceAll("\\.", ","): ""%>" onchange="reemplazarPuntos(this);"/>
                            </div>
                        </div>
                    </div>

                    <br><br>
                    <div class="lineaFormulario">
                        <div style="width: 250px; float: left;" class="etiqueta">
                            <%=meLanbide12I18n.getMensaje(idiomaUsuario,"label.horas_imput")%>
                        </div>
                        <div>
                            <div style="float: left;">
                                <input id="horas_imput" name="horas_imput" type="text" class="inputTexto" size="25" maxlength="10"
                                       value="<%=datModif != null && datModif.getHorasImput() != null ? datModif.getHorasImput().toString().replaceAll("\\.", ","): ""%>" onchange="reemplazarPuntos(this);"/>
                            </div>
                        </div>
                    </div>

                    <br><br>
                    <div class="lineaFormulario">
                        <div style="width: 250px; float: left;" class="etiqueta">
                            <%=meLanbide12I18n.getMensaje(idiomaUsuario,"label.imp_gest")%>
                        </div>
                        <div>
                            <div style="float: left;">
                                <input id="imp_gest" name="imp_gest" type="text" class="inputTexto" size="25" maxlength="10"
                                       value="<%=datModif != null && datModif.getImpGest() != null ? datModif.getImpGest().toString().replaceAll("\\.", ","): ""%>" onchange="reemplazarPuntos(this);"/>
                            </div>
                        </div>
                    </div>

                    <br><br>
                    <div class="lineaFormulario">
                        <div style="width: 250px; float: left;" class="etiqueta">
                            <%=meLanbide12I18n.getMensaje(idiomaUsuario,"label.person_pract")%>
                        </div>
                        <div>
                            <div style="float: left;">
                                <input id="person_pract" name="person_pract" type="text" class="inputTexto" size="25" maxlength="10"
                                       value="<%=datModif != null && datModif.getPersonPract() != null ? datModif.getPersonPract().toString().replaceAll("\\.", ","): ""%>" onchange="reemplazarPuntos(this);"/>
                            </div>
                        </div>
                    </div>

                    <br><br>
                    <div class="lineaFormulario">
                        <div style="width: 250px; float: left;" class="etiqueta">
                            <%=meLanbide12I18n.getMensaje(idiomaUsuario,"label.imp_solic")%>
                        </div>
                        <div>
                            <div style="float: left;">
                                <input id="imp_solic" name="imp_solic" type="text" class="inputTexto" size="25" maxlength="10"
                                       value="<%=datModif != null && datModif.getImpSolic() != null ? datModif.getImpSolic().toString().replaceAll("\\.", ","): ""%>" onchange="reemplazarPuntos(this);"/>
                            </div>
                        </div>
                    </div>

                    <br><br>
                    <div class="lineaFormulario">
                        <div class="botonera" style="width: 100%; float: left; text-align: center;">
                            <input type="button" id="btnAceptar" name="btnAceptar" class="botonGeneral" value="<%=meLanbide12I18n.getMensaje(idiomaUsuario, "btn.aceptar")%>" onclick="guardarDatos();"/>
                            <input type="button" id="btnCancelar" name="btnCancelar" class="botonGeneral" value="<%=meLanbide12I18n.getMensaje(idiomaUsuario, "btn.cancelar")%>" onclick="cancelar();"/>
                        </div>
                    </div>
                </div>
            </form>

            <div id="popupcalendar" class="text"></div>
        </div>
    </body>
</html>

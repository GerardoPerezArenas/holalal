<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide12.i18n.MeLanbide12I18n" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide12.vo.FilaL1EmpresaExternaVO" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide12.vo.DesplegableAdmonLocalVO" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide12.util.ConstantesMeLanbide12" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide12.util.ConfigurationParameter" %>
<%@page import="java.util.ArrayList" %>
<%@page import="java.util.List" %>
<%@page import="java.text.SimpleDateFormat" %>
<html>
    <head>
        <meta http-equiv="X-UA-Compatible" content="IE=Edge" />
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <%
            FilaL1EmpresaExternaVO datModif = new FilaL1EmpresaExternaVO();

            String codOrganizacion = "";
            String nuevo = "";
            String numExpediente = "";
            String fec_emis = "";
            String fec_pago = "";


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
                    datModif = (FilaL1EmpresaExternaVO)request.getAttribute("datModif");
                    SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
                    fec_emis = formatoFecha.format(datModif.getFecEmis());
                    fec_pago = formatoFecha.format(datModif.getFecPago());
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

            function mostrarCalFec_emis(evento) {
                if (window.event)
                    evento = window.event;
                if (document.getElementById("calFec_emis").src.indexOf("icono.gif") != -1)
                    showCalendar('forms[0]', 'fec_emis', null, null, null, '', 'calFec_emis', '', null, null, null, null, null, null, null, null, evento);
            }
            function mostrarCalFec_pago(evento) {
                if (window.event)
                    evento = window.event;
                if (document.getElementById("calFec_pago").src.indexOf("icono.gif") != -1)
                    showCalendar('forms[0]', 'fec_pago', null, null, null, '', 'calFec_pago', '', null, null, null, null, null, null, null, null, evento);
            }

            function guardarDatos() {
                if (validarDatos()) {
                    elementoVisible('on', 'barraProgresoPRACT');
                    var parametros = "";

                    if ('<%=nuevo%>' != null && '<%=nuevo%>' == "1") {
                        parametros = "tarea=preparar&modulo=MELANBIDE12&operacion=crearNuevoL1EmpresaExterna&tipo=0";
                    } else {
                        parametros = "tarea=preparar&modulo=MELANBIDE12&operacion=modificarL1EmpresaExterna&tipo=0"
                                + "&id=<%=datModif != null && datModif.getId() != null ? datModif.getId().toString() : ""%>";
                    }
                    parametros += '&numExp=<%=numExpediente%>'
                            + '&cif=' + document.getElementById('cif').value
                            + '&denom_empr=' + document.getElementById('denom_empr').value
                            + '&n_factura=' + document.getElementById('n_factura').value
                            + '&fec_emis=' + document.getElementById('fec_emis').value
                            + '&fec_pago=' + document.getElementById('fec_pago').value
                            + '&imp_base=' + document.getElementById('imp_base').value
                            + '&imp_iva=' + document.getElementById('imp_iva').value
                            + '&imp_total=' + document.getElementById('imp_total').value
                            + '&personas=' + document.getElementById('personas').value
                            + '&imp_persona_fact=' + document.getElementById('imp_persona_fact').value
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
                campo = document.getElementById('cif').value;
                if (campo == null || campo == '') {
                    mensajeValidacion = '<%=meLanbide12I18n.getMensaje(idiomaUsuario, "msg.cif")%>';
                    return false;
                }
                campo = document.getElementById('denom_empr').value;
                if (campo == null || campo == '') {
                    mensajeValidacion = '<%=meLanbide12I18n.getMensaje(idiomaUsuario, "msg.denom_empr")%>';
                    return false;
                }
                campo = document.getElementById('fec_emis').value;
                if (campo == null || campo == '') {
                    mensajeValidacion = '<%=meLanbide12I18n.getMensaje(idiomaUsuario, "msg.fechaSub")%>';
                    return false;
                }
                campo = document.getElementById('fec_pago').value;
                if (campo == null || campo == '') {
                    mensajeValidacion = '<%=meLanbide12I18n.getMensaje(idiomaUsuario, "msg.fechaSub")%>';
                    return false;
                }
                campo = document.getElementById('imp_base').value;
                if (campo == null || campo == '') {
                    mensajeValidacion = '<%=meLanbide12I18n.getMensaje(idiomaUsuario, "msg.imp_base")%>';
                    return false;
                } else if (!validarNumericoDecimalPrecision(campo, 8, 2)) {
                    mensajeValidacion = '<%=meLanbide12I18n.getMensaje(idiomaUsuario, "msg.importe.errNumerico")%>';
                    return false;
                }
                campo = document.getElementById('imp_iva').value;
                if (campo == null || campo == '') {
                    mensajeValidacion = '<%=meLanbide12I18n.getMensaje(idiomaUsuario, "msg.imp_iva")%>';
                    return false;
                } else if (!validarNumericoDecimalPrecision(campo, 8, 2)) {
                    mensajeValidacion = '<%=meLanbide12I18n.getMensaje(idiomaUsuario, "msg.importe.errNumerico")%>';
                    return false;
                }
                campo = document.getElementById('imp_total').value;
                if (campo == null || campo == '') {
                    mensajeValidacion = '<%=meLanbide12I18n.getMensaje(idiomaUsuario, "msg.imp_total")%>';
                    return false;
                } else if (!validarNumericoDecimalPrecision(campo, 8, 2)) {
                    mensajeValidacion = '<%=meLanbide12I18n.getMensaje(idiomaUsuario, "msg.importe.errNumerico")%>';
                    return false;
                }
                campo = document.getElementById('personas').value;
                if (campo == null || campo == '') {
                    mensajeValidacion = '<%=meLanbide12I18n.getMensaje(idiomaUsuario, "msg.personas")%>';
                    return false;
                } else if (!validarNumericoDecimalPrecision(campo, 8, 2)) {
                    mensajeValidacion = '<%=meLanbide12I18n.getMensaje(idiomaUsuario, "msg.importe.errNumerico")%>';
                    return false;
                }
                campo = document.getElementById('imp_persona_fact').value;
                if (campo == null || campo == '') {
                    mensajeValidacion = '<%=meLanbide12I18n.getMensaje(idiomaUsuario, "msg.imp_persona_fact")%>';
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

            function comprobarFecha(inputFecha) {
                var formato = 'dd/mm/yyyy';
                if (Trim(inputFecha.value) != '') {
                    var D = ValidarFechaConFormato(inputFecha.value, formato);
                    if (!D[0]) {
                        jsp_alerta("A", "<%=meLanbide12I18n.getMensaje(idiomaUsuario, "msg.fechaNoVal")%>");
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
                            <%=meLanbide12I18n.getMensaje(idiomaUsuario,"label.nuevoL1EmpresaExterna")%>
                        </span>
                    </div>

                    <div class="lineaFormulario">
                        <div style="width: 250px; float: left;" class="etiqueta">
                            <%=meLanbide12I18n.getMensaje(idiomaUsuario,"label.cif")%>
                        </div>
                        <div>
                            <div style="float: left;">
                                <input id="cif" name="cif" type="text" class="inputTexto" size="100" maxlength="100"
                                       value="<%=datModif != null && datModif.getCif() != null ? datModif.getCif() : ""%>"/>
                            </div>
                        </div>
                    </div>

                    <br><br>
                    <div class="lineaFormulario">
                        <div style="width: 250px; float: left;" class="etiqueta">
                            <%=meLanbide12I18n.getMensaje(idiomaUsuario,"label.denom_empr")%>
                        </div>
                        <div>
                            <div style="float: left;">
                                <input id="denom_empr" name="denom_empr" type="text" class="inputTexto" size="100" maxlength="50"
                                       value="<%=datModif != null && datModif.getDenomEmpr() != null ? datModif.getDenomEmpr() : ""%>"/>
                            </div>
                        </div>
                    </div>

                    <br><br>
                    <div class="lineaFormulario">
                        <div style="width: 250px; float: left;" class="etiqueta">
                            <%=meLanbide12I18n.getMensaje(idiomaUsuario,"label.n_factura")%>
                        </div>
                        <div>
                            <div style="float: left;">
                                <input id="n_factura" name="n_factura" type="text" class="inputTexto" size="100" maxlength="50"
                                       value="<%=datModif != null && datModif.getnFactura() != null ? datModif.getnFactura() : ""%>"/>
                            </div>
                        </div>
                    </div>

                    <br><br>
                    <div class="lineaFormulario">
                        <div style="width: 250px; float: left;" class="etiqueta">
                            <%=meLanbide12I18n.getMensaje(idiomaUsuario,"label.fec_emis")%>
                        </div>
                        <div>
                            <div style="float: left;">
                                <input type="text" class="inputTxtFecha"
                                       id="fec_emis" name="fec_emis"
                                       maxlength="10"  size="10"
                                       value="<%=fec_emis%>"
                                       onkeyup = "return SoloCaracteresFechaLanbide(this);"
                                       onblur = "javascript:return comprobarFechaLanbide(this);"
                                       onfocus="javascript:this.select();"/>
                                <A href="javascript:calClick(event);return false;" onClick="mostrarCalFec_emis(event);return false;" style="text-decoration:none;">
                                    <IMG style="border: 0px solid none" height="17" id="calFec_emis" name="calFec_emis" border="0"
                                         src="<c:url value='/images/calendario/icono.gif'/>" > <!--width="25"-->
                                </A>
                            </div>
                        </div>
                    </div>

                    <br><br>
                    <div class="lineaFormulario">
                        <div style="width: 250px; float: left;" class="etiqueta">
                            <%=meLanbide12I18n.getMensaje(idiomaUsuario,"label.fec_pago")%>
                        </div>
                        <div>
                            <div style="float: left;">
                                <input type="text" class="inputTxtFecha"
                                       id="fec_pago" name="fec_pago"
                                       maxlength="10"  size="10"
                                       value="<%=fec_pago%>"
                                       onkeyup = "return SoloCaracteresFechaLanbide(this);"
                                       onblur = "javascript:return comprobarFechaLanbide(this);"
                                       onfocus="javascript:this.select();"/>
                                <A href="javascript:calClick(event);return false;" onClick="mostrarCalFec_pago(event);return false;" style="text-decoration:none;">
                                    <IMG style="border: 0px solid none" height="17" id="calFec_pago" name="calFec_pago" border="0"
                                         src="<c:url value='/images/calendario/icono.gif'/>" > <!--width="25"-->
                                </A>
                            </div>
                        </div>
                    </div>

                    <br><br>
                    <div class="lineaFormulario">
                        <div style="width: 250px; float: left;" class="etiqueta">
                            <%=meLanbide12I18n.getMensaje(idiomaUsuario,"label.imp_base")%>
                        </div>
                        <div>
                            <div style="float: left;">
                                <input id="imp_base" name="imp_base" type="text" class="inputTexto" size="25" maxlength="10"
                                       value="<%=datModif != null && datModif.getImpBase() != null ? datModif.getImpBase().toString().replaceAll("\\.", ","): ""%>" onchange="reemplazarPuntos(this);"/>
                            </div>
                        </div>
                    </div>

                    <br><br>
                    <div class="lineaFormulario">
                        <div style="width: 250px; float: left;" class="etiqueta">
                            <%=meLanbide12I18n.getMensaje(idiomaUsuario,"label.imp_iva")%>
                        </div>
                        <div>
                            <div style="float: left;">
                                <input id="imp_iva" name="imp_iva" type="text" class="inputTexto" size="25" maxlength="10"
                                       value="<%=datModif != null && datModif.getImpIva() != null ? datModif.getImpIva().toString().replaceAll("\\.", ","): ""%>" onchange="reemplazarPuntos(this);"/>
                            </div>
                        </div>
                    </div>

                    <br><br>
                    <div class="lineaFormulario">
                        <div style="width: 250px; float: left;" class="etiqueta">
                            <%=meLanbide12I18n.getMensaje(idiomaUsuario,"label.imp_total")%>
                        </div>
                        <div>
                            <div style="float: left;">
                                <input id="imp_total" name="imp_total" type="text" class="inputTexto" size="25" maxlength="10"
                                       value="<%=datModif != null && datModif.getImpTotal() != null ? datModif.getImpTotal().toString().replaceAll("\\.", ","): ""%>" onchange="reemplazarPuntos(this);"/>
                            </div>
                        </div>
                    </div>

                    <br><br>
                    <div class="lineaFormulario">
                        <div style="width: 250px; float: left;" class="etiqueta">
                            <%=meLanbide12I18n.getMensaje(idiomaUsuario,"label.personas")%>
                        </div>
                        <div>
                            <div style="float: left;">
                                <input id="personas" name="personas" type="text" class="inputTexto" size="25" maxlength="10"
                                       value="<%=datModif != null && datModif.getPersonas() != null ? datModif.getPersonas().toString().replaceAll("\\.", ","): ""%>" onchange="reemplazarPuntos(this);"/>
                            </div>
                        </div>
                    </div>

                    <br><br>
                    <div class="lineaFormulario">
                        <div style="width: 250px; float: left;" class="etiqueta">
                            <%=meLanbide12I18n.getMensaje(idiomaUsuario,"label.imp_persona_fact")%>
                        </div>
                        <div>
                            <div style="float: left;">
                                <input id="imp_persona_fact" name="imp_persona_fact" type="text" class="inputTexto" size="25" maxlength="10"
                                       value="<%=datModif != null && datModif.getImpPersonaFact() != null ? datModif.getImpPersonaFact().toString().replaceAll("\\.", ","): ""%>" onchange="reemplazarPuntos(this);"/>
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

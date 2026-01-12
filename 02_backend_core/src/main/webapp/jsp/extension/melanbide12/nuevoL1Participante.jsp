<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide12.i18n.MeLanbide12I18n" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide12.vo.FilaL1ParticipanteVO" %>
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
            FilaL1ParticipanteVO datModif = new FilaL1ParticipanteVO();

            String codOrganizacion = "";
            String nuevo = "";
            String numExpediente = "";
            String fec_ini_pract = "";
            String fec_fin_pract = "";


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
                    datModif = (FilaL1ParticipanteVO)request.getAttribute("datModif");
                    SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
                    fec_ini_pract = formatoFecha.format(datModif.getFecIniPract());
                    fec_fin_pract = formatoFecha.format(datModif.getFecFinPract());
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

            //Desplegable  tipoDoc
            var comboListaTipoDoc;
            var listaCodigosTipoDoc = new Array();
            var listaDescripcionesTipoDoc = new Array();
            function buscaCodigoTipoDoc(codTipoDoc) {
                comboListaTipoDoc.buscaCodigo(codTipoDoc);
            }
            function cargarDatosTipoDoc() {
                var codTipoDocSeleccionado = document.getElementById("codListaTipoDoc").value;
                buscaCodigoTipoDoc(codTipoDocSeleccionado);
            }


            function mostrarCalFec_ini_pract(evento) {
                if (window.event)
                    evento = window.event;
                if (document.getElementById("calFec_ini_pract").src.indexOf("icono.gif") != -1)
                    showCalendar('forms[0]', 'fec_ini_pract', null, null, null, '', 'calFec_ini_pract', '', null, null, null, null, null, null, null, null, evento);
            }
            function mostrarCalFec_fin_pract(evento) {
                if (window.event)
                    evento = window.event;
                if (document.getElementById("calFec_fin_pract").src.indexOf("icono.gif") != -1)
                    showCalendar('forms[0]', 'fec_fin_pract', null, null, null, '', 'calFec_fin_pract', '', null, null, null, null, null, null, null, null, evento);
            }

            function guardarDatos() {
                if (validarDatos()) {
                    elementoVisible('on', 'barraProgresoPRACT');
                    var parametros = "";

                    if ('<%=nuevo%>' != null && '<%=nuevo%>' == "1") {
                        parametros = "tarea=preparar&modulo=MELANBIDE12&operacion=crearNuevoL1Participante&tipo=0";
                    } else {
                        parametros = "tarea=preparar&modulo=MELANBIDE12&operacion=modificarL1Participante&tipo=0"
                                + "&id=<%=datModif != null && datModif.getId() != null ? datModif.getId().toString() : ""%>";
                    }
                    parametros += '&numExp=<%=numExpediente%>'
                            + '&tipoDoc=' + document.getElementById('codListaTipoDoc').value
                            + '&doc=' + document.getElementById('doc').value
                            + '&nombre=' + document.getElementById('nombre').value
                            + '&ape1=' + document.getElementById('ape1').value
                            + '&ape2=' + document.getElementById('ape2').value
                            + '&nss=' + document.getElementById('nss').value
                            + '&cod_act_form=' + document.getElementById('cod_act_form').value
                            + '&fec_ini_pract=' + document.getElementById('fec_ini_pract').value
                            + '&fec_fin_pract=' + document.getElementById('fec_fin_pract').value
                            + '&cc_cot=' + document.getElementById('cc_cot').value
                            + '&dias_cot=' + document.getElementById('dias_cot').value
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
                campo = document.getElementById('codListaTipoDoc').value;
                if (campo == null || campo == '') {
                    mensajeValidacion = '<%=meLanbide12I18n.getMensaje(idiomaUsuario, "msg.tipoDoc")%>';
                    return false;
                }
                campo = document.getElementById('doc').value;
                if (campo == null || campo == '') {
                    mensajeValidacion = '<%=meLanbide12I18n.getMensaje(idiomaUsuario, "msg.doc")%>';
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
                campo = document.getElementById('fec_ini_pract').value;
                if (campo == null || campo == '') {
                    mensajeValidacion = '<%=meLanbide12I18n.getMensaje(idiomaUsuario, "msg.fechaSub")%>';
                    return false;
                }
                campo = document.getElementById('fec_fin_pract').value;
                if (campo == null || campo == '') {
                    mensajeValidacion = '<%=meLanbide12I18n.getMensaje(idiomaUsuario, "msg.fechaSub")%>';
                    return false;
                }
                campo = document.getElementById('dias_cot').value;
                if (campo == null || campo == '') {
                    mensajeValidacion = '<%=meLanbide12I18n.getMensaje(idiomaUsuario, "msg.dias_cot")%>';
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

            function rellenarDesplegables() {
                if ('<%=datModif%>' != null) {
                    buscaCodigoTipoDoc('<%=datModif.getTipoDoc() != null ? datModif.getTipoDoc() : ""%>');
                } else
                    alert('No hemos podido cargar los datos para modificar');
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
                            <%=meLanbide12I18n.getMensaje(idiomaUsuario,"label.nuevoL1Participante")%>
                        </span>
                    </div>

                    <br><br>
                    <div class="lineaFormulario">
                        <div class="etiqueta" style="width: 250px; float: left;">
                            <%=meLanbide12I18n.getMensaje(idiomaUsuario,"label.tipoDoc")%>
                        </div>
                        <div>
                            <div>
                                <input type="text" name="codListaTipoDoc" id="codListaTipoDoc" size="12" class="inputTexto" value="" onkeyup="xAMayusculas(this);" />
                                <input type="text" name="descListaTipoDoc"  id="descListaTipoDoc" size="60" class="inputTexto" readonly="true" value="" />
                                <a href="" id="anchorListaTipoDoc" name="anchorListaTipoDoc">
                                    <span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonAplicacion"
                                          name="botonAplicacion" style="cursor:hand;"></span>
                                </a>
                            </div>
                        </div>
                    </div>

                    <div class="lineaFormulario">
                        <div style="width: 250px; float: left;" class="etiqueta">
                            <%=meLanbide12I18n.getMensaje(idiomaUsuario,"label.doc")%>
                        </div>
                        <div>
                            <div style="float: left;">
                                <input id="doc" name="doc" type="text" class="inputTexto" size="100" maxlength="100"
                                       value="<%=datModif != null && datModif.getDoc() != null ? datModif.getDoc() : ""%>"/>
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
                            <%=meLanbide12I18n.getMensaje(idiomaUsuario,"label.nss")%>
                        </div>
                        <div>
                            <div style="float: left;">
                                <input id="nss" name="nss" type="text" class="inputTexto" size="100" maxlength="50"
                                       value="<%=datModif != null && datModif.getNss() != null ? datModif.getNss() : ""%>"/>
                            </div>
                        </div>
                    </div>

                    <br><br>
                    <div class="lineaFormulario">
                        <div style="width: 250px; float: left;" class="etiqueta">
                            <%=meLanbide12I18n.getMensaje(idiomaUsuario,"label.cod_act_form")%>
                        </div>
                        <div>
                            <div style="float: left;">
                                <input id="cod_act_form" name="cod_act_form" type="text" class="inputTexto" size="100" maxlength="50"
                                       value="<%=datModif != null && datModif.getCodActForm() != null ? datModif.getCodActForm() : ""%>"/>
                            </div>
                        </div>
                    </div>

                    <br><br>
                    <div class="lineaFormulario">
                        <div style="width: 250px; float: left;" class="etiqueta">
                            <%=meLanbide12I18n.getMensaje(idiomaUsuario,"label.fec_ini_pract")%>
                        </div>
                        <div>
                            <div style="float: left;">
                                <input type="text" class="inputTxtFecha"
                                       id="fec_ini_pract" name="fec_ini_pract"
                                       maxlength="10"  size="10"
                                       value="<%=fec_ini_pract%>"
                                       onkeyup = "return SoloCaracteresFechaLanbide(this);"
                                       onblur = "javascript:return comprobarFechaLanbide(this);"
                                       onfocus="javascript:this.select();"/>
                                <A href="javascript:calClick(event);return false;" onClick="mostrarCalFec_ini_pract(event);return false;" style="text-decoration:none;">
                                    <IMG style="border: 0px solid none" height="17" id="calFec_ini_pract" name="calFec_ini_pract" border="0"
                                         src="<c:url value='/images/calendario/icono.gif'/>" > <!--width="25"-->
                                </A>
                            </div>
                        </div>
                    </div>

                    <br><br>
                    <div class="lineaFormulario">
                        <div style="width: 250px; float: left;" class="etiqueta">
                            <%=meLanbide12I18n.getMensaje(idiomaUsuario,"label.fec_fin_pract")%>
                        </div>
                        <div>
                            <div style="float: left;">
                                <input type="text" class="inputTxtFecha"
                                       id="fec_fin_pract" name="fec_fin_pract"
                                       maxlength="10"  size="10"
                                       value="<%=fec_fin_pract%>"
                                       onkeyup = "return SoloCaracteresFechaLanbide(this);"
                                       onblur = "javascript:return comprobarFechaLanbide(this);"
                                       onfocus="javascript:this.select();"/>
                                <A href="javascript:calClick(event);return false;" onClick="mostrarCalFec_fin_pract(event);return false;" style="text-decoration:none;">
                                    <IMG style="border: 0px solid none" height="17" id="calFec_fin_pract" name="calFec_fin_pract" border="0"
                                         src="<c:url value='/images/calendario/icono.gif'/>" > <!--width="25"-->
                                </A>
                            </div>
                        </div>
                    </div>

                    <br><br>
                    <div class="lineaFormulario">
                        <div style="width: 250px; float: left;" class="etiqueta">
                            <%=meLanbide12I18n.getMensaje(idiomaUsuario,"label.cc_cot")%>
                        </div>
                        <div>
                            <div style="float: left;">
                                <input id="cc_cot" name="cc_cot" type="text" class="inputTexto" size="100" maxlength="50"
                                       value="<%=datModif != null && datModif.getCcCot() != null ? datModif.getCcCot() : ""%>"/>
                            </div>
                        </div>
                    </div>

                    <br><br>
                    <div class="lineaFormulario">
                        <div style="width: 250px; float: left;" class="etiqueta">
                            <%=meLanbide12I18n.getMensaje(idiomaUsuario,"label.dias_cot")%>
                        </div>
                        <div>
                            <div style="float: left;">
                                <input id="dias_cot" name="dias_cot" type="text" class="inputTexto" size="25" maxlength="10"
                                       value="<%=datModif != null && datModif.getDiasCot() != null ? datModif.getDiasCot().toString().replaceAll("\\.", ","): ""%>" onchange="reemplazarPuntos(this);"/>
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
            <script type="text/javascript">

                /*desplegable tipoDoc*/
                listaCodigosTipoDoc[0] = "";
                listaDescripcionesTipoDoc[0] = "";
                contador = 0;
                <logic:iterate id="tipoDoc" name="listaTipoDoc" scope="request">
                listaCodigosTipoDoc[contador] = ['<bean:write name="tipoDoc" property="des_val_cod" />'];
                listaDescripcionesTipoDoc[contador] = ['<bean:write name="tipoDoc" property="des_nom" />'];
                contador++;
                </logic:iterate>
                comboListaTipoDoc = new Combo("ListaTipoDoc");
                comboListaTipoDoc.addItems(listaCodigosTipoDoc, listaDescripcionesTipoDoc);
                comboListaTipoDoc.change = cargarDatosTipoDoc;

                if (<%=nuevo%> != null && <%=nuevo%> == 0) {
                    rellenarDesplegables();
                }

            </script>
            <div id="popupcalendar" class="text"></div>
        </div>
    </body>
</html>

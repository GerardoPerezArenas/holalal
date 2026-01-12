<%-- 
    Document   : m81ProyectoMantenimiento
    Created on : 05-abr-2022, 14:37:29
    Author     : kepa
--%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide81.i18n.MeLanbide81I18n" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide81.vo.ProyectoVO" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide81.util.ConstantesMeLanbide81" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide81.util.ConfigurationParameter" %>
<%@page import="java.util.ArrayList" %>
<%@page import="java.util.List" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="X-UA-Compatible" content="IE=Edge" />
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Mantenimiento PROYECTOS</title>
        <%
            UsuarioValueObject usuarioVO = new UsuarioValueObject();
            ProyectoVO datModif = new ProyectoVO();
            int idiomaUsuario = 1;
            int apl = 5;
            String css = "";
            String nuevo = "";  
            try {
                if (session.getAttribute("usuario") != null) {
                    usuarioVO = (UsuarioValueObject) session.getAttribute("usuario");
                    apl = usuarioVO.getAppCod();
                    idiomaUsuario = usuarioVO.getIdioma();
                    css = usuarioVO.getCss();
                }
                nuevo = (String)request.getAttribute("nuevo");
                if(request.getAttribute("datModif") != null) {
                    datModif = (ProyectoVO)request.getAttribute("datModif");
                }

            }catch(Exception ex) {
            }
           
            //Clase para internacionalizar los mensajes de la aplicación.
            MeLanbide81I18n meLanbide81I18n = MeLanbide81I18n.getInstance();
            String numExpediente = (String)request.getAttribute("numExp");
        %>
        <jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
        <jsp:setProperty name="descriptor"  property="idi_cod" value="<%=idiomaUsuario%>" />
        <jsp:setProperty name="descriptor"  property="apl_cod" value="<%=apl%>" />
        <link rel="StyleSheet" media="screen" type="text/css" href="<%=request.getContextPath()%><%=css%>">
        <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/css/font-awesome.min.css'/>" media="screen">
        <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/less/animated.less'/>" media="screen">
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/>
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/extension/melanbide81/melanbide81.css"/>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/listaComboBox.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/JavaScriptUtil.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script>        
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/general.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/melanbide81/LpeelUtils.js"></script>
        <script type="text/javascript">
            var APP_CONTEXT_PATH = '<%=request.getContextPath()%>';
            var url = APP_CONTEXT_PATH + '/PeticionModuloIntegracion.do';
            var mensajeValidacion = '';

            //Desplegable  TipoProy Proyecto
            var comboListaTipoProy;
            var listaCodigosTipoProy = new Array();
            var listaDescripcionesTipoProy = new Array();
            function buscaCodigoTipoProy(tipo) {
                comboListaTipoProy.buscaCodigo(tipo);
            }
            function cargarDatosTipoProy() {
                var tipoSeleccionado = document.getElementById("codListaTipoProy").value;
                buscaCodigoTipoProy(tipoSeleccionado);
            }

            function guardarProyecto() {
                if (validarDatos()) {
                    elementoVisible('on', 'barraProgresoLPEEL');
                    var parametros = "";
                    if ('<%=nuevo%>' != null && '<%=nuevo%>' == "1") {
                        parametros = "tarea=preparar&modulo=MELANBIDE81&operacion=crearNuevoProyecto&tipo=0";
                    } else {
                        parametros = "tarea=preparar&modulo=MELANBIDE81&operacion=modificarProyecto&tipo=0"
                                + "&id=<%=datModif != null && datModif.getId() != null ? datModif.getId().toString() : ""%>";
                    }
                    parametros += "&numExp=<%=numExpediente%>"
                            + "&prioridad=" + document.getElementById('prioridad').value
                            + "&denominacion=" + document.getElementById('denomProyecto').value.replace(/\r?\n|\r/g, " ").trim()
                            + "&entidad=" + document.getElementById('entidad').value.replace(/\r?\n|\r/g, " ").trim()
                            + "&tipoProy=" + document.getElementById('codListaTipoProy').value
                            + "&fases=" + document.getElementById('fases').value
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
                        elementoVisible('off', 'barraProgresoLPEEL');
                        mostrarErrorPeticion();
                    }
                } else {
                    jsp_alerta("A", mensajeValidacion);
                }
            }

            function validarDatos() {
                mensajeValidacion = "";

                var campo = document.getElementById('prioridad');
                if (campo.value == null || campo.value == '') {
                    mensajeValidacion = '<%=meLanbide81I18n.getMensaje(idiomaUsuario, "msg.prioridad")%>';
                    return false;
                } else if (/^([0-9])*$/.test(campo)) {
                    mensajeValidacion = '<%=meLanbide81I18n.getMensaje(idiomaUsuario, "msg.prioridad.formato")%>';
                    return false;
                }

                campo = document.getElementById('denomProyecto');
                if (campo.value == null || campo.value == '') {
                    mensajeValidacion = '<%=meLanbide81I18n.getMensaje(idiomaUsuario, "msg.denomProyecto")%>';
                    return false;
                } else if (!compruebaTamanoCampo(campo, 200)) {
                    mensajeValidacion = '<%=meLanbide81I18n.getMensaje(idiomaUsuario, "msg.denomProyecto.tamano")%>';
                    return false;
                }

                campo = document.getElementById('entidad');
                if (campo.value == null || campo.value == '') {
                    mensajeValidacion = '<%=meLanbide81I18n.getMensaje(idiomaUsuario, "msg.entidad")%>';
                    return false;
                } else if (!compruebaTamanoCampo(campo, 100)) {
                    mensajeValidacion = '<%=meLanbide81I18n.getMensaje(idiomaUsuario, "msg.entidad.tamano")%>';
                    return false;
                }

                campo = document.getElementById('codListaTipoProy');
                if (campo.value == null || campo.value == '') {
                    mensajeValidacion = '<%=meLanbide81I18n.getMensaje(idiomaUsuario, "msg.tipoProyecto")%>';
                    return false;
                }

                campo = document.getElementById('fases');
                if (campo.value == null || campo.value == '') {
                    mensajeValidacion = '<%=meLanbide81I18n.getMensaje(idiomaUsuario, "msg.fases")%>';
                    return false;
                } else if (/^([0-9])*$/.test(campo)) {
                    mensajeValidacion = '<%=meLanbide81I18n.getMensaje(idiomaUsuario, "msg.fases.formato")%>';
                    return false;
                }
                return true;
            }

            function cancelar() {
                var resultado = jsp_alerta('', '<%=meLanbide81I18n.getMensaje(idiomaUsuario, "msg.preguntaCancelar")%>');
                if (resultado == 1) {
                    cerrarVentana();
                }
            }

            function rellenarDesplegables() {
                if ('<%=datModif%>' != null) {
                    buscaCodigoTipoProy('<%=datModif.getTipoProyecto() != null ? datModif.getTipoProyecto() : ""%>');
                }
            }

            function desbloquearCampos() {
                document.getElementById('prioridad').disabled = false;
                document.getElementById('denomProyecto').disabled = false;
            }

            // FUNCIONES QUE PROCESAN RESPUESTA O ERROR DE LA PETICION AJAX
            function procesarRespuestaAltaModificacion(ajaxResult) {
                elementoVisible('off', 'barraProgresoLPEEL');
                var datos = JSON.parse(ajaxResult);
                var codigoOperacion = datos.tabla.codigoOperacion;
                if (codigoOperacion == "0") {
                    var proyectos = datos.tabla.lista;
                    if (proyectos.length > 0) {
                        var respuesta = new Array();
                        respuesta[0] = "0";
                        respuesta[1] = proyectos;
                        self.parent.opener.retornoXanelaAuxiliar(respuesta);
                        cerrarVentana();
                    } else {
                        mostrarErrorPeticion(5);
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
                    <div  class="sub3titulo" style="clear: both; text-align: center; font-size: 14px;">
                        <span id="tituloVentana">
                            <%=nuevo != null && nuevo=="1" ? meLanbide81I18n.getMensaje(idiomaUsuario,"label.nuevo") : meLanbide81I18n.getMensaje(idiomaUsuario,"label.modif")%>
                        </span>
                    </div>

                    <legend class="legendAzul" id="titProyecto"><%=meLanbide81I18n.getMensaje(idiomaUsuario,"label.proyecto")%></legend>                        
                    <div class="lineaFormulario">
                        <div  class="etiquetaLPEEL">
                            <%=meLanbide81I18n.getMensaje(idiomaUsuario,"label.prioridad")%>
                        </div>
                        <div style="width:450px;; float: left;">
                            <input id="prioridad" name="prioridad"  type="text" class="inputTextoObligatorio" size="6" maxlength="6" onkeyup="SoloDigitos(this);" 
                                   value="<%=datModif != null && datModif.getPrioridad() != null ? datModif.getPrioridad() : ""%>" disabled/>
                        </div>
                    </div>
                    <div class="lineaFormulario" style="padding-top: 40px">
                        <div  class="etiquetaLPEEL">
                            <%=meLanbide81I18n.getMensaje(idiomaUsuario,"label.denominacion")%>
                        </div>
                        <div style="width:450px; float: left; ">
                            <textarea  id="denomProyecto" name="denomProyecto" type="text" class="inputTextoObligatorio" rows="4" cols="50" maxlength="200" disabled style="text-align: left"><%=datModif != null && datModif.getDenominacion() != null ? datModif.getDenominacion() : ""%></textarea>
                        </div>
                    </div>
                    <div class="lineaFormulario" style="padding-top: 80px">
                        <div  class="etiquetaLPEEL">
                            <%=meLanbide81I18n.getMensaje(idiomaUsuario,"label.entidad")%>
                        </div>
                        <div style="width:450px; float: left;">
                            <input id="entidad" name="entidad" type="text" class="inputTextoObligatorio" size="50" maxlength="100" 
                                   value="<%=datModif != null && datModif.getEntidad() != null ? datModif.getEntidad() : ""%>" />
                        </div>
                    </div>
                    <div class="lineaFormulario" style="padding-top: 40px">
                        <div  class="etiquetaLPEEL">
                            <%=meLanbide81I18n.getMensaje(idiomaUsuario,"label.tipoProy")%>
                        </div>
                        <div style="width:450px; float: left;">
                            <input type="text" name="codListaTipoProy" id="codListaTipoProy" size="3" class="inputComboObligatorio" value="" onkeyup="SoloDigitos(this);" maxlength="1"/>
                            <input type="text" name="descListaTipoProy"  id="descListaTipoProy" size="40" class="inputComboObligatorio" readonly value="" />
                            <a href="" id="anchorListaTipoProy" name="anchorListaTipoProy" title="<%=meLanbide81I18n.getMensaje(idiomaUsuario,"label.tipoProy")%>">
                                <span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonAplicacion" style="cursor:pointer;"></span>
                            </a>
                        </div>
                    </div>
                    <div class="lineaFormulario" style="padding-top: 40px">
                        <div  class="etiquetaLPEEL">
                            <%=meLanbide81I18n.getMensaje(idiomaUsuario,"label.fases")%>
                        </div>
                        <div style="width: 350px;float: left;">
                            <input id="fases" name="fases" type="text" class="inputTextoObligatorio" size="3" maxlength="2" 
                                   value="<%=datModif != null && datModif.getFases() != null ? datModif.getFases(): ""%>" onkeyup="SoloDigitos(this);" />
                        </div>
                    </div>
                    <div class="lineaFormulario" style="padding-top: 40px">
                        <div class="botonera" style="width: 100%; float: left; text-align: center;">
                            <input type="button" id="btnAceptar" name="btnAceptar" class="botonGeneral" value="<%=meLanbide81I18n.getMensaje(idiomaUsuario, "btn.aceptar")%>" onclick="guardarProyecto();"/>
                            <input type="button" id="btnCancelar" name="btnCancelar" class="botonGeneral" value="<%=meLanbide81I18n.getMensaje(idiomaUsuario, "btn.cancelar")%>" onclick="cancelar();"/>
                        </div>
                    </div>
                </div>
            </form>
            <script type="text/javascript">
                /*  TIPO */
                listaCodigosTipoProy[0] = "";
                listaDescripcionesTipoProy[0] = "";
                contador = 0;
                <logic:iterate id="tipoProy" name="listaTipoProy" scope="request">
                listaCodigosTipoProy[contador] = ['<bean:write name="tipoProy" property="des_val_cod" />'];
                listaDescripcionesTipoProy[contador] = ['<bean:write name="tipoProy" property="des_nom" />'];
                contador++;
                </logic:iterate>
                var comboListaTipoProy = new Combo("ListaTipoProy");
                comboListaTipoProy.addItems(listaCodigosTipoProy, listaDescripcionesTipoProy);
                comboListaTipoProy.change = cargarDatosTipoProy;

                if (<%=nuevo%> != null && <%=nuevo%> == 0) {
                    rellenarDesplegables();
                } else {
                    desbloquearCampos();
                }
            </script>
        </div>
    </body>
</html>
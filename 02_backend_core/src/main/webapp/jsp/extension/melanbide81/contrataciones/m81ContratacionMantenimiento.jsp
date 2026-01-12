<%-- 
    Document   : m81ContratacionMantenimiento
    Created on : 05-abr-2022, 14:37:07
    Author     : kepa
--%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide81.i18n.MeLanbide81I18n" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide81.vo.ContratacionVO" %>
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
        <title>Mantenimiento CONTRATOS</title>
        <%
            UsuarioValueObject usuarioVO = new UsuarioValueObject();
            ContratacionVO datModif = new ContratacionVO();
            int idiomaUsuario = 1;
            int apl = 5;
            String css = "";
            String nuevo = "";
            String numExpediente = "";
            String idProyecto = "";
            String prioridad = "";
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
                if (request.getAttribute("prioridad") != null){
                    prioridad = (String)request.getAttribute("prioridad");
                }
                if(request.getAttribute("datModif") != null) {
                    datModif = (ContratacionVO)request.getAttribute("datModif");
                }
            }catch(Exception ex) {
            }           
            //Clase para internacionalizar los mensajes de la aplicación.
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

//Desplegable  Tipo Desempleado
            var comboListaTipoDesempleado;
            var listaCodigosTipoDesempleado = new Array();
            var listaDescripcionesTipoDesempleado = new Array();
            function buscaCodigoTipoDesempleado(tipo) {
                comboListaTipoDesempleado.buscaCodigo(tipo);
            }
            function cargarDatosTipoDesempleado() {
                var tipoSeleccionado = document.getElementById("codListaTipoDesempleado").value;
                buscaCodigoTipoDesempleado(tipoSeleccionado);
            }
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
            function guardarContratacion() {
                if (validarDatos()) {
                    elementoVisible('on', 'barraProgresoLPEEL');
                    var parametros = "";
                    if ('<%=nuevo%>' != null && '<%=nuevo%>' == "1") {
                        parametros = "tarea=preparar&modulo=MELANBIDE81&operacion=crearNuevaContratacion&tipo=0&idProyecto=<%=idProyecto%>";
                    } else {
                        parametros = "tarea=preparar&modulo=MELANBIDE81&operacion=modificarContratacion&tipo=0"
                                + "&idProyecto=<%=datModif != null && datModif.getIdProyecto() != null ? datModif.getIdProyecto().toString() : ""%>"
                                + "&id=<%=datModif != null && datModif.getId() != null ? datModif.getId().toString() : ""%>";
                    }
                    parametros += "&numExp=<%=numExpediente%>"
                            + "&prioridad=" + document.getElementById('prioridadContratacion').value
                            + "&tipoDesempleado=" + document.getElementById('codListaTipoDesempleado').value
                            + "&duracionContrato=" + document.getElementById('duracionContrato').value
                            + "&sexo=" + document.getElementById('codListaSexo').value
                            + "&porcJorn=" + document.getElementById('porcJornContratacion').value
                            + "&numContratos=" + document.getElementById('numContratos').value
                            + "&subvencion=" + document.getElementById('subvSoliContratacion').value
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

                var campo = document.getElementById('prioridadContratacion');
                if (campo.value == null || campo.value == '') {
                    mensajeValidacion = '<%=meLanbide81I18n.getMensaje(idiomaUsuario, "msg.prioridad")%>';
                    return false;
                } else if (/^([0-9])*$/.test(campo)) {
                    mensajeValidacion = '<%=meLanbide81I18n.getMensaje(idiomaUsuario, "msg.prioridad.formato")%>';
                    return false;
                }

                campo = document.getElementById('codListaTipoDesempleado');
                if (campo.value == null || campo.value == '') {
                    mensajeValidacion = '<%=meLanbide81I18n.getMensaje(idiomaUsuario, "msg.tipoDesempleado")%>';
                    return false;
                }

                campo = document.getElementById('duracionContrato');
                if (campo.value == null || campo.value == '') {
                    mensajeValidacion = '<%=meLanbide81I18n.getMensaje(idiomaUsuario, "msg.duracion")%>';
                    return false;
                } else if (!validarNumericoDecimalPrecision(campo.value, 6, 2)) {
                    mensajeValidacion = '<%=meLanbide81I18n.getMensaje(idiomaUsuario, "msg.duracion.formato")%>';
                    return false;
                }

                campo = document.getElementById('codListaSexo');
                if (campo.value == null || campo.value == '') {
                    mensajeValidacion = '<%=meLanbide81I18n.getMensaje(idiomaUsuario, "msg.sexo")%>';
                    return false;
                }

                campo = document.getElementById('porcJornContratacion');
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

                campo = document.getElementById('numContratos');
                if (campo.value == null || campo.value == '') {
                    mensajeValidacion = '<%=meLanbide81I18n.getMensaje(idiomaUsuario, "msg.numContratos")%>';
                    return false;
                } else if (/^([0-9])*$/.test(campo)) {
                    mensajeValidacion = '<%=meLanbide81I18n.getMensaje(idiomaUsuario, "msg.numContratos.formato")%>';
                    return false;
                }

                campo = document.getElementById('subvSoliContratacion');
                if (campo.value == null || campo.value == '') {
                    mensajeValidacion = '<%=meLanbide81I18n.getMensaje(idiomaUsuario, "msg.subvSoli")%>';
                    return false;
                } else if (!validarNumericoDecimalPrecision(campo.value, 9, 2)) {
                    mensajeValidacion = '<%=meLanbide81I18n.getMensaje(idiomaUsuario, "msg.subvSoli.formato")%>';
                    return false;
                }

                return true;
            }

            function rellenarDesplegables() {
                if ('<%=datModif%>' != null) {
                    buscaCodigoTipoDesempleado('<%=datModif.getTipoDesempleado() != null ? datModif.getTipoDesempleado() : ""%>');
                    buscaCodigoSexo('<%=datModif.getSexo() != null ? datModif.getSexo() : ""%>');
                }
            }
            function cancelar() {
                var resultado = jsp_alerta('', '<%=meLanbide81I18n.getMensaje(idiomaUsuario, "msg.preguntaCancelar")%>');
                if (resultado == 1) {
                    cerrarVentana();
                }
            }
            // FUNCIONES QUE PROCESAN RESPUESTA O ERROR DE LA PETICION AJAX
            function procesarRespuestaAltaModificacion(ajaxResult) {
                elementoVisible('off', 'barraProgresoLPEEL');
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
                    <legend class="legendAzul" id="titContratacion"><%=meLanbide81I18n.getMensaje(idiomaUsuario,"label.contratacion")%></legend>                        
                    <div class="lineaFormulario">
                        <div  class="etiquetaLPEEL">
                            <%=meLanbide81I18n.getMensaje(idiomaUsuario,"label.prioridad")%>
                        </div>
                        <div style="width:450px; float: left;">
                            <input id="prioridadContratacion" name="prioridadContratacion"  type="text" class="inputTextoObligatorio" size="6" maxlength="6" onkeyup="SoloDigitos(this);" 
                                   value="<%=datModif != null && datModif.getIdPrioridadProyecto() != null ? datModif.getIdPrioridadProyecto() : ""%>" disabled/>
                        </div>
                    </div>
                    <div class="lineaFormulario" style="padding-top: 40px">
                        <div  class="etiquetaLPEEL">
                            <%=meLanbide81I18n.getMensaje(idiomaUsuario,"label.tipoPers")%>
                        </div>
                        <div style="width:450px; float: left;">
                            <input type="text" name="codListaTipoDesempleado" id="codListaTipoDesempleado" size="3" class="inputComboObligatorio" value=""  maxlength="1" onkeyup="xAMayusculas(this);"/>
                            <input type="text" name="descListaTipoDesempleado"  id="descListaTipoDesempleado" size="50" class="inputCombo" readonly value="" />
                            <a href="" id="anchorListaTipoDesempleado" name="anchorListaTipoDesempleado" title="<%=meLanbide81I18n.getMensaje(idiomaUsuario,"label.tipoPers")%>">
                                <span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonAplicacion" style="cursor:pointer;"></span>
                            </a>
                        </div>
                    </div>
                    <div class="lineaFormulario" style="padding-top: 40px">
                        <div  class="etiquetaLPEEL">
                            <%=meLanbide81I18n.getMensaje(idiomaUsuario,"label.duracion")%>
                        </div>
                        <div style="width:450px; float: left;">
                            <input id="duracionContrato" name="duracionContrato" type="text" class="inputTextoObligatorio" size="10" maxlength="7" onchange="reemplazarPuntos(this);" onblur="validarNumeroReal(this);"
                                   value="<%=datModif != null && datModif.getDuracion() != null ? datModif.getDuracion().toString().replaceAll("\\.", ",") : ""%>" />
                        </div>
                    </div>
                    <div class="lineaFormulario" style="padding-top: 40px">
                        <div  class="etiquetaLPEEL">
                            <%=meLanbide81I18n.getMensaje(idiomaUsuario,"label.sexo")%>
                        </div>
                        <div style="width:450px; float: left;">
                            <input type="text" name="codListaSexo" id="codListaSexo" size="3" class="inputComboObligatorio" value="" onkeyup="SoloDigitos(this);" maxlength="1"/>
                            <input type="text" name="descListaSexo"  id="descListaSexo" size="15" class="inputCombo" readonly value="" />
                            <a href="" id="anchorListaSexo" name="anchorListaSexo" title="<%=meLanbide81I18n.getMensaje(idiomaUsuario,"label.sexo")%>">
                                <span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonAplicacion" style="cursor:pointer;"></span>
                            </a>
                        </div>
                    </div>
                    <div class="lineaFormulario" style="padding-top: 40px">
                        <div  class="etiquetaLPEEL">
                            <%=meLanbide81I18n.getMensaje(idiomaUsuario,"label.porcJorn")%>
                        </div>
                        <div style="width:450px; float: left;">
                            <input id="porcJornContratacion" name="porcJornContratacion" type="text" class="inputTextoObligatorio" size="10" maxlength="6" onchange="reemplazarPuntos(this);" onblur="validarNumeroReal(this);"
                                   value="<%=datModif != null && datModif.getPorcJorn() != null ? datModif.getPorcJorn().toString().replaceAll("\\.", ",") : ""%>" />
                        </div>
                    </div>
                    <div class="lineaFormulario" style="padding-top: 40px">
                        <div  class="etiquetaLPEEL">
                            <%=meLanbide81I18n.getMensaje(idiomaUsuario,"label.numContratosPre")%>
                        </div>
                        <div style="width:450px; float: left;">
                            <input id="numContratos" name="numContratos" type="text" class="inputTextoObligatorio" size="10" maxlength="3" onkeyup="SoloDigitos(this);" 
                                   value="<%=datModif != null && datModif.getNumContratos() != null ? datModif.getNumContratos() : ""%>" />
                        </div>
                    </div>
                    <div class="lineaFormulario" style="padding-top: 40px">
                        <div  class="etiquetaLPEEL">
                            <%=meLanbide81I18n.getMensaje(idiomaUsuario,"label.subvSoli")%>
                        </div>
                        <div style="width:450px; float: left;">
                            <input id="subvSoliContratacion" name="subvSoliContratacion" type="text" class="inputTextoObligatorio" size="10" maxlength="10" onchange="reemplazarPuntos(this);" onblur="validarNumeroReal(this);"
                                   value="<%=datModif != null && datModif.getSubvencion() != null ? datModif.getSubvencion().toString().replaceAll("\\.", ",") : ""%>" />
                        </div>
                    </div>
                    <div class="lineaFormulario" style="padding-top: 40px">
                        <div class="botonera" style="width: 100%; float: left; text-align: center;">
                            <input type="button" id="btnAceptarContratacion" name="btnAceptarContratacion" class="botonGeneral" value="<%=meLanbide81I18n.getMensaje(idiomaUsuario, "btn.aceptar")%>" onclick="guardarContratacion();"/>
                            <input type="button" id="btnCancelarContratacion" name="btnCancelarContratacion" class="botonGeneral" value="<%=meLanbide81I18n.getMensaje(idiomaUsuario, "btn.cancelar")%>" onclick="cancelar();"/>
                        </div>
                    </div>
                </div>
            </form>
            <script type="text/javascript">
                /*  TIPO */
                listaCodigosTipoDesempleado[0] = "";
                listaDescripcionesTipoDesempleado[0] = "";
                contador = 0;
                <logic:iterate id="tipoDesempleado" name="listaTipo" scope="request">
                listaCodigosTipoDesempleado[contador] = ['<bean:write name="tipoDesempleado" property="des_val_cod" />'];
                listaDescripcionesTipoDesempleado[contador] = ['<bean:write name="tipoDesempleado" property="des_nom" />'];
                contador++;
                </logic:iterate>
                var comboListaTipoDesempleado = new Combo("ListaTipoDesempleado");
                comboListaTipoDesempleado.addItems(listaCodigosTipoDesempleado, listaDescripcionesTipoDesempleado);
                comboListaTipoDesempleado.change = cargarDatosTipoDesempleado;

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

                if (<%=nuevo%> != null && <%=nuevo%> == 0) {
                    rellenarDesplegables();
                } else {
                    document.getElementById('prioridadContratacion').value = '<%=prioridad%>';
                }

            </script>
        </div>
    </body>
</html>

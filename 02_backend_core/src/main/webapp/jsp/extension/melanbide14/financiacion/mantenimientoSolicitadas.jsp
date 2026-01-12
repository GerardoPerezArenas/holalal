<%-- 
    Document   : mantenimientoSolicitadas
    Created on : 08-oct-2024, 16:48:39
    Author     : kigonzalez
--%>
<%@taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@taglib uri="/WEB-INF/tlds/c.tld" prefix="c"%>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject"%>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject"%>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide14.i18n.MeLanbide14I18n"%>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide14.util.ConfigurationParameter"%>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide14.util.ConstantesMeLanbide14"%>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide14.vo.OperacionSolicitadaVO"%>
<%@page import="java.text.SimpleDateFormat"%>
<html>
    <head>
        <meta http-equiv="X-UA-Compatible" content="IE=Edge" />
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <%
            int idiomaUsuario = 1;
            int apl = 5;
            String css = "";
            String codOrganizacion = "";
            String nuevo = "";
            String tituloPagina = "";
            String numExpediente = "";
            String numOrden ="";
            String ejerSolicitado = "";
            String fechaInicio = "";
            String fechaFin = "";
            OperacionSolicitadaVO datModif = new OperacionSolicitadaVO();
            UsuarioValueObject usuarioVO = new UsuarioValueObject();
            //Clase para internacionalizar los mensajes de la aplicación.
            MeLanbide14I18n meLanbide14I18n = MeLanbide14I18n.getInstance();
            SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");   
            
            try {
                if (session.getAttribute("usuario") != null) {
                    usuarioVO = (UsuarioValueObject) session.getAttribute("usuario");
                    apl = usuarioVO.getAppCod();
                    idiomaUsuario = usuarioVO.getIdioma();
                    css = usuarioVO.getCss();
                }                

                numExpediente = (String)request.getAttribute("numExp");
                codOrganizacion  = request.getParameter("codOrganizacionModulo");
                nuevo = (String)request.getAttribute("nuevo");
                ejerSolicitado = (String)request.getAttribute("ejerSolicitado");
                if (request.getAttribute("numOrden") != null) {
                    numOrden = (String)request.getAttribute("numOrden");
                   
                }
                if(request.getAttribute("datModif") != null) {
                    datModif = (OperacionSolicitadaVO)request.getAttribute("datModif");  
                    fechaInicio = formatoFecha.format(datModif.getFecInicio());
                    fechaFin = formatoFecha.format(datModif.getFecFin());
                }
                tituloPagina = meLanbide14I18n.getMensaje(idiomaUsuario,"label.titulo.mantenimiento");
                
            } catch(Exception ex) {}
        %>
        <jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
        <jsp:setProperty name="descriptor"  property="idi_cod" value="<%=idiomaUsuario%>"/>
        <jsp:setProperty name="descriptor"  property="apl_cod" value="<%=apl%>"/>
        <link rel="StyleSheet" media="screen" type="text/css" href="<%=request.getContextPath()%><%=css%>">
        <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/css/font-awesome.min.css'/>" media="screen">
        <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/less/animated.less'/>" media="screen">
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/>
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/extension/melanbide14/melanbide14.css"/>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/JavaScriptUtil.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/melanbide14/ExtensionUtils.js"></script>        
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/general.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/listaComboBox.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/calendario.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/popup.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/validaciones.js"></script>
        <title><%=tituloPagina%></title>
        <script type="text/javascript">
            var url = '<%=request.getContextPath()%>' + '/PeticionModuloIntegracion.do';
            var mensajeValidacion = '';
            // PRIORIDAD
            var comboPrioridad;
            var listaCodigosPrioridad = new Array();
            var listaDescripcionesPrioridad = new Array();
            function cargarDatosPrioridad() {
                var codSeleccionado = document.getElementById("codListaPrioridad").value;
                buscaCodigoDesplegable(comboPrioridad, codSeleccionado);
            }
            // OBJETIVO
            var comboObjetivo;
            var listaCodigosObjetivo = new Array();
            var listaDescripcionesObjetivo = new Array();
            function cargarDatosObjetivo() {
                var codSeleccionado = document.getElementById("codListaObjetivo").value;
                buscaCodigoDesplegable(comboObjetivo, codSeleccionado);
            }
            // TIPOLOGIA
            var comboTipologia;
            var listaCodigosTipologia = new Array();
            var listaDescripcionesTipologia = new Array();
            function cargarDatosTipologia() {
                var codSeleccionado = document.getElementById("codListaTipologia").value;
                buscaCodigoDesplegable(comboTipologia, codSeleccionado);
            }
            // ENTIDAD
            var comboEntidad;
            var listaCodigosEntidad = new Array();
            var listaDescripcionesEntidad = new Array();
            function cargarDatosEntidad() {
                var codSeleccionado = document.getElementById("codListaEntidad").value;
                buscaCodigoDesplegable(comboEntidad, codSeleccionado);
            }

            function guardarDatos() {
                if (validarDatos()) {
                    // .value.replace(/\r?\n|\r/g, " ").trim()
                    pleaseWaitSinFrame('on');
                    var parametros = 'tarea=preparar&modulo=MELANBIDE14&tipo=0'
                            + '&numExp=<%=numExpediente%>'
                            + '&ejeOperacion=<%=ejerSolicitado%>'
                            + '&numOpePre=' + document.getElementById('numOpePre').value
                            + '&prioridad=' + document.getElementById('codListaPrioridad').value
                            + '&objetivo=' + document.getElementById('codListaObjetivo').value
                            + '&tipologia=' + document.getElementById('codListaTipologia').value
                            + '&fecInicio=' + document.getElementById('fechaInicio').value
                            + '&fecFin=' + document.getElementById('fechaFin').value
                            + '&entidad=' + document.getElementById('codListaEntidad').value
                            + '&organismo=' + document.getElementById('organismo').value.replace(/\r?\n|\r/g, " ").trim()
                            + "&operacion="
                            ;
                    if (<%=nuevo%> != null && <%=nuevo%> == 1) {
                        parametros += 'altaOpeSolicitada';
                    } else {
                        parametros += "modificarOpeSolicitada&id=<%=datModif != null && datModif.getId() != null ? datModif.getId().toString() : ""%>";
                    }
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
                        pleaseWaitSinFrame('off');
                        mostrarErrorPeticion()();
                    }
                } else {
                    jsp_alerta("A", mensajeValidacion);
                }
            }

            function validarDatos() {
                mensajeValidacion = '';
                var campo = document.getElementById('codListaPrioridad');
                if (campo.value == null || campo.value == '') {
                    mensajeValidacion = '<%=meLanbide14I18n.getMensaje(idiomaUsuario, "msg.financiacion.prioridad")%>';
                    return false;
                }
                campo = document.getElementById('codListaObjetivo');
                if (campo.value == null || campo.value == '') {
                    mensajeValidacion = '<%=meLanbide14I18n.getMensaje(idiomaUsuario, "msg.financiacion.objetivo")%>';
                    return false;
                }
                campo = document.getElementById('codListaTipologia');
                if (campo.value == null || campo.value == '') {
                    mensajeValidacion = '<%=meLanbide14I18n.getMensaje(idiomaUsuario, "msg.financiacion.tipologia")%>';
                    return false;
                }
                campo = document.getElementById('fechaInicio');
                if (campo.value == null || campo.value == '') {
                    mensajeValidacion = '<%=meLanbide14I18n.getMensaje(idiomaUsuario, "msg.financiacion.fecInicio")%>';
                    return false;
                }
                campo = document.getElementById('fechaFin');
                if (campo.value == null || campo.value == '') {
                    mensajeValidacion = '<%=meLanbide14I18n.getMensaje(idiomaUsuario, "msg.financiacion.fecFin")%>';
                    return false;
                }
                campo = document.getElementById('codListaEntidad');
                if (campo.value == null || campo.value == '') {
                    mensajeValidacion = '<%=meLanbide14I18n.getMensaje(idiomaUsuario, "msg.financiacion.entidad")%>';
                    return false;
                }
                campo = document.getElementById('organismo');
                if (campo.value == null || campo.value == '') {
                    mensajeValidacion = '<%=meLanbide14I18n.getMensaje(idiomaUsuario, "msg.financiacion.organismo")%>';
                    return false;
                } else if (!compruebaTamanoCampo(campo, 250)) {
                    mensajeValidacion = '<%=meLanbide14I18n.getMensaje(idiomaUsuario, "msg.financiacion.organismo.tamano")%>';
                    return false;
                }
                return true;
            }

            function rellenarDesplegables() {
                if ('<%=datModif%>' != null) {
                    buscaCodigoDesplegable(comboPrioridad, '<%=datModif.getPrioridad() != null ? datModif.getPrioridad() : ""%>');
                    buscaCodigoDesplegable(comboObjetivo, '<%=datModif.getObjetivo() != null ? datModif.getObjetivo() : ""%>');
                    buscaCodigoDesplegable(comboTipologia, '<%=datModif.getTipologia() != null ? datModif.getTipologia() : ""%>');
                    buscaCodigoDesplegable(comboEntidad, '<%=datModif.getEntidad() != null ? datModif.getEntidad() : ""%>');
                }
            }

            function cancelar() {
                var resultado = jsp_alerta('', '<%=meLanbide14I18n.getMensaje(idiomaUsuario, "msg.preguntaCancelar")%>');
                if (resultado == 1) {
                    cerrarVentana();
                }
            }
            // FUNCIONES QUE PROCESAN RESPUESTA O ERROR DE LA PETICION AJAX
            function procesarRespuestaAltaModificacion(ajaxResult) {
                pleaseWaitSinFrame('off');
                var datos = JSON.parse(ajaxResult);
                var codigoOperacion = datos.tabla.codigoOperacion;
                if (codigoOperacion == "0") {
                    var opSolicitadas = datos.tabla.lista;
                    if (opSolicitadas.length > 0) {
                        var respuesta = new Array();
                        respuesta[0] = "0";
                        respuesta[1] = opSolicitadas;
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
                pleaseWaitSinFrame('off');
                var codigo;
                if (<%=nuevo%> != null && <%=nuevo%> == 0) {
                    codigo = "8";
                } else {
                    codigo = "7";
                }
                mostrarErrorPeticion(codigo);
            }

        </script>
    </head>
    <body class="bandaBody" onload="pleaseWaitSinFrame('off');">
        <jsp:include page="/jsp/hidepage.jsp" flush="true">
            <jsp:param name='cargaDatos' value='<%=meLanbide14I18n.getMensaje(idiomaUsuario, "msg.procesando")%>'/>
        </jsp:include>
        <div class="contenidoPantalla">
            <input type="hidden" id="errorBD" name="errorBD" value="<%=meLanbide14I18n.getMensaje(idiomaUsuario,"error.errorBD")%>"/>
            <input type="hidden" id="generico" name="generico" value="<%=meLanbide14I18n.getMensaje(idiomaUsuario,"error.errorGen")%>"/>
            <input type="hidden" id="pasoParametros" name="pasoParametros" value="<%=meLanbide14I18n.getMensaje(idiomaUsuario,"error.pasoParametros")%>"/>
            <input type="hidden" id="listaVacia" name="listaVacia" value="<%=meLanbide14I18n.getMensaje(idiomaUsuario,"error.listaVacia")%>"/>
            <input type="hidden" id="obtenerDatos" name="obtenerDatos" value="<%=meLanbide14I18n.getMensaje(idiomaUsuario,"error.obtenerDatos")%>"/>
            <input type="hidden" id="borrarDatos" name="borrarDatos" value="<%=meLanbide14I18n.getMensaje(idiomaUsuario,"error.borrarDatos")%>"/>
            <input type="hidden" id="insertarDatos" name="insertarDatos" value="<%=meLanbide14I18n.getMensaje(idiomaUsuario,"error.insertarDatos")%>"/>
            <input type="hidden" id="actualizarDatos" name="actualizarDatos" value="<%=meLanbide14I18n.getMensaje(idiomaUsuario,"error.actualizarDatos")%>"/> 
            <input type="hidden" id="operacionIncorrecta" name="operacionIncorrecta" value="<%=meLanbide14I18n.getMensaje(idiomaUsuario,"error.operacionIncorrecta")%>"/>
            <input type="hidden" id="fechaIncorrecta" name="fechaIncorrecta" value="<%=meLanbide14I18n.getMensaje(idiomaUsuario,"msg.fechaNoVal")%>"/>
            <form>
                <div style="width: 100%; padding: 10px; text-align: left;">
                    <div  class="sub3titulo" style="clear: both; text-align: center; font-size: 14px;">
                        <span id="tituloVentana"/>
                    </div>
                    <fieldset id="formSolicitadas" name="formSolicitadas">
                        <legend class="legendTema" align="center" id="titEjercicio" name="titEjercicio"style="font-size: x-large;"></legend>
                        <!-- numOpePre -->
                        <div class="lineaFormulario">
                            <div class="etiquetaPFSE"><%=meLanbide14I18n.getMensaje(idiomaUsuario,"label.operacion.numOpePre")%></div>
                            <div style="float: left;">
                                <input id="numOpePre" name="numOpePre" type="text" class="inputTexto" size="4" maxlength="4" disabled="true" value="<%=datModif != null && datModif.getNumOpePre() != null ? datModif.getNumOpePre() : ""%>"/>
                            </div>
                        </div>
                        <!-- prioridad -->
                        <div class="lineaFormulario">   
                            <div class="etiquetaPFSE"><%=meLanbide14I18n.getMensaje(idiomaUsuario,"label.operacion.prioridad")%></div>
                            <div style="float: left;">
                                <div>
                                    <input type="text" name="codListaPrioridad" id="codListaPrioridad" size="2" maxlength="1" class="inputTextoObligatorio" value="" onkeyup="SoloDigitos(this);" />
                                    <input type="text" name="descListaPrioridad" id="descListaPrioridad" size="30" class="inputComboObligatorio" readonly="true" value="" />
                                    <a href="" id="anchorListaPrioridad" name="anchorListaPrioridad">
                                        <span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonAplicacion" name="botonAplicacion" style="cursor:pointer;"></span>
                                    </a>
                                </div>
                            </div>
                        </div>
                        <!-- objetivo -->
                        <div class="lineaFormulario">  
                            <div class="etiquetaPFSE"><%=meLanbide14I18n.getMensaje(idiomaUsuario,"label.operacion.objetivo")%></div>
                            <div style="float: left;">
                                <input type="text" name="codListaObjetivo" id="codListaObjetivo" size="2" maxlength="1" class="inputTextoObligatorio" value="" onkeyup="xAMayusculas(this);" />
                                <input type="text" name="descListaObjetivo" id="descListaObjetivo" size="125" class="inputComboObligatorio" readonly="true" value="" />
                                <a href="" id="anchorListaObjetivo" name="anchorListaObjetivo">
                                    <span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonAplicacion" name="botonAplicacion" style="cursor:pointer;"></span>
                                </a>
                            </div>
                        </div>
                        <!-- tipologia -->
                        <div class="lineaFormulario">     
                            <div class="etiquetaPFSE"><%=meLanbide14I18n.getMensaje(idiomaUsuario,"label.operacion.tipologia")%></div>
                            <div style="float: left;">
                                <input type="text" name="codListaTipologia" id="codListaTipologia" size="2" maxlength="1" class="inputTextoObligatorio" value="" onkeyup="xAMayusculas(this);" />
                                <input type="text" name="descListaTipologia" id="descListaTipologia" size="60" class="inputComboObligatorio" readonly="true" value="" />
                                <a href="" id="anchorListaTipologia" name="anchorListaTipologia">
                                    <span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonAplicacion" name="botonAplicacion" style="cursor:pointer;"></span>
                                </a>
                            </div>
                        </div>                        
                        <!-- fecInicio -->
                        <div class="lineaFormulario">       
                            <div class="etiquetaPFSE"><%=meLanbide14I18n.getMensaje(idiomaUsuario,"label.operacion.fecInicio")%></div>
                            <div style="float: left;">
                                <input type="text" class="inputTxtFechaObligatorio" 
                                       id="fechaInicio" name="fechaInicio" 
                                       maxlength="10"  size="10"
                                       value="<%=fechaInicio%>"
                                       onkeyup = "return SoloCaracteresFecha(this);"
                                       onblur = "javascript:return comprobarFechaExtension(this);"
                                       onfocus="javascript:this.select();"/>
                                <A href="javascript:calClick(event);return false;" onClick='mostrarCalendarios(event, "fechaInicio", "calFechaInicio");
                                        return false;' style="text-decoration:none;">
                                    <IMG style="border: 0px solid" height="17" id="calFechaInicio" name="calFechaInicio" border="0" src="<c:url value='/images/calendario/icono.gif'/>" >
                                </A>
                            </div>
                        </div>
                        <!-- fecFin -->
                        <div class="lineaFormulario">        
                            <div class="etiquetaPFSE"><%=meLanbide14I18n.getMensaje(idiomaUsuario,"label.operacion.fecFin")%></div>
                            <div style="float: left;">
                                <input type="text" class="inputTxtFechaObligatorio" 
                                       id="fechaFin" name="fechaFin" 
                                       maxlength="10"  size="10"
                                       value="<%=fechaFin%>"
                                       onkeyup = "return SoloCaracteresFecha(this);"
                                       onblur = "javascript:return comprobarFechaExtension(this);"
                                       onfocus="javascript:this.select();"/>
                                <A href="javascript:calClick(event);return false;" onClick='mostrarCalendarios(event, "fechaFin", "calFechaFin");
                                        return false;' style="text-decoration:none;">
                                    <IMG style="border: 0px solid" height="17" id="calFechaFin" name="calFechaFin" border="0" src="<c:url value='/images/calendario/icono.gif'/>" >
                                </A>
                            </div>
                        </div>
                        <!-- entidad -->
                        <div class="lineaFormulario">     
                            <div class="etiquetaPFSE"><%=meLanbide14I18n.getMensaje(idiomaUsuario,"label.operacion.entidad")%></div>
                            <div style="float: left;">
                                <input type="text" name="codListaEntidad" id="codListaEntidad" size="2" maxlength="1" class="inputTextoObligatorio" value="" onkeyup="xAMayusculas(this);" />
                                <input type="text" name="descListaEntidad" id="descListaEntidad" size="60" class="inputComboObligatorio" readonly="true" value="" />
                                <a href="" id="anchorListaEntidad" name="anchorListaEntidad">
                                    <span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonAplicacion" name="botonAplicacion" style="cursor:pointer;"></span>
                                </a>
                            </div>
                        </div>
                        <!-- organismo -->
                        <div class="lineaFormulario">  
                            <div class="etiquetaPFSE"><%=meLanbide14I18n.getMensaje(idiomaUsuario,"label.operacion.organismo")%></div>
                            <div style="float: left;">
                                <textarea id="organismo" name="organismo" type="text" class="melanbide14_txtSinMayusculas" rows="2" cols="125"  maxlength="250"><%=datModif != null && datModif.getOrganismo() != null ? datModif.getOrganismo() : ""%></textarea>
                            </div>
                        </div>
                    </fieldset>
                    <div class="lineaFormulario">
                        <div class="botonera" style="width: 100%; float: left; text-align: center;">
                            <input type="button" id="btnAceptar" name="btnAceptar" class="botonGeneral" value="<%=meLanbide14I18n.getMensaje(idiomaUsuario, "btn.aceptar")%>" onclick="guardarDatos();"/>
                            <input type="button" id="btnCancelar" name="btnCancelar" class="botonGeneral" value="<%=meLanbide14I18n.getMensaje(idiomaUsuario, "btn.cancelar")%>" onclick="cancelar();"/>
                        </div>
                    </div>
                </div>
            </form>
            <script type="text/javascript">
                listaCodigosPrioridad[0] = "";
                listaDescripcionesPrioridad[0] = "";
                contador = 0;
                <logic:iterate id="prioridad" name="listaPrioridad" scope="request">
                listaCodigosPrioridad[contador] = ['<bean:write name="prioridad" property="des_val_cod" />'];
                listaDescripcionesPrioridad[contador] = ['<bean:write name="prioridad" property="des_nom" />'];
                contador++;
                </logic:iterate>
                comboPrioridad = new Combo("ListaPrioridad");
                comboPrioridad.addItems(listaCodigosPrioridad, listaDescripcionesPrioridad);
                comboPrioridad.change = cargarDatosPrioridad;


                listaCodigosObjetivo[0] = "";
                listaDescripcionesObjetivo[0] = "";
                contador = 0;
                <logic:iterate id="objetivo" name="listaObjetivo" scope="request">
                listaCodigosObjetivo[contador] = ['<bean:write name="objetivo" property="des_val_cod" />'];
                listaDescripcionesObjetivo[contador] = ['<bean:write name="objetivo" property="des_nom" />'];
                contador++;
                </logic:iterate>
                comboObjetivo = new Combo("ListaObjetivo");
                comboObjetivo.addItems(listaCodigosObjetivo, listaDescripcionesObjetivo);
                comboObjetivo.change = cargarDatosObjetivo;

                listaCodigosEntidad[0] = "";
                listaDescripcionesEntidad[0] = "";
                contador = 0;
                <logic:iterate id="entidad" name="listaEntidad" scope="request">
                listaCodigosEntidad[contador] = ['<bean:write name="entidad" property="des_val_cod" />'];
                listaDescripcionesEntidad[contador] = ['<bean:write name="entidad" property="des_nom" />'];
                contador++;
                </logic:iterate>
                comboEntidad = new Combo("ListaEntidad");
                comboEntidad.addItems(listaCodigosEntidad, listaDescripcionesEntidad);
                comboEntidad.change = cargarDatosEntidad;

                listaCodigosTipologia[0] = "";
                listaDescripcionesTipologia[0] = "";
                contador = 0;
                <logic:iterate id="tipologia" name="listaTipologia" scope="request">
                listaCodigosTipologia[contador] = ['<bean:write name="tipologia" property="des_val_cod" />'];
                listaDescripcionesTipologia[contador] = ['<bean:write name="tipologia" property="des_nom" />'];
                contador++;
                </logic:iterate>
                comboTipologia = new Combo("ListaTipologia");
                comboTipologia.addItems(listaCodigosTipologia, listaDescripcionesTipologia);
                comboTipologia.change = cargarDatosTipologia;


                if (<%=nuevo%> != null && <%=nuevo%> == 0) {
                    rellenarDesplegables();
                    document.getElementById('tituloVentana').innerHTML = '<%=meLanbide14I18n.getMensaje(idiomaUsuario, "label.operacion.edicion")%>';
                } else {
                    document.getElementById('tituloVentana').innerHTML = '<%=meLanbide14I18n.getMensaje(idiomaUsuario, "label.operacion.alta")%>';
                    document.getElementById('numOpePre').value = '<%=numOrden%>';
                }
                document.getElementById('titEjercicio').innerHTML = '<%=ejerSolicitado%>';
            </script>
        </div>
    </body>
</html>
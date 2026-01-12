<%-- 
    Document   : nuevoSubSolic
    Created on : 14-dic-2022, 12:22:53
    Author     : kepa
--%>
<%@taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@taglib uri="/WEB-INF/tlds/c.tld" prefix="c"%>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject"%>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide67.i18n.MeLanbide67I18n"%>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide67.vo.SubSolicVO"%>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide67.util.ConfigurationParameter"%>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide67.util.ConstantesMeLanbide67"%>
<%@page import="java.text.SimpleDateFormat"%>
<html>
    <head>
        <meta http-equiv="X-UA-Compatible" content="IE=Edge" />
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <% 
            SubSolicVO datModif = new SubSolicVO();
            String codOrganizacion = "";
            String nuevo = "";
            String tituloPagina = "";
            String numExpediente = "";
            String fecha = "";
            
            MeLanbide67I18n meLanbide67I18n = MeLanbide67I18n.getInstance();
            numExpediente = (String)request.getAttribute("numExp");
            int idiomaUsuario = 1;
            int apl = 5;
            String css = "";
            try {
                UsuarioValueObject usuario = new UsuarioValueObject();
                try {
                    if (session != null) {
                        if (usuario != null) {
                            usuario = (UsuarioValueObject) session.getAttribute("usuario");
                            idiomaUsuario = usuario.getIdioma();
                            apl = usuario.getAppCod();
                            css = usuario.getCss();
                        }
                    }
                } catch(Exception ex) {}
                codOrganizacion  = request.getParameter("codOrganizacionModulo");
                nuevo = (String)request.getAttribute("nuevo");
                if(request.getAttribute("datModif") != null) {
                    datModif = (SubSolicVO)request.getAttribute("datModif");     
                    SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
                    fecha = formatoFecha.format(datModif.getFecha());
                }
                if(nuevo=="1") {
                    tituloPagina = meLanbide67I18n.getMensaje(idiomaUsuario,"label.subSolic.alta");
                } else {
                    tituloPagina=meLanbide67I18n.getMensaje(idiomaUsuario,"label.subSolic.edicion");
                }
            } catch(Exception ex){}
        %>
        <title><%=tituloPagina%></title>
        <jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
        <jsp:setProperty name="descriptor"  property="idi_cod" value="<%=idiomaUsuario%>"/>
        <jsp:setProperty name="descriptor"  property="apl_cod" value="<%=apl%>"/>
        <link rel="StyleSheet" media="screen" type="text/css" href="<%=request.getContextPath()%><%=css%>">
        <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/css/font-awesome.min.css'/>" media="screen">
        <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/less/animated.less'/>" media="screen">
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/>
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/extension/melanbide67/melanbide67.css"/>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/melanbide67/leaukUtils.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/melanbide67/lanbide.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/JavaScriptUtil.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script>        
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/general.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/calendario.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/validaciones.js"></script>
        <script type="text/javascript" src="<c:url value='/scripts/listaComboBox.js'/>"></script>
        <script type="text/javascript">
            var APP_CONTEXT_PATH = '<%=request.getContextPath()%>';
            var url = APP_CONTEXT_PATH + '/PeticionModuloIntegracion.do';
            var mensajeValidacion = '';

            //Desplegable  estado
            var comboListaEstado;
            var listaCodigosEstado = new Array();
            var listaDescripcionesEstado = new Array();
            function buscaCodigoEstado(codEstado) {
                comboListaEstado.buscaCodigo(codEstado);
            }
            function cargarDatosEstado() {
                var codEstadoSeleccionado = document.getElementById("codListaEstado").value;
                buscaCodigoEstado(codEstadoSeleccionado);
            }

            //Funcion para el calendario de fecha 
            function mostrarCalFecha(evento) {
                if (window.event)
                    evento = window.event;
                if (document.getElementById("calFecha").src.indexOf("icono.gif") != -1)
                    showCalendar('forms[0]', 'fecha', null, null, null, '', 'calFecha', '', null, null, null, null, null, null, null, null, evento);
            }
            function comprobarFecha(inputFecha) {
                var formato = 'dd/mm/yyyy';
                if (Trim(inputFecha.value) != '') {
                    var D = ValidarFechaConFormatoLanbide(inputFecha.value, formato);
                    if (!D[0]) {
                        jsp_alerta("A", "<%=meLanbide67I18n.getMensaje(idiomaUsuario, "msg.fechaNoVal")%>");
                        document.getElementById(inputFecha.name).focus();
                        document.getElementById(inputFecha.name).select();
                        return false;
                    } else {
                        inputFecha.value = D[1];
                        return true;
                    }//if (!D[0])
                }//if (Trim(inputFecha.value)!='')
                return true;
            }

            function guardarDatos() {
                if (validarDatos()) {
                    barraProgresoLak('on', 'barraProgresoLAK');
                    var parametros = "tarea=preparar&modulo=MELANBIDE67&tipo=0"
                            + '&numExp=<%=numExpediente%>'
                            + "&estado=" + document.getElementById('codListaEstado').value
                            + '&organismo=' + document.getElementById('organismo').value
                            + '&objeto=' + document.getElementById('objeto').value
                            + "&importe=" + document.getElementById('importe').value
                            + "&fecha=" + document.getElementById('fecha').value
                            + "&operacion="
                            ;
                    if ('<%=nuevo%>' != null && '<%=nuevo%>' == "1") {
                        parametros += "crearNuevoSubSolic";
                    } else {
                        parametros += "modificarSubSolic&id=<%=datModif != null && datModif.getId() != null ? datModif.getId().toString() : ""%>";
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
                        barraProgresoLak('off', 'barraProgresoLAK');
                        mostrarError();
                    }
                } else {
                    jsp_alerta("A", mensajeValidacion);
                }
            }

            function validarDatos() {
                mensajeValidacion = "";
                // Obligatorios
                campo = document.getElementById('codListaEstado');
                if (campo.value == null || campo.value == '') {
                    mensajeValidacion = '<%=meLanbide67I18n.getMensaje(idiomaUsuario, "msg.subSolic.estado")%>';
                    return false;
                }
                campo = document.getElementById('organismo');
                if (campo.value == null || campo.value == '') {
                    mensajeValidacion = '<%=meLanbide67I18n.getMensaje(idiomaUsuario, "msg.subSolic.organismo")%>';
                    return false;
                } else if (!compruebaTamanoCampo(campo, 200)) {
                    mensajeValidacion = '<%=meLanbide67I18n.getMensaje(idiomaUsuario, "msg.subSolic.organismo.tamano")%>';
                    return false;
                }
                campo = document.getElementById('objeto');
                if (campo.value == null || campo.value == '') {
                    mensajeValidacion = '<%=meLanbide67I18n.getMensaje(idiomaUsuario, "msg.subSolic.objeto")%>';
                    return false;
                } else if (!compruebaTamanoCampo(campo, 200)) {
                    mensajeValidacion = '<%=meLanbide67I18n.getMensaje(idiomaUsuario, "msg.subSolic.objeto.tamano")%>';
                    return false;
                }
                campo = document.getElementById('importe');
                if (campo.value == null || campo.value == '') {
                    mensajeValidacion = '<%=meLanbide67I18n.getMensaje(idiomaUsuario, "msg.subSolic.importe")%>';
                    return false;
                } else if (!validarNumericoDecimalLak(campo, 8, 2)) {
                    mensajeValidacion = '<%=meLanbide67I18n.getMensaje(idiomaUsuario, "msg.subSolic.importe.errNumerico")%>';
                    return false;
                }
                campo = document.getElementById('fecha');
                if (campo.value == null || campo.value == '') {
                    mensajeValidacion = '<%=meLanbide67I18n.getMensaje(idiomaUsuario, "msg.subSolic.fecha")%>';
                    return false;
                }
                return true;
            }

            function cancelar() {
                var resultado = jsp_alerta('', '<%=meLanbide67I18n.getMensaje(idiomaUsuario, "msg.preguntaCancelar")%>');
                if (resultado == 1) {
                    cerrarVentana();
                }
            }

            function rellenarDesplegables() {
                if ('<%=datModif%>' != null) {
                    buscaCodigoEstado('<%=datModif.getEstado() != null ? datModif.getEstado() : ""%>');
                } else
                    alert('No hemos podido cargar los datos para modificar');
            }

            // FUNCIONES QUE PROCESAN RESPUESTA O ERROR DE LA PETICION AJAX
            function procesarRespuestaAltaModificacion(ajaxResult) {
                barraProgresoLak('off', 'barraProgresoLAK');
                var datos = JSON.parse(ajaxResult);
                var codigoOperacion = datos.tabla.codigoOperacion;
                if (codigoOperacion == "0") {
                    var Subvenciones = datos.tabla.lista;
                    if (Subvenciones.length > 0) {
                        var respuesta = new Array();
                        respuesta[0] = "0";
                        respuesta[1] = Subvenciones;
                        self.parent.opener.retornoXanelaAuxiliar(respuesta);
                        cerrarVentana();
                    } else {
                        mostrarError(codigoOperacion);
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
                barraProgresoLak('off', 'barraProgresoLAK');
                if (codigo == "1") {
                    jsp_alerta("A", '<%=meLanbide67I18n.getMensaje(idiomaUsuario,"error.errorBD")%>');
                } else if (codigo == "2") {
                    jsp_alerta("A", '<%=meLanbide67I18n.getMensaje(idiomaUsuario,"error.generico")%>');
                } else if (codigo == "3") {
                    jsp_alerta("A", '<%=meLanbide67I18n.getMensaje(idiomaUsuario,"error.pasoParametros")%>');
                } else if (codigo == "4") {
                    jsp_alerta("A", '<%=meLanbide67I18n.getMensaje(idiomaUsuario,"error.listaVacia")%>');
                } else if (codigo == "5") {
                    jsp_alerta("A", '<%=meLanbide67I18n.getMensaje(idiomaUsuario,"error.errorObtenerDatos")%>');
                } else if (codigo == "6") {
                    jsp_alerta("A", '<%=meLanbide67I18n.getMensaje(idiomaUsuario,"error.errorBorrarDatos")%>');
                } else if (codigo == "7") {
                    jsp_alerta("A", '<%=meLanbide67I18n.getMensaje(idiomaUsuario,"error.errorInsertarDatos")%>');
                } else if (codigo == "8") {
                    jsp_alerta("A", '<%=meLanbide67I18n.getMensaje(idiomaUsuario,"error.errorActualizarDatos")%>');
                } else if (codigo == "-1") {
                    jsp_alerta("A", '<%=meLanbide67I18n.getMensaje(idiomaUsuario,"error.operacionIncorrecta")%>');
                } else {
                    jsp_alerta("A", '<%=meLanbide67I18n.getMensaje(idiomaUsuario,"error.generico")%>');
                }
            }


        </script>
    </head>
    <body class="bandaBody" onload="javascript:{
                barraProgresoLak('off', 'barraProgresoLAK');
            }" >
        <div class="contenidoPantalla">
            <div id="barraProgresoLAK" style="visibility: hidden;">
                <div class="contenedorHidepage">
                    <div class="textoHide">
                        <span>
                            <%=meLanbide67I18n.getMensaje(idiomaUsuario, "msg.procesando")%>
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
                        <span id="tituloVentana" style="width: 95%;">
                        </span>
                    </div>
                    <fieldset id="Subenciones" name="Subenciones">
                        <div class="lineaFormulario">
                            <div class="etiquetaLak">
                                <%=meLanbide67I18n.getMensaje(idiomaUsuario,"label.subSolic.estado")%>
                            </div>
                            <div>
                                <input type="text" name="codListaEstado" id="codListaEstado" size="12" class="inputComboObligatorio" value="" onkeyup="xAMayusculas(this);" />
                                <input type="text" name="descListaEstado"  id="descListaEstado" size="60" class="inputComboObligatorio" readonly="true" value="" />
                                <a href="" id="anchorListaEstado" name="anchorListaEstado">
                                    <span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonAplicacion"
                                          name="botonAplicacion" style="cursor:pointer;"></span>
                                </a>
                            </div>
                        </div>
                        <div class="lineaFormulario">
                            <div class="etiquetaLak">
                                <%=meLanbide67I18n.getMensaje(idiomaUsuario,"label.subSolic.organismo")%>
                            </div>
                            <div style="float: left;">
                                <textarea  id="organismo" name="organismo" type="text" class="inputTextoObligatorio" rows="2" cols="100" maxlength="200" style="text-align: left"><%=datModif != null && datModif.getOrganismo() != null ? datModif.getOrganismo() : ""%></textarea>
                            </div>
                        </div>
                        <div class="lineaFormulario">
                            <div class="etiquetaLak">
                                <%=meLanbide67I18n.getMensaje(idiomaUsuario,"label.subSolic.objeto")%>
                            </div>
                            <div style="float: left;">
                                <textarea  id="objeto" name="objeto" type="text" class="inputTextoObligatorio" rows="2" cols="100"  maxlength="200" style="text-align: left"><%=datModif != null && datModif.getObjeto() != null ? datModif.getObjeto() : ""%></textarea>
                            </div>
                        </div>
                        <div class="lineaFormulario">
                            <div class="etiquetaLak">
                                <%=meLanbide67I18n.getMensaje(idiomaUsuario,"label.subSolic.importe")%>
                            </div>
                            <div style="float: left;">
                                <input id="importe" name="importe" type="text" class="inputTextoObligatorio" size="9" maxlength="9" onchange="reemplazarPuntosLak(this);"
                                       onkeyup="return soloNumerosComa(this);"
                                       value="<%=datModif != null && datModif.getImporte() != null ? datModif.getImporte().toString().replaceAll("\\.", ","): ""%>"/>
                            </div>
                        </div>
                        <div class="lineaFormulario">
                            <div class="etiquetaLak">
                                <%=meLanbide67I18n.getMensaje(idiomaUsuario,"label.subSolic.fecha")%>
                            </div>
                            <div style="float: left;">
                                <input type="text" class="inputTxtFechaObligatorio" 
                                       id="fecha" name="fecha"
                                       maxlength="10"  size="10"
                                       value="<%=fecha%>"
                                       onkeyup = "return SoloCaracteresFechaLanbide(this);"
                                       onblur = "javascript:return comprobarFecha(this);"
                                       onfocus="javascript:this.select();"/>
                                <A href="javascript:calClick(event);return false;" onClick="mostrarCalFecha(event);return false;" style="text-decoration:none;">
                                    <IMG style="border: 0px solid" height="17" id="calFecha" name="calFecha" border="0" 
                                         src="<c:url value='/images/calendario/icono.gif'/>" >
                                </A>
                            </div>
                        </div>
                    </fieldset>
                    <div class="lineaFormulario">
                        <div class="botonera" style="padding-top: 15px; text-align: center;">
                            <input type="button" id="btnAceptar" name="btnAceptar" class="botonGeneral" value="<%=meLanbide67I18n.getMensaje(idiomaUsuario, "btn.aceptar")%>" onclick="guardarDatos();"/>
                            <input type="button" id="btnCancelar" name="btnCancelar" class="botonGeneral" value="<%=meLanbide67I18n.getMensaje(idiomaUsuario, "btn.cancelar")%>" onclick="cancelar();"/>
                        </div>
                    </div>
                </div>
            </form>
            <script type="text/javascript">
                /*desplegable estado*/
                listaCodigosEstado[0] = "";
                listaDescripcionesEstado[0] = "";
                contador = 0;
                <logic:iterate id="estado" name="listaEstado" scope="request">
                listaCodigosEstado[contador] = ['<bean:write name="estado" property="des_val_cod" />'];
                listaDescripcionesEstado[contador] = ['<bean:write name="estado" property="des_nom" />'];
                contador++;
                </logic:iterate>
                comboListaEstado = new Combo("ListaEstado");
                comboListaEstado.addItems(listaCodigosEstado, listaDescripcionesEstado);
                comboListaEstado.change = cargarDatosEstado;

                if (<%=nuevo%> != null && <%=nuevo%> == 0) {
                    rellenarDesplegables();
                    document.getElementById('tituloVentana').innerHTML = '<%=meLanbide67I18n.getMensaje(idiomaUsuario, "label.subSolic.modifSubvencion")%>';
                } else {
                    document.getElementById('tituloVentana').innerHTML = '<%=meLanbide67I18n.getMensaje(idiomaUsuario, "label.subSolic.nuevaSubvencion")%>';
                }
            </script>
        </div>
    </body>
</html>

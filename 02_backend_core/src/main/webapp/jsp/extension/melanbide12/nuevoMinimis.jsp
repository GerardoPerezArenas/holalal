<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide12.i18n.MeLanbide12I18n" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide12.vo.FilaMinimisVO" %>
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
            FilaMinimisVO datModif = new FilaMinimisVO();
      
            String codOrganizacion = "";
            String nuevo = "";        
            String numExpediente = "";
            String fecha = "";
            
        
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
                    datModif = (FilaMinimisVO)request.getAttribute("datModif");     
                    SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
                    fecha = formatoFecha.format(datModif.getFecha());
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

            function guardarDatos() {
                if (validarDatos()) {
                    elementoVisible('on', 'barraProgresoPRACT');
                    var parametros = "";

                    if ('<%=nuevo%>' != null && '<%=nuevo%>' == "1") {
                        parametros = "tarea=preparar&modulo=MELANBIDE12&operacion=crearNuevoMinimis&tipo=0";
                    } else {
                        parametros = "tarea=preparar&modulo=MELANBIDE12&operacion=modificarMinimis&tipo=0"
                                + "&id=<%=datModif != null && datModif.getId() != null ? datModif.getId().toString() : ""%>";
                    }
                    parametros += '&numExp=<%=numExpediente%>'
                            + "&estado=" + document.getElementById('codListaEstado').value
                            + '&organismo=' + document.getElementById('organismo').value
                            + '&objeto=' + document.getElementById('objeto').value
                            + "&importe=" + document.getElementById('importe').value
                            + "&fecha=" + document.getElementById('fecha').value
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
                campo = document.getElementById('codListaEstado').value;
                if (campo == null || campo == '') {
                    mensajeValidacion = '<%=meLanbide12I18n.getMensaje(idiomaUsuario, "msg.estado")%>';
                    return false;
                }
                campo = document.getElementById('organismo').value;
                if (campo == null || campo == '') {
                    mensajeValidacion = '<%=meLanbide12I18n.getMensaje(idiomaUsuario, "msg.organismo")%>';
                    return false;
                }
                campo = document.getElementById('objeto').value;
                if (campo == null || campo == '') {
                    mensajeValidacion = '<%=meLanbide12I18n.getMensaje(idiomaUsuario, "msg.objeto")%>';
                    return false;
                }
                campo = document.getElementById('importe').value;
                if (campo == null || campo == '') {
                    mensajeValidacion = '<%=meLanbide12I18n.getMensaje(idiomaUsuario, "msg.importe")%>';
                    return false;
                } else if (!validarNumericoDecimalPrecision(campo, 10, 2)) {
                    mensajeValidacion = '<%=meLanbide12I18n.getMensaje(idiomaUsuario, "msg.importe.errNumerico")%>';
                    return false;
                }
                campo = document.getElementById('fecha').value;
                if (campo == null || campo == '') {
                    mensajeValidacion = '<%=meLanbide12I18n.getMensaje(idiomaUsuario, "msg.fechaSub")%>';
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
                    buscaCodigoEstado('<%=datModif.getEstado() != null ? datModif.getEstado() : ""%>');
                } else
                    alert('No hemos podido cargar los datos para modificar');
            }

            // FUNCIONES QUE PROCESAN RESPUESTA O ERROR DE LA PETICION AJAX
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
                            <%=meLanbide12I18n.getMensaje(idiomaUsuario,"label.nuevaSubvencion")%>
                        </span>
                    </div> 

                    <br><br>
                    <div class="lineaFormulario">
                        <div class="etiqueta" style="width: 250px; float: left;">
                            <%=meLanbide12I18n.getMensaje(idiomaUsuario,"label.estado")%>
                        </div>
                        <div>
                            <div>
                                <input type="text" name="codListaEstado" id="codListaEstado" size="12" class="inputTexto" value="" onkeyup="xAMayusculas(this);" />
                                <input type="text" name="descListaEstado"  id="descListaEstado" size="60" class="inputTexto" readonly="true" value="" />
                                <a href="" id="anchorListaEstado" name="anchorListaEstado">
                                    <span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonAplicacion"
                                          name="botonAplicacion" style="cursor:hand;"></span>
                                </a>
                            </div>
                        </div>
                    </div>

                    <div class="lineaFormulario">
                        <div style="width: 250px; float: left;" class="etiqueta">
                            <%=meLanbide12I18n.getMensaje(idiomaUsuario,"label.organismo")%>
                        </div>
                        <div>
                            <div style="float: left;">
                                <input id="organismo" name="organismo" type="text" class="inputTexto" size="100" maxlength="100" 
                                       value="<%=datModif != null && datModif.getOrganismo() != null ? datModif.getOrganismo() : ""%>"/>
                            </div>
                        </div>
                    </div>

                    <br><br>
                    <div class="lineaFormulario">
                        <div style="width: 250px; float: left;" class="etiqueta">
                            <%=meLanbide12I18n.getMensaje(idiomaUsuario,"label.objeto")%>
                        </div>
                        <div>
                            <div style="float: left;">
                                <input id="objeto" name="objeto" type="text" class="inputTexto" size="100" maxlength="50" 
                                       value="<%=datModif != null && datModif.getObjeto() != null ? datModif.getObjeto() : ""%>"/>
                            </div>
                        </div>
                    </div>

                    <br><br>
                    <div class="lineaFormulario">
                        <div style="width: 250px; float: left;" class="etiqueta">
                            <%=meLanbide12I18n.getMensaje(idiomaUsuario,"label.importe")%>
                        </div>
                        <div>
                            <div style="float: left;">
                                <input id="importe" name="importe" type="text" class="inputTexto" size="25" maxlength="10" 
                                       value="<%=datModif != null && datModif.getImporte() != null ? datModif.getImporte().toString().replaceAll("\\.", ","): ""%>" onchange="reemplazarPuntos(this);"/>
                            </div>
                        </div>
                    </div>        

                    <br><br>
                    <div class="lineaFormulario">
                        <div style="width: 250px; float: left;" class="etiqueta">
                            <%=meLanbide12I18n.getMensaje(idiomaUsuario,"label.fecha")%>
                        </div>
                        <div>
                            <div style="float: left;">
                                <input type="text" class="inputTxtFecha" 
                                       id="fecha" name="fecha"
                                       maxlength="10"  size="10"
                                       value="<%=fecha%>"
                                       onkeyup = "return SoloCaracteresFechaLanbide(this);"
                                       onblur = "javascript:return comprobarFechaLanbide(this);"
                                       onfocus="javascript:this.select();"/>
                                <A href="javascript:calClick(event);return false;" onClick="mostrarCalFecha(event);return false;" style="text-decoration:none;">
                                    <IMG style="border: 0px solid none" height="17" id="calFecha" name="calFecha" border="0" 
                                         src="<c:url value='/images/calendario/icono.gif'/>" > <!--width="25"-->
                                </A>
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
                }

            </script>
            <div id="popupcalendar" class="text"></div>        
        </div>
    </body>
</html> 

<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide14.i18n.MeLanbide14I18n" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide14.vo.OperacionVO" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide14.vo.DesplegableAdmonLocalVO" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide14.util.ConstantesMeLanbide14" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide14.util.ConfigurationParameter" %>
<%@page import="java.util.ArrayList" %>
<%@page import="java.util.List" %>
<html>
    <head>
        <meta http-equiv="X-UA-Compatible" content="IE=Edge" />
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <%
            OperacionVO datModif = new OperacionVO();
      
            String codOrganizacion = "";
            String nuevo = "";        
            String numExpediente = "";
            
            MeLanbide14I18n meLanbide14I18n = MeLanbide14I18n.getInstance();

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
                catch(Exception ex){}

                codOrganizacion  = request.getParameter("codOrganizacionModulo");
                nuevo = (String)request.getAttribute("nuevo");
                if(request.getAttribute("datModif") != null)
                {
                    datModif = (OperacionVO)request.getAttribute("datModif");    
                }
            }
            catch(Exception ex){}
        %>
        <jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
        <jsp:setProperty name="descriptor"  property="idi_cod" value="<%=idiomaUsuario%>" />
        <jsp:setProperty name="descriptor"  property="apl_cod" value="<%=apl%>" />
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
        <script type="text/javascript" src="<c:url value='/scripts/listaComboBox.js'/>"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/validaciones.js"></script>
        <script type="text/javascript">
            var APP_CONTEXT_PATH = '<%=request.getContextPath()%>';
            var url = APP_CONTEXT_PATH + '/PeticionModuloIntegracion.do';
            var mensajeValidacion = '';

            // Desplegable PRIO
            var comboListaPrio;
            var listaCodigosPrio = new Array();
            var listaDescripcionesPrio = new Array();
            function buscaCodigoPrio(codPrio) {
                comboListaPrio.buscaCodigo(codPrio);
            }
            function cargarDatosPrio() {
                var codPrioSeleccionado = document.getElementById("codListaPrio").value;
                buscaCodigoPrio(codPrioSeleccionado);
            }

            function rellenarDesplegables() {
                if ('<%=datModif%>' != null) {
                    buscaCodigoPrio('<%=datModif.getPrio() != null ? datModif.getPrio() : ""%>');
                    buscaCodigoLin1('<%=datModif.getLin1() != null ? datModif.getLin1() : ""%>');
                    buscaCodigoLin2('<%=datModif.getLin2() != null ? datModif.getLin2() : ""%>');
                    buscaCodigoLin3('<%=datModif.getLin3() != null ? datModif.getLin3() : ""%>');
                } else
                    alert('No hemos podido cargar los datos para modificar');
            }


            var comboListaLin1;
            var listaCodigosLin1 = new Array();
            var listaDescripcionesLin1 = new Array();
            function buscaCodigoLin1(codLin1) {
                comboListaLin1.buscaCodigo(codLin1);
            }
            function cargarDatosLin1() {
                var codLin1Seleccionado = document.getElementById("codListaLin1").value;
                buscaCodigoLin1(codLin1Seleccionado);
            }

            var comboListaLin2;
            var listaCodigosLin2 = new Array();
            var listaDescripcionesLin2 = new Array();
            function buscaCodigoLin2(codLin2) {
                comboListaLin2.buscaCodigo(codLin2);
            }
            function cargarDatosLin2() {
                var codLin2Seleccionado = document.getElementById("codListaLin2").value;
                buscaCodigoLin2(codLin2Seleccionado);
            }

            var comboListaLin3;
            var listaCodigosLin3 = new Array();
            var listaDescripcionesLin3 = new Array();

            function buscaCodigoLin3(codLin3) {
                comboListaLin3.buscaCodigo(codLin3);
            }
            function cargarDatosLin3() {
                var codLin3Seleccionado = document.getElementById("codListaLin3").value;
                buscaCodigoLin3(codLin3Seleccionado);
            }

            function guardarDatos() {
                if (validarDatos()) {
                    elementoVisible('on', 'barraProgresoGO');
                    var parametros = "";

                    if ('<%=nuevo%>' != null && '<%=nuevo%>' == "1") {
                        parametros = "tarea=preparar&modulo=MELANBIDE14&operacion=crearNuevaOperacion&tipo=0";
                    } else {
                        parametros = "tarea=preparar&modulo=MELANBIDE14&operacion=modificarOperacion&tipo=0"
                                + "&id=" + '<%=datModif != null && datModif.getId() != null ? datModif.getId().toString() : ""%>';
                    }
                    parametros += "&numExp=" + "<%=numExpediente%>"
                            + "&numOper=" + document.getElementById('numOper').value
                            + "&nombreOper=" + document.getElementById('nombreOper').value
                            + "&prio=" + document.getElementById('codListaPrio').value
                            + "&lin1=" + document.getElementById('codListaLin1').value
                            + "&lin2=" + document.getElementById('codListaLin2').value
                            + "&lin3=" + document.getElementById('codListaLin3').value
                            + "&impOper=" + document.getElementById('impOper').value;

                    try {
                        $.ajax({
                            url: url,
                            type: 'POST',
                            async: true,
                            data: parametros,
                            success: procesarRespuestaAltaOperacion,
                            error: mostrarErrorAltaOperacion
                        });
                    } catch (Err) {
                        elementoVisible('off', 'barraProgresoGO');
                        mostrarError();
                    }
                } else {
                    jsp_alerta("A", mensajeValidacion);
                }
            }

            function validarDatos() {
                mensajeValidacion = "";
                var campo = document.getElementById('numExp').value;
                if (campo == null || campo == '') {
                    mensajeValidacion = '<%=meLanbide14I18n.getMensaje(idiomaUsuario, "msg.num_exp")%>';
                    return false;
                }
                campo = document.getElementById('numOper').value;
                if (campo == '' || isNaN(campo)) {
                    mensajeValidacion = '<%=meLanbide14I18n.getMensaje(idiomaUsuario, "msg.numoper")%>';
                    return false;
                }
                campo = document.getElementById('nombreOper').value;
                if (campo == null || campo == '') {
                    mensajeValidacion = '<%=meLanbide14I18n.getMensaje(idiomaUsuario, "msg.nombreoper")%>';
                    return false;
                }
                campo = document.getElementById('impOper').value;
                if (campo == '' || isNaN(campo)) {
                    mensajeValidacion = '<%=meLanbide14I18n.getMensaje(idiomaUsuario, "msg.impoper")%>';
                    return false;
                }

                return true;
            }

            function cancelar() {
                var resultado = jsp_alerta('', '<%=meLanbide14I18n.getMensaje(idiomaUsuario, "msg.preguntaCancelar")%>');
                if (resultado == 1) {
                    cerrarVentana();
                }
            }

            // FUNCIONES QUE PROCESAN RESPUESTA O ERROR DE LA PETICION AJAX
            function procesarRespuestaAltaOperacion(ajaxResult) {
                elementoVisible('off', 'barraProgresoGO');
                var datos = JSON.parse(ajaxResult);
                var codigoOperacion = datos.tabla.codigoOperacion;
                if (codigoOperacion == "0") {
                    var accesos = datos.tabla.lista;
                    if (accesos.length > 0) {
                        var respuesta = new Array();
                        respuesta[0] = "0";
                        respuesta[1] = accesos;
                        self.parent.opener.retornoXanelaAuxiliar(respuesta);
                        cerrarVentana();
                    } else {
                        mostrarError("5");
                    }
                } else {
                    mostrarError(codigoOperacion);
                }
            }

            function mostrarErrorAltaOperacion() {
                var codigo;
                if (<%=nuevo%> != null && <%=nuevo%> == 0) {
                    codigo = "8";
                } else {
                    codigo = "7";
                }
                mostrarError(codigo);
            }

            function mostrarError(codigo) {
                elementoVisible('off', 'barraProgresoGO');
                if (codigo == "1") {
                    jsp_alerta("A", '<%=meLanbide14I18n.getMensaje(idiomaUsuario,"error.errorBD")%>');
                } else if (codigo == "2") {
                    jsp_alerta("A", '<%=meLanbide14I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                } else if (codigo == "3") {
                    jsp_alerta("A", '<%=meLanbide14I18n.getMensaje(idiomaUsuario,"error.pasoParametros")%>');
                } else if (codigo == "4") {
                    jsp_alerta("A", '<%=meLanbide14I18n.getMensaje(idiomaUsuario,"error.listaVacia")%>');
                } else if (codigo == "5") {
                    jsp_alerta("A", '<%=meLanbide14I18n.getMensaje(idiomaUsuario,"error.obtenerDatos")%>');
                } else if (codigo == "6") {
                    jsp_alerta("A", '<%=meLanbide14I18n.getMensaje(idiomaUsuario,"error.borrarDatos")%>');
                } else if (codigo == "7") {
                    jsp_alerta("A", '<%=meLanbide14I18n.getMensaje(idiomaUsuario,"error.insertarDatos")%>');
                } else if (codigo == "8") {
                    jsp_alerta("A", '<%=meLanbide14I18n.getMensaje(idiomaUsuario,"error.actualizarDatos")%>');
                } else if (codigo == "-1") {
                    jsp_alerta("A", '<%=meLanbide14I18n.getMensaje(idiomaUsuario,"error.operacionIncorrecta")%>');
                } else {
                    jsp_alerta("A", '<%=meLanbide14I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                }
            }

        </script>
    </head>
    <body class="bandaBody box" onload="javascript:{
                elementoVisible('off', 'barraProgresoGO');
            }" >
        <div style="width: 100%" class="contenidoPantalla">
            <div id="barraProgresoGO" style="visibility: hidden">
                <div class="contenedorHidepage">
                    <div class="textoHide">
                        <span>
                            <%=meLanbide14I18n.getMensaje(idiomaUsuario, "msg.procesando")%>
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
                        <span id="tituloVentana" style="width: 98%;">      
                            <%=nuevo != null && nuevo=="1" ? meLanbide14I18n.getMensaje(idiomaUsuario,"label.nuevaOperacion"):meLanbide14I18n.getMensaje(idiomaUsuario,"label.modifOperacion")%>
                        </span>
                    </div>


                    <input id="numExp" name="numExp" type="hidden" class="inputTexto" size="20" maxlength="20" 
                           value="<%=numExpediente%>" disabled="true"/>

                    <div class="lineaFormulario">
                        <div style="width: 250px; float: left;" class="etiqueta">
                            <%=meLanbide14I18n.getMensaje(idiomaUsuario,"label.numoper")%>
                        </div>
                        <div>
                            <div style="float: left;">
                                <input id="numOper" name="numOper" type="number" class="inputTexto" style="height:28px" min="0" max="9999" size="4" maxlength="4"  
                                       value="<%=datModif != null && datModif.getNumOper() != null ? datModif.getNumOper().toString() : ""%>" />
                            </div>
                        </div>
                    </div>

                    <br><br>
                    <div class="lineaFormulario">
                        <div style="width: 250px; float: left;" class="etiqueta">
                            <%=meLanbide14I18n.getMensaje(idiomaUsuario,"label.nombreoper")%>
                        </div>
                        <div>
                            <div style="float: left;">
                                <input id="nombreOper" name="nombreOper" type="text" class="inputTexto" size="65" maxlength="100" 
                                       value="<%=datModif != null && datModif.getNombreOper() != null ? datModif.getNombreOper() : ""%>"/>
                            </div>
                        </div>
                    </div>

                    <br><br>
                    <div class="lineaFormulario">
                        <div style="width: 250px; float: left;" class="etiqueta">
                            <%=meLanbide14I18n.getMensaje(idiomaUsuario,"label.prioobj")%>
                        </div>
                        <div>
                            <div style="float: left;">
                                <input type="text" name="codListaPrio" id="codListaPrio" size="3" maxlength="2" class="inputTexto" value="" onkeyup="SoloDigitos(this);">
                                <input type="text" name="descListaPrio"  id="descListaPrio" size="65" class="inputTexto" readonly="true" value="">
                                <a href="" id="anchorListaPrio" name="anchorListaPrio">
                                    <span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonAplicacionPrio"
                                          name="botonAplicacionPrio" style="cursor:hand;"></span>
                                </a>                                 
                            </div>
                        </div>
                    </div>        

                    <br><br>
                    <div class="lineaFormulario">
                        <div style="width: 250px; float: left;" class="etiqueta">
                            <%=meLanbide14I18n.getMensaje(idiomaUsuario,"label.linact1")%>
                        </div>
                        <div>
                            <div style="float: left;">
                                <input type="text" name="codListaLin1" id="codListaLin1" size="3" maxlength="3" class="inputTexto" value="">
                                <input type="text" name="descListaLin1"  id="descListaLin1" size="65" class="inputTexto" readonly="true" value="">
                                <a href="" id="anchorListaLin1" name="anchorListaLin1">
                                    <span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonAplicacionLin1"
                                          name="botonAplicacionLin1" style="cursor:hand;"></span>
                                </a>                                
                            </div>
                        </div>
                    </div>        

                    <br><br>
                    <div class="lineaFormulario">
                        <div style="width: 250px; float: left;" class="etiqueta">
                            <%=meLanbide14I18n.getMensaje(idiomaUsuario,"label.linact2")%>
                        </div>
                        <div>
                            <div style="float: left;">
                                <input type="text" name="codListaLin2" id="codListaLin2" size="3" maxlength="4" class="inputTexto" value="">
                                <input type="text" name="descListaLin2"  id="descListaLin2" size="65" class="inputTexto" readonly="true" value="">
                                <a href="" id="anchorListaLin2" name="anchorListaLin2">
                                    <span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonAplicacionLin2"
                                          name="botonAplicacionLin2" style="cursor:hand;"></span>
                                </a>                               
                            </div>
                        </div>
                    </div>        

                    <br><br>
                    <div class="lineaFormulario">
                        <div style="width: 250px; float: left;" class="etiqueta">
                            <%=meLanbide14I18n.getMensaje(idiomaUsuario,"label.linact3")%>
                        </div>
                        <div>
                            <div style="float: left;">
                                <input type="text" name="codListaLin3" id="codListaLin3" size="3" maxlength="4" class="inputTexto" value="">
                                <input type="text" name="descListaLin3" id="descListaLin3" size="65" class="inputTexto" readonly="true" value="">
                                <a href="" id="anchorListaLin3" name="anchorListaLin3">
                                    <span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonAplicacionLin3"
                                          name="botonAplicacionLin3" style="cursor:hand;"></span>
                                </a>
                            </div>
                        </div>
                    </div>        

                    <br><br>
                    <div class="lineaFormulario">
                        <div style="width: 250px; float: left;" class="etiqueta">
                            <%=meLanbide14I18n.getMensaje(idiomaUsuario,"label.impoper")%>
                        </div>
                        <div>
                            <div style="float: left;">
                                <input id="impOper" name="impOper" type="number" class="inputTexto" style="height:28px" min="0.00" max="9999999999.99" step=".01"
                                       value="<%=datModif != null && datModif.getImpOper() != null ? datModif.getImpOper() : ""%>"/>
                            </div>
                        </div>
                    </div>        

                    <br><br><br><br>

                    <div class="lineaFormulario">
                        <div class="botonera" style="width: 100%; float: left; text-align: center;">
                            <input type="button" id="btnAceptar" name="btnAceptar" class="botonGeneral" value="<%=meLanbide14I18n.getMensaje(idiomaUsuario, "btn.aceptar")%>" onclick="guardarDatos();"/>
                            <input type="button" id="btnCancelar" name="btnCancelar" class="botonGeneral" value="<%=meLanbide14I18n.getMensaje(idiomaUsuario, "btn.cancelar")%>" onclick="cancelar();"/>
                        </div>
                    </div>
                </div>
            </form>
            <script type="text/javascript">
                listaCodigosPrio[0] = "";
                listaDescripcionesPrio[0] = "";
                contador = 0;
                <logic:iterate id="prio" name="listaPrio" scope="request">
                listaCodigosPrio[contador] = ['<bean:write name="prio" property="des_val_cod" />'];
                listaDescripcionesPrio[contador] = ['<bean:write name="prio" property="des_nom" />'];
                contador++;
                </logic:iterate>
                comboListaPrio = new Combo("ListaPrio");
                comboListaPrio.addItems(listaCodigosPrio, listaDescripcionesPrio);
                comboListaPrio.change = cargarDatosPrio;

                listaCodigosLin1[0] = "";
                listaDescripcionesLin1[0] = "";
                contador = 0;
                <logic:iterate id="lin1" name="listaLin1" scope="request">
                listaCodigosLin1[contador] = ['<bean:write name="lin1" property="des_val_cod" />'];
                listaDescripcionesLin1[contador] = ['<bean:write name="lin1" property="des_nom" />'];
                contador++;
                </logic:iterate>
                comboListaLin1 = new Combo("ListaLin1");
                comboListaLin1.addItems(listaCodigosLin1, listaDescripcionesLin1);
                comboListaLin1.change = cargarDatosLin1;

                listaCodigosLin2[0] = "";
                listaDescripcionesLin2[0] = "";
                contador = 0;
                <logic:iterate id="lin2" name="listaLin2" scope="request">
                listaCodigosLin2[contador] = ['<bean:write name="lin2" property="des_val_cod" />'];
                listaDescripcionesLin2[contador] = ['<bean:write name="lin2" property="des_nom" />'];
                contador++;
                </logic:iterate>
                comboListaLin2 = new Combo("ListaLin2");
                comboListaLin2.addItems(listaCodigosLin2, listaDescripcionesLin2);
                comboListaLin2.change = cargarDatosLin2;

                listaCodigosLin3[0] = "";
                listaDescripcionesLin3[0] = "";
                contador = 0;
                <logic:iterate id="lin3" name="listaLin3" scope="request">
                listaCodigosLin3[contador] = ['<bean:write name="lin3" property="des_val_cod" />'];
                listaDescripcionesLin3[contador] = ['<bean:write name="lin3" property="des_nom" />'];
                contador++;
                </logic:iterate>
                comboListaLin3 = new Combo("ListaLin3");
                comboListaLin3.addItems(listaCodigosLin3, listaDescripcionesLin3);
                comboListaLin3.change = cargarDatosLin3;
                
                if (<%=nuevo%> != null && <%=nuevo%> == 0) {
                    rellenarDesplegables();
                }
            </script>     
        </div>
    </body>
</html> 

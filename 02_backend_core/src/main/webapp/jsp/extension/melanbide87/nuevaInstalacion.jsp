<%@taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide87.i18n.MeLanbide87I18n" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide87.vo.InstalacionVO" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide87.vo.DesplegableAdmonLocalVO" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide87.util.ConstantesMeLanbide87" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide87.util.ConfigurationParameter" %>
<%@page import="java.util.ArrayList" %>
<%@page import="java.util.List" %>
<%@page import="java.text.SimpleDateFormat" %>
<html>
    <head>
        <meta http-equiv="X-UA-Compatible" content="IE=Edge" />
        <%
            InstalacionVO datModif = new InstalacionVO();
      
            String codOrganizacion = "";
            String nuevo = "";        
            String expediente = "";        
        
            MeLanbide87I18n meLanbide87I18n = MeLanbide87I18n.getInstance();

            expediente = (String)request.getAttribute("numExp");
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
                    datModif = (InstalacionVO)request.getAttribute("datModif");                 
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
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/extension/melanbide87/melanbide87.css"/>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/JavaScriptUtil.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script>        
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/general.js"></script>
        <script type="text/javascript" src="<c:url value='/scripts/listaComboBox.js'/>"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/validaciones.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/popup.js"></script>
        <script type="text/javascript">
            var APP_CONTEXT_PATH = '<%=request.getContextPath()%>';
            var url = APP_CONTEXT_PATH + '/PeticionModuloIntegracion.do';
            var mensajeValidacion = '';

            //Desplegable tipo instalacion
            var comboListaTipoInst;
            var listaCodigosTipoInst = new Array();
            var listaDescripcionesTipoInst = new Array();
            function buscaCodigoTipoInst(tipo) {
                comboListaTipoInst.buscaCodigo(tipo);
            }
            function cargarDatosTipoInst() {
                var tipoSeleccionado = document.getElementById("codListaTipoInst").value;
                buscaCodigoTipoInst(tipoSeleccionado);
            }

            function guardarDatos() {
                if (validarDatos()) {
                    elementoVisible('on', 'barraProgresoCOLVU');
                    var parametros = "";

                    if ('<%=nuevo%>' != null && '<%=nuevo%>' == "1") {
                        parametros = "tarea=preparar&modulo=MELANBIDE87&operacion=crearNuevaInstalacion&tipo=0";
                    } else {
                        parametros = "tarea=preparar&modulo=MELANBIDE87&operacion=modificarInstalacion&tipo=0"
                                + "&id=<%=datModif != null && datModif.getId() != null ? datModif.getId().toString() : ""%>";
                    }
                    parametros += '&expediente=<%=expediente%>'
                            + "&tipoInst=" + document.getElementById('codListaTipoInst').value
                            + "&localidad=" + document.getElementById('localidad').value
                            + "&municipio=" + document.getElementById('municipio').value
                            + "&direccion=" + document.getElementById('direccion').value
                            + "&codPost=" + document.getElementById('codPost').value
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
                        elementoVisible('off', 'barraProgresoCOLVU');
                        mostrarError();
                    }
                } else {
                    jsp_alerta("A", mensajeValidacion);
                }
            }

            function validarDatos() {
                mensajeValidacion = "";

                var campo = document.getElementById('codListaTipoInst').value;
                if (campo == null || campo == '') {
                    mensajeValidacion = '<%=meLanbide87I18n.getMensaje(idiomaUsuario, "msg.tipoInst")%>';
                    return false;
                }

                campo = document.getElementById('municipio').value;
                if (campo == null || campo == '') {
                    mensajeValidacion = '<%=meLanbide87I18n.getMensaje(idiomaUsuario, "msg.municipio")%>';
                    return false;
                }

                campo = document.getElementById('localidad').value;
                if (campo == null || campo == '') {
                    mensajeValidacion = '<%=meLanbide87I18n.getMensaje(idiomaUsuario, "msg.localidad")%>';
                    return false;
                }

                campo = document.getElementById('direccion').value;
                if (campo == null || campo == '') {
                    mensajeValidacion = '<%=meLanbide87I18n.getMensaje(idiomaUsuario, "msg.direccion")%>';
                    return false;
                }

                campo = document.getElementById('codPost');
                if (campo.value == null || campo.value == '') {
                    mensajeValidacion = '<%=meLanbide87I18n.getMensaje(idiomaUsuario, "msg.codPost")%>';
                    return false;
                } else if (/^([0-9])*$/.test(campo)) {
                    mensajeValidacion = '<%=meLanbide87I18n.getMensaje(idiomaUsuario, "msg.codPost.format")%>';
                    return false;
                }
                return true;
            }

            function cancelar() {
                var resultado = jsp_alerta('', '<%=meLanbide87I18n.getMensaje(idiomaUsuario, "msg.preguntaCancelar")%>');
                if (resultado == 1) {
                    cerrarVentana();
                }
            }

            function rellenardatModificar() {
                if ('<%=datModif%>' != null) {
                    buscaCodigoTipoInst('<%=datModif.getTipoInst() != null ? datModif.getTipoInst() : ""%>')
                } else
                    alert('No hemos podido cargar los datos para modificar');
            }

            // FUNCIONES QUE PROCESAN RESPUESTA O ERROR DE LA PETICION AJAX
            function procesarRespuestaAltaModificacion(ajaxResult) {
                elementoVisible('off', 'barraProgresoCOLVU');
                var datos = JSON.parse(ajaxResult);
                var codigoOperacion = datos.tabla.codigoOperacion;
                var direccion;
                if (codigoOperacion == "0") {
                    var Instalacions = datos.tabla.lista;
                    if (Instalacions.length > 0) {
                        var respuesta = new Array();
                        respuesta[0] = "0";
                        respuesta[1] = Instalacions;
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
                elementoVisible('off', 'barraProgresoCOLVU');
                if (codigo == "1") {
                    jsp_alerta("A", '<%=meLanbide87I18n.getMensaje(idiomaUsuario,"error.errorBD")%>');
                } else if (codigo == "2") {
                    jsp_alerta("A", '<%=meLanbide87I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                } else if (codigo == "3") {
                    jsp_alerta("A", '<%=meLanbide87I18n.getMensaje(idiomaUsuario,"error.pasoParametros")%>');
                } else if (codigo == "4") {
                    jsp_alerta("A", '<%=meLanbide87I18n.getMensaje(idiomaUsuario,"error.listaVacia")%>');
                } else if (codigo == "5") {
                    jsp_alerta("A", '<%=meLanbide87I18n.getMensaje(idiomaUsuario,"error.errorObtenerDatos")%>');
                } else if (codigo == "6") {
                    jsp_alerta("A", '<%=meLanbide87I18n.getMensaje(idiomaUsuario,"error.errorBorrarDatos")%>');
                } else if (codigo == "7") {
                    jsp_alerta("A", '<%=meLanbide87I18n.getMensaje(idiomaUsuario,"error.errorInsertarDatos")%>');
                } else if (codigo == "8") {
                    jsp_alerta("A", '<%=meLanbide87I18n.getMensaje(idiomaUsuario,"error.errorActualizarDatos")%>');
                } else if (codigo == "-1") {
                    jsp_alerta("A", '<%=meLanbide87I18n.getMensaje(idiomaUsuario,"error.operacionIncorrecta")%>');
                } else {
                    jsp_alerta("A", '<%=meLanbide87I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                }
            }

            function elementoVisible(valor, idBarra) {
                if (valor == 'on') {
                    document.getElementById(idBarra).style.visibility = 'inherit';
                } else if (valor == 'off') {
                    document.getElementById(idBarra).style.visibility = 'hidden';
                }
            }

            function cerrarVentana() {
                if (navigator.appName == "Microsoft Internet Explorer") {
                    window.parent.window.opener = null;
                    window.parent.window.close();
                } else if (navigator.appName == "Netscape") {
                    top.window.opener = top;
                    top.window.open('', '_parent', '');
                    top.window.close();
                } else {
                    window.close();
                }
            }
        </script>
    </head>
    <body class="bandaBody" onload="javascript:{
                elementoVisible('off', 'barraProgresoCOLVU');
            }" >
        <div class="contenidoPantalla">
            <div id="barraProgresoCOLVU" style="visibility: hidden;">
                <div class="contenedorHidepage">
                    <div class="textoHide">
                        <span>
                            <%=meLanbide87I18n.getMensaje(idiomaUsuario, "msg.procesando")%>
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
                        </span>
                    </div>
                    <fieldset id="Instalacionles" name="Instalacionles">
                        <div class="lineaFormulario">
                            <div style="width: 150px; float: left;  margin-left: 10px; text-align: left;" class="etiqueta">
                                <%=meLanbide87I18n.getMensaje(idiomaUsuario,"label.tipoInst")%>
                            </div>
                            <div style=" float: left; margin-left: 10px;">
                                <input type="text" name="codListaTipoInst" id="codListaTipoInst" size="1" class="inputComboObligatorio" value="" onkeyup="xAMayusculas(this);" maxlength="2"/>
                                <input type="text" name="descListaTipoInst"  id="descListaTipoInst" size="15" class="inputComboObligatorio" readonly value="" />
                                <a href="" id="anchorListaTipoInst" name="anchorListaTipoInst">
                                    <span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonAplicacion"
                                          style="cursor:hand;"></span>
                                </a>
                            </div>
                        </div> 
                        <br><br>
                        <div class="lineaFormulario">
                            <div style="width: 150px; float: left;  margin-left: 10px; text-align: left;" class="etiqueta">
                                <%=meLanbide87I18n.getMensaje(idiomaUsuario,"label.municipio")%>
                            </div>
                            <div style="width: 300px; float: left; margin-left: 10px;">
                                <input id="municipio" name="municipio" type="text" class="inputTextoObligatorio" size="30" maxlength="150" onkeyup="xAMayusculas(this);"
                                       value="<%=datModif != null && datModif.getMunicipio() != null ? datModif.getMunicipio() : ""%>"/>
                            </div>
                        </div> 
                        <br><br>
                        <div class="lineaFormulario"> 
                            <div style="width: 150px; float: left;  margin-left: 10px; text-align: left;" class="etiqueta">
                                <%=meLanbide87I18n.getMensaje(idiomaUsuario,"label.localidad")%>
                            </div>
                            <div style="width: 300px; float: left; margin-left: 10px;">
                                <input id="localidad" name="localidad" type="text" class="inputTextoObligatorio" size="30" maxlength="150" onkeyup="xAMayusculas(this);"
                                       value="<%=datModif != null && datModif.getLocalidad() != null ? datModif.getLocalidad() : ""%>"/>
                            </div>
                        </div>
                        <br><br>                            
                        <div class="lineaFormulario">
                            <div style="width: 150px; float: left;  margin-left: 10px; text-align: left;" class="etiqueta">
                                <%=meLanbide87I18n.getMensaje(idiomaUsuario,"label.direccion")%>
                            </div>
                            <div style="width: 300px; float: left;  margin-left: 10px;">
                                <input id="direccion" name="direccion" type="text" class="inputTextoObligatorio" size="30" maxlength="200" 
                                       value="<%=datModif != null && datModif.getDireccion() != null ? datModif.getDireccion() : ""%>"/>
                            </div>
                        </div>       
                        <br><br>
                        <div class="lineaFormulario">
                            <div style="width: 150px; float: left;  margin-left: 10px; text-align: left;" class="etiqueta">
                                <%=meLanbide87I18n.getMensaje(idiomaUsuario,"label.codPost")%>
                            </div>
                            <div style="width: 300px;  margin-left: 10px;float: left;">
                                <input id="codPost" name="codPost" type="text" class="inputTextoObligatorio" size="6" maxlength="5" 
                                       value="<%=datModif != null && datModif.getCodPost() != null ? datModif.getCodPost() : ""%>"/>
                            </div>
                        </div>
                    </fieldset>
                    <div class="lineaFormulario">
                        <div class="botonera" style="padding-top: 15px; text-align: center;">
                            <input type="button" id="btnAceptar" name="btnAceptar" class="botonGeneral" value="<%=meLanbide87I18n.getMensaje(idiomaUsuario, "btn.aceptar")%>" onclick="guardarDatos();"/>
                            <input type="button" id="btnCancelar" name="btnCancelar" class="botonGeneral" value="<%=meLanbide87I18n.getMensaje(idiomaUsuario, "btn.cancelar")%>" onclick="cancelar();"/>
                        </div>
                    </div>
                </div>
            </form>
            <script type="text/javascript">
                /* TIPO INSTALACION */
                listaCodigosTipoInst[0] = "";
                listaDescripcionesTipoInst[0] = "";
                contador = 0;

                <logic:iterate id="tipoInst" name="listaTipoInst" scope="request">
                listaCodigosTipoInst[contador] = ['<bean:write name="tipoInst" property="des_val_cod" />'];
                listaDescripcionesTipoInst[contador] = ['<bean:write name="tipoInst" property="des_nom" />'];
                contador++;
                </logic:iterate>

                var comboListaTipoInst = new Combo("ListaTipoInst");
                comboListaTipoInst.addItems(listaCodigosTipoInst, listaDescripcionesTipoInst);
                comboListaTipoInst.change = cargarDatosTipoInst;

                if (<%=nuevo%> != null && <%=nuevo%> == 0) {
                    document.getElementById('tituloVentana').innerHTML = '<%=meLanbide87I18n.getMensaje(idiomaUsuario, "label.modifInstalacion")%>';
                    rellenardatModificar();
                } else {
                    document.getElementById('tituloVentana').innerHTML = '<%=meLanbide87I18n.getMensaje(idiomaUsuario, "label.nuevaInstalacion")%>';
                }
            </script>
            <div id="popupcalendar" class="text"></div>        
        </div>
    </body>
</html> 

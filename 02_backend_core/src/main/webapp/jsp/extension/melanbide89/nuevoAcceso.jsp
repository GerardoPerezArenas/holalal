<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide89.i18n.MeLanbide89I18n" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide89.vo.AccesoVO" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide89.util.ConstantesMeLanbide89" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide89.util.ConfigurationParameter" %>
<%@page import="java.util.ArrayList" %>
<%@page import="java.util.List" %>
<html>
    <head>
        <meta http-equiv="X-UA-Compatible" content="IE=Edge" />
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <%
            AccesoVO datModif = new AccesoVO();
      
            String codOrganizacion = "";
            String nuevo = "";        
            String numExpediente = "";
            
            MeLanbide89I18n meLanbide89I18n = MeLanbide89I18n.getInstance();

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
                    datModif = (AccesoVO)request.getAttribute("datModif");    
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
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/extension/melanbide89/melanbide89.css"/>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/JavaScriptUtil.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/melanbide89/GoUtils.js"></script>        
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/general.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/melanbide89/GoUtils.js"></script>
        <script type="text/javascript" src="<c:url value='/scripts/listaComboBox.js'/>"></script>
        <script type="text/javascript" src="<c:url value='/scripts/calendario.js'/>"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/validaciones.js"></script>
        <script type="text/javascript">
            var APP_CONTEXT_PATH = '<%=request.getContextPath()%>';
            var url = APP_CONTEXT_PATH + '/PeticionModuloIntegracion.do';
            var mensajeValidacion = '';


            function guardarDatos() {
                if (validarDatos()) {
                    elementoVisible('on', 'barraProgresoGO');
                    var parametros = "";

                    if ('<%=nuevo%>' != null && '<%=nuevo%>' == "1") {
                        parametros = "tarea=preparar&modulo=MELANBIDE89&operacion=crearNuevoAcceso&tipo=0";
                    } else {
                        parametros = "tarea=preparar&modulo=MELANBIDE89&operacion=modificarAcceso&tipo=0"
                                + "&id=<%=datModif != null && datModif.getId() != null ? datModif.getId().toString() : ""%>";
                    }
                    parametros += '&numExp=<%=numExpediente%>'
                            + "&nombre=" + document.getElementById('nombre').value
                            + "&apellido1=" + document.getElementById('apellido1').value
                            + "&apellido2=" + document.getElementById('apellido2').value
                            + "&dninie=" + document.getElementById('dninie').value
                            ;

                    try {
                        $.ajax({
                            url: url,
                            type: 'POST',
                            async: true,
                            data: parametros,
                            success: procesarRespuestaAltaAcceso,
                            error: mostrarErrorAltaAcceso
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
                var campo = document.getElementById('nombre').value;
                if (campo == null || campo == '') {
                    mensajeValidacion = '<%=meLanbide89I18n.getMensaje(idiomaUsuario, "msg.nombre")%>';
                    return false;
                }
                campo = document.getElementById('apellido1').value;
                if (campo == null || campo == '') {
                    mensajeValidacion = '<%=meLanbide89I18n.getMensaje(idiomaUsuario, "msg.apellido1")%>';
                    return false;
                }
                campo = document.getElementById('dninie').value;
                if (campo == null || campo == '') {
                    mensajeValidacion = '<%=meLanbide89I18n.getMensaje(idiomaUsuario, "msg.dninie")%>';
                    return false;
                } 
                campo = document.getElementById('dninie');
                if (campo.value != null && campo.value != '') {
                    if (!validarDniNie(campo)) {
                        mensajeValidacion = '<%=meLanbide89I18n.getMensaje(idiomaUsuario, "msg.documentoIncorrecto")%>';
                        return false;
                    }
                }
               
                return true;
            }

            function cancelar() {
                var resultado = jsp_alerta('', '<%=meLanbide89I18n.getMensaje(idiomaUsuario, "msg.preguntaCancelar")%>');
                if (resultado == 1) {
                    cerrarVentana();
                }
            }

            // FUNCIONES QUE PROCESAN RESPUESTA O ERROR DE LA PETICION AJAX
            function procesarRespuestaAltaAcceso(ajaxResult) {
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
                        mostrarError(5);
                    }
                } else {
                    mostrarError(codigoOperacion);
                }
            }

            function mostrarErrorAltaAcceso() {
                var codigo;
                if ('<%=nuevo%>' != null && '<%=nuevo%>' == 0) {
                    codigo = "8";
                } else {
                    codigo = "7";
                }
                mostrarError(codigo);
            }

            function mostrarError(codigo) {
                elementoVisible('off', 'barraProgresoGO');
                if (codigo == "1") {
                    jsp_alerta("A", '<%=meLanbide89I18n.getMensaje(idiomaUsuario,"error.errorBD")%>');
                } else if (codigo == "2") {
                    jsp_alerta("A", '<%=meLanbide89I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                } else if (codigo == "3") {
                    jsp_alerta("A", '<%=meLanbide89I18n.getMensaje(idiomaUsuario,"error.pasoParametros")%>');
                } else if (codigo == "4") {
                    jsp_alerta("A", '<%=meLanbide89I18n.getMensaje(idiomaUsuario,"error.listaVacia")%>');
                } else if (codigo == "5") {
                    jsp_alerta("A", '<%=meLanbide89I18n.getMensaje(idiomaUsuario,"error.errorObtenerDatos")%>');
                } else if (codigo == "6") {
                    jsp_alerta("A", '<%=meLanbide89I18n.getMensaje(idiomaUsuario,"error.errorBorrarDatos")%>');
                } else if (codigo == "7") {
                    jsp_alerta("A", '<%=meLanbide89I18n.getMensaje(idiomaUsuario,"error.errorInsertarDatos")%>');
                } else if (codigo == "8") {
                    jsp_alerta("A", '<%=meLanbide89I18n.getMensaje(idiomaUsuario,"error.errorActualizarDatos")%>');
                } else if (codigo == "-1") {
                    jsp_alerta("A", '<%=meLanbide89I18n.getMensaje(idiomaUsuario,"error.operacionIncorrecta")%>');
                } else {
                    jsp_alerta("A", '<%=meLanbide89I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                }
            }

        </script>
    </head>
    <body class="bandaBody" onload="javascript:{
                elementoVisible('off', 'barraProgresoGO');
            }" >
        <div class="contenidoPantalla">
            <div id="barraProgresoGO" style="visibility: hidden;">
                <div class="contenedorHidepage">
                    <div class="textoHide">
                        <span>
                            <%=meLanbide89I18n.getMensaje(idiomaUsuario, "msg.procesando")%>
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
                            <%=nuevo != null && nuevo=="1" ? meLanbide89I18n.getMensaje(idiomaUsuario,"label.nuevoAcceso"):meLanbide89I18n.getMensaje(idiomaUsuario,"label.modifAcceso")%>
                        </span>
                    </div>
                        
                    <br>
                    <div class="lineaFormulario">
                        <div style="width: 250px; float: left;" class="etiqueta">
                            <%=meLanbide89I18n.getMensaje(idiomaUsuario,"label.nombre")%>
                        </div>
                        <div>
                            <div style="float: left;">
                                <input id="nombre" name="nombre" type="text" class="inputTexto" size="100" maxlength="100" 
                                value="<%=datModif != null && datModif.getNombre() != null ? datModif.getNombre() : ""%>"/>
                            </div>
                        </div>
                    </div>
                            
                    <br><br>
                    <div class="lineaFormulario">
                        <div style="width: 250px; float: left;" class="etiqueta">
                            <%=meLanbide89I18n.getMensaje(idiomaUsuario,"label.ape1")%>
                        </div>
                        <div>
                            <div style="float: left;">
                                <input id="apellido1" name="apellido1" type="text" class="inputTexto" size="100" maxlength="100" 
                                value="<%=datModif != null && datModif.getApellido1() != null ? datModif.getApellido1() : ""%>"/>
                            </div>
                        </div>
                    </div>

                    <br><br>
                    <div class="lineaFormulario">
                        <div style="width: 250px; float: left;" class="etiqueta">
                            <%=meLanbide89I18n.getMensaje(idiomaUsuario,"label.ape2")%>
                        </div>
                        <div>
                            <div style="float: left;">
                                <input id="apellido2" name="apellido2" type="text" class="inputTexto" size="100" maxlength="100" 
                                value="<%=datModif != null && datModif.getApellido2() != null ? datModif.getApellido2() : ""%>"/>
                            </div>
                        </div>
                    </div>

                    <br><br>
                    <div class="lineaFormulario">
                        <div style="width: 250px; float: left;" class="etiqueta">
                            <%=meLanbide89I18n.getMensaje(idiomaUsuario,"label.dninie")%>
                        </div>
                        <div>
                            <div style="float: left;">
                                <input id="dninie" name="dninie" type="text" class="inputTexto" size="100" maxlength="100" 
                                value="<%=datModif != null && datModif.getDninie() != null ? datModif.getDninie() : ""%>"/>
                            </div>
                        </div>
                    </div>        

                    <br><br><br><br>
                    
                    <div class="lineaFormulario">
                        <div class="botonera" style="width: 100%; float: left; text-align: center;">
                            <input type="button" id="btnAceptar" name="btnAceptar" class="botonGeneral" value="<%=meLanbide89I18n.getMensaje(idiomaUsuario, "btn.aceptar")%>" onclick="guardarDatos();"/>
                            <input type="button" id="btnCancelar" name="btnCancelar" class="botonGeneral" value="<%=meLanbide89I18n.getMensaje(idiomaUsuario, "btn.cancelar")%>" onclick="cancelar();"/>
                        </div>
                    </div>
                </div>
            </form>
            <div id="popupcalendar" class="text"></div>        
        </div>
    </body>
</html> 

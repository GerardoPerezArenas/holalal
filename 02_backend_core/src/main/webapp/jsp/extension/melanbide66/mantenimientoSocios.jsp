<%-- 
    Document   : mantenimientoSocios
    Created on : 07-jul-2022, 2:46:39
    Author     : kepa
--%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide66.i18n.MeLanbide66I18n" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide66.vo.SocioVO" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide66.util.ConstantesMeLanbide66" %>
<%@page import="java.util.ArrayList" %>
<%@page import="java.util.List" %>
<%@page import="java.text.SimpleDateFormat" %>
<html lang="es">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <%
            UsuarioValueObject usuarioVO = new UsuarioValueObject();
            SocioVO datModif = new SocioVO();
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
                    datModif = (SocioVO)request.getAttribute("datModif");
                }
            }catch(Exception ex) {
            }
           
            //Clase para internacionalizar los mensajes de la aplicación.
            MeLanbide66I18n meLanbide66I18n = MeLanbide66I18n.getInstance();
            String numExpediente = (String)request.getAttribute("numExp");
        %>
        <jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
        <jsp:setProperty name="descriptor"  property="idi_cod" value="<%=idiomaUsuario%>" />
        <jsp:setProperty name="descriptor"  property="apl_cod" value="<%=apl%>" />
        <link rel="StyleSheet" media="screen" type="text/css" href="<%=request.getContextPath()%><%=css%>">
        <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/css/font-awesome.min.css'/>" media="screen">
        <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/less/animated.less'/>" media="screen">
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/>
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/extension/melanbide66/melanbide66.css"/>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/general.js"></script>
        <script type="text/javascript">
            var APP_CONTEXT_PATH = '<%=request.getContextPath()%>';
            var url = '<%=request.getContextPath()%>/PeticionModuloIntegracion.do';

            function guardarDatos() {
                if (validarDatos()) {
                    elementoVisible('on', 'barraProgresoSocios');
                    var parametros = "";

                    if ('<%=nuevo%>' != null && '<%=nuevo%>' == "1") {
                        parametros = "tarea=preparar&modulo=MELANBIDE66&operacion=crearNuevoSocio&tipo=0";
                    } else {
                        parametros = "tarea=preparar&modulo=MELANBIDE66&operacion=modificarSocio&tipo=0"
                                + "&id=<%=datModif != null && datModif.getId() != null ? datModif.getId().toString() : ""%>";

                    }
                    parametros += '&numExp=<%=numExpediente%>'
                            + "&nombre=" + (document.getElementById('nombre').value != null && document.getElementById('nombre').value != "" ? document.getElementById('nombre').value : "")
                            + "&apellido1=" + (document.getElementById('apellido1').value != null && document.getElementById('apellido1').value != "" ? document.getElementById('apellido1').value : "")
                            + "&apellido2=" + (document.getElementById('apellido2').value != null && document.getElementById('apellido2').value != "" ? document.getElementById('apellido2').value : "")
                            + "&dninie=" + (document.getElementById('dninie').value != null && document.getElementById('dninie').value != "" ? document.getElementById('dninie').value : "");
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
                        elementoVisible('off', 'barraProgresoSocios');
                        mostrarError();
                    }
                } else {
                    jsp_alerta("A", mensajeValidacion);
                }
            }

            function validarDatos() {
                mensajeValidacion = "";
                var campo = document.getElementById('dninie');
                if (campo.value != null && campo.value != '') {
                    if (!validarDniNie(campo)) {
                        mensajeValidacion = '<%=meLanbide66I18n.getMensaje(idiomaUsuario, "msg.documentoIncorrecto")%>';
                        return false;
                    }
                } else {
                    mensajeValidacion = '<%=meLanbide66I18n.getMensaje(idiomaUsuario, "msg.documentoObligatorio")%>';
                    return false;
                }
                campo = document.getElementById('nombre').value;
                if (campo == null || campo == '') {
                    mensajeValidacion = '<%=meLanbide66I18n.getMensaje(idiomaUsuario, "msg.nombre")%>';
                    return false;
                }
                campo = document.getElementById('apellido1').value;
                if (campo == null || campo == '') {
                    mensajeValidacion = '<%=meLanbide66I18n.getMensaje(idiomaUsuario, "msg.apellido1")%>';
                    return false;
                }
                return true;
            }

            function cancelar() {
                var resultado = jsp_alerta('', '<%=meLanbide66I18n.getMensaje(idiomaUsuario, "msg.preguntaCancelar")%>');
                if (resultado == 1) {
                    cerrarVentana();
                }
            }

            // FUNCIONES QUE PROCESAN RESPUESTA O ERROR DE LA PETICION AJAX
            function procesarRespuestaAltaModificacion(ajaxResult) {
                elementoVisible('off', 'barraProgresoSocios');
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
                elementoVisible('off', 'barraProgresoSocios');
                if (codigo == "1") {
                    jsp_alerta("A", '<%=meLanbide66I18n.getMensaje(idiomaUsuario,"error.errorBD")%>');
                } else if (codigo == "2") {
                    jsp_alerta("A", '<%=meLanbide66I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                } else if (codigo == "3") {
                    jsp_alerta("A", '<%=meLanbide66I18n.getMensaje(idiomaUsuario,"error.pasoParametros")%>');
                } else if (codigo == "4") {
                    jsp_alerta("A", '<%=meLanbide66I18n.getMensaje(idiomaUsuario,"error.listaVacia")%>');
                } else if (codigo == "5") {
                    jsp_alerta("A", '<%=meLanbide66I18n.getMensaje(idiomaUsuario,"error.errorObtenerDatos")%>');
                } else if (codigo == "6") {
                    jsp_alerta("A", '<%=meLanbide66I18n.getMensaje(idiomaUsuario,"error.errorElimDatos")%>');
                } else if (codigo == "7") {
                    jsp_alerta("A", '<%=meLanbide66I18n.getMensaje(idiomaUsuario,"error.errorInsDatos")%>');
                } else if (codigo == "8") {
                    jsp_alerta("A", '<%=meLanbide66I18n.getMensaje(idiomaUsuario,"error.errorModDatos")%>');
                } else if (codigo == "-1") {
                    jsp_alerta("A", '<%=meLanbide66I18n.getMensaje(idiomaUsuario,"error.operacionIncorrecta")%>');
                } else {
                    jsp_alerta("A", '<%=meLanbide66I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
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

            function elementoVisible(valor, idBarra) {
                if (valor == 'on') {
                    document.getElementById(idBarra).style.visibility = 'inherit';
                } else if (valor == 'off') {
                    document.getElementById(idBarra).style.visibility = 'hidden';
                }
            }

            function validarDniNie(campo) {
                var cadena = 'TRWAGMYFPDXBNJZSQVHLCKET';
                var letra;
                var aux;
                var posicion;
                var longitud;
                var dni = campo.value;
                longitud = dni.length;
                aux = dni.substring(longitud - 1).toUpperCase();
                if ((longitud >= 9) && (longitud < 10)) {
                    if (isNaN(aux)) {
                        posicion = dni.substring(0, longitud - 1) % 23;
                        letra = cadena.charAt(posicion);
                        if (isNaN(dni.substring(0, longitud - 1))) {
                            //return false;//PUEDE Q NIE
                            var temp = dni.toUpperCase();
                            var cadenadni = "TRWAGMYFPDXBNJZSQVHLCKE";
                            if (/^[XYZ]{1}/.test(temp)) {
                                temp = temp.replace('X', '0');
                                temp = temp.replace('Y', '1');
                                temp = temp.replace('Z', '2');
                                pos = temp.substring(0, 8) % 23;

                                if (dni.toUpperCase().charAt(8) == cadenadni.substring(pos, pos + 1)) {
                                    return true;
                                } else {
                                    return false;
                                }
                            }
                        }
                        if (aux != letra) {
                            return false;
                        }
                    } else {
                        if (isNaN(dni.substring(0, longitud))) {
                            return false;
                        }
                        posicion = dni.substring(0, longitud) % 23;
                        letra = cadena.charAt(posicion);
                        campo.value = dni + letra;
                        return true;
                    }
                } else {
                    return false;
                }
                campo.value = campo.value.toUpperCase();
                return true;
            }


        </script>
    </head>
    <body class="bandaBody" onload="javascript:{
                elementoVisible('off', 'barraProgresoSocios');
            }" >
        <div class="contenidoPantalla">
            <div id="barraProgresoSocios" style="visibility: hidden;">
                <div class="contenedorHidepage">
                    <div class="textoHide">
                        <span>
                            <%=meLanbide66I18n.getMensaje(idiomaUsuario, "msg.procesando")%>
                        </span>
                    </div>
                    <div class="imagenHide">
                        <span id="disco" class="fa fa-spinner fa-spin" aria-hidden="true"/>
                    </div>
                </div>
            </div>
            <form>
                <div style="width: 100%; padding: 10px; text-align: left;">
                    <div  class="sub3titulo" style="clear: both; text-align: center; font-size: 14px; width: 95%">
                        <span id="tituloVentana">      
                            <%=nuevo != null && nuevo=="1" ? meLanbide66I18n.getMensaje(idiomaUsuario,"label.nuevo"):meLanbide66I18n.getMensaje(idiomaUsuario,"label.modif")%>
                        </span>
                    </div>
                    <fieldset id="socio" name="socio"style="width: 95%;">
                        <legend class="legendAzul" id="titPuesto"><%=meLanbide66I18n.getMensaje(idiomaUsuario,"label.socio")%></legend>
                        <div class="lineaFormulario">
                            <div  style="width: 190px; float: left;" class="etiqueta">
                                <%=meLanbide66I18n.getMensaje(idiomaUsuario,"socio.dni")%>
                            </div>
                            <div style="width: 450px;float: left;">
                                <input id="dninie" name="dninie" type="text" class="inputTexto" size="10" maxlength="9" onkeyup="xAMayusculas(this);"
                                       value="<%=datModif != null && datModif.getDni() != null ? datModif.getDni() : ""%>" />                          
                            </div>
                        </div>
                        <br><br>  
                        <div class="lineaFormulario">
                            <div  style="width: 190px; float: left;" class="etiqueta">
                                <%=meLanbide66I18n.getMensaje(idiomaUsuario,"socio.nombre")%>
                            </div>
                            <div style="width: 450px;float: left;">
                                <input id="nombre" name="nombre" type="text" class="inputTexto" size="30" maxlength="100"
                                       value="<%=datModif != null && datModif.getNombre() != null ? datModif.getNombre() : ""%>" />
                            </div>                            
                        </div>
                        <br><br>  
                        <div class="lineaFormulario">
                            <div  style="width: 190px; float: left;" class="etiqueta">
                                <%=meLanbide66I18n.getMensaje(idiomaUsuario,"socio.apellido1")%>
                            </div>
                            <div style="width: 450px;float: left;">
                                <input id="apellido1" name="apellido1" type="text" class="inputTexto" size="30" maxlength="50" 
                                       value="<%=datModif != null && datModif.getApellido1() != null ? datModif.getApellido1() : ""%>" />
                            </div>                            
                        </div>
                        <br><br>  
                        <div class="lineaFormulario"> 
                            <div  style="width: 190px; float: left;" class="etiqueta">
                                <%=meLanbide66I18n.getMensaje(idiomaUsuario,"socio.apellido2")%>
                            </div>
                            <div style="width: 450px;float: left;">
                                <input id="apellido2" name="apellido23" type="text" class="inputTexto" size="30" maxlength="50" 
                                       value="<%=datModif != null && datModif.getApellido2() != null ? datModif.getApellido2() : ""%>" />
                            </div>
                        </div>
                    </fieldset>
                    <br><br> 
                    <div class="lineaFormulario">
                        <div class="botonera" style="width: 100%; float: left; text-align: center;">
                            <input type="button" id="btnAceptar" name="btnAceptar" class="botonGeneral" value="<%=meLanbide66I18n.getMensaje(idiomaUsuario, "btn.aceptar")%>" onclick="guardarDatos();"/>
                            <input type="button" id="btnCancelar" name="btnCancelar" class="botonGeneral" value="<%=meLanbide66I18n.getMensaje(idiomaUsuario, "btn.cancelar")%>" onclick="cancelar();"/>
                        </div>
                    </div>
                </div>
            </form>
        </div>
    </body>
</html>
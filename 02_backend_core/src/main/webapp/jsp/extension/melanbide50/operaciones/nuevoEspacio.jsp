<%-- 
    Document   : nuevoEspacio
    Created on : 18-jul-2022, 15:17:07
    Author     : kepa
--%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide50.i18n.MeLanbide50I18n" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide50.vo.espacios.EspacioVO" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide50.vo.especialidades.EspecialidadesVO" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide50.util.ConstantesMeLanbide50" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide50.util.ConfigurationParameter" %>
<%@page import="java.util.ArrayList" %>
<%@page import="java.util.List" %>
<html>
    <head>
        <meta http-equiv="X-UA-Compatible" content="IE=Edge" />
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <%
        UsuarioValueObject usuarioVO = new UsuarioValueObject();
        EspacioVO datModif = new EspacioVO();
        EspecialidadesVO datoEspecialidad = new EspecialidadesVO();
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
                datModif = (EspacioVO)request.getAttribute("datModif");
            }
            if(request.getAttribute("datoEspecialidad") != null) {
                datoEspecialidad = (EspecialidadesVO)request.getAttribute("datoEspecialidad");
            }
        }catch(Exception ex) {
        }
           
        //Clase para internacionalizar los mensajes de la aplicación.
        MeLanbide50I18n meLanbide50I18n = MeLanbide50I18n.getInstance();
        String numExpediente = (String)request.getAttribute("numero");
        %>
        <jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
        <jsp:setProperty name="descriptor"  property="idi_cod" value="<%=idiomaUsuario%>" />
        <jsp:setProperty name="descriptor"  property="apl_cod" value="<%=apl%>" />
        <link rel="StyleSheet" media="screen" type="text/css" href="<%=request.getContextPath()%><%=css%>">
        <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/css/font-awesome.min.css'/>" media="screen">
        <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/less/animated.less'/>" media="screen">
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/>
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/extension/melanbide50/melanbide50.css"/>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/meLanbide50/RgcfmUtils.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/general.js"></script>
        <script type="text/javascript">
            var APP_CONTEXT_PATH = '<%=request.getContextPath()%>';
            var url = '<%=request.getContextPath()%>/PeticionModuloIntegracion.do';
            var mensajeValidacion = '';

            function guardarDatos() {
                if (validarDatos()) {
                    var ajax = getXMLHttpRequest();
                    var nodos = null;
                    var parametros = "";
                    var llamar = true;
                    if ('<%=nuevo%>' != null && '<%=nuevo%>' == '1') {
                        parametros = "tarea=preparar&modulo=MELANBIDE50&operacion=crearEspacio&tipo=0";
                    } else {
                        parametros = "tarea=preparar&modulo=MELANBIDE50&operacion=modificarEspacio&tipo=0"
                                + "&id=<%=datModif != null && datModif.getId() != null ? datModif.getId().toString() : ""%>";
                    }
                    parametros += "&numero=<%=numExpediente%>"
                            + "&denominacion=" + document.getElementById('denominacionEspa').value
                            + "&espaciosAcred=" + document.getElementById('espAcred').value
                            + "&espaciosSolic=" + document.getElementById('espSolic').value
                            + "&alumnosAcred=" + document.getElementById('numAlumn').value
                            + "&alumnosSolic=" + document.getElementById('numAlumSolic').value
                            + "&idEpsol=<%=datoEspecialidad.getId()%>"
                            ;

                    try {
                        ajax.open("POST", url, false);
                        ajax.setRequestHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
                        ajax.setRequestHeader("Accept", "text/xml, application/xml, text/plain");
                        ajax.send(parametros);
                        if (ajax.readyState == 4 && ajax.status == 200) {
                            var xmlDoc = null;
                            if (navigator.appName.indexOf("Internet Explorer") != -1) {
                                // En IE el XML viene en responseText y no en la propiedad responseXML
                                var text = ajax.responseText;
                                xmlDoc = new ActiveXObject("Microsoft.XMLDOM");
                                xmlDoc.async = "false";
                                xmlDoc.loadXML(text);
                            } else {
                                // En el resto de navegadores el XML se recupera de la propiedad responseXML
                                xmlDoc = ajax.responseXML;
                            }//if(navigator.appName.indexOf("Internet Explorer")!=-1)
                        }//if (ajax.readyState==4 && ajax.status==200)
                        nodos = xmlDoc.getElementsByTagName("RESPUESTA");
                        var lista = extraerListaEspacios(nodos);
                        var codigoOperacion = lista[0];
                        if (codigoOperacion == "0") {
                            self.parent.opener.retornoXanelaAuxiliar(lista);
                            cerrarVentana();
                        } else if (codigoOperacion == "1") {
                            jsp_alerta("A", '<%=meLanbide50I18n.getMensaje(idiomaUsuario,"error.errorBD")%>');
                        } else if (codigoOperacion == "2") {
                            jsp_alerta("A", '<%=meLanbide50I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                        } else if (codigoOperacion == "3") {
                            jsp_alerta("A", '<%=meLanbide50I18n.getMensaje(idiomaUsuario,"error.pasoParametros")%>');
                        } else {
                            jsp_alerta("A", '<%=meLanbide50I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                        }
                    } catch (err) {
                    }
                } else {
                    jsp_alerta("A", mensajeValidacion);
                }
            }

            function validarDatos() {
                mensajeValidacion = "";
                var campo = document.getElementById('denominacionEspa');
                if (campo.value == null || campo.value == '') {
                    mensajeValidacion = '';
                    return false;
                } else {
                    if (!compruebaTamanoCampo(campo, 500)) {
                        mensajeValidacion = '<%=meLanbide50I18n.getMensaje(idiomaUsuario, "msg.tamano.denominacion")%>';
                        return false;
                    }
                }

                campo = document.getElementById('espAcred');
                if (campo.value != null && campo.value != '') {
                    if (!compruebaTamanoCampo(campo, 2000)) {
                        mensajeValidacion = '<%=meLanbide50I18n.getMensaje(idiomaUsuario, "msg.tamano.espAcre")%>';
                        return false;
                    }
                }

                campo = document.getElementById('espSolic');
                if (campo.value != null && campo.value != '') {
                    if (!compruebaTamanoCampo(campo, 2000)) {
                        mensajeValidacion = '<%=meLanbide50I18n.getMensaje(idiomaUsuario, "msg.tamano.espSolic")%>';
                        return false;
                    }
                }

                campo = document.getElementById('numAlumn');
                if (campo.value != null && campo.value != '') {
                    if (!compruebaTamanoCampo(campo, 6)) {
                        mensajeValidacion = '<%=meLanbide50I18n.getMensaje(idiomaUsuario, "msg.tamano.numAlum")%>';
                        return false;
                    }
                }
                campo = document.getElementById('numAlumSolic');
                if (campo.value != null && campo.value != '') {
                    if (!compruebaTamanoCampo(campo, 6)) {
                        mensajeValidacion = '<%=meLanbide50I18n.getMensaje(idiomaUsuario, "msg.tamano.numAlumSol")%>';
                        return false;
                    }
                }
                return true;
            }

        </script>
    </head>
    <body class="bandaBody" >
        <div class="contenidoPantalla">
            <form>
                <div style="width: 100%; padding: 10px; text-align: left;">
                    <div  class="sub3titulo" style="clear: both; text-align: center; font-size: 14px;">
                        <span id="tituloVentana">
                            <%=nuevo != null && nuevo=="1" ? meLanbide50I18n.getMensaje(idiomaUsuario,"label.espacios.nuevo") : meLanbide50I18n.getMensaje(idiomaUsuario,"label.espacios.modif")%>
                        </span>
                    </div>
                    <fieldset id="espacio" name="espacio"style="width: 95%;">
                        <legend class="legendAzul" id="titPuesto"><%=meLanbide50I18n.getMensaje(idiomaUsuario,"label.espacios.espacio")%></legend>                        
                        <div class="lineaFormulario">
                            <div  style="width: 190px; float: left;" class="etiqueta"><%=meLanbide50I18n.getMensaje(idiomaUsuario,"label.espacios.denominacion")%></div>
                            <div style="width: 450px;float: left;">
                                <div style="float: left;">
                                    <textarea rows="4" cols="61" id="denominacionEspa"  name="denominacionEspa" type="text" class="inputTexto" maxlength="500" style="text-align: left"><%= nuevo != null && nuevo=="1"  ? datoEspecialidad!=null && datoEspecialidad.getDenominacion()!=null ? datoEspecialidad.getDenominacion():"" :             datModif != null && datModif.getDenominacion() != null ? datModif.getDenominacion()  : ""%></textarea>     
                                </div>                               
                            </div>
                        </div>
                        <br><br>
                        <div class="lineaFormulario">
                            <div  style="width: 190px; float: left;" class="etiqueta"><%=meLanbide50I18n.getMensaje(idiomaUsuario,"label.espacios.espAcred")%></div>
                            <div style="width: 450px;float: left;">
                                <textarea rows="4" cols="61" id="espAcred" name="espAcred" type="text" class="inputTexto"  maxlength="2000" style="text-align: left"><%=datModif != null && datModif.getEspAcred() != null ? datModif.getEspAcred()  : ""%></textarea>
                            </div>
                        </div>
                        <br><br>
                        <div class="lineaFormulario">
                            <div  style="width: 190px; float: left;" class="etiqueta"><%=meLanbide50I18n.getMensaje(idiomaUsuario,"label.espacios.espSolic")%></div>
                            <div style="width: 450px;float: left;">
                                <textarea rows="4" cols="61" id="espSolic" name="espSolic" type="text" class="inputTexto" maxlength="2000" style="text-align: left"><%=datModif != null && datModif.getEspAutor() != null ? datModif.getEspAutor()  : ""%></textarea>
                            </div>
                        </div>
                        <br><br>
                        <div class="lineaFormulario">
                            <div  style="width: 190px; float: left;" class="etiqueta"><%=meLanbide50I18n.getMensaje(idiomaUsuario,"label.espacios.numAlumn")%></div>
                            <div style="width: 450px;float: left;">
                                <input id="numAlumn" name="numAlumn" type="text" class="inputTexto" size="9" maxlength="6" value="<%=datModif != null && datModif.getNumAlumnos() != null ? datModif.getNumAlumnos(): ""%>" onkeyup="SoloDigitos(this);" />
                            </div>
                        </div>
                        <br><br>
                        <div class="lineaFormulario">
                            <div  style="width: 190px; float: left;" class="etiqueta"><%=meLanbide50I18n.getMensaje(idiomaUsuario,"label.espacios.numAlumSolic")%></div>
                            <div style="width: 450px;float: left;">
                                <input id="numAlumSolic" name="numAlumSolic" type="text" class="inputTexto" size="9" maxlength="6" value="<%=datModif != null && datModif.getAlumNuevos() != null ? datModif.getAlumNuevos(): ""%>" onkeyup="SoloDigitos(this);" />
                            </div>
                        </div>
                        <br><br> 
                    </fieldset>
                    <br><br> 
                    <div class="lineaFormulario">
                        <div class="botonera" style="width: 100%; float: left; text-align: center;">
                            <input type="button" id="btnAceptar" name="btnAceptar" class="botonGeneral" value="<%=meLanbide50I18n.getMensaje(idiomaUsuario, "btn.aceptar")%>" onclick="guardarDatos();"/>
                            <input type="button" id="btnCancelar" name="btnCancelar" class="botonGeneral" value="<%=meLanbide50I18n.getMensaje(idiomaUsuario, "btn.cancelar")%>" onclick="cancelar();"/>
                        </div>
                    </div>
                </div>
            </form>  
        </div>
    </body>
</html>

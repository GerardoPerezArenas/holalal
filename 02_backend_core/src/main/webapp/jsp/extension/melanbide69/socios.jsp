<%-- 
    Document   : listaSocios
    Created on : 07-jul-2022, 1:49:29
    Author     : kepa
--%>
<%@taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide69.i18n.MeLanbide69I18n" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide69.vo.SocioVO"%>
<%@page import="java.util.ArrayList" %>
<%@page import="java.util.List" %>
<% 
  UsuarioValueObject usuarioVO = new UsuarioValueObject();
    int idiomaUsuario = 1;
    int apl = 5;
    String css = "";
    if (session.getAttribute("usuario") != null) {
        usuarioVO = (UsuarioValueObject) session.getAttribute("usuario");
        apl = usuarioVO.getAppCod();
        idiomaUsuario = usuarioVO.getIdioma();
        css = usuarioVO.getCss();
    }

    //Clase para internacionalizar los mensajes de la aplicación.
    MeLanbide69I18n meLanbide69I18n = MeLanbide69I18n.getInstance();
    String numExpediente = (String)request.getAttribute("numExp");
%>
<html>
    <head>
        <meta http-equiv="X-UA-Compatible" content="IE=Edge" />
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
        <jsp:setProperty name="descriptor"  property="idi_cod" value="<%=idiomaUsuario%>" />
        <jsp:setProperty name="descriptor"  property="apl_cod" value="<%=apl%>" />
        <link rel="StyleSheet" media="screen" type="text/css" href="<%=request.getContextPath()%><%=css%>"/>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script>
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/extension/melanbide69/melanbide69.css"/>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/popup.js"></script>
        <script type="text/javascript">

            var url = '<%=request.getContextPath()%>/PeticionModuloIntegracion.do';

            function pulsarNuevoSocio() {
                lanzarPopUpModal(url + '?tarea=preparar&modulo=MELANBIDE69&operacion=cargarNuevoSocio&tipo=0&nuevo=1&numExp=<%=numExpediente%>', 600, 700, 'no', 'no', function (result) {
                    if (result != undefined) {
                        if (result[0] == '0') {
                            recargarTablaSocios(result[1]);
                        }
                    }
                });
            }

            function pulsarModificarSocio() {
                if (tablaSocios.selectedIndex != -1) {
                    lanzarPopUpModal(url + '?tarea=preparar&modulo=MELANBIDE69&operacion=cargarModificarSocio&tipo=0&nuevo=0&numExp=<%=numExpediente%>&id=' + listaSocios[tablaSocios.selectedIndex][0], 600, 700, 'no', 'no', function (result) {
                        if (result != undefined) {
                            if (result[0] == '0') {
                                recargarTablaSocios(result[1]);
                            }
                        }
                    });
                } else {
                    jsp_alerta('A', '<%=meLanbide69I18n.getMensaje(idiomaUsuario, "msg.msjNoSelecFila")%>');
                }
            }

            function pulsarEliminarSocio() {
                if (tablaSocios.selectedIndex != -1) {
                    var resultado = jsp_alerta('', '<%=meLanbide69I18n.getMensaje(idiomaUsuario, "msg.preguntaEliminarSocio")%>');
                    if (resultado == 1) {
                        elementoVisible('on', 'barraProgresoSocios');
                        var parametros = 'tarea=preparar&modulo=MELANBIDE69&operacion=eliminarSocio&tipo=0&numExp=<%=numExpediente%>&id=' + listaSocios[tablaSocios.selectedIndex][0];
                        try {
                            $.ajax({
                                url: url,
                                type: 'POST',
                                async: true,
                                data: parametros,
                                success: procesarRespuestaActualizarPestana,
                                error: mostrarErrorEliminarSocio
                            });
                        } catch (Err) {
                            elementoVisible('off', 'barraProgresoSocios');
                            mostrarErrorPeticion();
                        }
                    }
                } else {
                    jsp_alerta('A', '<%=meLanbide69I18n.getMensaje(idiomaUsuario, "msg.msjNoSelecFila")%>');
                }
            }

            function crearTablaSocios() {
                tablaSocios = new Tabla(true, '<%=descriptor.getDescripcion("buscar")%>', '<%=descriptor.getDescripcion("anterior")%>', '<%=descriptor.getDescripcion("siguiente")%>', '<%=descriptor.getDescripcion("mosFilasPag")%>', '<%=descriptor.getDescripcion("msgNoResultBusq")%>', '<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>', '<%=descriptor.getDescripcion("filtrDeTotal")%>', '<%=descriptor.getDescripcion("primero")%>', '<%=descriptor.getDescripcion("ultimo")%>', document.getElementById('listaSocios'));
                tablaSocios.addColumna('0', 'center', "<%=meLanbide69I18n.getMensaje(idiomaUsuario,"socio.id")%>");
                tablaSocios.addColumna('200', 'center', "<%=meLanbide69I18n.getMensaje(idiomaUsuario,"socio.nombre")%>");
                tablaSocios.addColumna('100', 'center', "<%=meLanbide69I18n.getMensaje(idiomaUsuario,"socio.apellido1")%>");
                tablaSocios.addColumna('100', 'center', "<%=meLanbide69I18n.getMensaje(idiomaUsuario,"socio.apellido2")%>");
                tablaSocios.addColumna('100', 'center', "<%=meLanbide69I18n.getMensaje(idiomaUsuario,"socio.dni")%>");

                tablaSocios.displayCabecera = true;
                tablaSocios.height = 360;
            }

            function recargarTablaSocios(socios) {
                var socio;
                listaSocios = new Array();
                listaSociosTabla = new Array();
                for (var i = 0; i < socios.length; i++) {
                    socio = socios[i];
                    listaSocios[i] = [socio.id, socio.nombre, socio.apellido1, (socio.apellido2 != undefined ? socio.apellido2 : '-'), socio.dni];
                    listaSociosTabla[i] = [socio.id, socio.nombre, socio.apellido1, (socio.apellido2 != undefined ? socio.apellido2 : '-'), socio.dni];
                }
                crearTablaSocios();
                tablaSocios.lineas = listaSociosTabla;
                tablaSocios.displayTabla();
            }

            function actualizarPestanaSocios() {
                elementoVisible('on', 'barraProgresoSocios');
                var parametros = 'tarea=preparar&modulo=MELANBIDE69&operacion=actualizarPestanaSocios&tipo=0&numExp=<%=numExpediente%>';
                try {
                    $.ajax({
                        url: url,
                        type: 'POST',
                        async: true,
                        data: parametros,
                        success: procesarRespuestaActualizarPestana,
                        error: mostrarErrorPeticion
                    });
                } catch (Err) {
                    elementoVisible('off', 'barraProgresoSocios');
                    mostrarErrorPeticion();
                }
            }

            // FUNCIONES QUE PROCESAN RESPUESTA O ERROR DE LA PETICION AJAX
            function procesarRespuestaActualizarPestana(ajaxResult) {
                var datos = JSON.parse(ajaxResult);
                var codigoOperacion = datos.tabla.codigoOperacion;
                if (codigoOperacion == "0") {
                    var socios = datos.tabla.lista;
                    if (socios.length > 0) {
                        elementoVisible('off', 'barraProgresoSocios');
                        recargarTablaSocios(socios);
                    } else {
                        mostrarErrorPeticion();
                    }
                } else {
                    mostrarErrorPeticion(codigoOperacion);
                }
            }

            function mostrarErrorEliminarSocio() {
                mostrarErrorPeticion(6);
            }

            function mostrarErrorPeticion(codigo) {
                var msgtitle = "¡¡ ERROR !!";
                elementoVisible('off', 'barraProgresoSocios');
                if (codigo == "1") {
                    jsp_alerta("A", '<%=meLanbide69I18n.getMensaje(idiomaUsuario,"error.errorBD")%>', msgtitle);
                } else if (codigo == "2") {
                    jsp_alerta("A", '<%=meLanbide69I18n.getMensaje(idiomaUsuario,"error.errorGen")%>', msgtitle);
                } else if (codigo == "3") {
                    jsp_alerta("A", '<%=meLanbide69I18n.getMensaje(idiomaUsuario,"error.pasoParametros")%>', msgtitle);
                } else if (codigo == "4") {
                    jsp_alerta("A", '<%=meLanbide69I18n.getMensaje(idiomaUsuario,"error.listaVacia")%>', msgtitle);
                } else if (codigo == "5") {
                    jsp_alerta("A", '<%=meLanbide69I18n.getMensaje(idiomaUsuario,"error.errorObtenerDatos")%>', msgtitle);
                } else if (codigo == "6") {
                    jsp_alerta("A", '<%=meLanbide69I18n.getMensaje(idiomaUsuario,"error.falloOperacionElim")%>', msgtitle);
                } else if (codigo == "-1") {
                    jsp_alerta("A", '<%=meLanbide69I18n.getMensaje(idiomaUsuario,"error.operacionIncorrecta")%>', msgtitle);
                } else {
                    jsp_alerta("A", '<%=meLanbide69I18n.getMensaje(idiomaUsuario,"error.errorGen")%>', msgtitle);
                }
            }

            function elementoVisible(valor, elemento) {
                if (valor == 'on') {
                    document.getElementById(elemento).style.visibility = 'inherit';
                } else if (valor == 'off') {
                    document.getElementById(elemento).style.visibility = 'hidden';
                }
            }
        </script>
    </head>
    <body class="bandaBody" onload="javascript:{
                elementoVisible('off', 'barraProgresoSocios');
            }" >
        <div class="tab-page" id="tabPage692" style="height:520px; width: 100%;">
            <h2 class="tab" id="pestana692"><%=meLanbide69I18n.getMensaje(idiomaUsuario,"label.titulo.pestanaSocios")%></h2> 
            <script type="text/javascript">tp1.addTabPage(document.getElementById("tabPage692"));</script>
            <div id="barraProgresoSocios" style="visibility: hidden;">
                <div class="contenedorHidepage">
                    <div class="textoHide">
                        <span>
                            <%=meLanbide69I18n.getMensaje(idiomaUsuario, "msg.procesando")%>
                        </span>
                    </div>
                    <div class="imagenHide">
                        <span id="disco" class="fa fa-spinner fa-spin" aria-hidden="true"/>
                    </div>
                </div>
            </div>
            <br/>
            <h2 class="legendAzul" id="pestanaPrinc"><%=meLanbide69I18n.getMensaje(idiomaUsuario,"label.tituloSocios")%></h2>
            <div>    
                <br>
                <div id="divGeneral">     
                    <div id="listaSocios"  align="center"></div>
                </div>
                <div class="botonera" style="text-align: center;">
                    <input type="button" id="btnNuevoSocio" name="btnNuevoSocio" class="botonGeneral"  value="<%=meLanbide69I18n.getMensaje(idiomaUsuario, "button.etiqGrabar")%>" onclick="pulsarNuevoSocio();">
                    <input type="button" id="btnModificarSocio" name="btnModificarSocio" class="botonGeneral" value="<%=meLanbide69I18n.getMensaje(idiomaUsuario, "button.etiqModificar")%>" onclick="pulsarModificarSocio();">
                    <input type="button" id="btnEliminarSocio" name="btnEliminarSocio"   class="botonGeneral" value="<%=meLanbide69I18n.getMensaje(idiomaUsuario, "button.etiqEliminar")%>" onclick="pulsarEliminarSocio();">
                </div>
            </div>
            <script  type="text/javascript">
                var tablaSocios;
                var listaSocios = new Array();
                var listaSociosTabla = new Array();
                crearTablaSocios();
                <% SocioVO objectVO = null;
                List<SocioVO> List = null;
                if(request.getAttribute("listaSocios")!=null){
                    List = (List<SocioVO>)request.getAttribute("listaSocios");
                }	
                if (List!= null && List.size() >0){
                    for (int indice=0;indice<List.size();indice++) {
                        objectVO = List.get(indice);
                        String nombre = "-";
                        if(objectVO.getNombre()!=null){
                            nombre=objectVO.getNombre();
                        }
                        String apel1 = "-";               
                        if(objectVO.getApellido1()!=null){
                            apel1=objectVO.getApellido1();
                        }
                        String apel2 = "-";
                        if(objectVO.getApellido2()!=null){
                            apel2=objectVO.getApellido2();
                        } 
                        String dni="-";
                        if(objectVO.getDni()!=null){
                            dni=objectVO.getDni();
                        }
                %>
                listaSocios[<%=indice%>] = ['<%=objectVO.getId()%>', '<%=nombre%>', '<%=apel1%>', '<%=apel2%>', '<%=dni%>'];
                listaSociosTabla[<%=indice%>] = ['<%=objectVO.getId()%>', '<%=nombre%>', '<%=apel1%>', '<%=apel2%>', '<%=dni%>'];
                <%
                    }
               }
                %>
                tablaSocios.lineas = listaSociosTabla;
                tablaSocios.displayTabla();

            </script>
    </body>   
</html>
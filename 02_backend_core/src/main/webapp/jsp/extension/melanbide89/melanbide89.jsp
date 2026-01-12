<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide89.i18n.MeLanbide89I18n" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.common.service.config.Config"%>
<%@page import="es.altia.common.service.config.ConfigServiceHelper"%>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide89.vo.AccesoVO"%>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide89.util.ConfigurationParameter"%>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide89.util.ConstantesMeLanbide89"%>
<%@page import="java.util.ArrayList" %>
<%@page import="java.util.List" %>

<html>
    <head>
        <meta http-equiv="X-UA-Compatible" content="IE=Edge" />
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <%
            int idiomaUsuario = 1;
            if(request.getParameter("idioma") != null) {
                try {
                    idiomaUsuario = Integer.parseInt(request.getParameter("idioma"));
                } catch(Exception ex) {
                }
            }

            UsuarioValueObject usuarioVO = new UsuarioValueObject();
            int idioma = 1;
            int apl = 5;
            String css = "";
            if (session.getAttribute("usuario") != null) {
                    usuarioVO = (UsuarioValueObject) session.getAttribute("usuario");
                    apl = usuarioVO.getAppCod();
                    idioma = usuarioVO.getIdioma();
                    css = usuarioVO.getCss();
            }

            MeLanbide89I18n meLanbide89I18n = MeLanbide89I18n.getInstance();
            String numExpediente = (String)request.getAttribute("numExp");

        %>
        <jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
        <jsp:setProperty name="descriptor"  property="idi_cod" value="<%=idioma%>" />
        <jsp:setProperty name="descriptor"  property="apl_cod" value="<%=apl%>" />
        <link rel="StyleSheet" media="screen" type="text/css" href="<%=request.getContextPath()%><%=css%>"/>
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/>
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/extension/melanbide89/melanbide89.css"/>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/melanbide89/GoUtils.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/general.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/popup.js"></script>

        <script type="text/javascript">
            var APP_CONTEXT_PATH = '<%=request.getContextPath()%>';
            var url = APP_CONTEXT_PATH + '/PeticionModuloIntegracion.do';

            function pulsarNuevo() {
                lanzarPopUpModal(url + '?tarea=preparar&modulo=MELANBIDE89&operacion=cargarNuevoAcceso&tipo=0&nuevo=1&numExp=<%=numExpediente%>', 900, 1200, 'no', 'no', function (result) {
                    if (result != undefined) {
                        if (result[0] == '0') {
                            recargarTabla(result[1]);
                        }
                    }
                });
            }

            function pulsarModificar() {
                if (tabla.selectedIndex != -1) {
                    lanzarPopUpModal(url + '?tarea=preparar&modulo=MELANBIDE89&operacion=cargarModificarAcceso&tipo=0&nuevo=0&numExp=<%=numExpediente%>&id=' + lista[tabla.selectedIndex][0], 900, 1200, 'no', 'no', function (result) {
                        if (result != undefined) {
                            if (result[0] == '0') {
                                recargarTabla(result[1]);
                            }
                        }
                    });
                } else {
                    jsp_alerta('A', '<%=meLanbide89I18n.getMensaje(idiomaUsuario, "msg.msjNoSelecFila")%>');
                }
            }

            function pulsarEliminar() {
                if (tabla.selectedIndex != -1) {
                    var resultado = jsp_alerta('', '<%=meLanbide89I18n.getMensaje(idiomaUsuario, "msg.preguntaEliminar")%>');
                    if (resultado == 1) {
                        elementoVisible('on', 'barraProgresoGO');
                        console.log('<%=numExpediente%>');
                        console.log(lista[tabla.selectedIndex][0]);
                        var parametros = 'tarea=preparar&modulo=MELANBIDE89&operacion=eliminarAcceso&tipo=0&numExp=<%=numExpediente%>&id=' + lista[tabla.selectedIndex][0];
                        try {
                            $.ajax({
                                url: url,
                                type: 'POST',
                                async: true,
                                data: parametros,
                                success: procesarRespuestaActualizarPestana,
                                error: mostrarErrorEliminar
                            });
                        } catch (Err) {
                            elementoVisible('off', 'barraProgresoGO');
                            mostrarErrorPeticion();
                        }
                    }
                } else {
                    jsp_alerta('A', '<%=meLanbide89I18n.getMensaje(idiomaUsuario, "msg.msjNoSelecFila")%>');
                }
            }

            function iniciarTabla() {
                tabla = new Tabla(true, '<%=descriptor.getDescripcion("buscar")%>', '<%=descriptor.getDescripcion("anterior")%>', '<%=descriptor.getDescripcion("siguiente")%>', '<%=descriptor.getDescripcion("mosFilasPag")%>', '<%=descriptor.getDescripcion("msgNoResultBusq")%>', '<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>', '<%=descriptor.getDescripcion("filtrDeTotal")%>', '<%=descriptor.getDescripcion("primero")%>', '<%=descriptor.getDescripcion("ultimo")%>', document.getElementById('lista'));

                tabla.addColumna('0', 'center', "<%=meLanbide89I18n.getMensaje(idiomaUsuario,"tabla.id")%>");
                tabla.addColumna('100', 'center', "<%=meLanbide89I18n.getMensaje(idiomaUsuario,"tabla.nombre")%>");
                tabla.addColumna('100', 'center', "<%=meLanbide89I18n.getMensaje(idiomaUsuario,"tabla.apellido1")%>");
                tabla.addColumna('100', 'center', "<%=meLanbide89I18n.getMensaje(idiomaUsuario,"tabla.apellido2")%>");
                tabla.addColumna('50', 'center', "<%=meLanbide89I18n.getMensaje(idiomaUsuario,"label.dninie")%>");

                tabla.displayCabecera = true;
                tabla.height = 360;
                tabla.lineas = lista;
                tabla.displayTabla();

            }

            function recargarTabla(accesos) {
                var fila;
                lista = new Array();
                listaTabla = new Array();
                listaTabla_titulos = new Array();
                for (var i = 0; i < accesos.length; i++) {
                    fila = accesos[i];
                    lista[i] = [fila.id, fila.nombre, fila.apellido1, fila.apellido2, fila.dninie];
                    listaTabla[i] = [fila.nombre, fila.apellido1, fila.apellido2, fila.dninie];
                    listaTabla_titulos[i] = [fila.nombre, fila.apellido1, fila.apellido2, fila.dninie];
                }
                iniciarTabla();
            }

            function actualizarPestana() {
                var parametros = 'tarea=preparar&modulo=MELANBIDE89&operacion=actualizarPestana&tipo=0&numExp=<%=numExpediente%>';
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
                    mostrarErrorPeticion();
                }
            }

// FUNCIONES QUE PROCESAN RESPUESTA O ERROR DE LA PETICION AJAX
            function procesarRespuestaActualizarPestana(ajaxResult) {
                var datos = JSON.parse(ajaxResult);
                var codigoOperacion = datos.tabla.codigoOperacion;
                if (codigoOperacion == "0") {
                    var accesos = datos.tabla.lista;
                    elementoVisible('off', 'barraProgresoGO');
                    recargarTabla(accesos);
                } else {
                    mostrarErrorPeticion(codigoOperacion);
                }
            }

            function mostrarErrorEliminar() {
                mostrarErrorPeticion(6);
            }

            function mostrarErrorPeticion(codigo) {
                var msgtitle = "¡¡ ERROR !!";
                elementoVisible('off', 'barraProgresoGO');
                if (codigo == "1") {
                    jsp_alerta("A", '<%=meLanbide89I18n.getMensaje(idiomaUsuario,"error.errorBD")%>', msgtitle);
                } else if (codigo == "2") {
                    jsp_alerta("A", '<%=meLanbide89I18n.getMensaje(idiomaUsuario,"error.errorGen")%>', msgtitle);
                } else if (codigo == "3") {
                    jsp_alerta("A", '<%=meLanbide89I18n.getMensaje(idiomaUsuario,"error.pasoParametros")%>', msgtitle);
                } else if (codigo == "4") {
                    jsp_alerta("A", '<%=meLanbide89I18n.getMensaje(idiomaUsuario,"error.listaVacia")%>', msgtitle);
                } else if (codigo == "5") {
                    jsp_alerta("A", '<%=meLanbide89I18n.getMensaje(idiomaUsuario,"error.errorObtenerDatos")%>', msgtitle);
                } else if (codigo == "6") {
                    jsp_alerta("A", '<%=meLanbide89I18n.getMensaje(idiomaUsuario,"error.errorBorrarDatos")%>', msgtitle);
                } else if (codigo == "-1") {
                    jsp_alerta("A", '<%=meLanbide89I18n.getMensaje(idiomaUsuario,"error.operacionIncorrecta")%>', msgtitle);
                } else {
                    jsp_alerta("A", '<%=meLanbide89I18n.getMensaje(idiomaUsuario,"error.errorGen")%>', msgtitle);
                }
            }

        </script>        
    </head>    
    <body class="bandaBody">
        <div class="tab-page" id="tabPage891" style="height:520px; width: 100%;">
            <h2 class="tab" id="pestana891"><%=meLanbide89I18n.getMensaje(idiomaUsuario,"label.titulo.pestana")%></h2> 
            <script type="text/javascript">tp1.addTabPage(document.getElementById("tabPage891"));</script>
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
            <br/>
            <h2 class="legendAzul" id="pestanaPrinc"><%=meLanbide89I18n.getMensaje(idiomaUsuario,"label.tituloPrincipal")%></h2>
            <div>    
                <br>
                <div id="divGeneral">     
                    <div id="lista"  align="center"></div>
                </div>
                <br>
                <div class="botonera" style="text-align: center;">
                    <input type="button" id="btnNuevo" name="btnNuevo" class="botonGeneral"  value="<%=meLanbide89I18n.getMensaje(idiomaUsuario, "btn.nuevo")%>" onclick="pulsarNuevo();">
                    <input type="button" id="btnModificar" name="btnModificar" class="botonGeneral" value="<%=meLanbide89I18n.getMensaje(idiomaUsuario, "btn.modificar")%>" onclick="pulsarModificar();">
                    <input type="button" id="btnEliminar" name="btnEliminar"   class="botonGeneral" value="<%=meLanbide89I18n.getMensaje(idiomaUsuario, "btn.eliminar")%>" onclick="pulsarEliminar();">
                </div>
            </div>  
        </div>
        <script  type="text/javascript">
            //Tabla
            var tabla;
            var lista = new Array();
            var listaTabla = new Array();
            var listaTabla_titulos = new Array();

            <%  		
               AccesoVO objectVO = null;
               List<AccesoVO> List = null;
               if(request.getAttribute("listaAccesos")!=null){
                   List = (List<AccesoVO>)request.getAttribute("listaAccesos");
               }													
               if (List!= null && List.size() >0){
                   for (int indice=0;indice<List.size();indice++)
                   {
                        objectVO = List.get(indice);
               
                        String nombre = "";
                        if(objectVO.getNombre()!=null){
                           nombre=String.valueOf(objectVO.getNombre());
                        }		   
                        String apellido1 = "";
                        if(objectVO.getApellido1()!=null){
                            apellido1=objectVO.getApellido1();
                        }
                        String apellido2 = "";
                        if(objectVO.getApellido2()!=null){
                                apellido2=objectVO.getApellido2();
                        }
                        String dninie = "";
                        if(objectVO.getDninie()!=null){
                                dninie=objectVO.getDninie();
                        }
                                         
            %>
            lista[<%=indice%>] = ['<%=objectVO.getId()%>', '<%=nombre%>', '<%=apellido1%>', '<%=apellido2%>', '<%=dninie%>'];
            listaTabla[<%=indice%>] = ['<%=nombre%>', '<%=apellido1%>', '<%=apellido2%>', '<%=dninie%>'];
            listaTabla_titulos[<%=indice%>] = ['<%=nombre%>', '<%=apellido1%>', '<%=apellido2%>', '<%=dninie%>'];
            <%
                   }// for
               }// if
            %>
            iniciarTabla();

        </script>
        <div id="popupcalendar" class="text"></div>                
    </body>
</html>
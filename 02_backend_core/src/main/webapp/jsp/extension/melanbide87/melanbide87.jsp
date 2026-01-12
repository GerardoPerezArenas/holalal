<%@taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide87.i18n.MeLanbide87I18n" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.common.service.config.Config"%>
<%@page import="es.altia.common.service.config.ConfigServiceHelper"%>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide87.vo.InstalacionVO"%>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide87.util.ConfigurationParameter"%>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide87.util.ConstantesMeLanbide87"%>
<%@page import="java.util.ArrayList" %>
<%@page import="java.util.List" %>
<%@page import="java.text.DateFormat" %>
<%@page import="java.text.SimpleDateFormat" %>
<html>
    <head>
        <%
            int idiomaUsuario = 1;
            if(request.getParameter("idioma") != null)
            {
                try
                {
                    idiomaUsuario = Integer.parseInt(request.getParameter("idioma"));
                }
                catch(Exception ex)
                {}
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

            //Clase para internacionalizar los mensajes de la aplicación.
            MeLanbide87I18n meLanbide87I18n = MeLanbide87I18n.getInstance();
            String numExpediente = (String)request.getAttribute("numExp");
        %>
        <jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
        <jsp:setProperty name="descriptor"  property="idi_cod" value="<%=idioma%>" />
        <jsp:setProperty name="descriptor"  property="apl_cod" value="<%=apl%>" />
        <link rel="StyleSheet" media="screen" type="text/css" href="<%=request.getContextPath()%><%=css%>"/>
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/>
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/extension/melanbide87/melanbide87.css"/>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/popup.js"></script>
        <script type="text/javascript">
            var APP_CONTEXT_PATH = '<%=request.getContextPath()%>';
            var url = APP_CONTEXT_PATH + '/PeticionModuloIntegracion.do';

            function pulsarNuevaInstalacion() {
                lanzarPopUpModal(url + '?tarea=preparar&modulo=MELANBIDE87&operacion=cargarNuevaInstalacion&tipo=0&nuevo=1&numExp=<%=numExpediente%>', 400, 800, 'no', 'no', function (result) {
                    if (result != undefined) {
                        if (result[0] == '0') {
                            recargarTablaInstalaciones(result[1]);
                        }
                    }
                });
            }

            function pulsarModificarInstalacion() {
                if (tablaInstalaciones.selectedIndex != -1) {
                    lanzarPopUpModal(url + '?tarea=preparar&modulo=MELANBIDE87&operacion=cargarModificarInstalacion&tipo=0&nuevo=0&numExp=<%=numExpediente%>&id=' + listaInstalaciones[tablaInstalaciones.selectedIndex][0], 400, 800, 'no', 'no', function (result) {
                        if (result != undefined) {
                            if (result[0] == '0') {
                                recargarTablaInstalaciones(result[1]);
                            }
                        }
                    });
                } else {
                    jsp_alerta('A', '<%=meLanbide87I18n.getMensaje(idiomaUsuario, "msg.msjNoSelecFila")%>');
                }
            }

            function pulsarEliminarInstalacion() {
                if (tablaInstalaciones.selectedIndex != -1) {
                    var resultado = jsp_alerta('', '<%=meLanbide87I18n.getMensaje(idiomaUsuario, "msg.preguntaEliminar")%>');
                    if (resultado == 1) {
                        elementoVisible('on', 'barraProgresoCOLVU');
                        var parametros = 'tarea=preparar&modulo=MELANBIDE87&operacion=eliminarInstalacion&tipo=0&numExp=<%=numExpediente%>&id=' + listaInstalaciones[tablaInstalaciones.selectedIndex][0];
                        try {
                            $.ajax({
                                url: url,
                                type: 'POST',
                                async: true,
                                data: parametros,
                                success: procesarRespuestaActualizarPestana,
                                error: mostrarErrorEliminarInstalacion
                            });
                        } catch (Err) {
                            elementoVisible('off', 'barraProgresoCOLVU');
                            mostrarErrorPeticion();
                        }
                    }
                } else {
                    jsp_alerta('A', '<%=meLanbide87I18n.getMensaje(idiomaUsuario, "msg.msjNoSelecFila")%>');
                }
            }

            function crearTablaInstalaciones() {
                tablaInstalaciones = new Tabla(true, '<%=descriptor.getDescripcion("buscar")%>', '<%=descriptor.getDescripcion("anterior")%>', '<%=descriptor.getDescripcion("siguiente")%>', '<%=descriptor.getDescripcion("mosFilasPag")%>', '<%=descriptor.getDescripcion("msgNoResultBusq")%>', '<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>', '<%=descriptor.getDescripcion("filtrDeTotal")%>', '<%=descriptor.getDescripcion("primero")%>', '<%=descriptor.getDescripcion("ultimo")%>', document.getElementById('listaInstalaciones'));

                tablaInstalaciones.addColumna('0', 'center', "<%=meLanbide87I18n.getMensaje(idiomaUsuario,"instalacion.id")%>");
                tablaInstalaciones.addColumna('100', 'center', "<%=meLanbide87I18n.getMensaje(idiomaUsuario,"instalacion.tipo")%>");
                tablaInstalaciones.addColumna('100', 'center', "<%=meLanbide87I18n.getMensaje(idiomaUsuario,"instalacion.municipio")%>");
                tablaInstalaciones.addColumna('100', 'center', "<%=meLanbide87I18n.getMensaje(idiomaUsuario,"instalacion.localidad")%>");
                tablaInstalaciones.addColumna('200', 'center', "<%=meLanbide87I18n.getMensaje(idiomaUsuario,"instalacion.direccion")%>");
                tablaInstalaciones.addColumna('50', 'center', "<%=meLanbide87I18n.getMensaje(idiomaUsuario,"instalacion.codPost")%>");

                tablaInstalaciones.displayCabecera = true;
                tablaInstalaciones.height = 360;
            }

            function recargarTablaInstalaciones(instalaciones) {
                listaInstalaciones = new Array();
                listaInstalacionesTabla = new Array();
                var direccion;
                for (var i = 0; i < instalaciones.length; i++) {
                    var instal = instalaciones[i];
                    listaInstalaciones[i] = [instal.id, instal.descTipoInst, instal.municipio, instal.localidad, instal.direccion, instal.codPost];
                    listaInstalacionesTabla[i] = [instal.id, instal.descTipoInst, instal.municipio, instal.localidad, instal.direccion, instal.codPost];
                }
                crearTablaInstalaciones();
                tablaInstalaciones.lineas = listaInstalacionesTabla;
                tablaInstalaciones.displayTabla();
            }

// FUNCIONES QUE PROCESAN RESPUESTA O ERROR DE LA PETICION AJAX
            function procesarRespuestaActualizarPestana(ajaxResult) {
                var datos = JSON.parse(ajaxResult);
                var codigoOperacion = datos.tabla.codigoOperacion;
                if (codigoOperacion == "0") {
                    var instalaciones = datos.tabla.lista;
                    if (instalaciones.length > 0) {
                        elementoVisible('off', 'barraProgresoCOLVU');
                        recargarTablaInstalaciones(instalaciones);
                   //     jsp_alerta("A", '<%=meLanbide87I18n.getMensaje(idiomaUsuario,"msg.registrosCargadosOK")%>');
                    } else {
                        mostrarErrorPeticion();
                    }
                } else {
                    mostrarErrorPeticion(codigoOperacion);
                }
            }

            function mostrarErrorEliminarInstalacion() {
                mostrarErrorPeticion(6);
            }

            function mostrarErrorActualizarPestana() {
                mostrarErrorPeticion();
            }

            function mostrarErrorPeticion(codigo) {
                var msgtitle = "¡¡ ERROR !!";
                elementoVisible('off', 'barraProgresoCOLVU');
                if (codigo == "1") {
                    jsp_alerta("A", '<%=meLanbide87I18n.getMensaje(idiomaUsuario,"error.errorBD")%>', msgtitle);
                } else if (codigo == "2") {
                    jsp_alerta("A", '<%=meLanbide87I18n.getMensaje(idiomaUsuario,"error.errorGen")%>', msgtitle);
                } else if (codigo == "3") {
                    jsp_alerta("A", '<%=meLanbide87I18n.getMensaje(idiomaUsuario,"error.pasoParametros")%>', msgtitle);
                } else if (codigo == "4") {
                    jsp_alerta("A", '<%=meLanbide87I18n.getMensaje(idiomaUsuario,"error.listaVacia")%>', msgtitle);
                } else if (codigo == "5") {
                    jsp_alerta("A", '<%=meLanbide87I18n.getMensaje(idiomaUsuario,"error.errorObtenerDatos")%>', msgtitle);
                } else if (codigo == "6") {
                    jsp_alerta("A", '<%=meLanbide87I18n.getMensaje(idiomaUsuario,"error.errorBorrarDatos")%>', msgtitle);
                }  else if (codigo == "-1") {
                    jsp_alerta("A", '<%=meLanbide87I18n.getMensaje(idiomaUsuario,"error.operacionIncorrecta")%>', msgtitle);
                } else {
                    jsp_alerta("A", '<%=meLanbide87I18n.getMensaje(idiomaUsuario,"error.errorGen")%>', msgtitle);
                }
            }

            function elementoVisible(valor, idBarra) {
                if (valor == 'on') {
                    document.getElementById(idBarra).style.visibility = 'inherit';
                } else if (valor == 'off') {
                    document.getElementById(idBarra).style.visibility = 'hidden';
                }
            }
        </script> 
    </head>
    <body class="bandaBody" onload="javascript:{
                elementoVisible('off', 'barraProgresoCOLVU');
            }" >
        <div class="tab-page" id="tabPage352" style="height:520px; width: 100%;">
            <h2 class="tab" id="pestana352"><%=meLanbide87I18n.getMensaje(idiomaUsuario,"label.titulo.pestana")%></h2> 
            <script type="text/javascript">tp1.addTabPage(document.getElementById("tabPage352"));</script>
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
            <br/>
            <h2 class="legendAzul" id="pestanaPrinc"><%=meLanbide87I18n.getMensaje(idiomaUsuario,"label.tituloPrincipal")%></h2>
            <div>    
                <div id="divGeneral">     
                    <div id="listaInstalaciones"  align="center"></div>
                </div>
                <div class="botonera" style="text-align: center;">
                    <input type="button" id="btnNuevaInstalacion" name="btnNuevaInstalacion" class="botonGeneral"  value="<%=meLanbide87I18n.getMensaje(idiomaUsuario, "btn.nuevo")%>" onclick="pulsarNuevaInstalacion();">
                    <input type="button" id="btnModificarInstalacion" name="btnModificarInstalacion" class="botonGeneral" value="<%=meLanbide87I18n.getMensaje(idiomaUsuario, "btn.modificar")%>" onclick="pulsarModificarInstalacion();">
                    <input type="button" id="btnEliminarInstalacion" name="btnEliminarInstalacion"   class="botonGeneral" value="<%=meLanbide87I18n.getMensaje(idiomaUsuario, "btn.eliminar")%>" onclick="pulsarEliminarInstalacion();">
                </div>
            </div>  
        </div>
        <script  type="text/javascript">
            //Tabla Instalaciones
            var tablaInstalaciones;
            var listaInstalaciones = new Array();
            var listaInstalacionesTabla = new Array();

            crearTablaInstalaciones();
            <%  		
               InstalacionVO objectVO = null;
               List<InstalacionVO> List = null;
               if(request.getAttribute("listaInstalaciones")!=null){
                   List = (List<InstalacionVO>)request.getAttribute("listaInstalaciones");
               }													
               if (List!= null && List.size() >0){
                   for (int indice=0;indice<List.size();indice++)
                   {
                       objectVO = List.get(indice);
               
                       String tipo="";
                       if(objectVO.getDescTipoInst()!=null){
                           tipo=objectVO.getDescTipoInst();
                       }else{
                           tipo="-";
                       }
                       String municipio = "";
                       if(objectVO.getMunicipio()!=null){
                           municipio=objectVO.getMunicipio();
                       }else{
                           municipio="-";
                       }
                       String localidad = "";               
                       if(objectVO.getLocalidad()!=null){
                           localidad=objectVO.getLocalidad();
                       }else{
                           localidad="-";
                       }
                       String direccion = "";
                       if(objectVO.getDireccion()!=null){
                           direccion=objectVO.getDireccion();
                       } 
                        String codP = ""; //Integer
                        if(objectVO.getCodPost()!=null){
                           codP = String.valueOf(objectVO.getCodPost());
                       }                       
            %>
            listaInstalaciones[<%=indice%>] = ['<%=objectVO.getId()%>', '<%=tipo%>', '<%=municipio%>', '<%=localidad%>', '<%=direccion%>', '<%=codP%>'];
            listaInstalacionesTabla[<%=indice%>] = ['<%=objectVO.getId()%>', '<%=tipo%>', '<%=municipio%>', '<%=localidad%>', '<%=direccion%>', '<%=codP%>'];
            <%
                   }// for
               }// if
            %>
            tablaInstalaciones.lineas = listaInstalacionesTabla;
            tablaInstalaciones.displayTabla();

            if (navigator.appName.indexOf("Internet Explorer") != -1 || (navigator.appName.indexOf("Netscape") != -1 && navigator.appCodeName.match(/Mozilla/))) {
                try {
                    var div = document.getElementById('listaInstalaciones');
                    if (navigator.appName.indexOf("Internet Explorer") != -1 && !(navigator.userAgent.match(/compatible/))) {
                        div.children[0].children[0].children[0].children[0].children[0].children[0].children[1].style.width = '100%';
                        div.children[0].children[1].children[0].children[0].children[0].children[0].style.width = '100%';
                    } else {
                        div.children[0].children[0].children[0].children[0].children[0].children[0].style.width = '100%';
                        div.children[0].children[1].children[0].children[0].style.width = '100%';
                    }
                } catch (err) {
                }
            }
        </script>
        <div id="popupcalendar" class="text"></div>                
    </body>
</html>



<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide17.i18n.MeLanbide17I18n" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.common.service.config.Config"%>
<%@page import="es.altia.common.service.config.ConfigServiceHelper"%>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide17.vo.FilaMinimisVO"%>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide17.util.ConfigurationParameter"%>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide17.util.ConstantesMeLanbide17"%>
<%@page import="java.util.ArrayList" %>
<%@page import="java.util.List" %>
<%@page import="java.text.DateFormat" %>
<%@page import="java.text.SimpleDateFormat" %>
<html>
    <head>
        <meta http-equiv="X-UA-Compatible" content="IE=Edge"/>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
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

            MeLanbide17I18n meLanbide17I18n = MeLanbide17I18n.getInstance();
            String numExpediente = (String)request.getAttribute("numExp");

        %>
        <jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
        <jsp:setProperty name="descriptor"  property="idi_cod" value="<%=idiomaUsuario%>" />
        <jsp:setProperty name="descriptor"  property="apl_cod" value="<%=apl%>" />
        <link rel="StyleSheet" media="screen" type="text/css" href="<%=request.getContextPath()%><%=css%>"/>
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/>
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/extension/melanbide17/melanbide17.css"/>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/melanbide17/utils.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/popup.js"></script>
        <script type="text/javascript">
            var APP_CONTEXT_PATH = '<%=request.getContextPath()%>';
            var url = APP_CONTEXT_PATH + '/PeticionModuloIntegracion.do';

            function pulsarNuevoMinimis() {
                lanzarPopUpModal(url + '?tarea=preparar&modulo=MELANBIDE17&operacion=cargarNuevoMinimis&tipo=0&nuevo=1&numExp=<%=numExpediente%>', 500, 1200, 'no', 'no', function (result) {
                    if (result != undefined) {
                        if (result[0] == '0') {
                            recargarTablaMinimis(result[1]);
                        }
                    }
                });
            }

            function pulsarModificarMinimis() {
                if (tablaMinimis.selectedIndex != -1) {
                    lanzarPopUpModal(url + '?tarea=preparar&modulo=MELANBIDE17&operacion=cargarModificarMinimis&tipo=0&nuevo=0&numExp=<%=numExpediente%>&id=' + listaMinimis[tablaMinimis.selectedIndex][0], 500, 1200, 'no', 'no', function (result) {
                        if (result != undefined) {
                            if (result[0] == '0') {
                                recargarTablaMinimis(result[1]);
                            }
                        }
                    });
                } else {
                    jsp_alerta('A', '<%=meLanbide17I18n.getMensaje(idiomaUsuario, "msg.msjNoSelecFila")%>');
                }
            }

            function pulsarEliminarMinimis() {
                if (tablaMinimis.selectedIndex != -1) {
                    var resultado = jsp_alerta('', '<%=meLanbide17I18n.getMensaje(idiomaUsuario, "msg.preguntaEliminar")%>');
                    if (resultado == 1) {
                        var parametros = 'tarea=preparar&modulo=MELANBIDE17&operacion=eliminarMinimis&tipo=0&numExp=<%=numExpediente%>&id=' + listaMinimis[tablaMinimis.selectedIndex][0];
                        try {
                            $.ajax({
                                url: url,
                                type: 'POST',
                                async: true,
                                data: parametros,
                                success: procesarRespuestaActualizarPestana,
                                error: mostrarErrorEliminarMinimis
                            });
                        } catch (Err) {
                            mostrarErrorPeticion();
                        }
                    }
                } else {
                    jsp_alerta('A', '<%=meLanbide17I18n.getMensaje(idiomaUsuario, "msg.msjNoSelecFila")%>');
                }
            }

            function iniciarTablaMinimis() {
                tablaMinimis = new Tabla(true, '<%=descriptor.getDescripcion("buscar")%>', '<%=descriptor.getDescripcion("anterior")%>', '<%=descriptor.getDescripcion("siguiente")%>', '<%=descriptor.getDescripcion("mosFilasPag")%>', '<%=descriptor.getDescripcion("msgNoResultBusq")%>', '<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>', '<%=descriptor.getDescripcion("filtrDeTotal")%>', '<%=descriptor.getDescripcion("primero")%>', '<%=descriptor.getDescripcion("ultimo")%>', document.getElementById('listaMinimis'));

                tablaMinimis.addColumna('0', 'center', "<%=meLanbide17I18n.getMensaje(idiomaUsuario,"minimis.tablaMinimis.id")%>");
                tablaMinimis.addColumna('100', 'center', "<%=meLanbide17I18n.getMensaje(idiomaUsuario,"minimis.tablaMinimis.estado")%>");
                tablaMinimis.addColumna('400', 'left', "<%=meLanbide17I18n.getMensaje(idiomaUsuario,"minimis.tablaMinimis.organismo")%>");
                tablaMinimis.addColumna('820', 'left', "<%=meLanbide17I18n.getMensaje(idiomaUsuario,"minimis.tablaMinimis.objeto")%>");
                tablaMinimis.addColumna('100', 'right', "<%=meLanbide17I18n.getMensaje(idiomaUsuario,"minimis.tablaMinimis.importe")%>");
                tablaMinimis.addColumna('100', 'center', "<%=meLanbide17I18n.getMensaje(idiomaUsuario,"minimis.tablaMinimis.fecha")%>" + " <%=meLanbide17I18n.getMensaje(idiomaUsuario,"minimis.tablaMinimis.asterisco")%>");

                tablaMinimis.displayCabecera = true;
                tablaMinimis.height = 360;
            }

            function recargarTablaMinimis(result) {
                var fila;
                listaMinimis = new Array();
                listaMinimisTabla = new Array();
                listaMinimisTabla_titulos = new Array();
                for (var i = 0; i < result.length; i++) {
                    fila = result[i];
                    listaMinimis[i] = [fila.id, fila.desEstado, fila.organismo, fila.objeto, fila.importe.toString().replace(".", ","), fila.fecha, fila.fechaStr];
                    listaMinimisTabla[i] = [fila.id, fila.desEstado, fila.organismo, fila.objeto, fila.importe.toString().replace(".", ","), fila.fechaStr];
                    listaMinimisTabla_titulos[i] = [fila.id, fila.desEstado, fila.organismo, fila.objeto, fila.importe.toString().replace(".", ","), fila.fechaStr];
                }
                iniciarTablaMinimis();
                tablaMinimis.lineas = listaMinimisTabla;
                tablaMinimis.displayTabla();
            }

            function procesarRespuestaActualizarPestana(ajaxResult) {
                var datos = JSON.parse(ajaxResult);
                var codigoOperacion = datos.tabla.codigoOperacion;
                if (codigoOperacion == "0") {
                    var result = datos.tabla.lista;
                    recargarTablaMinimis(result);
                } else {
                    mostrarErrorPeticion(codigoOperacion);
                }
            }

            function mostrarErrorEliminarMinimis() {
                mostrarErrorPeticion("6");
            }

            function mostrarErrorPeticion(codigo) {
                var msgtitle = "¡¡ ERROR !!";
                elementoVisible('off', 'barraProgreso');
                if (codigo == "1") {
                    jsp_alerta("A", '<%=meLanbide17I18n.getMensaje(idiomaUsuario,"error.errorBD")%>', msgtitle);
                } else if (codigo == "2") {
                    jsp_alerta("A", '<%=meLanbide17I18n.getMensaje(idiomaUsuario,"error.errorGen")%>', msgtitle);
                } else if (codigo == "3") {
                    jsp_alerta("A", '<%=meLanbide17I18n.getMensaje(idiomaUsuario,"error.pasoParametros")%>', msgtitle);
                } else if (codigo == "4") {
                    jsp_alerta("A", '<%=meLanbide17I18n.getMensaje(idiomaUsuario,"error.listaVacia")%>', msgtitle);
                } else if (codigo == "5") {
                    jsp_alerta("A", '<%=meLanbide17I18n.getMensaje(idiomaUsuario,"error.errorObtenerDatos")%>', msgtitle);
                } else if (codigo == "6") {
                    jsp_alerta("A", '<%=meLanbide17I18n.getMensaje(idiomaUsuario,"error.errorBorrarDatos")%>', msgtitle);
                } else if (codigo == "-1") {
                    jsp_alerta("A", '<%=meLanbide17I18n.getMensaje(idiomaUsuario,"error.operacionIncorrecta")%>', msgtitle);
                } else {
                    jsp_alerta("A", '<%=meLanbide17I18n.getMensaje(idiomaUsuario,"error.errorGen")%>', msgtitle);
                }
            }

        </script>
    </head>
    <body class="bandaBody">
        <div class="tab-page" id="tabPage172" style="height:520px; width: 100%;">
            <h2 class="tab" id="pestana172"><%=meLanbide17I18n.getMensaje(idiomaUsuario,"label.titulo.pestana")%></h2> 
            <script type="text/javascript">tp1.addTabPage(document.getElementById("tabPage172"));</script>
            <br/>
            <h2 class="legendAzul" id="pestanaPrinc"><%=meLanbide17I18n.getMensaje(idiomaUsuario,"label.tituloPrincipal")%></h2>
            <div>    
                <br>
                <div id="divGeneral">     
                    <div id="listaMinimis"  align="center"></div>
                </div>
                <br>
                <div class="legendRojo" id="notaFecha"><%=meLanbide17I18n.getMensaje(idiomaUsuario,"label.notaFecha")%></div>    
                <div class="botonera" style="text-align: center;">
                    <input type="button" id="btnNuevoMinimis" name="btnNuevoMinimis" class="botonGeneral"  value="<%=meLanbide17I18n.getMensaje(idiomaUsuario, "btn.nuevo")%>" onclick="pulsarNuevoMinimis();">
                    <input type="button" id="btnModificarMinimis" name="btnModificarMinimis" class="botonGeneral" value="<%=meLanbide17I18n.getMensaje(idiomaUsuario, "btn.modificar")%>" onclick="pulsarModificarMinimis();">
                    <input type="button" id="btnEliminarMinimis" name="btnEliminarMinimis"   class="botonGeneral" value="<%=meLanbide17I18n.getMensaje(idiomaUsuario, "btn.eliminar")%>" onclick="pulsarEliminarMinimis();">
                </div>
            </div>  
        </div>
        <script  type="text/javascript">
            //Tabla Minimis
            var tablaMinimis;
            var listaMinimis = new Array();
            var listaMinimisTabla = new Array();
            var listaMinimisTabla_titulos = new Array();
            var listaMinimisTabla_estilos = new Array();

            <% 		
               FilaMinimisVO objectVO = null;
               List<FilaMinimisVO> List = null;
               if(request.getAttribute("listaMinimis")!=null){
                   List = (List<FilaMinimisVO>)request.getAttribute("listaMinimis");
               }													
               if (List!= null && List.size() >0){
                   DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                   for (int indice=0;indice<List.size();indice++)
                   {
                        objectVO = List.get(indice);
                     
                        String estado="";
                        if(objectVO.getDesEstado()!=null){
                            String descripcion = objectVO.getDesEstado();

                            String barraSeparadoraDobleIdiomaDesple = ConfigurationParameter.getParameter(ConstantesMeLanbide17.BARRA_SEPARADORA_IDIOMA_DESPLEGABLES, ConstantesMeLanbide17.FICHERO_PROPIEDADES);
                            String[] descripcionDobleIdioma = (descripcion!=null?descripcion.split(barraSeparadoraDobleIdiomaDesple):null);
                            if(descripcionDobleIdioma!=null && descripcionDobleIdioma.length>1){
                                if(idiomaUsuario==ConstantesMeLanbide17.CODIGO_IDIOMA_EUSKERA){
                                    descripcion=descripcionDobleIdioma[1];
                                }else{
                                    // Cogemos la primera posición que debería ser castellano
                                    descripcion=descripcionDobleIdioma[0];
                                }
                            }
                            estado = descripcion;
                        }else{
                            estado="-";
                        }

                        String organismo="";
                        if(objectVO.getOrganismo()!=null){
                            organismo=objectVO.getOrganismo().replaceAll("\\n", " ");
                        }else{
                            organismo="-";
                        }
                        String objeto="";
                        if(objectVO.getObjeto()!=null){
                            objeto=objectVO.getObjeto().replaceAll("\\n", " ");
                        }else{
                            objeto="-";
                        }
                        String importe="";
                        if(objectVO.getImporte()!=null){
                            importe=String.valueOf((objectVO.getImporte().toString()).replace(".",","));
                        }else{
                            importe="-";
                        }
                        String fecha="";
                        if(objectVO.getFecha()!=null){
                            fecha=dateFormat.format(objectVO.getFecha());
                        }else{
                            fecha="-";
                        }
                               
            %>
            listaMinimis[<%=indice%>] = ['<%=objectVO.getId()%>', '<%=estado%>', '<%=organismo%>', '<%=objeto%>', '<%=importe%>', '<%=fecha%>'];
            listaMinimisTabla[<%=indice%>] = ['<%=objectVO.getId()%>', '<%=estado%>', '<%=organismo%>', '<%=objeto%>', '<%=importe%>', '<%=fecha%>'];
            listaMinimisTabla_titulos[<%=indice%>] = ['<%=objectVO.getId()%>', '<%=estado%>', '<%=organismo%>', '<%=objeto%>', '<%=importe%>', '<%=fecha%>'];
            <%
                   }// for
               }// if
            %>
            iniciarTablaMinimis();
            tablaMinimis.lineas = listaMinimisTabla;
            tablaMinimis.displayTabla();
        </script>
    </body>
</html>
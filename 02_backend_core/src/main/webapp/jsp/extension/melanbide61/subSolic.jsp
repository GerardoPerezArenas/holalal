<%-- 
    Document   : subSolic
    Created on : 26-dic-2022, 8:21:10
    Author     : gerardo
--%>
<%@taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@taglib uri="/WEB-INF/tlds/c.tld" prefix="c"%>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject"%>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide61.i18n.MeLanbide61I18n"%>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide61.vo.SubSolicVO"%>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide61.util.ConfigurationParameter"%>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide61.util.ConstantesMeLanbide61"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="java.text.DateFormat"%>
<%@page import="java.text.SimpleDateFormat"%>
<html>
    <head>
        <meta http-equiv="X-UA-Compatible" content="IE=Edge" />
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <%
            int idiomaUsuario = 1;
            int apl = 5;
            String css = "";
            UsuarioValueObject usuario = new UsuarioValueObject();
            try {
                if (session != null) {
                    if (usuario != null) {
                        usuario = (UsuarioValueObject) session.getAttribute("usuario");
                        idiomaUsuario = usuario.getIdioma();
                        apl = usuario.getAppCod();
                        css = usuario.getCss();
                    }
                }
            } catch(Exception ex) {}
            
            String numExpediente = request.getParameter("numero");
            MeLanbide61I18n meLanbide61I18n = MeLanbide61I18n.getInstance();

        %>
        <jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
        <jsp:setProperty name="descriptor"  property="idi_cod" value="<%=idiomaUsuario%>" />
        <jsp:setProperty name="descriptor"  property="apl_cod" value="<%=apl%>" />
        <link rel="StyleSheet" media="screen" type="text/css" href="<%=request.getContextPath()%><%=css%>"/>
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/>
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/extension/melanbide61/melanbide61.css"/>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/melanbide61/ecaUtils.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/popup.js"></script>
        <script type="text/javascript">
            var APP_CONTEXT_PATH = '<%=request.getContextPath()%>';
            var url = APP_CONTEXT_PATH + '/PeticionModuloIntegracion.do';

            function pulsarNuevoSubSolic() {
                lanzarPopUpModal(url + '?tarea=preparar&modulo=MELANBIDE61&operacion=cargarNuevoSubSolic&tipo=0&nuevo=1&numExp=<%=numExpediente%>', 500, 1100, 'no', 'no', function (result) {
                    if (result != undefined) {
                        if (result[0] == '0') {
                            recargarTablaSubSolic(result[1]);
                        }
                    }
                });
            }

            function pulsarModificarSubSolic() {
                if (tablaSubSolic.selectedIndex != -1) {
                    lanzarPopUpModal(url + '?tarea=preparar&modulo=MELANBIDE61&operacion=cargarModificarSubSolic&tipo=0&nuevo=0&numExp=<%=numExpediente%>&id=' + listaSubSolic[tablaSubSolic.selectedIndex][0], 500, 1100, 'no', 'no', function (result) {
                        if (result != undefined) {
                            if (result[0] == '0') {
                                recargarTablaSubSolic(result[1]);
                            }
                        }
                    });
                } else {
                    jsp_alerta('A', '<%=meLanbide61I18n.getMensaje(idiomaUsuario, "msg.msjNoSelecFila")%>');
                }
            }

            function pulsarEliminarSubSolic() {
                if (tablaSubSolic.selectedIndex != -1) {
                    var resultado = jsp_alerta('', '<%=meLanbide61I18n.getMensaje(idiomaUsuario, "msg.preguntaEliminar")%>');
                    if (resultado == 1) {
                        barraProgresoReple('on', 'barraProgresoREPLE');
                        var parametros = 'tarea=preparar&modulo=MELANBIDE61&operacion=eliminarSubSolic&tipo=0&numExp=<%=numExpediente%>&id=' + listaSubSolic[tablaSubSolic.selectedIndex][0];
                        try {
                            $.ajax({
                                url: url,
                                type: 'POST',
                                async: true,
                                data: parametros,
                                success: procesarRespuestaPeticion,
                                error: mostrarErrorEliminarSubSolic
                            });
                        } catch (Err) {
                            barraProgresoReple('off', 'barraProgresoREPLE');
                            mostrarErrorPeticion();
                        }
                    }
                } else {
                    jsp_alerta('A', '<%=meLanbide61I18n.getMensaje(idiomaUsuario, "msg.msjNoSelecFila")%>');
                }
            }

            function crearTablaSubSolic() {
                tablaSubSolic = new Tabla(true, '<%=descriptor.getDescripcion("buscar")%>', '<%=descriptor.getDescripcion("anterior")%>', '<%=descriptor.getDescripcion("siguiente")%>', '<%=descriptor.getDescripcion("mosFilasPag")%>', '<%=descriptor.getDescripcion("msgNoResultBusq")%>', '<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>', '<%=descriptor.getDescripcion("filtrDeTotal")%>', '<%=descriptor.getDescripcion("primero")%>', '<%=descriptor.getDescripcion("ultimo")%>', document.getElementById('listaSubSolic'));
                tablaSubSolic.addColumna('0', 'center', "<%=meLanbide61I18n.getMensaje(idiomaUsuario,"tabla.subSolic.id")%>");
                tablaSubSolic.addColumna('100', 'center', "<%=meLanbide61I18n.getMensaje(idiomaUsuario,"tabla.subSolic.estado")%>");
                tablaSubSolic.addColumna('400', 'left', "<%=meLanbide61I18n.getMensaje(idiomaUsuario,"tabla.subSolic.organismo")%>");
                tablaSubSolic.addColumna('400', 'left', "<%=meLanbide61I18n.getMensaje(idiomaUsuario,"tabla.subSolic.objeto")%>");
                tablaSubSolic.addColumna('100', 'right', "<%=meLanbide61I18n.getMensaje(idiomaUsuario,"tabla.subSolic.importe")%>");
                tablaSubSolic.addColumna('100', 'center', "<%=meLanbide61I18n.getMensaje(idiomaUsuario,"tabla.subSolic.fecha")%>"  + "<%=meLanbide61I18n.getMensaje(idiomaUsuario,"tabla.subSolic.asterisco")%>" );

                tablaSubSolic.displayCabecera = true;
                tablaSubSolic.height = 360;
            }

            function recargarTablaSubSolic(subSolic) {
                var fila;
                listaSubSolic = new Array();
                listaSubSolicTabla = new Array();
                listaSubSolicTabla_titulos = new Array();
                
                if(subSolic!=null && subSolic!= undefined){
                     for (var i = 0; i < subSolic.length; i++) {
                        fila = subSolic[i];
                        listaSubSolic[i] = [fila.id, fila.estado, fila.desEstado, fila.organismo, fila.objeto, fila.importe.toString().replace(".",","), fila.fecha];
                        listaSubSolicTabla[i] = [fila.id, fila.desEstado, fila.organismo, fila.objeto, fila.importe.toString().replace(".",","), fila.fecha];
                        listaSubSolicTabla_titulos[i] = [fila.id, fila.desEstado, fila.organismo, fila.objeto, fila.importe.toString().replace(".",","), fila.fecha];
                    }
                }               
                crearTablaSubSolic();
                tablaSubSolic.lineas = listaSubSolicTabla;
                tablaSubSolic.displayTabla();
            }

            function procesarRespuestaPeticion(ajaxResult) {
                var datos = JSON.parse(ajaxResult);
                var codigoOperacion = datos.tabla.codigoOperacion;
                if (codigoOperacion == "0") {
                     barraProgresoReple('off', 'barraProgresoREPLE');
                    var subSolic = datos.tabla.lista;
                     recargarTablaSubSolic(subSolic);
                } else {
                    mostrarErrorPeticion(codigoOperacion);
                }
            }

            function mostrarErrorEliminarSubSolic() {
                mostrarErrorPeticion(6);
            }

            function mostrarErrorPeticion(codigo) {
                var msgtitle = "¡¡ ERROR !!";
                barraProgresoReple('off', 'barraProgresoREPLE');
                if (codigo == "1") {
                    jsp_alerta("A", '<%=meLanbide61I18n.getMensaje(idiomaUsuario,"error.errorBD")%>', msgtitle);
                } else if (codigo == "2") {
                    jsp_alerta("A", '<%=meLanbide61I18n.getMensaje(idiomaUsuario,"error.generico")%>', msgtitle);
                } else if (codigo == "3") {
                    jsp_alerta("A", '<%=meLanbide61I18n.getMensaje(idiomaUsuario,"error.pasoParametros")%>', msgtitle);
                } else if (codigo == "4") {
                    jsp_alerta("A", '<%=meLanbide61I18n.getMensaje(idiomaUsuario,"error.listaVacia")%>', msgtitle);
                } else if (codigo == "5") {
                    jsp_alerta("A", '<%=meLanbide61I18n.getMensaje(idiomaUsuario,"error.errorObtenerDatos")%>', msgtitle);
                } else if (codigo == "6") {
                    jsp_alerta("A", '<%=meLanbide61I18n.getMensaje(idiomaUsuario,"error.errorBorrarDatos")%>', msgtitle);
                } else if (codigo == "-1") {
                    jsp_alerta("A", '<%=meLanbide61I18n.getMensaje(idiomaUsuario,"error.operacionIncorrecta")%>', msgtitle);
                } else {
                    jsp_alerta("A", '<%=meLanbide61I18n.getMensaje(idiomaUsuario,"error.generico")%>', msgtitle);
                }
            }
        </script>
    </head>
    <body class="bandaBody"onload="javascript:{
                barraProgresoReple('off', 'barraProgresoREPLE');
            }" >
        <div class="tab-page" id="tabPage614" style="height:220px; width: 100%;">
            <h2 class="tab" id="pestana614"><%=meLanbide61I18n.getMensaje(idiomaUsuario,"label.subSolic.pestanaSubSolic")%></h2> 
            <script type="text/javascript">tp1.addTabPage(document.getElementById("tabPage614"));</script>
            <div id="barraProgresoREPLE" style="position: absolute; z-index:10; visibility: hidden; top: 30%; left: 30%;">
                <div class="contenedorHidepage">
                    <div class="textoHide">
                        <span>
                            <%=meLanbide61I18n.getMensaje(idiomaUsuario, "msg.cargandoDatos")%>
                        </span>
                    </div>
                    <div class="imagenHide">
                        <span id="disco" class="fa fa-spinner fa-spin" aria-hidden="true"/>
                    </div>
                </div>
            </div>
            <br/>
            <h2 class="legendAzul" id="pestanaPrinc"><%=meLanbide61I18n.getMensaje(idiomaUsuario,"label.subSolic.tituloPrincipal")%></h2>
            <div>    
                <div id="divGeneral">     
                    <div id="listaSubSolic"  align="center"></div>
                </div>
                <br>
                <div class="legendRojo" id="notaFecha"><%=meLanbide61I18n.getMensaje(idiomaUsuario,"label.subSolic.notaFecha")%></div>
                <div class="botonera" style="text-align: center;">
                    <input type="button" id="btnNuevaSubSolic" name="btnNuevaSubSolic" class="botonGeneral"  value="<%=meLanbide61I18n.getMensaje(idiomaUsuario, "btn.alta")%>" onclick="pulsarNuevoSubSolic();">
                    <input type="button" id="btnModificarSubSolic" name="btnModificarSubSolic" class="botonGeneral" value="<%=meLanbide61I18n.getMensaje(idiomaUsuario, "btn.modificar")%>" onclick="pulsarModificarSubSolic();">
                    <input type="button" id="btnEliminarSubSolic" name="btnEliminarSubSolic"   class="botonGeneral" value="<%=meLanbide61I18n.getMensaje(idiomaUsuario, "btn.eliminar")%>" onclick="pulsarEliminarSubSolic();">
                </div>
            </div> 
        </div>
        <script  type="text/javascript">
            //Tabla SubSolic
            var tablaSubSolic;
            var listaSubSolic = new Array();
            var listaSubSolicTabla = new Array();
            var listaSubSolicTabla_titulos = new Array();
            <%
                SubSolicVO objectVO = null;
                List<SubSolicVO> List = null;
                if(request.getAttribute("listaSubSolic")!=null) {
                    List =(List<SubSolicVO>)request.getAttribute("listaSubSolic");
                }
                if (List!= null && List.size() >0){
                    for (int indice=0;indice<List.size();indice++) {
                        objectVO = List.get(indice);
                        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

                        String estado="-";
                        String descripcion = "-";
                        if(objectVO.getDesEstado()!=null){
                            descripcion = objectVO.getDesEstado();
                            String barraSeparadora = "\\" + ConstantesMeLanbide61.PIPE;
                            String[] descripcionDobleIdioma= (descripcion != null ? descripcion.split(barraSeparadora) : null);
                            if(descripcionDobleIdioma!=null && descripcionDobleIdioma.length>1){
                                if(idiomaUsuario==ConstantesMeLanbide61.CODIGO_IDIOMA_EUSKERA){
                                    descripcion=descripcionDobleIdioma[1];
                                }else{
                                    descripcion=descripcionDobleIdioma[0];
                                }
                            }
                        }

                        String organismo="-";
                        if(objectVO.getOrganismo()!=null){
                            organismo=objectVO.getOrganismo();
                        }

                        String objeto="-";
                        if(objectVO.getObjeto()!=null){
                            objeto=objectVO.getObjeto();
                        }

                        String importe="-";
                        if(objectVO.getImporte()!=null){
                            importe=String.valueOf((objectVO.getImporte().toString()).replace(".",","));
                        }

                        String fecha="-";
                        if(objectVO.getFecha()!=null){
                            fecha=dateFormat.format(objectVO.getFecha());
                        }
            %>
            listaSubSolic[<%=indice%>] = ['<%=objectVO.getId()%>', '<%=estado%>', '<%=descripcion%>', '<%=organismo%>', '<%=objeto%>', '<%=importe%>', '<%=fecha%>'];
            listaSubSolicTabla[<%=indice%>] = ['<%=objectVO.getId()%>', '<%=descripcion%>', '<%=organismo%>', '<%=objeto%>', '<%=importe%>', '<%=fecha%>'];
            listaSubSolicTabla_titulos[<%=indice%>] = ['<%=objectVO.getId()%>', '<%=descripcion%>', '<%=organismo%>', '<%=objeto%>', '<%=importe%>', '<%=fecha%>'];
            <%
                    }
                }
            %>
            crearTablaSubSolic();
            tablaSubSolic.lineas = listaSubSolicTabla;
            tablaSubSolic.displayTabla();
        </script>
    </body>
</html>
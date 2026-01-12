<%-- 
    Document   : m81Proyectos
    Created on : 05-abr-2022, 14:31:50
    Author     : kepa
--%>
<%@taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.common.service.config.Config"%>
<%@page import="es.altia.common.service.config.ConfigServiceHelper"%>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide81.i18n.MeLanbide81I18n" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide81.util.ConfigurationParameter"%>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide81.util.ConstantesMeLanbide81"%>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide81.vo.ProyectoVO"%>
<%@page import="java.util.ArrayList" %>
<%@page import="java.util.List" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>  
        <meta http-equiv="X-UA-Compatible" content="IE=Edge" />
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <%
                UsuarioValueObject usuarioVO = new UsuarioValueObject();
                int idiomaUsuario = 1;
                int apl = 5;
                String css = "";
                if (session.getAttribute("usuario") != null){
                    usuarioVO = (UsuarioValueObject) session.getAttribute("usuario");
                    apl = usuarioVO.getAppCod();
                    idiomaUsuario = usuarioVO.getIdioma();
                    css = usuarioVO.getCss();
                }
        
                //Clase para internacionalizar los mensajes de la aplicación.
                MeLanbide81I18n meLanbide81I18n = MeLanbide81I18n.getInstance();
                String numExpediente = (String)request.getAttribute("numExp");               
        %>
        <jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
        <jsp:setProperty name="descriptor"  property="idi_cod" value="<%=idiomaUsuario%>" />
        <jsp:setProperty name="descriptor"  property="apl_cod" value="<%=apl%>" />
        <link rel="StyleSheet" media="screen" type="text/css" href="<%=request.getContextPath()%><%=css%>"/>
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/>
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/extension/melanbide81/melanbide81.css"/>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/melanbide81/LpeelUtils.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/popup.js"></script>
         <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/melanbide81/LpeelTablaNueva.js"></script>
       <script type="text/javascript">
            var APP_CONTEXT_PATH = '<%=request.getContextPath()%>';
            var url = APP_CONTEXT_PATH + "/PeticionModuloIntegracion.do";
            var tpM81;

            function pulsarNuevoProyecto() {
                lanzarPopUpModal(url + '?tarea=preparar&modulo=MELANBIDE81&operacion=cargarNuevoProyecto&tipo=0&nuevo=1&numExp=<%=numExpediente%>', 500, 650, 'no', 'no', function (result) {
                    if (result != undefined) {
                        if (result[0] == '0') {
                            recargarTablaProyectos(result[1]);
                        }
                    }
                });
            }

            function pulsarModificarProyecto() {
                if (tablaProyectos.selectedIndex != -1) {
                    lanzarPopUpModal(url + '?tarea=preparar&modulo=MELANBIDE81&operacion=cargarModificarProyecto&tipo=0&nuevo=0&numExp=<%=numExpediente%>&id=' + listaProyectos[tablaProyectos.selectedIndex][0], 500, 650, 'no', 'no', function (result) {
                        if (result != undefined) {
                            if (result[0] == '0') {
                                recargarTablaProyectos(result[1]);
                            }
                        }
                    });
                } else {
                    jsp_alerta('A', '<%=meLanbide81I18n.getMensaje(idiomaUsuario, "msg.msjNoSelecFila")%>');
                }
            }

            function pulsarEliminarProyecto() {
                if (tablaProyectos.selectedIndex != -1) {
                    var resultado = jsp_alerta('', '<%=meLanbide81I18n.getMensaje(idiomaUsuario, "msg.preguntaEliminarProyecto")%>');
                    if (resultado == 1) {
                        var parametros = 'tarea=preparar&modulo=MELANBIDE81&operacion=eliminarProyecto&tipo=0&numExp=<%=numExpediente%>&id=' + listaProyectos[tablaProyectos.selectedIndex][0] + '&idProyecto=' + listaProyectos[tablaProyectos.selectedIndex][0];
                        try {
                            $.ajax({
                                url: url,
                                type: 'POST',
                                async: true,
                                data: parametros,
                                success: procesarRespuestaEliminarProyecto,
                                error: mostrarErrorEliminarProyecto
                            });
                        } catch (Err) {
                            mostrarErrorPeticion();
                        }
                    }
                } else {
                    jsp_alerta('A', '<%=meLanbide81I18n.getMensaje(idiomaUsuario, "msg.msjNoSelecFila")%>');
                }
            }

            function pulsarListasxProyecto() {
                if (tablaProyectos.selectedIndex != -1) {
                    lanzarPopUpModal(url + '?tarea=preparar&modulo=MELANBIDE81&operacion=cargarListaxProyecto&tipo=0&numExp=<%=numExpediente%>&idProyecto=' + listaProyectos[tablaProyectos.selectedIndex][0]
                            + '&prioridad=' + listaProyectos[tablaProyectos.selectedIndex][1] + '&denomProyecto=' + listaProyectos[tablaProyectos.selectedIndex][2] + '&tipoProyecto=' + listaProyectos[tablaProyectos.selectedIndex][4], 600, 1200, 'no', 'no', function (result) {
                        if (result != undefined) {
                            if (result[0] == '0') {
                                recargarTablaProyectos(result[1]);
                            }
                        }
                    });
                } else {
                    jsp_alerta('A', '<%=meLanbide81I18n.getMensaje(idiomaUsuario, "msg.msjNoSelecFila")%>');
                }
            }

            function crearTablaProyectos() {
                tablaProyectos = new TablaLpeel(true, '<%=descriptor.getDescripcion("buscar")%>', '<%=descriptor.getDescripcion("anterior")%>', '<%=descriptor.getDescripcion("siguiente")%>', '<%=descriptor.getDescripcion("mosFilasPag")%>', '<%=descriptor.getDescripcion("msgNoResultBusq")%>', '<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>', '<%=descriptor.getDescripcion("filtrDeTotal")%>', '<%=descriptor.getDescripcion("primero")%>', '<%=descriptor.getDescripcion("ultimo")%>', document.getElementById('listaProyectos'));

                tablaProyectos.addColumna('0', 'center', "<%=meLanbide81I18n.getMensaje(idiomaUsuario,"tabla.id")%>");
                tablaProyectos.addColumna('40', 'center', "<%=meLanbide81I18n.getMensaje(idiomaUsuario,"proyectos.prioridad")%>");
                tablaProyectos.addColumna('200', 'left', "<%=meLanbide81I18n.getMensaje(idiomaUsuario,"tabla.denominacion")%>");
                tablaProyectos.addColumna('125', 'left', "<%=meLanbide81I18n.getMensaje(idiomaUsuario,"proyectos.entidad")%>");
                tablaProyectos.addColumna('100', 'left', "<%=meLanbide81I18n.getMensaje(idiomaUsuario,"proyectos.tipo")%>");
                tablaProyectos.addColumna('50', 'center', "<%=meLanbide81I18n.getMensaje(idiomaUsuario,"proyectos.fases")%>");

                tablaProyectos.displayCabecera = true;
                tablaProyectos.height = 360;

            }

            function recargarTablaProyectos(proyectos) {
                var proyecto;
                listaProyectos = new Array();
                listaProyectosTabla = new Array();
                for (var i = 0; i < proyectos.length; i++) {
                    proyecto = proyectos[i];
                    listaProyectos[i] = [proyecto.id.toString(), proyecto.prioridad.toString(), proyecto.denominacion, proyecto.entidad, proyecto.tipoProyecto, proyecto.descTipoProyecto, proyecto.fases.toString()];
                    listaProyectosTabla[i] = [proyecto.id.toString(), proyecto.prioridad.toString(), proyecto.denominacion, proyecto.entidad, proyecto.descTipoProyecto, proyecto.fases.toString()];
                }
                crearTablaProyectos();
                tablaProyectos.lineas = listaProyectosTabla;
                tablaProyectos.displayTabla();
            }

            function procesarRespuestaEliminarProyecto(ajaxResult) {
                var datos = JSON.parse(ajaxResult);
                var codigoOperacion = datos.tabla.codigoOperacion;
                if (codigoOperacion == "0") {
                    var proyectos = datos.tabla.lista;
                    recargarTablaProyectos(proyectos);
                } else {
                    mostrarErrorPeticion(codigoOperacion);
                }
            }

            function mostrarErrorEliminarProyecto() {
                elementoVisible('off', 'barraProgresoLPEEL');
                mostrarErrorPeticion(6);
            }
        </script>
    </head>
    <body class="bandaBody">
        <div class="tab-page" id="tabPage81-1" style="height:520px; width: 100%;">  
            <input type="hidden" id="errorBD" name="errorBD" value="<%=meLanbide81I18n.getMensaje(idiomaUsuario,"error.errorBD")%>"/>
            <input type="hidden" id="generico" name="generico" value="<%=meLanbide81I18n.getMensaje(idiomaUsuario,"error.generico")%>"/>
            <input type="hidden" id="pasoParametros" name="pasoParametros" value="<%=meLanbide81I18n.getMensaje(idiomaUsuario,"error.pasoParametros")%>"/>
            <input type="hidden" id="listaVacia" name="listaVacia" value="<%=meLanbide81I18n.getMensaje(idiomaUsuario,"error.listaVacia")%>"/>
            <input type="hidden" id="obtenerDatos" name="obtenerDatos" value="<%=meLanbide81I18n.getMensaje(idiomaUsuario,"error.obtenerDatos")%>"/>
            <input type="hidden" id="borrarDatos" name="borrarDatos" value="<%=meLanbide81I18n.getMensaje(idiomaUsuario,"error.borrarDatos")%>"/>
            <input type="hidden" id="operacionIncorrecta" name="operacionIncorrecta" value="<%=meLanbide81I18n.getMensaje(idiomaUsuario,"error.operacionIncorrecta")%>"/>
            <h2 class="tab" id="pestanaProyectos"><%=meLanbide81I18n.getMensaje(idiomaUsuario,"label.titulo.pestanaPrincipal")%></h2>
            <script type="text/javascript">tpM81 = tp1.addTabPage(document.getElementById("tabPage81-1"));</script>
            <br>
            <h2 class="legendAzul"><%=meLanbide81I18n.getMensaje(idiomaUsuario, "label.titulo.pestanaProyectos")%></h2>
            <div style="clear: both;">
                <div id="listaProyectos"  name="listaProyectos" align="center"></div>
            </div>
            <div class="botonera centrarElementos">
                <input type="button" id="btnNuevoProyecto" name="btnNuevoProyecto" class="botonGeneral"  value="<%=meLanbide81I18n.getMensaje(idiomaUsuario, "btn.nuevo")%>" onclick="pulsarNuevoProyecto();">
                <input type="button" id="btnModificarProyecto" name="btnModificarProyecto" class="botonGeneral" value="<%=meLanbide81I18n.getMensaje(idiomaUsuario, "btn.modificar")%>" onclick="pulsarModificarProyecto();">
                <input type="button" id="btnEliminarProyecto" name="btnEliminarProyecto"   class="botonGeneral" value="<%=meLanbide81I18n.getMensaje(idiomaUsuario, "btn.eliminar")%>" onclick="pulsarEliminarProyecto();">
            </div>   
            <div class="botonera centrarElementos" style="padding: 10px;">
                <input type="button" id="btnListasxProyecto" name="btnListasxProyecto" class="botonEnorme" value="<%=meLanbide81I18n.getMensaje(idiomaUsuario, "btn.listasxProyecto")%>" onclick="pulsarListasxProyecto();">
            </div>
        </div>
        <script  type="text/javascript">
            var tablaProyectos;
            var listaProyectos = new Array();
            var listaProyectosTabla = new Array();
            <%
                ProyectoVO proyectoVO = null;
                List<ProyectoVO> List = null;
                if(request.getAttribute("listaProyectos")!=null){
                    List = (List<ProyectoVO>)request.getAttribute("listaProyectos");
                }
        
                if (List!= null && List.size() >0){
                    for (int indice=0;indice<List.size();indice++) {
                        proyectoVO = List.get(indice);
                
                        String prioridad = "-";
                        if (proyectoVO.getPrioridad() != null) {
                            prioridad = String.valueOf(proyectoVO.getPrioridad());
                        }
                        String denominacion = "-";
                        if (proyectoVO.getDenominacion() != null) {
                            denominacion = proyectoVO.getDenominacion();
                        }
                        String entidad = "-";
                        if (proyectoVO.getEntidad() != null) {
                            entidad = proyectoVO.getEntidad();
                        }
                        String tipoProyecto = "-";
                        if (proyectoVO.getTipoProyecto() != null) {
                            tipoProyecto = proyectoVO.getTipoProyecto();
                        }
                        String descTipoProyecto = "-";
                        if (proyectoVO.getDescTipoProyecto() != null) {
                            descTipoProyecto = proyectoVO.getDescTipoProyecto();
                        }
                        String fases = "-";
                        if (proyectoVO.getFases() != null) {
                            fases = String.valueOf(proyectoVO.getFases());
                        }
                
            %>
            listaProyectos[<%=indice%>] = ['<%=proyectoVO.getId()%>', '<%=prioridad%>', '<%=denominacion%>', '<%=entidad%>', '<%=tipoProyecto%>', '<%=descTipoProyecto%>', '<%=fases%>'];
            listaProyectosTabla[<%=indice%>] = ['<%=proyectoVO.getId()%>', '<%=prioridad%>', '<%=denominacion%>', '<%=entidad%>', '<%=descTipoProyecto%>', '<%=fases%>'];
            <%
                    }
               }
            %>
            crearTablaProyectos();
            tablaProyectos.lineas = listaProyectosTabla;
            tablaProyectos.displayTabla();
        </script>
    </body>
</html>
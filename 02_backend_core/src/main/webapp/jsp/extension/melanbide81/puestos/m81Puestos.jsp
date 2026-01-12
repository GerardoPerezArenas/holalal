<%-- 
    Document   : m81Puestos
    Created on : 05-abr-2022, 14:32:16
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
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide81.vo.PuestoVO"%>
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
          //      String idProyecto = (String)request.getAttribute("idProyecto");               
        //        String prioridad = (String)request.getAttribute("prioridad");               
        %>
        <jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
        <jsp:setProperty name="descriptor"  property="idi_cod" value="<%=idiomaUsuario%>" />
        <jsp:setProperty name="descriptor"  property="apl_cod" value="<%=apl%>" />
        <link rel="StyleSheet" media="screen" type="text/css" href="<%=request.getContextPath()%><%=css%>"/>
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/>
        <script type="text/javascript">
            function pulsarNuevoPuesto() {
                lanzarPopUpModal(url + '?tarea=preparar&modulo=MELANBIDE81&operacion=cargarNuevoPuesto&tipo=0&nuevo=1&numExp=<%=numExpediente%>&idProyecto=' + document.getElementById('idProyecto').value + '&prioridad=' + document.getElementById('prioridad').value, 500, 650, 'no', 'no', function (result) {
                    if (result != undefined) {
                        if (result[0] == '0') {
                            recargarTablaPuestos(result[1]);
                        }
                    }
                });
            }

            function pulsarModificarPuesto() {
                if (tablaPuestos.selectedIndex != -1) {
                    lanzarPopUpModal(url + '?tarea=preparar&modulo=MELANBIDE81&operacion=cargarModificarPuesto&tipo=0&nuevo=0&numExp=<%=numExpediente%>&id=' + listaPuestos[tablaPuestos.selectedIndex][0], 500, 650, 'no', 'no', function (result) {
                        if (result != undefined) {
                            if (result[0] == '0') {
                                recargarTablaPuestos(result[1]);
                            }
                        }
                    });
                } else {
                    jsp_alerta('A', '<%=meLanbide81I18n.getMensaje(idiomaUsuario, "msg.msjNoSelecFila")%>');
                }
            }

            function pulsarExportarExcelFomento() {
                var CONTEXT_PATH = '<%=request.getContextPath()%>'
                var url = CONTEXT_PATH + "/PeticionModuloIntegracion.do";
                var parametros = '';
                parametros = '?tarea=preparar&modulo=MELANBIDE81&operacion=generarExcelFomentoEmpleo&idProyecto=' + document.getElementById('idProyecto').value + '&tipo=0&numExp=<%=numExpediente%>';
                window.open(url + parametros, "_blank");
            }

            function pulsarEliminarPuesto() {
                if (tablaPuestos.selectedIndex != -1) {
                    var resultado = jsp_alerta('', '<%=meLanbide81I18n.getMensaje(idiomaUsuario, "msg.preguntaEliminar")%>');
                    if (resultado == 1) {
                        elementoVisible('on', 'barraProgresoLPEEL');
                        var parametros = 'tarea=preparar&modulo=MELANBIDE81&operacion=eliminarPuesto&tipo=0&numExp=<%=numExpediente%>&idProyecto=' + document.getElementById('idProyecto').value + '&id=' + listaPuestos[tablaPuestos.selectedIndex][0];
                        try {
                            $.ajax({
                                url: url,
                                type: 'POST',
                                async: true,
                                data: parametros,
                                success: procesarRespuestaEliminarPuesto,
                                error: mostrarErrorEliminarPuesto
                            });
                        } catch (Err) {
                            elementoVisible('off', 'barraProgresoLPEEL');
                            mostrarErrorPeticion();
                        }
                    }
                } else {
                    jsp_alerta('A', '<%=meLanbide81I18n.getMensaje(idiomaUsuario, "msg.msjNoSelecFila")%>');
                }
            }

            function crearTablaPuestos() {
                tablaPuestos = new TablaLpeel(true, '<%=descriptor.getDescripcion("buscar")%>', '<%=descriptor.getDescripcion("anterior")%>', '<%=descriptor.getDescripcion("siguiente")%>', '<%=descriptor.getDescripcion("mosFilasPag")%>', '<%=descriptor.getDescripcion("msgNoResultBusq")%>', '<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>', '<%=descriptor.getDescripcion("filtrDeTotal")%>', '<%=descriptor.getDescripcion("primero")%>', '<%=descriptor.getDescripcion("ultimo")%>', document.getElementById('listaPuestos'));

                tablaPuestos.addColumna('0', 'center', "<%=meLanbide81I18n.getMensaje(idiomaUsuario,"tabla.id")%>");
                tablaPuestos.addColumna('0', 'center', "<%=meLanbide81I18n.getMensaje(idiomaUsuario,"tabla.id")%>");//idProyecto
                tablaPuestos.addColumna('50', 'center', "<%=meLanbide81I18n.getMensaje(idiomaUsuario,"proyectos.prioridad")%>");
                tablaPuestos.addColumna('200', 'left', "<%=meLanbide81I18n.getMensaje(idiomaUsuario,"puestos.denominacion")%>", 'String');
                tablaPuestos.addColumna('50', 'center', "<%=meLanbide81I18n.getMensaje(idiomaUsuario,"tabla.duracion")%>");
                tablaPuestos.addColumna('50', 'center', "<%=meLanbide81I18n.getMensaje(idiomaUsuario,"tabla.porcJorn")%>");
                tablaPuestos.addColumna('50', 'center', "<%=meLanbide81I18n.getMensaje(idiomaUsuario,"puestos.numContratos")%>");
                tablaPuestos.addColumna('50', 'right', "<%=meLanbide81I18n.getMensaje(idiomaUsuario,"puestos.costeEstimado")%>");
                tablaPuestos.addColumna('50', 'right', "<%=meLanbide81I18n.getMensaje(idiomaUsuario,"tabla.subvSoli")%>");

                tablaPuestos.displayCabecera = true;
                tablaPuestos.height = 360;
                tablaPuestos.lineas = listaPuestosTabla;
                tablaPuestos.displayTabla();
            }

            function recargarTablaPuestos(puestos) {
                listaPuestos = new Array();
                listaPuestosTabla = new Array();
                for (var i = 0; i < puestos.length; i++) {
                    var puesto = puestos[i];
                    listaPuestos[i] = [puesto.id.toString(), puesto.idProyecto.toString(), puesto.idPrioridadProyecto.toString(), puesto.denominacion, puesto.duracion.toString().replace(".", ","), puesto.porcJorn.toString().replace(".", ","), puesto.numContratos.toString(), puesto.coste.toString().replace(".", ","), puesto.subvencion.toString().replace(".", ",")];
                    listaPuestosTabla[i] = [puesto.id.toString(), puesto.idProyecto.toString(), puesto.idPrioridadProyecto.toString(), puesto.denominacion, puesto.duracion.toString().replace(".", ","), puesto.porcJorn.toString().replace(".", ","), puesto.numContratos.toString(), puesto.coste.toString().replace(".", ","), puesto.subvencion.toString().replace(".", ",")];
                }
                crearTablaPuestos();
            }

            function procesarRespuestaEliminarPuesto(ajaxResult) {
                elementoVisible('off', 'barraProgresoLPEEL');
                var datos = JSON.parse(ajaxResult);
                var codigoOperacion = datos.tabla.codigoOperacion;
                if (codigoOperacion == "0") {
                    var puestos = datos.tabla.lista;
                    recargarTablaPuestos(puestos);
                } else {
                    mostrarErrorPeticion(codigoOperacion);
                }
            }

            function mostrarErrorEliminarPuesto() {
                elementoVisible('off', 'barraProgresoLPEEL');
                mostrarErrorPeticion(6);
            }
        </script>
    </head>
    <body class="bandaBody">
        <div class="tab-page" id="tabPageM81-1" style="width: 100%;">
            <h2 class="legendAzul"><%=meLanbide81I18n.getMensaje(idiomaUsuario, "label.titulo.puestos")%></h2>
            <div style="clear: both;">
                <div id="listaPuestos"  name="listaPuestos" align="center"style="margin: 5px;"></div>
            </div>
            <div class="botonera centrarElementos">
                <input type="button" id="btnNuevoPuesto" name="btnNuevoPuesto" class="botonGeneral"  value="<%=meLanbide81I18n.getMensaje(idiomaUsuario, "btn.nuevo")%>" onclick="pulsarNuevoPuesto();">
                <input type="button" id="btnModificarPuesto" name="btnModificarPuesto" class="botonGeneral" value="<%=meLanbide81I18n.getMensaje(idiomaUsuario, "btn.modificar")%>" onclick="pulsarModificarPuesto();">
                <input type="button" id="btnEliminarPuesto" name="btnEliminarPuesto"   class="botonGeneral" value="<%=meLanbide81I18n.getMensaje(idiomaUsuario, "btn.eliminar")%>" onclick="pulsarEliminarPuesto();">
                <input type="button" id="btnExportarExcel" name="btnExportarExcel"   class="botonGeneral" value="<%=meLanbide81I18n.getMensaje(idiomaUsuario, "btn.exportar")%>" onclick="pulsarExportarExcelFomento();">

            </div>
            <br>
        </div> 
        <script  type="text/javascript">
            var tablaPuestos;
            var listaPuestos = new Array();
            var listaPuestosTabla = new Array();
            <%
            PuestoVO puestoVO = null;
            List<PuestoVO> List = null;
            if(request.getAttribute("listaPuestos")!=null){
                List = (List<PuestoVO>)request.getAttribute("listaPuestos");
            }

            if (List!= null && List.size() >0){
                for (int indice=0;indice<List.size();indice++) {
                    puestoVO = List.get(indice);

                    String idProyecto = "-";
                    if (puestoVO.getIdProyecto() != null) {
                        idProyecto = String.valueOf(puestoVO.getIdProyecto());
                    }
                    String prioridadProyecto = "-";
                    if (puestoVO.getIdPrioridadProyecto() != null) {
                        prioridadProyecto = String.valueOf(puestoVO.getIdPrioridadProyecto());
                    }
                    String denominacion = "-";
                    if (puestoVO.getDenominacion() != null) {
                        denominacion = puestoVO.getDenominacion();
                    }
                    String duracion  = "";
                    if(puestoVO.getDuracion()!=null){
                       duracion = String.valueOf(puestoVO.getDuracion().toString().replace(".",","));
                    }
                    String porcJorn  = "";
                    if(puestoVO.getPorcJorn()!=null){
                       porcJorn = String.valueOf(puestoVO.getPorcJorn().toString().replace(".",","));
                    }
                    String numContratos = "-";
                    if (puestoVO.getNumContratos() != null) {
                        numContratos = String.valueOf(puestoVO.getNumContratos());
                    }
                    String coste  = "";
                    if(puestoVO.getCoste()!=null){
                       coste = String.valueOf(puestoVO.getCoste().toString().replace(".",","));
                    }
                    String subvencion  = "";
                    if(puestoVO.getSubvencion()!=null){
                       subvencion = String.valueOf(puestoVO.getSubvencion().toString().replace(".",","));
                    }
            %>
            listaPuestos[<%=indice%>] = ['<%=puestoVO.getId()%>', '<%=idProyecto%>', '<%=prioridadProyecto%>', '<%=denominacion%>', '<%=duracion%>', '<%=porcJorn%>', '<%=numContratos%>', '<%=coste%>', '<%=subvencion%>'];
            listaPuestosTabla[<%=indice%>] = ['<%=puestoVO.getId()%>', '<%=idProyecto%>', '<%=prioridadProyecto%>', '<%=denominacion%>', '<%=duracion%>', '<%=porcJorn%>', '<%=numContratos%>', '<%=coste%>', '<%=subvencion%>'];
            <%
                    }
               }
            %>
            crearTablaPuestos();
        </script>
    </body>
</html>
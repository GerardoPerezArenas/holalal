<%-- 
    Document   : m81Contrataciones
    Created on : 05-abr-2022, 14:31:30
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
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide81.vo.ContratacionVO"%>
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
        <script type="text/javascript">
            function pulsarNuevaContratacion() {
                lanzarPopUpModal(url + '?tarea=preparar&modulo=MELANBIDE81&operacion=cargarNuevaContratacion&tipo=0&nuevo=1&numExp=<%=numExpediente%>&idProyecto=' + document.getElementById('idProyecto').value + '&prioridad=' + document.getElementById('prioridad').value, 500, 650, 'no', 'no', function (result) {
                    if (result != undefined) {
                        if (result[0] == '0') {
                            recargarTablaContrataciones(result[1]);
                        }
                    }
                });
            }

            function pulsarModificarContratacion() {
                if (tablaContrataciones.selectedIndex != -1) {
                    lanzarPopUpModal(url + '?tarea=preparar&modulo=MELANBIDE81&operacion=cargarModificarContratacion&tipo=0&nuevo=0&numExp=<%=numExpediente%>&id=' + listaContrataciones[tablaContrataciones.selectedIndex][0], 500, 650, 'no', 'no', function (result) {
                        if (result != undefined) {
                            if (result[0] == '0') {
                                recargarTablaContrataciones(result[1]);
                            }
                        }
                    });
                } else {
                    jsp_alerta('A', '<%=meLanbide81I18n.getMensaje(idiomaUsuario, "msg.msjNoSelecFila")%>');
                }
            }


            function pulsarExportarExcelAyudas() {
                var CONTEXT_PATH = '<%=request.getContextPath()%>'
                var url = CONTEXT_PATH + "/PeticionModuloIntegracion.do";
                var parametros = '';
                parametros = '?tarea=preparar&modulo=MELANBIDE81&operacion=generarExcelAyudasContratacion&idProyecto=' + document.getElementById('idProyecto').value + '&tipo=0&numExp=<%=numExpediente%>';
                window.open(url + parametros, "_blank");
            }

            function pulsarEliminarContratacion() {
                if (tablaContrataciones.selectedIndex != -1) {
                    var resultado = jsp_alerta('', '<%=meLanbide81I18n.getMensaje(idiomaUsuario, "msg.preguntaEliminar")%>');
                    if (resultado == 1) {
                        elementoVisible('on', 'barraProgresoLPEEL');
                        var parametros = 'tarea=preparar&modulo=MELANBIDE81&operacion=eliminarContratacion&tipo=0&numExp=<%=numExpediente%>&idProyecto=' + document.getElementById('idProyecto').value + '&id=' + listaContrataciones[tablaContrataciones.selectedIndex][0];
                        try {
                            $.ajax({
                                url: url,
                                type: 'POST',
                                async: true,
                                data: parametros,
                                success: procesarRespuestaEliminarContratacion,
                                error: mostrarErrorEliminarContratacion
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

            function crearTablaContrataciones() {
                tablaContrataciones = new TablaLpeel(true, '<%=descriptor.getDescripcion("buscar")%>', '<%=descriptor.getDescripcion("anterior")%>', '<%=descriptor.getDescripcion("siguiente")%>', '<%=descriptor.getDescripcion("mosFilasPag")%>', '<%=descriptor.getDescripcion("msgNoResultBusq")%>', '<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>', '<%=descriptor.getDescripcion("filtrDeTotal")%>', '<%=descriptor.getDescripcion("primero")%>', '<%=descriptor.getDescripcion("ultimo")%>', document.getElementById('listaContrataciones'));

                tablaContrataciones.addColumna('0', 'center', "<%=meLanbide81I18n.getMensaje(idiomaUsuario,"tabla.id")%>");
                tablaContrataciones.addColumna('0', 'center', "<%=meLanbide81I18n.getMensaje(idiomaUsuario,"tabla.id")%>");//idProyecto
                tablaContrataciones.addColumna('50', 'center', "<%=meLanbide81I18n.getMensaje(idiomaUsuario,"proyectos.prioridad")%>");
                tablaContrataciones.addColumna('200', 'center', "<%=meLanbide81I18n.getMensaje(idiomaUsuario,"contrataciones.tipoPers")%>");
                tablaContrataciones.addColumna('50', 'center', "<%=meLanbide81I18n.getMensaje(idiomaUsuario,"contrataciones.sexo")%>");
                tablaContrataciones.addColumna('50', 'center', "<%=meLanbide81I18n.getMensaje(idiomaUsuario,"tabla.duracion")%>");
                tablaContrataciones.addColumna('50', 'center', "<%=meLanbide81I18n.getMensaje(idiomaUsuario,"tabla.porcJorn")%>");
                tablaContrataciones.addColumna('50', 'center', "<%=meLanbide81I18n.getMensaje(idiomaUsuario,"contrataciones.numContratosPre")%>");
                tablaContrataciones.addColumna('50', 'right', "<%=meLanbide81I18n.getMensaje(idiomaUsuario,"tabla.subvSoli")%>");

                tablaContrataciones.displayCabecera = true;
                tablaContrataciones.height = 360;
            }

            function recargarTablaContrataciones(contrataciones) {
                var contratacion;
                listaContrataciones = new Array();
                listaContratacionesTabla = new Array();
                for (var i = 0; i < contrataciones.length; i++) {
                    contratacion = contrataciones[i];
                    listaContrataciones[i] = [contratacion.id.toString(), contratacion.idProyecto.toString(), contratacion.idPrioridadProyecto.toString(), contratacion.tipoDesempleado, contratacion.descTipoDesempleado, contratacion.sexo, contratacion.descSexo, contratacion.duracion.toString().replace(".", ","), contratacion.porcJorn.toString().replace(".", ","), contratacion.numContratos.toString(), contratacion.subvencion.toString().replace(".", ",")];
                    listaContratacionesTabla[i] = [contratacion.id.toString(), contratacion.idProyecto.toString(), contratacion.idPrioridadProyecto.toString(), contratacion.descTipoDesempleado, contratacion.descSexo, contratacion.duracion.toString().replace(".", ","), contratacion.porcJorn.toString().replace(".", ","), contratacion.numContratos.toString(), contratacion.subvencion.toString().replace(".", ",")];
                }
                crearTablaContrataciones();
                tablaContrataciones.lineas = listaContratacionesTabla;
                tablaContrataciones.displayTabla();
            }

            function procesarRespuestaEliminarContratacion(ajaxResult) {
                elementoVisible('off', 'barraProgresoLPEEL');
                var datos = JSON.parse(ajaxResult);
                var codigoOperacion = datos.tabla.codigoOperacion;
                if (codigoOperacion == "0") {
                    var contrataciones = datos.tabla.lista;
                    recargarTablaContrataciones(contrataciones);
                } else {
                    mostrarErrorPeticion(codigoOperacion);
                }
            }

            function mostrarErrorEliminarContratacion() {
                elementoVisible('off', 'barraProgresoLPEEL');
                mostrarErrorPeticion(6);
            }
        </script>
    </head>
    <body class="bandaBody">
        <div class="tab-page" id="tabPageM81-2" style="width: 100%;">
            <h2 class="legendAzul"><%=meLanbide81I18n.getMensaje(idiomaUsuario, "label.titulo.contrataciones")%></h2>
            <div style="clear: both;">
                <div id="listaContrataciones"  name="listaContrataciones" align="center" style="margin: 5px;"></div>
            </div>
            <div class="botonera centrarElementos">
                <input type="button" id="btnNuevoContratacion" name="btnNuevoContratacion" class="botonGeneral"  value="<%=meLanbide81I18n.getMensaje(idiomaUsuario, "btn.nuevo")%>" onclick="pulsarNuevaContratacion();">
                <input type="button" id="btnModificarContratacion" name="btnModificarContratacion" class="botonGeneral" value="<%=meLanbide81I18n.getMensaje(idiomaUsuario, "btn.modificar")%>" onclick="pulsarModificarContratacion();">
                <input type="button" id="btnEliminarContratacion" name="btnEliminarContratacion"   class="botonGeneral" value="<%=meLanbide81I18n.getMensaje(idiomaUsuario, "btn.eliminar")%>" onclick="pulsarEliminarContratacion();">
                <input type="button" id="btnExportarExcel" name="btnExportarExcel"   class="botonGeneral" value="<%=meLanbide81I18n.getMensaje(idiomaUsuario, "btn.exportar")%>" onclick="pulsarExportarExcelAyudas();">
            </div>   
            <br>
        </div>
        <script  type="text/javascript">
            var tablaContrataciones;
            var listaContrataciones = new Array();
            var listaContratacionesTabla = new Array();
            <%
            ContratacionVO contratacionVO = null;
            List<ContratacionVO> List = null;
            if(request.getAttribute("listaContrataciones")!=null){
                List = (List<ContratacionVO>)request.getAttribute("listaContrataciones");
            }

            if (List!= null && List.size() >0){
                for (int indice=0;indice<List.size();indice++) {
                    contratacionVO = List.get(indice);

                    String idProyecto = "-";
                    if (contratacionVO.getIdProyecto() != null) {
                        idProyecto = String.valueOf(contratacionVO.getIdProyecto());
                    }
                    String prioridadProyecto = "-";
                    if (contratacionVO.getIdPrioridadProyecto() != null) {
                        prioridadProyecto = String.valueOf(contratacionVO.getIdPrioridadProyecto());
                    }
                    String tipoDesempleado = "-";
                    if (contratacionVO.getTipoDesempleado() != null) {
                        tipoDesempleado = contratacionVO.getTipoDesempleado();
                    }
                    String descTipoDesempleado = "-";
                    if (contratacionVO.getDescTipoDesempleado() != null) {
                        descTipoDesempleado = contratacionVO.getDescTipoDesempleado();
                    }
                    String sexo = "-";
                    if (contratacionVO.getSexo() != null) {
                        sexo = contratacionVO.getSexo();
                    }
                    String descSexo = "-";
                    if (contratacionVO.getDescSexo() != null) {
                        descSexo = contratacionVO.getDescSexo();
                    }
                    String duracion  = "";
                    if(contratacionVO.getDuracion()!=null){
                       duracion = String.valueOf(contratacionVO.getDuracion().toString().replace(".",","));
                    }
                    String porcJorn  = "";
                    if(contratacionVO.getPorcJorn()!=null){
                       porcJorn = String.valueOf(contratacionVO.getPorcJorn().toString().replace(".",","));
                    }
                    String numContratos = "-";
                    if (contratacionVO.getNumContratos() != null) {
                        numContratos = String.valueOf(contratacionVO.getNumContratos());
                    }
                    String subvencion  = "";
                    if(contratacionVO.getSubvencion()!=null){
                       subvencion = String.valueOf(contratacionVO.getSubvencion().toString().replace(".",","));
                    }
            %>
            listaContrataciones[<%=indice%>] = ['<%=contratacionVO.getId()%>', '<%=idProyecto%>', '<%=prioridadProyecto%>', '<%=tipoDesempleado%>', '<%=descTipoDesempleado%>', '<%=sexo%>', '<%=descSexo%>', '<%=duracion%>', '<%=porcJorn%>', '<%=numContratos%>', '<%=subvencion%>'];
            listaContratacionesTabla[<%=indice%>] = ['<%=contcontratacionVOratacionVO.getId()%>', '<%=idProyecto%>', '<%=prioridadProyecto%>', '<%=descTipoDesempleado%>', '<%=descSexo%>', '<%=duracion%>', '<%=porcJorn%>', '<%=numContratos%>', '<%=subvencion%>'];
            <%
                    }
               }
            %>
            crearTablaContrataciones();
            tablaContrataciones.lineas = listaContratacionesTabla;
            tablaContrataciones.displayTabla();
        </script>
    </body>
</html>
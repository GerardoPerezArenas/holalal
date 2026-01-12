<%--
    Document   : jornadaicaSeguimiento23
    Created on : 02-ene-2025, 13:55:46
    Author     : kigonzalez
--%>
<%@taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@taglib uri="/WEB-INF/tlds/c.tld" prefix="c"%>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide35.i18n.MeLanbide35I18n"%>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide35.vo.justificacion.JusPreparadoresECA23VO"%>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide35.util.ConfigurationParameter"%>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide35.util.ConstantesMeLanbide35"%>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject"%>
<%@page import="es.altia.common.service.config.Config"%>
<%@page import="es.altia.common.service.config.ConfigServiceHelper"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="java.text.DecimalFormat"%>
<%@page import="java.text.DecimalFormatSymbols"%>
<%@page import="com.google.gson.Gson" %>
<%@page import="com.google.gson.GsonBuilder" %>
<%
    int idiomaUsuario = 1;
    int apl = 5;
    String css = "";
    UsuarioValueObject usuario = new UsuarioValueObject();

    if (session.getAttribute("usuario") != null) {
        usuario = (UsuarioValueObject) session.getAttribute("usuario");
        apl = usuario.getAppCod();
        idiomaUsuario = usuario.getIdioma();
        css = usuario.getCss();
    }

//Clase para internacionalizar los mensajes de la aplicación.
    MeLanbide35I18n meLanbide35I18n = MeLanbide35I18n.getInstance();
    String numExpediente = request.getParameter("numero");
    String totalPreparadores = (String)request.getAttribute("totalPreparadoresVal");

    DecimalFormatSymbols simbolo=new DecimalFormatSymbols();
    simbolo.setDecimalSeparator(',');
    simbolo.setGroupingSeparator('.');
    DecimalFormat formateador = new DecimalFormat("###,##0.##",simbolo);
%>
<jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
<jsp:setProperty name="descriptor"  property="idi_cod" value="<%=idiomaUsuario%>"/>
<jsp:setProperty name="descriptor"  property="apl_cod" value="<%=apl%>"/>
<script type="text/javascript">
    var APP_CONTEXT_PATH = '<%=request.getContextPath()%>';
    var url = APP_CONTEXT_PATH + '/PeticionModuloIntegracion.do';
    var mensajeValidacion = '';
    var totalPreparadores = <%=totalPreparadores%>;

    function pulsarNuevoPreparadorValidacionEca() {
        lanzarPopUpModal(url + '?tarea=preparar&modulo=MELANBIDE35&operacion=cargarMantenimientoInsertValPreparador23&tipo=0&nuevo=1&numExp=<%=numExpediente%>', 370, 815, 'no', 'no', function (result) {
            if (result != undefined) {
                if (result[0] == '0') {
                    recargarTablaValPreparadores(result[1]);
                }
            }
        });
    }

    function pulsarModificarPreparadorValidacionEca() {
        if (tablaValPreparadores.selectedIndex != -1) {
            lanzarPopUpModal(url + '?tarea=preparar&modulo=MELANBIDE35&operacion=cargarMantenimientoUpdateValPreparador23&tipo=0&nuevo=0&numExp=<%=numExpediente%>&id=' +
                listaValPreparadores[tablaValPreparadores.selectedIndex][0], 370, 815, 'no', 'no', function (result) {
                if (result != undefined) {
                    if (result[0] == '0') {
                        recargarTablaValPreparadores(result[1]);
                    }
                }
            });
        } else {
            jsp_alerta('A', '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "msg.msjNoSelecFila")%>');
        }
    }

    function pulsarEliminarPreparadorValidacionEca() {
            console.log("pulsarEliminarPreparadorValidacionEca tablaValPreparadores.selectedIndex = " +
                    tablaValPreparadores.selectedIndex);
            if (tablaValPreparadores.selectedIndex != -1) {
                var resultado = jsp_alerta('', '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "msg.preguntaEliminar")%>');
                if (resultado == 1) {
                    var parametros = 'tarea=preparar&modulo=MELANBIDE35&operacion=eliminarValPreparador23&tipo=0&numExp=<%=numExpediente%>&id=' + listaValPreparadores[tablaValPreparadores.selectedIndex][0];

                    console.log("pulsarEliminarPreparadorValidacionEca: " + parametros);
                    try {
                        $.ajax({
                            url: url,
                            type: 'POST',
                            async: true,
                            data: parametros,
                            success: procesarRespuestaPrepAltaModificacion,
                            error: mostrarErrorEliminarPreparador
                        });
                    } catch (Err) {
                        mostrarErrorEliminarPreparador();
                    }
                }
            } else {
                jsp_alerta('A', '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "msg.msjNoSelecFila")%>');
            }
    }

            // FUNCIONES QUE PROCESAN RESPUESTA O ERROR DE LA PETICION AJAX
            function procesarRespuestaPrepAltaModificacion(ajaxResult) {
                //console.log("procesarRespuestaPrepAltaModificacion ajaxResult=" + ajaxResult.toString());

                var datos = JSON.parse(ajaxResult);
                var codigoOperacion = datos.tabla.codigoOperacion;
                if (codigoOperacion == "0") {
                    var preparadores = datos.tabla.lista;
                    var respuesta = new Array();
                    if (preparadores.length > 0) {
                        respuesta[0] = "0";
                    }
                    respuesta[1] = preparadores;
                    recargarTablaValPreparadores(respuesta[1]);
                } else {
                    mostrarErrorGenericoPrep(codigoOperacion);
                }
            }

            function mostrarErrorEliminarPreparador() {
                mostrarErrorGenericoPrep(6);
            }

            function mostrarErrorGenericoPrep(codigo) {
                //elementoVisible('off', 'barraProgresoECA');
                console.log("mostrarErrorGenericoPrep codigo=" + codigo);
                if (codigo == "1") {
                    jsp_alerta("A", '<%=meLanbide35I18n.getMensaje(idiomaUsuario,"error.validacion.errorBD")%>');
                } else if (codigo == "2") {
                    jsp_alerta("A", '<%=meLanbide35I18n.getMensaje(idiomaUsuario,"error.validacion.errorGen")%>');
                } else if (codigo == "3") {
                    jsp_alerta("A", '<%=meLanbide35I18n.getMensaje(idiomaUsuario,"error.validacion.pasoParametros")%>');
                } else if (codigo == "4") {
                    jsp_alerta("A", '<%=meLanbide35I18n.getMensaje(idiomaUsuario,"error.validacion.listaVacia")%>');
                } else if (codigo == "5") {
                    jsp_alerta("A", '<%=meLanbide35I18n.getMensaje(idiomaUsuario,"error.validacion.errorObtenerDatos")%>');
                } else if (codigo == "6") {
                    jsp_alerta("A", '<%=meLanbide35I18n.getMensaje(idiomaUsuario,"error.validacion.errorBorrarDatos")%>');
                } else if (codigo == "7") {
                    jsp_alerta("A", '<%=meLanbide35I18n.getMensaje(idiomaUsuario,"error.validacion.errorInsertarDatos")%>');
                } else if (codigo == "8") {
                    jsp_alerta("A", '<%=meLanbide35I18n.getMensaje(idiomaUsuario,"error.validacion.errorActualizarDatos")%>');
                } else if (codigo == "-1") {
                    jsp_alerta("A", '<%=meLanbide35I18n.getMensaje(idiomaUsuario,"error.validacion.operacionIncorrecta")%>');
                } else {
                    jsp_alerta("A", '<%=meLanbide35I18n.getMensaje(idiomaUsuario,"error.validacion.errorGen")%>');
                }
            }

    function cargarSeguimientosPreparador() {
        if (tablaValPreparadores.selectedIndex != -1) {
            lanzarPopUpModal(url + '?tarea=preparar&modulo=MELANBIDE35&operacion=cargarSeguimientosPreparador_ValidacionECA23&tipo=0&nuevo=1&numExp=<%=numExpediente%>&dniPreparador=' + listaValPreparadores[tablaValPreparadores.selectedIndex][1], 600, 1200, 'no', 'no', function (result) {
                if (result != undefined) {
                    if (result[0] == '0') {
                        recargarTablaValPreparadores(result[1]);
                    }
                }
            });
        } else {
            jsp_alerta('A', '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "msg.msjNoSelecFila")%>');
        }
    }

    function crearTablaValPreparadores() {
		console.log("crearTablaValPreparadores: Inicio");
        tablaValPreparadores = new TablaEca(true, '<%=descriptor.getDescripcion("buscar")%>', '<%=descriptor.getDescripcion("anterior")%>', '<%=descriptor.getDescripcion("siguiente")%>', '<%=descriptor.getDescripcion("mosFilasPag")%>', '<%=descriptor.getDescripcion("msgNoResultBusq")%>', '<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>', '<%=descriptor.getDescripcion("filtrDeTotal")%>', '<%=descriptor.getDescripcion("primero")%>', '<%=descriptor.getDescripcion("ultimo")%>', document.getElementById('listaValPreparadores'));
        tablaValPreparadores.addColumna('50', 'center', '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.justificacion23.tabla.dniPreparador")%>');
        tablaValPreparadores.addColumna('50', 'center', '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.justificacion23.tabla.jornada")%>');
        tablaValPreparadores.addColumna('50', 'center', '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.justificacion23.tabla.segPermitidos")%>');
        tablaValPreparadores.addColumna('50', 'right', '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.justificacion23.tabla.importeJus")%>');

        tablaValPreparadores.displayCabecera = true;
        //tablaValPreparadores.height = 400;
        tablaValPreparadores.lineas = listaValPreparadoresTabla;
        tablaValPreparadores.displayTablaConTooltips(listaValPreparadoresTitulos);
        tablaValPreparadores.displayTabla();
    }

    function recargarTablaValPreparadores(preparadores) {
        var preparador;
        listaValPreparadores = [];
        listaValPreparadoresTabla = [];
        listaValPreparadoresTitulos = [];
        for (var i = 0; i < preparadores.length; i++) {
            preparador = preparadores[i];
            listaValPreparadores[i] = [preparador.id, preparador.nifPreparador, preparador.jornada, preparador.permitidos, formatearNumero(preparador.importePrep)];
            listaValPreparadoresTabla[i] = [preparador.nifPreparador, preparador.jornada, preparador.permitidos, formatearNumero(preparador.importePrep)];
            listaValPreparadoresTitulos[i] = [preparador.nifPreparador, preparador.jornada, preparador.permitidos, formatearNumero(preparador.importePrep)];
        }
        crearTablaValPreparadores();
    }

    function callFromTableToEca(rowID, tableName) {
    }

    setCombos = false;
</script>
<body>
    <div class="tab-page" id="tabPage3523" style="height:520px; width: 98%;">
        <div  class="sub3titulo" style="width: 98%; clear: both; text-align: center; font-size: 14px;text-transform: uppercase;">
            <span id="subtituloValPreparadores"><%=meLanbide35I18n.getMensaje(idiomaUsuario,"label.justificacion23.preparadores.tituloPagina")%></span>
        </div>
        <fieldset id="preparadoresVal23" name="preparadoresVal23">
            <div>
                <div id="listaValPreparadores" style="padding: 5px; width:98%; text-align: center; margin:0px;margin-top:0px;" align="center"></div>
            </div>


				<div class="lineaFormulario" style="padding-top: 5px; width:38%;margin:0px;margin:auto;" align="center">
					<div style="width: 40%; float: left; text-align: center; text-transform: uppercase" class="etiqueta">
						<input  type="button" id="btnSeguimientosPreparador" name="btnSeguimientosPreparador" class="botonMasLargo" value="<%=meLanbide35I18n.getMensaje(idiomaUsuario, "btn.justificacion23.seguiPreparador")%>" onclick="cargarSeguimientosPreparador();">
					</div>
					<div style="width: 20%; float: left; text-align: center; text-transform: uppercase" class="etiqueta">
						<input type="button" class="botonGeneral" id="btnGuardarValPreparador" value="<%=meLanbide35I18n.getMensaje(idiomaUsuario,"label.validacion.btnInsertar")%>" onClick="pulsarNuevoPreparadorValidacionEca();">
					</div>
					<div style="width: 20%; float: left; text-align: center; text-transform: uppercase" class="etiqueta">
						<input type="button" class="botonGeneral" id="btnModificarValPreparador" value="<%=meLanbide35I18n.getMensaje(idiomaUsuario,"label.validacion.btnModificar")%>" onClick="pulsarModificarPreparadorValidacionEca();">
					</div>
					<div style="width: 20%; float: left; text-align: center; text-transform: uppercase" class="etiqueta">
						<input type="button" class="botonGeneral" id="btnEliminarValPreparador" value="<%=meLanbide35I18n.getMensaje(idiomaUsuario,"label.validacion.btnEliminar")%>" onClick="pulsarEliminarPreparadorValidacionEca();">
					</div>
				</div>

        </fieldset>
    </div>
    <script  type="text/javascript">
        var tablaValPreparadores;
        var listaValPreparadores = new Array();
        var listaValPreparadoresTabla = new Array();
        var listaValPreparadoresTitulos = new Array();
        <%
            JusPreparadoresECA23VO preparador = null;
            List<JusPreparadoresECA23VO> ListaPrep = null;

            if(request.getAttribute("listaPreparadoresValidacion")!=null) {
                ListaPrep = (List<JusPreparadoresECA23VO>)request.getAttribute("listaPreparadoresValidacion");
            }

            if (ListaPrep!= null && ListaPrep.size() > 0) {
                for (int indice = 0;indice < ListaPrep.size();indice++) {
                    preparador = ListaPrep.get(indice);

                    String jornada = "-";
                    if (preparador.getJornada() != null) {
                        jornada = formateador.format(preparador.getJornada());
                    }

                    String permitidos = "-";
                    if (preparador.getPermitidos() != null) {
                        permitidos = Integer.toString(preparador.getPermitidos());
                    }

                    String justificados = "-";
                    if (preparador.getSegumientos() != null) {
                        justificados = Integer.toString(preparador.getSegumientos());
                    }

                    String importePrep = "-";
                    if (preparador.getImportePrep() != null) {
                        importePrep = formateador.format(preparador.getImportePrep());
                    }
        %>
        listaValPreparadores[<%=indice%>] = ['<%=preparador.getId()%>', '<%=preparador.getNifPreparador()%>', '<%=jornada%>', '<%=permitidos%>', '<%=importePrep%>'];
        listaValPreparadoresTabla[<%=indice%>] = ['<%=preparador.getNifPreparador()%>', '<%=jornada%>', '<%=permitidos%>', '<%=importePrep%>' + ' \u20ac'];
        listaValPreparadoresTitulos[<%=indice%>] = ['<%=preparador.getNifPreparador()%>', '<%=jornada%>', '<%=permitidos%>', '<%=importePrep%>' + ' \u20ac'];
        <%
                }
        %>
        <%
            }
        %>
        crearTablaValPreparadores();
        console.log("pestaña Preparadores listaValPreparadores.length() = " + listaValPreparadores.length);
    </script>
</body>
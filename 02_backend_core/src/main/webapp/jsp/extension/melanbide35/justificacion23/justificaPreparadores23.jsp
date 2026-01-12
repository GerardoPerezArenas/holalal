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
    String totalPreparadores = (String)request.getAttribute("totalPreparadoresJus");

    DecimalFormatSymbols simbolo=new DecimalFormatSymbols();
    simbolo.setDecimalSeparator(',');
    simbolo.setGroupingSeparator('.');
    DecimalFormat formateador = new DecimalFormat("###,##0.##",simbolo);
%>
<jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
<jsp:setProperty name="descriptor"  property="idi_cod" value="<%=idiomaUsuario%>"/>
<jsp:setProperty name="descriptor"  property="apl_cod" value="<%=apl%>"/>
<script type="text/javascript">
    var totalPreparadores = <%=totalPreparadores%>;
    function pulsarNuevoPreparadorJustificacionEca() {
        lanzarPopUpModal(url + '?tarea=preparar&modulo=MELANBIDE35&operacion=cargarMantenimientoJusInsercion23&tipo=0&nuevo=1&numExp=<%=numExpediente%>', 600, 1200, 'no', 'no', function (result) {
            if (result != undefined) {
                if (result[0] == '0') {
                    recargarTablaJusPreparadores(result[1]);
                }
            }
        });
    }

    function pulsarModificarPreparadorJustificacionEca() {
        if (tablaJusPreparadores.selectedIndex != -1) {
            lanzarPopUpModal(url + '?tarea=preparar&modulo=MELANBIDE35&operacion=cargarMantenimientoJusInsercion23&tipo=0&nuevo=0&numExp=<%=numExpediente%>&id=' + listaJusPreparadores[tablaJusPreparadores.selectedIndex][0], 600, 1200, 'no', 'no', function (result) {
                if (result != undefined) {
                    if (result[0] == '0') {
                        recargarTablaJusPreparadores(result[1]);
                    }
                }
            });
        } else {
            jsp_alerta('A', '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "msg.msjNoSelecFila")%>');
        }
    }

    function pulsarEliminarPreparadorJustificacionEca() {
        if (tablaJusPreparadores.selectedIndex != -1) {
            var resultado = jsp_alerta('', '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "msg.preguntaEliminar")%>');
            if (resultado == 1) {
                var parametros = '';
                try {
                    $.ajax({
                        url: url,
                        type: 'POST',
                        async: true,
                        data: parametros,
                        beforeSend: function () {
                            pleaseWait('on');
                        },
                        success: procesarRespuestaEliminarPreparador,
                        error: function () {
                            pleaseWait('off');
                            mostrarErrorPeticionJus23("6");
                        }
                    });
                } catch (Err) {
                    mostrarErrorPeticionJus23();
                }
            }
        } else {
            jsp_alerta('A', '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "msg.msjNoSelecFila")%>');
        }
    }

    function cargarSeguimientosPreparador() {
        if (tablaJusPreparadores.selectedIndex != -1) {
            lanzarPopUpModal(url + '?tarea=preparar&modulo=MELANBIDE35&operacion=cargarSeguimientosPreparador_JustificacionECA23&tipo=0&nuevo=1&numExp=<%=numExpediente%>&dniPreparador=' + listaJusPreparadores[tablaJusPreparadores.selectedIndex][1], 600, 1200, 'no', 'no', function (result) {
                if (result != undefined) {
                    if (result[0] == '0') {
                        recargarTablaJusPreparadores(result[1]);
                    }
                }
            });
        } else {
            jsp_alerta('A', '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "msg.msjNoSelecFila")%>');
        }
    }

    function crearTablaJusPreparadores() {
        tablaJusPreparadores = new TablaEca(true, '<%=descriptor.getDescripcion("buscar")%>', '<%=descriptor.getDescripcion("anterior")%>', '<%=descriptor.getDescripcion("siguiente")%>', '<%=descriptor.getDescripcion("mosFilasPag")%>', '<%=descriptor.getDescripcion("msgNoResultBusq")%>', '<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>', '<%=descriptor.getDescripcion("filtrDeTotal")%>', '<%=descriptor.getDescripcion("primero")%>', '<%=descriptor.getDescripcion("ultimo")%>', document.getElementById('listaJusPreparadores'));
        tablaJusPreparadores.addColumna('0', 'center', '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.justificacion23.tabla.ID")%>');
        tablaJusPreparadores.addColumna('50', 'center', '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.justificacion23.tabla.dniPreparador")%>');
        tablaJusPreparadores.addColumna('50', 'center', '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.justificacion23.tabla.jornada")%>');
        tablaJusPreparadores.addColumna('50', 'center', '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.justificacion23.tabla.segPermitidos")%>');
        tablaJusPreparadores.addColumna('50', 'center', '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.justificacion23.tabla.segJustificados")%>');
        tablaJusPreparadores.addColumna('50', 'right', '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.justificacion23.tabla.importeJus")%>');

        tablaJusPreparadores.displayCabecera = true;
        //tablaJusPreparadores.height = 400;
        tablaJusPreparadores.lineas = listaJusPreparadoresTabla;
        tablaJusPreparadores.displayTablaConTooltips(listaJusPreparadoresTitulos);
    }

    function recargarTablaJusPreparadores(preparadores) {
        var preparador;
        listaJusPreparadores = new Array();
        listaJusPreparadoresTabla = new Array();
        listaJusPreparadoresTitulos = new Array();
        for (var i = 0; i < preparadores.length; i++) {
            preparador = preparadores[i];
            listaJusPreparadores[i] = [preparador.id.toString(), preparador.nifPreparador, preparador.jornada, preparador.permitidos.toString(), preparador.segumientos.toString(), formatearNumero(preparador.importePrep)];
            listaJusPreparadoresTabla[i] = [preparador.id.toString(), preparador.nifPreparador, preparador.jornada, preparador.permitidos.toString(), preparador.segumientos.toString(), formatearNumero(preparador.importePrep)];
            listaJusPreparadoresTitulos[i] = [preparador.id.toString(), preparador.nifPreparador, preparador.jornada, preparador.permitidos.toString(), preparador.segumientos.toString(), formatearNumero(preparador.importePrep)];
        }
        crearTablaJusPreparadores();
    }

    function procesarRespuestaEliminarPreparador() {}

    function callFromTableToEca(rowID, tableName) {
    }

</script>
<body>
    <div class="tab-page" id="tabPage3523" style="height:520px; width: 98%;">
        <div  class="sub3titulo" style="width: 98%; clear: both; text-align: center; font-size: 14px;text-transform: uppercase;">
            <span id="subtituloJusPreparadores"><%=meLanbide35I18n.getMensaje(idiomaUsuario,"label.justificacion23.preparadores.tituloPagina")%></span>
        </div>    
        <fieldset id="preparadoresJus23" name="preparadoresJus23">
            <div>
                <div id="listaJusPreparadores" style="padding: 5px; width:98%; text-align: center; margin:0px;margin-top:0px;" align="center"></div>
            </div>
            <div class="botonera" style="text-align: center;">
                <input  type="button" id="btnSeguimientosPreparador" name="btnSeguimientosPreparador" class="botonMasLargo" value="<%=meLanbide35I18n.getMensaje(idiomaUsuario, "btn.justificacion23.seguiPreparador")%>" onclick="cargarSeguimientosPreparador();" disabled="true">         
            </div>
        </fieldset>
    </div>
    <script  type="text/javascript">
        var tablaJusPreparadores;
        var listaJusPreparadores = new Array();
        var listaJusPreparadoresTabla = new Array();
        var listaJusPreparadoresTitulos = new Array();
        <%
            JusPreparadoresECA23VO preparador = null;
            List<JusPreparadoresECA23VO> ListaPrep = null;
            
            if(request.getAttribute("listaPreparadoresJustificacion")!=null) {
                ListaPrep = (List<JusPreparadoresECA23VO>)request.getAttribute("listaPreparadoresJustificacion");
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
        listaJusPreparadores[<%=indice%>] = ['<%=preparador.getId()%>', '<%=preparador.getNifPreparador()%>', '<%=jornada%>', '<%=permitidos%>', '<%=justificados%>', '<%=importePrep%>'];
        listaJusPreparadoresTabla[<%=indice%>] = ['<%=preparador.getId()%>', '<%=preparador.getNifPreparador()%>', '<%=jornada%>', '<%=permitidos%>', '<%=justificados%>', '<%=importePrep%>' + ' \u20ac'];
        listaJusPreparadoresTitulos[<%=indice%>] = ['<%=preparador.getId()%>', '<%=preparador.getNifPreparador()%>', '<%=jornada%>', '<%=permitidos%>', '<%=justificados%>', '<%=importePrep%>' + ' \u20ac'];
        <%
                }
        %>
        document.getElementById('btnSeguimientosPreparador').disabled = false;
        <%
            }
        %>
        crearTablaJusPreparadores();
    </script>
</body>
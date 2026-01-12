
<%@taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@taglib uri="/WEB-INF/tlds/c.tld" prefix="c"%>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide35.i18n.MeLanbide35I18n"%>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide35.util.ConstantesMeLanbide35"%>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide35.util.MeLanbide35Utils"%>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide35.vo.comun.Eca23ConfiguracionVO"%>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide35.vo.justificacion.JustificacionECA23VO"%>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.common.service.config.Config"%>
<%@page import="es.altia.common.service.config.ConfigServiceHelper"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="java.text.DecimalFormat"%>
<%@page import="java.text.DecimalFormatSymbols"%>
<head>
    <meta http-equiv="X-UA-Compatible" content="IE=Edge"/>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
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
        Eca23ConfiguracionVO importesConfiguracion = (Eca23ConfiguracionVO)request.getAttribute("configuracion");

        String urlPestanaValInsercionECA23 = (String)request.getAttribute("urlPestanaInserciones_ValidacionECA23");
        String urlPestanaValPreparadoresECA23 = (String)request.getAttribute("urlPestanaPreparadores_ValidacionECA23");
        JustificacionECA23VO justificacion =(JustificacionECA23VO)request.getAttribute("validacion");
        int numInsercionesJ = (Integer)request.getAttribute("numInsercionesVal");
        int numPreparadoresJ = (Integer)request.getAttribute("numPreparadoresVal");
        int numSeguimientosJ = (Integer)request.getAttribute("numSeguimientosVal");
        DecimalFormatSymbols simbolo=new DecimalFormatSymbols();
        simbolo.setDecimalSeparator(',');
        simbolo.setGroupingSeparator('.');
        DecimalFormat formateador = new DecimalFormat("###,##0.##",simbolo);
    %>
    <jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
    <jsp:setProperty name="descriptor"  property="idi_cod" value="<%=idiomaUsuario%>" />
    <jsp:setProperty name="descriptor"  property="apl_cod" value="<%=apl%>" />
    <link rel="StyleSheet" media="screen" type="text/css" href="<%=request.getContextPath()%><%=css%>">
    <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/melanbide35/ecaUtils.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/melanbide35/TablaNuevaEca.js"></script>
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/>
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/extension/melanbide35/melanbide35.css"/>
    <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/popup.js"></script>
    <script type="text/javascript">
        var url = '<%=request.getContextPath()%>' + '/PeticionModuloIntegracion.do';
        var tp35_val;
        var impEca = '<%=formateador.format(justificacion.getImporteEca())%>';
        var impValidado = '<%=formateador.format(justificacion.getImporteValidado())%>';
        var totInserciones = '<%=formateador.format(justificacion.getTotalInserciones())%>';
        var totSeguimientos = '<%=formateador.format(justificacion.getTotalSeguimientos())%>';
console.log("impEca=" + impEca + ", impValidado=" + impValidado + ", totInserciones=" + totInserciones + ", totSeguimientos=" + totSeguimientos);
        var numInsercionesJ = <%=numInsercionesJ%>;
        var numPreparadoresJ = <%=numPreparadoresJ%>;
        var numSeguimientosJ = <%=numSeguimientosJ%>;
        var maxSeguimientosJ;

        function mostrarErrorPeticionJus23() {
            var msgtitle = '<%=meLanbide35I18n.getMensaje(idiomaUsuario,"error.tituloKO")%>';
            switch (codigo) {
                case "1":
                    jsp_alerta("A", '<%=meLanbide35I18n.getMensaje(idiomaUsuario,"error.errorBD")%>', msgtitle);
                    break;
                case "2":
                    jsp_alerta("A", '<%=meLanbide35I18n.getMensaje(idiomaUsuario,"error.generico")%>', msgtitle);
                    break;
                case "3":
                    jsp_alerta("A", '<%=meLanbide35I18n.getMensaje(idiomaUsuario,"error.pasoParametros")%>', msgtitle);
                    break;
                case "4":
                    jsp_alerta("A", '<%=meLanbide35I18n.getMensaje(idiomaUsuario,"error.listaVacia")%>', msgtitle);
                    break;
                case "5":
                    jsp_alerta("A", '<%=meLanbide35I18n.getMensaje(idiomaUsuario,"error.errorObtenerDatos")%>', msgtitle);
                    break;
                case "6":
                    jsp_alerta("A", '<%=meLanbide35I18n.getMensaje(idiomaUsuario,"error.errorBorrarDatos")%>', msgtitle);
                    break;
                case "7":
                    jsp_alerta("A", '<%=meLanbide35I18n.getMensaje(idiomaUsuario,"error.noTabla")%>', msgtitle);
                    break;
                default:
                    jsp_alerta("A", '<%=meLanbide35I18n.getMensaje(idiomaUsuario,"error.generico")%>', msgtitle);
                    break;
            }
        }

        function copiarDesdeJustificacionAValidacion() {
            pleaseWait('on');

            var variableRes = {
                tarea: 'preparar'
                , modulo: 'MELANBIDE35'
                , operacion: null
                , tipo: 0
                , numero: null
            };

            var dataParameter = $.extend({}, variableRes);
            dataParameter.numero = document.forms[0].numero.value; // N?mero de expediente
            dataParameter.convocatoria = dataParameter.numero.split('/')[0];

            console.log("copiarDesdeJustificacionAValidacion expediente=" + dataParameter.numero + ", convocatoria=" + dataParameter.convocatoria);
            dataParameter.control = new Date().getTime();
            dataParameter.operacion = 'copiarDesdeJustificacionAValidacion';
            dataParameter.idiomaUsuario = <%=idiomaUsuario%>;

            var urlBaseLlamada = APP_CONTEXT_PATH + "/PeticionModuloIntegracion.do";

            $.ajax({
                type: 'POST',
                url: urlBaseLlamada,
                data: dataParameter,
                success: function (response) {
                    console.log("copiarDesdeJustificacionAValidacion response = " + response);
                    let respuesta = JSON.parse(response);

                    console.log("copiarDesdeJustificacionAValidacion respuesta = " + respuesta);
                    pleaseWait('off');
                    if (respuesta !== null) {
                        if (respuesta == "0") {
                            jsp_alerta("A", '<%=meLanbide35I18n.getMensaje(idiomaUsuario,"msg.pestanaValidacion.copyJustToVal")%>');
                            recargarDatosExpediente();
                        } else if (respuesta == "1") {
                            jsp_alerta("A", '<%=meLanbide35I18n.getMensaje(idiomaUsuario,"msg.pestanaValidacion.nothingToCopy")%>');
                        } else if (respuesta == "2") {
                            jsp_alerta("A", '<%=meLanbide35I18n.getMensaje(idiomaUsuario,"msg.pestanaValidacion.errorToCopy")%>');
                        }
                    } else {
                        jsp_alerta("A", '<%=meLanbide35I18n.getMensaje(idiomaUsuario,"msg.pestanaValidacion.errorToCopy")%>');
                    }
                },
                error: function (jqXHR, textStatus, errorThrown) {
                    pleaseWait('off');
                    jsp_alerta("A", '<%=meLanbide35I18n.getMensaje(idiomaUsuario,"msg.pestanaValidacion.errorToCopy")%>');
                },
                async: true
            });
        }

        function recargarDatosExpediente() {
            document.forms[0].opcion.value = "cargarPestTram";
            document.forms[0].target = "mainFrame";
            document.forms[0].action = "<c:url value='/sge/FichaExpediente.do'/>";
            document.forms[0].submit();
        }
    </script>
</head>
<body>
    <div class="tab-page" id="tabPage353" style="height:520px; width: 100%;">
        <h2 class="tab" id="pestana353"><%=meLanbide35I18n.getMensaje(idiomaUsuario,"label.validacion.tituloPestana")%></h2>
        <script type="text/javascript">tp1.addTabPage(document.getElementById("tabPage353"));</script>
        <div style="clear: both;">
            <fieldset id="totalesJus23" name="totalesJus23" style="margin-top: 15px; border-color: black;">
                <legend class="legendTema" style="text-transform: uppercase;text-align: center;" id="titTotalesJus"><%=meLanbide35I18n.getMensaje(idiomaUsuario,"label.resumen.total")%></legend>
                <div class="lineaFormulario" style="padding-top: 15px;">
                    <div style="width: 30%; float: left; padding-left: 5%;">
                        <div style="width: 50%; float: left; text-align: center; text-transform: uppercase" class="etiqueta">
                            <%=meLanbide35I18n.getMensaje(idiomaUsuario,"label.justificacion23.resumen.numInserciones")%>
                        </div>
                        <div style="width: 50%;  float: left;">
                            <input disabled type="text" id="numInsercionesVal" name="numInsercionesVal" size="3" maxlength="3" style="text-align: center;" value="" class="inputTexto total"/>
                        </div>
                    </div>
                    <div style="width: 30%; float: left;">
                        <div style="width: 50%; float: left; text-align: center; text-transform: uppercase" class="etiqueta">
                            <%=meLanbide35I18n.getMensaje(idiomaUsuario,"label.justificacion23.resumen.numPreparadores")%>
                        </div>
                        <div style="width: 50%;  float: left;">
                            <input disabled type="text" id="numPreparadoresVal" name="numPreparadoresJus" size="3" maxlength="3" style="text-align: center;" value="" class="inputTexto total"/>
                        </div>
                    </div>
                    <div style="width: 30%; float: left;">
                        <div style="width: 50%; float: left; text-align: center; text-transform: uppercase" class="etiqueta">
                            <%=meLanbide35I18n.getMensaje(idiomaUsuario,"label.justificacion23.resumen.numSeguimientos")%>
                        </div>
                        <div style="width: 50%;  float: left;">
                            <input disabled type="text" id="numSeguimientosVal" name="numSeguimientosVal" size="3" maxlength="3" style="text-align: center;" value="" class="inputTexto total"/>
                        </div>
                    </div>
                </div>

                <div class="lineaFormulario" style="padding-top: 15px;">
                    <div style="width: 30%; float: left; padding-left: 17%;">
                        <div style="width: 50%; float: left; text-align: center; text-transform: uppercase" class="etiqueta">
                            <%=meLanbide35I18n.getMensaje(idiomaUsuario,"label.justificacion23.resumen.totInserciones")%>
                        </div>
                        <div style="width: 50%;  float: left;">
                            <input disabled type="text" id="totInsercionesVal" name="totInsercionesVal" size="10" maxlength="10" style="text-align: right;" value="" class="inputTexto total"/>
                        </div>
                    </div>
                    <div style="width: 30%; float: left;">
                        <div style="width: 50%; float: left; text-align: center; text-transform: uppercase" class="etiqueta">
                            <%=meLanbide35I18n.getMensaje(idiomaUsuario,"label.justificacion23.resumen.totSeguimientos")%>
                        </div>
                        <div style="width: 50%;  float: left;">
                            <input disabled type="text" id="totSeguimientosVal" name="totSeguimientosVal" size="10" maxlength="10" style="text-align: right;" value="" class="inputTexto total"/>
                        </div>
                    </div>
                </div>

                <div class="lineaFormulario" style="padding-top: 15px;">
                    <div style="width: 30%; float: left; padding-left: 17%;">
                        <div style="width: 50%; float: left; text-align: center; text-transform: uppercase" class="etiqueta">
                            <%=meLanbide35I18n.getMensaje(idiomaUsuario,"label.justificacion23.resumen.importeEca")%>
                        </div>
                        <div style="width: 50%;  float: left;">
                            <input disabled type="text" id="importeEcaVal" name="importeEcaVal" size="10" maxlength="10" style="text-align: right;" value="" class="inputTexto total"/>
                        </div>
                    </div>
                    <div style="width: 30%; float: left;">
                        <div style="width: 50%; float: left; text-align: center; text-transform: uppercase" class="etiqueta">
                            <%=meLanbide35I18n.getMensaje(idiomaUsuario,"label.justificacion23.resumen.importeValidado")%>
                        </div>
                        <div style="width: 50%;  float: left;">
                            <input disabled type="text" id="importeValidadoVal" name="importeValidadoVal" size="10" maxlength="10" style="text-align: right;" value="" class="inputTexto total"/>
                        </div>
                    </div>
                </div>

                <div class="lineaFormulario" style="padding-top: 15px;">
                    <div style="width: 100%; float: left; text-align: center; text-transform: uppercase" class="etiqueta">
                        <input type="button" class="botonGeneral" id="btnCopiar" value="<%=meLanbide35I18n.getMensaje(idiomaUsuario,"label.validacion.btnCopiar")%>" onClick="copiarDesdeJustificacionAValidacion();">
                    </div>
                </div>
            </fieldset>
            <div id="tab-panel-35_val" class="tab-pane" style="margin-top: 15px; float: left;" align="center"></div>
            <script type="text/javascript">
                tp35_val = new WebFXTabPane(document.getElementById("tab-panel-35_val"));
                tp35_val.selectedIndex = 0;
            </script>
            <div class="tab-page" id="tabPage35_val_2" style="height: 480px; width: 98%;">
                <h2 class="tab" id="pestana35_val_2" style="margin-left:10px;"><%=meLanbide35I18n.getMensaje(idiomaUsuario,"label.justificacion23.inserciones.tituloPestana")%></h2>
                <script type="text/javascript">tp35_val.addTabPage(document.getElementById("tabPage35_val_2"));</script>
                <div style="clear: both; padding-top: 14px;">
                    <jsp:include page="<%=urlPestanaValInsercionECA23%>" flush="true"/>
                </div>
            </div>
            <div class="tab-page" id="tabPage35_val_3" style="height: 480px; width: 98%;">
                <h2 class="tab" id="pestana35_val_3"><%=meLanbide35I18n.getMensaje(idiomaUsuario,"label.justificacion23.preparadores.tituloPestana")%></h2>
                <script type="text/javascript">tp35_val.addTabPage(document.getElementById("tabPage35_val_3"));</script>
                <div style="clear: both; padding-top: 14px;">
                    <jsp:include page="<%=urlPestanaValPreparadoresECA23%>" flush="true"/>
                </div>
            </div>
        </div>
    </div>
    <script  type="text/javascript">
        document.getElementById('numInsercionesVal').value = numInsercionesJ.toString();
        document.getElementById('numPreparadoresVal').value = numPreparadoresJ.toString();
        document.getElementById('numSeguimientosVal').value = numSeguimientosJ.toString();

        document.getElementById('totInsercionesVal').value = totInserciones.toString() + ' \u20ac';
        document.getElementById('totSeguimientosVal').value = totSeguimientos.toString() + ' \u20ac';
        document.getElementById('importeEcaVal').value = impEca.toString() + ' \u20ac';

        document.getElementById('importeValidadoVal').value = impValidado.toString() + ' \u20ac';
        <%
        if (importesConfiguracion!= null) {
        %>
        maxSeguimientosJ = <%=importesConfiguracion.getMaximoSeguimientos()%>;
        <%
            }
        %>
    </script>
</body>
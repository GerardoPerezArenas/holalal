<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>

<%@page import="es.altia.agora.interfaces.user.web.sge.TramitacionExpedientesForm" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.meikus.i18n.MeIkus01I18n" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.meikus.vo.DatosPasikusVO"%>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.common.service.config.Config"%>
<%@page import="es.altia.common.service.config.ConfigServiceHelper"%>
<%@page import="java.util.ArrayList" %>
<%@page import="java.util.List" %>
<%@page import="java.sql.Date" %>
<head>
    <meta http-equiv="X-UA-Compatible" content="IE=Edge" />
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <%
        int idiomaUsuario = 1;
        int apl = 5; // Pendiente de mirar
        int codOrganizacion = 0;
        String css = "";
        UsuarioValueObject usuario = new UsuarioValueObject();
        TramitacionExpedientesForm tramExpForm=new TramitacionExpedientesForm();
        try {
            if (session != null)
            {
                tramExpForm = (TramitacionExpedientesForm)session.getAttribute("TramitacionExpedientesForm");
                if (usuario != null)
                {
                    usuario = (UsuarioValueObject) session.getAttribute("usuario");
                    apl = usuario.getAppCod();
                    idiomaUsuario = usuario.getIdioma();
                    codOrganizacion  = usuario.getOrgCod();
                    css = usuario.getCss();
                }
            }
        }
        catch(Exception ex){}
        //Clase para internacionalizar los mensajes de la aplicacion.
        MeIkus01I18n meIkus01I18n = MeIkus01I18n.getInstance();
    
        String nombreModulo     = request.getParameter("nombreModulo");
        String numExpediente    = request.getParameter("numero");
    
        DatosPasikusVO datosPasikus = (DatosPasikusVO)request.getAttribute("datosPasikus");
    
    %>

    <jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
    <jsp:setProperty name="descriptor"  property="idi_cod" value="<%=idiomaUsuario%>" />
    <jsp:setProperty name="descriptor"  property="apl_cod" value="<%=apl%>" />
    <link rel="StyleSheet" media="screen" type="text/css" href="<%=request.getContextPath()%><%=css%>">
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/extension/meikus01/meikus01.css"/>

    <script type="text/javascript">
        const maxValue = 99999999.99;

        var auxConvert;
        var idiomaUsuario = <%=idiomaUsuario%>;
        <% String envioEika = datosPasikus.getEnvioEika(); %>
        var envioEika = '<%=envioEika != null ? envioEika.replace("null", "") : ""%>';
        <% String impReserva = datosPasikus.getImpReserva(); %>
        var impReserva = '<%=impReserva != null ? impReserva.replace("null", "") : ""%>';
        auxConvert = Number(impReserva);
        impReserva = impReserva != "" && auxConvert != NaN ? auxConvert : "";
        <% String idReserva = datosPasikus.getIdReserva(); %>
        var idReserva = '<%=idReserva != null ? idReserva.replace("null", "") : ""%>';
        <% String impanio1 = datosPasikus.getImpanio1(); %>
        var impanio1 = '<%=impanio1 != null ? impanio1.replace("null", "") : ""%>';
        auxConvert = Number(impanio1);
        impanio1 = impanio1 != "" && auxConvert != NaN ? auxConvert : "";
        <% String impanio2 = datosPasikus.getImpanio2(); %>
        var impanio2 = '<%=impanio2 != null ? impanio2.replace("null", "") : ""%>';
        auxConvert = Number(impanio2);
        impanio2 = impanio2 != "" && auxConvert != NaN ? auxConvert : "";
        <% String idIkus = datosPasikus.getIdIkus(); %>
        var idIkus = '<%=idIkus != null ? idIkus.replace("null", "") : ""%>';
        <% String expeIkad = datosPasikus.getExpeIkad(); %>
        var expeIkad = '<%=expeIkad != null ? expeIkad.replace("null", "") : ""%>';
        <% String importeConcedido = datosPasikus.getImporteConcedido(); %>
        var importeConcedido = '<%=importeConcedido != null ? importeConcedido.replace("null", "") : ""%>';
        auxConvert = Number(importeConcedido);
        importeConcedido = importeConcedido != "" && auxConvert != NaN ? auxConvert : "";
        <% String pago1 = datosPasikus.getPago1(); %>
        var pago1 = '<%=pago1 != null ? pago1.replace("null", "") : ""%>';
        auxConvert = Number(pago1);
        pago1 = pago1 != "" && auxConvert != NaN ? auxConvert : "";
        <% String pago2 = datosPasikus.getPago2(); %>
        var pago2 = '<%=pago2 != null ? pago2.replace("null", "") : ""%>';
        auxConvert = Number(pago2);
        pago2 = pago2 != "" && auxConvert != NaN ? auxConvert : "";
        <% String idPago1 = datosPasikus.getIdPago1(); %>
        var idPago1 = '<%=idPago1 != null ? idPago1.replace("null", "") : ""%>';
        <% String idPago2 = datosPasikus.getIdPago2(); %>
        var idPago2 = '<%=idPago2 != null ? idPago2.replace("null", "") : ""%>';
        <% String expeIkao1 = datosPasikus.getExpeIkao1(); %>
        var expeIkao1 = '<%=expeIkao1 != null ? expeIkao1.replace("null", "") : ""%>';
        <% String expeIkao2 = datosPasikus.getExpeIkao2(); %>
        var expeIkao2 = '<%=expeIkao2 != null ? expeIkao2.replace("null", "") : ""%>';
        var titleWindow = '<%=meIkus01I18n.getMensaje(idiomaUsuario, "label.pasikus.tituloPestana")%>';
        <% String mostrarBtnReserva = datosPasikus.getMostrarBtnReserva(); %>
        var mostrarBtnReserva = '<%=mostrarBtnReserva != null ? mostrarBtnReserva.replace("null", "") : ""%>';
        <% String mostrarBtnGrabarRes = datosPasikus.getMostrarBtnGrabarRes(); %>
        var mostrarBtnGrabarRes = '<%=mostrarBtnGrabarRes != null ? mostrarBtnGrabarRes.replace("null", "") : ""%>';
        <% String mostrarBtnGrabarPago = datosPasikus.getMostrarBtnGrabarPago(); %>
        var mostrarBtnGrabarPago = '<%=mostrarBtnGrabarPago != null ? mostrarBtnGrabarPago.replace("null", "") : ""%>';


        function cargarDatos() {
            console.log("cargarDatos: envioEika=" + envioEika + ", impReserva=" + impReserva + ", idReserva=" + idReserva + ", impanio1=" + impanio1 + ", impanio2=" +
                    impanio2 + ", idIkus=" + idIkus + ", expeIkad=" + expeIkad + ", importeConcedido=" + importeConcedido + ", pago1=" +
                    pago1 + ", pago2=" + pago2 + ", idPago1=" + idPago1 + ", idPago2=" + idPago2 + ", expeIkao1=" +
                    expeIkao1 + ", expeIkao2=" + expeIkao2 + ", idiomaUsuario=" + idiomaUsuario +
                    ", mostrarBtnReserva=" + mostrarBtnReserva + ", mostrarBtnGrabarRes=" + mostrarBtnGrabarRes +
                    ", mostrarBtnGrabarPago=" + mostrarBtnGrabarPago);
            document.getElementById("codENVIOEIKA").value = envioEika;
            document.getElementById("IMPRESERVA").value = impReserva.toString().replace(".", ",");
            document.getElementById("IDRESERVA").value = idReserva;
            document.getElementById("IMPANIO1").value = impanio1.toString().replace(".", ",");
            document.getElementById("IMPANIO2").value = impanio2.toString().replace(".", ",");
            document.getElementById("IDIKUS").value = idIkus;
            document.getElementById("EXPEIKAD").value = expeIkad;
            document.getElementById("IMPORTECONCEDIDO").value = importeConcedido.toString().replace(".", ",");
            document.getElementById("PAGO1").value = pago1.toString().replace(".", ",");
            document.getElementById("PAGO2").value = pago2.toString().replace(".", ",");
            document.getElementById("IDPAGO1").value = idPago1;
            document.getElementById("IDPAGO2").value = idPago2;
            document.getElementById("EXPEIKAO1").value = expeIkao1;
            document.getElementById("EXPEIKAO2").value = expeIkao2;
            document.getElementById("botonReservarCredito").disabled = mostrarBtnReserva === "1" ? false : true;
            document.getElementById("botonGrabarResolucion").disabled = mostrarBtnGrabarRes === "1" ? false : true;
            document.getElementById("botonGrabarPago").disabled = mostrarBtnGrabarPago === "1" ? false : true;
        }

        function reservarCreditoSinExt() {
            pleaseWait('on');

            var variableRes = {
                tarea: 'preparar'
                , modulo: 'MEIKUS01'
                , operacion: null
                , tipo: 0
                , numero: null
            };

            var dataParameter = $.extend({}, variableRes);
            dataParameter.numero = document.forms[0].numero.value; // N?mero de expediente
            dataParameter.convocatoria = dataParameter.numero.split('/')[0];

            console.log("reservarCreditoSinExt expediente=" + dataParameter.numero + ", convocatoria=" + dataParameter.convocatoria);
            dataParameter.control = new Date().getTime();
            dataParameter.operacion = 'reservarCreditoSinExt';
            dataParameter.idiomaUsuario = idiomaUsuario;

            var urlBaseLlamada = APP_CONTEXT_PATH + "/PeticionModuloIntegracion.do";

            $.ajax({
                type: 'POST',
                url: urlBaseLlamada,
                data: dataParameter,
                success: function (response) {
                    console.log("reservarCreditoSinExt response = " + response);
                    let respuesta = JSON.parse(response);
                    console.log("reservarCreditoSinExt respuesta.codError = " + respuesta.codError);
                    console.log("reservarCreditoSinExt respuesta.msgCodError = " + respuesta.msgCodError);
                    pleaseWait('off');
                    if (respuesta !== null) {
                        if (respuesta.codError == "0") {
                            jsp_alerta("A", '<%=meIkus01I18n.getMensaje(idiomaUsuario,"msg.AddPasikusStash")%>');
                            recargarDatosExpediente();
                        } else {
                            jsp_alerta("A", respuesta.msgCodError);
                        }
                    } else {
                        jsp_alerta("A", '<%=meIkus01I18n.getMensaje(idiomaUsuario,"error.AddPasikusStash")%>');
                    }
                },
                error: function (jqXHR, textStatus, errorThrown) {
                    pleaseWait('off');
                    jsp_alerta("A", '<%=meIkus01I18n.getMensaje(idiomaUsuario,"error.AddPasikusStash")%>');
                },
                async: true
            });
        }

        function grabarResolucionSinExt() {
            pleaseWait('on');

            var variableResol = {
                tarea: 'preparar'
                , modulo: 'MEIKUS01'
                , operacion: null
                , tipo: 0
                , numero: null
            };

            var dataParameter = $.extend({}, variableResol);
            dataParameter.numero = document.forms[0].numero.value; // N?mero de expediente
            dataParameter.convocatoria = dataParameter.numero.split('/')[0];

            console.log("grabarResolucionSinExt expediente=" + dataParameter.numero + ", convocatoria=" + dataParameter.convocatoria);
            dataParameter.control = new Date().getTime();
            dataParameter.operacion = 'grabarResolucionSinExt';
            dataParameter.idiomaUsuario = idiomaUsuario;

            var urlBaseLlamada = APP_CONTEXT_PATH + "/PeticionModuloIntegracion.do";

            $.ajax({
                type: 'POST',
                url: urlBaseLlamada,
                data: dataParameter,
                success: function (response) {
                    console.log("grabarResolucionSinExt response = " + response);
                    respuesta = JSON.parse(response);
                    console.log("grabarResolucionSinExt respuesta.codError = " + respuesta.codError);
                    console.log("grabarResolucionSinExt respuesta.msgCodError = " + respuesta.msgCodError);
                    pleaseWait('off');
                    if (respuesta !== null) {
                        if (respuesta.codError == "0") {
                            jsp_alerta("A", '<%=meIkus01I18n.getMensaje(idiomaUsuario,"msg.AddPasikusDecision")%>');
                            recargarDatosExpediente();
                        } else {
                            jsp_alerta("A", respuesta.msgCodError);
                        }
                    } else {
                        jsp_alerta("A", '<%=meIkus01I18n.getMensaje(idiomaUsuario,"error.AddPasikusDecision")%>');
                    }
                },
                error: function (jqXHR, textStatus, errorThrown) {
                    pleaseWait('off');
                    jsp_alerta("A", '<%=meIkus01I18n.getMensaje(idiomaUsuario,"error.AddPasikusDecision")%>');
                },
                async: true
            });
        }

        function grabarPagoSinExt() {
            pleaseWait('on');

            var variablePagos = {
                tarea: 'preparar'
                , modulo: 'MEIKUS01'
                , operacion: null
                , tipo: 0
                , numero: null
            };

            var dataParameter = $.extend({}, variablePagos);
            dataParameter.numero = document.forms[0].numero.value; // N?mero de expediente
            dataParameter.convocatoria = dataParameter.numero.split('/')[0];
            dataParameter.idiomaUsuario = idiomaUsuario;

            console.log("grabarPagoSinExt expediente=" + dataParameter.numero + ", convocatoria=" + dataParameter.convocatoria);
            dataParameter.control = new Date().getTime();
            dataParameter.operacion = 'grabarPagoSinExt';

            var urlBaseLlamada = APP_CONTEXT_PATH + "/PeticionModuloIntegracion.do";

            console.log("grabarPagoSinExt");

            $.ajax({
                type: 'POST',
                url: urlBaseLlamada,
                data: dataParameter,
                success: function (response) {
                    console.log("grabarPagoSinExt response = " + response);
                    respuesta = JSON.parse(response);
                    console.log("grabarPagoSinExt respuesta.codError = " + respuesta.codError);
                    console.log("grabarPagoSinExt respuesta.msgCodError = " + respuesta.msgCodError);
                    pleaseWait('off');
                    if (respuesta !== null) {
                        if (respuesta.codError == "0") {
                            jsp_alerta("A", '<%=meIkus01I18n.getMensaje(idiomaUsuario,"msg.AddPasikusPayment")%>');
                            recargarDatosExpediente();
                        } else {
                            jsp_alerta("A", respuesta.msgCodError);
                        }
                    } else {
                        jsp_alerta("A", '<%=meIkus01I18n.getMensaje(idiomaUsuario,"error.AddPasikusPayment")%>');
                    }
                },
                error: function (jqXHR, textStatus, errorThrown) {
                    pleaseWait('off');
                    jsp_alerta("A", '<%=meIkus01I18n.getMensaje(idiomaUsuario,"error.AddPasikusPayment")%>');
                },
                async: true
            });
        }

        function guardarDatosPasikus() {
            pleaseWait('on');

            var parametrosLLamadaMeIkus01 = {
                tarea: 'preparar'
                , modulo: 'MEIKUS01'
                , operacion: null
                , tipo: 0
                , numero: null
            };

            var dataParameter = $.extend({},parametrosLLamadaMeIkus01);

            dataParameter.numero = document.forms[0].numero.value; // N?mero de expediente
            var codMun = document.forms[0].codMunicipio.value;
            var ejercicio = document.forms[0].ejercicio.value;
            var numero = document.forms[0].numero.value;
            var expHistorico = document.forms[0].expHistorico.value;
            dataParameter.codMunicipio = document.forms[0].codMunicipio.value;
            dataParameter.ejercicio = document.forms[0].ejercicio.value;
            dataParameter.numero = document.forms[0].numero.value;
            dataParameter.expHistorico = document.forms[0].expHistorico.value;
            dataParameter.codProcedimiento = document.forms[0].codProcedimiento.value;
            dataParameter.envioEika = document.getElementById("codENVIOEIKA").value;
            dataParameter.impReserva = document.getElementById("IMPRESERVA").value;
            dataParameter.idReserva = document.getElementById("IDRESERVA").value;
            dataParameter.impAnio1 = document.getElementById("IMPANIO1").value;
            dataParameter.impAnio2 = document.getElementById("IMPANIO2").value;
            dataParameter.idIkus = document.getElementById("IDIKUS").value;
            dataParameter.expeIkad = document.getElementById("EXPEIKAD").value;
            dataParameter.importeConcedido = document.getElementById("IMPORTECONCEDIDO").value;
            dataParameter.pago1 = document.getElementById("PAGO1").value;
            dataParameter.pago2 = document.getElementById("PAGO2").value;
            dataParameter.idPago1 = document.getElementById("IDPAGO1").value;
            dataParameter.idPago2 = document.getElementById("IDPAGO2").value;
            dataParameter.expeIkao1 = document.getElementById("EXPEIKAO1").value;
            dataParameter.expeIkao2 = document.getElementById("EXPEIKAO2").value;

            console.log("guardarDatosPasikus envioEika=" + dataParameter.envioEika + ", impReserva=" + dataParameter.impReserva +
                    ", idReserva=" + dataParameter.idReserva + ", impanio1=" + dataParameter.impAnio1 + ", impanio2=" +
                    dataParameter.impAnio2 + ", idIkus=" + dataParameter.idIkus + ", expeIkad=" + dataParameter.expeIkad +
                    ", importeConcedido=" + dataParameter.importeConcedido + ", pago1=" + dataParameter.pago1 + ", pago2=" +
                    dataParameter.pago2 + ", idPago1=" + dataParameter.idPago1 + ", idPago2=" + dataParameter.idPago2 +
                    ", expeIkao1=" + dataParameter.expeIkao1 + ", expeIkao2=" + dataParameter.expeIkao2);
            dataParameter.control = new Date().getTime();
            dataParameter.operacion = 'grabarDatosPasikus';

            var urlBaseLlamada = APP_CONTEXT_PATH + "/PeticionModuloIntegracion.do";

            console.log("guardarDatosPasikus");

            $.ajax({
                type: 'POST',
                url: urlBaseLlamada,
                data: dataParameter,
                success: function (response) {
                    console.log("guardarDatosPasikus response = " + response);
                    respuesta = JSON.parse(response);
                    console.log("guardarDatosPasikus respuesta = " + respuesta);
                    pleaseWait('off');
                    if (respuesta !== null) {
                        if (respuesta == "1") {
                            jsp_alerta("A", '<%=meIkus01I18n.getMensaje(idiomaUsuario,"msg.AddPasikusData")%>');
                            recargarDatosExpediente();
                        } else {
                            jsp_alerta("A", '<%=meIkus01I18n.getMensaje(idiomaUsuario,"error.AddPasikusData")%>');
                        }
                    } else {
                        jsp_alerta("A", '<%=meIkus01I18n.getMensaje(idiomaUsuario,"error.AddPasikusData")%>');
                    }
                },
                error: function (jqXHR, textStatus, errorThrown) {
                    pleaseWait('off');
                    jsp_alerta("A", '<%=meIkus01I18n.getMensaje(idiomaUsuario,"error.AddPasikusData")%>');
                },
                async: true
            });
        }

        function comboEnvioEika() {
            var variableCombo = new Combo("ENVIOEIKA");
            //        variableCombo.change = update;
            var lcodENVIOEIKA = new Array();
            var ldescENVIOEIKA = new Array();

            lcodENVIOEIKA.push("S");
            ldescENVIOEIKA.push("SI");
            lcodENVIOEIKA.push("N");
            ldescENVIOEIKA.push("NO");

            variableCombo.addItems(lcodENVIOEIKA, ldescENVIOEIKA);
        }

        function update() {
            console.log("update resultado = " + document.getElementById("codENVIOEIKA").value);
        }

        function loadYesNo(value) {
            if (value == 'S') {
                document.getElementById("descENVIOEIKA").value = "SI";
            } else if (value == 'N') {
                document.getElementById("descENVIOEIKA").value = "NO";
            } else {
                document.getElementById("descENVIOEIKA").value = "";
            }
        }

        function onlyNumberKey(evt, acum) {
            //        console.log ("onlyNumberKey " + acum + ", " + acum.indexOf(",") + ", " + acum.length);
            // Only ASCII character in that range allowed
            var ASCIICode = (evt.which) ? evt.which : evt.keyCode;
            if (ASCIICode >= 48 && ASCIICode <= 57) {
                if (acum.indexOf(",") == -1)
                    return true;
                else if (acum.indexOf(",") != -1 && acum.indexOf(",") >= acum.length - 2)
                    return true;
            } else if (ASCIICode == 44 && acum.indexOf(",") == -1)
                return true;

            return false;
        }

        function testValue(acum, txtId) {
            acum = acum.replace(",", ".");
            if (isNaN(acum)) {
                alert("Valor incorrecto: Debe de ser un número");
                document.getElementById(txtId).value = "";
                document.getElementById(txtId).focus();
            } else if (acum > maxValue) {
                alert("El número no puede ser superior a " + maxValue);
                acum /= 10;
                while (acum > maxValue)
                    acum /= 10;
                $("#" + txtId).val(Math.floor(acum));
            }
        }

        function comprobarAutorizado() {
            console.log("comprobarAutorizado");

            var parametrosLLamadaMeIkus01 = {
                tarea: 'preparar'
                , modulo: 'MEIKUS01'
                , operacion: null
                , tipo: 0
                , numero: null
            };

            var dataParameter = $.extend({}, parametrosLLamadaMeIkus01);

            dataParameter.numero = document.forms[0].numero.value; // N?mero de expediente
            var codMun = document.forms[0].codMunicipio.value;
            var ejercicio = document.forms[0].ejercicio.value;
            var numero = document.forms[0].numero.value;
            var expHistorico = document.forms[0].expHistorico.value;
            dataParameter.codMunicipio = document.forms[0].codMunicipio.value;
            dataParameter.ejercicio = document.forms[0].ejercicio.value;
            dataParameter.numero = document.forms[0].numero.value;
            dataParameter.expHistorico = document.forms[0].expHistorico.value;
            dataParameter.codProcedimiento = document.forms[0].codProcedimiento.value;
            dataParameter.impReserva = document.getElementById("IMPRESERVA").value;
            dataParameter.impAnio1 = document.getElementById("IMPANIO1").value;
            dataParameter.impAnio2 = document.getElementById("IMPANIO2").value;

            console.log("comprobarAutorizado impReserva=" + dataParameter.impReserva +
                    ", impanio1=" + dataParameter.impAnio1 + ", impanio2=" +
                    dataParameter.impAnio2);
            dataParameter.control = new Date().getTime();
            dataParameter.operacion = 'comprobarAutorizado';

            var urlBaseLlamada = APP_CONTEXT_PATH + "/PeticionModuloIntegracion.do";

            if (dataParameter.impReserva == "" || dataParameter.impAnio1 == "" || dataParameter.impAnio2 == "") {
                $.ajax({
                    type: 'POST',
                    url: urlBaseLlamada,
                    data: dataParameter,
                    success: function (response) {
                        console.log("comprobarAutorizado response = " + response);
                        respuesta = JSON.parse(response);
                        if (respuesta !== null) {
                            if (respuesta == "1") {
                                recargarDatosExpediente();
                            }
                        }
                    },
                    error: function (jqXHR, textStatus, errorThrown) {
                        jsp_alerta("A", '<%=meIkus01I18n.getMensaje(idiomaUsuario,"error.AddPasikusData")%>');
                    },
                    async: true
                });
            }
        }


        function recargarDatosExpediente() {
            pleaseWait('on');
            document.forms[0].opcion.value = "cargarPestTram";
            document.forms[0].target = "mainFrame";
            document.forms[0].action = "<c:url value='/sge/FichaExpediente.do'/>";
            document.forms[0].submit();
        }

    </script>
</head>
<body>
    <!--<body>-->
    <div class="tab-page" id="tabDatosPasikus" style="height:90%; width: 100%;">
        <h2 class="tab" id="pestanaDatosPasikus"><%=meIkus01I18n.getMensaje(idiomaUsuario,"label.pasikus.tituloPestana")%></h2>
        <script type="text/javascript">tp1.addTabPage(document.getElementById("tabDatosPasikus"));</script>
        <div style="clear: both;">
            <br>
            <div class="sub3titulo" style="clear: both; text-align: center; font-size: 14px;">
                <span id="tituloVentana" style="width: 98%;"></span>
            </div>
            <div id="divGeneral"  style="overflow-y: auto; overflow-x: hidden; height: 90%;">
                <form id="frmDatosPasikus">
                    <fieldset id="program" name="program">
                        <div class="lineaFormulario">
                            <div style="width: 200px;" class="etiquetaPasikus"><%=meIkus01I18n.getMensaje(idiomaUsuario,"label.datosPasikus.envioEika")%></div>
                            <div style="float: left;">
                                <!--<input type="hidden" id="ENVIOEIKA">-->
                                <input type="text" name="codENVIOEIKA" id="codENVIOEIKA" size="2" class="inputCombo" value="" onkeyup="xAMayusculas(this);" maxlength="1" />
                                <input type="text" name="descENVIOEIKA"  id="descENVIOEIKA" size="10" class="inputCombo" readonly value="" />
                                <a href="" id="anchorENVIOEIKA" name="anchorENVIOEIKA">
                                    <span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonENVIOEIKA" name="botonENVIOEIKA"
                                          style="cursor:pointer;"></span>
                                </a>
                            </div>
                        </div>
                            <br><br>
                        <hr>
                        <fieldset id="reserva" name="reserva">
                            <legend class="legendAzulPasikus"><%=meIkus01I18n.getMensaje(idiomaUsuario,"label.pasikus.reserva")%></legend>
                            <div class="lineaFormulario">
                                <div class="etiquetaPasikus"><%=meIkus01I18n.getMensaje(idiomaUsuario,"label.datosPasikus.impReserva")%></div>
                                <div style="width: 300px;float: left;">
                                    <input id="IMPRESERVA" name="IMPRESERVA" type="text" class="inputTexto" maxlength="11" value=""
                                           onkeypress="return onlyNumberKey(event, document.getElementById('IMPRESERVA').value)"
                                           onfocusout="return testValue(document.getElementById('IMPRESERVA').value, 'IMPRESERVA')"/>
                                </div>
                                <div style="margin-left: 20px;" class="etiquetaPasikus"><%=meIkus01I18n.getMensaje(idiomaUsuario,"label.datosPasikus.idReserva")%></div>
                                <div style="width: 300px;float: left;">
                                    <input id="IDRESERVA" name="IDRESERVA" type="text" class="inputTextoReadOnly" maxlength="20" disabled />
                                </div>
                            </div>
                            <div class="lineaFormulario">
                                <div class="etiquetaPasikus"><%=meIkus01I18n.getMensaje(idiomaUsuario,"label.datosPasikus.impanio1")%></div>
                                <div style="width: 300px;float: left;">
                                    <input id="IMPANIO1" name="IMPANIO1" type="text" class="inputTexto" maxlength="11"
                                           onkeypress="return onlyNumberKey(event, document.getElementById('IMPANIO1').value)"
                                           onfocusout="return testValue(document.getElementById('IMPANIO1').value, 'IMPANIO1')"/>
                                </div>                        
                                <div style="margin-left: 20px;" class="etiquetaPasikus"><%=meIkus01I18n.getMensaje(idiomaUsuario,"label.datosPasikus.impanio2")%></div>
                                <div style="width: 300px;float: left;">
                                    <input id="IMPANIO2" name="IMPANIO2" type="text" class="inputTexto" maxlength="11"
                                           onkeypress="return onlyNumberKey(event, document.getElementById('IMPANIO2').value)"
                                           onfocusout="return testValue(document.getElementById('IMPANIO2').value, 'IMPANIO2')"/>
                                </div>
                            </div>
                            <div class="lineaFormulario">
                                <div style="margin-left: 320px">
                                    <input type="button" id="botonReservarCredito" name="botonReservarCredito" value='<%=meIkus01I18n.getMensaje(idiomaUsuario,"label.datosPasikus.botonReservarCredito")%>' onclick="reservarCreditoSinExt();" ></input>
                                </div>
                            </div>
                        </fieldset>
                        <br>
                        <fieldset id= "resolucion" name="resolucion">
                            <legend class="legendAzulPasikus"><%=meIkus01I18n.getMensaje(idiomaUsuario,"label.pasikus.resolucion")%></legend>
                            <div class="lineaFormulario">
                                <div class="etiquetaPasikus"><%=meIkus01I18n.getMensaje(idiomaUsuario,"label.datosPasikus.idIkus")%></div>
                                <div style="width: 300px;float: left;">
                                    <input id="IDIKUS" name="IDIKUS" type="text" class="inputTextoReadOnly" maxlength="20" style="width: 225px;" disabled />
                                </div>                            
                                <div style="margin-left: 20px;" class="etiquetaPasikus"><%=meIkus01I18n.getMensaje(idiomaUsuario,"label.datosPasikus.expeIkad")%></div>
                                <div style="width: 300px;float: left;">
                                    <input id="EXPEIKAD" name="EXPEIKAD" type="text" class="inputTextoReadOnly" maxlength="30"  style="width: 225px;" disabled />
                                </div>
                            </div>
                            <div class="lineaFormulario">
                                <div style="margin-left: 320px">
                                    <input type="button" id="botonGrabarResolucion" name="botonGrabarResolucion" value='<%=meIkus01I18n.getMensaje(idiomaUsuario, "label.datosPasikus.botonGrabarResolucion")%>' onclick="grabarResolucionSinExt();" ></input>
                                </div>
                            </div>
                        </fieldset>
                        <br>
                        <fieldset id="pago" name="pago">
                            <legend class="legendAzulPasikus"><%=meIkus01I18n.getMensaje(idiomaUsuario,"label.pasikus.pago")%></legend>
                            <div class="lineaFormulario">
                                <div class="etiquetaPasikus"><%=meIkus01I18n.getMensaje(idiomaUsuario,"label.datosPasikus.importeConcedido")%></div>
                                <div style="width: 300px;float: left;">
                                    <input id="IMPORTECONCEDIDO" name="IMPORTECONCEDIDO" type="text" class="inputTexto" maxlength="11"
                                           onkeypress="return onlyNumberKey(event, document.getElementById('IMPORTECONCEDIDO').value)"
                                           onfocusout="return testValue(document.getElementById('IMPORTECONCEDIDO').value, 'IMPORTECONCEDIDO')"/>
                                </div>
                            </div> 
                            <div class="lineaFormulario">
                                <div class="etiquetaPasikus"><%=meIkus01I18n.getMensaje(idiomaUsuario,"label.datosPasikus.pago1")%></div>
                                <div style="width: 300px;float: left;">
                                    <input id="PAGO1" name="PAGO1" type="text" class="inputTexto" maxlength="11"
                                           onkeypress="return onlyNumberKey(event, document.getElementById('PAGO1').value)"
                                           onfocusout="return testValue(document.getElementById('PAGO1').value, 'PAGO1')" />
                                </div>                            
                                <div style="margin-left: 20px;" class="etiquetaPasikus"><%=meIkus01I18n.getMensaje(idiomaUsuario,"label.datosPasikus.pago2")%></div>
                                <div style="width: 300px;float: left;">
                                    <input id="PAGO2" name="PAGO2" type="text" class="inputTexto" maxlength="11"
                                           onkeypress="return onlyNumberKey(event, document.getElementById('PAGO2').value)"
                                           onfocusout="return testValue(document.getElementById('PAGO2').value, 'PAGO2')" />
                                </div>
                            </div>                             
                            <div class="lineaFormulario">
                                <div class="etiquetaPasikus"><%=meIkus01I18n.getMensaje(idiomaUsuario,"label.datosPasikus.idPago1")%></div>
                                <div style="width: 300px;float: left;">
                                    <input id="IDPAGO1" name="IDPAGO1" type="text" class="inputTextoReadOnly" maxlength="20" style="width: 225px;" disabled />
                                </div>
                                <div style="margin-left: 20px;" class="etiquetaPasikus"><%=meIkus01I18n.getMensaje(idiomaUsuario,"label.datosPasikus.idPago2")%></div>
                                <div style="width: 300px;float: left;">
                                    <input id="IDPAGO2" name="IDPAGO2" type="text" class="inputTextoReadOnly" maxlength="20" style="width: 225px;" disabled />
                                </div>
                            </div> 
                            <div class="lineaFormulario">
                                <div class="etiquetaPasikus"><%=meIkus01I18n.getMensaje(idiomaUsuario,"label.datosPasikus.expeIkao1")%></div>
                                <div style="width: 300px;float: left;">
                                    <input id="EXPEIKAO1" name="EXPEIKAO1" type="text" class="inputTexto" maxlength="30" style="width: 225px;" disabled />
                                </div>
                                <div style="margin-left: 20px;" class="etiquetaPasikus"><%=meIkus01I18n.getMensaje(idiomaUsuario,"label.datosPasikus.expeIkao2")%></div>
                                <div style="width: 300px;float: left;">
                                    <input id="EXPEIKAO2" name="EXPEIKAO2" type="text" class="inputTexto" maxlength="30" style="width: 225px;" disabled />
                                </div>
                            </div>
                            <div class="lineaFormulario">
                                <div style="margin-left: 320px">
                                    <input type="button" id="botonGrabarPago" name="botonGrabarPago" value='<%=meIkus01I18n.getMensaje(idiomaUsuario, "label.datosPasikus.botonGrabarPago")%>' onclick="grabarPagoSinExt();" ></input>
                                </div>
                            </div>
                        </fieldset>
                        <!--<br><br>-->
                        <div class="lineaFormulario">
                            <div class="botonera" style="padding-top: 15px; text-align: center;">
                                <input type="button" id="botonGrabarDatosPasikus" name="botonGrabarDatosPasikus" value='<%=meIkus01I18n.getMensaje(idiomaUsuario, "label.datosPasikus.boton")%>' onclick="guardarDatosPasikus();" ></input>
                            </div>
                        </div>  
                    </fieldset> 
                </form>
            </div>
        </div>
        <!-- Es la capa m?s externa de la pesta?a -->
    </div>
</body>
<script type="text/javascript">
    cargarDatos();
    comprobarAutorizado();
    loadYesNo(envioEika);
    document.getElementById('tituloVentana').innerHTML = titleWindow;
    comboEnvioEika();
</script>

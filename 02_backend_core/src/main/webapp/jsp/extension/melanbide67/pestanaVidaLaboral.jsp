<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>

<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide_interop.i18n.MeLanbideInteropI18n" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide_interop.vo.RegistroVidaLaboralVO"%>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.common.service.config.Config"%>
<%@page import="es.altia.common.service.config.ConfigServiceHelper"%>
<%@page import="java.util.ArrayList" %>
<%@page import="java.util.List" %>
<%@page import="java.sql.Date" %>


<%
    int idiomaUsuario = 1;
    int apl = 5; // Pendiente de mirar
    int codOrganizacion = 0;
    String css = "";
    UsuarioValueObject usuario = new UsuarioValueObject();
    try
    {
        if (session != null)
        {
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
    MeLanbideInteropI18n meLanbideInteropI18n = MeLanbideInteropI18n.getInstance();

    String nombreModulo     = request.getParameter("nombreModulo");
    String numExpediente    = request.getParameter("numero");

    List<RegistroVidaLaboralVO> registrosVidaLaboral = (List<RegistroVidaLaboralVO>)request.getAttribute("listaRegistros");

%>

<jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
<jsp:setProperty name="descriptor"  property="idi_cod" value="<%=idiomaUsuario%>" />
<jsp:setProperty name="descriptor"  property="apl_cod" value="<%=apl%>" />
<link rel="StyleSheet" media="screen" type="text/css" href="<%=request.getContextPath()%><%=css%>">

<script type="text/javascript">

    var parametrosLLamadaM67 = {
        tarea: 'preparar'
        , modulo: 'MELANBIDE67'
        , operacion: null
        , tipo: 0
        , numero: null
    };

    var parametrosLLamadaMInterop = {
        tarea: 'preparar'
        , modulo: 'MELANBIDE_INTEROP'
        , operacion: null
        , tipo: 0
        , numero: null
    };

    //Tabla vidaLaboral
    var listaLaboral = new Array();

    var urlBaseLlamada = APP_CONTEXT_PATH + "/PeticionModuloIntegracion.do";

    function obtenerDatosVidaLaboralIntermediacion() {
        pleaseWait('on');

        var dataParameter = $.extend({}, parametrosLLamadaM67);

        dataParameter.numero = document.forms[0].numero.value; // Número de expediente
        dataParameter.ejercicioHHFF = document.forms[0].numero.value.split("/")[0]; // Número de expediente
        dataParameter.procedimientoHHFF = document.forms[0].numero.value.split("/")[1]; // Número de expediente
        dataParameter.numeroExpedienteDesde = parseInt(document.forms[0].numero.value.split("/")[2]);
        dataParameter.numeroExpedienteHasta = parseInt(document.forms[0].numero.value.split("/")[2]);
        dataParameter.fechaDesdeCVL = $("#fechaDesde").val();
        dataParameter.fechaHastaCVL = $("#fechaHasta").val();
        dataParameter.fkWSSolicitado = "5";
        dataParameter.control = new Date().getTime();
        dataParameter.operacion = 'consultaVidaLaboralCVLBatchExpediente';

        console.log("obtenerDatosVidaLaboralIntermediacion dataParameter.numero=" + dataParameter.numero +
                ", dataParameter.control=" + dataParameter.control +
                ", dataParameter.operacion=" + dataParameter.operacion +
                ", dataParameter.fechaDesdeCVL=" + dataParameter.fechaDesdeCVL +
                ", dataParameter.fechaHastaCVL=" + dataParameter.fechaHastaCVL +
                ", dataParameter.procedimientoHHFF=" + dataParameter.procedimientoHHFF +
                ", dataParameter.numeroExpedienteDesde=" + dataParameter.numeroExpedienteDesde +
                ", dataParameter.numeroExpedienteHasta=" + dataParameter.numeroExpedienteHasta +
                ", dataParameter.ejercicioHHFF=" + dataParameter.ejercicioHHFF);

        $.ajax({
            type: 'POST',
            url: urlBaseLlamada,
            data: dataParameter,
            success: function (response) {
                console.log("consultaVidaLaboralCVLBatchExpediente response = " + response);
                respuesta = JSON.parse(response);
                console.log("consultaVidaLaboralCVLBatchExpediente respuesta = " + respuesta.codRespuesta + ", " + respuesta.listaRegistros);
                pleaseWait('off');
                if (respuesta !== null) {
                    if (respuesta.codRespuesta == "0000") {
                        if (respuesta.listaRegistros.length > 0) {
                            jsp_alerta("A", '<%=meLanbideInteropI18n.getMensaje(idiomaUsuario,"msg.informacionInsertadosRegistrosVidaLaboral")%>');
                            loadWSDataOnTable(respuesta.listaRegistros);
                        } else {
                            jsp_alerta("A", '<%=meLanbideInteropI18n.getMensaje(idiomaUsuario,"msg.informacionNoHayRegistrosVidaLaboral")%>');
                        }
                    } else if (respuesta.codRespuesta == "0001") {
                        jsp_alerta("A", '<%=meLanbideInteropI18n.getMensaje(idiomaUsuario,"error.NotExistsDocument")%>');
                    } else if (respuesta.codRespuesta == "0002") {
                        jsp_alerta("A", '<%=meLanbideInteropI18n.getMensaje(idiomaUsuario,"error.DuplicatedDocumentInSS")%>');
                    } else if (respuesta.codRespuesta == "0257") {
                        jsp_alerta("A", '<%=meLanbideInteropI18n.getMensaje(idiomaUsuario,"error.WSVidaLaboralServiceDatabiggerToday")%>');
                    } else if (respuesta.codRespuesta == "0252") {
                        jsp_alerta("A", '<%=meLanbideInteropI18n.getMensaje(idiomaUsuario,"error.WSVidaLaboralServiceBeginDataBefore5Years")%>');
                    } else if (respuesta.codRespuesta == "0231") {
                        jsp_alerta("A", '<%=meLanbideInteropI18n.getMensaje(idiomaUsuario,"error.WSVidaLaboralServiceIncorrectDocument")%>');
                    } else if (respuesta.codRespuesta == "0254") {
                        jsp_alerta("A", '<%=meLanbideInteropI18n.getMensaje(idiomaUsuario,"error.WSVidaLaboralServiceLittleInformacion")%>');
                    } else if (respuesta.codRespuesta == "0314") {
                        jsp_alerta("A", '<%=meLanbideInteropI18n.getMensaje(idiomaUsuario,"error.NoPermissionForSuchProcCod")%>');
                    } else {
                        jsp_alerta("A", '<%=meLanbideInteropI18n.getMensaje(idiomaUsuario,"error.WSVidaLaboralUnknownError")%>');
                    }
                } else {
                    jsp_alerta("A", '<%=meLanbideInteropI18n.getMensaje(idiomaUsuario,"error.WSVidaLaboralServiceNotAvailable")%>');
                }
            },
            error: function (jqXHR, textStatus, errorThrown) {
                pleaseWait('off');
                console.log("consultaVidaLaboralCVLBatchExpediente 1 jqXHR.responseText = " + jqXHR.responseText);
                jsp_alerta("A", '<%=meLanbideInteropI18n.getMensaje(idiomaUsuario,"error.WSVidaLaboralServiceNotAvailable")%>');
            },
            async: true
        });
    }

    function pulsarExportarExcelVidaLaboral() {

        var parametros = "";
        parametros = '?tarea=preparar&modulo=MELANBIDE_INTEROP&operacion=generarExcelVidaLaboral&tipo=0&numExp=<%=numExpediente%>';
        window.open(urlBaseLlamada + parametros, "_blank");
    }

    function loadBDDataOnTable() {
        console.log("loadBDDataOnTable");
        var listaLaboralTabla = new Array();
        var tabLaboral = new Tabla(true, '<%=descriptor.getDescripcion("buscar")%>', '<%=descriptor.getDescripcion("anterior")%>', '<%=descriptor.getDescripcion("siguiente")%>', '<%=descriptor.getDescripcion("mosFilasPag")%>', '<%=descriptor.getDescripcion("msgNoResultBusq")%>', '<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>', '<%=descriptor.getDescripcion("filtrDeTotal")%>', '<%=descriptor.getDescripcion("primero")%>', '<%=descriptor.getDescripcion("ultimo")%>', document.getElementById('listaLaboral'), 820);
        tabLaboral.addColumna('10', 'left', "<%= meLanbideInteropI18n.getMensaje(idiomaUsuario,"label.vidalaboral.col1")%>");
        tabLaboral.addColumna('10', 'left', "<%= meLanbideInteropI18n.getMensaje(idiomaUsuario,"label.vidalaboral.col2")%>");
        tabLaboral.addColumna('10', 'left', "<%= meLanbideInteropI18n.getMensaje(idiomaUsuario,"label.vidalaboral.col3")%>");
        tabLaboral.addColumna('10', 'left', "<%= meLanbideInteropI18n.getMensaje(idiomaUsuario,"label.vidalaboral.col4")%>");
        tabLaboral.addColumna('10', 'left', "<%= meLanbideInteropI18n.getMensaje(idiomaUsuario,"label.vidalaboral.col5")%>");
        tabLaboral.addColumna('10', 'left', "<%= meLanbideInteropI18n.getMensaje(idiomaUsuario,"label.vidalaboral.col6")%>");
        tabLaboral.addColumna('10', 'left', "<%= meLanbideInteropI18n.getMensaje(idiomaUsuario,"label.vidalaboral.col7")%>");
        tabLaboral.addColumna('10', 'left', "<%= meLanbideInteropI18n.getMensaje(idiomaUsuario,"label.vidalaboral.col8")%>");
        tabLaboral.addColumna('10', 'left', "<%= meLanbideInteropI18n.getMensaje(idiomaUsuario,"label.vidalaboral.col9")%>");
        tabLaboral.addColumna('10', 'left', "<%= meLanbideInteropI18n.getMensaje(idiomaUsuario,"label.vidalaboral.col10")%>");
        tabLaboral.addColumna('10', 'left', "<%= meLanbideInteropI18n.getMensaje(idiomaUsuario,"label.vidalaboral.col11")%>");
        tabLaboral.addColumna('10', 'left', "<%= meLanbideInteropI18n.getMensaje(idiomaUsuario,"label.vidalaboral.col12")%>");
        tabLaboral.addColumna('10', 'left', "<%= meLanbideInteropI18n.getMensaje(idiomaUsuario,"label.vidalaboral.col13")%>");
        tabLaboral.addColumna('10', 'left', "<%= meLanbideInteropI18n.getMensaje(idiomaUsuario,"label.vidalaboral.col14")%>");
        tabLaboral.addColumna('10', 'left', "<%= meLanbideInteropI18n.getMensaje(idiomaUsuario,"label.vidalaboral.col15")%>");
        tabLaboral.addColumna('10', 'left', "<%= meLanbideInteropI18n.getMensaje(idiomaUsuario,"label.vidalaboral.col16")%>");
        tabLaboral.addColumna('10', 'left', "<%= meLanbideInteropI18n.getMensaje(idiomaUsuario,"label.vidalaboral.col17")%>");
        tabLaboral.displayCabecera = true;
        tabLaboral.height = 100;
    <%
            RegistroVidaLaboralVO registroVO = null;
            if (registrosVidaLaboral!= null && registrosVidaLaboral.size() >0){
                for (int indice=0 ; indice < registrosVidaLaboral.size() ; indice++)
                {
                    registroVO = registrosVidaLaboral.get(indice);
    %>
    <% String tipoDocumentacionBD = registroVO.getTipoDocumentacion(); %>
        var tipoDocumentacionBD = '<%=tipoDocumentacionBD != null ? tipoDocumentacionBD.replace("null", "") : ""%>';
    <% String documentacionBD = registroVO.getDocumentacion(); %>
        var documentacionBD = '<%=documentacionBD != null ? documentacionBD.replace("null", "") : ""%>';
    <% Date fechaDesdeBD = registroVO.getFechaDesde(); %>
        var fechaDesdeBD = '<%=fechaDesdeBD != null ? fechaDesdeBD : ""%>';
        fechaDesdeBD = fechaDesdeBD != "" ? getDateFormatted(fechaDesdeBD) : "";
    <% Date fechaHastaBD = registroVO.getFechaHasta(); %>
        var fechaHastaBD = '<%=fechaHastaBD != null ? fechaHastaBD : ""%>';
        fechaHastaBD = fechaHastaBD != "" ? getDateFormatted(fechaHastaBD) : "";
    <% String numeroAfiliacionLBD = registroVO.getNumeroAfiliacionL(); %>
        var numeroAfiliacionLBD = '<%=numeroAfiliacionLBD != null ? numeroAfiliacionLBD.replace("null", "") : ""%>';
    <% Date fechaNacimientoBD = registroVO.getFechaNacimiento(); %>
        var fechaNacimientoBD = '<%=fechaNacimientoBD != null ? fechaNacimientoBD : ""%>';
        fechaNacimientoBD = fechaNacimientoBD != "" ? getDateFormatted(fechaNacimientoBD) : "";
        var resumenConplTotalDiasAlta = '<%=registroVO.getResumenConplTotalDiasAlta()%>';
    <% String regimenBD = registroVO.getRegimen(); %>
        var regimenBD = '<%=regimenBD != null ? regimenBD.replace("null", "") : ""%>';
    <% String codCuentaCotBD = registroVO.getCodCuentaCot(); %>
        var codCuentaCotBD = '<%=codCuentaCotBD != null ? codCuentaCotBD.replace("null", "") : ""%>';
    <% String provinciaBD = registroVO.getProvincia(); %>
        var provinciaBD = '<%=provinciaBD != null ? provinciaBD.replace("null", "") : ""%>';
    <% Date fechaAltaBD = registroVO.getFechaAlta(); %>
        var fechaAltaBD = '<%=fechaAltaBD != null ? fechaAltaBD : ""%>';
        fechaAltaBD = fechaAltaBD != "" ? getDateFormatted(fechaAltaBD) : "";
    <% Date fechaEfectosBD = registroVO.getFechaEfectos(); %>
        var fechaEfectosBD = '<%=fechaEfectosBD != null ? fechaEfectosBD : ""%>';
        fechaEfectosBD = fechaEfectosBD != "" ? getDateFormatted(fechaEfectosBD) : "";
    <% Date fechaBajaBD = registroVO.getFechaBaja(); %>
        var fechaBajaBD = '<%=fechaBajaBD != null ? fechaBajaBD : ""%>';
        fechaBajaBD = fechaBajaBD != "" ? getDateFormatted(fechaBajaBD) : "";
    <% String contratoTrabajoBD = registroVO.getContratoTrabajo(); %>
        var contratoTrabajoBD = '<%=contratoTrabajoBD != null ? contratoTrabajoBD.replace("null", "") : ""%>';
    <% String contratoTParcialBD = registroVO.getContratoTParcial(); %>
        var contratoTParcialBD = '<%= contratoTParcialBD != null ? contratoTParcialBD.replace("null", "") : ""%>';
    <% String grupoCotizacionBD = registroVO.getGrupoCotizacion(); %>
        var grupoCotizacionBD = '<%=grupoCotizacionBD != null ? grupoCotizacionBD.replace("null", "") : ""%>';
        var diasAlta = '<%=registroVO.getDiasAlta()%>';

        listaLaboralTabla[<%=indice%>] = [tipoDocumentacionBD, documentacionBD, fechaDesdeBD, fechaHastaBD,
            numeroAfiliacionLBD, fechaNacimientoBD, resumenConplTotalDiasAlta, regimenBD, codCuentaCotBD,
            provinciaBD, fechaAltaBD, fechaEfectosBD, fechaBajaBD, contratoTrabajoBD, contratoTParcialBD,
            grupoCotizacionBD, diasAlta];

    <%
                }// for
            }// if
    %>

        tabLaboral.lineas = listaLaboralTabla;
        tabLaboral.displayTabla();
    }

    function loadWSDataOnTable(respuesta) {
        console.log("loadWSDataOnTable " + respuesta.length);
        var listaLaboralTabla = new Array();
        var tabLaboral = new Tabla(true, '<%=descriptor.getDescripcion("buscar")%>', '<%=descriptor.getDescripcion("anterior")%>', '<%=descriptor.getDescripcion("siguiente")%>', '<%=descriptor.getDescripcion("mosFilasPag")%>', '<%=descriptor.getDescripcion("msgNoResultBusq")%>', '<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>', '<%=descriptor.getDescripcion("filtrDeTotal")%>', '<%=descriptor.getDescripcion("primero")%>', '<%=descriptor.getDescripcion("ultimo")%>', document.getElementById('listaLaboral'), 820);
        tabLaboral.addColumna('10', 'left', "<%= meLanbideInteropI18n.getMensaje(idiomaUsuario,"label.vidalaboral.col1")%>");
        tabLaboral.addColumna('10', 'left', "<%= meLanbideInteropI18n.getMensaje(idiomaUsuario,"label.vidalaboral.col2")%>");
        tabLaboral.addColumna('10', 'left', "<%= meLanbideInteropI18n.getMensaje(idiomaUsuario,"label.vidalaboral.col3")%>");
        tabLaboral.addColumna('10', 'left', "<%= meLanbideInteropI18n.getMensaje(idiomaUsuario,"label.vidalaboral.col4")%>");
        tabLaboral.addColumna('10', 'left', "<%= meLanbideInteropI18n.getMensaje(idiomaUsuario,"label.vidalaboral.col5")%>");
        tabLaboral.addColumna('10', 'left', "<%= meLanbideInteropI18n.getMensaje(idiomaUsuario,"label.vidalaboral.col6")%>");
        tabLaboral.addColumna('10', 'left', "<%= meLanbideInteropI18n.getMensaje(idiomaUsuario,"label.vidalaboral.col7")%>");
        tabLaboral.addColumna('10', 'left', "<%= meLanbideInteropI18n.getMensaje(idiomaUsuario,"label.vidalaboral.col8")%>");
        tabLaboral.addColumna('10', 'left', "<%= meLanbideInteropI18n.getMensaje(idiomaUsuario,"label.vidalaboral.col9")%>");
        tabLaboral.addColumna('10', 'left', "<%= meLanbideInteropI18n.getMensaje(idiomaUsuario,"label.vidalaboral.col10")%>");
        tabLaboral.addColumna('10', 'left', "<%= meLanbideInteropI18n.getMensaje(idiomaUsuario,"label.vidalaboral.col11")%>");
        tabLaboral.addColumna('10', 'left', "<%= meLanbideInteropI18n.getMensaje(idiomaUsuario,"label.vidalaboral.col12")%>");
        tabLaboral.addColumna('10', 'left', "<%= meLanbideInteropI18n.getMensaje(idiomaUsuario,"label.vidalaboral.col13")%>");
        tabLaboral.addColumna('10', 'left', "<%= meLanbideInteropI18n.getMensaje(idiomaUsuario,"label.vidalaboral.col14")%>");
        tabLaboral.addColumna('10', 'left', "<%= meLanbideInteropI18n.getMensaje(idiomaUsuario,"label.vidalaboral.col15")%>");
        tabLaboral.addColumna('10', 'left', "<%= meLanbideInteropI18n.getMensaje(idiomaUsuario,"label.vidalaboral.col16")%>");
        tabLaboral.addColumna('10', 'left', "<%= meLanbideInteropI18n.getMensaje(idiomaUsuario,"label.vidalaboral.col17")%>");
        tabLaboral.displayCabecera = true;
        tabLaboral.height = 100;

        var fechaDesde, fechaHasta, fechaNacimiento, fechaAlta, fechaEfectos, fechaBaja;
        var tipoDocumentacion, documentacion, numeroAfiliacionL, regimen, codCuentaCot;
        var provincia, contratoTrabajo, contratoTParcial, grupoCotizacion;
        for (var i = 0; i < respuesta.length; i++) {
            tipoDocumentacion = respuesta[i].tipoDocumentacion != null ? respuesta[i].tipoDocumentacion : "";
            documentacion = respuesta[i].documentacion != null ? respuesta[i].documentacion : "";
            numeroAfiliacionL = respuesta[i].numeroAfiliacionL != null ? respuesta[i].numeroAfiliacionL : "";
            regimen = respuesta[i].regimen != null ? respuesta[i].regimen : "";
            codCuentaCot = respuesta[i].codCuentaCot != null ? respuesta[i].codCuentaCot : "";
            provincia = respuesta[i].provincia != null ? respuesta[i].provincia : "";
            contratoTrabajo = respuesta[i].contratoTrabajo != null ? respuesta[i].contratoTrabajo : "";
            grupoCotizacion = respuesta[i].grupoCotizacion != null ? respuesta[i].grupoCotizacion : "";
            fechaDesde = respuesta[i].fechaDesde != null ? getDateFormattedFromLocale(respuesta[i].fechaDesde) : "";
            fechaHasta = respuesta[i].fechaHasta != null ? getDateFormattedFromLocale(respuesta[i].fechaHasta) : "";
            fechaNacimiento = respuesta[i].fechaNacimiento != null ? getDateFormattedFromLocale(respuesta[i].fechaNacimiento) : "";
            fechaAlta = respuesta[i].fechaAlta != null ? getDateFormattedFromLocale(respuesta[i].fechaAlta) : "";
            fechaEfectos = respuesta[i].fechaEfectos != null ? getDateFormattedFromLocale(respuesta[i].fechaEfectos) : "";
            fechaBaja = respuesta[i].fechaBaja != null ? getDateFormattedFromLocale(respuesta[i].fechaBaja) : "";
            contratoTParcial = respuesta[i].contratoTParcial != null ? respuesta[i].contratoTParcial : "";

            listaLaboralTabla[i] = [tipoDocumentacion, documentacion, fechaDesde,
                fechaHasta, numeroAfiliacionL,
                fechaNacimiento,
                respuesta[i].resumenConplTotalDiasAlta, regimen, codCuentaCot,
                provincia, fechaAlta, fechaEfectos,
                fechaBaja,
                contratoTrabajo, contratoTParcial,
                grupoCotizacion, respuesta[i].diasAlta];
        }

        tabLaboral.lineas = listaLaboralTabla;
        tabLaboral.displayTabla();
    }

    function getDateFormatted(date) {
        // gives you your current date
        const today = new Date(date);
        const yyyy = today.getFullYear();
        let mm = today.getMonth() + 1; // Months start at 0!
        let dd = today.getDate();

        if (dd < 10)
            dd = '0' + dd;
        if (mm < 10)
            mm = '0' + mm;

        const formattedToday = dd + '/' + mm + '/' + yyyy;

        return formattedToday;
    }

    function getDateFormattedFromLocale(date) {
        if (date.indexOf(",") == 6) {
            var day = date.substring(4, 6);
            var year = date.substring(8);
        } else {
            var day = "0" + date.substring(4, 5);
            var year = date.substring(7);
        }

        var month = date.substring(0, 3);
        if (month == "ene" || month == "urt")
            month = "01";
        else if (month == "feb" || month == "ots")
            month = "02";
        else if (month == "mar" || month == "mar")
            month = "03";
        else if (month == "abr" || month == "api")
            month = "04";
        else if (month == "may" || month == "mai")
            month = "05";
        else if (month == "jun" || month == "eka")
            month = "06";
        else if (month == "jul" || month == "uzt")
            month = "07";
        else if (month == "ago" || month == "abu")
            month = "08";
        else if (month == "sep" || month == "ira")
            month = "09";
        else if (month == "oct" || month == "urr")
            month = "10";
        else if (month == "nov" || month == "aza")
            month = "11";
        else if (month == "dic" || month == "abe")
            month = "12";

        return day + "/" + month + "/" + year;
    }

</script>

<body>
    <!--<body>-->
    <div class="tab-page" id="tabTablaVidaLaboral" style="height:90%; width: 100%;">
        <h2 class="tab" id="pestanaVidaLaboral"><%=meLanbideInteropI18n.getMensaje(idiomaUsuario,"label.vidalaboral.tituloPestana")%></h2>
        <script type="text/javascript">tp1.addTabPage(document.getElementById("tabTablaVidaLaboral"));</script>
        <div style="clear: both;">
            <label class="legendAzul" style="text-align: center;"><%=meLanbideInteropI18n.getMensaje(idiomaUsuario, "label.vidalaboral.tituloPestana")%></label>
            <div id="divGeneral"  style="overflow-y: auto; overflow-x: hidden; height: 90%;">     <!--onscroll="deshabilitarRadios();"-->
                <div id="listaLaboral" style="padding: 5px; width: 98%; height: 50%; text-align: center; overflow-x: auto; overflow-y: auto;margin:0px;margin-top:0px;" align="center"></div>
            </div>
        </div>
        <!-- Es la capa más externa de la pestaña -->
        <form  id="formBusqueda">
            <div style="clear: both;">
                <div style="text-align: left; margin-left: 50px; margin-top: 12px" class="etiqueta">
                    <label><%=meLanbideInteropI18n.getMensaje(idiomaUsuario,"label.lak.fechaDesde")%></label>
                    <input type="date" id="fechaDesde" name="fechaDesde" />
                    <script>
                        var ahora = new Date();
                        var hace1Anho = ahora.setFullYear(ahora.getFullYear() - 1);
                        document.getElementById('fechaDesde').valueAsDate = new Date(hace1Anho);
                    </script>
                    <label><%=meLanbideInteropI18n.getMensaje(idiomaUsuario,"label.lak.fechaHasta")%></label>
                    <input type="date" id="fechaHasta" name="fechaHasta" />
                    <script>
                        document.getElementById('fechaHasta').valueAsDate = new Date();
                    </script>
                </div>
            </div>

            <div style="clear: both;">
                <label><%=meLanbideInteropI18n.getMensaje(idiomaUsuario,"label.lak.entidadColaboradora.titulo")%></label>
                </br>
                <div style="text-align: left; margin-left: 50px" class="etiqueta">
                    <input type="button" id="botonDatosVidaLaboralIntermediacion" name="botonDatosVidaLaboralIntermediacion" value="<%=meLanbideInteropI18n.getMensaje(idiomaUsuario,"label.extension.boton.datosVidaLaboralIntermediacion")%>" onclick="obtenerDatosVidaLaboralIntermediacion();" >
                    </input>
                    <input type="button" id="botonLaboralExpExcel" name="botonLaboralExpExcel" value="<%=meLanbideInteropI18n.getMensaje(idiomaUsuario,"btn.Exportar")%>" onclick="pulsarExportarExcelVidaLaboral();" >
                    </input>
                </div>
            </div>
        </form>
    </div>
    <script type="text/javascript">
        loadBDDataOnTable();
    </script>
</body>

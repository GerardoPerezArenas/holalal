<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>

<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.common.service.config.Config"%>
<%@page import="es.altia.common.service.config.ConfigServiceHelper"%>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide74.i18n.MeLanbide74I18n" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide74.util.ConfigurationParameter"%>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide74.util.ConstantesMeLanbide74"%>

<%
    int idiomaUsuario = 0;
    int codOrganizacion = 0;
    UsuarioValueObject usuario = new UsuarioValueObject();
    try {
        if (session != null) {
            if (usuario != null) {
                usuario = (UsuarioValueObject) session.getAttribute("usuario");
                idiomaUsuario = usuario.getIdioma();
                codOrganizacion  = usuario.getOrgCod();
            }
        }
    }
    catch(Exception ex) {}
    
    MeLanbide74I18n meLanbide74I18n = MeLanbide74I18n.getInstance();
    String numExpediente = request.getParameter("numero");
    String codigoProcedimiento = (String)request.getAttribute("codigoProcedimiento");
    String codTramite    = (String)request.getAttribute("codigoTramite");
    String ocuTramite    = (String)request.getAttribute("ocuTramite");
    String ejercicioExp = (String)request.getAttribute("ejercicioExp");
    String valorIdDeuda = (String)request.getAttribute("valorIdDeuda");    
    Config m_Config = ConfigServiceHelper.getConfig("common");

%>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/estilo.css"/>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/extension/melanbide74/melanbide74.css"/>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/listaComboBox.js"></script>
<script type="text/javascript">
    var url = '<%=request.getContextPath()%>' + '/PeticionModuloIntegracion.do';

    var mensajeValidacionA = '';
    var idCarta = <%=valorIdDeuda%>;
    var nombreCarta;
    var interesesGenerados;
    var codProcedimiento = "<%=codigoProcedimiento%>";
    var codTramite = <%=codTramite%>;
    var numeroExpediente = "<%=numExpediente%>";
    var ejercicioExp = "<%=ejercicioExp%>";
    var ocuTramite = document.forms[0].ocurrenciaTramite.value;

    function altaCartaPago() {
        document.getElementById('mensajeError').innerHTML = "";

        if (!faltanCampos()) {
            deshabilitarBotonLargo(document.forms[0].btnAltaCarta);
            var parametrosLLamada = {
                tarea: 'preparar'
                , modulo: 'MELANBIDE74'
                , operacion: 'altaCartaPagoRenunciaNoRGI'
                , tipo: 0
                , numero: '<%=numExpediente%>'
                , control: new Date().getTime()
                , codTramite: codTramite
                , ocurrenciaTramite: ocuTramite
            };
            try {
                $.ajax({
                    url: url,
                    type: 'POST',
                    async: true,
                    data: parametrosLLamada,
                    beforeSend: function () {
                        pleaseWait('on');
                    },
                    success: function (respuesta) {
                        var datos = JSON.parse(respuesta);
                        var codigoOperacion = datos.tabla.codigoOperacion;
                        if (codigoOperacion == "0") {
                            pleaseWait('off');
                            idCarta = datos.tabla.idDeuda;
                            nombreCarta = datos.tabla.nombreCarta;
                            interesesGenerados = datos.tabla.intereses;
                        deshabilitarBotonLargo(document.forms[0].btnAltaCarta);
                        habilitarBoton(document.forms[0].cmdVisualizarCARTAPAGORENUN_1);
                        if (document.getElementById('T_' + codTramite + '_' + ocuTramite + '_IDDEUDA') != null && document.getElementById('T_' + codTramite + '_' + ocuTramite + '_IDDEUDA') != undefined) {
                            document.getElementById('T_' + codTramite + '_' + ocuTramite + '_IDDEUDA').value = idCarta;
                        }
                        if (document.getElementById('CARTAPAGORENUN_' + ocuTramite) != null && document.getElementById('CARTAPAGORENUN_' + ocuTramite) != undefined) {
                            document.getElementById('CARTAPAGORENUN_' + ocuTramite).value = nombreCarta;
                        }
                        // INTERESDEMORA
                        if (document.getElementById('T_' + codTramite + '_' + ocuTramite + '_INTERESDEMORA') != null && document.getElementById('T_' + codTramite + '_' + ocuTramite + '_INTERESDEMORA') != undefined) {
                                document.getElementById('T_' + codTramite + '_' + ocuTramite + '_INTERESDEMORA').value = interesesGenerados;
                        }
                            mensajeValidacionA = '<%=meLanbide74I18n.getMensaje(idiomaUsuario,"altaCartaRenuncia.correcto")%>';
                            document.getElementById('mensajeError').innerHTML = mensajeValidacionA;
                            jsp_alerta("A", mensajeValidacionA);
                        } else {
                            mostrarErrorPeticion(codigoOperacion);
                        }
                    },
                    error: function () {
                        mostrarErrorPeticion("1");
                    }
                });
            } catch (Err) {
                alert("ERROR " + Err);
            }

        } else {
            jsp_alerta("A", mensajeValidacionA);
        }
    } // altaCartaPago

    function faltanCampos() {
        var fechaAcept;
        var fechaPago;
        var codArea;
        var importeDeuda;

        if (document.getElementsByName('T_' + codTramite + '_' + ocuTramite + '_FECACEPRENU') != undefined) {
            fechaAcept = document.getElementsByName('T_' + codTramite + '_' + ocuTramite + '_FECACEPRENU')[0].value;
        } else {
            mensajeValidacionA = '<%=meLanbide74I18n.getMensaje(idiomaUsuario,"msg.error.faltaFechaAcept")%>';
        }

        if (document.getElementsByName('codT_' + codTramite + '_' + ocuTramite + '_CODAREA') != undefined) {
            codArea = document.getElementsByName('codT_' + codTramite + '_' + ocuTramite + '_CODAREA')[0].value;
        } else {
            mensajeValidacionA = '<%=meLanbide74I18n.getMensaje(idiomaUsuario,"msg.error.faltaCodigoArea")%>';
        }

        if (document.getElementsByName('T_' + codTramite + '_' + ocuTramite + '_IMPORTEDEUDA') != undefined) {
            importeDeuda = document.getElementsByName('T_' + codTramite + '_' + ocuTramite + '_IMPORTEDEUDA')[0].value;
        } else {
            mensajeValidacionA = '<%=meLanbide74I18n.getMensaje(idiomaUsuario,"msg.error.faltaImporteDeuda")%>';
        }

        if (document.getElementsByName('T_' + codTramite + '_' + ocuTramite + '_FECPAGOSUBV') != undefined) {
            fechaPago = document.getElementsByName('T_' + codTramite + '_' + ocuTramite + '_FECPAGOSUBV')[0].value;
        } else {
            mensajeValidacionA = '<%=meLanbide74I18n.getMensaje(idiomaUsuario,"msg.error.faltaFechaPago")%>';
        }

        if (fechaAcept == null || fechaAcept == '') {
            mensajeValidacionA = '<%=meLanbide74I18n.getMensaje(idiomaUsuario,"msg.error.faltaFechaAcept")%>';
        } else if (codArea == null || codArea == '') {
            mensajeValidacionA = '<%=meLanbide74I18n.getMensaje(idiomaUsuario,"msg.error.faltaCodigoArea")%>';
        } else if (importeDeuda == null || importeDeuda == '') {
            mensajeValidacionA = '<%=meLanbide74I18n.getMensaje(idiomaUsuario,"msg.error.faltaImporteDeuda")%>';
        } else if (fechaPago == null || fechaPago == '') {
            mensajeValidacionA = '<%=meLanbide74I18n.getMensaje(idiomaUsuario,"msg.error.faltaFechaPago")%>';
        } else {
            return false;
        }
        return true;
    }

    function mostrarErrorPeticion(codigo) {
        pleaseWait('off');
        habilitarBotonLargo(document.forms[0].btnAltaCarta);
        switch (codigo) {
            case '1':
                mensajeValidacionA = '<%=meLanbide74I18n.getMensaje(idiomaUsuario,"error.altaCartaRenuncia.1")%>';
                break;
            case '2':
                mensajeValidacionA = '<%=meLanbide74I18n.getMensaje(idiomaUsuario,"error.altaCartaRenuncia.2")%>';
                break;
            case '3':
                mensajeValidacionA = '<%=meLanbide74I18n.getMensaje(idiomaUsuario,"error.altaCartaRenuncia.3")%>';
                break;
            case '4':
                mensajeValidacionA = '<%=meLanbide74I18n.getMensaje(idiomaUsuario,"error.altaCartaRenuncia.4")%>';
                break;
            case '5':
                mensajeValidacionA = '<%=meLanbide74I18n.getMensaje(idiomaUsuario,"error.altaCartaRenuncia.5")%>';
                break;
            case '6':
                mensajeValidacionA = '<%=meLanbide74I18n.getMensaje(idiomaUsuario,"error.altaCartaRenuncia.6")%>';
                break;
            case '7':
                mensajeValidacionA = '<%=meLanbide74I18n.getMensaje(idiomaUsuario,"error.altaCartaRenuncia.7")%>';
                break;
            case '8':
                mensajeValidacionA = '<%=meLanbide74I18n.getMensaje(idiomaUsuario,"error.altaCartaRenuncia.8")%>';
                break;
            case '9':
                mensajeValidacionA = '<%=meLanbide74I18n.getMensaje(idiomaUsuario,"error.altaCartaRenuncia.9")%>';
                break;
            case '10':
                mensajeValidacionA = '<%=meLanbide74I18n.getMensaje(idiomaUsuario,"error.altaCartaRenuncia.10")%>';
                break;
            case '11':
                mensajeValidacionA = '<%=meLanbide74I18n.getMensaje(idiomaUsuario,"error.altaCartaRenuncia.11")%>';
                break;
            case '12':
                mensajeValidacionA = '<%=meLanbide74I18n.getMensaje(idiomaUsuario,"error.altaCartaRenuncia.12")%>';
                break;
            case '13':
                mensajeValidacionA = '<%=meLanbide74I18n.getMensaje(idiomaUsuario,"error.altaCartaRenuncia.13")%>';
                break;
            case '14':
                mensajeValidacionA = '<%=meLanbide74I18n.getMensaje(idiomaUsuario,"error.altaCartaRenuncia.14")%>';
                break;
            case '15':
                mensajeValidacionA = '<%=meLanbide74I18n.getMensaje(idiomaUsuario,"error.altaCartaRenuncia.15")%>';
                break;
            case '16':
                mensajeValidacionA = '<%=meLanbide74I18n.getMensaje(idiomaUsuario,"error.altaCartaRenuncia.16")%>';
                break;
            case '17':
                mensajeValidacionA = '<%=meLanbide74I18n.getMensaje(idiomaUsuario,"error.altaCartaRenuncia.17")%>';
                break;
            default:
                mensajeValidacionA = codigo;
                break;
        }
        document.getElementById('mensajeError').innerHTML = mensajeValidacionA;
        jsp_alerta("A", mensajeValidacionA);
    }
</script>
<body class="bandaBody" onload="javascript:{
            pleaseWait('off');
        }" >
<div class="tab-page" id="tabPage741" style="width: 100%;">
    <h2 class="tab" id="pestana741"><%=meLanbide74I18n.getMensaje(idiomaUsuario,"label.tituloPestana")%></h2>
    <script type="text/javascript">tp1_p741 = tp1.addTabPage(document.getElementById("tabPage741"));</script>
        <h2 class="sub3titulo" id="pestanaPrinc"><%=meLanbide74I18n.getMensaje(idiomaUsuario,"label.tituloPrincipal")%></h2>
    <div style="clear: both;">           
        <div class="botonera" style="padding-top: 20px; padding-bottom: 20px; text-align: center;">  
            <input type="button" id="btnAltaCarta" name="btnAltaCarta" class="botonMasLargo" value="<%=meLanbide74I18n.getMensaje(idiomaUsuario,"boton.btnAltaCarta")%>" onclick="altaCartaPago();
                    return false;">                  
        </div>
    </div>    
        <div style="width: 100%; padding: 10px; align-content: center; text-align: center;">
            <label id="mensajeError" name="mensajeError" class="legendRojo"></label>
        </div>
</div>
<script type="text/javascript">
    if (document.getElementById('T_' + codTramite + '_' + ocuTramite + '_IDDEUDA').value != null && document.getElementById('T_' + codTramite + '_' + ocuTramite + '_IDDEUDA').value != '') {
        deshabilitarBotonLargo(document.forms[0].btnAltaCarta);
    }
</script>
</body>
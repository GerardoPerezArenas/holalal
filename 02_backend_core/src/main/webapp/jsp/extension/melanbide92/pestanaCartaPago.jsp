<%-- 
    Document   : pestana5001
    Created on : 08-feb-2023, 11:46:41
    Author     : kepa
--%>
<%@taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide92.i18n.MeLanbide92I18n" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide92.util.ConfigurationParameter" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide92.util.ConstantesMeLanbide92" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.common.service.config.Config"%>
<%@page import="es.altia.common.service.config.ConfigServiceHelper"%>
<head>
    <%
        int idiomaUsuario = 0;
        int codOrganizacion = 0;
        int apl = 5;
        String css = "";
        UsuarioValueObject usuario = new UsuarioValueObject();
        try {
            if (session != null){
                if (usuario != null) {
                    usuario = (UsuarioValueObject) session.getAttribute("usuario");
                    idiomaUsuario = usuario.getIdioma();
                    apl = usuario.getAppCod();
                    css = usuario.getCss();
                    codOrganizacion  = usuario.getOrgCod();
                }    
            }
        } 
        catch(Exception ex) {}
    
        MeLanbide92I18n meLanbide92I18n = MeLanbide92I18n.getInstance();  
        String numExpediente = request.getParameter("numero");
        String codTramite = request.getParameter("codigoTramite");
        String ocuTramite = request.getParameter("ocuTramite");
        Integer fase = (Integer)request.getAttribute("fase");
        String idDeudaIni = (String)request.getAttribute("idDeudaIni");
        String idDeudaRes = (String)request.getAttribute("idDeudaRes");
        String idDeudaSus = (String)request.getAttribute("idDeudaSus");
    %>
    <jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
    <jsp:setProperty name="descriptor"  property="idi_cod" value="<%=idiomaUsuario%>" />
    <jsp:setProperty name="descriptor"  property="apl_cod" value="<%=apl%>" />
    <link rel="StyleSheet" media="screen" type="text/css" href="<%=request.getContextPath()%><%=css%>"/>
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/extension/melanbide92/melanbide92.css"/>
    <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/popup.js"></script>
    <meta http-equiv="X-UA-Compatible" content="IE=Edge" />
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <script type="text/javascript">
        var url = '<%=request.getContextPath()%>' + '/PeticionModuloIntegracion.do';
        var mensajeValidacion = '';
        var codTram = '<%=codTramite%>';
        var ocuTram = '<%=ocuTramite%>';
        var fase = <%=fase%>;
        var vIdDeudaIni = <%=idDeudaIni%>;
        var vIdDeudaRes = <%=idDeudaRes%>;
        var vIdDeudaSus = <%=idDeudaSus%>;

        var parametrosLLamada = {
            tarea: 'preparar'
            , modulo: 'MELANBIDE92'
            , operacion: null
            , tipo: 0
            , control: new Date().getTime()
            , numExp: '<%=numExpediente%>'
            , codTramite: codTram
            , ocuTramite: ocuTram
            , fase: fase
            , importeDeudaIni: null
            , importeDeudaRes: null
            , importeDeudaSus: null
            , fechaResolucion: null
            , fechaActivacion: null
        };

        function existeDeuda() {
            var existe = true;
            switch (fase) {
                case 1:
                    if (vIdDeudaIni == null || vIdDeudaIni == '') {
                        existe = false;
                    } else {
                        mensajeValidacion = '<%=meLanbide92I18n.getMensaje(idiomaUsuario,"error.existeIdDeudaIni")%>';
                    }
                    break;
                case 2:
                    if (vIdDeudaRes == null || vIdDeudaRes == '') {
                        existe = false;
                    } else {
                        mensajeValidacion = '<%=meLanbide92I18n.getMensaje(idiomaUsuario,"error.existeIdDeudaRes")%>';
                    }
                    break;
                case 3:
                    if (vIdDeudaSus == null || vIdDeudaSus == '') {
                        existe = false;
                    } else {
                        mensajeValidacion = '<%=meLanbide92I18n.getMensaje(idiomaUsuario,"error.existeIdDeudaSusp")%>';
                    }
                    break;
                default:
                    mensajeValidacion = '<%=meLanbide92I18n.getMensaje(idiomaUsuario,"error.zorku.13")%>';// revisar error
                    //   return false;
                    break;
            }
            return existe;
        }

        function altaCartaPago() {
            document.getElementById('mensajeError').innerHTML = "";
            if (!existeDeuda()) {
                var dataParameter = $.extend({}, parametrosLLamada);
                var llamar = false;
                switch (fase) {
                    case 1:
                        var valorImporteDeudaIni = '';
                        valorImporteDeudaIni = document.getElementsByName('T_' + codTram + '_' + ocuTram + '_IMPORTEDEUDAINI')[0].value;
                        if (valorImporteDeudaIni != '') {
                            dataParameter.importeDeudaIni = valorImporteDeudaIni;
                            llamar = true;
                        }
                        break;
                    case 2:
                        var valorImporteDeudaRes = '';
                        var valorFechaResolucion = '';
                        valorImporteDeudaRes = document.getElementsByName('T_' + codTram + '_' + ocuTram + '_IMPORTEDEUDARES')[0].value;
                        valorFechaResolucion = document.getElementsByName('T_' + codTram + '_' + ocuTram + '_FECRESOLUCION')[0].value;
                        if (valorImporteDeudaRes != '' && valorFechaResolucion != '') {
                            dataParameter.importeDeudaRes = valorImporteDeudaRes;
                            dataParameter.fechaResolucion = valorFechaResolucion;
                            llamar = true;
                        }
                        break;
                    case 3:
                        var valorImporteDeudaSus = '';
                        var valorFechaActivacion = '';
                        valorImporteDeudaSus = document.getElementsByName('T_' + codTram + '_' + ocuTram + '_IMPORTEDEUDASUSP')[0].value;
                        valorFechaActivacion = document.getElementsByName('T_' + codTram + '_' + ocuTram + '_FECACT')[0].value;
                        if (valorImporteDeudaSus != '' && valorFechaActivacion != '') {
                            dataParameter.importeDeudaSus = valorImporteDeudaSus;
                            dataParameter.fechaActivacion = valorFechaActivacion;
                            llamar = true;
                        }
                        break;
                    default:
                        jsp_alerta("A", '<%=meLanbide92I18n.getMensaje(idiomaUsuario,"error.zorku.13")%>');
                        break;
                }

                if (llamar) {
                    dataParameter.operacion = 'validarCampos';
                    try {
                        $.ajax({
                            url: url,
                            type: 'POST',
                            async: true,
                            data: dataParameter,
                            beforeSend: function () {
                                pleaseWait('on');
                            },
                            success: procesarRespuestaValidar,
                            error: mostrarErrorValidar
                        });
                    } catch (Err) {
                        mostrarErrorPeticion();
                    }
                } else {
                    var llamar = false;
                    jsp_alerta("A", '<%=meLanbide92I18n.getMensaje(idiomaUsuario,"error.camposVacios")%>');
                }

            } else {
                jsp_alerta("A", mensajeValidacion);
            }
        }

        function procesarRespuestaValidar(ajaxResult) {
            var dataParameter = $.extend({}, parametrosLLamada);
            var datos = JSON.parse(ajaxResult);
            var codigoOperacion = datos.tabla.codigoOperacion;
            if (codigoOperacion == '0') {
                dataParameter.operacion = 'altaDeudaSubvenciones';
                try {
                    $.ajax({
                        url: url,
                        type: 'POST',
                        async: true,
                        data: dataParameter,
                        success: procesarRespuestaWS,
                        error: mostrarErrorWS
                    });
                } catch (Err) {
                    mostrarErrorPeticion();
                }
            } else {
                mostrarErrorPeticion(codigoOperacion);
            }
        }

        function procesarRespuestaWS(ajaxResult) {
            pleaseWait('off');
            var datos = JSON.parse(ajaxResult);
            var codigoOperacion = datos.tabla.codigoOperacion;
            if (codigoOperacion == '0') {
                var msgtitle = '<%=meLanbide92I18n.getMensaje(idiomaUsuario,"error.tituloOK")%>';
                switch (fase) {
                    case 1:
                        vIdDeudaIni = datos.tabla.idDeuda;
                        break;
                    case 2:
                        vIdDeudaRes = datos.tabla.idDeuda;
                        break;
                    case 3:
                        vIdDeudaSusp = datos.tabla.idDeuda;
                        break;
                    default:

                        break;
                }
                jsp_alerta("A", '<%=meLanbide92I18n.getMensaje(idiomaUsuario,"altDeuda.correcto")%>');
                deshabilitarBotonLargo(document.forms[0].btnAltaCarta);
            } else {
                mostrarErrorPeticion(codigoOperacion);
            }
        }

        function mostrarErrorValidar() {
            pleaseWait('off');
            var msgtitle = '<%=meLanbide92I18n.getMensaje(idiomaUsuario,"error.tituloKO")%>';
            jsp_alerta("A", '<%=meLanbide92I18n.getMensaje(idiomaUsuario,"error.operacionIncorrecta")%>', msgtitle);// revisar cod error
        }

        function mostrarErrorWS() {
            pleaseWait('off');
            var msgtitle = '<%=meLanbide92I18n.getMensaje(idiomaUsuario,"error.tituloKO")%>';
            jsp_alerta("A", '<%=meLanbide92I18n.getMensaje(idiomaUsuario,"error.operacionIncorrecta")%>', msgtitle);
        }

        function mostrarErrorPeticion(codigo) {
            pleaseWait('off');
            var msgtitle = '<%=meLanbide92I18n.getMensaje(idiomaUsuario,"error.tituloKO")%>';
            switch (codigo) {
                case '1':
                    mensajeValidacion = '<%=meLanbide92I18n.getMensaje(idiomaUsuario,"error.zorku.1")%>';
                    break;
                case '2':
                    mensajeValidacion = '<%=meLanbide92I18n.getMensaje(idiomaUsuario,"error.zorku.2")%>';
                    break;
                case '3':
                    mensajeValidacion = '<%=meLanbide92I18n.getMensaje(idiomaUsuario,"error.zorku.3")%>';
                    break;
                case '4':
                    mensajeValidacion = '<%=meLanbide92I18n.getMensaje(idiomaUsuario,"error.zorku.4")%>';
                    break;
                case '5':
                    mensajeValidacion = '<%=meLanbide92I18n.getMensaje(idiomaUsuario,"error.zorku.5")%>';
                    break;
                case '6':
                    mensajeValidacion = '<%=meLanbide92I18n.getMensaje(idiomaUsuario,"error.zorku.6")%>';
                    break;
                case '7':
                    mensajeValidacion = '<%=meLanbide92I18n.getMensaje(idiomaUsuario,"error.zorku.7")%>';
                    break;
                case '8':
                    mensajeValidacion = '<%=meLanbide92I18n.getMensaje(idiomaUsuario,"error.zorku.8")%>';
                    break;
                case '9':
                    mensajeValidacion = '<%=meLanbide92I18n.getMensaje(idiomaUsuario,"error.zorku.9")%>';
                    break;
                case '10':
                    mensajeValidacion = '<%=meLanbide92I18n.getMensaje(idiomaUsuario,"error.zorku.10")%>';
                    break;
                case '11':
                    mensajeValidacion = '<%=meLanbide92I18n.getMensaje(idiomaUsuario,"error.zorku.11")%>';
                    break;
                case '12':
                    mensajeValidacion = '<%=meLanbide92I18n.getMensaje(idiomaUsuario,"error.zorku.12")%>';
                    break;
                case '13':
                    mensajeValidacion = '<%=meLanbide92I18n.getMensaje(idiomaUsuario,"error.zorku.13")%>';
                    break;
                case '14':
                    mensajeValidacion = '<%=meLanbide92I18n.getMensaje(idiomaUsuario,"error.zorku.14")%>';
                    break;
                case '15':
                    mensajeValidacion = '<%=meLanbide92I18n.getMensaje(idiomaUsuario,"error.zorku.15")%>';
                    break;
                case '16':
                    mensajeValidacion = '<%=meLanbide92I18n.getMensaje(idiomaUsuario,"error.zorku.16")%>';
                    break;
                case '17':
                    mensajeValidacion = '<%=meLanbide92I18n.getMensaje(idiomaUsuario,"error.zorku.17")%>';
                    break;
                case '18':
                    mensajeValidacion = '<%=meLanbide92I18n.getMensaje(idiomaUsuario,"error.zorku.18")%>';
                    break;
                case '19':
                    mensajeValidacion = '<%=meLanbide92I18n.getMensaje(idiomaUsuario,"error.zorku.19")%>';
                    break;
                case '20':
                    mensajeValidacion = '<%=meLanbide92I18n.getMensaje(idiomaUsuario,"error.zorku.20")%>';
                    break;
                case '21':
                    mensajeValidacion = '<%=meLanbide92I18n.getMensaje(idiomaUsuario,"error.zorku.21")%>';
                    break;
                case '22':
                    mensajeValidacion = '<%=meLanbide92I18n.getMensaje(idiomaUsuario,"error.zorku.22")%>';
                    break;
                case '23':
                    mensajeValidacion = '<%=meLanbide92I18n.getMensaje(idiomaUsuario,"error.zorku.23")%>';
                    break;
                case '24':
                    mensajeValidacion = '<%=meLanbide92I18n.getMensaje(idiomaUsuario,"error.zorku.24")%>';
                    break;
                case '25':
                    mensajeValidacion = '<%=meLanbide92I18n.getMensaje(idiomaUsuario,"error.zorku.25")%>';
                    break;
                case '26':
                    mensajeValidacion = '<%=meLanbide92I18n.getMensaje(idiomaUsuario,"error.zorku.26")%>';
                    break;
                case '27':
                    mensajeValidacion = '<%=meLanbide92I18n.getMensaje(idiomaUsuario,"error.zorku.27")%>';
                    break;
                case '28':
                    mensajeValidacion = '<%=meLanbide92I18n.getMensaje(idiomaUsuario,"error.zorku.28")%>';
                    break;
                case '29':
                    mensajeValidacion = '<%=meLanbide92I18n.getMensaje(idiomaUsuario,"error.zorku.29")%>';
                    break;
                case '30':
                    mensajeValidacion = '<%=meLanbide92I18n.getMensaje(idiomaUsuario,"error.zorku.30")%>';
                    break;
                case '31':
                    mensajeValidacion = '<%=meLanbide92I18n.getMensaje(idiomaUsuario,"error.zorku.31")%>';
                    break;
                case '99':
                    mensajeValidacion = '<%=meLanbide92I18n.getMensaje(idiomaUsuario,"error.errorGen")%>';
                    break;
                case '400':
                    mensajeValidacion = '<%=meLanbide92I18n.getMensaje(idiomaUsuario,"error.400")%>';
                    break;
                case '404':
                    mensajeValidacion = '<%=meLanbide92I18n.getMensaje(idiomaUsuario,"error.404")%>';
                    break;
                case '500':
                    mensajeValidacion = '<%=meLanbide92I18n.getMensaje(idiomaUsuario,"error.500")%>';
                    break;
                case '503':
                    mensajeValidacion = '<%=meLanbide92I18n.getMensaje(idiomaUsuario,"error.503")%>';
                    break;
                default :
                    mensajeValidacion = codigo;
                    break;
            }
            jsp_alerta("A", mensajeValidacion, msgtitle);
            document.getElementById('mensajeError').innerHTML = mensajeValidacion;
        }

    </script>
</head>
<body class="bandaBody" onload="javascript:{
            pleaseWait('off');
        }" >
    <div class="tab-page" id="tabPage921" style="width: 100%;">
        <h2 class="tab" id="pestana921"><%=meLanbide92I18n.getMensaje(idiomaUsuario,"label.tituloPestana")%></h2>
        <script type="text/javascript">tp1_p921 = tp1.addTabPage(document.getElementById("tabPage921"));</script>
        <h2 class="sub3titulo" id="pestanaPrinc"><%=meLanbide92I18n.getMensaje(idiomaUsuario,"label.tituloPrincipal")%></h2>

        <div style="clear: both;">           
            <div class="botonera" style="padding-top: 20px; padding-bottom: 20px; text-align: center;">  
                <input type="button" id="btnAltaCarta" name="btnAltaCarta" class="botonMasLargo" value="<%=meLanbide92I18n.getMensaje(idiomaUsuario,"btn.generar")%>" onclick="altaCartaPago();
                        return false;">                  
            </div>
        </div>
        <div style="width: 100%; padding: 10px; align-content: center; text-align: center;">
            <label id="mensajeError" name="mensajeError" class="legendRojo"></label>
        </div>
    </div>
    <script type="text/javascript">
        if (existeDeuda()) {
            deshabilitarBotonLargo(document.forms[0].btnAltaCarta);
            document.getElementById('mensajeError').innerHTML = mensajeValidacion;
        }
    </script>

</body>
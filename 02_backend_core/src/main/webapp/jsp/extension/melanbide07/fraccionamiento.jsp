<%-- 
    Document   : fraccionamiento
    Created on : 18-jun-2025, 10:25:43
    Author     : kigonzalez
--%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@taglib uri="/WEB-INF/tlds/c.tld" prefix="c"%>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide07.i18n.MeLanbide07I18n"%>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide07.util.ConfigurationParameter"%>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide07.util.ConstantesMeLanbide07"%>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject"%>
<html>
    <%
        int idiomaUsuario = 0;
        UsuarioValueObject usuario = new UsuarioValueObject();
        try {
            if (session != null){
                if (usuario != null) {
                    usuario = (UsuarioValueObject) session.getAttribute("usuario");
                    idiomaUsuario = usuario.getIdioma();                  
                }    
            }
        } 
        catch(Exception ex) {}
        
        MeLanbide07I18n meLanbide07I18n = MeLanbide07I18n.getInstance();
        String numExpediente = request.getParameter("numero");
    %>
    <head>
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/estilo.css"/>
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/extension/melanbide07/melanbide07.css"/>
        <meta http-equiv="X-UA-Compatible" content="IE=Edge" />
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
        <script type="text/javascript">
            var mensajeValidacion = '';
            function altaFraccionamiento() {
                deshabilitarBotonLargo(document.forms[0].btnFraccionarDeuda);
                document.getElementById('mensajeErrorFracc').innerHTML = "";
                var url = '<%=request.getContextPath()%>' + '/PeticionModuloIntegracion.do';

                var parametrosLlamada = {
                    tarea: 'preparar'
                    , modulo: 'MELANBIDE07'
                    , operacion: 'altaFraccionamientoSubvenciones'
                    , tipo: 0
                    , numero: '<%=numExpediente%>'
                };
                try {
                    $.ajax({
                        url: url,
                        type: 'POST',
                        async: true,
                        data: parametrosLlamada,
                        beforeSend: function () {
                            pleaseWait('on');
                        },
                        success: function (respuesta) {
                                            pleaseWait('off');
                            var datos = JSON.parse(respuesta);
                            var codigoOperacion = datos.tabla.codigoOperacion;
                            if (codigoOperacion == "0") {
                                jsp_alerta("A", '<%=meLanbide07I18n.getMensaje(idiomaUsuario,"msg.fraccionarOK")%>');
                            } else {
                                mostrarErrorFraccionamiento(codigoOperacion);
                            }
                        },
                        error: function () {
                            mostrarErrorFraccionamiento("-1");
                        }
                    });
                } catch (Err) {
                    mostrarErrorFraccionamiento("-1");
                }
            }

            function mostrarErrorFraccionamiento(codigo) {
                pleaseWait('off');
                habilitarBotonLargo(document.forms[0].btnFraccionarDeuda);
                var msgtitle = '<%=meLanbide07I18n.getMensaje(idiomaUsuario,"error.tituloKO")%>';
                switch (codigo) {
                    case '-1':
                        mensajeValidacion = '<%=meLanbide07I18n.getMensaje(idiomaUsuario,"error.errorGen")%>';
                        break;
                    case '1':
                        mensajeValidacion = '<%=meLanbide07I18n.getMensaje(idiomaUsuario,"error.zorku.1")%>';
                        break;
                    case '4':
                        mensajeValidacion = '<%=meLanbide07I18n.getMensaje(idiomaUsuario,"error.zorku.4")%>';
                        break;
                    case '15':
                        mensajeValidacion = '<%=meLanbide07I18n.getMensaje(idiomaUsuario,"error.zorku.15")%>';
                        break;
                    case '26':
                        mensajeValidacion = '<%=meLanbide07I18n.getMensaje(idiomaUsuario,"error.zorku.26")%>';
                        break;
                    case '27':
                        mensajeValidacion = '<%=meLanbide07I18n.getMensaje(idiomaUsuario,"error.zorku.27")%>';
                        break;
                    case '28':
                        mensajeValidacion = '<%=meLanbide07I18n.getMensaje(idiomaUsuario,"error.zorku.28")%>';
                        break;
                    case '29':
                        mensajeValidacion = '<%=meLanbide07I18n.getMensaje(idiomaUsuario,"error.zorku.29")%>';
                        break;
                    case '30':
                        mensajeValidacion = '<%=meLanbide07I18n.getMensaje(idiomaUsuario,"error.zorku.30")%>';
                        break;
                    case '34':
                        mensajeValidacion = '<%=meLanbide07I18n.getMensaje(idiomaUsuario,"error.zorku.34")%>';
                        break;
                    case '35':
                        mensajeValidacion = '<%=meLanbide07I18n.getMensaje(idiomaUsuario,"error.zorku.35")%>';
                        break;
                    case '36':
                        mensajeValidacion = '<%=meLanbide07I18n.getMensaje(idiomaUsuario,"error.zorku.36")%>';
                        break;
                    case '37':
                        mensajeValidacion = '<%=meLanbide07I18n.getMensaje(idiomaUsuario,"error.zorku.37")%>';
                        break;
                    case '38':
                        mensajeValidacion = '<%=meLanbide07I18n.getMensaje(idiomaUsuario,"error.zorku.38")%>';
                        break;
                    case '404':
                        mensajeValidacion = '<%=meLanbide07I18n.getMensaje(idiomaUsuario,"error.zorku.404")%>';
                        break;
                    default:
                        mensajeValidacion = codigo;
                        break;
                }
                document.getElementById('mensajeErrorFracc').innerHTML = mensajeValidacion;
                jsp_alerta("A", mensajeValidacion, msgtitle);
            }
        </script>
    </head>
    <body class="bandaBody"onload="javascript:{
                pleaseWait('off');
            }" >
        <div class="tab-page" id="tabPage072" style="height:520px;width: 100%;">
            <h2 class="tab" id="pestana072"><%=meLanbide07I18n.getMensaje(idiomaUsuario,"label.tituloPestanaFracc")%></h2>
            <script type="text/javascript">tp1_p072 = tp1.addTabPage(document.getElementById("tabPage072"));</script>
            <h2 class="sub3titulo" id="pestanaPrinc"><%=meLanbide07I18n.getMensaje(idiomaUsuario,"label.tituloFraccionamiento")%></h2>
            <div style="clear: both;">
                <div class="botonera" style="padding-top: 20px; padding-bottom: 20px; text-align: center;">
                    <input type="button" id="btnFraccionarDeuda" name="btnFraccionarDeuda" class="botonMasLargo" value="<%=meLanbide07I18n.getMensaje(idiomaUsuario,"btn.fraccionar")%>" onclick="altaFraccionamiento();"/>  
                </div>
            </div>
            <div style="width: 100%; padding: 10px; align-content: center; text-align: center;">
                <label id="mensajeErrorFracc" name="mensajeErrorFracc" class="legendRojo"></label>
            </div>
        </div>
    </body>
</html>
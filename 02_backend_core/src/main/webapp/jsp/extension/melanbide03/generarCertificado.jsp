<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>

<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide03.i18n.MeLanbide03I18n" %>
<%@page import="java.util.List" %>
<html>
    <head>
        <meta http-equiv="X-UA-Compatible" content="IE=Edge" />
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <%
            int idiomaUsuario = 0;
            int codOrganizacion = 0;
            UsuarioValueObject usuario = new UsuarioValueObject();
            try
            {
                if (session != null) 
                {
                    if (usuario != null) 
                    {
                        usuario = (UsuarioValueObject) session.getAttribute("usuario");
                        idiomaUsuario = usuario.getIdioma();
                        codOrganizacion  = usuario.getOrgCod();
                    }
                }
            }
            catch(Exception ex)
            {
        
            }
            MeLanbide03I18n meLanbide03I18n = MeLanbide03I18n.getInstance();
            String numExp    = request.getParameter("numExp");
        %>
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/estilo.css"/>
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/extension/melanbide03/melanbide03.css"/>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/general.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/popup.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/TablaNueva.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/validaciones.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/listaComboBox.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/melanbide37/ecaUtils.js"></script>
        <script type="text/javascript">
            var APP_CONTEXT_PATH = '<%=request.getContextPath()%>';
            var msgValidacion = '';

            function getXMLHttpRequest() {
                var aVersions = ["MSXML2.XMLHttp.5.0",
                    "MSXML2.XMLHttp.4.0", "MSXML2.XMLHttp.3.0",
                    "MSXML2.XMLHttp", "Microsoft.XMLHttp"
                ];

                if (window.XMLHttpRequest) {
                    // para IE7, Mozilla, Safari, etc: que usen el objeto nativo
                    return new XMLHttpRequest();
                } else if (window.ActiveXObject) {
                    // de lo contrario utilizar el control ActiveX para IE5.x y IE6.x
                    for (var i = 0; i < aVersions.length; i++) {
                        try {
                            var oXmlHttp = new ActiveXObject(aVersions[i]);
                            return oXmlHttp;
                        } catch (error) {
                            //no necesitamos hacer nada especial
                        }
                    }
                } else {
                    return null;
                }
            }

            function inicializar() {
                window.focus();
            }

            function ejecutarListadoCP() {
                var control = new Date();

                if (navigator.appName.indexOf("Internet Explorer") != -1) {
                    lanzarPopUpModal('<%=request.getContextPath()%>/PeticionModuloIntegracion.do?tarea=preparar&modulo=MELANBIDE03&operacion=cargarCriteriosFiltroListadoCP_APA&tipo=0&numExp=<%=numExp%>&control=' + control.getTime(), 330, 840, 'no', 'no', function (result) {
                        if (result != undefined) {
                            if (result[0] == '0') {
                                //recargarCriteriosListadoCP(result);
                                var fila;
                                for (var i = 1; i < result.length; i++) {
                                    fila = result[i];
                                    var numExp = fila[0];
                                    //alert(tipoacred);

                                }//for

                                var url = APP_CONTEXT_PATH + "/PeticionModuloIntegracion.do";
                                var parametros = "";
                                var control = new Date();
                                parametros = '?tarea=preparar&modulo=MELANBIDE03&operacion=generarPDF_CPAPA&tipo=0&numExp=' + numExp;
                                window.open(url + parametros, "_blank");
                            }
                        }
                    });
                } else {
                    lanzarPopUpModal('<%=request.getContextPath()%>/PeticionModuloIntegracion.do?tarea=preparar&modulo=MELANBIDE03&operacion=cargarCriteriosFiltroListadoCP_APA&tipo=0&numExp=<%=numExp%>&control=' + control.getTime(), 400, 870, 'no', 'no', function (result) {
                        if (result != undefined) {
                            if (result[0] == '0') {
                                //recargarCriteriosListadoCP(result);
                                var fila;
                                for (var i = 1; i < result.length; i++) {
                                    fila = result[i];
                                    var numExp = fila[0];
                                    //alert(tipoacred);

                                }//for

                                var url = APP_CONTEXT_PATH + "/PeticionModuloIntegracion.do";
                                var parametros = "";
                                var control = new Date();
                                parametros = '?tarea=preparar&modulo=MELANBIDE03&operacion=generarPDF_CPAPA&tipo=0&numExp=' + numExp;
                                window.open(url + parametros, "_blank");
                            }
                        }
                    });
                }
            }

        </script>
    </head>
    <body class="bandaBody" onload="javascript:{
                inicializar();
            }">
        <form id="formProcesos">
            <div style="height:550px; width: 100%;">
                <table width="100%" style="height: 550px;" cellpadding="0px" cellspacing="0px" border="0px">
                    <tr>
                        <td style="width:100%;  height:30px; padding-left: 15px" class="txttitblanco"><%=meLanbide03I18n.getMensaje(idiomaUsuario, "proc.label.tit_certificadoPos")%></td>
                    </tr>               
                    <tr>
                        <td class="contenidoPantalla" valign="top" style="padding-top: 5px; padding-bottom: 10px;">
                            <div id="contenidoProc" class="cuadroFondoBlanco" style="width:970px; height: 550px; overflow-x: auto; overflow-y: auto; padding: 10px;margin:0px;margin-top:0px;" align="center">
                                <fieldset style="width: 47%; float: left; padding-left: 10px; padding-right: 10px;">
                                    <legend class="legendAzul" align="center"><!--%=meLanbide03I18n.getMensaje(idiomaUsuario, "proc.legend.certificado")%--></legend>
                                    <div class="lineaFormulario">
                                        <div style="width: 300px; float: left; text-align: left;">
                                            <%=meLanbide03I18n.getMensaje(idiomaUsuario, "proc.legend.certificado")%>
                                        </div>
                                        <div style="width: 100px; float: left;">
                                            <div style="float: left;">
                                                <input type="button" id="btnListadoCP" name="btnListadoCP" class="botonGeneral" value="<%=meLanbide03I18n.getMensaje(idiomaUsuario, "proc.btn.generar")%>" onclick="ejecutarListadoCP();">
                                            </div>
                                        </div>
                                    </div>

                                </fieldset>
                            </div>
                        </td>
                    </tr>
                </table>
            </div>
        </form>
    </body>
</html>
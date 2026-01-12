<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide07.i18n.MeLanbide07I18n" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide07.util.ConfigurationParameter" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide07.util.ConstantesMeLanbide07" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="java.util.*" %>
<%@page import="java.math.*" %>
<%@page import="java.text.*"%>
<%
    int idiomaUsuario = 1;
    int codOrganizacion = 0;
    UsuarioValueObject usuario = new UsuarioValueObject();
    try {
        if (session != null){
            if (usuario != null) {
                usuario = (UsuarioValueObject) session.getAttribute("usuario");
                idiomaUsuario = usuario.getIdioma();
                codOrganizacion  = usuario.getOrgCod();
            }
        }
    } catch(Exception ex) {}
    
    MeLanbide07I18n meLanbide07I18n = MeLanbide07I18n.getInstance();  
    String numExpediente = request.getParameter("numero");
    String codTramite = request.getParameter("codigoTramite");
    String ocuTramite = request.getParameter("ocuTramite");
    String codTramiteReqReintegro = (String)request.getAttribute("codigoTramiteReqReintegro");    
    String codTramiteResolucion  = (String)request.getAttribute("codigoTramiteResolucion");
    String codTramiteSuspension  = (String)request.getAttribute("codigoTramiteSuspension");
    String valorIdDeuda = (String)request.getAttribute("valorIdDeuda");    
    String valorIdDeudaIni = (String)request.getAttribute("valorIdDeudaIni");   
    String valorIdDeudaSusp = (String)request.getAttribute("valorIdDeudaSusp");
    String deudaPagada = (String)request.getAttribute("deudaPagada");
%>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/estilo.css"/>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/extension/melanbide07/melanbide07.css"/>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
<script type="text/javascript">
    var url = '<%=request.getContextPath()%>' + '/PeticionModuloIntegracion.do';
    var mensajeValidacionA = '';

    var vIdDeuda = "<%=valorIdDeuda%>";
    var vIdDeudaIni = "<%=valorIdDeudaIni%>";
    var vIdDeudaSusp = "<%=valorIdDeudaSusp%>";

    var codTram = "<%=codTramite%>";
    var ocuTramite = '<%=ocuTramite%>';
    var codTramReqReintegro = "<%=codTramiteReqReintegro%>";
    var codTramResolucion = "<%=codTramiteResolucion%>";
    var codTramSuspension = "<%=codTramiteSuspension%>";
    var deuPagada = "<%=deudaPagada%>";

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

    function existeDeuda() {
        if (codTram == codTramReqReintegro) {
            if (vIdDeudaIni == null || vIdDeudaIni == '') {
                return false;
            } else
                mensajeValidacionA = '<%=meLanbide07I18n.getMensaje(idiomaUsuario,"msg.error.existeIdDeudaIni")%>';
        } else if (codTram == codTramResolucion) {
            if (vIdDeuda == null || vIdDeuda == '') {
                return false;
            } else
                mensajeValidacionA = '<%=meLanbide07I18n.getMensaje(idiomaUsuario,"msg.error.existeIdDeuda")%>';
        } else if (codTram == codTramSuspension) {
            if (vIdDeudaSusp == null || vIdDeudaSusp == '') {
                return false;
            } else
                mensajeValidacionA = '<%=meLanbide07I18n.getMensaje(idiomaUsuario,"msg.error.existeIdDeudaSusp")%>';
        }
        return true;
    }

    function altaCartaPago() {
      deshabilitarBotonLargo(document.forms[0].btnAltaCarta);
      document.getElementById('mensajeError').innerHTML = "";

        if (!existeDeuda()) {           
            var ocuTramite = document.forms[0].ocurrenciaTramite.value;
            var valorImporteDeudaIni = '';
            var valorFechaReqDeuda = '';

            var valorImporteDeuda = '';
            var valorFecResolucion = '';

            var valorImporteDeudaSusp = '';
            var valorFechaActivacion = '';

            if (codTram == codTramReqReintegro) {
                valorImporteDeudaIni = document.getElementsByName('T_' + codTram + '_' + ocuTramite + '_IMPORTEDEUDAINI')[0].value;
                valorFechaReqDeuda = document.getElementsByName('T_' + codTram + '_' + ocuTramite + '_FECHAREQDEUDA')[0].value;

            } else if (codTram == codTramResolucion) {
                valorImporteDeuda = document.getElementsByName('T_' + codTram + '_' + ocuTramite + '_IMPORTEDEUDA')[0].value;
                valorFecResolucion = document.getElementsByName('T_' + codTram + '_' + ocuTramite + '_FECRESOLUCION')[0].value;


            } else if (codTram == codTramSuspension) {
                valorImporteDeudaSusp = document.getElementsByName('T_' + codTram + '_' + ocuTramite + '_IMPORTEDEUDASUSP')[0].value;
                valorFechaActivacion = document.getElementsByName('T_' + codTram + '_' + ocuTramite + '_FECACT')[0].value;
            }

            if ((codTram == codTramReqReintegro && valorImporteDeudaIni != '' && valorFechaReqDeuda != '') ||
                    (codTram == codTramResolucion && valorImporteDeuda != '' && valorFecResolucion != '') ||
                    (codTram == codTramSuspension && valorImporteDeudaSusp != '' && valorFechaActivacion != '')) {
                var ajax = getXMLHttpRequest();
                var nodos = null;
                var parametros = "";
                var control = new Date();
                parametros = '?tarea=preparar&modulo=MELANBIDE07&operacion=validarCamposDeuda&tipo=0&numero=<%=numExpediente%>'
                        + '&control=' + control.getTime()
                        + '&codTramite=<%=codTramite%>'
                        + '&ocuTramite=' + ocuTramite
                        + '&importeDeudaIni=' + valorImporteDeudaIni
                        + '&fechaReqDeuda=' + valorFechaReqDeuda
                        + '&importeDeuda=' + valorImporteDeuda
                        + '&fecResolucion=' + valorFecResolucion
                        + '&importeDeudaSusp=' + valorImporteDeudaSusp
                        + '&fecActivacion=' + valorFechaActivacion
                        ;
                try {
                    ajax.open("POST", url, false);
                    ajax.setRequestHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF8");
                    ajax.setRequestHeader("Accept", "text/xml, application/xml, text/plain");
                    ajax.send(parametros);
                    if (ajax.readyState == 4 && ajax.status == 200) {
                        var xmlDoc = null;
                        if (navigator.appName.indexOf("Internet Explorer") != -1) {
                            // En IE el XML viene en responseText y no en la propiedad responseXML
                            var text = ajax.responseText;
                            xmlDoc = new ActiveXObject("Microsoft.XMLDOM");
                            xmlDoc.async = "false";
                            xmlDoc.loadXML(text);
                        } else {
                            // En el resto de navegadores el XML se recupera de la propiedad responseXML
                            xmlDoc = ajax.responseXML;
                        }//if(navigator.appName.indexOf("Internet Explorer")!=-1)
                    }//if (ajax.readyState==4 && ajax.status==200)
                    nodos = xmlDoc.getElementsByTagName("RESPUESTA");
                    var respuesta = nodos[0].childNodes[0].childNodes[0].nodeValue;
                    if (respuesta == 0) {
                        var ajax = getXMLHttpRequest();
                        var nodos = null;

                        var parametros = "";
                        var control = new Date();
                        parametros = '?tarea=preparar&modulo=MELANBIDE07&operacion=llamaAlServicioWebaltaCartaPagoNoRGI&tipo=0&numero=<%=numExpediente%>'
                                + '&control=' + control.getTime()
                                + '&codTramite=<%=codTramite%>'
                                + '&ocuTramite=' + ocuTramite
                                ;
                        try {
                            ajax.open("POST", url, false);
                            ajax.setRequestHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF8");
                            ajax.setRequestHeader("Accept", "text/xml, application/xml, text/plain");
                            ajax.send(parametros);
                            if (ajax.readyState == 4 && ajax.status == 200) {
                                var xmlDoc = null;
                                if (navigator.appName.indexOf("Internet Explorer") != -1) {
                                    // En IE el XML viene en responseText y no en la propiedad responseXML
                                    var text = ajax.responseText;
                                    xmlDoc = new ActiveXObject("Microsoft.XMLDOM");
                                    xmlDoc.async = "false";
                                    xmlDoc.loadXML(text);
                                } else {
                                    // En el resto de navegadores el XML se recupera de la propiedad responseXML
                                    xmlDoc = ajax.responseXML;
                                }//if(navigator.appName.indexOf("Internet Explorer")!=-1)
                            }//if (ajax.readyState==4 && ajax.status==200)
                            nodos = xmlDoc.getElementsByTagName("RESPUESTA");
                            var codigoOperacion = nodos[0].childNodes[0].childNodes[0].nodeValue;
                            habilitarBotonLargo(document.forms[0].btnAltaCarta);
                            switch (codigoOperacion) {
                                case '0':
                                    var idDeuda = nodos[0].childNodes[1].childNodes[0].nodeValue;
                                    jsp_alerta("A", '<%=meLanbide07I18n.getMensaje(idiomaUsuario,"altDeuda.correcto")%>');
                                    if (codTram == codTramReqReintegro) {
                                        vIdDeudaIni = idDeuda;
                                    } else if (codTram == codTramResolucion) {
                                        vIdDeuda = idDeuda;
                                    } else if (codTram == codTramSuspension) {
                                        vIdDeudaSusp = idDeuda;
                                    }
                                    deshabilitarBotonLargo(document.forms[0].btnAltaCarta);
                                    break;
                                case '-1':
                                    mensajeValidacionA = '<%=meLanbide07I18n.getMensaje(idiomaUsuario,"error.errorGen")%>';
                                    break;
                                case '1':
                                    mensajeValidacionA = '<%=meLanbide07I18n.getMensaje(idiomaUsuario,"error.zorku.1")%>';
                                    break;
                                case '2':
                                    mensajeValidacionA = '<%=meLanbide07I18n.getMensaje(idiomaUsuario,"error.zorku.2")%>';
                                    break;
                                case '3':
                                    mensajeValidacionA = '<%=meLanbide07I18n.getMensaje(idiomaUsuario,"error.zorku.3")%>';
                                    break;
                                case '4':
                                    mensajeValidacionA = '<%=meLanbide07I18n.getMensaje(idiomaUsuario,"error.zorku.4")%>';
                                    break;
                                case '5':
                                    mensajeValidacionA = '<%=meLanbide07I18n.getMensaje(idiomaUsuario,"error.zorku.5")%>';
                                    break;
                                case '6':
                                    mensajeValidacionA = '<%=meLanbide07I18n.getMensaje(idiomaUsuario,"error.zorku.6")%>';
                                    break;
                                case '7':
                                    mensajeValidacionA = '<%=meLanbide07I18n.getMensaje(idiomaUsuario,"error.zorku.7")%>';
                                    break;
                                case '8':
                                    mensajeValidacionA = '<%=meLanbide07I18n.getMensaje(idiomaUsuario,"error.zorku.8")%>';
                                    break;
                                case '9':
                                    mensajeValidacionA = '<%=meLanbide07I18n.getMensaje(idiomaUsuario,"error.zorku.9")%>';
                                    break;
                                case '10':
                                    mensajeValidacionA = '<%=meLanbide07I18n.getMensaje(idiomaUsuario,"error.zorku.10")%>';
                                    break;
                                case '11':
                                    mensajeValidacionA = '<%=meLanbide07I18n.getMensaje(idiomaUsuario,"error.zorku.11")%>';
                                    break;
                                case '12':
                                    mensajeValidacionA = '<%=meLanbide07I18n.getMensaje(idiomaUsuario,"error.zorku.12")%>';
                                    break;
                                case '14':
                                    mensajeValidacionA = '<%=meLanbide07I18n.getMensaje(idiomaUsuario,"error.zorku.14")%>';
                                    break;
                                case '15':
                                    mensajeValidacionA = '<%=meLanbide07I18n.getMensaje(idiomaUsuario,"error.zorku.15")%>';
                                    break;
                                case '16':
                                    mensajeValidacionA = '<%=meLanbide07I18n.getMensaje(idiomaUsuario,"error.zorku.16")%>';
                                    break;
                                case '17':
                                    mensajeValidacionA = '<%=meLanbide07I18n.getMensaje(idiomaUsuario,"error.zorku.17")%>';
                                    break;
                                case '18':
                                    mensajeValidacionA = '<%=meLanbide07I18n.getMensaje(idiomaUsuario,"error.zorku.18")%>';
                                    break;
                                case '19':
                                    mensajeValidacionA = '<%=meLanbide07I18n.getMensaje(idiomaUsuario,"error.zorku.19")%>';
                                    break;
                                case '24':
                                    mensajeValidacionA = '<%=meLanbide07I18n.getMensaje(idiomaUsuario,"error.zorku.24")%>';
                                    break;
                                case '39':
                                    mensajeValidacionA = '<%=meLanbide07I18n.getMensaje(idiomaUsuario,"error.zorku.39")%>';
                                    break;
                                case '41':
                                    mensajeValidacionA = '<%=meLanbide07I18n.getMensaje(idiomaUsuario,"error.zorku.41")%>';
                                    break;
                                case '42':
                                    mensajeValidacionA = '<%=meLanbide07I18n.getMensaje(idiomaUsuario,"error.zorku.42")%>';
                                    break;
                                case '43':
                                    mensajeValidacionA = '<%=meLanbide07I18n.getMensaje(idiomaUsuario,"error.zorku.43")%>';
                                    break;
                                case '44':
                                    mensajeValidacionA = '<%=meLanbide07I18n.getMensaje(idiomaUsuario,"error.zorku.44")%>';
                                    break;
                                case '45':
                                    mensajeValidacionA = '<%=meLanbide07I18n.getMensaje(idiomaUsuario,"error.zorku.45")%>';
                                    break;
                                case '46':
                                    mensajeValidacionA = '<%=meLanbide07I18n.getMensaje(idiomaUsuario,"error.zorku.46")%>';
                                    break;
                                case '47':
                                    mensajeValidacionA = '<%=meLanbide07I18n.getMensaje(idiomaUsuario,"error.zorku.47")%>';
                                    break;
                                case '403':
                                    mensajeValidacionA = '<%=meLanbide07I18n.getMensaje(idiomaUsuario,"error.zorku.403")%>';
                                    break;
                                case '404':
                                    mensajeValidacionA = '<%=meLanbide07I18n.getMensaje(idiomaUsuario,"error.zorku.404")%>';
                                    break;
                                case '500':
                                    mensajeValidacionA = '<%=meLanbide07I18n.getMensaje(idiomaUsuario,"error.zorku.500")%>';
                                    break;
                                case '503':
                                    mensajeValidacionA = '<%=meLanbide07I18n.getMensaje(idiomaUsuario,"error.zorku.503")%>';
                                    break;
                                default:
                                    mensajeValidacionA = codigoOperacion;
                                    break;
                            }
                            if (codigoOperacion != '0') {
                                document.getElementById('mensajeError').innerHTML = mensajeValidacionA;
                                jsp_alerta("A", mensajeValidacionA);
                            }
                        } catch (Err) {
                            habilitarBotonLargo(document.forms[0].btnAltaCarta);
                            //alert ("ERROR " + Err);
                        }//try-catch        
                    } else {
                        habilitarBotonLargo(document.forms[0].btnAltaCarta);
                        jsp_alerta("A", '<%=meLanbide07I18n.getMensaje(idiomaUsuario,"msg.error.datosSinGuardar")%>');
                    } //respuesta==0
                } catch (Err) {
//                    alert("ERROR " + Err);
                    habilitarBotonLargo(document.forms[0].btnAltaCarta);

                }//try-catch 
            } else {
                habilitarBotonLargo(document.forms[0].btnAltaCarta);
                jsp_alerta("A", '<%=meLanbide07I18n.getMensaje(idiomaUsuario,"msg.error.camposVacios")%>');
            }
        } //!existeDeuda()
        else {
            jsp_alerta("A", mensajeValidacionA);
        }
    }

    function actualizaImporte() {
        if (deuPagada == 'S') {
            var campo;
            if (codTram == codTramReqReintegro) {
                campo = document.getElementsByName('T_' + codTram + '_' + ocuTramite + '_IMPORTEDEUDAINI')[0];
            } else if (codTram == codTramResolucion) {
                campo = document.getElementsByName('T_' + codTram + '_' + ocuTramite + '_IMPORTEDEUDA')[0];
            }
            campo.readOnly = true;
            campo.value = "0";
        }
    }
</script>

<div class="tab-page" id="tabPage341" style="width: 100%;">
    <h2 class="tab" id="pestana341"><%=meLanbide07I18n.getMensaje(idiomaUsuario,"label.tituloPestana")%></h2>
    <script type="text/javascript">tp1_p341 = tp1.addTabPage(document.getElementById("tabPage341"));</script>
    <h2 class="sub3titulo" id="pestanaPrinc"><%=meLanbide07I18n.getMensaje(idiomaUsuario,"label.tituloPrincipal")%></h2>
    <div style="clear: both;">           
        <div class="botonera" style="padding-top: 20px; padding-bottom: 20px; text-align: center;">  
            <input type="button" id="btnAltaCarta" name="btnAltaCarta" class="botonMasLargo" value="<%=meLanbide07I18n.getMensaje(idiomaUsuario,"btn.generar")%>" onclick="altaCartaPago();
                        return false;">                  
        </div>
    </div>
    <div style="width: 100%; padding: 10px; align-content: center; text-align: center;">
        <label id="mensajeError" name="mensajeError" class="legendRojo"></label>
    </div>
</div>
<script type="text/javascript">
    actualizaImporte();
    if (existeDeuda()) {
        deshabilitarBotonLargo(document.forms[0].btnAltaCarta);
        document.getElementById('mensajeError').innerHTML = mensajeValidacionA;
    }
</script>

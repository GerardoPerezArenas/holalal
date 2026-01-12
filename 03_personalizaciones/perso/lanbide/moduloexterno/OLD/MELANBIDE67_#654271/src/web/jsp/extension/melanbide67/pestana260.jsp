<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>

<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide67.i18n.MeLanbide67I18n" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide67.vo.SelectItem"%>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide67.vo.DatosPestanaVO" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide67.util.ConfigurationParameter"%>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide67.util.ConstantesMeLanbide67"%>
<%@page import="es.altia.common.service.config.Config"%>
<%@page import="es.altia.common.service.config.ConfigServiceHelper"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.ArrayList" %>
<%@page import="java.util.List" %>
<%@page import="org.apache.commons.lang.StringEscapeUtils"%>
<%@page import="es.altia.agora.business.util.GeneralValueObject"%>
<%@page import="es.altia.agora.interfaces.user.web.sge.FichaExpedienteForm"%>
<%@page import="java.util.*" %>
<%@page import="java.math.*" %>
<%@page import="java.text.*"%>
<html>
    <head>
        <meta http-equiv="X-UA-Compatible" content="IE=Edge" />
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <%
            String sIdioma = request.getParameter("idioma");
            int idiomaUsuario = Integer.parseInt(sIdioma);
            int apl = 5;
            String css = "";
            UsuarioValueObject usuario = new UsuarioValueObject();
            try
            {
                if (session != null) 
                {
                    if (usuario != null) 
                    {
                        usuario = (UsuarioValueObject) session.getAttribute("usuario");
                        idiomaUsuario = usuario.getIdioma();
                        apl = usuario.getAppCod();
                        css = usuario.getCss();
                    }
                }
            }
            catch(Exception ex)
            {
        
            }    


            //Clase para internacionalizar los mensajes de la aplicacion.
            MeLanbide67I18n meLanbide67I18n = MeLanbide67I18n.getInstance();
    
            Config m_Config = ConfigServiceHelper.getConfig("common");
            String statusBar = m_Config.getString("JSP.StatusBar");
            String nombreModulo     = request.getParameter("nombreModulo");
            String codOrganizacion  = request.getParameter("codOrganizacionModulo");
            String numExpediente    = request.getParameter("numero");
            String codTram          = request.getParameter("codigoTramite");
            String ocurrenciaTram = request.getParameter("ocurrenciaTramite");
            String unidadTraTramite = request.getParameter("codUnidadTramitadoraTram");
            //
                        String procedimiento1 = request.getParameter("procedimiento");
                        String codMunicipio1 = request.getParameter("codMunicipio");
                        String codProcedimiento1 = request.getParameter("codProcedimiento");
                        String ejercicio1 = request.getParameter("ejercicio");
                        String codTramite1 = request.getParameter("codTramite");
                        //String ocurrenciaTramite = request.getParameter("ocurrenciaTramite");
                        String titular1 = request.getParameter("titular");
                        String codUnidadOrganicaExp1 = request.getParameter("codUnidadOrganicaExp");
                        //String codUnidadTramitadoraTram = request.getParameter("codUnidadTramitadoraTram");
                 
            DecimalFormatSymbols simbolo=new DecimalFormatSymbols();
            simbolo.setDecimalSeparator(',');
            simbolo.setGroupingSeparator('.');
            DecimalFormat formateador = new DecimalFormat("###,##0.00",simbolo);
            String estadoInicial =(String)request.getAttribute("estadoInicial");
            DatosPestanaVO datosPestana = new DatosPestanaVO();
            datosPestana = (DatosPestanaVO) request.getAttribute("datosPestana");
            BigDecimal impSuvencionIniTramResolEstTec= (datosPestana != null && datosPestana.getImpSuvencionIniTramResolEstTec() != null ? datosPestana.getImpSuvencionIniTramResolEstTec() : BigDecimal.ZERO);
        %>

        <jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
        <jsp:setProperty name="descriptor"  property="idi_cod" value="<%=idiomaUsuario%>" />
        <jsp:setProperty name="descriptor"  property="apl_cod" value="<%=apl%>" />
        <link rel="StyleSheet" media="screen" type="text/css" href="<%=request.getContextPath()%><%=css%>">
        <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/css/font-awesome.min.css'/>" media="screen">
        <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/less/animated.less'/>" media="screen">
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/>
        <link rel="stylesheet" type="text/css" href="<c:url value='/css/extension/melanbide67/melanbide67.css'/>">
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script>
        <script type="text/javascript" src="<c:url value='/scripts/popup.js'/>"></script>
        <script type="text/javascript" src="<c:url value='/scripts/listaComboBox.js'/>"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/melanbide67/leaukUtils.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/melanbide67/lanbide.js"></script>
        <script type="text/javascript">
            var APP_CONTEXT_PATH = '<%=request.getContextPath()%>';
            var comboListaCausas;
            var listaCodCausas = new Array();
            var listaDescCausas = new Array();
            var comboListaMotivos;
            var listaCodMotivos = new Array();
            var listaDescMotivos = new Array();

            var fechaInicio;
            var fechaFinE;
            var fechaFinR;

            var concedido1;
            var concedido2;
            var concedidoTotal;

            var nuevoPago1;
            var nuevoPago2;
            var totalNuevo;

            var pagado1;
            var pagado2;
            var pagadoTotal;

            var devolver1;
            var devolver2;
            var devolverTotal;

            var estadoInicial;
            var diasEsperados;
            var diasReales;


            function getXMLHttpRequest()
            {
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

            function compruebaTamanoCampo(elemento, maxTex) {
                var texto = elemento.value;
                if (texto.length > maxTex) {
                    jsp_alerta("A", '<%=meLanbide67I18n.getMensaje(idiomaUsuario, "error.excesoTexto")%>' + maxTex);
                    elemento.focus();
                    return false;
                }
                return true;
            }
            function buscaCodigoCausa(codCausa) {
                comboListaCausas.buscaCodigo(codCausa);
            }
            function cargarDatosCausas() {
                limpiarCampos();
                document.getElementById('codListaMotivos').value = "";
                document.getElementById('descListaMotivos').value = "";
                var codCausasSeleccionado = document.getElementById("codListaCausas").value;
                buscaCodigoCausa(codCausasSeleccionado);
                compruebaCausa();
            }
            function buscaCodigoMotivos(codMotivo) {
                comboListaMotivos.buscaCodigo(codMotivo);
            }
            function cargarDatosMotivos() {
                limpiarCampos();
                var codMotivosSeleccionado = document.getElementById("codListaMotivos").value;
                buscaCodigoMotivos(codMotivosSeleccionado);
                compruebaCausa();
            }
            function compruebaCausa(llamarcalDevolver) {
                var llamaDevolver = (llamarcalDevolver != null && llamarcalDevolver != undefined ? llamarcalDevolver : true);
                if (document.getElementById('codListaCausas').value == "3") {
                    habilitarMotivo();
                    habilitar2Fechas();
                    hacerNoEditable();
                    if (document.getElementById('codListaMotivos').value == "5") {
                        importeNuevoCero();
                        deshabilitarFechaReal();
                        // Habilito el calculo para que me actualice la fila por si se han estado seleccionando diferentes opciones
                        llamaDevolver = true;
                        //  jsp_alerta("A","Estado 1");
                    } else {
                        habilitarFechaReal();
                        if (document.getElementById('fechaFinReal').value != null && document.getElementById('fechaFinReal').value != "") {
                            calculoFinPrematuro();
                        } else {
                            // En caso de que no haya fecha de fin real incializamos los valores de la fila a devolver 
                            // y le indicamos que no llame la funcion calcularDevolver para se calcule al editar el campo fecha
                            llamaDevolver = false;
                            document.getElementById('celdaD1').innerHTML = '0,00';
                            document.getElementById('celdaD2').innerHTML = '0,00';
                            document.getElementById('celdaD3').innerHTML = '0,00';

                        }
                        //   jsp_alerta("A","Estado 2");
                        //  filaNuevoEstado2();
                    }
                } else {
                    deshabilitarCampos();
                    if (document.getElementById('codListaCausas').value == "5") {
                        hacerEditable();
                        importeNuevoConcedido();
                        // jsp_alerta("A","Estado 3");
                    } else {
                        importeNuevoCero();
                        hacerNoEditable();
                        //   jsp_alerta("A","Estado 4");
                    }
                }
                if (llamaDevolver)
                    calcularADevolver();
            }

            function deshabilitarCampos() {
                deshabilitarMotivo();
                deshabilitar2Fechas();
                deshabilitarFechaReal();
                hacerNoEditable();
            }

            function habilitarMotivo() {
                document.getElementById('campoMotivo').style.visibility = "visible";
                document.getElementById('campoMotivo').disabled = false;
            }
            function deshabilitarMotivo() {
                document.getElementById('campoMotivo').style.visibility = "hidden";
                document.getElementById('campoMotivo').disabled = true;
            }
            function habilitar2Fechas() {
                document.getElementById('campoFInicio').style.visibility = "visible";
                document.getElementById('campoFInicio').disabled = false;
                document.getElementById('fechaInicio').style.visibility = "visible";
                document.getElementById('campoFinEsperado').style.visibility = "visible";
                document.getElementById('campoFinEsperado').disabled = false;
                document.getElementById('fechaFinEsperado').style.visibility = "visible";
            }
            function deshabilitar2Fechas() {
                document.getElementById('campoFInicio').style.visibility = "hidden";
                document.getElementById('campoFInicio').disabled = true;
                document.getElementById('fechaInicio').style.visibility = "hidden";
                document.getElementById('fechaInicio').disabled = true;
                document.getElementById('campoFinEsperado').style.visibility = "hidden";
                document.getElementById('campoFinEsperado').disabled = true;
                document.getElementById('fechaFinEsperado').style.visibility = "hidden";
                document.getElementById('fechaFinEsperado').disabled = true;
            }
            function habilitarFechaReal() {
                document.getElementById('campoFReal').style.visibility = "visible";
                document.getElementById('campoFReal').disabled = false;
                document.getElementById('fechaFinReal').style.visibility = "visible";
                document.getElementById('fechaFinReal').disabled = false;
                document.getElementById('calFechaReal').style.visibility = "visible";
            }
            function deshabilitarFechaReal() {
                document.getElementById('campoFReal').style.visibility = "hidden";
                document.getElementById('campoFReal').disabled = true;
                document.getElementById('fechaFinReal').style.visibility = "hidden";
                document.getElementById('fechaFinReal').disabled = true;
                document.getElementById('fechaFinReal').value = "";
                document.getElementById('calFechaReal').style.visibility = "hidden";
            }

            function hacerEditable() {
                document.getElementById('impCeldaN1').disabled = false;
                document.getElementById('impCeldaN1').readonly = false;
                document.getElementById('impCeldaN2').disabled = false;
                document.getElementById('impCeldaN2').readonly = false;
                document.getElementById('impCeldaN3').disabled = false;
                document.getElementById('impCeldaN3').readonly = false;
                // AJUSTES 
                document.getElementById('impCeldaN1').style.visibility = "visible";
                document.getElementById('impCeldaN2').style.visibility = "visible";
            }
            function hacerNoEditable() {
                document.getElementById('impCeldaN1').disabled = true;
                document.getElementById('impCeldaN1').readonly = true;
                document.getElementById('impCeldaN2').disabled = true;
                document.getElementById('impCeldaN2').readonly = true;
                document.getElementById('impCeldaN3').disabled = true;
                document.getElementById('impCeldaN3').readonly = true;
// AJUSTES 
                document.getElementById('impCeldaN1').style.visibility = "hidden";
                document.getElementById('impCeldaN2').style.visibility = "hidden";
            }
            function importeNuevoCero() {
// AJUSTES        
                document.getElementById('impCeldaN1').value = "0,00";
                document.getElementById('impCeldaN2').value = "0,00";
                document.getElementById('impCeldaN3').value = "0,00";
            }
            function importeNuevoConcedido() {
// AJUSTES        
                document.getElementById('impCeldaN1').value = formatearNumeroAString('<%=datosPestana != null && datosPestana.getPago1() != null ? datosPestana.getPago1() :"0.00"%>');
                document.getElementById('impCeldaN2').value = formatearNumeroAString('<%=datosPestana != null && datosPestana.getPago2() != null ? datosPestana.getPago2() :"0.00"%>');
                document.getElementById('impCeldaN3').value = formatearNumeroAString('<%=datosPestana != null && datosPestana.getImporteTotalIni() != null ? datosPestana.getImporteTotalIni() :"0.00"%>');
            }

            function pulsarCalcular(mod) {
                if (compruebaCampos()) {
                    var causaCal = document.getElementById('codListaCausas').value;
                    var fechaInicio = "";
                    var fechaEspera = "";
                    var fechaReal = "";
                    var motivoCal = "";
                    var totalNuevo;
                    var primerNuevo;
                    var segundoNuevo;

                    if (causaCal == "3") {
                        // var pagar = parseFloat(convertirANumero(document.getElementById('impAPagar').value));
                        motivoCal = document.getElementById('codListaMotivos').value;
                        fechaInicio = '<%=datosPestana.getFechaInicio()%>';
                        fechaEspera = '<%=datosPestana.getFechaFinEsperada()%>';
                        if (motivoCal == "5") {
                            totalNuevo = parseFloat(convertirANumero(document.getElementById('celdaC3').innerHTML));
                            primerNuevo = parseFloat(convertirANumero(document.getElementById('celdaP1').innerHTML));
                            segundoNuevo = parseFloat(convertirANumero(document.getElementById('celdaP2').innerHTML));
                        } else {
                            //calcular nuevo importe
                            calculoFinPrematuro();
                            fechaReal = document.getElementById('fechaFinReal').value;
                            //   primerNuevo =document.getElementById('impCeldaN1').value;
                            primerNuevo = parseFloat(convertirANumero(document.getElementById('celdaP1').innerHTML));
                            segundoNuevo = parseFloat(convertirANumero(document.getElementById('celdaP2').innerHTML));
                            totalNuevo = parseFloat('<%=impSuvencionIniTramResolEstTec%>');//parseFloat(convertirANumero(document.getElementById('celdaC3').innerHTML));
                            calcularADevolver();
                        }
                    } else if (causaCal == "5") {
// AJUSTES        
// he puesto la segunda carga de la variable - seria mejor un if??
                        primerNuevo = parseFloat(convertirANumero(document.getElementById('impCeldaN1').value));
                        primerNuevo = (primerNuevo == null || primerNuevo == undefined || isNaN(primerNuevo) || primerNuevo == "") ? 0 : primerNuevo;
                        segundoNuevo = parseFloat(convertirANumero(document.getElementById('impCeldaN2').value));
                        segundoNuevo = (segundoNuevo == null || segundoNuevo == undefined || isNaN(segundoNuevo) || segundoNuevo == "") ? 0 : segundoNuevo;
                        totalNuevo = parseFloat(convertirANumero(document.getElementById('impCeldaN3').value));

                    } else {
                        totalNuevo = parseFloat(convertirANumero(document.getElementById('celdaC3').innerHTML));
                        primerNuevo = parseFloat(convertirANumero(document.getElementById('celdaP1').innerHTML));
                        segundoNuevo = parseFloat(convertirANumero(document.getElementById('celdaP2').innerHTML));
                    }

                    var salida;
                    var ajax = getXMLHttpRequest();
                    var nodos = null;
                    var control = new Date();
                    var CONTEXT_PATH = '<%=request.getContextPath()%>';
                    var url = CONTEXT_PATH + '/PeticionModuloIntegracion.do';
                    var parametros = "tarea=preparar&modulo=MELANBIDE67&operacion=recalculoModSubvLakEmp&tipo=0&nuevo=0"
                            + "&numExp=<%=numExpediente%>&tramite=<%=codTram%>"
                            + "&ocurrenciaTram=<%=ocurrenciaTram%>"
                            + "&fecIni=" + fechaInicio
                            + "&fecFinEspe=" + fechaEspera
                            + "&fecFinReal=" + fechaReal
                            + "&causa=" + causaCal
                            + "&motivo=" + motivoCal
                            + "&impConcedido=" + totalNuevo
                            + "&impPagado1=" + primerNuevo
                            + "&impPagado2=" + segundoNuevo;
                    try
                    {
                        ajax.open("POST", url, false);
                        ajax.setRequestHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
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
                        var elemento = nodos[0];
                        var hijos = elemento.childNodes;
                        var codigoOperacion = null;
                        var nodoValores;
                        var hijosValores;
                        for (var j = 0; hijos != null && j < hijos.length; j++) {
                            if (hijos[j].nodeName == "CODIGO_OPERACION") {
                                codigoOperacion = hijos[j].childNodes[0].nodeValue;
                            }//if(hijos[j].nodeName=="CODIGO_OPERACION") 
                            else if (hijos[j].nodeName == "SALIDA") {
                                salida = hijos[j].childNodes[0].nodeValue;
                            } else if (hijos[j].nodeName == "IMPTOT") {
                                totalNuevo = hijos[j].childNodes[0].nodeValue;
                                //totalNuevo=(totalNuevo!=null && totalNuevo!= undefined ? totalNuevo.toString().replace('.',','):totalNuevo);
                            } else if (hijos[j].nodeName == "PAGO1") {
                                primerNuevo = hijos[j].childNodes[0].nodeValue;
                                //primerNuevo=(primerNuevo!=null && primerNuevo!= undefined ? primerNuevo.toString().replace('.',','):primerNuevo);
                            } else if (hijos[j].nodeName == "PAGO2") {
                                segundoNuevo = hijos[j].childNodes[0].nodeValue;
                                //segundoNuevo=(segundoNuevo!=null && segundoNuevo!= undefined ? segundoNuevo.toString().replace('.',','):segundoNuevo);
                            }
                        }//for(j=0;hijos!=null && j<hijos.length;j++)														
                        if (codigoOperacion == "0") {
                            jsp_alerta('A', "Se han modificado los importes");
                            document.getElementById('impCeldaN3').value = formatearNumeroAString(totalNuevo);
                            //FormatNumber(totalNuevo, 6, 0, 'impCeldaN3');  
// AJUSTES  
                            document.getElementById('impCeldaN1').value = formatearNumeroAString(primerNuevo);
//                    
//                    
                            //FormatNumber(primerNuevo, 6, 0, 'impCeldaN1'); //primerNuevo;
                            if (segundoNuevo < 0) {
// AJUSTES
                                document.getElementById('impCeldaN2').value = "0,00";
                                document.getElementById('celdaD3').innerHTML = formatearNumeroAString(segundoNuevo);
                                document.getElementById('celdaD1').innerHTML = "0,00";
                            } else {
// AJUSTES 
                                document.getElementById('impCeldaN2').value = formatearNumeroAString(segundoNuevo);
                                //FormatNumber(segundoNuevo, 6, 0, 'impCeldaN2'); 
                                calcularADevolver();
                            }
                        } else if (codigoOperacion == "1") {
                            jsp_alerta("A", "FALLO EN LA CARGA DE DATOS");
                        } else if (codigoOperacion == "2") {
                            jsp_alerta("A", "En el expediente:  faltan campos suplementarios ");
                        } else if (codigoOperacion == "3") {
                            jsp_alerta("A", " - " + salida);
                        } else {
                            jsp_alerta("A", "ERROR PETICION CARGAR DATOS");
                        }
                    } catch (err)
                    {
                        // jsp_alerta("A", "ERROR calcular");
                    }
                    // grabo suplementarios
                    //  calcularADevolver();
                    if (codigoOperacion == "0") {
                        grabarSuplementarios();
                        //comboT_22_1_CAUSA.buscaCodigo(causaCal);
                        //
                        //+
                        //    ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
                        // esto en local   
                        var codTramite = "<%=codTram%>";
                        //var ca= document.getElementById('codListaCausas').value;
                        /*
                         * Codigos de campos
                         * CAUSA  (Desplegable)
                         * MOTIMODRESOL (Desplegable)
                         * FECFINMODRESOL
                         * IMPRESOLINI
                         * IMPREMODSOL
                         * IMPPAGPAGO1
                         * IMPPTEPAGO2
                         * IMPDEVOL
                         */
                        if (document.getElementById('T_' + codTramite + '_<%=ocurrenciaTram%>_CAUSA') != null && document.getElementById('T_' + codTramite + '_<%=ocurrenciaTram%>_CAUSA') != undefined)
                            document.getElementById('T_' + codTramite + '_<%=ocurrenciaTram%>_CAUSA').value = document.getElementById('codListaCausas').value;
                        if (document.getElementById('descT_' + codTramite + '_<%=ocurrenciaTram%>_CAUSA') != null && document.getElementById('descT_' + codTramite + '_<%=ocurrenciaTram%>_CAUSA') != undefined)
                            document.getElementById('descT_' + codTramite + '_<%=ocurrenciaTram%>_CAUSA').value = document.getElementById('descListaCausas').value;
                        if (document.getElementById('T_' + codTramite + '_<%=ocurrenciaTram%>_MOTIMODRESOL') != null && document.getElementById('T_' + codTramite + '_<%=ocurrenciaTram%>_MOTIMODRESOL') != undefined)
                            document.getElementById('T_' + codTramite + '_<%=ocurrenciaTram%>_MOTIMODRESOL').value = document.getElementById('codListaMotivos').value;
                        if (document.getElementById('descT_' + codTramite + '_<%=ocurrenciaTram%>_MOTIMODRESOL') != null && document.getElementById('descT_' + codTramite + '_<%=ocurrenciaTram%>_MOTIMODRESOL') != undefined)
                            document.getElementById('descT_' + codTramite + '_<%=ocurrenciaTram%>_MOTIMODRESOL').value = document.getElementById('descListaMotivos').value;
                        if (document.getElementById('T_' + codTramite + '_<%=ocurrenciaTram%>_FECFINMODRESOL') != null && document.getElementById('T_' + codTramite + '_<%=ocurrenciaTram%>_FECFINMODRESOL') != undefined)
                            document.getElementById('T_' + codTramite + '_<%=ocurrenciaTram%>_FECFINMODRESOL').value = document.getElementById('fechaFinReal').value;
                        if (document.getElementById('T_' + codTramite + '_<%=ocurrenciaTram%>_IMPRESOLINI') != null && document.getElementById('T_' + codTramite + '_<%=ocurrenciaTram%>_IMPRESOLINI') != undefined)
                            document.getElementById('T_' + codTramite + '_<%=ocurrenciaTram%>_IMPRESOLINI').value = document.getElementById('celdaC3').innerHTML;
                        if (document.getElementById('T_' + codTramite + '_<%=ocurrenciaTram%>_IMPREMODSOL') != null && document.getElementById('T_' + codTramite + '_<%=ocurrenciaTram%>_IMPREMODSOL') != undefined)
                            document.getElementById('T_' + codTramite + '_<%=ocurrenciaTram%>_IMPREMODSOL').value = document.getElementById('impCeldaN3').value;
                        if (document.getElementById('T_' + codTramite + '_<%=ocurrenciaTram%>_IMPPAGPAGO1') != null && document.getElementById('T_' + codTramite + '_<%=ocurrenciaTram%>_IMPPAGPAGO1') != undefined)
                            document.getElementById('T_' + codTramite + '_<%=ocurrenciaTram%>_IMPPAGPAGO1').value = document.getElementById('impCeldaN1').value;
                        if (document.getElementById('T_' + codTramite + '_<%=ocurrenciaTram%>_IMPPTEPAGO2') != null && document.getElementById('T_' + codTramite + '_<%=ocurrenciaTram%>_IMPPTEPAGO2') != undefined)
                            document.getElementById('T_' + codTramite + '_<%=ocurrenciaTram%>_IMPPTEPAGO2').value = document.getElementById('impCeldaN2').value;
                        if (document.getElementById('T_' + codTramite + '_<%=ocurrenciaTram%>_IMPDEVOL') != null && document.getElementById('T_' + codTramite + '_<%=ocurrenciaTram%>_IMPDEVOL') != undefined)
                            document.getElementById('T_' + codTramite + '_<%=ocurrenciaTram%>_IMPDEVOL').value = document.getElementById('celdaD3').innerHTML;
                        if (document.getElementById('T_' + codTramite + '_<%=ocurrenciaTram%>_IMPTOT') != null && document.getElementById('T_' + codTramite + '_<%=ocurrenciaTram%>_IMPTOT') != undefined)
                            document.getElementById('T_' + codTramite + '_<%=ocurrenciaTram%>_IMPTOT').value = document.getElementById('impCeldaN3').value;
                        if (document.getElementById('T_' + codTramite + '_<%=ocurrenciaTram%>_IMPPAGO1') != null && document.getElementById('T_' + codTramite + '_<%=ocurrenciaTram%>_IMPPAGO1') != undefined)
                            document.getElementById('T_' + codTramite + '_<%=ocurrenciaTram%>_IMPPAGO1').value = document.getElementById('impCeldaN1').value;
                        if (document.getElementById('T_' + codTramite + '_<%=ocurrenciaTram%>_IMPPAGO2') != null && document.getElementById('T_' + codTramite + '_<%=ocurrenciaTram%>_IMPPAGO2') != undefined)
                            document.getElementById('T_' + codTramite + '_<%=ocurrenciaTram%>_IMPPAGO2').value = document.getElementById('impCeldaN2').value;

                        // funciona correctamente recargando todo el tramite
                        /*
                         document.forms[0].numExpediente.value = "<%=numExpediente%>";
                         document.forms[0].procedimiento.value = "<%=procedimiento1%>";
                         document.forms[0].codMunicipio.value = "<%=codMunicipio1%>";
                         document.forms[0].codProcedimiento.value = "<%=codProcedimiento1%>";
                         document.forms[0].ejercicio.value = "<%=ejercicio1%>";
                         document.forms[0].numero.value = "<%=numExpediente%>";
                         document.forms[0].codTramite.value = "<%=codTramite1%>";//listaTramitesOriginal[i][1];
                         document.forms[0].ocurrenciaTramite.value = "<%=ocurrenciaTram%>";//listaTramitesOriginal[i][0];
                         document.forms[0].titular.value = "<%=titular1%>";
                         //document.forms[0].codUnidadOrganicaExp.value = "<%=codUnidadOrganicaExp1%>";
                         document.forms[0].codUnidadTramitadoraTram.value = "<%=unidadTraTramite%>";//listaTramitesOriginal[i][10];
                         //  pleaseWait('on');
                         //  activarFormulario();
                         document.forms[0].opcion.value="inicio";
                         document.forms[0].target="mainFrame";
                         document.forms[0].action="<c:url value='/sge/TramitacionExpedientes.do'/>";
                         document.forms[0].submit();
                         */
                    }
                } else {
                    //    jsp_alerta("A", "No se puede llamar a la función de cálculo");
                }
            }

            function grabarSuplementarios() {
                var fechaFReal = "";
                if (document.getElementById('fechaFinReal').value != null) {
                    fechaFReal = document.getElementById('fechaFinReal').value;
                } else {
                    fechaFReal = "";
                }

                var causaCal = document.getElementById('codListaCausas').value;
                var motivoCal;
                if (causaCal == "3") {
                    motivoCal = document.getElementById('codListaMotivos').value;
                } else {
                    motivoCal = "";
                }
                var primerNuevo = document.getElementById('impCeldaN1').value;

                //   var priNue = convertirANumero(primerNuevo);
                //    jsp_alerta("A"," "+priNue);
                var segundoNuevo = document.getElementById('impCeldaN2').value;
                var totalNuevo = document.getElementById('impCeldaN3').value;
                var devolver = document.getElementById('celdaD3').innerHTML;
                var concedido = convertirANumero(document.getElementById('celdaC3').innerHTML);

                var importeTotalActualizado = document.getElementById('impCeldaN3').value;  //document.getElementById('T_<%=codTram%>_<%=ocurrenciaTram%>_IMPTOT').value; 
                var importePrimerPagoActualizado = document.getElementById('impCeldaN1').value; //document.getElementById('T_<%=codTram%>_<%=ocurrenciaTram%>_IMPPAGO1').value; 
                var importeSegundoPagoActualizado = document.getElementById('impCeldaN2').value;    //document.getElementById('T_<%=codTram%>_<%=ocurrenciaTram%>_IMPPAGO2').value; 



                var salida;
                var ajax = getXMLHttpRequest();
                var nodos = null;
                var control = new Date();
                var CONTEXT_PATH = '<%=request.getContextPath()%>';
                var url = CONTEXT_PATH + '/PeticionModuloIntegracion.do';
                var parametros = "tarea=preparar&modulo=MELANBIDE67&operacion=grabarSuplementariosTramite&tipo=0&nuevo=0"
                        + "&numExp=<%=numExpediente%>&tramite=<%=codTram%>"
                        + "&ocurrenciaTram=<%=ocurrenciaTram%>"
                        + "&fecFinReal=" + fechaFReal
                        + "&causa=" + causaCal
                        + "&motivo=" + motivoCal
                        + "&impResol=" + concedido
                        + "&impModif=" + totalNuevo
                        + "&impPagado1=" + primerNuevo
                        + "&impPagado2=" + segundoNuevo
                        + "&aDevolver=" + devolver
                        + "&importeTotalActualizado=" + importeTotalActualizado
                        + "&importePrimerPagoActualizado=" + importePrimerPagoActualizado
                        + "&importeSegundoPagoActualizado=" + importeSegundoPagoActualizado
                        ;

                try {
                    ajax.open("POST", url, false);
                    ajax.setRequestHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
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
                    var elemento = nodos[0];
                    var hijos = elemento.childNodes;
                    var codigoOperacion = null;
                    for (var j = 0; hijos != null && j < hijos.length; j++) {
                        if (hijos[j].nodeName == "CODIGO_OPERACION") {
                            codigoOperacion = hijos[j].childNodes[0].nodeValue;
                        }//if(hijos[j].nodeName=="CODIGO_OPERACION")   
                    }//for(j=0;hijos!=null && j<hijos.length;j++)
                    if (codigoOperacion != "0") {
                        //jsp_alerta('A', "Se han grabado los campos suplementarios del Tramite  ");
                        jsp_alerta('A', "Se ha presntado un error al grabar los campos suplementarios del tramite.");
                    }
                } catch (err) {
                    jsp_alerta("A", "ERROR grabar suplementarios");
                }
            }

            function calcularADevolver() {
                // controlar NaN null vacio


                var nuevoTotal;
                nuevoTotal = document.getElementById('impCeldaN3').value;
                //  var nuevoTotal = parseFloat(document.getElementById('impCeldaN3').innerHTML);
                //   nuevoTotal = (nuevoTotal == null || nuevoTotal == undefined || nuevoTotal == "") ? 0 : nuevoTotal;
                var nuevo1 = document.getElementById('impCeldaN1').value;
                // si tiene ,
                // var nuevoFlo = parseFloat(nuevo1);
                //   jsp_alerta("A", "n1: "+nuevo1);
                var nuevo2 = document.getElementById('impCeldaN2').value;
                //  jsp_alerta("A", "n2: "+nuevo2 + " - "+ Number(nuevo2));
                var paga1 = parseFloat(<%=datosPestana != null && datosPestana.getPagado1() != null ? datosPestana.getPagado1() :""%>);
                var paga2 = parseFloat(<%=datosPestana != null && datosPestana.getPagado2() != null ? datosPestana.getPagado2() :""%>);
                nuevoTotal = (nuevoTotal != null && nuevoTotal != undefined && nuevoTotal != "" ? nuevoTotal.toString().replace('.', '').replace(',', '.') : 0);
                nuevoTotal = parseFloat(nuevoTotal);
                var devolverTotal = nuevoTotal - paga1 - paga2;
                var devolver1 = 0;
                nuevo1 = (nuevo1 != null && nuevo1 != undefined && nuevo1 != "" ? nuevo1.toString().replace('.', '').replace(',', '.') : 0);
                nuevo1 = parseFloat(nuevo1);
                devolver1 += devolver1 + nuevo1 - paga1;    //Number(nuevo1)
                var devolver2 = 0;
                nuevo2 = (nuevo2 != null && nuevo2 != undefined && nuevo2 != "" ? nuevo2.toString().replace('.', '').replace(',', '.') : 0);
                nuevo2 = parseFloat(nuevo2);
                devolver2 += devolver2 + nuevo2 - paga2;    //Number(nuevo2)

                document.getElementById('celdaD1').innerHTML = formatearNumeroAString(devolver1.toFixed(2));
                document.getElementById('celdaD2').innerHTML = formatearNumeroAString(devolver2.toFixed(2));
                document.getElementById('celdaD3').innerHTML = formatearNumeroAString(devolverTotal.toFixed(2));

            }

            function calculaDuracionEsperada() {
                var fechaInicial = document.getElementById("fechaInicio").value;
                var fechaFinal = document.getElementById("fechaFinEsperado").value;
                diasEsperados = calcularDias(fechaInicial, fechaFinal);

            }

            function calcularDuracionReal() {
                var fechaInicial = document.getElementById("fechaInicio").value;
                if (document.getElementById('fechaFinReal').value != null && document.getElementById('fechaFinReal').value != "") {
                    var fechaFinal = document.getElementById('fechaFinReal').value;
                    diasReales = calcularDias(fechaInicial, fechaFinal);
                } else {
                    jsp_alerta("A", "No se puede calcular los dias reales del contrato, no se ha introducido la Fecha Final");
                }

            }

            function calcularDias(fechaInicial, fechaFinal) {
                var resultado = "";
                var diferencia = 0;
                inicial = fechaInicial.split("/");
                final = fechaFinal.split("/");
                // obtenemos las fechas en milisegundos
                var dateStart = new Date(inicial[2], (inicial[1] - 1), inicial[0]);
                var dateEnd = new Date(final[2], (final[1] - 1), final[0]);
                if (dateStart < dateEnd) {
                    // la diferencia entre las dos fechas, la dividimos entre 86400 segundos
                    // que tiene un dia, y posteriormente entre 1000 ya que estamos
                    // trabajando con milisegundos.
                    resultado = "La diferencia es de " + (((dateEnd - dateStart) / 86400) / 1000) + " días";
                    diferencia = (((dateEnd - dateStart) / 86400) / 1000);
                } else {
                    resultado = "La fecha inicial es posterior a la fecha final";
                }
                var dif = diferencia.toFixed();
                //  jsp_alerta("A", dif);
                return dif;
            }

            function filaNuevoEstado2() {
            }

            function formatearNumeroAString(nStr) {
                nStr += '';
                var x = nStr.split('.');
                var x1 = x[0];
                var x3Min2dec = x.length > 1 ? x[1] : '00';
                x3Min2dec = (x3Min2dec.length == 1 ? ',' + x3Min2dec + '0' : ',' + x3Min2dec);
                var x2 = x.length > 1 ? ',' + x[1] : '';
                var rgx = /(\d+)(\d{3})/;
                while (rgx.test(x1)) {
                    x1 = x1.replace(rgx, '$1' + '.' + '$2');
                }
                return x1 + x3Min2dec; //x2;
            }


            function mostrarCalFechaReal(evento) {
                if (window.event)
                    evento = window.event;
                if (document.getElementById("calFechaReal").src.indexOf("icono.gif") != -1)
                    showCalendar('forms[0]', 'fechaFinReal', null, null, null, '', 'calFechaReal', '', null, null, null, null, null, null, null, null, evento);
            }

            function limpiarCampos() {
                document.getElementById('impCeldaN1').value = "";
                document.getElementById('impCeldaN2').value = "";
                document.getElementById('impCeldaN3').value = "";
                document.getElementById('fechaFinReal').value = "";
            }

            function calculoFinPrematuro() {
                if (document.getElementById('fechaFinReal').value != null && document.getElementById('fechaFinReal').value != "") {
                    calcularDuracionReal();
                    //   jsp_alerta("A","duracion real "+diasReales+ " - esperada : "+diasEsperados);
                    var concedido = '<%=impSuvencionIniTramResolEstTec%>';  //datosPestana.getImporteTotalIni()
                    // totalFin = concedido*(diasReales/diasEsperados)
                    var totalFin;
                    var nuevo1Fin;
                    var nuevo2Fin;
                    try {
                        var resul = parseFloat(concedido) * (parseFloat(diasReales) / parseFloat(diasEsperados));
                        totalFin = resul.toFixed(2);

                        var totalRedondo = totalFin.toString();
                        totalRedondo = (totalRedondo != null && totalRedondo != undefined ? totalRedondo.replace('.', ',') : '0');
                        document.getElementById('impCeldaN3').value = totalRedondo;
                        FormatNumber(document.getElementById('impCeldaN3').value, 6, 2, 'impCeldaN3');
                        //   document.getElementById('impCeldaN3').innerHTML =totalRedondo.toString();
                        //    jsp_alerta("A", "nuevo total redondo: " + totalRedondo);
                        //  document.getElementById('impCeldaN3').innerHTML =totalFin.toFixed(2);
                        // if (parseFloat(totalFin) > parseFloat(document.getElementById('celdaP1').value)) {
                        //     jsp_alerta("A", "nuevo > pagado");
                        //      nuevo1Fin = document.getElementById('celdaP1').value;
                        //    } else {
                        //       jsp_alerta("A", "nuevo < pagado");
                        //       nuevo1Fin = totalRedondo;
                        //  }
                        nuevo1Fin = document.getElementById('celdaP1').innerHTML;
// AJUSTES      
                        document.getElementById('impCeldaN1').value = nuevo1Fin;    //formatearNumeroAString(nuevo1Fin);
                        //FormatNumber(document.getElementById('impCeldaN1').value, 6, 2, 'impCeldaN1');
                        if (nuevo1Fin != null) {
                            nuevo1Fin = nuevo1Fin.replace('.', '');
                            nuevo1Fin = nuevo1Fin.replace(',', '.');
                        }
                        var resta = parseFloat(totalFin) - parseFloat(nuevo1Fin);
                        nuevo2Fin = resta.toFixed(2);
                        var nuevo2Fin1 = (nuevo2Fin != null && nuevo2Fin != undefined ? nuevo2Fin.replace('.', ',') : '0.00');
// AJUSTES       
                        document.getElementById('impCeldaN2').value = formatearNumeroAString(nuevo2Fin); //nuevo2Fin.toFixed(2);
                        //FormatNumber(document.getElementById('impCeldaN2').value, 6, 2, 'impCeldaN2');
                        // KEPA 
                        // calcularADevolver();
                    } catch (err) {

                    }
                } else {
                    jsp_alerta("A", "<%=meLanbide67I18n.getMensaje(idiomaUsuario,"msg.error.faltaInformar.campos")%><%=meLanbide67I18n.getMensaje(idiomaUsuario,"label.calculo.fechaFin")%>");
                }
            }

            function compruebaCampos() {
                if (document.getElementById('codListaCausas').value == null || document.getElementById('codListaCausas').value == undefined || document.getElementById('codListaCausas').value == "") {
                    //jsp_alerta("A", "No se puede llamar a la función de cálculo, no se ha introducido la CAUSA");
                    jsp_alerta("A", "<%=meLanbide67I18n.getMensaje(idiomaUsuario,"msg.error.faltaInformar.campos")%><%=meLanbide67I18n.getMensaje(idiomaUsuario,"label.calculo.causa")%>");
                    return false;
                }
                if (document.getElementById('codListaCausas').value == "3") {
                    if (document.getElementById('codListaMotivos').value == null || document.getElementById('codListaMotivos').value == undefined || document.getElementById('codListaMotivos').value == "") {
                        //jsp_alerta("A", "No se puede llamar a la función de cálculo, no se ha introducido el MOTIVO");
                        jsp_alerta("A", "<%=meLanbide67I18n.getMensaje(idiomaUsuario,"msg.error.faltaInformar.campos")%><%=meLanbide67I18n.getMensaje(idiomaUsuario,"label.calculo.motivo")%>");
                        return false;
                    }
                    if (document.getElementById('fechaInicio').value == null || document.getElementById('fechaInicio').value == "") {
                        //jsp_alerta("A", "No se puede llamar a la función de cálculo, no se ha introducido la Fecha Fin INICIO");
                        jsp_alerta("A", "<%=meLanbide67I18n.getMensaje(idiomaUsuario,"msg.error.faltaInformar.campos")%><%=meLanbide67I18n.getMensaje(idiomaUsuario,"label.calculo.fechaInicio")%>");
                        return false;
                    }
                    if (document.getElementById('fechaFinEsperado').value == null || document.getElementById('fechaFinEsperado').value == "" || document.getElementById('fechaFinEsperado').value == undefined) {
                        //jsp_alerta("A", "No se puede llamar a la función de cálculo, no se ha introducido la Fecha Fin ESPERADO");
                        jsp_alerta("A", "<%=meLanbide67I18n.getMensaje(idiomaUsuario,"msg.error.faltaInformar.campos")%><%=meLanbide67I18n.getMensaje(idiomaUsuario,"label.calculo.fechaFinEsperado")%>");
                        return false;
                    }
                    if (document.getElementById('codListaMotivos').value != "5") {
                        if (document.getElementById('fechaFinReal').value == null || document.getElementById('fechaFinReal').value == "" || document.getElementById('fechaFinReal').value == undefined) {
                            //jsp_alerta("A", "No se puede llamar a la función de cálculo, no se ha introducido la Fecha Fin REAL");
                            jsp_alerta("A", "<%=meLanbide67I18n.getMensaje(idiomaUsuario,"msg.error.faltaInformar.campos")%><%=meLanbide67I18n.getMensaje(idiomaUsuario,"label.calculo.fechaFin")%>");
                            return false;
                        }
                        if (document.getElementById('impCeldaN1').value == "0" || document.getElementById('impCeldaN2').value == "0") {
                            //   jsp_alerta("A", "No se puede llamar a la función de cálculo, no se han calculado los nuevos importes");
                            calculoFinPrematuro();
                            //  return false;
                        }
                    }
                }
                if (document.getElementById('codListaCausas').value == 5) {
                    //   nuevoTotal = (nuevoTotal == null || nuevoTotal == undefined || nuevoTotal == "") ? 0 : nuevoTotal;
                    var primerN = parseFloat(convertirANumero(document.getElementById('impCeldaN1').value));
                    primerN = (primerN == null || primerN == undefined || isNaN(primerN) || primerN == "") ? 0 : primerN;
                    var segundoN = parseFloat(convertirANumero(document.getElementById('impCeldaN2').value));
                    segundoN = (segundoN == null || segundoN == undefined || isNaN(segundoN) || segundoN == "") ? 0 : segundoN;
                    var totalN = parseFloat(convertirANumero(document.getElementById('impCeldaN3').value));
                    //      totalN = (totalN == null || totalN == undefined || totalN.isNaN || totalN == "") ? 0 : totalN;
                    var suma = Number(primerN) + Number(segundoN);
                    if (suma != totalN) {
                        //jsp_alerta("A", "No se puede llamar a la función de cálculo, la suma de los pagos no coincide con el Total");
                        jsp_alerta("A", "<%=meLanbide67I18n.getMensaje(idiomaUsuario,"msg.error.faltaInformar.campos")%><%=meLanbide67I18n.getMensaje(idiomaUsuario,"msg.error.faltaInformar.campos.sumaPago.DifTotal")%>");
                        return false;
                    }
                }
                return true;
            }

        </script>
    </head>
    <body>
        <div class="tab-page" id="tabPage673" style="height:480px; width: 100%;">
            <h2 class="tab" id="pestana673"><%=meLanbide67I18n.getMensaje(idiomaUsuario,"label.lak.tituloPestana")%></h2>
            <script type="text/javascript">tp1_p673 = tp1.addTabPage(document.getElementById("tabPage673"));</script>
            <div style="clear: both;">
                <div id="divGeneral" style="overflow-y: auto; overflow-x: hidden; height: 380px; padding: 20px;">
                    <div class="lineaFormulario">
                        <div style="width: 100px; float: left; text-align: left;">
                            <label class="etiqueta">
                                <%=meLanbide67I18n.getMensaje(idiomaUsuario,"label.calculo.causa")%>
                            </label>   
                        </div>
                        <div style="padding-left: 10px">
                            <input type="text" name="codListaCausas" id="codListaCausas" size="10" class="inputTexto" value="<%=datosPestana != null && datosPestana.getCodCausa() != null ? datosPestana.getCodCausa() : ""%>" onkeyup="xAMayusculas(this);"/>
                            <input type="text" name="descListaCausas" id="descListaCausas" size="50" class="inputTexto" readonly="true" value="<%=datosPestana != null && datosPestana.getDescCausa() != null ? datosPestana.getDescCausa() : ""%>"/>
                            <a href="" id="anchorListaCausas" name="anchorListaCausas">                                                
                                <span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonAplicacion" name="botonAplicacion" style="cursor:hand;"></span>
                            </a>
                        </div>
                    </div>
                    <div class="lineaFormulario" id="campoMotivo">
                        <div style="width: 100px; float: left; text-align: left;">
                            <label class="etiqueta">
                                <%=meLanbide67I18n.getMensaje(idiomaUsuario,"label.calculo.motivo")%> 
                            </label>
                        </div>
                        <div style="padding-left: 10px">
                            <input type="text" name="codListaMotivos" id="codListaMotivos" size="10" class="inputTexto" value="<%=datosPestana != null && datosPestana.getCodMotivo() != null ? datosPestana.getCodMotivo() : ""%>" onkeyup="xAMayusculas(this);"/>
                            <input type="text" name="descListaMotivos" id="descListaMotivos" size="50" class="inputTexto"  readonly="true" value="<%=datosPestana != null && datosPestana.getDescMotivo() != null ? datosPestana.getDescMotivo() : ""%>"/>
                            <a href="" id="anchorListaMotivos" name="anchorListaMotivos">                                                
                                <span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonAplicacion" name="botonAplicacion" style="cursor:hand;"></span>
                            </a>  
                        </div>
                    </div>  
                    <div class="lineaFormulario" id="campoFechas" style="width: 85%">
                        <table id="tablaFechas">
                            <tbody>
                                <tr>
                                    <td id="campoFInicio" style="width: 15%">
                                        <label class="etiqueta">
                                            <%=meLanbide67I18n.getMensaje(idiomaUsuario,"label.calculo.fechaInicio")%> 
                                        </label>  
                                    </td>
                                    <td style="padding-left: 10px">
                                        <input type="text" class="inputTxtFecha"
                                               id="fechaInicio" name="fechaInicio"
                                               maxlength="10" size="10"
                                               value="<%=datosPestana != null && datosPestana.getFechaInicio() != null ? datosPestana.getFechaInicio() :""%>"
                                               onkeyup = "return SoloCaracteresFechaLanbide(this);"                              
                                               onblur = "javascript:return comprobarFechaLak(this);"
                                               onfocus="javascript:this.select();"    readonly="true"
                                               disable="true"/>   
                                    </td>
                                    <td style="width: 5%"></td>
                                    <td id="campoFinEsperado"  style="width: 15%">
                                        <label class="etiqueta">
                                            <%=meLanbide67I18n.getMensaje(idiomaUsuario,"label.calculo.fechaFinEsperado")%> 
                                        </label>  
                                    </td>
                                    <td style="padding-left: 10px">
                                        <input type="text" class="inputTxtFecha"
                                               id="fechaFinEsperado" name="fechaFinEsperado"
                                               maxlength="10" size="10"
                                               value="<%=datosPestana != null && datosPestana.getFechaFinEsperada() != null ? datosPestana.getFechaFinEsperada() :""%>"                                         
                                               readonly="true"
                                               disable="true"/>   
                                    </td>
                                    <td style="width: 5%"></td>
                                    <td  id="campoFReal"  style="width: 15%">
                                        <label class="etiqueta">
                                            <%=meLanbide67I18n.getMensaje(idiomaUsuario,"label.calculo.fechaFin")%> 
                                        </label> 
                                    </td>
                                    <td style="padding-left: 10px">
                                        <input type="text" class="inputTxtFecha"
                                               id="fechaFinReal" name="fechaFinReal"
                                               maxlength="10" size="10"
                                               value=""
                                               onkeyup = "return SoloCaracteresFechaLanbide(this);"                              
                                               onblur = "javascript:return comprobarFechaLak(this);"
                                               onfocus="javascript:this.select();"
                                               onchange="calculoFinPrematuro();"
                                               disable="true"/>   
                                        <A href="javascript:calClick(event);return false;" onClick="mostrarCalFechaReal(event);
                                                return false;" style="text-decoration:none; padding-left: 5px">   <!-- onblur="javascript:return comprobarFecha(this);" onkeyup="return SoloCaracteresFecha(this);" -->
                                            <IMG style="border: 0px solid none" height="17" id="calFechaReal" name="calFechaReal" border="0" 
                                                 src="<c:url value='/images/calendario/icono.gif'/>" > <!--width="25"-->
                                        </A>
                                    </td>

                                </tr>
                            </tbody>
                        </table>
                    </div>
                    <br>
                    <br>
                    <div class="lineaFormulario">

                        <table style="width: 70%; table-layout:fixed" id="tablaImportes">

                            <tr>
                                <th class="etiqueta" style="width: 25%"><%=meLanbide67I18n.getMensaje(idiomaUsuario, "tabla.tablaImportes.col1")%></th>
                                <th class="etiqueta" style="width: 25%"><%=meLanbide67I18n.getMensaje(idiomaUsuario, "tabla.tablaImportes.col2")%></th>
                                <th class="etiqueta" style="width: 25%"><%=meLanbide67I18n.getMensaje(idiomaUsuario, "tabla.tablaImportes.col3")%></th>
                                <th class="etiqueta" style="width: 25%"><%=meLanbide67I18n.getMensaje(idiomaUsuario, "tabla.tablaImportes.col4")%></th>
                            </tr>
                            <tr id="filaConcedido" style="border: 1px solid #004595; height: 40px">
                                <th scope="row" class="etiqueta">
                                    <%=meLanbide67I18n.getMensaje(idiomaUsuario, "tabla.tablaImportes.filaConcedido")%>
                                </th>
                                <td style="text-align: right; border: 1px solid #004595" id="celdaC1" class="inputTexto" type="inputTexto"><%=datosPestana != null && datosPestana.getPago1() != null ? formateador.format(Float.valueOf(datosPestana.getPago1())) :""%></td>
                                <td style="text-align: right; border: 1px solid #004595" id="celdaC2" class="inputTexto"><%=datosPestana != null && datosPestana.getPago2() != null ?  formateador.format(Float.valueOf(datosPestana.getPago2())):""%></td>
                                <td style="text-align: right; border: 1px solid #004595" id="celdaC3" class="inputTexto"><%=datosPestana != null && datosPestana.getImporteTotalIni() != null ? formateador.format(Float.valueOf(datosPestana.getImporteTotalIni())) :""%></td>
                            </tr>                        
                            <tr id="filaPagado" style="border: 1px solid #004595; height: 40px">
                                <th scope="row" class="etiqueta">
                                    <%=meLanbide67I18n.getMensaje(idiomaUsuario, "tabla.tablaImportes.filaPagado")%>
                                </th>
                                <td style="text-align: right; border: 1px solid #004595" id="celdaP1" class="inputTexto"><%=datosPestana != null && datosPestana.getPagado1() != null ? formateador.format(Float.valueOf(datosPestana.getPagado1())) :""%></td>
                                <td style="text-align: right; border: 1px solid #004595" id="celdaP2" class="inputTexto"><%=datosPestana != null && datosPestana.getPagado2() != null ? formateador.format(Float.valueOf(datosPestana.getPagado2())) :""%></td>
                                <td style="text-align: right; border: 1px solid #004595" id="celdaP3" class="inputTexto"><%=datosPestana != null && datosPestana.getTotalPagado() != null ? formateador.format(Float.valueOf(datosPestana.getTotalPagado())) :""%></td>
                            </tr>
                            <tr id="filaNuevo" style="border: 1px solid #004595; height: 40px">
                                <th scope="row" class="etiqueta">
                                    <%=meLanbide67I18n.getMensaje(idiomaUsuario, "tabla.tablaImportes.filaNuevo")%>
                                </th>
                                <td style="text-align: right; border: 1px solid #004595" type="inputTexto" id="celdaN1">
                                    <input type="text"  class="inputTexto" id="impCeldaN1" name="impCeldaN1"  
                                           value="<%=datosPestana != null && datosPestana.getPagoNuevo1() != null ? formateador.format(Float.valueOf(datosPestana.getPagoNuevo1())) :""%>"
                                           style="text-align: right;" 
                                           onkeyup="FormatNumber(this.value, 6, 0, this.id);"
                                           />
                                </td>
                                <td style="text-align: right; border: 1px solid #004595"  type="inputTexto" id="celdaN2">
                                    <input type="text"  class="inputTexto" id="impCeldaN2" name="impCeldaN2" 
                                           value="<%=datosPestana != null && datosPestana.getPagoNuevo2() != null ? datosPestana.getPagoNuevo2() :""%>"
                                           style="text-align: right;" 
                                           onkeyup="FormatNumber(this.value, 6, 0, this.id);"/>
                                </td>
                                <td style="text-align: right; border: 1px solid #004595" type="inputTexto" id="celdaN3">
                                    <input type="text"  class="inputTexto" id="impCeldaN3" name="impCeldaN3" 
                                           value="<%=datosPestana != null && datosPestana.getTotalNuevo() != null ? datosPestana.getTotalNuevo() :""%>"
                                           style="text-align: right;" 
                                           onkeyup="FormatNumber(this.value, 6, 0, this.id);"/>  
                                </td>
                            </tr>
                            <tr id="filaDevolver" style="border: 1px solid #004595; height: 40px">
                                <th scope="row" class="etiqueta">
                                    <%=meLanbide67I18n.getMensaje(idiomaUsuario, "tabla.tablaImportes.filaDevolver")%>
                                </th>
                                <td style="text-align: right; border: 1px solid #004595" id="celdaD1" class="inputTexto"><%=datosPestana != null && datosPestana.getDevolver1() != null ? formateador.format(Float.valueOf(datosPestana.getDevolver1())) :""%></td>
                                <td style="text-align: right; border: 1px solid #004595" id="celdaD2" class="inputTexto"><%=datosPestana != null && datosPestana.getDevolver2() != null ? formateador.format(Float.valueOf(datosPestana.getDevolver2())) :""%></td>
                                <td style="text-align: right; border: 1px solid #004595" id="celdaD3" class="inputTexto"><%=datosPestana != null && datosPestana.getDevolverTotal() != null ? formateador.format(Float.valueOf(datosPestana.getDevolverTotal())) :""%></td>
                            </tr>
                        </table>

                    </div>

                    <div style="clear: both;" align="left">
                        <br>                
                        <div class="botonera">
                            <input type="button" id="btnCalcular" name="btnCalcular" class="botonGeneral" value="<%=meLanbide67I18n.getMensaje(idiomaUsuario, "btn.calcular")%>" onclick="pulsarCalcular(true);"/>
                        </div>
                    </div>   
                </div>    
            </div>
        </div>   
    </body>

    <script type="text/javascript">
        estadoInicial = '<%=estadoInicial%>';


        listaCodCausas[0] = "1";
        listaCodCausas[1] = "2";
        listaCodCausas[2] = "3";
        listaCodCausas[3] = "4";
        listaCodCausas[4] = "5";

        listaDescCausas[0] = "RENUNCIA";
        listaDescCausas[1] = "NO JUSTIFICA";
        listaDescCausas[2] = "FIN CONTRATO PREMATURO";
        listaDescCausas[3] = "RECURSO";
        listaDescCausas[4] = "OTRAS CAUSAS";

        listaCodMotivos[0] = "1";
        listaCodMotivos[1] = "2";
        listaCodMotivos[2] = "3";
        listaCodMotivos[3] = "4";
        listaCodMotivos[4] = "5";

        listaDescMotivos[0] = "NO SUPERAR EL PERIODO DE PRUEBA";
        listaDescMotivos[1] = "CESE VOLUNTARIO";
        listaDescMotivos[2] = "DESPIDO DECLARADO PROCEDENTE";
        listaDescMotivos[3] = "MUERTE O INVALIDEZ DE LA PERSONA CONTRATADA";
        listaDescMotivos[4] = "OTROS MOTIVOS";

        comboListaCausas = new Combo("ListaCausas");
        comboListaCausas.addItems(listaCodCausas, listaDescCausas);
        comboListaCausas.change = cargarDatosCausas;

        comboListaMotivos = new Combo("ListaMotivos");
        comboListaMotivos.addItems(listaCodMotivos, listaDescMotivos);
        comboListaMotivos.change = cargarDatosMotivos;


        deshabilitarCampos();
        // Inicializamos los valores fila a devolver la primera vez que entramos
        document.getElementById('celdaD1').innerHTML = '0,00';
        document.getElementById('celdaD2').innerHTML = '0,00';
        document.getElementById('celdaD3').innerHTML = '0,00';
// AJUSTES
        document.getElementById('impCeldaN1').value = '';
        document.getElementById('impCeldaN2').value = '';
        compruebaCausa(false);
        //  calcularADevolver();
        calculaDuracionEsperada();
    </script>
</html>
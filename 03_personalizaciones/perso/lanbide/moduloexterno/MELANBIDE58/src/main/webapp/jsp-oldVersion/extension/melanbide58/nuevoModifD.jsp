<%-- 
    Document   : nuevoModifD
    Created on : 16-sep-2019, 11:01:38
    Author     : Kepa
--%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>

<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide58.i18n.MeLanbide58I18n" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide58.vo.PensionistaVO" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide58.vo.DesplegableAdmonLocalVO" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide58.util.ConstantesMeLanbide58" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide58.util.ConfigurationParameter" %>
<%@page import="java.util.ArrayList" %>
<%@page import="java.util.List" %>
<%@page import="java.text.SimpleDateFormat" %>
<html>
    <head>
        <meta http-equiv="X-UA-Compatible" content="IE=Edge" />
        <%
             PensionistaVO datModif = new PensionistaVO();
             PensionistaVO objectVO = new PensionistaVO();
            
             String codOrganizacion = "";
             String nuevo = "";
             String expediente = "";
             String fechaIni = "";
             String fechaFin = "";
             String fechaInss = "";
             String fechaSin = "";
             String fechaCon = "";

             MeLanbide58I18n meLanbide58I18n = MeLanbide58I18n.getInstance();
         //ConstantesMeLanbide58 _constantesMeLanbide58 = ConstantesMeLanbide58();
             expediente = (String)request.getAttribute("numExp");
             int idiomaUsuario = 1;
             int apl = 5;
             String css = "";
             try{
                 UsuarioValueObject usuario = new UsuarioValueObject();
                 try{
                     if (session != null){
                         if (usuario != null){
                             usuario = (UsuarioValueObject) session.getAttribute("usuario");
                             idiomaUsuario = usuario.getIdioma();
                             apl = usuario.getAppCod();
                             css = usuario.getCss();
                         }
                     }
                 }catch(Exception  ex){
                 }
                 codOrganizacion  = request.getParameter("codOrganizacionModulo");
                 nuevo = (String)request.getAttribute("nuevo");
                 if(request.getAttribute("datModif") != null){
                     datModif = (PensionistaVO)request.getAttribute("datModif");
                     SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
                     if (datModif.getFechaIni()!=null){
                         fechaIni = formatoFecha.format(datModif.getFechaIni());
                     }
                     if (datModif.getFechaFin()!=null){
                         fechaFin = formatoFecha.format(datModif.getFechaFin());
                     }
                     if (datModif.getFechaEfecInss()!=null){
                         fechaInss = formatoFecha.format(datModif.getFechaEfecInss());
                     }
                     if (datModif.getFechaEfecSinE()!=null){
                         fechaSin = formatoFecha.format(datModif.getFechaEfecSinE());
                     }
                     if (datModif.getFechaEfecConE()!=null){
                         fechaCon = formatoFecha.format(datModif.getFechaEfecConE());
                     }
                 }
             }catch(Exception ex){
             }
        %>
        <jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
        <jsp:setProperty name="descriptor"  property="idi_cod" value="<%=idiomaUsuario%>" />
        <jsp:setProperty name="descriptor"  property="apl_cod" value="<%=apl%>" />
        <link rel="StyleSheet" media="screen" type="text/css" href="<%=request.getContextPath()%><%=css%>">
        <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/css/font-awesome.min.css'/>" media="screen">
        <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/less/animated.less'/>" media="screen">
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script>
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/>
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/extension/melanbide58/melanbide58.css"/>
        <script type="text/javascript">
            var APP_CONTEXT_PATH = '<%=request.getContextPath()%>';
        </script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/general.js"></script>
        <script type="text/javascript" src="<c:url value='/scripts/listaComboBox.js'/>"></script>
        <script type="text/javascript" src="<c:url value='/scripts/calendario.js'/>"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/validaciones.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/popup.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/meLanbide58/JavaScriptUtil.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/meLanbide58/Parsers.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/meLanbide58/InputMask.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/meLanbide58/lanbide.js"></script>
        <script type="text/javascript">
            var mensajeValidacion = '';

            var comboListaTipoConD;
            var listaCodigosTipoConD = new Array();
            var listaDescripcionesTipoConD = new Array();

            var comboListaTipoJorD;
            var listaCodigosTipoJorD = new Array();
            var listaDescripcionesTipoJorD = new Array();

            function buscaCodigoTipoConD(codTipoConD) {
                comboListaTipoConD.buscaCodigo(codTipoConD);
            }

            function cargarDatosTipoConD() {
                var codTipoConDSeleccionado = document.getElementById("codListaTipoConD").value;
                buscaCodigoTipoConD(codTipoConDSeleccionado);
            }

            function buscaCodigoTipoJorD(codTipoJorD) {
                comboListaTipoJorD.buscaCodigo(codTipoJorD);
            }

            function cargarDatosTipoJorD() {
                var codTipoJorDSeleccionado = document.getElementById("codListaTipoJorD").value;
                buscaCodigoTipoJorD(codTipoJorDSeleccionado);
            }

            function mostrarCalFechaIniD(evento) {
                if (window.event)
                    evento = window.event;
                if (document.getElementById("calFechaIniD").src.indexOf("icono.gif") != -1)
                    showCalendar('forms[0]', 'fechaInicioD', null, null, null, '', 'calFechaIniD', '', null, null, null, null, null, null, null, null, evento);
            }

            function mostrarCalFechaFinD(evento) {
                if (window.event)
                    evento = window.event;
                if (document.getElementById("calFechaFinD").src.indexOf("icono.gif") != -1)
                    showCalendar('forms[0]', 'fechaFinD', null, null, null, '', 'calFechaFinD', '', null, null, null, null, null, null, null, null, evento);
            }

            function mostrarCalFechaInssD(evento) {
                if (window.event)
                    evento = window.event;
                if (document.getElementById("calFechaInssD").src.indexOf("icono.gif") != -1)
                    showCalendar('forms[0]', 'fechaInssD', null, null, null, '', 'calFechaInssD', '', null, null, null, null, null, null, null, null, evento);
            }

            function mostrarCalFechaSinD(evento) {
                if (window.event)
                    evento = window.event;
                if (document.getElementById("calFechaSinD").src.indexOf("icono.gif") != -1)
                    showCalendar('forms[0]', 'fechaSinD', null, null, null, '', 'calFechaSinD', '', null, null, null, null, null, null, null, null, evento);
            }

            function mostrarCalFechaConD(evento) {
                if (window.event)
                    evento = window.event;
                if (document.getElementById("calFechaConD").src.indexOf("icono.gif") != -1)
                    showCalendar('forms[0]', 'fechaConD', null, null, null, '', 'calFechaConD', '', null, null, null, null, null, null, null, null, evento);
            }
            function rellenarDatModificar() {
                if ('<%=datModif%>' != null) {
                    buscaCodigoTipoConD('<%=datModif.getTipoContrato() != null ? datModif.getTipoContrato() : ""%>');
                    buscaCodigoTipoJorD('<%=datModif.getTipoContDurac() != null ? datModif.getTipoContDurac() : ""%>');
                } else {
                    alert('No hemos podido cargar los datos para modificar');
                }
            }

            function guardarDatos() {
                mensajeValidacion = "";
                if (validarDatosNumericosVacios() && validarCampos()) {
                    var ajax = getXMLHttpRequest();
                    var nodos = null;
                    var CONTEXT_PATH = '<%=request.getContextPath()%>';
                    var url = CONTEXT_PATH + "/PeticionModuloIntegracion.do";
                    var parametros = "";
                    var nuevo = "<%=nuevo%>";
                    var nombre = "";
                    var ape = "";
                    if (document.getElementById('nombre').value == null || document.getElementById('nombre').value == '') {
                        nombre = "";
                    } else {
                        nombre = document.getElementById('nombre').value.replace(/\'/g, "''");
                    }
                    if (document.getElementById('apellidos').value == null || document.getElementById('apellidos').value == '') {
                        ape = "";
                    } else {
                        ape = document.getElementById('apellidos').value.replace(/\'/g, "''");
                    }
                    if (nuevo != null && nuevo == "1") {
                        parametros = "tarea=preparar&modulo=MELANBIDE58&operacion=crearNuevoPensionista&tipo=0"
                                + "&expediente=<%=expediente%>"
                                + "&numLinea=" + document.getElementById('numLinea').value
                                + "&dni=" + document.getElementById('dni_nif').value
                                + '&nombre=' + escape(nombre)
                                + '&apellidos=' + escape(ape)
                                + "&codTipoCon=" + document.getElementById('codListaTipoConD').value
                                + "&codTipoJor=" + document.getElementById('codListaTipoJorD').value
                                + "&porcJornada=" + document.getElementById('porcJornadaD').value
                                + "&fecInicio=" + document.getElementById('fechaInicioD').value
                                + "&fecFin=" + document.getElementById('fechaFinD').value
                                + "&fecInss=" + document.getElementById('fechaInssD').value
                                + "&fecSin=" + document.getElementById('fechaSinD').value
                                + "&fecCon=" + document.getElementById('fechaConD').value
                                + "&grado=" + document.getElementById('gradoD').value
                                + '&tabla=3'
                                ;
                    } else {
                        parametros = "tarea=preparar&modulo=MELANBIDE58&operacion=modificarPensionista&tipo=0"
                                + "&id=<%=datModif != null && datModif.getId() != null ? datModif.getId().toString() : ""%>"
                                + "&expediente=<%=datModif != null && datModif.getNumExp() != null ? datModif.getNumExp().toString() : ""%>"
                                + "&numLinea=" + document.getElementById('numLinea').value
                                + "&dni=" + document.getElementById('dni_nif').value
                                + '&nombre=' + escape(nombre)
                                + '&apellidos=' + escape(ape)
                                + "&codTipoCon=" + document.getElementById('codListaTipoConD').value
                                + "&codTipoJor=" + document.getElementById('codListaTipoJorD').value
                                + "&porcJornada=" + document.getElementById('porcJornadaD').value
                                + "&fecInicio=" + document.getElementById('fechaInicioD').value
                                + "&fecFin=" + document.getElementById('fechaFinD').value
                                + "&fecInss=" + document.getElementById('fechaInssD').value
                                + "&fecSin=" + document.getElementById('fechaSinD').value
                                + "&fecCon=" + document.getElementById('fechaConD').value
                                + "&grado=" + document.getElementById('gradoD').value
                                + '&tabla=3'
                                ;
                    }
                    try {
                        ajax.open("POST", url, false);
                        ajax.setRequestHeader("Content-Type", "application/x-www-form-urlencoded; charset=ISO-8859-1");
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
                        var lista = new Array();
                        var fila = new Array();
                        var nodoFila;
                        var hijosFila;
                        for (j = 0; hijos != null && j < hijos.length; j++) {
                            if (hijos[j].nodeName == "CODIGO_OPERACION") {
                                codigoOperacion = hijos[j].childNodes[0].nodeValue;
                                lista[j] = codigoOperacion;
                            }//if(hijos[j].nodeName=="CODIGO_OPERACION")
                            else if (hijos[j].nodeName == "FILA") {
                                nodoFila = hijos[j];
                                hijosFila = nodoFila.childNodes;
                                for (var cont = 0; cont < hijosFila.length; cont++) {
                                    if (hijosFila[cont].nodeName == "ID") {
                                        if (hijosFila[cont].childNodes.length > 0) {
                                            fila[0] = hijosFila[cont].childNodes[0].nodeValue;
                                        } else {
                                            fila[0] = '-';
                                        }
                                    } else if (hijosFila[cont].nodeName == "NUMLINEA") {
                                        if (hijosFila[cont].childNodes[0].nodeValue != "null") {
                                            fila[1] = hijosFila[cont].childNodes[0].nodeValue;
                                        } else {
                                            fila[1] = '-';
                                        }
                                    } else if (hijosFila[cont].nodeName == "APELLIDOS") {
                                        if (hijosFila[cont].childNodes[0].nodeValue != "null") {
                                            fila[2] = hijosFila[cont].childNodes[0].nodeValue;
                                        } else {
                                            fila[2] = '-';
                                        }
                                    } else if (hijosFila[cont].nodeName == "NOMBRE") {
                                        if (hijosFila[cont].childNodes[0].nodeValue != "null") {
                                            fila[3] = hijosFila[cont].childNodes[0].nodeValue;
                                        } else {
                                            fila[3] = '-';
                                        }
                                    } else if (hijosFila[cont].nodeName == "DNI") {
                                        if (hijosFila[cont].childNodes[0].nodeValue != "null") {
                                            fila[4] = hijosFila[cont].childNodes[0].nodeValue;
                                        } else {
                                            fila[4] = '-';
                                        }
                                    } else if (hijosFila[cont].nodeName == "TIPOCON") {
                                        if (hijosFila[cont].childNodes[0].nodeValue != "null") {
                                            fila[5] = hijosFila[cont].childNodes[0].nodeValue;
                                        } else {
                                            fila[5] = '-';
                                        }
                                    } else if (hijosFila[cont].nodeName == "TIPOCON_DESC") {
                                        if (hijosFila[cont].childNodes[0].nodeValue != "null") {
                                            fila[6] = hijosFila[cont].childNodes[0].nodeValue;
                                        } else {
                                            fila[6] = '-';
                                        }
                                    } else if (hijosFila[cont].nodeName == "TIPOJOR") {
                                        if (hijosFila[cont].childNodes[0].nodeValue != "null") {
                                            fila[7] = hijosFila[cont].childNodes[0].nodeValue;
                                        } else {
                                            fila[7] = '-';
                                        }
                                    } else if (hijosFila[cont].nodeName == "TIPOJOR_DESC") {
                                        if (hijosFila[cont].childNodes[0].nodeValue != "null") {
                                            fila[8] = hijosFila[cont].childNodes[0].nodeValue;
                                        } else {
                                            fila[8] = '-';
                                        }
                                    } else if (hijosFila[cont].nodeName == "FECINICIO") {
                                        if (hijosFila[cont].childNodes.length > 0) {
                                            fila[9] = hijosFila[cont].childNodes[0].nodeValue;
                                        } else {
                                            fila[9] = '-';
                                        }
                                    } else if (hijosFila[cont].nodeName == "FECFIN") {
                                        if (hijosFila[cont].childNodes.length > 0) {
                                            fila[10] = hijosFila[cont].childNodes[0].nodeValue;
                                        } else {
                                            fila[10] = '-';
                                        }
                                    } else if (hijosFila[cont].nodeName == "FECINSS") {
                                        if (hijosFila[cont].childNodes.length > 0) {
                                            fila[11] = hijosFila[cont].childNodes[0].nodeValue;
                                        } else {
                                            fila[11] = '-';
                                        }
                                    } else if (hijosFila[cont].nodeName == "FECSIN") {
                                        if (hijosFila[cont].childNodes.length > 0) {
                                            fila[12] = hijosFila[cont].childNodes[0].nodeValue;
                                        } else {
                                            fila[12] = '-';
                                        }
                                    } else if (hijosFila[cont].nodeName == "FECCON") {
                                        if (hijosFila[cont].childNodes.length > 0) {
                                            fila[13] = hijosFila[cont].childNodes[0].nodeValue;
                                        } else {
                                            fila[13] = '-';
                                        }
                                    } else if (hijosFila[cont].nodeName == "GRADO") {
                                        if (hijosFila[cont].childNodes[0].nodeValue != "null" && hijosFila[cont].childNodes[0].nodeValue != "") {
                                            fila[14] = hijosFila[cont].childNodes[0].nodeValue;
                                            var tex = fila[14].toString();
                                            tex = tex.replace(".", ",");
                                            fila[14] = tex;

                                        } else {
                                            fila[14] = '-';
                                        }
                                    } else if (hijosFila[cont].nodeName == "PORCJORNADA") {
                                        if (hijosFila[cont].childNodes[0].nodeValue != "null" && hijosFila[cont].childNodes[0].nodeValue != "") {
                                            fila[15] = hijosFila[cont].childNodes[0].nodeValue;
                                            var text = fila[15].toString();
                                            text = text.replace(".", ",");
                                            fila[15] = text;

                                        } else {
                                            fila[15] = '-';
                                        }
                                    }
                                }// for elementos de la fila
                                lista[j] = fila;
                                fila = new Array();
                            }
                        }//for(j=0;hijos!=null && j<hijos.length;j++)

                        if (codigoOperacion == "0") {
                            self.parent.opener.retornoXanelaAuxiliar(lista);
                            cerrarVentana();
                        } else if (codigoOperacion == "1") {
                            jsp_alerta("A", '<%=meLanbide58I18n.getMensaje(idiomaUsuario,"error.errorBD")%>');
                        } else if (codigoOperacion == "2") {
                            jsp_alerta("A", '<%=meLanbide58I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                        } else if (codigoOperacion == "3") {
                            jsp_alerta("A", '<%=meLanbide58I18n.getMensaje(idiomaUsuario,"error.pasoParametros")%>');
                        } else {
                            jsp_alerta("A", '<%=meLanbide58I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                        }
                    } catch (Err) {
                        //   jsp_alerta("A", 'KO - ' + Err.toString());
                    }//try-catch
                } else {
                    jsp_alerta("A", mensajeValidacion);
                }
            }


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
            function cancelar() {
                var resultado = jsp_alerta('', '<%=meLanbide58I18n.getMensaje(idiomaUsuario, "msg.preguntaCancelar")%>');
                if (resultado == 1) {
                    cerrarVentana();
                }
            }

            function cerrarVentana() {
                if (navigator.appName == "Microsoft Internet Explorer") {
                    window.parent.window.opener = null;
                    window.parent.window.close();
                } else if (navigator.appName == "Netscape") {
                    top.window.opener = top;
                    top.window.open('', '_parent', '');
                    top.window.close();
                } else {
                    window.close();
                }
            }

            function validarCampos() {
                mensajeValidacion = "";
                var correcto = true;
                if (document.getElementById('nombre').value == null || document.getElementById('nombre').value == '') {
                    mensajeValidacion = '<%=meLanbide58I18n.getMensaje(idiomaUsuario, "msg.nombreObligatorio")%>';
                    return false;
                }
                if (document.getElementById('apellidos').value == null || document.getElementById('apellidos').value == '') {
                    mensajeValidacion = '<%=meLanbide58I18n.getMensaje(idiomaUsuario, "msg.apellido1")%>';
                    return false;
                }
                if (document.getElementById('dni_nif').value == null || document.getElementById('dni_nif').value == '') {
                    mensajeValidacion = '<%=meLanbide58I18n.getMensaje(idiomaUsuario, "msg.dniObligatorio")%>';
                    return false;
                }
                if (document.getElementById('codListaTipoConD').value == null || document.getElementById('codListaTipoConD').value == '') {
                    mensajeValidacion = '<%=meLanbide58I18n.getMensaje(idiomaUsuario, "msg.tipoCon")%>';
                    return false;
                }
                if (document.getElementById('codListaTipoJorD').value == null || document.getElementById('codListaTipoJorD').value == '') {
                    mensajeValidacion = '<%=meLanbide58I18n.getMensaje(idiomaUsuario, "msg.tipoJor")%>';
                    return false;
                }
                return correcto;
            }

            function validarDatosNumericosVacios() {
                mensajeValidacion = "";
                var correcto = true;
                if (document.getElementById('numLinea').value == null || document.getElementById('numLinea').value == '') {
                    mensajeValidacion = '<%=meLanbide58I18n.getMensaje(idiomaUsuario, "msg.numLineaObligatorio")%>';
                    return false;
                } else {
                    if (Trim(document.getElementById('numLinea').value) != '') {
                        if (/^([0-9])*$/.test(document.getElementById('numLinea').value)) {
                        } else {
                            mensajeValidacion = '<%=meLanbide58I18n.getMensaje(idiomaUsuario, "msg.numLinea.errNumerico")%>';
                            return false;
                        }
                    }
                }
                if (document.getElementById('gradoD').value == null || document.getElementById('gradoD').value == '') {
                    mensajeValidacion = '<%=meLanbide58I18n.getMensaje(idiomaUsuario, "msg.grado")%>';
                    return false;
                } else {
                    if (!validarNumericoDecimalPrecision(document.getElementById('gradoD').value, 5, 2)) {
                        mensajeValidacion = '<%=meLanbide58I18n.getMensaje(idiomaUsuario, "msg.grado.errNumerico")%>';
                        return false;
                    } else {
                        if (!validarNumericoPorcentajeCien(document.getElementById('gradoD').value)) {
                            mensajeValidacion = '<%=meLanbide58I18n.getMensaje(idiomaUsuario, "msg.grado.errRango")%>';
                            return false;
                        }
                    }
                }
                if (document.getElementById('porcJornadaD').value == null || document.getElementById('porcJornadaD').value == '') {

                } else {
                    if (!validarNumericoDecimalPrecision(document.getElementById('porcJornadaD').value, 5, 2)) {
                        mensajeValidacion = '<%=meLanbide58I18n.getMensaje(idiomaUsuario, "msg.porcJornada.errNumerico")%>';
                        return false;
                    } else {
                        if (!validarNumericoPorcentajeCien(document.getElementById('porcJornadaD').value)) {
                            mensajeValidacion = '<%=meLanbide58I18n.getMensaje(idiomaUsuario, "msg.porcJornada.errRango")%>';
                            return false;
                        }
                    }
                }
                return correcto;
            }

            function validarNumericoDecimalPrecision(numero, longTotal, longParteDecimal) {
                try {
                    var longParteEntera = parseInt(longTotal) - parseInt(longParteDecimal);
                    if (Trim(numero) != '') {
                        var valor = numero;
                        var pattern = '^[0-9]{1,' + longParteEntera + '}(,[0-9]{1,' + longParteDecimal + '})?$';
                        var regex = new RegExp(pattern);
                        //var result = valor.match(regex);
                        var result = regex.test(valor);
                        return result;
                        //return /^[0-]{1,}(,[0-9]{1,longParteDecimal})?$/.test(numero.value);
                    } else {
                        //alert("TRUEEEEEEE");
                        return true;
                    }
                } catch (err) {
                    alert(err);
                    return false;
                }
            }

            function validarNumericoPorcentajeCien(numero) {
                try {
                    if (numero < 0 || numero > 100) {
                        return false;
                    } else {
                        return true;
                    }
                } catch (err) {
                    return false;
                }
            }

            function validarNumerico(numero) {
                try {
                    if (Trim(numero.value) != '') {
                        return /^([0-9])*$/.test(numero.value);
                    }
                } catch (err) {
                    return false;
                }
            }

            function comprobarFechaLanbide(inputFecha) {
                var formato = 'dd/mm/yyyy';
                if (Trim(inputFecha.value) != '') {
                    var D = ValidarFechaConFormatoLanbide(inputFecha.value, formato);
                    if (!D[0]) {
                        jsp_alerta("A", "<%=meLanbide58I18n.getMensaje(idiomaUsuario, "msg.fechaNoVal")%>");
                        document.getElementById(inputFecha.name).focus();
                        document.getElementById(inputFecha.name).select();
                        return false;
                    } else {
                        inputFecha.value = D[1];
                        return true;
                    }//if (!D[0])
                }//if (Trim(inputFecha.value)!='')
                return true;
            }//comprobarFechaLanbide

            function reemplazarPuntos(campo) {
                try {
                    var valor = campo.value;
                    if (valor != null && valor != '') {
                        valor = valor.replace(/\./g, ',');
                        campo.value = valor;
                    }
                } catch (err) {
                }
            }
        </script>        
    </head>
    <body>
        <div class="contenidoPantalla">
            <form>
                <div style="width: 100%; padding: 10px; text-align: left;">
                    <div  class="sub3titulo" style="clear: both; text-align: center; font-size: 14px;">
                        <span>
                            <%=meLanbide58I18n.getMensaje(idiomaUsuario,"label.nuevoModifD")%>
                        </span>
                    </div>
                    <br><br>
                    <div class="lineaFormulario">
                        <div style="width: 190px; float: left;" class="etiqueta">
                            <%=meLanbide58I18n.getMensaje(idiomaUsuario,"label.numLinea")%>
                        </div>
                        <div style="width: 450px; float: left;">
                            <div style="float: left;">
                                <input id="numLinea" name="numLinea" type="text" class="inputTexto" size="15" maxlength="5"
                                       value="<%=datModif != null && datModif.getNumLinea() != null ? datModif.getNumLinea() : ""%>"/>
                            </div>
                        </div>
                    </div>
                    <br><br>
                    <div class="lineaFormulario">
                        <div style="width: 190px; float: left;" class="etiqueta">
                            <%=meLanbide58I18n.getMensaje(idiomaUsuario,"label.dni_nif")%>
                        </div>
                        <div style="width: 450px; float: left;">
                            <div style="float: left;">
                                <input id="dni_nif" name="dni_nif" type="text" class="inputTexto" size="15" maxlength="15"
                                       value="<%=datModif != null && datModif.getNif() != null ? datModif.getNif() : ""%>"/>
                            </div>
                        </div>
                    </div><br><br>
                    <div class="lineaFormulario">
                        <div style="width: 190px; float: left;" class="etiqueta">
                            <%=meLanbide58I18n.getMensaje(idiomaUsuario,"label.nombre")%>
                        </div>
                        <div style="width: 450px; float: left;">
                            <div style="float: left;">
                                <input id="nombre" name="nombre" type="text" class="inputTexto" size="30" maxlength="50"
                                       value="<%=datModif != null && datModif.getNombre() != null ? datModif.getNombre() : ""%>"/>
                            </div>
                        </div>
                    </div><br><br>
                    <div class="lineaFormulario">
                        <div style="width: 190px; float: left;" class="etiqueta">
                            <%=meLanbide58I18n.getMensaje(idiomaUsuario,"label.apellidos")%>
                        </div>
                        <div style="width: 450px; float: left;">
                            <div style="float: left;">
                                <input id="apellidos" name="apellidos" type="text" class="inputTexto" size="30" maxlength="50"
                                       value="<%=datModif != null && datModif.getApellidos() != null ? datModif.getApellidos() : ""%>"/>
                            </div>
                        </div>
                    </div>
                    <br><br>
                    <div class="lineaFormulario">
                        <div style="width: 190px; float: left;" class="etiqueta">
                            <%=meLanbide58I18n.getMensaje(idiomaUsuario,"label.tipoContrato")%>
                        </div>
                        <div>
                            <div>
                                <input type="text" name="codListaTipoConD" id="codListaTipoConD" size="10" class="inputTexto" value="" onkeyup="xAMayusculas(this);" />
                                <input type="text" name="descListaTipoConD"  id="descListaTipoConD" size="60" class="inputTexto" readonly="true" value="" />
                                <a href="" id="anchorListaTipoConD" name="anchorListaTipoConD">
                                    <span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonAplicacion"
                                          name="botonAplicacion" style="cursor:hand;"></span>
                                </a>
                            </div>
                        </div>
                    </div><br>
                    <div class="lineaFormulario">
                        <div style="width: 190px; float: left;" class="etiqueta">
                            <%=meLanbide58I18n.getMensaje(idiomaUsuario,"label.tipoJornada")%>
                        </div>
                        <div>
                            <div>
                                <input type="text" name="codListaTipoJorD" id="codListaTipoJorD" size="10" class="inputTexto" value="" onkeyup="xAMayusculas(this);" />
                                <input type="text" name="descListaTipoJorD"  id="descListaTipoJorD" size="60" class="inputTexto" readonly="true" value="" />
                                <a href="" id="anchorListaTipoJorD" name="anchorListaTipoJorD">
                                    <span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonAplicacion"
                                          name="botonAplicacion" style="cursor:hand;"></span>
                                </a>
                            </div>
                        </div>
                    </div><br>
                    <div class="lineaFormulario">
                        <div style="width: 190px; float: left;" class="etiqueta">
                            <%=meLanbide58I18n.getMensaje(idiomaUsuario,"label.porcJornada")%>
                        </div>
                        <div style="width: 450px; float: left;">
                            <div style="float: left;">
                                <input id="porcJornadaD" name="porcJornadaD" type="text" class="inputTexto" size="5" maxlength="5" 
                                       value="<%=datModif != null && datModif.getPorcJornada() != null ? datModif.getPorcJornada().toString().replaceAll("\\.", ","): ""%>" onchange="reemplazarPuntos(this);"/>
                            </div>
                        </div>
                    </div><br><br>
                    <div class="lineaFormulario">
                        <div style="width: 190px; float: left;" class="etiqueta">
                            <%=meLanbide58I18n.getMensaje(idiomaUsuario,"label.fechaInicio")%>
                        </div>
                        <div style="width: 450px; float: left;">
                            <div style="float: left;">
                                <input type="text" class="inputTxtFecha"
                                       id="fechaInicioD" name="fechaInicioD"
                                       maxlength="10"  size="10"
                                       value="<%=fechaIni%>"
                                       onkeyup = "return SoloCaracteresFechaLanbide(this);"
                                       onblur = "javascript:return comprobarFechaLanbide(this);"
                                       onfocus="javascript:this.select();"/>
                                <A href="javascript:calClick(event);return false;" onClick="mostrarCalFechaIniD(event);
                                        return false;" style="text-decoration:none;">   <!-- onblur="javascript:return comprobarFecha(this);" onkeyup="return SoloCaracteresFecha(this);" -->
                                    <IMG style="border: 0px solid none" height="17" id="calFechaIniD" name="calFechaIniD" border="0"
                                         src="<c:url value='/images/calendario/icono.gif'/>" > <!--width="25"-->
                                </A>
                            </div>
                        </div>
                    </div><br><br>
                    <div class="lineaFormulario">
                        <div style="width: 190px; float: left;" class="etiqueta">
                            <%=meLanbide58I18n.getMensaje(idiomaUsuario,"label.fechaFin")%>
                        </div>
                        <div style="width: 450px; float: left;">
                            <div style="float: left;">
                                <input type="text" class="inputTxtFecha"
                                       id="fechaFinD" name="fechaFinD"
                                       maxlength="10"  size="10"
                                       value="<%=fechaFin%>"
                                       onkeyup = "return SoloCaracteresFechaLanbide(this);"
                                       onblur = "javascript:return comprobarFechaLanbide(this);"
                                       onfocus="javascript:this.select();"/>
                                <A href="javascript:calClick(event);return false;" onClick="mostrarCalFechaFinD(event);
                                        return false;" style="text-decoration:none;">   <!-- onblur="javascript:return comprobarFecha(this);" onkeyup="return SoloCaracteresFecha(this);" -->
                                    <IMG style="border: 0px solid none" height="17" id="calFechaFinD" name="calFechaFinD" border="0"
                                         src="<c:url value='/images/calendario/icono.gif'/>" > <!--width="25"-->
                                </A>
                            </div>
                        </div>
                    </div>
                    <br><br>
                    <div class="lineaFormulario">
                        <div style="width: 190px; float: left;" class="etiqueta">
                            <%=meLanbide58I18n.getMensaje(idiomaUsuario,"label.fechaInss")%>
                        </div>

                        <div style="width: 450px; float: left;">
                            <div style="float: left;">
                                <input type="text" class="inputTxtFecha"
                                       id="fechaInssD" name="fechaInssD"
                                       maxlength="10"  size="10"
                                       value="<%=fechaInss%>"
                                       onkeyup = "return SoloCaracteresFechaLanbide(this);"
                                       onblur = "javascript:return comprobarFechaLanbide(this);"
                                       onfocus="javascript:this.select();"/>
                                <A href="javascript:calClick(event);return false;" onClick="mostrarCalFechaInssD(event);return false;" style="text-decoration:none;">   <!-- onblur="javascript:return comprobarFecha(this);" onkeyup="return SoloCaracteresFecha(this);" -->
                                    <IMG style="border: 0px solid none" height="17" id="calFechaInssD" name="calFechaInssD" border="0"
                                         src="<c:url value='/images/calendario/icono.gif'/>" > <!--width="25"-->
                                </A>
                            </div>
                        </div>
                    </div>
                    <br><br>
                    <div class="lineaFormulario">
                        <div style="width: 190px; float: left;" class="etiqueta">
                            <%=meLanbide58I18n.getMensaje(idiomaUsuario,"label.fechaSin")%>
                        </div>
                        <div style="width: 450px; float: left;">
                            <div style="float: left;">
                                <input type="text" class="inputTxtFecha"
                                       id="fechaSinD" name="fechaSinD"
                                       maxlength="10"  size="10"
                                       value="<%=fechaSin%>"
                                       onkeyup = "return SoloCaracteresFechaLanbide(this);"
                                       onblur = "javascript:return comprobarFechaLanbide(this);"
                                       onfocus="javascript:this.select();"/>
                                <A href="javascript:calClick(event);return false;" onClick="mostrarCalFechaSinD(event);return false;" style="text-decoration:none;">   <!-- onblur="javascript:return comprobarFecha(this);" onkeyup="return SoloCaracteresFecha(this);" -->
                                    <IMG style="border: 0px solid none" height="17" id="calFechaSinD" name="calFechaSinD" border="0"
                                         src="<c:url value='/images/calendario/icono.gif'/>" > <!--width="25"-->
                                </A>
                            </div>
                        </div>
                    </div>
                    <br><br><br>
                    <div class="lineaFormulario">
                        <div style="width: 190px; float: left;" class="etiqueta">
                            <%=meLanbide58I18n.getMensaje(idiomaUsuario,"label.fechaCon")%>
                        </div>
                        <div style="width: 450px; float: left;">
                            <div style="float: left;">
                                <input type="text" class="inputTxtFecha"
                                       id="fechaConD" name="fechaConD"
                                       maxlength="10"  size="10"
                                       value="<%=fechaCon%>"
                                       onkeyup = "return SoloCaracteresFechaLanbide(this);"
                                       onblur = "javascript:return comprobarFechaLanbide(this);"
                                       onfocus="javascript:this.select();"/>
                                <A href="javascript:calClick(event);return false;" onClick="mostrarCalFechaConD(event);return false;" style="text-decoration:none;">   <!-- onblur="javascript:return comprobarFecha(this);" onkeyup="return SoloCaracteresFecha(this);" -->
                                    <IMG style="border: 0px solid none" height="17" id="calFechaConD" name="calFechaConD" border="0"
                                         src="<c:url value='/images/calendario/icono.gif'/>" > <!--width="25"-->
                                </A>
                            </div>
                        </div>
                    </div>
                    <br><br><br>
                    <div class="lineaFormulario">
                        <div style="width: 190px; float: left;" class="etiqueta">
                            <%=meLanbide58I18n.getMensaje(idiomaUsuario,"label.gradoDiscapacidad")%>
                        </div>
                        <div style="width: 450px; float: left;">
                            <div style="float: left;">
                                <input id="gradoD" name="gradoD" type="text" class="inputTexto" size="5" maxlength="5"
                                       value="<%=datModif != null && datModif.getGradoDisc() != null ? datModif.getGradoDisc().toString().replaceAll("\\.", ","): ""%>" onchange="reemplazarPuntos(this);"/>
                            </div>
                        </div>
                    </div>
                    <br><br/>
                    <div class="lineaFormulario">
                        <div class="botonera" style="text-align: center;">
                            <input type="button" id="btnAceptar" name="btnAceptar" class="botonGeneral" value="<%=meLanbide58I18n.getMensaje(idiomaUsuario, "btn.aceptar")%>" onclick="guardarDatos();"/>
                            <input type="button" id="btnCancelar" name="btnCancelar" class="botonGeneral" value="<%=meLanbide58I18n.getMensaje(idiomaUsuario, "btn.cancelar")%>" onclick="cancelar();"/>
                        </div>
                    </div>
                </div>
                <div id="reloj" style="font-size:20px;"></div>
            </form>
            <script type="text/javascript">
                /* DESPLEGABLES */
                /*desplegable tipo contrato*/
                listaCodigosTipoConD[0] = "";
                listaDescripcionesTipoConD[0] = "";
                contador = 0;
                <logic:iterate id="tipoConD" name="listaTipoConD" scope="request">
                listaCodigosTipoConD[contador] = ['<bean:write name="tipoConD" property="des_val_cod" />'];
                listaDescripcionesTipoConD[contador] = ['<bean:write name="tipoConD" property="des_nom" />'];
                contador++;
                </logic:iterate>

                var comboListaTipoConD = new Combo("ListaTipoConD");
                comboListaTipoConD.addItems(listaCodigosTipoConD, listaDescripcionesTipoConD);
                comboListaTipoConD.change = cargarDatosTipoConD;

                /*desplegable tipo jornada*/
                listaCodigosTipoJorD[0] = "";
                listaDescripcionesTipoJorD[0] = "";
                contador = 0;
                <logic:iterate id="tipoJorD" name="listaTipoJorD" scope="request">
                listaCodigosTipoJorD[contador] = ['<bean:write name="tipoJorD" property="des_val_cod" />'];
                listaDescripcionesTipoJorD[contador] = ['<bean:write name="tipoJorD" property="des_nom" />'];
                contador++;
                </logic:iterate>

                var comboListaTipoJorD = new Combo("ListaTipoJorD");
                comboListaTipoJorD.addItems(listaCodigosTipoJorD, listaDescripcionesTipoJorD);
                comboListaTipoJorD.change = cargarDatosTipoJorD;

                var nuevo = "<%=nuevo%>";
                if (nuevo == 0) {
                    rellenarDatModificar();
                }
            </script>
            <div id="popupcalendar" class="text"></div>
        </div>
    </body>
</html>



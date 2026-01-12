<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>

<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide58.i18n.MeLanbide58I18n" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide58.vo.ContratacionVO" %>
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
            ContratacionVO datModif = new ContratacionVO ();
            ContratacionVO objectVO = new ContratacionVO ();
            
            String codOrganizacion = "";
            String nuevo="";
            String expediente="";
            String fechaAlta = "";
            String fechaBaja = "";
            
            int idiomaUsuario = 1;
            int apl = 5;
            String css = "";
            MeLanbide58I18n meLanbide58I18n = MeLanbide58I18n.getInstance();
            expediente = (String)request.getAttribute("numExp");
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
                    datModif = (ContratacionVO)request.getAttribute("datModif");
                    SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
                    if (datModif.getFechaAlta()!=null){
                        fechaAlta = formatoFecha.format(datModif.getFechaAlta());
                    }
                    if (datModif.getFechaBaja()!=null){
                        fechaBaja = formatoFecha.format(datModif.getFechaBaja());
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
            var mensajeValidacion = "";

            function mostrarCalFechaAltaE(evento) {
                if (window.event)
                    evento = window.event;
                if (document.getElementById("calFechaAltaE").src.indexOf("icono.gif") != -1)
                    showCalendar('forms[0]', 'fechaAltaE', null, null, null, '', 'calFechaAltaE', '', null, null, null, null, null, null, null, null, evento);
            }

            function mostrarCalFechaBajaE(evento) {
                if (window.event)
                    evento = window.event;
                if (document.getElementById("calFechaBajaE").src.indexOf("icono.gif") != -1)
                    showCalendar('forms[0]', 'fechaBajaE', null, null, null, '', 'calFechaBajaE', '', null, null, null, null, null, null, null, null, evento);
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

            function guardarDatos() {                
                mensajeValidacion = "";
                if (validarDatosNumericosVacios() && validarCampos()) {
                    var ajax = getXMLHttpRequest();
                    var nodos = null;
                    var CONTEXT_PATH = '<%=request.getContextPath()%>';
                    var url = CONTEXT_PATH + "/PeticionModuloIntegracion.do";
                    var parametros = "";
                    var nuevo = "<%=nuevo%>";
                    if (nuevo != null && nuevo == "1") {
                        parametros = "tarea=preparar&modulo=MELANBIDE58&operacion=crearNuevoContratacion&tipo=0"
                                + "&expediente=<%=expediente%>"
                                + "&numLinea=" + document.getElementById('numLinea').value
                                + "&dni=" + document.getElementById('dni_nif').value
                                + "&porcJornada=" + document.getElementById('porcJornadaE').value
                                + "&fechaAlta=" + document.getElementById('fechaAltaE').value
                                + "&fechaBaja=" + document.getElementById('fechaBajaE').value
                                + "&claveCont=" + document.getElementById('claveE').value
                                + "&docVerificada=" + $('#docVerificadaE').is(":checked")
                                + "&docValidada=" + $('#docValidadaE').is(":checked")
                                + "&motNoValidacion=" + $('#motNoValidacionE').val()
                                ;
                    } else {
                        parametros = "tarea=preparar&modulo=MELANBIDE58&operacion=modificarContratacion&tipo=0"
                                + "&id=<%=datModif != null && datModif.getId() != null ? datModif.getId().toString() : ""%>"
                                + "&expediente=<%=datModif != null && datModif.getNumExp() != null ? datModif.getNumExp().toString() : ""%>"
                                + "&numLinea=" + document.getElementById('numLinea').value
                                + "&dni=" + document.getElementById('dni_nif').value
                                + "&porcJornada=" + document.getElementById('porcJornadaE').value
                                + "&fechaAlta=" + document.getElementById('fechaAltaE').value
                                + "&fechaBaja=" + document.getElementById('fechaBajaE').value
                                + "&claveCont=" + document.getElementById('claveE').value
                                + "&docVerificada=" + $('#docVerificadaE').is(":checked")
                                + "&docValidada=" + $('#docValidadaE').is(":checked")
                                + "&motNoValidacion=" + document.getElementById('motNoValidacionE').value
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
                                    } else if (hijosFila[cont].nodeName == "DNI") {
                                        if (hijosFila[cont].childNodes[0].nodeValue != "null") {
                                            fila[2] = hijosFila[cont].childNodes[0].nodeValue;
                                        } else {
                                            fila[2] = '-';
                                        }
                                    } else if (hijosFila[cont].nodeName == "PORCJORNADA") {
                                        if (hijosFila[cont].childNodes.length > 0) {
                                            fila[3] = hijosFila[cont].childNodes[0].nodeValue;
                                        } else {
                                            fila[3] = '-';
                                        }
                                    } else if (hijosFila[cont].nodeName == "FECALTA") {
                                        if (hijosFila[cont].childNodes.length > 0) {
                                            fila[4] = hijosFila[cont].childNodes[0].nodeValue;
                                        } else {
                                            fila[4] = '-';
                                        }
                                    } else if (hijosFila[cont].nodeName == "FECBAJA") {
                                        if (hijosFila[cont].childNodes.length > 0) {
                                            fila[5] = hijosFila[cont].childNodes[0].nodeValue;
                                        } else {
                                            fila[5] = '-';
                                        }
                                    } else if (hijosFila[cont].nodeName == "CLAVECONTRATO") {
                                        if (hijosFila[cont].childNodes.length > 0) {
                                            fila[6] = hijosFila[cont].childNodes[0].nodeValue;
                                        } else {
                                            fila[6] = '-';
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
                return correcto;
            }

            function validarCampos() {
                mensajeValidacion = "";
                var correcto = true;
                if (document.getElementById('dni_nif').value == null || document.getElementById('dni_nif').value == '') {
                    mensajeValidacion = '<%=meLanbide58I18n.getMensaje(idiomaUsuario, "msg.dniObligatorio")%>';
                    return false;
                }
                var claveContrato = document.getElementById('claveE').value;
                if (claveContrato != null || claveContrato != '') {
                    if (claveContrato != '402' && claveContrato != '502' && claveContrato != '410' && claveContrato != '510') {
                        mensajeValidacion = '<%=meLanbide58I18n.getMensaje(idiomaUsuario, "msg.claveContratoNoValido")%>';
                        return false;
                    }
                }
                var porcJornada = document.getElementById('porcJornadaE').value;
                if (porcJornada != null || porcJornada != '') {
                    if (porcJornada > 100 || porcJornada < 0) {
                        mensajeValidacion = '<%=meLanbide58I18n.getMensaje(idiomaUsuario, "msg.porcJornada.errRango")%>';
                        return false;
                    }
                }
                return correcto;

            }

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
        </script>
    </head>
    <body>
        <div class="contenidoPantalla">
            <form>
                <div style="width: 100%; padding: 10px; text-align: left;">
                    <div  class="sub3titulo" style="clear: both; text-align: center; font-size: 14px;">
                        <span>
                            <%=meLanbide58I18n.getMensaje(idiomaUsuario,"label.nuevoAltaE")%>
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
                            <%=meLanbide58I18n.getMensaje(idiomaUsuario,"label.porcJornada")%>
                        </div>
                        <div style="width: 450px; float: left;">
                            <div style="float: left;">
                                <input id="porcJornadaE" name="porcJornadaE" type="text" class="inputTexto" size="5" maxlength="5"
                                       value="<%=datModif != null && datModif.getPorcJornada() != null ? datModif.getPorcJornada().toString().replaceAll("\\.", ","): ""%>" onchange="reemplazarPuntos(this);"/>
                            </div>
                        </div>
                    </div><br><br>
                    <div class="lineaFormulario">
                        <div style="width: 190px; float: left;" class="etiqueta">
                            <%=meLanbide58I18n.getMensaje(idiomaUsuario,"label.fechaAlta")%>
                        </div>
                        <div style="width: 450px; float: left;">
                            <div style="float: left;">
                                <input type="text" class="inputTxtFecha"
                                       id="fechaAltaE" name="fechaAltaE"
                                       maxlength="10"  size="10"
                                       value="<%=fechaAlta%>"
                                       onkeyup = "return SoloCaracteresFechaLanbide(this);"
                                       onblur = "javascript:return comprobarFechaLanbide(this);"
                                       onfocus="javascript:this.select();"/>
                                <A href="javascript:calClick(event);return false;" onClick="mostrarCalFechaAltaE(event);
                                        return false;" style="text-decoration:none;">  
                                    <IMG style="border: 0px solid none" height="17" id="calFechaAltaE" name="calFechaAltaE" border="0"
                                         src="<c:url value='/images/calendario/icono.gif'/>" >
                                </A>
                            </div>
                        </div>
                    </div><br><br>
                    <div class="lineaFormulario">
                        <div style="width: 190px; float: left;" class="etiqueta">
                            <%=meLanbide58I18n.getMensaje(idiomaUsuario,"label.fechaBaja")%>
                        </div>
                        <div style="width: 450px; float: left;">
                            <div style="float: left;">
                                <input type="text" class="inputTxtFecha"
                                       id="fechaBajaE" name="fechaBajaE"
                                       maxlength="10"  size="10"
                                       value="<%=fechaBaja%>"
                                       onkeyup = "return SoloCaracteresFechaLanbide(this);"
                                       onblur = "javascript:return comprobarFechaLanbide(this);"
                                       onfocus="javascript:this.select();"/>
                                <A href="javascript:calClick(event);return false;" onClick="mostrarCalFechaBajaE(event);
                                        return false;" style="text-decoration:none;">   
                                    <IMG style="border: 0px solid none" height="17" id="calFechaBajaE" name="calFechaBajaE" border="0"
                                         src="<c:url value='/images/calendario/icono.gif'/>" > 
                                </A>
                            </div>
                        </div>
                    </div>
                    <br><br>

                    <div class="lineaFormulario">
                        <div style="width: 190px; float: left;" class="etiqueta">
                            <%=meLanbide58I18n.getMensaje(idiomaUsuario,"label.claveContrato")%>
                        </div>
                        <div style="width: 450px; float: left;">
                            <div style="float: left;">
                                <input id="claveE" name="claveE" type="text" class="inputTexto" size="5" maxlength="4" 
                                       value="<%=datModif != null && datModif.getClaveContrato() != null ? datModif.getClaveContrato() : ""%>"
                                       onchange="reemplazarPuntos(this);"disable="true"/>
                            </div>
                        </div>
                    </div>
                    <br><br>                
                    <div class="lineaFormulario">
                        <div style="width: 190px; float: left;" class="etiqueta">
                            <%=meLanbide58I18n.getMensaje(idiomaUsuario,"label.docVerificada")%>
                        </div>
                        <div style="width: 450px; float: left;">
                            <div style="float: left;">
                                <input id="docVerificadaE" name="docVerificadaE" type="checkbox" class="inputTexto"
                                    <%=datModif != null && datModif.getDocVerificada() != null && datModif.getDocVerificada() ? "checked" : "" %>/>
                            </div>
                        </div>
                    </div>
                    <br><br>
                    <div class="lineaFormulario">
                        <div style="width: 190px; float: left;" class="etiqueta">
                            <%=meLanbide58I18n.getMensaje(idiomaUsuario,"label.docValidada")%>
                        </div>
                        <div style="width: 450px; float: left;">
                            <div style="float: left;">
                                <input id="docValidadaE" name="docValidadaE" type="checkbox" class="inputTexto"
                                      <%=datModif != null && datModif.getDocValidada() != null && datModif.getDocValidada() ? "checked" : "" %>/>
                            </div>
                        </div>
                    </div>
                    <br><br/>
                    <div class="lineaFormulario">
                        <div style="width: 190px; float: left;" class="etiqueta">
                            <%=meLanbide58I18n.getMensaje(idiomaUsuario,"label.motNoValidacion")%>
                        </div>
                        <div style="width: 450px; float: left;">
                            <div style="float: left;">
                                <textarea id="motNoValidacionE" style="width:300px; height:150px;" name="motNoValidacionE" class="inputTexto" size="30" maxlength="200" 
                                          ><%=datModif != null && datModif.getMotNoVal() != null ? datModif.getMotNoVal() : ""%></textarea>
                            </div>
                        </div>
                    </div>
                    <br><br> 
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

            </script>
            <div id="popupcalendar" class="text"></div>
        </div>
    </body>
</html>
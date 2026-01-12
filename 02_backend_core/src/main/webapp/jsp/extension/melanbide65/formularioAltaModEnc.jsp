<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>

<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide65.i18n.MeLanbide65I18n" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide65.vo.EncargadoVO" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="java.util.ArrayList" %>
<%@page import="java.util.List" %>
<%@page import="java.text.SimpleDateFormat" %>
<html>
    <head>
        <meta http-equiv="X-UA-Compatible" content="IE=Edge" >
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <%
            EncargadoVO objEncMod = null;
            String procesoC = "";
            String expediente = "";
            String identEncarg="";
        
            String nuevo = "";  
                try {
                    nuevo = (String)request.getAttribute("nuevo");
                }catch(Exception ex) {
                nuevo = "1";  
                }
        
            MeLanbide65I18n meLanbide65I18n = MeLanbide65I18n.getInstance();
        
            int idiomaUsuario = 1;
            int apl = 5;
            String css = "";
            try
            {
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

                expediente = (String)request.getAttribute("numExpediente");
                if(request.getAttribute("datosEncargado") != null){
                    objEncMod = (EncargadoVO)request.getAttribute("datosEncargado");
                    identEncarg = (String)request.getAttribute("identificador");
                    procesoC = "mod";
                } else procesoC = "alta";
            }
            catch(Exception ex)
            {
            }
        %>
        <jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
        <jsp:setProperty name="descriptor"  property="idi_cod" value="<%=idiomaUsuario%>" />
        <jsp:setProperty name="descriptor"  property="apl_cod" value="<%=apl%>" />
        <link rel="StyleSheet" media="screen" type="text/css" href="<%=request.getContextPath()%><%=css%>">
        <script type="text/javascript" src="<c:url value='/scripts/general.js'/>"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script>
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/>
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/extension/melanbide65/melanbide65.css"/>
        <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/css/font-awesome.min.css'/>" media="screen">
        <script type="text/javascript" src="<c:url value='/scripts/listaComboBox.js'/>"></script>
        <script type="text/javascript" src="<c:url value='/scripts/calendario.js'/>"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/validaciones.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/popup.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/melanbide65/JavaScriptUtil.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/melanbide65/lanbide.js"></script>
        <script type="text/javascript">
            var APP_CONTEXT_PATH = '<%=request.getContextPath()%>';
            var mensajeValidacionC = '';
            var procesoC = '<%=procesoC%>';

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

            function guardarDatosEncargado() {

                if (validarDatosEncargado()) {
                    var ajax = getXMLHttpRequest();
                    var nodos = null;
                    var url = APP_CONTEXT_PATH + "/PeticionModuloIntegracion.do";
                    var parametros;
                    if (procesoC == 'alta') {
                        parametros = "tarea=preparar&modulo=MELANBIDE65&operacion=darAltaEncargado&tipo=0";
                    } else {
                        parametros = "tarea=preparar&modulo=MELANBIDE65&operacion=modificarEncargado&tipo=0&identificador=<%=identEncarg%>";
                    }
                    parametros += "&expediente=<%=expediente%>"
                            + "&nombre=" + document.getElementById('nombreAnexoC').value
                            + "&apellido1=" + document.getElementById('apellido1AnexoC').value
                            + "&apellido2=" + document.getElementById('apellido2AnexoC').value
                            + "&tipoContrato=" + document.getElementById('codListaContrato').value
                            + "&tipoJornada=" + document.getElementById('codListaJornada').value
                            + "&dni=" + document.getElementById('dniAnexoC').value
                            + "&fechaAltaContratoIndefinido=" + document.getElementById('fechaAltaIndefinido').value
                            + "&fechaAltaContratoTemporal=" + document.getElementById('fechaAltaTemporal').value
                            + "&fechaBajaContratoTemporal=" + document.getElementById('fechaBajaTemporal').value
                            + "&jornadaParcialPor=" + document.getElementById('jornadaParcialPorAnexoC').value
                            + "&pensionista=" + document.getElementById('codListaTipoPensionista').value
                            + "&tipoPensionista=" + document.getElementById('codListaPensionista').value
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
                        var lista = new Array();
                        var fila = new Array();
                        var nodoFila;
                        var hijosFila;
                        for (var j = 0; hijos != null && j < hijos.length; j++) {
                            if (hijos[j].nodeName == "CODIGO_OPERACION") {
                                codigoOperacion = hijos[j].childNodes[0].nodeValue;
                                lista[j] = codigoOperacion;
                            }//if(hijos[j].nodeName=="CODIGO_OPERACION")                      
                            else if (hijos[j].nodeName == "FILA") {
                                nodoFila = hijos[j];
                                hijosFila = nodoFila.childNodes;
                                for (var cont = 0; cont < hijosFila.length; cont++) {
                                    if (hijosFila[cont].nodeName == "ID") {
                                        fila[0] = hijosFila[cont].childNodes[0].nodeValue;
                                    } else if (hijosFila[cont].nodeName == "APELLIDO1") {
                                        if (hijosFila[cont].childNodes.length > 0) {
                                            fila[2] = hijosFila[cont].childNodes[0].nodeValue;
                                        } else {
                                            fila[2] = '-';
                                        }
                                    } else if (hijosFila[cont].nodeName == "APELLIDO2") {
                                        if (hijosFila[cont].childNodes.length > 0) {
                                            fila[3] = hijosFila[cont].childNodes[0].nodeValue;
                                        } else {
                                            fila[3] = '-';
                                        }
                                    } else if (hijosFila[cont].nodeName == "NOMBRE") {
                                        if (hijosFila[cont].childNodes.length > 0) {
                                            fila[4] = hijosFila[cont].childNodes[0].nodeValue;
                                        } else {
                                            fila[4] = '-';
                                        }
                                    } else if (hijosFila[cont].nodeName == "DNI") {
                                        if (hijosFila[cont].childNodes.length > 0) {
                                            fila[6] = hijosFila[cont].childNodes[0].nodeValue;
                                        } else {
                                            fila[6] = '-';
                                        }
                                    } else if (hijosFila[cont].nodeName == "FECHAALTA") {
                                        if (hijosFila[cont].childNodes.length > 0) {
                                            fila[7] = hijosFila[cont].childNodes[0].nodeValue;
                                        } else {
                                            fila[7] = '-';
                                        }
                                    } else if (hijosFila[cont].nodeName == "TIPOCONTRATO") {
                                        if (hijosFila[cont].childNodes.length > 0) {
                                            fila[8] = hijosFila[cont].childNodes[0].nodeValue;
                                        } else {
                                            fila[8] = '-';
                                        }
                                    } else if (hijosFila[cont].nodeName == "JORNADAPARCIALPOR") {
                                        if (hijosFila[cont].childNodes.length > 0) {
                                            fila[9] = hijosFila[cont].childNodes[0].nodeValue;
                                            var tex = fila[9].toString();
                                            tex = tex.replace(".", ",");
                                            fila[9] = tex;
                                        } else {
                                            fila[9] = '-';
                                        }
                                    } else if (hijosFila[cont].nodeName == "FECHAALTAT") {
                                        if (hijosFila[cont].childNodes.length > 0) {
                                            fila[10] = hijosFila[cont].childNodes[0].nodeValue;
                                        } else {
                                            fila[10] = '-';
                                        }
                                    } else if (hijosFila[cont].nodeName == "FECHABAJAT") {
                                        if (hijosFila[cont].childNodes.length > 0) {
                                            fila[11] = hijosFila[cont].childNodes[0].nodeValue;
                                        } else {
                                            fila[11] = '-';
                                        }
                                    } else if (hijosFila[cont].nodeName == "TIPOJORNADA") {
                                        if (hijosFila[cont].childNodes.length > 0) {
                                            fila[12] = hijosFila[cont].childNodes[0].nodeValue;
                                        } else {
                                            fila[12] = '-';
                                        }
                                    }else if (hijosFila[cont].nodeName == "PENSIONISTA") {
                                        if (hijosFila[cont].childNodes.length > 0) {
                                            fila[13] = hijosFila[cont].childNodes[0].nodeValue;
                                            var tex = fila[13].toString();
                                            fila[13] = tex;
                                        } else {
                                            fila[13] = '-';
                                        }
                                    }
                                     else if (hijosFila[cont].nodeName == "TIPPENSIONISTA") {
                                        if (hijosFila[cont].childNodes.length > 0) {
                                            fila[14] = hijosFila[cont].childNodes[0].nodeValue;
                                            var tex = fila[14].toString();
                                            fila[14] = tex;
                                        } else {
                                            fila[14] = '-';
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
                            jsp_alerta("A", '<%=meLanbide65I18n.getMensaje(idiomaUsuario,"error.errorBD")%>');
                        } else if (codigoOperacion == "2") {
                            jsp_alerta("A", '<%=meLanbide65I18n.getMensaje(idiomaUsuario,"error.errorParsearDatos")%>');
                        } else if (codigoOperacion == "3" || codigoOperacion == "5") {
                            jsp_alerta("A", '<%=meLanbide65I18n.getMensaje(idiomaUsuario,"error.errorObtenerDatos")%>');
                        } else if (codigoOperacion == "4") {
                            jsp_alerta("A", '<%=meLanbide65I18n.getMensaje(idiomaUsuario,"error.errorInsertarDatos")%>');
                        } else if (codigoOperacion == "6") {
                            jsp_alerta("A", '<%=meLanbide65I18n.getMensaje(idiomaUsuario,"error.errorActualizarDatos")%>');
                        } else if (codigoOperacion == "7") {
                            jsp_alerta("A", '<%=meLanbide65I18n.getMensaje(idiomaUsuario,"error.errorBorrarDatos")%>');
                        } else if (codigoOperacion == "-1") {
                            jsp_alerta("A", '<%=meLanbide65I18n.getMensaje(idiomaUsuario,"error.operacionIncorrecta")%>');
                        } else {
                            jsp_alerta("A", '<%=meLanbide65I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                        }
                    } catch (Err) {
                        jsp_alerta("A", '<%=meLanbide65I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                    }//try-catch
                } else {
                    jsp_alerta("A", mensajeValidacionC);
                }
            }

            function cancelar() {
                var resultado = jsp_alerta('', '<%=meLanbide65I18n.getMensaje(idiomaUsuario, "msg.preguntaCancelar")%>');
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

            function validarDatosEncargado() {
                mensajeValidacionC = "";
                var correcto = true;

                var valor = document.getElementById('nombreAnexoC').value;
                if (valor == null || valor == '') {
                    mensajeValidacionC = '<%=meLanbide65I18n.getMensaje(idiomaUsuario, "msg.nombreObligatorio")%>';
                    return false;
                }

                valor = document.getElementById('apellido1AnexoC').value;
                if (valor == null || valor == '') {
                    mensajeValidacionC = '<%=meLanbide65I18n.getMensaje(idiomaUsuario, "msg.apellido1Obligatorio")%>';
                    return false;
                }

                

                valor = document.getElementById('dniAnexoC').value;
                if (valor == null || valor == '') {
                    mensajeValidacionC = '<%=meLanbide65I18n.getMensaje(idiomaUsuario, "msg.dniObligatorio")%>';
                    return false;
                } else if (!validarDniNie(document.getElementById('dniAnexoC'))) {
                    mensajeValidacionC = '<%=meLanbide65I18n.getMensaje(idiomaUsuario, "msg.dniIncorrecto")%>';
                    return false;
                }
                
                 var pensionista = document.getElementById('codListaTipoPensionista').value;
                if (pensionista == null || pensionista == '') {
                    mensajeValidacionC = '<%=meLanbide65I18n.getMensaje(idiomaUsuario, "msg.tipoPensionistaObligatorio")%>';
                    return false;
                }
                 var pensionistaTipo = document.getElementById('codListaPensionista').value;
                 if (pensionista == 'S') {
                     if (pensionistaTipo == null || pensionistaTipo == '') {
                    
                    mensajeValidacionC = '<%=meLanbide65I18n.getMensaje(idiomaUsuario, "msg.tipoPensionistaTipoObligatorio")%>';
                    return false;
                }}

                var jornada = document.getElementById('codListaJornada').value;
                if (jornada == null || jornada == '') {
                    mensajeValidacionC = '<%=meLanbide65I18n.getMensaje(idiomaUsuario, "msg.tipoJornadaObligatorio")%>';
                    return false;
                }

                var contrato = document.getElementById('codListaContrato').value;
                if (contrato == '') {
                    mensajeValidacionC = '<%=meLanbide65I18n.getMensaje(idiomaUsuario, "msg.tipoContratoObligatorio")%>';
                    return false;
                }

                valor = document.getElementById('fechaAltaIndefinido').value;
                if (contrato == 'I' && (valor == null || valor == '')) {
                    mensajeValidacionC = '<%=meLanbide65I18n.getMensaje(idiomaUsuario, "msg.fechaAltaObligatorio")%>';
                    return false;
                }

                valor = document.getElementById('fechaAltaTemporal').value;
                if (contrato == 'T' && (valor == null || valor == '')) {
                    mensajeValidacionC = '<%=meLanbide65I18n.getMensaje(idiomaUsuario, "msg.fechaAltaTemporalObligatorio")%>';
                    return false;
                }

                valor = document.getElementById('fechaBajaTemporal').value;
                if (contrato == 'T' && (valor == null || valor == '')) {
                    mensajeValidacionC = '<%=meLanbide65I18n.getMensaje(idiomaUsuario, "msg.fechaBajaTemporalObligatorio")%>';
                    return false;
                }

                var porjorpar = document.getElementById('jornadaParcialPorAnexoC').value;
                if ((jornada == 'PARC' && (porjorpar == null || porjorpar == ''))) {
                    mensajeValidacionC = '<%=meLanbide65I18n.getMensaje(idiomaUsuario, "msg.porcentajeJornadaParcialObligatorio")%>';
                    return false;
                } else if (!validarNumericoDecimalPrecision(porjorpar, 5, 2)) {
                    mensajeValidacionC = '<%=meLanbide65I18n.getMensaje(idiomaUsuario, "msg.porNumericoDecimal")%>';
                    return false;
                } else if (!validarNumericoPorcentajeCien(porjorpar)) {
                    mensajeValidacionC = '<%=meLanbide65I18n.getMensaje(idiomaUsuario, "msg.porcentajeJornadaParcialErrorRango")%>';
                    return false;
                }
                return correcto;
            }

            function validarNumerico(numero) {
                try {
                    if (Trim(numero) != '') {
                        return /^([0-9])*$/.test(numero);
                    }
                } catch (err) {
                    return false;
                }
            }

            function validarNumericoPorcentajeCien(numero) {
                try {
                    if (parseFloat(numero) < 0 || parseFloat(numero) > 100) {
                        return false;
                    } else {
                        return true;
                    }
                } catch (err) {
                    return false;
                }
            }

            function validarNumericoDecimal(numero) {
                try {
                    if (Trim(numero) != '') {
                        return /^[0-9]{1,2}(,[0-9]{1,2})?$/.test(numero);
                    }
                } catch (err) {
                    return false;
                }
            }


            function mostrarCalFechaABIndef(evento) {
                if (window.event)
                    evento = window.event;
                if (document.getElementById("calFecha").src.indexOf("icono.gif") != -1)
                    showCalendar('forms[0]', 'fechaAltaIndefinido', null, null, null, '', 'calFecha', '', null, null, null, null, null, null, null, null, evento);

            }


            function mostrarCalFechaABTemp(evento) {
                if (window.event)
                    evento = window.event;
                if (document.getElementById("calFechaAltaTemporal").src.indexOf("icono.gif") != -1)
                    showCalendar('forms[0]', 'fechaAltaTemporal', null, null, null, '', 'calFechaAltaTemporal', '', null, null, null, null, null, null, null, null, evento);

            }


            function mostrarCalFechaBTemporal(evento) {
                if (window.event)
                    evento = window.event;
                if (document.getElementById("calFechaBajaTemporal").src.indexOf("icono.gif") != -1)
                    showCalendar('forms[0]', 'fechaBajaTemporal', null, null, null, '', 'calFechaBajaTemporal', '', null, null, null, null, null, null, null, null, evento);

            }

            function seleccionarSexoEncargado(value) {
                document.getElementById('valorSexoAnexoC').value = value;
            }

            function seleccionarTipoContrato(value) {
                document.getElementById('valorTipoContratoAnexoA').value = value;
            }

            function mostrarOcultarPensionista() {
                if (document.getElementById('codListaTipoPensionista').value == 'S'){
                    document.getElementById('pensionistaTipo').hidden=false;
                } else {
                    document.getElementById('codListaPensionista').value = '';
                    document.getElementById('pensionistaTipo').hidden=true;
                }
                    
            }

            function seleccionarTipoJornada(value) {
                document.getElementById('valorTipoJornadaAnexoA').value = value;
            }
        </script>
    </head>
    <body>
        <div class="contenidoPantalla">
            <form><!--    action="/PeticionModuloIntegracion.do" method="POST" -->
                <div style="width: 100%; padding: 10px; text-align: left;">
                    <div  class="sub3titulo" style="clear: both; text-align: center; font-size: 14px;">
                        <span><%= procesoC.equals("alta") ? meLanbide65I18n.getMensaje(idiomaUsuario,"label.altaEncargado") : 
                            meLanbide65I18n.getMensaje(idiomaUsuario,"label.modEncargado")%></span>
                    </div>                        
                    <br><br>   
                    <div style="margin-left: 10px">


                        <div class="lineaFormulario">
                            <div style="width: 340px; float: left;" class="etiqueta">
                                <%=meLanbide65I18n.getMensaje(idiomaUsuario,"label.nombre")%>
                            </div>
                            <div style="width: 300px; float: left;">
                                <div style="float: left;">
                                    <input id="nombreAnexoC" name="nombreAnexoC" type="text" class="inputTexto" size="30" maxlength="80"
                                           value="<%=objEncMod != null && objEncMod.getNombre() != null ? objEncMod.getNombre() : ""%>"/>
                                </div>
                            </div>
                        </div>
                        <br><br>
                        <div class="lineaFormulario">
                            <div style="width: 340px; float: left;" class="etiqueta">
                                <%=meLanbide65I18n.getMensaje(idiomaUsuario,"label.apellido1")%>
                            </div>
                            <div style="width: 300px; float: left;">
                                <div style="float: left;">
                                    <input id="apellido1AnexoC" name="apellido1AnexoC" type="text" class="inputTexto" size="30" maxlength="50"
                                           value="<%=objEncMod != null && objEncMod.getApellido1() != null ? objEncMod.getApellido1() : ""%>"/>
                                </div>
                            </div>
                        </div>
                        <br><br>
                        <div class="lineaFormulario">
                            <div style="width: 340px; float: left;" class="etiqueta">
                                <%=meLanbide65I18n.getMensaje(idiomaUsuario,"label.apellido2")%>
                            </div>
                            <div style="width: 300px; float: left;">
                                <div style="float: left;">
                                    <input id="apellido2AnexoC" name="apellido2AnexoC" type="text" class="inputTexto" size="30" maxlength="50"
                                           value="<%=objEncMod != null && objEncMod.getApellido2() != null ? objEncMod.getApellido2() : ""%>"/>
                                </div>
                            </div>
                        </div>
                                 <br><br>
                     <div class="lineaFormulario">
                        <div style="width: 340px; float: left;" class="etiqueta">
                            <%=meLanbide65I18n.getMensaje(idiomaUsuario,"label.pensionista")%>
                        </div>
                        <div style="width: 500px; float: left;">
                            <div style="float: left;">
                                <input type="text" name="codListaTipoPensionista" id="codListaTipoPensionista" size="5" class="inputCombo" value="" maxlength="2" onkeyup="xAMayusculas(this);" onfocusout="mostrarOcultarPensionista()" onchange="mostrarOcultarPensionista()"/>
                                <input type="text" name="descListaTipoPensionista"  id="descListaTipoPensionista" size="40" class="inputCombo" readonly value="" />
                                <a href="" id="anchorListaTipoPensionista" name="anchorListaTipoPensionista" title="">
                                    <span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonAplicacion" style="cursor:pointer;"></span>
                                </a>
                            </div>
                        </div>
                    </div>    
                    <br><br>
                     <div class="lineaFormulario">
                        <div id="pensionistaTipo" hidden>
                        <div style="width: 340px; float: left;" class="etiqueta">
                            <%=meLanbide65I18n.getMensaje(idiomaUsuario,"label.tipoPensionista")%>
                        </div>
                        <div style="width: 500px; float: left;">
                            <div style="float: left;">
                                <input type="text" name="codListaPensionista" id="codListaPensionista" size="5" class="inputCombo" value="" maxlength="2" onkeyup="xAMayusculas(this);" />
                                <input type="text" name="descListaPensionista"  id="descListaPensionista" size="40" class="inputCombo" readonly value="" />
                                <a href="" id="anchorListaPensionista" name="anchorListaPensionista" title="">
                                    <span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonAplicacion" style="cursor:pointer;"></span>
                                </a>
                            </div>
                        </div>
                        </div>
                    </div>      
                        <br><br>

                        <div class="lineaFormulario">
                            <div style="width: 340px; float: left;" class="etiqueta">
                                <%=meLanbide65I18n.getMensaje(idiomaUsuario,"label.dni")%>
                            </div>
                            <div style="width: 300px; float: left;">
                                <div style="float: left;">
                                    <input id="dniAnexoC" name="dniAnexoC" type="text" class="inputTexto" size="15" maxlength="10" onkeyup="xAMayusculas(this);" 
                                           value="<%=objEncMod != null && objEncMod.getDni() != null ? objEncMod.getDni() : ""%>"/>
                                </div>
                            </div>
                        </div>
                        <br><br>
                        <div class="lineaFormulario">
                            <div style="width: 340px; float: left;" class="etiqueta">
                                <%=meLanbide65I18n.getMensaje(idiomaUsuario,"label.tipoContrato")%>
                            </div>
                            <div style="width: 500px; float: left;">
                                <div style="float: left;">
                                    <input type="text" name="codListaContrato" id="codListaContrato" size="5" class="inputCombo" value="" maxlength="5" onkeyup="xAMayusculas(this);" />
                                    <input type="text" name="descListaContrato"  id="descListaContrato" size="40" class="inputCombo" readonly value="" />
                                    <a href="" id="anchorListaContrato" name="anchorListaContrato" title="">
                                        <span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonAplicacion" style="cursor:pointer;"></span>
                                    </a>
                                </div>
                            </div>
                        </div>

                        <br><br>
                        <div class="lineaFormulario">
                            <div style="width: 340px; float: left;" class="etiqueta">
                                <%=meLanbide65I18n.getMensaje(idiomaUsuario,"label.fechaAlta")%>
                            </div>
                            <div style="width: 300px; float: left;">
                                <div style="float: left;">
                                    <input type="text" class="inputTxtFecha" 
                                           id="fechaAltaIndefinido" name="fechaAltaIndefinido"
                                           maxlength="10"  size="10"
                                           value="<%=objEncMod != null && objEncMod.getFecAltaAsStr()!= null ? objEncMod.getFecAltaAsStr(): ""%>"
                                           onkeyup = "return SoloCaracteresFechaLanbide(this);"                                       
                                           onfocus="javascript:this.select();"/>
                                    <A id="afechaAltaIndefinido" href="javascript:calClick(event);return false;" onClick="mostrarCalFechaABIndef(event);
                                            return false;" style="text-decoration:none;">
                                        <IMG style="border: 0px solid none" height="17" id="calFecha" name="calFecha" border="0" 
                                             src="<c:url value='/images/calendario/icono.gif'/>" > <!--width="25"-->
                                    </A>
                                </div>
                            </div>
                        </div>
                        <br><br>
                        <div class="lineaFormulario">
                            <div style="width: 340px; float: left;" class="etiqueta">
                                <%=meLanbide65I18n.getMensaje(idiomaUsuario,"label.fechaAltaTemp")%>
                            </div>
                            <div style="width: 300px; float: left;">
                                <div style="float: left;">
                                    <input type="text" class="inputTxtFecha" 
                                           id="fechaAltaTemporal" name="fechaAltaTemporal"
                                           maxlength="10"  size="10"
                                           value="<%=objEncMod != null && objEncMod.getFecAltaContrTempAsStr()!= null ? objEncMod.getFecAltaContrTempAsStr(): ""%>"
                                           onkeyup = "return SoloCaracteresFechaLanbide(this);"                                       
                                           onfocus="javascript:this.select();"/>
                                    <A id="afechaAltaTemporal" href="javascript:calClick(event);return false;" onClick="mostrarCalFechaABTemp(event);return false;" style="text-decoration:none;">
                                        <IMG style="border: 0px solid none" height="17" id="calFechaAltaTemporal" name="calFechaAltaTemporal" border="0" 
                                             src="<c:url value='/images/calendario/icono.gif'/>" > <!--width="25"-->
                                    </A>
                                </div>
                            </div>
                        </div>


                        <br><br>
                        <div class="lineaFormulario">
                            <div style="width: 340px; float: left;" class="etiqueta">
                                <%=meLanbide65I18n.getMensaje(idiomaUsuario,"label.fechaBajaTemp")%>
                            </div>
                            <div style="width: 300px; float: left;">
                                <div style="float: left;">
                                    <input type="text" class="inputTxtFecha" 
                                           id="fechaBajaTemporal" name="fechaBajaTemporal"
                                           maxlength="10"  size="10"
                                           value="<%=objEncMod != null && objEncMod.getFecBajaContrTempAsStr()!= null ? objEncMod.getFecBajaContrTempAsStr(): ""%>"
                                           onkeyup = "return SoloCaracteresFechaLanbide(this);"                                       
                                           onfocus="javascript:this.select();"/>
                                    <A id="afechaBajaTemporal" href="javascript:calClick(event);return false;" onClick="mostrarCalFechaBTemporal(event);return false;" style="text-decoration:none;">
                                        <IMG style="border: 0px solid none" height="17" id="calFechaBajaTemporal" name="calFechaBajaTemporal" border="0" 
                                             src="<c:url value='/images/calendario/icono.gif'/>" > <!--width="25"-->
                                    </A>
                                </div>
                            </div>
                        </div>


                        <br><br>

                        <div class="lineaFormulario">
                            <div style="width: 340px; float: left;" class="etiqueta">
                                <%=meLanbide65I18n.getMensaje(idiomaUsuario,"label.tipoJornada")%>
                            </div>
                            <div style="width: 500px; float: left;">
                                <div style="float: left;">
                                    <input type="text" name="codListaJornada" id="codListaJornada" size="5" class="inputCombo" value="" maxlength="5" onkeyup="xAMayusculas(this);"/>
                                    <input type="text" name="descListaJornada"  id="descListaJornada" size="40" class="inputCombo" readonly value="" />
                                    <a href="" id="anchorListaJornada" name="anchorListaJornada" title="">
                                        <span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonAplicacion" style="cursor:pointer;"></span>
                                    </a>
                                </div>
                            </div>

                        </div>
                        <br><br>

                        <div class="lineaFormulario">
                            <div style="width: 340px; float: left;" class="etiqueta">
                                <%=meLanbide65I18n.getMensaje(idiomaUsuario,"label.jornadaLaboralParcialPor")%>
                            </div>
                            <div style="width: 300px; float: left;">
                                <div style="float: left;">
                                    <input id="jornadaParcialPorAnexoC" name="jornadaParcialPorAnexoC" type="text" class="inputTexto" size="10" maxlength="6" onchange="reemplazarPuntos(this);"
                                           value="<%=objEncMod != null && objEncMod.getJornadaParcialPor()!=-1.0 ? String.valueOf(objEncMod.getJornadaParcialPor()).replace(".",",") : ""%>"/>
                                </div>
                            </div>
                        </div>
                        <br><br>

                    </div> 
                    <div class="lineaFormulario" style="margin-top: 100px">
                        <div class="botonera" style="text-align: center;">
                            <input type="button" id="btnAceptarAnexoC" name="btnAceptarAnexoC" class="botonGeneral" value="<%=meLanbide65I18n.getMensaje(idiomaUsuario, "btn.aceptar")%>" onclick="guardarDatosEncargado();"/>
                            <input type="button" id="btnCancelarAnexoC" name="btnCancelarAnexoC" class="botonGeneral" value="<%=meLanbide65I18n.getMensaje(idiomaUsuario, "btn.cancelar")%>" onclick="cancelar();"/>
                        </div>
                    </div>
                </div>
            </form>
            <script type="text/javascript">
                function comprobarJornada() {
                    if (document.getElementById("codListaJornada").value == "PARC") {
                        document.getElementById('jornadaParcialPorAnexoC').disabled = false;
                    } else {
                        document.getElementById('jornadaParcialPorAnexoC').disabled = true;
                        document.getElementById('jornadaParcialPorAnexoC').value = '';
                    }
                }

                function comprobarContrato() {
                    if (document.getElementById("codListaContrato").value == "I") {
                        document.getElementById('fechaAltaIndefinido').disabled = false;
                        document.getElementById('fechaAltaTemporal').disabled = true;
                        document.getElementById('fechaBajaTemporal').disabled = true;
                        document.getElementById('fechaAltaTemporal').value = '';
                        document.getElementById('fechaBajaTemporal').value = '';
                        document.getElementById('afechaAltaTemporal').style.visibility = 'hidden';
                        document.getElementById('afechaBajaTemporal').style.visibility = 'hidden';
                        document.getElementById('afechaAltaIndefinido').style.visibility = '';
                    } else {
                        document.getElementById('fechaAltaIndefinido').disabled = true;
                        document.getElementById('afechaAltaIndefinido').style.visibility = 'hidden';
                        document.getElementById('fechaAltaIndefinido').value = '';
                        document.getElementById('fechaAltaTemporal').disabled = false;
                        document.getElementById('fechaBajaTemporal').disabled = false;
                        document.getElementById('afechaAltaTemporal').style.visibility = '';
                        document.getElementById('afechaBajaTemporal').style.visibility = '';
                    }
                }

                /*desplegable jornada*/
                var comboListaJornada;
                var listaCodigosJornada = new Array();
                var listaDescripcionesJornada = new Array();

                listaCodigosJornada[0] = "";
                listaDescripcionesJornada[0] = "";
                var contador1 = 0;

                <logic:iterate id="jornada" name="listaJornada" scope="request">
                listaCodigosJornada[contador1] = ['<bean:write name="jornada" property="des_val_cod" />'];
                listaDescripcionesJornada[contador1] = ['<bean:write name="jornada" property="des_nom" />'];
                contador1++;
                </logic:iterate>

                var comboListaJornada = new Combo("ListaJornada");
                comboListaJornada.addItems(listaCodigosJornada, listaDescripcionesJornada);
                comboListaJornada.change = cargarDatosJornada;

                function buscaCodigoJornada(jornada) {
                    comboListaJornada.buscaCodigo(jornada);
                }

                function cargarDatosJornada() {
                    var jornadaSeleccionado = document.getElementById("codListaJornada").value;
                    buscaCodigoJornada(jornadaSeleccionado);
                    comprobarJornada();
                }

                /*desplegable contrato*/
                var comboListaContrato;
                var listaCodigosContrato = new Array();
                var listaDescripcionesContrato = new Array();

                listaCodigosContrato[0] = "";
                listaDescripcionesContrato[0] = "";
                contador1 = 0;

                <logic:iterate id="contrato" name="listaContrato" scope="request">
                listaCodigosContrato[contador1] = ['<bean:write name="contrato" property="des_val_cod" />'];
                listaDescripcionesContrato[contador1] = ['<bean:write name="contrato" property="des_nom" />'];
                contador1++;
                </logic:iterate>

                var comboListaContrato = new Combo("ListaContrato");
                comboListaContrato.addItems(listaCodigosContrato, listaDescripcionesContrato);
                comboListaContrato.change = cargarDatosContrato;

                function buscaCodigoContrato(contrato) {
                    comboListaContrato.buscaCodigo(contrato);
                }

                function cargarDatosContrato() {
                    var contratoSeleccionado = document.getElementById("codListaContrato").value;
                    buscaCodigoContrato(contratoSeleccionado);
                    comprobarContrato();
                }
                
                 
                /*desplegable Pensionista*/
                var comboListaPensionista;
                var listaCodigosPensionista = new Array();
                var listaDescripcionesPensionista = new Array();

                listaCodigosPensionista[0] = "";
                listaDescripcionesPensionista[0] = "";
                contador1 = 0;

                <logic:iterate id="pensionista" name="listaPensionista" scope="request">
                listaCodigosPensionista[contador1] = ['<bean:write name="pensionista" property="des_val_cod" />'];
                listaDescripcionesPensionista[contador1] = ['<bean:write name="pensionista" property="des_nom" />'];
                contador1++;
                </logic:iterate>

                var comboListaPensionista = new Combo("ListaPensionista");
                comboListaPensionista.addItems(listaCodigosPensionista, listaDescripcionesPensionista);
                comboListaPensionista.change = cargarDatosPensionista;

                function buscaCodigoPensionista(Pensionista) {
                    comboListaPensionista.buscaCodigo(Pensionista);
                }

                function cargarDatosPensionista() {
                    var pensionistaSeleccionado = document.getElementById("codListaPensionista").value;
                    buscaCodigoPensionista(pensionistaSeleccionado);
                }
                
                 /*desplegable TipoPensionista*/
                var comboListaTipoPensionista;
                var listaCodigosTipoPensionista = new Array();
                var listaDescripcionesTipoPensionista = new Array();

                listaCodigosTipoPensionista[0] = "";
                listaDescripcionesTipoPensionista[0] = "";
                contador1 = 0;

                <logic:iterate id="tipoPensionista" name="listaTipoPensionista" scope="request">
                listaCodigosTipoPensionista[contador1] = ['<bean:write name="tipoPensionista" property="des_val_cod" />'];
                listaDescripcionesTipoPensionista[contador1] = ['<bean:write name="tipoPensionista" property="des_nom" />'];
                contador1++;
                </logic:iterate>

                var comboListaTipoPensionista = new Combo("ListaTipoPensionista");
                comboListaTipoPensionista.addItems(listaCodigosTipoPensionista, listaDescripcionesTipoPensionista);
                comboListaTipoPensionista.change = cargarDatosTipoPensionista;

                function buscaCodigoTipoPensionista(TipoPensionista) {
                    comboListaTipoPensionista.buscaCodigo(TipoPensionista);
                }

                function cargarDatosTipoPensionista() {
                    var tipopensionistaSeleccionado = document.getElementById("codListaTipoPensionista").value;
                    buscaCodigoTipoPensionista(tipopensionistaSeleccionado);
                }

                if (<%=nuevo%> != null && <%=nuevo%> == 0) {
                    rellenarDatModificar();
                }

                function rellenarDatModificar() {
                    buscaCodigoJornada('<%=objEncMod != null && objEncMod.getTipoJornada() != null ? objEncMod.getTipoJornada() : ""%>');
                    buscaCodigoContrato('<%=objEncMod != null && objEncMod.getTipoContrato() != null ? objEncMod.getTipoContrato() : ""%>');
                    if ('<%=objEncMod != null && objEncMod.getTipoPensionista() != null ? objEncMod.getTipoPensionista() : ""%>'!='0'){
                    buscaCodigoPensionista('<%=objEncMod != null && objEncMod.getTipoPensionista() != null ? objEncMod.getTipoPensionista() : ""%>');
                }
                    buscaCodigoTipoPensionista('<%=objEncMod != null && objEncMod.getPensionista() != null ? objEncMod.getPensionista() : ""%>');
                    mostrarOcultarPensionista();
                }
            </script>
            <div id="popupcalendar" class="text"></div>        
        </div>
    </body>
</html>

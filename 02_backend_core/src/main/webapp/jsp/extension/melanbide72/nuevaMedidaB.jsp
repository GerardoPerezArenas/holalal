<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>

<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide72.i18n.MeLanbide72I18n" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide72.vo.MedidaA1BCVO" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide72.util.ConstantesMeLanbide72" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide72.util.ConfigurationParameter" %>
<%@page import="java.util.ArrayList" %>
<%@page import="java.util.List" %>
<html>
    <head>
        <meta http-equiv="X-UA-Compatible" content="IE=Edge" />
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <%
            MedidaA1BCVO datModif = new MedidaA1BCVO();
            
            String codOrganizacion = "";
            String codTipoMedida = "";
            String nuevo = "";
            String expediente = "";
            
            
            MeLanbide72I18n meLanbide72I18n = MeLanbide72I18n.getInstance();
            
            expediente = (String)request.getAttribute("numExp");
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

                codOrganizacion  = request.getParameter("codOrganizacionModulo");
                nuevo = (String)request.getAttribute("nuevo");
                if(request.getAttribute("datModif") != null)
                {
                    datModif = (MedidaA1BCVO)request.getAttribute("datModif");
                }
            }
            catch(Exception ex)
            {
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
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/extension/melanbide72/melanbide72.css"/>

        <script type="text/javascript">
            var APP_CONTEXT_PATH = '<%=request.getContextPath()%>';
        </script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/general.js"></script>
        <script type="text/javascript" src="<c:url value='/scripts/listaComboBox.js'/>"></script>
        <script type="text/javascript" src="<c:url value='/scripts/calendario.js'/>"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/validaciones.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/popup.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/meLanbide72/JavaScriptUtil.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/meLanbide72/InputMask.js"></script>
        <!-- Eventos onKeyPress compatibilidad firefox  -->
        <!--<script type="text/javascript" src="http://code.jquery.com/jquery-1.4.4.js"></script>-->


        <script type="text/javascript">

            var mensajeValidacion = '';

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

            function guardarDatos() {

                if (validarDatos()) {
                    var ajax = getXMLHttpRequest();
                    var nodos = null;
                    var CONTEXT_PATH = '<%=request.getContextPath()%>';
                    var url = CONTEXT_PATH + "/PeticionModuloIntegracion.do";
                    var parametros = "";
                    var nuevo = "<%=nuevo%>";

                    var nombre = "";
                    if (document.getElementById('nombre').value == null || document.getElementById('nombre').value == '') {
                        nombre = "";
                    } else {
                        nombre = document.getElementById('nombre').value.replace(/\'/g, "''");
                    }

                    if (nuevo != null && nuevo == "1") {
                        parametros = "tarea=preparar&modulo=MELANBIDE72&operacion=crearNuevaMedidaB&tipo=0"
                                + "&expediente=<%=expediente%>"
                                + "&nombre=" + nombre
                                + "&nif=" + document.getElementById('nif').value
                                + "&importeAnual=" + document.getElementById('importeAnual').value
                                ;
                    } else {
                        parametros = "tarea=preparar&modulo=MELANBIDE72&operacion=modificarMedidaB&tipo=0"
                                + "&id=<%=datModif != null && datModif.getId() != null ? datModif.getId().toString() : ""%>"
                                + "&nombre=" + nombre
                                + "&nif=" + document.getElementById('nif').value
                                + "&importeAnual=" + document.getElementById('importeAnual').value
                                ;
                    }
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
                                    } else if (hijosFila[cont].nodeName == "NOMBRE") {
                                        if (hijosFila[cont].childNodes[0].nodeValue != "null") {
                                            fila[1] = hijosFila[cont].childNodes[0].nodeValue;
                                        } else {
                                            fila[1] = '-';
                                        }
                                    } else if (hijosFila[cont].nodeName == "NIF_CIF") {
                                        if (hijosFila[cont].childNodes[0].nodeValue != "null") {
                                            fila[2] = hijosFila[cont].childNodes[0].nodeValue;
                                        } else {
                                            fila[2] = '-';
                                        }
                                    } else if (hijosFila[cont].nodeName == "IMPORTE_ANUAL") {
                                        if (hijosFila[cont].childNodes[0].nodeValue != "null") {
                                            fila[3] = hijosFila[cont].childNodes[0].nodeValue;
                                            var tex = fila[3].toString();
                                            tex = tex.replace(".", ",");
                                            fila[3] = tex;
                                        } else {
                                            fila[3] = '-';
                                        }
                                    }
                                }// for elementos de la fila
                                lista[j] = fila;
                                fila = new Array();
                            }
                        }//for(j=0;hijos!=null && j<hijos.length;j++)
                        if (codigoOperacion == "0") {
                            //jsp_alerta("A",'Correcto');
                            self.parent.opener.retornoXanelaAuxiliar(lista);
                            cerrarVentana();
                        } else if (codigoOperacion == "1") {
                            jsp_alerta("A", '<%=meLanbide72I18n.getMensaje(idiomaUsuario,"error.errorBD")%>');
                        } else if (codigoOperacion == "2") {
                            jsp_alerta("A", '<%=meLanbide72I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                        } else if (codigoOperacion == "3") {
                            jsp_alerta("A", '<%=meLanbide72I18n.getMensaje(idiomaUsuario,"error.pasoParametros")%>');
                        } else {
                            jsp_alerta("A", '<%=meLanbide72I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                        }
                    } catch (Err) {
                    }//try-catch
                } else {
                    jsp_alerta("A", mensajeValidacion);
                }
            }

            function cancelar() {
                var resultado = jsp_alerta('', '<%=meLanbide72I18n.getMensaje(idiomaUsuario, "msg.preguntaCancelar")%>');
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

            function validarNumericoDecimalPrecision(numero, longTotal, longParteDecimal) {
                try {
                    var longParteEntera = parseInt(longTotal) - parseInt(longParteDecimal);
                    if (Trim(numero) != '') {
                        var valor = numero;
                        var pattern = '^[0-9]{1,' + longParteEntera + '}(,[0-9]{1,' + longParteDecimal + '})?$';
                        var regex = new RegExp(pattern);
                        var result = regex.test(valor);
                        return result;
                    } else {
                        return true;
                    }
                } catch (err) {
                    alert(err);
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

            function validarCIF(cif) {
                par = 0;
                non = 0;
                letras = "ABCDEFGHJKLMNPQRSUVW";
                letrasInicio = "KPQS";
                letrasFin = "ABEH";
                letrasPosiblesFin = "JABCDEFGHI";
                let = cif.charAt(0).toUpperCase();

                if (cif.length != 9) {
                    return false;
                } else {
                    caracterControl = cif.charAt(8).toUpperCase();
                }

                for (zz = 2; zz < 8; zz += 2) {
                    par = par + parseInt(cif.charAt(zz));
                }

                for (zz = 1; zz < 9; zz += 2) {
                    nn = 2 * parseInt(cif.charAt(zz));
                    if (nn > 9)
                        nn = 1 + (nn - 10);
                    non = non + nn;
                }

                parcial = par + non;
                control = (10 - (parcial % 10));
                if (control == 10)
                    control = 0;

                /*
                 * El valor del último carácter:
                 * Será una LETRA si la clave de entidad es K, P, Q ó S.
                 * Será un NUMERO si la entidad es A, B, E ó H.
                 * Para otras claves de entidad: el dígito podrá ser tanto número como letra.
                 * */

                if (letrasInicio.indexOf(let) != -1) {
                    return (letrasPosiblesFin.charAt(control) == caracterControl);
                } else if (letrasFin.indexOf(let) != -1) {
                    return (caracterControl == control);
                } else if (letras.indexOf(let) != -1) {
                    return ((letrasPosiblesFin.charAt(control) == caracterControl) || (caracterControl == control));
                } else {
                    return false;
                }
            }

            function validarNIF(abc) {
                dni = abc.substring(0, abc.length - 1);
                let = abc.charAt(abc.length - 1);
                if (!isNaN(let)) {
                    return false;
                } else {
                    cadena = "TRWAGMYFPDXBNJZSQVHLCKET";
                    posicion = dni % 23;
                    letra = cadena.substring(posicion, posicion + 1);
                    if (letra != let.toUpperCase())
                        return false;
                }
                return true;
            }

            function validarDatos() {
                mensajeValidacion = "";
                var correcto = true;

                if (document.getElementById('nombre').value == null || document.getElementById('nombre').value == '') {
                    mensajeValidacion = '<%=meLanbide72I18n.getMensaje(idiomaUsuario, "msg.nombreObligatorio")%>';
                    return false;
                }

                if (document.getElementById('nif').value == null || document.getElementById('nif').value == '') {
                    mensajeValidacion = '<%=meLanbide72I18n.getMensaje(idiomaUsuario, "msg.nifObligatorio")%>';
                    return false;
                } else {
                    if (validarCIF(document.getElementById('nif').value) || validarNIF(document.getElementById('nif').value)) {
                    } else {
                        mensajeValidacion = '<%=meLanbide72I18n.getMensaje(idiomaUsuario, "msg.nif.errorNIF_CIF")%>';
                        return false;
                    }
                }

                if (document.getElementById('importeAnual').value == null || document.getElementById('importeAnual').value == '') {
                    mensajeValidacion = '<%=meLanbide72I18n.getMensaje(idiomaUsuario, "msg.importeAnualObligatorio")%>';
                    return false;
                } else {
                    if (!validarNumericoDecimalPrecision(document.getElementById('importeAnual').value, 9, 2)) {
                        mensajeValidacion = '<%=meLanbide72I18n.getMensaje(idiomaUsuario, "msg.importeAnual.errNumerico")%>';
                        return false;
                    }
                }

                return correcto;
            }

            function compruebaTamanoCampo(elemento, maxTex) {
                var texto = elemento.value;
                if (texto.length > maxTex) {
                    jsp_alerta("A", '<%=meLanbide72I18n.getMensaje(idiomaUsuario, "msg.errExcdTexto")%>');
                    elemento.focus();
                    return false;
                }
                return true;
            }

        </script>

    </head>
    <body>
        <div class="contenidoPantalla">
            <form><!--    action="/PeticionModuloIntegracion.do" method="POST" -->
                <div style="width: 100%; padding: 10px; text-align: left;">
                    <div class="sub3titulo" style="clear: both; text-align: center; font-size: 14px;">
                        <span>
                            <%=meLanbide72I18n.getMensaje(idiomaUsuario,"label.nuevaMedida")%>
                        </span>
                    </div>                        
                    <br><br>
                    <div class="lineaFormulario">
                        <div style="width: 190px; float: left;" class="etiqueta">
                            <%=meLanbide72I18n.getMensaje(idiomaUsuario,"label.nombre")%>
                        </div>
                        <div style="width: 450px; float: left;">
                            <div style="float: left;">
                                <input id="nombre" name="nombre" type="text" class="inputTexto" size="30" maxlength="50" 
                                       value="<%=datModif != null && datModif.getNombre() != null ? datModif.getNombre() : ""%>"/>
                            </div>
                        </div>
                    </div>    
                    <br><br> 
                    <div class="lineaFormulario">
                        <div style="width: 190px; float: left;" class="etiqueta">
                            <%=meLanbide72I18n.getMensaje(idiomaUsuario,"label.nif")%>
                        </div>
                        <div style="width: 450px; float: left;">
                            <div style="float: left;">
                                <input id="nif" name="nif" type="text" class="inputTexto" size="15" maxlength="15" 
                                       value="<%=datModif != null && datModif.getNif() != null ? datModif.getNif() : ""%>"/>
                            </div>
                        </div>
                    </div
                    <br><br>
                    <div class="lineaFormulario">
                        <div style="width: 190px; float: left;" class="etiqueta">
                            <%=meLanbide72I18n.getMensaje(idiomaUsuario,"label.importeAnual")%>
                        </div>
                        <div style="width: 450px; float: left;">
                            <div style="float: left;">
                                <input id="importeAnual" name="importeAnual" type="text" class="inputTexto" size="9" maxlength="9" 
                                       value="<%=datModif != null && datModif.getImporteAnual() != null ? datModif.getImporteAnual().toString().replaceAll("\\.", ","): ""%>" onchange="reemplazarPuntos(this);"/>
                            </div>
                        </div>
                    </div>

                    <br><br><br><br>

                    <div class="lineaFormulario">
                        <div class="botonera" style="text-align: center;">
                            <input type="button" id="btnAceptar" name="btnAceptar" class="botonGeneral" value="<%=meLanbide72I18n.getMensaje(idiomaUsuario, "btn.aceptar")%>" onclick="guardarDatos();"/>
                            <input type="button" id="btnCancelar" name="btnCancelar" class="botonGeneral" value="<%=meLanbide72I18n.getMensaje(idiomaUsuario, "btn.cancelar")%>" onclick="cancelar();"/>
                        </div>
                    </div>
                </div>
                <div id="reloj" style="font-size:20px;"></div>
            </form>
            <div id="popupcalendar" class="text"></div>        
        </div>
    </body>
</html>
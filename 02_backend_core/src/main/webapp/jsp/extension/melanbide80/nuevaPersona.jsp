<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>

<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide80.i18n.MeLanbide80I18n" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide80.vo.PersonaVO" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide80.vo.DesplegableAdmonLocalVO" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide80.util.ConstantesMeLanbide80" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide80.util.ConfigurationParameter" %>
<%@page import="java.util.ArrayList" %>
<%@page import="java.util.List" %>
<%@page import="java.text.SimpleDateFormat" %>
<html>
    <head>
        <meta http-equiv="X-UA-Compatible" content="IE=Edge" />
        <%
            PersonaVO datModif = new PersonaVO();
            PersonaVO objectVO = new PersonaVO();
        
            String codOrganizacion = "";
            String nuevo = "";
        
            String expediente = "";
        
            String fecinisit = "";
            String fecfinsit = "";
        
            MeLanbide80I18n meLanbide80I18n = MeLanbide80I18n.getInstance();

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
                    datModif = (PersonaVO)request.getAttribute("datModif");
                    SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
                
                    if (datModif.getFecinisit()!=null){
                        fecinisit = formatoFecha.format(datModif.getFecinisit());
                    }
                    if (datModif.getFecfinsit()!=null){
                        fecfinsit = formatoFecha.format(datModif.getFecfinsit());
                    }
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
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/extension/melanbide80/melanbide80.css"/>

        <script type="text/javascript">
            var APP_CONTEXT_PATH = '<%=request.getContextPath()%>';
        </script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/general.js"></script>
        <script type="text/javascript" src="<c:url value='/scripts/listaComboBox.js'/>"></script>
        <script type="text/javascript" src="<c:url value='/scripts/calendario.js'/>"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/validaciones.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/popup.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/meLanbide80/JavaScriptUtil.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/meLanbide80/Parsers.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/meLanbide80/InputMask.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/meLanbide80/lanbide.js"></script>
        <!-- Eventos onKeyPress compatibilidad firefox  -->
        <!--<script type="text/javascript" src="http://code.jquery.com/jquery-1.4.4.js"></script>-->

        <script type="text/javascript">

            var mensajeValidacion = '';

            //Desplegable tipo contrato
            var comboListaTipcontA;
            var listaCodigosTipcontA = new Array();
            var listaDescripcionesTipcontA = new Array();

            function buscaCodigoTipcontA(tipcontA) {
                comboListaTipcontA.buscaCodigo(tipcontA);
            }
            function cargarDatosTipcontA() {
                var tipcontASeleccionado = document.getElementById("codListaTipcontA").value;
                buscaCodigoTipcontA(tipcontASeleccionado);
                comprobarTipoContrato();
            }

            //Desplegable jornada
            var comboListaJornada;
            var listaCodigosJornada = new Array();
            var listaDescripcionesJornada = new Array();

            function buscaCodigoJornada(jornada) {
                comboListaJornada.buscaCodigo(jornada);
            }
            function cargarDatosJornada() {
                var jornadaSeleccionado = document.getElementById("codListaJornada").value;
                buscaCodigoJornada(jornadaSeleccionado);
                comprobarJornada();
            }

            //Desplegable situación
            var comboListaSituacion;
            var listaCodigosSituacion = new Array();
            var listaDescripcionesSituacion = new Array();

            function buscaCodigoSituacion(situacion) {
                comboListaSituacion.buscaCodigo(situacion);
            }
            function cargarDatosSituacion() {
                var situacionSeleccionado = document.getElementById("codListaSituacion").value;
                buscaCodigoSituacion(situacionSeleccionado);
                comprobarSituacion();
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

            function rellenardatModificar() {
                if ('<%=datModif%>' != null) {
                    buscaCodigoTipcontA('<%=datModif.getTipcontA() != null ? datModif.getTipcontA() : ""%>');
                    buscaCodigoJornada('<%=datModif.getJornada() != null ? datModif.getJornada() : ""%>');
                    buscaCodigoSituacion('<%=datModif.getSituacion() != null ? datModif.getSituacion() : ""%>');
                } else
                    alert('No hemos podido cargar los datos para modificar');
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

                if (validarDatosNumericosVacios() && validarDatos()) {
                    var ajax = getXMLHttpRequest();
                    var nodos = null;
                    var CONTEXT_PATH = '<%=request.getContextPath()%>';
                    var url = CONTEXT_PATH + "/PeticionModuloIntegracion.do";
                    var parametros = "";
                    var nuevo = "<%=nuevo%>";
                    var nombre = "";
                    var apel1 = "";
                    var apel2 = "";
                    if (document.getElementById('nombre').value == null || document.getElementById('nombre').value == '') {
                        nombre = "";
                    } else {
                        nombre = document.getElementById('nombre').value.replace(/\'/g, "''");
                    }
                    if (document.getElementById('apel1').value == null || document.getElementById('apel1').value == '') {
                        apel1 = "";
                    } else {
                        apel1 = document.getElementById('apel1').value.replace(/\'/g, "''");
                    }
                    if (document.getElementById('apel2').value == null || document.getElementById('apel2').value == '') {
                        apel2 = "";
                    } else {
                        apel2 = document.getElementById('apel2').value.replace(/\'/g, "''");
                    }

                    if (nuevo != null && nuevo == "1") {
                        parametros = "tarea=preparar&modulo=MELANBIDE80&operacion=crearNuevaPersona&tipo=0"
                                + '&expediente=<%=expediente%>'
                                + "&dni=" + document.getElementById('dni').value
                                + '&nombre=' + escape(nombre)
                                + '&apel1=' + escape(apel1)
                                + '&apel2=' + escape(apel2)
                                + "&tipcontA=" + document.getElementById('codListaTipcontA').value
                                + "&tipcontB=" + document.getElementById('tipcontB').value
                                + "&jornada=" + document.getElementById('codListaJornada').value
                                + "&porjorpar=" + document.getElementById('porjorpar').value
                                + "&situacion=" + document.getElementById('codListaSituacion').value
                                + "&reducjorn=" + document.getElementById('reducjorn').value
                                + "&fecinisit=" + document.getElementById('fecinisit').value
                                + "&fecfinsit=" + document.getElementById('fecfinsit').value
                                + "&numdiasit=" + document.getElementById('numdiasit').value
                                + "&baseregul=" + document.getElementById('baseregul').value
                                + "&impprest=" + document.getElementById('impprest').value
                                + "&complsal=" + document.getElementById('complsal').value
                                + "&impsubvsol=" + document.getElementById('impsubvsol').value
                                ;

                    } else {
                        parametros = "tarea=preparar&modulo=MELANBIDE80&operacion=modificarPersona&tipo=0"
                                + "&id=<%=datModif != null && datModif.getId() != null ? datModif.getId().toString() : ""%>"
                                + "&dni=" + document.getElementById('dni').value
                                + '&nombre=' + escape(nombre)
                                + '&apel1=' + escape(apel1)
                                + '&apel2=' + escape(apel2)
                                + "&tipcontA=" + document.getElementById('codListaTipcontA').value
                                + "&tipcontB=" + document.getElementById('tipcontB').value
                                + "&jornada=" + document.getElementById('codListaJornada').value
                                + "&porjorpar=" + document.getElementById('porjorpar').value
                                + "&situacion=" + document.getElementById('codListaSituacion').value
                                + "&reducjorn=" + document.getElementById('reducjorn').value
                                + "&fecinisit=" + document.getElementById('fecinisit').value
                                + "&fecfinsit=" + document.getElementById('fecfinsit').value
                                + "&numdiasit=" + document.getElementById('numdiasit').value
                                + "&baseregul=" + document.getElementById('baseregul').value
                                + "&impprest=" + document.getElementById('impprest').value
                                + "&complsal=" + document.getElementById('complsal').value
                                + "&impsubvsol=" + document.getElementById('impsubvsol').value
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
                                    } else if (hijosFila[cont].nodeName == "DNI") {
                                        if (hijosFila[cont].childNodes.length > 0) {
                                            fila[1] = hijosFila[cont].childNodes[0].nodeValue;
                                        } else {
                                            fila[1] = '-';
                                        }
                                    } else if (hijosFila[cont].nodeName == "NOMBRE") {
                                        if (hijosFila[cont].childNodes[0].nodeValue != "null") {
                                            fila[2] = hijosFila[cont].childNodes[0].nodeValue;
                                        } else {
                                            fila[2] = '-';
                                        }
                                    } else if (hijosFila[cont].nodeName == "APEL1") {
                                        if (hijosFila[cont].childNodes[0].nodeValue != "null") {
                                            fila[3] = hijosFila[cont].childNodes[0].nodeValue;
                                        } else {
                                            fila[3] = '-';
                                        }
                                    } else if (hijosFila[cont].nodeName == "APEL2") {
                                        if (hijosFila[cont].childNodes[0].nodeValue != "null") {
                                            fila[4] = hijosFila[cont].childNodes[0].nodeValue;
                                        } else {
                                            fila[4] = '-';
                                        }
                                    } else if (hijosFila[cont].nodeName == "DESTIPCONTA") {
                                        if (hijosFila[cont].childNodes[0].nodeValue != "null") {
                                            fila[5] = hijosFila[cont].childNodes[0].nodeValue;
                                        } else {
                                            fila[5] = '-';
                                        }
                                    } else if (hijosFila[cont].nodeName == "TIPCONTB") {
                                        if (hijosFila[cont].childNodes[0].nodeValue != "null") {
                                            fila[6] = hijosFila[cont].childNodes[0].nodeValue;
                                            var tex = fila[6].toString();
                                            tex = tex.replace(".", ",");
                                            fila[6] = tex;
                                        } else {
                                            fila[6] = '-';
                                        }
                                    } else if (hijosFila[cont].nodeName == "DESJORNADA") {
                                        if (hijosFila[cont].childNodes[0].nodeValue != "null") {
                                            fila[7] = hijosFila[cont].childNodes[0].nodeValue;
                                        } else {
                                            fila[7] = '-';
                                        }
                                    } else if (hijosFila[cont].nodeName == "PORJORPAR") {
                                        if (hijosFila[cont].childNodes[0].nodeValue != "null") {
                                            fila[8] = hijosFila[cont].childNodes[0].nodeValue;
                                            var tex = fila[8].toString();
                                            tex = tex.replace(".", ",");
                                            fila[8] = tex;
                                        } else {
                                            fila[8] = '-';
                                        }
                                    } else if (hijosFila[cont].nodeName == "DESSITUACION") {
                                        if (hijosFila[cont].childNodes[0].nodeValue != "null") {
                                            fila[9] = hijosFila[cont].childNodes[0].nodeValue;
                                        } else {
                                            fila[9] = '-';
                                        }
                                    } else if (hijosFila[cont].nodeName == "REDUCJORN") {
                                        if (hijosFila[cont].childNodes[0].nodeValue != "null") {
                                            fila[10] = hijosFila[cont].childNodes[0].nodeValue;
                                            var tex = fila[10].toString();
                                            tex = tex.replace(".", ",");
                                            fila[10] = tex;
                                        } else {
                                            fila[10] = '-';
                                        }
                                    } else if (hijosFila[cont].nodeName == "FECINISIT") {
                                        if (hijosFila[cont].childNodes.length > 0) {
                                            fila[11] = hijosFila[cont].childNodes[0].nodeValue;
                                        } else {
                                            fila[11] = '-';
                                        }
                                    } else if (hijosFila[cont].nodeName == "FECFINSIT") {
                                        if (hijosFila[cont].childNodes.length > 0) {
                                            fila[12] = hijosFila[cont].childNodes[0].nodeValue;
                                        } else {
                                            fila[12] = '-';
                                        }
                                    } else if (hijosFila[cont].nodeName == "NUMDIASIT") {
                                        if (hijosFila[cont].childNodes[0].nodeValue != "null") {
                                            fila[13] = hijosFila[cont].childNodes[0].nodeValue;
                                        } else {
                                            fila[13] = '-';
                                        }
                                    } else if (hijosFila[cont].nodeName == "BASEREGUL") {
                                        if (hijosFila[cont].childNodes[0].nodeValue != "null") {
                                            fila[14] = hijosFila[cont].childNodes[0].nodeValue;
                                            var tex = fila[14].toString();
                                            tex = tex.replace(".", ",");
                                            fila[14] = tex;
                                        } else {
                                            fila[14] = '-';
                                        }
                                    } else if (hijosFila[cont].nodeName == "IMPPREST") {
                                        if (hijosFila[cont].childNodes[0].nodeValue != "null") {
                                            fila[15] = hijosFila[cont].childNodes[0].nodeValue;
                                            var tex = fila[15].toString();
                                            tex = tex.replace(".", ",");
                                            fila[15] = tex;
                                        } else {
                                            fila[15] = '-';
                                        }
                                    } else if (hijosFila[cont].nodeName == "COMPLSAL") {
                                        if (hijosFila[cont].childNodes[0].nodeValue != "null") {
                                            fila[16] = hijosFila[cont].childNodes[0].nodeValue;
                                            var tex = fila[16].toString();
                                            tex = tex.replace(".", ",");
                                            fila[16] = tex;
                                        } else {
                                            fila[16] = '-';
                                        }
                                    } else if (hijosFila[cont].nodeName == "IMPSUBVSOL") {
                                        if (hijosFila[cont].childNodes[0].nodeValue != "null") {
                                            fila[17] = hijosFila[cont].childNodes[0].nodeValue;
                                            var tex = fila[17].toString();
                                            tex = tex.replace(".", ",");
                                            fila[17] = tex;
                                        } else {
                                            fila[17] = '-';
                                        }
                                    }
                                }
                                lista[j] = fila;
                                fila = new Array();
                            }
                        }//for(j=0;hijos!=null && j<hijos.length;j++)
                        if (codigoOperacion == "0") {
                            //jsp_alerta("A",'Correcto');
                            self.parent.opener.retornoXanelaAuxiliar(lista);
                            cerrarVentana();
                        } else if (codigoOperacion == "1") {
                            jsp_alerta("A", '<%=meLanbide80I18n.getMensaje(idiomaUsuario,"error.errorBD")%>');
                        } else if (codigoOperacion == "2") {
                            jsp_alerta("A", '<%=meLanbide80I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                        } else if (codigoOperacion == "3") {
                            jsp_alerta("A", '<%=meLanbide80I18n.getMensaje(idiomaUsuario,"error.pasoParametros")%>');
                        } else {
                            jsp_alerta("A", '<%=meLanbide80I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                        }
                    } catch (Err) {
                    }//try-catch
                } else {
                    jsp_alerta("A", mensajeValidacion);
                }
            }

            function cancelar() {
                var resultado = jsp_alerta('', '<%=meLanbide80I18n.getMensaje(idiomaUsuario, "msg.preguntaCancelar")%>');
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

            function validarDatosNumericosVacios() {
                mensajeValidacion = "";
                var correcto = true;

                if (document.getElementById('tipcontB').value == null || document.getElementById('tipcontB').value == '') {
                } else {
                    if (!validarNumericoDecimalPrecision(document.getElementById('tipcontB').value, 5, 2)) {
                        mensajeValidacion = '<%=meLanbide80I18n.getMensaje(idiomaUsuario, "msg.tipcontB.errNumerico")%>';
                        return false;
                    } else {
                        if (!validarNumericoPorcentajeCien(document.getElementById('tipcontB').value)) {
                            mensajeValidacion = '<%=meLanbide80I18n.getMensaje(idiomaUsuario, "msg.tipcontB.errRango")%>';
                            return false;
                        }
                    }
                }

                if (document.getElementById('porjorpar').value == null || document.getElementById('porjorpar').value == '') {
                } else {
                    if (!validarNumericoDecimalPrecision(document.getElementById('porjorpar').value, 5, 2)) {
                        mensajeValidacion = '<%=meLanbide80I18n.getMensaje(idiomaUsuario, "msg.porjorpar.errNumerico")%>';
                        return false;
                    } else {
                        if (!validarNumericoPorcentajeCien(document.getElementById('porjorpar').value)) {
                            mensajeValidacion = '<%=meLanbide80I18n.getMensaje(idiomaUsuario, "msg.porjorpar.errRango")%>';
                            return false;
                        }
                    }
                }

                if (document.getElementById('reducjorn').value == null || document.getElementById('reducjorn').value == '') {
                } else {
                    if (!validarNumericoDecimalPrecision(document.getElementById('reducjorn').value, 5, 2)) {
                        mensajeValidacion = '<%=meLanbide80I18n.getMensaje(idiomaUsuario, "msg.reducjorn.errNumerico")%>';
                        return false;
                    } else {
                        if (!validarNumericoPorcentajeCien(document.getElementById('reducjorn').value)) {
                            mensajeValidacion = '<%=meLanbide80I18n.getMensaje(idiomaUsuario, "msg.reducjorn.errRango")%>';
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

            function validarDatos() {
                mensajeValidacion = "";
                var correcto = true;

                var dni = document.getElementById('dni').value;
                if (dni == null || dni == '') {
                    mensajeValidacion = '<%=meLanbide80I18n.getMensaje(idiomaUsuario, "msg.dni")%>';
                    return false;
                }

                var nombre = document.getElementById('nombre').value;
                if (nombre == null || nombre == '') {
                    mensajeValidacion = '<%=meLanbide80I18n.getMensaje(idiomaUsuario, "msg.nombre")%>';
                    return false;
                }

                var apel1 = document.getElementById('apel1').value;
                if (apel1 == null || apel1 == '') {
                    mensajeValidacion = '<%=meLanbide80I18n.getMensaje(idiomaUsuario, "msg.apel1")%>';
                    return false;
                }

                var tipcontA = document.getElementById('codListaTipcontA').value;
                if (tipcontA == null || tipcontA == '') {
                    mensajeValidacion = '<%=meLanbide80I18n.getMensaje(idiomaUsuario, "msg.tipcontA")%>';
                    return false;
                }
                var tipcontB = document.getElementById('tipcontB').value;
                if ((tipcontA == 'BRI' || tipcontA == 'BRT') && tipcontB != '85' && tipcontB != '75') {
                    mensajeValidacion = '<%=meLanbide80I18n.getMensaje(idiomaUsuario, "msg.tipcontB")%>';
                    return false;
                }

                var jornada = document.getElementById('codListaJornada').value;
                if (jornada == null || jornada == '') {
                    mensajeValidacion = '<%=meLanbide80I18n.getMensaje(idiomaUsuario, "msg.jornada")%>';
                    return false;
                }
                var porjorpar = document.getElementById('porjorpar').value;
                if (jornada == 'PARC' && (porjorpar == null || porjorpar == '')) {
                    mensajeValidacion = '<%=meLanbide80I18n.getMensaje(idiomaUsuario, "msg.porjorpar")%>';
                    return false;
                }

                var situacion = document.getElementById('codListaSituacion').value;
                if (situacion == null || situacion == '') {
                    mensajeValidacion = '<%=meLanbide80I18n.getMensaje(idiomaUsuario, "msg.situacion")%>';
                    return false;
                }
                var reducjorn = document.getElementById('reducjorn').value;
                if (situacion == 'R' && (reducjorn == null || reducjorn == '')) {
                    mensajeValidacion = '<%=meLanbide80I18n.getMensaje(idiomaUsuario, "msg.reducjorn")%>';
                    return false;
                }

                var fecinisit = document.getElementById('fecinisit').value;
                if (fecinisit == null || fecinisit == '') {
                    mensajeValidacion = '<%=meLanbide80I18n.getMensaje(idiomaUsuario, "msg.fecinisit")%>';
                    return false;
                }

                var numdiasit = document.getElementById('numdiasit').value;
                if (numdiasit == null || numdiasit == '') {
                    mensajeValidacion = '<%=meLanbide80I18n.getMensaje(idiomaUsuario, "msg.numdiasit")%>';
                    return false;
                } else {
                    if (!validarNumerico(document.getElementById('numdiasit'))) {
                        mensajeValidacion = '<%=meLanbide80I18n.getMensaje(idiomaUsuario, "msg.numdiasit.errNumerico")%>';
                        return false;
                    }
                }
                var baseregul = document.getElementById('baseregul').value;
                if (baseregul == null || baseregul == '') {
                    mensajeValidacion = '<%=meLanbide80I18n.getMensaje(idiomaUsuario, "msg.baseregul")%>';
                    return false;
                } else {
                    if (!validarNumericoDecimalPrecision(document.getElementById('baseregul').value, 9, 2)) {
                        mensajeValidacion = '<%=meLanbide80I18n.getMensaje(idiomaUsuario, "msg.baseregul.errNumerico")%>';
                        return false;
                    }
                }

                var impprest = document.getElementById('impprest').value;
                if (impprest == null || impprest == '') {
                    mensajeValidacion = '<%=meLanbide80I18n.getMensaje(idiomaUsuario, "msg.impprest")%>';
                    return false;
                } else {
                    if (!validarNumericoDecimalPrecision(document.getElementById('impprest').value, 9, 2)) {
                        mensajeValidacion = '<%=meLanbide80I18n.getMensaje(idiomaUsuario, "msg.impprest.errNumerico")%>';
                        return false;
                    }
                }

                var complsal = document.getElementById('complsal').value;
                if (complsal == null || complsal == '') {
                    mensajeValidacion = '<%=meLanbide80I18n.getMensaje(idiomaUsuario, "msg.complsal")%>';
                    return false;
                } else {
                    if (!validarNumericoDecimalPrecision(document.getElementById('complsal').value, 9, 2)) {
                        mensajeValidacion = '<%=meLanbide80I18n.getMensaje(idiomaUsuario, "msg.complsal.errNumerico")%>';
                        return false;
                    }
                }

                var impsubvsol = document.getElementById('impsubvsol').value;
                if (impsubvsol == null || impsubvsol == '') {
                    mensajeValidacion = '<%=meLanbide80I18n.getMensaje(idiomaUsuario, "msg.impsubvsol")%>';
                    return false;
                } else {
                    if (!validarNumericoDecimalPrecision(document.getElementById('impsubvsol').value, 9, 2)) {
                        mensajeValidacion = '<%=meLanbide80I18n.getMensaje(idiomaUsuario, "msg.impsubvsol.errNumerico")%>';
                        return false;
                    }
                }

                return correcto;
            }

            function comprobarFechaLanbide(inputFecha) {
                var formato = 'dd/mm/yyyy';
                if (Trim(inputFecha.value) != '') {
                    var D = ValidarFechaConFormatoLanbide(inputFecha.value, formato);
                    if (!D[0]) {
                        jsp_alerta("A", "<%=meLanbide80I18n.getMensaje(idiomaUsuario, "msg.fechaNoVal")%>");
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

            //Funcion para el calendario de fecha 
            function mostrarCalFecinisit(evento) {
                if (window.event)
                    evento = window.event;
                if (document.getElementById("calFecinisit").src.indexOf("icono.gif") != -1)
                    showCalendar('forms[0]', 'fecinisit', null, null, null, '', 'calFecinisit', '', null, null, null, null, null, null, null, null, evento);

            }

            function mostrarCalFecfinsit(evento) {
                if (window.event)
                    evento = window.event;
                if (document.getElementById("calFecfinsit").src.indexOf("icono.gif") != -1)
                    showCalendar('forms[0]', 'fecfinsit', null, null, null, '', 'calFecfinsit', '', null, null, null, null, null, null, null, null, evento);

            }

            function compruebaTamanoCampo(elemento, maxTex) {
                var texto = elemento.value;
                if (texto.length > maxTex) {
                    jsp_alerta("A", '<%=meLanbide80I18n.getMensaje(idiomaUsuario, "msg.errExcdTexto")%>');
                    elemento.focus();
                    return false;
                }
                return true;
            }

            function comprobarTipoContrato() {
                if (document.getElementById("codListaTipcontA").value == "BRI" || document.getElementById("codListaTipcontA").value == "BRT") {
                    document.getElementById('tipcontB').disabled = false;
                } else {
                    document.getElementById('tipcontB').disabled = true;
                    document.getElementById('tipcontB').value = '';
                }
            }

            function comprobarJornada() {
                if (document.getElementById("codListaJornada").value == "PARC") {
                    document.getElementById('porjorpar').disabled = false;
                } else {
                    document.getElementById('porjorpar').disabled = true;
                    document.getElementById('porjorpar').value = '';
                }
            }

            function comprobarSituacion() {
                if (document.getElementById("codListaSituacion").value == "R") {
                    document.getElementById('reducjorn').disabled = false;
                } else {
                    document.getElementById('reducjorn').disabled = true;
                    document.getElementById('reducjorn').value = '';
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
                            <%=meLanbide80I18n.getMensaje(idiomaUsuario,"label.nuevaPersona")%>
                        </span>
                    </div>
                    <div class="lineaFormulario">
                        <div style="width: 225px; float: left;" class="etiqueta">
                            <%=meLanbide80I18n.getMensaje(idiomaUsuario,"label.dni")%>
                        </div>
                        <div style="width: 450px; float: left;">
                            <div style="float: left;">
                                <input id="dni" name="dni" type="text" class="inputTexto" size="15" maxlength="15" 
                                       value="<%=datModif != null && datModif.getDni() != null ? datModif.getDni() : ""%>"/>
                            </div>
                        </div>
                    </div>
                    <br><br>
                    <div class="lineaFormulario">
                        <div style="width: 225px; float: left;" class="etiqueta">
                            <%=meLanbide80I18n.getMensaje(idiomaUsuario,"label.nombre")%>
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
                        <div style="width: 225px; float: left;" class="etiqueta">
                            <%=meLanbide80I18n.getMensaje(idiomaUsuario,"label.apel1")%>
                        </div>
                        <div style="width: 450px; float: left;">
                            <div style="float: left;">
                                <input id="apel1" name="apel1" type="text" class="inputTexto" size="30" maxlength="80" 
                                       value="<%=datModif != null && datModif.getApel1() != null ? datModif.getApel1() : ""%>"/>
                            </div>
                        </div>
                    </div>
                    <br><br>
                    <div class="lineaFormulario">
                        <div style="width: 225px; float: left;" class="etiqueta">
                            <%=meLanbide80I18n.getMensaje(idiomaUsuario,"label.apel2")%>
                        </div>
                        <div style="width: 450px; float: left;">
                            <div style="float: left;">
                                <input id="apel2" name="apel2" type="text" class="inputTexto" size="30" maxlength="80" 
                                       value="<%=datModif != null && datModif.getApel2() != null ? datModif.getApel2() : ""%>"/>
                            </div>
                        </div>
                    </div>
                    <br><br>
                    <div class="lineaFormulario">
                        <div class="etiqueta" style="width: 225px; float: left;">
                            <%=meLanbide80I18n.getMensaje(idiomaUsuario,"label.tipcontA")%>
                        </div>
                        <div>
                            <div>
                                <input type="text" name="codListaTipcontA" id="codListaTipcontA" size="5" class="inputTexto" value="" onkeyup="xAMayusculas(this);" />
                                <input type="text" name="descListaTipcontA"  id="descListaTipcontA" size="60" class="inputTexto" readonly="true" value="" />
                                <a href="" id="anchorListaTipcontA" name="anchorListaTipcontA">
                                    <span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonAplicacion"
                                          name="botonAplicacion" style="cursor:hand;"></span>
                                </a>
                            </div>
                        </div>
                    </div>
                    <div class="lineaFormulario">
                        <div style="width: 225px; float: left;" class="etiqueta">
                            <%=meLanbide80I18n.getMensaje(idiomaUsuario,"label.tipcontB")%>
                        </div>
                        <div style="width: 450px; float: left;">
                            <div style="float: left;">
                                <input id="tipcontB" name="tipcontB" type="text" class="inputTexto" size="6" maxlength="6" 
                                       value="<%=datModif != null && datModif.getTipcontB() != null ? datModif.getTipcontB().toString().replaceAll("\\.", ","): ""%>" onchange="reemplazarPuntos(this);" disabled="true"/>
                            </div>
                        </div>
                    </div>
                    <br><br>
                    <div class="lineaFormulario">
                        <div class="etiqueta" style="width: 225px; float: left;">
                            <%=meLanbide80I18n.getMensaje(idiomaUsuario,"label.jornada")%>
                        </div>
                        <div>
                            <div>
                                <input type="text" name="codListaJornada" id="codListaJornada" size="5" class="inputTexto" value="" onkeyup="xAMayusculas(this);" />
                                <input type="text" name="descListaJornada"  id="descListaJornada" size="60" class="inputTexto" readonly="true" value="" />
                                <a href="" id="anchorListaJornada" name="anchorListaJornada">
                                    <span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonAplicacion"
                                          name="botonAplicacion" style="cursor:hand;"></span>
                                </a>
                            </div>
                        </div>
                    </div>
                    <div class="lineaFormulario">
                        <div style="width: 225px; float: left;" class="etiqueta">
                            <%=meLanbide80I18n.getMensaje(idiomaUsuario,"label.porjorpar")%>
                        </div>
                        <div style="width: 450px; float: left;">
                            <div style="float: left;">
                                <input id="porjorpar" name="porjorpar" type="text" class="inputTexto" size="6" maxlength="6" 
                                       value="<%=datModif != null && datModif.getPorjorpar() != null ? datModif.getPorjorpar().toString().replaceAll("\\.", ","): ""%>" onchange="reemplazarPuntos(this);" disabled="true"/>
                            </div>
                        </div>
                    </div>
                    <br><br>
                    <div class="lineaFormulario">
                        <div class="etiqueta" style="width: 225px; float: left;">
                            <%=meLanbide80I18n.getMensaje(idiomaUsuario,"label.situacion")%>
                        </div>
                        <div>
                            <div>
                                <input type="text" name="codListaSituacion" id="codListaSituacion" size="5" class="inputTexto" value="" onkeyup="xAMayusculas(this);" />
                                <input type="text" name="descListaSituacion"  id="descListaSituacion" size="60" class="inputTexto" readonly="true" value="" />
                                <a href="" id="anchorListaSituacion" name="anchorListaSituacion">
                                    <span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonAplicacion"
                                          name="botonAplicacion" style="cursor:hand;"></span>
                                </a>
                            </div>
                        </div>
                    </div>
                    <div class="lineaFormulario">
                        <div style="width: 225px; float: left;" class="etiqueta">
                            <%=meLanbide80I18n.getMensaje(idiomaUsuario,"label.reducjorn")%>
                        </div>
                        <div style="width: 450px; float: left;">
                            <div style="float: left;">
                                <input id="reducjorn" name="reducjorn" type="text" class="inputTexto" size="6" maxlength="6" 
                                       value="<%=datModif != null && datModif.getReducjorn() != null ? datModif.getReducjorn().toString().replaceAll("\\.", ","): ""%>" onchange="reemplazarPuntos(this);" disabled="true"/>
                            </div>
                        </div>
                    </div>
                    <br><br>
                    <div class="lineaFormulario">
                        <div style="width: 225px; float: left;" class="etiqueta">
                            <%=meLanbide80I18n.getMensaje(idiomaUsuario,"label.fecinisit")%>
                        </div>
                        <div style="width: 450px; float: left;">
                            <div style="float: left;">
                                <input type="text" class="inputTxtFecha" 
                                       id="fecinisit" name="fecinisit"
                                       maxlength="10"  size="10"
                                       value="<%=fecinisit%>"
                                       onkeyup = "return SoloCaracteresFechaLanbide(this);"
                                       onblur = "javascript:return comprobarFechaLanbide(this);"
                                       onfocus="javascript:this.select();"/>
                                <A href="javascript:calClick(event);return false;" onClick="mostrarCalFecinisit(event);return false;" style="text-decoration:none;">
                                    <IMG style="border: 0px solid none" height="17" id="calFecinisit" name="calFecinisit" border="0" 
                                         src="<c:url value='/images/calendario/icono.gif'/>" >
                                    <!--width="25"-->
                                </A>
                            </div>
                        </div>
                    </div>
                    <br><br>
                    <div class="lineaFormulario">
                        <div style="width: 225px; float: left;" class="etiqueta" >
                            <%=meLanbide80I18n.getMensaje(idiomaUsuario,"label.fecfinsit")%>
                        </div>
                        <div style="width: 450px; float: left;">
                            <div style="float: left;">
                                <input type="text" class="inputTxtFecha" 
                                       id="fecfinsit" name="fecfinsit"
                                       maxlength="10"  size="10"
                                       value="<%=fecfinsit%>"
                                       onkeyup = "return SoloCaracteresFechaLanbide(this);"
                                       onblur = "javascript:return comprobarFechaLanbide(this);"
                                       onfocus="javascript:this.select();"/>
                                <A href="javascript:calClick(event);return false;" onClick="mostrarCalFecfinsit(event);return false;" style="text-decoration:none;">
                                    <IMG style="border: 0px solid none" height="17" id="calFecfinsit" name="calFecfinsit" border="0" 
                                         src="<c:url value='/images/calendario/icono.gif'/>" >
                                    <!--width="25"-->
                                </A>
                            </div>
                        </div>
                    </div>
                    <br><br>
                    <div class="lineaFormulario">
                        <div style="width: 225px; float: left;" class="etiqueta">
                            <%=meLanbide80I18n.getMensaje(idiomaUsuario,"label.numdiasit")%>
                        </div>
                        <div style="width: 450px; float: left;">
                            <div style="float: left;">
                                <input id="numdiasit" name="numdiasit" type="text" class="inputTexto" size="5" maxlength="5" 
                                       value="<%=datModif != null && datModif.getNumdiasit() != null ? datModif.getNumdiasit() : ""%>"/>
                            </div>
                        </div>
                    </div>
                    <br><br>           
                    <div class="lineaFormulario">
                        <div style="width: 225px; float: left;" class="etiqueta">
                            <%=meLanbide80I18n.getMensaje(idiomaUsuario,"label.baseregul")%>
                        </div>
                        <div style="width: 450px; float: left;">
                            <div style="float: left;">
                                <input id="baseregul" name="baseregul" type="text" class="inputTexto" size="9" maxlength="9" 
                                       value="<%=datModif != null && datModif.getBaseregul() != null ? datModif.getBaseregul().toString().replaceAll("\\.", ","): ""%>" onchange="reemplazarPuntos(this);"/>
                            </div>
                        </div>
                    </div>
                    <br><br>
                    <div class="lineaFormulario">
                        <div style="width: 225px; float: left;" class="etiqueta">
                            <%=meLanbide80I18n.getMensaje(idiomaUsuario,"label.impprest")%>
                        </div>
                        <div style="width: 450px; float: left;">
                            <div style="float: left;">
                                <input id="impprest" name="impprest" type="text" class="inputTexto" size="9" maxlength="9" 
                                       value="<%=datModif != null && datModif.getImpprest() != null ? datModif.getImpprest().toString().replaceAll("\\.", ","): ""%>" onchange="reemplazarPuntos(this);"/>
                            </div>
                        </div>
                    </div>
                    <br><br>
                    <div class="lineaFormulario">
                        <div style="width: 225px; float: left;" class="etiqueta">
                            <%=meLanbide80I18n.getMensaje(idiomaUsuario,"label.complsal")%>
                        </div>
                        <div style="width: 450px; float: left;">
                            <div style="float: left;">
                                <input id="complsal" name="complsal" type="text" class="inputTexto" size="9" maxlength="9" 
                                       value="<%=datModif != null && datModif.getComplsal() != null ? datModif.getComplsal().toString().replaceAll("\\.", ","): ""%>" onchange="reemplazarPuntos(this);"/>
                            </div>
                        </div>
                    </div>
                    <br><br>
                    <div class="lineaFormulario">
                        <div style="width: 225px; float: left;" class="etiqueta">
                            <%=meLanbide80I18n.getMensaje(idiomaUsuario,"label.impsubvsol")%>
                        </div>
                        <div style="width: 450px; float: left;">
                            <div style="float: left;">
                                <input id="impsubvsol" name="impsubvsol" type="text" class="inputTexto" size="9" maxlength="9" 
                                       value="<%=datModif != null && datModif.getImpsubvsol() != null ? datModif.getImpsubvsol().toString().replaceAll("\\.", ","): ""%>" onchange="reemplazarPuntos(this);"/>
                            </div>
                        </div>
                    </div>
                    <br><br><br>
                    <div class="lineaFormulario">
                        <div class="botonera" style="text-align: center;">
                            <input type="button" id="btnAceptar" name="btnAceptar" class="botonGeneral" value="<%=meLanbide80I18n.getMensaje(idiomaUsuario, "btn.aceptar")%>" onclick="guardarDatos();"/>
                            <input type="button" id="btnCancelar" name="btnCancelar" class="botonGeneral" value="<%=meLanbide80I18n.getMensaje(idiomaUsuario, "btn.cancelar")%>" onclick="cancelar();"/>
                        </div>
                    </div>
                </div>
            </form>
            <script type="text/javascript">
                listaCodigosTipcontA[0] = "";
                listaDescripcionesTipcontA[0] = "";
                contador = 0;

                <logic:iterate id="tipcontA" name="listaTipcontA" scope="request">
                listaCodigosTipcontA[contador] = ['<bean:write name="tipcontA" property="des_val_cod" />'];
                listaDescripcionesTipcontA[contador] = ['<bean:write name="tipcontA" property="des_nom" />'];
                contador++;
                </logic:iterate>

                var comboListaTipcontA = new Combo("ListaTipcontA");
                comboListaTipcontA.addItems(listaCodigosTipcontA, listaDescripcionesTipcontA);
                comboListaTipcontA.change = cargarDatosTipcontA;

                /*desplegable jornada*/
                listaCodigosJornada[0] = "";
                listaDescripcionesJornada[0] = "";
                contador1 = 0;

                <logic:iterate id="jornada" name="listaJornada" scope="request">
                listaCodigosJornada[contador1] = ['<bean:write name="jornada" property="des_val_cod" />'];
                listaDescripcionesJornada[contador1] = ['<bean:write name="jornada" property="des_nom" />'];
                contador1++;
                </logic:iterate>

                var comboListaJornada = new Combo("ListaJornada");
                comboListaJornada.addItems(listaCodigosJornada, listaDescripcionesJornada);
                comboListaJornada.change = cargarDatosJornada;

                /*desplegable situación*/
                listaCodigosSituacion[0] = "";
                listaDescripcionesSituacion[0] = "";
                contador2 = 0;

                <logic:iterate id="situacion" name="listaSituacion" scope="request">
                listaCodigosSituacion[contador2] = ['<bean:write name="situacion" property="des_val_cod" />'];
                listaDescripcionesSituacion[contador2] = ['<bean:write name="situacion" property="des_nom" />'];
                contador2++;
                </logic:iterate>

                var comboListaSituacion = new Combo("ListaSituacion");
                comboListaSituacion.addItems(listaCodigosSituacion, listaDescripcionesSituacion);
                comboListaSituacion.change = cargarDatosSituacion;

                var nuevo = "<%=nuevo%>";
                if (nuevo == 0) {
                    rellenardatModificar();
                    comprobarTipoContrato();
                }
            </script>
            <div id="popupcalendar" class="text"></div>        
        </div>
    </body>
</html> 

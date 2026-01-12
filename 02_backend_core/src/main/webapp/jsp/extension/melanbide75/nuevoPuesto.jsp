<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>

<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide75.i18n.MeLanbide75I18n" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide75.vo.ControlAccesoVO" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide75.vo.DesplegableAdmonLocalVO" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide75.util.ConstantesMeLanbide75" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide75.util.ConfigurationParameter" %>
<%@page import="java.util.ArrayList" %>
<%@page import="java.util.List" %>
<%@page import="java.text.SimpleDateFormat" %>
<html>
    <head>
        <meta http-equiv="X-UA-Compatible" content="IE=Edge" />
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <%
                ControlAccesoVO datModif = new ControlAccesoVO();
                ControlAccesoVO objectVO = new ControlAccesoVO();
        
                String codOrganizacion = "";
                String nuevo = "";
        
                String expediente = "";
        
                String fechaConDesde = "";
                String fechaConHasta = "";
        
                MeLanbide75I18n meLanbide75I18n = MeLanbide75I18n.getInstance();

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
                        datModif = (ControlAccesoVO)request.getAttribute("datModif");
                        SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
                
                        if (datModif.getConDesde()!=null){
                            fechaConDesde = formatoFecha.format(datModif.getConDesde());
                        }
                        if (datModif.getConHasta()!=null){
                            fechaConHasta = formatoFecha.format(datModif.getConHasta());
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
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/extension/melanbide75/melanbide75.css"/>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/general.js"></script>
        <script type="text/javascript" src="<c:url value='/scripts/listaComboBox.js'/>"></script>
        <script type="text/javascript" src="<c:url value='/scripts/calendario.js'/>"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/validaciones.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/popup.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/JavaScriptUtil.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/Parsers.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/melanbide75/InputMask.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/melanbide76/lanbide.js"></script>
        <script type="text/javascript">
            var APP_CONTEXT_PATH = '<%=request.getContextPath()%>';
            var mensajeValidacion = '';

            var comboListaTipoCon1;
            var listaCodigosTipoCon1 = new Array();
            var listaDescripcionesTipoCon1 = new Array();


            function buscaCodigoTipoCon1(codTipoCon1) {
                comboListaTipoCon1.buscaCodigo(codTipoCon1);
            }

            function cargarDatosTipoCon1() {
                var codTipoCon1Seleccionado = document.getElementById("codListaTipoCon1").value;
                buscaCodigoTipoCon1(codTipoCon1Seleccionado);
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
                    buscaCodigoTipoCon1('<%=datModif.getTipoCon() != null ? datModif.getTipoCon() : ""%>');
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
                    var url = APP_CONTEXT_PATH + "/PeticionModuloIntegracion.do";
                    var parametros = "";
                    var nuevo = "<%=nuevo%>";
                    var nombre = "";
                    var ape = "";
                    if (document.getElementById('nombre').value == null || document.getElementById('nombre').value == '') {
                        nombre = "";
                    } else {
                        nombre = document.getElementById('nombre').value.replace(/\'/g, "''");
                    }

                    if (nuevo != null && nuevo == "1") {
                        parametros = "tarea=preparar&modulo=MELANBIDE75&operacion=crearNuevoAcceso&tipo=0"
                                + "&expediente=<%=expediente%>"
                                + "&puesto=" + document.getElementById('puesto').value
                                + '&nombre=' + nombre
                                + "&nif=" + document.getElementById('dni_nif').value
                                + "&porDisc=" + document.getElementById('grado').value
                                + "&tipoCon=" + document.getElementById('codListaTipoCon1').value
                                + "&conDesde=" + document.getElementById('conDesde').value
                                + "&conHasta=" + document.getElementById('conHasta').value
                                + "&totalDias=" + document.getElementById('totalDias').value
                                ;

                    } else {
                        parametros = "tarea=preparar&modulo=MELANBIDE75&operacion=modificarAcceso&tipo=0"
                                + "&id=<%=datModif != null && datModif.getId() != null ? datModif.getId().toString() : ""%>"
                                + "&puesto=" + document.getElementById('puesto').value
                                + '&nombre=' + nombre
                                + "&nif=" + document.getElementById('dni_nif').value
                                + "&porDisc=" + document.getElementById('grado').value
                                + "&tipoCon=" + document.getElementById('codListaTipoCon1').value
                                + "&conDesde=" + document.getElementById('conDesde').value
                                + "&conHasta=" + document.getElementById('conHasta').value
                                + "&totalDias=" + document.getElementById('totalDias').value
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
                                    } else if (hijosFila[cont].nodeName == "DENOMPUESTO") {
                                        if (hijosFila[cont].childNodes[0].nodeValue != "null") {
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
                                    } else if (hijosFila[cont].nodeName == "NIFCIF") {
                                        if (hijosFila[cont].childNodes.length > 0) {
                                            fila[3] = hijosFila[cont].childNodes[0].nodeValue;
                                        } else {
                                            fila[3] = '-';
                                        }
                                    } else if (hijosFila[cont].nodeName == "GRADO") {
                                        if (hijosFila[cont].childNodes[0].nodeValue != "null") {
                                            fila[4] = hijosFila[cont].childNodes[0].nodeValue;
                                            var tex = fila[4].toString();
                                            tex = tex.replace(".", ",");
                                            fila[4] = tex;
                                        } else {
                                            fila[4] = '-';
                                        }
                                    } else if (hijosFila[cont].nodeName == "TIPOCONTRATO") {
                                        if (hijosFila[cont].childNodes[0].nodeValue != "null") {
                                            fila[5] = hijosFila[cont].childNodes[0].nodeValue;
                                        } else {
                                            fila[5] = '-';
                                        }
                                    } else if (hijosFila[cont].nodeName == "CONTRATODESDE") {
                                        if (hijosFila[cont].childNodes.length > 0) {
                                            fila[6] = hijosFila[cont].childNodes[0].nodeValue;
                                        } else {
                                            fila[6] = '-';
                                        }
                                    } else if (hijosFila[cont].nodeName == "CONTRATOHASTA") {
                                        if (hijosFila[cont].childNodes[0].nodeValue != "null") {
                                            fila[7] = hijosFila[cont].childNodes[0].nodeValue;
                                        } else {
                                            fila[7] = '-';
                                        }
                                    } else if (hijosFila[cont].nodeName == "TOTALDIAS") {
                                        if (hijosFila[cont].childNodes[0].nodeValue != "null") {
                                            fila[8] = hijosFila[cont].childNodes[0].nodeValue;
                                        } else {
                                            fila[8] = '-';
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
                            jsp_alerta("A", '<%=meLanbide75I18n.getMensaje(idiomaUsuario,"error.errorBD")%>');
                        } else if (codigoOperacion == "2") {
                            jsp_alerta("A", '<%=meLanbide75I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                        } else if (codigoOperacion == "3") {
                            jsp_alerta("A", '<%=meLanbide75I18n.getMensaje(idiomaUsuario,"error.pasoParametros")%>');
                        } else {
                            jsp_alerta("A", '<%=meLanbide75I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                        }
                    } catch (Err) {
                    }//try-catch
                } else {
                    jsp_alerta("A", mensajeValidacion);
                }
            }

            function cancelar() {
                var resultado = jsp_alerta('', '<%=meLanbide75I18n.getMensaje(idiomaUsuario, "msg.preguntaCancelar")%>');
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

                if (document.getElementById('grado').value == null || document.getElementById('grado').value == '') {
                } else {
                    if (!validarNumericoDecimalPrecision(document.getElementById('grado').value, 5, 2)) {
                        mensajeValidacion = '<%=meLanbide75I18n.getMensaje(idiomaUsuario, "msg.grado.errNumerico")%>';
                        return false;
                    } else {
                        if (!validarNumericoPorcentajeCien(document.getElementById('grado').value)) {
                            mensajeValidacion = '<%=meLanbide75I18n.getMensaje(idiomaUsuario, "msg.grado.errRango")%>';
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
                        var result = regex.test(valor);
                        return result;
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

            //CNAE y Nombre y apellidos no son obligatorios por LOPD(protección de datos), CNAE se quita
            function validarDatos() {
                mensajeValidacion = "";
                var correcto = true;

                var puesto = document.getElementById('puesto').value;
                if (puesto == null || puesto == '') {
                    mensajeValidacion = '<%=meLanbide75I18n.getMensaje(idiomaUsuario, "msg.puestoObligatorio")%>';
                    return false;
                }

                var dni_nif = document.getElementById('dni_nif').value;
                if (dni_nif == null || dni_nif == '') {
                    mensajeValidacion = '<%=meLanbide75I18n.getMensaje(idiomaUsuario, "msg.dniObligatorio")%>';
                    return false;
                }

                var grado = document.getElementById('grado').value;
                if (grado == null || grado == '') {
                    mensajeValidacion = '<%=meLanbide75I18n.getMensaje(idiomaUsuario, "msg.grado")%>';
                    return false;
                }
                var tipCont = document.getElementById('codListaTipoCon1').value;
                if (tipCont == null || tipCont == '') {
                    mensajeValidacion = '<%=meLanbide75I18n.getMensaje(idiomaUsuario, "msg.tipoCon")%>';
                    return false;
                }

                var fecha = document.getElementById('conDesde').value;
                if (fecha == null || fecha == '') {
                    mensajeValidacion = '<%=meLanbide75I18n.getMensaje(idiomaUsuario, "msg.fechaObligatorio")%>';
                    return false;
                }

                fecha = document.getElementById('conHasta').value;
                if (fecha == null || fecha == '') {
                    mensajeValidacion = '<%=meLanbide75I18n.getMensaje(idiomaUsuario, "msg.fechaObligatorio")%>';
                    return false;
                }

                var totalDias = document.getElementById('totalDias').value;
                if (totalDias == null || totalDias == '') {
                    mensajeValidacion = '<%=meLanbide75I18n.getMensaje(idiomaUsuario, "msg.totalDias")%>';
                    return false;
                } else {
                    if (!validarNumerico(document.getElementById('totalDias'))) {
                        mensajeValidacion = '<%=meLanbide75I18n.getMensaje(idiomaUsuario, "msg.totalDias.errNumerico")%>';
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
                        jsp_alerta("A", "<%=meLanbide75I18n.getMensaje(idiomaUsuario, "msg.fechaNoVal")%>");
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
            function mostrarCalConDesde(evento) {
                if (window.event)
                    evento = window.event;
                if (document.getElementById("calConDesde").src.indexOf("icono.gif") != -1)
                    showCalendar('forms[0]', 'conDesde', null, null, null, '', 'calConDesde', '', null, null, null, null, null, null, null, null, evento);

            }

            function mostrarCalConHasta(evento) {
                if (window.event)
                    evento = window.event;
                if (document.getElementById("calConHasta").src.indexOf("icono.gif") != -1)
                    showCalendar('forms[0]', 'conHasta', null, null, null, '', 'calConHasta', '', null, null, null, null, null, null, null, null, evento);

            }

            function compruebaTamanoCampo(elemento, maxTex) {
                var texto = elemento.value;
                if (texto.length > maxTex) {
                    jsp_alerta("A", '<%=meLanbide75I18n.getMensaje(idiomaUsuario, "msg.errExcdTexto")%>');
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
                    <div  class="sub3titulo" style="clear: both; text-align: center; font-size: 14px;">
                        <span>
                            <%=meLanbide75I18n.getMensaje(idiomaUsuario,"label.nuevoPuesto")%>
                        </span>
                    </div> 
                    <br>

                    <div class="lineaFormulario">
                        <div style="width: 190px; float: left;" class="etiqueta">
                            <%=meLanbide75I18n.getMensaje(idiomaUsuario,"label.puesto")%>
                        </div>
                        <div style="width: 450px; float: left;">
                            <div style="float: left;">
                                <input id="puesto" name="puesto" type="text" class="inputTexto" size="80" maxlength="150" 
                                       value="<%=datModif != null && datModif.getPuesto() != null ? datModif.getPuesto() : ""%>"/>
                            </div>
                        </div>
                    </div>
                    <br><br>

                    <div class="lineaFormulario">
                        <div style="width: 190px; float: left;" class="etiqueta">
                            <%=meLanbide75I18n.getMensaje(idiomaUsuario,"label.nombre")%>
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
                            <%=meLanbide75I18n.getMensaje(idiomaUsuario,"label.dni_nif")%>
                        </div>
                        <div style="width: 450px; float: left;">
                            <div style="float: left;">
                                <input id="dni_nif" name="dni_nif" type="text" class="inputTexto" size="15" maxlength="15" onkeyup="xAMayusculas(this);"
                                       value="<%=datModif != null && datModif.getNif() != null ? datModif.getNif() : ""%>"/>
                            </div>
                        </div>
                    </div>
                    <br><br>

                    <div class="lineaFormulario">
                        <div style="width: 190px; float: left;" class="etiqueta">
                            <%=meLanbide75I18n.getMensaje(idiomaUsuario,"label.gradoDiscapacidad")%>
                        </div>
                        <div style="width: 450px; float: left;">
                            <div style="float: left;">
                                <input id="grado" name="grado" type="text" class="inputTexto" size="5" maxlength="5" 
                                       value="<%=datModif != null && datModif.getPorDisc() != null ? datModif.getPorDisc().toString().replaceAll("\\.", ","): ""%>" onchange="reemplazarPuntos(this);"/>
                            </div>
                        </div>
                    </div>
                    <br><br>

                    <div class="lineaFormulario">
                        <div class="etiqueta" style="width: 190px; float: left;">
                            <%=meLanbide75I18n.getMensaje(idiomaUsuario,"label.tipoContrato")%>
                        </div>
                        <div style="width: 450px; float: left;">
                            <div>
                                <input type="text" name="codListaTipoCon1" id="codListaTipoCon1" size="10" class="inputTexto" value="" onkeyup="xAMayusculas(this);" />
                                <input type="text" name="descListaTipoCon1"  id="descListaTipoCon1" size="60" class="inputTexto" readonly="true" value="" />
                                <a href="" id="anchorListaTipoCon1" name="anchorListaTipoCon1">
                                    <span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonAplicacion"
                                          name="botonAplicacion" style="cursor:pointer;"></span>
                                </a>
                            </div>
                        </div>
                    </div>
                    <br><br>

                    <div class="lineaFormulario">
                        <div style="width: 190px; float: left;" class="etiqueta">
                            <%=meLanbide75I18n.getMensaje(idiomaUsuario,"label.conDesde")%>
                        </div>
                        <div style="width: 450px; float: left;">
                            <div style="float: left;">
                                <input type="text" class="inputTxtFecha" 
                                       id="conDesde" name="conDesde"
                                       maxlength="10"  size="10"
                                       value="<%=fechaConDesde%>"
                                       onkeyup = "return SoloCaracteresFechaLanbide(this);"
                                       onblur = "javascript:return comprobarFechaLanbide(this);"
                                       onfocus="javascript:this.select();"/>
                                <A href="javascript:calClick(event);return false;" onClick="mostrarCalConDesde(event);return false;" style="text-decoration:none;">
                                    <IMG style="border: 0px solid" height="17" id="calConDesde" name="calConDesde" border="0" 
                                         src="<c:url value='/images/calendario/icono.gif'/>" > <!--width="25"-->
                                </A>
                            </div>
                        </div>
                    </div>
                    <br><br>

                    <div class="lineaFormulario">
                        <div style="width: 190px; float: left;text-align: left;" class="etiqueta" >
                            <%=meLanbide75I18n.getMensaje(idiomaUsuario,"label.conHasta")%>
                        </div>
                        <div style="width: 450px; float: left;">
                            <input type="text" class="inputTxtFecha" 
                                   id="conHasta" name="conHasta"
                                   maxlength="10"  size="10"
                                   value="<%=fechaConHasta%>"
                                   onkeyup = "return SoloCaracteresFechaLanbide(this);"
                                   onblur = "javascript:return comprobarFechaLanbide(this);"
                                   onfocus="javascript:this.select();"/>
                            <A href="javascript:calClick(event);return false;" onClick="mostrarCalConHasta(event);
                                    return false;" style="text-decoration:none;">
                                <IMG style="border: 0px solid" height="17" id="calConHasta" name="calConHasta" border="0" 
                                     src="<c:url value='/images/calendario/icono.gif'/>" > <!--width="25"-->
                            </A>
                        </div>
                    </div>
                    <br><br>

                    <div class="lineaFormulario">
                        <div style="width: 190px; float: left;" class="etiqueta">
                            <%=meLanbide75I18n.getMensaje(idiomaUsuario,"label.totalDias")%>
                        </div>
                        <div style="width: 450px; float: left;">
                            <div style="float: left;">
                                <input id="totalDias" name="totalDias" type="text" class="inputTexto" size="15" maxlength="5" 
                                       value="<%=datModif != null && datModif.getTotalDias() != null ? datModif.getTotalDias() : ""%>"/>
                            </div>
                        </div>
                    </div>
                    <br><br>

                    <div class="lineaFormulario">
                        <div class="botonera" style="text-align: center;">
                            <input type="button" id="btnAceptar" name="btnAceptar" class="botonGeneral" value="<%=meLanbide75I18n.getMensaje(idiomaUsuario, "btn.aceptar")%>" onclick="guardarDatos();"/>
                            <input type="button" id="btnCancelar" name="btnCancelar" class="botonGeneral" value="<%=meLanbide75I18n.getMensaje(idiomaUsuario, "btn.cancelar")%>" onclick="cancelar();"/>
                        </div>
                    </div>
                </div>
                <div id="reloj" style="font-size:20px;"></div>
            </form>
            <script type="text/javascript">

                /*desplegable tipo contrato*/
                listaCodigosTipoCon1[0] = "";
                listaDescripcionesTipoCon1[0] = "";
                contador = 0;

                <logic:iterate id="tipoCon1" name="listaTipoCon1" scope="request">
                listaCodigosTipoCon1[contador] = ['<bean:write name="tipoCon1" property="des_val_cod" />'];
                listaDescripcionesTipoCon1[contador] = ['<bean:write name="tipoCon1" property="des_nom" />'];
                contador++;
                </logic:iterate>

                var comboListaTipoCon1 = new Combo("ListaTipoCon1");
                comboListaTipoCon1.addItems(listaCodigosTipoCon1, listaDescripcionesTipoCon1);
                comboListaTipoCon1.change = cargarDatosTipoCon1;

                var nuevo = "<%=nuevo%>";
                if (nuevo == 0) {
                    rellenardatModificar();
                }

            </script>
            <div id="popupcalendar" class="text"></div>        
        </div>
    </body>
</html> 

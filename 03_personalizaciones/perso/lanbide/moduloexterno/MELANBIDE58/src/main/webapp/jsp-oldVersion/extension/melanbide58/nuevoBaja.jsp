<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>

<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide58.i18n.MeLanbide58I18n" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide58.vo.BajaVO" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide58.vo.CausaAltaBajaVO" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide58.util.ConstantesMeLanbide58" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide58.util.ConfigurationParameter" %>
<%@page import="java.util.ArrayList" %>
<%@page import="java.util.List" %>
<%@page import="java.text.SimpleDateFormat" %>
<html>
    <head>
        <meta http-equiv="X-UA-Compatible" content="IE=Edge" />
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <%
            BajaVO datModif = new BajaVO();
            BajaVO objectVO = new BajaVO();
            List<CausaAltaBajaVO> listaEspSol = new ArrayList<CausaAltaBajaVO>();

            String codOrganizacion = "";
            String codMotivoVisita = "";
            String nuevo = "";
            String fecha = "";
            String expediente = "";
            String codCausa = "";
         String numLineaS = (String)request.getAttribute("numLineaS");
            MeLanbide58I18n meLanbide58I18n = MeLanbide58I18n.getInstance();
            //ConstantesMeLanbide58 _constantesMeLanbide58 = ConstantesMeLanbide58();

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
                    datModif = (BajaVO)request.getAttribute("datModif");
                    SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
                    fecha = formatoFecha.format(datModif.getFechaBaja());
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
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/meLanbide58/ceescUtils.js"></script>
        <!-- Eventos onKeyPress compatibilidad firefox  -->
        <!--<script type="text/javascript" src="http://code.jquery.com/jquery-1.4.4.js"></script>-->

        <script type="text/javascript">

            var mensajeValidacion = '';
            var comboListaCausas;
            var listaCodigosCausas = new Array();
            var listaDescripcionesCausas = new Array();

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
                mensajeValidacion = "";
                if (validarNumericoVacio(document.getElementById('numLinea').value)) {
                    if (validarNif(document.getElementById('nif'))) {

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
                            parametros = "tarea=preparar&modulo=MELANBIDE58&operacion=crearNuevoBaja&tipo=0"
                                    + "&expediente=<%=expediente%>"
                                    + "&numLinea=" + document.getElementById('numLinea').value
                                    + '&nombre=' + nombre
                                    + '&apellidos=' + ape
                                    + "&nif=" + document.getElementById('nif').value
                                    + "&fechaBaja=" + document.getElementById('fechabaja').value
                                    + "&numSS=" + document.getElementById('numSS').value
                                    + "&causa=" + document.getElementById('codListaCausas').value
                                    ;

                        } else {
                            parametros = "tarea=preparar&modulo=MELANBIDE58&operacion=modificarBaja&tipo=0"
                                    + "&id=<%=datModif != null && datModif.getId() != null ? datModif.getId().toString() : ""%>"
                                    + "&expediente=<%=datModif != null && datModif.getNumExp() != null ? datModif.getNumExp().toString() : ""%>"
                                    + "&numLinea=" + document.getElementById('numLinea').value
                                    + '&nombre=' + nombre
                                    + '&apellidos=' + ape
                                    + "&nif=" + document.getElementById('nif').value
                                    + "&fechaBaja=" + document.getElementById('fechabaja').value
                                    + "&numSS=" + document.getElementById('numSS').value
                                    + "&causa=" + document.getElementById('codListaCausas').value
                                    ;
                        }
                        try {
                            ajax.open("POST", url, false);
                            ajax.setRequestHeader("Content-Type", "application/x-www-form-urlencoded; charset=ISO-8859-1");
                            ajax.setRequestHeader("Accept", "text/xml, application/xml, text/plain");
                            //var formData = new FormData(document.getElementById('formContrato'));
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
                                        } else if (hijosFila[cont].nodeName == "FECHABAJA") {
                                            if (hijosFila[cont].childNodes.length > 0) {
                                                fila[4] = hijosFila[cont].childNodes[0].nodeValue;
                                            } else {
                                                fila[4] = '-';
                                            }
                                        } else if (hijosFila[cont].nodeName == "NIF") {
                                            if (hijosFila[cont].childNodes[0].nodeValue != "null") {
                                                fila[5] = hijosFila[cont].childNodes[0].nodeValue;
                                            } else {
                                                fila[5] = '-';
                                            }
                                        } else if (hijosFila[cont].nodeName == "NUMSS") {
                                            if (hijosFila[cont].childNodes[0].nodeValue != "null") {
                                                fila[6] = hijosFila[cont].childNodes[0].nodeValue;
                                            } else {
                                                fila[6] = '-';
                                            }
                                        } else if (hijosFila[cont].nodeName == "DES_NOM") {
                                            if (hijosFila[cont].childNodes[0].nodeValue != "null") {
                                                fila[7] = hijosFila[cont].childNodes[0].nodeValue;
                                            } else {
                                                fila[7] = '-';
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
                                jsp_alerta("A", '<%=meLanbide58I18n.getMensaje(idiomaUsuario,"error.errorBD")%>');
                            } else if (codigoOperacion == "2") {
                                jsp_alerta("A", '<%=meLanbide58I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                            } else if (codigoOperacion == "3") {
                                jsp_alerta("A", '<%=meLanbide58I18n.getMensaje(idiomaUsuario,"error.pasoParametros")%>');
                            } else {
                                jsp_alerta("A", '<%=meLanbide58I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                            }
                        } catch (Err) {
                        }//try-catch
                    } else {
                        mensajeValidacion = '<%=meLanbide58I18n.getMensaje(idiomaUsuario, "msg.documentoIncorrecto")%>';
                        jsp_alerta("A", mensajeValidacion);
                    }
                } else {
                    mensajeValidacion = '<%=meLanbide58I18n.getMensaje(idiomaUsuario, "msg.numLinea.errNumerico")%>';
                    jsp_alerta("A", mensajeValidacion);
                }
            }

            function cancelar() {
                var resultado = jsp_alerta('', '<%=meLanbide58I18n.getMensaje(idiomaUsuario, "msg.preguntaCancelar")%>');
                if (resultado == 1) {
                    cerrarVentana();
                }
            }

            function validarNumericoVacio(numero) {
                try {
                    if (numero == null || numero == '') {
                        return true;
                    } else {
                        if (Trim(numero) != '') {
                            return /^([0-9])*$/.test(numero);
                        }
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

                var numLinea = document.getElementById('numLinea').value;
                if (numLinea == null || numLinea == '') {
                    mensajeValidacion = '<%=meLanbide58I18n.getMensaje(idiomaUsuario, "msg.numLineaObligatorio")%>';
                    return false;
                } else {
                    if (!validarNumerico(document.getElementById('numLinea'))) {
                        mensajeValidacion = '<%=meLanbide58I18n.getMensaje(idiomaUsuario, "msg.numLinea.errNumerico")%>';
                        return false;
                    }
                }

                var campo = document.getElementById('nombre').value;
                if (campo == null || campo == '') {
                    mensajeValidacion = '<%=meLanbide58I18n.getMensaje(idiomaUsuario, "msg.nombre")%>';
                    return false;
                }

                campo = document.getElementById('apellidos').value;
                if (campo == null || campo == '') {
                    mensajeValidacion = '<%=meLanbide58I18n.getMensaje(idiomaUsuario, "msg.apellido1")%>';
                    return false;
                }

                campo = document.getElementById('fechabaja').value;
                if (campo == null || campo == '') {
                    mensajeValidacion = '<%=meLanbide58I18n.getMensaje(idiomaUsuario, "msg.fechabaja")%>';
                    return false;
                }
                campo = document.getElementById('nif').value;
                if (campo == null || campo == '') {
                    mensajeValidacion = '<%=meLanbide58I18n.getMensaje(idiomaUsuario, "msg.nif")%>';
                    return false;
                }
                campo = document.getElementById('numss').value;
                if (campo == null || campo == '') {
                    mensajeValidacion = '<%=meLanbide58I18n.getMensaje(idiomaUsuario, "msg.numSS")%>';
                    return false;
                }

                return correcto;
            }

            function compruebaTamanoCampo(elemento, maxTex) {
                var texto = elemento.value;
                if (texto.length > maxTex) {
                    jsp_alerta("A", '<%=meLanbide58I18n.getMensaje(idiomaUsuario, "msg.errExcdTexto")%>');
                    elemento.focus();
                    return false;
                }
                return true;
            }

            function mostrarCalFechaBaja(evento) {
                if (window.event)
                    evento = window.event;
                if (document.getElementById("calFechaBaja").src.indexOf("icono.gif") != -1)
                    showCalendar('forms[0]', 'fechabaja', null, null, null, '', 'calFechaBaja', '', null, null, null, null, null, null, null, null, evento);

            }

            function cargarDatosCausa() {
                var codCausaSeleccionado = document.getElementById("codListaCausas").value;
                buscaCodigoCausa(codCausaSeleccionado);
            }

            function buscaCodigoCausa(codCausa) {
                comboListaCausas.buscaCodigo(codCausa);
            }

            function rellenardatCausa() {
                if ('<%=datModif%>' != null) {
                    buscaCodigoCausa('<%=datModif.getCausa() != null ? datModif.getCausa() : ""%>');
                } else
                    alert('No hemos podido cargar los datos para modificar');
            }

            function desbloquearCampos() {
                document.getElementById('nif').disabled = false;
                document.getElementById('nombre').disabled = false;
                document.getElementById('apellidos').disabled = false;
            }
        </script>
    </head>
    <body>
        <div class="contenidoPantalla">
            <form><!--    action="/PeticionModuloIntegracion.do" method="POST" -->
                <div style="width: 100%; padding: 10px; text-align: left;">
                    <div  class="sub3titulo" style="clear: both; text-align: center; font-size: 14px;">
                        <span>
                            <%=meLanbide58I18n.getMensaje(idiomaUsuario,"label.nuevoBaja")%>
                        </span>
                    </div>                        
                    <br><br>   
                    <div class="lineaFormulario">
                        <div style="width: 190px; float: left;" class="etiqueta">
                            <%=meLanbide58I18n.getMensaje(idiomaUsuario,"label.numLinea")%>
                        </div>
                        <div style="width: 450px; float: left;">
                            <div style="float: left;">
                                <input id="numLinea" name="numLinea" type="text" class="inputTexto" size="10" maxlength="5" 
                                       value="<%=datModif != null && datModif.getNumLinea() != null ? datModif.getNumLinea() : ""%>" disabled/>
                            </div>
                        </div>
                    </div>
                    <br><br>   
                    <div class="lineaFormulario">
                        <div style="width: 190px; float: left;" class="etiqueta">
                            <%=meLanbide58I18n.getMensaje(idiomaUsuario,"label.nif")%>
                        </div>
                        <div style="width: 450px; float: left;">
                            <div style="float: left;">
                                <input id="nif" name="nif" type="text" class="inputTextoObligatorio" size="10" maxlength="10" onkeyup="xAMayusculas(this);"
                                       onblur="//comprobarCampoBusq();"
                                       value="<%=datModif != null && datModif.getNif() != null ? datModif.getNif() : ""%>" disabled/>
                            </div>
                        </div>
                    </div>
                    <br><br>
                    <div class="lineaFormulario">
                        <div style="width: 190px; float: left;" class="etiqueta">
                            <%=meLanbide58I18n.getMensaje(idiomaUsuario,"label.nombre")%>
                        </div>
                        <div style="width: 450px; float: left;">
                            <div style="float: left;">
                                <input id="nombre" name="nombre" type="text" class="inputTextoObligatorio" size="30" maxlength="50" 
                                       value="<%=datModif != null && datModif.getNombre() != null ? datModif.getNombre() : ""%>" disabled="true"/>
                            </div>
                        </div>
                    </div>
                    <br><br>
                    <div class="lineaFormulario">
                        <div style="width: 190px; float: left;" class="etiqueta">
                            <%=meLanbide58I18n.getMensaje(idiomaUsuario,"label.apellidos")%>
                        </div>
                        <div style="width: 450px; float: left;">
                            <div style="float: left;">
                                <input id="apellidos" name="apellidos" type="text" class="inputTextoObligatorio" size="30" maxlength="50" 
                                       value="<%=datModif != null && datModif.getApellidos() != null ? datModif.getApellidos() : ""%>" disabled="true"/>
                            </div>
                        </div>
                    </div>
                    <br><br>


                    <div class="lineaFormulario">
                        <div style="width: 190px; float: left;" class="etiqueta">
                            <%=meLanbide58I18n.getMensaje(idiomaUsuario,"label.numSS")%>
                        </div>
                        <div style="width: 450px; float: left;">
                            <div style="float: left;">
                                <input id="numSS" name="numSS" type="text" class="inputTexto" size="15" maxlength="15" 
                                       value="<%=datModif != null && datModif.getNumSS() != null ? datModif.getNumSS() : ""%>"/>
                            </div>
                        </div>
                    </div>

                    <br><br><br/>

                    <div class="lineaFormulario">
                        <div style="width: 190px; float: left;" class="etiqueta">
                            <%=meLanbide58I18n.getMensaje(idiomaUsuario,"label.fechaBaja")%>
                        </div>
                        <div style="width: 450px; float: left;">
                            <div style="float: left;">
                                <input type="text" class="inputTxtFechaObligatorio" 
                                       id="fechabaja" name="fechabaja"
                                       maxlength="10"  size="10"
                                       value="<%=fecha%>"
                                       onkeyup = "return SoloCaracteresFechaLanbide(this);"
                                       onblur = "javascript:return comprobarFechaLanbide(this);"
                                       onfocus="javascript:this.select();"/>
                                <A href="javascript:calClick(event);return false;" onClick="mostrarCalFechaBaja(event);
                                        return false;" style="text-decoration:none;">   <!-- onblur="javascript:return comprobarFecha(this);" onkeyup="return SoloCaracteresFecha(this);" -->
                                    <IMG style="border: 0px solid none" height="17" id="calFechaBaja" name="calFechaBaja" border="0" 
                                         src="<c:url value='/images/calendario/icono.gif'/>" > <!--width="25"-->
                                </A>
                            </div>
                        </div>
                    </div>
                    <br><br/>
                    <div>
                        <div class="etiqueta" >
                            <%=meLanbide58I18n.getMensaje(idiomaUsuario,"label.causa.baja")%>
                        </div>
                        <div>
                            <input type="text" name="codListaCausas" id="codListaCausas" size="10" class="inputTexto" value="" onkeyup="xAMayusculas(this);" />
                            <input type="text" name="descListaCausas"  id="descListaCausas" size="90" class="inputTexto" readonly="true" value="" />
                            <a href="" id="anchorListaCausas" name="anchorListaCausas">
                                <span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonAplicacion"
                                      name="botonAplicacion" style="cursor:hand;"></span>
                            </a>
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

                listaCodigosCausas[0] = "";
                listaDescripcionesCausas[0] = "";

                /* Lista con los certificados recuperados de la BBDD */
                var contador = 0;
                <logic:iterate id="causas" name="listaCausas" scope="request">
                listaCodigosCausas[contador] = ['<bean:write name="causas" property="desValCod" />'];
                listaDescripcionesCausas[contador] = ['<bean:write name="causas" property="des_nombre" />'];
                contador++;
                </logic:iterate>

                var comboListaCausas = new Combo("ListaCausas");
                comboListaCausas.addItems(listaCodigosCausas, listaDescripcionesCausas);
                comboListaCausas.change = cargarDatosCausa;

                var nuevo = "<%=nuevo%>";
                if (nuevo == 0) {
                    rellenardatCausa();
                } else {
                    desbloquearCampos();
                    var linea = "<%=numLineaS%>";
                    document.getElementById('numLinea').value = linea;
                }

            </script>   
            <div id="popupcalendar" class="text"></div>        
        </div>
    </body>
</html>
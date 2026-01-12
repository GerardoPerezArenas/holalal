<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>

<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide58.i18n.MeLanbide58I18n" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide58.vo.ControlAccesoVO" %>
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
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <%
            ControlAccesoVO datModif = new ControlAccesoVO();
            ControlAccesoVO objectVO = new ControlAccesoVO();

            String codOrganizacion = "";
            String codMotivoVisita = "";
            String nuevo = "";
            String fecha = "";
            String fechaIV="";
            String fechaFV="";
            String expediente = "";

        
            MeLanbide58I18n meLanbide58I18n = MeLanbide58I18n.getInstance();
            //ConstantesMeLanbide58 _constantesMeLanbide58 = ConstantesMeLanbide58();
            String numLineaS = (String)request.getAttribute("numLineaS");
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
                    fecha = formatoFecha.format(datModif.getFecNaci());
                    if (datModif.getFecInicio()!=null){
                        fechaIV = formatoFecha.format(datModif.getFecInicio());
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

            var nuevo = "<%=nuevo%>";
            var titulo;
            var mensajeValidacion = '';

            var comboListaSexo;
            var listaCodigosSexo = new Array();
            var listaDescripcionesSexo = new Array();

            var comboListaTipoDis;
            var listaCodigosTipoDis = new Array();
            var listaDescripcionesTipoDis = new Array();

            var comboListaTipoCon1;
            var listaCodigosTipoCon1 = new Array();
            var listaDescripcionesTipoCon1 = new Array();

            var comboListaTipoCon2;
            var listaCodigosTipoCon2 = new Array();
            var listaDescripcionesTipoCon2 = new Array();

            var comboListaSevero;
            var listaCodigosSevero = new Array();
            var listaDescripcionesSevero = new Array();

            var comboListaValidado;
            var listaCodigosValidado = new Array();
            var listaDescripcionesValidado = new Array();

            var comboListaSubtipo;
            var listaCodigosSubtipo = new Array();
            var listaDescripcionesSubtipo = new Array();

            function buscaCodigoSexo(codSexo) {
                comboListaSexo.buscaCodigo(codSexo);
            }

            function cargarDatosSexo() {
                var codSexoSeleccionado = document.getElementById("codListaSexo").value;
                buscaCodigoSexo(codSexoSeleccionado);
            }

            function buscaCodigoTipoDis(codTipoDis) {
                comboListaTipoDis.buscaCodigo(codTipoDis);
            }

            function cargarDatosTipoDis() {
                var codTipoDisSeleccionado = document.getElementById("codListaTipoDis").value;
                comprobarTipoDisc(codTipoDisSeleccionado);
                buscaCodigoTipoDis(codTipoDisSeleccionado);
            }

            function buscaCodigoSubtipo(codSubtipo) {
                comboListaSubtipo.buscaCodigo(codSubtipo);
            }

            function cargarDatosSubtipo() {
                var codSubtipoSeleccionado = document.getElementById("codListaSubtipo").value;
                buscaCodigoSubtipo(codSubtipoSeleccionado);
            }

            function buscaCodigoTipoCon1(codTipoCon1) {
                comboListaTipoCon1.buscaCodigo(codTipoCon1);
            }

            function cargarDatosTipoCon1() {
                var codTipoCon1Seleccionado = document.getElementById("codListaTipoCon1").value;
                buscaCodigoTipoCon1(codTipoCon1Seleccionado);
            }

            function buscaCodigoTipoCon2(codTipoCon2) {
                comboListaTipoCon2.buscaCodigo(codTipoCon2);
            }

            function cargarDatosTipoCon2() {
                var codTipoCon2Seleccionado = document.getElementById("codListaTipoCon2").value;
                buscaCodigoTipoCon2(codTipoCon2Seleccionado);
            }

            function buscaCodigoSevero(codListaSevero) {
                comboListaSevero.buscaCodigo(codListaSevero);
            }

            function cargarDatosSevero() {
                var codListaSeveroSeleccionado = document.getElementById("codListaSevero").value;
                buscaCodigoSevero(codListaSeveroSeleccionado);
            }

            function buscaCodigoValidado(codListaValidado) {
                comboListaValidado.buscaCodigo(codListaValidado);
            }

            function cargarDatosValidado() {
                var codListaValidadoSeleccionado = document.getElementById("codListaValidado").value;
                buscaCodigoValidado(codListaValidadoSeleccionado);
            }

            function comprobarTipoDisc(codigo) {
                if (codigo == "PS" || codigo == "PSF" || codigo == "PSS" || codigo == "PSFS") {
                    document.getElementById("subtipoDiscapacidad").style.visibility = 'inherit';
                } else {
                    document.getElementById("subtipoDiscapacidad").style.visibility = 'hidden';
                }
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
                    buscaCodigoSexo('<%=datModif.getSexo() != null ? datModif.getSexo() : ""%>');
                    buscaCodigoTipoDis('<%=datModif.getTipoDis() != null ? datModif.getTipoDis() : ""%>');
                    buscaCodigoTipoCon1('<%=datModif.getTipoCon1() != null ? datModif.getTipoCon1() : ""%>');
                    buscaCodigoTipoCon2('<%=datModif.getTipoCon2() != null ? datModif.getTipoCon2() : ""%>');
                    buscaCodigoSevero('<%=datModif.getDiscSevera()!= null ? datModif.getDiscSevera() : ""%>');
                    buscaCodigoValidado('<%=datModif.getDiscValidada() != null ? datModif.getDiscValidada() : ""%>');
                    buscaCodigoSubtipo('<%=datModif.getSubtipo() != null ? datModif.getSubtipo() : ""%>');
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

                if (validarDatosNumericosVacios()) {



                    var ajax = getXMLHttpRequest();
                    var nodos = null;
                    var CONTEXT_PATH = '<%=request.getContextPath()%>';
                    var url = CONTEXT_PATH + "/PeticionModuloIntegracion.do";
                    var parametros = "";
                    var nuevo = "<%=nuevo%>";
                    var nombre = "";
                    var ape = "";
                    var severaEmp = "-";
                    var subtipo = "";
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
                    if (document.getElementById('codListaSubtipo').value == null || document.getElementById('codListaSubtipo').value == '') {
                        subtipo = document.getElementById('codListaSubtipo').value;
                    }
                    var llamar = false;

                    if (nuevo != null && nuevo == "1") {
                        parametros = "tarea=preparar&modulo=MELANBIDE58&operacion=crearNuevoAcceso&tipo=0"
                                + "&expediente=<%=expediente%>"
                                + "&numLinea=" + document.getElementById('numLinea').value
                                + "&dni_nif=" + document.getElementById('dni_nif').value
                                + '&nombre=' + nombre
                                + '&apellidos=' + ape
                                + "&codSexo=" + document.getElementById('codListaSexo').value
                                + "&fechaNac=" + document.getElementById('meLanbide58Fecha').value
                                + "&numSS=" + document.getElementById('numSS').value
                                + "&fecCert=" + document.getElementById('fecInicioVigencia').value
                                + "&tipoDis=" + document.getElementById('codListaTipoDis').value
                                + "&subtipo=" + subtipo
                                + "&grado=" + document.getElementById('grado').value
                                + "&severo=" + document.getElementById('codListaSevero').value
                                + "&validado=" + document.getElementById('codListaValidado').value
                                + "&codTipoCon1=" + document.getElementById('codListaTipoCon1').value
                                + "&codTipoCon2=" + document.getElementById('codListaTipoCon2').value
                                + "&porcJornada=" + document.getElementById('porcJornada').value
                                ;
                        llamar = true;
                    } else {
                        parametros = "tarea=preparar&modulo=MELANBIDE58&operacion=modificarAcceso&tipo=0"
                                + "&id=<%=datModif != null && datModif.getId() != null ? datModif.getId().toString() : ""%>"
                                + "&numLinea=" + document.getElementById('numLinea').value
                                + "&dni_nif=" + document.getElementById('dni_nif').value
                                + '&nombre=' + nombre
                                + '&apellidos=' + ape
                                + "&codSexo=" + document.getElementById('codListaSexo').value
                                + "&fechaNac=" + document.getElementById('meLanbide58Fecha').value
                                + "&numSS=" + document.getElementById('numSS').value
                                + "&fecCert=" + document.getElementById('fecInicioVigencia').value
                                + "&tipoDis=" + document.getElementById('codListaTipoDis').value
                                + "&subtipo=" + subtipo
                                + "&grado=" + document.getElementById('grado').value
                                + "&severo=" + document.getElementById('codListaSevero').value
                                + "&validado=" + document.getElementById('codListaValidado').value
                                + "&codTipoCon1=" + document.getElementById('codListaTipoCon1').value
                                + "&codTipoCon2=" + document.getElementById('codListaTipoCon2').value
                                + "&porcJornada=" + document.getElementById('porcJornada').value
                                + "&porcOriginal=<%=datModif != null && datModif.getPorcJornada() != null ? datModif.getPorcJornada().toString(): ""%>"
                                ;
                        var resultado = jsp_alerta('', '<%=meLanbide58I18n.getMensaje(idiomaUsuario, "msg.preguntaModificarPlantilla")%>');
                        if (resultado == 1) {
                            llamar = true;
                        }
                    }
                    if (llamar) {
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
                            var codigoIncompleto = null;
                            var lista = new Array();
                            var fila = new Array();
                            var nodoFila;
                            var hijosFila;
                            for (j = 0; hijos != null && j < hijos.length; j++) {
                                if (hijos[j].nodeName == "CODIGO_OPERACION") {
                                    codigoOperacion = hijos[j].childNodes[0].nodeValue;
                                    lista[j] = codigoOperacion;
                                }//if(hijos[j].nodeName=="CODIGO_OPERACION") 
                                else if (hijos[j].nodeName == "CODIGO_INCOMPLETO") {
                                    codigoIncompleto = hijos[j].childNodes[0].nodeValue;
                                } else if (hijos[j].nodeName == "FILA") {
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
                                        } else if (hijosFila[cont].nodeName == "SEXO") {
                                            if (hijosFila[cont].childNodes[0].nodeValue != "null") {
                                                fila[4] = hijosFila[cont].childNodes[0].nodeValue;
                                            } else {
                                                fila[4] = '-';
                                            }
                                        } else if (hijosFila[cont].nodeName == "FECNACI") {
                                            if (hijosFila[cont].childNodes.length > 0) {
                                                fila[5] = hijosFila[cont].childNodes[0].nodeValue;
                                            } else {
                                                fila[5] = '-';
                                            }
                                        } else if (hijosFila[cont].nodeName == "NIFCIF") {
                                            if (hijosFila[cont].childNodes[0].nodeValue != "null") {
                                                fila[6] = hijosFila[cont].childNodes[0].nodeValue;
                                            } else {
                                                fila[6] = '-';
                                            }
                                        } else if (hijosFila[cont].nodeName == "NUMSS") {
                                            if (hijosFila[cont].childNodes[0].nodeValue != "null") {
                                                fila[7] = hijosFila[cont].childNodes[0].nodeValue;
                                            } else {
                                                fila[7] = '-';
                                            }
                                        } else if (hijosFila[cont].nodeName == "FECINICIO") {
                                            if (hijosFila[cont].childNodes.length > 0) {
                                                fila[8] = hijosFila[cont].childNodes[0].nodeValue;
                                            } else {
                                                fila[8] = '-';
                                            }
                                        } else if (hijosFila[cont].nodeName == "TIPODIS") {
                                            if (hijosFila[cont].childNodes[0].nodeValue != "null") {
                                                fila[9] = hijosFila[cont].childNodes[0].nodeValue;
                                            } else {
                                                fila[9] = '-';
                                            }
                                        } else if (hijosFila[cont].nodeName == "SUBTIPO") {
                                            if (hijosFila[cont].childNodes[0].nodeValue != "null") {
                                                fila[16] = hijosFila[cont].childNodes[0].nodeValue;
                                            } else {
                                                fila[16] = '-';
                                            }
                                        } else if (hijosFila[cont].nodeName == "GRADO") {
                                            if (hijosFila[cont].childNodes[0].nodeValue != "null") {
                                                fila[10] = hijosFila[cont].childNodes[0].nodeValue;
                                                var tex = fila[10].toString();
                                                tex = tex.replace(".", ",");
                                                fila[10] = tex;
                                            } else {
                                                fila[10] = '-';
                                            }
                                        } else if (hijosFila[cont].nodeName == "DES_DISC_SEVERA_EMP") {
                                            if (hijosFila[cont].childNodes.nodeValue != "null") {
                                                fila[14] = hijosFila[cont].childNodes[0].nodeValue;
                                            } else {
                                                fila[14] = '-';
                                            }
                                        } else if (hijosFila[cont].nodeName == "DES_DISC_SEVERA_LAN") {
                                            if (hijosFila[cont].childNodes.nodeValue != "null") {
                                                fila[15] = hijosFila[cont].childNodes[0].nodeValue;
                                            } else {
                                                fila[15] = '-';
                                            }
                                        } else if (hijosFila[cont].nodeName == "TIPOCON1") {
                                            if (hijosFila[cont].childNodes[0].nodeValue != "null") {
                                                fila[11] = hijosFila[cont].childNodes[0].nodeValue;
                                            } else {
                                                fila[11] = '-';
                                            }
                                        } else if (hijosFila[cont].nodeName == "TIPOCON2") {
                                            if (hijosFila[cont].childNodes[0].nodeValue != "null") {
                                                fila[12] = hijosFila[cont].childNodes[0].nodeValue;
                                            } else {
                                                fila[12] = '-';
                                            }
                                        } else if (hijosFila[cont].nodeName == "PORCJORNADA") {
                                            if (hijosFila[cont].childNodes[0].nodeValue != "null") {
                                                fila[13] = hijosFila[cont].childNodes[0].nodeValue;
                                                var text = fila[13].toString();
                                                text = text.replace(".", ",");
                                                fila[13] = text;
                                            } else {
                                                fila[13] = '-';
                                            }
                                        } else if (hijosFila[cont].nodeName == "DATOS_PENDIENTES") {
                                            if (hijosFila[cont].childNodes[0].nodeValue != "null") {
                                                fila[17] = hijosFila[cont].childNodes[0].nodeValue;
                                            } else {
                                                fila[17] = '-';
                                            }
                                        } else if (hijosFila[cont].nodeName == "NUEVA_ALTA") {
                                            if (hijosFila[cont].childNodes[0].nodeValue != "null") {
                                                fila[18] = hijosFila[cont].childNodes[0].nodeValue;
                                            } else {
                                                fila[18] = '-';
                                            }
                                        }
                                    }// for elementos de la fila
                                    lista[j] = fila;
                                    fila = new Array();
                                }
                            }//for(j=0;hijos!=null && j<hijos.length;j++)
                            if (codigoOperacion == "0") {
                                //jsp_alerta("A",'Correcto');
                                if (codigoIncompleto != "0") {
                                    jsp_alerta("A", '<%=meLanbide58I18n.getMensaje(idiomaUsuario,"msg.personaIncompletaA")%>' + codigoIncompleto + ' ' + '<%=meLanbide58I18n.getMensaje(idiomaUsuario,"msg.personaIncompletaB")%>');
                                }
                                self.parent.opener.retornoXanelaAuxiliar(lista);
                                cerrarVentana();
                            } else if (codigoOperacion == "1") {
                                jsp_alerta("A", '<%=meLanbide58I18n.getMensaje(idiomaUsuario,"error.errorBD")%>');
                            } else if (codigoOperacion == "2") {
                                jsp_alerta("A", '<%=meLanbide58I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                            } else if (codigoOperacion == "3") {
                                jsp_alerta("A", '<%=meLanbide58I18n.getMensaje(idiomaUsuario,"error.pasoParametros")%>');
                            } else if (codigoOperacion == "4") {
                                jsp_alerta("A", '<%=meLanbide58I18n.getMensaje(idiomaUsuario,"error.anioVacio")%>');
                            } else if (codigoOperacion == "5") {
                                jsp_alerta("A", '<%=meLanbide58I18n.getMensaje(idiomaUsuario,"error.recalculoSMI")%>');
                            } else if (codigoOperacion == "6") {
                                jsp_alerta("A", '<%=meLanbide58I18n.getMensaje(idiomaUsuario,"error.notFoundSMI")%>');
                            } else if (codigoOperacion == "7") {
                                jsp_alerta("A", '<%=meLanbide58I18n.getMensaje(idiomaUsuario,"error.actualizarRecalculoSMI")%>');
                            } else if (codigoOperacion == "8") {
                                jsp_alerta("A", '<%=meLanbide58I18n.getMensaje(idiomaUsuario,"error.insertarPersonaDiscapacitada")%>');
                            } else if (codigoOperacion == "9") {
                                jsp_alerta("A", '<%=meLanbide58I18n.getMensaje(idiomaUsuario,"error.actualizarPersonaDisc")%>');
                            } else if (codigoOperacion == "10") {
                                jsp_alerta("A", '<%=meLanbide58I18n.getMensaje(idiomaUsuario,"error.modificarAccesoSeleccionado")%>');
                            } else if (codigoOperacion == "11") {
                                jsp_alerta("A", '<%=meLanbide58I18n.getMensaje(idiomaUsuario,"error.actualizaJornadaB")%>');
                            } else if (codigoOperacion == "12") {
                                jsp_alerta("A", '<%=meLanbide58I18n.getMensaje(idiomaUsuario,"error.actualizarPersonaDisc")%>');
                            } else {
                                jsp_alerta("A", '<%=meLanbide58I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                            }
                        } catch (Err) {
                        }//try-catch
                    }// if (llamar)



                } else {
                    jsp_alerta("A", mensajeValidacion);
                }
            }

            function cancelar() {
                var resultado = jsp_alerta('', '<%=meLanbide58I18n.getMensaje(idiomaUsuario, "msg.preguntaCancelar")%>');
                if (resultado == 1) {
                    cerrarVentana();
                }
            }

            function validarDatosNumericosVacios() {
                mensajeValidacion = "";
                var correcto = true;

                if (document.getElementById('numLinea').value == null || document.getElementById('numLinea').value == '') {
                } else {
                    if (Trim(document.getElementById('numLinea').value) != '') {
                        if (/^([0-9])*$/.test(document.getElementById('numLinea').value)) {
                        } else {
                            mensajeValidacion = '<%=meLanbide58I18n.getMensaje(idiomaUsuario, "msg.numLinea.errNumerico")%>';
                            return false;
                        }
                    }
                }

                if (document.getElementById('grado').value == null || document.getElementById('grado').value == '') {
                } else {
                    if (!validarNumericoDecimalPrecision(document.getElementById('grado').value, 5, 2)) {
                        mensajeValidacion = '<%=meLanbide58I18n.getMensaje(idiomaUsuario, "msg.grado.errNumerico")%>';
                        return false;
                    } else {
                        if (!validarNumericoPorcentajeCien(document.getElementById('grado').value)) {
                            mensajeValidacion = '<%=meLanbide58I18n.getMensaje(idiomaUsuario, "msg.grado.errRango")%>';
                            return false;
                        }
                    }
                }

                if (document.getElementById('porcJornada').value == null || document.getElementById('porcJornada').value == '') {
                } else {
                    if (!validarNumericoDecimalPrecision(document.getElementById('porcJornada').value, 5, 2)) {
                        mensajeValidacion = '<%=meLanbide58I18n.getMensaje(idiomaUsuario, "msg.grado.errNumerico")%>';
                        return false;
                    } else {
                        if (!validarNumericoPorcentajeCien(document.getElementById('porcJornada').value)) {
                            mensajeValidacion = '<%=meLanbide58I18n.getMensaje(idiomaUsuario, "msg.grado.errRango")%>';
                            return false;
                        }
                    }
                }
                var doc = document.getElementById('dni_nif');
                if (doc.value == null || doc.value == '') {
                    mensajeValidacion = '<%=meLanbide58I18n.getMensaje(idiomaUsuario, "msg.dniObligatorio")%>';
                    return false;
                } else {
                    if (!validarNif(doc)) {
                        mensajeValidacion = '<%=meLanbide58I18n.getMensaje(idiomaUsuario, "msg.documentoIncorrecto")%>';
                        return false;
                    }
                }
                return correcto;
            }

            function validarDatos() {
                mensajeValidacion = "";
                var correcto = true;

                var fecha = document.getElementById('meLanbide58Fecha').value;
                if (fecha == null || fecha == '') {
                    mensajeValidacion = '<%=meLanbide58I18n.getMensaje(idiomaUsuario, "msg.fechaObligatorio")%>';
                    return false;
                }

                fecha = document.getElementById('fecInicioVigencia').value;
                if (fecha == null || fecha == '') {
                    mensajeValidacion = '<%=meLanbide58I18n.getMensaje(idiomaUsuario, "msg.fechaObligatorio")%>';
                    return false;
                }

                var dni_nif = document.getElementById('dni_nif').value;
                if (dni_nif == null || dni_nif == '') {
                    mensajeValidacion = '<%=meLanbide58I18n.getMensaje(idiomaUsuario, "msg.dniObligatorio")%>';
                    return false;
                }

                var nombre = document.getElementById('nombre').value;
                if (nombre == null || nombre == '') {
                    mensajeValidacion = '<%=meLanbide58I18n.getMensaje(idiomaUsuario, "msg.nombreObligatorio")%>';
                    return false;
                }

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

                nombre = document.getElementById('apellidos').value;
                if (nombre == null || nombre == '') {
                    mensajeValidacion = '<%=meLanbide58I18n.getMensaje(idiomaUsuario, "msg.apellido1")%>';
                    return false;
                }

                nombre = document.getElementById('codListaSexo').value;
                if (nombre == null || nombre == '') {
                    mensajeValidacion = '<%=meLanbide58I18n.getMensaje(idiomaUsuario, "msg.sexo")%>';
                    return false;
                }
                nombre = document.getElementById('numSS').value;
                if (nombre == null || nombre == '') {
                    mensajeValidacion = '<%=meLanbide58I18n.getMensaje(idiomaUsuario, "msg.numSS")%>';
                    return false;
                }
                nombre = document.getElementById('codListaTipoDis').value;
                if (nombre == null || nombre == '') {
                    mensajeValidacion = '<%=meLanbide58I18n.getMensaje(idiomaUsuario, "msg.tipoDis")%>';
                    return false;
                } else {
                    if (nombre == "PS" || nombre == "PSF" || nombre == "PSS" || nombre == "PSFS" || nombre == "FS") {
                        if (document.getElementById('codListaSubtipo').value == null || document.getElementById('codListaSubtipo').value == '') {
                            mensajeValidacion = '<%=meLanbide58I18n.getMensaje(idiomaUsuario, "msg.subtipo")%>';
                            //      return false;
                        }
                    }
                }
                nombre = document.getElementById('grado').value;
                if (nombre == null || nombre == '') {
                    mensajeValidacion = '<%=meLanbide58I18n.getMensaje(idiomaUsuario, "msg.grado")%>';
                    return false;
                }
                nombre = document.getElementById('codListaTipoCon1').value;
                if (nombre == null || nombre == '') {
                    mensajeValidacion = '<%=meLanbide58I18n.getMensaje(idiomaUsuario, "msg.tipoCon")%>';
                    return false;
                }
                nombre = document.getElementById('codListaTipoCon2').value;
                if (nombre == null || nombre == '') {
                    mensajeValidacion = '<%=meLanbide58I18n.getMensaje(idiomaUsuario, "msg.tipoCon")%>';
                    return false;
                }

                return correcto;
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

            //Funcion para el calendario de fecha 
            function mostrarCalFecha(evento) {
                if (window.event)
                    evento = window.event;
                if (document.getElementById("calMeLanbide58Fecha").src.indexOf("icono.gif") != -1)
                    showCalendar('forms[0]', 'meLanbide58Fecha', null, null, null, '', 'calMeLanbide58Fecha', '', null, null, null, null, null, null, null, null, evento);

            }//mostrarCalFechaAdqui

            function mostrarCalFechaIVigen(evento) {
                if (window.event)
                    evento = window.event;
                if (document.getElementById("calFecInicioVigencia").src.indexOf("icono.gif") != -1)
                    showCalendar('forms[0]', 'fecInicioVigencia', null, null, null, '', 'calFecInicioVigencia', '', null, null, null, null, null, null, null, null, evento);

            }
            function mostrarCalFechaFVigen(evento) {
                if (window.event)
                    evento = window.event;
                if (document.getElementById("calFecFinVigencia").src.indexOf("icono.gif") != -1)
                    showCalendar('forms[0]', 'fecFinVigencia', null, null, null, '', 'calFecFinVigencia', '', null, null, null, null, null, null, null, null, evento);

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

            function desbloquearCampos() {
                document.getElementById('dni_nif').disabled = false;
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
                        <span id="subtitulo"></span>
                    </div>                        
                    <br><br>   
                    <div class="lineaFormulario">
                        <div style="width: 190px; float: left;" class="etiqueta">
                            <%=meLanbide58I18n.getMensaje(idiomaUsuario,"label.numLinea")%>
                        </div>
                        <div style="width: 450px; float: left;">
                            <div style="float: left;">
                                <input id="numLinea" name="numLinea" type="text" class="inputTexto" size="10" maxlength="5" disabled
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
                                <input id="dni_nif" name="dni_nif" type="text" class="inputTextoObligatorio" size="12" maxlength="12"
                                       onkeyup="xAMayusculas(this);" disabled
                                       value="<%=datModif != null && datModif.getNif_Dni() != null ? datModif.getNif_Dni() : ""%>"/>
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
                                <input id="nombre" name="nombre" type="text" class="inputTextoObligatorio" size="30" maxlength="50" disabled
                                       value="<%=datModif != null && datModif.getNombre() != null ? datModif.getNombre() : ""%>"/>
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
                                <input id="apellidos" name="apellidos" type="text" class="inputTextoObligatorio" size="30" maxlength="50" disabled
                                       value="<%=datModif != null && datModif.getApellidos() != null ? datModif.getApellidos() : ""%>"/>
                            </div>
                        </div>
                    </div>
                    <br><br>
                    <div class="lineaFormulario">
                        <div class="etiqueta" style="width: 190px; float: left;">
                            <%=meLanbide58I18n.getMensaje(idiomaUsuario,"label.sexo")%>
                        </div>
                        <div>
                            <div>
                                <input type="text" name="codListaSexo" id="codListaSexo" size="10" class="inputTextoObligatorio" value="" onkeyup="xAMayusculas(this);" />
                                <input type="text" name="descListaSexo"  id="descListaSexo" size="60" class="inputComboObligatorio" readonly="true" value="" />
                                <a href="" id="anchorListaSexo" name="anchorListaSexo">
                                    <span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonAplicacion"
                                          name="botonAplicacion" style="cursor:hand;"></span>
                                </a>
                            </div>
                        </div>
                    </div>
                    <br>
                    <div class="lineaFormulario">
                        <div style="width: 190px; float: left;" class="etiqueta">
                            <%=meLanbide58I18n.getMensaje(idiomaUsuario,"label.FechaNacimiento")%>
                        </div>
                        <div style="width: 450px; float: left;">
                            <div style="float: left;">
                                <input type="text" class="inputTxtFecha" 
                                       id="meLanbide58Fecha" name="meLanbide58Fecha"
                                       maxlength="10"  size="10"
                                       value="<%=fecha%>"
                                       onkeyup = "return SoloCaracteresFechaLanbide(this);"
                                       onblur = "javascript:return comprobarFechaLanbide(this);"
                                       onfocus="javascript:this.select();"/>
                                <A href="javascript:calClick(event);return false;" onClick="mostrarCalFecha(event);
                                        return false;" style="text-decoration:none;">   <!-- onblur="javascript:return comprobarFecha(this);" onkeyup="return SoloCaracteresFecha(this);" -->
                                    <IMG style="border: 0px solid none" height="17" id="calMeLanbide58Fecha" name="calMeLanbide58Fecha" border="0" 
                                         src="<c:url value='/images/calendario/icono.gif'/>" > <!--width="25"-->
                                </A>
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
                    </div><br>                        
                    <br><br>
                    <div class="lineaFormulario">
                        <div style="width: 190px; float: left;text-align: left;" class="etiqueta" >
                            <%=meLanbide58I18n.getMensaje(idiomaUsuario,"label.fechaCertif")%>
                        </div>
                        <div style="width: 450px; float: left;">
                            <input type="text" class="inputTxtFechaObligatorio" 
                                   id="fecInicioVigencia" name="fecInicioVigencia"
                                   maxlength="10"  size="10"
                                   value="<%=fechaIV%>"
                                   onkeyup = "return SoloCaracteresFechaLanbide(this);"
                                   onblur = "javascript:return comprobarFechaLanbide(this);"
                                   onfocus="javascript:this.select();"/>
                            <A href="javascript:calClick(event);return false;" onClick="mostrarCalFechaIVigen(event);return false;" style="text-decoration:none;">   <!-- onblur="javascript:return comprobarFecha(this);" onkeyup="return SoloCaracteresFecha(this);" -->
                                <IMG style="border: 0px solid none" height="17" id="calFecInicioVigencia" name="calFecInicioVigencia" border="0" 
                                     src="<c:url value='/images/calendario/icono.gif'/>" > <!--width="25"-->
                            </A>
                        </div>
                    </div><br><br><br>
                    <div class="lineaFormulario">
                        <div class="etiqueta" style="width: 190px; float: left;">
                            <%=meLanbide58I18n.getMensaje(idiomaUsuario,"label.tipoDiscapacidad")%>
                        </div>
                        <div>
                            <div>
                                <input type="text" name="codListaTipoDis" id="codListaTipoDis" size="10" class="inputTextoObligatorio" value="" onkeyup="xAMayusculas(this);" />
                                <input type="text" name="descListaTipoDis"  id="descListaTipoDis" size="60" class="inputComboObligatorio" readonly="true" value="" />
                                <a href="" id="anchorListaTipoDis" name="anchorListaTipoDis">
                                    <span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonAplicacion"
                                          name="botonAplicacion" style="cursor:hand;"></span>
                                </a>
                            </div>
                        </div>
                    </div>
                    <br>
                    <div class="lineaFormulario" id="subtipoDiscapacidad" style="visibility: hidden">
                        <div class="etiqueta" style="width: 190px; float: left;">
                            <%=meLanbide58I18n.getMensaje(idiomaUsuario,"label.subtipo")%>
                        </div>
                        <div>
                            <div>
                                <input type="text" name="codListaSubtipo" id="codListaSubtipo" size="10" class="inputTexto" value="" onkeyup="xAMayusculas(this);" />
                                <input type="text" name="descListaSubtipo"  id="descListaSubtipo" size="60" class="inputTexto" readonly="true" value="" />
                                <a href="" id="anchorListaSubtipo" name="anchorListaSubtipo">
                                    <span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonAplicacion"
                                          name="botonAplicacion" style="cursor:hand;"></span>
                                </a>
                            </div>
                        </div>
                    </div>
                    <br>
                    <div class="lineaFormulario">
                        <div style="width: 190px; float: left;" class="etiqueta">
                            <%=meLanbide58I18n.getMensaje(idiomaUsuario,"label.gradoDiscapacidad")%>
                        </div>
                        <div style="width: 450px; float: left;">
                            <div style="float: left;">
                                <input id="grado" name="grado" type="text" class="inputTextoObligatorio" size="5" maxlength="5" 
                                       value="<%=datModif != null && datModif.getGrado() != null ? datModif.getGrado().toString().replaceAll("\\.", ","): ""%>" onchange="reemplazarPuntos(this);"/>
                            </div>
                        </div>
                    </div>
                    <br><br>
                    <div class="lineaFormulario">
                        <div style="width: 190px; float: left;" class="etiqueta">
                            <%=meLanbide58I18n.getMensaje(idiomaUsuario,"label.severa")%>
                        </div>                     
                        <div>
                            <div>
                                <input type="text" name="codListaSevero" id="codListaSevero" size="10" class="inputTexto" value="" onkeyup="xAMayusculas(this);" />
                                <input type="text" name="descListaSevero"  id="descListaSevero" size="60" class="inputTexto" readonly="true" value="" />
                                <a href="" id="anchorListaValidado" name="anchorListaSevero">
                                    <span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonAplicacion"
                                          name="botonAplicacion" style="cursor:hand;"></span>
                                </a>
                            </div>
                        </div>                       
                    </div>
                    <br>
                    <div class="lineaFormulario">
                        <div style="width: 190px; float: left;" class="etiqueta">
                            <%=meLanbide58I18n.getMensaje(idiomaUsuario,"label.severaLan")%>
                        </div>
                        <div>
                            <div>
                                <input type="text" name="codListaValidado" id="codListaValidado" size="10" class="inputTexto" value="" onkeyup="xAMayusculas(this);" />
                                <input type="text" name="descListaValidado"  id="descListaValidado" size="60" class="inputTexto" readonly="true" value="" />
                                <a href="" id="anchorListaValidado" name="anchorListaValidado">
                                    <span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonAplicacion"
                                          name="botonAplicacion" style="cursor:hand;"></span>
                                </a>
                            </div>
                        </div>                       
                    </div>
                    <br>
                    <div class="lineaFormulario">
                        <div class="etiqueta" style="width: 190px; float: left;">
                            <%=meLanbide58I18n.getMensaje(idiomaUsuario,"label.tipoContrato1")%>
                        </div>
                        <div>
                            <div>
                                <input type="text" name="codListaTipoCon1" id="codListaTipoCon1" size="10" class="inputTexto" value="" onkeyup="xAMayusculas(this);" />
                                <input type="text" name="descListaTipoCon1"  id="descListaTipoCon1" size="60" class="inputTexto" readonly="true" value="" />
                                <a href="" id="anchorListaTipoCon1" name="anchorListaTipoCon1">
                                    <span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonAplicacion"
                                          name="botonAplicacion" style="cursor:hand;"></span>
                                </a>
                            </div>
                        </div>
                    </div>
                    <br>
                    <div class="lineaFormulario">
                        <div class="etiqueta" style="width: 190px; float: left;">
                            <%=meLanbide58I18n.getMensaje(idiomaUsuario,"label.tipoContrato2")%>
                        </div>
                        <div>
                            <div>
                                <input type="text" name="codListaTipoCon2" id="codListaTipoCon2" size="10" class="inputTexto" value="" onkeyup="xAMayusculas(this);" />
                                <input type="text" name="descListaTipoCon2"  id="descListaTipoCon2" size="60" class="inputTexto" readonly="true" value="" />
                                <a href="" id="anchorListaTipoCon2" name="anchorListaTipoCon2">
                                    <span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonAplicacion"
                                          name="botonAplicacion" style="cursor:hand;"></span>
                                </a>
                            </div>
                        </div>
                    </div>
                    <br>
                    <div class="lineaFormulario">
                        <div class="etiqueta" style="width: 190px; float: left;">
                            <%=meLanbide58I18n.getMensaje(idiomaUsuario,"label.porcJornada")%>
                        </div>
                        <div style="width: 450px; float: left;">
                            <div style="float: left;">
                                <input id="porcJornada" name="porcJornada" type="text" class="inputTextoObligatorio" size="5" maxlength="5" 
                                       value="<%=datModif != null && datModif.getPorcJornada() != null ? datModif.getPorcJornada().toString().replaceAll("\\.", ","): ""%>" onchange="reemplazarPuntos(this);"/>
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

                /*desplegable sexo*/

                listaCodigosSexo[0] = "";
                listaDescripcionesSexo[0] = "";
                contador = 0;

                <logic:iterate id="sexo" name="listaSexo" scope="request">
                listaCodigosSexo[contador] = ['<bean:write name="sexo" property="des_val_cod" />'];
                listaDescripcionesSexo[contador] = ['<bean:write name="sexo" property="des_nom" />'];
                contador++;
                </logic:iterate>

                var comboListaSexo = new Combo("ListaSexo");
                comboListaSexo.addItems(listaCodigosSexo, listaDescripcionesSexo);
                comboListaSexo.change = cargarDatosSexo;

                /*desplegable tipo discapacidad*/

                listaCodigosTipoDis[0] = "";
                listaDescripcionesTipoDis[0] = "";
                contador = 0;

                <logic:iterate id="tipoDis" name="listaTipoDis" scope="request">
                listaCodigosTipoDis[contador] = ['<bean:write name="tipoDis" property="des_val_cod" />'];
                listaDescripcionesTipoDis[contador] = ['<bean:write name="tipoDis" property="des_nom" />'];
                contador++;
                </logic:iterate>

                var comboListaTipoDis = new Combo("ListaTipoDis");
                comboListaTipoDis.addItems(listaCodigosTipoDis, listaDescripcionesTipoDis);
                comboListaTipoDis.change = cargarDatosTipoDis;

                /*desplegables tipo contrato*/

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

                listaCodigosTipoCon2[0] = "";
                listaDescripcionesTipoCon2[0] = "";
                contador = 0;

                <logic:iterate id="tipoCon2" name="listaTipoCon2" scope="request">
                listaCodigosTipoCon2[contador] = ['<bean:write name="tipoCon2" property="des_val_cod" />'];
                listaDescripcionesTipoCon2[contador] = ['<bean:write name="tipoCon2" property="des_nom" />'];
                contador++;
                </logic:iterate>

                var comboListaTipoCon2 = new Combo("ListaTipoCon2");
                comboListaTipoCon2.addItems(listaCodigosTipoCon2, listaDescripcionesTipoCon2);
                comboListaTipoCon2.change = cargarDatosTipoCon2;

                /*desplegable SEVERO*/
                listaCodigosSevero[0] = "";
                listaDescripcionesSevero[0] = "";
                contador = 0;

                <logic:iterate id="severo" name="listaSiNo" scope="request">
                listaCodigosSevero[contador] = ['<bean:write name="severo" property="des_val_cod" />'];
                listaDescripcionesSevero[contador] = ['<bean:write name="severo" property="des_nom" />'];
                contador++;
                </logic:iterate>

                var comboListaSevero = new Combo("ListaSevero");
                comboListaSevero.addItems(listaCodigosSevero, listaDescripcionesSevero);
                comboListaSevero.change = cargarDatosSevero;

                /*desplegable VALIDADO*/
                listaCodigosValidado[0] = "";
                listaDescripcionesValidado[0] = "";
                contador = 0;

                <logic:iterate id="validado" name="listaSiNo" scope="request">
                listaCodigosValidado[contador] = ['<bean:write name="validado" property="des_val_cod" />'];
                listaDescripcionesValidado[contador] = ['<bean:write name="validado" property="des_nom" />'];
                contador++;
                </logic:iterate>

                var comboListaValidado = new Combo("ListaValidado");
                comboListaValidado.addItems(listaCodigosValidado, listaDescripcionesValidado);
                comboListaValidado.change = cargarDatosValidado;

                /*desplegable SUBTIPO*/
                listaCodigosSubtipo[0] = "";
                listaDescripcionesSubtipo[0] = "";
                contador = 0;
                <logic:iterate id="subtipo" name="listaSubtipos" scope="request">
                listaCodigosSubtipo[contador] = ['<bean:write name="subtipo" property="des_val_cod" />'];
                listaDescripcionesSubtipo[contador] = ['<bean:write name="subtipo" property="des_nom" />'];
                contador++;
                </logic:iterate>

                var comboListaSubtipo = new Combo("ListaSubtipo");
                comboListaSubtipo.addItems(listaCodigosSubtipo, listaDescripcionesSubtipo);
                comboListaSubtipo.change = cargarDatosSubtipo;


                if (nuevo == 0) {
                    rellenardatModificar();
                    document.getElementById("dni_nif").disabled = true;
                    document.getElementById("subtitulo").innerText = "<%=meLanbide58I18n.getMensaje(idiomaUsuario,"label.modificarC")%>";
                } else {
                    desbloquearCampos();
                    var linea = "<%=numLineaS%>";
                    document.getElementById('numLinea').value = linea;
                    document.getElementById("subtitulo").innerText = "<%=meLanbide58I18n.getMensaje(idiomaUsuario,"label.nuevoAcceso")%>";
                }

            </script>
            <div id="popupcalendar" class="text"></div>        
        </div>
    </body>
</html> 

<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>

<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide37.i18n.MeLanbide37I18n" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide37.vo.ExpedienteCEPAPVO"%>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide37.util.ConstantesMeLanbide37" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide37.util.ConfigurationParameter" %>
<%@page import="java.util.List" %>
<html>
    <head>
        <meta http-equiv="X-UA-Compatible" content="IE=Edge" />
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>CEPAP - Entrega título</title>
        <%
   
            UsuarioValueObject usuarioVO = new UsuarioValueObject();
            int idiomaUsuario = 1;
            int numeroTotalRegistros = 0;
            String css = "";
            try
            {
                if (session.getAttribute("usuario") != null) {
                    usuarioVO = (UsuarioValueObject) session.getAttribute("usuario");
                    idiomaUsuario = usuarioVO.getIdioma();
                    css = usuarioVO.getCss();
                }
            }
            catch(Exception ex)
            {
        
            }
    
            if (request.getAttribute("numeroTotalRegistros") != null) {
                numeroTotalRegistros = (Integer)request.getAttribute("numeroTotalRegistros");
            }

            MeLanbide37I18n meLanbide37I18n = MeLanbide37I18n.getInstance();
            String numMaxLineas = ConfigurationParameter.getParameter(ConstantesMeLanbide37.NUMERO_LINEAS_PAGINA, ConstantesMeLanbide37.FICHERO_PROPIEDADES);
            if(numMaxLineas == null || numMaxLineas=="")
                numMaxLineas="15";

        %>

        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/estilo.css"/>
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/extension/melanbide37/melanbide37.css"/>

        <jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />

        <script type="text/javascript">
            var APP_CONTEXT_PATH = '<%=request.getContextPath()%>';
        </script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/general.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/popup.js"></script>
        <script type="text/javascript"  src="<%=request.getContextPath()%>/scripts/domlay.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/tabpane.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/TablaNueva.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/melanbide53/lanbide.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/calendario.js"></script>
        <script type="text/javascript"  src="<%=request.getContextPath()%>/scripts/listas.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/validaciones.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/listaComboBox.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/melanbide37/ecaUtils.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/TablaNueva.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/JavaScriptUtil.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/Parsers.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/melanbide53/InputMask.js"></script>

        <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/css/font-awesome.min.css'/>" media="screen" >
        <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/less/animated.less'/>" media="screen" >
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script>
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/>
        <script type="text/javascript">

            function crearTabla() {
                tablaExpedientes = new Tabla(true, '<%=descriptor.getDescripcion("buscar")%>', '<%=descriptor.getDescripcion("anterior")%>', '<%=descriptor.getDescripcion("siguiente")%>', '<%=descriptor.getDescripcion("mosFilasPag")%>', '<%=descriptor.getDescripcion("msgNoResultBusq")%>', '<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>', '<%=descriptor.getDescripcion("filtrDeTotal")%>', '<%=descriptor.getDescripcion("primero")%>', '<%=descriptor.getDescripcion("ultimo")%>', document.getElementById('listaExpedientes'), 4000);

                tablaExpedientes.addColumna('35', 'center', "");

                tablaExpedientes.addColumna('150', 'center', "<%=meLanbide37I18n.getMensaje(idiomaUsuario,"entregaTitulo.tablaExpedientes.col0")%>");
                tablaExpedientes.addColumna('350', 'center', "<%=meLanbide37I18n.getMensaje(idiomaUsuario,"entregaTitulo.tablaExpedientes.col1")%>");
                tablaExpedientes.addColumna('100', 'center', "<%=meLanbide37I18n.getMensaje(idiomaUsuario,"entregaTitulo.tablaExpedientes.col2")%>");
                tablaExpedientes.addColumna('300', 'center', "<%=meLanbide37I18n.getMensaje(idiomaUsuario,"entregaTitulo.tablaExpedientes.col3")%>");
                tablaExpedientes.addColumna('100', 'center', "<%=meLanbide37I18n.getMensaje(idiomaUsuario,"entregaTitulo.tablaExpedientes.col4")%>");
                tablaExpedientes.addColumna('100', 'center', "<%=meLanbide37I18n.getMensaje(idiomaUsuario,"entregaTitulo.tablaExpedientes.col5")%>");

                tablaExpedientes.displayCabecera = true;
                tablaExpedientes.height = 370;
            }

        </script>
    </head>
    <body class="bandaBody"onload="javascript:{
                //pleaseWait('off');
            }" >
        <div id="divTituloEntregado" style="height: 100%; width: 100%;overflow-y: scroll;">
            <form onload="">

                <h2 class="legendAzul" id="pestanaPrinc"><%=meLanbide37I18n.getMensaje(idiomaUsuario,"label.tituloPrincipal")%></h2>
                <div class="contenidoPantalla">
                    <div style="clear: both;">
                        <div id="div_label" class="sub3titulo" style=" text-align: center;">
                            <label class="sub3titulo" style=" text-align: center; position: relative;" ><%=meLanbide37I18n.getMensaje(idiomaUsuario, "legend.titulo.excelOficina")%></label>
                        </div>
                        <br/>
                        <div>
                            <table id="tablaExcel">
                                <tbody>
                                    <tr>
                                        <td style="width: 10%">
                                            <label class="etiqueta">
                                                <%=meLanbide37I18n.getMensaje(idiomaUsuario,"label.titulo.oficina")%>
                                            </label>
                                        </td>
                                        <td>
                                            <input id="oficina" name="oficina" type="text" class="inputTextoM37" size="50" maxlength="200">
                                        </td>
                                        <td style="text-align: center;">
                                            <input style="margin-left: 50px;" type="button" id="botonExcelOficina" name="botonExcelOficina" class="botonGeneral" value="<%=meLanbide37I18n.getMensaje(idiomaUsuario, "btn.excelOficina")%>" onclick="pulsarExcelOficina();">
                                        </td>
                                    </tr>
                                </tbody>
                            </table>
                        </div>
                    </div>
                    <div style="clear: both;">
                        <div id="div_label" class="sub3titulo" style=" text-align: center;">
                            <label class="sub3titulo" style=" text-align: center; position: relative;" ><%=meLanbide37I18n.getMensaje(idiomaUsuario, "legend.titulo.busqueda")%></label>
                        </div>
                        <br/>
                        <div>
                            <table id="tablaBusqueda">
                                <tbody>

                                    <!-- <tr>
                                        <td style="width: 15%">
                                            <label class="etiqueta"><%=meLanbide37I18n.getMensaje(idiomaUsuario,"label.busqueda.fecha")%></label>                            
                                        </td>
                                        <td>
                                            <table style="width: 50%">
                                                <tbody>
                                                    <tr>
                                                        <td style="width: 6%">
                                                            <div style="float: left;">
                                                                <input type="text" class="inputTxtFecha" 
                                                                       id="meLanbide37Fecha_busqE" name="meLanbide37Fecha_busqE"
                                                                       maxlength="10"  size="10"
                                                                       value=""
                                                                       onkeyup = "return SoloCaracteresFechaLanbide(this);"
                                                                       onblur = "javascript:return comprobarFechaLanbide(this);"
                                                                       onfocus="javascript:this.select();"/>
                                                                <A href="javascript:calClick(event);return false;" onClick="mostrarCalFechaE(event);return false;" style="text-decoration:none;">   
                                                                    <IMG style="border: 0px solid none" height="17" id="calMeLanbide37Fecha_busqE" name="calMeLanbide37Fecha_busqE" border="0" 
                                                                         src="<c:url value='/images/calendario/icono.gif'/>" >
                                                                </A>
                                                            </div>
                                                        </td>
                                                        <td style="width: 2%; text-align: center;">
                                                            <label class="etiqueta" style="float: left; text-align: center;">
                                    <%=meLanbide37I18n.getMensaje(idiomaUsuario,"label.busqueda.hasta")%>
                                </label> 
                            </td>
                            <td style="width: 7%">
                                <div style="float: left;">
                                    <input type="text" class="inputTxtFecha" 
                                           id="meLanbide37Fecha_busqS" name="meLanbide37Fecha_busqS"
                                           maxlength="10"  size="10"
                                           value=""
                                           onkeyup = "return SoloCaracteresFechaLanbide(this);"
                                           onblur = "javascript:return comprobarFechaLanbide(this);"
                                           onfocus="javascript:this.select();"/>
                                    <A href="javascript:calClick(event);return false;" onClick="mostrarCalFechaS(event);return false;" style="text-decoration:none;">   
                                        <IMG style="border: 0px solid none" height="17" id="calMeLanbide37Fecha_busqS" name="calMeLanbide37Fecha_busqS" border="0" 
                                             src="<c:url value='/images/calendario/icono.gif'/>" >
                                    </A>
                                </div>
                            </td>
                        </tr>
                    </tbody>
                </table>
            </td>
        </tr> -->

                                    <tr>
                                        <td style="width: 15%">
                                            <label class="etiqueta">
                                                <%=meLanbide37I18n.getMensaje(idiomaUsuario,"label.titulo.dni")%>
                                            </label>
                                        </td>
                                        <td>
                                            <input id="dniInteresado_busq" name="dniInteresado_busq" type="text" class="inputTextoM37" size="32" maxlength="32">
                                        </td>
                                    </tr>

                                    <tr>
                                        <td style="width: 15%">
                                            <label class="etiqueta">
                                                <%=meLanbide37I18n.getMensaje(idiomaUsuario,"label.titulo.numexpediente")%>
                                            </label>
                                        </td>
                                        <td>
                                            <input id="numeroExpediente_busq" name="numeroExpediente_busq" type="text" class="inputTextoM37" size="32" maxlength="100">
                                        </td>
                                    </tr>

                                    <tr>
                                        <td style="text-align: center;" colspan="2">
                                            <input type="button" id="botonBusqueda" name="botonBusqueda" class="botonGeneral" value="<%=meLanbide37I18n.getMensaje(idiomaUsuario, "btn.buscar")%>" onclick="pulsarBusquedaFiltrando();">
                                        </td>
                                    </tr>
                                </tbody>
                            </table>
                        </div>
                    </div>
                    <div style="clear: both;">
                        <div>
                            <div id="div_label" class="sub3titulo" style=" text-align: center; width: 95% ">
                                <label class="sub3titulo" style=" text-align: center; width: 100%; position: relative;" ><%=meLanbide37I18n.getMensaje(idiomaUsuario, "legend.titulo.tabla")%></label>
                            </div>    
                            <div id="divGeneral" style="overflow-y: auto; overflow-x: auto; height: 420px;">     
                                <div id="listaExpedientes" style="padding: 5px; width:98%; height: 410px; text-align: center; overflow-x:auto; overflow-y:auto;margin:0px;margin-top:0px;" align="center"></div>
                            </div>
                            <br/>
                            <div class="botonera" style="text-align: center;">
                                <!-- <input type= "button" id="all" name="all" class="botonGeneral" value="<%=meLanbide37I18n.getMensaje(idiomaUsuario, "btn.selTodos")%>"  onClick="pulsarSeleccTodos();"> -->
                                <input type="button" id="btnMarcarEntrega" name="btnMarcarEntrega" class="botonGeneral" value="<%=meLanbide37I18n.getMensaje(idiomaUsuario, "btn.marcar")%>" onclick="pulsarMarcarEntrega();">
                            </div>
                        </div>
                    </div>
                    <br/>
                </div>


                <script type="text/javascript">
                    var mensajeValidacion = "";

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

                    function pulsarExcelOficina() {
                        var CONTEXT_PATH = '<%=request.getContextPath()%>';
                        var url = CONTEXT_PATH + "/PeticionModuloIntegracion.do";
                        var parametros = "";
                        parametros = '?tarea=preparar&modulo=MELANBIDE37&operacion=generarExcelOficina&tipo=0&oficina=' + document.getElementById('oficina').value;

                        window.open(url + parametros, "_blank");
                    }

                    /*function comprobarFechaLanbide(inputFecha) {
                     var formato = 'dd/mm/yyyy';
                     if (Trim(inputFecha.value) != '') {
                     var D = ValidarFechaConFormatoLanbide(inputFecha.value, formato);
                     if (!D[0]) {
                     jsp_alerta("A", "<%=meLanbide37I18n.getMensaje(idiomaUsuario, "msg.fechaNoVal")%>");
                     document.getElementById(inputFecha.name).focus();
                     document.getElementById(inputFecha.name).select();
                     return false;
                     } else {
                     inputFecha.value = D[1];
                     return true;
                     }//if (!D[0])
                     }//if (Trim(inputFecha.value)!='')
                     return true;
                     }*/

                    /*function mostrarCalFechaE(evento) {
                     if (window.event)
                     evento = window.event;
                     if (document.getElementById("calMeLanbide37Fecha_busqE").src.indexOf("icono.gif") != -1)
                     showCalendar('forms[0]', 'meLanbide37Fecha_busqE', null, null, null, '', 'calMeLanbide37Fecha_busqE', '', null, null, null, null, null, null, null, null, evento);
                     }//mostrarCalFechaE
                     
                     function mostrarCalFechaS(evento) {
                     if (window.event)
                     evento = window.event;
                     if (document.getElementById("calMeLanbide37Fecha_busqS").src.indexOf("icono.gif") != -1)
                     showCalendar('forms[0]', 'meLanbide37Fecha_busqS', null, null, null, '', 'calMeLanbide37Fecha_busqS', '', null, null, null, null, null, null, null, null, evento);
                     }*/

                    function recargarTablaExpedientes(result) {
                        //pleaseWait('on');
                        var fila;
                        listaExpedientes = new Array();
                        listaExpedientesTabla = new Array();

                        crearTabla();

                        var contador = 0;
                        for (var i = 1; i < result.length; i++) {
                            fila = result[i];
                            //listaExpedientes[i - 1] = fila;
                            listaExpedientes[i - 1] = [check('false', contador), fila[1], fila[2], fila[3], fila[4], fila[5], fila[6]];
                            listaExpedientesTabla[i - 1] = [check('false', contador), fila[1], fila[2], fila[3], fila[4], fila[5], fila[6]];
                            contador++;
                        }

                        //pleaseWait('off');
                        tablaExpedientes.lineas = listaExpedientesTabla;
                        tablaExpedientes.displayTablaConTooltips(listaExpedientesTabla);

                        if (navigator.appName.indexOf("Internet Explorer") != -1 || (navigator.appName.indexOf("Netscape") != -1 && navigator.appCodeName.match(/Mozilla/))) {
                            try {
                                var div = document.getElementById('listaExpedientes');
                                if (navigator.appName.indexOf("Internet Explorer") != -1 && !(navigator.userAgent.match(/compatible/))) {
                                    div.children[0].children[0].children[0].children[0].children[0].children[0].children[1].style.width = '100%';
                                    div.children[0].children[1].children[0].children[0].children[0].children[0].style.width = '100%';
                                } else {
                                    div.children[0].children[0].children[0].children[0].children[0].style.width = '100%';
                                    div.children[0].children[1].children[0].style.width = '100%';
                                }
                            } catch (err) {
                                jsp_alerta("A", '<%=meLanbide37I18n.getMensaje(idiomaUsuario, "msg.errorGen")%>' + ' - ' + err.message);
                            }
                        }

                        quitarAtributosTablaExpedientes();

                        habilitarBoton('#btnMarcarEntrega');
                        habilitarBoton('#botonBusqueda');

                    }

                    function pulsarBusquedaFiltrando() {

                        if (validarFiltrosBusqueda()) {
                            deshabilitarBoton('#btnMarcarEntrega');
                            deshabilitarBoton('#botonBusqueda');

                            var ajax = getXMLHttpRequest();
                            var nodos = null;
                            var nodosNumRegTotal = null;
                            var CONTEXT_PATH = '<%=request.getContextPath()%>';
                            var url = CONTEXT_PATH + "/PeticionModuloIntegracion.do";
                            var parametros = "";
                            var control = new Date();
                            parametros = "tarea=preparar&modulo=MELANBIDE37&operacion=busquedaFiltrandoListaExpedientes&tipo=0&control=" + control.getTime()
                                    //+ "&meLanbide37Fecha_busqE=" + document.getElementById('meLanbide37Fecha_busqE').value
                                    //+ "&meLanbide37Fecha_busqS=" + document.getElementById('meLanbide37Fecha_busqS').value
                                    + "&dniInteresado_busq=" + document.getElementById('dniInteresado_busq').value
                                    + "&numeroExpediente_busq=" + document.getElementById('numeroExpediente_busq').value
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
                                nodosNumRegTotal = xmlDoc.getElementsByTagName("NUMTOTALREGISTROS");
                                var elementoRegTotal = nodosNumRegTotal[0];

                                if (elementoRegTotal != null && elementoRegTotal.childNodes != null) {
                                    numeroTotalRegistros = elementoRegTotal.childNodes[0].nodeValue;
                                }
                                var elemento = nodos[0];
                                var hijos = elemento.childNodes;
                                var codigoOperacion = null;
                                var listaNueva = new Array();
                                var fila = new Array();
                                var nodoFila;
                                var hijosFila;



                                for (j = 0; hijos != null && j < hijos.length; j++) {
                                    if (hijos[j].nodeName == "CODIGO_OPERACION") {
                                        codigoOperacion = hijos[j].childNodes[0].nodeValue;
                                        listaNueva[j] = codigoOperacion;
                                    }//if(hijos[j].nodeName=="CODIGO_OPERACION")
                                    else if (hijos[j].nodeName == "FILA") {
                                        nodoFila = hijos[j];
                                        hijosFila = nodoFila.childNodes;
                                        for (var cont = 0; cont < hijosFila.length; cont++) {
                                            if (hijosFila[cont].nodeName == "NUM_EXPEDIENTE") {
                                                if (hijosFila[cont].childNodes.length > 0) {
                                                    fila[1] = hijosFila[cont].childNodes[0].nodeValue;
                                                } else {
                                                    fila[1] = '-';
                                                }
                                            } else if (hijosFila[cont].nodeName == "NOMBRE_APELLIDOS") {
                                                if (hijosFila[cont].childNodes.length > 0) {
                                                    fila[2] = hijosFila[cont].childNodes[0].nodeValue;

                                                } else {
                                                    fila[2] = '-';
                                                }
                                            } else if (hijosFila[cont].nodeName == "CP") {
                                                if (hijosFila[cont].childNodes.length > 0) {
                                                    fila[3] = hijosFila[cont].childNodes[0].nodeValue;
                                                } else {
                                                    fila[3] = '-';
                                                }
                                            } else if (hijosFila[cont].nodeName == "LUGAR_ENVIO") {
                                                if (hijosFila[cont].childNodes.length > 0 && hijosFila[cont].childNodes[0].nodeValue != 'null') {
                                                    fila[4] = hijosFila[cont].childNodes[0].nodeValue;
                                                } else {
                                                    fila[4] = '-';
                                                }
                                            } else if (hijosFila[cont].nodeName == "LOTE") {
                                                if (hijosFila[cont].childNodes.length > 0 && hijosFila[cont].childNodes[0].nodeValue != 'null') {
                                                    fila[5] = hijosFila[cont].childNodes[0].nodeValue;
                                                } else {
                                                    fila[5] = '-';
                                                }
                                            } else if (hijosFila[cont].nodeName == "TITULO_ENTREGADO") {
                                                if (hijosFila[cont].childNodes.length > 0) {
                                                    fila[6] = hijosFila[cont].childNodes[0].nodeValue;
                                                } else {
                                                    fila[6] = '-';
                                                }
                                            }
                                        }
                                        listaNueva[j] = fila;
                                        fila = new Array();
                                    }
                                }//for(j=0;hijos!=null && j<hijos.length;j++)

                                if (codigoOperacion == "0") {

                                    recargarTablaExpedientes(listaNueva);

                                } else if (codigoOperacion == "1") {
                                    jsp_alerta("A", '<%=meLanbide37I18n.getMensaje(idiomaUsuario,"error.errorBD")%>');
                                } else if (codigoOperacion == "2") {
                                    jsp_alerta("A", '<%=meLanbide37I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                                } else if (codigoOperacion == "3") {
                                    jsp_alerta("A", '<%=meLanbide37I18n.getMensaje(idiomaUsuario,"error.pasoParametros")%>');
                                } else {
                                    jsp_alerta("A", '<%=meLanbide37I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                                }
                                //}//if(
                            } catch (Err) {

                                jsp_alerta("A", '<%=meLanbide37I18n.getMensaje(idiomaUsuario,"error.errorGen")%>' + ' - ' + Err.message);

                            }//try-catch
                        } else {
                            jsp_alerta('A', mensajeValidacion);
                        }
                    }

                    function validarFiltrosBusqueda() {
                        var result = true;
                        /*if (document.getElementById("meLanbide37Fecha_busqE").value != "" && document.getElementById("meLanbide37Fecha_busqS").value != "")
                         {
                         var fechaentrada = document.getElementById("meLanbide37Fecha_busqE").value.split("/");
                         var fechasalida = document.getElementById("meLanbide37Fecha_busqS").value.split("/");
                         fechaentrada = fechaentrada[2] + fechaentrada[1] + fechaentrada[0];
                         fechasalida = fechasalida[2] + fechasalida[1] + fechasalida[0];
                         if (fechaentrada > fechasalida) {
                         mensajeValidacion = '<%=meLanbide37I18n.getMensaje(idiomaUsuario, "msg.busqueda.errorintervalosfechaerror")%>';
                         document.getElementById("meLanbide37Fecha_busqE").select();
                         return false;
                         }
                         }*/
                        if (document.getElementById("dniInteresado_busq").value == "" && document.getElementById("numeroExpediente_busq").value == "") {
                            mensajeValidacion = '<%=meLanbide37I18n.getMensaje(idiomaUsuario, "msg.busqueda.errorCamposVacios")%>';
                            return false;
                        }

                        return result;
                    }

                    function check(seleccionado, contador) {
                        var fila = '';
                        if (seleccionado == "true") {
                            fila += '<input type="checkbox" value="true" checked=' + seleccionado + ' id="Seleccionado' + contador + '" />';
                        } else {
                            fila += "<input type='checkbox' value='true' id='Seleccionado" + contador + "' />";
                        }
                        fila += '&nbsp;';
                        return fila;
                    }

                    function getSeleccionados() {
                        var c = 0;
                        var j = 0;

                        listaSeleccionados = new Array();
                        listaNumExpSeleccionados = new Array();
                        for (c = 0; listaExpedientes != null && c < listaExpedientes.length; c++) {
                            if (document.getElementById("Seleccionado" + c) != null) {
                                if (document.getElementById("Seleccionado" + c).checked) {
                                    //var numExp = listaExpedientes[c].toString().substring(listaExpedientes[c].toString().indexOf(",")+1);
                                    var numExp = listaExpedientes[c][1];
                                    listaNumExpSeleccionados[j] = numExp;
                                    listaSeleccionados[j] = "1";
                                    j++;
                                }
                            }
                        }
                    }

                    /*function pulsarSeleccTodos(){
                     var nodoCheck = document.getElementsByTagName("input");
                     var varCheck = "true";
                     
                     for (i=0; i<nodoCheck.length; i++){
                     if (nodoCheck[i].type == "checkbox" && nodoCheck[i].disabled == false) {
                     nodoCheck[i].checked = varCheck;
                     }
                     }
                     }*/

                    function pulsarMarcarEntrega() {

                        getSeleccionados();

                        if (listaNumExpSeleccionados.length > 0) {

                            deshabilitarBoton('#btnMarcarEntrega');
                            deshabilitarBoton('#botonBusqueda');

                            var resultado = jsp_alerta('', '<%=meLanbide37I18n.getMensaje(idiomaUsuario, "msg.preguntaMarcar")%>');
                            if (resultado == 1) {

                                var stringListaExpJSON = JSON.stringify(listaNumExpSeleccionados);

                                var ajax = getXMLHttpRequest();
                                var nodos = null;
                                var nodosNumRegTotal = null;
                                var CONTEXT_PATH = '<%=request.getContextPath()%>';
                                var url = CONTEXT_PATH + "/PeticionModuloIntegracion.do";
                                var parametros = "";
                                var control = new Date();
                                parametros = "tarea=preparar&modulo=MELANBIDE37&operacion=marcarTitulosEntregado&tipo=0&control=" + control.getTime()
                                        + "&listaExpedientesMarcados=" + stringListaExpJSON
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
                                        }
                                    }

                                    nodos = xmlDoc.getElementsByTagName("RESPUESTA");
                                    nodosNumRegTotal = xmlDoc.getElementsByTagName("NUMEXPEDIENTESMARCADOS");
                                    var elementoRegTotal = nodosNumRegTotal[0];
                                    var numExpedientesMarc = 0;

                                    if (elementoRegTotal != null && elementoRegTotal.childNodes != null) {
                                        numExpedientesMarc = elementoRegTotal.childNodes[0].nodeValue;
                                    }
                                    var elemento = nodos[0];
                                    var hijos = elemento.childNodes;
                                    var codigoOperacion = null;

                                    for (j = 0; hijos != null && j < hijos.length; j++) {
                                        if (hijos[j].nodeName == "CODIGO_OPERACION") {
                                            codigoOperacion = hijos[j].childNodes[0].nodeValue;
                                        }

                                    }

                                    if (codigoOperacion == "0") {
                                        jsp_alerta("A", '<%=meLanbide37I18n.getMensaje(idiomaUsuario,"msg.marcados")%>');
                                        pulsarBusquedaFiltrando();
                                    } else if (codigoOperacion == "1") {
                                        jsp_alerta("A", '<%=meLanbide37I18n.getMensaje(idiomaUsuario,"error.errorBD")%>');
                                    } else if (codigoOperacion == "2") {
                                        jsp_alerta("A", '<%=meLanbide37I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                                    } else if (codigoOperacion == "3") {
                                        jsp_alerta("A", '<%=meLanbide37I18n.getMensaje(idiomaUsuario,"error.pasoParametros")%>');
                                    } else {
                                        jsp_alerta("A", '<%=meLanbide37I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                                    }
                                    //}//if(
                                } catch (Err) {
                                    jsp_alerta("A", '<%=meLanbide37I18n.getMensaje(idiomaUsuario,"error.errorGen")%>' + ' - ' + Err.message);
                                }
                            } else {
                                habilitarBoton('#btnMarcarEntrega');
                                habilitarBoton('#botonBusqueda');
                            }
                        } else {
                            jsp_alerta("A", '<%=meLanbide37I18n.getMensaje(idiomaUsuario,"error.errorNoSeleccionados")%>');
                        }
                    }

                    function quitarAtributosTablaExpedientes() {
                        $('td').removeAttr('title');
                        $('td').removeAttr('onclick');
                        $('td').removeAttr('ondblclick');
                        $("#listaExpedientes tr").css('cursor', 'default');
                    }

                    function habilitarBoton(elemento) {
                        $(elemento).prop('disabled', false);
                    }
                    function deshabilitarBoton(elemento) {
                        $(elemento).prop('disabled', true);
                    }

                </script>            

            </form>
        </div>

    </body>
</html>
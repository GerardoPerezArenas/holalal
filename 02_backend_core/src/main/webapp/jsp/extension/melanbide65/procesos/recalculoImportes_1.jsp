<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>

<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide65.i18n.MeLanbide65I18n" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide65.util.ConstantesMeLanbide65" %>
<%@page import="java.util.List" %>
<html>
    <head>
        <meta http-equiv="X-UA-Compatible" content="IE=Edge" />
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title> - RecalculoImportes - </title>
        <%
   
            UsuarioValueObject usuarioVO = new UsuarioValueObject();
            int idiomaUsuario = 1;
            String css = "";
            int numeroTotalRegistros = 0;
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
    
            MeLanbide65I18n meLanbide65I18n = MeLanbide65I18n.getInstance();
    
            if (request.getAttribute("numeroTotalRegistros") != null) {
                numeroTotalRegistros = (Integer)request.getAttribute("numeroTotalRegistros");
            }
            String numMaxLineas = "15";
    

        %>

        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/estilo.css"/>
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/extension/melanbide65/melanbide65.css"/>

        <jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />

        <script type="text/javascript">
            var APP_CONTEXT_PATH = '<%=request.getContextPath()%>';
        </script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/general.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/popup.js"></script>
        <script type="text/javascript"  src="<%=request.getContextPath()%>/scripts/domlay.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/tabpane.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/TablaNueva.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/calendario.js"></script>
        <script type="text/javascript"  src="<%=request.getContextPath()%>/scripts/listas.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/validaciones.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/listaComboBox.js"></script>



        <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/css/font-awesome.min.css'/>" media="screen" >
        <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/less/animated.less'/>" media="screen" >
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script>
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/>

        <script type="text/javascript">

            function crearTabla() {
                tablaExpedientes = new Tabla(true, '<%=descriptor.getDescripcion("buscar")%>', '<%=descriptor.getDescripcion("anterior")%>', '<%=descriptor.getDescripcion("siguiente")%>', '<%=descriptor.getDescripcion("mosFilasPag")%>', '<%=descriptor.getDescripcion("msgNoResultBusq")%>', '<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>', '<%=descriptor.getDescripcion("filtrDeTotal")%>', '<%=descriptor.getDescripcion("primero")%>', '<%=descriptor.getDescripcion("ultimo")%>', document.getElementById('listaExpedientes'), 4000);
                tablaExpedientes.addColumna('150', 'center', "<%=meLanbide65I18n.getMensaje(idiomaUsuario,"recalculoImporte.tablaExpedientes.col0")%>");
                tablaExpedientes.addColumna('350', 'center', "<%=meLanbide65I18n.getMensaje(idiomaUsuario,"recalculoImporte.tablaExpedientes.col1")%>");
                tablaExpedientes.addColumna('50', 'center', "<%=meLanbide65I18n.getMensaje(idiomaUsuario,"recalculoImporte.tablaExpedientes.col2")%>");
                tablaExpedientes.addColumna('150', 'right', "<%=meLanbide65I18n.getMensaje(idiomaUsuario,"recalculoImporte.tablaExpedientes.col3")%>");
                tablaExpedientes.addColumna('150', 'right', "<%=meLanbide65I18n.getMensaje(idiomaUsuario,"recalculoImporte.tablaExpedientes.col4")%>");

                tablaExpedientes.displayCabecera = true;
                tablaExpedientes.height = 370;
            }

        </script>

    </head>
    <body class="bandaBody"onload="javascript:{
                //pleaseWait('off');
            }" >
        <div id="divRecalculoImportes" style="height: 100%; width: 100%;overflow-y: scroll;">


            <h2 class="legendAzul" id="pestanaPrinc"><%=meLanbide65I18n.getMensaje(idiomaUsuario,"label.tituloPrincipalRecalculoImportes")%></h2>
            <div class="contenidoPantalla">
                <div style="clear: both;">
                    <div id="div_label" class="sub3titulo" style=" text-align: center;">
                        <label class="sub3titulo" style=" text-align: center; position: relative;" ><%=meLanbide65I18n.getMensaje(idiomaUsuario, "legend.titulo.busqueda")%></label>
                    </div>
                    <br/>
                    <div>
                        <table id="tablaBusqueda">
                            <tbody>
                                <tr>
                                    <td>
                                        <label class="etiqueta"><%=meLanbide65I18n.getMensaje(idiomaUsuario,"label.titulo.tipoProcedimiento")%></label>
                                        <select id="tipoProcedimiento" name="tipoProcedimiento" onchange="ShowSelected();">
                                            <option value="0"></option>
                                            <option value="UAAP">UAAP</option>
                                            <option value="AEXCE">AEXCE</option>
                                        </select>
                                    </td>   
                                    <td>
                                        <label class="etiqueta"><%=meLanbide65I18n.getMensaje(idiomaUsuario,"label.titulo.PeriodoSubvencionable")%></label>
                                        <select id="periodoSubvencionable" name="periodoSubvencionable" onchange="ShowSelected1();">
                                            <option value="0"></option>
                                            <option value="SI">SI</option>
                                            <option value="NO">NO</option>
                                        </select>
                                    </td>                                      
                                    <td>
                                        <label class="etiqueta" >
                                            <%=meLanbide65I18n.getMensaje(idiomaUsuario,"label.titulo.Ejercicio")%>
                                        </label>
                                        <input id="ejercicio" name="ejercicio" type="text" class="inputTextoObligatorio" size="15" maxlength="4">
                                    </td>
                                    <td>
                                        <label class="etiqueta"><%=meLanbide65I18n.getMensaje(idiomaUsuario,"label.titulo.importeMaximo")%></label>
                                        <input id="importeMaximo" name="importeMaximo" type="text" class="inputTextoObligatorio" size="15" maxlength="15">
                                    </td>
                                    <td style="text-align: center;" colspan="2">
                                        <input type="button" id="btnBuscarExp" name="btnBuscarExp" class="botonGeneral" value="<%=meLanbide65I18n.getMensaje(idiomaUsuario, "btn.buscar")%>" onclick="pulsarBusquedaFiltrando();">
                                        <input type="button" id="btnRecalcularImporte" name="btnRecalcularImporte" class="botonGeneral" value="<%=meLanbide65I18n.getMensaje(idiomaUsuario, "btn.marcar")%>" onclick="pulsarRecalcularImporte();">
                                    </td>
                                </tr>
                            </tbody>
                        </table>
                    </div>
                </div>
                <div style="clear: both;">
                    <div>
                        <div id="div_label" class="sub3titulo" style=" text-align: center; width: 95% ">
                            <label class="sub3titulo" style=" text-align: center; width: 100%; position: relative;" ><%=meLanbide65I18n.getMensaje(idiomaUsuario, "legend.titulo.tabla")%></label>
                        </div>    
                        <div id="divGeneral" style="overflow-y: auto; overflow-x: auto; height: 420px;">     
                            <div id="listaExpedientes" style="padding: 5px; width:98%; height: 410px; text-align: center; overflow-x:auto; overflow-y:auto;margin:0px;margin-top:0px;" align="center"></div>
                        </div>
                        <br/>
                    </div>
                    <br/>
                </div>


                <script type="text/javascript">

                    var mensajeValidacion = "";
                    var codProcedimiento = "";

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

                    function ShowSelected()
                    {
                        /* Para obtener el valor */
                        var codProcedimiento = document.getElementById("tipoProcedimiento").value;
                        //alert(cod);

                        /* Para obtener el texto */
                        //var combo = document.getElementById("tipoProcedimiento");
                        //var selected = combo.options[combo.selectedIndex].text;
                        //alert(selected);
                    }
                    function ShowSelected1()
                    {
                        /* Para obtener el valor */
                        var periodoSubvencionable = document.getElementById("periodoSubvencionable").value;
                        //alert(periodoSubvencionable);

                        /* Para obtener el texto */
                        //var combo = document.getElementById("tipoProcedimiento");
                        //var selected = combo.options[combo.selectedIndex].text;
                        //alert(selected);
                    }


                    function validarFiltrosBusqueda() {
                        var result = true;
                        if (document.getElementById("ejercicio").value == "") {
                            mensajeValidacion = '<%=meLanbide65I18n.getMensaje(idiomaUsuario, "msg.busqueda.errorCamposVacios")%>';
                            return false;
                        }
                        if (document.getElementById("importeMaximo").value == "") {
                            mensajeValidacion = '<%=meLanbide65I18n.getMensaje(idiomaUsuario, "msg.busqueda.errorCamposVacios")%>';
                            return false;
                        }

                        return result;
                    }


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
                            listaExpedientes[i - 1] = [fila[1], fila[2], fila[3], fila[4], fila[5]];
                            listaExpedientesTabla[i - 1] = [fila[1], fila[2], fila[3], fila[4], fila[5]];
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
                                jsp_alerta("A", '<%=meLanbide65I18n.getMensaje(idiomaUsuario, "msg.errorGen")%>' + ' - ' + err.message);
                            }
                        }
                        quitarAtributosTablaExpedientes();

                        habilitarBoton('#botonBusqueda');

                    }

                    function quitarAtributosTablaExpedientes() {
                        $('td').removeAttr('title');
                        $('td').removeAttr('onclick');
                        $('td').removeAttr('ondblclick');
                        $("#listaExpedientes tr").css('cursor', 'default');
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

                    function pulsarBusquedaFiltrando() {

                        if (!validarFiltrosBusqueda()) {
                            jsp_alerta('A', mensajeValidacion);
                        } else {

                            var ajax = getXMLHttpRequest();
                            var nodos = null;
                            var nodosNumRegTotal = null;
                            var url = APP_CONTEXT_PATH + "/PeticionModuloIntegracion.do";
                            var parametros = "tarea=preparar&modulo=MELANBIDE65&operacion=busquedaFiltrandoListaExpedientes&tipo=0"
                                    + "&tipoProcedimiento=" + document.getElementById("tipoProcedimiento").value
                                    + "&ejercicio=" + document.getElementById('ejercicio').value
                                    + "&periodoSubvencionable=" + document.getElementById('periodoSubvencionable').value;

                            try {
                                ajax.open("POST", url, false);
                                ajax.setRequestHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
                                ajax.setRequestHeader("Accept", "text/xml, application/xml, text/plain");
                                ajax.send(parametros);
                                /*       ajax.open("POST", url, false);
                                 ajax.setRequestHeader("Content-Type", "application/x-www-form-urlencoded; charset=ISO-8859-1");
                                 ajax.setRequestHeader("Accept", "text/xml, application/xml, text/plain");
                                 ajax.send(parametros);*/

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
                                            } else if (hijosFila[cont].nodeName == "IMPORTE_CONINI") {
                                                if (hijosFila[cont].childNodes.length > 0 && hijosFila[cont].childNodes[0].nodeValue != 'null') {
                                                    fila[4] = hijosFila[cont].childNodes[0].nodeValue;
                                                } else {
                                                    fila[4] = '-';
                                                }
                                            } else if (hijosFila[cont].nodeName == "IMPTOTRECAL") {
                                                if (hijosFila[cont].childNodes.length > 0) {
                                                    fila[5] = hijosFila[cont].childNodes[0].nodeValue;

                                                } else {
                                                    fila[5] = '-';
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
                                    jsp_alerta("A", '<%=meLanbide65I18n.getMensaje(idiomaUsuario,"error.errorBD")%>');
                                } else if (codigoOperacion == "2") {
                                    jsp_alerta("A", '<%=meLanbide65I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                                } else if (codigoOperacion == "3") {
                                    jsp_alerta("A", '<%=meLanbide65I18n.getMensaje(idiomaUsuario,"error.pasoParametros")%>');
                                } else {
                                    jsp_alerta("A", '<%=meLanbide65I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                                }
                            } catch (Err) {
                                //         alert("SALIDA POR CATCH ERROR");
                                jsp_alerta("A", '<%=meLanbide65I18n.getMensaje(idiomaUsuario,"error.errorGen")%>' + ' - ' + Err.message);
                            }//try-catch
                        }
                    }



                    function habilitarBoton(elemento) {
                        $(elemento).prop('disabled', false);
                    }
                    function deshabilitarBoton(elemento) {
                        $(elemento).prop('disabled', true);
                    }

                    function getSeleccionados() {
                        var c = 0;
                        var j = 0;

                        listaSeleccionados = new Array();
                        listaNumExpSeleccionados = new Array();
                        for (c = 0; listaExpedientes != null && c < listaExpedientes.length; c++) {
                            //if (document.getElementById("Seleccionado"+c)!=null){
                            //if (document.getElementById("Seleccionado"+c).checked){
                            //var numExp = listaExpedientes[c].toString().substring(listaExpedientes[c].toString().indexOf(",")+1);
                            var numExp = listaExpedientes[c][0];
                            listaNumExpSeleccionados[j] = numExp;
                            listaSeleccionados[j] = "1";
                            j++;
                            //}
                            //}
                        }
                    }

                    function pulsarRecalcularImporte() {
                        if (validarFiltrosBusqueda()) {
                            getSeleccionados();

                            //if(listaNumExpSeleccionados.length > 0){

                            //deshabilitarBoton('#btnRecalcularImporte');
                            //deshabilitarBoton('#botonBusqueda');

                            var resultado = jsp_alerta('', '<%=meLanbide65I18n.getMensaje(idiomaUsuario, "msg.preguntaMarcar")%>');
                            if (resultado == 1) {

                                var stringListaExpJSON = JSON.stringify(listaNumExpSeleccionados);

                                var ajax = getXMLHttpRequest();
                                var nodos = null;
                                var nodosNumRegTotal = null;
                                var CONTEXT_PATH = '<%=request.getContextPath()%>';
                                var url = CONTEXT_PATH + "/PeticionModuloIntegracion.do";
                                var parametros = "";
                                var control = new Date();

                                parametros = "tarea=preparar&modulo=MELANBIDE65&operacion=marcarRecalculoImportes&tipo=0"
                                        + "&listaExpedientesMarcados=" + stringListaExpJSON
                                        + "&importeMaximo=" + document.getElementById('importeMaximo').value
                                        + "&tipoProcedimiento=" + document.getElementById("tipoProcedimiento").value;

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
                                        jsp_alerta("A", '<%=meLanbide65I18n.getMensaje(idiomaUsuario,"msg.marcados")%>');
                                        pulsarBusquedaFiltrando();
                                    } else if (codigoOperacion == "1") {
                                        jsp_alerta("A", '<%=meLanbide65I18n.getMensaje(idiomaUsuario,"error.errorBD")%>');
                                    } else if (codigoOperacion == "2") {
                                        jsp_alerta("A", '<%=meLanbide65I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                                    } else if (codigoOperacion == "3") {
                                        jsp_alerta("A", '<%=meLanbide65I18n.getMensaje(idiomaUsuario,"error.pasoParametros")%>');
                                    } else {
                                        jsp_alerta("A", '<%=meLanbide65I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                                    }
                                    //}//if(
                                } catch (Err) {
                                    jsp_alerta("A", '<%=meLanbide65I18n.getMensaje(idiomaUsuario,"error.errorGen")%>' + ' - ' + Err.message);
                                }
                            } else {
                                habilitarBoton('#btnRecalcularImporte');
                                habilitarBoton('#botonBusqueda');
                            }
                            // }else{

                        }
                    }

                </script>   
            </div>

    </body>
</html>
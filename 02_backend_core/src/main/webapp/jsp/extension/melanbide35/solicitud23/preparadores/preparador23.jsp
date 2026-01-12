<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>

<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide35.i18n.MeLanbide35I18n"%>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide35.vo.solicitud.FilaPreparadorECA23VO" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide35.vo.solicitud.EcaSolicitud23VO" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide35.util.ConstantesMeLanbide35" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide35.util.MeLanbide35Utils" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.common.service.config.Config"%>
<%@page import="es.altia.common.service.config.ConfigServiceHelper"%>
<%@page import="java.util.ArrayList" %>
<%@page import="java.util.List" %>
<%@page import="java.text.*" %>
<%@page import="java.math.*" %>

<%
    int idiomaUsuario = 1;
    int apl = 5;
    String css = "";
    UsuarioValueObject usuario = new UsuarioValueObject();
       
    if (session.getAttribute("usuario") != null) {
        usuario = (UsuarioValueObject) session.getAttribute("usuario");
        apl = usuario.getAppCod();
        idiomaUsuario = usuario.getIdioma();
        css = usuario.getCss();
    }
    
//Clase para internacionalizar los mensajes de la aplicación.
    MeLanbide35I18n meLanbide35I18n = MeLanbide35I18n.getInstance();     
    String numExpediente    = request.getParameter("numero");
    
    EcaSolicitud23VO solicitud = (EcaSolicitud23VO)request.getAttribute("SolicitudECA23");
    DecimalFormatSymbols simbolo=new DecimalFormatSymbols();
    simbolo.setDecimalSeparator(',');
    simbolo.setGroupingSeparator('.');
    DecimalFormat formateador = new DecimalFormat("###,##0.00",simbolo);
%>
<jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
<jsp:setProperty name="descriptor"  property="idi_cod" value="<%=idiomaUsuario%>" />
<jsp:setProperty name="descriptor"  property="apl_cod" value="<%=apl%>" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/extension/melanbide35/melanbide35.css"/>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/melanbide35/ecaUtils.js"></script>
<script type="text/javascript">
    function pulsarNuevoPreparadorSolicitudEca() {
        var control = new Date();
        var result = null;
        lanzarPopUpModal('<%=request.getContextPath()%>/PeticionModuloIntegracion.do?tarea=preparar&modulo=MELANBIDE35&operacion=cargarNuevoPreparadorSolicitud23&tipo=0&numero=<%=numExpediente%>&opcion=nuevo&control=' + control.getTime(), 600, 850, 'no', 'no', function (result) {
            recargarTablaPreparadoresSolicitudEca();
        });
    }

    function pulsarEliminarPreparadorSolicitudEca() {
        if (tablaPreparadores.selectedIndex != -1) {
            var resultado = jsp_alerta('', '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "msg.preguntaEliminar")%>');
            if (resultado == 1) {
                var ajax = getXMLHttpRequest();
                var nodos = null;
                var parametros = "";
                var control = new Date();
                parametros = 'tarea=preparar&modulo=MELANBIDE35&operacion=eliminarPreparadorSolicitud23&tipo=0&numero=<%=numExpediente%>&id=' + listaPreparadores[tablaPreparadores.selectedIndex][0] + '&control=' + control.getTime();
                try {
                    ajax.open("POST", baseUrl, false);
                    ajax.setRequestHeader("Content-Type", "application/x-www-form-urlencoded; charset=iso-8859-15");
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
                    var codigoOperacion = nodos[0].firstChild.textContent;
                    if (codigoOperacion == "0") {
                        jsp_alerta("A", '<%=meLanbide35I18n.getMensaje(idiomaUsuario,"msg.registroEliminadoOK")%>');
                        recargarTablaPreparadoresSolicitudEca();
                    } else if (codigoOperacion == "1") {
                        jsp_alerta("A", '<%=meLanbide35I18n.getMensaje(idiomaUsuario,"error.errorEliminarBD")%>');
                    } else if (codigoOperacion == "2") {
                        jsp_alerta("A", '<%=meLanbide35I18n.getMensaje(idiomaUsuario,"error.errorEliminarGen")%>');
                    } else if (codigoOperacion == "3") {
                        jsp_alerta("A", '<%=meLanbide35I18n.getMensaje(idiomaUsuario,"error.pasoParametrosEliminar")%>');
                    } else {
                        jsp_alerta("A", '<%=meLanbide35I18n.getMensaje(idiomaUsuario,"error.errorEliminarGen")%>');
                    }//if(
                } catch (Err) {
                    jsp_alerta("A", '<%=meLanbide35I18n.getMensaje(idiomaUsuario,"error.errorProcesarRespuesta")%>');
                }//try-catch
            }
        } else {
            jsp_alerta('A', '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "msg.msjNoSelecFila")%>');
        }
    }

    function pulsarGuardarHorasTotales() {
        var ajax = getXMLHttpRequest();
        var nodos = null;
        var parametros = "";
        var control = new Date();
        parametros = 'tarea=preparar&modulo=MELANBIDE35&operacion=guardarHorasPreparadorSolicitud23&tipo=0&numero=<%=numExpediente%>&horas=' + document.getElementById('horasConvenio').value + '&sumatoria=' + document.getElementById('sumatoriaHorasTotales').value + '&control=' + control.getTime();
        try {
            ajax.open("POST", baseUrl, false);
            ajax.setRequestHeader("Content-Type", "application/x-www-form-urlencoded; charset=iso-8859-15");
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
            var codigoOperacion = nodos[0].firstChild.textContent;
            if (codigoOperacion == "0") {
                recargarTablaPreparadoresSolicitudEca();
                jsp_alerta("A", '<%=meLanbide35I18n.getMensaje(idiomaUsuario,"msg.datosGuardadosOK")%>');
            } else if (codigoOperacion == "1") {
                jsp_alerta("A", '<%=meLanbide35I18n.getMensaje(idiomaUsuario,"error.errorBD")%>');
            } else if (codigoOperacion == "2") {
                jsp_alerta("A", '<%=meLanbide35I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
            } else if (codigoOperacion == "3") {
                jsp_alerta("A", '<%=meLanbide35I18n.getMensaje(idiomaUsuario,"error.pasoParametros")%>');
            } else {
                jsp_alerta("A", '<%=meLanbide35I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
            }//if(
        } catch (Err) {
            jsp_alerta("A", '<%=meLanbide35I18n.getMensaje(idiomaUsuario,"error.errorProcesarRespuesta")%>');
        }//try-catch
    }

    function crearTablaPreparadoresSolicitudEca() {
        tablaPreparadores = new TablaEca(true, '<%=descriptor.getDescripcion("buscar")%>', '<%=descriptor.getDescripcion("anterior")%>', '<%=descriptor.getDescripcion("siguiente")%>', '<%=descriptor.getDescripcion("mosFilasPag")%>', '<%=descriptor.getDescripcion("msgNoResultBusq")%>', '<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>', '<%=descriptor.getDescripcion("filtrDeTotal")%>', '<%=descriptor.getDescripcion("primero")%>', '<%=descriptor.getDescripcion("ultimo")%>', document.getElementById('listaPreparadores'));
        tablaPreparadores.addColumna('0', 'center', '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.solicitud23.preparadores.tabla.col0")%>'); // ID
        tablaPreparadores.addColumna('0', 'center', '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.solicitud23.preparadores.tabla.col1")%>'); // num exp
        tablaPreparadores.addColumna('250', 'left', '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.solicitud23.preparadores.tabla.col2")%>'); // nombre + ape
        tablaPreparadores.addColumna('50', 'center', '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.solicitud23.preparadores.tabla.col3")%>'); // dni
        tablaPreparadores.addColumna('50', 'center', '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.solicitud23.preparadores.tabla.col4")%>'); // dni
        tablaPreparadores.displayCabecera = true;
        tablaPreparadores.height = 300;
    }

    function recargarTablaPreparadoresSolicitudEca() {
        tablaPreparadores;
        listaPreparadores = new Array();
        listaPreparadoresTabla = new Array();
        var ajax = getXMLHttpRequest();
        var fila;
        var nodos = null;
        var parametros = "";
        var control = new Date();
        parametros = 'tarea=preparar&modulo=MELANBIDE35&operacion=recargarPreparadoresSolicitud23&tipo=0&numero=<%=numExpediente%>&control=' + control.getTime();
        try {
            ajax.open("POST", baseUrl, false);
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
            var result = extraerListadoPreparadoresSolicitudEca(nodos);
            crearTablaPreparadoresSolicitudEca();
            for (var i = 1; i < result.length; i++) {
                fila = result[i];
                listaPreparadores[i - 1] = fila;
                listaPreparadoresTabla[i - 1] = fila;
            }
            tablaPreparadores.lineas = listaPreparadoresTabla;
            tablaPreparadores.displayTabla();
            calcularSumatoriaHorasTotales();
        } catch (Err) {
            jsp_alerta("A", '<%=meLanbide35I18n.getMensaje(idiomaUsuario,"error.errorProcesarRespuesta")%>');
        }
    }

    function extraerListadoPreparadoresSolicitudEca(nodos) {
        var elemento = nodos[0];
        var hijos = elemento.childNodes;
        var codigoOperacion = null;
        var listaPreparadores = new Array();
        var fila = new Array();
        var nodoFila;
        var hijosFila;
        var nodoCampo;
        var j;
        for (j = 0; hijos != null && j < hijos.length; j++) {
            if (hijos[j].nodeName == "CODIGO_OPERACION") {
                codigoOperacion = hijos[j].childNodes[0].nodeValue;
                listaPreparadores[j] = codigoOperacion;
            }//if(hijos[j].nodeName=="CODIGO_OPERACION")                      
            else if (hijos[j].nodeName == "FILA") {
                nodoFila = hijos[j];
                hijosFila = nodoFila.childNodes;
                for (var cont = 0; cont < hijosFila.length; cont++) {
                    if (hijosFila[cont].nodeName == "ID") {
                        nodoCampo = hijosFila[cont];
                        if (nodoCampo.childNodes.length > 0) {
                            fila[0] = nodoCampo.childNodes[0].childNodes[0].nodeValue;
                        } else {
                            fila[0] = '-';
                        }
                    } else if (hijosFila[cont].nodeName == "NUMEROEXPEDIENTE") {
                        nodoCampo = hijosFila[cont];
                        if (nodoCampo.childNodes.length > 0) {
                            fila[1] = nodoCampo.childNodes[0].childNodes[0].nodeValue;
                        } else {
                            fila[1] = '-';
                        }
                    } else if (hijosFila[cont].nodeName == "NOMBRE") {
                        nodoCampo = hijosFila[cont];
                        if (nodoCampo.childNodes.length > 0) {
                            fila[2] = nodoCampo.childNodes[0].childNodes[0].nodeValue;
                        } else {
                            fila[2] = '-';
                        }
                    } else if (hijosFila[cont].nodeName == "DNI") {
                        nodoCampo = hijosFila[cont];
                        if (nodoCampo.childNodes.length > 0) {
                            fila[3] = nodoCampo.childNodes[0].childNodes[0].nodeValue;
                        } else {
                            fila[3] = '-';
                        }
                    } else if (hijosFila[cont].nodeName == "HORASECA") {
                        nodoCampo = hijosFila[cont];
                        if (nodoCampo.childNodes.length > 0) {
                            fila[4] = nodoCampo.childNodes[0].childNodes[0].nodeValue;
                        } else {
                            fila[4] = '-';
                        }
                    } else if (hijosFila[cont].nodeName == "HORASTOTALES") {
                        nodoCampo = hijosFila[cont];
                        if (nodoCampo.childNodes.length > 0) {
                            document.getElementById('horasPreparadores').value = nodoCampo.childNodes[0].childNodes[0].nodeValue;
                        }
                    } else if (hijosFila[cont].nodeName == "HORASCONVENIO") {
                        nodoCampo = hijosFila[cont];
                        if (nodoCampo.childNodes.length > 0) {
                            document.getElementById('horasConvenio').value = nodoCampo.childNodes[0].childNodes[0].nodeValue;
                        }
                    } else if (hijosFila[cont].nodeName == "SUMATORIAHORAS") {
                        nodoCampo = hijosFila[cont];
                        if (nodoCampo.childNodes.length > 0) {
                            document.getElementById('sumatoriaHorasTotales').value = nodoCampo.childNodes[0].childNodes[0].nodeValue;
                        }
                    }
                }
            }
            listaPreparadores[j] = fila;
            fila = new Array();
        }
        return listaPreparadores;
    }

    function calcularSumatoriaHorasTotales() {
        var correcto = true;
        document.getElementById('sumatoriaHorasTotales').value = '';
        if (document.getElementById('horasConvenio').value != '') {
            try {
                if (!validarNumericoDecimalEca(document.getElementById('horasConvenio'), 8, 2)) {
                    correcto = false;
                    document.getElementById('horasConvenio').value = 0;
                }
            } catch (err) {
                correcto = false;
                document.getElementById('horasConvenio').value = 0;
            }
        } else {
            correcto = false;
            document.getElementById('horasConvenio').value = 0;
        }

        if (document.getElementById('horasPreparadores').value != '') {
            var valor = formatearNumero(document.getElementById('horasPreparadores'));
            try {
                if (!validarNumericoDecimalEca(valor, 8, 2)) {
                    correcto = false;
                }
            } catch (err) {
                correcto = false;
            }
        } else {
            correcto = false;
            document.getElementById('horasConvenio').value = 0;
        }

        //Calculo
        if (correcto) {
            document.getElementById('sumatoriaHorasTotales').value = Number(document.getElementById('horasPreparadores').value / document.getElementById('horasConvenio').value).toFixed(2);
        }
    }
     
    function callFromTableToEca(rowID, tableName) {
    }
</script>
<body>
    <div class="tab-page" id="tabPage3511" style="height:520px; width: 98%;">
        <div  class="sub3titulo" style="width: 98%; clear: both; text-align: center; font-size: 14px;text-transform: uppercase;">
            <span id="subtituloPrep"><%=meLanbide35I18n.getMensaje(idiomaUsuario,"label.solicitud.preparadores.titulo")%></span>
        </div>  
        <div id="divGeneral">
            <div id="listaPreparadores" style="padding-left: 10%; width:70%; text-align: center; margin:0px;margin-top:0px; float: left;" align="center"></div>
        </div> 
        <div class="botonera" style=" padding-top: 20px;">
            <input type="button" id="btnNuevoPreparadorSol" name="btnNuevoPreparadorSol" class="botonGeneral" value="<%=meLanbide35I18n.getMensaje(idiomaUsuario, "btn.general.nuevo")%>" onclick="pulsarNuevoPreparadorSolicitudEca();">
            <input type="button" id="btnEliminarPreparadorSol" name="btnEliminarPreparadorSol" class="botonGeneral" value="<%=meLanbide35I18n.getMensaje(idiomaUsuario, "btn.general.eliminar")%>" onclick="pulsarEliminarPreparadorSolicitudEca();">
        </div> 
        <div class="lineaFormulario" style="/*margin-top: 15px;*/ width: 80%" >
            <div style="width: 85%; float: left;text-align: right;" class="etiqueta">
                <%=meLanbide35I18n.getMensaje(idiomaUsuario,"label.solicitud23.preparador.horasConvenio")%>
            </div>
            <div style="width: 15%; float: left;text-align: center;">
                <input onfocusout="calcularSumatoriaHorasTotales()" type="text" id="horasConvenio" name="horasConvenio" size="10" maxlength="10" style="text-align: center;"
                       value="" class="inputTexto"/>
            </div>
        </div>
        <div class="lineaFormulario" style="padding-top: 15px; width: 80%" >
            <div style="width: 85%; float: left;text-align: right;" class="etiqueta">
                <%=meLanbide35I18n.getMensaje(idiomaUsuario,"label.solicitud23.preparador.sumatoriaHorasTotales")%>
            </div>
            <div style="width: 15%; float: left;text-align: center;">
                <input disabled type="text" id="sumatoriaHorasTotales" name="sumatoriaHorasTotales" size="10" maxlength="10" style="text-align: center;"
                       value="<%=solicitud.getSumatoriaHorasTotales()%>" class="inputTexto"/>
            </div>
            <input hidden disabled type="text" id="horasPreparadores" name="horasPreparadores" size="10" maxlength="10" 
                   value="<%=solicitud.getSumatoriaHorasTotales()%>" class="inputTexto"/>

        </div>
        <div class="botonera" style=" padding-top: 20px;">
            <input type="button" id="btnGuardarHoras" name="btnGuardarHoras" class="botonMasLargo" value="<%=meLanbide35I18n.getMensaje(idiomaUsuario, "btn.solicitud23.preparadores.act")%>" onclick="pulsarGuardarHorasTotales();">
        </div>   
    </div>
</body>
<script type="text/javascript">
    <%
    if (solicitud != null){
    %>
    document.getElementById('horasConvenio').value = '<%=solicitud.getHorasConvenio() != null ? solicitud.getHorasConvenio().toString() : "" %>';
    document.getElementById('sumatoriaHorasTotales').value = '<%=solicitud.getSumatoriaHorasTotales() != null ? formateador.format(solicitud.getSumatoriaHorasTotales()) : "" %>';
    <%            
        }
    %>
    var tablaPreparadores;
    var listaPreparadores = new Array();
    var listaPreparadoresTabla = new Array();

    crearTablaPreparadoresSolicitudEca();

    <%
        List<FilaPreparadorECA23VO> solList = (List<FilaPreparadorECA23VO>)request.getAttribute("listaPreparadoresSolicitud");
        FilaPreparadorECA23VO act = null;
        BigDecimal sumaHoras = new BigDecimal(0);
        if (solList!= null && solList.size() >0){
            for (int i = 0; i <solList.size(); i++) {
                act = solList.get(i);
                sumaHoras = sumaHoras.add(act.getHorasECA());
    %>
    listaPreparadores[<%=i%>] = ['<%=act.getId()%>', '<%=act.getNumeroExpediente()%>', '<%=act.getNombre()%>', '<%=act.getDni()%>', '<%=act.getHorasECA()%>'];
    listaPreparadoresTabla[<%=i%>] = ['<%=act.getId()%>', '<%=act.getNumeroExpediente()%>', '<%=act.getNombre()%>', '<%=act.getDni()%>', '<%=act.getHorasECA()%>'];
    <%
            }
        }
    %>
    document.getElementById('horasPreparadores').value = '<%=sumaHoras != null ? sumaHoras.toString() : "" %>';
    tablaPreparadores.lineas = listaPreparadoresTabla;
    tablaPreparadores.displayTabla();
</script>
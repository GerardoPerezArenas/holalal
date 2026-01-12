<%@taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@taglib uri="/WEB-INF/tlds/c.tld" prefix="c"%>

<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide35.i18n.MeLanbide35I18n"%>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide35.vo.solicitud.FilaProspectorECA23VO" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide35.util.ConstantesMeLanbide35" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide35.util.MeLanbide35Utils" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.common.service.config.Config"%>
<%@page import="es.altia.common.service.config.ConfigServiceHelper"%>
<%@page import="java.util.ArrayList" %>
<%@page import="java.util.List" %>

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
%>
<jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
<jsp:setProperty name="descriptor"  property="idi_cod" value="<%=idiomaUsuario%>" />
<jsp:setProperty name="descriptor"  property="apl_cod" value="<%=apl%>" />
<script type="text/javascript">
    function pulsarNuevoProspectorSolicitudEca() {
        var control = new Date();
        var result = null;
        lanzarPopUpModal('<%=request.getContextPath()%>/PeticionModuloIntegracion.do?tarea=preparar&modulo=MELANBIDE35&operacion=cargarNuevoProspectorSolicitud23&tipo=0&numero=<%=numExpediente%>&opcion=nuevo&control=' + control.getTime(), 600, 850, 'no', 'no', function (result) {
            recargarTablaProspectoresSolicitudEca();
        });
    }

    function pulsarEliminarProspectorSolicitudEca() {
        if (tablaProspectores.selectedIndex != -1) {
            var resultado = jsp_alerta('', '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "msg.preguntaEliminar")%>');
            if (resultado == 1) {
                var ajax = getXMLHttpRequest();
                var nodos = null;
                var parametros = "";
                var control = new Date();
                parametros = 'tarea=preparar&modulo=MELANBIDE35&operacion=eliminarProspectorSolicitud23&tipo=0&numero=<%=numExpediente%>&id=' + listaProspectores[tablaProspectores.selectedIndex][0] + '&control=' + control.getTime();
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
                        recargarTablaProspectoresSolicitudEca();
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

    function crearTablaProspectoresSolicitudEca() {
        tablaProspectores = new TablaEca(true, '<%=descriptor.getDescripcion("buscar")%>', '<%=descriptor.getDescripcion("anterior")%>', '<%=descriptor.getDescripcion("siguiente")%>', '<%=descriptor.getDescripcion("mosFilasPag")%>', '<%=descriptor.getDescripcion("msgNoResultBusq")%>', '<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>', '<%=descriptor.getDescripcion("filtrDeTotal")%>', '<%=descriptor.getDescripcion("primero")%>', '<%=descriptor.getDescripcion("ultimo")%>', document.getElementById('listaProspectores'));
        tablaProspectores.addColumna('0', 'center', '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.solicitud23.prospectores.tabla.col0")%>'); // ID
        tablaProspectores.addColumna('0', 'center', '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.solicitud23.prospectores.tabla.col1")%>'); // num exp
        tablaProspectores.addColumna('250', 'left', '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.solicitud23.prospectores.tabla.col2")%>'); // nombre + ape
        tablaProspectores.addColumna('50', 'center', '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.solicitud23.prospectores.tabla.col3")%>'); // dni
        tablaProspectores.displayCabecera = true;
        tablaProspectores.height = 300;
    }


    function recargarTablaProspectoresSolicitudEca() {
        tablaProspectores;
        listaProspectores = new Array();
        listaProspectoresTabla = new Array();
        var ajax = getXMLHttpRequest();
        var fila;
        var nodos = null;
        var parametros = "";
        var control = new Date();
        parametros = 'tarea=preparar&modulo=MELANBIDE35&operacion=recargarProspectoresSolicitud23&tipo=0&numero=<%=numExpediente%>&control=' + control.getTime();
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
            var result = extraerListadoProspectoresSolicitudEca(nodos);
            crearTablaProspectoresSolicitudEca();
            for (var i = 1; i < result.length; i++) {
                fila = result[i];
                listaProspectores[i - 1] = fila;
                listaProspectoresTabla[i - 1] = fila;
            }
            tablaProspectores.lineas = listaProspectoresTabla;
            tablaProspectores.displayTabla();
        } catch (Err) {
            jsp_alerta("A", '<%=meLanbide35I18n.getMensaje(idiomaUsuario,"error.errorProcesarRespuesta")%>');
        }
    }


    function extraerListadoProspectoresSolicitudEca(nodos) {
        var elemento = nodos[0];
        var hijos = elemento.childNodes;
        var codigoOperacion = null;
        var listaProspectores = new Array();
        var fila = new Array();
        var nodoFila;
        var hijosFila;
        var nodoCampo;
        var j;
        for (j = 0; hijos != null && j < hijos.length; j++) {
            if (hijos[j].nodeName == "CODIGO_OPERACION") {
                codigoOperacion = hijos[j].childNodes[0].nodeValue;
                listaProspectores[j] = codigoOperacion;
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
                    }
                }
            }
            listaProspectores[j] = fila;
            fila = new Array();
        }
        return listaProspectores;
    }
    
    function callFromTableToEca(rowID, tableName) {    }
</script>
<body>
    <div class="tab-page" id="tabPage3512" style="height:520px; width: 98%;">
        <div  class="sub3titulo" style="width: 98%; clear: both; text-align: center; font-size: 14px;text-transform: uppercase;">
            <span id="subtituloPros"><%=meLanbide35I18n.getMensaje(idiomaUsuario,"label.solicitud.prospectores.titulo")%></span>
        </div>
        <div id="divGeneral">
            <div id="listaProspectores" style="padding-left: 10%; width:70%; text-align: center; margin:0px;margin-top:0px;" align="center"></div>
        </div> 
        <div class="botonera" style=" padding-top: 20px;">
            <input type="button" id="btnNuevoProspectorSol" name="btnNuevoProspectorSol" class="botonGeneral" value="<%=meLanbide35I18n.getMensaje(idiomaUsuario, "btn.general.nuevo")%>" onclick="pulsarNuevoProspectorSolicitudEca();">
            <input type="button" id="btnEliminarProspectorSol" name="btnEliminarProspectorSol" class="botonGeneral" value="<%=meLanbide35I18n.getMensaje(idiomaUsuario, "btn.general.eliminar")%>" onclick="pulsarEliminarProspectorSolicitudEca();">
        </div> 
    </div>
</body>
<script type="text/javascript">
    var tablaProspectores;
    var listaProspectores = new Array();
    var listaProspectoresTabla = new Array();

    crearTablaProspectoresSolicitudEca();

    <%
        List<FilaProspectorECA23VO> solList = (List<FilaProspectorECA23VO>)request.getAttribute("listaProspectoresSolicitud");
        FilaProspectorECA23VO act = null;
        
        if (solList!= null && solList.size() >0){
            for (int i = 0; i <solList.size(); i++) {
                act = solList.get(i);
    %>
    listaProspectores[<%=i%>] = ['<%=act.getId()%>', '<%=act.getNumeroExpediente()%>', '<%=act.getNombre()%>', '<%=act.getDni()%>'];
    listaProspectoresTabla[<%=i%>] = ['<%=act.getId()%>', '<%=act.getNumeroExpediente()%>', '<%=act.getNombre()%>', '<%=act.getDni()%>'];
    <%
            }
        }
    %>
    tablaProspectores.lineas = listaProspectoresTabla;
    tablaProspectores.displayTabla();
</script>
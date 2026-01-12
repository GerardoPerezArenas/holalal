<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>

<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.agora.business.util.GeneralValueObject"%>
<%@page import="es.altia.common.service.config.Config"%>
<%@page import="es.altia.common.service.config.ConfigServiceHelper"%>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide68.i18n.MeLanbide68I18n" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide68.vo.FilaTipDocLanbideVO" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide68.util.ConstantesMeLanbide68" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide68.util.ConfigurationParameter" %>
<%@page import="java.text.DateFormat" %>
<%@page import="java.text.SimpleDateFormat" %>
<%@page import="java.util.Comparator" %>
<%@page import="java.util.ArrayList" %>
<%@page import="java.util.List" %>

<html>
    <head>
        <%
            int idiomaUsuario = 0;
            int codOrganizacion = 0;
            int apl = 5;
            String css = "";
            UsuarioValueObject usuario = new UsuarioValueObject();
            try
            {
                if (session != null) 
                {
                    if (usuario != null) 
                    {
                        usuario = (UsuarioValueObject) session.getAttribute("usuario");
                        idiomaUsuario = usuario.getIdioma();
                        codOrganizacion  = usuario.getOrgCod();
                        apl = usuario.getAppCod();
                        css = usuario.getCss();
                    }
                }
            }
            catch(Exception ex)
            {

            }   
            //Clase para internacionalizar los mensajes de la aplicacion.
            MeLanbide68I18n meLanbide68I18n = MeLanbide68I18n.getInstance();
        %>     
        <jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
        <jsp:setProperty name="descriptor"  property="idi_cod" value="<%=idiomaUsuario%>" />
        <jsp:setProperty name="descriptor"  property="apl_cod" value="<%=apl%>" />
        <link rel="StyleSheet" media="screen" type="text/css" href="<%=request.getContextPath()%><%=css%>">
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/popup.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/general.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/TablaNueva.js"></script> 
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/melanbide68/JavaScriptUtil.js"></script>

        <script type="text/javascript">
            var APP_CONTEXT_PATH = '<%=request.getContextPath()%>';
        </script>         
        <script type="text/javascript">
            function crearTabla() {
                tabTipDocLanbide = new Tabla(true, '<%=descriptor.getDescripcion("buscar")%>', '<%=descriptor.getDescripcion("anterior")%>', '<%=descriptor.getDescripcion("siguiente")%>', '<%=descriptor.getDescripcion("mosFilasPag")%>', '<%=descriptor.getDescripcion("msgNoResultBusq")%>', '<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>', '<%=descriptor.getDescripcion("filtrDeTotal")%>', '<%=descriptor.getDescripcion("primero")%>', '<%=descriptor.getDescripcion("ultimo")%>', document.getElementById('listaTiposDocumentales'));
                tabTipDocLanbide.addColumna('0', 'center', "<%= meLanbide68I18n.getMensaje(idiomaUsuario,"label.TipDocLanbide.tabla.col1")%>", "<%= meLanbide68I18n.getMensaje(idiomaUsuario,"label.TipDocLanbide.tabla.col1.title")%>");
                tabTipDocLanbide.addColumna('100', 'center', "<%= meLanbide68I18n.getMensaje(idiomaUsuario,"label.TipDocLanbide.tabla.col2")%>", "<%= meLanbide68I18n.getMensaje(idiomaUsuario,"label.TipDocLanbide.tabla.col2.title")%>");
                tabTipDocLanbide.addColumna('340', 'left', "<%= meLanbide68I18n.getMensaje(idiomaUsuario,"label.TipDocLanbide.tabla.col3")%>", "<%= meLanbide68I18n.getMensaje(idiomaUsuario,"label.TipDocLanbide.tabla.col3.title")%>");
                tabTipDocLanbide.addColumna('340', 'left', "<%= meLanbide68I18n.getMensaje(idiomaUsuario,"label.TipDocLanbide.tabla.col4")%>", "<%= meLanbide68I18n.getMensaje(idiomaUsuario,"label.TipDocLanbide.tabla.col4.title")%>");
                tabTipDocLanbide.addColumna('0', 'left', "<%= meLanbide68I18n.getMensaje(idiomaUsuario,"label.TipDocLanbide.tabla.col5")%>", "<%= meLanbide68I18n.getMensaje(idiomaUsuario,"label.TipDocLanbide.tabla.col5.title")%>");
                tabTipDocLanbide.addColumna('0', 'left', "<%= meLanbide68I18n.getMensaje(idiomaUsuario,"label.TipDocLanbide.tabla.col6")%>", "<%= meLanbide68I18n.getMensaje(idiomaUsuario,"label.TipDocLanbide.tabla.col6.title")%>");
                tabTipDocLanbide.addColumna('200', 'left', "<%= meLanbide68I18n.getMensaje(idiomaUsuario,"label.TipDocLanbide.tabla.col7")%>", "<%= meLanbide68I18n.getMensaje(idiomaUsuario,"label.TipDocLanbide.tabla.col7.title")%>");
                tabTipDocLanbide.addColumna('80', 'center', "<%= meLanbide68I18n.getMensaje(idiomaUsuario,"label.TipDocLanbide.tabla.col8")%>", "<%= meLanbide68I18n.getMensaje(idiomaUsuario,"label.TipDocLanbide.tabla.col8.title")%>");
                tabTipDocLanbide.addColumna('80', 'center', "<%= meLanbide68I18n.getMensaje(idiomaUsuario,"label.TipDocLanbide.tabla.col9")%>", "<%= meLanbide68I18n.getMensaje(idiomaUsuario,"label.TipDocLanbide.tabla.col9.title")%>");
                tabTipDocLanbide.addColumna('0', 'center', "<%= meLanbide68I18n.getMensaje(idiomaUsuario,"label.TipDocLanbide.tabla.col10")%>", "<%= meLanbide68I18n.getMensaje(idiomaUsuario,"label.TipDocLanbide.tabla.col10.title")%>");

                tabTipDocLanbide.displayCabecera = true;
                tabTipDocLanbide.height = '300';
            }
        </script> 
    </head>
    <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script>
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/>
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/estilo.css"/>         
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/extension/melanbide68/melanbide68.css"/>

    <body class="bandaBody" onload="javascript:{
                    pleaseWait('off')
                }">
        <jsp:include page="/jsp/hidepage.jsp" flush="true">
            <jsp:param name='cargaDatos' value='<%=descriptor.getDescripcion("msjCargDatos")%>'/>
        </jsp:include>  
        <div class="contenidoPantalla">
            <div style="height:50px; width: 100%;">
                <table width="100%" style="height: 100%;" cellpadding="0px" cellspacing="0px" border="0px">
                    <tr>
                        <td class="txttitblanco"><%=meLanbide68I18n.getMensaje(idiomaUsuario,"label.tituloTablaTiposDoc")%> </td>
                    </tr>
                </table>
            </div>  
            <div class="tab-page" id="tabPage354" style="height:520px; width: 100%;">
                </br>
                <div style="padding: 10px;">
                    </br>
                    <div id="divGeneral" >     
                        <div id="listaTiposDocumentales" align="center"></div>
                    </div> 

                    <br/>
                    <br/>
                    <!-- botones debajo de la tabla --->
                    <div class="botonera" >
                        <input type="button" id="btnInsertarTipDoc" name="btnInsertarTipDoc" class="botonGeneral" value="<%=meLanbide68I18n.getMensaje(idiomaUsuario, "btn.insertar")%>" onclick="pulsarInsertarTipDoc(true);">
                        <input type="button" id="btnModificarTipDoc" name="btnModificarTipDoc" class="botonGeneral" value="<%=meLanbide68I18n.getMensaje(idiomaUsuario, "btn.modificar")%>" onclick="pulsarModificarTipDoc();">
                        <%--<input type="button" id="btnEliminarTipDoc" name="btnEliminarTipDoc" class="botonGeneral" value="<%=meLanbide68I18n.getMensaje(idiomaUsuario, "btn.eliminar")%>" onclick="pulsarEliminarTipDoc(true);">--%>
                        <input type="button" id="btnDeshabilitarTipDoc" name="btnDeshabilitarTipDoc" class="botonLargo" value="<%=meLanbide68I18n.getMensaje(idiomaUsuario, "btn.deshabilitar")%>" onclick="pulsarDeshabilitarTipDoc(true);"> 
                        <!-- Boton Excel --->
                        <input type="button" id="btnExportarExcel" name="btnExportarExcel" class="botonLargo" value="<%=meLanbide68I18n.getMensaje(idiomaUsuario, "btn.excel")%>" onclick="pulsarImprimir();"> 
                        <!-- Boton Procedimientos --->
                        <input type="button" id="btnProcedimientos" name="btnProcedimientos" class="botonLargo" value="<%=meLanbide68I18n.getMensaje(idiomaUsuario, "btn.procedimientos")%>" onclick="procesarProcedimientos();" disabled>   
                        <!-- Nuevo botón centrado y debajo --->
                        <div style="text-align: center; margin-top: 20px;">
                            <input type="button" id="btnNuevoLargo" name="btnNuevoLargo" class="botonExtraLargo" value="<%=meLanbide68I18n.getMensaje(idiomaUsuario, "btn.TodosProcedimientos")%>" onclick="pulsarInsertarEnTodos();">
                    </div>   
                </div>
            </div>            

            <script  type="text/javascript">
                pleaseWait('on');
                //Tabla TiposDocumentales
                var tabTipDocLanbide;
                var listaTiposDocumentales = new Array();
                var listaTiposDocumentalesTabla = new Array();

                crearTabla();
                <% 
                    FilaTipDocLanbideVO objectVO = null;
                    List<FilaTipDocLanbideVO> List = null;

                    if(request.getAttribute("listaTiposDocumentales")!=null){
                        List = (List<FilaTipDocLanbideVO>)request.getAttribute("listaTiposDocumentales");
                    }	

                    if (List!= null && List.size() >0){             

                        for (int indice=0;indice<List.size();indice++)
                        {
                            objectVO = List.get(indice);                
                %>

                listaTiposDocumentales[<%=indice%>] = ['<%=objectVO.getId()%>', '<%=objectVO.getTipDocID()%>', '<%=objectVO.getTipDocLanbide_es()%>', '<%=objectVO.getTipDocLanbide_eu()%>',
                    '<%=objectVO.getTipDocLanbide_es_L() != null ? objectVO.getTipDocLanbide_es_L().replaceAll("\\n", " ") : "-"%>',
                    '<%=objectVO.getTipDocLanbide_eu_L() != null ? objectVO.getTipDocLanbide_eu_L().replaceAll("\\n", " ") : "-"%>',
                    '<%=objectVO.getTipDocDokusi()%>', '<%=objectVO.getTieneMetadato()%>', '<%=objectVO.getDeshabilitado()%>',
                    '<%=objectVO.getCodGrupo() != null ? objectVO.getCodGrupo() : "-"%>'];
                listaTiposDocumentalesTabla[<%=indice%>] = ['<%=objectVO.getId()%>', '<%=objectVO.getTipDocID()%>', '<%=objectVO.getTipDocLanbide_es()%>', '<%=objectVO.getTipDocLanbide_eu()%>',
                    '<%=objectVO.getTipDocLanbide_es_L() != null ? objectVO.getTipDocLanbide_es_L().replaceAll("\\n", " ") : "-"%>',
                    '<%=objectVO.getTipDocLanbide_eu_L() != null ? objectVO.getTipDocLanbide_eu_L().replaceAll("\\n", " ") : "-"%>',
                    '<%=objectVO.getTipDocDokusi()%>', '<%=objectVO.getTieneMetadato()%>', '<%=objectVO.getDeshabilitado()%>',
                    '<%=objectVO.getCodGrupo() != null ? objectVO.getCodGrupo() : "-"%>'];
                <%
                        }// for
                    }// if
                %>

                tabTipDocLanbide.lineas = listaTiposDocumentales;
                tabTipDocLanbide.displayTablaConTooltips(listaTiposDocumentalesTabla);
                if (navigator.appName.indexOf("Internet Explorer") != -1) {
                    try {
                        var div = document.getElementById('listaTiposDocumentales');
                        div.children[0].children[0].children[0].children[1].style.width = '100%';
                        div.children[0].children[1].style.width = '100%';
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

                //Función para eliminar un registro
                function pulsarEliminarTipDoc() {
                    var fila;

                    if (tabTipDocLanbide.selectedIndex != -1) {
                        fila = tabTipDocLanbide.selectedIndex;
                        var resultado = jsp_alerta('', '<%=meLanbide68I18n.getMensaje(idiomaUsuario, "msg.preguntaEliminar")%>');
                        if (resultado == 1) {
                            var ajax = getXMLHttpRequest();
                            var nodos = null;
                            var CONTEXT_PATH = '<%=request.getContextPath()%>';
                            var url = CONTEXT_PATH + "/PeticionModuloIntegracion.do";
                            var parametros = "";
                            var control = new Date();
                            var listaTipDoc = new Array();
                            parametros = 'tarea=preparar&modulo=MELANBIDE68&operacion=cargarEliminarTipDocLanbide&tipo=0'
                                    + '&id=' + tabTipDocLanbide.lineas[fila][0]
                                    + '&control=' + control.getTime();
                            try {
                                ajax.open("POST", url, false);
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
                                // que pasaría con el código de respuesta ?????
                                nodos = xmlDoc.getElementsByTagName("RESPUESTA");
                                listaTipDoc = extraerListadoTipDoc(nodos);
                                //recargarTablalistaTipDoc(listaTipDoc);
                                var codigoOperacion = listaTipDoc[0];
                                if (codigoOperacion == "0") {
                                    recargarTablalistaTipDoc(listaTipDoc);
                                    jsp_alerta("A", '<%=meLanbide68I18n.getMensaje(idiomaUsuario,"msg.registroEliminadoOK")%>');
                                } else if (codigoOperacion == "1") {
                                    jsp_alerta("A", '<%=meLanbide68I18n.getMensaje(idiomaUsuario,"error.errorEliminarBD")%>');
                                } else if (codigoOperacion == "2") {
                                    jsp_alerta("A", '<%=meLanbide68I18n.getMensaje(idiomaUsuario,"error.errorEliminarGen")%>');
                                } else if (codigoOperacion == "3") {
                                    jsp_alerta("A", '<%=meLanbide68I18n.getMensaje(idiomaUsuario,"error.pasoParametrosEliminar")%>');
                                } else if (codigoOperacion == "4") {
                                    jsp_alerta("A", '<%=meLanbide68I18n.getMensaje(idiomaUsuario,"error.noesposibleEliminar")%>');
                                } else {
                                    jsp_alerta("A", '<%=meLanbide68I18n.getMensaje(idiomaUsuario,"error.errorEliminarGen")%>');
                                }//if(
                            } catch (Err) {
                                jsp_alerta("A", '<%=meLanbide68I18n.getMensaje(idiomaUsuario,"error.errorProcesarRespuesta")%>');
                            }//try-catch
                        }
                    } else {
                        jsp_alerta('A', '<%=meLanbide68I18n.getMensaje(idiomaUsuario, "msg.msjNoSelecFila")%>');
                    }
                }

                function extraerListadoTipDoc(nodos) {
                    var elemento = nodos[0];
                    var hijos = elemento.childNodes;
                    var codigoOperacion = null;
                    var listaTipDoc = new Array();
                    var fila = new Array();
                    var nodoFila;
                    var hijosFila;
                    var nodoCampo;
                    var j;

                    for (j = 0; hijos != null && j < hijos.length; j++) {
                        if (hijos[j].nodeName == "CODIGO_OPERACION") {
                            codigoOperacion = hijos[j].childNodes[0].nodeValue;
                            listaTipDoc[j] = codigoOperacion;
                        } else if (hijos[j].nodeName == "FILA") {
                            nodoFila = hijos[j];
                            hijosFila = nodoFila.childNodes;
                            for (var cont = 0; cont < hijosFila.length; cont++) {
                                if (hijosFila[cont].nodeName == "ID") {
                                    nodoCampo = hijosFila[cont];
                                    if (nodoCampo.childNodes.length > 0) {
                                        fila[0] = nodoCampo.childNodes[0].nodeValue;
                                    } else {
                                        fila[0] = '-';
                                    }
                                } else if (hijosFila[cont].nodeName == "COD_TIPDOC") {
                                    nodoCampo = hijosFila[cont];
                                    if (nodoCampo.childNodes.length > 0) {
                                        fila[1] = nodoCampo.childNodes[0].nodeValue;
                                    } else {
                                        fila[1] = '-';
                                    }
                                } else if (hijosFila[cont].nodeName == "TIPDOC_ES") {
                                    nodoCampo = hijosFila[cont];
                                    if (nodoCampo.childNodes.length > 0) {
                                        fila[2] = nodoCampo.childNodes[0].nodeValue;
                                    } else {
                                        fila[2] = '-';
                                    }
                                } else if (hijosFila[cont].nodeName == "TIPDOC_EU") {
                                    nodoCampo = hijosFila[cont];
                                    if (nodoCampo.childNodes.length > 0) {
                                        fila[3] = nodoCampo.childNodes[0].nodeValue;
                                    } else {
                                        fila[3] = '-';
                                    }
                                } else if (hijosFila[cont].nodeName == "TIPDOC_ES_L") {
                                    nodoCampo = hijosFila[cont];
                                    if (nodoCampo.childNodes.length > 0) {
                                        fila[4] = nodoCampo.childNodes[0].nodeValue;
                                    } else {
                                        fila[4] = '-';
                                    }
                                } else if (hijosFila[cont].nodeName == "TIPDOC_EU_L") {
                                    nodoCampo = hijosFila[cont];
                                    if (nodoCampo.childNodes.length > 0) {
                                        fila[5] = nodoCampo.childNodes[0].nodeValue;
                                    } else {
                                        fila[5] = '-';
                                    }
                                } else if (hijosFila[cont].nodeName == "TIPDOC_DOKUSI") {
                                    nodoCampo = hijosFila[cont];
                                    if (nodoCampo.childNodes.length > 0) {
                                        fila[6] = nodoCampo.childNodes[0].nodeValue;
                                    } else {
                                        fila[6] = '-';
                                    }
                                } else if (hijosFila[cont].nodeName == "TIENE_METADATO") {
                                    nodoCampo = hijosFila[cont];
                                    if (nodoCampo.childNodes.length > 0) {
                                        fila[7] = nodoCampo.childNodes[0].nodeValue;
                                    } else {
                                        fila[7] = '-';
                                    }
                                } else if (hijosFila[cont].nodeName == "DESHABILITADO") {
                                    nodoCampo = hijosFila[cont];
                                    if (nodoCampo.childNodes.length > 0) {
                                        fila[8] = nodoCampo.childNodes[0].nodeValue;
                                    } else {
                                        fila[8] = '-';
                                    }
                                } else if (hijosFila[cont].nodeName == "GRUPO") {
                                    nodoCampo = hijosFila[cont];
                                    if (nodoCampo.childNodes.length > 0) {
                                        fila[9] = nodoCampo.childNodes[0].nodeValue;
                                    } else {
                                        fila[9] = '-';
                                    }
                                }
                            }
                            listaTipDoc[j] = fila;
                            fila = new Array();
                        }
                    }
                    return listaTipDoc;
                }

                function recargarTablalistaTipDoc(result) {
                    var fila;
                    //Tabla TiposDocumentales
                    var listaTiposDocumentales = new Array();
                    var listaTiposDocumentalesTabla = new Array();

                    for (var i = 1; i < result.length; i++) {
                        fila = result[i];

                        listaTiposDocumentales[i - 1] = [fila[0], fila[1], fila[2], fila[3], fila[4], fila[5], fila[6], fila[7], fila[8], fila[9]];
                        listaTiposDocumentalesTabla[i - 1] = [fila[0], fila[1], fila[2], fila[3], fila[4], fila[5], fila[6], fila[7], fila[8], fila[9]];
                    }

                    crearTabla();

                    tabTipDocLanbide.lineas = listaTiposDocumentales;
                    tabTipDocLanbide.displayTablaConTooltips(listaTiposDocumentalesTabla);
                    if (navigator.appName.indexOf("Internet Explorer") != -1) {
                        try {
                            var div = document.getElementById('listaTiposDocumentales');
                            div.children[0].children[0].children[0].children[1].style.width = '100%';
                            div.children[0].children[1].style.width = '100%';
                        } catch (err) {
                        }
                    }
                }

                function pulsarInsertarTipDoc() {
                    var control = new Date();
                    var result = null;

                    if (navigator.appName.indexOf("Internet Explorer") != -1) {
                        lanzarPopUpModal('<%=request.getContextPath()%>/PeticionModuloIntegracion.do?tarea=preparar&modulo=MELANBIDE68&operacion=cargarNuevoTipDocLanbide&tipo=0'
                                    + '&control=' + control.getTime(), 560, 1020, 'no', 'no', function (result) {
                            if (result != undefined) {
                                if (result[0] == '0') {
                                    recargarTablalistaTipDoc(result);
                                }
                            }
                        });
                    } else {
                        lanzarPopUpModal('<%=request.getContextPath()%>/PeticionModuloIntegracion.do?tarea=preparar&modulo=MELANBIDE68&operacion=cargarNuevoTipDocLanbide&tipo=0'
                                    + '&control=' + control.getTime(), 560, 1020, 'no', 'no', function (result) {
                            if (result != undefined) {
                                if (result[0] == '0') {
                                    recargarTablalistaTipDoc(result);
                                }
                            }
                        });
                    }
                }

                function pulsarModificarTipDoc() {
                    var fila;

                    if (tabTipDocLanbide.selectedIndex != -1) {
                        fila = tabTipDocLanbide.selectedIndex;
                        var control = new Date();

                        if (navigator.appName.indexOf("Internet Explorer") != -1) {
                            lanzarPopUpModal('<%=request.getContextPath()%>/PeticionModuloIntegracion.do?tarea=preparar&modulo=MELANBIDE68&operacion=cargarModificarTipDoc&tipo=0'
                                    + '&id=' + tabTipDocLanbide.lineas[fila][0]
                                    //+'&codTipDoc='+tabTipDocLanbide.lineas[fila][1]
                                    //+'&tipDocCas='+tabTipDocLanbide.lineas[fila][2]
                                    //+'&tipDocEus='+tabTipDocLanbide.lineas[fila][3]
                                    //+'&tipDocCasL='+tabTipDocLanbide.lineas[fila][4]
                                    //+'&tipDocEusL='+tabTipDocLanbide.lineas[fila][5]
                                    + '&tipDocDokusi=' + tabTipDocLanbide.lineas[fila][6]
                                        + '&control=' + control.getTime(), 560, 1020, 'no', 'no', function (result) {
                                if (result != undefined) {
                                    // aquí también tengo que ver la forma de recuperar el CodigoOperacion porque si no me va a dar error
                                    if (result[0] == '0') {
                                        recargarTablalistaTipDoc(result);
                                    }
                                }
                            });
                        } else {
                            lanzarPopUpModal('<%=request.getContextPath()%>/PeticionModuloIntegracion.do?tarea=preparar&modulo=MELANBIDE68&operacion=cargarModificarTipDoc&tipo=0'
                                    + '&id=' + tabTipDocLanbide.lineas[fila][0]
                                    //+'&codTipDoc='+tabTipDocLanbide.lineas[fila][1]
                                    //+'&tipDocCas='+tabTipDocLanbide.lineas[fila][2]
                                    //+'&tipDocEus='+tabTipDocLanbide.lineas[fila][3]
                                    //+'&tipDocCasL='+tabTipDocLanbide.lineas[fila][4]
                                    //+'&tipDocEusL='+tabTipDocLanbide.lineas[fila][5]
                                    + '&tipDocDokusi=' + tabTipDocLanbide.lineas[fila][6]
                                        + '&control=' + control.getTime(), 560, 1020, 'no', 'no', function (result) {
                                if (result != undefined) {
                                    // aquí también tengo que ver la forma de recuperar el CodigoOperacion porque si no me va a dar error
                                    if (result[0] == '0') {
                                        recargarTablalistaTipDoc(result);
                                    }
                                }
                            });
                        }

                    } else {
                        jsp_alerta('A', '<%=meLanbide68I18n.getMensaje(idiomaUsuario, "msg.msjNoSelecFila")%>');
                    }
                }

                function pulsarDeshabilitarTipDoc() {
                    var fila;

                    if (tabTipDocLanbide.selectedIndex != -1) {
                        fila = tabTipDocLanbide.selectedIndex;
                        if (tabTipDocLanbide.lineas[fila][5] == 'N') {
                            var resultado = jsp_alerta('', '<%=meLanbide68I18n.getMensaje(idiomaUsuario, "msg.preguntaDeshabilitar")%>');
                        } else {
                            var resultado = jsp_alerta('', '<%=meLanbide68I18n.getMensaje(idiomaUsuario, "msg.preguntaHabilitar")%>');
                        }
                        if (resultado == 1) {
                            var ajax = getXMLHttpRequest();
                            var nodos = null;
                            var CONTEXT_PATH = '<%=request.getContextPath()%>';
                            var url = CONTEXT_PATH + "/PeticionModuloIntegracion.do";
                            var parametros = "";
                            var control = new Date();
                            var listaTipDoc = new Array();
                            parametros = 'tarea=preparar&modulo=MELANBIDE68&operacion=cargarDeshabilitarTipDocLanbide&tipo=0'
                                    + '&id=' + tabTipDocLanbide.lineas[fila][0]
                                    + '&control=' + control.getTime();
                            try {
                                ajax.open("POST", url, false);
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
                                // que pasaría con el código de respuesta ?????
                                nodos = xmlDoc.getElementsByTagName("RESPUESTA");
                                listaTipDoc = extraerListadoTipDoc(nodos);
                                //recargarTablalistaTipDoc(listaTipDoc);
                                var codigoOperacion = listaTipDoc[0];
                                if (codigoOperacion == "0") {
                                    recargarTablalistaTipDoc(listaTipDoc);
                                    jsp_alerta("A", '<%=meLanbide68I18n.getMensaje(idiomaUsuario,"msg.registroDeshabilitadoOK")%>');
                                } else if (codigoOperacion == "1") {
                                    jsp_alerta("A", '<%=meLanbide68I18n.getMensaje(idiomaUsuario,"error.errorProcesarRespuesta")%>');
                                } else if (codigoOperacion == "2") {
                                    jsp_alerta("A", '<%=meLanbide68I18n.getMensaje(idiomaUsuario,"error.errorModificarGen")%>');
                                } else if (codigoOperacion == "3") {
                                    jsp_alerta("A", '<%=meLanbide68I18n.getMensaje(idiomaUsuario,"error.pasoParametrosDeshabilitar")%>');
                                } else {
                                    jsp_alerta("A", '<%=meLanbide68I18n.getMensaje(idiomaUsuario,"error.errorModificarGen")%>');
                                }//if(
                            } catch (Err) {
                                jsp_alerta("A", '<%=meLanbide68I18n.getMensaje(idiomaUsuario,"error.errorProcesarRespuesta")%>');
                            }//try-catch
                        }
                    } else {
                        jsp_alerta('A', '<%=meLanbide68I18n.getMensaje(idiomaUsuario, "msg.msjNoSelecFila")%>');
                    }
                }
                function pulsarImprimir() {
                    var CONTEXT_PATH = '<%=request.getContextPath()%>';
                    var url = CONTEXT_PATH + "/PeticionModuloIntegracion.do";
                    var parametros = 'tarea=preparar&modulo=MELANBIDE68&operacion=getExcelTiposDocumentales&tipo=0';
                    url = url + "?" + parametros
                    
                    window.location.href = url;
                }
                    function pulsarInsertarEnTodos() {
                        // Confirmación antes de realizar la acciónif (tabTipDocLanbide.selectedIndex != -1) {
                        var fila;

                        if (tabTipDocLanbide.selectedIndex != -1) {
                            fila = tabTipDocLanbide.selectedIndex;
                            var control = new Date();

                            // Mensaje de confirmación en doble idioma
                            var mensajeConfirmacion = '<%=meLanbide68I18n.getMensaje(idiomaUsuario,"msg.confirmacionInsertarTodos")%>';
                            if (!confirm(mensajeConfirmacion)) {
                                return;
                            }
                            var idTipoDocumental = tabTipDocLanbide.lineas[fila][1];
                            var CONTEXT_PATH = '<%=request.getContextPath()%>';
                            var url = CONTEXT_PATH + "/PeticionModuloIntegracion.do";
                            var parametros = 'tarea=preparar&modulo=MELANBIDE68&operacion=guardarInsertarEnTodos&tipo=0'
                                    + '&idTipoDocumental=' + idTipoDocumental;
                            var ajax = getXMLHttpRequest();
                            var nodos = null;

                            try {
                                ajax.open("POST", url, false);
                                //ajax.setRequestHeader("Content-Type", "application/x-www-form-urlencoded; charset=iso-8859-15");
                                ajax.setRequestHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
                                ajax.setRequestHeader("Accept", "text/xml, application/xml, text/plain");
                                ajax.send(parametros);
                                if (ajax.readyState == 4 && ajax.status == 200) {
                                    var xmlDoc = null;

                                    // En el resto de navegadores el XML se recupera de la propiedad responseXML
                                    xmlDoc = ajax.responseXML;

                                }//if (ajax.readyState==4 && ajax.status==200)
                                nodos = xmlDoc.getElementsByTagName("RESPUESTA");
                                var elemento = nodos[0];
                                var hijos = elemento.childNodes;
                                var codigoOperacion = null;
                                var listaTipDoc = new Array();
                                var mensajeTotal = "";
                                var nodoFila;
                                var hijosFila;
                                var nodoCampo;
                                var j;

                                for (j = 0; hijos != null && j < hijos.length; j++) {
                                    if (hijos[j].nodeName == "CODIGO_OPERACION") {
                                        codigoOperacion = hijos[j].childNodes[0].nodeValue;
                                        listaTipDoc[j] = codigoOperacion;
                                    } else if (hijos[j].nodeName == "FILA") {
                                        nodoFila = hijos[j];
                                        hijosFila = nodoFila.childNodes;
                                        for (var cont = 0; cont < hijosFila.length; cont++) {
                                            if (hijosFila[cont].nodeName == "MENS") {
                                                mensajeTotal += hijosFila[cont].childNodes[0].nodeValue + "\n";
                                            }
                                        }
                                    }
                                }
                                if (codigoOperacion == "0") {
                    <%--jsp_alerta("A", '<%=meLanbide68I18n.getMensaje(idiomaUsuario,"msg.tipDocModificadoOK")%>');--%>
                                    mostrarMensajeEnVentana(mensajeTotal);
                                } else if (codigoOperacion == "1") {
                                    jsp_alerta("A", '<%=meLanbide68I18n.getMensaje(idiomaUsuario,"error.errorBD")%>');
                                } else if (codigoOperacion == "2") {
                                    jsp_alerta("A", '<%=meLanbide68I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                                } else if (codigoOperacion == "3") {
                                    jsp_alerta("A", '<%=meLanbide68I18n.getMensaje(idiomaUsuario,"error.pasoParametros")%>');
                                }  //if (
                            } catch (Err) {

                                jsp_alerta("A", '<%=meLanbide68I18n.getMensaje(idiomaUsuario,"error.errorProcesarRespuesta")%>');
                            }
                        } else {
                            jsp_alerta('A', '<%=meLanbide68I18n.getMensaje(idiomaUsuario, "msg.msjNoSelecFila")%>');
                        }
                    }
                    function mostrarMensajeEnVentana(mensajeTotal) {
                        var nuevaVentana = window.open("", "_blank", "width=800,height=600,scrollbars=yes,resizable=yes");

                        nuevaVentana.document.write("<html><head><title>Detalles de la Inserción</title>");
                        nuevaVentana.document.write("<style>");
                        nuevaVentana.document.write("body { font-family: Arial, sans-serif; padding: 20px; }");
                        nuevaVentana.document.write("table { width: 100%; border-collapse: collapse; margin-top: 20px; }");
                        nuevaVentana.document.write("th, td { border: 1px solid black; padding: 8px; text-align: center; }");
                        nuevaVentana.document.write("th { background-color: #f2f2f2; }");
                        nuevaVentana.document.write(".no-insertado { color: red; font-weight: bold; }");
                        nuevaVentana.document.write(".insertado { color: green; font-weight: bold; }");
                        nuevaVentana.document.write("</style>");
                        nuevaVentana.document.write("</head><body>");

                        nuevaVentana.document.write("<h2>Detalles de la Inserción</h2>");
                        nuevaVentana.document.write("<table>");
                        nuevaVentana.document.write("<tr><th>Procedimiento</th><th>Insertado</th><th>Detalles</th></tr>");

                        // Dividir el mensaje en líneas y recorrerlas
                        var lineas = mensajeTotal.split("\n");
                        var yaProcesados = new Set(); // Para evitar duplicados

                        for (var i = 0; i < lineas.length; i++) {
                            if (lineas[i].trim() !== "") { // Evitar líneas vacías

                                // Extraer el código del procedimiento
                                var match = lineas[i].match(/proc (\w+)/i); // Busca "proc XYZ123"
                                var procedimiento = match ? match[1] : "Desconocido"; // Extrae solo el código

                                // Evitar duplicados
                                if (yaProcesados.has(procedimiento)) {
                                    continue; // Si ya se procesó, saltarlo
                                }
                                yaProcesados.add(procedimiento);

                                // Log para depuración
                                console.log("Línea actual:", lineas[i]);
                                console.log("Procedimiento detectado:", procedimiento);

                                // Determinar si fue insertado correctamente
                                var insertado = lineas[i].includes("KO") ? "<span class='no-insertado'>No</span>" : "<span class='insertado'>Sí</span>";

                                // Detalles solo si NO se ha insertado
                                var detalles = lineas[i].includes("KO") ? "Consulte al administrador" : "";

                                nuevaVentana.document.write("<tr>");
                                nuevaVentana.document.write("<td>" + procedimiento + "</td>"); // Solo muestra el procedimiento
                                nuevaVentana.document.write("<td>" + insertado + "</td>"); // "Sí" en verde o "No" en rojo
                                nuevaVentana.document.write("<td>" + detalles + "</td>"); // Muestra detalles si es necesario
                                nuevaVentana.document.write("</tr>");
                            }
                        }

                        nuevaVentana.document.write("</table>");
                        nuevaVentana.document.write("</body></html>");
                        nuevaVentana.document.close();
                    }

                function procesarProcedimientos() {
                    //var rowActive = $('table > tbody > tr.activa');
                    var fila;
                    if (tabTipDocLanbide.selectedIndex != -1) {
                        fila = tabTipDocLanbide.selectedIndex;

                        var idTipoDocumental = tabTipDocLanbide.lineas[fila][1];
                        var CONTEXT_PATH = '<%=request.getContextPath()%>';
                        var url = CONTEXT_PATH + "/PeticionModuloIntegracion.do";
                        var parametros = 'tarea=preparar&modulo=MELANBIDE68&operacion=getProcedimientosByTipoDocumental&tipo=0'
                                + '&idTipoDocumental=' + idTipoDocumental;
                        url = url + "?" + parametros;

                        lanzarPopUpModal(url, 300, 1030, 'no', 'no', function (result) {
                            if (result != undefined) {
                                if (result[0] == '0') {
                                    recargarTablalistaTipDoc(result);
                                }
                            }
                        });
                    } else {
                        jsp_alerta('A', '<%=meLanbide68I18n.getMensaje(idiomaUsuario, "msg.msjNoSelecFila")%>');
                    }
                }

                //Activar botón de Procedimientos
                $('.xTabla tbody').click(function () {
                    $("#btnProcedimientos").prop('disabled', false);
                });
            </script>                          
        </div>
    </body>
</html>
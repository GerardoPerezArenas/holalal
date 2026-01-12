<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide32.i18n.MeLanbide32I18n" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide32.vo.comun.EntidadVO" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide32.vo.centroempleo.FilaUbicCentroEmpleoVO" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide32.vo.centroempleo.FilaTrayectoriaCentroEmpleoVO" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide32.vo.comun.SelectItem" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide32.util.ConfigurationParameter" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide32.util.ConstantesMeLanbide32" %>
<%@page import="java.util.ArrayList" %>
<%@page import="java.util.List" %>
<%@page import="org.apache.commons.lang.StringEscapeUtils"%>

<%
    String sIdioma = request.getParameter("idioma");
    int idiomaUsuario = 1;
    idiomaUsuario = sIdioma!=null?Integer.parseInt(sIdioma):1;
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
         apl = usuario.getAppCod();
         css = usuario.getCss();
            }
        }
    }
    catch(Exception ex)
    {
        
    }
    //Clase para internacionalizar los mensajes de la aplicación.
    MeLanbide32I18n meLanbide32I18n = MeLanbide32I18n.getInstance();
    String codOrganizacion  = request.getParameter("codOrganizacionModulo");
    String numExpediente    = request.getParameter("numero");
    Integer ejercicio = (numExpediente != null && numExpediente!="" ? Integer.valueOf(numExpediente.substring(0, 4)) : 0); 
    String[] datos = numExpediente.split(ConstantesMeLanbide32.BARRA_SEPARADORA);
    String procedimiento = datos[1];
    EntidadVO entidad = (EntidadVO)request.getAttribute("entidad");

    String urlOtrosDatosCE = (String)request.getAttribute("urlOtrosDatosCE");
    
    String userAgent = request.getHeader("user-agent");
    String margenIzqPestanasDatosColec = "";

    if(userAgent.indexOf("MSIE")!=-1) {
        // Internet Explorer
        margenIzqPestanasDatosColec = "";
    }else{
        // Firefox u otro navegador
        margenIzqPestanasDatosColec = "margin-left:10px;";
    }
    
    String urlPestanaDatos_ambitosSolicitados = (String)request.getAttribute("urlPestanaDatos_ambitosSolicitados");

%>

<%!
// Funcion para escapar strings para java en JSP
    private String escape(String str) 
    {
        return StringEscapeUtils.escapeJavaScript(str);
    }
%>
<jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
<jsp:setProperty name="descriptor"  property="idi_cod" value="<%=idiomaUsuario%>" />
<jsp:setProperty name="descriptor"  property="apl_cod" value="<%=apl%>" />
<link rel="StyleSheet" media="screen" type="text/css" href="<%=request.getContextPath()%><%=css%>">
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/extension/melanbide32/melanbide32.css"/>

<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/popup.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/melanbide32/gestionM32_CEMP_Comun.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/melanbide32/gestionCriteriosSeleccionCE.js"></script>


<script type="text/javascript">
    var tp32;
    var anio = <%=ejercicio%>;
    var anchoTabla = 1205;
    if (anio < 2018) {
        anchoTabla = 1035;
    }
    var mensajeValidacion = "";

    function pulsarAltaUbicacionCE() {
        var control = new Date();
        var result = null;
        lanzarPopUpModal('<%=request.getContextPath()%>/PeticionModuloIntegracion.do?tarea=preparar&modulo=MELANBIDE32&operacion=cargarNuevaUbicacionCE&tipo=0&numero=<%=numExpediente%>&control=' + control.getTime(), 450, 1000, 'si', 'no', function (result) {
            if (result != undefined) {
                if (result[0] == '0') {
                    recargarTablaUbicacionesCE(result);
                }
            }
        });
    }

    function pulsarModificarUbicacionCE() {
        if (tabUbicacionesCE.selectedIndex != -1) {
            var control = new Date();
            lanzarPopUpModal('<%=request.getContextPath()%>/PeticionModuloIntegracion.do?tarea=preparar&modulo=MELANBIDE32&operacion=cargarModifUbicacionCE&tipo=0&numero=<%=numExpediente%>&idUbic=' + listaUbicacionesCE[tabUbicacionesCE.selectedIndex][0] + '&control=' + control.getTime(), 450, 1000, 'si', 'no', function (result) {
                if (result != undefined) {
                    if (result[0] == '0') {
                        recargarTablaUbicacionesCE(result);
                    }
                }
            });
        } else {
            jsp_alerta('A', '<%=meLanbide32I18n.getMensaje(idiomaUsuario, "msg.msjNoSelecFila")%>');
        }
    }

    function pulsarEliminarUbicacionCE() {

        if (tabUbicacionesCE.selectedIndex != -1) {
            var resultado = jsp_alerta('', '<%=meLanbide32I18n.getMensaje(idiomaUsuario, "msg.preguntaEliminar")%>');
            if (resultado == 1) {

                var ajax = getXMLHttpRequest();
                var nodos = null;
                var CONTEXT_PATH = '<%=request.getContextPath()%>'
                var url = CONTEXT_PATH + "/PeticionModuloIntegracion.do";
                var parametros = "";
                var control = new Date();
                parametros = 'tarea=preparar&modulo=MELANBIDE32&operacion=eliminarUbicacionCE&tipo=0&numero=<%=numExpediente%>&idUbic=' + listaUbicacionesCE[tabUbicacionesCE.selectedIndex][0] + '&control=' + control.getTime();
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
                    var listaUbicacionesNueva = new Array();
                    var fila = new Array();
                    var nodoFila;
                    var hijosFila;
                    for (j = 0; hijos != null && j < hijos.length; j++) {
                        if (hijos[j].nodeName == "CODIGO_OPERACION") {
                            codigoOperacion = hijos[j].childNodes[0].nodeValue;
                            listaUbicacionesNueva[j] = codigoOperacion;
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
                                } else if (hijosFila[cont].nodeName == "ESPECIAL") {
                                    if (hijosFila[cont].childNodes.length > 0) {
                                        fila[1] = hijosFila[cont].childNodes[0].nodeValue;
                                    } else {
                                        fila[1] = '-';
                                    }
                                } else if (hijosFila[cont].nodeName == "PROVINCIA") {
                                    if (hijosFila[cont].childNodes.length > 0) {
                                        fila[2] = hijosFila[cont].childNodes[0].nodeValue;
                                    } else {
                                        fila[2] = '-';
                                    }
                                } else if (hijosFila[cont].nodeName == "AMBITO") {
                                    if (hijosFila[cont].childNodes.length > 0) {
                                        fila[3] = hijosFila[cont].childNodes[0].nodeValue;
                                    } else {
                                        fila[3] = '-';
                                    }
                                } else if (hijosFila[cont].nodeName == "MUNICIPIO") {
                                    if (hijosFila[cont].childNodes.length > 0) {
                                        fila[4] = hijosFila[cont].childNodes[0].nodeValue;
                                    } else {
                                        fila[4] = '-';
                                    }
                                } else if (hijosFila[cont].nodeName == "DIRECCION") {
                                    if (hijosFila[cont].childNodes.length > 0) {
                                        fila[5] = hijosFila[cont].childNodes[0].nodeValue;
                                    } else {
                                        fila[5] = '-';
                                    }
                                } else if (hijosFila[cont].nodeName == "ADJUDICADA") {
                                    if (hijosFila[cont].childNodes.length > 0) {
                                        fila[6] = hijosFila[cont].childNodes[0].nodeValue;
                                    } else {
                                        fila[6] = '-';
                                    }
                                } else if (hijosFila[cont].nodeName == "VALIDACION") {
                                    if (hijosFila[cont].childNodes.length > 0) {
                                        fila[7] = hijosFila[cont].childNodes[0].nodeValue;
                                    } else {
                                        fila[7] = '-';
                                    }
                                } else if (hijosFila[cont].nodeName == "CODIGOPOSTAL") {
                                    if (hijosFila[cont].childNodes.length > 0) {
                                        fila[8] = hijosFila[cont].childNodes[0].nodeValue;
                                    } else {
                                        fila[8] = '-';
                                    }
                                } else if (hijosFila[cont].nodeName == "HORARIOATENCION") {
                                    if (hijosFila[cont].childNodes.length > 0) {
                                        fila[9] = hijosFila[cont].childNodes[0].nodeValue;
                                    } else {
                                        fila[9] = '-';
                                    }
                                } else if (hijosFila[cont].nodeName == "APROBADO") {
                                    if (hijosFila[cont].childNodes.length > 0) {
                                        fila[10] = hijosFila[cont].childNodes[0].nodeValue;
                                    } else {
                                        fila[10] = '-';
                                    }
                                } else if (hijosFila[cont].nodeName == "MANTENIDO") {
                                    if (hijosFila[cont].childNodes.length > 0) {
                                        fila[11] = hijosFila[cont].childNodes[0].nodeValue;
                                    } else {
                                        fila[11] = '-';
                                    }
                                } else if (hijosFila[cont].nodeName == "PUNTUACIONCE") {
                                    if (hijosFila[cont].childNodes.length > 0) {
                                        fila[12] = hijosFila[cont].childNodes[0].nodeValue;
                                    } else {
                                        fila[12] = '-';
                                    }
                                } else if (hijosFila[cont].nodeName == "LOCAL_NUEVO_VALIDADO") {
                                    if (hijosFila[cont].childNodes.length > 0) {
                                        fila[13] = hijosFila[cont].childNodes[0].nodeValue;
                                    } else {
                                        fila[13] = '-';
                                    }
                                }
                            }
                            listaUbicacionesNueva[j] = fila;
                            fila = new Array();
                        }
                    }//for(j=0;hijos!=null && j<hijos.length;j++)
                    if (codigoOperacion == "0") {
                        recargarTablaUbicacionesCE(listaUbicacionesNueva);
                    } else if (codigoOperacion == "1") {
                        jsp_alerta("A", '<%=meLanbide32I18n.getMensaje(idiomaUsuario,"error.errorBD")%>');
                    } else if (codigoOperacion == "2") {
                        jsp_alerta("A", '<%=meLanbide32I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                    } else if (codigoOperacion == "3") {
                        jsp_alerta("A", '<%=meLanbide32I18n.getMensaje(idiomaUsuario,"error.pasoParametros")%>');
                    } else {
                        jsp_alerta("A", '<%=meLanbide32I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                    }//if(
                } catch (Err) {
                    jsp_alerta("A", '<%=meLanbide32I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                }//try-catch
            }
        } else {
            jsp_alerta('A', '<%=meLanbide32I18n.getMensaje(idiomaUsuario, "msg.msjNoSelecFila")%>');
        }
    }

    function pulsarAltaTrayectoriaCE() {
        var control = new Date();
        var result = null;
        lanzarPopUpModal('<%=request.getContextPath()%>/PeticionModuloIntegracion.do?tarea=preparar&modulo=MELANBIDE32&operacion=cargarNuevaTrayectoriaCE&tipo=0&numero=<%=numExpediente%>&control=' + control.getTime(), 240, 800, 'no', 'no', function (result) {
            if (result != undefined) {
                if (result[0] == '0') {
                    recargarTablaTrayectoriaCE(result);
                }
            }
        });
        if (result != undefined) {
            if (result[0] == '0') {
                recargarTablaTrayectoriaCE(result);
            }
        }
    }

    function pulsarModificarTrayectoriaCE() {
        if (tabTrayectoriaCE.selectedIndex != -1) {
            var control = new Date();
            var result = null;
            lanzarPopUpModal('<%=request.getContextPath()%>/PeticionModuloIntegracion.do?tarea=preparar&modulo=MELANBIDE32&operacion=cargarModifTrayectoriaCE&tipo=0&numero=<%=numExpediente%>&idTray=' + listaTrayectoriaCE[tabTrayectoriaCE.selectedIndex][0] + '&control=' + control.getTime(), 450, 900, 'no', 'no', function (result) {
                if (result != undefined) {
                    if (result[0] == '0') {
                        recargarTablaTrayectoriaCE(result);
                    }
                }
            });
        } else {
            jsp_alerta('A', '<%=meLanbide32I18n.getMensaje(idiomaUsuario, "msg.msjNoSelecFila")%>');
        }
    }

    function pulsarEliminarTrayectoriaCE() {

        if (tabTrayectoriaCE.selectedIndex != -1) {
            var resultado = jsp_alerta('', '<%=meLanbide32I18n.getMensaje(idiomaUsuario, "msg.preguntaEliminar")%>');
            if (resultado == 1) {

                var ajax = getXMLHttpRequest();
                var nodos = null;
                var CONTEXT_PATH = '<%=request.getContextPath()%>'
                var url = CONTEXT_PATH + "/PeticionModuloIntegracion.do";
                var parametros = "";
                var control = new Date();
                parametros = 'tarea=preparar&modulo=MELANBIDE32&operacion=eliminarTrayectoriaCE&tipo=0&numero=<%=numExpediente%>&idTray=' + listaTrayectoriaCE[tabTrayectoriaCE.selectedIndex][0] + '&control=' + control.getTime();
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
                    var listaTrayectoriaNueva = new Array();
                    var fila = new Array();
                    var nodoFila;
                    var hijosFila;
                    for (j = 0; hijos != null && j < hijos.length; j++) {
                        if (hijos[j].nodeName == "CODIGO_OPERACION") {
                            codigoOperacion = hijos[j].childNodes[0].nodeValue;
                            listaTrayectoriaNueva[j] = codigoOperacion;
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
                                } else if (hijosFila[cont].nodeName == "DESCRIPCION") {
                                    if (hijosFila[cont].childNodes.length > 0) {
                                        fila[1] = hijosFila[cont].childNodes[0].nodeValue;
                                    } else {
                                        fila[1] = '-';
                                    }
                                } else if (hijosFila[cont].nodeName == "DURACION") {
                                    if (hijosFila[cont].childNodes.length > 0) {
                                        fila[2] = hijosFila[cont].childNodes[0].nodeValue;
                                    } else {
                                        fila[2] = '-';
                                    }
                                }
                            }
                            listaTrayectoriaNueva[j] = fila;
                            fila = new Array();
                        }
                    }//for(j=0;hijos!=null && j<hijos.length;j++)
                    if (codigoOperacion == "0") {
                        recargarTablaTrayectoriaCE(listaTrayectoriaNueva);
                    } else if (codigoOperacion == "1") {
                        jsp_alerta("A", '<%=meLanbide32I18n.getMensaje(idiomaUsuario,"error.errorBD")%>');
                    } else if (codigoOperacion == "2") {
                        jsp_alerta("A", '<%=meLanbide32I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                    } else if (codigoOperacion == "3") {
                        jsp_alerta("A", '<%=meLanbide32I18n.getMensaje(idiomaUsuario,"error.pasoParametros")%>');
                    } else {
                        jsp_alerta("A", '<%=meLanbide32I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                    }//if(
                } catch (Err) {
                    jsp_alerta("A", '<%=meLanbide32I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                }//try-catch
            }
        } else {
            jsp_alerta('A', '<%=meLanbide32I18n.getMensaje(idiomaUsuario, "msg.msjNoSelecFila")%>');
        }
    }

    function recargarTablaUbicacionesCE(result) {
        var fila;
        listaUbicacionesCE = new Array();
        listaUbicacionesCETabla = new Array();
        for (var i = 1; i < result.length; i++) {
            fila = result[i];
            listaUbicacionesCE[i - 1] = [fila[0], fila[1], fila[2], fila[3], fila[4], fila[5], fila[12],fila[6], fila[7], fila[8], fila[9], fila[10], fila[11],fila[13]];
            if(anio>=2018){
                if(anio<2023){
                    listaUbicacionesCETabla[i - 1] = [(fila[1]=="null"?"-":fila[1]), (fila[2]=="null"?"-":fila[2]), (fila[3]=="null"?"-":fila[3]), (fila[4]=="null"?"-":fila[4]), (fila[5]=="null"?"-":fila[5]), (fila[7]=="null"?"-":fila[7]), (fila[6]=="null"?"-":fila[6]), (fila[8]=="null"?"-":fila[8]), (fila[9]=="null"?"-":fila[9]), (fila[10]=="null"?"-":fila[10]=="1"?"S":fila[10]=="0"?"N":"-"), (fila[11]=="null"?"-":fila[11]=="1"?"S":fila[11]=="0"?"N":"-")];
                }else{
                    listaUbicacionesCETabla[i - 1] = [(fila[1]=="null"?"-":fila[1]), (fila[2]=="null"?"-":fila[2]), (fila[3]=="null"?"-":fila[3]), (fila[4]=="null"?"-":fila[4]), (fila[5]=="null"?"-":fila[5]), (fila[7]=="null"?"-":fila[7]), (fila[12]=="null"?"-":fila[12]), (fila[6]=="null"?"-":fila[6]), (fila[8]=="null"?"-":fila[8]), (fila[10]=="null"?"-":fila[10]=="1"?"S":fila[10]=="0"?"N":"-"), (fila[11]=="null"?"-":fila[11]=="1"?"S":fila[11]=="0"?"N":"-"), (fila[13]=="null"?"-":fila[13]=="1"?"S":fila[13]=="0"?"N":"-")];
                }
            }
            else{
                listaUbicacionesCETabla[i - 1] = [(fila[1]=="null"?"-":fila[1]), (fila[2]=="null"?"-":fila[2]), (fila[3]=="null"?"-":fila[3]), (fila[4]=="null"?"-":fila[4]), (fila[5]=="null"?"-":fila[5]), (fila[7]=="null"?"-":fila[7]), (fila[6]=="null"?"-":fila[6]), (fila[8]=="null"?"-":fila[8]), (fila[9]=="null"?"-":fila[9])];
            }
        }
        tabUbicacionesCE = new Tabla(true, '<%=descriptor.getDescripcion("buscar")%>', '<%=descriptor.getDescripcion("anterior")%>', '<%=descriptor.getDescripcion("siguiente")%>', '<%=descriptor.getDescripcion("mosFilasPag")%>', '<%=descriptor.getDescripcion("msgNoResultBusq")%>', '<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>', '<%=descriptor.getDescripcion("filtrDeTotal")%>', '<%=descriptor.getDescripcion("primero")%>', '<%=descriptor.getDescripcion("ultimo")%>', document.getElementById('listaUbicacionesCE'), anchoTabla);
        tabUbicacionesCE.addColumna('60', 'left', "<%= meLanbide32I18n.getMensaje(idiomaUsuario,"ce.tablaSolicHoras.col1")%>");
        tabUbicacionesCE.addColumna('82', 'left', "<%= meLanbide32I18n.getMensaje(idiomaUsuario,"ce.tablaSolicHoras.col2")%>");
        tabUbicacionesCE.addColumna('100', 'left', "<%= meLanbide32I18n.getMensaje(idiomaUsuario,"ce.tablaSolicHoras.col3")%>");
        tabUbicacionesCE.addColumna('120', 'left', "<%= meLanbide32I18n.getMensaje(idiomaUsuario,"ce.tablaSolicHoras.col4")%>");
        tabUbicacionesCE.addColumna('326', 'left', "<%= meLanbide32I18n.getMensaje(idiomaUsuario,"ce.tablaSolicHoras.col5")%>");
        tabUbicacionesCE.addColumna('80', 'right', "<%= meLanbide32I18n.getMensaje(idiomaUsuario,"ce.tablaSolicHoras.col7")%>");
        if(anio>=2023){
            tabUbicacionesCE.addColumna('80', 'right', "<%= meLanbide32I18n.getMensaje(idiomaUsuario,"ce.tablaSolicHoras.col12")%>");
            tabUbicacionesCE.addColumna('80', 'right', "<%= meLanbide32I18n.getMensaje(idiomaUsuario,"ce.tablaSolicHoras.col6")%>");
            tabUbicacionesCE.addColumna('80', 'right', "<%= meLanbide32I18n.getMensaje(idiomaUsuario,"ce.tablaSolicHoras.col8")%>");
            tabUbicacionesCE.addColumna('80', 'right', "<%= meLanbide32I18n.getMensaje(idiomaUsuario,"ce.tablaSolicHoras.col10")%>");
            tabUbicacionesCE.addColumna('85', 'right', "<%= meLanbide32I18n.getMensaje(idiomaUsuario,"ce.tablaSolicHoras.col11")%>");
            tabUbicacionesCE.addColumna('85', 'right', "<%= meLanbide32I18n.getMensaje(idiomaUsuario,"ce.tablaSolicHoras.col13")%>");
        }else{
        tabUbicacionesCE.addColumna('80', 'right', "<%= meLanbide32I18n.getMensaje(idiomaUsuario,"ce.tablaSolicHoras.col6")%>");
        tabUbicacionesCE.addColumna('80', 'right', "<%= meLanbide32I18n.getMensaje(idiomaUsuario,"ce.tablaSolicHoras.col8")%>");
            tabUbicacionesCE.addColumna('100', 'right', "<%= meLanbide32I18n.getMensaje(idiomaUsuario,"ce.tablaSolicHoras.col9")%>");
        if(anio>=2018){
            tabUbicacionesCE.addColumna('80', 'right', "<%= meLanbide32I18n.getMensaje(idiomaUsuario,"ce.tablaSolicHoras.col10")%>");
            tabUbicacionesCE.addColumna('85', 'right', "<%= meLanbide32I18n.getMensaje(idiomaUsuario,"ce.tablaSolicHoras.col11")%>");
        }
        }
        tabUbicacionesCE.lineas = listaUbicacionesCETabla;
        tabUbicacionesCE.displayCabecera = true;
        tabUbicacionesCE.height = 100;
        tabUbicacionesCE.displayTabla();
        if (navigator.appName.indexOf("Internet Explorer") != -1) {
            try {
                var div = document.getElementById('listaUbicacionesCE');
                div.children[0].children[0].children[0].children[0].children[0].children[0].children[0].children[0].children[5].style.backgroundColor = 'FF9966';
                div.children[0].children[0].children[0].children[0].children[0].children[0].children[0].children[0].children[5].style.width = '100%';
                div.children[0].children[1].children[0].children[0].children[0].children[0].children[0].children[5].style.width = '100%';
            } catch (err) {

            }
        }
    }

    function recargarTablaTrayectoriaCE(result) {
        var fila;
        listaTrayectoriaCE = new Array();
        listaTrayectoriaCETabla = new Array();
        for (var i = 1; i < result.length; i++) {
            fila = result[i];
            var textDescripcion = fila[1].replace("*BR***", "<br />");
            listaTrayectoriaCE[i - 1] = [fila[0], textDescripcion, fila[2], fila[3]];//si en IE9
            listaTrayectoriaCETabla[i - 1] = [textDescripcion, fila[2], fila[3]];
        }
        tabTrayectoriaCE = new Tabla(true, '<%=descriptor.getDescripcion("buscar")%>', '<%=descriptor.getDescripcion("anterior")%>', '<%=descriptor.getDescripcion("siguiente")%>', '<%=descriptor.getDescripcion("mosFilasPag")%>', '<%=descriptor.getDescripcion("msgNoResultBusq")%>', '<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>', '<%=descriptor.getDescripcion("filtrDeTotal")%>', '<%=descriptor.getDescripcion("primero")%>', '<%=descriptor.getDescripcion("ultimo")%>', document.getElementById('listaTrayectoriaCE'), 850);
        tabTrayectoriaCE.addColumna('500', 'left', "<%= meLanbide32I18n.getMensaje(idiomaUsuario,"ce.tablaTrayectoria.col1")%>");
        tabTrayectoriaCE.addColumna('175', 'right', "<%= meLanbide32I18n.getMensaje(idiomaUsuario,"ce.tablaTrayectoria.col2")%>");
        tabTrayectoriaCE.addColumna('175', 'right', "<%= meLanbide32I18n.getMensaje(idiomaUsuario,"ce.tablaTrayectoria.col3")%>");

        tabTrayectoriaCE.displayCabecera = true;
        tabTrayectoriaCE.height = 150;
        tabTrayectoriaCE.lineas = listaTrayectoriaCETabla;
        tabTrayectoriaCE.displayTabla();

        if (navigator.appName.indexOf("Internet Explorer") != -1) {
            try {
                var div = document.getElementById('listaTrayectoriaCE');
                div.children[0].children[0].children[0].children[0].children[0].children[0].children[1].style.width = '100%';
                div.children[0].children[1].children[0].children[0].children[0].children[1].style.width = '100%';
            } catch (err) {

            }
        }
    }

    function resetCombo(nombreCombo) {
        try {
            var codCombo = 'cod' + nombreCombo;
            var descCombo = 'desc' + nombreCombo;
            document.getElementById(codCombo).value = '';
            document.getElementById(descCombo).value = '';
        } catch (err) {

        }
    }

    function guardarDatosCE() {
        if (validarDatosCE()) {
            var ajax = getXMLHttpRequest();
            var nodos = null;
            var CONTEXT_PATH = '<%=request.getContextPath()%>'
            var url = CONTEXT_PATH + "/PeticionModuloIntegracion.do";
            var parametros = "";
            var control = new Date();
            parametros = 'tarea=preparar&modulo=MELANBIDE32&operacion=guardarOtrosDatosCE&tipo=0&numero=<%=numExpediente%>'
                    + '&nomEnt=' + document.getElementById('nombreEntCE').value
                    + '&' + getParametrosOtrosDatosCE()
                    + '&control=' + control.getTime();
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
                for (j = 0; hijos != null && j < hijos.length; j++) {
                    if (hijos[j].nodeName == "CODIGO_OPERACION") {
                        codigoOperacion = hijos[j].childNodes[0].nodeValue;
                    }
                }//for(j=0;hijos!=null && j<hijos.length;j++)
                if (codigoOperacion == "0") {
                    jsp_alerta("A", '<%=meLanbide32I18n.getMensaje(idiomaUsuario,"msg.datosGuardadosOK")%>');
                } else if (codigoOperacion == "1") {
                    jsp_alerta("A", '<%=meLanbide32I18n.getMensaje(idiomaUsuario,"error.errorBD")%>');
                } else if (codigoOperacion == "2") {
                    jsp_alerta("A", '<%=meLanbide32I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                } else if (codigoOperacion == "3") {
                    jsp_alerta("A", '<%=meLanbide32I18n.getMensaje(idiomaUsuario,"error.pasoParametros")%>');
                } else if (codigoOperacion == "4") {
                    jsp_alerta("A", '<%=meLanbide32I18n.getMensaje(idiomaUsuario,"error.expSinTercero")%>');
                } else {
                    jsp_alerta("A", '<%=meLanbide32I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                }//if(
            } catch (Err) {
                jsp_alerta("A", '<%=meLanbide32I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
            }//try-catch
        } else {
            jsp_alerta("A", mensajeValidacion);
        }
    }

    function validarDatosCE() {
        mensajeValidacion = "";

        var nombreEntidad = document.getElementById('nombreEntCE').value;
        if (nombreEntidad == null || nombreEntidad == '') {
            mensajeValidacion = '<%=meLanbide32I18n.getMensaje(idiomaUsuario, "ce.msg.nomEntidadObligatorio")%>';
            document.getElementById('nombreEntCE').style.border = '1px solid red';
            return false;
        } else {
            if (!comprobarCaracteresEspecialesCE(nombreEntidad)) {
                document.getElementById('nombreEntCE').style.border = '1px solid red';
                mensajeValidacion = '<%=meLanbide32I18n.getMensaje(idiomaUsuario, "ce.msg.nomEntidadCaracteresEspeciales")%>';
                return false;
            } else {
                mensajeValidacion = '';
                document.getElementById('nombreEntCE').removeAttribute("style");
            }
        }

        return validarOtrosDatos();
    }

    function validarNumericoDecimalCE(numero) {
        try {
            if (Trim(numero.value) != '') {
                return /^[0-9]{1,5}(,[0-9]{1,2})?$/.test(numero.value);
            }
        } catch (err) {
            return false;
        }
    }

    function validarNumericoCE(numero) {
        try {
            return !isNaN(numero.value);
        } catch (err) {
            return false;
        }
    }

    function cambioNombreEntidadCE() {
        try {
            var nuevoValor = document.getElementById('nombreEntCE').value;
            var nombreEntidadORI = document.getElementById('nombreEntORI');
            if (nombreEntidadORI != null) {
                nombreEntidadORI.value = nuevoValor;
            }
        } catch (err) {

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

    function comprobarCaracteresEspecialesCE(texto) {
        //var iChars = "!@#$%^&*()+=-[]\\';,./{}|\":<>?";
        var iChars = "&'<>|^/\\%";
        for (var i = 0; i < texto.length; i++) {
            if (iChars.indexOf(texto.charAt(i)) != -1) {
                return false;
            }
        }
        return true;
    }

    function deshabilitarRadios() {
        try {
            deshabilitarRadiosOtrosDatos();
        } catch (err) {
        }
    }
    
    function pulsarAltaAmbitoCE(){}
    function pulsarEliminarAmbitoCE(){}
    function pulsarModificarAmbitoCE(){}
    
</script>
<body>
    <!-- Campos Comunes del Modulo Gestion JS -->
    <input type="hidden" id="ejercicioExpediente" name="ejercicioExpediente" value="<%=ejercicio%>"/>
    
    <div class="tab-page" id="tabPage321" style="height:520px; width: 100%;">
        <h2 class="tab" id="pestana321"><%=meLanbide32I18n.getMensaje(idiomaUsuario,"ce.label.tituloPestana")%></h2>
        <script type="text/javascript">tp1_p321 = tp1.addTabPage(document.getElementById("tabPage321"));</script>
        <div style="clear: both;">
            <div>
                <div style="clear: both; width: 100%; text-align: left; padding-top: 10px;">
                    <div id="tab-panel-321" class="tab-pane" style="float: left;" align="center"></div>
                    <script type="text/javascript">
                        tp321 = new WebFXTabPane(document.getElementById("tab-panel-321"));
                        tp321.selectedIndex = 0;
                    </script>
                    <!-- subpestaña Datos generales-->
                    <div class="tab-page" id="tabPage3211" style="height: 194px; font-size: 13px;">
                        <h2 class="tab" id="pestana3211" style="<%=margenIzqPestanasDatosColec%>"><%=meLanbide32I18n.getMensaje(idiomaUsuario,"ce.label.datosGenerales.tituloPestana")%></h2>
                        <script type="text/javascript">tp321.addTabPage(document.getElementById("tabPage3211"));</script>
                        <div style="clear: both;">

                            <fieldset style="width: 97.5%;">
                                <legend class="legendAzul"><%=meLanbide32I18n.getMensaje(idiomaUsuario, "ce.legend.centroEmpleo")%></legend>
                                <div id="divGeneral" onscroll="deshabilitarRadios();" style="overflow-y: auto; overflow-x: hidden; height: 440px;">
                                    <div class="tituloAzul">
                                        <span>
                                            <%=meLanbide32I18n.getMensaje(idiomaUsuario,"ce.label.ubicacionesCE")%>
                                        </span>
                                    </div>
                                    <div style="width: 120px; float: left;">
                                        <%=meLanbide32I18n.getMensaje(idiomaUsuario,"ce.label.nomEnt")%>
                                    </div>
                                    <div style="width: 575px; float: left;">
                                        <input id="nombreEntCE" name="nombreEntCE" type="text" class="inputTexto" size="92" maxlength="80" value="<%=entidad != null && entidad.getOriEntNom() != null ? entidad.getOriEntNom() : ""%>" onchange="cambioNombreEntidadCE();">
                                    </div>
                                    <div id="listaUbicacionesCE" class="tablaPersCE"  align="center"></div>
                                    <div class="botonera">
                                        <input type="button" id="btnNuevaUbicCE" name="btnNuevaUbicCE" class="botonGeneral" value="<%=meLanbide32I18n.getMensaje(idiomaUsuario, "ori.btn.nuevo")%>" onclick="pulsarAltaUbicacionCE();">
                                        <input type="button" id="btnEliminarUbicCE" name="btnEliminarUbicCE" class="botonGeneral" value="<%=meLanbide32I18n.getMensaje(idiomaUsuario, "ori.btn.eliminar")%>" onclick="pulsarEliminarUbicacionCE();">
                                        <input type="button" id="btnModificarUbicCE" name="btnModificarUbicCE" class="botonGeneral" value="<%=meLanbide32I18n.getMensaje(idiomaUsuario, "ori.btn.modificar")%>" onclick="pulsarModificarUbicacionCE();">
                                    </div>
                                    <div id="divListaTrayectoriaCE"> 
                                        <div class="tituloAzul">
                                            <span>
                                                <%=meLanbide32I18n.getMensaje(idiomaUsuario,"ce.label.trayectoriaCentroEmpleo")%>
                                            </span>
                                        </div>
                                        <div id="listaTrayectoriaCE" align="center"></div>
                                        <div class="botonera">
                                            <input type="button" id="btnNuevaTrayectoriaCE" name="btnNuevaTrayectoriaCE" class="botonGeneral" style="<%=procedimiento.equals(ConstantesMeLanbide32.CODIGO_PROCEDIMIENTO_CEMP) ? "display:none" : ""%>" value="<%=meLanbide32I18n.getMensaje(idiomaUsuario, "ori.btn.nuevo")%>" onclick="pulsarAltaTrayectoriaCE();">
                                            <input type="button" id="btnEliminarTrayectoriaCE" name="btnEliminarTrayectoriaCE"   class="botonGeneral" style="<%=procedimiento.equals(ConstantesMeLanbide32.CODIGO_PROCEDIMIENTO_CEMP) ? "display:none" : ""%>" value="<%=meLanbide32I18n.getMensaje(idiomaUsuario, "ori.btn.eliminar")%>" onclick="pulsarEliminarTrayectoriaCE();">
                                            <input type="button" id="btnModificarTrayectoriaCE" name="btnModificarTrayectoriaCE" class="botonGeneral" value="<%=meLanbide32I18n.getMensaje(idiomaUsuario, "ori.btn.modificar")%>" onclick="pulsarModificarTrayectoriaCE();">
                                        </div>                                        
                                    </div>
                                    <jsp:include page="<%=urlOtrosDatosCE%>" flush="true"/>
                                </div>
                            </fieldset>
                            <div class="botonera" style="padding-top: 20px;">
                                <input type="button" id="btnGuardarCE" name="btnGuardarCE" class="botonGeneral" value="<%=meLanbide32I18n.getMensaje(idiomaUsuario, "ori.btn.guardar")%>" onclick="guardarDatosCE();">
                            </div>

                        </div>
                    </div> <!-- fin subpestaña Datos generales-->
                    <div class="tab-page" id="tabPage3212" style="height: 194px; font-size: 13px;">
                        <h2 class="tab" id="pestana3212" style="<%=margenIzqPestanasDatosColec%>"><%=meLanbide32I18n.getMensaje(idiomaUsuario,"ce.label.ambitos.tituloPestana")%></h2>
                        <script type="text/javascript">tp321.addTabPage(document.getElementById("tabPage3212"));</script>
                        <div style="clear: both;">
                            <jsp:include page="<%=urlPestanaDatos_ambitosSolicitados%>" flush="true"/>
                        </div>
                    </div>      
                </div>
            </div>
        </div>
    </div>
</body>

<script type="text/javascript">
    //Tabla solicitud de horas de orientacion
    var tabUbicacionesCE;
    var listaUbicacionesCE = new Array();
    var listaUbicacionesCETabla = new Array();

    tabUbicacionesCE = new Tabla(true, '<%=descriptor.getDescripcion("buscar")%>', '<%=descriptor.getDescripcion("anterior")%>', '<%=descriptor.getDescripcion("siguiente")%>', '<%=descriptor.getDescripcion("mosFilasPag")%>', '<%=descriptor.getDescripcion("msgNoResultBusq")%>', '<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>', '<%=descriptor.getDescripcion("filtrDeTotal")%>', '<%=descriptor.getDescripcion("primero")%>', '<%=descriptor.getDescripcion("ultimo")%>', document.getElementById('listaUbicacionesCE'), anchoTabla);
    tabUbicacionesCE.addColumna('60', 'left', "<%= meLanbide32I18n.getMensaje(idiomaUsuario,"ce.tablaSolicHoras.col1")%>");
    tabUbicacionesCE.addColumna('82', 'left', "<%= meLanbide32I18n.getMensaje(idiomaUsuario,"ce.tablaSolicHoras.col2")%>");
    tabUbicacionesCE.addColumna('100', 'left', "<%= meLanbide32I18n.getMensaje(idiomaUsuario,"ce.tablaSolicHoras.col3")%>");
    tabUbicacionesCE.addColumna('120', 'left', "<%= meLanbide32I18n.getMensaje(idiomaUsuario,"ce.tablaSolicHoras.col4")%>");
    tabUbicacionesCE.addColumna('326', 'left', "<%= meLanbide32I18n.getMensaje(idiomaUsuario,"ce.tablaSolicHoras.col5")%>");
    tabUbicacionesCE.addColumna('80', 'right', "<%= meLanbide32I18n.getMensaje(idiomaUsuario,"ce.tablaSolicHoras.col7")%>");
    if(anio>=2023){
        tabUbicacionesCE.addColumna('80', 'right', "<%= meLanbide32I18n.getMensaje(idiomaUsuario,"ce.tablaSolicHoras.col12")%>");
        tabUbicacionesCE.addColumna('80', 'right', "<%= meLanbide32I18n.getMensaje(idiomaUsuario,"ce.tablaSolicHoras.col6")%>");
        tabUbicacionesCE.addColumna('80', 'right', "<%= meLanbide32I18n.getMensaje(idiomaUsuario,"ce.tablaSolicHoras.col8")%>");
        tabUbicacionesCE.addColumna('80', 'right', "<%= meLanbide32I18n.getMensaje(idiomaUsuario,"ce.tablaSolicHoras.col10")%>");
        tabUbicacionesCE.addColumna('85', 'right', "<%= meLanbide32I18n.getMensaje(idiomaUsuario,"ce.tablaSolicHoras.col11")%>");
        tabUbicacionesCE.addColumna('85', 'right', "<%= meLanbide32I18n.getMensaje(idiomaUsuario,"ce.tablaSolicHoras.col13")%>");
    }else{
    tabUbicacionesCE.addColumna('80', 'right', "<%= meLanbide32I18n.getMensaje(idiomaUsuario,"ce.tablaSolicHoras.col6")%>");
    tabUbicacionesCE.addColumna('80', 'right', "<%= meLanbide32I18n.getMensaje(idiomaUsuario,"ce.tablaSolicHoras.col8")%>");
        tabUbicacionesCE.addColumna('100', 'right', "<%= meLanbide32I18n.getMensaje(idiomaUsuario,"ce.tablaSolicHoras.col9")%>");
    if (anio >= 2018) {
        tabUbicacionesCE.addColumna('80', 'right', "<%= meLanbide32I18n.getMensaje(idiomaUsuario,"ce.tablaSolicHoras.col10")%>");
        tabUbicacionesCE.addColumna('85', 'right', "<%= meLanbide32I18n.getMensaje(idiomaUsuario,"ce.tablaSolicHoras.col11")%>");
    }
    }

    tabUbicacionesCE.displayCabecera = true;
    tabUbicacionesCE.height = 100;
    <%  		
        FilaUbicCentroEmpleoVO voU = null;
        List<FilaUbicCentroEmpleoVO> ubicList = (List<FilaUbicCentroEmpleoVO>)request.getAttribute("listaUbicCentroEmpleo");													
        if (ubicList != null && ubicList.size() >0){
            for(int i = 0;i < ubicList.size();i++)
            {
                voU = ubicList.get(i);
                
    %>
    if (anio >= 2018) {
        if (anio < 2023){
            listaUbicacionesCE[<%=i%>] = ['<%=voU.getOriCeUbicCod()%>', '<%=voU.getEspecial()%>', '<%=voU.getProvincia()%>', '<%=voU.getAmbito()%>', '<%=voU.getMunicipio()%>', '<%=voU.getDireccion()%>', '<%=voU.getValidacion()%>', '<%=voU.getAdjudicado()%>', '<%=voU.getCodigoPostal()!=null?voU.getCodigoPostal():""%>', '<%=voU.getHorarioAtencion()!=null?voU.getHorarioAtencion():""%>', '<%=voU.getAprobado()!=null ? voU.getAprobado():""%>', '<%=voU.getMantenido()!=null ? voU.getMantenido(): ""%>'];
        }else{
            listaUbicacionesCE[<%=i%>] = ['<%=voU.getOriCeUbicCod()%>', '<%=voU.getEspecial()%>', '<%=voU.getProvincia()%>', '<%=voU.getAmbito()%>', '<%=voU.getMunicipio()%>', '<%=voU.getDireccion()%>', '<%=voU.getValidacion()%>', '<%=voU.getPuntuacionCentroE()%>', '<%=voU.getAdjudicado()%>', '<%=voU.getCodigoPostal()!=null?voU.getCodigoPostal():""%>', '<%=voU.getAprobado()!=null ? voU.getAprobado():""%>', '<%=voU.getMantenido()!=null ? voU.getMantenido(): ""%>', '<%=voU.getLocalNuevoValidado()!=null ? voU.getLocalNuevoValidado(): ""%>'];
        }
    }else{
        listaUbicacionesCE[<%=i%>] = ['<%=voU.getOriCeUbicCod()%>', '<%=voU.getEspecial()%>', '<%=voU.getProvincia()%>', '<%=voU.getAmbito()%>', '<%=voU.getMunicipio()%>', '<%=voU.getDireccion()%>', '<%=voU.getValidacion()%>', '<%=voU.getAdjudicado()%>', '<%=voU.getCodigoPostal()!=null?voU.getCodigoPostal():""%>', '<%=voU.getHorarioAtencion()!=null?voU.getHorarioAtencion():""%>'];
    }

    var valorLocalPreviamenteAprobado ='<%= voU.getAprobado() %>';
    var valorMantieneRequisitosLPA ='<%= voU.getMantenido() %>';
    var valorLocalNuevoValidado ='<%= voU.getLocalNuevoValidado() %>';
    if(valorLocalPreviamenteAprobado!=null && valorLocalPreviamenteAprobado!="null"){
        valorLocalPreviamenteAprobado=(valorLocalPreviamenteAprobado=="1"?"S":"N");
    }else
        valorLocalPreviamenteAprobado="-";
    if(valorMantieneRequisitosLPA!=null && valorMantieneRequisitosLPA!="null"){
        valorMantieneRequisitosLPA=(valorMantieneRequisitosLPA=="1"?"S":"N");
    }else{
        valorMantieneRequisitosLPA="-";
    }
    if(valorLocalNuevoValidado!=null && valorLocalNuevoValidado!="null"){
        valorLocalNuevoValidado=(valorLocalNuevoValidado=="1"?"S":"N");
    }else{
        valorLocalNuevoValidado="-";
    }
    if (anio >= 2018) {
        if (anio < 2023) {
            listaUbicacionesCETabla[<%=i%>] = ['<%=voU.getEspecial()!=null?voU.getEspecial():"-"%>', '<%=voU.getProvincia()!=null ? voU.getProvincia() :"-"%>', '<%=voU.getAmbito()!=null?voU.getAmbito():"-"%>', '<%=voU.getMunicipio()!=null?voU.getMunicipio():"-"%>', '<%=voU.getDireccion()!=null?voU.getDireccion():"-"%>', '<%=voU.getValidacion()!=null?voU.getValidacion():"-"%>', '<%=voU.getAdjudicado()!=null?voU.getAdjudicado():"-"%>', '<%=voU.getCodigoPostal()!=null?voU.getCodigoPostal():""%>', '<%=voU.getHorarioAtencion()!=null?voU.getHorarioAtencion():""%>',valorLocalPreviamenteAprobado,valorMantieneRequisitosLPA];
        }else{
            listaUbicacionesCETabla[<%=i%>] = ['<%=voU.getEspecial()!=null?voU.getEspecial():"-"%>', '<%=voU.getProvincia()!=null ? voU.getProvincia() :"-"%>', '<%=voU.getAmbito()!=null?voU.getAmbito():"-"%>', '<%=voU.getMunicipio()!=null?voU.getMunicipio():"-"%>', '<%=voU.getDireccion()!=null?voU.getDireccion():"-"%>', '<%=voU.getValidacion()!=null?voU.getValidacion():"-"%>', '<%=voU.getPuntuacionCentroE()!=null?voU.getPuntuacionCentroE():"-"%>', '<%=voU.getAdjudicado()!=null?voU.getAdjudicado():"-"%>', '<%=voU.getCodigoPostal()!=null?voU.getCodigoPostal():""%>', valorLocalPreviamenteAprobado,valorMantieneRequisitosLPA,valorLocalNuevoValidado];
        }
    }else{
        listaUbicacionesCETabla[<%=i%>] = ['<%=voU.getEspecial()!=null?voU.getEspecial():"-"%>', '<%=voU.getProvincia()!=null ? voU.getProvincia() :"-"%>', '<%=voU.getAmbito()!=null?voU.getAmbito():"-"%>', '<%=voU.getMunicipio()!=null?voU.getMunicipio():"-"%>', '<%=voU.getDireccion()!=null?voU.getDireccion():"-"%>', '<%=voU.getValidacion()!=null?voU.getValidacion():"-"%>', '<%=voU.getAdjudicado()!=null?voU.getAdjudicado():"-"%>', '<%=voU.getCodigoPostal()!=null?voU.getCodigoPostal():""%>', '<%=voU.getHorarioAtencion()!=null?voU.getHorarioAtencion():""%>'];
    }
    <%
            }// for
        }// if
    %>
    tabUbicacionesCE.lineas = listaUbicacionesCETabla;
    tabUbicacionesCE.displayTabla();

    if (navigator.appName.indexOf("Internet Explorer") != -1) {
        try {
            var div = document.getElementById('listaUbicacionesCE');
            div.children[0].children[0].children[0].children[0].children[0].children[0].children[0].children[6].style.backgroundColor = 'FF9966';
            div.children[0].children[0].children[0].children[0].children[0].children[0].children[0].children[6].style.width = '100%';
            div.children[0].children[1].children[0].children[0].children[0].children[0].children[6].style.width = '100%';
        } catch (err) {

        }
    }

    //Tabla Trayectoria de la entidad
    var tabTrayectoriaCE;
    var listaTrayectoriaCE = new Array();
    var listaTrayectoriaCETabla = new Array();

    tabTrayectoriaCE = new Tabla(true, '<%=descriptor.getDescripcion("buscar")%>', '<%=descriptor.getDescripcion("anterior")%>', '<%=descriptor.getDescripcion("siguiente")%>', '<%=descriptor.getDescripcion("mosFilasPag")%>', '<%=descriptor.getDescripcion("msgNoResultBusq")%>', '<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>', '<%=descriptor.getDescripcion("filtrDeTotal")%>', '<%=descriptor.getDescripcion("primero")%>', '<%=descriptor.getDescripcion("ultimo")%>', document.getElementById('listaTrayectoriaCE'), 850);
    tabTrayectoriaCE.addColumna('430', 'left', "<%= meLanbide32I18n.getMensaje(idiomaUsuario,"ce.tablaTrayectoria.col1")%>");
    tabTrayectoriaCE.addColumna('215', 'right', "<%= meLanbide32I18n.getMensaje(idiomaUsuario,"ce.tablaTrayectoria.col2")%>");
    tabTrayectoriaCE.addColumna('215', 'right', "<%= meLanbide32I18n.getMensaje(idiomaUsuario,"ce.tablaTrayectoria.col3")%>");

    tabTrayectoriaCE.displayCabecera = true;
    tabTrayectoriaCE.height = 150;

    <%  		
        FilaTrayectoriaCentroEmpleoVO voT = null;
        List<FilaTrayectoriaCentroEmpleoVO> traList = (List<FilaTrayectoriaCentroEmpleoVO>)request.getAttribute("listaTrayectoriaCentroEmpleo");													
        if (traList!= null && traList.size() >0){
            for (int indiceTra=0;indiceTra<traList.size();indiceTra++)
            {
                voT = traList.get(indiceTra);
                
    %>
    var textoDescripcion1 = '<%=voT.getDescripcion().toUpperCase()%>';
    textoDescripcion1 = textoDescripcion1.replace("*BR***", "<BR />");
    listaTrayectoriaCE[<%=indiceTra%>] = ['<%=voT.getOriCeCod()%>', textoDescripcion1, '<%=voT.getDuracion()%>', '<%=voT.getDuracionValidada()%>'];
    listaTrayectoriaCETabla[<%=indiceTra%>] = [textoDescripcion1, '<%=voT.getDuracion()%>', '<%=voT.getDuracionValidada()%>'];
    <%
            }// for
        }// if
    %>

    tabTrayectoriaCE.lineas = listaTrayectoriaCETabla;

    tabTrayectoriaCE.displayTabla();
    if (navigator.appName.indexOf("Internet Explorer") != -1) {
        try {
            var div = document.getElementById('listaTrayectoriaCE');
            div.children[0].children[0].children[0].children[0].children[0].children[0].children[1].style.width = '100%';
            div.children[0].children[1].children[0].children[0].children[0].children[1].style.width = '100%';
        } catch (err) {

        }
    }
</script>

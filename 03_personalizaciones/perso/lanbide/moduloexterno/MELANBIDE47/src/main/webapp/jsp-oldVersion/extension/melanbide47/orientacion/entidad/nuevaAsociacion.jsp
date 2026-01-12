<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>

<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide47.i18n.MeLanbide47I18n" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide47.vo.orientacion.AsociacionVO" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide47.util.ConstantesMeLanbide47" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide47.vo.comun.SelectItem"%>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.agora.business.util.GeneralValueObject"%>
<%@page import="es.altia.common.service.config.Config"%>
<%@page import="es.altia.common.service.config.ConfigServiceHelper"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Date"%>
<%@page import="java.util.Vector"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="org.apache.commons.lang.StringEscapeUtils"%>

<html>
    <head> 
        <%
            int idiomaUsuario = 1;
            UsuarioValueObject usuario = new UsuarioValueObject();
            try
            {
                if (session != null) 
                {
                    if (usuario != null) 
                    {
                        usuario = (UsuarioValueObject) session.getAttribute("usuario");
                        idiomaUsuario = usuario.getIdioma();
                    }
                }
            }
            catch(Exception ex)
            {

            }
            //Clase para internacionalizar los mensajes de la aplicación.
            MeLanbide47I18n meLanbide47I18n = MeLanbide47I18n.getInstance();

            Config m_Config = ConfigServiceHelper.getConfig("common");
            String statusBar = m_Config.getString("JSP.StatusBar");
            String nombreModulo     = request.getParameter("nombreModulo");
            String codOrganizacion  = request.getParameter("codOrganizacionModulo");
            String numExpediente    = request.getParameter("numero");
            
            AsociacionVO asociacionModif = (AsociacionVO)request.getAttribute("asociacionModif");
            String descTit1 = (String)request.getAttribute("descTit1");
            String descTit2 = (String)request.getAttribute("descTit2");
            String descTit3 = (String)request.getAttribute("descTit3");
            Boolean consulta = request.getAttribute("consulta") != null ? (Boolean)request.getAttribute("consulta") : false;
    
    
            String tituloPagina = "";
            if(consulta)
            {
                tituloPagina = meLanbide47I18n.getMensaje(idiomaUsuario,"label.asociacion.titulo.consultaAsociacion");
            }
            else
            {
                if(asociacionModif != null)
                {
                    tituloPagina = meLanbide47I18n.getMensaje(idiomaUsuario,"label.asociacion.titulo.modifAsociacion");
                }
                else
                {
                    tituloPagina = meLanbide47I18n.getMensaje(idiomaUsuario, "label.asociacion.titulo.nuevaAsociacion");
                }
            }
        %>

        <%!
            // Funcion para escapar strings para javascript
            private String escape(String str) 
            {
                return StringEscapeUtils.escapeJavaScript(str);
            }
        %>
        <title><%=tituloPagina%></title>

        
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/estilo.css"/>
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/extension/melanbide47/melanbide47.css"/>

        <script type="text/javascript" src="<c:url value='/scripts/general.js'/>"></script>
        <script type="text/javascript">
            var APP_CONTEXT_PATH = '<%=request.getContextPath()%>';
        </script>
        <script type="text/javascript" src="<c:url value='/scripts/calendario.js'/>"></script>
        <script type="text/javascript" src="<c:url value='/scripts/validaciones.js'/>"></script>
        <script type="text/javascript" src="<c:url value='/scripts/listaComboBox.js'/>"></script>
        <script type="text/javascript" src="<c:url value='/scripts/popup.js'/>"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/melanbide47/moment-with-locales.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/melanbide47/ori14Utils.js"></script>
        
        <script type="text/javascript">
            var mensajeValidacion = '';
            var nuevo = true;
    
            function getXMLHttpRequest(){
                var aVersions = [ "MSXML2.XMLHttp.5.0",
                    "MSXML2.XMLHttp.4.0","MSXML2.XMLHttp.3.0",
                    "MSXML2.XMLHttp","Microsoft.XMLHttp"
                    ];

                if (window.XMLHttpRequest){
                    // para IE7, Mozilla, Safari, etc: que usen el objeto nativo
                    return new XMLHttpRequest();
                }else if (window.ActiveXObject){
                    // de lo contrario utilizar el control ActiveX para IE5.x y IE6.x
                    for (var i = 0; i < aVersions.length; i++) {
                        try {
                            var oXmlHttp = new ActiveXObject(aVersions[i]);
                            return oXmlHttp;
                        }catch (error) {
                        //no necesitamos hacer nada especial
                        }
                    }
                }else{
                    return null;
                }
            }
            
            function inicio(){
                <%
                if(asociacionModif != null)
                {
                    SimpleDateFormat format = new SimpleDateFormat(ConstantesMeLanbide47.FORMATO_FECHA);
                %>
                        //Los textarea se inicializan directamente en la etiqueta, para evitar problemas con los saltos de linea
                    nuevo = false;
                    
                    //Datos asociacion
                    document.getElementById('cif').value = '<%=asociacionModif.getOriAsocCif() != null ? asociacionModif.getOriAsocCif(): "" %>';
                    document.getElementById('nombre').value = '<%=asociacionModif.getOriAsocNombre() != null ? asociacionModif.getOriAsocNombre(): "" %>';
                    
                    <%
                    if(asociacionModif.getOriEntSupramun() != null && asociacionModif.getOriEntSupramun().equals(1))
                    {
                    %>
                        document.getElementById('supramunS').checked = 'true';                                
                    <%
                    }
                    else if(asociacionModif.getOriEntSupramun() != null && asociacionModif.getOriEntSupramun().equals(0))
                    {
                    %>
                        document.getElementById('supramunN').checked = 'true'; 
                    <%
                    }
                    %>

                    <%
                    if(asociacionModif.getOriEntAdmLocal() != null && asociacionModif.getOriEntAdmLocal().equals(1))
                    {
                    %>
                        document.getElementById('admLocalS').checked = 'true';                                
                    <%
                    }
                    else if(asociacionModif.getOriEntAdmLocal() != null && asociacionModif.getOriEntAdmLocal().equals(0))
                    {
                    %>
                        document.getElementById('admLocalN').checked = 'true'; 
                    <%
                    }
                    %>

                    <%
                    if(asociacionModif.getOriExpCentrofpPub() != null && asociacionModif.getOriExpCentrofpPub().equals(1))
                    {
                    %>
                        document.getElementById('centrofpPubS').checked = 'true';                                
                    <%
                    }
                    else if(asociacionModif.getOriExpCentrofpPub() != null && asociacionModif.getOriExpCentrofpPub().equals(0))
                    {
                    %>
                        document.getElementById('centrofpPubN').checked = 'true'; 
                    <%
                    }
                    %>

                    <%
                    if(asociacionModif.getOriExpCentrofpPriv() != null && asociacionModif.getOriExpCentrofpPriv().equals(1))
                    {
                    %>
                        document.getElementById('centrofpPrivS').checked = 'true';                                
                    <%
                    }
                    else if(asociacionModif.getOriExpCentrofpPriv() != null && asociacionModif.getOriExpCentrofpPriv().equals(0))
                    {
                    %>
                        document.getElementById('centrofpPrivN').checked = 'true'; 
                    <%
                    }
                    %>

                    
                <%
                }

                if(consulta == true)
                {
                %>
                    //Deshabilito todos los campos
                    
                    //Datos asociacion
                    document.getElementById('cif').readOnly = true;
                    document.getElementById('cif').className = 'inputTexto readOnly';
                    document.getElementById('nombre').readOnly = true;
                    document.getElementById('nombre').className = 'inputTexto readOnly';
                    document.getElementById('supramunS').readOnly = true;
                    document.getElementById('supramunN').readOnly = true;
                    document.getElementById('admLocalS').readOnly = true;
                    document.getElementById('admLocalN').readOnly = true;
                    document.getElementById('centrofpPubS').readOnly = true;
                    document.getElementById('centrofpPubN').readOnly = true;
                    document.getElementById('centrofpPrivS').readOnly = true;
                    document.getElementById('centrofpPrivN').readOnly = true;
                <%
                }
                %>
                   
                resizeForFF();
            }
        
            function resizeForFF(){
                if(navigator.appName.indexOf("Internet Explorer")==-1){
                    document.getElementById('cuerpoNuevaAsociacion').style.width = '99%';
                    document.getElementById('divCuerpo').style.width = '98%';
                    document.getElementById('fieldsetDatosAsociacion').style.width = '98%';
                }
            }
            
            function cerrarVentana(){
                if(navigator.appName=='Microsoft Internet Explorer') { 
                      window.parent.window.opener=null; 
                      window.parent.window.close(); 
                } else if(navigator.appName=="Netscape") { 
                      top.window.opener = top; 
                      top.window.open('','_parent',''); 
                      top.window.close(); 
                 }else{
                     window.close(); 
                 } 
            }
            
            function cancelar(){
                var resultado = jsp_alerta('','<%=meLanbide47I18n.getMensaje(idiomaUsuario, "msg.preguntaCancelar")%>');
                if (resultado == 1){
                    cerrarVentana();
                }
            }
            
            function guardar(){
                if(validarDatos()){
                    document.getElementById('msgGuardandoDatos').style.display="inline";
                    barraProgresoOri14('on', 'barraProgresoNuevaAsociacion');
                    var ajax = getXMLHttpRequest();
                    var nodos = null;
                    var CONTEXT_PATH = '<%=request.getContextPath()%>'
                    var url = CONTEXT_PATH + "/PeticionModuloIntegracion.do";
                    var parametros = "";
                    var control = new Date();
                    parametros = 'tarea=preparar&modulo=MELANBIDE47&operacion=guardarAsociacion&tipo=0&numero=<%=numExpediente%>'
                        +'&codAsociacion=<%=asociacionModif != null && asociacionModif.getOriAsocCod() != null ? asociacionModif.getOriAsocCod() : ""%>'
                        +'&cif='+document.getElementById('cif').value
                        +'&nombre='+document.getElementById('nombre').value
                        +'&supramun='+(document.getElementById('supramunS').checked ? 1 : document.getElementById('supramunN').checked ? 0 : null)
                        +'&admLocal='+(document.getElementById('admLocalS').checked ? 1 : document.getElementById('admLocalN').checked ? 0 : null)
                        +'&centrofpPub='+(document.getElementById('centrofpPubS').checked ? 1 : document.getElementById('centrofpPubN').checked ? 0 : null)
                        +'&centrofpPriv='+(document.getElementById('centrofpPrivS').checked ? 1 : document.getElementById('centrofpPrivN').checked ? 0 : null)
                        +'&codigoEntidad='+document.getElementById('codigoEntidad').value
                        +'&nombreEntidad='+document.getElementById('nombreEntidad').value
                        +'&control='+control.getTime();
                    try{
                        ajax.open("POST",url,false);
                        ajax.setRequestHeader("Content-Type","application/x-www-form-urlencoded; charset=iso-8859-15");
                        ajax.setRequestHeader("Accept", "text/xml, application/xml, text/plain");     
                        //var formData = new FormData(document.getElementById('formContrato'));
                        ajax.send(parametros);
                        if (ajax.readyState==4 && ajax.status==200){
                            var xmlDoc = null;
                            if(navigator.appName.indexOf("Internet Explorer")!=-1){
                                // En IE el XML viene en responseText y no en la propiedad responseXML
                                var text = ajax.responseText;
                                xmlDoc=new ActiveXObject("Microsoft.XMLDOM");
                                xmlDoc.async="false";
                                xmlDoc.loadXML(text);
                            }else{
                                // En el resto de navegadores el XML se recupera de la propiedad responseXML
                                xmlDoc = ajax.responseXML;
                            }//if(navigator.appName.indexOf("Internet Explorer")!=-1)
                        }//if (ajax.readyState==4 && ajax.status==200)
                        nodos = xmlDoc.getElementsByTagName("RESPUESTA");
                        var elemento = nodos[0];
                    var hijos = elemento.childNodes;
                    var codigoOperacion = null;
                    var codigoEntidad = null;
                    var listaAsociaciones = new Array();
                    var fila = new Array();
                    var nodoFila;
                    var hijosFila;
                    var nodoCampo;
                    var j;

                    for(j=0;hijos!=null && j<hijos.length;j++){
                        if(hijos[j].nodeName=="CODIGO_OPERACION"){                            
                            codigoOperacion = hijos[j].childNodes[0].nodeValue;
                            listaAsociaciones[j] = codigoOperacion;
                        }else if(hijos[j].nodeName=="CODIGO_ENTIDAD"){
                            codigoEntidad = hijos[j].childNodes[0].nodeValue;
                            listaAsociaciones[j] = codigoEntidad;
                        }else if(hijos[j].nodeName=="ASOCIACION"){
                            nodoFila = hijos[j];
                            hijosFila = nodoFila.childNodes;
                            for(var cont = 0; cont < hijosFila.length; cont++){
                                if(hijosFila[cont].nodeName=="COD_ENTIDAD"){
                                    nodoCampo = hijosFila[cont];
                                    if(nodoCampo.childNodes.length > 0){
                                        fila[0] = nodoCampo.childNodes[0].nodeValue;
                                    }
                                    else{
                                        fila[0] = '-';
                                    }
                                }
                                if(hijosFila[cont].nodeName=="COD_ASOCIACION"){
                                    nodoCampo = hijosFila[cont];
                                    if(nodoCampo.childNodes.length > 0){
                                        fila[1] = nodoCampo.childNodes[0].nodeValue;
                                    }
                                    else{
                                        fila[1] = '-';
                                    }
                                }
                                else if(hijosFila[cont].nodeName=="CIF"){
                                    nodoCampo = hijosFila[cont];
                                    if(nodoCampo.childNodes.length > 0){
                                        fila[2] = nodoCampo.childNodes[0].nodeValue;
                                    }
                                    else{
                                        fila[2] = '-';
                                    }
                                }
                                else if(hijosFila[cont].nodeName=="NOMBRE"){
                                    nodoCampo = hijosFila[cont];
                                    if(nodoCampo.childNodes.length > 0){
                                        fila[3] = nodoCampo.childNodes[0].nodeValue;
                                    }
                                    else{
                                        fila[3] = '-';
                                    }
                                }
                                else if(hijosFila[cont].nodeName=="SUPRAMUN"){
                                    nodoCampo = hijosFila[cont];
                                    if(nodoCampo.childNodes.length > 0){
                                        fila[4] = nodoCampo.childNodes[0].nodeValue;
                                        if (fila[4] == '1') {
                                           fila[4] = '<%=meLanbide47I18n.getMensaje(idiomaUsuario, "label.si").toUpperCase()%>';
                                       } else if (fila[4] == '0') {
                                           fila[4] = '<%=meLanbide47I18n.getMensaje(idiomaUsuario, "label.no").toUpperCase()%>';
                                       }
                                    }
                                    else{
                                        fila[4] = '-';
                                    }
                                }
                                else if(hijosFila[cont].nodeName=="ADM_LOCAL"){
                                    nodoCampo = hijosFila[cont];
                                    if(nodoCampo.childNodes.length > 0){
                                        fila[5] = nodoCampo.childNodes[0].nodeValue;
                                        if (fila[5] == '1') {
                                           fila[5] = '<%=meLanbide47I18n.getMensaje(idiomaUsuario, "label.si").toUpperCase()%>';
                                       } else if (fila[5] == '0') {
                                           fila[5] = '<%=meLanbide47I18n.getMensaje(idiomaUsuario, "label.no").toUpperCase()%>';
                                       }
                                    }
                                    else{
                                        fila[5] = '-';
                                    }
                                }
                                else if(hijosFila[cont].nodeName=="CENTROFP_PUB"){
                                    nodoCampo = hijosFila[cont];
                                    if(nodoCampo.childNodes.length > 0){
                                        fila[6] = nodoCampo.childNodes[0].nodeValue;
                                        if (fila[6] == '1') {
                                           fila[6] = '<%=meLanbide47I18n.getMensaje(idiomaUsuario, "label.si").toUpperCase()%>';
                                       } else if (fila[6] == '0') {
                                           fila[6] = '<%=meLanbide47I18n.getMensaje(idiomaUsuario, "label.no").toUpperCase()%>';
                                       }
                                    }
                                    else{
                                        fila[6] = '-';
                                    }
                                }
                                else if(hijosFila[cont].nodeName=="CENTROFP_PRIV"){
                                    nodoCampo = hijosFila[cont];
                                    if(nodoCampo.childNodes.length > 0){
                                        fila[7] = nodoCampo.childNodes[0].nodeValue;
                                        if (fila[7] == '1') {
                                           fila[7] = '<%=meLanbide47I18n.getMensaje(idiomaUsuario, "label.si").toUpperCase()%>';
                                       } else if (fila[7] == '0') {
                                           fila[7] = '<%=meLanbide47I18n.getMensaje(idiomaUsuario, "label.no").toUpperCase()%>';
                                       }
                                    }
                                    else{
                                        fila[7] = '-';
                                    }
                                }
                            }
                            listaAsociaciones[j] = fila;
                            fila = new Array();
                        }   
                    }//for(j=0;hijos!=null && j<hijos.length;j++)
                        if(codigoOperacion=="0"){
                            window.returnValue =  listaAsociaciones;
                            barraProgresoOri14('off', 'barraProgresoNuevaAsociacion');
                            jsp_alerta("A",'<%=meLanbide47I18n.getMensaje(idiomaUsuario,"msg.datosGuardadosOK")%>');
                            cerrarVentana();
                        }else if(codigoOperacion=="1"){
                            jsp_alerta("A",'<%=meLanbide47I18n.getMensaje(idiomaUsuario,"error.errorBD")%>');
                        }else if(codigoOperacion=="2"){
                            jsp_alerta("A",'<%=meLanbide47I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                        }else if(codigoOperacion=="3"){
                            jsp_alerta("A",'<%=meLanbide47I18n.getMensaje(idiomaUsuario,"error.pasoParametros")%>');
                        }else if(codigoOperacion=="4"){
                            jsp_alerta("A",'<%=meLanbide47I18n.getMensaje(idiomaUsuario,"error.datosNoValidos")%>');
                        }else if(codigoOperacion=="5"){
                            jsp_alerta("A",'<%=meLanbide47I18n.getMensaje(idiomaUsuario,"msg.asociacionRepetida")%>');
                        }else{
                            jsp_alerta("A",'<%=meLanbide47I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                        }//if(
                    }
                    catch(Err){
                        jsp_alerta("A",'<%=meLanbide47I18n.getMensaje(idiomaUsuario,"error.errorProcesarRespuesta")%>');
                    }//try-catch

                    barraProgresoOri14('off', 'barraProgresoNuevaAsociacion');
                }else{
                    jsp_alerta("A", escape(mensajeValidacion));
                }
            }
            
            function validarDatos(){
                mensajeValidacion = '';
                var correcto = true;
                var valor = document.getElementById('cif').value;
                if(!comprobarCaracteresEspecialesOri14(valor)){
                    mensajeValidacion = '<%=meLanbide47I18n.getMensaje(idiomaUsuario, "msg.asociacion.cif.caracteresNoPermitidos")%>';
                    return false;
                }else if(!validarCIFOri14(valor) && !validarNifOri14(valor) && !validarNieOri14(valor)){
                    mensajeValidacion = '<%=meLanbide47I18n.getMensaje(idiomaUsuario, "msg.asociacion.cif.cifIncorrecto")%>';
                    return false;
                }
                
                valor = document.getElementById('nombre').value;
                if(!comprobarCaracteresEspecialesOri14(valor)){
                    mensajeValidacion = '<%=meLanbide47I18n.getMensaje(idiomaUsuario, "msg.asociacion.nombre.caracteresNoPermitidos")%>';
                    return false;
                }
                
                if (!document.getElementById('supramunS').checked && !document.getElementById('supramunN').checked) {
                        mensajeValidacion = '<%=meLanbide47I18n.getMensaje(idiomaUsuario, "msg.asociacion.supramun.obligatorio")%>';
                        return false;
                }
                
                if (!document.getElementById('admLocalS').checked && !document.getElementById('admLocalN').checked) {
                        mensajeValidacion = '<%=meLanbide47I18n.getMensaje(idiomaUsuario, "msg.asociacion.admLocal.obligatorio")%>';
                        return false;
                }
                
                if (!document.getElementById('centrofpPubS').checked && !document.getElementById('centrofpPubN').checked) {
                        mensajeValidacion = '<%=meLanbide47I18n.getMensaje(idiomaUsuario, "msg.asociacion.centrofpPub.obligatorio")%>';
                        return false;
                }
                
                if (!document.getElementById('centrofpPrivS').checked && !document.getElementById('centrofpPrivN').checked) {
                        mensajeValidacion = '<%=meLanbide47I18n.getMensaje(idiomaUsuario, "msg.asociacion.centrofpPriv.obligatorio")%>';
                        return false;
                }
                return correcto;
            }
            
            function cambioCentrofpEntidadOri14(id){
                if(id == 'centrofpPubS'){
                    document.getElementById('centrofpPrivN').checked = true;
                }else if(id == 'centrofpPrivS'){
                    document.getElementById('centrofpPubN').checked = true;
                }
            }
            
        </script>
    </head>
    <body id="cuerpoNuevaAsociacion" style="text-align: left;" class="contenidoPantalla">
        <div id="divCuerpo" style="overflow-y: auto; padding-left: 10px; padding-right: 10px;">
        <form  id="formNuevaAsociacion">
                <div id="barraProgresoNuevaAsociacion" style="position: absolute; z-index:10; visibility: hidden; top: 30%; left: 30%;">
                    <table width="100%" border="0px" cellpadding="0px" cellspacing="0px" border="0px">
                        <tr>
                            <td align="center" valign="middle">
                                <table class="contenedorHidepage" cellpadding="0px" cellspacing="0px" border="0px">
                                    <tr>
                                        <td>
                                            <table width="349px" height="100%">
                                                <tr>
                                                    <td colspan="3" style="height:70%;text-align:center;valign:middle;">
                                                        <span id="msgGuardandoDatos">
                                                            <%=meLanbide47I18n.getMensaje(idiomaUsuario, "msg.guardandoDatos")%>
                                                        </span>
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td style="width:5%;height:20%;"></td>
                                                    <td class="imagenHide"></td>
                                                    <td style="width:5%;height:20%;"></td>
                                                </tr>
                                                <tr>
                                                    <td colspan="3" style="height:10%" ></td>
                                                </tr>
                                            </table>
                                        </td>
                                    </tr>
                                </table>
                            </td>
                        </tr>
                    </table>
                </div>
                <fieldset id="fieldsetDatosAsociacion" name="fieldsetDatosAsociacion" style="width: 100%;">
                    <legend class="legendAzul"><%=meLanbide47I18n.getMensaje(idiomaUsuario,"legend.asociacion.datosAsociacion")%></legend>
                    <div class="lineaFormulario">
                        <div style="float: left; width: 60px;">
                            <%=meLanbide47I18n.getMensaje(idiomaUsuario,"label.asociacion.cif")%>
                        </div>
                        <div>
                            <input type="text" maxlength="15" size="20" id="cif" name="cif" value="" class="inputTexto"/>
                        </div>
                    </div>
                    <div class="lineaFormulario">
                        <div style="float: left; width: 60px;">
                            <%=meLanbide47I18n.getMensaje(idiomaUsuario,"label.asociacion.nombre")%>
                        </div>
                        <div>
                            <input type="text" maxlength="500" size="60" id="nombre" name="nombre" value="" class="inputTexto"/>
                        </div>
                    </div>
                    <div class="lineaFormulario">
                        <div style="float: left; width: 310px;">
                            <%=meLanbide47I18n.getMensaje(idiomaUsuario,"label.asociacion.pregunta.supramun")%>
                        </div>
                        <div style="width: auto; float: left; margin-left: 10px; margin-right: 42px;">
                            <div style="float: left;">
                                <input type="radio" name="supramun" id="supramunS" value="S"><%=meLanbide47I18n.getMensaje(idiomaUsuario, "label.si")%>
                                <input type="radio" name="supramun" id="supramunN" value="N"><%=meLanbide47I18n.getMensaje(idiomaUsuario, "label.no")%>
                            </div>
                        </div>
                    </div>
                    <div class="lineaFormulario">
                        <div style="float: left; width: 310px;">
                            <%=meLanbide47I18n.getMensaje(idiomaUsuario,"label.asociacion.pregunta.admlocal")%>
                        </div>
                        <div style="width: auto; float: left; margin-left: 10px;">
                            <div style="float: left;">
                                <input type="radio" name="admLocal" id="admLocalS" value="S"><%=meLanbide47I18n.getMensaje(idiomaUsuario, "label.si")%>
                                <input type="radio" name="admLocal" id="admLocalN" value="N"><%=meLanbide47I18n.getMensaje(idiomaUsuario, "label.no")%>
                            </div>
                        </div>
                    </div>
                    <div class="lineaFormulario">
                        <div style="float: left; width: 310px;">
                            <%=meLanbide47I18n.getMensaje(idiomaUsuario,"label.asociacion.pregunta.centrofpPub")%>
                        </div>
                        <div style="width: auto; float: left; margin-left: 10px;">
                            <div style="float: left;">
                                <input type="radio" name="centrofpPub" id="centrofpPubS" value="S" onclick="cambioCentrofpEntidadOri14(this.id);"><%=meLanbide47I18n.getMensaje(idiomaUsuario, "label.si")%>
                                <input type="radio" name="centrofpPub" id="centrofpPubN" value="N"><%=meLanbide47I18n.getMensaje(idiomaUsuario, "label.no")%>
                            </div>
                        </div>
                    </div>
                    <div class="lineaFormulario">
                        <div style="float: left; width: 310px;">
                            <%=meLanbide47I18n.getMensaje(idiomaUsuario,"label.asociacion.pregunta.centrofpPriv")%>
                        </div>
                        <div style="width: auto; float: left; margin-left: 10px;">
                            <div style="float: left;">
                                <input type="radio" name="centrofpPriv" id="centrofpPrivS" value="S" onclick="cambioCentrofpEntidadOri14(this.id);"><%=meLanbide47I18n.getMensaje(idiomaUsuario, "label.si")%>
                                <input type="radio" name="centrofpPriv" id="centrofpPrivN" value="N"><%=meLanbide47I18n.getMensaje(idiomaUsuario, "label.no")%>
                            </div>
                        </div>
                    </div>
                </fieldset>
                <div class="botonera" style="margin-top: 25px;">
                    <input type="button" id="btnGuardarAsociacion" name="btnGuardarAsociacion" class="botonGeneral" value="<%=meLanbide47I18n.getMensaje(idiomaUsuario,"btn.aceptar")%>" onclick="guardar();">
                    <input type="button" id="btnCancelarAsociacion" name="btnCancelarAsociacion" class="botonGeneral" value="<%=meLanbide47I18n.getMensaje(idiomaUsuario,"btn.cancelar")%>" onclick="cancelar();">
                    <input type="button" id="btnCerrar" name="btnCerrar" class="botonGeneral" value="<%=meLanbide47I18n.getMensaje(idiomaUsuario,"btn.cerrar")%>" onclick="cerrarVentana();" style="display: none;">
                </div>
        </form>
        <div id="popupcalendar" class="text"></div>
        <input type="hidden" id="codigoEntidad" name="codigoEntidad" value="<%=request.getParameter("codigoEntidad")%>"/>
        <input type="hidden" id="nombreEntidad" name="nombreEntidad" value="<%=request.getParameter("nombreEntidad")%>"/>
        </div>
        <script type="text/javascript">
            inicio();
        </script>
    </body>
</html>
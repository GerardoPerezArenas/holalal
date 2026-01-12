<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>

<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide32.i18n.MeLanbide32I18n" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide32.vo.procesos.FilaAuditoriaProcesosVO" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide32.util.ConstantesMeLanbide32" %>
<%@page import="java.util.List" %>

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
	 apl = usuario.getAppCod();
	 css = usuario.getCss();
                codOrganizacion  = usuario.getOrgCod();
            }
        }
    }
    catch(Exception ex)
    {
        
    }
    MeLanbide32I18n meLanbide32I18n = MeLanbide32I18n.getInstance();
    String numExpediente    = request.getParameter("numero");
    
    String codProcedimiento = request.getParameter("codProc");
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
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/extension/melanbide32/melanbide32.css"/>

<script type="text/javascript">
    var APP_CONTEXT_PATH = '<%=request.getContextPath()%>';
</script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/general.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/popup.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/TablaNueva.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/calendario.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/validaciones.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/listaComboBox.js"></script>

<script type="text/javascript">
    var msgValidacion = '';
    
    function ejecutarResolucionProvisionalHoras(){
        var ajax = getXMLHttpRequest();
        var nodos = null;
        var CONTEXT_PATH = '<%=request.getContextPath()%>'
        var url = CONTEXT_PATH + "/PeticionModuloIntegracion.do";
        var parametros = "";
        var control = new Date();
            lanzarPopUpModal('<%=request.getContextPath()%>/jsp/extension/melanbide32/procesos/solicitarAno.jsp?idioma=<%=idiomaUsuario%>&control='+control.getTime(),250,300,'no','no', function(ano){
        if(ano != null && ano != undefined && ano != ''){
            parametros = 'tarea=preparar&modulo=MELANBIDE32&operacion=adjudicaOrientacion&tipo=0&codProcedimiento=<%=codProcedimiento%>&ano='+ano+'&control='+control.getTime();
            try{
                ajax.open("POST",url,false);
                ajax.setRequestHeader("Content-Type","application/x-www-form-urlencoded; charset=ISO-8859-1");
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
                var mensaje = null;
                var estado = '';
                for(j=0;hijos!=null && j<hijos.length;j++){
                    if(hijos[j].nodeName=="CODIGO_OPERACION"){                            
                        codigoOperacion = hijos[j].childNodes[0].nodeValue;
                    }//if(hijos[j].nodeName=="CODIGO_OPERACION")                      
                    else if(hijos[j].nodeName=="MENSAJE"){
                        mensaje = hijos[j].childNodes[0].nodeValue;
                    }                      
                    else if(hijos[j].nodeName=="ESTADO"){
                        estado = hijos[j].childNodes[0].nodeValue;
                    }  
                }//for(j=0;hijos!=null && j<hijos.length;j++)
                if(codigoOperacion == "0"){
                    jsp_alerta("A",'<%=meLanbide32I18n.getMensaje(idiomaUsuario,"msg.procesoOk")%>'+'. \n'+estado);
                }else if(codigoOperacion=="1"){
                    jsp_alerta("A",'<%=meLanbide32I18n.getMensaje(idiomaUsuario,"error.errorBD")%>'+'\n'+mensaje+'. \n'+estado);
                }else if(codigoOperacion=="2"){
                    jsp_alerta("A",'<%=meLanbide32I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                }else if(codigoOperacion=="3"){
                    jsp_alerta("A",'<%=meLanbide32I18n.getMensaje(idiomaUsuario,"error.pasoParametros")%>');
                }else if(codigoOperacion=="4"){
                    jsp_alerta("A",mensaje);
                }else{
                    jsp_alerta("A",'<%=meLanbide32I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                }//if(
            }
            catch(Err){

            }//try-catch
        }
        inicio = false;
        cargaPaginaE(paginaActualE);
            });          
    }
    
    function generarDocumentacionResolucionHoras(){
        var control = new Date();    
        lanzarPopUpModal('<%=request.getContextPath()%>/jsp/extension/melanbide32/procesos/solicitarAno.jsp?idioma=<%=idiomaUsuario%>&control='+control.getTime(),250,300,'no','no', function(ano){
        if(ano != null && ano != undefined && ano != ''){
            var ajax = getXMLHttpRequest();
            var nodos = null;
            var CONTEXT_PATH = '<%=request.getContextPath()%>'
            var url = CONTEXT_PATH + "/PeticionModuloIntegracion.do";
            var parametros = "";
            var control = new Date();
            parametros = '?tarea=preparar&modulo=MELANBIDE32&operacion=crearDocumentacionResolucionHoras&tipo=0&ano='+ano+'&codProcedimiento=<%=codProcedimiento%>&control='+control.getTime();
            inicio = false;
            window.open(url+parametros);
        }
        inicio = false;
        cargaPaginaE(paginaActualE);
        });
    }
    
    function consolidarHoras(){
        var ajax = getXMLHttpRequest();
        var nodos = null;
        var CONTEXT_PATH = '<%=request.getContextPath()%>'
        var url = CONTEXT_PATH + "/PeticionModuloIntegracion.do";
        var parametros = "";
        var control = new Date();
        parametros = 'tarea=preparar&modulo=MELANBIDE32&operacion=consolidaHoras&tipo=0&codProcedimiento=<%=codProcedimiento%>&control='+control.getTime();
        try{
            ajax.open("POST",url,false);
            ajax.setRequestHeader("Content-Type","application/x-www-form-urlencoded; charset=ISO-8859-1");
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
            var mensaje = null;
            for(j=0;hijos!=null && j<hijos.length;j++){
                if(hijos[j].nodeName=="CODIGO_OPERACION"){                            
                    codigoOperacion = hijos[j].childNodes[0].nodeValue;
                }//if(hijos[j].nodeName=="CODIGO_OPERACION")                      
                else if(hijos[j].nodeName=="MENSAJE"){
                    mensaje = hijos[j].childNodes[0].nodeValue;
                }   
            }//for(j=0;hijos!=null && j<hijos.length;j++)
            if(codigoOperacion == "0"){
                jsp_alerta("A",'<%=meLanbide32I18n.getMensaje(idiomaUsuario,"msg.procesoOk")%>');
            }else if(codigoOperacion=="1"){
                jsp_alerta("A",'<%=meLanbide32I18n.getMensaje(idiomaUsuario,"error.errorBD")%>'+'\n'+mensaje);
            }else if(codigoOperacion=="2"){
                jsp_alerta("A",'<%=meLanbide32I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
            }else if(codigoOperacion=="3"){
                jsp_alerta("A",'<%=meLanbide32I18n.getMensaje(idiomaUsuario,"error.pasoParametros")%>');
            }else{
                jsp_alerta("A",'<%=meLanbide32I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
            }//if(
        }
        catch(Err){
            jsp_alerta("A",'<%=meLanbide32I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
        }//try-catch
        inicio = false;
        cargaPaginaE(paginaActualE);
    }
    
    function deshacerConsolidacionHoras(){
        var ajax = getXMLHttpRequest();
        var nodos = null;
        var CONTEXT_PATH = '<%=request.getContextPath()%>'
        var url = CONTEXT_PATH + "/PeticionModuloIntegracion.do";
        var parametros = "";
        var control = new Date();
        lanzarPopUpModal('<%=request.getContextPath()%>/jsp/extension/melanbide32/procesos/solicitarAno.jsp?idioma=<%=idiomaUsuario%>&control='+control.getTime(),250,300,'no','no', function(ano){
        if(ano != null && ano != undefined && ano != ''){
            parametros = 'tarea=preparar&modulo=MELANBIDE32&operacion=deshacerConsolidacionHoras&tipo=0&ano='+ano+'&codProcedimiento=<%=codProcedimiento%>&control='+control.getTime();
            try{
                ajax.open("POST",url,false);
                ajax.setRequestHeader("Content-Type","application/x-www-form-urlencoded; charset=ISO-8859-1");
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
                var mensaje = null;
                for(j=0;hijos!=null && j<hijos.length;j++){
                    if(hijos[j].nodeName=="CODIGO_OPERACION"){                            
                        codigoOperacion = hijos[j].childNodes[0].nodeValue;
                    }//if(hijos[j].nodeName=="CODIGO_OPERACION")                      
                    else if(hijos[j].nodeName=="MENSAJE"){
                        mensaje = hijos[j].childNodes[0].nodeValue;
                    }   
                }//for(j=0;hijos!=null && j<hijos.length;j++)
                if(codigoOperacion == "0"){
                    jsp_alerta("A",'<%=meLanbide32I18n.getMensaje(idiomaUsuario,"msg.procesoOk")%>');
                }else if(codigoOperacion=="1"){
                    jsp_alerta("A",'<%=meLanbide32I18n.getMensaje(idiomaUsuario,"error.errorBD")%>'+'\n'+mensaje);
                }else if(codigoOperacion=="2"){
                    jsp_alerta("A",'<%=meLanbide32I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                }else if(codigoOperacion=="3"){
                    jsp_alerta("A",'<%=meLanbide32I18n.getMensaje(idiomaUsuario,"error.pasoParametros")%>');
                }else{
                    jsp_alerta("A",'<%=meLanbide32I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                }//if(
            }
            catch(Err){

            }//try-catch
        }
        inicio = false;
        cargaPaginaE(paginaActualE);
        });
    }
    
    function ejecutarResolucionProvisionalCentros(){
        var ajax = getXMLHttpRequest();
        var nodos = null;
        var CONTEXT_PATH = '<%=request.getContextPath()%>'
        var url = CONTEXT_PATH + "/PeticionModuloIntegracion.do";
        var parametros = "";
        var control = new Date();
        lanzarPopUpModal('<%=request.getContextPath()%>/jsp/extension/melanbide32/procesos/solicitarAno.jsp?idioma=<%=idiomaUsuario%>&control='+control.getTime(),250,300,'no','no', function(ano){
        if(ano != null && ano != undefined && ano != ''){
            parametros = 'tarea=preparar&modulo=MELANBIDE32&operacion=adjudicaCentros&tipo=0&ano='+ano+'&codProcedimiento=<%=codProcedimiento%>&control='+control.getTime();
            try{
                ajax.open("POST",url,false);
                ajax.setRequestHeader("Content-Type","application/x-www-form-urlencoded; charset=ISO-8859-1");
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
                var mensaje = null;
                var estado = '';
                for(j=0;hijos!=null && j<hijos.length;j++){
                    if(hijos[j].nodeName=="CODIGO_OPERACION"){                            
                        codigoOperacion = hijos[j].childNodes[0].nodeValue;
                    }//if(hijos[j].nodeName=="CODIGO_OPERACION")                      
                    else if(hijos[j].nodeName=="MENSAJE"){
                        mensaje = hijos[j].childNodes[0].nodeValue;
                    }                       
                    else if(hijos[j].nodeName=="ESTADO"){
                        estado = hijos[j].childNodes[0].nodeValue;
                    }   
                }//for(j=0;hijos!=null && j<hijos.length;j++)
                if(codigoOperacion == "0"){
                    jsp_alerta("A",'<%=meLanbide32I18n.getMensaje(idiomaUsuario,"msg.procesoOk")%>'+'. \n'+estado);
                }else if(codigoOperacion=="1"){
                    jsp_alerta("A",'<%=meLanbide32I18n.getMensaje(idiomaUsuario,"error.errorBD")%>'+'\n'+mensaje+'. \n'+estado);
                }else if(codigoOperacion=="2"){
                    jsp_alerta("A",'<%=meLanbide32I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                }else if(codigoOperacion=="3"){
                    jsp_alerta("A",'<%=meLanbide32I18n.getMensaje(idiomaUsuario,"error.pasoParametros")%>');
                }else if(codigoOperacion=="4"){
                    jsp_alerta("A",mensaje);
                }else{
                    jsp_alerta("A",'<%=meLanbide32I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                }//if(
            }
            catch(Err){

            }//try-catch
        }
        inicio = false;
        cargaPaginaE(paginaActualE);
        });
    }
    
    function ejecutarListadoAltaCentros(){
        var control = new Date();    
        lanzarPopUpModal('<%=request.getContextPath()%>/jsp/extension/melanbide32/procesos/solicitarAno.jsp?idioma=<%=idiomaUsuario%>&control='+control.getTime(),250,300,'no','no', function(ano){
        if(ano != null && ano != undefined && ano != ''){
            var ajax = getXMLHttpRequest();
            var nodos = null;
            var CONTEXT_PATH = '<%=request.getContextPath()%>'
            var url = CONTEXT_PATH + "/PeticionModuloIntegracion.do";
            var parametros = "";
            var control = new Date();
            parametros = '?tarea=preparar&modulo=MELANBIDE32&operacion=generarListadoAltaCentros&tipo=0&ano='+ano+'&codProcedimiento=<%=codProcedimiento%>&control='+control.getTime();
            inicio = false;
            window.open(url+parametros);
        }
        inicio = false;
        cargaPaginaE(paginaActualE);
        });
    }
    
    function generarDocumentacionResolucionCentros(){
        var control = new Date();    
        lanzarPopUpModal('<%=request.getContextPath()%>/jsp/extension/melanbide32/procesos/solicitarAno.jsp?idioma=<%=idiomaUsuario%>&control='+control.getTime(),250,300,'no','no', function(ano){
        if(ano != null && ano != undefined && ano != ''){
            var ajax = getXMLHttpRequest();
            var nodos = null;
            var CONTEXT_PATH = '<%=request.getContextPath()%>'
            var url = CONTEXT_PATH + "/PeticionModuloIntegracion.do";
            var parametros = "";
            var control = new Date();
            parametros = '?tarea=preparar&modulo=MELANBIDE32&operacion=crearDocumentacionResolucionCentros&tipo=0&ano='+ano+'&codProcedimiento=<%=codProcedimiento%>&control='+control.getTime();
            inicio = false;
            window.open(url+parametros);
        }
        inicio = false;
        cargaPaginaE(paginaActualE);
        });
    }
    
    function consolidarCentros(){
        var ajax = getXMLHttpRequest();
        var nodos = null;
        var CONTEXT_PATH = '<%=request.getContextPath()%>'
        var url = CONTEXT_PATH + "/PeticionModuloIntegracion.do";
        var parametros = "";
        var control = new Date();
        parametros = 'tarea=preparar&modulo=MELANBIDE32&operacion=consolidaCentros&tipo=0&codProcedimiento=<%=codProcedimiento%>&control='+control.getTime();
        try{
            ajax.open("POST",url,false);
            ajax.setRequestHeader("Content-Type","application/x-www-form-urlencoded; charset=ISO-8859-1");
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
            var mensaje = null;
            for(j=0;hijos!=null && j<hijos.length;j++){
                if(hijos[j].nodeName=="CODIGO_OPERACION"){                            
                    codigoOperacion = hijos[j].childNodes[0].nodeValue;
                }//if(hijos[j].nodeName=="CODIGO_OPERACION")                      
                else if(hijos[j].nodeName=="MENSAJE"){
                    mensaje = hijos[j].childNodes[0].nodeValue;
                }   
            }//for(j=0;hijos!=null && j<hijos.length;j++)
            if(codigoOperacion == "0"){
                jsp_alerta("A",'<%=meLanbide32I18n.getMensaje(idiomaUsuario,"msg.procesoOk")%>');
            }else if(codigoOperacion=="1"){
                jsp_alerta("A",'<%=meLanbide32I18n.getMensaje(idiomaUsuario,"error.errorBD")%>'+'\n'+mensaje);
            }else if(codigoOperacion=="2"){
                jsp_alerta("A",'<%=meLanbide32I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
            }else if(codigoOperacion=="3"){
                jsp_alerta("A",'<%=meLanbide32I18n.getMensaje(idiomaUsuario,"error.pasoParametros")%>');
            }else{
                jsp_alerta("A",'<%=meLanbide32I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
            }//if(
        }
        catch(Err){
            jsp_alerta("A",'<%=meLanbide32I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
        }//try-catch
        inicio = false;
        cargaPaginaE(paginaActualE);
    }
    
    function deshacerConsolidacionCentros(){
        var ajax = getXMLHttpRequest();
        var nodos = null;
        var CONTEXT_PATH = '<%=request.getContextPath()%>'
        var url = CONTEXT_PATH + "/PeticionModuloIntegracion.do";
        var parametros = "";
        var control = new Date();
        lanzarPopUpModal('<%=request.getContextPath()%>/jsp/extension/melanbide32/procesos/solicitarAno.jsp?idioma=<%=idiomaUsuario%>&control='+control.getTime(),250,300,'no','no', function(ano){
        if(ano != null && ano != undefined && ano != ''){
            parametros = 'tarea=preparar&modulo=MELANBIDE32&operacion=deshacerConsolidacionCentros&tipo=0&ano='+ano+'&codProcedimiento=<%=codProcedimiento%>&control='+control.getTime();
            try{
                ajax.open("POST",url,false);
                ajax.setRequestHeader("Content-Type","application/x-www-form-urlencoded; charset=ISO-8859-1");
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
                var mensaje = null;
                for(j=0;hijos!=null && j<hijos.length;j++){
                    if(hijos[j].nodeName=="CODIGO_OPERACION"){                            
                        codigoOperacion = hijos[j].childNodes[0].nodeValue;
                    }//if(hijos[j].nodeName=="CODIGO_OPERACION")                      
                    else if(hijos[j].nodeName=="MENSAJE"){
                        mensaje = hijos[j].childNodes[0].nodeValue;
                    }   
                }//for(j=0;hijos!=null && j<hijos.length;j++)
                if(codigoOperacion == "0"){
                    jsp_alerta("A",'<%=meLanbide32I18n.getMensaje(idiomaUsuario,"msg.procesoOk")%>');
                }else if(codigoOperacion=="1"){
                    jsp_alerta("A",'<%=meLanbide32I18n.getMensaje(idiomaUsuario,"error.errorBD")%>'+'\n'+mensaje);
                }else if(codigoOperacion=="2"){
                    jsp_alerta("A",'<%=meLanbide32I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                }else if(codigoOperacion=="3"){
                    jsp_alerta("A",'<%=meLanbide32I18n.getMensaje(idiomaUsuario,"error.pasoParametros")%>');
                }else{
                    jsp_alerta("A",'<%=meLanbide32I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                }//if(
            }
            catch(Err){

            }//try-catch
        }
        inicio = false;
        cargaPaginaE(paginaActualE);
    });
    }
    
    function inicializar(){
        window.focus();
        
        //Si el procedimiento es CEMP, entonces hay que ocultar los procesos de Resolucion - Horas
        
        <%
            if(codProcedimiento != null && codProcedimiento.equalsIgnoreCase(ConstantesMeLanbide32.CODIGO_PROCEDIMIENTO_CEMP))
            {
        %>
                document.getElementById('fieldsetResolucionHoras').style.display = 'none';
                if(navigator.appName=="Microsoft Internet Explorer"){
                    document.getElementById('fieldsetResolucionCentros').style.width = '100%';
                }else{
                    document.getElementById('fieldsetResolucionCentros').style.width = '97%';
                }
        <%
            }
        %>
                
        if(navigator.appName=="Microsoft Internet Explorer"){
            document.getElementById('fieldsetOtros').style.width = '100%';
        }
    }
            
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
        
    
    var enlacesPaginaE  = 10;
    var lineasPaginaE   = 12;
    var paginaActualE   = 1;
    var paginaInferiorE = 1;
    var paginaSuperiorE = enlacesPaginaE;
    var listaPE = new Array();
    var listaSelE = new Array();
    var listaSelEOriginal = new Array();
    var inicioE = 0;
    var finE    = 0;
    var numRelacionAuditorias = 0;
    var numeroPaginasE=Math.ceil(numRelacionAuditorias /lineasPaginaE);
    if (numeroPaginasE < enlacesPaginaE) 
        paginaSuperiorE= numeroPaginasE;
    
    //JAVASCRIPT DE LA TABLA AUDITORIA
    function inicializaLista(numeroPaginaE){
        tableObject=tabAud;
        var j = 0;
        var jE = 0;

        paginaActualE = numeroPaginaE;
        listaPE = new Array();

        inicioE =0;
        finE = lineasPaginaE;
        listaPE = listaSelE;

        tabAud.lineas=listaPE;
        refrescaExpedientes();
        domlay('enlaceAud',1,0,0,enlacesAud());

      }

      function enlacesAud() {
        var htmlString = " ";
        numeroPaginasE = Math.ceil(numRelacionAuditorias /lineasPaginaE);
        /*if (numeroPaginasE < enlacesPaginaE) 
            paginaSuperiorE= numeroPaginasE;*/

      if (numeroPaginasE > 1) {
        htmlString += '<table class="fondoNavegacion" cellpadding="2" cellspacing="0" align="center"><tr>'
        if (paginaActualE > 1) {
          if(paginaInferiorE > enlacesPaginaE){
            htmlString += '<td width="35" class="botonNavegacion">';
            htmlString += '<a href="javascript:irPrimeraPaginaE();" class="linkNavegacion" target="_self">';
            htmlString += ' |<< ';
            htmlString += '</a></td>';
            htmlString += '<td width="5"></td>';
            htmlString += '<td width="35" class="botonNavegacion">';
            htmlString += '<a class="linkNavegacion" href="javascript:irNPaginasAnterioresE('+ eval(paginaActualE) + ')" target="_self">';
            htmlString += ' << ';
            htmlString += '</a></td>';
          } else htmlString += '<td width="75">&nbsp;</td>';
        }else htmlString += '<td width="75">&nbsp;</td>';
        htmlString += '</td><td align="center" width="400">';

        for(var i=paginaInferiorE-1; i < paginaSuperiorE; i++){
            if ((i+1) == paginaActualE)
              htmlString += '<span class="indiceNavSelected">'+ (i+1) + '</span>&nbsp;&nbsp;';
            else
              htmlString += '<a class="indiceNavegacion" href="javascript:cargaPaginaE('+ eval(i+1) + ')" target="_self">'+ (i+1) + '</a>&nbsp;&nbsp;';
        }

        if (paginaSuperiorE < numeroPaginasE){
          htmlString += '</td><td width="35" class="botonNavegacion">';
          htmlString += '<a class="linkNavegacion" href="javascript:irNPaginasSiguientesE('+ eval(eval(paginaActualE))+ ')" target="_self">';
          htmlString += ' >> ';
          htmlString += '</a></td>';
          htmlString += '<td width="5"></td>';
          htmlString += '<td width="35" class="botonNavegacion">';
          htmlString += '<a href="javascript:irUltimaPaginaE();" class="linkNavegacion" target="_self">';
          htmlString += ' >>| ';
          htmlString += '</a></td>';
        } else htmlString += '</td><td width="70"></td>';
        htmlString += '</tr></table>';
      }

      var registroInferiorE = ((paginaActualE - 1) * lineasPaginaE) + 1;
      var registroSuperiorE = (paginaActualE * lineasPaginaE);
      if (paginaActualE == numeroPaginasE)
        registroSuperiorE = numRelacionAuditorias;
      if (listaSelE.length > 0)
        htmlString += '<center><font class="textoSuelto">Resultados&nbsp;' + registroInferiorE + '&nbsp;a&nbsp;' + registroSuperiorE + '&nbsp;de&nbsp;' + numRelacionAuditorias + '&nbsp;encontrados.</font></center>'
      else
        htmlString += '<center><font class="textoSuelto">&nbsp;' + numRelacionAuditorias  + '&nbsp;encontrados.</font></center>'

      return (htmlString);
    }
    

    function calcularLimitesE(numeroPaginaE) {
      if(numeroPaginaE > paginaInferiorE +(enlacesPaginaE-1)) {
        paginaInferiorE = numeroPaginaE;
      }
      var enlacePaginaE = Math.ceil(numeroPaginaE/enlacesPaginaE);
      var ultimaPantalla = Math.ceil(numeroPaginasE/enlacesPaginaE);
      var valorMaxPaginaE = 0;
      if(enlacePaginaE == 0) valorMaxPaginaE = enlacesPaginaE;
      else valorMaxPaginaE = enlacePaginaE * enlacesPaginaE;
      if(numeroPaginasE < valorMaxPaginaE && enlacePaginaE == ultimaPantalla) paginaSuperiorE = numeroPaginasE;
      else paginaSuperiorE = (numeroPaginaE-1) + enlacesPaginaE;
    }

    function irNPaginasAnterioresE(pagActualE){
      var incremento = enlacesPaginaE + (pagActualE - paginaInferiorE) ;
        if (paginaInferiorE-1 <= 0)
                    pagActualE = 1;
            else  pagActualE -= incremento;
      paginaInferiorE = pagActualE;
      paginaSuperiorE = paginaInferiorE + enlacesPaginaE-1;

      calcularLimitesE(pagActualE);
      cargaPaginaE(pagActualE);
    }

    function irNPaginasSiguientesE(pagActualE){
      pagActualE = parseInt(pagActualE);
      var incremento = paginaSuperiorE +1 - pagActualE;
      if (pagActualE + incremento > numeroPaginasE)
          pagActualE = Math.ceil(numRelacionAuditorias/lineasPaginaE); // Ultima
      else {
        pagActualE +=  incremento;
        pagInferiorE = pagActualE;
        if (paginaInferiorE + enlacesPaginaE > numeroPaginasE)
            paginaSuperiorE=numeroPaginasE;
        else paginaSuperiorE=paginaInferiorE+enlacesPaginaE-1;
      }

      calcularLimitesE(pagActualE);
      cargaPaginaE(pagActualE);
    }

    function irUltimaPaginaE() {
      paginaActualE   = Math.ceil(numRelacionAuditorias/lineasPaginaE);
      paginaInferiorE = 1;
      if (numeroPaginasE <= enlacesPaginaE)
          paginaSuperiorE = numeroPaginasE;
      else {
        paginaSuperiorE = enlacesPaginaE;
        while (paginaActualE > paginaSuperiorE) {
          paginaInferiorE = paginaSuperiorE +1;
          if (numeroPaginasE > paginaInferiorE-1+enlacesPaginaE)
            paginaSuperiorE = paginaInferiorE-1+enlacesPaginaE;
          else paginaSuperiorE = numeroPaginasE;
         }
      }
      cargaPaginaE(paginaActualE)
    }

    function irPrimeraPaginaE() {
      paginaActualE   = 1;
      paginaInferiorE = 1;
      if (numeroPaginasE <= enlacesPaginaE)
          paginaSuperiorE = numeroPaginasE;
        else paginaSuperiorE = enlacesPaginaE;
      cargaPaginaE(paginaActualE)
    }
    
    

    function cargaPaginaE(numeroPaginaE){
        paginaActualE = numeroPaginaE;
        filtrarAuditorias();
        listaSelE = new Array();
        for(var i = 0; i < listaSelEOriginal.length; i++){
            listaSelE[i] = listaSelEOriginal[i];
        }

        inicializaLista(numeroPaginaE);
    }
    //FIN JAVASCRIPT DE LA TABLA AUDITORIA
    
    function filtrarAuditorias()
    {
        if(validarDatosFiltro()){
            var ajax = getXMLHttpRequest();
            var nodos = null;
            var CONTEXT_PATH = '<%=request.getContextPath()%>'
            var url = CONTEXT_PATH + "/PeticionModuloIntegracion.do";
            var parametros = "";
            var control = new Date();
            var nomApellidos = document.getElementById('nomApellidos').value;
            var feDesde = document.getElementById('fechaDesde').value;
            var feHasta = document.getElementById('fechaHasta').value;
            var codProc = document.getElementById('codProceso').value;
            parametros = 'tarea=preparar&modulo=MELANBIDE32&operacion=filtrarAuditoriaProcesos&tipo=0&numero=<%=numExpediente%>&nomApellidos='+nomApellidos
                +'&feDesde='+feDesde+'&feHasta='+feHasta+'&codProc='+codProc+'&pagAct='+paginaActualE+'&maxFilas='+lineasPaginaE+'&codProcedimiento=<%=codProcedimiento%>&control='+control.getTime();
            try{
                ajax.open("POST",url,false);
                ajax.setRequestHeader("Content-Type","application/x-www-form-urlencoded; charset=ISO-8859-1");
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
                var listaRegistros = new Array();
                var fila = new Array();
                var nodoFila;
                var hijosFila;
                var contFilas = 0;
                for(j=0;hijos!=null && j<hijos.length;j++){
                    if(hijos[j].nodeName=="CODIGO_OPERACION"){                            
                        codigoOperacion = hijos[j].childNodes[0].nodeValue;
                    }//if(hijos[j].nodeName=="CODIGO_OPERACION")   
                    else if(hijos[j].nodeName=="TOTAL_REGISTROS"){
                        try{
                            numRelacionAuditorias = parseInt(hijos[j].childNodes[0].nodeValue);
                        }catch(err){

                        }
                    }
                    else if(hijos[j].nodeName=="REGISTRO"){
                        nodoFila = hijos[j];
                        hijosFila = nodoFila.childNodes;
                        for(var cont = 0; cont < hijosFila.length; cont++){
                            if(hijosFila[cont].nodeName=="NOMAPELLIDOS"){
                                if(hijosFila[cont].childNodes.length > 0){
                                    fila[0] = hijosFila[cont].childNodes[0].nodeValue;
                                }
                                else{
                                    fila[0] = '-';
                                }
                            }
                            else if(hijosFila[cont].nodeName=="PROCESO"){
                                if(hijosFila[cont].childNodes.length > 0){
                                    fila[1] = hijosFila[cont].childNodes[0].nodeValue;
                                }
                                else{
                                    fila[1] = '-';
                                }
                            }
                            else if(hijosFila[cont].nodeName=="FECHA"){
                                if(hijosFila[cont].childNodes.length > 0){
                                    fila[2] = hijosFila[cont].childNodes[0].nodeValue;
                                }
                                else{
                                    fila[2] = '-';
                                }
                            }
                            else if(hijosFila[cont].nodeName=="RESULTADO"){
                                if(hijosFila[cont].childNodes.length > 0){
                                    fila[3] = hijosFila[cont].childNodes[0].nodeValue;
                                }
                                else{
                                    fila[3] = '-';
                                }
                            }
                        }
                        listaRegistros[contFilas] = fila;
                        contFilas++;
                        fila = new Array();
                    }       
                }//for(j=0;hijos!=null && j<hijos.length;j++)
                if(codigoOperacion=="0"){
                    listaSelEOriginal = new Array();
                    for(var i = 0; i < listaRegistros.length; i++){
                        listaSelEOriginal[i] = listaRegistros[i];
                    }


                    if(inicio == false){
                        paginaSuperiorE = enlacesPaginaE;
                        var np = Math.ceil(numRelacionAuditorias /lineasPaginaE);
                        if(paginaSuperiorE > np)
                            paginaSuperiorE = np;
                        inicio = true;
                    }

                }else if(codigoOperacion=="1"){
                    jsp_alerta("A",'<%=meLanbide32I18n.getMensaje(idiomaUsuario,"error.errorBD")%>');
                }else if(codigoOperacion=="2"){
                    jsp_alerta("A",'<%=meLanbide32I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                }else if(codigoOperacion=="3"){
                    jsp_alerta("A",'<%=meLanbide32I18n.getMensaje(idiomaUsuario,"error.pasoParametros")%>');
                }else{
                    jsp_alerta("A",'<%=meLanbide32I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                }//if(
            }
            catch(Err){

            }//try-catch
        }
    }
    
    function mostrarCalFechaDesde(){
        if(window.event) 
        evento = window.event;
        if (document.getElementById("calFechaDesde").src.indexOf("icono.gif") != -1 )
            showCalendar('forms[0]','fechaDesde',null,null,null,'','calFechaDesde','',null,null,null,null,null,null,null,null,evento);
    }
    
    function mostrarCalFechaHasta(){
        if(window.event) 
        evento = window.event;
        if (document.getElementById("calFechaHasta").src.indexOf("icono.gif") != -1 )
            showCalendar('forms[0]','fechaHasta',null,null,null,'','calFechaHasta','',null,null,null,null,null,null,null,null,evento);
    }
    
    function limpiarFiltro(){
        document.getElementById('nomApellidos').value = '';
        document.getElementById('fechaDesde').value = '';
        document.getElementById('fechaHasta').value = '';
        document.getElementById('codProceso').value = '';
        document.getElementById('descProceso').value = '';
    }
    
    function cargarComboProcesos(){
            var ajax = getXMLHttpRequest();
            var nodos = null;
            var CONTEXT_PATH = '<%=request.getContextPath()%>'
            var url = CONTEXT_PATH + "/PeticionModuloIntegracion.do";
            var parametros = "";
            var control = new Date();
            parametros = 'tarea=preparar&modulo=MELANBIDE32&operacion=cargarComboProcesos&tipo=0&numero=<%=numExpediente%>&control='+control.getTime();
            try{
                ajax.open("POST",url,false);
                ajax.setRequestHeader("Content-Type","application/x-www-form-urlencoded; charset=ISO-8859-1");
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
                var listaProcesos = new Array();
                var fila = new Array();
                var nodoFila;
                var hijosFila;
                var contProcesos = 0;
                for(j=0;hijos!=null && j<hijos.length;j++){
                    if(hijos[j].nodeName=="CODIGO_OPERACION"){                            
                        codigoOperacion = hijos[j].childNodes[0].nodeValue;
                    }//if(hijos[j].nodeName=="CODIGO_OPERACION")                         
                    else if(hijos[j].nodeName=="SELECT_ITEM"){
                        nodoFila = hijos[j];
                        hijosFila = nodoFila.childNodes;
                        for(var cont = 0; cont < hijosFila.length; cont++){
                            if(hijosFila[cont].nodeName=="ID"){
                                if(hijosFila[cont].childNodes.length > 0){
                                    fila[0] = hijosFila[cont].childNodes[0].nodeValue;
                                }
                                else{
                                    fila[0] = '-';
                                }
                            }
                            else if(hijosFila[cont].nodeName=="LABEL"){
                                if(hijosFila[cont].childNodes.length > 0){
                                    fila[1] = hijosFila[cont].childNodes[0].nodeValue;
                                }
                                else{
                                    fila[1] = '-';
                                }
                            }
                        }
                        listaProcesos[contProcesos] = fila;
                        contProcesos++;
                        fila = new Array();
                    }  
                }//for(j=0;hijos!=null && j<hijos.length;j++)
                if(codigoOperacion=="0"){
                    document.getElementById('codProceso').value = '';
                    document.getElementById('descProceso').value = '';
                    comboProcesos = new Combo("Proceso");
                    codProcesos = new Array();
                    descProcesos = new Array();
                    for(var i = 0; i < listaProcesos.length; i++){
                        codProcesos[i] = listaProcesos[i][0];
                        descProcesos[i] = listaProcesos[i][1];
                    }
                    comboProcesos.addItems(codProcesos, descProcesos);
                }else if(codigoOperacion=="1"){
                    jsp_alerta("A",'<%=meLanbide32I18n.getMensaje(idiomaUsuario,"error.errorBD")%>');
                }else if(codigoOperacion=="2"){
                    jsp_alerta("A",'<%=meLanbide32I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                }else if(codigoOperacion=="3"){
                    jsp_alerta("A",'<%=meLanbide32I18n.getMensaje(idiomaUsuario,"error.pasoParametros")%>');
                }else{
                    jsp_alerta("A",'<%=meLanbide32I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                }//if(
            }
            catch(Err){

            }//try-catch
    }
    
    function buscar(){
        inicio = false;
        paginaActualE = 1;
        cargaPaginaE(paginaActualE);
    }
    
    function comprobarFecha(inputFecha) {
        if (Trim(inputFecha.value)!='') {
            if (!ValidarFechaConFormato(document.forms[0],inputFecha)){
                jsp_alerta("A","<%=meLanbide32I18n.getMensaje(idiomaUsuario, "msg.fechaNoVal")%>");
                return false;
            }
        }
        return true;
    }
    
    function validarDatosFiltro(){
        var txtDesde = document.getElementById('fechaDesde').value;
        var txtHasta = document.getElementById('fechaHasta').value;
        
        var hayFechaDesde = false;
        var hayFechaHasta = false;
        if(txtDesde != null && txtDesde != ''){
            if(!ValidarFechaConFormato(document.forms[0],document.getElementById('fechaDesde'))){
                jsp_alerta("A","<%=meLanbide32I18n.getMensaje(idiomaUsuario, "msg.fechaDesdeIncorrecto")%>");
                return false;
            }else{
                hayFechaDesde = true;
            }
        }
        
        if(txtHasta != null && txtHasta != ''){
            if(!ValidarFechaConFormato(document.forms[0],document.getElementById('fechaHasta'))){
                jsp_alerta("A","<%=meLanbide32I18n.getMensaje(idiomaUsuario, "msg.fechaHastaIncorrecto")%>");
                return false;
            }else{
                hayFechaHasta = true;
            }
        }
        
        var correcto = true;
        if(hayFechaDesde && hayFechaHasta){
            var array_fecha_desde = txtDesde.split("/");
            var array_fecha_hasta = txtHasta.split("/");
            var dia_desde = array_fecha_desde[0];
            var mes_desde = array_fecha_desde[1];
            var ano_desde = array_fecha_desde[2];
            var dia_hasta = array_fecha_hasta[0];
            var mes_hasta = array_fecha_hasta[1];
            var ano_hasta = array_fecha_hasta[2];
            
            var desde = new Date(ano_desde, mes_desde-1, dia_desde, 0, 0, 0, 0);
            var hasta = new Date(ano_hasta, mes_hasta-1, dia_hasta, 0, 0, 0, 0);
            var n1 = desde.getTime();
            var n2 = hasta.getTime();
            var result = n2 - n1;
            /*if(n2 <= 0){
                document.getElementById('fechaNacimientoPC').style.border = '1px solid red';
                if(mensajeError == "")
                    mensajeError = "<%=meLanbide32I18n.getMensaje(idiomaUsuario, "fechaNacNoVal")%>"+" '"+"<%=meLanbide32I18n.getMensaje(idiomaUsuario, "legend.personaContratada")%>"+"'";
                correcto = false;
            }*/
            if(result < 0){
                jsp_alerta("A","<%=meLanbide32I18n.getMensaje(idiomaUsuario, "msg.fechaDesdeAnteriorHasta")%>");
                correcto = false;
            }
        }
        return correcto;
    }
</script>
<body class="bandaBody" onload="inicializar();">
    <form id="formProcesos">
        <div class="tab-page" id="tabPage323" style="height:550px; width: 100%;">
			<div class="txttitblanco"><%=codProcedimiento+" - "+meLanbide32I18n.getMensaje(idiomaUsuario, "tit_procesos")%></div>
			<div class="contenidoPantalla">
				<div id="contenidoProc" class="cuadroFondoBlanco"  align="center">
							<fieldset id="fieldsetResolucionHoras" >
                                                        <legend class="legendAzul"><%=meLanbide32I18n.getMensaje(idiomaUsuario, "proc.legend.orientacion")%></legend>
                                                        <div class="lineaFormularioFont">
									<div style="width: 300px; float: left; text-align: left;" class="etiqueta">
                                                                <%=meLanbide32I18n.getMensaje(idiomaUsuario, "proc.label.RPH")%>
                                                            </div>
                                                            <div style="width: 100px; float: left;">
                                                                <div style="float: left;">
                                                                    <input type="button" id="btnRPH" name="btnRPH" class="botonGeneral" value="<%=meLanbide32I18n.getMensaje(idiomaUsuario, "proc.btn.ejecutar")%>" onclick="ejecutarResolucionProvisionalHoras();">
                                                                </div>
                                                            </div>
                                                        </div>

                                                        <div class="lineaFormularioFont">
									<div style="width: 300px; float: left; text-align: left;"  class="etiqueta">
                                                                <%=meLanbide32I18n.getMensaje(idiomaUsuario, "proc.label.DRH")%>
                                                            </div>
                                                            <div style="width: 100px; float: left;">
                                                                <div style="float: left;">
                                                                    <input type="button" id="btnDRH" name="btnDRH" class="botonGeneral" value="<%=meLanbide32I18n.getMensaje(idiomaUsuario, "proc.btn.generar")%>" onclick="generarDocumentacionResolucionHoras();">
                                                                </div>
                                                            </div>
                                                        </div>

                                                        <div class="lineaFormularioFont">
									<div style="width: 300px; float: left; text-align: left;"  class="etiqueta">
                                                                <%=meLanbide32I18n.getMensaje(idiomaUsuario, "proc.label.CH")%>
                                                            </div>
                                                            <div style="width: 100px; float: left;">
                                                                <div style="float: left;">
                                                                    <input type="button" id="btnCH" name="btnCH" class="botonGeneral" value="<%=meLanbide32I18n.getMensaje(idiomaUsuario, "proc.btn.ejecutar")%>" onclick="consolidarHoras();">
                                                                </div>
                                                            </div>
                                                        </div>

                                                        <div class="lineaFormularioFont">
									<div style="width: 300px; float: left; text-align: left;"  class="etiqueta">
                                                                <%=meLanbide32I18n.getMensaje(idiomaUsuario, "proc.label.DCH")%>
                                                            </div>
                                                            <div style="width: 100px; float: left;">
                                                                <div style="float: left;">
                                                                    <input type="button" id="btnDCH" name="btnDCH" class="botonGeneral" value="<%=meLanbide32I18n.getMensaje(idiomaUsuario, "proc.btn.ejecutar")%>" onclick="deshacerConsolidacionHoras();">
                                                                </div>
                                                            </div>
                                                        </div>
                                                    </fieldset>
							<fieldset id="fieldsetResolucionCentros" >
                                                        <legend class="legendAzul"><%=meLanbide32I18n.getMensaje(idiomaUsuario, "proc.legend.centroEmpleo")%></legend>
                                                        <div class="lineaFormularioFont">
									<div style="width: 300px; float: left; text-align: left;"  class="etiqueta">
                                                                <%=meLanbide32I18n.getMensaje(idiomaUsuario, "proc.label.RPC")%>
                                                            </div>
                                                            <div style="width: 100px; float: left;">
                                                                <div style="float: left;">
                                                                    <input type="button" id="btnRPC" name="btnRPC" class="botonGeneral" value="<%=meLanbide32I18n.getMensaje(idiomaUsuario, "proc.btn.ejecutar")%>" onclick="ejecutarResolucionProvisionalCentros();">
                                                                </div>
                                                            </div>
                                                        </div>

                                                        <div class="lineaFormularioFont">
									<div style="width: 300px; float: left; text-align: left;"  class="etiqueta">
                                                                <%=meLanbide32I18n.getMensaje(idiomaUsuario, "proc.label.DRC")%>
                                                            </div>
                                                            <div style="width: 100px; float: left;">
                                                                <div style="float: left;">
                                                                    <input type="button" id="btnDRC" name="btnDRC" class="botonGeneral" value="<%=meLanbide32I18n.getMensaje(idiomaUsuario, "proc.btn.generar")%>" onclick="generarDocumentacionResolucionCentros();">
                                                                </div>
                                                            </div>
                                                        </div>

                                                        <div class="lineaFormularioFont">
									<div style="width: 300px; float: left; text-align: left;"  class="etiqueta">
                                                                <%=meLanbide32I18n.getMensaje(idiomaUsuario, "proc.label.CC")%>
                                                            </div>
                                                            <div style="width: 100px; float: left;">
                                                                <div style="float: left;">
                                                                    <input type="button" id="btnCC" name="btnCC" class="botonGeneral" value="<%=meLanbide32I18n.getMensaje(idiomaUsuario, "proc.btn.ejecutar")%>" onclick="consolidarCentros();">
                                                                </div>
                                                            </div>
                                                        </div>

                                                        <div class="lineaFormularioFont">
									<div style="width: 300px; float: left; text-align: left;"  class="etiqueta">
                                                                <%=meLanbide32I18n.getMensaje(idiomaUsuario, "proc.label.DCC")%>
                                                            </div>
                                                            <div style="width: 100px; float: left;">
                                                                <div style="float: left;">
                                                                    <input type="button" id="btnDCC" name="btnDCC" class="botonGeneral" value="<%=meLanbide32I18n.getMensaje(idiomaUsuario, "proc.btn.ejecutar")%>" onclick="deshacerConsolidacionCentros();">
                                                                </div>
                                                            </div>
                                                        </div>
                                                    </fieldset>
							<fieldset id="fieldsetOtros" >
                                                        <legend class="legendAzul"><%=meLanbide32I18n.getMensaje(idiomaUsuario, "proc.legend.otros")%></legend>
                                                        <div class="lineaFormularioFont">
									<div style="width: 300px; float: left; text-align: left;"  class="etiqueta">
                                                                <%=meLanbide32I18n.getMensaje(idiomaUsuario, "proc.label.LAC")%>
                                                            </div>
                                                            <div style="width: 100px; float: left;">
                                                                <div style="float: left;">
                                                                    <input type="button" id="btnLAC" name="btnLAC" class="botonGeneral" value="<%=meLanbide32I18n.getMensaje(idiomaUsuario, "proc.btn.ejecutar")%>" onclick="ejecutarListadoAltaCentros();">
                                                                </div>
                                                            </div>
                                                        </div>
                                                    </fieldset>
						<div>
                                                        <fieldset style="width: 99%; float: left; padding-top: 10px;">
                                                            <legend class="legendAzul"><%=meLanbide32I18n.getMensaje(idiomaUsuario, "proc.legend.auditoriaProcesos")%></legend>
                                                            <div style="clear: both; float: left; width: 100%;">
                                                                <table style="width: 100%; font-size: 12px;">
                                                                    <tr>
                                                                        <td>
                                                                            <b><u><%=meLanbide32I18n.getMensaje(idiomaUsuario,"proc.label.criteriosBusq")%></u></b>
                                                                        </td>
                                                                    </tr>
                                                                    <tr>
                                                                        <td>
                                                                            <%=meLanbide32I18n.getMensaje(idiomaUsuario,"proc.label.nomApellidos")%>
                                                                            <input id="nomApellidos" name="nomApellidos" type="text" class="inputTexto" size="52" maxlength="40" />
                                                                            <%=meLanbide32I18n.getMensaje(idiomaUsuario,"proc.label.fDesde")%>
                                                                            <input type="text" class="inputTxtFecha" size="10" maxlength="10" id="fechaDesde" name="fechaDesde" onblur="javascript:return comprobarFecha(this);" onkeyup="return SoloCaracteresFecha(this);" onfocus="this.select();" />
                                                                            <A href="javascript:calClick(event);" onclick="mostrarCalFechaDesde(event);return false;" style="text-decoration:none; padding-right: 10px;" >
                                                                                <IMG style="border: 0" height="17" id="calFechaDesde" name="calFechaDesde" alt="<%=meLanbide32I18n.getMensaje(idiomaUsuario,"label.fNac")%>" src="<c:url value='/images/calendario/icono.gif'/>">
                                                                            </A>
                                                                            <%=meLanbide32I18n.getMensaje(idiomaUsuario,"proc.label.fHasta")%>
                                                                            <input type="text" class="inputTxtFecha" size="10" maxlength="10" id="fechaHasta" name="fechaHasta" onblur="javascript:return comprobarFecha(this);" onkeyup="return SoloCaracteresFecha(this);" onfocus="this.select();" />
                                                                            <A href="javascript:calClick(event);" onclick="mostrarCalFechaHasta(event);return false;" style="text-decoration:none; padding-right: 10px;" >
                                                                                <IMG style="border: 0" height="17" id="calFechaHasta" name="calFechaHasta" alt="<%=meLanbide32I18n.getMensaje(idiomaUsuario,"label.fNac")%>" src="<c:url value='/images/calendario/icono.gif'/>">
                                                                            </A>
                                                                        </td>
                                                                    </tr>
                                                                    <tr>
                                                                        <td>
                                                                            <%=meLanbide32I18n.getMensaje(idiomaUsuario,"proc.label.proceso")%>
                                                                            <input id="codProceso" name="codProceso" type="text" class="inputTexto" size="6" maxlength="2" 
                                                                                   onkeypress="javascript:return SoloDigitosConsulta(event);" >
                                                                            <input id="descProceso" name="descProceso" type="text" class="inputTexto" size="53" readonly>
													<a id="anchorProceso" name="anchorProceso" href=""><span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonProceso" name="botonProceso" style="cursor:hand;" alt="<%=meLanbide32I18n.getMensaje(idiomaUsuario, "altDesplegable")%>" title="<%=meLanbide32I18n.getMensaje(idiomaUsuario, "altDesplegable")%>"></span></a>
                                                                        </td>
                                                                    </tr>
                                                                    <tr>
                                                                        <td>
                                                                            <div class="botonera">
                                                                                <input type="button" id="btnBuscar" name="btnBuscar" class="botonGeneral" value="<%=meLanbide32I18n.getMensaje(idiomaUsuario, "proc.btn.buscar")%>" onclick="buscar();">
                                                                                <input type="button" id="btnLimpiar" name="btnLimpiar" class="botonGeneral" value="<%=meLanbide32I18n.getMensaje(idiomaUsuario, "proc.btn.limpiar")%>" onclick="limpiarFiltro();">
                                                                            </div>
                                                                        </td>
                                                                    </tr>
                                                                </table>
                                                            </div>
                                                            <div style="clear: both; float: left; width: 100%;">
                                                                <table style="width: 100%;">
                                                                    <tr>
                                                                        <td align="left" id="tablaAuditoria"/>
                                                                    </tr>
                                                                </table>
                                                            </div>
                                                            <div id="enlaceAud" STYLE="width:100%; clear: both;">
                                                            </div>
                                                        </fieldset>
                                                    </div>
                                                </div>
			</div>
        </div>
        <div id="popupcalendar" class="text"></div>
        <input type="hidden" id="codProcedimiento"/>
    </form>
</body>

<script type="text/javascript">
   
   // JAVASCRIPT DE LA TABLA AUDITORIA
   
   
    var comboProcesos = new Combo("Proceso");
    var codProcesos = new Array();
    var descProcesos = new Array();

    var tabAud = new Tabla(true,'<%=descriptor.getDescripcion("buscar")%>','<%=descriptor.getDescripcion("anterior")%>','<%=descriptor.getDescripcion("siguiente")%>','<%=descriptor.getDescripcion("mosFilasPag")%>','<%=descriptor.getDescripcion("msgNoResultBusq")%>','<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>','<%=descriptor.getDescripcion("filtrDeTotal")%>','<%=descriptor.getDescripcion("primero")%>','<%=descriptor.getDescripcion("ultimo")%>',document.getElementById('tablaAuditoria'), 930);
    //TOTAL ANCHO: 928
    tabAud.addColumna('245','left','<%= meLanbide32I18n.getMensaje(idiomaUsuario, "proc.auditoria.nombre")%>');
    tabAud.addColumna('250','left','<%= meLanbide32I18n.getMensaje(idiomaUsuario, "proc.auditoria.proceso")%>');
    tabAud.addColumna('165','center','<%= meLanbide32I18n.getMensaje(idiomaUsuario, "proc.auditoria.fecha")%>');
    tabAud.addColumna('244','center','<%= meLanbide32I18n.getMensaje(idiomaUsuario, "proc.auditoria.resultado")%>');
    tabAud.displayCabecera=true;
    tabAud.height = 125;

    var tableObject=tabAud;
    var inicio = false;
    
    cargarComboProcesos();
    
    cargaPaginaE(1);

    function refrescaExpedientes() {
      tabAud.displayTabla();
    }

    tabAud.displayDatos = pintaDatosExpedientes;

    function pintaDatosExpedientes() {
      tableObject = tabAud;
    }
</script>
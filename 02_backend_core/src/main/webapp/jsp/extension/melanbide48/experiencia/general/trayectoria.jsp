<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<meta http-equiv="X-UA-Compatible" content="IE=11">

<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide48.i18n.MeLanbide48I18n" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@ page import="es.altia.common.service.config.Config"%>
<%@ page import="es.altia.common.service.config.ConfigServiceHelper"%>
<%@page import="java.util.ArrayList" %>
<%@page import="java.util.List" %>
<%@page import="java.util.Map" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide48.vo.FilaEntidadVO"%>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide48.vo.ColecEntidadEnAgrupacionVO"%>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide48.vo.ColecEntidadVO"%>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide48.vo.ColecTrayVO"%>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide48.util.ConstantesMeLanbide48"%>
<!--<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />-->
<%
    String sIdioma = request.getParameter("idioma");
    int idiomaUsuario = sIdioma!=null && sIdioma!= "" ? Integer.parseInt(sIdioma) : 1;
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
    MeLanbide48I18n meLanbide48I18n = MeLanbide48I18n.getInstance();
    
    Config m_Config = ConfigServiceHelper.getConfig("common");
    String statusBar = m_Config.getString("JSP.StatusBar");
    String nombreModulo     = request.getParameter("nombreModulo");
    String codOrganizacion  = request.getParameter("codOrganizacionModulo");
    String numExpediente    = request.getParameter("numero");
    String mensajeProgreso = "";
    
    //String[] datos = numExpediente.split("/");
    String anoExpStr = (String)request.getAttribute("anoExp");
    //int anoExp = Integer.parseInt(datos[0]);
    int anoExp = anoExpStr!=null && anoExpStr!="" ? Integer.parseInt(anoExpStr):0;
    
    List<FilaEntidadVO> listaAsociaciones = (List<FilaEntidadVO>)request.getAttribute("asociaciones");
    ColecEntidadVO entidadPadre = (ColecEntidadVO)request.getAttribute("entidadPadre");
    int numAsociaciones = 0;
    try
    {
        numAsociaciones = listaAsociaciones != null ? listaAsociaciones.size() : 0;
    }
    catch(Exception ex)
    {
        
    }
    
Map<Long, ColecTrayVO> trayectorias = (Map<Long, ColecTrayVO>)request.getAttribute("trayectoriasGeneral");
%>
<style>
    #divTablaTrayectoria {
            height: 450px !important;
            width: 1070px !important;
    }
    #trayectoriaTable {
            font-size: 12px; 
            width:<%=((numAsociaciones * 300) + 350)%>px;  
    }
</style>
<jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
<jsp:setProperty name="descriptor"  property="idi_cod" value="<%=idiomaUsuario%>" />
<jsp:setProperty name="descriptor"  property="apl_cod" value="<%=apl%>" />
<link rel="StyleSheet" media="screen" type="text/css" href="<%=request.getContextPath()%><%=css%>">
<!--<link href="< c:url value='/css/extension/melanbide48/ScrollTabla.css'/>" rel="stylesheet" media="screen" />-->
<!--<script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/1.9.1/jquery.min.js"></script>-->
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script>
<!--<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script>-->

<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/>
<link rel="stylesheet" type="text/css" href="<c:url value='/css/extension/melanbide48/melanbide48.css'/>">
<link rel="stylesheet" type="text/css" href="<c:url value='/css/table.css'/>">

<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/melanbide48/tableHeadFixer.js"></script>

<script type="text/javascript" src="<c:url value='/scripts/popup.js'/>"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/melanbide48/colecUtils.js"></script>
<script type="text/javascript"> 
    var mensajeValidacion = '';
    
    function guardarTrayectoria(){
        if(validarTrayectoriaGeneral()){
            var numAsociaciones = 0;
            try{
                //numAsociaciones = parseInt('<%=numAsociaciones%>');
                numAsociaciones = document.getElementById('numeroAsociaciones').value;
            }catch(err){
                
            }
            var ajax = getXMLHttpRequest();
            var nodos = null;
            var CONTEXT_PATH = '<%=request.getContextPath()%>'
            var url = CONTEXT_PATH + "/PeticionModuloIntegracion.do";
            var parametros = "";
            var control = new Date();
            parametros = 'tarea=preparar&modulo=MELANBIDE48&operacion=guardarTrayectoriaGeneral&tipo=0&numero=<%=numExpediente%>'
                        +'&numEntidades=' + numAsociaciones;
                    
                        for(var i = 0; i < numAsociaciones; i++){
                            parametros += '&codigoEnt_'+i+'='+document.getElementById('codigoAsoc_'+i).value
                            +'&chk_dec327_2007_'+i+'='+(document.getElementById('chk_dec327_2007_'+i)!=null && document.getElementById('chk_dec327_2007_'+i).checked ? 1 : 0)
                            +'&chk_dec327_2008_'+i+'='+(document.getElementById('chk_dec327_2008_'+i)!=null && document.getElementById('chk_dec327_2008_'+i).checked ? 1 : 0)
                            +'&chk_dec327_2009_'+i+'='+(document.getElementById('chk_dec327_2009_'+i)!=null && document.getElementById('chk_dec327_2009_'+i).checked ? 1 : 0)
                            +'&chk_dec327_2010_'+i+'='+(document.getElementById('chk_dec327_2010_'+i)!=null && document.getElementById('chk_dec327_2010_'+i).checked ? 1 : 0)
                            +'&chk_min94_2007_'+i+'='+(document.getElementById('chk_min94_2007_'+i)!=null && document.getElementById('chk_min94_2007_'+i).checked ? 1 : 0)
                            +'&chk_min94_2008_'+i+'='+(document.getElementById('chk_min94_2008_'+i)!=null && document.getElementById('chk_min94_2008_'+i).checked ? 1 : 0)
                            +'&chk_min94_2009_'+i+'='+(document.getElementById('chk_min94_2009_'+i)!=null && document.getElementById('chk_min94_2009_'+i).checked ? 1 : 0)
                            +'&chk_min94_2010_'+i+'='+(document.getElementById('chk_min94_2010_'+i)!=null && document.getElementById('chk_min94_2010_'+i).checked ? 1 : 0)
                            +'&chk_min94_2011_'+i+'='+(document.getElementById('chk_min94_2011_'+i)!=null && document.getElementById('chk_min94_2011_'+i).checked ? 1 : 0)
                            +'&chk_min94_2012_'+i+'='+(document.getElementById('chk_min94_2012_'+i)!=null && document.getElementById('chk_min94_2012_'+i).checked ? 1 : 0)
                            +'&chk_min94_2013_'+i+'='+(document.getElementById('chk_min94_2013_'+i)!=null && document.getElementById('chk_min94_2013_'+i).checked ? 1 : 0)
                            +'&chk_min94_2014_'+i+'='+(document.getElementById('chk_min94_2014_'+i)!=null && document.getElementById('chk_min94_2014_'+i).checked ? 1 : 0)
                            +'&chk_min94_2015_'+i+'='+(document.getElementById('chk_min94_2015_'+i)!=null && document.getElementById('chk_min94_2015_'+i).checked ? 1 : 0)
                            +'&chk_min94_2016_'+i+'='+(document.getElementById('chk_min94_2016_'+i)!=null && document.getElementById('chk_min94_2016_'+i).checked ? 1 : 0)
                            +'&chk_min94_2017_'+i+'='+(document.getElementById('chk_min94_2017_'+i)!=null && document.getElementById('chk_min94_2017_'+i).checked ? 1 : 0)
                            +'&chk_min94_2018_'+i+'='+(document.getElementById('chk_min94_2018_'+i)!=null && document.getElementById('chk_min94_2018_'+i).checked ? 1 : 0)
                            +'&chk_min98_2007_'+i+'='+(document.getElementById('chk_min98_2007_'+i)!=null && document.getElementById('chk_min98_2007_'+i).checked ? 1 : 0)
                            +'&chk_min98_2008_'+i+'='+(document.getElementById('chk_min98_2008_'+i)!=null && document.getElementById('chk_min98_2008_'+i).checked ? 1 : 0)
                            +'&chk_min98_2009_'+i+'='+(document.getElementById('chk_min98_2009_'+i)!=null && document.getElementById('chk_min98_2009_'+i).checked ? 1 : 0)
                            +'&chk_min98_2010_'+i+'='+(document.getElementById('chk_min98_2010_'+i)!=null && document.getElementById('chk_min98_2010_'+i).checked ? 1 : 0)
                            +'&chk_min98_2011_'+i+'='+(document.getElementById('chk_min98_2011_'+i)!=null && document.getElementById('chk_min98_2011_'+i).checked ? 1 : 0)
                            +'&chk_min98_2012_'+i+'='+(document.getElementById('chk_min98_2012_'+i)!=null && document.getElementById('chk_min98_2012_'+i).checked ? 1 : 0)
                            +'&chk_min98_2013_'+i+'='+(document.getElementById('chk_min98_2013_'+i)!=null && document.getElementById('chk_min98_2013_'+i).checked ? 1 : 0)
                            +'&chk_min98_2014_'+i+'='+(document.getElementById('chk_min98_2014_'+i)!=null && document.getElementById('chk_min98_2014_'+i).checked ? 1 : 0)
                            +'&chk_min98_2015_'+i+'='+(document.getElementById('chk_min98_2015_'+i)!=null && document.getElementById('chk_min98_2015_'+i).checked ? 1 : 0)
                            +'&chk_min98_2016_'+i+'='+(document.getElementById('chk_min98_2016_'+i)!=null && document.getElementById('chk_min98_2016_'+i).checked ? 1 : 0)
                            +'&chk_min98_2017_'+i+'='+(document.getElementById('chk_min98_2017_'+i)!=null && document.getElementById('chk_min98_2017_'+i).checked ? 1 : 0)
                            +'&chk_min98_2018_'+i+'='+(document.getElementById('chk_min98_2018_'+i)!=null && document.getElementById('chk_min98_2018_'+i).checked ? 1 : 0)
                            +'&chk_tas03_2007_'+i+'='+(document.getElementById('chk_tas03_2007_'+i)!=null && document.getElementById('chk_tas03_2007_'+i).checked ? 1 : 0)
                            +'&chk_tas03_2008_'+i+'='+(document.getElementById('chk_tas03_2008_'+i)!=null && document.getElementById('chk_tas03_2008_'+i).checked ? 1 : 0)
                            +'&chk_tas03_2009_'+i+'='+(document.getElementById('chk_tas03_2009_'+i)!=null && document.getElementById('chk_tas03_2009_'+i).checked ? 1 : 0)
                            +'&chk_tas03_2010_'+i+'='+(document.getElementById('chk_tas03_2010_'+i)!=null && document.getElementById('chk_tas03_2010_'+i).checked ? 1 : 0)
                            +'&chk_tas03_2011_'+i+'='+(document.getElementById('chk_tas03_2011_'+i)!=null && document.getElementById('chk_tas03_2011_'+i).checked ? 1 : 0)
                            +'&chk_tas03_2012_'+i+'='+(document.getElementById('chk_tas03_2012_'+i)!=null && document.getElementById('chk_tas03_2012_'+i).checked ? 1 : 0)
                            +'&chk_tas03_2013_'+i+'='+(document.getElementById('chk_tas03_2013_'+i)!=null && document.getElementById('chk_tas03_2013_'+i).checked ? 1 : 0)
                            +'&chk_tas03_2014_'+i+'='+(document.getElementById('chk_tas03_2014_'+i)!=null && document.getElementById('chk_tas03_2014_'+i).checked ? 1 : 0)
                            +'&chk_tas03_2015_'+i+'='+(document.getElementById('chk_tas03_2015_'+i)!=null && document.getElementById('chk_tas03_2015_'+i).checked ? 1 : 0)
                            +'&chk_tas03_2016_'+i+'='+(document.getElementById('chk_tas03_2016_'+i)!=null && document.getElementById('chk_tas03_2016_'+i).checked ? 1 : 0)
                            +'&chk_tas03_2017_'+i+'='+(document.getElementById('chk_tas03_2017_'+i)!=null && document.getElementById('chk_tas03_2017_'+i).checked ? 1 : 0)
                            +'&chk_tas03_2018_'+i+'='+(document.getElementById('chk_tas03_2018_'+i)!=null && document.getElementById('chk_tas03_2018_'+i).checked ? 1 : 0)
                            //+'&txt_act5603_'+i+'='+document.getElementById('txt_act5603_'+i).value
                            +'&chk_lan_2011_'+i+'='+(document.getElementById('chk_lan_2011_'+i)!=null && document.getElementById('chk_lan_2011_'+i).checked ? 1 : 0)
                            +'&chk_lan_2013_'+i+'='+(document.getElementById('chk_lan_2013_'+i)!=null && document.getElementById('chk_lan_2013_'+i).checked ? 1 : 0)
                            +'&chk_lan_2014_'+i+'='+(document.getElementById('chk_lan_2014_'+i)!=null && document.getElementById('chk_lan_2014_'+i).checked ? 1 : 0)
                            +'&chk_lan_2015_'+i+'='+(document.getElementById('chk_lan_2015_'+i)!=null && document.getElementById('chk_lan_2015_'+i).checked ? 1 : 0)
                            +'&chk_lan_2017_'+i+'='+(document.getElementById('chk_lan_2017_'+i)!=null && document.getElementById('chk_lan_2017_'+i).checked ? 1 : 0)
                            //+'&txt_otros_'+i+'='+document.getElementById('txt_otros_'+i).value
                        }
                        + '&control='+control.getTime();
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

                for(var j=0;hijos!=null && j<hijos.length;j++){
                    if(hijos[j].nodeName=="CODIGO_OPERACION"){                            
                        codigoOperacion = hijos[j].childNodes[0].nodeValue;
                    }//if(hijos[j].nodeName=="CODIGO_OPERACION")   
                }//for(j=0;hijos!=null && j<hijos.length;j++)
                if(codigoOperacion=="0"){
                    jsp_alerta("A",'<%=meLanbide48I18n.getMensaje(idiomaUsuario,"msg.datosGuardadosOK")%>');
                    //refrescarPestanaTrayectoriaColec();
                }else if(codigoOperacion=="1"){
                    jsp_alerta("A",'<%=meLanbide48I18n.getMensaje(idiomaUsuario,"error.errorBD")%>');
                }else if(codigoOperacion=="2"){
                    jsp_alerta("A",'<%=meLanbide48I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                }else if(codigoOperacion=="3"){
                    jsp_alerta("A",'<%=meLanbide48I18n.getMensaje(idiomaUsuario,"error.pasoParametros")%>');
                }else if(codigoOperacion=="4"){
                    jsp_alerta("A",'<%=meLanbide48I18n.getMensaje(idiomaUsuario,"error.trayectoria.errorGuardarTray")%>');
                }else{
                    jsp_alerta("A",'<%=meLanbide48I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                }//if(
            }catch(err){

            }
        }else{
            jsp_alerta("A", escape(mensajeValidacion));
        }
    }
            
    function validarTrayectoriaGeneral(){
        var elemento;
        var numAsociaciones = <%=numAsociaciones%>;
        var correcto = true;
        var i = 0;
        /*
        while(i < numAsociaciones){
            elemento = document.getElementById('txt_act5603_'+i);
            if(!validarNumericoColec(elemento, 8)){
                correcto = false;
                elemento.style.border = "1px solid red";
            }else{
                elemento.removeAttribute("style");
            }
            
            elemento = document.getElementById('txt_otros_'+i);
            if(!validarNumericoColec(elemento, 8)){
                correcto = false;
                elemento.style.border = "1px solid red";
            }else{
                elemento.removeAttribute("style");
            }
       
            i++;
        }
        */
        
        if(!correcto){
            mensajeValidacion = '<%=meLanbide48I18n.getMensaje(idiomaUsuario,"error.solicitud.general.trayectoria.datoIncorrecto")%>';
        }else{
            mensajeValidacion = '';
        }
        
        return correcto;
    }
    
    function refrescarPestanaTrayectoriaColec(){
        //Se actualizan los datos de la pestaña
        var ajax = getXMLHttpRequest();
        var nodos = null;
        var CONTEXT_PATH = '<%=request.getContextPath()%>'
        var url = CONTEXT_PATH + "/PeticionModuloIntegracion.do";
        var parametros = "";
        var control = new Date();
        parametros = 'tarea=preparar&modulo=MELANBIDE48&operacion=getTrayectoriaGeneral&tipo=0&numero=<%=numExpediente%>&control='+control.getTime();
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
            var listaTrayectorias = extraerTrayectoriasCOLEC(nodos);
            var codigoOperacion = listaTrayectorias[0];
            if(codigoOperacion=="0"){
                recargarTrayectoriasCOLEC(listaTrayectorias);
            }else if(codigoOperacion=="1"){
                jsp_alerta("A",'<%=String.format(meLanbide48I18n.getMensaje(idiomaUsuario,"error.refrescarPestana.errorBD"), meLanbide48I18n.getMensaje(idiomaUsuario, "label.trayectoria.tituloPestana"))%>');
            }else if(codigoOperacion=="2"){
                jsp_alerta("A",'<%=String.format(meLanbide48I18n.getMensaje(idiomaUsuario,"error.refrescarPestana.errorGen"), meLanbide48I18n.getMensaje(idiomaUsuario, "label.trayectoria.tituloPestana"))%>');
            }else if(codigoOperacion=="3"){
                jsp_alerta("A",'<%=String.format(meLanbide48I18n.getMensaje(idiomaUsuario,"error.refrescarPestana.pasoParametros"), meLanbide48I18n.getMensaje(idiomaUsuario, "label.trayectoria.tituloPestana"))%>');
            }else{
                    jsp_alerta("A",'<%=String.format(meLanbide48I18n.getMensaje(idiomaUsuario,"error.refrescarPestana.errorGen"), meLanbide48I18n.getMensaje(idiomaUsuario, "label.trayectoria.tituloPestana"))%>');
            }//if(
        }
        catch(Err){
            jsp_alerta("A",'<%=String.format(meLanbide48I18n.getMensaje(idiomaUsuario,"error.refrescarPestana.errorGen"), meLanbide48I18n.getMensaje(idiomaUsuario, "label.trayectoria.tituloPestana"))%>'+ 'Detalles: ' + Err.message);
        }//try-catch
    }
    
    function extraerTrayectoriasCOLEC(nodos){
        var elemento = nodos[0];
        var hijos = elemento.childNodes;
        var codigoOperacion = null;
        var listaTrayectorias = new Array();
        var datos = new Array();
        var filaTrayectoria = new Array();
        var filaAsociacion = new Array();
        var nodoAsociacion;
        var hijosAsociacion;
        var nodoTrayectoria;
        var hijosTrayectoria;
        for(j=0;hijos!=null && j<hijos.length;j++){
            if(hijos[j].nodeName=="CODIGO_OPERACION"){                            
                codigoOperacion = hijos[j].childNodes[0].nodeValue;
                datos[j] = codigoOperacion;
            }//if(hijos[j].nodeName=="CODIGO_OPERACION")                        
            else if(hijos[j].nodeName=="ASOCIACION"){
                nodoAsociacion = hijos[j];
                hijosAsociacion = nodoAsociacion.childNodes;
                for(var cont = 0; cont < hijosAsociacion.length; cont++){
                    if(hijosAsociacion[cont].nodeName=="COD_ASOCIACION"){
                        if(hijosAsociacion[cont].childNodes.length > 0){
                            filaAsociacion[0] = hijosAsociacion[cont].childNodes[0].nodeValue;
                        }
                        else{
                            filaAsociacion[0] = '-';
                        }
                    }
                    else if(hijosAsociacion[cont].nodeName=="NOMBRE_ASOCIACION"){
                        if(hijosAsociacion[cont].childNodes.length > 0){
                            filaAsociacion[1] = hijosAsociacion[cont].childNodes[0].nodeValue;
                        }
                        else{
                            filaAsociacion[1] = '-';
                        }
                    }
                    else if(hijosAsociacion[cont].nodeName=="TRAYECTORIA"){
                        nodoTrayectoria = hijosAsociacion[cont];
                        hijosTrayectoria = nodoTrayectoria.childNodes;
                        for(var cont2 = 0; cont2 < hijosTrayectoria.length; cont2++){
                            if(hijosTrayectoria[cont2].nodeName=="DEC_327_2007"){
                                if(hijosTrayectoria[cont2].childNodes.length > 0){
                                    filaTrayectoria[0] = hijosTrayectoria[cont2].childNodes[0].nodeValue;
                                }
                                else{
                                    filaTrayectoria[0] = '-';
                                }
                            }
                            else if(hijosTrayectoria[cont2].nodeName=="DEC_327_2008"){
                                if(hijosTrayectoria[cont2].childNodes.length > 0){
                                    filaTrayectoria[1] = hijosTrayectoria[cont2].childNodes[0].nodeValue;
                                }
                                else{
                                    filaTrayectoria[1] = '-';
                                }
                            }
                            else if(hijosTrayectoria[cont2].nodeName=="DEC_327_2009"){
                                if(hijosTrayectoria[cont2].childNodes.length > 0){
                                    filaTrayectoria[2] = hijosTrayectoria[cont2].childNodes[0].nodeValue;
                                }
                                else{
                                    filaTrayectoria[2] = '-';
                                }
                            }
                            else if(hijosTrayectoria[cont2].nodeName=="DEC_327_2010"){
                                if(hijosTrayectoria[cont2].childNodes.length > 0){
                                    filaTrayectoria[3] = hijosTrayectoria[cont2].childNodes[0].nodeValue;
                                }
                                else{
                                    filaTrayectoria[3] = '-';
                                }
                            }
                            else if(hijosTrayectoria[cont2].nodeName=="MIN_94_2007"){
                                if(hijosTrayectoria[cont2].childNodes.length > 0){
                                    filaTrayectoria[4] = hijosTrayectoria[cont2].childNodes[0].nodeValue;
                                }
                                else{
                                    filaTrayectoria[4] = '-';
                                }
                            }
                            else if(hijosTrayectoria[cont2].nodeName=="MIN_94_2008"){
                                if(hijosTrayectoria[cont2].childNodes.length > 0){
                                    filaTrayectoria[5] = hijosTrayectoria[cont2].childNodes[0].nodeValue;
                                }
                                else{
                                    filaTrayectoria[5] = '-';
                                }
                            }
                            else if(hijosTrayectoria[cont2].nodeName=="MIN_94_2009"){
                                if(hijosTrayectoria[cont2].childNodes.length > 0){
                                    filaTrayectoria[6] = hijosTrayectoria[cont2].childNodes[0].nodeValue;
                                }
                                else{
                                    filaTrayectoria[6] = '-';
                                }
                            }
                            else if(hijosTrayectoria[cont2].nodeName=="MIN_94_2010"){
                                if(hijosTrayectoria[cont2].childNodes.length > 0){
                                    filaTrayectoria[7] = hijosTrayectoria[cont2].childNodes[0].nodeValue;
                                }
                                else{
                                    filaTrayectoria[7] = '-';
                                }
                            }
                            else if(hijosTrayectoria[cont2].nodeName=="MIN_94_2011"){
                                if(hijosTrayectoria[cont2].childNodes.length > 0){
                                    filaTrayectoria[8] = hijosTrayectoria[cont2].childNodes[0].nodeValue;
                                }
                                else{
                                    filaTrayectoria[8] = '-';
                                }
                            }
                            else if(hijosTrayectoria[cont2].nodeName=="MIN_94_2012"){
                                if(hijosTrayectoria[cont2].childNodes.length > 0){
                                    filaTrayectoria[9] = hijosTrayectoria[cont2].childNodes[0].nodeValue;
                                }
                                else{
                                    filaTrayectoria[9] = '-';
                                }
                            }
                            else if(hijosTrayectoria[cont2].nodeName=="MIN_94_2013"){
                                if(hijosTrayectoria[cont2].childNodes.length > 0){
                                    filaTrayectoria[10] = hijosTrayectoria[cont2].childNodes[0].nodeValue;
                                }
                                else{
                                    filaTrayectoria[10] = '-';
                                }
                            }
                            else if(hijosTrayectoria[cont2].nodeName=="MIN_94_2014"){
                                if(hijosTrayectoria[cont2].childNodes.length > 0){
                                    filaTrayectoria[11] = hijosTrayectoria[cont2].childNodes[0].nodeValue;
                                }
                                else{
                                    filaTrayectoria[11] = '-';
                                }
                            }
                            else if(hijosTrayectoria[cont2].nodeName=="MIN_94_2015"){
                                if(hijosTrayectoria[cont2].childNodes.length > 0){
                                    filaTrayectoria[12] = hijosTrayectoria[cont2].childNodes[0].nodeValue;
                                }
                                else{
                                    filaTrayectoria[12] = '-';
                                }
                            }
                            else if(hijosTrayectoria[cont2].nodeName=="MIN_94_2016"){
                                if(hijosTrayectoria[cont2].childNodes.length > 0){
                                    filaTrayectoria[13] = hijosTrayectoria[cont2].childNodes[0].nodeValue;
                                }
                                else{
                                    filaTrayectoria[13] = '-';
                                }
                            }
                            else if(hijosTrayectoria[cont2].nodeName=="MIN_98_2007"){
                                if(hijosTrayectoria[cont2].childNodes.length > 0){
                                    filaTrayectoria[14] = hijosTrayectoria[cont2].childNodes[0].nodeValue;
                                }
                                else{
                                    filaTrayectoria[14] = '-';
                                }
                            }
                            else if(hijosTrayectoria[cont2].nodeName=="MIN_98_2008"){
                                if(hijosTrayectoria[cont2].childNodes.length > 0){
                                    filaTrayectoria[15] = hijosTrayectoria[cont2].childNodes[0].nodeValue;
                                }
                                else{
                                    filaTrayectoria[15] = '-';
                                }
                            }
                            else if(hijosTrayectoria[cont2].nodeName=="MIN_98_2009"){
                                if(hijosTrayectoria[cont2].childNodes.length > 0){
                                    filaTrayectoria[16] = hijosTrayectoria[cont2].childNodes[0].nodeValue;
                                }
                                else{
                                    filaTrayectoria[16] = '-';
                                }
                            }
                            else if(hijosTrayectoria[cont2].nodeName=="MIN_98_2010"){
                                if(hijosTrayectoria[cont2].childNodes.length > 0){
                                    filaTrayectoria[17] = hijosTrayectoria[cont2].childNodes[0].nodeValue;
                                }
                                else{
                                    filaTrayectoria[17] = '-';
                                }
                            }
                            else if(hijosTrayectoria[cont2].nodeName=="MIN_98_2011"){
                                if(hijosTrayectoria[cont2].childNodes.length > 0){
                                    filaTrayectoria[18] = hijosTrayectoria[cont2].childNodes[0].nodeValue;
                                }
                                else{
                                    filaTrayectoria[18] = '-';
                                }
                            }
                            else if(hijosTrayectoria[cont2].nodeName=="MIN_98_2012"){
                                if(hijosTrayectoria[cont2].childNodes.length > 0){
                                    filaTrayectoria[19] = hijosTrayectoria[cont2].childNodes[0].nodeValue;
                                }
                                else{
                                    filaTrayectoria[19] = '-';
                                }
                            }
                            else if(hijosTrayectoria[cont2].nodeName=="MIN_98_2013"){
                                if(hijosTrayectoria[cont2].childNodes.length > 0){
                                    filaTrayectoria[20] = hijosTrayectoria[cont2].childNodes[0].nodeValue;
                                }
                                else{
                                    filaTrayectoria[20] = '-';
                                }
                            }
                            else if(hijosTrayectoria[cont2].nodeName=="MIN_98_2014"){
                                if(hijosTrayectoria[cont2].childNodes.length > 0){
                                    filaTrayectoria[21] = hijosTrayectoria[cont2].childNodes[0].nodeValue;
                                }
                                else{
                                    filaTrayectoria[21] = '-';
                                }
                            }
                            else if(hijosTrayectoria[cont2].nodeName=="MIN_98_2015"){
                                if(hijosTrayectoria[cont2].childNodes.length > 0){
                                    filaTrayectoria[22] = hijosTrayectoria[cont2].childNodes[0].nodeValue;
                                }
                                else{
                                    filaTrayectoria[22] = '-';
                                }
                            }
                            else if(hijosTrayectoria[cont2].nodeName=="MIN_98_2016"){
                                if(hijosTrayectoria[cont2].childNodes.length > 0){
                                    filaTrayectoria[23] = hijosTrayectoria[cont2].childNodes[0].nodeValue;
                                }
                                else{
                                    filaTrayectoria[23] = '-';
                                }
                            }
                            else if(hijosTrayectoria[cont2].nodeName=="TAS_03_2007"){
                                if(hijosTrayectoria[cont2].childNodes.length > 0){
                                    filaTrayectoria[24] = hijosTrayectoria[cont2].childNodes[0].nodeValue;
                                }
                                else{
                                    filaTrayectoria[24] = '-';
                                }
                            }
                            else if(hijosTrayectoria[cont2].nodeName=="TAS_03_2008"){
                                if(hijosTrayectoria[cont2].childNodes.length > 0){
                                    filaTrayectoria[25] = hijosTrayectoria[cont2].childNodes[0].nodeValue;
                                }
                                else{
                                    filaTrayectoria[25] = '-';
                                }
                            }
                            else if(hijosTrayectoria[cont2].nodeName=="TAS_03_2009"){
                                if(hijosTrayectoria[cont2].childNodes.length > 0){
                                    filaTrayectoria[26] = hijosTrayectoria[cont2].childNodes[0].nodeValue;
                                }
                                else{
                                    filaTrayectoria[26] = '-';
                                }
                            }
                            else if(hijosTrayectoria[cont2].nodeName=="TAS_03_2010"){
                                if(hijosTrayectoria[cont2].childNodes.length > 0){
                                    filaTrayectoria[27] = hijosTrayectoria[cont2].childNodes[0].nodeValue;
                                }
                                else{
                                    filaTrayectoria[27] = '-';
                                }
                            }
                            else if(hijosTrayectoria[cont2].nodeName=="TAS_03_2011"){
                                if(hijosTrayectoria[cont2].childNodes.length > 0){
                                    filaTrayectoria[28] = hijosTrayectoria[cont2].childNodes[0].nodeValue;
                                }
                                else{
                                    filaTrayectoria[28] = '-';
                                }
                            }
                            else if(hijosTrayectoria[cont2].nodeName=="TAS_03_2012"){
                                if(hijosTrayectoria[cont2].childNodes.length > 0){
                                    filaTrayectoria[29] = hijosTrayectoria[cont2].childNodes[0].nodeValue;
                                }
                                else{
                                    filaTrayectoria[29] = '-';
                                }
                            }
                            else if(hijosTrayectoria[cont2].nodeName=="TAS_03_2013"){
                                if(hijosTrayectoria[cont2].childNodes.length > 0){
                                    filaTrayectoria[30] = hijosTrayectoria[cont2].childNodes[0].nodeValue;
                                }
                                else{
                                    filaTrayectoria[30] = '-';
                                }
                            }
                            else if(hijosTrayectoria[cont2].nodeName=="TAS_03_2014"){
                                if(hijosTrayectoria[cont2].childNodes.length > 0){
                                    filaTrayectoria[31] = hijosTrayectoria[cont2].childNodes[0].nodeValue;
                                }
                                else{
                                    filaTrayectoria[31] = '-';
                                }
                            }
                            else if(hijosTrayectoria[cont2].nodeName=="TAS_03_2015"){
                                if(hijosTrayectoria[cont2].childNodes.length > 0){
                                    filaTrayectoria[32] = hijosTrayectoria[cont2].childNodes[0].nodeValue;
                                }
                                else{
                                    filaTrayectoria[32] = '-';
                                }
                            }
                            else if(hijosTrayectoria[cont2].nodeName=="TAS_03_2016"){
                                if(hijosTrayectoria[cont2].childNodes.length > 0){
                                    filaTrayectoria[33] = hijosTrayectoria[cont2].childNodes[0].nodeValue;
                                }
                                else{
                                    filaTrayectoria[33] = '-';
                                }
                            }
                            /*else if(hijosTrayectoria[cont2].nodeName=="ACT_56_03"){
                                if(hijosTrayectoria[cont2].childNodes.length > 0){
                                    filaTrayectoria[34] = hijosTrayectoria[cont2].childNodes[0].nodeValue;
                                }
                                else{
                                    filaTrayectoria[34] = '-';
                                }
                            }
                            */
                            else if(hijosTrayectoria[cont2].nodeName=="LAN_2011"){
                                if(hijosTrayectoria[cont2].childNodes.length > 0){
                                    filaTrayectoria[34] = hijosTrayectoria[cont2].childNodes[0].nodeValue;
                                }
                                else{
                                    filaTrayectoria[34] = '-';
                                }
                            }
                            else if(hijosTrayectoria[cont2].nodeName=="LAN_2013"){
                                if(hijosTrayectoria[cont2].childNodes.length > 0){
                                    filaTrayectoria[35] = hijosTrayectoria[cont2].childNodes[0].nodeValue;
                                }
                                else{
                                    filaTrayectoria[35] = '-';
                                }
                            }
                            else if(hijosTrayectoria[cont2].nodeName=="LAN_2014"){
                                if(hijosTrayectoria[cont2].childNodes.length > 0){
                                    filaTrayectoria[36] = hijosTrayectoria[cont2].childNodes[0].nodeValue;
                                }
                                else{
                                    filaTrayectoria[36] = '-';
                                }
                            }
                            else if(hijosTrayectoria[cont2].nodeName=="LAN_2015"){
                                if(hijosTrayectoria[cont2].childNodes.length > 0){
                                    filaTrayectoria[37] = hijosTrayectoria[cont2].childNodes[0].nodeValue;
                                }
                                else{
                                    filaTrayectoria[37] = '-';
                                }
                            }
                            else if(hijosTrayectoria[cont2].nodeName=="COLEC_COD_TRAY"){
                                if(hijosTrayectoria[cont2].childNodes.length > 0){
                                    filaTrayectoria[38] = hijosTrayectoria[cont2].childNodes[0].nodeValue;
                                }
                                else{
                                    filaTrayectoria[38] = '-';
                                }
                            }
                            /*else if(hijosTrayectoria[cont2].nodeName=="LAN_OTROS"){
                                if(hijosTrayectoria[cont2].childNodes.length > 0){
                                    filaTrayectoria[39] = hijosTrayectoria[cont2].childNodes[0].nodeValue;
                                }
                                else{
                                    filaTrayectoria[39] = '-';
                                }
                            }*/
                            else if(hijosTrayectoria[cont2].nodeName=="COLEC1_MIN_94_2017"){
                                if(hijosTrayectoria[cont2].childNodes.length > 0){
                                    filaTrayectoria[39] = hijosTrayectoria[cont2].childNodes[0].nodeValue;
                                }
                                else{
                                    filaTrayectoria[39] = '-';
                                }
                            }
                            else if(hijosTrayectoria[cont2].nodeName=="COLEC1_MIN_94_2018"){
                                if(hijosTrayectoria[cont2].childNodes.length > 0){
                                    filaTrayectoria[40] = hijosTrayectoria[cont2].childNodes[0].nodeValue;
                                }
                                else{
                                    filaTrayectoria[40] = '-';
                                }
                            }
                            else if(hijosTrayectoria[cont2].nodeName=="COLEC1_MIN_98_2017"){
                                if(hijosTrayectoria[cont2].childNodes.length > 0){
                                    filaTrayectoria[41] = hijosTrayectoria[cont2].childNodes[0].nodeValue;
                                }
                                else{
                                    filaTrayectoria[41] = '-';
                                }
                            }
                            else if(hijosTrayectoria[cont2].nodeName=="COLEC1_MIN_98_2018"){
                                if(hijosTrayectoria[cont2].childNodes.length > 0){
                                    filaTrayectoria[42] = hijosTrayectoria[cont2].childNodes[0].nodeValue;
                                }
                                else{
                                    filaTrayectoria[42] = '-';
                                }
                            }
                            else if(hijosTrayectoria[cont2].nodeName=="COLEC1_TAS_03_2017"){
                                if(hijosTrayectoria[cont2].childNodes.length > 0){
                                    filaTrayectoria[43] = hijosTrayectoria[cont2].childNodes[0].nodeValue;
                                }
                                else{
                                    filaTrayectoria[43] = '-';
                                }
                            }
                            else if(hijosTrayectoria[cont2].nodeName=="COLEC1_TAS_03_2018"){
                                if(hijosTrayectoria[cont2].childNodes.length > 0){
                                    filaTrayectoria[44] = hijosTrayectoria[cont2].childNodes[0].nodeValue;
                                }
                                else{
                                    filaTrayectoria[44] = '-';
                                }
                            }
                            else if(hijosTrayectoria[cont2].nodeName=="COLEC2_LAN_2017"){
                                if(hijosTrayectoria[cont2].childNodes.length > 0){
                                    filaTrayectoria[45] = hijosTrayectoria[cont2].childNodes[0].nodeValue;
                                }
                                else{
                                    filaTrayectoria[45] = '-';
                                }
                            }
                        }
                        filaAsociacion[2] = filaTrayectoria;
                        filaTrayectoria = new Array();
                    }
                } 
                listaTrayectorias[j - 1] = filaAsociacion;
                filaAsociacion = new Array();
            }   
        }//for(j=0;hijos!=null && j<hijos.length;j++)
        datos[1] = listaTrayectorias;

        return datos;
    }
    
    function recargarTrayectoriasCOLEC(listaTrayectorias){
    
        var asociaciones = listaTrayectorias[1];
        
        var estiloI = '';
        var estiloD = '';
        var numAsociaciones = asociaciones.length;
        var codigoAsociacion = '';
        var nombreAsociacion = '';
        var asociacion = new Array();
        var trayectoria = new Array();
        var ano = document.getElementById('anoExpediente').value;
        var colspanN =4;
        if(ano>=2017)
            colspanN=7;
       var html2C =//'<table id="trayectoriaTable1" style="table-layout: fixed" >'
'                                        <tr>'
+'                                             <th style="height: 13px; width: 350px; table-layout: fixed">'
+'                                             </th>';
                                               for(var i = 0; i<numAsociaciones; i++)
                                                {

                                                    if(i == numAsociaciones - 1)
                                                    {
                                                        estiloD = "";
                                                    }
                                                    else
                                                    {
                                                        estiloD = " border-right: 1px solid black;";
                                                    }

                                                   //asociacion = listaAsociaciones.get(i);
                                                   asociacion = asociaciones[i];
                                                   if(asociacion[0] != null && asociacion[0] != '-'){
                                                        codigoAsociacion = asociacion[0];
                                                    }else{
                                                        codigoAsociacion = '';
                                                    }
                                                    nombreAsociacion = asociacion[1];
html2C +='                                           <th style="text-align: center; background-color: #4B95D3; height: 13px;'+estiloD+'" colspan="'+colspanN+'" title="'+nombreAsociacion+'">'
+'                                                        <div style="overflow: hidden; text-overflow: ellipsis; white-space: nowrap; width: 100%;">'
+                                                                   nombreAsociacion
+'                                                            <input type="hidden" id="codigoAsoc_'+i+'" value="'+codigoAsociacion+'"'
+'                                                        </div>'
+'                                                    </th>';
                                                }

html2C +='                                        </tr>'
;
var html2='                                        <tr>'
+'                                            <td style="background-color: LightGrey; height: 14px;'+estiloD+'">'
+'                                                <%=meLanbide48I18n.getMensaje(idiomaUsuario,"label.trayectoria.programasSubvencionables")%>'
+'                                            </td>';
                                                for(var i = 0; i < numAsociaciones; i++)
                                                {
                                                    if(i == numAsociaciones - 1)
                                                    {
                                                        estiloD = "";
                                                    }
                                                    else
                                                    {
                                                        estiloD = " border-right: 1px solid black;";
                                                    }
                                             if(ano < 2017) {
html2 +='                                       <td style="text-align: center;background-color: LightGrey; height: 14px;" width="25">'
+'                                                2007'
+'                                            </td>'
+'                                            <td style="text-align: center;background-color: LightGrey; height: 14px;" width="25">'
+'                                                2008'
+'                                            </td>'
+'                                            <td style="text-align: center;background-color: LightGrey; height: 14px;" width="25">'
+'                                                2009'
+'                                            </td>';
                                             }
                                             if(ano < 2018) { 
html2 +='                                    <td style="text-align: center;background-color: LightGrey; height: 14px;'+(ano<2017?estiloD:"")+'" width="25">'
+'                                                2010'
+'                                            </td>';
                                             }
                                            if(ano >= 2017) { 
                                               if(ano < 2018) {
html2 +='                                     <td style="text-align: center;background-color: LightGrey; height: 14px;" width="25">'
+'                                                2011'
+'                                            </td>';
                                              }
html2 +='                                     <td style="text-align: center;background-color: LightGrey; height: 14px;" width="25">'
+'                                                2012'
+'                                            </td>'
+'                                            <td style="text-align: center;background-color: LightGrey; height: 14px;" width="25">'
+'                                                2013'
+'                                            </td>'
+'                                            <td style="text-align: center;background-color: LightGrey; height: 14px;" width="25">'
+'                                                2014'
+'                                            </td>'
+'                                            <td style="text-align: center;background-color: LightGrey; height: 14px;" width="25">'
+'                                                2015'
+'                                            </td>'
+'                                            <td style="text-align: center;background-color: LightGrey; height: 14px;'+(ano<2018?estiloD:"")+'" width="25">'
+'                                                2016'
+'                                            </td>';
                                            if(ano >= 2018) {
html2 +='                                     <td style="text-align: center;background-color: LightGrey; height: 14px;" width="25">'
+'                                                2017'
+'                                            </td>'
+'                                            <td style="text-align: center;background-color: LightGrey; height: 14px;'+estiloD+'" width="25">'
+'                                                2018'
+'                                            </td>';                                                
                                            }
                                         }
                                     }
html2 +='                                 </tr>';
                                          if(ano < 2018) {
html2 +='                                 <tr>'
+'                                            <td style="vertical-align: top; padding-left: 30px; height: 30px;'+estiloD+'">'
+'                                                        <%=meLanbide48I18n.getMensaje(idiomaUsuario,"label.trayectoria.programasSubvencionables.prog1")%>'
+'                                            </td>'
                                                for(var i = 0; i < numAsociaciones; i++)
                                                {
                                                    if(i == numAsociaciones - 1)
                                                    {
                                                        //Es el ultimo'
                                                        if(i > 0)
                                                        {
                                                            estiloI = "border-left: 1px solid black;";
                                                        }
                                                        else
                                                        {
                                                            estiloI = "";
                                                        }
                                                        estiloD = "";
                                                    }
                                                    else if(i == 0)
                                                    {
                                                        //Es el primero'
                                                        estiloI = "";
                                                        estiloD = "border-right: 1px solid black;";
                                                    }
                                                    else
                                                    {
                                                        //Es intermedio'
                                                        estiloI = "border-left: 1px solid black;";
                                                        estiloD = "border-right: 1px solid black;";
                                                    }
                                                   asociacion = asociaciones[i];
                                                   if(asociacion[0] != null && asociacion[0] != '-'){
                                                        codigoAsociacion = asociacion[0];
                                                    }else{
                                                        codigoAsociacion = '';
                                                    }
                                                    nombreAsociacion = asociacion[1];
                                                    trayectoria = asociacion[2];
                                                   

                                                    if(ano < 2017) { 
html2 +='                                              <td style="text-align: center; height: 30px; vertical-align: top;'+estiloI+'">'
+'                                                        <input type="checkbox" id="chk_dec327_2007_'+i+'" '+ (trayectoria[0] == '1' ? 'checked' : '')+' size="2"/>'
+'                                                    </td>'
+'                                                    <td style="text-align: center; height: 30px; vertical-align: top;">'
+'                                                        <input type="checkbox" id="chk_dec327_2008_'+i+'" '+ (trayectoria[1] == '1' ? 'checked' : '')+' size="2"/>'
+'                                                    </td>'
+'                                                    <td style="text-align: center; height: 30px; vertical-align: top;">'
+'                                                        <input type="checkbox" id="chk_dec327_2009_'+i+'" '+ (trayectoria[2] == '1' ? 'checked' : '')+' size="2"/>'
+'                                                    </td>';
                                                   }
                                                   if(ano < 2018) { 
html2 +='                                             <td style="text-align: center; height: 30px; vertical-align: top;'+(ano<2017?estiloD:"")+'">'
+'                                                        <input type="checkbox" id="chk_dec327_2010_'+i+'" '+ (trayectoria[3] == '1' ? 'checked' : '')+' size="2"/>'
+'                                                    </td>';
                                                   }
                                                   if(ano>=2017){
                                                       //Creamos las celdas para conservas la estructura de la tabla
html2 +='                                            <td style="text-align: center; height: 30px; vertical-align: top;">'
+'                                                    </td>'
+'                                                    <td style="text-align: center; height: 30px; vertical-align: top;">'
+'                                                    </td>'
+'                                                    <td style="text-align: center; height: 30px; vertical-align: top;">'
+'                                                    </td>'
+'                                                    <td style="text-align: center; height: 30px; vertical-align: top;">'
+'                                                    </td>'
+'                                                    <td style="text-align: center; height: 30px; vertical-align: top;">'
+'                                                    </td>';
                                                       // agrego la celda que quito de 2010 para exp. >= de 2018
                                                      if(ano >= 2018) {
html2 +='                                             <td style="text-align: center; height: 30px; vertical-align: top;'+estiloD+'">'
+'                                                    </td>';                                                           
                                                      }
html2 +='                                             <td style="text-align: center; height: 30px; vertical-align: top;'+estiloD+'">'
+'                                                    </td>';                                                       
                                                   }
                                                }
html2 +='                                 </tr>';
                                          }
html2 +='                                 <tr>'
+'                                            <td style="vertical-align: top; padding-left: 30px; height: 30px;'+estiloD+'">'
+'                                                        <%=meLanbide48I18n.getMensaje(idiomaUsuario,"label.trayectoria.programasSubvencionables.prog2")%>'
+'                                            </td>';
                                                for(var i = 0; i < numAsociaciones; i++)
                                                {
                                                    if(i == numAsociaciones - 1)
                                                    {
                                                        //Es el ultimo'
                                                        if(i > 0)
                                                        {
                                                            estiloI = "border-left: 1px solid black;";
                                                        }
                                                        else
                                                        {
                                                            estiloI = "";
                                                        }
                                                        estiloD = "";
                                                    }
                                                    else if(i == 0)
                                                    {
                                                        //Es el primero'
                                                        estiloI = "";
                                                        estiloD = "border-right: 1px solid black;";
                                                    }
                                                    else
                                                    {
                                                        //Es intermedio'
                                                        estiloI = "border-left: 1px solid black;";
                                                        estiloD = "border-right: 1px solid black;";
                                                    }
                                                   asociacion = asociaciones[i];
                                                   if(asociacion[0] != null && asociacion[0] != '-'){
                                                        codigoAsociacion = asociacion[0];
                                                    }else{
                                                        codigoAsociacion = '';
                                                    }
                                                    nombreAsociacion = asociacion[1];
                                                    trayectoria = asociacion[2];
                                                    if(ano < 2017) {
html2 +='                                           <td style="text-align: center; height: 30px; vertical-align: top;'+estiloI+'">'
+'                                                        <input type="checkbox" id="chk_min94_2007_'+i+'" '+ (trayectoria[4] == '1' ? 'checked' : '')+' size="2"/>'
+'                                                    </td>'
+'                                                    <td style="text-align: center; height: 30px; vertical-align: top;">'
+'                                                        <input type="checkbox" id="chk_min94_2008_'+i+'" '+ (trayectoria[5] == '1' ? 'checked' : '')+' size="2"/>'
+'                                                    </td>'
+'                                                    <td style="text-align: center; height: 30px; vertical-align: top;">'
+'                                                        <input type="checkbox" id="chk_min94_2009_'+i+'" '+ (trayectoria[6] == '1' ? 'checked' : '')+' size="2"/>'
+'                                                    </td>';
                                                    }
                                                    if(ano < 2018) { 
html2 +='                                           <td style="text-align: center; height: 30px; vertical-align: top;'+(ano<2017?estiloD:"")+'">'
+'                                                        <input type="checkbox" id="chk_min94_2010_'+i+'" '+ (trayectoria[7] == '1' ? 'checked' : '')+' size="2"/>'
+'                                                    </td>';
                                                    }
                                                    if(ano >= 2017) {
                                                        if(ano < 2018) {
html2 +='                                             <td style="text-align: center; height: 30px; vertical-align: top;">'
+'                                                        <input type="checkbox" id="chk_min94_2011_'+i+'" '+ (trayectoria[8] == '1' ? 'checked' : '')+' size="2"/>'
+'                                                    </td>';
                                                        }
html2 +='                                             <td style="text-align: center; height: 30px; vertical-align: top;">'
+'                                                        <input type="checkbox" id="chk_min94_2012_'+i+'" '+ (trayectoria[9] == '1' ? 'checked' : '')+' size="2"/>'
+'                                                    </td>'
+'                                                    <td style="text-align: center; height: 30px; vertical-align: top;">'
+'                                                        <input type="checkbox" id="chk_min94_2013_'+i+'" '+ (trayectoria[10] == '1' ? 'checked' : '')+' size="2"/>'
+'                                                    </td>'
+'                                                    <td style="text-align: center; height: 30px; vertical-align: top;">'
+'                                                        <input type="checkbox" id="chk_min94_2014_'+i+'" '+ (trayectoria[11] == '1' ? 'checked' : '')+' size="2"/>'
+'                                                    </td>'
+'                                                    <td style="text-align: center; height: 30px; vertical-align: top;">'
+'                                                        <input type="checkbox" id="chk_min94_2015_'+i+'" '+ (trayectoria[12] == '1' ? 'checked' : '')+' size="2"/>'
+'                                                    </td>'
+'                                                    <td style="text-align: center; height: 30px; vertical-align: top;'+(ano<2018?estiloD:"")+'">'
+'                                                        <input type="checkbox" id="chk_min94_2016_'+i+'" '+ (trayectoria[13] == '1' ? 'checked' : '')+' size="2"/>'
+'                                                    </td>';
                                                    if(ano >= 2018) {
html2 +='                                           <td style="text-align: center; height: 30px; vertical-align: top;">'
+'                                                        <input type="checkbox" id="chk_min94_2017_'+i+'" '+ (trayectoria[39] == '1' ? 'checked' : '')+' size="2"/>'
+'                                                    </td>'
+'                                                    <td style="text-align: center; height: 30px; vertical-align: top;'+estiloD+'">'
+'                                                        <input type="checkbox" id="chk_min94_2018_'+i+'" '+ (trayectoria[40] == '1' ? 'checked' : '')+' size="2"/>'
+'                                                    </td>';                                                        
                                                    }
                                                   }
                                                }
html2 +='                                 </tr>'
+'                                        <tr>'
+'                                            <td style="vertical-align: top; padding-left: 30px; height: 30px;'+estiloD+'">'
+'                                                        <%=meLanbide48I18n.getMensaje(idiomaUsuario,"label.trayectoria.programasSubvencionables.prog3")%>'
+'                                            </td>';
                                            for(var i = 0; i < numAsociaciones; i++)
                                                {
                                                    if(i == numAsociaciones - 1)
                                                    {
                                                        //Es el ultimo'
                                                        if(i > 0)
                                                        {
                                                            estiloI = "border-left: 1px solid black;";
                                                        }
                                                        else
                                                        {
                                                            estiloI = "";
                                                        }
                                                        estiloD = "";
                                                    }
                                                    else if(i == 0)
                                                    {
                                                        //Es el primero'
                                                        estiloI = "";
                                                        estiloD = "border-right: 1px solid black;";
                                                    }
                                                    else
                                                    {
                                                        //Es intermedio'
                                                        estiloI = "border-left: 1px solid black;";
                                                        estiloD = "border-right: 1px solid black;";
                                                    }
                                                   asociacion = asociaciones[i];
                                                   if(asociacion[0] != null && asociacion[0] != '-'){
                                                        codigoAsociacion = asociacion[0];
                                                    }else{
                                                        codigoAsociacion = '';
                                                    }
                                                    nombreAsociacion = asociacion[1];
                                                    trayectoria = asociacion[2];
                                                    
                                                    if(ano < 2017) {
html2 +='                                             <td style="text-align: center; height: 30px; vertical-align: top;'+estiloI+'">'
+'                                                        <input type="checkbox" id="chk_min98_2007_'+i+'" '+ (trayectoria[14] == '1' ? 'checked' : '')+' size="2"/>'
+'                                                    </td>'
+'                                                    <td style="text-align: center; height: 30px; vertical-align: top;">'
+'                                                        <input type="checkbox" id="chk_min98_2008_'+i+'" '+ (trayectoria[15] == '1' ? 'checked' : '')+' size="2"/>'
+'                                                    </td>'
+'                                                    <td style="text-align: center; height: 30px; vertical-align: top;">'
+'                                                        <input type="checkbox" id="chk_min98_2009_'+i+'" '+ (trayectoria[16] == '1' ? 'checked' : '')+' size="2"/>'
+'                                                    </td>';
                                                    }
                                                    if(ano < 2018) {
html2 +='                                             <td style="text-align: center; height: 30px; vertical-align: top;'+(ano<2017?estiloD:"")+'">'
+'                                                        <input type="checkbox" id="chk_min98_2010_'+i+'" '+ (trayectoria[17] == '1' ? 'checked' : '')+' size="2"/>'
+'                                                    </td>';
                                                    }
                                                    if(ano >= 2017) {
                                                       if(ano < 2018) {
html2 +='                                             <td style="text-align: center; height: 30px; vertical-align: top;">'
+'                                                        <input type="checkbox" id="chk_min98_2011_'+i+'" '+ (trayectoria[18] == '1' ? 'checked' : '')+' size="2"/>'
+'                                                    </td>';
                                                       }
html2 +='                                              <td style="text-align: center; height: 30px; vertical-align: top;">'
+'                                                        <input type="checkbox" id="chk_min98_2012_'+i+'" '+ (trayectoria[19] == '1' ? 'checked' : '')+' size="2"/>'
+'                                                    </td>'
+'                                                    <td style="text-align: center; height: 30px; vertical-align: top;">'
+'                                                        <input type="checkbox" id="chk_min98_2013_'+i+'" '+ (trayectoria[20] == '1' ? 'checked' : '')+' size="2"/>'
+'                                                    </td>'
+'                                                    <td style="text-align: center; height: 30px; vertical-align: top;">'
+'                                                        <input type="checkbox" id="chk_min98_2014_'+i+'" '+ (trayectoria[21] == '1' ? 'checked' : '')+' size="2"/>'
+'                                                    </td>'
+'                                                    <td style="text-align: center; height: 30px; vertical-align: top;">'
+'                                                        <input type="checkbox" id="chk_min98_2015_'+i+'" '+ (trayectoria[22] == '1' ? 'checked' : '')+' size="2"/>'
+'                                                    </td>'
+'                                                    <td style="text-align: center; height: 30px; vertical-align: top;'+(ano<2018?estiloD:"")+'">'
+'                                                        <input type="checkbox" id="chk_min98_2016_'+i+'" '+ (trayectoria[23] == '1' ? 'checked' : '')+' size="2"/>'
+'                                                    </td>';
                                                      if(ano >= 2018) {
html2 +='                                             <td style="text-align: center; height: 30px; vertical-align: top;">'
+'                                                        <input type="checkbox" id="chk_min98_2017_'+i+'" '+ (trayectoria[41] == '1' ? 'checked' : '')+' size="2"/>'
+'                                                    </td>'
+'                                                    <td style="text-align: center; height: 30px; vertical-align: top;'+estiloD+'">'
+'                                                        <input type="checkbox" id="chk_min98_2018_'+i+'" '+ (trayectoria[42] == '1' ? 'checked' : '')+' size="2"/>'
+'                                                    </td>';                                                          
                                                      }
                                                    }
                                                }
html2 +='                                 </tr>'
+'                                        <tr>'
+'                                            <td style="vertical-align: top; padding-left: 30px; height: 30px;'+estiloD+'">'
+'                                                        <%=meLanbide48I18n.getMensaje(idiomaUsuario,"label.trayectoria.programasSubvencionables.prog4")%>'
+'                                            </td>';
                                            for(var i = 0; i < numAsociaciones; i++)
                                                {
                                                    if(i == numAsociaciones - 1)
                                                    {
                                                        //Es el ultimo'
                                                        if(i > 0)
                                                        {
                                                            estiloI = "border-left: 1px solid black;";
                                                        }
                                                        else
                                                        {
                                                            estiloI = "";
                                                        }
                                                        estiloD = "";
                                                    }
                                                    else if(i == 0)
                                                    {
                                                        //Es el primero'
                                                        estiloI = "";
                                                        estiloD = "border-right: 1px solid black;";
                                                    }
                                                    else
                                                    {
                                                        //Es intermedio'
                                                        estiloI = "border-left: 1px solid black;";
                                                        estiloD = "border-right: 1px solid black;";
                                                    }
                                                   asociacion = asociaciones[i];
                                                   if(asociacion[0] != null && asociacion[0] != '-'){
                                                        codigoAsociacion = asociacion[0];
                                                    }else{
                                                        codigoAsociacion = '';
                                                    }
                                                    nombreAsociacion = asociacion[1];
                                                    trayectoria = asociacion[2];
                                                    
                                                if(ano < 2017) {
html2 +='                                             <td style="text-align: center; height: 30px; vertical-align: top;'+estiloI+'">'
+'                                                        <input type="checkbox" id="chk_tas03_2007_'+i+'" '+ (trayectoria[24] == '1' ? 'checked' : '')+' size="2"/>'
+'                                                    </td>'
+'                                                    <td style="text-align: center; height: 30px; vertical-align: top;">'
+'                                                        <input type="checkbox" id="chk_tas03_2008_'+i+'" '+ (trayectoria[25] == '1' ? 'checked' : '')+' size="2"/>'
+'                                                    </td>'
+'                                                    <td style="text-align: center; height: 30px; vertical-align: top;">'
+'                                                        <input type="checkbox" id="chk_tas03_2009_'+i+'" '+ (trayectoria[26] == '1' ? 'checked' : '')+' size="2"/>'
+'                                                    </td>';
                                                }
                                                if(ano < 2018) {
html2 +='                                             <td style="text-align: center; height: 30px; vertical-align: top;'+(ano<2017?estiloD:"")+'">'
+'                                                        <input type="checkbox" id="chk_tas03_2010_'+i+'" '+ (trayectoria[27] == '1' ? 'checked' : '')+' size="2"/>'
+'                                                    </td>';
                                                }
                                                 if(ano >= 2017) {
                                                     if(ano < 2018) {
html2 +='                                             <td style="text-align: center; height: 30px; vertical-align: top;">'
+'                                                        <input type="checkbox" id="chk_tas03_2011_'+i+'" '+ (trayectoria[28] == '1' ? 'checked' : '')+' size="2"/>'
+'                                                    </td>';
                                                     }
html2 +='                                            <td style="text-align: center; height: 30px; vertical-align: top;">'
+'                                                        <input type="checkbox" id="chk_tas03_2012_'+i+'" '+ (trayectoria[29] == '1' ? 'checked' : '')+' size="2"/>'
+'                                                    </td>'
+'                                                    <td style="text-align: center; height: 30px; vertical-align: top;">'
+'                                                        <input type="checkbox" id="chk_tas03_2013_'+i+'" '+ (trayectoria[30] == '1' ? 'checked' : '')+' size="2"/>'
+'                                                    </td>'
+'                                                    <td style="text-align: center; height: 30px; vertical-align: top;">'
+'                                                        <input type="checkbox" id="chk_tas03_2014_'+i+'" '+ (trayectoria[31] == '1' ? 'checked' : '')+' size="2"/>'
+'                                                    </td>'
+'                                                    <td style="text-align: center; height: 30px; vertical-align: top;">'
+'                                                        <input type="checkbox" id="chk_tas03_2015_'+i+'" '+ (trayectoria[32] == '1' ? 'checked' : '')+' size="2"/>'
+'                                                    </td>'
+'                                                    <td style="text-align: center; height: 30px; vertical-align: top;'+(ano<2018?estiloD:"")+'">'
+'                                                        <input type="checkbox" id="chk_tas03_2016_'+i+'" '+ (trayectoria[33] == '1' ? 'checked' : '')+' size="2"/>'
+'                                                    </td>';
                                                     if(ano >= 2018) {
html2 +='                                             <td style="text-align: center; height: 30px; vertical-align: top;">'
+'                                                        <input type="checkbox" id="chk_tas03_2017_'+i+'" '+ (trayectoria[43] == '1' ? 'checked' : '')+' size="2"/>'
+'                                                    </td>'
+'                                                    <td style="text-align: center; height: 30px; vertical-align: top;'+estiloD+'">'
+'                                                        <input type="checkbox" id="chk_tas03_2018_'+i+'" '+ (trayectoria[44] == '1' ? 'checked' : '')+' size="2"/>'
+'                                                    </td>';
                                                     }
                                                 }
                                               }
html2 +='                                 </tr>'
+'                                        <tr>'
+'                                            <td style="background-color: LightGrey; height: 14px;'+estiloD+'">'
+'                                                        <%=meLanbide48I18n.getMensaje(idiomaUsuario,"label.trayectoria.convocatoria")%>'
+'                                            </td>';
                                                        for(var i = 0; i < numAsociaciones; i++)
                                                        {

                                                            if(i == numAsociaciones - 1)
                                                            {
                                                                estiloD = "";
                                                            }
                                                            else
                                                            {
                                                                estiloD = " border-right: 1px solid black;";
                                                            }
                                                    
                                                        if(ano < 2017) {
html2 +='                                                     <td style="text-align: center;background-color: LightGrey; height: 14px;'+estiloD+'" colspan="4">'
+'                                                            </td>';
                                                         }else{ 
html2 +='                                                     <td style="text-align: center;background-color: LightGrey; height: 14px;'+estiloD+'" colspan="7">'
+'                                                            </td>';
                                                        }
                                                      }
html2 +='                                 </tr>'
+'                                        <tr>'
+'                                            <td style="vertical-align: top; padding-left: 30px; height: 100px;'+estiloD+'">'
+'                                                        <%=meLanbide48I18n.getMensaje(idiomaUsuario,"label.trayectoria.convocatoria.conv1")%>'
+'                                            </td>';
                                                for(var i = 0; i < numAsociaciones; i++)
                                                {
                                                    if(i == numAsociaciones - 1)
                                                    {
                                                        //Es el ultimo'
                                                        if(i > 0)
                                                        {
                                                            estiloI = "border-left: 1px solid black;";
                                                        }
                                                        else
                                                        {
                                                            estiloI = "";
                                                        }
                                                        estiloD = "";
                                                    }
                                                    else if(i == 0)
                                                    {
                                                        //Es el primero'
                                                        estiloI = "";
                                                        estiloD = "border-right: 1px solid black;";
                                                    }
                                                    else
                                                    {
                                                        //Es intermedio'
                                                        estiloI = "border-left: 1px solid black;";
                                                        estiloD = "border-right: 1px solid black;";
                                                    }
                                                   asociacion = asociaciones[i];
                                                   if(asociacion[0] != null && asociacion[0] != '-'){
                                                        codigoAsociacion = asociacion[0];
                                                    }else{
                                                        codigoAsociacion = '';
                                                    }
                                                    nombreAsociacion = asociacion[1];
                                                    trayectoria = asociacion[2];
                                                    
html2 +='                                           <td style="text-align: center; height: 100px; vertical-align: central;'+estiloD+'" colspan="'+colspanN+'">'
+'                                                       <input type="checkbox" id="chk_lan_2011_'+i+'" '+ (trayectoria[34] == '1' ? 'checked' : '')+' size="2"/>'
+'                                                  </td>';
                                          }
html2 +='                                 </tr>'
+'                                        <tr>'
+'                                            <td style="vertical-align: top; padding-left: 30px; height: 100px;'+estiloD+'">' 
+'                                                        <%=meLanbide48I18n.getMensaje(idiomaUsuario,"label.trayectoria.convocatoria.conv2")%>'
+'                                            </td>';
                                            for(var i = 0; i < numAsociaciones; i++)
                                                {
                                                    if(i == numAsociaciones - 1)
                                                    {
                                                        //Es el ultimo'
                                                        if(i > 0)
                                                        {
                                                            estiloI = "border-left: 1px solid black;";
                                                        }
                                                        else
                                                        {
                                                            estiloI = "";
                                                        }
                                                        estiloD = "";
                                                    }
                                                    else if(i == 0)
                                                    {
                                                        //Es el primero'
                                                        estiloI = "";
                                                        estiloD = "border-right: 1px solid black;";
                                                    }
                                                    else
                                                    {
                                                        //Es intermedio'
                                                        estiloI = "border-left: 1px solid black;";
                                                        estiloD = "border-right: 1px solid black;";
                                                    }
                                                   asociacion = asociaciones[i];
                                                   if(asociacion[0] != null && asociacion[0] != '-'){
                                                        codigoAsociacion = asociacion[0];
                                                    }else{
                                                        codigoAsociacion = '';
                                                    }
                                                    nombreAsociacion = asociacion[1];
                                                    trayectoria = asociacion[2];
html2 +='                                           <td style="text-align: center; height: 100px; vertical-align: central;'+estiloD+'" colspan="'+colspanN+'">'
+'                                                        <input type="checkbox" id="chk_lan_2013_'+i+'" '+ (trayectoria[35] == '1' ? 'checked' : '')+' size="2"/>'
+'                                                  </td>';
                                                }
html2 +='                                 </tr>';
                                        if(ano >= 2015) {
html2 +='                                 <tr>'
+'                                            <td style="vertical-align: top; padding-left: 30px; height: 100px;'+estiloD+'">'
+'                                                <%=meLanbide48I18n.getMensaje(idiomaUsuario,"label.trayectoria.convocatoria.conv3")%>'
+'                                            </td>';
                                            for(var i = 0; i < numAsociaciones; i++)
                                                {
                                                    if(i == numAsociaciones - 1)
                                                    {
                                                        //Es el ultimo'
                                                        if(i > 0)
                                                        {
                                                            estiloI = "border-left: 1px solid black;";
                                                        }
                                                        else
                                                        {
                                                            estiloI = "";
                                                        }
                                                        estiloD = "";
                                                    }
                                                    else if(i == 0)
                                                    {
                                                        //Es el primero'
                                                        estiloI = "";
                                                        estiloD = "border-right: 1px solid black;";
                                                    }
                                                    else
                                                    {
                                                        //Es intermedio'
                                                        estiloI = "border-left: 1px solid black;";
                                                        estiloD = "border-right: 1px solid black;";
                                                    }
                                                   asociacion = asociaciones[i];
                                                   if(asociacion[0] != null && asociacion[0] != '-'){
                                                        codigoAsociacion = asociacion[0];
                                                    }else{
                                                        codigoAsociacion = '';
                                                    }
                                                    nombreAsociacion = asociacion[1];
                                                    trayectoria = asociacion[2];
html2 +='                                            <td style="text-align: center; height: 100px; vertical-align: central;'+estiloD+'" colspan="'+colspanN+'">'
+'                                                        <input type="checkbox" id="chk_lan_2014_'+i+'" '+ (trayectoria[36] == '1' ? 'checked' : '')+' size="2"/>'
+'                                                    </td>';
                                                }
html2 +='                                 </tr>';
                                        }
                                        if(ano >= 2017) {
html2 +='                                 <tr>'
+'                                            <td style="vertical-align: top; padding-left: 30px; height: 100px;'+estiloD+'">'
+'                                                    <%=meLanbide48I18n.getMensaje(idiomaUsuario,"label.trayectoria.convocatoria.conv4")%>'
+'                                            </td>';
                                              for(var i = 0; i < numAsociaciones; i++)
                                                {
                                                    if(i == numAsociaciones - 1)
                                                    {
                                                        //Es el ultimo'
                                                        if(i > 0)
                                                        {
                                                            estiloI = "border-left: 1px solid black;";
                                                        }
                                                        else
                                                        {
                                                            estiloI = "";
                                                        }
                                                        estiloD = "";
                                                    }
                                                    else if(i == 0)
                                                    {
                                                        //Es el primero'
                                                        estiloI = "";
                                                        estiloD = "border-right: 1px solid black;";
                                                    }
                                                    else
                                                    {
                                                        //Es intermedio'
                                                        estiloI = "border-left: 1px solid black;";
                                                        estiloD = "border-right: 1px solid black;";
                                                    }
                                                   asociacion = asociaciones[i];
                                                   if(asociacion[0] != null && asociacion[0] != '-'){
                                                        codigoAsociacion = asociacion[0];
                                                    }else{
                                                        codigoAsociacion = '';
                                                    }
                                                    nombreAsociacion = asociacion[1];
                                                    trayectoria = asociacion[2];
html2 +='                                            <td style="text-align: center; height: 100px; vertical-align: central;'+estiloD+'" colspan="'+colspanN+'">'
+'                                                        <input type="checkbox" id="chk_lan_2015_'+i+'" '+ (trayectoria[37] == '1' ? 'checked' : '')+' size="2"/>'
+'                                                    </td>';
                                                }
html2 +='                                 </tr>';
                                       }
                                        if(ano >= 2018) {
html2 +='                                 <tr>'
+'                                            <td style="vertical-align: top; padding-left: 30px; height: 100px;'+estiloD+'">'
+'                                                    <%=meLanbide48I18n.getMensaje(idiomaUsuario,"label.trayectoria.convocatoria.conv5")%>'
+'                                            </td>';
                                              for(var i = 0; i < numAsociaciones; i++)
                                                {
                                                    if(i == numAsociaciones - 1)
                                                    {
                                                        //Es el ultimo'
                                                        if(i > 0)
                                                        {
                                                            estiloI = "border-left: 1px solid black;";
                                                        }
                                                        else
                                                        {
                                                            estiloI = "";
                                                        }
                                                        estiloD = "";
                                                    }
                                                    else if(i == 0)
                                                    {
                                                        //Es el primero'
                                                        estiloI = "";
                                                        estiloD = "border-right: 1px solid black;";
                                                    }
                                                    else
                                                    {
                                                        //Es intermedio'
                                                        estiloI = "border-left: 1px solid black;";
                                                        estiloD = "border-right: 1px solid black;";
                                                    }
                                                   asociacion = asociaciones[i];
                                                   if(asociacion[0] != null && asociacion[0] != '-'){
                                                        codigoAsociacion = asociacion[0];
                                                    }else{
                                                        codigoAsociacion = '';
                                                    }
                                                    nombreAsociacion = asociacion[1];
                                                    trayectoria = asociacion[2];
html2 +='                                            <td style="text-align: center; height: 100px; vertical-align: central;'+estiloD+'" colspan="'+colspanN+'">'
+'                                                        <input type="checkbox" id="chk_lan_2017_'+i+'" '+ (trayectoria[45] == '1' ? 'checked' : '')+' size="2"/>'
+'                                                    </td>';
                                                }
html2 +='                                 </tr>';
                                       }                                       
//html2+='                                    </table>'
//}     
  
//        document.getElementById('divTablaTrayectoria').innerHTML = html2;
//        jQuery('#divTablaTrayectoria')[0].innerHTML = html2;
        document.getElementById('numeroAsociaciones').value = numAsociaciones;
        
//        jQuery('#divTablaTrayectoria').load(document.URL + ' #divTablaTrayectoria');
//        (function ($){
            $.noConflict();
            jQuery(document).ready(function($) {
                'use strict';
                //alert(("Entramos para actualizar #trayectoriaTable via jQuery"));
                //$( "#divTablaTrayectoria" ).append(html2);
                $('#cabeceraTable').html(html2C);
                $('#cuerpoTable').html(html2);
                //document.getElementById('divTablaTrayectoria').innerHTML = html2;
                //$('#divTablaTrayectoria').load(document.URL + ' #divTablaTrayectoria');
                //$("#trayectoriaTable").tableHeadFixer({"left" : 1}); 
                
                var nuevaTablaHtml = $('#divTablaTrayectoria').html();
                //$( "#trayectoriaTable" ).remove();
                $( "divTablaTrayectoria" ).append(nuevaTablaHtml);
                var v = ((document.getElementById("numeroAsociaciones").value  * 300) + 350);
                document.getElementById("trayectoriaTable").width= v+'px'; 
//                $( "trayectoriaTable").css('width',v+'px');
                //$("#trayectoriaTable").tableHeadFixer({"left" : 1}); 
             });
//        })(jQuery);
        

    }
</script>

<div>
    <div id="divTablaTrayectoria"><!--align="center"-->
<!--            <div style="clear: both; overflow-x: hidden; overflow-y: auto; height:450px ;"> 463px
                <div id="divTablaTrayectoria" style="overflow-x: auto; width: 1000px;">-->
                    <!--<table style="height: 434px;" onmousewheel="propagarEvento(event, 'trayectoriaGen');">
                        <tbody>
                            <tr>
                                <td>
                    -->
                               <table id="trayectoriaTable" style="table-layout: fixed" >
                                   <thead id="cabeceraTable">
                                       <tr>
                                            <th style="height: 13px; width: 350px; table-layout: fixed;"  >
                                             </th>
                                             <%
                                                String estiloI = "";
                                                String estiloD = "";

                                                FilaEntidadVO  asociacion = null;
                                                ColecTrayVO tray = null;
                                                String nombreAsociacion = "";
                                                for(int i = 0; i < numAsociaciones; i++)
                                                {

                                                    if(i == numAsociaciones - 1)
                                                    {
                                                        estiloD = "";
                                                    }
                                                    else
                                                    {
                                                        estiloD = " border-right: 1px solid black;";
                                                    }

                                                    asociacion = listaAsociaciones.get(i);

                                                    try
                                                    {
                                                        tray = trayectorias.get(asociacion.getCodEntidad());
                                                    }
                                                    catch(Exception ex)
                                                    {
                                                        tray = null;
                                                    }

                                                    nombreAsociacion = asociacion.getCif() != null ? asociacion.getCif() : "";
                                                    nombreAsociacion += !nombreAsociacion.equals("") && asociacion.getNombre() != null && !asociacion.getNombre().equals("") ? " - " : "";
                                                    nombreAsociacion += asociacion.getNombre() != null ? asociacion.getNombre() : "";
                                            %>  
                                                    <th style="text-align: center;  background-color: #4B95D3; height: 13px;<%=estiloD%>" colspan="<%=anoExp<2017?"4":"7"%>" title="<%=nombreAsociacion%>">
                                                        <div style="overflow: hidden; text-overflow: ellipsis; white-space: nowrap; width: 100%;">
                                                            <%=nombreAsociacion%>
                                                            <input type="hidden" id="codigoAsoc_<%=i%>" value="<%=asociacion.getCodEntidad() != null ? asociacion.getCodEntidad() : ""%>">
                                                        </div>
                                                    </th>
                                            <%
                                                }
                                            %>
                                        </tr>
                                   </thead>
                                   <tbody id="cuerpoTable">
                                        <tr>
                                            <td style="background-color: LightGrey; height: 14px;<%=estiloD%>"  >
                                                <%=meLanbide48I18n.getMensaje(idiomaUsuario,"label.trayectoria.programasSubvencionables")%>
                                            </td>
                                            <%
                                                for(int i = 0; i < numAsociaciones; i++)
                                                {

                                                    if(i == numAsociaciones - 1)
                                                    {
                                                        estiloD = "";
                                                    }
                                                    else
                                                    {
                                                        estiloD = " border-right: 1px solid black;";
                                                    }
                                            %>
                                            <% if(anoExp < 2017) { %>
                                            <td style="text-align: center;background-color: LightGrey; height: 14px; " width="25">
                                                2007
                                            </td>
                                            <td style="text-align: center;background-color: LightGrey; height: 14px;" width="25">
                                                2008
                                            </td>
                                            <td style="text-align: center;background-color: LightGrey; height: 14px;" width="25">
                                                2009
                                            </td>
                                            <% }%>
                                            <% if(anoExp < 2018) { %>
                                            <td style="text-align: center;background-color: LightGrey; height: 14px;<%=anoExp<2017?estiloD:""%>" width="25">
                                                2010
                                            </td>
                                            <% }%>
                                            <% if(anoExp >= 2017) { 
                                                if(anoExp < 2018) {
                                            %>
                                            <td style="text-align: center;background-color: LightGrey; height: 14px;" width="25">
                                                2011
                                            </td>
                                                <% }%>
                                            <td style="text-align: center;background-color: LightGrey; height: 14px;" width="25">
                                                2012
                                            </td>
                                            <td style="text-align: center;background-color: LightGrey; height: 14px;" width="25">
                                                2013
                                            </td>
                                            <td style="text-align: center;background-color: LightGrey; height: 14px;" width="25">
                                                2014
                                            </td>
                                            <td style="text-align: center;background-color: LightGrey; height: 14px;" width="25">
                                                2015
                                            </td>
                                            <td style="text-align: center;background-color: LightGrey; height: 14px;<%=anoExp<2018?estiloD:""%>" width="25">
                                                2016
                                            </td>
                                            <% if(anoExp >= 2018) { %>
                                            <td style="text-align: center;background-color: LightGrey; height: 14px;" width="25">
                                                2017
                                            </td>
                                            <td style="text-align: center;background-color: LightGrey; height: 14px;<%=estiloD%>" width="25">
                                                2018
                                            </td>
                                            <%  } 
                                            } %>
                                        <% }
                                            %>
                                        </tr>
                                        <% if(anoExp < 2018) { %>
                                        <tr>
                                            <td style="vertical-align: top; padding-left: 30px; height: 30px;<%=estiloD%>"  >
                                                        <%=meLanbide48I18n.getMensaje(idiomaUsuario,"label.trayectoria.programasSubvencionables.prog1")%>
                                            </td>
                                            <%
                                                for(int i = 0; i < numAsociaciones; i++)
                                                {

                                                    if(i == numAsociaciones - 1)
                                                    {
                                                        //Es el ultimo
                                                        if(i > 0)
                                                        {
                                                            estiloI = "border-left: 1px solid black;";
                                                        }
                                                        else
                                                        {
                                                            estiloI = "";
                                                        }
                                                        estiloD = "";
                                                    }
                                                    else if(i == 0)
                                                    {
                                                        //Es el primero
                                                        estiloI = "";
                                                        estiloD = "border-right: 1px solid black;";
                                                    }
                                                    else
                                                    {
                                                        //Es intermedio
                                                        estiloI = "border-left: 1px solid black;";
                                                        estiloD = "border-right: 1px solid black;";
                                                    }

                                                    asociacion = listaAsociaciones.get(i);

                                                    try
                                                    {
                                                        tray = trayectorias.get(asociacion.getCodEntidad());
                                                    }
                                                    catch(Exception ex)
                                                    {
                                                        tray = null;
                                                    }
                                            %>
                                                    <% if(anoExp < 2017) { %>
                                                    <td style="text-align: center; height: 30px;  vertical-align: top;<%=estiloI%>">
                                                        <input type="checkbox" id="chk_dec327_2007_<%=i%>" <%=tray != null && tray.getDec327_2007() != null && tray.getDec327_2007() == 1 ? "checked" : ""%> size="2"/>
                                                    </td>
                                                    <td style="text-align: center; height: 30px; vertical-align: top;">
                                                        <input type="checkbox" id="chk_dec327_2008_<%=i%>" <%=tray != null && tray.getDec327_2008() != null && tray.getDec327_2008() == 1 ? "checked" : ""%> size="2"/>
                                                    </td>
                                                    <td style="text-align: center; height: 30px; vertical-align: top;">
                                                        <input type="checkbox" id="chk_dec327_2009_<%=i%>" <%=tray != null && tray.getDec327_2009() != null && tray.getDec327_2009() == 1 ? "checked" : ""%> size="2"/>
                                                    </td>
                                                    <% }%>
                                                    <% if(anoExp < 2018) { %>
                                                    <td style="text-align: center; height: 30px;  vertical-align: top;<%=anoExp<2017?estiloD:""%>">
                                                        <input type="checkbox" id="chk_dec327_2010_<%=i%>" <%=tray != null && tray.getDec327_2010() != null && tray.getDec327_2010() == 1 ? "checked" : ""%> size="2"/>
                                                    </td>
                                                    <% } %>
                                                    <!-- Si es mas de >= 2017 hayque crear las celdas vacias para no descuadrar la tabla-->
                                                    <% if(anoExp >= 2017) { %>
                                                    <td style="text-align: center; height: 30px; vertical-align: top;">
                                                    </td>
                                                    <td style="text-align: center; height: 30px; vertical-align: top;">
                                                    </td>
                                                    <td style="text-align: center; height: 30px; vertical-align: top;">
                                                    </td>
                                                    <td style="text-align: center; height: 30px; vertical-align: top;">
                                                    </td>
                                                    <td style="text-align: center; height: 30px; vertical-align: top;">
                                                    </td>
                                                    <!-- Agregamos la de 2010 que hemos quitado para exp desde 2018 -->
                                                    <% if(anoExp >= 2018) { %>
                                                    <td style="text-align: center; height: 30px; vertical-align: top;">
                                                    </td>
                                                    <% } %>
                                                    <td style="text-align: center; height: 30px; vertical-align: top;<%=estiloD%>">
                                                    </td>
                                                    <% }%>
                                            <%
                                                }
                                            %>
                                        </tr>
                                        <% } %>
                                        <tr>
                                            <td style="vertical-align: top; padding-left: 30px; height: 30px;<%=estiloD%>" >
                                                        <%=meLanbide48I18n.getMensaje(idiomaUsuario,"label.trayectoria.programasSubvencionables.prog2")%>
                                            </td>
                                            <%
                                                for(int i = 0; i < numAsociaciones; i++)
                                                {
                                                    if(i == numAsociaciones - 1)
                                                    {
                                                        //Es el ultimo
                                                        if(i > 0)
                                                        {
                                                            estiloI = "border-left: 1px solid black;";
                                                        }
                                                        else
                                                        {
                                                            estiloI = "";
                                                        }
                                                        estiloD = "";
                                                    }
                                                    else if(i == 0)
                                                    {
                                                        //Es el primero
                                                        estiloI = "";
                                                        estiloD = "border-right: 1px solid black;";
                                                    }
                                                    else
                                                    {
                                                        //Es intermedio
                                                        estiloI = "border-left: 1px solid black;";
                                                        estiloD = "border-right: 1px solid black;";
                                                    }

                                                    asociacion = listaAsociaciones.get(i);

                                                    try
                                                    {
                                                        tray = trayectorias.get(asociacion.getCodEntidad());
                                                    }
                                                    catch(Exception ex)
                                                    {
                                                        tray = null;
                                                    }
                                            %>
                                                    <% if(anoExp < 2017) { %>
                                                    <td style="text-align: center; height: 30px;  vertical-align: top;<%=estiloI%>">
                                                        <input type="checkbox" id="chk_min94_2007_<%=i%>" <%=tray != null && tray.getMin94_2007() != null && tray.getMin94_2007() == 1 ? "checked" : ""%> size="2"/>
                                                    </td>
                                                    <td style="text-align: center; height: 30px; vertical-align: top;">
                                                        <input type="checkbox" id="chk_min94_2008_<%=i%>" <%=tray != null && tray.getMin94_2008() != null && tray.getMin94_2008() == 1 ? "checked" : ""%> size="2"/>
                                                    </td>
                                                    <td style="text-align: center; height: 30px; vertical-align: top;">
                                                        <input type="checkbox" id="chk_min94_2009_<%=i%>" <%=tray != null && tray.getMin94_2009() != null && tray.getMin94_2009() == 1 ? "checked" : ""%> size="2"/>
                                                    </td>
                                                    <% }%>
                                                    <% if(anoExp < 2018) { %>
                                                    <td style="text-align: center; height: 30px; vertical-align: top;<%=anoExp<2017?estiloD:""%>">
                                                        <input type="checkbox" id="chk_min94_2010_<%=i%>" <%=tray != null && tray.getMin94_2010() != null && tray.getMin94_2010() == 1 ? "checked" : ""%> size="2"/>
                                                    </td>
                                                    <% }%>
                                                    <% if(anoExp >= 2017) { 
                                                        if(anoExp < 2018) { %>
                                                    <td style="text-align: center; height: 30px; vertical-align: top;">
                                                        <input type="checkbox" id="chk_min94_2011_<%=i%>" <%=tray != null && tray.getMin94_2011() != null && tray.getMin94_2011() == 1 ? "checked" : ""%> size="2"/>
                                                    </td>
                                                    <% } %>
                                                    <td style="text-align: center; height: 30px; vertical-align: top;">
                                                        <input type="checkbox" id="chk_min94_2012_<%=i%>" <%=tray != null && tray.getMin94_2012() != null && tray.getMin94_2012() == 1 ? "checked" : ""%> size="2"/>
                                                    </td>
                                                    <td style="text-align: center; height: 30px; vertical-align: top;">
                                                        <input type="checkbox" id="chk_min94_2013_<%=i%>" <%=tray != null && tray.getMin94_2013() != null && tray.getMin94_2013() == 1 ? "checked" : ""%> size="2"/>
                                                    </td>
                                                    <td style="text-align: center; height: 30px; vertical-align: top;">
                                                        <input type="checkbox" id="chk_min94_2014_<%=i%>" <%=tray != null && tray.getMin94_2014() != null && tray.getMin94_2014() == 1 ? "checked" : ""%> size="2"/>
                                                    </td>
                                                    <td style="text-align: center; height: 30px; vertical-align: top;">
                                                        <input type="checkbox" id="chk_min94_2015_<%=i%>" <%=tray != null && tray.getMin94_2015() != null && tray.getMin94_2015() == 1 ? "checked" : ""%> size="2"/>
                                                    </td>
                                                    <td style="text-align: center; height: 30px; vertical-align: top;<%=anoExp<2018?estiloD:""%>">
                                                        <input type="checkbox" id="chk_min94_2016_<%=i%>" <%=tray != null && tray.getMin94_2016() != null && tray.getMin94_2016() == 1 ? "checked" : ""%> size="2"/>
                                                    </td>
                                                    <% if(anoExp >= 2018) { %>
                                                    <td style="text-align: center; height: 30px; vertical-align: top;">
                                                        <input type="checkbox" id="chk_min94_2017_<%=i%>" <%=tray != null && tray.getMin94_2017() != null && tray.getMin94_2017() == 1 ? "checked" : ""%> size="2"/>
                                                    </td>
                                                    <td style="text-align: center; height: 30px; vertical-align: top;<%=estiloD%>">
                                                        <input type="checkbox" id="chk_min94_2018_<%=i%>" <%=tray != null && tray.getMin94_2018() != null && tray.getMin94_2018() == 1 ? "checked" : ""%> size="2"/>
                                                    </td>
                                                    <% }
                                                    }%>
                                            <%
                                                }
                                            %>
                                        </tr>
                                        <tr>
                                            <td style="vertical-align: top; padding-left: 30px; height: 30px;<%=estiloD%>"  >
                                                        <%=meLanbide48I18n.getMensaje(idiomaUsuario,"label.trayectoria.programasSubvencionables.prog3")%>
                                            </td>
                                            <%
                                                for(int i = 0; i < numAsociaciones; i++)
                                                {
                                                    if(i == numAsociaciones - 1)
                                                    {
                                                        //Es el ultimo
                                                        if(i > 0)
                                                        {
                                                            estiloI = "border-left: 1px solid black;";
                                                        }
                                                        else
                                                        {
                                                            estiloI = "";
                                                        }
                                                        estiloD = "";
                                                    }
                                                    else if(i == 0)
                                                    {
                                                        //Es el primero
                                                        estiloI = "";
                                                        estiloD = "border-right: 1px solid black;";
                                                    }
                                                    else
                                                    {
                                                        //Es intermedio
                                                        estiloI = "border-left: 1px solid black;";
                                                        estiloD = "border-right: 1px solid black;";
                                                    }

                                                    asociacion = listaAsociaciones.get(i);

                                                    try
                                                    {
                                                        tray = trayectorias.get(asociacion.getCodEntidad());
                                                    }
                                                    catch(Exception ex)
                                                    {
                                                        tray = null;
                                                    }
                                            %>
                                                    <% if(anoExp < 2017) { %>
                                                    <td style="text-align: center; height: 30px; vertical-align: top;<%=estiloI%>">
                                                        <input type="checkbox" id="chk_min98_2007_<%=i%>" <%=tray != null && tray.getMin98_2007() != null && tray.getMin98_2007() == 1 ? "checked" : ""%> size="2"/>
                                                    </td>
                                                    <td style="text-align: center; height: 30px; vertical-align: top;">
                                                        <input type="checkbox" id="chk_min98_2008_<%=i%>" <%=tray != null && tray.getMin98_2008() != null && tray.getMin98_2008() == 1 ? "checked" : ""%> size="2"/>
                                                    </td>
                                                    <td style="text-align: center; height: 30px; vertical-align: top;">
                                                        <input type="checkbox" id="chk_min98_2009_<%=i%>" <%=tray != null && tray.getMin98_2009() != null && tray.getMin98_2009() == 1 ? "checked" : ""%> size="2"/>
                                                    </td>
                                                    <% } 
                                                    if(anoExp <= 2017) { %>
                                                    <td style="text-align: center; height: 30px; vertical-align: top;<%=anoExp<2017?estiloD:""%>">
                                                        <input type="checkbox" id="chk_min98_2010_<%=i%>" <%=tray != null && tray.getMin98_2010() != null && tray.getMin98_2010() == 1 ? "checked" : ""%> size="2"/>
                                                    </td>
                                                    <%} 
                                                    if(anoExp >= 2017) {
                                                      if(anoExp < 2018){
                                                    %>
                                                    <td style="text-align: center; height: 30px; vertical-align: top;">
                                                        <input type="checkbox" id="chk_min98_2011_<%=i%>" <%=tray != null && tray.getMin98_2011() != null && tray.getMin98_2011() == 1 ? "checked" : ""%> size="2"/>
                                                    </td>
                                                    <% }%>
                                                    <td style="text-align: center; height: 30px; vertical-align: top;">
                                                        <input type="checkbox" id="chk_min98_2012_<%=i%>" <%=tray != null && tray.getMin98_2012() != null && tray.getMin98_2012() == 1 ? "checked" : ""%> size="2"/>
                                                    </td>
                                                    <td style="text-align: center; height: 30px; vertical-align: top;">
                                                        <input type="checkbox" id="chk_min98_2013_<%=i%>" <%=tray != null && tray.getMin98_2013() != null && tray.getMin98_2013() == 1 ? "checked" : ""%> size="2"/>
                                                    </td>
                                                    <td style="text-align: center; height: 30px; vertical-align: top;">
                                                        <input type="checkbox" id="chk_min98_2014_<%=i%>" <%=tray != null && tray.getMin98_2014() != null && tray.getMin98_2014() == 1 ? "checked" : ""%> size="2"/>
                                                    </td>
                                                    <td style="text-align: center; height: 30px; vertical-align: top;">
                                                        <input type="checkbox" id="chk_min98_2015_<%=i%>" <%=tray != null && tray.getMin98_2015() != null && tray.getMin98_2015() == 1 ? "checked" : ""%> size="2"/>
                                                    </td>
                                                    <td style="text-align: center; height: 30px; vertical-align: top;<%=anoExp<2018?estiloD:""%>">
                                                        <input type="checkbox" id="chk_min98_2016_<%=i%>" <%=tray != null && tray.getMin98_2016() != null && tray.getMin98_2016() == 1 ? "checked" : ""%> size="2"/>
                                                    </td>
                                                    <%if(anoExp >= 2018) {%>
                                                    <td style="text-align: center; height: 30px; vertical-align: top;">
                                                        <input type="checkbox" id="chk_min98_2017_<%=i%>" <%=tray != null && tray.getMin98_2017() != null && tray.getMin98_2017() == 1 ? "checked" : ""%> size="2"/>
                                                    </td>
                                                    <td style="text-align: center; height: 30px; vertical-align: top;<%=estiloD%>">
                                                        <input type="checkbox" id="chk_min98_2018_<%=i%>" <%=tray != null && tray.getMin98_2018() != null && tray.getMin98_2018() == 1 ? "checked" : ""%> size="2"/>
                                                    </td>
                                                    <% }
                                                    }%>
                                            <%
                                                }
                                            %>
                                        </tr>
                                        <tr>
                                            <td style="vertical-align: top; padding-left: 30px;  height: 30px;<%=estiloD%>" >
                                                        <%=meLanbide48I18n.getMensaje(idiomaUsuario,"label.trayectoria.programasSubvencionables.prog4")%>
                                            </td>
                                            <%
                                                for(int i = 0; i < numAsociaciones; i++)
                                                {
                                                    if(i == numAsociaciones - 1)
                                                    {
                                                        //Es el ultimo
                                                        if(i > 0)
                                                        {
                                                            estiloI = "border-left: 1px solid black;";
                                                        }
                                                        else
                                                        {
                                                            estiloI = "";
                                                        }
                                                        estiloD = "";
                                                    }
                                                    else if(i == 0)
                                                    {
                                                        //Es el primero
                                                        estiloI = "";
                                                        estiloD = "border-right: 1px solid black;";
                                                    }
                                                    else
                                                    {
                                                        //Es intermedio
                                                        estiloI = "border-left: 1px solid black;";
                                                        estiloD = "border-right: 1px solid black;";
                                                    }

                                                    asociacion = listaAsociaciones.get(i);

                                                    try
                                                    {
                                                        tray = trayectorias.get(asociacion.getCodEntidad());
                                                    }
                                                    catch(Exception ex)
                                                    {
                                                        tray = null;
                                                    }
                                            %>
                                                <% if(anoExp < 2017) { %>    
                                                    <td style="text-align: center; height: 30px; vertical-align: top;<%=estiloI%>">
                                                        <input type="checkbox" id="chk_tas03_2007_<%=i%>" <%=tray != null && tray.getTas03_2007() != null && tray.getTas03_2007() == 1 ? "checked" : ""%> size="2"/>
                                                    </td>
                                                    <td style="text-align: center; height: 30px; vertical-align: top;">
                                                        <input type="checkbox" id="chk_tas03_2008_<%=i%>" <%=tray != null && tray.getTas03_2008() != null && tray.getTas03_2008() == 1 ? "checked" : ""%> size="2"/>
                                                    </td>
                                                    <td style="text-align: center; height: 30px; vertical-align: top;">
                                                        <input type="checkbox" id="chk_tas03_2009_<%=i%>" <%=tray != null && tray.getTas03_2009() != null && tray.getTas03_2009() == 1 ? "checked" : ""%> size="2"/>
                                                    </td>
                                                <% }
                                                if(anoExp < 2017) {%>
                                                    <td style="text-align: center; height: 30px; vertical-align: top;<%=anoExp<2017?estiloD:""%>">
                                                        <input type="checkbox" id="chk_tas03_2010_<%=i%>" <%=tray != null && tray.getTas03_2010() != null && tray.getTas03_2010() == 1 ? "checked" : ""%> size="2"/>
                                                    </td>
                                                <% }
                                                   if(anoExp >= 2017) { 
                                                    if(anoExp < 2018) { 
                                                %>
                                                    <td style="text-align: center; height: 30px; vertical-align: top;">
                                                        <input type="checkbox" id="chk_tas03_2011_<%=i%>" <%=tray != null && tray.getTas03_2011() != null && tray.getTas03_2011() == 1 ? "checked" : ""%> size="2"/>
                                                    </td>
                                                <% }%>
                                                    <td style="text-align: center; height: 30px; vertical-align: top;">
                                                        <input type="checkbox" id="chk_tas03_2012_<%=i%>" <%=tray != null && tray.getTas03_2012() != null && tray.getTas03_2012() == 1 ? "checked" : ""%> size="2"/>
                                                    </td>
                                                    <td style="text-align: center; height: 30px; vertical-align: top;">
                                                        <input type="checkbox" id="chk_tas03_2013_<%=i%>" <%=tray != null && tray.getTas03_2013() != null && tray.getTas03_2013() == 1 ? "checked" : ""%> size="2"/>
                                                    </td>
                                                    <td style="text-align: center; height: 30px; vertical-align: top;">
                                                        <input type="checkbox" id="chk_tas03_2014_<%=i%>" <%=tray != null && tray.getTas03_2014() != null && tray.getTas03_2014() == 1 ? "checked" : ""%> size="2"/>
                                                    </td>
                                                    <td style="text-align: center; height: 30px; vertical-align: top;">
                                                        <input type="checkbox" id="chk_tas03_2015_<%=i%>" <%=tray != null && tray.getTas03_2015() != null && tray.getTas03_2015() == 1 ? "checked" : ""%> size="2"/>
                                                    </td>
                                                    <td style="text-align: center; height: 30px; vertical-align: top;<%=anoExp<2018?estiloD:""%>">
                                                        <input type="checkbox" id="chk_tas03_2016_<%=i%>" <%=tray != null && tray.getTas03_2016() != null && tray.getTas03_2016() == 1 ? "checked" : ""%> size="2"/>
                                                    </td>
                                                    <% if(anoExp >= 2018) { %>
                                                    <td style="text-align: center; height: 30px; vertical-align: top;">
                                                        <input type="checkbox" id="chk_tas03_2017_<%=i%>" <%=tray != null && tray.getTas03_2017() != null && tray.getTas03_2017() == 1 ? "checked" : ""%> size="2"/>
                                                    </td>
                                                    <td style="text-align: center; height: 30px; vertical-align: top;<%=estiloD%>">
                                                        <input type="checkbox" id="chk_tas03_2018_<%=i%>" <%=tray != null && tray.getTas03_2018() != null && tray.getTas03_2018() == 1 ? "checked" : ""%> size="2"/>
                                                    </td>                                                    
                                                <% }
                                                } %>
                                            <%
                                                }
                                            %>
                                        </tr>
                                        <tr>
                                            <td style="background-color: LightGrey; height: 14px;<%=estiloD%>" >
                                                        <%=meLanbide48I18n.getMensaje(idiomaUsuario,"label.trayectoria.convocatoria")%>
                                            </td>
                                            <%
                                                        for(int i = 0; i < numAsociaciones; i++)
                                                        {

                                                            if(i == numAsociaciones - 1)
                                                            {
                                                                estiloD = "";
                                                            }
                                                            else
                                                            {
                                                                estiloD = " border-right: 1px solid black;";
                                                            }
                                                    %>
                                                        <% if(anoExp < 2017) { %>
                                                            <td style="text-align: center;background-color: LightGrey; height: 14px;<%=estiloD%>" colspan="4">

                                                            </td>
                                                        <% }else{ %>
                                                            <td style="text-align: center;background-color: LightGrey; height: 14px;<%=estiloD%>" colspan="7">

                                                            </td>
                                                        <% }%>
                                                    <%
                                                        }
                                                    %>
                                        </tr>
                                        <tr>
                                            <td style="vertical-align: top; padding-left: 30px; height: 100px;<%=estiloD%>" >
                                                        <%=meLanbide48I18n.getMensaje(idiomaUsuario,"label.trayectoria.convocatoria.conv1")%>
                                            </td>
                                            
                                                    <%
                                                        for(int i = 0; i < numAsociaciones; i++)
                                                        {
                                                            if(i == numAsociaciones - 1)
                                                            {
                                                                //Es el ultimo
                                                                if(i > 0)
                                                                {
                                                                    estiloI = "border-left: 1px solid black;";
                                                                }
                                                                else
                                                                {
                                                                    estiloI = "";
                                                                }
                                                                estiloD = "";
                                                            }
                                                            else if(i == 0)
                                                            {
                                                                //Es el primero
                                                                estiloI = "";
                                                                estiloD = "border-right: 1px solid black;";
                                                            }
                                                            else
                                                            {
                                                                //Es intermedio
                                                                estiloI = "border-left: 1px solid black;";
                                                                estiloD = "border-right: 1px solid black;";
                                                            }

                                                            asociacion = listaAsociaciones.get(i);

                                                            try
                                                            {
                                                                tray = trayectorias.get(asociacion.getCodEntidad());
                                                            }
                                                            catch(Exception ex)
                                                            {
                                                                tray = null;
                                                            }
                                                    %>
                                                            <td style="text-align: center; height: 100px; vertical-align: central;<%=estiloD%>" colspan="<%=anoExp<2017?"4":"7"%>">
                                                                <input type="checkbox" id="chk_lan_2011_<%=i%>" <%=tray != null && tray.getLan_2011() != null && tray.getLan_2011() == 1 ? "checked" : ""%> size="2"/>
                                                            </td>
                                                    <%
                                                        }
                                                    %>
                                        </tr>
                                        <tr>
                                            <td style="vertical-align: top; padding-left: 30px; height: 100px;<%=estiloD%>" >
                                                        <%=meLanbide48I18n.getMensaje(idiomaUsuario,"label.trayectoria.convocatoria.conv2")%>
                                            </td>
                                                   <%
                                                        for(int i = 0; i < numAsociaciones; i++)
                                                        {
                                                            if(i == numAsociaciones - 1)
                                                            {
                                                                //Es el ultimo
                                                                if(i > 0)
                                                                {
                                                                    estiloI = "border-left: 1px solid black;";
                                                                }
                                                                else
                                                                {
                                                                    estiloI = "";
                                                                }
                                                                estiloD = "";
                                                            }
                                                            else if(i == 0)
                                                            {
                                                                //Es el primero
                                                                estiloI = "";
                                                                estiloD = "border-right: 1px solid black;";
                                                            }
                                                            else
                                                            {
                                                                //Es intermedio
                                                                estiloI = "border-left: 1px solid black;";
                                                                estiloD = "border-right: 1px solid black;";
                                                            }

                                                            asociacion = listaAsociaciones.get(i);

                                                            try
                                                            {
                                                                tray = trayectorias.get(asociacion.getCodEntidad());
                                                            }
                                                            catch(Exception ex)
                                                            {
                                                                tray = null;
                                                            }
                                                    %>
                                                            <td style="text-align: center; height: 100px; vertical-align: central;<%=estiloD%>" colspan="<%=anoExp<2017?"4":"7"%>">
                                                                <input type="checkbox" id="chk_lan_2013_<%=i%>" <%=tray != null && tray.getLan_2013() != null && tray.getLan_2013() == 1 ? "checked" : ""%> size="2"/>
                                                            </td>
                                                    <%
                                                        }
                                                    %>
                                        </tr>
                                        <% if(anoExp >= 2015) { %>
                                        <tr>
                                            <td style="vertical-align: top; padding-left: 30px; height: 100px;<%=estiloD%>" >
                                                <%=meLanbide48I18n.getMensaje(idiomaUsuario,"label.trayectoria.convocatoria.conv3")%>
                                            </td>
                                            <%
                                                for(int i = 0; i < numAsociaciones; i++)
                                                {
                                                    if(i == numAsociaciones - 1)
                                                    {
                                                        //Es el ultimo
                                                        if(i > 0)
                                                        {
                                                            estiloI = "border-left: 1px solid black;";
                                                        }
                                                        else
                                                        {
                                                            estiloI = "";
                                                        }
                                                        estiloD = "";
                                                    }
                                                    else if(i == 0)
                                                    {
                                                        //Es el primero
                                                        estiloI = "";
                                                        estiloD = "border-right: 1px solid black;";
                                                    }
                                                    else
                                                    {
                                                        //Es intermedio
                                                        estiloI = "border-left: 1px solid black;";
                                                        estiloD = "border-right: 1px solid black;";
                                                    }

                                                    asociacion = listaAsociaciones.get(i);

                                                    try
                                                    {
                                                        tray = trayectorias.get(asociacion.getCodEntidad());
                                                    }
                                                    catch(Exception ex)
                                                    {
                                                        tray = null;
                                                    }
                                            %>
                                                    <td style="text-align: center; height: 100px; vertical-align: central;<%=estiloD%>" colspan="<%=anoExp<2017?"4":"7"%>">
                                                        <input type="checkbox" id="chk_lan_2014_<%=i%>" <%=tray != null && tray.getLan_2014() != null && tray.getLan_2014() == 1 ? "checked" : ""%> size="2"/>
                                                    </td>
                                            <%
                                                }
                                            %>
                                        </tr>
                                        <% }%>
                                        <% if(anoExp >= 2017) { %>
                                        <tr>
                                            <td style="vertical-align: top; padding-left: 30px; height: 100px;<%=estiloD%>" >
                                                    <%=meLanbide48I18n.getMensaje(idiomaUsuario,"label.trayectoria.convocatoria.conv4")%>
                                            </td>
                                            <%
                                                for(int i = 0; i < numAsociaciones; i++)
                                                {
                                                    if(i == numAsociaciones - 1)
                                                    {
                                                        //Es el ultimo
                                                        if(i > 0)
                                                        {
                                                            estiloI = "border-left: 1px solid black;";
                                                        }
                                                        else
                                                        {
                                                            estiloI = "";
                                                        }
                                                        estiloD = "";
                                                    }
                                                    else if(i == 0)
                                                    {
                                                        //Es el primero
                                                        estiloI = "";
                                                        estiloD = "border-right: 1px solid black;";
                                                    }
                                                    else
                                                    {
                                                        //Es intermedio
                                                        estiloI = "border-left: 1px solid black;";
                                                        estiloD = "border-right: 1px solid black;";
                                                    }

                                                    asociacion = listaAsociaciones.get(i);

                                                    try
                                                    {
                                                        tray = trayectorias.get(asociacion.getCodEntidad());
                                                    }
                                                    catch(Exception ex)
                                                    {
                                                        tray = null;
                                                    }
                                            %>
                                                    <td style="text-align: center; height: 100px; vertical-align: central;<%=estiloD%>" colspan="7">
                                                        <input type="checkbox" id="chk_lan_2015_<%=i%>" <%=tray != null && tray.getLan_2015() != null && tray.getLan_2015() == 1 ? "checked" : ""%> size="2"/>
                                                    </td>
                                            <%
                                                }
                                            %>
                                        </tr>
                                    <% }%>
                                    <% if(anoExp >= 2018) { %>
                                        <tr>
                                            <td style="vertical-align: top; padding-left: 30px; height: 100px;<%=estiloD%>" >
                                                    <%=meLanbide48I18n.getMensaje(idiomaUsuario,"label.trayectoria.convocatoria.conv5")%>
                                            </td>
                                            <%
                                                for(int i = 0; i < numAsociaciones; i++)
                                                {
                                                    if(i == numAsociaciones - 1)
                                                    {
                                                        //Es el ultimo
                                                        if(i > 0)
                                                        {
                                                            estiloI = "border-left: 1px solid black;";
                                                        }
                                                        else
                                                        {
                                                            estiloI = "";
                                                        }
                                                        estiloD = "";
                                                    }
                                                    else if(i == 0)
                                                    {
                                                        //Es el primero
                                                        estiloI = "";
                                                        estiloD = "border-right: 1px solid black;";
                                                    }
                                                    else
                                                    {
                                                        //Es intermedio
                                                        estiloI = "border-left: 1px solid black;";
                                                        estiloD = "border-right: 1px solid black;";
                                                    }

                                                    asociacion = listaAsociaciones.get(i);

                                                    try
                                                    {
                                                        tray = trayectorias.get(asociacion.getCodEntidad());
                                                    }
                                                    catch(Exception ex)
                                                    {
                                                        tray = null;
                                                    }
                                            %>
                                                    <td style="text-align: center; height: 100px; vertical-align: central;<%=estiloD%>" colspan="7">
                                                        <input type="checkbox" id="chk_lan_2017_<%=i%>" <%=tray != null && tray.getLan_2017() != null && tray.getLan_2017() == 1 ? "checked" : ""%> size="2"/>
                                                    </td>
                                            <%
                                                }
                                            %>
                                        </tr>
                                    <% }%>
                               </tbody>
                                        
                                    </table>
                </div>        
                <input type="hidden" id="numeroAsociaciones" />
                <input type="hidden" id="anoExpediente" />
                <div class="botonera">
                    <input type="button" id="btnGuardarTrayectoriaColec" name="btnGuardarTrayectoriaColec" class="botonGeneral" value="<%=meLanbide48I18n.getMensaje(idiomaUsuario,"btn.guardar")%>" onclick="guardarTrayectoria();"/>
                </div>
<!--            </div>
        </div>-->
</div>

<script type="text/javascript">   
    document.getElementById('numeroAsociaciones').value = <%=numAsociaciones%>;
    document.getElementById('anoExpediente').value = <%=anoExp%>;
    
    jQuery(function($){
        $("#trayectoriaTable").tableHeadFixer({"left" : 1});
        
        $("#divTablaTrayectoria").on("load",function (){
               alert("Salta evento carga divpadre con on event");
               //$("#trayectoriaTable").tableHeadFixer({"left" : 1});
            }).each(function() {
            if(this.complete) $(this).load();
        });
        $( "#divTablaTrayectoria" ).load(function (){
               alert("Salta evento carga divpadre con load event");
               //$("#trayectoriaTable").tableHeadFixer({"left" : 1});
            });
        });
                
    
    
   
//    jQuery(document).ready(function($) {
//         $("#trayectoriaTable").tableHeadFixer({"left" : 1});
//    });
    
    jQuery("#divTablaTrayectoria").on("load","table",function (event){
               alert("Salta evento carga divpadre" + event.toString()); 
               jQuery("#trayectoriaTable").tableHeadFixer({"left" : 1});
            });

            
</script>

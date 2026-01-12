<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>

<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide47.i18n.MeLanbide47I18n" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@ page import="es.altia.common.service.config.Config"%>
<%@ page import="es.altia.common.service.config.ConfigServiceHelper"%>
<%@page import="java.util.ArrayList" %>
<%@page import="java.util.List" %>
<%@page import="java.util.Map" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide47.vo.orientacion.FilaAsociacionVO"%>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide47.vo.orientacion.OriTrayectoriaVO"%>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide47.util.ConstantesMeLanbide47"%>

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
    MeLanbide47I18n meLanbide47I18n = MeLanbide47I18n.getInstance();
    
    Config m_Config = ConfigServiceHelper.getConfig("common");
    String statusBar = m_Config.getString("JSP.StatusBar");
    String nombreModulo     = request.getParameter("nombreModulo");
    String codOrganizacion  = request.getParameter("codOrganizacionModulo");
    String numExpediente    = request.getParameter("numero");
    String mensajeProgreso = "";
    
    String[] datos = numExpediente.split("/");
    int anoExp = Integer.parseInt(datos[0]);
    int colspanNHtml = 4;
    int anchoTabla = 350;
    if(anoExp==2017){
        colspanNHtml=7;
        anchoTabla=350;
    }
    else if(anoExp>=2018){
        colspanNHtml=8;
        anchoTabla=420;
    }
    
    List<FilaAsociacionVO> listaAsociaciones = (List<FilaAsociacionVO>)request.getAttribute("asociaciones");
    int numAsociaciones = 0;
    try
    {
        numAsociaciones = listaAsociaciones != null ? listaAsociaciones.size() : 0;
    }
    catch(Exception ex)
    {
        
    }
    
    Map<Long, OriTrayectoriaVO> trayectorias = (Map<Long, OriTrayectoriaVO>)request.getAttribute("trayectoriasGeneral");
%>

<jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
<jsp:setProperty name="descriptor"  property="idi_cod" value="<%=idiomaUsuario%>" />
<jsp:setProperty name="descriptor"  property="apl_cod" value="<%=apl%>" />
<link rel="StyleSheet" media="screen" type="text/css" href="<%=request.getContextPath()%><%=css%>">
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/>
<link rel="stylesheet" type="text/css" href="<c:url value='/css/extension/melanbide47/melanbide47.css'/>">
<link rel="stylesheet" type="text/css" href="<c:url value='/css/table.css'/>">

<script type="text/javascript" src="<c:url value='/scripts/popup.js'/>"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/melanbide47/FixedColumnsTable.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/melanbide47/moment-with-locales.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/melanbide47/ori14Utils.js"></script>

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
            parametros = 'tarea=preparar&modulo=MELANBIDE47&operacion=guardarTrayectoriaORI&tipo=0&numero=<%=numExpediente%>'
                        +'&numAsociaciones=' + numAsociaciones;
                    
                        for(var i = 0; i < numAsociaciones; i++){
                            parametros += '&codigoAsoc_'+i+'='+document.getElementById('codigoAsoc_'+i).value
                            +'&rad_dec327_'+i+'='+(document.getElementById('rad_dec327_S_'+i)!=null && document.getElementById('rad_dec327_S_'+i).checked ? 1 : document.getElementById('rad_dec327_N_'+i)!=null && document.getElementById('rad_dec327_N_'+i).checked ? 0 : "")
                            +'&chk_dec327_2007_'+i+'='+(document.getElementById('chk_dec327_2007_'+i)!=null && document.getElementById('chk_dec327_2007_'+i).checked ? 1 : 0)
                            +'&chk_dec327_2008_'+i+'='+(document.getElementById('chk_dec327_2008_'+i)!=null && document.getElementById('chk_dec327_2008_'+i).checked ? 1 : 0)
                            +'&chk_dec327_2009_'+i+'='+(document.getElementById('chk_dec327_2009_'+i)!=null && document.getElementById('chk_dec327_2009_'+i).checked ? 1 : 0)
                            +'&chk_dec327_2010_'+i+'='+(document.getElementById('chk_dec327_2010_'+i)!=null && document.getElementById('chk_dec327_2010_'+i).checked ? 1 : 0)
                            +'&rad_min94_'+i+'='+(document.getElementById('rad_min94_S_'+i)!=null && document.getElementById('rad_min94_S_'+i).checked ? 1 : document.getElementById('rad_min94_N_'+i)!=null && document.getElementById('rad_min94_N_'+i).checked ? 0 : "")
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
                            +'&rad_min98_'+i+'='+(document.getElementById('rad_min98_S_'+i)!=null && document.getElementById('rad_min98_S_'+i).checked ? 1 : document.getElementById('rad_min98_N_'+i)!=null && document.getElementById('rad_min98_N_'+i).checked ? 0 : "")
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
                            +'&rad_tas03_'+i+'='+(document.getElementById('rad_tas03_S_'+i)!=null && document.getElementById('rad_tas03_S_'+i).checked ? 1 : document.getElementById('rad_tas03_N_'+i)!=null && document.getElementById('rad_tas03_N_'+i).checked ? 0 : "")
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
                            +'&txt_act5603_'+i+'='+(document.getElementById('txt_act5603_'+i)!=null ? document.getElementById('txt_act5603_'+i).value:"")
                            +'&chk_lan_2011_'+i+'='+(document.getElementById('chk_lan_2011_'+i)!=null && document.getElementById('chk_lan_2011_'+i).checked ? 1 : 0)
                            +'&chk_lan_2013_'+i+'='+(document.getElementById('chk_lan_2013_'+i)!=null && document.getElementById('chk_lan_2013_'+i).checked ? 1 : 0)
                            +'&chk_lan_2014_'+i+'='+(document.getElementById('chk_lan_2014_'+i)!=null && document.getElementById('chk_lan_2014_'+i).checked ? 1 : 0)
                            +'&chk_lan_2015_'+i+'='+(document.getElementById('chk_lan_2015_'+i)!=null && document.getElementById('chk_lan_2015_'+i).checked ? 1 : 0)
                            +'&chk_lan_2017_'+i+'='+(document.getElementById('chk_lan_2017_'+i)!=null && document.getElementById('chk_lan_2017_'+i).checked ? 1 : 0)
                            +'&txt_otros_'+i+'='+(document.getElementById('txt_otros_'+i)!=null ? document.getElementById('txt_otros_'+i).value : "")
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
                    jsp_alerta("A",'<%=meLanbide47I18n.getMensaje(idiomaUsuario,"msg.datosGuardadosOK")%>');
                }else if(codigoOperacion=="1"){
                    jsp_alerta("A",'<%=meLanbide47I18n.getMensaje(idiomaUsuario,"error.errorBD")%>');
                }else if(codigoOperacion=="2"){
                    jsp_alerta("A",'<%=meLanbide47I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                }else if(codigoOperacion=="3"){
                    jsp_alerta("A",'<%=meLanbide47I18n.getMensaje(idiomaUsuario,"error.pasoParametros")%>');
                }else if(codigoOperacion=="4"){
                    jsp_alerta("A",'<%=meLanbide47I18n.getMensaje(idiomaUsuario,"error.trayectoria.errorGuardarTray")%>');
                }else{
                    jsp_alerta("A",'<%=meLanbide47I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
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
        var ejercicioExpediente = <%=anoExp%>;
        var correcto = true;
        var i = 0;
        
        if(ejercicioExpediente<2018){
            while(i < numAsociaciones){
                elemento = document.getElementById('txt_act5603_'+i);
                if(!validarNumericoOri14(elemento, 8)){
                    correcto = false;
                    elemento.style.border = "1px solid red";
                }else{
                    elemento.removeAttribute("style");
                }

                elemento = document.getElementById('txt_otros_'+i);
                if(!validarNumericoOri14(elemento, 8)){
                    correcto = false;
                    elemento.style.border = "1px solid red";
                }else{
                    elemento.removeAttribute("style");
                }
                i++;
            }
        }
        
        if(!correcto){
            mensajeValidacion = '<%=meLanbide47I18n.getMensaje(idiomaUsuario,"error.trayectoria.datoIncorrecto")%>';
        }else{
            mensajeValidacion = '';
        }
        
        return correcto;
    }
    
    function refrescarPestanaTrayectoria(){
        //Se actualizan los datos de la pestaña
        
                    
        var ajax = getXMLHttpRequest();
        var nodos = null;
        var CONTEXT_PATH = '<%=request.getContextPath()%>'
        var url = CONTEXT_PATH + "/PeticionModuloIntegracion.do";
        var parametros = "";
        var control = new Date();
        parametros = 'tarea=preparar&modulo=MELANBIDE47&operacion=getTrayectoriaORI&tipo=0&numero=<%=numExpediente%>&codEntidad='+document.getElementById('codEntidadOri14').value+'&control='+control.getTime();
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
            var listaTrayectorias = extraerTrayectoriasORI14(nodos);
            var codigoOperacion = listaTrayectorias[0];
            if(codigoOperacion=="0"){
                recargarTrayectoriasORI14(listaTrayectorias);
            }else if(codigoOperacion=="1"){
                jsp_alerta("A",'<%=String.format(meLanbide47I18n.getMensaje(idiomaUsuario,"error.refrescarPestana.errorBD"), meLanbide47I18n.getMensaje(idiomaUsuario, "label.trayectoria.tituloPestana"))%>');
            }else if(codigoOperacion=="2"){
                jsp_alerta("A",'<%=String.format(meLanbide47I18n.getMensaje(idiomaUsuario,"error.refrescarPestana.errorGen"), meLanbide47I18n.getMensaje(idiomaUsuario, "label.trayectoria.tituloPestana"))%>');
            }else if(codigoOperacion=="3"){
                jsp_alerta("A",'<%=String.format(meLanbide47I18n.getMensaje(idiomaUsuario,"error.refrescarPestana.pasoParametros"), meLanbide47I18n.getMensaje(idiomaUsuario, "label.trayectoria.tituloPestana"))%>');
            }else{
                    jsp_alerta("A",'<%=String.format(meLanbide47I18n.getMensaje(idiomaUsuario,"error.refrescarPestana.errorGen"), meLanbide47I18n.getMensaje(idiomaUsuario, "label.trayectoria.tituloPestana"))%>');
            }//if(
        }
        catch(Err){
            jsp_alerta("A",'<%=String.format(meLanbide47I18n.getMensaje(idiomaUsuario,"error.refrescarPestana.errorGen"), meLanbide47I18n.getMensaje(idiomaUsuario, "label.trayectoria.tituloPestana"))%>');
        }//try-catch
    }
    
    function extraerTrayectoriasORI14(nodos){
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
                            else if(hijosTrayectoria[cont2].nodeName=="ACT_56_03"){
                                if(hijosTrayectoria[cont2].childNodes.length > 0){
                                    filaTrayectoria[34] = hijosTrayectoria[cont2].childNodes[0].nodeValue;
                                }
                                else{
                                    filaTrayectoria[34] = '-';
                                }
                            }
                            else if(hijosTrayectoria[cont2].nodeName=="LAN_2011"){
                                if(hijosTrayectoria[cont2].childNodes.length > 0){
                                    filaTrayectoria[35] = hijosTrayectoria[cont2].childNodes[0].nodeValue;
                                }
                                else{
                                    filaTrayectoria[35] = '-';
                                }
                            }
                            else if(hijosTrayectoria[cont2].nodeName=="LAN_2013"){
                                if(hijosTrayectoria[cont2].childNodes.length > 0){
                                    filaTrayectoria[36] = hijosTrayectoria[cont2].childNodes[0].nodeValue;
                                }
                                else{
                                    filaTrayectoria[36] = '-';
                                }
                            }
                            else if(hijosTrayectoria[cont2].nodeName=="LAN_2014"){
                                if(hijosTrayectoria[cont2].childNodes.length > 0){
                                    filaTrayectoria[37] = hijosTrayectoria[cont2].childNodes[0].nodeValue;
                                }
                                else{
                                    filaTrayectoria[37] = '-';
                                }
                            }
                            else if(hijosTrayectoria[cont2].nodeName=="LAN_2015"){
                                if(hijosTrayectoria[cont2].childNodes.length > 0){
                                    filaTrayectoria[38] = hijosTrayectoria[cont2].childNodes[0].nodeValue;
                                }
                                else{
                                    filaTrayectoria[38] = '-';
                                }
                            }
                            else if(hijosTrayectoria[cont2].nodeName=="LAN_OTROS"){
                                if(hijosTrayectoria[cont2].childNodes.length > 0){
                                    filaTrayectoria[39] = hijosTrayectoria[cont2].childNodes[0].nodeValue;
                                }
                                else{
                                    filaTrayectoria[39] = '-';
                                }
                            }
                            else if(hijosTrayectoria[cont2].nodeName=="MIN_94_2017"){
                                if(hijosTrayectoria[cont2].childNodes.length > 0){
                                    filaTrayectoria[40] = hijosTrayectoria[cont2].childNodes[0].nodeValue;
                                }
                                else{
                                    filaTrayectoria[40] = '-';
                                }
                            }
                            else if(hijosTrayectoria[cont2].nodeName=="MIN_94_2018"){
                                if(hijosTrayectoria[cont2].childNodes.length > 0){
                                    filaTrayectoria[41] = hijosTrayectoria[cont2].childNodes[0].nodeValue;
                                }
                                else{
                                    filaTrayectoria[41] = '-';
                                }
                            }
                            else if(hijosTrayectoria[cont2].nodeName=="MIN_98_2017"){
                                if(hijosTrayectoria[cont2].childNodes.length > 0){
                                    filaTrayectoria[42] = hijosTrayectoria[cont2].childNodes[0].nodeValue;
                                }
                                else{
                                    filaTrayectoria[42] = '-';
                                }
                            }
                            else if(hijosTrayectoria[cont2].nodeName=="MIN_98_2018"){
                                if(hijosTrayectoria[cont2].childNodes.length > 0){
                                    filaTrayectoria[43] = hijosTrayectoria[cont2].childNodes[0].nodeValue;
                                }
                                else{
                                    filaTrayectoria[43] = '-';
                                }
                            }
                            else if(hijosTrayectoria[cont2].nodeName=="TAS_03_2017"){
                                if(hijosTrayectoria[cont2].childNodes.length > 0){
                                    filaTrayectoria[44] = hijosTrayectoria[cont2].childNodes[0].nodeValue;
                                }
                                else{
                                    filaTrayectoria[44] = '-';
                                }
                            }
                            else if(hijosTrayectoria[cont2].nodeName=="TAS_03_2018"){
                                if(hijosTrayectoria[cont2].childNodes.length > 0){
                                    filaTrayectoria[45] = hijosTrayectoria[cont2].childNodes[0].nodeValue;
                                }
                                else{
                                    filaTrayectoria[45] = '-';
                                }
                            }
                            else if(hijosTrayectoria[cont2].nodeName=="LAN_2017"){
                                if(hijosTrayectoria[cont2].childNodes.length > 0){
                                    filaTrayectoria[46] = hijosTrayectoria[cont2].childNodes[0].nodeValue;
                                }
                                else{
                                    filaTrayectoria[46] = '-';
                                }
                            }
                            else if(hijosTrayectoria[cont2].nodeName=="COLEC1_DEC_327"){
                                if(hijosTrayectoria[cont2].childNodes.length > 0){
                                    filaTrayectoria[47] = hijosTrayectoria[cont2].childNodes[0].nodeValue;
                                }
                                else{
                                    filaTrayectoria[47] = '-';
                                }
                            }
                            else if(hijosTrayectoria[cont2].nodeName=="COLEC1_MIN_94"){
                                if(hijosTrayectoria[cont2].childNodes.length > 0){
                                    filaTrayectoria[48] = hijosTrayectoria[cont2].childNodes[0].nodeValue;
                                }
                                else{
                                    filaTrayectoria[48] = '-';
                                }
                            }
                            else if(hijosTrayectoria[cont2].nodeName=="COLEC1_MIN_98"){
                                if(hijosTrayectoria[cont2].childNodes.length > 0){
                                    filaTrayectoria[49] = hijosTrayectoria[cont2].childNodes[0].nodeValue;
                                }
                                else{
                                    filaTrayectoria[49] = '-';
                                }
                            }
                            else if(hijosTrayectoria[cont2].nodeName=="COLEC1_TAS_03"){
                                if(hijosTrayectoria[cont2].childNodes.length > 0){
                                    filaTrayectoria[50] = hijosTrayectoria[cont2].childNodes[0].nodeValue;
                                }
                                else{
                                    filaTrayectoria[50] = '-';
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
    
    function recargarTrayectoriasORI14(listaTrayectorias){
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
        if(ano==2017)
            colspanN=7;
        else if(ano>=2018)
            colspanN=8;
        var html = '<table style="height: 434px;" onmousewheel="propagarEvento(event, \'trayectoriaGen\');">'
                  +'    <tbody>'
                  +'          <tr>'
                  +'              <td class="tablefrozencolumn" style="width: 300px; vertical-align: top;">'
                  +'                  <div id="divfrozen_trayectoriaGen" style="overflow-x: hidden; overflow-y: hidden;" style="width: 300px; height: 406px; display: inline; float: left; padding-right: 25px;">'
                  +'                      <table style="font-size: 12px; border-collapse: collapse; width: 300px;">'
                  +'                          <tr>'
                  +'                              <td style="height: 13px;">'
                  +'                              </td>'
                  +'                          </tr>'
                  +'                          <tr>'
                  +'                              <td style="background-color: LightGrey; height: 14px;">'
                  +'                                  <%=meLanbide47I18n.getMensaje(idiomaUsuario,"label.trayectoria.programasSubvencionables")%>'
                  +'                              </td>'
                  +'                          </tr>'
                  +'                          <tr>'
                  +'                              <td style="vertical-align: top; padding-left: 30px; height: 30px;">'
                  +'                                  <%=meLanbide47I18n.getMensaje(idiomaUsuario,"label.trayectoria.programasSubvencionables.prog1")%>'
                  +'                              </td>'
                  +'                          </tr>'
                  +'                          <tr>'
                  +'                              <td style="vertical-align: top; padding-left: 30px; height: 30px;">'
                  +'                                  <%=meLanbide47I18n.getMensaje(idiomaUsuario,"label.trayectoria.programasSubvencionables.prog2")%>'
                  +'                              </td>'
                  +'                          </tr>'
                  +'                          <tr>'
                  +'                              <td style="vertical-align: top; padding-left: 30px; height: 30px;">'
                  +'                                  <%=meLanbide47I18n.getMensaje(idiomaUsuario,"label.trayectoria.programasSubvencionables.prog3")%>'
                  +'                              </td>'
                  +'                          </tr>'
                  +'                          <tr>'
                  +'                              <td style="vertical-align: top; padding-left: 30px; height: 30px;">'
                  +'                                  <%=meLanbide47I18n.getMensaje(idiomaUsuario,"label.trayectoria.programasSubvencionables.prog4")%>'
                  +'                              </td>'
                  +'                          </tr>'
                  +'                          <tr>'
                  +'                              <td style="background-color: LightGrey; height: 14px;">'
                  +'                                  <%=meLanbide47I18n.getMensaje(idiomaUsuario,"label.trayectoria.actividades")%>'
                  +'                              </td>'
                  +'                          </tr>'
                  +'                          <tr>'
                  +'                              <td style="vertical-align: top; padding-left: 30px; height: 60px;">'
                  +'                                  <%=meLanbide47I18n.getMensaje(idiomaUsuario,"label.trayectoria.actividades.act1")%>'
                  +'                              </td>'
                  +'                          </tr>'
                  +'                          <tr>'
                  +'                              <td style="background-color: LightGrey; height: 14px;">'
                  +'                                  <%=meLanbide47I18n.getMensaje(idiomaUsuario,"label.trayectoria.convocatoria")%>'
                  +'                              </td>'
                  +'                          </tr>'
                  +'                          <tr>'
                  +'                              <td style="vertical-align: top; padding-left: 30px; height: 100px;">'
                  +'                                  <%=meLanbide47I18n.getMensaje(idiomaUsuario,"label.trayectoria.convocatoria.conv1")%>'
                  +'                              </td>'
                  +'                          </tr>'
                  +'                          <tr>'
                  +'                              <td style="vertical-align: top; padding-left: 30px; height: 100px;">'
                  +'                                  <%=meLanbide47I18n.getMensaje(idiomaUsuario,"label.trayectoria.convocatoria.conv2")%>'
                  +'                              </td>'
                  +'                          </tr>';
                  if(ano >= 2015){
                    html+='                          <tr>'
                    +'                              <td style="vertical-align: top; padding-left: 30px; height: 100px;">'
                    +'                                  <%=meLanbide47I18n.getMensaje(idiomaUsuario,"label.trayectoria.convocatoria.conv3")%>'
                    +'                              </td>'
                    +'                          </tr>'
                    +'                          <tr>'
                    +'                              <td style="vertical-align: top; padding-left: 30px; height: 100px;">'
                    +'                                  <%=meLanbide47I18n.getMensaje(idiomaUsuario,"label.trayectoria.convocatoria.conv4")%>'
                    +'                              </td>'
                    +'                          </tr>'
                                                        ;
                    }
                  html+='                          <tr>'
                  +'                              <td style="background-color: LightGrey; height: 14px;">'
                  +'                                  <%=meLanbide47I18n.getMensaje(idiomaUsuario,"label.trayectoria.otrosProgramas")%>'
                  +'                              </td>'
                  +'                          </tr>'
                  +'                          <tr>'
                  +'                              <td style="vertical-align: top; padding-left: 30px; height: 40px;">'
                  +'                                  <%=meLanbide47I18n.getMensaje(idiomaUsuario,"label.trayectoria.otrosProgramas.otrosProg1")%>'
                  +'                              </td>'
                  +'                          </tr>'
                  +'                      </table>'
                  +'                  </div>'
                  +'              </td>'
                  +'              <td class="tablecontent" style="vertical-align: top; width: 600px;">'
                  +'                  <div id="contentscroll_trayectoriaGen" style="width: 560px; height: 406px;" class="content">'
                  +'                      <table width="'+(numAsociaciones * 180)+'" class="content" style="border-collapse: collapse; font-size: 12px;">'
                  +'                          <tr>';
              
              
              
                  for(var i = 0; i < numAsociaciones; i++){
                      if(i == numAsociaciones - 1){
                          estiloD = '';
                      }else{
                          estiloD = ' border-right: 1px solid black;';
                      }
                      
                      asociacion = asociaciones[i];
                      
                      if(asociacion[0] != null && asociacion[0] != '-'){
                          codigoAsociacion = asociacion[0];
                      }else{
                          codigoAsociacion = '';
                      }
                      nombreAsociacion = asociacion[1];
                  
                       html += '                  <td style="text-align: center; background-color: #4B95D3; height: 13px;'+estiloD+'" colspan="4" title="'+nombreAsociacion+'">'
                       +'                             <div style="overflow: hidden; text-overflow: ellipsis; white-space: nowrap; width: 100%;">'
                       +'                                 '+nombreAsociacion
                       +'                                 <input type="hidden" id="codigoAsoc_'+i+'" value="'+codigoAsociacion+'"'
                       +'                             </div>'
                       +'                         </td>'
                   }
                   
                   html += '                       </tr>'
                          +'                       <tr>';
                      
                  for(var i = 0; i < numAsociaciones; i++){
                        if(i == numAsociaciones - 1)
                        {
                            estiloD = '';
                        }
                        else
                        {
                            estiloD = ' border-right: 1px solid black;';
                        }
                        if(ano <= 2015){
                        html += '                       <td style="text-align: center;background-color: LightGrey; height: 14px;" width="25">'
                               +'                           2007'
                               +'                       </td>'
                               +'                       <td style="text-align: center;background-color: LightGrey; height: 14px;" width="25">'
                               +'                           2008'
                               +'                       </td>'
                               +'                       <td style="text-align: center;background-color: LightGrey; height: 14px;" width="25">'
                               +'                           2009'
                               +'                       </td>';
                        }
                        if(ano < 2018){
                        html +='                       <td style="text-align: center;background-color: LightGrey; height: 14px;'+estiloD+'" width="25">'
                               +'                           2010'
                               +'                       </td>';
                   }
                  }
                  
                  html +='                         </tr>'
                        +'                         <tr>';
                    
                  
                for(var i = 0; i < numAsociaciones; i++)
                {
                    if(i == numAsociaciones - 1)
                    {
                        //Es el ultimo
                        if(i > 0)
                        {
                            estiloI = 'border-left: 1px solid black;';
                        }
                        else
                        {
                            estiloI = '';
                        }
                        estiloD = '';
                    }
                    else if(i == 0)
                    {
                        //Es el primero
                        estiloI = '';
                        estiloD = 'border-right: 1px solid black;';
                    }
                    else
                    {
                        //Es intermedio
                        estiloI = 'border-left: 1px solid black;';
                        estiloD = 'border-right: 1px solid black;';
                    }
                    
                    asociacion = asociaciones[i];
                    trayectoria = asociacion[2];
                    if(ano <= 2015){
                    html += '                     <td style="text-align: center; height: 30px; vertical-align: top;'+estiloI+'">'
                           +'                         <input type="checkbox" id="chk_dec327_2007_'+i+'" '+(trayectoria[0] == '1' ? 'checked' : '')+' size="2"/>'
                           +'                     </td>'
                           +'                     <td style="text-align: center; height: 30px; vertical-align: top;">'
                           +'                         <input type="checkbox" id="chk_dec327_2008_'+i+'" '+(trayectoria[1] == '1' ? 'checked' : '')+' size="2"/>'
                           +'                     </td>'
                           +'                     <td style="text-align: center; height: 30px; vertical-align: top;">'
                           +'                         <input type="checkbox" id="chk_dec327_2009_'+i+'" '+(trayectoria[2] == '1' ? 'checked' : '')+' size="2"/>'
                           +'                     </td>';
                    }
                    html +='                     <td style="text-align: center; height: 30px; vertical-align: top;'+estiloD+'">'
                           +'                         <input type="checkbox" id="chk_dec327_2010_'+i+'" '+(trayectoria[3] == '1' ? 'checked' : '')+' size="2"/>'
                           +'                     </td>';
                }
                
                html += '                   </tr>'
                       +'                   <tr>';
                   
                
                for(var i = 0; i < numAsociaciones; i++)
                {
                    if(i == numAsociaciones - 1)
                    {
                        //Es el ultimo
                        if(i > 0)
                        {
                            estiloI = 'border-left: 1px solid black;';
                        }
                        else
                        {
                            estiloI = '';
                        }
                        estiloD = '';
                    }
                    else if(i == 0)
                    {
                        //Es el primero
                        estiloI = '';
                        estiloD = 'border-right: 1px solid black;';
                    }
                    else
                    {
                        //Es intermedio
                        estiloI = 'border-left: 1px solid black;';
                        estiloD = 'border-right: 1px solid black;';
                    }
                    
                    asociacion = asociaciones[i];
                    trayectoria = asociacion[2];
                    if(ano <= 2015){
                    html += '                     <td style="text-align: center; height: 30px; vertical-align: top;'+estiloI+'">'
                           +'                         <input type="checkbox" id="chk_min94_2007_'+i+'" '+(trayectoria[4] == '1' ? 'checked' : '')+' size="2"/>'
                           +'                     </td>'
                           +'                     <td style="text-align: center; height: 30px; vertical-align: top;">'
                           +'                         <input type="checkbox" id="chk_min94_2008_'+i+'" '+(trayectoria[5] == '1' ? 'checked' : '')+' size="2"/>'
                           +'                     </td>'
                           +'                     <td style="text-align: center; height: 30px; vertical-align: top;">'
                           +'                         <input type="checkbox" id="chk_min94_2009_'+i+'" '+(trayectoria[6] == '1' ? 'checked' : '')+' size="2"/>'
                           +'                     </td>';
                    }
                    html +='                     <td style="text-align: center; height: 30px; vertical-align: top;'+estiloD+'">'
                           +'                         <input type="checkbox" id="chk_min94_2010_'+i+'" '+(trayectoria[7] == '1' ? 'checked' : '')+' size="2"/>'
                           +'                     </td>';
                   if(ano >= 2017){
                       html +='                     <td style="text-align: center; height: 30px; vertical-align: top;'+estiloD+'">'
                           +'                         <input type="checkbox" id="chk_min94_2011_'+i+'" '+(trayectoria[8] == '1' ? 'checked' : '')+' size="2"/>'
                           +'                     </td>'
                           +'                     <td style="text-align: center; height: 30px; vertical-align: top;'+estiloD+'">'
                           +'                         <input type="checkbox" id="chk_min94_2012_'+i+'" '+(trayectoria[9] == '1' ? 'checked' : '')+' size="2"/>'
                           +'                     </td>'
                           +'                     <td style="text-align: center; height: 30px; vertical-align: top;'+estiloD+'">'
                           +'                         <input type="checkbox" id="chk_min94_2013_'+i+'" '+(trayectoria[10] == '1' ? 'checked' : '')+' size="2"/>'
                           +'                     </td>'
                           +'                     <td style="text-align: center; height: 30px; vertical-align: top;'+estiloD+'">'
                           +'                         <input type="checkbox" id="chk_min94_2014_'+i+'" '+(trayectoria[11] == '1' ? 'checked' : '')+' size="2"/>'
                           +'                     </td>'
                           +'                     <td style="text-align: center; height: 30px; vertical-align: top;'+estiloD+'">'
                           +'                         <input type="checkbox" id="chk_min94_2015_'+i+'" '+(trayectoria[12] == '1' ? 'checked' : '')+' size="2"/>'
                           +'                     </td>'
                           +'                     <td style="text-align: center; height: 30px; vertical-align: top;'+estiloD+'">'
                           +'                         <input type="checkbox" id="chk_min94_2016_'+i+'" '+(trayectoria[13] == '1' ? 'checked' : '')+' size="2"/>'
                           +'                     </td>'
                            ;
                        }
                }
                
                html += '                    </tr>'
                       +'                    <tr>';
                   
                
                for(var i = 0; i < numAsociaciones; i++)
                {
                    if(i == numAsociaciones - 1)
                    {
                        //Es el ultimo
                        if(i > 0)
                        {
                            estiloI = 'border-left: 1px solid black;';
                        }
                        else
                        {
                            estiloI = '';
                        }
                        estiloD = '';
                    }
                    else if(i == 0)
                    {
                        //Es el primero
                        estiloI = '';
                        estiloD = 'border-right: 1px solid black;';
                    }
                    else
                    {
                        //Es intermedio
                        estiloI = 'border-left: 1px solid black;';
                        estiloD = 'border-right: 1px solid black;';
                    }
                    
                    asociacion = asociaciones[i];
                    trayectoria = asociacion[2];
                    if(ano <= 2015){
                    html += '                     <td style="text-align: center; height: 30px; vertical-align: top;'+estiloI+'">'
                           +'                         <input type="checkbox" id="chk_min98_2007_'+i+'" '+(trayectoria[14] == '1' ? 'checked' : '')+' size="2"/>'
                           +'                     </td>'
                           +'                     <td style="text-align: center; height: 30px; vertical-align: top;">'
                           +'                         <input type="checkbox" id="chk_min98_2008_'+i+'" '+(trayectoria[15] == '1' ? 'checked' : '')+' size="2"/>'
                           +'                     </td>'
                           +'                     <td style="text-align: center; height: 30px; vertical-align: top;">'
                           +'                         <input type="checkbox" id="chk_min98_2009_'+i+'" '+(trayectoria[16] == '1' ? 'checked' : '')+' size="2"/>'
                           +'                     </td>';
                    }
                    html +='                     <td style="text-align: center; height: 30px; vertical-align: top;'+estiloD+'">'
                           +'                         <input type="checkbox" id="chk_min98_2010_'+i+'" '+(trayectoria[17] == '1' ? 'checked' : '')+' size="2"/>'
                           +'                     </td>';
                   if(ano >= 2017){
                    html +='                     <td style="text-align: center; height: 30px; vertical-align: top;'+estiloD+'">'
                           +'                         <input type="checkbox" id="chk_min98_2011_'+i+'" '+(trayectoria[18] == '1' ? 'checked' : '')+' size="2"/>'
                           +'                     </td>'
                           +'                     <td style="text-align: center; height: 30px; vertical-align: top;'+estiloD+'">'
                           +'                         <input type="checkbox" id="chk_min98_2012_'+i+'" '+(trayectoria[19] == '1' ? 'checked' : '')+' size="2"/>'
                           +'                     </td>'
                           +'                     <td style="text-align: center; height: 30px; vertical-align: top;'+estiloD+'">'
                           +'                         <input type="checkbox" id="chk_min98_2013_'+i+'" '+(trayectoria[20] == '1' ? 'checked' : '')+' size="2"/>'
                           +'                     </td>'
                           +'                     <td style="text-align: center; height: 30px; vertical-align: top;'+estiloD+'">'
                           +'                         <input type="checkbox" id="chk_min98_2014_'+i+'" '+(trayectoria[21] == '1' ? 'checked' : '')+' size="2"/>'
                           +'                     </td>'
                           +'                     <td style="text-align: center; height: 30px; vertical-align: top;'+estiloD+'">'
                           +'                         <input type="checkbox" id="chk_min98_2015_'+i+'" '+(trayectoria[22] == '1' ? 'checked' : '')+' size="2"/>'
                           +'                     </td>'
                           +'                     <td style="text-align: center; height: 30px; vertical-align: top;'+estiloD+'">'
                           +'                         <input type="checkbox" id="chk_min98_2016_'+i+'" '+(trayectoria[23] == '1' ? 'checked' : '')+' size="2"/>'
                           +'                     </td>'
                           ;
                   }
                }
                
                html += '                    </tr>'
                       +'                    <tr>';
                   
                   
                for(var i = 0; i < numAsociaciones; i++)
                {
                    if(i == numAsociaciones - 1)
                    {
                        //Es el ultimo
                        if(i > 0)
                        {
                            estiloI = 'border-left: 1px solid black;';
                        }
                        else
                        {
                            estiloI = '';
                        }
                        estiloD = '';
                    }
                    else if(i == 0)
                    {
                        //Es el primero
                        estiloI = '';
                        estiloD = 'border-right: 1px solid black;';
                    }
                    else
                    {
                        //Es intermedio
                        estiloI = 'border-left: 1px solid black;';
                        estiloD = 'border-right: 1px solid black;';
                    }
                    
                    asociacion = asociaciones[i];
                    trayectoria = asociacion[2];
                    if(ano <= 2015){
                    html += '                     <td style="text-align: center; height: 30px; vertical-align: top;'+estiloI+'">'
                           +'                         <input type="checkbox" id="chk_tas03_2007_'+i+'" '+(trayectoria[24] == '1' ? 'checked' : '')+' size="2"/>'
                           +'                     </td>'
                           +'                     <td style="text-align: center; height: 30px; vertical-align: top;">'
                           +'                         <input type="checkbox" id="chk_tas03_2008_'+i+'" '+(trayectoria[25] == '1' ? 'checked' : '')+' size="2"/>'
                           +'                     </td>'
                           +'                     <td style="text-align: center; height: 30px; vertical-align: top;">'
                           +'                         <input type="checkbox" id="chk_tas03_2009_'+i+'" '+(trayectoria[26] == '1' ? 'checked' : '')+' size="2"/>'
                           +'                     </td>';
                    }
                   html +='                     <td style="text-align: center; height: 30px; vertical-align: top;'+estiloD+'">'
                           +'                         <input type="checkbox" id="chk_tas03_2010_'+i+'" '+(trayectoria[27] == '1' ? 'checked' : '')+' size="2"/>'
                           +'                     </td>';
                   if(ano >= 2017){
                    html +='                     <td style="text-align: center; height: 30px; vertical-align: top;'+estiloD+'">'
                           +'                         <input type="checkbox" id="chk_tas03_2011_'+i+'" '+(trayectoria[28] == '1' ? 'checked' : '')+' size="2"/>'
                           +'                     </td>'
                           +'                     <td style="text-align: center; height: 30px; vertical-align: top;'+estiloD+'">'
                           +'                         <input type="checkbox" id="chk_tas03_2012_'+i+'" '+(trayectoria[29] == '1' ? 'checked' : '')+' size="2"/>'
                           +'                     </td>'
                           +'                     <td style="text-align: center; height: 30px; vertical-align: top;'+estiloD+'">'
                           +'                         <input type="checkbox" id="chk_tas03_2013_'+i+'" '+(trayectoria[30] == '1' ? 'checked' : '')+' size="2"/>'
                           +'                     </td>'
                           +'                     <td style="text-align: center; height: 30px; vertical-align: top;'+estiloD+'">'
                           +'                         <input type="checkbox" id="chk_tas03_2014_'+i+'" '+(trayectoria[31] == '1' ? 'checked' : '')+' size="2"/>'
                           +'                     </td>'
                           +'                     <td style="text-align: center; height: 30px; vertical-align: top;'+estiloD+'">'
                           +'                         <input type="checkbox" id="chk_tas03_2015_'+i+'" '+(trayectoria[32] == '1' ? 'checked' : '')+' size="2"/>'
                           +'                     </td>'
                           +'                     <td style="text-align: center; height: 30px; vertical-align: top;'+estiloD+'">'
                           +'                         <input type="checkbox" id="chk_tas03_2016_'+i+'" '+(trayectoria[33] == '1' ? 'checked' : '')+' size="2"/>'
                           +'                     </td>'
                            ;
                   }
                }
                
                html += '                    </tr>'
                       +'                    <tr>';
                
                for(var i = 0; i < numAsociaciones; i++)
                {

                    if(i == numAsociaciones - 1)
                    {
                        estiloD = '';
                    }
                    else
                    {
                        estiloD = ' border-right: 1px solid black;';
                    }
                    
                    html += '                     <td style="text-align: center; background-color: LightGrey; height: 14px;'+estiloD+'" colspan="4">'
                           +'                                 <%=meLanbide47I18n.getMensaje(idiomaUsuario,"label.trayectoria.duracionMeses")%>'
                           +'                     </td>';
                }
                
                html += '                    </tr>'
                       +'                    <tr>';
                for(var i = 0; i < numAsociaciones; i++)
                {
                    if(i == numAsociaciones - 1)
                    {
                        //Es el ultimo
                        if(i > 0)
                        {
                            estiloI = 'border-left: 1px solid black;';
                        }
                        else
                        {
                            estiloI = '';
                        }
                        estiloD = '';
                    }
                    else if(i == 0)
                    {
                        //Es el primero
                        estiloI = '';
                        estiloD = 'border-right: 1px solid black;';
                    }
                    else
                    {
                        //Es intermedio
                        estiloI = 'border-left: 1px solid black;';
                        estiloD = 'border-right: 1px solid black;';
                    }
                    
                    asociacion = asociaciones[i];
                    trayectoria = asociacion[2];
                    
                    html += '                      <td style="text-align: center; height: 60px; vertical-align: top;'+estiloI+'" colspan="4">'
                          +'                          <input type="text" id="txt_act5603_'+i+'" value="'+(trayectoria[34] != '-' ? trayectoria[34] : '')+'" size="6" maxlength="8" class="inputTexto" title="<%=String.format(meLanbide47I18n.getMensaje(idiomaUsuario,"label.trayectoria.tipoDatoNumerico"), 8)%>"/>'
                          +'                      </td>';
                }
                
                html += '                    </tr>'
                       +'                    <tr>';
                for(var i = 0; i < numAsociaciones; i++)
                {

                    if(i == numAsociaciones - 1)
                    {
                        estiloD = '';
                    }
                    else
                    {
                        estiloD = ' border-right: 1px solid black;';
                    }
                    
                    html += '                     <td style="text-align: center;background-color: LightGrey; height: 14px;'+estiloD+'" colspan="4">'
                           +'                     </td>';
                }
                
                html += '                    </tr>'
                       +'                    <tr>';
                   
                   
                for(var i = 0; i < numAsociaciones; i++)
                {
                    if(i == numAsociaciones - 1)
                    {
                        //Es el ultimo
                        if(i > 0)
                        {
                            estiloI = 'border-left: 1px solid black;';
                        }
                        else
                        {
                            estiloI = '';
                        }
                        estiloD = '';
                    }
                    else if(i == 0)
                    {
                        //Es el primero
                        estiloI = '';
                        estiloD = 'border-right: 1px solid black;';
                    }
                    else
                    {
                        //Es intermedio
                        estiloI = 'border-left: 1px solid black;';
                        estiloD = 'border-right: 1px solid black;';
                    }
                    
                    asociacion = asociaciones[i];
                    trayectoria = asociacion[2];
                    
                    html += '                     <td style="text-align: center; height: 100px; vertical-align: top;'+estiloD+'" colspan="4">'
                           +'                         <input type="checkbox" id="chk_lan_2011_'+i+'" '+(trayectoria[35] == '1' ? 'checked' : '')+' size="2"/>'
                           +'                     </td>';
                }
                if(ano >= 2015 ){
                    html += '                    </tr>'
                           +'                    <tr>';


                    for(var i = 0; i < numAsociaciones; i++)
                    {
                        if(i == numAsociaciones - 1)
                        {
                            //Es el ultimo
                            if(i > 0)
                            {
                                estiloI = 'border-left: 1px solid black;';
                            }
                            else
                            {
                                estiloI = '';
                            }
                            estiloD = '';
                        }
                        else if(i == 0)
                        {
                            //Es el primero
                            estiloI = '';
                            estiloD = 'border-right: 1px solid black;';
                        }
                        else
                        {
                            //Es intermedio
                            estiloI = 'border-left: 1px solid black;';
                            estiloD = 'border-right: 1px solid black;';
                        }

                        asociacion = asociaciones[i];
                        trayectoria = asociacion[2];

                        html += '                     <td style="text-align: center; height: 100px; vertical-align: top;'+estiloD+'" colspan="4">'
                               +'                         <input type="checkbox" id="chk_lan_2013_'+i+'" '+(trayectoria[36] == '1' ? 'checked' : '')+' size="2"/>'
                               +'                     </td>';
                    }
                }
                html += '                    </tr>'
                       +'                    <tr>';
                
                
                for(var i = 0; i < numAsociaciones; i++)
                {
                    if(i == numAsociaciones - 1)
                    {
                        //Es el ultimo
                        if(i > 0)
                        {
                            estiloI = 'border-left: 1px solid black;';
                        }
                        else
                        {
                            estiloI = '';
                        }
                        estiloD = '';
                    }
                    else if(i == 0)
                    {
                        //Es el primero
                        estiloI = '';
                        estiloD = 'border-right: 1px solid black;';
                    }
                    else
                    {
                        //Es intermedio
                        estiloI = 'border-left: 1px solid black;';
                        estiloD = 'border-right: 1px solid black;';
                    }
                    
                    asociacion = asociaciones[i];
                    trayectoria = asociacion[2];
                    
                   html += '                     <td style="text-align: center; height: 100px; vertical-align: top;'+estiloD+'" colspan="4">'
                           +'                         <input type="checkbox" id="chk_lan_2014_'+i+'" '+(trayectoria[37] == '1' ? 'checked' : '')+' size="2"/>'
                           +'                     </td>';
                }
                
                html += '                    </tr>'
                       +'                    <tr>';
               
                for(var i = 0; i < numAsociaciones; i++)
                {
                    if(i == numAsociaciones - 1)
                    {
                        //Es el ultimo
                        if(i > 0)
                        {
                            estiloI = 'border-left: 1px solid black;';
                        }
                        else
                        {
                            estiloI = '';
                        }
                        estiloD = '';
                    }
                    else if(i == 0)
                    {
                        //Es el primero
                        estiloI = '';
                        estiloD = 'border-right: 1px solid black;';
                    }
                    else
                    {
                        //Es intermedio
                        estiloI = 'border-left: 1px solid black;';
                        estiloD = 'border-right: 1px solid black;';
                    }
                    
                    asociacion = asociaciones[i];
                    trayectoria = asociacion[2];
                    
                   html += '                     <td style="text-align: center; height: 100px; vertical-align: top;'+estiloD+'" colspan="4">'
                           +'                         <input type="checkbox" id="chk_lan_2015_'+i+'" '+(trayectoria[38] == '1' ? 'checked' : '')+' size="2"/>'
                           +'                     </td>';
                }
                
                html += '                    </tr>'
                       +'                    <tr>';
                   
                   
                for(var i = 0; i < numAsociaciones; i++)
                {

                    if(i == numAsociaciones - 1)
                    {
                        estiloD = '';
                    }
                    else
                    {
                        estiloD = ' border-right: 1px solid black;';
                    }
                    
                    html += '                     <td style="text-align: center; background-color: LightGrey; height: 14px;'+estiloD+'" colspan="4">'
                           +'                         <%=meLanbide47I18n.getMensaje(idiomaUsuario,"label.trayectoria.duracionMeses")%>'
                           +'                     </td>';
                }
                
                html += '                    </tr>'
                       +'                    <tr>';
                for(var i = 0; i < numAsociaciones; i++)
                {
                    if(i == numAsociaciones - 1)
                    {
                        //Es el ultimo
                        if(i > 0)
                        {
                            estiloI = 'border-left: 1px solid black;';
                        }
                        else
                        {
                            estiloI = '';
                        }
                        estiloD = '';
                    }
                    else if(i == 0)
                    {
                        //Es el primero
                        estiloI = '';
                        estiloD = 'border-right: 1px solid black;';
                    }
                    else
                    {
                        //Es intermedio
                        estiloI = 'border-left: 1px solid black;';
                        estiloD = 'border-right: 1px solid black;';
                    }
                    
                    asociacion = asociaciones[i];
                    trayectoria = asociacion[2];
                    
                    html += '                     <td style="text-align: center; height: 40px; vertical-align: top;'+estiloD+'" colspan="4">'
                           +'                         <input type="text" id="txt_otros_'+i+'" value="'+(trayectoria[39] != '-' ? trayectoria[39] : '')+'" size="6" maxlength="8" class="inputTexto" title="<%=String.format(meLanbide47I18n.getMensaje(idiomaUsuario,"label.trayectoria.tipoDatoNumerico"), 8)%>"/>'
                           +'                     </td>';
                }
                
                html += '                    </tr>'
                       +'                    <tr>' 
                       +'                 </table>'
                       +'             </div>'
                       +'         </td>'
                       +'         <td style="width: 30px;">'
                       +'             <div id="vScroll_trayectoriaGen" class="vertical-scroll" style="margin-left: -6px; display: none; width: 20px; height: 406px; display: inline-block; position: relative;" onscroll="scrollVertical(this, \'trayectoriaGen\');">'
                       +'                 <div style="height: 500px;"/>'
                       +'             </div>'
                       +'         </td>'
                       +'     </tr>'
                       +'     <tr class="horizontal-scroll">'
                       +'         <td colspan="2" style="width: '+(numAsociaciones * 180)+'px;">'
                       +'             <div class="horizontal-scroll" style="width: 867px; display:inline-block; position: relative; margin-top: -26px;" onscroll="scrollHorizontal(this, \'trayectoriaGen\');">'
                       +'                 <div style="width: '+((numAsociaciones * 180) + 300)+'px;"/>'
                       +'             </div>'
                       +'         </td>'
                       +'     </tr>'
                       +' </tbody>'
                       +'</table>';
       var anchoTablaJS = <%=anchoTabla%>;            
       var html2 =//'<table style="height: 434px;" onmousewheel="propagarEvento(event, \'trayectoriaGen\');">'
//+'                        <tbody>'
//+'                            <tr>'
//+'                                <td style="width: 98%;">'
'                                    <table style="font-size: 12px; width:'+((numAsociaciones * anchoTablaJS) + 400)+'px;  table-layout: fixed">'
+'                                        <tr>'
+'                                             <td style="height: 13px; width: 400px; table-layout: fixed">'
+'                                             </td>';
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
                                                   //tray = trayectorias.get(asociacion.getCodAsociacion());
                                                   //trayectoria = asociacion[2];
                                                   
                                                    //nombreAsociacion = asociacion.getCif() != null ? asociacion.getCif() : "";
                                                    //nombreAsociacion += !nombreAsociacion.equals("") && asociacion.getNombre() != null && !asociacion.getNombre().equals("") ? " - " : "";
                                                    //nombreAsociacion += asociacion.getNombre() != null ? asociacion.getNombre() : "";
html2 +='                                           <td style="text-align: center; background-color: #4B95D3; height: 13px;'+estiloD+'" colspan="'+colspanN+'" title="'+nombreAsociacion+'">'
+'                                                        <div style="overflow: hidden; text-overflow: ellipsis; white-space: nowrap; width: 100%;">'
+                                                                   nombreAsociacion
+'                                                            <input type="hidden" id="codigoAsoc_'+i+'" value="'+codigoAsociacion+'"'
+'                                                        </div>'
+'                                                    </td>';
                                                }

html2 +='                                        </tr>'
+'                                        <tr>'
+'                                            <td style="background-color: LightGrey; height: 14px;'+estiloD+'">'
+'                                                <%=meLanbide47I18n.getMensaje(idiomaUsuario,"label.trayectoria.programasSubvencionables")%>'
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
                                             if(ano >= 2018) {
html2 +='                                         <td style="text-align: center;background-color: LightGrey; height: 14px;"  width="50">'
+'                                                E.Previa &nbsp Si|No'
+'                                                </td>';
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
                                                if(ano < 2018){
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
                                              if(ano >= 2018){
html2 +='                                     <td style="text-align: center;background-color: LightGrey; height: 14px;" width="25">'
+'                                                2017'
+'                                            </td>'
+'                                            <td style="text-align: center;background-color: LightGrey; height: 14px;'+estiloD+'" width="25">'
+'                                                2018'
+'                                            </td>';
                                              }
                                            }
                                         }
html2 +='                                 </tr>'
+'                                        <tr>'
+'                                            <td style="vertical-align: top; padding-left: 30px; height: 30px;'+estiloD+'">'
+'                                                        <%=meLanbide47I18n.getMensaje(idiomaUsuario,"label.trayectoria.programasSubvencionables.prog1")%>'
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
                                                    
                                                if(ano >= 2018) {
html2 +='                                         <td style="text-align: center; height: 30px; vertical-align: top;'+estiloI+'">'
+'                                                        <input type="radio" name="rad_dec327_'+i+'" id="rad_dec327_S_'+i+'" title="Si" '+(trayectoria[47] != null && trayectoria[47] == '1' ? 'checked' : '')+ ' size="2"/>'
+'                                                        |<input type="radio" name="rad_dec327_'+i+'" id="rad_dec327_N_'+i+'" title="No" '+(trayectoria[47] != null && trayectoria[47]  == '0' ? 'checked' : '')+ ' size="2"/>'
+'                                                </td>';
                                                }
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
                                                   if(ano<2018){
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
+'                                                    </td>';
                                                      if(ano>=2018){ // agregamos 1 para despues de 2018 (son 8 2010,12,13,14,15,16,17,18)
html2 +='                                             <td style="text-align: center; height: 30px; vertical-align: top;">'
+'                                                    </td>';
                                                      }
html2 +='                                             <td style="text-align: center; height: 30px; vertical-align: top;">'
+'                                                    </td>'
+'                                                    <td style="text-align: center; height: 30px; vertical-align: top;'+estiloD+'">'
+'                                                    </td>';                                                       
                                                   }
                                                }
html2 +='                                 </tr>'
+'                                        <tr>'
+'                                            <td style="vertical-align: top; padding-left: 30px; height: 30px;'+estiloD+'">'
+'                                                        <%=meLanbide47I18n.getMensaje(idiomaUsuario,"label.trayectoria.programasSubvencionables.prog2")%>'
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
                                                if(ano >= 2018) {
html2 +='                                         <td style="text-align: center; height: 30px; vertical-align: top;'+estiloI+'">'
+'                                                        <input type="radio" name="rad_min94_'+i+'" id="rad_min94_S_'+i+'" title="Si" '+(trayectoria[48] != null && trayectoria[48] == '1' ? 'checked' : '')+ ' size="2"/>'
+'                                                        |<input type="radio" name="rad_min94_'+i+'" id="rad_min94_N_'+i+'" title="No" '+(trayectoria[48] != null && trayectoria[48]  == '0' ? 'checked' : '')+ ' size="2"/>'
+'                                                </td>';
                                                }
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
                                                      if(ano >= 2018){
html2 +='                                             <td style="text-align: center; height: 30px; vertical-align: top;">'
+'                                                        <input type="checkbox" id="chk_min94_2017_'+i+'" '+ (trayectoria[40] == '1' ? 'checked' : '')+' size="2"/>'
+'                                                    </td>'
+'                                                    <td style="text-align: center; height: 30px; vertical-align: top;'+estiloD+'">'
+'                                                        <input type="checkbox" id="chk_min94_2018_'+i+'" '+ (trayectoria[41] == '1' ? 'checked' : '')+' size="2"/>'
+'                                                    </td>';                                                          
                                                      }
                                                   }
                                                }
html2 +='                                 </tr>'
+'                                        <tr>'
+'                                            <td style="vertical-align: top; padding-left: 30px; height: 30px;'+estiloD+'">'
+'                                                        <%=meLanbide47I18n.getMensaje(idiomaUsuario,"label.trayectoria.programasSubvencionables.prog3")%>'
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
                                                    if(ano >= 2018) {
html2 +='                                               <td style="text-align: center; height: 30px; vertical-align: top;'+estiloI+'">'
+'                                                          <input type="radio" name="rad_min98_'+i+'" id="rad_min98_S_'+i+'" title="Si" '+(trayectoria[49] != null && trayectoria[49] == '1' ? 'checked' : '')+ ' size="2"/>'
+'                                                          |<input type="radio" name="rad_min98_'+i+'" id="rad_min98_N_'+i+'" title="No" '+(trayectoria[49] != null && trayectoria[49]  == '0' ? 'checked' : '')+ ' size="2"/>'
+'                                                      </td>';
                                                    }
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
                                                    if(ano < 2018) {  // A pertir del programa subv. 2 se debe ocultar.
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
html2 +='                                            <td style="text-align: center; height: 30px; vertical-align: top;">'
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
+'                                                    <td style="text-align: center; height: 30px; vertical-align: top;'+(ano<2017?estiloD:"")+'">'
+'                                                        <input type="checkbox" id="chk_min98_2016_'+i+'" '+ (trayectoria[23] == '1' ? 'checked' : '')+' size="2"/>'
+'                                                    </td>';
                                                        if(ano >= 2018){
html2 +='                                             <td style="text-align: center; height: 30px; vertical-align: top;">'
+'                                                        <input type="checkbox" id="chk_min98_2017_'+i+'" '+ (trayectoria[42] == '1' ? 'checked' : '')+' size="2"/>'
+'                                                    </td>'
+'                                                    <td style="text-align: center; height: 30px; vertical-align: top;'+estiloD+'">'
+'                                                        <input type="checkbox" id="chk_min98_2018_'+i+'" '+ (trayectoria[43] == '1' ? 'checked' : '')+' size="2"/>'
+'                                                    </td>';
                                                        }
                                                    }
                                                }
html2 +='                                 </tr>'
+'                                        <tr>'
+'                                            <td style="vertical-align: top; padding-left: 30px; height: 30px;'+estiloD+'">'
+'                                                        <%=meLanbide47I18n.getMensaje(idiomaUsuario,"label.trayectoria.programasSubvencionables.prog4")%>'
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
                                                    if(ano >= 2018) {
html2 +='                                               <td style="text-align: center; height: 30px; vertical-align: top;'+estiloI+'">'
+'                                                          <input type="radio" name="rad_tas03_'+i+'" id="rad_tas03_S_'+i+'" title="Si" '+(trayectoria[50] != null && trayectoria[50] == '1' ? 'checked' : '')+ ' size="2"/>'
+'                                                          |<input type="radio" name="rad_tas03_'+i+'" id="rad_tas03_N_'+i+'" title="No" '+(trayectoria[50] != null && trayectoria[50]  == '0' ? 'checked' : '')+ ' size="2"/>'
+'                                                      </td>';
                                                    }
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
                                                if(ano < 2018) {  // A pertir del programa subv. 2 se debe ocultar.
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
html2 +='                                                    <td style="text-align: center; height: 30px; vertical-align: top;">'
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
                                                      if(ano >= 2018){
html2 +='                                              <td style="text-align: center; height: 30px; vertical-align: top;">'
+'                                                        <input type="checkbox" id="chk_tas03_2017_'+i+'" '+ (trayectoria[44] == '1' ? 'checked' : '')+' size="2"/>'
+'                                                    </td>'
+'                                                    <td style="text-align: center; height: 30px; vertical-align: top;'+estiloD+'">'
+'                                                        <input type="checkbox" id="chk_tas03_2018_'+i+'" '+ (trayectoria[45] == '1' ? 'checked' : '')+' size="2"/>'
+'                                                    </td>';                                                          
                                                      }
                                                 }
                                               }
html2 +='                                 </tr>'
+'                                        <tr>'
+'                                            <td style="background-color: LightGrey; height: 14px;'+estiloD+'">'
+'                                                        <%=meLanbide47I18n.getMensaje(idiomaUsuario,"label.trayectoria.convocatoria")%>'
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
html2 +='                                                     <td style="text-align: center;background-color: LightGrey; height: 14px;'+estiloD+'" colspan="'+colspanN+'">'
+'                                                            </td>';
                                                      }
html2 +='                                 </tr>'
+'                                        <tr>'
+'                                            <td style="vertical-align: top; padding-left: 30px; height: 100px;'+estiloD+'">'
+'                                                        <%=meLanbide47I18n.getMensaje(idiomaUsuario,"label.trayectoria.convocatoria.conv1")%>'
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
+'                                                       <input type="checkbox" id="chk_lan_2011_'+i+'" '+ (trayectoria[35] == '1' ? 'checked' : '')+' size="2"/>'
+'                                                  </td>';
                                          }
html2 +='                                 </tr>'
+'                                        <tr>'
+'                                            <td style="vertical-align: top; padding-left: 30px; height: 100px;'+estiloD+'">' 
+'                                                        <%=meLanbide47I18n.getMensaje(idiomaUsuario,"label.trayectoria.convocatoria.conv2")%>'
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
+'                                                        <input type="checkbox" id="chk_lan_2013_'+i+'" '+ (trayectoria[36] == '1' ? 'checked' : '')+' size="2"/>'
+'                                                  </td>';
                                                }
html2 +='                                 </tr>';
                                        if(ano >= 2015) {
html2 +='                                 <tr>'
+'                                            <td style="vertical-align: top; padding-left: 30px; height: 100px;'+estiloD+'">'
+'                                                <%=meLanbide47I18n.getMensaje(idiomaUsuario,"label.trayectoria.convocatoria.conv3")%>'
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
+'                                                        <input type="checkbox" id="chk_lan_2014_'+i+'" '+ (trayectoria[37] == '1' ? 'checked' : '')+' size="2"/>'
+'                                                    </td>';
                                                }
html2 +='                                 </tr>';
                                        }
                                        if(ano >= 2017) {
html2 +='                                 <tr>'
+'                                            <td style="vertical-align: top; padding-left: 30px; height: 100px;'+estiloD+'">'
+'                                                    <%=meLanbide47I18n.getMensaje(idiomaUsuario,"label.trayectoria.convocatoria.conv4")%>'
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
+'                                                        <input type="checkbox" id="chk_lan_2015_'+i+'" '+ (trayectoria[38] == '1' ? 'checked' : '')+' size="2"/>'
+'                                                    </td>';
                                                }
html2 +='                                 </tr>';
                                        }
                                        if(ano >= 2018) {
html2 +='                                 <tr>'
+'                                            <td style="vertical-align: top; padding-left: 30px; height: 100px;'+estiloD+'">'
+'                                                    <%=meLanbide47I18n.getMensaje(idiomaUsuario,"label.trayectoria.convocatoria.conv5")%>'
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
+'                                                        <input type="checkbox" id="chk_lan_2017_'+i+'" '+ (trayectoria[46] == '1' ? 'checked' : '')+' size="2"/>'
+'                                                    </td>';
                                                }
html2 +='                                 </tr>';
                                       }
                                       if(ano < 2018){
html2 +='                                 <tr>'
+'                                            <td style="background-color: LightGrey; height: 14px;'+estiloD+'">'
+'                                                        <%=meLanbide47I18n.getMensaje(idiomaUsuario,"label.trayectoria.otrosProgramas")%>'
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
html2 +='                                       <td style="text-align: center; background-color: LightGrey; height: 14px; '+estiloD+'" colspan="'+colspanN+'">'
+'                                                        <%=meLanbide47I18n.getMensaje(idiomaUsuario,"label.trayectoria.duracionMeses")%>'
+'                                              </td>';
                                                }
html2 +='                                 </tr>'
+'                                        <tr>'
+'                                            <td style="vertical-align: top; padding-left: 30px; height: 40px;'+estiloD+'">'
+'                                                        <%=meLanbide47I18n.getMensaje(idiomaUsuario,"label.trayectoria.otrosProgramas.otrosProg1")%>'
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
html2 +='                                           <td style="text-align: center; height: 40px; vertical-align: central; '+estiloI+'" colspan="'+colspanN+'">'
+'                                                        <input type="text" id="txt_otros_'+i+'" value="'+(trayectoria[39] != '-' ? trayectoria[39] : '')+'" size="6" maxlength="8" class="inputTexto" title="<%=String.format(meLanbide47I18n.getMensaje(idiomaUsuario,"label.trayectoria.tipoDatoNumerico"), 8)%>"/>'                     
+'                                                  </td>';
                                                }
html2 +='                                 </tr>'
+'                                        <tr>'
+'                                            <td style="background-color: LightGrey; height: 14px;'+estiloD+'">'
+'                                                        <%=meLanbide47I18n.getMensaje(idiomaUsuario,"label.trayectoria.actividades")%>'
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
html2 +='                                       <td style="text-align: center; background-color: LightGrey; height: 14px; '+estiloD+'" colspan="'+colspanN+'">'
+'                                                            <%=meLanbide47I18n.getMensaje(idiomaUsuario,"label.trayectoria.duracionMeses")%>'
+'                                              </td>';
                                              }
html2 +='                                 </tr>'
+'                                        <tr>'
+'                                            <td style="vertical-align: top; padding-left: 30px; height: 60px;'+estiloD+'">'
+'                                                        <%=meLanbide47I18n.getMensaje(idiomaUsuario,"label.trayectoria.actividades.act1")%>'
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
html2 +='                                           <td style="text-align: center; height: 60px; vertical-align: central; '+estiloI+'" colspan="'+colspanN+'">'
+'                                                        <input type="text" id="txt_act5603_'+i+'" value="'+(trayectoria[34] != '-' ? trayectoria[34] : '')+'" size="6" maxlength="8" class="inputTexto" title="<%=String.format(meLanbide47I18n.getMensaje(idiomaUsuario,"label.trayectoria.tipoDatoNumerico"), 8)%>"/>'
+'                                                  </td>';
                                                }
html2 +='                                  </tr>';
                                    }
html2 +='                          </table>'
//+'                                </td>'
//+'                                <td style="width: 2%;">'
//+'                                    <div id="vScroll_trayectoriaGen" class="vertical-scroll" style="margin-left: -6px; display: none; width: 20px; height: 406px; display: inline-block; position: relative;" onscroll="scrollVertical(this, \'trayectoriaGen\');">'
//+'                                        <div style="height: 500px;"/>'
//+'                                    </div>'
//+'                                </td>'
//+'                            </tr>'
//+'                            <tr class="horizontal-scroll">'
//+'                                <td colspan="2" style="width: '+(numAsociaciones * 180)+'px;">'
//+'                                    <div class="horizontal-scroll" style="width: 867px; display:inline-block; position: relative; margin-top: -26px;" onscroll="scrollHorizontal(this, \'trayectoriaGen\');">'
//+'                                        <div style="width: '+((numAsociaciones * 180) + 300)+'px;"/>'
//+'                                    </div>'
//+'                                </td>'
//+'                            </tr>'
//+'                        </tbody>'
//+'                    </table>'
;     
  
        document.getElementById('divTablaTrayectoria').innerHTML = html2;
        document.getElementById('numeroAsociaciones').value = numAsociaciones;

    }
</script>

<body>
    <!--form  id="formGeneralTrayectoria" style="margin-top: 1px;"-->
        <div><!--align="center"-->
            <div style="clear: both; overflow-x: hidden; overflow-y: auto; height:450px ;"> <!--463px-->
                <div id="divTablaTrayectoria" style="overflow-x: auto; width: 1000px;">
                    <!--<table style="height: 434px;" onmousewheel="propagarEvento(event, 'trayectoriaGen');">
                        <tbody>
                            <tr>
                                <td>
                        -->           <table style="font-size: 12px; width:<%=((numAsociaciones * anchoTabla) + 400)%>px;  table-layout: fixed" >
                                        <tr>
                                             <td style="height: 13px; width: 400px; table-layout: fixed">
                                             </td>
                                             <%
                                                String estiloI = "";
                                                String estiloD = "";

                                                FilaAsociacionVO asociacion = null;
                                                OriTrayectoriaVO tray = null;
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
                                                        tray = trayectorias.get(asociacion.getCodAsociacion());
                                                    }
                                                    catch(Exception ex)
                                                    {
                                                        tray = null;
                                                    }

                                                    nombreAsociacion = asociacion.getCif() != null ? asociacion.getCif() : "";
                                                    nombreAsociacion += !nombreAsociacion.equals("") && asociacion.getNombre() != null && !asociacion.getNombre().equals("") ? " - " : "";
                                                    nombreAsociacion += asociacion.getNombre() != null ? asociacion.getNombre() : "";
                                            %>  
                                                    <td style="text-align: center; background-color: #4B95D3; height: 13px;<%=estiloD%>" colspan="<%=colspanNHtml%>" title="<%=nombreAsociacion%>">
                                                        <div style="overflow: hidden; text-overflow: ellipsis; white-space: nowrap; width: 100%;">
                                                            <%=nombreAsociacion%>
                                                            <input type="hidden" id="codigoAsoc_<%=i%>" value="<%=asociacion.getCodAsociacion() != null ? asociacion.getCodAsociacion() : ""%>">
                                                        </div>
                                                    </td>
                                            <%
                                                }
                                            %>
                                        </tr>
                                        <tr style="table-layout: initial">
                                            <td style="background-color: LightGrey; height: 14px;<%=estiloD%>">
                                                <%=meLanbide47I18n.getMensaje(idiomaUsuario,"label.trayectoria.programasSubvencionables")%>
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
                                            <% if(anoExp >= 2018) { %>
                                                <td style="text-align: center;background-color: LightGrey; height: 14px;"  width="50">
                                                E.Previa &nbsp Si|No
                                                </td>
                                            <% }%>
                                            <% if(anoExp < 2017) { %>
                                            <td style="text-align: center;background-color: LightGrey; height: 14px;" width="25">
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
                                            <% } %>
                                            <% if(anoExp >= 2017) { %>
                                            <% if(anoExp < 2018) { %>
                                            <td style="text-align: center;background-color: LightGrey; height: 14px;" width="25">
                                                2011
                                            </td>
                                            <% } %>
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
                                            <% } %>
                                            <% } %>
                                        <% }
                                            %>
                                        </tr>
                                        <tr>
                                            <td style="vertical-align: top; padding-left: 30px; height: 30px;<%=estiloD%>">
                                                        <%=meLanbide47I18n.getMensaje(idiomaUsuario,"label.trayectoria.programasSubvencionables.prog1")%>
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
                                                        tray = trayectorias.get(asociacion.getCodAsociacion());
                                                    }
                                                    catch(Exception ex)
                                                    {
                                                        tray = null;
                                                    }
                                            %>
                                                    <% if(anoExp >= 2018) { %>
                                                    <td style="text-align: center; height: 30px; vertical-align: top;<%=estiloI%>">
                                                        <input type="radio" name="rad_dec327_<%=i%>" id="rad_dec327_S_<%=i%>" title="Si" <%=tray != null && tray.getDec327() != null && tray.getDec327() == 1 ? "checked" : ""%> size="2"/>
                                                        |<input type="radio" name="rad_dec327_<%=i%>" id="rad_dec327_N_<%=i%>" title="No" <%=tray != null && tray.getDec327() != null && tray.getDec327() == 0 ? "checked" : ""%> size="2"/>
                                                    </td>
                                                    <% } %> 
                                                    <% if(anoExp < 2017) { %>
                                                    <td style="text-align: center; height: 30px; vertical-align: top;<%=estiloI%>">
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
                                                    <td style="text-align: center; height: 30px; vertical-align: top;<%=anoExp<2017?estiloD:""%>">
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
                                        <tr>
                                            <td style="vertical-align: top; padding-left: 30px; height: 30px;<%=estiloD%>">
                                                        <%=meLanbide47I18n.getMensaje(idiomaUsuario,"label.trayectoria.programasSubvencionables.prog2")%>
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
                                                        tray = trayectorias.get(asociacion.getCodAsociacion());
                                                    }
                                                    catch(Exception ex)
                                                    {
                                                        tray = null;
                                                    }
                                            %>
                                                    <% if(anoExp >= 2018) { %>
                                                    <td style="text-align: center; height: 30px; vertical-align: top;<%=estiloI%>">
                                                        <input type="radio" name="rad_min94_<%=i%>" id="rad_min94_S_<%=i%>" title="Si" <%=tray != null && tray.getMin94() != null && tray.getMin94() == 1 ? "checked" : ""%> size="2"/>
                                                        |<input type="radio" name="rad_min94_<%=i%>" id="rad_min94_N_<%=i%>" title="No" <%=tray != null && tray.getMin94() != null && tray.getMin94() == 0 ? "checked" : ""%> size="2"/>
                                                    </td>
                                                    <% } %>
                                                    <% if(anoExp < 2017) { %>
                                                    <td style="text-align: center; height: 30px; vertical-align: top;<%=estiloI%>">
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
                                                    <% } %>
                                                    <% if(anoExp >= 2017) { %>
                                                        <% if(anoExp <= 2017) { %>
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
                                                        <% } %>
                                                    <% }%>
                                            <%
                                                }
                                            %>
                                        </tr>
                                        <tr>
                                            <td style="vertical-align: top; padding-left: 30px; height: 30px;<%=estiloD%>">
                                                        <%=meLanbide47I18n.getMensaje(idiomaUsuario,"label.trayectoria.programasSubvencionables.prog3")%>
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
                                                        tray = trayectorias.get(asociacion.getCodAsociacion());
                                                    }
                                                    catch(Exception ex)
                                                    {
                                                        tray = null;
                                                    }
                                            %>
                                                    <% if(anoExp >= 2018) { %>
                                                    <td style="text-align: center; height: 30px; vertical-align: top;<%=estiloI%>">
                                                        <input type="radio" name="rad_min98_<%=i%>" id="rad_min98_S_<%=i%>" title="Si" <%=tray != null && tray.getMin98() != null && tray.getMin98() == 1 ? "checked" : ""%> size="2"/>
                                                        |<input type="radio" name="rad_min98_<%=i%>" id="rad_min98_N_<%=i%>" title="No" <%=tray != null && tray.getMin98() != null && tray.getMin98() == 0 ? "checked" : ""%> size="2"/>
                                                    </td>
                                                    <% } %>
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
                                                    <% }%>
                                                    <% if(anoExp <= 2017) { %>
                                                    <td style="text-align: center; height: 30px; vertical-align: top;<%=anoExp<2017?estiloD:""%>">
                                                        <input type="checkbox" id="chk_min98_2010_<%=i%>" <%=tray != null && tray.getMin98_2010() != null && tray.getMin98_2010() == 1 ? "checked" : ""%> size="2"/>
                                                    </td>
                                                    <% } %>
                                                    <% if(anoExp >= 2017) { %>
                                                        <% if(anoExp <= 2017) { %>
                                                        <td style="text-align: center; height: 30px; vertical-align: top;">
                                                            <input type="checkbox" id="chk_min98_2011_<%=i%>" <%=tray != null && tray.getMin98_2011() != null && tray.getMin98_2011() == 1 ? "checked" : ""%> size="2"/>
                                                        </td>
                                                        <% } %>
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
                                                    <% if(anoExp >= 2018) { %>
                                                        <td style="text-align: center; height: 30px; vertical-align: top;">
                                                            <input type="checkbox" id="chk_min98_2017_<%=i%>" <%=tray != null && tray.getMin98_2017() != null && tray.getMin98_2017() == 1 ? "checked" : ""%> size="2"/>
                                                        </td>
                                                        <td style="text-align: center; height: 30px; vertical-align: top;<%=estiloD%>">
                                                            <input type="checkbox" id="chk_min98_2018_<%=i%>" <%=tray != null && tray.getMin98_2018() != null && tray.getMin98_2018() == 1 ? "checked" : ""%> size="2"/>
                                                        </td>
                                                    <% } %>
                                                    <% }%>
                                            <%
                                                }
                                            %>
                                        </tr>
                                        <tr>
                                            <td style="vertical-align: top; padding-left: 30px; height: 30px;<%=estiloD%>">
                                                        <%=meLanbide47I18n.getMensaje(idiomaUsuario,"label.trayectoria.programasSubvencionables.prog4")%>
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
                                                        tray = trayectorias.get(asociacion.getCodAsociacion());
                                                    }
                                                    catch(Exception ex)
                                                    {
                                                        tray = null;
                                                    }
                                            %>
                                                <% if(anoExp >= 2018) { %>
                                                    <td style="text-align: center; height: 30px; vertical-align: top;<%=estiloI%>">
                                                        <input type="radio" name="rad_tas03_<%=i%>" id="rad_tas03_S_<%=i%>" title="Si" <%=tray != null && tray.getTas03() != null && tray.getTas03() == 1 ? "checked" : ""%> size="2"/>
                                                        |<input type="radio" name="rad_tas03_<%=i%>" id="rad_tas03_N_<%=i%>" title="No" <%=tray != null && tray.getTas03() != null && tray.getTas03() == 0 ? "checked" : ""%> size="2"/>
                                                    </td>
                                                <% } %>
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
                                                <% }%>
                                                <% if(anoExp < 2018) { %>
                                                    <td style="text-align: center; height: 30px; vertical-align: top;<%=anoExp<2017?estiloD:""%>">
                                                        <input type="checkbox" id="chk_tas03_2010_<%=i%>" <%=tray != null && tray.getTas03_2010() != null && tray.getTas03_2010() == 1 ? "checked" : ""%> size="2"/>
                                                    </td>
                                                <% }%>    
                                                <% if(anoExp >= 2017) { %>
                                                    <% if(anoExp <= 2017) { %>
                                                    <td style="text-align: center; height: 30px; vertical-align: top;">
                                                        <input type="checkbox" id="chk_tas03_2011_<%=i%>" <%=tray != null && tray.getTas03_2011() != null && tray.getTas03_2011() == 1 ? "checked" : ""%> size="2"/>
                                                    </td>
                                                    <% } %>
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
                                                    <% } %>
                                                <% }%>
                                            <%
                                                }
                                            %>
                                        </tr>
                                        <tr>
                                            <td style="background-color: LightGrey; height: 14px;<%=estiloD%>">
                                                        <%=meLanbide47I18n.getMensaje(idiomaUsuario,"label.trayectoria.convocatoria")%>
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
                                                    <td style="text-align: center;background-color: LightGrey; height: 14px;<%=estiloD%>" colspan="<%=colspanNHtml%>">
                                                    </td>
                                                    <%
                                                        }
                                                    %>
                                        </tr>
                                        <tr>
                                            <td style="vertical-align: top; padding-left: 30px; height: 100px;<%=estiloD%>">
                                                        <%=meLanbide47I18n.getMensaje(idiomaUsuario,"label.trayectoria.convocatoria.conv1")%>
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
                                                                tray = trayectorias.get(asociacion.getCodAsociacion());
                                                            }
                                                            catch(Exception ex)
                                                            {
                                                                tray = null;
                                                            }
                                                    %>
                                                            <td style="text-align: center; height: 100px; vertical-align: central;<%=estiloD%>" colspan="<%=colspanNHtml%>">
                                                                <input type="checkbox" id="chk_lan_2011_<%=i%>" <%=tray != null && tray.getLan_2011() != null && tray.getLan_2011() == 1 ? "checked" : ""%> size="2"/>
                                                            </td>
                                                    <%
                                                        }
                                                    %>
                                        </tr>
                                        <tr>
                                            <td style="vertical-align: top; padding-left: 30px; height: 100px;<%=estiloD%>">
                                                        <%=meLanbide47I18n.getMensaje(idiomaUsuario,"label.trayectoria.convocatoria.conv2")%>
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
                                                                tray = trayectorias.get(asociacion.getCodAsociacion());
                                                            }
                                                            catch(Exception ex)
                                                            {
                                                                tray = null;
                                                            }
                                                    %>
                                                            <td style="text-align: center; height: 100px; vertical-align: central;<%=estiloD%>" colspan="<%=colspanNHtml%>">
                                                                <input type="checkbox" id="chk_lan_2013_<%=i%>" <%=tray != null && tray.getLan_2013() != null && tray.getLan_2013() == 1 ? "checked" : ""%> size="2"/>
                                                            </td>
                                                    <%
                                                        }
                                                    %>
                                        </tr>
                                        <% if(anoExp >= 2015) { %>
                                        <tr>
                                            <td style="vertical-align: top; padding-left: 30px; height: 100px;<%=estiloD%>">
                                                <%=meLanbide47I18n.getMensaje(idiomaUsuario,"label.trayectoria.convocatoria.conv3")%>
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
                                                        tray = trayectorias.get(asociacion.getCodAsociacion());
                                                    }
                                                    catch(Exception ex)
                                                    {
                                                        tray = null;
                                                    }
                                            %>
                                                    <td style="text-align: center; height: 100px; vertical-align: central;<%=estiloD%>" colspan="<%=colspanNHtml%>">
                                                        <input type="checkbox" id="chk_lan_2014_<%=i%>" <%=tray != null && tray.getLan_2014() != null && tray.getLan_2014() == 1 ? "checked" : ""%> size="2"/>
                                                    </td>
                                            <%
                                                }
                                            %>
                                        </tr>
                                        <% }%>
                                        <% if(anoExp >= 2017) { %>
                                        <tr>
                                            <td style="vertical-align: top; padding-left: 30px; height: 100px;<%=estiloD%>">
                                                    <%=meLanbide47I18n.getMensaje(idiomaUsuario,"label.trayectoria.convocatoria.conv4")%>
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
                                                        tray = trayectorias.get(asociacion.getCodAsociacion());
                                                    }
                                                    catch(Exception ex)
                                                    {
                                                        tray = null;
                                                    }
                                            %>
                                                    <td style="text-align: center; height: 100px; vertical-align: central;<%=estiloD%>" colspan="<%=colspanNHtml%>">
                                                        <input type="checkbox" id="chk_lan_2015_<%=i%>" <%=tray != null && tray.getLan_2015() != null && tray.getLan_2015() == 1 ? "checked" : ""%> size="2"/>
                                                    </td>
                                            <%
                                                }
                                            %>
                                        </tr>
                                        <% }%>
                                        <% if(anoExp >= 2018) { %>
                                        <tr>
                                            <td style="vertical-align: top; padding-left: 30px; height: 100px;<%=estiloD%>">
                                                    <%=meLanbide47I18n.getMensaje(idiomaUsuario,"label.trayectoria.convocatoria.conv5")%>
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
                                                        tray = trayectorias.get(asociacion.getCodAsociacion());
                                                    }
                                                    catch(Exception ex)
                                                    {
                                                        tray = null;
                                                    }
                                            %>
                                                    <td style="text-align: center; height: 100px; vertical-align: central;<%=estiloD%>" colspan="<%=colspanNHtml%>">
                                                        <input type="checkbox" id="chk_lan_2017_<%=i%>" <%=tray != null && tray.getLan_2017() != null && tray.getLan_2017() == 1 ? "checked" : ""%> size="2"/>
                                                    </td>
                                            <%
                                                }
                                            %>
                                        </tr>
                                        <% }%>
                                        <% if(anoExp <= 2017) { %>
                                        <tr>
                                            <td style="background-color: LightGrey; height: 14px;<%=estiloD%>">
                                                        <%=meLanbide47I18n.getMensaje(idiomaUsuario,"label.trayectoria.otrosProgramas")%>
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
                                                    <td style="text-align: center; background-color: LightGrey; height: 14px;<%=estiloD%>" colspan="<%=anoExp<2017?"4":"7"%>">
                                                        <%=meLanbide47I18n.getMensaje(idiomaUsuario,"label.trayectoria.duracionMeses")%>
                                                    </td>
                                            <%
                                                }
                                            %>
                                        </tr>
                                        <tr>
                                            <td style="vertical-align: top; padding-left: 30px; height: 40px;<%=estiloD%>">
                                                        <%=meLanbide47I18n.getMensaje(idiomaUsuario,"label.trayectoria.otrosProgramas.otrosProg1")%>
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
                                                        tray = trayectorias.get(asociacion.getCodAsociacion());
                                                    }
                                                    catch(Exception ex)
                                                    {
                                                        tray = null;
                                                    }
                                            %>
                                                    <td style="text-align: center; height: 40px; vertical-align: central;<%=estiloI%>" colspan="<%=anoExp<2017?"4":"7"%>">
                                                        <input type="text" id="txt_otros_<%=i%>" value="<%=tray != null && tray.getLan_otros() != null ? tray.getLan_otros() : ""%>" size="6" maxlength="8" class="inputTexto" title="<%=String.format(meLanbide47I18n.getMensaje(idiomaUsuario,"label.trayectoria.tipoDatoNumerico"), 8)%>"/>
                                                    </td>
                                            <%
                                                }
                                            %>
                                        </tr>
                                        <tr>
                                            <td style="background-color: LightGrey; height: 14px;<%=estiloD%>">
                                                        <%=meLanbide47I18n.getMensaje(idiomaUsuario,"label.trayectoria.actividades")%>
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
                                                        <td style="text-align: center; background-color: LightGrey; height: 14px;<%=estiloD%>" colspan="<%=anoExp<2017?"4":"7"%>">
                                                            <%=meLanbide47I18n.getMensaje(idiomaUsuario,"label.trayectoria.duracionMeses")%>
                                                        </td>
                                                <%
                                                    }
                                                %>
                                        </tr>
                                        <tr>
                                            <td style="vertical-align: top; padding-left: 30px; height: 60px;<%=estiloD%>">
                                                        <%=meLanbide47I18n.getMensaje(idiomaUsuario,"label.trayectoria.actividades.act1")%>
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
                                                        tray = trayectorias.get(asociacion.getCodAsociacion());
                                                    }
                                                    catch(Exception ex)
                                                    {
                                                        tray = null;
                                                    }
                                            %>
                                                    <td style="text-align: center; height: 60px; vertical-align: central;<%=estiloI%>" colspan="<%=anoExp<2017?"4":"7"%>">
                                                        <input type="text" id="txt_act5603_<%=i%>" value="<%=tray != null && tray.getAct56_03() != null ? tray.getAct56_03() : ""%>" size="6" maxlength="8" class="inputTexto" title="<%=String.format(meLanbide47I18n.getMensaje(idiomaUsuario,"label.trayectoria.tipoDatoNumerico"), 8)%>"/>
                                                    </td>
                                            <%
                                                }
                                            %>
                                        </tr>
                                    <% } %>
                                    </table>
                        <!--        </td>
                            </tr>
                        </tbody>
                    </table>
                        -->
                </div>        
                <input type="hidden" id="numeroAsociaciones" />
                <input type="hidden" id="anoExpediente" />
                <div class="botonera">
                    <input type="button" id="btnGuardarTrayectoriaOri14" name="btnGuardarTrayectoriaOri14" class="botonGeneral" value="<%=meLanbide47I18n.getMensaje(idiomaUsuario,"btn.guardar")%>" onclick="guardarTrayectoria();"/>
                </div>
            </div>
        </div>
    <!--/form-->
</body>

<script type="text/javascript">   
    document.getElementById('numeroAsociaciones').value = <%=numAsociaciones%>;
    document.getElementById('anoExpediente').value = <%=anoExp%>;
</script>

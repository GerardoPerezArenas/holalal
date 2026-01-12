<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>

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
    int idiomaUsuario = Integer.parseInt(sIdioma);
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
    String mensajeProgreso = "";
    
    String[] datos = numExpediente.split("/");
    int anoExp = Integer.parseInt(datos[0]);
    
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

<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/estilo.css"/>
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
                            
                            +'&chk_dec327_2007_'+i+'='+(document.getElementById('chk_dec327_2007_'+i).checked ? 1 : 0)
                            +'&chk_dec327_2008_'+i+'='+(document.getElementById('chk_dec327_2008_'+i).checked ? 1 : 0)
                            +'&chk_dec327_2009_'+i+'='+(document.getElementById('chk_dec327_2009_'+i).checked ? 1 : 0)
                            +'&chk_dec327_2010_'+i+'='+(document.getElementById('chk_dec327_2010_'+i).checked ? 1 : 0)
                            +'&chk_min94_2007_'+i+'='+(document.getElementById('chk_min94_2007_'+i).checked ? 1 : 0)
                            +'&chk_min94_2008_'+i+'='+(document.getElementById('chk_min94_2008_'+i).checked ? 1 : 0)
                            +'&chk_min94_2009_'+i+'='+(document.getElementById('chk_min94_2009_'+i).checked ? 1 : 0)
                            +'&chk_min94_2010_'+i+'='+(document.getElementById('chk_min94_2010_'+i).checked ? 1 : 0)
                            +'&chk_min98_2007_'+i+'='+(document.getElementById('chk_min98_2007_'+i).checked ? 1 : 0)
                            +'&chk_min98_2008_'+i+'='+(document.getElementById('chk_min98_2008_'+i).checked ? 1 : 0)
                            +'&chk_min98_2009_'+i+'='+(document.getElementById('chk_min98_2009_'+i).checked ? 1 : 0)
                            +'&chk_min98_2010_'+i+'='+(document.getElementById('chk_min98_2010_'+i).checked ? 1 : 0)
                            +'&chk_tas03_2007_'+i+'='+(document.getElementById('chk_tas03_2007_'+i).checked ? 1 : 0)
                            +'&chk_tas03_2008_'+i+'='+(document.getElementById('chk_tas03_2008_'+i).checked ? 1 : 0)
                            +'&chk_tas03_2009_'+i+'='+(document.getElementById('chk_tas03_2009_'+i).checked ? 1 : 0)
                            +'&chk_tas03_2010_'+i+'='+(document.getElementById('chk_tas03_2010_'+i).checked ? 1 : 0)
                        
                            +'&txt_act5603_'+i+'='+document.getElementById('txt_act5603_'+i).value
                        
                            +'&chk_lan_2011_'+i+'='+(document.getElementById('chk_lan_2011_'+i).checked ? 1 : 0)
                            +'&chk_lan_2013_'+i+'='+(document.getElementById('chk_lan_2013_'+i).checked ? 1 : 0)
                            +'&chk_lan_2014_'+i+'='+(document.getElementById('chk_lan_2014_'+i).checked ? 1 : 0)
                        
                            +'&txt_otros_'+i+'='+document.getElementById('txt_otros_'+i).value
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
        var correcto = true;
        var i = 0;
        
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
                            else if(hijosTrayectoria[cont2].nodeName=="MIN_98_2007"){
                                if(hijosTrayectoria[cont2].childNodes.length > 0){
                                    filaTrayectoria[8] = hijosTrayectoria[cont2].childNodes[0].nodeValue;
                                }
                                else{
                                    filaTrayectoria[8] = '-';
                                }
                            }
                            else if(hijosTrayectoria[cont2].nodeName=="MIN_98_2008"){
                                if(hijosTrayectoria[cont2].childNodes.length > 0){
                                    filaTrayectoria[9] = hijosTrayectoria[cont2].childNodes[0].nodeValue;
                                }
                                else{
                                    filaTrayectoria[9] = '-';
                                }
                            }
                            else if(hijosTrayectoria[cont2].nodeName=="MIN_98_2009"){
                                if(hijosTrayectoria[cont2].childNodes.length > 0){
                                    filaTrayectoria[10] = hijosTrayectoria[cont2].childNodes[0].nodeValue;
                                }
                                else{
                                    filaTrayectoria[10] = '-';
                                }
                            }
                            else if(hijosTrayectoria[cont2].nodeName=="MIN_98_2010"){
                                if(hijosTrayectoria[cont2].childNodes.length > 0){
                                    filaTrayectoria[11] = hijosTrayectoria[cont2].childNodes[0].nodeValue;
                                }
                                else{
                                    filaTrayectoria[11] = '-';
                                }
                            }
                            else if(hijosTrayectoria[cont2].nodeName=="TAS_03_2007"){
                                if(hijosTrayectoria[cont2].childNodes.length > 0){
                                    filaTrayectoria[12] = hijosTrayectoria[cont2].childNodes[0].nodeValue;
                                }
                                else{
                                    filaTrayectoria[12] = '-';
                                }
                            }
                            else if(hijosTrayectoria[cont2].nodeName=="TAS_03_2008"){
                                if(hijosTrayectoria[cont2].childNodes.length > 0){
                                    filaTrayectoria[13] = hijosTrayectoria[cont2].childNodes[0].nodeValue;
                                }
                                else{
                                    filaTrayectoria[13] = '-';
                                }
                            }
                            else if(hijosTrayectoria[cont2].nodeName=="TAS_03_2009"){
                                if(hijosTrayectoria[cont2].childNodes.length > 0){
                                    filaTrayectoria[14] = hijosTrayectoria[cont2].childNodes[0].nodeValue;
                                }
                                else{
                                    filaTrayectoria[14] = '-';
                                }
                            }
                            else if(hijosTrayectoria[cont2].nodeName=="TAS_03_2010"){
                                if(hijosTrayectoria[cont2].childNodes.length > 0){
                                    filaTrayectoria[15] = hijosTrayectoria[cont2].childNodes[0].nodeValue;
                                }
                                else{
                                    filaTrayectoria[15] = '-';
                                }
                            }
                            else if(hijosTrayectoria[cont2].nodeName=="ACT_56_03"){
                                if(hijosTrayectoria[cont2].childNodes.length > 0){
                                    filaTrayectoria[16] = hijosTrayectoria[cont2].childNodes[0].nodeValue;
                                }
                                else{
                                    filaTrayectoria[16] = '-';
                                }
                            }
                            else if(hijosTrayectoria[cont2].nodeName=="LAN_2011"){
                                if(hijosTrayectoria[cont2].childNodes.length > 0){
                                    filaTrayectoria[17] = hijosTrayectoria[cont2].childNodes[0].nodeValue;
                                }
                                else{
                                    filaTrayectoria[17] = '-';
                                }
                            }
                            else if(hijosTrayectoria[cont2].nodeName=="LAN_2013"){
                                if(hijosTrayectoria[cont2].childNodes.length > 0){
                                    filaTrayectoria[18] = hijosTrayectoria[cont2].childNodes[0].nodeValue;
                                }
                                else{
                                    filaTrayectoria[18] = '-';
                                }
                            }
                            else if(hijosTrayectoria[cont2].nodeName=="LAN_OTROS"){
                                if(hijosTrayectoria[cont2].childNodes.length > 0){
                                    filaTrayectoria[19] = hijosTrayectoria[cont2].childNodes[0].nodeValue;
                                }
                                else{
                                    filaTrayectoria[19] = '-';
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
        
        var html = '<table style="height: 434px;" onmousewheel="propagarEvento(event, \'trayectoriaGen\');">'
                  +'    <tbody>'
                  +'          <tr>'
                  +'              <td class="tablefrozencolumn" style="width: 300px; vertical-align: top;">'
                  +'                  <div id="divfrozen_trayectoriaGen" style="overflow-x: hidden; overflow-y: hidden;" style="width: 300px; height: 406px; display: inline; float: left; padding-right: 25px;">'
                  +'                      <table style="font-size: 11px; border-collapse: collapse; width: 300px;">'
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
                    +'                          </tr>';
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
                  +'                      <table width="'+(numAsociaciones * 180)+'" class="content" style="border-collapse: collapse; font-size: 11px;">'
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
                        
                        html += '                       <td style="text-align: center;background-color: LightGrey; height: 14px;" width="25">'
                               +'                           2007'
                               +'                       </td>'
                               +'                       <td style="text-align: center;background-color: LightGrey; height: 14px;" width="25">'
                               +'                           2008'
                               +'                       </td>'
                               +'                       <td style="text-align: center;background-color: LightGrey; height: 14px;" width="25">'
                               +'                           2009'
                               +'                       </td>'
                               +'                       <td style="text-align: center;background-color: LightGrey; height: 14px;'+estiloD+'" width="25">'
                               +'                           2010'
                               +'                       </td>';
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
                    
                    html += '                     <td style="text-align: center; height: 30px; vertical-align: top;'+estiloI+'">'
                           +'                         <input type="checkbox" id="chk_dec327_2007_'+i+'" '+(trayectoria[0] == '1' ? 'checked' : '')+' size="2"/>'
                           +'                     </td>'
                           +'                     <td style="text-align: center; height: 30px; vertical-align: top;">'
                           +'                         <input type="checkbox" id="chk_dec327_2008_'+i+'" '+(trayectoria[1] == '1' ? 'checked' : '')+' size="2"/>'
                           +'                     </td>'
                           +'                     <td style="text-align: center; height: 30px; vertical-align: top;">'
                           +'                         <input type="checkbox" id="chk_dec327_2009_'+i+'" '+(trayectoria[2] == '1' ? 'checked' : '')+' size="2"/>'
                           +'                     </td>'
                           +'                     <td style="text-align: center; height: 30px; vertical-align: top;'+estiloD+'">'
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
                    
                    html += '                     <td style="text-align: center; height: 30px; vertical-align: top;'+estiloI+'">'
                           +'                         <input type="checkbox" id="chk_min94_2007_'+i+'" '+(trayectoria[4] == '1' ? 'checked' : '')+' size="2"/>'
                           +'                     </td>'
                           +'                     <td style="text-align: center; height: 30px; vertical-align: top;">'
                           +'                         <input type="checkbox" id="chk_min94_2008_'+i+'" '+(trayectoria[5] == '1' ? 'checked' : '')+' size="2"/>'
                           +'                     </td>'
                           +'                     <td style="text-align: center; height: 30px; vertical-align: top;">'
                           +'                         <input type="checkbox" id="chk_min94_2009_'+i+'" '+(trayectoria[6] == '1' ? 'checked' : '')+' size="2"/>'
                           +'                     </td>'
                           +'                     <td style="text-align: center; height: 30px; vertical-align: top;'+estiloD+'">'
                           +'                         <input type="checkbox" id="chk_min94_2010_'+i+'" '+(trayectoria[7] == '1' ? 'checked' : '')+' size="2"/>'
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
                    
                    html += '                     <td style="text-align: center; height: 30px; vertical-align: top;'+estiloI+'">'
                           +'                         <input type="checkbox" id="chk_min98_2007_'+i+'" '+(trayectoria[8] == '1' ? 'checked' : '')+' size="2"/>'
                           +'                     </td>'
                           +'                     <td style="text-align: center; height: 30px; vertical-align: top;">'
                           +'                         <input type="checkbox" id="chk_min98_2008_'+i+'" '+(trayectoria[9] == '1' ? 'checked' : '')+' size="2"/>'
                           +'                     </td>'
                           +'                     <td style="text-align: center; height: 30px; vertical-align: top;">'
                           +'                         <input type="checkbox" id="chk_min98_2009_'+i+'" '+(trayectoria[10] == '1' ? 'checked' : '')+' size="2"/>'
                           +'                     </td>'
                           +'                     <td style="text-align: center; height: 30px; vertical-align: top;'+estiloD+'">'
                           +'                         <input type="checkbox" id="chk_min98_2010_'+i+'" '+(trayectoria[11] == '1' ? 'checked' : '')+' size="2"/>'
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
                    
                    html += '                     <td style="text-align: center; height: 30px; vertical-align: top;'+estiloI+'">'
                           +'                         <input type="checkbox" id="chk_tas03_2007_'+i+'" '+(trayectoria[12] == '1' ? 'checked' : '')+' size="2"/>'
                           +'                     </td>'
                           +'                     <td style="text-align: center; height: 30px; vertical-align: top;">'
                           +'                         <input type="checkbox" id="chk_tas03_2008_'+i+'" '+(trayectoria[13] == '1' ? 'checked' : '')+' size="2"/>'
                           +'                     </td>'
                           +'                     <td style="text-align: center; height: 30px; vertical-align: top;">'
                           +'                         <input type="checkbox" id="chk_tas03_2009_'+i+'" '+(trayectoria[14] == '1' ? 'checked' : '')+' size="2"/>'
                           +'                     </td>'
                           +'                     <td style="text-align: center; height: 30px; vertical-align: top;'+estiloD+'">'
                           +'                         <input type="checkbox" id="chk_tas03_2010_'+i+'" '+(trayectoria[15] == '1' ? 'checked' : '')+' size="2"/>'
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
                          +'                          <input type="text" id="txt_act5603_'+i+'" value="'+(trayectoria[16] != '-' ? trayectoria[16] : '')+'" size="6" maxlength="8" class="inputTexto" title="<%=String.format(meLanbide47I18n.getMensaje(idiomaUsuario,"label.trayectoria.tipoDatoNumerico"), 8)%>"/>'
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
                           +'                         <input type="checkbox" id="chk_lan_2011_'+i+'" '+(trayectoria[17] == '1' ? 'checked' : '')+' size="2"/>'
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
                               +'                         <input type="checkbox" id="chk_lan_2013_'+i+'" '+(trayectoria[18] == '1' ? 'checked' : '')+' size="2"/>'
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
                           +'                         <input type="checkbox" id="chk_lan_2014_'+i+'" '+(trayectoria[18] == '1' ? 'checked' : '')+' size="2"/>'
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
                           +'                         <input type="text" id="txt_otros_'+i+'" value="'+(trayectoria[19] != '-' ? trayectoria[19] : '')+'" size="6" maxlength="8" class="inputTexto" title="<%=String.format(meLanbide47I18n.getMensaje(idiomaUsuario,"label.trayectoria.tipoDatoNumerico"), 8)%>"/>'
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
                   
                   
        document.getElementById('divTablaTrayectoria').innerHTML = html;
        document.getElementById('numeroAsociaciones').value = numAsociaciones;

    }
</script>

<body>
    <form  id="formGeneralTrayectoria" style="margin-top: 1px;">
        <div align="center">
            <div style="clear: both; overflow-x: hidden; overflow-y: auto; height: 463px;">
                <div id="divTablaTrayectoria">
                    <table style="height: 434px;" onmousewheel="propagarEvento(event, 'trayectoriaGen');">
                        <tbody>
                            <tr>
                                <td class="tablefrozencolumn" style="width: 300px; vertical-align: top;">
                                    <div id="divfrozen_trayectoriaGen" style="overflow-x: hidden; overflow-y: hidden;" style="width: 300px; height: 406px; display: inline; float: left; padding-right: 25px;">
                                        <table style="font-size: 11px; border-collapse: collapse; width: 300px;">
                                            <tr>
                                                <td style="height: 13px;">

                                                </td>
                                            </tr>
                                            <tr>
                                                <td style="background-color: LightGrey; height: 14px;">
                                                    <%=meLanbide47I18n.getMensaje(idiomaUsuario,"label.trayectoria.programasSubvencionables")%>
                                                </td>
                                            </tr>
                                            <tr>
                                                <td style="vertical-align: top; padding-left: 30px; height: 30px;">
                                                    <%=meLanbide47I18n.getMensaje(idiomaUsuario,"label.trayectoria.programasSubvencionables.prog1")%>
                                                </td>
                                            </tr>
                                            <tr>
                                                <td style="vertical-align: top; padding-left: 30px; height: 30px;">
                                                    <%=meLanbide47I18n.getMensaje(idiomaUsuario,"label.trayectoria.programasSubvencionables.prog2")%>
                                                </td>
                                            </tr>
                                            <tr>
                                                <td style="vertical-align: top; padding-left: 30px; height: 30px;">
                                                    <%=meLanbide47I18n.getMensaje(idiomaUsuario,"label.trayectoria.programasSubvencionables.prog3")%>
                                                </td>
                                            </tr>
                                            <tr>
                                                <td style="vertical-align: top; padding-left: 30px; height: 30px;">
                                                    <%=meLanbide47I18n.getMensaje(idiomaUsuario,"label.trayectoria.programasSubvencionables.prog4")%>
                                                </td>
                                            </tr>
                                            <tr>
                                                <td style="background-color: LightGrey; height: 14px;">
                                                    <%=meLanbide47I18n.getMensaje(idiomaUsuario,"label.trayectoria.actividades")%>
                                                </td>
                                            </tr>
                                            <tr>
                                                <td style="vertical-align: top; padding-left: 30px; height: 60px;">
                                                    <%=meLanbide47I18n.getMensaje(idiomaUsuario,"label.trayectoria.actividades.act1")%>
                                                </td>
                                            </tr>
                                            <tr>
                                                <td style="background-color: LightGrey; height: 14px;">
                                                    <%=meLanbide47I18n.getMensaje(idiomaUsuario,"label.trayectoria.convocatoria")%>
                                                </td>
                                            </tr>
                                            <tr>
                                                <td style="vertical-align: top; padding-left: 30px; height: 100px;">
                                                    <%=meLanbide47I18n.getMensaje(idiomaUsuario,"label.trayectoria.convocatoria.conv1")%>
                                                </td>
                                            </tr>
                                            <tr>
                                                <td style="vertical-align: top; padding-left: 30px; height: 100px;">
                                                    <%=meLanbide47I18n.getMensaje(idiomaUsuario,"label.trayectoria.convocatoria.conv2")%>
                                                </td>
                                            </tr>
                                            <% if(anoExp >= 2015) { %>
                                            <tr>
                                                <td style="vertical-align: top; padding-left: 30px; height: 100px;">
                                                    <%=meLanbide47I18n.getMensaje(idiomaUsuario,"label.trayectoria.convocatoria.conv3")%>
                                                </td>
                                            </tr>
                                            <% } %>
                                            <tr>
                                                <td style="background-color: LightGrey; height: 14px;">
                                                    <%=meLanbide47I18n.getMensaje(idiomaUsuario,"label.trayectoria.otrosProgramas")%>
                                                </td>
                                            </tr>
                                            <tr>
                                                <td style="vertical-align: top; padding-left: 30px; height: 40px;">
                                                    <%=meLanbide47I18n.getMensaje(idiomaUsuario,"label.trayectoria.otrosProgramas.otrosProg1")%>
                                                </td>
                                            </tr>
                                        </table>
                                    </div>
                                </td>
                                <td class="tablecontent" style="vertical-align: top; width: 600px;">
                                    <div id="contentscroll_trayectoriaGen" style="width: 560px; height: 406px;" class="content">
                                        <table width="<%=(numAsociaciones * 180)%>" class="content" style="border-collapse: collapse; font-size: 11px;">
                                            <tr>
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
                                                <td style="text-align: center; background-color: #4B95D3; height: 13px;<%=estiloD%>" colspan="4" title="<%=nombreAsociacion%>">
                                                    <div style="overflow: hidden; text-overflow: ellipsis; white-space: nowrap; width: 100%;">
                                                        <%=nombreAsociacion%>
                                                        <input type="hidden" id="codigoAsoc_<%=i%>" value="<%=asociacion.getCodAsociacion() != null ? asociacion.getCodAsociacion() : ""%>"
                                                    </div>
                                                </td>
                                        <%
                                            }
                                        %>
                                            </tr>
                                            <tr>
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
                                                <td style="text-align: center;background-color: LightGrey; height: 14px;" width="25">
                                                    2007
                                                </td>
                                                <td style="text-align: center;background-color: LightGrey; height: 14px;" width="25">
                                                    2008
                                                </td>
                                                <td style="text-align: center;background-color: LightGrey; height: 14px;" width="25">
                                                    2009
                                                </td>
                                                <td style="text-align: center;background-color: LightGrey; height: 14px;<%=estiloD%>" width="25">
                                                    2010
                                                </td>
                                        <%
                                            }
                                        %>
                                            </tr>
                                            <tr>
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
                                                <td style="text-align: center; height: 30px; vertical-align: top;<%=estiloI%>">
                                                    <input type="checkbox" id="chk_dec327_2007_<%=i%>" <%=tray != null && tray.getDec327_2007() != null && tray.getDec327_2007() == 1 ? "checked" : ""%> size="2"/>
                                                </td>
                                                <td style="text-align: center; height: 30px; vertical-align: top;">
                                                    <input type="checkbox" id="chk_dec327_2008_<%=i%>" <%=tray != null && tray.getDec327_2008() != null && tray.getDec327_2008() == 1 ? "checked" : ""%> size="2"/>
                                                </td>
                                                <td style="text-align: center; height: 30px; vertical-align: top;">
                                                    <input type="checkbox" id="chk_dec327_2009_<%=i%>" <%=tray != null && tray.getDec327_2009() != null && tray.getDec327_2009() == 1 ? "checked" : ""%> size="2"/>
                                                </td>
                                                <td style="text-align: center; height: 30px; vertical-align: top;<%=estiloD%>">
                                                    <input type="checkbox" id="chk_dec327_2010_<%=i%>" <%=tray != null && tray.getDec327_2010() != null && tray.getDec327_2010() == 1 ? "checked" : ""%> size="2"/>
                                                </td>
                                        <%
                                            }
                                        %>
                                            </tr>
                                            <tr>
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
                                                <td style="text-align: center; height: 30px; vertical-align: top;<%=estiloI%>">
                                                    <input type="checkbox" id="chk_min94_2007_<%=i%>" <%=tray != null && tray.getMin94_2007() != null && tray.getMin94_2007() == 1 ? "checked" : ""%> size="2"/>
                                                </td>
                                                <td style="text-align: center; height: 30px; vertical-align: top;">
                                                    <input type="checkbox" id="chk_min94_2008_<%=i%>" <%=tray != null && tray.getMin94_2008() != null && tray.getMin94_2008() == 1 ? "checked" : ""%> size="2"/>
                                                </td>
                                                <td style="text-align: center; height: 30px; vertical-align: top;">
                                                    <input type="checkbox" id="chk_min94_2009_<%=i%>" <%=tray != null && tray.getMin94_2009() != null && tray.getMin94_2009() == 1 ? "checked" : ""%> size="2"/>
                                                </td>
                                                <td style="text-align: center; height: 30px; vertical-align: top;<%=estiloD%>">
                                                    <input type="checkbox" id="chk_min94_2010_<%=i%>" <%=tray != null && tray.getMin94_2010() != null && tray.getMin94_2010() == 1 ? "checked" : ""%> size="2"/>
                                                </td>
                                        <%
                                            }
                                        %>
                                            </tr>
                                            <tr>
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
                                                <td style="text-align: center; height: 30px; vertical-align: top;<%=estiloI%>">
                                                    <input type="checkbox" id="chk_min98_2007_<%=i%>" <%=tray != null && tray.getMin98_2007() != null && tray.getMin98_2007() == 1 ? "checked" : ""%> size="2"/>
                                                </td>
                                                <td style="text-align: center; height: 30px; vertical-align: top;">
                                                    <input type="checkbox" id="chk_min98_2008_<%=i%>" <%=tray != null && tray.getMin98_2008() != null && tray.getMin98_2008() == 1 ? "checked" : ""%> size="2"/>
                                                </td>
                                                <td style="text-align: center; height: 30px; vertical-align: top;">
                                                    <input type="checkbox" id="chk_min98_2009_<%=i%>" <%=tray != null && tray.getMin98_2009() != null && tray.getMin98_2009() == 1 ? "checked" : ""%> size="2"/>
                                                </td>
                                                <td style="text-align: center; height: 30px; vertical-align: top;<%=estiloD%>">
                                                    <input type="checkbox" id="chk_min98_2010_<%=i%>" <%=tray != null && tray.getMin98_2010() != null && tray.getMin98_2010() == 1 ? "checked" : ""%> size="2"/>
                                                </td>
                                        <%
                                            }
                                        %>
                                            </tr>
                                            <tr>
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
                                                <td style="text-align: center; height: 30px; vertical-align: top;<%=estiloI%>">
                                                    <input type="checkbox" id="chk_tas03_2007_<%=i%>" <%=tray != null && tray.getTas03_2007() != null && tray.getTas03_2007() == 1 ? "checked" : ""%> size="2"/>
                                                </td>
                                                <td style="text-align: center; height: 30px; vertical-align: top;">
                                                    <input type="checkbox" id="chk_tas03_2008_<%=i%>" <%=tray != null && tray.getTas03_2008() != null && tray.getTas03_2008() == 1 ? "checked" : ""%> size="2"/>
                                                </td>
                                                <td style="text-align: center; height: 30px; vertical-align: top;">
                                                    <input type="checkbox" id="chk_tas03_2009_<%=i%>" <%=tray != null && tray.getTas03_2009() != null && tray.getTas03_2009() == 1 ? "checked" : ""%> size="2"/>
                                                </td>
                                                <td style="text-align: center; height: 30px; vertical-align: top;<%=estiloD%>">
                                                    <input type="checkbox" id="chk_tas03_2010_<%=i%>" <%=tray != null && tray.getTas03_2010() != null && tray.getTas03_2010() == 1 ? "checked" : ""%> size="2"/>
                                                </td>
                                        <%
                                            }
                                        %>
                                            </tr>
                                            <tr>
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
                                                        <td style="text-align: center; background-color: LightGrey; height: 14px;<%=estiloD%>" colspan="4">
                                                            <%=meLanbide47I18n.getMensaje(idiomaUsuario,"label.trayectoria.duracionMeses")%>
                                                        </td>
                                                <%
                                                    }
                                                %>
                                            </tr>
                                            <tr>
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
                                                        <td style="text-align: center; height: 60px; vertical-align: top;<%=estiloI%>" colspan="4">
                                                            <input type="text" id="txt_act5603_<%=i%>" value="<%=tray != null && tray.getAct56_03() != null ? tray.getAct56_03() : ""%>" size="6" maxlength="8" class="inputTexto" title="<%=String.format(meLanbide47I18n.getMensaje(idiomaUsuario,"label.trayectoria.tipoDatoNumerico"), 8)%>"/>
                                                        </td>
                                                <%
                                                    }
                                                %>
                                            </tr>
                                            <tr>
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
                                                        <td style="text-align: center;background-color: LightGrey; height: 14px;<%=estiloD%>" colspan="4">

                                                        </td>
                                                <%
                                                    }
                                                %>
                                            </tr>
                                            <tr>
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
                                                        <td style="text-align: center; height: 100px; vertical-align: top;<%=estiloD%>" colspan="4">
                                                            <input type="checkbox" id="chk_lan_2011_<%=i%>" <%=tray != null && tray.getLan_2011() != null && tray.getLan_2011() == 1 ? "checked" : ""%> size="2"/>
                                                        </td>
                                                <%
                                                    }
                                                %>
                                            </tr>
                                            <% if(anoExp >= 2015) { %>
                                            <tr>
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
                                                        <td style="text-align: center; height: 100px; vertical-align: top;<%=estiloD%>" colspan="4">
                                                            <input type="checkbox" id="chk_lan_2013_<%=i%>" <%=tray != null && tray.getLan_2013() != null && tray.getLan_2013() == 1 ? "checked" : ""%> size="2"/>
                                                        </td>
                                                <%
                                                    }
                                                %>
                                            </tr>
                                            <% } %>
                                            <tr>
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
                                                        <td style="text-align: center; height: 100px; vertical-align: top;<%=estiloD%>" colspan="4">
                                                            <input type="checkbox" id="chk_lan_2014_<%=i%>" <%=tray != null && tray.getLan_2014() != null && tray.getLan_2014() == 1 ? "checked" : ""%> size="2"/>
                                                        </td>
                                                <%
                                                    }
                                                %>
                                            </tr>
                                            <tr>
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
                                                        <td style="text-align: center; background-color: LightGrey; height: 14px;<%=estiloD%>" colspan="4">
                                                            <%=meLanbide47I18n.getMensaje(idiomaUsuario,"label.trayectoria.duracionMeses")%>
                                                        </td>
                                                <%
                                                    }
                                                %>
                                            </tr>
                                            <tr>
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
                                                        <td style="text-align: center; height: 40px; vertical-align: top;<%=estiloI%>" colspan="4">
                                                            <input type="text" id="txt_otros_<%=i%>" value="<%=tray != null && tray.getLan_otros() != null ? tray.getLan_otros() : ""%>" size="6" maxlength="8" class="inputTexto" title="<%=String.format(meLanbide47I18n.getMensaje(idiomaUsuario,"label.trayectoria.tipoDatoNumerico"), 8)%>"/>
                                                        </td>
                                                <%
                                                    }
                                                %>
                                            </tr>
                                        </table>
                                    </div>
                                </td>
                                <td style="width: 30px;">
                                    <div id="vScroll_trayectoriaGen" class="vertical-scroll" style="margin-left: -6px; display: none; width: 20px; height: 406px; display: inline-block; position: relative;" onscroll="scrollVertical(this, 'trayectoriaGen');">
                                        <div style="height: 500px;"/>
                                    </div>
                                </td>
                            </tr>
                            <tr class="horizontal-scroll">
                                <td colspan="2" style="width: <%=(numAsociaciones * 180)%>px;">
                                    <div class="horizontal-scroll" style="width: 867px; display:inline-block; position: relative; margin-top: -26px;" onscroll="scrollHorizontal(this, 'trayectoriaGen');">
                                        <div style="width: <%=((numAsociaciones * 180) + 300)%>px;"/>
                                    </div>
                                </td>
                            </tr>
                        </tbody>
                    </table>
                </div>        
                <input type="hidden" id="numeroAsociaciones" />
                <input type="hidden" id="anoExpediente" />
                <div class="botonera">
                    <input type="button" id="btnGuardarTrayectoriaOri14" name="btnGuardarTrayectoriaOri14" class="botonGeneral" value="<%=meLanbide47I18n.getMensaje(idiomaUsuario,"btn.guardar")%>" onclick="guardarTrayectoria();"/>
                </div>
            </div>
        </div>
    </form>
</body>

<script type="text/javascript">   
    document.getElementById('numeroAsociaciones').value = <%=numAsociaciones%>;
    document.getElementById('anoExpediente').value = <%=anoExp%>;
</script>
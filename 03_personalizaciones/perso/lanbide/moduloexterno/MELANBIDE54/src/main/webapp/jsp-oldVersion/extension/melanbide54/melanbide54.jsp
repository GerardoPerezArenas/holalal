<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>

<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide54.i18n.MeLanbide54I18n" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide54.vo.CentroVO" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@ page import="es.altia.common.service.config.Config"%>
<%@ page import="es.altia.common.service.config.ConfigServiceHelper"%>
<%@page import="java.util.ArrayList" %>
<%@page import="java.util.List" %>
<%@page import="org.apache.commons.lang.StringEscapeUtils"%>

<%@page import="es.altia.agora.business.util.GeneralValueObject"%>
<%@page import="es.altia.agora.interfaces.user.web.sge.FichaExpedienteForm"%>

<%
    String sIdioma = request.getParameter("idioma");
    //int idiomaUsuario = Integer.parseInt(sIdioma);
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
//prueba
    MeLanbide54I18n meLanbide54I18n = MeLanbide54I18n.getInstance();
    
    Config m_Config = ConfigServiceHelper.getConfig("common");
    String statusBar = m_Config.getString("JSP.StatusBar");
    String nombreModulo     = request.getParameter("nombreModulo");
    String codOrganizacion  = request.getParameter("codOrganizacionModulo");
    String numExpediente    = request.getParameter("numero");
    String mensajeProgreso = "";
%>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/estilo.css"/>
<link rel="stylesheet" type="text/css" href="<c:url value='/css/extension/melanbide39/melanbide39.css'/>">

<script type="text/javascript" src="<c:url value='/scripts/popup.js'/>"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/meLanbide54/FixedColumnsTable.js"></script>


<script type="text/javascript">  
    function recargarTabla(result){
        var fila;
        tab = new Array();
        listaTabla = new Array();
        listaTabla_titulos = new Array();
        listaTabla_estilos = new Array();
        fila = result[1];
        
        //recargarCalculosCpe(fila);
        for(var i = 1;i< result.length; i++){
            fila = result[i];
            lista[i-1] = fila;
            listaTabla[i-1] = [fila[0], fila[3], fila[5], fila[6], fila[7], fila[8], fila[9], fila[10], fila[11], fila[12]];
            listaTabla_titulos[i-1] = [fila[0], fila[3], fila[5], fila[6], fila[7], fila[8], fila[9], fila[10], fila[11], fila[12]];
        }

        tab = new Tabla(document.getElementById('resumenesCpe'), 765); //790
        tab.addColumna('40','left',"Id");
        tab.addColumna('140','left',"<%= meLanbide54I18n.getMensaje(idiomaUsuario,"label.tabla.col1")%>");
        tab.addColumna('100','left',"<%= meLanbide54I18n.getMensaje(idiomaUsuario,"label.tabla.col2")%>");    
        tab.addColumna('120','left',"<%= meLanbide54I18n.getMensaje(idiomaUsuario,"label.tabla.col3")%>");   
        tab.addColumna('60','left',"<%= meLanbide54I18n.getMensaje(idiomaUsuario,"label.tabla.col4")%>");   
        tab.addColumna('60','left',"<%= meLanbide54I18n.getMensaje(idiomaUsuario,"label.tabla.col5")%>");   
        tab.addColumna('60','left',"<%= meLanbide54I18n.getMensaje(idiomaUsuario,"label.tabla.col6")%>");   
        tab.addColumna('60','right',"<%= meLanbide54I18n.getMensaje(idiomaUsuario,"label.tabla.col7")%>");   
        tab.addColumna('100','right',"<%= meLanbide54I18n.getMensaje(idiomaUsuario,"label.tabla.col8")%>");
        tab.addColumna('140','right',"<%= meLanbide54I18n.getMensaje(idiomaUsuario,"label.tabla.col9")%>");

        tab.displayCabecera=true;
        
        tab.lineas=listaTabla;
        //tabEspecialidades.dblClkFunction = 'dblClckListasxEspecialidad';
        //tab.displayTabla();
        if(navigator.appName.indexOf("Internet Explorer")!=-1){
            try{
                var div = document.getElementById('resumenesCpe');
                //div.children[0].children[0].children[0].children[0].children[0].children[1].style.width = '100%';
                //div.children[0].children[1].children[0].children[1].style.width = '100%'; 
                div.children[0].children[0].children[0].children[0].children[0].children[0].children[0].children[0].children[0].children[1].style.width = '100%';
                div.children[1].style.width = '100%'; 
            }
            catch(err){

            }
        }

        tab.height = '146';

        tab.altoCabecera = 50;

        tab.scrollWidth = 5372;

        tab.displayTabla();


        tab.displayCabecera=true;
    
        /*for(var cont = 0; cont < listaTabla.length; cont++){
            tab.addFilaConFormato(listaTabla[cont], listaTabla_titulos[cont], listaTabla_estilos[cont])
        }

        tab.height = '146';
    
        tab.altoCabecera = 50;

        tab.scrollWidth = 5372;

        tab.displayTabla();*/


    }
    
    function actualizarPestana(){
        try{
            var nodos = this.getListaResumenCpe();
            var result = this.extraerListado(nodos);
            this.recargarTablaResumenCpe(result);
        }catch(err){
            
        }
    }
    
    function getLista(){
        var ajax = getXMLHttpRequest();
        var nodos = null;
        var CONTEXT_PATH = '<%=request.getContextPath()%>'
        var url = CONTEXT_PATH + '/PeticionModuloIntegracion.do';
        var parametros = '';
        var control = new Date();
        parametros = 'tarea=preparar&modulo=MELANBIDE54&operacion=getListaCentros&tipo=0&numero=<%=numExpediente%>&control='+control.getTime();
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
        }
        catch(err){
            alert(err);
            jsp_alerta("A",'<%=meLanbide54I18n.getMensaje(idiomaUsuario,"error.errorProcesarRespuesta")%>');
        }//try-catch
        return nodos;
    }
    
    function pulsarAlta(){
        var control = new Date();
        var result = null;
        if(navigator.appName.indexOf("Internet Explorer")!=-1){
            result = lanzarPopUpModal('<%=request.getContextPath()%>/PeticionModuloIntegracion.do?tarea=preparar&modulo=MELANBIDE54&operacion=cargarNuevoCentro&tipo=0&numero=<%=numExpediente%>&control='+control.getTime(),400,800,'yes','no');
        }else{
            result = lanzarPopUpModal('<%=request.getContextPath()%>/PeticionModuloIntegracion.do?tarea=preparar&modulo=MELANBIDE54&operacion=cargarNuevoCentro&tipo=0&numero=<%=numExpediente%>&control='+control.getTime(),400,800,'no','no');
        }
        if (result != undefined){
            if(result[0] == '0'){
                recargarTabla(result);
            }
        }
    }

    function pulsarModificar(){
        if(tab.selectedIndex != -1) {
            var control = new Date();
            var result = null;
            if(navigator.appName.indexOf("Internet Explorer")!=-1){
                result = lanzarPopUpModal('<%=request.getContextPath()%>/PeticionModuloIntegracion.do?tarea=preparar&modulo=MELANBIDE54&operacion=cargarModifCentro&tipo=0&numero=<%=numExpediente%>&id='+lista[tab.selectedIndex][0]+'&control='+control.getTime(),400,800,'yes','no');
            }else{
                result = lanzarPopUpModal('<%=request.getContextPath()%>/PeticionModuloIntegracion.do?tarea=preparar&modulo=MELANBIDE54&operacion=cargarModifCentro&tipo=0&numero=<%=numExpediente%>&id='+lista[tab.selectedIndex][0]+'&control='+control.getTime(),400,800,'no','no');
            }
            if (result != undefined){
                if(result[0] == '0'){
                    recargarTabla(result);
                }
            }
        } 
        else {
            jsp_alerta('A', '<%=meLanbide54I18n.getMensaje(idiomaUsuario, "msg.msjNoSelecFila")%>');
        }
    }

    function pulsarEliminar(){
        if(tab.selectedIndex != -1) {
            var resultado = jsp_alerta('', '<%=meLanbide54I18n.getMensaje(idiomaUsuario, "msg.preguntaEliminar")%>');
            if (resultado == 1){
                var ajax = getXMLHttpRequest();
                var nodos = null;
                var CONTEXT_PATH = '<%=request.getContextPath()%>'
                var url = CONTEXT_PATH + "/PeticionModuloIntegracion.do";
                var parametros = "";
                var control = new Date();
                parametros = 'tarea=preparar&modulo=MELANBIDE54&operacion=eliminarCentro&tipo=0&numero=<%=numExpediente%>&id='+lista[tab.selectedIndex][0]+'&control='+control.getTime();
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
                    var listaNueva = new Array();
                    var fila = new Array();
                    var nodoFila;
                    var hijosFila;
                    var nodoCampo;
                    for(j=0;hijos!=null && j<hijos.length;j++){
                            if(hijos[j].nodeName=="CODIGO_OPERACION"){                            
                                codigoOperacion = hijos[j].childNodes[0].nodeValue;
                                listaNueva[j] = codigoOperacion;
                            }//if(hijos[j].nodeName=="CODIGO_OPERACION")                      
                            else if(hijos[j].nodeName=="FILA"){
                                nodoFila = hijos[j];
                                hijosFila = nodoFila.childNodes;
                                for(var cont = 0; cont < hijosFila.length; cont++){
                                    if(hijosFila[cont].nodeName=="ID"){
                                        nodoCampo = hijosFila[cont];
                                        if(nodoCampo.childNodes.length > 0){
                                            fila[0] = nodoCampo.childNodes[0].nodeValue;
                                        }
                                        else{
                                            fila[0] = '-';
                                        }
                                    }else if(hijosFila[cont].nodeName=="EXP_NUM"){
                                        nodoCampo = hijosFila[cont];
                                        if(nodoCampo.childNodes.length > 0){
                                            fila[1] = nodoCampo.childNodes[0].nodeValue;
                                        }
                                        else{
                                            fila[1] = '-';
                                        }
                                    }else if(hijosFila[cont].nodeName=="CODTH"){
                                        nodoCampo = hijosFila[cont];
                                        if(nodoCampo.childNodes.length > 0){
                                            fila[2] = nodoCampo.childNodes[0].nodeValue;
                                        }
                                        else{
                                            fila[2] = '-';
                                        }
                                    }
                                    else if(hijosFila[cont].nodeName=="DESCODTH"){
                                        nodoCampo = hijosFila[cont];
                                        if(nodoCampo.childNodes.length > 0){
                                            fila[3] = nodoCampo.childNodes[0].nodeValue;
                                        }
                                        else{
                                            fila[3] = '-';
                                        }
                                    }else if(hijosFila[cont].nodeName=="CODMUN"){
                                        nodoCampo = hijosFila[cont];
                                        if(nodoCampo.childNodes.length > 0){
                                            fila[4] = nodoCampo.childNodes[0].nodeValue;
                                        }
                                        else{
                                            fila[4] = '-';
                                        }
                                    }
                                    else if(hijosFila[cont].nodeName=="DESCODMUN"){
                                        nodoCampo = hijosFila[cont];
                                        if(nodoCampo.childNodes.length > 0){
                                            fila[5] = nodoCampo.childNodes[0].nodeValue;
                                        }
                                        else{
                                            fila[5] = '-';
                                        }
                                    }
                                    else if(hijosFila[cont].nodeName=="CALLE"){
                                        nodoCampo = hijosFila[cont];
                                        if(nodoCampo.childNodes.length > 0){
                                            fila[6] = nodoCampo.childNodes[0].nodeValue;
                                        }
                                        else{
                                            fila[6] = '-';
                                        }
                                    }
                                    else if(hijosFila[cont].nodeName=="PORTAL"){
                                        nodoCampo = hijosFila[cont];
                                        if(nodoCampo.childNodes.length > 0){
                                            fila[7] = nodoCampo.childNodes[0].nodeValue;
                                        }
                                        else{
                                            fila[7] = '-';
                                        }
                                    }
                                    else if(hijosFila[cont].nodeName=="PISO"){
                                        nodoCampo = hijosFila[cont];
                                        if(nodoCampo.childNodes.length > 0){
                                            fila[8] = nodoCampo.childNodes[0].nodeValue;
                                        }
                                        else{
                                            fila[8] = '-';
                                        }
                                    }
                                    else if(hijosFila[cont].nodeName=="LETRA"){
                                        nodoCampo = hijosFila[cont];
                                        if(nodoCampo.childNodes.length > 0){
                                            fila[9] = nodoCampo.childNodes[0].nodeValue;
                                        }
                                        else{
                                            fila[9] = '';
                                        }
                                    }
                                    else if(hijosFila[cont].nodeName=="CP"){
                                        nodoCampo = hijosFila[cont];
                                        if(nodoCampo.childNodes.length > 0){
                                            fila[10] = nodoCampo.childNodes[0].nodeValue;
                                        }
                                        else{
                                            fila[10] = '-';
                                        }
                                    }
                                    else if(hijosFila[cont].nodeName=="TELEF"){
                                        nodoCampo = hijosFila[cont];
                                        if(nodoCampo.childNodes.length > 0){
                                            fila[11] = nodoCampo.childNodes[0].nodeValue;
                                        }
                                        else{
                                            fila[11] = '-';
                                        }
                                    }
                                    else if(hijosFila[cont].nodeName=="EMAIL"){
                                        nodoCampo = hijosFila[cont];
                                        if(nodoCampo.childNodes.length > 0){
                                            fila[12] = nodoCampo.childNodes[0].nodeValue;
                                        }
                                        else{
                                            fila[12] = '-';
                                        }
                                    }
                            }
                            listaNueva[j] = fila;
                            fila = new Array();
                        }   
                    }//for(j=0;hijos!=null && j<hijos.length;j++)
                    if(codigoOperacion=="0"){
                        recargarTabla(listaNueva);
                    }else if(codigoOperacion=="1"){
                        jsp_alerta("A",'<%=meLanbide54I18n.getMensaje(idiomaUsuario,"error.errorBD")%>');
                    }else if(codigoOperacion=="2"){
                        jsp_alerta("A",'<%=meLanbide54I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                    }else if(codigoOperacion=="3"){
                        jsp_alerta("A",'<%=meLanbide54I18n.getMensaje(idiomaUsuario,"error.pasoParametros")%>');
                    }else{
                            jsp_alerta("A",'<%=meLanbide54I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                    }//if(
                }
                catch(Err){
                    jsp_alerta("A",'<%=meLanbide54I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                }//try-catch
            }
        } 
        else {
            jsp_alerta('A', '<%=meLanbide54I18n.getMensaje(idiomaUsuario, "msg.msjNoSelecFila")%>');
        }
    }
    
    
    
    function extraerListado(nodos){
            var elemento = nodos[0];
            var hijos = elemento.childNodes;
            var codigoOperacion = null;
            var listaJustif = new Array();
            var fila = new Array();
            var nodoFila;
            var hijosFila;
            var nodoCampo;
            var j;
            
            for(j=0;hijos!=null && j<hijos.length;j++){
                if(hijos[j].nodeName=="CODIGO_OPERACION"){                            
                    codigoOperacion = hijos[j].childNodes[0].nodeValue;
                    listaJustif[j] = codigoOperacion;
                }                  
                else if(hijos[j].nodeName=="FILA"){
                    nodoFila = hijos[j];
                    hijosFila = nodoFila.childNodes;
                    for(var cont = 0; cont < hijosFila.length; cont++){
                        if(hijosFila[cont].nodeName=="ID"){
                            nodoCampo = hijosFila[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[0] = nodoCampo.childNodes[0].nodeValue;
                            }
                            else{
                                fila[0] = '-';
                            }
                        }else if(hijosFila[cont].nodeName=="EXP_NUM"){
                            nodoCampo = hijosFila[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[1] = nodoCampo.childNodes[0].nodeValue;
                            }
                            else{
                                fila[1] = '-';
                            }
                        }else if(hijosFila[cont].nodeName=="CODTH"){
                            nodoCampo = hijosFila[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[2] = nodoCampo.childNodes[0].nodeValue;
                            }
                            else{
                                fila[2] = '-';
                            }
                        }
                        else if(hijosFila[cont].nodeName=="DESCODTH"){
                            nodoCampo = hijosFila[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[3] = nodoCampo.childNodes[0].nodeValue;
                            }
                            else{
                                fila[3] = '-';
                            }
                        }else if(hijosFila[cont].nodeName=="CODMUN"){
                            nodoCampo = hijosFila[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[4] = nodoCampo.childNodes[0].nodeValue;
                            }
                            else{
                                fila[4] = '-';
                            }
                        }
                        else if(hijosFila[cont].nodeName=="DESCODMUN"){
                            nodoCampo = hijosFila[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[5] = nodoCampo.childNodes[0].nodeValue;
                            }
                            else{
                                fila[5] = '-';
                            }
                        }
                        else if(hijosFila[cont].nodeName=="CALLE"){
                            nodoCampo = hijosFila[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[6] = nodoCampo.childNodes[0].nodeValue;
                            }
                            else{
                                fila[6] = '-';
                            }
                        }
                        else if(hijosFila[cont].nodeName=="PORTAL"){
                            nodoCampo = hijosFila[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[7] = nodoCampo.childNodes[0].nodeValue;
                            }
                            else{
                                fila[7] = '-';
                            }
                        }
                        else if(hijosFila[cont].nodeName=="PISO"){
                            nodoCampo = hijosFila[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[8] = nodoCampo.childNodes[0].nodeValue;
                            }
                            else{
                                fila[8] = '-';
                            }
                        }
                        else if(hijosFila[cont].nodeName=="LETRA"){
                            nodoCampo = hijosFila[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[9] = nodoCampo.childNodes[0].nodeValue;
                            }
                            else{
                                fila[9] = '';
                            }
                        }
                        else if(hijosFila[cont].nodeName=="CP"){
                            nodoCampo = hijosFila[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[10] = nodoCampo.childNodes[0].nodeValue;
                            }
                            else{
                                fila[10] = '-';
                            }
                        }
                        else if(hijosFila[cont].nodeName=="TELEF"){
                            nodoCampo = hijosFila[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[11] = nodoCampo.childNodes[0].nodeValue;
                            }
                            else{
                                fila[11] = '-';
                            }
                        }
                        else if(hijosFila[cont].nodeName=="EMAIL"){
                            nodoCampo = hijosFila[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[12] = nodoCampo.childNodes[0].nodeValue;
                            }
                            else{
                                fila[12] = '-';
                            }
                        }
                    }
                    listaJustif[j] = fila;
                    
                    fila = new Array();   
                }
        } 
        return listaJustif;
    }
</script>
<body>
    <div id="barraProgresoPuestosResumenCpe" style="position: absolute; z-index:10; visibility: hidden; top: 30%; left: 30%;">
        <table width="100%" border="0px" cellpadding="0px" cellspacing="0px" border="0px">
            <tr>
                <td align="center" valign="middle">
                    <table class="contenedorHidepage" cellpadding="0px" cellspacing="0px" border="0px">
                        <tr>
                            <td>
                                <table width="349px" height="100%">
                                    <tr>
                                        <td colspan="3" style="height:70%;text-align:center;valign:middle;">
                                            <span id="msgCargandoPuestoResumenCpe">
                                                <%=meLanbide54I18n.getMensaje(idiomaUsuario, "msg.cargandoDatos")%>
                                            </span>
                                            <span id="msgEliminandoPuestoResumenCpe">
                                                <%=meLanbide54I18n.getMensaje(idiomaUsuario, "msg.eliminandoDatos")%>
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
                                            
    <div class="tab-page" id="tabPage541" style="clear: both;">
        <h2 class="tab" id="pestana541"><%=meLanbide54I18n.getMensaje(idiomaUsuario,"label.pestana.titulo")%></h2>
        <script type="text/javascript">tp1_p541 = tp1.addTabPage( document.getElementById( "tabPage541" ) );</script>
        <div id="resumenesCpe" style="padding: 5px; width:850px; height: 300px; text-align: center; overflow-x: auto; overflow-y: auto;margiwidth:0px;margin-top:0px;" align="center"></div>
        <div class="botonera">
            <input type="button" id="btnNuevoModuloForm" name="btnNuevoModuloForm" class="botonGeneral"  value="<%=meLanbide54I18n.getMensaje(idiomaUsuario, "btn.nuevo")%>" onclick="pulsarAlta();">
            <input type="button" id="btnEliminarModuloForm" name="btnEliminarModuloForm"   class="botonGeneral" value="<%=meLanbide54I18n.getMensaje(idiomaUsuario, "btn.eliminar")%>" onclick="pulsarEliminar();">
            <input type="button" id="btnModificarModuloForm" name="btnModificarModuloForm" class="botonGeneral" value="<%=meLanbide54I18n.getMensaje(idiomaUsuario, "btn.modificar")%>" onclick="pulsarModificar();">
        </div>
    </div>
</body>

<script type="text/javascript">
    var tab;
    var lista = new Array();
    var listaTabla = new Array();
    var listaTabla_titulos = new Array();
    var listaTabla_estilos = new Array();
    tab = new Tabla(document.getElementById('resumenesCpe'), 765); //790
    tab.addColumna('40','left',"Id");
    tab.addColumna('140','left',"<%= meLanbide54I18n.getMensaje(idiomaUsuario,"label.tabla.col1")%>");
    tab.addColumna('100','left',"<%= meLanbide54I18n.getMensaje(idiomaUsuario,"label.tabla.col2")%>");    
    tab.addColumna('120','left',"<%= meLanbide54I18n.getMensaje(idiomaUsuario,"label.tabla.col3")%>");   
    tab.addColumna('60','left',"<%= meLanbide54I18n.getMensaje(idiomaUsuario,"label.tabla.col4")%>");   
    tab.addColumna('60','left',"<%= meLanbide54I18n.getMensaje(idiomaUsuario,"label.tabla.col5")%>");   
    tab.addColumna('60','left',"<%= meLanbide54I18n.getMensaje(idiomaUsuario,"label.tabla.col6")%>");   
    tab.addColumna('60','right',"<%= meLanbide54I18n.getMensaje(idiomaUsuario,"label.tabla.col7")%>");
    tab.addColumna('100','right',"<%= meLanbide54I18n.getMensaje(idiomaUsuario,"label.tabla.col8")%>");
    tab.addColumna('140','right',"<%= meLanbide54I18n.getMensaje(idiomaUsuario,"label.tabla.col9")%>");
    
   

    tab.displayCabecera=true;
    
    <%  		
        CentroVO filaResumen = null;
        List<CentroVO> listaResumen = (List<CentroVO>)request.getAttribute("ListErrores");													
        if (listaResumen != null && listaResumen.size() >0){
            for(int i = 0;i < listaResumen.size();i++)
            {
                filaResumen = listaResumen.get(i);
        %>
            lista[<%=i%>] = ['<%=filaResumen.getId()%>', '<%=filaResumen.getDesCodTH()%>', '<%=filaResumen.getDesCodMun()%>', '<%=filaResumen.getCalle()%>', '<%=filaResumen.getPortal()%>', '<%=filaResumen.getPiso()%>', '<%=filaResumen.getLetra()%>', '<%=filaResumen.getCp()%>', '<%=filaResumen.getTlef()%>', '<%=filaResumen.getEmail()%>'];
            listaTabla[<%=i%>] = ['<%=filaResumen.getId()%>', '<%=filaResumen.getCodTH()%>','<%=filaResumen.getDesCodTH()%>', '<%=filaResumen.getDesCodMun()%>', '<%=filaResumen.getCodMun()%>', '<%=filaResumen.getCalle()%>', '<%=filaResumen.getPortal()%>', '<%=filaResumen.getPiso()%>', '<%=filaResumen.getLetra()%>', '<%=filaResumen.getCp()%>', '<%=filaResumen.getTlef()%>', '<%=filaResumen.getEmail()%>'];

        <%
            }// for
        }// if
    %>
    tab.lineas=lista;
    //tabEspecialidades.dblClkFunction = 'dblClckListasxEspecialidad';
    //tab.displayTabla();
    if(navigator.appName.indexOf("Internet Explorer")!=-1){
        try{
            var div = document.getElementById('resumenesCpe');
            //div.children[0].children[0].children[0].children[0].children[0].children[1].style.width = '100%';
            //div.children[0].children[1].children[0].children[1].style.width = '100%'; 
            div.children[0].children[0].children[0].children[0].children[0].children[0].children[0].children[0].children[0].children[1].style.width = '100%';
            div.children[1].style.width = '100%'; 
        }
        catch(err){

        }
    }
    /*document.getElementById('resumenesCpe').children[0].children[1].children[0].children[0].ondblclick = function(event){
        pulsarModificar(event);
    }*/
    
    //for(var cont = 0; cont < listaTabla.length; cont++){
      //  tabe.addFilaConFormato(listaTabla[cont], listaTabla_titulos[cont], listaTabla_estilos[cont])
    //}
    
    tab.height = '146';
    
    tab.altoCabecera = 50;
    
    tab.scrollWidth = 5372;
    
    tab.displayTabla();
    
    //tab.pack();
</script>
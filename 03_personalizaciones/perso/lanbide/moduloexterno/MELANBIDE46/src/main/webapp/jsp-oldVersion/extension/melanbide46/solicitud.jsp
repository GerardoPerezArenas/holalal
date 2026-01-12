<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>

<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide46.i18n.MeLanbide46I18n" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide46.vo.FilaPuestoVO" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@ page import="es.altia.common.service.config.Config"%>
<%@ page import="es.altia.common.service.config.ConfigServiceHelper"%>
<%@page import="java.util.ArrayList" %>
<%@page import="java.util.List" %>

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
    MeLanbide46I18n meLanbide46I18n = MeLanbide46I18n.getInstance();
    
    Config m_Config = ConfigServiceHelper.getConfig("common");
    String statusBar = m_Config.getString("JSP.StatusBar");
    String nombreModulo     = request.getParameter("nombreModulo");
    String codOrganizacion  = request.getParameter("codOrganizacionModulo");
    String numExpediente    = request.getParameter("numero");
    String mensajeProgreso = "";
%>

<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/estilo.css"/>
<link rel="stylesheet" type="text/css" href="<c:url value='/css/extension/melanbide46/melanbide46.css'/>">
<link rel="stylesheet" type="text/css" href="<c:url value='/css/table.css'/>">

<script type="text/javascript" src="<c:url value='/scripts/popup.js'/>"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/melanbide46/FixedColumnsTable.js"></script>

<script type="text/javascript">   
    function pulsarAltaPuestoCme(){
        var control = new Date();
        var result = null;
        if(navigator.appName.indexOf("Internet Explorer")!=-1){
            result = lanzarPopUpModal('<%=request.getContextPath()%>/PeticionModuloIntegracion.do?tarea=preparar&modulo=MELANBIDE46&operacion=cargarNuevoPuesto&tipo=0&numero=<%=numExpediente%>&control='+control.getTime(),560,980,'yes','no');
        }else{
            result = lanzarPopUpModal('<%=request.getContextPath()%>/PeticionModuloIntegracion.do?tarea=preparar&modulo=MELANBIDE46&operacion=cargarNuevoPuesto&tipo=0&numero=<%=numExpediente%>&control='+control.getTime(),620,980,'yes','no');
        }
        if (result != undefined){
            if(result[0] == '0'){
                recargarTablaPuestosCme(result);
            }
        }
    }
    
    function pulsarModificarPuestoCme(){
        var fila;
       /* var idTabla = document.getElementById('puestosCme').children[0].children[1].children[0].children[0].id;
        if(window.event) { //IE
            if(window.event.srcElement.tagName=='TD'){
                if(window.event.srcElement.parentElement.parentElement.parentElement.id==idTabla){
                    fila = window.event.srcElement.parentElement.rowIndex;
                }
            }else if(window.event.srcElement.id=='btnModificarPuestoCme'){
                fila = tabPuestosCme.selectedIndex;
            }
        }else{ // FF
            if(event.target.tagName=='TD'){
                if(event.target.parentElement.parentElement.parentElement.id==idTabla){
                    fila = event.target.parentNode.rowIndex;
                }
            }else if(window.event.srcElement.id=='btnModificarPuestoCme'){
                fila = tabPuestosCme.selectedIndex;
            }
        }*/
            
        //if(fila >= 0 && fila < listaPuestosCme.length) {
        if(tabPuestosCme.selectedIndex != -1) {
            fila = tabPuestosCme.selectedIndex;
            var control = new Date();
            var result = null;
            var opcion = '';
            if(document.forms[0].modoConsulta.value == "si"){
                opcion = 'consultar';
            }else{
                opcion = 'modificar';
            }
            if(navigator.appName.indexOf("Internet Explorer")!=-1){
                result = lanzarPopUpModal('<%=request.getContextPath()%>/PeticionModuloIntegracion.do?tarea=preparar&modulo=MELANBIDE46&operacion=cargarModificarPuesto&tipo=0&numero=<%=numExpediente%>&opcion='+opcion+'&idPuesto='+listaPuestosCme[fila][0]+'&control='+control.getTime(),560,980,'yes','no');
            }else{
                result = lanzarPopUpModal('<%=request.getContextPath()%>/PeticionModuloIntegracion.do?tarea=preparar&modulo=MELANBIDE46&operacion=cargarModificarPuesto&tipo=0&numero=<%=numExpediente%>&opcion='+opcion+'&idPuesto='+listaPuestosCme[fila][0]+'&control='+control.getTime(),620,980,'yes','no');
            }
            if (result != undefined){
                if(result[0] == '0'){
                    recargarTablaPuestosCme(result);
                }
            }
        }else{
                jsp_alerta('A', '<%=meLanbide46I18n.getMensaje(idiomaUsuario, "msg.msjNoSelecFila")%>');
            }
    }
    
    function pulsarEliminarPuestoCme(){
        

            if(tabPuestosCme.selectedIndex != -1) {
                var resultado = jsp_alerta('', '<%=meLanbide46I18n.getMensaje(idiomaUsuario, "msg.preguntaEliminar")%>');
                if (resultado == 1){
                    
                    document.getElementById('msgCargandoPuestoCme').style.display="none";
                    document.getElementById('msgEliminandoPuestoCme').style.display="inline";
                    barraProgresoCme('on', 'barraProgresoPuestosSolicitudCme');
                    var ajax = getXMLHttpRequest();
                    var nodos = null;
                    var CONTEXT_PATH = '<%=request.getContextPath()%>'
                    var url = CONTEXT_PATH + "/PeticionModuloIntegracion.do";
                    var parametros = "";
                    var control = new Date();
                    var listaPuestos = new Array();
                    parametros = 'tarea=preparar&modulo=MELANBIDE46&operacion=eliminarPuesto&tipo=0&numero=<%=numExpediente%>&idPuesto='+listaPuestosCme[tabPuestosCme.selectedIndex][0]+'&control='+control.getTime();
                    try{
                        ajax.open("POST",url,false);
                        ajax.setRequestHeader("Content-Type","application/x-www-form-urlencoded; charset=iso-8859-15");
                        ajax.setRequestHeader("Accept", "text/xml, application/xml, text/plain");     
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
                        listaPuestos = extraerListadoPuestosCme(nodos);
                        recargarTablaPuestosCme(listaPuestos);
                        var codigoOperacion = listaPuestos[0];
                        if(codigoOperacion=="0"){
                            jsp_alerta("A",'<%=meLanbide46I18n.getMensaje(idiomaUsuario,"msg.registroEliminadoOK")%>');
                        }else if(codigoOperacion=="1"){
                            jsp_alerta("A",'<%=meLanbide46I18n.getMensaje(idiomaUsuario,"error.errorEliminarBD")%>');
                        }else if(codigoOperacion=="2"){
                            jsp_alerta("A",'<%=meLanbide46I18n.getMensaje(idiomaUsuario,"error.errorEliminarGen")%>');
                        }else if(codigoOperacion=="3"){
                            jsp_alerta("A",'<%=meLanbide46I18n.getMensaje(idiomaUsuario,"error.pasoParametrosEliminar")%>');
                        }else{
                            jsp_alerta("A",'<%=meLanbide46I18n.getMensaje(idiomaUsuario,"error.errorEliminarGen")%>');
                        }//if(
                    }
                    catch(Err){
                        jsp_alerta("A",'<%=meLanbide46I18n.getMensaje(idiomaUsuario,"error.errorProcesarRespuesta")%>');
                    }//try-catch
                }
            } 
            else {
                jsp_alerta('A', '<%=meLanbide46I18n.getMensaje(idiomaUsuario, "msg.msjNoSelecFila")%>');
            }
            barraProgresoCme('off', 'barraProgresoPuestosSolicitudCme');
    }
    
    function recargarTablaPuestosCme(result){
        var fila;
        listaPuestosCme = new Array();
        listaPuestosCmeTabla = new Array();
        listaPuestosCmeTabla_titulos = new Array();
        listaPuestosCmeTabla_estilos = new Array();
        fila = result[1];
        recargarCalculosCme(fila);
        var titulas;
        
        for(var i = 2;i< result.length; i++){
            fila = result[i];
            
            titulas = fila[5][0] != null && fila[5][0] != '' ? fila[5][0].toUpperCase() : '';
            titulas += fila[5][1] != null && fila[5][1] != '' ? (titulas != null && titulas != '' ? '<br/>' : '') : '';
            titulas += fila[5][1] != null && fila[5][1] != '' ? fila[5][1].toUpperCase() : '';
            titulas += fila[5][2] != null && fila[5][2] != '' ? (titulas != null && titulas != '' ? '<br/>' : '') : '';
            titulas += fila[5][2] != null && fila[5][2] != '' ? fila[5][2].toUpperCase() : '';
            
            if(titulas == null || titulas == ''){
                    titulas = "-";
                }
                if(titulas.length > 16){
                    if(fila[4][0] != null && fila[4][0] != ''){
                        titulas = fila[4][0].toUpperCase();
                    }else if(fila[4][1] != null && fila[4][1] != ''){
                        titulas = fila[4][1].toUpperCase();
                    }else if(fila[4][2] != null && fila[4][2] != ''){
                        titulas = fila[4][2].toUpperCase();
                    }
                    if(titulas.length > 16){
                        titulas = titulas.substring(0, 16);
                    }
                    titulas = titulas+"...";
                }
            
            listaPuestosCme[i-2] = fila;
            listaPuestosCmeTabla[i-2] = [fila[1], fila[2], fila[3], fila[4], titulas, fila[6], fila[7], fila[8], fila[9], fila[10], fila[11], fila[12], fila[13], fila[14], fila[15], fila[16], fila[17], fila[18], fila[19], fila[20], fila[21], fila[22], fila[23], fila[24], fila[25], fila[26], fila[27]];
            listaPuestosCmeTabla_titulos[i-2] = [fila[1], fila[2], fila[3], fila[4], titulas, fila[6], fila[7], fila[8], fila[9], fila[10], fila[11], fila[12], fila[13], fila[14], fila[15], fila[16], fila[17], fila[18], fila[19], fila[20], fila[21], fila[22], fila[23], fila[24], fila[25], fila[26], fila[27]];
        }

        //tabPuestosCme = new Tabla(document.getElementById('puestosCme'), 890);
        tabPuestosCme = new FixedColumnTable(document.getElementById('puestosCme'), 850, 876, 'puestosCme');
        tabPuestosCme.addColumna('180','left',"<%= meLanbide46I18n.getMensaje(idiomaUsuario,"label.solicitud.tabla.puestos.col1")%>", "<%= meLanbide46I18n.getMensaje(idiomaUsuario,"label.solicitud.tabla.puestos.col1.title")%>");
        tabPuestosCme.addColumna('130','left',"<%= meLanbide46I18n.getMensaje(idiomaUsuario,"label.solicitud.tabla.puestos.col2")%>", "<%= meLanbide46I18n.getMensaje(idiomaUsuario,"label.solicitud.tabla.puestos.col2.title")%>");    
        tabPuestosCme.addColumna('130','left',"<%= meLanbide46I18n.getMensaje(idiomaUsuario,"label.solicitud.tabla.puestos.col3")%>", "<%= meLanbide46I18n.getMensaje(idiomaUsuario,"label.solicitud.tabla.puestos.col3.title")%>");    
        tabPuestosCme.addColumna('0','left',"<%= meLanbide46I18n.getMensaje(idiomaUsuario,"label.solicitud.tabla.puestos.col4")%>", "<%= meLanbide46I18n.getMensaje(idiomaUsuario,"label.solicitud.tabla.puestos.col4.title")%>");   
        tabPuestosCme.addColumna('140','left',"<%= meLanbide46I18n.getMensaje(idiomaUsuario,"label.solicitud.tabla.puestos.col5")%>", "<%= meLanbide46I18n.getMensaje(idiomaUsuario,"label.solicitud.tabla.puestos.col5.title")%>");   
        tabPuestosCme.addColumna('80','right',"<%= meLanbide46I18n.getMensaje(idiomaUsuario,"label.solicitud.tabla.puestos.col6")%>", "<%= meLanbide46I18n.getMensaje(idiomaUsuario,"label.solicitud.tabla.puestos.col6.title")%>");   
        tabPuestosCme.addColumna('100','right',"<%= meLanbide46I18n.getMensaje(idiomaUsuario,"label.solicitud.tabla.puestos.col7")%>", "<%= meLanbide46I18n.getMensaje(idiomaUsuario,"label.solicitud.tabla.puestos.col7.title")%>");  
        tabPuestosCme.addColumna('130','left',"<%= meLanbide46I18n.getMensaje(idiomaUsuario,"label.solicitud.tabla.puestos.col8")%>", "<%= meLanbide46I18n.getMensaje(idiomaUsuario,"label.solicitud.tabla.puestos.col8.title")%>");     
        tabPuestosCme.addColumna('130','left',"<%= meLanbide46I18n.getMensaje(idiomaUsuario,"label.solicitud.tabla.puestos.col9")%>", "<%= meLanbide46I18n.getMensaje(idiomaUsuario,"label.solicitud.tabla.puestos.col9.title")%>");     
        tabPuestosCme.addColumna('200','left',"<%= meLanbide46I18n.getMensaje(idiomaUsuario,"label.solicitud.tabla.puestos.col10")%>", "<%= meLanbide46I18n.getMensaje(idiomaUsuario,"label.solicitud.tabla.puestos.col10.title")%>");     

        tabPuestosCme.numColumnasFijas = 1;
        
        for(var cont = 0; cont < listaPuestosCmeTabla.length; cont++){
            tabPuestosCme.addFilaConFormato(listaPuestosCmeTabla[cont], listaPuestosCmeTabla_titulos[cont], listaPuestosCmeTabla_estilos[cont]);
        }

        tabPuestosCme.displayCabecera=true;
        tabPuestosCme.height = 146;
    
        tabPuestosCme.altoCabecera = 50;

        tabPuestosCme.scrollWidth = 1330;

        tabPuestosCme.dblClkFunction = 'dblClckTablaPuestosCme';

        tabPuestosCme.displayTabla();

        tabPuestosCme.pack();
        
        actualizarOtrasPestanas('justif');
    }
    
    function extraerListadoPuestosCme(nodos){
            var elemento = nodos[0];
            var hijos = elemento.childNodes;
            var codigoOperacion = null;
            var listaPuestos = new Array();
            var fila = new Array();
            var nodoFila;
            var hijosFila;
            var nodoCalculos;
            var hijosCalculos;
            var nodoCampo;
            var filaTitulaciones = new Array();
            var j;
            
            for(j=0;hijos!=null && j<hijos.length;j++){
                if(hijos[j].nodeName=="CODIGO_OPERACION"){                            
                    codigoOperacion = hijos[j].childNodes[0].nodeValue;
                    listaPuestos[j] = codigoOperacion;
                }//if(hijos[j].nodeName=="CODIGO_OPERACION")                      
                else if(hijos[j].nodeName=="CALCULOS"){
                    nodoCalculos = hijos[j];
                    hijosCalculos = nodoCalculos.childNodes;
                    for(var cont = 0; cont < hijosCalculos.length; cont++){
                        if(hijosCalculos[cont].nodeName=="IMP_SOL"){
                            nodoCampo = hijosCalculos[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[0] = nodoCampo.childNodes[0].nodeValue;
                            }
                            else{
                                fila[0] = '-';
                            }
                        }
                        else if(hijosCalculos[cont].nodeName=="IMP_CONV"){
                            nodoCampo = hijosCalculos[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[1] = nodoCampo.childNodes[0].nodeValue;
                            }
                            else{
                                fila[1] = '-';
                            }
                        }
                        else if(hijosCalculos[cont].nodeName=="IMP_PREV_CON"){
                            nodoCampo = hijosCalculos[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[2] = nodoCampo.childNodes[0].nodeValue;
                            }
                            else{
                                fila[2] = '-';
                            }
                        }
                        else if(hijosCalculos[cont].nodeName=="IMP_CON"){
                            nodoCampo = hijosCalculos[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[3] = nodoCampo.childNodes[0].nodeValue;
                            }
                            else{
                                fila[3] = '-';
                            }
                        }
                        else if(hijosCalculos[cont].nodeName=="IMP_JUS"){
                            nodoCampo = hijosCalculos[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[4] = nodoCampo.childNodes[0].nodeValue;
                            }
                            else{
                                fila[4] = '-';
                            }
                        }
                        else if(hijosCalculos[cont].nodeName=="IMP_REN"){
                            nodoCampo = hijosCalculos[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[5] = nodoCampo.childNodes[0].nodeValue;
                            }
                            else{
                                fila[5] = '-';
                            }
                        }
                        else if(hijosCalculos[cont].nodeName=="IMP_PAG"){
                            nodoCampo = hijosCalculos[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[6] = nodoCampo.childNodes[0].nodeValue;
                            }
                            else{
                                fila[6] = '-';
                            }
                        }
                        else if(hijosCalculos[cont].nodeName=="IMP_PAG_2"){
                            nodoCampo = hijosCalculos[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[7] = nodoCampo.childNodes[0].nodeValue;
                            }
                            else{
                                fila[7] = '-';
                            }
                        }
                        else if(hijosCalculos[cont].nodeName=="IMP_REI"){
                            nodoCampo = hijosCalculos[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[8] = nodoCampo.childNodes[0].nodeValue;
                            }
                            else{
                                fila[8] = '-';
                            }
                        }
                        else if(hijosCalculos[cont].nodeName=="OTRAS_AYUDAS_SOLIC"){
                            nodoCampo = hijosCalculos[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[9] = nodoCampo.childNodes[0].nodeValue;
                            }
                            else{
                                fila[9] = '-';
                            }
                        }
                        else if(hijosCalculos[cont].nodeName=="OTRAS_AYUDAS_CONCE"){
                            nodoCampo = hijosCalculos[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[10] = nodoCampo.childNodes[0].nodeValue;
                            }
                            else{
                                fila[10] = '-';
                            }
                        }
                        else if(hijosCalculos[cont].nodeName=="MINIMIS_SOLIC"){
                            nodoCampo = hijosCalculos[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[11] = nodoCampo.childNodes[0].nodeValue;
                            }
                            else{
                                fila[11] = '-';
                            }
                        }
                        else if(hijosCalculos[cont].nodeName=="MINIMIS_CONCE"){
                            nodoCampo = hijosCalculos[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[12] = nodoCampo.childNodes[0].nodeValue;
                            }
                            else{
                                fila[12] = '-';
                            }
                        }
                        else if(hijosCalculos[cont].nodeName=="IMP_DESP"){
                            nodoCampo = hijosCalculos[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[13] = nodoCampo.childNodes[0].nodeValue;
                            }
                            else{
                                fila[13] = '-';
                            }
                        }
                        else if(hijosCalculos[cont].nodeName=="IMP_BAJA"){
                            nodoCampo = hijosCalculos[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[14] = nodoCampo.childNodes[0].nodeValue;
                            }
                            else{
                                fila[14] = '-';
                            }
                        }
                        else if(hijosCalculos[cont].nodeName=="CONCEDIDO_REAL"){
                            nodoCampo = hijosCalculos[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[15] = nodoCampo.childNodes[0].nodeValue;
                            }
                            else{
                                fila[15] = '-';
                            }
                        }
                        else if(hijosCalculos[cont].nodeName=="PAGADO_REAL"){
                            nodoCampo = hijosCalculos[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[16] = nodoCampo.childNodes[0].nodeValue;
                            }
                            else{
                                fila[16] = '-';
                            }
                        }
                        else if(hijosCalculos[cont].nodeName=="PAGADO_REAL_2"){
                            nodoCampo = hijosCalculos[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[17] = nodoCampo.childNodes[0].nodeValue;
                            }
                            else{
                                fila[17] = '-';
                            }
                        }
                        else if(hijosCalculos[cont].nodeName=="IMP_NO_JUS"){
                            nodoCampo = hijosCalculos[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[18] = nodoCampo.childNodes[0].nodeValue;
                            }
                            else{
                                fila[18] = '-';
                            }
                        }
                        else if(hijosCalculos[cont].nodeName=="IMP_REN_RES"){
                            nodoCampo = hijosCalculos[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[19] = nodoCampo.childNodes[0].nodeValue;
                            }
                            else{
                                fila[19] = '-';
                            }
                        }
                        else if(hijosCalculos[cont].nodeName=="IMP_PAG_RES"){
                            nodoCampo = hijosCalculos[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[20] = nodoCampo.childNodes[0].nodeValue;
                            }
                            else{
                                fila[20] = '-';
                            }
                        }
                    }
                    listaPuestos[j] = fila;
                    fila = new Array();  
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
                        }
                        else if(hijosFila[cont].nodeName=="DESC_PUESTO"){
                            nodoCampo = hijosFila[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[1] = nodoCampo.childNodes[0].nodeValue;
                            }
                            else{
                                fila[1] = '-';
                            }
                        }
                        else if(hijosFila[cont].nodeName=="RANGO_EDAD"){
                            nodoCampo = hijosFila[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[2] = nodoCampo.childNodes[0].nodeValue;
                            }
                            else{
                                fila[2] = '-';
                            }
                        }
                        else if(hijosFila[cont].nodeName=="PAIS"){
                            nodoCampo = hijosFila[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[3] = nodoCampo.childNodes[0].nodeValue;
                            }
                            else{
                                fila[3] = '-';
                            }
                        }
                        else if(hijosFila[cont].nodeName=="NIVEL_FORMATIVO"){
                            nodoCampo = hijosFila[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[4] = nodoCampo.childNodes[0].nodeValue;
                            }
                            else{
                                fila[4] = '-';
                            }
                        }
                        else if(hijosFila[cont].nodeName=="TITULACION_1"){
                            nodoCampo = hijosFila[cont];
                            if(nodoCampo.childNodes.length > 0){
                                filaTitulaciones[0] = nodoCampo.childNodes[0].nodeValue;
                            }
                            else{
                                filaTitulaciones[0] = '';
                            }
                        }
                        else if(hijosFila[cont].nodeName=="TITULACION_2"){
                            nodoCampo = hijosFila[cont];
                            if(nodoCampo.childNodes.length > 0){
                                filaTitulaciones[1] = nodoCampo.childNodes[0].nodeValue;
                            }
                            else{
                                filaTitulaciones[1] = '';
                            }
                        }
                        else if(hijosFila[cont].nodeName=="TITULACION_3"){
                            nodoCampo = hijosFila[cont];
                            if(nodoCampo.childNodes.length > 0){
                                filaTitulaciones[2] = nodoCampo.childNodes[0].nodeValue;
                            }
                            else{
                                filaTitulaciones[2] = '';
                            }
                            fila[5] = filaTitulaciones;
                        }
                        else if(hijosFila[cont].nodeName=="SUBV_SOLIC"){
                            nodoCampo = hijosFila[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[6] = nodoCampo.childNodes[0].nodeValue;
                            }
                            else{
                                fila[6] = '-';
                            }
                        }
                        else if(hijosFila[cont].nodeName=="SUBV_APROB"){
                            nodoCampo = hijosFila[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[7] = nodoCampo.childNodes[0].nodeValue;
                            }
                            else{
                                fila[7] = '-';
                            }
                        }
                        else if(hijosFila[cont].nodeName=="RESULTADO"){
                            nodoCampo = hijosFila[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[8] = nodoCampo.childNodes[0].nodeValue;
                            }
                            else{
                                fila[8] = '-';
                            }
                        }
                        else if(hijosFila[cont].nodeName=="MOTIVO"){
                            nodoCampo = hijosFila[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[9] = nodoCampo.childNodes[0].nodeValue;
                            }
                            else{
                                fila[9] = '-';
                            }
                        }
                        else if(hijosFila[cont].nodeName=="OBSERVACIONES"){
                            nodoCampo = hijosFila[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[10] = nodoCampo.childNodes[0].nodeValue;
                            }
                            else{
                                fila[10] = '-';
                            }
                        }
                    }
                    listaPuestos[j] = fila;
                    filaTitulaciones = new Array();
                    fila = new Array();   
                }
        } 
        return listaPuestos;
    }
    
    function dblClckTablaPuestosCme(rowID,tableName){
        pulsarModificarPuestoCme();
    }
</script>

<body>
    <div id="barraProgresoPuestosSolicitudCme" style="position: absolute; z-index:10; visibility: hidden; top: 30%; left: 30%;">
        <table width="100%" border="0px" cellpadding="0px" cellspacing="0px" border="0px">
            <tr>
                <td align="center" valign="middle">
                    <table class="contenedorHidepage" cellpadding="0px" cellspacing="0px" border="0px">
                        <tr>
                            <td>
                                <table width="349px" height="100%">
                                    <tr>
                                        <td colspan="3" style="height:70%;text-align:center;valign:middle;">
                                            <span id="msgCargandoPuestoCme">
                                                <%=meLanbide46I18n.getMensaje(idiomaUsuario, "msg.cargandoDatos")%>
                                            </span>
                                            <span id="msgEliminandoPuestoCme">
                                                <%=meLanbide46I18n.getMensaje(idiomaUsuario, "msg.eliminandoDatos")%>
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
    <div style="clear: both;">
        <div>
            <div id="puestosCme" style="padding: 5px; width:876px; height: 159px; text-align: center; margin:0px;margin-top:0px;" align="center"></div>
            <div class="botonera">
                <input type="button" id="btnNuevoPuestoCme" name="btnNuevoPuestoCme" class="botonGeneral" value="<%=meLanbide46I18n.getMensaje(idiomaUsuario, "btn.alta")%>" onclick="pulsarAltaPuestoCme();">
                <input type="button" id="btnModificarPuestoCme" name="btnModificarPuestoCme" class="botonGeneral" value="<%=meLanbide46I18n.getMensaje(idiomaUsuario, "btn.modificar")%>" onclick="pulsarModificarPuestoCme();">
                <input type="button" id="btnEliminaPuestoCme" name="btnEliminaPuestoCme" class="botonGeneral" value="<%=meLanbide46I18n.getMensaje(idiomaUsuario, "btn.eliminar")%>" onclick="pulsarEliminarPuestoCme();">
            </div>
        </div>
    </div>
</body>

<script type="text/javascript">   
    var tabPuestosCme;
    var listaPuestosCme = new Array();
    var listaPuestosCmeTabla = new Array();
    var listaPuestosCmeTabla_titulos = new Array();
    var listaPuestosCmeTabla_estilos = new Array();

    //tabPuestosCme = new Tabla(document.getElementById('puestosCme'), 890);
    tabPuestosCme = new FixedColumnTable(document.getElementById('puestosCme'), 850, 876, 'puestosCme');
    tabPuestosCme.addColumna('180','left',"<%= meLanbide46I18n.getMensaje(idiomaUsuario,"label.solicitud.tabla.puestos.col1")%>", "<%= meLanbide46I18n.getMensaje(idiomaUsuario,"label.solicitud.tabla.puestos.col1.title")%>");
    tabPuestosCme.addColumna('130','left',"<%= meLanbide46I18n.getMensaje(idiomaUsuario,"label.solicitud.tabla.puestos.col2")%>", "<%= meLanbide46I18n.getMensaje(idiomaUsuario,"label.solicitud.tabla.puestos.col2.title")%>");    
    tabPuestosCme.addColumna('130','left',"<%= meLanbide46I18n.getMensaje(idiomaUsuario,"label.solicitud.tabla.puestos.col3")%>", "<%= meLanbide46I18n.getMensaje(idiomaUsuario,"label.solicitud.tabla.puestos.col3.title")%>");    
    tabPuestosCme.addColumna('0','left',"<%= meLanbide46I18n.getMensaje(idiomaUsuario,"label.solicitud.tabla.puestos.col4")%>", "<%= meLanbide46I18n.getMensaje(idiomaUsuario,"label.solicitud.tabla.puestos.col4.title")%>");   
    tabPuestosCme.addColumna('140','left',"<%= meLanbide46I18n.getMensaje(idiomaUsuario,"label.solicitud.tabla.puestos.col5")%>", "<%= meLanbide46I18n.getMensaje(idiomaUsuario,"label.solicitud.tabla.puestos.col5.title")%>");   
    tabPuestosCme.addColumna('80','right',"<%= meLanbide46I18n.getMensaje(idiomaUsuario,"label.solicitud.tabla.puestos.col6")%>", "<%= meLanbide46I18n.getMensaje(idiomaUsuario,"label.solicitud.tabla.puestos.col6.title")%>");   
    tabPuestosCme.addColumna('100','right',"<%= meLanbide46I18n.getMensaje(idiomaUsuario,"label.solicitud.tabla.puestos.col7")%>", "<%= meLanbide46I18n.getMensaje(idiomaUsuario,"label.solicitud.tabla.puestos.col7.title")%>");   
    tabPuestosCme.addColumna('130','left',"<%= meLanbide46I18n.getMensaje(idiomaUsuario,"label.solicitud.tabla.puestos.col8")%>", "<%= meLanbide46I18n.getMensaje(idiomaUsuario,"label.solicitud.tabla.puestos.col8.title")%>");     
    tabPuestosCme.addColumna('130','left',"<%= meLanbide46I18n.getMensaje(idiomaUsuario,"label.solicitud.tabla.puestos.col9")%>", "<%= meLanbide46I18n.getMensaje(idiomaUsuario,"label.solicitud.tabla.puestos.col9.title")%>");     
    tabPuestosCme.addColumna('200','left',"<%= meLanbide46I18n.getMensaje(idiomaUsuario,"label.solicitud.tabla.puestos.col10")%>", "<%= meLanbide46I18n.getMensaje(idiomaUsuario,"label.solicitud.tabla.puestos.col10.title")%>");     
    
    tabPuestosCme.numColumnasFijas = 1;

    tabPuestosCme.displayCabecera=true;
    
    var listaTitulaciones = new Array();
    
    <%  		
        FilaPuestoVO voP = null;
        List<FilaPuestoVO> listaPuestos = (List<FilaPuestoVO>)request.getAttribute("puestos");													
        if (listaPuestos != null && listaPuestos.size() >0){
            for(int i = 0;i < listaPuestos.size();i++)
            {
                voP = listaPuestos.get(i);
                
                
                String titulacion = voP.getTitulacion1() != null && !voP.getTitulacion1().equals("") ? voP.getTitulacion1().toUpperCase() : "";
                titulacion += voP.getTitulacion2() != null && !voP.getTitulacion2().equals("") ? (titulacion != null && !titulacion.equals("") ? "<br/>" : "") : "";
                titulacion += voP.getTitulacion2() != null && !voP.getTitulacion2().equals("") ? voP.getTitulacion2().toUpperCase() : "";
                titulacion += voP.getTitulacion3() != null && !voP.getTitulacion3().equals("") ? (titulacion != null && !titulacion.equals("") ? "<br/>" : "") : "";
                titulacion += voP.getTitulacion3() != null && !voP.getTitulacion3().equals("") ? voP.getTitulacion3().toUpperCase() : "";
                
                if(titulacion == null || titulacion.equals("")){
                    titulacion = "-";
                }
                if(titulacion.length() > 16){
                    if(voP.getTitulacion1() != null && !voP.getTitulacion1().equals("")){
                        titulacion = voP.getTitulacion1().toUpperCase() ;
                    }
                    else if(voP.getTitulacion2() != null && !voP.getTitulacion2().equals("")){
                        titulacion = voP.getTitulacion2().toUpperCase() ;
                    }
                    else if(voP.getTitulacion3() != null && !voP.getTitulacion3().equals("")){
                        titulacion = voP.getTitulacion3().toUpperCase() ;
                    }
                    if(titulacion.length() > 16){
                        titulacion = titulacion.substring(0, 16);
                    }
                    titulacion = titulacion+"...";
                }
    %>
        listaTitulaciones = new Array();
        listaTitulaciones[0] = '<%=voP.getTitulacion1()%>';
        listaTitulaciones[1] = '<%=voP.getTitulacion2()%>';
        listaTitulaciones[2] = '<%=voP.getTitulacion3()%>';
        listaPuestosCme[<%=i%>] = ['<%=voP.getCodPuesto()%>','<%=voP.getDescPuesto()%>', '<%=voP.getRangoEdad()%>', '<%=voP.getPais()%>', '<%=voP.getNivelFor()%>', '<%=titulacion%>', '<%=voP.getSubvSolic()%>', '<%=voP.getSubvAprob()%>', '<%=voP.getSubvAprob()%>', '<%=voP.getResultado()%>', '<%=voP.getMotivo()%>', '<%=voP.getObservaciones()%>'];
        listaPuestosCmeTabla[<%=i%>] = ['<%=voP.getDescPuesto()%>', '<%=voP.getRangoEdad()%>', '<%=voP.getPais()%>', '<%=voP.getNivelFor()%>', '<%=titulacion%>', '<%=voP.getSubvSolic()%>', '<%=voP.getSubvAprob()%>', '<%=voP.getResultado()%>', '<%=voP.getMotivo()%>', '<%=voP.getObservaciones()%>'];
        listaPuestosCmeTabla_titulos[<%=i%>] = ['<%=voP.getDescPuesto()%>', '<%=voP.getRangoEdad()%>', '<%=voP.getPais()%>', '<%=voP.getNivelFor()%>', getListAsTextCme(listaTitulaciones), '<%=voP.getSubvSolic()%>', '<%=voP.getSubvAprob()%>', '<%=voP.getResultado()%>', '<%=voP.getMotivo()%>', '<%=voP.getObservaciones()%>'];
    <%
            }// for
        }// if
    %>
    
    for(var cont = 0; cont < listaPuestosCmeTabla.length; cont++){
        tabPuestosCme.addFilaConFormato(listaPuestosCmeTabla[cont], listaPuestosCmeTabla_titulos[cont], listaPuestosCmeTabla_estilos[cont])
    }
    
    tabPuestosCme.height = '146';
    
    tabPuestosCme.altoCabecera = 50;
    
    tabPuestosCme.scrollWidth = 1330;
    
    tabPuestosCme.dblClkFunction = 'dblClckTablaPuestosCme';
    
    tabPuestosCme.displayTabla();
    
    tabPuestosCme.pack();
</script>
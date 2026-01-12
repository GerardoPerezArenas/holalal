<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>

<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide39.i18n.MeLanbide39I18n" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide39.vo.FilaPuestoVO" %>
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
    MeLanbide39I18n meLanbide39I18n = MeLanbide39I18n.getInstance();
    
    Config m_Config = ConfigServiceHelper.getConfig("common");
    String statusBar = m_Config.getString("JSP.StatusBar");
    String nombreModulo     = request.getParameter("nombreModulo");
    String codOrganizacion  = request.getParameter("codOrganizacionModulo");
    String numExpediente    = request.getParameter("numero");
    String mensajeProgreso = "";
%>

<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/estilo.css"/>
<link rel="stylesheet" type="text/css" href="<c:url value='/css/extension/melanbide39/melanbide39.css'/>">
<link rel="stylesheet" type="text/css" href="<c:url value='/css/table.css'/>">

<script type="text/javascript" src="<c:url value='/scripts/popup.js'/>"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/melanbide39/FixedColumnsTable.js"></script>

<script type="text/javascript">   
    function pulsarAltaPuestoCpe(){
        var control = new Date();
        var result = null;
        if(navigator.appName.indexOf("Internet Explorer")!=-1){
            result = lanzarPopUpModal('<%=request.getContextPath()%>/PeticionModuloIntegracion.do?tarea=preparar&modulo=MELANBIDE39&operacion=cargarNuevoPuesto&tipo=0&numero=<%=numExpediente%>&control='+control.getTime(),560,980,'yes','no');
        }else{
            result = lanzarPopUpModal('<%=request.getContextPath()%>/PeticionModuloIntegracion.do?tarea=preparar&modulo=MELANBIDE39&operacion=cargarNuevoPuesto&tipo=0&numero=<%=numExpediente%>&control='+control.getTime(),620,980,'yes','no');
        }
        if (result != undefined){
            if(result[0] == '0'){
                recargarTablaPuestosCpe(result);
            }
        }
    }
    
    function pulsarModificarPuestoCpe(){
        var fila;
       /* var idTabla = document.getElementById('puestosCpe').children[0].children[1].children[0].children[0].id;
        if(window.event) { //IE
            if(window.event.srcElement.tagName=='TD'){
                if(window.event.srcElement.parentElement.parentElement.parentElement.id==idTabla){
                    fila = window.event.srcElement.parentElement.rowIndex;
                }
            }else if(window.event.srcElement.id=='btnModificarPuestoCpe'){
                fila = tabPuestosCpe.selectedIndex;
            }
        }else{ // FF
            if(event.target.tagName=='TD'){
                if(event.target.parentElement.parentElement.parentElement.id==idTabla){
                    fila = event.target.parentNode.rowIndex;
                }
            }else if(window.event.srcElement.id=='btnModificarPuestoCpe'){
                fila = tabPuestosCpe.selectedIndex;
            }
        }*/
            
        //if(fila >= 0 && fila < listaPuestosCpe.length) {
        if(tabPuestosCpe.selectedIndex != -1) {
            fila = tabPuestosCpe.selectedIndex;
            var control = new Date();
            var result = null;
            var opcion = '';
            if(document.forms[0].modoConsulta.value == "si"){
                opcion = 'consultar';
            }else{
                opcion = 'modificar';
            }
            if(navigator.appName.indexOf("Internet Explorer")!=-1){
                result = lanzarPopUpModal('<%=request.getContextPath()%>/PeticionModuloIntegracion.do?tarea=preparar&modulo=MELANBIDE39&operacion=cargarModificarPuesto&tipo=0&numero=<%=numExpediente%>&opcion='+opcion+'&idPuesto='+listaPuestosCpe[fila][0]+'&control='+control.getTime(),560,980,'yes','no');
            }else{
                result = lanzarPopUpModal('<%=request.getContextPath()%>/PeticionModuloIntegracion.do?tarea=preparar&modulo=MELANBIDE39&operacion=cargarModificarPuesto&tipo=0&numero=<%=numExpediente%>&opcion='+opcion+'&idPuesto='+listaPuestosCpe[fila][0]+'&control='+control.getTime(),620,980,'yes','no');
            }
            if (result != undefined){
                if(result[0] == '0'){
                    recargarTablaPuestosCpe(result);
                }
            }
        }else{
                jsp_alerta('A', '<%=meLanbide39I18n.getMensaje(idiomaUsuario, "msg.msjNoSelecFila")%>');
            }
    }
    
    function pulsarEliminarPuestoCpe(){
        

            if(tabPuestosCpe.selectedIndex != -1) {
                var resultado = jsp_alerta('', '<%=meLanbide39I18n.getMensaje(idiomaUsuario, "msg.preguntaEliminar")%>');
                if (resultado == 1){
                    
                    document.getElementById('msgCargandoPuestoCpe').style.display="none";
                    document.getElementById('msgEliminandoPuestoCpe').style.display="inline";
                    barraProgresoCpe('on', 'barraProgresoPuestosSolicitudCpe');
                    var ajax = getXMLHttpRequest();
                    var nodos = null;
                    var CONTEXT_PATH = '<%=request.getContextPath()%>'
                    var url = CONTEXT_PATH + "/PeticionModuloIntegracion.do";
                    var parametros = "";
                    var control = new Date();
                    var listaPuestos = new Array();
                    parametros = 'tarea=preparar&modulo=MELANBIDE39&operacion=eliminarPuesto&tipo=0&numero=<%=numExpediente%>&idPuesto='+listaPuestosCpe[tabPuestosCpe.selectedIndex][0]+'&control='+control.getTime();
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
                        listaPuestos = extraerListadoPuestosCpe(nodos);
                        recargarTablaPuestosCpe(listaPuestos);
                        var codigoOperacion = listaPuestos[0];
                        if(codigoOperacion=="0"){
                            jsp_alerta("A",'<%=meLanbide39I18n.getMensaje(idiomaUsuario,"msg.registroEliminadoOK")%>');
                        }else if(codigoOperacion=="1"){
                            jsp_alerta("A",'<%=meLanbide39I18n.getMensaje(idiomaUsuario,"error.errorEliminarBD")%>');
                        }else if(codigoOperacion=="2"){
                            jsp_alerta("A",'<%=meLanbide39I18n.getMensaje(idiomaUsuario,"error.errorEliminarGen")%>');
                        }else if(codigoOperacion=="3"){
                            jsp_alerta("A",'<%=meLanbide39I18n.getMensaje(idiomaUsuario,"error.pasoParametrosEliminar")%>');
                        }else{
                            jsp_alerta("A",'<%=meLanbide39I18n.getMensaje(idiomaUsuario,"error.errorEliminarGen")%>');
                        }//if(
                    }
                    catch(Err){
                        jsp_alerta("A",'<%=meLanbide39I18n.getMensaje(idiomaUsuario,"error.errorProcesarRespuesta")%>');
                    }//try-catch
                }
            } 
            else {
                jsp_alerta('A', '<%=meLanbide39I18n.getMensaje(idiomaUsuario, "msg.msjNoSelecFila")%>');
            }
            barraProgresoCpe('off', 'barraProgresoPuestosSolicitudCpe');
    }
    
    function recargarTablaPuestosCpe(result){
        var fila;
        listaPuestosCpe = new Array();
        listaPuestosCpeTabla = new Array();
        listaPuestosCpeTabla_titulos = new Array();
        listaPuestosCpeTabla_estilos = new Array();
        fila = result[1];
        recargarCalculosCpe(fila);
        var titulas;
        
        for(var i = 2;i< result.length; i++){
            fila = result[i];
            
            titulas = fila[4][0] != null && fila[4][0] != '' ? fila[4][0].toUpperCase() : '';
            titulas += fila[4][1] != null && fila[4][1] != '' ? (titulas != null && titulas != '' ? '<br/>' : '') : '';
            titulas += fila[4][1] != null && fila[4][1] != '' ? fila[4][1].toUpperCase() : '';
            titulas += fila[4][2] != null && fila[4][2] != '' ? (titulas != null && titulas != '' ? '<br/>' : '') : '';
            titulas += fila[4][2] != null && fila[4][2] != '' ? fila[4][2].toUpperCase() : '';
            
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
            
            listaPuestosCpe[i-2] = fila;
            listaPuestosCpeTabla[i-2] = [fila[1], fila[2], fila[3], titulas, fila[5], fila[6], fila[7], fila[8], fila[9], fila[10], fila[11], fila[12], fila[13], fila[14], fila[15], fila[16], fila[17], fila[18], fila[19], fila[20], fila[21], fila[22], fila[23], fila[24], fila[25], fila[26], fila[27]];
            listaPuestosCpeTabla_titulos[i-2] = [fila[1], fila[2], fila[3], getListAsTextCpe(fila[4]), fila[5], fila[6], fila[7], fila[8], fila[9], fila[10], fila[11], fila[12], fila[13], fila[14], fila[15], fila[16], fila[17], fila[18], fila[19], fila[20], fila[21], fila[22], fila[23], fila[24], fila[25], fila[26], fila[27]];
        }

        //tabPuestosCpe = new Tabla(document.getElementById('puestosCpe'), 890);
        tabPuestosCpe = new FixedColumnTable(document.getElementById('puestosCpe'), 850, 876, 'puestosCpe');
        tabPuestosCpe.addColumna('180','left',"<%= meLanbide39I18n.getMensaje(idiomaUsuario,"label.solicitud.tabla.puestos.col1")%>", "<%= meLanbide39I18n.getMensaje(idiomaUsuario,"label.solicitud.tabla.puestos.col1.title")%>");
        tabPuestosCpe.addColumna('130','left',"<%= meLanbide39I18n.getMensaje(idiomaUsuario,"label.solicitud.tabla.puestos.col2")%>", "<%= meLanbide39I18n.getMensaje(idiomaUsuario,"label.solicitud.tabla.puestos.col2.title")%>");    
        tabPuestosCpe.addColumna('80','left',"<%= meLanbide39I18n.getMensaje(idiomaUsuario,"label.solicitud.tabla.puestos.col3")%>", "<%= meLanbide39I18n.getMensaje(idiomaUsuario,"label.solicitud.tabla.puestos.col3.title")%>");   
        tabPuestosCpe.addColumna('140','left',"<%= meLanbide39I18n.getMensaje(idiomaUsuario,"label.solicitud.tabla.puestos.col4")%>", "<%= meLanbide39I18n.getMensaje(idiomaUsuario,"label.solicitud.tabla.puestos.col4.title")%>");   
        tabPuestosCpe.addColumna('80','right',"<%= meLanbide39I18n.getMensaje(idiomaUsuario,"label.solicitud.tabla.puestos.col5")%>", "<%= meLanbide39I18n.getMensaje(idiomaUsuario,"label.solicitud.tabla.puestos.col5.title")%>");   
        tabPuestosCpe.addColumna('100','right',"<%= meLanbide39I18n.getMensaje(idiomaUsuario,"label.solicitud.tabla.puestos.col6")%>", "<%= meLanbide39I18n.getMensaje(idiomaUsuario,"label.solicitud.tabla.puestos.col6.title")%>");  
        tabPuestosCpe.addColumna('130','left',"<%= meLanbide39I18n.getMensaje(idiomaUsuario,"label.solicitud.tabla.puestos.col7")%>", "<%= meLanbide39I18n.getMensaje(idiomaUsuario,"label.solicitud.tabla.puestos.col7.title")%>");   
        tabPuestosCpe.addColumna('130','left',"<%= meLanbide39I18n.getMensaje(idiomaUsuario,"label.solicitud.tabla.puestos.col8")%>", "<%= meLanbide39I18n.getMensaje(idiomaUsuario,"label.solicitud.tabla.puestos.col8.title")%>");     
        tabPuestosCpe.addColumna('200','left',"<%= meLanbide39I18n.getMensaje(idiomaUsuario,"label.solicitud.tabla.puestos.col9")%>", "<%= meLanbide39I18n.getMensaje(idiomaUsuario,"label.solicitud.tabla.puestos.col9.title")%>");     

        tabPuestosCpe.numColumnasFijas = 1;
        
        for(var cont = 0; cont < listaPuestosCpeTabla.length; cont++){
            tabPuestosCpe.addFilaConFormato(listaPuestosCpeTabla[cont], listaPuestosCpeTabla_titulos[cont], listaPuestosCpeTabla_estilos[cont])
        }

        tabPuestosCpe.displayCabecera=true;
        tabPuestosCpe.height = 146;
    
        tabPuestosCpe.altoCabecera = 50;

        tabPuestosCpe.scrollWidth = 1200;

        tabPuestosCpe.dblClkFunction = 'dblClckTablaPuestosCpe';

        tabPuestosCpe.displayTabla();

        tabPuestosCpe.pack();
        
        actualizarOtrasPestanas('justif');
    }
    
    function extraerListadoPuestosCpe(nodos){
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
                        else if(hijosFila[cont].nodeName=="PAIS"){
                            nodoCampo = hijosFila[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[2] = nodoCampo.childNodes[0].nodeValue;
                            }
                            else{
                                fila[2] = '-';
                            }
                        }
                        else if(hijosFila[cont].nodeName=="NIVEL_FORMATIVO"){
                            nodoCampo = hijosFila[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[3] = nodoCampo.childNodes[0].nodeValue;
                            }
                            else{
                                fila[3] = '-';
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
                            fila[4] = filaTitulaciones;
                        }
                        else if(hijosFila[cont].nodeName=="SUBV_SOLIC"){
                            nodoCampo = hijosFila[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[5] = nodoCampo.childNodes[0].nodeValue;
                            }
                            else{
                                fila[5] = '-';
                            }
                        }
                        else if(hijosFila[cont].nodeName=="SUBV_APROB"){
                            nodoCampo = hijosFila[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[6] = nodoCampo.childNodes[0].nodeValue;
                            }
                            else{
                                fila[6] = '-';
                            }
                        }
                        else if(hijosFila[cont].nodeName=="RESULTADO"){
                            nodoCampo = hijosFila[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[7] = nodoCampo.childNodes[0].nodeValue;
                            }
                            else{
                                fila[7] = '-';
                            }
                        }
                        else if(hijosFila[cont].nodeName=="MOTIVO"){
                            nodoCampo = hijosFila[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[8] = nodoCampo.childNodes[0].nodeValue;
                            }
                            else{
                                fila[8] = '-';
                            }
                        }
                        else if(hijosFila[cont].nodeName=="OBSERVACIONES"){
                            nodoCampo = hijosFila[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[9] = nodoCampo.childNodes[0].nodeValue;
                            }
                            else{
                                fila[9] = '-';
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
    
    function dblClckTablaPuestosCpe(rowID,tableName){
        pulsarModificarPuestoCpe();
    }
</script>

<body>
    <div id="barraProgresoPuestosSolicitudCpe" style="position: absolute; z-index:10; visibility: hidden; top: 30%; left: 30%;">
        <table width="100%" border="0px" cellpadding="0px" cellspacing="0px" border="0px">
            <tr>
                <td align="center" valign="middle">
                    <table class="contenedorHidepage" cellpadding="0px" cellspacing="0px" border="0px">
                        <tr>
                            <td>
                                <table width="349px" height="100%">
                                    <tr>
                                        <td colspan="3" style="height:70%;text-align:center;valign:middle;">
                                            <span id="msgCargandoPuestoCpe">
                                                <%=meLanbide39I18n.getMensaje(idiomaUsuario, "msg.cargandoDatos")%>
                                            </span>
                                            <span id="msgEliminandoPuestoCpe">
                                                <%=meLanbide39I18n.getMensaje(idiomaUsuario, "msg.eliminandoDatos")%>
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
            <div id="puestosCpe" style="padding: 5px; width:876px; height: 159px; text-align: center; margin:0px;margin-top:0px;" align="center"></div>
            <div class="botonera">
                <input type="button" id="btnNuevoPuestoCpe" name="btnNuevoPuestoCpe" class="botonGeneral" value="<%=meLanbide39I18n.getMensaje(idiomaUsuario, "btn.alta")%>" onclick="pulsarAltaPuestoCpe();">
                <input type="button" id="btnModificarPuestoCpe" name="btnModificarPuestoCpe" class="botonGeneral" value="<%=meLanbide39I18n.getMensaje(idiomaUsuario, "btn.modificar")%>" onclick="pulsarModificarPuestoCpe();">
                <input type="button" id="btnEliminaPuestoCpe" name="btnEliminaPuestoCpe" class="botonGeneral" value="<%=meLanbide39I18n.getMensaje(idiomaUsuario, "btn.eliminar")%>" onclick="pulsarEliminarPuestoCpe();">
            </div>
        </div>
    </div>
</body>

<script type="text/javascript">   
    var tabPuestosCpe;
    var listaPuestosCpe = new Array();
    var listaPuestosCpeTabla = new Array();
    var listaPuestosCpeTabla_titulos = new Array();
    var listaPuestosCpeTabla_estilos = new Array();

    //tabPuestosCpe = new Tabla(document.getElementById('puestosCpe'), 890);
    tabPuestosCpe = new FixedColumnTable(document.getElementById('puestosCpe'), 850, 876, 'puestosCpe');
    tabPuestosCpe.addColumna('180','left',"<%= meLanbide39I18n.getMensaje(idiomaUsuario,"label.solicitud.tabla.puestos.col1")%>", "<%= meLanbide39I18n.getMensaje(idiomaUsuario,"label.solicitud.tabla.puestos.col1.title")%>");
    tabPuestosCpe.addColumna('130','left',"<%= meLanbide39I18n.getMensaje(idiomaUsuario,"label.solicitud.tabla.puestos.col2")%>", "<%= meLanbide39I18n.getMensaje(idiomaUsuario,"label.solicitud.tabla.puestos.col2.title")%>");    
    tabPuestosCpe.addColumna('80','left',"<%= meLanbide39I18n.getMensaje(idiomaUsuario,"label.solicitud.tabla.puestos.col3")%>", "<%= meLanbide39I18n.getMensaje(idiomaUsuario,"label.solicitud.tabla.puestos.col3.title")%>");   
    tabPuestosCpe.addColumna('140','left',"<%= meLanbide39I18n.getMensaje(idiomaUsuario,"label.solicitud.tabla.puestos.col4")%>", "<%= meLanbide39I18n.getMensaje(idiomaUsuario,"label.solicitud.tabla.puestos.col4.title")%>");   
    tabPuestosCpe.addColumna('80','right',"<%= meLanbide39I18n.getMensaje(idiomaUsuario,"label.solicitud.tabla.puestos.col5")%>", "<%= meLanbide39I18n.getMensaje(idiomaUsuario,"label.solicitud.tabla.puestos.col5.title")%>");   
    tabPuestosCpe.addColumna('100','right',"<%= meLanbide39I18n.getMensaje(idiomaUsuario,"label.solicitud.tabla.puestos.col6")%>", "<%= meLanbide39I18n.getMensaje(idiomaUsuario,"label.solicitud.tabla.puestos.col6.title")%>");   
    tabPuestosCpe.addColumna('130','left',"<%= meLanbide39I18n.getMensaje(idiomaUsuario,"label.solicitud.tabla.puestos.col7")%>", "<%= meLanbide39I18n.getMensaje(idiomaUsuario,"label.solicitud.tabla.puestos.col7.title")%>");   
    tabPuestosCpe.addColumna('130','left',"<%= meLanbide39I18n.getMensaje(idiomaUsuario,"label.solicitud.tabla.puestos.col8")%>", "<%= meLanbide39I18n.getMensaje(idiomaUsuario,"label.solicitud.tabla.puestos.col8.title")%>");     
    tabPuestosCpe.addColumna('200','left',"<%= meLanbide39I18n.getMensaje(idiomaUsuario,"label.solicitud.tabla.puestos.col9")%>", "<%= meLanbide39I18n.getMensaje(idiomaUsuario,"label.solicitud.tabla.puestos.col9.title")%>");     
    
    tabPuestosCpe.numColumnasFijas = 1;

    tabPuestosCpe.displayCabecera=true;
    
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
        listaPuestosCpe[<%=i%>] = ['<%=voP.getCodPuesto()%>','<%=voP.getDescPuesto()%>', '<%=voP.getPais()%>', '<%=voP.getNivelFor()%>', '<%=titulacion%>', '<%=voP.getSubvSolic()%>', '<%=voP.getSubvAprob()%>', '<%=voP.getSubvAprob()%>', '<%=voP.getResultado()%>', '<%=voP.getMotivo()%>', '<%=voP.getObservaciones()%>'];
        listaPuestosCpeTabla[<%=i%>] = ['<%=voP.getDescPuesto()%>', '<%=voP.getPais()%>', '<%=voP.getNivelFor()%>', '<%=titulacion%>', '<%=voP.getSubvSolic()%>', '<%=voP.getSubvAprob()%>', '<%=voP.getResultado()%>', '<%=voP.getMotivo()%>', '<%=voP.getObservaciones()%>'];
        listaPuestosCpeTabla_titulos[<%=i%>] = ['<%=voP.getDescPuesto()%>', '<%=voP.getPais()%>', '<%=voP.getNivelFor()%>', getListAsTextCpe(listaTitulaciones), '<%=voP.getSubvSolic()%>', '<%=voP.getSubvAprob()%>', '<%=voP.getResultado()%>', '<%=voP.getMotivo()%>', '<%=voP.getObservaciones()%>'];
    <%
            }// for
        }// if
    %>
    
    for(var cont = 0; cont < listaPuestosCpeTabla.length; cont++){
        tabPuestosCpe.addFilaConFormato(listaPuestosCpeTabla[cont], listaPuestosCpeTabla_titulos[cont], listaPuestosCpeTabla_estilos[cont])
    }
    
    tabPuestosCpe.height = '146';
    
    tabPuestosCpe.altoCabecera = 50;
    
    tabPuestosCpe.scrollWidth = 1200;
    
    tabPuestosCpe.dblClkFunction = 'dblClckTablaPuestosCpe';
    
    tabPuestosCpe.displayTabla();
    
    tabPuestosCpe.pack();
</script>
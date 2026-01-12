<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>

<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide39.i18n.MeLanbide39I18n" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide39.vo.FilaOfertaVO" %>
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

<script type="text/javascript" src="<c:url value='/scripts/popup.js'/>"></script>

<script type="text/javascript">   
    function pulsarAltaOfertaCpe(){
        var control = new Date();
        var result = null;
        var nodos = null;
        if(navigator.appName.indexOf("Internet Explorer")!=-1){
            nodos = lanzarPopUpModal('<%=request.getContextPath()%>/PeticionModuloIntegracion.do?tarea=preparar&modulo=MELANBIDE39&operacion=cargarNuevaOferta&tipo=0&numero=<%=numExpediente%>&control='+control.getTime(),560,980,'no','no');
        }else{
            nodos = lanzarPopUpModal('<%=request.getContextPath()%>/PeticionModuloIntegracion.do?tarea=preparar&modulo=MELANBIDE39&operacion=cargarNuevaOferta&tipo=0&numero=<%=numExpediente%>&control='+control.getTime(),620,980,'no','no');
        }
        if (nodos != undefined){
            result = extraerListadoOfertasCpe(nodos);
            if(result != undefined){
                if(result[0] == '0'){
                    recargarTablaOfertasCpe(result);
                }
            }
        }
    }
    
    function pulsarModificarOfertaCpe(){
        var fila;
        if(tabOfertasCpe.selectedIndex != -1) {
            fila = tabOfertasCpe.selectedIndex;
            var control = new Date();
            var result = null;
            var nodos = null;
            var opcion = '';
            if(document.forms[0].modoConsulta.value == "si"){
                opcion = 'consultar';
            }else{
                opcion = 'modificar';
            }
            if(navigator.appName.indexOf("Internet Explorer")!=-1){
                nodos = lanzarPopUpModal('<%=request.getContextPath()%>/PeticionModuloIntegracion.do?tarea=preparar&modulo=MELANBIDE39&operacion=cargarModificarOferta&tipo=0&numero=<%=numExpediente%>&opcion='+opcion+'&idOferta='+listaOfertasCpe[fila][0]+'&idPuesto='+listaOfertasCpe[fila][1]+'&control='+control.getTime(),560,980,'yes','no');
            }else{
                nodos = lanzarPopUpModal('<%=request.getContextPath()%>/PeticionModuloIntegracion.do?tarea=preparar&modulo=MELANBIDE39&operacion=cargarModificarOferta&tipo=0&numero=<%=numExpediente%>&opcion='+opcion+'&idOferta='+listaOfertasCpe[fila][0]+'&idPuesto='+listaOfertasCpe[fila][1]+'&control='+control.getTime(),620,980,'yes','no');
            }
            if (nodos != undefined){
                result = extraerListadoOfertasCpe(nodos);
                if(result != undefined){
                    if(result[0] == '0'){
                        recargarTablaOfertasCpe(result);
                    }
                }
            }
        }else{
                jsp_alerta('A', '<%=meLanbide39I18n.getMensaje(idiomaUsuario, "msg.msjNoSelecFila")%>');
            }
    }
    
    function pulsarEliminarOfertaCpe(){
        

            if(tabOfertasCpe.selectedIndex != -1) {
                var resultado = jsp_alerta('', '<%=meLanbide39I18n.getMensaje(idiomaUsuario, "msg.preguntaEliminar")%>');
                if (resultado == 1){
                    
                    document.getElementById('msgCargandoPuestoOfertaCpe').style.display="none";
                    document.getElementById('msgEliminandoPuestoOfertaCpe').style.display="inline";
                    barraProgresoCpe('on', 'barraProgresoPuestosOfertaCpe');
                    var ajax = getXMLHttpRequest();
                    var nodos = null;
                    var CONTEXT_PATH = '<%=request.getContextPath()%>'
                    var url = CONTEXT_PATH + "/PeticionModuloIntegracion.do";
                    var parametros = "";
                    var control = new Date();
                    var listaPuestos = new Array();
                    parametros = 'tarea=preparar&modulo=MELANBIDE39&operacion=eliminarOferta&tipo=0&numero=<%=numExpediente%>&idOferta='+listaOfertasCpe[tabOfertasCpe.selectedIndex][0]+'&idPuesto='+listaOfertasCpe[fila][1]+'&control='+control.getTime();
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
                        listaPuestos = extraerListadoOfertasCpe(nodos);
                        recargarTablaOfertasCpe(listaPuestos);
                        var codigoOperacion = listaPuestos[0];
                        barraProgresoCpe('off', 'barraProgresoPuestosOfertaCpe');
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
    }
    
    function recargarTablaOfertasCpe(result){
        var fila;
        listaOfertasCpe = new Array();
        listaOfertasCpeTabla = new Array();
        listaOfertasCpeTabla_titulos = new Array();
        listaOfertasCpeTabla_estilos = new Array();
        fila = result[1];
        recargarCalculosCpe(fila);
        for(var i = 2;i< result.length; i++){
            fila = result[i];
            listaOfertasCpe[i-2] = fila;
            listaOfertasCpeTabla[i-2] = [fila[2],fila[3], fila[4], fila[5], fila[6], fila[7], fila[8], fila[9]];
            listaOfertasCpeTabla_titulos[i-2] = [fila[2],fila[3], fila[4], fila[5], fila[6], fila[7], fila[8], fila[9]];
        }

        tabOfertasCpe = new FixedColumnTable(document.getElementById('ofertasCpe'), 850, 876, 'ofertasCpe');
        tabOfertasCpe.addColumna('140','left',"<%= meLanbide39I18n.getMensaje(idiomaUsuario,"label.solicitud.tabla.ofertas.col1")%>");
        tabOfertasCpe.addColumna('140','left',"<%= meLanbide39I18n.getMensaje(idiomaUsuario,"label.solicitud.tabla.ofertas.col2")%>");    
        tabOfertasCpe.addColumna('250','left',"<%= meLanbide39I18n.getMensaje(idiomaUsuario,"label.solicitud.tabla.ofertas.col3")%>");   
        tabOfertasCpe.addColumna('80','left',"<%= meLanbide39I18n.getMensaje(idiomaUsuario,"label.solicitud.tabla.ofertas.col4")%>");   
        tabOfertasCpe.addColumna('60','left',"<%= meLanbide39I18n.getMensaje(idiomaUsuario,"label.solicitud.tabla.ofertas.col5")%>");   
        tabOfertasCpe.addColumna('60','left',"<%= meLanbide39I18n.getMensaje(idiomaUsuario,"label.solicitud.tabla.ofertas.col6")%>");   
        tabOfertasCpe.addColumna('60','left',"<%= meLanbide39I18n.getMensaje(idiomaUsuario,"label.solicitud.tabla.ofertas.col7")%>");   
        tabOfertasCpe.addColumna('140','left',"<%= meLanbide39I18n.getMensaje(idiomaUsuario,"label.solicitud.tabla.ofertas.col8")%>");     

        tabOfertasCpe.numColumnasFijas = 2;

        tabOfertasCpe.displayCabecera=true;
    
        for(var cont = 0; cont < listaOfertasCpeTabla.length; cont++){
            tabOfertasCpe.addFilaConFormato(listaOfertasCpeTabla[cont], listaOfertasCpeTabla_titulos[cont], listaOfertasCpeTabla_estilos[cont])
        }

        tabOfertasCpe.height = '146';

        tabOfertasCpe.altoCabecera = 50;

        tabOfertasCpe.scrollWidth = 1200;

        tabOfertasCpe.dblClkFunction = 'dblClckTablaOfertasCpe';

        tabOfertasCpe.displayTabla();

        tabOfertasCpe.pack();
        
        actualizarOtrasPestanas('oferta');
    }
    
    function dblClckTablaOfertasCpe(rowID,tableName){
        pulsarModificarOfertaCpe();
    }
    
    function extraerListadoOfertasCpe(nodos){
            var elemento = nodos[0];
            var hijos = elemento.childNodes;
            var codigoOperacion = null;
            var listaOfertas = new Array();
            var fila = new Array();
            var nodoFila;
            var hijosFila;
            var nodoCampo;
            var nodoCalculos;
            var hijosCalculos;
            var j;
            
            for(j=0;hijos!=null && j<hijos.length;j++){
                if(hijos[j].nodeName=="CODIGO_OPERACION"){                            
                    codigoOperacion = hijos[j].childNodes[0].nodeValue;
                    listaOfertas[j] = codigoOperacion;
                }                  
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
                    listaOfertas[j] = fila;
                    fila = new Array();  
                }else if(hijos[j].nodeName=="FILA"){
                    nodoFila = hijos[j];
                    hijosFila = nodoFila.childNodes;
                    for(var cont = 0; cont < hijosFila.length; cont++){
                        if(hijosFila[cont].nodeName=="ID_OFERTA"){
                            nodoCampo = hijosFila[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[0] = nodoCampo.childNodes[0].nodeValue;
                            }
                            else{
                                fila[0] = '-';
                            }
                        }else if(hijosFila[cont].nodeName=="COD_PUESTO"){
                            nodoCampo = hijosFila[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[1] = nodoCampo.childNodes[0].nodeValue;
                            }
                            else{
                                fila[1] = '-';
                            }
                        }
                        else if(hijosFila[cont].nodeName=="DESC_PUESTO"){
                            nodoCampo = hijosFila[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[2] = nodoCampo.childNodes[0].nodeValue;
                            }
                            else{
                                fila[2] = '-';
                            }
                        }
                        else if(hijosFila[cont].nodeName=="DESC_OFERTA"){
                            nodoCampo = hijosFila[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[3] = nodoCampo.childNodes[0].nodeValue;
                            }
                            else{
                                fila[3] = '-';
                            }
                        }
                        else if(hijosFila[cont].nodeName=="NOMAPEL"){
                            nodoCampo = hijosFila[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[4] = nodoCampo.childNodes[0].nodeValue;
                            }
                            else{
                                fila[4] = '-';
                            }
                        }
                        else if(hijosFila[cont].nodeName=="DNI"){
                            nodoCampo = hijosFila[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[5] = nodoCampo.childNodes[0].nodeValue;
                            }
                            else{
                                fila[5] = '-';
                            }
                        }
                        else if(hijosFila[cont].nodeName=="FEC_INI"){
                            nodoCampo = hijosFila[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[6] = nodoCampo.childNodes[0].nodeValue;
                            }
                            else{
                                fila[6] = '';
                            }
                        }
                        else if(hijosFila[cont].nodeName=="FEC_FIN"){
                            nodoCampo = hijosFila[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[7] = nodoCampo.childNodes[0].nodeValue;
                            }
                            else{
                                fila[7] = '';
                            }
                        }
                        else if(hijosFila[cont].nodeName=="FEC_BAJA"){
                            nodoCampo = hijosFila[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[8] = nodoCampo.childNodes[0].nodeValue;
                            }
                            else{
                                fila[8] = '';
                            }
                        }
                        else if(hijosFila[cont].nodeName=="CAUSA_BAJA"){
                            nodoCampo = hijosFila[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[9] = nodoCampo.childNodes[0].nodeValue;
                            }
                            else{
                                fila[9] = '-';
                            }
                        }
                    }
                    listaOfertas[j] = fila;
                    fila = new Array();   
                }
        } 
        return listaOfertas;
    }
    
    function getListaOfertasCpe(){
        var ajax = getXMLHttpRequest();
        var nodos = null;
        var CONTEXT_PATH = '<%=request.getContextPath()%>'
        var url = CONTEXT_PATH + '/PeticionModuloIntegracion.do';
        var parametros = '';
        var control = new Date();
        parametros = 'tarea=preparar&modulo=MELANBIDE39&operacion=getListaOfertasNoDenegadasPorExpediente&tipo=0&numero=<%=numExpediente%>&control='+control.getTime();
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
            jsp_alerta("A",'<%=meLanbide39I18n.getMensaje(idiomaUsuario,"error.errorProcesarRespuesta")%>');
        }//try-catch
        return nodos;
    }
    
    function actualizarPestanaOferta(){
        try{
            var nodos = this.getListaOfertasCpe();
            var result = this.extraerListadoOfertasCpe(nodos);
            this.recargarTablaOfertasCpe(result);
        }catch(err){
            
        }
    }
</script>

<body>
    <div id="barraProgresoPuestosOfertaCpe" style="position: absolute; z-index:10; visibility: hidden; top: 30%; left: 30%;">
        <table width="100%" border="0px" cellpadding="0px" cellspacing="0px" border="0px">
            <tr>
                <td align="center" valign="middle">
                    <table class="contenedorHidepage" cellpadding="0px" cellspacing="0px" border="0px">
                        <tr>
                            <td>
                                <table width="349px" height="100%">
                                    <tr>
                                        <td colspan="3" style="height:70%;text-align:center;valign:middle;">
                                            <span id="msgCargandoPuestoOfertaCpe">
                                                <%=meLanbide39I18n.getMensaje(idiomaUsuario, "msg.cargandoDatos")%>
                                            </span>
                                            <span id="msgEliminandoPuestoOfertaCpe">
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
        <div id="ofertasCpe" style="padding: 5px; width:876px; height: 159px; text-align: center; margin:0px;margin-top:0px;" align="center"></div>
        <div class="botonera">
            <!--<input type="button" id="btnNuevaOfertaCpe" name="btnNuevaOfertaCpe" class="botonGeneral" value="<%=meLanbide39I18n.getMensaje(idiomaUsuario, "btn.alta")%>" onclick="pulsarAltaOfertaCpe();">-->
            <input type="button" id="btnModificarOfertaCpe" name="btnModificarOfertaCpe" class="botonGeneral" value="<%=meLanbide39I18n.getMensaje(idiomaUsuario, "btn.modificar")%>" onclick="pulsarModificarOfertaCpe();">
            <!--<input type="button" id="btnEliminaOfertaCpe" name="btnEliminaOfertaCpe" class="botonGeneral" value="<%=meLanbide39I18n.getMensaje(idiomaUsuario, "btn.eliminar")%>" onclick="pulsarEliminarOfertaCpe();">-->
        </div>
    </div>
</body>

<script type="text/javascript">
    var tabOfertasCpe;
    var listaOfertasCpe = new Array();
    var listaOfertasCpeTabla = new Array();
    var listaOfertasCpeTabla_titulos = new Array();
    var listaOfertasCpeTabla_estilos = new Array();

    tabOfertasCpe = new FixedColumnTable(document.getElementById('ofertasCpe'), 850, 876, 'ofertasCpe');
    tabOfertasCpe.addColumna('140','left',"<%= meLanbide39I18n.getMensaje(idiomaUsuario,"label.solicitud.tabla.ofertas.col1")%>");
    tabOfertasCpe.addColumna('140','left',"<%= meLanbide39I18n.getMensaje(idiomaUsuario,"label.solicitud.tabla.ofertas.col2")%>");    
    tabOfertasCpe.addColumna('250','left',"<%= meLanbide39I18n.getMensaje(idiomaUsuario,"label.solicitud.tabla.ofertas.col3")%>");   
    tabOfertasCpe.addColumna('80','left',"<%= meLanbide39I18n.getMensaje(idiomaUsuario,"label.solicitud.tabla.ofertas.col4")%>");   
    tabOfertasCpe.addColumna('60','left',"<%= meLanbide39I18n.getMensaje(idiomaUsuario,"label.solicitud.tabla.ofertas.col5")%>");   
    tabOfertasCpe.addColumna('60','left',"<%= meLanbide39I18n.getMensaje(idiomaUsuario,"label.solicitud.tabla.ofertas.col6")%>");   
    tabOfertasCpe.addColumna('60','left',"<%= meLanbide39I18n.getMensaje(idiomaUsuario,"label.solicitud.tabla.ofertas.col7")%>");   
    tabOfertasCpe.addColumna('140','left',"<%= meLanbide39I18n.getMensaje(idiomaUsuario,"label.solicitud.tabla.ofertas.col8")%>");     
    
    tabOfertasCpe.numColumnasFijas = 2;

    tabOfertasCpe.displayCabecera=true;
    
    <%  		
        FilaOfertaVO oferta = null;
        List<FilaOfertaVO> listaOfertas = (List<FilaOfertaVO>)request.getAttribute("ofertas");													
        if (listaOfertas != null && listaOfertas.size() >0){
            for(int i = 0;i < listaOfertas.size();i++)
            {
                oferta = listaOfertas.get(i);
    %>
        listaOfertasCpe[<%=i%>] = ['<%=oferta.getIdOferta()%>', '<%=oferta.getCodPuesto()%>', '<%=oferta.getDescPuesto()%>', '<%=oferta.getDescOferta()%>', '<%=oferta.getNomApel()%>', '<%=oferta.getDni()%>', '<%=oferta.getFecIni()%>', '<%=oferta.getFecFin()%>', '<%=oferta.getFecBaja()%>', '<%=oferta.getCausaBaja()%>'];
        listaOfertasCpeTabla[<%=i%>] = ['<%=oferta.getDescPuesto()%>', '<%=oferta.getDescOferta()%>', '<%=oferta.getNomApel()%>', '<%=oferta.getDni()%>', '<%=oferta.getFecIni()%>', '<%=oferta.getFecFin()%>', '<%=oferta.getFecBaja()%>', '<%=oferta.getCausaBaja()%>'];
        listaOfertasCpeTabla_titulos[<%=i%>] = ['<%=oferta.getDescPuesto()%>', '<%=oferta.getDescOferta()%>', '<%=oferta.getNomApel()%>', '<%=oferta.getDni()%>', '<%=oferta.getFecIni()%>', '<%=oferta.getFecFin()%>', '<%=oferta.getFecBaja()%>', '<%=oferta.getCausaBaja()%>'];
    <%
            }// for
        }// if
    %>
    
    for(var cont = 0; cont < listaOfertasCpeTabla.length; cont++){
        tabOfertasCpe.addFilaConFormato(listaOfertasCpeTabla[cont], listaOfertasCpeTabla_titulos[cont], listaOfertasCpeTabla_estilos[cont])
    }
    
    tabOfertasCpe.height = '146';
    
    tabOfertasCpe.altoCabecera = 50;
    
    tabOfertasCpe.scrollWidth = 1200;
    
    tabOfertasCpe.dblClkFunction = 'dblClckTablaOfertasCpe';
    
    tabOfertasCpe.displayTabla();
    
    tabOfertasCpe.pack();
</script>
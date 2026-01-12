<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>

<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide60.i18n.MeLanbide60I18n" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide60.vo.FilaOfertaVO" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@ page import="es.altia.common.service.config.Config"%>
<%@ page import="es.altia.common.service.config.ConfigServiceHelper"%>
<%@page import="java.util.ArrayList" %>
<%@page import="java.util.List" %>

<%
    String sIdioma = request.getParameter("idioma");
    int idiomaUsuario = Integer.parseInt(sIdioma);
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
    //Clase para internacionalizar los mensajes de la aplicacion.
    MeLanbide60I18n meLanbide60I18n = MeLanbide60I18n.getInstance();
    
    Config m_Config = ConfigServiceHelper.getConfig("common");
    String statusBar = m_Config.getString("JSP.StatusBar");
    String nombreModulo     = request.getParameter("nombreModulo");
    String codOrganizacion  = request.getParameter("codOrganizacionModulo");
    String numExpediente    = request.getParameter("numero");
    String mensajeProgreso = "";
%>
<jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
<jsp:setProperty name="descriptor"  property="idi_cod" value="<%=idiomaUsuario%>" />
<jsp:setProperty name="descriptor"  property="apl_cod" value="<%=apl%>" />
<link rel="StyleSheet" media="screen" type="text/css" href="<%=request.getContextPath()%><%=css%>">
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/>
<link rel="stylesheet" type="text/css" href="<c:url value='/css/extension/melanbide60/melanbide60.css'/>">

<script type="text/javascript" src="<c:url value='/scripts/popup.js'/>"></script>

<script type="text/javascript">   
    function pulsarAltaOfertaCme(){
        var control = new Date();
        var result = null;
        lanzarPopUpModal('<%=request.getContextPath()%>/PeticionModuloIntegracion.do?tarea=preparar&modulo=MELANBIDE60&operacion=cargarNuevaOferta&tipo=0&numero=<%=numExpediente%>&control='+control.getTime(),600,980,'no','no', function(nodos){
        if (nodos != undefined){
            result = extraerListadoOfertasCme(nodos);
            if(result != undefined){
                if(result[0] == '0'){
                    recargarTablaOfertasCme(result);
                }
            }
        }
		});
    }
    
    function pulsarModificarOfertaCme(){
        var fila;
        if(tabOfertasCme.selectedIndex != -1) {
            fila = tabOfertasCme.selectedIndex;
            var control = new Date();
            var result = null;
            var nodos = null;
            var opcion = '';
            if(document.forms[0].modoConsulta.value == "si"){
                opcion = 'consultar';
            }else{
                opcion = 'modificar';
            }
            lanzarPopUpModal('<%=request.getContextPath()%>/PeticionModuloIntegracion.do?tarea=preparar&modulo=MELANBIDE60&operacion=cargarModificarOferta&tipo=0&numero=<%=numExpediente%>&opcion='+opcion+'&idOferta='+listaOfertasCme[fila][0]+'&idPuesto='+listaOfertasCme[fila][1]+'&control='+control.getTime(),600,980,'yes','no', function(nodos){
            if (nodos != undefined){
                result = extraerListadoOfertasCme(nodos);
                if(result != undefined){
                    if(result[0] == '0'){
                        recargarTablaOfertasCme(result);
                    }
                }
            }
			});
        }else{
                jsp_alerta('A', '<%=meLanbide60I18n.getMensaje(idiomaUsuario, "msg.msjNoSelecFila")%>');
            }
    }
    
    function pulsarEliminarOfertaCme(){
        

            if(tabOfertasCme.selectedIndex != -1) {
                var resultado = jsp_alerta('', '<%=meLanbide60I18n.getMensaje(idiomaUsuario, "msg.preguntaEliminar")%>');
                if (resultado == 1){
                    
                    document.getElementById('msgCargandoPuestoOfertaCme').style.display="none";
                    document.getElementById('msgEliminandoPuestoOfertaCme').style.display="inline";
                    barraProgresoCme('on', 'barraProgresoPuestosOfertaCme');
                    var ajax = getXMLHttpRequest();
                    var nodos = null;
                    var CONTEXT_PATH = '<%=request.getContextPath()%>'
                    var url = CONTEXT_PATH + "/PeticionModuloIntegracion.do";
                    var parametros = "";
                    var control = new Date();
                    var listaPuestos = new Array();
                    parametros = 'tarea=preparar&modulo=MELANBIDE60&operacion=eliminarOferta&tipo=0&numero=<%=numExpediente%>&idOferta='+listaOfertasCme[tabOfertasCme.selectedIndex][0]+'&idPuesto='+listaOfertasCme[fila][1]+'&control='+control.getTime();
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
                        listaPuestos = extraerListadoOfertasCme(nodos);
                        recargarTablaOfertasCme(listaPuestos);
                        var codigoOperacion = listaPuestos[0];
                        barraProgresoCme('off', 'barraProgresoPuestosOfertaCme');
                        if(codigoOperacion=="0"){
                            jsp_alerta("A",'<%=meLanbide60I18n.getMensaje(idiomaUsuario,"msg.registroEliminadoOK")%>');
                        }else if(codigoOperacion=="1"){
                            jsp_alerta("A",'<%=meLanbide60I18n.getMensaje(idiomaUsuario,"error.errorEliminarBD")%>');
                        }else if(codigoOperacion=="2"){
                            jsp_alerta("A",'<%=meLanbide60I18n.getMensaje(idiomaUsuario,"error.errorEliminarGen")%>');
                        }else if(codigoOperacion=="3"){
                            jsp_alerta("A",'<%=meLanbide60I18n.getMensaje(idiomaUsuario,"error.pasoParametrosEliminar")%>');
                        }else{
                            jsp_alerta("A",'<%=meLanbide60I18n.getMensaje(idiomaUsuario,"error.errorEliminarGen")%>');
                        }//if(
                    }
                    catch(Err){
                        jsp_alerta("A",'<%=meLanbide60I18n.getMensaje(idiomaUsuario,"error.errorProcesarRespuesta")%>');
                    }//try-catch
                }
            } 
            else {
                jsp_alerta('A', '<%=meLanbide60I18n.getMensaje(idiomaUsuario, "msg.msjNoSelecFila")%>');
            }
    }
    
    function recargarTablaOfertasCme(result){
        var fila;
        listaOfertasCme = new Array();
        listaOfertasCmeTabla = new Array();
        listaOfertasCmeTabla_titulos = new Array();
        listaOfertasCmeTabla_estilos = new Array();
        fila = result[1];
        recargarCalculosCme(fila);
        for(var i = 2;i< result.length; i++){
            fila = result[i];
            //listaOfertasCme[i-2] = fila;
			listaOfertasCme[i-2] = [fila[0],fila[1],fila[2],fila[3], fila[4], fila[5], fila[6], fila[7], fila[8], fila[9]];
            listaOfertasCmeTabla[i-2] = [fila[2],fila[3], fila[4], fila[5], fila[6], fila[7], fila[8], fila[9]];
            listaOfertasCmeTabla_titulos[i-2] = [fila[2],fila[3], fila[4], fila[5], fila[6], fila[7], fila[8], fila[9]];
        }

        tabOfertasCme = new FixedColumnTable(document.getElementById('ofertasCme'), 850, 876, 'ofertasCme');
        tabOfertasCme.addColumna('200','left',"<%= meLanbide60I18n.getMensaje(idiomaUsuario,"label.solicitud.tabla.ofertas.col1")%>");
        tabOfertasCme.addColumna('150','left',"<%= meLanbide60I18n.getMensaje(idiomaUsuario,"label.solicitud.tabla.ofertas.col2")%>");    
        tabOfertasCme.addColumna('300','left',"<%= meLanbide60I18n.getMensaje(idiomaUsuario,"label.solicitud.tabla.ofertas.col3")%>");   
        tabOfertasCme.addColumna('100','left',"<%= meLanbide60I18n.getMensaje(idiomaUsuario,"label.solicitud.tabla.ofertas.col4")%>");   
        tabOfertasCme.addColumna('80','left',"<%= meLanbide60I18n.getMensaje(idiomaUsuario,"label.solicitud.tabla.ofertas.col5")%>");   
        tabOfertasCme.addColumna('80','left',"<%= meLanbide60I18n.getMensaje(idiomaUsuario,"label.solicitud.tabla.ofertas.col6")%>");   
        tabOfertasCme.addColumna('80','left',"<%= meLanbide60I18n.getMensaje(idiomaUsuario,"label.solicitud.tabla.ofertas.col7")%>");   
        tabOfertasCme.addColumna('350','left',"<%= meLanbide60I18n.getMensaje(idiomaUsuario,"label.solicitud.tabla.ofertas.col8")%>");     

        tabOfertasCme.numColumnasFijas = 2;

        tabOfertasCme.displayCabecera=true;
    
        for(var cont = 0; cont < listaOfertasCmeTabla.length; cont++){
            tabOfertasCme.addFilaConFormato(listaOfertasCmeTabla[cont], listaOfertasCmeTabla_titulos[cont], listaOfertasCmeTabla_estilos[cont])
        }

        tabOfertasCme.height = '146';

        tabOfertasCme.altoCabecera = 50;

        tabOfertasCme.scrollWidth = 1400;

        tabOfertasCme.dblClkFunction = 'dblClckTablaOfertasCme';

        tabOfertasCme.displayTabla();

        tabOfertasCme.pack();
        
        actualizarOtrasPestanas('oferta');
    }
    
    function dblClckTablaOfertasCme(rowID,tableName){
        pulsarModificarOfertaCme();
    }
    
    function extraerListadoOfertasCme(nodos){
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
    
    function getListaOfertasCme(){
        var ajax = getXMLHttpRequest();
        var nodos = null;
        var CONTEXT_PATH = '<%=request.getContextPath()%>'
        var url = CONTEXT_PATH + '/PeticionModuloIntegracion.do';
        var parametros = '';
        var control = new Date();
        parametros = 'tarea=preparar&modulo=MELANBIDE60&operacion=getListaOfertasNoDenegadasPorExpediente&tipo=0&numero=<%=numExpediente%>&control='+control.getTime();
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
            jsp_alerta("A",'<%=meLanbide60I18n.getMensaje(idiomaUsuario,"error.errorProcesarRespuesta")%>');
        }//try-catch
        return nodos;
    }
    
    function actualizarPestanaOferta(){
        try{
            var nodos = this.getListaOfertasCme();
            var result = this.extraerListadoOfertasCme(nodos);
            this.recargarTablaOfertasCme(result);
        }catch(err){
            
        }
    }
</script>

<body>
    <div id="barraProgresoPuestosOfertaCme" style="position: absolute; z-index:10; visibility: hidden; top: 30%; left: 30%;">
        <table width="100%" border="0px" cellpadding="0px" cellspacing="0px" border="0px">
            <tr>
                <td align="center" valign="middle">
                    <table class="contenedorHidepage" cellpadding="0px" cellspacing="0px" border="0px">
                        <tr>
                            <td>
                                <table width="349px" height="100%">
                                    <tr>
                                        <td colspan="3" style="height:70%;text-align:center;valign:middle;">
                                            <span id="msgCargandoPuestoOfertaCme">
                                                <%=meLanbide60I18n.getMensaje(idiomaUsuario, "msg.cargandoDatos")%>
                                            </span>
                                            <span id="msgEliminandoPuestoOfertaCme">
                                                <%=meLanbide60I18n.getMensaje(idiomaUsuario, "msg.eliminandoDatos")%>
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
        <div id="ofertasCme" style="padding: 5px; width:86%; height: 159px; text-align: center; margin:0px;margin-top:0px;" align="center"></div>
        <div class="botonera">
            <!--<input type="button" id="btnNuevaOfertaCme" name="btnNuevaOfertaCme" class="botonGeneral" value="<%=meLanbide60I18n.getMensaje(idiomaUsuario, "btn.alta")%>" onclick="pulsarAltaOfertaCme();">-->
            <input type="button" id="btnModificarOfertaCme" name="btnModificarOfertaCme" class="botonGeneral" value="<%=meLanbide60I18n.getMensaje(idiomaUsuario, "btn.modificar")%>" onclick="pulsarModificarOfertaCme();">
            <!--<input type="button" id="btnEliminaOfertaCme" name="btnEliminaOfertaCme" class="botonGeneral" value="<%=meLanbide60I18n.getMensaje(idiomaUsuario, "btn.eliminar")%>" onclick="pulsarEliminarOfertaCme();">-->
        </div>
    </div>
</body>

<script type="text/javascript">
    var tabOfertasCme;
    var listaOfertasCme = new Array();
    var listaOfertasCmeTabla = new Array();
    var listaOfertasCmeTabla_titulos = new Array();
    var listaOfertasCmeTabla_estilos = new Array();

    tabOfertasCme = new FixedColumnTable(document.getElementById('ofertasCme'), 850, 876, 'ofertasCme');
    tabOfertasCme.addColumna('200','left',"<%= meLanbide60I18n.getMensaje(idiomaUsuario,"label.solicitud.tabla.ofertas.col1")%>");
    tabOfertasCme.addColumna('150','left',"<%= meLanbide60I18n.getMensaje(idiomaUsuario,"label.solicitud.tabla.ofertas.col2")%>");    
    tabOfertasCme.addColumna('300','left',"<%= meLanbide60I18n.getMensaje(idiomaUsuario,"label.solicitud.tabla.ofertas.col3")%>");   
    tabOfertasCme.addColumna('100','left',"<%= meLanbide60I18n.getMensaje(idiomaUsuario,"label.solicitud.tabla.ofertas.col4")%>");   
    tabOfertasCme.addColumna('80','left',"<%= meLanbide60I18n.getMensaje(idiomaUsuario,"label.solicitud.tabla.ofertas.col5")%>");   
    tabOfertasCme.addColumna('80','left',"<%= meLanbide60I18n.getMensaje(idiomaUsuario,"label.solicitud.tabla.ofertas.col6")%>");   
    tabOfertasCme.addColumna('80','left',"<%= meLanbide60I18n.getMensaje(idiomaUsuario,"label.solicitud.tabla.ofertas.col7")%>");   
    tabOfertasCme.addColumna('350','left',"<%= meLanbide60I18n.getMensaje(idiomaUsuario,"label.solicitud.tabla.ofertas.col8")%>");     
    
    tabOfertasCme.numColumnasFijas = 2;

    tabOfertasCme.displayCabecera=true;
    
    <%  		
        FilaOfertaVO oferta = null;
        List<FilaOfertaVO> listaOfertas = (List<FilaOfertaVO>)request.getAttribute("ofertas");													
        if (listaOfertas != null && listaOfertas.size() >0){
            for(int i = 0;i < listaOfertas.size();i++)
            {
                oferta = listaOfertas.get(i);
    %>
        listaOfertasCme[<%=i%>] = ['<%=oferta.getIdOferta()%>', '<%=oferta.getCodPuesto()%>', '<%=oferta.getDescPuesto()%>', '<%=oferta.getDescOferta()%>', '<%=oferta.getNomApel()%>', '<%=oferta.getDni()%>', '<%=oferta.getFecIni()%>', '<%=oferta.getFecFin()%>', '<%=oferta.getFecBaja()%>', '<%=oferta.getCausaBaja()%>'];
        listaOfertasCmeTabla[<%=i%>] = ['<%=oferta.getDescPuesto()%>', '<%=oferta.getDescOferta()%>', '<%=oferta.getNomApel()%>', '<%=oferta.getDni()%>', '<%=oferta.getFecIni()%>', '<%=oferta.getFecFin()%>', '<%=oferta.getFecBaja()%>', '<%=oferta.getCausaBaja()%>'];
        listaOfertasCmeTabla_titulos[<%=i%>] = ['<%=oferta.getDescPuesto()%>', '<%=oferta.getDescOferta()%>', '<%=oferta.getNomApel()%>', '<%=oferta.getDni()%>', '<%=oferta.getFecIni()%>', '<%=oferta.getFecFin()%>', '<%=oferta.getFecBaja()%>', '<%=oferta.getCausaBaja()%>'];
    <%
            }// for
        }// if
    %>
    
    for(var cont = 0; cont < listaOfertasCmeTabla.length; cont++){
        tabOfertasCme.addFilaConFormato(listaOfertasCmeTabla[cont], listaOfertasCmeTabla_titulos[cont], listaOfertasCmeTabla_estilos[cont])
    }
    
    tabOfertasCme.height = '146';
    
    tabOfertasCme.altoCabecera = 50;
    
    tabOfertasCme.scrollWidth = 1400;
    
    tabOfertasCme.dblClkFunction = 'dblClckTablaOfertasCme';
    
    tabOfertasCme.displayTabla();
    
    tabOfertasCme.pack();
</script>

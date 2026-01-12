<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>

<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide48.i18n.MeLanbide48I18n" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide48.vo.FilaEntidadVO" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide48.vo.ColecRepresentanteVO" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide48.vo.ColecEntidadVO" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide48.vo.ColecEntidadEnAgrupacionVO" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide48.vo.SelectItem"%>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.common.service.config.Config"%>
<%@page import="es.altia.common.service.config.ConfigServiceHelper"%>
<%@page import="java.util.ArrayList" %>
<%@page import="java.util.List" %>
<%@page import="org.apache.commons.lang.StringEscapeUtils"%>

<%
    String sIdioma = request.getParameter("idioma");
    int idiomaUsuario = (sIdioma!=null && sIdioma!="" ? Integer.parseInt(sIdioma) : 1);
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

    String lcodNif = "";
    String ldescNif = "";

    // Tipo documento
    List<SelectItem> listaNif = new ArrayList<SelectItem>();
    if(request.getAttribute("listaNif") != null)
    listaNif = (List<SelectItem>)request.getAttribute("listaNif");    

    //comboNif
    if (listaNif != null && listaNif.size() > 0) 
    {
        int i;
        SelectItem gc = null;
        for (i = 0; i < listaNif.size() - 1; i++) 
        {
            gc = (SelectItem) listaNif.get(i);
            lcodNif += "\"" + gc.getCodigo() + "\",";
            ldescNif += "\"" + escape(gc.getDescripcion()) + "\",";
        }
        gc = (SelectItem) listaNif.get(i);
        lcodNif += "\"" + gc.getCodigo() + "\"";
        ldescNif += "\"" + escape(gc.getDescripcion()) + "\"";
    }

    ColecEntidadVO entidad = (ColecEntidadVO) request.getAttribute("entidad");

%>
<%!
    // Funcion para escapar strings para javascript
    private String escape(String str) 
    {
        return StringEscapeUtils.escapeJavaScript(str);
    }
%>

<jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
<jsp:setProperty name="descriptor"  property="idi_cod" value="<%=idiomaUsuario%>" />
<jsp:setProperty name="descriptor"  property="apl_cod" value="<%=apl%>" />
<link rel="StyleSheet" media="screen" type="text/css" href="<%=request.getContextPath()%><%=css%>">
<!--<script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.2/jquery.min.js"></script>-->
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/melanbide48/colecGestionDatosEntidadAgrupacion.js"></script>


<script type="text/javascript">   
    var codNif = [<%=lcodNif%>];
    var descNif = [<%=ldescNif%>];

    function pulsarAltaEntidadColec(){
        var control = new Date();
        var result = null;
       // Hay que pasar el codigo de la entidad padre para poner el VO en la request.
        lanzarPopUpModal('<%=request.getContextPath()%>/PeticionModuloIntegracion.do?tarea=preparar&modulo=MELANBIDE48&operacion=cargarNuevaEntidad&tipo=0&numero=<%=numExpediente%>&codEntidadPadre=<%=entidad!=null&&entidad.getCodEntidad()!=null?entidad.getCodEntidad():""%>&control='+control.getTime(),600,1180,'no','no',function(result){
        	if (result != undefined){
				if(result[0] == '0'){
                                    recargarTablaEntidadesColec(result);
                                    refrescarPestanasColec();
                                }
			}
		});
    }
    
    function pulsarModificarEntidadColec(){
        var fila;
        if(tabEntidadColec.selectedIndex != -1) {
            fila = tabEntidadColec.selectedIndex;
            var control = new Date();
            var result = null;
            var opcion = '';
            if(document.forms[0].modoConsulta.value == "si"){
                opcion = 'consultar';
            }else{
                opcion = 'modificar';
            }
            lanzarPopUpModal('<%=request.getContextPath()%>/PeticionModuloIntegracion.do?tarea=preparar&modulo=MELANBIDE48&operacion=cargarModificarEntidadAsociada&tipo=0&numero=<%=numExpediente%>&opcion='+opcion+'&codEntidad='+listaEntidadesColec[tabEntidadColec.selectedIndex][0]+'&codEntidadPadre='+listaEntidadesColec[tabEntidadColec.selectedIndex][1]+'&control='+control.getTime(),600,1180,'no','no',function(result){
        	if (result != undefined){
				if(result[0] == '0'){
                                    recargarTablaEntidadesColec1(result,"0");
                                    refrescarPestanasColec();
                                }
			}
		});
                
                
        }else{
                jsp_alerta('A', '<%=meLanbide48I18n.getMensaje(idiomaUsuario, "msg.msjNoSelecFila")%>');
            }
    }
    
    function pulsarEliminarEntidadColec(){
        

            if(tabEntidadColec.selectedIndex != -1) {
                var resultado = jsp_alerta('', '<%=meLanbide48I18n.getMensaje(idiomaUsuario, "msg.preguntaEliminar")%>');
                if (resultado == 1){
                    
                    document.getElementById('msgCargandoEntidadColec').style.display="none";
                    document.getElementById('msgEliminandoEntidadColec').style.display="inline";
                    //barraProgresoColec('on', 'barraProgresoEntidadesSolicitudColec');
                    var ajax = getXMLHttpRequest();
                    var nodos = null;
                    var CONTEXT_PATH = '<%=request.getContextPath()%>'
                    var url = CONTEXT_PATH + "/PeticionModuloIntegracion.do";
                    var parametros = "";
                    var control = new Date();
                    var listaEntidades = new Array();
                    parametros = 'tarea=preparar&modulo=MELANBIDE48&operacion=eliminarEntidadAsociada&tipo=0&numero=<%=numExpediente%>&codEntidad='+listaEntidadesColec[tabEntidadColec.selectedIndex][0]+'&codEntidadPadre='+listaEntidadesColec[tabEntidadColec.selectedIndex][1]+'&control='+control.getTime();
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
                        listaEntidades = extraerListadoEntidadesColec(nodos);
                        recargarTablaEntidadesColec(listaEntidades);
                        var codigoOperacion = listaEntidades[0];
                        if(codigoOperacion=="0"){
                            refrescarPestanasColec();
                            jsp_alerta("A",'<%=meLanbide48I18n.getMensaje(idiomaUsuario,"msg.registroEliminadoOK")%>');
                        }else if(codigoOperacion=="1"){
                            jsp_alerta("A",'<%=meLanbide48I18n.getMensaje(idiomaUsuario,"error.errorEliminarBD")%>');
                        }else if(codigoOperacion=="2"){
                            jsp_alerta("A",'<%=meLanbide48I18n.getMensaje(idiomaUsuario,"error.errorEliminarGen")%>');
                        }else if(codigoOperacion=="3"){
                            jsp_alerta("A",'<%=meLanbide48I18n.getMensaje(idiomaUsuario,"error.pasoParametrosEliminar")%>');
                        }else{
                            jsp_alerta("A",'<%=meLanbide48I18n.getMensaje(idiomaUsuario,"error.errorEliminarGen")%>');
                        }//if(
                    }
                    catch(Err){
                        jsp_alerta("A",'<%=meLanbide48I18n.getMensaje(idiomaUsuario,"error.errorProcesarRespuesta")%>');
                    }//try-catch
                }
            } 
            else {
                jsp_alerta('A', '<%=meLanbide48I18n.getMensaje(idiomaUsuario, "msg.msjNoSelecFila")%>');
            }
            //barraProgresoColec('off', 'barraProgresoEntidadesSolicitudColec');
    }
    function recargarTablaEntidadesColec(result){
        recargarTablaEntidadesColec1(result,null);
    }
    function recargarTablaEntidadesColec1(result,actualizaTotal){
        var fila;
        //var listaEntidadesColec = new Array();
        listaEntidadesColec = new Array();
        listaEntidadesColecTabla = new Array();
        //listaEntidadesColecTabla_titulos = new Array();
        //listaEntidadesColecTabla_estilos = new Array();
        
        for(var i = 1;i< result.length; i++){
            fila = result[i];
            var porcentaje1=reemplazarTextoColec(fila[10],'.',',');
            listaEntidadesColec[i-1] = [fila[0],fila[1],fila[2],fila[3],fila[4],fila[5],fila[6],fila[7],fila[8],fila[9],fila[10],fila[11],fila[12]];
            listaEntidadesColecTabla[i-1] = [fila[4], fila[5], fila[6], fila[7],fila[8],fila[9],fila[11],fila[12]];
        }
        
        tabEntidadColec = new TablaColec(true,'<%=descriptor.getDescripcion("buscar")%>','<%=descriptor.getDescripcion("anterior")%>','<%=descriptor.getDescripcion("siguiente")%>','<%=descriptor.getDescripcion("mosFilasPag")%>','<%=descriptor.getDescripcion("msgNoResultBusq")%>','<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>','<%=descriptor.getDescripcion("filtrDeTotal")%>','<%=descriptor.getDescripcion("primero")%>','<%=descriptor.getDescripcion("ultimo")%>',document.getElementById('listaEntidadesColec'),855,25); 
        tabEntidadColec.addColumna('100','center',"<%= meLanbide48I18n.getMensaje(idiomaUsuario,"label.entidad.tabla.col1")%>", "<%= meLanbide48I18n.getMensaje(idiomaUsuario,"label.entidad.tabla.col1.title")%>");    
        tabEntidadColec.addColumna('300','left',"<%= meLanbide48I18n.getMensaje(idiomaUsuario,"label.entidad.tabla.col2")%>", "<%= meLanbide48I18n.getMensaje(idiomaUsuario,"label.entidad.tabla.col2.title")%>");   
        //tabEntidadColec.addColumna('90','right',"< %= meLanbide48I18n.getMensaje(idiomaUsuario,"label.entidad.tabla.col3")%>", "<%= meLanbide48I18n.getMensaje(idiomaUsuario,"label.entidad.tabla.col3.title")%>");   
        tabEntidadColec.addColumna('90','center',"<%= meLanbide48I18n.getMensaje(idiomaUsuario,"label.entidad.tabla.col4")%>", "<%= meLanbide48I18n.getMensaje(idiomaUsuario,"label.entidad.tabla.col4.title")%>");   
        tabEntidadColec.addColumna('90','center',"<%= meLanbide48I18n.getMensaje(idiomaUsuario,"label.entidad.tabla.col5")%>", "<%= meLanbide48I18n.getMensaje(idiomaUsuario,"label.entidad.tabla.col5.title")%>");   
        tabEntidadColec.addColumna('90','center',"<%= meLanbide48I18n.getMensaje(idiomaUsuario,"label.entidad.tabla.col6")%>", "<%= meLanbide48I18n.getMensaje(idiomaUsuario,"label.entidad.tabla.col6.title")%>");   
        tabEntidadColec.addColumna('90','center',"<%= meLanbide48I18n.getMensaje(idiomaUsuario,"label.entidad.tabla.col7")%>", "<%= meLanbide48I18n.getMensaje(idiomaUsuario,"label.entidad.tabla.col7.title")%>");   
        tabEntidadColec.addColumna('90','center',"<%= meLanbide48I18n.getMensaje(idiomaUsuario,"label.entidad.tabla.col8")%>", "<%= meLanbide48I18n.getMensaje(idiomaUsuario,"label.entidad.tabla.col8.title")%>");   
        tabEntidadColec.addColumna('90','center',"<%= meLanbide48I18n.getMensaje(idiomaUsuario,"label.entidad.tabla.col9")%>", "<%= meLanbide48I18n.getMensaje(idiomaUsuario,"label.entidad.tabla.col9.title")%>");   

        tabEntidadColec.lineas = listaEntidadesColecTabla;
        tabEntidadColec.displayCabecera=true;
        tabEntidadColec.height = 156;
        tabEntidadColec.displayTabla();
        // Se actualiza el valor por defecto (viene a null 2 parametro) o que sea 1 (true)
        if(actualizaTotal==null || "1"==actualizaTotal){
            actualizarTotalEntidadesAgrup();
        }
    }
    function actualizarTotalEntidadesAgrup(){
        if(listaEntidadesColecTabla!=null){
            if(document.getElementById('numeroTotalEntAgrupacion')!=null){
                if(listaEntidadesColec.length!=document.getElementById('numeroTotalEntAgrupacion').value){
                    document.getElementById('numeroTotalEntAgrupacion').value=listaEntidadesColec.length;
                }
            }
        }
    }
    function comprobarIgualTotalEntTabblayCampo(){
        if(listaEntidadesColecTabla!=null){
            if(document.getElementById('numeroTotalEntAgrupacion')!=null){
                if(listaEntidadesColec.length!=document.getElementById('numeroTotalEntAgrupacion').value){
                    return false;
                }
            }
        }
        return true;
    }
    
    function extraerListadoEntidadesColec(nodos){
            var elemento = nodos[0];
            var hijos = elemento.childNodes;
            var codigoOperacion = null;
            var listaEntidades = new Array();
            var fila = new Array();
            var nodoFila;
            var hijosFila;
            var nodoCampo;
            var j;
            
            for(j=0;hijos!=null && j<hijos.length;j++){
                if(hijos[j].nodeName=="CODIGO_OPERACION"){                            
                    codigoOperacion = hijos[j].childNodes[0].nodeValue;
                    listaEntidades[j] = codigoOperacion;
                }
                else if(hijos[j].nodeName=="FILA"){
                    nodoFila = hijos[j];
                    hijosFila = nodoFila.childNodes;
                    for(var cont = 0; cont < hijosFila.length; cont++){
                        if(hijosFila[cont].nodeName=="COLEC_ENT_COD"){
                            nodoCampo = hijosFila[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[0] = nodoCampo.childNodes[0].nodeValue;
                            }
                            else{
                                fila[0] = '-';
                            }
                        }
                        else if(hijosFila[cont].nodeName=="COLEC_ENT_AGRUP_COD"){
                            nodoCampo = hijosFila[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[1] = nodoCampo.childNodes[0].nodeValue;
                            }
                            else{
                                fila[1] = '-';
                            }
                        }
                        else if(hijosFila[cont].nodeName=="COLEC_NUMEXP"){
                            nodoCampo = hijosFila[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[2] = nodoCampo.childNodes[0].nodeValue;
                            }
                            else{
                                fila[2] = '-';
                            }
                        }
                        else if(hijosFila[cont].nodeName=="COLEC_ENT_TIPO_CIF"){
                            nodoCampo = hijosFila[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[3] = nodoCampo.childNodes[0].nodeValue;
                            }
                            else{
                                fila[3] = '-';
                            }
                        }
                        else if(hijosFila[cont].nodeName=="COLEC_ENT_CIF"){
                            nodoCampo = hijosFila[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[4] = nodoCampo.childNodes[0].nodeValue;
                            }
                            else{
                                fila[4] = '-';
                            }
                        }
                        else if(hijosFila[cont].nodeName=="COLEC_ENT_NOMBRE"){
                            nodoCampo = hijosFila[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[5] = nodoCampo.childNodes[0].nodeValue;
                            }
                            else{
                                fila[5] = '-';
                            }
                        }
                        else if(hijosFila[cont].nodeName=="COLEC_ENT_CENTESPEMPTH"){
                            nodoCampo = hijosFila[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[6] = nodoCampo.childNodes[0].nodeValue;
                                if (fila[6] == '1') {
                                   fila[6] = '<%=meLanbide48I18n.getMensaje(idiomaUsuario, "label.si").toUpperCase()%>';
                               } else if (fila[6] == '0') {
                                   fila[6] = '<%=meLanbide48I18n.getMensaje(idiomaUsuario, "label.no").toUpperCase()%>';
                               }
                            }
                            else{
                                fila[6] = '-';
                            }
                        }
                        else if(hijosFila[cont].nodeName=="COLEC_ENT_PARTMAYCEETH"){
                            nodoCampo = hijosFila[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[7] = nodoCampo.childNodes[0].nodeValue;
                                if (fila[7] == '1') {
                                   fila[7] = '<%=meLanbide48I18n.getMensaje(idiomaUsuario, "label.si").toUpperCase()%>';
                               } else if (fila[7] == '0') {
                                   fila[7] = '<%=meLanbide48I18n.getMensaje(idiomaUsuario, "label.no").toUpperCase()%>';
                               }                            }
                            else{
                                fila[7] = '-';
                            }
                        }
                        else if(hijosFila[cont].nodeName=="COLEC_ENT_EMPINSERCIONTH"){
                            nodoCampo = hijosFila[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[8] = nodoCampo.childNodes[0].nodeValue;
                                if (fila[8] == '1') {
                                   fila[8] = '<%=meLanbide48I18n.getMensaje(idiomaUsuario, "label.si").toUpperCase()%>';
                               } else if (fila[8] == '0') {
                                   fila[8] = '<%=meLanbide48I18n.getMensaje(idiomaUsuario, "label.no").toUpperCase()%>';
                               }                            }
                            else{
                                fila[8] = '-';
                            }
                        }
                        else if(hijosFila[cont].nodeName=="COLEC_ENT_PROMOEMPINSTH"){
                            nodoCampo = hijosFila[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[9] = nodoCampo.childNodes[0].nodeValue;
                                if (fila[9] == '1') {
                                   fila[9] = '<%=meLanbide48I18n.getMensaje(idiomaUsuario, "label.si").toUpperCase()%>';
                               } else if (fila[9] == '0') {
                                   fila[9] = '<%=meLanbide48I18n.getMensaje(idiomaUsuario, "label.no").toUpperCase()%>';
                               }                            
                            }
                            else{
                                fila[9] = '-';
                            }
                        }
                        else if(hijosFila[cont].nodeName=="COLEC_ENT_PORCEN_COMPROM_REALI"){
                            nodoCampo = hijosFila[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[10] = nodoCampo.childNodes[0].nodeValue;
                            }else{
                                fila[10] = '-';
                            }
                        }
                        else if(hijosFila[cont].nodeName=="PLANIGUALDAD"){
                            nodoCampo = hijosFila[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[11] = nodoCampo.childNodes[0].nodeValue;
                            }else{
                                fila[11] = '-';
                            }
                        }
                        else if(hijosFila[cont].nodeName=="CERTIFICADOCALIDAD"){
                            nodoCampo = hijosFila[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[12] = nodoCampo.childNodes[0].nodeValue;
                            }else{
                                fila[12] = '-';
                            }
                        }
                    }
                    listaEntidades[j] = fila;
                    fila = new Array();   
                }
        } 
        return listaEntidades;
    }
    
    function dblClckTablaEntidadesColec(rowID,tableName){
        //pulsarModificarEntidadColec();
    }
    
    function cambiaEntidad() {
        var esAsociacion = (document.getElementById('esAsociacionS').checked ? "S" : (document.getElementById('esAsociacionN').checked ? "N" : ""));
                if ("S" == esAsociacion) {
            document.getElementById('listaEntidadesColec').style.display = "block";
            //document.getElementById('fieldsetDatosEntidades').style.visibility = "visible";
            $("#fieldsetDatosEntidades").show();
        } else if ("N" == esAsociacion) {
            document.getElementById('listaEntidadesColec').style.display = "none";
            //document.getElementById('fieldsetDatosEntidades').style.visibility = "hidden";
            $("#fieldsetDatosEntidades").hide();
        } else {
            document.getElementById('listaEntidadesColec').style.display = "none";
            //document.getElementById('fieldsetDatosEntidades').style.visibility = "hidden";
            $("#fieldsetDatosEntidades").hide();
        }
    }
    
    function buscarTercero(){
        if(tipoDoc != '' && numDoc != ''){
            var ajax = getXMLHttpRequest();
            var nodos = null;
            var CONTEXT_PATH = '<%=request.getContextPath()%>'
            var url = CONTEXT_PATH + '/PeticionModuloIntegracion.do';

            var parametros = '';
            var tipoDoc = document.getElementById('codNifContratado').value;
            var numDoc = document.getElementById('txtNifOferta').value;
            var control = new Date();
            parametros = 'tarea=preparar&modulo=MELANBIDE48&operacion=busquedaTercero&tipo=0&numero=<%=numExpediente%>'
                +'&tipoDoc='+tipoDoc+'&numDoc='+numDoc+'&control='+control.getTime();
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
                var j;
                nodos = xmlDoc.getElementsByTagName("RESPUESTA");
                var elemento = nodos[0];
                var hijos = elemento.childNodes;
                var codigoOperacion = null;
                var nombre = null;
                var hayDatosTercero = false;
                var hayErrores = false;
                var nodoTerceros = null;
                var terceros = null;
                var hijosTercero = null;
                var nodoErrores = null;
                var hijosErrores = null;
                var nodoCampo = null;
                var errores = null;

                for(j=0;hijos!=null && j<hijos.length;j++){
                    if(hijos[j].nodeName=="CODIGO_OPERACION"){                            
                        codigoOperacion = hijos[j].childNodes[0].nodeValue;
                    }else if(hijos[j].nodeName=="RESULTADOS"){
                        hayDatosTercero = true;
                        nodoTerceros = hijos[j];
                        terceros = nodoTerceros.childNodes;
                        for(var cont2 = 0; cont2 < terceros.length; cont2++){
                            hijosTercero = terceros[cont2].childNodes;
                            for(var cont = 0; cont < hijosTercero.length; cont++){
                                 if(hijosTercero[cont].nodeName=="NOMBRE"){
                                    nodoCampo = hijosTercero[cont];
                                    if(nodoCampo.childNodes.length > 0){
                                        nombre = nodoCampo.childNodes[0].nodeValue;
                                    }
                                    else{
                                        nombre = '';
                                    }
                                }
                            }
                        }
                    }else if(hijos[j].nodeName=="ERRORES"){
                        hayErrores = true;
                        nodoErrores = hijos[j];
                        hijosErrores = nodoErrores.childNodes;
                        errores = "Ha habido errores al buscar terceros:\n\n"
                        for(var cont = 0; cont < hijosErrores.length; cont++){
                            if(hijosErrores[cont].nodeName=="ERROR"){
                                nodoCampo = hijosErrores[cont];
                                if(nodoCampo.childNodes.length > 0){
                                    errores += "- "+nodoCampo.childNodes[0].nodeValue + '\n';
                                }
                            }
                        }
                    }
                }

                //barraProgresoColec('off', 'barraProgresoEntidadesSolicitudColec');

                if(codigoOperacion=="0"){
                    if(!hayErrores){
                        if(hayDatosTercero){
                            //Establecemos los valores que nos ha devuelto el WS
                            document.getElementById('txtNombre').value = nombre;
                        }else{
                            //Establecemos los valores que nos ha devuelto el WS
                            document.getElementById('txtNombre').value = '';
                        }

                    }else{
                        jsp_alerta("A",errores);
                    }
                }else{
                    jsp_alerta("A",'<%=meLanbide48I18n.getMensaje(idiomaUsuario,"error.errorBusqTercero")%>');
                }
            }
            catch(err){
                jsp_alerta("A",'<%=meLanbide48I18n.getMensaje(idiomaUsuario,"error.errorProcesarRespuestaGen")%>');
            }//try-catch
        }else{
            //Limpiamos el resto
            document.getElementById('txtNombre').value = '';

        }
    }

    function cargarDescripcionesCombos(){
        var desc = "";
        var codAct = "";
        var codigo = "";

        //Nif
        <%--codigo = '<%=entidadModif != null && entidadModif.getCodTipoNif() != null ? entidadModif.getCodTipoNif() : ""%>';--%>
        codigo = '';
        desc = '';
        encontrado = false;
        i = 0; 
        if(codigo != null && codigo != '')
        {
            while(i<codNif.length && !encontrado)
            {
                codAct = codNif[i];
                if(codAct == codigo)
                {
                    encontrado = true;
                    desc = descNif[i];
                }
                else
                    {
                        i++;
                    }
            }
        }
        //document.getElementById('descNifContratado').value = desc;
    }

    function cargarCombos(){    
        //comboNifRepresentante.addItems(codNif, descNif);
    }
    
    function validarDatosColec(){
        var correcto = true;
        var numeroTotalEnts = document.getElementById('numeroTotalEntAgrupacion').value;
        if(numeroTotalEnts != null && numeroTotalEnts != ''){
            if(!validarNumericoColec(document.getElementById('numeroTotalEntAgrupacion'),3)){
                document.getElementById('numeroTotalEntAgrupacion').style.border = '1px solid red';
                mensajeValidacion = '<%=meLanbide48I18n.getMensaje(idiomaUsuario, "msg.entidad.numeroTotal.Entidad.Asociacion")%>';
                return false;
            }
        }
        return correcto;
    }
        
    function guardarDatosColec(){
        if(validarDatosColec()){
            var resultado = "1";
            if(!comprobarIgualTotalEntTabblayCampo()){
                //var mensaje1 = "'< %=meLanbide48I18n.getMensaje(idiomaUsuario, "msg.preguntaGuardar.DiferentesDatos.NroEntidades")%>'";
                //mensaje1=mensaje1.replace("\"\"","'< %=meLanbide48I18n.getMensaje(idiomaUsuario,"label.entidad.numero.total.entidades.asociacion")%>'");
                resultado = jsp_alerta('', '<%=meLanbide48I18n.getMensaje(idiomaUsuario, "msg.preguntaGuardar.DiferentesDatos.NroEntidades")%>');
            }
            if(resultado=="1"){
                //document.getElementById('msgGuardandoDatos').style.display="inline";
                //barraProgresoColec('on', 'barraProgresoEntidadesSolicitudColec');
                var ajax = getXMLHttpRequest();
                var nodos = null;
                var CONTEXT_PATH = '<%=request.getContextPath()%>'
                var url = CONTEXT_PATH + "/PeticionModuloIntegracion.do";
                var parametros = "";
                var control = new Date();
                var listaEntidades = new Array();

                parametros = 'tarea=preparar&modulo=MELANBIDE48&operacion=guardarDatosColec&tipo=0&numero=<%=numExpediente%>'
                    //+'&codNifRepresentante='+document.getElementById('codNifContratado').value
                    //+'&nifRepresentante='+document.getElementById('txtNifOferta').value
                    //+'&nombreRepresentante='+document.getElementById('txtNombre').value
                    +'&codEntidad=<%=(entidad!=null&&entidad.getCodEntidad()!=null?entidad.getCodEntidad():"")%>'
                    +'&cif='+(document.getElementById('cifEntidad')!=null? document.getElementById('cifEntidad').value:"")
                    +'&nombre='+(document.getElementById('nombreEntidad')!=null?escape(document.getElementById('nombreEntidad').value):"")
                    +'&esAsociacion='+(document.getElementById('esAsociacionS').checked ? 1 : document.getElementById('esAsociacionN').checked ? 0 : null)
                    +'&esAsociacionValidado='+(document.getElementById('esAsociacionSValidado').checked ? 1 : document.getElementById('esAsociacionNValidado').checked ? 0 : null)
                    +'&centroEspecialEmpTH='+(document.getElementById('esEspecialS').checked ? 1 : document.getElementById('esEspecialN').checked ? 0 : null)
                    +'&centroEspecialEmpTHValidado='+(document.getElementById('esEspecialSValidado').checked ? 1 : document.getElementById('esEspecialNValidado').checked ? 0 : null)
                    +'&particiMayorCentEspeEmpTH='+(document.getElementById('esEspecialPartMayorS').checked ? 1 : document.getElementById('esEspecialPartMayorN').checked ? 0 : null)
                    +'&particiMayorCentEspeEmpTHValidado='+(document.getElementById('esEspecialPartMayorSValidado').checked ? 1 : document.getElementById('esEspecialPartMayorNValidado').checked ? 0 : null)
                    +'&empresaInsercionTH='+(document.getElementById('esInsercionS').checked ? 1 : document.getElementById('esInsercionN').checked ? 0 : null)
                    +'&empresaInsercionTHValidado='+(document.getElementById('esInsercionSValidado').checked ? 1 : document.getElementById('esInsercionNValidado').checked ? 0 : null)
                    +'&promotoraEmprInsercionTH='+(document.getElementById('esInsercionPromotoraS').checked ? 1 : document.getElementById('esInsercionPromotoraN').checked ? 0 : null)
                    +'&promotoraEmprInsercionTHValidado='+(document.getElementById('esInsercionPromotoraSValidado').checked ? 1 : document.getElementById('esInsercionPromotoraNValidado').checked ? 0 : null)
                    +'&planIgualdad='+(document.getElementById('planIgualdadS') != null && document.getElementById('planIgualdadS')!=undefined && document.getElementById('planIgualdadS').checked ? 1 : (document.getElementById('planIgualdadN') != null && document.getElementById('planIgualdadN') != undefined && document.getElementById('planIgualdadN').checked) ? 0 : null)
                    +'&planIgualdadValidado='+(document.getElementById('planIgualdadSValidado')!=null && document.getElementById('planIgualdadSValidado') != undefined && document.getElementById('planIgualdadSValidado').checked ? 1 : (document.getElementById('planIgualdadNValidado')!= null && document.getElementById('planIgualdadNValidado')!= undefined &&  document.getElementById('planIgualdadNValidado').checked) ? 0 : null)
                    +'&certificadoCalidad='+(document.getElementById('certificadoCalidadS') != null && document.getElementById('certificadoCalidadS')!= undefined && document.getElementById('certificadoCalidadS').checked ? 1 : (document.getElementById('certificadoCalidadN') != null && document.getElementById('certificadoCalidadN')!= undefined &&  document.getElementById('certificadoCalidadN').checked) ? 0 : null)
                    +'&certificadoCalidadValidado='+(document.getElementById('certificadoCalidadSValidado') != null && document.getElementById('certificadoCalidadSValidado') != undefined && document.getElementById('certificadoCalidadSValidado').checked ? 1 : document.getElementById('certificadoCalidadNValidado')!= null && document.getElementById('certificadoCalidadNValidado')!= undefined && document.getElementById('certificadoCalidadNValidado').checked ? 0 : null)
                    +'&aceptaNumeroSuperiorHoras='+(document.getElementById('aceptaNumeroSuperiorHorasS').checked ? 1 : document.getElementById('aceptaNumeroSuperiorHorasN').checked ? 0 : null)
                    +'&segundosLocalesMismoAmbito='+(document.getElementById('segundosLocalesMismoAmbitoS').checked ? 1 : document.getElementById('segundosLocalesMismoAmbitoN').checked ? 0 : null)
                    +'&segundosLocalesMismoAmbitoValidado='+(document.getElementById('segundosLocalesMismoAmbitoSValidado').checked ? 1 : document.getElementById('segundosLocalesMismoAmbitoNValidado').checked ? 0 : null)
                    +'&numeroTotalEntAgrupacion='+(document.getElementById('numeroTotalEntAgrupacion')!=null ? document.getElementById('numeroTotalEntAgrupacion').value : "null")
                    // Nuevos campos convocaroria
                    +'&entSinAnimoLucro='+(document.getElementById('entSinAnimoLucroS')!=null && document.getElementById('entSinAnimoLucroS') != undefined && document.getElementById('entSinAnimoLucroS').checked ? 1 : (document.getElementById('entSinAnimoLucroN')!= null && document.getElementById('entSinAnimoLucroN')!= undefined &&  document.getElementById('entSinAnimoLucroN').checked) ? 0 : null)
                    +'&sujetoDerPublico='+(document.getElementById('sujetoDerPublicoS')!=null && document.getElementById('sujetoDerPublicoS') != undefined && document.getElementById('sujetoDerPublicoS').checked ? 1 : (document.getElementById('sujetoDerPublicoN')!= null && document.getElementById('sujetoDerPublicoN')!= undefined &&  document.getElementById('sujetoDerPublicoN').checked) ? 0 : null)
                    +'&compIgualdadOpcion='+getValorSeleccionadoCompromisoIgualdad()
                    +'&certificadoCalidadLista='+getValorSeleccionadoCertificadoCalidad()
                    // Datos validados
                    +'&entSinAnimoLucroValidado='+(document.getElementById('entidadSinAnimoLucroSValidado')!=null && document.getElementById('entidadSinAnimoLucroSValidado') != undefined && document.getElementById('entidadSinAnimoLucroSValidado').checked ? 1 : (document.getElementById('entidadSinAnimoLucroNValidado')!= null && document.getElementById('entidadSinAnimoLucroNValidado')!= undefined &&  document.getElementById('entidadSinAnimoLucroNValidado').checked) ? 0 : null)
                    +'&entSujetaDerPublValidado='+(document.getElementById('entSujetaDerPublSValidado')!=null && document.getElementById('entSujetaDerPublSValidado') != undefined && document.getElementById('entSujetaDerPublSValidado').checked ? 1 : (document.getElementById('entSujetaDerPublNValidado')!= null && document.getElementById('entSujetaDerPublNValidado')!= undefined &&  document.getElementById('entSujetaDerPublNValidado').checked) ? 0 : null)
                    +'&compromisoIgualdadValidado='+getValorSeleccionadoCompromisoIgualdadValidado()
                    +'&certificadoCalidadListaValidado='+getValorSeleccionadoCertificadoCalidadValidado()
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
                    listaEntidades = extraerListadoEntidadesColec(nodos);
                    recargarTablaEntidadesColec1(listaEntidades,"0");
                    var codigoOperacion = listaEntidades[0];
                    if(codigoOperacion=="0"){
                        //window.returnValue =  listaEntidades;
                        //barraProgresoColec('off', 'barraProgresoEntidadesSolicitudColec');
                        jsp_alerta("A",'<%=meLanbide48I18n.getMensaje(idiomaUsuario,"msg.entidad.guardadaOK")%>');
                        refrescarPestanasColec()
                    }else if(codigoOperacion=="1"){
                        jsp_alerta("A",'<%=meLanbide48I18n.getMensaje(idiomaUsuario,"error.errorBD")%>');
                    }else if(codigoOperacion=="2"){
                        jsp_alerta("A",'<%=meLanbide48I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                    }else if(codigoOperacion=="3"){
                        jsp_alerta("A",'<%=meLanbide48I18n.getMensaje(idiomaUsuario,"error.pasoParametros")%>');
                    }else if(codigoOperacion=="4"){
                        jsp_alerta("A",'<%=meLanbide48I18n.getMensaje(idiomaUsuario,"error.datosNoValidos")%>');
                    }else{
                        jsp_alerta("A",'<%=meLanbide48I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                    }//if(
                }
                catch(Err){
                    jsp_alerta("A",'<%=meLanbide48I18n.getMensaje(idiomaUsuario,"error.errorProcesarRespuesta")%>');
                }//try-catch
                //barraProgresoColec('off', 'barraProgresoEntidadesSolicitudColec');
            }
        }else{
            jsp_alerta("A", escape(mensajeValidacion));
        }
    }


    
    function inicio() {
        //cargarDescripcionesCombos();

        //Datos representante
        //comboNifRepresentante = new Combo('NifContratado');
        //comboNifRepresentante.change = buscarTercero;
        
        <%
        if (entidad != null) {
            if(entidad.getEsAgrupacion() != null && entidad.getEsAgrupacion().equals(1))
            {
            %>
                document.getElementById('esAsociacionS').checked = 'true';                                
            <%
            }
            else if(entidad.getEsAgrupacion() != null && entidad.getEsAgrupacion().equals(0))
            {
            %>
                document.getElementById('esAsociacionN').checked = 'true'; 
            <%
            }
            %>
            <%
            if(entidad.getCentroEspEmpTH() != null && entidad.getCentroEspEmpTH().equals(1))
            {
            %>
                document.getElementById('esEspecialS').checked = 'true';                                
            <%
            }
            else if(entidad.getCentroEspEmpTH() != null && entidad.getCentroEspEmpTH().equals(0))
            {
            %>
                document.getElementById('esEspecialN').checked = 'true'; 
            <%
            }
            %>
            <%
            if(entidad.getParticipanteMayorCentEcpEmpTH() != null && entidad.getParticipanteMayorCentEcpEmpTH().equals(1))
            {
            %>
                document.getElementById('esEspecialPartMayorS').checked = 'true';                                
            <%
            }
            else if(entidad.getParticipanteMayorCentEcpEmpTH() != null && entidad.getParticipanteMayorCentEcpEmpTH().equals(0))
            {
            %>
                document.getElementById('esEspecialPartMayorN').checked = 'true'; 
            <%
            }
            %>
            <%
            if(entidad.getEmpresaInsercionTH() != null && entidad.getEmpresaInsercionTH().equals(1))
            {
            %>
                document.getElementById('esInsercionS').checked = 'true';                                
            <%
            }
            else if(entidad.getEmpresaInsercionTH() != null && entidad.getEmpresaInsercionTH().equals(0))
            {
            %>
                document.getElementById('esInsercionN').checked = 'true'; 
            <%
            }
            %>
            <%
            if(entidad.getPromotorEmpInsercionTH() != null && entidad.getPromotorEmpInsercionTH().equals(1))
            {
            %>
                document.getElementById('esInsercionPromotoraS').checked = 'true';                                
            <%
            }
            else if(entidad.getPromotorEmpInsercionTH() != null && entidad.getPromotorEmpInsercionTH().equals(0))
            {
            %>
                document.getElementById('esInsercionPromotoraN').checked = 'true'; 
            <%
            }
            %>
            <%
            if(entidad.getPlanIgualdad() != null && entidad.getPlanIgualdad().equals(1))
            {
            %>
                if(document.getElementById('planIgualdadS')!=null && document.getElementById('planIgualdadS')!= undefined){
                    document.getElementById('planIgualdadS').checked = 'true';
                }
            <%
            }
            else if(entidad.getPlanIgualdad() != null && entidad.getPlanIgualdad().equals(0))
            {
            %>
                if(document.getElementById('planIgualdadN')!=null && document.getElementById('planIgualdadN')!= undefined){
                    document.getElementById('planIgualdadN').checked = 'true';
                }
            <%
            }
            %>
            <%
            if(entidad.getCertificadoCalidad() != null && entidad.getCertificadoCalidad().equals(1))
            {
            %>
                if(document.getElementById('certificadoCalidadS')!=null && document.getElementById('certificadoCalidadS')!= undefined){
                    document.getElementById('certificadoCalidadS').checked = 'true';
                }
            <%
            }
            else if(entidad.getCertificadoCalidad() != null && entidad.getCertificadoCalidad().equals(0))
            {
            %>
                if(document.getElementById('certificadoCalidadN')!=null && document.getElementById('certificadoCalidadN')!= undefined){
                    document.getElementById('certificadoCalidadN').checked = 'true';
                }
            <%
            }
            %>
            <%
            if(entidad.getAceptaNumeroSuperiorHoras() != null && entidad.getAceptaNumeroSuperiorHoras().equals(1))
            {
            %>
                document.getElementById('aceptaNumeroSuperiorHorasS').checked = 'true';                                
            <%
            }
            else if(entidad.getAceptaNumeroSuperiorHoras() != null && entidad.getAceptaNumeroSuperiorHoras().equals(0))
            {
            %>
                document.getElementById('aceptaNumeroSuperiorHorasN').checked = 'true'; 
            <%
            }
            %>
            <%
            if(entidad.getSegundosLocalesMismoAmbito() != null && entidad.getSegundosLocalesMismoAmbito().equals(1))
            {
            %>
                document.getElementById('segundosLocalesMismoAmbitoS').checked = 'true';                                
            <%
            }
            else if(entidad.getSegundosLocalesMismoAmbito() != null && entidad.getSegundosLocalesMismoAmbito().equals(0))
            {
            %>
                document.getElementById('segundosLocalesMismoAmbitoN').checked = 'true'; 
            <%
            }
        }
        %>
        /*< %
            if(representante != null) {
        % >
            document.getElementById('codNifContratado').value = '< %=representante.getCodTipoDoc() != null ? representante.getCodTipoDoc().toUpperCase() : "" %>';
            document.getElementById('txtNifOferta').value = '< %=representante.getDocumento() != null ? representante.getDocumento(): "" %>';
            document.getElementById('txtNombre').value = '< %=representante.getNombre() != null ? representante.getNombre(): "" %>';
        < %
            }
        % >*/
        cargarCombos();
        cambiaEntidad();
    }
    
    function copiarDatosPresenSolicitudToValidar(){
        $("#esEspecialSValidado").prop("checked",$("#esEspecialS").prop("checked"));
        $("#esEspecialNValidado").prop("checked",$("#esEspecialN").prop("checked"));
        $("#esEspecialPartMayorSValidado").prop("checked",$("#esEspecialPartMayorS").prop("checked"));
        $("#esEspecialPartMayorNValidado").prop("checked",$("#esEspecialPartMayorN").prop("checked"));
        $("#esInsercionSValidado").prop("checked",$("#esInsercionS").prop("checked"));
        $("#esInsercionNValidado").prop("checked",$("#esInsercionN").prop("checked"));
        $("#esInsercionPromotoraSValidado").prop("checked",$("#esInsercionPromotoraS").prop("checked"));
        $("#esInsercionPromotoraNValidado").prop("checked",$("#esInsercionPromotoraN").prop("checked"));
        if($("#planIgualdadSValidado")!= undefined)
            $("#planIgualdadSValidado").prop("checked",$("#planIgualdadS").prop("checked"));
        if($("#planIgualdadNValidado")!= undefined)
            $("#planIgualdadNValidado").prop("checked",$("#planIgualdadN").prop("checked"));
        if($("#certificadoCalidadNValidado")!= undefined)
            $("#certificadoCalidadSValidado").prop("checked",$("#certificadoCalidadS").prop("checked"));
        if($("#certificadoCalidadNValidado")!= undefined)
            $("#certificadoCalidadNValidado").prop("checked",$("#certificadoCalidadN").prop("checked"));
        $("#esAsociacionSValidado").prop("checked",$("#esAsociacionS").prop("checked"));
        $("#esAsociacionNValidado").prop("checked",$("#esAsociacionN").prop("checked"));
        $("#segundosLocalesMismoAmbitoSValidado").prop("checked",$("#segundosLocalesMismoAmbitoS").prop("checked"));
        $("#segundosLocalesMismoAmbitoNValidado").prop("checked",$("#segundosLocalesMismoAmbitoN").prop("checked"));
        if($("#entSinAnimoLucroS")!= undefined)
            $("#entSinAnimoLucroS").prop("checked",$("#entidadSinAnimoLucroSValidado").prop("checked"));
        if($("#entSinAnimoLucroN")!= undefined)
            $("#entSinAnimoLucroN").prop("checked",$("#entidadSinAnimoLucroNValidado").prop("checked"));
        if($("#sujetoDerPublicoS")!= undefined)
            $("#sujetoDerPublicoS").prop("checked",$("#entSujetaDerPublSValidado").prop("checked"));
        if($("#sujetoDerPublicoN")!= undefined)
            $("#sujetoDerPublicoN").prop("checked",$("#entSujetaDerPublNValidado").prop("checked"));
        if($("#compIgualdadOpcionSValidado")!= undefined){
           if($("#tableCompromisoIgualdad input:checked") != undefined && $("#tableCompromisoIgualdad input:checked").length >0)
                $("#compIgualdadOpcionSValidado").prop("checked",true);
           else
                $("#compIgualdadOpcionNValidado").prop("checked",true);
        }
        // Campos Certificados de calidad
        $("#tableCertificadoCalidad input").each(function(indice,elemento){
            var sufijoIDElemento = elemento.id.substring(elemento.id.indexOf("_"));
            if($(elemento).prop("checked"))
                $("#certificadoCalidadOpcionValTS"+sufijoIDElemento).prop("checked",true);
            else
                $("#certificadoCalidadOpcionValTN"+sufijoIDElemento).prop("checked",true);
        });
    }

    function getValorSeleccionadoCompromisoIgualdad(){
        var respuesta = "";
        if($("#tableCompromisoIgualdad input:checked")!= null && $("#tableCompromisoIgualdad input:checked")!= undefined)
        {
            $("#tableCompromisoIgualdad input:checked").each(function(elementoIndex,elemento){
                respuesta=elemento.id.substring(elemento.id.indexOf("_")+1);
            });
        }
        return respuesta;
    }

    function getValorSeleccionadoCertificadoCalidad(){
        var respuesta = "";
        if($("#tableCertificadoCalidad input:checked")!= null && $("#tableCertificadoCalidad input:checked")!= undefined)
        {
            $("#tableCertificadoCalidad input:checked").each(function(elementoIndex,elemento){
                respuesta+= (respuesta != "" ? ",":"");
                respuesta+= elemento.id.substring(elemento.id.indexOf("_")+1);
            });
        }
        return respuesta;
    }

    function getValorSeleccionadoCompromisoIgualdadValidado(){
        var respuesta = "";
        if($("#tableCompromisoIgualdad input:checked")!= null && $("#tableCompromisoIgualdad input:checked")!= undefined)
        {
            $("#tableCompromisoIgualdad input:checked").each(function(elementoIndex,elemento){
                // Es solo una opcion,no hace falta leer el id, basta con saber si se valida o no
                //respuesta+= (respuesta != "" ? ",":"");
                respuesta+= //elemento.id.substring(elemento.id.indexOf("_")+1)  +"_"+ (
                    $("#compIgualdadOpcionSValidado") != null && $("#compIgualdadOpcionSValidado")!=undefined && $("#compIgualdadOpcionSValidado").prop("checked")
                        ? getValorSeleccionadoCompromisoIgualdad()
                        : ($("#compIgualdadOpcionNValidado") != null && $("#compIgualdadOpcionNValidado")!=undefined && $("#compIgualdadOpcionNValidado").prop("checked")?0:"")
                //)
                ;
            });
        }
        return respuesta;
    }

    function getValorSeleccionadoCertificadoCalidadValidado(){
        var respuesta = "";
        if($('[name^="certificadoCalidadOpcionValT"]input:checked')!= null && $('[name^="certificadoCalidadOpcionValT"]input:checked')!= undefined)
        {
            $('[name^="certificadoCalidadOpcionValT"]input:checked').each(function(i,campo){
                respuesta+= (respuesta != "" ? ",":"");
                respuesta+=campo.id.substring(campo.id.indexOf("_")+1)+"_"+ campo.value;
            });
        }
        return respuesta;
    }

</script>

<body>
    <div style="clear: both;">
        <div class="shadow p-3 mb-5 bg-light rounded etiqueta">
            <div class="form-group">
                <div class="lineaFormulario"> 
                    <div style="width: auto; float: left; margin-left: 10px;">
                        <div style="float: left;">
                            <div style="width: 150px; float: left; margin-right: 20px;">
                                <%=meLanbide48I18n.getMensaje(idiomaUsuario,"label.entidad.cif")%>
                            </div>
                            <input type="text" name="cifEntidad" id="cifEntidad" value="<%=(entidad!=null&&entidad.getCif()!=null?entidad.getCif():"")%>" class="inputTexto" size="12" />
                        </div>
                    </div>
                </div>
                <div class="lineaFormulario">
                    <div style="width: auto; float: left; margin-left: 10px;">
                        <div style="float: left;">

                            <div style="width: 150px; float: left; margin-right: 20px;">
                                <%=meLanbide48I18n.getMensaje(idiomaUsuario,"label.entidad.nombre")%>
                            </div>
                            <input type="text" name="nombreEntidad" id="nombreEntidad" value="<%=(entidad!=null&&entidad.getNombre()!=null?entidad.getNombre():"")%>" class="inputTexto" size="80" />
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="form-row">
            <div class="col">
                <h4 class="alert alert-success alert-heading"><%=meLanbide48I18n.getMensaje(idiomaUsuario,"legend.entidad.datos.solicitud")%></h4>
                <hr class="my-4">
            </div>
        </div>                
        <div class="form-row">
            <div class="col">
                <div class="lineaFormulario"> 
                    <div style="float: left; width: 80%; margin-left: 30px;">
                        <%=meLanbide48I18n.getMensaje(idiomaUsuario,"label.entidad.pregunta.especial")%>
                    </div>
                    <div style="width: auto; float: left; margin-left: 10px;">
                        <div style="float: left;">
                            <input type="radio" name="esEspecial" id="esEspecialS" value="S"><%=meLanbide48I18n.getMensaje(idiomaUsuario, "label.si")%>
                            <input type="radio" name="esEspecial" id="esEspecialN" value="N"><%=meLanbide48I18n.getMensaje(idiomaUsuario, "label.no")%>
                        </div>
                    </div>
                </div>
                <div class="lineaFormulario"> 
                    <div style="float: left; width: 80%; margin-left: 30px;">
                        <%=meLanbide48I18n.getMensaje(idiomaUsuario,"label.entidad.pregunta.especial.participante.mayor")%>
                    </div>
                    <div style="width: auto; float: left; margin-left: 10px;">
                        <div style="float: left;">
                            <input type="radio" name="esEspecialPartMayor" id="esEspecialPartMayorS" value="S"><%=meLanbide48I18n.getMensaje(idiomaUsuario, "label.si")%>
                            <input type="radio" name="esEspecialPartMayor" id="esEspecialPartMayorN" value="N"><%=meLanbide48I18n.getMensaje(idiomaUsuario, "label.no")%>
                        </div>
                    </div>

                </div>
                <div class="lineaFormulario"> 
                    <div style="float: left; width: 80%; margin-left: 30px;">
                        <%=meLanbide48I18n.getMensaje(idiomaUsuario,"label.entidad.pregunta.insercion")%>
                    </div>
                    <div style="width: auto; float: left; margin-left: 10px;">
                        <div style="float: left;">
                            <input type="radio" name="esInsercion" id="esInsercionS" value="S"><%=meLanbide48I18n.getMensaje(idiomaUsuario, "label.si")%>
                            <input type="radio" name="esInsercion" id="esInsercionN" value="N"><%=meLanbide48I18n.getMensaje(idiomaUsuario, "label.no")%>
                        </div>
                    </div>
                </div>
                <div class="lineaFormulario"> 
                    <div style="float: left; width: 80%; margin-left: 30px;">
                        <%=meLanbide48I18n.getMensaje(idiomaUsuario,"label.entidad.pregunta.insercion.promotor")%>
                    </div>
                    <div style="width: auto; float: left; margin-left: 10px;">
                        <div style="float: left;">
                            <input type="radio" name="esInsercionPromotora" id="esInsercionPromotoraS" value="S"><%=meLanbide48I18n.getMensaje(idiomaUsuario, "label.si")%>
                            <input type="radio" name="esInsercionPromotora" id="esInsercionPromotoraN" value="N"><%=meLanbide48I18n.getMensaje(idiomaUsuario, "label.no")%>
                        </div>
                    </div>
                </div>
                <c:choose>
                    <c:when test = "${convocatoriaActiva!=null && convocatoriaActiva.proCod=='COLEC' && convocatoriaActiva.id > 30}">
                         <div class="lineaFormulario">
                             <div style="float: left; width: 80%; margin-left: 30px;" id="lblEntSinAnimoLucro">
                                 <%=meLanbide48I18n.getMensaje(idiomaUsuario,"label.entidad.pregunta.entidad.sin.animo.lucro")%>
                             </div>
                             <div style="width: auto; float: left; margin-left: 10px;">
                                 <div style="float: left;">
                                     <input type="radio" name="entSinAnimoLucro" id="entSinAnimoLucroS" value="S" <c:if test="${entidad!=null && entidad.entSinAnimoLucro!=null && entidad.entSinAnimoLucro==1}">checked</c:if>><%=meLanbide48I18n.getMensaje(idiomaUsuario, "label.si")%>
                                     <input type="radio" name="entSinAnimoLucro" id="entSinAnimoLucroN" value="N" <c:if test="${entidad!=null && entidad.entSinAnimoLucro!=null && entidad.entSinAnimoLucro==0}">checked</c:if>><%=meLanbide48I18n.getMensaje(idiomaUsuario, "label.no")%>
                                 </div>
                             </div>
                         </div>
                         <div class="lineaFormulario">
                             <div style="float: left; width: 80%; margin-left: 30px;" id="lblSujetoDerPublico">
                                 <%=meLanbide48I18n.getMensaje(idiomaUsuario,"label.entidad.pregunta.sujeto.derecho.publico")%>
                             </div>
                             <div style="width: auto; float: left; margin-left: 10px;">
                                 <div style="float: left;">
                                     <input type="radio" name="sujetoDerPublico" id="sujetoDerPublicoS" value="S" <c:if test="${entidad!=null && entidad.entSujetaDerPubl!=null && entidad.entSujetaDerPubl==1}">checked</c:if>><%=meLanbide48I18n.getMensaje(idiomaUsuario, "label.si")%>
                                     <input type="radio" name="sujetoDerPublico" id="sujetoDerPublicoN" value="N" <c:if test="${entidad!=null && entidad.entSujetaDerPubl!=null && entidad.entSujetaDerPubl==0}">checked</c:if>><%=meLanbide48I18n.getMensaje(idiomaUsuario, "label.no")%>
                                 </div>
                             </div>
                         </div>
                         <div class="lineaFormulario">
                            <div style="float: left; width: 80%; margin-left: 30px;" id="lblCompromisoIgualdad">
                                <%=meLanbide48I18n.getMensaje(idiomaUsuario,"label.entidad.pregunta.compromiso.igualdad")%>
                            </div>
                            <div style="width: 80%; float: left; margin-left: 40px;">
                                <div style="float: left;">
                                    <table style="text-align:justify;" id="tableCompromisoIgualdad">
                                        <c:forEach items="${listaCompromisoIgualdad}" var="compromisoIgualdad" varStatus="contadorCI">
                                            <tr>
                                                <td style="padding:5px;"><input type="radio" name="compromisoIgualdad" id="compromisoIgualdad_<c:out value="${compromisoIgualdad.codigo}"/>" value="<c:out value="${compromisoIgualdad.codigo}"/>"
                                                <c:if test="${entidad!=null && entidad.compIgualdadOpcion!=null && entidad.compIgualdadOpcion==compromisoIgualdad.codigo}">checked</c:if>/></td>
                                                <td><span><c:out value = "${compromisoIgualdad.descripcion}"/></span></td>
                                            </tr>
                                        </c:forEach>
                                    </table>
                                </div>
                            </div>
                        </div>
                     </c:when>
                     <c:otherwise>
                        <div class="lineaFormulario">
                            <div style="float: left; width: 80%; margin-left: 30px;" id="lblPlanIgualdad">
                                <%=meLanbide48I18n.getMensaje(idiomaUsuario,"label.entidad.pregunta.plan.igualdad")%>
                            </div>
                            <div style="width: 80%; float: left; margin-left: 10px;">
                                <div style="float: left;">
                                    <input type="radio" name="planIgualdad" id="planIgualdadS" value="S"><%=meLanbide48I18n.getMensaje(idiomaUsuario, "label.si")%>
                                    <input type="radio" name="planIgualdad" id="planIgualdadN" value="N"><%=meLanbide48I18n.getMensaje(idiomaUsuario, "label.no")%>
                                </div>
                            </div>
                        </div>
                     </c:otherwise>
                </c:choose>
                <div class="lineaFormulario"> 
                    <c:choose>
                        <c:when test = "${convocatoriaActiva!=null && convocatoriaActiva.proCod=='COLEC' && convocatoriaActiva.id > 30}">
                            <div style="float: left; width: 80%; margin-left: 30px;" id="lblCertificadoCalidad">
                                <%=meLanbide48I18n.getMensaje(idiomaUsuario,"label.entidad.pregunta.certificado.calidad")%>
                            </div>
                            <div style="width: 80%; float: left; margin-left: 40px;">
                                <div style="float: left;">
                                    <table style="text-align:justify;" id="tableCertificadoCalidad">
                                        <c:forEach items="${listaCertificadosCalidad}" var="certificadoCalidad" varStatus="contadorCC">
                                            <tr>
                                                <td style="padding:5px;"><input type="checkbox" name="certificadoCalidad_<c:out value="${certificadoCalidad.codigo}"/>" id="certificadoCalidad_<c:out value="${certificadoCalidad.codigo}"/>" value="<c:out value="${certificadoCalidad.codigo}"/>"/></td>
                                                <td><span><c:out value = "${certificadoCalidad.descripcion}"/></span></td>
                                            </tr>
                                        </c:forEach>
                                    </table>
                                </div>
                            </div>
                        </c:when>
                        <c:otherwise>
                            <div style="float: left; width: 80%; margin-left: 30px;" id="lblCertificadoCalidad">
                                <%=meLanbide48I18n.getMensaje(idiomaUsuario,"label.entidad.pregunta.certificado.calidad")%>
                            </div>
                            <div style="width: auto; float: left; margin-left: 10px;">
                                <div style="float: left;">
                                    <input type="radio" name="certificadoCalidad" id="certificadoCalidadS" value="S"><%=meLanbide48I18n.getMensaje(idiomaUsuario, "label.si")%>
                                    <input type="radio" name="certificadoCalidad" id="certificadoCalidadN" value="N"><%=meLanbide48I18n.getMensaje(idiomaUsuario, "label.no")%>
                                </div>
                            </div>
                        </c:otherwise>
                    </c:choose>
                </div>
                <div class="lineaFormulario">    
                    <div style="float: left; width: 80%; margin-left: 30px;">
                        <%=meLanbide48I18n.getMensaje(idiomaUsuario,"label.entidad.pregunta.asociacion")%>
                    </div>            
                    <div style="width: auto; float: left; margin-left: 10px;">
                        <div style="float: left;">
                            <input type="radio" name="esAsociacion" id="esAsociacionS" value="S" onClick="cambiaEntidad()"><%=meLanbide48I18n.getMensaje(idiomaUsuario, "label.si")%>
                            <input type="radio" name="esAsociacion" id="esAsociacionN" value="N" onClick="cambiaEntidad()"><%=meLanbide48I18n.getMensaje(idiomaUsuario, "label.no")%>
                        </div>
                    </div>
                </div>
            </div>
        </div>                
    </div>
    <br />
    <div id="divFsDatosEntidades" style="clear: both;">
    <fieldset id="fieldsetDatosEntidades" name="fieldsetDatosEntidades" style="height: 260px; padding-top: 15px; border: none; display: block">
        <legend class="legendAzul"><%=meLanbide48I18n.getMensaje(idiomaUsuario,"legend.entidad.datosIntegrantes.Agrupacion")%></legend>
        <div style="clear: both;">
                <div id="listaEntidadesColec" name="listaEntidadesColec" style="font-size: 13px; overflow-y: scroll;height: 240px;"></div>
                <div style="clear: both;">
                    <div class="lineaFormulario" style="width: 875;">
                        <div style="float: left" >
                            <%=meLanbide48I18n.getMensaje(idiomaUsuario,"label.entidad.numero.total.entidades.asociacion")%>
                        </div>            
                        <div style="float: right" >
                            <div >
                                <input type="text" style="text-align: right" maxlength="3" size="4" class="inputTexto"name="numeroTotalEntAgrupacion" id="numeroTotalEntAgrupacion" value="<%=entidad!=null && entidad.getNumTotalEntAgrupacion()!=null?entidad.getNumTotalEntAgrupacion():""%>" />
                            </div>
                        </div>
                    </div>
                </div>
                <div class="botonera" id="botoneraEntidad" name="botoneraEntidad">
                    <input type="button" id="btnNuevaEntidad" name="btnNuevaEntidad" class="botonGeneral" value="<%=meLanbide48I18n.getMensaje(idiomaUsuario, "btn.alta")%>" onclick="pulsarAltaEntidadColec();">
                    <input type="button" id="btnModificarEntidad" name="btnModificarEntidad" class="botonGeneral" value="<%=meLanbide48I18n.getMensaje(idiomaUsuario, "btn.modificar")%>" onclick="pulsarModificarEntidadColec();">
                    <input type="button" id="btnEliminarEntidad" name="btnEliminarEntidad" class="botonGeneral" value="<%=meLanbide48I18n.getMensaje(idiomaUsuario, "btn.eliminar")%>" onclick="pulsarEliminarEntidadColec();">
                </div>
        </div>
    </fieldset>
    </div>
    <div style="clear: both;  margin-top: 100px;">
        <div class="jumbotron">
            <p><%=meLanbide48I18n.getMensaje(idiomaUsuario,"label.entidad.pregunta.acepta.realizacion.numero.superior.horas")%></p>
            <hr class="my-4">
            <div style="float: left;">
                <input type="radio" name="aceptaNumeroSuperiorHoras" id="aceptaNumeroSuperiorHorasS" value="S"><%=meLanbide48I18n.getMensaje(idiomaUsuario, "label.si")%>
                <input type="radio" name="aceptaNumeroSuperiorHoras" id="aceptaNumeroSuperiorHorasN" value="N"><%=meLanbide48I18n.getMensaje(idiomaUsuario, "label.no")%>
            </div>
        </div> 
        <div class="jumbotron">
            <p><%=meLanbide48I18n.getMensaje(idiomaUsuario,"label.entidad.pregunta.segundos.locales.mismo.ambito")%></p>
            <hr class="my-4">
            <div style="float: left;">
                <input type="radio" name="segundosLocalesMismoAmbito" id="segundosLocalesMismoAmbitoS" value="S"><%=meLanbide48I18n.getMensaje(idiomaUsuario, "label.si")%>
                <input type="radio" name="segundosLocalesMismoAmbito" id="segundosLocalesMismoAmbitoN" value="N"><%=meLanbide48I18n.getMensaje(idiomaUsuario, "label.no")%>
            </div>
        </div>
    </div> 
    <div class="container">
        <div class="form-row">
            <div class="col">
                <h4 class="alert alert-success alert-heading"><%=meLanbide48I18n.getMensaje(idiomaUsuario,"legend.entidad.datos.solicitud.validados")%></h4>
                <hr class="my-4">
                <ul class="nav nav-pills">
                    <li class="nav-item">
                        <a class="nav-link active" href="#" onclick="copiarDatosPresenSolicitudToValidar()"><%=meLanbide48I18n.getMensaje(idiomaUsuario,"legend.entidad.copiar.datos.solicitud.a.validar")%></a>
                    </li>
                </ul>
                <hr class="my-4">
            </div>
        </div>  
        <div class="form-row">
            <div class="col">
                <div class="row">
                    <%=meLanbide48I18n.getMensaje(idiomaUsuario,"label.entidad.pregunta.especial")%>
                </div>
                <div class="row">
                    <%=meLanbide48I18n.getMensaje(idiomaUsuario,"label.entidad.pregunta.especial.participante.mayor")%>
                </div>
                <div class="row">
                    <%=meLanbide48I18n.getMensaje(idiomaUsuario,"label.entidad.pregunta.insercion")%>
                </div>
                <div class="row">
                    <%=meLanbide48I18n.getMensaje(idiomaUsuario,"label.entidad.pregunta.insercion.promotor")%>
                </div>
                <div class="row">
                    <%=meLanbide48I18n.getMensaje(idiomaUsuario,"label.entidad.pregunta.asociacion")%>
                </div>
                <div class="row">
                    <%=meLanbide48I18n.getMensaje(idiomaUsuario,"label.ubicacion.valoracion.puntos.segundos.locales")%>
                </div>
                <c:choose>
                    <c:when test = "${convocatoriaActiva!=null && convocatoriaActiva.proCod=='COLEC' && convocatoriaActiva.id > 30}">
                        <div class="row">
                            <%=meLanbide48I18n.getMensaje(idiomaUsuario,"label.entidad.pregunta.entidad.sin.animo.lucro")%>
                        </div>
                        <div class="row">
                            <%=meLanbide48I18n.getMensaje(idiomaUsuario,"label.entidad.pregunta.sujeto.derecho.publico")%>
                        </div>
                        <div class="row">
                            <%=meLanbide48I18n.getMensaje(idiomaUsuario,"label.entidad.pregunta.certificado.calidad")%>
                        </div>
                       <c:forEach items="${listaCertificadosCalidad}" var="certificadoCalidadV" varStatus="contadorCCV">
                           <div class="row" id="certificadoCalidadOpcionVal_<c:out value = "${certificadoCalidadV.codigo}"/>">
                               <span style="margin-inline:10px;"><c:out value = "${certificadoCalidadV.descripcion}"/></span>
                           </div>
                       </c:forEach>
                       <div class="row">
                           <%=meLanbide48I18n.getMensaje(idiomaUsuario,"label.entidad.pregunta.compromiso.igualdad")%>
                       </div>
                    </c:when>
                    <c:otherwise>
                         <div class="row">
                            <%=meLanbide48I18n.getMensaje(idiomaUsuario,"label.entidad.pregunta.certificado.calidad")%>
                        </div>
                        <div class="row">
                            <%=meLanbide48I18n.getMensaje(idiomaUsuario,"label.entidad.pregunta.plan.igualdad")%>
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>
            <div class="col">
                <div class="row" style="text-align: center;"> 
                    <input type="radio" name="esEspecialValidado" id="esEspecialSValidado" value="S" <c:if test="${entidad!=null && entidad.centroEspEmpTHValidado!=null && entidad.centroEspEmpTHValidado==1}">checked</c:if>>&nbsp;<%=meLanbide48I18n.getMensaje(idiomaUsuario, "label.si")%>
                    &nbsp;&nbsp;<input type="radio" name="esEspecialValidado" id="esEspecialNValidado" value="N" <c:if test="${entidad!=null && entidad.centroEspEmpTHValidado!=null && entidad.centroEspEmpTHValidado==0}">checked</c:if>>&nbsp;<%=meLanbide48I18n.getMensaje(idiomaUsuario, "label.no")%>
                </div>
                <div class="row" style="text-align: center;"> 
                    <input type="radio" name="esEspecialPartMayorValidado" id="esEspecialPartMayorSValidado" value="S" <c:if test="${entidad!=null && entidad.participanteMayorCentEcpEmpTHValidado!=null && entidad.participanteMayorCentEcpEmpTHValidado==1}">checked</c:if>>&nbsp;<%=meLanbide48I18n.getMensaje(idiomaUsuario, "label.si")%>
                    &nbsp;&nbsp;<input type="radio" name="esEspecialPartMayorValidado" id="esEspecialPartMayorNValidado" value="N" <c:if test="${entidad!=null && entidad.participanteMayorCentEcpEmpTHValidado!=null && entidad.participanteMayorCentEcpEmpTHValidado==0}">checked</c:if>>&nbsp;<%=meLanbide48I18n.getMensaje(idiomaUsuario, "label.no")%>
                </div>
                <div class="row" style="text-align: center;"> 
                    <input type="radio" name="esInsercionValidado" id="esInsercionSValidado" value="S" <c:if test="${entidad!=null && entidad.empresaInsercionTHValidado!=null && entidad.empresaInsercionTHValidado==1}">checked</c:if>>&nbsp;<%=meLanbide48I18n.getMensaje(idiomaUsuario, "label.si")%>
                    &nbsp;&nbsp;<input type="radio" name="esInsercionValidado" id="esInsercionNValidado" value="N" <c:if test="${entidad!=null && entidad.empresaInsercionTHValidado!=null && entidad.empresaInsercionTHValidado==0}">checked</c:if>>&nbsp;<%=meLanbide48I18n.getMensaje(idiomaUsuario, "label.no")%>
                </div>
                <div class="row" style="text-align: center;"> 
                    <input type="radio" name="esInsercionPromotoraValidado" id="esInsercionPromotoraSValidado" value="S" <c:if test="${entidad!=null && entidad.promotorEmpInsercionTHValidado!=null && entidad.promotorEmpInsercionTHValidado==1}">checked</c:if>>&nbsp;<%=meLanbide48I18n.getMensaje(idiomaUsuario, "label.si")%>
                    &nbsp;&nbsp;<input type="radio" name="esInsercionPromotoraValidado" id="esInsercionPromotoraNValidado" value="N" <c:if test="${entidad!=null && entidad.promotorEmpInsercionTHValidado!=null && entidad.promotorEmpInsercionTHValidado==0}">checked</c:if>>&nbsp;<%=meLanbide48I18n.getMensaje(idiomaUsuario, "label.no")%>
                </div>
                <div class="row" style="text-align: center;">
                    <input type="radio" name="esAsociacionValidado" id="esAsociacionSValidado" value="S" <c:if test="${entidad!=null && entidad.esAgrupacionValidado!=null && entidad.esAgrupacionValidado==1}">checked</c:if>>&nbsp;<%=meLanbide48I18n.getMensaje(idiomaUsuario, "label.si")%>
                    &nbsp;&nbsp;<input type="radio" name="esAsociacionValidado" id="esAsociacionNValidado" value="N" <c:if test="${entidad!=null && entidad.esAgrupacionValidado!=null && entidad.esAgrupacionValidado==0}">checked</c:if>>&nbsp;<%=meLanbide48I18n.getMensaje(idiomaUsuario, "label.no")%>
                </div>
                <div class="row" style="text-align: center;">    
                    <input type="radio" name="segundosLocalesMismoAmbitoValidado" id="segundosLocalesMismoAmbitoSValidado" value="S" <c:if test="${entidad!=null && entidad.segundosLocalesMismoAmbitoValidado!=null && entidad.segundosLocalesMismoAmbitoValidado==1}">checked</c:if>>&nbsp;<%=meLanbide48I18n.getMensaje(idiomaUsuario, "label.si")%>
                    &nbsp;&nbsp;<input type="radio" name="segundosLocalesMismoAmbitoValidado" id="segundosLocalesMismoAmbitoNValidado" value="N" <c:if test="${entidad!=null && entidad.segundosLocalesMismoAmbitoValidado!=null && entidad.segundosLocalesMismoAmbitoValidado==0}">checked</c:if>>&nbsp;<%=meLanbide48I18n.getMensaje(idiomaUsuario, "label.no")%>
                </div>
                <c:choose>
                    <c:when test = "${convocatoriaActiva!=null && convocatoriaActiva.proCod=='COLEC' && convocatoriaActiva.id > 30}">
                         <div class="row" style="text-align: center;">
                            <input type="radio" name="entidadSinAnimoLucroValidado" id="entidadSinAnimoLucroSValidado" value="S" <c:if test="${entidad!=null && entidad.entSinAnimoLucroVal!=null && entidad.entSinAnimoLucroVal==1}">checked</c:if>>&nbsp;<%=meLanbide48I18n.getMensaje(idiomaUsuario, "label.si")%>
                            &nbsp;&nbsp;<input type="radio" name="entidadSinAnimoLucroValidado" id="entidadSinAnimoLucroNValidado" value="N" <c:if test="${entidad!=null && entidad.entSinAnimoLucroVal!=null && entidad.entSinAnimoLucroVal==0}">checked</c:if>>&nbsp;<%=meLanbide48I18n.getMensaje(idiomaUsuario, "label.no")%>
                        </div>
                        <div class="row" style="text-align: center;">
                            <input type="radio" name="entSujetaDerPublValidado" id="entSujetaDerPublSValidado" value="S" <c:if test="${entidad!=null && entidad.entSujetaDerPublVal!=null && entidad.entSujetaDerPublVal==1}">checked</c:if>>&nbsp;<%=meLanbide48I18n.getMensaje(idiomaUsuario, "label.si")%>
                            &nbsp;&nbsp;<input type="radio" name="entSujetaDerPublValidado" id="entSujetaDerPublNValidado" value="N" <c:if test="${entidad!=null && entidad.entSujetaDerPublVal!=null && entidad.entSujetaDerPublVal==0}">checked</c:if>>&nbsp;<%=meLanbide48I18n.getMensaje(idiomaUsuario, "label.no")%>
                        </div>
                        <div class="row">
                            <span style="padding:10px;"></span>
                        </div>
                        <c:forEach items="${listaCertificadosCalidad}" var="certificadoCalidadVOP" varStatus="contadorCCVOP">
                            <div class="row" style="text-align: center;" id="certificadoCalidadOpcionValT_<c:out value = "${certificadoCalidadVOP.codigo}"/>">
                                <input type="radio" name="certificadoCalidadOpcionValT_<c:out value = "${certificadoCalidadVOP.codigo}"/>" id="certificadoCalidadOpcionValTS_<c:out value = "${certificadoCalidadVOP.codigo}"/>" value="S">&nbsp;<%=meLanbide48I18n.getMensaje(idiomaUsuario, "label.si")%>
                                &nbsp;&nbsp;<input type="radio" name="certificadoCalidadOpcionValT_<c:out value = "${certificadoCalidadVOP.codigo}"/>" id="certificadoCalidadOpcionValTN_<c:out value = "${certificadoCalidadVOP.codigo}"/>" value="N">&nbsp;<%=meLanbide48I18n.getMensaje(idiomaUsuario, "label.no")%>
                            </div>
                        </c:forEach>
                        <div class="row" style="text-align: center;">
                            <input type="radio" name="compIgualdadOpcionValidado" id="compIgualdadOpcionSValidado" value="S" <c:if test="${entidad!=null && entidad.compIgualdadOpcionVal!=null && entidad.compIgualdadOpcionVal>0}">checked</c:if>>&nbsp;<%=meLanbide48I18n.getMensaje(idiomaUsuario, "label.si")%>
                            &nbsp;&nbsp;<input type="radio" name="compIgualdadOpcionValidado" id="compIgualdadOpcionNValidado" value="N" <c:if test="${entidad!=null && entidad.compIgualdadOpcionVal !=null && entidad.compIgualdadOpcionVal==0}">checked</c:if>>&nbsp;<%=meLanbide48I18n.getMensaje(idiomaUsuario, "label.no")%>
                        </div>
                    </c:when>
                    <c:otherwise>
                         <div class="row" style="text-align: center;">
                             <input type="radio" name="planIgualdadValidado" id="planIgualdadSValidado" value="S" <c:if test="${entidad!=null && entidad.planIgualdadValidado!=null && entidad.planIgualdadValidado==1}">checked</c:if>>&nbsp;<%=meLanbide48I18n.getMensaje(idiomaUsuario, "label.si")%>
                             &nbsp;&nbsp;<input type="radio" name="planIgualdadValidado" id="planIgualdadNValidado" value="N" <c:if test="${entidad!=null && entidad.planIgualdadValidado!=null && entidad.planIgualdadValidado==0}">checked</c:if>>&nbsp;<%=meLanbide48I18n.getMensaje(idiomaUsuario, "label.no")%>
                         </div>
                         <div class="row" style="text-align: center;">
                             <input type="radio" name="certificadoCalidadValidado" id="certificadoCalidadSValidado" value="S" <c:if test="${entidad!=null && entidad.certificadoCalidadValidado!=null && entidad.certificadoCalidadValidado==1}">checked</c:if>>&nbsp;<%=meLanbide48I18n.getMensaje(idiomaUsuario, "label.si")%>
                             &nbsp;&nbsp;<input type="radio" name="certificadoCalidadValidado" id="certificadoCalidadNValidado" value="N" <c:if test="${entidad!=null && entidad.certificadoCalidadValidado!=null && entidad.certificadoCalidadValidado==0}">checked</c:if>>&nbsp;<%=meLanbide48I18n.getMensaje(idiomaUsuario, "label.no")%>
                         </div>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>  
    </div>
    <div style="clear: both; margin-top: 15px;">
        <div class="botonera" style="text-align: right;width: 95%; margin-bottom: 15px;">
            <input type="button" id="btnGuardarEntidadColec" name="btnGuardarEntidadColec" class="botonGeneral" value="<%=meLanbide48I18n.getMensaje(idiomaUsuario, "btn.guardar")%>" onclick="guardarDatosColec();">
        </div>
    </div>
</body>

<script type="text/javascript">   
    var tabEntidadColec;
    var listaEntidadesColec = new Array();
    var listaEntidadesColecTabla = new Array();

    tabEntidadColec = new TablaColec(true,'<%=descriptor.getDescripcion("buscar")%>','<%=descriptor.getDescripcion("anterior")%>','<%=descriptor.getDescripcion("siguiente")%>','<%=descriptor.getDescripcion("mosFilasPag")%>','<%=descriptor.getDescripcion("msgNoResultBusq")%>','<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>','<%=descriptor.getDescripcion("filtrDeTotal")%>','<%=descriptor.getDescripcion("primero")%>','<%=descriptor.getDescripcion("ultimo")%>',document.getElementById('listaEntidadesColec'),955,25); 
    
    tabEntidadColec.addColumna('100','center',"<%= meLanbide48I18n.getMensaje(idiomaUsuario,"label.entidad.tabla.col1")%>", "<%= meLanbide48I18n.getMensaje(idiomaUsuario,"label.entidad.tabla.col1.title")%>");    
    tabEntidadColec.addColumna('300','left',"<%= meLanbide48I18n.getMensaje(idiomaUsuario,"label.entidad.tabla.col2")%>", "<%= meLanbide48I18n.getMensaje(idiomaUsuario,"label.entidad.tabla.col2.title")%>");   
    //tabEntidadColec.addColumna('90','right',"< %= meLanbide48I18n.getMensaje(idiomaUsuario,"label.entidad.tabla.col3")%>", "<%= meLanbide48I18n.getMensaje(idiomaUsuario,"label.entidad.tabla.col3.title")%>");   
    tabEntidadColec.addColumna('90','center',"<%= meLanbide48I18n.getMensaje(idiomaUsuario,"label.entidad.tabla.col4")%>", "<%= meLanbide48I18n.getMensaje(idiomaUsuario,"label.entidad.tabla.col4.title")%>");   
    tabEntidadColec.addColumna('90','center',"<%= meLanbide48I18n.getMensaje(idiomaUsuario,"label.entidad.tabla.col5")%>", "<%= meLanbide48I18n.getMensaje(idiomaUsuario,"label.entidad.tabla.col5.title")%>");   
    tabEntidadColec.addColumna('90','center',"<%= meLanbide48I18n.getMensaje(idiomaUsuario,"label.entidad.tabla.col6")%>", "<%= meLanbide48I18n.getMensaje(idiomaUsuario,"label.entidad.tabla.col6.title")%>");   
    tabEntidadColec.addColumna('90','center',"<%= meLanbide48I18n.getMensaje(idiomaUsuario,"label.entidad.tabla.col7")%>", "<%= meLanbide48I18n.getMensaje(idiomaUsuario,"label.entidad.tabla.col7.title")%>");   
    tabEntidadColec.addColumna('90','center',"<%= meLanbide48I18n.getMensaje(idiomaUsuario,"label.entidad.tabla.col8")%>", "<%= meLanbide48I18n.getMensaje(idiomaUsuario,"label.entidad.tabla.col8.title")%>");   
    tabEntidadColec.addColumna('90','center',"<%= meLanbide48I18n.getMensaje(idiomaUsuario,"label.entidad.tabla.col9")%>", "<%= meLanbide48I18n.getMensaje(idiomaUsuario,"label.entidad.tabla.col9.title")%>");   
    
  
    <%  		
        ColecEntidadEnAgrupacionVO voP = null;
        List<ColecEntidadEnAgrupacionVO> listaEntidades = (List<ColecEntidadEnAgrupacionVO>)request.getAttribute("entidades");													
        if (listaEntidades != null && listaEntidades.size() >0){
            for(int i = 0;i < listaEntidades.size();i++)
            {
                voP = listaEntidades.get(i);


                String centroEspecial = (voP.getCentroEspEmpTH()!=null ? String.valueOf(voP.getCentroEspEmpTH()) :"");
                if ("1".equals(centroEspecial)) {
                    centroEspecial = meLanbide48I18n.getMensaje(idiomaUsuario, "label.si").toUpperCase();
                } else if ("0".equals(centroEspecial)) {
                    centroEspecial = meLanbide48I18n.getMensaje(idiomaUsuario, "label.no").toUpperCase();
                }
                String centroEspecialMayor = (voP.getParticipanteMayorCentEcpEmpTH()!=null?String.valueOf(voP.getParticipanteMayorCentEcpEmpTH()):"");
                if ("1".equals(centroEspecialMayor)) {
                    centroEspecialMayor = meLanbide48I18n.getMensaje(idiomaUsuario, "label.si").toUpperCase();
                } else if ("0".equals(centroEspecialMayor)) {
                    centroEspecialMayor = meLanbide48I18n.getMensaje(idiomaUsuario, "label.no").toUpperCase();
                }
                String centroInsercion = (voP.getEmpresaInsercionTH()!=null?String.valueOf(voP.getEmpresaInsercionTH()):"");
                if ("1".equals(centroInsercion)) {
                    centroInsercion = meLanbide48I18n.getMensaje(idiomaUsuario, "label.si").toUpperCase();
                } else if ("0".equals(centroInsercion)) {
                    centroInsercion = meLanbide48I18n.getMensaje(idiomaUsuario, "label.no").toUpperCase();
                }
                String centroInsercionPromotor = (voP.getPromotorEmpInsercionTH()!=null?String.valueOf(voP.getPromotorEmpInsercionTH()):"");
                if ("1".equals(centroInsercionPromotor)) {
                    centroInsercionPromotor = meLanbide48I18n.getMensaje(idiomaUsuario, "label.si").toUpperCase();
                } else if ("0".equals(centroInsercionPromotor)) {
                    centroInsercionPromotor = meLanbide48I18n.getMensaje(idiomaUsuario, "label.no").toUpperCase();
                }
                String planIgualdad = (voP.getPlanIgualdad()!=null?String.valueOf(voP.getPlanIgualdad()):"-");
                if ("1".equals(planIgualdad)) {
                    planIgualdad = meLanbide48I18n.getMensaje(idiomaUsuario, "label.si").toUpperCase();
                } else if ("0".equals(planIgualdad)) {
                    planIgualdad = meLanbide48I18n.getMensaje(idiomaUsuario, "label.no").toUpperCase();
                }
                String certificadoCalidad = (voP.getCertificadoCalidad()!=null?String.valueOf(voP.getCertificadoCalidad()):"-");
                if ("1".equals(certificadoCalidad)) {
                    certificadoCalidad = meLanbide48I18n.getMensaje(idiomaUsuario, "label.si").toUpperCase();
                } else if ("0".equals(certificadoCalidad)) {
                    certificadoCalidad = meLanbide48I18n.getMensaje(idiomaUsuario, "label.no").toUpperCase();
                }
                String porcentajeCompromisoReali = (voP.getPorcentaCompromisoRealizacion()!=null?String.valueOf(voP.getPorcentaCompromisoRealizacion()):"");

    %>
        var porcentajeReali=reemplazarTextoColec('<%=porcentajeCompromisoReali%>','.',',');
        listaEntidadesColec[<%=i%>] = ['<%=voP.getCodEntidad()%>','<%=voP.getCodEntidadPadreAgrup()%>','<%=voP.getNumExp()%>', '<%=voP.getTipoCif()%>', '<%=voP.getCif()%>', '<%=voP.getNombre()%>','<%=centroEspecial%>','<%=centroEspecialMayor%>', '<%=centroInsercion%>','<%=centroInsercionPromotor%>','<%=porcentajeCompromisoReali%>','<%=planIgualdad%>','<%=certificadoCalidad%>'];
        listaEntidadesColecTabla[<%=i%>] = ['<%=voP.getCif()%>', '<%=voP.getNombre()%>','<%=centroEspecial%>','<%=centroEspecialMayor%>', '<%=centroInsercion%>','<%=centroInsercionPromotor%>','<%=planIgualdad%>','<%=certificadoCalidad%>'];
    <%
            }// for
        }// if
    %>
    
    tabEntidadColec.lineas = listaEntidadesColecTabla;
    tabEntidadColec.displayCabecera=true;
    tabEntidadColec.height = 156;
    tabEntidadColec.displayTabla();
    inicio();

</script>
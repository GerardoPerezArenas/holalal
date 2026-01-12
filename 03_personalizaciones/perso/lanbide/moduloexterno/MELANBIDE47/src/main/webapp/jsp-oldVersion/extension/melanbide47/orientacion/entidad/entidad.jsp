<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>

<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide47.i18n.MeLanbide47I18n" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide47.vo.comun.EntidadVO" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide47.vo.comun.SelectItem"%>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide47.vo.orientacion.FilaAsociacionVO"%>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.common.service.config.Config"%>
<%@page import="es.altia.common.service.config.ConfigServiceHelper"%>
<%@page import="java.util.ArrayList" %>
<%@page import="java.util.List" %>
<%@page import="org.apache.commons.lang.StringEscapeUtils"%>

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

    EntidadVO entidad = (EntidadVO) request.getAttribute("entidad");
    
    List<FilaAsociacionVO> asociaciones = (List<FilaAsociacionVO>)request.getAttribute("asociaciones");

%>
<%!
    // Funcion para escapar strings para javascript
    private String escape(String str) 
    {
        return StringEscapeUtils.escapeJavaScript(str);
    }
%>

<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/estilo.css"/>
<link rel="stylesheet" type="text/css" href="<c:url value='/css/extension/melanbide47/melanbide47.css'/>">
<link rel="stylesheet" type="text/css" href="<c:url value='/css/table.css'/>">

<script type="text/javascript" src="<c:url value='/scripts/popup.js'/>"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/melanbide47/FixedColumnsTable.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/melanbide47/moment-with-locales.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/melanbide47/ori14Utils.js"></script>

<script type="text/javascript">   

    function pulsarAltaAsociacionOri14(){
        var control = new Date();
        var result = null;
        if(navigator.appName.indexOf("Internet Explorer")!=-1){
            result = lanzarPopUpModal('<%=request.getContextPath()%>/PeticionModuloIntegracion.do?tarea=preparar&modulo=MELANBIDE47&operacion=cargarNuevaAsociacionORI&tipo=0&numero=<%=numExpediente%>&codigoEntidad='+document.getElementById('codEntidadOri14').value+'&nombreEntidad='+document.getElementById('nombreAsociacion').value+'&control='+control.getTime(),250,580,'no','no');
        }else{
            result = lanzarPopUpModal('<%=request.getContextPath()%>/PeticionModuloIntegracion.do?tarea=preparar&modulo=MELANBIDE47&operacion=cargarNuevaAsociacionORI&tipo=0&numero=<%=numExpediente%>&codigoEntidad='+document.getElementById('codEntidadOri14').value+'&nombreEntidad='+document.getElementById('nombreAsociacion').value+'&control='+control.getTime(),250,580,'no','no');
        }
        if (result != undefined){
            if(result[0] == '0'){
                recargarTablaEntidadesOri14(result);
                limpiar();
                refrescarPestanasORI14(1);
            }
        }
    }
    
    function pulsarModificarAsociacionOri14(){
        var fila;
        if(tabEntidadOri14.selectedIndex != -1) {
            fila = tabEntidadOri14.selectedIndex;
            var control = new Date();
            var result = null;
            var opcion = '';
            if(document.forms[0].modoConsulta.value == "si"){
                opcion = 'consultar';
            }else{
                opcion = 'modificar';
            }
            if(navigator.appName.indexOf("Internet Explorer")!=-1){
                result = lanzarPopUpModal('<%=request.getContextPath()%>/PeticionModuloIntegracion.do?tarea=preparar&modulo=MELANBIDE47&operacion=cargarModificarAsociacion&tipo=0&numero=<%=numExpediente%>&opcion='+opcion+'&codigoEntidad='+document.getElementById('codEntidadOri14').value+'&nombreEntidad='+document.getElementById('nombreAsociacion').value+'&codAsociacion='+listaEntidadesOri14[fila][1]+'&control='+control.getTime(),250,580,'no','no');
            }else{
                result = lanzarPopUpModal('<%=request.getContextPath()%>/PeticionModuloIntegracion.do?tarea=preparar&modulo=MELANBIDE47&operacion=cargarModificarAsociacion&tipo=0&numero=<%=numExpediente%>&opcion='+opcion+'&codigoEntidad='+document.getElementById('codEntidadOri14').value+'&nombreEntidad='+document.getElementById('nombreAsociacion').value+'&codAsociacion='+listaEntidadesOri14[fila][1]+'&control='+control.getTime(),250,580,'no','no');
            }
            if (result != undefined){
                if(result[0] == '0'){
                    recargarTablaEntidadesOri14(result);
                    refrescarPestanasORI14(1);
                }
            }
        }else{
                jsp_alerta('A', '<%=meLanbide47I18n.getMensaje(idiomaUsuario, "msg.msjNoSelecFila")%>');
            }
    }
    
    function pulsarEliminarAsociacionOri14(){
        

            if(tabEntidadOri14.selectedIndex != -1) {
                var resultado = jsp_alerta('', '<%=meLanbide47I18n.getMensaje(idiomaUsuario, "msg.preguntaEliminar")%>');
                if (resultado == 1){
                    
                    document.getElementById('msgCargandoEntidadOri14').style.display="none";
                    document.getElementById('msgEliminandoEntidadOri14').style.display="inline";
                    //barraProgresoOri14('on', 'barraProgresoEntidadesSolicitudOri14');
                    var ajax = getXMLHttpRequest();
                    var nodos = null;
                    var CONTEXT_PATH = '<%=request.getContextPath()%>'
                    var url = CONTEXT_PATH + "/PeticionModuloIntegracion.do";
                    var parametros = "";
                    var control = new Date();
                    var listaEntidades = new Array();
                    parametros = 'tarea=preparar&modulo=MELANBIDE47&operacion=eliminarAsociacion&tipo=0&numero=<%=numExpediente%>&codEntidad='+listaEntidadesOri14[tabEntidadOri14.selectedIndex][0]+'&codAsociacion='+listaEntidadesOri14[tabEntidadOri14.selectedIndex][1]+'&control='+control.getTime();
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
                        listaEntidades = extraerListadoEntidadesOri14(nodos);
                        recargarTablaEntidadesOri14(listaEntidades);
                        var codigoOperacion = listaEntidades[0];
                        if(codigoOperacion=="0"){
                            jsp_alerta("A",'<%=meLanbide47I18n.getMensaje(idiomaUsuario,"msg.registroEliminadoOK")%>');
                            refrescarPestanasORI14(1);
                        }else if(codigoOperacion=="1"){
                            jsp_alerta("A",'<%=meLanbide47I18n.getMensaje(idiomaUsuario,"error.errorEliminarBD")%>');
                        }else if(codigoOperacion=="2"){
                            jsp_alerta("A",'<%=meLanbide47I18n.getMensaje(idiomaUsuario,"error.errorEliminarGen")%>');
                        }else if(codigoOperacion=="3"){
                            jsp_alerta("A",'<%=meLanbide47I18n.getMensaje(idiomaUsuario,"error.pasoParametrosEliminar")%>');
                        }else{
                            jsp_alerta("A",'<%=meLanbide47I18n.getMensaje(idiomaUsuario,"error.errorEliminarGen")%>');
                        }//if(
                    }
                    catch(Err){
                        jsp_alerta("A",'<%=meLanbide47I18n.getMensaje(idiomaUsuario,"error.errorProcesarRespuesta")%>');
                    }//try-catch
                }
            } 
            else {
                jsp_alerta('A', '<%=meLanbide47I18n.getMensaje(idiomaUsuario, "msg.msjNoSelecFila")%>');
            }
            //barraProgresoOri14('off', 'barraProgresoEntidadesSolicitudOri14');
    }
    
    function recargarTablaEntidadesOri14(result){
        document.getElementById('codEntidadOri14').value = result[1];
        var fila;
        listaEntidadesOri14 = new Array();
        listaEntidadesOri14Tabla = new Array();
        listaEntidadesOri14Tabla_titulos = new Array();
        listaEntidadesOri14Tabla_estilos = new Array();
        
        for(var i = 2;i< result.length; i++){
            fila = result[i];
            listaEntidadesOri14[i-2] = fila;
            listaEntidadesOri14Tabla[i-2] = [fila[2], fila[3], fila[4], fila[5], fila[6], fila[7], fila[8]];
            listaEntidadesOri14Tabla_titulos[i-2] = [fila[2], fila[3], fila[4], fila[5], fila[6], fila[7], fila[8]];
        }

        inicializarTablaEntidadOri14();
        
    }
    
    function extraerListadoEntidadesOri14(nodos){
            var elemento = nodos[0];
            var hijos = elemento.childNodes;
            var codigoOperacion = null;
            var listaEntidades = new Array();
            var fila = new Array();
            var nodoFila;
            var hijosFila;
            var nodoCampo;
            var j;
            var codigoEntidad;
            
            for(j=0;hijos!=null && j<hijos.length;j++){
                if(hijos[j].nodeName=="CODIGO_OPERACION"){                            
                    codigoOperacion = hijos[j].childNodes[0].nodeValue;
                    listaEntidades[j] = codigoOperacion;
                }else if(hijos[j].nodeName=="CODIGO_ENTIDAD"){
                    codigoEntidad = hijos[j].childNodes[0].nodeValue;
                    listaEntidades[j] = codigoEntidad;
                }else if(hijos[j].nodeName=="ASOCIACION"){
                    nodoFila = hijos[j];
                    hijosFila = nodoFila.childNodes;
                    for(var cont = 0; cont < hijosFila.length; cont++){
                        if(hijosFila[cont].nodeName=="COD_ENTIDAD"){
                            nodoCampo = hijosFila[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[0] = nodoCampo.childNodes[0].nodeValue;
                            }
                            else{
                                fila[0] = '-';
                            }
                        }
                        if(hijosFila[cont].nodeName=="COD_ASOCIACION"){
                            nodoCampo = hijosFila[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[1] = nodoCampo.childNodes[0].nodeValue;
                            }
                            else{
                                fila[1] = '-';
                            }
                        }
                        else if(hijosFila[cont].nodeName=="CIF"){
                            nodoCampo = hijosFila[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[2] = nodoCampo.childNodes[0].nodeValue;
                            }
                            else{
                                fila[2] = '-';
                            }
                        }
                        else if(hijosFila[cont].nodeName=="NOMBRE"){
                            nodoCampo = hijosFila[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[3] = nodoCampo.childNodes[0].nodeValue;
                            }
                            else{
                                fila[3] = '-';
                            }
                        }
                        else if(hijosFila[cont].nodeName=="SUPRAMUN"){
                            nodoCampo = hijosFila[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[4] = nodoCampo.childNodes[0].nodeValue;
                                if (fila[4] == '1') {
                                   fila[4] = '<%=meLanbide47I18n.getMensaje(idiomaUsuario, "label.si").toUpperCase()%>';
                               } else if (fila[4] == '0') {
                                   fila[4] = '<%=meLanbide47I18n.getMensaje(idiomaUsuario, "label.no").toUpperCase()%>';
                               }
                            }
                            else{
                                fila[4] = '-';
                            }
                        }
                        else if(hijosFila[cont].nodeName=="ADM_LOCAL"){
                            nodoCampo = hijosFila[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[5] = nodoCampo.childNodes[0].nodeValue;
                                if (fila[5] == '1') {
                                   fila[5] = '<%=meLanbide47I18n.getMensaje(idiomaUsuario, "label.si").toUpperCase()%>';
                               } else if (fila[5] == '0') {
                                   fila[5] = '<%=meLanbide47I18n.getMensaje(idiomaUsuario, "label.no").toUpperCase()%>';
                               }
                            }
                            else{
                                fila[5] = '-';
                            }
                        }
                        else if(hijosFila[cont].nodeName=="CENTROFP_PUB"){
                            nodoCampo = hijosFila[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[6] = nodoCampo.childNodes[0].nodeValue;
                                if (fila[6] == '1') {
                                   fila[6] = '<%=meLanbide47I18n.getMensaje(idiomaUsuario, "label.si").toUpperCase()%>';
                               } else if (fila[6] == '0') {
                                   fila[6] = '<%=meLanbide47I18n.getMensaje(idiomaUsuario, "label.no").toUpperCase()%>';
                               }
                            }
                            else{
                                fila[6] = '-';
                            }
                        }
                        else if(hijosFila[cont].nodeName=="CENTROFP_PRIV"){
                            nodoCampo = hijosFila[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[7] = nodoCampo.childNodes[0].nodeValue;
                                if (fila[7] == '1') {
                                   fila[7] = '<%=meLanbide47I18n.getMensaje(idiomaUsuario, "label.si").toUpperCase()%>';
                               } else if (fila[7] == '0') {
                                   fila[7] = '<%=meLanbide47I18n.getMensaje(idiomaUsuario, "label.no").toUpperCase()%>';
                               }
                            }
                            else{
                                fila[7] = '-';
                            }
                        }
                    }
                    listaEntidades[j] = fila;
                    fila = new Array();
                }   
            }//for(j=0;hijos!=null && j<hijos.length;j++)
        return listaEntidades;
    }
    
    function dblClckTablaEntidadesOri14(rowID,tableName){
        //pulsarModificarEntidadOri14();
    }
    
    function cambiaEntidad() {
        var esAsociacion = (document.getElementById('esAsociacionS').checked ? "S" : (document.getElementById('esAsociacionN').checked ? "N" : ""));
        if ("S" == esAsociacion) {
            document.getElementById('supramunS').disabled = true;
            document.getElementById('supramunN').disabled = true;
            document.getElementById('admLocalS').disabled = true;
            document.getElementById('admLocalN').disabled = true;
            document.getElementById('centrofpPubN').disabled = true;
            document.getElementById('centrofpPubN').disabled = true;
            document.getElementById('centrofpPrivN').disabled = true;
            document.getElementById('centrofpPrivN').disabled = true;
            
            /*document.getElementById('supramunS').checked = false;
            document.getElementById('supramunN').checked = false;
            document.getElementById('admLocalS').checked = false;
            document.getElementById('admLocalN').checked = false;
            document.getElementById('centrofpPubS').checked = false;
            document.getElementById('centrofpPubN').checked = false;
            document.getElementById('centrofpPrivS').checked = false;
            document.getElementById('centrofpPrivN').checked = false;*/
            
            document.getElementById('nombreAsociacion').disabled = false;
            
            document.getElementById('fieldsetListadoEntidades').style.display = 'inline';
            document.getElementById('fieldsetEntidad').style.display = 'none';
        } else if ("N" == esAsociacion) {
            document.getElementById('supramunS').disabled = false;
            document.getElementById('supramunN').disabled = false;
            document.getElementById('admLocalS').disabled = false;
            document.getElementById('admLocalN').disabled = false;
            document.getElementById('centrofpPubS').disabled = false;
            document.getElementById('centrofpPubN').disabled = false;
            document.getElementById('centrofpPrivS').disabled = false;
            document.getElementById('centrofpPrivN').disabled = false;
            
            //document.getElementById('nombreAsociacion').value = '';
            document.getElementById('nombreAsociacion').disabled = true;
            
            document.getElementById('fieldsetListadoEntidades').style.display = 'none';
            document.getElementById('fieldsetEntidad').style.display = 'inline';
        } else {
            document.getElementById('supramunS').disabled = true;
            document.getElementById('supramunN').disabled = true;
            document.getElementById('admLocalS').disabled = true;
            document.getElementById('admLocalN').disabled = true;
            document.getElementById('centrofpPubS').disabled = true;
            document.getElementById('centrofpPubN').disabled = true;
            document.getElementById('centrofpPrivS').disabled = true;
            document.getElementById('centrofpPrivN').disabled = true;
            
            document.getElementById('supramunS').checked = false;
            document.getElementById('supramunN').checked = false;
            document.getElementById('admLocalS').checked = false;
            document.getElementById('admLocalN').checked = false;
            document.getElementById('centrofpPubS').checked = false;
            document.getElementById('centrofpPubN').checked = false;
            document.getElementById('centrofpPrivS').checked = false;
            document.getElementById('centrofpPrivN').checked = false;
            
            document.getElementById('nombreAsociacion').value = '';
            document.getElementById('nombreAsociacion').disabled = true;
            
            document.getElementById('fieldsetListadoEntidades').style.display = 'none';
            document.getElementById('fieldsetEntidad').style.display = 'none';
        }
    }
    
    function validarDatosOri14(){
        mensajeValidacion = '';
        var correcto = true;
        
        if(document.getElementById('esAsociacionN').checked){
            var valor = document.getElementById('cif').value;
            if(!comprobarCaracteresEspecialesOri14(valor)){
                mensajeValidacion = '<%=meLanbide47I18n.getMensaje(idiomaUsuario, "msg.asociacion.cif.caracteresNoPermitidos")%>';
                return false;
            }else if(!validarCIFOri14(valor) && !validarNifOri14(valor) && !validarNieOri14(valor)){
                mensajeValidacion = '<%=meLanbide47I18n.getMensaje(idiomaUsuario, "msg.asociacion.cif.cifIncorrecto")%>';
                return false;
            }

            valor = document.getElementById('nombre').value;
            if(!comprobarCaracteresEspecialesOri14(valor)){
                mensajeValidacion = '<%=meLanbide47I18n.getMensaje(idiomaUsuario, "msg.asociacion.nombre.caracteresNoPermitidos")%>';
                return false;
            }

            if (!document.getElementById('supramunS').checked && !document.getElementById('supramunN').checked) {
                    mensajeValidacion = '<%=meLanbide47I18n.getMensaje(idiomaUsuario, "msg.asociacion.supramun.obligatorio")%>';
                    return false;
            }

            if (!document.getElementById('admLocalS').checked && !document.getElementById('admLocalN').checked) {
                    mensajeValidacion = '<%=meLanbide47I18n.getMensaje(idiomaUsuario, "msg.asociacion.admLocal.obligatorio")%>';
                    return false;
            }

            if (!document.getElementById('centrofpPubS').checked && !document.getElementById('centrofpPubN').checked) {
                    mensajeValidacion = '<%=meLanbide47I18n.getMensaje(idiomaUsuario, "msg.asociacion.centrofpPub.obligatorio")%>';
                    return false;
            }

            if (!document.getElementById('centrofpPrivS').checked && !document.getElementById('centrofpPrivN').checked) {
                    mensajeValidacion = '<%=meLanbide47I18n.getMensaje(idiomaUsuario, "msg.asociacion.centrofpPriv.obligatorio")%>';
                    return false;
            }
        }

        if(!validarNumericoOri14(document.getElementById('trayectoriaVal'), 2)){
            mensajeValidacion = '<%=String.format(meLanbide47I18n.getMensaje(idiomaUsuario, "msg.asociacion.trayectoriaValIncorrecta"), 2)%>';
            return false;
        }
        
        return correcto;
    }
        
    function guardarDatosOri14(){
        if(validarDatosOri14()){
            document.getElementById('msgGuardandoDatos').style.display="inline";
            barraProgresoOri14('on', 'barraProgresoEntidadesSolicitudOri14');
            var ajax = getXMLHttpRequest();
            var nodos = null;
            var CONTEXT_PATH = '<%=request.getContextPath()%>'
            var url = CONTEXT_PATH + "/PeticionModuloIntegracion.do";
            var parametros = "";
            var control = new Date();
            parametros = 'tarea=preparar&modulo=MELANBIDE47&operacion=guardarDatosOri14&tipo=0&numero=<%=numExpediente%>'
                +'&codEntidad='+document.getElementById('codEntidadOri14').value
                +'&asociacion='+(document.getElementById('esAsociacionS').checked ? 1 : document.getElementById('esAsociacionN').checked ? 0 : '')
                +'&cif='+document.getElementById('cif').value
                +'&nombre='+document.getElementById('nombre').value
                +'&supramun='+(document.getElementById('supramunS').checked ? 1 : document.getElementById('supramunN').checked ? 0 : '')
                +'&admLocal='+(document.getElementById('admLocalS').checked ? 1 : document.getElementById('admLocalN').checked ? 0 : '')
                +'&centrofpPub='+(document.getElementById('centrofpPubS').checked ? 1 : document.getElementById('centrofpPubN').checked ? 0 : '')
                +'&centrofpPriv='+(document.getElementById('centrofpPrivS').checked ? 1 : document.getElementById('centrofpPrivN').checked ? 0 : '')
                +'&nombreAsociacion='+document.getElementById('nombreAsociacion').value
                +'&aceptaMas='+(document.getElementById('aceptaMasS').checked ? 1 : document.getElementById('aceptaMasN').checked ? 0 : '')
                +'&supramunVal='+(document.getElementById('supramunValS').checked ? 1 : document.getElementById('supramunValN').checked ? 0 : '')
                +'&admLocalVal='+(document.getElementById('admLocalValS').checked ? 1 : document.getElementById('admLocalValN').checked ? 0 : '')
                +'&centrofpPubVal='+(document.getElementById('centrofpPubValS').checked ? 1 : document.getElementById('centrofpPubValN').checked ? 0 : '')
                +'&centrofpPrivVal='+(document.getElementById('centrofpPrivValS').checked ? 1 : document.getElementById('centrofpPrivValN').checked ? 0 : '')
                +'&trayectoriaVal='+document.getElementById('trayectoriaVal').value
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
                }else if(hijos[j].nodeName=="CODIGO_ENTIDAD"){
                    document.getElementById('codEntidadOri14').value = hijos[j].childNodes[0].nodeValue;
                }
            }//for(j=0;hijos!=null && j<hijos.length;j++)
                if(codigoOperacion=="0"){
                    barraProgresoOri14('off', 'barraProgresoEntidadesSolicitudOri14');
                    jsp_alerta("A",'<%=meLanbide47I18n.getMensaje(idiomaUsuario,"msg.datosGuardadosOK")%>');
                    limpiar();
                    refrescarPestanasORI14(1);
                }else if(codigoOperacion=="1"){
                    jsp_alerta("A",'<%=meLanbide47I18n.getMensaje(idiomaUsuario,"error.errorBD")%>');
                }else if(codigoOperacion=="2"){
                    jsp_alerta("A",'<%=meLanbide47I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                }else if(codigoOperacion=="3"){
                    jsp_alerta("A",'<%=meLanbide47I18n.getMensaje(idiomaUsuario,"error.pasoParametros")%>');
                }else if(codigoOperacion=="4"){
                    jsp_alerta("A",'<%=meLanbide47I18n.getMensaje(idiomaUsuario,"error.datosNoValidos")%>');
                }else{
                    jsp_alerta("A",'<%=meLanbide47I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                }//if(
            }
            catch(Err){
                jsp_alerta("A",'<%=meLanbide47I18n.getMensaje(idiomaUsuario,"error.errorProcesarRespuesta")%>');
            }//try-catch

            barraProgresoOri14('off', 'barraProgresoEntidadesSolicitudOri14');
        }else{
            jsp_alerta("A", escape(mensajeValidacion));
        }
    }


    
    function inicio() {
        <%
            if (entidad != null) 
            {
        %>
                document.getElementById('codEntidadOri14').value = '<%=entidad.getOriEntCod()%>';
                document.getElementById('nombreAsociacion').value = '<%=entidad.getOriEntNom() != null ? entidad.getOriEntNom() : ""%>';
        <%
                if(entidad.getOriEntAsociacion() != null)
                {
                    if(entidad.getOriEntAsociacion().equals(1))
                    {
        %>
                        document.getElementById('esAsociacionS').checked = 'true';                                
        <%
                    }
                    else if(entidad.getOriEntAsociacion().equals(0))
                    {
                        
        %>
                        document.getElementById('esAsociacionN').checked = 'true'; 
        <%
                        if(asociaciones != null && asociaciones.size() > 0)
                        {
                            FilaAsociacionVO asoc = asociaciones.get(0);
        %>
                            document.getElementById('cif').value = '<%=asoc.getCif() != null ? asoc.getCif() : ""%>';
                            document.getElementById('nombre').value = '<%=asoc.getNombre() != null ? asoc.getNombre() : ""%>';
        <%
                            if(asoc.getSupramun() != null)
                            {
                                if(asoc.getSupramun().equals("0"))
                                {
        %>
                                    document.getElementById('supramunN').checked = 'true';
        <%
                                }
                                else if(asoc.getSupramun().equals("1"))
                                {
        %>
                                     document.getElementById('supramunS').checked = 'true';
        <%
                                }
                            }
                            if(asoc.getAdmLocal() != null)
                            {
                                if(asoc.getAdmLocal().equals("0"))
                                {
        %>
                                    document.getElementById('admLocalN').checked = 'true';
        <%
                                }
                                else if(asoc.getAdmLocal().equals("1"))
                                {
        %>
                                     document.getElementById('admLocalS').checked = 'true';
        <%
                                }
                            }
                            if(asoc.getCentrofpPub() != null)
                            {
                                if(asoc.getCentrofpPub().equals("0"))
                                {
        %>
                                    document.getElementById('centrofpPubN').checked = 'true';
        <%
                                }
                                else if(asoc.getCentrofpPub().equals("1"))
                                {
        %>
                                     document.getElementById('centrofpPubS').checked = 'true';
        <%
                                }
                            }
                            if(asoc.getCentrofpPriv() != null)
                            {
                                if(asoc.getCentrofpPriv().equals("0"))
                                {
        %>
                                    document.getElementById('centrofpPrivN').checked = 'true';
        <%
                                }
                                else if(asoc.getCentrofpPriv().equals("1"))
                                {
        %>
                                     document.getElementById('centrofpPrivS').checked = 'true';
        <%
                                }
                            }
                        }
                    }
                }
                
                if(entidad.getOriEntSupramunVal() != null)
                {
                    if(entidad.getOriEntSupramunVal().equals("0"))
                    {
        %>
                        document.getElementById('supramunValN').checked = 'true';
        <%                
                    }
                    else if(entidad.getOriEntSupramunVal().equals("1"))
                    {
        %>
                        document.getElementById('supramunValS').checked = 'true';
        <%        
                    }
                }
                
                if(entidad.getOriEntAdmLocalVal() != null)
                {
                    if(entidad.getOriEntAdmLocalVal().equals("0"))
                    {
        %>
                        document.getElementById('admLocalValN').checked = 'true';
        <%          
                    }
                    else if(entidad.getOriEntAdmLocalVal().equals("1"))
                    {
        %>
                        document.getElementById('admLocalValS').checked = 'true';
        <%        
                    }
                }
                
                if(entidad.getOriExpCentrofpPubVal() != null)
                {
                    if(entidad.getOriExpCentrofpPubVal().equals("0"))
                    {
        %>
                        document.getElementById('centrofpPubValN').checked = 'true';
        <%        
                    }
                    else if(entidad.getOriExpCentrofpPubVal().equals("1"))
                    {
        %>
                        document.getElementById('centrofpPubValS').checked = 'true';
        <%        
                    }
                }
                
                if(entidad.getOriExpCentrofpPrivVal() != null)
                {
                    if(entidad.getOriExpCentrofpPrivVal().equals("0"))
                    {
        %>
                        document.getElementById('centrofpPrivValN').checked = 'true';
        <%        
                    }
                    else if(entidad.getOriExpCentrofpPrivVal().equals("1"))
                    {
        %>
                        document.getElementById('centrofpPrivValS').checked = 'true';
        <%        
                    }
                }
                
                if(entidad.getOriEntAceptaMas() != null)
                {
                    if(entidad.getOriEntAceptaMas() == 0)
                    {
        %>
                        document.getElementById('aceptaMasN').checked = 'true';
        <%       
                    }
                    else if(entidad.getOriEntAceptaMas() == 1)
                    {
        %>
                        document.getElementById('aceptaMasS').checked = 'true';
        <%       
                    }
                }
                
                if(entidad.getOriEntTrayectoriaVal() != null)
                {
        %>
                    document.getElementById('trayectoriaVal').value = '<%=entidad.getOriEntTrayectoriaVal()%>';
        <%
                }
            }
        %>
           
        cambiaEntidad();
        inicializarTablaEntidadOri14();
        limpiar();
    }
    
    function limpiar(){
        if(!document.getElementById('esAsociacionS').checked){
            document.getElementById('nombreAsociacion').value = '';
            listaEntidadesOri14Tabla = new Array();
            listaEntidadesOri14Tabla_titulos = new Array();
            listaEntidadesOri14Tabla_estilos = new Array();
            inicializarTablaEntidadOri14();
        }else if(!document.getElementById('esAsociacionN').checked){
            document.getElementById('cif').value = '';
            document.getElementById('nombre').value = '';
            document.getElementById('supramunS').checked = false;
            document.getElementById('supramunN').checked = false;
            document.getElementById('admLocalS').checked = false;
            document.getElementById('admLocalN').checked = false;
            document.getElementById('centrofpPubS').checked = false;
            document.getElementById('centrofpPubN').checked = false;
            document.getElementById('centrofpPrivS').checked = false;
            document.getElementById('centrofpPrivN').checked = false;
        }
    }
    
    function inicializarTablaEntidadOri14(){
        tabEntidadOri14 = new FixedColumnTable(document.getElementById('entidadesOri14'), 850, 876, 'entidadesOri14');

        tabEntidadOri14.addColumna('100','center',"<%= meLanbide47I18n.getMensaje(idiomaUsuario,"label.entidad.tabla.col1")%>", "<%= meLanbide47I18n.getMensaje(idiomaUsuario,"label.entidad.tabla.col1.title")%>");    
        tabEntidadOri14.addColumna('268','left',"<%= meLanbide47I18n.getMensaje(idiomaUsuario,"label.entidad.tabla.col2")%>", "<%= meLanbide47I18n.getMensaje(idiomaUsuario,"label.entidad.tabla.col2.title")%>");   
        tabEntidadOri14.addColumna('120','center',"<%= meLanbide47I18n.getMensaje(idiomaUsuario,"label.entidad.tabla.col3")%>", "<%= meLanbide47I18n.getMensaje(idiomaUsuario,"label.entidad.tabla.col3.title")%>");   
        tabEntidadOri14.addColumna('120','center',"<%= meLanbide47I18n.getMensaje(idiomaUsuario,"label.entidad.tabla.col4")%>", "<%= meLanbide47I18n.getMensaje(idiomaUsuario,"label.entidad.tabla.col4.title")%>");   
        tabEntidadOri14.addColumna('120','center',"<%= meLanbide47I18n.getMensaje(idiomaUsuario,"label.entidad.tabla.col5")%>", "<%= meLanbide47I18n.getMensaje(idiomaUsuario,"label.entidad.tabla.col5.title")%>");   
        tabEntidadOri14.addColumna('120','center',"<%= meLanbide47I18n.getMensaje(idiomaUsuario,"label.entidad.tabla.col6")%>", "<%= meLanbide47I18n.getMensaje(idiomaUsuario,"label.entidad.tabla.col6.title")%>");   

        tabEntidadOri14.numColumnasFijas = 0;
        
        for(var cont = 0; cont < listaEntidadesOri14Tabla.length; cont++){
            tabEntidadOri14.addFilaConFormato(listaEntidadesOri14Tabla[cont], listaEntidadesOri14Tabla_titulos[cont], listaEntidadesOri14Tabla_estilos[cont])
        }

        tabEntidadOri14.displayCabecera=true;
        tabEntidadOri14.height = 156;
    
        tabEntidadOri14.altoCabecera = 30;

        tabEntidadOri14.scrollWidth = 850;

        tabEntidadOri14.dblClkFunction = 'dblClckTablaEntidadesOri14';

        tabEntidadOri14.displayTabla();

        tabEntidadOri14.pack();
    }
    
    function refrescarPestanaEntidad(){
        //Se actualizan los datos de la pestaña
    }
            
    function cambioCentrofpEntidadOri14(id){
        if(id == 'centrofpPubS'){
            document.getElementById('centrofpPrivN').checked = true;
        }else if(id == 'centrofpPrivS'){
            document.getElementById('centrofpPubN').checked = true;
        }
    }
</script>

<body>
    <div id="barraProgresoEntidadesSolicitudOri14" style="position: absolute; z-index:10; visibility: hidden; top: 30%; left: 30%;">
        <table width="100%" border="0px" cellpadding="0px" cellspacing="0px" border="0px">
            <tr>
                <td align="center" valign="middle">
                    <table class="contenedorHidepage" cellpadding="0px" cellspacing="0px" border="0px">
                        <tr>
                            <td>
                                <table width="349px" height="100%">
                                    <tr>
                                        <td colspan="3" style="height:70%;text-align:center;valign:middle;">
                                            <span id="msgCargandoEntidadOri14">
                                                <%=meLanbide47I18n.getMensaje(idiomaUsuario, "msg.cargandoDatos")%>
                                            </span>
                                            <span id="msgEliminandoEntidadOri14">
                                                <%=meLanbide47I18n.getMensaje(idiomaUsuario, "msg.eliminandoDatos")%>
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
    <div style="clear: both; font-size:15px;">
        <div class="lineaFormulario"> 
            <div style="float: left; width: 120px;">
                <label for="esAsociacion"><%=meLanbide47I18n.getMensaje(idiomaUsuario,"label.entidad.pregunta.asociacion")%></label>
            </div>
            <div style="width: auto; float: left; margin-left: 10px;">
                <div style="float: left;">
                    <input type="radio" name="esAsociacion" id="esAsociacionS" value="S" onClick="cambiaEntidad()"><%=meLanbide47I18n.getMensaje(idiomaUsuario, "label.si")%>
                    <input type="radio" name="esAsociacion" id="esAsociacionN" value="N" onClick="cambiaEntidad()"><%=meLanbide47I18n.getMensaje(idiomaUsuario, "label.no")%>
                </div>
            </div>
        </div>
        <fieldset id="fieldsetEntidad" name="fieldsetEntidad" style="width: 100%;height: 288px;">
            <legend class="legendAzul"><%=meLanbide47I18n.getMensaje(idiomaUsuario,"legend.entidad.datosEntidad")%></legend>
            <div class="lineaFormulario">
                <div style="float: left; width: 60px;">
                    <%=meLanbide47I18n.getMensaje(idiomaUsuario,"label.asociacion.cif")%>
                </div>
                <div>
                    <input type="text" maxlength="15" size="20" id="cif" name="cif" value="" class="inputTexto"/>
                </div>
            </div>
            <div class="lineaFormulario">
                <div style="float: left; width: 60px;">
                    <%=meLanbide47I18n.getMensaje(idiomaUsuario,"label.asociacion.nombre")%>
                </div>
                <div>
                    <input type="text" maxlength="500" size="60" id="nombre" name="nombre" value="" class="inputTexto"/>
                </div>
            </div>
            <div class="lineaFormulario"> 
                <div style="float: left; width: 120px; margin-left: 30px;">
                    <%=meLanbide47I18n.getMensaje(idiomaUsuario,"label.asociacion.pregunta.supramun")%>
                </div>
                <div style="width: auto; float: left; margin-left: 10px;">
                    <div style="float: left;">
                        <input type="radio" name="supramun" id="supramunS" value="S"><%=meLanbide47I18n.getMensaje(idiomaUsuario, "label.si")%>
                        <input type="radio" name="supramun" id="supramunN" value="N"><%=meLanbide47I18n.getMensaje(idiomaUsuario, "label.no")%>
                    </div>
                </div>
            </div>
            <div class="lineaFormulario"> 
                <div style="float: left; width: 120px; margin-left: 30px;">
                    <%=meLanbide47I18n.getMensaje(idiomaUsuario,"label.asociacion.pregunta.admlocal")%>
                </div>
                <div style="width: auto; float: left; margin-left: 10px;">
                    <div style="float: left;">
                        <input type="radio" name="admLocal" id="admLocalS" value="S"><%=meLanbide47I18n.getMensaje(idiomaUsuario, "label.si")%>
                        <input type="radio" name="admLocal" id="admLocalN" value="N"><%=meLanbide47I18n.getMensaje(idiomaUsuario, "label.no")%>
                    </div>
                </div>
            </div>
            <div class="lineaFormulario"> 
                <div style="float: left; width: 120px; margin-left: 30px;">
                    <%=meLanbide47I18n.getMensaje(idiomaUsuario,"label.asociacion.pregunta.centrofpPub")%>
                </div>
                <div style="width: auto; float: left; margin-left: 10px;">
                    <div style="float: left;">
                        <input type="radio" name="centrofpPub" id="centrofpPubS" value="S" onclick="cambioCentrofpEntidadOri14(this.id);"><%=meLanbide47I18n.getMensaje(idiomaUsuario, "label.si")%>
                        <input type="radio" name="centrofpPub" id="centrofpPubN" value="N"><%=meLanbide47I18n.getMensaje(idiomaUsuario, "label.no")%>
                    </div>
                </div>
            </div>
            <div class="lineaFormulario"> 
                <div style="float: left; width: 120px; margin-left: 30px;">
                    <%=meLanbide47I18n.getMensaje(idiomaUsuario,"label.asociacion.pregunta.centrofpPriv")%>
                </div>
                <div style="width: auto; float: left; margin-left: 10px;">
                    <div style="float: left;">
                        <input type="radio" name="centrofpPriv" id="centrofpPrivS" value="S" onclick="cambioCentrofpEntidadOri14(this.id);"><%=meLanbide47I18n.getMensaje(idiomaUsuario, "label.si")%>
                        <input type="radio" name="centrofpPriv" id="centrofpPrivN" value="N"><%=meLanbide47I18n.getMensaje(idiomaUsuario, "label.no")%>
                    </div>
                </div>
            </div>
        </fieldset>
    </div>
    <fieldset id="fieldsetListadoEntidades" name="fieldsetListadoEntidades" style="width: 100%;height: 290px; padding-top: 15px;">
        <legend class="legendAzul"><%=meLanbide47I18n.getMensaje(idiomaUsuario,"legend.entidad.listaAsociaciones")%></legend>
        <div class="lineaFormulario">
            <div style="float: left; width: 60px;">
                <%=meLanbide47I18n.getMensaje(idiomaUsuario,"label.asociacion.nombreAsociacion")%>
            </div>
            <div>
                <input type="text" maxlength="80" size="60" id="nombreAsociacion" name="nombreAsociacion" value="" class="inputTexto"/>
            </div>
        </div>
        <div style="clear: both;">
            <div>
                <div id="entidadesOri14" name="entidadesOri14" style="padding: 5px; width:876px; height: 129px; text-align: center; margin:0px;margin-top:0px;" align="center"></div>
                <div class="botonera" id="botoneraEntidad" name="botoneraEntidad">
                    <input type="button" id="btnNuevaEntidad" name="btnNuevaEntidad" class="botonGeneral" value="<%=meLanbide47I18n.getMensaje(idiomaUsuario, "btn.alta")%>" onclick="pulsarAltaAsociacionOri14();">
                    <input type="button" id="btnModificarEntidad" name="btnModificarEntidad" class="botonGeneral" value="<%=meLanbide47I18n.getMensaje(idiomaUsuario, "btn.modificar")%>" onclick="pulsarModificarAsociacionOri14();">
                    <input type="button" id="btnEliminarEntidad" name="btnEliminarEntidad" class="botonGeneral" value="<%=meLanbide47I18n.getMensaje(idiomaUsuario, "btn.eliminar")%>" onclick="pulsarEliminarAsociacionOri14();">
                </div>
            </div>
        </div>
    </fieldset>
    <fieldset id="fieldsetAceptaMas" name="fieldsetAceptaMas" style="width: 100%;height: 30px; padding-top: 15px;">
        <legend class="legendAzul"><%=meLanbide47I18n.getMensaje(idiomaUsuario,"legend.entidad.aceptaMas")%></legend>
        <div class="lineaFormulario">
            <div style="float: left; width: 564px;">
                <%=meLanbide47I18n.getMensaje(idiomaUsuario,"label.entidad.aceptaMas")%>
            </div>
            <div style="float: left; margin-left: 25px;">
                <input type="radio" name="aceptaMas" id="aceptaMasS" value="S"><%=meLanbide47I18n.getMensaje(idiomaUsuario, "label.si")%>
                <input type="radio" name="aceptaMas" id="aceptaMasN" value="N"><%=meLanbide47I18n.getMensaje(idiomaUsuario, "label.no")%>
            </div>
        </div>
    </fieldset>
    <fieldset id="fieldsetValidacion" name="fieldsetValidacion" style="width: 100%;height: 110px; padding-top: 15px;">
        <legend class="legendAzul"><%=meLanbide47I18n.getMensaje(idiomaUsuario,"legend.entidad.validacion")%></legend>
        <div class="lineaFormulario">
            <div style="float: left; width: 240px;">
                <%=meLanbide47I18n.getMensaje(idiomaUsuario,"label.asociacion.pregunta.supramun")%>
            </div>
            <div style="float: left;">
                <input type="radio" name="supramunVal" id="supramunValS" value="S" disabled="true" readonly="true"><%=meLanbide47I18n.getMensaje(idiomaUsuario, "label.si")%>
                <input type="radio" name="supramunVal" id="supramunValN" value="N" disabled="true" readonly="true"><%=meLanbide47I18n.getMensaje(idiomaUsuario, "label.no")%>
            </div>
            <div style="float: left; width: 240px; margin-left: 100px;">
                <%=meLanbide47I18n.getMensaje(idiomaUsuario,"label.asociacion.pregunta.admlocal")%>
            </div>
            <div style="float: left;">
                <input type="radio" name="admLocalVal" id="admLocalValS" value="S" disabled="true" readonly="true"><%=meLanbide47I18n.getMensaje(idiomaUsuario, "label.si")%>
                <input type="radio" name="admLocalVal" id="admLocalValN" value="N" disabled="true" readonly="true"><%=meLanbide47I18n.getMensaje(idiomaUsuario, "label.no")%>
            </div>
        </div>
        <div class="lineaFormulario">
            <div style="float: left; width: 240px;">
                <%=meLanbide47I18n.getMensaje(idiomaUsuario,"label.asociacion.pregunta.centrofpPub")%>
            </div>
            <div style="float: left;">
                <input type="radio" name="centrofpPubVal" id="centrofpPubValS" value="S" disabled="true" readonly="true"><%=meLanbide47I18n.getMensaje(idiomaUsuario, "label.si")%>
                <input type="radio" name="centrofpPubVal" id="centrofpPubValN" value="N" disabled="true" readonly="true"><%=meLanbide47I18n.getMensaje(idiomaUsuario, "label.no")%>
            </div>
            <div style="float: left; width: 240px; margin-left: 100px;">
                <%=meLanbide47I18n.getMensaje(idiomaUsuario,"label.asociacion.pregunta.centrofpPriv")%>
            </div>
            <div style="float: left;">
                <input type="radio" name="centrofpPrivVal" id="centrofpPrivValS" value="S" disabled="true" readonly="true"><%=meLanbide47I18n.getMensaje(idiomaUsuario, "label.si")%>
                <input type="radio" name="centrofpPrivVal" id="centrofpPrivValN" value="N" disabled="true" readonly="true"><%=meLanbide47I18n.getMensaje(idiomaUsuario, "label.no")%>
            </div>
        </div>
        <div class="lineaFormulario">
            <div style="float: left; margin-right: 5px;">
                <%=meLanbide47I18n.getMensaje(idiomaUsuario,"label.asociacion.pregunta.trayectoria")%>
            </div>
            <div style="float: left;">
                <input type="text" name="trayectoriaVal" id="trayectoriaVal" value="" maxlength="2" size="2" class="inputTexto"/>
            </div>
<!--            <div style="float: left; margin-left: 5px;">
                <%=meLanbide47I18n.getMensaje(idiomaUsuario,"label.asociacion.anos")%>
            </div>-->
        </div>
    </fieldset>
    <div style="clear: both; margin-top: 15px;">
        <div class="botonera" style="text-align: right;width: 95%; margin-bottom: 15px;">
            <input type="button" id="btnGuardarEntidadOri14" name="btnGuardarEntidadOri14" class="botonGeneral" value="<%=meLanbide47I18n.getMensaje(idiomaUsuario, "btn.guardar")%>" onclick="guardarDatosOri14();">
        </div>
    </div>
        <input type="hidden" id="codEntidadOri14" name="codEntidadOri14" value=""/>
</body>

<script type="text/javascript">   
    var tabEntidadOri14;
    var listaEntidadesOri14 = new Array();
    var listaEntidadesOri14Tabla = new Array();
    var listaEntidadesOri14Tabla_titulos = new Array();
    var listaEntidadesOri14Tabla_estilos = new Array();
    
    var supramun;
    var admLocal;
    var centrofpPub;
    var centrofpPriv;
    
    <%
        if(entidad != null && entidad.getOriEntAsociacion() != null && entidad.getOriEntAsociacion().equals(1))
        {
            if(asociaciones != null && asociaciones.size() > 0)
            {
                FilaAsociacionVO fila = null;
                for(int i = 0; i < asociaciones.size(); i++)
                {
                    fila = asociaciones.get(i);
    %>
        
                    supramun = '<%=fila.getSupramun() != null ? (fila.getSupramun().equals("1") ? meLanbide47I18n.getMensaje(idiomaUsuario, "label.si").toUpperCase() : meLanbide47I18n.getMensaje(idiomaUsuario, "label.no").toUpperCase()) : "-"%>';
                    admLocal = '<%=fila.getAdmLocal() != null ? (fila.getAdmLocal().equals("1") ? meLanbide47I18n.getMensaje(idiomaUsuario, "label.si").toUpperCase() : meLanbide47I18n.getMensaje(idiomaUsuario, "label.no").toUpperCase()) : "-"%>';
                    centrofpPub = '<%=fila.getCentrofpPub() != null ? (fila.getCentrofpPub().equals("1") ? meLanbide47I18n.getMensaje(idiomaUsuario, "label.si").toUpperCase() : meLanbide47I18n.getMensaje(idiomaUsuario, "label.no").toUpperCase()) : "-"%>';
                    centrofpPriv = '<%=fila.getCentrofpPriv() != null ? (fila.getCentrofpPriv().equals("1") ? meLanbide47I18n.getMensaje(idiomaUsuario, "label.si").toUpperCase() : meLanbide47I18n.getMensaje(idiomaUsuario, "label.no").toUpperCase()) : "-"%>';

                    listaEntidadesOri14[<%=i%>] = [ '<%=fila.getCodEntidad()%>', '<%=fila.getCodAsociacion()%>', '<%=fila.getCif()%>', '<%=fila.getNombre()%>', supramun, admLocal, centrofpPub, centrofpPriv];
                    listaEntidadesOri14Tabla[<%=i%>] = ['<%=fila.getCif()%>', '<%=fila.getNombre()%>', supramun, admLocal, centrofpPub, centrofpPriv];
                    listaEntidadesOri14Tabla_titulos[<%=i%>] = ['<%=fila.getCif()%>', '<%=fila.getNombre()%>', supramun, admLocal, centrofpPub, centrofpPriv];
    <%
                }
            }
        }
    %>

    inicio();
</script>
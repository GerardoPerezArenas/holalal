<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide47.i18n.MeLanbide47I18n" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide47.vo.orientacion.OriAmbitoSolicitadoVO" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide47.vo.comun.SelectItem" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="java.util.ArrayList" %>
<%@page import="java.util.List" %>
<%@page import="org.apache.commons.lang.StringEscapeUtils"%>

<html>
    <head> 
        <meta http-equiv="Content-Type" content="text/html" charset="ISO_8859-1">		
		<meta http-equiv="X-UA-Compatible" content="IE=Edge" />
        <%
            String lcodProvincias = "";
            String ldescProvincias = "";
            OriAmbitoSolicitadoVO ambitoSoliModif = new OriAmbitoSolicitadoVO();
            String codOrganizacion = "";
            String numExpediente = "";
            int anionumExpediente =0;
            String nuevo = "1";
            MeLanbide47I18n meLanbide47I18n = MeLanbide47I18n.getInstance();
            int idiomaUsuario = 1;
            int apl = 5;
            String css = "";
            List<SelectItem> listaProvincias = new ArrayList<SelectItem>();
        
        try
        {
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
            
            codOrganizacion  = request.getParameter("codOrganizacionModulo");
            numExpediente    = request.getParameter("numero");
            anionumExpediente = numExpediente != null ? Integer.parseInt(numExpediente.substring(0,4)):0;
            nuevo = (String)request.getAttribute("nuevo");
            //LISTAS PARA LOS COMBOS
            
            if(request.getAttribute("listaProvincias") != null)
                listaProvincias = (List<SelectItem>)request.getAttribute("listaProvincias");
            
            // Provincias
            if (listaProvincias != null && listaProvincias.size() > 0) 
            {
                int i;
                SelectItem si = null;
                for (i = 0; i < listaProvincias.size() - 1; i++) 
                {
                    si = (SelectItem) listaProvincias.get(i);
                    lcodProvincias += "\"" + si.getId() + "\",";
                    ldescProvincias += "\"" + escape(si.getLabel()) + "\",";
                }
                si = (SelectItem) listaProvincias.get(i);
                lcodProvincias += "\"" + si.getId() + "\"";
                ldescProvincias += "\"" + escape(si.getLabel()) + "\"";
            }
            
            if(request.getAttribute("ambitoSoliModif") != null)
            {
                ambitoSoliModif = (OriAmbitoSolicitadoVO)request.getAttribute("ambitoSoliModif");
            }
        }
        catch(Exception ex)
        {
        }
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
<link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/css/font-awesome.min.css'/>" media="screen">
<link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/less/animated.less'/>" media="screen">
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/extension/melanbide47/melanbide47.css"/>

<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/general.js"></script>
<script type="text/javascript">
    var APP_CONTEXT_PATH = '<%=request.getContextPath()%>';
</script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/validaciones.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/listaComboBox.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/melanbide47/moment-with-locales.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/melanbide47/ori14Utils.js"></script>
        
        <script type="text/javascript">
            var provinciaCambiada = false;
            var ambitoCambiado = false;
            
        function getXMLHttpRequest(){
            var aVersions = [ "MSXML2.XMLHttp.5.0",
                "MSXML2.XMLHttp.4.0","MSXML2.XMLHttp.3.0",
                "MSXML2.XMLHttp","Microsoft.XMLHttp"
                ];

            if (window.XMLHttpRequest){
                // para IE7, Mozilla, Safari, etc: que usen el objeto nativo
                return new XMLHttpRequest();
            }else if (window.ActiveXObject){
                // de lo contrario utilizar el control ActiveX para IE5.x y IE6.x
                for (var i = 0; i < aVersions.length; i++) {
                    try {
                        var oXmlHttp = new ActiveXObject(aVersions[i]);
                        return oXmlHttp;
                    }catch (error) {
                    //no necesitamos hacer nada especial
                    }
                }
            }else{
                return null;
            }
        }
            
            
            //Listas de valores para los combos
            var codProvinciasAmbitoSolicitado = [<%=lcodProvincias%>];
            var descProvinciasAmbitoSolicitado = [<%=ldescProvincias%>];
            var codAmbitosAmbitoSolicitado = new Array();
            var descAmbitosAmbitoSolicitado = new Array();
            //var codMunicipios = new Array();
            //var descMunicipios = new Array();
            
            var mensajeValidacion = '';
            
            
            function inicioAmbitosSolicitados(){
                cargarCombos();
                cargarCodigosCombos();
                cargarDescripcionesCombos();
            }
            
            function cargarCombos() {
                comboProvinciasAmbitoSolicitado.addItems(codProvinciasAmbitoSolicitado, descProvinciasAmbitoSolicitado);
            }
            
            function cargarCodigosCombos(){
                document.getElementById('codProvinciaAmbitoSolicitado').value = '<%=ambitoSoliModif != null && ambitoSoliModif.getOriAmbSolTerHis() != null ? String.valueOf(ambitoSoliModif.getOriAmbSolTerHis()) : ""%>';
                document.getElementById('codAmbitoSolicitado').value = '<%=ambitoSoliModif != null && ambitoSoliModif.getOriAmbSolAmbito() != null ? String.valueOf(ambitoSoliModif.getOriAmbSolAmbito()) : ""%>';
            }
            
            function cargarDescripcionesCombos(){
                var desc = "";
                var codAct = "";
                var codigo = "";

                
                //Provincias
                codigo = "<%=ambitoSoliModif != null && ambitoSoliModif.getOriAmbSolTerHis() != null ? String.valueOf(ambitoSoliModif.getOriAmbSolTerHis()) : ""%>";
                if(codigo != null && codigo != "")
                {
                    for(var i=0; i<codProvinciasAmbitoSolicitado.length; i++)
                    {
                        codAct = codProvinciasAmbitoSolicitado[i];
                        if(codAct == codigo)
                        {
                            desc = descProvinciasAmbitoSolicitado[i];
                            cargarAmbitosPorProvinciaAmbitoSolicitado();
                            document.getElementById('codAmbitoSolicitado').value = '<%=ambitoSoliModif != null && ambitoSoliModif.getOriAmbSolAmbito() != null ? String.valueOf(ambitoSoliModif.getOriAmbSolAmbito()) : ""%>';
                        }
                    }
                }
                document.getElementById('descProvinciaAmbitoSolicitado').value = desc;
                
                desc = "";
                codAct = "";
                codigo = "";
                //Ambitos
                codigo = "<%=ambitoSoliModif != null && ambitoSoliModif.getOriAmbSolAmbito() != null ? String.valueOf(ambitoSoliModif.getOriAmbSolAmbito()) : ""%>";
                if(codigo != null && codigo != "")
                {
                    for(var i=0; i<codAmbitosAmbitoSolicitado.length; i++)
                    {
                        codAct = codAmbitosAmbitoSolicitado[i];
                        if(codAct == codigo)
                        {
                            desc = descAmbitosAmbitoSolicitado[i];
                        }
                    }
                }
                document.getElementById('descAmbitoSolicitado').value = desc;
                
                desc = "";
                codAct = "";
                codigo = "";
            }
            
            function cargarAmbitosPorProvinciaAmbitoSolicitado(){
                var codProvincia = document.getElementById('codProvinciaAmbitoSolicitado').value;
                
                if(provinciaCambiada == false){
                    var codProvinciaIni = '<%=ambitoSoliModif != null && ambitoSoliModif.getOriAmbSolTerHis() != null ? String.valueOf(ambitoSoliModif.getOriAmbSolTerHis()) : ""%>';
                    if(codProvinciaIni != codProvincia){
                        provinciaCambiada = true;
                    }
                }
                
                if(codProvincia != null && codProvincia != ''){
                    var ajax = getXMLHttpRequest();
                    var nodos = null;
                    var CONTEXT_PATH = '<%=request.getContextPath()%>'
                    var url = CONTEXT_PATH + "/PeticionModuloIntegracion.do";
                    var parametros = "";
                    var control = new Date();
                    parametros = 'tarea=preparar&modulo=MELANBIDE47&operacion=cargarAmbitosHorasPorProvincia&tipo=0&numero=<%=numExpediente%>&codProvincia='+codProvincia+'&control='+control.getTime();
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
                        var listaAmbitos = new Array();
                        var fila = new Array();
                        var nodoFila;
                        var hijosFila;
                        var contAmbitos = 0;
                        for(j=0;hijos!=null && j<hijos.length;j++){
                            if(hijos[j].nodeName=="CODIGO_OPERACION"){                            
                                codigoOperacion = hijos[j].childNodes[0].nodeValue;
                            }//if(hijos[j].nodeName=="CODIGO_OPERACION")                      
                            else if(hijos[j].nodeName=="ITEM_AMBITO"){
                                nodoFila = hijos[j];
                                hijosFila = nodoFila.childNodes;
                                for(var cont = 0; cont < hijosFila.length; cont++){
                                    if(hijosFila[cont].nodeName=="ID"){
                                        if(hijosFila[cont].childNodes.length > 0){
                                            fila[0] = hijosFila[cont].childNodes[0].nodeValue;
                                        }
                                        else{
                                            fila[0] = '-';
                                        }
                                    }
                                    else if(hijosFila[cont].nodeName=="LABEL"){
                                        if(hijosFila[cont].childNodes.length > 0){
                                            fila[1] = hijosFila[cont].childNodes[0].nodeValue;
                                        }
                                        else{
                                            fila[1] = '-';
                                        }
                                    }
                                }
                                listaAmbitos[contAmbitos] = fila;
                                contAmbitos++;
                                fila = new Array();
                            }       
                        }//for(j=0;hijos!=null && j<hijos.length;j++)
                        if(codigoOperacion=="0"){
                            if(provinciaCambiada == true){
                                document.getElementById('codAmbitoSolicitado').value = '';
                                document.getElementById('descAmbitoSolicitado').value = '';
                            }
                            comboAmbitosSolicitado = new Combo("AmbitoSolicitado");
                            codAmbitosAmbitoSolicitado = new Array();
                            descAmbitosAmbitoSolicitado = new Array();
                            for(var i = 0; i < listaAmbitos.length; i++){
                                codAmbitosAmbitoSolicitado[i] = listaAmbitos[i][0];
                                descAmbitosAmbitoSolicitado[i] = listaAmbitos[i][1];
                            }
                            comboAmbitosSolicitado.addItems(codAmbitosAmbitoSolicitado, descAmbitosAmbitoSolicitado);
                            
                        }else if(codigoOperacion=="1"){
                            jsp_alerta("A",'<%=meLanbide47I18n.getMensaje(idiomaUsuario,"error.errorBD")%>');
                        }else if(codigoOperacion=="2"){
                            jsp_alerta("A",'<%=meLanbide47I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                        }else if(codigoOperacion=="3"){
                            jsp_alerta("A",'<%=meLanbide47I18n.getMensaje(idiomaUsuario,"error.pasoParametros")%>');
                        }else{
                            jsp_alerta("A",'<%=meLanbide47I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                        }//if(
                    }
                    catch(Err){

                    }//try-catch
                }
            }
            
            function guardarDatosAmbitoSolicitado(){
                if(validarDatos()){
                    var ajax = getXMLHttpRequest();
                    var nodos = null;
                    var CONTEXT_PATH = '<%=request.getContextPath()%>'
                    var url = CONTEXT_PATH + "/PeticionModuloIntegracion.do";
                    var parametros = "";
                    parametros = "tarea=preparar&modulo=MELANBIDE47&operacion=guardarAmbitoSolicitadoORI&tipo=0&numero=<%=numExpediente%>"
                            +"&idAmbitoSolicitado=<%=ambitoSoliModif != null && ambitoSoliModif.getOriAmbSolCod() != null ? ambitoSoliModif.getOriAmbSolCod().toString() : ""%>"
                            +"&codProvinciaAmbitoSolicitado="+document.getElementById('codProvinciaAmbitoSolicitado').value
                            +"&codAmbitoSolicitado="+document.getElementById('codAmbitoSolicitado').value
                            +"&nroBloquesSolic="+document.getElementById('nroBloquesSolic').value
                            +"&nroUbicacionesxAmbito="+document.getElementById('nroUbicacionesxAmbito').value
                        ;
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
                        var listaAmbitosSolicitados = new Array();
                        var fila = new Array();
                        var nodoFila;
                        var hijosFila;
                        for(j=0;hijos!=null && j<hijos.length;j++){
                            if(hijos[j].nodeName=="CODIGO_OPERACION"){                            
                                codigoOperacion = hijos[j].childNodes[0].nodeValue;
                                listaAmbitosSolicitados[j] = codigoOperacion;
                            }//if(hijos[j].nodeName=="CODIGO_OPERACION")                        
                            else if(hijos[j].nodeName=="FILA"){
                                nodoFila = hijos[j];
                                hijosFila = nodoFila.childNodes;
                                for(var cont = 0; cont < hijosFila.length; cont++){
                                    if(hijosFila[cont].nodeName=="ORI_AMB_SOL_COD"){
                                        if(hijosFila[cont].childNodes.length > 0){
                                            fila[0] = hijosFila[cont].childNodes[0].nodeValue;
                                        }
                                        else{
                                            fila[0] = '-';
                                        }
                                    }
                                    else if(hijosFila[cont].nodeName=="ORI_AMB_SOL_NUM_EXP"){
                                        if(hijosFila[cont].childNodes.length > 0){
                                            fila[1] = hijosFila[cont].childNodes[0].nodeValue;
                                        }
                                        else{
                                            fila[1] = '-';
                                        }
                                    }
                                    else if(hijosFila[cont].nodeName=="ORI_AMB_SOL_TERHIS"){
                                        if(hijosFila[cont].childNodes.length > 0){
                                            fila[2] = hijosFila[cont].childNodes[0].nodeValue;
                                        }
                                        else{
                                            fila[2] = '-';
                                        }
                                    }
                                    else if(hijosFila[cont].nodeName=="ORI_AMB_SOL_AMBITO"){
                                        if(hijosFila[cont].childNodes.length > 0){
                                            fila[3] = hijosFila[cont].childNodes[0].nodeValue;
                                        }
                                        else{
                                            fila[3] = '-';
                                        }
                                    }
                                    else if(hijosFila[cont].nodeName=="ORI_AMB_SOL_NRO_BLOQUES"){
                                        if(hijosFila[cont].childNodes.length > 0){
                                            fila[4] = hijosFila[cont].childNodes[0].nodeValue;
                                        }
                                        else{
                                            fila[4] = '-';
                                        }
                                    }
                                    else if(hijosFila[cont].nodeName=="ORI_AMB_SOL_NRO_UBIC"){
                                        if(hijosFila[cont].childNodes.length > 0){
                                            fila[5] = hijosFila[cont].childNodes[0].nodeValue;
                                        }
                                        else{
                                            fila[5] = '-';
                                        }
                                    }
                                    else if(hijosFila[cont].nodeName=="ORI_AMB_SOL_TERHIS_DESC"){
                                        if(hijosFila[cont].childNodes.length > 0){
                                            fila[6] = hijosFila[cont].childNodes[0].nodeValue;
                                        }
                                        else{
                                            fila[6] = '-';
                                        }
                                    }
                                    else if(hijosFila[cont].nodeName=="ORI_AMB_SOL_AMBITO_DESC"){
                                        if(hijosFila[cont].childNodes.length > 0){
                                            fila[7] = hijosFila[cont].childNodes[0].nodeValue;
                                        }
                                        else{
                                            fila[7] = '-';
                                        }
                                    }
                                }
                                listaAmbitosSolicitados[j] = fila;
                                fila = new Array();
                            }   
                        }//for(j=0;hijos!=null && j<hijos.length;j++)
                        if(codigoOperacion=="0"){
                            //jsp_alerta("A",'Correcto');
                            self.parent.opener.retornoXanelaAuxiliar(listaAmbitosSolicitados);
                            jsp_alerta("A",'<%=meLanbide47I18n.getMensaje(idiomaUsuario,"msg.datosGuardadosOK")%>');
                            cerrarVentana();
                        }else if(codigoOperacion=="1"){
                            jsp_alerta("A",'<%=meLanbide47I18n.getMensaje(idiomaUsuario,"error.errorBD")%>');
                        }else if(codigoOperacion=="2"){
                            jsp_alerta("A",'<%=meLanbide47I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                        }else if(codigoOperacion=="3"){
                            jsp_alerta("A",'<%=meLanbide47I18n.getMensaje(idiomaUsuario,"error.pasoParametros")%>');
                        }else if(codigoOperacion=="4"){
                            jsp_alerta("A",'<%=meLanbide47I18n.getMensaje(idiomaUsuario,"error.expSinTercero")%>');
                        }else{
                                jsp_alerta("A",'<%=meLanbide47I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                        }//if(
                    }
                    catch(Err){

                    }//try-catch
                }else{
                    jsp_alerta("A", mensajeValidacion);
                }
            }
            
            function getProvinciaDeMunicipioSeleccionado(codMun){
                try{
                    var i = 0;
                    var encontrado = false;
                    while(i < codMunicipios.length && !encontrado){
                        if(codMunicipios[i] == codMun){
                            encontrado = true;
                        }
                        i++;
                    }
                    if(encontrado){
                        return prvMunicipios[i];
                    }else{
                        return "";
                    }
                }catch(err){
                    
                }
            }
            
            function validarDatos(){
                mensajeValidacion = "";
                var codProvincia = document.getElementById('codProvinciaAmbitoSolicitado').value;
                if(codProvincia == null || codProvincia == ''){
                    mensajeValidacion = '<%=meLanbide47I18n.getMensaje(idiomaUsuario, "msg.ambitos.nuevoAmbito.provincia.obligatorio")%>';
                    return false;
                }
                
                var codAmbito = document.getElementById('codAmbitoSolicitado').value;
                if(codAmbito == null || codAmbito == ''){
                    mensajeValidacion = '<%=meLanbide47I18n.getMensaje(idiomaUsuario, "msg.ambitos.nuevoAmbito.ambito.obligatorio")%>';
                    return false;
                }
        
                var bloquesSol = document.getElementById('nroBloquesSolic').value;
                if(bloquesSol == null || bloquesSol == ''){
                    //mensajeValidacion = '<%=meLanbide47I18n.getMensaje(idiomaUsuario, "msg.ambitos.nuevoAmbito.todosCamposOblig")%>';
                    //return false;
                }
                
                var correcto = true;
                
                if(!validarNumerico(document.getElementById('nroBloquesSolic'))){
                    document.getElementById('nroBloquesSolic').style.border = '1px solid red';
                    if(mensajeValidacion == '')
                        mensajeValidacion = '<%=meLanbide47I18n.getMensaje(idiomaUsuario, "msg.ambitos.nuevoAmbito.numBloquesSolicitados.incorrecto")%>';
                    correcto = false;
                }
                else{
                    try{
                        var horas = parseInt(document.getElementById('nroBloquesSolic').value);
                        if(horas < 0){
                            document.getElementById('nroBloquesSolic').style.border = '1px solid red';
                            if(mensajeValidacion == '')
                                mensajeValidacion = '<%=meLanbide47I18n.getMensaje(idiomaUsuario, "msg.ambitos.nuevoAmbito.numBloquesSolicitados.incorrecto")%>';
                            correcto = false;
                        }else{
                            document.getElementById('nroBloquesSolic').removeAttribute("style");
                        }
                    }
                    catch(err){
                        document.getElementById('nroBloquesSolic').style.border = '1px solid red';
                        if(mensajeValidacion == '')
                            mensajeValidacion = '<%=meLanbide47I18n.getMensaje(idiomaUsuario, "msg.ambitos.nuevoAmbito.numHorasIncorrecto")%>';
                        correcto = false;
                    }
                }
                
                if(!validarNumerico(document.getElementById('nroUbicacionesxAmbito'))){
                    document.getElementById('nroUbicacionesxAmbito').style.border = '1px solid red';
                    if(mensajeValidacion == '')
                        mensajeValidacion = '<%=meLanbide47I18n.getMensaje(idiomaUsuario, "msg.ambitos.nuevoAmbito.numUbicacionesAmbito.incorrecto")%>';
                    correcto = false;
                }
                else{
                    try{
                        var horas = parseInt(document.getElementById('nroUbicacionesxAmbito').value);
                        if(horas < 0){
                            document.getElementById('nroUbicacionesxAmbito').style.border = '1px solid red';
                            if(mensajeValidacion == '')
                                mensajeValidacion = '<%=meLanbide47I18n.getMensaje(idiomaUsuario, "msg.ambitos.nuevoAmbito.numUbicacionesAmbito.incorrecto")%>';
                            correcto = false;
                        }else{
                            document.getElementById('nroUbicacionesxAmbito').removeAttribute("style");
                        }
                    }
                    catch(err){
                        document.getElementById('nroUbicacionesxAmbito').style.border = '1px solid red';
                        if(mensajeValidacion == '')
                            mensajeValidacion = '<%=meLanbide47I18n.getMensaje(idiomaUsuario, "msg.ambitos.nuevoAmbito.numUbicacionesAmbito.incorrecto")%>';
                        correcto = false;
                    }
                }
                
                return correcto;
            }
            
            function validarNumerico(numero){
                try{
                    if (Trim(numero.value)!='') {
                        return /^([0-9])*$/.test(numero.value);
                    }
                }
                catch(err){
                    return false;
                }
            }
            
            function cancelar(){
                var resultado = jsp_alerta('','<%=meLanbide47I18n.getMensaje(idiomaUsuario, "msg.preguntaCancelar")%>');
                if (resultado == 1){
                    cerrarVentana();
                }
            }
            
            function cerrarVentana(){
                if(navigator.appName=="Microsoft Internet Explorer") { 
                      window.parent.window.opener=null; 
                      window.parent.window.close(); 
                }else if(navigator.appName=="Netscape") { 
                      top.window.opener = top; 
                      top.window.open('','_parent',''); 
                      top.window.close(); 
                }else{
                     window.close(); 
                } 
            }
        </script>
    </head>
    <body onload="inicioAmbitosSolicitados();" class="contenidoPantalla">
            <div style="width: 100%; padding: 10px; text-align: left;">
                <div class="tituloAzul" style="clear: both; text-align: left;">
                    <span>
                        <%=meLanbide47I18n.getMensaje(idiomaUsuario,"label.ambitos.nuevoAmbito.datosAmbitoSolicitado")%>
                    </span>
                </div>
                <div class="lineaFormulario">
                    <div style="width: 170px; float: left;">
                        <%=meLanbide47I18n.getMensaje(idiomaUsuario,"label.ambitos.nuevoAmbito.territorioHistorico")%>
                    </div>
                    <div style="width: 450px; float: left;">
                        <div style="float: left;">
                            <input id="codProvinciaAmbitoSolicitado" name="codProvinciaAmbitoSolicitado" type="text" class="inputTexto" size="2" maxlength="2" 
                                   onkeypress="javascript:return SoloDigitosConsulta(event);" >
                            <input id="descProvinciaAmbitoSolicitado" name="descProvinciaAmbitoSolicitado" type="text" class="inputTexto" size="15" readonly>
                            <a id="anchorProvinciaAmbitoSolicitado" name="anchorProvinciaAmbitoSolicitado" href=""><span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonProvinciaAmbitoSolicitado" name="botonProvinciaAmbitoSolicitado" style="cursor:hand;" alt="<%=meLanbide47I18n.getMensaje(idiomaUsuario, "altDesplegable")%>" title="<%=meLanbide47I18n.getMensaje(idiomaUsuario, "altDesplegable")%>"></span></a>
                        </div>
                    </div>
                </div>
                <div class="lineaFormulario">
                    <div style="width: 170px; float: left;">
                        <%=meLanbide47I18n.getMensaje(idiomaUsuario,"label.ambitos.nuevoAmbito.ambito")%>
                    </div>
                    <div style="width: 247px; float: left;">
                        <div style="float: left;">
                            <input id="codAmbitoSolicitado" name="codAmbitoSolicitado" type="text" class="inputTexto" size="2" maxlength="2" 
                                   onkeypress="javascript:return SoloDigitosConsulta(event);"  >
                            <input id="descAmbitoSolicitado" name="descAmbitoSolicitado" type="text" class="inputTexto" size="20" readonly>
                            <a id="anchorAmbitoSolicitado" name="anchorAmbitoSolicitado" href=""><span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonAmbito" name="botonAmbito" style="cursor:hand;" alt="<%=meLanbide47I18n.getMensaje(idiomaUsuario, "altDesplegable")%>" title="<%=meLanbide47I18n.getMensaje(idiomaUsuario, "altDesplegable")%>"></span></a>
                        </div>
                    </div>
                </div>
                <div class="lineaFormulario">
                    <div style="width: 170px; float: left;">
                        <%=meLanbide47I18n.getMensaje(idiomaUsuario,"label.ambitos.nuevoAmbito.bloquesSolicitados")%>
                    </div>
                    <div style="width: 450px; float: left;">
                        <input id="nroBloquesSolic" name="nroBloquesSolic" type="text" class="inputTexto" size="5" maxlength="5" 
                           value="<%=ambitoSoliModif != null && ambitoSoliModif.getOriAmbSolNroBloques() != null ? ambitoSoliModif.getOriAmbSolNroBloques().toString() : ""%>"/>
                    </div>
                </div>
                <div class="lineaFormulario">
                    <div style="width: 170px; float: left;">
                        <%=meLanbide47I18n.getMensaje(idiomaUsuario,"label.ambitos.nuevoAmbito.numeroubicaciones")%>
                    </div>
                    <div style="width: 450px; float: left;">
                        <input id="nroUbicacionesxAmbito" name="nroUbicacionesxAmbito" type="text" class="inputTexto" size="5" maxlength="5" 
                           value="<%=ambitoSoliModif != null && ambitoSoliModif.getOriAmbSolNroUbic() != null ? ambitoSoliModif.getOriAmbSolNroUbic().toString() : ""%>"/>
                    </div>
                </div>
                <div class="botonera">
                    <input type="button" id="btnAceptar" name="btnAceptar" class="botonGeneral" value="<%=meLanbide47I18n.getMensaje(idiomaUsuario, "btn.aceptar")%>" onclick="guardarDatosAmbitoSolicitado();">
                    <input type="button" id="btnCancelar" name="btnCancelar" class="botonGeneral" value="<%=meLanbide47I18n.getMensaje(idiomaUsuario, "btn.cancelar")%>" onclick="cancelar();">
                </div>
            </div>
        
        <script type="text/javascript">
            var comboProvinciasAmbitoSolicitado = new Combo("ProvinciaAmbitoSolicitado");
            comboProvinciasAmbitoSolicitado.change = cargarAmbitosPorProvinciaAmbitoSolicitado;
            var comboAmbitosSolicitado = new Combo("AmbitoSolicitado");
        </script>
    </body>
</html>

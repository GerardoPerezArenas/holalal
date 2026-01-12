<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide47.i18n.MeLanbide47I18n" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide47.vo.orientacion.OriUbicVO" %>
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
            OriUbicVO ubicModif = new OriUbicVO();
            String codOrganizacion = "";
            String numExpediente = "";
            int anionumExpediente =0;
            String convocatoria = "";
            int nuevaCon = 0;
            String codEntidad = "";
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
            codEntidad = request.getParameter("codEntidad");
            nuevo = (String)request.getAttribute("nuevo");
            convocatoria = (String)request.getAttribute("nuevaCon");
            nuevaCon = Integer.parseInt(convocatoria);
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
            
            if(request.getAttribute("ubicModif") != null)
            {
                ubicModif = (OriUbicVO)request.getAttribute("ubicModif");
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
            var codProvincias = [<%=lcodProvincias%>];
            var descProvincias = [<%=ldescProvincias%>];
            var codAmbitos = new Array();
            var descAmbitos = new Array();
            var codMunicipios = new Array();
            var descMunicipios = new Array();
            
            var mensajeValidacion = '';
            
            
            function inicio(){
                cargarCombos();
                cargarCodigosCombos();
                cargarDescripcionesCombos();
            }
            
            function cargarCombos() {
                comboProvincias.addItems(codProvincias, descProvincias);
            }
            
            function cargarCodigosCombos(){
                document.getElementById('codProvincia').value = '<%=ubicModif != null && ubicModif.getMunPrv() != null ? String.valueOf(ubicModif.getMunPrv()) : ""%>';
                document.getElementById('codAmbito').value = '<%=ubicModif != null && ubicModif.getOriAmbCod() != null ? String.valueOf(ubicModif.getOriAmbCod()) : ""%>';
                document.getElementById('codMunCE').value = '<%=ubicModif != null && ubicModif.getMunCod() != null ? String.valueOf(ubicModif.getMunCod()) : ""%>';
            }
            
            function cargarDescripcionesCombos(){
                var desc = "";
                var codAct = "";
                var codigo = "";

                
                //Provincias
                codigo = "<%=ubicModif != null && ubicModif.getMunPrv() != null ? String.valueOf(ubicModif.getMunPrv()) : ""%>";
                if(codigo != null && codigo != "")
                {
                    for(var i=0; i<codProvincias.length; i++)
                    {
                        codAct = codProvincias[i];
                        if(codAct == codigo)
                        {
                            desc = descProvincias[i];
                            cargarAmbitosPorProvincia();
                            document.getElementById('codAmbito').value = '<%=ubicModif != null && ubicModif.getOriAmbCod() != null ? String.valueOf(ubicModif.getOriAmbCod()) : ""%>';
                        }
                    }
                }
                document.getElementById('descProvincia').value = desc;
                
                desc = "";
                codAct = "";
                codigo = "";
                //Ambitos
                codigo = "<%=ubicModif != null && ubicModif.getOriAmbCod() != null ? String.valueOf(ubicModif.getOriAmbCod()) : ""%>";
                if(codigo != null && codigo != "")
                {
                    for(var i=0; i<codAmbitos.length; i++)
                    {
                        codAct = codAmbitos[i];
                        if(codAct == codigo)
                        {
                            desc = descAmbitos[i];
                            cargarMunicipiosPorAmbitoProvincia();
                            document.getElementById('codMunCE').value = '<%=ubicModif != null && ubicModif.getMunCod() != null ? String.valueOf(ubicModif.getMunCod()) : ""%>';
                        }
                    }
                }
                document.getElementById('descAmbito').value = desc;
                
                desc = "";
                codAct = "";
                codigo = "";
                //Municipios
                codigo = "<%=ubicModif != null && ubicModif.getMunCod() != null ? String.valueOf(ubicModif.getMunCod()) : ""%>";
                if(codigo != null && codigo != "")
                {
                    for(var i=0; i<codMunicipios.length; i++)
                    {
                        codAct = codMunicipios[i];
                        if(codAct == codigo)
                        {
                            desc = descMunicipios[i];
                        }
                    }
                }
                document.getElementById('descMunCE').value = desc;
            }
            
            
        
            function cambioValorCheck(check){
                if(check.checked){
                    check.value="S";
                }
                else{
                    check.value="N";
                }
                if(check!=null && check != undefined && check.id=="checkLocalPreviaAprobado"){
                    if(check.checked){
                        document.getElementById("divMantenimientoRequisitosLPA").style.display = "block";
                    }else{
                        document.getElementById("checkMantieneRequisitosLPA").checked = false;
                        document.getElementById("divMantenimientoRequisitosLPA").style.display = "none";
                    }
                }
            }
            
            function cargarAmbitosPorProvincia(){
                var codProvincia = document.getElementById('codProvincia').value;
                
                if(provinciaCambiada == false){
                    var codProvinciaIni = '<%=ubicModif != null && ubicModif.getMunPrv() != null ? String.valueOf(ubicModif.getMunPrv()) : ""%>';
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
                                document.getElementById('codAmbito').value = '';
                                document.getElementById('descAmbito').value = '';
                                document.getElementById('codMunCE').value = '';
                                document.getElementById('descMunCE').value = '';
                            }
                            comboAmbitos = new Combo("Ambito");
                            comboAmbitos.change = cargarMunicipiosPorAmbitoProvincia;
                            codAmbitos = new Array();
                            descAmbitos = new Array();
                            for(var i = 0; i < listaAmbitos.length; i++){
                                codAmbitos[i] = listaAmbitos[i][0];
                                descAmbitos[i] = listaAmbitos[i][1];
                            }
                            comboAmbitos.addItems(codAmbitos, descAmbitos);
                            
                            comboMunicipios = new Combo("MunCE");
                            codMunicipios = new Array();
                            descMunicipios = new Array();
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
            
            function cargarMunicipiosPorAmbitoProvincia(){
                var codProvincia = document.getElementById('codProvincia').value;
                var codAmbito = document.getElementById('codAmbito').value;
                
                
                
                if(ambitoCambiado == false){
                    var codAmbitoIni = '<%=ubicModif != null && ubicModif.getOriAmbCod() != null ? String.valueOf(ubicModif.getOriAmbCod()) : ""%>';
                    if(codAmbitoIni != codAmbito){
                        ambitoCambiado = true;
                    }
                }
                
                prvMunicipios = new Array();
                if(codProvincia != null && codProvincia != '' && codAmbito != null && codAmbito != ''){
                    var ajax = getXMLHttpRequest();
                    var nodos = null;
                    var CONTEXT_PATH = '<%=request.getContextPath()%>'
                    var url = CONTEXT_PATH + "/PeticionModuloIntegracion.do";
                    var parametros = "";
                    var control = new Date();
                    parametros = 'tarea=preparar&modulo=MELANBIDE47&operacion=cargarMunicipiosPorAmbitoProvinciaHoras&tipo=0&numero=<%=numExpediente%>&codProvincia='+codProvincia+'&codAmbito='+codAmbito+'&control='+control.getTime();
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
                        var listaMunicipios = new Array();
                        var fila = new Array();
                        var nodoFila;
                        var hijosFila;
                        var contMunicipios = 0;
                        for(j=0;hijos!=null && j<hijos.length;j++){
                            if(hijos[j].nodeName=="CODIGO_OPERACION"){                            
                                codigoOperacion = hijos[j].childNodes[0].nodeValue;
                            }//if(hijos[j].nodeName=="CODIGO_OPERACION")                         
                            else if(hijos[j].nodeName=="ITEM_MUNICIPIO"){
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
                                    else if(hijosFila[cont].nodeName=="PRV"){
                                        if(hijosFila[cont].childNodes.length > 0){
                                            prvMunicipios[j] = hijosFila[cont].childNodes[0].nodeValue;
                                        }
                                        else{
                                            prvMunicipios[j] = '-1';
                                        }
                                    }
                                }
                                listaMunicipios[contMunicipios] = fila;
                                contMunicipios++;
                                fila = new Array();
                            }  
                        }//for(j=0;hijos!=null && j<hijos.length;j++)
                        if(codigoOperacion=="0"){
                            if(ambitoCambiado == true){
                                document.getElementById('codMunCE').value = '';
                                document.getElementById('descMunCE').value = '';
                            }
                            comboMunicipios = new Combo("MunCE");
                            codMunicipios = new Array();
                            descMunicipios = new Array();
                            for(var i = 0; i < listaMunicipios.length; i++){
                                codMunicipios[i] = listaMunicipios[i][0];
                                descMunicipios[i] = listaMunicipios[i][1];
                            }
                            comboMunicipios.addItems(codMunicipios, descMunicipios);
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
            
            function guardarDatos(){
                if(validarDatos()){
                    var ajax = getXMLHttpRequest();
                    var nodos = null;
                    var CONTEXT_PATH = '<%=request.getContextPath()%>'
                    var url = CONTEXT_PATH + "/PeticionModuloIntegracion.do";
                    var parametros = "";
                    var codProv = getProvinciaDeMunicipioSeleccionado(document.getElementById('codMunCE').value);
                    //var codProv = document.getElementById('codProvincia').value;
                    parametros = "tarea=preparar&modulo=MELANBIDE47&operacion=guardarAmbitoORI&tipo=0&numero=<%=numExpediente%>&nuevaCon=<%=nuevaCon%>"
                            +"&codEntidad=<%=codEntidad != null ? codEntidad : ""%>"
                            +"&idUbic=<%=ubicModif != null && ubicModif.getOriOrientUbicCod() != null ? ubicModif.getOriOrientUbicCod().toString() : ""%>"
                            +"&provincia="+codProv+"&ambito="+document.getElementById('codAmbito').value+"&municipio="+document.getElementById('codMunCE').value
                            +"&codPostal="+document.getElementById('codPostal').value
                            +"&direccion="+escape(document.getElementById('direccion').value)
                            +"&direccionNumero="+escape(document.getElementById('direccionNumero').value)
                            +"&direccionPiso="+escape(document.getElementById('direccionPiso').value)
                            +"&direccionLetra="+escape(document.getElementById('direccionLetra').value)
                            +"&horas="+(document.getElementById('horasSolic') != null ? document.getElementById('horasSolic').value : "")
                            +"&despachos="+(document.getElementById('checkDespachos')!=null? document.getElementById('checkDespachos').value : "")
                            +"&segundaAula="+(document.getElementById('checkSegundaAula')!=null? document.getElementById('checkSegundaAula').value : "")
                            +"&DispMas1Ubicacion="+(document.getElementById('checkDispMas1Ubicacion')!=null? document.getElementById('checkDispMas1Ubicacion').value : "")
                            +"&EspacioOrdeWifi="+(document.getElementById('checkEspacioOrdeWifi')!=null? document.getElementById('checkEspacioOrdeWifi').value : "")
                            +"&ubicTelFijo="+(document.getElementById('ubicTelFijo')!=null? document.getElementById('ubicTelFijo').value : "")
                            +"&localPreviaAprobado="+(document.getElementById('checkLocalPreviaAprobado')!=null && document.getElementById('checkLocalPreviaAprobado').checked ? 1 : 0)
                            +"&mantieneRequisitosLPA="+(document.getElementById('checkLocalPreviaAprobado')!=null && document.getElementById('checkLocalPreviaAprobado').checked ?
                                                            (document.getElementById('checkMantieneRequisitosLPA')!=null && document.getElementById('checkMantieneRequisitosLPA').checked ? 1 : 0) : "")
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
                        var listaUbicaciones = new Array();
                        var fila = new Array();
                        var nodoFila;
                        var hijosFila;
                        for(j=0;hijos!=null && j<hijos.length;j++){
                            if(hijos[j].nodeName=="CODIGO_OPERACION"){                            
                                codigoOperacion = hijos[j].childNodes[0].nodeValue;
                                listaUbicaciones[j] = codigoOperacion;
                            }//if(hijos[j].nodeName=="CODIGO_OPERACION")                        
                            else if(hijos[j].nodeName=="CODIGO_ENTIDAD"){
                                listaUbicaciones[j] = hijos[j].childNodes[0].nodeValue;
                            }                    
                            else if(hijos[j].nodeName=="FILA"){
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
                                    else if(hijosFila[cont].nodeName=="PROVINCIA"){
                                        if(hijosFila[cont].childNodes.length > 0){
                                            fila[1] = hijosFila[cont].childNodes[0].nodeValue;
                                        }
                                        else{
                                            fila[1] = '-';
                                        }
                                    }
                                    else if(hijosFila[cont].nodeName=="AMBITO"){
                                        if(hijosFila[cont].childNodes.length > 0){
                                            fila[2] = hijosFila[cont].childNodes[0].nodeValue;
                                        }
                                        else{
                                            fila[2] = '-';
                                        }
                                    }
                                    else if(hijosFila[cont].nodeName=="MUNICIPIO"){
                                        if(hijosFila[cont].childNodes.length > 0){
                                            fila[3] = hijosFila[cont].childNodes[0].nodeValue;
                                        }
                                        else{
                                            fila[3] = '-';
                                        }
                                    }
                                    else if(hijosFila[cont].nodeName=="DIRECCION"){
                                        if(hijosFila[cont].childNodes.length > 0){
                                            fila[4] = hijosFila[cont].childNodes[0].nodeValue;
                                        }
                                        else{
                                            fila[4] = '-';
                                        }
                                    }
                                    else if(hijosFila[cont].nodeName=="COD_POSTAL"){
                                        if(hijosFila[cont].childNodes.length > 0){
                                            fila[5] = hijosFila[cont].childNodes[0].nodeValue;
                                        }
                                        else{
                                            fila[5] = '-';
                                        }
                                    }
                                    else if(hijosFila[cont].nodeName=="HORAS"){
                                        if(hijosFila[cont].childNodes.length > 0){
                                            fila[6] = hijosFila[cont].childNodes[0].nodeValue;
                                        }
                                        else{
                                            fila[6] = '-';
                                        }
                                    }
                                    else if(hijosFila[cont].nodeName=="DESPACHOS"){
                                        if(hijosFila[cont].childNodes.length > 0){
                                            fila[7] = hijosFila[cont].childNodes[0].nodeValue;
                                        }
                                        else{
                                            fila[7] = '-';
                                        }
                                    }
                                    else if(hijosFila[cont].nodeName=="AULAGRUPAL"){
                                        if(hijosFila[cont].childNodes.length > 0){
                                            fila[8] = hijosFila[cont].childNodes[0].nodeValue;
                                        }
                                        else{
                                            fila[8] = '-';
                                        }
                                    }
                                    else if(hijosFila[cont].nodeName=="VALORACION"){
                                        if(hijosFila[cont].childNodes.length > 0){
                                            fila[9] = hijosFila[cont].childNodes[0].nodeValue;
                                        }
                                        else{
                                            fila[9] = '-';
                                        }
                                    }
                                    else if(hijosFila[cont].nodeName=="TOTAL"){
                                        if(hijosFila[cont].childNodes.length > 0){
                                            fila[10] = hijosFila[cont].childNodes[0].nodeValue;
                                        }
                                        else{
                                            fila[10] = '-';
                                        }
                                    }
                                    else if(hijosFila[cont].nodeName=="HORASADJ"){
                                        if(hijosFila[cont].childNodes.length > 0){
                                            fila[11] = hijosFila[cont].childNodes[0].nodeValue;
                                        }
                                        else{
                                            fila[11] = '-';
                                        }
                                    }
                                    else if(hijosFila[cont].nodeName=="TELEFIJO_UBICACION"){
                                        if(hijosFila[cont].childNodes.length > 0){
                                            fila[12] = hijosFila[cont].childNodes[0].nodeValue;
                                        }
                                        else{
                                            fila[12] = '-';
                                        }
                                    }
                                    else if(hijosFila[cont].nodeName=="ESPACIOADICIONA"){
                                        if(hijosFila[cont].childNodes.length > 0){
                                            fila[13] = hijosFila[cont].childNodes[0].nodeValue;
                                        }
                                        else{
                                            fila[13] = '-';
                                        }
                                    }
                                    else if(hijosFila[cont].nodeName=="ESPHERRABUSQEMP"){
                                        if(hijosFila[cont].childNodes.length > 0){
                                            fila[14] = hijosFila[cont].childNodes[0].nodeValue;
                                        }
                                        else{
                                            fila[14] = '-';
                                        }
                                    }
                                    else if(hijosFila[cont].nodeName=="ORI_LOCALPREVAPRO"){
                                        if(hijosFila[cont].childNodes.length > 0){
                                            fila[15] = hijosFila[cont].childNodes[0].nodeValue;
                                        }
                                        else{
                                            fila[15] = '-';
                                        }
                                    }
                                    else if(hijosFila[cont].nodeName=="ORI_MATENREQ_LPA"){
                                        if(hijosFila[cont].childNodes.length > 0){
                                            fila[16] = hijosFila[cont].childNodes[0].nodeValue;
                                        }
                                        else{
                                            fila[16] = '-';
                                        }
                                    }
                                    else if(hijosFila[cont].nodeName=="ORI_ORIENT_NUMERO_UBICACION"){
                                        if(hijosFila[cont].childNodes.length > 0){
                                            fila[17] = hijosFila[cont].childNodes[0].nodeValue;
                                        }
                                        else{
                                            fila[17] = '';
                                        }
                                    }
                                    else if(hijosFila[cont].nodeName=="ORI_ORIENT_PISO_UBICACION"){
                                        if(hijosFila[cont].childNodes.length > 0){
                                            fila[18] = hijosFila[cont].childNodes[0].nodeValue;
                                        }
                                        else{
                                            fila[18] = '';
                                        }
                                    }
                                    else if(hijosFila[cont].nodeName=="ORI_ORIENT_LETRA_UBICACION"){
                                        if(hijosFila[cont].childNodes.length > 0){
                                            fila[19] = hijosFila[cont].childNodes[0].nodeValue;
                                        }
                                        else{
                                            fila[19] = '';
                                        }
                                    }
                                    else if(hijosFila[cont].nodeName=="ORI_PLANIGUALDAD"){
                                        if(hijosFila[cont].childNodes.length > 0){
                                            fila[20] = hijosFila[cont].childNodes[0].nodeValue;
                                        }
                                        else{
                                            fila[20] = '';
                                        }
                                    }
                                    else if(hijosFila[cont].nodeName=="ORI_CERTCALIDAD"){
                                        if(hijosFila[cont].childNodes.length > 0){
                                            fila[21] = hijosFila[cont].childNodes[0].nodeValue;
                                        }
                                        else{
                                            fila[21] = '';
                                        }
                                    }
                                }
                                listaUbicaciones[j] = fila;
                                fila = new Array();
                            }   
                        }//for(j=0;hijos!=null && j<hijos.length;j++)
                        if(codigoOperacion=="0"){
                            //jsp_alerta("A",'Correcto');
                            self.parent.opener.retornoXanelaAuxiliar( listaUbicaciones);
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
                var codProvincia = document.getElementById('codProvincia').value;
                if(codProvincia == null || codProvincia == ''){
                    mensajeValidacion = '<%=meLanbide47I18n.getMensaje(idiomaUsuario, "msg.ambitos.nuevoAmbito.todosCamposOblig")%>';
                    return false;
                }
                
                var codAmbito = document.getElementById('codAmbito').value;
                if(codAmbito == null || codAmbito == ''){
                    mensajeValidacion = '<%=meLanbide47I18n.getMensaje(idiomaUsuario, "msg.ambitos.nuevoAmbito.todosCamposOblig")%>';
                    return false;
                }
                
                var codMun = document.getElementById('codMunCE').value;
                if(codMun == null || codMun == ''){
                    mensajeValidacion = '<%=meLanbide47I18n.getMensaje(idiomaUsuario, "msg.ambitos.nuevoAmbito.todosCamposOblig")%>';
                    return false;
                }
                
                var telFijo = document.getElementById('ubicTelFijo').value;
                var anioExpediente = <%=anionumExpediente%>;
                if((telFijo == null || telFijo == '') && anioExpediente >= 2017 ){
                    mensajeValidacion = '<%=meLanbide47I18n.getMensaje(idiomaUsuario, "msg.ambitos.nuevoAmbito.camposOblig.telefonoFijo")%>';
                    return false;
                }
                
                var direccion = document.getElementById('direccion').value;
                if(direccion == null || direccion == ''){
                    mensajeValidacion = '<%=meLanbide47I18n.getMensaje(idiomaUsuario, "msg.ambitos.nuevoAmbito.todosCamposOblig")%>';
                    return false;
                }else{
                    if(!comprobarCaracteresEspecialesOri14(direccion)){
                        document.getElementById('direccion').style.border = '1px solid red';
                        mensajeValidacion = '<%=meLanbide47I18n.getMensaje(idiomaUsuario, "msg.ambitos.nuevoAmbito.direccionCaracteresEspeciales")%>';
                        return false;
                    }else{
                        document.getElementById('direccion').removeAttribute("style");
                    }
                }
                
                var codPostal = document.getElementById('codPostal').value;
                if(codPostal == null || codPostal == ''){
                    mensajeValidacion = '<%=meLanbide47I18n.getMensaje(idiomaUsuario, "msg.ambitos.nuevoAmbito.todosCamposOblig")%>';
                    return false;
                }else{
                    //El codigo postal no se comprueba para EIBAR-ERMUA-MALLABIA
                    var codAmbito = document.getElementById('codAmbito').value;
                    var codMun = document.getElementById('codMunCE').value;
                    if((codAmbito != '26' && codAmbito != '92')  || (codMun != '34' && codMun != '58'))
                    {
                        if(!validarCodPostalOri14(codPostal, codProvincia)){
                            document.getElementById('codPostal').style.border = '1px solid red';
                            mensajeValidacion = '<%=meLanbide47I18n.getMensaje(idiomaUsuario, "msg.ambitos.nuevoAmbito.codPostalIncorrecto")%>';
                            return false;
                        }else{
                            document.getElementById('codPostal').removeAttribute("style");
                        }
                    }
                }
                
                var correcto = true;
                var anioExpJavaScript = <%=anionumExpediente%> ;
                if(anioExpJavaScript!=null && anioExpJavaScript <2018){
                    var horas = (document.getElementById('horasSolic')!=null ? document.getElementById('horasSolic').value : "");
                    if(horas == null || horas == ''){
                        mensajeValidacion = '<%=meLanbide47I18n.getMensaje(idiomaUsuario, "msg.ambitos.nuevoAmbito.todosCamposOblig")%>';
                        return false;
                    }


                    if(!validarNumerico(document.getElementById('horasSolic'))){
                        document.getElementById('horasSolic').style.border = '1px solid red';
                        if(mensajeValidacion == '')
                            mensajeValidacion = '<%=meLanbide47I18n.getMensaje(idiomaUsuario, "msg.ambitos.nuevoAmbito.numHorasIncorrecto")%>';
                        correcto = false;
                    }
                    else{
                        try{
                            var horas = parseInt(document.getElementById('horasSolic').value);
                            if(horas < 0){
                                document.getElementById('horasSolic').style.border = '1px solid red';
                                if(mensajeValidacion == '')
                                    mensajeValidacion = '<%=meLanbide47I18n.getMensaje(idiomaUsuario, "msg.ambitos.nuevoAmbito.numHorasIncorrecto")%>';
                                correcto = false;
                            }else{
                                document.getElementById('horasSolic').removeAttribute("style");
                            }
                        }
                        catch(err){
                            document.getElementById('horasSolic').style.border = '1px solid red';
                            if(mensajeValidacion == '')
                                mensajeValidacion = '<%=meLanbide47I18n.getMensaje(idiomaUsuario, "msg.ambitos.nuevoAmbito.numHorasIncorrecto")%>';
                            correcto = false;
                        }
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
    <body onload="inicio();" class="contenidoPantalla">
        <form>
            <div style="width: 100%; padding: 10px; text-align: left;">
                <div class="tituloAzul" style="clear: both; text-align: left;">
                    <span>
                        <%=meLanbide47I18n.getMensaje(idiomaUsuario,"label.ambitos.nuevoAmbito.datosUbicacion")%>
                    </span>
                </div>
                <div class="lineaFormulario">
                    <div style="width: 170px; float: left;">
                        <%=meLanbide47I18n.getMensaje(idiomaUsuario,"label.ambitos.nuevoAmbito.territorioHistorico")%>
                    </div>
                    <div style="width: 450px; float: left;">
                        <div style="float: left;">
                            <input id="codProvincia" name="codProvincia" type="text" class="inputTexto" size="2" maxlength="2" 
                                   onkeypress="javascript:return SoloDigitosConsulta(event);" >
                            <input id="descProvincia" name="descProvincia" type="text" class="inputTexto" size="15" readonly>
                            <a id="anchorProvincia" name="anchorProvincia" href=""><span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonProvincia" name="botonProvincia" style="cursor:hand;" alt="<%=meLanbide47I18n.getMensaje(idiomaUsuario, "altDesplegable")%>" title="<%=meLanbide47I18n.getMensaje(idiomaUsuario, "altDesplegable")%>"></span></a>
                        </div>
                    </div>
                </div>
                <div class="lineaFormulario">
                    <div style="width: 170px; float: left;">
                        <%=meLanbide47I18n.getMensaje(idiomaUsuario,"label.ambitos.nuevoAmbito.ambito")%>
                    </div>
                    <div style="width: 247px; float: left;">
                        <div style="float: left;">
                            <input id="codAmbito" name="codAmbito" type="text" class="inputTexto" size="2" maxlength="2" 
                                   onkeypress="javascript:return SoloDigitosConsulta(event);"  >
                            <input id="descAmbito" name="descAmbito" type="text" class="inputTexto" size="20" readonly>
                            <a id="anchorAmbito" name="anchorAmbito" href=""><span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonAmbito" name="botonAmbito" style="cursor:hand;" alt="<%=meLanbide47I18n.getMensaje(idiomaUsuario, "altDesplegable")%>" title="<%=meLanbide47I18n.getMensaje(idiomaUsuario, "altDesplegable")%>"></span></a>
                        </div>
                    </div>

                    <div style="width: 170px; float: left;">
                        <%=meLanbide47I18n.getMensaje(idiomaUsuario,"label.ambitos.nuevoAmbito.municipioCentroEmpleo")%>
                    </div>
                    <div style="width: 350px; float: left;">
                        <div style="float: left;">
                            <input id="codMunCE" name="codMunCE" type="text" class="inputTexto" size="2" maxlength="2" 
                                   onkeypress="javascript:return SoloDigitosConsulta(event);" >
                            <input id="descMunCE" name="descMunCE" type="text" class="inputTexto" size="35" readonly>
                            <a id="anchorMunCE" name="anchorMunCE" href=""><span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonMunCE" name="botonMunCE" style="cursor:hand;" alt="<%=meLanbide47I18n.getMensaje(idiomaUsuario, "altDesplegable")%>" title="<%=meLanbide47I18n.getMensaje(idiomaUsuario, "altDesplegable")%>"></span></a>
                        </div>
                    </div>
                </div>
                <div class="lineaFormulario">
                    <div style="width: 170px; float: left;">
                        <%=meLanbide47I18n.getMensaje(idiomaUsuario,"label.ambitos.nuevoAmbito.direccion")%>
                    </div>
                    <div style="width: 450px; float: left;">
                        <input id="direccion" name="direccion" type="text" class="inputTexto" size="100" maxlength="100" 
                           value="<%=ubicModif != null && ubicModif.getOriOrientDireccion() != null ? ubicModif.getOriOrientDireccion().toUpperCase() : ""%>"/>
                    </div>
                </div>
                <div class="lineaFormulario">
                    <div style="width: 170px; float: left;">
                        <%=meLanbide47I18n.getMensaje(idiomaUsuario,"label.ambitos.nuevoAmbito.direccionNumero")%>
                    </div>
                    <div style="width: 150px; float: left;">
                        <input id="direccionNumero" name="direccionNumero" type="text" class="inputTexto" size="5" maxlength="5" 
                           value="<%=ubicModif != null && ubicModif.getOriOrientUbicaNumero() != null ? ubicModif.getOriOrientUbicaNumero() : ""%>"/>
                    </div>
                    <div style="width: 100px; float: left;">
                        <%=meLanbide47I18n.getMensaje(idiomaUsuario,"label.ambitos.nuevoAmbito.direccionPiso")%>
                    </div>
                    <div style="width: 150px; float: left;">
                        <input id="direccionPiso" name="direccionPiso" type="text" class="inputTexto" size="5" maxlength="5" 
                           value="<%=ubicModif != null && ubicModif.getOriOrientUbicaPiso() != null ? ubicModif.getOriOrientUbicaPiso() : ""%>"/>
                    </div>
                    <div style="width: 100px; float: left;">
                        <%=meLanbide47I18n.getMensaje(idiomaUsuario,"label.ambitos.nuevoAmbito.direccionLetra")%>
                    </div>
                    <div style="width: 150px; float: left;">
                        <input id="direccionLetra" name="direccionLetra" type="text" class="inputTexto" size="5" maxlength="5" 
                           value="<%=ubicModif != null && ubicModif.getOriOrientUbicaLetra() != null ? ubicModif.getOriOrientUbicaLetra().toUpperCase() : ""%>"/>
                    </div>
                </div>
                <div class="lineaFormulario">
                    <div style="width: 170px; float: left;">
                        <%=meLanbide47I18n.getMensaje(idiomaUsuario,"label.ambitos.nuevoAmbito.codPostal")%>
                    </div>
                    <div style="width: 450px; float: left;">
                        <input id="codPostal" name="codPostal" type="text" class="inputTexto" size="5" maxlength="5" 
                           value="<%=ubicModif != null && ubicModif.getOriOrientCP() != null ? ubicModif.getOriOrientCP().toUpperCase() : ""%>"/>
                    </div>
                </div>
                <% if (anionumExpediente<2018) { %>    
                <div class="lineaFormulario">
                    <div style="width: 170px; float: left;">
                        <%=meLanbide47I18n.getMensaje(idiomaUsuario,"label.ambitos.nuevoAmbito.bloquesSolicitados")%>
                    </div>
                    <div style="width: 450px; float: left;">
                        <input id="horasSolic" name="horasSolic" type="text" class="inputTexto" size="5" maxlength="5" 
                           value="<%=ubicModif != null && ubicModif.getOriOrientHorasSolicitadas() != null ? ubicModif.getOriOrientHorasSolicitadas().toString() : ""%>"/>
                    </div>
                </div>
                <% } %>    
                <div class="lineaFormulario">
                    <div style="width: 170px; float: left;">
                        <%=meLanbide47I18n.getMensaje(idiomaUsuario,"label.ambitos.nuevoAmbito.telefonoFijo")%>
                    </div>
                    <div style="width: 450px; float: left;">
                        <input id="ubicTelFijo" name="ubicTelFijo" type="text" class="inputTexto" size="25" maxlength="25" 
                           value="<%=ubicModif != null && ubicModif.getOriOrientUbicaTeleFijo() != null ? ubicModif.getOriOrientUbicaTeleFijo().toString() : ""%>"/>
                    </div>
                </div>    
                <% if (anionumExpediente<2017) {%>
                <div class="lineaFormulario">
                    <div style="width: 170px; float: left;">
                        <%=meLanbide47I18n.getMensaje(idiomaUsuario,"label.ambitos.nuevoAmbito.dispEspComp")%>
                    </div>
                    <div style="width: 450px; float: left;">
                        <input type="checkBox" id="checkDespachos" name="checkDespachos" value="<%=ubicModif != null && ubicModif.getOriOrientDespachos() != null && ubicModif.getOriOrientDespachos().equals("S") ? "S" : "N"%>" <%=ubicModif != null && ubicModif.getOriOrientDespachos() != null && ubicModif.getOriOrientDespachos().equals("S") ? "checked" : ""%> onchange="cambioValorCheck(this);"></input>
                    </div>
                </div>
                <div class="lineaFormulario">
                    <div style="width: 170px; float: left;">
                        <%=meLanbide47I18n.getMensaje(idiomaUsuario,"label.ambitos.nuevoAmbito.segundaAula")%>
                    </div>
                    <div style="width: 450px; float: left;">
                        <input type="checkBox" id="checkSegundaAula" name="checkSegundaAula" value="<%=ubicModif != null && ubicModif.getOriOrientAulagrupal() != null && ubicModif.getOriOrientAulagrupal().equals("S") ? "S" : "N"%>" <%=ubicModif != null && ubicModif.getOriOrientAulagrupal() != null && ubicModif.getOriOrientAulagrupal().equals("S") ? "checked" : ""%> onchange="cambioValorCheck(this);"></input>
                    </div>
                </div>
                <% }%>
                <% if (anionumExpediente>=2017) {%>
                    <% if (anionumExpediente<2018) { %>
                    <div class="lineaFormulario">
                        <div style="width: 70%; float: left;">
                            <%=meLanbide47I18n.getMensaje(idiomaUsuario,"label.ambitos.nuevoAmbito.dispMas1Ubicacion")%>
                        </div>
                        <div style="width: 30%; float: left; text-align:-webkit-center">
                            <input type="checkBox" id="checkDispMas1Ubicacion" name="checkDispMas1Ubicacion" value="<%=ubicModif != null && ubicModif.getOriOrientUbicaEspacioAdicional() != null && ubicModif.getOriOrientUbicaEspacioAdicional().equals("S") ? "S" : "N"%>" <%=ubicModif != null && ubicModif.getOriOrientUbicaEspacioAdicional() != null && ubicModif.getOriOrientUbicaEspacioAdicional().equals("S") ? "checked" : ""%> onchange="cambioValorCheck(this);"></input>
                        </div>
                    </div>
                   <% } %>
                <div class="lineaFormulario">
                    <div style="width: 70%; float: left; ">
                        <%=meLanbide47I18n.getMensaje(idiomaUsuario,"label.ambitos.nuevoAmbito.espacioOrdeWifi")%>
                    </div>
                    <div style="width: 30%; float: left; text-align:-webkit-center">
                        <input type="checkBox" id="checkEspacioOrdeWifi" name="checkEspacioOrdeWifi" value="<%=ubicModif != null && ubicModif.getOriOrientUbicaEspAdicioHerrBusqEmpleo() != null && ubicModif.getOriOrientUbicaEspAdicioHerrBusqEmpleo().equals("S") ? "S" : "N"%>" <%=ubicModif != null && ubicModif.getOriOrientUbicaEspAdicioHerrBusqEmpleo() != null && ubicModif.getOriOrientUbicaEspAdicioHerrBusqEmpleo().equals("S") ? "checked" : ""%> onchange="cambioValorCheck(this);"></input>
                    </div>
                </div>
                <% if (anionumExpediente>=2018) { %>
                    <div class="lineaFormulario">
                        <div style="width: 70%; float: left; ">
                            <%=meLanbide47I18n.getMensaje(idiomaUsuario,"label.ambitos.nuevoAmbito.localpreviamenteaprobado")%>
                        </div>
                        <div style="width: 30%; float: left; text-align:-webkit-center">
                            <input type="checkBox" id="checkLocalPreviaAprobado" name="checkLocalPreviaAprobado" <%=ubicModif != null && ubicModif.getOriCELocalPreviamenteAprobado() != null && ubicModif.getOriCELocalPreviamenteAprobado()==1 ? "checked" : ""%> onchange="cambioValorCheck(this);"/>
                        </div>
                    </div>
                        <div class="lineaFormulario" id="divMantenimientoRequisitosLPA">
                        <div style="width: 70%; float: left; ">
                            <%=meLanbide47I18n.getMensaje(idiomaUsuario,"label.ambitos.nuevoAmbito.mantienerequisitosLPA")%>
                        </div>
                        <div style="width: 30%; float: left; text-align:-webkit-center">
                            <input type="checkBox" id="checkMantieneRequisitosLPA" name="checkMantieneRequisitosLPA" <%=ubicModif != null && ubicModif.getOriCEMantenimientoRequisitosLPA() != null && ubicModif.getOriCEMantenimientoRequisitosLPA()==1 ? "checked" : ""%> onchange="cambioValorCheck(this);"/>
                        </div>
                    </div>
                <% } %>
                <% }%>
                <div class="botonera">
                    <input type="button" id="btnAceptar" name="btnAceptar" class="botonGeneral" value="<%=meLanbide47I18n.getMensaje(idiomaUsuario, "btn.aceptar")%>" onclick="guardarDatos();">
                    <input type="button" id="btnCancelar" name="btnCancelar" class="botonGeneral" value="<%=meLanbide47I18n.getMensaje(idiomaUsuario, "btn.cancelar")%>" onclick="cancelar();">
                </div>
            </div>
        </form>
                    
                    
        <script type="text/javascript">
            //Persona contratada
            var comboProvincias = new Combo("Provincia");
            comboProvincias.change = cargarAmbitosPorProvincia;
            var comboAmbitos = new Combo("Ambito");
            comboAmbitos.change = cargarMunicipiosPorAmbitoProvincia;
            var comboMunicipios = new Combo("MunCE");        
            var prvMunicipios = new Array();
            // Carga Inicial Hacemos dinamico el nuevo campo
            if(document.getElementById("checkLocalPreviaAprobado")!=null){
                    if(document.getElementById("checkLocalPreviaAprobado").checked){
                        if(document.getElementById("divMantenimientoRequisitosLPA")!=null)
                            document.getElementById("divMantenimientoRequisitosLPA").style.display = "block";
                    }else{
                        document.getElementById("checkMantieneRequisitosLPA").checked = false;
                        document.getElementById("divMantenimientoRequisitosLPA").style.display = "none";
                    }
                }
        </script>
    </body>
</html>

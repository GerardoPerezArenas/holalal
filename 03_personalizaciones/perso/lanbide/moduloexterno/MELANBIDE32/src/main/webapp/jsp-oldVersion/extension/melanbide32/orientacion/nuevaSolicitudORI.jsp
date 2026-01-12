<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide32.i18n.MeLanbide32I18n" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide32.vo.orientacion.OriUbicacionVO" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide32.vo.comun.SelectItem" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="java.util.ArrayList" %>
<%@page import="java.util.List" %>
<%@page import="org.apache.commons.lang.StringEscapeUtils"%>

<html>
    <head> 
        <meta http-equiv="Content-Type" content="text/html" charset="ISO_8859-1">
        <%
            String lcodProvincias = "";
            String ldescProvincias = "";
            OriUbicacionVO ubicModif = new OriUbicacionVO();
            String codOrganizacion = "";
            String numExpediente = "";
            String nuevo = "1";
            MeLanbide32I18n meLanbide32I18n = MeLanbide32I18n.getInstance();
            int idiomaUsuario = 1;
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
                    }
                }
            }
            catch(Exception ex)
            {

            }
            
            codOrganizacion  = request.getParameter("codOrganizacionModulo");
            numExpediente    = request.getParameter("numero");
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
            
            if(request.getAttribute("ubicModif") != null)
            {
                ubicModif = (OriUbicacionVO)request.getAttribute("ubicModif");
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
        
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/estilo.css"/>
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/extension/melanbide32/melanbide32.css"/>
        
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/general.js"></script>
        <script type="text/javascript">
            var APP_CONTEXT_PATH = '<%=request.getContextPath()%>';
        </script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/validaciones.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/listaComboBox.js"></script>
        
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
            }
            
            function cargarAmbitosPorProvincia(){
                var codProvincia = document.getElementById('codProvincia').value;
                
                if(provinciaCambiada == false){
                    var codProvinciaIni = '<%=ubicModif != null && ubicModif.getMunPrv() != null ? String.valueOf(ubicModif.getMunPrv()) : ""%>';
                    if(codProvinciaIni != codProvincia){
                        provinciaCambiada = true;
                    }
                }
                
                var codProvincia = document.getElementById('codProvincia').value;
                if(codProvincia != null && codProvincia != ''){
                    var ajax = getXMLHttpRequest();
                    var nodos = null;
                    var CONTEXT_PATH = '<%=request.getContextPath()%>'
                    var url = CONTEXT_PATH + "/PeticionModuloIntegracion.do";
                    var parametros = "";
                    var control = new Date();
                    parametros = 'tarea=preparar&modulo=MELANBIDE32&operacion=cargarAmbitosHorasPorProvincia&tipo=0&numero=<%=numExpediente%>&codProvincia='+codProvincia+'&control='+control.getTime();
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
                            jsp_alerta("A",'<%=meLanbide32I18n.getMensaje(idiomaUsuario,"error.errorBD")%>');
                        }else if(codigoOperacion=="2"){
                            jsp_alerta("A",'<%=meLanbide32I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                        }else if(codigoOperacion=="3"){
                            jsp_alerta("A",'<%=meLanbide32I18n.getMensaje(idiomaUsuario,"error.pasoParametros")%>');
                        }else{
                            jsp_alerta("A",'<%=meLanbide32I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
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
                    parametros = 'tarea=preparar&modulo=MELANBIDE32&operacion=cargarMunicipiosPorAmbitoProvinciaHoras&tipo=0&numero=<%=numExpediente%>&codProvincia='+codProvincia+'&codAmbito='+codAmbito+'&control='+control.getTime();
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
                            jsp_alerta("A",'<%=meLanbide32I18n.getMensaje(idiomaUsuario,"error.errorBD")%>');
                        }else if(codigoOperacion=="2"){
                            jsp_alerta("A",'<%=meLanbide32I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                        }else if(codigoOperacion=="3"){
                            jsp_alerta("A",'<%=meLanbide32I18n.getMensaje(idiomaUsuario,"error.pasoParametros")%>');
                        }else{
                            jsp_alerta("A",'<%=meLanbide32I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
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
                    var nuevo = "<%=nuevo%>";
                    var codProv = getProvinciaDeMunicipioSeleccionado(document.getElementById('codMunCE').value);
                    if(nuevo != null && nuevo == "1"){
                        parametros = "tarea=preparar&modulo=MELANBIDE32&operacion=crearSolicitudORI&tipo=0&numero=<%=numExpediente%>"
                                +"&provincia="+codProv+"&ambito="+document.getElementById('codAmbito').value+"&municipio="+document.getElementById('codMunCE').value
                                +"&direccion="+escape(document.getElementById('direccion').value)
                                +"&horas="+document.getElementById('horasSolic').value
                                +"&despachos="+document.getElementById('numDespachos').value
                                +"&segundaAula="+document.getElementById('checkSegundaAula').value;
                    }
                    else{
                        parametros = "tarea=preparar&modulo=MELANBIDE32&operacion=modificarSolicitudORI&tipo=0&numero=<%=numExpediente%>"
                                +"&idUbic=<%=ubicModif != null && ubicModif.getOriOrientUbicCod() != null ? ubicModif.getOriOrientUbicCod().toString() : ""%>"
                                +"&provincia="+codProv+"&ambito="+document.getElementById('codAmbito').value+"&municipio="+document.getElementById('codMunCE').value
                                +"&direccion="+escape(document.getElementById('direccion').value)
                                +"&horas="+document.getElementById('horasSolic').value
                                +"&despachos="+document.getElementById('numDespachos').value
                                +"&segundaAula="+document.getElementById('checkSegundaAula').value;
                    }
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
                                    else if(hijosFila[cont].nodeName=="HORAS"){
                                        if(hijosFila[cont].childNodes.length > 0){
                                            fila[5] = hijosFila[cont].childNodes[0].nodeValue;
                                        }
                                        else{
                                            fila[5] = '-';
                                        }
                                    }
                                    else if(hijosFila[cont].nodeName=="DESPACHOS"){
                                        if(hijosFila[cont].childNodes.length > 0){
                                            fila[6] = hijosFila[cont].childNodes[0].nodeValue;
                                        }
                                        else{
                                            fila[6] = '-';
                                        }
                                    }
                                    else if(hijosFila[cont].nodeName=="AULAGRUPAL"){
                                        if(hijosFila[cont].childNodes.length > 0){
                                            fila[7] = hijosFila[cont].childNodes[0].nodeValue;
                                        }
                                        else{
                                            fila[7] = '-';
                                        }
                                    }
                                    else if(hijosFila[cont].nodeName=="VALORACION"){
                                        if(hijosFila[cont].childNodes.length > 0){
                                            fila[8] = hijosFila[cont].childNodes[0].nodeValue;
                                        }
                                        else{
                                            fila[8] = '-';
                                        }
                                    }
                                    else if(hijosFila[cont].nodeName=="TOTAL"){
                                        if(hijosFila[cont].childNodes.length > 0){
                                            fila[9] = hijosFila[cont].childNodes[0].nodeValue;
                                        }
                                        else{
                                            fila[9] = '-';
                                        }
                                    }
                                    else if(hijosFila[cont].nodeName=="HORASADJ"){
                                        if(hijosFila[cont].childNodes.length > 0){
                                            fila[10] = hijosFila[cont].childNodes[0].nodeValue;
                                        }
                                        else{
                                            fila[10] = '-';
                                        }
                                    }
                                }
                                listaUbicaciones[j] = fila;
                                fila = new Array();
                            }   
                        }//for(j=0;hijos!=null && j<hijos.length;j++)
                        if(codigoOperacion=="0"){
                            //jsp_alerta("A",'Correcto');
                            window.returnValue =  listaUbicaciones;
                            cerrarVentana();
                        }else if(codigoOperacion=="1"){
                            jsp_alerta("A",'<%=meLanbide32I18n.getMensaje(idiomaUsuario,"error.errorBD")%>');
                        }else if(codigoOperacion=="2"){
                            jsp_alerta("A",'<%=meLanbide32I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                        }else if(codigoOperacion=="3"){
                            jsp_alerta("A",'<%=meLanbide32I18n.getMensaje(idiomaUsuario,"error.pasoParametros")%>');
                        }else if(codigoOperacion=="4"){
                            jsp_alerta("A",'<%=meLanbide32I18n.getMensaje(idiomaUsuario,"error.expSinTercero")%>');
                        }else{
                                jsp_alerta("A",'<%=meLanbide32I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
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
                    mensajeValidacion = '<%=meLanbide32I18n.getMensaje(idiomaUsuario, "ori.msg.todosCamposOblig")%>';
                    return false;
                }
                
                var codAmbito = document.getElementById('codAmbito').value;
                if(codAmbito == null || codAmbito == ''){
                    mensajeValidacion = '<%=meLanbide32I18n.getMensaje(idiomaUsuario, "ori.msg.todosCamposOblig")%>';
                    return false;
                }
                
                var codMun = document.getElementById('codMunCE').value;
                if(codMun == null || codMun == ''){
                    mensajeValidacion = '<%=meLanbide32I18n.getMensaje(idiomaUsuario, "ori.msg.todosCamposOblig")%>';
                    return false;
                }
                
                var direccion = document.getElementById('direccion').value;
                if(direccion == null || direccion == ''){
                    mensajeValidacion = '<%=meLanbide32I18n.getMensaje(idiomaUsuario, "ori.msg.todosCamposOblig")%>';
                    return false;
                }else{
                    if(!comprobarCaracteresEspeciales(direccion)){
                        document.getElementById('direccion').style.border = '1px solid red';
                        mensajeValidacion = '<%=meLanbide32I18n.getMensaje(idiomaUsuario, "ori.msg.direccionCaracteresEspeciales")%>';
                        return false;
                    }else{
                        document.getElementById('direccion').removeAttribute("style");
                    }
                }
                
                var horas = document.getElementById('horasSolic').value;
                if(horas == null || horas == ''){
                    mensajeValidacion = '<%=meLanbide32I18n.getMensaje(idiomaUsuario, "ori.msg.todosCamposOblig")%>';
                    return false;
                }
                
                var correcto = true;
                
                var numDespachos = document.getElementById('numDespachos').value;
                if(numDespachos == null || numDespachos == ''){
                    document.getElementById('numDespachos').style.border = '1px solid red';
                    if(mensajeValidacion == '')
                        mensajeValidacion = '<%=meLanbide32I18n.getMensaje(idiomaUsuario, "ori.msg.numHorasIncorrecto")%>';
                    correcto = false;
                }
                
                if(!validarNumerico(document.getElementById('horasSolic'))){
                    document.getElementById('horasSolic').style.border = '1px solid red';
                    if(mensajeValidacion == '')
                        mensajeValidacion = '<%=meLanbide32I18n.getMensaje(idiomaUsuario, "ori.msg.numHorasIncorrecto")%>';
                    correcto = false;
                }
                else{
                    try{
                        var horas = parseInt(document.getElementById('horasSolic').value);
                        if(horas < 0){
                            document.getElementById('horasSolic').style.border = '1px solid red';
                            if(mensajeValidacion == '')
                                mensajeValidacion = '<%=meLanbide32I18n.getMensaje(idiomaUsuario, "ori.msg.numHorasIncorrecto")%>';
                            correcto = false;
                        }else{
                            document.getElementById('horasSolic').removeAttribute("style");
                        }
                    }
                    catch(err){
                        document.getElementById('horasSolic').style.border = '1px solid red';
                        if(mensajeValidacion == '')
                            mensajeValidacion = '<%=meLanbide32I18n.getMensaje(idiomaUsuario, "ori.msg.numHorasIncorrecto")%>';
                        correcto = false;
                    }
                }
                
                if(!validarNumerico(document.getElementById('numDespachos'))){
                    document.getElementById('numDespachos').style.border = '1px solid red';
                    if(mensajeValidacion == '')
                        mensajeValidacion = '<%=meLanbide32I18n.getMensaje(idiomaUsuario, "ori.msg.numDespachosIncorrecto")%>';
                    correcto = false;
                }
                else{
                    try{
                        var desp = parseInt(document.getElementById('numDespachos').value);
                        if(desp < 0){
                            document.getElementById('numDespachos').style.border = '1px solid red';
                            if(mensajeValidacion == '')
                                mensajeValidacion = '<%=meLanbide32I18n.getMensaje(idiomaUsuario, "ori.msg.numDespachosIncorrecto")%>';
                            correcto = false;
                        }else{
                            document.getElementById('numDespachos').removeAttribute("style");
                        }
                    }
                    catch(err){
                        document.getElementById('numDespachos').style.border = '1px solid red';
                        if(mensajeValidacion == '')
                            mensajeValidacion = '<%=meLanbide32I18n.getMensaje(idiomaUsuario, "ori.msg.numDespachosIncorrecto")%>';
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
                var resultado = jsp_alerta('','<%=meLanbide32I18n.getMensaje(idiomaUsuario, "ori.msg.preguntaCancelar")%>');
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
            
            function comprobarCaracteresEspeciales(texto){
                //var iChars = "!@#$%^&*()+=-[]\\';,./{}|\":<>?";
                var iChars = "&'<>|^/\\%";
                for (var i = 0; i < texto.length; i++) {
                    if (iChars.indexOf(texto.charAt(i)) != -1) {
                        return false;
                    }
                }
                return true;
            }
        </script>
    </head>
    <body onload="inicio();" class="contenidoPantalla">
        <form>
            <div style="width: 100%; padding: 10px; text-align: left;">
                <div class="tituloAzul" style="clear: both; text-align: left;">
                    <span>
                        <%=meLanbide32I18n.getMensaje(idiomaUsuario,"ori.label.datosUbicacion")%>
                    </span>
                </div>
                <div class="lineaFormulario">
                    <div style="width: 170px; float: left;">
                        <%=meLanbide32I18n.getMensaje(idiomaUsuario,"ori.label.territorioHistorico")%>
                    </div>
                    <div style="width: 450px; float: left;">
                        <div style="float: left;">
                            <input id="codProvincia" name="codProvincia" type="text" class="inputTexto" size="2" maxlength="2" 
                                   onkeypress="javascript:return SoloDigitosConsulta(event);" >
                            <input id="descProvincia" name="descProvincia" type="text" class="inputTexto" size="15" readonly>
                            <a id="anchorProvincia" name="anchorProvincia" href=""><img src="<%=request.getContextPath()%>/images/listas/botondesplegable.gif" id="botonProvincia" name="botonProvincia" border="0" width="14" height="14" style="cursor:hand;" alt="<%=meLanbide32I18n.getMensaje(idiomaUsuario, "altDesplegable")%>" title="<%=meLanbide32I18n.getMensaje(idiomaUsuario, "altDesplegable")%>"></a>
                        </div>
                    </div>
                </div>
                <div class="lineaFormulario">
                    <div style="width: 170px; float: left;">
                        <%=meLanbide32I18n.getMensaje(idiomaUsuario,"ori.label.ambito")%>
                    </div>
                    <div style="width: 247px; float: left;">
                        <div style="float: left;">
                            <input id="codAmbito" name="codAmbito" type="text" class="inputTexto" size="2" maxlength="2" 
                                   onkeypress="javascript:return SoloDigitosConsulta(event);"  >
                            <input id="descAmbito" name="descAmbito" type="text" class="inputTexto" size="20" readonly>
                            <a id="anchorAmbito" name="anchorAmbito" href=""><img src="<%=request.getContextPath()%>/images/listas/botondesplegable.gif" id="botonAmbito" name="botonAmbito" border="0" width="14" height="14" style="cursor:hand;" alt="<%=meLanbide32I18n.getMensaje(idiomaUsuario, "altDesplegable")%>" title="<%=meLanbide32I18n.getMensaje(idiomaUsuario, "altDesplegable")%>"></a>
                        </div>
                    </div>

                    <div style="width: 170px; float: left;">
                        <%=meLanbide32I18n.getMensaje(idiomaUsuario,"ori.label.municipioCentroEmpleo")%>
                    </div>
                    <div style="width: 200px; float: left;">
                        <div style="float: left;">
                            <input id="codMunCE" name="codMunCE" type="text" class="inputTexto" size="2" maxlength="2" 
                                   onkeypress="javascript:return SoloDigitosConsulta(event);" >
                            <input id="descMunCE" name="descMunCE" type="text" class="inputTexto" size="20" readonly>
                            <a id="anchorMunCE" name="anchorMunCE" href=""><img src="<%=request.getContextPath()%>/images/listas/botondesplegable.gif" id="botonMunCE" name="botonMunCE" border="0" width="14" height="14" style="cursor:hand;" alt="<%=meLanbide32I18n.getMensaje(idiomaUsuario, "altDesplegable")%>" title="<%=meLanbide32I18n.getMensaje(idiomaUsuario, "altDesplegable")%>"></a>
                        </div>
                    </div>
                </div>
                <div class="lineaFormulario">
                    <div style="width: 170px; float: left;">
                        <%=meLanbide32I18n.getMensaje(idiomaUsuario,"ori.label.direccion")%>
                    </div>
                    <div style="width: 450px; float: left;">
                        <input id="direccion" name="direccion" type="text" class="inputTexto" size="100" maxlength="100" 
                           value="<%=ubicModif != null && ubicModif.getOriOrientDireccion() != null ? ubicModif.getOriOrientDireccion().toUpperCase() : ""%>"/>
                    </div>
                </div>
                <div class="lineaFormulario">
                    <div style="width: 170px; float: left;">
                        <%=meLanbide32I18n.getMensaje(idiomaUsuario,"ori.label.horasSolicitadas")%>
                    </div>
                    <div style="width: 450px; float: left;">
                        <input id="horasSolic" name="horasSolic" type="text" class="inputTexto" size="5" maxlength="5" 
                           value="<%=ubicModif != null && ubicModif.getOriOrientHorasSolicitadas() != null ? ubicModif.getOriOrientHorasSolicitadas().toString() : ""%>"/>
                    </div>
                </div>
                <div class="lineaFormulario">
                    <div style="width: 170px; float: left;">
                        <%=meLanbide32I18n.getMensaje(idiomaUsuario,"ori.label.numeroDespachos")%>
                    </div>
                    <div style="width: 450px; float: left;">
                        <input id="numDespachos" name="numDespachos" type="text" class="inputTexto" size="2" maxlength="2" 
                           value="<%=ubicModif != null && ubicModif.getOriOrientDespachos() != null ? String.valueOf(ubicModif.getOriOrientDespachos()) : ""%>"/>
                    </div>
                </div>
                <div class="lineaFormulario">
                    <div style="width: 170px; float: left;">
                        <%=meLanbide32I18n.getMensaje(idiomaUsuario,"ori.label.segundaAula")%>
                    </div>
                    <div style="width: 450px; float: left;">
                        <input type="checkBox" id="checkSegundaAula" name="checkSegundaAula" value="<%=ubicModif != null && ubicModif.getOriOrientAulagrupal() != null && ubicModif.getOriOrientAulagrupal().equals("S") ? "S" : "N"%>" <%=ubicModif != null && ubicModif.getOriOrientAulagrupal() != null && ubicModif.getOriOrientAulagrupal().equals("S") ? "checked" : ""%> onchange="cambioValorCheck(this);"></input>
                    </div>
                </div>
                <div class="botonera">
                    <input type="button" id="btnAceptar" name="btnAceptar" class="botonGeneral" value="<%=meLanbide32I18n.getMensaje(idiomaUsuario, "ori.btn.aceptar")%>" onclick="guardarDatos();">
                    <input type="button" id="btnCancelar" name="btnCancelar" class="botonGeneral" value="<%=meLanbide32I18n.getMensaje(idiomaUsuario, "ori.btn.cancelar")%>" onclick="cancelar();">
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
        </script>
    </body>
</html>
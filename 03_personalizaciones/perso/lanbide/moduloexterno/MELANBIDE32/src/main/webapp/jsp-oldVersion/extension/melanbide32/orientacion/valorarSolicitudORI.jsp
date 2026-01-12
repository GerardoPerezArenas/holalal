<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide32.i18n.MeLanbide32I18n" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide32.vo.orientacion.OriUbicValoracionVO" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<html>
    <head>
        <%
            OriUbicValoracionVO ubicModif = new OriUbicValoracionVO();
            String codOrganizacion = "";
            String numExpediente = "";
            String nuevo = "1";
            MeLanbide32I18n meLanbide32I18n = MeLanbide32I18n.getInstance();
            int idiomaUsuario = 1;
            OriUbicValoracionVO ubicVal = new OriUbicValoracionVO();
        
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
            
                if(request.getAttribute("ubicVal") != null)
                {
                    ubicModif = (OriUbicValoracionVO)request.getAttribute("ubicVal");
                }
            }
            catch(Exception ex)
            {
                
            }
        %>
        
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/estilo.css"/>
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/extension/melanbide32/melanbide32.css"/>
        
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/general.js"></script>
        <script type="text/javascript">
            var APP_CONTEXT_PATH = '<%=request.getContextPath()%>';
        </script>
        
        <script type="text/javascript">
            
            
            var mensajeValidacion = '';
            
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
            
            
            function carcularValoracion(){
                var total = 0;
                
                //Trayectoria
                try{
                    var trayectoriaVal = parseInt(document.getElementById('trayectoriaValid').value);
                    if(!isNaN(trayectoriaVal)){
                        trayectoriaVal = trayectoriaVal - 2;
                        if(trayectoriaVal < 0)
                            trayectoriaVal = 0;
                        document.getElementById('trayectoriaValor').value = trayectoriaVal;
                        total += trayectoriaVal;
                    }
                }
                catch(err){
                    
                }
                
                //Ubicacion
                try{
                    var ubicacion = parseFloat(document.getElementById('ubicacionValid').value);
                    if(!isNaN(ubicacion)){
                        document.getElementById('ubicacionValor').value = ubicacion;
                        total += ubicacion;
                    }
                }
                catch(err){
                    alert(err);
                }
                
                //Numero despachos
                try{
                    var numDespachos = parseInt(document.getElementById('numDespachosValid').value);
                    if(!isNaN(numDespachos)){
                        numDespachos = (numDespachos - 1) * 2;
                        if(numDespachos < 0)
                            numDespachos = 0;
                        if(numDespachos > 4)
                            numDespachos = 4;
                        document.getElementById('despachosValor').value = numDespachos;
                        total += numDespachos;
                    }
                }
                catch(err){
                    
                }
            
                //Aula grupal
                try{
                    var aulaGrupal = document.getElementById('aulaGrupalValid').value;
                    if(aulaGrupal.toUpperCase() == 'S')
                    {
                        document.getElementById('aulasValor').value = 1;
                        total += 1;
                    }
                    else
                    {
                        document.getElementById('aulasValor').value = 0;
                    }
                }
                catch(err){
                    
                }
                
                document.getElementById('totalValoracion').value = total;
            }
            
            function rellenarVacios(){
                if(document.getElementById('numDespachosValid').value == ''){
                    document.getElementById('numDespachosValid').value = '0';
                }
                if(document.getElementById('aulaGrupalValid').value == ''){
                    document.getElementById('aulaGrupalValid').value = 'N';
                }
            }
            
            function guardarDatos(){
                if(validarDatos()){
                    
                    var ajax = getXMLHttpRequest();
                    var nodos = null;
                    var CONTEXT_PATH = '<%=request.getContextPath()%>'
                    var url = CONTEXT_PATH + "/PeticionModuloIntegracion.do";
                    var parametros = "";
                    parametros = "tarea=preparar&modulo=MELANBIDE32&operacion=valorarSolicitudORI&tipo=0&numero=<%=numExpediente%>"
                            +"&idUbic=<%=ubicModif != null && ubicModif.getOriOrientUbicCod() != null ? ubicModif.getOriOrientUbicCod().toString() : ""%>"
                            +"&despachos="+document.getElementById('numDespachosValid').value
                            +"&aulaGrupal="+document.getElementById('aulaGrupalValid').value
                            +"&trayVal="+document.getElementById('trayectoriaValor').value
                            +"&ubicVal="+document.getElementById('ubicacionValor').value
                            +"&despVal="+document.getElementById('despachosValor').value
                            +"&aulaVal="+document.getElementById('aulasValor').value
                            +"&puntuacion="+document.getElementById('totalValoracion').value
                            +"&observaciones="+escape(document.getElementById('observaciones').value);
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
            
            function validarDatos(){
                mensajeValidacion = "";
                
                
                
                var observaciones = document.getElementById('observaciones').value;
                if(!comprobarCaracteresEspeciales(observaciones)){
                    document.getElementById('observaciones').style.border = '1px solid red';
                    mensajeValidacion = '<%=meLanbide32I18n.getMensaje(idiomaUsuario, "ori.msg.observacionesCaracteresEspeciales")%>';
                    return false;
                }else{
                    document.getElementById('observaciones').removeAttribute("style");
                }
                
                var correcto = true;
                
                if(!validarNumerico(document.getElementById('numDespachosValid'))){
                    document.getElementById('numDespachosValid').style.border = '1px solid red';
                    if(mensajeValidacion == '')
                        mensajeValidacion = '<%=meLanbide32I18n.getMensaje(idiomaUsuario, "ori.msg.numDespachosValidIncorrecto")%>';
                    correcto = false;
                }
                else{
                    try{
                        var despachos = parseInt(document.getElementById('numDespachosValid').value);
                        if(despachos < 0){
                            document.getElementById('numDespachosValid').style.border = '1px solid red';
                            if(mensajeValidacion == '')
                                mensajeValidacion = '<%=meLanbide32I18n.getMensaje(idiomaUsuario, "ori.msg.numDespachosValidNegativo")%>';
                            correcto = false;
                        }else{
                            document.getElementById('numDespachosValid').removeAttribute("style");
                        }
                    }
                    catch(err){
                        document.getElementById('numDespachosValid').style.border = '1px solid red';
                        if(mensajeValidacion == '')
                            mensajeValidacion = '<%=meLanbide32I18n.getMensaje(idiomaUsuario, "ori.msg.numDespachosValidIncorrecto")%>';
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
            
            function inicio(){
                var traySol = '<%=ubicModif != null && ubicModif.getOriEntTrayectoria() != null ? ubicModif.getOriEntTrayectoria() : "0"%>';
                document.getElementById('trayectoriaSol').value = traySol;
                
                var trayVal = '<%=ubicModif != null && ubicModif.getOriEntTrayectoriaVal() != null ? ubicModif.getOriEntTrayectoriaVal() : "0"%>';
                document.getElementById('trayectoriaValid').value = trayVal;
                
                var ubicSol = '<%=ubicModif != null && ubicModif.getOriUbicPuntuacion() != null ? ubicModif.getOriUbicPuntuacion() : "0"%>';
                document.getElementById('ubicacionSol').value = ubicSol;
                
                var ubicVal = '<%=ubicModif != null && ubicModif.getOriUbicPuntuacionVal() != null ? ubicModif.getOriUbicPuntuacionVal() : "0"%>';
                document.getElementById('ubicacionValid').value = ubicVal;
                
                var despachosSol = '<%=ubicModif != null && ubicModif.getOriOrientDespachos() != null ? ubicModif.getOriOrientDespachos() : "0"%>';
                document.getElementById('numDespachosSol').value = despachosSol;
                
                var despachosValid = '<%=ubicModif != null && ubicModif.getOriOrientDespachosValidados() != null ? ubicModif.getOriOrientDespachosValidados() : "0"%>';
                document.getElementById('numDespachosValid').value = despachosValid;
                
                var aulaSol = '<%=ubicModif != null && ubicModif.getOriOrientAulagrupal() != null ? ubicModif.getOriOrientAulagrupal() : "0"%>';
                document.getElementById('aulaGrupalSol').value = aulaSol;
                
                var aulaVal = '<%=ubicModif != null && ubicModif.getOriOrientAulaGrupalValidada() != null && !ubicModif.getOriOrientAulaGrupalValidada().equals("") ? ubicModif.getOriOrientAulaGrupalValidada() : "N"%>';
                document.getElementById('aulaGrupalValid').value = aulaVal;
                
                rellenarVacios();
                carcularValoracion();
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
                    <div style="width: 255px; float: left;">
                        <div style="float: left;">
                            <%=ubicModif != null && ubicModif.getDescProvincia() != null ? ubicModif.getDescProvincia() : ""%>
                        </div>
                    </div>
                </div>
                <div class="lineaFormulario">
                    <div style="width: 170px; float: left;">
                        <%=meLanbide32I18n.getMensaje(idiomaUsuario,"ori.label.ambito")%>
                    </div>
                    <div style="width: 255px; float: left;">
                        <div style="float: left;">
                            <%=ubicModif != null && ubicModif.getDescAmbito() != null ? ubicModif.getDescAmbito() : ""%>
                        </div>
                    </div>
                </div>
                <div class="lineaFormulario">
                    <div style="width: 170px; float: left;">
                        <%=meLanbide32I18n.getMensaje(idiomaUsuario,"ori.label.municipio")%>
                    </div>
                    <div style="width: 255px; float: left;">
                        <div style="float: left;">
                            <%=ubicModif != null && ubicModif.getDescMunicipio() != null ? ubicModif.getDescMunicipio() : ""%>
                        </div>
                    </div>
                </div>
                <div class="lineaFormulario">
                    <div style="width: 170px; float: left;">
                        <%=meLanbide32I18n.getMensaje(idiomaUsuario,"ori.label.direccion")%>
                    </div>
                    <div style="width: 255px; float: left;">
                        <div style="float: left;">
                            <%=ubicModif != null && ubicModif.getDireccion() != null ? ubicModif.getDireccion() : ""%>
                        </div>
                    </div>
                </div>
                <div class="lineaFormulario">
                    <div style="width: 170px; float: left;">
                        <%=meLanbide32I18n.getMensaje(idiomaUsuario,"ori.label.horasSolicitadas")%>
                    </div>
                    <div style="width: 255px; float: left;">
                        <div style="float: left;">
                            <%=ubicModif != null && ubicModif.getHorasSolic() != null ? ubicModif.getHorasSolic() : ""%>
                        </div>
                    </div>
                </div>
                <div class="lineaFormulario">
                    <div style="width: 353px; float: left; text-align: right;">
                            <%=meLanbide32I18n.getMensaje(idiomaUsuario, "ori.label.solicitud")%>
                    </div>
                    <div style="width: 70px; float: left; text-align: right;">
                            <%=meLanbide32I18n.getMensaje(idiomaUsuario, "ori.label.validado")%>
                    </div>
                </div>
                <div class="lineaFormulario">
                    <div style="width: 300px; float: left;">
                        <%=meLanbide32I18n.getMensaje(idiomaUsuario, "ori.label.trayectoria")%>
                    </div>
                    <div style="width: 70px; float: left;">
                        <div style="float: left;">
                            <input id="trayectoriaSol" name="trayectoriaSol" type="text" class="inputTexto" size="5" maxlength="5" 
                            value="" disabled="disabled"/>
                        </div>
                    </div>
                    <div style="width: 70px; float: left;">
                        <div style="float: left;">
                            <input id="trayectoriaValid" name="trayectoriaValid" type="text" class="inputTexto" size="5" maxlength="5" 
                            value="" disabled="disabled"/>
                        </div>
                    </div>
                </div>
                <div class="lineaFormulario">
                    <div style="width: 300px; float: left;">
                        <%=meLanbide32I18n.getMensaje(idiomaUsuario, "ori.label.ubicacion")%>
                    </div>
                    <div style="width: 70px; float: left;">
                        <div style="float: left;">
                            <input id="ubicacionSol" name="ubicacionSol" type="text" class="inputTexto" size="5" maxlength="5" 
                            value="" disabled="disabled"/>
                        </div>
                    </div>
                    <div style="width: 70px; float: left;">
                        <div style="float: left;">
                            <input id="ubicacionValid" name="ubicacionValid" type="text" class="inputTexto" size="5" maxlength="5" 
                            value="" disabled="disabled"/>
                        </div>
                    </div>
                </div>
                <div class="lineaFormulario">
                    <div style="width: 300px; float: left;">
                        <%=meLanbide32I18n.getMensaje(idiomaUsuario, "ori.label.numDespachos")%>
                    </div>
                    <div style="width: 70px; float: left;">
                        <div style="float: left;">
                            <input id="numDespachosSol" name="numDespachosSol" type="text" class="inputTexto" size="5" maxlength="5" 
                            value="" disabled="disabled"/>
                        </div>
                    </div>
                    <div style="width: 70px; float: left;">
                        <div style="float: left;">
                            <input id="numDespachosValid" name="numDespachosValid" type="text" class="inputTexto" size="5" maxlength="5" 
                                   value="" onkeyup="carcularValoracion();" onchange="rellenarVacios();"/>
                        </div>
                    </div>
                </div>
                <div class="lineaFormulario">
                    <div style="width: 300px; float: left;">
                        <%=meLanbide32I18n.getMensaje(idiomaUsuario, "ori.label.dispAulaGrupal")%>
                    </div>
                    <div style="width: 70px; float: left;">
                        <div style="float: left;">
                            <input id="aulaGrupalSol" name="aulaGrupalSol" type="text" class="inputTexto" size="5" maxlength="5" 
                            value="" disabled="disabled"/>
                        </div>
                    </div>
                    <div style="width: 70px; float: left;">
                        <div style="float: left;">
                            <input id="aulaGrupalValid" name="aulaGrupalValid" type="text" class="inputTexto" size="5" maxlength="5" 
                                   value="" onkeyup="carcularValoracion();" onchange="rellenarVacios();"/>
                        </div>
                    </div>
                </div>
                <div class="tituloAzul" style="clear: both; text-align: left;">
                    <span>
                        <%=meLanbide32I18n.getMensaje(idiomaUsuario,"ori.label.valoracion")%>
                    </span>
                </div>
                <div>
                    <div style="float: left;">
                        <div class="lineaFormulario">
                            <div style="width: 150px; float: left;">
                                <%=meLanbide32I18n.getMensaje(idiomaUsuario, "ori.label.trayectoria")%>
                            </div>
                            <div style="width: 70px; float: left;">
                                <div style="float: left;">
                                    <input id="trayectoriaValor" name="trayectoriaValor" type="text" class="inputTexto" size="5" maxlength="5" 
                                    value="" disabled="disabled"/>
                                </div>
                            </div>
                        </div>
                        <div class="lineaFormulario">
                            <div style="width: 150px; float: left;">
                                <%=meLanbide32I18n.getMensaje(idiomaUsuario, "ori.label.ubicacion")%>
                            </div>
                            <div style="width: 70px; float: left;">
                                <div style="float: left;">
                                    <input id="ubicacionValor" name="ubicacionValor" type="text" class="inputTexto" size="5" maxlength="5" 
                                    value="" disabled="disabled"/>
                                </div>
                            </div>
                        </div>
                        <div class="lineaFormulario">
                            <div style="width: 150px; float: left;">
                                <%=meLanbide32I18n.getMensaje(idiomaUsuario, "ori.label.despachosExtra")%>
                            </div>
                            <div style="width: 70px; float: left;">
                                <div style="float: left;">
                                    <input id="despachosValor" name="despachosValor" type="text" class="inputTexto" size="5" maxlength="5" 
                                    value="" disabled="disabled"/>
                                </div>
                            </div>
                        </div>
                        <div class="lineaFormulario">
                            <div style="width: 150px; float: left;">
                                <%=meLanbide32I18n.getMensaje(idiomaUsuario, "ori.label.aulasExtra")%>
                            </div>
                            <div style="width: 70px; float: left;">
                                <div style="float: left;">
                                    <input id="aulasValor" name="aulasValor" type="text" class="inputTexto" size="5" maxlength="5" 
                                    value="" disabled="disabled"/>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div style="height: 58px; padding-top: 46px; float: left;">
                        <div style="width: 150px; float: left;">
                            <%=meLanbide32I18n.getMensaje(idiomaUsuario, "ori.label.total")%>
                        </div>
                        <div style="width: 70px; float: left;">
                            <input id="totalValoracion" name="totalValoracion" type="text" class="inputTexto" size="5" maxlength="5" 
                            value="" disabled="disabled"/>

                        </div>
                    </div>
                </div>
                <div class="tituloAzul" style="clear: both; text-align: left;">
                    <span>
                        <%=meLanbide32I18n.getMensaje(idiomaUsuario,"ori.label.observaciones")%>
                    </span>
                </div>
                <div>
                    <textarea rows="4" cols="50" id="observaciones" name="observaciones" maxlength="500"><%=ubicModif != null && ubicModif.getOriOrientObservaciones() != null && !ubicModif.getOriOrientObservaciones().equals("") ? ubicModif.getOriOrientObservaciones() : ""%></textarea>
                </div>
                <div class="botonera">
                    <input type="button" id="btnAceptar" name="btnAceptar" class="botonGeneral" value="<%=meLanbide32I18n.getMensaje(idiomaUsuario, "ori.btn.aceptar")%>" onclick="guardarDatos();">
                    <input type="button" id="btnCancelar" name="btnCancelar" class="botonGeneral" value="<%=meLanbide32I18n.getMensaje(idiomaUsuario, "ori.btn.cancelar")%>" onclick="cancelar();">
                </div>
            </div>
        </form>
    </body>
</html>
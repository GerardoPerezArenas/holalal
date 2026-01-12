<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide47.i18n.MeLanbide47I18n" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide47.util.ConstantesMeLanbide47" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide47.vo.orientacion.OriUbicValoracionVO" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<html>
    <head>
        <%
            OriUbicValoracionVO ubicModif = new OriUbicValoracionVO();
            String codOrganizacion = "";
            String numExpediente = "";
            String nuevo = "1";
            String codEntidad = "";
            MeLanbide47I18n meLanbide47I18n = MeLanbide47I18n.getInstance();
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
                
                codEntidad = request.getParameter("codEntidad");
            
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
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/extension/melanbide47/melanbide47.css"/>
        
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/general.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/melanbide47/moment-with-locales.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/melanbide47/ori14Utils.js"></script>
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
            
            
            function calcularValoracion(){
                var total = 0;
                
                //Trayectoria
                try{
                    var trayectoriaVal = parseInt(document.getElementById('trayectoriaValid').value);
                    if(!isNaN(trayectoriaVal)){
                        //trayectoriaVal = (trayectoriaVal - 2) * 2;
                        if(trayectoriaVal < 0){
                            trayectoriaVal = 0;
                        }else if(trayectoriaVal > 10){
                            trayectoriaVal = 10;
                        }
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
                        if(ubicacion < 0){
                            ubicacion = 0;
                        }else if(ubicacion > 10){
                            ubicacion = 10;
                        }
                        document.getElementById('ubicacionValor').value = ubicacion;
                        total += ubicacion;
                    }
                }
                catch(err){
                    
                }
                
                //Numero despachos
                try{
                    if(document.getElementById('numDespachosValid').checked){
                        document.getElementById('despachosValor').value = 4;
                        total += 4;
                    }else{
                        document.getElementById('despachosValor').value = 0;
                    }
                }
                catch(err){
                    
                }
            
                //Aula grupal
                try{
                    if(document.getElementById('aulaGrupalValid').checked)
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
                    document.getElementById('numDespachosValid').value = 'N';
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
                    parametros = "tarea=preparar&modulo=MELANBIDE47&operacion=valorarAmbitoORI&tipo=0&numero=<%=numExpediente%>"
                            +"&codEntidad=<%=codEntidad != null ? codEntidad : ""%>"
                            +"&idUbic=<%=ubicModif != null && ubicModif.getOriOrientUbicCod() != null ? ubicModif.getOriOrientUbicCod().toString() : ""%>"
                            +"&despachos="+(document.getElementById('numDespachosValid').checked ? '<%=ConstantesMeLanbide47.SI%>' : '<%=ConstantesMeLanbide47.NO%>')
                            +"&aulaGrupal="+(document.getElementById('aulaGrupalValid').checked ? '<%=ConstantesMeLanbide47.SI%>' : '<%=ConstantesMeLanbide47.NO%>')
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
                                }
                                listaUbicaciones[j] = fila;
                                fila = new Array();
                            }   
                        }//for(j=0;hijos!=null && j<hijos.length;j++)
                        if(codigoOperacion=="0"){
                            //jsp_alerta("A",'Correcto');
                            window.returnValue =  listaUbicaciones;
                            jsp_alerta("A",'<%=meLanbide47I18n.getMensaje(idiomaUsuario,"msg.datosGuardadosOK")%>');
                            cerrarVentana();
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
                }else{
                    jsp_alerta("A", mensajeValidacion);
                }
            }
            
            function validarDatos(){
                mensajeValidacion = "";
                
                var correcto = true;
                
                
                
                var observaciones = document.getElementById('observaciones').value;
                if(!comprobarCaracteresEspecialesOri14(observaciones)){
                    document.getElementById('observaciones').style.border = '1px solid red';
                    mensajeValidacion = '<%=meLanbide47I18n.getMensaje(idiomaUsuario, "msg.ambitos.valorarAmbito.observacionesCaracteresEspeciales")%>';
                    return false;
                }else{
                    document.getElementById('observaciones').removeAttribute("style");
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
            
            function inicio(){
                var traySol = '<%=ubicModif != null && ubicModif.getOriEntTrayectoria() != null ? ubicModif.getOriEntTrayectoria() : "0"%>';
                document.getElementById('trayectoriaSol').value = traySol;
                
                var trayVal = '<%=ubicModif != null && ubicModif.getOriEntTrayectoriaVal() != null ? ubicModif.getOriEntTrayectoriaVal() : "0"%>';
                document.getElementById('trayectoriaValid').value = trayVal;
                
                var ubicSol = '<%=ubicModif != null && ubicModif.getOriUbicPuntuacion() != null ? ubicModif.getOriUbicPuntuacion() : "0"%>';
                document.getElementById('ubicacionSol').value = ubicSol;
                
                var ubicVal = '<%=ubicModif != null && ubicModif.getOriUbicPuntuacionVal() != null ? ubicModif.getOriUbicPuntuacionVal() : "0"%>';
                document.getElementById('ubicacionValid').value = ubicVal;
                
                <%
                    if(ubicModif != null && ubicModif.getOriOrientDespachos() != null && ubicModif.getOriOrientDespachos().equalsIgnoreCase(ConstantesMeLanbide47.SI))
                    {
                %>
                        document.getElementById('numDespachosSol').checked = true;
                <%
                    }
                %>
                        
                <%
                    if(ubicModif != null && ubicModif.getOriOrientDespachosValidados() != null && ubicModif.getOriOrientDespachosValidados().equalsIgnoreCase(ConstantesMeLanbide47.SI))
                    {
                %>
                        document.getElementById('numDespachosValid').checked = true;
                <%
                    }
                %>
                
                
                
                <%
                    if(ubicModif != null && ubicModif.getOriOrientAulagrupal() != null && ubicModif.getOriOrientAulagrupal().equalsIgnoreCase(ConstantesMeLanbide47.SI))
                    {
                %>
                        document.getElementById('aulaGrupalSol').checked = true;
                <%
                    }
                %>
                        
                <%
                    if(ubicModif != null && ubicModif.getOriOrientAulaGrupalValidada() != null && ubicModif.getOriOrientAulaGrupalValidada().equalsIgnoreCase(ConstantesMeLanbide47.SI))
                    {
                %>
                        document.getElementById('aulaGrupalValid').checked = true;
                <%
                    }
                %>
                
                rellenarVacios();
                calcularValoracion();
            }
        </script>
    </head>
    <body onload="inicio();" class="contenidoPantalla">
        <form>
            <div style="width: 100%; padding: 10px; text-align: left;">
                <div class="tituloAzul" style="clear: both; text-align: left;">
                    <span>
                        <%=meLanbide47I18n.getMensaje(idiomaUsuario,"label.ambitos.valorarAmbito.datosUbicacion")%>
                    </span>
                </div>
                <div class="lineaFormulario">
                    <div style="width: 170px; float: left;">
                        <%=meLanbide47I18n.getMensaje(idiomaUsuario,"label.ambitos.valorarAmbito.territorioHistorico")%>
                    </div>
                    <div style="width: 255px; float: left;">
                        <div style="float: left;">
                            <%=ubicModif != null && ubicModif.getDescProvincia() != null ? ubicModif.getDescProvincia() : ""%>
                        </div>
                    </div>
                </div>
                <div class="lineaFormulario">
                    <div style="width: 170px; float: left;">
                        <%=meLanbide47I18n.getMensaje(idiomaUsuario,"label.ambitos.valorarAmbito.ambito")%>
                    </div>
                    <div style="width: 255px; float: left;">
                        <div style="float: left;">
                            <%=ubicModif != null && ubicModif.getDescAmbito() != null ? ubicModif.getDescAmbito() : ""%>
                        </div>
                    </div>
                </div>
                <div class="lineaFormulario">
                    <div style="width: 170px; float: left;">
                        <%=meLanbide47I18n.getMensaje(idiomaUsuario,"label.ambitos.valorarAmbito.municipio")%>
                    </div>
                    <div style="width: 255px; float: left;">
                        <div style="float: left;">
                            <%=ubicModif != null && ubicModif.getDescMunicipio() != null ? ubicModif.getDescMunicipio() : ""%>
                        </div>
                    </div>
                </div>
                <div class="lineaFormulario">
                    <div style="width: 170px; float: left;">
                        <%=meLanbide47I18n.getMensaje(idiomaUsuario,"label.ambitos.valorarAmbito.direccion")%>
                    </div>
                    <div style="width: 255px; float: left;">
                        <div style="float: left;">
                            <%=ubicModif != null && ubicModif.getDireccion() != null ? ubicModif.getDireccion() : ""%>
                        </div>
                    </div>
                </div>
                <div class="lineaFormulario">
                    <div style="width: 170px; float: left;">
                        <%=meLanbide47I18n.getMensaje(idiomaUsuario,"label.ambitos.valorarAmbito.codPostal")%>
                    </div>
                    <div style="width: 255px; float: left;">
                        <div style="float: left;">
                            <%=ubicModif != null && ubicModif.getCodPostal() != null ? ubicModif.getCodPostal() : ""%>
                        </div>
                    </div>
                </div>
                <div class="lineaFormulario">
                    <div style="width: 170px; float: left;">
                        <%=meLanbide47I18n.getMensaje(idiomaUsuario,"label.ambitos.valorarAmbito.bloquesSolicitados")%>
                    </div>
                    <div style="width: 255px; float: left;">
                        <div style="float: left;">
                            <%=ubicModif != null && ubicModif.getHorasSolic() != null ? ubicModif.getHorasSolic() : ""%>
                        </div>
                    </div>
                </div>
                <div class="lineaFormulario">
                    <div style="width: 353px; float: left; text-align: right;">
                            <%=meLanbide47I18n.getMensaje(idiomaUsuario, "label.ambitos.valorarAmbito.solicitud")%>
                    </div>
                    <div style="width: 70px; float: left; text-align: right;">
                            <%=meLanbide47I18n.getMensaje(idiomaUsuario, "label.ambitos.valorarAmbito.validado")%>
                    </div>
                </div>
                <div class="lineaFormulario">
                    <div style="width: 300px; float: left;">
                        <%=meLanbide47I18n.getMensaje(idiomaUsuario, "label.ambitos.valorarAmbito.trayectoria")%>
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
                        <%=meLanbide47I18n.getMensaje(idiomaUsuario, "label.ambitos.valorarAmbito.ubicacion")%>
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
                        <%=meLanbide47I18n.getMensaje(idiomaUsuario, "label.ambitos.valorarAmbito.dispEspComp")%>
                    </div>
                    <div style="width: 70px; float: left;">
                        <div style="float: left;">
                            <input type="checkBox" id="numDespachosSol" name="numDespachosSol"  disabled="true" readonly="true"></input>
                        </div>
                    </div>
                    <div style="width: 70px; float: left;">
                        <div style="float: left;">
                            <input type="checkBox" id="numDespachosValid" name="numDespachosValid" onclick="calcularValoracion();"></input>
                        </div>
                    </div>
                </div>
                <div class="lineaFormulario">
                    <div style="width: 300px; float: left;">
                        <%=meLanbide47I18n.getMensaje(idiomaUsuario, "label.ambitos.valorarAmbito.dispAulaGrupal")%>
                    </div>
                    <div style="width: 70px; float: left;">
                        <div style="float: left;">
                            <input type="checkBox" id="aulaGrupalSol" name="aulaGrupalSol"  disabled="true" readonly="true"></input>
                        </div>
                    </div>
                    <div style="width: 70px; float: left;">
                        <div style="float: left;">
                            <input type="checkBox" id="aulaGrupalValid" name="aulaGrupalValid" onclick="calcularValoracion();"></input>
                        </div>
                    </div>
                </div>
                <div class="tituloAzul" style="clear: both; text-align: left;">
                    <span>
                        <%=meLanbide47I18n.getMensaje(idiomaUsuario,"label.ambitos.valorarAmbito.valoracion")%>
                    </span>
                </div>
                <div>
                    <div style="float: left;">
                        <div class="lineaFormulario">
                            <div style="width: 230px; float: left;">
                                <%=meLanbide47I18n.getMensaje(idiomaUsuario, "label.ambitos.valorarAmbito.trayectoria")%>
                            </div>
                            <div style="width: 70px; float: left;">
                                <div style="float: left;">
                                    <input id="trayectoriaValor" name="trayectoriaValor" type="text" class="inputTexto" size="5" maxlength="5" 
                                    value="" disabled="disabled"/>
                                </div>
                            </div>
                        </div>
                        <div class="lineaFormulario">
                            <div style="width: 230px; float: left;">
                                <%=meLanbide47I18n.getMensaje(idiomaUsuario, "label.ambitos.valorarAmbito.ubicacion")%>
                            </div>
                            <div style="width: 70px; float: left;">
                                <div style="float: left;">
                                    <input id="ubicacionValor" name="ubicacionValor" type="text" class="inputTexto" size="5" maxlength="5" 
                                    value="" disabled="disabled"/>
                                </div>
                            </div>
                        </div>
                        <div class="lineaFormulario">
                            <div style="width: 230px; float: left;">
                                <%=meLanbide47I18n.getMensaje(idiomaUsuario, "label.ambitos.valorarAmbito.despachosExtra")%>
                            </div>
                            <div style="width: 70px; float: left;">
                                <div style="float: left;">
                                    <input id="despachosValor" name="despachosValor" type="text" class="inputTexto" size="5" maxlength="5" 
                                    value="" disabled="disabled"/>
                                </div>
                            </div>
                        </div>
                        <div class="lineaFormulario">
                            <div style="width: 230px; float: left;">
                                <%=meLanbide47I18n.getMensaje(idiomaUsuario, "label.ambitos.valorarAmbito.aulasExtra")%>
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
                        <div style="width: 230px; float: left;">
                            <%=meLanbide47I18n.getMensaje(idiomaUsuario, "label.ambitos.valorarAmbito.total")%>
                        </div>
                        <div style="width: 70px; float: left;">
                            <input id="totalValoracion" name="totalValoracion" type="text" class="inputTexto" size="5" maxlength="5" 
                            value="" disabled="disabled"/>

                        </div>
                    </div>
                </div>
                <div class="tituloAzul" style="clear: both; text-align: left;">
                    <span>
                        <%=meLanbide47I18n.getMensaje(idiomaUsuario,"ori.label.observaciones")%>
                    </span>
                </div>
                <div>
                    <textarea rows="4" cols="50" id="observaciones" name="observaciones" maxlength="500"><%=ubicModif != null && ubicModif.getOriOrientObservaciones() != null && !ubicModif.getOriOrientObservaciones().equals("") ? ubicModif.getOriOrientObservaciones() : ""%></textarea>
                </div>
                <div class="botonera">
                    <input type="button" id="btnAceptar" name="btnAceptar" class="botonGeneral" value="<%=meLanbide47I18n.getMensaje(idiomaUsuario, "btn.aceptar")%>" onclick="guardarDatos();">
                    <input type="button" id="btnCancelar" name="btnCancelar" class="botonGeneral" value="<%=meLanbide47I18n.getMensaje(idiomaUsuario, "btn.cancelar")%>" onclick="cancelar();">
                </div>
            </div>
        </form>
    </body>
</html>
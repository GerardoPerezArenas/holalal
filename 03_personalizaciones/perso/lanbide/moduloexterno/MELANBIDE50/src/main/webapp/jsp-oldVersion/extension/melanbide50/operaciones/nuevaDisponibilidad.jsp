<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide50.i18n.MeLanbide50I18n" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide50.vo.disponibilidad.DisponibilidadVO" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide50.util.ConstantesMeLanbide50" %>
<%@page import="java.util.ArrayList" %>
<%@page import="java.util.List" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>

<div>
        <%
            DisponibilidadVO datModif = new DisponibilidadVO();
            String codOrganizacion = "";
            String numExpediente = "";
            String nuevo = "1";
            MeLanbide50I18n meLanbide50I18n = MeLanbide50I18n.getInstance();
            
            int idiomaUsuario = 1;
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

                if(request.getAttribute("datModif") != null)
                {
                    datModif = (DisponibilidadVO)request.getAttribute("datModif");
                }
            }
            catch(Exception ex)
            {
            }
        %>
        
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/estilo.css"/>
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/extension/melanbide50/melanbide50.css"/>
        
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/general.js"></script>
        <script type="text/javascript" src="<c:url value='/scripts/listaComboBox.js'/>"></script>
        
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
            
            function guardarDatos(){
                
                if(validarDatos()){
                    var ajax = getXMLHttpRequest();
                    var nodos = null;
                    var CONTEXT_PATH = '<%=request.getContextPath()%>';
                    var url = CONTEXT_PATH + "/PeticionModuloIntegracion.do";
                    var parametros = "";
                    var nuevo = "<%=nuevo%>";
                    
                    if(nuevo != null && nuevo == "1"){  
                        parametros = "tarea=preparar&modulo=MELANBIDE50&operacion=modificarDisponibilidad&tipo=0&numero=<%=numExpediente%>"
                                +"&id=<%=datModif != null && datModif.getId() != null ? datModif.getId().toString() : ""%>"
                                +"&idEspSol=<%=datModif != null && datModif.getIdEspSol() != null ? datModif.getIdEspSol().toString() : ""%>"
                                +"&codcp=<%=datModif != null && datModif.getCodCp() != null ? datModif.getCodCp(): ""%>"
                                +"&propiedadce="+ escape(document.getElementById('propiedadce').value)
                                +"&situados="+ escape(document.getElementById('situados').value)
                                +"&supaulas="+ document.getElementById('supaulas').value
                                +"&suptaller="+ document.getElementById('suptaller').value
                                +"&supaulastaller="+ document.getElementById('supaulastaller').value
                                +"&supcampoprac="+ document.getElementById('supcampoprac').value;
                    }
                    else{
                        parametros = "tarea=preparar&modulo=MELANBIDE50&operacion=modificarDisponibilidad&tipo=0&numero=<%=numExpediente%>"
                                +"&id=<%=datModif != null && datModif.getId() != null ? datModif.getId().toString() : ""%>"
                                +"&idEspSol=<%=datModif != null && datModif.getIdEspSol() != null ? datModif.getIdEspSol().toString() : ""%>"
                                +"&codcp=<%=datModif != null && datModif.getCodCp() != null ? datModif.getCodCp(): ""%>"
                                +"&propiedadce="+ escape(document.getElementById('propiedadce').value)
                                +"&situados="+ escape(document.getElementById('situados').value)
                                +"&supaulas="+ document.getElementById('supaulas').value
                                +"&suptaller="+ document.getElementById('suptaller').value
                                +"&supaulastaller="+ document.getElementById('supaulastaller').value
                                +"&supcampoprac="+ document.getElementById('supcampoprac').value;
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
                        var listaNueva = new Array();
                        var fila = new Array();
                        var nodoFila;
                        var hijosFila;
                        for(j=0;hijos!=null && j<hijos.length;j++){
                            if(hijos[j].nodeName=="CODIGO_OPERACION"){                            
                                codigoOperacion = hijos[j].childNodes[0].nodeValue;
                                listaNueva[j] = codigoOperacion;
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
                                        else if(hijosFila[cont].nodeName=="ID_ESPSOL"){
                                            if(hijosFila[cont].childNodes.length > 0){
                                                fila[1] = hijosFila[cont].childNodes[0].nodeValue;
                                            }
                                            else{
                                                fila[1] = '-';
                                            }
                                        }
                                        else if(hijosFila[cont].nodeName=="DRE_NUM"){
                                            if(hijosFila[cont].childNodes.length > 0){
                                                fila[2] = hijosFila[cont].childNodes[0].nodeValue;
                                            }
                                            else{
                                                fila[2] = '-';
                                            }
                                        }
                                        else if(hijosFila[cont].nodeName=="DRE_CODCP"){
                                            if(hijosFila[cont].childNodes.length > 0){
                                                fila[3] = hijosFila[cont].childNodes[0].nodeValue;
                                            }
                                            else{
                                                fila[3] = '-';
                                            }
                                        }
                                        else if(hijosFila[cont].nodeName=="DRE_PRCE"){
                                            if(hijosFila[cont].childNodes.length > 0){
                                                fila[4] = hijosFila[cont].childNodes[0].nodeValue;
                                            }
                                            else{
                                                fila[4] = '-';
                                            }
                                        }
                                        else if(hijosFila[cont].nodeName=="DRE_SIT"){
                                            if(hijosFila[cont].childNodes.length > 0){
                                                fila[5] = hijosFila[cont].childNodes[0].nodeValue;
                                            }
                                            else{
                                                fila[5] = '-';
                                            }
                                        }
                                        else if(hijosFila[cont].nodeName=="DRE_AUL"){
                                            if(hijosFila[cont].childNodes.length > 0){
                                                fila[6] = hijosFila[cont].childNodes[0].nodeValue;
                                            }
                                            else{
                                                fila[6] = '-';
                                            }
                                        }
                                        else if(hijosFila[cont].nodeName=="DRE_TAL"){
                                            if(hijosFila[cont].childNodes.length > 0){
                                                fila[7] = hijosFila[cont].childNodes[0].nodeValue;
                                            }
                                            else{
                                                fila[7] = '-';
                                            }
                                        }
                                        else if(hijosFila[cont].nodeName=="DRE_AUTA"){
                                            if(hijosFila[cont].childNodes.length > 0){
                                                fila[8] = hijosFila[cont].childNodes[0].nodeValue;
                                            }
                                            else{
                                                fila[8] = '-';
                                            }
                                        }
                                        else if(hijosFila[cont].nodeName=="DRE_CAPR"){
                                            if(hijosFila[cont].childNodes.length > 0){
                                                fila[9] = hijosFila[cont].childNodes[0].nodeValue;
                                            }
                                            else{
                                                fila[9] = '-';
                                            }
                                        }
                                }
                                listaNueva[j] = fila;
                                fila = new Array();
                            }   
                        }//for(j=0;hijos!=null && j<hijos.length;j++)
                        if(codigoOperacion=="0"){
                            //jsp_alerta("A",'Correcto');
                            window.returnValue =  listaNueva;
                            cerrarVentana();
                        }else if(codigoOperacion=="1"){
                            jsp_alerta("A",'<%=meLanbide50I18n.getMensaje(idiomaUsuario,"error.errorBD")%>');
                        }else if(codigoOperacion=="2"){
                            jsp_alerta("A",'<%=meLanbide50I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                        }else if(codigoOperacion=="3"){
                            jsp_alerta("A",'<%=meLanbide50I18n.getMensaje(idiomaUsuario,"error.pasoParametros")%>');
                        }else if(codigoOperacion=="4"){
                            jsp_alerta("A",'<%=meLanbide50I18n.getMensaje(idiomaUsuario,"error.expSinTercero")%>');
                        }else{
                                jsp_alerta("A",'<%=meLanbide50I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                        }//if(
                    }
                    catch(Err){

                    }//try-catch
                }else{
                    jsp_alerta("A", mensajeValidacion);
                }
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
            
            function validarNumericoDecimal(numero){
                try{
                    if(Trim(numero.value) != ''){
                        return /^[0-9]{1,2}(,[0-9]{1,2})?$/.test(numero.value);
                    }
                }
                catch(err){
                    return false;
                }
            }
            
            function validarNumericoDecimalPrecision(numero, longTotal, longParteDecimal){
                try{
                    var longParteEntera = parseInt(longTotal) - parseInt(longParteDecimal);
                    if(Trim(numero.value) != ''){
                        var valor = numero.value;
                        var pattern = '^[0-9]{1,'+longParteEntera+'}(,[0-9]{1,'+longParteDecimal+'})?$';
                        var regex = new RegExp(pattern);
                        //var result = valor.match(regex);
                        var result = regex.test(valor);
                        return result;
                        //return /^[0-]{1,}(,[0-9]{1,longParteDecimal})?$/.test(numero.value);
                    }else{
                        return true;
                    }
                }
                catch(err){
                    alert(err);
                    return false;
                }
            }
            
            function cancelar(){
                var resultado = 1; //jsp_alerta('','<%=meLanbide50I18n.getMensaje(idiomaUsuario, "msg.preguntaCancelar")%>');
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
        
            function reemplazarPuntos(campo){
                try{
                    var valor = campo.value;
                    if(valor != null && valor != ''){
                        valor = valor.replace(/\./g,',');
                        campo.value = valor;
                    }
                }
                catch(err){
                }
            }
            
            function comprobarCaracteresEspeciales(texto){
                //var iChars = "!@#$%^&*()+=-[]\\';,./{}|\":<>?";
                var iChars = "&'<>|^\\%";   // / quitado para carga automatica de trayectorias 30052014
                for (var i = 0; i < texto.length; i++) {
                    if (iChars.indexOf(texto.charAt(i)) != -1) {
                        return false;
                    }
                }
                return true;
            }
            
            function validarDatos(){
                mensajeValidacion = "";
                var correcto = true;
                
                if(!compruebaTamanoCampo(document.getElementById('propiedadce'),500)){
                     mensajeValidacion = '<%=meLanbide50I18n.getMensaje(idiomaUsuario, "msg.errExcdTexto")%>';
                     return false;
                }
                if(!compruebaTamanoCampo(document.getElementById('situados'),500)){
                     mensajeValidacion = '<%=meLanbide50I18n.getMensaje(idiomaUsuario, "msg.errExcdTexto")%>';
                     return false;
                }
                
    
               return correcto;
            }
            
            function compruebaTamanoCampo(elemento, maxTex){
                var texto = elemento.value;
                if(texto.length>maxTex){
                    jsp_alerta("A", '<%=meLanbide50I18n.getMensaje(idiomaUsuario, "msg.errExcdTexto")%>');
                    elemento.focus();
                    return false;
                }
                return true;
            }
            
    </script>

<div class="contenidoPantalla">
        <form>  <!-- action="/PeticionModuloIntegracion.do" method="POST" -->
            <div style="width: 100%; padding: 10px; text-align: left;">
                <div  class="sub3titulo" style="clear: both; text-align: center; font-size: 14px;">
                    <span>
                        <%=meLanbide50I18n.getMensaje(idiomaUsuario,"label.disponibilidad.modifDisponibilidad")%>
                    </span>
                </div>
                <div class="lineaFormulario">
                    <div style="width: 190px; float: left;" class="etiqueta">
                        <%=meLanbide50I18n.getMensaje(idiomaUsuario,"label.disponibilidad.cpespecialidad")%>
                    </div>
                    <div style="width: 450px; float: left;">
                        <div style="float: left;">
                            <input id="codcp" name="codcp" type="text" class="inputTexto" readonly="true" size="15" maxlength="8" 
                            value="<%=datModif != null && datModif.getCodCp() != null ? datModif.getCodCp()  : ""%>"/>
                        </div>
                    </div>
                </div>
                <br><br>
                <div class="lineaFormulario">
                    <div style="width: 190px; float: left;" class="etiqueta">
                        <%=meLanbide50I18n.getMensaje(idiomaUsuario,"label.disponibilidad.propiedadce")%>
                    </div>
                    <div style="width: 450px; float: left;">
                        <div style="float: left;">
                            <textarea rows="2" cols="65" id="propiedadce" name="propiedadce" maxlength="500" onblur="compruebaTamanoCampo(this,500)"><%=datModif != null && datModif.getPropiedadCedidos() != null ? datModif.getPropiedadCedidos()  : ""%></textarea>
                        </div>
                    </div>
                </div>
                <br><br>
                <div class="lineaFormulario">
                    <div style="width: 190px; float: left;" class="etiqueta">
                        <%=meLanbide50I18n.getMensaje(idiomaUsuario,"label.disponibilidad.situados")%>
                    </div>
                    <div style="width: 450px; float: left;">
                        <div style="float: left;">
                            <textarea rows="2" cols="65" id="situados" name="situados" maxlength="500" onblur="compruebaTamanoCampo(this,500)"><%=datModif != null && datModif.getSituados() != null ? datModif.getSituados()  : ""%></textarea>
                        </div>
                    </div>
                </div>
                <br><br>
                <div class="lineaFormulario">
                    <div style="width: 190px; float: left;" class="etiqueta">
                        <%=meLanbide50I18n.getMensaje(idiomaUsuario,"label.disponibilidad.supaulas")%>
                    </div>
                    <div style="width: 450px; float: left;">
                        <div style="float: left;">
                            <input id="supaulas" name="supaulas" type="text" class="inputTexto" size="30" maxlength="200" 
                            value="<%=datModif != null && datModif.getSupAulas() != null ? datModif.getSupAulas().toString()  : ""%>" onchange="reemplazarPuntos(this);"/>
                        </div>
                    </div>
                </div>
                <br><br>
                <div class="lineaFormulario">
                    <div style="width: 190px; float: left;" class="etiqueta">
                        <%=meLanbide50I18n.getMensaje(idiomaUsuario,"label.disponibilidad.suptaller")%>
                    </div>
                    <div style="width: 450px; float: left;">
                        <div style="float: left;">
                            <input id="suptaller" name="suptaller" type="text" class="inputTexto" size="30" maxlength="200" 
                            value="<%=datModif != null && datModif.getSupTaller() != null ? datModif.getSupTaller().toString()  : ""%>" onchange="reemplazarPuntos(this);"/>
                        </div>
                    </div>
                </div>
                <br><br>
                <div class="lineaFormulario">
                    <div style="width: 190px; float: left;" class="etiqueta">
                        <%=meLanbide50I18n.getMensaje(idiomaUsuario,"label.disponibilidad.supaulastaller")%>
                    </div>
                    <div style="width: 450px; float: left;">
                        <div style="float: left;">
                            <input id="supaulastaller" name="supaulastaller" type="text" class="inputTexto" size="30" maxlength="200" 
                            value="<%=datModif != null && datModif.getSupAulaTaller() != null ? datModif.getSupAulaTaller().toString()  : ""%>" onchange="reemplazarPuntos(this);"/>
                        </div>
                    </div>
                </div>
                <br><br>
                <div class="lineaFormulario">
                    <div style="width: 190px; float: left;" class="etiqueta">
                        <%=meLanbide50I18n.getMensaje(idiomaUsuario,"label.disponibilidad.supcampoprac")%>
                    </div>
                    <div style="width: 450px; float: left;">
                        <div style="float: left;">
                            <input id="supcampoprac" name="supcampoprac" type="text" class="inputTexto" size="30" maxlength="200" 
                            value="<%=datModif != null && datModif.getSupCampoPract() != null ? datModif.getSupCampoPract().toString()  : ""%>" onchange="reemplazarPuntos(this);"/>
                        </div>
                    </div>
                </div>
                <br><br>
                <div class="botonera">
                    <input type="button" id="btnAceptar" name="btnAceptar" class="botonGeneral" value="<%=meLanbide50I18n.getMensaje(idiomaUsuario, "btn.aceptar")%>" onclick="guardarDatos()">
                    <input type="button" id="btnCancelar" name="btnCancelar" class="botonGeneral" value="<%=meLanbide50I18n.getMensaje(idiomaUsuario, "btn.cancelar")%>" onclick="cancelar();">
                </div>
            </div>
        </form>
    </div>
</div>
   
    
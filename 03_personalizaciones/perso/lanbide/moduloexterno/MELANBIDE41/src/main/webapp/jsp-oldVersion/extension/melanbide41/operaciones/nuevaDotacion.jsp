<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide41.i18n.MeLanbide41I18n" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide41.vo.dotacion.DotacionVO" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide41.vo.especialidades.EspecialidadesVO" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide41.util.ConstantesMeLanbide41" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide41.util.ConfigurationParameter" %>
<%@page import="java.util.ArrayList" %>
<%@page import="java.util.List" %>
<%@page import="java.text.SimpleDateFormat" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>

<div>
        <%
            DotacionVO datModif = new DotacionVO();
            EspecialidadesVO datoEspecialidad = new EspecialidadesVO();
            String codOrganizacion = "";
            String numExpediente = "";
            String nuevo = "1";
            String fechaAdqui = "";
            MeLanbide41I18n meLanbide41I18n = MeLanbide41I18n.getInstance();
            
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
                String nombreModulo = (String)request.getAttribute("nombreModulo");
                

                if(request.getAttribute("datModif") != null)
                {
                    datModif = (DotacionVO)request.getAttribute("datModif");
                    //SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
                    //fechaAdqui = formatoFecha.format(datModif.getFechaAdq());
                    fechaAdqui = datModif.getFechaAdq();
                }

                if(request.getAttribute("datoEspecialidad") != null)
                {
                    datoEspecialidad = (EspecialidadesVO)request.getAttribute("datoEspecialidad");
                }
            }
            catch(Exception ex)
            {
            }
        %>
        
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/estilo.css"/>
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/extension/melanbide41/melanbide41.css"/>
        
        <script type="text/javascript">
            var APP_CONTEXT_PATH = '<%=request.getContextPath()%>';
        </script>
        
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/general.js"></script>
        <script type="text/javascript" src="<c:url value='/scripts/listaComboBox.js'/>"></script>
        <script type="text/javascript" src="<c:url value='/scripts/calendario.js'/>"></script>

        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/validaciones.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/popup.js"></script>
        
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/meLanbide41/lanbide.js"></script>
        
        

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
                        parametros = "tarea=preparar&modulo=MELANBIDE41&operacion=crearDotacion&tipo=0&numero=<%=numExpediente%>"
                                +"&cantidad="+escape(document.getElementById('cantidad').value)
                                +"&denominacionET="+ escape(document.getElementById('denominacionET').value)
                                +"&meLanbide41FechaAdqui="+ document.getElementById('meLanbide41FechaAdqui').value
                                +"&idEpsol=<%=datoEspecialidad.getId()%>";
                        
                    }
                    else{
                        parametros = "tarea=preparar&modulo=MELANBIDE41&operacion=modificarDotacion&tipo=0&numero=<%=numExpediente%>"
                                +"&id=<%=datModif != null && datModif.getId() != null ? datModif.getId().toString() : ""%>"
                                +"&cantidad="+escape(document.getElementById('cantidad').value)
                                +"&denominacionET="+ escape(document.getElementById('denominacionET').value)
                                +"&meLanbide41FechaAdqui="+ document.getElementById('meLanbide41FechaAdqui').value
                                +"&idEpsol=<%=datoEspecialidad.getId()%>";
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
                        var listaDotacion = new Array();
                        var fila = new Array();
                        var nodoFila;
                        var hijosFila;
                        for(j=0;hijos!=null && j<hijos.length;j++){
                            if(hijos[j].nodeName=="CODIGO_OPERACION"){                            
                                codigoOperacion = hijos[j].childNodes[0].nodeValue;
                                listaDotacion[j] = codigoOperacion;
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
                                        else if(hijosFila[cont].nodeName=="DOT_NUM"){
                                            if(hijosFila[cont].childNodes.length > 0){
                                                fila[1] = hijosFila[cont].childNodes[0].nodeValue;
                                            }
                                            else{
                                                fila[1] = '-';
                                            }
                                        }
                                        else if(hijosFila[cont].nodeName=="DOT_CANT"){
                                            if(hijosFila[cont].childNodes.length > 0){
                                                fila[2] = hijosFila[cont].childNodes[0].nodeValue;
                                            }
                                            else{
                                                fila[2] = '-';
                                            }
                                        }
                                        else if(hijosFila[cont].nodeName=="DOT_DET"){
                                            if(hijosFila[cont].childNodes.length > 0){
                                                fila[3] = hijosFila[cont].childNodes[0].nodeValue;
                                            }
                                            else{
                                                fila[3] = '-';
                                            }
                                        }
                                        else if(hijosFila[cont].nodeName=="DOT_FAD"){
                                            if(hijosFila[cont].childNodes.length > 0){
                                                fila[4] = hijosFila[cont].childNodes[0].nodeValue;
                                            }
                                            else{
                                                fila[4] = '-';
                                            }
                                        }
                                        else if(hijosFila[cont].nodeName=="ID_ESPSOL"){
                                            if(hijosFila[cont].childNodes.length > 0){
                                                fila[5] = hijosFila[cont].childNodes[0].nodeValue;
                                            }
                                            else{
                                                fila[5] = '-';
                                            }
                                        }
                                }
                                listaDotacion[j] = fila;
                                fila = new Array();
                            }   
                        }//for(j=0;hijos!=null && j<hijos.length;j++)
                        if(codigoOperacion=="0"){
                            //jsp_alerta("A",'Correcto');
                            window.returnValue =  listaDotacion;
                            cerrarVentana();
                        }else if(codigoOperacion=="1"){
                            jsp_alerta("A",'<%=meLanbide41I18n.getMensaje(idiomaUsuario,"error.errorBD")%>');
                        }else if(codigoOperacion=="2"){
                            jsp_alerta("A",'<%=meLanbide41I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                        }else if(codigoOperacion=="3"){
                            jsp_alerta("A",'<%=meLanbide41I18n.getMensaje(idiomaUsuario,"error.pasoParametros")%>');
                        }else if(codigoOperacion=="4"){
                            jsp_alerta("A",'<%=meLanbide41I18n.getMensaje(idiomaUsuario,"error.expSinTercero")%>');
                        }else{
                                jsp_alerta("A",'<%=meLanbide41I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
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
                var resultado = 1; //jsp_alerta('','<%=meLanbide41I18n.getMensaje(idiomaUsuario, "msg.preguntaCancelar")%>');
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
                var cantidad = document.getElementById('cantidad').value;
                if(cantidad == null || cantidad == ''){
                    mensajeValidacion = '<%=meLanbide41I18n.getMensaje(idiomaUsuario, "msg.todosCamposOblig")%>';
                    return false;
                }
                else
                {
                    if(!validarNumerico(document.getElementById('cantidad'))){
                        mensajeValidacion = '<%=meLanbide41I18n.getMensaje(idiomaUsuario, "dotacion.msg.cantidad.errInteger")%>';
                        return false;
                    }
                }
                
                var denominacionET = document.getElementById('denominacionET').value;
                if(denominacionET == null || denominacionET == ''){
                    mensajeValidacion = '<%=meLanbide41I18n.getMensaje(idiomaUsuario, "msg.todosCamposOblig")%>';
                    return false;
                }
                else{
                    if(!compruebaTamanoCampo(document.getElementById('denominacionET'),1000)){
                         mensajeValidacion = '<%=meLanbide41I18n.getMensaje(idiomaUsuario, "msg.errExcdTexto")%>';
                         return false;
                    }
                }

                var meLanbide41FechaAdqui = document.getElementById('meLanbide41FechaAdqui').value;
                if(meLanbide41FechaAdqui == null || meLanbide41FechaAdqui == ''){
                    mensajeValidacion = '<%=meLanbide41I18n.getMensaje(idiomaUsuario, "dotacion.msg.superficie")%>';
                    return false;
                }
                else
                {
                    if(!comprobarFechaLanbide(document.getElementById('meLanbide41FechaAdqui'))){
                        mensajeValidacion = '<%=meLanbide41I18n.getMensaje(idiomaUsuario, "dotacion.msg.meLanbide41FechaAdqui.errFormatoFec")%>';
                    return false;
                    }
                }
               return correcto;
            }
            
            function comprobarFechaLanbide(inputFecha) {
                var formato = 'dd/mm/yyyy';
                if (Trim(inputFecha.value)!='') {
                  var D = ValidarFechaConFormatoLanbide(inputFecha.value,formato);
                  if (!D[0]){
                    jsp_alerta("A","<%=meLanbide41I18n.getMensaje(idiomaUsuario, "msg.fechaNoVal")%>");
                    document.getElementById(inputFecha.name).focus();
                    document.getElementById(inputFecha.name).select();
                    return false;
                  } else {
                    inputFecha.value = D[1];
                    return true;
                  }//if (!D[0])
                }//if (Trim(inputFecha.value)!='')
                return true;
            }//comprobarFechaLanbide
            
            //Funcion para el calendario de fecha 
            function mostrarCalFechaAdqui(evento) { 
                if(window.event) 
                    evento = window.event;
                if (document.getElementById("calMeLanbide41FechaAdqui").src.indexOf("icono.gif") != -1 )
                    showCalendar('forms[0]','meLanbide41FechaAdqui',null,null,null,'','calMeLanbide41FechaAdqui','',null,null,null,null,null,null,null, null,evento);        
            }//mostrarCalFechaAdqui

            function comprobarFecha(inputFecha) {
                if (Trim(inputFecha.value)!='') {
                    if (!ValidarFechaConFormato(document.forms[0],inputFecha)){
                        jsp_alerta("A","<%=meLanbide41I18n.getMensaje(idiomaUsuario, "msg.fechaNoVal")%>");
                return false;
            }
        }
        return true;
    }
    
    function compruebaTamanoCampo(elemento, maxTex){
                var texto = elemento.value;
                if(texto.length>maxTex){
                    jsp_alerta("A", '<%=meLanbide41I18n.getMensaje(idiomaUsuario, "msg.errExcdTexto")%>');
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
                        <%=meLanbide41I18n.getMensaje(idiomaUsuario,"label.dotacion.nuevaDotacion")%>
                    </span>
                </div>
                <div class="lineaFormulario">
                    <div style="width: 190px; float: left;" class="etiqueta">
                        <%=meLanbide41I18n.getMensaje(idiomaUsuario,"label.dotacion.cantidad")%>
                    </div>
                    <div style="width: 450px; float: left;">
                        <div style="float: left;">
                            <input id="cantidad" name="cantidad" type="text" class="inputTexto" size="15" maxlength="9" 
                            value="<%=datModif != null && datModif.getCantidad() != null ? datModif.getCantidad().toString().replaceAll("\\.", ",")  : ""%>" onchange="reemplazarPuntos(this);"/>
                        </div>
                    </div>
                </div>
                <br><br>
                <div class="lineaFormulario">
                    <div style="width: 190px; float: left;" class="etiqueta">
                        <%=meLanbide41I18n.getMensaje(idiomaUsuario,"label.dotacion.denominacionET")%>
                    </div>
                    <div style="width: 450px; float: left;">
                        <div style="float: left;">
                            <textarea rows="5" cols="70" id="denominacionET" name="denominacionET" maxlength="1000" onblur="compruebaTamanoCampo(this,1000)"><%=datModif != null && datModif.getDenominacionET() != null ? datModif.getDenominacionET()  : ""%></textarea>
                        </div>
                    </div>
                </div>
                <br><br>
                <div class="lineaFormulario">
                    <div style="width: 190px; float: left;" class="etiqueta">
                        <%=meLanbide41I18n.getMensaje(idiomaUsuario,"label.dotacion.meLanbide41FechaAdqui")%>
                    </div>
                    <div style="width: 450px; float: left;">
                        <div style="float: left;">
                            <input type="text" class="inputTxtFecha" 
                                   id="meLanbide41FechaAdqui" name="meLanbide41FechaAdqui"
                                   maxlength="10"  size="10"
                                   value="<%=fechaAdqui%>"
                                   onkeyup = "return SoloCaracteresFechaLanbide(this);"
                                   onblur = "javascript:return comprobarFechaLanbide(this);"
                                   onfocus="javascript:this.select();"/>
                            <A href="javascript:calClick(event);return false;" onClick="mostrarCalFechaAdqui(event);return false;" style="text-decoration:none;">   <!-- onblur="javascript:return comprobarFecha(this);" onkeyup="return SoloCaracteresFecha(this);" -->
                                <IMG style="border: 0px solid none" height="17" id="calMeLanbide41FechaAdqui" name="calMeLanbide41FechaAdqui" border="0" 
                                     src="<c:url value='/images/calendario/icono.gif'/>" > <!--width="25"-->
                            </A>

                        </div>
                    </div>
                </div>
                <br><br>
                <div class="botonera">
                    <input type="button" id="btnAceptar" name="btnAceptar" class="botonGeneral" value="<%=meLanbide41I18n.getMensaje(idiomaUsuario, "btn.aceptar")%>" onclick="guardarDatos()">
                    <input type="button" id="btnCancelar" name="btnCancelar" class="botonGeneral" value="<%=meLanbide41I18n.getMensaje(idiomaUsuario, "btn.cancelar")%>" onclick="cancelar();">
                </div>
            </div>
        </form>
    <div id="popupcalendar" class="text"></div>
    </div>
</div>
                
   
    
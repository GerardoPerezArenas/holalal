<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>

<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide45.i18n.MeLanbide45I18n" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide45.vo.moduloform.ModuloFormVO" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide45.util.ConstantesMeLanbide45" %>
<%@page import="java.util.ArrayList" %>
<%@page import="java.util.List" %>

<div>
        <%
            ModuloFormVO datModif = new ModuloFormVO();
            String codOrganizacion = "";
            String numExpediente = "";
            String nuevo = "1";
            MeLanbide45I18n meLanbide45I18n = MeLanbide45I18n.getInstance();
            int idiomaUsuario = 1;
 int apl = 5;
 String css = "";
            String obDisplay = "none";
            String obDisplayUC = "none";
            String obDisplayCodMF = "none";
            String obDisplayCodUC = "none";

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
                nuevo = (String)request.getAttribute("nuevo");

                if(request.getAttribute("datModif") != null)
                {
                    datModif = (ModuloFormVO)request.getAttribute("datModif");
                }
                if(datModif.getExiste() != null && datModif.getExiste().equals("N")){
                    obDisplay = "inline";
                }   
                if (datModif.getExiste().equals("S")){
                    obDisplayCodMF = "inline";
                }             
                if(datModif.getUc_existe() != null && datModif.getUc_existe().equals("N")){
                    obDisplayUC = "inline";                    
                }                              
                if(datModif.getUc_existe().equals("S")){
                    obDisplayCodUC = "inline";
                }
                
            }
            catch(Exception ex)
            {
            }
        %>
        
        <jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
<jsp:setProperty name="descriptor"  property="idi_cod" value="<%=idiomaUsuario%>" />
<jsp:setProperty name="descriptor"  property="apl_cod" value="<%=apl%>" />
<link rel="StyleSheet" media="screen" type="text/css" href="<%=request.getContextPath()%><%=css%>">
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/>
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/extension/melanbide45/melanbide45.css"/>
        <script type="text/javascript">
            var APP_CONTEXT_PATH = '<%=request.getContextPath()%>';
        </script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/general.js"></script>
        <script type="text/javascript" src="<c:url value='/scripts/listaComboBox.js'/>"></script>

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
                    var uc_existe = "";
                    var existe = "";
                    if(document.getElementById('uc_existeS').checked)
                        uc_existe="S";                    
                    if(document.getElementById('uc_existeN').checked)
                        uc_existe="N";
                    if(document.getElementById('existeS').checked)
                        existe="S";                    
                    if(document.getElementById('existeN').checked)
                        existe="N";

                    if(nuevo != null && nuevo == "1"){
                        parametros = "tarea=preparar&modulo=MELANBIDE45&operacion=crearModuloForm&tipo=0&numero=<%=numExpediente%>"
                                +"&denominacion="+escape(document.getElementById('denominacion').value)
                                +"&duracion="+ document.getElementById('duracion').value
                                +"&objetivo="+ escape(document.getElementById('objetivo').value)
                                +"&contenidoTP="+ escape(document.getElementById('contenidoTP').value)
                                +"&codMod="+ escape(document.getElementById('codMod').value)
                                +"&codUC="+ escape(document.getElementById('codUC').value)
                                +"&desUC="+ escape(document.getElementById('desUC').value)
                                +"&duracionMax="+ escape(document.getElementById('duracionMax').value)
                                +"&ucNivel="+ escape(document.getElementById('uc_nivel').value)
                                +"&ucExiste="+ uc_existe
                                +"&nivel="+ escape(document.getElementById('nivel').value)
                                +"&existe="+ existe;
                        
                    }
                    else{
                        parametros = "tarea=preparar&modulo=MELANBIDE45&operacion=modificarModuloForm&tipo=0&numero=<%=numExpediente%>"
                                +"&id=<%=datModif != null && datModif.getId() != null ? datModif.getId().toString() : ""%>"
                                +"&denominacion="+escape(document.getElementById('denominacion').value)
                                +"&duracion="+ document.getElementById('duracion').value
                                +"&objetivo="+ escape(document.getElementById('objetivo').value)
                                +"&contenidoTP="+ escape(document.getElementById('contenidoTP').value)
                                +"&codMod="+ escape(document.getElementById('codMod').value)
                                +"&codUC="+ escape(document.getElementById('codUC').value)
                                +"&desUC="+ escape(document.getElementById('desUC').value)
                                +"&duracionMax="+ escape(document.getElementById('duracionMax').value)
                                +"&ucNivel="+ escape(document.getElementById('uc_nivel').value)
                                +"&ucExiste="+ uc_existe
                                +"&nivel="+ escape(document.getElementById('nivel').value)
                                +"&existe="+ existe;
                        
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
                        var lista = new Array();
                        var fila = new Array();
                        var nodoFila;
                        var hijosFila;
                        for(j=0;hijos!=null && j<hijos.length;j++){
                            if(hijos[j].nodeName=="CODIGO_OPERACION"){                            
                                codigoOperacion = hijos[j].childNodes[0].nodeValue;
                                lista[j] = codigoOperacion;
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
                                        else if(hijosFila[cont].nodeName=="MDF_COD"){
                                            if(hijosFila[cont].childNodes.length > 0){
                                                fila[1] = hijosFila[cont].childNodes[0].nodeValue;
                                            }
                                            else{
                                                fila[1] = '-';
                                            }
                                        }
                                        else if(hijosFila[cont].nodeName=="MDF_DEN"){
                                            if(hijosFila[cont].childNodes.length > 0){
                                                fila[2] = hijosFila[cont].childNodes[0].nodeValue;
                                            }
                                            else{
                                                fila[2] = '-';
                                            }
                                        }
                                        else if(hijosFila[cont].nodeName=="MDF_UC_COD"){
                                            if(hijosFila[cont].childNodes.length > 0){
                                                fila[3] = hijosFila[cont].childNodes[0].nodeValue;
                                            }
                                            else{
                                                fila[3] = '-';
                                            }
                                        }
                                        else if(hijosFila[cont].nodeName=="MDF_UC_DEN"){
                                            if(hijosFila[cont].childNodes.length > 0){
                                                fila[4] = hijosFila[cont].childNodes[0].nodeValue;
                                            }
                                            else{
                                                fila[4] = '-';
                                            }
                                        }
                                        else if(hijosFila[cont].nodeName=="MDF_DUR"){
                                            if(hijosFila[cont].childNodes.length > 0){
                                                fila[5] = hijosFila[cont].childNodes[0].nodeValue;
                                                var tex = fila[5].toString();
                                                tex = tex.replace(".", ",");
                                                fila[5] = tex;
                                            }
                                            else{
                                                fila[5] = '-';
                                            }
                                        }
                                        else if(hijosFila[cont].nodeName=="MDF_DUR_MAX_TEL"){
                                            if(hijosFila[cont].childNodes.length > 0){
                                                fila[6] = hijosFila[cont].childNodes[0].nodeValue;
                                            }
                                            else{
                                                fila[6] = '-';
                                            }
                                        }
                                        else if(hijosFila[cont].nodeName=="MDF_OBJ"){
                                            if(hijosFila[cont].childNodes.length > 0){
                                                fila[7] = hijosFila[cont].childNodes[0].nodeValue;
                                              }
                                            else{
                                                fila[7] = '-';
                                            }
                                        }
                                        else if(hijosFila[cont].nodeName=="MDF_CP"){
                                            if(hijosFila[cont].childNodes.length > 0){
                                                fila[8] = hijosFila[cont].childNodes[0].nodeValue;
                                            }
                                            else{
                                                fila[8] = '-';
                                            }
                                        }
                                }
                                lista[j] = fila;
                                fila = new Array();
                            }   
                        }//for(j=0;hijos!=null && j<hijos.length;j++)
                        if(codigoOperacion=="0"){
                            //jsp_alerta("A",'Correcto');
                            //window.returnValue =  lista;
							self.parent.opener.retornoXanelaAuxiliar(lista);
                            cerrarVentana();
                        }else if(codigoOperacion=="1"){
                            jsp_alerta("A",'<%=meLanbide45I18n.getMensaje(idiomaUsuario,"error.errorBD")%>');
                        }else if(codigoOperacion=="2"){
                            jsp_alerta("A",'<%=meLanbide45I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                        }else if(codigoOperacion=="3"){
                            jsp_alerta("A",'<%=meLanbide45I18n.getMensaje(idiomaUsuario,"error.pasoParametros")%>');
                        }else if(codigoOperacion=="4"){
                            jsp_alerta("A",'<%=meLanbide45I18n.getMensaje(idiomaUsuario,"error.expSinTercero")%>');
                        }else{
                                jsp_alerta("A",'<%=meLanbide45I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
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
                var resultado = 1; //jsp_alerta('','<%=meLanbide45I18n.getMensaje(idiomaUsuario, "msg.preguntaCancelar")%>');
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
                mensajeValidacion = '<%=meLanbide45I18n.getMensaje(idiomaUsuario, "msg.reviseCamposOblig")%>';
                var correcto = true;
                var codMod = document.getElementById('codMod').value;
                var codUC = document.getElementById('codUC').value;
                if(
                    (!document.getElementById('existeS').checked && !document.getElementById('existeN').checked)
                    || (!document.getElementById('uc_existeS').checked && !document.getElementById('uc_existeN').checked)   
                    || (document.getElementById('existeS').checked && (codMod === null || codMod === ''))
                    || (document.getElementById('uc_existeS').checked && (codUC === null || codUC === ''))                    
                  ){
                    return false;
                }

                if(document.getElementById('existeN').checked)
                {
                    var denominacion = document.getElementById('denominacion').value;
                    if(denominacion == null || denominacion == ''){
                        return false;
                    }
                    else{
                        if(!compruebaTamanoCampo(document.getElementById('denominacion'),500)){
                             return false;
                        }
                    }

                    var nivel = document.getElementById('nivel').value;
                    if(nivel == null || nivel == ''){
                        return false;
                    }


                    var duracion = document.getElementById('duracion').value;
                    if(duracion == null || duracion == ''){
                        return false;
                    }

                    var duracionMax = document.getElementById('duracionMax').value;
                    if(duracionMax == null || duracionMax == ''){
                        return false;
                    }
                }
                
                if(document.getElementById('uc_existeN').checked)
                {
                    var desUC = document.getElementById('desUC').value;
                    if(desUC == null || desUC == ''){
                        return false;
                    }

                    var uc_nivel = document.getElementById('uc_nivel').value;
                    if(uc_nivel == null || uc_nivel == ''){
                        return false;
                    }
                }
                mensajeValidacion ='';
                return correcto;
            }
            
            function compruebaTamanoCampo(elemento, maxTex){
                var texto = elemento.value;
                if(texto.length>maxTex){
                    jsp_alerta("A", '<%=meLanbide45I18n.getMensaje(idiomaUsuario, "msg.errExcdTexto")%>');
                    elemento.focus();
                    return false;
                }
                return true;
            }

            function updateSpanVisibility() {
                if(document.getElementById('existeN').checked){
                    document.getElementById('obDenominacion').style.display = 'inline';
                    document.getElementById('obNivel').style.display = 'inline';
                    document.getElementById('obDuracion').style.display = 'inline';
                    document.getElementById('obCodMod').style.display = 'none';                                                    
                }
                else{
                    document.getElementById('obDenominacion').style.display = 'none';
                    document.getElementById('obNivel').style.display = 'none';
                    document.getElementById('obDuracion').style.display = 'none';
                    document.getElementById('obDuracionMax').style.display = 'none';                    
                    document.getElementById('obCodMod').style.display = 'inline';                                                    
                }
                if(document.getElementById('uc_existeN').checked){
                    document.getElementById('obNivelUC').style.display = 'inline';
                    document.getElementById('obDescUC').style.display = 'inline';                    
                    document.getElementById('obCodUC').style.display = 'none';                                                    
                }
                else if(document.getElementById('uc_existeS').checked){
                    document.getElementById('obNivelUC').style.display = 'none';
                    document.getElementById('obDescUC').style.display = 'none';                    
                    document.getElementById('obCodUC').style.display = 'inline';                                                    
                }
            }
            
    </script>

<div class="contenidoPantalla">
        <form>  <!-- action="/PeticionModuloIntegracion.do" method="POST" -->
            <div style="width: 100%; padding: 10px; text-align: left;">
                <div  class="sub3titulo" style="clear: both; text-align: center; font-size: 14px;">
                    <span>
                        <%=meLanbide45I18n.getMensaje(idiomaUsuario,"label.moduloformativo.nuevoModifModuloForm")%>
                    </span>
                </div>
                <br><br>
                <fieldset style="border: 1px solid;">
                    <legend style="font-weight: bold;"><%=meLanbide45I18n.getMensaje(idiomaUsuario,"label.moduloformativo.tituloFieldset")%></legend>
                    <div class="lineaFormulario" style="margin-left: 4px">
                        <div style="width: 190px; float: left;" class="etiqueta">
                            <span id="obCodUC" style="display:<%=obDisplayCodUC%>"><%=meLanbide45I18n.getMensaje(idiomaUsuario,"label.obligatorio")%></span>
                            <%=meLanbide45I18n.getMensaje(idiomaUsuario,"label.moduloformativo.codUC")%>
                        </div>
                        <div style="width: 200px; float: left;">
                            <div style="float: left;">
                                <input id="codUC" name="codUC" type="text" class="inputTexto" size="8" maxlength="8" 
                                value="<%=datModif != null && datModif.getCodUC() != null ? datModif.getCodUC()  : ""%>"/>
                            </div>
                        </div>
                    </div>
                    <br><br>

                    <div class="lineaFormulario" style="margin-left: 4px">
                        <div style="width: 190px; float: left;" class="etiqueta">
                            <span id="obDescUC" style="display:<%=obDisplayUC%>"><%=meLanbide45I18n.getMensaje(idiomaUsuario,"label.obligatorio")%></span>
                            <%=meLanbide45I18n.getMensaje(idiomaUsuario,"label.moduloformativo.desUC")%>
                        </div>
                        <div style="width: 200px; float: left;">
                            <div style="float: left;">
                                <input id="desUC" name="desUC" type="text" class="inputTexto" size="120" maxlength="500" 
                                value="<%=datModif != null && datModif.getDesUC() != null ? datModif.getDesUC()  : ""%>"/>
                            </div>
                        </div>
                    </div>
                    <br><br>


                    <div class="lineaFormulario" style="margin-left: 4px">
                        <div style="width: 190px; float: left;" class="etiqueta">
                            <span id="obNivelUC" style="display:<%=obDisplayUC%>"><%=meLanbide45I18n.getMensaje(idiomaUsuario,"label.obligatorio")%></span>
                            <%=meLanbide45I18n.getMensaje(idiomaUsuario,"label.moduloformativo.uc_nivel")%>
                        </div>
                        <div style="width: 450px; float: left;">
                            <div style="float: left;">
                                <input id="uc_nivel" name="uc_nivel" type="text" class="inputTexto" size="15" maxlength="2" 
                                value="<%=datModif != null && datModif.getUc_nivel() != null ? datModif.getUc_nivel().toString()  : ""%>" />
                            </div>
                        </div>
                    </div>
<br><br>
                    <div class="lineaFormulario" style="margin-left: 4px">
                        <div style="width: 190px; float: left;" class="etiqueta">
                            <span><%=meLanbide45I18n.getMensaje(idiomaUsuario,"label.obligatorio")%></span>
                            <%=meLanbide45I18n.getMensaje(idiomaUsuario,"label.moduloformativo.uc_existente")%>
                        </div>
                        <div style="width: 450px; float: left;">
                            <div style="float: left;">
                                <input onclick="updateSpanVisibility()" type="radio" name="uc_existe" <%=datModif.getUc_existe()!= null && datModif.getUc_existe().equals("S") ? "checked='checked'" : ""%> id="uc_existeS" value="S"><%=meLanbide45I18n.getMensaje(idiomaUsuario,"label.moduloformativo.si")%>
                                <input onclick="updateSpanVisibility()" type="radio" name="uc_existe" <%=datModif.getUc_existe()!= null && datModif.getUc_existe().equals("N") ? "checked='checked'" : ""%> id="uc_existeN" value="N"><%=meLanbide45I18n.getMensaje(idiomaUsuario,"label.moduloformativo.no")%>
                            </div>
                        </div>
                    </div><br><br>
                </fieldset>       
                        
               <br><br>
                <fieldset style="border: 1px solid;"><legend style="font-weight: bold;"><%=meLanbide45I18n.getMensaje(idiomaUsuario,"label.moduloformativo.tituloPestana")%></legend>
                    <div class="lineaFormulario" style="margin-left: 4px">
                        <div style="width: 190px; float: left;" class="etiqueta">
                            <span id="obcodMod" style="display:<%=obDisplayCodMF%>"><%=meLanbide45I18n.getMensaje(idiomaUsuario,"label.obligatorio")%></span>
                            <%=meLanbide45I18n.getMensaje(idiomaUsuario,"label.moduloformativo.codModulo")%>
                        </div>
                        <div style="width: 450px; float: left;">
                            <div style="float: left;">
                                <input id="codMod" name="codMod" type="text" class="inputTexto" size="8" maxlength="8" 
                                value="<%=datModif != null && datModif.getCodMod() != null ? datModif.getCodMod()  : ""%>"/>
                            </div>
                        </div>
                    </div>
                    <br><br>
                    <div class="lineaFormulario" style="margin-left: 4px">
                        <div style="width: 190px; float: left;" class="etiqueta">
                            <span id="obDenominacion" style="display:<%=obDisplay%>"><%=meLanbide45I18n.getMensaje(idiomaUsuario,"label.obligatorio")%></span>
                            <%=meLanbide45I18n.getMensaje(idiomaUsuario,"label.moduloformativo.denominacion")%>
                        </div>
                        <div style="width: 450px; float: left;">
                            <div style="float: left;">
                                <input id="denominacion" name="denominacion" type="text" class="inputTexto" size="120" maxlength="500" 
                                value="<%=datModif != null && datModif.getDenominacion() != null ? datModif.getDenominacion()  : ""%>"/>
                            </div>
                        </div>
                    </div>
                            <br><br>
                    <div class="lineaFormulario" style="margin-left: 4px">
                        <div style="width: 190px; float: left;" class="etiqueta">
                            <span id="obNivel" style="display:<%=obDisplay%>"><%=meLanbide45I18n.getMensaje(idiomaUsuario,"label.obligatorio")%></span>
                            <%=meLanbide45I18n.getMensaje(idiomaUsuario,"label.moduloformativo.nivel")%>
                        </div>
                        <div style="width: 450px; float: left;">
                            <div style="float: left;">
                                <input id="nivel" name="nivel" type="text" class="inputTexto" size="15" maxlength="2" 
                                value="<%=datModif != null && datModif.getNivel() != null ? datModif.getNivel().toString()  : ""%>" />
                            </div>
                        </div>
                    </div>
<br><br>
                    <div class="lineaFormulario" style="margin-left: 4px">
                        <div style="width: 190px; float: left;" class="etiqueta">
                            <span><%=meLanbide45I18n.getMensaje(idiomaUsuario,"label.obligatorio")%></span>
                            <%=meLanbide45I18n.getMensaje(idiomaUsuario,"label.moduloformativo.nivel_existente")%>
                        </div>
                        <div style="width: 450px; float: left;">
                            <div style="float: left;">
                                <input onclick="updateSpanVisibility()" type="radio" name="existe" id="existeS" <%=datModif.getExiste() != null && datModif.getExiste().equals("S") ? "checked='checked'" : ""%> value="S"><%=meLanbide45I18n.getMensaje(idiomaUsuario,"label.moduloformativo.si")%>
                                <input onclick="updateSpanVisibility()" type="radio" name="existe" id="existeN" <%=datModif.getExiste() != null && datModif.getExiste().equals("N") ? "checked='checked'" : ""%> value="N"><%=meLanbide45I18n.getMensaje(idiomaUsuario,"label.moduloformativo.no")%>
                            </div>
                        </div>
                    </div>
                         <br><br>   
                    <div class="lineaFormulario" style="margin-left: 4px">
                        <div style="width: 190px; float: left;" class="etiqueta">
                            <span id="obDuracion" style="display:<%=obDisplay%>"><%=meLanbide45I18n.getMensaje(idiomaUsuario,"label.obligatorio")%></span>
                            <%=meLanbide45I18n.getMensaje(idiomaUsuario,"label.moduloformativo.duracion")%>
                        </div>
                        <div style="width: 450px; float: left;">
                            <div style="float: left;">
                                <input id="duracion" name="duracion" type="text" class="inputTexto" size="15" maxlength="9" 
                                value="<%=datModif != null && datModif.getDuracion() != null ? datModif.getDuracion().toString().replaceAll("\\.", ",")  : ""%>" onchange="reemplazarPuntos(this);"/>
                            </div>
                        </div>
                    </div>
                    <br><br>


                    <div class="lineaFormulario" style="margin-left: 4px">
                        <div style="width: 190px; float: left;" class="etiqueta">
                            <span id="obDuracionMax" style="display:<%=obDisplay%>"><%=meLanbide45I18n.getMensaje(idiomaUsuario,"label.obligatorio")%></span>
                            <%=meLanbide45I18n.getMensaje(idiomaUsuario,"label.moduloformativo.duracionMax")%>
                        </div>
                        <div style="width: 450px; float: left;">
                            <div style="float: left;">
                                <input id="duracionMax" name="duracionMax" type="text" class="inputTexto" size="15" maxlength="9" 
                                value="<%=datModif != null && datModif.getDuracMax() != null ? datModif.getDuracMax().toString().replaceAll("\\.", ",")  : ""%>" onchange="reemplazarPuntos(this);"/>
                            </div>
                        </div>
                    </div>
                    <br><br>

                    <div class="lineaFormulario" style="margin-left: 4px">
                        <div style="width: 190px; float: left;" class="etiqueta">
                            <%=meLanbide45I18n.getMensaje(idiomaUsuario,"label.moduloformativo.objetivo")%>
                        </div>
                        <div style="width: 450px; float: left;">
                            <div style="float: left;">
                                <textarea rows="4" cols="90" id="objetivo" name="objetivo" maxlength="500" onblur="compruebaTamanoCampo(this,500)"><%=datModif != null && datModif.getObjetivo() != null ? datModif.getObjetivo()  : ""%></textarea>
                            </div>
                        </div>
                    </div>
                    <br><br>
                    <div class="lineaFormulario" style="margin-left: 4px">
                        <div style="width: 190px; float: left;" class="etiqueta">
                            <%=meLanbide45I18n.getMensaje(idiomaUsuario,"label.moduloformativo.contenidoTP")%>
                        </div>
                        <div style="width: 450px; float: left;">
                            <div style="float: left;">
                                <textarea rows="15" cols="90" id="contenidoTP" name="contenidoTP" maxlength="4000" onblur="compruebaTamanoCampo(this,4000)"><%=datModif != null && datModif.getContenidoTP() != null ? datModif.getContenidoTP()  : ""%></textarea>
                            </div>
                        </div>
                    </div>
                    <br><br>
                </fieldset>
                        
                <br><br>
                <div class="botonera">
                    <input type="button" id="btnAceptar" name="btnAceptar" class="botonGeneral" value="<%=meLanbide45I18n.getMensaje(idiomaUsuario, "btn.aceptar")%>" onclick="guardarDatos()">
                    <input type="button" id="btnCancelar" name="btnCancelar" class="botonGeneral" value="<%=meLanbide45I18n.getMensaje(idiomaUsuario, "btn.cancelar")%>" onclick="cancelar();">
                </div>
            </div>
        </form>
    </div>
</div>
   
    

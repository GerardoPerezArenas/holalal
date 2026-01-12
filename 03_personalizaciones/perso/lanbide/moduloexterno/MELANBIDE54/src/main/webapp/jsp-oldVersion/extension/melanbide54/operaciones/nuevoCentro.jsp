<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide54.i18n.MeLanbide54I18n" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide54.vo.CentroVO" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide54.vo.TerritorioHVO" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide54.util.ConstantesMeLanbide54" %>
<%@page import="java.util.ArrayList" %>
<%@page import="java.util.List" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>

        <%
            CentroVO datModif = new CentroVO();
            String codOrganizacion = "";
            String numExpediente = "";
            String nuevo = "1";
            MeLanbide54I18n meLanbide54I18n = MeLanbide54I18n.getInstance();
            
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
                    datModif = (CentroVO)request.getAttribute("datModif");
                }
            }
            catch(Exception ex)
            {
            }
        %>
        
        <script type="text/javascript">

            var mensajeValidacion = '';

            var comboListaCertificados;
            var listaCodigosCertificados = new Array();
            var listaDescripcionesCertificados = new Array();
           
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
                        parametros = "tarea=preparar&modulo=MELANBIDE54&operacion=crearCentro&tipo=0&numero=<%=numExpediente%>"
                                +"&codTh="+ document.getElementById('codTh').value
                                +"&codMun="+ escape(document.getElementById('codMun').value)
                                +"&calle="+ escape(document.getElementById('calle').value)
                                +"&portal="+ escape(document.getElementById('portal').value)
                                +"&piso="+ escape(document.getElementById('piso').value)
                                +"&letra="+ escape(document.getElementById('letra').value)
                                +"&cp="+ escape(document.getElementById('cp').value)
                                +"&telef="+ escape(document.getElementById('telef').value)
                                +"&existe="+ existe;
                        
                    }
                    else{
                        parametros = "tarea=preparar&modulo=MELANBIDE54&operacion=modificarCentro&tipo=0&numero=<%=numExpediente%>"
                                +"&id=<%=datModif != null && datModif.getId() != null ? datModif.getId().toString() : ""%>"
                                +"&codTh="+ document.getElementById('codTh').value
                                +"&codMun="+ escape(document.getElementById('codMun').value)
                                +"&calle="+ escape(document.getElementById('calle').value)
                                +"&portal="+ escape(document.getElementById('portal').value)
                                +"&piso="+ escape(document.getElementById('piso').value)
                                +"&letra="+ escape(document.getElementById('letra').value)
                                +"&cp="+ escape(document.getElementById('cp').value)
                                +"&telef="+ escape(document.getElementById('telef').value)
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
                                }else if(hijosFila[cont].nodeName=="MDF_COD"){
                                    if(hijosFila[cont].childNodes.length > 0){
                                        fila[1] = hijosFila[cont].childNodes[0].nodeValue;
                                    }
                                    else{
                                        fila[1] = '';
                                    }
                                }
                                else if(hijosFila[cont].nodeName=="MDF_DEN"){
                                    if(hijosFila[cont].childNodes.length > 0){
                                        fila[2] = hijosFila[cont].childNodes[0].nodeValue;
                                    }
                                    else{
                                        fila[2] = '';
                                    }
                                }else if(hijosFila[cont].nodeName=="MDF_UC_COD"){
                                    if(hijosFila[cont].childNodes.length > 0){
                                        fila[3] = hijosFila[cont].childNodes[0].nodeValue;
                                    }
                                    else{
                                        fila[3] = '';
                                    }
                                }else if(hijosFila[cont].nodeName=="MDF_UC_DEN"){
                                    if(hijosFila[cont].childNodes.length > 0){
                                        fila[4] = hijosFila[cont].childNodes[0].nodeValue;
                                    }
                                    else{
                                        fila[4] = '';
                                    }
                                }
                                else if(hijosFila[cont].nodeName=="MDF_DUR"){
                                    if(hijosFila[cont].childNodes.length > 0){
                                        fila[5] = hijosFila[cont].childNodes[0].nodeValue;
                                    }
                                    else{
                                        fila[5] = '-';
                                    }
                                }else if(hijosFila[cont].nodeName=="MDF_DUR_MAX_TEL"){
                                    if(hijosFila[cont].childNodes.length > 0){
                                        fila[6] = hijosFila[cont].childNodes[0].nodeValue;
                                    }
                                    else{
                                        fila[6] = '';
                                    }
                                }
                                else if(hijosFila[cont].nodeName=="MDF_OBJ"){
                                    if(hijosFila[cont].childNodes.length > 0){
                                        fila[7] = hijosFila[cont].childNodes[0].nodeValue;
                                    }
                                    else{
                                        fila[7] = '';
                                    }
                                }
                                else if(hijosFila[cont].nodeName=="MDF_CP"){
                                    if(hijosFila[cont].childNodes.length > 0){
                                        fila[8] = hijosFila[cont].childNodes[0].nodeValue;
                                    }
                                    else{
                                        fila[8] = '';
                                    }
                                }
                            }
                                lista[j] = fila;
                                fila = new Array();
                            }   
                        }//for(j=0;hijos!=null && j<hijos.length;j++)
                        if(codigoOperacion=="0"){
                            //jsp_alerta("A",'Correcto');
                            window.returnValue =  lista;
                            cerrarVentana();
                        }else if(codigoOperacion=="1"){
                            jsp_alerta("A",'<%=meLanbide54I18n.getMensaje(idiomaUsuario,"error.errorBD")%>');
                        }else if(codigoOperacion=="2"){
                            jsp_alerta("A",'<%=meLanbide54I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                        }else if(codigoOperacion=="3"){
                            jsp_alerta("A",'<%=meLanbide54I18n.getMensaje(idiomaUsuario,"error.pasoParametros")%>');
                        }else if(codigoOperacion=="4"){
                            jsp_alerta("A",'<%=meLanbide54I18n.getMensaje(idiomaUsuario,"error.expSinTercero")%>');
                        }else{
                                jsp_alerta("A",'<%=meLanbide54I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
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
                var resultado = 1; //jsp_alerta('','<%=meLanbide54I18n.getMensaje(idiomaUsuario, "msg.preguntaCancelar")%>');
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
                var codMod = document.getElementById('codMod').value;
                if(codMod == null || codMod == ''){
                    mensajeValidacion = '<%=meLanbide54I18n.getMensaje(idiomaUsuario, "msg.codigoModOblig")%>';
                    return false;
                }
                
                
                if(!document.getElementById('existeS').checked && !document.getElementById('existeN').checked){
                    mensajeValidacion = '<%=meLanbide54I18n.getMensaje(idiomaUsuario, "msg.todosCamposOblig")%>';
                    return false;
                }
                
                if(document.getElementById('existeN').checked)
                {
                    var denominacion = document.getElementById('denominacion').value;
                    if(denominacion == null || denominacion == ''){
                        mensajeValidacion = '<%=meLanbide54I18n.getMensaje(idiomaUsuario, "msg.todosCamposOblig")%>';
                        return false;
                    }
                    else{
                        if(!compruebaTamanoCampo(document.getElementById('denominacion'),500)){
                             mensajeValidacion = '<%=meLanbide54I18n.getMensaje(idiomaUsuario, "msg.errExcdTexto")%>';
                             return false;
                        }
                    }

                    var nivel = document.getElementById('nivel').value;
                    if(nivel == null || nivel == ''){
                        mensajeValidacion = '<%=meLanbide54I18n.getMensaje(idiomaUsuario, "msg.todosCamposOblig")%>';
                        return false;
                    }


                    var duracion = document.getElementById('duracion').value;
                    if(duracion == null || duracion == ''){
                        mensajeValidacion = '<%=meLanbide54I18n.getMensaje(idiomaUsuario, "msg.todosCamposOblig")%>';
                        return false;
                    }

                    var duracionMax = document.getElementById('duracionMax').value;
                    if(duracionMax == null || duracionMax == ''){
                        mensajeValidacion = '<%=meLanbide54I18n.getMensaje(idiomaUsuario, "msg.todosCamposOblig")%>';
                        return false;
                    }

                    var objetivo = document.getElementById('objetivo').value;
                    if(objetivo == null || objetivo == ''){
                        mensajeValidacion = '<%=meLanbide54I18n.getMensaje(idiomaUsuario, "msg.todosCamposOblig")%>';
                        return false;
                    }

                    var contenidoTP = document.getElementById('contenidoTP').value;
                    if(contenidoTP == null || contenidoTP == ''){
                        mensajeValidacion = '<%=meLanbide54I18n.getMensaje(idiomaUsuario, "msg.todosCamposOblig")%>';
                        return false;
                    }

                    var contenidoP = document.getElementById('contenidoPrac').value;
                    if(contenidoP == null || contenidoP == ''){
                        mensajeValidacion = '<%=meLanbide54I18n.getMensaje(idiomaUsuario, "msg.todosCamposOblig")%>';
                        return false;
                    }
                }
                
                //UC
                var nivel = document.getElementById('codUC').value;
                if(nivel == null || nivel == ''){
                    mensajeValidacion = '<%=meLanbide54I18n.getMensaje(idiomaUsuario, "msg.codigoUCOblig")%>';
                    return false;
                }
                
                if(!document.getElementById('uc_existeS').checked && !document.getElementById('uc_existeN').checked){
                    mensajeValidacion = '<%=meLanbide54I18n.getMensaje(idiomaUsuario, "msg.todosCamposOblig")%>';
                    return false;
                }
                
                if(document.getElementById('uc_existeN').checked)
                {
                    var duracion = document.getElementById('desUC').value;
                    if(duracion == null || duracion == ''){
                        mensajeValidacion = '<%=meLanbide54I18n.getMensaje(idiomaUsuario, "msg.todosCamposOblig")%>';
                        return false;
                    }

                    var duracionMax = document.getElementById('uc_nivel').value;
                    if(duracionMax == null || duracionMax == ''){
                        mensajeValidacion = '<%=meLanbide54I18n.getMensaje(idiomaUsuario, "msg.todosCamposOblig")%>';
                        return false;
                    }
                }
               return correcto;
            }
            
            function compruebaTamanoCampo(elemento, maxTex){
                var texto = elemento.value;
                if(texto.length>maxTex){
                    jsp_alerta("A", '<%=meLanbide54I18n.getMensaje(idiomaUsuario, "msg.errExcdTexto")%>');
                    elemento.focus();
                    return false;
                }
                return true;
            }
            
            function buscaCodigoTh (codTh){
            comboListaTh.buscaCodigo(codTh);
            }
            
            function buscaCodigoMun (codTh){
            comboListaMun.buscaCodigo(codTh);
            }     
            
            function cargarDatosTh(){ 
                var codThSeleccionado = document.getElementById("codListaTh").value;
                buscaCodigoTh(codThSeleccionado);
            }
            
            function cargarDatosMun(){ 
                var codThSeleccionado = document.getElementById("codListaMun").value;
                buscaCodigoMun(codThSeleccionado);
            }
            
            function rellenardatEspModificar(){
                if('<%=datModif%>' != null){
                    buscaCodigoTh('<%=datModif.getCodTH() != null ? datModif.getCodTH() : ""%>');  
                    buscaCodigoMun('<%=datModif.getCodMun() != null ? datModif.getCodMun() : ""%>'); 
                }
                else
                    alert('No hemos podido cargar los datos para modificar');
            }
            
    </script>
        
        
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/estilo.css"/>
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/extension/melanbide54/melanbide54.css"/>
        <script type="text/javascript">
            var APP_CONTEXT_PATH = '<%=request.getContextPath()%>';
        </script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/general.js"></script>
        <script type="text/javascript" src="<c:url value='/scripts/listaComboBox.js'/>"></script>

        

    <div class="contenidoPantalla">
        <form>  <!-- action="/PeticionModuloIntegracion.do" method="POST" -->

            <br><br>
            <div class="lineaFormulario">
                <div>
                    <div class="etiqueta" >
                        <%=meLanbide54I18n.getMensaje(idiomaUsuario,"label.nuevoCentro.centro")%>
                    </div>
                    <div>
                        <input type="text" name="codListaCertificados" id="codListaCertificados" size="10" class="inputTexto" value="" onkeyup="xAMayusculas(this);"/>
                        <input type="text" name="descListaCertificados"  id="descListaCertificados" size="112" class="inputTexto" readonly="true" value=""/>
                        <a href="" id="anchorListaCertificados" name="anchorListaCertificados">
                            <img src="<%=request.getContextPath()%>/images/listas/botondesplegable.gif" id="botonAplicacion"
                                name="botonAplicacion" height="14" width="14" border="0" style="cursor:hand;">
                        </a>
                    </div>
                </div>
            </div>
            <br><br>
            <div class="lineaFormulario">
                <div class="etiqueta" style="width: 190px; float: left;" >
                    <%=meLanbide54I18n.getMensaje(idiomaUsuario,"especialidades.label.descEspecialidad")%>
                </div>
                <br>
                <div>
                    <div class="etiqueta" >
                        <%=meLanbide54I18n.getMensaje(idiomaUsuario,"label.nuevoCentro.municipio")%>
                    </div>
                    <div>
                        <input type="text" name="codListaMun" id="codListaMun" size="10" class="inputTexto" value="" onkeyup="xAMayusculas(this);" />
                        <input type="text" name="descListaMun"  id="descListaMun" size="90" class="inputTexto" readonly="true" value="" />
                        <a href="" id="anchorListaCertificados" name="anchorListaCentros">
                            <img src="<%=request.getContextPath()%>/images/listas/botondesplegable.gif" id="botonAplicacion"
                                 name="botonAplicacion" height="14" width="14" border="0" style="cursor:hand;">
                        </a>
                    </div>
                </div>
            </div>
            <br><br>
            <div class="lineaFormulario" style="margin-left: 4px">
                <div style="width: 190px; float: left;" class="etiqueta">
                    <%=meLanbide54I18n.getMensaje(idiomaUsuario,"label.nuevoCentro.calle")%>
                </div>
                <div style="width: 450px; float: left;">
                    <div style="float: left;">
                        <input id="calle" name="calle" type="text" class="inputTexto" size="120" maxlength="500" 
                        value="<%=datModif != null && datModif.getCalle() != null ? datModif.getCalle()  : ""%>"/>
                    </div>
                </div>
            </div>
            <br><br>
            <div class="lineaFormulario" style="margin-left: 4px">
                <div style="width: 190px; float: left;" class="etiqueta">
                    <%=meLanbide54I18n.getMensaje(idiomaUsuario,"label.nuevoCentro.portal")%>
                </div>
                <div style="width: 450px; float: left;">
                    <div style="float: left;">
                        <input id="portal" name="portal" type="text" class="inputTexto" size="15" maxlength="4" 
                        value="<%=datModif != null && datModif.getPortal() != null ? datModif.getPortal().toString()  : ""%>" />
                    </div>
                </div>
            </div>
            <br><br>
            <div class="lineaFormulario" style="margin-left: 4px">
                <div style="width: 190px; float: left;" class="etiqueta">
                    <%=meLanbide54I18n.getMensaje(idiomaUsuario,"label.nuevoCentro.piso")%>
                </div>
                <div style="width: 450px; float: left;">
                    <div style="float: left;">
                        <input id="piso" name="piso" type="text" class="inputTexto" size="15" maxlength="4" 
                        value="<%=datModif != null && datModif.getPiso() != null ? datModif.getPiso().toString()  : ""%>" />
                    </div>
                </div>
            </div>
            <br><br>
            <div class="lineaFormulario" style="margin-left: 4px">
                <div style="width: 190px; float: left;" class="etiqueta">
                    <%=meLanbide54I18n.getMensaje(idiomaUsuario,"label.nuevoCentro.letra")%>
                </div>
                <div style="width: 450px; float: left;">
                    <div style="float: left;">
                        <input id="letra" name="letra" type="text" class="inputTexto" size="15" maxlength="4" 
                        value="<%=datModif != null && datModif.getLetra() != null ? datModif.getLetra().toString()  : ""%>" />
                    </div>
                </div>
            </div>
            <br><br>
            <div class="lineaFormulario" style="margin-left: 4px">
                <div style="width: 190px; float: left;" class="etiqueta">
                    <%=meLanbide54I18n.getMensaje(idiomaUsuario,"label.nuevoCentro.cp")%>
                </div>
                <div style="width: 450px; float: left;">
                    <div style="float: left;">
                        <input id="cp" name="cp" type="text" class="inputTexto" size="15" maxlength="9" 
                        value="<%=datModif != null && datModif.getCp() != null ? datModif.getCp().toString().replaceAll("\\.", ",")  : ""%>" onchange="reemplazarPuntos(this);"/>
                    </div>
                </div>
            </div>
            <br><br>
            <div class="lineaFormulario" style="margin-left: 4px">
                <div style="width: 190px; float: left;" class="etiqueta">
                    <%=meLanbide54I18n.getMensaje(idiomaUsuario,"label.nuevoCentro.telef")%>
                </div>
                <div style="width: 450px; float: left;">
                    <div style="float: left;">
                        <input id="telef" name="telef" type="text" class="inputTexto" size="15" maxlength="12" 
                        value="<%=datModif != null && datModif.getTlef() != null ? datModif.getTlef().toString().replaceAll("\\.", ",")  : ""%>" onchange="reemplazarPuntos(this);"/>
                    </div>
                </div>
            </div>
            <br><br><br><br>
            <div class="botonera">
                <input type="button" id="btnAceptar" name="btnAceptar" class="botonGeneral" value="<%=meLanbide54I18n.getMensaje(idiomaUsuario, "btn.aceptar")%>" onclick="guardarDatos()">
                <input type="button" id="btnCancelar" name="btnCancelar" class="botonGeneral" value="<%=meLanbide54I18n.getMensaje(idiomaUsuario, "btn.cancelar")%>" onclick="cancelar();">
            </div>
        </form>
    </div>
    <script type="text/javascript">

        listaCodigosTh[0] = "";
        listaDescripcionesTh[0] = "";

        /* Lista con los territorios históricos */
        var contador = 0;
        
        <logic:iterate  id="th" name="listaTh" scope="request">
        listaCodigosTh[contador] = ['<bean:write name="th" property="id" />'];
        listaDescripcionesTh[contador] = ['<bean:write name="th" property="descripcion" />'];
        contador++;
        </logic:iterate>
        
        var comboListaTh = new Combo("listaTh");
        comboListaTh.addItems(listaCodigosTh, listaDescripcionesTh);
        comboListaTh.change = cargarDatosTh;


        
        var nuevo = "<%=nuevo%>";
            if(nuevo==0){
                rellenardatEspModificar();
            }

    </script>
                
    

   
    
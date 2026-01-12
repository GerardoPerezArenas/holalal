<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide32.i18n.MeLanbide32I18n" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide32.vo.centroempleo.CeTrayectoriaVO" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide32.util.ConstantesMeLanbide32" %>

<html>
    <head> 
	<meta http-equiv="X-UA-Compatible" content="IE=Edge" />
        <%
            CeTrayectoriaVO trayModif = new CeTrayectoriaVO();
            String codOrganizacion = "";
            String numExpediente = "";
            String nuevo = "1";
            Integer codConvocatoria = null;
            MeLanbide32I18n meLanbide32I18n = MeLanbide32I18n.getInstance();
            int idiomaUsuario = 1;
 int apl = 5;
 String css = "";
        
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

                if(request.getAttribute("trayModif") != null)
                {
                    trayModif = (CeTrayectoriaVO)request.getAttribute("trayModif");
                    codConvocatoria=trayModif.getOriCetraConvocatoria();
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
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/extension/melanbide32/melanbide32.css"/>
        
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/general.js"></script>
        <script type="text/javascript">
            var APP_CONTEXT_PATH = '<%=request.getContextPath()%>';
        </script>
        
        <script type="text/javascript">
            
            
            var mensajeValidacion = '';
            var codConvocatoriaJSP = '<%=codConvocatoria%>';
            
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
                    var CONTEXT_PATH = '<%=request.getContextPath()%>'
                    var url = CONTEXT_PATH + "/PeticionModuloIntegracion.do";
                    var parametros = "";
                    var nuevo = "<%=nuevo%>";
                    if(nuevo != null && nuevo == "1"){
                        parametros = "tarea=preparar&modulo=MELANBIDE32&operacion=crearTrayectoriaCE&tipo=0&numero=<%=numExpediente%>"
                                +"&descripcion="+escape(document.getElementById('descripcion').value)
                                +"&duracion="+document.getElementById('duracion').value
                                +"&duracionValidada="+document.getElementById('duracionValidada').value;
                                
                    }
                    else{
                        parametros = "tarea=preparar&modulo=MELANBIDE32&operacion=modificarTrayectoriaCE&tipo=0&numero=<%=numExpediente%>"
                                +"&idTray=<%=trayModif != null && trayModif.getOriCeCod() != null ? trayModif.getOriCeCod().toString() : ""%>"
                                +"&descripcion="+escape(document.getElementById('descripcion').value)
                                +"&duracion="+document.getElementById('duracion').value
                                +"&duracionValidada="+document.getElementById('duracionValidada').value;
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
                                // Si viene a null al respuesta intentamos buscarla en el text como en el IE
                                if(ajax.responseXML!=null){
                                    xmlDoc = ajax.responseXML;
                                }else
                                {
                                    var text = ajax.responseText;
                                    xmlDoc=new ActiveXObject("Microsoft.XMLDOM");
                                    xmlDoc.async="false";
                                    xmlDoc.loadXML(text);
                                }
                            }//if(navigator.appName.indexOf("Internet Explorer")!=-1)
                        }//if (ajax.readyState==4 && ajax.status==200)
                        nodos = xmlDoc.getElementsByTagName("RESPUESTA");
                        var elemento = nodos[0];
                        var hijos = elemento.childNodes;
                        var codigoOperacion = null;
                        var listaTrayectorias = new Array();
                        var fila = new Array();
                        var nodoFila;
                        var hijosFila;
                        for(j=0;hijos!=null && j<hijos.length;j++){
                            if(hijos[j].nodeName=="CODIGO_OPERACION"){                            
                                codigoOperacion = hijos[j].childNodes[0].nodeValue;
                                listaTrayectorias[j] = codigoOperacion;
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
                                    else if(hijosFila[cont].nodeName=="DESCRIPCION"){
                                        if(hijosFila[cont].childNodes.length > 0){
                                            //fila[1] = hijosFila[cont].childNodes[0].nodeValue;
                                            var texto = hijosFila[cont].childNodes[0].nodeValue;
                                            //texto = texto.replace("#br###","<br />");
                                            fila[1] = texto;
                                        }
                                        else{
                                            fila[1] = '-';
                                        }
                                    }
                                    else if(hijosFila[cont].nodeName=="DURACION"){
                                        if(hijosFila[cont].childNodes.length > 0){
                                            fila[2] = hijosFila[cont].childNodes[0].nodeValue;
                                        }
                                        else{
                                            fila[2] = '-';
                                        }
                                    }else if(hijosFila[cont].nodeName=="DURACION_VALIDADA"){
                                        if(hijosFila[cont].childNodes.length > 0){
                                            fila[3] = hijosFila[cont].childNodes[0].nodeValue;
                                        }
                                        else{
                                            fila[3] = '-';
                                        }
                                    }
                                }
                                listaTrayectorias[j] = fila;
                                fila = new Array();
                            }   
                        }//for(j=0;hijos!=null && j<hijos.length;j++)
                        if(codigoOperacion=="0"){
                            //jsp_alerta("A",'Correcto');
                            self.parent.opener.retornoXanelaAuxiliar(listaTrayectorias);
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
            
            function validarDatos(){
                mensajeValidacion = "";
                var descripcion = document.getElementById('descripcion').value;
                // Hacemos esto apra evitar erorres de JavaScript por los saltos de lineas.
                document.getElementById('descripcionTextoPlano').value=descripcion;
                descripcion = document.getElementById('descripcionTextoPlano').value;
                document.getElementById('descripcionTextoPlano').value="";
                ////////////// 2017/08/15
                if(descripcion == null || descripcion == ''){
                    mensajeValidacion = '<%=meLanbide32I18n.getMensaje(idiomaUsuario, "ori.msg.todosCamposOblig")%>';
                    return false;
                }else{
                    if(!comprobarCaracteresEspeciales(descripcion)){
                        mensajeValidacion = '<%=meLanbide32I18n.getMensaje(idiomaUsuario, "ce.msg.descripcionCaracteresEspeciales")%>';
                        document.getElementById('descripcion').style.border = '1px solid red';
                        return false;
                    }else{
                        document.getElementById('descripcion').removeAttribute("style");
                    }
                }
                
                var duracion = document.getElementById('duracion').value;
                if(duracion == null || duracion == ''){
                    mensajeValidacion = '<%=meLanbide32I18n.getMensaje(idiomaUsuario, "ori.msg.todosCamposOblig")%>';
                    return false;
                }
                else
                {
                    if(comprobarDuracionTrayectoria(codConvocatoriaJSP,duracion)==1){//descripcion
                        mensajeValidacion = '<%=meLanbide32I18n.getMensaje(idiomaUsuario, "ori.msg.descripcionDuracionTrayectoria1")%>';
                        document.getElementById('duracion').style.border = '1px solid red';
                        return false;
                    }else{
                        document.getElementById('duracion').removeAttribute("style");
                    }
                    if(comprobarDuracionTrayectoria(codConvocatoriaJSP,duracion)>1){//descripcion
                        mensajeValidacion = '<%=meLanbide32I18n.getMensaje(idiomaUsuario, "ori.msg.descripcionDuracionTrayectoria2y3")%>';
                        document.getElementById('duracion').style.border = '1px solid red';
                        return false;
                    }else{
                        document.getElementById('duracion').removeAttribute("style");
                    }
                }
                /*
                // 13/10/2017 -- Nueva columna de datos validados por el Lanbide- Solo validamos los valores
               duracion = document.getElementById('duracionValidada').value;
                if(duracion == null || duracion == ''){
                    mensajeValidacion = '< %=meLanbide32I18n.getMensaje(idiomaUsuario, "ori.msg.todosCamposOblig")%>';
                    return false;
                }
                else
                {
                    if(comprobarDuracionTrayectoria(descripcion,duracion)==1){
                        mensajeValidacion = '< %=meLanbide32I18n.getMensaje(idiomaUsuario, "ori.msg.descripcionDuracionTrayectoria1")%>';
                        document.getElementById('duracion').style.border = '1px solid red';
                        return false;
                    }else{
                        document.getElementById('duracion').removeAttribute("style");
                    }
                    if(comprobarDuracionTrayectoria(descripcion,duracion)>1){
                        mensajeValidacion = '< %=meLanbide32I18n.getMensaje(idiomaUsuario, "ori.msg.descripcionDuracionTrayectoria2y3")%>';
                        document.getElementById('duracion').style.border = '1px solid red';
                        return false;
                    }else{
                        document.getElementById('duracion').removeAttribute("style");
                    }
                }
                
                */
                var correcto = true;
                
                if(!validarNumerico(document.getElementById('duracion'))){
                    document.getElementById('duracion').style.border = '1px solid red';
                    if(mensajeValidacion == '')
                        mensajeValidacion = '<%=meLanbide32I18n.getMensaje(idiomaUsuario, "ori.msg.duracionServicioIncorrecto2")%>';
                    correcto = false;
                }
                else{
                    try{
                        var anos = parseFloat(document.getElementById('duracion').value.replace(/\,/,"."));
                        if(anos < 0){
                            document.getElementById('duracion').style.border = '1px solid red';
                            if(mensajeValidacion == '')
                                mensajeValidacion = '<%=meLanbide32I18n.getMensaje(idiomaUsuario, "ori.msg.duracionServicioNegativo")%>';
                            correcto = false;
                        }else if(anos > 99.0){
                            document.getElementById('duracion').style.border = '1px solid red';
                            if(mensajeValidacion == '')
                                mensajeValidacion = '<%=meLanbide32I18n.getMensaje(idiomaUsuario, "ori.msg.duracionServicioIncorrecto")%>';
                            correcto = false;
                        }else{
                            document.getElementById('duracion').removeAttribute("style");
                        }
                    }
                    catch(err){
                        document.getElementById('duracion').style.border = '1px solid red';
                        if(mensajeValidacion == '')
                            mensajeValidacion = '<%=meLanbide32I18n.getMensaje(idiomaUsuario, "ori.msg.duracionServicioIncorrecto")%>';
                        correcto = false;
                    }
                }
                
                if(document.getElementById('duracionValidada')!=null && document.getElementById('duracionValidada').value!=""){
                    if(!validarNumerico(document.getElementById('duracionValidada'))){
                        document.getElementById('duracionValidada').style.border = '1px solid red';
                        if(mensajeValidacion == '')
                            mensajeValidacion = '<%=meLanbide32I18n.getMensaje(idiomaUsuario, "ori.msg.duracionServicioIncorrecto2")%>';
                        correcto = false;
                    }
                    else{
                        try{
                            var anos = parseFloat(document.getElementById('duracionValidada').value.replace(/\,/,"."));
                            if(anos < 0){
                                document.getElementById('duracionValidada').style.border = '1px solid red';
                                if(mensajeValidacion == '')
                                    mensajeValidacion = '<%=meLanbide32I18n.getMensaje(idiomaUsuario, "ori.msg.duracionServicioNegativo")%>';
                                correcto = false;
                            }else if(anos > 99.0){
                                document.getElementById('duracion').style.border = '1px solid red';
                                if(mensajeValidacion == '')
                                    mensajeValidacion = '<%=meLanbide32I18n.getMensaje(idiomaUsuario, "ori.msg.duracionServicioIncorrecto")%>';
                                correcto = false;
                            }else{
                                document.getElementById('duracionValidada').removeAttribute("style");
                            }
                        }
                        catch(err){
                            document.getElementById('duracionValidada').style.border = '1px solid red';
                            if(mensajeValidacion == '')
                                mensajeValidacion = '<%=meLanbide32I18n.getMensaje(idiomaUsuario, "ori.msg.duracionServicioIncorrecto")%>';
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
            function comprobarDuracionTrayectoria(texto, duracion)
            {
                var resultado = 0; 
                /* La descripcion es una constante de ConstantesMeLanbide32.
                 * Si empieza por 1, es la convocatoria 1, que puede ser hasta 9 años (Los texto empiezan correlativamente)
                 * Las demas solo un año de exèriencia ya que la convocatoria es anual
                 */
                if(texto!=null && texto!=""){
                    if(texto==="1")
                    {
                        if(duracion<0 || duracion > 9)
                        {
                            resultado = 1;
                            return resultado;
                        }
                    }else{
                        if(duracion<0 || duracion > 1)
                        {
                            resultado = 2;
                            return resultado;
                        }
                    }
                }
                return resultado;
                /*
                 * texto == '< %=ConstantesMeLanbide32.TRAYECTORIA_PRECARGA_1%>'
                 * texto == '< %=ConstantesMeLanbide32.TRAYECTORIA_PRECARGA_2%>' || texto == '< %=ConstantesMeLanbide32.TRAYECTORIA_PRECARGA_3%>' || texto == '< %=ConstantesMeLanbide32.TRAYECTORIA_PRECARGA_4%>'
                 */
            }
        </script>
    </head>
    <body class="contenidoPantalla">
        <form>
            <div style="width: 100%; padding: 10px; text-align: left;">
                <div class="tituloAzul" style="clear: both; text-align: left;">
                    <span>
                        <%=meLanbide32I18n.getMensaje(idiomaUsuario,"ce.label.trayectoriaCentroEmpleo")%>
                    </span>
                </div>

                <div class="lineaFormulario">
                    <div style="width: 190px; float: left;">
                        <%=meLanbide32I18n.getMensaje(idiomaUsuario,"ce.label.descTrayectoria")%>
                    </div>
                    <div style="width: 450px; float: left;">
                        <div style="float: left;">
                            <textarea rows="4" cols="61" id="descripcion" name="descripcion" maxlength="500" disabled="true" readonly="true"><%=trayModif != null && trayModif.getOriCetraDescripcion() != null ? trayModif.getOriCetraDescripcion() : ""%></textarea>
                        </div>
                        <!-- Hacemos este apaño para pasar el contenido de textarea a texto plano -->
                        <input id="descripcionTextoPlano" style="display: none;" />                        
                    </div>
                </div>

                <div class="lineaFormulario">
                    <div style="width: 190px; float: left;">
                        <%=meLanbide32I18n.getMensaje(idiomaUsuario,"ce.label.duracionServicio")%>
                    </div>
                    <div style="width: 450px; float: left;">
                        <div style="float: left;">
                            <input id="duracion" name="duracion" type="text" class="inputTexto" size="3" maxlength="5" 
                            value="<%=trayModif != null && trayModif.getOriCetraDuracion() != null ? trayModif.getOriCetraDuracion().toString().replaceAll("\\.", ",") : ""%>" onchange="reemplazarPuntos(this);"/>
                        </div>
                    </div>
                </div>
                        
                <div class="lineaFormulario">
                    <div style="width: 190px; float: left;">
                        <%=meLanbide32I18n.getMensaje(idiomaUsuario,"ce.label.duracionServicio.validado")%>
                    </div>
                    <div style="width: 450px; float: left;">
                        <div style="float: left;">
                            <input id="duracionValidada" name="duracionValidada" type="text" class="inputTexto" size="3" maxlength="5" 
                            value="<%=trayModif != null && trayModif.getOriCetraDuracionValidada() != null ? trayModif.getOriCetraDuracionValidada().toString().replaceAll("\\.", ",") : ""%>" onchange="reemplazarPuntos(this);"/>
                        </div>
                    </div>
                </div>
                <div class="botonera">
                    <input type="button" id="btnAceptar" name="btnAceptar" class="botonGeneral" value="<%=meLanbide32I18n.getMensaje(idiomaUsuario, "ori.btn.aceptar")%>" onclick="guardarDatos();">
                    <input type="button" id="btnCancelar" name="btnCancelar" class="botonGeneral" value="<%=meLanbide32I18n.getMensaje(idiomaUsuario, "ori.btn.cancelar")%>" onclick="cancelar();">
                </div>
            </div>
        </form>
    </body>
</html>

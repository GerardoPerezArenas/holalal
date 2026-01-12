<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>

<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.agora.business.util.GeneralValueObject"%>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide68.i18n.MeLanbide68I18n" %>
<%@page import="es.altia.common.service.config.Config"%>
<%@page import="es.altia.common.service.config.ConfigServiceHelper"%>
<%@page import="org.apache.commons.lang.StringEscapeUtils"%>

<!doctype html>
<html>
    <head> 
        <meta http-equiv="X-UA-Compatible" content="IE=Edge" />
        <%

            int idiomaUsuario = 0;
            int codOrganizacion = 0;
            UsuarioValueObject usuario = new UsuarioValueObject();
            try
            {
                if (session != null) 
                {
                    if (usuario != null) 
                    {
                        usuario = (UsuarioValueObject) session.getAttribute("usuario");
                        idiomaUsuario = usuario.getIdioma();
                        codOrganizacion  = usuario.getOrgCod();
                    }
                }
            }
            catch(Exception ex)
            {

            }   
            //Clase para internacionalizar los mensajes de la aplicacion.
            MeLanbide68I18n meLanbide68I18n = MeLanbide68I18n.getInstance();

            Config m_Config = ConfigServiceHelper.getConfig("common");
            String statusBar = m_Config.getString("JSP.StatusBar");

            String codTipDoc = request.getParameter("codTipDoc");
            String codMetadato = request.getParameter("codMetadato");
            String metadatoDCTM = request.getParameter("metadatoDCTM");
            String obligatorio = request.getParameter("obligatorio");
            
            String tituloPagina = "";
            if(codMetadato != null) {
                tituloPagina = meLanbide68I18n.getMensaje(idiomaUsuario,"label.Metadato.titulo.modifMetadato");
            }
            else
            {
                tituloPagina = meLanbide68I18n.getMensaje(idiomaUsuario, "label.Metadato.titulo.nuevoMetadato");
                codMetadato = "";
            }
        %>
        <%!
            // Funcion para escapar strings para javascript
            private String escape(String str) 
            {
                return StringEscapeUtils.escapeJavaScript(str);
            }
        %>
        <title><%=tituloPagina%></title>
        
        <script type="text/javascript" src="<c:url value='/scripts/validaciones.js'/>"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/popup.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/general.js"></script>
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/estilo.css"/>
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/extension/melanbide68/melanbide68.css"/>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/melanbide68/tabUtils.js"></script>      
        <script type="text/javascript">
            var APP_CONTEXT_PATH = '<%=request.getContextPath()%>';
        </script>

    </head>
    <body id="cuerpoNuevoMetadato" style="text-align: left;" > 
<div id="divCuerpo" class="contenidoPantalla" style="height: auto; padding: 10px; margin-right: 25px">
        <form  id="formNuevoMetadato">
            <fieldset id="fieldsetDatosMetadato" name="fieldsetDatosMetadato" style="width: 90%;">
                <legend class="legendAzul"><%=meLanbide68I18n.getMensaje(idiomaUsuario,"legend.Metadato.datosMetadato")%></legend>
  
                <div class="lineaFormulario" style="width: auto; float: left; margin: 10px;">
                    <div style="padding: 10px;">
                        <label class="etiqueta" style="text-align: center; position: relative; margin: 5px; margin-right: 25px"><%=meLanbide68I18n.getMensaje(idiomaUsuario,"label.Metadato.datosMetadato.Id")%></label>
                        <input type="text" maxlength="150" size="120" id="codMetadato" name="codMetadato" value="" class="inputTextoMinus" />
                    </div>
                    <div style="padding: 10px;">
                        <label class="etiqueta" style="text-align: center; position: relative; margin: 5px; margin-right: 25px"><%=meLanbide68I18n.getMensaje(idiomaUsuario,"label.Metadato.datosMetadato.DCTM")%></label>
                        <input type="text" maxlength="50" size="50" id="metadatoDCTM" name="metadatoDCTM" value="" class="inputTextoMinus" />
                    </div>
                    <div style="clear: both;padding: 10px;">
                        <label class="etiqueta" style="text-align: center; position: relative; margin: 5px; margin-right: 25px"><%=meLanbide68I18n.getMensaje(idiomaUsuario,"label.Metadato.datosMetadato.Oblig")%></label>
                        <input type="radio" id="obligatorioS" name="obligatorio" value="S"> Sí
                        <input type="radio" id="obligatorioN" name="obligatorio" value="N"> No
                    </div>

                </div>
            </fieldset>               

            <div class="botonera" style="padding: 20px">
                <input type="button" id="btnGuardarMetadato" name="btnGuardarMetadato" class="botonGeneral" value="<%=meLanbide68I18n.getMensaje(idiomaUsuario,"btn.aceptar")%>" onclick="guardar();">
                <input type="button" id="btnCancelarMetadato" name="btnCancelarMetadato" class="botonGeneral" value="<%=meLanbide68I18n.getMensaje(idiomaUsuario,"btn.cancelar")%>" onclick="cancelar();">
                <input type="button" id="btnCerrar" name="btnCerrar" class="botonGeneral" value="<%=meLanbide68I18n.getMensaje(idiomaUsuario,"btn.cerrar")%>" onclick="cerrarVentana();" style="display: none;">
            </div>  
        </form>
            
        <script type="text/javascript">
            var mensajeValidacion = '';

            if('<%=codMetadato%>' != ''){
                pintarDatos();

                document.getElementById("codMetadato").disabled=true;
            }else{
                document.getElementById("codMetadato").focus();
                document.getElementById('obligatorioN').checked = true;
                document.getElementById('obligatorioS').checked = false;
            }
           

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

            function pintarDatos(){
                document.getElementById('codMetadato').value = '<%=request.getParameter("codMetadato") != null ? request.getParameter("codMetadato"): "" %>';
                document.getElementById('metadatoDCTM').value = '<%=request.getParameter("metadatoDCTM") != null ? request.getParameter("metadatoDCTM"): "" %>';
                 
                if ('<%=request.getParameter("obligatorio")%>'=='S'){
                    document.getElementById('obligatorioN').checked = false;
                    document.getElementById('obligatorioS').checked = true;
                }
                else{
                    document.getElementById('obligatorioN').checked = true;
                    document.getElementById('obligatorioS').checked = false;
                }

                resizeForFF();
            }

            function resizeForFF(){
                if(navigator.appName.indexOf("Internet Explorer")==-1){
                    document.getElementById('cuerpoNuevoMetadato').style.width = '99%';
                    document.getElementById('divCuerpo').style.width = '98%';
                    document.getElementById('fieldsetDatosMetadato').style.width = '98%';
                }
            }

            function cancelar(){
                var resultado = jsp_alerta('','<%=meLanbide68I18n.getMensaje(idiomaUsuario, "msg.preguntaCancelar")%>');
                if (resultado == 1){
                    cerrarVentana();
                }
            }
            
            function cerrarVentana(){
                if(navigator.appName=='Microsoft Internet Explorer') { 
                      window.parent.window.opener=null; 
                      window.parent.window.close(); 
                } else if(navigator.appName=="Netscape") { 
                      top.window.opener = top; 
                      top.window.open('','_parent',''); 
                      top.window.close(); 
                }else{
                     window.close(); 
                 } 
            }

            function guardar(){
                if(validarDatos()){
                    var ajax = getXMLHttpRequest();
                    var nodos = null;
                    var CONTEXT_PATH = '<%=request.getContextPath()%>';
                    var url = CONTEXT_PATH + "/PeticionModuloIntegracion.do";
                    var parametros = "";
                    
                    if('<%=codMetadato%>' == '') {
                        if(document.getElementById('obligatorioS').checked){
                            parametros = 'tarea=preparar&modulo=MELANBIDE68&operacion=guardarMetadato&tipo=0&codTipDoc=<%=codTipDoc%>'
                                +'&codMetadato='+escape(document.getElementById('codMetadato').value)
                                +'&metadatoDCTM='+escape(document.getElementById('metadatoDCTM').value)
                                +'&obligatorio='+escape(document.getElementById('obligatorioS').value);
                        }else{
                            parametros = 'tarea=preparar&modulo=MELANBIDE68&operacion=guardarMetadato&tipo=0&codTipDoc=<%=codTipDoc%>'
                                +'&codMetadato='+escape(document.getElementById('codMetadato').value)
                                +'&metadatoDCTM='+escape(document.getElementById('metadatoDCTM').value)
                                +'&obligatorio='+escape(document.getElementById('obligatorioN').value);
                        }
                    }else{
                        if(document.getElementById('obligatorioS').checked){
                            parametros = 'tarea=preparar&modulo=MELANBIDE68&operacion=modificarMetadato&tipo=0&codTipDoc=<%=codTipDoc%>'
                                +'&codMetadato='+escape(document.getElementById('codMetadato').value)
                                +'&metadatoDCTM='+escape(document.getElementById('metadatoDCTM').value)
                                +'&obligatorio='+escape(document.getElementById('obligatorioS').value);
                        }else{
                            parametros = 'tarea=preparar&modulo=MELANBIDE68&operacion=modificarMetadato&tipo=0&codTipDoc=<%=codTipDoc%>'
                                +'&codMetadato='+escape(document.getElementById('codMetadato').value)
                                +'&metadatoDCTM='+escape(document.getElementById('metadatoDCTM').value)
                                +'&obligatorio='+escape(document.getElementById('obligatorioN').value);
                        }    
                    }
                    try{
                        ajax.open("POST",url,false);
                        ajax.setRequestHeader("Content-Type","application/x-www-form-urlencoded; charset=iso-8859-15");
                        ajax.setRequestHeader("Accept", "text/xml, application/xml, text/plain");     
                        ajax.send(parametros);
                        if (ajax.readyState==4 && ajax.status==200){
                            var xmlDoc = null;
                            if(navigator.appName.indexOf("Internet Explorer")!= -1){
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
                        var listaMetadatos = new Array();
                        var fila = new Array();
                        var nodoFila;
                        var hijosFila;
                        var nodoCampo;
                        var j;

                        for(j=0;hijos!=null && j<hijos.length;j++){
                            if(hijos[j].nodeName=="CODIGO_OPERACION"){                            
                                codigoOperacion = hijos[j].childNodes[0].nodeValue;
                                listaMetadatos[j] = codigoOperacion;
                            }else if(hijos[j].nodeName=="FILA"){
                                nodoFila = hijos[j];
                                hijosFila = nodoFila.childNodes;
                                for(var cont = 0; cont < hijosFila.length; cont++){
                                     if(hijosFila[cont].nodeName=="METADATO"){
                                        nodoCampo = hijosFila[cont];
                                        if(nodoCampo.childNodes.length > 0){
                                            fila[0] = nodoCampo.childNodes[0].nodeValue;
                                        }
                                        else{
                                            fila[0] = '-';
                                        }
                                    }
                                    if(hijosFila[cont].nodeName=="METADATODCTM"){
                                        nodoCampo = hijosFila[cont];
                                        if(nodoCampo.childNodes.length > 0){
                                            fila[1] = nodoCampo.childNodes[0].nodeValue;
                                        }
                                        else{
                                            fila[1] = '-';
                                        }
                                    }
                                    if(hijosFila[cont].nodeName=="OBLIGATORIO"){
                                        nodoCampo = hijosFila[cont];
                                        if(nodoCampo.childNodes.length > 0){
                                            fila[2] = nodoCampo.childNodes[0].nodeValue;
                                        }
                                        else{
                                            fila[2] = '-';
                                        }
                                    }
                                    if(hijosFila[cont].nodeName=="DESHABILITADO"){
                                        nodoCampo = hijosFila[cont];
                                        if(nodoCampo.childNodes.length > 0){
                                            fila[3] = nodoCampo.childNodes[0].nodeValue;
                                        }
                                        else{
                                            fila[3] = '-';
                                        }
                                    }                                 
                                }
                                listaMetadatos[j] = fila;
                                fila = new Array();
                            }   
                        }
                        if(codigoOperacion=="0"){
                            self.parent.opener.retornoXanelaAuxiliar(listaMetadatos);
                            if('<%=codMetadato%>' == '') { 
                                jsp_alerta("A",'<%=meLanbide68I18n.getMensaje(idiomaUsuario,"msg.metadatoGuardadoOK")%>');
                            }
                            else{
                                jsp_alerta("A",'<%=meLanbide68I18n.getMensaje(idiomaUsuario,"msg.metadatoModificadoOK")%>');
                            }
                            cerrarVentana();
                        }else if(codigoOperacion=="1"){
                            jsp_alerta("A",'<%=meLanbide68I18n.getMensaje(idiomaUsuario,"error.errorBD")%>');
                        }else if(codigoOperacion=="2"){
                            jsp_alerta("A",'<%=meLanbide68I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                        }else if(codigoOperacion=="3"){
                            jsp_alerta("A",'<%=meLanbide68I18n.getMensaje(idiomaUsuario,"error.pasoParametros")%>');
                        }else if(codigoOperacion=="4"){
                            jsp_alerta("A",'<%=meLanbide68I18n.getMensaje(idiomaUsuario,"error.metadatoDuplicado")%>');
                        }else{
                            jsp_alerta("A",'<%=meLanbide68I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                        }//if(
                    }
                    catch(Err){
                        jsp_alerta("A",'<%=meLanbide68I18n.getMensaje(idiomaUsuario,"error.errorProcesarRespuesta")%>');
                    }
                }else{
                    jsp_alerta("A", escape(mensajeValidacion));
                }
            }

            function validarDatos(){
                mensajeValidacion = '';
                var correcto = true;
                try{
                    if(!validarDatosMetadato()){
                        correcto = false;
                    }                
                }catch(err){
                    correcto = false;
                    if(mensajeValidacion == ''){
                        mensajeValidacion = '<%=meLanbide68I18n.getMensaje(idiomaUsuario, "msg.errorValidarDatos")%>';
                    }
                }
                return correcto;
            }

            function validarDatosMetadato(){
                var correcto = true;
                // Código de Metadato
                var cMetadato = document.getElementById('codMetadato').value;
                if(cMetadato == null || cMetadato == ''){
                    mensajeValidacion = '<%=meLanbide68I18n.getMensaje(idiomaUsuario, "msg.metadato.datosMetadato.codMetadatoVacio")%>';
                    correcto = false;
                }     
                // Metadato DCTM
                var cMetadatoDCTM = document.getElementById('metadatoDCTM').value;
                if(cMetadatoDCTM == null || cMetadatoDCTM == ''){
                    mensajeValidacion = '<%=meLanbide68I18n.getMensaje(idiomaUsuario, "msg.metadato.datosMetadato.metadatoDCTMVacio")%>';
                    correcto = false;
                }   
                return correcto;
            }
            
        </script>
        </div>
    </body>
</html>
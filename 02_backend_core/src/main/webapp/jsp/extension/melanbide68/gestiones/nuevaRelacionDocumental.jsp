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
            int apl = 5;
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

            String codTipDoc     = request.getParameter("codTipDoc");
            String tipDocLanbide = request.getParameter("tipDocLanbide");            
            String codDokusi  = request.getParameter("tipDocDokusi");
            String tipDokusi     = request.getParameter("tipDokusi");
            
            String tituloPagina = "";
            if(codTipDoc != null) {
                tituloPagina = meLanbide68I18n.getMensaje(idiomaUsuario,"label.TipDocDokusi.titulo.modifTipDoc");
            }
            else
            {
                tituloPagina = meLanbide68I18n.getMensaje(idiomaUsuario, "label.TipDocDokusi.titulo.nuevoTipDoc");
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

        <jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
        <jsp:setProperty name="descriptor"  property="idi_cod" value="<%=idiomaUsuario%>" />
        <jsp:setProperty name="descriptor"  property="apl_cod" value="<%=apl%>" />
        <script type="text/javascript" src="<c:url value='/scripts/validaciones.js'/>"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/popup.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/general.js"></script>
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/estilo.css"/>
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/extension/melanbide68/melanbide68.css"/>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/melanbide68/tabUtils.js"></script>
        <script type="text/javascript" src="<c:url value='/scripts/listaComboBox.js'/>"></script>
        <script type="text/javascript">
            var APP_CONTEXT_PATH = '<%=request.getContextPath()%>';
        </script>

    </head>
    <body id="cuerpoNuevoTipDoc" style="text-align: left;" > 
        <div id="divCuerpo" class="contenidoPantalla" style="overflow-y: auto; padding: 10px;">
        <form  id="formNuevoTipDoc">
            <fieldset id="fieldsetDatosTipDoc" name="fieldsetDatosTipDoc" style="width: 99%; margin-top:40px">
                <legend class="legendAzul"><%=meLanbide68I18n.getMensaje(idiomaUsuario,"legend.TipDocDokusi.datosTipDoc")%></legend>

                <div class="lineaFormulario">                    
                    <div style="width: auto; float: left; margin: 10px;">
                        <div style="padding: 10px;">
                            <label class="etiqueta" style="text-align: left; position: relative; margin-left: 5px; margin-right: 25px;"><%=meLanbide68I18n.getMensaje(idiomaUsuario,"label.TipDocDokusi.datosTipDoc.TipDocLanbide")%></label>
                            <!-- combo de Tipos documentales -->
                            <input type="text" name="codListaTipDoc" id="codListaTipDoc" maxlength="5" size="3" class="inputTexto" value="" />
                            <input type="text" name="descListaTipDoc" id="descListaTipDoc" size="125" class="inputTexto" readonly="true" value="" /> 
                            <a href="" id="anchorListaTipDoc" name="anchorListaTipDoc">
                                <img src="<%=request.getContextPath()%>/images/listas/botondesplegable.gif" id="botonTipDoc"
                                     name="botonTipDoc" height="14" width="14" border="0" style="cursor:hand;">
                            </a>
                        </div>
                    </div>
                </div>                                       
                <div class="lineaFormulario">                    
                    <div style="width: auto; float: left; margin: 10px;">
                        <div style="padding: 10px;">
                            <label class="etiqueta" style="text-align: left; position: relative; margin-left: 5px; margin-right: 25px;"><%=meLanbide68I18n.getMensaje(idiomaUsuario,"label.TipDocDokusi.datosTipDoc.TipDocDokusi")%></label>
                            <!-- combo de Tipos documentales Dokusi -->
                            <input type="text" name="codListaTipDokusi" id="codListaTipDokusi" maxlength="50" size="45" class="inputTextoMinus" value="" />
                            <input type="text" name="descListaTipDokusi" id="descListaTipDokusi" size="83" class="inputTexto" readonly="true" value="" /> 
                            <a href="" id="anchorListaTipDokusi" name="anchorListaTipDokusi">
                                <img src="<%=request.getContextPath()%>/images/listas/botondesplegable.gif" id="botonTipDokusi"
                                     name="botonTipDokusi" height="14" width="14" border="0" style="cursor:hand;">
                            </a>
                        </div>
                    </div>
                </div>  

            </fieldset>               

            <div class="botonera" style="padding: 20px">
                <input type="button" id="btnGuardarTipDoc" name="btnGuardarTipDoc" class="botonGeneral" value="<%=meLanbide68I18n.getMensaje(idiomaUsuario,"btn.aceptar")%>" onclick="guardar();">
                <input type="button" id="btnCancelarTipDoc" name="btnCancelarTipDoc" class="botonGeneral" value="<%=meLanbide68I18n.getMensaje(idiomaUsuario,"btn.cancelar")%>" onclick="cancelar();">
                <input type="button" id="btnCerrar" name="btnCerrar" class="botonGeneral" value="<%=meLanbide68I18n.getMensaje(idiomaUsuario,"btn.cerrar")%>" onclick="cerrarVentana();" style="display: none;">
            </div>  
        </form>
            
        <script type="text/javascript">

            var mensajeValidacion = '';

            var listaCodigosTipDoc = new Array();
            var listaDescripcionesTipDoc = new Array();

            listaCodigosTipDoc[0] = "";
            listaDescripcionesTipDoc[0] = "";

            contador = 0;

            <logic:iterate id="numTipdoc" name="listaTipDoc" scope="request">
                listaCodigosTipDoc[contador] = ['<bean:write name="numTipdoc" property="codTipDoc" />'];
                listaDescripcionesTipDoc[contador] = '<bean:write name="numTipdoc" property="descTipDoc" />';
                contador++;
            </logic:iterate>                

            var comboListaTipDoc = new Combo("ListaTipDoc");
            comboListaTipDoc.addItems(listaCodigosTipDoc, listaDescripcionesTipDoc);          

            var listaCodigosTipDokusi = new Array();
            var listaDescripcionesTipDokusi = new Array();

            listaCodigosTipDokusi[0] = "";
            listaDescripcionesTipDokusi[0] = "";
            
            contador = 0;
            
            <logic:iterate id="numTipdokusi" name="listaTipDokusi" scope="request">
                listaCodigosTipDokusi[contador] = ['<bean:write name="numTipdokusi" property="codDokusi" />'];
                listaDescripcionesTipDokusi[contador] = '<bean:write name="numTipdokusi" property="desDokusi" />';
                contador++;
            </logic:iterate>

            var comboListaTipDokusi = new Combo("ListaTipDokusi");
            comboListaTipDokusi.addItems(listaCodigosTipDokusi, listaDescripcionesTipDokusi);                

            if(<%=codTipDoc%> != null){
                pintarDatos();

                document.getElementById("codListaTipDoc").disabled=true;
                document.getElementById("anchorListaTipDoc").style.display="none";
                //document.getElementById("tipDocDokusi").focus(); 
                document.getElementById("codListaTipDokusi").focus();
            }
          //  document.getElementById("descListaTipDoc").disabled=true;
          //  document.getElementById("descListaTipDokusi").disabled=true;

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
                document.getElementById('codListaTipDoc').value = '<%=request.getParameter("codTipDoc") != null ? request.getParameter("codTipDoc") : "" %>';
                document.getElementById('descListaTipDoc').value = '<%=request.getParameter("tipDocLanbide") != null ? request.getParameter("tipDocLanbide").toUpperCase() : "" %>';                    
                document.getElementById('codListaTipDokusi').value = '<%=request.getParameter("tipDocDokusi") != null ? request.getParameter("tipDocDokusi") : "" %>';
                document.getElementById('descListaTipDokusi').value = '<%=request.getParameter("tipDokusi") != null ? request.getParameter("tipDokusi").toUpperCase() : "" %>';    
                resizeForFF();
            }

            function resizeForFF(){
                if(navigator.appName.indexOf("Internet Explorer")==-1){
                    document.getElementById('cuerpoNuevoTipDoc').style.width = '99%';
                    document.getElementById('divCuerpo').style.width = '98%';
                    document.getElementById('fieldsetDatosTipDoc').style.width = '98%';
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
                    
                    if(<%=codTipDoc%> == null) {
                        parametros = 'tarea=preparar&modulo=MELANBIDE68&operacion=guardarTipDocDokusi&tipo=0'
                            +'&codTipDoc='+document.getElementById('codListaTipDoc').value
                            +'&tipDocDokusi='+escape(document.getElementById('codListaTipDokusi').value);
                    }else{
                        parametros = 'tarea=preparar&modulo=MELANBIDE68&operacion=modificarTipDocDokusi&tipo=0'
                            +'&codTipDoc='+document.getElementById('codListaTipDoc').value
                            +'&tipDocDokusi='+escape(document.getElementById('codListaTipDokusi').value);
                        ;
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
                        var listaTipDocDokusi = new Array();
                        var fila = new Array();
                        var nodoFila;
                        var hijosFila;
                        var nodoCampo;
                        var j;

                        for(j=0;hijos!=null && j<hijos.length;j++){
                            if(hijos[j].nodeName=="CODIGO_OPERACION"){                            
                                codigoOperacion = hijos[j].childNodes[0].nodeValue;
                                listaTipDocDokusi[j] = codigoOperacion;
                            }else if(hijos[j].nodeName=="FILA"){
                                nodoFila = hijos[j];
                                hijosFila = nodoFila.childNodes;
                                for(var cont = 0; cont < hijosFila.length; cont++){
                                     if(hijosFila[cont].nodeName=="COD_TIPDOC"){
                                        nodoCampo = hijosFila[cont];
                                        if(nodoCampo.childNodes.length > 0){
                                            fila[0] = nodoCampo.childNodes[0].nodeValue;
                                        }
                                        else{
                                            fila[0] = '-';
                                        }
                                    }
                                    if(hijosFila[cont].nodeName=="TIPDOC_LANBIDE"){
                                        nodoCampo = hijosFila[cont];
                                        if(nodoCampo.childNodes.length > 0){
                                            fila[1] = nodoCampo.childNodes[0].nodeValue;
                                        }
                                        else{
                                            fila[1] = '-';
                                        }
                                    }
                                    if(hijosFila[cont].nodeName=="TIPDOC_DOKUSI"){
                                        nodoCampo = hijosFila[cont];
                                        if(nodoCampo.childNodes.length > 0){
                                            fila[2] = nodoCampo.childNodes[0].nodeValue;
                                        }
                                        else{
                                            fila[2] = '-';
                                        }
                                    }
                                }
                                listaTipDocDokusi[j] = fila;
                                fila = new Array();
                            }   
                        }
                        if(codigoOperacion=="0"){
                            self.parent.opener.retornoXanelaAuxiliar(listaTipDocDokusi);
                            if(<%=codTipDoc%> == null) { 
                                jsp_alerta("A",'<%=meLanbide68I18n.getMensaje(idiomaUsuario,"msg.tipDocGuardadoOK")%>');
                            }
                            else{
                                jsp_alerta("A",'<%=meLanbide68I18n.getMensaje(idiomaUsuario,"msg.tipDocModificadoOK")%>');
                            }
                            cerrarVentana();
                        }else if(codigoOperacion=="1"){
                            jsp_alerta("A",'<%=meLanbide68I18n.getMensaje(idiomaUsuario,"error.errorBD")%>');
                        }else if(codigoOperacion=="2"){
                            jsp_alerta("A",'<%=meLanbide68I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                        }else if(codigoOperacion=="3"){
                            jsp_alerta("A",'<%=meLanbide68I18n.getMensaje(idiomaUsuario,"error.pasoParametros")%>');
                        }else if(codigoOperacion=="4"){
                            jsp_alerta("A",'<%=meLanbide68I18n.getMensaje(idiomaUsuario,"error.tipDocDuplicado")%>');
                        }else if(codigoOperacion=="5"){
                            jsp_alerta("A",'<%=meLanbide68I18n.getMensaje(idiomaUsuario,"error.noExisteTipDoc")%>');
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
                    if(!validarDatosTipDoc()){
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

            function validarDatosTipDoc(){
                var correcto = true;
                //  Código de Tipo documental
                var cTipDoc = document.getElementById('codListaTipDoc').value;
                if(cTipDoc != null && cTipDoc != ''){
                    if(!existeCodigoCombo(cTipDoc, listaCodigosTipDoc)){            
                        correcto = false;
                        document.getElementById('codListaTipDoc').style.border = '1px solid red';
                        document.getElementById('descListaTipDoc').style.border = '1px solid red';
                        if(mensajeValidacion == ''){
                            mensajeValidacion = '<%=meLanbide68I18n.getMensaje(idiomaUsuario, "msg.tipDoc.datosTipDoc.codTipDocNoExiste")%>';
                        }
                    }else{
                        document.getElementById('codListaTipDoc').removeAttribute("style");
                        document.getElementById('descListaTipDoc').removeAttribute("style");
                    }
                }else{
                       mensajeValidacion = '<%=meLanbide68I18n.getMensaje(idiomaUsuario, "msg.tipDoc.datosTipDoc.codTipDocVacio")%>';
                       correcto = false;
               }
            
                if(correcto){
                //tipo documental DOKUSI
                    var cTipDokusi = document.getElementById('codListaTipDokusi').value;
                    if(cTipDokusi != null && cTipDokusi != ''){
                        if(!existeCodigoCombo(cTipDokusi, listaCodigosTipDokusi)){            
                            correcto = false;
                            document.getElementById('codListaTipDokusi').style.border = '1px solid red';
                            document.getElementById('descListaTipDokusi').style.border = '1px solid red';
                            if(mensajeValidacion == ''){
                                mensajeValidacion = '<%=meLanbide68I18n.getMensaje(idiomaUsuario, "msg.tipDokusi.datosDokusi.codDokusiNoExiste")%>';
                            }
                        }else{
                            document.getElementById('codListaTipDokusi').removeAttribute("style");
                            document.getElementById('descListaTipDokusi').removeAttribute("style");
                        }
                    }else{
                           mensajeValidacion = '<%=meLanbide68I18n.getMensaje(idiomaUsuario, "msg.tipDoc.datosTipDoc.tipDocDokusiVacio")%>';
                        correcto = false;
                    }                    
                }
                
                return correcto;
            }
            
            function existeCodigoCombo(seleccionado, listaCodigos){
                if(seleccionado != undefined && seleccionado != null && listaCodigos != undefined && listaCodigos != null){
                    if(validarNumerico(seleccionado)){
                        var encontrado = false;
                        var i = 0;
                        while(!encontrado && i < listaCodigos.length){
                            if(listaCodigos[i] == seleccionado){
                                encontrado = true;
                            }else{
                                i++;
                            }
                        }
                        return encontrado;
                    }else{
                        return true;
                    }
                }else{
                    return true;
                }
            }
            
        </script>
        </div>
    </body>
</html>

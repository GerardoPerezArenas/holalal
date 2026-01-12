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

            String codProc     = request.getParameter("codProc");
            
            String tituloPagina = "";
            tituloPagina = meLanbide68I18n.getMensaje(idiomaUsuario, "label.TipDocDokusi.titulo.nuevoTipDoc");
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
    <body id="cuerpoNuevoTipDoc" style="text-align: left; margin-top: 2vh; height: 50vh; display: flex; flex-direction: column; justify-content: space-between;">
    <div id="divCuerpo" class="contenidoPantalla" style="overflow-y: hidden; padding: 2vh; margin-top: 2vh; height: 80%; display: flex; flex-direction: column; justify-content: space-between;">

        <form id="formNuevoTipDoc" style="flex-grow: 1; display: flex; flex-direction: column; justify-content: space-between;">
            <fieldset id="fieldsetDatosTipDoc" name="fieldsetDatosTipDoc" style="width: 95%; margin: 0 auto; padding: 2vh;">
                <legend class="legendAzul">
                    <%=meLanbide68I18n.getMensaje(idiomaUsuario,"legend.TipDocLanbide.popupProcedimientos")%>
                </legend>
                    <div>
                    <div style="width: auto; float: left; margin: 10px;">
                        <div style="padding: 10px;">
                            <label class="etiqueta" style="text-align: left; position: relative; margin-left: 5px; margin-right: 25px;"><%=meLanbide68I18n.getMensaje(idiomaUsuario,"label.TipDocDokusi.datosTipDoc.TipDocLanbide")%></label>
                            <!-- combo de Tipos documentales -->
                            <input type="text" name="codListaTipDoc" id="codListaTipDoc" maxlength="5" size="5" class="inputTexto" value="" />
                            <input type="text" name="descListaTipDoc" id="descListaTipDoc" size="125" class="inputTexto" readonly="true" value="" /> 
                            <a href="" id="anchorListaTipDoc" name="anchorListaTipDoc">
                                <img src="<%=request.getContextPath()%>/images/listas/botondesplegable.gif" id="botonTipDoc"
                                     name="botonTipDoc" height="14" width="14" border="0" style="cursor:hand;">
                            </a>
                        </div>
                    </div>
                </div>                                       
            </fieldset>               
                <br>
                <div class="botonera" style="padding: 20px; margin-bottom: 20px">
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
            listaCodigosTipDoc[contador] = ['<bean:write name="numTipdoc" property="codTipDocBBDD" />'];
                listaDescripcionesTipDoc[contador] = '<bean:write name="numTipdoc" property="descTipDoc" />';
                contador++;
            </logic:iterate>                

            var comboListaTipDoc = new Combo("ListaTipDoc");
            comboListaTipDoc.addItems(listaCodigosTipDoc, listaDescripcionesTipDoc);                 

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
                    
                    parametros = 'tarea=preparar&modulo=MELANBIDE68&operacion=guardarTipDocProc&tipo=0&codProc=<%=codProc%>'
                                +'&codTipDoc='+document.getElementById('codListaTipDoc').value;
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
                        var listaTipDocProc = new Array();
                        var fila = new Array();
                        var nodoFila;
                        var hijosFila;
                        var nodoCampo;
                        var j;

                        for(j=0;hijos!=null && j<hijos.length;j++){
                            if(hijos[j].nodeName=="CODIGO_OPERACION"){                            
                                codigoOperacion = hijos[j].childNodes[0].nodeValue;
                                listaTipDocProc[j] = codigoOperacion;
                            }else if(hijos[j].nodeName=="FILA"){
                                nodoFila = hijos[j];
                                hijosFila = nodoFila.childNodes;
                                for(var cont = 0; cont < hijosFila.length; cont++){
                                        if (hijosFila[cont].nodeName == "COD_TIPDOC") {
                                          if (hijosFila[cont].childNodes.length > 0) {
                                              nodoCampo = hijosFila[cont];
                                              if(nodoCampo.childNodes.length > 0){
                                                  fila[0] = nodoCampo.childNodes[0].nodeValue;
                                                } else {
                                                  fila[0] = '-';
                                              }
                                          }
                                        }
                                        if (hijosFila[cont].nodeName == "TIPDOC_ES") {
                                          if (hijosFila[cont].childNodes.length > 0) {
                                              nodoCampo = hijosFila[cont];
                                              if(nodoCampo.childNodes.length > 0){
                                                  fila[1] = nodoCampo.childNodes[0].nodeValue;
                                                } else {
                                                  fila[1] = '-';
                                              }
                                          }
                                        }
                                        if (hijosFila[cont].nodeName == "TIPDOC_EU") {
                                           if (hijosFila[cont].childNodes.length > 0) {
                                               nodoCampo = hijosFila[cont];
                                               if(nodoCampo.childNodes.length > 0){
                                                   fila[2] = nodoCampo.childNodes[0].nodeValue;
                                                } else {
                                                   fila[2] = '-';
                                               }
                                           }
                                        }  
                                        if (hijosFila[cont].nodeName == "TIPDOC_DOKUSI") {
                                            if (hijosFila[cont].childNodes.length > 0) {
                                                nodoCampo = hijosFila[cont];
                                                if (nodoCampo.childNodes.length > 0) {
                                                    fila[3] = nodoCampo.childNodes[0].nodeValue;
                                                } else {
                                                    fila[3] = '-';
                                                }
                                            }
                                        }
                                }
                                listaTipDocProc[j] = fila;
                                fila = new Array();
                            }   
                        }
                        if(codigoOperacion=="0"){
                            self.parent.opener.retornoXanelaAuxiliar(listaTipDocProc);
                            jsp_alerta("A",'<%=meLanbide68I18n.getMensaje(idiomaUsuario,"msg.tipDocGuardadoOK")%>');
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
                        } catch (Err) {
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
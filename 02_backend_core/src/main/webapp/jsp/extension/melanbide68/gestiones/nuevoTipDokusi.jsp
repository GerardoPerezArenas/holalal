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

            String codDokusi = request.getParameter("codDokusi");
            String codDokusiPadre = request.getParameter("codDokusiPadre");
            String familiaDokusi = request.getParameter("familiaDokusi");
            String tipDokusiCas = request.getParameter("tipDokusiCas");
            String tipDokusiEus = request.getParameter("tipDokusiEus");
            
            String tituloPagina = "";
            if(codDokusi != null) {
                tituloPagina = meLanbide68I18n.getMensaje(idiomaUsuario,"label.TipDokusi.titulo.modifTipDokusi");
            }
            else
            {
                tituloPagina = meLanbide68I18n.getMensaje(idiomaUsuario, "label.TipDokusi.titulo.nuevoTipDokusi");
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
    <body id="cuerpoNuevoTipDokusi" style="text-align: left;min-width: 1200px;width: 100%;"" > 
        <div id="divCuerpo" class="contenidoPantalla" style="overflow-y: auto; padding: 10px;">
        <form  id="formNuevoTipDokusi">
                <fieldset id="fieldsetDatosTipDokusi" name="fieldsetDatosTipDokusi" style="width: 98%; margin-top:15px">
                <legend class="legendAzul"><%=meLanbide68I18n.getMensaje(idiomaUsuario,"legend.TipDokusi.datosTipDokusi")%></legend>
  
                <div class="lineaFormulario" style="width: auto; float: left; margin: 10px;">
                    <div style="padding: 10px;">
                        <label class="etiqueta" style="text-align: center; position: relative; margin: 5px; margin-right: 75px"><%=meLanbide68I18n.getMensaje(idiomaUsuario,"label.TipDokusi.datosTipDokusi.TipDokusi")%></label>
                        <input type="text" maxlength="50" size="50" id="codDokusi" style="margin-left: 42px;"name="codDokusi" value="" class="inputTextoMinus" />
                    </div>

                    <div style="clear: both;padding: 10px;">
                        <label class="etiqueta" style="text-align: center; position: relative; margin: 5px; margin-right: 25px"><%=meLanbide68I18n.getMensaje(idiomaUsuario,"label.TipDokusi.datosTipDokusi.TipDokusiPadre")%></label>
                        <input type="text" maxlength="50" style="margin-left: 55px;" size="50" id="codDokusiPadre" name="codDokusiPadre" value="" class="inputTextoMinus"/> 
                    </div>

                        <div style="clear: both;padding: 10px;display: none;">
                        <label class="etiqueta" style="text-align: center; position: relative; margin: 5px; margin-right: 25px"><%=meLanbide68I18n.getMensaje(idiomaUsuario,"label.TipDokusi.datosTipDokusi.TipDokusiFamilia")%></label>
                        <input type="text" maxlength="2" size="2" id="familiaDokusi" name="familiaDokusi" value="" class="inputTextoMinus textoNumerico"/> 
                    </div>
                        
                    <div style="clear: both;padding: 10px;">
                        <label class="etiqueta" style="text-align: center; position: relative; margin: 5px; margin-right: 25px"><%=meLanbide68I18n.getMensaje(idiomaUsuario,"label.TipDokusi.datosTipDokusi.descTipDokusiCas")%></label>
                            <input 
                                type="text"
                                maxlength="150"
                                size="135"
                                id="tipDokusiCas"
                                name="tipDokusiCas"
                                class="inputTextoMinus"
                                value="<%= request.getParameter("tipDokusiCas") != null ? request.getParameter("tipDokusiCas") : "" %>"
                                <%
                                  // Si codDokusi NO es null Y tipDokusiCas NO es vacio, pongo readonly
                                  String paramCas = request.getParameter("tipDokusiCas");
                                  if (codDokusi != null && !"".equals(codDokusi.trim()) && paramCas != null && !"".equals(paramCas.trim())) {
                                %>
                                readonly="readonly"
                                <%
                                  }
                                %>
                                />                    </div>

                    <div style="clear: both;padding: 10px;">
                        <label class="etiqueta" style="text-align: center; position: relative; margin: 5px; margin-right: 38px"><%=meLanbide68I18n.getMensaje(idiomaUsuario,"label.TipDokusi.datosTipDokusi.descTipDokusiEus")%></label>
                            <input 
                                type="text"
                                maxlength="150"
                                size="135"
                                id="tipDokusiEus"
                                name="tipDokusiEus"
                                class="inputTextoMinus"
                                value="<%= request.getParameter("tipDokusiEus") != null ? request.getParameter("tipDokusiEus") : "" %>"
                                <%
                                  // Si codDokusi NO es null Y tipDokusiEus NO es vacio, pongo readonly
                                  String paramEus = request.getParameter("tipDokusiEus");
                                  if (codDokusi != null && !"".equals(codDokusi.trim()) && paramEus != null && !"".equals(paramEus.trim())) {
                                %>
                                readonly="readonly"
                                <%
                                  }
                                %>
                                />         

                    </div>
                </div>
            </fieldset>               

            <div class="botonera" style="padding: 20px">
                <input type="button" id="btnGuardarTipDokusi" name="btnGuardarTipDokusi" class="botonGeneral" value="<%=meLanbide68I18n.getMensaje(idiomaUsuario,"btn.aceptar")%>" onclick="guardar();">
                <input type="button" id="btnCancelarTipDokusi" name="btnCancelarTipDokusi" class="botonGeneral" value="<%=meLanbide68I18n.getMensaje(idiomaUsuario,"btn.cancelar")%>" onclick="cancelar();">
                <input type="button" id="btnCerrar" name="btnCerrar" class="botonGeneral" value="<%=meLanbide68I18n.getMensaje(idiomaUsuario,"btn.cerrar")%>" onclick="cerrarVentana();" style="display: none;">
            </div>  
        </form>
             
        <script type="text/javascript">

            var mensajeValidacion = '';

            if('<%=codDokusi%>' != 'null'){             
                pintarDatos();

                document.getElementById("codDokusi").disabled=true;
                document.getElementById("codDokusiPadre").focus(); 
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
                document.getElementById('codDokusi').value = '<%=request.getParameter("codDokusi") != null ? request.getParameter("codDokusi") : "" %>';
                document.getElementById('codDokusiPadre').value = '<%=request.getParameter("codDokusiPadre") != null ? request.getParameter("codDokusiPadre") : "" %>';
                document.getElementById('familiaDokusi').value = '<%=request.getParameter("familiaDokusi") != null ? request.getParameter("familiaDokusi") : "" %>';                
                document.getElementById('tipDokusiCas').value = '<%=request.getParameter("tipDokusiCas") != null ? request.getParameter("tipDokusiCas") : "" %>';
                document.getElementById('tipDokusiEus').value = '<%=request.getParameter("tipDokusiEus") != null ? request.getParameter("tipDokusiEus") : "" %>';     
                resizeForFF();
            }

            function resizeForFF(){
                if(navigator.appName.indexOf("Internet Explorer")==-1){
                    document.getElementById('cuerpoNuevoTipDokusi').style.width = '99%';
                    document.getElementById('divCuerpo').style.width = '98%';
                    document.getElementById('fieldsetDatosTipDokusi').style.width = '98%';
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
                    
                    if('<%=codDokusi%>' == 'null') {
                        parametros = 'tarea=preparar&modulo=MELANBIDE68&operacion=guardarTipDokusi&tipo=0'
                                    + '&tipDokusiEus=' + escape(document.getElementById('tipDokusiEus').value)
                                    + '&tipDokusiCas=' + escape(document.getElementById('tipDokusiCas').value)
                            +'&codDokusi='+escape(document.getElementById('codDokusi').value)
                            +'&familiaDokusi='+document.getElementById('familiaDokusi').value
                                    + '&codDokusiPadre=' + escape(document.getElementById('codDokusiPadre').value);
                    }else{
                        parametros = 'tarea=preparar&modulo=MELANBIDE68&operacion=modificarTipDokusi&tipo=0'
                                    + '&tipDokusiEus=' + escape(document.getElementById('tipDokusiEus').value)
                                    + '&tipDokusiCas=' + escape(document.getElementById('tipDokusiCas').value)
                            +'&codDokusi='+escape(document.getElementById('codDokusi').value)
                            +'&familiaDokusi='+document.getElementById('familiaDokusi').value
                                    + '&codDokusiPadre=' + escape(document.getElementById('codDokusiPadre').value);
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
                        var listaTipDokusi = new Array();
                        var fila = new Array();
                        var nodoFila;
                        var hijosFila;
                        var nodoCampo;
                        var j;

                        for(j=0;hijos!=null && j<hijos.length;j++){
                            if(hijos[j].nodeName=="CODIGO_OPERACION"){                            
                                codigoOperacion = hijos[j].childNodes[0].nodeValue;
                                listaTipDokusi[j] = codigoOperacion;
                            }else if(hijos[j].nodeName=="FILA"){
                                nodoFila = hijos[j];
                                hijosFila = nodoFila.childNodes;
                                for(var cont = 0; cont < hijosFila.length; cont++){
                                if(hijosFila[cont].nodeName=="COD_TIPDOC"){
                                        if (hijosFila[cont].childNodes[0].nodeValue != "null") {
                                            fila[0] = hijosFila[cont].childNodes[0].nodeValue;
                                        }
                                        else {
                                            fila[0] = '';
                                        }
                                    }
                               else if(hijosFila[cont].nodeName=="COD_DOKUSI"){
                                        if (hijosFila[cont].childNodes[0].nodeValue != "null") {
                                        fila[3] = hijosFila[cont].childNodes[0].nodeValue;
                                        }
                                        else {
                                        fila[3] = '';
                                    } 
                                }
                               else if(hijosFila[cont].nodeName=="TIPDOC_DOKUSI_PADRE"){
                                    if (hijosFila[cont].childNodes[0].nodeValue != "null") {
                                        fila[4] = hijosFila[cont].childNodes[0].nodeValue;
                                    }
                                    else {
                                        fila[4] = '';
                                        }
                                    }
                                    
                                    else if(hijosFila[cont].nodeName=="DOKUSI_FAMILIA"){
                                        if (hijosFila[cont].childNodes[0].nodeValue != "null") {
                                        fila[5] = hijosFila[cont].childNodes[0].nodeValue;
                                        }
                                        else {
                                        fila[5] = '';
                                        }  
                                    }
                                    else if(hijosFila[cont].nodeName=="DOKUSI_ES"){
                                        if (hijosFila[cont].childNodes[0].nodeValue != "null") {
                                        fila[2] = hijosFila[cont].childNodes[0].nodeValue;
                                        }
                                        else {
                                        fila[2] = '';
                                        }
                                    }
                                    else if(hijosFila[cont].nodeName=="DOKUSI_EU"){
                                        if (hijosFila[cont].childNodes[0].nodeValue != "null") {
                                        fila[1] = hijosFila[cont].childNodes[0].nodeValue;
                                        }
                                        else {
                                        fila[1] = '';
                                        }
                                    }                                    
                                }
                                listaTipDokusi[j] = fila;
                                fila = new Array();
                            }   
                        }
                        if(codigoOperacion=="0"){
                            self.parent.opener.retornoXanelaAuxiliar(listaTipDokusi);
                            if('<%=codDokusi%>' == 'null') { 
                                jsp_alerta("A",'<%=meLanbide68I18n.getMensaje(idiomaUsuario,"msg.tipDocGuardadoOK")%>');
                                } else {
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
                    if(!validarDatosTipDokusi()){
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

            function validarDatosTipDokusi(){
                var correcto = true;
                // Código de Tipo documental Dokusi
                var cTipDokusi = document.getElementById('codDokusi').value;
                if(cTipDokusi == null || cTipDokusi == ""){
                    mensajeValidacion = '<%=meLanbide68I18n.getMensaje(idiomaUsuario, "msg.tipDokusi.datosdokusi.tipDokusiVacio")%>';
                    correcto = false;
                }
                    
                // Descripción en castellano del Tipo Dokusi
                if(correcto){
                    var ctipDokusiCas = document.getElementById('tipDokusiCas').value;
                    if(ctipDokusiCas == null || ctipDokusiCas == ''){
                        mensajeValidacion = '<%=meLanbide68I18n.getMensaje(idiomaUsuario, "msg.tipDokusi.datosDokusi.DesCasVacio")%>';
                        correcto = false;
                    }
                }
                //
                // Familia
                if(correcto){
                    var familia = document.getElementById('familiaDokusi').value;
                    //if(familia != null && familia != ''){
                    familia = lTrim(familia);
                    familia = rTrim(familia);                    
                    if(familia != null){                                   
                        if(!validarNumerico(familia)){
                            if(mensajeValidacion == ''){
                                mensajeValidacion = '<%=meLanbide68I18n.getMensaje(idiomaUsuario, "msg.tipDokusi.datosDokusi.FamiliaNoNumerico")%>';                            
                            }
                            document.getElementById('familiaDokusi').style.border = '1px solid red';
                            correcto = false;
                        }else{
                            document.getElementById('familiaDokusi').removeAttribute("style");
                        }
                    }
                }
                                          
                return correcto;
            }

        </script>
        </div>
    </body>
</html>
    
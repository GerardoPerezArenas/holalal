<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>

<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide43.i18n.MeLanbide43I18n" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide43.vo.FilaFaseVO" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide43.util.ConstantesMeLanbide43" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide43.vo.SelectItem"%>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide43.vo.SelectItemFases"%>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.agora.business.util.GeneralValueObject"%>
<%@page import="es.altia.common.service.config.Config"%>
<%@page import="es.altia.common.service.config.ConfigServiceHelper"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Date"%>
<%@page import="java.util.Vector"%>
<%@page import="org.apache.commons.lang.StringEscapeUtils"%>
<!doctype html>
<html>
    <head> 
	<meta http-equiv="X-UA-Compatible" content="IE=edge" />
        <%
            int idiomaUsuario = 1;
            int apl = 5;
            String css = "";
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
            //Clase para internacionalizar los mensajes de la aplicacion.
            MeLanbide43I18n meLanbide43I18n = MeLanbide43I18n.getInstance();

            Config m_Config = ConfigServiceHelper.getConfig("common");
            String statusBar = m_Config.getString("JSP.StatusBar");
            String codProc     = request.getParameter("codProc");
            String codTram     = request.getParameter("codTram");
            String descTram    = request.getParameter("descTram");
            String codFase     = request.getParameter("codFase");
            String descFaseCas = request.getParameter("descFaseCas");
            String descFaseEus = request.getParameter("descFaseEus");
            
            System.out.println("codTram: " + codTram);
            String tituloPagina = "";

            if(codTram != null) {
                tituloPagina = meLanbide43I18n.getMensaje(idiomaUsuario,"label.fase.titulo.modifFase");
            }
            else
            {
                tituloPagina = meLanbide43I18n.getMensaje(idiomaUsuario, "label.fase.titulo.nuevaFase");
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
<link rel="StyleSheet" media="screen" type="text/css" href="<%=request.getContextPath()%><%=css%>">
<link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/css/font-awesome.min.css'/>" media="screen">
<link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/less/animated.less'/>" media="screen">
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/extension/melanbide43/melanbide43.css"/>

<script type="text/javascript" src="<c:url value='/scripts/general.js'/>"></script>
<script type="text/javascript">
    var APP_CONTEXT_PATH = '<%=request.getContextPath()%>';
</script>
<script type="text/javascript" src="<c:url value='/scripts/calendario.js'/>"></script>
<script type="text/javascript" src="<c:url value='/scripts/validaciones.js'/>"></script>
<script type="text/javascript" src="<c:url value='/scripts/listaComboBox.js'/>"></script>
<script type="text/javascript" src="<c:url value='/scripts/popup.js'/>"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/melanbide43/ecaUtils.js"></script>
    </head>
    <body id="cuerpoNuevaFase" style="text-align: left;" >
        <div id="divCuerpo" class="contenidoPantalla" style="overflow-y: auto; padding: 10px;">
        <form  id="formNuevaFase">
            <fieldset id="fieldsetDatosFase" name="fieldsetDatosFase" style="width: 99%;">
                <legend class="legendAzul"><%=meLanbide43I18n.getMensaje(idiomaUsuario,"legend.fase.datosFase")%></legend>

                <div class="lineaFormulario">                    
                    <div style="width: auto; float: left; margin: 10px;">
                        <div>
                            <label class="etiqueta" style="text-align: left; position: relative; margin-left: 5px; margin-right: 35px;"><%=meLanbide43I18n.getMensaje(idiomaUsuario,"label.Tramites")%></label>
                            <!-- combo de tramites -->
                            <input type="text" name="codListaTramite" id="codListaTramite" size="10" class="inputTexto" value="" onkeyup="xAMayusculas(this);" />
                            <input type="text" name="descListaTramite" id="descListaTramite" size="130" class="inputTexto" readonly="true" value="" />
                            <a href="" id="anchorListaTramite" name="anchorListaTramite">
                                <img src="<%=request.getContextPath()%>/images/listas/botondesplegable.gif" id="botonTramite"
                                     name="botonTramite" height="14" width="14" border="0" style="cursor:hand;">
                            </a>
                        </div>
                    </div>
                </div>     

                <div class="lineaFormulario" style="width: auto; float: left; margin: 10px;">
                    <div>
                        <label class="etiqueta" style="text-align: center; position: relative; margin: 5px; margin-right: 75px"><%=meLanbide43I18n.getMensaje(idiomaUsuario,"label.fase.datosFase.fase")%></label>
                        <input type="text" maxlength="4" size="5" id="codFase" name="codFase" value="" class="inputTexto textoNumerico" />
                    </div>

                    <div style="clear: both;">
                        <label class="etiqueta" style="text-align: center; position: relative; margin: 5px; margin-right: 25px"><%=meLanbide43I18n.getMensaje(idiomaUsuario,"label.fase.datosFase.descFaseCas")%></label>
                        <input type="text" maxlength="200" size="100" id="descFaseCas" name="descFaseCas" value="" class="inputTexto"/> 
                    </div>

                    <div style="clear: both;">
                        <label class="etiqueta" style="text-align: center; position: relative; margin: 5px; margin-right: 40px"><%=meLanbide43I18n.getMensaje(idiomaUsuario,"label.fase.datosFase.descFaseEus")%></label>
                        <input type="text" maxlength="200" size="100" id="descFaseEus" name="descFaseEus" value="" class="inputTexto"/> 
                    </div>
                </div>
            </fieldset>               

            <div class="botonera" style="padding: 10px">
                <input type="button" id="btnGuardarFase" name="btnGuardarFase" class="botonGeneral" value="<%=meLanbide43I18n.getMensaje(idiomaUsuario,"btn.aceptar")%>" onclick="guardar();">
                <input type="button" id="btnCancelarFase" name="btnCancelarFase" class="botonGeneral" value="<%=meLanbide43I18n.getMensaje(idiomaUsuario,"btn.cancelar")%>" onclick="cancelar();">
                <input type="button" id="btnCerrar" name="btnCerrar" class="botonGeneral" value="<%=meLanbide43I18n.getMensaje(idiomaUsuario,"btn.cerrar")%>" onclick="cerrarVentana();" style="display: none;">
            </div>  
        </form>
                
        <script type="text/javascript">
            var mensajeValidacion = '';
               
            var listaCodigosTramite = new Array();
            var listaDescripcionesTramite = new Array();

            listaCodigosTramite[0] = "";
            listaDescripcionesTramite[0] = "";

            contador = 0;

            <logic:iterate id="numTram" name="listaTramite" scope="request">
                listaCodigosTramite[contador] = ['<bean:write name="numTram" property="codTramInterno" />'];
                listaDescripcionesTramite[contador] = ['<bean:write name="numTram" property="descTramite" />'];
                contador++;
            </logic:iterate>

            var comboListaTramite = new Combo("ListaTramite");
            comboListaTramite.addItems(listaCodigosTramite, listaDescripcionesTramite); 

            if(<%=codTram%> != null){
                pintarDatos();
                //deshabilitar combo, proteger campos de clave y poner el cursor en la descripcion castellano
                document.getElementById("codListaTramite").disabled=true;
                document.getElementById("descListaTramite").disabled=true;
                document.getElementById("anchorListaTramite").style.display="none";
                document.getElementById("codFase").disabled=true;
                document.getElementById("descFaseCas").focus(); 
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
                //Los textarea se inicializan directamente en la etiqueta, para evitar problemas con los saltos de linea
                document.getElementById('codListaTramite').value = '<%=request.getParameter("codTram") != null ? request.getParameter("codTram").toUpperCase() : "" %>';
                document.getElementById('descListaTramite').value = '<%=request.getParameter("descTram") != null ? request.getParameter("descTram").toUpperCase() : "" %>';
                document.getElementById('codFase').value = '<%=request.getParameter("codFase") != null ? request.getParameter("codFase").toUpperCase()  : "" %>';
                document.getElementById('descFaseCas').value = '<%=request.getParameter("descFaseCas") != null ? request.getParameter("descFaseCas").toUpperCase() : "" %>';
                document.getElementById('descFaseEus').value = '<%=request.getParameter("descFaseEus") != null ? request.getParameter("descFaseEus").toUpperCase() : "" %>';     
                resizeForFF();
            }

            function resizeForFF(){
                if(navigator.appName.indexOf("Internet Explorer")==-1){
                    document.getElementById('cuerpoNuevaFase').style.width = '99%';
                    document.getElementById('divCuerpo').style.width = '98%';
                    document.getElementById('fieldsetDatosFase').style.width = '98%';
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

            function cancelar(){
                var resultado = jsp_alerta('','<%=meLanbide43I18n.getMensaje(idiomaUsuario, "msg.preguntaCancelar")%>');
                if (resultado == 1){
                    cerrarVentana();
                }
            }

            function guardar(){
                // distinguir entre Nueva Fase o modificación  ??
                if(validarDatos()){
                    //document.getElementById('msgGuardandoDatos').style.display="inline";
                    //barraProgreso('on', 'barraProgresoNuevaFase');
                    var ajax = getXMLHttpRequest();
                    var nodos = null;
                    var CONTEXT_PATH = '<%=request.getContextPath()%>';
                    var url = CONTEXT_PATH + "/PeticionModuloIntegracion.do";
                    var parametros = "";
                    
                    if(<%=codTram%> == null) {
                        parametros = 'tarea=preparar&modulo=MELANBIDE43&operacion=guardarFase&tipo=0&codProc=<%=codProc%>'
                            +'&codTramite='+document.getElementById('codListaTramite').value
                            +'&codFase='+document.getElementById('codFase').value
                            +'&descFaseCas='+escape(document.getElementById('descFaseCas').value)
                            +'&descFaseEus='+escape(document.getElementById('descFaseEus').value);
                    }else{
                        parametros = 'tarea=preparar&modulo=MELANBIDE43&operacion=modificarFase&tipo=0&codProc=<%=codProc%>'
                            +'&codTramite='+document.getElementById('codListaTramite').value
                            +'&codFase='+document.getElementById('codFase').value
                            +'&descFaseCas='+escape(document.getElementById('descFaseCas').value)
                            +'&descFaseEus='+escape(document.getElementById('descFaseEus').value);
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
                        var listaFases = new Array();
                        var fila = new Array();
                        var nodoFila;
                        var hijosFila;
                        var nodoCampo;
                        var j;

                        for(j=0;hijos!=null && j<hijos.length;j++){
                            if(hijos[j].nodeName=="CODIGO_OPERACION"){                            
                                codigoOperacion = hijos[j].childNodes[0].nodeValue;
                                listaFases[j] = codigoOperacion;
                            }else if(hijos[j].nodeName=="FILA"){
                                nodoFila = hijos[j];
                                hijosFila = nodoFila.childNodes;
                                for(var cont = 0; cont < hijosFila.length; cont++){
                                     if(hijosFila[cont].nodeName=="COD_TRAM_EXT"){
                                        nodoCampo = hijosFila[cont];
                                        if(nodoCampo.childNodes.length > 0){
                                            fila[0] = nodoCampo.childNodes[0].nodeValue;
                                        }
                                        else{
                                            fila[0] = '-';
                                        }
                                    }
                                    if(hijosFila[cont].nodeName=="COD_TRAMITE"){
                                        nodoCampo = hijosFila[cont];
                                        if(nodoCampo.childNodes.length > 0){
                                            fila[1] = nodoCampo.childNodes[0].nodeValue;
                                        }
                                        else{
                                            fila[1] = '-';
                                        }
                                    }
                                    else if(hijosFila[cont].nodeName=="TRAMITE"){
                                        nodoCampo = hijosFila[cont];
                                        if(nodoCampo.childNodes.length > 0){
                                            fila[2] = nodoCampo.childNodes[0].nodeValue;
                                        }
                                        else{
                                            fila[2] = '-';
                                        }
                                    }
                                    else if(hijosFila[cont].nodeName=="COD_FASE"){
                                        nodoCampo = hijosFila[cont];
                                        if(nodoCampo.childNodes.length > 0){
                                            fila[3] = nodoCampo.childNodes[0].nodeValue;
                                        }
                                        else{
                                            fila[3] = '-';
                                        }
                                    }
                                    else if(hijosFila[cont].nodeName=="DESC_FASE_C"){
                                        nodoCampo = hijosFila[cont];
                                        if(nodoCampo.childNodes.length > 0){
                                            fila[4] = nodoCampo.childNodes[0].nodeValue;
                                        }
                                        else{
                                            fila[4] = '-';
                                        }
                                    }
                                    else if(hijosFila[cont].nodeName=="DESC_FASE_E"){
                                        nodoCampo = hijosFila[cont];
                                        if(nodoCampo.childNodes.length > 0){
                                            fila[5] = nodoCampo.childNodes[0].nodeValue;
                                        }
                                        else{
                                            fila[5] = '-';
                                        }
                                    } 
                                }
                                listaFases[j] = fila;
                                fila = new Array();
                            }   
                        }
                        if(codigoOperacion=="0"){
                            self.parent.opener.retornoXanelaAuxiliar(listaFases);

                            if(<%=codTram%> == null) { 
                                jsp_alerta("A",'<%=meLanbide43I18n.getMensaje(idiomaUsuario,"msg.faseGuardadoOK")%>');
                            }
                            else{
                                jsp_alerta("A",'<%=meLanbide43I18n.getMensaje(idiomaUsuario,"msg.faseModificadoOK")%>');
                            }
                            cerrarVentana();
                        }else if(codigoOperacion=="1"){
                            jsp_alerta("A",'<%=meLanbide43I18n.getMensaje(idiomaUsuario,"error.errorBD")%>');
                        }else if(codigoOperacion=="2"){
                            jsp_alerta("A",'<%=meLanbide43I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                        }else if(codigoOperacion=="3"){
                            jsp_alerta("A",'<%=meLanbide43I18n.getMensaje(idiomaUsuario,"error.pasoParametros")%>');
                        }else if(codigoOperacion=="4"){
                            jsp_alerta("A",'<%=meLanbide43I18n.getMensaje(idiomaUsuario,"error.datosNoValidos")%>');
                        }else{
                            jsp_alerta("A",'<%=meLanbide43I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                        }//if(
                    }
                    catch(Err){
                        jsp_alerta("A",'<%=meLanbide43I18n.getMensaje(idiomaUsuario,"error.errorProcesarRespuesta")%>');
                    }//try-catch
                }else{
                    jsp_alerta("A", escape(mensajeValidacion));
                }
        }

        function validarDatos(){
            mensajeValidacion = '';
            var correcto = true;
            try{
                if(!validarDatosFase()){
                    correcto = false;
                }                
            }catch(err){
                correcto = false;
                if(mensajeValidacion == ''){
                    mensajeValidacion = '<%=meLanbide43I18n.getMensaje(idiomaUsuario, "msg.errorValidarDatos")%>';
                }
            }
            return correcto;
        }

        function validarDatosFase(){
            var correcto = true;

            //  Código de Trámite
            var cTram = document.getElementById('codListaTramite').value;
            if(cTram != null && cTram != ''){
                if(!existeCodigoCombo(cTram, listaCodigosTramite)){            
                    correcto = false;
                    document.getElementById('codListaTramite').style.border = '1px solid red';
                    document.getElementById('descListaTramite').style.border = '1px solid red';
                    if(mensajeValidacion == ''){
                        mensajeValidacion = '<%=meLanbide43I18n.getMensaje(idiomaUsuario, "msg.fase.datosFase.codTramiteNoExiste")%>';
                    }
                }else{
                    document.getElementById('codListaTramite').removeAttribute("style");
                    document.getElementById('descListaTramite').removeAttribute("style");
                }
            }else{
                   mensajeValidacion = '<%=meLanbide43I18n.getMensaje(idiomaUsuario, "msg.fase.datosFase.codTramiteVacio")%>';
                   correcto = false;
               }
            // Código de fase
            var cFase = document.getElementById('codFase').value;
            if(cFase != null && cFase != ''){
                if(!validarNumerico(cFase)){
                    if(mensajeValidacion == ''){
                        mensajeValidacion = '<%=meLanbide43I18n.getMensaje(idiomaUsuario, "msg.fase.datosFase.codFaseNoNumerico")%>';
                    }
                    document.getElementById('codFase').style.border = '1px solid red';
                    correcto = false;
                }else{
                    document.getElementById('codFase').removeAttribute("style");
                }
            }else{
                mensajeValidacion = '<%=meLanbide43I18n.getMensaje(idiomaUsuario, "msg.fase.datosFase.codFaseVacio")%>';
                correcto = false;
            }
            return correcto;
        }

        function existeCodigoCombo(seleccionado, listaCodigos){
            if(seleccionado != undefined && seleccionado != null && listaCodigos != undefined && listaCodigos != null){
                if(trim(seleccionado) != ''){
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
        
        function cargarCombos(){
            comboTramite.addItems(codTramI, descTramI);   
        }
        
        </script>
        </div>
    </body>
</html>

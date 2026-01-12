<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<meta http-equiv="X-UA-Compatible" content="IE=11">

<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide48.i18n.MeLanbide48I18n" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@ page import="es.altia.common.service.config.Config"%>
<%@ page import="es.altia.common.service.config.ConfigServiceHelper"%>
<%@page import="java.util.ArrayList" %>
<%@page import="java.util.List" %>
<%@page import="java.util.Iterator" %>
<%@page import="java.util.Map" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide48.vo.SelectItem"%>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide48.vo.FilaTrayEspVO"%>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide48.vo.ColecTrayEspVO"%>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide48.util.ConstantesMeLanbide48"%>
<%@page import="org.apache.commons.lang.StringEscapeUtils"%>

<%
    String sIdioma = request.getParameter("idioma");
    int idiomaUsuario = (sIdioma!=null && sIdioma!="" ? Integer.parseInt(sIdioma) : 1);
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
    //Clase para internacionalizar los mensajes de la aplicación.
    MeLanbide48I18n meLanbide48I18n = MeLanbide48I18n.getInstance();
    
    Config m_Config = ConfigServiceHelper.getConfig("common");
    String statusBar = m_Config.getString("JSP.StatusBar");
    String nombreModulo     = request.getParameter("nombreModulo");
    String codOrganizacion  = request.getParameter("codOrganizacionModulo");
    String numExpediente    = request.getParameter("numero");
    String mensajeProgreso = "";
    
    
    String tituloPagina = meLanbide48I18n.getMensaje(idiomaUsuario, "label.solicitud.colectivo.trayectoria.nuevaActividad.tituloPagina");
    ColecTrayEspVO trayecEspModif = (ColecTrayEspVO)request.getAttribute("trayecEspModif");
    String colectivo = (String)request.getAttribute("colectivo") != null ? (String)request.getAttribute("colectivo") : "";
    String modoDatos = (String)request.getAttribute("modoDatos") != null ? (String)request.getAttribute("modoDatos") : "";
    
    
    //LISTAS PARA LOS COMBOS
    //List<SelectItem> listaColectivos = new ArrayList<SelectItem>();
    List<SelectItem> listaEntidades = new ArrayList<SelectItem>();

    //String lcodColectivos = "";
    //String ldescColectivos = "";
    String lcodEntidades = "";
    String ldescEntidades = "";

    //if(request.getAttribute("listaColectivos") != null)
    //    listaColectivos = (List<SelectItem>)request.getAttribute("listaColectivos");

    if(request.getAttribute("listaEntidades") != null)
        listaEntidades = (List<SelectItem>)request.getAttribute("listaEntidades");


    // Colectivos
    /*if (listaColectivos != null && listaColectivos.size() > 0) 
    {
        int i;
        SelectItem si = null;
        for (i = 0; i < listaColectivos.size() - 1; i++) 
        {
            si = (SelectItem) listaColectivos.get(i);
            lcodColectivos += "\"" + si.getCodigo() + "\",";
            ldescColectivos += "\"" + escape(si.getDescripcion()) + "\",";
        }
        si = (SelectItem) listaColectivos.get(i);
        lcodColectivos += "\"" + si.getCodigo() + "\"";
        ldescColectivos += "\"" + escape(si.getDescripcion()) + "\"";
    }
*/

    // Entidades
    if (listaEntidades != null && listaEntidades.size() > 0) 
    {
        int i;
        SelectItem si = null;
        for (i = 0; i < listaEntidades.size() - 1; i++) 
        {
            si = (SelectItem) listaEntidades.get(i);
            lcodEntidades += "\"" + si.getCodigo() + "\",";
            ldescEntidades += "\"" + escape(si.getDescripcion()) + "\",";
        }
        si = (SelectItem) listaEntidades.get(i);
        lcodEntidades += "\"" + si.getCodigo() + "\"";
        ldescEntidades += "\"" + escape(si.getDescripcion()) + "\"";
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
<!--<script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.2/jquery.min.js"></script>-->
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/>

<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/estilo.css"/>
<link rel="stylesheet" type="text/css" href="<c:url value='/css/extension/melanbide48/melanbide48.css'/>">
<link rel="stylesheet" type="text/css" href="<c:url value='/css/table.css'/>">

<script type="text/javascript">
    var APP_CONTEXT_PATH = '<%=request.getContextPath()%>';
</script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/melanbide48/colecUtils.js"></script>

<script type="text/javascript" src="<c:url value='/scripts/popup.js'/>"></script>
<script type="text/javascript" src="<c:url value='/scripts/listaComboBox.js'/>"></script>
<script type="text/javascript" src="<c:url value='/scripts/general.js'/>"></script>
<!--<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/TablaNueva.js"></script>-->
<script type="text/javascript">                
    var mensajeValidacion;
    var repetidos = false;
    
    //var codColectivos = [< %=lcodColectivos%>];
    //var descColectivos = [< %=ldescColectivos%>];

    var codEntidades = [<%=lcodEntidades%>];
    var descEntidades = [<%=ldescEntidades%>]; 
    
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
    
    function inicio(){
        cargarCombos();
        cargarCodigosCombos();
        cargarDescripcionesCombos();
    }
    
    function cargarCombos(){
        //comboColectivo.addItems(codColectivos, descColectivos);
        comboEntidad.addItems(codEntidades, descEntidades);
    }
    
    function cargarCodigosCombos(){
        //document.getElementById('codColectivo').value = '< %=colectivo != null ? colectivo : ""%>';
        document.getElementById('codEntidad').value = '<%=trayecEspModif != null && trayecEspModif.getCodEntidad()!=null ? trayecEspModif.getCodEntidad() : ""%>';
    }
    
    function cargarDescripcionesCombos(){
        var desc = "";
        var codAct = "";
        var codigo = "";


        //Colectivo
//        codigo = "< %=colectivo != null ? colectivo : ""%>";
//        if(codigo != null && codigo != "")
//        {
//            for(var i=0; i<codColectivos.length; i++)
//            {
//                codAct = codColectivos[i];
//                if(codAct == codigo)
//                {
//                    desc = descColectivos[i];
//                }
//            }
//        }
//        document.getElementById('descColectivo').value = desc;
        // Entidad
        codigo = "<%=trayecEspModif != null && trayecEspModif.getCodEntidad()!=null? trayecEspModif.getCodEntidad() : ""%>";
        if(codigo != null && codigo != "")
        {
            for(var i=0; i<codEntidades.length; i++)
            {
                codAct = codEntidades[i];
                if(codAct == codigo)
                {
                    desc = descEntidades[i];
                }
            }
        }
        document.getElementById('descEntidad').value = desc;
    }
    
    function guardarTrayectoriaEspecial(){
        //TODO
        if(validarTrayectoria()){
            var codColectivo = '<%=colectivo%>'; //document.getElementById('codColectivo').value;
            var idTrayEsp = '<%=trayecEspModif!=null && trayecEspModif.getCodTrayEsp()!=null?trayecEspModif.getCodTrayEsp():""%>';
            var codEntidad = document.getElementById('codEntidad').value;
            var descEntidad = escape(document.getElementById('descEntidad').value);
            var nombreAdm = escape(document.getElementById('nombreAdm').value);
            var descAct = escape(document.getElementById('descAct').value);
            //var codValidada = document.getElementById('chkValidada').checked ? '1' : '0';
            //var descValidada = document.getElementById('chkValidada').checked ? '< %=meLanbide48I18n.getMensaje(idiomaUsuario,"label.si")%>' : '< %=meLanbide48I18n.getMensaje(idiomaUsuario,"label.no")%>';
                    var ajax = getXMLHttpRequest();
                    var nodos = null;
                    var CONTEXT_PATH = '<%=request.getContextPath()%>'
                    var url = CONTEXT_PATH + "/PeticionModuloIntegracion.do";
                    var parametros = "";
                    var control = new Date();
                    parametros = 'tarea=preparar&modulo=MELANBIDE48&operacion=guardarTrayectoriasEspecificas&tipo=0&modoDatos=<%=modoDatos%>&numero=<%=numExpediente%>'
                        +'&codColectivo='+codColectivo
                        +'&idTrayEsp='+idTrayEsp
                        +'&codEntidad='+codEntidad
                        +'&nombreAdm='+nombreAdm
                        +'&descAct='+descAct
                        +'&idiomaUsuario=<%=idiomaUsuario%>'
                        +'&control='+control.getTime();
                    try{
                        ajax.open("POST",url,false);
                        ajax.setRequestHeader("Content-Type","application/x-www-form-urlencoded; charset=iso-8859-15");
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
                        var listaDatosRespuesta = new Array();
                        var fila = new Array();
                        var nodoFila;
                        var hijosFila;
                        var nodoCampo;
                        var j;
                        for(j=0;hijos!=null && j<hijos.length;j++){
                            if(hijos[j].nodeName=="CODIGO_OPERACION"){                            
                                codigoOperacion = hijos[j].childNodes[0].nodeValue;
                                listaDatosRespuesta[j] = codigoOperacion;
                            }else if(hijos[j].nodeName=="TRAYECTORIAS"){
                                var nodoFila1 = hijos[j];
                                var hijosFila1 = nodoFila1.childNodes;
                                for(var cont1 = 0; cont1 < hijosFila1.length; cont1++){
                                    nodoFila = hijosFila1[cont1];
                                    hijosFila = nodoFila.childNodes;
                                    for(var cont = 0; cont < hijosFila.length; cont++){      // recorremos los nodos TRAYECTORIA
                                        if(hijosFila[cont].nodeName=="COLEC_COD_TRAY_ESP"){
                                            nodoCampo = hijosFila[cont];
                                            if(nodoCampo.childNodes.length > 0){
                                                fila[0] = nodoCampo.childNodes[0].nodeValue;
                                            }
                                            else{
                                                fila[0] = '-';
                                            }
                                        }
                                        if(hijosFila[cont].nodeName=="COLEC_COD_ENTIDAD"){
                                            nodoCampo = hijosFila[cont];
                                            if(nodoCampo.childNodes.length > 0){
                                                fila[1] = nodoCampo.childNodes[0].nodeValue;
                                            }
                                            else{
                                                fila[1] = '-';
                                            }
                                        }
                                        else if(hijosFila[cont].nodeName=="COLEC_COLECTIVO"){
                                            nodoCampo = hijosFila[cont];
                                            if(nodoCampo.childNodes.length > 0){
                                                fila[2] = nodoCampo.childNodes[0].nodeValue;
                                            }
                                            else{
                                                fila[2] = '-';
                                            }
                                        }
                                        else if(hijosFila[cont].nodeName=="COLEC_NOMBRE_ADM"){
                                            nodoCampo = hijosFila[cont];
                                            if(nodoCampo.childNodes.length > 0){
                                                fila[3] = nodoCampo.childNodes[0].nodeValue;
                                            }else {
                                                fila[3] = '-';
                                            }
                                        }else if (hijosFila[cont].nodeName == "COLEC_DESC_ACTIVIDAD") {
                                            nodoCampo = hijosFila[cont];
                                            if (nodoCampo.childNodes.length > 0) {
                                                fila[4] = nodoCampo.childNodes[0].nodeValue;
                                            }else{
                                                fila[4] = '-';
                                            }
                                        }else if (hijosFila[cont].nodeName == "COLEC_ENT_CIF") {
                                            nodoCampo = hijosFila[cont];
                                            if (nodoCampo.childNodes.length > 0) {
                                                fila[5] = nodoCampo.childNodes[0].nodeValue;
                                            }else {
                                                fila[5] = '-';
                                            }
                                        }else if (hijosFila[cont].nodeName == "COLEC_ENT_NOMBRE") {
                                            nodoCampo = hijosFila[cont];
                                            if (nodoCampo.childNodes.length > 0) {
                                                fila[6] = nodoCampo.childNodes[0].nodeValue;
                                            }else {
                                                fila[6] = '-';
                                            }
                                        }else if (hijosFila[cont].nodeName == "COLEC_VALIDADA") {
                                            nodoCampo = hijosFila[cont];
                                            if (nodoCampo.childNodes.length > 0) {
                                                fila[7] = nodoCampo.childNodes[0].nodeValue;
                                            }else {
                                                fila[7] = '-';
                                            }
                                        }else if (hijosFila[cont].nodeName == "COLEC_NUMEXP") {
                                            nodoCampo = hijosFila[cont];
                                            if (nodoCampo.childNodes.length > 0) {
                                                fila[8] = nodoCampo.childNodes[0].nodeValue;
                                            }else {
                                                fila[8] = '-';
                                            }
                                        }else if (hijosFila[cont].nodeName == "COLEC_EXP_EJE") {
                                            nodoCampo = hijosFila[cont];
                                            if (nodoCampo.childNodes.length > 0) {
                                                fila[9] = nodoCampo.childNodes[0].nodeValue;
                                            }else {
                                                fila[9] = '-';
                                            }
                                        }
                                    }
                                    listaDatosRespuesta[cont1+1] = fila;
                                    fila = new Array();
                                }
                                
                            }
                        }//for(j=0;hijos!=null && j<hijos.length;j++)
                        if (codigoOperacion == "0") {
                            self.parent.opener.retornoXanelaAuxiliar(listaDatosRespuesta);
                            jsp_alerta("A", '<%=meLanbide48I18n.getMensaje(idiomaUsuario,"msg.datosGuardadosOK")%>');
                            cerrarVentana();
                        } else if (codigoOperacion == "1") {
                            jsp_alerta("A", '<%=meLanbide48I18n.getMensaje(idiomaUsuario,"error.errorBD")%>');
                        } else if (codigoOperacion == "2") {
                            jsp_alerta("A", '<%=meLanbide48I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                        } else if (codigoOperacion == "3") {
                            jsp_alerta("A", '<%=meLanbide48I18n.getMensaje(idiomaUsuario,"error.pasoParametros")%>');
                        } else if (codigoOperacion == "4") {
                            jsp_alerta("A", '<%=meLanbide48I18n.getMensaje(idiomaUsuario,"error.datosNoValidos")%>');
                        } else {
                            jsp_alerta("A", '<%=meLanbide48I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                        }//if(
                    }
                    catch (Err) {
                        jsp_alerta("A", '<%=meLanbide48I18n.getMensaje(idiomaUsuario,"error.errorProcesarRespuesta")%>');
                    }//try-catch
        } else {
            jsp_alerta("A", escape(mensajeValidacion));
        }
        limpiarFormulario();
    }
    
    function validarTrayectoria(){
        mensajeValidacion = '';
        var codigo = '<%=colectivo%>';//document.getElementById('codColectivo').value;
        if(codigo == ''){
            mensajeValidacion = '<%=meLanbide48I18n.getMensaje(idiomaUsuario,"msg.solicitud.colectivo.nuevaActividad.camposObligatorios")%>';
            return false;
        }
        codigo = document.getElementById('codEntidad').value;
        if(codigo == ''){
            mensajeValidacion = '<%=meLanbide48I18n.getMensaje(idiomaUsuario,"msg.solicitud.colectivo.nuevaActividad.camposObligatorios")%>';
            return false;
        }
        var nombreAdm = document.getElementById('nombreAdm').value;
        if(nombreAdm == ''){
            mensajeValidacion = '<%=meLanbide48I18n.getMensaje(idiomaUsuario,"msg.solicitud.colectivo.nuevaActividad.camposObligatorios")%>';
            return false;
        }else{
            if(!comprobarCaracteresEspecialesColec(nombreAdm)){
                mensajeValidacion = '<%= meLanbide48I18n.getMensaje(idiomaUsuario,"msg.solicitud.colectivo.nuevaActividad.nombreAdmCaracteresNoPermitidos")%>';
                return false;
            }
        }

        var descAct = document.getElementById('descAct').value;
        if(descAct == ''){
            mensajeValidacion = '<%=meLanbide48I18n.getMensaje(idiomaUsuario,"msg.solicitud.colectivo.nuevaActividad.camposObligatorios")%>';
            return false;
        }else{
            if(!comprobarCaracteresEspecialesColec(descAct)){
                mensajeValidacion = '<%= meLanbide48I18n.getMensaje(idiomaUsuario,"msg.solicitud.colectivo.nuevaActividad.descActCaracteresNoPermitidos")%>';
                return false;
            }
        }
        return true;
    }
    
    function limpiarFormulario(){
        document.getElementById('codEntidad').value = '';
        document.getElementById('descEntidad').value = '';
        document.getElementById('nombreAdm').value = '';
        document.getElementById('descAct').value = '';
    }
           
    function cerrarVentana(){
        window.close();
    }
            
    function confirmarSalida(){
        return '<%=meLanbide48I18n.getMensaje(idiomaUsuario,"msg.confirmarSalida")%>';
    }
    
</script>

<!--<body class="contenidoPantalla">-->
    <!--<form  id="formTray" style="margin-top: 1px;">-->
        <div align="center" class="contenidoPantalla">
            <div style="clear: both; overflow-x: hidden; overflow-y: auto; height: 650px;">
                 <fieldset id="fieldsetCentros" name="fieldsetCentros" style="width: 935px;">
                    <legend class="legendAzul"><%=meLanbide48I18n.getMensaje(idiomaUsuario,"legend.solicitud.colectivo.trayectoria.datosActividad")%></legend>
                    <div class="lineaFormulario" style="padding-left: 5px; padding-right: 5px; text-align: left;">
                        <!-- COLECTIVO -->
<!--                        <label for="codColectivo" style="vertical-align: bottom;">< %=meLanbide48I18n.getMensaje(idiomaUsuario,"label.solicitud.colectivo.trayectoria.nuevaAct.colectivo")%></label><font color="red">*</font>
                        <input id="codColectivo" name="codColectivo" type="text" class="inputTexto" size="2" maxlength="2" style="margin-left: 5px;" 
                               onkeypress="javascript:return SoloDigitosConsulta(event);" readonly="true" disabled="true">
                        <input id="descColectivo" name="descColectivo" type="text" class="inputTexto" size="40" readonly="true" disabled="true">
                        <a id="anchorColectivo" name="anchorColectivo" href="" style="display: none;"><img src="< %=request.getContextPath()%>/images/listas/botondesplegable.gif" id="botonColectivo" name="botonColectivo" border="0" width="14" height="14" style="cursor:hand;" alt="<%=meLanbide48I18n.getMensaje(idiomaUsuario, "altDesplegable")%>" title="<%=meLanbide48I18n.getMensaje(idiomaUsuario, "altDesplegable")%>"></a>-->
                        
                        <!-- ENTIDAD -->
                        <label for="codEntidad" style="vertical-align: bottom;"><%=meLanbide48I18n.getMensaje(idiomaUsuario,"label.solicitud.colectivo.trayectoria.nuevaAct.entidad")%></label><font color="red">*</font>
                        <input id="codEntidad" name="codEntidad" type="text" class="inputTexto" size="4" maxlength="2" style="margin-left: 19px;"
                               onkeypress="javascript:return SoloDigitosConsulta(event);">
                        <input id="descEntidad" name="descEntidad" type="text" class="inputTexto" size="70" readonly>
                        <a id="anchorEntidad" name="anchorEntidad" href=""><img src="<%=request.getContextPath()%>/images/listas/botondesplegable.gif" id="botonEntidad" name="botonEntidad" border="0" width="14" height="14" style="cursor:hand;" alt="<%=meLanbide48I18n.getMensaje(idiomaUsuario, "altDesplegable")%>" title="<%=meLanbide48I18n.getMensaje(idiomaUsuario, "altDesplegable")%>"></a>
                    </div> 
                    <div class="lineaFormulario" style="padding-left: 5px; padding-right: 5px; text-align: left; padding-bottom: 2px;">
                        <!-- NOMBRE ADMINISTRACION -->
                        <label for="nombreAdm" style="vertical-align: bottom;"><%=meLanbide48I18n.getMensaje(idiomaUsuario,"label.solicitud.colectivo.trayectoria.nuevaAct.nombreAdm")%></label><font color="red">*</font>
                        <input type="text" id="nombreAdm" name="nombreAdm" class="inputTexto" size="128" maxlength="200" value="<%=trayecEspModif != null && trayecEspModif.getNombreAdm()!=null ? trayecEspModif.getNombreAdm() : ""%>"/>
                    </div>
                    <div class="lineaFormulario" style="padding-left: 5px; padding-right: 5px; text-align: left;">    
                        <!-- DESCRIPCION ACTIVIDAD -->
                        <label for="descAct" style="vertical-align: top;"><%=meLanbide48I18n.getMensaje(idiomaUsuario,"label.solicitud.colectivo.trayectoria.nuevaAct.descActividad")%></label><font color="red" style="margin-right: 44px; vertical-align: top;">*</font>
                        <textarea id="descAct" name="descAct" class="inputTexto" rows="8" cols="124" maxlength="500"><%=trayecEspModif != null && trayecEspModif.getDescActividad() !=null? trayecEspModif.getDescActividad() : ""%></textarea>
                    </div>
<!--                    <div class="lineaFormulario" style="padding-left: 5px; padding-right: 5px; text-align: left;">    
                         DESCRIPCION ACTIVIDAD 
                        <label for="descAct" style="vertical-align: top;">< %=meLanbide48I18n.getMensaje(idiomaUsuario,"label.solicitud.colectivo.trayectoria.nuevaAct.validada")%></label>
                        <input type="checkbox" id="chkValidada" name="chkValidada"/>
                    </div>-->
                    <div class="botonera">
                        <input type="button" id="btnAnadir" name="btnAnadir" class="botonGeneral" value="<%=meLanbide48I18n.getMensaje(idiomaUsuario,"btn.guardar")%>" onclick="guardarTrayectoriaEspecial()"/>
                    </div>
                 </fieldset>
                    
<!--                 <fieldset id="fieldsetCentros" name="fieldsetCentros" style="width: 935px;">
                    <legend class="legendAzul">< %=meLanbide48I18n.getMensaje(idiomaUsuario,"legend.solicitud.colectivo.trayectoria.datosActividad")%></legend>
                    <div id="listaTrayectorias" style="padding: 5px; width:930px; height: 330px; text-align: center; overflow-x: auto; overflow-y: auto;margin:0px;margin-top:0px;" align="center"></div>    
                 </fieldset>
                <div class="botonera">
                    <input type="button" id="btnGuardar" name="btnGuardar" class="botonGeneral" value="< %=meLanbide48I18n.getMensaje(idiomaUsuario,"btn.guardar")%>" onclick="guardarTrayectoria();"/>
                    <input type="button" id="btnEliminar" name="btnEliminar" class="botonGeneral" value="< %=meLanbide48I18n.getMensaje(idiomaUsuario,"btn.eliminar")%>" onclick="eliminar();"/>
                    <input type="button" id="btnEliminarTodas" name="btnEliminarTodas" class="botonLargo" value="< %=meLanbide48I18n.getMensaje(idiomaUsuario,"btn.eliminarTodas")%>" onclick="eliminarTodas();"/>
                    <input type="button" id="btnCerrar" name="btnCerrar" class="botonGeneral" value="< %=meLanbide48I18n.getMensaje(idiomaUsuario,"btn.cerrar")%>" onclick="cerrar();"/>
                </div>-->
            </div>
        </div>
    <!--</form>-->
<!--</body>-->

<script type="text/javascript">   
    //var comboColectivo = new Combo("Colectivo");
    var comboEntidad = new Combo("Entidad");
    inicio();
</script>
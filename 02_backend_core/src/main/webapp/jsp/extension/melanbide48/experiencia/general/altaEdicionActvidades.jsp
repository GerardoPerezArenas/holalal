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
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide48.vo.ColecTrayActividadVO"%>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide48.util.ConstantesMeLanbide48"%>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide48.util.MeLanbide48Utils"%>
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
    ColecTrayActividadVO trayecModif = (ColecTrayActividadVO)request.getAttribute("trayecModif");
    String modoDatos = (String)request.getAttribute("modoDatos") != null ? (String)request.getAttribute("modoDatos") : "";
    
    List<SelectItem> listaEntidades = new ArrayList<SelectItem>();

    String lcodEntidades = "";
    String ldescEntidades = "";
    
    if(request.getAttribute("listaEntidades") != null)
        listaEntidades = (List<SelectItem>)request.getAttribute("listaEntidades");

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
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/general.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/validaciones.js"></script>
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

<link rel="stylesheet" href="//code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css">
<link rel="stylesheet" href="/resources/demos/style.css">
<script src="https://code.jquery.com/jquery-1.12.4.js"></script>
<script src="https://code.jquery.com/ui/1.12.1/jquery-ui.js"></script>
<script type="text/javascript" src="js/jquery-1.4.2.min.js"></script>
<script type="text/javascript" src="js/jquery-ui-1.8.4.custom.min.js"></script>
<script type="text/javascript" src="js/jquery.ui.datepicker-es.js"></script>
<script type="text/javascript">
     // JQuery
    $.datepicker.regional['es'] = {
    closeText: 'Cerrar',
    prevText: '< Ant',
    nextText: 'Sig >',
    currentText: 'Hoy',
    monthNames: ['Enero', 'Febrero', 'Marzo', 'Abril', 'Mayo', 'Junio', 'Julio', 'Agosto', 'Septiembre', 'Octubre', 'Noviembre', 'Diciembre'],
    monthNamesShort: ['Ene','Feb','Mar','Abr', 'May','Jun','Jul','Ago','Sep', 'Oct','Nov','Dic'],
    dayNames: ['Domingo', 'Lunes', 'Martes', 'Miércoles', 'Jueves', 'Viernes', 'Sábado'],
    dayNamesShort: ['Dom','Lun','Mar','Mié','Juv','Vie','Sáb'],
    dayNamesMin: ['Do','Lu','Ma','Mi','Ju','Vi','Sá'],
    weekHeader: 'Sm',
    dateFormat: 'dd/mm/yy',
    firstDay: 1,
    isRTL: false,
    showMonthAfterYear: false,
    yearSuffix: ''
    };
    $.datepicker.setDefaults($.datepicker.regional['es']);
    $( function() {
    //$( "#fechaInicioAC" ).datepicker();
    $( "#fechaInicioAC" ).datepicker( $.datepicker.regional['es' ] );
   // $( "#fechaInicioAC" ).datepicker( "option", "dateFormat", "dd/mm/yy");
    //$( "#fechaFinAC" ).datepicker();
    $( "#fechaFinAC" ).datepicker( $.datepicker.regional[ 'es' ] );
    //$( "#fechaFinAC" ).datepicker( "option", "dateFormat", "dd/mm/yy");
  } );
</script>

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
        document.getElementById('codEntidad').value = '<%=trayecModif != null && trayecModif.getCodEntidad()!=null ? trayecModif.getCodEntidad() : ""%>';
    }
    
    function cargarDescripcionesCombos(){
        var desc = "";
        var codAct = "";
        var codigo = "";

        // Entidad
        codigo = "<%=trayecModif != null &&  trayecModif.getCodEntidad()!=null ? trayecModif.getCodEntidad() : ""%>";
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
    
    function guardarTrayectoriaGeneralActividades(){
        //TODO
        if(validarTrayectoriaGenActividades()){
            var idTray = '<%=trayecModif!=null && trayecModif.getCodIdActividad()!=null?trayecModif.getCodIdActividad():""%>';
            var codEntidad = document.getElementById('codEntidad').value;
            var descEntidad = escape(document.getElementById('descEntidad').value);
            var descActividad = escape(document.getElementById('descActividad').value);
            var fechaInicioAC = document.getElementById('fechaInicioAC').value;
            var fechaFinAC = document.getElementById('fechaFinAC').value;
                    var ajax = getXMLHttpRequest();
                    var nodos = null;
                    var CONTEXT_PATH = '<%=request.getContextPath()%>'
                    var url = CONTEXT_PATH + "/PeticionModuloIntegracion.do";
                    var parametros = "";
                    var control = new Date();
                    parametros = 'tarea=preparar&modulo=MELANBIDE48&operacion=guardarTrayectoriaGeneralActividades&tipo=0&modoDatos=<%=modoDatos%>&numero=<%=numExpediente%>'
                        +'&idTray='+idTray
                        +'&codEntidad='+codEntidad
                        +'&descActividad='+descActividad
                        +'&fechaInicio='+fechaInicioAC
                        +'&fechaFin='+fechaFinAC
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
                                        if(hijosFila[cont].nodeName=="COLEC_ACTIV_COD"){
                                            nodoCampo = hijosFila[cont];
                                            if(nodoCampo.childNodes.length > 0){
                                                fila[0] = nodoCampo.childNodes[0].nodeValue;
                                            }
                                            else{
                                                fila[0] = '-';
                                            }
                                        }
                                        if(hijosFila[cont].nodeName=="COLEC_ACTIV_TIPO"){
                                            nodoCampo = hijosFila[cont];
                                            if(nodoCampo.childNodes.length > 0){
                                                fila[1] = nodoCampo.childNodes[0].nodeValue;
                                            }
                                            else{
                                                fila[1] = '-';
                                            }
                                        }
                                        else if(hijosFila[cont].nodeName=="COLEC_ACTIV_EXP_EJE"){
                                            nodoCampo = hijosFila[cont];
                                            if(nodoCampo.childNodes.length > 0){
                                                fila[2] = nodoCampo.childNodes[0].nodeValue;
                                            }
                                            else{
                                                fila[2] = '-';
                                            }
                                        }
                                        else if(hijosFila[cont].nodeName=="COLEC_ACTIV_NUMEXP"){
                                            nodoCampo = hijosFila[cont];
                                            if(nodoCampo.childNodes.length > 0){
                                                fila[3] = nodoCampo.childNodes[0].nodeValue;
                                            }else {
                                                fila[3] = '-';
                                            }
                                        }else if (hijosFila[cont].nodeName == "COLEC_ACTIV_COD_ENTIDAD") {
                                            nodoCampo = hijosFila[cont];
                                            if (nodoCampo.childNodes.length > 0) {
                                                fila[4] = nodoCampo.childNodes[0].nodeValue;
                                            }else{
                                                fila[4] = '-';
                                            }
                                        }else if (hijosFila[cont].nodeName == "COLEC_ACTIV_DESC_SERVPUB") {
                                            nodoCampo = hijosFila[cont];
                                            if (nodoCampo.childNodes.length > 0) {
                                                fila[5] = nodoCampo.childNodes[0].nodeValue;
                                            }else {
                                                fila[5] = '-';
                                            }
                                        }else if (hijosFila[cont].nodeName == "COLEC_ACTIV_INICIO") {
                                            nodoCampo = hijosFila[cont];
                                            if (nodoCampo.childNodes.length > 0) {
                                                fila[6] = nodoCampo.childNodes[0].nodeValue;
                                            }else {
                                                fila[6] = '-';
                                            }
                                        }else if (hijosFila[cont].nodeName == "COLEC_ACTIV_FIN") {
                                            nodoCampo = hijosFila[cont];
                                            if (nodoCampo.childNodes.length > 0) {
                                                fila[7] = nodoCampo.childNodes[0].nodeValue;
                                            }else {
                                                fila[7] = '-';
                                            }
                                        }else if (hijosFila[cont].nodeName == "COLEC_ENT_CIF") {
                                            nodoCampo = hijosFila[cont];
                                            if (nodoCampo.childNodes.length > 0) {
                                                fila[8] = nodoCampo.childNodes[0].nodeValue;
                                            }else {
                                                fila[8] = '-';
                                            }
                                        }else if (hijosFila[cont].nodeName == "COLEC_ENT_NOMBRE") {
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
        //limpiarFormulario();
    }
    
    function validarTrayectoriaGenActividades(){
        mensajeValidacion = '';
        var codigo = '';
        codigo = document.getElementById('codEntidad').value;
        if(codigo == ''){
            mensajeValidacion = '<%=meLanbide48I18n.getMensaje(idiomaUsuario,"msg.generico.campos.obligatorios")%>';
            return false;
        }
        var variable = document.getElementById('descActividad').value;
        if(variable == ''){
            mensajeValidacion = '<%=meLanbide48I18n.getMensaje(idiomaUsuario,"msg.generico.campos.obligatorios")%>';
            return false;
        }else{
            if(!comprobarCaracteresEspecialesColec(variable)){
                mensajeValidacion = '<%= meLanbide48I18n.getMensaje(idiomaUsuario,"msg.generico.caracteres.noPermitidos")%>';
                return false;
            }
        }

        variable = document.getElementById('fechaInicioAC').value;
        if(variable == ''){
            mensajeValidacion = '<%=meLanbide48I18n.getMensaje(idiomaUsuario,"msg.generico.campos.obligatorios")%>';
            return false;
        }else{
            if(!comprobarFormatoFechaColec(variable)){
                mensajeValidacion = '<%= meLanbide48I18n.getMensaje(idiomaUsuario,"msg.generico.campos.formato.fecha")%>';
                document.getElementById('fechaInicioAC').focus();
                document.getElementById('fechaInicioAC').select();
                return false;
            }
        }
        variable = document.getElementById('fechaFinAC').value;
        if(variable == ''){
            mensajeValidacion = '<%=meLanbide48I18n.getMensaje(idiomaUsuario,"msg.generico.campos.obligatorios")%>';
            return false;
        }else{
            if(!comprobarFormatoFechaColec(variable)){
                mensajeValidacion = '<%= meLanbide48I18n.getMensaje(idiomaUsuario,"msg.generico.campos.formato.fecha")%>';
                document.getElementById('fechaFinAC').focus();
                document.getElementById('fechaFinAC').select();
                return false;
            }
        }
        return true;
    }
    
    function limpiarFormulario(){
        document.getElementById('codEntidad').value = '';
        document.getElementById('descEntidad').value = '';
        document.getElementById('descActividad').value = '';
        document.getElementById('fechaInicioAC').value = '';
        document.getElementById('fechaFinAC').value = '';
    }
           
    function cerrarVentana(){
        window.close();
    }
            
    function confirmarSalida(){
        return '<%=meLanbide48I18n.getMensaje(idiomaUsuario,"msg.confirmarSalida")%>';
    }
   
</script>

<body class="contenidoPantalla">
<!--    <form  id="formTrayGenAC" style="margin-top: 1px;">-->
        <div align="center">
            <div style="clear: both; overflow-x: hidden; overflow-y: auto; height: 650px;">
                 <fieldset id="fieldsetActividades" name="fieldsetActividades" style="width: 935px;">
                    <legend class="legendAzul"><%=meLanbide48I18n.getMensaje(idiomaUsuario,"label.trayectoria.general.actividades")%></legend>
                    <div class="lineaFormulario" style="padding-left: 5px; padding-right: 5px; text-align: left;">
                        <!-- ENTIDAD -->
                        <label for="codEntidad" style="vertical-align: bottom;"><%=meLanbide48I18n.getMensaje(idiomaUsuario,"label.solicitud.colectivo.trayectoria.nuevaAct.entidad")%></label><font color="red">*</font>
                        <input id="codEntidad" name="codEntidad" type="text" class="inputTexto" size="4" maxlength="2" style="margin-left: 19px;"
                               onkeypress="javascript:return SoloDigitosConsulta(event);">
                        <input id="descEntidad" name="descEntidad" type="text" class="inputTexto" size="70" readonly>
                        <a id="anchorEntidad" name="anchorEntidad" href=""><img src="<%=request.getContextPath()%>/images/listas/botondesplegable.gif" id="botonEntidad" name="botonEntidad" border="0" width="14" height="14" style="cursor:hand;" alt="<%=meLanbide48I18n.getMensaje(idiomaUsuario, "altDesplegable")%>" title="<%=meLanbide48I18n.getMensaje(idiomaUsuario, "altDesplegable")%>"></a>
                    </div> 
                    <div class="lineaFormulario" style="padding-left: 5px; padding-right: 5px; text-align: left;">    
                        <!-- ACTIVIDAD -->
                        <label for="descActividad" style="vertical-align: top;"><%=meLanbide48I18n.getMensaje(idiomaUsuario,"label.solicitud.trayectoria.general.actividades")%></label><font color="red" style="margin-right: 44px; vertical-align: top;">*</font>
                        <textarea id="descActividad" name="descActividad" class="inputTexto" rows="8" cols="124" maxlength="500"><%=trayecModif != null && trayecModif.getDesActividadyServPublEmp()!=null ? trayecModif.getDesActividadyServPublEmp() : ""%></textarea>
                    </div>
                    <div class="lineaFormulario" style="padding-left: 5px; padding-right: 5px; text-align: left;">    
                        <table>
                            <tr>
                                 <!-- FECHA INICIO -->
                                <td>
                                    <label for="fechaInicioAC" style="vertical-align: top;"><%=meLanbide48I18n.getMensaje(idiomaUsuario,"label.solicitud.trayectoria.general.otrosProgramas.fechaInicio")%></label><font color="red" style="margin-right: 44px; vertical-align: top;">*</font>
                                </td>
                                <td>
                                    <input type="text"  id="fechaInicioAC" name="fechaInicioAC" class="inputTexto" maxlength="10" style="text-align: right;" value="<%=trayecModif != null ? MeLanbide48Utils.formatearFecha_ddmmyyyy(trayecModif.getFechaInicio()): ""%>">
                                </td>
                            </tr>
                            <tr>
                                <!-- FECHA FIN -->
                                <td>
                                    <label for="fechaFinAC" style="vertical-align: top;"><%=meLanbide48I18n.getMensaje(idiomaUsuario,"label.solicitud.trayectoria.general.otrosProgramas.fechaFin")%></label><font color="red" style="margin-right: 44px; vertical-align: top;">*</font>
                                </td>
                                <td>
                                    <input type="text"  id="fechaFinAC" name="fechaFinAC" class="inputTexto" maxlength="10" style="text-align: right;" value="<%=trayecModif != null ? MeLanbide48Utils.formatearFecha_ddmmyyyy(trayecModif.getFechaFin()): ""%>">
                                </td>
                            </tr>
                        </table>
                    </div>
                    <div class="botonera">
                        <input type="button" id="btnAnadir" name="btnAnadir" class="botonGeneral" value="<%=meLanbide48I18n.getMensaje(idiomaUsuario,"btn.guardar")%>" onclick="guardarTrayectoriaGeneralActividades()"/>
                    </div>
                 </fieldset>
            </div>
        </div>
<!--    </form>-->
</body>

<script type="text/javascript">   
    //var comboColectivo = new Combo("Colectivo");
    var comboEntidad = new Combo("Entidad");
    inicio();
</script>
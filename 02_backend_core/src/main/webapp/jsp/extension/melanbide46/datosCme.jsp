<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>

<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide46.i18n.MeLanbide46I18n" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide46.vo.SelectItem"%>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide46.util.ConfigurationParameter"%>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide46.util.ConstantesMeLanbide46"%>
<%@ page import="es.altia.common.service.config.Config"%>
<%@ page import="es.altia.common.service.config.ConfigServiceHelper"%>
<%@page import="java.text.DecimalFormat" %>
<%@page import="java.util.ArrayList" %>
<%@page import="java.util.List" %>
<%@page import="org.apache.commons.lang.StringEscapeUtils"%>

<%@page import="es.altia.agora.business.util.GeneralValueObject"%>
<%@page import="es.altia.agora.interfaces.user.web.sge.FichaExpedienteForm"%>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide46.vo.datosCmeVO" %>

<%
    String sIdioma = request.getParameter("idioma");
    int idiomaUsuario = Integer.parseInt(sIdioma);
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
    //Clase para internacionalizar los mensajes de la aplicaci�n.
    MeLanbide46I18n meLanbide46I18n = MeLanbide46I18n.getInstance();
    
    Config m_Config = ConfigServiceHelper.getConfig("common");
    String statusBar = m_Config.getString("JSP.StatusBar");
    String nombreModulo     = request.getParameter("nombreModulo");
    String codOrganizacion  = request.getParameter("codOrganizacionModulo");
    String numExpediente    = request.getParameter("numero");
    String mensajeProgreso = "";
    
    String urlPestanaDatos_solicitud = (String)request.getAttribute("urlPestanaDatos_solicitud");
    String urlPestanaDatos_oferta = (String)request.getAttribute("urlPestanaDatos_oferta");
    String urlPestanaDatos_justif = (String)request.getAttribute("urlPestanaDatos_justif");
    String urlPestanaDatos_resumen = (String)request.getAttribute("urlPestanaDatos_resumen");
    
    DecimalFormat df = new DecimalFormat("#.00");
    
    
        
    boolean tramRes = false;
    boolean tramResModif = false;
    
    try
    {
        if(request.getAttribute("tramRes") != null)
        {
            tramRes = (Boolean)request.getAttribute("tramRes");
        }
        if(request.getAttribute("tramResModif") != null)
        {
            tramResModif = (Boolean)request.getAttribute("tramResModif");
        }
    }
    catch(Exception ex)
    {
        
    }
    
    FichaExpedienteForm expForm = (FichaExpedienteForm) session.getAttribute("FichaExpedienteForm");
    GeneralValueObject expedienteVO = expForm.getExpedienteVO();
    
    String userAgent = request.getHeader("user-agent");
    String margenIzqPestanasDatosCme = "";

    if(userAgent.indexOf("MSIE")!=-1) {
        // Internet Explorer
        margenIzqPestanasDatosCme = "";
    }else{
        // Firefox u otro navegador
        margenIzqPestanasDatosCme = "margin-left:10px;";
    }
    
    //COMBOS
            
    // Gestor Tramitacion
    List<SelectItem> listaGestores = new ArrayList<SelectItem>();
    if(request.getAttribute("listaGestores") != null)
        listaGestores = (List<SelectItem>)request.getAttribute("listaGestores");
    
    
    String lcodGestorTramitador = "";
    String ldescGestorTramitador = "";
    
            
    if (listaGestores != null && listaGestores.size() > 0) 
    {
        int i;
        SelectItem gest = null;
        for (i = 0; i < listaGestores.size() - 1; i++) 
        {
            gest = (SelectItem) listaGestores.get(i);
            lcodGestorTramitador += "\"" + gest.getCodigo() + "\",";
            ldescGestorTramitador += "\"" + escape(gest.getDescripcion()) + "\",";
        }
        gest = (SelectItem) listaGestores.get(i);
        lcodGestorTramitador += "\"" + gest.getCodigo() + "\"";
        ldescGestorTramitador += "\"" + escape(gest.getDescripcion()) + "\"";
    }
    
    String gestor = "";
    String empresa = "";
    String impSolicitado = "";
    String impConvocatoria = "";
    String impPrevCon = "";
    String impConcedido = "";
    String impJustificado = "";
    String impRenunciado = "";
    String impPagado = "";
    String impPagado2 = "";
    String impReintegrar = "";
    String otrasAyudasConce = "";
    String otrasAyudasSolic = "";
    String minimisConce = "";
    String minimisSolic = "";
    String impDesp = "";
    String impBaja = "";
    String concedidoReal = "";
    String pagadoReal = "";
    String pagadoReal2 = "";
    String impNoJustif = "";
    String impRenunciadoRes = "";
    String impPagadoRes = "";
    
    if(request.getAttribute("gestor") != null)
    {
        gestor = (String)request.getAttribute("gestor");
    }
    
    if(request.getAttribute("empresa") != null)
    {
        empresa = (String)request.getAttribute("empresa");
    }
    
    if(request.getAttribute("solicitado") != null)
    {
        impSolicitado = (String)request.getAttribute("solicitado");
    }
    
    if(request.getAttribute("convocatoria") != null)
    {
        impConvocatoria = (String)request.getAttribute("convocatoria");
    }
    
    if(request.getAttribute("previsto") != null)
    {
        impPrevCon = (String)request.getAttribute("previsto");
    }
    
    if(request.getAttribute("concedido") != null)
    {
        impConcedido = (String)request.getAttribute("concedido");
    }
    
    if(request.getAttribute("justificado") != null)
    {
        impJustificado = (String)request.getAttribute("justificado");
    }
    
    if(request.getAttribute("renunciado") != null)
    {
        impRenunciado = (String)request.getAttribute("renunciado");
    }
    
    if(request.getAttribute("pagado") != null)
    {
        impPagado = (String)request.getAttribute("pagado");
    }
    
    if(request.getAttribute("pagado2") != null)
    {
        impPagado2 = (String)request.getAttribute("pagado2");
    }
    
    if(request.getAttribute("reintegrar") != null)
    {
        impReintegrar = (String)request.getAttribute("reintegrar");
    }
    
    if(request.getAttribute("otrasAyudasSolic") != null)
    {
        otrasAyudasSolic = (String)request.getAttribute("otrasAyudasSolic");
    }
    
    if(request.getAttribute("otrasAyudasConce") != null)
    {
        otrasAyudasConce = (String)request.getAttribute("otrasAyudasConce");
    }
    
    if(request.getAttribute("minimisSolic") != null)
    {
        minimisSolic = (String)request.getAttribute("minimisSolic");
    }
    
    if(request.getAttribute("minimisConce") != null)
    {
        minimisConce = (String)request.getAttribute("minimisConce");
    }
    
    if(request.getAttribute("importeDespido") != null)
    {
        impDesp = (String)request.getAttribute("importeDespido");
    }
    
    if(request.getAttribute("importeBajas") != null)
    {
        impBaja = (String)request.getAttribute("importeBajas");
    }
    
    if(request.getAttribute("concedidoReal") != null)
    {
        concedidoReal = (String)request.getAttribute("concedidoReal");
    }
    
    if(request.getAttribute("pagadoReal") != null)
    {
        pagadoReal = (String)request.getAttribute("pagadoReal");
    }
    
    if(request.getAttribute("pagadoReal2") != null)
    {
        pagadoReal2 = (String)request.getAttribute("pagadoReal2");
    }
    
    if(request.getAttribute("noJustificado") != null)
    {
        impNoJustif = (String)request.getAttribute("noJustificado");
    }
    
    if(request.getAttribute("renunciadoRes") != null)
    {
        impRenunciadoRes = (String)request.getAttribute("renunciadoRes");
    }
    
    if(request.getAttribute("pagadoRes") != null)
    {
        impPagadoRes = (String)request.getAttribute("pagadoRes");
    }
    
    datosCmeVO datos = null;
    datos = (datosCmeVO)request.getAttribute("datosCme");
%>

<%!
    // Funcion para escapar strings para javascript
    private String escape(String str) 
    {
        return StringEscapeUtils.escapeJavaScript(str);
    }
%>


<jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
<jsp:setProperty name="descriptor"  property="idi_cod" value="<%=idiomaUsuario%>" />
<jsp:setProperty name="descriptor"  property="apl_cod" value="<%=apl%>" />
<link rel="StyleSheet" media="screen" type="text/css" href="<%=request.getContextPath()%><%=css%>">
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/css/font-awesome.min.css'/>" media="screen">
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/less/animated.less'/>" media="screen">
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/>
<link rel="stylesheet" type="text/css" href="<c:url value='/css/extension/melanbide46/melanbide46.css'/>">

<script type="text/javascript" src="<c:url value='/scripts/popup.js'/>"></script>
<!--script type="text/javascript" src="<c:url value='/scripts/general.js'/>"></script-->

<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/melanbide46/cmeUtils.js"></script>

<script type="text/javascript">   
    var tp39;
    var mensajeValidacion = '';
    
    //LISTAS DE VALORES PARA LOS COMBOS
    var codGestorTramitador = [<%=lcodGestorTramitador%>];
    var descGestorTramitador = [<%=ldescGestorTramitador%>];
    
    function cargarDatosCme(){
        comboGestorTramitador.addItems(codGestorTramitador, descGestorTramitador);
        document.getElementById('codGestorTramitador').value = '<%=gestor != null ? gestor.toUpperCase() : ""%>';
        document.getElementById('empresa').value = '<%=empresa != null ? empresa.toUpperCase() : ""%>';
        document.getElementById('impSolicitado').value = '<%=impSolicitado != null ? impSolicitado.toUpperCase() : ""%>';
        document.getElementById('impConvocatoria').value = '<%=impConvocatoria != null ? impConvocatoria.toUpperCase() : ""%>';
        document.getElementById('impPrevistoConcesion').value = '<%=impPrevCon != null ? impPrevCon.toUpperCase() : ""%>';
        document.getElementById('impConcedido').value = '<%=impConcedido != null ? impConcedido.toUpperCase() : ""%>';
        //document.getElementById('impJustificado').value = '<%=impJustificado != null ? impJustificado.toUpperCase() :""%>';
        document.getElementById('impJustificado').value = '<%=datos != null ? datos.getImpJustificado() :""%>';
        document.getElementById('impRenunciado').value = '<%=impRenunciado != null ? impRenunciado.toUpperCase() : ""%>';
        //document.getElementById('impPagado').value = '<%=impPagado != null ? impPagado.toUpperCase() : ""%>';
        document.getElementById('impPagado2').value = '<%=impPagado2 != null ? impPagado2.toUpperCase() : ""%>';
        //document.getElementById('impReintegrar').value = '<%=impReintegrar != null ? impReintegrar.toUpperCase() : ""%>';
        document.getElementById('impReintegrar').value = '<%=datos != null ? datos.getReintegro() : ""%>';
        document.getElementById('otrasAyudasSolic').value = '<%=otrasAyudasSolic != null ? otrasAyudasSolic.toUpperCase() : ""%>';
        document.getElementById('otrasAyudasConce').value = '<%=otrasAyudasConce != null ? otrasAyudasConce.toUpperCase() : ""%>';
        document.getElementById('minimisSolic').value = '<%=minimisSolic != null ? minimisSolic.toUpperCase() : ""%>';
        document.getElementById('minimisConce').value = '<%=minimisConce != null ? minimisConce.toUpperCase() : ""%>';
        <%--document.getElementById('impDespido').value = '<%=impDesp != null ? impDesp.toUpperCase() : ""%>';
        document.getElementById('impBaja').value = '<%=impBaja != null ? impBaja.toUpperCase() : ""%>';
        --%>document.getElementById('concedidoReal').value = '<%=concedidoReal != null ? concedidoReal.toUpperCase() : ""%>';
        document.getElementById('pagadoReal').value = '<%=pagadoReal != null ? pagadoReal.toUpperCase() : ""%>';
        document.getElementById('pagadoReal2').value = '<%=pagadoReal2 != null ? pagadoReal2.toUpperCase() : ""%>';
        //document.getElementById('impNoJustif').value = '<%=impNoJustif != null ? impNoJustif.toUpperCase() : ""%>';
        document.getElementById('impNoJustif').value = '<%=datos != null ? datos.getImpNoJusi() : ""%>';
        document.getElementById('impRenunciadoRes').value = '<%=impRenunciadoRes != null ? impRenunciadoRes.toUpperCase() : ""%>';
        //document.getElementById('impPagadoRes').value = '<%=impPagadoRes != null ? impPagadoRes.toUpperCase() : ""%>';
        document.getElementById('importesModificados').value = '0';
        
        <%
            if(tramResModif == true)
            {
        %>
                document.getElementById('impPagado').value = '<%=impPagadoRes != null ? impPagadoRes.toUpperCase() : ""%>';
        <%
            }
            else
            {
        %>
                document.getElementById('impPagado').value = '<%=impPagado != null ? impPagado.toUpperCase() : ""%>';
        <%
            }
        %>
        
        
        
        
        
        cargarDescripcionesCombosDatosCme();
        
        reemplazarPuntosCme(document.getElementById('impSolicitado'));
        reemplazarPuntosCme(document.getElementById('impConvocatoria'));
        reemplazarPuntosCme(document.getElementById('impPrevistoConcesion'));
        reemplazarPuntosCme(document.getElementById('impConcedido'));
        reemplazarPuntosCme(document.getElementById('impJustificado'));
        reemplazarPuntosCme(document.getElementById('impRenunciado'));
        reemplazarPuntosCme(document.getElementById('impPagado'));
        reemplazarPuntosCme(document.getElementById('impPagado2'));
        reemplazarPuntosCme(document.getElementById('impReintegrar'));
        reemplazarPuntosCme(document.getElementById('otrasAyudasSolic'));
        reemplazarPuntosCme(document.getElementById('otrasAyudasConce'));
        reemplazarPuntosCme(document.getElementById('minimisSolic'));
        reemplazarPuntosCme(document.getElementById('minimisConce'));
        /*reemplazarPuntosCme(document.getElementById('impDespido'));
        reemplazarPuntosCme(document.getElementById('impBaja'));
        */reemplazarPuntosCme(document.getElementById('concedidoReal'));
        reemplazarPuntosCme(document.getElementById('pagadoReal'));
        
        comprobarImporteConcedido();
        comprobarImportePagado();
        comprobarImportePagado2();
        
        var consulta = '<%=(String) expedienteVO.getAtributo("modoConsulta")%>';
        if(consulta == 'si'){
            comboGestorTramitador.deactivate();
            
            document.getElementById('codGestorTramitador').className = 'inputTexto readOnly';
            document.getElementById('descGestorTramitador').className = 'inputTexto readOnly';
            document.getElementById('empresa').className = 'inputTexto readOnly';
            
            document.getElementById('impConcedido').className = 'inputTexto readOnly';
            document.getElementById('impPagado').className = 'inputTexto readOnly';
            document.getElementById('impPagado2').className = 'inputTexto readOnly';
            document.getElementById('otrasAyudasSolic').className = 'inputTexto readOnly';
            document.getElementById('otrasAyudasConce').className = 'inputTexto readOnly';
            document.getElementById('minimisSolic').className = 'inputTexto readOnly';
        }    
        mostrarOtrasAyudas();
        mostrarEnlaces();
        calculaImporteNoJusti();
    }
    
    function calculaImporteNoJusti(){
        //cargamos importe no justificado
        var conce = document.getElementById('impConcedido').value;
        var justi = document.getElementById('impJustificado').value;
        conce =  parseFloat(reemplazarTextoCme(conce, /,/g, '.'));
        justi =  parseFloat(reemplazarTextoCme(justi, /,/g, '.'));
        var nojusti = 0.0;
        if(conce > justi)
            nojusti = conce - justi;
        nojusti= nojusti.toFixed(2);
        document.getElementById('impNoJustif').value = nojusti;   
        reemplazarPuntosCme(document.getElementById('impNoJustif'));
    }
        
    function cargarDescripcionesCombosDatosCme(){
        var desc = "";
        var codAct = "";
        var codigo = "";

        //Estudios
        codigo = "<%=gestor != null ? gestor : ""%>";
        if(codigo != null && codigo != "")
        {
            for(var i=0; i<codGestorTramitador.length; i++)
            {
                codAct = codGestorTramitador[i];
                if(codAct == codigo)
                {
                    desc = descGestorTramitador[i];
                }
            }
        }
        document.getElementById('descGestorTramitador').value = desc;
    }
    
    function guardarDatosCme(){
        if(validarDatosCme()){
            document.getElementById('msgGuardandoDatosCme').style.display='inline';
            barraProgresoCme('on', 'barraProgresoSolicitudCme');
            var ajax = getXMLHttpRequest();
            var nodos = null;
            var CONTEXT_PATH = '<%=request.getContextPath()%>'
            var url = CONTEXT_PATH + '/PeticionModuloIntegracion.do';
            var parametros = '';
            var control = new Date();
            parametros = 'tarea=preparar&modulo=MELANBIDE46&operacion=guardarDatosCme&tipo=0&numero=<%=numExpediente%>'
                +'&gestor='+document.getElementById('codGestorTramitador').value
                +'&empresa='+escape(document.getElementById('empresa').value)
                +'&impPagado='+document.getElementById('impPagado').value
                +'&impPagado2='+document.getElementById('impPagado2').value
                +'&otrasAyudasSolic='+document.getElementById('otrasAyudasSolic').value
                +'&otrasAyudasConce='+document.getElementById('otrasAyudasConce').value
                +'&minimisSolic='+document.getElementById('minimisSolic').value
                +'&minimisConce='+document.getElementById('minimisConce').value
                +'&importeConcedido='+document.getElementById('impConcedido').value
                +'&importeReintegrar='+document.getElementById('impReintegrar').value
                +'&importeRenunciadoRes='+document.getElementById('impRenunciadoRes').value
                +'&importeJustificado='+document.getElementById('impJustificado').value
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
                var nodoCalculos;
                var hijosCalculos;
                var fila = new Array();
                var nodoCampo;
                for(j=0;hijos!=null && j<hijos.length;j++){
                    if(hijos[j].nodeName=="CODIGO_OPERACION"){                            
                        codigoOperacion = hijos[j].childNodes[0].nodeValue;
                    }else if(hijos[j].nodeName=="CALCULOS"){
                        nodoCalculos = hijos[j];
                        hijosCalculos = nodoCalculos.childNodes;
                        for(var cont = 0; cont < hijosCalculos.length; cont++){
                            if(hijosCalculos[cont].nodeName=="IMP_SOL"){
                                nodoCampo = hijosCalculos[cont];
                                if(nodoCampo.childNodes.length > 0){
                                    fila[0] = nodoCampo.childNodes[0].nodeValue;
                                }
                                else{
                                    fila[0] = '-';
                                }
                            }
                            else if(hijosCalculos[cont].nodeName=="IMP_CONV"){
                                nodoCampo = hijosCalculos[cont];
                                if(nodoCampo.childNodes.length > 0){
                                    fila[1] = nodoCampo.childNodes[0].nodeValue;
                                }
                                else{
                                    fila[1] = '-';
                                }
                            }
                            else if(hijosCalculos[cont].nodeName=="IMP_PREV_CON"){
                                nodoCampo = hijosCalculos[cont];
                                if(nodoCampo.childNodes.length > 0){
                                    fila[2] = nodoCampo.childNodes[0].nodeValue;
                                }
                                else{
                                    fila[2] = '-';
                                }
                            }
                            else if(hijosCalculos[cont].nodeName=="IMP_CON"){
                                nodoCampo = hijosCalculos[cont];
                                if(nodoCampo.childNodes.length > 0){
                                    fila[3] = nodoCampo.childNodes[0].nodeValue;
                                }
                                else{
                                    fila[3] = '-';
                                }
                            }
                            else if(hijosCalculos[cont].nodeName=="IMP_JUS"){
                                nodoCampo = hijosCalculos[cont];
                                if(nodoCampo.childNodes.length > 0){
                                    fila[4] = nodoCampo.childNodes[0].nodeValue;
                                }
                                else{
                                    fila[4] = '-';
                                }
                            }
                            else if(hijosCalculos[cont].nodeName=="IMP_REN"){
                                nodoCampo = hijosCalculos[cont];
                                if(nodoCampo.childNodes.length > 0){
                                    fila[5] = nodoCampo.childNodes[0].nodeValue;
                                }
                                else{
                                    fila[5] = '-';
                                }
                            }
                            else if(hijosCalculos[cont].nodeName=="IMP_PAG"){
                                nodoCampo = hijosCalculos[cont];
                                if(nodoCampo.childNodes.length > 0){
                                    fila[6] = nodoCampo.childNodes[0].nodeValue;
                                }
                                else{
                                    fila[6] = '-';
                                }
                            }
                            else if(hijosCalculos[cont].nodeName=="IMP_PAG_2"){
                                nodoCampo = hijosCalculos[cont];
                                if(nodoCampo.childNodes.length > 0){
                                    fila[7] = nodoCampo.childNodes[0].nodeValue;
                                }
                                else{
                                    fila[7] = '-';
                                }
                            }
                            else if(hijosCalculos[cont].nodeName=="IMP_REI"){
                                nodoCampo = hijosCalculos[cont];
                                if(nodoCampo.childNodes.length > 0){
                                    fila[8] = nodoCampo.childNodes[0].nodeValue;
                                }
                                else{
                                    fila[8] = '-';
                                }
                            }
                            else if(hijosCalculos[cont].nodeName=="OTRAS_AYUDAS_SOLIC"){
                                nodoCampo = hijosCalculos[cont];
                                if(nodoCampo.childNodes.length > 0){
                                    fila[9] = nodoCampo.childNodes[0].nodeValue;
                                }
                                else{
                                    fila[9] = '-';
                                }
                            }
                            else if(hijosCalculos[cont].nodeName=="OTRAS_AYUDAS_CONCE"){
                                nodoCampo = hijosCalculos[cont];
                                if(nodoCampo.childNodes.length > 0){
                                    fila[10] = nodoCampo.childNodes[0].nodeValue;
                                }
                                else{
                                    fila[10] = '-';
                                }
                            }
                            else if(hijosCalculos[cont].nodeName=="MINIMIS_SOLIC"){
                                nodoCampo = hijosCalculos[cont];
                                if(nodoCampo.childNodes.length > 0){
                                    fila[11] = nodoCampo.childNodes[0].nodeValue;
                                }
                                else{
                                    fila[11] = '-';
                                }
                            }
                            else if(hijosCalculos[cont].nodeName=="MINIMIS_CONCE"){
                                nodoCampo = hijosCalculos[cont];
                                if(nodoCampo.childNodes.length > 0){
                                    fila[12] = nodoCampo.childNodes[0].nodeValue;
                                }
                                else{
                                    fila[12] = '-';
                                }
                            }
                            else if(hijosCalculos[cont].nodeName=="IMP_DESP"){
                                nodoCampo = hijosCalculos[cont];
                                if(nodoCampo.childNodes.length > 0){
                                    fila[13] = nodoCampo.childNodes[0].nodeValue;
                                }
                                else{
                                    fila[13] = '-';
                                }
                            }
                            else if(hijosCalculos[cont].nodeName=="IMP_BAJA"){
                                nodoCampo = hijosCalculos[cont];
                                if(nodoCampo.childNodes.length > 0){
                                    fila[14] = nodoCampo.childNodes[0].nodeValue;
                                }
                                else{
                                    fila[14] = '-';
                                }
                            }
                            else if(hijosCalculos[cont].nodeName=="CONCEDIDO_REAL"){
                                nodoCampo = hijosCalculos[cont];
                                if(nodoCampo.childNodes.length > 0){
                                    fila[15] = nodoCampo.childNodes[0].nodeValue;
                                }
                                else{
                                    fila[15] = '-';
                                }
                            }
                            else if(hijosCalculos[cont].nodeName=="PAGADO_REAL"){
                                nodoCampo = hijosCalculos[cont];
                                if(nodoCampo.childNodes.length > 0){
                                    fila[16] = nodoCampo.childNodes[0].nodeValue;
                                }
                                else{
                                    fila[16] = '-';
                                }
                            }
                            else if(hijosCalculos[cont].nodeName=="PAGADO_REAL_2"){
                                nodoCampo = hijosCalculos[cont];
                                if(nodoCampo.childNodes.length > 0){
                                    fila[17] = nodoCampo.childNodes[0].nodeValue;
                                }
                                else{
                                    fila[17] = '-';
                                }
                            }
                            else if(hijosCalculos[cont].nodeName=="IMP_NO_JUS"){
                                nodoCampo = hijosCalculos[cont];
                                if(nodoCampo.childNodes.length > 0){
                                    fila[18] = nodoCampo.childNodes[0].nodeValue;
                                }
                                else{
                                    fila[18] = '-';
                                }
                            }
                            else if(hijosCalculos[cont].nodeName=="IMP_REN_RES"){
                                nodoCampo = hijosCalculos[cont];
                                if(nodoCampo.childNodes.length > 0){
                                    fila[19] = nodoCampo.childNodes[0].nodeValue;
                                }
                                else{
                                    fila[19] = '-';
                                }
                            }
                            else if(hijosCalculos[cont].nodeName=="IMP_PAG_RES"){
                                nodoCampo = hijosCalculos[cont];
                                if(nodoCampo.childNodes.length > 0){
                                    fila[20] = nodoCampo.childNodes[0].nodeValue;
                                }
                                else{
                                    fila[20] = '-';
                                }
                            }
                        }
                        recargarCalculosCme(fila);
                    }
                }
                
                barraProgresoCme('off', 'barraProgresoSolicitudCme');
                if(codigoOperacion=="0"){
                    jsp_alerta("A",'<%=meLanbide46I18n.getMensaje(idiomaUsuario,"msg.datosGuardadosOK")%>');
                }else if(codigoOperacion=="1"){
                    jsp_alerta("A",'<%=meLanbide46I18n.getMensaje(idiomaUsuario,"error.errorBD")%>');
                }else if(codigoOperacion=="2"){
                    jsp_alerta("A",'<%=meLanbide46I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                }else if(codigoOperacion=="3"){
                    jsp_alerta("A",'<%=meLanbide46I18n.getMensaje(idiomaUsuario,"error.pasoParametros")%>');
                }else if(codigoOperacion=="4"){
                    jsp_alerta("A",'<%=meLanbide46I18n.getMensaje(idiomaUsuario,"error.datosNoValidos")%>');
                }else{
                    jsp_alerta("A",'<%=meLanbide46I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                }
            }catch(err){
                jsp_alerta("A",'<%=meLanbide46I18n.getMensaje(idiomaUsuario,"error.errorProcesarRespuesta")%>');
            }
        }else{
            if(mensajeValidacion != ''){
                jsp_alerta("A", escape(mensajeValidacion));
            }
        }
    }
    
    function validarDatosCme(){
        var correcto = true;
        mensajeValidacion = '';
        
        var empresa = document.getElementById('empresa').value;
        if(!comprobarCaracteresEspecialesCme(empresa)){
            document.getElementById('empresa').style.border = '1px solid red';
            mensajeValidacion = '<%=meLanbide46I18n.getMensaje(idiomaUsuario, "msg.datosCme.empresaCaracteresNoPermitidos")%>';
            return false;
        }else{
            document.getElementById('empresa').removeAttribute("style");
            /*document.getElementById('empresa').style.marginLeft = '5px'; */
        }

        if(document.getElementById('impSolicitado').value != ''){
            try{
                if(!validarNumericoDecimalCme(document.getElementById('impSolicitado'), 10, 2)){
                    correcto = false;
                    document.getElementById('impSolicitado').style.border = '1px solid red';
                    if(mensajeValidacion == '')
                        mensajeValidacion = '<%=meLanbide46I18n.getMensaje(idiomaUsuario, "msg.datosCme.impSolicitadoIncorrecto")%>';
                }else{
                    document.getElementById('impSolicitado').removeAttribute('style');
                    /*document.getElementById('impSolicitado').style.marginLeft = '5px'; 
                    document.getElementById('impSolicitado').style.marginRight = '20px'; */
                    document.getElementById('impSolicitado').style.textAlign = 'right';
                }
            }catch(err){
                correcto = false;
                document.getElementById('impSolicitado').style.border = '1px solid red';
                if(mensajeValidacion == '')
                    mensajeValidacion = '<%=meLanbide46I18n.getMensaje(idiomaUsuario, "msg.datosCme.impSolicitadoIncorrecto")%>';
            }
        }

        if(document.getElementById('impConvocatoria').value != ''){
            try{
                if(!validarNumericoDecimalCme(document.getElementById('impConvocatoria'), 10, 2)){
                    correcto = false;
                    document.getElementById('impConvocatoria').style.border = '1px solid red';
                    if(mensajeValidacion == '')
                        mensajeValidacion = '<%=meLanbide46I18n.getMensaje(idiomaUsuario, "msg.datosCme.impConvocatoriaIncorrecto")%>';
                }else{
                    document.getElementById('impConvocatoria').removeAttribute('style');
                    /*document.getElementById('impConvocatoria').style.marginLeft = '5px'; */
                    document.getElementById('impConvocatoria').style.textAlign = 'right';
                }
            }catch(err){
                correcto = false;
                document.getElementById('impConvocatoria').style.border = '1px solid red';
                if(mensajeValidacion == '')
                    mensajeValidacion = '<%=meLanbide46I18n.getMensaje(idiomaUsuario, "msg.datosCme.impConvocatoriaIncorrecto")%>';
            }
        }

        if(document.getElementById('impConcedido').value != ''){
            try{
                if(!validarNumericoDecimalCme(document.getElementById('impConcedido'), 10, 2)){
                    correcto = false;
                    document.getElementById('impConcedido').style.border = '1px solid red';
                    if(mensajeValidacion == '')
                        mensajeValidacion = '<%=meLanbide46I18n.getMensaje(idiomaUsuario, "msg.datosCme.impConcedidoIncorrecto")%>';
                }else{
                    if(comprobarImporteConcedido()){
                        document.getElementById('impConcedido').removeAttribute('style');
                        /*document.getElementById('impConcedido').style.marginLeft = '5px'; */
                        document.getElementById('impConcedido').style.textAlign = 'right';
                    }
                }
            }catch(err){
                correcto = false;
                document.getElementById('impConcedido').style.border = '1px solid red';
                if(mensajeValidacion == '')
                    mensajeValidacion = '<%=meLanbide46I18n.getMensaje(idiomaUsuario, "msg.datosCme.impConcedidoIncorrecto")%>';
            }
        }

        if(document.getElementById('impPrevistoConcesion').value != ''){
            try{
                if(!validarNumericoDecimalCme(document.getElementById('impPrevistoConcesion'), 10, 2)){
                    correcto = false;
                    document.getElementById('impPrevistoConcesion').style.border = '1px solid red';
                    if(mensajeValidacion == '')
                        mensajeValidacion = '<%=meLanbide46I18n.getMensaje(idiomaUsuario, "msg.datosCme.impPrevistoConcesionIncorrecto")%>';
                }else{
                    document.getElementById('impPrevistoConcesion').removeAttribute('style');
                    /*document.getElementById('impPrevistoConcesion').style.marginLeft = '5px'; */
                    document.getElementById('impPrevistoConcesion').style.textAlign = 'right';
                }
            }catch(err){
                correcto = false;
                document.getElementById('impPrevistoConcesion').style.border = '1px solid red';
                if(mensajeValidacion == '')
                    mensajeValidacion = '<%=meLanbide46I18n.getMensaje(idiomaUsuario, "msg.datosCme.impPrevistoConcesionIncorrecto")%>';
            }
        }

        if(document.getElementById('impJustificado').value != ''){
            try{
                if(!validarNumericoDecimalCme(document.getElementById('impJustificado'), 10, 2)){
                    correcto = false;
                    document.getElementById('impJustificado').style.border = '1px solid red';
                    if(mensajeValidacion == '')
                        mensajeValidacion = '<%=meLanbide46I18n.getMensaje(idiomaUsuario, "msg.datosCme.impJustificadoIncorrecto")%>';
                }else{
                    document.getElementById('impJustificado').removeAttribute('style');
                    /*document.getElementById('impJustificado').style.marginLeft = '5px'; 
                    document.getElementById('impJustificado').style.marginRight = '20px'; */
                    document.getElementById('impJustificado').style.textAlign = 'right';
                }
            }catch(err){
                correcto = false;
                document.getElementById('impJustificado').style.border = '1px solid red';
                if(mensajeValidacion == '')
                    mensajeValidacion = '<%=meLanbide46I18n.getMensaje(idiomaUsuario, "msg.datosCme.impJustificadoIncorrecto")%>';
            }
        }

        if(document.getElementById('impRenunciado').value != ''){
            try{
                if(!validarNumericoDecimalCme(document.getElementById('impRenunciado'), 10, 2)){
                    correcto = false;
                    document.getElementById('impRenunciado').style.border = '1px solid red';
                    if(mensajeValidacion == '')
                        mensajeValidacion = '<%=meLanbide46I18n.getMensaje(idiomaUsuario, "msg.datosCme.impRenunciadoIncorrecto")%>';
                }else{
                    document.getElementById('impRenunciado').removeAttribute('style');
                    /*document.getElementById('impRenunciado').style.marginLeft = '5px'; */
                    document.getElementById('impRenunciado').style.textAlign = 'right';
                }
            }catch(err){
                correcto = false;
                document.getElementById('impRenunciado').style.border = '1px solid red';
                if(mensajeValidacion == '')
                    mensajeValidacion = '<%=meLanbide46I18n.getMensaje(idiomaUsuario, "msg.datosCme.impRenunciadoIncorrecto")%>';
            }
        }

        if(document.getElementById('impPagado').value != ''){
            try{
                if(!validarNumericoDecimalCme(document.getElementById('impPagado'), 10, 2)){
                    correcto = false;
                    document.getElementById('impPagado').style.border = '1px solid red';
                    if(mensajeValidacion == '')
                        mensajeValidacion = '<%=meLanbide46I18n.getMensaje(idiomaUsuario, "msg.datosCme.impPagadoIncorrecto")%>';
                }else{
                    if(comprobarImportePagado()){
                        document.getElementById('impPagado').removeAttribute('style');
                        /*document.getElementById('impPagado').style.marginLeft = '5px'; */
                        document.getElementById('impPagado').style.textAlign = 'right';
                    }
                }
            }catch(err){
                correcto = false;
                document.getElementById('impPagado').style.border = '1px solid red';
                if(mensajeValidacion == '')
                    mensajeValidacion = '<%=meLanbide46I18n.getMensaje(idiomaUsuario, "msg.datosCme.impPagadoIncorrecto")%>';
            }
        }

        if(document.getElementById('impPagado2').value != ''){
            try{
                if(!validarNumericoDecimalCme(document.getElementById('impPagado2'), 10, 2)){
                    correcto = false;
                    document.getElementById('impPagado2').style.border = '1px solid red';
                    if(mensajeValidacion == '')
                        mensajeValidacion = '<%=meLanbide46I18n.getMensaje(idiomaUsuario, "msg.datosCme.impPagado2Incorrecto")%>';
                }else{
                    if(comprobarImportePagado()){
                        document.getElementById('impPagado2').removeAttribute('style');
                        /*document.getElementById('impPagado2').style.marginLeft = '5px'; */
                        document.getElementById('impPagado2').style.textAlign = 'right';
                    }
                }
            }catch(err){
                correcto = false;
                document.getElementById('impPagado2').style.border = '1px solid red';
                if(mensajeValidacion == '')
                    mensajeValidacion = '<%=meLanbide46I18n.getMensaje(idiomaUsuario, "msg.datosCme.impPagado2Incorrecto")%>';
            }
        }

        if(document.getElementById('impReintegrar').value != ''){
            try{
                if(!validarNumericoDecimalCme(document.getElementById('impReintegrar'), 10, 2)){
                    correcto = false;
                    document.getElementById('impReintegrar').style.border = '1px solid red';
                    if(mensajeValidacion == '')
                        mensajeValidacion = '<%=meLanbide46I18n.getMensaje(idiomaUsuario, "msg.datosCme.impReintegrarIncorrecto")%>';
                }else{
                    document.getElementById('impReintegrar').removeAttribute('style');
                    /*document.getElementById('impReintegrar').style.marginLeft = '5px'; */
                    document.getElementById('impReintegrar').style.textAlign = 'right';
                }
            }catch(err){
                correcto = false;
                document.getElementById('impReintegrar').style.border = '1px solid red';
                if(mensajeValidacion == '')
                    mensajeValidacion = '<%=meLanbide46I18n.getMensaje(idiomaUsuario, "msg.datosCme.impReintegrarIncorrecto")%>';
            }
        }
        
        if(document.getElementById('otrasAyudasSolic').value != ''){
            try{
                if(!validarNumericoDecimalCme(document.getElementById('otrasAyudasSolic'), 10, 2)){
                    correcto = false;
                    document.getElementById('otrasAyudasSolic').style.border = '1px solid red';
                    if(mensajeValidacion == '')
                        mensajeValidacion = '<%=meLanbide46I18n.getMensaje(idiomaUsuario, "msg.datosCme.otrasAyudasSolicIncorrecto")%>';
                }else{
                    document.getElementById('otrasAyudasSolic').removeAttribute('style');
                    /*document.getElementById('otrasAyudasSolic').style.marginLeft = '5px'; 
                    document.getElementById('otrasAyudasSolic').style.marginRight = '20px'; */
                    document.getElementById('otrasAyudasSolic').style.textAlign = 'right';
                }
            }catch(err){
                correcto = false;
                document.getElementById('otrasAyudasSolic').style.border = '1px solid red';
                if(mensajeValidacion == '')
                    mensajeValidacion = '<%=meLanbide46I18n.getMensaje(idiomaUsuario, "msg.datosCme.otrasAyudasSolicIncorrecto")%>';
            }
        }
        
        if(document.getElementById('otrasAyudasConce').value != ''){
            try{
                if(!validarNumericoDecimalCme(document.getElementById('otrasAyudasConce'), 10, 2)){
                    correcto = false;
                    document.getElementById('otrasAyudasConce').style.border = '1px solid red';
                    if(mensajeValidacion == '')
                        mensajeValidacion = '<%=meLanbide46I18n.getMensaje(idiomaUsuario, "msg.datosCme.otrasAyudasConceIncorrecto")%>';
                }else{
                    document.getElementById('otrasAyudasConce').removeAttribute('style');
                    /*document.getElementById('otrasAyudasConce').style.marginLeft = '5px'; 
                    document.getElementById('otrasAyudasConce').style.marginRight = '20px'; */
                    document.getElementById('otrasAyudasConce').style.textAlign = 'right';
                }
            }catch(err){
                correcto = false;
                document.getElementById('otrasAyudasConce').style.border = '1px solid red';
                if(mensajeValidacion == '')
                    mensajeValidacion = '<%=meLanbide46I18n.getMensaje(idiomaUsuario, "msg.datosCme.otrasAyudasConceIncorrecto")%>';
            }
        }
        
        if(document.getElementById('minimisSolic').value != ''){
            try{
                if(!validarNumericoDecimalCme(document.getElementById('minimisSolic'), 10, 2)){
                    correcto = false;
                    document.getElementById('minimisSolic').style.border = '1px solid red';
                    if(mensajeValidacion == '')
                        mensajeValidacion = '<%=meLanbide46I18n.getMensaje(idiomaUsuario, "msg.datosCme.minimisSolicIncorrecto")%>';
                }else{
                    document.getElementById('minimisSolic').removeAttribute('style');
                    /*document.getElementById('minimisSolic').style.marginLeft = '5px'; 
                    document.getElementById('minimisSolic').style.marginRight = '20px'; */
                    document.getElementById('minimisSolic').style.textAlign = 'right';
                }
            }catch(err){
                correcto = false;
                document.getElementById('minimisSolic').style.border = '1px solid red';
                if(mensajeValidacion == '')
                    mensajeValidacion = '<%=meLanbide46I18n.getMensaje(idiomaUsuario, "msg.datosCme.minimisSolicIncorrecto")%>';
            }
        }
        
        if(document.getElementById('minimisConce').value != ''){
            try{
                if(!validarNumericoDecimalCme(document.getElementById('minimisConce'), 10, 2)){
                    correcto = false;
                    document.getElementById('minimisConce').style.border = '1px solid red';
                    if(mensajeValidacion == '')
                        mensajeValidacion = '<%=meLanbide46I18n.getMensaje(idiomaUsuario, "msg.datosCme.minimisConceIncorrecto")%>';
                }else{
                    document.getElementById('minimisConce').removeAttribute('style');
                    /*document.getElementById('minimisConce').style.marginLeft = '5px'; 
                    document.getElementById('minimisConce').style.marginRight = '20px'; */
                    document.getElementById('minimisConce').style.textAlign = 'right';
                }
            }catch(err){
                correcto = false;
                document.getElementById('minimisConce').style.border = '1px solid red';
                if(mensajeValidacion == '')
                    mensajeValidacion = '<%=meLanbide46I18n.getMensaje(idiomaUsuario, "msg.datosCme.minimisConceIncorrecto")%>';
            }
        }
        
        <%--if(document.getElementById('impDespido').value != ''){
            try{
                if(!validarNumericoDecimalCme(document.getElementById('impDespido'), 10, 2)){
                    correcto = false;
                    document.getElementById('impDespido').style.border = '1px solid red';
                    if(mensajeValidacion == '')
                        mensajeValidacion = '<%=meLanbide46I18n.getMensaje(idiomaUsuario, "msg.datosCme.impDespidosIncorrecto")%>';
                }else{
                    document.getElementById('impDespido').removeAttribute('style');
                    document.getElementById('impDespido').style.marginLeft = '5px'; 
                    document.getElementById('impDespido').style.marginRight = '20px'; 
                    document.getElementById('impDespido').style.textAlign = 'right';
                }
            }catch(err){
                correcto = false;
                document.getElementById('impDespido').style.border = '1px solid red';
                if(mensajeValidacion == '')
                    mensajeValidacion = '<%=meLanbide46I18n.getMensaje(idiomaUsuario, "msg.datosCme.impDespidosIncorrecto")%>';
            }
        }
        
        if(document.getElementById('impBaja').value != ''){
            try{
                if(!validarNumericoDecimalCme(document.getElementById('impBaja'), 10, 2)){
                    correcto = false;
                    document.getElementById('impBaja').style.border = '1px solid red';
                    if(mensajeValidacion == '')
                        mensajeValidacion = '<%=meLanbide46I18n.getMensaje(idiomaUsuario, "msg.datosCme.impBajasIncorrecto")%>';
                }else{
                    document.getElementById('impBaja').removeAttribute('style');
                    document.getElementById('impBaja').style.marginLeft = '5px'; 
                    document.getElementById('impBaja').style.marginRight = '20px'; 
                    document.getElementById('impBaja').style.textAlign = 'right';
                }
            }catch(err){
                correcto = false;
                document.getElementById('impBaja').style.border = '1px solid red';
                if(mensajeValidacion == '')
                    mensajeValidacion = '<%=meLanbide46I18n.getMensaje(idiomaUsuario, "msg.datosCme.impBajasIncorrecto")%>';
            }
        }

   /*     var impSubvFinalCorrecto = true;
        if(document.getElementById('impSubvFinal').value != ''){
            try{
                if(!validarNumericoDecimalCme(document.getElementById('impSubvFinal'), 10, 2)){
                    correcto = false;
                    document.getElementById('impSubvFinal').style.border = '1px solid red';
                    if(mensajeValidacion == '')
                        mensajeValidacion = '<%=meLanbide46I18n.getMensaje(idiomaUsuario, "msg.datosCme.impSubvFinalIncorrecto")%>';
                }else{
                    if(!comprobarImpSubvFinal()){
                        impSubvFinalCorrecto = false;
                        document.getElementById('impSubvFinal').style.border = '1px solid red';
                    }else{
                        document.getElementById('impSubvFinal').removeAttribute('style');
                    }
                }
            }catch(err){
                correcto = false;
                document.getElementById('impSubvFinal').style.border = '1px solid red';
                if(mensajeValidacion == '')
                    mensajeValidacion = '<%=meLanbide46I18n.getMensaje(idiomaUsuario, "msg.datosCme.impSubvFinalIncorrecto")%>';
            }
        }
        
        //Esto siempre al final
        //Si todos los datos tienen el formato v�lido pero el campo Imp. Subv. Final 
        //no es la suma de los otros dos, se pregunta si quiere guardar.
        if(correcto && !impSubvFinalCorrecto){
            var resultado = jsp_alerta('','<%=meLanbide46I18n.getMensaje(idiomaUsuario, "msg.datosCme.preguntaimpSubvFinalIncorrecto")%>');
            if (resultado != 1){
                correcto = false;
                mensajeValidacion = '';
            }
        }*/--%>
        
        return correcto;
    }
    
    function comprobarImpSubvFinal(){
        var input1 = document.getElementById('impSolicitado');
        var input2 = document.getElementById('otrasAyudasConce');
        var inputTot = document.getElementById('impConcedido');
        var n1;
        var n2;
        var tot;
        try{
            if(!isNaN(input1.value)){
                n1 = parseInt(input1.value);
            }
        }catch(err){
            
        }
        
        try{
            if(!isNaN(input2.value)){
                n2 = parseInt(input2.value);
            }
        }catch(err){
            
        }
        
        if(!isNaN(n1) && !isNaN(n2)){
            tot = ''+(n1 + n2);
        }else if(!isNaN(n1)){
            tot = ''+n1;
        }else if(!isNaN(n2)){
            tot = ''+n2;
        }else{
            tot = '';
        }
        
        if(inputTot.value == tot){
            inputTot.removeAttribute("style");
            return true;
        }else{
            //inputTot.style.color = 'red';
            inputTot.style.border = '1px solid red';
            return false;
        }
    }
    
    function recargarCalculosCme(calculos){
        var importesModificados = document.getElementById('importesModificados').value;
        if(importesModificados == '0'){
            document.getElementById('impConcedido').value = calculos[3];
            document.getElementById('impPagado').value = calculos[6];
            document.getElementById('otrasAyudasConce').value = calculos[10];
        }
        document.getElementById('impSolicitado').value = calculos[0];
        document.getElementById('impConvocatoria').value = calculos[1];
        document.getElementById('impPrevistoConcesion').value = calculos[2];
        
        document.getElementById('impJustificado').value = calculos[4];
        document.getElementById('impRenunciado').value = calculos[5];
        
        document.getElementById('impPagado2').value = calculos[7];

        document.getElementById('impReintegrar').value = calculos[8];
        
        document.getElementById('otrasAyudasSolic').value = calculos[9];
        document.getElementById('minimisSolic').value = calculos[11];
        document.getElementById('minimisConce').value = calculos[12];
        /*document.getElementById('impDespido').value = calculos[13];
        document.getElementById('impBaja').value = calculos[14];*/

        document.getElementById('concedidoReal').value = calculos[15] != undefined && calculos[15] != '' ? calculos[15] : '0,00';
        document.getElementById('pagadoReal').value = calculos[16] != undefined && calculos[16] != '' ? calculos[16] : '0,00';
        document.getElementById('pagadoReal2').value = calculos[17] != undefined && calculos[17] != '' ? calculos[17] : '0,00';
        document.getElementById('impNoJustif').value = calculos[18] != undefined && calculos[18] != '' ? calculos[18] : '0,00';
        document.getElementById('impRenunciadoRes').value = calculos[19] != undefined && calculos[19] != '' ? calculos[19] : '0,00';
        //document.getElementById('impPagadoRes').value = calculos[20] != undefined && calculos[20] != '' ? calculos[20] : '0,00';
        
        ajustarDecimalesImportes();
        comprobarImporteConcedido();
        comprobarImportePagado();
        comprobarImportePagado2();
        
        actualizarPestanaDatosSuplementarios();
    }
    
    function comprobarImporteConcedido(){
        var concedidoMostrado = document.getElementById('impConcedido').value;
        var concedidoReal = document.getElementById('concedidoReal').value;
        
        concedidoMostrado = reemplazarTextoCme(concedidoMostrado, /,/g, '.');
        concedidoReal = reemplazarTextoCme(concedidoReal, /,/g, '.');
        
        var f1;
        if(concedidoMostrado != ''){
            f1 = parseFloat(concedidoMostrado);
        }else{
            f1 = parseFloat(concedidoReal);
        }
        var f2 = parseFloat(concedidoReal);
        
        if(f1 != f2){
            document.getElementById('impConcedido').style.border = '1px solid red';
        }else{
            document.getElementById('impConcedido').removeAttribute('style');
            /*document.getElementById('impConcedido').style.marginLeft = '5px'; */
            document.getElementById('impConcedido').style.textAlign = 'right';
        }
    }
    
    function mostrarImpConCalculado(event){
        var rsultado;
        var minimis = document.getElementById('minimisConce').value;
        var impRen=parseInt(document.getElementById('impRenunciado').value)+parseInt(document.getElementById('impRenunciadoRes').value);
        if(document.forms[0].modoConsulta.value == "si"){
            if(minimis != undefined && minimis != ''){
                resultado = jsp_alerta_grande('A','<%=meLanbide46I18n.getMensaje(idiomaUsuario, "msg.datosCme.impConCalculadoEs")%>: <br/>'+document.getElementById('concedidoReal').value+' '+'<%=meLanbide46I18n.getMensaje(idiomaUsuario, "msg.euros")%>'+'<br/>'+'<%=meLanbide46I18n.getMensaje(idiomaUsuario, "msg.avisoMinimis")%>'+' '+document.getElementById('minimisConce').value+'<br/>'+'<%=meLanbide46I18n.getMensaje(idiomaUsuario, "msg.avisoImporteRenunciado")%>'+' '+impRen);
            }else{
                resultado = jsp_alerta_grande('A','<%=meLanbide46I18n.getMensaje(idiomaUsuario, "msg.datosCme.impConCalculadoEs")%>: <br/>'+document.getElementById('concedidoReal').value+' '+'<%=meLanbide46I18n.getMensaje(idiomaUsuario, "msg.euros")%>'+'<br/>'+'<%=meLanbide46I18n.getMensaje(idiomaUsuario, "msg.avisoImporteRenunciado")%>'+' '+impRen);
            }
        }else{
            if(minimis != undefined && minimis != ''){
                resultado = jsp_alerta_grande('','<%=meLanbide46I18n.getMensaje(idiomaUsuario, "msg.datosCme.impConCalculadoEs")%>: <br/>'+document.getElementById('concedidoReal').value+' '+'<%=meLanbide46I18n.getMensaje(idiomaUsuario, "msg.euros")%>'+'<br/>'+'<%=meLanbide46I18n.getMensaje(idiomaUsuario, "msg.avisoMinimis")%>'+' '+document.getElementById('minimisConce').value+'<br/>'+'<%=meLanbide46I18n.getMensaje(idiomaUsuario, "msg.avisoImporteRenunciado")%>'+' '+impRen+'<br/><br/>'+'<%=meLanbide46I18n.getMensaje(idiomaUsuario, "msg.datosCme.preguntaAceptarImporte")%>');
            }else{
                resultado = jsp_alerta_grande('','<%=meLanbide46I18n.getMensaje(idiomaUsuario, "msg.datosCme.impConCalculadoEs")%>: <br/>'+document.getElementById('concedidoReal').value+' '+'<%=meLanbide46I18n.getMensaje(idiomaUsuario, "msg.euros")%>'+'<br/>'+'<%=meLanbide46I18n.getMensaje(idiomaUsuario, "msg.avisoImporteRenunciado")%>'+' '+impRen+'<br/><br/>'+'<%=meLanbide46I18n.getMensaje(idiomaUsuario, "msg.datosCme.preguntaAceptarImporte")%>');
            }
        }
        if (resultado == 1){
            document.getElementById('impConcedido').value = document.getElementById('concedidoReal').value;
            document.getElementById('impConcedido').removeAttribute('style');
            /*document.getElementById('impConcedido').style.marginLeft = '5px'; */
            document.getElementById('impConcedido').style.textAlign = 'right';
            document.getElementById('importesModificados').value = '1';
            recalcularPagadoReal();
        }
    }
            
            
    function mostrarImpConCalculado2(event){
        var resultado;
        var minimis = document.getElementById('minimisConce').value;
        var valorMinimis = 0.0;
        try{
            valorMinimis = parseFloat(minimis);
        }catch(err){
            valorMinimis = 0.0;
        }
        var impPrevCon = document.getElementById('impPrevistoConcesion').value;
        var valorImpPrevCon = 0.0;
        try{
            valorImpPrevCon = parseFloat(impPrevCon);
        }catch(err){
            valorImpPrevCon = 0.0;
        }
        var conce = document.getElementById('concedidoReal').value;
        var strEuros = '<%=meLanbide46I18n.getMensaje(idiomaUsuario, "msg.euros").toLowerCase()%>';
        
        var msg = '';
        
        msg += '<%=meLanbide46I18n.getMensaje(idiomaUsuario, "msg.datosCme.impPrevConEs")%>: '+impPrevCon+' '+strEuros;
        if(valorMinimis > 0.0){
            msg += '<br/><br/>'+'<%=meLanbide46I18n.getMensaje(idiomaUsuario, "msg.avisoMinimis")%>'+' '+minimis+' '+strEuros;
            
            if(valorMinimis + valorImpPrevCon > parseFloat('<%=ConstantesMeLanbide46.MAX_CONCE_PERMITIDO%>')){
                msg += '<br/><br/>'+'<%=String.format(meLanbide46I18n.getMensaje(idiomaUsuario, "msg.datosCme.impConMasMinimisSuperaMaxPermitido"), df.format(ConstantesMeLanbide46.MAX_CONCE_PERMITIDO))%>'+' '+conce+' '+strEuros;
            }else{
                msg += '<br/><br/>'+'<%=meLanbide46I18n.getMensaje(idiomaUsuario, "msg.datosCme.impConCalculadoEs")%>'+' '+conce+' '+strEuros;
            }
        }else{
            if(valorImpPrevCon > parseFloat('<%=ConstantesMeLanbide46.MAX_CONCE_PERMITIDO%>')){
                msg += '<br/><br/>'+'<%=String.format(meLanbide46I18n.getMensaje(idiomaUsuario, "msg.datosCme.impConSuperaMaxPermitido"), df.format(ConstantesMeLanbide46.MAX_CONCE_PERMITIDO))%>'+' '+conce+' '+strEuros;
            }else{
                msg += '<br/><br/>'+'<%=meLanbide46I18n.getMensaje(idiomaUsuario, "msg.datosCme.impConCalculadoEs")%>'+' '+conce+' '+strEuros;
            }
        }
        
        /*if(valorMinimis + valorImpPrevCon > parseFloat('<%=ConstantesMeLanbide46.MAX_CONCE_PERMITIDO%>')){
             msg += '<br/><br/>'+'<%=String.format(meLanbide46I18n.getMensaje(idiomaUsuario, "msg.datosCme.impConSuperaMaxPermitido"), df.format(ConstantesMeLanbide46.MAX_CONCE_PERMITIDO))%>'+' '+conce+' '+strEuros;
        }else{
             msg += '<br/><br/>'+'<%=meLanbide46I18n.getMensaje(idiomaUsuario, "msg.datosCme.impConCalculadoEs")%>'+' '+conce+' '+strEuros;
        }*/
        
       
        
        
        if(document.forms[0].modoConsulta.value != "si"){
            msg += '<br/><br/>'+'<%=meLanbide46I18n.getMensaje(idiomaUsuario, "msg.datosCme.preguntaAceptarImporte")%>';
        }
        
        
        /*if(document.forms[0].modoConsulta.value == "si"){
            if(minimis != undefined && minimis != ''){
                resultado = jsp_alerta_grande('A','<%=meLanbide46I18n.getMensaje(idiomaUsuario, "msg.datosCme.impPrevConEs")%>: '+document.getElementById('impPrevistoConcesion').value+' '+'<%=meLanbide46I18n.getMensaje(idiomaUsuario, "msg.euros")%>'+'<br/><br/>'+'<%=meLanbide46I18n.getMensaje(idiomaUsuario, "msg.avisoMinimis")%>'+' '+document.getElementById('minimisConce').value+' '+'<%=meLanbide46I18n.getMensaje(idiomaUsuario, "msg.euros")%>'+'<br/><br/>'+'<%=meLanbide46I18n.getMensaje(idiomaUsuario, "msg.datosCme.impConCalculadoEs")%>'+' '+document.getElementById('concedidoReal').value+' '+'<%=meLanbide46I18n.getMensaje(idiomaUsuario, "msg.euros")%>', '<%=meLanbide46I18n.getMensaje(idiomaUsuario, "titulo.aceptarImporte")%>');
            }else{
                resultado = jsp_alerta_grande('A','<%=meLanbide46I18n.getMensaje(idiomaUsuario, "msg.datosCme.impPrevConEs")%>: '+document.getElementById('impPrevistoConcesion').value+' '+'<%=meLanbide46I18n.getMensaje(idiomaUsuario, "msg.euros")%>'+'<br/><br/>'+'<%=meLanbide46I18n.getMensaje(idiomaUsuario, "msg.datosCme.impConCalculadoEs")%>'+' '+document.getElementById('concedidoReal').value+' '+'<%=meLanbide46I18n.getMensaje(idiomaUsuario, "msg.euros")%>', '<%=meLanbide46I18n.getMensaje(idiomaUsuario, "titulo.aceptarImporte")%>');
            }
        }else{
            if(minimis != undefined && minimis != ''){
                resultado = jsp_alerta_grande('','<%=meLanbide46I18n.getMensaje(idiomaUsuario, "msg.datosCme.impPrevConEs")%>: '+document.getElementById('impPrevistoConcesion').value+' '+'<%=meLanbide46I18n.getMensaje(idiomaUsuario, "msg.euros")%>'+'<br/><br/>'+'<%=meLanbide46I18n.getMensaje(idiomaUsuario, "msg.avisoMinimis")%>'+' '+document.getElementById('minimisConce').value+' '+'<%=meLanbide46I18n.getMensaje(idiomaUsuario, "msg.euros")%>'+'<br/><br/>'+'<%=meLanbide46I18n.getMensaje(idiomaUsuario, "msg.datosCme.impConCalculadoEs")%>'+' '+document.getElementById('concedidoReal').value+' '+'<%=meLanbide46I18n.getMensaje(idiomaUsuario, "msg.euros")%>'+'<br/><br/>'+'<%=meLanbide46I18n.getMensaje(idiomaUsuario, "msg.datosCme.preguntaAceptarImporte")%>', '<%=meLanbide46I18n.getMensaje(idiomaUsuario, "titulo.aceptarImporte")%>');
            }else{
                resultado = jsp_alerta_grande('','<%=meLanbide46I18n.getMensaje(idiomaUsuario, "msg.datosCme.impPrevConEs")%>: '+document.getElementById('impPrevistoConcesion').value+' '+'<%=meLanbide46I18n.getMensaje(idiomaUsuario, "msg.euros")%>'+'<br/><br/>'+'<%=meLanbide46I18n.getMensaje(idiomaUsuario, "msg.datosCme.impConCalculadoEs")%>'+' '+document.getElementById('concedidoReal').value+' '+'<%=meLanbide46I18n.getMensaje(idiomaUsuario, "msg.euros")%>'+'<br/><br/>'+'<%=meLanbide46I18n.getMensaje(idiomaUsuario, "msg.datosCme.preguntaAceptarImporte")%>', '<%=meLanbide46I18n.getMensaje(idiomaUsuario, "titulo.aceptarImporte")%>');
            }
        }*/
                
        resultado = jsp_alerta_grande('P', msg, '<%=meLanbide46I18n.getMensaje(idiomaUsuario, "titulo.aceptarImporte")%>');
        
        if (resultado == 1){
            document.getElementById('impConcedido').value = document.getElementById('concedidoReal').value;
            document.getElementById('impConcedido').removeAttribute('style');
            /*document.getElementById('impConcedido').style.marginLeft = '5px'; */
            document.getElementById('impConcedido').style.textAlign = 'right';
            document.getElementById('importesModificados').value = '1';
            recalcularPagadoReal();
            calculaImporteNoJusti();
        }
    }
    
    function comprobarImportePagado(){
        var pagadoMostrado = document.getElementById('impPagado').value;
        var pagadoReal = document.getElementById('pagadoReal').value;
        
        pagadoMostrado = reemplazarTextoCme(pagadoMostrado, /,/g, '.');
        pagadoReal = reemplazarTextoCme(pagadoReal, /,/g, '.');
        
        var f1;
        if(pagadoMostrado != ''){
            f1 = parseFloat(pagadoMostrado);
        }else{
            f1 = parseFloat(pagadoReal);
        }
        var f2 = parseFloat(pagadoReal);
        
        if(f1 != f2){
            document.getElementById('impPagado').style.border = '1px solid red';
        }else{
            document.getElementById('impPagado').removeAttribute('style');
            /*document.getElementById('impPagado').style.marginLeft = '5px'; */
            document.getElementById('impPagado').style.textAlign = 'right';
        }
        //recalcularImpReintegro();
    }
    
    function mostrarImpPagCalculado(event){
        var resultado;
        if(document.forms[0].modoConsulta.value == "si"){    
            resultado = jsp_alerta('A','<%=meLanbide46I18n.getMensaje(idiomaUsuario, "msg.datosCme.impPagCalculadoEs")%>: <br/>'+document.getElementById('pagadoReal').value+' '+'<%=meLanbide46I18n.getMensaje(idiomaUsuario, "msg.euros")%>');
        }else{
            resultado = jsp_alerta('','<%=meLanbide46I18n.getMensaje(idiomaUsuario, "msg.datosCme.impPagCalculadoEs")%>: <br/>'+document.getElementById('pagadoReal').value+' '+'<%=meLanbide46I18n.getMensaje(idiomaUsuario, "msg.euros")%>'+'<br/><br/>'+'<%=meLanbide46I18n.getMensaje(idiomaUsuario, "msg.datosCme.preguntaAceptarImporte")%>');
        }
        if (resultado == 1){
            document.getElementById('impPagado').value = document.getElementById('pagadoReal').value;
            document.getElementById('impPagado').removeAttribute('style');
            /*document.getElementById('impPagado').style.marginLeft = '5px'; */
            document.getElementById('impPagado').style.textAlign = 'right';
            document.getElementById('importesModificados').value = '1';
            recalcularImpReintegro();
            recalcularPago2Real();
        }
    }
    
    function recalcularConcedidoReal(){
        try
        {
            if(validarNumericoDecimalCme(document.getElementById('impPrevistoConcesion'), 10, 2) && validarNumericoDecimalCme(document.getElementById('otrasAyudasConce'), 10, 2)){
                var prevCon = document.getElementById('impPrevistoConcesion').value;
                var oayu = document.getElementById('otrasAyudasConce').value;
                
                prevCon = reemplazarTextoCme(prevCon, /,/g, '.');
                oayu = reemplazarTextoCme(oayu, /,/g, '.');
                
                var f1 = parseFloat(prevCon);
                var f2 = parseFloat(oayu);
                var res = f1 - f2;
                res = res.toFixed(2);
                
                document.getElementById('concedidoReal').value = res;
                reemplazarPuntosCme(document.getElementById('concedidoReal'));
            }
        }catch(err){
            
        }
        comprobarImporteConcedido();
        recalcularPagadoReal();
    }
    
    function recalcularPagadoReal(){
        try{
            if(validarNumericoDecimalCme(document.getElementById('impConcedido'), 10, 2) && validarNumericoDecimalCme(document.getElementById('impConcedido'), 10, 2)){
                var con = document.getElementById('impConcedido').value;
                con = reemplazarTextoCme(con, /,/g, '.');
                var f1 = parseFloat(con);
                f1 = f1 * 0.8;
                f1 = f1.toFixed(2);
                
                document.getElementById('pagadoReal').value = f1;
                
                reemplazarPuntosCme(document.getElementById('pagadoReal'));
            }
        }catch(err){
            
        }
        comprobarImportePagado();
        recalcularPago2Real();
    }
    
    function mostrarImpPag2Calculado(event){
        var resultado;
        var valor = document.getElementById('pagadoReal2').value;
        if(validarNumericoDecimalCme(document.getElementById('impPagado'), 10, 2)){
            //var con = document.getElementById('impConcedido').value;
            var pag1 = document.getElementById('impPagado').value;
            var just = document.getElementById('impJustificado').value;
            
            //con = reemplazarTextoCme(con, /,/g, '.');
            pag1 = reemplazarTextoCme(pag1, /,/g, '.');
            just = reemplazarTextoCme(just, /,/g, '.');

            //var fCon = parseFloat(con);
            var fPag1 = parseFloat(pag1);
            var fJust = parseFloat(just);
            var fPag2 = null;

            if(fJust - fPag1 > 0){
                fPag2 = fJust - fPag1;
            }else{
                fPag2 = 0;
            }

            fPag2 = fPag2.toFixed(2);
            fPag2 = fPag2.replace(/\./g,',');
        }
        if(document.forms[0].modoConsulta.value == "si"){    
            resultado = jsp_alerta('A','<%=meLanbide46I18n.getMensaje(idiomaUsuario, "msg.datosCme.impPag2CalculadoEs")%>: <br/>'+fPag2+' '+'<%=meLanbide46I18n.getMensaje(idiomaUsuario, "msg.euros")%>');
        }else{
            resultado = jsp_alerta('','<%=meLanbide46I18n.getMensaje(idiomaUsuario, "msg.datosCme.impPag2CalculadoEs")%>: <br/>'+fPag2+' '+'<%=meLanbide46I18n.getMensaje(idiomaUsuario, "msg.euros")%>'+'<br/><br/>'+'<%=meLanbide46I18n.getMensaje(idiomaUsuario, "msg.datosCme.preguntaAceptarImporte")%>');
        }
        if (resultado == 1){
            document.getElementById('impPagado2').value = fPag2;
            document.getElementById('impPagado2').removeAttribute('style');
            /*document.getElementById('impPagado').style.marginLeft = '5px'; */
            document.getElementById('impPagado2').style.textAlign = 'right';
            document.getElementById('importesModificados').value = '1';
            recalcularImpReintegro();
        }
    }
    
    function recalcularPago2Real(){
        try{
            if(validarNumericoDecimalCme(document.getElementById('impConcedido'), 10, 2) && validarNumericoDecimalCme(document.getElementById('impPagado'), 10, 2)){
                var con = document.getElementById('impConcedido').value;
                var pag1 = document.getElementById('impPagado').value;
                var just = document.getElementById('impJustificado').value;
                
                con = reemplazarTextoCme(con, /,/g, '.');
                pag1 = reemplazarTextoCme(pag1, /,/g, '.');
                just = reemplazarTextoCme(just, /,/g, '.');
                
                var fCon = parseFloat(con);
                var fPag1 = parseFloat(pag1);
                var fJust = parseFloat(just);
                var fPag2 = null;
                                
                if(fJust - fPag1 > 0){
                    fPag2 = fJust - fPag1;
                }else{
                    fPag2 = 0;
                }
                
                fPag2 = fPag2.toFixed(2);
                
                document.getElementById('pagadoReal2').value = fPag2;
                
                reemplazarPuntosCme(document.getElementById('pagadoReal2'));
            }
        }catch(err){
            
        }
        comprobarImportePagado2();
    }
    
    function comprobarImportePagado2(){
        var pago2Mostrado = document.getElementById('impPagado2').value;
        var pago2Real = document.getElementById('pagadoReal2').value;
        
        pago2Mostrado = reemplazarTextoCme(pago2Mostrado, /,/g, '.');
        pago2Real = reemplazarTextoCme(pago2Real, /,/g, '.');
        
        var f1;
        if(pago2Mostrado != ''){
            f1 = parseFloat(pago2Mostrado);
        }else{
            f1 = parseFloat(pago2Real);
        }
        var f2 = parseFloat(pago2Real);
        
        if(f1 != f2){
            document.getElementById('impPagado2').style.border = '1px solid red';
        }else{
            document.getElementById('impPagado2').removeAttribute('style');
            /*document.getElementById('impConcedido').style.marginLeft = '5px'; */
            document.getElementById('impPagado2').style.textAlign = 'right';
        }
    }
    
    function importeModificado(){
        document.getElementById('importesModificados').value = '1';
    }
    
    function actualizarOtrasPestanas(pestana){
        try{
            if (pestana == 'resumen') {
                actualizarPestanaResumen();
            } else {
            if(pestana != 'oferta'){
                //Pestana ofertas
                actualizarPestanaOferta();
            }
            
            if(pestana != 'justif'){
                actualizarPestanaJustificacion();
                }
            }
        }catch(err){
            
        }
    }
    
    function recalcularImpReintegro(){
        try
        {
            if(validarNumericoDecimalCme(document.getElementById('impPagado'), 10, 2) && validarNumericoDecimalCme(document.getElementById('impJustificado'), 10, 2)){
                var pagado = document.getElementById('impPagado').value;
                var justif = document.getElementById('impJustificado').value;
                
                pagado = reemplazarTextoCme(pagado, /,/g, '.');
                justif = reemplazarTextoCme(justif, /,/g, '.');
                
                var f1 = parseFloat(pagado);
                var f2 = parseFloat(justif);
                if(f1 >= f2){
                var res = f1 - f2;
                    res = res.toFixed(2);

                    document.getElementById('impReintegrar').value = res;
                    reemplazarPuntosCme(document.getElementById('impReintegrar'));
                }else{
                    document.getElementById('impReintegrar').value = '0,00';
                }
            }else{
                document.getElementById('impReintegrar').value = '0,00';
            }
        }catch(err){
            
        }
        ajustarDecimalesImportes();
    }
    
    function ajustarDecimalesImportes(){
        var f;
        var v;
        //Importe solicitado
        v = document.getElementById('impSolicitado').value;
        v = reemplazarTextoCme(v, /,/g, '.');
        f = parseFloat(v);
        f = ajustarDecimalesCme(f, 2);
        if(!isNaN(f)){
            document.getElementById('impSolicitado').value = f;
            reemplazarPuntosCme(document.getElementById('impSolicitado'));
            document.getElementById('impSolicitado').removeAttribute("style");
            document.getElementById('impSolicitado').style.textAlign = 'right';
        }
        
        //Imp. prev. concesi�n
        v = document.getElementById('impPrevistoConcesion').value;
        v = reemplazarTextoCme(v, /,/g, '.');
        f = parseFloat(v);
        f = ajustarDecimalesCme(f, 2);
        if(!isNaN(f)){
            document.getElementById('impPrevistoConcesion').value = f;
            reemplazarPuntosCme(document.getElementById('impPrevistoConcesion'));
            document.getElementById('impPrevistoConcesion').removeAttribute("style");
            document.getElementById('impPrevistoConcesion').style.textAlign = 'right';
        }
        
        //Importe concedido
        v = document.getElementById('impConcedido').value;
        v = reemplazarTextoCme(v, /,/g, '.');
        f = parseFloat(v);
        f = ajustarDecimalesCme(f, 2);
        if(!isNaN(f)){
            document.getElementById('impConcedido').value = f;
            reemplazarPuntosCme(document.getElementById('impConcedido'));
            document.getElementById('impConcedido').removeAttribute("style");
            document.getElementById('impConcedido').style.textAlign = 'right';
        }
        
        //Importe renunciado
        v = document.getElementById('impRenunciado').value;
        v = reemplazarTextoCme(v, /,/g, '.');
        f = parseFloat(v);
        f = ajustarDecimalesCme(f, 2);
        if(!isNaN(f)){
            document.getElementById('impRenunciado').value = f;
            reemplazarPuntosCme(document.getElementById('impRenunciado'));
            document.getElementById('impRenunciado').removeAttribute("style");
            document.getElementById('impRenunciado').style.textAlign = 'right';
        }
        
        //Importe renunciado resolucion
        v = document.getElementById('impRenunciadoRes').value;
        v = reemplazarTextoCme(v, /,/g, '.');
        f = parseFloat(v);
        f = ajustarDecimalesCme(f, 2);
        if(!isNaN(f)){
            document.getElementById('impRenunciadoRes').value = f;
            reemplazarPuntosCme(document.getElementById('impRenunciadoRes'));
            document.getElementById('impRenunciadoRes').removeAttribute("style");
            document.getElementById('impRenunciadoRes').style.textAlign = 'right';
        }
        
        //Primer Pago
        v = document.getElementById('impPagado').value;
        v = reemplazarTextoCme(v, /,/g, '.');
        f = parseFloat(v);
        f = ajustarDecimalesCme(f, 2);
        if(!isNaN(f)){
            document.getElementById('impPagado').value = f;
            reemplazarPuntosCme(document.getElementById('impPagado'));
            document.getElementById('impPagado').removeAttribute("style");
            document.getElementById('impPagado').style.textAlign = 'right';
        }
        
        //Primer Pago Resolucion
        /*v = document.getElementById('impPagadoRes').value;
        v = reemplazarTextoCme(v, /,/g, '.');
        f = parseFloat(v);
        f = ajustarDecimalesCme(f, 2);
        if(!isNaN(f)){
            document.getElementById('impPagadoRes').value = f;
            reemplazarPuntosCme(document.getElementById('impPagadoRes'));
            document.getElementById('impPagadoRes').removeAttribute("style");
            document.getElementById('impPagadoRes').style.textAlign = 'right';
        }*/
        
        //Segundo Pago
        v = document.getElementById('impPagado2').value;
        v = reemplazarTextoCme(v, /,/g, '.');
        f = parseFloat(v);
        f = ajustarDecimalesCme(f, 2);
        if(!isNaN(f)){
            document.getElementById('impPagado2').value = f;
            reemplazarPuntosCme(document.getElementById('impPagado2'));
            document.getElementById('impPagado2').removeAttribute("style");
            document.getElementById('impPagado2').style.textAlign = 'right';
        }
        
        //Importe justificado
        
        v = document.getElementById('impJustificado').value;
        v = reemplazarTextoCme(v, /,/g, '.');
        f = parseFloat(v);
        f = ajustarDecimalesCme(f, 2);
        if(!isNaN(f)){
            document.getElementById('impJustificado').value = f;
            reemplazarPuntosCme(document.getElementById('impJustificado'));
            document.getElementById('impJustificado').removeAttribute("style");
            document.getElementById('impJustificado').style.textAlign = 'right';
        }
        
        //Importe no justificado
        v = document.getElementById('impNoJustif').value;
        v = reemplazarTextoCme(v, /,/g, '.');
        f = parseFloat(v);
        f = ajustarDecimalesCme(f, 2);
        if(!isNaN(f)){
            document.getElementById('impNoJustif').value = f;
            reemplazarPuntosCme(document.getElementById('impNoJustif'));
            document.getElementById('impNoJustif').removeAttribute("style");
            document.getElementById('impNoJustif').style.textAlign = 'right';
        }
        
        //Importe a reintegrar
        v = document.getElementById('impReintegrar').value;
        v = reemplazarTextoCme(v, /,/g, '.');
        f = parseFloat(v);
        f = ajustarDecimalesCme(f, 2);
        if(!isNaN(f)){
            document.getElementById('impReintegrar').value = f;
            reemplazarPuntosCme(document.getElementById('impReintegrar'));
            document.getElementById('impReintegrar').removeAttribute("style");
            document.getElementById('impReintegrar').style.textAlign = 'right';
        }
        
        //Otr. ayudas (solicitado)
        v = document.getElementById('otrasAyudasSolic').value;
        v = reemplazarTextoCme(v, /,/g, '.');
        f = parseFloat(v);
        f = ajustarDecimalesCme(f, 2);
        if(!isNaN(f)){
            document.getElementById('otrasAyudasSolic').value = f;
            reemplazarPuntosCme(document.getElementById('otrasAyudasSolic'));
            document.getElementById('otrasAyudasSolic').removeAttribute("style");
            document.getElementById('otrasAyudasSolic').style.textAlign = 'right';
        }
        
        //Otr. ayudas (concedido)
        v = document.getElementById('otrasAyudasConce').value;
        v = reemplazarTextoCme(v, /,/g, '.');
        f = parseFloat(v);
        f = ajustarDecimalesCme(f, 2);
        if(!isNaN(f)){
            document.getElementById('otrasAyudasConce').value = f;
            reemplazarPuntosCme(document.getElementById('otrasAyudasConce'));
            document.getElementById('otrasAyudasConce').removeAttribute("style");
            document.getElementById('otrasAyudasConce').style.textAlign = 'right';
        }
        
        //Minimis (solicitado)
        v = document.getElementById('minimisSolic').value;
        v = reemplazarTextoCme(v, /,/g, '.');
        f = parseFloat(v);
        f = ajustarDecimalesCme(f, 2);
        if(!isNaN(f)){
            document.getElementById('minimisSolic').value = f;
            reemplazarPuntosCme(document.getElementById('minimisSolic'));
            document.getElementById('minimisSolic').removeAttribute("style");
            document.getElementById('minimisSolic').style.textAlign = 'right';
        }
        
        //Minimis (concedido)
        v = document.getElementById('minimisConce').value;
        v = reemplazarTextoCme(v, /,/g, '.');
        f = parseFloat(v);
        f = ajustarDecimalesCme(f, 2);
        if(!isNaN(f)){
            document.getElementById('minimisConce').value = f;
            reemplazarPuntosCme(document.getElementById('minimisConce'));
            document.getElementById('minimisConce').removeAttribute("style");
            document.getElementById('minimisConce').style.textAlign = 'right';
        }
        
        //Importe convocatoria
        v = document.getElementById('impConvocatoria').value;
        v = reemplazarTextoCme(v, /,/g, '.');
        f = parseFloat(v);
        f = ajustarDecimalesCme(f, 2);
        if(!isNaN(f)){
            document.getElementById('impConvocatoria').value = f;
            reemplazarPuntosCme(document.getElementById('impConvocatoria'));
            document.getElementById('impConvocatoria').removeAttribute("style");
            document.getElementById('impConvocatoria').style.textAlign = 'right';
        }
        
        comprobarImporteConcedido();
        comprobarImportePagado();
        comprobarImportePagado2();
    }
    
    function mostrarOtrasAyudas(){
        var check = document.getElementById('checkOtrasAyudas');
        if(check.checked){
            document.getElementById('divLabelOtrasAyudasSolic').style.display = 'inline';
            document.getElementById('divTxtOtrasAyudasSolic').style.display = 'inline';
            document.getElementById('divLabelOtrasAyudasConce').style.display = 'inline';
            document.getElementById('divTxtOtrasAyudasConce').style.display = 'inline';
        }else{
            document.getElementById('divLabelOtrasAyudasSolic').style.display = 'none';
            document.getElementById('divTxtOtrasAyudasSolic').style.display = 'none';
            document.getElementById('divLabelOtrasAyudasConce').style.display = 'none';
            document.getElementById('divTxtOtrasAyudasConce').style.display = 'none';
        }
    }
    
    function mostrarEnlaces(){
        <%
            if(tramRes == true)
            {
        %>
                document.getElementById('linkImpRes').style.display = 'inline';
        <%
            }
            else
            {
        %>
                document.getElementById('linkImpRes').style.display = 'none';
        <%
            }
        %>
        
    
        <%
            if(tramResModif == true)
            {
        %>
                document.getElementById('linkImpResModif').style.display = 'inline';
        <%
            }
            else
            {
        %>
                document.getElementById('linkImpResModif').style.display = 'none';
        <%
            }
        %>
    }
    
    function actualizarPestanaDatosSuplementarios(){
        try{
           // if(estanCargadosCamposSuplementarios()){
                var ajax = getXMLHttpRequest();
                var nodos = null;
                var CONTEXT_PATH = '<%=request.getContextPath()%>'
                var url = CONTEXT_PATH + '/PeticionModuloIntegracion.do';
                var parametros = '';
                var control = new Date();
                parametros = 'tarea=preparar&modulo=MELANBIDE46&operacion=getTodosCamposSuplementarios&tipo=0&numero=<%=numExpediente%>&control='+control.getTime();
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
                    var nodoCalculos;
                    var hijosCalculos;
                    var importes = new Array();
                    var puestos = new Array();
                    var otros = new Array();
                    var nodoCampo;
                    for(j=0;hijos!=null && j<hijos.length;j++){
                        if(hijos[j].nodeName=="CODIGO_OPERACION"){                            
                            codigoOperacion = hijos[j].childNodes[0].nodeValue;
                        }else if(hijos[j].nodeName=="IMPORTES"){
                            nodoCalculos = hijos[j];
                            hijosCalculos = nodoCalculos.childNodes;
                            for(var cont = 0; cont < hijosCalculos.length; cont++){
                                if(hijosCalculos[cont].nodeName=="IMP_SOL"){
                                    nodoCampo = hijosCalculos[cont];
                                    if(nodoCampo.childNodes.length > 0){
                                        importes[0] = nodoCampo.childNodes[0].nodeValue;
                                    }
                                    else{
                                        importes[0] = '-';
                                    }
                                }
                                else if(hijosCalculos[cont].nodeName=="IMP_CONV"){
                                    nodoCampo = hijosCalculos[cont];
                                    if(nodoCampo.childNodes.length > 0){
                                        importes[1] = nodoCampo.childNodes[0].nodeValue;
                                    }
                                    else{
                                        importes[1] = '-';
                                    }
                                }
                                else if(hijosCalculos[cont].nodeName=="IMP_PREV_CON"){
                                    nodoCampo = hijosCalculos[cont];
                                    if(nodoCampo.childNodes.length > 0){
                                        importes[2] = nodoCampo.childNodes[0].nodeValue;
                                    }
                                    else{
                                        importes[2] = '-';
                                    }
                                }
                                else if(hijosCalculos[cont].nodeName=="IMP_CON"){
                                    nodoCampo = hijosCalculos[cont];
                                    if(nodoCampo.childNodes.length > 0){
                                        importes[3] = nodoCampo.childNodes[0].nodeValue;
                                    }
                                    else{
                                        importes[3] = '-';
                                    }
                                }
                                else if(hijosCalculos[cont].nodeName=="IMP_JUS"){
                                    nodoCampo = hijosCalculos[cont];
                                    if(nodoCampo.childNodes.length > 0){
                                        importes[4] = nodoCampo.childNodes[0].nodeValue;
                                    }
                                    else{
                                        importes[4] = '-';
                                    }
                                }
                                else if(hijosCalculos[cont].nodeName=="IMP_REN"){
                                    nodoCampo = hijosCalculos[cont];
                                    if(nodoCampo.childNodes.length > 0){
                                        importes[5] = nodoCampo.childNodes[0].nodeValue;
                                    }
                                    else{
                                        importes[5] = '-';
                                    }
                                }
                                else if(hijosCalculos[cont].nodeName=="IMP_PAG"){
                                    nodoCampo = hijosCalculos[cont];
                                    if(nodoCampo.childNodes.length > 0){
                                        importes[6] = nodoCampo.childNodes[0].nodeValue;
                                    }
                                    else{
                                        importes[6] = '-';
                                    }
                                }
                                else if(hijosCalculos[cont].nodeName=="IMP_PAG_2"){
                                    nodoCampo = hijosCalculos[cont];
                                    if(nodoCampo.childNodes.length > 0){
                                        importes[7] = nodoCampo.childNodes[0].nodeValue;
                                    }
                                    else{
                                        importes[7] = '-';
                                    }
                                }
                                else if(hijosCalculos[cont].nodeName=="IMP_REI"){
                                    nodoCampo = hijosCalculos[cont];
                                    if(nodoCampo.childNodes.length > 0){
                                        importes[8] = nodoCampo.childNodes[0].nodeValue;
                                    }
                                    else{
                                        importes[8] = '-';
                                    }
                                }
                                else if(hijosCalculos[cont].nodeName=="OTRAS_AYUDAS_SOLIC"){
                                    nodoCampo = hijosCalculos[cont];
                                    if(nodoCampo.childNodes.length > 0){
                                        importes[9] = nodoCampo.childNodes[0].nodeValue;
                                    }
                                    else{
                                        importes[9] = '-';
                                    }
                                }
                                else if(hijosCalculos[cont].nodeName=="OTRAS_AYUDAS_CONCE"){
                                    nodoCampo = hijosCalculos[cont];
                                    if(nodoCampo.childNodes.length > 0){
                                        importes[10] = nodoCampo.childNodes[0].nodeValue;
                                    }
                                    else{
                                        importes[10] = '-';
                                    }
                                }
                                else if(hijosCalculos[cont].nodeName=="MINIMIS_SOLIC"){
                                    nodoCampo = hijosCalculos[cont];
                                    if(nodoCampo.childNodes.length > 0){
                                        importes[11] = nodoCampo.childNodes[0].nodeValue;
                                    }
                                    else{
                                        importes[11] = '-';
                                    }
                                }
                                else if(hijosCalculos[cont].nodeName=="MINIMIS_CONCE"){
                                    nodoCampo = hijosCalculos[cont];
                                    if(nodoCampo.childNodes.length > 0){
                                        importes[12] = nodoCampo.childNodes[0].nodeValue;
                                    }
                                    else{
                                        importes[12] = '-';
                                    }
                                }
                                else if(hijosCalculos[cont].nodeName=="IMP_DESP"){
                                    nodoCampo = hijosCalculos[cont];
                                    if(nodoCampo.childNodes.length > 0){
                                        importes[13] = nodoCampo.childNodes[0].nodeValue;
                                    }
                                    else{
                                        importes[13] = '-';
                                    }
                                }
                                else if(hijosCalculos[cont].nodeName=="IMP_BAJA"){
                                    nodoCampo = hijosCalculos[cont];
                                    if(nodoCampo.childNodes.length > 0){
                                        importes[14] = nodoCampo.childNodes[0].nodeValue;
                                    }
                                    else{
                                        importes[14] = '-';
                                    }
                                }
                                else if(hijosCalculos[cont].nodeName=="CONCEDIDO_REAL"){
                                    nodoCampo = hijosCalculos[cont];
                                    if(nodoCampo.childNodes.length > 0){
                                        importes[15] = nodoCampo.childNodes[0].nodeValue;
                                    }
                                    else{
                                        importes[15] = '-';
                                    }
                                }
                                else if(hijosCalculos[cont].nodeName=="PAGADO_REAL"){
                                    nodoCampo = hijosCalculos[cont];
                                    if(nodoCampo.childNodes.length > 0){
                                        importes[16] = nodoCampo.childNodes[0].nodeValue;
                                    }
                                    else{
                                        importes[16] = '-';
                                    }
                                }
                                else if(hijosCalculos[cont].nodeName=="PAGADO_REAL_2"){
                                    nodoCampo = hijosCalculos[cont];
                                    if(nodoCampo.childNodes.length > 0){
                                        importes[17] = nodoCampo.childNodes[0].nodeValue;
                                    }
                                    else{
                                        importes[17] = '-';
                                    }
                                }
                                else if(hijosCalculos[cont].nodeName=="IMP_BONIF"){
                                    nodoCampo = hijosCalculos[cont];
                                    if(nodoCampo.childNodes.length > 0){
                                        importes[18] = nodoCampo.childNodes[0].nodeValue;
                                    }
                                    else{
                                        importes[18] = '-';
                                    }
                                }
                                else if(hijosCalculos[cont].nodeName=="IMP_NO_JUS"){
                                    nodoCampo = hijosCalculos[cont];
                                    if(nodoCampo.childNodes.length > 0){
                                        importes[19] = nodoCampo.childNodes[0].nodeValue;
                                    }
                                    else{
                                        importes[19] = '-';
                                    }
                                }
                                else if(hijosCalculos[cont].nodeName=="IMP_REN_RES"){
                                    nodoCampo = hijosCalculos[cont];
                                    if(nodoCampo.childNodes.length > 0){
                                        importes[20] = nodoCampo.childNodes[0].nodeValue;
                                    }
                                    else{
                                        importes[20] = '-';
                                    }
                                }
                                else if(hijosCalculos[cont].nodeName=="IMP_PAG_RES"){
                                    nodoCampo = hijosCalculos[cont];
                                    if(nodoCampo.childNodes.length > 0){
                                        importes[21] = nodoCampo.childNodes[0].nodeValue;
                                    }
                                    else{
                                        importes[21] = '-';
                                    }
                                }
                            }
                        }else if(hijos[j].nodeName=="PUESTOS"){
                            nodoCalculos = hijos[j];
                            hijosCalculos = nodoCalculos.childNodes;
                            for(var cont = 0; cont < hijosCalculos.length; cont++){
                                if(hijosCalculos[cont].nodeName=="PUESTOS_SOLICITADOS"){
                                    nodoCampo = hijosCalculos[cont];
                                    if(nodoCampo.childNodes.length > 0){
                                        puestos[0] = nodoCampo.childNodes[0].nodeValue;
                                    }
                                    else{
                                        puestos[0] = '-';
                                    }
                                }
                                else if(hijosCalculos[cont].nodeName=="PUESTOS_DENEGADOS"){
                                    nodoCampo = hijosCalculos[cont];
                                    if(nodoCampo.childNodes.length > 0){
                                        puestos[1] = nodoCampo.childNodes[0].nodeValue;
                                    }
                                    else{
                                        puestos[1] = '-';
                                    }
                                }
                                else if(hijosCalculos[cont].nodeName=="PUESTOS_CONTRATADOS"){
                                    nodoCampo = hijosCalculos[cont];
                                    if(nodoCampo.childNodes.length > 0){
                                        puestos[2] = nodoCampo.childNodes[0].nodeValue;
                                    }
                                    else{
                                        puestos[2] = '-';
                                    }
                                }
                                else if(hijosCalculos[cont].nodeName=="PERSONAS_CONTRATADAS"){
                                    nodoCampo = hijosCalculos[cont];
                                    if(nodoCampo.childNodes.length > 0){
                                        puestos[3] = nodoCampo.childNodes[0].nodeValue;
                                    }
                                    else{
                                        puestos[3] = '-';
                                    }
                                }
                                else if(hijosCalculos[cont].nodeName=="PUESTOS_DESPIDO"){
                                    nodoCampo = hijosCalculos[cont];
                                    if(nodoCampo.childNodes.length > 0){
                                        puestos[4] = nodoCampo.childNodes[0].nodeValue;
                                    }
                                    else{
                                        puestos[4] = '-';
                                    }
                                }
                                else if(hijosCalculos[cont].nodeName=="PERSONAS_DESPIDO"){
                                    nodoCampo = hijosCalculos[cont];
                                    if(nodoCampo.childNodes.length > 0){
                                        puestos[5] = nodoCampo.childNodes[0].nodeValue;
                                    }
                                    else{
                                        puestos[5] = '-';
                                    }
                                }
                                else if(hijosCalculos[cont].nodeName=="PUESTOS_BAJA"){
                                    nodoCampo = hijosCalculos[cont];
                                    if(nodoCampo.childNodes.length > 0){
                                        puestos[6] = nodoCampo.childNodes[0].nodeValue;
                                    }
                                    else{
                                        puestos[6] = '-';
                                    }
                                }
                                else if(hijosCalculos[cont].nodeName=="PERSONAS_BAJA"){
                                    nodoCampo = hijosCalculos[cont];
                                    if(nodoCampo.childNodes.length > 0){
                                        puestos[7] = nodoCampo.childNodes[0].nodeValue;
                                    }
                                    else{
                                        puestos[7] = '-';
                                    }
                                }
                                else if(hijosCalculos[cont].nodeName=="PUESTOS_RENUNCIADOS"){
                                    nodoCampo = hijosCalculos[cont];
                                    if(nodoCampo.childNodes.length > 0){
                                        puestos[8] = nodoCampo.childNodes[0].nodeValue;
                                    }
                                    else{
                                        puestos[8] = '-';
                                    }
                                }
                            }
                        }else if(hijos[j].nodeName=="OTROS"){
                            nodoCalculos = hijos[j];
                            hijosCalculos = nodoCalculos.childNodes;
                            for(var cont = 0; cont < hijosCalculos.length; cont++){
                                if(hijosCalculos[cont].nodeName=="GESTOR_TRAMITADOR"){
                                    nodoCampo = hijosCalculos[cont];
                                    if(nodoCampo.childNodes.length > 0){
                                        otros[0] = nodoCampo.childNodes[0].nodeValue;
                                    }
                                    else{
                                        otros[0] = '-';
                                    }
                                }
                                else if(hijosCalculos[cont].nodeName=="EMPRESA"){
                                    nodoCampo = hijosCalculos[cont];
                                    if(nodoCampo.childNodes.length > 0){
                                        otros[1] = nodoCampo.childNodes[0].nodeValue;
                                    }
                                    else{
                                        otros[1] = '-';
                                    }
                                }
                            }
                        }
                    }
                }catch(err){

                }

                if(importes){
                    try{
                        document.getElementById('<%=ConfigurationParameter.getParameter(ConstantesMeLanbide46.CAMPO_SUPL_IMP_CON, ConstantesMeLanbide46.FICHERO_PROPIEDADES)%>').value = importes[3];
                    }catch(err){
                    }
                    try{
                        document.getElementById('<%=ConfigurationParameter.getParameter(ConstantesMeLanbide46.CAMPO_SUPL_IMP_PAG, ConstantesMeLanbide46.FICHERO_PROPIEDADES)%>').value = importes[6];
                    }catch(err){
                    }
                    try{
                        document.getElementById('<%=ConfigurationParameter.getParameter(ConstantesMeLanbide46.CAMPO_SUPL_OTRAS_AYUDAS_CONCE, ConstantesMeLanbide46.FICHERO_PROPIEDADES)%>').value = importes[10];
                    }catch(err){
                    }
                    try{
                        document.getElementById('<%=ConfigurationParameter.getParameter(ConstantesMeLanbide46.CAMPO_SUPL_IMP_SOLICITADO, ConstantesMeLanbide46.FICHERO_PROPIEDADES)%>').value = importes[0];
                    }catch(err){
                    }
                    try{
                        document.getElementById('<%=ConfigurationParameter.getParameter(ConstantesMeLanbide46.CAMPO_SUPL_IMP_CONVOCATORIA, ConstantesMeLanbide46.FICHERO_PROPIEDADES)%>').value = importes[1];
                    }catch(err){
                    }
                    try{
                        document.getElementById('<%=ConfigurationParameter.getParameter(ConstantesMeLanbide46.CAMPO_SUPL_IMP_PREV_CON, ConstantesMeLanbide46.FICHERO_PROPIEDADES)%>').value = importes[2];
                    }catch(err){
                    }
                    try{
                        document.getElementById('<%=ConfigurationParameter.getParameter(ConstantesMeLanbide46.CAMPO_SUPL_IMP_JUS, ConstantesMeLanbide46.FICHERO_PROPIEDADES)%>').value = importes[4];
                    }catch(err){
                    }
                    try{
                        document.getElementById('<%=ConfigurationParameter.getParameter(ConstantesMeLanbide46.CAMPO_SUPL_IMP_REN, ConstantesMeLanbide46.FICHERO_PROPIEDADES)%>').value = importes[5];
                    }catch(err){
                    }
                    try{
                        document.getElementById('<%=ConfigurationParameter.getParameter(ConstantesMeLanbide46.CAMPO_SUPL_IMP_PAG_2, ConstantesMeLanbide46.FICHERO_PROPIEDADES)%>').value = importes[7];
                    }catch(err){
                    }
                    try{
                        document.getElementById('<%=ConfigurationParameter.getParameter(ConstantesMeLanbide46.CAMPO_SUPL_IMP_REI, ConstantesMeLanbide46.FICHERO_PROPIEDADES)%>').value = importes[8];
                    }catch(err){
                    }
                    try{
                        document.getElementById('<%=ConfigurationParameter.getParameter(ConstantesMeLanbide46.CAMPO_SUPL_OTRAS_AYUDAS_SOLIC, ConstantesMeLanbide46.FICHERO_PROPIEDADES)%>').value = importes[9];
                    }catch(err){
                    }
                    try{
                        document.getElementById('<%=ConfigurationParameter.getParameter(ConstantesMeLanbide46.CAMPO_SUPL_MINIMIS_SOLIC, ConstantesMeLanbide46.FICHERO_PROPIEDADES)%>').value = importes[11];
                    }catch(err){
                    }
                    try{
                        document.getElementById('<%=ConfigurationParameter.getParameter(ConstantesMeLanbide46.CAMPO_SUPL_MINIMIS_CONCE, ConstantesMeLanbide46.FICHERO_PROPIEDADES)%>').value = importes[12];
                    }catch(err){
                    }
                    try{
                        document.getElementById('<%=ConfigurationParameter.getParameter(ConstantesMeLanbide46.CAMPO_SUPL_IMP_NO_JUS, ConstantesMeLanbide46.FICHERO_PROPIEDADES)%>').value = importes[19];
                    }catch(err){
                    }
                    try{
                        document.getElementById('<%=ConfigurationParameter.getParameter(ConstantesMeLanbide46.CAMPO_SUPL_IMP_REN_RES, ConstantesMeLanbide46.FICHERO_PROPIEDADES)%>').value = importes[20];
                    }catch(err){
                    }
                    try{
                        document.getElementById('<%=ConfigurationParameter.getParameter(ConstantesMeLanbide46.CAMPO_SUPL_IMP_PAG_RES, ConstantesMeLanbide46.FICHERO_PROPIEDADES)%>').value = importes[21];
                    }catch(err){
                    }
                }

                if(puestos){
                    try{
                        document.getElementById('<%=ConfigurationParameter.getParameter(ConstantesMeLanbide46.CAMPO_SUPL_PUESTOS_SOLICITADOS, ConstantesMeLanbide46.FICHERO_PROPIEDADES)%>').value = puestos[0];
                    }catch(err){
                    }
                    try{
                        document.getElementById('<%=ConfigurationParameter.getParameter(ConstantesMeLanbide46.CAMPO_SUPL_PUESTOS_DENEGADOS, ConstantesMeLanbide46.FICHERO_PROPIEDADES)%>').value = puestos[1];
                    }catch(err){
                    }
                    try{
                        document.getElementById('<%=ConfigurationParameter.getParameter(ConstantesMeLanbide46.CAMPO_SUPL_PUESTOS_CONTRATADOS, ConstantesMeLanbide46.FICHERO_PROPIEDADES)%>').value = puestos[2];
                    }catch(err){
                    }
                    try{
                        document.getElementById('<%=ConfigurationParameter.getParameter(ConstantesMeLanbide46.CAMPO_SUPL_PERSONAS_CONTRATADAS, ConstantesMeLanbide46.FICHERO_PROPIEDADES)%>').value = puestos[3];
                    }catch(err){
                    }
                    try{
                        document.getElementById('<%=ConfigurationParameter.getParameter(ConstantesMeLanbide46.CAMPO_SUPL_PUESTOS_DESPIDO, ConstantesMeLanbide46.FICHERO_PROPIEDADES)%>').value = puestos[4];
                    }catch(err){
                    }
                    try{
                        document.getElementById('<%=ConfigurationParameter.getParameter(ConstantesMeLanbide46.CAMPO_SUPL_PERSONAS_DESPIDO, ConstantesMeLanbide46.FICHERO_PROPIEDADES)%>').value = puestos[5];
                    }catch(err){
                    }
                    try{
                        document.getElementById('<%=ConfigurationParameter.getParameter(ConstantesMeLanbide46.CAMPO_SUPL_PUESTOS_BAJA, ConstantesMeLanbide46.FICHERO_PROPIEDADES)%>').value = puestos[6];
                    }catch(err){
                    }
                    try{
                        document.getElementById('<%=ConfigurationParameter.getParameter(ConstantesMeLanbide46.CAMPO_SUPL_PERSONAS_BAJA, ConstantesMeLanbide46.FICHERO_PROPIEDADES)%>').value = puestos[7];
                    }catch(err){
                    }
                    try{
                        document.getElementById('<%=ConfigurationParameter.getParameter(ConstantesMeLanbide46.CAMPO_SUPL_PUESTOS_RENUNCIADOS, ConstantesMeLanbide46.FICHERO_PROPIEDADES)%>').value = puestos[8];
                    }catch(err){
                    }
                }

                if(otros){
                    try{
                        document.getElementById('<%=ConfigurationParameter.getParameter(ConstantesMeLanbide46.CAMPO_SUPL_GESTOR_TRAMITADOR, ConstantesMeLanbide46.FICHERO_PROPIEDADES)%>').value = otros[0];
                    }catch(err){
                    }
                    try{
                        document.getElementById('<%=ConfigurationParameter.getParameter(ConstantesMeLanbide46.CAMPO_SUPL_EMPRESA, ConstantesMeLanbide46.FICHERO_PROPIEDADES)%>').value = otros[1];
                    }catch(err){
                    }
                }
            //} //FIN IF ESTAN CARGADOS
        }catch(err){
        
        }
     }
     
     function consultarImportesResolucion(res){
        try{
            if(res != undefined && res != ''){
                var control = new Date();
                if(navigator.appName.indexOf("Internet Explorer")!=-1){
                    lanzarPopUpModal('<%=request.getContextPath()%>/PeticionModuloIntegracion.do?tarea=preparar&modulo=MELANBIDE46&operacion=cargarImportesResolucion&res='+res+'&tipo=0&numero=<%=numExpediente%>&control='+control.getTime(),380,395,'yes','no', function(result){});
                }else{
                    lanzarPopUpModal('<%=request.getContextPath()%>/PeticionModuloIntegracion.do?tarea=preparar&modulo=MELANBIDE46&operacion=cargarImportesResolucion&res='+res+'&tipo=0&numero=<%=numExpediente%>&control='+control.getTime(),400,440,'yes','no', function(result){});
                }
            }else{
                //No se pueden cargar los datos
                jsp_alerta("A",'<%=meLanbide46I18n.getMensaje(idiomaUsuario,"error.cargarDatosResolucion")%>');
            }
        }catch(err){
            
        }
     }
     
     function consultarHistoricos(res){
        try{
            var control = new Date();
            if(navigator.appName.indexOf("Internet Explorer")!=-1){
                lanzarPopUpModal('<%=request.getContextPath()%>/PeticionModuloIntegracion.do?tarea=preparar&modulo=MELANBIDE46&operacion=cargarHistorico&tipo=0&tram='+res+'&numero=<%=numExpediente%>&control='+control.getTime(),380,950,'yes','no', function(result){});
            }else{            
                lanzarPopUpModal('<%=request.getContextPath()%>/PeticionModuloIntegracion.do?tarea=preparar&modulo=MELANBIDE46&operacion=cargarHistorico&tipo=0&tram='+res+'&numero=<%=numExpediente%>&control='+control.getTime(),400,950,'yes','no', function(result){});
            }
        }catch(err){
            
        }
     }
</script>

<body>
    <div id="barraProgresoSolicitudCme" style="position: absolute; z-index:10; visibility: hidden; top: 30%; left: 30%;">
        <table width="100%" border="0px" cellpadding="0px" cellspacing="0px" border="0px">
            <tr>
                <td align="center" valign="middle">
                    <table class="contenedorHidepage" cellpadding="0px" cellspacing="0px" border="0px">
                        <tr>
                            <td>
                                <table width="349px" height="100%">
                                    <tr>
                                        <td colspan="3" style="height:70%;text-align:center;valign:middle;">
                                            <span id="msgGuardandoDatosCme">
                                                <%=meLanbide46I18n.getMensaje(idiomaUsuario, "msg.guardandoDatos")%>
                                            </span>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td style="width:5%;height:20%;"></td>
                                        <td class="imagenHide"></td>
                                        <td style="width:5%;height:20%;"></td>
                                    </tr>
                                    <tr>
                                        <td colspan="3" style="height:10%" ></td>
                                    </tr>
                                </table>
                            </td>
                        </tr>
                    </table>
                </td>
            </tr>
        </table>
    </div>
    <div class="tab-page" id="tabPage391" style="height:480px; width: 100%;">
        <h2 class="tab" id="pestana391"><%=meLanbide46I18n.getMensaje(idiomaUsuario,"label.datosCme.tituloPestana")%></h2>
        <script type="text/javascript">tp1_p391 = tp1.addTabPage( document.getElementById( "tabPage391" ) );</script>
        <div style="clear: both;">
            <div>
                <div style="clear: both; width: 100%; text-align: left; padding-left: 12px;" class="textoAzul">
                    <div class="lineaFormulario">
                        <div style="width: 97px; float: left;">
                            <%=meLanbide46I18n.getMensaje(idiomaUsuario,"label.datosCme.gestorTramitador")%>
                        </div>
                        <div style="width: 259px; float: left;">
                            <input id="codGestorTramitador" name="codGestorTramitador" type="text" class="inputTexto" size="2" maxlength="3" 
                                   onkeypress="javascript:return SoloDigitosConsulta(event);" value="" >
                            <input id="descGestorTramitador" name="descGestorTramitador" type="text" class="inputTexto" size="29" readonly >
                            <a id="anchorGestorTramitador" name="anchorGestorTramitador" href=""><span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonGestorTramitador" name="botonGestorTramitador" style="cursor:hand;" alt="<%=meLanbide46I18n.getMensaje(idiomaUsuario, "altDesplegable")%>" title="<%=meLanbide46I18n.getMensaje(idiomaUsuario, "altDesplegable")%>"></span></a>
                        </div>
                        <div style="width: 51px; float: left;">
                            <%=meLanbide46I18n.getMensaje(idiomaUsuario,"label.datosCme.empresa")%>
                        </div>
                        <div style="width: 505px; float: left;">
                            <input type="text" class="inputTexto" size="80" maxlength="500" id="empresa" name="empresa" value="" style=""/>
                        </div>
                    </div>
                        <hr style="width: 98%; float: left; margin-top: 5px; margin-bottom: 5px;"/>
                    <div class="lineaFormulario">
                        <div style="width: 105px; float: left;">
                            <%=meLanbide46I18n.getMensaje(idiomaUsuario,"label.datosCme.impSolicitado")%>
                        </div>
                        <div style="width: 103px; float: left;">
                            <input type="text" class="inputTexto readOnly" size="9" maxlength="11" id="impSolicitado" name="impSolicitado" value="" style="text-align: right;" readonly="true"/>
                        </div>
                        <div style="width: 105px; float: left;">
                            <%=meLanbide46I18n.getMensaje(idiomaUsuario,"label.datosCme.impPrevistoConcesion")%>
                        </div>
                        <div style="width: 110px; float: left;">
                            <input type="text" class="inputTexto readOnly" size="9" maxlength="11" id="impPrevistoConcesion" name="impPrevistoConcesion" value="" style="text-align: right;" readonly="true"/>
                        </div>
                        <div style="width: 105px; float: left;">
                            <%=meLanbide46I18n.getMensaje(idiomaUsuario,"label.datosCme.impConcedido")%>
                        </div>
                        <div style="width: 80px; float: left;">
                            <input type="text" class="inputTexto" size="9" maxlength="11" id="impConcedido" name="impConcedido" value="" style="text-align: right;" onchange="reemplazarPuntosCme(this);calculaImporteNoJusti();" onblur="comprobarImporteConcedido();importeModificado();calculaImporteNoJusti();"/>
                        </div>
                        <div style="width: 30px; float: left; padding-top: 2px;">
                            <a href="#" onclick="mostrarImpConCalculado(event);return false;" style="text-decoration:none;" >
                                <img style="border: 0" height="17" id="calcularImpCon" name="calcularImpCon" alt="<%=meLanbide46I18n.getMensaje(idiomaUsuario,"label.datosCme.calcularImporteConcedido")%>" src="<c:url value='/images/extension/melanbide46/calculadora.png'/>"/>
                            </a>
                        </div>
                        <div style="width: 105px; float: left;">
                            <%=meLanbide46I18n.getMensaje(idiomaUsuario,"label.datosCme.impRenunciadoSol")%>
                        </div>
                        <div style="width: 103px; float: left;">
                            <input type="text" class="inputTexto readOnly" size="9" maxlength="11" id="impRenunciado" name="impRenunciado" value="" style="text-align: right;" readonly="true"/>
                        </div>
                    </div>
                    <div class="lineaFormulario">
                        <div style="width: 105px; float: left;">
                            <%=meLanbide46I18n.getMensaje(idiomaUsuario,"label.datosCme.primerPago")%>
                        </div>
                        <div style="width: 80px; float: left;">
                            <input type="text" value="" class="inputTexto" size="9" maxlength="11" id="impPagado" name="impPagado" value="" style="text-align: right;" onchange="reemplazarPuntosCme(this);" onblur="comprobarImportePagado();importeModificado();"/>
                        </div>
                        <div style="width: 23px; float: left; padding-top: 2px;">
                            <a href="#" onclick="mostrarImpPagCalculado(event);return false;" style="text-decoration:none;" >
                                <img style="border: 0" height="17" id="calcularImpPag" name="calcularImpPag" alt="<%=meLanbide46I18n.getMensaje(idiomaUsuario,"label.datosCme.calcularImportePagado")%>" src="<c:url value='/images/extension/melanbide46/calculadora.png'/>"/>
                            </a>
                        </div>
                        <div style="width: 105px; float: left;">
                            <%=meLanbide46I18n.getMensaje(idiomaUsuario,"label.datosCme.segundoPago")%>
                        </div>
                        <div style="width: 80px; float: left;">
                            <input type="text" class="inputTexto" size="9" maxlength="11" id="impPagado2" name="impPagado2" value="" style="text-align: right;" onchange="reemplazarPuntosCme(this);"/>
                        </div>
                        <div style="width: 30px; float: left; padding-top: 2px;">
                            <a href="#" onclick="mostrarImpPag2Calculado(event);return false;" style="text-decoration:none;" >
                                <img style="border: 0" height="17" id="calcularImpPag2" name="calcularImpPag2" alt="<%=meLanbide46I18n.getMensaje(idiomaUsuario,"label.datosCme.calcularImportePagado2")%>" src="<c:url value='/images/extension/melanbide46/calculadora.png'/>"/>
                            </a>
                        </div>
                        <div style="width: 105px; float: left;">
                            <%=meLanbide46I18n.getMensaje(idiomaUsuario,"label.datosCme.impJustificado")%>
                        </div>
                        <div style="width: 110px; float: left;">
                            <input type="text" class="inputTexto readOnly" size="9" maxlength="11" id="impJustificado" name="impJustificado" value="" style="text-align: right;" readonly="true"/>
                        </div>
                        <div style="width: 105px; float: left;">
                            <%=meLanbide46I18n.getMensaje(idiomaUsuario,"label.datosCme.impRenunciadoRes")%>
                        </div>
                        <div style="width: 103px; float: left;">
                            <input type="text" class="inputTexto readOnly" size="9" maxlength="11" id="impRenunciadoRes" name="impRenunciadoRes" value="" style="text-align: right;" readonly="true"/>
                        </div>
                    </div>
                    <div class="lineaFormulario">
                        
                        <div style="width: 100px; float: left; margin-right: 5px; margin-left: 423px;">
                            <%=meLanbide46I18n.getMensaje(idiomaUsuario,"label.datosCme.impNoJustif")%>
                        </div>
                        <div style="width: 110px; float: left;">
                            <input type="text" value="" class="inputTexto readOnly" size="9" maxlength="11" id="impNoJustif" name="impNoJustif" value="" style="text-align: right;" readonly="true"/>
                        </div>
                        <div style="width: 105px; float: left;">
                            <%=meLanbide46I18n.getMensaje(idiomaUsuario,"label.datosCme.impReintegrar")%>
                        </div>
                        <div style="width: 110px; float: left;">
                            <input type="text" class="inputTexto readOnly" size="9" maxlength="11" id="impReintegrar" name="impReintegrar" value="" style="text-align: right;" readonly="true"/>
                        </div>
                    </div>
                    <div class="lineaFormulario">
                        <div style="width: 208px; float: left; padding-bottom: 10px;">
                            <input type="checkBox" id="checkOtrasAyudas" name="checkOtrasAyudas" onclick="mostrarOtrasAyudas();"/><%=meLanbide46I18n.getMensaje(idiomaUsuario,"label.datosCme.mostrarOtrasAyudas")%>
                        </div>
                        <div style="float: right; width: 492px;">
                            <div style="width: 105px; float: left;margin-left: -90px;">
                                <%=meLanbide46I18n.getMensaje(idiomaUsuario,"label.datosCme.minimisSolic")%>
                            </div>
                            <div style="width: 110px; float: left;">
                                <input type="text" class="inputTexto" size="9" maxlength="11" id="minimisSolic" name="minimisSolic" value="" style="text-align: right;" onchange="reemplazarPuntosCme(this);"/>
                            </div>
                            <div style="width: 105px; float: left;">
                                <%=meLanbide46I18n.getMensaje(idiomaUsuario,"label.datosCme.minimisConce")%>
                            </div>
                            <div style="width: 103px; float: left;">
                                <input type="text" class="inputTexto" size="9" maxlength="11" id="minimisConce" name="minimisConce" value="" style="text-align: right;" onchange="reemplazarPuntosCme(this);"/>
                            </div>
                        </div>
                    </div>
                    <div class="lineaFormulario" style="height: 42px;">
                        <div id="divLabelOtrasAyudasSolic" style="width: 105px; float: rigth;">
                            <%=meLanbide46I18n.getMensaje(idiomaUsuario,"label.datosCme.otrasAyudasSolic")%>
                        </div>
                        <div id="divTxtOtrasAyudasSolic" style="width: 102px; float: left;">
                            <input type="text" class="inputTexto" size="9" maxlength="11" id="otrasAyudasSolic" name="otrasAyudasSolic" value="" style="text-align: right;" onchange="reemplazarPuntosCme(this);"/>
                        </div>
                        <div id="divLabelOtrasAyudasConce" style="width: 105px; float: left;">
                            <%=meLanbide46I18n.getMensaje(idiomaUsuario,"label.datosCme.otrasAyudasConce")%>
                        </div>
                        <div id="divTxtOtrasAyudasConce" style="width: 60px; float: left;">
                            <input type="text" class="inputTexto" size="9" maxlength="11" id="otrasAyudasConce" name="otrasAyudasConce" value="" style="text-align: right;" onchange="reemplazarPuntosCme(this);" onblur="recalcularConcedidoReal();importeModificado();"/>
                        </div> 
                        <div style="float: right; width: 80%; text-align: right; margin-right: 5%">
                            <a href="#" id="linkImpRes" name="linkImpRes" onclick="consultarImportesResolucion('I');"><%=meLanbide46I18n.getMensaje(idiomaUsuario,"label.datosCme.consulImpRes")%></a>
                            &nbsp;&nbsp;
                            <a href="#" id="linkImpResModif" name="linkImpResModif" onclick="consultarImportesResolucion('M');"><%=meLanbide46I18n.getMensaje(idiomaUsuario,"label.datosCme.consulImpResModif")%></a>
<BR />
                            <a href="#" id="linkImpHist" name="linkImpHist" onclick="consultarHistoricos('3');"><%=meLanbide46I18n.getMensaje(idiomaUsuario,"label.datosCme.consultaHist")%></a>                        
&nbsp;&nbsp;&nbsp;
                            <a href="#" id="linkImpHist2" name="linkImpHist" onclick="consultarHistoricos('44');"><%=meLanbide46I18n.getMensaje(idiomaUsuario,"label.datosCme.consultaHist2")%></a>                        
                        </div>
                    </div>
                    <!--<div class="lineaFormulario">-->
                        <!--<div style="width: 105px; float: left;">
                            <%=meLanbide46I18n.getMensaje(idiomaUsuario,"label.datosCme.impDespido")%>
                        </div>
                        <div style="width: 103px; float: left;">
                            <input type="text" class="inputTexto readOnly" size="9" maxlength="11" id="impDespido" name="impDespido" value="" style="margin-left: 5px; margin-right: 20px; text-align: right;" readonly="true"/>
                        </div>-->
                        <!--<div style="width: 105px; float: left;">
                            <%=meLanbide46I18n.getMensaje(idiomaUsuario,"label.datosCme.impBaja")%>
                        </div>
                        <div style="width: 103px; float: left;">
                            <input type="text" class="inputTexto readOnly" size="9" maxlength="11" id="impBaja" name="impBaja" value="" style="margin-left: 5px; margin-right: 20px; text-align: right;" readonly="true"/>
                        </div>-->
                    <!--</div>-->
                    <div class="lineaFormulario">
                        <div style="width: 520px; float: left; text-align: right;">
                            <input type="button" id="btnGuardarDatosCme" name="btnGuardarDatosCme" class="botonGeneral" value="<%=meLanbide46I18n.getMensaje(idiomaUsuario,"btn.guardar")%>" onclick="guardarDatosCme();">
                        </div>
                        <div style="width: 390px; text-align: right; float: left;">
                            <span style="width: 110px; text-align: left; color: red;">
                                <%=meLanbide46I18n.getMensaje(idiomaUsuario,"label.datosCme.impConvocatoria")%>
                            </span>
                            <span style="width: 82px; margin-right: 89px; text-align: left;">
                                <input type="text" class="inputTexto readOnly" size="9" maxlength="11" id="impConvocatoria" name="impConvocatoria" value="" style="text-align: right;" readonly="true"/>
                            </span>
                        </div>
                    </div>
                </div>
                <div style="clear: both; width: 100%; text-align: left; padding-top: 10px;">
                    <div id="tab-panel-391" class="tab-pane" style="float: left;" align="center"></div>
                    <script type="text/javascript"> 
                        tp391 = new WebFXTabPane(document.getElementById("tab-panel-391"));
                        tp391.selectedIndex = 0;
                    </script>
                    <div class="tab-page" id="tabPage3911" style="height: 194px;">
                        <h2 class="tab" id="pestana3911" style="<%=margenIzqPestanasDatosCme%>"><%=meLanbide46I18n.getMensaje(idiomaUsuario,"label.solicitud.tituloPestana")%></h2>
                        <script type="text/javascript">tp391.addTabPage( document.getElementById( "tabPage3911" ) );</script>
                        <div style="clear: both;">
                            <jsp:include page="<%=urlPestanaDatos_solicitud%>" flush="true"/>
                        </div>
                    </div>
                    <div class="tab-page" id="tabPage3912" style="height: 194px;">
                        <h2 class="tab" id="pestana3912"><%=meLanbide46I18n.getMensaje(idiomaUsuario,"label.oferta.tituloPestana")%></h2>
                        <script type="text/javascript">tp391.addTabPage( document.getElementById( "tabPage3912" ) );</script>
                        <div style="clear: both;">
                            <jsp:include page="<%=urlPestanaDatos_oferta%>" flush="true"/>
                        </div>
                    </div>
                    <div class="tab-page" id="tabPage3913" style="height: 194px;">
                        <h2 class="tab" id="pestana3913"><%=meLanbide46I18n.getMensaje(idiomaUsuario,"label.justif.tituloPestana")%></h2>
                        <script type="text/javascript">tp391.addTabPage( document.getElementById( "tabPage3913" ) );</script>
                        <div style="clear: both;">
                            <jsp:include page="<%=urlPestanaDatos_justif%>" flush="true"/>
                        </div>
                    </div>
                    <div class="tab-page" id="tabPage3914" style="height: 194px;">
                        <h2 class="tab" id="pestana3914"><%=meLanbide46I18n.getMensaje(idiomaUsuario,"label.resumen.tituloPestana")%></h2>
                        <script type="text/javascript">tp391.addTabPage( document.getElementById( "tabPage3914" ) );</script>
                        <div style="clear: both;">
                            <jsp:include page="<%=urlPestanaDatos_resumen%>" flush="true"/>
                        </div>
                    </div>
                        
                        
                </div>
            </div>
        </div> 
    </div>
    <input type="hidden" id="concedidoReal" name="concedidoReal"/>
    <input type="hidden" id="pagadoReal" name="pagadoReal"/>
    <input type="hidden" id="pagadoReal2" name="pagadoReal2"/>
    <input type="hidden" id="importesModificados" name="importesModificados"/>
</body>

<script type="text/javascript">    
    var comboGestorTramitador = new Combo('GestorTramitador');
    
    cargarDatosCme();
    
</script>

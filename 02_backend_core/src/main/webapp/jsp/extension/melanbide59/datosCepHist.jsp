<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>

<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide59.i18n.MeLanbide59I18n" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide59.vo.SelectItem"%>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide59.util.ConfigurationParameter"%>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide59.util.ConstantesMeLanbide59"%>
<%@ page import="es.altia.common.service.config.Config"%>
<%@ page import="es.altia.common.service.config.ConfigServiceHelper"%>
<%@page import="java.util.ArrayList" %>
<%@page import="java.util.List" %>
<%@page import="org.apache.commons.lang.StringEscapeUtils"%>

<%@page import="es.altia.agora.business.util.GeneralValueObject"%>
<%@page import="es.altia.agora.interfaces.user.web.sge.FichaExpedienteForm"%>

<%
    int idiomaUsuario = 0;
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
    MeLanbide59I18n meLanbide59I18n = MeLanbide59I18n.getInstance();
    
    Config m_Config = ConfigServiceHelper.getConfig("common");
    String statusBar = m_Config.getString("JSP.StatusBar");
    String nombreModulo     = request.getParameter("nombreModulo");
    String codOrganizacion  = request.getParameter("codOrganizacionModulo");
    String numExpediente    = request.getParameter("numero");
    String mensajeProgreso = "";
    
    String urlPestanaDatos_solicitud = (String)request.getAttribute("urlPestanaDatos_solicitud");
    String urlPestanaDatos_oferta = (String)request.getAttribute("urlPestanaDatos_oferta");
    String urlPestanaDatos_justif = (String)request.getAttribute("urlPestanaDatos_justif");
    
    
        
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
    String margenIzqPestanasDatosCpe = "";

    if(userAgent.indexOf("MSIE")!=-1) {
        // Internet Explorer
        margenIzqPestanasDatosCpe = "";
    }else{
        // Firefox u otro navegador
        margenIzqPestanasDatosCpe = "margin-left:10px;";
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
%>

<%!
    // Funcion para escapar strings para javascript
    private String escape(String str) 
    {
        return StringEscapeUtils.escapeJavaScript(str);
    }
%>

<script type="text/javascript">  var APP_CONTEXT_PATH="/flexia"; </script>
<jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
<jsp:setProperty name="descriptor"  property="idi_cod" value="<%=idiomaUsuario%>" />
<jsp:setProperty name="descriptor"  property="apl_cod" value="<%=apl%>" />
<link rel="StyleSheet" media="screen" type="text/css" href="<%=request.getContextPath()%><%=css%>">
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/>
<link rel="stylesheet" type="text/css" href="<c:url value='/css/extension/melanbide59/melanbide59.css'/>">

<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/popup.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/general.js"></script>

<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/melanbide59/cpeUtils.js"></script>
<script type="text/javascript" src="<c:url value='/scripts/tabpane.js'/>"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/listaComboBox.js"></script>

<script type="text/javascript">   
    var tp391;
    var mensajeValidacion = '';
    
    //LISTAS DE VALORES PARA LOS COMBOS
    var codGestorTramitador = [<%=lcodGestorTramitador%>];
    var descGestorTramitador = [<%=ldescGestorTramitador%>];
    
    function cargarDatosCpe(){
        comboGestorTramitador.addItems(codGestorTramitador, descGestorTramitador);
        document.getElementById('codGestorTramitador').value = '<%=gestor != null ? gestor.toUpperCase() : ""%>';
        document.getElementById('empresa').value = '<%=empresa != null ? empresa.toUpperCase() : ""%>';
        document.getElementById('impSolicitado').value = '<%=impSolicitado != null ? impSolicitado.toUpperCase() : ""%>';
        document.getElementById('impConvocatoria').value = '<%=impConvocatoria != null ? impConvocatoria.toUpperCase() : ""%>';
        document.getElementById('impPrevistoConcesion').value = '<%=impPrevCon != null ? impPrevCon.toUpperCase() : ""%>';
        document.getElementById('impConcedido').value = '<%=impConcedido != null ? impConcedido.toUpperCase() : ""%>';
        document.getElementById('impJustificado').value = '<%=impJustificado != null ? impJustificado.toUpperCase() :""%>';
        document.getElementById('impRenunciado').value = '<%=impRenunciado != null ? impRenunciado.toUpperCase() : ""%>';
        //document.getElementById('impPagado').value = '<%=impPagado != null ? impPagado.toUpperCase() : ""%>';
        document.getElementById('impPagado2').value = '<%=impPagado2 != null ? impPagado2.toUpperCase() : ""%>';
        document.getElementById('impReintegrar').value = '<%=impReintegrar != null ? impReintegrar.toUpperCase() : ""%>';
        document.getElementById('otrasAyudasSolic').value = '<%=otrasAyudasSolic != null ? otrasAyudasSolic.toUpperCase() : ""%>';
        document.getElementById('otrasAyudasConce').value = '<%=otrasAyudasConce != null ? otrasAyudasConce.toUpperCase() : ""%>';
        document.getElementById('minimisSolic').value = '<%=minimisSolic != null ? minimisSolic.toUpperCase() : ""%>';
        document.getElementById('minimisConce').value = '<%=minimisConce != null ? minimisConce.toUpperCase() : ""%>';
        <%--document.getElementById('impDespido').value = '<%=impDesp != null ? impDesp.toUpperCase() : ""%>';
        document.getElementById('impBaja').value = '<%=impBaja != null ? impBaja.toUpperCase() : ""%>';
        --%>document.getElementById('concedidoReal').value = '<%=concedidoReal != null ? concedidoReal.toUpperCase() : ""%>';
        document.getElementById('pagadoReal').value = '<%=pagadoReal != null ? pagadoReal.toUpperCase() : ""%>';
        document.getElementById('pagadoReal2').value = '<%=pagadoReal2 != null ? pagadoReal2.toUpperCase() : ""%>';
        document.getElementById('impNoJustif').value = '<%=impNoJustif != null ? impNoJustif.toUpperCase() : ""%>';
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
        
        
        
        
        
        cargarDescripcionesCombosDatosCpe();
        
        reemplazarPuntosCpe(document.getElementById('impSolicitado'));
        reemplazarPuntosCpe(document.getElementById('impConvocatoria'));
        reemplazarPuntosCpe(document.getElementById('impPrevistoConcesion'));
        reemplazarPuntosCpe(document.getElementById('impConcedido'));
        reemplazarPuntosCpe(document.getElementById('impJustificado'));
        reemplazarPuntosCpe(document.getElementById('impRenunciado'));
        reemplazarPuntosCpe(document.getElementById('impPagado'));
        reemplazarPuntosCpe(document.getElementById('impPagado2'));
        reemplazarPuntosCpe(document.getElementById('impReintegrar'));
        reemplazarPuntosCpe(document.getElementById('otrasAyudasSolic'));
        reemplazarPuntosCpe(document.getElementById('otrasAyudasConce'));
        reemplazarPuntosCpe(document.getElementById('minimisSolic'));
        reemplazarPuntosCpe(document.getElementById('minimisConce'));
        reemplazarPuntosCpe(document.getElementById('concedidoReal'));
        reemplazarPuntosCpe(document.getElementById('pagadoReal'));
        
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
            document.getElementById('minimisConce').className = 'inputTexto readOnly';
        }    
        mostrarOtrasAyudas();
        mostrarEnlaces();
    }
        
    function cargarDescripcionesCombosDatosCpe(){
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
    
   
    
    function recargarCalculosCpe(calculos){
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

        document.getElementById('concedidoReal').value = calculos[15] != undefined && calculos[15] != '' ? calculos[15] : '0,00';
        document.getElementById('pagadoReal').value = calculos[16] != undefined && calculos[16] != '' ? calculos[16] : '0,00';
        document.getElementById('pagadoReal2').value = calculos[17] != undefined && calculos[17] != '' ? calculos[17] : '0,00';
        document.getElementById('impNoJustif').value = calculos[18] != undefined && calculos[18] != '' ? calculos[18] : '0,00';
        document.getElementById('impRenunciadoRes').value = calculos[19] != undefined && calculos[19] != '' ? calculos[19] : '0,00';
        
        ajustarDecimalesImportes();
        comprobarImporteConcedido();
        comprobarImportePagado();
        comprobarImportePagado2();
        
        actualizarPestanaDatosSuplementarios();
    }
    
    function comprobarImporteConcedido(){
        var concedidoMostrado = document.getElementById('impConcedido').value;
        var concedidoReal = document.getElementById('concedidoReal').value;
        
        concedidoMostrado = reemplazarTextoCpe(concedidoMostrado, /,/g, '.');
        concedidoReal = reemplazarTextoCpe(concedidoReal, /,/g, '.');
        
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
            document.getElementById('impConcedido').style.textAlign = 'right';
        }
    }
    
    function mostrarImpConCalculado(event){
        var rsultado;
        var minimis = document.getElementById('minimisConce').value;
        if(document.forms[0].modoConsulta.value == "si"){
            if(minimis != undefined && minimis != ''){
                resultado = jsp_alerta('A','<%=meLanbide59I18n.getMensaje(idiomaUsuario, "msg.datosCpe.impConCalculadoEs")%>: <br/>'+document.getElementById('concedidoReal').value+' '+'<%=meLanbide59I18n.getMensaje(idiomaUsuario, "msg.euros")%>'+'<br/>'+'<%=meLanbide59I18n.getMensaje(idiomaUsuario, "msg.avisoMinimis")%>'+' '+document.getElementById('minimisConce').value);
            }else{
                resultado = jsp_alerta('A','<%=meLanbide59I18n.getMensaje(idiomaUsuario, "msg.datosCpe.impConCalculadoEs")%>: <br/>'+document.getElementById('concedidoReal').value+' '+'<%=meLanbide59I18n.getMensaje(idiomaUsuario, "msg.euros")%>');
            }
        }else{
            if(minimis != undefined && minimis != ''){
                resultado = jsp_alerta('','<%=meLanbide59I18n.getMensaje(idiomaUsuario, "msg.datosCpe.impConCalculadoEs")%>: <br/>'+document.getElementById('concedidoReal').value+' '+'<%=meLanbide59I18n.getMensaje(idiomaUsuario, "msg.euros")%>'+'<br/>'+'<%=meLanbide59I18n.getMensaje(idiomaUsuario, "msg.avisoMinimis")%>'+' '+document.getElementById('minimisConce').value+'<br/><br/>'+'<%=meLanbide59I18n.getMensaje(idiomaUsuario, "msg.datosCpe.preguntaAceptarImporte")%>');
            }else{
                resultado = jsp_alerta('','<%=meLanbide59I18n.getMensaje(idiomaUsuario, "msg.datosCpe.impConCalculadoEs")%>: <br/>'+document.getElementById('concedidoReal').value+' '+'<%=meLanbide59I18n.getMensaje(idiomaUsuario, "msg.euros")%>'+'<br/><br/>'+'<%=meLanbide59I18n.getMensaje(idiomaUsuario, "msg.datosCpe.preguntaAceptarImporte")%>');
            }
        }
        if (resultado == 1){
            document.getElementById('impConcedido').value = document.getElementById('concedidoReal').value;
            document.getElementById('impConcedido').removeAttribute('style');
            document.getElementById('impConcedido').style.textAlign = 'right';
            document.getElementById('importesModificados').value = '1';
            recalcularPagadoReal();
        }
    }
    
    function comprobarImportePagado(){
        var pagadoMostrado = document.getElementById('impPagado').value;
        var pagadoReal = document.getElementById('pagadoReal').value;
        
        pagadoMostrado = reemplazarTextoCpe(pagadoMostrado, /,/g, '.');
        pagadoReal = reemplazarTextoCpe(pagadoReal, /,/g, '.');
        
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
            document.getElementById('impPagado').style.textAlign = 'right';
        }
        //recalcularImpReintegro();
    }
    
    function mostrarImpPagCalculado(event){
        var resultado;
        if(document.forms[0].modoConsulta.value == "si"){    
            resultado = jsp_alerta('A','<%=meLanbide59I18n.getMensaje(idiomaUsuario, "msg.datosCpe.impPagCalculadoEs")%>: <br/>'+document.getElementById('pagadoReal').value+' '+'<%=meLanbide59I18n.getMensaje(idiomaUsuario, "msg.euros")%>');
        }else{
            resultado = jsp_alerta('','<%=meLanbide59I18n.getMensaje(idiomaUsuario, "msg.datosCpe.impPagCalculadoEs")%>: <br/>'+document.getElementById('pagadoReal').value+' '+'<%=meLanbide59I18n.getMensaje(idiomaUsuario, "msg.euros")%>'+'<br/><br/>'+'<%=meLanbide59I18n.getMensaje(idiomaUsuario, "msg.datosCpe.preguntaAceptarImporte")%>');
        }
        if (resultado == 1){
            document.getElementById('impPagado').value = document.getElementById('pagadoReal').value;
            document.getElementById('impPagado').removeAttribute('style');
            document.getElementById('impPagado').style.textAlign = 'right';
            document.getElementById('importesModificados').value = '1';
            recalcularImpReintegro();
            recalcularPago2Real();
        }
    }
    
    function recalcularConcedidoReal(){
        try
        {
            if(validarNumericoDecimalCpe(document.getElementById('impPrevistoConcesion'), 10, 2) && validarNumericoDecimalCpe(document.getElementById('otrasAyudasConce'), 10, 2)){
                var prevCon = document.getElementById('impPrevistoConcesion').value;
                var oayu = document.getElementById('otrasAyudasConce').value;
                
                prevCon = reemplazarTextoCpe(prevCon, /,/g, '.');
                oayu = reemplazarTextoCpe(oayu, /,/g, '.');
                
                var f1 = parseFloat(prevCon);
                var f2 = parseFloat(oayu);
                var res = f1 - f2;
                res = res.toFixed(2);
                
                document.getElementById('concedidoReal').value = res;
                reemplazarPuntosCpe(document.getElementById('concedidoReal'));
            }
        }catch(err){
            
        }
        comprobarImporteConcedido();
        recalcularPagadoReal();
    }
    
    function recalcularPagadoReal(){
        try{
            if(validarNumericoDecimalCpe(document.getElementById('impConcedido'), 10, 2) && validarNumericoDecimalCpe(document.getElementById('impConcedido'), 10, 2)){
                var con = document.getElementById('impConcedido').value;
                con = reemplazarTextoCpe(con, /,/g, '.');
                var f1 = parseFloat(con);
                f1 = f1 * 0.8;
                f1 = f1.toFixed(2);
                
                document.getElementById('pagadoReal').value = f1;
                
                reemplazarPuntosCpe(document.getElementById('pagadoReal'));
            }
        }catch(err){
            
        }
        comprobarImportePagado();
        recalcularPago2Real();
    }
    
    function mostrarImpPag2Calculado(event){
        var resultado;
        var valor = document.getElementById('pagadoReal2').value;
        if(valor == undefined || valor == ''){
            valor = '0,00';
        }
        if(document.forms[0].modoConsulta.value == "si"){    
            resultado = jsp_alerta('A','<%=meLanbide59I18n.getMensaje(idiomaUsuario, "msg.datosCpe.impPag2CalculadoEs")%>: <br/>'+valor+' '+'<%=meLanbide59I18n.getMensaje(idiomaUsuario, "msg.euros")%>');
        }else{
            resultado = jsp_alerta('','<%=meLanbide59I18n.getMensaje(idiomaUsuario, "msg.datosCpe.impPag2CalculadoEs")%>: <br/>'+valor+' '+'<%=meLanbide59I18n.getMensaje(idiomaUsuario, "msg.euros")%>'+'<br/><br/>'+'<%=meLanbide59I18n.getMensaje(idiomaUsuario, "msg.datosCpe.preguntaAceptarImporte")%>');
        }
        if (resultado == 1){
            document.getElementById('impPagado2').value = document.getElementById('pagadoReal2').value;
            document.getElementById('impPagado2').removeAttribute('style');
            document.getElementById('impPagado2').style.textAlign = 'right';
            document.getElementById('importesModificados').value = '1';
            recalcularImpReintegro();
        }
    }
    
    function recalcularPago2Real(){
        try{
            if(validarNumericoDecimalCpe(document.getElementById('impConcedido'), 10, 2) && validarNumericoDecimalCpe(document.getElementById('impPagado'), 10, 2)){
                var con = document.getElementById('impConcedido').value;
                var pag1 = document.getElementById('impPagado').value;
                var just = document.getElementById('impJustificado').value;
                
                con = reemplazarTextoCpe(con, /,/g, '.');
                pag1 = reemplazarTextoCpe(pag1, /,/g, '.');
                just = reemplazarTextoCpe(just, /,/g, '.');
                
                var fCon = parseFloat(con);
                var fPag1 = parseFloat(pag1);
                var fJust = parseFloat(just);
                var fPag2 = null;
                
                if(fJust > fCon){
                    fJust = fCon;
                }
                
                if(fJust - fPag1 > 0){
                    fPag2 = fJust - fPag1;
                }else{
                    fPag2 = 0;
                }
                
                fPag2 = fPag2.toFixed(2);
                
                document.getElementById('pagadoReal2').value = fPag2;
                
                reemplazarPuntosCpe(document.getElementById('pagadoReal2'));
            }
        }catch(err){
            
        }
        comprobarImportePagado2();
    }
    
    function comprobarImportePagado2(){
        var pago2Mostrado = document.getElementById('impPagado2').value;
        var pago2Real = document.getElementById('pagadoReal2').value;
        
        pago2Mostrado = reemplazarTextoCpe(pago2Mostrado, /,/g, '.');
        pago2Real = reemplazarTextoCpe(pago2Real, /,/g, '.');
        
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
            document.getElementById('impPagado2').style.textAlign = 'right';
        }
    }
    
    function importeModificado(){
        document.getElementById('importesModificados').value = '1';
    }
    
    function actualizarOtrasPestanas(pestana){
        try{
            if(pestana != 'oferta'){
                //Pestana ofertas
                actualizarPestanaOferta();
            }
            
            if(pestana != 'justif'){
                actualizarPestanaJustificacion();
            }
        }catch(err){
            
        }
    }
    
    function recalcularImpReintegro(){
        try
        {
            if(validarNumericoDecimalCpe(document.getElementById('impPagado'), 10, 2) && validarNumericoDecimalCpe(document.getElementById('impJustificado'), 10, 2)){
                var pagado = document.getElementById('impPagado').value;
                var justif = document.getElementById('impJustificado').value;
                
                pagado = reemplazarTextoCpe(pagado, /,/g, '.');
                justif = reemplazarTextoCpe(justif, /,/g, '.');
                
                var f1 = parseFloat(pagado);
                var f2 = parseFloat(justif);
                if(f1 >= f2){
                var res = f1 - f2;
                    res = res.toFixed(2);

                    document.getElementById('impReintegrar').value = res;
                    reemplazarPuntosCpe(document.getElementById('impReintegrar'));
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
        v = reemplazarTextoCpe(v, /,/g, '.');
        f = parseFloat(v);
        f = ajustarDecimalesCpe(f, 2);
        if(!isNaN(f)){
            document.getElementById('impSolicitado').value = f;
            reemplazarPuntosCpe(document.getElementById('impSolicitado'));
            document.getElementById('impSolicitado').removeAttribute("style");
            document.getElementById('impSolicitado').style.textAlign = 'right';
        }
        
        //Imp. prev. concesion
        v = document.getElementById('impPrevistoConcesion').value;
        v = reemplazarTextoCpe(v, /,/g, '.');
        f = parseFloat(v);
        f = ajustarDecimalesCpe(f, 2);
        if(!isNaN(f)){
            document.getElementById('impPrevistoConcesion').value = f;
            reemplazarPuntosCpe(document.getElementById('impPrevistoConcesion'));
            document.getElementById('impPrevistoConcesion').removeAttribute("style");
            document.getElementById('impPrevistoConcesion').style.textAlign = 'right';
        }
        
        //Importe concedido
        v = document.getElementById('impConcedido').value;
        v = reemplazarTextoCpe(v, /,/g, '.');
        f = parseFloat(v);
        f = ajustarDecimalesCpe(f, 2);
        if(!isNaN(f)){
            document.getElementById('impConcedido').value = f;
            reemplazarPuntosCpe(document.getElementById('impConcedido'));
            document.getElementById('impConcedido').removeAttribute("style");
            document.getElementById('impConcedido').style.textAlign = 'right';
        }
        
        //Importe renunciado
        v = document.getElementById('impRenunciado').value;
        v = reemplazarTextoCpe(v, /,/g, '.');
        f = parseFloat(v);
        f = ajustarDecimalesCpe(f, 2);
        if(!isNaN(f)){
            document.getElementById('impRenunciado').value = f;
            reemplazarPuntosCpe(document.getElementById('impRenunciado'));
            document.getElementById('impRenunciado').removeAttribute("style");
            document.getElementById('impRenunciado').style.textAlign = 'right';
        }
        
        //Importe renunciado resolucion
        v = document.getElementById('impRenunciadoRes').value;
        v = reemplazarTextoCpe(v, /,/g, '.');
        f = parseFloat(v);
        f = ajustarDecimalesCpe(f, 2);
        if(!isNaN(f)){
            document.getElementById('impRenunciadoRes').value = f;
            reemplazarPuntosCpe(document.getElementById('impRenunciadoRes'));
            document.getElementById('impRenunciadoRes').removeAttribute("style");
            document.getElementById('impRenunciadoRes').style.textAlign = 'right';
        }
        
        //Primer Pago
        v = document.getElementById('impPagado').value;
        v = reemplazarTextoCpe(v, /,/g, '.');
        f = parseFloat(v);
        f = ajustarDecimalesCpe(f, 2);
        if(!isNaN(f)){
            document.getElementById('impPagado').value = f;
            reemplazarPuntosCpe(document.getElementById('impPagado'));
            document.getElementById('impPagado').removeAttribute("style");
            document.getElementById('impPagado').style.textAlign = 'right';
        }
        
        
        //Segundo Pago
        v = document.getElementById('impPagado2').value;
        v = reemplazarTextoCpe(v, /,/g, '.');
        f = parseFloat(v);
        f = ajustarDecimalesCpe(f, 2);
        if(!isNaN(f)){
            document.getElementById('impPagado2').value = f;
            reemplazarPuntosCpe(document.getElementById('impPagado2'));
            document.getElementById('impPagado2').removeAttribute("style");
            document.getElementById('impPagado2').style.textAlign = 'right';
        }
        
        //Importe justificado
        v = document.getElementById('impJustificado').value;
        v = reemplazarTextoCpe(v, /,/g, '.');
        f = parseFloat(v);
        f = ajustarDecimalesCpe(f, 2);
        if(!isNaN(f)){
            document.getElementById('impJustificado').value = f;
            reemplazarPuntosCpe(document.getElementById('impJustificado'));
            document.getElementById('impJustificado').removeAttribute("style");
            document.getElementById('impJustificado').style.textAlign = 'right';
        }
        
        //Importe no justificado
        v = document.getElementById('impNoJustif').value;
        v = reemplazarTextoCpe(v, /,/g, '.');
        f = parseFloat(v);
        f = ajustarDecimalesCpe(f, 2);
        if(!isNaN(f)){
            document.getElementById('impNoJustif').value = f;
            reemplazarPuntosCpe(document.getElementById('impNoJustif'));
            document.getElementById('impNoJustif').removeAttribute("style");
            document.getElementById('impNoJustif').style.textAlign = 'right';
        }
        
        //Importe a reintegrar
        v = document.getElementById('impReintegrar').value;
        v = reemplazarTextoCpe(v, /,/g, '.');
        f = parseFloat(v);
        f = ajustarDecimalesCpe(f, 2);
        if(!isNaN(f)){
            document.getElementById('impReintegrar').value = f;
            reemplazarPuntosCpe(document.getElementById('impReintegrar'));
            document.getElementById('impReintegrar').removeAttribute("style");
            document.getElementById('impReintegrar').style.textAlign = 'right';
        }
        
        //Otr. ayudas (solicitado)
        v = document.getElementById('otrasAyudasSolic').value;
        v = reemplazarTextoCpe(v, /,/g, '.');
        f = parseFloat(v);
        f = ajustarDecimalesCpe(f, 2);
        if(!isNaN(f)){
            document.getElementById('otrasAyudasSolic').value = f;
            reemplazarPuntosCpe(document.getElementById('otrasAyudasSolic'));
            document.getElementById('otrasAyudasSolic').removeAttribute("style");
            document.getElementById('otrasAyudasSolic').style.textAlign = 'right';
        }
        
        //Otr. ayudas (concedido)
        v = document.getElementById('otrasAyudasConce').value;
        v = reemplazarTextoCpe(v, /,/g, '.');
        f = parseFloat(v);
        f = ajustarDecimalesCpe(f, 2);
        if(!isNaN(f)){
            document.getElementById('otrasAyudasConce').value = f;
            reemplazarPuntosCpe(document.getElementById('otrasAyudasConce'));
            document.getElementById('otrasAyudasConce').removeAttribute("style");
            document.getElementById('otrasAyudasConce').style.textAlign = 'right';
        }
        
        //Minimis (solicitado)
        v = document.getElementById('minimisSolic').value;
        v = reemplazarTextoCpe(v, /,/g, '.');
        f = parseFloat(v);
        f = ajustarDecimalesCpe(f, 2);
        if(!isNaN(f)){
            document.getElementById('minimisSolic').value = f;
            reemplazarPuntosCpe(document.getElementById('minimisSolic'));
            document.getElementById('minimisSolic').removeAttribute("style");
            document.getElementById('minimisSolic').style.textAlign = 'right';
        }
        
        //Minimis (concedido)
        v = document.getElementById('minimisConce').value;
        v = reemplazarTextoCpe(v, /,/g, '.');
        f = parseFloat(v);
        f = ajustarDecimalesCpe(f, 2);
        if(!isNaN(f)){
            document.getElementById('minimisConce').value = f;
            reemplazarPuntosCpe(document.getElementById('minimisConce'));
            document.getElementById('minimisConce').removeAttribute("style");
            document.getElementById('minimisConce').style.textAlign = 'right';
        }
        
        //Importe convocatoria
        v = document.getElementById('impConvocatoria').value;
        v = reemplazarTextoCpe(v, /,/g, '.');
        f = parseFloat(v);
        f = ajustarDecimalesCpe(f, 2);
        if(!isNaN(f)){
            document.getElementById('impConvocatoria').value = f;
            reemplazarPuntosCpe(document.getElementById('impConvocatoria'));
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
            if(estanCargadosCamposSuplementarios()){
                var ajax = getXMLHttpRequest();
                var nodos = null;
                var CONTEXT_PATH = '<%=request.getContextPath()%>'
                var url = CONTEXT_PATH + '/PeticionModuloIntegracion.do';
                var parametros = '';
                var control = new Date();
                parametros = 'tarea=preparar&modulo=MELANBIDE59&operacion=getTodosCamposSuplementarios&tipo=0&numero=<%=numExpediente%>&control='+control.getTime();
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
                        document.getElementById('<%=ConfigurationParameter.getParameter(ConstantesMeLanbide59.CAMPO_SUPL_IMP_CON, ConstantesMeLanbide59.FICHERO_PROPIEDADES)%>').value = importes[3];
                    }catch(err){
                    }
                    try{
                        document.getElementById('<%=ConfigurationParameter.getParameter(ConstantesMeLanbide59.CAMPO_SUPL_IMP_PAG, ConstantesMeLanbide59.FICHERO_PROPIEDADES)%>').value = importes[6];
                    }catch(err){
                    }
                    try{
                        document.getElementById('<%=ConfigurationParameter.getParameter(ConstantesMeLanbide59.CAMPO_SUPL_OTRAS_AYUDAS_CONCE, ConstantesMeLanbide59.FICHERO_PROPIEDADES)%>').value = importes[10];
                    }catch(err){
                    }
                    try{
                        document.getElementById('<%=ConfigurationParameter.getParameter(ConstantesMeLanbide59.CAMPO_SUPL_IMP_SOLICITADO, ConstantesMeLanbide59.FICHERO_PROPIEDADES)%>').value = importes[0];
                    }catch(err){
                    }
                    try{
                        document.getElementById('<%=ConfigurationParameter.getParameter(ConstantesMeLanbide59.CAMPO_SUPL_IMP_CONVOCATORIA, ConstantesMeLanbide59.FICHERO_PROPIEDADES)%>').value = importes[1];
                    }catch(err){
                    }
                    try{
                        document.getElementById('<%=ConfigurationParameter.getParameter(ConstantesMeLanbide59.CAMPO_SUPL_IMP_PREV_CON, ConstantesMeLanbide59.FICHERO_PROPIEDADES)%>').value = importes[2];
                    }catch(err){
                    }
                    try{
                        document.getElementById('<%=ConfigurationParameter.getParameter(ConstantesMeLanbide59.CAMPO_SUPL_IMP_JUS, ConstantesMeLanbide59.FICHERO_PROPIEDADES)%>').value = importes[4];
                    }catch(err){
                    }
                    try{
                        document.getElementById('<%=ConfigurationParameter.getParameter(ConstantesMeLanbide59.CAMPO_SUPL_IMP_REN, ConstantesMeLanbide59.FICHERO_PROPIEDADES)%>').value = importes[5];
                    }catch(err){
                    }
                    try{
                        document.getElementById('<%=ConfigurationParameter.getParameter(ConstantesMeLanbide59.CAMPO_SUPL_IMP_PAG_2, ConstantesMeLanbide59.FICHERO_PROPIEDADES)%>').value = importes[7];
                    }catch(err){
                    }
                    try{
                        document.getElementById('<%=ConfigurationParameter.getParameter(ConstantesMeLanbide59.CAMPO_SUPL_IMP_REI, ConstantesMeLanbide59.FICHERO_PROPIEDADES)%>').value = importes[8];
                    }catch(err){
                    }
                    try{
                        document.getElementById('<%=ConfigurationParameter.getParameter(ConstantesMeLanbide59.CAMPO_SUPL_OTRAS_AYUDAS_SOLIC, ConstantesMeLanbide59.FICHERO_PROPIEDADES)%>').value = importes[9];
                    }catch(err){
                    }
                    try{
                        document.getElementById('<%=ConfigurationParameter.getParameter(ConstantesMeLanbide59.CAMPO_SUPL_MINIMIS_SOLIC, ConstantesMeLanbide59.FICHERO_PROPIEDADES)%>').value = importes[11];
                    }catch(err){
                    }
                    try{
                        document.getElementById('<%=ConfigurationParameter.getParameter(ConstantesMeLanbide59.CAMPO_SUPL_MINIMIS_CONCE, ConstantesMeLanbide59.FICHERO_PROPIEDADES)%>').value = importes[12];
                    }catch(err){
                    }
                    try{
                        document.getElementById('<%=ConfigurationParameter.getParameter(ConstantesMeLanbide59.CAMPO_SUPL_IMP_NO_JUS, ConstantesMeLanbide59.FICHERO_PROPIEDADES)%>').value = importes[19];
                    }catch(err){
                    }
                    try{
                        document.getElementById('<%=ConfigurationParameter.getParameter(ConstantesMeLanbide59.CAMPO_SUPL_IMP_REN_RES, ConstantesMeLanbide59.FICHERO_PROPIEDADES)%>').value = importes[20];
                    }catch(err){
                    }
                    try{
                        document.getElementById('<%=ConfigurationParameter.getParameter(ConstantesMeLanbide59.CAMPO_SUPL_IMP_PAG_RES, ConstantesMeLanbide59.FICHERO_PROPIEDADES)%>').value = importes[21];
                    }catch(err){
                    }
                }

                if(puestos){
                    try{
                        document.getElementById('<%=ConfigurationParameter.getParameter(ConstantesMeLanbide59.CAMPO_SUPL_PUESTOS_SOLICITADOS, ConstantesMeLanbide59.FICHERO_PROPIEDADES)%>').value = puestos[0];
                    }catch(err){
                    }
                    try{
                        document.getElementById('<%=ConfigurationParameter.getParameter(ConstantesMeLanbide59.CAMPO_SUPL_PUESTOS_DENEGADOS, ConstantesMeLanbide59.FICHERO_PROPIEDADES)%>').value = puestos[1];
                    }catch(err){
                    }
                    try{
                        document.getElementById('<%=ConfigurationParameter.getParameter(ConstantesMeLanbide59.CAMPO_SUPL_PUESTOS_CONTRATADOS, ConstantesMeLanbide59.FICHERO_PROPIEDADES)%>').value = puestos[2];
                    }catch(err){
                    }
                    try{
                        document.getElementById('<%=ConfigurationParameter.getParameter(ConstantesMeLanbide59.CAMPO_SUPL_PERSONAS_CONTRATADAS, ConstantesMeLanbide59.FICHERO_PROPIEDADES)%>').value = puestos[3];
                    }catch(err){
                    }
                    try{
                        document.getElementById('<%=ConfigurationParameter.getParameter(ConstantesMeLanbide59.CAMPO_SUPL_PUESTOS_DESPIDO, ConstantesMeLanbide59.FICHERO_PROPIEDADES)%>').value = puestos[4];
                    }catch(err){
                    }
                    try{
                        document.getElementById('<%=ConfigurationParameter.getParameter(ConstantesMeLanbide59.CAMPO_SUPL_PERSONAS_DESPIDO, ConstantesMeLanbide59.FICHERO_PROPIEDADES)%>').value = puestos[5];
                    }catch(err){
                    }
                    try{
                        document.getElementById('<%=ConfigurationParameter.getParameter(ConstantesMeLanbide59.CAMPO_SUPL_PUESTOS_BAJA, ConstantesMeLanbide59.FICHERO_PROPIEDADES)%>').value = puestos[6];
                    }catch(err){
                    }
                    try{
                        document.getElementById('<%=ConfigurationParameter.getParameter(ConstantesMeLanbide59.CAMPO_SUPL_PERSONAS_BAJA, ConstantesMeLanbide59.FICHERO_PROPIEDADES)%>').value = puestos[7];
                    }catch(err){
                    }
                    try{
                        document.getElementById('<%=ConfigurationParameter.getParameter(ConstantesMeLanbide59.CAMPO_SUPL_PUESTOS_RENUNCIADOS, ConstantesMeLanbide59.FICHERO_PROPIEDADES)%>').value = puestos[8];
                    }catch(err){
                    }
                }

                if(otros){
                    try{
                        document.getElementById('<%=ConfigurationParameter.getParameter(ConstantesMeLanbide59.CAMPO_SUPL_GESTOR_TRAMITADOR, ConstantesMeLanbide59.FICHERO_PROPIEDADES)%>').value = otros[0];
                    }catch(err){
                    }
                    try{
                        document.getElementById('<%=ConfigurationParameter.getParameter(ConstantesMeLanbide59.CAMPO_SUPL_EMPRESA, ConstantesMeLanbide59.FICHERO_PROPIEDADES)%>').value = otros[1];
                    }catch(err){
                    }
                }
            }
        }catch(err){
        
        }
     }
     
     function consultarImportesResolucion(res){
        try{
            if(res != undefined && res != ''){
                var control = new Date();
                if(navigator.appName.indexOf("Internet Explorer")!=-1){
                    lanzarPopUpModal('<%=request.getContextPath()%>/PeticionModuloIntegracion.do?tarea=preparar&modulo=MELANBIDE59&operacion=cargarImportesResolucion&res='+res+'&tipo=0&numero=<%=numExpediente%>&control='+control.getTime(),380,395,'yes','no',function(){
					});
                }else{
                    lanzarPopUpModal('<%=request.getContextPath()%>/PeticionModuloIntegracion.do?tarea=preparar&modulo=MELANBIDE59&operacion=cargarImportesResolucion&res='+res+'&tipo=0&numero=<%=numExpediente%>&control='+control.getTime(),400,440,'yes','no',function(){
					});
                }
            }else{
                //No se pueden cargar los datos
                jsp_alerta("A",'<%=meLanbide59I18n.getMensaje(idiomaUsuario,"error.cargarDatosResolucion")%>');
            }
        }catch(err){
            
        }
     }
</script>

<body>
    <div class="tab-page" id="tabPage391" style="height:480px; width: 100%; ">
        <h2 class="tab" id="pestana391" style="font-size:1em;"><%=meLanbide59I18n.getMensaje(idiomaUsuario,"label.datosCpe.tituloPestanaHist")%></h2>
        <div style="clear: both;">
            <div>
                
                <div style="clear: both; width: 100%; text-align: left; padding-top: 10px;">
                    <div id="tab-panel-391" class="tab-pane" style="float: left;" align="center"></div>
                    <script type="text/javascript"> 
                        tp391 = new WebFXTabPane(document.getElementById("tab-panel-391"));
                        tp391.selectedIndex = 0;
                    </script>
                    <div class="tab-page" id="tabPage3911" style="height: 194px;">
                        <h2 class="tab" id="pestana3911" style="<%=margenIzqPestanasDatosCpe%>"><%=meLanbide59I18n.getMensaje(idiomaUsuario,"label.solicitud.tituloPestana")%></h2>
                        <script type="text/javascript">tp391.addTabPage( document.getElementById( "tabPage3911" ) );</script>
                        <div style="clear: both;">
                            <jsp:include page="<%=urlPestanaDatos_solicitud%>" flush="true"/>
                        </div>
                    </div>
                    <div class="tab-page" id="tabPage3912" style="height: 194px;">
                        <h2 class="tab" id="pestana3912"><%=meLanbide59I18n.getMensaje(idiomaUsuario,"label.oferta.tituloPestana")%></h2>
                        <script type="text/javascript">tp391.addTabPage( document.getElementById( "tabPage3912" ) );</script>
                        <div style="clear: both;">
                            <jsp:include page="<%=urlPestanaDatos_oferta%>" flush="true"/>
                        </div>
                    </div>
                    <div class="tab-page" id="tabPage3913" style="height: 194px;">
                        <h2 class="tab" id="pestana3913"><%=meLanbide59I18n.getMensaje(idiomaUsuario,"label.justif.tituloPestana")%></h2>
                        <script type="text/javascript">tp391.addTabPage( document.getElementById( "tabPage3913" ) );</script>
                        <div style="clear: both;">
                            <jsp:include page="<%=urlPestanaDatos_justif%>" flush="true"/>
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
    //var comboGestorTramitador = new Combo('GestorTramitador');
    //cargarDatosCpe();
</script>
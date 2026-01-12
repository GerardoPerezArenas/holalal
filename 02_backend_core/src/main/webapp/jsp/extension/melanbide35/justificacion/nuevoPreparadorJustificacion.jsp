<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>

<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide35.i18n.MeLanbide35I18n" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide35.vo.justificacion.EcaJusPreparadoresVO" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide35.vo.justificacion.FilaPreparadorJustificacionVO" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide35.vo.comun.SelectItem" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide35.util.ConstantesMeLanbide35" %>
<%@page import="java.text.SimpleDateFormat" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="java.util.List" %>
<%@page import="java.util.ArrayList" %>
<%@page import="org.apache.commons.lang.StringEscapeUtils"%>

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
    
    SimpleDateFormat format = new SimpleDateFormat(ConstantesMeLanbide35.FORMATO_FECHA);
    
    //Clase para internacionalizar los mensajes de la aplicación.
    MeLanbide35I18n meLanbide35I18n = MeLanbide35I18n.getInstance();
    String numExpediente    = request.getParameter("numero");
    EcaJusPreparadoresVO preparadorModif = (EcaJusPreparadoresVO)request.getAttribute("preparadorModif");
    session.removeAttribute("preparadorModif");
    EcaJusPreparadoresVO preparadorOrigen = (EcaJusPreparadoresVO)request.getAttribute("preparadorSustituir");
    session.removeAttribute("preparadorSustituir");
    EcaJusPreparadoresVO sustituto = (EcaJusPreparadoresVO)request.getAttribute("sustituto");
    session.removeAttribute("sustituto");
    Boolean consulta = request.getAttribute("consulta") != null ? (Boolean)request.getAttribute("consulta") : false;

    
    // lista tipo contrato
    List<SelectItem> listaTiposContratos = new ArrayList<SelectItem>();
    if(request.getAttribute("lstTipoContrato") != null)
        listaTiposContratos = (List<SelectItem>)request.getAttribute("lstTipoContrato");
            
    String lcodTipoContrato = "";
    String ldescTipoContrato = "";

    if (listaTiposContratos != null && listaTiposContratos.size() > 0) 
    {
        int i;
        SelectItem tc = null;
        for (i = 0; i < listaTiposContratos.size() - 1; i++) 
        {
            tc = (SelectItem) listaTiposContratos.get(i);
            lcodTipoContrato += "\"" + tc.getId().toString() + "\",";
            ldescTipoContrato += "\"" + escape(tc.getLabel()) + "\",";
        }
        tc = (SelectItem) listaTiposContratos.get(i);
        lcodTipoContrato += "\"" + tc.getId().toString() + "\"";
        ldescTipoContrato += "\"" + escape(tc.getLabel()) + "\"";
    }
    
    String tituloPagina = "";
    if(consulta)
    {
        tituloPagina = meLanbide35I18n.getMensaje(idiomaUsuario,"label.justificacion.preparadores.consultaPreparador.tituloPagina");
    }
    else
    {
        if(preparadorModif != null)
        {
            tituloPagina = meLanbide35I18n.getMensaje(idiomaUsuario,"label.justificacion.preparadores.modifPreparador.tituloPagina");
        }
        else
        {
            tituloPagina = meLanbide35I18n.getMensaje(idiomaUsuario,"label.justificacion.preparadores.nuevoPreparador.tituloPagina");
        }
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
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/less/animated.less'/>" media="screen">
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/>
<link rel="stylesheet" type="text/css" href="<c:url value='/css/extension/melanbide35/melanbide35.css'/>">

<script type="text/javascript">
    var APP_CONTEXT_PATH = '<%=request.getContextPath()%>';
</script>
<script type="text/javascript" src="<c:url value='/scripts/calendario.js'/>"></script>
<script type="text/javascript" src="<c:url value='/scripts/general.js'/>"></script>
<script type="text/javascript" src="<c:url value='/scripts/popup.js'/>"></script>
<script type="text/javascript" src="<c:url value='/scripts/validaciones.js'/>"></script>
        <script type="text/javascript" src="<c:url value='/scripts/listaComboBox.js'/>"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/melanbide35/ecaUtils.js"></script>

<script type="text/javascript">
    
    var mensajeValidacion = '';
    var nuevo = true;
    
    //LISTAS DE VALORES PARA LOS COMBOS
    var codTipoContrato = [<%=lcodTipoContrato%>];
    var descTipoContrato = [<%=ldescTipoContrato%>];
    
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
        try{            
            <%
            
            if(preparadorModif != null)
            {
            %>
                nuevo = false;
                document.getElementById('nif').value = '<%=preparadorModif.getNif() != null ? preparadorModif.getNif().toUpperCase() : "" %>';
                document.getElementById('nomApel').value = '<%=preparadorModif.getNombre() != null ? preparadorModif.getNombre().toUpperCase() : "" %>';
                document.getElementById('fechaInicio').value = '<%=preparadorModif.getFecIni() != null ? format.format(preparadorModif.getFecIni()) : "" %>';
                document.getElementById('fechaFin').value = '<%=preparadorModif.getFecFin() != null ? format.format(preparadorModif.getFecFin()) : "" %>';
                document.getElementById('horasAnualesJC').value = '<%=preparadorModif.getHorasJC() != null ? preparadorModif.getHorasJC().toPlainString() : "" %>';
                document.getElementById('horasContrato').value = '<%=preparadorModif.getHorasCont() != null ? preparadorModif.getHorasCont().toPlainString() : "" %>';
                document.getElementById('horasDedicacionEca').value = '<%=preparadorModif.getHorasEca() != null ? preparadorModif.getHorasEca().toPlainString() : "" %>';
                document.getElementById('costesSalarialesSSJor').value = '<%=preparadorModif.getImpSSJC() != null ? preparadorModif.getImpSSJC().toPlainString() : "" %>';
                document.getElementById('costesSalarialesSSPorJor').value = '<%=preparadorModif.getImpSSJR() != null ? preparadorModif.getImpSSJR().toPlainString() : "" %>';
                document.getElementById('costesSalarialesSSEca').value = '<%=preparadorModif.getImpSSECA() != null ? preparadorModif.getImpSSECA().toPlainString() : "" %>';                     
                document.getElementById('codTipoContrato').value = '<%=preparadorModif.getTipoContrato() != null ? preparadorModif.getTipoContrato() : ""%>';
                document.getElementById('nifOrigen').value = '<%=preparadorOrigen != null ? preparadorOrigen.getNif().toUpperCase() : "" %>';
                document.getElementById('nomApelOrigen').value = '<%=preparadorOrigen != null ? preparadorOrigen.getNombre().toUpperCase() : "" %>';        
                document.getElementById('nifSustituto').value = '<%=sustituto != null ? sustituto.getNif().toUpperCase() : "" %>';
                document.getElementById('nomApelSustituto').value = '<%=sustituto != null ? sustituto.getNombre().toUpperCase() : "" %>';        
            <%
            }else { %>
                document.getElementById('nifOrigen').value = '<%=preparadorOrigen != null ? preparadorOrigen.getNif().toUpperCase() : "" %>';
                document.getElementById('nomApelOrigen').value = '<%=preparadorOrigen != null ? preparadorOrigen.getNombre().toUpperCase() : "" %>';        
        //preparadorOrigen 
         <%  }
            %>
                        
            <%
                if(preparadorOrigen == null)
                {
            %>
                    document.getElementById('divOrigen').style.display = 'none';
            <%
                }
            %>   
                        
            <%
                if(sustituto == null)
                {
            %>
                    document.getElementById('divSustituto').style.display = 'none';
            <%
                }
            %>
                        
            reemplazarPuntosEca(document.getElementById('horasAnualesJC'));
            reemplazarPuntosEca(document.getElementById('horasContrato'));
            reemplazarPuntosEca(document.getElementById('horasDedicacionEca'));
            reemplazarPuntosEca(document.getElementById('costesSalarialesSSJor'));
            reemplazarPuntosEca(document.getElementById('costesSalarialesSSPorJor'));
            reemplazarPuntosEca(document.getElementById('costesSalarialesSSEca'));
                   
            FormatNumber(document.getElementById('horasAnualesJC').value, 8, 2, document.getElementById('horasAnualesJC').id);
            FormatNumber(document.getElementById('horasContrato').value, 8, 2, document.getElementById('horasContrato').id);
            FormatNumber(document.getElementById('horasDedicacionEca').value, 8, 2, document.getElementById('horasDedicacionEca').id);
            FormatNumber(document.getElementById('costesSalarialesSSJor').value, 8, 2, document.getElementById('costesSalarialesSSJor').id);
            FormatNumber(document.getElementById('costesSalarialesSSPorJor').value, 8, 2, document.getElementById('costesSalarialesSSPorJor').id);
            FormatNumber(document.getElementById('costesSalarialesSSEca').value, 8, 2, document.getElementById('costesSalarialesSSEca').id);
            
            /*reemplazarPuntosEca(document.getElementById('horasAnualesJC'));
            reemplazarPuntosEca(document.getElementById('horasContrato'));
            reemplazarPuntosEca(document.getElementById('horasDedicacionEca'));
            reemplazarPuntosEca(document.getElementById('costesSalarialesSSJor'));
            reemplazarPuntosEca(document.getElementById('costesSalarialesSSPorJor'));
            reemplazarPuntosEca(document.getElementById('costesSalarialesSSEca'));*/
            
            
            <%
                if(consulta == true)
                {
            %>
                    //Deshabilito todos los campos
                    document.getElementById('nif').disabled = true;
                    document.getElementById('nomApel').disabled = true;
                    document.getElementById('fechaInicio').disabled = true;
                    document.getElementById('fechaFin').disabled = true;
                    document.getElementById('horasAnualesJC').disabled = true;
                    document.getElementById('horasContrato').disabled = true;
                    document.getElementById('horasDedicacionEca').disabled = true;
                    document.getElementById('costesSalarialesSSJor').disabled = true;
                    document.getElementById('costesSalarialesSSPorJor').disabled = true;
                    document.getElementById('costesSalarialesSSEca').disabled = true;
                    document.getElementById('codTipoContrato').disabled = true;
                    document.getElementById('descTipoContrato').disabled = true;

                    document.getElementById('btnAceptar').style.display = 'none';
                    document.getElementById('btnCancelar').style.display = 'none';
                    document.getElementById('btnCerrar').style.display = 'inline';
                    document.getElementById('anchorTipoContrato').style.display = 'none';
                    document.getElementById('calFechaInicio').style.display = 'none';
                    document.getElementById('calFechaFin').style.display = 'none';
            <%
                }
                else
                {
            %>  
                    comboTipoContrato = new Combo('TipoContrato');
                    cargarCombos();
            <%
                }
            %>
                  
                cargarDescripcionesCombos();
        }catch(err){
            
        }
        ajustarDecimalesImportes();
    }
    
    function cargarCombos(){
        comboTipoContrato.addItems(codTipoContrato, descTipoContrato);
    }
        
    function cargarDescripcionesCombos(){
        var desc = "";
        var codAct = "";
        var codigo = "";

        //Tipo contrato
        codigo = '<%=preparadorModif != null && preparadorModif.getTipoContrato() != null ? preparadorModif.getTipoContrato() : ""%>';
        desc = '';
        var encontrado = false;
        var i = 0; 
        if(codigo != null && codigo != '')
        {
            while(i<codTipoContrato.length && !encontrado)
            {
                codAct = codTipoContrato[i];
                if(codAct == codigo)
                {
                    encontrado = true;
                    desc = descTipoContrato[i];
                }else{
                    i++;
                }
            }
        }
        document.getElementById('descTipoContrato').value = desc;
    }
        
    function mostrarCalFechaInicio(evento) {
        if(window.event) 
            evento = window.event;
        if (document.getElementById("calFechaInicio").src.indexOf("icono.gif") != -1 )
            showCalendar('forms[0]','fechaInicio',null,null,null,'','calFechaInicio','',null,null,null,null,null,null,null,null,evento);
    }
        
    function mostrarCalFechaFin(evento) {
        if(window.event) 
            evento = window.event;
        if (document.getElementById("calFechaFin").src.indexOf("icono.gif") != -1 )
            showCalendar('forms[0]','fechaFin',null,null,null,'','calFechaFin','',null,null,null,null,null,null,null,null,evento);
    }
            
    function cancelar(){
        var resultado = jsp_alerta("","<%=meLanbide35I18n.getMensaje(idiomaUsuario, "mens.preguntaCancelar")%>");
        if (resultado == 1){
            cerrarVentana();
        }
    }
    
    
    function cerrarVentana(){
      if(navigator.appName=="Microsoft Internet Explorer") { 
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
    
    function inicializarErroresCamposPrepJus(){
        camposErrores = new Array();
        for(var i = 0; i < <%=FilaPreparadorJustificacionVO.NUM_CAMPOS_FILA%>; i++){
            camposErrores[i] = '<%=ConstantesMeLanbide35.FALSO%>';
        }
    }
    
    function guardar(){
        if(validarDatos()){
            
            document.getElementById('msgGuardandoDatos').style.display="inline";
            barraProgresoEca('on', 'barraProgresoNuevoPreparadorSolicitud');
            var ajax = getXMLHttpRequest();
            var nodos = null;
            var CONTEXT_PATH = '<%=request.getContextPath()%>'
            var url = CONTEXT_PATH + "/PeticionModuloIntegracion.do";
            var parametros = "";
            var control = new Date();
            parametros = 'tarea=preparar&modulo=MELANBIDE35&operacion=guardarPreparadorJustificacion&tipo=0&numero=<%=numExpediente%>'
                +'&idPrep=<%=preparadorModif != null && preparadorModif.getJusPreparadoresCod() != null ? preparadorModif.getJusPreparadoresCod() : ""%>'
                +'&nif='+escape(document.getElementById('nif').value)
                +'&nomApel='+escape(document.getElementById('nomApel').value)
                +'&feIni='+document.getElementById('fechaInicio').value
                +'&feFin='+document.getElementById('fechaFin').value
                +'&horasAnualesJC='+convertirANumero(document.getElementById('horasAnualesJC').value)
                +'&horasContrato='+convertirANumero(document.getElementById('horasContrato').value)
                +'&horasECA='+convertirANumero(document.getElementById('horasDedicacionEca').value)
                +'&costesSSJor='+convertirANumero(document.getElementById('costesSalarialesSSJor').value)
                +'&costesSSPorJor='+convertirANumero(document.getElementById('costesSalarialesSSPorJor').value)
                +'&costesSSECA='+convertirANumero(document.getElementById('costesSalarialesSSEca').value)
                +'&tipoContrato='+document.getElementById('codTipoContrato').value
                +'&idPrepOrigen=<%=preparadorOrigen != null && preparadorOrigen.getJusPreparadoresCod() != null ? preparadorOrigen.getJusPreparadoresCod() : ""%>'
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
                
            var listaErrores = new Array();           
            var nodoErrores;
            var nodoCampo;
            var errores;           
            var camposErrores;            
            
            var elemento = nodos[0];    
            
            var hijos = elemento.childNodes;
            var codigoOperacion = null;
            var msgValidacion = null;
            var listaPreparadores = new Array();
            var fila = new Array();
            var nodoFila;
            var hijosFila;
            var j;
            
            inicializarErroresCamposPrepJus();
            
            for(j=0;hijos!=null && j<hijos.length;j++){
                if(hijos[j].nodeName=="CODIGO_OPERACION"){                            
                    codigoOperacion = hijos[j].childNodes[0].nodeValue;
                    listaPreparadores[j] = codigoOperacion;
                }//if(hijos[j].nodeName=="CODIGO_OPERACION")                      
                else if(hijos[j].nodeName=="FILA"){
                    nodoFila = hijos[j];
                    hijosFila = nodoFila.childNodes;
                    camposErrores = new Array();
                    for(var cont = 0; cont < hijosFila.length; cont++){
                        if(hijosFila[cont].nodeName=="ID"){
                            nodoCampo = hijosFila[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[0] = hijosFila[cont].childNodes[0].childNodes[0].nodeValue;
                            }
                            else{
                                fila[0] = '-';
                            }
                        }
                        else if(hijosFila[cont].nodeName=="NIF"){
                            nodoCampo = hijosFila[cont];
                            if(nodoCampo.childNodes.length > 0){                                
                                 fila[<%=FilaPreparadorJustificacionVO.POS_CAMPO_NIF%>] = nodoCampo.childNodes[0].childNodes[0].nodeValue;
                                camposErrores[<%=FilaPreparadorJustificacionVO.POS_CAMPO_NIF%>] = nodoCampo.childNodes[1].childNodes[0].nodeValue;
                            }
                            else{
                                fila[<%=FilaPreparadorJustificacionVO.POS_CAMPO_NIF%>] = '-';
                            }
                        }
                        else if(hijosFila[cont].nodeName=="NOMBRE"){
                            nodoCampo = hijosFila[cont];
                            if(nodoCampo.childNodes.length > 0){
                                 fila[<%=FilaPreparadorJustificacionVO.POS_CAMPO_NOMBREAPEL%>] = nodoCampo.childNodes[0].childNodes[0].nodeValue;
                                camposErrores[<%=FilaPreparadorJustificacionVO.POS_CAMPO_NOMBREAPEL%>] = nodoCampo.childNodes[1].childNodes[0].nodeValue;
                            }
                            else{
                                fila[<%=FilaPreparadorJustificacionVO.POS_CAMPO_NOMBREAPEL%>] = '-';
                            }    
                        }
                        else if(hijosFila[cont].nodeName=="FECHA_INICIO"){
                            nodoCampo = hijosFila[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[<%=FilaPreparadorJustificacionVO.POS_CAMPO_FECHA_INICIO%>] = nodoCampo.childNodes[0].childNodes[0].nodeValue;
                                camposErrores[<%=FilaPreparadorJustificacionVO.POS_CAMPO_FECHA_INICIO%>] = nodoCampo.childNodes[1].childNodes[0].nodeValue;
                            }
                            else{
                                fila[<%=FilaPreparadorJustificacionVO.POS_CAMPO_FECHA_INICIO%>] = '-';
                            }
                        }
                        else if(hijosFila[cont].nodeName=="FECHA_FIN"){
                            nodoCampo = hijosFila[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[<%=FilaPreparadorJustificacionVO.POS_CAMPO_FECHA_FIN%>] = nodoCampo.childNodes[0].childNodes[0].nodeValue;
                                camposErrores[<%=FilaPreparadorJustificacionVO.POS_CAMPO_FECHA_FIN%>] = nodoCampo.childNodes[1].childNodes[0].nodeValue;
                            }
                            else{
                                fila[<%=FilaPreparadorJustificacionVO.POS_CAMPO_FECHA_FIN%>] = '-';
                            }
                        }else if(hijosFila[cont].nodeName=="TIPO_CONTRATO"){
                            nodoCampo = hijosFila[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[<%=FilaPreparadorJustificacionVO.POS_CAMPO_TIPO_CONTRATO%>] = nodoCampo.childNodes[0].childNodes[0].nodeValue;
                                camposErrores[<%=FilaPreparadorJustificacionVO.POS_CAMPO_TIPO_CONTRATO%>] = nodoCampo.childNodes[1].childNodes[0].nodeValue;
                            }
                            else{
                                fila[<%=FilaPreparadorJustificacionVO.POS_CAMPO_TIPO_CONTRATO%>] = '-';
                            }
                        }
                        else if(hijosFila[cont].nodeName=="HORAS_ANUALES"){
                            nodoCampo = hijosFila[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[<%=FilaPreparadorJustificacionVO.POS_CAMPO_HORAS_ANUALES%>] = nodoCampo.childNodes[0].childNodes[0].nodeValue;
                                camposErrores[<%=FilaPreparadorJustificacionVO.POS_CAMPO_HORAS_ANUALES%>] = nodoCampo.childNodes[1].childNodes[0].nodeValue;
                            }
                            else{
                                fila[<%=FilaPreparadorJustificacionVO.POS_CAMPO_HORAS_ANUALES%>] = '-';
                            }
                        }
                        else if(hijosFila[cont].nodeName=="HORAS_CONTRATO"){
                            nodoCampo = hijosFila[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[<%=FilaPreparadorJustificacionVO.POS_CAMPO_HORAS_CONTRATO%>] = nodoCampo.childNodes[0].childNodes[0].nodeValue;
                                camposErrores[<%=FilaPreparadorJustificacionVO.POS_CAMPO_HORAS_CONTRATO%>] = nodoCampo.childNodes[1].childNodes[0].nodeValue;
                            }
                            else{
                                fila[<%=FilaPreparadorJustificacionVO.POS_CAMPO_HORAS_CONTRATO%>] = '-';
                            }
                        }
                        else if(hijosFila[cont].nodeName=="HORAS_DEDICACION_ECA"){
                            nodoCampo = hijosFila[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[<%=FilaPreparadorJustificacionVO.POS_CAMPO_HORAS_DEDICACION_ECA%>] = nodoCampo.childNodes[0].childNodes[0].nodeValue;
                                camposErrores[<%=FilaPreparadorJustificacionVO.POS_CAMPO_HORAS_DEDICACION_ECA%>] = nodoCampo.childNodes[1].childNodes[0].nodeValue;
                            }
                            else{
                                fila[<%=FilaPreparadorJustificacionVO.POS_CAMPO_HORAS_DEDICACION_ECA%>] = '-';
                            }
                        }
                        else if(hijosFila[cont].nodeName=="COSTES_SALARIALES_SS_JOR"){
                            nodoCampo = hijosFila[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[<%=FilaPreparadorJustificacionVO.POS_CAMPO_COSTES_SALARIALES_SS_JOR%>] = nodoCampo.childNodes[0].childNodes[0].nodeValue;
                                camposErrores[<%=FilaPreparadorJustificacionVO.POS_CAMPO_COSTES_SALARIALES_SS_JOR%>] = nodoCampo.childNodes[1].childNodes[0].nodeValue;
                            }
                            else{
                                fila[<%=FilaPreparadorJustificacionVO.POS_CAMPO_COSTES_SALARIALES_SS_JOR%>] = '-';
                            }
                        }
                        else if(hijosFila[cont].nodeName=="COSTES_SALARIALES_SS_POR_JOR"){
                            nodoCampo = hijosFila[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[<%=FilaPreparadorJustificacionVO.POS_CAMPO_COSTES_SALARIALES_SS_POR_JOR%>] = nodoCampo.childNodes[0].childNodes[0].nodeValue;
                                camposErrores[<%=FilaPreparadorJustificacionVO.POS_CAMPO_COSTES_SALARIALES_SS_POR_JOR%>] = nodoCampo.childNodes[1].childNodes[0].nodeValue;
                            }
                            else{
                                fila[<%=FilaPreparadorJustificacionVO.POS_CAMPO_COSTES_SALARIALES_SS_POR_JOR%>] = '-';
                            }
                        }
                        else if(hijosFila[cont].nodeName=="COSTES_SALARIALES_SS_ECA"){
                            nodoCampo = hijosFila[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[<%=FilaPreparadorJustificacionVO.POS_CAMPO_COSTES_SALARIALES_SS_ECA%>] = nodoCampo.childNodes[0].childNodes[0].nodeValue;
                                camposErrores[<%=FilaPreparadorJustificacionVO.POS_CAMPO_COSTES_SALARIALES_SS_ECA%>] = nodoCampo.childNodes[1].childNodes[0].nodeValue;
                            }
                            else{
                                fila[<%=FilaPreparadorJustificacionVO.POS_CAMPO_COSTES_SALARIALES_SS_ECA%>] = '-';
                            }
                        }
                        else if(hijosFila[cont].nodeName=="NUM_SEG_ANTERIORES"){
                            nodoCampo = hijosFila[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[<%=FilaPreparadorJustificacionVO.POS_CAMPO_NUM_SEG_ANTERIORES%>] = nodoCampo.childNodes[0].childNodes[0].nodeValue;
                                camposErrores[<%=FilaPreparadorJustificacionVO.POS_CAMPO_NUM_SEG_ANTERIORES%>] = nodoCampo.childNodes[1].childNodes[0].nodeValue;
                            }
                            else{
                                fila[<%=FilaPreparadorJustificacionVO.POS_CAMPO_NUM_SEG_ANTERIORES%>] = '-';
                            }
                        }
                        else if(hijosFila[cont].nodeName=="IMPORTE"){
                            nodoCampo = hijosFila[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[<%=FilaPreparadorJustificacionVO.POS_CAMPO_IMPORTE%>] = nodoCampo.childNodes[0].childNodes[0].nodeValue;
                                camposErrores[<%=FilaPreparadorJustificacionVO.POS_CAMPO_IMPORTE%>] = nodoCampo.childNodes[1].childNodes[0].nodeValue;
                            }
                            else{
                                fila[<%=FilaPreparadorJustificacionVO.POS_CAMPO_IMPORTE%>] = '-';
                            }
                        }
                        else if(hijosFila[cont].nodeName=="C1H"){
                            nodoCampo = hijosFila[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[<%=FilaPreparadorJustificacionVO.POS_CAMPO_C1H%>] = nodoCampo.childNodes[0].childNodes[0].nodeValue;
                                camposErrores[<%=FilaPreparadorJustificacionVO.POS_CAMPO_C1H%>] = nodoCampo.childNodes[1].childNodes[0].nodeValue;
                            }
                            else{
                                fila[<%=FilaPreparadorJustificacionVO.POS_CAMPO_C1H%>] = '-';
                            }
                        }
                        else if(hijosFila[cont].nodeName=="C1M"){
                            nodoCampo = hijosFila[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[<%=FilaPreparadorJustificacionVO.POS_CAMPO_C1M%>] = nodoCampo.childNodes[0].childNodes[0].nodeValue;
                                camposErrores[<%=FilaPreparadorJustificacionVO.POS_CAMPO_C1M%>] = nodoCampo.childNodes[1].childNodes[0].nodeValue;
                            }
                            else{
                                fila[<%=FilaPreparadorJustificacionVO.POS_CAMPO_C1M%>] = '-';
                            }
                        }
                        else if(hijosFila[cont].nodeName=="C1TOTAL"){
                            nodoCampo = hijosFila[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[<%=FilaPreparadorJustificacionVO.POS_CAMPO_C1TOTAL%>] = nodoCampo.childNodes[0].childNodes[0].nodeValue;
                                camposErrores[<%=FilaPreparadorJustificacionVO.POS_CAMPO_C1TOTAL%>] = nodoCampo.childNodes[1].childNodes[0].nodeValue;
                            }
                            else{
                                fila[<%=FilaPreparadorJustificacionVO.POS_CAMPO_C1TOTAL%>] = '-';
                            }
                        }
                        else if(hijosFila[cont].nodeName=="C2H"){
                           nodoCampo = hijosFila[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[<%=FilaPreparadorJustificacionVO.POS_CAMPO_C2H%>] = nodoCampo.childNodes[0].childNodes[0].nodeValue;
                                camposErrores[<%=FilaPreparadorJustificacionVO.POS_CAMPO_C2H%>] = nodoCampo.childNodes[1].childNodes[0].nodeValue;
                            }
                            else{
                                fila[<%=FilaPreparadorJustificacionVO.POS_CAMPO_C2H%>] = '-';
                            }
                        }
                        else if(hijosFila[cont].nodeName=="C2M"){
                            nodoCampo = hijosFila[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[<%=FilaPreparadorJustificacionVO.POS_CAMPO_C2M%>] = nodoCampo.childNodes[0].childNodes[0].nodeValue;
                                camposErrores[<%=FilaPreparadorJustificacionVO.POS_CAMPO_C2M%>] = nodoCampo.childNodes[1].childNodes[0].nodeValue;
                            }
                            else{
                                fila[<%=FilaPreparadorJustificacionVO.POS_CAMPO_C2M%>] = '-';
                            }
                        }
                        else if(hijosFila[cont].nodeName=="C2TOTAL"){
                            nodoCampo = hijosFila[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[<%=FilaPreparadorJustificacionVO.POS_CAMPO_C2TOTAL%>] = nodoCampo.childNodes[0].childNodes[0].nodeValue;
                                camposErrores[<%=FilaPreparadorJustificacionVO.POS_CAMPO_C2TOTAL%>] = nodoCampo.childNodes[1].childNodes[0].nodeValue;
                            }
                            else{
                                fila[<%=FilaPreparadorJustificacionVO.POS_CAMPO_C2TOTAL%>] = '-';
                            }
                        }
                        else if(hijosFila[cont].nodeName=="C3H"){
                           nodoCampo = hijosFila[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[<%=FilaPreparadorJustificacionVO.POS_CAMPO_C3H%>] = nodoCampo.childNodes[0].childNodes[0].nodeValue;
                                camposErrores[<%=FilaPreparadorJustificacionVO.POS_CAMPO_C3H%>] = nodoCampo.childNodes[1].childNodes[0].nodeValue;
                            }
                            else{
                                fila[<%=FilaPreparadorJustificacionVO.POS_CAMPO_C3H%>] = '-';
                            }
                        }
                        else if(hijosFila[cont].nodeName=="C3M"){
                            nodoCampo = hijosFila[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[<%=FilaPreparadorJustificacionVO.POS_CAMPO_C3M%>] = nodoCampo.childNodes[0].childNodes[0].nodeValue;
                                camposErrores[<%=FilaPreparadorJustificacionVO.POS_CAMPO_C3M%>] = nodoCampo.childNodes[1].childNodes[0].nodeValue;
                            }
                            else{
                                fila[<%=FilaPreparadorJustificacionVO.POS_CAMPO_C3M%>] = '-';
                            }
                        }
                        else if(hijosFila[cont].nodeName=="C3TOTAL"){
                            nodoCampo = hijosFila[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[<%=FilaPreparadorJustificacionVO.POS_CAMPO_C3TOTAL%>] = nodoCampo.childNodes[0].childNodes[0].nodeValue;
                                camposErrores[<%=FilaPreparadorJustificacionVO.POS_CAMPO_C3TOTAL%>] = nodoCampo.childNodes[1].childNodes[0].nodeValue;
                            }
                            else{
                                fila[<%=FilaPreparadorJustificacionVO.POS_CAMPO_C3TOTAL%>] = '-';
                            }
                        }
                        else if(hijosFila[cont].nodeName=="C4H"){
                            nodoCampo = hijosFila[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[<%=FilaPreparadorJustificacionVO.POS_CAMPO_C4H%>] = nodoCampo.childNodes[0].childNodes[0].nodeValue;
                                camposErrores[<%=FilaPreparadorJustificacionVO.POS_CAMPO_C4H%>] = nodoCampo.childNodes[1].childNodes[0].nodeValue;
                            }
                            else{
                                fila[<%=FilaPreparadorJustificacionVO.POS_CAMPO_C4H%>] = '-';
                            }
                        }
                        else if(hijosFila[cont].nodeName=="C4M"){
                           nodoCampo = hijosFila[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[<%=FilaPreparadorJustificacionVO.POS_CAMPO_C4M%>] = nodoCampo.childNodes[0].childNodes[0].nodeValue;
                                camposErrores[<%=FilaPreparadorJustificacionVO.POS_CAMPO_C4M%>] = nodoCampo.childNodes[1].childNodes[0].nodeValue;
                            }
                            else{
                                fila[<%=FilaPreparadorJustificacionVO.POS_CAMPO_C4M%>] = '-';
                            }
                        }
                        else if(hijosFila[cont].nodeName=="C4TOTAL"){
                            nodoCampo = hijosFila[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[<%=FilaPreparadorJustificacionVO.POS_CAMPO_C4TOTAL%>] = nodoCampo.childNodes[0].childNodes[0].nodeValue;
                                camposErrores[<%=FilaPreparadorJustificacionVO.POS_CAMPO_C4TOTAL%>] = nodoCampo.childNodes[1].childNodes[0].nodeValue;
                            }
                            else{
                                fila[<%=FilaPreparadorJustificacionVO.POS_CAMPO_C4TOTAL%>] = '-';
                            }
                        }
                        else if(hijosFila[cont].nodeName=="INSERCIONES"){
                            nodoCampo = hijosFila[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[<%=FilaPreparadorJustificacionVO.POS_CAMPO_INSERCIONES%>] = nodoCampo.childNodes[0].childNodes[0].nodeValue;
                                camposErrores[<%=FilaPreparadorJustificacionVO.POS_CAMPO_INSERCIONES%>] = nodoCampo.childNodes[1].childNodes[0].nodeValue;
                            }
                            else{
                                fila[<%=FilaPreparadorJustificacionVO.POS_CAMPO_INSERCIONES%>] = '-';
                            }
                        }
                        else if(hijosFila[cont].nodeName=="SEGUIMIENTOS_INSERCIONES"){
                            nodoCampo = hijosFila[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[<%=FilaPreparadorJustificacionVO.POS_CAMPO_SEGUIMIENTOS_INSERCIONES%>] = nodoCampo.childNodes[0].childNodes[0].nodeValue;
                                camposErrores[<%=FilaPreparadorJustificacionVO.POS_CAMPO_SEGUIMIENTOS_INSERCIONES%>] = nodoCampo.childNodes[1].childNodes[0].nodeValue;
                            }
                            else{
                                fila[<%=FilaPreparadorJustificacionVO.POS_CAMPO_SEGUIMIENTOS_INSERCIONES%>] = '-';
                            }
                        }
                        else if(hijosFila[cont].nodeName=="COSTES_SALARIALES_SS"){
                            nodoCampo = hijosFila[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[<%=FilaPreparadorJustificacionVO.POS_CAMPO_COSTES_SALARIALES_SS%>] = nodoCampo.childNodes[0].childNodes[0].nodeValue;
                                camposErrores[<%=FilaPreparadorJustificacionVO.POS_CAMPO_COSTES_SALARIALES_SS%>] = nodoCampo.childNodes[1].childNodes[0].nodeValue;
                            }
                            else{
                                fila[<%=FilaPreparadorJustificacionVO.POS_CAMPO_COSTES_SALARIALES_SS%>] = '-';
                            }
                        }
                        else if(hijosFila[cont].nodeName=="ERRORES"){
                            listaErrores = new Array();
                            nodoErrores = hijosFila[cont];
                            errores = nodoErrores.childNodes;
                            for(var contE = 0; contE < errores.length; contE++){
                                if(errores[contE].nodeName=="ERROR"){
                                    if(errores[contE].childNodes.length > 0){
                                        listaErrores[contE] = errores[contE].childNodes[0].nodeValue;
                                    }
                                }
                            }
                            fila[29] = listaErrores;
                        }
                        else if(hijosFila[cont].nodeName=="ES_SUSTITUTO"){
                            nodoCampo = hijosFila[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[31] = nodoCampo.childNodes[0].nodeValue;
                            }
                            else{
                                fila[31] = '0';
                            }
                        }
                    }
                    fila[30] = camposErrores;
                    listaPreparadores[j] = fila;
                    fila = new Array();
                    camposErrores = new Array();
                }else if(hijos[j].nodeName=="VALIDACION"){        
                    msgValidacion = hijos[j].childNodes[0].nodeValue;
                }
            }//for(j=0;hijos!=null && j<hijos.length;j++)
                if(codigoOperacion=="0"){
                    self.parent.opener.retornoXanelaAuxiliar(listaPreparadores);
                    barraProgresoEca('off', 'barraProgresoNuevoPreparadorSolicitud');
                    jsp_alerta("A",'<%=meLanbide35I18n.getMensaje(idiomaUsuario,"msg.datosGuardadosOK")%>');
                    cerrarVentana();
                }else if(codigoOperacion=="1"){
                    jsp_alerta("A",'<%=meLanbide35I18n.getMensaje(idiomaUsuario,"error.errorBD")%>');
                }else if(codigoOperacion=="2"){
                    jsp_alerta("A",'<%=meLanbide35I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                }else if(codigoOperacion=="3"){
                    jsp_alerta("A",'<%=meLanbide35I18n.getMensaje(idiomaUsuario,"error.pasoParametros")%>');
                }else if(codigoOperacion=="4"){
                    jsp_alerta("A",msgValidacion);
                }else if(codigoOperacion=="5"){
                    jsp_alerta("A",'<%=meLanbide35I18n.getMensaje(idiomaUsuario,"error.nifPreparadorRepetido")%>');
                }else{
                    jsp_alerta("A",'<%=meLanbide35I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                }//if(
            }
            catch(Err){
                jsp_alerta("A",'<%=meLanbide35I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
            }//try-catch
            
            barraProgresoEca('off', 'barraProgresoNuevoPreparadorSolicitud');
        }else{
            jsp_alerta("A", escape(mensajeValidacion));
        }
    }
    
    function validarDatos(){
        var nif = document.getElementById('nif').value;
        if(nif == null || nif == ''){
            mensajeValidacion = '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "msg.solicitud.preparadores.nifVacio")%>';
            return false;
        }
        var nomApel = document.getElementById('nomApel').value;
        if(nomApel == null || nomApel == ''){
            mensajeValidacion = '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "msg.solicitud.preparadores.nomApelVacio")%>';
            return false;
        }
        
        
        var correcto = true;
        mensajeValidacion = '';
        
       if(nuevo){
            if(!validarNIFEca(document.getElementById('nif'))){
                document.getElementById('nif').style.border = '1px solid red';
                if(mensajeValidacion == ''){
                    var letra = calcularLetraNifEca(document.getElementById('nif').value);
                    if(letra != ''){
                        mensajeValidacion = '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "msg.solicitud.preparadores.nifIncorrecto")%>'+' '+'<%=meLanbide35I18n.getMensaje(idiomaUsuario, "msg.solicitud.preparadores.letraNifDeberiaSer")%>'+letra;
                    }else{
                        mensajeValidacion = '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "msg.solicitud.preparadores.nifIncorrecto")%>';
                    }
                }
                correcto = false;
            }else{
                document.getElementById('nif').removeAttribute("style");
            }
        }


        if(!comprobarCaracteresEspecialesEca(nomApel)){
            document.getElementById('nomApel').style.border = '1px solid red';
            mensajeValidacion = '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "msg.solicitud.preparadores.nomApelCaracteresEspeciales")%>';
            return false;
        }else{
            document.getElementById('nomApel').removeAttribute("style");
        }
         
        if (nuevo){

            if(!validarFechaEca(document.forms[0], document.getElementById('fechaInicio'))){
                document.getElementById('fechaInicio').style.border = '1px solid red';
                if(mensajeValidacion == '')
                    mensajeValidacion = '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "msg.solicitud.preparadores.fechaIniIncorrecto")%>';
                correcto = false;
            }else{
                document.getElementById('fechaInicio').removeAttribute('style');
            }

            if(!validarFechaEca(document.forms[0], document.getElementById('fechaFin'))){
                document.getElementById('fechaFin').style.border = '1px solid red';
                if(mensajeValidacion == '')
                    mensajeValidacion = '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "msg.solicitud.preparadores.fechaFinIncorrecto")%>';
                correcto = false;
            }else{
                document.getElementById('fechaFin').removeAttribute('style');
            }
            if(correcto){
                var feIni = document.getElementById('fechaInicio').value;
                var feFin = document.getElementById('fechaFin').value;

                var array_fecha_ini = feIni.split('/');
                var diaIni = array_fecha_ini[0];
                var mesIni = array_fecha_ini[1];
                var anoIni = array_fecha_ini[2];
                var dIni = new Date(anoIni, mesIni-1, diaIni, 0, 0, 0, 0);

                var array_fecha_fin = feFin.split('/');
                var diaFin = array_fecha_fin[0];
                var mesFin = array_fecha_fin[1];
                var anoFin = array_fecha_fin[2];
                var dFin = new Date(anoFin, mesFin-1, diaFin, 0, 0, 0, 0);


                var n1 = dFin.getTime();
                var n2 = dIni.getTime();
                var result = n1 - n2;
                if(result < 0){
                    document.getElementById('fechaInicio').style.border = '1px solid red';
                    document.getElementById('fechaFin').style.border = '1px solid red';
                    if(mensajeValidacion == '')
                        mensajeValidacion = '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "msg.solicitud.preparadores.fechaIniPosteriorFin")%>';
                    correcto = false;
                }
                else{
                    document.getElementById('fechaInicio').removeAttribute('style');
                    document.getElementById('fechaFin').removeAttribute('style');
                }
                
                
                //COMPROBACION FECHAS ORIGEN Y SUSTITUTO
                <% if (preparadorOrigen != null){ %>
                    /*var finiOrigen = '<%= preparadorOrigen.getFecIni() %>';
                    var ffinOrigen = '<%= preparadorOrigen.getFecFin() %>';
                    
                    var array_fecha_iniOrigen = finiOrigen.split('-');
                    var anoIniOrigen = array_fecha_iniOrigen[0];
                    var mesIniOrigen = array_fecha_iniOrigen[1];
                    var diaIniOrigen = array_fecha_iniOrigen[2];
                    var dIniOrigen = new Date(anoIniOrigen, mesIniOrigen-1, diaIniOrigen, 0, 0, 0, 0);
                    var timedIniorigen = dIniOrigen.getTime();
                    
                    var array_fecha_finOrigen = ffinOrigen.split('-');
                    var anoFinOrigen = array_fecha_finOrigen[0];
                    var mesFinOrigen = array_fecha_finOrigen[1];
                    var diaFinOrigen = array_fecha_finOrigen[2];
                    var dFinOrigen = new Date(anoFinOrigen, mesFinOrigen-1, diaFinOrigen, 0, 0, 0, 0);
                    var timedFinorigen = dFinOrigen.getTime();
                    //finisustituto-ffinorigen>0
                    var resultFcontrato = n2 - timedFinorigen ;
                    if (resultFcontrato <= 0){
                    //alert('fechas origen:'+finiOrigen+' - '+ffinOrigen);
                        document.getElementById('fechaInicio').style.border = '1px solid red';
                        //document.getElementById('fechaFin').style.border = '1px solid red';
                        if(mensajeValidacion == '')
                            mensajeValidacion = '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "msg.justificacion.preparadores.fechaIniAnteriorAOrigen")%>';
                        correcto = false;
                    }*/
                <% } %>
                
            }
            
            
            //tipo contrato       
            if(document.getElementById('codTipoContrato').value == ''){
                 correcto = false;
                 document.getElementById('codTipoContrato').style.border = '1px solid red';
                 document.getElementById('descTipoContrato').style.border = '1px solid red';
                 if(mensajeValidacion == '')
                    mensajeValidacion = '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "validaciones.justificacion.preparadores.tipoContratoOblig")%>';

            }else document.getElementById('codTipoContrato').removeAttribute("style");
       
        }
        else //modificar) 
        {
        //COMPROBACION FECHAS ORIGEN Y SUSTITUTO
            <% if (preparadorOrigen != null){ %>
               /*var feIni = document.getElementById('fechaInicio').value;
               var array_fecha_ini = feIni.split('/');
                var diaIni = array_fecha_ini[0];
                var mesIni = array_fecha_ini[1];
                var anoIni = array_fecha_ini[2];
                var dIni = new Date(anoIni, mesIni-1, diaIni, 0, 0, 0, 0);
                var n2 = dIni.getTime();
                   
                    var finiOrigen = '<%= preparadorOrigen.getFecIni() %>';
                    var ffinOrigen = '<%= preparadorOrigen.getFecFin() %>';
                    
                    var array_fecha_iniOrigen = finiOrigen.split('-');
                    var anoIniOrigen = array_fecha_iniOrigen[0];
                    var mesIniOrigen = array_fecha_iniOrigen[1];
                    var diaIniOrigen = array_fecha_iniOrigen[2];
                    var dIniOrigen = new Date(anoIniOrigen, mesIniOrigen-1, diaIniOrigen, 0, 0, 0, 0);
                    var timedIniorigen = dIniOrigen.getTime();
                    
                    var array_fecha_finOrigen = ffinOrigen.split('-');
                    var anoFinOrigen = array_fecha_finOrigen[0];
                    var mesFinOrigen = array_fecha_finOrigen[1];
                    var diaFinOrigen = array_fecha_finOrigen[2];
                    var dFinOrigen = new Date(anoFinOrigen, mesFinOrigen-1, diaFinOrigen, 0, 0, 0, 0);
                    var timedFinorigen = dFinOrigen.getTime();
                    //finisustituto-ffinorigen>0
                    var resultFcontrato = n2 - timedFinorigen ;
                    if (resultFcontrato <= 0){
                    //alert('fechas origen:'+finiOrigen+' - '+ffinOrigen);
                        document.getElementById('fechaInicio').style.border = '1px solid red';
                        //document.getElementById('fechaFin').style.border = '1px solid red';
                        if(mensajeValidacion == '')
                            mensajeValidacion = '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "validaciones.justificacion.preparadores.fechaIniAnteriorAOrigen")%>';
                        correcto = false;
                    }*/
            <% } %>
                
            <% if (sustituto != null){ %>
                    /*var feFin = document.getElementById('fechaFin').value;
                    var array_fecha_fin = feFin.split('/');
                    var diaFin = array_fecha_fin[0];
                    var mesFin = array_fecha_fin[1];
                    var anoFin = array_fecha_fin[2];
                    var dFin = new Date(anoFin, mesFin-1, diaFin, 0, 0, 0, 0);
                    var n1 = dFin.getTime();
                
                    var finiSustituto = '<%= sustituto.getFecIni() %>';
                    var ffinSustituto = '<%= sustituto.getFecFin() %>';
                    
                    var array_fecha_iniSustituto = finiSustituto.split('-');
                    var anoIniSustituto = array_fecha_iniSustituto[0];
                    var mesIniSustituto = array_fecha_iniSustituto[1];
                    var diaIniSustituto = array_fecha_iniSustituto[2];
                    var dIniSustituto = new Date(anoIniSustituto, mesIniSustituto-1, diaIniSustituto, 0, 0, 0, 0);
                    var timedIniSustituto = dIniSustituto.getTime();
                                      
                    var resultFcontrato = timedIniSustituto - n1 ;
                    if (resultFcontrato <= 0){                   
                        document.getElementById('fechaFin').style.border = '1px solid red';
                        if(mensajeValidacion == '')
                            mensajeValidacion = '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "validacion.justificacion.preparadores.fechaIniAnteriorAOrigen")%>';
                        correcto = false;
                    }*/
            <% } %>
                
        }
        //TODO: Fecha de inicio o fin en el año del expediente ???
        
        //Horas Anuales Jornada Completa >= Horas contrato >= Horas dedicación ECA
        var hajc = 0.0;
        var hc = 0.0;
        var hdECA = 0.0;

        if(document.getElementById('horasAnualesJC').value != ''){
            try{
                if(!validarNumericoDecimalEca(document.getElementById('horasAnualesJC'), 8, 2)){
                    correcto = false;
                    document.getElementById('horasAnualesJC').style.border = '1px solid red';
                    if(mensajeValidacion == '')
                        mensajeValidacion = '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "msg.solicitud.preparadores.horasAnualesJCIncorrecto")%>';
                }else{
                    document.getElementById('horasAnualesJC').removeAttribute('style');
                    hajc = parseFloat(convertirANumero(document.getElementById('horasAnualesJC').value));
                }
            }catch(err){
                correcto = false;
                document.getElementById('horasAnualesJC').style.border = '1px solid red';
                if(mensajeValidacion == '')
                    mensajeValidacion = '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "msg.solicitud.preparadores.horasAnualesJCIncorrecto")%>';
            }
        }
        if(document.getElementById('horasContrato').value != ''){
            try{
                if(!validarNumericoDecimalEca(document.getElementById('horasContrato'), 8, 2)){
                    correcto = false;
                    document.getElementById('horasContrato').style.border = '1px solid red';
                    if(mensajeValidacion == '')
                        mensajeValidacion = '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "msg.solicitud.preparadores.horasContratoIncorrecto")%>';
                }else{
                    document.getElementById('horasContrato').removeAttribute('style');
                    hc = parseFloat(convertirANumero(document.getElementById('horasContrato').value));
                }
            }catch(err){
                correcto = false;
                document.getElementById('horasContrato').style.border = '1px solid red';
                if(mensajeValidacion == '')
                    mensajeValidacion = '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "msg.solicitud.preparadores.horasContratoIncorrecto")%>';
            }
        }
        if(document.getElementById('horasDedicacionEca').value != ''){
            try{
                if(!validarNumericoDecimalEca(document.getElementById('horasDedicacionEca'), 8, 2)){
                    correcto = false;
                    document.getElementById('horasDedicacionEca').style.border = '1px solid red';
                    if(mensajeValidacion == '')
                        mensajeValidacion = '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "msg.solicitud.preparadores.horasDedicacionEcaIncorrecto")%>';
                }else{
                    document.getElementById('horasDedicacionEca').removeAttribute('style');
                    hdECA = parseFloat(convertirANumero(document.getElementById('horasDedicacionEca').value));
                }
            }catch(err){
                correcto = false;
                document.getElementById('horasDedicacionEca').style.border = '1px solid red';
                if(mensajeValidacion == '')
                    mensajeValidacion = '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "msg.solicitud.preparadores.horasDedicacionEcaIncorrecto")%>';
            }
        }

        if(correcto && nuevo){
            if(hajc < hc){
                correcto = false;
                document.getElementById('horasAnualesJC').style.border = '1px solid red';
                if(mensajeValidacion == '')
                    mensajeValidacion = '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "msg.solicitud.preparadores.horasAnualesJCMenorHorasContrato")%>';
            }
            else{
                document.getElementById('horasAnualesJC').removeAttribute('style');
            }
            if(hc < hdECA){
                correcto = false;
                document.getElementById('horasContrato').style.border = '1px solid red';
                if(mensajeValidacion == '')
                    mensajeValidacion = '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "msg.solicitud.preparadores.horasContratoMenorHorasDedicacionEca")%>';
            }else{
                document.getElementById('horasContrato').removeAttribute('style');
            }
        }
        
        //Horas dedicación ECA >= 50% Horas contrato
        hc = 0.0;
        hdECA = 0.0;
        if(document.getElementById('horasContrato').value != ''){
            try{
                if(!validarNumericoDecimalEca(document.getElementById('horasContrato'), 8, 2)){
                    correcto = false;
                    document.getElementById('horasContrato').style.border = '1px solid red';
                    if(mensajeValidacion == '')
                        mensajeValidacion = '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "msg.solicitud.preparadores.horasContratoIncorrecto")%>';
                }else{
                    document.getElementById('horasContrato').removeAttribute('style');
                    hc = parseFloat(convertirANumero(document.getElementById('horasContrato').value));
                }
            }catch(err){
                correcto = false;
                document.getElementById('horasContrato').style.border = '1px solid red';
                if(mensajeValidacion == '')
                    mensajeValidacion = '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "msg.solicitud.preparadores.horasContratoIncorrecto")%>';
            }
        }
        if(document.getElementById('horasDedicacionEca').value != ''){
            try{
                if(!validarNumericoDecimalEca(document.getElementById('horasDedicacionEca'), 8, 2)){
                    correcto = false;
                    document.getElementById('horasDedicacionEca').style.border = '1px solid red';
                    if(mensajeValidacion == '')
                        mensajeValidacion = '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "msg.solicitud.preparadores.horasDedicacionEcaIncorrecto")%>';
                }else{
                    document.getElementById('horasDedicacionEca').removeAttribute('style');
                    hdECA = parseFloat(convertirANumero(document.getElementById('horasDedicacionEca').value));
                }
            }catch(err){
                correcto = false;
                document.getElementById('horasDedicacionEca').style.border = '1px solid red';
                if(mensajeValidacion == '')
                    mensajeValidacion = '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "msg.solicitud.preparadores.horasDedicacionEcaIncorrecto")%>';
            }
        }

        if(correcto && nuevo){
            hc *= 0.5;
            if(hdECA < hc){
                correcto = false;
                document.getElementById('horasDedicacionEca').style.border = '1px solid red';
                if(mensajeValidacion == '')
                    mensajeValidacion = '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "msg.justificacion.preparadores.horasDedicacionEca50HorasContrato")%>';
            }else{
                document.getElementById('horasDedicacionEca').removeAttribute('style');
            }
        }
        
        
        //Costes salariales + Seg social JC >= Costes salariales + Seg social Contrato >= Costes salariales + Seg social dedicación ECA
        var cssJC = 0.0;
        var cssPorJor = 0.0;
        var cssECA = 0.0;

        if(document.getElementById('costesSalarialesSSJor').value != ''){
            try{
                if(!validarNumericoDecimalEca(document.getElementById('costesSalarialesSSJor'), 8, 2)){
                    correcto = false;
                    document.getElementById('costesSalarialesSSJor').style.border = '1px solid red';
                    if(mensajeValidacion == '')
                        mensajeValidacion = '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "msg.solicitud.preparadores.costesSSJorIncorrecto")%>';
                }else{
                    document.getElementById('costesSalarialesSSJor').removeAttribute('style');
                    cssJC = parseFloat(convertirANumero(document.getElementById('costesSalarialesSSJor').value));
                }
            }catch(err){
                correcto = false;
                document.getElementById('costesSalarialesSSJor').style.border = '1px solid red';
                if(mensajeValidacion == '')
                    mensajeValidacion = '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "msg.solicitud.preparadores.costesSSJorIncorrecto")%>';
            }
        }
        if(document.getElementById('costesSalarialesSSPorJor').value != ''){
            try{
                if(!validarNumericoDecimalEca(document.getElementById('costesSalarialesSSPorJor'), 8, 2)){
                    correcto = false;
                    document.getElementById('costesSalarialesSSPorJor').style.border = '1px solid red';
                    if(mensajeValidacion == '')
                        mensajeValidacion = '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "msg.solicitud.preparadores.costesSSPorJorIncorrecto")%>';
                }else{
                    document.getElementById('costesSalarialesSSPorJor').removeAttribute('style');
                    cssPorJor = parseFloat(convertirANumero(document.getElementById('costesSalarialesSSPorJor').value));
                }
            }catch(err){
                correcto = false;
                document.getElementById('costesSalarialesSSPorJor').style.border = '1px solid red';
                if(mensajeValidacion == '')
                    mensajeValidacion = '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "msg.solicitud.preparadores.costesSSPorJorIncorrecto")%>';
            }
        }
        if(document.getElementById('costesSalarialesSSEca').value != ''){
            try{
                if(!validarNumericoDecimalEca(document.getElementById('costesSalarialesSSEca'), 8, 2)){
                    correcto = false;
                    document.getElementById('costesSalarialesSSEca').style.border = '1px solid red';
                    if(mensajeValidacion == '')
                        mensajeValidacion = '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "msg.solicitud.preparadores.costesSSEcaIncorrecto")%>';
                }else{
                    document.getElementById('costesSalarialesSSEca').removeAttribute('style');
                    cssECA = parseFloat(convertirANumero(document.getElementById('costesSalarialesSSEca').value));
                }
            }catch(err){
                correcto = false;
                document.getElementById('costesSalarialesSSEca').style.border = '1px solid red';
                if(mensajeValidacion == '')
                    mensajeValidacion = '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "msg.solicitud.preparadores.costesSSEcaIncorrecto")%>';
            }
        }

        if(correcto && nuevo){
            if(cssJC < cssPorJor){
                correcto = false;
                document.getElementById('costesSalarialesSSJor').style.border = '1px solid red';
                if(mensajeValidacion == '')
                    mensajeValidacion = '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "msg.solicitud.preparadores.costesSalarialesJorMenorCostesSalarialesPorJor")%>';
            }
            else{
                document.getElementById('costesSalarialesSSJor').removeAttribute('style');
            }
            if(cssPorJor < cssECA){
                correcto = false;
                document.getElementById('costesSalarialesSSPorJor').style.border = '1px solid red';
                if(mensajeValidacion == '')
                    mensajeValidacion = '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "msg.solicitud.preparadores.costesSalarialesPorJorMenorCostesSalarialesEca")%>';
            }else{
                document.getElementById('costesSalarialesSSPorJor').removeAttribute('style');
            }
        }

        //TODO: Los totales finales: ¿Calculados o insertados?
        
        return correcto;
    }
            
    function ajustarDecimalesImportes(){
        
        var campo;
        campo = document.getElementById('horasAnualesJC');
        ajustarDecimalesCampo(campo, 2);
        
        campo = document.getElementById('horasContrato');
        ajustarDecimalesCampo(campo, 2);
        
        campo = document.getElementById('horasDedicacionEca');
        ajustarDecimalesCampo(campo, 2);
        
        campo = document.getElementById('costesSalarialesSSJor');
        ajustarDecimalesCampo(campo, 2);
        
        campo = document.getElementById('costesSalarialesSSPorJor');
        ajustarDecimalesCampo(campo, 2);
        
        campo = document.getElementById('costesSalarialesSSEca');
        ajustarDecimalesCampo(campo, 2);
       
       /* var f;
        var v;

        //Horas Anuales Jornada Completa
        v = document.getElementById('horasAnualesJC').value;
        v = convertirANumero(v);
        f = parseFloat(v);
        f = ajustarDecimalesEca(f, 2);
        if(!isNaN(f)){
            document.getElementById('horasAnualesJC').value = f;
            reemplazarPuntosEca(document.getElementById('horasAnualesJC'));
            document.getElementById('horasAnualesJC').removeAttribute("style");
        }

        //Horas Contrato
        v = document.getElementById('horasContrato').value;
        v = convertirANumero(v);
        f = parseFloat(v);
        f = ajustarDecimalesEca(f, 2);
        if(!isNaN(f)){
            document.getElementById('horasContrato').value = f;
            reemplazarPuntosEca(document.getElementById('horasContrato'));
            document.getElementById('horasContrato').removeAttribute("style");
        }

        //Horas Dedicación ECA
        v = document.getElementById('horasDedicacionEca').value;
        v = convertirANumero(v);
        f = parseFloat(v);
        f = ajustarDecimalesEca(f, 2);
        if(!isNaN(f)){
            document.getElementById('horasDedicacionEca').value = f;
            reemplazarPuntosEca(document.getElementById('horasDedicacionEca'));
            document.getElementById('horasDedicacionEca').removeAttribute("style");
        }

        //Costes Salariales + SS Jornada
        v = document.getElementById('costesSalarialesSSJor').value;
        v = convertirANumero(v);
        f = parseFloat(v);
        f = ajustarDecimalesEca(f, 2);
        if(!isNaN(f)){
            document.getElementById('costesSalarialesSSJor').value = f;
            reemplazarPuntosEca(document.getElementById('costesSalarialesSSJor'));
            document.getElementById('costesSalarialesSSJor').removeAttribute("style");
        }

        //Costes Salariales + SS % Jornada
        v = document.getElementById('costesSalarialesSSPorJor').value;
       v = convertirANumero(v);
        f = parseFloat(v);
        f = ajustarDecimalesEca(f, 2);
        if(!isNaN(f)){
            document.getElementById('costesSalarialesSSPorJor').value = f;
            reemplazarPuntosEca(document.getElementById('costesSalarialesSSPorJor'));
            document.getElementById('costesSalarialesSSPorJor').removeAttribute("style");
        }

        //Costes Salariales SS ECA
        v = document.getElementById('costesSalarialesSSEca').value;
        v = convertirANumero(v);
        f = parseFloat(v);
        f = ajustarDecimalesEca(f, 2);
        if(!isNaN(f)){
            document.getElementById('costesSalarialesSSEca').value = f;
            reemplazarPuntosEca(document.getElementById('costesSalarialesSSEca'));
            document.getElementById('costesSalarialesSSEca').removeAttribute("style");
        }*/
    }
    
    function calcularCostesSalarialesECA(){
        var c1 = document.getElementById('horasDedicacionEca');
        var c2 = document.getElementById('costesSalarialesSSJor');
        var c3 = document.getElementById('horasAnualesJC');
        var result = 0.0;
        var valor = null;
        var valorCalculado = false;

        if(c1.value != '' && validarNumericoDecimalEca(c1, 12, 5)){
            if(c2.value != '' && validarNumericoDecimalEca(c2, 12, 5)){
                if(c3.value != '' && validarNumericoDecimalEca(c3, 12, 5)){
                    try{
                        valor = c1.value;
                        valor = convertirANumero(valor);
                        result = parseFloat(valor);

                        valor = c2.value;
                        valor = convertirANumero(valor);
                        result *= parseFloat(valor);

                        valor = c3.value;
                        valor = convertirANumero(valor);
                        result /= parseFloat(valor);
                        
                        if (isFinite(result)){
                        
                            result = result.toFixed(2);

                            document.getElementById('costesSalarialesSSEca').value = result;
                            reemplazarPuntosEca(document.getElementById('costesSalarialesSSEca'));
                        
                            valorCalculado = true;
                        }
                    }catch(err){
                        
                    }
                }
            }
        //}else c1.style="border:1px solid red";
        }else{
            c1.style.border = "1px solid red";
        }
        
        if(!valorCalculado){
            document.getElementById('costesSalarialesSSEca').value = '';
        }
    }
    
    function calcularCostesSalarialesPorJor(){
        var c1 = document.getElementById('horasContrato');
        var c2 = document.getElementById('costesSalarialesSSJor');
        var c3 = document.getElementById('horasAnualesJC');
        var result = 0.0;
        var valor = null;
        var valorCalculado = false;

        if(c1.value != '' && validarNumericoDecimalEca(c1, 12, 5)){
            if(c2.value != '' && validarNumericoDecimalEca(c2, 12, 5)){
                if(c3.value != '' && validarNumericoDecimalEca(c3, 12, 5)){
                    try{
                        valor = c1.value;
                        valor = convertirANumero(valor);
                        result = parseFloat(valor);

                        valor = c2.value;
                        valor = convertirANumero(valor);
                        result *= parseFloat(valor);

                        valor = c3.value;
                        valor = convertirANumero(valor);
                        result /= parseFloat(valor);
                        
                        if (isFinite(result)){
                        
                            result = result.toFixed(2);

                            document.getElementById('costesSalarialesSSPorJor').value = result;
                            reemplazarPuntosEca(document.getElementById('costesSalarialesSSPorJor'));
                        
                            valorCalculado = true;
                        }
                    }catch(err){
                        
                    }
                }
            }
        }
        
        if(!valorCalculado){
            document.getElementById('costesSalarialesSSPorJor').value = '';
        }
    }
    
    function calcularCostesSalarialesSSJC(){
        var a = document.getElementById('horasAnualesJC');
        var f = document.getElementById('costesSalarialesSSEca');
        var c = document.getElementById('horasDedicacionEca');
       
        var result = 0.0;
        var valor = null;
        var valorCalculado = false;
        
        if(a.value != '' && validarNumericoDecimalEca(a, 50, 25)){
            if(f.value != '' && validarNumericoDecimalEca(f, 50, 25)){
                if(c.value != '' && validarNumericoDecimalEca(c, 50, 25)){
                    try{
                        valor = a.value;
                        valor = convertirANumero(valor);
                        result = parseFloat(valor);

                        valor = f.value;
                        valor = convertirANumero(valor);
                        result *= parseFloat(valor);

                        valor = c.value;
                        valor = convertirANumero(valor);
                        result /= parseFloat(valor);
                        
                        if (isFinite(result)){
                        
                            result = result.toFixed(2);

                            document.getElementById('costesSalarialesSSJor').value = result;
                            reemplazarPuntosEca(document.getElementById('costesSalarialesSSJor'));
                        
                            valorCalculado = true;
                        }
                    }catch(err){
                        
                    }
                }
            }
        }
        
        if(!valorCalculado){
            document.getElementById('costesSalarialesSSJor').value = '';
        }
    }
</script>
<body onload="inicio();" class="contenidoPantalla etiqueta">
    <form>
        <div id="barraProgresoNuevoPreparadorSolicitud" style="position: absolute; z-index:10; visibility: hidden; top: 30%; left: 30%;">
            <table width="100%" border="0px" cellpadding="0px" cellspacing="0px" border="0px">
                <tr>
                    <td align="center" valign="middle">
                        <table class="contenedorHidepage" cellpadding="0px" cellspacing="0px" border="0px">
                            <tr>
                                <td>
                                    <table width="349px" height="100%">
                                        <tr>
                                            <td colspan="3" style="height:70%;text-align:center;valign:middle;">
                                                <span id="msgGuardandoDatos">
                                                    <%=meLanbide35I18n.getMensaje(idiomaUsuario, "msg.guardandoDatos")%>
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
        <div style="width: 98%; padding: 10px; text-align: left;">
            <!--<div class="tituloAzul" style="clear: both; text-align: left;">-->
            <div class="sub3titulo" style="clear: both; text-align: left;height:18px;">
                <span>
                    <%=meLanbide35I18n.getMensaje(idiomaUsuario,"label.solicitud.preparadores.datosPreparador")%>
                </span>
            </div>
            <div class="lineaFormulario" id="divOrigen">
                <div style="width: 120px; float: left;">
                    <%=meLanbide35I18n.getMensaje(idiomaUsuario,"label.justificacion.preparadores.preparadorOrigen")%>
                </div>
                <div style="width: 85px; float: left;">
                    <input type="text" id="nifOrigen" name="nifOrigen" size="10" maxlength="10" class="inputTexto readOnly" disabled="disabled"/>
                </div>                
                <div style="width: 540px; float: left;">
                    <input type="text" id="nomApelOrigen" name="nomApelOrigen" size="83" maxlength="200" class="inputTexto readOnly" disabled="disabled"/>
                </div>
               
            </div>
                <div style="clear:both"></div>
                <fieldset>    
                <legend>Preparador</legend>
            <div class="lineaFormulario">
                <div style="width: 36px; float: left;">
                    <%=meLanbide35I18n.getMensaje(idiomaUsuario,"label.solicitud.preparadores.nif")%><font color="red">*</font>
                </div>
                <div style="width: 101px; float: left;">
                    <input type="text" id="nif" name="nif" size="10" maxlength="10" class="inputTexto"/>
                </div>
                <div style="width: 120px; float: left;">
                    <%=meLanbide35I18n.getMensaje(idiomaUsuario,"label.solicitud.preparadores.nomApel")%><font color="red">*</font>
                </div>
                <div style="width: 540px; float: left;">
                    <input type="text" id="nomApel" name="nomApel" size="83" maxlength="200" class="inputTexto"/>
                </div>
            </div>
            <div class="lineaFormulario">
                <div style="width: 100px; float: left;">
                    <%=meLanbide35I18n.getMensaje(idiomaUsuario,"label.justificacion.preparadores.tipocontrato")%><font color="red">*</font>
                </div>
                <div style="width: 220px; float: left;">
                    <input id="codTipoContrato" name="codTipoContrato" type="text" class="inputTexto" size="2" maxlength="3" 
                           onkeypress="javascript:return SoloDigitosConsulta(event);" value="">
                    <input id="descTipoContrato" name="descTipoContrato" type="text" class="inputTexto" size="22" readonly >
                     <a id="anchorTipoContrato" name="anchorTipoContrato" href=""><span class="fa fa-chevron-circle-down" aria-hidden="true" id="botonTipoContrato" name="botonTipoContrato" style="cursor:hand;" alt="<%=meLanbide35I18n.getMensaje(idiomaUsuario, "altDesplegable")%>" title="<%=meLanbide35I18n.getMensaje(idiomaUsuario, "altDesplegable")%>"></span></a>
                </div>
            </div>
            <div class="lineaFormulario">
                <div style="width: 94px; float: left;">
                    <%=meLanbide35I18n.getMensaje(idiomaUsuario,"label.solicitud.preparadores.fechaInicio")%><%if(preparadorModif == null){%><font color="red">*</font><%}%>
                </div>
                <div style="width: 140px; float: left; margin-left: 28px;">
                    <input type="text" class="inputTxtFecha" size="10" maxlength="10" id="fechaInicio" name="fechaInicio" onkeyup="return SoloCaracteresFecha(this);" onblur=" javascript:return comprobarFechaEca(this, '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "msg.fechaNoVal")%>');" onfocus="this.select();"/>
                    <A href="javascript:calClick(event);" onclick="mostrarCalFechaInicio(event);return false;" style="text-decoration:none;" >
                        <IMG style="border: 0" height="17" id="calFechaInicio" name="calFechaInicio" alt="<%=meLanbide35I18n.getMensaje(idiomaUsuario,"label.solicitud.preparadores.fechaInicio")%>" src="<c:url value='/images/calendario/icono.gif'/>">
                    </A>
                </div>
                <div style="width: 94px; float: left;">
                    <%=meLanbide35I18n.getMensaje(idiomaUsuario,"label.solicitud.preparadores.fechaFin")%>
                </div>
                <div style="width: 140px; float: left; margin-left: 12px;">
                    <input type="text" class="inputTxtFecha" size="10" maxlength="10" id="fechaFin" name="fechaFin" onkeyup="return SoloCaracteresFecha(this);" onblur=" javascript:return comprobarFechaEca(this, '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "msg.fechaNoVal")%>');" onfocus="this.select();"/>
                    <A href="javascript:calClick(event);" onclick="mostrarCalFechaFin(event);return false;" style="text-decoration:none;" >
                        <IMG style="border: 0" height="17" id="calFechaFin" name="calFechaFin" alt="<%=meLanbide35I18n.getMensaje(idiomaUsuario,"label.solicitud.preparadores.fechaFin")%>" src="<c:url value='/images/calendario/icono.gif'/>">
                    </A>
                </div>
            </div>
            <div class="lineaFormulario">
                <div style="width: 150px; float: left;">
                    <%=meLanbide35I18n.getMensaje(idiomaUsuario,"label.solicitud.preparadores.horasAnualesJC")%>
                </div>
                <div style="width: 112px; float: left;">
                    <input type="text" id="horasAnualesJC" name="horasAnualesJC" size="11" maxlength="9" class="inputTexto textoNumerico" 
                           onkeyup="calcularCostesSalarialesSSJC();FormatNumber(this.value, 8, 2, this.id)"
                           onblur="calcularCostesSalarialesSSJC();ajustarDecimalesImportes();"/>
                </div>
                <div style="width: 117px; float: left;">
                    <%=meLanbide35I18n.getMensaje(idiomaUsuario,"label.solicitud.preparadores.horasContrato")%>
                </div>
                <div style="width: 108px; float: left;">
                    <input type="text" id="horasContrato" name="horasContrato" size="11" maxlength="9" class="inputTexto textoNumerico" 
                           onkeyup="calcularCostesSalarialesPorJor();FormatNumber(this.value, 8, 2, this.id);"
                           onblur="calcularCostesSalarialesPorJor();ajustarDecimalesImportes();"/>
                </div>
                <div style="width: 157px; float: left;">
                    <%=meLanbide35I18n.getMensaje(idiomaUsuario,"label.solicitud.preparadores.horasDedicacionEca")%>
                </div>
                <div style="width: 108px; float: left;">
                    <input type="text" id="horasDedicacionEca" name="horasDedicacionEca" size="11" maxlength="9" class="inputTexto textoNumerico" 
                           onkeyup="calcularCostesSalarialesSSJC();calcularCostesSalarialesPorJor();FormatNumber(this.value, 8, 2, this.id);"
                           onblur="calcularCostesSalarialesSSJC();calcularCostesSalarialesPorJor();ajustarDecimalesImportes();"/>
                </div>
            </div>
            <div class="lineaFormulario">
                <div style="width: 150px; float: left;">
                    <%=meLanbide35I18n.getMensaje(idiomaUsuario,"label.solicitud.preparadores.costesSalarialesSSJor")%>
                </div>
                <div style="width: 112px; float: left;">
                    <input type="text" id="costesSalarialesSSJor" name="costesSalarialesSSJor" size="11" maxlength="9" class="inputTexto textoNumerico readOnly" disabled="disabled"/>
                </div>
                <div style="width: 117px; float: left;">
                    <%=meLanbide35I18n.getMensaje(idiomaUsuario,"label.solicitud.preparadores.costesSalarialesSSPorJor")%>
                </div>
                <div style="width: 108px; float: left;">
                    <input type="text" id="costesSalarialesSSPorJor" name="costesSalarialesSSPorJor" size="11" maxlength="9" class="inputTexto textoNumerico readOnly " disabled="disabled"/>
                </div>
                <div style="width: 157px; float: left;">
                    <%=meLanbide35I18n.getMensaje(idiomaUsuario,"label.solicitud.preparadores.costesSalarialesSSEca")%>
                </div>
                <div style="width: 108px; float: left;">
                    <input type="text" id="costesSalarialesSSEca" name="costesSalarialesSSEca" size="11" maxlength="9" class="inputTexto textoNumerico" onkeyup="calcularCostesSalarialesSSJC();calcularCostesSalarialesPorJor();FormatNumber(this.value, 8, 2, this.id);" onblur="calcularCostesSalarialesSSJC();calcularCostesSalarialesPorJor();ajustarDecimalesImportes();"/>
                </div>
            </div>            
            </fieldset>
            <div class="lineaFormulario" id="divSustituto">
                <div style="width: 120px; float: left;">
                    <%=meLanbide35I18n.getMensaje(idiomaUsuario,"label.justificacion.preparadores.preparadorSustituto")%>
                </div>
                <div style="width: 85px; float: left;">
                    <input type="text" id="nifSustituto" name="nifSustituto" size="10" maxlength="10" class="inputTexto readOnly" disabled="disabled"/>
                </div>                
                <div style="width: 540px; float: left;">
                    <input type="text" id="nomApelSustituto" name="nomApelSustituto" size="83" maxlength="200" class="inputTexto readOnly" disabled="disabled"/>
                </div>
               
            </div>

            <div class="botonera" style="padding-top: 20px;">
                <input type="button" id="btnAceptar" name="btnAceptar" class="botonGeneral" value="<%=meLanbide35I18n.getMensaje(idiomaUsuario, "btn.general.aceptar")%>" onclick="guardar();">
                <input type="button" id="btnCancelar" name="btnCancelar" class="botonGeneral" value="<%=meLanbide35I18n.getMensaje(idiomaUsuario, "btn.general.cancelar")%>" onclick="cancelar();">
                <input type="button" id="btnCerrar" name="btnCerrar" class="botonGeneral" value="<%=meLanbide35I18n.getMensaje(idiomaUsuario, "btn.general.cerrar")%>" onclick="cerrarVentana();" style="display: none;">
            </div>
        </div>
    </form>
    <div id="popupcalendar" class="text"></div>
</body>

<script type="text/javascript">
    var comboTipoContrato;
</script>

<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>

<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide35.i18n.MeLanbide35I18n" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide35.vo.justificacion.EcaJusProspectoresVO" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide35.vo.justificacion.FilaProspectorJustificacionVO" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide35.util.ConstantesMeLanbide35" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide35.util.MeLanbide35Utils" %>
<%@page import="java.text.SimpleDateFormat" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="java.util.List" %>

<%
    int idiomaUsuario = 1;
    UsuarioValueObject usuario = new UsuarioValueObject();
    try
    {
        if (session != null) 
        {
            if (usuario != null) 
            {
                usuario = (UsuarioValueObject) session.getAttribute("usuario");
                idiomaUsuario = usuario.getIdioma();
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
            
    Integer anoExpediente = MeLanbide35Utils.getEjercicioDeExpediente(numExpediente);
            
    EcaJusProspectoresVO prospectorModif = (EcaJusProspectoresVO)request.getAttribute("prospectorModif");
    session.removeAttribute("prospectorModif");
    EcaJusProspectoresVO prospectorOrigen = (EcaJusProspectoresVO)request.getAttribute("prospectorOrigen");
    session.removeAttribute("prospectorOrigen");
    EcaJusProspectoresVO sustitutopros = (EcaJusProspectoresVO)request.getAttribute("sustitutopros");
    session.removeAttribute("sustitutopros");
    
    Boolean consulta = request.getAttribute("consulta") != null ? (Boolean)request.getAttribute("consulta") : false;
    
    
    String tituloPagina = "";
    if(consulta)
    {
        tituloPagina = meLanbide35I18n.getMensaje(idiomaUsuario,"label.justificacion.prospectores.consultaProspector.tituloPagina");
    }
    else
    {
        if(prospectorModif != null)
        {
            tituloPagina = meLanbide35I18n.getMensaje(idiomaUsuario,"label.justificacion.prospectores.modifProspector.tituloPagina");
        }
        else
        {
            tituloPagina = meLanbide35I18n.getMensaje(idiomaUsuario,"label.justificacion.prospectores.nuevoProspector.tituloPagina");
        }
    }
%>
<title><%=tituloPagina%></title>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/estilo.css"/>
<link rel="stylesheet" type="text/css" href="<c:url value='/css/extension/melanbide35/melanbide35.css'/>">

<script type="text/javascript">
    var APP_CONTEXT_PATH = '<%=request.getContextPath()%>';
</script>
<script type="text/javascript" src="<c:url value='/scripts/calendario.js'/>"></script>
<script type="text/javascript" src="<c:url value='/scripts/general.js'/>"></script>
<script type="text/javascript" src="<c:url value='/scripts/popup.js'/>"></script>
<script type="text/javascript" src="<c:url value='/scripts/validaciones.js'/>"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/melanbide35/ecaUtils.js"></script>

<script type="text/javascript">
    
    var mensajeValidacion = '';
    var nuevo = true;
    
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
            
            if(prospectorModif != null)
            {
            %>
                nuevo = false;
                document.getElementById('nif').value = '<%=prospectorModif.getNif() != null ? prospectorModif.getNif().toUpperCase() : "" %>';
                document.getElementById('nomApel').value = '<%=prospectorModif.getNombre() != null ? prospectorModif.getNombre().toUpperCase() : "" %>';
                document.getElementById('fechaInicio').value = '<%=prospectorModif.getFecIni() != null ? format.format(prospectorModif.getFecIni()) : "" %>';
                document.getElementById('fechaFin').value = '<%=prospectorModif.getFecFin() != null ? format.format(prospectorModif.getFecFin()) : "" %>';
                document.getElementById('horasAnualesJC').value = '<%=prospectorModif.getHorasJC() != null ? prospectorModif.getHorasJC().toPlainString() : "" %>';
                document.getElementById('horasContrato').value = '<%=prospectorModif.getHorasCont() != null ? prospectorModif.getHorasCont().toPlainString() : "" %>';
                document.getElementById('horasDedicacionEca').value = '<%=prospectorModif.getHorasEca() != null ? prospectorModif.getHorasEca().toPlainString() : "" %>';
                document.getElementById('costesSalarialesSSJor').value = '<%=prospectorModif.getImpSSJC() != null ? prospectorModif.getImpSSJC().toPlainString() : "" %>';
                document.getElementById('costesSalarialesSSPorJor').value = '<%=prospectorModif.getImpSSJR() != null ? prospectorModif.getImpSSJR().toPlainString() : "" %>';
                document.getElementById('costesSalarialesSSEca').value = '<%=prospectorModif.getImpSSECA() != null ? prospectorModif.getImpSSECA().toPlainString() : "" %>';                     
                document.getElementById('nifOrigen').value = '<%=prospectorOrigen != null ? prospectorOrigen.getNif().toUpperCase() : "" %>';
                document.getElementById('nomApelOrigen').value = '<%=prospectorOrigen != null ? prospectorOrigen.getNombre().toUpperCase() : "" %>';        
                document.getElementById('nifSustituto').value = '<%=sustitutopros != null ? sustitutopros.getNif().toUpperCase() : "" %>';
                document.getElementById('nomApelSustituto').value = '<%=sustitutopros != null ? sustitutopros.getNombre().toUpperCase() : "" %>';        
            
            <%
            }else { %>
                document.getElementById('nifOrigen').value = '<%=prospectorOrigen != null ? prospectorOrigen.getNif().toUpperCase() : "" %>';
                document.getElementById('nomApelOrigen').value = '<%=prospectorOrigen != null ? prospectorOrigen.getNombre().toUpperCase() : "" %>';        
        //prospectorOrigen 
         <%  }
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

                    document.getElementById('btnAceptar').style.display = 'none';
                    document.getElementById('btnCancelar').style.display = 'none';
                    document.getElementById('btnCerrar').style.display = 'inline';
                    document.getElementById('btnFechaFin').style.display = 'none';
                    document.getElementById('btnFechaInicio').style.display = 'none';
            <%
                }
            %>    
              
                FormatNumber(document.getElementById('horasAnualesJC').value, 8, 2, document.getElementById('horasAnualesJC').id);
                FormatNumber(document.getElementById('horasContrato').value, 8, 2, document.getElementById('horasContrato').id);
                FormatNumber(document.getElementById('horasDedicacionEca').value, 8, 2, document.getElementById('horasDedicacionEca').id);
                FormatNumber(document.getElementById('costesSalarialesSSJor').value, 8, 2, document.getElementById('costesSalarialesSSJor').id);
                FormatNumber(document.getElementById('costesSalarialesSSPorJor').value, 8, 2, document.getElementById('costesSalarialesSSPorJor').id);
                FormatNumber(document.getElementById('costesSalarialesSSEca').value, 8, 2, document.getElementById('costesSalarialesSSEca').id);
            
            ajustarDecimalesImportes();
        }catch(err){
            
        }
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
    
    function inicializarErroresCamposProsJus(){
        camposErrores = new Array();
        for(var i = 0; i < <%=FilaProspectorJustificacionVO.NUM_CAMPOS_FILA%>; i++){
            camposErrores[i] = '<%=ConstantesMeLanbide35.FALSO%>';
        }
    }
    
    function guardar(){
        if(validarDatos()){
            
            document.getElementById('msgGuardandoDatos').style.display="inline";
            barraProgresoEca('on', 'barraProgresoNuevoProspectorSolicitud');
            var ajax = getXMLHttpRequest();
            var nodos = null;
            var CONTEXT_PATH = '<%=request.getContextPath()%>'
            var url = CONTEXT_PATH + "/PeticionModuloIntegracion.do";
            var parametros = "";
            var control = new Date();
            parametros = 'tarea=preparar&modulo=MELANBIDE35&operacion=guardarProspectorJustificacion&tipo=0&numero=<%=numExpediente%>'
                +'&idPros=<%=prospectorModif != null && prospectorModif.getJusProspectoresCod() != null ? prospectorModif.getJusProspectoresCod() : ""%>'
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
                +'&idProsOrigen=<%=prospectorOrigen != null && prospectorOrigen.getJusProspectoresCod() != null ? prospectorOrigen.getJusProspectoresCod() : ""%>'
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
            var listaProspectores = new Array();
            var fila = new Array();
            var nodoFila;
            var hijosFila;
            var j;
            
            inicializarErroresCamposProsJus();
            
            for(j=0;hijos!=null && j<hijos.length;j++){
                if(hijos[j].nodeName=="CODIGO_OPERACION"){                            
                    codigoOperacion = hijos[j].childNodes[0].nodeValue;
                    listaProspectores[j] = codigoOperacion;
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
                                 fila[<%=FilaProspectorJustificacionVO.POS_CAMPO_NIF%>] = nodoCampo.childNodes[0].childNodes[0].nodeValue;
                                camposErrores[<%=FilaProspectorJustificacionVO.POS_CAMPO_NIF%>] = nodoCampo.childNodes[1].childNodes[0].nodeValue;
                            }
                            else{
                                fila[<%=FilaProspectorJustificacionVO.POS_CAMPO_NIF%>] = '-';
                            }
                        }
                        else if(hijosFila[cont].nodeName=="NOMBRE"){
                            nodoCampo = hijosFila[cont];
                            if(nodoCampo.childNodes.length > 0){
                                 fila[<%=FilaProspectorJustificacionVO.POS_CAMPO_NOMBREAPEL%>] = nodoCampo.childNodes[0].childNodes[0].nodeValue;
                                camposErrores[<%=FilaProspectorJustificacionVO.POS_CAMPO_NOMBREAPEL%>] = nodoCampo.childNodes[1].childNodes[0].nodeValue;
                            }
                            else{
                                fila[<%=FilaProspectorJustificacionVO.POS_CAMPO_NOMBREAPEL%>] = '-';
                            }    
                        }
                        else if(hijosFila[cont].nodeName=="FECHA_INICIO"){
                            nodoCampo = hijosFila[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[<%=FilaProspectorJustificacionVO.POS_CAMPO_FECHA_INICIO%>] = nodoCampo.childNodes[0].childNodes[0].nodeValue;
                                camposErrores[<%=FilaProspectorJustificacionVO.POS_CAMPO_FECHA_INICIO%>] = nodoCampo.childNodes[1].childNodes[0].nodeValue;
                            }
                            else{
                                fila[<%=FilaProspectorJustificacionVO.POS_CAMPO_FECHA_INICIO%>] = '-';
                            }
                        }
                        else if(hijosFila[cont].nodeName=="FECHA_FIN"){
                            nodoCampo = hijosFila[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[<%=FilaProspectorJustificacionVO.POS_CAMPO_FECHA_FIN%>] = nodoCampo.childNodes[0].childNodes[0].nodeValue;
                                camposErrores[<%=FilaProspectorJustificacionVO.POS_CAMPO_FECHA_FIN%>] = nodoCampo.childNodes[1].childNodes[0].nodeValue;
                            }
                            else{
                                fila[<%=FilaProspectorJustificacionVO.POS_CAMPO_FECHA_FIN%>] = '-';
                            }
                        }
                        else if(hijosFila[cont].nodeName=="HORAS_ANUALES"){
                            nodoCampo = hijosFila[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[<%=FilaProspectorJustificacionVO.POS_CAMPO_HORAS_ANUALES%>] = nodoCampo.childNodes[0].childNodes[0].nodeValue;
                                camposErrores[<%=FilaProspectorJustificacionVO.POS_CAMPO_HORAS_ANUALES%>] = nodoCampo.childNodes[1].childNodes[0].nodeValue;
                            }
                            else{
                                fila[<%=FilaProspectorJustificacionVO.POS_CAMPO_HORAS_ANUALES%>] = '-';
                            }
                        }
                        else if(hijosFila[cont].nodeName=="HORAS_CONTRATO"){
                            nodoCampo = hijosFila[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[<%=FilaProspectorJustificacionVO.POS_CAMPO_HORAS_CONTRATO%>] = nodoCampo.childNodes[0].childNodes[0].nodeValue;
                                camposErrores[<%=FilaProspectorJustificacionVO.POS_CAMPO_HORAS_CONTRATO%>] = nodoCampo.childNodes[1].childNodes[0].nodeValue;
                            }
                            else{
                                fila[<%=FilaProspectorJustificacionVO.POS_CAMPO_HORAS_CONTRATO%>] = '-';
                            }
                        }
                        else if(hijosFila[cont].nodeName=="HORAS_DEDICACION_ECA"){
                            nodoCampo = hijosFila[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[<%=FilaProspectorJustificacionVO.POS_CAMPO_HORAS_DEDICACION_ECA%>] = nodoCampo.childNodes[0].childNodes[0].nodeValue;
                                camposErrores[<%=FilaProspectorJustificacionVO.POS_CAMPO_HORAS_DEDICACION_ECA%>] = nodoCampo.childNodes[1].childNodes[0].nodeValue;
                            }
                            else{
                                fila[<%=FilaProspectorJustificacionVO.POS_CAMPO_HORAS_DEDICACION_ECA%>] = '-';
                            }
                        }
                        else if(hijosFila[cont].nodeName=="COSTES_SALARIALES_SS_JOR"){
                            nodoCampo = hijosFila[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[<%=FilaProspectorJustificacionVO.POS_CAMPO_COSTES_SALARIALES_SS_JOR%>] = nodoCampo.childNodes[0].childNodes[0].nodeValue;
                                camposErrores[<%=FilaProspectorJustificacionVO.POS_CAMPO_COSTES_SALARIALES_SS_JOR%>] = nodoCampo.childNodes[1].childNodes[0].nodeValue;
                            }
                            else{
                                fila[<%=FilaProspectorJustificacionVO.POS_CAMPO_COSTES_SALARIALES_SS_JOR%>] = '-';
                            }
                        }
                        else if(hijosFila[cont].nodeName=="COSTES_SALARIALES_SS_POR_JOR"){
                            nodoCampo = hijosFila[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[<%=FilaProspectorJustificacionVO.POS_CAMPO_COSTES_SALARIALES_SS_POR_JOR%>] = nodoCampo.childNodes[0].childNodes[0].nodeValue;
                                camposErrores[<%=FilaProspectorJustificacionVO.POS_CAMPO_COSTES_SALARIALES_SS_POR_JOR%>] = nodoCampo.childNodes[1].childNodes[0].nodeValue;
                            }
                            else{
                                fila[<%=FilaProspectorJustificacionVO.POS_CAMPO_COSTES_SALARIALES_SS_POR_JOR%>] = '-';
                            }
                        }
                        else if(hijosFila[cont].nodeName=="COSTES_SALARIALES_SS_ECA"){
                            nodoCampo = hijosFila[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[<%=FilaProspectorJustificacionVO.POS_CAMPO_COSTES_SALARIALES_SS_ECA%>] = nodoCampo.childNodes[0].childNodes[0].nodeValue;
                                camposErrores[<%=FilaProspectorJustificacionVO.POS_CAMPO_COSTES_SALARIALES_SS_ECA%>] = nodoCampo.childNodes[1].childNodes[0].nodeValue;
                            }
                            else{
                                fila[<%=FilaProspectorJustificacionVO.POS_CAMPO_COSTES_SALARIALES_SS_ECA%>] = '-';
                            }
                        }
                        else if(hijosFila[cont].nodeName=="VISITAS"){
                            nodoCampo = hijosFila[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[<%=FilaProspectorJustificacionVO.POS_CAMPO_VISITAS%>] = nodoCampo.childNodes[0].childNodes[0].nodeValue;
                                camposErrores[<%=FilaProspectorJustificacionVO.POS_CAMPO_VISITAS%>] = nodoCampo.childNodes[1].childNodes[0].nodeValue;
                            }
                            else{
                                fila[<%=FilaProspectorJustificacionVO.POS_CAMPO_VISITAS%>] = '-';
                            }
                        }
                        else if(hijosFila[cont].nodeName=="VISITAS_IMP"){
                            nodoCampo = hijosFila[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[<%=FilaProspectorJustificacionVO.POS_CAMPO_VISITAS_IMP%>] = nodoCampo.childNodes[0].childNodes[0].nodeValue;
                                camposErrores[<%=FilaProspectorJustificacionVO.POS_CAMPO_VISITAS_IMP%>] = nodoCampo.childNodes[1].childNodes[0].nodeValue;
                            }
                            else{
                                fila[<%=FilaProspectorJustificacionVO.POS_CAMPO_VISITAS_IMP%>] = '-';
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
                            fila[13] = listaErrores;
                        }else if(hijosFila[cont].nodeName=="ES_SUSTITUTO"){
                            nodoCampo = hijosFila[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[15] = nodoCampo.childNodes[0].nodeValue;
                            }
                            else{
                                fila[15] = '0';
                            }
                        }
                    }
                    fila[14] = camposErrores;
                    listaProspectores[j] = fila;
                    fila = new Array();
                    camposErrores = new Array();
                }   
            }//for(j=0;hijos!=null && j<hijos.length;j++)
                if(codigoOperacion=="0"){
                    window.returnValue =  listaProspectores;
                    barraProgresoEca('off', 'barraProgresoNuevoProspectorSolicitud');
                    jsp_alerta("A",'<%=meLanbide35I18n.getMensaje(idiomaUsuario,"msg.datosGuardadosOK")%>');
                    cerrarVentana();
                }else if(codigoOperacion=="1"){
                    jsp_alerta("A",'<%=meLanbide35I18n.getMensaje(idiomaUsuario,"error.errorBD")%>');
                }else if(codigoOperacion=="2"){
                    jsp_alerta("A",'<%=meLanbide35I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                }else if(codigoOperacion=="3"){
                    jsp_alerta("A",'<%=meLanbide35I18n.getMensaje(idiomaUsuario,"error.pasoParametros")%>');
                }else if(codigoOperacion=="4"){
                    jsp_alerta("A",'<%=meLanbide35I18n.getMensaje(idiomaUsuario,"error.datosNoValidos")%>');
                }else if(codigoOperacion=="5"){
                    jsp_alerta("A",'<%=meLanbide35I18n.getMensaje(idiomaUsuario,"error.nifProspectorRepetido")%>');
                }else{
                    jsp_alerta("A",'<%=meLanbide35I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                }//if(
            }
            catch(Err){
                jsp_alerta("A",'<%=meLanbide35I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
            }//try-catch
            
            barraProgresoEca('off', 'barraProgresoNuevoProspectorSolicitud');
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
        if(nuevo){
            var fecIni = document.getElementById('fechaInicio').value;
            if(fecIni == null || fecIni == ''){
                mensajeValidacion = '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "msg.solicitud.preparadores.fechaIniVacio")%>';
                return false;
            }
            /*var fecFin = document.getElementById('fechaFin').value;
            if(fecFin == null || fecFin == ''){
                mensajeValidacion = '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "msg.solicitud.preparadores.fechaFinVacio")%>';
                return false;
            }*/
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
        if(correcto && nuevo){
            var feIni = document.getElementById('fechaInicio').value;
            var feFin = document.getElementById('fechaFin').value;
            
            var array_fecha_ini;
            var diaIni;
            var mesIni;
            var anoIni;


            var array_fecha_fin;
            var diaFin;
            var mesFin;
            var anoFin;
            
            var hayFeIni = false;
            var hayFeFin = false;
            
            if(feIni != undefined && feIni != ''){
                hayFeIni = true;
                array_fecha_ini = feIni.split('/');
                diaIni = array_fecha_ini[0];
                mesIni = array_fecha_ini[1];
                anoIni = array_fecha_ini[2];

                if(anoIni != '<%=anoExpediente%>'){
                    correcto = false;
                    document.getElementById('fechaInicio').style.border = '1px solid red';
                    if(mensajeValidacion == '')
                        mensajeValidacion = '<%=String.format(meLanbide35I18n.getMensaje(idiomaUsuario, "validaciones.solicitud.preparadores.fechasAnoExpediente"), anoExpediente)%>';
                }else{
                    document.getElementById('fechaInicio').removeAttribute('style');
                }
            }
            
            if(feFin != undefined && feFin != ''){
                hayFeFin = true;
                array_fecha_fin = feFin.split('/');
                diaFin = array_fecha_fin[0];
                mesFin = array_fecha_fin[1];
                anoFin = array_fecha_fin[2];

                if(anoFin != '<%=anoExpediente%>'){
                    correcto = false;
                    document.getElementById('fechaFin').style.border = '1px solid red';
                    if(mensajeValidacion == '')
                        mensajeValidacion = '<%=String.format(meLanbide35I18n.getMensaje(idiomaUsuario, "validaciones.solicitud.preparadores.fechasAnoExpediente"), anoExpediente)%>';
                }else{
                    document.getElementById('fechaFin').removeAttribute('style');
                }
            }

            if(hayFeIni && hayFeFin && correcto){
                var dIni = new Date(anoIni, mesIni-1, diaIni, 0, 0, 0, 0);
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
            }
            
            
                
                //COMPROBACION FECHAS ORIGEN Y SUSTITUTO
                <% if (prospectorOrigen != null){ %>
                    /*var finiOrigen = '<%= prospectorOrigen.getFecIni() %>';
                    var ffinOrigen = '<%= prospectorOrigen.getFecFin() %>';
                    
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
                    mensajeValidacion = '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "msg.justificacion.prospectores.horasDedicacionEca50HorasContrato")%>';
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
        
        /*var f;
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
                        
                        result = result.toFixed(2);

                        if (isFinite(result)){
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
        <div id="barraProgresoNuevoProspectorSolicitud" style="position: absolute; z-index:10; visibility: hidden; top: 30%; left: 30%;">
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
        <div style="width: 100%; padding: 10px; text-align: left;">
            <!--<div class="tituloAzul" style="clear: both; text-align: left;">-->
            <div class="sub3titulo" style="clear: both; text-align: left;height:18px;">
                <span>
                    <%=meLanbide35I18n.getMensaje(idiomaUsuario,"label.justificacion.prospectores.datosProspector")%>
                </span>
            </div>
            <div class="lineaFormulario" >
                <div style="width: 120px; float: left;">
                    <%=meLanbide35I18n.getMensaje(idiomaUsuario,"label.justificacion.prospectores.prospectorOrigen")%>
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
                <legend>Prospector</legend>
            <div class="lineaFormulario">
                <div style="width: 36px; float: left;">
                    <%=meLanbide35I18n.getMensaje(idiomaUsuario,"label.justificacion.prospectores.nif")%><font color="red">*</font>
                </div>
                <div style="width: 101px; float: left;">
                    <input type="text" id="nif" name="nif" size="10" maxlength="10" class="inputTexto"/>
                </div>
                <div style="width: 120px; float: left;">
                    <%=meLanbide35I18n.getMensaje(idiomaUsuario,"label.justificacion.prospectores.nomApel")%><font color="red">*</font>
                </div>
                <div style="width: 530px; float: left;">
                    <input type="text" id="nomApel" name="nomApel" size="83" maxlength="200" class="inputTexto"/>
                </div>
            </div>
            <div class="lineaFormulario">
                <div style="width: 94px; float: left;">
                    <%=meLanbide35I18n.getMensaje(idiomaUsuario,"label.justificacion.prospectores.fechaInicio")%><font color="red">*</font>
                </div>
                <div style="width: 140px; float: left;">
                    <input type="text" class="inputTxtFecha" size="10" maxlength="10" id="fechaInicio" name="fechaInicio" onkeyup="return SoloCaracteresFecha(this);" onblur=" javascript:return comprobarFechaEca(this, '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "msg.fechaNoVal")%>');" onfocus="this.select();"/>
                    <A href="javascript:calClick(event);" onclick="mostrarCalFechaInicio(event);return false;" style="text-decoration:none;" >
                        <IMG style="border: 0" height="17" id="calFechaInicio" name="calFechaInicio" alt="<%=meLanbide35I18n.getMensaje(idiomaUsuario,"label.solicitud.preparadores.fechaInicio")%>" src="<c:url value='/images/calendario/icono.gif'/>">
                    </A>
                </div>
                <div style="width: 94px; float: left;">
                    <%=meLanbide35I18n.getMensaje(idiomaUsuario,"label.justificacion.prospectores.fechaFin")%>
                </div>
                <div style="width: 140px; float: left;">
                    <input type="text" class="inputTxtFecha" size="10" maxlength="10" id="fechaFin" name="fechaFin" onkeyup="return SoloCaracteresFecha(this);" onblur=" javascript:return comprobarFechaEca(this, '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "msg.fechaNoVal")%>');" onfocus="this.select();"/>
                    <A href="javascript:calClick(event);" onclick="mostrarCalFechaFin(event);return false;" style="text-decoration:none;" >
                        <IMG style="border: 0" height="17" id="calFechaFin" name="calFechaFin" alt="<%=meLanbide35I18n.getMensaje(idiomaUsuario,"label.solicitud.preparadores.fechaFin")%>" src="<c:url value='/images/calendario/icono.gif'/>">
                    </A>
                </div>
            </div>
            <div class="lineaFormulario">
                <div style="width: 150px; float: left;">
                    <%=meLanbide35I18n.getMensaje(idiomaUsuario,"label.justificacion.prospectores.horasAnualesJC")%>
                </div>
                <div style="width: 112px; float: left;">
                    <input type="text" id="horasAnualesJC" name="horasAnualesJC" size="11" maxlength="9" class="inputTexto textoNumerico" 
                           onkeyup="calcularCostesSalarialesSSJC();FormatNumber(this.value, 8, 2, this.id);"
                           onblur="calcularCostesSalarialesSSJC();ajustarDecimalesImportes();"/>
                </div>
                <div style="width: 117px; float: left;">
                    <%=meLanbide35I18n.getMensaje(idiomaUsuario,"label.justificacion.prospectores.horasContrato")%>
                </div>
                <div style="width: 108px; float: left;">
                    <input type="text" id="horasContrato" name="horasContrato" size="11" maxlength="9" class="inputTexto textoNumerico" 
                           onkeyup="calcularCostesSalarialesPorJor();FormatNumber(this.value, 8, 2, this.id);"
                           onblur="calcularCostesSalarialesPorJor();ajustarDecimalesImportes();"/>
                </div>
                <div style="width: 157px; float: left;">
                    <%=meLanbide35I18n.getMensaje(idiomaUsuario,"label.justificacion.prospectores.horasDedicacionEca")%>
                </div>
                <div style="width: 108px; float: left;">
                    <input type="text" id="horasDedicacionEca" name="horasDedicacionEca" size="11" maxlength="9" class="inputTexto textoNumerico" 
                           onkeyup="calcularCostesSalarialesSSJC();calcularCostesSalarialesPorJor();FormatNumber(this.value, 8, 2, this.id);"
                           onblur="calcularCostesSalarialesSSJC();calcularCostesSalarialesPorJor();ajustarDecimalesImportes();"/>
                </div>
            </div>
            <div class="lineaFormulario">
                <div style="width: 150px; float: left;">
                    <%=meLanbide35I18n.getMensaje(idiomaUsuario,"label.justificacion.prospectores.costesSalarialesSSJor")%>
                </div>
                <div style="width: 112px; float: left;">
                    <input type="text" id="costesSalarialesSSJor" name="costesSalarialesSSJor" size="11" maxlength="9" class="inputTexto textoNumerico readOnly" disabled="disabled"/>
                </div>
                <div style="width: 117px; float: left;">
                    <%=meLanbide35I18n.getMensaje(idiomaUsuario,"label.justificacion.prospectores.costesSalarialesSSPorJor")%>
                </div>
                <div style="width: 108px; float: left;">
                    <input type="text" id="costesSalarialesSSPorJor" name="costesSalarialesSSPorJor" size="11" maxlength="9" class="inputTexto textoNumerico readOnly" disabled="disabled"/>
                </div>
                <div style="width: 157px; float: left;">
                    <%=meLanbide35I18n.getMensaje(idiomaUsuario,"label.justificacion.prospectores.costesSalarialesSSEca")%>
                </div>
                <div style="width: 108px; float: left;">
                    <input type="text" id="costesSalarialesSSEca" name="costesSalarialesSSEca" size="11" maxlength="9" class="inputTexto textoNumerico"
                           onkeyup="calcularCostesSalarialesSSJC();calcularCostesSalarialesPorJor();FormatNumber(this.value, 8, 2, this.id);" 
                           onblur="calcularCostesSalarialesSSJC();calcularCostesSalarialesPorJor();ajustarDecimalesImportes();"/>
                </div>
            </div>            
            </fieldset>
            <div class="lineaFormulario" >
                <div style="width: 120px; float: left;">
                    <%=meLanbide35I18n.getMensaje(idiomaUsuario,"label.justificacion.prospectores.prospectorSustituto")%>
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
    
    
</script>
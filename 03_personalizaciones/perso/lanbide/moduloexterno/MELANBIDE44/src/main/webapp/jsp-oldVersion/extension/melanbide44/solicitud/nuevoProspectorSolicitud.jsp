<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>

<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide44.i18n.MeLanbide44I18n" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide44.vo.solicitud.EcaSolProspectoresVO" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide44.vo.comun.EcaConfiguracionVO" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide44.util.ConstantesMeLanbide44" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide44.vo.solicitud.FilaProspectorSolicitudVO" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide44.util.MeLanbide44Utils" %>
<%@page import="java.text.SimpleDateFormat" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>

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
    
    SimpleDateFormat format = new SimpleDateFormat(ConstantesMeLanbide44.FORMATO_FECHA);
    
    //Clase para internacionalizar los mensajes de la aplicación.
    MeLanbide44I18n meLanbide44I18n = MeLanbide44I18n.getInstance();
    String numExpediente    = request.getParameter("numero");
    EcaSolProspectoresVO prospectorModif = (EcaSolProspectoresVO)request.getAttribute("prospectorModif");
    session.removeAttribute("prospectorModif");
    EcaSolProspectoresVO prospectorOrigen = (EcaSolProspectoresVO)request.getAttribute("prospectorOrigen");
    session.removeAttribute("prospectorOrigen");
    EcaSolProspectoresVO sustitutopros = (EcaSolProspectoresVO)request.getAttribute("sustitutopros");
    session.removeAttribute("sustitutopros");
    Boolean consulta = request.getAttribute("consulta") != null ? (Boolean)request.getAttribute("consulta") : false;
    EcaConfiguracionVO ecaConfig = (EcaConfiguracionVO)session.getAttribute("ecaConfiguracion");
    
    
    
    String tituloPagina = "";
    if(consulta)
    {
        tituloPagina = meLanbide44I18n.getMensaje(idiomaUsuario,"label.solicitud.prospectores.consultaProspector.tituloPagina");
    }
    else
    {
        if(prospectorModif != null)
        {
            tituloPagina = meLanbide44I18n.getMensaje(idiomaUsuario,"label.solicitud.prospectores.modifProspector.tituloPagina");
        }
        else
        {
            tituloPagina = meLanbide44I18n.getMensaje(idiomaUsuario,"label.solicitud.prospectores.nuevoProspector.tituloPagina");
        }
    }
    
    Integer anoExpediente = MeLanbide44Utils.getEjercicioDeExpediente(numExpediente);
%>
<title><%=tituloPagina%></title>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/estilo.css"/>
<link rel="stylesheet" type="text/css" href="<c:url value='/css/extension/melanbide44/melanbide44.css'/>">

<script type="text/javascript">
    var APP_CONTEXT_PATH = '<%=request.getContextPath()%>';
</script>
<script type="text/javascript" src="<c:url value='/scripts/calendario.js'/>"></script>
<script type="text/javascript" src="<c:url value='/scripts/general.js'/>"></script>
<script type="text/javascript" src="<c:url value='/scripts/popup.js'/>"></script>
<script type="text/javascript" src="<c:url value='/scripts/validaciones.js'/>"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/melanbide44/ecaUtils.js"></script>

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
                document.getElementById('visitas').value = '<%=prospectorModif.getVisitas() != null ? prospectorModif.getVisitas().toString() : "" %>';
                document.getElementById('visitasImp').value = '<%=prospectorModif.getVisitasImp() != null ? prospectorModif.getVisitasImp().toPlainString() : "" %>';
                document.getElementById('coste').value = '<%=prospectorModif.getCoste() != null ? prospectorModif.getCoste().toPlainString() : "" %>';
                document.getElementById('importeConcedido').value = '<%=prospectorModif.getImpteConcedido() != null ? prospectorModif.getImpteConcedido().toPlainString() : "" %>';
                document.getElementById('nifOrigen').value = '<%=prospectorOrigen != null ? prospectorOrigen.getNif().toUpperCase() : "" %>';
                document.getElementById('nomApelOrigen').value = '<%=prospectorOrigen != null ? prospectorOrigen.getNombre().toUpperCase() : "" %>';        
                document.getElementById('nifSustituto').value = '<%=sustitutopros != null ? sustitutopros.getNif().toUpperCase() : "" %>';
                document.getElementById('nomApelSustituto').value = '<%=sustitutopros != null ? sustitutopros.getNombre().toUpperCase() : "" %>';
            <%
            }
            if(prospectorOrigen != null)
            {
            %>
                document.getElementById('nifOrigen').value = '<%=prospectorOrigen.getNif() != null ? prospectorOrigen.getNif().toUpperCase() : "" %>';
                document.getElementById('nomApelOrigen').value = '<%=prospectorOrigen.getNombre() != null ? prospectorOrigen.getNombre().toUpperCase() : "" %>';            
            <%
            }
            else
            {
            %>
                document.getElementById('nifOrigen').value = '';
                document.getElementById('nomApelOrigen').value = '';
                document.getElementById('divProsOrigen').style.display = 'none';
            <%
            }
    
            if(sustitutopros != null)
            {
            %>
                document.getElementById('nifSustituto').value = '<%=sustitutopros.getNif() != null ? sustitutopros.getNif().toUpperCase() : ""%>';
                document.getElementById('nomApelSustituto').value = '<%=sustitutopros.getNombre() != null ? sustitutopros.getNombre().toUpperCase() : ""%>';
            <%
            }
            else
            {
            %>
                document.getElementById('nifSustituto').value = '';
                document.getElementById('nomApelSustituto').value = '';
                document.getElementById('divProsSustituto').style.display = 'none';
            <%
            }
            %>
                   
            reemplazarPuntosEca(document.getElementById('horasAnualesJC'));
            reemplazarPuntosEca(document.getElementById('horasContrato'));
            reemplazarPuntosEca(document.getElementById('horasDedicacionEca'));
            reemplazarPuntosEca(document.getElementById('costesSalarialesSSJor'));
            reemplazarPuntosEca(document.getElementById('costesSalarialesSSPorJor'));
            reemplazarPuntosEca(document.getElementById('costesSalarialesSSEca'));
            reemplazarPuntosEca(document.getElementById('visitasImp'));
            reemplazarPuntosEca(document.getElementById('coste'));
            reemplazarPuntosEca(document.getElementById('importeConcedido'));
            
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
                    document.getElementById('visitas').disabled = true;
                    document.getElementById('visitasImp').disabled = true;
                    document.getElementById('coste').disabled = true;
                    document.getElementById('importeConcedido').disabled = true;
                
                    document.getElementById('btnAceptar').style.display = 'none';
                    document.getElementById('btnCancelar').style.display = 'none';
                    document.getElementById('btnCerrar').style.display = 'inline';
                    document.getElementById('btnFechaFin').style.display = 'none';
                    document.getElementById('btnFechaInicio').style.display = 'none';
            <%
                }
            %>   
            ajustarDecimalesImportes();
            /*actualizarMinimoVisitas();*/
            
            /*if(nuevo){
                document.getElementById('divProsOrigen').style.display = 'none';
                document.getElementById('divProsSustituto').style.display = 'none';
            }*/
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
        var resultado = jsp_alerta("","<%=meLanbide44I18n.getMensaje(idiomaUsuario, "mens.preguntaCancelar")%>");
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
            parametros = 'tarea=preparar&modulo=MELANBIDE44&operacion=guardarProspectorSolicitud&tipo=0&numero=<%=numExpediente%>'
                +'&idPros=<%=prospectorModif != null && prospectorModif.getSolProspectoresCod() != null ? prospectorModif.getSolProspectoresCod() : ""%>'
                +'&nif='+document.getElementById('nif').value
                +'&nomApel='+escape(document.getElementById('nomApel').value)
                +'&feIni='+document.getElementById('fechaInicio').value
                +'&feFin='+document.getElementById('fechaFin').value
                +'&horasAnualesJC='+convertirANumero(document.getElementById('horasAnualesJC').value)
                +'&horasContrato='+convertirANumero(document.getElementById('horasContrato').value)
                +'&horasECA='+convertirANumero(document.getElementById('horasDedicacionEca').value)
                +'&costesSSJor='+convertirANumero(document.getElementById('costesSalarialesSSJor').value)
                +'&costesSSPorJor='+convertirANumero(document.getElementById('costesSalarialesSSPorJor').value)
                +'&costesSSECA='+convertirANumero(document.getElementById('costesSalarialesSSEca').value)
                +'&visitas='+convertirANumero(document.getElementById('visitas').value)
                +'&visitasImp='+convertirANumero(document.getElementById('visitasImp').value)
                +'&coste='+convertirANumero(document.getElementById('coste').value)
                +'&impteConcedido='+convertirANumero(document.getElementById('importeConcedido').value)
                +'&idProsOrigen=<%=prospectorOrigen != null && prospectorOrigen.getSolProspectoresCod() != null ? prospectorOrigen.getSolProspectoresCod() : ""%>'
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
            var msgValidacion = '';
            var listaProspectores = new Array();
            var listaErrores = new Array();
            var fila = new Array();
            var nodoFila;
            var hijosFila;
            var nodoErrores;
            var nodoCampo;
            var errores;
            var camposErrores;
            var j;
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
                                fila[0] = nodoCampo.childNodes[0].childNodes[0].nodeValue;
                            }
                            else{
                                fila[0] = '-';
                            }
                        }
                        else if(hijosFila[cont].nodeName=="NIF"){
                            nodoCampo = hijosFila[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[<%=FilaProspectorSolicitudVO.POS_CAMPO_NIF%>] = nodoCampo.childNodes[0].childNodes[0].nodeValue;
                                camposErrores[<%=FilaProspectorSolicitudVO.POS_CAMPO_NIF%>] = nodoCampo.childNodes[1].childNodes[0].nodeValue;
                            }
                            else{
                                fila[<%=FilaProspectorSolicitudVO.POS_CAMPO_NIF%>] = '-';
                            }
                        }
                        else if(hijosFila[cont].nodeName=="NOMBRE"){
                            nodoCampo = hijosFila[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[<%=FilaProspectorSolicitudVO.POS_CAMPO_NOMBREAPEL%>] = nodoCampo.childNodes[0].childNodes[0].nodeValue;
                                camposErrores[<%=FilaProspectorSolicitudVO.POS_CAMPO_NOMBREAPEL%>] = nodoCampo.childNodes[1].childNodes[0].nodeValue;
                            }
                            else{
                                fila[<%=FilaProspectorSolicitudVO.POS_CAMPO_NOMBREAPEL%>] = '-';
                            }
                        }
                        else if(hijosFila[cont].nodeName=="FECHA_INICIO"){
                            nodoCampo = hijosFila[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[<%=FilaProspectorSolicitudVO.POS_CAMPO_FECHA_INICIO%>] = nodoCampo.childNodes[0].childNodes[0].nodeValue;
                                camposErrores[<%=FilaProspectorSolicitudVO.POS_CAMPO_FECHA_INICIO%>] = nodoCampo.childNodes[1].childNodes[0].nodeValue;
                            }
                            else{
                                fila[<%=FilaProspectorSolicitudVO.POS_CAMPO_FECHA_INICIO%>] = '-';
                            }
                        }
                        else if(hijosFila[cont].nodeName=="FECHA_FIN"){
                            nodoCampo = hijosFila[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[<%=FilaProspectorSolicitudVO.POS_CAMPO_FECHA_FIN%>] = nodoCampo.childNodes[0].childNodes[0].nodeValue;
                                camposErrores[<%=FilaProspectorSolicitudVO.POS_CAMPO_FECHA_FIN%>] = nodoCampo.childNodes[1].childNodes[0].nodeValue;
                            }
                            else{
                                fila[<%=FilaProspectorSolicitudVO.POS_CAMPO_FECHA_FIN%>] = '-';
                            }
                        }
                        else if(hijosFila[cont].nodeName=="HORAS_ANUALES"){
                            nodoCampo = hijosFila[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[<%=FilaProspectorSolicitudVO.POS_CAMPO_HORAS_ANUALES%>] = nodoCampo.childNodes[0].childNodes[0].nodeValue;
                                camposErrores[<%=FilaProspectorSolicitudVO.POS_CAMPO_HORAS_ANUALES%>] = nodoCampo.childNodes[1].childNodes[0].nodeValue;
                            }
                            else{
                                fila[<%=FilaProspectorSolicitudVO.POS_CAMPO_HORAS_ANUALES%>] = '-';
                            }
                        }
                        else if(hijosFila[cont].nodeName=="HORAS_CONTRATO"){
                            nodoCampo = hijosFila[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[<%=FilaProspectorSolicitudVO.POS_CAMPO_HORAS_CONTRATO%>] = nodoCampo.childNodes[0].childNodes[0].nodeValue;
                                camposErrores[<%=FilaProspectorSolicitudVO.POS_CAMPO_HORAS_CONTRATO%>] = nodoCampo.childNodes[1].childNodes[0].nodeValue;
                            }
                            else{
                                fila[<%=FilaProspectorSolicitudVO.POS_CAMPO_HORAS_CONTRATO%>] = '-';
                            }
                        }
                        else if(hijosFila[cont].nodeName=="HORAS_DEDICACION_ECA"){
                            nodoCampo = hijosFila[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[<%=FilaProspectorSolicitudVO.POS_CAMPO_HORAS_DEDICACION_ECA%>] = nodoCampo.childNodes[0].childNodes[0].nodeValue;
                                camposErrores[<%=FilaProspectorSolicitudVO.POS_CAMPO_HORAS_DEDICACION_ECA%>] = nodoCampo.childNodes[1].childNodes[0].nodeValue;
                            }
                            else{
                                fila[<%=FilaProspectorSolicitudVO.POS_CAMPO_HORAS_DEDICACION_ECA%>] = '-';
                            }
                        }
                        else if(hijosFila[cont].nodeName=="COSTES_SALARIALES_SS_JOR"){
                            nodoCampo = hijosFila[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[<%=FilaProspectorSolicitudVO.POS_CAMPO_COSTES_SALARIALES_SS_JOR%>] = nodoCampo.childNodes[0].childNodes[0].nodeValue;
                                camposErrores[<%=FilaProspectorSolicitudVO.POS_CAMPO_COSTES_SALARIALES_SS_JOR%>] = nodoCampo.childNodes[1].childNodes[0].nodeValue;
                            }
                            else{
                                fila[<%=FilaProspectorSolicitudVO.POS_CAMPO_COSTES_SALARIALES_SS_JOR%>] = '-';
                            }
                        }
                        else if(hijosFila[cont].nodeName=="COSTES_SALARIALES_SS_POR_JOR"){
                            nodoCampo = hijosFila[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[<%=FilaProspectorSolicitudVO.POS_CAMPO_COSTES_SALARIALES_SS_POR_JOR%>] = nodoCampo.childNodes[0].childNodes[0].nodeValue;
                                camposErrores[<%=FilaProspectorSolicitudVO.POS_CAMPO_COSTES_SALARIALES_SS_POR_JOR%>] = nodoCampo.childNodes[1].childNodes[0].nodeValue;
                            }
                            else{
                                fila[<%=FilaProspectorSolicitudVO.POS_CAMPO_COSTES_SALARIALES_SS_POR_JOR%>] = '-';
                            }
                        }
                        else if(hijosFila[cont].nodeName=="COSTES_SALARIALES_SS_ECA"){
                            nodoCampo = hijosFila[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[<%=FilaProspectorSolicitudVO.POS_CAMPO_COSTES_SALARIALES_SS_ECA%>] = nodoCampo.childNodes[0].childNodes[0].nodeValue;
                                camposErrores[<%=FilaProspectorSolicitudVO.POS_CAMPO_COSTES_SALARIALES_SS_ECA%>] = nodoCampo.childNodes[1].childNodes[0].nodeValue;
                            }
                            else{
                                fila[<%=FilaProspectorSolicitudVO.POS_CAMPO_COSTES_SALARIALES_SS_ECA%>] = '-';
                            }
                        }
                        else if(hijosFila[cont].nodeName=="VISITAS"){
                            nodoCampo = hijosFila[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[<%=FilaProspectorSolicitudVO.POS_CAMPO_VISITAS%>] = nodoCampo.childNodes[0].childNodes[0].nodeValue;
                                camposErrores[<%=FilaProspectorSolicitudVO.POS_CAMPO_VISITAS%>] = nodoCampo.childNodes[1].childNodes[0].nodeValue;
                            }
                            else{
                                fila[<%=FilaProspectorSolicitudVO.POS_CAMPO_VISITAS%>] = '-';
                            }
                        }
                        else if(hijosFila[cont].nodeName=="VISITAS_IMP"){
                            nodoCampo = hijosFila[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[<%=FilaProspectorSolicitudVO.POS_CAMPO_VISITAS_IMP%>] = nodoCampo.childNodes[0].childNodes[0].nodeValue;
                                camposErrores[<%=FilaProspectorSolicitudVO.POS_CAMPO_VISITAS_IMP%>] = nodoCampo.childNodes[1].childNodes[0].nodeValue;
                            }
                            else{
                                fila[<%=FilaProspectorSolicitudVO.POS_CAMPO_VISITAS_IMP%>] = '-';
                            }
                        }
                        else if(hijosFila[cont].nodeName=="COSTE"){
                            nodoCampo = hijosFila[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[<%=FilaProspectorSolicitudVO.POS_CAMPO_COSTE%>] = nodoCampo.childNodes[0].childNodes[0].nodeValue;
                                camposErrores[<%=FilaProspectorSolicitudVO.POS_CAMPO_COSTE%>] = nodoCampo.childNodes[1].childNodes[0].nodeValue;
                            }
                            else{
                                fila[<%=FilaProspectorSolicitudVO.POS_CAMPO_COSTE%>] = '-';
                            }
                        }
                        else if(hijosFila[cont].nodeName=="IMPORTE_CONCEDIDO"){
                            nodoCampo = hijosFila[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[<%=FilaProspectorSolicitudVO.POS_CAMPO_IMPORTE_CONCEDIDO%>] = nodoCampo.childNodes[0].childNodes[0].nodeValue;
                                camposErrores[<%=FilaProspectorSolicitudVO.POS_CAMPO_IMPORTE_CONCEDIDO%>] = nodoCampo.childNodes[1].childNodes[0].nodeValue;
                            }
                            else{
                                fila[<%=FilaProspectorSolicitudVO.POS_CAMPO_IMPORTE_CONCEDIDO%>] = '-';
                            }
                        }
                        else if(hijosFila[cont].nodeName=="TIPO_SUST"){
                            nodoCampo = hijosFila[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[16] = nodoCampo.childNodes[0].nodeValue;
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
                            fila[15] = listaErrores;
                        }
                    }
                    fila[17] = camposErrores;
                    listaProspectores[j] = fila;
                    fila = new Array();
                }else if(hijos[j].nodeName=="VALIDACION"){                            
                    msgValidacion = hijos[j].childNodes[0].nodeValue;
                }
            }//for(j=0;hijos!=null && j<hijos.length;j++)
                if(codigoOperacion=="0"){
                    window.returnValue =  listaProspectores;
                    barraProgresoEca('off', 'barraProgresoNuevoProspectorSolicitud');
                    var msj50='';
                    /*var hc = 0.0;
                    var hdECA = 0.0;
                    hc = parseFloat(convertirANumero(document.getElementById('horasContrato').value));                    
                    hdECA = parseFloat(convertirANumero(document.getElementById('horasDedicacionEca').value));
                    hc *= 0.5;
                    if(hdECA < hc){                                                
                            msj50 = '<%=meLanbide44I18n.getMensaje(idiomaUsuario, "msg.solicitud.prospectores.horasDedicacionEca50HorasContratoTexto")%>';
                            //msj50 = 'El número de Horas Dedicación ECA debe ser al menos el 50 por ciento del número de Horas Contrato.';
                    }*/
                    msj50+='<%=meLanbide44I18n.getMensaje(idiomaUsuario,"msg.datosGuardadosOK")%>';
                    //alert(msj50);
                    jsp_alerta("A",msj50);
                    
                    cerrarVentana();
                }else if(codigoOperacion=="1"){
                    jsp_alerta("A",'<%=meLanbide44I18n.getMensaje(idiomaUsuario,"error.errorBD")%>');
                }else if(codigoOperacion=="2"){
                    jsp_alerta("A",'<%=meLanbide44I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                }else if(codigoOperacion=="3"){
                    jsp_alerta("A",'<%=meLanbide44I18n.getMensaje(idiomaUsuario,"error.pasoParametros")%>');
                }else if(codigoOperacion=="4"){
                    jsp_alerta("A",msgValidacion);
                }else if(codigoOperacion=="5"){
                    jsp_alerta("A",'<%=meLanbide44I18n.getMensaje(idiomaUsuario,"error.nifProspectorRepetido")%>');
                }else{
                    jsp_alerta("A",'<%=meLanbide44I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                }//if(
            }
            catch(Err){
                jsp_alerta("A",'<%=meLanbide44I18n.getMensaje(idiomaUsuario,"error.errorProcesarRespuesta")%>');
            }//try-catch
            
            barraProgresoEca('off', 'barraProgresoNuevoProspectorSolicitud');
        }else{
            jsp_alerta("A", escape(mensajeValidacion));
        }
    }
    
    function validarDatos(){
        var nif = document.getElementById('nif').value;
        if(nif == null || nif == ''){
            mensajeValidacion = '<%=meLanbide44I18n.getMensaje(idiomaUsuario, "msg.solicitud.prospectores.nifVacio")%>';
            return false;
        }
        var nomApel = document.getElementById('nomApel').value;
        if(nomApel == null || nomApel == ''){
            mensajeValidacion = '<%=meLanbide44I18n.getMensaje(idiomaUsuario, "msg.solicitud.prospectores.nomApelVacio")%>';
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
                        mensajeValidacion = '<%=meLanbide44I18n.getMensaje(idiomaUsuario, "msg.solicitud.prospectores.nifIncorrecto")%>'+' '+'<%=meLanbide44I18n.getMensaje(idiomaUsuario, "msg.solicitud.prospectores.letraNifDeberiaSer")%>'+letra;
                    }else{
                        mensajeValidacion = '<%=meLanbide44I18n.getMensaje(idiomaUsuario, "msg.solicitud.prospectores.nifIncorrecto")%>';
                    }
                }
                correcto = false;
            }else{
                document.getElementById('nif').removeAttribute("style");
            }
        }
        
        
        if(!comprobarCaracteresEspecialesEca(nomApel)){
            document.getElementById('nomApel').style.border = '1px solid red';
            mensajeValidacion = '<%=meLanbide44I18n.getMensaje(idiomaUsuario, "msg.solicitud.prospectores.nomApelCaracteresEspeciales")%>';
            return false;
        }else{
            document.getElementById('nomApel').removeAttribute("style");
        }
        
        if(nuevo){
            if(!validarFechaEca(document.forms[0], document.getElementById('fechaInicio'))){
                document.getElementById('fechaInicio').style.border = '1px solid red';
                if(mensajeValidacion == '')
                    mensajeValidacion = '<%=meLanbide44I18n.getMensaje(idiomaUsuario, "msg.solicitud.prospectores.fechaIniIncorrecto")%>';
                correcto = false;
            }else{
                document.getElementById('fechaInicio').removeAttribute('style');
            }

            if(!validarFechaEca(document.forms[0], document.getElementById('fechaFin'))){
                document.getElementById('fechaFin').style.border = '1px solid red';
                if(mensajeValidacion == '')
                    mensajeValidacion = '<%=meLanbide44I18n.getMensaje(idiomaUsuario, "msg.solicitud.prospectores.fechaFinIncorrecto")%>';
                correcto = false;
            }else{
                document.getElementById('fechaFin').removeAttribute('style');
            }
            if(correcto){
                var feIni = document.getElementById('fechaInicio').value;
                var feFin = document.getElementById('fechaFin').value;
                if(feIni != '' && feFin != ''){
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
                            mensajeValidacion = '<%=meLanbide44I18n.getMensaje(idiomaUsuario, "msg.solicitud.prospectores.fechaIniPosteriorFin")%>';
                        correcto = false;
                    }
                    else{
                        if(anoIni != '<%=anoExpediente%>'){
                            correcto = false;
                            document.getElementById('fechaInicio').style.border = '1px solid red';
                            if(mensajeValidacion == '')
                                mensajeValidacion = '<%=String.format(meLanbide44I18n.getMensaje(idiomaUsuario, "validaciones.solicitud.prospectores.fechasAno"), anoExpediente)%>';
                        }else{
                            document.getElementById('fechaInicio').removeAttribute('style');
                        }
                        if(anoFin != '<%=anoExpediente%>'){
                            correcto = false;
                            document.getElementById('fechaFin').style.border = '1px solid red';
                            if(mensajeValidacion == '')
                                mensajeValidacion = '<%=String.format(meLanbide44I18n.getMensaje(idiomaUsuario, "validaciones.solicitud.prospectores.fechasAno"), anoExpediente)%>';
                        }else{
                            document.getElementById('fechaFin').removeAttribute('style');
                        }
                    }
                }
            }
        }
        
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
                        mensajeValidacion = '<%=meLanbide44I18n.getMensaje(idiomaUsuario, "msg.solicitud.prospectores.horasAnualesJCIncorrecto")%>';
                }else{
                    document.getElementById('horasAnualesJC').removeAttribute('style');
                    hajc = parseFloat(convertirANumero(document.getElementById('horasAnualesJC').value));
                }
            }catch(err){
                correcto = false;
                document.getElementById('horasAnualesJC').style.border = '1px solid red';
                if(mensajeValidacion == '')
                    mensajeValidacion = '<%=meLanbide44I18n.getMensaje(idiomaUsuario, "msg.solicitud.prospectores.horasAnualesJCIncorrecto")%>';
            }
        }
        if(document.getElementById('horasContrato').value != ''){
            try{
                if(!validarNumericoDecimalEca(document.getElementById('horasContrato'), 8, 2)){
                    correcto = false;
                    document.getElementById('horasContrato').style.border = '1px solid red';
                    if(mensajeValidacion == '')
                        mensajeValidacion = '<%=meLanbide44I18n.getMensaje(idiomaUsuario, "msg.solicitud.prospectores.horasContratoIncorrecto")%>';
                }else{
                    document.getElementById('horasContrato').removeAttribute('style');
                    hc = parseFloat(convertirANumero(document.getElementById('horasContrato').value));
                }
            }catch(err){
                correcto = false;
                document.getElementById('horasContrato').style.border = '1px solid red';
                if(mensajeValidacion == '')
                    mensajeValidacion = '<%=meLanbide44I18n.getMensaje(idiomaUsuario, "msg.solicitud.prospectores.horasContratoIncorrecto")%>';
            }
        }
        if(document.getElementById('horasDedicacionEca').value != ''){
            try{
                if(!validarNumericoDecimalEca(document.getElementById('horasDedicacionEca'), 8, 2)){
                    correcto = false;
                    document.getElementById('horasDedicacionEca').style.border = '1px solid red';
                    if(mensajeValidacion == '')
                        mensajeValidacion = '<%=meLanbide44I18n.getMensaje(idiomaUsuario, "msg.solicitud.prospectores.horasDedicacionEcaIncorrecto")%>';
                }else{
                    document.getElementById('horasDedicacionEca').removeAttribute('style');
                    hdECA = parseFloat(convertirANumero(document.getElementById('horasDedicacionEca').value));
                }
            }catch(err){
                correcto = false;
                document.getElementById('horasDedicacionEca').style.border = '1px solid red';
                if(mensajeValidacion == '')
                    mensajeValidacion = '<%=meLanbide44I18n.getMensaje(idiomaUsuario, "msg.solicitud.prospectores.horasDedicacionEcaIncorrecto")%>';
            }
        }

        if(correcto && nuevo){
            if(hajc < hc){
                correcto = false;
                document.getElementById('horasAnualesJC').style.border = '1px solid red';
                if(mensajeValidacion == '')
                    mensajeValidacion = '<%=meLanbide44I18n.getMensaje(idiomaUsuario, "msg.solicitud.prospectores.horasAnualesJCMenorHorasContrato")%>';
            }
            else{
                document.getElementById('horasAnualesJC').removeAttribute('style');
            }
            if(hc < hdECA){
                correcto = false;
                document.getElementById('horasContrato').style.border = '1px solid red';
                if(mensajeValidacion == '')
                    mensajeValidacion = '<%=meLanbide44I18n.getMensaje(idiomaUsuario, "msg.solicitud.prospectores.horasContratoMenorHorasDedicacionEca")%>';
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
                        mensajeValidacion = '<%=meLanbide44I18n.getMensaje(idiomaUsuario, "msg.solicitud.preparadores.horasContratoIncorrecto")%>';
                }else{
                    document.getElementById('horasContrato').removeAttribute('style');
                    hc = parseFloat(document.getElementById('horasContrato').value);
                }
            }catch(err){
                correcto = false;
                document.getElementById('horasContrato').style.border = '1px solid red';
                if(mensajeValidacion == '')
                    mensajeValidacion = '<%=meLanbide44I18n.getMensaje(idiomaUsuario, "msg.solicitud.preparadores.horasContratoIncorrecto")%>';
            }
        }
        if(document.getElementById('horasDedicacionEca').value != ''){
            try{
                if(!validarNumericoDecimalEca(document.getElementById('horasDedicacionEca'), 8, 2)){
                    correcto = false;
                    document.getElementById('horasDedicacionEca').style.border = '1px solid red';
                    if(mensajeValidacion == '')
                        mensajeValidacion = '<%=meLanbide44I18n.getMensaje(idiomaUsuario, "msg.solicitud.preparadores.horasDedicacionEcaIncorrecto")%>';
                }else{
                    document.getElementById('horasDedicacionEca').removeAttribute('style');
                    hdECA = parseFloat(document.getElementById('horasDedicacionEca').value);
                }
            }catch(err){
                correcto = false;
                document.getElementById('horasDedicacionEca').style.border = '1px solid red';
                if(mensajeValidacion == '')
                    mensajeValidacion = '<%=meLanbide44I18n.getMensaje(idiomaUsuario, "msg.solicitud.preparadores.horasDedicacionEcaIncorrecto")%>';
            }
        }

        /*if(correcto && nuevo){
            hc *= 0.5;
            if(hdECA < hc){
                correcto = false;
                document.getElementById('horasDedicacionEca').style.border = '1px solid red';
                if(mensajeValidacion == '')
                    mensajeValidacion = '<%//=meLanbide44I18n.getMensaje(idiomaUsuario, "msg.solicitud.prospectores.horasDedicacionEca50HorasContrato")%>';
            }else{
                document.getElementById('horasDedicacionEca').removeAttribute('style');
            }
        }*/
        
        
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
                        mensajeValidacion = '<%=meLanbide44I18n.getMensaje(idiomaUsuario, "msg.solicitud.prospectores.costesSSJorIncorrecto")%>';
                }else{
                    document.getElementById('costesSalarialesSSJor').removeAttribute('style');
                    cssJC = parseFloat(convertirANumero(document.getElementById('costesSalarialesSSJor').value));
                }
            }catch(err){
                correcto = false;
                document.getElementById('costesSalarialesSSJor').style.border = '1px solid red';
                if(mensajeValidacion == '')
                    mensajeValidacion = '<%=meLanbide44I18n.getMensaje(idiomaUsuario, "msg.solicitud.prospectores.costesSSJorIncorrecto")%>';
            }
        }
        if(document.getElementById('costesSalarialesSSPorJor').value != ''){
            try{
                if(!validarNumericoDecimalEca(document.getElementById('costesSalarialesSSPorJor'), 8, 2)){
                    correcto = false;
                    document.getElementById('costesSalarialesSSPorJor').style.border = '1px solid red';
                    if(mensajeValidacion == '')
                        mensajeValidacion = '<%=meLanbide44I18n.getMensaje(idiomaUsuario, "msg.solicitud.prospectores.costesSSPorJorIncorrecto")%>';
                }else{
                    document.getElementById('costesSalarialesSSPorJor').removeAttribute('style');
                    cssPorJor = parseFloat(convertirANumero(document.getElementById('costesSalarialesSSPorJor').value));
                }
            }catch(err){
                correcto = false;
                document.getElementById('costesSalarialesSSPorJor').style.border = '1px solid red';
                if(mensajeValidacion == '')
                    mensajeValidacion = '<%=meLanbide44I18n.getMensaje(idiomaUsuario, "msg.solicitud.prospectores.costesSSPorJorIncorrecto")%>';
            }
        }
        if(document.getElementById('costesSalarialesSSEca').value != ''){
            try{
                if(!validarNumericoDecimalEca(document.getElementById('costesSalarialesSSEca'), 8, 2)){
                    correcto = false;
                    document.getElementById('costesSalarialesSSEca').style.border = '1px solid red';
                    if(mensajeValidacion == '')
                        mensajeValidacion = '<%=meLanbide44I18n.getMensaje(idiomaUsuario, "msg.solicitud.prospectores.costesSSEcaIncorrecto")%>';
                }else{
                    document.getElementById('costesSalarialesSSEca').removeAttribute('style');
                    cssECA = parseFloat(convertirANumero(document.getElementById('costesSalarialesSSEca').value));
                }
            }catch(err){
                correcto = false;
                document.getElementById('costesSalarialesSSEca').style.border = '1px solid red';
                if(mensajeValidacion == '')
                    mensajeValidacion = '<%=meLanbide44I18n.getMensaje(idiomaUsuario, "msg.solicitud.prospectores.costesSSEcaIncorrecto")%>';
            }
        }

        if(correcto && nuevo){
            if(cssJC < cssPorJor){
                correcto = false;
                document.getElementById('costesSalarialesSSJor').style.border = '1px solid red';
                if(mensajeValidacion == '')
                    mensajeValidacion = '<%=meLanbide44I18n.getMensaje(idiomaUsuario, "msg.solicitud.prospectores.costesSalarialesJorMenorCostesSalarialesPorJor")%>';
            }
            else{
                document.getElementById('costesSalarialesSSJor').removeAttribute('style');
            }
            if(cssPorJor < cssECA){
                correcto = false;
                document.getElementById('costesSalarialesSSPorJor').style.border = '1px solid red';
                if(mensajeValidacion == '')
                    mensajeValidacion = '<%=meLanbide44I18n.getMensaje(idiomaUsuario, "msg.solicitud.prospectores.costesSalarialesPorJorMenorCostesSalarialesEca")%>';
            }else{
                document.getElementById('costesSalarialesSSPorJor').removeAttribute('style');
            }
        }
        
        if(document.getElementById('visitas').value != ''){
            try{
                if(!validarNumericoEca(document.getElementById('visitas'))){
                    correcto = false;
                    document.getElementById('visitas').style.border = '1px solid red';
                    if(mensajeValidacion == '')
                        mensajeValidacion = '<%=meLanbide44I18n.getMensaje(idiomaUsuario, "msg.solicitud.prospectores.visitasIncorrecto")%>';
                }else{
                    document.getElementById('visitas').removeAttribute('style');
                    cssECA = parseFloat(convertirANumero(document.getElementById('visitas').value));
                }
            }catch(err){
                correcto = false;
                document.getElementById('visitas').style.border = '1px solid red';
                if(mensajeValidacion == '')
                    mensajeValidacion = '<%=meLanbide44I18n.getMensaje(idiomaUsuario, "msg.solicitud.prospectores.visitasIncorrecto")%>';
            }
        }
        
        if(document.getElementById('visitasImp').value != ''){
            try{
                if(!validarNumericoDecimalEca(document.getElementById('visitasImp'), 8, 2)){
                    correcto = false;
                    document.getElementById('visitasImp').style.border = '1px solid red';
                    if(mensajeValidacion == '')
                        mensajeValidacion = '<%=meLanbide44I18n.getMensaje(idiomaUsuario, "msg.solicitud.prospectores.visitasImpIncorrecto")%>';
                }else{
                    document.getElementById('visitasImp').removeAttribute('style');
                    cssECA = parseFloat(convertirANumero(document.getElementById('visitasImp').value));
                }
            }catch(err){
                correcto = false;
                document.getElementById('visitasImp').style.border = '1px solid red';
                if(mensajeValidacion == '')
                    mensajeValidacion = '<%=meLanbide44I18n.getMensaje(idiomaUsuario, "msg.solicitud.prospectores.visitasImpIncorrecto")%>';
            }
        }
        
        if(document.getElementById('coste').value != ''){
            try{
                if(!validarNumericoDecimalEca(document.getElementById('coste'), 8, 2)){
                    correcto = false;
                    document.getElementById('coste').style.border = '1px solid red';
                    if(mensajeValidacion == '')
                        mensajeValidacion = '<%=meLanbide44I18n.getMensaje(idiomaUsuario, "msg.solicitud.prospectores.costeIncorrecto")%>';
                }else{
                    document.getElementById('coste').removeAttribute('style');
                    cssECA = parseFloat(convertirANumero(document.getElementById('coste').value));
                }
            }catch(err){
                correcto = false;
                document.getElementById('coste').style.border = '1px solid red';
                if(mensajeValidacion == '')
                    mensajeValidacion = '<%=meLanbide44I18n.getMensaje(idiomaUsuario, "msg.solicitud.prospectores.costeIncorrecto")%>';
            }
        }
        
        if(correcto && nuevo){
            /*if(document.getElementById('visitas').value != ''){
                //Número visitas: controlar mínimo 100 máximo 200
                try
                {
                    var numVisitas = parseInt(document.getElementById('visitas').value);
                    var minVisitas = getMinVisitas();
                    var maxVisitas = getMaxVisitas();
                    if(numVisitas < minVisitas || numVisitas > maxVisitas){
                        correcto = false;
                        document.getElementById('visitas').style.border = '1px solid red';
                        if(mensajeValidacion == ''){
                            var texto = '<%=meLanbide44I18n.getMensaje(idiomaUsuario, "validaciones.solicitud.prospectores.visitasFueraRangoJS")%>';
                            texto = reemplazarTextoEca(texto, /XX/g, ''+minVisitas);
                            texto = reemplazarTextoEca(texto, /YY/g, ''+maxVisitas);
                            mensajeValidacion = texto;
                        }
                    }else{
                        document.getElementById('visitas').removeAttribute('style');
                    }
                }catch(err){
                    correcto = false;
                    document.getElementById('visitas').style.border = '1px solid red';
                    if(mensajeValidacion == ''){
                        var texto = '<%=meLanbide44I18n.getMensaje(idiomaUsuario, "validaciones.solicitud.prospectores.visitasFueraRangoJS")%>';
                        texto = reemplazarTextoEca(texto, /XX/g, ''+minVisitas);
                        texto = reemplazarTextoEca(texto, /YY/g, ''+maxVisitas);
                        mensajeValidacion = texto;
                    }
                }
            }*/
            //Comprobar coste total visitas
            if(document.getElementById('visitasImp').value != ''){
                var numVisitas = parseInt(document.getElementById('visitas').value);
                var importeCalculado = numVisitas * <%=ecaConfig != null && ecaConfig.getImpVisita() != null ? ecaConfig.getImpVisita().doubleValue() : 0.0%>;
                var visitasImp = parseFloat(convertirANumero(document.getElementById('visitasImp').value));
                if(visitasImp != importeCalculado){
                    correcto = false;
                    document.getElementById('visitasImp').style.border = '1px solid red';
                    if(mensajeValidacion == '')
                        mensajeValidacion = '<%=meLanbide44I18n.getMensaje(idiomaUsuario, "validaciones.solicitud.prospectores.importeTotalVisitasIncorrecto")%>';
                }else{
                    document.getElementById('visitasImp').removeAttribute('style');
                }

                //Comprobar coste total solicitado prospector
                if(document.getElementById('coste').value != ''){
                    var coste = parseFloat(convertirANumero(document.getElementById('coste').value));
                    var minimo;
                    if(document.getElementById('costesSalarialesSSEca').value != ''){
                        var imSSEca = parseFloat(convertirANumero(document.getElementById('costesSalarialesSSEca').value));
                        minimo = Math.min(importeCalculado, imSSEca);
                    }else{
                        minimo = importeCalculado;
                    }


                    if(coste > minimo){
                        correcto = false;
                        document.getElementById('coste').style.border = '1px solid red';
                        if(mensajeValidacion == '')
                            mensajeValidacion = '<%=meLanbide44I18n.getMensaje(idiomaUsuario, "validaciones.solicitud.prospectores.importeSolicitadoIncorrecto")%>';
                    }else{
                        document.getElementById('coste').removeAttribute('style');
                    }
                }
            }else{
                if(document.getElementById('coste').value != ''){
                    if(document.getElementById('costesSalarialesSSEca').value){
                        var coste = parseFloat(convertirANumero(document.getElementById('coste').value));
                        var minimo = parseFloat(convertirANumero(document.getElementById('costesSalarialesSSEca').value));

                        if(coste > minimo){
                            correcto = false;
                            document.getElementById('coste').style.border = '1px solid red';
                            if(mensajeValidacion == '')
                                mensajeValidacion = '<%=meLanbide44I18n.getMensaje(idiomaUsuario, "validaciones.solicitud.prospectores.importeSolicitadoIncorrecto")%>';
                        }else{
                            document.getElementById('coste').removeAttribute('style');
                        }
                    }else{
                        correcto = false;
                        document.getElementById('coste').style.border = '1px solid red';
                        if(mensajeValidacion == '')
                            mensajeValidacion = '<%=meLanbide44I18n.getMensaje(idiomaUsuario, "validaciones.solicitud.prospectores.importeSolicitadoIncorrecto")%>';
                    }
                }
            }
        }
        return correcto;
    }
    
    function calcularImporteVisitas(){
        if(document.getElementById('visitas').value != '' && validarNumericoEca(document.getElementById('visitas'))){
            var numVisitas = parseInt(convertirANumero(document.getElementById('visitas').value));
            var importeCalculado = numVisitas * <%=ecaConfig != null && ecaConfig.getImpVisita() != null ? ecaConfig.getImpVisita().doubleValue() : 0.0%>;
            document.getElementById('visitasImp').value = importeCalculado;
            reemplazarPuntosEca(document.getElementById('visitasImp'));
        }else{
            document.getElementById('visitasImp').value = '';
        }
        calcularImporteSolicitadoProspector();
    }
    
    function calcularImporteSolicitadoProspector(){
        //reemplazarPuntosEca(document.getElementById('costesSalarialesSSEca'));
        var txtSSEca = validarNumericoDecimalEca(document.getElementById('costesSalarialesSSEca'), 50, 25) ? document.getElementById('costesSalarialesSSEca').value : '';
        var txtTotal = validarNumericoDecimalEca(document.getElementById('visitasImp'), 50, 25) ? document.getElementById('visitasImp').value : '';
        txtSSEca = convertirANumero(txtSSEca);
        txtTotal = convertirANumero(txtTotal);
        var minimo;
        if(txtSSEca != '' && txtTotal != ''){
            var eca = parseFloat(txtSSEca);
            var tot = parseFloat(txtTotal);
            minimo = Math.min(eca, tot);
        }else if(txtSSEca != ''){
            minimo = parseFloat(txtSSEca);
        }else if(txtTotal != ''){
            minimo = parseFloat(txtTotal);
        }
        if(minimo != undefined && minimo != null){
            document.getElementById('coste').value = minimo;
            reemplazarPuntosEca(document.getElementById('coste'));
        }else{
            document.getElementById('coste').value = '';
        }
    }
            
    function ajustarDecimalesImportes(){
        var campo;

        //Horas anueales jornada completa
        campo = document.getElementById('horasAnualesJC');
        ajustarDecimalesCampo(campo, 2);

        //Horas contrato
        campo = document.getElementById('horasContrato');
        ajustarDecimalesCampo(campo, 2);

        //Horas dedicacion ECA
        campo = document.getElementById('horasDedicacionEca');
        ajustarDecimalesCampo(campo, 2);

        //Costes salariales SS Jor
        campo = document.getElementById('costesSalarialesSSJor');
        ajustarDecimalesCampo(campo, 2);

        //Costes salariales SS % Jor
        campo = document.getElementById('costesSalarialesSSPorJor');
        ajustarDecimalesCampo(campo, 2);

        //Costes salariales SS ECA
        campo = document.getElementById('costesSalarialesSSEca');
        ajustarDecimalesCampo(campo, 2);

        //Total
        campo = document.getElementById('visitasImp');
        ajustarDecimalesCampo(campo, 2);
        
        //Importe total solicitado prospector
        campo = document.getElementById('coste');
        ajustarDecimalesCampo(campo, 2);
        
        //Importe concedido
        campo = document.getElementById('importeConcedido');
        ajustarDecimalesCampo(campo, 2);
    }
    
    function calcularCostesSalarialesECA(){
        var c1 = document.getElementById('horasDedicacionEca');
        var c2 = document.getElementById('costesSalarialesSSJor');
        var c3 = document.getElementById('horasAnualesJC');
        var result = 0.0;
        var valor = null;
        var valorCalculado = false;

        if(c1.value != '' && validarNumericoDecimalEca(c1, 50, 25)){
            if(c2.value != '' && validarNumericoDecimalEca(c2, 50, 25)){
                if(c3.value != '' && validarNumericoDecimalEca(c3, 50, 25)){
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
        
        calcularImporteSolicitadoProspector();
    }
    
    function calcularCostesSalarialesPorJor(){
        var c1 = document.getElementById('horasContrato');
        var c2 = document.getElementById('costesSalarialesSSJor');
        var c3 = document.getElementById('horasAnualesJC');
        var result = 0.0;
        var valor = null;
        var valorCalculado = false;

        if(c1.value != '' && validarNumericoDecimalEca(c1, 50, 25)){
            if(c2.value != '' && validarNumericoDecimalEca(c2, 50, 25)){
                if(c3.value != '' && validarNumericoDecimalEca(c3, 50, 25)){
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
                        
                        result = result.toFixed(2);
                        
                        if (isFinite(result)){

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
        
        calcularImporteSolicitadoProspector();
    }
    
    function calcularPorJornada(){
        var c1 = document.getElementById('horasAnualesJC');
        var c2 = document.getElementById('horasContrato');
        var result;
        var valor = -1;
        try
        {
            if(c1.value != '' && validarNumericoDecimalEca(c1, 50, 25)){
                if(c2.value != '' && validarNumericoDecimalEca(c2, 50, 25)){
                    valor = c2.value;
                    valor = convertirANumero(valor);
                    result = parseFloat(valor);
                    //result *= 100.0;

                    valor = c1.value;
                    valor = convertirANumero(valor);
                    result /= parseFloat(valor);
                    //result = result.toFixed(0);
                }
            }
        }catch(err){
            result = -1;
        }
        return result;
    }
    
    /*function actualizarMinimoVisitas(){
        try{
            var minVisitas = getMinVisitas();
            var texto = '<%=meLanbide44I18n.getMensaje(idiomaUsuario,"label.solicitud.prospectores.visitasJS")%>';
            texto = reemplazarTextoEca(texto, /XX/g, ''+minVisitas);
            document.getElementById('lblVisitas').innerText = texto;
        }catch(err){
            
        }
    }*/
    
    
    //Para el procedimiento de 2013 se tenian que calcular en funcion del porcentaje de jornada
    //Para 2014 son 100 siempre
    /*function getMinVisitas(){
        var porJornada = calcularPorJornada();
        var minVisitas = <%=ecaConfig != null && ecaConfig.getMinEmpVisit() != null ? ecaConfig.getMinEmpVisit() : 0%>;
        if(porJornada >= 0 && porJornada <= 1){
            minVisitas *= porJornada;
            minVisitas = minVisitas.toFixed(0);
        }
        return minVisitas;
    }*/
                
    function getMinVisitas(){
        var minVisitas = <%=ecaConfig != null && ecaConfig.getMinEmpVisit() != null ? ecaConfig.getMinEmpVisit() : 0%>;
        return minVisitas;
    }
    
    
    //Para el procedimiento de 2013 el maximo de visitas se calculaba en funcion del porcentaje de jornada
    //Para el 2014 son 200 siempre
    /*function getMaxVisitas(){
        var porJornada = calcularPorJornada();
        var maxVisitas = <%=ecaConfig != null && ecaConfig.getMaxEmpVisit() != null ? ecaConfig.getMaxEmpVisit() : 0%>;
        if(porJornada >= 0 && porJornada <= 1){
            maxVisitas *= porJornada;
            maxVisitas = maxVisitas.toFixed(0);
        }
        return maxVisitas;
    }*/
                
    function getMaxVisitas(){
        var maxVisitas = <%=ecaConfig != null && ecaConfig.getMaxEmpVisit() != null ? ecaConfig.getMaxEmpVisit() : 0%>;
        return maxVisitas;
    }
    
    function verComparacionProspector(){        
            var control = new Date();
            var result = null;
            var opcion = '';
            
            if(navigator.appName.indexOf("Internet Explorer")!=-1){
                result = lanzarPopUpModal('<%=request.getContextPath()%>/PeticionModuloIntegracion.do?tarea=preparar&modulo=MELANBIDE44&operacion=compararDatosProsSol&tipo=0&numero=<%=numExpediente%>&idPros=<%=prospectorModif != null && prospectorModif.getSolProspectoresCod() != null ? prospectorModif.getSolProspectoresCod() : ""%>&control='+control.getTime(),450,850,'no','no');
            }else{
                result = lanzarPopUpModal('<%=request.getContextPath()%>/PeticionModuloIntegracion.do?tarea=preparar&modulo=MELANBIDE44&operacion=compararDatosProsSol&tipo=0&numero=<%=numExpediente%>&idPros=<%=prospectorModif != null && prospectorModif.getSolProspectoresCod() != null ? prospectorModif.getSolProspectoresCod() : ""%>&control='+control.getTime(),470,870,'no','no');
            }                   
    }
    
</script>
<body onload="inicio();" class="contenidoPantalla">
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
                                                    <%=meLanbide44I18n.getMensaje(idiomaUsuario, "msg.guardandoDatos")%>
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
            <div class="sub3titulo" style="clear: both; text-align: left;height:18px; width: 97%;">
                    <span>
                        <%=meLanbide44I18n.getMensaje(idiomaUsuario,"label.solicitud.prospectores.datosProspector")%>
                    </span>
            </div> 
            <div class="lineaFormulario" id="divProsOrigen" style="height: 30px; margin-top: 10px; margin-bottom: 10px;">
                <div style="width: 120px; float: left;">
                    <%=meLanbide44I18n.getMensaje(idiomaUsuario,"label.solicitud.prospectores.prospectorOrigen")%>
                </div>
                <div style="width: 85px; float: left;">
                    <input type="text" id="nifOrigen" name="nifOrigen" size="10" maxlength="10" class="inputTexto readOnly" disabled="disabled"/>
                </div>                
                <div style="width: 500px; float: left;">
                    <input type="text" id="nomApelOrigen" name="nomApelOrigen" size="78" maxlength="200" class="inputTexto readOnly" disabled="disabled"/>
                </div>
            </div>
            <div style="clear:both"></div>
            <fieldset style="width: 94%;">    
                <legend>Prospector</legend>
                <div class="lineaFormulario">
                    <div style="width: 36px; float: left;">
                        <%=meLanbide44I18n.getMensaje(idiomaUsuario,"label.solicitud.prospectores.nif")%><font color="red">*</font>
                    </div>
                    <div style="width: 101px; float: left;">
                        <input type="text" id="nif" name="nif" size="10" maxlength="10" class="inputTexto"/>
                    </div>
                    <div style="width: 140px; float: left;">
                        <%=meLanbide44I18n.getMensaje(idiomaUsuario,"label.solicitud.prospectores.nomApel")%><font color="red">*</font>
                    </div>
                    <div style="width: 540px; float: left;">
                        <input type="text" id="nomApel" name="nomApel" size="83" maxlength="200" class="inputTexto"/>
                    </div>
                </div>
                <div class="lineaFormulario">
                    <div style="width: 169px; float: left;">
                        <%=meLanbide44I18n.getMensaje(idiomaUsuario,"label.solicitud.prospectores.fechaInicio")%>
                    </div>
                    <div style="width: 131px; float: left;">
                        <input type="text" class="inputTxtFecha" size="10" maxlength="10" id="fechaInicio" name="fechaInicio" onkeyup="return SoloCaracteresFecha(this);" onblur="javascript:return comprobarFechaEca(this, '<%=meLanbide44I18n.getMensaje(idiomaUsuario, "msg.fechaNoVal")%>');" onfocus="this.select();"/>
                        <A id="btnFechaInicio" href="javascript:calClick(event);" onclick="mostrarCalFechaInicio(event);return false;" style="text-decoration:none;" >
                            <IMG style="border: 0" height="17" id="calFechaInicio" name="calFechaInicio" alt="<%=meLanbide44I18n.getMensaje(idiomaUsuario,"label.solicitud.prospectores.fechaInicio")%>" src="<c:url value='/images/calendario/icono.gif'/>">
                        </A>
                    </div>
                    <div style="width: 155px; float: left;">
                        <%=meLanbide44I18n.getMensaje(idiomaUsuario,"label.solicitud.prospectores.fechaFin")%>
                    </div>
                    <div style="width: 131px; float: left;">
                        <input type="text" class="inputTxtFecha" size="10" maxlength="10" id="fechaFin" name="fechaFin" onkeyup="return SoloCaracteresFecha(this);" onblur="javascript:return comprobarFechaEca(this, '<%=meLanbide44I18n.getMensaje(idiomaUsuario, "msg.fechaNoVal")%>');" onfocus="this.select();"/>
                        <A id="btnFechaFin" href="javascript:calClick(event);" onclick="mostrarCalFechaFin(event);return false;" style="text-decoration:none;" >
                            <IMG style="border: 0" height="17" id="calFechaFin" name="calFechaFin" alt="<%=meLanbide44I18n.getMensaje(idiomaUsuario,"label.solicitud.prospectores.fechaFin")%>" src="<c:url value='/images/calendario/icono.gif'/>">
                        </A>
                    </div>
                </div>
                <div class="lineaFormulario">
                    <div style="width: 229px; float: left;">
                        <%=meLanbide44I18n.getMensaje(idiomaUsuario,"label.solicitud.prospectores.horasAnualesJC")%>
                    </div>
                    <div style="width: 108px; float: left;">
                        <input type="text" id="horasAnualesJC" name="horasAnualesJC" size="11" maxlength="9" class="inputTexto textoNumerico"                            
                               onkeyup="calcularCostesSalarialesSSJC();FormatNumber(this.value, 8, 2, this.id);"
                               onblur="calcularCostesSalarialesSSJC();ajustarDecimalesImportes();"/>
                    </div>
                    <div style="width: 117px; float: left;">
                        <%=meLanbide44I18n.getMensaje(idiomaUsuario,"label.solicitud.prospectores.horasContrato")%>
                    </div>
                    <div style="width: 108px; float: left;">
                        <input type="text" id="horasContrato" name="horasContrato" size="11" maxlength="9" class="inputTexto textoNumerico" 
                               onkeyup="calcularCostesSalarialesPorJor();FormatNumber(this.value, 8, 2, this.id);"
                               onblur="calcularCostesSalarialesPorJor();ajustarDecimalesImportes();"/>
                    </div>
                    <div style="width: 157px; float: left;">
                        <%=meLanbide44I18n.getMensaje(idiomaUsuario,"label.solicitud.prospectores.horasDedicacionEca")%>
                    </div>
                    <div style="width: 108px; float: left;">
                        <input type="text" id="horasDedicacionEca" name="horasDedicacionEca" size="11" maxlength="9" class="inputTexto textoNumerico" 
                               onkeyup="calcularCostesSalarialesSSJC();calcularCostesSalarialesPorJor();FormatNumber(this.value, 8, 2, this.id);"
                               onblur="calcularCostesSalarialesSSJC();calcularCostesSalarialesPorJor();ajustarDecimalesImportes();"/>
                    </div>
                </div>
                <div class="lineaFormulario">
                    <div style="width: 229px; float: left;">
                        <%=meLanbide44I18n.getMensaje(idiomaUsuario,"label.solicitud.prospectores.costesSalarialesSSJor")%>
                    </div>
                    <div style="width: 108px; float: left;">
                        <input type="text" id="costesSalarialesSSJor" name="costesSalarialesSSJor" size="11" maxlength="9" class="inputTexto textoNumerico readOnly" disabled="disabled"/>
                    </div>
                    <div style="width: 117px; float: left;">
                        <%=meLanbide44I18n.getMensaje(idiomaUsuario,"label.solicitud.prospectores.costesSalarialesSSPorJor")%>
                    </div>
                    <div style="width: 108px; float: left;">
                        <input type="text" id="costesSalarialesSSPorJor" name="costesSalarialesSSPorJor" size="11" maxlength="9" class="inputTexto textoNumerico readOnly" disabled="disabled"/>
                    </div>
                    <div style="width: 157px; float: left;">
                        <%=meLanbide44I18n.getMensaje(idiomaUsuario,"label.solicitud.prospectores.costesSalarialesSSEca")%>
                    </div>
                    <div style="width: 108px; float: left;">
                        <input type="text" id="costesSalarialesSSEca" name="costesSalarialesSSEca" size="11" maxlength="9" class="inputTexto textoNumerico" 
                               onkeyup="calcularCostesSalarialesSSJC();calcularCostesSalarialesPorJor();FormatNumber(this.value, 8, 2, this.id);" 
                               onblur="calcularCostesSalarialesSSJC();calcularCostesSalarialesPorJor();ajustarDecimalesImportes();"/>
                    </div>
                </div>
                <div class="lineaFormulario">
                    <div style="width: 229px; float: left;">
                        <label id="lblVisitas"><%=meLanbide44I18n.getMensaje(idiomaUsuario,"label.solicitud.prospectores.visitas")%></label>
                    </div>
                    <div style="width: 108px; float: left;">
                        <input type="text" id="visitas" name="visitas" size="11" maxlength="9" class="inputTexto textoNumerico"   
                               onkeyup="calcularImporteVisitas();FormatNumber(this.value, 8, 2, this.id);"
                               onblur="calcularImporteVisitas();ajustarDecimalesImportes();"/>
                    </div>
                    <div style="width: 117px; float: left;">
                        <%=meLanbide44I18n.getMensaje(idiomaUsuario,"label.solicitud.prospectores.visitasImp")%>
                    </div>
                    <div style="width: 108px; float: left;">
                        <input type="text" id="visitasImp" name="visitasImp" size="11" maxlength="9" class="inputTexto textoNumerico readOnly" disabled="disabled"/>
                    </div>
                    <div style="width: 157px; float: left;">
                        <%=meLanbide44I18n.getMensaje(idiomaUsuario,"label.solicitud.prospectores.coste")%>
                    </div>
                    <div style="width: 108px; float: left;">
                        <input type="text" id="coste" name="coste" size="11" maxlength="9" class="inputTexto textoNumerico" 
                               onkeyup="FormatNumber(this.value, 8, 2, this.id);"
                               onblur="ajustarDecimalesImportes();"/>
                    </div>
                </div>
                 <div class="lineaFormulario">
                    <div style="width: 162px; float: left;">
                        <%=meLanbide44I18n.getMensaje(idiomaUsuario,"label.solicitud.prospectores.impteConcedido")%>
                    </div>
                    <div style="width: 108px; float: left;">
                        <input type="text" id="importeConcedido" name="importeConcedido" size="11" maxlength="9" class="inputTexto textoNumerico" onkeyup="FormatNumber(this.value, 8, 2, this.id);" onblur="ajustarDecimalesImportes();"/>
                    </div>
                </div>
            </fieldset>
            <div class="lineaFormulario" id="divProsSustituto" style="height: 30px; margin-top: 10px; margin-bottom: 10px;">
                <div style="width: 120px; float: left;">
                    <%=meLanbide44I18n.getMensaje(idiomaUsuario,"label.solicitud.prospectores.prospectorSustituto")%>
                </div>
                <div style="width: 85px; float: left;">
                    <input type="text" id="nifSustituto" name="nifSustituto" size="10" maxlength="10" class="inputTexto readOnly" disabled="disabled"/>
                </div>                
                <div style="width: 500px; float: left;">
                    <input type="text" id="nomApelSustituto" name="nomApelSustituto" size="78" maxlength="200" class="inputTexto readOnly" disabled="disabled"/>
                </div>
               
            </div>
            

            <div class="botonera" style="padding-top: 20px;">
                <input type="button" id="btnAceptar" name="btnAceptar" class="botonGeneral" value="<%=meLanbide44I18n.getMensaje(idiomaUsuario, "btn.general.aceptar")%>" onclick="guardar();">
                <input type="button" id="btnCancelar" name="btnCancelar" class="botonGeneral" value="<%=meLanbide44I18n.getMensaje(idiomaUsuario, "btn.general.cancelar")%>" onclick="cancelar();">
                <input type="button" id="btnCerrar" name="btnCerrar" class="botonGeneral" value="<%=meLanbide44I18n.getMensaje(idiomaUsuario, "btn.general.cerrar")%>" onclick="cerrarVentana();" style="display: none;">
                <input type="button" id="btnComparacion" name="btnAceptar" class="botonGeneral" value="<%=meLanbide44I18n.getMensaje(idiomaUsuario, "btn.general.comparacion")%>" onclick="verComparacionProspector();">
            </div>
        </div>
    </form>
    <div id="popupcalendar" class="text"></div>
</body>

<script type="text/javascript">
    
    
</script>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>

<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide35.i18n.MeLanbide35I18n" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide35.vo.solicitud.FilaPreparadorSolicitudVO" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide35.util.ConstantesMeLanbide35" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide35.util.MeLanbide35Utils" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@ page import="es.altia.common.service.config.Config"%>
<%@ page import="es.altia.common.service.config.ConfigServiceHelper"%>
<%@page import="java.util.ArrayList" %>
<%@page import="java.util.List" %>

<%
    String sIdioma = request.getParameter("idioma");
    int idiomaUsuario = Integer.parseInt(sIdioma);
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
    //Clase para internacionalizar los mensajes de la aplicación.
    MeLanbide35I18n meLanbide35I18n = MeLanbide35I18n.getInstance();
    
    Config m_Config = ConfigServiceHelper.getConfig("common");
    String statusBar = m_Config.getString("JSP.StatusBar");
    String nombreModulo     = request.getParameter("nombreModulo");
    String codOrganizacion  = request.getParameter("codOrganizacionModulo");
    String numExpediente    = request.getParameter("numero");
    Integer anoExpediente = MeLanbide35Utils.getEjercicioDeExpediente(numExpediente);
    
    String nombreTabla = "solicitudes";
%>

<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/estilo.css"/>
<link rel="stylesheet" type="text/css" href="<c:url value='/css/extension/melanbide35/melanbide35.css'/>">
<link rel="stylesheet" type="text/css" href="<c:url value='/css/table.css'/>">


<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/popup.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/melanbide35/FixedColumnsTable.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/melanbide35/ecaUtils.js"></script>

<script type="text/javascript">
    
    function cargarFicheroPreparadoresEca(){
        var hayFicheroSeleccionado = false;
        if(document.getElementById('fichero_preparadores_sol').files){
            if(document.getElementById('fichero_preparadores_sol').files[0]){
                hayFicheroSeleccionado = true;
            }
        }else if(document.getElementById('fichero_preparadores_sol').value != ''){
            hayFicheroSeleccionado = true;
        }
        if(hayFicheroSeleccionado){
            var resultado = 1;
            if(listaPreparadores_sol != undefined && listaPreparadores_sol.length > 0){
                resultado = jsp_alerta('', '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "msg.preguntaImportarSolicitud")%>');
            }
            if (resultado == 1){
                document.getElementById('msgImportandoPreparadores').style.display='inline';
                document.getElementById('msgEliminandoPreparadores').style.display='none';
                barraProgresoEca('on', 'barraProgresoPreparadoresSolicitud');
                var CONTEXT_PATH = '<%=request.getContextPath()%>';
                var url = CONTEXT_PATH + '/PeticionModuloIntegracion.do';
                var control = new Date();
                var parametros = 'tarea=preparar&modulo=MELANBIDE35&operacion=procesarExcelPreparadores&tipo=0&numero=<%=numExpediente%>&control='+control.getTime();
                document.forms[0].action = url+'?'+parametros;
                document.forms[0].enctype = 'multipart/form-data';
                document.forms[0].encoding = 'multipart/form-data';
                document.forms[0].method = 'POST';
                document.forms[0].target = 'uploadFramePreparadoresSol';
                document.forms[0].submit();
            }
            return false;
        }else{
            jsp_alerta("A",'<%=meLanbide35I18n.getMensaje(idiomaUsuario,"msg.ficheroNoSeleccionado")%>');
            return false;
        }
    }

    function uploadProgress(evt) {
        if (evt.lengthComputable) {
            var percentComplete = Math.round(evt.loaded * 100 / evt.total);
            document.getElementById('progressNumber').innerHTML = percentComplete.toString() + '%';
            document.getElementById('prog').value = percentComplete;
        }
        else {
            document.getElementById('progressNumber').innerHTML = 'unable to compute';
        }
    }

    function uploadComplete(evt) {
        /* This event is raised when the server send back a response */
        //alert(evt.target.responseText);
    }

    function uploadFailed(evt) {
        //alert("There was an error attempting to upload the file.");
    }

    function uploadCanceled(evt) {
        //alert("The upload has been canceled by the user or the browser dropped the connection.");
    }
    
    function getListaPreparadoresSolicitudEca(){
        var ajax = getXMLHttpRequest();
        var nodos = null;
        var CONTEXT_PATH = '<%=request.getContextPath()%>'
        var url = CONTEXT_PATH + '/PeticionModuloIntegracion.do';
        var parametros = '';
        var control = new Date();
        var listaPreparadores = new Array();
        parametros = 'tarea=preparar&modulo=MELANBIDE35&operacion=getListaPreparadoresSolicitud&tipo=0&numero=<%=numExpediente%>&control='+control.getTime();
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
            listaPreparadores = extraerListadoPreparadoresSolicitudEca(nodos);
        }
        catch(err){
            jsp_alerta("A",'<%=meLanbide35I18n.getMensaje(idiomaUsuario,"error.errorProcesarRespuesta")%>');
        }//try-catch
        return listaPreparadores;
    }
    
    function recargarTablaPreparadoresSolicitudEca(result, actualizarOtrasPestanas){
        
        var fila;
        var haySustitutos = false;
        listaPreparadores_sol = new Array();
        listaPreparadores_solTabla = new Array();
        listaPreparadores_solTabla_titulos = new Array();
        listaPreparadores_solTabla_estilos = new Array();

        var listaEstilosFila;
        
        for(var i = 1;i< result.length; i++){
            fila = result[i];
            listaPreparadores_sol[i-1] = fila;
            listaPreparadores_solTabla[i-1] = [fila[29] != undefined && fila[29].length > 0 ? '<img src="<%=request.getContextPath()%>/images/extension/melanbide35/alert.png"/>' : '', fila[1], fila[2], fila[3], fila[4], fila[5], fila[6], fila[7], fila[8], fila[9], fila[10], fila[11], fila[12], fila[13], fila[14], fila[15], fila[16], fila[17], fila[18], fila[19], fila[20], fila[21], fila[22], fila[23], fila[24], fila[25], fila[26], fila[27], fila[28]];
            listaPreparadores_solTabla_titulos[i-1] = [getListAsText(fila[29]), fila[1], fila[2], fila[3], fila[4], fila[5], fila[6], fila[7], fila[8], fila[9], fila[10], fila[11], fila[12], fila[13], fila[14], fila[15], fila[16], fila[17], fila[18], fila[19], fila[20], fila[21], fila[22], fila[23], fila[24], fila[25], fila[26], fila[27], fila[28]];
            if(fila[29] != undefined && fila[29].length > 0){
                listaEstilosFila = new Array();
                camposErrores = fila[30];
                for(var j = 0; j < camposErrores.length; j++){
                    if(camposErrores[j] == '<%=ConstantesMeLanbide35.CIERTO%>'){
                        listaEstilosFila[j] = 'color: red;';
                    }else{
                        listaEstilosFila[j] = 'color: black;';
                    }
                }
                
                listaEstilosFila[<%=FilaPreparadorSolicitudVO.POS_CAMPO_NIF%>] = 'color: red;';
                listaEstilosFila[<%=FilaPreparadorSolicitudVO.POS_CAMPO_NOMBREAPEL%>] = 'color: red;';
                
                listaPreparadores_solTabla_estilos[i-1] = listaEstilosFila;
            }
            
            if(fila[31] != undefined && fila[31] != null && fila[31] == '1'){
                haySustitutos = true;
                if(fila[28] == undefined || fila[28].length == 0 || listaEstilosFila == undefined || listaEstilosFila == null){
                    listaEstilosFila = new Array();
                }
                var estilo;
                if(listaEstilosFila[<%=FilaPreparadorSolicitudVO.POS_CAMPO_NIF%>] != undefined){
                    estilo = listaEstilosFila[<%=FilaPreparadorSolicitudVO.POS_CAMPO_NIF%>];
                }else{
                    estilo = '';
                }
                estilo = estilo+'padding-left: 20px;';
                listaEstilosFila[<%=FilaPreparadorSolicitudVO.POS_CAMPO_NIF%>] = estilo;
                if(listaEstilosFila[<%=FilaPreparadorSolicitudVO.POS_CAMPO_NOMBREAPEL%>] != undefined){
                    estilo = listaEstilosFila[<%=FilaPreparadorSolicitudVO.POS_CAMPO_NOMBREAPEL%>];
                }
                estilo = estilo+'padding-left: 20px;';
                listaEstilosFila[<%=FilaPreparadorSolicitudVO.POS_CAMPO_NOMBREAPEL%>] = estilo;
                
                listaPreparadores_solTabla_estilos[i-1] = listaEstilosFila;
            }
        }

        tabPreparadores_sol = new FixedColumnTable(document.getElementById('listaPreparadores_sol'), 850, 966, 'listaPreparadores_sol');
        tabPreparadores_sol.addColumna('30','center','<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.solicitud.preparadores.tabla.col0")%>','<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.solicitud.preparadores.tabla.col0.title")%>');
        if(haySustitutos){
            tabPreparadores_sol.addColumna('140','left','<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.solicitud.preparadores.tabla.col1")%>','<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.solicitud.preparadores.tabla.col1.title")%>');
        }else{
            tabPreparadores_sol.addColumna('80','left','<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.solicitud.preparadores.tabla.col1")%>','<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.solicitud.preparadores.tabla.col1.title")%>');
        }
        tabPreparadores_sol.addColumna('143','left','<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.solicitud.preparadores.tabla.col2")%>','<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.solicitud.preparadores.tabla.col2.title")%>');
        tabPreparadores_sol.addColumna('70','left','<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.solicitud.preparadores.tabla.col4")%>','<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.solicitud.preparadores.tabla.col4.title")%>');
        tabPreparadores_sol.addColumna('70','right','<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.solicitud.preparadores.tabla.col5")%>','<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.solicitud.preparadores.tabla.col5.title")%>');
        tabPreparadores_sol.addColumna('70','right','<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.solicitud.preparadores.tabla.col6")%>','<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.solicitud.preparadores.tabla.col6.title")%>');
        tabPreparadores_sol.addColumna('70','right','<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.solicitud.preparadores.tabla.col7")%>','<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.solicitud.preparadores.tabla.col7.title")%>');
        tabPreparadores_sol.addColumna('70','right','<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.solicitud.preparadores.tabla.col8")%>','<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.solicitud.preparadores.tabla.col8.title")%>');
        tabPreparadores_sol.addColumna('90','right','<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.solicitud.preparadores.tabla.col9")%>','<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.solicitud.preparadores.tabla.col9.title")%>');
        tabPreparadores_sol.addColumna('90','right','<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.solicitud.preparadores.tabla.col10")%>','<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.solicitud.preparadores.tabla.col0.title")%>');
        tabPreparadores_sol.addColumna('90','right','<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.solicitud.preparadores.tabla.col11")%>','<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.solicitud.preparadores.tabla.col11.title")%>');
        tabPreparadores_sol.addColumna('50','right','<%=String.format(meLanbide35I18n.getMensaje(idiomaUsuario, "label.solicitud.preparadores.tabla.col12"), anoExpediente)%>','<%=String.format(meLanbide35I18n.getMensaje(idiomaUsuario, "label.solicitud.preparadores.tabla.col12.title"), anoExpediente)%>');
        tabPreparadores_sol.addColumna('70','right','<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.solicitud.preparadores.tabla.col13")%>','<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.solicitud.preparadores.tabla.col13.title")%>');
        tabPreparadores_sol.addColumna('40','right','<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.solicitud.preparadores.tabla.col14")%>','<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.solicitud.preparadores.tabla.col14.title")%>');
        tabPreparadores_sol.addColumna('40','right','<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.solicitud.preparadores.tabla.col15")%>','<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.solicitud.preparadores.tabla.col15.title")%>');
        tabPreparadores_sol.addColumna('50','right','<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.solicitud.preparadores.tabla.col16")%>','<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.solicitud.preparadores.tabla.col16.title")%>');
        tabPreparadores_sol.addColumna('40','right','<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.solicitud.preparadores.tabla.col17")%>','<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.solicitud.preparadores.tabla.col17.title")%>');
        tabPreparadores_sol.addColumna('40','right','<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.solicitud.preparadores.tabla.col18")%>','<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.solicitud.preparadores.tabla.col18.title")%>');
        tabPreparadores_sol.addColumna('50','right','<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.solicitud.preparadores.tabla.col19")%>','<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.solicitud.preparadores.tabla.col19.title")%>');
        tabPreparadores_sol.addColumna('40','right','<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.solicitud.preparadores.tabla.col20")%>','<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.solicitud.preparadores.tabla.col20.title")%>');
        tabPreparadores_sol.addColumna('40','right','<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.solicitud.preparadores.tabla.col21")%>','<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.solicitud.preparadores.tabla.col21.title")%>');
        tabPreparadores_sol.addColumna('50','right','<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.solicitud.preparadores.tabla.col22")%>','<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.solicitud.preparadores.tabla.col22.title")%>');
        tabPreparadores_sol.addColumna('40','right','<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.solicitud.preparadores.tabla.col23")%>','<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.solicitud.preparadores.tabla.col23.title")%>');
        tabPreparadores_sol.addColumna('40','right','<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.solicitud.preparadores.tabla.col24")%>','<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.solicitud.preparadores.tabla.col24.title")%>');
        tabPreparadores_sol.addColumna('50','right','<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.solicitud.preparadores.tabla.col25")%>','<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.solicitud.preparadores.tabla.col25.title")%>');
        tabPreparadores_sol.addColumna('70','right','<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.solicitud.preparadores.tabla.col26")%>','<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.solicitud.preparadores.tabla.col26.title")%>');
        tabPreparadores_sol.addColumna('70','right','<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.solicitud.preparadores.tabla.col27")%>','<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.solicitud.preparadores.tabla.col27.title")%>');
        tabPreparadores_sol.addColumna('80','right','<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.solicitud.preparadores.tabla.col28")%>','<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.solicitud.preparadores.tabla.col28.title")%>');
        tabPreparadores_sol.addColumna('80','right','<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.solicitud.preparadores.tabla.col29")%>','<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.solicitud.preparadores.tabla.col29.title")%>');

        tabPreparadores_sol.numColumnasFijas = 3;

        //tabPreparadores_sol.lineas=listaPreparadores_solTabla;
        
        for(var cont = 0; cont < listaPreparadores_solTabla.length; cont++){
            tabPreparadores_sol.addFilaConFormato(listaPreparadores_solTabla[cont], listaPreparadores_solTabla_titulos[cont], listaPreparadores_solTabla_estilos[cont])
        }

        tabPreparadores_sol.height = '300';

        tabPreparadores_sol.altoCabecera = 50;
    
        if(haySustitutos){
            if(navigator.appName.indexOf("Internet Explorer")!=-1){
                tabPreparadores_sol.scrollWidth = 1920;
            }else{
                tabPreparadores_sol.scrollWidth = 2220;
            }
        }else{
            if(navigator.appName.indexOf("Internet Explorer")!=-1){
                tabPreparadores_sol.scrollWidth = 1820;
            }else{
                tabPreparadores_sol.scrollWidth = 2120;
            }
        }
    
        tabPreparadores_sol.dblClkFunction = 'dblClckTablaPreparadoresSolicitud';

        tabPreparadores_sol.displayTabla();

        tabPreparadores_sol.pack();
        
        if(actualizarOtrasPestanas){
            actualizarOtrasPestanasEca(2);
        }
    }
    
    function actualizarTablaPreparadoresSolicitudEca(actualizarOtrasPestanas){
        try{
            var result = getListaPreparadoresSolicitudEca();
            recargarTablaPreparadoresSolicitudEca(result, actualizarOtrasPestanas);
        }catch(err){
        }
        barraProgresoEca('off', 'barraProgresoPreparadoresSolicitud');
    }
    
    function pulsarNuevoPreparadorSolicitudEca(){
            var control = new Date();
            var result = null;
            if(navigator.appName.indexOf("Internet Explorer")!=-1){
                result = lanzarPopUpModal('<%=request.getContextPath()%>/PeticionModuloIntegracion.do?tarea=preparar&modulo=MELANBIDE35&operacion=cargarNuevoPreparadorSolicitud&tipo=0&numero=<%=numExpediente%>&opcion=nuevo&control='+control.getTime(),450,850,'no','no');
            }else{
                result = lanzarPopUpModal('<%=request.getContextPath()%>/PeticionModuloIntegracion.do?tarea=preparar&modulo=MELANBIDE35&operacion=cargarNuevoPreparadorSolicitud&tipo=0&numero=<%=numExpediente%>&opcion=nuevo&control='+control.getTime(),470,870,'no','no');
            }
            if (result != undefined){
                recargarTablaPreparadoresSolicitudEca(result, true);
            }
    }
    
    function pulsarSustituirPreparadorSolicitudEca(){
        if(tabPreparadores_sol.selectedIndex != -1) {
            if(listaPreparadores_sol[tabPreparadores_sol.selectedIndex][30] == undefined || listaPreparadores_sol[tabPreparadores_sol.selectedIndex][30] == null || listaPreparadores_sol[tabPreparadores_sol.selectedIndex][30] != '1')
            {
                var control = new Date();
                var result = null;
                if(navigator.appName.indexOf("Internet Explorer")!=-1){
                    result = lanzarPopUpModal('<%=request.getContextPath()%>/PeticionModuloIntegracion.do?tarea=preparar&modulo=MELANBIDE35&operacion=cargarSustituirPreparadorSolicitud&tipo=0&numero=<%=numExpediente%>&idPrepSustituir='+listaPreparadores_sol[tabPreparadores_sol.selectedIndex][0]+'&control='+control.getTime(),510,850,'no','no');
                }else{
                    result = lanzarPopUpModal('<%=request.getContextPath()%>/PeticionModuloIntegracion.do?tarea=preparar&modulo=MELANBIDE35&operacion=cargarSustituirPreparadorSolicitud&tipo=0&numero=<%=numExpediente%>&idPrepSustituir='+listaPreparadores_sol[tabPreparadores_sol.selectedIndex][0]+'&control='+control.getTime(),530,870,'no','no');
                }
                if (result != undefined){
                    if(result[0] == '0'){
                        recargarTablaPreparadoresSolicitudEca(result, true);
                    }
                }
            }else{
                jsp_alerta('A', '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "msg.sustitutoSustNoPermitido")%>');
            }
        } 
        else {
            jsp_alerta('A', '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "msg.msjNoSelecFila")%>');
        }
    }
    
    function pulsarModifPreparadorSolicitudEca(){
        if(tabPreparadores_sol.selectedIndex != -1) {
            var control = new Date();
            var result = null;
            var opcion = '';
            if(document.forms[0].modoConsulta.value == "si"){
                opcion = 'consultar';
            }else{
                opcion = 'modificar';
            }
            if(navigator.appName.indexOf("Internet Explorer")!=-1){
                result = lanzarPopUpModal('<%=request.getContextPath()%>/PeticionModuloIntegracion.do?tarea=preparar&modulo=MELANBIDE35&operacion=cargarNuevoPreparadorSolicitud&tipo=0&numero=<%=numExpediente%>&opcion='+opcion+'&idPrepModif='+listaPreparadores_sol[tabPreparadores_sol.selectedIndex][0]+'&control='+control.getTime(),510,850,'no','no');
            }else{
                result = lanzarPopUpModal('<%=request.getContextPath()%>/PeticionModuloIntegracion.do?tarea=preparar&modulo=MELANBIDE35&operacion=cargarNuevoPreparadorSolicitud&tipo=0&numero=<%=numExpediente%>&opcion='+opcion+'&idPrepModif='+listaPreparadores_sol[tabPreparadores_sol.selectedIndex][0]+'&control='+control.getTime(),530,870,'no','no');
            }
            if (result != undefined){
                recargarTablaPreparadoresSolicitudEca(result, true);
            }
        } 
        else {
            jsp_alerta('A', '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "msg.msjNoSelecFila")%>');
        }
    }
    
    function pulsarEliminarPreparadorSolicitudEca(){
        

            if(tabPreparadores_sol.selectedIndex != -1) {
                var resultado = jsp_alerta('', '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "msg.preguntaEliminar")%>');
                if (resultado == 1){
                    
                    document.getElementById('msgImportandoPreparadores').style.display="none";
                    document.getElementById('msgEliminandoPreparadores').style.display="inline";
                    barraProgresoEca('on', 'barraProgresoPreparadoresSolicitud');
                    var ajax = getXMLHttpRequest();
                    var nodos = null;
                    var CONTEXT_PATH = '<%=request.getContextPath()%>'
                    var url = CONTEXT_PATH + "/PeticionModuloIntegracion.do";
                    var parametros = "";
                    var control = new Date();
                    var listaPreparadores = new Array();
                    parametros = 'tarea=preparar&modulo=MELANBIDE35&operacion=eliminarPreparadorSolicitud&tipo=0&numero=<%=numExpediente%>&idPrep='+listaPreparadores_sol[tabPreparadores_sol.selectedIndex][0]+'&control='+control.getTime();
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
                        listaPreparadores = extraerListadoPreparadoresSolicitudEca(nodos);
                        barraProgresoEca('off', 'barraProgresoPreparadoresSolicitud');
                        var codigoOperacion = listaPreparadores[0];
                        if(codigoOperacion=="0"){
                            recargarTablaPreparadoresSolicitudEca(listaPreparadores, true);
                            jsp_alerta("A",'<%=meLanbide35I18n.getMensaje(idiomaUsuario,"msg.registroEliminadoOK")%>');
                        }else if(codigoOperacion=="1"){
                            jsp_alerta("A",'<%=meLanbide35I18n.getMensaje(idiomaUsuario,"error.errorEliminarBD")%>');
                        }else if(codigoOperacion=="2"){
                            jsp_alerta("A",'<%=meLanbide35I18n.getMensaje(idiomaUsuario,"error.errorEliminarGen")%>');
                        }else if(codigoOperacion=="3"){
                            jsp_alerta("A",'<%=meLanbide35I18n.getMensaje(idiomaUsuario,"error.pasoParametrosEliminar")%>');
                        }else{
                            jsp_alerta("A",'<%=meLanbide35I18n.getMensaje(idiomaUsuario,"error.errorEliminarGen")%>');
                        }//if(
                    }
                    catch(Err){
                        jsp_alerta("A",'<%=meLanbide35I18n.getMensaje(idiomaUsuario,"error.errorProcesarRespuesta")%>');
                    }//try-catch
                }
            } 
            else {
                jsp_alerta('A', '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "msg.msjNoSelecFila")%>');
            }
    }
    
    function extraerListadoPreparadoresSolicitudEca(nodos){
            var elemento = nodos[0];
            var hijos = elemento.childNodes;
            var codigoOperacion = null;
            var listaPreparadores = new Array();
            var listaErrores = new Array();
            var fila = new Array();
            var nodoFila;
            var hijosFila;
            var nodoErrores;
            var errores;
            var nodoCampo;
            var j;
            
            inicializarErroresCampos();
            
            for(j=0;hijos!=null && j<hijos.length;j++){
                if(hijos[j].nodeName=="CODIGO_OPERACION"){                            
                    codigoOperacion = hijos[j].childNodes[0].nodeValue;
                    listaPreparadores[j] = codigoOperacion;
                }//if(hijos[j].nodeName=="CODIGO_OPERACION")                      
                else if(hijos[j].nodeName=="FILA"){
                    nodoFila = hijos[j];
                    hijosFila = nodoFila.childNodes;
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
                                fila[<%=FilaPreparadorSolicitudVO.POS_CAMPO_NIF%>] = nodoCampo.childNodes[0].childNodes[0].nodeValue;
                                camposErrores[<%=FilaPreparadorSolicitudVO.POS_CAMPO_NIF%>] = nodoCampo.childNodes[1].childNodes[0].nodeValue;
                            }
                            else{
                                fila[<%=FilaPreparadorSolicitudVO.POS_CAMPO_NIF%>] = '-';
                            }
                        }
                        else if(hijosFila[cont].nodeName=="NOMBRE"){
                            nodoCampo = hijosFila[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[<%=FilaPreparadorSolicitudVO.POS_CAMPO_NOMBREAPEL%>] = nodoCampo.childNodes[0].childNodes[0].nodeValue;
                                camposErrores[<%=FilaPreparadorSolicitudVO.POS_CAMPO_NOMBREAPEL%>] = nodoCampo.childNodes[1].childNodes[0].nodeValue;
                            }
                            else{
                                fila[<%=FilaPreparadorSolicitudVO.POS_CAMPO_NOMBREAPEL%>] = '-';
                            }
                        }
                        else if(hijosFila[cont].nodeName=="FECHA_INICIO"){
                            nodoCampo = hijosFila[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[<%=FilaPreparadorSolicitudVO.POS_CAMPO_FECHA_INICIO%>] = nodoCampo.childNodes[0].childNodes[0].nodeValue;
                                camposErrores[<%=FilaPreparadorSolicitudVO.POS_CAMPO_FECHA_INICIO%>] = nodoCampo.childNodes[1].childNodes[0].nodeValue;
                            }
                            else{
                                fila[<%=FilaPreparadorSolicitudVO.POS_CAMPO_FECHA_INICIO%>] = '-';
                            }
                        }
                        else if(hijosFila[cont].nodeName=="FECHA_FIN"){
                            nodoCampo = hijosFila[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[<%=FilaPreparadorSolicitudVO.POS_CAMPO_FECHA_FIN%>] = nodoCampo.childNodes[0].childNodes[0].nodeValue;
                                camposErrores[<%=FilaPreparadorSolicitudVO.POS_CAMPO_FECHA_FIN%>] = nodoCampo.childNodes[1].childNodes[0].nodeValue;
                            }
                            else{
                                fila[<%=FilaPreparadorSolicitudVO.POS_CAMPO_FECHA_FIN%>] = '-';
                            }
                        }
                        else if(hijosFila[cont].nodeName=="HORAS_ANUALES"){
                            nodoCampo = hijosFila[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[<%=FilaPreparadorSolicitudVO.POS_CAMPO_HORAS_ANUALES%>] = nodoCampo.childNodes[0].childNodes[0].nodeValue;
                                camposErrores[<%=FilaPreparadorSolicitudVO.POS_CAMPO_HORAS_ANUALES%>] = nodoCampo.childNodes[1].childNodes[0].nodeValue;
                            }
                            else{
                                fila[<%=FilaPreparadorSolicitudVO.POS_CAMPO_HORAS_ANUALES%>] = '-';
                            }
                        }
                        else if(hijosFila[cont].nodeName=="HORAS_CONTRATO"){
                            nodoCampo = hijosFila[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[<%=FilaPreparadorSolicitudVO.POS_CAMPO_HORAS_CONTRATO%>] = nodoCampo.childNodes[0].childNodes[0].nodeValue;
                                camposErrores[<%=FilaPreparadorSolicitudVO.POS_CAMPO_HORAS_CONTRATO%>] = nodoCampo.childNodes[1].childNodes[0].nodeValue;
                            }
                            else{
                                fila[<%=FilaPreparadorSolicitudVO.POS_CAMPO_HORAS_CONTRATO%>] = '-';
                            }
                        }
                        else if(hijosFila[cont].nodeName=="HORAS_DEDICACION_ECA"){
                            nodoCampo = hijosFila[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[<%=FilaPreparadorSolicitudVO.POS_CAMPO_HORAS_DEDICACION_ECA%>] = nodoCampo.childNodes[0].childNodes[0].nodeValue;
                                camposErrores[<%=FilaPreparadorSolicitudVO.POS_CAMPO_HORAS_DEDICACION_ECA%>] = nodoCampo.childNodes[1].childNodes[0].nodeValue;
                            }
                            else{
                                fila[<%=FilaPreparadorSolicitudVO.POS_CAMPO_HORAS_DEDICACION_ECA%>] = '-';
                            }
                        }
                        else if(hijosFila[cont].nodeName=="COSTES_SALARIALES_SS_JOR"){
                            nodoCampo = hijosFila[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[<%=FilaPreparadorSolicitudVO.POS_CAMPO_COSTES_SALARIALES_SS_JOR%>] = nodoCampo.childNodes[0].childNodes[0].nodeValue;
                                camposErrores[<%=FilaPreparadorSolicitudVO.POS_CAMPO_COSTES_SALARIALES_SS_JOR%>] = nodoCampo.childNodes[1].childNodes[0].nodeValue;
                            }
                            else{
                                fila[<%=FilaPreparadorSolicitudVO.POS_CAMPO_COSTES_SALARIALES_SS_JOR%>] = '-';
                            }
                        }
                        else if(hijosFila[cont].nodeName=="COSTES_SALARIALES_SS_POR_JOR"){
                            nodoCampo = hijosFila[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[<%=FilaPreparadorSolicitudVO.POS_CAMPO_COSTES_SALARIALES_SS_POR_JOR%>] = nodoCampo.childNodes[0].childNodes[0].nodeValue;
                                camposErrores[<%=FilaPreparadorSolicitudVO.POS_CAMPO_COSTES_SALARIALES_SS_POR_JOR%>] = nodoCampo.childNodes[1].childNodes[0].nodeValue;
                            }
                            else{
                                fila[<%=FilaPreparadorSolicitudVO.POS_CAMPO_COSTES_SALARIALES_SS_POR_JOR%>] = '-';
                            }
                        }
                        else if(hijosFila[cont].nodeName=="COSTES_SALARIALES_SS_ECA"){
                            nodoCampo = hijosFila[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[<%=FilaPreparadorSolicitudVO.POS_CAMPO_COSTES_SALARIALES_SS_ECA%>] = nodoCampo.childNodes[0].childNodes[0].nodeValue;
                                camposErrores[<%=FilaPreparadorSolicitudVO.POS_CAMPO_COSTES_SALARIALES_SS_ECA%>] = nodoCampo.childNodes[1].childNodes[0].nodeValue;
                            }
                            else{
                                fila[<%=FilaPreparadorSolicitudVO.POS_CAMPO_COSTES_SALARIALES_SS_ECA%>] = '-';
                            }
                        }
                        else if(hijosFila[cont].nodeName=="NUM_SEG_ANTERIORES"){
                            nodoCampo = hijosFila[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[<%=FilaPreparadorSolicitudVO.POS_CAMPO_NUM_SEG_ANTERIORES%>] = nodoCampo.childNodes[0].childNodes[0].nodeValue;
                                camposErrores[<%=FilaPreparadorSolicitudVO.POS_CAMPO_NUM_SEG_ANTERIORES%>] = nodoCampo.childNodes[1].childNodes[0].nodeValue;
                            }
                            else{
                                fila[<%=FilaPreparadorSolicitudVO.POS_CAMPO_NUM_SEG_ANTERIORES%>] = '-';
                            }
                        }
                        else if(hijosFila[cont].nodeName=="IMPORTE"){
                            nodoCampo = hijosFila[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[<%=FilaPreparadorSolicitudVO.POS_CAMPO_IMPORTE%>] = nodoCampo.childNodes[0].childNodes[0].nodeValue;
                                camposErrores[<%=FilaPreparadorSolicitudVO.POS_CAMPO_IMPORTE%>] = nodoCampo.childNodes[1].childNodes[0].nodeValue;
                            }
                            else{
                                fila[<%=FilaPreparadorSolicitudVO.POS_CAMPO_IMPORTE%>] = '-';
                            }
                        }
                        else if(hijosFila[cont].nodeName=="C1H"){
                            nodoCampo = hijosFila[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[<%=FilaPreparadorSolicitudVO.POS_CAMPO_C1H%>] = nodoCampo.childNodes[0].childNodes[0].nodeValue;
                                camposErrores[<%=FilaPreparadorSolicitudVO.POS_CAMPO_C1H%>] = nodoCampo.childNodes[1].childNodes[0].nodeValue;
                            }
                            else{
                                fila[<%=FilaPreparadorSolicitudVO.POS_CAMPO_C1H%>] = '-';
                            }
                        }
                        else if(hijosFila[cont].nodeName=="C1M"){
                            nodoCampo = hijosFila[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[<%=FilaPreparadorSolicitudVO.POS_CAMPO_C1M%>] = nodoCampo.childNodes[0].childNodes[0].nodeValue;
                                camposErrores[<%=FilaPreparadorSolicitudVO.POS_CAMPO_C1M%>] = nodoCampo.childNodes[1].childNodes[0].nodeValue;
                            }
                            else{
                                fila[<%=FilaPreparadorSolicitudVO.POS_CAMPO_C1M%>] = '-';
                            }
                        }
                        else if(hijosFila[cont].nodeName=="C1TOTAL"){
                            nodoCampo = hijosFila[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[<%=FilaPreparadorSolicitudVO.POS_CAMPO_C1TOTAL%>] = nodoCampo.childNodes[0].childNodes[0].nodeValue;
                                camposErrores[<%=FilaPreparadorSolicitudVO.POS_CAMPO_C1TOTAL%>] = nodoCampo.childNodes[1].childNodes[0].nodeValue;
                            }
                            else{
                                fila[<%=FilaPreparadorSolicitudVO.POS_CAMPO_C1TOTAL%>] = '-';
                            }
                        }
                        else if(hijosFila[cont].nodeName=="C2H"){
                            nodoCampo = hijosFila[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[<%=FilaPreparadorSolicitudVO.POS_CAMPO_C2H%>] = nodoCampo.childNodes[0].childNodes[0].nodeValue;
                                camposErrores[<%=FilaPreparadorSolicitudVO.POS_CAMPO_C2H%>] = nodoCampo.childNodes[1].childNodes[0].nodeValue;
                            }
                            else{
                                fila[<%=FilaPreparadorSolicitudVO.POS_CAMPO_C2H%>] = '-';
                            }
                        }
                        else if(hijosFila[cont].nodeName=="C2M"){
                            nodoCampo = hijosFila[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[<%=FilaPreparadorSolicitudVO.POS_CAMPO_C2M%>] = nodoCampo.childNodes[0].childNodes[0].nodeValue;
                                camposErrores[<%=FilaPreparadorSolicitudVO.POS_CAMPO_C2M%>] = nodoCampo.childNodes[1].childNodes[0].nodeValue;
                            }
                            else{
                                fila[<%=FilaPreparadorSolicitudVO.POS_CAMPO_C2M%>] = '-';
                            }
                        }
                        else if(hijosFila[cont].nodeName=="C2TOTAL"){
                            nodoCampo = hijosFila[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[<%=FilaPreparadorSolicitudVO.POS_CAMPO_C2TOTAL%>] = nodoCampo.childNodes[0].childNodes[0].nodeValue;
                                camposErrores[<%=FilaPreparadorSolicitudVO.POS_CAMPO_C2TOTAL%>] = nodoCampo.childNodes[1].childNodes[0].nodeValue;
                            }
                            else{
                                fila[<%=FilaPreparadorSolicitudVO.POS_CAMPO_C2TOTAL%>] = '-';
                            }
                        }
                        else if(hijosFila[cont].nodeName=="C3H"){
                            nodoCampo = hijosFila[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[<%=FilaPreparadorSolicitudVO.POS_CAMPO_C3H%>] = nodoCampo.childNodes[0].childNodes[0].nodeValue;
                                camposErrores[<%=FilaPreparadorSolicitudVO.POS_CAMPO_C3H%>] = nodoCampo.childNodes[1].childNodes[0].nodeValue;
                            }
                            else{
                                fila[<%=FilaPreparadorSolicitudVO.POS_CAMPO_C3H%>] = '-';
                            }
                        }
                        else if(hijosFila[cont].nodeName=="C3M"){
                            nodoCampo = hijosFila[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[<%=FilaPreparadorSolicitudVO.POS_CAMPO_C3M%>] = nodoCampo.childNodes[0].childNodes[0].nodeValue;
                                camposErrores[<%=FilaPreparadorSolicitudVO.POS_CAMPO_C3M%>] = nodoCampo.childNodes[1].childNodes[0].nodeValue;
                            }
                            else{
                                fila[<%=FilaPreparadorSolicitudVO.POS_CAMPO_C3M%>] = '-';
                            }
                        }
                        else if(hijosFila[cont].nodeName=="C3TOTAL"){
                            nodoCampo = hijosFila[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[<%=FilaPreparadorSolicitudVO.POS_CAMPO_C3TOTAL%>] = nodoCampo.childNodes[0].childNodes[0].nodeValue;
                                camposErrores[<%=FilaPreparadorSolicitudVO.POS_CAMPO_C3TOTAL%>] = nodoCampo.childNodes[1].childNodes[0].nodeValue;
                            }
                            else{
                                fila[<%=FilaPreparadorSolicitudVO.POS_CAMPO_C3TOTAL%>] = '-';
                            }
                        }
                        else if(hijosFila[cont].nodeName=="C4H"){
                            nodoCampo = hijosFila[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[<%=FilaPreparadorSolicitudVO.POS_CAMPO_C4H%>] = nodoCampo.childNodes[0].childNodes[0].nodeValue;
                                camposErrores[<%=FilaPreparadorSolicitudVO.POS_CAMPO_C4H%>] = nodoCampo.childNodes[1].childNodes[0].nodeValue;
                            }
                            else{
                                fila[<%=FilaPreparadorSolicitudVO.POS_CAMPO_C4H%>] = '-';
                            }
                        }
                        else if(hijosFila[cont].nodeName=="C4M"){
                            nodoCampo = hijosFila[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[<%=FilaPreparadorSolicitudVO.POS_CAMPO_C4M%>] = nodoCampo.childNodes[0].childNodes[0].nodeValue;
                                camposErrores[<%=FilaPreparadorSolicitudVO.POS_CAMPO_C4M%>] = nodoCampo.childNodes[1].childNodes[0].nodeValue;
                            }
                            else{
                                fila[<%=FilaPreparadorSolicitudVO.POS_CAMPO_C4M%>] = '-';
                            }
                        }
                        else if(hijosFila[cont].nodeName=="C4TOTAL"){
                            nodoCampo = hijosFila[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[<%=FilaPreparadorSolicitudVO.POS_CAMPO_C4TOTAL%>] = nodoCampo.childNodes[0].childNodes[0].nodeValue;
                                camposErrores[<%=FilaPreparadorSolicitudVO.POS_CAMPO_C4TOTAL%>] = nodoCampo.childNodes[1].childNodes[0].nodeValue;
                            }
                            else{
                                fila[<%=FilaPreparadorSolicitudVO.POS_CAMPO_C4TOTAL%>] = '-';
                            }
                        }
                        else if(hijosFila[cont].nodeName=="INSERCIONES"){
                            nodoCampo = hijosFila[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[<%=FilaPreparadorSolicitudVO.POS_CAMPO_INSERCIONES%>] = nodoCampo.childNodes[0].childNodes[0].nodeValue;
                                camposErrores[<%=FilaPreparadorSolicitudVO.POS_CAMPO_INSERCIONES%>] = nodoCampo.childNodes[1].childNodes[0].nodeValue;
                            }
                            else{
                                fila[<%=FilaPreparadorSolicitudVO.POS_CAMPO_INSERCIONES%>] = '-';
                            }
                        }
                        else if(hijosFila[cont].nodeName=="SEGUIMIENTOS_INSERCIONES"){
                            nodoCampo = hijosFila[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[<%=FilaPreparadorSolicitudVO.POS_CAMPO_SEGUIMIENTOS_INSERCIONES%>] = nodoCampo.childNodes[0].childNodes[0].nodeValue;
                                camposErrores[<%=FilaPreparadorSolicitudVO.POS_CAMPO_SEGUIMIENTOS_INSERCIONES%>] = nodoCampo.childNodes[1].childNodes[0].nodeValue;
                            }
                            else{
                                fila[<%=FilaPreparadorSolicitudVO.POS_CAMPO_SEGUIMIENTOS_INSERCIONES%>] = '-';
                            }
                        }
                        else if(hijosFila[cont].nodeName=="COSTES_SALARIALES_SS"){
                            nodoCampo = hijosFila[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[<%=FilaPreparadorSolicitudVO.POS_CAMPO_COSTES_SALARIALES_SS%>] = nodoCampo.childNodes[0].childNodes[0].nodeValue;
                                camposErrores[<%=FilaPreparadorSolicitudVO.POS_CAMPO_COSTES_SALARIALES_SS%>] = nodoCampo.childNodes[1].childNodes[0].nodeValue;
                            }
                            else{
                                fila[<%=FilaPreparadorSolicitudVO.POS_CAMPO_COSTES_SALARIALES_SS%>] = '-';
                            }
                        }
                        else if(hijosFila[cont].nodeName=="IMPORTE_CONCEDIDO"){
                            nodoCampo = hijosFila[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[<%=FilaPreparadorSolicitudVO.POS_CAMPO_IMPORTE_CONCEDIDO%>] = nodoCampo.childNodes[0].childNodes[0].nodeValue;
                                camposErrores[<%=FilaPreparadorSolicitudVO.POS_CAMPO_IMPORTE_CONCEDIDO%>] = nodoCampo.childNodes[1].childNodes[0].nodeValue;
                            }
                            else{
                                fila[<%=FilaPreparadorSolicitudVO.POS_CAMPO_IMPORTE_CONCEDIDO%>] = '-';
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
                        else if(hijosFila[cont].nodeName=="TIPO_SUST"){
                            nodoCampo = hijosFila[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[31] = nodoCampo.childNodes[0].nodeValue;
                            }
                        }
                    }
                    fila[30] = camposErrores;
                    listaPreparadores[j] = fila;
                    fila = new Array();
                    camposErrores = new Array();
                }   
            }//for(j=0;hijos!=null && j<hijos.length;j++)
            
            return listaPreparadores;
    }
    
    function dblClckTablaPreparadoresSolicitud(rowID,tableName){
        pulsarModifPreparadorSolicitudEca();
    }
    
    function mostrarBotonCargarFicheroPreparadores(){
        if(document.getElementById('fichero_preparadores_sol').files.length > 0){
            document.getElementById('btnCargarFicheroPreparadoresSol').disabled = false;
            document.getElementById('btnCargarFicheroPreparadoresSol').className = 'botonMasLargo';
        }else{
            document.getElementById('btnCargarFicheroPreparadoresSol').disabled = true;
            document.getElementById('btnCargarFicheroPreparadoresSol').className = 'botonMasLargoDeshabilitado';
        }
    }
    
    function inicializarErroresCampos(){
        camposErrores = new Array();
        for(var i = 0; i < <%=FilaPreparadorSolicitudVO.NUM_CAMPOS_FILA%>; i++){
            camposErrores[i] = '<%=ConstantesMeLanbide35.FALSO%>';
        }
    }
</script>
<body>
    <div style="width: 100%; float: left; text-align: left; clear: both; height: 100%; overflow-x: hidden; overflow-y: auto;">
        <div id="barraProgresoPreparadoresSolicitud" style="position: absolute; z-index:10; visibility: hidden; top: 30%; left: 30%;">
            <table width="100%" border="0px" cellpadding="0px" cellspacing="0px" border="0px">
                <tr>
                    <td align="center" valign="middle">
                        <table class="contenedorHidepage" cellpadding="0px" cellspacing="0px" border="0px">
                            <tr>
                                <td>
                                    <table width="349px" height="100%">
                                        <tr>
                                            <td colspan="3" style="height:70%;text-align:center;valign:middle;">
                                                <span id="msgImportandoPreparadores">
                                                    <%=meLanbide35I18n.getMensaje(idiomaUsuario, "msg.cargandoDatos")%>
                                                </span>
                                                <span id="msgEliminandoPreparadores">
                                                    <%=meLanbide35I18n.getMensaje(idiomaUsuario, "msg.eliminandoDatos")%>
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
        
        
        
        <div style="clear: both;">
            <div id="listaPreparadores_sol" style="padding: 5px; width:876px; height: 340px; text-align: center; margin:0px;margin-top:0px;" align="center"></div>


            <div style="width: 900px; text-align: right; height: 70px;">
                <div style="float: left; text-align: right; width: 765px;">
                    <%=meLanbide35I18n.getMensaje(idiomaUsuario,"label.general.cargarFichero")%>:&nbsp;
                    <input type="file"  name="fichero_preparadores_sol" id="fichero_preparadores_sol" class="inputTexto" size="57" accept=".xls">
                    <input type="button" id="btnCargarFicheroPreparadoresSol" name="btnCargarFicheroPreparadoresSol" class="botonMasLargo" value="<%=meLanbide35I18n.getMensaje(idiomaUsuario,"btn.general.cargar")%>" onclick="cargarFicheroPreparadoresEca();">
                </div>
                <div style="float: left; text-align: right; width: 135px;">
                    <a href="#" onclick="descargarPlantillaEca('plantilla_preparadores_solicitud.xls');"><%=meLanbide35I18n.getMensaje(idiomaUsuario,"btn.general.descargarPlantilla")%></a>
                </div>
            </div>

            <div class="botonera">
                <input type="button" id="btnNuevoPreparadorSol" name="btnNuevoPreparadorSol" class="botonGeneral" value="<%=meLanbide35I18n.getMensaje(idiomaUsuario, "btn.general.nuevo")%>" onclick="pulsarNuevoPreparadorSolicitudEca();">
                <input type="button" id="btnSustituirPreparadorSol" name="btnSustituirPreparadorSol" class="botonGeneral" value="<%=meLanbide35I18n.getMensaje(idiomaUsuario, "btn.general.sustituir")%>" onclick="pulsarSustituirPreparadorSolicitudEca();">
                <input type="button" id="btnModificarPreparadorSol" name="btnModificarPreparadorSol" class="botonGeneral" value="<%=meLanbide35I18n.getMensaje(idiomaUsuario, "btn.general.modificar")%>" onclick="pulsarModifPreparadorSolicitudEca();">
                <input type="button" id="btnEliminarPreparadorSol" name="btnEliminarPreparadorSol" class="botonGeneral" value="<%=meLanbide35I18n.getMensaje(idiomaUsuario, "btn.general.eliminar")%>" onclick="pulsarEliminarPreparadorSolicitudEca();">
            </div>
        </div>
    </div>
            
            <iframe id="uploadFramePreparadoresSol" name="uploadFramePreparadoresSol" height="0" width="0" 
            frameborder="0" scrolling="yes"></iframe>  
</body>

<script type="text/javascript">
    var tabPreparadores_sol;
    var listaPreparadores_sol = new Array();
    var listaPreparadores_solTabla = new Array();
    var listaPreparadores_solTabla_titulos = new Array();
    var listaPreparadores_solTabla_estilos = new Array();
    var camposErrores = new Array();
    var haySustitutos = false;
    
    <%  		
        FilaPreparadorSolicitudVO act = null;
        
        List<FilaPreparadorSolicitudVO> solList = (List<FilaPreparadorSolicitudVO>)request.getAttribute("listaPreparadoresSolicitud");
        if (solList!= null && solList.size() >0){
            for (int i = 0; i <solList.size(); i++)
            {
                act = solList.get(i);
                
    %>
        listaPreparadores_sol[<%=i%>] = ['<%=act.getId()%>', '<%=act.getNif()%>', '<%=act.getNombreApel()%>', '<%=act.getFechaInicio()%>', 
            '<%=act.getFechaFin()%>', '<%=act.getHorasAnuales()%>', '<%=act.getHorasContrato()%>', '<%=act.getHorasDedicacionECA()%>', '<%=act.getCostesSalarialesSSJor()%>', '<%=act.getCostesSalarialesSSPorJor()%>', 
            '<%=act.getCostesSalarialesSSEca()%>', '<%=act.getNumSegAnteriores()%>', '<%=act.getImporte()%>', '<%=act.getC1h()%>', '<%=act.getC1m()%>', '<%=act.getC1Total()%>', 
            '<%=act.getC2h()%>', '<%=act.getC2m()%>', '<%=act.getC2Total()%>', '<%=act.getC3h()%>', '<%=act.getC3m()%>', '<%=act.getC3Total()%>', 
            '<%=act.getC4h()%>', '<%=act.getC4m()%>', '<%=act.getC4Total()%>', '<%=act.getInserciones()%>', '<%=act.getSeguimientosInserciones()%>', '<%=act.getCostesSalarialesSS()%>', '<%=act.getImporteConcedido()%>'];
        
        
        listaPreparadores_sol[<%=i%>][30] = '<%=act.getSolPreparadorOrigen() != null ? "1" : "0"%>';
        
        listaPreparadores_solTabla[<%=i%>] = ['<%=act.getErrores() != null && act.getErrores().size() > 0 ? "<img src=\""+request.getContextPath()+"/images/extension/melanbide35/alert.png\"/>" : ""%>', '<%=act.getNif()%>', '<%=act.getNombreApel()%>', '<%=act.getFechaInicio()%>', 
            '<%=act.getFechaFin()%>', '<%=act.getHorasAnuales()%>', '<%=act.getHorasContrato()%>', '<%=act.getHorasDedicacionECA()%>', '<%=act.getCostesSalarialesSSJor()%>', '<%=act.getCostesSalarialesSSPorJor()%>', 
            '<%=act.getCostesSalarialesSSEca()%>', '<%=act.getNumSegAnteriores()%>', '<%=act.getImporte()%>', '<%=act.getC1h()%>', '<%=act.getC1m()%>', '<%=act.getC1Total()%>', 
            '<%=act.getC2h()%>', '<%=act.getC2m()%>', '<%=act.getC2Total()%>', '<%=act.getC3h()%>', '<%=act.getC3m()%>', '<%=act.getC3Total()%>', 
            '<%=act.getC4h()%>', '<%=act.getC4m()%>', '<%=act.getC4Total()%>', '<%=act.getInserciones()%>', '<%=act.getSeguimientosInserciones()%>', '<%=act.getCostesSalarialesSS()%>' ,'<%=act.getImporteConcedido()%>'];
        
        
        var arrayErrores = new Array();
        <%
            if(act.getErrores() != null)
            {
                for(int contE = 0; contE < act.getErrores().size(); contE++)
                {
        %>
                    arrayErrores[<%=contE%>] = '<%=act.getErrores().get(contE)%>';
        <%
                }
            }
        %>
            
        var erroresStr = '';
        erroresStr = getListAsText(arrayErrores);
        
        listaPreparadores_solTabla_titulos[<%=i%>] =  [erroresStr, '<%=act.getNif()%>', '<%=act.getNombreApel()%>', '<%=act.getFechaInicio()%>', 
            '<%=act.getFechaFin()%>', '<%=act.getHorasAnuales()%>', '<%=act.getHorasContrato()%>', '<%=act.getHorasDedicacionECA()%>', '<%=act.getCostesSalarialesSSJor()%>', '<%=act.getCostesSalarialesSSPorJor()%>', 
            '<%=act.getCostesSalarialesSSEca()%>', '<%=act.getNumSegAnteriores()%>', '<%=act.getImporte()%>', '<%=act.getC1h()%>', '<%=act.getC1m()%>', '<%=act.getC1Total()%>', 
            '<%=act.getC2h()%>', '<%=act.getC2m()%>', '<%=act.getC2Total()%>', '<%=act.getC3h()%>', '<%=act.getC3m()%>', '<%=act.getC3Total()%>', 
            '<%=act.getC4h()%>', '<%=act.getC4m()%>', '<%=act.getC4Total()%>', '<%=act.getInserciones()%>', '<%=act.getSeguimientosInserciones()%>', '<%=act.getCostesSalarialesSS()%>', '<%=act.getImporteConcedido()%>'];
        
        //listaPreparadores_solTabla_estilos[<%=i%>] = '<%=act.getErrores() != null && act.getErrores().size() > 0 ? "color: red;" : ""%>';
        
        inicializarErroresCampos();
        
        <%
            String errorAct = "";
            for(int posCE = 0; posCE < FilaPreparadorSolicitudVO.NUM_CAMPOS_FILA; posCE++)
            {
        %>
                    camposErrores[<%=posCE%>] = '<%=act.getErrorCampo(posCE)%>';
        <%    
            }
        %>
            var estilosFila = new Array();
            for(var cont = 0; cont < camposErrores.length; cont++){
                if(camposErrores[cont] == '<%=ConstantesMeLanbide35.CIERTO%>'){
                    estilosFila[cont] = 'color: red;';
                }else{
                    estilosFila[cont] = 'color: black;';
                }
            }
            
        <%
            if(act.getErrores() != null && act.getErrores().size() > 0)
            {
        %>
                estilosFila[<%=FilaPreparadorSolicitudVO.POS_CAMPO_NIF%>] = 'color: red;';
                estilosFila[<%=FilaPreparadorSolicitudVO.POS_CAMPO_NOMBREAPEL%>] = 'color: red;';
        <%
            }
        %>    
            
        <%
            if(act.getTipoSust() != null && act.getTipoSust().equals(ConstantesMeLanbide35.TIPOS_SUSTITUTO.SUSTITUTO_SOLICITUD))
            {
        %>
                haySustitutos = true;
                estilosFila[<%=FilaPreparadorSolicitudVO.POS_CAMPO_NIF%>] = estilosFila[<%=FilaPreparadorSolicitudVO.POS_CAMPO_NIF%>]+'padding-left: 20px;';
                estilosFila[<%=FilaPreparadorSolicitudVO.POS_CAMPO_NOMBREAPEL%>] = estilosFila[<%=FilaPreparadorSolicitudVO.POS_CAMPO_NIF%>]+'padding-left: 20px;';
        <%
            }
        %>    
            
            listaPreparadores_solTabla_estilos[<%=i%>] = estilosFila;
    <%
            }// for
        }// if
    %>
        
    tabPreparadores_sol = new FixedColumnTable(document.getElementById('listaPreparadores_sol'), 850, 966, 'listaPreparadores_sol');
    tabPreparadores_sol.addColumna('30','center','<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.solicitud.preparadores.tabla.col0")%>','<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.solicitud.preparadores.tabla.col0.title")%>');
    if(haySustitutos){
        tabPreparadores_sol.addColumna('140','left','<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.solicitud.preparadores.tabla.col1")%>','<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.solicitud.preparadores.tabla.col1.title")%>');
    }else{
        tabPreparadores_sol.addColumna('80','left','<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.solicitud.preparadores.tabla.col1")%>','<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.solicitud.preparadores.tabla.col1.title")%>');
    }
    tabPreparadores_sol.addColumna('143','left','<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.solicitud.preparadores.tabla.col2")%>','<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.solicitud.preparadores.tabla.col2.title")%>');
    tabPreparadores_sol.addColumna('70','left','<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.solicitud.preparadores.tabla.col4")%>','<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.solicitud.preparadores.tabla.col4.title")%>');
    tabPreparadores_sol.addColumna('70','right','<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.solicitud.preparadores.tabla.col5")%>','<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.solicitud.preparadores.tabla.col5.title")%>');
    tabPreparadores_sol.addColumna('70','right','<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.solicitud.preparadores.tabla.col6")%>','<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.solicitud.preparadores.tabla.col6.title")%>');
    tabPreparadores_sol.addColumna('70','right','<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.solicitud.preparadores.tabla.col7")%>','<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.solicitud.preparadores.tabla.col7.title")%>');
    tabPreparadores_sol.addColumna('70','right','<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.solicitud.preparadores.tabla.col8")%>','<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.solicitud.preparadores.tabla.col8.title")%>');
    tabPreparadores_sol.addColumna('90','right','<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.solicitud.preparadores.tabla.col9")%>','<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.solicitud.preparadores.tabla.col9.title")%>');
    tabPreparadores_sol.addColumna('90','right','<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.solicitud.preparadores.tabla.col10")%>','<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.solicitud.preparadores.tabla.col0.title")%>');
    tabPreparadores_sol.addColumna('90','right','<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.solicitud.preparadores.tabla.col11")%>','<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.solicitud.preparadores.tabla.col11.title")%>');
    tabPreparadores_sol.addColumna('50','right','<%=String.format(meLanbide35I18n.getMensaje(idiomaUsuario, "label.solicitud.preparadores.tabla.col12"), anoExpediente)%>','<%=String.format(meLanbide35I18n.getMensaje(idiomaUsuario, "label.solicitud.preparadores.tabla.col12.title"), anoExpediente)%>');
    tabPreparadores_sol.addColumna('70','right','<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.solicitud.preparadores.tabla.col13")%>','<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.solicitud.preparadores.tabla.col13.title")%>');
    tabPreparadores_sol.addColumna('40','right','<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.solicitud.preparadores.tabla.col14")%>','<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.solicitud.preparadores.tabla.col14.title")%>');
    tabPreparadores_sol.addColumna('40','right','<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.solicitud.preparadores.tabla.col15")%>','<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.solicitud.preparadores.tabla.col15.title")%>');
    tabPreparadores_sol.addColumna('50','right','<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.solicitud.preparadores.tabla.col16")%>','<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.solicitud.preparadores.tabla.col16.title")%>');
    tabPreparadores_sol.addColumna('40','right','<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.solicitud.preparadores.tabla.col17")%>','<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.solicitud.preparadores.tabla.col17.title")%>');
    tabPreparadores_sol.addColumna('40','right','<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.solicitud.preparadores.tabla.col18")%>','<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.solicitud.preparadores.tabla.col18.title")%>');
    tabPreparadores_sol.addColumna('50','right','<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.solicitud.preparadores.tabla.col19")%>','<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.solicitud.preparadores.tabla.col19.title")%>');
    tabPreparadores_sol.addColumna('40','right','<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.solicitud.preparadores.tabla.col20")%>','<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.solicitud.preparadores.tabla.col20.title")%>');
    tabPreparadores_sol.addColumna('40','right','<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.solicitud.preparadores.tabla.col21")%>','<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.solicitud.preparadores.tabla.col21.title")%>');
    tabPreparadores_sol.addColumna('50','right','<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.solicitud.preparadores.tabla.col22")%>','<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.solicitud.preparadores.tabla.col22.title")%>');
    tabPreparadores_sol.addColumna('40','right','<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.solicitud.preparadores.tabla.col23")%>','<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.solicitud.preparadores.tabla.col23.title")%>');
    tabPreparadores_sol.addColumna('40','right','<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.solicitud.preparadores.tabla.col24")%>','<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.solicitud.preparadores.tabla.col24.title")%>');
    tabPreparadores_sol.addColumna('50','right','<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.solicitud.preparadores.tabla.col25")%>','<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.solicitud.preparadores.tabla.col25.title")%>');
    tabPreparadores_sol.addColumna('70','right','<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.solicitud.preparadores.tabla.col26")%>','<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.solicitud.preparadores.tabla.col26.title")%>');
    tabPreparadores_sol.addColumna('70','right','<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.solicitud.preparadores.tabla.col27")%>','<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.solicitud.preparadores.tabla.col27.title")%>');
    tabPreparadores_sol.addColumna('80','right','<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.solicitud.preparadores.tabla.col28")%>','<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.solicitud.preparadores.tabla.col28.title")%>');
    tabPreparadores_sol.addColumna('80','right','<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.solicitud.preparadores.tabla.col29")%>','<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.solicitud.preparadores.tabla.col29.title")%>');
    
    tabPreparadores_sol.numColumnasFijas = 3;
    
    //tabPreparadores_sol.lineas=listaPreparadores_solTabla;
    
    for(var cont = 0; cont < listaPreparadores_solTabla.length; cont++){
        tabPreparadores_sol.addFilaConFormato(listaPreparadores_solTabla[cont], listaPreparadores_solTabla_titulos[cont], listaPreparadores_solTabla_estilos[cont])
    }
    
    tabPreparadores_sol.height = '300';
    
    tabPreparadores_sol.altoCabecera = 50;
    
    if(haySustitutos){
        if(navigator.appName.indexOf("Internet Explorer")!=-1){
            tabPreparadores_sol.scrollWidth = 1920;
        }else{
            tabPreparadores_sol.scrollWidth = 2220;
        }
    }else{
        if(navigator.appName.indexOf("Internet Explorer")!=-1){
            tabPreparadores_sol.scrollWidth = 1820;
        }else{
            tabPreparadores_sol.scrollWidth = 2120;
        }
    }
    
    tabPreparadores_sol.dblClkFunction = 'dblClckTablaPreparadoresSolicitud';
    
    tabPreparadores_sol.displayTabla();
    
    tabPreparadores_sol.pack();
    
    
</script>
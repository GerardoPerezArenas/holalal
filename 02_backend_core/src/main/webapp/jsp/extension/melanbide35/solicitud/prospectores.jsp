<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>

<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide35.i18n.MeLanbide35I18n" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide35.vo.solicitud.FilaProspectorSolicitudVO" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide35.util.ConstantesMeLanbide35" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@ page import="es.altia.common.service.config.Config"%>
<%@ page import="es.altia.common.service.config.ConfigServiceHelper"%>
<%@page import="java.util.ArrayList" %>
<%@page import="java.util.List" %>

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
    //Clase para internacionalizar los mensajes de la aplicación.
    MeLanbide35I18n meLanbide35I18n = MeLanbide35I18n.getInstance();
    
    Config m_Config = ConfigServiceHelper.getConfig("common");
    String statusBar = m_Config.getString("JSP.StatusBar");
    String nombreModulo     = request.getParameter("nombreModulo");
    String codOrganizacion  = request.getParameter("codOrganizacionModulo");
    String numExpediente    = request.getParameter("numero");
    
    String nombreTabla = "solicitudes";
%>

<jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
<jsp:setProperty name="descriptor"  property="idi_cod" value="<%=idiomaUsuario%>" />
<jsp:setProperty name="descriptor"  property="apl_cod" value="<%=apl%>" />
<link rel="StyleSheet" media="screen" type="text/css" href="<%=request.getContextPath()%><%=css%>">
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/>
<link rel="stylesheet" type="text/css" href="<c:url value='/css/extension/melanbide35/melanbide35.css'/>">
<link rel="stylesheet" type="text/css" href="<c:url value='/css/table.css'/>">


<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/popup.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/melanbide35/FixedColumnsTable.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/melanbide35/ecaUtils.js"></script>

<script type="text/javascript">
     
    function cargarFicheroProspectoresEca(){
        
        var hayFicheroSeleccionado = false;
        if(document.getElementById('fichero_prospectores_sol').files){
            if(document.getElementById('fichero_prospectores_sol').files[0]){
                hayFicheroSeleccionado = true;
            }
        }else if(document.getElementById('fichero_prospectores_sol').value != ''){
            hayFicheroSeleccionado = true;
        }
        if(hayFicheroSeleccionado){
            var resultado = 1;
            if(listaProspectores_sol != undefined && listaProspectores_sol.length > 0){
                var resultado = jsp_alerta('', '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "msg.preguntaImportarSolicitud")%>');
            }
            if (resultado == 1){
                document.getElementById('msgImportandoProspectores').style.display='inline';
                document.getElementById('msgEliminandoProspectores').style.display='none';
                barraProgresoEca('on', 'barraProgresoProspectoresSolicitud');
                var CONTEXT_PATH = '<%=request.getContextPath()%>';
                var url = CONTEXT_PATH + '/PeticionModuloIntegracion.do';
                var control = new Date();
                var parametros = 'tarea=preparar&modulo=MELANBIDE35&operacion=procesarExcelProspectores&tipo=0&numero=<%=numExpediente%>&control='+control.getTime();
                document.forms[0].action = url+'?'+parametros;
                document.forms[0].enctype = 'multipart/form-data';
                document.forms[0].encoding = 'multipart/form-data';
                document.forms[0].method = 'POST';
                document.forms[0].target = 'uploadFrameProspectoresSol';
                document.forms[0].submit();
            }
            return false;
        }else{
            jsp_alerta("A",'<%=meLanbide35I18n.getMensaje(idiomaUsuario,"msg.ficheroNoSeleccionado")%>');
            return false;
        }
    }
    
    function getListaProspectoresSolicitudEca(){
        var ajax = getXMLHttpRequest();
        var nodos = null;
        var CONTEXT_PATH = '<%=request.getContextPath()%>'
        var url = CONTEXT_PATH + '/PeticionModuloIntegracion.do';
        var parametros = '';
        var control = new Date();
        var listaProspectores = new Array();
        parametros = 'tarea=preparar&modulo=MELANBIDE35&operacion=getListaProspectoresSolicitud&tipo=0&numero=<%=numExpediente%>&control='+control.getTime();
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
            listaProspectores = extraerListadoProspectoresSolicitudEca(nodos);
        }
        catch(err){
            jsp_alerta("A",'<%=meLanbide35I18n.getMensaje(idiomaUsuario,"error.errorProcesarRespuesta")%>');
        }//try-catch
        return listaProspectores;
    }
   
    function recargarTablaProspectoresSolicitudEca(result, actualizarOtrasPestanas){
        
        var fila;
        var haySustitutos = false;
        listaProspectores_sol = new Array();
        listaProspectores_solTabla = new Array();
        listaProspectores_solTabla_titulos = new Array();
        listaProspectores_solTabla_estilos = new Array();

        var listaEstilosFila;
        
        
        for(var i = 1;i< result.length; i++){
            fila = result[i];
            //listaProspectores_sol[i-1] = fila;//no funciona en IE5
            listaProspectores_sol[i-1] = [fila[0],fila[1],fila[2],fila[3],fila[4],fila[5],fila[6],fila[7],fila[8],fila[9],fila[10],fila[11],fila[12],fila[13],fila[14],fila[15],fila[16]];
            listaProspectores_solTabla[i-1] = [fila[15] != undefined && fila[15].length > 0 ? '<img src="<%=request.getContextPath()%>/images/extension/melanbide35/alert.png"/>' : '', fila[1], fila[2], fila[3], fila[4], fila[5], fila[6], fila[7], fila[8], fila[9], fila[10], fila[11], fila[12], fila[13], fila[14]];
            listaProspectores_solTabla_titulos[i-1] = [getListAsText(fila[15]), fila[1], fila[2], fila[3], fila[4], fila[5], fila[6], fila[7], fila[8], fila[9], fila[10], fila[11], fila[12], fila[13], fila[14]];
            if(fila[15] != undefined && fila[15].length > 0){
                listaEstilosFila = new Array();
                camposErroresProspectores_sol = fila[15];
                for(var j = 0; j < camposErroresProspectores_sol.length; j++){
                    if(camposErroresProspectores_sol[j] == '<%=ConstantesMeLanbide35.CIERTO%>'){
                        listaEstilosFila[j] = 'color: red;';
                    }else{
                        listaEstilosFila[j] = 'color: black;';
                    }
                }
                
                listaEstilosFila[<%=FilaProspectorSolicitudVO.POS_CAMPO_NIF%>] = 'color: red;';
                listaEstilosFila[<%=FilaProspectorSolicitudVO.POS_CAMPO_NOMBREAPEL%>] = 'color: red;';
                
                listaProspectores_solTabla_estilos[i-1] = listaEstilosFila;
            }
            
            if(fila[16] != undefined && fila[16] == '1'){
                haySustitutos = true;
                if(fila[14] == undefined || fila[14].length == 0 || listaEstilosFila == undefined || listaEstilosFila == null){
                    listaEstilosFila = new Array();
                }
                var estilo;
                if(listaEstilosFila[<%=FilaProspectorSolicitudVO.POS_CAMPO_NIF%>] != undefined){
                    estilo = listaEstilosFila[<%=FilaProspectorSolicitudVO.POS_CAMPO_NIF%>];
                }else{
                    estilo = '';
                }
                estilo = estilo+'padding-left: 20px;';
                listaEstilosFila[<%=FilaProspectorSolicitudVO.POS_CAMPO_NIF%>] = estilo;
                if(listaEstilosFila[<%=FilaProspectorSolicitudVO.POS_CAMPO_NOMBREAPEL%>] != undefined){
                    estilo = listaEstilosFila[<%=FilaProspectorSolicitudVO.POS_CAMPO_NOMBREAPEL%>];
                }
                estilo = estilo+'padding-left: 20px;';
                listaEstilosFila[<%=FilaProspectorSolicitudVO.POS_CAMPO_NOMBREAPEL%>] = estilo;
                
                listaProspectores_solTabla_estilos[i-1] = listaEstilosFila;
            }
        }
        tabProspectores_sol = new FixedColumnTable(document.getElementById('listaProspectores_sol'), 850, 966, 'listaProspectores_sol');

        tabProspectores_sol.addColumna('30','center','<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.solicitud.prospectores.tabla.col0")%>','<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.solicitud.prospectores.tabla.col0.title")%>');
        //if(haySustitutos){
            tabProspectores_sol.addColumna('140','left','<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.solicitud.prospectores.tabla.col1")%>', '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.solicitud.prospectores.tabla.col1.title")%>');
        //}else{
        //    tabProspectores_sol.addColumna('80','left','<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.solicitud.prospectores.tabla.col1")%>', '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.solicitud.prospectores.tabla.col1.title")%>');
        //}
        tabProspectores_sol.addColumna('250','left','<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.solicitud.prospectores.tabla.col2")%>', '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.solicitud.prospectores.tabla.col2.title")%>');
        tabProspectores_sol.addColumna('80','left','<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.solicitud.prospectores.tabla.col3")%>', '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.solicitud.prospectores.tabla.col3.title")%>');
        tabProspectores_sol.addColumna('80','left','<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.solicitud.prospectores.tabla.col4")%>', '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.solicitud.prospectores.tabla.col4.title")%>');
        tabProspectores_sol.addColumna('70','right','<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.solicitud.prospectores.tabla.col5")%>', '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.solicitud.prospectores.tabla.col5.title")%>');
        tabProspectores_sol.addColumna('70','right','<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.solicitud.prospectores.tabla.col6")%>', '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.solicitud.prospectores.tabla.col6.title")%>');
        tabProspectores_sol.addColumna('70','right','<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.solicitud.prospectores.tabla.col7")%>', '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.solicitud.prospectores.tabla.col7.title")%>');
        tabProspectores_sol.addColumna('100','right','<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.solicitud.prospectores.tabla.col8")%>', '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.solicitud.prospectores.tabla.col8.title")%>');
        tabProspectores_sol.addColumna('100','right','<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.solicitud.prospectores.tabla.col9")%>', '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.solicitud.prospectores.tabla.col9.title")%>');
        tabProspectores_sol.addColumna('100','right','<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.solicitud.prospectores.tabla.col10")%>', '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.solicitud.prospectores.tabla.col10.title")%>');
        tabProspectores_sol.addColumna('70','right','<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.solicitud.prospectores.tabla.col11")%>', '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.solicitud.prospectores.tabla.col11.title")%>');
        tabProspectores_sol.addColumna('80','right','<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.solicitud.prospectores.tabla.col12")%>', '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.solicitud.prospectores.tabla.col12.title")%>');
        tabProspectores_sol.addColumna('80','right','<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.solicitud.prospectores.tabla.col13")%>', '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.solicitud.prospectores.tabla.col13.title")%>');
        tabProspectores_sol.addColumna('80','right','<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.solicitud.prospectores.tabla.col14")%>', '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.solicitud.prospectores.tabla.col14.title")%>');

        tabProspectores_sol.numColumnasFijas = 3;

        //tabProspectores_sol.lineas=listaProspectores_solTabla;
        
        for(var cont = 0; cont < listaProspectores_solTabla.length; cont++){
            tabProspectores_sol.addFilaConFormato(listaProspectores_solTabla[cont], listaProspectores_solTabla_titulos[cont], listaProspectores_solTabla_estilos[cont])
        }

        tabProspectores_sol.height = '300';

        tabProspectores_sol.altoCabecera = 50;

        tabProspectores_sol.dblClkFunction = 'dblClckTablaProspectoresSolicitud';

        tabProspectores_sol.displayTabla();

        tabProspectores_sol.pack();
        
        if(actualizarOtrasPestanas){
            actualizarOtrasPestanasEca(3);
        }
    }
    
    function actualizarTablaProspectoresSolicitudEca(actualizarOtrasPestanas){
        try{
            var result = getListaProspectoresSolicitudEca();
            recargarTablaProspectoresSolicitudEca(result, actualizarOtrasPestanas);
        }catch(err){
        }
        barraProgresoEca('off', 'barraProgresoProspectoresSolicitud');
    }
    
    function pulsarNuevoProspectorSolicitudEca(){
            var control = new Date();
            var result = null;
            if(navigator.appName.indexOf("Internet Explorer")!=-1){
                lanzarPopUpModal('<%=request.getContextPath()%>/PeticionModuloIntegracion.do?tarea=preparar&modulo=MELANBIDE35&operacion=cargarNuevoProspectorSolicitud&tipo=0&numero=<%=numExpediente%>&opcion=nuevo&control='+control.getTime(),500,850,'no','no', function(result){
					if (result != undefined){                    											
						recargarTablaProspectoresSolicitudEca(result,true);
					}
				});

            }else{
                lanzarPopUpModal('<%=request.getContextPath()%>/PeticionModuloIntegracion.do?tarea=preparar&modulo=MELANBIDE35&operacion=cargarNuevoProspectorSolicitud&tipo=0&numero=<%=numExpediente%>&opcion=nuevo&control='+control.getTime(),500,850,'no','no', function(result){
					if (result != undefined){                    											
						recargarTablaProspectoresSolicitudEca(result,true);
					}
				});
            }            
    }
    
    function pulsarSustituirProspectorSolicitudEca(){
        if(tabProspectores_sol.selectedIndex != -1) {
            if(listaProspectores_sol[tabProspectores_sol.selectedIndex][16] == undefined || listaProspectores_sol[tabProspectores_sol.selectedIndex][16] == null || listaProspectores_sol[tabProspectores_sol.selectedIndex][16] != '1')
            {
                var control = new Date();
                var result = null;
                if(navigator.appName.indexOf("Internet Explorer")!=-1){
                    lanzarPopUpModal('<%=request.getContextPath()%>/PeticionModuloIntegracion.do?tarea=preparar&modulo=MELANBIDE35&operacion=cargarSustituirProspectorSolicitud&tipo=0&numero=<%=numExpediente%>&idProsSustituir='+listaProspectores_sol[tabProspectores_sol.selectedIndex][0]+'&control='+control.getTime(),500,850,'no','no', function(result){
					if (result != undefined){                    											
						recargarTablaProspectoresSolicitudEca(result,true);
					}
				});

                }else{
                    lanzarPopUpModal('<%=request.getContextPath()%>/PeticionModuloIntegracion.do?tarea=preparar&modulo=MELANBIDE35&operacion=cargarSustituirProspectorSolicitud&tipo=0&numero=<%=numExpediente%>&idProsSustituir='+listaProspectores_sol[tabProspectores_sol.selectedIndex][0]+'&control='+control.getTime(),500,850,'no','no', function(result){
					if (result != undefined){  
                    				if(result[0] == '0'){                  											
							recargarTablaProspectoresSolicitudEca(result,true);
						}
					}
				});
                }                
            }else{
                jsp_alerta('A', '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "msg.sustitutoSustNoPermitido")%>');
            }
        } 
        else {
            jsp_alerta('A', '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "msg.msjNoSelecFila")%>');
        }
    }
    
    function pulsarModifProspectorSolicitudEca(){
        if(tabProspectores_sol.selectedIndex != -1) {
            var control = new Date();
            var result = null;
            var opcion = '';
            if(document.forms[0].modoConsulta.value == "si"){
                opcion = 'consultar';
            }else{
                opcion = 'modificar';
            }
            if(navigator.appName.indexOf("Internet Explorer")!=-1){
                lanzarPopUpModal('<%=request.getContextPath()%>/PeticionModuloIntegracion.do?tarea=preparar&modulo=MELANBIDE35&operacion=cargarNuevoProspectorSolicitud&tipo=0&numero=<%=numExpediente%>&opcion='+opcion+'&idProsModif='+listaProspectores_sol[tabProspectores_sol.selectedIndex][0]+'&control='+control.getTime(),500,850,'no','no', function(result){
					if (result != undefined){                    											
						recargarTablarecargarTablaProspectoresSolicitudEcaProspectoresSolicitudEca(result,true);
					}
				});		
            }else{
                lanzarPopUpModal('<%=request.getContextPath()%>/PeticionModuloIntegracion.do?tarea=preparar&modulo=MELANBIDE35&operacion=cargarNuevoProspectorSolicitud&tipo=0&numero=<%=numExpediente%>&opcion='+opcion+'&idProsModif='+listaProspectores_sol[tabProspectores_sol.selectedIndex][0]+'&control='+control.getTime(),500,850,'no','no', function(result){
					if (result != undefined){                    											
						recargarTablaProspectoresSolicitudEca(result,true);
					}
				});		

            }
        } 
        else {
            jsp_alerta('A', '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "msg.msjNoSelecFila")%>');
        }
    }
    
    function pulsarEliminarProspectorSolicitudEca(){
        

            if(tabProspectores_sol.selectedIndex != -1) {
                var resultado = jsp_alerta('', '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "msg.preguntaEliminar")%>');
                if (resultado == 1){
                    
                    document.getElementById('msgImportandoProspectores').style.display="none";
                    document.getElementById('msgEliminandoProspectores').style.display="inline";
                    barraProgresoEca('on', 'barraProgresoProspectoresSolicitud');
                    var ajax = getXMLHttpRequest();
                    var nodos = null;
                    var CONTEXT_PATH = '<%=request.getContextPath()%>'
                    var url = CONTEXT_PATH + "/PeticionModuloIntegracion.do";
                    var parametros = "";
                    var control = new Date();
                    var listaProspectores = new Array();
                    parametros = 'tarea=preparar&modulo=MELANBIDE35&operacion=eliminarProspectorSolicitud&tipo=0&numero=<%=numExpediente%>&idPros='+listaProspectores_sol[tabProspectores_sol.selectedIndex][0]+'&control='+control.getTime();
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
                        listaProspectores = extraerListadoProspectoresSolicitudEca(nodos);
                        barraProgresoEca('off', 'barraProgresoProspectoresSolicitud');
                        var codigoOperacion = listaProspectores[0];
                        if(codigoOperacion=="0"){
                            recargarTablaProspectoresSolicitudEca(listaProspectores,true);
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
    
    function extraerListadoProspectoresSolicitudEca(nodos){
            var elemento = nodos[0];
            var hijos = elemento.childNodes;
            var codigoOperacion = null;
            var listaProspectores = new Array();
            var listaErrores = new Array();
            var fila = new Array();
            var nodoFila;
            var hijosFila;
            var nodoErrores;
            var errores;
            var nodoCampo;
            var j;
            for(j=0;hijos!=null && j<hijos.length;j++){
                if(hijos[j].nodeName=="CODIGO_OPERACION"){                            
                    codigoOperacion = hijos[j].childNodes[0].nodeValue;
                    listaProspectores[j] = codigoOperacion;
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
                        else if(hijosFila[cont].nodeName=="TIPO_SUST"){
                            nodoCampo = hijosFila[cont];
                            if(nodoCampo.childNodes.length > 0){
                                fila[17] = nodoCampo.childNodes[0].nodeValue;
                            }
                        }
                    }
                    fila[16] = camposErrores;
                    listaProspectores[j] = fila;
                    fila = new Array();
                    camposErrores = new Array();
                }   
            }//for(j=0;hijos!=null && j<hijos.length;j++)
            
            return listaProspectores;
    }
    
    function dblClckTablaProspectoresSolicitud(rowID,tableName){
        pulsarModifProspectorSolicitudEca();
    }
    
    function mostrarBotonCargarFicheroProspectores(){
        if(document.getElementById('fichero_prospectores_sol').files.length > 0){
            document.getElementById('btnCargarFicheroProspectoresSol').disabled = false;
            document.getElementById('btnCargarFicheroProspectoresSol').className = 'botonMasLargo';
        }else{
            document.getElementById('btnCargarFicheroProspectoresSol').disabled = true;
            document.getElementById('btnCargarFicheroProspectoresSol').className = 'botonMasLargoDeshabilitado';
        }
    }
    
    function inicializarErroresCamposProspectoresSol_Eca(){
        camposErroresProspectores_sol = new Array();
        for(var i = 0; i < <%=FilaProspectorSolicitudVO.NUM_CAMPOS_FILA%>; i++){
            camposErroresProspectores_sol[i] = '<%=ConstantesMeLanbide35.FALSO%>';
        }
    }
</script>
<body>
    <div style="width: 100%; float: left; text-align: left; clear: both; height: 100%; overflow-x: hidden; overflow-y: auto;">
        <div id="barraProgresoProspectoresSolicitud" style="position: absolute; z-index:10; visibility: hidden; top: 30%; left: 30%;">
            <table width="100%" border="0px" cellpadding="0px" cellspacing="0px" border="0px">
                <tr>
                    <td align="center" valign="middle">
                        <table class="contenedorHidepage" cellpadding="0px" cellspacing="0px" border="0px">
                            <tr>
                                <td>
                                    <table width="349px" height="100%">
                                        <tr>
                                            <td colspan="3" style="height:70%;text-align:center;valign:middle;">
                                                <span id="msgImportandoProspectores">
                                                    <%=meLanbide35I18n.getMensaje(idiomaUsuario, "msg.cargandoDatos")%>
                                                </span>
                                                <span id="msgEliminandoProspectores">
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
            <div id="listaProspectores_sol" style="padding: 5px; width:92.1%; height: 340px; text-align: center; margin:0px;margin-top:0px;" align="center"></div>


            <div style="width: 900px; text-align: right; height: 70px;">
                <div style="float: left; text-align: right; width: 765px;">
                    <%=meLanbide35I18n.getMensaje(idiomaUsuario,"label.general.cargarFichero")%>:&nbsp;
                    <input type="file"  name="fichero_prospectores_sol" id="fichero_prospectores_sol" class="inputTexto" size="57" accept=".ods, .xls">
                    <input type="button" id="btnCargarFicheroProspectoresSol" name="btnCargarFicheroProspectoresSol" class="botonMasLargo" value="<%=meLanbide35I18n.getMensaje(idiomaUsuario,"btn.general.cargar")%>" onclick="cargarFicheroProspectoresEca();">
                </div>
                <div style="float: left; text-align: right; width: 135px;">
                    <a href="#" onclick="descargarPlantillaEca('plantilla_prospectores_solicitud.xls');"><%=meLanbide35I18n.getMensaje(idiomaUsuario,"btn.general.descargarPlantilla")%></a>
                </div>
                </div>

            <div class="botonera">
                <input type="button" id="btnNuevoProspectorSol" name="btnNuevoProspectorSol" class="botonGeneral" value="<%=meLanbide35I18n.getMensaje(idiomaUsuario, "btn.general.nuevo")%>" onclick="pulsarNuevoProspectorSolicitudEca();">
                <input type="button" id="btnSustituirProspectorSol" name="btnSustituirProspectorSol" class="botonGeneral" value="<%=meLanbide35I18n.getMensaje(idiomaUsuario, "btn.general.sustituir")%>" onclick="pulsarSustituirProspectorSolicitudEca();">
                <input type="button" id="btnModificarProspectorSol" name="btnModificarProspectorSol" class="botonGeneral" value="<%=meLanbide35I18n.getMensaje(idiomaUsuario, "btn.general.modificar")%>" onclick="pulsarModifProspectorSolicitudEca();">
                <input type="button" id="btnEliminarProspectorSol" name="btnEliminarProspectorSol" class="botonGeneral" value="<%=meLanbide35I18n.getMensaje(idiomaUsuario, "btn.general.eliminar")%>" onclick="pulsarEliminarProspectorSolicitudEca();">
            </div>
        </div>
    </div>
            
            <iframe id="uploadFrameProspectoresSol" name="uploadFrameProspectoresSol" height="0" width="0" 
            frameborder="0" scrolling="yes"></iframe>  
</body>

<script type="text/javascript">
    var tabProspectores_sol;
    var listaProspectores_sol = new Array();
    var listaProspectores_solTabla = new Array();
    var listaProspectores_solTabla_titulos = new Array();
    var listaProspectores_solTabla_estilos = new Array();
    var camposErroresProspectores_sol = new Array();
    var haySustitutos = false;
   
    <%  		
        FilaProspectorSolicitudVO act = null;
        
        List<FilaProspectorSolicitudVO> prosList = (List<FilaProspectorSolicitudVO>)request.getAttribute("listaProspectoresSolicitud");
        if (prosList!= null && prosList.size() >0){
            for (int i = 0; i <prosList.size(); i++)
            {
                act = prosList.get(i);
                
    %>
        listaProspectores_sol[<%=i%>] = ['<%=act.getId()%>', '<%=act.getNif()%>', '<%=act.getNombreApel()%>', '<%=act.getFechaInicio()%>', 
            '<%=act.getFechaFin()%>', '<%=act.getHorasAnuales()%>', '<%=act.getHorasContrato()%>', '<%=act.getHorasDedicacionECA()%>', '<%=act.getCostesSalarialesSSJor()%>', '<%=act.getCostesSalarialesSSPorJor()%>', 
            '<%=act.getCostesSalarialesSSEca()%>', '<%=act.getVisitas()%>', '<%=act.getVisitasImp()%>', '<%=act.getCoste()%>','<%=act.getImporteConcedido()%>'];
        
        listaProspectores_sol[<%=i%>][16] = '<%=act.getSolProspectorOrigen() != null ? "1" : "0"%>';
        
        
        listaProspectores_solTabla[<%=i%>] = ['<%=act.getErrores() != null && act.getErrores().size() > 0 ? "<img src=\""+request.getContextPath()+"/images/extension/melanbide35/alert.png\"/>" : ""%>', '<%=act.getNif()%>', '<%=act.getNombreApel()%>', '<%=act.getFechaInicio()%>', 
            '<%=act.getFechaFin()%>', '<%=act.getHorasAnuales()%>', '<%=act.getHorasContrato()%>', '<%=act.getHorasDedicacionECA()%>', '<%=act.getCostesSalarialesSSJor()%>', '<%=act.getCostesSalarialesSSPorJor()%>', 
            '<%=act.getCostesSalarialesSSEca()%>', '<%=act.getVisitas()%>', '<%=act.getVisitasImp()%>', '<%=act.getCoste()%>','<%=act.getImporteConcedido()%>'];
        
        var arrayErroresPros = new Array();
        <%
            if(act.getErrores() != null)
            {
                for(int contE = 0; contE < act.getErrores().size(); contE++)
                {
        %>
                    arrayErroresPros[<%=contE%>] = '<%=act.getErrores().get(contE)%>';
        <%
                }
            }
        %>
            
        var erroresProsStr = '';
        erroresProsStr = getListAsText(arrayErroresPros);
        
        listaProspectores_solTabla_titulos[<%=i%>] =  [erroresProsStr, '<%=act.getNif()%>', '<%=act.getNombreApel()%>', '<%=act.getFechaInicio()%>', 
            '<%=act.getFechaFin()%>', '<%=act.getHorasAnuales()%>', '<%=act.getHorasContrato()%>', '<%=act.getHorasDedicacionECA()%>', '<%=act.getCostesSalarialesSSJor()%>', '<%=act.getCostesSalarialesSSPorJor()%>', 
            '<%=act.getCostesSalarialesSSEca()%>', '<%=act.getVisitas()%>', '<%=act.getVisitasImp()%>', '<%=act.getCoste()%>','<%=act.getImporteConcedido()%>'];
        
        //listaProspectores_solTabla_estilos[<%=i%>] = '<%=act.getErrores() != null && act.getErrores().size() > 0 ? "color: red;" : ""%>';
        
         
        inicializarErroresCamposProspectoresSol_Eca();
        
        <%
            String errorAct = "";
            for(int posCE = 0; posCE < FilaProspectorSolicitudVO.NUM_CAMPOS_FILA; posCE++)
            {
        %>
                    camposErroresProspectores_sol[<%=posCE%>] = '<%=act.getErrorCampo(posCE)%>';
        <%    
            }
        %>
            var estilosFilaProspectores_sol = new Array();
            for(var cont = 0; cont < camposErroresProspectores_sol.length; cont++){
                if(camposErroresProspectores_sol[cont] == '<%=ConstantesMeLanbide35.CIERTO%>'){
                    estilosFilaProspectores_sol[cont] = 'color: red;';
                }else{
                    estilosFilaProspectores_sol[cont] = 'color: black;';
                }
            }
            
        <%
            if(act.getErrores() != null && act.getErrores().size() > 0)
            {
        %>
            estilosFilaProspectores_sol[<%=FilaProspectorSolicitudVO.POS_CAMPO_NIF%>] = 'color: red;';
            estilosFilaProspectores_sol[<%=FilaProspectorSolicitudVO.POS_CAMPO_NOMBREAPEL%>] = 'color: red;';
        <%
            }
        %>    
            
        <%
            if(act.getTipoSust() != null && act.getTipoSust().equals(ConstantesMeLanbide35.TIPOS_SUSTITUTO.SUSTITUTO_SOLICITUD))
            {
        %>
                haySustitutos = true;
                estilosFilaProspectores_sol[<%=FilaProspectorSolicitudVO.POS_CAMPO_NIF%>] = estilosFilaProspectores_sol[<%=FilaProspectorSolicitudVO.POS_CAMPO_NIF%>]+'padding-left: 20px;';
                estilosFilaProspectores_sol[<%=FilaProspectorSolicitudVO.POS_CAMPO_NOMBREAPEL%>] = estilosFilaProspectores_sol[<%=FilaProspectorSolicitudVO.POS_CAMPO_NIF%>]+'padding-left: 20px;';
        <%
            }
        %>    
        
        listaProspectores_solTabla_estilos[<%=i%>] = estilosFilaProspectores_sol;
    <%
            }// for
        }// if
    %>
    
    tabProspectores_sol = new FixedColumnTable(document.getElementById('listaProspectores_sol'), 850, 966, 'listaProspectores_sol');
    
    tabProspectores_sol.addColumna('30','center','<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.solicitud.prospectores.tabla.col0")%>','<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.solicitud.prospectores.tabla.col0.title")%>');
    //if(haySustitutos){
        tabProspectores_sol.addColumna('140','left','<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.solicitud.prospectores.tabla.col1")%>', '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.solicitud.prospectores.tabla.col1.title")%>');
    //}else{
    //    tabProspectores_sol.addColumna('80','left','<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.solicitud.prospectores.tabla.col1")%>', '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.solicitud.prospectores.tabla.col1.title")%>');
    //}
    tabProspectores_sol.addColumna('250','left','<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.solicitud.prospectores.tabla.col2")%>', '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.solicitud.prospectores.tabla.col2.title")%>');
    tabProspectores_sol.addColumna('80','left','<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.solicitud.prospectores.tabla.col3")%>', '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.solicitud.prospectores.tabla.col3.title")%>');
    tabProspectores_sol.addColumna('80','left','<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.solicitud.prospectores.tabla.col4")%>', '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.solicitud.prospectores.tabla.col4.title")%>');
    tabProspectores_sol.addColumna('70','right','<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.solicitud.prospectores.tabla.col5")%>', '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.solicitud.prospectores.tabla.col5.title")%>');
    tabProspectores_sol.addColumna('70','right','<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.solicitud.prospectores.tabla.col6")%>', '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.solicitud.prospectores.tabla.col6.title")%>');
    tabProspectores_sol.addColumna('70','right','<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.solicitud.prospectores.tabla.col7")%>', '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.solicitud.prospectores.tabla.col7.title")%>');
    tabProspectores_sol.addColumna('100','right','<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.solicitud.prospectores.tabla.col8")%>', '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.solicitud.prospectores.tabla.col8.title")%>');
    tabProspectores_sol.addColumna('100','right','<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.solicitud.prospectores.tabla.col9")%>', '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.solicitud.prospectores.tabla.col9.title")%>');
    tabProspectores_sol.addColumna('100','right','<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.solicitud.prospectores.tabla.col10")%>', '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.solicitud.prospectores.tabla.col10.title")%>');
    tabProspectores_sol.addColumna('70','right','<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.solicitud.prospectores.tabla.col11")%>', '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.solicitud.prospectores.tabla.col11.title")%>');
    tabProspectores_sol.addColumna('80','right','<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.solicitud.prospectores.tabla.col12")%>', '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.solicitud.prospectores.tabla.col12.title")%>');
    tabProspectores_sol.addColumna('80','right','<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.solicitud.prospectores.tabla.col13")%>', '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.solicitud.prospectores.tabla.col13.title")%>');
    tabProspectores_sol.addColumna('80','right','<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.solicitud.prospectores.tabla.col14")%>', '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.solicitud.prospectores.tabla.col14.title")%>');
    tabProspectores_sol.numColumnasFijas = 3;
    
    //tabProspectores_sol.lineas=listaProspectores_solTabla;
    
    for(var cont = 0; cont < listaProspectores_solTabla.length; cont++){
        tabProspectores_sol.addFilaConFormato(listaProspectores_solTabla[cont], listaProspectores_solTabla_titulos[cont], listaProspectores_solTabla_estilos[cont])
    }
    
    tabProspectores_sol.height = '300';
    
    tabProspectores_sol.altoCabecera = 50;
    
    tabProspectores_sol.dblClkFunction = 'dblClckTablaProspectoresSolicitud';
    
    tabProspectores_sol.displayTabla();
    
    tabProspectores_sol.pack();
</script>

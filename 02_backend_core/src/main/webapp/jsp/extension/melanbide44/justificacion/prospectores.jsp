<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>

<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide44.i18n.MeLanbide44I18n" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide44.vo.justificacion.FilaProspectorJustificacionVO" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide44.util.ConstantesMeLanbide44" %>
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
    MeLanbide44I18n meLanbide44I18n = MeLanbide44I18n.getInstance();
    
    Config m_Config = ConfigServiceHelper.getConfig("common");
    String statusBar = m_Config.getString("JSP.StatusBar");
    String nombreModulo     = request.getParameter("nombreModulo");
    String codOrganizacion  = request.getParameter("codOrganizacionModulo");
    String numExpediente    = request.getParameter("numero");
    String [] datosExp = numExpediente!=null ? numExpediente.split("/"):null;
    int ejercicioExpediente = (datosExp!=null && datosExp.length>0 ?  Integer.valueOf(datosExp[0]) : 2019);  // A partir de 2019 son los cambios
 
%>

<jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
<jsp:setProperty name="descriptor"  property="idi_cod" value="<%=idiomaUsuario%>" />
<jsp:setProperty name="descriptor"  property="apl_cod" value="<%=apl%>" />
<link rel="StyleSheet" media="screen" type="text/css" href="<%=request.getContextPath()%><%=css%>">
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/>
<link rel="stylesheet" type="text/css" href="<c:url value='/css/extension/melanbide44/melanbide44.css'/>">
<link rel="stylesheet" type="text/css" href="<c:url value='/css/table.css'/>">


<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/popup.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/melanbide44/FixedColumnsTable.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/melanbide44/ecaUtils.js"></script>

<script type="text/javascript">
    var ejercicio = <%=ejercicioExpediente%>;
    
    function cargarFicheroVisitasProsEca_ANTES(){
        
        var hayFicheroSeleccionado = false;
        if(document.getElementById('fichero_visitas_jus').files){
            if(document.getElementById('fichero_visitas_jus').files[0]){
                hayFicheroSeleccionado = true;
            }
        }else if(document.getElementById('fichero_visitas_jus').value != ''){
            hayFicheroSeleccionado = true;
        }
        if(hayFicheroSeleccionado){       
            var resultado = jsp_alerta('', '<%=meLanbide44I18n.getMensaje(idiomaUsuario, "msg.preguntaImportarJustificacion")%>');
            if (resultado == 1){             
                document.getElementById('msgImportandoProspectoresJus').style.display='inline';
                document.getElementById('msgEliminandoProspectoresJus').style.display='none';
                barraProgresoEca('on', 'barraProgresoProspectoresJustificacion');
                var CONTEXT_PATH = '<%=request.getContextPath()%>';
                var url = CONTEXT_PATH + '/PeticionModuloIntegracion.do';
                var control = new Date();
                var parametros = 'tarea=preparar&modulo=MELANBIDE44&operacion=procesarExcelVisitasPros_Antes&tipo=0&numero=<%=numExpediente%>&control='+control.getTime();
                document.forms[0].action = url+'?'+parametros;
                document.forms[0].enctype = 'multipart/form-data';
                document.forms[0].encoding = 'multipart/form-data';
                document.forms[0].method = 'POST';
                document.forms[0].target = 'uploadFrameSeguimientosPrep';
                document.forms[0].submit();
                document.getElementById('opcionescargavis').style.display = 'none';
                document.getElementById('btnCargarVisitasJus').className = 'botonMasLargo';
                document.getElementById('btnCargarVisitasJus').disabled = false;
            }
            return false;
        }else{
            jsp_alerta("A",'<%=meLanbide44I18n.getMensaje(idiomaUsuario,"msg.ficheroNoSeleccionado")%>');
            return false;
        }
    }
    
    function cargarFicheroVisitasProsEca(){
        
        var hayFicheroSeleccionado = false;
        if(document.getElementById('fichero_visitas_jus').files){
            if(document.getElementById('fichero_visitas_jus').files[0]){
                hayFicheroSeleccionado = true;
            }
        }else if(document.getElementById('fichero_visitas_jus').value != ''){
            hayFicheroSeleccionado = true;
        }
        if(hayFicheroSeleccionado){       
            var resultado = jsp_alerta('', '<%=meLanbide44I18n.getMensaje(idiomaUsuario, "msg.preguntaImportarJustificacion")%>');
            if (resultado == 1){             
                document.getElementById('msgImportandoProspectoresJus').style.display='inline';
                document.getElementById('msgEliminandoProspectoresJus').style.display='none';
                barraProgresoEca('on', 'barraProgresoProspectoresJustificacion');
                var CONTEXT_PATH = '<%=request.getContextPath()%>';
                var url = CONTEXT_PATH + '/PeticionModuloIntegracion.do';
                var control = new Date();
                var parametros = 'tarea=preparar&modulo=MELANBIDE44&operacion=procesarExcelVisitasPros&tipo=0&numero=<%=numExpediente%>&control='+control.getTime();
                document.forms[0].action = url+'?'+parametros;
                document.forms[0].enctype = 'multipart/form-data';
                document.forms[0].encoding = 'multipart/form-data';
                document.forms[0].method = 'POST';
                document.forms[0].target = 'uploadFrameSeguimientosPrep';
                document.forms[0].submit();
                document.getElementById('opcionescargavis').style.display = 'none';
                document.getElementById('btnCargarVisitasJus').className = 'botonMasLargo';
                document.getElementById('btnCargarVisitasJus').disabled = false;
            }
            return false;
        }else{
            jsp_alerta("A",'<%=meLanbide44I18n.getMensaje(idiomaUsuario,"msg.ficheroNoSeleccionado")%>');
            return false;
        }
    }
    
    function getListaProspectoresJustificacionEca(){
        var ajax = getXMLHttpRequest();
        var nodos = null;
        var CONTEXT_PATH = '<%=request.getContextPath()%>'
        var url = CONTEXT_PATH + '/PeticionModuloIntegracion.do';
        var parametros = '';
        var control = new Date();
        var listaProspectores = new Array();
        parametros = 'tarea=preparar&modulo=MELANBIDE44&operacion=getListaProspectoresJustificacion&tipo=0&numero=<%=numExpediente%>&control='+control.getTime();
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
            listaProspectores = extraerListadoProspectoresJustificacionEca(nodos);
        }
        catch(err){
            //alert(err);
        }//try-catch
        return listaProspectores;
    }
    
    function recargarTablaProspectoresJustificacionEca(result, actualizarOtrasPestanas){
        
        var fila;
        var haySustitutos = false;
        listaProspectores_jus = new Array();
        listaProspectores_jusTabla = new Array();    
        listaProspectores_jusTabla_titulos = new Array();
        listaProspectores_jusTabla_estilos = new Array();
        
        var listaEstilosFila;
        
        for(var i = 1;i< result.length; i++){
            fila = result[i];
            if(fila[13] != undefined && fila[13].length > 0){
                listaEstilosFila = new Array();
                camposErroresProspectores_jus = fila[14];
                for(var j = 0; j < camposErroresProspectores_jus.length; j++){
                    if(camposErroresProspectores_jus[j] == '<%=ConstantesMeLanbide44.CIERTO%>'){
                        listaEstilosFila[j] = 'color: red;';
                    }else{
                        listaEstilosFila[j] = 'color: black;';
                    }
                }
                
                listaEstilosFila[<%=FilaProspectorJustificacionVO.POS_CAMPO_NIF%>] = 'color: red;';
                listaEstilosFila[<%=FilaProspectorJustificacionVO.POS_CAMPO_NOMBREAPEL%>] = 'color: red;';
                
                listaProspectores_jusTabla_estilos[i-1] = listaEstilosFila;
            }
            
            if(fila[15] != undefined && fila[15] == '1'){
                haySustitutos = true;
                if(listaEstilosFila == undefined || listaEstilosFila == null){
                    listaEstilosFila = new Array();
                }
                var estilo;
                if(listaEstilosFila[<%=FilaProspectorJustificacionVO.POS_CAMPO_NIF%>] != undefined){
                    estilo = listaEstilosFila[<%=FilaProspectorJustificacionVO.POS_CAMPO_NIF%>];
                }else{
                    estilo = '';
                }
                estilo = estilo+'padding-left: 20px;';
                listaEstilosFila[<%=FilaProspectorJustificacionVO.POS_CAMPO_NIF%>] = estilo;
                if(listaEstilosFila[<%=FilaProspectorJustificacionVO.POS_CAMPO_NOMBREAPEL%>] != undefined){
                    estilo = listaEstilosFila[<%=FilaProspectorJustificacionVO.POS_CAMPO_NOMBREAPEL%>];
                }
                estilo = estilo+'padding-left: 20px;';
                listaEstilosFila[<%=FilaProspectorJustificacionVO.POS_CAMPO_NOMBREAPEL%>] = estilo;
                
                listaProspectores_jusTabla_estilos[i-1] = listaEstilosFila;
            }
            
            //listaProspectores_jus[i-1] = fila;//no funciona en IE9
            listaProspectores_jus[i-1] =[fila[0],fila[1],fila[2],fila[3],fila[4],fila[5],fila[6],fila[7],fila[8],fila[9],fila[10],fila[11],fila[12],fila[13],fila[14],fila[15]];
           
            listaProspectores_jusTabla[i-1] = [fila[13] != undefined && fila[13].length > 0 ? '<img src="<%=request.getContextPath()%>/images/extension/melanbide44/alert.png"/>' : '', fila[1], fila[2], fila[3], fila[4], fila[5], fila[6], fila[7], fila[8], fila[9], fila[10], 
                '<a href=\'javascript:cargarVisitasProspector('+fila[0]+')\' style=\''+((listaEstilosFila!=undefined)?listaEstilosFila[<%=FilaProspectorJustificacionVO.POS_CAMPO_VISITAS%>]:'color:black;')+'\'>'+fila[11]+'</a>', 
                fila[12]];
            listaProspectores_jusTabla_titulos[i-1] = [getListAsText(fila[13]), fila[1], fila[2], fila[3], fila[4], fila[5], fila[6], fila[7], fila[8], fila[9], fila[10], fila[11], fila[12]];
            listaEstilosFila = new Array();
        }
       
        tabProspectores_jus = new FixedColumnTable(document.getElementById('listaProspectores_jus'), 850, 876, 'listaProspectores_jus');
        
        tabProspectores_jus.addColumna('30','left','<%=meLanbide44I18n.getMensaje(idiomaUsuario, "label.justificacion.prospectores.tabla.col0")%>');
        //if(haySustitutos == true){
            tabProspectores_jus.addColumna('140','left','<%=meLanbide44I18n.getMensaje(idiomaUsuario, "label.justificacion.prospectores.tabla.col1")%>');
        //}else{
        //    tabProspectores_jus.addColumna('80','left','<%=meLanbide44I18n.getMensaje(idiomaUsuario, "label.justificacion.prospectores.tabla.col1")%>');
        //}
        tabProspectores_jus.addColumna('250','left','<%=meLanbide44I18n.getMensaje(idiomaUsuario, "label.justificacion.prospectores.tabla.col2")%>');
        tabProspectores_jus.addColumna('80','left','<%=meLanbide44I18n.getMensaje(idiomaUsuario, "label.justificacion.prospectores.tabla.col3")%>');
        tabProspectores_jus.addColumna('80','left','<%=meLanbide44I18n.getMensaje(idiomaUsuario, "label.justificacion.prospectores.tabla.col4")%>');
        tabProspectores_jus.addColumna('100','right','<%=meLanbide44I18n.getMensaje(idiomaUsuario, "label.justificacion.prospectores.tabla.col5")%>');
        tabProspectores_jus.addColumna('70','right','<%=meLanbide44I18n.getMensaje(idiomaUsuario, "label.justificacion.prospectores.tabla.col6")%>');
        tabProspectores_jus.addColumna('100','right','<%=meLanbide44I18n.getMensaje(idiomaUsuario, "label.justificacion.prospectores.tabla.col7")%>');
        tabProspectores_jus.addColumna('130','right','<%=meLanbide44I18n.getMensaje(idiomaUsuario, "label.justificacion.prospectores.tabla.col8")%>');
        tabProspectores_jus.addColumna('130','right','<%=meLanbide44I18n.getMensaje(idiomaUsuario, "label.justificacion.prospectores.tabla.col9")%>');
        tabProspectores_jus.addColumna('150','right','<%=meLanbide44I18n.getMensaje(idiomaUsuario, "label.justificacion.prospectores.tabla.col10")%>');
        if(ejercicio<2019){
            tabProspectores_sol.addColumna('130','right','<%=meLanbide44I18n.getMensaje(idiomaUsuario, "label.solicitud.prospectores.tabla.col11")%>');
        } else {
            tabProspectores_sol.addColumna('130','right','<%=meLanbide44I18n.getMensaje(idiomaUsuario, "label.solicitud.prospectores.tabla.col111")%>');
        }

        tabProspectores_jus.addColumna('100','right','<%=meLanbide44I18n.getMensaje(idiomaUsuario, "label.justificacion.prospectores.tabla.col12")%>');

        tabProspectores_jus.numColumnasFijas = 3;

        for(var cont = 0; cont < listaProspectores_jusTabla.length; cont++){
            tabProspectores_jus.addFilaConFormato(listaProspectores_jusTabla[cont], listaProspectores_jusTabla_titulos[cont], listaProspectores_jusTabla_estilos[cont])
        }

        tabProspectores_jus.height = '300';

        tabProspectores_jus.altoCabecera = 50;

        tabProspectores_jus.dblClkFunction = 'dblClckTablaProspectoresJustificacion';

        tabProspectores_jus.displayTabla();

        tabProspectores_jus.pack();
        
        if(actualizarOtrasPestanas){
            actualizarOtrasPestanasEca(6);
        }
    }
   
    function actualizarTablaProspectoresJustificacionEca(actualizarOtrasPestanas){
        try{
            var result = getListaProspectoresJustificacionEca();
            recargarTablaProspectoresJustificacionEca(result, actualizarOtrasPestanas);
        }catch(err){
        }
        barraProgresoEca('off', 'barraProgresoProspectoresJustificacion');
    }
  
    function pulsarNuevoProspectorJustificacionEca(){
        if(tabProspectores_jus.selectedIndex != -1) {  
            if(listaProspectores_jus[tabProspectores_jus.selectedIndex][15] == undefined || listaProspectores_jus[tabProspectores_jus.selectedIndex][15] == null || listaProspectores_jus[tabProspectores_jus.selectedIndex][15] != '1')
            {  
                var control = new Date();
                var result = null;
               lanzarPopUpModal('<%=request.getContextPath()%>/PeticionModuloIntegracion.do?tarea=preparar&modulo=MELANBIDE44&operacion=cargarNuevoProspectorJustificacion&tipo=0&numero=<%=numExpediente%>&opcion=nuevo&idProsOrigen='+listaProspectores_jus[tabProspectores_jus.selectedIndex][0]+'&control='+control.getTime(),430,850,'no','no', function(result){
               		if (result != undefined){
						if(result[0] == '0'){
							recargarTablaProspectoresJustificacionEca(result, true);
						}
					}
				});
            }else{
                jsp_alerta('A', '<%=meLanbide44I18n.getMensaje(idiomaUsuario, "msg.sustitutoSustNoPermitido")%>');
            }
        }else {
            jsp_alerta('A', '<%=meLanbide44I18n.getMensaje(idiomaUsuario, "msg.msjNoSelecFila")%>');
        }
    }
    
    function pulsarModifProspectorJustificacionEca(){
        if(tabProspectores_jus.selectedIndex != -1) {
            var control = new Date();
            var result = null;
            var opcion = '';
            if(document.forms[0].modoConsulta.value == "si"){
                opcion = 'consultar';
            }else{
                opcion = 'modificar';
            }
            lanzarPopUpModal('<%=request.getContextPath()%>/PeticionModuloIntegracion.do?tarea=preparar&modulo=MELANBIDE44&operacion=cargarNuevoProspectorJustificacion&tipo=0&numero=<%=numExpediente%>&opcion='+opcion+'&idProsModif='+listaProspectores_jus[tabProspectores_jus.selectedIndex][0]+'&control='+control.getTime(),430,820,'no','no', function(result){
				if (result != undefined){
					if(result[0] == '0'){
						recargarTablaProspectoresJustificacionEca(result, true);
					}
				}
			});
        } 
        else {
            jsp_alerta('A', '<%=meLanbide44I18n.getMensaje(idiomaUsuario, "msg.msjNoSelecFila")%>');
        }
    }
    
    function pulsarEliminarProspectorJustificacionEca(){
        if(tabProspectores_jus.selectedIndex != -1) {
            var resultado = 0;
            if(prospectorTieneVisitas(tabProspectores_jus.selectedIndex)){
                resultado = jsp_alerta('', '<%=meLanbide44I18n.getMensaje(idiomaUsuario, "msg.preguntaEliminarProsVis")%>');
            }else{
                resultado = jsp_alerta('', '<%=meLanbide44I18n.getMensaje(idiomaUsuario, "msg.preguntaEliminarPros")%>');
            }
            if (resultado == 1){
                    
                document.getElementById('msgImportandoProspectoresJus').style.display="none";
                document.getElementById('msgEliminandoProspectoresJus').style.display="inline";
                barraProgresoEca('on', 'barraProgresoProspectoresJustificacion');
                var ajax = getXMLHttpRequest();
                var nodos = null;
                var CONTEXT_PATH = '<%=request.getContextPath()%>'
                var url = CONTEXT_PATH + "/PeticionModuloIntegracion.do";
                var parametros = "";
                var control = new Date();
                var listaProspectores = new Array();
                parametros = 'tarea=preparar&modulo=MELANBIDE44&operacion=eliminarProspectorJustificacion&tipo=0&numero=<%=numExpediente%>&idPros='+listaProspectores_jus[tabProspectores_jus.selectedIndex][0]+'&control='+control.getTime();
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
                    listaProspectores = extraerListadoProspectoresJustificacionEca(nodos);
                    recargarTablaProspectoresJustificacionEca(listaProspectores, true);
                    barraProgresoEca('off', 'barraProgresoProspectoresJustificacion');
                    var codigoOperacion = listaProspectores[0];
                    if(codigoOperacion=="0"){
                        jsp_alerta("A",'<%=meLanbide44I18n.getMensaje(idiomaUsuario,"msg.registroEliminadoOK")%>');
                    }else if(codigoOperacion=="1"){
                        jsp_alerta("A",'<%=meLanbide44I18n.getMensaje(idiomaUsuario,"error.errorEliminarBD")%>');
                    }else if(codigoOperacion=="2"){
                        jsp_alerta("A",'<%=meLanbide44I18n.getMensaje(idiomaUsuario,"error.errorEliminarGen")%>');
                    }else if(codigoOperacion=="3"){
                        jsp_alerta("A",'<%=meLanbide44I18n.getMensaje(idiomaUsuario,"error.pasoParametrosEliminar")%>');
                    }else{
                        jsp_alerta("A",'<%=meLanbide44I18n.getMensaje(idiomaUsuario,"error.errorEliminarGen")%>');
                    }//if(
                }
                catch(Err){
                    jsp_alerta("A",'<%=meLanbide44I18n.getMensaje(idiomaUsuario,"error.errorEliminarGen")%>');
                }//try-catch
            }
        } 
        else {
            jsp_alerta('A', '<%=meLanbide44I18n.getMensaje(idiomaUsuario, "msg.msjNoSelecFila")%>');
        }
    }

    function extraerListadoProspectoresJustificacionEca(nodos){
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
            
        return listaProspectores;
    }
 
    function dblClckTablaProspectoresJustificacion(rowID,tableName){
        pulsarModifProspectorJustificacionEca();
    }
    
    <%--function mostrarBotonCargarFicheroProspectores(){
        if(document.getElementById('fichero_prospectores_sol').files.length > 0){
            document.getElementById('btnCargarFicheroProspectoresJus').disabled = false;
            document.getElementById('btnCargarFicheroProspectoresJus').className = 'botonMasLargo';
        }else{
            document.getElementById('btnCargarFicheroProspectoresJus').disabled = true;
            document.getElementById('btnCargarFicheroProspectoresJus').className = 'botonMasLargoDeshabilitado';
        }
    }--%>
        
        function inicializarErroresCamposProsJus(){
            camposErroresProspectores_jus = new Array();
            for(var i = 0; i < <%=FilaProspectorJustificacionVO.NUM_CAMPOS_FILA%>; i++){
                camposErroresProspectores_jus[i] = '<%=ConstantesMeLanbide44.FALSO%>';
            }
        }
    
        function mostrarBotonCargarFicheroVisitasJus(){
            if(document.getElementById('fichero_visitas_jus').files.length > 0){
                document.getElementById('btnCargarFicheroVisitasJus').disabled = false;
                document.getElementById('btnCargarFicheroVisitasJus').className = 'botonMasLargo';
            }else{
                document.getElementById('btnCargarFicheroVisitasJus').disabled = true;
                document.getElementById('btnCargarFicheroVisitasJus').className = 'botonMasLargoDeshabilitado';
            }
        }
    
        function mostrarOpcionesCargaVisita(){      
            if (document.getElementById('btnCargarVisitasJus').disabled){
                document.getElementById('opcionescargavis').style.display = 'none';
                document.getElementById('btnCargarVisitasJus').className = 'botonMasLargo';
                document.getElementById('btnCargarVisitasJus').disabled = false;                
            }else {               
                document.getElementById('opcionescargavis').style.display = 'block';
                document.getElementById('btnCargarVisitasJus').className = 'botonMasLargoDeshabilitado';
                document.getElementById('btnCargarVisitasJus').disabled = true;               
            }
        
        }
    
        function cargarTodasVisitasJus(){        
            var control = new Date();
            var result = null;   
            var opcion = '';
            if(document.forms[0].modoConsulta.value == "si"){
                opcion = 'consultar';
            }else{
                opcion = 'modificar';
            }    
            lanzarPopUpModal('<%=request.getContextPath()%>/PeticionModuloIntegracion.do?tarea=preparar&modulo=MELANBIDE44&operacion=cargarVisitas&tipo=0&numero=<%=numExpediente%>&opcion='+opcion+'&control='+control.getTime(),590,900,'no','no', function(result){
				if(result != undefined && result == true){
					var lista = getListaProspectoresJustificacionEca();
					recargarTablaProspectoresJustificacionEca(lista, true)
				}
			});
        }
    
        function cargarVisitasProspector(idProspector){
            var control = new Date();
            var result = null;
            var opcion = '';
            if(document.forms[0].modoConsulta.value == "si"){
                opcion = 'consultar';
            }else{
                opcion = 'modificar';
            }       
            lanzarPopUpModal('<%=request.getContextPath()%>/PeticionModuloIntegracion.do?tarea=preparar&modulo=MELANBIDE44&operacion=cargarVisitas&tipo=0&numero=<%=numExpediente%>&idProspector='+idProspector+'&opcion='+opcion+'&control='+control.getTime(),450,850,'no','no', function(result){
				if(result != undefined && result == true){
					var lista = getListaProspectoresJustificacionEca();
					recargarTablaProspectoresJustificacionEca(lista, true)
				}
			});
        }
    
        function habilitarBotonVisitas(){
            document.getElementById('btnListaVisitasJus').disabled = false;
        }
    
        function prospectorTieneVisitas(selectedIndex){
            var ajax = getXMLHttpRequest();
            var nodos = null;
            var CONTEXT_PATH = '<%=request.getContextPath()%>'
            var url = CONTEXT_PATH + "/PeticionModuloIntegracion.do";
            var parametros = "";
            var control = new Date();
            var listaPreparadores = new Array();
            parametros = 'tarea=preparar&modulo=MELANBIDE44&operacion=getNumeroVisitasProspector&tipo=0&numero=<%=numExpediente%>&idPros='+listaProspectores_jus[selectedIndex][0]+'&control='+control.getTime();
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
                var listaPreparadores = new Array();
                var visitas = 0;
                var j;
                for(j=0;hijos!=null && j<hijos.length;j++){
                    if(hijos[j].nodeName=="CODIGO_OPERACION"){                            
                        codigoOperacion = hijos[j].childNodes[0].nodeValue;
                        listaPreparadores[j] = codigoOperacion;
                    }//if(hijos[j].nodeName=="CODIGO_OPERACION")                      
                    else if(hijos[j].nodeName=="VISITAS"){
                        visitas = hijos[j].childNodes[0].nodeValue
                    }
                }
                var codigoOperacion = listaPreparadores[0];
                if(codigoOperacion=="0"){
                    var total = parseInt(visitas);
                    if(total > 0){
                        return true;
                    }else{
                        return false;
                    }
                }else if(codigoOperacion=="1"){
                    return false;
                }
            }catch(err){
            
            }
        }

        function pulsarCopiarProspectores(){
            var resultado = 0;
            resultado = jsp_alerta('', '<%=meLanbide44I18n.getMensaje(idiomaUsuario, "msg.preguntaCopiarProspectores")%>');
        
            if (resultado == 1){    
                //alert("copiar");
                    
                document.getElementById('msgCopiandoProspectores').style.display="inline";
                document.getElementById('msgEliminandoProspectoresJus').style.display="none";
                document.getElementById('msgEliminandoProspectoresJus').style.display="none";
                barraProgresoEca('on', 'barraProgresoProspectoresJustificacion');
                var ajax = getXMLHttpRequest();
                var nodos = null;
                var CONTEXT_PATH = '<%=request.getContextPath()%>'
                var url = CONTEXT_PATH + "/PeticionModuloIntegracion.do";
                var parametros = "";
                var control = new Date();
                var listaProspectores = new Array();
                parametros = 'tarea=preparar&modulo=MELANBIDE44&operacion=copiarProspectores&tipo=0&numero=<%=numExpediente%>&control='+control.getTime();
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
                    listaProspectores = extraerListadoProspectoresJustificacionEca(nodos);
                    recargarTablaProspectoresJustificacionEca(listaProspectores, true);
                    barraProgresoEca('off', 'barraProgresoProspectoresJustificacion');
                    var codigoOperacion = listaProspectores[0];
                    if(codigoOperacion=="0"){
                        jsp_alerta("A",'<%=meLanbide44I18n.getMensaje(idiomaUsuario,"msg.registrosCopiadosOK")%>');
                    }else if(codigoOperacion=="1"){
                        jsp_alerta("A",'<%=meLanbide44I18n.getMensaje(idiomaUsuario,"error.errorCopiarBD")%>');
                    }else if(codigoOperacion=="2"){
                        jsp_alerta("A",'<%=meLanbide44I18n.getMensaje(idiomaUsuario,"error.errorCopiarGen")%>');
                    }else if(codigoOperacion=="-1"){
                        jsp_alerta("A",'<%=meLanbide44I18n.getMensaje(idiomaUsuario,"msg.noProspectoresCopiar")%>');
                    }else{
                        jsp_alerta("A",'<%=meLanbide44I18n.getMensaje(idiomaUsuario,"error.errorCopiarGen")%>');
                    }//if(
                }
                catch(Err){
                    jsp_alerta("A",'<%=meLanbide44I18n.getMensaje(idiomaUsuario,"error.errorCopiarGen")%>');
                }//try-catch
            }
            else {
                jsp_alerta('A', '<%=meLanbide44I18n.getMensaje(idiomaUsuario, "msg.msjNoSelecFila")%>');
            }
        }
    
    
</script>

<body>
    <div style="width: 100%; float: left; text-align: left; clear: both; height: 100%; overflow-x: hidden; overflow-y: auto;">
        <div id="barraProgresoProspectoresJustificacion" style="position: absolute; z-index:10; visibility: hidden; top: 30%; left: 30%;">
            <table width="100%" border="0px" cellpadding="0px" cellspacing="0px" border="0px">
                <tr>
                    <td align="center" valign="middle">
                        <table class="contenedorHidepage" cellpadding="0px" cellspacing="0px" border="0px">
                            <tr>
                                <td>
                                    <table width="349px" height="100%">
                                        <tr>
                                            <td colspan="3" style="height:70%;text-align:center;valign:middle;">
                                                <span id="msgCopiandoProspectores">
                                                    <%=meLanbide44I18n.getMensaje(idiomaUsuario, "msg.copiandoDatos")%>
                                                </span>
                                                <span id="msgImportandoProspectoresJus">
                                                    <%=meLanbide44I18n.getMensaje(idiomaUsuario, "msg.cargandoDatos")%>
                                                </span>
                                                <span id="msgEliminandoProspectoresJus">
                                                    <%=meLanbide44I18n.getMensaje(idiomaUsuario, "msg.eliminandoDatos")%>
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
            <input type="button" id="btnCopiarProspectores" name="btnCopiarProspectores" class="botonMasLargo" value="<%=meLanbide44I18n.getMensaje(idiomaUsuario, "btn.justificacion.CopiarProspectores")%>" style="margin:2px" onClick="pulsarCopiarProspectores();"/>

            <div id="listaProspectores_jus" style="padding: 5px; width:96.7%; height: 340px; text-align: center; margin:0px;margin-top:0px;" align="center"></div>
                             
            <div class="botonera">
                <input type="button" id="btnNuevoProspectorJus" name="btnNuevoProspectorJus" class="botonGeneral" value="<%=meLanbide44I18n.getMensaje(idiomaUsuario, "btn.general.sustituir")%>" onclick="pulsarNuevoProspectorJustificacionEca();">
                <input type="button" id="btnModificarProspectorJus" name="btnModificarProspectorJus" class="botonGeneral" value="<%=meLanbide44I18n.getMensaje(idiomaUsuario, "btn.general.modificar")%>" onclick="pulsarModifProspectorJustificacionEca();">
                <input type="button" id="btnEliminarProspectorJus" name="btnEliminarProspectorJus" class="botonGeneral" value="<%=meLanbide44I18n.getMensaje(idiomaUsuario, "btn.general.eliminar")%>" onclick="pulsarEliminarProspectorJustificacionEca();">
            </div>
            
            <div style="width: 20%; float: left; padding-top: 10px;" onmouseover="habilitarBotonesListados();">
                <input type="button" id="btnCargarVisitasJus" name="btnCargarVisitasJus" class="botonMasLargo" value="<%=meLanbide44I18n.getMensaje(idiomaUsuario, "btn.justificacion.cargaVisitas")%>" onclick="mostrarOpcionesCargaVisita();" style="margin:2px">
                <input type="button" id="btnListaVisitasJus" name="btnListaVisitasJus" class="botonMasLargo" value="<%=meLanbide44I18n.getMensaje(idiomaUsuario, "btn.justificacion.listadoVisitas")%>" onclick="cargarTodasVisitasJus();" style="margin:2px">
            </div>
            
            <div style="width: 78%; text-align: right; height: 110px; float: right; padding-top: 10px;">
                <fieldset id="opcionescargavis" style="display: none; width: 100%; height: 100px; padding-top: 24px;">
                    <div style="text-align: right; clear: both; margin-bottom: 10px;">
                        <%=meLanbide44I18n.getMensaje(idiomaUsuario,"label.justificacion.cargarFicheroVisitas")%>:&nbsp;
                        <input type="file"  name="fichero_visitas_jus" id="fichero_visitas_jus" class="inputTexto" size="57" accept=".xls">
                        <a href="#" onclick="descargarPlantillaEca('plantilla_justificacion_vis.xls');"><%=meLanbide44I18n.getMensaje(idiomaUsuario,"btn.general.descargarPlantilla")%></a>
                    </div>
                    <div class="botonera">
                        <input type="button" id="btnCargarFicheroVisitasJus" name="btnCargarFicheroVisitasJus" class="botonMasLargo" value="<%=meLanbide44I18n.getMensaje(idiomaUsuario,"btn.general.cargar")%>" onclick="cargarFicheroVisitasProsEca();"> <!--onclick="cargarFicheroVisitasProsEca_ANTES();-->
                        <!--<input type="button" id="btnCargarFicheroVisitasJus" name="btnCargarFicheroVisitasJus" class="botonMasLargo" value="<%=meLanbide44I18n.getMensaje(idiomaUsuario,"btn.general.cargar")%> NUEVO" onclick="cargarFicheroVisitasProsEca();">-->
                        <input type="button" id="btnCancelarFicheroVisitasJus" name="btnCancelarFicheroVisitasJus" class="botonMasLargo" value="<%=meLanbide44I18n.getMensaje(idiomaUsuario,"btn.general.cancelar")%>" onclick="mostrarOpcionesCargaVisita();" >
                    </div>      

                    <div style="display:none">
                        Esto se debe dejar, porque si no los botones de arriba se van al final de la pantalla
                    </div>
                </fieldset>
            </div> 
        </div>
    </div>

    <iframe id="uploadFrameProspectoresJus" name="uploadFrameProspectoresJus" height="0" width="0" 
            frameborder="0" scrolling="yes"></iframe>  
</body>

<script type="text/javascript">
    var tabProspectores_sol;
    var listaProspectores_jus = new Array();
    var listaProspectores_jusTabla = new Array();
    var listaProspectores_jusTabla_titulos = new Array();    
    var listaProspectores_jusTabla_estilos = new Array();
    var camposErroresProspectores_jus = new Array();
    var haySustitutos = false;
    <%  FilaProspectorJustificacionVO act = null;
        
        List<FilaProspectorJustificacionVO> prosList = (List<FilaProspectorJustificacionVO>)request.getAttribute("listaProspectoresJus");
        if (prosList!= null && prosList.size() >0){
            for (int i = 0; i <prosList.size(); i++)
            {
                act = prosList.get(i);
                
    %>
        listaProspectores_jus[<%=i%>] = ['<%=act.getId()%>', '<%=act.getNif()%>', '<%=act.getNombreApel()%>', '<%=act.getFechaInicio()%>', 
            '<%=act.getFechaFin()%>', '<%=act.getHorasAnuales()%>', '<%=act.getHorasContrato()%>', '<%=act.getHorasDedicacionECA()%>', '<%=act.getCostesSalarialesSSJor()%>', '<%=act.getCostesSalarialesSSPorJor()%>', 
            '<%=act.getCostesSalarialesSSEca()%>', '<%=act.getVisitas()%>', '<%=act.getVisitasImp()%>'];
        
        listaProspectores_jus[<%=i%>][15] = '<%=act.esSustituto() ? "1" : "0"%>';
        
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
        
        listaProspectores_jusTabla_titulos[<%=i%>] =  [erroresStr, '<%=act.getNif()%>', '<%=act.getNombreApel()%>', '<%=act.getFechaInicio()%>', 
            '<%=act.getFechaFin()%>',  '<%=act.getHorasAnuales()%>', '<%=act.getHorasContrato()%>', '<%=act.getHorasDedicacionECA()%>', '<%=act.getCostesSalarialesSSJor()%>', '<%=act.getCostesSalarialesSSPorJor()%>', 
            '<%=act.getCostesSalarialesSSEca()%>', '<%=act.getVisitas()%>', '<%=act.getVisitasImp()%>'];
                      
        inicializarErroresCamposProsJus();
        
         
    <%  String errorAct = "";
            for(int posCE = 0; posCE < FilaProspectorJustificacionVO.NUM_CAMPOS_FILA; posCE++)
            {
    %>
        camposErroresProspectores_jus[<%=posCE%>] = '<%=act.getErrorCampo(posCE)%>';
    <%    
            }
    %>
            
            
        var estilosFila = new Array();
        for(var cont = 0; cont < camposErroresProspectores_jus.length; cont++){
            if(camposErroresProspectores_jus[cont] == '<%=ConstantesMeLanbide44.CIERTO%>'){
                estilosFila[cont] = 'color: red;';
            }else{
                estilosFila[cont] = 'color: black;';
            }
        }   
            
    <%
            if(act.getErrores() != null && act.getErrores().size() > 0)
            {
    %>
        estilosFila[<%=FilaProspectorJustificacionVO.POS_CAMPO_NIF%>] = 'color: red;';
        estilosFila[<%=FilaProspectorJustificacionVO.POS_CAMPO_NOMBREAPEL%>] = 'color: red;';
    <%
            }
    %>    
            
    <%
            if(act.esSustituto() != null && act.esSustituto() == true)
            {
    %>
        haySustitutos = true;
        estilosFila[<%=FilaProspectorJustificacionVO.POS_CAMPO_NIF%>] = estilosFila[<%=FilaProspectorJustificacionVO.POS_CAMPO_NIF%>]+'padding-left: 20px;';
        estilosFila[<%=FilaProspectorJustificacionVO.POS_CAMPO_NOMBREAPEL%>] = estilosFila[<%=FilaProspectorJustificacionVO.POS_CAMPO_NIF%>]+'padding-left: 20px;';
    <%
            }
    %>    
        listaProspectores_jusTabla_estilos[<%=i%>] = estilosFila;
        
        listaProspectores_jusTabla[<%=i%>] = ['<%=act.getErrores() != null && act.getErrores().size() > 0 ? "<img src=\""+request.getContextPath()+"/images/extension/melanbide44/alert.png\" />" : ""%>',
            '<%=act.getNif()%>', '<%=act.getNombreApel()%>', '<%=act.getFechaInicio()%>', 
            '<%=act.getFechaFin()%>', '<%=act.getHorasAnuales()%>', '<%=act.getHorasContrato()%>', '<%=act.getHorasDedicacionECA()%>', '<%=act.getCostesSalarialesSSJor()%>', '<%=act.getCostesSalarialesSSPorJor()%>', 
            '<%=act.getCostesSalarialesSSEca()%>', 
            '<a href=\'javascript:cargarVisitasProspector(<%=act.getId() %>)\' style=\''+estilosFila[<%=FilaProspectorJustificacionVO.POS_CAMPO_VISITAS%>]+'\'><%=act.getVisitas()%></a>', 
            '<%=act.getVisitasImp()%>'];
  
    <%
            }// for
        }// if
    %>
    
        tabProspectores_jus = new FixedColumnTable(document.getElementById('listaProspectores_jus'), 850, 876, 'listaProspectores_jus');
    
        tabProspectores_jus.addColumna('30','left','<%=meLanbide44I18n.getMensaje(idiomaUsuario, "label.justificacion.prospectores.tabla.col0")%>');
        //if(haySustitutos == true){
            tabProspectores_jus.addColumna('140','left','<%=meLanbide44I18n.getMensaje(idiomaUsuario, "label.justificacion.prospectores.tabla.col1")%>');
        //}else{
        //    tabProspectores_jus.addColumna('80','left','<%=meLanbide44I18n.getMensaje(idiomaUsuario, "label.justificacion.prospectores.tabla.col1")%>');
        //}
        tabProspectores_jus.addColumna('250','left','<%=meLanbide44I18n.getMensaje(idiomaUsuario, "label.justificacion.prospectores.tabla.col2")%>');
        tabProspectores_jus.addColumna('80','left','<%=meLanbide44I18n.getMensaje(idiomaUsuario, "label.justificacion.prospectores.tabla.col3")%>');
        tabProspectores_jus.addColumna('80','left','<%=meLanbide44I18n.getMensaje(idiomaUsuario, "label.justificacion.prospectores.tabla.col4")%>');
        tabProspectores_jus.addColumna('100','right','<%=meLanbide44I18n.getMensaje(idiomaUsuario, "label.justificacion.prospectores.tabla.col5")%>');
        tabProspectores_jus.addColumna('70','right','<%=meLanbide44I18n.getMensaje(idiomaUsuario, "label.justificacion.prospectores.tabla.col6")%>');
        tabProspectores_jus.addColumna('100','right','<%=meLanbide44I18n.getMensaje(idiomaUsuario, "label.justificacion.prospectores.tabla.col7")%>');
        tabProspectores_jus.addColumna('130','right','<%=meLanbide44I18n.getMensaje(idiomaUsuario, "label.justificacion.prospectores.tabla.col8")%>');
        tabProspectores_jus.addColumna('130','right','<%=meLanbide44I18n.getMensaje(idiomaUsuario, "label.justificacion.prospectores.tabla.col9")%>');
        tabProspectores_jus.addColumna('150','right','<%=meLanbide44I18n.getMensaje(idiomaUsuario, "label.justificacion.prospectores.tabla.col10")%>');
        if(ejercicio<2019){
            tabProspectores_sol.addColumna('130','right','<%=meLanbide44I18n.getMensaje(idiomaUsuario, "label.solicitud.prospectores.tabla.col11")%>');
        } else {
            tabProspectores_sol.addColumna('130','right','<%=meLanbide44I18n.getMensaje(idiomaUsuario, "label.solicitud.prospectores.tabla.col111")%>');
        }
        tabProspectores_jus.addColumna('100','right','<%=meLanbide44I18n.getMensaje(idiomaUsuario, "label.justificacion.prospectores.tabla.col12")%>');
    
        tabProspectores_jus.numColumnasFijas = 3;
    
        for(var cont = 0; cont < listaProspectores_jusTabla.length; cont++){
            tabProspectores_jus.addFilaConFormato(listaProspectores_jusTabla[cont], listaProspectores_jusTabla_titulos[cont], listaProspectores_jusTabla_estilos[cont])
        }
    
        tabProspectores_jus.height = '300';
    
        tabProspectores_jus.altoCabecera = 50;
    
        tabProspectores_jus.dblClkFunction = 'dblClckTablaProspectoresJustificacion';
    
        tabProspectores_jus.displayTabla();
    
        tabProspectores_jus.pack();
</script>

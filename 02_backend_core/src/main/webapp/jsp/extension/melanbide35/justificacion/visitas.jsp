<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide35.i18n.MeLanbide35I18n" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide35.vo.justificacion.EcaJusProspectoresVO" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide35.vo.justificacion.FilaVisProspectoresVO" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide35.util.ConstantesMeLanbide35" %>
<%@page import="java.text.SimpleDateFormat" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="java.util.List" %>
<html>
    <head>
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
            EcaJusProspectoresVO prospector = (EcaJusProspectoresVO)request.getAttribute("prospector");
            String codOrganizacion  = request.getParameter("codOrganizacionModulo");
            Boolean consulta = request.getAttribute("consulta") != null ? (Boolean)request.getAttribute("consulta") : false;

            String tituloPagina = meLanbide35I18n.getMensaje(idiomaUsuario,"label.justificacion.visitas.tituloPagina");

        %>

        <title><%=tituloPagina%></title>
        
        <jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
<jsp:setProperty name="descriptor"  property="idi_cod" value="<%=idiomaUsuario%>" />
<jsp:setProperty name="descriptor"  property="apl_cod" value="<%=apl%>" />
<link rel="StyleSheet" media="screen" type="text/css" href="<%=request.getContextPath()%><%=css%>">
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/>
        <link rel="stylesheet" type="text/css" href="<c:url value='/css/extension/melanbide35/melanbide35.css'/>">
        <link rel="stylesheet" type="text/css" href="<c:url value='/css/table.css'/>">

        <script type="text/javascript">
            var APP_CONTEXT_PATH = '<%=request.getContextPath()%>';
        </script>
        <script type="text/javascript" src="<c:url value='/scripts/calendario.js'/>"></script>
        <script type="text/javascript" src="<c:url value='/scripts/general.js'/>"></script>
        <script type="text/javascript" src="<c:url value='/scripts/popup.js'/>"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/melanbide35/FixedColumnsTable.js"></script>
        <script type="text/javascript" src="<c:url value='/scripts/validaciones.js'/>"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/melanbide35/ecaUtils.js"></script>

        <script type="text/javascript">
           /* window.onbeforeunload  = function () {
                actualizarDatosProspectores();
            };*/

            var mensajeValidacion = '';

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
                    if(consulta)
                    {
                    %>
                        document.getElementById('btnNuevoVisitaPros').style.display = 'none';
                        document.getElementById('btnModificarVisitaPros').style.display = 'none';
                        document.getElementById('btnEliminarVisitaPros').style.display = 'none';
                        document.getElementById('btnConsultarVisitaPros').style.display = 'inline';
                    <%
                    }
                    else
                    {
                    %>
                        document.getElementById('btnNuevoVisitaPros').style.display = 'inline';
                        document.getElementById('btnModificarVisitaPros').style.display = 'inline';
                        document.getElementById('btnEliminarVisitaPros').style.display = 'inline';
                        document.getElementById('btnConsultarVisitaPros').style.display = 'none';
                    <%
                    }
                    %>

                }catch(err){

                }
            }


            function cerrarVentana(){
              //actualizarDatosProspectores();
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

            function inicializarErroresCamposVisPros(){
                camposErrores = new Array();
                for(var i = 0; i < <%=FilaVisProspectoresVO.NUM_CAMPOS_FILA %>; i++){
                    camposErrores[i] = '<%=ConstantesMeLanbide35.FALSO%>';
                }
            }

            function pulsarNuevaVisitaPros(){
                    var control = new Date();
                    var result = null;
                    if(navigator.appName.indexOf("Internet Explorer")!=-1){
                        lanzarPopUpModal('<%=request.getContextPath()%>/PeticionModuloIntegracion.do?tarea=preparar&modulo=MELANBIDE35&operacion=cargarNuevaVisitaProspector&tipo=0&numero=<%=numExpediente%>&opcion=nuevo&prospector=<%=prospector != null ? prospector.getJusProspectoresCod() : ""%>&control='+control.getTime(),600,850,'no','no', function(result){
					if (result != undefined){
                    				if(result[0] == '0'){								
							self.parent.opener.retornoXanelaAuxiliar(true);
                            				recargarTablaVisProspectores(result);
						}
					}
				});
                    }else{
                        lanzarPopUpModal('<%=request.getContextPath()%>/PeticionModuloIntegracion.do?tarea=preparar&modulo=MELANBIDE35&operacion=cargarNuevaVisitaProspector&tipo=0&numero=<%=numExpediente%>&opcion=nuevo&prospector=<%=prospector != null ? prospector.getJusProspectoresCod() : ""%>&control='+control.getTime(),600,850,'no','no', function(result){
					if (result != undefined){
                    				if(result[0] == '0'){								
							self.parent.opener.retornoXanelaAuxiliar(true);
                            				recargarTablaVisProspectores(result);
						}
					}
				});
                    }
            }
            
            function pulsarModifVisitaPros(){                
                if(tabVisitas_pros.selectedIndex != -1) {
                    var control = new Date();
                    var result = null;
                    var opcion = '';
                    opcion = '<%=consulta != null && consulta == true ? "consultar" : "modificar"%>';
                    if(navigator.appName.indexOf("Internet Explorer")!=-1){
                        lanzarPopUpModal('<%=request.getContextPath()%>/PeticionModuloIntegracion.do?tarea=preparar&modulo=MELANBIDE35&operacion=cargarNuevaVisitaProspector&tipo=0&numero=<%=numExpediente%>&opcion='+opcion+'&preparador=<%=prospector!=null?prospector.getJusProspectoresCod():""  %>&idVisModificar='+listaVisitas_pros[tabVisitas_pros.selectedIndex][0]+'&control='+control.getTime(),600,850,'no','no', function(result){
					if (result != undefined){
                    				if(result[0] == '0'){								
							self.parent.opener.retornoXanelaAuxiliar(true);
                            				recargarTablaVisProspectores(result);
						}
					}
				});
                    }else{
                        lanzarPopUpModal('<%=request.getContextPath()%>/PeticionModuloIntegracion.do?tarea=preparar&modulo=MELANBIDE35&operacion=cargarNuevaVisitaProspector&tipo=0&numero=<%=numExpediente%>&opcion='+opcion+'&preparador=<%=prospector!=null?prospector.getJusProspectoresCod():""  %>&idVisModificar='+listaVisitas_pros[tabVisitas_pros.selectedIndex][0]+'&control='+control.getTime(),600,850,'no','no', function(result){
					if (result != undefined){
                    				if(result[0] == '0'){								
							self.parent.opener.retornoXanelaAuxiliar(true);
                            				recargarTablaVisProspectores(result);
						}
					}
				});
                    }
                } 
                else {
                    jsp_alerta('A', '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "msg.msjNoSelecFila")%>');
                }
            }

            function pulsarEliminarVisitaPros(){
                    if(tabVisitas_pros.selectedIndex != -1) {
                        var resultado = jsp_alerta('', '<%=meLanbide35I18n.getMensaje(idiomaUsuario, "msg.preguntaEliminar")%>');
                        if (resultado == 1){

                            document.getElementById('msgGuardandoDatosVis').style.display="none";
                            document.getElementById('msgEliminandoDatosVis').style.display="inline";
                            barraProgresoEca('on', 'barraProgresoVisProspector');
                            var ajax = getXMLHttpRequest();
                            var nodos = null;
                            var CONTEXT_PATH = '<%=request.getContextPath()%>'
                            var url = CONTEXT_PATH + "/PeticionModuloIntegracion.do";
                            var parametros = "";
                            var control = new Date();
                            var listaVisitas = new Array();
                            parametros = 'tarea=preparar&modulo=MELANBIDE35&operacion=eliminarVisitaProspectorJustificacion&tipo=0&numero=<%=numExpediente%>&idVis='+listaVisitas_pros[tabVisitas_pros.selectedIndex][0]+'&filtrar=<%=prospector != null ? "1" : "0"%>&control='+control.getTime();
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
                                listaVisitas = extraerListadoVisitas(nodos);
                                barraProgresoEca('off', 'barraProgresoVisProspector');
                                var codigoOperacion = listaVisitas[0];
                                if(codigoOperacion=="0"){
                                    self.parent.opener.retornoXanelaAuxiliar(true);
                                    recargarTablaVisProspectores(listaVisitas);
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

            function inicializarErroresCamposVisitas(){
                camposErrores = new Array();
                for(var i = 0; i < <%=FilaVisProspectoresVO.NUM_CAMPOS_FILA %>; i++){
                    camposErrores[i] = '<%=ConstantesMeLanbide35.FALSO%>';
                }
            }


            function extraerListadoVisitas(nodos){
                    var elemento = nodos[0];
                    var hijos = elemento.childNodes;
                    var codigoOperacion = null;
                    var listaVisitas = new Array();
                    var listaErrores = new Array();
                    var fila = new Array();
                    var nodoFila;
                    var hijosFila;
                    var nodoErrores;
                    var errores;
                    var nodoCampo;
                    var j;

                    inicializarErroresCamposVisitas();

                    for(j=0;hijos!=null && j<hijos.length;j++){
                        if(hijos[j].nodeName=="CODIGO_OPERACION"){                            
                            codigoOperacion = hijos[j].childNodes[0].nodeValue;
                            listaVisitas[j] = codigoOperacion;
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
                                else if(hijosFila[cont].nodeName=="CIF"){
                                    nodoCampo = hijosFila[cont];
                                    if(nodoCampo.childNodes.length > 0){
                                        fila[<%=FilaVisProspectoresVO.POS_CAMPO_CIF%>] = nodoCampo.childNodes[0].childNodes[0].nodeValue;
                                        camposErrores[<%=FilaVisProspectoresVO.POS_CAMPO_CIF%>] = nodoCampo.childNodes[1].childNodes[0].nodeValue;
                                    }
                                    else{
                                        fila[<%=FilaVisProspectoresVO.POS_CAMPO_CIF%>] = '-';
                                    }
                                }
                                else if(hijosFila[cont].nodeName=="EMPRESA"){
                                    nodoCampo = hijosFila[cont];
                                    if(nodoCampo.childNodes.length > 0){
                                        fila[<%=FilaVisProspectoresVO.POS_CAMPO_EMPRESA%>] = nodoCampo.childNodes[0].childNodes[0].nodeValue;
                                        camposErrores[<%=FilaVisProspectoresVO.POS_CAMPO_EMPRESA%>] = nodoCampo.childNodes[1].childNodes[0].nodeValue;
                                    }
                                    else{
                                        fila[<%=FilaVisProspectoresVO.POS_CAMPO_EMPRESA%>] = '-';
                                    }
                                }
                                else if(hijosFila[cont].nodeName=="FECHA_VISITA"){
                                    nodoCampo = hijosFila[cont];
                                    if(nodoCampo.childNodes.length > 0){
                                        fila[<%=FilaVisProspectoresVO.POS_CAMPO_FECVISITA%>] = nodoCampo.childNodes[0].childNodes[0].nodeValue;
                                        camposErrores[<%=FilaVisProspectoresVO.POS_CAMPO_FECVISITA%>] = nodoCampo.childNodes[1].childNodes[0].nodeValue;
                                    }
                                    else{
                                        fila[<%=FilaVisProspectoresVO.POS_CAMPO_FECVISITA%>] = '-';
                                    }
                                }
                                else if(hijosFila[cont].nodeName=="SECTOR"){
                                    nodoCampo = hijosFila[cont];
                                    if(nodoCampo.childNodes.length > 0){
                                        fila[<%=FilaVisProspectoresVO.POS_CAMPO_SECTORACT%>] = nodoCampo.childNodes[0].childNodes[0].nodeValue;
                                        camposErrores[<%=FilaVisProspectoresVO.POS_CAMPO_SECTORACT%>] = nodoCampo.childNodes[1].childNodes[0].nodeValue;
                                    }
                                    else{
                                        fila[<%=FilaVisProspectoresVO.POS_CAMPO_SECTORACT%>] = '-';
                                    }
                                }
                                else if(hijosFila[cont].nodeName=="DIRECCION"){
                                    nodoCampo = hijosFila[cont];
                                    if(nodoCampo.childNodes.length > 0){
                                        fila[<%=FilaVisProspectoresVO.POS_CAMPO_DIRECCION%>] = nodoCampo.childNodes[0].childNodes[0].nodeValue;
                                        camposErrores[<%=FilaVisProspectoresVO.POS_CAMPO_DIRECCION%>] = nodoCampo.childNodes[1].childNodes[0].nodeValue;
                                    }
                                    else{
                                        fila[<%=FilaVisProspectoresVO.POS_CAMPO_DIRECCION%>] = '-';
                                    }
                                }
                                else if(hijosFila[cont].nodeName=="CPOSTAL"){
                                    nodoCampo = hijosFila[cont];
                                    if(nodoCampo.childNodes.length > 0){
                                        fila[<%=FilaVisProspectoresVO.POS_CAMPO_CPOSTAL%>] = nodoCampo.childNodes[0].childNodes[0].nodeValue;
                                        camposErrores[<%=FilaVisProspectoresVO.POS_CAMPO_CPOSTAL%>] = nodoCampo.childNodes[1].childNodes[0].nodeValue;
                                    }
                                    else{
                                        fila[<%=FilaVisProspectoresVO.POS_CAMPO_CPOSTAL%>] = '-';
                                    }
                                }
                                else if(hijosFila[cont].nodeName=="LOCALIDAD"){
                                    nodoCampo = hijosFila[cont];
                                    if(nodoCampo.childNodes.length > 0){
                                        fila[<%=FilaVisProspectoresVO.POS_CAMPO_LOCALIDAD%>] = nodoCampo.childNodes[0].childNodes[0].nodeValue;
                                        camposErrores[<%=FilaVisProspectoresVO.POS_CAMPO_LOCALIDAD%>] = nodoCampo.childNodes[1].childNodes[0].nodeValue;
                                    }
                                    else{
                                        fila[<%=FilaVisProspectoresVO.POS_CAMPO_LOCALIDAD%>] = '-';
                                    }
                                }
                                else if(hijosFila[cont].nodeName=="PROVINCIA"){
                                    nodoCampo = hijosFila[cont];
                                    if(nodoCampo.childNodes.length > 0){
                                        fila[<%=FilaVisProspectoresVO.POS_CAMPO_PROVINCIA%>] = nodoCampo.childNodes[0].childNodes[0].nodeValue;
                                        camposErrores[<%=FilaVisProspectoresVO.POS_CAMPO_PROVINCIA%>] = nodoCampo.childNodes[1].childNodes[0].nodeValue;
                                    }
                                    else{
                                        fila[<%=FilaVisProspectoresVO.POS_CAMPO_PROVINCIA%>] = '-';
                                    }
                                }
                                else if(hijosFila[cont].nodeName=="PERS_CONTACTO"){
                                    nodoCampo = hijosFila[cont];
                                    if(nodoCampo.childNodes.length > 0){
                                        fila[<%=FilaVisProspectoresVO.POS_CAMPO_PCONTACTO%>] = nodoCampo.childNodes[0].childNodes[0].nodeValue;
                                        camposErrores[<%=FilaVisProspectoresVO.POS_CAMPO_PCONTACTO%>] = nodoCampo.childNodes[1].childNodes[0].nodeValue;
                                    }
                                    else{
                                        fila[<%=FilaVisProspectoresVO.POS_CAMPO_PCONTACTO%>] = '-';
                                    }
                                }
                                else if(hijosFila[cont].nodeName=="PUESTO"){
                                    nodoCampo = hijosFila[cont];
                                    if(nodoCampo.childNodes.length > 0){
                                        fila[<%=FilaVisProspectoresVO.POS_CAMPO_CARGO%>] = nodoCampo.childNodes[0].childNodes[0].nodeValue;
                                        camposErrores[<%=FilaVisProspectoresVO.POS_CAMPO_CARGO%>] = nodoCampo.childNodes[1].childNodes[0].nodeValue;
                                    }
                                    else{
                                        fila[<%=FilaVisProspectoresVO.POS_CAMPO_CARGO%>] = '-';
                                    }
                                }
                                else if(hijosFila[cont].nodeName=="EMAIL"){
                                    nodoCampo = hijosFila[cont];
                                    if(nodoCampo.childNodes.length > 0){
                                        fila[<%=FilaVisProspectoresVO.POS_CAMPO_EMAIL%>] = nodoCampo.childNodes[0].childNodes[0].nodeValue;
                                        camposErrores[<%=FilaVisProspectoresVO.POS_CAMPO_EMAIL%>] = nodoCampo.childNodes[1].childNodes[0].nodeValue;
                                    }
                                    else{
                                        fila[<%=FilaVisProspectoresVO.POS_CAMPO_EMAIL%>] = '-';
                                    }
                                }
                                else if(hijosFila[cont].nodeName=="TELEFONO"){
                                    nodoCampo = hijosFila[cont];
                                    if(nodoCampo.childNodes.length > 0){
                                        fila[<%=FilaVisProspectoresVO.POS_CAMPO_TELEFONO%>] = nodoCampo.childNodes[0].childNodes[0].nodeValue;
                                        camposErrores[<%=FilaVisProspectoresVO.POS_CAMPO_TELEFONO%>] = nodoCampo.childNodes[1].childNodes[0].nodeValue;
                                    }
                                    else{
                                        fila[<%=FilaVisProspectoresVO.POS_CAMPO_TELEFONO%>] = '-';
                                    }
                                }
                                else if(hijosFila[cont].nodeName=="NIF_PROSPECTOR"){
                                    nodoCampo = hijosFila[cont];
                                    if(nodoCampo.childNodes.length > 0){
                                        fila[<%=FilaVisProspectoresVO.POS_CAMPO_NIFPROSPECTOR%>] = nodoCampo.childNodes[0].childNodes[0].nodeValue;
                                        camposErrores[<%=FilaVisProspectoresVO.POS_CAMPO_NIFPROSPECTOR%>] = nodoCampo.childNodes[1].childNodes[0].nodeValue;
                                    }
                                    else{
                                        fila[<%=FilaVisProspectoresVO.POS_CAMPO_NIFPROSPECTOR%>] = '-';
                                    }
                                }
                                else if(hijosFila[cont].nodeName=="NUMTRAB"){
                                    nodoCampo = hijosFila[cont];
                                    if(nodoCampo.childNodes.length > 0){
                                        fila[<%=FilaVisProspectoresVO.POS_CAMPO_NUMTRAB%>] = nodoCampo.childNodes[0].childNodes[0].nodeValue;
                                        camposErrores[<%=FilaVisProspectoresVO.POS_CAMPO_NUMTRAB%>] = nodoCampo.childNodes[1].childNodes[0].nodeValue;
                                    }
                                    else{
                                        fila[<%=FilaVisProspectoresVO.POS_CAMPO_NUMTRAB%>] = '-';
                                    }
                                }     
                                else if(hijosFila[cont].nodeName=="NUMTRABDISC"){
                                    nodoCampo = hijosFila[cont];
                                    if(nodoCampo.childNodes.length > 0){
                                        fila[<%=FilaVisProspectoresVO.POS_CAMPO_NUMTRABDISC%>] = nodoCampo.childNodes[0].childNodes[0].nodeValue;
                                        camposErrores[<%=FilaVisProspectoresVO.POS_CAMPO_NUMTRABDISC%>] = nodoCampo.childNodes[1].childNodes[0].nodeValue;
                                    }
                                    else{
                                        fila[<%=FilaVisProspectoresVO.POS_CAMPO_NUMTRABDISC%>] = '-';
                                    }
                                }                        
                                else if(hijosFila[cont].nodeName=="CUMPLELISMI"){
                                    nodoCampo = hijosFila[cont];
                                    if(nodoCampo.childNodes.length > 0){
                                        fila[<%=FilaVisProspectoresVO.POS_CAMPO_CUMPLELISMI%>] = nodoCampo.childNodes[0].childNodes[0].nodeValue;
                                        camposErrores[<%=FilaVisProspectoresVO.POS_CAMPO_CUMPLELISMI%>] = nodoCampo.childNodes[1].childNodes[0].nodeValue;
                                    }
                                    else{
                                        fila[<%=FilaVisProspectoresVO.POS_CAMPO_CUMPLELISMI%>] = '-';
                                    }
                                }                       
                                else if(hijosFila[cont].nodeName=="RESULTADO"){
                                    nodoCampo = hijosFila[cont];
                                    if(nodoCampo.childNodes.length > 0){
                                        fila[<%=FilaVisProspectoresVO.POS_CAMPO_RESULTADO%>] = nodoCampo.childNodes[0].childNodes[0].nodeValue;
                                        camposErrores[<%=FilaVisProspectoresVO.POS_CAMPO_RESULTADO%>] = nodoCampo.childNodes[1].childNodes[0].nodeValue;
                                    }
                                    else{
                                        fila[<%=FilaVisProspectoresVO.POS_CAMPO_RESULTADO%>] = '-';
                                    }
                                }                       
                                else if(hijosFila[cont].nodeName=="OBSERVACIONES"){
                                    nodoCampo = hijosFila[cont];
                                    if(nodoCampo.childNodes.length > 0){
                                        fila[<%=FilaVisProspectoresVO.POS_CAMPO_OBSERVACIONES%>] = nodoCampo.childNodes[0].childNodes[0].nodeValue;
                                        camposErrores[<%=FilaVisProspectoresVO.POS_CAMPO_OBSERVACIONES%>] = nodoCampo.childNodes[1].childNodes[0].nodeValue;
                                    }
                                    else{
                                        fila[<%=FilaVisProspectoresVO.POS_CAMPO_OBSERVACIONES%>] = '-';
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
                                    fila[19] = listaErrores;
                                }
                            }
                            fila[20] = camposErrores;
                            listaVisitas[j] = fila;
                            fila = new Array();
                            camposErrores = new Array();
                        }   
                    }//for(j=0;hijos!=null && j<hijos.length;j++)

                    return listaVisitas;
            }
            
            function recargarTablaVisProspectores(result){

           // var camposErrores = new Array();
                var fila;
                listaVisitas_pros = new Array();
                listaVisitas_prosTabla = new Array();        
                listaVisitas_prosTabla_titulos = new Array();
                listaVisitas_prosTabla_estilos = new Array();        
                var listaEstilosFila;

                for(var i = 1;i< result.length; i++){
                    fila = result[i];
                    listaVisitas_pros[i-1] = fila;
                    listaVisitas_prosTabla[i-1] = [fila[19] != undefined && fila[19].length > 0 ? '<img src="<%=request.getContextPath()%>/images/extension/melanbide35/alert.png"/>' : '', fila[1], fila[2], fila[3], fila[4], fila[5], fila[6], fila[7], fila[8], fila[9], fila[10], fila[11], fila[12], fila[13], fila[14], 
                        <%
                        if(prospector == null)
                        {
                        %>
                            fila[15], 
                        <%
                        }
                        %>
                        
                        fila[16], fila[17], fila[18]];
                    listaVisitas_prosTabla_titulos[i-1] = [getListAsText(fila[19]), fila[1], fila[2], fila[3], fila[4],                 
                        fila[5], fila[6], fila[7], fila[8], fila[9], fila[10], fila[11], fila[12], fila[13], fila[14], 
                        <%
                        if(prospector == null)
                        {
                        %>
                            fila[15], 
                        <%
                        }
                        %>
                        
                        fila[16], fila[17], fila[18]];
                    if(fila[19] != undefined && fila[19].length > 0){
                        listaEstilosFila = new Array();
                        camposErrores = fila[20];
                        for(var j = 0; j < camposErrores.length; j++){
                            if(camposErrores[j] == '<%=ConstantesMeLanbide35.CIERTO%>'){
                                listaEstilosFila[j] = 'color: red;';
                            }else{
                                listaEstilosFila[j] = 'color: black;';
                            }
                        }

                        listaEstilosFila[<%=FilaVisProspectoresVO.POS_CAMPO_CIF%>] = 'color: red;';
                        listaEstilosFila[<%=FilaVisProspectoresVO.POS_CAMPO_EMPRESA%>] = 'color: red;';

                        listaVisitas_prosTabla_estilos[i-1] = listaEstilosFila;
                    }
                }


                tabVisitas_pros = new FixedColumnTable(document.getElementById('listaVisitas_pros'), 725, 750, 'listaVisitas_pros');
                tabVisitas_pros.addColumna('30','center','<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.justificacion.visita.tabla.col0")%>','<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.solicitud.preparadores.tabla.col0.title")%>');    
                tabVisitas_pros.addColumna('150','left','<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.justificacion.visita.tabla.col2")%>');
                tabVisitas_pros.addColumna('80','left','<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.justificacion.visita.tabla.col3")%>');
                tabVisitas_pros.addColumna('80','left','<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.justificacion.visita.tabla.col1")%>');
                tabVisitas_pros.addColumna('150','left','<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.justificacion.visita.tabla.col4")%>');
                tabVisitas_pros.addColumna('150','left','<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.justificacion.visita.tabla.col5")%>');
                tabVisitas_pros.addColumna('60','left','<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.justificacion.visita.tabla.col6")%>');
                tabVisitas_pros.addColumna('100','left','<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.justificacion.visita.tabla.col7")%>');
                tabVisitas_pros.addColumna('100','left','<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.justificacion.visita.tabla.col8")%>');
                tabVisitas_pros.addColumna('70','left','<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.justificacion.visita.tabla.col9")%>');
                tabVisitas_pros.addColumna('80','left','<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.justificacion.visita.tabla.col10")%>');
                tabVisitas_pros.addColumna('80','left','<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.justificacion.visita.tabla.col11")%>');
                tabVisitas_pros.addColumna('100','left','<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.justificacion.visita.tabla.col12")%>');
                
                <%
                if(prospector == null)
                {
                %>
                    tabVisitas_pros.addColumna('120','left','<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.justificacion.visita.tabla.col13")%>');
                <%
                }
                %>
                tabVisitas_pros.addColumna('60','left','<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.justificacion.visita.tabla.col14")%>');
                tabVisitas_pros.addColumna('80','right','<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.justificacion.visita.tabla.col15")%>');
                tabVisitas_pros.addColumna('100','left','<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.justificacion.visita.tabla.col16")%>');
                tabVisitas_pros.addColumna('100','left','<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.justificacion.visita.tabla.col17")%>');
                tabVisitas_pros.addColumna('100','left','<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.justificacion.visita.tabla.col18")%>');

                tabVisitas_pros.numColumnasFijas = 3;    

                tabVisitas_pros.height = '300';

                tabVisitas_pros.altoCabecera = 50;

                tabVisitas_pros.dblClkFunction = 'dblClckTablaVisProspector';

                //tabPreparadores_jus.lineas=listaVisitas_prosTabla;

                for(var cont = 0; cont < listaVisitas_prosTabla.length; cont++){
                    tabVisitas_pros.addFilaConFormato(listaVisitas_prosTabla[cont], listaVisitas_prosTabla_titulos[cont], listaVisitas_prosTabla_estilos[cont])
                }

                tabVisitas_pros.displayTabla();

                tabVisitas_pros.pack();
            }

            function dblClckTablaVisProspector(rowID,tableName){
                pulsarModifVisitaPros();
            }

            function actualizarDatosProspectores(){       
                try{
                    var result = window.opener.getListaProspectoresJustificacionEca();
                    window.opener.recargarTablaProspectoresJustificacionEca(result, true);

                }catch(err){
                }

            }  


        </script>
    </head>
    <body onload="inicio();" class="contenidoPantalla etiqueta" style="margin:0" >
        <form>
            <div id="barraProgresoVisProspector" style="position: absolute; z-index:10; visibility: hidden; top: 30%; left: 30%;">
                <table width="100%" border="0px" cellpadding="0px" cellspacing="0px" border="0px">
                    <tr>
                        <td align="center" valign="middle">
                            <table class="contenedorHidepage" cellpadding="0px" cellspacing="0px" border="0px">
                                <tr>
                                    <td>
                                        <table width="349px" height="100%">
                                            <tr>
                                                <td colspan="3" style="height:70%;text-align:center;valign:middle;">
                                                    <span id="msgGuardandoDatosVis">
                                                        <%=meLanbide35I18n.getMensaje(idiomaUsuario, "msg.guardandoDatos")%>
                                                    </span>
                                                     <span id="msgEliminandoDatosVis">
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
            <div style="width: 95%; padding: 10px; text-align: left;">
                <!--<div class="tituloAzul" style="clear: both; text-align: left;">-->
                <div class="sub3titulo" style="clear: both; text-align: left;height:18px;">
                    <span>
                        <%=meLanbide35I18n.getMensaje(idiomaUsuario,"label.justificacion.visita.datosVisita")%>
                    </span>
                </div>
                <div class="lineaFormulario">
                    <div>
                        <%=meLanbide35I18n.getMensaje(idiomaUsuario,"label.justificacion.visita.prospector").toUpperCase()%>:
                        <label id="nif" name="nif"><%=prospector != null && prospector.getNif() != null && !prospector.getNif().equals("") ? prospector.getNif().toUpperCase() + (prospector.getNombre() != null && !prospector.getNombre().equals("") ? " - " : "") : "" %></label><label id="nomApel" name="nomApel"><%=prospector != null && prospector.getNombre() != null ? prospector.getNombre().toUpperCase() : "" %></label>
                    </div>                
                    <div style="clear: both;">
                        <div id="listaVisitas_pros" style="padding: 5px; width:750px; height: 340px; text-align: center; margin:0px;margin-top:0px;" align="center"></div>
                    </div>

                    <div class="botonera">
                        <input type="button" id="btnNuevoVisitaPros" name="btnNuevoVisitaPros" class="botonGeneral" value="<%=meLanbide35I18n.getMensaje(idiomaUsuario, "btn.general.nuevo")%>" onclick="pulsarNuevaVisitaPros();">
                        <input type="button" id="btnModificarVisitaPros" name="btnModificarVisitaPros" class="botonGeneral" value="<%=meLanbide35I18n.getMensaje(idiomaUsuario, "btn.general.modificar")%>" onclick="pulsarModifVisitaPros();" >
                        <input type="button" id="btnEliminarVisitaPros" name="btnEliminarVisitaPros" class="botonGeneral" value="<%=meLanbide35I18n.getMensaje(idiomaUsuario, "btn.general.eliminar")%>" onclick="pulsarEliminarVisitaPros();">
                        <input type="button" id="btnConsultarVisitaPros" name="btnConsultarVisitaPros" class="botonGeneral" value="<%=meLanbide35I18n.getMensaje(idiomaUsuario, "btn.general.consultar")%>" onclick="pulsarModifVisitaPros();">
                        <input type="button" id="btnCerrar" name="btnCerrar" class="botonGeneral" value="<%=meLanbide35I18n.getMensaje(idiomaUsuario, "btn.general.cerrar")%>" onclick="cerrarVentana();">
                    </div>
                </div>
            </div>
        </form>
        <div id="popupcalendar" class="text"></div>

        <script type="text/javascript">

            var tabVisitas_pros;
            var listaVisitas_pros = new Array();
            var listaVisitas_prosTabla = new Array();
            var listaVisitas_prosTabla_titulos = new Array();
            var listaVisitas_prosTabla_estilos = new Array();
            var camposErrores = new Array();

            tabVisitas_pros = new FixedColumnTable(document.getElementById('listaVisitas_pros'), 725, 750, 'listaVisitas_pros');
            tabVisitas_pros.addColumna('30','center','<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.justificacion.visita.tabla.col0")%>','<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.solicitud.preparadores.tabla.col0.title")%>');    
            tabVisitas_pros.addColumna('150','left','<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.justificacion.visita.tabla.col2")%>');
            tabVisitas_pros.addColumna('80','left','<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.justificacion.visita.tabla.col3")%>');
            tabVisitas_pros.addColumna('80','left','<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.justificacion.visita.tabla.col1")%>');
            tabVisitas_pros.addColumna('150','left','<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.justificacion.visita.tabla.col4")%>');
            tabVisitas_pros.addColumna('150','left','<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.justificacion.visita.tabla.col5")%>');
            tabVisitas_pros.addColumna('60','left','<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.justificacion.visita.tabla.col6")%>');
            tabVisitas_pros.addColumna('100','left','<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.justificacion.visita.tabla.col7")%>');
            tabVisitas_pros.addColumna('100','left','<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.justificacion.visita.tabla.col8")%>');
            tabVisitas_pros.addColumna('70','left','<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.justificacion.visita.tabla.col9")%>');
            tabVisitas_pros.addColumna('80','left','<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.justificacion.visita.tabla.col10")%>');
            tabVisitas_pros.addColumna('80','left','<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.justificacion.visita.tabla.col11")%>');
            tabVisitas_pros.addColumna('100','left','<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.justificacion.visita.tabla.col12")%>');
            <%
                if(prospector == null)
                {
            %>
                    tabVisitas_pros.addColumna('120','left','<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.justificacion.visita.tabla.col13")%>');
            <%
                }
            %>
            tabVisitas_pros.addColumna('60','left','<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.justificacion.visita.tabla.col14")%>');
            tabVisitas_pros.addColumna('80','right','<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.justificacion.visita.tabla.col15")%>');
            tabVisitas_pros.addColumna('100','left','<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.justificacion.visita.tabla.col16")%>');
            tabVisitas_pros.addColumna('100','left','<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.justificacion.visita.tabla.col17")%>');
            tabVisitas_pros.addColumna('100','left','<%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.justificacion.visita.tabla.col18")%>');

            tabVisitas_pros.numColumnasFijas = 3;

            <%  		
                FilaVisProspectoresVO act = null;

                List<FilaVisProspectoresVO> visList = (List<FilaVisProspectoresVO>)request.getAttribute("listaVisitas");
                if (visList!= null && visList.size() >0){
                    for (int i = 0; i <visList.size(); i++)
                    {
                        act = visList.get(i);

            %> 
                listaVisitas_pros[<%=i%>] = ['<%=act.getCodProspector()%>_'+'<%=act.getIdvisita()%>', '<%=act.getEmpresa()%>', '<%=act.getCif()%>', '<%=act.getFecVisita()%>', '<%=act.getDescSector()%>',
                    '<%=act.getDireccion()%>','<%=act.getCpostal()%>','<%=act.getLocalidad()%>','<%=act.getDescProvincia()%>','<%=act.getPersContacto()%>',
                    '<%=act.getPuesto()%>', '<%=act.getMail()%>', '<%=act.getTelefono()%>',
                    <%
                        if(prospector == null)
                        {
                    %>
                            '<%=act.getNifProspector()%>', 
                    <%
                        }
                    %>    
                    '<%=act.getNumTrab()%>' , '<%=act.getNumTrabDisc()%>', '<%=act.getDescCumpleLismi()%>', '<%=act.getDescResultadoFinal()%>', '<%=act.getObservaciones()%>'];

                listaVisitas_prosTabla[<%=i%>] = ['<%=act.getErrores() != null && act.getErrores().size() > 0 ? "<img src=\""+request.getContextPath()+"/images/extension/melanbide35/alert.png\" />" : ""%>','<%=act.getEmpresa()%>', '<%=act.getCif()%>', '<%=act.getFecVisita()%>', '<%=act.getDescSector()%>',
                    '<%=act.getDireccion()%>','<%=act.getCpostal()%>','<%=act.getLocalidad()%>','<%=act.getDescProvincia()%>','<%=act.getPersContacto()%>',
                    '<%=act.getPuesto()%>', '<%=act.getMail()%>', '<%=act.getTelefono()%>',
                    <%
                        if(prospector == null)
                        {
                    %>
                            '<%=act.getNifProspector()%>', 
                    <%
                        }
                    %>    
                    '<%=act.getNumTrab()%>' , '<%=act.getNumTrabDisc()%>', '<%=act.getDescCumpleLismi()%>', '<%=act.getDescResultadoFinal()%>', '<%=act.getObservaciones()%>'];

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

               listaVisitas_prosTabla_titulos[<%=i%>] = [erroresStr,'<%=act.getEmpresa()%>', '<%=act.getCif()%>', '<%=act.getFecVisita()%>', '<%=act.getDescSector()%>',
                    '<%=act.getDireccion()%>','<%=act.getCpostal()%>','<%=act.getLocalidad()%>','<%=act.getDescProvincia()%>','<%=act.getPersContacto()%>',
                    '<%=act.getPuesto()%>', '<%=act.getMail()%>', '<%=act.getTelefono()%>',
                    <%
                        if(prospector == null)
                        {
                    %>
                            '<%=act.getNifProspector()%>', 
                    <%
                        }
                    %>
                    '<%=act.getNumTrab()%>' , '<%=act.getNumTrabDisc()%>', '<%=act.getDescCumpleLismi()%>', '<%=act.getDescResultadoFinal()%>', '<%=act.getObservaciones()%>'];

                //inicializarErroresCamposVisPros();

                <%
                    String errorAct = "";
                    for(int posCEp = 0; posCEp < FilaVisProspectoresVO.NUM_CAMPOS_FILA; posCEp++)
                    {
                %>
                         camposErrores[<%=posCEp%>] = '<%=act.getErrorCampo(posCEp)%>';
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
                    estilosFila[<%=FilaVisProspectoresVO.POS_CAMPO_CIF%>] = 'color: red;';
                    estilosFila[<%=FilaVisProspectoresVO.POS_CAMPO_EMPRESA%>] = 'color: red;';
                <%
                    }
                %>    

                    listaVisitas_prosTabla_estilos[<%=i%>] = estilosFila;

            <%
                    }// for
                }// if
            %>

            //tabVisitas_pros.lineas=listaVisitas_prosTabla;
            for(var cont = 0; cont < listaVisitas_prosTabla.length; cont++){
                tabVisitas_pros.addFilaConFormato(listaVisitas_prosTabla[cont], listaVisitas_prosTabla_titulos[cont], listaVisitas_prosTabla_estilos[cont])
            }

            tabVisitas_pros.height = '300';

            tabVisitas_pros.altoCabecera = 50;

            tabVisitas_pros.dblClkFunction = 'dblClckTablaVisProspector';

            tabVisitas_pros.displayTabla();

            tabVisitas_pros.pack();


        </script>
    </body>
</html>

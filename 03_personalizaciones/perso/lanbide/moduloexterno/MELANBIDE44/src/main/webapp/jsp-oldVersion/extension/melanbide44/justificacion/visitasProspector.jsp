<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide44.i18n.MeLanbide44I18n" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide44.vo.justificacion.EcaJusProspectoresVO" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide44.vo.justificacion.FilaVisProspectoresVO" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide44.util.ConstantesMeLanbide44" %>
<%@page import="java.text.SimpleDateFormat" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="java.util.List" %>
<html>
    <head>
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
            EcaJusProspectoresVO prospector = (EcaJusProspectoresVO)request.getAttribute("prospector");
            String codOrganizacion  = request.getParameter("codOrganizacionModulo");

            String tituloPagina = meLanbide44I18n.getMensaje(idiomaUsuario,"label.justificacion.visitas.tituloPagina");

        %>

        <title><%=tituloPagina%></title>
        
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/estilo.css"/>
        <link rel="stylesheet" type="text/css" href="<c:url value='/css/extension/melanbide44/melanbide44.css'/>">
        <link rel="stylesheet" type="text/css" href="<c:url value='/css/table.css'/>">

        <script type="text/javascript">
            var APP_CONTEXT_PATH = '<%=request.getContextPath()%>';
        </script>
        <script type="text/javascript" src="<c:url value='/scripts/calendario.js'/>"></script>
        <script type="text/javascript" src="<c:url value='/scripts/general.js'/>"></script>
        <script type="text/javascript" src="<c:url value='/scripts/popup.js'/>"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/melanbide44/FixedColumnsTable.js"></script>
        <script type="text/javascript" src="<c:url value='/scripts/validaciones.js'/>"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/melanbide44/ecaUtils.js"></script>

        <script type="text/javascript">
            /*window.onbeforeunload  = function () {
                actualizarDatosProspectores();
            };
            */

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
                try{<%

                    if(prospector != null)
                    {%>

                        document.getElementById('nomApel').innerHTML = '<%=prospector.getNombre() != null ? prospector.getNombre().toUpperCase() : "" %>';
                        document.getElementById('nif').innerHTML= '<%=prospector.getNif() != null ? prospector.getNif().toUpperCase() : "" %>';
                     <%
                    }
                    %>    

                }catch(err){

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

            function inicializarErroresCamposVisPros(){
                camposErrores = new Array();
                for(var i = 0; i < <%=FilaVisProspectoresVO.NUM_CAMPOS_FILA %>; i++){
                    camposErrores[i] = '<%=ConstantesMeLanbide44.FALSO%>';
                }
            }

            <%--
            function pulsarNuevoSeguimientoJus(){
                    var control = new Date();
                    var result = null;
                    if(navigator.appName.indexOf("Internet Explorer")!=-1){
                        result = lanzarPopUpModal('<%=request.getContextPath()%>/PeticionModuloIntegracion.do?tarea=preparar&modulo=MELANBIDE44&operacion=cargarNuevoSeguimientoPreparador&tipo=0&numero=<%=numExpediente%>&opcion=nuevo&tiposeg=0&preparador=<%=preparador!=null?preparador.getJusPreparadoresCod():""  %>&colectivo=<%=colectivo%>&sexo=<%=sexo%>&control='+control.getTime(),390,820,'no','no');
                    }else{
                        result = lanzarPopUpModal('<%=request.getContextPath()%>/PeticionModuloIntegracion.do?tarea=preparar&modulo=MELANBIDE44&operacion=cargarNuevoSeguimientoPreparador&tipo=0&numero=<%=numExpediente%>&opcion=nuevo&tiposeg=0&preparador=<%=preparador!=null?preparador.getJusPreparadoresCod():""  %>&colectivo=<%=colectivo%>&sexo=<%=sexo%>&control='+control.getTime(),410,850,'no','no');
                    }
                    if (result != undefined){
                        if(result[0] == '0'){
                            recargarTablaSegPreparadores(result);
                            //this.parent.actualizarTablaPreparadoresJustificacionEca(true);
                            //window.opener.actualizarTablaPreparadoresJustificacionEca(true);

                        }
                    }
            }
            --%>
            function pulsarModifVisitaPros(){                
                if(tabVisitas_pros.selectedIndex != -1) {
                    var control = new Date();
                    var result = null;
                    var opcion = '';
                   /* if(document.forms[0].modoConsulta.value == "si"){
                        opcion = 'consultar';
                    }else{
                   */     opcion = 'modificar';
                   // }
                    if(navigator.appName.indexOf("Internet Explorer")!=-1){
                        result = lanzarPopUpModal('<%=request.getContextPath()%>/PeticionModuloIntegracion.do?tarea=preparar&modulo=MELANBIDE44&operacion=cargarNuevaVisitaProspector&tipo=0&numero=<%=numExpediente%>&opcion='+opcion+'&preparador=<%=prospector!=null?prospector.getJusProspectoresCod():""  %>&idVisModificar='+listaVisitas_pros[tabVisitas_pros.selectedIndex][0]+'&control='+control.getTime(),390,820,'no','no');
                    }else{
                        result = lanzarPopUpModal('<%=request.getContextPath()%>/PeticionModuloIntegracion.do?tarea=preparar&modulo=MELANBIDE44&operacion=cargarNuevaVisitaProspector&tipo=0&numero=<%=numExpediente%>&opcion='+opcion+'&preparador=<%=prospector!=null?prospector.getJusProspectoresCod():"" %>&idVisModificar='+listaVisitas_pros[tabVisitas_pros.selectedIndex][0]+'&control='+control.getTime(),410,850,'no','no');
                    }
                    if (result != undefined){
                        if(result[0] == '0'){
                            recargarTablaVisProspectores(result);                    
                        }
                    }
                } 
                else {
                    jsp_alerta('A', '<%=meLanbide44I18n.getMensaje(idiomaUsuario, "msg.msjNoSelecFila")%>');
                }
            }

           <%-- function pulsarEliminarSegPreparador(){
                    if(tabVisitas_pros.selectedIndex != -1) {
                        var resultado = jsp_alerta('', '<%=meLanbide44I18n.getMensaje(idiomaUsuario, "msg.preguntaEliminar")%>');
                        if (resultado == 1){

                            document.getElementById('msgGuardandoDatos').style.display="none";
                            document.getElementById('msgEliminandoDatos').style.display="inline";
                            barraProgresoSegPrep('on', 'barraProgresoVisProspector');
                            var ajax = getXMLHttpRequest();
                            var nodos = null;
                            var CONTEXT_PATH = '<%=request.getContextPath()%>'
                            var url = CONTEXT_PATH + "/PeticionModuloIntegracion.do";
                            var parametros = "";
                            var control = new Date();
                            var listaVisitas = new Array();
                            parametros = 'tarea=preparar&modulo=MELANBIDE44&operacion=eliminarSegPreparador&tipo=0&numero=<%=numExpediente%>&idSegPrep='+listaVisitas_pros[tabVisitas_pros.selectedIndex][0]+'&control='+control.getTime();
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
                                listaVisitas = extraerListadoSegPreparadores(nodos);
                                recargarTablaSegPreparadores(listaVisitas);
                                barraProgresoSegPrep('off', 'barraProgresoVisProspector');
                                var codigoOperacion = listaVisitas[0];
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
                                jsp_alerta("A",'<%=meLanbide44I18n.getMensaje(idiomaUsuario,"error.errorProcesarRespuesta")%>');
                            }//try-catch
                        }
                    } 
                    else {
                        jsp_alerta('A', '<%=meLanbide44I18n.getMensaje(idiomaUsuario, "msg.msjNoSelecFila")%>');
                    }
            }


            function extraerListadoSegPreparadores(nodos){
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

                    inicializarErroresCamposSegPrep();

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
                                else if(hijosFila[cont].nodeName=="NIF"){
                                    nodoCampo = hijosFila[cont];
                                    if(nodoCampo.childNodes.length > 0){
                                        fila[<%=FilaVisProspectoresVO.POS_CAMPO_NIF%>] = nodoCampo.childNodes[0].childNodes[0].nodeValue;
                                        camposErrores[<%=FilaVisProspectoresVO.POS_CAMPO_NIF%>] = nodoCampo.childNodes[1].childNodes[0].nodeValue;
                                    }
                                    else{
                                        fila[<%=FilaVisProspectoresVO.POS_CAMPO_NIF%>] = '-';
                                    }
                                }
                                else if(hijosFila[cont].nodeName=="NOMBRE"){
                                    nodoCampo = hijosFila[cont];
                                    if(nodoCampo.childNodes.length > 0){
                                        fila[<%=FilaVisProspectoresVO.POS_CAMPO_NOMBREAPEL%>] = nodoCampo.childNodes[0].childNodes[0].nodeValue;
                                        camposErrores[<%=FilaVisProspectoresVO.POS_CAMPO_NOMBREAPEL%>] = nodoCampo.childNodes[1].childNodes[0].nodeValue;
                                    }
                                    else{
                                        fila[<%=FilaVisProspectoresVO.POS_CAMPO_NOMBREAPEL%>] = '-';
                                    }
                                }
                                else if(hijosFila[cont].nodeName=="FECHA_SEGUIMIENTO"){
                                    nodoCampo = hijosFila[cont];
                                    if(nodoCampo.childNodes.length > 0){
                                        fila[<%=FilaVisProspectoresVO.POS_CAMPO_FECHA_SEGUIMIENTO%>] = nodoCampo.childNodes[0].childNodes[0].nodeValue;
                                        camposErrores[<%=FilaVisProspectoresVO.POS_CAMPO_FECHA_SEGUIMIENTO%>] = nodoCampo.childNodes[1].childNodes[0].nodeValue;
                                    }
                                    else{
                                        fila[<%=FilaVisProspectoresVO.POS_CAMPO_FECHA_SEGUIMIENTO%>] = '-';
                                    }
                                }
                                else if(hijosFila[cont].nodeName=="FECHA_NACIMIENTO"){
                                    nodoCampo = hijosFila[cont];
                                    if(nodoCampo.childNodes.length > 0){
                                        fila[<%=FilaVisProspectoresVO.POS_CAMPO_FECHA_NACIMIENTO%>] = nodoCampo.childNodes[0].childNodes[0].nodeValue;
                                        camposErrores[<%=FilaVisProspectoresVO.POS_CAMPO_FECHA_NACIMIENTO%>] = nodoCampo.childNodes[1].childNodes[0].nodeValue;
                                    }
                                    else{
                                        fila[<%=FilaVisProspectoresVO.POS_CAMPO_FECHA_NACIMIENTO%>] = '-';
                                    }
                                }
                                else if(hijosFila[cont].nodeName=="SEXO"){
                                    nodoCampo = hijosFila[cont];
                                    if(nodoCampo.childNodes.length > 0){
                                        fila[<%=FilaVisProspectoresVO.POS_CAMPO_SEXO%>] = nodoCampo.childNodes[0].childNodes[0].nodeValue;
                                        camposErrores[<%=FilaVisProspectoresVO.POS_CAMPO_SEXO%>] = nodoCampo.childNodes[1].childNodes[0].nodeValue;
                                    }
                                    else{
                                        fila[<%=FilaVisProspectoresVO.POS_CAMPO_SEXO%>] = '-';
                                    }
                                }
                                else if(hijosFila[cont].nodeName=="TIPO_DISCAPACIDAD"){
                                    nodoCampo = hijosFila[cont];
                                    if(nodoCampo.childNodes.length > 0){
                                        fila[<%=FilaVisProspectoresVO.POS_CAMPO_TIPODISCAPACIDAD%>] = nodoCampo.childNodes[0].childNodes[0].nodeValue;
                                        camposErrores[<%=FilaVisProspectoresVO.POS_CAMPO_TIPODISCAPACIDAD%>] = nodoCampo.childNodes[1].childNodes[0].nodeValue;
                                    }
                                    else{
                                        fila[<%=FilaVisProspectoresVO.POS_CAMPO_TIPODISCAPACIDAD%>] = '-';
                                    }
                                }
                                else if(hijosFila[cont].nodeName=="GRAVEDAD"){
                                    nodoCampo = hijosFila[cont];
                                    if(nodoCampo.childNodes.length > 0){
                                        fila[<%=FilaVisProspectoresVO.POS_CAMPO_GRAVEDAD%>] = nodoCampo.childNodes[0].childNodes[0].nodeValue;
                                        camposErrores[<%=FilaVisProspectoresVO.POS_CAMPO_GRAVEDAD%>] = nodoCampo.childNodes[1].childNodes[0].nodeValue;
                                    }
                                    else{
                                        fila[<%=FilaVisProspectoresVO.POS_CAMPO_GRAVEDAD%>] = '-';
                                    }
                                }
                                else if(hijosFila[cont].nodeName=="TIPO_CONTRATO"){
                                    nodoCampo = hijosFila[cont];
                                    if(nodoCampo.childNodes.length > 0){
                                        fila[<%=FilaVisProspectoresVO.POS_CAMPO_TIPOCONTRATO%>] = nodoCampo.childNodes[0].childNodes[0].nodeValue;
                                        camposErrores[<%=FilaVisProspectoresVO.POS_CAMPO_TIPOCONTRATO%>] = nodoCampo.childNodes[1].childNodes[0].nodeValue;
                                    }
                                    else{
                                        fila[<%=FilaVisProspectoresVO.POS_CAMPO_TIPOCONTRATO%>] = '-';
                                    }
                                }
                                else if(hijosFila[cont].nodeName=="PORC_JORNADA"){
                                    nodoCampo = hijosFila[cont];
                                    if(nodoCampo.childNodes.length > 0){
                                        fila[<%=FilaVisProspectoresVO.POS_CAMPO_PORCJORN%>] = nodoCampo.childNodes[0].childNodes[0].nodeValue;
                                        camposErrores[<%=FilaVisProspectoresVO.POS_CAMPO_PORCJORN%>] = nodoCampo.childNodes[1].childNodes[0].nodeValue;
                                    }
                                    else{
                                        fila[<%=FilaVisProspectoresVO.POS_CAMPO_PORCJORN%>] = '-';
                                    }
                                }
                                else if(hijosFila[cont].nodeName=="FECHA_INICIO"){
                                    nodoCampo = hijosFila[cont];
                                    if(nodoCampo.childNodes.length > 0){
                                        fila[<%=FilaVisProspectoresVO.POS_CAMPO_FECHA_INICIO%>] = nodoCampo.childNodes[0].childNodes[0].nodeValue;
                                        camposErrores[<%=FilaVisProspectoresVO.POS_CAMPO_FECHA_INICIO%>] = nodoCampo.childNodes[1].childNodes[0].nodeValue;
                                    }
                                    else{
                                        fila[<%=FilaVisProspectoresVO.POS_CAMPO_FECHA_INICIO%>] = '-';
                                    }
                                }
                                else if(hijosFila[cont].nodeName=="FECHA_FIN"){
                                    nodoCampo = hijosFila[cont];
                                    if(nodoCampo.childNodes.length > 0){
                                        fila[<%=FilaVisProspectoresVO.POS_CAMPO_FECHA_FIN%>] = nodoCampo.childNodes[0].childNodes[0].nodeValue;
                                        camposErrores[<%=FilaVisProspectoresVO.POS_CAMPO_FECHA_FIN%>] = nodoCampo.childNodes[1].childNodes[0].nodeValue;
                                    }
                                    else{
                                        fila[<%=FilaVisProspectoresVO.POS_CAMPO_FECHA_FIN%>] = '-';
                                    }
                                }
                                else if(hijosFila[cont].nodeName=="NIF_PREPARADOR"){
                                    nodoCampo = hijosFila[cont];
                                    if(nodoCampo.childNodes.length > 0){
                                        fila[<%=FilaVisProspectoresVO.POS_CAMPO_NIF_PREPARADOR%>] = nodoCampo.childNodes[0].childNodes[0].nodeValue;
                                        camposErrores[<%=FilaVisProspectoresVO.POS_CAMPO_NIF_PREPARADOR%>] = nodoCampo.childNodes[1].childNodes[0].nodeValue;
                                    }
                                    else{
                                        fila[<%=FilaVisProspectoresVO.POS_CAMPO_NIF_PREPARADOR%>] = '-';
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
                                else if(hijosFila[cont].nodeName=="PERS_CONTACTO"){
                                    nodoCampo = hijosFila[cont];
                                    if(nodoCampo.childNodes.length > 0){
                                        fila[<%=FilaVisProspectoresVO.POS_CAMPO_CONTACTO%>] = nodoCampo.childNodes[0].childNodes[0].nodeValue;
                                        camposErrores[<%=FilaVisProspectoresVO.POS_CAMPO_CONTACTO%>] = nodoCampo.childNodes[1].childNodes[0].nodeValue;
                                    }
                                    else{
                                        fila[<%=FilaVisProspectoresVO.POS_CAMPO_CONTACTO%>] = '-';
                                    }
                                }     
                                else if(hijosFila[cont].nodeName=="HORAS_CONTRATO"){
                                    nodoCampo = hijosFila[cont];
                                    if(nodoCampo.childNodes.length > 0){
                                        fila[<%=FilaVisProspectoresVO.POS_CAMPO_HORAS_ANUALES%>] = nodoCampo.childNodes[0].childNodes[0].nodeValue;
                                        camposErrores[<%=FilaVisProspectoresVO.POS_CAMPO_HORAS_ANUALES%>] = nodoCampo.childNodes[1].childNodes[0].nodeValue;
                                    }
                                    else{
                                        fila[<%=FilaVisProspectoresVO.POS_CAMPO_HORAS_ANUALES%>] = '-';
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
                                    fila[17] = listaErrores;
                                }
                            }
                            fila[18] = camposErrores;
                            listaVisitas[j] = fila;
                            fila = new Array();
                            camposErrores = new Array();
                        }   
                    }//for(j=0;hijos!=null && j<hijos.length;j++)

                    return listaVisitas;
            }
            --%>
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
                    listaVisitas_prosTabla[i-1] = [fila[19] != undefined && fila[19].length > 0 ? '<img src="<%=request.getContextPath()%>/images/extension/melanbide44/alert.png"/>' : '', fila[1], fila[2], fila[3], fila[4], fila[5], fila[6], fila[7], fila[8], fila[9], fila[10], fila[11], fila[12], fila[13], fila[14], fila[15], fila[16], fila[17], fila[18]];
                    listaVisitas_prosTabla_titulos[i-1] = [getListAsText(fila[19]), fila[1], fila[2], fila[3], fila[4],                 
                        fila[5], fila[6], fila[7], fila[8], fila[9], fila[10], fila[11], fila[12], fila[13], fila[14], fila[15], fila[16], fila[17], fila[18]];
                    if(fila[19] != undefined && fila[19].length > 0){
                        listaEstilosFila = new Array();
                        camposErrores = fila[20];
                        for(var j = 0; j < camposErrores.length; j++){
                            if(camposErrores[j] == '<%=ConstantesMeLanbide44.CIERTO%>'){
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
                tabVisitas_pros.addColumna('30','center','<%=meLanbide44I18n.getMensaje(idiomaUsuario, "label.justificacion.visita.tabla.col0")%>','<%=meLanbide44I18n.getMensaje(idiomaUsuario, "label.solicitud.preparadores.tabla.col0.title")%>');    
                tabVisitas_pros.addColumna('150','left','<%=meLanbide44I18n.getMensaje(idiomaUsuario, "label.justificacion.visita.tabla.col2")%>');
                tabVisitas_pros.addColumna('80','left','<%=meLanbide44I18n.getMensaje(idiomaUsuario, "label.justificacion.visita.tabla.col3")%>');
                tabVisitas_pros.addColumna('80','left','<%=meLanbide44I18n.getMensaje(idiomaUsuario, "label.justificacion.visita.tabla.col1")%>');
                tabVisitas_pros.addColumna('150','left','<%=meLanbide44I18n.getMensaje(idiomaUsuario, "label.justificacion.visita.tabla.col4")%>');
                tabVisitas_pros.addColumna('150','left','<%=meLanbide44I18n.getMensaje(idiomaUsuario, "label.justificacion.visita.tabla.col5")%>');
                tabVisitas_pros.addColumna('60','left','<%=meLanbide44I18n.getMensaje(idiomaUsuario, "label.justificacion.visita.tabla.col6")%>');
                tabVisitas_pros.addColumna('100','left','<%=meLanbide44I18n.getMensaje(idiomaUsuario, "label.justificacion.visita.tabla.col7")%>');
                tabVisitas_pros.addColumna('100','left','<%=meLanbide44I18n.getMensaje(idiomaUsuario, "label.justificacion.visita.tabla.col8")%>');
                tabVisitas_pros.addColumna('70','left','<%=meLanbide44I18n.getMensaje(idiomaUsuario, "label.justificacion.visita.tabla.col9")%>');
                tabVisitas_pros.addColumna('80','left','<%=meLanbide44I18n.getMensaje(idiomaUsuario, "label.justificacion.visita.tabla.col10")%>');
                tabVisitas_pros.addColumna('80','left','<%=meLanbide44I18n.getMensaje(idiomaUsuario, "label.justificacion.visita.tabla.col11")%>');
                tabVisitas_pros.addColumna('100','left','<%=meLanbide44I18n.getMensaje(idiomaUsuario, "label.justificacion.visita.tabla.col12")%>');
                tabVisitas_pros.addColumna('120','left','<%=meLanbide44I18n.getMensaje(idiomaUsuario, "label.justificacion.visita.tabla.col13")%>');
                tabVisitas_pros.addColumna('60','left','<%=meLanbide44I18n.getMensaje(idiomaUsuario, "label.justificacion.visita.tabla.col14")%>');
                tabVisitas_pros.addColumna('80','left','<%=meLanbide44I18n.getMensaje(idiomaUsuario, "label.justificacion.visita.tabla.col15")%>');
                tabVisitas_pros.addColumna('100','left','<%=meLanbide44I18n.getMensaje(idiomaUsuario, "label.justificacion.visita.tabla.col16")%>');

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

            <%--function barraProgresoSegPrep(valor, idBarra) {
            if(valor=='on'){
                document.getElementById(idBarra).style.visibility = 'inherit';
            }
            else if(valor=='off'){
                document.getElementById(idBarra).style.visibility = 'hidden';
            }
            }

            function actualizarDatosProspectores(){       
                try{
                    var result = window.opener.getListaProspectoresJustificacionEca();
                    window.opener.recargarTablaPreparadoresJustificacionEca(result, true);
                    //alert('longitudresult:'+result.length);

                }catch(err){
                }

            }  --%>


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
                                                        <%=meLanbide44I18n.getMensaje(idiomaUsuario, "msg.guardandoDatos")%>
                                                    </span>
                                                     <span id="msgEliminandoDatosVis">
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
            <div style="width: 95%; padding: 10px; text-align: left;">
                <!--<div class="tituloAzul" style="clear: both; text-align: left;">-->
                <div class="sub3titulo" style="clear: both; text-align: left;height:18px;">
                    <span>
                        <%=meLanbide44I18n.getMensaje(idiomaUsuario,"label.justificacion.visita.datosVisita")%>
                    </span>
                </div>
                <div class="lineaFormulario">
                    <div>
                        <%=meLanbide44I18n.getMensaje(idiomaUsuario,"label.justificacion.visita.prospector").toUpperCase()%>:<span 
                        type="text" id="nif" name="nif" size="10" maxlength="10" />-<span 
                        type="text" id="nomApel" name="nomApel" size="83" maxlength="200" />
                    </div>                
                <div style="clear: both;">
                    <div id="listaVisitas_pros" style="padding: 5px; width:750px; height: 340px; text-align: center; margin:0px;margin-top:0px;" align="center"></div>
                </div>

                <div class="botonera">
                    <input type="button" id="btnNuevoVisitaPros" name="btnNuevoVisitaPros" class="botonGeneral" value="<%=meLanbide44I18n.getMensaje(idiomaUsuario, "btn.general.nuevo")%>" onclick="pulsarNuevoVisitaPros();">
                    <input type="button" id="btnModificarVisitaPros" name="btnModificarVisitaPros" class="botonGeneral" value="<%=meLanbide44I18n.getMensaje(idiomaUsuario, "btn.general.modificar")%>" onclick="pulsarModifVisitaPros();" >
                    <input type="button" id="btnEliminarVisitaPros" name="btnEliminarVisitaPros" class="botonGeneral" value="<%=meLanbide44I18n.getMensaje(idiomaUsuario, "btn.general.eliminar")%>" onclick="pulsarEliminarVisitaPros();">
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
            tabVisitas_pros.addColumna('30','center','<%=meLanbide44I18n.getMensaje(idiomaUsuario, "label.justificacion.visita.tabla.col0")%>','<%=meLanbide44I18n.getMensaje(idiomaUsuario, "label.solicitud.preparadores.tabla.col0.title")%>');    
            tabVisitas_pros.addColumna('150','left','<%=meLanbide44I18n.getMensaje(idiomaUsuario, "label.justificacion.visita.tabla.col2")%>');
            tabVisitas_pros.addColumna('80','left','<%=meLanbide44I18n.getMensaje(idiomaUsuario, "label.justificacion.visita.tabla.col3")%>');
            tabVisitas_pros.addColumna('80','left','<%=meLanbide44I18n.getMensaje(idiomaUsuario, "label.justificacion.visita.tabla.col1")%>');
            tabVisitas_pros.addColumna('150','left','<%=meLanbide44I18n.getMensaje(idiomaUsuario, "label.justificacion.visita.tabla.col4")%>');
            tabVisitas_pros.addColumna('150','left','<%=meLanbide44I18n.getMensaje(idiomaUsuario, "label.justificacion.visita.tabla.col5")%>');
            tabVisitas_pros.addColumna('60','left','<%=meLanbide44I18n.getMensaje(idiomaUsuario, "label.justificacion.visita.tabla.col6")%>');
            tabVisitas_pros.addColumna('100','left','<%=meLanbide44I18n.getMensaje(idiomaUsuario, "label.justificacion.visita.tabla.col7")%>');
            tabVisitas_pros.addColumna('100','left','<%=meLanbide44I18n.getMensaje(idiomaUsuario, "label.justificacion.visita.tabla.col8")%>');
            tabVisitas_pros.addColumna('70','left','<%=meLanbide44I18n.getMensaje(idiomaUsuario, "label.justificacion.visita.tabla.col9")%>');
            tabVisitas_pros.addColumna('80','left','<%=meLanbide44I18n.getMensaje(idiomaUsuario, "label.justificacion.visita.tabla.col10")%>');
            tabVisitas_pros.addColumna('80','left','<%=meLanbide44I18n.getMensaje(idiomaUsuario, "label.justificacion.visita.tabla.col11")%>');
            tabVisitas_pros.addColumna('100','left','<%=meLanbide44I18n.getMensaje(idiomaUsuario, "label.justificacion.visita.tabla.col12")%>');
            tabVisitas_pros.addColumna('120','left','<%=meLanbide44I18n.getMensaje(idiomaUsuario, "label.justificacion.visita.tabla.col13")%>');
            tabVisitas_pros.addColumna('60','left','<%=meLanbide44I18n.getMensaje(idiomaUsuario, "label.justificacion.visita.tabla.col14")%>');
            tabVisitas_pros.addColumna('80','left','<%=meLanbide44I18n.getMensaje(idiomaUsuario, "label.justificacion.visita.tabla.col15")%>');
            tabVisitas_pros.addColumna('100','left','<%=meLanbide44I18n.getMensaje(idiomaUsuario, "label.justificacion.visita.tabla.col16")%>');

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
                    '<%=act.getPuesto()%>', '<%=act.getMail()%>', '<%=act.getTelefono()%>','<%=act.getNifProspector()%>', '<%=act.getNumTrab()%>' , '<%=act.getNumTrabDisc()%>', '<%=act.getDescCumpleLismi()%>', '<%=act.getDescResultadoFinal()%>', '<%=act.getObservaciones()%>'];

                listaVisitas_prosTabla[<%=i%>] = ['<%=act.getErrores() != null && act.getErrores().size() > 0 ? "<img src=\""+request.getContextPath()+"/images/extension/melanbide44/alert.png\" />" : ""%>','<%=act.getEmpresa()%>', '<%=act.getCif()%>', '<%=act.getFecVisita()%>', '<%=act.getDescSector()%>',
                    '<%=act.getDireccion()%>','<%=act.getCpostal()%>','<%=act.getLocalidad()%>','<%=act.getDescProvincia()%>','<%=act.getPersContacto()%>',
                    '<%=act.getPuesto()%>', '<%=act.getMail()%>', '<%=act.getTelefono()%>','<%=act.getNifProspector()%>', '<%=act.getNumTrab()%>' , '<%=act.getNumTrabDisc()%>', '<%=act.getDescCumpleLismi()%>', '<%=act.getDescResultadoFinal()%>', '<%=act.getObservaciones()%>'];

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
                    '<%=act.getPuesto()%>', '<%=act.getMail()%>', '<%=act.getTelefono()%>','<%=act.getNifProspector()%>', '<%=act.getNumTrab()%>' , '<%=act.getNumTrabDisc()%>', '<%=act.getDescCumpleLismi()%>', '<%=act.getDescResultadoFinal()%>', '<%=act.getObservaciones()%>'];

                listaVisitas_prosTabla[<%=i%>] = ['<%=act.getErrores() != null && act.getErrores().size() > 0 ? "<img src=\""+request.getContextPath()+"/images/extension/melanbide44/alert.png\" />" : ""%>','<%=act.getEmpresa()%>', '<%=act.getCif()%>', '<%=act.getFecVisita()%>', '<%=act.getDescSector()%>',
                    '<%=act.getDireccion()%>','<%=act.getCpostal()%>','<%=act.getLocalidad()%>','<%=act.getDescProvincia()%>','<%=act.getPersContacto()%>',
                    '<%=act.getPuesto()%>', '<%=act.getMail()%>', '<%=act.getTelefono()%>','<%=act.getNifProspector()%>', '<%=act.getNumTrab()%>' , '<%=act.getNumTrabDisc()%>', '<%=act.getDescCumpleLismi()%>', '<%=act.getDescResultadoFinal()%>', '<%=act.getObservaciones()%>'];

                inicializarErroresCamposVisPros();

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
                        if(camposErrores[cont] == '<%=ConstantesMeLanbide44.CIERTO%>'){
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
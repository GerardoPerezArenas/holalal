<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide44.i18n.MeLanbide44I18n" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide44.vo.justificacion.EcaJusPreparadoresVO" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide44.vo.justificacion.FilaInsPreparadoresVO" %>
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
            String colectivo    = request.getParameter("colectivo");
            if (colectivo==null ||colectivo.equals("null")) colectivo="";
            String sexo    = request.getParameter("sexo");
            if (sexo==null || sexo.equals("null")) sexo = "";
            EcaJusPreparadoresVO preparador = (EcaJusPreparadoresVO)request.getAttribute("preparador");
            String codOrganizacion  = request.getParameter("codOrganizacionModulo");
            Boolean consulta = request.getAttribute("consulta") != null ? (Boolean)request.getAttribute("consulta") : false;

            String tituloPagina = meLanbide44I18n.getMensaje(idiomaUsuario,"label.justificacion.inserciones.tituloPagina");


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
                        document.getElementById('btnNuevoSeguimientoJus').style.display = 'none';
                        document.getElementById('btnModificarSeguimientoJus').style.display = 'none';
                        document.getElementById('btnEliminarSeguimientoJus').style.display = 'none';
                        document.getElementById('btnConsultar').style.display = 'inline';
                    <%
                    }
                    else
                    {
                    %>
                        document.getElementById('btnNuevoSeguimientoJus').style.display = 'inline';
                        document.getElementById('btnModificarSeguimientoJus').style.display = 'inline';
                        document.getElementById('btnEliminarSeguimientoJus').style.display = 'inline';
                        document.getElementById('btnConsultar').style.display = 'none';
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

            function inicializarErroresCamposSegPrep(){
                camposErrores = new Array();
                for(var i = 0; i < <%=FilaInsPreparadoresVO.NUM_CAMPOS_FILA %>; i++){
                    camposErrores[i] = '<%=ConstantesMeLanbide44.FALSO%>';
                }
            }


            function pulsarNuevoSeguimientoJus(){
                    var control = new Date();
                    var result = null;
                    if(navigator.appName.indexOf("Internet Explorer")!=-1){
                        result = lanzarPopUpModal('<%=request.getContextPath()%>/PeticionModuloIntegracion.do?tarea=preparar&modulo=MELANBIDE44&operacion=cargarNuevoSeguimientoPreparador&tipo=0&numero=<%=numExpediente%>&opcion=nuevo&tiposeg=1&preparador=<%=preparador!=null?preparador.getJusPreparadoresCod():""  %>&colectivo=<%=colectivo%>&sexo=<%=sexo%>&control='+control.getTime(),390,820,'no','no');
                    }else{
                        result = lanzarPopUpModal('<%=request.getContextPath()%>/PeticionModuloIntegracion.do?tarea=preparar&modulo=MELANBIDE44&operacion=cargarNuevoSeguimientoPreparador&tipo=0&numero=<%=numExpediente%>&opcion=nuevo&tiposeg=1&preparador=<%=preparador!=null?preparador.getJusPreparadoresCod():""  %>&colectivo=<%=colectivo%>&sexo=<%=sexo%>&control='+control.getTime(),410,850,'no','no');
                    }
                    if (result != undefined){
                        if(result[0] == '0'){
                            window.returnValue = true;
                            recargarTablaSegPreparadores(result);
                            //this.parent.actualizarTablaPreparadoresJustificacionEca(true);
                            //window.opener.actualizarTablaPreparadoresJustificacionEca(true);

                        }
                    }
            }

            function pulsarModifSegPreparador(){                
                if(tabSeguimientos_prep.selectedIndex != -1) {
                    var control = new Date();
                    var result = null;
                    var opcion = '<%=consulta != null && consulta == true ? "consultar" : "modificar"%>';
                    if(navigator.appName.indexOf("Internet Explorer")!=-1){
                        result = lanzarPopUpModal('<%=request.getContextPath()%>/PeticionModuloIntegracion.do?tarea=preparar&modulo=MELANBIDE44&operacion=cargarNuevoSeguimientoPreparador&tipo=0&numero=<%=numExpediente%>&opcion='+opcion+'&preparador=<%=preparador!=null?preparador.getJusPreparadoresCod():""  %>&colectivo=<%=colectivo%>&sexo=<%=sexo%>&idSegModificar='+listaSeguimientos_prep[tabSeguimientos_prep.selectedIndex][0]+'&control='+control.getTime(),390,820,'no','no');
                    }else{
                        result = lanzarPopUpModal('<%=request.getContextPath()%>/PeticionModuloIntegracion.do?tarea=preparar&modulo=MELANBIDE44&operacion=cargarNuevoSeguimientoPreparador&tipo=0&numero=<%=numExpediente%>&opcion='+opcion+'&preparador=<%=preparador!=null?preparador.getJusPreparadoresCod():""  %>&colectivo=<%=colectivo%>&sexo=<%=sexo%>&idSegModificar='+listaSeguimientos_prep[tabSeguimientos_prep.selectedIndex][0]+'&control='+control.getTime(),410,850,'no','no');
                    }
                    if (result != undefined){
                        if(result[0] == '0'){
                            window.returnValue = true;
                            recargarTablaSegPreparadores(result);
                            //window.opener.actualizarTablaPreparadoresJustificacionEca(true);
                        }
                    }
                } 
                else {
                    jsp_alerta('A', '<%=meLanbide44I18n.getMensaje(idiomaUsuario, "msg.msjNoSelecFila")%>');
                }
            }

            function pulsarEliminarSegPreparador(){
                    if(tabSeguimientos_prep.selectedIndex != -1) {
                        var resultado = jsp_alerta('', '<%=meLanbide44I18n.getMensaje(idiomaUsuario, "msg.preguntaEliminar")%>');
                        if (resultado == 1){

                            document.getElementById('msgGuardandoDatos').style.display="none";
                            document.getElementById('msgEliminandoDatos').style.display="inline";
                            barraProgresoSegPrep('on', 'barraProgresoSegPreparador');
                            var ajax = getXMLHttpRequest();
                            var nodos = null;
                            var CONTEXT_PATH = '<%=request.getContextPath()%>'
                            var url = CONTEXT_PATH + "/PeticionModuloIntegracion.do";
                            var parametros = "";
                            var control = new Date();
                            var listaSeguimientos = new Array();
                            parametros = 'tarea=preparar&modulo=MELANBIDE44&operacion=eliminarSegPreparador&tipo=0&numero=<%=numExpediente%>&idSegPrep='+listaSeguimientos_prep[tabSeguimientos_prep.selectedIndex][0]+'&tipoSeg=<%=ConstantesMeLanbide44.TIPO_INSERCION_MELANBIDE44%>&colectivo=<%=colectivo%>&sexo=<%=sexo%>&filtrar=<%=preparador != null ? "1" : "0"%>&control='+control.getTime();
                            try{
                                ajax.open("POST",url,false);
                                ajax.setRequestHeader("Content-Type","application/x-www-form-urlencoded; charset=ISO-8859-1");
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
                                listaSeguimientos = extraerListadoSegPreparadores(nodos);
                                recargarTablaSegPreparadores(listaSeguimientos);
                                barraProgresoSegPrep('off', 'barraProgresoSegPreparador');
                                var codigoOperacion = listaSeguimientos[0];
                                if(codigoOperacion=="0"){
                                    window.returnValue = true;
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
                    var listaSeguimientos = new Array();
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
                            listaSeguimientos[j] = codigoOperacion;
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
                                        fila[<%=FilaInsPreparadoresVO.POS_CAMPO_NIF%>] = nodoCampo.childNodes[0].childNodes[0].nodeValue;
                                        camposErrores[<%=FilaInsPreparadoresVO.POS_CAMPO_NIF%>] = nodoCampo.childNodes[1].childNodes[0].nodeValue;
                                    }
                                    else{
                                        fila[<%=FilaInsPreparadoresVO.POS_CAMPO_NIF%>] = '-';
                                    }
                                }
                                else if(hijosFila[cont].nodeName=="NOMBRE"){
                                    nodoCampo = hijosFila[cont];
                                    if(nodoCampo.childNodes.length > 0){
                                        fila[<%=FilaInsPreparadoresVO.POS_CAMPO_NOMBREAPEL%>] = nodoCampo.childNodes[0].childNodes[0].nodeValue;
                                        camposErrores[<%=FilaInsPreparadoresVO.POS_CAMPO_NOMBREAPEL%>] = nodoCampo.childNodes[1].childNodes[0].nodeValue;
                                    }
                                    else{
                                        fila[<%=FilaInsPreparadoresVO.POS_CAMPO_NOMBREAPEL%>] = '-';
                                    }
                                }
                                
                                else if(hijosFila[cont].nodeName=="FECHA_NACIMIENTO"){
                                    nodoCampo = hijosFila[cont];
                                    if(nodoCampo.childNodes.length > 0){
                                        fila[<%=FilaInsPreparadoresVO.POS_CAMPO_FECHA_NACIMIENTO%>] = nodoCampo.childNodes[0].childNodes[0].nodeValue;
                                        camposErrores[<%=FilaInsPreparadoresVO.POS_CAMPO_FECHA_NACIMIENTO%>] = nodoCampo.childNodes[1].childNodes[0].nodeValue;
                                    }
                                    else{
                                        fila[<%=FilaInsPreparadoresVO.POS_CAMPO_FECHA_NACIMIENTO%>] = '-';
                                    }
                                }
                                else if(hijosFila[cont].nodeName=="SEXO"){
                                    nodoCampo = hijosFila[cont];
                                    if(nodoCampo.childNodes.length > 0){
                                        fila[<%=FilaInsPreparadoresVO.POS_CAMPO_SEXO%>] = nodoCampo.childNodes[0].childNodes[0].nodeValue;
                                        camposErrores[<%=FilaInsPreparadoresVO.POS_CAMPO_SEXO%>] = nodoCampo.childNodes[1].childNodes[0].nodeValue;
                                    }
                                    else{
                                        fila[<%=FilaInsPreparadoresVO.POS_CAMPO_SEXO%>] = '-';
                                    }
                                }
                                else if(hijosFila[cont].nodeName=="COLECTIVO"){
                                    nodoCampo = hijosFila[cont];
                                    if(nodoCampo.childNodes.length > 0){
                                        fila[<%=FilaInsPreparadoresVO.POS_CAMPO_COLECTIVO%>] = nodoCampo.childNodes[0].childNodes[0].nodeValue;
                                        camposErrores[<%=FilaInsPreparadoresVO.POS_CAMPO_COLECTIVO%>] = nodoCampo.childNodes[1].childNodes[0].nodeValue;
                                    }
                                    else{
                                        fila[<%=FilaInsPreparadoresVO.POS_CAMPO_SEXO%>] = '-';
                                    }
                                }
                                else if(hijosFila[cont].nodeName=="TIPO_DISCAPACIDAD"){
                                    nodoCampo = hijosFila[cont];
                                    if(nodoCampo.childNodes.length > 0){
                                        fila[<%=FilaInsPreparadoresVO.POS_CAMPO_TIPODISCAPACIDAD%>] = nodoCampo.childNodes[0].childNodes[0].nodeValue;
                                        camposErrores[<%=FilaInsPreparadoresVO.POS_CAMPO_TIPODISCAPACIDAD%>] = nodoCampo.childNodes[1].childNodes[0].nodeValue;
                                    }
                                    else{
                                        fila[<%=FilaInsPreparadoresVO.POS_CAMPO_TIPODISCAPACIDAD%>] = '-';
                                    }
                                }
                                else if(hijosFila[cont].nodeName=="GRAVEDAD"){
                                    nodoCampo = hijosFila[cont];
                                    if(nodoCampo.childNodes.length > 0){
                                        fila[<%=FilaInsPreparadoresVO.POS_CAMPO_GRAVEDAD%>] = nodoCampo.childNodes[0].childNodes[0].nodeValue;
                                        camposErrores[<%=FilaInsPreparadoresVO.POS_CAMPO_GRAVEDAD%>] = nodoCampo.childNodes[1].childNodes[0].nodeValue;
                                    }
                                    else{
                                        fila[<%=FilaInsPreparadoresVO.POS_CAMPO_GRAVEDAD%>] = '-';
                                    }
                                }
                                else if(hijosFila[cont].nodeName=="TIPO_CONTRATO"){
                                    nodoCampo = hijosFila[cont];
                                    if(nodoCampo.childNodes.length > 0){
                                        fila[<%=FilaInsPreparadoresVO.POS_CAMPO_TIPOCONTRATO%>] = nodoCampo.childNodes[0].childNodes[0].nodeValue;
                                        camposErrores[<%=FilaInsPreparadoresVO.POS_CAMPO_TIPOCONTRATO%>] = nodoCampo.childNodes[1].childNodes[0].nodeValue;
                                    }
                                    else{
                                        fila[<%=FilaInsPreparadoresVO.POS_CAMPO_TIPOCONTRATO%>] = '-';
                                    }
                                }
                                else if(hijosFila[cont].nodeName=="PORC_JORNADA"){
                                    nodoCampo = hijosFila[cont];
                                    if(nodoCampo.childNodes.length > 0){
                                        fila[<%=FilaInsPreparadoresVO.POS_CAMPO_PORCJORN%>] = nodoCampo.childNodes[0].childNodes[0].nodeValue;
                                        camposErrores[<%=FilaInsPreparadoresVO.POS_CAMPO_PORCJORN%>] = nodoCampo.childNodes[1].childNodes[0].nodeValue;
                                    }
                                    else{
                                        fila[<%=FilaInsPreparadoresVO.POS_CAMPO_PORCJORN%>] = '-';
                                    }
                                }
                                else if(hijosFila[cont].nodeName=="FECHA_INICIO"){
                                    nodoCampo = hijosFila[cont];
                                    if(nodoCampo.childNodes.length > 0){
                                        fila[<%=FilaInsPreparadoresVO.POS_CAMPO_FECHA_INICIO%>] = nodoCampo.childNodes[0].childNodes[0].nodeValue;
                                        camposErrores[<%=FilaInsPreparadoresVO.POS_CAMPO_FECHA_INICIO%>] = nodoCampo.childNodes[1].childNodes[0].nodeValue;
                                    }
                                    else{
                                        fila[<%=FilaInsPreparadoresVO.POS_CAMPO_FECHA_INICIO%>] = '-';
                                    }
                                }
                                else if(hijosFila[cont].nodeName=="FECHA_FIN"){
                                    nodoCampo = hijosFila[cont];
                                    if(nodoCampo.childNodes.length > 0){
                                        fila[<%=FilaInsPreparadoresVO.POS_CAMPO_FECHA_FIN%>] = nodoCampo.childNodes[0].childNodes[0].nodeValue;
                                        camposErrores[<%=FilaInsPreparadoresVO.POS_CAMPO_FECHA_FIN%>] = nodoCampo.childNodes[1].childNodes[0].nodeValue;
                                    }
                                    else{
                                        fila[<%=FilaInsPreparadoresVO.POS_CAMPO_FECHA_FIN%>] = '-';
                                    }
                                }
                                else if(hijosFila[cont].nodeName=="NIF_PREPARADOR"){
                                    nodoCampo = hijosFila[cont];
                                    if(nodoCampo.childNodes.length > 0){
                                        fila[<%=FilaInsPreparadoresVO.POS_CAMPO_NIF_PREPARADOR%>] = nodoCampo.childNodes[0].childNodes[0].nodeValue;
                                        camposErrores[<%=FilaInsPreparadoresVO.POS_CAMPO_NIF_PREPARADOR%>] = nodoCampo.childNodes[1].childNodes[0].nodeValue;
                                    }
                                    else{
                                        fila[<%=FilaInsPreparadoresVO.POS_CAMPO_NIF_PREPARADOR%>] = '-';
                                    }
                                }
                                else if(hijosFila[cont].nodeName=="EMPRESA"){
                                    nodoCampo = hijosFila[cont];
                                    if(nodoCampo.childNodes.length > 0){
                                        fila[<%=FilaInsPreparadoresVO.POS_CAMPO_EMPRESA%>] = nodoCampo.childNodes[0].childNodes[0].nodeValue;
                                        camposErrores[<%=FilaInsPreparadoresVO.POS_CAMPO_EMPRESA%>] = nodoCampo.childNodes[1].childNodes[0].nodeValue;
                                    }
                                    else{
                                        fila[<%=FilaInsPreparadoresVO.POS_CAMPO_EMPRESA%>] = '-';
                                    }
                                }
                                /*else if(hijosFila[cont].nodeName=="PERS_CONTACTO"){
                                    nodoCampo = hijosFila[cont];
                                    if(nodoCampo.childNodes.length > 0){
                                        fila[<%//=FilaInsPreparadoresVO.POS_CAMPO_CONTACTO%>] = nodoCampo.childNodes[0].childNodes[0].nodeValue;
                                        camposErrores[<%//=FilaInsPreparadoresVO.POS_CAMPO_CONTACTO%>] = nodoCampo.childNodes[1].childNodes[0].nodeValue;
                                    }
                                    else{
                                        fila[<%//=FilaInsPreparadoresVO.POS_CAMPO_CONTACTO%>] = '-';
                                    }
                                } */    
                                else if(hijosFila[cont].nodeName=="HORAS_CONTRATO"){
                                    nodoCampo = hijosFila[cont];
                                    if(nodoCampo.childNodes.length > 0){
                                        fila[<%=FilaInsPreparadoresVO.POS_CAMPO_HORAS_ANUALES%>] = nodoCampo.childNodes[0].childNodes[0].nodeValue;
                                        camposErrores[<%=FilaInsPreparadoresVO.POS_CAMPO_HORAS_ANUALES%>] = nodoCampo.childNodes[1].childNodes[0].nodeValue;
                                    }
                                    else{
                                        fila[<%=FilaInsPreparadoresVO.POS_CAMPO_HORAS_ANUALES%>] = '-';
                                    }
                                }                        
                                else if(hijosFila[cont].nodeName=="OBSERVACIONES"){
                                    nodoCampo = hijosFila[cont];
                                    if(nodoCampo.childNodes.length > 0){
                                        fila[<%=FilaInsPreparadoresVO.POS_CAMPO_OBSERVACIONES%>] = nodoCampo.childNodes[0].childNodes[0].nodeValue;
                                        camposErrores[<%=FilaInsPreparadoresVO.POS_CAMPO_OBSERVACIONES%>] = nodoCampo.childNodes[1].childNodes[0].nodeValue;
                                    }
                                    else{
                                        fila[<%=FilaInsPreparadoresVO.POS_CAMPO_OBSERVACIONES%>] = '-';
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
                            listaSeguimientos[j] = fila;
                            fila = new Array();
                            camposErrores = new Array();
                        }   
                    }//for(j=0;hijos!=null && j<hijos.length;j++)

                    return listaSeguimientos;
            }

            function recargarTablaSegPreparadores(result){

           // var camposErrores = new Array();
                var fila;
                listaSeguimientos_prep = new Array();
                listaSeguimientos_prepTabla = new Array();        
                listaSeguimientos_prepTabla_titulos = new Array();
                listaSeguimientos_prepTabla_estilos = new Array();        
                var hombre = '<%=meLanbide44I18n.getMensaje(idiomaUsuario, "label.justificacion.seguimiento.sexo0").toUpperCase() %>';
                var mujer = '<%=meLanbide44I18n.getMensaje(idiomaUsuario, "label.justificacion.seguimiento.sexo1").toUpperCase() %>';
                var listaEstilosFila;

                for(var i = 1;i< result.length; i++){
                    fila = result[i];
                    listaSeguimientos_prep[i-1] = fila;
                    if (fila[4]=="0") fila[4]=hombre;
                    else if (fila[4]=="1") fila[4]=mujer;
                    <%
                        if(colectivo == null || colectivo.equals(""))
                        {
                    %>
                            listaSeguimientos_prepTabla[i-1] = [fila[17] != undefined && fila[17].length > 0 ? '<img src="<%=request.getContextPath()%>/images/extension/melanbide44/alert.png"/>' : '', fila[1], fila[2], fila[3], fila[4], fila[5], fila[6], fila[7], fila[8], fila[9], fila[10], fila[11], fila[12], fila[13], fila[14], fila[15], fila[17]];
                            listaSeguimientos_prepTabla_titulos[i-1] = [getListAsText(fila[17]), fila[1], fila[2], fila[3],fila[4],                 
                                fila[5], fila[6], fila[7], fila[8], fila[9], fila[10], fila[11], fila[12], fila[13], fila[14],  fila[15]/*, fila[16]*/];
                        
                    <%
                        }
                        else
                        {
                    %>
                            listaSeguimientos_prepTabla[i-1] = [fila[17] != undefined && fila[17].length > 0 ? '<img src="<%=request.getContextPath()%>/images/extension/melanbide44/alert.png"/>' : '', fila[1], fila[2], fila[3], fila[4], fila[6], fila[7], fila[8], fila[9], fila[10], fila[11], fila[12], fila[13], fila[14], fila[15], fila[17]];
                            listaSeguimientos_prepTabla_titulos[i-1] = [getListAsText(fila[17]), fila[1], fila[2], fila[3],fila[4],                 
                                fila[6], fila[7], fila[8], fila[9], fila[10], fila[11], fila[12], fila[13], fila[14],  fila[15]/*, fila[16]*/];
                    <%
                        }
                    %>
                    if(fila[17] != undefined && fila[17].length > 0){
                        listaEstilosFila = new Array();
                        camposErrores = fila[18];
                        for(var j = 0; j < camposErrores.length; j++){
                            if(j < <%=FilaInsPreparadoresVO.POS_CAMPO_COLECTIVO%>){
                                if(camposErrores[j] == '<%=ConstantesMeLanbide44.CIERTO%>'){
                                    listaEstilosFila[j] = 'color: red;';
                                }else{
                                    listaEstilosFila[j] = 'color: black;';
                                }
                            }else{
                                
                                <%
                                    if(colectivo == null || colectivo.equals(""))
                                    {
                                %>
                                        if(camposErrores[j] == '<%=ConstantesMeLanbide44.CIERTO%>'){
                                            listaEstilosFila[j] = 'color: red;';
                                        }else{
                                            listaEstilosFila[j] = 'color: black;';
                                        }
                                <%
                                    }
                                    else
                                    {
                                %>
                                        if(j > <%=FilaInsPreparadoresVO.POS_CAMPO_COLECTIVO%>){           
                                            if(camposErrores[j] == '<%=ConstantesMeLanbide44.CIERTO%>'){
                                                listaEstilosFila[j-1] = 'color: red;';
                                            }else{
                                                listaEstilosFila[j-1] = 'color: black;';
                                            }
                                        }
                                <%
                                    }
                                %>
                            }
                        }

                        listaEstilosFila[<%=FilaInsPreparadoresVO.POS_CAMPO_NIF%>] = 'color: red;';
                        listaEstilosFila[<%=FilaInsPreparadoresVO.POS_CAMPO_NOMBREAPEL%>] = 'color: red;';

                        listaSeguimientos_prepTabla_estilos[i-1] = listaEstilosFila;
                    }
                }


                tabSeguimientos_prep = new FixedColumnTable(document.getElementById('listaSeguimientos_prep'), 850, 876, 'listaSeguimientos_prep');
                tabSeguimientos_prep.addColumna('30','left','<%=meLanbide44I18n.getMensaje(idiomaUsuario, "label.justificacion.inserciones.tabla.col0")%>');        
                tabSeguimientos_prep.addColumna('80','left','<%=meLanbide44I18n.getMensaje(idiomaUsuario, "label.justificacion.inserciones.tabla.col2")%>');
                tabSeguimientos_prep.addColumna('150','left','<%=meLanbide44I18n.getMensaje(idiomaUsuario, "label.justificacion.inserciones.tabla.col3")%>');
               
                tabSeguimientos_prep.addColumna('80','left','<%=meLanbide44I18n.getMensaje(idiomaUsuario, "label.justificacion.inserciones.tabla.col4")%>');
                tabSeguimientos_prep.addColumna('60','left','<%=meLanbide44I18n.getMensaje(idiomaUsuario, "label.justificacion.inserciones.tabla.col5")%>');
                
                <%
                    if(colectivo == null || colectivo.equals(""))
                    {
                %>
                        tabSeguimientos_prep.addColumna('70','left','<%=meLanbide44I18n.getMensaje(idiomaUsuario, "label.justificacion.inserciones.tabla.col6")%>');
                <%
                    }
                %>
                
                tabSeguimientos_prep.addColumna('90','left','<%=meLanbide44I18n.getMensaje(idiomaUsuario, "label.justificacion.inserciones.tabla.col7")%>');
                tabSeguimientos_prep.addColumna('100','left','<%=meLanbide44I18n.getMensaje(idiomaUsuario, "label.justificacion.inserciones.tabla.col8")%>');
                tabSeguimientos_prep.addColumna('100','left','<%=meLanbide44I18n.getMensaje(idiomaUsuario, "label.justificacion.inserciones.tabla.col9")%>');
                tabSeguimientos_prep.addColumna('70','right','<%=meLanbide44I18n.getMensaje(idiomaUsuario, "label.justificacion.inserciones.tabla.col10")%>');
                tabSeguimientos_prep.addColumna('80','left','<%=meLanbide44I18n.getMensaje(idiomaUsuario, "label.justificacion.inserciones.tabla.col11")%>');
                tabSeguimientos_prep.addColumna('80','left','<%=meLanbide44I18n.getMensaje(idiomaUsuario, "label.justificacion.inserciones.tabla.col12")%>');
                tabSeguimientos_prep.addColumna('100','left','<%=meLanbide44I18n.getMensaje(idiomaUsuario, "label.justificacion.inserciones.tabla.col13")%>');
                tabSeguimientos_prep.addColumna('130','left','<%=meLanbide44I18n.getMensaje(idiomaUsuario, "label.justificacion.inserciones.tabla.col14")%>');
                //tabSeguimientos_prep.addColumna('150','left','<%//=meLanbide44I18n.getMensaje(idiomaUsuario, "label.justificacion.inserciones.tabla.col15")%>');
                tabSeguimientos_prep.addColumna('90','right','<%=meLanbide44I18n.getMensaje(idiomaUsuario, "label.justificacion.inserciones.tabla.col16")%>');
                tabSeguimientos_prep.addColumna('250','left','<%=meLanbide44I18n.getMensaje(idiomaUsuario, "label.justificacion.inserciones.tabla.col17")%>');

                tabSeguimientos_prep.numColumnasFijas = 3;        

                tabSeguimientos_prep.height = '300';

                tabSeguimientos_prep.altoCabecera = 50;

                tabSeguimientos_prep.dblClkFunction = 'dblClckTablaSegPreparador';

                //tabPreparadores_jus.lineas=listaSeguimientos_prepTabla;

                for(var cont = 0; cont < listaSeguimientos_prepTabla.length; cont++){
                    tabSeguimientos_prep.addFilaConFormato(listaSeguimientos_prepTabla[cont], listaSeguimientos_prepTabla_titulos[cont], listaSeguimientos_prepTabla_estilos[cont])
                }

                tabSeguimientos_prep.displayTabla();

                tabSeguimientos_prep.pack();
            }

            function dblClckTablaSegPreparador(rowID,tableName){
                pulsarModifSegPreparador();
            }

            function barraProgresoSegPrep(valor, idBarra) {
            if(valor=='on'){
                document.getElementById(idBarra).style.visibility = 'inherit';
            }
            else if(valor=='off'){
                document.getElementById(idBarra).style.visibility = 'hidden';
            }
            }


        </script>
    </head>
    <body onload="inicio();" class="contenidoPantalla etiqueta" style="margin:0" >
        <form>
            <div id="barraProgresoSegPreparador" style="position: absolute; z-index:10; visibility: hidden; top: 30%; left: 30%;">
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
                                                     <span id="msgEliminandoDatos">
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
                        <%=meLanbide44I18n.getMensaje(idiomaUsuario,"label.solicitud.preparadores.datosPreparador")%>
                    </span>
                </div>
                <div class="lineaFormulario">
                    <div style="width:50%;float:left">
                        <%=meLanbide44I18n.getMensaje(idiomaUsuario,"label.justificacion.preparadores.preparador").toUpperCase()%>:
                        <label id="nif" name="nif"><%=preparador != null && preparador.getNif() != null && !preparador.getNif().equals("") ? preparador.getNif().toUpperCase() + (preparador.getNombre() != null && !preparador.getNombre().equals("") ? " - " : "") : "" %></label><label id="nomApel" name="nomApel"><%=preparador != null && preparador.getNombre() != null ? preparador.getNombre().toUpperCase() : "" %></label>
                    </div>   
                    <%
                        if(colectivo != null && !colectivo.equals(""))
                        {
                    %>
                            <div style="width:50%;float:left">
                                <%=meLanbide44I18n.getMensaje(idiomaUsuario,"label.justificacion.preparadores.colectivo").toUpperCase()%>:
                                <label id="nif" name="nif"><%=colectivo.toUpperCase()%></label>
                            </div>  
                    <%
                        }
                    %>
                <div style="clear: both;">
                    <div id="listaSeguimientos_prep" style="padding: 5px; width:750px; height: 340px; text-align: center; margin:0px;margin-top:0px;" align="center"></div>
                </div>

                <div class="botonera">
                    <input type="button" id="btnNuevoSeguimientoJus" name="btnNuevoSeguimientoJus" class="botonGeneral" value="<%=meLanbide44I18n.getMensaje(idiomaUsuario, "btn.general.nuevo")%>" onclick="pulsarNuevoSeguimientoJus();">
                    <input type="button" id="btnModificarSeguimientoJus" name="btnModificarSeguimientoJus" class="botonGeneral" value="<%=meLanbide44I18n.getMensaje(idiomaUsuario, "btn.general.modificar")%>" onclick="pulsarModifSegPreparador();" >
                    <input type="button" id="btnEliminarSeguimientoJus" name="btnEliminarSeguimientoJus" class="botonGeneral" value="<%=meLanbide44I18n.getMensaje(idiomaUsuario, "btn.general.eliminar")%>" onclick="pulsarEliminarSegPreparador();">
                    <input type="button" id="btnConsultar" name="btnConsultar" class="botonGeneral" value="<%=meLanbide44I18n.getMensaje(idiomaUsuario, "btn.general.consultar")%>" onclick="pulsarModifSegPreparador();">
                    <input type="button" id="btnCerrar" name="btnCerrar" class="botonGeneral" value="<%=meLanbide44I18n.getMensaje(idiomaUsuario, "btn.general.cerrar")%>" onclick="cerrarVentana();">
                </div>
            </div>
        </form>
        <div id="popupcalendar" class="text"></div>

        <script type="text/javascript">

            var tabSeguimientos_prep;
            var listaSeguimientos_prep = new Array();
            var listaSeguimientos_prepTabla = new Array();
            var listaSeguimientos_prepTabla_titulos = new Array();
            var listaSeguimientos_prepTabla_estilos = new Array();
            var camposErrores = new Array();

            tabSeguimientos_prep = new FixedColumnTable(document.getElementById('listaSeguimientos_prep'), 725, 750, 'listaSeguimientos_prep');
            tabSeguimientos_prep.addColumna('30','center','<%=meLanbide44I18n.getMensaje(idiomaUsuario, "label.justificacion.inserciones.tabla.col0")%>','<%=meLanbide44I18n.getMensaje(idiomaUsuario, "label.solicitud.preparadores.tabla.col0.title")%>');    
            tabSeguimientos_prep.addColumna('80','left','<%=meLanbide44I18n.getMensaje(idiomaUsuario, "label.justificacion.inserciones.tabla.col2")%>');
            tabSeguimientos_prep.addColumna('150','left','<%=meLanbide44I18n.getMensaje(idiomaUsuario, "label.justificacion.inserciones.tabla.col3")%>');
            
            tabSeguimientos_prep.addColumna('80','left','<%=meLanbide44I18n.getMensaje(idiomaUsuario, "label.justificacion.inserciones.tabla.col4")%>');
            tabSeguimientos_prep.addColumna('60','left','<%=meLanbide44I18n.getMensaje(idiomaUsuario, "label.justificacion.inserciones.tabla.col5")%>');

            <%
                if(colectivo == null || colectivo.equals(""))
                {
            %>
                    tabSeguimientos_prep.addColumna('70','left','<%=meLanbide44I18n.getMensaje(idiomaUsuario, "label.justificacion.inserciones.tabla.col6")%>');
            <%
                }
            %>
            
            tabSeguimientos_prep.addColumna('90','left','<%=meLanbide44I18n.getMensaje(idiomaUsuario, "label.justificacion.inserciones.tabla.col7")%>');
            tabSeguimientos_prep.addColumna('100','left','<%=meLanbide44I18n.getMensaje(idiomaUsuario, "label.justificacion.inserciones.tabla.col8")%>');
            tabSeguimientos_prep.addColumna('100','left','<%=meLanbide44I18n.getMensaje(idiomaUsuario, "label.justificacion.inserciones.tabla.col9")%>');
            tabSeguimientos_prep.addColumna('70','right','<%=meLanbide44I18n.getMensaje(idiomaUsuario, "label.justificacion.inserciones.tabla.col10")%>');
            tabSeguimientos_prep.addColumna('80','left','<%=meLanbide44I18n.getMensaje(idiomaUsuario, "label.justificacion.inserciones.tabla.col11")%>');
            tabSeguimientos_prep.addColumna('80','left','<%=meLanbide44I18n.getMensaje(idiomaUsuario, "label.justificacion.inserciones.tabla.col12")%>');
            tabSeguimientos_prep.addColumna('100','left','<%=meLanbide44I18n.getMensaje(idiomaUsuario, "label.justificacion.inserciones.tabla.col13")%>');
            tabSeguimientos_prep.addColumna('130','left','<%=meLanbide44I18n.getMensaje(idiomaUsuario, "label.justificacion.inserciones.tabla.col14")%>');
            //tabSeguimientos_prep.addColumna('150','left','<%//=meLanbide44I18n.getMensaje(idiomaUsuario, "label.justificacion.inserciones.tabla.col15")%>');
            tabSeguimientos_prep.addColumna('90','right','<%=meLanbide44I18n.getMensaje(idiomaUsuario, "label.justificacion.inserciones.tabla.col16")%>');
            tabSeguimientos_prep.addColumna('250','left','<%=meLanbide44I18n.getMensaje(idiomaUsuario, "label.justificacion.inserciones.tabla.col17")%>');

            tabSeguimientos_prep.numColumnasFijas = 3;

            <%  		
                FilaInsPreparadoresVO act = null;

                List<FilaInsPreparadoresVO> segList = (List<FilaInsPreparadoresVO>)request.getAttribute("listaSeguimientos");
                if (segList!= null && segList.size() >0){
                    for (int i = 0; i <segList.size(); i++)
                    {
                        act = segList.get(i);

            %>
                <%
                    if(colectivo == null || colectivo.equals(""))
                    {
                %>
                        listaSeguimientos_prep[<%=i%>] = ['<%=act.getCodPreparador()%>_'+'<%=act.getIdseg()%>', '<%=act.getNif()%>', '<%=act.getNombreApe()%>', '<%=act.getFecNacimiento()%>',
                        '<%=act.getSexo()%>','<%=act.getColectivo()%>','<%=act.getTipoDiscapacidad()%>','<%=act.getGravedad()%>','<%=act.getTipoContrato()%>','<%=act.getPorcJornada()%>',
                        '<%=act.getFecIni()%>', '<%=act.getFecFin()%>', '<%=act.getNifPreparador()%>', '<%=act.getEmpresa()%>' , '<%=act.getHorasCont()%>', '<%=act.getObservaciones()%>'];

                        listaSeguimientos_prepTabla[<%=i%>] = ['<%=act.getErrores() != null && act.getErrores().size() > 0 ? "<img src=\""+request.getContextPath()+"/images/extension/melanbide44/alert.png\" />" : ""%>', '<%=act.getNif()%>', '<%=act.getNombreApe()%>', '<%=act.getFecNacimiento()%>',
                        '<%=act.getSexo().equals("0")?meLanbide44I18n.getMensaje(idiomaUsuario, "label.justificacion.seguimiento.sexo0").toUpperCase():meLanbide44I18n.getMensaje(idiomaUsuario, "label.justificacion.seguimiento.sexo1").toUpperCase()%>','<%=act.getColectivo()%>','<%=act.getTipoDiscapacidad()%>','<%=act.getGravedad()%>','<%=act.getTipoContrato()%>','<%=act.getPorcJornada()%>',
                        '<%=act.getFecIni()%>', '<%=act.getFecFin()%>', '<%=act.getNifPreparador()%>', '<%=act.getEmpresa()%>',  '<%=act.getHorasCont()%>', '<%=act.getObservaciones()%>'];
                <%
                    }
                    else
                    {
                %>
                        listaSeguimientos_prep[<%=i%>] = ['<%=act.getCodPreparador()%>_'+'<%=act.getIdseg()%>', '<%=act.getNif()%>', '<%=act.getNombreApe()%>', '<%=act.getFecNacimiento()%>',
                        '<%=act.getSexo()%>','<%=act.getTipoDiscapacidad()%>','<%=act.getGravedad()%>','<%=act.getTipoContrato()%>','<%=act.getPorcJornada()%>',
                        '<%=act.getFecIni()%>', '<%=act.getFecFin()%>', '<%=act.getNifPreparador()%>', '<%=act.getEmpresa()%>' , '<%=act.getHorasCont()%>', '<%=act.getObservaciones()%>'];

                        listaSeguimientos_prepTabla[<%=i%>] = ['<%=act.getErrores() != null && act.getErrores().size() > 0 ? "<img src=\""+request.getContextPath()+"/images/extension/melanbide44/alert.png\" />" : ""%>', '<%=act.getNif()%>', '<%=act.getNombreApe()%>', '<%=act.getFecNacimiento()%>',
                        '<%=act.getSexo().equals("0")?meLanbide44I18n.getMensaje(idiomaUsuario, "label.justificacion.seguimiento.sexo0").toUpperCase():meLanbide44I18n.getMensaje(idiomaUsuario, "label.justificacion.seguimiento.sexo1").toUpperCase()%>','<%=act.getTipoDiscapacidad()%>','<%=act.getGravedad()%>','<%=act.getTipoContrato()%>','<%=act.getPorcJornada()%>',
                        '<%=act.getFecIni()%>', '<%=act.getFecFin()%>', '<%=act.getNifPreparador()%>', '<%=act.getEmpresa()%>', '<%=act.getHorasCont()%>', '<%=act.getObservaciones()%>'];
                <%
                    }
                %>

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
                <%
                    if(colectivo == null || colectivo.equals(""))
                    {
                %>

                        listaSeguimientos_prepTabla_titulos[<%=i%>] = [erroresStr,'<%=act.getNif()%>', '<%=act.getNombreApe()%>', '<%=act.getFecNacimiento()%>',
                        '<%=act.getSexo().equals("0")?meLanbide44I18n.getMensaje(idiomaUsuario, "label.justificacion.seguimiento.sexo0"):meLanbide44I18n.getMensaje(idiomaUsuario, "label.justificacion.seguimiento.sexo1")%>','<%=act.getColectivo()%>','<%=act.getTipoDiscapacidad()%>','<%=act.getGravedad()%>','<%=act.getTipoContrato()%>','<%=act.getPorcJornada()%>',
                        '<%=act.getFecIni()%>', '<%=act.getFecFin()%>', '<%=act.getNifPreparador()%>', '<%=act.getEmpresa()%>',  '<%=act.getHorasCont()%>', '<%=act.getObservaciones()%>'];


                        listaSeguimientos_prepTabla[<%=i%>] = ['<%=act.getErrores() != null && act.getErrores().size() > 0 ? "<img src=\""+request.getContextPath()+"/images/extension/melanbide44/alert.png\" />" : ""%>','<%=act.getNif()%>', '<%=act.getNombreApe()%>', '<%=act.getFecNacimiento()%>',
                        '<%=act.getSexo().equals("0")?meLanbide44I18n.getMensaje(idiomaUsuario, "label.justificacion.seguimiento.sexo0").toUpperCase():(act.getSexo().equals("1")?meLanbide44I18n.getMensaje(idiomaUsuario, "label.justificacion.seguimiento.sexo1").toUpperCase():act.getSexo())%>','<%=act.getColectivo()%>','<%=act.getTipoDiscapacidad()%>','<%=act.getGravedad()%>','<%=act.getTipoContrato()%>','<%=act.getPorcJornada()%>',
                        '<%=act.getFecIni()%>', '<%=act.getFecFin()%>', '<%=act.getNifPreparador()%>', '<%=act.getEmpresa()%>',  '<%=act.getHorasCont()%>', '<%=act.getObservaciones()%>'];

                <%
                    }
                    else
                    {
                %>
                        listaSeguimientos_prepTabla_titulos[<%=i%>] = [erroresStr,'<%=act.getNif()%>', '<%=act.getNombreApe()%>', '<%=act.getFecNacimiento()%>',
                        '<%=act.getSexo().equals("0")?meLanbide44I18n.getMensaje(idiomaUsuario, "label.justificacion.seguimiento.sexo0"):meLanbide44I18n.getMensaje(idiomaUsuario, "label.justificacion.seguimiento.sexo1")%>','<%=act.getTipoDiscapacidad()%>','<%=act.getGravedad()%>','<%=act.getTipoContrato()%>','<%=act.getPorcJornada()%>',
                        '<%=act.getFecIni()%>', '<%=act.getFecFin()%>', '<%=act.getNifPreparador()%>', '<%=act.getEmpresa()%>',  '<%=act.getHorasCont()%>', '<%=act.getObservaciones()%>'];


                        listaSeguimientos_prepTabla[<%=i%>] = ['<%=act.getErrores() != null && act.getErrores().size() > 0 ? "<img src=\""+request.getContextPath()+"/images/extension/melanbide44/alert.png\" />" : ""%>','<%=act.getNif()%>', '<%=act.getNombreApe()%>', '<%=act.getFecNacimiento()%>',
                        '<%=act.getSexo().equals("0")?meLanbide44I18n.getMensaje(idiomaUsuario, "label.justificacion.seguimiento.sexo0").toUpperCase():(act.getSexo().equals("1")?meLanbide44I18n.getMensaje(idiomaUsuario, "label.justificacion.seguimiento.sexo1").toUpperCase():act.getSexo())%>','<%=act.getTipoDiscapacidad()%>','<%=act.getGravedad()%>','<%=act.getTipoContrato()%>','<%=act.getPorcJornada()%>',
                        '<%=act.getFecIni()%>', '<%=act.getFecFin()%>', '<%=act.getNifPreparador()%>', '<%=act.getEmpresa()%>',  '<%=act.getHorasCont()%>', '<%=act.getObservaciones()%>'];
                <%
                    }
                %>
                inicializarErroresCamposSegPrep();

                <%
                    String errorAct = "";
                    for(int posCE = 0; posCE < FilaInsPreparadoresVO.NUM_CAMPOS_FILA; posCE++)
                    {
                %>
                            camposErrores[<%=posCE%>] = '<%=act.getErrorCampo(posCE)%>';
                <%    
                    }
                %>
                    var estilosFila = new Array();
                    for(var cont = 0; cont < camposErrores.length; cont++){
                        if(cont < <%=FilaInsPreparadoresVO.POS_CAMPO_COLECTIVO%>){
                            if(camposErrores[cont] == '<%=ConstantesMeLanbide44.CIERTO%>'){
                                estilosFila[cont] = 'color: red;';
                            }else{
                                estilosFila[cont] = 'color: black;';
                            }
                        }else{
                            <%
                                if(colectivo == null || colectivo.equals(""))
                                {
                            %>
                                     if(camposErrores[cont] == '<%=ConstantesMeLanbide44.CIERTO%>'){
                                            estilosFila[cont] = 'color: red;';
                                        }else{
                                            estilosFila[cont] = 'color: black;';
                                        }   
                            <%
                                }
                                else
                                {
                            %>
                                        if(cont > <%=FilaInsPreparadoresVO.POS_CAMPO_COLECTIVO%>){
                                            if(camposErrores[cont] == '<%=ConstantesMeLanbide44.CIERTO%>'){
                                                   estilosFila[cont-1] = 'color: red;';
                                               }else{
                                                   estilosFila[cont-1] = 'color: black;';
                                               }       
                                        }
                            <%
                                }
                            %>
                        }
                    }

                <%
                    if(act.getErrores() != null && act.getErrores().size() > 0)
                    {
                %>
                    estilosFila[<%=FilaInsPreparadoresVO.POS_CAMPO_NIF%>] = 'color: red;';
                    estilosFila[<%=FilaInsPreparadoresVO.POS_CAMPO_NOMBREAPEL%>] = 'color: red;';
                <%
                    }
                %>    

                    listaSeguimientos_prepTabla_estilos[<%=i%>] = estilosFila;



            <%
                    }// for
                }// if
            %>

            //tabSeguimientos_prep.lineas=listaSeguimientos_prepTabla;
            for(var cont = 0; cont < listaSeguimientos_prepTabla.length; cont++){
                tabSeguimientos_prep.addFilaConFormato(listaSeguimientos_prepTabla[cont], listaSeguimientos_prepTabla_titulos[cont], listaSeguimientos_prepTabla_estilos[cont])
            }

            tabSeguimientos_prep.height = '300';

            tabSeguimientos_prep.altoCabecera = 50;

            tabSeguimientos_prep.dblClkFunction = 'dblClckTablaSegPreparador';

            tabSeguimientos_prep.displayTabla();

            tabSeguimientos_prep.pack();


        </script>
    </body>
</html>
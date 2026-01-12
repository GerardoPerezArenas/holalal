<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>

<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide50.i18n.MeLanbide50I18n" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.common.service.config.Config"%>
<%@page import="es.altia.common.service.config.ConfigServiceHelper"%>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide50.vo.especialidades.EspecialidadesVO"%>
<%@page import="java.util.ArrayList" %>
<%@page import="java.util.List" %>

<html>
    <head>
        <%
            int idiomaUsuario = 1;
            if(request.getParameter("idioma") != null)
            {
                try
                {
                    idiomaUsuario = Integer.parseInt(request.getParameter("idioma"));
                }
                catch(Exception ex)
                {}
            }
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
            MeLanbide50I18n meLanbide50I18n = MeLanbide50I18n.getInstance();

            Config m_Config = ConfigServiceHelper.getConfig("common");
            String statusBar = m_Config.getString("JSP.StatusBar");
            String nombreModulo     = request.getParameter("nombreModulo");
            String codOrganizacion  = request.getParameter("codOrganizacionModulo");
            String numExpediente    = request.getParameter("numero");

            %>
    
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/estilo.css"/>
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/extension/melanbide50/melanbide50.css"/>
    
    <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/general.js"></script>
    <script type="text/javascript">
        var APP_CONTEXT_PATH = '<%=request.getContextPath()%>';
    </script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/validaciones.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/listaComboBox.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/popup.js"></script>

    <body>
            <div class="tab-page" id="tabPage501" style="height:420px; width: 90%;">
                <script type="text/javascript">tp1_p501 = tp1.addTabPage( document.getElementById( "tabPage501" ) );</script>
                <div style="clear: both;">
                    <label class="legendAzul" style="text-align: center; position: relative; left: 5px;"><%=meLanbide50I18n.getMensaje(idiomaUsuario, "especialidades.legend.titulo")%></label>
                        <div id="divGeneral"  style="overflow-y: auto; overflow-x: hidden; height: 360px;">     <!--onscroll="deshabilitarRadios();"-->
                            <div id="listaEspecialidades" style="padding: 5px; width:805px; height: 300px; text-align: center; overflow-x: auto; overflow-y: auto; margiwidth:0px;margin-top:0px;" align="center"></div>
                            <div class="botonera">
                                <input type="button" id="btnNuevaEspecialidad" name="btnNuevaEspecialidad" class="botonGeneral"  value="<%=meLanbide50I18n.getMensaje(idiomaUsuario, "btn.nuevo")%>" onclick="pulsarAltaEspecialidad();">
                                <input type="button" id="btnEliminarEspecialidad" name="btnEliminarEspecialidad"   class="botonGeneral" value="<%=meLanbide50I18n.getMensaje(idiomaUsuario, "btn.eliminar")%>" onclick="pulsarEliminarEspecialidad();">
                                <input type="button" id="btnModificarEspecialidad" name="btnModificarEspecialidad" class="botonGeneral" value="<%=meLanbide50I18n.getMensaje(idiomaUsuario, "btn.modificar")%>" onclick="pulsarModificarEspecialidad();">
                                <input type="button" id="btnListasxEspecialidades" name="btnListasxEspecialidades" class="botonMasLargo" style="float: right;" value="<%=meLanbide50I18n.getMensaje(idiomaUsuario, "btn.especialidades.listasxespecialidad")%>" onclick="pulsarListasxEspecialidad(event);">
                            </div>
                        </div>
                    <!--
                    <div class="botonera" style="padding-top: 20px;">
                        <input type="button" id="btnGuardarCE" name="btnGuardarCE" class="botonGeneral" value="<%=meLanbide50I18n.getMensaje(idiomaUsuario, "ori.btn.guardar")%>" onclick="guardarDatosCE();">
                    </div>
                    -->
                </div>
            </div>
            <!--Script Ejecucion Elementos Pagina-->
            <script type="text/javascript">
                //Tabla Especialidades
                var tabEspecialidades;
                var listaEspecialidades = new Array();
                var listaEspecialidadesTabla = new Array();

                tabEspecialidades = new Tabla(document.getElementById('listaEspecialidades'), 765); //790
                tabEspecialidades.addColumna('150','center',"<%= meLanbide50I18n.getMensaje(idiomaUsuario,"especialidades.tablaEspecialidades.col1")%>");
                tabEspecialidades.addColumna('600','left',"<%= meLanbide50I18n.getMensaje(idiomaUsuario,"especialidades.tablaEspecialidades.col2")%>");
                //tabEspecialidades.addColumna('80','center',"<%= meLanbide50I18n.getMensaje(idiomaUsuario,"especialidades.tablaEspecialidades.col3")%>");
                //tabEspecialidades.addColumna('90','center',"<%= meLanbide50I18n.getMensaje(idiomaUsuario,"especialidades.tablaEspecialidades.col4")%>");
                //tabEspecialidades.addColumna('200','center',"<%= meLanbide50I18n.getMensaje(idiomaUsuario,"especialidades.tablaEspecialidades.col5")%>");

                tabEspecialidades.displayCabecera=true;
                tabEspecialidades.height = 150;

                <%  		
                    EspecialidadesVO objectVO = null;
                    List<EspecialidadesVO> List = (List<EspecialidadesVO>)request.getAttribute("ListEspecialidades");													
                    if (List!= null && List.size() >0){
                        for (int indice=0;indice<List.size();indice++)
                        {
                            objectVO = List.get(indice);

                %>
                    var textoPresencial = '<%=objectVO.getInscripcionPresencial()%>';
                    var textoTeleformacion = '<%=objectVO.getInscripcionTeleformacion()%>';
                    var textoAcreditado = '<%=objectVO.getAcreditacion()%>';
                    if(textoPresencial == 1)
                        textoPresencial = 'N';
                    else if(textoPresencial == 0)
                        textoPresencial = 'S';
                    else
                        textoPresencial = '-';
                    if(textoTeleformacion == 1)
                        textoTeleformacion = 'N';
                    else if(textoTeleformacion == 0)
                        textoTeleformacion = 'S';
                    else
                        textoTeleformacion = '-';
                    if(textoAcreditado == 1)
                        textoAcreditado = 'N';
                    else if(textoAcreditado == 0)
                        textoAcreditado = 'S';
                    else
                        textoAcreditado = '-';
                    //listaEspecialidadesTabla[<%=indice%>] = ['<%=objectVO.getCodCP().toUpperCase()%>','<%=objectVO.getDenominacion()%>',textoPresencial,textoTeleformacion,textoAcreditado];
                    listaEspecialidadesTabla[<%=indice%>] = ['<%=objectVO.getCodCP().toUpperCase()%>','<%=objectVO.getDenominacion()%>'];
                    listaEspecialidades[<%=indice%>] = ['<%=objectVO.getId()%>','<%=objectVO.getCodCP()%>','<%=objectVO.getDenominacion()%>','<%=objectVO.getInscripcionPresencial()%>','<%=objectVO.getInscripcionTeleformacion()%>','<%=objectVO.getAcreditacion()%>'];

                <%
                        }// for
                    }// if
                %>
                   
                tabEspecialidades.lineas=listaEspecialidadesTabla;
                //tabEspecialidades.dblClkFunction = 'dblClckListasxEspecialidad';
                tabEspecialidades.displayTabla();
                if(navigator.appName.indexOf("Internet Explorer")!=-1){
                    try{
                        var div = document.getElementById('listaEspecialidades');
                        //div.children[0].children[0].children[0].children[0].children[0].children[1].style.width = '100%';
                        //div.children[0].children[1].children[0].children[1].style.width = '100%'; 
                        div.children[0].children[0].children[1].style.width = '100%';
                        div.children[1].style.width = '100%'; 
                    }
                    catch(err){

                    }
                }
                document.getElementById('listaEspecialidades').children[0].children[1].children[0].children[0].ondblclick = function(event){
                    pulsarListasxEspecialidad(event);
                }
            
            </script>
            <!-- Script Con Funciones-->
            <script type="text/javascript">

                function pulsarAltaEspecialidad(){
                    var control = new Date();
                    var result = null;
                    if(navigator.appName.indexOf("Internet Explorer")!=-1){
                        result = lanzarPopUpModal('<%=request.getContextPath()%>/PeticionModuloIntegracion.do?tarea=preparar&modulo=MELANBIDE50&operacion=cargarNuevaEspecialidad&tipo=0&numero=<%=numExpediente%>&control='+control.getTime(),300,780,'no','no');
                    }else{
                        result = lanzarPopUpModal('<%=request.getContextPath()%>/PeticionModuloIntegracion.do?tarea=preparar&modulo=MELANBIDE50&operacion=cargarNuevaEspecialidad&tipo=0&numero=<%=numExpediente%>&control='+control.getTime(),300,780,'no','no');
                    }
                    if (result != undefined){
                        if(result[0] == '0'){
                            recargarTablaEspecialidades(result);
                            actualizarDatosDisponibilidad();
                            //actualizarDatosIdentificacionEsp();
                        }
                    }
                }

                function pulsarModificarEspecialidad(){
                    if(tabEspecialidades.selectedIndex != -1) {
                        var control = new Date();
                        var result = null;
                        if(navigator.appName.indexOf("Internet Explorer")!=-1){
                            result = lanzarPopUpModal('<%=request.getContextPath()%>/PeticionModuloIntegracion.do?tarea=preparar&modulo=MELANBIDE50&operacion=cargarModifEspecialidad&tipo=0&numero=<%=numExpediente%>&id='+listaEspecialidades[tabEspecialidades.selectedIndex][0]+'&control='+control.getTime(),300,780,'no','no');
                        }else{
                            result = lanzarPopUpModal('<%=request.getContextPath()%>/PeticionModuloIntegracion.do?tarea=preparar&modulo=MELANBIDE50&operacion=cargarModifEspecialidad&tipo=0&numero=<%=numExpediente%>&id='+listaEspecialidades[tabEspecialidades.selectedIndex][0]+'&control='+control.getTime(),300,780,'no','no');
                        }
                        if (result != undefined){
                            if(result[0] == '0'){
                                recargarTablaEspecialidades(result);
                                actualizarDatosDisponibilidad();
                                //actualizarDatosIdentificacionEsp();
                            }
                        }
                    } 
                    else {
                        jsp_alerta('A', '<%=meLanbide50I18n.getMensaje(idiomaUsuario, "msg.msjNoSelecFila")%>');
                    }
                }

                function pulsarEliminarEspecialidad(){
                    if(tabEspecialidades.selectedIndex != -1) {
                        var resultado = jsp_alerta('', '<%=meLanbide50I18n.getMensaje(idiomaUsuario, "msg.especialidades.preguntaEliminar")%>');
                        if (resultado == 1){

                            var ajax = getXMLHttpRequest();
                            var nodos = null;
                            var CONTEXT_PATH = '<%=request.getContextPath()%>'
                            var url = CONTEXT_PATH + "/PeticionModuloIntegracion.do";
                            var parametros = "";
                            var control = new Date();
                            parametros = 'tarea=preparar&modulo=MELANBIDE50&operacion=eliminarEspecialidad&tipo=0&numero=<%=numExpediente%>&id='+listaEspecialidades[tabEspecialidades.selectedIndex][0]+'&control='+control.getTime();
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
                                var elemento = nodos[0];
                                var hijos = elemento.childNodes;
                                var codigoOperacion = null;
                                var listaTrayectoriaNueva = new Array();
                                var fila = new Array();
                                var nodoFila;
                                var hijosFila;
                                for(j=0;hijos!=null && j<hijos.length;j++){
                                    if(hijos[j].nodeName=="CODIGO_OPERACION"){                            
                                        codigoOperacion = hijos[j].childNodes[0].nodeValue;
                                        listaTrayectoriaNueva[j] = codigoOperacion;
                                    }//if(hijos[j].nodeName=="CODIGO_OPERACION")                      
                                    else if(hijos[j].nodeName=="FILA"){
                                        nodoFila = hijos[j];
                                        hijosFila = nodoFila.childNodes;
                                        for(var cont = 0; cont < hijosFila.length; cont++){
                                            if(hijosFila[cont].nodeName=="ID"){
                                                if(hijosFila[cont].childNodes.length > 0){
                                                    fila[0] = hijosFila[cont].childNodes[0].nodeValue;
                                                }
                                                else{
                                                    fila[0] = '-';
                                                }
                                            }
                                            else if(hijosFila[cont].nodeName=="ESP_CODCP"){
                                                if(hijosFila[cont].childNodes.length > 0){
                                                    fila[1] = hijosFila[cont].childNodes[0].nodeValue;
                                                }
                                                else{
                                                    fila[1] = '-';
                                                }
                                            }
                                            else if(hijosFila[cont].nodeName=="ESP_DENOM"){
                                                if(hijosFila[cont].childNodes.length > 0){
                                                    fila[2] = hijosFila[cont].childNodes[0].nodeValue;
                                                }
                                                else{
                                                    fila[2] = '-';
                                                }
                                            }
                                            else if(hijosFila[cont].nodeName=="ESP_PRESE"){
                                                if(hijosFila[cont].childNodes.length > 0){
                                                    fila[3] = hijosFila[cont].childNodes[0].nodeValue;
                                                }
                                                else{
                                                    fila[3] = '-';
                                                }
                                            }
                                            else if(hijosFila[cont].nodeName=="ESP_TELEF"){
                                                if(hijosFila[cont].childNodes.length > 0){
                                                    fila[4] = hijosFila[cont].childNodes[0].nodeValue;
                                                }
                                                else{
                                                    fila[4] = '-';
                                                }
                                            }
                                            else if(hijosFila[cont].nodeName=="ESP_ACRED"){
                                                if(hijosFila[cont].childNodes.length > 0){
                                                    fila[5] = hijosFila[cont].childNodes[0].nodeValue;
                                                }
                                                else{
                                                    fila[5] = '-';
                                                }
                                            }
                                        }
                                        listaTrayectoriaNueva[j] = fila;
                                        fila = new Array();
                                    }   
                                }//for(j=0;hijos!=null && j<hijos.length;j++)
                                if(codigoOperacion=="0"){
                                    recargarTablaEspecialidades(listaTrayectoriaNueva);
                                    actualizarDatosDisponibilidad();
                                    //actualizarDatosIdentificacionEsp();
                                }else if(codigoOperacion=="1"){
                                    jsp_alerta("A",'<%=meLanbide50I18n.getMensaje(idiomaUsuario,"error.errorBD")%>');
                                }else if(codigoOperacion=="2"){
                                    jsp_alerta("A",'<%=meLanbide50I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                                }else if(codigoOperacion=="3"){
                                    jsp_alerta("A",'<%=meLanbide50I18n.getMensaje(idiomaUsuario,"error.pasoParametros")%>');
                                }else{
                                        jsp_alerta("A",'<%=meLanbide50I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                                }//if(
                            }
                            catch(Err){
                                jsp_alerta("A",'<%=meLanbide50I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                            }//try-catch
                        }
                    } 
                    else {
                        jsp_alerta('A', '<%=meLanbide50I18n.getMensaje(idiomaUsuario, "msg.msjNoSelecFila")%>');
                    }
                }

                function recargarTablaEspecialidades(result){
                    var fila;
                    listaEspecialidades = new Array();
                    listaEspecialidadesTabla = new Array();
                    for(var i = 1;i< result.length; i++){
                        fila = result[i];
                        listaEspecialidades[i-1] = fila;
                        listaEspecialidadesTabla[i-1] = [fila[1], fila[2]];//(fila[5]==0) ? 'S' : (fila[5]==1) ? 'N' : '-'
                    }
                    tabEspecialidades = new Tabla(document.getElementById('listaEspecialidades'), 765);
                    tabEspecialidades.addColumna('150','center',"<%= meLanbide50I18n.getMensaje(idiomaUsuario,"especialidades.tablaEspecialidades.col1")%>");
                    tabEspecialidades.addColumna('600','left',"<%= meLanbide50I18n.getMensaje(idiomaUsuario,"especialidades.tablaEspecialidades.col2")%>");
                    //tabEspecialidades.addColumna('80','center',"<%= meLanbide50I18n.getMensaje(idiomaUsuario,"especialidades.tablaEspecialidades.col3")%>");
                    //tabEspecialidades.addColumna('90','center',"<%= meLanbide50I18n.getMensaje(idiomaUsuario,"especialidades.tablaEspecialidades.col4")%>");
                    //tabEspecialidades.addColumna('200','center',"<%= meLanbide50I18n.getMensaje(idiomaUsuario,"especialidades.tablaEspecialidades.col5")%>");

                    tabEspecialidades.displayCabecera=true;
                    tabEspecialidades.height = 100;
                    tabEspecialidades.lineas=listaEspecialidadesTabla;
                    tabEspecialidades.dblClkFunction = 'dblClckListasxEspecialidad';
                    tabEspecialidades.displayTabla();

                    if(navigator.appName.indexOf("Internet Explorer")!=-1){
                        try{
                            var div = document.getElementById('listaEspecialidades');
                            //div.children[0].children[0].children[0].children[0].children[0].children[1].style.width = '100%';
                            //div.children[0].children[1].children[0].children[1].style.width = '100%'; 
                            div.children[0].children[0].children[1].style.width = '100%';
                            div.children[1].style.width = '100%'; 
                        }
                        catch(err){

                        }
                    }
                    
                    document.getElementById('listaEspecialidades').children[0].children[1].children[0].children[0].ondblclick = function(event){
                        pulsarListasxEspecialidad(event);
                    }
                }  
                
                function actualizarDatosDisponibilidad(){
                            var ajax = getXMLHttpRequest();
                            var nodos = null;
                            var CONTEXT_PATH = '<%=request.getContextPath()%>'
                            var url = CONTEXT_PATH + "/PeticionModuloIntegracion.do";
                            var parametros = "";
                            var control = new Date();
                            parametros = 'tarea=preparar&modulo=MELANBIDE50&operacion=actualizarDatosPantallaDisponibilidad&tipo=0&numero=<%=numExpediente%>&control='+control.getTime();
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
                                var elemento = nodos[0];
                                var hijos = elemento.childNodes;
                                var codigoOperacion = null;
                                var listaNueva = new Array();
                                var fila = new Array();
                                var nodoFila;
                                var hijosFila;
                                for(j=0;hijos!=null && j<hijos.length;j++){
                                    if(hijos[j].nodeName=="CODIGO_OPERACION"){                            
                                        codigoOperacion = hijos[j].childNodes[0].nodeValue;
                                        listaNueva[j] = codigoOperacion;
                                    }//if(hijos[j].nodeName=="CODIGO_OPERACION")                      
                                    else if(hijos[j].nodeName=="FILA"){
                                        nodoFila = hijos[j];
                                        hijosFila = nodoFila.childNodes;
                                        for(var cont = 0; cont < hijosFila.length; cont++){
                                            if(hijosFila[cont].nodeName=="ID"){
                                                if(hijosFila[cont].childNodes.length > 0){
                                                    fila[0] = hijosFila[cont].childNodes[0].nodeValue;
                                                }
                                                else{
                                                    fila[0] = '-';
                                                }
                                            }
                                            else if(hijosFila[cont].nodeName=="ID_ESPSOL"){
                                                if(hijosFila[cont].childNodes.length > 0){
                                                    fila[1] = hijosFila[cont].childNodes[0].nodeValue;
                                                }
                                                else{
                                                    fila[1] = '-';
                                                }
                                            }
                                            else if(hijosFila[cont].nodeName=="DRE_NUM"){
                                                if(hijosFila[cont].childNodes.length > 0){
                                                    fila[2] = hijosFila[cont].childNodes[0].nodeValue;
                                                }
                                                else{
                                                    fila[2] = '-';
                                                }
                                            }
                                            else if(hijosFila[cont].nodeName=="DRE_CODCP"){
                                                if(hijosFila[cont].childNodes.length > 0){
                                                    fila[3] = hijosFila[cont].childNodes[0].nodeValue;
                                                }
                                                else{
                                                    fila[3] = '-';
                                                }
                                            }
                                            else if(hijosFila[cont].nodeName=="DRE_PRCE"){
                                                if(hijosFila[cont].childNodes.length > 0){
                                                    fila[4] = hijosFila[cont].childNodes[0].nodeValue;
                                                }
                                                else{
                                                    fila[4] = '-';
                                                }
                                            }
                                            else if(hijosFila[cont].nodeName=="DRE_SIT"){
                                                if(hijosFila[cont].childNodes.length > 0){
                                                    fila[5] = hijosFila[cont].childNodes[0].nodeValue;
                                                }
                                                else{
                                                    fila[5] = '-';
                                                }
                                            }
                                            else if(hijosFila[cont].nodeName=="DRE_AUL"){
                                                if(hijosFila[cont].childNodes.length > 0){
                                                    fila[6] = hijosFila[cont].childNodes[0].nodeValue;
                                                }
                                                else{
                                                    fila[6] = '-';
                                                }
                                            }
                                            else if(hijosFila[cont].nodeName=="DRE_TAL"){
                                                if(hijosFila[cont].childNodes.length > 0){
                                                    fila[7] = hijosFila[cont].childNodes[0].nodeValue;
                                                }
                                                else{
                                                    fila[7] = '-';
                                                }
                                            }
                                            else if(hijosFila[cont].nodeName=="DRE_AUTA"){
                                                if(hijosFila[cont].childNodes.length > 0){
                                                    fila[8] = hijosFila[cont].childNodes[0].nodeValue;
                                                }
                                                else{
                                                    fila[8] = '-';
                                                }
                                            }
                                            else if(hijosFila[cont].nodeName=="DRE_CAPR"){
                                                if(hijosFila[cont].childNodes.length > 0){
                                                    fila[9] = hijosFila[cont].childNodes[0].nodeValue;
                                                }
                                                else{
                                                    fila[9] = '-';
                                                }
                                            }
                                        }
                                        listaNueva[j] = fila;
                                        fila = new Array();
                                    }   
                                }//for(j=0;hijos!=null && j<hijos.length;j++)
                                if(codigoOperacion=="0"){
                                    recargarTablaDisponibilidad(listaNueva);
                                }else if(codigoOperacion=="1"){
                                    jsp_alerta("A",'<%=meLanbide50I18n.getMensaje(idiomaUsuario,"error.errorBD")%>');
                                }else if(codigoOperacion=="2"){
                                    jsp_alerta("A",'<%=meLanbide50I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                                }else if(codigoOperacion=="3"){
                                    jsp_alerta("A",'<%=meLanbide50I18n.getMensaje(idiomaUsuario,"error.pasoParametros")%>');
                                }else{
                                        jsp_alerta("A",'<%=meLanbide50I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                                }//if(
                            }
                            catch(Err){
                                jsp_alerta("A",'<%=meLanbide50I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                            }//try-catch
                    //jsp_alerta('A', '<%=meLanbide50I18n.getMensaje(idiomaUsuario, "msg.msjNoSelecFila")%>');
                }
                
                function actualizarDatosIdentificacionEsp(){
                   var ajax = getXMLHttpRequest();
                            var nodos = null;
                            var CONTEXT_PATH = '<%=request.getContextPath()%>'
                            var url = CONTEXT_PATH + "/PeticionModuloIntegracion.do";
                            var parametros = "";
                            var control = new Date();
                            parametros = 'tarea=preparar&modulo=MELANBIDE50&operacion=actualizarDatosPantallaIdentificacionEsp&tipo=0&numero=<%=numExpediente%>&control='+control.getTime();
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
                                var elemento = nodos[0];
                                var hijos = elemento.childNodes;
                                var codigoOperacion = null;
                                var listaNueva = new Array();
                                var fila = new Array();
                                var nodoFila;
                                var hijosFila;
                                for(j=0;hijos!=null && j<hijos.length;j++){
                                    if(hijos[j].nodeName=="CODIGO_OPERACION"){                            
                                        codigoOperacion = hijos[j].childNodes[0].nodeValue;
                                        listaNueva[j] = codigoOperacion;
                                    }//if(hijos[j].nodeName=="CODIGO_OPERACION")                      
                                    else if(hijos[j].nodeName=="FILA"){
                                        nodoFila = hijos[j];
                                        hijosFila = nodoFila.childNodes;
                                        for(var cont = 0; cont < hijosFila.length; cont++){
                                            if(hijosFila[cont].nodeName=="ID"){
                                                if(hijosFila[cont].childNodes.length > 0){
                                                    fila[0] = hijosFila[cont].childNodes[0].nodeValue;
                                                }
                                                else{
                                                    fila[0] = '-';
                                                }
                                            }
                                            else if(hijosFila[cont].nodeName=="ID_ESPSOL"){
                                                if(hijosFila[cont].childNodes.length > 0){
                                                    fila[1] = hijosFila[cont].childNodes[0].nodeValue;
                                                }
                                                else{
                                                    fila[1] = '-';
                                                }
                                            }
                                            else if(hijosFila[cont].nodeName=="IDE_NUM"){
                                                if(hijosFila[cont].childNodes.length > 0){
                                                    fila[2] = hijosFila[cont].childNodes[0].nodeValue;
                                                }
                                                else{
                                                    fila[2] = '-';
                                                }
                                            }
                                            else if(hijosFila[cont].nodeName=="IDE_CODESP"){
                                                if(hijosFila[cont].childNodes.length > 0){
                                                    fila[3] = hijosFila[cont].childNodes[0].nodeValue;
                                                }
                                                else{
                                                    fila[3] = '-';
                                                }
                                            }
                                            else if(hijosFila[cont].nodeName=="IDE_DENESP"){
                                                if(hijosFila[cont].childNodes.length > 0){
                                                    fila[4] = hijosFila[cont].childNodes[0].nodeValue;
                                                }
                                                else{
                                                    fila[4] = '-';
                                                }
                                            }
                                            else if(hijosFila[cont].nodeName=="IDE_HORAS"){
                                                if(hijosFila[cont].childNodes.length > 0){
                                                    fila[5] = hijosFila[cont].childNodes[0].nodeValue;
                                                }
                                                else{
                                                    fila[5] = '-';
                                                }
                                            }
                                            else if(hijosFila[cont].nodeName=="IDE_ALUM"){
                                                if(hijosFila[cont].childNodes.length > 0){
                                                    fila[6] = hijosFila[cont].childNodes[0].nodeValue;
                                                }
                                                else{
                                                    fila[6] = '-';
                                                }
                                            }
                                            else if(hijosFila[cont].nodeName=="IDE_CERTP"){
                                                if(hijosFila[cont].childNodes.length > 0){
                                                    fila[7] = hijosFila[cont].childNodes[0].nodeValue;
                                                }
                                                else{
                                                    fila[7] = '-';
                                                }
                                            }
                                            else if(hijosFila[cont].nodeName=="IDE_RDER"){
                                                if(hijosFila[cont].childNodes.length > 0){
                                                    fila[8] = hijosFila[cont].childNodes[0].nodeValue;
                                                }
                                                else{
                                                    fila[8] = '-';
                                                }
                                            }
                                            else if(hijosFila[cont].nodeName=="IDE_BOEFP"){
                                                if(hijosFila[cont].childNodes.length > 0){
                                                    fila[9] = hijosFila[cont].childNodes[0].nodeValue;
                                                }
                                                else{
                                                    fila[9] = '-';
                                                }
                                            }
                                            else if(hijosFila[cont].nodeName=="IDE_DESADAP"){
                                                if(hijosFila[cont].childNodes.length > 0){
                                                    fila[10] = hijosFila[cont].childNodes[0].nodeValue;
                                                }
                                                else{
                                                    fila[10] = '-';
                                                }
                                            }
                                            else if(hijosFila[cont].nodeName=="IDE_OBSADAP"){
                                                if(hijosFila[cont].childNodes.length > 0){
                                                    fila[11] = hijosFila[cont].childNodes[0].nodeValue;
                                                }
                                                else{
                                                    fila[11] = '-';
                                                }
                                            }
                                        }
                                        listaNueva[j] = fila;
                                        fila = new Array();
                                    }   
                                }//for(j=0;hijos!=null && j<hijos.length;j++)
                                if(codigoOperacion=="0"){
                                    recargarTablaIdentificacionEsp(listaNueva);
                                }else if(codigoOperacion=="1"){
                                    jsp_alerta("A",'<%=meLanbide50I18n.getMensaje(idiomaUsuario,"error.errorBD")%>');
                                }else if(codigoOperacion=="2"){
                                    jsp_alerta("A",'<%=meLanbide50I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                                }else if(codigoOperacion=="3"){
                                    jsp_alerta("A",'<%=meLanbide50I18n.getMensaje(idiomaUsuario,"error.pasoParametros")%>');
                                }else{
                                        jsp_alerta("A",'<%=meLanbide50I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                                }//if(
                            }
                            catch(Err){
                                jsp_alerta("A",'<%=meLanbide50I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                            }//try-catch
                    //jsp_alerta('A', '<%=meLanbide50I18n.getMensaje(idiomaUsuario, "msg.msjNoSelecFila")%>');
                }
                
                //function dblClckListasxEspecialidad(rowID,tableName){
                //    pulsarListasxEspecialidad();
                //}
                /*function pulsarListasxEspecialidad1{
                    if(tabEspecialidades.selectedIndex != -1) {
                        var control = new Date();
                        var result = null;
                        var opcion = '';
                        if(document.forms[0].modoConsulta.value == "si"){
                            opcion = 'consultar';
                        }else{
                            opcion = 'modificar';
                        }
                        if(navigator.appName.indexOf("Internet Explorer")!=-1){
                            result = lanzarPopUpModal('<%=request.getContextPath()%>/PeticionModuloIntegracion.do?tarea=preparar&modulo=MELANBIDE50&operacion=cargarListasxEspecialidad&tipo=0&numero=<%=numExpediente%>&id='+listaEspecialidades[tabEspecialidades.selectedIndex][0]+'&control='+control.getTime(),700,1200,'no','no');
                        }else{
                            result = lanzarPopUpModal('<%=request.getContextPath()%>/PeticionModuloIntegracion.do?tarea=preparar&modulo=MELANBIDE50&operacion=cargarListasxEspecialidad&tipo=0&numero=<%=numExpediente%>&id='+listaEspecialidades[tabEspecialidades.selectedIndex][0]+'&control='+control.getTime(),700,1200,'no','no');
                        }
                        if (result != undefined){
                            recargarTablaEspecialidades(result);
                        }
                    } 
                    else {
                        jsp_alerta('A', '<%=meLanbide50I18n.getMensaje(idiomaUsuario, "msg.msjNoSelecFila")%>');
                    }
                }*/

                 function pulsarListasxEspecialidad(event){
                    var idTabla = document.getElementById('listaEspecialidades').children[0].children[1].children[0].children[0].id;
                    var fila;
                    if(window.event) { //IE
                        if(window.event.srcElement.tagName=='TD'){
                            if(window.event.srcElement.parentElement.parentElement.parentElement.id==idTabla){
                                fila = window.event.srcElement.parentElement.rowIndex;
                            }
                        }else if(window.event.srcElement.id=='btnListasxEspecialidades'){
                            fila = tabEspecialidades.selectedIndex;
                        }
                    }else{ // FF
                        if(event.target.tagName=='TD'){
                            if(event.target.parentElement.parentElement.parentElement.id==idTabla){
                                fila = event.target.parentNode.rowIndex;
                            }
                        }else if(window.event.srcElement.id=='btnListasxEspecialidades'){
                            fila = tabEspecialidades.selectedIndex;
                        }
                    }


                    if(fila >= 0 && fila < listaEspecialidades.length) {
                        var control = new Date();
                        var result = null;
                        if(navigator.appName.indexOf("Internet Explorer")!=-1){
                            result = lanzarPopUpModal('<%=request.getContextPath()%>/PeticionModuloIntegracion.do?tarea=preparar&modulo=MELANBIDE50&operacion=cargarListasxEspecialidad&tipo=0&numero=<%=numExpediente%>&id='+listaEspecialidades[fila][0]+'&control='+control.getTime(),520,1100,'no','no');
                        }else{
                            result = lanzarPopUpModal('<%=request.getContextPath()%>/PeticionModuloIntegracion.do?tarea=preparar&modulo=MELANBIDE50&operacion=cargarListasxEspecialidad&tipo=0&numero=<%=numExpediente%>&id='+listaEspecialidades[fila][0]+'&control='+control.getTime(),520,1100,'no','no');
                        }
                        if (result != undefined){
                            if(result[0] == '0'){
                                recargarTablaEspecialidades(result);
                            }
                        }
                    } 
                    else {
                        jsp_alerta('A', '<%=meLanbide50I18n.getMensaje(idiomaUsuario, "msg.msjNoSelecFila")%>');
                    }
                    
                }

            </script>
    </head>
    </body>
</html>

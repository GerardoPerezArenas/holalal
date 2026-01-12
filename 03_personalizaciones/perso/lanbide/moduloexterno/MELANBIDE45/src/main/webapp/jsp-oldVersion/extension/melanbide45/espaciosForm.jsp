<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>

<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide45.i18n.MeLanbide45I18n" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.common.service.config.Config"%>
<%@page import="es.altia.common.service.config.ConfigServiceHelper"%>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide45.vo.espacioform.EspacioFormVO"%>
<%@page import="java.util.ArrayList" %>
<%@page import="java.util.List" %>

<html>
    <head>
        <%
            int idiomaUsuario = 1;
            if(request.getParameter("codIdioma") != null)
            {
                try
                {
                    idiomaUsuario = Integer.parseInt(request.getParameter("codIdioma"));
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
            MeLanbide45I18n meLanbide45I18n = MeLanbide45I18n.getInstance();

            Config m_Config = ConfigServiceHelper.getConfig("common");
            String statusBar = m_Config.getString("JSP.StatusBar");
            String nombreModulo     = request.getParameter("nombreModulo");
            String codOrganizacion  = request.getParameter("codOrganizacionModulo");
            String numExpediente    = request.getParameter("numero");

            %>
    
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/estilo.css"/>
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/extension/melanbide45/melanbide45.css"/>
    <script type="text/javascript">
        var APP_CONTEXT_PATH = '<%=request.getContextPath()%>';
    </script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/general.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/validaciones.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/listaComboBox.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/popup.js"></script>

    <body>
            <div class="tab-page" id="tabPage452" style="height:420px; width: 91%;">
                <script type="text/javascript">tp1_p452 = tp1.addTabPage( document.getElementById( "tabPage452" ) );</script>
                <div style="clear: both;">
                    <label class="legendAzul" style="text-align: center; position: relative; left: 5px;"><%=meLanbide45I18n.getMensaje(idiomaUsuario, "legend.espacioformativo.titulo")%></label>
                        <div id="divGeneral"  style="overflow-y: auto; overflow-x: hidden; height: 440px;">     <!--onscroll="deshabilitarRadios();"-->
                            <div id="listaEspacioForm" style="padding: 5px; width:790px; height: 300px; text-align: center; overflow-x: auto; overflow-y: auto;margin:0px;margin-top:0px;" align="center"></div>
                            <div class="botonera">
                                <input type="button" id="btnNuevoEspacioForm" name="btnNuevoEspacioForm" class="botonGeneral"  value="<%=meLanbide45I18n.getMensaje(idiomaUsuario, "btn.nuevo")%>" onclick="pulsarAltaEspacioForm();">
                                <input type="button" id="btnEliminarEspacioForm" name="btnEliminarEspacioForm"   class="botonGeneral" value="<%=meLanbide45I18n.getMensaje(idiomaUsuario, "btn.eliminar")%>" onclick="pulsarEliminarEspacioForm();">
                                <input type="button" id="btnModificarEspacioForm" name="btnModificarEspacioForm" class="botonGeneral" value="<%=meLanbide45I18n.getMensaje(idiomaUsuario, "btn.modificar")%>" onclick="pulsarModificarEspacioForm();">
                            </div>
                        </div>
                </div>
            </div>
            <!--Script Ejecucion Elementos Pagina-->
            <script type="text/javascript">
                //Tabla Especialidades
                var tabEspacioForm;
                var listaEspacioForm = new Array();
                var listaEspacioFormTabla = new Array();

                tabEspacioForm = new Tabla(document.getElementById('listaEspacioForm'), 762);
                tabEspacioForm.addColumna('600','left',"<%= meLanbide45I18n.getMensaje(idiomaUsuario,"espacioformativo.tablaEspacioForm.col1")%>");
                tabEspacioForm.addColumna('150','right',"<%= meLanbide45I18n.getMensaje(idiomaUsuario,"espacioformativo.tablaEspacioForm.col2")%>");

                tabEspacioForm.displayCabecera=true;
                tabEspacioForm.height = 150;

                <%  		
                    EspacioFormVO objectVO = null;
                    List<EspacioFormVO> List = (List<EspacioFormVO>)request.getAttribute("listEspaciosForm");													
                    if (List!= null && List.size() >0){
                        for (int indice=0;indice<List.size();indice++)
                        {
                            objectVO = List.get(indice);

                %>
                    listaEspacioFormTabla[<%=indice%>] = ['<%=objectVO.getDescripcion().replaceAll("\n\r","<br>").replaceAll("\r","<br>").replaceAll("\n","<br>")%>','<%=objectVO.getSuperficie()%>'];
                    listaEspacioForm[<%=indice%>] = ['<%=objectVO.getId()%>','<%=objectVO.getDescripcion().replaceAll("\n\r","<br>").replaceAll("\r","<br>").replaceAll("\n","<br>")%>','<%=objectVO.getSuperficie()%>'];

                <%
                        }// for
                    }// if
                %>
                   
                tabEspacioForm.lineas=listaEspacioFormTabla;
                tabEspacioForm.displayTabla();
                if(navigator.appName.indexOf("Internet Explorer")!=-1){
                    try{
                        var div = document.getElementById('listaEspacioForm');
                        div.children[0].children[0].children[1].style.width = '100%';
                        div.children[1].style.width = '100%';
                    }
                    catch(err){

                    }
                }
            </script>
            <!-- Script Con Funciones-->
            <script type="text/javascript">

                function pulsarAltaEspacioForm(){
                    var control = new Date();
                    var result = null;
                    if(navigator.appName.indexOf("Internet Explorer")!=-1){
                        result = lanzarPopUpModal('<%=request.getContextPath()%>/PeticionModuloIntegracion.do?tarea=preparar&modulo=MELANBIDE45&operacion=cargarNuevoEspacioForm&tipo=0&numero=<%=numExpediente%>&control='+control.getTime(),300,900,'no','no');
                    }else{
                        result = lanzarPopUpModal('<%=request.getContextPath()%>/PeticionModuloIntegracion.do?tarea=preparar&modulo=MELANBIDE45&operacion=cargarNuevoEspacioForm&tipo=0&numero=<%=numExpediente%>&control='+control.getTime(),300,900,'no','no');
                    }
                    if (result != undefined){
                        if(result[0] == '0'){
                            recargarTablaEspacioForm(result);
                        }
                    }
                }

                function pulsarModificarEspacioForm(){
                    if(tabEspacioForm.selectedIndex != -1) {
                        var control = new Date();
                        var result = null;
                        if(navigator.appName.indexOf("Internet Explorer")!=-1){
                            result = lanzarPopUpModal('<%=request.getContextPath()%>/PeticionModuloIntegracion.do?tarea=preparar&modulo=MELANBIDE45&operacion=cargarModifEspacioForm&tipo=0&numero=<%=numExpediente%>&id='+listaEspacioForm[tabEspacioForm.selectedIndex][0]+'&control='+control.getTime(),300,900,'no','no');
                        }else{
                            result = lanzarPopUpModal('<%=request.getContextPath()%>/PeticionModuloIntegracion.do?tarea=preparar&modulo=MELANBIDE45&operacion=cargarModifEspacioForm&tipo=0&numero=<%=numExpediente%>&id='+listaEspacioForm[tabEspacioForm.selectedIndex][0]+'&control='+control.getTime(),300,900,'no','no');
                        }
                        if (result != undefined){
                            if(result[0] == '0'){
                                recargarTablaEspacioForm(result);
                            }
                        }
                    } 
                    else {
                        jsp_alerta('A', '<%=meLanbide45I18n.getMensaje(idiomaUsuario, "msg.msjNoSelecFila")%>');
                    }
                }

                function pulsarEliminarEspacioForm(){
                    if(tabEspacioForm.selectedIndex != -1) {
                        var resultado = jsp_alerta('', '<%=meLanbide45I18n.getMensaje(idiomaUsuario, "msg.preguntaEliminar")%>');
                        if (resultado == 1){

                            var ajax = getXMLHttpRequest();
                            var nodos = null;
                            var CONTEXT_PATH = '<%=request.getContextPath()%>'
                            var url = CONTEXT_PATH + "/PeticionModuloIntegracion.do";
                            var parametros = "";
                            var control = new Date();
                            parametros = 'tarea=preparar&modulo=MELANBIDE45&operacion=eliminarEspacioForm&tipo=0&numero=<%=numExpediente%>&id='+listaEspacioForm[tabEspacioForm.selectedIndex][0]+'&control='+control.getTime();
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
                                            else if(hijosFila[cont].nodeName=="EPF_DES"){
                                                if(hijosFila[cont].childNodes.length > 0){
                                                    fila[1] = hijosFila[cont].childNodes[0].nodeValue;
                                                }
                                                else{
                                                    fila[1] = '';
                                                }
                                            }
                                            else if(hijosFila[cont].nodeName=="EPF_SUP"){
                                                if(hijosFila[cont].childNodes.length > 0){
                                                    fila[2] = hijosFila[cont].childNodes[0].nodeValue;
                                                }
                                                else{
                                                    fila[2] = '-';
                                                }
                                            }
                                        }
                                        listaNueva[j] = fila;
                                        fila = new Array();
                                    }   
                                }//for(j=0;hijos!=null && j<hijos.length;j++)
                                if(codigoOperacion=="0"){
                                    recargarTablaEspacioForm(listaNueva);
                                }else if(codigoOperacion=="1"){
                                    jsp_alerta("A",'<%=meLanbide45I18n.getMensaje(idiomaUsuario,"error.errorBD")%>');
                                }else if(codigoOperacion=="2"){
                                    jsp_alerta("A",'<%=meLanbide45I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                                }else if(codigoOperacion=="3"){
                                    jsp_alerta("A",'<%=meLanbide45I18n.getMensaje(idiomaUsuario,"error.pasoParametros")%>');
                                }else{
                                        jsp_alerta("A",'<%=meLanbide45I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                                }//if(
                            }
                            catch(Err){
                                jsp_alerta("A",'<%=meLanbide45I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                            }//try-catch
                        }
                    } 
                    else {
                        jsp_alerta('A', '<%=meLanbide45I18n.getMensaje(idiomaUsuario, "msg.msjNoSelecFila")%>');
                    }
                }

                function recargarTablaEspacioForm(result){
                    var fila;
                    listaEspacioForm = new Array();
                    listaEspacioFormTabla = new Array();
                    for(var i = 1;i< result.length; i++){
                        fila = result[i];
                        listaEspacioForm[i-1] = fila;
                        listaEspacioFormTabla[i-1] = [fila[1].replace("\n\r","<br>").replace("\r","<br>").replace("\n","<br>"),fila[2]];
                    }
                    tabEspacioForm = new Tabla(document.getElementById('listaEspacioForm'), 762);
                    tabEspacioForm.addColumna('600','left',"<%= meLanbide45I18n.getMensaje(idiomaUsuario,"espacioformativo.tablaEspacioForm.col1")%>");
                    tabEspacioForm.addColumna('150','right',"<%= meLanbide45I18n.getMensaje(idiomaUsuario,"espacioformativo.tablaEspacioForm.col2")%>");

                    tabEspacioForm.displayCabecera=true;
                    tabEspacioForm.height = 150;
                    tabEspacioForm.lineas=listaEspacioFormTabla;
                    tabEspacioForm.displayTabla();

                    if(navigator.appName.indexOf("Internet Explorer")!=-1){
                        try{
                            var div = document.getElementById('listaEspacioForm');
                            div.children[0].children[0].children[1].style.width = '100%';
                            div.children[1].style.width = '100%';
                        }
                        catch(err){

                        }
                    }
                }        

            </script>
    </head>
    </body>
</html>

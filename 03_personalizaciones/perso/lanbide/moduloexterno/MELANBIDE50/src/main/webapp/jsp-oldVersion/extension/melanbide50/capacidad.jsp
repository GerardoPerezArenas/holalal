<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>

<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide50.i18n.MeLanbide50I18n" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.common.service.config.Config"%>
<%@page import="es.altia.common.service.config.ConfigServiceHelper"%>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide50.vo.capacidad.CapacidadVO"%>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide50.vo.especialidades.EspecialidadesVO" %>
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

            EspecialidadesVO datoEspecialidad = new EspecialidadesVO();
            if(request.getAttribute("datoEspecialidad") != null)
            {
                datoEspecialidad = (EspecialidadesVO)request.getAttribute("datoEspecialidad");
            }

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
                <!--<script type="text/javascript">tp1_p501 = tp1.addTabPage( document.getElementById( "tabPage501" ) );</script>-->
                <div style="clear: both; alignment-adjust: central;">
                    <label class="legendAzul" style=" text-align: center; width: 100%; position: relative; left: 25px;" ><%=meLanbide50I18n.getMensaje(idiomaUsuario, "capacidad.legend.titulo")%></label>
                        <div id="divGeneral"  style="overflow-y: auto; overflow-x: hidden; height: 420px;">     <!--onscroll="deshabilitarRadios();"-->
                            <div id="listaCapacidad" style="padding: 5px; width:905px; height: 300px; text-align: center; overflow-x: auto; overflow-y: auto;margin:0px;margin-top:0px;" align="center"></div>
                            <div class="botonera">
                                <input type="button" id="btnNuevaCapacidad" name="btnNuevaCapacidad" class="botonGeneral"  value="<%=meLanbide50I18n.getMensaje(idiomaUsuario, "btn.nuevo")%>" onclick="pulsarAltaCapacidad();">
                                <input type="button" id="btnEliminarCapacidad" name="btnEliminarCapacidad"   class="botonGeneral" value="<%=meLanbide50I18n.getMensaje(idiomaUsuario, "btn.eliminar")%>" onclick="pulsarEliminarCapacidad();">
                                <input type="button" id="btnModificarCapacidad" name="btnModificarCapacidad" class="botonGeneral" value="<%=meLanbide50I18n.getMensaje(idiomaUsuario, "btn.modificar")%>" onclick="pulsarModificarCapacidad();">
                            </div>
                        </div>
                </div>
            </div>
            <!--Script Ejecucion Elementos Pagina-->
            <script type="text/javascript">
                //Tabla Especialidades
                var tabCapacidad;
                var listaCapacidad = new Array();
                var listaCapacidadTabla = new Array();

                tabCapacidad = new Tabla(document.getElementById('listaCapacidad'), 870);
                tabCapacidad.addColumna('325','left',"<%= meLanbide50I18n.getMensaje(idiomaUsuario,"capacidad.tablaCapacidad.col1")%>");
                tabCapacidad.addColumna('375','left',"<%= meLanbide50I18n.getMensaje(idiomaUsuario,"capacidad.tablaCapacidad.col2")%>");
                tabCapacidad.addColumna('150','right',"<%= meLanbide50I18n.getMensaje(idiomaUsuario,"capacidad.tablaCapacidad.col3")%>");

                tabCapacidad.displayCabecera=true;
                tabCapacidad.height = 100;

                <%  		
                    CapacidadVO objectVO = null;
                    List<CapacidadVO> List = (List<CapacidadVO>)request.getAttribute("listCapacidad");													
                    if (List!= null && List.size() >0){
                        for (int indice=0;indice<List.size();indice++)
                        {
                            objectVO = List.get(indice);

                %>
                    listaCapacidadTabla[<%=indice%>] = ['<%=objectVO.getIdetificacionEspFor().replaceAll("\n\r","<br>").replaceAll("\r","<br>").replaceAll("\n","<br>")%>','<%=objectVO.getUbicacion().replaceAll("\n\r","<br>").replaceAll("\r","<br>").replaceAll("\n","<br>")%>','<%=objectVO.getSuperficie()%>'];
                    listaCapacidad[<%=indice%>] = ['<%=objectVO.getId()%>','<%=objectVO.getIdetificacionEspFor().replaceAll("\n\r","<br>").replaceAll("\r","<br>").replaceAll("\n","<br>")%>','<%=objectVO.getUbicacion().replaceAll("\n\r","<br>").replaceAll("\r","<br>").replaceAll("\n","<br>")%>','<%=objectVO.getSuperficie()%>'];

                <%
                        }// for
                    }// if
                %>
                   
                tabCapacidad.lineas=listaCapacidadTabla;
                tabCapacidad.displayTabla();
                if(navigator.appName.indexOf("Internet Explorer")!=-1){
                    try{
                        var div = document.getElementById('listaCapacidad');
                        div.children[0].children[0].children[0].children[1].style.width = '100%';
                        div.children[0].children[1].style.width = '100%';
                    }
                    catch(err){

                    }
                }
            </script>
            <!-- Script Con Funciones-->
            <script type="text/javascript">

                function pulsarAltaCapacidad(){
                    var control = new Date();
                    var result = null;
                    if(navigator.appName.indexOf("Internet Explorer")!=-1){
                        result = lanzarPopUpModal('<%=request.getContextPath()%>/PeticionModuloIntegracion.do?tarea=preparar&modulo=MELANBIDE50&operacion=cargarNuevaCapacidad&tipo=0&numero=<%=numExpediente%>&control='+control.getTime()+'&idEpsol=<%=datoEspecialidad.getId()%>',350,900,'no','no');
                    }else{
                        result = lanzarPopUpModal('<%=request.getContextPath()%>/PeticionModuloIntegracion.do?tarea=preparar&modulo=MELANBIDE50&operacion=cargarNuevaCapacidad&tipo=0&numero=<%=numExpediente%>&control='+control.getTime()+'&idEpsol=<%=datoEspecialidad.getId()%>',350,900,'no','no');
                    }
                    if (result != undefined){
                        if(result[0] == '0'){
                            recargarTablaCapacidad(result);
                        }
                    }
                }

                function pulsarModificarCapacidad(){
                    if(tabCapacidad.selectedIndex != -1) {
                        var control = new Date();
                        var result = null;
                        if(navigator.appName.indexOf("Internet Explorer")!=-1){
                            result = lanzarPopUpModal('<%=request.getContextPath()%>/PeticionModuloIntegracion.do?tarea=preparar&modulo=MELANBIDE50&operacion=cargarModifCapacidad&tipo=0&numero=<%=numExpediente%>&id='+listaCapacidad[tabCapacidad.selectedIndex][0]+'&control='+control.getTime()+'&idEpsol=<%=datoEspecialidad.getId()%>',350,900,'no','no');
                        }else{
                            result = lanzarPopUpModal('<%=request.getContextPath()%>/PeticionModuloIntegracion.do?tarea=preparar&modulo=MELANBIDE50&operacion=cargarModifCapacidad&tipo=0&numero=<%=numExpediente%>&id='+listaCapacidad[tabCapacidad.selectedIndex][0]+'&control='+control.getTime()+'&idEpsol=<%=datoEspecialidad.getId()%>',350,900,'no','no');
                        }
                        if (result != undefined){
                            if(result[0] == '0'){
                                recargarTablaCapacidad(result);
                            }
                        }
                    } 
                    else {
                        jsp_alerta('A', '<%=meLanbide50I18n.getMensaje(idiomaUsuario, "msg.msjNoSelecFila")%>');
                    }
                }

                function pulsarEliminarCapacidad(){
                    if(tabCapacidad.selectedIndex != -1) {
                        var resultado = jsp_alerta('', '<%=meLanbide50I18n.getMensaje(idiomaUsuario, "msg.preguntaEliminar")%>');
                        if (resultado == 1){

                            var ajax = getXMLHttpRequest();
                            var nodos = null;
                            var CONTEXT_PATH = '<%=request.getContextPath()%>'
                            var url = CONTEXT_PATH + "/PeticionModuloIntegracion.do";
                            var parametros = "";
                            var control = new Date();
                            parametros = 'tarea=preparar&modulo=MELANBIDE50&operacion=eliminarCapacidad&tipo=0&numero=<%=numExpediente%>&id='+listaCapacidad[tabCapacidad.selectedIndex][0]+'&control='+control.getTime()
                                         +'&idEpsol=<%=datoEspecialidad.getId()%>';
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
                                            else if(hijosFila[cont].nodeName=="CAIN_NUM"){
                                                if(hijosFila[cont].childNodes.length > 0){
                                                    fila[1] = hijosFila[cont].childNodes[0].nodeValue;
                                                }
                                                else{
                                                    fila[1] = '-';
                                                }
                                            }
                                            else if(hijosFila[cont].nodeName=="CAIN_IDEF"){
                                                if(hijosFila[cont].childNodes.length > 0){
                                                    fila[2] = hijosFila[cont].childNodes[0].nodeValue;
                                                }
                                                else{
                                                    fila[2] = '-';
                                                }
                                            }
                                            else if(hijosFila[cont].nodeName=="CAIN_UBI"){
                                                if(hijosFila[cont].childNodes.length > 0){
                                                    fila[3] = hijosFila[cont].childNodes[0].nodeValue;
                                                }
                                                else{
                                                    fila[3] = '-';
                                                }
                                            }
                                            else if(hijosFila[cont].nodeName=="CAIN_SUP"){
                                                if(hijosFila[cont].childNodes.length > 0){
                                                    fila[4] = hijosFila[cont].childNodes[0].nodeValue;
                                                }
                                                else{
                                                    fila[4] = '-';
                                                }
                                            }
                                            if(hijosFila[cont].nodeName=="ID_ESPSOL"){
                                                if(hijosFila[cont].childNodes.length > 0){
                                                    fila[5] = hijosFila[cont].childNodes[0].nodeValue;
                                                }
                                                else{
                                                    fila[5] = '-';
                                                }
                                            }
                                        }
                                        listaNueva[j] = fila;
                                        fila = new Array();
                                    }   
                                }//for(j=0;hijos!=null && j<hijos.length;j++)
                                if(codigoOperacion=="0"){
                                    recargarTablaCapacidad(listaNueva);
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

                function recargarTablaCapacidad(result){
                    var fila;
                    listaCapacidad = new Array();
                    listaCapacidadTabla = new Array();
                    for(var i = 1;i< result.length; i++){
                        fila = result[i];
                        listaCapacidad[i-1] = fila;
                        listaCapacidadTabla[i-1] = [fila[2].replace("\n\r","<br>").replace("\r","<br>").replace("\n","<br>"), fila[3].replace("\n\r","<br>").replace("\r","<br>").replace("\n","<br>"),fila[4]];
                    }
                    tabCapacidad = new Tabla(document.getElementById('listaCapacidad'), 870);
                    tabCapacidad.addColumna('325','left',"<%= meLanbide50I18n.getMensaje(idiomaUsuario,"capacidad.tablaCapacidad.col1")%>");
                    tabCapacidad.addColumna('375','left',"<%= meLanbide50I18n.getMensaje(idiomaUsuario,"capacidad.tablaCapacidad.col2")%>");
                    tabCapacidad.addColumna('150','right',"<%= meLanbide50I18n.getMensaje(idiomaUsuario,"capacidad.tablaCapacidad.col3")%>");


                    tabCapacidad.displayCabecera=true;
                    tabCapacidad.height = 100;
                    tabCapacidad.lineas=listaCapacidadTabla;
                    tabCapacidad.displayTabla();

                    if(navigator.appName.indexOf("Internet Explorer")!=-1){
                        try{
                            var div = document.getElementById('listaCapacidad');
                            div.children[0].children[0].children[0].children[1].style.width = '100%';
                            div.children[0].children[1].style.width = '100%';
                        }
                        catch(err){

                        }
                    }
                }        

            </script>
    </head>
    </body>
</html>
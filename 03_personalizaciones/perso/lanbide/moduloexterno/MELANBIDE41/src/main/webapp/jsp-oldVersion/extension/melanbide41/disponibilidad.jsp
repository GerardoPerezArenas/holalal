<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>

<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide41.i18n.MeLanbide41I18n" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.common.service.config.Config"%>
<%@page import="es.altia.common.service.config.ConfigServiceHelper"%>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide41.vo.disponibilidad.DisponibilidadVO"%>
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
            MeLanbide41I18n meLanbide41I18n = MeLanbide41I18n.getInstance();

            Config m_Config = ConfigServiceHelper.getConfig("common");
            String statusBar = m_Config.getString("JSP.StatusBar");
            String nombreModulo     = request.getParameter("nombreModulo");
            String codOrganizacion  = request.getParameter("codOrganizacionModulo");
            String numExpediente    = request.getParameter("numero");

            %>
    
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/estilo.css"/>
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/extension/melanbide41/melanbide41.css"/>
    
    <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/general.js"></script>
    <script type="text/javascript">
        var APP_CONTEXT_PATH = '<%=request.getContextPath()%>';
    </script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/validaciones.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/popup.js"></script>

    <body>
            <div class="tab-page" style="height:420px; width: 98%;">
                <div style="clear: both;">
                    <label class="legendAzul" style="text-align: center; position: relative; left: 5px;"><%=meLanbide41I18n.getMensaje(idiomaUsuario, "disponibilidad.legend.titulo")%></label>
                        <div id="divGeneral"  style="overflow-y: auto; overflow-x: hidden; height: 440px;">     <!--onscroll="deshabilitarRadios();"-->
                            <div id="listaDisponibilidad" style="padding: 5px; width:900px; height: 300px; text-align: center; overflow-x: auto; overflow-y: auto;margin:0px;margin-top:0px;" align="center"></div>
                            <div class="botonera">
                                <input type="button" id="btnNuevaDisponibilidad" name="btnNuevaDisponibilidad" class="botonGeneral" style="display: none"  value="<%=meLanbide41I18n.getMensaje(idiomaUsuario, "btn.nuevo")%>" onclick="pulsarAltaDisponibilidad();">
                                <input type="button" id="btnEliminarDisponibilidad" name="btnEliminarDisponibilidad"   class="botonGeneral" style="display: none" value="<%=meLanbide41I18n.getMensaje(idiomaUsuario, "btn.eliminar")%>" onclick="pulsarEliminarDisponibilidad();">
                                <input type="button" id="btnModificarDisponibilidad" name="btnModificarDisponibilidad" class="botonGeneral" value="<%=meLanbide41I18n.getMensaje(idiomaUsuario, "btn.modificar")%>" onclick="pulsarModificarDisponibilidad();">
                            </div>
                        </div>
                </div>
            </div>
            <!--Script Ejecucion Elementos Pagina-->
            <script type="text/javascript">
                //Tabla Especialidades
                var tabDisponibilidad;
                var listaDisponibilidad = new Array();
                var listaDisponibilidadTabla = new Array();
                
                tabDisponibilidad = new Tabla(document.getElementById('listaDisponibilidad'), 1010);
                tabDisponibilidad.addColumna('100','center',"<%= meLanbide41I18n.getMensaje(idiomaUsuario,"disponibilidad.tablaEspecialidades.col1")%>");
                tabDisponibilidad.addColumna('270','left',"<%= meLanbide41I18n.getMensaje(idiomaUsuario,"disponibilidad.tablaEspecialidades.col2")%>");
                tabDisponibilidad.addColumna('290','left',"<%= meLanbide41I18n.getMensaje(idiomaUsuario,"disponibilidad.tablaEspecialidades.col3")%>");
                tabDisponibilidad.addColumna('75','right',"<%= meLanbide41I18n.getMensaje(idiomaUsuario,"disponibilidad.tablaEspecialidades.col4")%>");
                tabDisponibilidad.addColumna('75','right',"<%= meLanbide41I18n.getMensaje(idiomaUsuario,"disponibilidad.tablaEspecialidades.col5")%>");
                tabDisponibilidad.addColumna('75','right',"<%= meLanbide41I18n.getMensaje(idiomaUsuario,"disponibilidad.tablaEspecialidades.col6")%>");
                tabDisponibilidad.addColumna('75','right',"<%= meLanbide41I18n.getMensaje(idiomaUsuario,"disponibilidad.tablaEspecialidades.col7")%>");

                tabDisponibilidad.displayCabecera=true;
                tabDisponibilidad.height = 100;

                <%  		
                    DisponibilidadVO objectVO = null;
                    List<DisponibilidadVO> List = (List<DisponibilidadVO>)request.getAttribute("listDisponibilidad");													
                    if (List!= null && List.size() >0){
                        for (int indice=0;indice<List.size();indice++)
                        {
                            objectVO = List.get(indice);

                %>
                    var getPropiedadCedidos = '<%= objectVO.getPropiedadCedidos() != null ? objectVO.getPropiedadCedidos().replaceAll("\n\r","<br>").replaceAll("\r","<br>").replaceAll("\n","<br>") : objectVO.getPropiedadCedidos() %>';
                    if (getPropiedadCedidos == "null")
                        getPropiedadCedidos="";
                    var getSituados = '<%=objectVO.getSituados() != null ? objectVO.getSituados().replaceAll("\n\r","<br>").replaceAll("\r","<br>").replaceAll("\n","<br>") : objectVO.getSituados() %>';
                    if (getSituados == "null")
                        getSituados="";
                    var getSupAulas = '<%=objectVO.getSupAulas()%>';
                    if (getSupAulas == "null")
                        getSupAulas="";
                    var getSupTaller = '<%=objectVO.getSupTaller()%>';
                    if (getSupTaller == "null")
                        getSupTaller="";
                    var getSupAulaTaller = '<%=objectVO.getSupAulaTaller()%>';
                    if (getSupAulaTaller == "null")
                        getSupAulaTaller="";
                    var getSupCampoPract = '<%=objectVO.getSupCampoPract()%>';
                    if (getSupCampoPract == "null")
                        getSupCampoPract="";

                    listaDisponibilidadTabla[<%=indice%>] = ['<%=objectVO.getCodCp()%>',getPropiedadCedidos,getSituados,getSupAulas,getSupTaller,getSupAulaTaller,getSupCampoPract];
                    listaDisponibilidad[<%=indice%>] = ['<%=objectVO.getId()%>','<%=objectVO.getIdEspSol()%>','<%=objectVO.getCodCp()%>',getPropiedadCedidos,getSituados,getSupAulas,getSupTaller,getSupAulaTaller,getSupCampoPract];

                <%
                        }// for
                    }// if
                %>
                   
                tabDisponibilidad.lineas=listaDisponibilidadTabla;
                tabDisponibilidad.displayTabla();
                if(navigator.appName.indexOf("Internet Explorer")!=-1){
                    try{
                        var div = document.getElementById('listaDisponibilidad');
                        div.children[0].children[0].children[0].children[0].children[0].children[0].children[0].children[1].style.width = '100%';
                        div.children[0].children[1].children[0].children[0].children[0].children[1].style.width = '100%';
                    }
                    catch(err){

                    }
                }
            </script>
            <!-- Script Con Funciones-->
            <script type="text/javascript">

                function pulsarAltaDisponibilidad(){
                    var control = new Date();
                    var result = null;
                    if(navigator.appName.indexOf("Internet Explorer")!=-1){
                        result = lanzarPopUpModal('<%=request.getContextPath()%>/PeticionModuloIntegracion.do?tarea=preparar&modulo=MELANBIDE41&operacion=cargarNuevaEspecialdad&tipo=0&numero=<%=numExpediente%>&control='+control.getTime(),500,850,'no','no');
                    }else{
                        result = lanzarPopUpModal('<%=request.getContextPath()%>/PeticionModuloIntegracion.do?tarea=preparar&modulo=MELANBIDE41&operacion=cargarNuevaEspecialdad&tipo=0&numero=<%=numExpediente%>&control='+control.getTime(),500,850,'no','no');
                    }
                    if (result != undefined){
                        if(result[0] == '0'){
                            recargarTablaDisponibilidad(result);
                        }
                    }
                }

                function pulsarModificarDisponibilidad(){
                    if(tabDisponibilidad.selectedIndex != -1) {
                        var control = new Date();
                        var result = null;
                        if(navigator.appName.indexOf("Internet Explorer")!=-1){
                            result = lanzarPopUpModal('<%=request.getContextPath()%>/PeticionModuloIntegracion.do?tarea=preparar&modulo=MELANBIDE41&operacion=cargarModifDisponibilidad&tipo=0&numero=<%=numExpediente%>&id='+listaDisponibilidad[tabDisponibilidad.selectedIndex][0]+'&idespsol='+listaDisponibilidad[tabDisponibilidad.selectedIndex][1]+'&control='+control.getTime(),500,850,'no','no');
                        }else{
                            result = lanzarPopUpModal('<%=request.getContextPath()%>/PeticionModuloIntegracion.do?tarea=preparar&modulo=MELANBIDE41&operacion=cargarModifDisponibilidad&tipo=0&numero=<%=numExpediente%>&id='+listaDisponibilidad[tabDisponibilidad.selectedIndex][0]+'&idespsol='+listaDisponibilidad[tabDisponibilidad.selectedIndex][1]+'&control='+control.getTime(),500,850,'no','no');
                        }
                        if (result != undefined){
                            if(result[0] == '0'){
                                recargarTablaDisponibilidad(result);
                            }
                        }
                    } 
                    else {
                        jsp_alerta('A', '<%=meLanbide41I18n.getMensaje(idiomaUsuario, "msg.msjNoSelecFila")%>');
                    }
                }

                function pulsarEliminarDisponibilidad(){
                    if(tabDisponibilidad.selectedIndex != -1) {
                        var resultado = jsp_alerta('', '<%=meLanbide41I18n.getMensaje(idiomaUsuario, "msg.preguntaEliminar")%>');
                        if (resultado == 1){

                            var ajax = getXMLHttpRequest();
                            var nodos = null;
                            var CONTEXT_PATH = '<%=request.getContextPath()%>'
                            var url = CONTEXT_PATH + "/PeticionModuloIntegracion.do";
                            var parametros = "";
                            var control = new Date();
                            parametros = 'tarea=preparar&modulo=MELANBIDE41&operacion=eliminarDisponibilidad&tipo=0&numero=<%=numExpediente%>&id='+listaDisponibilidad[tabDisponibilidad.selectedIndex][0]+'&control='+control.getTime();
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
                                    jsp_alerta("A",'<%=meLanbide41I18n.getMensaje(idiomaUsuario,"error.errorBD")%>');
                                }else if(codigoOperacion=="2"){
                                    jsp_alerta("A",'<%=meLanbide41I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                                }else if(codigoOperacion=="3"){
                                    jsp_alerta("A",'<%=meLanbide41I18n.getMensaje(idiomaUsuario,"error.pasoParametros")%>');
                                }else{
                                        jsp_alerta("A",'<%=meLanbide41I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                                }//if(
                            }
                            catch(Err){
                                jsp_alerta("A",'<%=meLanbide41I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                            }//try-catch
                        }
                    } 
                    else {
                        jsp_alerta('A', '<%=meLanbide41I18n.getMensaje(idiomaUsuario, "msg.msjNoSelecFila")%>');
                    }
                }

                function recargarTablaDisponibilidad(result){
                    var fila;
                    listaDisponibilidad = new Array();
                    listaDisponibilidadTabla = new Array();
                    for(var i = 1;i< result.length; i++){
                        fila = result[i];
                        listaDisponibilidad[i-1] = fila;
                        listaDisponibilidadTabla[i-1] = [fila[3],(fila[4]=="null")?'':fila[4].replace("\n\r","<br>").replace("\r","<br>").replace("\n","<br>"),(fila[5]=="null")?'':fila[5].replace("\n\r","<br>").replace("\r","<br>").replace("\n","<br>"),(fila[6]=="null")?'':fila[6],(fila[7]=="null")?'':fila[7],(fila[8]=="null")?'':fila[8],(fila[9]=="null")?'':fila[9]];
                    }
                    tabDisponibilidad = new Tabla(document.getElementById('listaDisponibilidad'), 1010);
                    tabDisponibilidad.addColumna('100','center',"<%= meLanbide41I18n.getMensaje(idiomaUsuario,"disponibilidad.tablaEspecialidades.col1")%>");
                    tabDisponibilidad.addColumna('270','left',"<%= meLanbide41I18n.getMensaje(idiomaUsuario,"disponibilidad.tablaEspecialidades.col2")%>");
                    tabDisponibilidad.addColumna('290','left',"<%= meLanbide41I18n.getMensaje(idiomaUsuario,"disponibilidad.tablaEspecialidades.col3")%>");
                    tabDisponibilidad.addColumna('75','right',"<%= meLanbide41I18n.getMensaje(idiomaUsuario,"disponibilidad.tablaEspecialidades.col4")%>");
                    tabDisponibilidad.addColumna('75','right',"<%= meLanbide41I18n.getMensaje(idiomaUsuario,"disponibilidad.tablaEspecialidades.col5")%>");
                    tabDisponibilidad.addColumna('75','right',"<%= meLanbide41I18n.getMensaje(idiomaUsuario,"disponibilidad.tablaEspecialidades.col6")%>");
                    tabDisponibilidad.addColumna('75','right',"<%= meLanbide41I18n.getMensaje(idiomaUsuario,"disponibilidad.tablaEspecialidades.col7")%>");

                    tabDisponibilidad.displayCabecera=true;
                    tabDisponibilidad.height = 100;
                    tabDisponibilidad.lineas=listaDisponibilidadTabla;
                    tabDisponibilidad.displayTabla();
                    
                    if(navigator.appName.indexOf("Internet Explorer")!=-1){
                        try{
                            var div = document.getElementById('listaDisponibilidad');
                            div.children[0].children[0].children[0].children[0].children[0].children[0].children[0].children[1].style.width = '100%';
                            div.children[0].children[1].children[0].children[0].children[0].children[1].style.width = '100%';
                        }
                        catch(err){
                        }
                    }
                }        

            </script>
    </head>
    </body>
</html>

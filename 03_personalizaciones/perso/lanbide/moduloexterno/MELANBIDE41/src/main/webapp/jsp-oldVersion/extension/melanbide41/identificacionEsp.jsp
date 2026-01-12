<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>

<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide41.i18n.MeLanbide41I18n" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.common.service.config.Config"%>
<%@page import="es.altia.common.service.config.ConfigServiceHelper"%>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide41.vo.identificacionesp.IdentificacionEspVO"%>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide41.vo.especialidades.EspecialidadesVO" %>
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
            String urlNuevaIdentificacionEsp = (String)request.getAttribute("urlNuevaIdentificacionEsp");
            EspecialidadesVO datoEspecialidad = new EspecialidadesVO();
            if(request.getAttribute("datoEspecialidad") != null)
            {
                datoEspecialidad = (EspecialidadesVO)request.getAttribute("datoEspecialidad");
            }

            %>
    
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/estilo.css"/>
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/extension/melanbide41/melanbide41.css"/>
    
    <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/general.js"></script>
    <script type="text/javascript">
        var APP_CONTEXT_PATH = '<%=request.getContextPath()%>';
    </script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/validaciones.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/listaComboBox.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/popup.js"></script>


    <body>
            <div class="tab-page" id="tabPage411" style="height:420px; width: 90%;">
                <div style="clear: both;">
                    <label class="legendAzul" style="text-align: center; position: relative; left: 28%;"><%=meLanbide41I18n.getMensaje(idiomaUsuario, "identificacionesp.legend.titulo")%></label>
                        <div id="divGeneral"  style="overflow-y: auto; overflow-x: hidden; height: 420px;">     <!--onscroll="deshabilitarRadios();"-->
                            <div id="listaIdentificacionEsp" style=" display: none; padding: 5px; width:900px; height: 300px; text-align: center; overflow-x: auto; overflow-y: auto;margin:0px;margin-top:0px;" align="center"></div>
                            <div>
                                <jsp:include page="<%=urlNuevaIdentificacionEsp%>" flush="true"/>
                            </div>
                            <div class="botonera">
                                <input type="button" id="btnNuevaIdentificacionEsp" name="btnNuevaIdentificacionEsp" style="display: none" class="botonGeneral" value="<%=meLanbide41I18n.getMensaje(idiomaUsuario, "btn.nuevo")%>" onclick="pulsarAltaIdentificacionEsp();">
                                <input type="button" id="btnEliminarIdentificacionEsp" name="btnEliminarIdentificacionEsp"   style="display: none" class="botonGeneral" value="<%=meLanbide41I18n.getMensaje(idiomaUsuario, "btn.eliminar")%>" onclick="pulsarEliminarIdentificacionEsp();">
                                <input type="button" id="btnModificarIdentificacionEsp" name="btnModificarIdentificacionEsp" style="display: none" class="botonGeneral" value="<%=meLanbide41I18n.getMensaje(idiomaUsuario, "btn.modificar")%>" onclick="pulsarModificarIdentificacionEsp();">
                            </div>
                        </div>
                </div>
            </div>
            <!--Script Ejecucion Elementos Pagina-->
            <script type="text/javascript">
                //Tabla Identifiacacnioes
                var tabIdentificacionEsp;
                var listaIdentificacionEsp = new Array();
                var listaIdentificacionEspTabla = new Array();
                
                tabIdentificacionEsp = new Tabla(document.getElementById('listaIdentificacionEsp'), 2000);
                tabIdentificacionEsp.addColumna('100','center',"<%=meLanbide41I18n.getMensaje(idiomaUsuario,"identificacionesp.tablaIdentificacionEsp.col1")%>");
                tabIdentificacionEsp.addColumna('500','left',"<%=meLanbide41I18n.getMensaje(idiomaUsuario,"identificacionesp.tablaIdentificacionEsp.col2")%>");
                tabIdentificacionEsp.addColumna('150','right',"<%=meLanbide41I18n.getMensaje(idiomaUsuario,"identificacionesp.tablaIdentificacionEsp.col3")%>");
                tabIdentificacionEsp.addColumna('150','right',"<%=meLanbide41I18n.getMensaje(idiomaUsuario,"identificacionesp.tablaIdentificacionEsp.col4")%>");
                tabIdentificacionEsp.addColumna('200','center',"<%=meLanbide41I18n.getMensaje(idiomaUsuario,"identificacionesp.tablaIdentificacionEsp.col5")%>");
                tabIdentificacionEsp.addColumna('400','left',"<%=meLanbide41I18n.getMensaje(idiomaUsuario,"identificacionesp.tablaIdentificacionEsp.col6")%>");
                tabIdentificacionEsp.addColumna('400','left',"<%=meLanbide41I18n.getMensaje(idiomaUsuario,"identificacionesp.tablaIdentificacionEsp.col7")%>");

                tabIdentificacionEsp.displayCabecera=true;
                tabIdentificacionEsp.height = 100;

                <%  		
                    IdentificacionEspVO objectVO = null;
                    List<IdentificacionEspVO> List = (List<IdentificacionEspVO>)request.getAttribute("listaIdentificacionEsp");													
                    if (List!= null && List.size() >0){
                        for (int indice=0;indice<List.size();indice++)
                        {
                            objectVO = List.get(indice);

                %>
                    var getHoras = '<%=objectVO.getHoras()%>';
                    if(getHoras=="null")
                        getHoras="";
                    var getAlumnos = '<%=objectVO.getAlumnos()%>';
                    if(getAlumnos=="null")
                        getAlumnos="";
                    var getCertPro= '<%=objectVO.getCertPro()%>';
                    if(getCertPro=="null")
                        getCertPro="";
                    else if(getCertPro=="0")
                        getCertPro='S';
                    else if(getCertPro=="1")
                        getCertPro='N';
                    else
                        getCertPro="";
                    var getRealDecRegu = '<%=objectVO.getRealDecRegu()!=null ? objectVO.getRealDecRegu().replaceAll("\n\r","<br>").replaceAll("\r","<br>").replaceAll("\n","<br>") : objectVO.getRealDecRegu()%>';
                    if(getRealDecRegu=="null")
                        getRealDecRegu="";
                    var getBoeFecPub ='<%=objectVO.getBoeFecPub() != null ? objectVO.getBoeFecPub().replaceAll("\n\r","<br>").replaceAll("\r","<br>").replaceAll("\n","<br>") : objectVO.getBoeFecPub()%>';
                    if(getBoeFecPub=="null")
                        getBoeFecPub="";
                    listaIdentificacionEspTabla[<%=indice%>] = ['<%=objectVO.getCodCp()%>','<%=objectVO.getDenomEsp()%>',getHoras,getAlumnos,getCertPro,getRealDecRegu,getBoeFecPub];
                    listaIdentificacionEsp[<%=indice%>] = ['<%=objectVO.getId()%>','<%=objectVO.getIdEspSol()%>','<%=objectVO.getCodCp()%>','<%=objectVO.getDenomEsp()%>',getHoras,getAlumnos,getCertPro,getRealDecRegu,getBoeFecPub];
                <%
                        }// for
                    }// if
                %>
                   
                tabIdentificacionEsp.lineas=listaIdentificacionEspTabla;
                tabIdentificacionEsp.displayTabla();
                if(navigator.appName.indexOf("Internet Explorer")!=-1){
                    try{
                        var div = document.getElementById('listaIdentificacionEsp');
                        div.children[0].children[0].children[0].children[0].children[0].children[0].children[0].children[0].children[1].style.width = '100%';
                        div.children[0].children[1].children[0].children[0].children[0].children[0].children[1].style.width = '100%';
                    }
                    catch(err){

                    }
                }
            </script>
            <!-- Script Con Funciones-->
            <script type="text/javascript">

                function pulsarAltaIdentificacionEsp(){
                    var control = new Date();
                    var result = null;
                    if(navigator.appName.indexOf("Internet Explorer")!=-1){
                        result = lanzarPopUpModal('<%=request.getContextPath()%>/PeticionModuloIntegracion.do?tarea=preparar&modulo=MELANBIDE41&operacion=cargarNuevaIdentificacionEsp&tipo=0&numero=<%=numExpediente%>&control='+control.getTime(),400,850,'no','no');
                    }else{
                        result = lanzarPopUpModal('<%=request.getContextPath()%>/PeticionModuloIntegracion.do?tarea=preparar&modulo=MELANBIDE41&operacion=cargarNuevaIdentificacionEsp&tipo=0&numero=<%=numExpediente%>&control='+control.getTime(),400,850,'no','no');
                    }
                    if (result != undefined){
                        if(result[0] == '0'){
                            recargarTablaIdentificacionEsp(result);
                        }
                    }
                }

                function pulsarModificarIdentificacionEsp(){
                    if(tabIdentificacionEsp.selectedIndex != -1) {
                        var control = new Date();
                        var result = null;
                        if(navigator.appName.indexOf("Internet Explorer")!=-1){
                            result = lanzarPopUpModal('<%=request.getContextPath()%>/PeticionModuloIntegracion.do?tarea=preparar&modulo=MELANBIDE41&operacion=cargarModifIdentificacionEsp&tipo=0&numero=<%=numExpediente%>&id='+listaIdentificacionEsp[tabIdentificacionEsp.selectedIndex][0]+'&idEspSol='+listaIdentificacionEsp[tabIdentificacionEsp.selectedIndex][1]+'&control='+control.getTime(),400,850,'no','no');
                        }else{
                            result = lanzarPopUpModal('<%=request.getContextPath()%>/PeticionModuloIntegracion.do?tarea=preparar&modulo=MELANBIDE41&operacion=cargarModifIdentificacionEsp&tipo=0&numero=<%=numExpediente%>&id='+listaIdentificacionEsp[tabIdentificacionEsp.selectedIndex][0]+'&idEspSol='+listaIdentificacionEsp[tabIdentificacionEsp.selectedIndex][1]+'&control='+control.getTime(),400,850,'no','no');
                        }
                        if (result != undefined){
                            if(result[0] == '0'){
                                recargarTablaIdentificacionEsp(result);
                            }
                        }
                    } 
                    else {
                        jsp_alerta('A', '<%=meLanbide41I18n.getMensaje(idiomaUsuario, "msg.msjNoSelecFila")%>');
                    }
                }

                function pulsarEliminarIdentificacionEsp(){
                    if(tabIdentificacionEsp.selectedIndex != -1) {
                        var resultado = jsp_alerta('', '<%=meLanbide41I18n.getMensaje(idiomaUsuario, "msg.preguntaEliminar")%>');
                        if (resultado == 1){

                            var ajax = getXMLHttpRequest();
                            var nodos = null;
                            var CONTEXT_PATH = '<%=request.getContextPath()%>'
                            var url = CONTEXT_PATH + "/PeticionModuloIntegracion.do";
                            var parametros = "";
                            var control = new Date();
                            parametros = 'tarea=preparar&modulo=MELANBIDE41&operacion=eliminarIdentificacionEsp&tipo=0&numero=<%=numExpediente%>&id='+listaIdentificacionEsp[tabIdentificacionEsp.selectedIndex][0]+'&control='+control.getTime();
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

                function recargarTablaIdentificacionEsp(result){
                    var fila;
                    listaIdentificacionEsp = new Array();
                    listaIdentificacionEspTabla = new Array();
                    for(var i = 1;i< result.length; i++){
                        fila = result[i];
                        listaIdentificacionEsp[i-1] = fila;
                        listaIdentificacionEspTabla[i-1] = [fila[3],fila[4],(fila[5]=="null")?'':fila[5],(fila[6]=="null")?'':fila[6],(fila[7]=="null")?'':fila[7]==0 ? 'S' : fila[7]==1 ? 'N' : '',(fila[8]=="null")?'':fila[8].replace("\n\r","<br>").replace("\r","<br>").replace("\n","<br>"),(fila[9]=="null")?'':fila[9].replace("\n\r","<br>").replace("\r","<br>").replace("\n","<br>")];
                    }
                    tabIdentificacionEsp = new Tabla(document.getElementById('listaIdentificacionEsp'), 2000);
                    tabIdentificacionEsp.addColumna('100','center',"<%=meLanbide41I18n.getMensaje(idiomaUsuario,"identificacionesp.tablaIdentificacionEsp.col1")%>");
                    tabIdentificacionEsp.addColumna('500','left',"<%=meLanbide41I18n.getMensaje(idiomaUsuario,"identificacionesp.tablaIdentificacionEsp.col2")%>");
                    tabIdentificacionEsp.addColumna('150','right',"<%=meLanbide41I18n.getMensaje(idiomaUsuario,"identificacionesp.tablaIdentificacionEsp.col3")%>");
                    tabIdentificacionEsp.addColumna('150','right',"<%=meLanbide41I18n.getMensaje(idiomaUsuario,"identificacionesp.tablaIdentificacionEsp.col4")%>");
                    tabIdentificacionEsp.addColumna('200','center',"<%=meLanbide41I18n.getMensaje(idiomaUsuario,"identificacionesp.tablaIdentificacionEsp.col5")%>");
                    tabIdentificacionEsp.addColumna('400','left',"<%=meLanbide41I18n.getMensaje(idiomaUsuario,"identificacionesp.tablaIdentificacionEsp.col6")%>");
                    tabIdentificacionEsp.addColumna('400','left',"<%=meLanbide41I18n.getMensaje(idiomaUsuario,"identificacionesp.tablaIdentificacionEsp.col7")%>");

                    tabIdentificacionEsp.displayCabecera=true;
                    tabIdentificacionEsp.height = 100;
                    tabIdentificacionEsp.lineas=listaIdentificacionEspTabla;
                    tabIdentificacionEsp.displayTabla();
                    
                    if(navigator.appName.indexOf("Internet Explorer")!=-1){
                        try{
                            var div = document.getElementById('listaIdentificacionEsp');
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

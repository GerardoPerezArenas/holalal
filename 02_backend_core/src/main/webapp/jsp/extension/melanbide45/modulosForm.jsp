<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>

<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide45.i18n.MeLanbide45I18n" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.common.service.config.Config"%>
<%@page import="es.altia.common.service.config.ConfigServiceHelper"%>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide45.vo.moduloform.ModuloFormVO"%>
<%@page import="java.util.ArrayList" %>
<%@page import="java.util.List" %>

<!--html>
    <head-->
        <%
            int idiomaUsuario = 1;
 int apl = 5;
 String css = "";
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
	 apl = usuario.getAppCod();
	 css = usuario.getCss();
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
    
    <jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
<jsp:setProperty name="descriptor"  property="idi_cod" value="<%=idiomaUsuario%>" />
<jsp:setProperty name="descriptor"  property="apl_cod" value="<%=apl%>" />
<!--link rel="StyleSheet" media="screen" type="text/css" href="<%=request.getContextPath()%><%=css%>">
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/>
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/extension/melanbide45/melanbide45.css"/-->
    <script type="text/javascript">
        var APP_CONTEXT_PATH = '<%=request.getContextPath()%>';
    </script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/validaciones.js"></script>
    
    <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/popup.js"></script>
	<!--/head>
    <body-->
            <div class="tab-page" style="height:420px; width: 91%;">
                <div style="clear: both;">
                    <label class="legendAzul" style="text-align: center; position: relative; left: 5px;"><%=meLanbide45I18n.getMensaje(idiomaUsuario, "legend.moduloformativo.titulo")%></label>
                        <div id="divGeneral"  style="overflow-y: auto; overflow-x: hidden; height: 440px;">     <!--onscroll="deshabilitarRadios();"-->
                            <div id="listaModuloForm"  align="center"></div>
                            <div class="botonera">
                                <input type="button" id="btnNuevoModuloForm" name="btnNuevoModuloForm" class="botonGeneral"  value="<%=meLanbide45I18n.getMensaje(idiomaUsuario, "btn.nuevo")%>" onclick="pulsarAltaModuloForm();">
                                <input type="button" id="btnEliminarModuloForm" name="btnEliminarModuloForm"   class="botonGeneral" value="<%=meLanbide45I18n.getMensaje(idiomaUsuario, "btn.eliminar")%>" onclick="pulsarEliminarModuloForm();">
                                <input type="button" id="btnModificarModuloForm" name="btnModificarModuloForm" class="botonGeneral" value="<%=meLanbide45I18n.getMensaje(idiomaUsuario, "btn.modificar")%>" onclick="pulsarModificarModuloForm();">
                            </div>
                        </div>
                </div>
            </div>
            <!--Script Ejecucion Elementos Pagina-->
            <script type="text/javascript">
                //Tabla Especialidades
                var tabModuloForm;
                var listaModuloForm = new Array();
                var listaModuloFormTabla = new Array();

                tabModuloForm = new Tabla(true,'<%=descriptor.getDescripcion("buscar")%>','<%=descriptor.getDescripcion("anterior")%>','<%=descriptor.getDescripcion("siguiente")%>','<%=descriptor.getDescripcion("mosFilasPag")%>','<%=descriptor.getDescripcion("msgNoResultBusq")%>','<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>','<%=descriptor.getDescripcion("filtrDeTotal")%>','<%=descriptor.getDescripcion("primero")%>','<%=descriptor.getDescripcion("ultimo")%>',document.getElementById('listaModuloForm'), 818);
                tabModuloForm.addColumna('50','center',"<%= meLanbide45I18n.getMensaje(idiomaUsuario,"moduloformativo.tablaModuloForm.col1")%>");
                tabModuloForm.addColumna('240','left',"<%= meLanbide45I18n.getMensaje(idiomaUsuario,"moduloformativo.tablaModuloForm.col2")%>");
                tabModuloForm.addColumna('80','center',"<%= meLanbide45I18n.getMensaje(idiomaUsuario,"moduloformativo.tablaModuloForm.col3")%>");
                tabModuloForm.addColumna('80','center',"<%= meLanbide45I18n.getMensaje(idiomaUsuario,"moduloformativo.tablaModuloForm.col4")%>");
                tabModuloForm.addColumna('75','center',"<%= meLanbide45I18n.getMensaje(idiomaUsuario,"moduloformativo.tablaModuloForm.col5")%>");
                tabModuloForm.addColumna('240','left',"<%= meLanbide45I18n.getMensaje(idiomaUsuario,"moduloformativo.tablaModuloForm.col6")%>");

                tabModuloForm.displayCabecera=true;
                tabModuloForm.height = 150;
                <%  	
                    String id = "";
                    String codMod = "";
                    String deno = "";
                    String durac = "";
                    String duracMax = "";
                    String codUc = "";
                    String desUc = "";
                    String obj = "";
                    String cp = "";

                    ModuloFormVO objectVO = null;
                    List<ModuloFormVO> List = (List<ModuloFormVO>)request.getAttribute("listModulosForm");													
                    if (List!= null && List.size() >0){
                        for (int indice=0;indice<List.size();indice++)
                        {
                            objectVO = List.get(indice);
                            if (objectVO.getId() != null){
                                id = objectVO.getId().toString();
                            }
                            else{
                                id = "";
                            }
                            if (objectVO.getCodMod() != null){
                                codMod = objectVO.getCodMod();
                            }
                            else{
                                codMod = "";
                            }
                            if (objectVO.getDenominacion() != null){
                                deno = objectVO.getDenominacion();
                            }
                            else{
                                deno = "";
                            }
                            if (objectVO.getDuracion() != null){
                                durac = objectVO.getDuracion().toString();
                            }
                            else{
                                durac = "";
                            }
                            if (objectVO.getDuracMax() != null){
                                duracMax = objectVO.getDuracMax().toString();
                            }
                            else{
                                duracMax = "";
                            }
                            if (objectVO.getCodUC() != null){
                                codUc = objectVO.getCodUC();
                            }
                            else{
                                codUc = "";
                            }
                            if (objectVO.getDesUC() != null){
                                desUc = objectVO.getDesUC();
                            }
                            else{
                                desUc = "";
                            }
                            if (objectVO.getObjetivo() != null){
                                obj = objectVO.getObjetivo();
                            }
                            else{
                                obj = "";
                            }

                %>      
                    listaModuloFormTabla[<%=indice%>] = ['<%=codMod%>', 
                        '<%=deno.replaceAll("\n\r","<br>").replaceAll("\r","<br>").replaceAll("\n","<br>")%>', 
                        '<%=durac%>','<%=duracMax%>','<%=codUc%>', '<%=desUc%>'];
                    listaModuloForm[<%=indice%>] =['<%=id%>','<%=codMod%>', 
                        '<%=deno.replaceAll("\n\r","<br>").replaceAll("\r","<br>").replaceAll("\n","<br>")%>',
                        '<%=codUc%>', '<%=desUc%>', '<%=durac%>', '<%=duracMax%>', 
                        '<%=obj.replaceAll("\n\r","<br>").replaceAll("\r","<br>").replaceAll("\n","<br>")%>', '<%=cp%>'];
                <%
                        }// for
                    }// if
                %>
                   
                tabModuloForm.lineas=listaModuloFormTabla;
                tabModuloForm.displayTabla();
                
                if(navigator.appName.indexOf("Internet Explorer")!=-1){
                    try{
                        var div = document.getElementById('listaModuloForm');
                        div.children[0].children[0].children[0].children[0].children[0].children[1].style.width = '100%';
                        div.children[0].children[0].children[0].children[1].style.width = '100%';
                    }
                    catch(err){

                    }
                }
                
                    /*document.getElementById('listaModuloForm').children[0].children[1].children[0].children[0].children[1].ondblclick = function(event){
                        pulsarListasModulosForm(event);
                    }*/
            </script>
            <!-- Script Con Funciones-->
            <script type="text/javascript">

                function pulsarAltaModuloForm(){
                    var control = new Date();
                    var result = null;
                    if(navigator.appName.indexOf("Internet Explorer")!=-1){
                        //result = lanzarPopUpModal('<%=request.getContextPath()%>/PeticionModuloIntegracion.do?tarea=preparar&modulo=MELANBIDE45&operacion=cargarNuevoModuloForm&tipo=0&numero=<%=numExpediente%>&control='+control.getTime(),600,1100,'yes','no');
						lanzarPopUpModal('<%=request.getContextPath()%>/PeticionModuloIntegracion.do?tarea=preparar&modulo=MELANBIDE45&operacion=cargarNuevoModuloForm&tipo=0&numero=<%=numExpediente%>&control='+control.getTime(),600,1100,'no','no', function(result){
								if (result != undefined){
									if(result[0] == '0'){
										recargarTablaModuloForm(result);								
									}
								}
							});
                    }else{
                        //result = lanzarPopUpModal('<%=request.getContextPath()%>/PeticionModuloIntegracion.do?tarea=preparar&modulo=MELANBIDE45&operacion=cargarNuevoModuloForm&tipo=0&numero=<%=numExpediente%>&control='+control.getTime(),600,1100,'no','no');
						lanzarPopUpModal('<%=request.getContextPath()%>/PeticionModuloIntegracion.do?tarea=preparar&modulo=MELANBIDE45&operacion=cargarNuevoModuloForm&tipo=0&numero=<%=numExpediente%>&control='+control.getTime(),600,1100,'no','no', function(result){
								if (result != undefined){
									if(result[0] == '0'){
										recargarTablaModuloForm(result);								
									}
								}
							});
                    }
                    /*if (result != undefined){
                        if(result[0] == '0'){
                            recargarTablaModuloForm(result);
                        }
                    }*/
                }

                function pulsarModificarModuloForm(){
                    if(tabModuloForm.selectedIndex != -1) {
                        var control = new Date();
                        //var result = null;
                        if(navigator.appName.indexOf("Internet Explorer")!=-1){
                            //result = lanzarPopUpModal('<%=request.getContextPath()%>/PeticionModuloIntegracion.do?tarea=preparar&modulo=MELANBIDE45&operacion=cargarModifModuloForm&tipo=0&numero=<%=numExpediente%>&id='+listaModuloForm[tabModuloForm.selectedIndex][0]+'&control='+control.getTime(),600,1100,'yes','no');
							lanzarPopUpModal('<%=request.getContextPath()%>/PeticionModuloIntegracion.do?tarea=preparar&modulo=MELANBIDE45&operacion=cargarModifModuloForm&tipo=0&numero=<%=numExpediente%>&id='+listaModuloForm[tabModuloForm.selectedIndex][0]+'&control='+control.getTime(),600,1100,'no','no', function(result){
								if (result != undefined){
									if(result[0] == '0'){
										recargarTablaModuloForm(result);								
									}
								}
							});
                        }else{
                            //result = lanzarPopUpModal('<%=request.getContextPath()%>/PeticionModuloIntegracion.do?tarea=preparar&modulo=MELANBIDE45&operacion=cargarModifModuloForm&tipo=0&numero=<%=numExpediente%>&id='+listaModuloForm[tabModuloForm.selectedIndex][0]+'&control='+control.getTime(),600,1100,'no','no');
							lanzarPopUpModal('<%=request.getContextPath()%>/PeticionModuloIntegracion.do?tarea=preparar&modulo=MELANBIDE45&operacion=cargarModifModuloForm&tipo=0&numero=<%=numExpediente%>&id='+listaModuloForm[tabModuloForm.selectedIndex][0]+'&control='+control.getTime(),600,1100,'no','no', function(result){
								if (result != undefined){
									if(result[0] == '0'){
										recargarTablaModuloForm(result);								
									}
								}
							});
                        }
                        /*if (result != undefined){
                            if(result[0] == '0'){
                                recargarTablaModuloForm(result);
                            }
                        }*/
                    } 
                    else {
                        jsp_alerta('A', '<%=meLanbide45I18n.getMensaje(idiomaUsuario, "msg.msjNoSelecFila")%>');
                    }
                }

                function pulsarEliminarModuloForm(){
                    if(tabModuloForm.selectedIndex != -1) {
                        var resultado = jsp_alerta('', '<%=meLanbide45I18n.getMensaje(idiomaUsuario, "msg.preguntaEliminar")%>');
                        if (resultado == 1){

                            var ajax = getXMLHttpRequest();
                            var nodos = null;
                            var CONTEXT_PATH = '<%=request.getContextPath()%>'
                            var url = CONTEXT_PATH + "/PeticionModuloIntegracion.do";
                            var parametros = "";
                            var control = new Date();
                            parametros = 'tarea=preparar&modulo=MELANBIDE45&operacion=eliminarModuloForm&tipo=0&numero=<%=numExpediente%>&id='+listaModuloForm[tabModuloForm.selectedIndex][0]+'&control='+control.getTime();
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
                                            }else if(hijosFila[cont].nodeName=="MDF_COD"){
                                                if(hijosFila[cont].childNodes.length > 0){
                                                    fila[1] = hijosFila[cont].childNodes[0].nodeValue;
                                                }
                                                else{
                                                    fila[1] = '';
                                                }
                                            }
                                            else if(hijosFila[cont].nodeName=="MDF_DEN"){
                                                if(hijosFila[cont].childNodes.length > 0){
                                                    fila[2] = hijosFila[cont].childNodes[0].nodeValue;
                                                }
                                                else{
                                                    fila[2] = '';
                                                }
                                            }else if(hijosFila[cont].nodeName=="MDF_UC_COD"){
                                                if(hijosFila[cont].childNodes.length > 0){
                                                    fila[3] = hijosFila[cont].childNodes[0].nodeValue;
                                                }
                                                else{
                                                    fila[3] = '';
                                                }
                                            }else if(hijosFila[cont].nodeName=="MDF_UC_DEN"){
                                                if(hijosFila[cont].childNodes.length > 0){
                                                    fila[4] = hijosFila[cont].childNodes[0].nodeValue;
                                                }
                                                else{
                                                    fila[4] = '';
                                                }
                                            }
                                            else if(hijosFila[cont].nodeName=="MDF_DUR"){
                                                if(hijosFila[cont].childNodes.length > 0){
                                                    fila[5] = hijosFila[cont].childNodes[0].nodeValue;
                                                }
                                                else{
                                                    fila[5] = '-';
                                                }
                                            }else if(hijosFila[cont].nodeName=="MDF_DUR_MAX_TEL"){
                                                if(hijosFila[cont].childNodes.length > 0){
                                                    fila[6] = hijosFila[cont].childNodes[0].nodeValue;
                                                }
                                                else{
                                                    fila[6] = '';
                                                }
                                            }
                                            else if(hijosFila[cont].nodeName=="MDF_OBJ"){
                                                if(hijosFila[cont].childNodes.length > 0){
                                                    fila[7] = hijosFila[cont].childNodes[0].nodeValue;
                                                }
                                                else{
                                                    fila[7] = '';
                                                }
                                            }
                                            else if(hijosFila[cont].nodeName=="MDF_CP"){
                                                if(hijosFila[cont].childNodes.length > 0){
                                                    fila[8] = hijosFila[cont].childNodes[0].nodeValue;
                                                }
                                                else{
                                                    fila[8] = '';
                                                }
                                            }
                                        }
                                        listaNueva[j] = fila;
                                        fila = new Array();
                                    }   
                                }//for(j=0;hijos!=null && j<hijos.length;j++)
                                if(codigoOperacion=="0"){
                                    recargarTablaModuloForm(listaNueva);
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
                
                

                function recargarTablaModuloForm(result){
                    listaModuloForm = new Array();
                    listaModuloFormTabla = new Array();
                    for(var i = 1;i< result.length; i++){
                        fila = result[i];  
                        if(fila[1] == 'null') fila[1] ='';
                        if(fila[2] == 'null') fila[2] ='';
                        if(fila[3] == 'null') fila[3] ='';
                        if(fila[4] == 'null') fila[4] ='';
                        if(fila[5] == 'null') fila[5] ='';
                        if(fila[6] == 'null') fila[6] ='';
                        //alert("fila: "+fila[1] + ", "+fila[2] + ", "+fila[3] + ", "+fila[4] + ", " +fila[5] + ", "+fila[6] + ", " +fila[7] + ", " +fila[8] );
                        //listaModuloForm[i-1] = fila;//no funciona ie9
						listaModuloForm[i-1] =[fila[0], fila[1], fila[2], fila[3], fila[4], fila[5], fila[6], fila[7], fila[8]];
                        //listaModuloFormTabla[i-1] = [fila[5], fila[1], fila[6], fila[7], fila[2], fila[8], fila[3], fila[9]];
                        listaModuloFormTabla[i-1] = [fila[1], fila[2], fila[5], fila[6], fila[3], fila[4]];
                    }
                    tabModuloForm = new Tabla(true,'<%=descriptor.getDescripcion("buscar")%>','<%=descriptor.getDescripcion("anterior")%>','<%=descriptor.getDescripcion("siguiente")%>','<%=descriptor.getDescripcion("mosFilasPag")%>','<%=descriptor.getDescripcion("msgNoResultBusq")%>','<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>','<%=descriptor.getDescripcion("filtrDeTotal")%>','<%=descriptor.getDescripcion("primero")%>','<%=descriptor.getDescripcion("ultimo")%>',document.getElementById('listaModuloForm'), 818);
                    tabModuloForm.addColumna('50','center',"<%= meLanbide45I18n.getMensaje(idiomaUsuario,"moduloformativo.tablaModuloForm.col1")%>");
                    tabModuloForm.addColumna('240','left',"<%= meLanbide45I18n.getMensaje(idiomaUsuario,"moduloformativo.tablaModuloForm.col2")%>");
                    tabModuloForm.addColumna('80','center',"<%= meLanbide45I18n.getMensaje(idiomaUsuario,"moduloformativo.tablaModuloForm.col3")%>");
                    tabModuloForm.addColumna('80','center',"<%= meLanbide45I18n.getMensaje(idiomaUsuario,"moduloformativo.tablaModuloForm.col4")%>");
                    tabModuloForm.addColumna('75','center',"<%= meLanbide45I18n.getMensaje(idiomaUsuario,"moduloformativo.tablaModuloForm.col5")%>");
                    tabModuloForm.addColumna('240','left',"<%= meLanbide45I18n.getMensaje(idiomaUsuario,"moduloformativo.tablaModuloForm.col6")%>");

                    tabModuloForm.displayCabecera=true;
                    tabModuloForm.height = 150;
                    tabModuloForm.lineas=listaModuloFormTabla;
                    
                    tabModuloForm.displayTabla();

                    if(navigator.appName.indexOf("Internet Explorer")!=-1){
                        try{
                            var div = document.getElementById('listaModuloForm');
                            
                            div.children[0].children[0].children[0].children[0].children[0].children[1].style.width = '100%';
                            div.children[0].children[0].children[0].children[1].style.width = '100%';
                        }
                        catch(err){

                        }
                    }
                    /*document.getElementById('listaModuloForm').children[0].children[1].children[0].children[0].ondblclick = function(event){
                        pulsarListasModulosForm(event);
                    }*/
                }        

                function pulsarListasModulosForm(event){
                    var idTabla = document.getElementById('listaModuloForm').children[0].children[1].children[0].children[0].children[1].id;
                    
                    var fila;
                    if(window.event) { //IE
                        if(window.event.srcElement.tagName=='TD'){
                            if(window.event.srcElement.parentElement.parentElement.parentElement.id==idTabla){
                                fila = window.event.srcElement.parentElement.rowIndex;
                            }
                        }else if(window.event.srcElement.id=='btnModificarModuloForm'){
                            fila = tabModuloForm.selectedIndex;
                        }
                    }else{ // FF
                        if(event.target.tagName=='TD'){
                            if(event.target.parentElement.parentElement.parentElement.id==idTabla){
                                fila = event.target.parentNode.rowIndex;
                            }
                        }else if(window.event.srcElement.id=='btnModificarModuloForm'){
                            fila = tabModuloForm.selectedIndex;
                        }
                    }


                    if(fila >= 0 && fila < listaModuloForm.length) {
                        var control = new Date();
                        var result = null;
                        if(navigator.appName.indexOf("Internet Explorer")!=-1){
                            //result = lanzarPopUpModal('<%=request.getContextPath()%>/PeticionModuloIntegracion.do?tarea=preparar&modulo=MELANBIDE45&operacion=cargarModifModuloForm&tipo=0&numero=<%=numExpediente%>&id='+listaModuloForm[tabModuloForm.selectedIndex][0]+'&control='+control.getTime(),600,1100,'yes','no');
							lanzarPopUpModal('<%=request.getContextPath()%>/PeticionModuloIntegracion.do?tarea=preparar&modulo=MELANBIDE45&operacion=cargarModifModuloForm&tipo=0&numero=<%=numExpediente%>&id='+listaModuloForm[tabModuloForm.selectedIndex][0]+'&control='+control.getTime(),600,1100,'no','no', function(result){
								if (result != undefined){
									if(result[0] == '0'){
										recargarTablaModuloForm(result);								
									}
								}
							});
                        }else{
                            //result = lanzarPopUpModal('<%=request.getContextPath()%>/PeticionModuloIntegracion.do?tarea=preparar&modulo=MELANBIDE45&operacion=cargarModifModuloForm&tipo=0&numero=<%=numExpediente%>&id='+listaModuloForm[tabModuloForm.selectedIndex][0]+'&control='+control.getTime(),600,1100,'yes','no');
							lanzarPopUpModal('<%=request.getContextPath()%>/PeticionModuloIntegracion.do?tarea=preparar&modulo=MELANBIDE45&operacion=cargarModifModuloForm&tipo=0&numero=<%=numExpediente%>&id='+listaModuloForm[tabModuloForm.selectedIndex][0]+'&control='+control.getTime(),600,1100,'no','no', function(result){
								if (result != undefined){
									if(result[0] == '0'){
										recargarTablaModuloForm(result);								
									}
								}
							});
                        }
                        /*if (result != undefined){
                            if(result[0] == '0'){
                                recargarTablaModuloForm(result);
                            }
                        }*/
                    } 
                    else {
                        jsp_alerta('A', '<%=meLanbide45I18n.getMensaje(idiomaUsuario, "msg.msjNoSelecFila")%>');
                    }
                    
                }
            </script>
    
    <!--/body>
</html-->

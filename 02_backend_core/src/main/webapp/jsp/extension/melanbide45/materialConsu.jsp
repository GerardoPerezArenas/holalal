<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>

<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide45.i18n.MeLanbide45I18n" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.common.service.config.Config"%>
<%@page import="es.altia.common.service.config.ConfigServiceHelper"%>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide45.vo.materialconsu.MaterialConsuVO"%>
<%@page import="java.util.ArrayList" %>
<%@page import="java.util.List" %>

<html>
    <head>
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
<link rel="StyleSheet" media="screen" type="text/css" href="<%=request.getContextPath()%><%=css%>">
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/>
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/extension/melanbide45/melanbide45.css"/>
    <script type="text/javascript">
        var APP_CONTEXT_PATH = '<%=request.getContextPath()%>';
    </script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/validaciones.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/listaComboBox.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/popup.js"></script>

    <body>
            <div class="tab-page" id="tabPage451" style="height:420px; width: 91%;">
                <script type="text/javascript">tp1_p451 = tp1.addTabPage( document.getElementById( "tabPage451" ) );</script>
                <div style="clear: both;">
                    <label class="legendAzul" style="text-align: center; position: relative; left: 5px;"><%=meLanbide45I18n.getMensaje(idiomaUsuario, "legend.materialConsu.titulo")%></label>
                        <div id="divGeneral"  style="overflow-y: auto; overflow-x: hidden; height: 440px;">     <!--onscroll="deshabilitarRadios();"-->
                            <div id="listMateriales" style="padding: 5px; width:790px; height: 300px; text-align: center; overflow-x: auto; overflow-y: auto;margin:0px;margin-top:0px;" align="center"></div>
                            <div class="botonera">
                                <input type="button" id="btnNuevoModuloForm" name="btnNuevoModuloForm" class="botonGeneral"  value="<%=meLanbide45I18n.getMensaje(idiomaUsuario, "btn.nuevo")%>" onclick="pulsarAltaMaterial();">
                                <input type="button" id="btnEliminarModuloForm" name="btnEliminarModuloForm"   class="botonGeneral" value="<%=meLanbide45I18n.getMensaje(idiomaUsuario, "btn.eliminar")%>" onclick="pulsarEliminarMaterial();">
                                <input type="button" id="btnModificarModuloForm" name="btnModificarModuloForm" class="botonGeneral" value="<%=meLanbide45I18n.getMensaje(idiomaUsuario, "btn.modificar")%>" onclick="pulsarModificarMaterial();">
                            </div>
                        </div>
                </div>
            </div>
            <!--Script Ejecucion Elementos Pagina-->
            <script type="text/javascript">
                //Tabla Especialidades
                var tabMaterial;
                var listMaterial = new Array();
                var listMaterialTabla = new Array();

                tabMaterial = new Tabla(true,'<%=descriptor.getDescripcion("buscar")%>','<%=descriptor.getDescripcion("anterior")%>','<%=descriptor.getDescripcion("siguiente")%>','<%=descriptor.getDescripcion("mosFilasPag")%>','<%=descriptor.getDescripcion("msgNoResultBusq")%>','<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>','<%=descriptor.getDescripcion("filtrDeTotal")%>','<%=descriptor.getDescripcion("primero")%>','<%=descriptor.getDescripcion("ultimo")%>',document.getElementById('listMateriales'), 762);
                tabMaterial.addColumna('150','right',"<%= meLanbide45I18n.getMensaje(idiomaUsuario,"materialConsu.tablaModuloForm.col1")%>");
                tabMaterial.addColumna('600','left',"<%= meLanbide45I18n.getMensaje(idiomaUsuario,"materialConsu.tablaModuloForm.col2")%>");

                tabMaterial.displayCabecera=true;
                tabMaterial.height = 150;

                <%  		
                    MaterialConsuVO objectVO = null;
                    List<MaterialConsuVO> List = (List<MaterialConsuVO>)request.getAttribute("listMatetrialConsu");													
                    if (List!= null && List.size() >0){
                        for (int indice=0;indice<List.size();indice++)
                        {
                            objectVO = List.get(indice);

                %>
                    listMaterialTabla[<%=indice%>] = ['<%=objectVO.getCantidad()%>', '<%=objectVO.getDescripcion().replaceAll("\n\r","<br>").replaceAll("\r","<br>").replaceAll("\n","<br>")%>'];
                    listMaterial[<%=indice%>] = ['<%=objectVO.getId()%>','<%=objectVO.getCantidad()%>', '<%=objectVO.getDescripcion().replaceAll("\n\r","<br>").replaceAll("\r","<br>").replaceAll("\n","<br>")%>'];
                <%
                        }// for
                    }// if
                %>
                   
                tabMaterial.lineas=listMaterialTabla;
                tabMaterial.displayTabla();
                
                if(navigator.appName.indexOf("Internet Explorer")!=-1){
                    try{
                        var div = document.getElementById('listMateriales');
                        div.children[0].children[1].style.width = '100%';
                        div.children[0].children[1].style.width = '100%';
                    }
                    catch(err){

                    }
                }
            </script>
            <!-- Script Con Funciones-->
            <script type="text/javascript">

                function pulsarAltaMaterial(){
                    var control = new Date();
                    var result = null;
                    if(navigator.appName.indexOf("Internet Explorer")!=-1){
                        //result = lanzarPopUpModal('<%=request.getContextPath()%>/PeticionModuloIntegracion.do?tarea=preparar&modulo=MELANBIDE45&operacion=cargarNuevoMaterial&tipo=0&numero=<%=numExpediente%>&control='+control.getTime(),300,900,'yes','no');
						lanzarPopUpModal('<%=request.getContextPath()%>/PeticionModuloIntegracion.do?tarea=preparar&modulo=MELANBIDE45&operacion=cargarNuevoMaterial&tipo=0&numero=<%=numExpediente%>&control='+control.getTime(),600,900,'no','no', function(result){
								if (result != undefined){
									if(result[0] == '0'){
										recargarTablaMaterial(result);								
									}
								}
							});
                    }else{
                        //result = lanzarPopUpModal('<%=request.getContextPath()%>/PeticionModuloIntegracion.do?tarea=preparar&modulo=MELANBIDE45&operacion=cargarNuevoMaterial&tipo=0&numero=<%=numExpediente%>&control='+control.getTime(),300,900,'no','no');
						lanzarPopUpModal('<%=request.getContextPath()%>/PeticionModuloIntegracion.do?tarea=preparar&modulo=MELANBIDE45&operacion=cargarNuevoMaterial&tipo=0&numero=<%=numExpediente%>&control='+control.getTime(),600,900,'no','no', function(result){
								if (result != undefined){
									if(result[0] == '0'){
										recargarTablaMaterial(result);								
									}
								}
							});
                    }
                    /*if (result != undefined){
                        if(result[0] == '0'){
                            recargarTablaMaterial(result);
                        }
                    }*/
                }

                function pulsarModificarMaterial(){
                    if(tabMaterial.selectedIndex != -1) {
                        var control = new Date();
                        var result = null;
                        if(navigator.appName.indexOf("Internet Explorer")!=-1){
                            //result = lanzarPopUpModal('<%=request.getContextPath()%>/PeticionModuloIntegracion.do?tarea=preparar&modulo=MELANBIDE45&operacion=cargarModifMaterialConsu&tipo=0&numero=<%=numExpediente%>&id='+listMaterial[tabMaterial.selectedIndex][0]+'&control='+control.getTime(),300,900,'yes','no');
							lanzarPopUpModal('<%=request.getContextPath()%>/PeticionModuloIntegracion.do?tarea=preparar&modulo=MELANBIDE45&operacion=cargarModifMaterialConsu&tipo=0&numero=<%=numExpediente%>&id='+listMaterial[tabMaterial.selectedIndex][0]+'&control='+control.getTime(),600,900,'no','no', function(result){
								if (result != undefined){
									if(result[0] == '0'){
										recargarTablaMaterial(result);								
									}
								}
							});
                        }else{
                            //result = lanzarPopUpModal('<%=request.getContextPath()%>/PeticionModuloIntegracion.do?tarea=preparar&modulo=MELANBIDE45&operacion=cargarModifMaterialConsu&tipo=0&numero=<%=numExpediente%>&id='+listMaterial[tabMaterial.selectedIndex][0]+'&control='+control.getTime(),300,900,'no','no');
							lanzarPopUpModal('<%=request.getContextPath()%>/PeticionModuloIntegracion.do?tarea=preparar&modulo=MELANBIDE45&operacion=cargarModifMaterialConsu&tipo=0&numero=<%=numExpediente%>&id='+listMaterial[tabMaterial.selectedIndex][0]+'&control='+control.getTime(),600,900,'no','no', function(result){
								if (result != undefined){
									if(result[0] == '0'){
										recargarTablaMaterial(result);								
									}
								}
							});
                        }
                        /*if (result != undefined){
                            if(result[0] == '0'){
                                recargarTablaMaterial(result);
                            }
                        }*/
                    } 
                    else {
                        jsp_alerta('A', '<%=meLanbide45I18n.getMensaje(idiomaUsuario, "msg.msjNoSelecFila")%>');
                    }
                }

                function pulsarEliminarMaterial(){
                    if(tabMaterial.selectedIndex != -1) {
                        var resultado = jsp_alerta('', '<%=meLanbide45I18n.getMensaje(idiomaUsuario, "msg.preguntaEliminar")%>');
                        if (resultado == 1){

                            var ajax = getXMLHttpRequest();
                            var nodos = null;
                            var CONTEXT_PATH = '<%=request.getContextPath()%>'
                            var url = CONTEXT_PATH + "/PeticionModuloIntegracion.do";
                            var parametros = "";
                            var control = new Date();
                            parametros = 'tarea=preparar&modulo=MELANBIDE45&operacion=eliminarMateriales&tipo=0&numero=<%=numExpediente%>&id='+listMaterial[tabMaterial.selectedIndex][0]+'&control='+control.getTime();
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
                                            }else if(hijosFila[cont].nodeName=="MAC_CANT"){
                                                if(hijosFila[cont].childNodes.length > 0){
                                                    fila[1] = hijosFila[cont].childNodes[0].nodeValue;
                                                }
                                                else{
                                                    fila[1] = '';
                                                }
                                            }
                                            else if(hijosFila[cont].nodeName=="MAC_DET"){
                                                if(hijosFila[cont].childNodes.length > 0){
                                                    fila[2] = hijosFila[cont].childNodes[0].nodeValue;
                                                }
                                                else{
                                                    fila[2] = '';
                                                }
                                            }
                                        }
                                        listaNueva[j] = fila;
                                        fila = new Array();
                                    }   
                                }//for(j=0;hijos!=null && j<hijos.length;j++)
                                if(codigoOperacion=="0"){
                                    recargarTablaMaterial(listaNueva);
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

                function recargarTablaMaterial(result){
                    var fila;
                    listMaterial = new Array();
                    listMaterialTabla = new Array();
                    for(var i = 1;i< result.length; i++){
                        fila = result[i];  
                        //alert("fila: "+fila[1] + ", "+fila[2] + ", "+fila[3] + ", "+fila[4] + ", " +fila[5] + ", "+fila[6] + ", " +fila[7] + ", " +fila[8] );
                        listMaterial[i-1] = fila;
                        //listMaterialTabla[i-1] = [fila[5], fila[1], fila[6], fila[7], fila[2], fila[8], fila[3], fila[9]];
                        listMaterialTabla[i-1] = [fila[1], fila[2]];
                    }
                    tabMaterial = new Tabla(true,'<%=descriptor.getDescripcion("buscar")%>','<%=descriptor.getDescripcion("anterior")%>','<%=descriptor.getDescripcion("siguiente")%>','<%=descriptor.getDescripcion("mosFilasPag")%>','<%=descriptor.getDescripcion("msgNoResultBusq")%>','<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>','<%=descriptor.getDescripcion("filtrDeTotal")%>','<%=descriptor.getDescripcion("primero")%>','<%=descriptor.getDescripcion("ultimo")%>',document.getElementById('listMateriales'), 762);
                    tabMaterial.addColumna('150','right',"<%= meLanbide45I18n.getMensaje(idiomaUsuario,"materialConsu.tablaModuloForm.col1")%>");
                    tabMaterial.addColumna('600','left',"<%= meLanbide45I18n.getMensaje(idiomaUsuario,"materialConsu.tablaModuloForm.col2")%>");

                    tabMaterial.displayCabecera=true;
                    tabMaterial.height = 150;
                    tabMaterial.lineas=listMaterialTabla;
                    tabMaterial.displayTabla();

                    if(navigator.appName.indexOf("Internet Explorer")!=-1){
                        try{
                            var div = document.getElementById('listMaterial');
                            
                            div.children[0].children[1].style.width = '100%';
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

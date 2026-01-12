<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>

<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide38.i18n.MeLanbide38I18n" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@ page import="es.altia.common.service.config.Config"%>
<%@ page import="es.altia.common.service.config.ConfigServiceHelper"%>
<%@ page import="es.altia.flexia.integracion.moduloexterno.melanbide38.vo.FilaContratoVO"%>
<%@page import="java.util.ArrayList" %>
<%@page import="java.util.List" %>

<%
    String sIdioma = request.getParameter("idioma");
    int idiomaUsuario = Integer.parseInt(sIdioma);
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
    //Clase para internacionalizar los mensajes de la aplicaci�n.
    MeLanbide38I18n meLanbide38I18n = MeLanbide38I18n.getInstance();
    
    Config m_Config = ConfigServiceHelper.getConfig("common");
    String statusBar = m_Config.getString("JSP.StatusBar");
    String nombreModulo     = request.getParameter("nombreModulo");
    String codOrganizacion  = request.getParameter("codOrganizacionModulo");
    String numExpediente    = request.getParameter("numero");
    String mensajeProgreso = "";
%>

<jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
<jsp:setProperty name="descriptor"  property="idi_cod" value="<%=idiomaUsuario%>" />
<jsp:setProperty name="descriptor"  property="apl_cod" value="<%=apl%>" />
<link rel="StyleSheet" media="screen" type="text/css" href="<%=request.getContextPath()%><%=css%>"/>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/>
<link rel="stylesheet" type="text/css" href="<c:url value='/css/extension/melanbide38/melanbide38.css'/>">

<script type="text/javascript" src="<c:url value='/scripts/popup.js'/>"></script>

<script type="text/javascript">   
    function pulsarAltaContratoAycon(){
        var numContratos = listaContratosAycon.length;
        if(numContratos < 5){
            var control = new Date();
            var result = null;
            lanzarPopUpModal('<%=request.getContextPath()%>/PeticionModuloIntegracion.do?tarea=preparar&modulo=MELANBIDE38&operacion=cargarNuevoContrato&tipo=0&numero=<%=numExpediente%>&control='+control.getTime(),450,980,'no','no',function(result){
					            if (result != undefined){
                if(result[0] == '0'){
                    recargarTablaContratosAycon(result);
                }
            }
					});          

        }else{
            jsp_alerta('A', '<%=meLanbide38I18n.getMensaje(idiomaUsuario, "msg.maxContratosAlcanzado")%>');
        }
    }

    // Elimina un contrato de renovaci�n asociado al expediente.
    function pulsarEliminarContratoAycon(){
        if(tabContratosAycon.selectedIndex != -1) {
            var resultado = jsp_alerta('', '<%=meLanbide38I18n.getMensaje(idiomaUsuario, "msg.preguntaEliminar")%>');
            if (resultado == 1){

                var ajax = getXMLHttpRequest();
                var nodos = null;
                var CONTEXT_PATH = '<%=request.getContextPath()%>'
                var url = CONTEXT_PATH + "/PeticionModuloIntegracion.do";
                var parametros = "";
                var control = new Date();
                parametros = 'tarea=preparar&modulo=MELANBIDE38&operacion=eliminarContrato&tipo=0&numero=<%=numExpediente%>&idCon='+listaContratosAycon[tabContratosAycon.selectedIndex][0]+'&control='+control.getTime();
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
                    var listaContratosNueva = new Array();
                    var fila = new Array();
                    var nodoFila;
                    var hijosFila;
                    for(j=0;hijos!=null && j<hijos.length;j++){
                        if(hijos[j].nodeName=="CODIGO_OPERACION"){                            
                            codigoOperacion = hijos[j].childNodes[0].nodeValue;
                            listaContratosNueva[j] = codigoOperacion;
                        }//if(hijos[j].nodeName=="CODIGO_OPERACION")                      
                        else if(hijos[j].nodeName=="FILA"){
                            nodoFila = hijos[j];
                            hijosFila = nodoFila.childNodes;
                            for(var cont = 0; cont < hijosFila.length; cont++){
                                if(hijosFila[cont].nodeName=="ID"){
                                    fila[0] = hijosFila[cont].childNodes[0].nodeValue;
                                }
                                else if(hijosFila[cont].nodeName=="DNI"){
                                    fila[1] = hijosFila[cont].childNodes[0].nodeValue;
                                }
                                else if(hijosFila[cont].nodeName=="NOMBREAPELLIDOS"){
                                    fila[2] = hijosFila[cont].childNodes[0].nodeValue;
                                }
                            }
                            listaContratosNueva[j] = fila;
                            fila = new Array();
                        }   
                    }//for(j=0;hijos!=null && j<hijos.length;j++)
                    if(codigoOperacion=="0"){
                        jsp_alerta("A",'<%=meLanbide38I18n.getMensaje(idiomaUsuario,"msg.regEliminadoOK")%>');
                        recargarTablaContratosAycon(listaContratosNueva);
                    }else if(codigoOperacion=="1"){
                        jsp_alerta("A",'<%=meLanbide38I18n.getMensaje(idiomaUsuario,"error.errorBD")%>');
                    }else if(codigoOperacion=="2"){
                        jsp_alerta("A",'<%=meLanbide38I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                    }else if(codigoOperacion=="3"){
                        jsp_alerta("A",'<%=meLanbide38I18n.getMensaje(idiomaUsuario,"error.pasoParametros")%>');
                    }else{
                        jsp_alerta("A",'<%=meLanbide38I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                    }//if(
                }
                catch(Err){

                }//try-catch
            }
        } 
        else {
            jsp_alerta('A', '<%=meLanbide38I18n.getMensaje(idiomaUsuario, "msg.msjNoSelecFila")%>');
        }
    }

    // Carga los datos de un contrato de renovaci�n para poder editarlo
    function pulsarModificarContratoAycon(event){
        var idTabla = document.getElementById('contratosAycon').children[0].children[1].children[0].children[0].id;
        var fila;
        if(window.event) { //IE
            if(window.event.srcElement.tagName=='TD'){
                if(window.event.srcElement.parentElement.parentElement.parentElement.id==idTabla){
                    fila = window.event.srcElement.parentElement.rowIndex;
                }
            }else if(window.event.srcElement.id=='btnModificarContAycon'){
                fila = tabContratosAycon.selectedIndex;
            }
        }else{ // FF
            if(event.target.tagName=='TD'){
                if(event.target.parentElement.parentElement.parentElement.id==idTabla){
                    fila = event.target.parentNode.rowIndex;
                }
            }else if(window.event.srcElement.id=='btnModificarContAycon'){
                fila = tabContratosAycon.selectedIndex;
            }
        }
    
    
        if(fila >= 0 && fila < listaContratosAycon.length) {
            var control = new Date();
            var result = null;
            var cerrado = '';
            if(document.forms[0].modoConsulta.value == 'si'){
                cerrado = '1';
            }else{
                cerrado = '0';
            }
            
                lanzarPopUpModal('<%=request.getContextPath()%>/PeticionModuloIntegracion.do?tarea=preparar&modulo=MELANBIDE38&operacion=cargarModificarContrato&tipo=0&numero=<%=numExpediente%>&idCon='+listaContratosAycon[fila][0]+'&cerrado='+cerrado+'&control='+control.getTime(),450,980,'no','no',function(result){
                   if (result != undefined){
                       if(result[0] == '0'){
                           recargarTablaContratosAycon(result);
                       }
                   }
					});            
            
        } 
        else {
            jsp_alerta('A', '<%=meLanbide38I18n.getMensaje(idiomaUsuario, "msg.msjNoSelecFila")%>');
        }
    }

    function recargarTablaContratosAycon(result){
        var fila;
        listaContratosAycon = new Array();
        listaContratosAyconTabla = new Array();
        for(var i = 1;i< result.length; i++){
            fila = result[i];
            listaContratosAycon[i-1] = fila;
            listaContratosAyconTabla[i-1] = [fila[1], fila[2]];
        }
        tabContratosAycon = new Tabla(true,'<%=descriptor.getDescripcion("buscar")%>','<%=descriptor.getDescripcion("anterior")%>','<%=descriptor.getDescripcion("siguiente")%>','<%=descriptor.getDescripcion("mosFilasPag")%>','<%=descriptor.getDescripcion("msgNoResultBusq")%>','<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>','<%=descriptor.getDescripcion("filtrDeTotal")%>','<%=descriptor.getDescripcion("primero")%>','<%=descriptor.getDescripcion("ultimo")%>',document.getElementById('contratosAycon'), 874);
        tabContratosAycon.addColumna('100','left',"<%= meLanbide38I18n.getMensaje(idiomaUsuario,"contratos.tabla.col1")%>");
        tabContratosAycon.addColumna('762','left',"<%= meLanbide38I18n.getMensaje(idiomaUsuario,"contratos.tabla.col2")%>");                                                            

        tabContratosAycon.displayCabecera=true;
        tabContratosAycon.height = 390;
        tabContratosAycon.lineas=listaContratosAyconTabla;
        tabContratosAycon.displayTabla();
    
        document.getElementById('contratosAycon').children[0].children[1].children[0].children[0].ondblclick = function(event){
            pulsarModificarContratoAycon(event);
        }
    }
</script>

<body>
    <div class="tab-page" id="tabPage371" style="height:520px; width: 100%;">
        <h2 class="tab" id="pestana371"><%=meLanbide38I18n.getMensaje(idiomaUsuario,"label.tituloPestana")%></h2>
        <script type="text/javascript">tp1_p371 = tp1.addTabPage( document.getElementById( "tabPage371" ) );</script>
            <div style="clear: both;">
                <div>
                    <div class="sub3titulo" style="height: 18px; clear: both; padding-top: 4px;">
                        <span>
                            <%=meLanbide38I18n.getMensaje(idiomaUsuario,"label.listadoContratos")%>
                        </span>
                    </div>
                    <div id="contratosAycon" align="center"></div>
                    <div style="width: 100%; text-align: center;">
                        <input type="button" id="btnNuevoContAycon" name="btnNuevoContAycon" class="botonGeneral" value="<%=meLanbide38I18n.getMensaje(idiomaUsuario, "btn.alta")%>" onclick="pulsarAltaContratoAycon();">
                        <input type="button" id="btnModificarContAycon" name="btnModificarContAycon" class="botonGeneral" value="<%=meLanbide38I18n.getMensaje(idiomaUsuario, "btn.modificar")%>" onclick="pulsarModificarContratoAycon(event);">
                        <input type="button" id="btnEliminarContAycon" name="btnEliminarContAycon" class="botonGeneral" value="<%=meLanbide38I18n.getMensaje(idiomaUsuario, "btn.eliminar")%>" onclick="pulsarEliminarContratoAycon();">
                    </div>
                </div>
            </div>
    </div>
</body>

<script type="text/javascript">    
    var tabContratosAycon;
    var listaContratosAycon = new Array();
    var listaContratosAyconTabla = new Array();

    tabContratosAycon = new Tabla(true,'<%=descriptor.getDescripcion("buscar")%>','<%=descriptor.getDescripcion("anterior")%>','<%=descriptor.getDescripcion("siguiente")%>','<%=descriptor.getDescripcion("mosFilasPag")%>','<%=descriptor.getDescripcion("msgNoResultBusq")%>','<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>','<%=descriptor.getDescripcion("filtrDeTotal")%>','<%=descriptor.getDescripcion("primero")%>','<%=descriptor.getDescripcion("ultimo")%>',document.getElementById('contratosAycon'), 880);
    tabContratosAycon.addColumna('100','left',"<%= meLanbide38I18n.getMensaje(idiomaUsuario,"contratos.tabla.col1")%>");
    tabContratosAycon.addColumna('788','left',"<%= meLanbide38I18n.getMensaje(idiomaUsuario,"contratos.tabla.col2")%>");      

    tabContratosAycon.displayCabecera=true;
    tabContratosAycon.height = 370;
    <%  		
        FilaContratoVO vo = null;
        List<FilaContratoVO> listaContratos = (List<FilaContratoVO>)request.getAttribute("contratos");													
        if (listaContratos!= null && listaContratos.size() >0){
            for (int i = 0; i < listaContratos.size(); i++)
            {
                vo = listaContratos.get(i);
                
    %>
        listaContratosAycon[<%=i%>] = ["<%=vo.getNumContrato()%>", "<%=vo.getDni()%>", "<%=vo.getNombreApellidos()%>"];
        listaContratosAyconTabla[<%=i%>] = ["<%=vo.getDni()%>", "<%=vo.getNombreApellidos()%>"];
    <%
            }// for
        }// if
    %>
    tabContratosAycon.lineas=listaContratosAyconTabla;
    tabContratosAycon.displayTabla();
    if(navigator.appName.indexOf("Internet Explorer")!=-1){
        try{
            var div = document.getElementById('contratosAycon');
            div.children[0].children[0].children[0].children[0].children[0].children[0].children[1].style.width = '100%';
            div.children[0].children[1].children[0].children[0].children[0].children[1].style.width = '100%';
        }catch(err){
            
        }
    }
    
    document.getElementById('contratosAycon').children[0].children[1].children[0].children[0].ondblclick = function(event){
        pulsarModificarContratoAycon(event);
    }
</script>
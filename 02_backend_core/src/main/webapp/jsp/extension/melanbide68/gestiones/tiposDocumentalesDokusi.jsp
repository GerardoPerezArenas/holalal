<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>

<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.agora.business.util.GeneralValueObject"%>
<%@page import="es.altia.common.service.config.Config"%>
<%@page import="es.altia.common.service.config.ConfigServiceHelper"%>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide68.i18n.MeLanbide68I18n" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide68.vo.FilaTipDokusiVO" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide68.util.ConstantesMeLanbide68" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide68.util.ConfigurationParameter" %>
<%@page import="java.util.ArrayList" %>
<%@page import="java.util.List" %>

<html>
    <head>
            
        <%

            int idiomaUsuario = 0;
            int codOrganizacion = 0;
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
                        codOrganizacion  = usuario.getOrgCod();
                        apl = usuario.getAppCod();
                        css = usuario.getCss();                        
                    }
                }
            }
            catch(Exception ex)
            {

            }   
            //Clase para internacionalizar los mensajes de la aplicacion.
            MeLanbide68I18n meLanbide68I18n = MeLanbide68I18n.getInstance();
        %>
        <jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
        <jsp:setProperty name="descriptor"  property="idi_cod" value="<%=idiomaUsuario%>" />
        <jsp:setProperty name="descriptor"  property="apl_cod" value="<%=apl%>" />
        <link rel="StyleSheet" media="screen" type="text/css" href="<%=request.getContextPath()%><%=css%>">
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/popup.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/general.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/TablaNueva.js"></script>  
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/melanbide68/JavaScriptUtil.js"></script>

        <script type="text/javascript">
            var APP_CONTEXT_PATH = '<%=request.getContextPath()%>';
        </script> 
        <script type="text/javascript">
            function crearTabla(){
                tabTipDokusi = new Tabla(true,'<%=descriptor.getDescripcion("buscar")%>','<%=descriptor.getDescripcion("anterior")%>','<%=descriptor.getDescripcion("siguiente")%>','<%=descriptor.getDescripcion("mosFilasPag")%>','<%=descriptor.getDescripcion("msgNoResultBusq")%>','<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>','<%=descriptor.getDescripcion("filtrDeTotal")%>','<%=descriptor.getDescripcion("primero")%>','<%=descriptor.getDescripcion("ultimo")%>',document.getElementById('tiposDokusi'));        
               
                tabTipDokusi.addColumna('100','left',"<%= meLanbide68I18n.getMensaje(idiomaUsuario,"label.tipdoc.tabla.col1")%>", "<%= meLanbide68I18n.getMensaje(idiomaUsuario,"label.TipDokusi.tabla.col1.title")%>");    
                tabTipDokusi.addColumna('375','left',"<%= meLanbide68I18n.getMensaje(idiomaUsuario,"label.TipDokusi.tabla.col5")%>", "<%= meLanbide68I18n.getMensaje(idiomaUsuario,"label.TipDokusi.tabla.col5.title")%>");        
                tabTipDokusi.addColumna('375','left',"<%= meLanbide68I18n.getMensaje(idiomaUsuario,"label.TipDokusi.tabla.col4")%>", "<%= meLanbide68I18n.getMensaje(idiomaUsuario,"label.TipDokusi.tabla.col4.title")%>");    
                tabTipDokusi.addColumna('200','left',"<%= meLanbide68I18n.getMensaje(idiomaUsuario,"label.TipDokusi.tabla.col1")%>", "<%= meLanbide68I18n.getMensaje(idiomaUsuario,"label.TipDokusi.tabla.col1.title")%>");    
                tabTipDokusi.addColumna('0','left',"<%= meLanbide68I18n.getMensaje(idiomaUsuario,"label.TipDokusi.tabla.col2")%>", "<%= meLanbide68I18n.getMensaje(idiomaUsuario,"label.TipDokusi.tabla.col2.title")%>");    
                tabTipDokusi.addColumna('70','center',"<%= meLanbide68I18n.getMensaje(idiomaUsuario,"label.TipDokusi.tabla.col3")%>", "<%= meLanbide68I18n.getMensaje(idiomaUsuario,"label.TipDokusi.tabla.col3.title")%>");    

                tabTipDokusi.displayCabecera=true;
                tabTipDokusi.height = '300';  
            }            
        </script>         

    </head>
    <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script>
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/>
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/estilo.css"/> 
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/extension/melanbide68/melanbide68.css"/>

    <body class="bandaBody" onload="javascript:{pleaseWait('off')}">
        <jsp:include page="/jsp/hidepage.jsp" flush="true">
            <jsp:param name='cargaDatos' value='<%=descriptor.getDescripcion("msjCargDatos")%>'/>
        </jsp:include>      
    <%--body class="bandaBody"--%>
    <div class="contenidoPantalla">
        <div style="height:50px; width: 100%;">
            <table width="100%" style="height: 100%;" cellpadding="0px" cellspacing="0px" border="0px">
                <tr>
                    <td class="txttitblanco"><%=meLanbide68I18n.getMensaje(idiomaUsuario,"label.tituloTablaTiposDokusi")%> </td>
                </tr>
            </table>
        </div>  
        <div class="tab-page" id="tabPage354" style="height:520px; width: 100%;">
            </br>
            <div style="padding: 10px;">
                </br>
                <div id="divGeneral" >     
                    <div id="tiposDokusi" align="center"></div>
                </div> 
                <!-- botones debajo de la tabla --->
                <div class="botonera" >
                    <input type="button" id="btnInsertarTipDokusi" name="btnInsertarTipDokusi" class="botonGeneral" value="<%=meLanbide68I18n.getMensaje(idiomaUsuario, "btn.insertar")%>" onclick="pulsarInsertarTipDokusi(true);">
                    <input type="button" id="btnModificarTipDokusi" name="btnModificarTipDokusi" class="botonGeneral" value="<%=meLanbide68I18n.getMensaje(idiomaUsuario, "btn.modificar")%>" onclick="pulsarModificarTipDokusi();">
                    <input type="button" id="btnEliminarTipDokusi" name="btnEliminarTipDokusi" class="botonGeneral" value="<%=meLanbide68I18n.getMensaje(idiomaUsuario, "btn.eliminar")%>" onclick="pulsarEliminarTipDokusi(true);">
                </div>   
            </div>
        </div>                                                    

        <script type="text/javascript">
            pleaseWait('on');
            var tabTipDokusi;
            var listaTipDokusi = new Array();
            var listaTipDokusiTabla = new Array();

            crearTabla();
            <% 
                FilaTipDokusiVO objectVO = null;
                List<FilaTipDokusiVO> List = null;

                if(request.getAttribute("tiposDokusi")!=null){
                    List = (List<FilaTipDokusiVO>)request.getAttribute("tiposDokusi");
                }	

                if (List!= null && List.size() >0){             
          
                    for (int indice=0;indice<List.size();indice++)
                    {
                        objectVO = List.get(indice);                
                    %>
                        listaTipDokusi[<%=indice%>] =      ['<%=objectVO.getCodTipDoc()%>','<%=objectVO.getDesDokusi_eu()!=null?objectVO.getDesDokusi_eu():""%>','<%=objectVO.getDesDokusi_es()%>','<%=objectVO.getCodDokusi()%>','<%=objectVO.getCodDokusiPadre()!=null?objectVO.getCodDokusiPadre():""%>','<%=objectVO.getCodDokusiFamilia()!=null?objectVO.getCodDokusiFamilia():""%>'];
                        listaTipDokusiTabla[<%=indice%>] = ['<%=objectVO.getCodTipDoc()%>','<%=objectVO.getDesDokusi_eu()!=null?objectVO.getDesDokusi_eu():""%>','<%=objectVO.getDesDokusi_es()%>','<%=objectVO.getCodDokusi()%>','<%=objectVO.getCodDokusiPadre()!=null?objectVO.getCodDokusiPadre():""%>','<%=objectVO.getCodDokusiFamilia()!=null?objectVO.getCodDokusiFamilia():""%>'];
                         
                    <%
                    }// for
                }// if
            %>
            tabTipDokusi.lineas=listaTipDokusi;
            tabTipDokusi.displayTablaConTooltips(listaTipDokusiTabla);
            if(navigator.appName.indexOf("Internet Explorer")!=-1){
                try{
                    var div = document.getElementById('tiposDokusi');
                    div.children[0].children[0].children[0].children[1].style.width = '100%';
                    div.children[0].children[1].style.width = '100%';
                }
                catch(err){
                }
            }                

            function getXMLHttpRequest() {
                var aVersions = ["MSXML2.XMLHttp.5.0",
                    "MSXML2.XMLHttp.4.0", "MSXML2.XMLHttp.3.0",
                    "MSXML2.XMLHttp", "Microsoft.XMLHttp"
                ];

                if (window.XMLHttpRequest) {
                    // para IE7, Mozilla, Safari, etc: que usen el objeto nativo
                    return new XMLHttpRequest();
                } else if (window.ActiveXObject) {
                    // de lo contrario utilizar el control ActiveX para IE5.x y IE6.x
                    for (var i = 0; i < aVersions.length; i++) {
                        try {
                            var oXmlHttp = new ActiveXObject(aVersions[i]);
                            return oXmlHttp;
                        } catch (error) {
                            //no necesitamos hacer nada especial
                        }
                    }
                } else {
                    return null;
                }
            }

        //Función para eliminar un registro
            function pulsarEliminarTipDokusi(){ 
                var fila;

                if(tabTipDokusi.selectedIndex != -1) {
                    fila = tabTipDokusi.selectedIndex;
                    var resultado = jsp_alerta('', '<%=meLanbide68I18n.getMensaje(idiomaUsuario, "msg.preguntaEliminar")%>');
                    if (resultado == 1){
                        var ajax = getXMLHttpRequest();
                        var nodos = null;
                        var CONTEXT_PATH = '<%=request.getContextPath()%>';
                        var url = CONTEXT_PATH + "/PeticionModuloIntegracion.do";
                        var parametros = "";
                        var control = new Date();
                        var listaTipDokusi = new Array();
                        parametros = 'tarea=preparar&modulo=MELANBIDE68&operacion=cargarEliminarTipDokusi&tipo=0'
                                      +'&codTipDokusi='+tabTipDokusi.lineas[fila][3]
                                      +'&control='+control.getTime(); 
                        try{
                            ajax.open("POST",url,false);
                            ajax.setRequestHeader("Content-Type","application/x-www-form-urlencoded; charset=iso-8859-15");
                            ajax.setRequestHeader("Accept", "text/xml, application/xml, text/plain");     
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
                            // que pasaría con el código de respuesta ?????
                            nodos = xmlDoc.getElementsByTagName("RESPUESTA");
                            listaTipDokusi = extraerListadoTipDokusi(nodos);
                            //recargarTablalistaTipDokusi(listaTipDokusi);
                            var codigoOperacion = listaTipDokusi[0];
                            if(codigoOperacion=="0"){
                                recargarTablalistaTipDokusi(listaTipDokusi);
                                pleaseWait('off');
                                jsp_alerta("A",'<%=meLanbide68I18n.getMensaje(idiomaUsuario,"msg.registroEliminadoOK")%>');
                            }else if(codigoOperacion=="1"){
                                jsp_alerta("A",'<%=meLanbide68I18n.getMensaje(idiomaUsuario,"error.errorEliminarBD")%>');
                            }else if(codigoOperacion=="2"){
                                jsp_alerta("A",'<%=meLanbide68I18n.getMensaje(idiomaUsuario,"error.errorEliminarGen")%>');
                            }else if(codigoOperacion=="3"){
                                jsp_alerta("A",'<%=meLanbide68I18n.getMensaje(idiomaUsuario,"error.pasoParametrosEliminar")%>');
                            }else if(codigoOperacion=="4"){
                                jsp_alerta("A",'<%=meLanbide68I18n.getMensaje(idiomaUsuario,"error.noesposibleEliminar")%>');
                            }else{
                                jsp_alerta("A",'<%=meLanbide68I18n.getMensaje(idiomaUsuario,"error.errorEliminarGen")%>');
                            }//if(
                        }
                        catch(Err){
                            jsp_alerta("A",'<%=meLanbide68I18n.getMensaje(idiomaUsuario,"error.errorProcesarRespuesta")%>');
                        }//try-catch
                    }
                } 
                else {
                    jsp_alerta('A', '<%=meLanbide68I18n.getMensaje(idiomaUsuario, "msg.msjNoSelecFila")%>');
                }
            }   

            function extraerListadoTipDokusi(nodos){
                    var elemento = nodos[0];
                    var hijos = elemento.childNodes;
                    var codigoOperacion = null;
                    var listaTipDokusi = new Array();
                    var fila = new Array();
                    var nodoFila;
                    var hijosFila;
                    var nodoCampo;
                    var j;

                    for(j=0;hijos!=null && j<hijos.length;j++){
                        if(hijos[j].nodeName=="CODIGO_OPERACION"){                            
                            codigoOperacion = hijos[j].childNodes[0].nodeValue;
                            listaTipDokusi[j] = codigoOperacion;
                        }                   

                        else if(hijos[j].nodeName=="FILA"){
                            nodoFila = hijos[j];
                            hijosFila = nodoFila.childNodes;
                            for(var cont = 0; cont < hijosFila.length; cont++){
                                if(hijosFila[cont].nodeName=="COD_TIPDOC"){
                                    if (hijosFila[cont].childNodes[0].nodeValue != "null") {
                                        fila[0] = hijosFila[cont].childNodes[0].nodeValue;
                                    }
                                    else {
                                        fila[0] = '';
                                    } 
                                }
                                else if(hijosFila[cont].nodeName=="DOKUSI_EU"){   
                                    if (hijosFila[cont].childNodes[0].nodeValue != "null") {
                                        fila[1] = hijosFila[cont].childNodes[0].nodeValue;
                                    }
                                    else {
                                        fila[1] = '';
                                    }                                    
                                }
                                 else if(hijosFila[cont].nodeName=="DOKUSI_ES"){
                                    if (hijosFila[cont].childNodes[0].nodeValue != "null") {
                                        fila[2] = hijosFila[cont].childNodes[0].nodeValue;
                                    }
                                    else {
                                        fila[2] = '';
                                    }  
                                }
                                else if(hijosFila[cont].nodeName=="COD_DOKUSI"){
                                    if (hijosFila[cont].childNodes[0].nodeValue != "null") {
                                        fila[3] = hijosFila[cont].childNodes[0].nodeValue;
                                    }
                                    else {
                                        fila[3] = '';
                                    }    
                                }
                                 else if(hijosFila[cont].nodeName=="TIPDOC_DOKUSI_PADRE"){
                                    if (hijosFila[cont].childNodes[0].nodeValue != "null") {
                                        fila[4] = hijosFila[cont].childNodes[0].nodeValue;
                                    }
                                    else {
                                        fila[4] = '';
                                    }    
                                }
                                 else if(hijosFila[cont].nodeName=="DOKUSI_FAMILIA"){
                                    if (hijosFila[cont].childNodes[0].nodeValue != "null") {
                                        fila[5] = hijosFila[cont].childNodes[0].nodeValue;
                                    }
                                    else {
                                        fila[5] = '';
                                    }    
                                }
                                
                               
                                                                
                            }
                            listaTipDokusi[j] = fila;
                            fila = new Array();   
                        }
                } 
                return listaTipDokusi;
            }
            
            function recargarTablalistaTipDokusi(result){
                var fila;
                //Tabla TiposDocumentales DOKUSI
                var listaTipDokusi = new Array();
                var listaTipDokusiTabla = new Array();

                for(var i = 1;i< result.length; i++){
                    fila = result[i];

                    listaTipDokusi[i-1] = [fila[0],fila[1],fila[2],fila[3],fila[4],fila[5]];
                    listaTipDokusiTabla[i-1] = [fila[0],fila[1],fila[2],fila[3],fila[4],fila[5]];
                }
                
                crearTabla();

                tabTipDokusi.lineas=listaTipDokusi;
                tabTipDokusi.displayTablaConTooltips(listaTipDokusiTabla);   
                if(navigator.appName.indexOf("Internet Explorer")!=-1){
                    try{
                        var div = document.getElementById('tiposDokusi');
                        div.children[0].children[0].children[0].children[1].style.width = '100%';
                        div.children[0].children[1].style.width = '100%';
                    }
                    catch(err){
                    }
                }
            }

            function pulsarInsertarTipDokusi(){
                var control = new Date();
                var result = null;

                if(navigator.appName.indexOf("Internet Explorer")!=-1){
                    lanzarPopUpModal('<%=request.getContextPath()%>/PeticionModuloIntegracion.do?tarea=preparar&modulo=MELANBIDE68&operacion=cargarNuevoTipDokusi&tipo=0'
                                                                                                      +'&control='+control.getTime(),450,1250,'no','no', function(result){
                                                                if (result != undefined){
                                                                        if(result[0] == '0'){
                                                                                recargarTablalistaTipDokusi(result);								
                                                                        }
                                                                }
                                                        });
                }else{
                    lanzarPopUpModal('<%=request.getContextPath()%>/PeticionModuloIntegracion.do?tarea=preparar&modulo=MELANBIDE68&operacion=cargarNuevoTipDokusi&tipo=0'
                                                                                                      +'&control='+control.getTime(),450,1250,'no','no', function(result){
                                                                if (result != undefined){
                                                                        if(result[0] == '0'){
                                                                                recargarTablalistaTipDokusi(result);								
                                                                        }
                                                                }
                                                        });
                }   
            }
            
            function pulsarModificarTipDokusi(){
                var fila;

                if(tabTipDokusi.selectedIndex != -1) {
                    fila = tabTipDokusi.selectedIndex;
                    var control = new Date();

                    if(navigator.appName.indexOf("Internet Explorer")!=-1){
                        lanzarPopUpModal('<%=request.getContextPath()%>/PeticionModuloIntegracion.do?tarea=preparar&modulo=MELANBIDE68&operacion=cargarModificarTipDokusi&tipo=0'
                                                                        +'&codTipDoc='+tabTipDokusi.lineas[fila][0]
                                                                        +'&tipDokusiEus='+tabTipDokusi.lineas[fila][1]
                                                                        +'&tipDokusiCas='+tabTipDokusi.lineas[fila][2]
                                                                        +'&codDokusi='+tabTipDokusi.lineas[fila][3]                                                                      
                                                                        +'&codDokusiPadre='+tabTipDokusi.lineas[fila][4]                                 
                                                                        +'&familiaDokusi='+tabTipDokusi.lineas[fila][5]                                                                
                                                                        +'&control='+control.getTime(),450,1200,'no','no', function(result){
                                                                if (result != undefined){
                                                                    // aquí también tengo que ver la forma de recuperar el CodigoOperacion porque si no me va a dar error
                                                                        if(result[0] == '0'){
                                                                                recargarTablalistaTipDokusi(result);								
                                                                        }
                                                                }
                                                        });
                    }else{
                        lanzarPopUpModal('<%=request.getContextPath()%>/PeticionModuloIntegracion.do?tarea=preparar&modulo=MELANBIDE68&operacion=cargarModificarTipDokusi&tipo=0'
                                                                        +'&codTipDoc='+tabTipDokusi.lineas[fila][0]
                                                                        +'&tipDokusiEus='+tabTipDokusi.lineas[fila][1]
                                                                        +'&tipDokusiCas='+tabTipDokusi.lineas[fila][2]
                                                                        +'&codDokusi='+tabTipDokusi.lineas[fila][3]                                                                      
                                                                        +'&codDokusiPadre='+tabTipDokusi.lineas[fila][4]     
                                                                        +'&familiaDokusi='+tabTipDokusi.lineas[fila][5]                                                                
                                                                        +'&control='+control.getTime(),450,1200,'no','no', function(result){
                                                                if (result != undefined){
                                                                    // aquí también tengo que ver la forma de recuperar el CodigoOperacion porque si no me va a dar error
                                                                        if(result[0] == '0'){
                                                                                recargarTablalistaTipDokusi(result);								
                                                                        }
                                                                }
                                                        });
                    }

                }else{
                        jsp_alerta('A', '<%=meLanbide68I18n.getMensaje(idiomaUsuario, "msg.msjNoSelecFila")%>');
                    }    
            }

        </script>
    </body>

</html>
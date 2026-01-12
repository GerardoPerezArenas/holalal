<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>

<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide68.i18n.MeLanbide68I18n" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide68.vo.TipDocLanbideVO" %>
<%@page import="es.altia.agora.business.util.GeneralValueObject"%>
<%@page import="es.altia.common.service.config.Config"%>
<%@page import="es.altia.common.service.config.ConfigServiceHelper"%>
<%@page import="org.apache.commons.lang.StringEscapeUtils"%>
<%@page import="java.util.ArrayList" %>
<%@page import="java.util.List" %>

<html>
    <head>
        <%
            int idiomaUsuario = 0;
            int codOrganizacion = 0;
            int apl = 5;
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
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script>
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/>        
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/estilo.css"/>
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/extension/melanbide68/melanbide68.css"/>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/TablaNueva.js"></script> 
        <script type="text/javascript">var APP_CONTEXT_PATH = '<%=request.getContextPath()%>'</script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/general.js"></script>
        <script type="text/javascript" src="<c:url value='/scripts/listaComboBox.js'/>"></script>
        <script type="text/javascript" src="<c:url value='/scripts/calendario.js'/>"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/validaciones.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/popup.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/melanbide68/tabUtils.js"></script>
        
    </head>
<body class="bandaBody"align="center" >
        <div class="contenidoPantalla" style="clear: both; padding: 2%; max-height: 100vh; overflow-y: auto; display: flex; flex-direction: column;">

        <form id="formMantenimientoMetadatos"style="display: flex; flex-direction: column; flex-grow: 1;">
            <div style="height:50px; width: 100%;">
                <table width="100%" style="height: 100%;" cellpadding="0px" cellspacing="0px" border="0px">
                    <tr>
                        <td class="txttitblanco"><%=meLanbide68I18n.getMensaje(idiomaUsuario,"label.tituloTablaMetadatos")%> </td>
                    </tr>
                </table>
            </div>  
                    
            <div style="width: 100%; text-align: left;">
                <div style="padding: 30px;"> 
                    <!-- etiqueta de tipos documentales Lanbide -->
                    <div>
                        <label class="etiqueta" style="text-align: center; position: relative; padding-left: 30px;"><%=meLanbide68I18n.getMensaje(idiomaUsuario,"label.TipDocDokusi.datosTipDoc.TipDocLanbide")%></label>
                    </div>
                     <!-- combo de tipos documentales Lanbide -->
                    <div style="padding-left: 30px;">
                        <input type="text" name="codListaTipDoc" id="codListaTipDoc" maxlength="5" size="5" class="inputTexto" value="" onkeyup="xAMayusculas(this);"/>
                        <input type="text" name="descListaTipDoc" id="descListaTipDoc" size="130" class="inputTexto" readonly="true" value="" /> 
                        <a href="" id="anchorListaTipDoc" name="anchorListaTipDoc">
                            <img src="<%=request.getContextPath()%>/images/listas/botondesplegable.gif" id="botonTipDoc"
                                 name="botonTipDoc" height="14" width="14" border="0" style="cursor:hand;">
                        </a>
                    </div>
                </div>                        
                        <div id="divTablaBotones" style="flex-grow: 1; width: 80%;display: flex; flex-direction: column; padding: 30px; max-height: auto; overflow-y: auto;">
                    <div>
                        <!-- tabla de Metadatos asociados a tipos documentales Lanbide 
                        <div 
                            id="divTablaMetadatos" style="padding: 30px; width:100%; height: 400px; text-align: center">
                        </div>--->
                <div id="divGeneral">
                             <div id="divTablaMetadatos" align="center" style="padding-left: 30px;"></div>
                        </div>
                        <!-- botones debajo de la tabla --->
                    <div class="botonera" style="width: 80%; text-align: center; margin-top: 20px;">
                              <input type="button" id="btnInsertarMetadato" name="btnInsertarMetadato" class="botonGeneral" value="<%=meLanbide68I18n.getMensaje(idiomaUsuario, "btn.insertar")%>" onclick="pulsarInsertarMetadato(true);">
                              <input type="button" id="btnModificarMetadato" name="btnModificarMetadato" class="botonGeneral" value="<%=meLanbide68I18n.getMensaje(idiomaUsuario, "btn.modificar")%>" onclick="pulsarModificarMetadato();">
                          <%--<input type="button" id="btnEliminarMetadato" name="btnEliminarMetadato" class="botonGeneral" value="<%=meLanbide68I18n.getMensaje(idiomaUsuario, "btn.eliminar")%>" onclick="pulsarEliminarMetadato(true);"> --%>                     
                              <input type="button" id="btnDeshabilitarMetadato" name="btnDeshabilitarMetadato" class="botonLargo" value="<%=meLanbide68I18n.getMensaje(idiomaUsuario, "btn.deshabilitar")%>" onclick="pulsarDeshabilitarMetadato(true);">
                        </div>
                    </div>
                </div>
            </div>
        </form>
        </div>
       <script type="text/javascript">
        
            var tabMetadatos;
            var listaMetadatos = new Array();
            var listaMetadatosTabla = new Array();
            var listaMetadatosTabla_titulos = new Array();
            
            //tabMetadatos = new FixedColumnTable(document.getElementById('divTablaMetadatos'), 820, 820, 'metadatos');
            tabMetadatos = new Tabla(true, '<%=descriptor.getDescripcion("buscar")%>', '<%=descriptor.getDescripcion("anterior")%>', '<%=descriptor.getDescripcion("siguiente")%>', '<%=descriptor.getDescripcion("mosFilasPag")%>', '<%=descriptor.getDescripcion("msgNoResultBusq")%>', '<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>', '<%=descriptor.getDescripcion("filtrDeTotal")%>', '<%=descriptor.getDescripcion("primero")%>', '<%=descriptor.getDescripcion("ultimo")%>', document.getElementById('divTablaMetadatos'));

            tabMetadatos.addColumna('300','left',"<%= meLanbide68I18n.getMensaje(idiomaUsuario,"label.metadato.tabla.col1")%>", "<%= meLanbide68I18n.getMensaje(idiomaUsuario,"label.metadato.tabla.col1.title")%>");    
            tabMetadatos.addColumna('180','left',"<%= meLanbide68I18n.getMensaje(idiomaUsuario,"label.metadato.tabla.col3")%>", "<%= meLanbide68I18n.getMensaje(idiomaUsuario,"label.metadato.tabla.col3.title")%>");
            tabMetadatos.addColumna('75','center',"<%= meLanbide68I18n.getMensaje(idiomaUsuario,"label.metadato.tabla.col2")%>", "<%= meLanbide68I18n.getMensaje(idiomaUsuario,"label.metadato.tabla.col2.title")%>");    
            tabMetadatos.addColumna('75','center',"<%= meLanbide68I18n.getMensaje(idiomaUsuario,"label.metadato.tabla.col4")%>", "<%= meLanbide68I18n.getMensaje(idiomaUsuario,"label.metadato.tabla.col4.title")%>");    
            
            //tabMetadatos.numColumnasFijas = 0;
            tabMetadatos.displayCabecera=true;
            
            tabMetadatos.height = 'auto';
            //tabMetadatos.altoCabecera = 30;
            //tabMetadatos.scrollWidth = 820;
            //tabMetadatos.dblClkFunction = 'dblClckTablaMetadatos';
            tabMetadatos.lineas = listaMetadatosTabla;
            tabMetadatos.displayTabla();
            //tabMetadatos.pack();
            
            document.getElementById("divTablaBotones").style.display="none";
          
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
            
            var listaCodigosTipDoc = new Array();
            var listaDescripcionesTipDoc = new Array();

            listaCodigosTipDoc[0] = "";
            listaDescripcionesTipDoc[0] = "";
                        
            contador = 0;

            <logic:iterate id="numTipdoc" name="listaTipDoc" scope="request">
                listaCodigosTipDoc[contador] = ['<bean:write name="numTipdoc" property="codTipDocBBDD" />'];
                listaDescripcionesTipDoc[contador] = '<bean:write name="numTipdoc" property="descTipDoc" />';
                contador++;
            </logic:iterate>

            var comboListaTipDoc = new Combo("ListaTipDoc");
            comboListaTipDoc.addItems(listaCodigosTipDoc, listaDescripcionesTipDoc); 
           // document.getElementById("descListaTipDoc").disabled=true;
            comboListaTipDoc.change = cargarTablaMetadatos;

          // funcion que carga la tabla de Metadatos existentes para el tipo documental seleccionado
            function cargarTablaMetadatos()
            {                                
                var codTipDocSelec = document.getElementById("codListaTipDoc").value;
                
                if (codTipDocSelec!=null & codTipDocSelec!="")
                { 
                //Hazce visible la tabla 
                    document.getElementById("divTablaBotones").style.display=""
                   
                    var ajax = getXMLHttpRequest();
                    var nodos = null;
                    var CONTEXT_PATH = '<%=request.getContextPath()%>'
                    var url = CONTEXT_PATH + "/PeticionModuloIntegracion.do";
                    var parametros = "";

                    var control = new Date();

                    parametros = "tarea=preparar&modulo=MELANBIDE68&operacion=cargarTablaMetadatos&tipo=0&codTipDoc=" + codTipDocSelec 
                               + "&control=" + control.getTime();

                    try {
                        ajax.open("POST", url, false);
                        ajax.setRequestHeader("Content-Type", "application/x-www-form-urlencoded; charset=iso-8859-15");
                        ajax.setRequestHeader("Accept", "text/xml, application/xml, text/plain");
                        ajax.send(parametros);
                        if (ajax.readyState == 4 && ajax.status == 200) {
                            var xmlDoc = null;
                            if (navigator.appName.indexOf("Internet Explorer") != -1) {
                                // En IE el XML viene en responseText y no en la propiedad responseXML
                                var text = ajax.responseText;
                                xmlDoc = new ActiveXObject("Microsoft.XMLDOM");
                                xmlDoc.async = "false";
                                xmlDoc.loadXML(text);
                            } else {
                                // En el resto de navegadores el XML se recupera de la propiedad responseXML
                                xmlDoc = ajax.responseXML;
                            }   //if(navigator.appName.indexOf("Internet Explorer")!=-1){
                        }    //if (ajax.readyState==4 && ajax.status==200){
                        nodos = xmlDoc.getElementsByTagName("RESPUESTA");
                        var codigoOperacion = nodos[0].childNodes[0].childNodes[0].nodeValue; 
                        var listaMetadatos = new Array();

                        if (codigoOperacion == 0) 
                        {   
                            var fila = new Array();
                            var elemento = nodos[0];
                            var hijos = elemento.childNodes;
                            var codigoOperacion = null;
                            var nodoFila;
                            var nodoFila;
                            var hijosFila;
                            var nodoCampo;     

                            var hijosFila;
                            for (j = 0; hijos != null && j < hijos.length; j++) {

                                if (hijos[j].nodeName == "FILA") {
                                    nodoFila = hijos[j];
                                    hijosFila = nodoFila.childNodes;
                                    for (var cont = 0; cont < hijosFila.length; cont++) {
                                        if (hijosFila[cont].nodeName == "METADATO") {
                                          if (hijosFila[cont].childNodes.length > 0) {
                                              nodoCampo = hijosFila[cont];
                                              if(nodoCampo.childNodes.length > 0){
                                                  fila[0] = nodoCampo.childNodes[0].nodeValue;
                                              }
                                              else{
                                                  fila[0] = '-';
                                              }
                                          }
                                        }
                                        if (hijosFila[cont].nodeName == "METADATODCTM") {
                                          if (hijosFila[cont].childNodes.length > 0) {
                                              nodoCampo = hijosFila[cont];
                                              if(nodoCampo.childNodes.length > 0){
                                                  fila[1] = nodoCampo.childNodes[0].nodeValue;
                                              }
                                              else{
                                                  fila[1] = '-';
                                              }
                                          }
                                        }
                                        if (hijosFila[cont].nodeName == "OBLIGATORIO") {
                                           if (hijosFila[cont].childNodes.length > 0) {
                                               nodoCampo = hijosFila[cont];
                                               if(nodoCampo.childNodes.length > 0){
                                                   fila[2] = nodoCampo.childNodes[0].nodeValue;
                                               }
                                               else{
                                                   fila[2] = '-';
                                               }
                                           }
                                        }
                                        if (hijosFila[cont].nodeName == "DESHABILITADO") {
                                           if (hijosFila[cont].childNodes.length > 0) {
                                               nodoCampo = hijosFila[cont];
                                               if(nodoCampo.childNodes.length > 0){
                                                   fila[3] = nodoCampo.childNodes[0].nodeValue;
                                               }
                                               else{
                                                   fila[3] = '-';
                                               }
                                           }
                                        }                                        
                                    }
                                    listaMetadatos[j] = fila;
                                    fila = new Array();  
                                }
                            }
                        }   
                        recargarTablaMetadatos(listaMetadatos);
                    } catch(Err){
                        jsp_alerta("A",'<%=meLanbide68I18n.getMensaje(idiomaUsuario,"error.errorProcesarRespuesta")%>')
                    } //try-catch                    
                } //codTipDocSelec!=null & codTipDocSelec!=""
                else
                {
                    document.getElementById("divTablaBotones").style.display="none";
                    jsp_alerta("A",'<%=meLanbide68I18n.getMensaje(idiomaUsuario,"error.SeleccTipDoc")%>');
                } 
//                recargarTablaMetadatos(listaMetadatos);
            }    
            
            function recargarTablaMetadatos(result){
                var fila;
                listaMetadatos = new Array();
                listaMetadatosTabla = new Array();
                listaMetadatosTabla_titulos = new Array();

                for(var i = 1;i< result.length; i++){
                    fila = result[i];
  
                    if (fila[2]=="0") fila[2]="N";
                    else if (fila[2]=="1") fila[2]="S";
            
                    listaMetadatos[i-1] = [fila[0],fila[1],fila[2],fila[3]];
                    listaMetadatosTabla[i-1] = [fila[0],fila[1],fila[2],fila[3]];
                    listaMetadatosTabla_titulos[i-1] = [fila[0],fila[1],fila[2],fila[3]];
                }

                //tabMetadatos = new FixedColumnTable(document.getElementById('divTablaMetadatos'), 820, 820, 'metadatos');
                tabMetadatos = new Tabla(true, '<%=descriptor.getDescripcion("buscar")%>', '<%=descriptor.getDescripcion("anterior")%>', '<%=descriptor.getDescripcion("siguiente")%>', '<%=descriptor.getDescripcion("mosFilasPag")%>', '<%=descriptor.getDescripcion("msgNoResultBusq")%>', '<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>', '<%=descriptor.getDescripcion("filtrDeTotal")%>', '<%=descriptor.getDescripcion("primero")%>', '<%=descriptor.getDescripcion("ultimo")%>', document.getElementById('divTablaMetadatos'));

                tabMetadatos.addColumna('300','left',"<%= meLanbide68I18n.getMensaje(idiomaUsuario,"label.metadato.tabla.col1")%>", "<%= meLanbide68I18n.getMensaje(idiomaUsuario,"label.metadato.tabla.col1.title")%>");    
                tabMetadatos.addColumna('180','left',"<%= meLanbide68I18n.getMensaje(idiomaUsuario,"label.metadato.tabla.col3")%>", "<%= meLanbide68I18n.getMensaje(idiomaUsuario,"label.metadato.tabla.col3.title")%>");
                tabMetadatos.addColumna('75','center',"<%= meLanbide68I18n.getMensaje(idiomaUsuario,"label.metadato.tabla.col2")%>", "<%= meLanbide68I18n.getMensaje(idiomaUsuario,"label.metadato.tabla.col2.title")%>");    
                tabMetadatos.addColumna('75','center',"<%= meLanbide68I18n.getMensaje(idiomaUsuario,"label.metadato.tabla.col4")%>", "<%= meLanbide68I18n.getMensaje(idiomaUsuario,"label.metadato.tabla.col4.title")%>");    
                
                //tabMetadatos.numColumnasFijas = 0;
                tabMetadatos.displayCabecera=true;       

                //for(var cont = 0; cont < listaMetadatosTabla.length; cont++){
                //    tabMetadatos.addFilaConFormato(listaMetadatosTabla[cont], listaMetadatosTabla_titulos[cont])
                //}

                tabMetadatos.height = 'auto';
                //tabMetadatos.altoCabecera = 30;
                //tabMetadatos.scrollWidth = 820;
                //tabMetadatos.dblClkFunction = 'dblClckTablaMetadatos';
                tabMetadatos.lineas = listaMetadatosTabla;
                tabMetadatos.displayTabla();
                //tabMetadatos.pack();
            }
            
            //function dblClckTablaMetadatos(rowID,tableName){
                //pulsarModificarMetadato();
            //}          
      
            function pulsarEliminarMetadato(){ 
                var codTipDoc = document.getElementById("codListaTipDoc").value;
                var fila;

                if(tabMetadatos.selectedIndex != -1) {
                    fila = tabMetadatos.selectedIndex;
                    var resultado = jsp_alerta('', '<%=meLanbide68I18n.getMensaje(idiomaUsuario, "msg.preguntaEliminar")%>');
                    if (resultado == 1){
                        var ajax = getXMLHttpRequest();
                        var nodos = null;
                        var CONTEXT_PATH = '<%=request.getContextPath()%>'
                        var url = CONTEXT_PATH + "/PeticionModuloIntegracion.do";
                        var parametros = "";
                        var control = new Date();
                        var listaMetadatos = new Array();
                        parametros = 'tarea=preparar&modulo=MELANBIDE68&operacion=cargarEliminarMetadato&tipo=0&codTipDoc='+codTipDoc
                                      +'&codMetadato='+tabMetadatos.lineas[fila][0]
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
                            cargarTablaMetadatos();
                            // vuelvo a colocarme al principio del XML para recuperar el código de operacion
                            nodos = xmlDoc.getElementsByTagName("RESPUESTA"); 
                            var codigoOperacion = nodos[0].childNodes[0].childNodes[0].nodeValue; 
                            if(codigoOperacion=="0"){
                                jsp_alerta("A",'<%=meLanbide68I18n.getMensaje(idiomaUsuario,"msg.registroEliminadoOK")%>');
                            }else if(codigoOperacion=="1"){
                                jsp_alerta("A",'<%=meLanbide68I18n.getMensaje(idiomaUsuario,"error.errorEliminarBD")%>');
                            }else if(codigoOperacion=="2"){
                                jsp_alerta("A",'<%=meLanbide68I18n.getMensaje(idiomaUsuario,"error.errorEliminarGen")%>');
                            }else if(codigoOperacion=="3"){
                                jsp_alerta("A",'<%=meLanbide68I18n.getMensaje(idiomaUsuario,"error.pasoParametrosEliminar")%>');
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
        
        function pulsarInsertarMetadato(){
            var control = new Date();
            var result = null;
            var codTipDoc = document.getElementById("codListaTipDoc").value;

            if (codTipDoc!=null & codTipDoc!="")
            { 
                if(navigator.appName.indexOf("Internet Explorer")!=-1){
                    lanzarPopUpModal('<%=request.getContextPath()%>/PeticionModuloIntegracion.do?tarea=preparar&modulo=MELANBIDE68&operacion=cargarNuevoMetadato&tipo=0&codTipDoc='+codTipDoc
                                                                                                      +'&control='+control.getTime(),330,1030,'no','no', function(result){
                                                                if (result != undefined){
                                                                        if(result[0] == '0'){
                                                                                recargarTablaMetadatos(result);								
                                                                        }
                                                                }
                                                        });
                }else{
                    lanzarPopUpModal('<%=request.getContextPath()%>/PeticionModuloIntegracion.do?tarea=preparar&modulo=MELANBIDE68&operacion=cargarNuevoMetadato&tipo=0&codTipDoc='+codTipDoc
                                                                                                      +'&control='+control.getTime(),330,1030,'no','no', function(result){
                                                                if (result != undefined){
                                                                        if(result[0] == '0'){
                                                                                recargarTablaMetadatos(result);								
                                                                        }
                                                                }
                                                        });
                }
            }else
            {
                jsp_alerta("A",'<%=meLanbide68I18n.getMensaje(idiomaUsuario,"error.SeleccTipDoc")%>');
            }     
        }

        function pulsarModificarMetadato(){
            var fila;

            if(tabMetadatos.selectedIndex != -1) {
                fila = tabMetadatos.selectedIndex;
                var control = new Date();
                //var result = null;
                var codTipDoc = document.getElementById("codListaTipDoc").value;

                if(navigator.appName.indexOf("Internet Explorer")!=-1){
                    lanzarPopUpModal('<%=request.getContextPath()%>/PeticionModuloIntegracion.do?tarea=preparar&modulo=MELANBIDE68&operacion=cargarModificarMetadato&tipo=0&codTipDoc='+codTipDoc
                                                                    +'&codMetadato='+tabMetadatos.lineas[fila][0]
                                                                    +'&metadatoDCTM='+tabMetadatos.lineas[fila][1]
                                                                    +'&obligatorio='+tabMetadatos.lineas[fila][2]
                                                                    +'&control='+control.getTime(),400,1030,'no','no', function(result){
                                                            if (result != undefined){
                                                                // aquí también tengo que ver la forma de recuperar el CodigoOperacion porque si no me va a dar error
                                                                    if(result[0] == '0'){
                                                                            recargarTablaMetadatos(result);								
                                                                    }
                                                            }
                                                    });
                }else{
                    lanzarPopUpModal('<%=request.getContextPath()%>/PeticionModuloIntegracion.do?tarea=preparar&modulo=MELANBIDE68&operacion=cargarModificarMetadato&tipo=0&codTipDoc='+codTipDoc
                                                                    +'&codMetadato='+tabMetadatos.lineas[fila][0]
                                                                    +'&metadatoDCTM='+tabMetadatos.lineas[fila][1]
                                                                    +'&obligatorio='+tabMetadatos.lineas[fila][2]
                                                                    +'&control='+control.getTime(),400,1030,'no','no', function(result){
                                                            if (result != undefined){
                                                                // aquí también tengo que ver la forma de recuperar el CodigoOperacion porque si no me va a dar error
                                                                    if(result[0] == '0'){
                                                                            recargarTablaMetadatos(result);								
                                                                    }
                                                            }
                                                    });
                }

            }else{
                    jsp_alerta('A', '<%=meLanbide68I18n.getMensaje(idiomaUsuario, "msg.msjNoSelecFila")%>');
                }    
        }
        
        function pulsarDeshabilitarMetadato(){
                var codTipDoc = document.getElementById("codListaTipDoc").value;
                var fila;

                if(tabMetadatos.selectedIndex != -1) {
                    fila = tabMetadatos.selectedIndex;
                    if(tabMetadatos.lineas[fila][3]=='N'){
                        var resultado = jsp_alerta('', '<%=meLanbide68I18n.getMensaje(idiomaUsuario, "msg.preguntaDeshabilitar")%>');
                    }else {
                        var resultado = jsp_alerta('', '<%=meLanbide68I18n.getMensaje(idiomaUsuario, "msg.preguntaHabilitar")%>');
                    }                    
                    if (resultado == 1){
                        var ajax = getXMLHttpRequest();
                        var nodos = null;
                        var CONTEXT_PATH = '<%=request.getContextPath()%>'
                        var url = CONTEXT_PATH + "/PeticionModuloIntegracion.do";
                        var parametros = "";
                        var control = new Date();
                        var listaMetadatos = new Array();
                        parametros = 'tarea=preparar&modulo=MELANBIDE68&operacion=cargarDeshabilitarMetadato&tipo=0&codTipDoc='+codTipDoc
                                      +'&codMetadato='+encodeURI(tabMetadatos.lineas[fila][0])
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
                            cargarTablaMetadatos();
                            // vuelvo a colocarme al principio del XML para recuperar el código de operacion
                            nodos = xmlDoc.getElementsByTagName("RESPUESTA"); 
                            var codigoOperacion = nodos[0].childNodes[0].childNodes[0].nodeValue; 
                            if(codigoOperacion=="0"){
                                jsp_alerta("A",'<%=meLanbide68I18n.getMensaje(idiomaUsuario,"msg.registroDeshabilitadoOK")%>');
                            }else if(codigoOperacion=="1"){
                                jsp_alerta("A",'<%=meLanbide68I18n.getMensaje(idiomaUsuario,"error.errorProcesarRespuesta")%>');
                            }else if(codigoOperacion=="2"){
                                jsp_alerta("A",'<%=meLanbide68I18n.getMensaje(idiomaUsuario,"error.errorModificarGen")%>');
                            }else if(codigoOperacion=="3"){
                                jsp_alerta("A",'<%=meLanbide68I18n.getMensaje(idiomaUsuario,"error.pasoParametrosDeshabilitar")%>');
                            }else{
                                jsp_alerta("A",'<%=meLanbide68I18n.getMensaje(idiomaUsuario,"error.errorModificarGen")%>');
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
        
   document.addEventListener("DOMContentLoaded", function() {
    let elemento = document.querySelector(".xC");

    if (elemento) {
        requestAnimationFrame(() => {
            elemento.style.height = "300px";
            elemento.style.overflowY = "auto";
        });
    }
});



       </script>
    </body>
</html>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>

<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide39.i18n.MeLanbide39I18n" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide39.vo.CpePuestoVO" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide39.util.ConstantesMeLanbide39" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide39.vo.SelectItem"%>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.agora.business.util.GeneralValueObject"%>
<%@page import="es.altia.common.service.config.Config"%>
<%@page import="es.altia.common.service.config.ConfigServiceHelper"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Date"%>
<%@page import="java.util.Vector"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="org.apache.commons.lang.StringEscapeUtils"%>

<html>
    <head> 
        <%
            int idiomaUsuario = 1;
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
            //Clase para internacionalizar los mensajes de la aplicación.
            MeLanbide39I18n meLanbide39I18n = MeLanbide39I18n.getInstance();

            Config m_Config = ConfigServiceHelper.getConfig("common");
            String statusBar = m_Config.getString("JSP.StatusBar");
            String codOrganizacion  = request.getParameter("codOrganizacionModulo");
    
    
            String tituloPagina = meLanbide39I18n.getMensaje(idiomaUsuario,"label.busqTitulaciones.tituloPagina");
        %>

        <%!
            // Funcion para escapar strings para javascript
            private String escape(String str) 
            {
                return StringEscapeUtils.escapeJavaScript(str);
            }
        %>
        <title><%=tituloPagina%></title>

        
        <jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
<jsp:setProperty name="descriptor"  property="idi_cod" value="<%=idiomaUsuario%>" />
<jsp:setProperty name="descriptor"  property="apl_cod" value="<%=apl%>" />
<link rel="StyleSheet" media="screen" type="text/css" href="<%=request.getContextPath()%><%=css%>">
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/>
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/extension/melanbide39/melanbide39.css"/>
        <script type="text/javascript">
            var APP_CONTEXT_PATH = '<%=request.getContextPath()%>';
        </script>
        
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/melanbide39/cpeUtils.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/general.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/TablaNueva.js"/></script>
        
        <script type="text/javascript">
            
            function getXMLHttpRequest(){
                var aVersions = [ "MSXML2.XMLHttp.5.0",
                    "MSXML2.XMLHttp.4.0","MSXML2.XMLHttp.3.0",
                    "MSXML2.XMLHttp","Microsoft.XMLHttp"
                    ];

                if (window.XMLHttpRequest){
                    // para IE7, Mozilla, Safari, etc: que usen el objeto nativo
                    return new XMLHttpRequest();
                }else if (window.ActiveXObject){
                    // de lo contrario utilizar el control ActiveX para IE5.x y IE6.x
                    for (var i = 0; i < aVersions.length; i++) {
                        try {
                            var oXmlHttp = new ActiveXObject(aVersions[i]);
                            return oXmlHttp;
                        }catch (error) {
                        //no necesitamos hacer nada especial
                        }
                    }
                }else{
                    return null;
                }
            }
            
            function cerrarVentana(){
                if(navigator.appName=='Microsoft Internet Explorer') { 
                      window.parent.window.opener=null; 
                      window.parent.window.close(); 
                } else if(navigator.appName=="Netscape") { 
                      top.window.opener = top; 
                      top.window.open('','_parent',''); 
                      top.window.close(); 
                 }else{
                     window.close(); 
                 } 
            }
            
            function buscar(){
                if (document.getElementById('txtCodigo').value!='' || (document.getElementById('txtDescripcion').value).length >3){
                    document.getElementById('msgBuscando').style.display="inline";
                    barraProgresoCpe('on', 'barraProgresoBusqTitulaciones');
                    var ajax = getXMLHttpRequest();
                    var nodos = null;
                    var CONTEXT_PATH = '<%=request.getContextPath()%>'
                    var url = CONTEXT_PATH + "/PeticionModuloIntegracion.do";
                    var parametros = "";
                    var control = new Date();
                    parametros = 'tarea=preparar&modulo=MELANBIDE39&operacion=buscarTitulaciones&tipo=0'
                        +'&codigo='+document.getElementById('txtCodigo').value
                        +'&desc='+escape(document.getElementById('txtDescripcion').value)
                        +'&control='+control.getTime();
                    try{
                        ajax.open("POST",url,false);
                        ajax.setRequestHeader("Content-Type","application/x-www-form-urlencoded; charset=iso-8859-15");
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
                    var listaTitulaciones = new Array();
                    var fila = new Array();
                    var nodoFila;
                    var hijosFila;
                    var nodoTitulacion;
                    var hijosTitulacion;
                    var nodoCampo;
                    var j;

                    for(j=0;hijos!=null && j<hijos.length;j++){
                        if(hijos[j].nodeName=="CODIGO_OPERACION"){                            
                            codigoOperacion = hijos[j].childNodes[0].nodeValue;
                            listaTitulaciones[j] = codigoOperacion;
                        }else if(hijos[j].nodeName=="TITULACIONES"){      
                            nodoFila = hijos[j];
                            hijosFila = nodoFila.childNodes;
                            for(var cont = 0; cont < hijosFila.length; cont++){
                                if(hijosFila[cont].nodeName=="TITULACION"){
                                    nodoTitulacion = hijosFila[cont];
                                    hijosTitulacion = nodoTitulacion.childNodes;
                                    for(var elem = 0; elem < hijosTitulacion.length; elem++){
                                        if(hijosTitulacion[elem].nodeName=="CODIGO_INTERNO"){
                                            nodoCampo = hijosTitulacion[elem];
                                            if(nodoCampo.childNodes.length > 0){
                                                fila[0] = nodoCampo.childNodes[0].nodeValue;
                                            }
                                            else{
                                                fila[0] = '0';
                                            }
                                        }else if(hijosTitulacion[elem].nodeName=="CODIGO_VISIBLE"){
                                            nodoCampo = hijosTitulacion[elem];
                                            if(nodoCampo.childNodes.length > 0){
                                                fila[1] = nodoCampo.childNodes[0].nodeValue;
                                            }
                                            else{
                                                fila[1] = '0';
                                            }
                                        }else if(hijosTitulacion[elem].nodeName=="DESCRIPCION"){
                                            nodoCampo = hijosTitulacion[elem];
                                            if(nodoCampo.childNodes.length > 0){
                                                fila[2] = nodoCampo.childNodes[0].nodeValue;
                                            }
                                            else{
                                                fila[2] = '0';
                                            }
                                        }
                                    }
                                }
                                listaTitulaciones[cont+1] = fila;
                                fila = new Array();
                            }
                        }
                    }//for(j=0;hijos!=null && j<hijos.length;j++)
                        if(codigoOperacion=="0"){
                            recargarTablaTitulaciones(listaTitulaciones);
                        }else if(codigoOperacion=="1"){
                            jsp_alerta("A",'<%=meLanbide39I18n.getMensaje(idiomaUsuario,"error.errorBD")%>');
                        }else if(codigoOperacion=="2"){
                            jsp_alerta("A",'<%=meLanbide39I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                        }else if(codigoOperacion=="3"){
                            jsp_alerta("A",'<%=meLanbide39I18n.getMensaje(idiomaUsuario,"error.pasoParametros")%>');
                        }else if(codigoOperacion=="4"){
                            jsp_alerta("A",'<%=meLanbide39I18n.getMensaje(idiomaUsuario,"error.datosNoValidos")%>');
                        }else{
                            jsp_alerta("A",'<%=meLanbide39I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                        }//if(
                    }
                    catch(Err){
                        jsp_alerta("A",'<%=meLanbide39I18n.getMensaje(idiomaUsuario,"error.errorProcesarRespuesta")%>');
                    }//try-catch

                    barraProgresoCpe('off', 'barraProgresoBusqTitulaciones');
                } else {
                    jsp_alerta("A",'<%=meLanbide39I18n.getMensaje(idiomaUsuario,"msg.errorBusqTitulacion")%>');
                }
            }
        
        function recargarTablaTitulaciones(result){
            var fila;
            listaTitulacionesBusq = new Array();
            listaTitulacionesBusqTabla = new Array();
            for(var i = 1;i< result.length; i++){
                fila = result[i];
                listaTitulacionesBusq[i-1] = fila;
                listaTitulacionesBusqTabla[i-1] = [fila[1], fila[2]];
            }

            tabTitulacionesBusq = new Tabla(true,'<%=descriptor.getDescripcion("buscar")%>','<%=descriptor.getDescripcion("anterior")%>','<%=descriptor.getDescripcion("siguiente")%>','<%=descriptor.getDescripcion("mosFilasPag")%>','<%=descriptor.getDescripcion("msgNoResultBusq")%>','<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>','<%=descriptor.getDescripcion("filtrDeTotal")%>','<%=descriptor.getDescripcion("primero")%>','<%=descriptor.getDescripcion("ultimo")%>',document.getElementById('listaTitulacionesBusq'), 924);
            tabTitulacionesBusq.addColumna('150','left',"<%= meLanbide39I18n.getMensaje(idiomaUsuario,"busqTitulaciones.tablaResultados.col1")%>");
            tabTitulacionesBusq.addColumna('774','left',"<%= meLanbide39I18n.getMensaje(idiomaUsuario,"busqTitulaciones.tablaResultados.col2")%>");

            tabTitulacionesBusq.displayCabecera=true;
            tabTitulacionesBusq.height = 100;
            tabTitulacionesBusq.lineas=listaTitulacionesBusqTabla;
            tabTitulacionesBusq.displayTabla();
            if(navigator.appName.indexOf("Internet Explorer")!=-1){
                try{
                    var div = document.getElementById('listaTitulacionesBusq');
                    div.children[0].children[0].children[0].children[0].children[0].children[0].children[1].style.width = '100%';
                    div.children[0].children[1].children[0].children[0].children[0].children[1].style.width = '100%';
                    }
                catch(err){
                }
            }
            
            document.getElementById('lblNumResultados').innerText = '<%=meLanbide39I18n.getMensaje(idiomaUsuario,"label.busqTitulaciones.lblResultados1")%> '+(result.length - 1)+' <%=meLanbide39I18n.getMensaje(idiomaUsuario,"label.busqTitulaciones.lblResultados2")%>';
        }
        
        function aceptar(){
            if(tabTitulacionesBusq.selectedIndex != -1) {
                self.parent.opener.retornoXanelaAuxiliar(listaTitulacionesBusq[tabTitulacionesBusq.selectedIndex]);
                cerrarVentana();
            } 
            else{
                jsp_alerta('A', '<%=meLanbide39I18n.getMensaje(idiomaUsuario, "msg.msjNoSelecFila")%>');
            }
        }
        
        function limpiar(){
            document.getElementById('txtCodigo').value = '';
            document.getElementById('txtDescripcion').value = '';
        }
        </script>
    </head>
    <body id="cuerpoBusqTitulaciones" style="text-align: left;" class="contenidoPantalla">
        <div id="divCuerpo">
        <form  id="formBusqTitulaciones">
                <div id="barraProgresoBusqTitulaciones" style="position: absolute; z-index:10; visibility: hidden; top: 30%; left: 30%;">
                    <table width="100%" border="0px" cellpadding="0px" cellspacing="0px" border="0px">
                        <tr>
                            <td align="center" valign="middle">
                                <table class="contenedorHidepage" cellpadding="0px" cellspacing="0px" border="0px">
                                    <tr>
                                        <td>
                                            <table width="349px" height="100%">
                                                <tr>
                                                    <td colspan="3" style="height:70%;text-align:center;valign:middle;">
                                                        <span id="msgBuscando">
                                                            <%=meLanbide39I18n.getMensaje(idiomaUsuario, "msg.buscando")%>
                                                        </span>
                                                    </td>
                                                </tr>
                                                <tr>
                                                    <td style="width:5%;height:20%;"></td>
                                                    <td class="imagenHide"></td>
                                                    <td style="width:5%;height:20%;"></td>
                                                </tr>
                                                <tr>
                                                    <td colspan="3" style="height:10%" ></td>
                                                </tr>
                                            </table>
                                        </td>
                                    </tr>
                                </table>
                            </td>
                        </tr>
                    </table>
                </div>
                <fieldset id="fieldsetCriterios" name="fieldsetCriterios" style="width: 100%;">
                    <legend class="legendAzul"><%=meLanbide39I18n.getMensaje(idiomaUsuario,"legend.busqTitulaciones.criterios")%></legend>
                    <div class="lineaFormulario" style="text-align: right;">
                        <div style="float: left; width: 42px; text-align: left;">
                            <%=meLanbide39I18n.getMensaje(idiomaUsuario,"label.busqTitulaciones.criterios.codigo")%>
                        </div>
                        <div style="width: auto; float: left; margin-left: 10px;">
                            <input type="text" maxlength="10" size="10" id="txtCodigo" name="txtCodigo" class="inputTexto" />
                        </div>
                        <div style="float: left; width: 70px; text-align: left; margin-left: 30px;">
                            <%=meLanbide39I18n.getMensaje(idiomaUsuario,"label.busqTitulaciones.criterios.descripcion")%>
                        </div>
                        <div style="width: auto; float: left; margin-left: 10px;">
                            <input type="text" maxlength="500" size="108" id="txtDescripcion" name="txtDescripcion" class="inputTexto" />
                        </div>
                    </div>
                    <div class="botonera" style="padding-top: 15px;">
                        <input type="button" id="btnBuscar" name="btnBuscar" class="botonGeneral" value="<%=meLanbide39I18n.getMensaje(idiomaUsuario,"btn.buscar")%>" onclick="buscar();">
                        <input type="button" id="btnLimpiar" name="btnLimpiar" class="botonGeneral" value="<%=meLanbide39I18n.getMensaje(idiomaUsuario,"btn.limpiar")%>" onclick="limpiar();">
                    </div>
                </fieldset>
                <fieldset id="fieldsetResultados" name="fieldsetResultados" style="width: 98%;">
                    <legend class="legendAzul"><%=meLanbide39I18n.getMensaje(idiomaUsuario,"legend.busqTitulaciones.resultados")%></legend>
                    <div class="lineaFormulario" style="text-align: right;">
                        <label id="lblNumResultados"></label>
                    </div>
                    <div id="listaTitulacionesBusq" class="tablaPers" style="padding: 5px; width:900px; height: 300px; text-align: center; overflow-x: auto; overflow-y: auto;margin:0px;margin-top:0px;" align="center"></div>
                </fieldset>
                <div class="botonera">
                    <input type="button" id="btnAceptar" name="btnAceptar" class="botonGeneral" value="<%=meLanbide39I18n.getMensaje(idiomaUsuario,"btn.aceptar")%>" onclick="aceptar();">
                    <input type="button" id="btnCancelar" name="btnCancelar" class="botonGeneral" value="<%=meLanbide39I18n.getMensaje(idiomaUsuario,"btn.cancelar")%>" onclick="cerrarVentana();">
                </div>
        </form>
        </div>
        <script type="text/javascript">
            //Tabla solicitud de horas de orientacion
            var tabTitulacionesBusq;
            var listaTitulacionesBusq = new Array();
            var listaTitulacionesBusqTabla = new Array();

            tabTitulacionesBusq = new Tabla(true,'<%=descriptor.getDescripcion("buscar")%>','<%=descriptor.getDescripcion("anterior")%>','<%=descriptor.getDescripcion("siguiente")%>','<%=descriptor.getDescripcion("mosFilasPag")%>','<%=descriptor.getDescripcion("msgNoResultBusq")%>','<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>','<%=descriptor.getDescripcion("filtrDeTotal")%>','<%=descriptor.getDescripcion("primero")%>','<%=descriptor.getDescripcion("ultimo")%>',document.getElementById('listaTitulacionesBusq'), 924);
            tabTitulacionesBusq.addColumna('150','left',"<%= meLanbide39I18n.getMensaje(idiomaUsuario,"busqTitulaciones.tablaResultados.col1")%>");
            tabTitulacionesBusq.addColumna('774','left',"<%= meLanbide39I18n.getMensaje(idiomaUsuario,"busqTitulaciones.tablaResultados.col2")%>");

            tabTitulacionesBusq.displayCabecera=true;
            tabTitulacionesBusq.height = 100;
            tabTitulacionesBusq.displayTabla();
            if(navigator.appName.indexOf("Internet Explorer")!=-1){
                try{
                    var div = document.getElementById('listaTitulacionesBusq');
                    div.children[0].children[0].children[0].children[0].children[0].children[0].children[1].style.width = '100%';
                    div.children[0].children[1].children[0].children[0].children[0].children[1].style.width = '100%';
                    }
                catch(err){
                }
            }
        </script>
    </body>
</html>

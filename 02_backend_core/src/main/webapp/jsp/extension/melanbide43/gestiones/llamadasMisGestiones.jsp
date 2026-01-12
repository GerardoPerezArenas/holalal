<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>

<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide43.i18n.MeLanbide43I18n" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide43.vo.ProcedimientoVO" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide43.vo.FilaLlamadasMisGestVO" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide43.vo.TramiteVO" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide43.util.ConstantesMeLanbide43" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide43.util.ConfigurationParameter" %>
<%@page import="java.util.ArrayList" %>
<%@page import="java.util.List" %>
<%@page import="java.text.SimpleDateFormat" %>
<%@page import="org.apache.commons.lang.StringEscapeUtils"%>

<html>
    <head>
        <meta http-equiv="X-UA-Compatible" content="IE=edge" />
        <%
            MeLanbide43I18n meLanbide43I18n = MeLanbide43I18n.getInstance();
            //ConstantesMeLanbide43 constantesMelanbide43

            int idiomaUsuario = 1;
            int apl = 5;
            String css = "";
            try
            {
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

            }
            catch(Exception ex)
            {
            }
           
        SimpleDateFormat format = new SimpleDateFormat(ConstantesMeLanbide43.FORMATO_FECHA);
    %>
    <%!
        // Funcion para escapar strings para javascript
        private String escape(String str) 
        {
            return StringEscapeUtils.escapeJavaScript(str);
        }
    %>
        <jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
        <jsp:setProperty name="descriptor"  property="idi_cod" value="<%=idiomaUsuario%>" />
        <jsp:setProperty name="descriptor"  property="apl_cod" value="<%=apl%>" />
        
        <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/css/font-awesome.min.css'/>" media="all">
        <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/less/animated.less'/>" media="all">
        
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/bootstrap413/bootstrap.min.css" media="all"/>
        <link rel="StyleSheet" media="screen" type="text/css" href="<%=request.getContextPath()%><%=css%>">
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script>
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/>        
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/estilo.css"/>
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/extension/melanbide43/melanbide43.css"/>
        
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/bootstrap413/bootstrap.min.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/melanbide43/FixedColumnsTable.js"></script>        
        <script type="text/javascript">var APP_CONTEXT_PATH = '<%=request.getContextPath()%>'</script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/general.js"></script>
        <script type="text/javascript" src="<c:url value='/scripts/listaComboBox.js'/>"></script>
        <script type="text/javascript" src="<c:url value='/scripts/calendario.js'/>"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/validaciones.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/popup.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/melanbide43/JavaScriptUtil.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/melanbide43/Parsers.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/melanbide43/InputMask.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/melanbide43/lanbide.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/melanbide43/ecaUtils.js"></script>
        <!-- Eventos onKeyPress compatibilidad firefox  -->
        <!--script type="text/javascript" src="http://code.jquery.com/jquery-1.4.4.js"></script-->

    </head>
    <body class="bandaBody">
        <jsp:include page="/jsp/hidepage.jsp" flush="true">
            <jsp:param name='cargaDatos' value='<%=descriptor.getDescripcion("msjCargDatos")%>'/>
        </jsp:include>
        <div class="contenidoPantalla" style="clear: both;">
            <form>
                <div style="width: 100%; text-align: left;">
                    <div>
                        <h2 class="sub3titulo" id="pestana331"><%=meLanbide43I18n.getMensaje(idiomaUsuario,"label.LlamadasMisGestiones")%></h2>
                        <div style="margin: 10px;"> 
                            <fieldset id="fieldsetDatosIntegMisGest" name="fieldsetDatosIntegMisGest" style="width: 99%;">
                                <legend class="legendAzul"><%=meLanbide43I18n.getMensaje(idiomaUsuario,"legend.misGestiones.filtros")%></legend>
                                    <div class="form-group">
                                        <label for="listaProcedimiento" class="etiqueta"><%=meLanbide43I18n.getMensaje(idiomaUsuario, "label.Procedimientos")%></label>
                                        <select class="selectpicker form-control" data-actions-box="true" style="width: fit-content" name="listaProcedimiento" id="listaProcedimiento" title="<%=meLanbide43I18n.getMensaje(idiomaUsuario, "label.procedimientos")%>">
                                            <option value=""><%=meLanbide43I18n.getMensaje(idiomaUsuario, "label.select.option.default")%></option>
                                            <c:forEach items="${listaProcedimiento}" var="elementLista" varStatus="contador">
                                                <option value="<c:out value="${elementLista.codProc}"/>" title="<c:out value="${elementLista.descProc}"/>"><c:out value="${elementLista.codProc}"/></option>
                                            </c:forEach>
                                        </select>
                                    </div>
                                    <div style="margin: 10px;">
                                        <label class="etiqueta" style="text-align: center; position: relative; margin: 5px; margin-right: 75px"><%=meLanbide43I18n.getMensaje(idiomaUsuario,"label.Expediente")%></label>
                                        <input type="text" maxlength="30" size="30" id="numExped" name="numExped" value="" class="inputTexto" />
                                    </div>
                                    <!-- fechas desde y hasta -->
                                    <div style="margin: 10px;">
                                        <label class="etiqueta" style="text-align: center; position: relative; margin: 5px; margin-right: 25px"><%=meLanbide43I18n.getMensaje(idiomaUsuario,"label.FechaGenerado")%></label>
                                        <label class="etiqueta" style="text-align: center; position: relative; margin: 5px; margin-right: 25px"><%=meLanbide43I18n.getMensaje(idiomaUsuario,"label.FecDesde")%></label>
                                        <!-- meter aqu� el icono de calendario -->
                                        <input type="text" class="inputTxtFecha" size="10" maxlength="10" id="fecDesde" name="fecDesde" onkeyup="return SoloCaracteresFecha(this);" 
                                               onblur="javascript:return comprobarFechaEca(this, '<%=meLanbide43I18n.getMensaje(idiomaUsuario, "msg.fechaNoVal")%>');" onfocus="this.select();"/>
                                        <a href="javascript:calClick(event);" onclick="mostrarCalFecDesde(event);return false;" style="text-decoration:none;" >
                                            <img style="border: 0; height: 17" id="calFecDesde" name="calFecDesde" alt="<%=meLanbide43I18n.getMensaje(idiomaUsuario,"label.alt.fecDesde")%>" 
                                                 src="<c:url value='/images/calendario/icono.gif'/>">   
                                         
                                        </a>
                                        <label class="etiqueta" style="text-align: center; position: relative; margin: 5px; margin-right: 25px"><%=meLanbide43I18n.getMensaje(idiomaUsuario,"label.FecHasta")%></label>
                                        <!-- meter aqu� el icono de calendario -->
                                        <input type="text" class="inputTxtFecha" size="10" maxlength="10" id="fecHasta" name="fecHasta" onkeyup="return SoloCaracteresFecha(this);" 
                                               onblur="javascript:return comprobarFechaEca(this, '<%=meLanbide43I18n.getMensaje(idiomaUsuario, "msg.fechaNoVal")%>');" onfocus="this.select();"/>
                                        <a href="javascript:calClick(event);" onclick="mostrarCalFecHasta(event);return false;" style="text-decoration:none;" >
                                            <img style="border: 0" height="17" id="calFecHasta" name="calFecHasta" alt="<%=meLanbide43I18n.getMensaje(idiomaUsuario,"label.alt.fecHasta")%>" 
                                                 src="<c:url value='/images/calendario/icono.gif'/>">
                                        </a>
                                    </div>
                                    <!-- desplegable de resultado -->
                                     <div style="margin: 10px;">
                                        <label class="etiqueta" style="text-align: center; position: relative; margin: 5px; margin-right: 50px;"><%=meLanbide43I18n.getMensaje(idiomaUsuario,"label.DniTercero")%></label>
                                        <input type="text" maxlength="10" size="20" id="dniTercero" name="dniTercero" value="" class="inputTexto" />
                                    </div>
                                    <!-- boton de buscar  -->
                                    <div class="botonera" >
                                          <input type="button" id="btnBuscar" name="btnBuscar" class="botonGeneral" value="<%=meLanbide43I18n.getMensaje(idiomaUsuario, "btn.buscar")%>" 
                                                 onclick="cargarTablaLLamadasMisGestiones(true);">
                                    </div>
                            </fieldset>   
                        </div>
                    </div>                   
                    <div style="clear: both;">
                        <div>
                            <!-- tabla de Llamadas Mis Gestiones --->
                            <div 
                                id="divTablaIntegMisGest" style="padding: 10px; width:100%; height: 470px; text-align: center">
                            </div>
                            <!-- botones debajo de la tabla --->
                           <!-- <div style="margin-top: 20px" class="botonera" >
                                  <input type="button" id="btnReenviar" name="btnReenviar" class="botonGeneral" value="<%=meLanbide43I18n.getMensaje(idiomaUsuario, "btn.reenviar")%>" 
                                         onclick="pulsarReenviarLLamadasMisGestiones(true);">
                            </div>-->
                           <div class="botonera" >
                                <!-- Boton Excel --->
                                <br>
                                <input type="button" id="btnExportarExcel" name="btnExportarExcel" class="botonLargo" value="<%=meLanbide43I18n.getMensaje(idiomaUsuario, "btn.excel")%>" onclick="pulsarImprimir();"> 
                            </div>
                        </div>
                    </div>
                </div>
            </form>
        </div>

        <script type="text/javascript">
            pleaseWait("off");
            var tabIntegMisGest;
            var listaIntegMisGest = new Array();
            var listaIntegMisGestTabla = new Array();
            var listaIntegMisGestTabla_titulos = new Array();
            var listaIntegMisGestTabla_estilos = new Array();
            
            tabIntegMisGest = new FixedColumnTable(document.getElementById('divTablaIntegMisGest'), 1130, 1130, 'notificaciones');  
            tabIntegMisGest.addColumna('60','center',"<%= meLanbide43I18n.getMensaje(idiomaUsuario,"label.IntegmisGest.tabla.col1")%>", "<%= meLanbide43I18n.getMensaje(idiomaUsuario,"label.IntegmisGest.tabla.col1.title")%>", 'Number');    
            tabIntegMisGest.addColumna('150','center',"<%= meLanbide43I18n.getMensaje(idiomaUsuario,"label.IntegmisGest.tabla.col2")%>", "<%= meLanbide43I18n.getMensaje(idiomaUsuario,"label.IntegmisGest.tabla.col2.title")%>");
            tabIntegMisGest.addColumna('100','center',"<%= meLanbide43I18n.getMensaje(idiomaUsuario,"label.IntegmisGest.tabla.col3")%>", "<%= meLanbide43I18n.getMensaje(idiomaUsuario,"label.IntegmisGest.tabla.col3.title")%>");       
            tabIntegMisGest.addColumna('100','center',"<%= meLanbide43I18n.getMensaje(idiomaUsuario,"label.IntegmisGest.tabla.col4")%>", "<%= meLanbide43I18n.getMensaje(idiomaUsuario,"label.IntegmisGest.tabla.col4.title")%>");    
            tabIntegMisGest.addColumna('100','center',"<%= meLanbide43I18n.getMensaje(idiomaUsuario,"label.IntegmisGest.tabla.col5")%>", "<%= meLanbide43I18n.getMensaje(idiomaUsuario,"label.IntegmisGest.tabla.col5.title")%>");  
            tabIntegMisGest.addColumna('60','left',"<%= meLanbide43I18n.getMensaje(idiomaUsuario,"label.IntegmisGest.tabla.col6")%>", "<%= meLanbide43I18n.getMensaje(idiomaUsuario,"label.IntegmisGest.tabla.col6.title")%>");    
            tabIntegMisGest.addColumna('60','center',"<%= meLanbide43I18n.getMensaje(idiomaUsuario,"label.IntegmisGest.tabla.col7")%>", "<%= meLanbide43I18n.getMensaje(idiomaUsuario,"label.IntegmisGest.tabla.col7.title")%>");    
            tabIntegMisGest.addColumna('100','center',"<%= meLanbide43I18n.getMensaje(idiomaUsuario,"label.IntegmisGest.tabla.col8")%>", "<%= meLanbide43I18n.getMensaje(idiomaUsuario,"label.IntegmisGest.tabla.col8.title")%>");    
            tabIntegMisGest.addColumna('100','center',"<%= meLanbide43I18n.getMensaje(idiomaUsuario,"label.IntegmisGest.tabla.col9")%>", "<%= meLanbide43I18n.getMensaje(idiomaUsuario,"label.IntegmisGest.tabla.col9.title")%>");    
            tabIntegMisGest.addColumna('150','left',"<%= meLanbide43I18n.getMensaje(idiomaUsuario,"label.IntegmisGest.tabla.col10")%>", "<%= meLanbide43I18n.getMensaje(idiomaUsuario,"label.IntegmisGest.tabla.col10.title")%>", 'String');  
            tabIntegMisGest.addColumna('150','left',"<%= meLanbide43I18n.getMensaje(idiomaUsuario,"label.IntegmisGest.tabla.col11")%>", "<%= meLanbide43I18n.getMensaje(idiomaUsuario,"label.IntegmisGest.tabla.col11.title")%>");      
            tabIntegMisGest.addColumna('150','left',"<%= meLanbide43I18n.getMensaje(idiomaUsuario,"label.IntegmisGest.tabla.col12")%>", "<%= meLanbide43I18n.getMensaje(idiomaUsuario,"label.IntegmisGest.tabla.col12.title")%>");    
                
            tabIntegMisGest.addColumna('150','center',"<%= meLanbide43I18n.getMensaje(idiomaUsuario,"label.IntegmisGest.tabla.col13")%>", "<%= meLanbide43I18n.getMensaje(idiomaUsuario,"label.IntegmisGest.tabla.col13.title")%>");
            tabIntegMisGest.addColumna('60','center',"<%= meLanbide43I18n.getMensaje(idiomaUsuario,"label.IntegmisGest.tabla.col14")%>", "<%= meLanbide43I18n.getMensaje(idiomaUsuario,"label.IntegmisGest.tabla.col14.title")%>");                
            
            tabIntegMisGest.height = '470';
            tabIntegMisGest.altoCabecera = 50;
            tabIntegMisGest.scrollWidth = 2600;
            tabIntegMisGest.dblClkFunction = 'dblClckTablaIntegMisGest';
            tabIntegMisGest.displayTabla();
            tabIntegMisGest.pack();
            
            function getXMLHttpRequest() {
                var aVersions = ["MSXML2.XMLHttp.5.0",
                    "MSXML2.XMLHttp.4.0", "MSXML2.XMLHttp.3.0",
                    "MSXML2.XMLHttp", "Microsoft.XMLHttp"];

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
            
                   
            function mostrarCalFecDesde(evento) {
            if(window.event) 
                evento = window.event;
            if (document.getElementById("calFecDesde").src.indexOf("icono.gif") != -1 )
                showCalendar('forms[0]','fecDesde',null,null,null,'','calFecDesde','',null,null,null,null,null,null,null,null,evento);
            }
            
            function mostrarCalFecHasta(evento) {
            if(window.event) 
                evento = window.event;
            if (document.getElementById("calFecHasta").src.indexOf("icono.gif") != -1 )
                showCalendar('forms[0]','fecHasta',null,null,null,'','calFecHasta','',null,null,null,null,null,null,null,null,evento);
            }
                
            // funcion que carga la tabla de las llamadas existentes para parametros seleccionados
           function cargarTablaLLamadasMisGestiones(){                                  
                //var codProcSelec = document.getElementById("codListaProcedimiento").value;
                var numExped= (document.getElementById("numExped").value).toUpperCase();
                var fecDesde= document.getElementById("fecDesde").value;
                var fecHasta= document.getElementById("fecHasta").value;
                var dniTercero = document.getElementById("dniTercero").value;
                var codProcedimiento = $('#listaProcedimiento').val();
                
                var ajax = getXMLHttpRequest();
                var nodos = null;
                var CONTEXT_PATH = '<%=request.getContextPath()%>'
                var url = CONTEXT_PATH + "/PeticionModuloIntegracion.do";
                var parametros = "";
                var control = new Date();

                parametros = "tarea=preparar&modulo=MELANBIDE43&operacion=cargarTablaLLamacargarTablaLdasMisGestiones&tipo=0&numExped=" + numExped
                             + "&fecDesde=" + fecDesde
                             + "&fecHasta=" + fecHasta
                             + "&dniTercero=" + dniTercero
                             + "&codProcedimiento=" + codProcedimiento
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
                        }   //if(navigator.appName.indexOf("Internet Explorer")!=-1)
                    }    //if (ajax.readyState==4 && ajax.status==200)
                    nodos = xmlDoc.getElementsByTagName("RESPUESTA");
                    listaIntegMisGest=convertirXML(nodos);
                    recargarTablaLlamadas(listaIntegMisGest);
                    } catch(Err){
                    jsp_alerta("A",'<%=meLanbide43I18n.getMensaje(idiomaUsuario,"error.errorProcesarRespuesta")%>')
                    } //try-catch               
            }

            function convertirXML(nodos){
                var codigoOperacion = nodos[0].childNodes[0].childNodes[0].nodeValue; 
                var listaIntegMisGest = new Array();
                
                if (codigoOperacion == 0){
                    var fila = new Array();
                    var elemento = nodos[0];
                    var hijos = elemento.childNodes;
                    var codigoOperacion = null;
                    var nodoFila;
                    var hijosFila;
                    var nodoCampo;     

                    for (j = 0; hijos != null && j < hijos.length; j++) {

                        if (hijos[j].nodeName == "FILA") {
                            nodoFila = hijos[j];
                            hijosFila = nodoFila.childNodes;
                            for (var cont = 0; cont < hijosFila.length; cont++) {
                                if (hijosFila[cont].nodeName == "ID") {
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
                                else if (hijosFila[cont].nodeName == "NUM_EXPED") {
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
                                else if (hijosFila[cont].nodeName == "TER_TID") {
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
                                else if (hijosFila[cont].nodeName == "TER_DOC") {
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
                                else if (hijosFila[cont].nodeName == "EXP_TIPO") {
                                   if (hijosFila[cont].childNodes.length > 0) {
                                       nodoCampo = hijosFila[cont];
                                       if(nodoCampo.childNodes.length > 0){
                                           fila[4] = nodoCampo.childNodes[0].nodeValue;
                                       }
                                       else{
                                           fila[4] = '-';
                                       }
                                   }
                                }
                                else if (hijosFila[cont].nodeName == "TIPO_OPERACION") {
                                   if (hijosFila[cont].childNodes.length > 0) {
                                       nodoCampo = hijosFila[cont];
                                       if(nodoCampo.childNodes.length > 0){
                                           fila[5] = nodoCampo.childNodes[0].nodeValue;
                                       }
                                       else{
                                           fila[5] = '-';
                                       }
                                   }
                                }
                                else if (hijosFila[cont].nodeName == "COD_TRAMITE_INICIO") {
                                   if (hijosFila[cont].childNodes.length > 0) {
                                       nodoCampo = hijosFila[cont];
                                       if(nodoCampo.childNodes.length > 0){
                                           fila[6] = nodoCampo.childNodes[0].nodeValue;
                                       }
                                       else{
                                           fila[6] = '-';
                                       }
                                   }
                                }
                                else if (hijosFila[cont].nodeName == "FECHA_GENERADO") {
                                   if (hijosFila[cont].childNodes.length > 0) {
                                       nodoCampo = hijosFila[cont];
                                       if(nodoCampo.childNodes.length > 0){
                                           fila[7] = nodoCampo.childNodes[0].nodeValue;
                                       }
                                       else{
                                           fila[7] = '-';
                                       }
                                   }
                                }
                                else if (hijosFila[cont].nodeName == "FECHA_PROCESADO") {
                                   if (hijosFila[cont].childNodes.length > 0) {
                                       nodoCampo = hijosFila[cont];
                                       if(nodoCampo.childNodes.length > 0){
                                           fila[8] = nodoCampo.childNodes[0].nodeValue;
                                       }
                                       else{
                                           fila[8] = '-';
                                       }
                                   }
                                }
                                
                                else if (hijosFila[cont].nodeName == "RES_EJE") {
                                   if (hijosFila[cont].childNodes.length > 0) {
                                       nodoCampo = hijosFila[cont];
                                       if(nodoCampo.childNodes.length > 0){
                                           fila[9] = nodoCampo.childNodes[0].nodeValue;
                                       }
                                       else{
                                           fila[9] = '-';
                                       }
                                   }
                                }
                                else if (hijosFila[cont].nodeName == "RES_NUM") {
                                   if (hijosFila[cont].childNodes.length > 0) {
                                       nodoCampo = hijosFila[cont];
                                       if(nodoCampo.childNodes.length > 0){
                                           fila[10] = nodoCampo.childNodes[0].nodeValue;
                                       }
                                       else{
                                           fila[10] = '-';
                                       }
                                   }
                                }
                                
                                else if (hijosFila[cont].nodeName == "REG_TELEMATICO") {
                                   if (hijosFila[cont].childNodes.length > 0) {
                                       nodoCampo = hijosFila[cont];
                                       if(nodoCampo.childNodes.length > 0){
                                           fila[11] = nodoCampo.childNodes[0].nodeValue;
                                       }
                                       else{
                                           fila[11] = '-';
                                       }
                                   }
                                }
                                else if (hijosFila[cont].nodeName == "FECHA_TELEMATICO") {
                                   if (hijosFila[cont].childNodes.length > 0) {
                                       nodoCampo = hijosFila[cont];
                                       if(nodoCampo.childNodes.length > 0){
                                           fila[12] = nodoCampo.childNodes[0].nodeValue;
                                       }
                                       else{
                                           fila[12] = '-';
                                       }
                                   }
                                }
                                else if (hijosFila[cont].nodeName == "NUM_INTENT") {
                                   if (hijosFila[cont].childNodes.length > 0) {
                                       nodoCampo = hijosFila[cont];
                                       if(nodoCampo.childNodes.length > 0){
                                           fila[13] = nodoCampo.childNodes[0].nodeValue;
                                       }
                                       else{
                                           fila[13] = '-';
                                       }
                                   }
                                }
                            }
                            listaIntegMisGest[j] = fila;
                            fila = new Array();  
                        }
                    }
                }
                else{
                    jsp_alerta("A",'<%=meLanbide43I18n.getMensaje(idiomaUsuario,"msg.noDatosConsultaLlamadas")%>')                            
                }
                return listaIntegMisGest;
            }  
            
            function recargarTablaLlamadas(result){
                var fila;
                listaIntegMisGest = new Array();
                listaIntegMisGestTabla = new Array();
                listaIntegMisGestTabla_titulos = new Array();
                listaIntegMisGestTabla_estilos = new Array();

                for(var i = 1;i< result.length; i++){
                    fila = result[i];
  
                    listaIntegMisGest[i-1] = [fila[0],fila[1],fila[2],fila[3],fila[4],fila[5],fila[6],fila[7],fila[8],fila[9],
                                                fila[10],fila[11],fila[12],fila[13]];
                    listaIntegMisGestTabla[i-1] =  [fila[0],fila[1],fila[2],fila[3],fila[4],fila[5],fila[6],fila[7],fila[8],fila[9],
                                                fila[10],fila[11],fila[12],fila[13]];
                    listaIntegMisGestTabla_titulos[i-1] =  [fila[0],fila[1],fila[2],fila[3],fila[4],fila[5],fila[6],fila[7],fila[8],fila[9],
                                                fila[10],fila[11],fila[12],fila[13]];
                }
                
            tabIntegMisGest = new FixedColumnTable(document.getElementById('divTablaIntegMisGest'), 1130, 1130, 'notificaciones');  
            tabIntegMisGest.addColumna('60','center',"<%= meLanbide43I18n.getMensaje(idiomaUsuario,"label.IntegmisGest.tabla.col1")%>", "<%= meLanbide43I18n.getMensaje(idiomaUsuario,"label.IntegmisGest.tabla.col1.title")%>", 'Number');    
            tabIntegMisGest.addColumna('150','center',"<%= meLanbide43I18n.getMensaje(idiomaUsuario,"label.IntegmisGest.tabla.col2")%>", "<%= meLanbide43I18n.getMensaje(idiomaUsuario,"label.IntegmisGest.tabla.col2.title")%>");
            tabIntegMisGest.addColumna('100','center',"<%= meLanbide43I18n.getMensaje(idiomaUsuario,"label.IntegmisGest.tabla.col3")%>", "<%= meLanbide43I18n.getMensaje(idiomaUsuario,"label.IntegmisGest.tabla.col3.title")%>");       
            tabIntegMisGest.addColumna('100','center',"<%= meLanbide43I18n.getMensaje(idiomaUsuario,"label.IntegmisGest.tabla.col4")%>", "<%= meLanbide43I18n.getMensaje(idiomaUsuario,"label.IntegmisGest.tabla.col4.title")%>");    
            tabIntegMisGest.addColumna('100','center',"<%= meLanbide43I18n.getMensaje(idiomaUsuario,"label.IntegmisGest.tabla.col5")%>", "<%= meLanbide43I18n.getMensaje(idiomaUsuario,"label.IntegmisGest.tabla.col5.title")%>");  
            tabIntegMisGest.addColumna('60','left',"<%= meLanbide43I18n.getMensaje(idiomaUsuario,"label.IntegmisGest.tabla.col6")%>", "<%= meLanbide43I18n.getMensaje(idiomaUsuario,"label.IntegmisGest.tabla.col6.title")%>");    
            tabIntegMisGest.addColumna('60','center',"<%= meLanbide43I18n.getMensaje(idiomaUsuario,"label.IntegmisGest.tabla.col7")%>", "<%= meLanbide43I18n.getMensaje(idiomaUsuario,"label.IntegmisGest.tabla.col7.title")%>");    
            tabIntegMisGest.addColumna('100','center',"<%= meLanbide43I18n.getMensaje(idiomaUsuario,"label.IntegmisGest.tabla.col8")%>", "<%= meLanbide43I18n.getMensaje(idiomaUsuario,"label.IntegmisGest.tabla.col8.title")%>");    
            tabIntegMisGest.addColumna('100','center',"<%= meLanbide43I18n.getMensaje(idiomaUsuario,"label.IntegmisGest.tabla.col9")%>", "<%= meLanbide43I18n.getMensaje(idiomaUsuario,"label.IntegmisGest.tabla.col9.title")%>");    
            tabIntegMisGest.addColumna('150','left',"<%= meLanbide43I18n.getMensaje(idiomaUsuario,"label.IntegmisGest.tabla.col10")%>", "<%= meLanbide43I18n.getMensaje(idiomaUsuario,"label.IntegmisGest.tabla.col10.title")%>", 'String');  
            tabIntegMisGest.addColumna('150','left',"<%= meLanbide43I18n.getMensaje(idiomaUsuario,"label.IntegmisGest.tabla.col11")%>", "<%= meLanbide43I18n.getMensaje(idiomaUsuario,"label.IntegmisGest.tabla.col11.title")%>");      
            tabIntegMisGest.addColumna('150','left',"<%= meLanbide43I18n.getMensaje(idiomaUsuario,"label.IntegmisGest.tabla.col12")%>", "<%= meLanbide43I18n.getMensaje(idiomaUsuario,"label.IntegmisGest.tabla.col12.title")%>");    
                
            tabIntegMisGest.addColumna('150','center',"<%= meLanbide43I18n.getMensaje(idiomaUsuario,"label.IntegmisGest.tabla.col13")%>", "<%= meLanbide43I18n.getMensaje(idiomaUsuario,"label.IntegmisGest.tabla.col13.title")%>");
            tabIntegMisGest.addColumna('60','center',"<%= meLanbide43I18n.getMensaje(idiomaUsuario,"label.IntegmisGest.tabla.col14")%>", "<%= meLanbide43I18n.getMensaje(idiomaUsuario,"label.IntegmisGest.tabla.col14.title")%>");                
            tabIntegMisGest.numColumnasFijas = 0;
            
            tabIntegMisGest.displayCabecera=true;

                for(var cont = 0; cont < listaIntegMisGestTabla.length; cont++){
                    tabIntegMisGest.addFilaConFormato(listaIntegMisGestTabla[cont], listaIntegMisGestTabla_titulos[cont], listaIntegMisGestTabla_estilos[cont])
                }

                tabIntegMisGest.height = '470';
                tabIntegMisGest.altoCabecera = 50;
                tabIntegMisGest.scrollWidth = 2600;
                tabIntegMisGest.dblClkFunction = 'dblClckTablaIntegMisGest';
                tabIntegMisGest.displayTabla();
                tabIntegMisGest.pack();
            }
            
            function dblClckTablaIntegMisGest(rowID,tableName){
                //pulsarReenviarLLamadasMisGestiones();
            }
            
            function pulsarReenviarLLamadasMisGestiones(){
                var fila;

                if(tabIntegMisGest.selectedIndex != -1) {
                    fila = tabIntegMisGest.selectedIndex;
 //                   if (tabIntegMisGest.lineas[fila][12]!=null && tabIntegMisGest.lineas[fila][12]!=''){
                    if (tabIntegMisGest.lineas[fila][10] != "-"){
                        jsp_alerta("A",'<%=meLanbide43I18n.getMensaje(idiomaUsuario,"msg.notificacionCodigoPlatea")%>');
                    }else{ 
                        if (tabIntegMisGest.lineas[fila][14]=="null"){
                            jsp_alerta("A",'<%=meLanbide43I18n.getMensaje(idiomaUsuario,"msg.notificacionYaMarcada")%>');
                        }
                        else{
                            reenvioLlamada(fila);
                        }
                    }
                }else{
                        jsp_alerta('A', '<%=meLanbide43I18n.getMensaje(idiomaUsuario, "msg.msjNoSelecFila")%>');
                    }    
            }
            
            function reenvioLlamada(fila){
                //var codProc = document.getElementById("codListaProcedimiento").value;
                var numExped= (document.getElementById("numExped").value).toUpperCase();
                var fecDesde= document.getElementById("fecDesde").value;
                var fecHasta= document.getElementById("fecHasta").value;
                var dniTercero = document.getElementById("dniTercero").value;
                var codProcedimiento = $('#listaProcedimiento').val();

                var ajax = getXMLHttpRequest();
                var nodos = null;
                var CONTEXT_PATH = '<%=request.getContextPath()%>'
                var url = CONTEXT_PATH + "/PeticionModuloIntegracion.do";
                var parametros = "";
                var control = new Date();

                parametros = "tarea=preparar&modulo=MELANBIDE43&operacion=reenviarLlamadasMisGest&tipo=0&id=" + tabIntegMisGest.lineas[fila][0]
                             + "&numExped=" + numExped
                             + "&fecDesde=" + fecDesde
                             + "&fecHasta=" + fecHasta
                             + "&dniTercero=" + dniTercero
                             + "&codProcedimiento=" + codProcedimiento
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
                        }   //if(navigator.appName.indexOf("Internet Explorer")!=-1)
                    }    //if (ajax.readyState==4 && ajax.status==200)

                    nodos = xmlDoc.getElementsByTagName("RESPUESTA");
                    listaIntegMisGest=convertirXML(nodos);                            
                    recargarTablaLlamadas(listaIntegMisGest);
                    // vuelvo a colocarme al principio del XML para recuperar el c�digo de operacion
                    nodos = xmlDoc.getElementsByTagName("RESPUESTA"); 
                    var codigoOperacion = nodos[0].childNodes[0].childNodes[0].nodeValue; 
                        if(codigoOperacion=="0"){
                            jsp_alerta("A",'<%=meLanbide43I18n.getMensaje(idiomaUsuario,"msg.notificacionReenviadaOK")%>');
                        }else if(codigoOperacion=="1"){
                            jsp_alerta("A",'<%=meLanbide43I18n.getMensaje(idiomaUsuario,"error.errorReenviarBD")%>');
                        }else if(codigoOperacion=="2"){
                            jsp_alerta("A",'<%=meLanbide43I18n.getMensaje(idiomaUsuario,"error.errorReenviarGen")%>');
                        }else if(codigoOperacion=="3"){
                            jsp_alerta("A",'<%=meLanbide43I18n.getMensaje(idiomaUsuario,"error.pasoParametrosReenviar")%>');
                        }else{
                            jsp_alerta("A",'<%=meLanbide43I18n.getMensaje(idiomaUsuario,"error.errorReenviarGen")%>');
                        }//if(
                    }
                catch(Err){
                    jsp_alerta("A",'<%=meLanbide43I18n.getMensaje(idiomaUsuario,"error.errorProcesarRespuesta")%>');
                }//try-catch
            }
            
            function pulsarImprimir() {
                if( tabIntegMisGest!=null && tabIntegMisGest!=undefined 
                        && tabIntegMisGest.lineas!=null && tabIntegMisGest.lineas!=undefined  
                        && tabIntegMisGest.lineas.length>0){
                    pleaseWait("on");
                    var dataParameter={
                        tarea:'preparar'
                        ,modulo:'MELANBIDE43'
                        ,tipo:0
                        ,operacion : 'getExcelLlamadas'
                        ,numero : $("#numeroExpediente").val()
                        ,control : new Date().getTime()
                        ,numExped:($("#numExped")!=null && $("#numExped")!=undefined  && $("#numExped").val()!=null && $("#numExped").val()!=undefined ? $("#numExped").val().toUpperCase():"")
                        ,fecDesde:$("#fecDesde").val()
                        ,fecHasta:$("#fecHasta").val()
                        ,dniTercero:($("#dniTercero")!=null && $("#dniTercero")!=undefined  && $("#dniTercero").val()!=null && $("#dniTercero").val()!=undefined ? $("#dniTercero").val().toUpperCase():"")
                        ,codProcedimiento:$('#listaProcedimiento').val()
                    };
                    var urlBaseLlamada = APP_CONTEXT_PATH + "/PeticionModuloIntegracion.do?"+$.param(dataParameter);
                    window.location.href = urlBaseLlamada;
                    pleaseWait("off");
                    /*
                    $.ajax({
                        type: 'POST',
                        url: urlBaseLlamada,
                        data: dataParameter,
                        xhrFields: {
                            responseType: 'blob'
                        },
                        success: function (respuesta) {
                            pleaseWait("off");
                            if(respuesta!=null && respuesta!=""){
                                var url = window.URL.createObjectURL(respuesta);
                                window.location.assign(url);
                                jsp_alerta("A", "Fichero Excel Generado Correctamente.");
                            }else{
                                jsp_alerta("A", "Fichero Excel Generado No generado Correctamente.");
                            }
                            
                        },
                        //dataType: dataType,
                        error: function (jqXHR, textStatus, errorThrown) {
                            pleaseWait("off");
                            var mensajeError = 'Se ha presentado un error al guardar los datos. Expediente no actualizado. ' + jqXHR + ' - ' + textStatus + '  ' + errorThrown;
                            jsp_alerta("A", "No se ha podido generar el Excel. Se presentado un error. " +  mensajeError);

                        },
                        async: false
                    });
                    */
                    /*
                    var CONTEXT_PATH = '< %=request.getContextPath()%>';
                    var url = CONTEXT_PATH + "/PeticionModuloIntegracion.do";
                    var parametros = 'tarea=preparar&modulo=MELANBIDE43&operacion=getExcelLlamadas&tipo=0';
                    url = url + "?" + parametros 
                    window.location.href = url;
                    */
                }else{
                    jsp_alerta("A", "No hay datos para exportar. Selecciona los criterios y Ejecuta la Busqueda.. ");
                }
            }
        </script>
              
    </body>
</html>


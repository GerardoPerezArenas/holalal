<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>

<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide43.i18n.MeLanbide43I18n" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide43.vo.ProcedimientoVO" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide43.vo.FilaNotificacionVO" %>
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
        <link rel="StyleSheet" media="screen" type="text/css" href="<%=request.getContextPath()%><%=css%>">
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script>
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/>        
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/estilo.css"/>
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/extension/melanbide43/melanbide43.css"/>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/melanbide43/FixedColumnsTable.js"></script>        

        <script type="text/javascript">var APP_CONTEXT_PATH = '<%=request.getContextPath()%>'</script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/general.js"></script>
        <script type="text/javascript" src="<c:url value='/scripts/listaComboBox.js'/>"></script>
        <script type="text/javascript" src="<c:url value='/scripts/calendario.js'/>"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/validaciones.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/popup.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/meLanbide43/JavaScriptUtil.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/meLanbide43/Parsers.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/meLanbide43/InputMask.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/meLanbide43/lanbide.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/melanbide43/ecaUtils.js"></script>
        <!-- Eventos onKeyPress compatibilidad firefox  -->
        <!--script type="text/javascript" src="http://code.jquery.com/jquery-1.4.4.js"></script-->

    </head>
    <body class="bandaBody">
        <div class="contenidoPantalla" style="clear: both;">
            <form>
                <div style="width: 100%; text-align: left;">
                    <div>
                        <h2 class="sub3titulo" id="pestana331"><%=meLanbide43I18n.getMensaje(idiomaUsuario,"label.GestionNotificaciones")%></h2>
                        <div style="margin: 10px;"> 
                            <fieldset id="fieldsetDatosNotificacion" name="fieldsetDatosNotificacion" style="width: 99%;">
                                <legend class="legendAzul"><%=meLanbide43I18n.getMensaje(idiomaUsuario,"legend.notif.datosNotif")%></legend>
                                <div style ="margin: 10px"
                                    <!-- etiqueta de procedimientos -->
                                    <div>
                                        <label class="etiqueta" style="text-align: center; position: relative;"><%=meLanbide43I18n.getMensaje(idiomaUsuario,"label.Procedimientos")%></label>
                                    </div>
                                    <!-- combo de procedimientos -->
                                    <div>
                                        <input type="text" name="codListaProcedimiento" id="codListaProcedimiento" size="10" class="inputTexto" value="" onkeyup="xAMayusculas(this);" />
                                        <input type="text" name="descListaProcedimiento" id="descListaProcedimiento" size="150" class="inputTexto" readonly="true" value="" />
                                        <a href="" id="anchorListaProcedimiento" name="anchorListaProcedimiento">
                                            <img src="<%=request.getContextPath()%>/images/listas/botondesplegable.gif" id="botonProcedimiento"
                                                 name="botonProcedimiento" height="14" width="14" border="0" style="cursor:hand;">
                                        </a>
                                    </div>
                                    <!-- numero de Expediente -->
                                    <div style="margin: 10px;">
                                        <label class="etiqueta" style="text-align: center; position: relative; margin: 5px; margin-right: 75px"><%=meLanbide43I18n.getMensaje(idiomaUsuario,"label.Expediente")%></label>
                                        <input type="text" maxlength="30" size="30" id="numExped" name="numExped" value="" class="inputTexto" />
                                    </div>
                                    <!-- fechas desde y hasta -->
                                    <div style="margin: 10px;">
                                        <label class="etiqueta" style="text-align: center; position: relative; margin: 5px; margin-right: 25px"><%=meLanbide43I18n.getMensaje(idiomaUsuario,"label.FechaEnvio")%></label>
                                        <label class="etiqueta" style="text-align: center; position: relative; margin: 5px; margin-right: 25px"><%=meLanbide43I18n.getMensaje(idiomaUsuario,"label.Desde")%></label>
                                        <!-- meter aquí el icono de calendario -->
                                        <input type="text" class="inputTxtFecha" size="10" maxlength="10" id="fecDesde" name="fecDesde" onkeyup="return SoloCaracteresFecha(this);" 
                                               onblur="javascript:return comprobarFechaEca(this, '<%=meLanbide43I18n.getMensaje(idiomaUsuario, "msg.fechaNoVal")%>');" onfocus="this.select();"/>
                                        <a href="javascript:calClick(event);" onclick="mostrarCalFecDesde(event);return false;" style="text-decoration:none;" >
                                            <img style="border: 0; height: 17" id="calFecDesde" name="calFecDesde" alt="<%=meLanbide43I18n.getMensaje(idiomaUsuario,"label.alt.fechaDesde")%>" 
                                                 src="<c:url value='/images/calendario/icono.gif'/>">   
                                         
                                        </a>
                                        <label class="etiqueta" style="text-align: center; position: relative; margin: 5px; margin-right: 25px"><%=meLanbide43I18n.getMensaje(idiomaUsuario,"label.Hasta")%></label>
                                        <!-- meter aquí el icono de calendario -->
                                        <input type="text" class="inputTxtFecha" size="10" maxlength="10" id="fecHasta" name="fecHasta" onkeyup="return SoloCaracteresFecha(this);" 
                                               onblur="javascript:return comprobarFechaEca(this, '<%=meLanbide43I18n.getMensaje(idiomaUsuario, "msg.fechaNoVal")%>');" onfocus="this.select();"/>
                                        <a href="javascript:calClick(event);" onclick="mostrarCalFecHasta(event);return false;" style="text-decoration:none;" >
                                            <img style="border: 0" height="17" id="calFecHasta" name="calFecHasta" alt="<%=meLanbide43I18n.getMensaje(idiomaUsuario,"label.alt.fechaHasta")%>" 
                                                 src="<c:url value='/images/calendario/icono.gif'/>">
                                        </a>
                                    </div>
                                    <!-- desplegable de resultado -->
                                     <div style="margin: 10px;">
                                        <label class="etiqueta" style="text-align: center; position: relative; margin-right: 30px;"><%=meLanbide43I18n.getMensaje(idiomaUsuario,"label.Resultado")%></label>
                                        <input type="text" name="codListaResultado" id="codListaResultado" size="3" class="inputTexto" readonly="true" hidden="true" value="" />
                                        <input type="text" name="descListaResultado" id="descListaResultado" size="15" class="inputTexto" readonly="true" value="" />
                                        <a href="" id="anchorListaResultado" name="anchorListaResultado">
                                            <img src="<%=request.getContextPath()%>/images/listas/botondesplegable.gif" id="botonResultado"
                                                 name="botonResultado" height="14" width="14" border="0" style="cursor:hand;">
                                        </a>
                                    </div>
                                </div>
                                    <!-- boton de buscar  -->
                                    <div class="botonera" >
                                          <input type="button" id="btnBuscar" name="btnBuscar" class="botonGeneral" value="<%=meLanbide43I18n.getMensaje(idiomaUsuario, "btn.buscar")%>" 
                                                 onclick="cargarTablaNotificaciones(true);">
                                    </div>
                                </div>
                         
                            </fieldset>   
                        </div>
                    </div>                   
                <div style="clear: both;">
                    <div>
                        <!-- tabla de Notificaciones --->
                        <div 
                            id="divTablaNotificaciones" style="padding: 10px; width:100%; height: 470px; text-align: center">
                        </div>
                        <!-- botones debajo de la tabla --->
                        <div style="margin-top: 20px" class="botonera" >
                              <input type="button" id="btnReenviar" name="btnReenviar" class="botonGeneral" value="<%=meLanbide43I18n.getMensaje(idiomaUsuario, "btn.reenviar")%>" 
                                     onclick="pulsarReenviarNotificacion(true);">
                        </div>
                    </div>
                </div>
            </div>

            </form>
        </div>

        <script type="text/javascript">
            
            var tabNotificaciones;
            var listaNotificaciones = new Array();
            var listaNotificacionesTabla = new Array();
            var listaNotificacionesTabla_titulos = new Array();
            var listaNotificacionesTabla_estilos = new Array();
            
            tabNotificaciones = new FixedColumnTable(document.getElementById('divTablaNotificaciones'), 1130, 1130, 'notificaciones');  
            tabNotificaciones.addColumna('60','center',"<%= meLanbide43I18n.getMensaje(idiomaUsuario,"label.notif.tabla.col1")%>", "<%= meLanbide43I18n.getMensaje(idiomaUsuario,"label.notif.tabla.col1.title")%>", 'Number');    
            tabNotificaciones.addColumna('150','center',"<%= meLanbide43I18n.getMensaje(idiomaUsuario,"label.notif.tabla.col2")%>", "<%= meLanbide43I18n.getMensaje(idiomaUsuario,"label.notif.tabla.col2.title")%>");    
            tabNotificaciones.addColumna('60','center',"<%= meLanbide43I18n.getMensaje(idiomaUsuario,"label.notif.tabla.col3")%>", "<%= meLanbide43I18n.getMensaje(idiomaUsuario,"label.notif.tabla.col3.title")%>");    
            tabNotificaciones.addColumna('60','center',"<%= meLanbide43I18n.getMensaje(idiomaUsuario,"label.notif.tabla.col4")%>", "<%= meLanbide43I18n.getMensaje(idiomaUsuario,"label.notif.tabla.col4.title")%>");    
            tabNotificaciones.addColumna('60','center',"<%= meLanbide43I18n.getMensaje(idiomaUsuario,"label.notif.tabla.col5")%>", "<%= meLanbide43I18n.getMensaje(idiomaUsuario,"label.notif.tabla.col5.title")%>");  
            tabNotificaciones.addColumna('200','left',"<%= meLanbide43I18n.getMensaje(idiomaUsuario,"label.notif.tabla.col6")%>", "<%= meLanbide43I18n.getMensaje(idiomaUsuario,"label.notif.tabla.col6.title")%>");    
            tabNotificaciones.addColumna('60','center',"<%= meLanbide43I18n.getMensaje(idiomaUsuario,"label.notif.tabla.col7")%>", "<%= meLanbide43I18n.getMensaje(idiomaUsuario,"label.notif.tabla.col7.title")%>");    
            tabNotificaciones.addColumna('260','left',"<%= meLanbide43I18n.getMensaje(idiomaUsuario,"label.notif.tabla.col8")%>", "<%= meLanbide43I18n.getMensaje(idiomaUsuario,"label.notif.tabla.col8.title")%>");    
            tabNotificaciones.addColumna('60','center',"<%= meLanbide43I18n.getMensaje(idiomaUsuario,"label.notif.tabla.col9")%>", "<%= meLanbide43I18n.getMensaje(idiomaUsuario,"label.notif.tabla.col9.title")%>");    
            tabNotificaciones.addColumna('260','left',"<%= meLanbide43I18n.getMensaje(idiomaUsuario,"label.notif.tabla.col10")%>", "<%= meLanbide43I18n.getMensaje(idiomaUsuario,"label.notif.tabla.col10.title")%>", 'String');  
            tabNotificaciones.addColumna('60','center',"<%= meLanbide43I18n.getMensaje(idiomaUsuario,"label.notif.tabla.col11")%>", "<%= meLanbide43I18n.getMensaje(idiomaUsuario,"label.notif.tabla.col11.title")%>");      
            tabNotificaciones.addColumna('100','center',"<%= meLanbide43I18n.getMensaje(idiomaUsuario,"label.notif.tabla.col12")%>", "<%= meLanbide43I18n.getMensaje(idiomaUsuario,"label.notif.tabla.col12.title")%>");    
            tabNotificaciones.addColumna('260','left',"<%= meLanbide43I18n.getMensaje(idiomaUsuario,"label.notif.tabla.col13")%>", "<%= meLanbide43I18n.getMensaje(idiomaUsuario,"label.notif.tabla.col13.title")%>");    
            tabNotificaciones.addColumna('100','center',"<%= meLanbide43I18n.getMensaje(idiomaUsuario,"label.notif.tabla.col14")%>", "<%= meLanbide43I18n.getMensaje(idiomaUsuario,"label.notif.tabla.col14.title")%>");  
            tabNotificaciones.addColumna('60','center',"<%= meLanbide43I18n.getMensaje(idiomaUsuario,"label.notif.tabla.col15")%>", "<%= meLanbide43I18n.getMensaje(idiomaUsuario,"label.notif.tabla.col15.title")%>");    
            tabNotificaciones.addColumna('260','left',"<%= meLanbide43I18n.getMensaje(idiomaUsuario,"label.notif.tabla.col16")%>", "<%= meLanbide43I18n.getMensaje(idiomaUsuario,"label.notif.tabla.col16.title")%>");    
            tabNotificaciones.addColumna('60','center',"<%= meLanbide43I18n.getMensaje(idiomaUsuario,"label.notif.tabla.col17")%>", "<%= meLanbide43I18n.getMensaje(idiomaUsuario,"label.notif.tabla.col17.title")%>");    
            tabNotificaciones.addColumna('100','center',"<%= meLanbide43I18n.getMensaje(idiomaUsuario,"label.notif.tabla.col18")%>", "<%= meLanbide43I18n.getMensaje(idiomaUsuario,"label.notif.tabla.col18.title")%>");    

            
            tabNotificaciones.height = '470';
            tabNotificaciones.altoCabecera = 50;
            tabNotificaciones.scrollWidth = 2600;
            tabNotificaciones.dblClkFunction = 'dblClckTablaNotificaciones';
            tabNotificaciones.displayTabla();
            tabNotificaciones.pack();
            
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
            
            var listaCodigosProcedimiento = new Array();
            var listaDescripcionesProcedimiento = new Array();

            listaCodigosProcedimiento[0] = "";
            listaDescripcionesProcedimiento[0] = "";
            
            contador = 0;

            <logic:iterate id="numProc" name="listaProcedimiento" scope="request">
            listaCodigosProcedimiento[contador] = ['<bean:write name="numProc" property="codProc" />'];
            listaDescripcionesProcedimiento[contador] = ['<bean:write name="numProc" property="descProc" />'];
            contador++;
            </logic:iterate>

            var comboListaProcedimiento = new Combo("ListaProcedimiento");
            comboListaProcedimiento.addItems(listaCodigosProcedimiento, listaDescripcionesProcedimiento);
            
            // Cargar el combo Resultado de la Notificacion        
            var listaCodigosResultado = new Array();
            var listaDescripcionesResultado = new Array();

            listaCodigosResultado[0] = "";
            listaDescripcionesResultado[0] = "";
            
            listaCodigosResultado[0] = '<%=ConstantesMeLanbide43.ACEPTADA%>';
            listaCodigosResultado[1] = '<%=ConstantesMeLanbide43.RECHAZADA%>';
            listaDescripcionesResultado[0]= "Aceptada";
            listaDescripcionesResultado[1]= "Rechazada";            
            
            var comboListaResultado = new Combo("ListaResultado");
            comboListaResultado.addItems(listaCodigosResultado, listaDescripcionesResultado);
            
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
                
            // funcion que carga la tabla de las notificaciones existentes para el procedimiento y resto parametros seleccionados
            function cargarTablaNotificaciones(){                                  
                var codProcSelec = document.getElementById("codListaProcedimiento").value;
                var numExped= (document.getElementById("numExped").value).toUpperCase();
                var fecDesde= document.getElementById("fecDesde").value;
                var fecHasta= document.getElementById("fecHasta").value;
                var codResultado = document.getElementById("codListaResultado").value;
                
                var ajax = getXMLHttpRequest();
                var nodos = null;
                var CONTEXT_PATH = '<%=request.getContextPath()%>'
                var url = CONTEXT_PATH + "/PeticionModuloIntegracion.do";
                var parametros = "";
                var control = new Date();

                parametros = "tarea=preparar&modulo=MELANBIDE43&operacion=cargarTablaNotificaciones&tipo=0&codProc=" + codProcSelec
                             + "&numExped=" + numExped
                             + "&fecDesde=" + fecDesde
                             + "&fecHasta=" + fecHasta
                             + "&resultado=" + codResultado
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
                    listaNotificaciones=convertirXML(nodos);
                    recargarTablaNotificaciones(listaNotificaciones);
                    } catch(Err){
                    jsp_alerta("A",'<%=meLanbide43I18n.getMensaje(idiomaUsuario,"error.errorProcesarRespuesta")%>')
                    } //try-catch               
            }

            function convertirXML(nodos){
                var codigoOperacion = nodos[0].childNodes[0].childNodes[0].nodeValue; 
                var listaNotificaciones = new Array();
                
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
                                if (hijosFila[cont].nodeName == "COD_NOTIF") {
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
                                else if (hijosFila[cont].nodeName == "COD_PROC") {
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
                                else if (hijosFila[cont].nodeName == "EJERC") {
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
                                else if (hijosFila[cont].nodeName == "COD_MUNIC") {
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
                                else if (hijosFila[cont].nodeName == "COD_TRAM") {
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
                                else if (hijosFila[cont].nodeName == "OCU_TRAM") {
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
                                else if (hijosFila[cont].nodeName == "ACT_NOTIF") {
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
                                else if (hijosFila[cont].nodeName == "CAD_NOTIF") {
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
                                else if (hijosFila[cont].nodeName == "TXT_NOTIF") {
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
                                else if (hijosFila[cont].nodeName == "FIRMADA") {
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
                                else if (hijosFila[cont].nodeName == "FEC_ENVIO") {
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
                                else if (hijosFila[cont].nodeName == "COD_NOTIF_PLATEA") {
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
                                else if (hijosFila[cont].nodeName == "FEC_SOL_ENVIO") {
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
                                else if (hijosFila[cont].nodeName == "NUM_INTENT") {
                                   if (hijosFila[cont].childNodes.length > 0) {
                                       nodoCampo = hijosFila[cont];
                                       if(nodoCampo.childNodes.length > 0){
                                           fila[14] = nodoCampo.childNodes[0].nodeValue;
                                       }
                                       else{
                                           fila[14] = '-';
                                       }
                                   }
                                }
                                else if (hijosFila[cont].nodeName == "RESP_LLAMADA") {
                                   if (hijosFila[cont].childNodes.length > 0) {
                                       nodoCampo = hijosFila[cont];
                                       if(nodoCampo.childNodes.length > 0){
                                           fila[15] = nodoCampo.childNodes[0].nodeValue;
                                       }
                                       else{
                                           fila[15] = '-';
                                       }
                                   }
                                }
                                else if (hijosFila[cont].nodeName == "RESULTADO") {
                                   if (hijosFila[cont].childNodes.length > 0) {
                                       nodoCampo = hijosFila[cont];
                                       if(nodoCampo.childNodes.length > 0){
                                           fila[16] = nodoCampo.childNodes[0].nodeValue;
                                       }
                                       else{
                                           fila[16] = '-';
                                       }
                                   }
                                }
                                else if (hijosFila[cont].nodeName == "FEC_ACUSE") {
                                   if (hijosFila[cont].childNodes.length > 0) {
                                       nodoCampo = hijosFila[cont];
                                       if(nodoCampo.childNodes.length > 0){
                                           fila[17] = nodoCampo.childNodes[0].nodeValue;
                                       }
                                       else{
                                           fila[17] = '-';
                                       }
                                   }
                                }
                            }
                            listaNotificaciones[j] = fila;
                            fila = new Array();  
                        }
                    }
                }
                else{
                    jsp_alerta("A",'<%=meLanbide43I18n.getMensaje(idiomaUsuario,"msg.noDatosConsulta")%>')                            
                }
                return listaNotificaciones;
            }  
            
            function recargarTablaNotificaciones(result){
                var fila;
                listaNotificaciones = new Array();
                listaNotificacionesTabla = new Array();
                listaNotificacionesTabla_titulos = new Array();
                listaNotificacionesTabla_estilos = new Array();

                for(var i = 1;i< result.length; i++){
                    fila = result[i];
  
                    listaNotificaciones[i-1] = [fila[0],fila[1],fila[2],fila[3],fila[4],fila[5],fila[6],fila[7],fila[8],fila[9],
                                                fila[10],fila[11],fila[12],fila[13],fila[14],fila[15],fila[16],fila[17]];
                    listaNotificacionesTabla[i-1] =  [fila[0],fila[1],fila[2],fila[3],fila[4],fila[5],fila[6],fila[7],fila[8],fila[9],
                                                fila[10],fila[11],fila[12],fila[13],fila[14],fila[15],fila[16],fila[17]];
                    listaNotificacionesTabla_titulos[i-1] =  [fila[0],fila[1],fila[2],fila[3],fila[4],fila[5],fila[6],fila[7],fila[8],fila[9],
                                                fila[10],fila[11],fila[12],fila[13],fila[14],fila[15],fila[16],fila[17]];
                }
                
                tabNotificaciones = new FixedColumnTable(document.getElementById('divTablaNotificaciones'), 1130, 1130, 'notificaciones');  
                tabNotificaciones.addColumna('60','center',"<%= meLanbide43I18n.getMensaje(idiomaUsuario,"label.notif.tabla.col1")%>", "<%= meLanbide43I18n.getMensaje(idiomaUsuario,"label.notif.tabla.col1.title")%>", 'Number');    
                tabNotificaciones.addColumna('150','center',"<%= meLanbide43I18n.getMensaje(idiomaUsuario,"label.notif.tabla.col2")%>", "<%= meLanbide43I18n.getMensaje(idiomaUsuario,"label.notif.tabla.col2.title")%>");    
                tabNotificaciones.addColumna('60','center',"<%= meLanbide43I18n.getMensaje(idiomaUsuario,"label.notif.tabla.col3")%>", "<%= meLanbide43I18n.getMensaje(idiomaUsuario,"label.notif.tabla.col3.title")%>");    
                tabNotificaciones.addColumna('60','center',"<%= meLanbide43I18n.getMensaje(idiomaUsuario,"label.notif.tabla.col4")%>", "<%= meLanbide43I18n.getMensaje(idiomaUsuario,"label.notif.tabla.col4.title")%>");    
                tabNotificaciones.addColumna('60','center',"<%= meLanbide43I18n.getMensaje(idiomaUsuario,"label.notif.tabla.col5")%>", "<%= meLanbide43I18n.getMensaje(idiomaUsuario,"label.notif.tabla.col5.title")%>");  
                tabNotificaciones.addColumna('200','left',"<%= meLanbide43I18n.getMensaje(idiomaUsuario,"label.notif.tabla.col6")%>", "<%= meLanbide43I18n.getMensaje(idiomaUsuario,"label.notif.tabla.col6.title")%>");    
                tabNotificaciones.addColumna('60','center',"<%= meLanbide43I18n.getMensaje(idiomaUsuario,"label.notif.tabla.col7")%>", "<%= meLanbide43I18n.getMensaje(idiomaUsuario,"label.notif.tabla.col7.title")%>");    
                tabNotificaciones.addColumna('260','left',"<%= meLanbide43I18n.getMensaje(idiomaUsuario,"label.notif.tabla.col8")%>", "<%= meLanbide43I18n.getMensaje(idiomaUsuario,"label.notif.tabla.col8.title")%>");    
                tabNotificaciones.addColumna('60','center',"<%= meLanbide43I18n.getMensaje(idiomaUsuario,"label.notif.tabla.col9")%>", "<%= meLanbide43I18n.getMensaje(idiomaUsuario,"label.notif.tabla.col9.title")%>");    
                tabNotificaciones.addColumna('260','left',"<%= meLanbide43I18n.getMensaje(idiomaUsuario,"label.notif.tabla.col10")%>", "<%= meLanbide43I18n.getMensaje(idiomaUsuario,"label.notif.tabla.col10.title")%>", 'String');  
                tabNotificaciones.addColumna('60','center',"<%= meLanbide43I18n.getMensaje(idiomaUsuario,"label.notif.tabla.col11")%>", "<%= meLanbide43I18n.getMensaje(idiomaUsuario,"label.notif.tabla.col11.title")%>");      
                tabNotificaciones.addColumna('100','center',"<%= meLanbide43I18n.getMensaje(idiomaUsuario,"label.notif.tabla.col12")%>", "<%= meLanbide43I18n.getMensaje(idiomaUsuario,"label.notif.tabla.col12.title")%>");    
                tabNotificaciones.addColumna('260','left',"<%= meLanbide43I18n.getMensaje(idiomaUsuario,"label.notif.tabla.col13")%>", "<%= meLanbide43I18n.getMensaje(idiomaUsuario,"label.notif.tabla.col13.title")%>");    
                tabNotificaciones.addColumna('100','center',"<%= meLanbide43I18n.getMensaje(idiomaUsuario,"label.notif.tabla.col14")%>", "<%= meLanbide43I18n.getMensaje(idiomaUsuario,"label.notif.tabla.col14.title")%>");  
                tabNotificaciones.addColumna('60','center',"<%= meLanbide43I18n.getMensaje(idiomaUsuario,"label.notif.tabla.col15")%>", "<%= meLanbide43I18n.getMensaje(idiomaUsuario,"label.notif.tabla.col15.title")%>");    
                tabNotificaciones.addColumna('260','left',"<%= meLanbide43I18n.getMensaje(idiomaUsuario,"label.notif.tabla.col16")%>", "<%= meLanbide43I18n.getMensaje(idiomaUsuario,"label.notif.tabla.col16.title")%>");    
                tabNotificaciones.addColumna('60','center',"<%= meLanbide43I18n.getMensaje(idiomaUsuario,"label.notif.tabla.col17")%>", "<%= meLanbide43I18n.getMensaje(idiomaUsuario,"label.notif.tabla.col17.title")%>");    
                tabNotificaciones.addColumna('100','center',"<%= meLanbide43I18n.getMensaje(idiomaUsuario,"label.notif.tabla.col18")%>", "<%= meLanbide43I18n.getMensaje(idiomaUsuario,"label.notif.tabla.col18.title")%>");    
               
                tabNotificaciones.numColumnasFijas = 0;
                tabNotificaciones.displayCabecera=true;

                for(var cont = 0; cont < listaNotificacionesTabla.length; cont++){
                    tabNotificaciones.addFilaConFormato(listaNotificacionesTabla[cont], listaNotificacionesTabla_titulos[cont], listaNotificacionesTabla_estilos[cont])
                }

                tabNotificaciones.height = '470';
                tabNotificaciones.altoCabecera = 50;
                tabNotificaciones.scrollWidth = 2600;
                tabNotificaciones.dblClkFunction = 'dblClckTablaNotificaciones';
                tabNotificaciones.displayTabla();
                tabNotificaciones.pack();
            }
            
            function dblClckTablaNotificaciones(rowID,tableName){
                //pulsarReenviarNotificacion();
            }
            
            function pulsarReenviarNotificacion(){
                var fila;

                if(tabNotificaciones.selectedIndex != -1) {
                    fila = tabNotificaciones.selectedIndex;
 //                   if (tabNotificaciones.lineas[fila][12]!=null && tabNotificaciones.lineas[fila][12]!=''){
                    if (tabNotificaciones.lineas[fila][12] != "-"){
                        jsp_alerta("A",'<%=meLanbide43I18n.getMensaje(idiomaUsuario,"msg.notificacionCodigoPlatea")%>');
                    }else{ 
                        if (tabNotificaciones.lineas[fila][14]=="null"){
                            jsp_alerta("A",'<%=meLanbide43I18n.getMensaje(idiomaUsuario,"msg.notificacionYaMarcada")%>');
                        }
                        else{
                            reenvioNotificacion(fila);
                        }
                    }
                }else{
                        jsp_alerta('A', '<%=meLanbide43I18n.getMensaje(idiomaUsuario, "msg.msjNoSelecFila")%>');
                    }    
            }
            
            function reenvioNotificacion(fila){
                var codProc = document.getElementById("codListaProcedimiento").value;
                var numExped= (document.getElementById("numExped").value).toUpperCase();
                var fecDesde= document.getElementById("fecDesde").value;
                var fecHasta= document.getElementById("fecHasta").value;
                var codResultado = document.getElementById("codListaResultado").value;

                var ajax = getXMLHttpRequest();
                var nodos = null;
                var CONTEXT_PATH = '<%=request.getContextPath()%>'
                var url = CONTEXT_PATH + "/PeticionModuloIntegracion.do";
                var parametros = "";
                var control = new Date();

                parametros = "tarea=preparar&modulo=MELANBIDE43&operacion=reenviarNotificacion&tipo=0&codNotif=" + tabNotificaciones.lineas[fila][0]
                             + "&codProc=" + codProc
                             + "&numExped=" + numExped
                             + "&fecDesde=" + fecDesde
                             + "&fecHasta=" + fecHasta
                             + "&resultado=" + codResultado
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
                    listaNotificaciones=convertirXML(nodos);                            
                    recargarTablaNotificaciones(listaNotificaciones);
                    // vuelvo a colocarme al principio del XML para recuperar el código de operacion
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
        </script>
              
    </body>
</html>


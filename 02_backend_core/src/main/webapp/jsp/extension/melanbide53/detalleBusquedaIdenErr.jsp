<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.agora.interfaces.user.web.administracion.mantenimiento.MantenimientosAdminForm"%>
<%@page import="es.altia.agora.business.util.GeneralValueObject"%>
<%@page import="java.util.Vector"%>
<%@page import="java.util.Hashtable"%>
<%@page import="java.io.*"%>
<%@page import="java.util.*"%>
<%@page import="org.apache.commons.lang.StringEscapeUtils"%>
<%@page import="es.altia.agora.interfaces.user.web.administracion.MyHttpSessionListener"%>
<%@page import="es.altia.agora.interfaces.user.web.administracion.SessionInfo"%>
<%@page import="es.altia.agora.interfaces.user.web.administracion.InformacionSistemaForm"%>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide53.i18n.MeLanbide53I18n" %>
<%@page import="es.altia.common.service.config.Config"%>
<%@page import="es.altia.common.service.config.ConfigServiceHelper"%>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide53.vo.DetalleErrorVO"%>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide53.util.ConstantesMeLanbide53" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide53.util.ConfigurationParameter" %>
<%@page import="java.util.ArrayList" %>
<%@page import="java.util.List" %>


<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<html>
    <head><jsp:include page="/jsp/administracion/tpls/app-constants.jsp" />
        <meta http-equiv="Content-Type" content="text/html; charset=ISO_8859-1">
		<meta http-equiv="X-UA-Compatible" content="IE=Edge"/>
        <title>Gestion Errores</title>

        <%
            UsuarioValueObject usuarioVO = new UsuarioValueObject();
            int numeroTotalRegistros = 0;
            String iniciaModulo = "";
            int idiomaUsuario = 1;
            int apl = 5;
            String css = "";
            if (session.getAttribute("usuario") != null) {
                usuarioVO = (UsuarioValueObject) session.getAttribute("usuario");
                apl = usuarioVO.getAppCod();
                idiomaUsuario = usuarioVO.getIdioma();
                css = usuarioVO.getCss();
            }
            
            if (request.getAttribute("numeroTotalRegistros") != null) {
                numeroTotalRegistros = (Integer)request.getAttribute("numeroTotalRegistros");
            }
            if (request.getAttribute("iniciaModulo") != null) {
                iniciaModulo = (String)request.getAttribute("iniciaModulo");
            }

            MeLanbide53I18n meLanbide53I18n = MeLanbide53I18n.getInstance();
            String numMaxLineas = ConfigurationParameter.getParameter(ConstantesMeLanbide53.NUMERO_LINEAS_PAGINA, ConstantesMeLanbide53.FICHERO_PROPIEDADES);
            if(numMaxLineas == null || numMaxLineas=="")
                numMaxLineas="15";
       %>
       
       <script type="text/javascript">
            var APP_CONTEXT_PATH = '<%=request.getContextPath()%>';
       </script>
       
       <script type="text/javascript">
            function buscaCodListaTipo (codListaTipo){
                comboTipo.buscaCodigo(codListaTipo);
            }
            function buscaCodListaCritico (codListaCritico){
                comboCritico.buscaCodigo(codListaCritico);
            }
            //fin paginación
            
            function tipo(){
                //Recuperamos el valor seleccionado en el combo.
                if(document.getElementById("codListaTipo") != null){
                    var codigo = document.getElementById("codListaTipo").value;
                    buscaCodListaTipo(codigo);
                }
            }
            function critico(){
                //Recuperamos el valor seleccionado en el combo.
                if(document.getElementById("codListaCritico") != null){
                    var codigo = document.getElementById("codListaCritico").value;
                    buscaCodListaCritico(codigo);
                }
            }
            var listaCritico = new Array();
            var listaCodigosCritico = new Array();
            listaCodigosCritico[0] = 1;
            listaCodigosCritico[1] = 2;
            listaCodigosCritico[2] = 3;
            listaCodigosCritico[3] = 4;
            listaCritico[0] = '<%=meLanbide53I18n.getMensaje(idiomaUsuario,"label.iderror.critico.bloq")%>';
            listaCritico[1] = '<%=meLanbide53I18n.getMensaje(idiomaUsuario,"label.iderror.critico.grave")%>';
            listaCritico[2] = '<%=meLanbide53I18n.getMensaje(idiomaUsuario,"label.iderror.critico.leve")%>';
            listaCritico[3] = '<%=meLanbide53I18n.getMensaje(idiomaUsuario,"label.iderror.critico.inf")%>';

            var listaTipo = new Array();
            var listaCodigosTipo = new Array();
            listaCodigosTipo[0] = 1;
            listaCodigosTipo[1] = 2;
            listaTipo[0] = '<%=meLanbide53I18n.getMensaje(idiomaUsuario,"label.iderror.tipo.sist")%>';
            listaTipo[1] = '<%=meLanbide53I18n.getMensaje(idiomaUsuario,"label.iderror.tipo.func")%>';
       </script>
           
           

        <!-- Estilos -->
        <jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
        <jsp:setProperty name="descriptor"  property="idi_cod" value= "<%=idiomaUsuario%>" />
        <jsp:setProperty name="descriptor"  property="apl_cod" value="<%=apl%>" />
		<link rel="StyleSheet" media="screen" type="text/css" href="<%=request.getContextPath()%><%=css%>">
		<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script>
		<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/TablaNueva.js"></script>                  
        
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/general.js"></script>
        <script type="text/javascript"  src="<%=request.getContextPath()%>/scripts/domlay.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/tabpane.js"></script>
        <script type="text/javascript"  src="<%=request.getContextPath()%>/scripts/listas.js"></script>
       
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/validaciones.js"></script>
        
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/estilo.css"/>
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/extension/melanbide53/melanbide53.css"/>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/validaciones.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/listaComboBox.js"></script>
        
		<script type="text/javascript" src="<c:url value='/scripts/listaComboBox.js'/>"></script>
		<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/general.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/popup.js"></script>
        <script type="text/javascript" src="<c:url value='/scripts/calendario.js'/>"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/melanbide53/lanbide.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/melanbide53/JavaScriptUtil.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/melanbide53/Parsers.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/melanbide53/InputMask.js"></script>
		
        <!-- Eventos onKeyPress compatibilidad firefox  -->
        <!--script type="text/javascript" src="http://code.jquery.com/jquery-1.4.4.js"></script-->
        
    </head>
    <body class="bandaBody">
        <form>
            <h2 class="legendAzul" id="pestanaPrinc"><%=meLanbide53I18n.getMensaje(idiomaUsuario,"label.tituloPrincipalId")%></h2>
            <div class="contenidoPantalla">
                <div style="clear: both;">
                    <div id="div_label" class="sub3titulo" style=" text-align: center;">
                        <label class="sub3titulo" style=" text-align: center; position: relative;" ><%=meLanbide53I18n.getMensaje(idiomaUsuario, "legend.titulo.busquedaId")%></label>
                    </div>
                    <div>
                        <table id="tablaBusqueda">
                            <tbody>
                                <tr>
                                    <td style="width: 20%">
                                        <label class="etiqueta">
                                            <%=meLanbide53I18n.getMensaje(idiomaUsuario,"label.error.id.fk")%>
                                           
                                        </label>                            
                                    </td>
                                    <td>
                                        <input id="idError" name="idError" type="text" class="inputTextoM53" size="25" maxlength="25">
                                    </td>
                                </tr>
                                <tr>
                                    <td style="width: 20%">
                                        <label class="etiqueta">
                                            <%=meLanbide53I18n.getMensaje(idiomaUsuario,"label.detalle.descripcionErrorCorta")%>
                                        </label>                            
                                    </td>
                                    <td>
                                        <input id="desc" name="desc" type="text" class="inputTextoM53" size="100" maxlength="100">
                                    </td>
                                </tr>
                                <tr>
                                    <td colspan="2">
                                        <div class="lineaFormulario">
                                            <div style="width: 20%; float: left;" class="etiqueta">
                                                <%=meLanbide53I18n.getMensaje(idiomaUsuario,"label.detalle.tipoError")%>
                                            </div>
                                            <div>
                                                <input type="text" name="codListaTipo" id="codListaTipo" size="2" class="inputTexto" value="" onkeyup="xAMayusculas(this);" />
                                                <input type="text" name="descListaTipo"  id="descListaTipo" size="45" class="inputTexto" readonly="true" value="" />
                                                <a href="" id="anchorListaTipo" name="anchorListaTipo">
                                                    <img src="<%=request.getContextPath()%>/images/listas/botondesplegable.gif" id="botonAplicacion"
                                                         name="botonAplicacion" height="14" width="14" border="0" style="cursor:hand;">
                                                </a>
                                            </div>
                                        </div>
                                    </td>
                                </tr>
                                <tr>
                                    <td colspan="2">
                                        <div class="lineaFormulario">
                                            <div style="width: 20%; float: left;" class="etiqueta" >
                                                <%=meLanbide53I18n.getMensaje(idiomaUsuario,"label.detalle.criticError")%>
                                            </div>
                                            <div>
                                                <input type="text" name="codListaCritico" id="codListaCritico" size="2" class="inputTexto" value="" onkeyup="xAMayusculas(this);" />
                                                <input type="text" name="descListaCritico"  id="descListaCritico" size="45" class="inputTexto" readonly="true" value="" />
                                                <a href="" id="anchorListaCritico" name="anchorListaCritico">
                                                    <img src="<%=request.getContextPath()%>/images/listas/botondesplegable.gif" id="botonAplicacion"
                                                         name="botonAplicacion" height="14" width="14" border="0" style="cursor:hand;">
                                                </a>
                                            </div>
                                        </div>
                                    </td>
                                </tr>
                                <tr>
    <!--                                <td style="width: 15%"></td>-->
                                    <td style="text-align: center;" colspan="2">
                                        <input type="button" id="botonBusqueda" name="botonBusqueda" class="botonGeneral" value="<%=meLanbide53I18n.getMensaje(idiomaUsuario, "btn.buscar")%>" onclick="pulsarBusquedaFiltr();">
    <!--                                    <IMG  id="botonBusqueda"  name="botonBusqueda"  style="CURSOR: hand; BORDER-TOP: 0px; BORDER-RIGHT: 0px; BORDER-BOTTOM: 0px; BORDER-LEFT: 0px; margin-right: 5px; " 
                                              src="/flexia/images/prismaticos.gif" onclick="pulsarBusquedaFiltrando()">-->
                                    </td>
                                </tr>
                            </tbody>
                        </table>
                    </div>
                </div>
                <div style="clear: both;">
                    <div>
                        <div id="div_label" class="sub3titulo" style=" text-align: center;">
                            <label class="sub3titulo" style=" text-align: center; width: 100%; position: relative;" ><%=meLanbide53I18n.getMensaje(idiomaUsuario, "legend.titulo.tablaId")%></label>
                        </div>    
                           
                        <div id="divGeneral" style="overflow-y: auto; overflow-x: auto; height: 390px;">     
                            <div id="listaErrores" style="padding: 5px; width:1000px; height: 360px; text-align: center; overflow-x:auto; overflow-y:auto;margin:0px;margin-top:0px;" align="center"></div>
                        </div>   

                        <div class="botonera" style="text-align: center;">
                            <input type="button" id="btnNuevoAcceso" name="btnNuevoAcceso" class="botonGeneral"  value="<%=meLanbide53I18n.getMensaje(idiomaUsuario, "btn.nuevo")%>" onclick="pulsarNuevoError();">
                            <input type="button" id="btnActualizarError" name="btnActualizarError" class="botonGeneral" value="<%=meLanbide53I18n.getMensaje(idiomaUsuario, "btn.modificar")%>" onclick="pulsarModificarError();">
                            <input type="button" id="btnActualizarError" name="btnActualizarError" class="botonGeneral" value="<%=meLanbide53I18n.getMensaje(idiomaUsuario, "btn.eliminar")%>" onclick="pulsarEliminarError();">
                            <input type="button" id="btnVolver" name="btnVolver" class="botonGeneral" value="<%=meLanbide53I18n.getMensaje(idiomaUsuario, "btn.volver")%>" onclick="cerrarVentana();"/>   
                        </div>
                    </div>
                </div>
            </div>

            <!--Script Ejecucion Elementos Pagina   legendAzul-->
            <script type="text/javascript">
                
                //Combos
                var comboCritico = new Combo("ListaCritico");
                comboCritico.addItems(listaCodigosCritico, listaCritico);
                comboCritico.change = critico;
                
                
                var comboTipo = new Combo("ListaTipo");
                comboTipo.addItems(listaCodigosTipo, listaTipo);
                comboTipo.change = tipo;
              
                
                //Tabla Errores
                var tablaErrores;
                var listaErrores = new Array();
                var listaErroresTabla = new Array();

                //right - left - center
                tablaErrores = new Tabla(true,'<%=descriptor.getDescripcion("buscar")%>','<%=descriptor.getDescripcion("anterior")%>','<%=descriptor.getDescripcion("siguiente")%>','<%=descriptor.getDescripcion("mosFilasPag")%>','<%=descriptor.getDescripcion("msgNoResultBusq")%>','<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>','<%=descriptor.getDescripcion("filtrDeTotal")%>','<%=descriptor.getDescripcion("primero")%>','<%=descriptor.getDescripcion("ultimo")%>',document.getElementById('listaErrores'), 1400);
                tablaErrores.addColumna('160','left',"<%=meLanbide53I18n.getMensaje(idiomaUsuario,"gestionErrores.tablaIdErrores.col1")%>");
                tablaErrores.addColumna('300','left',"<%=meLanbide53I18n.getMensaje(idiomaUsuario,"gestionErrores.tablaIdErrores.col2")%>");
                tablaErrores.addColumna('540','left',"<%=meLanbide53I18n.getMensaje(idiomaUsuario,"gestionErrores.tablaIdErrores.col3")%>");
                tablaErrores.addColumna('150','left',"<%=meLanbide53I18n.getMensaje(idiomaUsuario,"gestionErrores.tablaIdErrores.col4")%>");
                tablaErrores.addColumna('150','left',"<%=meLanbide53I18n.getMensaje(idiomaUsuario,"gestionErrores.tablaIdErrores.col5")%>");
                //tablaErrores.addColumna('100','left',"<%=meLanbide53I18n.getMensaje(idiomaUsuario,"gestionErrores.tablaIdErrores.col6")%>");
                //tablaErrores.addColumna('150','left',"<%=meLanbide53I18n.getMensaje(idiomaUsuario,"gestionErrores.tablaIdErrores.col7")%>");

                tablaErrores.displayCabecera=true;
                tablaErrores.height = 600;
              
                <%  		
                    DetalleErrorVO objectVO = new DetalleErrorVO();
                    List<DetalleErrorVO> _list = null;
                    if(request.getAttribute("ListErrores")!=null){
                        _list = (List<DetalleErrorVO>)request.getAttribute("ListErrores");
                    }													
                    if (_list!= null && _list.size() >0){
                        for (int indice=0;indice<_list.size();indice++)
                        {
                            objectVO = _list.get(indice);
                %>
                var descC = '<%=objectVO.getDescripcionCorta()%>';
                var desc = '<%=objectVO.getDescripcion()%>';
                var desTipo = '<%=objectVO.getDesTipo()%>';
                var desCritico = '<%=objectVO.getDesCritico()%>';
                    listaErrores[<%=indice%>] = ['<%=objectVO.getId()%>', descC,desc, desTipo, desCritico];
                    listaErroresTabla[<%=indice%>] = ['<%=objectVO.getId()%>', descC, desc, desTipo, desCritico];
                <%
                        }// for
                    }// if
                %>
                listaSelErrOriginal = listaErroresTabla;
                tablaErrores.lineas=listaErroresTabla;
                //tablaErrores.displayTabla();  DavidL
                tablaErrores.displayTablaConTooltips(listaErroresTabla);   //DavidL
                //document.getElementById('listaErrores').children[0].children[1].children[0].children[0].ondblclick = function(event){
                //            pulsarModificarError(event);
                if(navigator.appName.indexOf("Internet Explorer")!=-1 || (navigator.appName.indexOf("Netscape")!=-1 && navigator.appCodeName.match(/Mozilla/))){
                    try{
                        var div = document.getElementById('listaErrores');
                        //div.children[0].children[0].children[0].children[0].children[0].children[0].children[0].children[0].children[0].children[0].children[0].children[0].children[0].children[1].style.width = '100%';
                        //div.children[0].children[1].children[0].children[0].children[0].children[0].children[0].children[0].children[0].children[0].children[0].children[1].style.width = '100%';
                        if(navigator.appName.indexOf("Internet Explorer")!=-1 && !(navigator.userAgent.match(/compatible/))){
                            div.children[0].children[0].children[0].children[0].children[0].children[0].children[1].style.width = '100%';
                            div.children[0].children[1].children[0].children[0].children[0].style.width = '100%';
                        }else{
                            div.children[0].children[0].children[0].children[0].children[0].style.width = '100%';
                            div.children[0].children[1].children[0].children[0].style.width = '100%';
                        }
                    }
                    catch (err) {

                    }
                }
            </script>
            <!-- Script Con Funciones-->
            <script type="text/javascript">
                var mensajeValidacion="";
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

                function comprobarFechaLanbide(inputFecha) {
                    var formato = 'dd/mm/yyyy';
                    if (Trim(inputFecha.value)!='') {
                      var D = ValidarFechaConFormatoLanbide(inputFecha.value,formato);
                      if (!D[0]){
                        jsp_alerta("A","<%=meLanbide53I18n.getMensaje(idiomaUsuario, "msg.fechaNoVal")%>");
                        document.getElementById(inputFecha.name).focus();
                        document.getElementById(inputFecha.name).select();
                        return false;
                      } else {
                        inputFecha.value = D[1];
                        return true;
                      }//if (!D[0])
                    }//if (Trim(inputFecha.value)!='')
                    return true;
                }//comprobarFechaLanbide

                function mostrarCalFechaE(evento) { 
                    if(window.event) 
                        evento = window.event;
                    if (document.getElementById("calMeLanbide53Fecha_busqE").src.indexOf("icono.gif") != -1 )
                        showCalendar('forms[0]','meLanbide53Fecha_busqE',null,null,null,'','calMeLanbide53Fecha_busqE','',null,null,null,null,null,null,null, null,evento);        
                }//mostrarCalFechaE

                function mostrarCalFechaS(evento) { 
                    if(window.event) 
                        evento = window.event;
                    if (document.getElementById("calMeLanbide53Fecha_busqS").src.indexOf("icono.gif") != -1 )
                        showCalendar('forms[0]','meLanbide53Fecha_busqS',null,null,null,'','calMeLanbide53Fecha_busqS','',null,null,null,null,null,null,null, null,evento);        
                }//mostrarCalFechaS

                function mostrarCalFechaRevE(evento) { 
                    if(window.event) 
                        evento = window.event;
                    if (document.getElementById("calMeLanbide53Fecha_RevbusqE").src.indexOf("icono.gif") != -1 )
                        showCalendar('forms[0]','meLanbide53Fecha_RevbusqE',null,null,null,'','calMeLanbide53Fecha_RevbusqE','',null,null,null,null,null,null,null, null,evento);        
                }//mostrarCalFechaRevE

                function mostrarCalFechaRevF(evento) { 
                    if(window.event) 
                        evento = window.event;
                    if (document.getElementById("calMeLanbide53Fecha_RevbusqF").src.indexOf("icono.gif") != -1 )
                        showCalendar('forms[0]','meLanbide53Fecha_RevbusqF',null,null,null,'','calMeLanbide53Fecha_RevbusqF','',null,null,null,null,null,null,null, null,evento);        
                }//mostrarCalFechaRevF

                function formatearHora(control,evento){  
                    evento = (evento) ? evento : window.event;  
                    var charCode = (evento.which) ? evento.which : evento.keyCode;  

                    var ignore = evento.altKey || evento.ctrlKey || inArray(charCode, JST_IGNORED_KEY_CODES);
                    if (!ignore) {
                        var range = getInputSelectionRange(control);
                        if (range != null && range[0] != range[1]) {
                            replaceSelection(this, "");
                        }
                    }

                    if (!ignore) {  //charCode >= 48 && charCode <= 56 && control.value.length < 8
                        var i = control.value.length;  
                        var texto = "";  
                        if (i == 2 || i == 5) {
                            texto = control.value+":";  
                            control.value = texto;  
                        }  
                        return true;  
                    }  
                    return false;  
                }

                function comprobarMaskaraHora(control){
                    if(control!=null){
                        if(control.value==""){
                            return true;
                        }
                        var texto = control.value.split(':');
                        if(texto.length!=3){
                            jsp_alerta("A", '<%=meLanbide53I18n.getMensaje(idiomaUsuario, "msg.horaNoVal")%>');
                            control.focus();
                            control.select();
                            return false;
                        }
                        if(!(texto[0]>=0 && texto[0]<=24)){
                                jsp_alerta("A", '<%=meLanbide53I18n.getMensaje(idiomaUsuario, "msg.horaNoVal")%>');
                                control.focus();
                                control.select();
                                return false;
                        }else{
                            if(texto[0].length<2){
                                texto[0]= '0'+texto[0];
                                control.value=texto[0]+':'+texto[1]+':'+texto[2];
                            }
                        }
                        if(!(texto[1]>=0 && texto[1]<=59) || !(texto[2]>=0 && texto[2]<=59)){
                                jsp_alerta("A", '<%=meLanbide53I18n.getMensaje(idiomaUsuario, "msg.horaNoVal")%>');
                                control.focus();
                                control.select();
                                return false;
                        }else{
                            if(texto[1].length<2){
                                texto[1]= '0'+texto[1];
                                control.value=texto[0]+':'+texto[1]+':'+texto[2];
                            }
                            if(texto[2].length<2){
                                texto[2]= '0'+texto[2];
                                control.value=texto[0]+':'+texto[1]+':'+texto[2];
                            }

                        }
                    }
                    else{
                        jsp_alerta("A", '<%=meLanbide53I18n.getMensaje(idiomaUsuario, "msg.horaNoVal")%>');
                    }
                }

                function pulsarNuevoError() {
                    var control = new Date();
                    var result = null;
                    if (navigator.appName.indexOf("Internet Explorer") != -1) {
                        lanzarPopUpModal('<%=request.getContextPath()%>/PeticionModuloIntegracion.do?tarea=preparar&modulo=MELANBIDE53&operacion=cargarNuevoError&tipo=0&nuevo=1&control='+control.getTime(), 760, 1100, 'no', 'no', function(result){
                            if (result != undefined) {
                                if (result[0] == '0') {                                                                                                        
                                    recargarTablaErrores(result); 
                                }
                            }
                        });
                    } else {
                        lanzarPopUpModal('<%=request.getContextPath()%>/PeticionModuloIntegracion.do?tarea=preparar&modulo=MELANBIDE53&operacion=cargarNuevoError&tipo=0&nuevo=1&control='+control.getTime(), 760, 1100, 'no', 'no', function(result){
                            if (result != undefined) {
                               if (result[0] == '0') {                                                                      
                                    recargarTablaErrores(result);                             
                                }
                            }
                        });
                    }
                    
                }

                function pulsarModificarError() {
                    if (tablaErrores.selectedIndex != -1) {
                        var control = new Date();
                        var result = null;
                        if (navigator.appName.indexOf("Internet Explorer") != -1) {
                            lanzarPopUpModal('<%=request.getContextPath()%>/PeticionModuloIntegracion.do?tarea=preparar&modulo=MELANBIDE53&operacion=cargarPantallaDetallesIDError&tipo=0&visible=true&nuevo=0&idError=' + listaErrores[tablaErrores.selectedIndex][0] + '&control=' + control.getTime(), 760, 1100, 'no', 'no', function(result){
                                if (result != undefined) {
                                    if (result[0] == '0') {                                                  
                                        recargarTablaErrores(result);   
                                    }
                                }
                            });
                        } else {
                            lanzarPopUpModal('<%=request.getContextPath()%>/PeticionModuloIntegracion.do?tarea=preparar&modulo=MELANBIDE53&operacion=cargarPantallaDetallesIDError&tipo=0&visible=true&nuevo=0&idError=' + listaErrores[tablaErrores.selectedIndex][0] + '&control=' + control.getTime(), 760, 1100, 'no', 'no', function(result){
                                 if (result != undefined) {
                                    if (result[0] == '0') {                                       
                                        recargarTablaErrores(result);   
                                    }
                                }
                            });
                        }
                        
                    }
                    else {
                        jsp_alerta('A', '<%=meLanbide53I18n.getMensaje(idiomaUsuario, "msg.msjNoSelecFila")%>');
                    }
                }
                
                function pulsarEliminarError() {
                    if (tablaErrores.selectedIndex != -1) {
                        var ajax = getXMLHttpRequest();
                        var nodos = null;
                        var CONTEXT_PATH = '<%=request.getContextPath()%>'
                        var url = CONTEXT_PATH + "/PeticionModuloIntegracion.do";
                        var parametros = "";
                        var control = new Date();
                        parametros = 'tarea=preparar&modulo=MELANBIDE53&operacion=eliminarIdError&tipo=0&idError='+listaErrores[tablaErrores.selectedIndex][0]+'&control='+control.getTime();
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
                            var nodoCampo;
                            for (j = 0; hijos != null && j < hijos.length; j++) {
                                if (hijos[j].nodeName == "CODIGO_OPERACION") {
                                    codigoOperacion = hijos[j].childNodes[0].nodeValue;
                                    listaNueva[j] = codigoOperacion;
                                }//if(hijos[j].nodeName=="CODIGO_OPERACION")                      
                                else if (hijos[j].nodeName == "FILA") { 
                                    nodoFila = hijos[j];
                                    hijosFila = nodoFila.childNodes;
                                    for (var cont = 0; cont < hijosFila.length; cont++) {
                                        if (hijos[j].nodeName == "CODIGO_OPERACION") {
                                            codigoOperacion = hijos[j].childNodes[0].nodeValue;
                                            listaNueva[j] = codigoOperacion;
                                        }//if(hijos[j].nodeName=="CODIGO_OPERACION")                      
                                        else if (hijos[j].nodeName == "FILA") {
                                            nodoFila = hijos[j];
                                            hijosFila = nodoFila.childNodes;
                                            for (var cont = 0; cont < hijosFila.length; cont++) {
                                                if (hijosFila[cont].nodeName == "IDEN_ERR_ID") {
                                                    if (hijosFila[cont].childNodes.length > 0) {
                                                        fila[0] = hijosFila[cont].childNodes[0].nodeValue;
                                                    }
                                                    else {
                                                        fila[0] = '-';
                                                    }
                                                }
                                                else if (hijosFila[cont].nodeName == "IDEN_ERR_DESC_C") {
                                                    if (hijosFila[cont].childNodes.length > 0) {
                                                        fila[1] = hijosFila[cont].childNodes[0].nodeValue;
                                                    }
                                                    else {
                                                        fila[1] = '-';
                                                    }
                                                }
                                                else if (hijosFila[cont].nodeName == "IDEN_ERR_DESC") {
                                                    if (hijosFila[cont].childNodes.length > 0) {
                                                        fila[2] = hijosFila[cont].childNodes[0].nodeValue;
                                                    }
                                                    else {
                                                        fila[2] = '-';
                                                    }
                                                }
                                                else if (hijosFila[cont].nodeName == "DES_TIPO") {
                                                    if (hijosFila[cont].childNodes.length > 0) {
                                                        fila[3] = hijosFila[cont].childNodes[0].nodeValue;
                                                    }
                                                    else {
                                                        fila[3] = '-';
                                                    }
                                                }
                                                else if (hijosFila[cont].nodeName == "DES_CRITICO") {
                                                    if (hijosFila[cont].childNodes.length > 0) {
                                                        fila[4] = hijosFila[cont].childNodes[0].nodeValue;
                                                    }
                                                    else {
                                                        fila[4] = '-';
                                                    }
                                                }
                                            }
                                        }
                                        listaNueva[j] = fila;
                                        fila = new Array();
                                    }
                                }
                            }//for(j=0;hijos!=null && j<hijos.length;j++)
                            if (codigoOperacion == "0") {
                                recargarTablaErrores(listaNueva);                                
                                jsp_alerta("A",'<%=meLanbide53I18n.getMensaje(idiomaUsuario,"error.registroEleminadoOK")%>');  //DavidL
                            } else if (codigoOperacion == "1") {
                                jsp_alerta("A", '<%=meLanbide53I18n.getMensaje(idiomaUsuario,"error.errorBD")%>');
                            } else if (codigoOperacion == "2") {
                                jsp_alerta("A", '<%=meLanbide53I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                            } else if (codigoOperacion == "3") {
                                jsp_alerta("A", '<%=meLanbide53I18n.getMensaje(idiomaUsuario,"error.pasoParametros")%>');
                            } else {
                                jsp_alerta("A", '<%=meLanbide53I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                            }
                        }
                        catch(Err){
                            jsp_alerta("A",'<%=meLanbide53I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                        }//try-catch
                    }
                }
                
                function cerrarVentana() {
                    if (navigator.appName == "Microsoft Internet Explorer") {
                        window.parent.window.opener = null;
                        window.parent.window.close();
                    } else if (navigator.appName == "Netscape") {
                        top.window.opener = top;
                        top.window.open('', '_parent', '');
                        top.window.close();
                    } else {
                        window.close();
                    }
                }
                function pulsarObtenerDocs() {
                    if (tablaErrores.selectedIndex != -1) {
                        var control = new Date();
                        //var result = null;
                        var oidSolicitud=listaErrores[tablaErrores.selectedIndex][8];
                        var codProcedimiento=listaErrores[tablaErrores.selectedIndex][6];
                        if(validarDatosDescargaDocumentos(oidSolicitud,codProcedimiento)){
                            if (navigator.appName.indexOf("Internet Explorer") != -1) {
                                //result = 
                                lanzarPopUpModal('<%=request.getContextPath()%>/PeticionModuloIntegracion.do?tarea=preparar&modulo=MELANBIDE53&operacion=cargarObtenerDocumentos&tipo=0&nuevo=0&oidSolicitud=' + oidSolicitud + '&codProcedimiento=' + codProcedimiento + '&control=' + control.getTime(), 350, 800, 'no', 'no', function(result){});
                            } else {
                                //result = 
                                lanzarPopUpModal('<%=request.getContextPath()%>/PeticionModuloIntegracion.do?tarea=preparar&modulo=MELANBIDE53&operacion=cargarObtenerDocumentos&tipo=0&nuevo=0&oidSolicitud=' + oidSolicitud + '&codProcedimiento=' + codProcedimiento + '&control=' + control.getTime(), 350, 800, 'no', 'no', function(result){});
                            }
                        }else{
                            jsp_alerta('A',mensajeValidacion);
                        }
                    }else {
                        jsp_alerta('A', '<%=meLanbide53I18n.getMensaje(idiomaUsuario, "msg.msjNoSelecFila")%>');
                    }
                }


                function recargarTablaErrores(result) {
                    var fila;
                    listaErrores = new Array();
                    listaErroresTabla = new Array();
                    for (var i = 1; i < result.length; i++) {
                        fila = result[i];
                        listaErrores[i - 1] = [fila[0],fila[1],fila[2],fila[3], fila[4]];
                               
                        listaErroresTabla[i - 1] = [fila[0],fila[1],fila[2],fila[3], fila[4]];
                    }
 
		    tablaErrores = new Tabla(true,'<%=descriptor.getDescripcion("buscar")%>','<%=descriptor.getDescripcion("anterior")%>','<%=descriptor.getDescripcion("siguiente")%>','<%=descriptor.getDescripcion("mosFilasPag")%>','<%=descriptor.getDescripcion("msgNoResultBusq")%>','<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>','<%=descriptor.getDescripcion("filtrDeTotal")%>','<%=descriptor.getDescripcion("primero")%>','<%=descriptor.getDescripcion("ultimo")%>',document.getElementById('listaErrores'), 1400);
                    tablaErrores.addColumna('160','left',"<%=meLanbide53I18n.getMensaje(idiomaUsuario,"gestionErrores.tablaIdErrores.col1")%>");
                    tablaErrores.addColumna('300','left',"<%=meLanbide53I18n.getMensaje(idiomaUsuario,"gestionErrores.tablaIdErrores.col2")%>");
                    tablaErrores.addColumna('540','left',"<%=meLanbide53I18n.getMensaje(idiomaUsuario,"gestionErrores.tablaIdErrores.col3")%>");
                    tablaErrores.addColumna('150','left',"<%=meLanbide53I18n.getMensaje(idiomaUsuario,"gestionErrores.tablaIdErrores.col4")%>");
                    tablaErrores.addColumna('150','left',"<%=meLanbide53I18n.getMensaje(idiomaUsuario,"gestionErrores.tablaIdErrores.col5")%>");
                 

                    tablaErrores.displayCabecera=true;
                    tablaErrores.height = 600;
                    listaSelErrOriginal = listaErroresTabla;
                    tablaErrores.lineas = listaErroresTabla;
                    tablaErrores.displayTablaConTooltips(listaErroresTabla);   
                    //document.getElementById('listaErrores').children[0].children[1].children[0].children[0].ondblclick = function(event){
                    //        pulsarModificarError(event);
                    if(navigator.appName.indexOf("Internet Explorer")!=-1 || (navigator.appName.indexOf("Netscape")!=-1 && navigator.appCodeName.match(/Mozilla/))){
                        try{
                            var div = document.getElementById('listaErrores');
                            //div.children[0].children[0].children[0].children[0].children[0].children[0].children[0].children[0].children[0].children[0].children[0].children[0].children[0].children[1].style.width = '100%';
                            //div.children[0].children[1].children[0].children[0].children[0].children[0].children[0].children[0].children[0].children[0].children[0].children[1].style.width = '100%';                                                      
                            if(navigator.appName.indexOf("Internet Explorer")!=-1 && !(navigator.userAgent.match(/compatible/))){
                                div.children[0].children[0].children[0].children[0].children[0].children[0].children[1].style.width = '100%';
                                div.children[0].children[1].children[0].children[0].children[0].style.width = '100%';
                            }else{
                                div.children[0].children[0].children[0].children[0].children[0].style.width = '100%';
                                div.children[0].children[1].children[0].children[0].style.width = '100%';
                            }                        
                        }
                        catch (err) {
                            jsp_alerta("A", '<%=meLanbide53I18n.getMensaje(idiomaUsuario, "msg.errorGen")%>' + ' - ' + err.message );
                        }
                    }
                }

                function pulsarBusquedaFiltr(){
                    if (true) {
                        var ajax = getXMLHttpRequest();
                        var nodos = null;
                        var CONTEXT_PATH = '<%=request.getContextPath()%>'
                        var url = CONTEXT_PATH + "/PeticionModuloIntegracion.do";
                        var parametros = "";
                        var control = new Date();
                        parametros = "tarea=preparar&modulo=MELANBIDE53&operacion=busquedaIdenError&tipo=0&control=" + control.getTime()
                                        + "&idError="+ document.getElementById('idError').value
                                        + "&descripcion="+ document.getElementById('desc').value
                                        + "&codTipo="+ document.getElementById('codListaTipo').value
                                        + "&codCritico="+ document.getElementById('codListaCritico').value
                        ;
                        try {
                            ajax.open("POST", url, false);
                            ajax.setRequestHeader("Content-Type", "application/x-www-form-urlencoded; charset=ISO-8859-1");
                            ajax.setRequestHeader("Accept", "text/xml, application/xml, text/plain");
                            //var formData = new FormData(document.getElementById('formContrato'));
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
                            for (j = 0; hijos != null && j < hijos.length; j++) {
                                if (hijos[j].nodeName == "CODIGO_OPERACION") {
                                    codigoOperacion = hijos[j].childNodes[0].nodeValue;
                                    listaNueva[j] = codigoOperacion;
                                }//if(hijos[j].nodeName=="CODIGO_OPERACION")                      
                                else if (hijos[j].nodeName == "FILA") {
                                    nodoFila = hijos[j];
                                    hijosFila = nodoFila.childNodes;
                                    for (var cont = 0; cont < hijosFila.length; cont++) {
                                    if (hijos[j].nodeName == "CODIGO_OPERACION") {
                                        codigoOperacion = hijos[j].childNodes[0].nodeValue;
                                        listaNueva[j] = codigoOperacion;
                                    }//if(hijos[j].nodeName=="CODIGO_OPERACION")                      
                                    else if (hijos[j].nodeName == "FILA") {
                                        nodoFila = hijos[j];
                                        hijosFila = nodoFila.childNodes;
                                        for (var cont = 0; cont < hijosFila.length; cont++) {
                                            if (hijosFila[cont].nodeName == "IDEN_ERR_ID") {
                                                if (hijosFila[cont].childNodes.length > 0) {
                                                    fila[0] = hijosFila[cont].childNodes[0].nodeValue;
                                                }
                                                else {
                                                    fila[0] = '-';
                                                }
                                            }
                                            else if (hijosFila[cont].nodeName == "IDEN_ERR_DESC_C") {
                                                if (hijosFila[cont].childNodes.length > 0) {
                                                    fila[1] = hijosFila[cont].childNodes[0].nodeValue;
                                                }
                                                else {
                                                    fila[1] = '-';
                                                }
                                            }
                                            else if (hijosFila[cont].nodeName == "IDEN_ERR_DESC") {
                                                if (hijosFila[cont].childNodes.length > 0) {
                                                    fila[2] = hijosFila[cont].childNodes[0].nodeValue;
                                                }
                                                else {
                                                    fila[2] = '-';
                                                }
                                            }
                                            else if (hijosFila[cont].nodeName == "DES_TIPO") {
                                                if (hijosFila[cont].childNodes.length > 0) {
                                                    fila[3] = hijosFila[cont].childNodes[0].nodeValue;
                                                }
                                                else {
                                                    fila[3] = '-';
                                                }
                                            }
                                            else if (hijosFila[cont].nodeName == "DES_CRITICO") {
                                                if (hijosFila[cont].childNodes.length > 0) {
                                                    fila[4] = hijosFila[cont].childNodes[0].nodeValue;
                                                }
                                                else {
                                                    fila[4] = '-';
                                                }
                                            }
                                        }
                                    }
                                    listaNueva[j] = fila;
                                    fila = new Array();
                                }
                            }//for(j=0;hijos!=null && j<hijos.length;j++)
                                if (codigoOperacion == "0") {
                                    recargarTablaErrores(listaNueva);
                                } else if (codigoOperacion == "1") {
                                    jsp_alerta("A", '<%=meLanbide53I18n.getMensaje(idiomaUsuario,"error.errorBD")%>');
                                } else if (codigoOperacion == "2") {
                                    jsp_alerta("A", '<%=meLanbide53I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                                } else if (codigoOperacion == "3") {
                                    jsp_alerta("A", '<%=meLanbide53I18n.getMensaje(idiomaUsuario,"error.pasoParametros")%>');
                                } else {
                                    jsp_alerta("A", '<%=meLanbide53I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                                }
                            }//if(
                        }
                        catch (Err) {
                            jsp_alerta("A", '<%=meLanbide53I18n.getMensaje(idiomaUsuario,"error.errorGen")%>' + ' - ' + Err.message);
                        }//try-catch
                    }
                    else {
                        jsp_alerta('A',mensajeValidacion);
                    }
                }
                
                function prepararFiltroChecked(idElement){
                    if(document.getElementById(idElement)!=null && document.getElementById(idElement).checked){
                        return '<%=ConstantesMeLanbide53.BOOLEAN_TRUE%>'; 
                    }else 
                        return "";
                }
                
                function validarFiltrosBusqueda(){
                    var result = true;
                    if(document.getElementById("meLanbide53Fecha_busqE").value!="" && document.getElementById("meLanbide53Fecha_busqS").value!="")
                    {
                        var fechaentrada = document.getElementById("meLanbide53Fecha_busqE").value.split("/");
                        var fechasalida = document.getElementById("meLanbide53Fecha_busqS").value.split("/");
                        fechaentrada=fechaentrada[2]+fechaentrada[1]+fechaentrada[0];
                        fechasalida=fechasalida[2]+fechasalida[1]+fechasalida[0];
                        if(fechaentrada>fechasalida){
                            mensajeValidacion='<%=meLanbide53I18n.getMensaje(idiomaUsuario, "msg.busqueda.errorintervalosfechaerror")%>';
                            document.getElementById("meLanbide53Fecha_busqE").select();
                            return false;
                        }

                    }
                    if(document.getElementById("hora_entrada_busq").value!="" && document.getElementById("hora_entrada_busqF").value!="")
                    {
                        var horaentrada = document.getElementById("hora_entrada_busq").value;
                        var horaentradaF = document.getElementById("hora_entrada_busqF").value;
                        if(horaentrada>horaentradaF){
                            mensajeValidacion='<%=meLanbide53I18n.getMensaje(idiomaUsuario, "msg.busqueda.errorintervaloshora")%>';
                            document.getElementById("hora_entrada_busq").select();
                            return false;
                        }

                    }
                    if(document.getElementById("meLanbide53Fecha_RevbusqE").value!="" && document.getElementById("meLanbide53Fecha_RevbusqF").value!="")
                    {
                        var fechaentrada = document.getElementById("meLanbide53Fecha_RevbusqE").value.split("/");
                        var fechasalida = document.getElementById("meLanbide53Fecha_RevbusqF").value.split("/");
                        fechaentrada=fechaentrada[2]+fechaentrada[1]+fechaentrada[0];
                        fechasalida=fechasalida[2]+fechasalida[1]+fechasalida[0];
                        if(fechaentrada>fechasalida){
                            mensajeValidacion='<%=meLanbide53I18n.getMensaje(idiomaUsuario, "msg.busqueda.errorintervalosfecharevision")%>';
                            document.getElementById("meLanbide53Fecha_RevbusqE").select();
                            return false;
                        }
                    }
                    return result;
                }
                
                function validarDatosDescargaDocumentos(oidSolicitud, procedimiento){
                    var result = true;
                    var _oidSolicitud = oidSolicitud;
                    var _procedimiento = procedimiento;
                    if(_oidSolicitud != null && _oidSolicitud!="" && _oidSolicitud!="null" )
                    {
                        if(_procedimiento!=null && _procedimiento!="" && _procedimiento!="null"){
                            return true;
                        }else{
                            mensajeValidacion='<%=meLanbide53I18n.getMensaje(idiomaUsuario, "msg.obtenerdocumento.sinprocedimiento")%>';
                            return false;
                        }
                    }else{
                        mensajeValidacion='<%=meLanbide53I18n.getMensaje(idiomaUsuario, "msg.obtenerdocumento.sinoid")%>';
                         return false;
                    }
                    return result;
                }
                
            </script>
        </form>
        <div id="popupcalendar" class="text"></div>   
    </body>
</html>
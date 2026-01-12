<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/fmt.tld" prefix="fmt" %>

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
<%@page import="es.altia.common.service.config.Config"%>
<%@page import="es.altia.common.service.config.ConfigServiceHelper"%>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide53.i18n.MeLanbide53I18n" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide53.vo.EstadisticasVO"%>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide53.util.ConstantesMeLanbide53" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide53.util.ConfigurationParameter" %>
<%@page import="java.util.Comparator" %>
<%@page import="java.util.ArrayList" %>
<%@page import="java.util.List" %>

<jsp:include page="/jsp/administracion/tpls/app-constants.jsp" />
<meta http-equiv="Content-Type" content="text/html; charset=ISO_8859-1">
		<meta http-equiv="X-UA-Compatible" content="IE=Edge" />
      
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
        MeLanbide53I18n meLanbide53I18n = MeLanbide53I18n.getInstance();
    %>  
    <script type="text/javascript">
        var APP_CONTEXT_PATH = '<%=request.getContextPath()%>';
    </script> 
    <link rel="StyleSheet" media="screen" type="text/css" href="<%=request.getContextPath()%><%=css%>">    
    <jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
    <jsp:setProperty name="descriptor"  property="idi_cod" value="<%=idiomaUsuario%>" />
    <jsp:setProperty name="descriptor"  property="apl_cod" value="<%=apl%>" />
    <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/general.js"></script>     
    <script type="text/javascript"  src="<%=request.getContextPath()%>/scripts/domlay.js"></script>    
    <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/tabpane.js"></script>
    <script type="text/javascript"  src="<%=request.getContextPath()%>/scripts/listas.js"></script>    
    <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/validaciones.js"></script>
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/estilo.css"/>       
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/extension/melanbide53/melanbide53.css"/>         
    <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/popup.js"></script> 
    <script type="text/javascript" src="<c:url value='/scripts/calendario.js'/>"></script>       
    <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/melanbide53/lanbide.js"></script>    
    <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/TablaNueva.js"></script>   
    <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/melanbide53/JavaScriptUtil.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/melanbide53/Parsers.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/melanbide53/InputMask.js"></script>    
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/css/font-awesome.min.css'/>" media="screen" >
    <link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/less/animated.less'/>" media="screen" >         
    <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script>    

    <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script>
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/>       
    <script type="text/javascript" src="<c:url value='/scripts/listaComboBox.js'/>"></script>    
    
    <script type="text/javascript">
        function crearTabla(){
            tablaEstadisticas = new Tabla(true,'<%=descriptor.getDescripcion("buscar")%>','<%=descriptor.getDescripcion("anterior")%>','<%=descriptor.getDescripcion("siguiente")%>','<%=descriptor.getDescripcion("mosFilasPag")%>','<%=descriptor.getDescripcion("msgNoResultBusq")%>','<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>','<%=descriptor.getDescripcion("filtrDeTotal")%>','<%=descriptor.getDescripcion("primero")%>','<%=descriptor.getDescripcion("ultimo")%>',document.getElementById('listaEstadisticas'));
            tablaEstadisticas.addColumna('40','center',"<%=meLanbide53I18n.getMensaje(idiomaUsuario,"gestionEstadisticas.tablaEstadisticas.col0")%>");
            tablaEstadisticas.addColumna('90','center',"<%=meLanbide53I18n.getMensaje(idiomaUsuario,"gestionEstadisticas.tablaEstadisticas.col1")%>");
            tablaEstadisticas.addColumna('75','left',"<%=meLanbide53I18n.getMensaje(idiomaUsuario,"gestionEstadisticas.tablaEstadisticas.col2")%>");
            tablaEstadisticas.addColumna('90','center',"<%=meLanbide53I18n.getMensaje(idiomaUsuario,"gestionEstadisticas.tablaEstadisticas.col3")%>");
            tablaEstadisticas.addColumna('95','left',"<%=meLanbide53I18n.getMensaje(idiomaUsuario,"gestionEstadisticas.tablaEstadisticas.col4")%>");
            tablaEstadisticas.addColumna('50','center',"<%=meLanbide53I18n.getMensaje(idiomaUsuario,"gestionEstadisticas.tablaEstadisticas.col5")%>");
            tablaEstadisticas.addColumna('50','left',"<%=meLanbide53I18n.getMensaje(idiomaUsuario,"gestionEstadisticas.tablaEstadisticas.col6")%>");
            tablaEstadisticas.addColumna('130','left',"<%=meLanbide53I18n.getMensaje(idiomaUsuario,"gestionEstadisticas.tablaEstadisticas.col7")%>");
            tablaEstadisticas.addColumna('185','left',"<%=meLanbide53I18n.getMensaje(idiomaUsuario,"gestionEstadisticas.tablaEstadisticas.col8")%>");

            tablaEstadisticas.displayCabecera=true;
            tablaEstadisticas.height = '300';
            
        }
    
        function pulsarExportarEstadisticas() {         
            if (validarFiltrosBusqueda()) {
		try{
			var url = '<%=request.getContextPath()%>/PeticionModuloIntegracion.do';
                        var parametros = "?tarea=preparar&modulo=MELANBIDE53&operacion=generarExcel&tipo=0"
                                        +'&Fecha_RegbusqE='+document.getElementById("meLanbide53Fecha_RegbusqE").value
                                        +'&Fecha_RegbusqF='+document.getElementById("meLanbide53Fecha_RegbusqF").value
                                        +'&ck_historico=' + prepararFiltroChecked('ck_historico')
                                        +'&numeroExpediente='+document.getElementById("numeroExpediente_busq").value 
                                        +'&idprocedimiento='+document.getElementById("idprocedimiento_busq").value 
                                        +'&clave='+document.getElementById("clave_busq").value 
                                        +'&codResultado='+document.getElementById("codListaResultado").value                                
                                        +'&error='+document.getElementById("error_busq").value                                 
                                        +'&evento='+escape(document.getElementById("codListaEvento").value)                                 
                        window.open(url+parametros, "_blank");                                              
                    }catch(err){
			alert('<%=meLanbide53I18n.getMensaje(idiomaUsuario, "error.estad.abrirDocumento")%>');
                    }
            }
            else {
		jsp_alerta('A',mensajeValidacion);
            }            
        }
   
        function pulsarConsultarEstadisticas() {
            if (tablaEstadisticas.selectedIndex != -1) {
                var control = new Date();
                lanzarPopUpModal('<%=request.getContextPath()%>/PeticionModuloIntegracion.do?tarea=preparar&modulo=MELANBIDE53&operacion=cargarConsultarEstadistica&tipo=0&idEstadistica=' + listaEstadisticas[tablaEstadisticas.selectedIndex][0] + '&control=' + control.getTime(), 780, 820, 'no', 'no');					
            }
            else {
                jsp_alerta('A', '<%=meLanbide53I18n.getMensaje(idiomaUsuario, "msg.msjNoSelecFila")%>');
            }
        }     

        function prepararFiltroChecked(idElement){
            if(document.getElementById(idElement)!=null && document.getElementById(idElement).checked){
                return '<%=ConstantesMeLanbide53.BOOLEAN_TRUE%>'; 
            }else 
                return '<%=ConstantesMeLanbide53.BOOLEAN_FALSE%>'; ;
        }
    </script>     

<body onload="javascript:{pleaseWait('off')}">
    <jsp:include page="/jsp/hidepage.jsp" flush="true">
        <jsp:param name='cargaDatos' value='<%=descriptor.getDescripcion("msjCargDatos")%>'/>
    </jsp:include>      
<form>      
    <div style="height:50px; width: 100%;">
        <table width="100%" style="height: 100%;" cellpadding="0px" cellspacing="0px" border="0px">
            <tr>
                <td class="txttitblanco"><%=meLanbide53I18n.getMensaje(idiomaUsuario,"label.tituloPrincipalEstadisticas")%> </td>   
            </tr>
        </table>
    </div> 
    <div class="tab-page" id="tabPage354" style="height:520px; width: 100%;">
        <div>        
            <div class="contenidoPantalla">               
                <div style="clear: both;">
                    <div id="div_label" class="sub3titulo" style=" text-align: center;">
                        <label class="sub3titulo" style=" text-align: center; position: relative;" ><%=meLanbide53I18n.getMensaje(idiomaUsuario, "legend.titulo.busquedaEst")%></label>
                    </div>                      
                    <div>
                        <table id="tablaBusqueda">                       
                            <tbody>                         
                                <tr>
                                    <td style="width: 15%">
                                        <label class="etiqueta">
                                            <%=meLanbide53I18n.getMensaje(idiomaUsuario,"label.estad.fecharegistro")%>
                                        </label>                            
                                    </td>
                                    <td>
                                        <table style="width: 50%">
                                            <tbody>                                                  
                                                <tr>
                                                    <td>
                                                        <div style="float: left;">
                                                            <input type="text" class="inputTxtFecha" 
                                                                   id="meLanbide53Fecha_RegbusqE" name="meLanbide53Fecha_RegbusqE"
                                                                   maxlength="10"  size="10"
                                                                   value=""
                                                                   onkeyup = "return SoloCaracteresFechaLanbide(this);"
                                                                   onblur = "javascript:return comprobarFechaLanbide(this);"
                                                                   onfocus="javascript:this.select();"/>
                                                            <A href="javascript:calClick(event);return false;" onClick="mostrarCalFechaRegE(event);return false;" style="text-decoration:none;">   
                                                                <IMG style="border: 0px solid none" height="17" id="calMeLanbide53Fecha_RegbusqE" name="calMeLanbide53Fecha_RegbusqE" border="0"  
                                                                     src="<c:url value='/images/calendario/icono.gif'/>" > <!--width="25"-->
                                                            </A>
                                                        </div>
                                                    </td>
                                                    <td style="text-align: center;">
                                                        <label class="etiqueta" style="float: left; text-align: center; ">
                                                            <%=meLanbide53I18n.getMensaje(idiomaUsuario,"label.busqueda.hasta")%>
                                                        </label> 
                                                    </td>
                                                    <td>
                                                        <div style="float: left;">
                                                            <input type="text" class="inputTxtFecha" 
                                                                   id="meLanbide53Fecha_RegbusqF" name="meLanbide53Fecha_RegbusqF"
                                                                   maxlength="10"  size="10"
                                                                   value=""
                                                                   onkeyup = "return SoloCaracteresFechaLanbide(this);"
                                                                   onblur = "javascript:return comprobarFechaLanbide(this);"
                                                                   onfocus="javascript:this.select();"/>
                                                            <A href="javascript:calClick(event);return false;" onClick="mostrarCalFechaRegF(event);return false;" style="text-decoration:none;">   
                                                                <IMG style="border: 0px solid none" height="17" id="calMeLanbide53Fecha_RegbusqF" name="calMeLanbide53Fecha_RegbusqF" border="0" 
                                                                     src="<c:url value='/images/calendario/icono.gif'/>" > <!--width="25"-->
                                                            </A>
                                                        </div>
                                                    </td>
                                                    <td>
                                                        <label class="etiqueta"><%=meLanbide53I18n.getMensaje(idiomaUsuario,"label.estad.historico")%></label>
                                                        <input id="ck_historico" name="ck_historico" type="checkbox">
                                                    </td>                                                                                     
                                                </tr>
                                            </tbody>
                                        </table>
                                    </td>                                 
                                </tr>                                                     
                                <tr>                                    
                                    <td style="width: 15%">
                                        <label class="etiqueta">
                                            <%=meLanbide53I18n.getMensaje(idiomaUsuario,"label.estad.expediente")%>
                                        </label>
                                    </td>
                                    <td>
                                        <input id="numeroExpediente_busq" name="numeroExpediente_busq" type="text" class="inputTextoM53" size="32" maxlength="100">
                                    </td>
                                </tr>                                
                                <tr>
                                    <td style="width: 15%">
                                        <label class="etiqueta">
                                            <%=meLanbide53I18n.getMensaje(idiomaUsuario,"label.estad.procedimiento")%>
                                        </label>                            
                                    </td>
                                    <td>
                                        <input id="idprocedimiento_busq" name="idprocedimiento_busq" type="text" class="inputTextoM53" size="25" maxlength="25">
                                    </td>
                                </tr>
                                <tr>
                                    <td style="width: 15%">
                                        <label class="etiqueta">
                                            <%=meLanbide53I18n.getMensaje(idiomaUsuario,"label.estad.OIDsolicitud")%>
                                        </label>
                                    </td>
                                    <td>
                                        <input id="clave_busq" name="clave_busq" type="text" class="inputTextoM53" size="32" maxlength="32">
                                    </td>
                                </tr> 
                                <tr>
                                    <!-- desplegable de resultado -->
                                     <td style="margin: 10px;">
                                        <label class="etiqueta" style="text-align: center; position: relative; margin-right: 30px;">
                                            <%=meLanbide53I18n.getMensaje(idiomaUsuario,"label.estad.resultado")%>
                                        </label>
                                     </td>
                                     <td>
                                        <input type="text" name="codListaResultado" id="codListaResultado" size="3" class="inputTexto" readonly="true" hidden="true" value="" />
                                        <input type="text" name="descListaResultado" id="descListaResultado" size="5" class="inputTexto" readonly="true" value="" />
                                        <a href="" id="anchorListaResultado" name="anchorListaResultado">
                                            <img src="<%=request.getContextPath()%>/images/listas/botondesplegable.gif" id="botonResultado"
                                                 name="botonResultado" height="14" width="14" border="0" style="cursor:pointer;">
                                        </a>
                                    </td> 
                                </tr> 
                                <tr>
                                    <td style="width: 15%">
                                        <label class="etiqueta">
                                            <%=meLanbide53I18n.getMensaje(idiomaUsuario,"label.estad.error")%>
                                        </label>
                                    </td>
                                    <td>
                                        <input id="error_busq" name="error_busq" type="text" class="inputTextoM53" size="10" maxlength="10">
                                    </td>                                 
                                </tr>
                                <tr>
                                    <!-- desplegable de Evento -->
                                     <td style="margin: 10px;">
                                        <label class="etiqueta" style="text-align: center; position: relative; margin-right: 30px;">
                                            <%=meLanbide53I18n.getMensaje(idiomaUsuario,"label.estad.evento")%>
                                        </label>
                                     </td>
                                     <td>
                                        <input type="text" name="codListaEvento" id="codListaEvento" size="1" class="inputTexto" readonly="true" hidden="true" value="" />
                                        <input type="text" name="descListaEvento" id="descListaEvento" size="60" class="inputTexto" readonly="true" value="" />
                                        <a href="" id="anchorListaEvento" name="anchorListaEvento">
                                            <img src="<%=request.getContextPath()%>/images/listas/botondesplegable.gif" id="botonEvento"
                                                 name="botonEvento" height="14" width="14" border="0" style="cursor:pointer;">
                                        </a> 
                                     </td>
                                </tr>                                 
                            </tbody>
                        </table>                                  
                    </div>                                                                   
                </div>
                <div style="text-align: center;">
                    <input type="button" id="btnLimpiar" name="btnLimpiar" class="botonGeneral"  value="<%=meLanbide53I18n.getMensaje(idiomaUsuario, "btn.limpiar")%>" onclick="pulsarLimpiar();">
                    <input type="button" id="btnBuscar" name="btnBuscar" class="botonGeneral"  value="<%=meLanbide53I18n.getMensaje(idiomaUsuario, "btn.filtrar")%>" onclick="pulsarFiltrarEstadisticas();">
                </div>                                                                            
                <div style="clear: both;">
                    </br>
                    <div>
                        <div id="div_label" class="sub3titulo" style=" text-align: center; width: 97% ">
                            <label class="sub3titulo" style=" text-align: center; width: 100%; position: relative;" ><%=meLanbide53I18n.getMensaje(idiomaUsuario, "legend.titulo.tablaEsta")%></label>
                        </div>     
                        <div id="divGeneral" >     
                            <div id="listaEstadisticas" align="center" style="width: 99% "></div>
                        </div>     
                        <div class="botonera" style="text-align: center;">
                            <input type="button" id="btnConsultar" name="btnConsultar"   class="botonGeneral" value="<%=meLanbide53I18n.getMensaje(idiomaUsuario, "btn.detalle")%>" onclick="pulsarConsultarEstadisticas();">  
                            <input type="button" id="btnExportar" name="btnExportar"   class="botonMasLargo" value="<%=meLanbide53I18n.getMensaje(idiomaUsuario, "btn.Exportar")%>" onclick="pulsarExportarEstadisticas();">
                            <input type="button" id="btnObtenerDocs" name="btnObtenerDocs"   class="botonMasLargo" value="<%=meLanbide53I18n.getMensaje(idiomaUsuario, "btn.obtenerDocs")%>" onclick="pulsarObtenerDocs();">
                            <input type="button" id="btnRetramitacion" name="btnRetramitacion"   class="botonMasLargo" value="<%=meLanbide53I18n.getMensaje(idiomaUsuario, "btn.Retramitacion")%>" onclick="pulsarRetramitacion();">
                        </div>                        
                    </div>                       
                </div>
            </div> 
        </div>                          
    </div>               
    <script  type="text/javascript">
        //Tabla Estadisticas
        pleaseWait('on');
        
        var mensajeValidacion = "";
        
        var tablaEstadisticas;
        var listaEstadisticas = new Array();
        var listaEstadisticasTabla = new Array();
      
        crearTabla();
        <% 
            EstadisticasVO objectVO = null;
            List<EstadisticasVO> List = null;

            if(request.getAttribute("listaEstadisticas")!=null){
                List = (List<EstadisticasVO>)request.getAttribute("listaEstadisticas");
            }	
            if (List!= null && List.size() >0){             
                 
                for (int indice=0;indice<List.size();indice++)
                {
                    objectVO = List.get(indice);                
                %>
                    listaEstadisticas[<%=indice%>] = ['<%=objectVO.getId()%>','<%=objectVO.getFechaRegistro()%>','<%=objectVO.getIdProcedimiento()!=null?objectVO.getIdProcedimiento():""%>','<%=objectVO.getNumeroExpediente()!= null?objectVO.getNumeroExpediente():""%>','<%=objectVO.getClave()!=null?objectVO.getClave():""%>','<%=objectVO.getResultado()!=null?objectVO.getResultado():""%>','<%=objectVO.getIdError()!=null?objectVO.getIdError():""%>','<%=objectVO.getEvento()!= null?objectVO.getEvento():""%>','<%=objectVO.getObservaciones()!= null?objectVO.getObservaciones():""%>'];
                    listaEstadisticasTabla[<%=indice%>] = ['<%=objectVO.getId()%>','<%=objectVO.getFechaRegistro()%>','<%=objectVO.getIdProcedimiento()!=null?objectVO.getIdProcedimiento():""%>','<%=objectVO.getNumeroExpediente()!= null?objectVO.getNumeroExpediente():""%>','<%=objectVO.getClave()!=null?objectVO.getClave():""%>','<%=objectVO.getResultado()!=null?objectVO.getResultado():""%>','<%=objectVO.getIdError()!=null?objectVO.getIdError():""%>','<%=objectVO.getEvento()!= null?objectVO.getEvento():""%>','<%=objectVO.getObservaciones()!= null?objectVO.getObservaciones():""%>'];
                <%
                }// for
            }// if
        %>

        tablaEstadisticas.lineas=listaEstadisticas;      
        tablaEstadisticas.displayTablaConTooltips(listaEstadisticasTabla); 
        if(navigator.appName.indexOf("Internet Explorer")!=-1){
            try{
                var div = document.getElementById('listaEstadisticas');
                div.children[0].children[0].children[0].children[1].style.width = '100%';
                div.children[0].children[1].style.width = '100%';
            }
            catch(err){
            }
        }

        // Cargar el combo Resultado        
        var listaCodigosResultado = new Array();
        var listaDescripcionesResultado = new Array();

        listaCodigosResultado[0] = "";
        listaDescripcionesResultado[0] = "";
            
        listaCodigosResultado[0] = '<%=ConstantesMeLanbide53.OK%>';
        listaCodigosResultado[1] = '<%=ConstantesMeLanbide53.KO%>';
        listaDescripcionesResultado[0]= "OK";
        listaDescripcionesResultado[1]= "KO";            
            
        var comboListaResultado = new Combo("ListaResultado");
        comboListaResultado.addItems(listaCodigosResultado, listaDescripcionesResultado);
        
        // Cargar el combo Evento        
        var listaCodigosEvento = new Array();
        var listaDescripcionesEvento = new Array();

        listaCodigosEvento[0] = "";
        listaDescripcionesEvento[0] = "";
            
        listaCodigosEvento[0] = '<%=ConstantesMeLanbide53.PRESENTACION_SOLICITUD%>';
        listaCodigosEvento[1] = '<%=ConstantesMeLanbide53.APORTACION_DOCUMENTOS_2%>';
        listaCodigosEvento[2] = '<%=ConstantesMeLanbide53.APORTACION_DOCUMENTOS_3%>';
        listaCodigosEvento[3] = '<%=ConstantesMeLanbide53.APORTACION_DOCUMENTOS_5%>';
        listaCodigosEvento[4] = '<%=ConstantesMeLanbide53.APORTACION_DOCUMENTOS_6%>';
        listaCodigosEvento[5] = '<%=ConstantesMeLanbide53.APORTACION_DOCUMENTOS_7%>';
        listaCodigosEvento[6] = '<%=ConstantesMeLanbide53.APORTACION_DOCUMENTOS_9%>';
        listaCodigosEvento[7] = '<%=ConstantesMeLanbide53.APORTACION_DOCUMENTOS_13%>';
        listaCodigosEvento[8] = '<%=ConstantesMeLanbide53.APORTACION_DOCUMENTOS_16%>';
        listaCodigosEvento[9] = '<%=ConstantesMeLanbide53.APORTACION_DOCUMENTOS_17%>';
        listaCodigosEvento[10] = '<%=ConstantesMeLanbide53.APORTACION_DOCUMENTOS_18%>';
        listaCodigosEvento[11] = '<%=ConstantesMeLanbide53.LOCATOR_RESULT%>';
        listaCodigosEvento[12] = '<%=ConstantesMeLanbide53.PUBLISH_NOTIFICATION_DELIVERY_REQ_SUB%>';        
        listaCodigosEvento[13] = '<%=ConstantesMeLanbide53.PUBLISH_NOTIFICATION_DELIVERY_RESOL%>';
        listaCodigosEvento[14] = '<%=ConstantesMeLanbide53.PUBLISH_NOTIFICATION_DELIVERY_ACEPT%>';        
        listaCodigosEvento[15] = '<%=ConstantesMeLanbide53.PUBLISH_NOTIFICATION_DELIVERY_RESOL_RECUR%>';        
        listaCodigosEvento[16] = '<%=ConstantesMeLanbide53.TRANSFORMATION_RESULT%>';        
        listaCodigosEvento[17] = '<%=ConstantesMeLanbide53.MANAGE_NOTIFICATION_EXPIRATION%>';                 
        listaCodigosEvento[18] = '<%=ConstantesMeLanbide53.REGISTRATION_DELIVERY%>';
        listaCodigosEvento[19] = '<%=ConstantesMeLanbide53.R02_SIGN%>';
        
        listaDescripcionesEvento[0] = '<%=ConstantesMeLanbide53.PRESENTACION_SOLICITUD_TXT%>';
        listaDescripcionesEvento[1] = '<%=ConstantesMeLanbide53.APORTACION_DOCUMENTOS_2_TXT%>';
        listaDescripcionesEvento[2] = '<%=ConstantesMeLanbide53.APORTACION_DOCUMENTOS_3_TXT%>';
        listaDescripcionesEvento[3] = '<%=ConstantesMeLanbide53.APORTACION_DOCUMENTOS_5_TXT%>';
        listaDescripcionesEvento[4] = '<%=ConstantesMeLanbide53.APORTACION_DOCUMENTOS_6_TXT%>';
        listaDescripcionesEvento[5] = '<%=ConstantesMeLanbide53.APORTACION_DOCUMENTOS_7_TXT%>';
        listaDescripcionesEvento[6] = '<%=ConstantesMeLanbide53.APORTACION_DOCUMENTOS_9_TXT%>';
        listaDescripcionesEvento[7] = '<%=ConstantesMeLanbide53.APORTACION_DOCUMENTOS_13_TXT%>';
        listaDescripcionesEvento[8] = '<%=ConstantesMeLanbide53.APORTACION_DOCUMENTOS_16_TXT%>';
        listaDescripcionesEvento[9] = '<%=ConstantesMeLanbide53.APORTACION_DOCUMENTOS_17_TXT%>';
        listaDescripcionesEvento[10] = '<%=ConstantesMeLanbide53.APORTACION_DOCUMENTOS_18_TXT%>';
        listaDescripcionesEvento[11] = '<%=ConstantesMeLanbide53.LOCATOR_RESULT_TXT%>';
        listaDescripcionesEvento[12] = '<%=ConstantesMeLanbide53.PUBLISH_NOTIFICATION_DELIVERY_REQ_SUB_TXT%>';
        listaDescripcionesEvento[13] = '<%=ConstantesMeLanbide53.PUBLISH_NOTIFICATION_DELIVERY_RESOL_TXT%>'; 
        listaDescripcionesEvento[14] = '<%=ConstantesMeLanbide53.PUBLISH_NOTIFICATION_DELIVERY_ACEPT_TXT%>';        
        listaDescripcionesEvento[15] = '<%=ConstantesMeLanbide53.PUBLISH_NOTIFICATION_DELIVERY_RESOL_RECUR_TXT%>';        
        listaDescripcionesEvento[16] = '<%=ConstantesMeLanbide53.TRANSFORMATION_RESULT_TXT%>';        
        listaDescripcionesEvento[17] = '<%=ConstantesMeLanbide53.MANAGE_NOTIFICATION_EXPIRATION_TXT%>';   
        listaDescripcionesEvento[18] = '<%=ConstantesMeLanbide53.REGISTRATION_DELIVERY_TXT%>';   
        listaDescripcionesEvento[19] = '<%=ConstantesMeLanbide53.R02_SIGN_TXT%>';
        
        var comboListaEvento = new Combo("ListaEvento");
        comboListaEvento.addItems(listaCodigosEvento, listaDescripcionesEvento);
        
       //--------------------------------- 
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
                
        function mostrarCalFechaRegE(evento) { 
            if(window.event) 
                evento = window.event;
            if (document.getElementById("calMeLanbide53Fecha_RegbusqE").src.indexOf("icono.gif") != -1 )
                showCalendar('forms[0]','meLanbide53Fecha_RegbusqE',null,null,null,'','calMeLanbide53Fecha_RegbusqE','',null,null,null,null,null,null,null, null,evento);        
        }//mostrarCalFechaRegE

        function mostrarCalFechaRegF(evento) { 
            if(window.event) 
                evento = window.event;
            if (document.getElementById("calMeLanbide53Fecha_RegbusqF").src.indexOf("icono.gif") != -1 )
                showCalendar('forms[0]','meLanbide53Fecha_RegbusqF',null,null,null,'','calMeLanbide53Fecha_RegbusqF','',null,null,null,null,null,null,null, null,evento);        
        }//mostrarCalFechaRegF       
        
        function validarFiltrosBusqueda(){
            if(document.getElementById("meLanbide53Fecha_RegbusqE").value!="" && document.getElementById("meLanbide53Fecha_RegbusqF").value!=""){
                var fechaentrada = document.getElementById("meLanbide53Fecha_RegbusqE").value.split("/");
                var fechasalida = document.getElementById("meLanbide53Fecha_RegbusqF").value.split("/");
                fechaentrada=fechaentrada[2]+fechaentrada[1]+fechaentrada[0];
                fechasalida=fechasalida[2]+fechasalida[1]+fechasalida[0];
                if(fechaentrada>fechasalida){
                    mensajeValidacion='<%=meLanbide53I18n.getMensaje(idiomaUsuario, "msg.busqueda.errorintervalosfechaReg")%>';
                    document.getElementById("meLanbide53Fecha_RegbusqE").select();
                    return false;
                }   
            }
            return true;
        }        
        
        //Función para limpiar
        function pulsarLimpiar(){
            document.getElementById("meLanbide53Fecha_RegbusqE").value='';
            document.getElementById("meLanbide53Fecha_RegbusqF").value='';
            document.getElementById("ck_historico").checked=false;
            document.getElementById("numeroExpediente_busq").value='';
            document.getElementById("idprocedimiento_busq").value='';
            document.getElementById("clave_busq").value='';
            document.getElementById("codListaResultado").value='';            
            document.getElementById("descListaResultado").value='';
            document.getElementById("error_busq").value='';
            document.getElementById("codListaEvento").value='';             
            document.getElementById("descListaEvento").value='';            
        }

        //Función para filtrar 
        function pulsarFiltrarEstadisticas(){ 
            if (validarFiltrosBusqueda()) {                 
                var ajax = getXMLHttpRequest();
                var nodos = null;
                var CONTEXT_PATH = '<%=request.getContextPath()%>';
                var url = CONTEXT_PATH + "/PeticionModuloIntegracion.do";
                var parametros = "";
                var control = new Date();
                var listaEstad = new Array();
                parametros = 'tarea=preparar&modulo=MELANBIDE53&operacion=cargarFiltrarEstadisticas&tipo=0'
                            +'&Fecha_RegbusqE='+document.getElementById("meLanbide53Fecha_RegbusqE").value
                            +'&Fecha_RegbusqF='+document.getElementById("meLanbide53Fecha_RegbusqF").value                
                            +'&ck_historico=' + prepararFiltroChecked('ck_historico')
                            +'&numeroExpediente='+document.getElementById("numeroExpediente_busq").value 
                            +'&idprocedimiento='+document.getElementById("idprocedimiento_busq").value 
                            +'&clave='+document.getElementById("clave_busq").value   
                            +'&codResultado='+document.getElementById("codListaResultado").value   
                            +'&error='+document.getElementById("error_busq").value      
                            +'&evento='+escape(document.getElementById("codListaEvento").value)                    
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
                        }
                    }
                    nodos = xmlDoc.getElementsByTagName("RESPUESTA");
                    listaEstad = extraerListadoEstad(nodos);
                    var codigoOperacion = listaEstad[0];
                    if(codigoOperacion=="0"){ 
                        pleaseWait('on');                        
                        recargarTabla(listaEstad);                                              
                        pleaseWait('off');                        
                    }else{                                
                        jsp_alerta("A",'<%=meLanbide53I18n.getMensaje(idiomaUsuario,"error.errorBD")%>');
                    }//if(
                }
                catch(Err){                 
                    jsp_alerta("A",'<%=meLanbide53I18n.getMensaje(idiomaUsuario,"error.errorProcesarRespuesta")%>');
                }//try-catch
            }
            else {     
		jsp_alerta('A',mensajeValidacion);
            }              
        }
            
            function extraerListadoEstad(nodos){
                    var elemento = nodos[0];
                    var hijos = elemento.childNodes;
                    var codigoOperacion = null;
                    var listaEstad = new Array();
                    var fila = new Array();
                    var nodoFila;
                    var hijosFila;
                    var nodoCampo;
                    var j;

                    for(j=0;hijos!=null && j<hijos.length;j++){
                        if(hijos[j].nodeName=="CODIGO_OPERACION"){                            
                            codigoOperacion = hijos[j].childNodes[0].nodeValue;
                            listaEstad[j] = codigoOperacion;
                        }                   

                        else if(hijos[j].nodeName=="FILA"){
                            nodoFila = hijos[j];
                            hijosFila = nodoFila.childNodes;
                            for(var cont = 0; cont < hijosFila.length; cont++){
                                if(hijosFila[cont].nodeName=="ID"){
                                    nodoCampo = hijosFila[cont];
                                    if(nodoCampo.childNodes.length > 0){
                                        fila[0] = nodoCampo.childNodes[0].nodeValue;
                                    }
                                    else{
                                        fila[0] = ' ';
                                    }
                                }
                                else if(hijosFila[cont].nodeName=="FEC_REGISTRO"){
                                    nodoCampo = hijosFila[cont];
                                    if(nodoCampo.childNodes.length > 0){
                                        fila[1] = nodoCampo.childNodes[0].nodeValue;
                                    }
                                    else{
                                        fila[1] = ' ';
                                    }
                                }
                                else if(hijosFila[cont].nodeName=="ID_PROCEDIMIENTO"){
                                    nodoCampo = hijosFila[cont];
                                    if(nodoCampo.childNodes.length > 0){
                                        fila[2] = nodoCampo.childNodes[0].nodeValue;
                                    }
                                    else{
                                        fila[2] = ' ';
                                    }
                                }
                                else if(hijosFila[cont].nodeName=="NUM_EXPEDIENTE"){
                                    nodoCampo = hijosFila[cont];
                                    if(nodoCampo.childNodes.length > 0){
                                        fila[3] = nodoCampo.childNodes[0].nodeValue;
                                    }
                                    else{
                                        fila[3] = ' ';
                                    }
                                }       
                                else if(hijosFila[cont].nodeName=="OID_SOLICITUD"){
                                    nodoCampo = hijosFila[cont];
                                    if(nodoCampo.childNodes.length > 0){
                                        fila[4] = nodoCampo.childNodes[0].nodeValue;
                                    }
                                    else{
                                        fila[4] = ' ';
                                    }
                                }    
                                else if(hijosFila[cont].nodeName=="RESULTADO"){
                                    nodoCampo = hijosFila[cont];
                                    if(nodoCampo.childNodes.length > 0){
                                        fila[5] = nodoCampo.childNodes[0].nodeValue;
                                    }
                                    else{
                                        fila[5] = ' ';
                                    }
                                }     
                                else if(hijosFila[cont].nodeName=="ID_ERROR"){
                                    nodoCampo = hijosFila[cont];
                                    if(nodoCampo.childNodes.length > 0){
                                        fila[6] = nodoCampo.childNodes[0].nodeValue;
                                    }
                                    else{
                                        fila[6] = ' ';
                                    }
                                } 
                                else if(hijosFila[cont].nodeName=="EVENTO"){
                                    nodoCampo = hijosFila[cont];
                                    if(nodoCampo.childNodes.length > 0){
                                        fila[7] = nodoCampo.childNodes[0].nodeValue;
                                    }
                                    else{
                                        fila[7] = ' ';
                                    }
                                } 
                                else if(hijosFila[cont].nodeName=="OBSERVACIONES"){
                                    nodoCampo = hijosFila[cont];
                                    if(nodoCampo.childNodes.length > 0){
                                        fila[8] = nodoCampo.childNodes[0].nodeValue;
                                    }
                                    else{
                                        fila[8] = ' ';
                                    }
                                }                                 
                            }
                            listaEstad[j] = fila;
                            fila = new Array();   
                        }
                } 
                return listaEstad;
            }
            
            function oidEsOK(oidDocumento) {
                var tieneOK = 0;
                
                var ajax = getXMLHttpRequest();
                var nodos = null;
                var CONTEXT_PATH = '<%=request.getContextPath()%>';
                var url = CONTEXT_PATH + "/PeticionModuloIntegracion.do";
                var parametros = "";
                var control = new Date();
                parametros = 'tarea=preparar&modulo=MELANBIDE53&operacion=oidEsOK&tipo=0&oidDocumento=' + oidDocumento + '&control=' + control.getTime();
                try {
                    ajax.open("POST", url, false);
                    ajax.setRequestHeader("Content-Type", "application/x-www-form-urlencoded; charset=ISO-8859-1");
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
                        }//if(navigator.appName.indexOf("Internet Explorer")!=-1)
                    }//if (ajax.readyState==4 && ajax.status==200)
                    nodos = xmlDoc.getElementsByTagName("RESPUESTA");

                    var elemento = nodos[0];
                    var hijos = elemento.childNodes;
                    var codigoOperacion = null;
                    for (j = 0; hijos != null && j < hijos.length; j++) {
                        if (hijos[j].nodeName == "CODIGO_OPERACION") {
                            codigoOperacion = hijos[j].childNodes[0].nodeValue;
                        }//if(hijos[j].nodeName=="CODIGO_OPERACION")   
                        if (hijos[j].nodeName == "ESOK") {
                            tieneOK = hijos[j].childNodes[0].nodeValue;
                        }//if(hijos[j].nodeName=="CODIGO_OPERACION")
                    }//for(j=0;hijos!=null && j<hijos.length;j++)

                    if (codigoOperacion == "0") {
                    } else if (codigoOperacion == "1") {
                        jsp_alerta("A", '<%=meLanbide53I18n.getMensaje(idiomaUsuario,"error.errorBD")%>');
                    } else if (codigoOperacion == "2") {
                        jsp_alerta("A", 'Error en la consulta de OK de oid en Estadísticas');
                    } else if (codigoOperacion == "3") {
                        jsp_alerta("A", '<%=meLanbide53I18n.getMensaje(idiomaUsuario,"error.pasoParametros")%>');
                    } else {
                        jsp_alerta("A", '<%=meLanbide53I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                    }//if(
                } catch (Err) {
                    jsp_alerta("A", '<%=meLanbide53I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                }//try-catch
                
                return tieneOK;
            }
            
            function getRegistroDeOid(oidDocumento) {
                var registro = '';
                
                var ajax = getXMLHttpRequest();
                var nodos = null;
                var CONTEXT_PATH = '<%=request.getContextPath()%>';
                var url = CONTEXT_PATH + "/PeticionModuloIntegracion.do";
                var parametros = "";
                var control = new Date();
                parametros = 'tarea=preparar&modulo=MELANBIDE53&operacion=getRegistroDeOid&tipo=0&oidDocumento=' + oidDocumento + '&control=' + control.getTime();
                try {
                    ajax.open("POST", url, false);
                    ajax.setRequestHeader("Content-Type", "application/x-www-form-urlencoded; charset=ISO-8859-1");
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
                        }//if(navigator.appName.indexOf("Internet Explorer")!=-1)
                    }//if (ajax.readyState==4 && ajax.status==200)
                    nodos = xmlDoc.getElementsByTagName("RESPUESTA");

                    var elemento = nodos[0];
                    var hijos = elemento.childNodes;
                    var codigoOperacion = null;
                    for (j = 0; hijos != null && j < hijos.length; j++) {
                        if (hijos[j].nodeName == "CODIGO_OPERACION") {
                            codigoOperacion = hijos[j].childNodes[0].nodeValue;
                        }   
                        if (hijos[j].nodeName == "REGISTRO") {
                            registro = hijos[j].childNodes[0].nodeValue;
                        }
                    }

                    if (codigoOperacion == "0") {
                    } else if (codigoOperacion == "1") {
                        jsp_alerta("A", '<%=meLanbide53I18n.getMensaje(idiomaUsuario,"error.errorBD")%>');
                    } else if (codigoOperacion == "2") {
                        jsp_alerta("A", 'Error en la consulta de oid en Registro');
                    } else if (codigoOperacion == "3") {
                        jsp_alerta("A", '<%=meLanbide53I18n.getMensaje(idiomaUsuario,"error.pasoParametros")%>');
                    } else {
                        jsp_alerta("A", '<%=meLanbide53I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                    }//if(
                } catch (Err) {
                    jsp_alerta("A", '<%=meLanbide53I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                }//try-catch
                
                return registro;
            }
            
            function retardo(ms) {
                var cur_d = new Date();
                var cur_ticks = cur_d.getTime();
                var ms_passed = 0;
                while(ms_passed < ms) {
                    var d = new Date();
                    var ticks = d.getTime();
                    ms_passed = ticks - cur_ticks;
                }
            }
            
            function pulsarRetramitacion() { 
                deshabilitarBoton('#btnRetramitacion');

                //getSeleccionados();

                if (tablaEstadisticas.selectedIndex != -1) {
                //if(listaIdRegistroSeleccionados.length == 1){
                    var control = new Date();
                    //var listaEstad = new Array();
                    var idEstadistica = listaEstadisticas[tablaEstadisticas.selectedIndex][0];//id que pasaremos para la retramitación
                    var oidDocumento = listaEstadisticas[tablaEstadisticas.selectedIndex][4];//oid de la solicitud o del documento (ej.:09f42404807e78f8), comprobar que no esté en R_RED (que no exista registro)
                    var resultadoTeletram = listaEstadisticas[tablaEstadisticas.selectedIndex][5];//debe ser 'KO' y no haber ningún 'OK' para ese oidSolicitud
                    //var idError = listaEstadisticas[tablaEstadisticas.selectedIndex][6];
                    //var eventoTeletram = listaEstadisticas[tablaEstadisticas.selectedIndex][7];//ni 'LOCATOR_RESULT' ni 'TRANSFORMATION_RESULT'

                    //ese oid de documento tiene 'OK' (ESTADISTICAS)
                    var tieneOK = oidEsOK(oidDocumento);

                    //ese oid de documento está en registro ? (R_RED)
                    var registro = getRegistroDeOid(oidDocumento);
                    
                    //se valida si es 'KO', si ese oid no tiene ningún 'OK' y el documento no está ya en un Registro
                    if (resultadoTeletram == 'KO' && tieneOK == 0 && (registro == 'null' || registro == '' || registro == 'no_existe')) {                       
                        var ajax = getXMLHttpRequest();
                        var nodos = null;
                        var CONTEXT_PATH = '<%=request.getContextPath()%>';
                        var url = CONTEXT_PATH + "/PeticionModuloIntegracion.do";
                        var parametros = "";
                        var control = new Date();
                        parametros = 'tarea=preparar&modulo=MELANBIDE53&operacion=retramitarEstadisticas&tipo=0&oid=' + oidDocumento + '&id=' + idEstadistica + '&control=' + control.getTime();
                        try {
                            ajax.open("POST", url, false);
                            ajax.setRequestHeader("Content-Type", "application/x-www-form-urlencoded; charset=ISO-8859-1");
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
                                }//if(navigator.appName.indexOf("Internet Explorer")!=-1)
                            }//if (ajax.readyState==4 && ajax.status==200)
                            nodos = xmlDoc.getElementsByTagName("RESPUESTA");
                            
                            //listaEstad = extraerListadoEstad(nodos);
                            //var codigoOperacion = listaEstad[0];
                            
                            var elemento = nodos[0];
                            var hijos = elemento.childNodes;
                            var resultado = "";
                            var codigoOperacion = null;
                            for (j = 0; hijos != null && j < hijos.length; j++) {
                                if (hijos[j].nodeName == "CODIGO_OPERACION") {
                                    codigoOperacion = hijos[j].childNodes[0].nodeValue;
                                }//if(hijos[j].nodeName=="CODIGO_OPERACION")   
                                if (hijos[j].nodeName == "DES_ERROR") {
                                    resultado = hijos[j].childNodes[0].nodeValue;
                                    //alert("Se ha producido un error en la retramitación. "+resultado);
                                }//if(hijos[j].nodeName=="CODIGO_OPERACION")
                            }//for(j=0;hijos!=null && j<hijos.length;j++)
                            
                            if (codigoOperacion == "0") {
                                if (resultado == 'OK') {
                                    var registroCreado = 'no_existe';

                                    //chequeamos si crea registro cada 2 seg durante 30 seg máximo
                                    var i = 1;
                                    while (registroCreado == 'no_existe' && i < 16) {
                                        retardo(2000);
                                        registroCreado = getRegistroDeOid(oidDocumento);
                                        i++;
                                    }

                                    if (registroCreado != 'null' && registroCreado != '' && registroCreado != 'no_existe') {
                                        jsp_alerta("A", 'Registro creado: ' + registroCreado);
                                    } else {
                                        jsp_alerta("A", 'No se ha creado Registro. Resultado:  ' + resultado);
                                    }
                                } else {
                                    jsp_alerta("A", 'No se ha creado Registro. Resultado:  ' + resultado);
                                }
                                //recargarTabla(listaEstadisticas);
                                pleaseWait('on');
                                pulsarFiltrarEstadisticas();
                                pleaseWait('off');
                            } else if (codigoOperacion == "1") {
                                jsp_alerta("A", '<%=meLanbide53I18n.getMensaje(idiomaUsuario,"error.errorBD")%>');
                            } else if (codigoOperacion == "2") {
                                jsp_alerta("A", 'La solicitud no es retramitable. No existe ningún evento de tramitación asociado a ella');
                            } else if (codigoOperacion == "3") {
                                jsp_alerta("A", '<%=meLanbide53I18n.getMensaje(idiomaUsuario,"error.pasoParametros")%>');
                            } else {
                                jsp_alerta("A", '<%=meLanbide53I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                                
                                //recargarTabla(listaEstadisticas);
                                pleaseWait('on');
                                pulsarFiltrarEstadisticas();
                                pleaseWait('off');
                            }//if(
                        } catch (Err) {
                            jsp_alerta("A", '<%=meLanbide53I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                        }//try-catch
                    } else {
                        if (resultadoTeletram != 'KO') {
                            jsp_alerta('A', 'No tiene resultado KO');
                        } else if (tieneOK == 1){
                            if (registro != 'null' && registro != '' && registro != 'no_existe'){
                                jsp_alerta('A', 'El documento ya tiene OK y al menos está en el registro ' + registro);
                            } else {
                                jsp_alerta('A', 'El documento ya tiene OK y no está en ningún registro');
                            }
                        } else {
                            jsp_alerta('A', 'El documento ya está en el registro ' + registro);
                        }
                    }
                } else {
                    jsp_alerta('A', '<%=meLanbide53I18n.getMensaje(idiomaUsuario, "msg.msjNoSelecFila")%>');
                }
            }
            
            function validarDatosDescargaDocumentos(oidSolicitud, procedimiento) {
                var result = true;
                var _oidSolicitud = oidSolicitud;
                var _procedimiento = procedimiento;
                if (_oidSolicitud != null && _oidSolicitud != "" && _oidSolicitud != "null")
                {
                    if (_procedimiento != null && _procedimiento != "" && _procedimiento != "null") {
                        return true;
                    } else {
                        mensajeValidacion = '<%=meLanbide53I18n.getMensaje(idiomaUsuario, "msg.obtenerdocumento.sinprocedimiento")%>';
                        return false;
                    }
                } else {
                    mensajeValidacion = '<%=meLanbide53I18n.getMensaje(idiomaUsuario, "msg.obtenerdocumento.sinoid")%>';
                    return false;
                }
                return result;
            }
            
            function pulsarObtenerDocs() {
                //getSeleccionados();

                if (tablaEstadisticas.selectedIndex != -1) {
                //if(listaIdRegistroSeleccionados.length == 1){
                    var control = new Date();
                    var oidSolicitud = listaEstadisticas[tablaEstadisticas.selectedIndex][4];
                    var codProcedimiento = listaEstadisticas[tablaEstadisticas.selectedIndex][2];
                    /*var oidSolicitud = listaOidSeleccionados[0];
                    var codProcedimiento = listaProcedimientoSeleccionados[0];*/
                    if (validarDatosDescargaDocumentos(oidSolicitud, codProcedimiento)) {
                        lanzarPopUpModal('<%=request.getContextPath()%>/PeticionModuloIntegracion.do?tarea=preparar&modulo=MELANBIDE53&operacion=cargarObtenerDocumentos&tipo=0&nuevo=0&oidSolicitud=' + oidSolicitud + '&codProcedimiento=' + codProcedimiento + '&control=' + control.getTime(), 420, 800, 'no', 'no', function (result) {});
                    } else {
                        jsp_alerta('A', mensajeValidacion);
                    }
                } else {
                    jsp_alerta('A', '<%=meLanbide53I18n.getMensaje(idiomaUsuario, "msg.msjNoSelecFila")%>');
                    //jsp_alerta("A",'<%=meLanbide53I18n.getMensaje(idiomaUsuario,"error.errorNoSeleccionadoUno")%>');
                }
            }
            
            function recargarTabla(result){
                var fila;
                //Tabla Estadisticas
                listaEstadisticas = new Array();
                listaEstadisticasTabla = new Array();

                for(var i = 1;i< result.length; i++){
                    fila = result[i];

                    listaEstadisticas[i-1] = [fila[0],fila[1],fila[2],fila[3],fila[4],fila[5],fila[6],fila[7],fila[8]];
                    listaEstadisticasTabla[i-1] = [fila[0],fila[1],fila[2],fila[3],fila[4],fila[5],fila[6],fila[7],fila[8]];
                }
                
                crearTabla();
                
                tablaEstadisticas.lineas=listaEstadisticas;
                tablaEstadisticas.displayTablaConTooltips(listaEstadisticasTabla);   
                if(navigator.appName.indexOf("Internet Explorer")!=-1){
                    try{
                        var div = document.getElementById('listaEstadisticas');
                        div.children[0].children[0].children[0].children[1].style.width = '100%';
                        div.children[0].children[1].style.width = '100%';
                    }
                    catch(err){
                    }
                }
            }
            
            $(document).ready(function(){
                //Deshabilitar botón Retramitación si la fila seleccionada es OK
                deshabilitarBoton('#btnRetramitacion');
                $('#listaEstadisticas').on('click', 'tr', function() {
                    if (tablaEstadisticas.selectedIndex != -1 && listaEstadisticas[tablaEstadisticas.selectedIndex][5] == 'KO') {
                        habilitarBoton('#btnRetramitacion');
                    } else {
                        deshabilitarBoton('#btnRetramitacion');
                    }
                });
            });
            
            function habilitarBoton(elemento){
                $(elemento).prop('disabled', false);
            }
            function deshabilitarBoton(elemento){
                $(elemento).prop('disabled', true);
            }
            
    </script>   
    <div id="popupcalendar" class="text"></div>          
</form>                       
</body>        
       

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
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide53.vo.RegistroErrorVO"%>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide53.util.ConstantesMeLanbide53" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide53.util.ConfigurationParameter" %>
<%@page import="java.util.ArrayList" %>
<%@page import="java.util.List" %>


<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@ taglib uri="/WEB-INF/tlds/fmt.tld" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/struts/struts-bean.tld"  prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts/struts-logic.tld"  prefix="logic" %>

<html>
    <head>
        <jsp:include page="/jsp/administracion/tpls/app-constants.jsp" />
        <meta http-equiv="Content-Type" content="text/html; charset=ISO_8859-1">
        <meta http-equiv="X-UA-Compatible" content="IE=Edge" />
        <title>Gestion Errores</title>

        <%
            UsuarioValueObject usuarioVO = new UsuarioValueObject();
            int idiomaUsuario = 1;
            int apl = 5;
            int numeroTotalRegistros = 0;
            String iniciaModulo = "";
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

        <!-- Estilos -->
        <link rel="StyleSheet" media="screen" type="text/css" href="<%=request.getContextPath()%><%=css%>">

        <jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
        <jsp:setProperty name="descriptor"  property="idi_cod" value= "<%=idiomaUsuario%>" />
        <jsp:setProperty name="descriptor"  property="apl_cod" value="<%=apl%>" />
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/general.js"></script>
        <script type="text/javascript"  src="<%=request.getContextPath()%>/scripts/domlay.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/tabpane.js"></script>
        <script type="text/javascript"  src="<%=request.getContextPath()%>/scripts/listas.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/validaciones.js"></script>
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/estilo.css"/>
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/extension/melanbide53/melanbide53.css"/>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/validaciones.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/listaComboBox.js"></script>
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

        <!-- Eventos onKeyPress compatibilidad firefox  -->
        <!--script type="text/javascript" src="http://code.jquery.com/jquery-1.4.4.js"></script-->
        <style>
            /*#cabecera11{display: none;}
            //#columna11{display: none;}

        </style>
        <script type="text/javascript">

            // Variable para desplegables Revisado y Notificado
            var comboListaOpcionRevisado;
            var comboListaOpcionNotificado;
            var comboListaOpcionRetramitado;
            

            var listaCodOpcionNotyRevi = new Array();
            var listaDesOpcionNotyRevi = new Array();
            // cargo de una vez los valores del desplegable para revisado y notificado
            listaCodOpcionNotyRevi[0] = '<%=ConstantesMeLanbide53.STANDFOR_TODOS_T%>';
            listaCodOpcionNotyRevi[1] = '<%=ConstantesMeLanbide53.STANDFOR_SI_S%>';
            listaCodOpcionNotyRevi[2] = '<%=ConstantesMeLanbide53.STANDFOR_NO_N%>';
            listaDesOpcionNotyRevi[0] = '<%=meLanbide53I18n.getMensaje(idiomaUsuario,"DESC_DESP_OPCION_T_TODOS")%>';
            listaDesOpcionNotyRevi[1] = '<%=meLanbide53I18n.getMensaje(idiomaUsuario,"DESC_DESP_OPCION_S_SI")%>';
            listaDesOpcionNotyRevi[2] = '<%=meLanbide53I18n.getMensaje(idiomaUsuario,"DESC_DESP_OPCION_N_NO")%>';

            var listaCodOpcionRetramite = new Array();
            var listaDesOpcionRetramite = new Array();
            listaCodOpcionRetramite[0] = '<%=ConstantesMeLanbide53.STANDFOR_SI_S%>';
            listaCodOpcionRetramite[1] = '<%=ConstantesMeLanbide53.STANDFOR_NO_N%>';
            listaDesOpcionRetramite[0] = '<%=meLanbide53I18n.getMensaje(idiomaUsuario,"DESC_DESP_OPCION_S_SI")%>';
            listaDesOpcionRetramite[1] = '<%=meLanbide53I18n.getMensaje(idiomaUsuario,"DESC_DESP_OPCION_N_NO")%>';

            function buscaCodigoRevisado(codRevisado) {
                comboListaOpcionRevisado.buscaCodigo(codRevisado);
            }
            function buscaCodigoNotificado(codNotificado) {
                comboListaOpcionNotificado.buscaCodigo(codNotificado);
            }
            function buscaCodigoRetramitado(codRetramitado) {
                comboListaOpcionRetramitado.buscaCodigo(codRetramitado);
            }

            function cargarDatosRevisado() {
                var codRevisadoSeleccionado = document.getElementById("codListaOpcionRevisado").value;
                buscaCodigoRevisado(codRevisadoSeleccionado);
            }
            function cargarDatosNotificado() {
                var codNotificadoSeleccionado = document.getElementById("codListaOpcionNotificado").value;
                buscaCodigoNotificado(codNotificadoSeleccionado);
            }
            function cargarDatosRetramitado() {
                var codRetramitadoSeleccionado = document.getElementById("codListaOpcionRetramitado").value;
                buscaCodigoRetramitado(codRetramitadoSeleccionado);
            }

            function crearTabla() {
                tablaErrores = new Tabla(true, '<%=descriptor.getDescripcion("buscar")%>', '<%=descriptor.getDescripcion("anterior")%>', '<%=descriptor.getDescripcion("siguiente")%>', '<%=descriptor.getDescripcion("mosFilasPag")%>', '<%=descriptor.getDescripcion("msgNoResultBusq")%>', '<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>', '<%=descriptor.getDescripcion("filtrDeTotal")%>', '<%=descriptor.getDescripcion("primero")%>', '<%=descriptor.getDescripcion("ultimo")%>', document.getElementById('listaErrores'), 4000);
                
                //tablaErrores.addColumna('120','center',"");
                
                tablaErrores.addColumna('120', 'center', "<%=meLanbide53I18n.getMensaje(idiomaUsuario,"gestionErrores.tablaErroresN.col0")%>");
                tablaErrores.addColumna('240', 'center', "<%=meLanbide53I18n.getMensaje(idiomaUsuario,"gestionErrores.tablaErrores.col1")%>");
                tablaErrores.addColumna('200', 'center', "<%=meLanbide53I18n.getMensaje(idiomaUsuario,"gestionErrores.tablaErroresN.col2")%>");
                tablaErrores.addColumna('200', 'center', "<%=meLanbide53I18n.getMensaje(idiomaUsuario,"gestionErrores.tablaErroresN.col3")%>");
                //tablaErrores.addColumna('220', 'center', "<%=meLanbide53I18n.getMensaje(idiomaUsuario,"gestionErrores.tablaErrores.col1")%>");
                //tablaErrores.addColumna('170', 'center', "<%=meLanbide53I18n.getMensaje(idiomaUsuario,"gestionErrores.tablaErroresN.col2")%>");
                //tablaErrores.addColumna('170', 'center', "<%=meLanbide53I18n.getMensaje(idiomaUsuario,"gestionErrores.tablaErroresN.col3")%>");
                tablaErrores.addColumna('300', 'left', "<%=meLanbide53I18n.getMensaje(idiomaUsuario,"gestionErrores.tablaErrores.col4")%>");
                tablaErrores.addColumna('200', 'left', "<%=meLanbide53I18n.getMensaje(idiomaUsuario,"gestionErrores.tablaErrores.col9")%>");
                tablaErrores.addColumna('250', 'left', "<%=meLanbide53I18n.getMensaje(idiomaUsuario,"gestionErrores.tablaErrores.col10")%>");
                tablaErrores.addColumna('300', 'left', "<%=meLanbide53I18n.getMensaje(idiomaUsuario,"gestionErrores.tablaErroresN.col5")%>");
                tablaErrores.addColumna('300', 'left', "<%=meLanbide53I18n.getMensaje(idiomaUsuario,"gestionErrores.tablaErrores.col6")%>");
                tablaErrores.addColumna('300', 'left', "<%=meLanbide53I18n.getMensaje(idiomaUsuario,"gestionErrores.tablaErrores.col7")%>");
                tablaErrores.addColumna('500', 'left', "<%=meLanbide53I18n.getMensaje(idiomaUsuario,"gestionErrores.tablaErrores.col8")%>");
                tablaErrores.addColumna('250', 'center', "<%=meLanbide53I18n.getMensaje(idiomaUsuario,"gestionErrores.tablaErroresN.col11")%>");
                tablaErrores.addColumna('220', 'center', "<%=meLanbide53I18n.getMensaje(idiomaUsuario,"gestionErrores.tablaErroresN.col12")%>");
                //tablaErrores.addColumna('230', 'center', "<%=meLanbide53I18n.getMensaje(idiomaUsuario,"gestionErrores.tablaErroresN.col11")%>");
                //tablaErrores.addColumna('200', 'center', "<%=meLanbide53I18n.getMensaje(idiomaUsuario,"gestionErrores.tablaErroresN.col12")%>");
                tablaErrores.addColumna('200', 'left', "<%=meLanbide53I18n.getMensaje(idiomaUsuario,"gestionErrores.tablaErroresN.col13")%>");
                tablaErrores.addColumna('120', 'center', "<%=meLanbide53I18n.getMensaje(idiomaUsuario,"gestionErrores.tablaErroresN.col14")%>");
                tablaErrores.addColumna('150', 'center', "<%=meLanbide53I18n.getMensaje(idiomaUsuario,"gestionErrores.tablaErroresN.col15")%>");
                tablaErrores.addColumna('150', 'center', "<%=meLanbide53I18n.getMensaje(idiomaUsuario,"gestionErrores.tablaErroresN.col16")%>");

                //var tableObject = tablaErrores;

                tablaErrores.displayCabecera = true;
                tablaErrores.height = 570;
            }

            function  cargardesplegable() {
                document.getElementById("codListaOpcionRevisado").value = "";
                document.getElementById("descListaOpcionRevisado").value = "";
            }
        </script>
    </head>

    <body class="bandaBody"onload="javascript:{
                pleaseWait('off')
            }" >
        <jsp:include page="/jsp/hidepage.jsp" flush="true">
            <jsp:param name='cargaDatos' value='<%=descriptor.getDescripcion("msjCargDatos")%>'/>
        </jsp:include>     
        <div id="divErroresMod" style="height: 100%; width: 100%;overflow-y: scroll;">
            <form onload="">
                <h2 class="legendAzul" id="pestanaPrinc"><%=meLanbide53I18n.getMensaje(idiomaUsuario,"label.tituloPrincipal")%></h2>
                <div class="contenidoPantalla">
                    <div style="clear: both;">
                        <div id="div_label" class="sub3titulo" style=" text-align: center;">
                            <label class="sub3titulo" style=" text-align: center; position: relative;" ><%=meLanbide53I18n.getMensaje(idiomaUsuario, "legend.titulo.busqueda")%></label>
                        </div>
                        <br/>
                        <div>
                            <table id="tablaBusqueda">
                                <tbody>
                                    <tr>
                                        <td style="width: 15%">
                                            <label class="etiqueta">
                                                <%=meLanbide53I18n.getMensaje(idiomaUsuario,"lable.error.id")%>
                                            </label>                            
                                        </td>
                                        <td>
                                            <input id="identificadorErrorBD_busq" name="identificadorErrorBD_busq" type="text" class="inputTextoM53" size="25" maxlength="25">
                                        </td>
                                    </tr>
                                    <tr>
                                        <td style="width: 15%">
                                            <label class="etiqueta">
                                                <%=meLanbide53I18n.getMensaje(idiomaUsuario,"label.error.id.fk")%>
                                            </label>                            
                                        </td>
                                        <td>
                                            <input id="identificadorError_busq" name="identificadorError_busq" type="text" class="inputTextoM53" size="25" maxlength="25">
                                        </td>
                                    </tr>
                                    <tr>
                                        <td style="width: 15%">
                                            <label class="etiqueta">
                                                <%=meLanbide53I18n.getMensaje(idiomaUsuario,"label.error.sistema.origen")%>
                                            </label>
                                        </td>
                                        <td>
                                            <input id="sistemaOrigen_busq" name="sistemaOrigen_busq" type="text" class="inputTextoM53" size="32" maxlength="32">
                                        </td>
                                    </tr>
                                    <tr>
                                        <td style="width: 15%">
                                            <label class="etiqueta">
                                                <%=meLanbide53I18n.getMensaje(idiomaUsuario,"label.error.idprocedimiento")%>
                                            </label>                            
                                        </td>
                                        <td>
                                            <input id="idprocedimiento_busq" name="idprocedimiento_busq" type="text" class="inputTextoM53" size="25" maxlength="25">
                                        </td>
                                    </tr>
                                    <tr>
                                        <td style="width: 15%">
                                            <label class="etiqueta">
                                                <%=meLanbide53I18n.getMensaje(idiomaUsuario,"label.error.numexpediente")%>
                                            </label>
                                        </td>
                                        <td>
                                            <input id="numeroExpediente_busq" name="numeroExpediente_busq" type="text" class="inputTextoM53" size="32" maxlength="100">
                                        </td>
                                    </tr>
                                    <tr>
                                        <td style="width: 15%">
                                            <label class="etiqueta">
                                                <%=meLanbide53I18n.getMensaje(idiomaUsuario,"label.error.clave")%>
                                            </label>
                                        </td>
                                        <td>
                                            <input id="clave_busq" name="clave_busq" type="text" class="inputTextoM53" size="32" maxlength="32">
                                        </td>
                                    </tr>
                                    <tr>
                                        <td style="width: 15%">
                                            <label class="etiqueta"><%=meLanbide53I18n.getMensaje(idiomaUsuario,"label.error.revisado")%></label>
                                        </td>
                                        <td>
                                            <input type="text" name="codListaOpcionRevisado" id="codListaOpcionRevisado" size="5" class="inputTexto" value="" onkeyup="xAMayusculas(this);" />
                                            <input type="text" name="descListaOpcionRevisado"  id="descListaOpcionRevisado" size="15" class="inputTexto" readonly="true" value="" />
                                            <a href="" id="anchorListaOpcionRevisado" name="anchorListaOpcionRevisado">
                                                <img src="<%=request.getContextPath()%>/images/listas/botondesplegable.gif" id="botonAplicacion"
                                                     name="botonAplicacion" height="14" width="14" border="0" style="cursor:hand;">
                                            </a>
                                            <!--<input id="check_notificado" name="check_notificado" type="checkbox" value="<//%=meLanbide53I18n.getMensaje(idiomaUsuario,"label.error.notificado")%>">-->
                                        </td>
                                    </tr>
                                    <tr>
                                        <td style="width: 15%">
                                            <label class="etiqueta"><%=meLanbide53I18n.getMensaje(idiomaUsuario,"label.error.notificado")%></label>
                                        </td>
                                        <td>
                                            <input type="text" name="codListaOpcionNotificado" id="codListaOpcionNotificado" size="5" class="inputTexto" value="" onkeyup="xAMayusculas(this);"  />
                                            <input type="text" name="descListaOpcionNotificado"  id="descListaOpcionNotificado" size="15" class="inputTexto" readonly="true" value="" />
                                            <a href="" id="anchorListaOpcionNotificado" name="anchorListaOpcionNotificado">
                                                <img src="<%=request.getContextPath()%>/images/listas/botondesplegable.gif" id="botonAplicacion"
                                                     name="botonAplicacion" height="14" width="14" border="0" style="cursor:hand;">
                                            </a>
                                            <!--<input id="check_verficado" name="check_verficado" type="checkbox" value="<//%=meLanbide53I18n.getMensaje(idiomaUsuario,"label.error.revisado")%>">-->
                                        </td>
                                    </tr>
                                    <!--Nuevo campo despelegable-->
                                    <tr>
                                        <td>
                                            <label class="etiqueta"><%=meLanbide53I18n.getMensaje(idiomaUsuario,"label.error.retramitado")%></label>
                                        </td>
                                        <td>
                                            <input type="text" name="codListaOpcionRetramitado" id="codListaOpcionRetramitado" size="5" class="inputTexto" value="" onkeyup="xAMayusculas(this);"  />
                                            <input type="text" name="descListaOpcionRetramitado"  id="descListaOpcionRetramitado" size="15" class="inputTexto" readonly="true" value="" />
                                            <a href="" id="anchorListaOpcionRetramitado" name="anchorListaOpcionRetramitado">
                                                <img src="<%=request.getContextPath()%>/images/listas/botondesplegable.gif" id="botonAplicacion"
                                                     name="botonAplicacion" height="14" width="14" border="0" style="cursor:hand;">
                                            </a>
                                        </td>
                                    </tr>
                                    <!--Fin Nuevo campo despelegable-->
                                    <tr>
                                        <td style="width: 15%">
                                            <label class="etiqueta"><%=meLanbide53I18n.getMensaje(idiomaUsuario,"label.busqueda.fecha")%></label>                            
                                        </td>
                                        <td >
                                            <table style="width: 50%">
                                                <tbody>
                                                    <tr>
                                                        <td style="width: 4%">
                                                            <!--<label class="etiqueta">
                                                            <%--<//%=meLanbide53I18n.getMensaje(idiomaUsuario,"label.busqueda.desde")%>--%>
                                                            <!--</label> -->
                                                            <div style="float: left;">
                                                                <input type="text" class="inputTxtFecha" 
                                                                       id="meLanbide53Fecha_busqE" name="meLanbide53Fecha_busqE"
                                                                       maxlength="10"  size="10"
                                                                       value=""
                                                                       onkeyup = "return SoloCaracteresFechaLanbide(this);"
                                                                       onblur = "javascript:return comprobarFechaLanbide(this);"
                                                                       onfocus="javascript:this.select();"/>
                                                                <A href="javascript:calClick(event);return false;" onClick="mostrarCalFechaE(event);return false;" style="text-decoration:none;">   
                                                                    <IMG style="border: 0px solid none" height="17" id="calMeLanbide53Fecha_busqE" name="calMeLanbide53Fecha_busqE" border="0" 
                                                                         src="<c:url value='/images/calendario/icono.gif'/>" > <!--width="25"-->
                                                                </A>
                                                            </div>
                                                        </td>
                                                        <td style="width: 2%; text-align: center;">
                                                            <label class="etiqueta" style="float: left; text-align: center;">
                                                                <%=meLanbide53I18n.getMensaje(idiomaUsuario,"label.busqueda.hasta")%>
                                                            </label> 
                                                        </td>
                                                        <td style="width: 7%">
                                                            <div style="float: left;">
                                                                <input type="text" class="inputTxtFecha" 
                                                                       id="meLanbide53Fecha_busqS" name="meLanbide53Fecha_busqS"
                                                                       maxlength="10"  size="10"
                                                                       value=""
                                                                       onkeyup = "return SoloCaracteresFechaLanbide(this);"
                                                                       onblur = "javascript:return comprobarFechaLanbide(this);"
                                                                       onfocus="javascript:this.select();"/>
                                                                <A href="javascript:calClick(event);return false;" onClick="mostrarCalFechaS(event);return false;" style="text-decoration:none;">   
                                                                    <IMG style="border: 0px solid none" height="17" id="calMeLanbide53Fecha_busqS" name="calMeLanbide53Fecha_busqS" border="0" 
                                                                         src="<c:url value='/images/calendario/icono.gif'/>" > <!--width="25"-->
                                                                </A>
                                                            </div>
                                                        </td>
                                                    </tr>
                                                </tbody>
                                            </table>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td style="width: 15%">
                                            <label class="etiqueta">
                                                <%=meLanbide53I18n.getMensaje(idiomaUsuario,"label.busqueda.hora")%>
                                            </label>                            
                                        </td>
                                        <td>
                                            <table style="width: 50%">
                                                <tbody>
                                                    <tr>
                                                        <td style="width: 4%">
                                                            <!--<label class="etiqueta" style="float: left; margin-left: 5px">
                                                            <%--<//%=meLanbide53I18n.getMensaje(idiomaUsuario,"label.busqueda.desde")%>--%>
                                                            <!--</label>-->
                                                            <input id="hora_entrada_busq" name="hora_entrada_busq" type="text" class="inputTextoM53" size="10" maxlength="8" 
                                                                   onkeypress="formatearHora(this, event)" onblur="comprobarMaskaraHora(this)" 
                                                                   style="float: left;">
                                                        </td>
                                                        <td style="width: 2%; text-align: center;">
                                                            <label class="etiqueta" style="float: left; text-align: center; ">
                                                                <%=meLanbide53I18n.getMensaje(idiomaUsuario,"label.busqueda.hasta")%>
                                                            </label>
                                                        </td>
                                                        <td style="width: 7%">
                                                            <input id="hora_entrada_busqF" name="hora_entrada_busqF" type="text" class="inputTextoM53" size="10" maxlength="8"   
                                                                   onkeypress="formatearHora(this, event)" onblur="comprobarMaskaraHora(this)" 
                                                                   style="float: left;">
                                                        </td>
                                                    </tr>
                                                </tbody>
                                            </table>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td style="width: 15%">
                                            <label class="etiqueta">
                                                <%=meLanbide53I18n.getMensaje(idiomaUsuario,"label.error.fecharevision")%>
                                            </label>                            
                                        </td>
                                        <td>
                                            <table style="width: 50%">
                                                <tbody>
                                                    <tr>
                                                        <td style="width: 4%">
                                                            <!--<label class="etiqueta">
                                                            <%--<//%=meLanbide53I18n.getMensaje(idiomaUsuario,"label.busqueda.desde")%>--%>
                                                            <!--</label> -->
                                                            <div style="float: left;">
                                                                <input type="text" class="inputTxtFecha" 
                                                                       id="meLanbide53Fecha_RevbusqE" name="meLanbide53Fecha_RevbusqE"
                                                                       maxlength="10"  size="10"
                                                                       value=""
                                                                       onkeyup = "return SoloCaracteresFechaLanbide(this);"
                                                                       onblur = "javascript:return comprobarFechaLanbide(this);"
                                                                       onfocus="javascript:this.select();"/>
                                                                <A href="javascript:calClick(event);return false;" onClick="mostrarCalFechaRevE(event);return false;" style="text-decoration:none;">   
                                                                    <IMG style="border: 0px solid none" height="17" id="calMeLanbide53Fecha_RevbusqE" name="calMeLanbide53Fecha_RevbusqE" border="0" 
                                                                         src="<c:url value='/images/calendario/icono.gif'/>" > <!--width="25"-->
                                                                </A>
                                                            </div>
                                                        </td>
                                                        <td style="width: 2%; text-align: center;">
                                                            <label class="etiqueta" style="float: left; text-align: center; ">
                                                                <%=meLanbide53I18n.getMensaje(idiomaUsuario,"label.busqueda.hasta")%>
                                                            </label> 
                                                        </td>
                                                        <td style="width: 7%">
                                                            <div style="float: left;">
                                                                <input type="text" class="inputTxtFecha" 
                                                                       id="meLanbide53Fecha_RevbusqF" name="meLanbide53Fecha_RevbusqF"
                                                                       maxlength="10"  size="10"
                                                                       value=""
                                                                       onkeyup = "return SoloCaracteresFechaLanbide(this);"
                                                                       onblur = "javascript:return comprobarFechaLanbide(this);"
                                                                       onfocus="javascript:this.select();"/>
                                                                <A href="javascript:calClick(event);return false;" onClick="mostrarCalFechaRevF(event);return false;" style="text-decoration:none;">   
                                                                    <IMG style="border: 0px solid none" height="17" id="calMeLanbide53Fecha_RevbusqF" name="calMeLanbide53Fecha_RevbusqF" border="0" 
                                                                         src="<c:url value='/images/calendario/icono.gif'/>" > <!--width="25"-->
                                                                </A>
                                                            </div>
                                                        </td>
                                                    </tr>
                                                </tbody>
                                            </table>
                                        </td>
                                    </tr>
                                    <tr>
                                        <!--                                <td style="width: 15%"></td>-->
                                        <td style="text-align: center;" colspan="2">
                                            <input type="button" id="botonBusqueda" name="botonBusqueda" class="botonGeneral" value="<%=meLanbide53I18n.getMensaje(idiomaUsuario, "btn.buscar")%>" onclick="pulsarBusquedaFiltrando();">
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
                            <div id="div_label" class="sub3titulo" style=" text-align: center; width: 95% ">
                                <label class="sub3titulo" style=" text-align: center; width: 100%; position: relative;" ><%=meLanbide53I18n.getMensaje(idiomaUsuario, "legend.titulo.tabla")%></label>
                            </div>    
                            <div id="divGeneral" style="overflow-y: auto; overflow-x: auto; height: 620px;">     
                                <div id="listaErrores" style="padding: 5px; width:98%; height: 610px; text-align: center; overflow-x:auto; overflow-y:auto;margin:0px;margin-top:0px;" align="center"></div>
                            </div>
                            <br/>
                            <div class="botonera" style="text-align: center;">
                                <!--<input type="button" id="btnNuevoAcceso" name="btnNuevoAcceso" class="botonGeneral"  value="<//%=meLanbide53I18n.getMensaje(idiomaUsuario, "btn.nuevo")%>" onclick="pulsarNuevoError();">-->
                                <input type="button" id="btnActualizarError" name="btnActualizarError" class="botonGeneral" value="<%=meLanbide53I18n.getMensaje(idiomaUsuario, "btn.modificar")%>" onclick="pulsarModificarError();">
                                <input type="button" id="btnObtenerDocs" name="btnObtenerDocs"   class="botonMasLargo" value="<%=meLanbide53I18n.getMensaje(idiomaUsuario, "btn.obtenerDocs")%>" onclick="pulsarObtenerDocs();">
                                <input type="button" id="btnError" name="btnError"   class="botonMasLargo" value="<%=meLanbide53I18n.getMensaje(idiomaUsuario, "btn.Error")%>" onclick="pulsarLinkDetalleError();">
                                <input type="button" id="btnEstadísticas" name="btnEstadísticas"   class="botonMasLargo" value="<%=meLanbide53I18n.getMensaje(idiomaUsuario, "btn.Error1")%>" onclick="estadisticas();">
                                <input type="button" id="btnRetramitacion" name="btnRetramitacion"   class="botonMasLargo" value="<%=meLanbide53I18n.getMensaje(idiomaUsuario, "btn.Retramitacion")%>" onclick="pulsarRetramitacion();">
                            </div>
                            <!-- <br/>
                            <div class="botonera" style="text-align: center;">
                                <input type="button" id="btnSeleccionarTodos" name="btnSeleccionarTodos"   class="botonMasLargo" value="<%=meLanbide53I18n.getMensaje(idiomaUsuario, "btn.SeleccionarTodos")%>" onclick="pulsarSeleccionarTodos();">
                                <input type="button" id="btnRetramitacionMasiva" name="btnRetramitacionMasiva"   class="botonMasLargo" value="<%=meLanbide53I18n.getMensaje(idiomaUsuario, "btn.RetramitacionMasiva")%>" onclick="pulsarRetramitacionMasiva();">
                            </div> -->
                        </div>
                    </div>
                </div>

                <!--Script Ejecucion Elementos Pagina   legendAzul-->
                <script type="text/javascript">
                    //Tabla Errores
                    pleaseWait('on');
                    var tablaErrores;
                    var listaErrores = new Array();
                    var listaErroresTabla = new Array();

                    crearTabla();

                    <%  		
                        RegistroErrorVO objectVO = new RegistroErrorVO();
                        List<RegistroErrorVO> _list = null;
                        if(request.getAttribute("ListErrores")!=null){
                            _list = (List<RegistroErrorVO>)request.getAttribute("ListErrores");
                        }													
                        if (_list!= null && _list.size() >0){
                            //%>//var contador=0;<%
                            for (int indice=0;indice<_list.size();indice++)
                            {
                                objectVO = _list.get(indice);
                    %>

                    var notificado = '<%=objectVO.getErrorNotificado()!=null?objectVO.getErrorNotificado():""%>';
                    if (notificado != "" && notificado == '<%=ConstantesMeLanbide53.STANDFOR_SI_S%>')
                        notificado = '<%=meLanbide53I18n.getMensaje(idiomaUsuario,"label.error.STANDFOR_SI_S")%>';
                    else if (notificado != "" && notificado == '<%=ConstantesMeLanbide53.STANDFOR_NO_N%>')
                        notificado = '<%=meLanbide53I18n.getMensaje(idiomaUsuario,"label.error.STANDFOR_NO_N")%>';

                    var revisado = '<%=objectVO.getErrorRevisado()!=null?objectVO.getErrorRevisado():""%>';
                    if (revisado != "" && revisado == '<%=ConstantesMeLanbide53.STANDFOR_SI_S%>')
                        revisado = '<%=meLanbide53I18n.getMensaje(idiomaUsuario,"label.error.STANDFOR_SI_S")%>';
                    else if (revisado != "" && revisado == '<%=ConstantesMeLanbide53.STANDFOR_NO_N%>')
                        revisado = '<%=meLanbide53I18n.getMensaje(idiomaUsuario,"label.error.STANDFOR_NO_N")%>';
                    // he descomentado la línea anterior                 

                    var retramitado = '<%=objectVO.getSolicitudRetramitada()!=null?objectVO.getSolicitudRetramitada():""%>';
                    if (retramitado != "" && retramitado == '<%=ConstantesMeLanbide53.STANDFOR_SI_S%>')
                        retramitado = '<%=meLanbide53I18n.getMensaje(idiomaUsuario,"label.error.STANDFOR_SI_S")%>';
                    else if (retramitado != "" && retramitado == '<%=ConstantesMeLanbide53.STANDFOR_NO_N%>')
                        retramitado = '<%=meLanbide53I18n.getMensaje(idiomaUsuario,"label.error.STANDFOR_NO_N")%>';

                    listaErrores[<%=indice%>] = ['<%=objectVO.getId()%>', '<%=objectVO.getFechaError()%>', '<%=objectVO.getMensajeError().replaceAll("\n\r","").replaceAll("\r","").replaceAll("\n","")%>', '<%=objectVO.getMensajeException()!=null?objectVO.getMensajeException().replaceAll("\n\r","").replaceAll("\r","").replaceAll("\n",""):""%>', '<%=objectVO.getCausaException()!=null?objectVO.getCausaException().replaceAll("\n\r","").replaceAll("\r","").replaceAll("\n",""):""%>', '<%=objectVO.getTrazaError()!=null?objectVO.getTrazaError().replaceAll("\n\r","").replaceAll("\r","").replaceAll("\n",""):""%>', '<%=objectVO.getIdProcedimiento()%>', '<%=objectVO.getIdErrorFK()%>', '<%=objectVO.getClave()%>', '<%=objectVO.getErrorNotificado()%>', '<%=objectVO.getErrorRevisado()%>', '<%=objectVO.getFechaRevisionError()%>', '<%=objectVO.getObservacionesError()!=null?objectVO.getObservacionesError().replaceAll("\n\r","").replaceAll("\r","").replaceAll("\n",""):""%>', '<%=objectVO.getSistemaOrigen()%>', '<%=objectVO.getUbicacionError()!=null?objectVO.getUbicacionError().replaceAll("\n\r","").replaceAll("\r","").replaceAll("\n",""):""%>', '<%=objectVO.getFicheroLog()%>', '<%=objectVO.getEvento()%>', '<%=objectVO.getNumeroExpediente()%>', '<%=objectVO.getXmlReglasSolicitud()!=null&& !objectVO.getXmlReglasSolicitud().equals("")?"1":""%>', retramitado, '<%=objectVO.getFechaRetramitacion()!=null?objectVO.getFechaRetramitacion():""%>', '<%=objectVO.getResultadoRetramitacion()!=null?objectVO.getResultadoRetramitacion():""%>', '<%=objectVO.getImpacto()!=null?objectVO.getImpacto():""%>', '<%=objectVO.getTratado()!=null?objectVO.getTratado():""%>'];
                    listaErroresTabla[<%=indice%>] = ['<%=objectVO.getId()%>', '<%=objectVO.getFechaError()%>', notificado, revisado, '<%=objectVO.getIdProcedimiento()!=null  ? objectVO.getIdProcedimiento():""%>', '<%=objectVO.getIdErrorFK()!=null  ? objectVO.getIdErrorFK():""%>', '<%=objectVO.getSistemaOrigen()!=null  ? objectVO.getSistemaOrigen():""%>', '<%=objectVO.getNumeroExpediente()!= null ?objectVO.getNumeroExpediente() :""%>', '<%=objectVO.getClave()!=null  ? objectVO.getClave():""%>', '<%=objectVO.getEvento()!=null ?objectVO.getEvento():""%>', '<%=objectVO.getMensajeError()!=null  ? objectVO.getMensajeError().replaceAll("\n\r"," ").replaceAll("\r"," ").replaceAll("\n"," "):""%>', '<%=objectVO.getXmlReglasSolicitud()!=null && !objectVO.getXmlReglasSolicitud().equals("")&& !objectVO.getXmlReglasSolicitud().equals("")?"1":""%>', retramitado, '<%=objectVO.getFechaRetramitacion()!=null?objectVO.getFechaRetramitacion():""%>', '<%=objectVO.getResultadoRetramitacion()!=null?objectVO.getResultadoRetramitacion():""%>', '<%=objectVO.getImpacto()!=null?objectVO.getImpacto():""%>', '<%=objectVO.getTratado()!=null?objectVO.getTratado():""%>'];
                    //listaErrores[<%=indice%>] = [check('false',contador),'<%=objectVO.getId()%>', '<%=objectVO.getFechaError()%>', '<%=objectVO.getMensajeError().replaceAll("\n\r","").replaceAll("\r","").replaceAll("\n","")%>', '<%=objectVO.getMensajeException()!=null?objectVO.getMensajeException().replaceAll("\n\r","").replaceAll("\r","").replaceAll("\n",""):""%>', '<%=objectVO.getCausaException()!=null?objectVO.getCausaException().replaceAll("\n\r","").replaceAll("\r","").replaceAll("\n",""):""%>', '<%=objectVO.getTrazaError()!=null?objectVO.getTrazaError().replaceAll("\n\r","").replaceAll("\r","").replaceAll("\n",""):""%>', '<%=objectVO.getIdProcedimiento()%>', '<%=objectVO.getIdErrorFK()%>', '<%=objectVO.getClave()%>', '<%=objectVO.getErrorNotificado()%>', '<%=objectVO.getErrorRevisado()%>', '<%=objectVO.getFechaRevisionError()%>', '<%=objectVO.getObservacionesError()!=null?objectVO.getObservacionesError().replaceAll("\n\r","").replaceAll("\r","").replaceAll("\n",""):""%>', '<%=objectVO.getSistemaOrigen()%>', '<%=objectVO.getUbicacionError()!=null?objectVO.getUbicacionError().replaceAll("\n\r","").replaceAll("\r","").replaceAll("\n",""):""%>', '<%=objectVO.getFicheroLog()%>', '<%=objectVO.getEvento()%>', '<%=objectVO.getNumeroExpediente()%>', '<%=objectVO.getXmlReglasSolicitud()!=null&& !objectVO.getXmlReglasSolicitud().equals("")?"1":""%>', retramitado, '<%=objectVO.getFechaRetramitacion()!=null?objectVO.getFechaRetramitacion():""%>', '<%=objectVO.getResultadoRetramitacion()!=null?objectVO.getResultadoRetramitacion():""%>', '<%=objectVO.getImpacto()!=null?objectVO.getImpacto():""%>', '<%=objectVO.getTratado()!=null?objectVO.getTratado():""%>'];
                    //listaErroresTabla[<%=indice%>] = [check('false',contador),'<%=objectVO.getId()%>', '<%=objectVO.getFechaError()%>', notificado, revisado, '<%=objectVO.getIdProcedimiento()!=null  ? objectVO.getIdProcedimiento():""%>', '<%=objectVO.getIdErrorFK()!=null  ? objectVO.getIdErrorFK():""%>', '<%=objectVO.getSistemaOrigen()!=null  ? objectVO.getSistemaOrigen():""%>', '<%=objectVO.getNumeroExpediente()!= null ?objectVO.getNumeroExpediente() :""%>', '<%=objectVO.getClave()!=null  ? objectVO.getClave():""%>', '<%=objectVO.getEvento()!=null ?objectVO.getEvento():""%>', '<%=objectVO.getMensajeError()!=null  ? objectVO.getMensajeError().replaceAll("\n\r"," ").replaceAll("\r"," ").replaceAll("\n"," "):""%>', '<%=objectVO.getXmlReglasSolicitud()!=null && !objectVO.getXmlReglasSolicitud().equals("")&& !objectVO.getXmlReglasSolicitud().equals("")?"1":""%>', retramitado, '<%=objectVO.getFechaRetramitacion()!=null?objectVO.getFechaRetramitacion():""%>', '<%=objectVO.getResultadoRetramitacion()!=null?objectVO.getResultadoRetramitacion():""%>', '<%=objectVO.getImpacto()!=null?objectVO.getImpacto():""%>', '<%=objectVO.getTratado()!=null?objectVO.getTratado():""%>'];
                    //contador++;
                    <%
                            }// for
                        }// if
                    %>

                    listaSelErrOriginal = listaErroresTabla;
                    tablaErrores.lineas = listaErroresTabla;
                    tablaErrores.displayTablaConTooltips(listaErroresTabla);
                    //document.getElementById('listaErrores').children[0].children[1].children[0].children[0].ondblclick = function(event){
                    //            pulsarModificarError(event);
                    if (navigator.appName.indexOf("Internet Explorer") != -1 || (navigator.appName.indexOf("Netscape") != -1 && navigator.appCodeName.match(/Mozilla/))) {
                        try {
                            var div = document.getElementById('listaErrores');
                            //div.children[0].children[0].children[0].children[0].children[0].children[0].children[0].children[0].children[0].children[0].children[0].children[0].children[0].children[1].style.width = '100%';
                            //div.children[0].children[1].children[0].children[0].children[0].children[0].children[0].children[0].children[0].children[0].children[0].children[1].style.width = '100%';
                            if (navigator.appName.indexOf("Internet Explorer") != -1 && !(navigator.userAgent.match(/compatible/))) {
                                div.children[0].children[0].children[0].children[0].children[0].children[0].children[1].style.width = '100%';
                                div.children[0].children[1].children[0].children[0].children[0].children[0].style.width = '100%';
                            } else {
                                div.children[0].children[0].children[0].children[0].children[0].children[0].style.width = '100%';
                                div.children[0].children[1].children[0].children[0].style.width = '100%';
                            }
                        } catch (err) {
                        }
                    }

                    var comboListaOpcionRevisado = new Combo("ListaOpcionRevisado");
                    comboListaOpcionRevisado.addItems(listaCodOpcionNotyRevi, listaDesOpcionNotyRevi);
                    comboListaOpcionRevisado.change = cargarDatosRevisado;

                    var comboListaOpcionNotificado = new Combo("ListaOpcionNotificado");
                    comboListaOpcionNotificado.addItems(listaCodOpcionNotyRevi, listaDesOpcionNotyRevi);
                    comboListaOpcionNotificado.change = cargarDatosNotificado;

                    var comboListaOpcionRetramitado = new Combo("ListaOpcionRetramitado");
                    comboListaOpcionRetramitado.addItems(listaCodOpcionRetramite, listaDesOpcionRetramite);
                    comboListaOpcionRetramitado.change = cargarDatosRetramitado;

                    var mensajeValidacion = "";

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
                        if (Trim(inputFecha.value) != '') {
                            var D = ValidarFechaConFormatoLanbide(inputFecha.value, formato);
                            if (!D[0]) {
                                jsp_alerta("A", "<%=meLanbide53I18n.getMensaje(idiomaUsuario, "msg.fechaNoVal")%>");
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
                        if (window.event)
                            evento = window.event;
                        if (document.getElementById("calMeLanbide53Fecha_busqE").src.indexOf("icono.gif") != -1)
                            showCalendar('forms[0]', 'meLanbide53Fecha_busqE', null, null, null, '', 'calMeLanbide53Fecha_busqE', '', null, null, null, null, null, null, null, null, evento);
                    }//mostrarCalFechaE

                    function mostrarCalFechaS(evento) {
                        if (window.event)
                            evento = window.event;
                        if (document.getElementById("calMeLanbide53Fecha_busqS").src.indexOf("icono.gif") != -1)
                            showCalendar('forms[0]', 'meLanbide53Fecha_busqS', null, null, null, '', 'calMeLanbide53Fecha_busqS', '', null, null, null, null, null, null, null, null, evento);
                    }//mostrarCalFechaS

                    function mostrarCalFechaRevE(evento) {
                        if (window.event)
                            evento = window.event;
                        if (document.getElementById("calMeLanbide53Fecha_RevbusqE").src.indexOf("icono.gif") != -1)
                            showCalendar('forms[0]', 'meLanbide53Fecha_RevbusqE', null, null, null, '', 'calMeLanbide53Fecha_RevbusqE', '', null, null, null, null, null, null, null, null, evento);
                    }//mostrarCalFechaRevE

                    function mostrarCalFechaRevF(evento) {
                        if (window.event)
                            evento = window.event;
                        if (document.getElementById("calMeLanbide53Fecha_RevbusqF").src.indexOf("icono.gif") != -1)
                            showCalendar('forms[0]', 'meLanbide53Fecha_RevbusqF', null, null, null, '', 'calMeLanbide53Fecha_RevbusqF', '', null, null, null, null, null, null, null, null, evento);
                    }//mostrarCalFechaRevF

                    function formatearHora(control, evento) {
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
                                texto = control.value + ":";
                                control.value = texto;
                            }
                            return true;
                        }
                        return false;
                    }

                    function pulsarLinkDetalleError() {
                        var control = new Date();
                        lanzarPopUpModal('<%=request.getContextPath()%>/PeticionModuloIntegracion.do?tarea=preparar&modulo=MELANBIDE53&operacion=cargarPantallaIdenErrores&tipo=0&nuevo=0&control=' + control.getTime(), 750, 1050, 'no', 'yes', function (result) {});
                    }

                    function comprobarMaskaraHora_Old(control) {
                        if (control != null) {
                            if (control.value == "") {
                                return true;
                            }
                            var texto = control.value.split(':');
                            if (texto.length != 3) {
                                jsp_alerta("A", '<%=meLanbide53I18n.getMensaje(idiomaUsuario, "msg.horaNoVal")%>');
                                control.focus();
                                control.select();
                                return false;
                            }
                            if (!(texto[0] >= 0 && texto[0] <= 24)) {
                                jsp_alerta("A", '<%=meLanbide53I18n.getMensaje(idiomaUsuario, "msg.horaNoVal")%>');
                                control.focus();
                                control.select();
                                return false;
                            } else {
                                if (texto[0].length < 2) {
                                    texto[0] = '0' + texto[0];
                                    control.value = texto[0] + ':' + texto[1] + ':' + texto[2];
                                }
                            }
                            if (!(texto[1] >= 0 && texto[1] <= 59) || !(texto[2] >= 0 && texto[2] <= 59)) {
                                jsp_alerta("A", '<%=meLanbide53I18n.getMensaje(idiomaUsuario, "msg.horaNoVal")%>');
                                control.focus();
                                control.select();
                                return false;
                            } else {
                                if (texto[1].length < 2) {
                                    texto[1] = '0' + texto[1];
                                    control.value = texto[0] + ':' + texto[1] + ':' + texto[2];
                                }
                                if (texto[2].length < 2) {
                                    texto[2] = '0' + texto[2];
                                    control.value = texto[0] + ':' + texto[1] + ':' + texto[2];
                                }
                            }
                        } else {
                            jsp_alerta("A", '<%=meLanbide53I18n.getMensaje(idiomaUsuario, "msg.horaNoVal")%>');
                        }
                    }

                    function comprobarMaskaraHora(control) {
                        if (control != null) {
                            if (control.value == "") {
                                return true;
                            }
                            var texto = control.value.split(':');
                            //--------------------------------------------------------------

                            if (texto.length == 1) {
                                texto[1] = '00';
                                texto[2] = '00';
                                control.value = texto[0] + ':' + texto[1] + ':' + texto[2];
                            }

                            if (texto.length == 2) {
                                texto[2] = '00';
                                control.value = texto[0] + ':' + texto[1] + ':' + texto[2];
                            }

                            //--------------------------------------------------------------------------------

                            if (texto.length != 3) {
                                jsp_alerta("A", '<%=meLanbide53I18n.getMensaje(idiomaUsuario, "msg.horaNoVal")%>');
                                control.focus();
                                control.select();
                                return false;
                            }
                            if (!(texto[0] >= 0 && texto[0] <= 24)) {
                                jsp_alerta("A", '<%=meLanbide53I18n.getMensaje(idiomaUsuario, "msg.horaNoVal")%>');
                                control.focus();
                                control.select();
                                return false;
                            } else {
                                if (texto[0].length < 2) {
                                    texto[0] = '0' + texto[0];
                                    control.value = texto[0] + ':' + texto[1] + ':' + texto[2];
                                }
                            }
                            if (!(texto[1] >= 0 && texto[1] <= 59) || !(texto[2] >= 0 && texto[2] <= 59)) {
                                jsp_alerta("A", '<%=meLanbide53I18n.getMensaje(idiomaUsuario, "msg.horaNoVal")%>');
                                control.focus();
                                control.select();
                                return false;
                            } else {
                                if (texto[1].length < 2) {
                                    texto[1] = '0' + texto[1];
                                    control.value = texto[0] + ':' + texto[1] + ':' + texto[2];
                                }
                                if (texto[2].length < 2) {
                                    texto[2] = '0' + texto[2];
                                    control.value = texto[0] + ':' + texto[1] + ':' + texto[2];
                                }
                            }
                        } else {
                            jsp_alerta("A", '<%=meLanbide53I18n.getMensaje(idiomaUsuario, "msg.horaNoVal")%>');
                        }
                    }

                    function pulsarNuevoError() {
                        var control = new Date();
                        lanzarPopUpModal('<%=request.getContextPath()%>/PeticionModuloIntegracion.do?tarea=preparar&modulo=MELANBIDE53&operacion=cargarNuevoError&tipo=0&nuevo=1&control=' + control.getTime(), 650, 1000, 'no', 'no', function (result) {
                            if (result != undefined) {
                                if (result[0] == '0') {
                                    recargarTablaErrores(result);
                                }
                            }
                        });
                    }

                    function pulsarModificarError() {
                        //getSeleccionados();
                        
                        if (tablaErrores.selectedIndex != -1) {
                        //if(listaIdRegistroSeleccionados.length == 1){
                            var control = new Date();
                            lanzarPopUpModal('<%=request.getContextPath()%>/PeticionModuloIntegracion.do?tarea=preparar&modulo=MELANBIDE53&operacion=cargarModificarGestionError&tipo=0&nuevo=0&id=' + listaErrores[tablaErrores.selectedIndex][0] + '&control=' + control.getTime(), 760, 1100, 'yes', 'yes', function (result) {
                            //lanzarPopUpModal('<%=request.getContextPath()%>/PeticionModuloIntegracion.do?tarea=preparar&modulo=MELANBIDE53&operacion=cargarModificarGestionError&tipo=0&nuevo=0&id=' + listaIdRegistroSeleccionados[0] + '&control=' + control.getTime(), 760, 1100, 'yes', 'yes', function (result) {    
                                if (result != undefined) {
                                    if (result[0] == '0') {
                                        recargarTablaErrores(result);
                                    }
                                }
                            });
                        } else {
                            jsp_alerta('A', '<%=meLanbide53I18n.getMensaje(idiomaUsuario, "msg.msjNoSelecFila")%>');
                            //jsp_alerta("A",'<%=meLanbide53I18n.getMensaje(idiomaUsuario,"error.errorNoSeleccionadoUno")%>');
                        }
                    }

                    function pulsarObtenerDocs() {
                        //getSeleccionados();
                        
                        if (tablaErrores.selectedIndex != -1) {
                        //if(listaIdRegistroSeleccionados.length == 1){
                            var control = new Date();
                            //var result = null;
                            var oidSolicitud = listaErrores[tablaErrores.selectedIndex][8];
                            var codProcedimiento = listaErrores[tablaErrores.selectedIndex][6];
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
                                }//if(hijos[j].nodeName=="CODIGO_OPERACION")   
                                if (hijos[j].nodeName == "REGISTRO") {
                                    registro = hijos[j].childNodes[0].nodeValue;
                                    //alert("Se ha producido un error en la retramitación. "+resultado);
                                }//if(hijos[j].nodeName=="CODIGO_OPERACION")
                            }//for(j=0;hijos!=null && j<hijos.length;j++)

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
                        //habilitará cuando el campo REG_ERR_RETRAMIT_SN sea nulo o igual a 'N' y el campo REG_ERR_SOLICITUD tenga valor (diferente de nulo), 
                        
                        //getSeleccionados();
                        
                        if (tablaErrores.selectedIndex != -1) {
                        //if(listaIdRegistroSeleccionados.length == 1){
                            var control = new Date();
                            var idRegistro = listaErrores[tablaErrores.selectedIndex][0];
                            var retramitado = listaErrores[tablaErrores.selectedIndex][19];
                            var xmlSolicitud = listaErrores[tablaErrores.selectedIndex][18];
                            
                            /*var idRegistro = listaIdRegistroSeleccionados[0];
                            var retramitado = listaRetramitadoSeleccionados[0];
                            var xmlSolicitud = listaXmlSolicitudSeleccionados[0];*/

                            var result = null;
                            
                            var oidDocumento = listaErrores[tablaErrores.selectedIndex][8];
                            
                            //ese oid de documento tiene 'OK' (ESTADISTICAS)
                            var tieneOK = oidEsOK(oidDocumento);
                            
                            //ese oid de documento está en registro ? (R_RED)
                            var registro = getRegistroDeOid(oidDocumento);
                            
                            if ((retramitado == "null" || retramitado == 'N' || retramitado == 'No') && (xmlSolicitud != 'null' && xmlSolicitud != '') 
                                    && tieneOK == 0 && (registro == 'null' || registro == '' || registro == 'no_existe')) {
                                // if ((retramitado=="null" ||retramitado=='' || retramitado=='N' || retramitado=='No')&&(xmlSolicitud!='')){                        
                                var ajax = getXMLHttpRequest();
                                var nodos = null;
                                var CONTEXT_PATH = '<%=request.getContextPath()%>';
                                var url = CONTEXT_PATH + "/PeticionModuloIntegracion.do";
                                var parametros = "";
                                var control = new Date();
                                parametros = 'tarea=preparar&modulo=MELANBIDE53&operacion=retramitarError&tipo=0&id=' + idRegistro + '&control=' + control.getTime();
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
                                    var resultado = "";
                                    var codigoOperacion = null;
                                    //var er = null;
                                    //var listaNueva = new Array();
                                    //var fila = new Array();
                                    var nodoFila;
                                    var hijosFila;
                                    for (j = 0; hijos != null && j < hijos.length; j++) {
                                        if (hijos[j].nodeName == "CODIGO_OPERACION") {
                                            codigoOperacion = hijos[j].childNodes[0].nodeValue;
                                            //listaNueva[j] = codigoOperacion;
                                        }//if(hijos[j].nodeName=="CODIGO_OPERACION")   
                                        if (hijos[j].nodeName == "DES_ERROR") {
                                            resultado = hijos[j].childNodes[0].nodeValue;
                                            //listaNueva[j] = resultado;
                                            //alert("Se ha producido un error en la retramitación. "+resultado);
                                        }//if(hijos[j].nodeName=="CODIGO_OPERACION")   
                                        /*else if (hijos[j].nodeName == "FILA") {
                                            nodoFila = hijos[j];
                                            hijosFila = nodoFila.childNodes;
                                            for (var cont = 0; cont < hijosFila.length; cont++) {
                                                if (hijosFila[cont].nodeName == "REG_ERR_ID") {
                                                    if (hijosFila[cont].childNodes.length > 0) {
                                                        fila[0] = hijosFila[cont].childNodes[0].nodeValue;
                                                    } else {
                                                        fila[0] = '-';
                                                    }
                                                } else if (hijosFila[cont].nodeName == "REG_ERR_FEC_ERROR") {
                                                    if (hijosFila[cont].childNodes.length > 0) {
                                                        fila[1] = hijosFila[cont].childNodes[0].nodeValue;
                                                    } else {
                                                        fila[1] = '-';
                                                    }
                                                } else if (hijosFila[cont].nodeName == "REG_ERR_MEN") {
                                                    if (hijosFila[cont].childNodes.length > 0) {
                                                        fila[2] = hijosFila[cont].childNodes[0].nodeValue;
                                                    } else {
                                                        fila[2] = '-';
                                                    }
                                                } else if (hijosFila[cont].nodeName == "REG_ERR_EXCEP_MEN") {
                                                    if (hijosFila[cont].childNodes.length > 0) {
                                                        fila[3] = hijosFila[cont].childNodes[0].nodeValue;
                                                    } else {
                                                        fila[3] = '-';
                                                    }
                                                } else if (hijosFila[cont].nodeName == "REG_ERR_CAUSA") {
                                                    if (hijosFila[cont].childNodes.length > 0) {
                                                        fila[4] = hijosFila[cont].childNodes[0].nodeValue;
                                                    } else {
                                                        fila[4] = '-';
                                                    }
                                                } else if (hijosFila[cont].nodeName == "REG_ERR_TRAZA") {
                                                    if (hijosFila[cont].childNodes.length > 0) {
                                                        fila[5] = hijosFila[cont].childNodes[0].nodeValue;
                                                    } else {
                                                        fila[5] = '-';
                                                    }
                                                } else if (hijosFila[cont].nodeName == "REG_ERR_ID_PROC") {
                                                    if (hijosFila[cont].childNodes.length > 0) {
                                                        fila[6] = hijosFila[cont].childNodes[0].nodeValue;
                                                    } else {
                                                        fila[6] = '-';
                                                    }
                                                } else if (hijosFila[cont].nodeName == "REG_ERR_IDEN_ERR_ID") {
                                                    if (hijosFila[cont].childNodes.length > 0) {
                                                        fila[7] = hijosFila[cont].childNodes[0].nodeValue;
                                                    } else {
                                                        fila[7] = '-';
                                                    }
                                                } else if (hijosFila[cont].nodeName == "REG_ERR_ID_CLAVE") {
                                                    if (hijosFila[cont].childNodes.length > 0) {
                                                        fila[8] = hijosFila[cont].childNodes[0].nodeValue;
                                                    } else {
                                                        fila[8] = '-';
                                                    }
                                                } else if (hijosFila[cont].nodeName == "REG_ERR_NOT") {
                                                    if (hijosFila[cont].childNodes.length > 0) {
                                                        fila[9] = hijosFila[cont].childNodes[0].nodeValue;
                                                    } else {
                                                        fila[9] = '-';
                                                    }
                                                } else if (hijosFila[cont].nodeName == "REG_ERR_REV") {
                                                    if (hijosFila[cont].childNodes.length > 0) {
                                                        fila[10] = hijosFila[cont].childNodes[0].nodeValue;
                                                    } else {
                                                        fila[10] = '-';
                                                    }
                                                } else if (hijosFila[cont].nodeName == "REG_ERR_FEC_REV") {
                                                    if (hijosFila[cont].childNodes.length > 0) {
                                                        fila[11] = hijosFila[cont].childNodes[0].nodeValue;
                                                    } else {
                                                        fila[11] = '-';
                                                    }
                                                } else if (hijosFila[cont].nodeName == "REG_ERR_OBS") {
                                                    if (hijosFila[cont].childNodes.length > 0) {
                                                        fila[12] = hijosFila[cont].childNodes[0].nodeValue;
                                                    } else {
                                                        fila[12] = '-';
                                                    }
                                                } else if (hijosFila[cont].nodeName == "REG_ERR_SIS_ORIG") {
                                                    if (hijosFila[cont].childNodes.length > 0) {
                                                        fila[13] = hijosFila[cont].childNodes[0].nodeValue;
                                                    } else {
                                                        fila[13] = '-';
                                                    }
                                                } else if (hijosFila[cont].nodeName == "REG_ERR_SITU") {
                                                    if (hijosFila[cont].childNodes.length > 0) {
                                                        fila[14] = hijosFila[cont].childNodes[0].nodeValue;
                                                    } else {
                                                        fila[14] = '-';
                                                    }
                                                } else if (hijosFila[cont].nodeName == "REG_ERR_LOG") {
                                                    if (hijosFila[cont].childNodes.length > 0) {
                                                        fila[15] = hijosFila[cont].childNodes[0].nodeValue;
                                                    } else {
                                                        fila[15] = '-';
                                                    }
                                                } else if (hijosFila[cont].nodeName == "REG_ERR_EVEN") {
                                                    if (hijosFila[cont].childNodes.length > 0) {
                                                        fila[16] = hijosFila[cont].childNodes[0].nodeValue;
                                                    } else {
                                                        fila[16] = '-';
                                                    }
                                                } else if (hijosFila[cont].nodeName == "REG_ERR_ID_FLEXIA") {
                                                    if (hijosFila[cont].childNodes.length > 0) {
                                                        fila[17] = hijosFila[cont].childNodes[0].nodeValue;
                                                    } else {
                                                        fila[17] = '-';
                                                    }
                                                }
                                            }
                                            listaNueva[j] = fila;
                                            fila = new Array();
                                        }*/
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
                                        
                                        //recargarTablaErrores(listaNueva);
                                        pleaseWait('on'); 
                                        pulsarBusquedaFiltrando();
                                        pleaseWait('off');
                                    } else if (codigoOperacion == "1") {
                                        jsp_alerta("A", '<%=meLanbide53I18n.getMensaje(idiomaUsuario,"error.errorBD")%>');
                                    } else if (codigoOperacion == "2") {
                                        //jsp_alerta("A", '<//%=meLanbide53I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                                        //alert("Se ha producido un error en la retramitación. "+resultado);
                                        jsp_alerta("A", 'El error no es retramitable. No existe ningún evento de tramitación asociado a él');
                                        // jsp_alerta("A", resultado);

                                    } else if (codigoOperacion == "3") {
                                        jsp_alerta("A", '<%=meLanbide53I18n.getMensaje(idiomaUsuario,"error.pasoParametros")%>');
                                    } else {
                                        //jsp_alerta("A", '<//%=meLanbide53I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                                        jsp_alerta("A", resultado);
                                        recargarTablaErrores(listaNueva);
                                    }//if(
                                } catch (Err) {
                                    jsp_alerta("A", '<%=meLanbide53I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                                }//try-catch


                            } else {

                                //jsp_alerta('A', 'No se puede retramitar' );
                                //jsp_alerta('A', 'No es posible retramitar porque no es un error retramitable o ya ha sido retramitado');
                                if ((retramitado != "null" && retramitado != 'N' && retramitado != 'No') || (xmlSolicitud == 'null'|| xmlSolicitud == '')) {
                                    jsp_alerta('A', 'No es posible retramitar porque no es un error retramitable o ya ha sido retramitado');
                                } else if (tieneOK == 1){
                                    if (registro != 'null' && registro != '' && registro != 'no_existe'){
                                        jsp_alerta('A', 'El documento ya tiene OK en Estadísticas y al menos está en el registro ' + registro);
                                    } else {
                                        jsp_alerta('A', 'El documento ya tiene OK en Estadísticas');
                                    }
                                } else {
                                    jsp_alerta('A', 'El documento ya está en el registro ' + registro);
                                }

                            }
                        } else {
                            recargarTablaErrores(listaNueva);
                            jsp_alerta('A', '<%=meLanbide53I18n.getMensaje(idiomaUsuario, "msg.msjNoSelecFila")%>');
                            //jsp_alerta("A",'<%=meLanbide53I18n.getMensaje(idiomaUsuario,"error.errorNoSeleccionadoUno")%>');
                        }
                    }

                    function pulsarEliminarError() {
                        //getSeleccionados();
                    
                        if (tablaErrores.selectedIndex != -1) {
                        //if(listaIdRegistroSeleccionados.length == 1){
                            var resultado = jsp_alerta('', '<%=meLanbide53I18n.getMensaje(idiomaUsuario, "msg.preguntaEliminar")%>');
                            if (resultado == 1) {

                                var ajax = getXMLHttpRequest();
                                var nodos = null;
                                var CONTEXT_PATH = '<%=request.getContextPath()%>';
                                var url = CONTEXT_PATH + "/PeticionModuloIntegracion.do";
                                var parametros = "";
                                var control = new Date();
                                parametros = 'tarea=preparar&modulo=MELANBIDE53&operacion=eliminarError&tipo=0&id=' + listaErrores[tablaErrores.selectedIndex][0] + '&control=' + control.getTime();
                                //parametros = 'tarea=preparar&modulo=MELANBIDE53&operacion=eliminarError&tipo=0&id=' + listaIdRegistroSeleccionados[0] + '&control=' + control.getTime();
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
                                                if (hijosFila[cont].nodeName == "REG_ERR_ID") {
                                                    if (hijosFila[cont].childNodes.length > 0) {
                                                        fila[0] = hijosFila[cont].childNodes[0].nodeValue;
                                                    } else {
                                                        fila[0] = '-';
                                                    }
                                                } else if (hijosFila[cont].nodeName == "REG_ERR_FEC_ERROR") {
                                                    if (hijosFila[cont].childNodes.length > 0) {
                                                        fila[1] = hijosFila[cont].childNodes[0].nodeValue;
                                                    } else {
                                                        fila[1] = '-';
                                                    }
                                                } else if (hijosFila[cont].nodeName == "REG_ERR_MEN") {
                                                    if (hijosFila[cont].childNodes.length > 0) {
                                                        fila[2] = hijosFila[cont].childNodes[0].nodeValue;
                                                    } else {
                                                        fila[2] = '-';
                                                    }
                                                } else if (hijosFila[cont].nodeName == "REG_ERR_EXCEP_MEN") {
                                                    if (hijosFila[cont].childNodes.length > 0) {
                                                        fila[3] = hijosFila[cont].childNodes[0].nodeValue;
                                                    } else {
                                                        fila[3] = '-';
                                                    }
                                                } else if (hijosFila[cont].nodeName == "REG_ERR_CAUSA") {
                                                    if (hijosFila[cont].childNodes.length > 0) {
                                                        fila[4] = hijosFila[cont].childNodes[0].nodeValue;
                                                    } else {
                                                        fila[4] = '-';
                                                    }
                                                } else if (hijosFila[cont].nodeName == "REG_ERR_TRAZA") {
                                                    if (hijosFila[cont].childNodes.length > 0) {
                                                        fila[5] = hijosFila[cont].childNodes[0].nodeValue;
                                                    } else {
                                                        fila[5] = '-';
                                                    }
                                                } else if (hijosFila[cont].nodeName == "REG_ERR_ID_PROC") {
                                                    if (hijosFila[cont].childNodes.length > 0) {
                                                        fila[6] = hijosFila[cont].childNodes[0].nodeValue;
                                                    } else {
                                                        fila[6] = '-';
                                                    }
                                                } else if (hijosFila[cont].nodeName == "REG_ERR_IDEN_ERR_ID") {
                                                    if (hijosFila[cont].childNodes.length > 0) {
                                                        fila[7] = hijosFila[cont].childNodes[0].nodeValue;
                                                    } else {
                                                        fila[7] = '-';
                                                    }
                                                } else if (hijosFila[cont].nodeName == "REG_ERR_ID_CLAVE") {
                                                    if (hijosFila[cont].childNodes.length > 0) {
                                                        fila[8] = hijosFila[cont].childNodes[0].nodeValue;
                                                    } else {
                                                        fila[8] = '-';
                                                    }
                                                } else if (hijosFila[cont].nodeName == "REG_ERR_NOT") {
                                                    if (hijosFila[cont].childNodes.length > 0) {
                                                        fila[9] = hijosFila[cont].childNodes[0].nodeValue;
                                                    } else {
                                                        fila[9] = '-';
                                                    }
                                                } else if (hijosFila[cont].nodeName == "REG_ERR_REV") {
                                                    if (hijosFila[cont].childNodes.length > 0) {
                                                        fila[10] = hijosFila[cont].childNodes[0].nodeValue;
                                                    } else {
                                                        fila[10] = '-';
                                                    }
                                                } else if (hijosFila[cont].nodeName == "REG_ERR_FEC_REV") {
                                                    if (hijosFila[cont].childNodes.length > 0) {
                                                        fila[11] = hijosFila[cont].childNodes[0].nodeValue;
                                                    } else {
                                                        fila[11] = '-';
                                                    }
                                                } else if (hijosFila[cont].nodeName == "REG_ERR_OBS") {
                                                    if (hijosFila[cont].childNodes.length > 0) {
                                                        fila[12] = hijosFila[cont].childNodes[0].nodeValue;
                                                    } else {
                                                        fila[12] = '-';
                                                    }
                                                } else if (hijosFila[cont].nodeName == "REG_ERR_SIS_ORIG") {
                                                    if (hijosFila[cont].childNodes.length > 0) {
                                                        fila[13] = hijosFila[cont].childNodes[0].nodeValue;
                                                    } else {
                                                        fila[13] = '-';
                                                    }
                                                } else if (hijosFila[cont].nodeName == "REG_ERR_SITU") {
                                                    if (hijosFila[cont].childNodes.length > 0) {
                                                        fila[14] = hijosFila[cont].childNodes[0].nodeValue;
                                                    } else {
                                                        fila[14] = '-';
                                                    }
                                                } else if (hijosFila[cont].nodeName == "REG_ERR_LOG") {
                                                    if (hijosFila[cont].childNodes.length > 0) {
                                                        fila[15] = hijosFila[cont].childNodes[0].nodeValue;
                                                    } else {
                                                        fila[15] = '-';
                                                    }
                                                } else if (hijosFila[cont].nodeName == "REG_ERR_EVEN") {
                                                    if (hijosFila[cont].childNodes.length > 0) {
                                                        fila[16] = hijosFila[cont].childNodes[0].nodeValue;
                                                    } else {
                                                        fila[16] = '-';
                                                    }
                                                } else if (hijosFila[cont].nodeName == "REG_ERR_ID_FLEXIA") {
                                                    if (hijosFila[cont].childNodes.length > 0) {
                                                        fila[17] = hijosFila[cont].childNodes[0].nodeValue;
                                                    } else {
                                                        fila[17] = '-';
                                                    }
                                                } else if (hijosFila[cont].nodeName == "REG_ERR_SOLICITUD") {
                                                    if (hijosFila[cont].childNodes.length > 0) {
                                                        fila[18] = hijosFila[cont].childNodes[0].nodeValue;
                                                        fila[18] = fila[18].replace(/>/g, "&gt;").replace(/</g, "&lt;").replace(/"/g, "&quot;");
                                                    } else {
                                                        fila[18] = '';
                                                    }
                                                } else if (hijosFila[cont].nodeName == "REG_ERR_RETRAMIT_SN") {
                                                    if (hijosFila[cont].childNodes.length > 0) {
                                                        fila[19] = hijosFila[cont].childNodes[0].nodeValue;
                                                        if (fila[9] != "" && fila[9] == '<%=ConstantesMeLanbide53.STANDFOR_SI_S%>')
                                                            fila[9] = '<%=meLanbide53I18n.getMensaje(idiomaUsuario,"label.error.STANDFOR_SI_S")%>';
                                                        else if (fila[9] != "" && fila[9] == '<%=ConstantesMeLanbide53.STANDFOR_NO_N%>')
                                                            fila[9] = '<%=meLanbide53I18n.getMensaje(idiomaUsuario,"label.error.STANDFOR_NO_N")%>';
                                                    } else {
                                                        fila[19] = '-';
                                                    }
                                                } else if (hijosFila[cont].nodeName == "REG_ERR_FEC_RETRAMIT") {
                                                    if (hijosFila[cont].childNodes.length > 0) {
                                                        fila[20] = hijosFila[cont].childNodes[0].nodeValue;
                                                    } else {
                                                        fila[20] = '-';
                                                    }
                                                } else if (hijosFila[cont].nodeName == "REG_ERR_RESULT_WS") {
                                                    if (hijosFila[cont].childNodes.length > 0) {
                                                        fila[21] = hijosFila[cont].childNodes[0].nodeValue;
                                                    } else {
                                                        fila[21] = '-';
                                                    }
                                                } else if (hijosFila[cont].nodeName == "REG_ERR_IMPACTO") {
                                                    if (hijosFila[cont].childNodes.length > 0) {
                                                        fila[22] = hijosFila[cont].childNodes[0].nodeValue;
                                                        if (fila[22] != "" && fila[22] == '<%=ConstantesMeLanbide53.STANDFOR_SI_S%>')
                                                            fila[22] = '<%=meLanbide53I18n.getMensaje(idiomaUsuario,"label.error.STANDFOR_SI_S")%>';
                                                        else if (fila[22] != "" && fila[22] == '<%=ConstantesMeLanbide53.STANDFOR_NO_N%>')
                                                            fila[22] = '<%=meLanbide53I18n.getMensaje(idiomaUsuario,"label.error.STANDFOR_NO_N")%>';

                                                    } else {
                                                        fila[22] = '-';
                                                    }
                                                } else if (hijosFila[cont].nodeName == "REG_ERR_TRATADO") {
                                                    if (hijosFila[cont].childNodes.length > 0) {
                                                        fila[23] = hijosFila[cont].childNodes[0].nodeValue;
                                                        if (fila[23] != "" && fila[23] == '<%=ConstantesMeLanbide53.STANDFOR_SI_S%>')
                                                            fila[23] = '<%=meLanbide53I18n.getMensaje(idiomaUsuario,"label.error.STANDFOR_SI_S")%>';
                                                        else if (fila[23] != "" && fila[23] == '<%=ConstantesMeLanbide53.STANDFOR_NO_N%>')
                                                            fila[23] = '<%=meLanbide53I18n.getMensaje(idiomaUsuario,"label.error.STANDFOR_NO_N")%>';
                                                    } else {
                                                        fila[23] = '-';
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
                                    }//if(
                                } catch (Err) {
                                    jsp_alerta("A", '<%=meLanbide53I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                                }//try-catch
                            }
                        } else {
                            jsp_alerta('A', '<%=meLanbide53I18n.getMensaje(idiomaUsuario, "msg.msjNoSelecFila")%>');
                            //jsp_alerta("A",'<%=meLanbide53I18n.getMensaje(idiomaUsuario,"error.errorNoSeleccionadoUno")%>');
                        }
                    }

                    function recargarTablaErrores(result) {
                        pleaseWait('on');
                        var fila;
                        listaErrores = new Array();
                        listaErroresTabla = new Array();

                        crearTabla();
                        
                        //var contador=0;
                        for (var i = 1; i < result.length; i++) {
                            fila = result[i];
                            listaErrores[i - 1] = fila;
                            
                            listaErroresTabla[i - 1] = [fila[0], fila[1], (fila[9] == 'null' ? "" : fila[9]), (fila[10] == 'null' ? "" : fila[10]), (fila[6] == 'null' ? "" : fila[6]), (fila[7] == 'null' ? "" : fila[7]), (fila[13] == 'null' ? "" : fila[13]), (fila[17] == 'null' ? "" : fila[17]), (fila[8] == 'null' ? "" : fila[8]), (fila[16] == 'null' ? "" : fila[16]), (fila[2] != null && fila[2] != 'null' ? fila[2] : ""), (fila[18] != null && fila[18] != 'null' && fila[18] != "" ? "1" : ""), (fila[19] != null && fila[19] != 'null' ? (fila[19] == 'N' ? "No" : "Sí") : ""), (fila[20] != null && fila[20] != 'null' ? fila[20] : ""), (fila[21] != null && fila[21] != 'null' ? fila[21] : ""), (fila[22] != null && fila[22] != 'null' ? fila[22] : ""), (fila[23] != null && fila[23] != 'null' ? fila[23] : "")];
                            
                            //listaErrores[i - 1] = [check('false',contador), fila[0], fila[1], fila[2], fila[3], fila[4], fila[5], fila[6], fila[7], fila[8], fila[9], fila[10], fila[11], fila[12], fila[13], fila[14], fila[15], fila[16], fila[17], fila[18], fila[19], fila[20], fila[21], fila[22], fila[23] ];
                            //listaErroresTabla[i - 1] = [check('false',contador), fila[0], fila[1], (fila[9] == 'null' ? "" : fila[9]), (fila[10] == 'null' ? "" : fila[10]), (fila[6] == 'null' ? "" : fila[6]), (fila[7] == 'null' ? "" : fila[7]), (fila[13] == 'null' ? "" : fila[13]), (fila[17] == 'null' ? "" : fila[17]), (fila[8] == 'null' ? "" : fila[8]), (fila[16] == 'null' ? "" : fila[16]), (fila[2] != null && fila[2] != 'null' ? fila[2] : ""), (fila[18] != null && fila[18] != 'null' && fila[18] != "" ? "1" : ""), (fila[19] != null && fila[19] != 'null' ? (fila[19] == 'N' ? "No" : "Sí") : ""), (fila[20] != null && fila[20] != 'null' ? fila[20] : ""), (fila[21] != null && fila[21] != 'null' ? fila[21] : ""), (fila[22] != null && fila[22] != 'null' ? fila[22] : ""), (fila[23] != null && fila[23] != 'null' ? fila[23] : "")];
                            //contador++;
                        }

                        pleaseWait('off');
                        listaSelErrOriginal = listaErroresTabla;
                        tablaErrores.lineas = listaErroresTabla;
                        tablaErrores.displayTablaConTooltips(listaErroresTabla);
                        //document.getElementById('listaErrores').children[0].children[1].children[0].children[0].ondblclick = function(event){
                        //        pulsarModificarError(event);
                        if (navigator.appName.indexOf("Internet Explorer") != -1 || (navigator.appName.indexOf("Netscape") != -1 && navigator.appCodeName.match(/Mozilla/))) {
                            try {
                                var div = document.getElementById('listaErrores');
                                //div.children[0].children[0].children[0].children[0].children[0].children[0].children[0].children[0].children[0].children[0].children[0].children[0].children[0].children[1].style.width = '100%';
                                //div.children[0].children[1].children[0].children[0].children[0].children[0].children[0].children[0].children[0].children[0].children[0].children[1].style.width = '100%';
                                if (navigator.appName.indexOf("Internet Explorer") != -1 && !(navigator.userAgent.match(/compatible/))) {
                                    div.children[0].children[0].children[0].children[0].children[0].children[0].children[1].style.width = '100%';
                                    div.children[0].children[1].children[0].children[0].children[0].children[0].style.width = '100%';
                                } else {
                                    div.children[0].children[0].children[0].children[0].children[0].style.width = '100%';
                                    div.children[0].children[1].children[0].style.width = '100%';
                                }
                            } catch (err) {
                                jsp_alerta("A", '<%=meLanbide53I18n.getMensaje(idiomaUsuario, "msg.errorGen")%>' + ' - ' + err.message);
                            }
                        }
                        
                        //quitarAtributosTablaErrores();
                    
                        //habilitarBoton('#btnRetramitacionMasiva');
                        
                    }

                    function pulsarBusquedaFiltrando() {
                        if (validarFiltrosBusqueda()) {
                            var ajax = getXMLHttpRequest();
                            var nodos = null;
                            var nodosNumRegTotal = null;
                            var CONTEXT_PATH = '<%=request.getContextPath()%>'
                            var url = CONTEXT_PATH + "/PeticionModuloIntegracion.do";
                            var parametros = "";
                            var control = new Date();
                            parametros = "tarea=preparar&modulo=MELANBIDE53&operacion=busquedaFiltrandoListaErrores&tipo=0&control=" + control.getTime()
                                    + "&idprocedimiento_busq=" + document.getElementById('idprocedimiento_busq').value
                                    + "&identificadorError_busq=" + document.getElementById('identificadorError_busq').value
                                    + "&clave_busq=" + document.getElementById('clave_busq').value
                                    //+ "&check_notificado="+ prepararFiltroChecked('check_notificado')
                                    //+ "&check_verficado="+ prepararFiltroChecked('check_verficado')
                                    + "&check_notificado=" + document.getElementById('codListaOpcionNotificado').value
                                    + "&check_retramitado=" + document.getElementById('codListaOpcionRetramitado').value
                                    + "&check_verficado=" + document.getElementById('codListaOpcionRevisado').value
                                    + "&sistemaOrigen_busq=" + document.getElementById('sistemaOrigen_busq').value
                                    + "&numeroExpediente_busq=" + document.getElementById('numeroExpediente_busq').value
                                    + "&meLanbide53Fecha_busqE=" + document.getElementById('meLanbide53Fecha_busqE').value
                                    + "&meLanbide53Fecha_busqS=" + document.getElementById('meLanbide53Fecha_busqS').value
                                    + "&hora_entrada_busq=" + document.getElementById('hora_entrada_busq').value
                                    + "&hora_entrada_busqF=" + document.getElementById('hora_entrada_busqF').value
                                    + "&meLanbide53Fecha_RevbusqE=" + document.getElementById('meLanbide53Fecha_RevbusqE').value
                                    + "&meLanbide53Fecha_RevbusqF=" + document.getElementById('meLanbide53Fecha_RevbusqF').value
                                    + "&identificadorErrorBD_busq=" + document.getElementById('identificadorErrorBD_busq').value
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
                                nodosNumRegTotal = xmlDoc.getElementsByTagName("NUMTOTALREGISTROS");
                                var elementoRegTotal = nodosNumRegTotal[0];
                                if (elementoRegTotal != null && elementoRegTotal.childNodes != null) {
                                    numeroTotalRegistros = elementoRegTotal.childNodes[0].nodeValue;
                                }
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
                                            if (hijosFila[cont].nodeName == "REG_ERR_ID") {
                                                if (hijosFila[cont].childNodes.length > 0) {
                                                    fila[0] = hijosFila[cont].childNodes[0].nodeValue;
                                                } else {
                                                    fila[0] = '-';
                                                }
                                            } else if (hijosFila[cont].nodeName == "REG_ERR_FEC_ERROR") {
                                                if (hijosFila[cont].childNodes.length > 0) {
                                                    fila[1] = hijosFila[cont].childNodes[0].nodeValue;

                                                } else {
                                                    fila[1] = '-';
                                                }
                                            } else if (hijosFila[cont].nodeName == "REG_ERR_MEN") {
                                                if (hijosFila[cont].childNodes.length > 0) {
                                                    fila[2] = hijosFila[cont].childNodes[0].nodeValue;
                                                } else {
                                                    fila[2] = '-';
                                                }
                                            } else if (hijosFila[cont].nodeName == "REG_ERR_EXCEP_MEN") {
                                                if (hijosFila[cont].childNodes.length > 0) {
                                                    fila[3] = hijosFila[cont].childNodes[0].nodeValue;
                                                } else {
                                                    fila[3] = '-';
                                                }
                                            } else if (hijosFila[cont].nodeName == "REG_ERR_CAUSA") {
                                                if (hijosFila[cont].childNodes.length > 0) {
                                                    fila[4] = hijosFila[cont].childNodes[0].nodeValue;
                                                } else {
                                                    fila[4] = '-';
                                                }
                                            } else if (hijosFila[cont].nodeName == "REG_ERR_TRAZA") {
                                                if (hijosFila[cont].childNodes.length > 0) {
                                                    fila[5] = hijosFila[cont].childNodes[0].nodeValue;
                                                } else {
                                                    fila[5] = '-';
                                                }
                                            } else if (hijosFila[cont].nodeName == "REG_ERR_ID_PROC") {
                                                if (hijosFila[cont].childNodes.length > 0) {
                                                    fila[6] = hijosFila[cont].childNodes[0].nodeValue;
                                                } else {
                                                    fila[6] = '-';
                                                }
                                            } else if (hijosFila[cont].nodeName == "REG_ERR_IDEN_ERR_ID") {
                                                if (hijosFila[cont].childNodes.length > 0) {
                                                    fila[7] = hijosFila[cont].childNodes[0].nodeValue;
                                                } else {
                                                    fila[7] = '-';
                                                }
                                            } else if (hijosFila[cont].nodeName == "REG_ERR_ID_CLAVE") {
                                                if (hijosFila[cont].childNodes.length > 0) {
                                                    fila[8] = hijosFila[cont].childNodes[0].nodeValue;
                                                } else {
                                                    fila[8] = '-';
                                                }
                                            } else if (hijosFila[cont].nodeName == "REG_ERR_NOT") {
                                                if (hijosFila[cont].childNodes.length > 0) {

                                                    fila[9] = hijosFila[cont].childNodes[0].nodeValue;
                                                    if (fila[9] != "" && fila[9] == '<%=ConstantesMeLanbide53.STANDFOR_SI_S%>')
                                                        fila[9] = '<%=meLanbide53I18n.getMensaje(idiomaUsuario,"label.error.STANDFOR_SI_S")%>';
                                                    else if (fila[9] != "" && fila[9] == '<%=ConstantesMeLanbide53.STANDFOR_NO_N%>')
                                                        fila[9] = '<%=meLanbide53I18n.getMensaje(idiomaUsuario,"label.error.STANDFOR_NO_N")%>';
                                                } else {
                                                    fila[9] = '-';
                                                }
                                            } else if (hijosFila[cont].nodeName == "REG_ERR_REV") {
                                                if (hijosFila[cont].childNodes.length > 0) {

                                                    fila[10] = hijosFila[cont].childNodes[0].nodeValue;
                                                    if (fila[10] != "" && fila[10] == '<%=ConstantesMeLanbide53.STANDFOR_SI_S%>')
                                                        fila[10] = '<%=meLanbide53I18n.getMensaje(idiomaUsuario,"label.error.STANDFOR_SI_S")%>';
                                                    else if (fila[10] != "" && fila[10] == '<%=ConstantesMeLanbide53.STANDFOR_NO_N%>')
                                                        fila[10] = '<%=meLanbide53I18n.getMensaje(idiomaUsuario,"label.error.STANDFOR_NO_N")%>';
                                                } else {
                                                    fila[10] = '-';
                                                }
                                            } else if (hijosFila[cont].nodeName == "REG_ERR_FEC_REV") {
                                                if (hijosFila[cont].childNodes.length > 0) {
                                                    fila[11] = hijosFila[cont].childNodes[0].nodeValue;
                                                } else {
                                                    fila[11] = '-';
                                                }
                                            } else if (hijosFila[cont].nodeName == "REG_ERR_OBS") {
                                                if (hijosFila[cont].childNodes.length > 0) {
                                                    fila[12] = hijosFila[cont].childNodes[0].nodeValue;
                                                } else {
                                                    fila[12] = '-';
                                                }
                                            } else if (hijosFila[cont].nodeName == "REG_ERR_SIS_ORIG") {
                                                if (hijosFila[cont].childNodes.length > 0) {
                                                    fila[13] = hijosFila[cont].childNodes[0].nodeValue;
                                                } else {
                                                    fila[13] = '-';
                                                }
                                            } else if (hijosFila[cont].nodeName == "REG_ERR_SITU") {
                                                if (hijosFila[cont].childNodes.length > 0) {
                                                    fila[14] = hijosFila[cont].childNodes[0].nodeValue;
                                                } else {
                                                    fila[14] = '-';
                                                }
                                            } else if (hijosFila[cont].nodeName == "REG_ERR_LOG") {
                                                if (hijosFila[cont].childNodes.length > 0) {
                                                    fila[15] = hijosFila[cont].childNodes[0].nodeValue;
                                                } else {
                                                    fila[15] = '-';
                                                }
                                            } else if (hijosFila[cont].nodeName == "REG_ERR_EVEN") {
                                                if (hijosFila[cont].childNodes.length > 0) {
                                                    fila[16] = hijosFila[cont].childNodes[0].nodeValue;
                                                } else {
                                                    fila[16] = '-';
                                                }
                                            } else if (hijosFila[cont].nodeName == "REG_ERR_ID_FLEXIA") {
                                                if (hijosFila[cont].childNodes.length > 0) {
                                                    fila[17] = hijosFila[cont].childNodes[0].nodeValue;
                                                } else {
                                                    fila[17] = '-';
                                                }
                                            } else if (hijosFila[cont].nodeName == "REG_ERR_SOLICITUD") {
                                                if (hijosFila[cont].childNodes.length > 0) {
                                                    fila[18] = hijosFila[cont].childNodes[0].nodeValue;
                                                    fila[18] = fila[18].replace(/>/g, "&gt;").replace(/</g, "&lt;").replace(/"/g, "&quot;");
                                                } else {
                                                    fila[18] = '';
                                                }
                                            } else if (hijosFila[cont].nodeName == "REG_ERR_RETRAMIT_SN") {
                                                if (hijosFila[cont].childNodes.length > 0) {
                                                    fila[19] = hijosFila[cont].childNodes[0].nodeValue;
                                                    if (fila[9] != "" && fila[9] == '<%=ConstantesMeLanbide53.STANDFOR_SI_S%>')
                                                        fila[9] = '<%=meLanbide53I18n.getMensaje(idiomaUsuario,"label.error.STANDFOR_SI_S")%>';
                                                    else if (fila[9] != "" && fila[9] == '<%=ConstantesMeLanbide53.STANDFOR_NO_N%>')
                                                        fila[9] = '<%=meLanbide53I18n.getMensaje(idiomaUsuario,"label.error.STANDFOR_NO_N")%>';
                                                } else {
                                                    fila[19] = '-';
                                                }
                                            } else if (hijosFila[cont].nodeName == "REG_ERR_FEC_RETRAMIT") {
                                                if (hijosFila[cont].childNodes.length > 0) {
                                                    fila[20] = hijosFila[cont].childNodes[0].nodeValue;
                                                } else {
                                                    fila[20] = '-';
                                                }
                                            } else if (hijosFila[cont].nodeName == "REG_ERR_RESULT_WS") {
                                                if (hijosFila[cont].childNodes.length > 0) {
                                                    fila[21] = hijosFila[cont].childNodes[0].nodeValue;
                                                } else {
                                                    fila[21] = '-';
                                                }
                                            } else if (hijosFila[cont].nodeName == "REG_ERR_IMPACTO") {
                                                if (hijosFila[cont].childNodes.length > 0) {
                                                    fila[22] = hijosFila[cont].childNodes[0].nodeValue;
                                                    if (fila[22] != "" && fila[22] == '<%=ConstantesMeLanbide53.STANDFOR_SI_S%>')
                                                        fila[22] = '<%=meLanbide53I18n.getMensaje(idiomaUsuario,"label.error.STANDFOR_SI_S")%>';
                                                    else if (fila[22] != "" && fila[22] == '<%=ConstantesMeLanbide53.STANDFOR_NO_N%>')
                                                        fila[22] = '<%=meLanbide53I18n.getMensaje(idiomaUsuario,"label.error.STANDFOR_NO_N")%>';

                                                } else {
                                                    fila[22] = '-';
                                                }
                                            } else if (hijosFila[cont].nodeName == "REG_ERR_TRATADO") {
                                                if (hijosFila[cont].childNodes.length > 0) {
                                                    fila[23] = hijosFila[cont].childNodes[0].nodeValue;
                                                    if (fila[23] != "" && fila[23] == '<%=ConstantesMeLanbide53.STANDFOR_SI_S%>')
                                                        fila[23] = '<%=meLanbide53I18n.getMensaje(idiomaUsuario,"label.error.STANDFOR_SI_S")%>';
                                                    else if (fila[23] != "" && fila[23] == '<%=ConstantesMeLanbide53.STANDFOR_NO_N%>')
                                                        fila[23] = '<%=meLanbide53I18n.getMensaje(idiomaUsuario,"label.error.STANDFOR_NO_N")%>';
                                                } else {
                                                    fila[23] = '-';
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
                                //}//if(
                            } catch (Err) {
                                jsp_alerta("A", '<%=meLanbide53I18n.getMensaje(idiomaUsuario,"error.errorGen")%>' + ' - ' + Err.message);
                            }//try-catch
                        } else {
                            jsp_alerta('A', mensajeValidacion);
                        }
                    }

                    function prepararFiltroChecked(idElement) {
                        if (document.getElementById(idElement) != null && document.getElementById(idElement).checked) {
                            return '<%=ConstantesMeLanbide53.BOOLEAN_TRUE%>';
                        } else
                            return "";
                    }

                    function validarFiltrosBusqueda() {
                        var result = true;
                        if (document.getElementById("meLanbide53Fecha_busqE").value != "" && document.getElementById("meLanbide53Fecha_busqS").value != "")
                        {
                            var fechaentrada = document.getElementById("meLanbide53Fecha_busqE").value.split("/");
                            var fechasalida = document.getElementById("meLanbide53Fecha_busqS").value.split("/");
                            fechaentrada = fechaentrada[2] + fechaentrada[1] + fechaentrada[0];
                            fechasalida = fechasalida[2] + fechasalida[1] + fechasalida[0];
                            if (fechaentrada > fechasalida) {
                                mensajeValidacion = '<%=meLanbide53I18n.getMensaje(idiomaUsuario, "msg.busqueda.errorintervalosfechaerror")%>';
                                document.getElementById("meLanbide53Fecha_busqE").select();
                                return false;
                            }
                        }
                        if (document.getElementById("hora_entrada_busq").value != "" && document.getElementById("hora_entrada_busqF").value != "")
                        {
                            var horaentrada = document.getElementById("hora_entrada_busq").value;
                            var horaentradaF = document.getElementById("hora_entrada_busqF").value;
                            if (horaentrada > horaentradaF) {
                                mensajeValidacion = '<%=meLanbide53I18n.getMensaje(idiomaUsuario, "msg.busqueda.errorintervaloshora")%>';
                                document.getElementById("hora_entrada_busq").select();
                                return false;
                            }
                        }
                        if (document.getElementById("meLanbide53Fecha_RevbusqE").value != "" && document.getElementById("meLanbide53Fecha_RevbusqF").value != "")
                        {
                            var fechaentrada = document.getElementById("meLanbide53Fecha_RevbusqE").value.split("/");
                            var fechasalida = document.getElementById("meLanbide53Fecha_RevbusqF").value.split("/");
                            fechaentrada = fechaentrada[2] + fechaentrada[1] + fechaentrada[0];
                            fechasalida = fechasalida[2] + fechasalida[1] + fechasalida[0];
                            if (fechaentrada > fechasalida) {
                                mensajeValidacion = '<%=meLanbide53I18n.getMensaje(idiomaUsuario, "msg.busqueda.errorintervalosfecharevision")%>';
                                document.getElementById("meLanbide53Fecha_RevbusqE").select();
                                return false;
                            }
                        }
                        if (document.getElementById('identificadorErrorBD_busq').value != "" && document.getElementById('identificadorErrorBD_busq').value != "")
                        {
                            if (!validarSoloNumerosEnteros(document.getElementById('identificadorErrorBD_busq'))) {
                                mensajeValidacion = '<%=meLanbide53I18n.getMensaje(idiomaUsuario, "msg.busqueda.soloNumerosEnteros")%>';
                                document.getElementById("identificadorErrorBD_busq").select();
                                return false;
                            }
                        }

                        return result;
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

                    function validarSoloNumerosEnteros(objeto)
                    {
                        var numeros = '0123456789';
                        var caracter_contenido = '';
                        var contenido = objeto.value;
                        for (var i = 0; i < contenido.length; i++)
                        {
                            caracter_contenido = contenido.charAt(i);   // retorna el caracter especificado
                            if (numeros.indexOf(caracter_contenido, 0) == -1) //No es un numero
                            {
                                //objeto.focus();
                                return false;
                            }//del if
                        }//del for
                        return true;
                    }//de la funcion

                    function estadisticas() {
                        var control = new Date();
                        lanzarPopUpModal('<%=request.getContextPath()%>/PeticionModuloIntegracion.do?tarea=preparar&modulo=MELANBIDE53&operacion=cargarEstadisticas&tipo=0&nuevo=0&control=' + control.getTime(), 340, 550, 'no', 'yes', function (result) {});
                    }
                    
                    //Retramitación masiva_______________________________________________________________inicio
                    
                    /*function check(seleccionado,contador){
                        var fila = '';
                        if(seleccionado=="true"){
                            fila+= '<input type="checkbox" value="true" checked='+seleccionado+' id="Seleccionado'+contador+'" />';
                        } else{
                            fila+="<input type='checkbox' value='true' id='Seleccionado"+contador+"' />";
                        }
                            fila += '&nbsp;';
                        return fila;
                    }

                    function getSeleccionados(){
                        var c=0;
                        var j=0;

                        listaSeleccionados=new Array();
                        listaIdRegistroSeleccionados=new Array();
                        listaRetramitadoSeleccionados=new Array();
                        listaXmlSolicitudSeleccionados=new Array();
                        listaOidSeleccionados=new Array();
                        listaProcedimientoSeleccionados=new Array();
                        for(c=0; listaErrores != null && c<listaErrores.length; c++){
                            if (document.getElementById("Seleccionado"+c)!=null){
                               if (document.getElementById("Seleccionado"+c).checked){
                                    var id = listaErrores[c][1];
                                    var retram = listaErrores[c][20];
                                    var xmlSolic = listaErrores[c][19];
                                    var oid = listaErrores[c][9];
                                    var proc = listaErrores[c][7];
                                    listaIdRegistroSeleccionados[j]=id;
                                    listaRetramitadoSeleccionados[j]=retram;
                                    listaXmlSolicitudSeleccionados[j]=xmlSolic;
                                    listaOidSeleccionados[j]=oid;
                                    listaProcedimientoSeleccionados[j]=proc;
                                    listaSeleccionados[j]="1";
                                    j++;
                                }
                            }
                        }
                    }

                    function pulsarSeleccionarTodos(){
                        var nodoCheck = document.getElementsByTagName("input");
                        var varCheck = "true";

                        for (i=0; i<nodoCheck.length; i++){
                            if (nodoCheck[i].type == "checkbox" && nodoCheck[i].disabled == false) {
                                nodoCheck[i].checked = varCheck;
                            }
                        }
                    }

                    function pulsarRetramitacionMasiva(){ 

                        getSeleccionados();

                        if(listaIdRegistroSeleccionados.length > 0){

                            deshabilitarBoton('#btnRetramitacionMasiva');
                            //deshabilitarBoton('#botonBusqueda');

                            var resultado = jsp_alerta('', '<%=meLanbide53I18n.getMensaje(idiomaUsuario, "msg.preguntaRetramitar")%>');
                            if (resultado == 1) {

                                var stringListaErroresJSON = JSON.stringify(listaIdRegistroSeleccionados);

                                var ajax = getXMLHttpRequest();
                                var nodos = null;
                                var nodosNumRegTotal = null;
                                var CONTEXT_PATH = '<%=request.getContextPath()%>';
                                var url = CONTEXT_PATH + "/PeticionModuloIntegracion.do";
                                var parametros = "";
                                var control = new Date();
                                parametros = "tarea=preparar&modulo=MELANBIDE53&operacion=retramitacionMasiva&tipo=0&control=" + control.getTime()
                                        + "&listaErroresSeleccionados=" + stringListaErroresJSON
                                        ;

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
                                        }
                                    }

                                    nodos = xmlDoc.getElementsByTagName("RESPUESTA");
                                    nodosNumRegTotal = xmlDoc.getElementsByTagName("NUMEXPEDIENTESMARCADOS");
                                    var elementoRegTotal = nodosNumRegTotal[0];
                                    var numExpedientesMarc = 0;

                                    if (elementoRegTotal != null && elementoRegTotal.childNodes != null) {
                                        numExpedientesMarc = elementoRegTotal.childNodes[0].nodeValue;
                                    }
                                    var elemento = nodos[0];
                                    var hijos = elemento.childNodes;
                                    var codigoOperacion = null;

                                    for (j = 0; hijos != null && j < hijos.length; j++) {
                                        if (hijos[j].nodeName == "CODIGO_OPERACION") {
                                            codigoOperacion = hijos[j].childNodes[0].nodeValue;
                                        }

                                    }

                                    if (codigoOperacion == "0") {
                                        jsp_alerta("A", '<%=meLanbide53I18n.getMensaje(idiomaUsuario,"msg.retramitados")%>');
                                        pulsarBusquedaFiltrando();
                                    } else if (codigoOperacion == "1") {
                                        jsp_alerta("A", '<%=meLanbide53I18n.getMensaje(idiomaUsuario,"error.errorBD")%>');
                                    } else if (codigoOperacion == "2") {
                                        jsp_alerta("A", '<%=meLanbide53I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                                    } else if (codigoOperacion == "3") {
                                        jsp_alerta("A", '<%=meLanbide53I18n.getMensaje(idiomaUsuario,"error.pasoParametros")%>');
                                    } else {
                                        jsp_alerta("A", '<%=meLanbide53I18n.getMensaje(idiomaUsuario,"error.errorGen")%>');
                                    }
                                    //}//if(
                                } catch (Err) {
                                    jsp_alerta("A", '<%=meLanbide53I18n.getMensaje(idiomaUsuario,"error.errorGen")%>' + ' - ' + Err.message);
                                }
                            }else{
                                habilitarBoton('#btnRetramitacionMasiva');
                                //habilitarBoton('#botonBusqueda');
                            }
                        }else{
                            jsp_alerta("A",'<%=meLanbide53I18n.getMensaje(idiomaUsuario,"error.errorNoSeleccionados")%>');
                        }
                    }

                    function quitarAtributosTablaErrores() {
                        $('td').removeAttr('title');
                        $('td').removeAttr('onclick');
                        $('td').removeAttr('ondblclick');
                        $("#listaErrores tr").css('cursor', 'default');
                    }*/

                    function habilitarBoton(elemento){
                        $(elemento).prop('disabled', false);
                    }
                    function deshabilitarBoton(elemento){
                        $(elemento).prop('disabled', true);
                    }
                    
                    $(document).ready(function(){
                        //Deshabilitar botón Retramitación si la fila seleccionada es OK
                        deshabilitarBoton('#btnRetramitacion');
                        $('#listaErrores').on('click', 'tr', function() {
                          
                            var retram = listaErrores[tablaErrores.selectedIndex][19];
                            var xmlSolic = listaErrores[tablaErrores.selectedIndex][18];
                            
                            if (tablaErrores.selectedIndex != -1 
                                    && ((retram == "null" || retram == 'N' || retram == 'No') && (xmlSolic != 'null' && xmlSolic != ''))) {
                                habilitarBoton('#btnRetramitacion');
                            } else {
                                deshabilitarBoton('#btnRetramitacion');
                            }
                        });
                    });
                    
                    //Retramitación masiva_______________________________________________________________fin
                    
                </script>
            </form>
            <!--        <div id="popupcalendar" class="text"></div>   -->
    </body>
</html>
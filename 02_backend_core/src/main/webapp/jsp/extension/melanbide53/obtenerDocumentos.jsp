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
<%@page import="java.util.ArrayList" %>
<%@page import="java.util.List" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide53.vo.DocumentoLan6TablaDocVO" %>


<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<html>
    <head><jsp:include page="/jsp/administracion/tpls/app-constants.jsp" />
        <meta http-equiv="Content-Type" content="text/html; charset=ISO_8859-1">
		<meta http-equiv="X-UA-Compatible" content="IE=Edge" />
        <title>Obtener Documentos</title>

        <%
            UsuarioValueObject usuarioVO = new UsuarioValueObject();
            int idiomaUsuario = 1;
            int apl = 5;
			String css = "";
            if (session.getAttribute("usuario") != null) {
                usuarioVO = (UsuarioValueObject) session.getAttribute("usuario");
                apl = usuarioVO.getAppCod();
                idiomaUsuario = usuarioVO.getIdioma();
				css = usuarioVO.getCss();
            }
            
            MeLanbide53I18n meLanbide53I18n = MeLanbide53I18n.getInstance();
       %>
       
       <script type="text/javascript">
            var APP_CONTEXT_PATH = '<%=request.getContextPath()%>';
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
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/popup.js"></script>
		
		        
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/estilo.css"/>
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/extension/melanbide53/melanbide53.css"/>
        <!-- Eventos onKeyPress compatibilidad firefox  -->
        <!--script type="text/javascript" src="http://code.jquery.com/jquery-1.4.4.js"></script-->
        
    </head>
    <body class="bandaBody">
        <form id="formobtenerDocumentos">
            <h2 class="legendAzul" id="pestanaPrinc"><%=meLanbide53I18n.getMensaje(idiomaUsuario,"label.tituloPrincipal")%></h2>
            <div class="contenidoPantalla">
                  <div style="clear: both;">
                    <div>
                        <div id="div_label" class="sub3titulo" style=" text-align: center;">
                            <label class="sub3titulo" style=" text-align: center; width: 100%; position: relative;" ><%=meLanbide53I18n.getMensaje(idiomaUsuario, "legend.titulo.tabla.obtener.documentos")%></label>
                        </div>    
                        <div id="divGeneral"  style="overflow-y: auto; overflow-x: hidden; height: 200px;">     
                            <div id="listaDocumentos" align="center"></div>
                        </div>
                        <br/>
                        <div class="botonera" style="text-align: center;">
                            <input type="button" id="btnDescargarDocumento" name="btnDescargarDocumento" class="botonGeneral" value="<%=meLanbide53I18n.getMensaje(idiomaUsuario, "btn.descargar")%>" onclick="pulsarDescagargarDocumento();">
                            <input type="button" id="btnVolver" name="btnVolver" class="botonGeneral" value="<%=meLanbide53I18n.getMensaje(idiomaUsuario, "btn.volver")%>" onclick="cerrarVentana();"/>   
                        </div>
                    </div>
                </div>
            </div>

            <!--Script Ejecucion Elementos Pagina   legendAzul-->
            <script type="text/javascript">

                //Tabla Documentos
                var tablaDocumentos;
                var listaDocumentos = new Array();
                var listaDocumentosTabla = new Array();

                //right - left - center
                //tablaDocumentos = new Tabla(document.getElementById('listaDocumentos'), 515);
				tablaDocumentos = new Tabla(true,'<%=descriptor.getDescripcion("buscar")%>','<%=descriptor.getDescripcion("anterior")%>','<%=descriptor.getDescripcion("siguiente")%>','<%=descriptor.getDescripcion("mosFilasPag")%>','<%=descriptor.getDescripcion("msgNoResultBusq")%>','<%=descriptor.getDescripcion("mosPagDePags")%>', '<%=descriptor.getDescripcion("noRegDisp")%>','<%=descriptor.getDescripcion("filtrDeTotal")%>','<%=descriptor.getDescripcion("primero")%>','<%=descriptor.getDescripcion("ultimo")%>',document.getElementById('listaDocumentos'), 515);
                tablaDocumentos.addColumna('180','center',"<%=meLanbide53I18n.getMensaje(idiomaUsuario,"gestionErrores.tablaDocumentos.col1")%>");
                tablaDocumentos.addColumna('320','left',"<%=meLanbide53I18n.getMensaje(idiomaUsuario,"gestionErrores.tablaDocumentos.col2")%>");

                tablaDocumentos.displayCabecera=true;
                tablaDocumentos.height = 140;

                <%  		
                    DocumentoLan6TablaDocVO objectVO = new DocumentoLan6TablaDocVO();
                    List<DocumentoLan6TablaDocVO> _list = null;
                    if(request.getAttribute("listaDocumentosTabla")!=null){
                        _list = (List<DocumentoLan6TablaDocVO>)request.getAttribute("listaDocumentosTabla");
                    }													
                    if (_list!= null && _list.size() >0){
                        for (int indice=0;indice<_list.size();indice++)
                        {
                            objectVO = _list.get(indice);
                %>
                    listaDocumentos[<%=indice%>] = ['<%=objectVO.getOid_solicitud()%>','<%=objectVO.getOid()%>','<%=objectVO.getNombre()%>','<%=objectVO.getExtension()%>','<%=objectVO.getNombreCompleto()%>','<%=objectVO.getIdProcedimiento()%>'];
                    listaDocumentosTabla[<%=indice%>] = ['<%=objectVO.getOid()%>','<%=objectVO.getNombreCompleto()%>'];
                <%
                        }// for
                    }// if
                %>

                tablaDocumentos.lineas=listaDocumentosTabla;
                tablaDocumentos.displayTabla();
                //document.getElementById('listaDocumentos').children[0].children[1].children[0].children[0].ondblclick = function(event){
                //            pulsarModificarError(event);
                if(navigator.appName.indexOf("Internet Explorer")!=-1 || (navigator.appName.indexOf("Netscape")!=-1 && navigator.appCodeName.match(/Mozilla/))){
                    try{
                        var div = document.getElementById('listaDocumentos');
                        //div.children[0].children[0].children[0].children[0].children[0].children[0].children[0].children[0].children[0].children[0].children[0].children[0].children[0].children[1].style.width = '100%';
                        //div.children[0].children[1].children[0].children[0].children[0].children[0].children[0].children[0].children[0].children[0].children[0].children[1].style.width = '100%';
                        if(navigator.appName.indexOf("Internet Explorer")!=-1 && !(navigator.userAgent.match(/compatible/))){
                            div.children[0].children[0].children[0].children[0].children[0].children[0].children[1].style.width = '100%';
                            div.children[0].children[1].children[0].children[0].children[0].children[0].style.width = '100%';
                        }else{
                            div.children[0].children[0].children[0].children[0].children[0].children[0].style.width = '100%';
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

                function pulsarDescagargarDocumento() {
                    if (tablaDocumentos.selectedIndex != -1) {
                        var control = new Date();
                        window.open('<%=request.getContextPath()%>/PeticionModuloIntegracion.do?tarea=preparar&modulo=MELANBIDE53&operacion=descargarDocumentoDokusiGError&tipo=0&nuevo=0&oidSolicitud='
                                + listaDocumentos[tablaDocumentos.selectedIndex][0] 
                                + '&oid=' + listaDocumentos[tablaDocumentos.selectedIndex][1] + '&idProcedimiento=' 
                                + listaDocumentos[tablaDocumentos.selectedIndex][5] 
                                + '&control=' + control.getTime(), 'ventana1',
                    'left=10, top=10, width=650, height=500, scrollbars=no, menubar=no, location=no, resizable=yes');
                       
//                        document.forms[0].action = '<%=request.getContextPath()%>/PeticionModuloIntegracion.do?tarea=preparar&modulo=MELANBIDE53&operacion=descargarDocumentoDokusiGError&tipo=0&nuevo=0&oidSolicitud='
//                                + listaDocumentos[tablaDocumentos.selectedIndex][0] 
//                                + '&oid=' + listaDocumentos[tablaDocumentos.selectedIndex][1] + '&idProcedimiento=' 
//                                + listaDocumentos[tablaDocumentos.selectedIndex][5] 
//                                + '&control=' + control.getTime();       
//                        document.forms[0].target="oculto";
//                        document.forms[0].submit();   
                
                    }
                    
                    else {
                        jsp_alerta('A', '<%=meLanbide53I18n.getMensaje(idiomaUsuario, "msg.msjNoSelecFila")%>');
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
            </script>
        </form>
    </body>
</html>
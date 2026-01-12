
<%@taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.common.service.config.Config"%>
<%@page import="es.altia.common.service.config.ConfigServiceHelper"%>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide81.i18n.MeLanbide81I18n" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide81.util.ConfigurationParameter"%>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide81.util.ConstantesMeLanbide81"%>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide81.vo.Tipo1VO"%>
<%@page import="java.util.ArrayList" %>
<%@page import="java.util.Date" %>
<%@page import="java.util.List" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>  
        <meta http-equiv="X-UA-Compatible" content="IE=Edge" />
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
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

                    MeLanbide81I18n meLanbide81I18n = MeLanbide81I18n.getInstance();
                    String numExpediente = (String)request.getAttribute("numExp");
		
                    String urlPestanaTipo1 = (String)request.getAttribute("urlPestanaTipo1");        
                    String urlPestanaTipo2 = (String)request.getAttribute("urlPestanaTipo2");        
        %>

        <jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
        <jsp:setProperty name="descriptor"  property="idi_cod" value="<%=idiomaUsuario%>" />
        <jsp:setProperty name="descriptor"  property="apl_cod" value="<%=apl%>" />
        <link rel="StyleSheet" media="screen" type="text/css" href="<%=request.getContextPath()%><%=css%>"/>
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/>
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/extension/melanbide81/melanbide81.css"/>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/melanbide81/LpeelUtils.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/popup.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/tabpane.js"></script>
        <script type="text/javascript">
            var APP_CONTEXT_PATH = '<%=request.getContextPath()%>';
            var url = APP_CONTEXT_PATH + "/PeticionModuloIntegracion.do";
            var tp81_esprec;

            function configurarPestanas() {
                mostrarPestanaPrincipal();
            }

            function ocultarPestanaPrincipal() {
                tp81_esprec.hideTabPage(1);
            }

            function mostrarPestanaPrincipal() {
                tp81_esprec.showTabPage(1);
            }
        </script>
    </head>

    <body class="bandaBody ">
        <div class="tab-page" id="tabPage8113" style="height:520px; width: 100%;"> 
            <!-- Datos Comunes para las pestanas -->
            <input type="hidden" id="errorBD" name="errorBD" value="<%=meLanbide81I18n.getMensaje(idiomaUsuario,"error.errorBD")%>"/>
            <input type="hidden" id="generico" name="generico" value="<%=meLanbide81I18n.getMensaje(idiomaUsuario,"error.generico")%>"/>
            <input type="hidden" id="pasoParametros" name="pasoParametros" value="<%=meLanbide81I18n.getMensaje(idiomaUsuario,"error.pasoParametros")%>"/>
            <input type="hidden" id="listaVacia" name="listaVacia" value="<%=meLanbide81I18n.getMensaje(idiomaUsuario,"error.listaVacia")%>"/>
            <input type="hidden" id="obtenerDatos" name="obtenerDatos" value="<%=meLanbide81I18n.getMensaje(idiomaUsuario,"error.obtenerDatos")%>"/>
            <input type="hidden" id="borrarDatos" name="borrarDatos" value="<%=meLanbide81I18n.getMensaje(idiomaUsuario,"error.borrarDatos")%>"/>
            <input type="hidden" id="operacionIncorrecta" name="operacionIncorrecta" value="<%=meLanbide81I18n.getMensaje(idiomaUsuario,"error.operacionIncorrecta")%>"/>

            <h2 class="tab" id="pestanaAportacion"><%=meLanbide81I18n.getMensaje(idiomaUsuario,"label.titulo.pestanaAportacion")%></h2>
            <div id="barraProgresoLPEEL" style="visibility: hidden;">
                <div class="contenedorHidepage">
                    <div class="textoHide">
                        <span>
                            <%=meLanbide81I18n.getMensaje(idiomaUsuario, "msg.procesando")%>
                        </span>
                    </div>
                    <div class="imagenHide">
                        <span id="disco" class="fa fa-spinner fa-spin" aria-hidden="true"/>
                    </div>
                </div>
            </div>            
            <div style="clear: both; width: 100%; text-align: left;">
                <div id="tab-panel-81_esprec" class="tab-pane" style="float: left;" align="center"></div>
                <script type="text/javascript">
                    tp81_esprec = new WebFXTabPane(document.getElementById("tab-panel-81_esprec"));
                    tp81_esprec.selectedIndex = 0;
                </script>            
                <div class="tab-page" id="tabPage81_tipo1" style="height: 480px;">
                    <h2 class="tab" id="pestana81_tipo1"><%=meLanbide81I18n.getMensaje(idiomaUsuario,"label.titulo.tipo1")%></h2>
                    <script type="text/javascript">tp81_esprec.addTabPage(document.getElementById("tabPage81_tipo1"));</script>
                    <div style="clear: both;border:1px solid; border-color: gray; height:100%; width:98%;">
                        <jsp:include page="<%=urlPestanaTipo1%>" flush="true"/>
                    </div>
                </div>
                <div class="tab-page" id="tabPage81_tipo2" style="height: 480px;">
                    <h2 class="tab" id="pestana81_tipo2"><%=meLanbide81I18n.getMensaje(idiomaUsuario,"label.titulo.tipo2")%></h2>
                    <script type="text/javascript">tp81_esprec.addTabPage(document.getElementById("tabPage81_tipo2"));</script>
                    <div style="clear: both;border:1px solid; border-color: gray; height:100%; width:98%;">
                        <jsp:include page="<%=urlPestanaTipo2%>" flush="true"/>
                    </div>                   
                </div>                
            </div>
        </div>                    
        <script  type="text/javascript">
            configurarPestanas();
        </script>        
    </body>
</html>

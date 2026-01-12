<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>
<%@page import="es.altia.common.service.config.Config"%>
<%@page import="es.altia.common.service.config.ConfigServiceHelper"%>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide15.i18n.MeLanbide15I18n" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide15.util.ConfigurationParameter"%>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide15.util.ConstantesMeLanbide15"%>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide15.util.MeLanbide15Utils"%>
<%@page import="java.util.ArrayList" %>
<%@page import="java.util.List" %>
<%@page import="java.util.Date" %>
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

                            MeLanbide15I18n meLanbide15I18n = MeLanbide15I18n.getInstance();
                            String numExpediente = (String)request.getAttribute("numExp");		
                         
    
             String urlPestanaIdentidad = (String) request.getAttribute("urlPestanaIdentidad");
            String urlPestanaFormacion = (String) request.getAttribute("urlPestanaFormacion");
            String urlPestanaOrientacion = (String) request.getAttribute("urlPestanaOrientacion");
            String urlPestanaContratacion = (String) request.getAttribute("urlPestanaContratacion");
    
       
        %>
        <jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
        <jsp:setProperty name="descriptor"  property="idi_cod" value="<%=idiomaUsuario%>" />
        <jsp:setProperty name="descriptor"  property="apl_cod" value="<%=apl%>" />
        <link rel="StyleSheet" media="screen" type="text/css" href="<%=request.getContextPath()%><%=css%>"/>
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/>
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/extension/melanbide15/melanbide15.css"/>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/melanbide15/CatpUtils.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/popup.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/tabpane.js"></script>
        <script type="text/javascript">
            var APP_CONTEXT_PATH = '<%=request.getContextPath()%>';
            var url = APP_CONTEXT_PATH + "/PeticionModuloIntegracion.do";
            var tp15_esprec;
            function configurarPestanas() {
                mostrarPestanaPrincipal();
            }

            function ocultarPestanaPrincipal() {
                tp15_esprec.hideTabPage(1);
            }

            function mostrarPestanaPrincipal() {
                tp15_esprec.showTabPage(1);
            }
        </script>

    </head>

    <body class="bandaBody ">
        <div class="tab-page" id="tabPage1513" style="height:520px; width: 100%;"> 
            <!-- Datos Comunes para las pestanas -->
            <input type="hidden" id="errorBD" name="errorBD" value="<%=meLanbide15I18n.getMensaje(idiomaUsuario,"error.errorBD")%>"/>
            <input type="hidden" id="generico" name="generico" value="<%=meLanbide15I18n.getMensaje(idiomaUsuario,"error.generico")%>"/>
            <input type="hidden" id="pasoParametros" name="pasoParametros" value="<%=meLanbide15I18n.getMensaje(idiomaUsuario,"error.pasoParametros")%>"/>
            <input type="hidden" id="listaVacia" name="listaVacia" value="<%=meLanbide15I18n.getMensaje(idiomaUsuario,"error.listaVacia")%>"/>
            <input type="hidden" id="obtenerDatos" name="obtenerDatos" value="<%=meLanbide15I18n.getMensaje(idiomaUsuario,"error.obtenerDatos")%>"/>
            <input type="hidden" id="borrarDatos" name="borrarDatos" value="<%=meLanbide15I18n.getMensaje(idiomaUsuario,"error.borrarDatos")%>"/>
            <input type="hidden" id="operacionIncorrecta" name="operacionIncorrecta" value="<%=meLanbide15I18n.getMensaje(idiomaUsuario,"error.operacionIncorrecta")%>"/>

            <h2 class="tab" id="pestanaParticipantes"><%=meLanbide15I18n.getMensaje(idiomaUsuario,"label.titulo.pestanaPrincipal")%></h2>

            <div style="clear: both; width: 100%; text-align: left;">
                <div id="tab-panel-15_esprec" class="tab-pane" style="float: left;" align="center"></div>
                <script type="text/javascript">
                    tp15_esprec = new WebFXTabPane(document.getElementById("tab-panel-15_esprec"));
                    tp15_esprec.selectedIndex = 0;
                </script>            
                <div class="tab-page" id="tabPage15_identidad" style="height: 480px;">
                    <h2 class="tab" id="pestana15_identidad"><%=meLanbide15I18n.getMensaje(idiomaUsuario,"label.titulo.identidad")%></h2>
                    <script type="text/javascript">tp15_esprec.addTabPage(document.getElementById("tabPage15_identidad"));</script>
                    <div style="clear: both;border:1px solid; border-color: gray; height:100%; width:98%;">
                        <jsp:include page="<%=urlPestanaIdentidad%>" flush="true"/>
                    </div>
                </div>
                <div class="tab-page" id="tabPage15_formacion" style="height: 480px;">
                    <h2 class="tab" id="pestana15_formacion"><%=meLanbide15I18n.getMensaje(idiomaUsuario,"label.titulo.formacion")%></h2>
                    <script type="text/javascript">tp15_esprec.addTabPage(document.getElementById("tabPage15_formacion"));</script>
                    <div style="clear: both;border:1px solid; border-color: gray; height:100%; width:98%;">
                        <jsp:include page="<%=urlPestanaFormacion%>" flush="true"/>
                    </div>                   
                </div>                
                <div class="tab-page" id="tabPage15_orientacion" style="height: 480px;">
                    <h2 class="tab" id="pestana15_orientacion"><%=meLanbide15I18n.getMensaje(idiomaUsuario,"label.titulo.orientacion")%></h2>
                    <script type="text/javascript">tp15_esprec.addTabPage(document.getElementById("tabPage15_orientacion"));</script>
                    <div style="clear: both;border:1px solid; border-color: gray; height:100%; width:98%;">
                        <jsp:include page="<%=urlPestanaOrientacion%>" flush="true"/>
                    </div>                   
                </div>                
                <div class="tab-page" id="tabPage15_contratacion" style="height: 480px;">
                    <h2 class="tab" id="pestana15_contratacion"><%=meLanbide15I18n.getMensaje(idiomaUsuario,"label.titulo.contratacion")%></h2>
                    <script type="text/javascript">tp15_esprec.addTabPage(document.getElementById("tabPage15_contratacion"));</script>
                    <div style="clear: both;border:1px solid; border-color: gray; height:100%; width:98%;">
                        <jsp:include page="<%=urlPestanaContratacion%>" flush="true"/>
                    </div>                   
                </div>                
            </div>
        </div>   

        <script  type="text/javascript">
            configurarPestanas();
        </script>        
    </body>
</html>

<%-- 
    Document   : justificacion23
    Created on : 02-ene-2025, 11:16:40
    Author     : kigonzalez
--%>
<%@taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@taglib uri="/WEB-INF/tlds/c.tld" prefix="c"%>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide35.i18n.MeLanbide35I18n"%>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide35.util.ConstantesMeLanbide35"%>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide35.util.MeLanbide35Utils"%>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide35.vo.comun.Eca23ConfiguracionVO"%>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide35.vo.justificacion.JustificacionECA23VO"%>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.common.service.config.Config"%>
<%@page import="es.altia.common.service.config.ConfigServiceHelper"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="java.text.DecimalFormat"%>
<%@page import="java.text.DecimalFormatSymbols"%>
<head>
    <meta http-equiv="X-UA-Compatible" content="IE=Edge"/>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <%
        int idiomaUsuario = 1;
        int apl = 5;
        String css = "";
        UsuarioValueObject usuario = new UsuarioValueObject();
    
        if (session.getAttribute("usuario") != null) {
            usuario = (UsuarioValueObject) session.getAttribute("usuario");
            apl = usuario.getAppCod();
            idiomaUsuario = usuario.getIdioma();
            css = usuario.getCss();
        }
    
    //Clase para internacionalizar los mensajes de la aplicación.
        MeLanbide35I18n meLanbide35I18n = MeLanbide35I18n.getInstance();
        String numExpediente = request.getParameter("numero");
        Eca23ConfiguracionVO importesConfiguracion = (Eca23ConfiguracionVO)request.getAttribute("configuracion");
        

    %>
    <jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
    <jsp:setProperty name="descriptor"  property="idi_cod" value="<%=idiomaUsuario%>" />
    <jsp:setProperty name="descriptor"  property="apl_cod" value="<%=apl%>" />
    <link rel="StyleSheet" media="screen" type="text/css" href="<%=request.getContextPath()%><%=css%>">
    <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/melanbide35/ecaUtils.js"></script>
    <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/melanbide35/TablaNuevaEca.js"></script>
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/>
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/extension/melanbide35/melanbide35.css"/>
    <script type="text/javascript" src="<%=request.getContextPath()%>/scripts/popup.js"></script>

</head>
<body>
    <div class="tab-page" id="tabPage353" style="height:520px; width: 100%;">
        <h2 class="tab" id="pestana353"><%=meLanbide35I18n.getMensaje(idiomaUsuario,"label.validacion.tituloPestana")%></h2>
            <legend class="legendTema" style="text-transform: uppercase;text-align: center;" id="invalidExp"><%=meLanbide35I18n.getMensaje(idiomaUsuario,"label.validacion.invalid_exp")%></legend>
    </div>
</body>
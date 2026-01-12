<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>

<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide35.i18n.MeLanbide35I18n" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.common.service.config.Config"%>
<%@page import="es.altia.common.service.config.ConfigServiceHelper"%>
<%@page import="java.util.ArrayList" %>
<%@page import="java.util.List" %>
<%@page import="java.sql.Date" %>
<%@page import="com.google.gson.Gson" %>
<%@page import="com.google.gson.GsonBuilder" %>

<head>
    <meta http-equiv="X-UA-Compatible" content="IE=Edge" />
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <%
        int idiomaUsuario = 1;
        int apl = 5; // Pendiente de mirar
        int codOrganizacion = 0;
        String css = "";
        String cuantiaSolicitada = "";
        String subvConc = "";
        String subvMod = "";
        UsuarioValueObject usuario = new UsuarioValueObject();
        try {
            if (session != null)
            {
                if (usuario != null)
                {
                    usuario = (UsuarioValueObject) session.getAttribute("usuario");
                    apl = usuario.getAppCod();
                    idiomaUsuario = usuario.getIdioma();
                    codOrganizacion  = usuario.getOrgCod();
                    css = usuario.getCss();
                }
            }
        }
        catch(Exception ex){}
        //Clase para internacionalizar los mensajes de la aplicacion.
        MeLanbide35I18n meLanbide35I18n = MeLanbide35I18n.getInstance();

        String nombreModulo     = request.getParameter("nombreModulo");
        String numExpediente    = request.getParameter("numero");

        cuantiaSolicitada = (String)request.getAttribute("cuantiaSolicitada");
        subvConc = (String)request.getAttribute("subvConc");
        subvMod = (String)request.getAttribute("subvMod");
    %>

    <jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
    <jsp:setProperty name="descriptor"  property="idi_cod" value="<%=idiomaUsuario%>" />
    <jsp:setProperty name="descriptor"  property="apl_cod" value="<%=apl%>" />
    <link rel="StyleSheet" media="screen" type="text/css" href="<%=request.getContextPath()%><%=css%>">
</head>
<body>

    <div class="tab-page" id="tabDatosResumenCampos" style="height:90%; width: 100%;">
        <h2 class="tab" id="pestanaResumenCampos"><%=meLanbide35I18n.getMensaje(idiomaUsuario,"label.resumen.tituloPestana")%></h2>
        <script type="text/javascript">tp1.addTabPage(document.getElementById("tabDatosResumenCampos"));</script>
        <div style="clear: both;">
            <div id="divGeneral"  style="overflow-y: auto; overflow-x: hidden; height: 90%;">
                    <fieldset id="program" name="program">
                            <div class="lineaFormulario">
                                <div id="divCuantiaSolicitada" name="divCuantiaSolicitada">
                                    <div class="etiquetaEca"><%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.resumen.cuantiaSolicitada")%></div>
                                    <div style="width: 400px;float: left;">
                                        <input id="cuantiaSolicitada" name="cuantiaSolicitada" type="text" class="inputTextoDeshabilitado" value="<%=cuantiaSolicitada%>" disabled/>
                                    </div>
                                </div>
                            </div>
                            <div class="lineaFormulario">
                                <div id="divSubvConc" name="divSubvConc">
                                    <div class="etiquetaEca"><%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.resumen.subvencionConcedida")%></div>
                                    <div style="width: 400px;float: left;">
                                        <input id="subvConc" name="subvConc" type="text" class="inputTextoDeshabilitado" value="<%=subvConc%>" disabled/>
                                    </div>
                                </div>
                            </div>
                            <div class="lineaFormulario">
                                <div id="divSubvMod" name="divSubvMod">
                                    <div class="etiquetaEca"><%=meLanbide35I18n.getMensaje(idiomaUsuario, "label.resumen.subvencionModificada")%></div>
                                    <div style="width: 400px;float: left;">
                                        <input id="subvMod" name="subvMod" type="text" class="inputTextoDeshabilitado" value="<%=subvMod%>" disabled/>
                                    </div>
                                </div>
                            </div>
                    </fieldset>
            </div>
        </div>
        <!-- Es la capa m?s externa de la pesta?a -->
    </div>
</body>

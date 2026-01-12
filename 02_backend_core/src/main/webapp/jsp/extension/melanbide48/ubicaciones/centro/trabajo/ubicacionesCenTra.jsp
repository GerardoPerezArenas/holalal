<%-- 
    Document   : ubicacionesCenTra
    Created on : 06-oct-2017, 10:10:55
    Author     : INGDGC
--%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>

<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide48.i18n.MeLanbide48I18n" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.common.service.config.Config"%>
<%@page import="es.altia.common.service.config.ConfigServiceHelper"%>
<%@page import="java.util.ArrayList" %>
<%@page import="java.util.List" %>
<%@page import="java.util.Iterator" %>
<%@page import="java.util.Map" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide48.vo.ColecUbicacionesCTVO"%>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide48.util.ConstantesMeLanbide48"%>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide48.util.MeLanbide48Utils"%>
<%@page import="org.apache.commons.lang.StringEscapeUtils"%>

<%
    String sIdioma = request.getParameter("idioma");
    int idiomaUsuario = (sIdioma!=null && sIdioma!="" ? Integer.parseInt(sIdioma) : 1);
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
                apl = usuario.getAppCod();
                css = usuario.getCss();
            }
        }
    }
    catch(Exception ex)
    {
        
    }
    //Clase para internacionalizar los mensajes de la aplicación.
    MeLanbide48I18n meLanbide48I18n = MeLanbide48I18n.getInstance();
    Config m_Config = ConfigServiceHelper.getConfig("common");
    String statusBar = m_Config.getString("JSP.StatusBar");
    String nombreModulo     = request.getParameter("nombreModulo");
    String codOrganizacion  = request.getParameter("codOrganizacionModulo");
    String numExpediente    = request.getParameter("numero");
    String mensajeProgreso = "";
    Integer ejercicio = (numExpediente != null && numExpediente!="" ? Integer.valueOf(numExpediente.substring(0, 4)) : 0); 
    
    
    List<ColecUbicacionesCTVO> listaUbicacionesCT = (List<ColecUbicacionesCTVO>)request.getAttribute("listaUbicacionesCT");
    String listaUbicacionesCTJSON = (String)request.getAttribute("listaUbicacionesCTJSON");

%>

<jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
<jsp:setProperty name="descriptor"  property="idi_cod" value="<%=idiomaUsuario%>" />
<jsp:setProperty name="descriptor"  property="apl_cod" value="<%=apl%>" />

<link rel="StyleSheet" media="screen" type="text/css" href="<%=request.getContextPath()%><%=css%>"/>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/estilo.css"/>
<link rel="stylesheet" type="text/css" href="<c:url value='/css/table.css'/>"/>
<link rel="stylesheet" type="text/css" href="<c:url value='/css/extension/melanbide48/melanbide48.css'/>"/>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/bootstrap413/bootstrap.min.css" media="all"/>

<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/bootstrap413/bootstrap.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script>
<script type="text/javascript" src="<c:url value='/scripts/popup.js'/>"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/melanbide48/TablaNuevaColec.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/melanbide48/FixedColumnsTable.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/melanbide48/colecUtils.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/melanbide48/colecGestionUbicacionesCT.js"></script>

<div id="bodyUbicacionesCetrosTra">
    <fieldset id="fieldsetUbicCentrosTra" style="height: 350px; margin-top: 20px; ">
        <legend class="legendAzul"><%=meLanbide48I18n.getMensaje(idiomaUsuario,"label.ubicaciones.centro.trabajo")%></legend>
        <div id="divTablaTrayectoriaGenActividades" align="center">
            <div id="divGeneral" style="clear: both; overflow-x: hidden; overflow-y: auto; height: 280px;">
                <div id="listaUbicacionesCentroTrabajo"></div>    <!--style="padding: 5px; width:98%; font-size: 13px; height: 220px; text-align: center; overflow-x: auto; overflow-y: auto;margin:0px;margin-top:0px;"-->
                <div class="botonera">
                    <input type="button" id="botonAnadirUbicacionCT" name="botonAnadirUbicacionCT" class="botonGeneral" value="<%=meLanbide48I18n.getMensaje(idiomaUsuario,"btn.anadir")%>" onclick="anadirUbicacionCentroTrabajo();"/>
                    <input type="button" id="botonModificarUbicacionCT" name="botonModificarUbicacionCT" class="botonGeneral" value="<%=meLanbide48I18n.getMensaje(idiomaUsuario,"btn.modificar")%>" onclick="modificarUbicacionCentroTrabajo();"/>
                    <input type="button" id="botonEliminarUbicacionCT" name="botonEliminarUbicacionCT" class="botonGeneral" value="<%=meLanbide48I18n.getMensaje(idiomaUsuario,"btn.eliminar")%>" onclick="eliminarUbicacionCentroTrabajo();"/>
                    <input type="button" id="botonValorarUbicacionCT" name="botonValorarUbicacionCT" class="botonGeneral" value="<%=meLanbide48I18n.getMensaje(idiomaUsuario,"btn.validar")%>" onclick="valorarUbicacionCentroTrabajo();"/>
                </div>
            </div>
        </div>
    </fieldset>
    <!-- Campos ocultas gestion desde JS -->
    <input type="hidden" id="listaUbicacionesCTJSON" name="listaUbicacionesCTJSON"/>
    <script>document.getElementById("listaUbicacionesCTJSON").value=JSON.stringify(<%=listaUbicacionesCTJSON%>,function(key, value) { return value === null ? "" : value });</script>
    <input type="hidden" id="listaUbicacionesCentroTrabajo_textoColumna1" value="<%=meLanbide48I18n.getMensaje(idiomaUsuario,"label.ubicaciones.centro.trabajo.tabla.columna1")%>"/>
    <input type="hidden" id="listaUbicacionesCentroTrabajo_textoColumna2" value="<%=meLanbide48I18n.getMensaje(idiomaUsuario,"label.ubicaciones.centro.trabajo.tabla.columna2")%>"/>
    <input type="hidden" id="listaUbicacionesCentroTrabajo_textoColumna3" value="<%=meLanbide48I18n.getMensaje(idiomaUsuario,"label.ubicaciones.centro.trabajo.tabla.columna3")%>"/>
    <input type="hidden" id="listaUbicacionesCentroTrabajo_textoColumna4" value="<%=meLanbide48I18n.getMensaje(idiomaUsuario,"label.ubicaciones.centro.trabajo.tabla.columna4")%>"/>
    <input type="hidden" id="listaUbicacionesCentroTrabajo_textoColumna5" value="<%=meLanbide48I18n.getMensaje(idiomaUsuario,"label.ubicaciones.centro.trabajo.tabla.columna5")%>"/>
    <input type="hidden" id="listaUbicacionesCentroTrabajo_textoColumna6" value="<%=meLanbide48I18n.getMensaje(idiomaUsuario,"label.ubicaciones.centro.trabajo.tabla.columna6")%>"/>
    <input type="hidden" id="listaUbicacionesCentroTrabajo_textoColumna7" value="<%=meLanbide48I18n.getMensaje(idiomaUsuario,"label.ubicaciones.centro.trabajo.tabla.columna7")%>"/>
    <input type="hidden" id="listaUbicacionesCentroTrabajo_textoColumna8" value="<%=meLanbide48I18n.getMensaje(idiomaUsuario,"label.ubicaciones.centro.trabajo.tabla.columna8")%>"/>
    <input type="hidden" id="listaUbicacionesCentroTrabajo_textoColumna9" value="<%=meLanbide48I18n.getMensaje(idiomaUsuario,"label.ubicaciones.centro.trabajo.tabla.columna9")%>"/>
    <input type="hidden" id="listaUbicacionesCentroTrabajo_textoColumna10" value="<%=meLanbide48I18n.getMensaje(idiomaUsuario,"label.ubicaciones.centro.trabajo.tabla.columna10")%>"/>
    <input type="hidden" id="listaUbicacionesCentroTrabajo_textoColumna11" value="<%=meLanbide48I18n.getMensaje(idiomaUsuario,"label.ubicaciones.centro.trabajo.tabla.columna11")%>"/>
</div>

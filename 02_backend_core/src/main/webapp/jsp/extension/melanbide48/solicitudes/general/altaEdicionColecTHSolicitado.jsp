<%-- 
    Document   : altaEdicionColecTHSolicitado
    Created on : 03-Jun-2021, 11:14:48
    Author     : INGDGC
--%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<meta http-equiv="X-UA-Compatible" content="IE=11">

<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide48.i18n.MeLanbide48I18n" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@ page import="es.altia.common.service.config.Config"%>
<%@ page import="es.altia.common.service.config.ConfigServiceHelper"%>
<%@page import="java.util.ArrayList" %>
<%@page import="java.util.List" %>
<%@page import="java.util.Iterator" %>
<%@page import="java.util.Map" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide48.vo.SelectItem"%>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide48.vo.ColecSolicitudVO"%>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide48.util.ConstantesMeLanbide48"%>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide48.util.MeLanbide48Utils"%>
<%@page import="org.apache.commons.lang.StringEscapeUtils"%>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide48.vo.MeLanbideConvocatorias" %>


<%
    String sIdioma = request.getParameter("idioma");
    int idiomaUsuario = (sIdioma!=null && sIdioma!="" ? Integer.parseInt(sIdioma) : 1);
    int apl = 5;
    String css = "";
    UsuarioValueObject usuario = new UsuarioValueObject();
    int codOrganizacion = 0;
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
                codOrganizacion  =  usuario.getOrgCod();
            }
        }
    }
    catch(Exception ex)
    {
        ex.printStackTrace();
    }
    //Clase para internacionalizar los mensajes de la aplicación.
    MeLanbide48I18n meLanbide48I18n = MeLanbide48I18n.getInstance();
    Config m_Config = ConfigServiceHelper.getConfig("common");
    String nombreModulo     = request.getParameter("nombreModulo");
    String numExpediente    = request.getParameter("numero");
    Integer ejercicio    = MeLanbide48Utils.getEjercicioDeExpediente(numExpediente);
    String codProcedimiento    = MeLanbide48Utils.getCodProcedimientoDeExpediente(numExpediente);
    String colecSolicitudVOJSON =  (String)request.getAttribute("colecSolicitudVOJSON");
    
    MeLanbideConvocatorias convocatoriaActiva = (MeLanbideConvocatorias)request.getAttribute("convocatoriaActiva");
    String codigoConvocatoriaExpediente = (convocatoriaActiva!=null && convocatoriaActiva.getDecretoCodigo() !=null ? convocatoriaActiva.getDecretoCodigo() : "");
    Integer idBDConvocatoriaExpediente = (convocatoriaActiva!=null ? convocatoriaActiva.getId() : 0);
%>

<jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
<jsp:setProperty name="descriptor"  property="idi_cod" value="<%=idiomaUsuario%>" />
<jsp:setProperty name="descriptor"  property="apl_cod" value="<%=apl%>" />

<link rel="StyleSheet" media="screen" type="text/css" href="<%=request.getContextPath()%><%=css%>">
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/estilo.css"/>
<link rel="stylesheet" type="text/css" href="<c:url value='/css/extension/melanbide48/melanbide48.css'/>">
<link rel="stylesheet" type="text/css" href="<c:url value='/css/table.css'/>">
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/bootstrap413/bootstrap.min.css" media="all"/>


<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/general.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/bootstrap413/bootstrap.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/melanbide48/TablaNuevaColec.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/melanbide48/FixedColumnsTable.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/melanbide48/colecUtils.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/melanbide48/colecGestionColectivoTHSolicitado.js"></script>
<script type="text/javascript">var APP_CONTEXT_PATH = '<%=request.getContextPath()%>';</script>

<div id="pantallaAltaEdicionColecTHSolicitado" class="contenidoPantalla">
    <input type="hidden" id="colecSolicitudVOJSON" name="colecSolicitudVOJSON"/>
    <input type="hidden" id="pantallaEditarDatos" name="pantallaEditarDatos" value="S"/>
    <input type="hidden" id="codSolicitud" name="codSolicitud"/>
    <input type="hidden" id="ejercicio" name="ejercicio" value="<%=ejercicio%>"/>
    <input type="hidden" id="numeroExpediente" name="numeroExpediente" value="<%=numExpediente%>"/>
    <input type="hidden" id="codigoProcedimiento" name="codigoProcedimiento" value="<%=codProcedimiento%>"/>
    <input type="hidden" id="codigoOrganizacion" name="codigoOrganizacion" value="<%=codOrganizacion%>"/>
    <input type="hidden" id="urlBaseLlamadaM48" name="urlBaseLlamadaM48" value="<%=request.getContextPath()+"/PeticionModuloIntegracion.do"%>"/>
    <input type="hidden" name="textoDesplegableSeleccionaOpcion" id="textoDesplegableSeleccionaOpcion" value="<%=meLanbide48I18n.getMensaje(idiomaUsuario,"label.solicitud.combo.selecciona.opcion")%>"/>
    <input type="hidden" id="idBDConvocatoriaExpediente" name="idBDConvocatoriaExpediente" value="<%=idBDConvocatoriaExpediente%>"/>

    <div id="formularioDatos">
        <div class="form-group">
            <label for="codigoColectivo"><%=meLanbide48I18n.getMensaje(idiomaUsuario,"label.solicitud.colectivo")%></label>
            <select class="form-control" id="codigoColectivo">
                <option value=""><%=meLanbide48I18n.getMensaje(idiomaUsuario,"label.solicitud.combo.selecciona.opcion")%></option>
                <c:forEach items="${listaColectivo}" var="elementLista" varStatus="contador">
                    <option value="<c:out value="${elementLista.codigo}"/>" title="<c:out value="${elementLista.descripcion}"/>"><c:out value="${elementLista.descripcion}"/></option>
                </c:forEach>
            </select>
        </div>
        <div class="form-group">
            <label for="territorioHistorico"><%=meLanbide48I18n.getMensaje(idiomaUsuario,"label.solicitud.territorio.historico")%></label>
            <select class="form-control" id="territorioHistorico">
                <option value="" title=""><%=meLanbide48I18n.getMensaje(idiomaUsuario,"label.solicitud.combo.selecciona.opcion")%></option>
                <c:forEach items="${listaTerritorioHistorico}" var="elementLista" varStatus="contador">
                    <option value="<c:out value="${elementLista.codigo}"/>" title="<c:out value="${elementLista.descripcion}"/>"><c:out value="${elementLista.descripcion}"/></option>
                </c:forEach>
            </select>
        </div>
        <div class="form-group" id="divAmbitoComarca">
            <label for="ambitoComarca"><%=meLanbide48I18n.getMensaje(idiomaUsuario,"label.solicitud.ambito.comarca")%></label>
            <select class="form-control" id="ambitoComarca">
                <option value=""><%=meLanbide48I18n.getMensaje(idiomaUsuario,"label.solicitud.combo.selecciona.opcion")%></option>
            </select>
        </div>
        <div class="form-group" id="divNumeroBloquesHoras">
            <label for="numeroBloquesHoras"><%=meLanbide48I18n.getMensaje(idiomaUsuario,"label.solicitud.colectivo.th.solicitado.numero.horas")%></label>
            <input type="number" class="form-control" id="numeroBloquesHoras" aria-describedby="numeroBloquesHorasHelp" placeholder="0000">
            <small id="numeroBloquesHorasHelp" class="form-text text-muted"><%=meLanbide48I18n.getMensaje(idiomaUsuario,"label.solicitud.colectivo.th.solicitado.numero.horas.ayuda")%></small>
        </div>
        <div class="form-group">
            <label for="numeroUbicaciones"><%=meLanbide48I18n.getMensaje(idiomaUsuario,"label.solicitud.colectivo.th.solicitado.numero.ubicaciones")%></label>
            <input type="number" class="form-control" id="numeroUbicaciones" placeholder="0000">
        </div>
        <div class="botonera" style="margin-top: 25px;">
            <input type="button" id="btnGuardarColecTHSolicitado" name="btnGuardarColecTHSolicitado" class="botonGeneral" value="<%=meLanbide48I18n.getMensaje(idiomaUsuario,"btn.aceptar")%>" onclick="guardarDatosColectivoTHSolicitado();">
            <input type="button" id="btnCancelarColecTHSolicitado" name="btnCancelarColecTHSolicitado" class="botonGeneral" value="<%=meLanbide48I18n.getMensaje(idiomaUsuario,"btn.cancelar")%>" onclick="cancelarEdicionColecTHSolicitado();">
        </div>
    </div>
</div>
     
<script type="text/javascript">
    document.getElementById("colecSolicitudVOJSON").value=JSON.stringify(<%=colecSolicitudVOJSON%>,function(key, value) { return value == null ? "" : value });
</script>

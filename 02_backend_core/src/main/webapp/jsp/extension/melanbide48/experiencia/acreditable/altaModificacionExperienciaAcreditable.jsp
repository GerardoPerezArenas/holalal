<%-- 
    Document   : altaEdicionColecTHSolicitado
    Created on : 07-Jul-2021, 11:14:48
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
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide48.vo.ColecTrayectoriaEntidad"%>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide48.util.ConstantesMeLanbide48"%>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide48.util.MeLanbide48Utils"%>
<%@page import="java.text.SimpleDateFormat"%>

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
%>

<jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
<jsp:setProperty name="descriptor"  property="idi_cod" value="<%=idiomaUsuario%>" />
<jsp:setProperty name="descriptor"  property="apl_cod" value="<%=apl%>" />

<link rel="StyleSheet" media="screen" type="text/css" href="<%=request.getContextPath()%><%=css%>">
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/estilo.css"/>
<link rel="stylesheet" type="text/css" href="<c:url value='/css/extension/melanbide48/melanbide48.css'/>">
<link rel="stylesheet" type="text/css" href="<c:url value='/css/table.css'/>">
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/>
<link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/css/font-awesome.min.css'/>" media="screen" />
<link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/less/animated.less'/>" media="screen" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/bootstrap413/bootstrap.min.css" media="all"/>

<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/bootstrap413/bootstrap.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/calendario.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/general.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/melanbide48/TablaNuevaColec.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/melanbide48/FixedColumnsTable.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/melanbide48/colecUtils.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/melanbide48/moment-with-locales.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/melanbide48/colecGestionExperienciaAcreCol.js"></script>
<script type="text/javascript">var APP_CONTEXT_PATH = '<%=request.getContextPath()%>';</script>

<div id="pantallaAltaEdicionColecExpAcreditable" class="contenidoPantalla">
    <input type="hidden" id="pantallaEditarDatos" name="pantallaEditarDatos" value="S"/>
    <input type="hidden" id="ejercicio" name="ejercicio" value="<%=ejercicio%>"/>
    <input type="hidden" id="numeroExpediente" name="numeroExpediente" value="<%=numExpediente%>"/>
    <input type="hidden" id="codigoProcedimiento" name="codigoProcedimiento" value="<%=codProcedimiento%>"/>
    <input type="hidden" id="codigoOrganizacion" name="codigoOrganizacion" value="<%=codOrganizacion%>"/>
    <input type="hidden" id="urlBaseLlamadaM48" name="urlBaseLlamadaM48" value="<%=request.getContextPath()+"/PeticionModuloIntegracion.do"%>"/>
    <input type="hidden" id="idColectivo" name="idColectivo" value="<c:out value="${idColectivo}"/>"/>
    <input type="hidden" id="idGrupo" name="idGrupo" value="<c:out value="${idGrupo}"/>"/>
    <input type="hidden" id="identificadorBDGestionar" name="identificadorBDGestionar" value="<c:if test="${datosModif!=null && datosModif.id!=null}"><c:out value="${datosModif.id}"/></c:if>"/>

    <form name="formularioDatosEAColec" id="formularioDatosEAColec">
        <div class="shadow p-3 mb-5 bg-light rounded etiqueta">
            <div class="form-group">
                <label for="idBDEntidad" class="etiqueta"><%=meLanbide48I18n.getMensaje(idiomaUsuario,"label.entidad.entidad")%></label>
                <input type="text" class="inputTexto" disabled="true" readonly="true" size="5" id="idBDEntidad" value="<c:out value="${idBDEntidad}"/>">
                <input type="text" class="inputTexto" disabled="true" readonly="true" size="15" id="cifEntidad" value="<c:out value="${cifEntidad}"/>">
                <input type="text" class="inputTexto" disabled="true" readonly="true" size="90" id="nombreEntidad" value="<c:out value="${nombreEntidad}"/>">
            </div>
        </div>
        <div id="divDatosActividades">
            <div class="input-group mb-3">
                <div class="input-group-prepend etiqueta">
                    <span class="input-group-text" id="inputGroup-sizing-default"><%=meLanbide48I18n.getMensaje(idiomaUsuario,"label.solicitud.colectivo.trayectoria.nuevaAct.nombreAdm")%></span>
                </div>
                <input type="text" class="form-control" aria-label="Sizing example input" aria-describedby="inputGroup-sizing-default" id="trayNombreAdmonPublica" value="<c:if test="${datosModif!=null && datosModif.trayNombreAdmonPublica!=null}"><c:out value="${datosModif.trayNombreAdmonPublica}"/></c:if>">
                </div>
                <div class="input-group mb-3" id="divTrayDescripcionAct">
                    <div class="input-group-prepend etiqueta">
                        <span class="input-group-text" id="inputGroup-sizing-default2"><%=meLanbide48I18n.getMensaje(idiomaUsuario,"label.solicitud.colectivo.trayectoria.nuevaAct.descActividad")%></span>
                </div>
                <input type="text" class="form-control" aria-label="Sizing example input" aria-describedby="inputGroup-sizing-default2" id="trayDescripcionAct" value="<c:if test="${datosModif!=null && datosModif.trayDescripcion !=null}"><c:out value="${datosModif.trayDescripcion }"/></c:if>">
            </div>
        </div>
        <div class="input-group mb-3" id="divTrayDescripcionGenerico">
            <div class="input-group-prepend etiqueta">
                <span class="input-group-text" id="inputGroup-sizing-default3"><%=meLanbide48I18n.getMensaje(idiomaUsuario,"label.programa.convocatoria")%></span>
            </div>
                <input type="text" class="form-control" aria-label="Sizing example input" aria-describedby="inputGroup-sizing-default3" id="trayDescripcion" value="<c:if test="${datosModif!=null && datosModif.trayDescripcion !=null}"><c:out value="${datosModif.trayDescripcion }"/></c:if>">
        </div>
        
        <div class="input-group mb-3">
            <div class="input-group-prepend etiqueta">
                <span class="input-group-text" id="inputGroup-sizing-default3"><%=meLanbide48I18n.getMensaje(idiomaUsuario,"label.solicitud.trayectoria.general.otrosProgramas.fechaInicio")%></span>
            </div>
                <input type="text" class="form-control" 
                       id="trayFechaInicio" name="trayFechaInicio" maxlength="10" size="10"
                       placeholder="dd/mm/yyyy"
                       value="<c:if test="${datosModif!=null && datosModif.trayFechaInicio!=null}"><c:out value="${datosModif.trayFechaInicioString}"/></c:if>"
                       onkeypress = "return permiteSoloFormatoFechas(event);"
                       onfocus="javascript:this.select();"
                       onblur = "validarFormatoFecha(this);"
                       />
                <div id="caltrayFechaInicio" name="caltrayFechaInicio" 
                     class="input-group-append"  style="cursor: pointer;"
                     onclick='mostrarCalendarios(event,"trayFechaInicio",this.id)'
                     onblur='ocultarCalendarios("trayFechaInicio")'
                     >
                    <span class="input-group-text">
                        <i class="fa fa-calendar" aria-hidden="true"></i>
                    </span>
                </div>
        </div>
        <div class="input-group mb-3">
            <div class="input-group-prepend etiqueta">
                <span class="input-group-text" id="inputGroup-sizing-default3"><%=meLanbide48I18n.getMensaje(idiomaUsuario,"label.solicitud.trayectoria.general.otrosProgramas.fechaFin")%></span>
            </div>
            <input type="text" class="form-control" aria-label="Sizing example input" aria-describedby="inputGroup-sizing-default3" 
                   id="trayFechaFin" 
                   value="<c:if test="${datosModif!=null && datosModif.trayFechaFin!=null}"><c:out value="${datosModif.trayFechaFinString}"/></c:if>"
                   placeholder="dd/mm/yyyy"
                   maxlength="10" name="trayFechaFin" size="10"
                   onkeypress = "return permiteSoloFormatoFechas(event);"
                   onfocus="javascript:this.select();"
                   onblur = "validarFormatoFecha(this);"
                   />
            <div id="caltrayFechaFin" name="caltrayFechaFin" 
                 class="input-group-append"  style="cursor: pointer;"
                 onclick='mostrarCalendarios(event,"trayFechaFin",this.id)'
                 onblur='ocultarCalendarios("trayFechaFin")'
                 >
                <span class="input-group-text">
                    <i class="fa fa-calendar" aria-hidden="true"></i>
                </span>
            </div>
        </div>
        <div class="botonera" style="margin-top: 25px;">
            <input type="button" id="btnGuardarColecTrayectoriaEntidad" name="btnGuardarColecTrayectoriaEntidad" class="botonGeneral" value="<%=meLanbide48I18n.getMensaje(idiomaUsuario,"btn.aceptar")%>" onclick="guardarDatosColecTrayectoriaEntidad('<c:out value="${idColectivo}"/>','<c:out value="${idGrupo}"/>');">
            <input type="button" id="btnCancelarColecTrayectoriaEntidad" name="btnCancelarColecTrayectoriaEntidad" class="botonGeneral" value="<%=meLanbide48I18n.getMensaje(idiomaUsuario,"btn.cancelar")%>" onclick="cancelarEdicionColecTrayectoriaEntidad();">
        </div>
    </form>
</div>


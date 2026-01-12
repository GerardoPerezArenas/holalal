
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<meta http-equiv="X-UA-Compatible" content="IE=11">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide88.i18n.MeLanbide88I18n" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="java.util.ArrayList" %>
<%@page import="java.util.List" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide88.util.MeLanbide88Utils"%>
<%@page import="java.text.SimpleDateFormat"%>

<%
    String sIdioma = request.getParameter("idioma");
    int apl = 5;
    int idiomaUsuario = (sIdioma!=null && sIdioma!="" ? Integer.parseInt(sIdioma) : 1);
    int codOrganizacion = 0;
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
                codOrganizacion  =  usuario.getOrgCod();
            }
        }
    }
    catch(Exception ex)
    {
        ex.printStackTrace();
    }
    //Clase para internacionalizar los mensajes de la aplicación.
    MeLanbide88I18n meLanbide88I18n = MeLanbide88I18n.getInstance();
    
    String numExpediente    = request.getParameter("numero");
    String codProcedimiento  = request.getParameter("codProcedimiento");

%>

<jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
<jsp:setProperty name="descriptor"  property="idi_cod" value="<%=idiomaUsuario%>" />
<jsp:setProperty name="descriptor"  property="apl_cod" value="<%=apl%>" />

<link rel="StyleSheet" media="screen" type="text/css" href="<%=request.getContextPath()%><%=css%>">
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/estilo.css"/>
<link rel="stylesheet" type="text/css" href="<c:url value='/css/extension/melanbide88/melanbide88.css'/>">
<link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/css/font-awesome.min.css'/>" media="screen" />
<link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/less/animated.less'/>" media="screen" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/bootstrap413/bootstrap.min.css" media="all"/>

<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/bootstrap413/bootstrap.bundle.min.js"></script>
<!--<script type="text/javascript" src="< %=request.getContextPath()%>/scripts/calendario.js"></script>-->
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/general.js"></script>
<!--<script type="text/javascript" src="< %=request.getContextPath()%>/scripts/extension/melanbide88/moment-with-locales.js"></script>-->
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/melanbide88/gestionTrecoM88API.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/melanbide88/gestionTrecoM88AltaEdicionDatos.js"></script>
<script type="text/javascript">var APP_CONTEXT_PATH = '<%=request.getContextPath()%>';</script>

<div id="pantallaAltaEdicionMelanbide88Socios" class="contenidoPantalla">
    <input type="hidden" id="pantallaEditarDatos" name="pantallaEditarDatos" value="S"/>
    <input type="hidden" id="numeroExpediente" name="numeroExpediente" value="<%=numExpediente%>"/>
    <input type="hidden" id="codigoProcedimiento" name="codigoProcedimiento" value="<%=codProcedimiento%>"/>
    <input type="hidden" id="codigoOrganizacion" name="codigoOrganizacion" value="<%=codOrganizacion%>"/>
    <input type="hidden" id="urlBaseLlamadaM88" name="urlBaseLlamadaM88" value="<%=request.getContextPath()+"/PeticionModuloIntegracion.do"%>"/>
    <input type="hidden" id="identificadorBDGestionar" name="identificadorBDGestionar" value="<c:if test="${datosModif!=null && datosModif.id!=null}"><c:out value="${datosModif.id}"/></c:if>"/>
    <input type="hidden" id="msg_generico_campos_obligatorios" name="msg_generico_campos_obligatorios" value="<%=meLanbide88I18n.getMensaje(idiomaUsuario,"label.campos.obligatorios")%>"/>
    <input type="hidden" name="msg_datos_procesados_ok" id="msg_datos_procesados_ok" value="<%=meLanbide88I18n.getMensaje(idiomaUsuario,"label.datos.procesados.correctamente")%>"/>
    <input type="hidden" name="msg_datos_procesados_error" id="msg_datos_procesados_error" value="<%=meLanbide88I18n.getMensaje(idiomaUsuario,"label.datos.procesados.error")%>"/>

    <form name="formularioDatosTRECOSocios" id="formularioDatosTRECOSocios">
        <div class="shadow p-3 mb-5 bg-light rounded etiqueta">
            <div class="form-group">
                <h3><%=meLanbide88I18n.getMensaje(idiomaUsuario,"label.edicion.datos")%></h3>
            </div>
        </div>
        <div id="divDatosSocios">
            <div class="input-group mb-3">
                    <div class="input-group-prepend etiqueta">
                        <span class="input-group-text" id="dniNieSocioLabel"><%=meLanbide88I18n.getMensaje(idiomaUsuario,"label.documento")%>*</span>
                </div>
                <!--title="<%=meLanbide88I18n.getMensaje(idiomaUsuario,"label.documento")%>"-->
                <input type="text" class="form-control" aria-label="Sizing example input" aria-describedby="dniNieSocioLabel" id="dniNieSocio"
                       data-toggle="tooltip" data-title="<%=meLanbide88I18n.getMensaje(idiomaUsuario,"label.documento")%>"
                       value="<c:if test="${datosModif!=null && datosModif.dniNieSocio !=null}"><c:out value="${datosModif.dniNieSocio }"/></c:if>">
            </div>
            <div class="input-group mb-3">
                <div class="input-group-prepend etiqueta">
                    <span class="input-group-text" id="nombreSocioLabel"><%=meLanbide88I18n.getMensaje(idiomaUsuario,"label.nombre")%>*</span>
                </div>
                <input type="text" class="form-control" aria-label="Sizing example input" aria-describedby="nombreSocioLabel" id="nombreSocio" 
                       data-toggle="tooltip" title="<%=meLanbide88I18n.getMensaje(idiomaUsuario,"label.nombre")%>"
                       value="<c:if test="${datosModif!=null && datosModif.nombreSocio!=null}"><c:out value="${datosModif.nombreSocio}"/></c:if>">
            </div>
            <div class="input-group mb-3">
                <div class="input-group-prepend etiqueta">
                    <span class="input-group-text" id="apellido1SocioLabel"><%=meLanbide88I18n.getMensaje(idiomaUsuario,"label.apellido1")%>*</span>
                </div>
                <input type="text" class="form-control" aria-label="Sizing example input" aria-describedby="apellido1SocioLabel" id="apellido1Socio" 
                       data-toggle="tooltip" title="<%=meLanbide88I18n.getMensaje(idiomaUsuario,"label.apellido1")%>"
                       value="<c:if test="${datosModif!=null && datosModif.apellido1Socio !=null}"><c:out value="${datosModif.apellido1Socio }"/></c:if>">
            </div>
            <div class="input-group mb-3">
                <div class="input-group-prepend etiqueta">
                    <span class="input-group-text" id="apellido2SocioLabel"><%=meLanbide88I18n.getMensaje(idiomaUsuario,"label.apellido2")%></span>
                </div>
                <input type="text" class="form-control" aria-label="Sizing example input" aria-describedby="apellido2SocioLabel" id="apellido2Socio" 
                       data-toggle="tooltip" title="<%=meLanbide88I18n.getMensaje(idiomaUsuario,"label.apellido2")%>"
                       value="<c:if test="${datosModif!=null && datosModif.apellido2Socio !=null}"><c:out value="${datosModif.apellido2Socio }"/></c:if>">
            </div>
        </div>
        
        <!--
        <div class="input-group mb-3">
            <div class="input-group-prepend etiqueta">
                <span class="input-group-text" id="inputGroup-sizing-default3"><%=meLanbide88I18n.getMensaje(idiomaUsuario,"label.solicitud.trayectoria.general.otrosProgramas.fechaInicio")%></span>
            </div>
                <input type="text" class="form-control" 
                       id="trayFechaInicio" name="trayFechaInicio" maxlength="10" size="10"
                       placeholder="dd/mm/yyyy"
                       value="< c:if test="$ {datosModif!=null && datosModif.trayFechaInicio!=null}">< c:out value="$ {datosModif.trayFechaInicioString}"/></ c:if>"
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
                <span class="input-group-text" id="inputGroup-sizing-default3"><%=meLanbide88I18n.getMensaje(idiomaUsuario,"label.solicitud.trayectoria.general.otrosProgramas.fechaFin")%></span>
            </div>
            <input type="text" class="form-control" aria-label="Sizing example input" aria-describedby="inputGroup-sizing-default3" 
                   id="trayFechaFin" 
                   value="< c:if test="$ {datosModif!=null && datosModif.trayFechaFin!=null}">< c:out value="$ {datosModif.trayFechaFinString}"/></ c:if>"
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
        -->
        <div class="botonera" style="margin-top: 25px;">
            <input type="button" id="btnGuardarMelanbide88Socios" name="btnGuardarMelanbide88Socios" class="botonGeneral" value="<%=meLanbide88I18n.getMensaje(idiomaUsuario,"btn.aceptar")%>" onclick="guardarDatosMelanbide88(1);">
            <input type="button" id="btnCancelarMelanbide88Socios" name="btnCancelarMelanbide88Socios" class="botonGeneral" value="<%=meLanbide88I18n.getMensaje(idiomaUsuario,"btn.cancelar")%>" onclick="cancelarEdicionMelanbide88(1);">
        </div>
    </form>
</div>


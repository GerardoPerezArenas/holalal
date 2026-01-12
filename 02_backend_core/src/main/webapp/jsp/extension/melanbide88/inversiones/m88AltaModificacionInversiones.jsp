
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
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/calendario.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/general.js"></script>
<!--<script type="text/javascript" src="< %=request.getContextPath()%>/scripts/extension/melanbide88/moment-with-locales.js"></script>-->
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/melanbide88/gestionTrecoM88API.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/melanbide88/gestionTrecoM88AltaEdicionDatos.js"></script>
<script type="text/javascript">var APP_CONTEXT_PATH = '<%=request.getContextPath()%>';</script>

<div id="pantallaAltaEdicionMelanbide88Inversiones" class="contenidoPantalla">
    <input type="hidden" id="pantallaEditarDatos" name="pantallaEditarDatos" value="S"/>
    <input type="hidden" id="numeroExpediente" name="numeroExpediente" value="<%=numExpediente%>"/>
    <input type="hidden" id="codigoProcedimiento" name="codigoProcedimiento" value="<%=codProcedimiento%>"/>
    <input type="hidden" id="codigoOrganizacion" name="codigoOrganizacion" value="<%=codOrganizacion%>"/>
    <input type="hidden" id="urlBaseLlamadaM88" name="urlBaseLlamadaM88" value="<%=request.getContextPath()+"/PeticionModuloIntegracion.do"%>"/>
    <input type="hidden" id="identificadorBDGestionar" name="identificadorBDGestionar" value="<c:if test="${datosModif!=null && datosModif.id!=null}"><c:out value="${datosModif.id}"/></c:if>"/>
    <input type="hidden" id="msg_generico_campos_obligatorios" name="msg_generico_campos_obligatorios" value="<%=meLanbide88I18n.getMensaje(idiomaUsuario,"label.campos.obligatorios")%>"/>
    <input type="hidden" name="msg_datos_procesados_ok" id="msg_datos_procesados_ok" value="<%=meLanbide88I18n.getMensaje(idiomaUsuario,"label.datos.procesados.correctamente")%>"/>
    <input type="hidden" name="msg_datos_procesados_error" id="msg_datos_procesados_error" value="<%=meLanbide88I18n.getMensaje(idiomaUsuario,"label.datos.procesados.error")%>"/>

    <form name="formularioDatosTRECOInversiones" id="formularioDatosTRECOInversiones">
        <div class="shadow p-3 mb-5 bg-light rounded etiqueta">
            <div class="form-group">
                <h3><%=meLanbide88I18n.getMensaje(idiomaUsuario,"label.edicion.datos")%></h3>
            </div>
        </div>
        <div id="divDatosInversiones">
            <div class="input-group mb-3">
                    <div class="input-group-prepend etiqueta">
                        <span class="input-group-text" id="fechaLabel"><%=meLanbide88I18n.getMensaje(idiomaUsuario,"label.fecha")%>*</span>
                </div>
                <input type="text" class="form-control"
                       id="fecha" maxlength="10" size="10" aria-label="Sizing example input" aria-describedby="fechaLabel" 
                       data-toggle="tooltip" data-title="<%=meLanbide88I18n.getMensaje(idiomaUsuario,"label.fecha")%>"
                       placeholder="dd/mm/yyyy"
                       value="<c:if test="${datosModif!=null && datosModif.fecha !=null}"><c:out value="${datosModif.fechaString }"/></c:if>"
                       onkeypress = "return permiteSoloFormatoFechas(event);"
                       onfocus="javascript:this.select();"
                       onblur = "validarFormatoFecha(this);"
                       >
                <div id="calfecha" name="calfecha" 
                     class="input-group-append"  style="cursor: pointer;"
                     onclick='mostrarCalendarios(event,"fecha",this.id)'
                     onblur='ocultarCalendarios("fecha")'
                     >
                    <span class="input-group-text">
                        <i class="fa fa-calendar" aria-hidden="true"></i>
                    </span>             
                </div>
            </div>
            <div class="input-group mb-3">
                <div class="input-group-prepend etiqueta">
                    <span class="input-group-text" id="numFacturaLabel"><%=meLanbide88I18n.getMensaje(idiomaUsuario,"label.numero.factura")%>*</span>
                </div>
                <input type="text" class="form-control" aria-label="Sizing example input" aria-describedby="numFacturaLabel" id="numFactura" 
                       data-toggle="tooltip" title="<%=meLanbide88I18n.getMensaje(idiomaUsuario,"label.numero.factura")%>"
                       value="<c:if test="${datosModif!=null && datosModif.numFactura!=null}"><c:out value="${datosModif.numFactura}"/></c:if>">
            </div>
            <div class="input-group mb-3">
                <div class="input-group-prepend etiqueta">
                    <span class="input-group-text" id="descripcionLabel"><%=meLanbide88I18n.getMensaje(idiomaUsuario,"label.descripcion")%>*</span>
                </div>
                <input type="text" class="form-control" aria-label="Sizing example input" aria-describedby="descripcionLabel" id="descripcion" 
                       data-toggle="tooltip" title="<%=meLanbide88I18n.getMensaje(idiomaUsuario,"label.descripcion")%>"
                       value="<c:if test="${datosModif!=null && datosModif.descripcion !=null}"><c:out value="${datosModif.descripcion }"/></c:if>">
            </div>
            <div class="input-group mb-3">
                <div class="input-group-prepend etiqueta">
                    <span class="input-group-text" id="nombProveedorLabel"><%=meLanbide88I18n.getMensaje(idiomaUsuario,"label.nombre.proveedor")%>*</span>
                </div>
                <input type="text" class="form-control" aria-label="Sizing example input" aria-describedby="nombProveedorLabel" id="nombProveedor" 
                       data-toggle="tooltip" title="<%=meLanbide88I18n.getMensaje(idiomaUsuario,"label.nombre.proveedor")%>"
                       value="<c:if test="${datosModif!=null && datosModif.nombProveedor !=null}"><c:out value="${datosModif.nombProveedor }"/></c:if>">
            </div>
            <div class="input-group mb-3">
                <div class="input-group-prepend etiqueta">
                    <span class="input-group-text" id="importeLabel"><%=meLanbide88I18n.getMensaje(idiomaUsuario,"label.importe.noiva")%>*</span>
                </div>
                <input type="text" class="form-control" aria-label="Sizing example input" aria-describedby="importeLabel" id="importe" 
                       data-toggle="tooltip" title="<%=meLanbide88I18n.getMensaje(idiomaUsuario,"label.importe.noiva")%>"
                       onkeypress="return SoloDecimales(event);"
                       value="<c:if test="${datosModif!=null && datosModif.importe !=null}"><c:out value="${datosModif.importe }"/></c:if>">
            </div>
            <div class="input-group mb-3">
                <div class="input-group-prepend etiqueta">
                    <span class="input-group-text" id="pagadaLabel"><%=meLanbide88I18n.getMensaje(idiomaUsuario,"label.pagada.sino")%>*</span>
                </div>
                <select class="form-control" id="pagada" style="width: auto;"
                        data-toggle="tooltip" title="<%=meLanbide88I18n.getMensaje(idiomaUsuario,"label.pagada.sino")%>"
                        >
                    <option value="" title=""
                            <c:if test="${datosModif==null || datosModif.pagada ==null}"><c:out value="selected"/></c:if>
                            ><%=meLanbide88I18n.getMensaje(idiomaUsuario,"label.solicitud.combo.selecciona.opcion")%></option>
                    <c:forEach items="${pagadaSelect}" var="elementLista" varStatus="contador">
                        <option value="<c:out value="${elementLista.codigo}"/>" title="<c:out value="${elementLista.descripcion}"/>"
                                <c:if test="${datosModif!=null && datosModif.pagada !=null && elementLista.codigo==datosModif.pagada }"><c:out value="selected"/></c:if>
                                ><c:out value="${elementLista.codigo}"/>&nbsp;-&nbsp;<c:out value="${elementLista.descripcion}"/></option>
                    </c:forEach>
                </select>
            </div>
            <div class="input-group mb-3">
                    <div class="input-group-prepend etiqueta">
                        <span class="input-group-text" id="fechaPagoLabel"><%=meLanbide88I18n.getMensaje(idiomaUsuario,"label.fecha.pago")%>*</span>
                </div>
                <input type="text" class="form-control"
                       id="fechaPago" maxlength="10" size="10" aria-label="Sizing example input" aria-describedby="fechaPagoLabel" 
                       data-toggle="tooltip" data-title="<%=meLanbide88I18n.getMensaje(idiomaUsuario,"label.fecha.pago")%>"
                       placeholder="dd/mm/yyyy"
                       value="<c:if test="${datosModif!=null && datosModif.fechaPago !=null}"><c:out value="${datosModif.fechaPagoString }"/></c:if>"
                       onkeypress = "return permiteSoloFormatoFechas(event);"
                       onfocus="javascript:this.select();"
                       onblur = "validarFormatoFecha(this);"
                       >
                <div id="calfechaPago" name="calfechaPago" 
                     class="input-group-append"  style="cursor: pointer;"
                     onclick='mostrarCalendarios(event,"fechaPago",this.id)'
                     onblur='ocultarCalendarios("fechaPago")'
                     >
                    <span class="input-group-text">
                        <i class="fa fa-calendar" aria-hidden="true"></i>
                    </span>             
                </div>
            </div>
        </div>
        <div class="botonera" style="margin-top: 25px;">
            <input type="button" id="btnGuardarMelanbide88Inversiones" name="btnGuardarMelanbide88Inversiones" class="botonGeneral" value="<%=meLanbide88I18n.getMensaje(idiomaUsuario,"btn.aceptar")%>" onclick="guardarDatosMelanbide88(2);">
            <input type="button" id="btnCancelarMelanbide88Inversiones" name="btnCancelarMelanbide88Inversiones" class="botonGeneral" value="<%=meLanbide88I18n.getMensaje(idiomaUsuario,"btn.cancelar")%>" onclick="cancelarEdicionMelanbide88(2);">
        </div>
    </form>
</div>


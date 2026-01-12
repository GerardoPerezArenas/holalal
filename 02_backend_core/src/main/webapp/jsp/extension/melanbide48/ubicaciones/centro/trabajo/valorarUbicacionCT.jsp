<%-- 
    Document   : valorarUbicacionCT
    Created on : 19-Aug-2021, 09:48:10
    Author     : INGDGC
--%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<meta http-equiv="X-UA-Compatible" content="IE=11">


<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide48.i18n.MeLanbide48I18n" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide48.vo.ColecUbicacionesCTVO"%>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide48.vo.ColecEntidadVO"%>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide48.util.MeLanbide48Utils"%>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide48.vo.MeLanbideConvocatorias" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.agora.business.util.GeneralValueObject"%>
<%@page import="es.altia.common.service.config.Config"%>
<%@page import="es.altia.common.service.config.ConfigServiceHelper"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>

<%
            int idiomaUsuario = 1;
            int codOrganizacion  = 0;
            UsuarioValueObject usuario = new UsuarioValueObject();
            try
            {
                if (session != null) 
                {
                    if (usuario != null) 
                    {
                        usuario = (UsuarioValueObject) session.getAttribute("usuario");
                        idiomaUsuario = usuario.getIdioma();
                        codOrganizacion = usuario.getOrgCod();
                    }
                }
            }
            catch(Exception ex)
            {
                ex.printStackTrace();
            }
            //Clase para internacionalizar los mensajes de la aplicaci�n.
            MeLanbide48I18n meLanbide48I18n = MeLanbide48I18n.getInstance();

            String nombreModulo     = request.getParameter("nombreModulo");
            String numExpediente    = request.getParameter("numero");
            Integer ejercicio    = MeLanbide48Utils.getEjercicioDeExpediente(numExpediente);
            String codProcedimiento    = MeLanbide48Utils.getCodProcedimientoDeExpediente(numExpediente);
            MeLanbideConvocatorias convocatoriaActiva = (MeLanbideConvocatorias)request.getAttribute("convocatoriaActiva");
            String codigoConvocatoriaExpediente = (convocatoriaActiva!=null && convocatoriaActiva.getDecretoCodigo() !=null ? convocatoriaActiva.getDecretoCodigo() : "");
            String idBDConvocatoriaExpediente = (convocatoriaActiva!=null ? String.valueOf(convocatoriaActiva.getId()) : "");

            //ColecUbicacionesCTVO ubicacionModif = new ColecUbicacionesCTVO();
            
%>


<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/estilo.css"/>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/extension/melanbide48/melanbide48.css"/>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/bootstrap413/bootstrap.min.css" media="all"/>
<link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/css/font-awesome.min.css'/>" media="screen" />
<link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/less/animated.less'/>" media="screen" />

<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/bootstrap413/bootstrap.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script>
<script type="text/javascript" src="<c:url value='/scripts/general.js'/>"></script>
<script type="text/javascript" src="<c:url value='/scripts/validaciones.js'/>"></script>
<script type="text/javascript" src="<c:url value='/scripts/popup.js'/>"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/melanbide48/colecUtils.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/melanbide48/colecGestionValidarValorarUbicacion.js"></script>
<script type="text/javascript">var APP_CONTEXT_PATH = '<%=request.getContextPath()%>';</script>

<div class="contenidoPantalla">
    <div class="jumbotron jumbotron-fluid">
        <div class="container">
            <h1 class="display-4"><%=meLanbide48I18n.getMensaje(idiomaUsuario,"label.ubicacion.valoracion.ubicacion.ct")%></h1>
            <p class="lead">
                <c:out value="${ubicacionModif.direccion}"/>&nbsp;
                <c:if test="${ubicacionModif.direccionPortal!=null}"><c:out value="${ubicacionModif.direccionPortal}"/>&nbsp;</c:if>
                <c:if test="${ubicacionModif.direccionPiso!=null}"><c:out value="${ubicacionModif.direccionPiso}"/>&nbsp;</c:if>
                <c:if test="${ubicacionModif.direccionLetra!=null}"><c:out value="${ubicacionModif.direccionLetra}"/></c:if><br/>
                <c:if test="${ubicacionModif.telefono!=null}"><c:out value="${ubicacionModif.telefono}"/><br/></c:if>
                <c:out value="${ubicacionModif.codigoPostal}"/><br/>
                <c:out value="${ubicacionModif.descMunicipio}"/><br/>
                <c:out value="${ubicacionModif.descTerritorioHist}"/>
            </p>
            <div class="" style="float: right;margin-right: 5px;">
                <input type="button" id="btnGuardarDatosValoracion" name="btnGuardarDatosValoracion" class="botonGeneral" value="<%=meLanbide48I18n.getMensaje(idiomaUsuario,"btn.guardar")%>" /> 
            </div>
        </div>
    </div>
    <div class="container">
        <div class="form-row">
            <div class="col-sm-6"></div>
            <div class="col" style="text-align: center;"><%=meLanbide48I18n.getMensaje(idiomaUsuario,"legend.puesto.datosSolicitud")%></div>
            <div class="col" style="text-align: center;"><%=meLanbide48I18n.getMensaje(idiomaUsuario,"label.ubicacion.valoracion.datos.validacion")%></div>
        </div>
        <div class="form-row">
            <div class="col-sm-6"><%=meLanbide48I18n.getMensaje(idiomaUsuario,"label.total.meses.solicitud.sin.solapar")%></div>
            <div class="col" style="text-align: center;"><input class="form-control formControlNumeroM48" type="text" id="mesesTrayectoriaEntidad" value="<c:out value="${numTotalMesesTraySinSolap}"/>" readonly disabled/></div>
            <div class="col" style="text-align: center;"><input class="form-control formControlNumeroM48" type="text" id="mesesTrayectoriaEntidadValoracion" value="<c:if test="${entidadTrayectoriaValidada!=null}"><c:out value="${trayNumeroMesesValidados}"/></c:if>" readonly disabled/></div>
        </div>
        <div class="form-row">
            <div class="col-sm-6"><%=meLanbide48I18n.getMensaje(idiomaUsuario,"label.ubicacion.valoracion.puntos.ubicacion")%></div>
            <div class="col" style="text-align: center;"><input class="form-control formControlNumeroM48" type="text" id="puntuacionUbicacionMun" value="<c:out value="${puntuacionUbicacionMun}"/>" readonly disabled/></div>
            <div class="col" style="text-align: center;"><input class="form-control formControlNumeroM48" type="text" id="puntuacionUbicacionMunValoracion" value="<c:out value="${puntuacionUbicacionMun}"/>" readonly disabled/></div>
        </div>
        <div class="form-row">
            <div class="col-sm-6"><%=meLanbide48I18n.getMensaje(idiomaUsuario,"label.ubicacion.valoracion.puntos.segundos.locales")%></div>
            <div class="col formControlCentrado" style="text-align: center;"><input class="form-control" type="checkbox" id="segundosLocalesMismoAmbito" readonly disabled <c:if test="${entidadUbicacion!=null && entidadUbicacion.segundosLocalesMismoAmbito!=null && entidadUbicacion.segundosLocalesMismoAmbito==1}">checked</c:if>/></div>
            <div class="col formControlCentrado" style="text-align: center;"><input class="form-control" type="checkbox" id="validarSegundosLocales"  <c:if test="${colecUbicCTValoracion!=null && colecUbicCTValoracion.validarSegundosLocales!=null && colecUbicCTValoracion.validarSegundosLocales==1}">checked</c:if>/></div>
        </div>
        <div class="form-row">
            <div class="col-sm-6"><%=meLanbide48I18n.getMensaje(idiomaUsuario,"label.ubicaciones.centro.trabajo.disponeEspacioComplWifi")%></div>
            <div class="col formControlCentrado" style="text-align: center;"><input class="form-control" type="checkbox" id="espacioComplementarioWifi" readonly disabled <c:if test="${ubicacionModif!=null && ubicacionModif.disponeEspacioComplWifi!=null && ubicacionModif.disponeEspacioComplWifi==1}">checked</c:if>/></div>
            <div class="col formControlCentrado" style="text-align: center;"><input class="form-control" type="checkbox" id="validarEspacioComplem" <c:if test="${colecUbicCTValoracion!=null && colecUbicCTValoracion.validarEspacioComplem!=null && colecUbicCTValoracion.validarEspacioComplem==1}">checked</c:if>/></div>
        </div>
        <div class="form-row" title="<%=meLanbide48I18n.getMensaje(idiomaUsuario,"label.ubicacion.valoracion.puntos.atencion.colectivo3.centroespemp.title")%>">
            <div class="col-sm-6"><%=meLanbide48I18n.getMensaje(idiomaUsuario,"label.entidad.pregunta.especial")%></div>
            <div class="col formControlCentrado" style="text-align: center;"><input class="form-control" type="checkbox" id="centroEmpleoColectivo3" readonly disabled <c:if test="${entidadUbicacion!=null && entidadUbicacion.centroEspEmpTH!=null && entidadUbicacion.centroEspEmpTH==1}">checked</c:if>/></div>
            <div class="col formControlCentrado" style="text-align: center;"><input class="form-control" type="checkbox" id="validarCentroEspEmpleo" readonly disabled <c:if test="${entidadUbicacion!=null && entidadUbicacion.centroEspEmpTHValidado!=null && entidadUbicacion.centroEspEmpTHValidado==1}">checked</c:if>/></div>
        </div>
        <div class="form-row" title="<%=meLanbide48I18n.getMensaje(idiomaUsuario,"label.ubicacion.valoracion.puntos.atencion.colectivo4.empinsercion.title")%>">
            <div class="col-sm-6"><%=meLanbide48I18n.getMensaje(idiomaUsuario,"label.entidad.pregunta.insercion")%></div>
            <div class="col formControlCentrado" style="text-align: center;"><input class="form-control" type="checkbox" id="empresaInsercionColectivo4" readonly disabled <c:if test="${entidadUbicacion!=null && entidadUbicacion.empresaInsercionTH!=null && entidadUbicacion.empresaInsercionTH==1}">checked</c:if>/></div>
            <div class="col formControlCentrado" style="text-align: center;"><input class="form-control" type="checkbox" id="validarEmpresaInsercion" readonly disabled <c:if test="${entidadUbicacion!=null && entidadUbicacion.empresaInsercionTHValidado!=null && entidadUbicacion.empresaInsercionTHValidado==1}">checked</c:if>/></div>
        </div>
        <div class="form-row" title="<%=meLanbide48I18n.getMensaje(idiomaUsuario,"label.ubicacion.valoracion.puntos.atencion.colectivo4.empinsercion.title")%>">
            <div class="col-sm-6"><%=meLanbide48I18n.getMensaje(idiomaUsuario,"label.entidad.pregunta.insercion.promotor")%></div>
            <div class="col formControlCentrado" style="text-align: center;"><input class="form-control" type="checkbox" id="empresaPromoInsercionColectivo4" readonly disabled <c:if test="${entidadUbicacion!=null && entidadUbicacion.promotorEmpInsercionTH!=null && entidadUbicacion.promotorEmpInsercionTH==1}">checked</c:if>/></div>
            <div class="col formControlCentrado" style="text-align: center;"><input class="form-control" type="checkbox" id="validarPromoEmpInsercion" readonly disabled <c:if test="${entidadUbicacion!=null && entidadUbicacion.promotorEmpInsercionTHValidado!=null && entidadUbicacion.promotorEmpInsercionTHValidado==1}">checked</c:if>/></div>
        </div>
        <c:choose>
            <c:when test = "${convocatoriaActiva!=null && convocatoriaActiva.proCod=='COLEC' && convocatoriaActiva.id < 30}">
                <div class="form-row">
                    <div class="col-sm-6"><%=meLanbide48I18n.getMensaje(idiomaUsuario,"label.entidad.pregunta.plan.igualdad")%></div>
                    <div class="col formControlCentrado" style="text-align: center;"><input class="form-control" type="checkbox" id="planIgualdad" readonly disabled <c:if test="${entidadUbicacion!=null && entidadUbicacion.planIgualdad!=null && entidadUbicacion.planIgualdad==1}">checked</c:if>/></div>
                    <div class="col formControlCentrado" style="text-align: center;"><input class="form-control" type="checkbox" id="validarPlanIgualdad" readonly disabled <c:if test="${entidadUbicacion!=null && entidadUbicacion.planIgualdadValidado!=null && entidadUbicacion.planIgualdadValidado==1}">checked</c:if>/></div>
                </div>
                <div class="form-row">
                    <div class="col-sm-6"><%=meLanbide48I18n.getMensaje(idiomaUsuario,"label.entidad.pregunta.certificado.calidad")%></div>
                    <div class="col formControlCentrado" style="text-align: center;"><input class="form-control" type="checkbox" id="certificadoCalidad" readonly disabled <c:if test="${entidadUbicacion!=null && entidadUbicacion.certificadoCalidad!=null && entidadUbicacion.certificadoCalidad==1}">checked</c:if>/></div>
                    <div class="col formControlCentrado" style="text-align: center;"><input class="form-control" type="checkbox" id="validarCertificadoCalidad" readonly disabled <c:if test="${entidadUbicacion!=null && entidadUbicacion.certificadoCalidadValidado!=null && entidadUbicacion.certificadoCalidadValidado==1}">checked</c:if>/></div>
                </div>
            </c:when>
            <c:otherwise>
                <div class="form-row" title="<%=meLanbide48I18n.getMensaje(idiomaUsuario,"label.ubicacion.valoracion.ubicacion.entidad.sin.animo.lucro")%>">
                    <div class="col-sm-6"><%=meLanbide48I18n.getMensaje(idiomaUsuario,"label.entidad.pregunta.entidad.sin.animo.lucro")%></div>
                    <div class="col formControlCentrado" style="text-align: center;"><input class="form-control" type="checkbox" id="entidadSinAnimoLucro" readonly disabled <c:if test="${entidadUbicacion!=null && entidadUbicacion.entSinAnimoLucro!=null && entidadUbicacion.entSinAnimoLucro==1}">checked</c:if>/></div>
                    <div class="col formControlCentrado" style="text-align: center;"><input class="form-control" type="checkbox" id="validarEntidadSinAnimoLucro" readonly disabled <c:if test="${entidadUbicacion!=null && entidadUbicacion.entSinAnimoLucroVal!=null && entidadUbicacion.entSinAnimoLucroVal==1}">checked</c:if>/></div>
                </div>
                <div class="form-row" title="<%=meLanbide48I18n.getMensaje(idiomaUsuario,"label.ubicacion.valoracion.puntos.compromiso.igualdad")%>">
                    <div class="col-sm-6"><%=meLanbide48I18n.getMensaje(idiomaUsuario,"label.entidad.pregunta.compromiso.igualdad")%></div>
                    <div class="col formControlCentrado" style="text-align: center;"><input class="form-control formControlNumeroM48" type="text" id="compromisoIgualdadGenero" readonly disabled value="<c:if test="${entidadUbicacion!=null}"><c:out value="${entidadUbicacion.compIgualdadOpcionLiteral}"></c:out></c:if>"/></div>
                    <div class="col formControlCentrado" style="text-align: center;"><input class="form-control" type="checkbox" id="validarCompromisoIgualdadGenero"
                            value="<c:if test="${entidadUbicacion!=null && entidadUbicacion.compIgualdadOpcionVal!=null && entidadUbicacion.compIgualdadOpcionVal>0}"><c:out value="${entidadUbicacion.compIgualdadOpcionVal}"></c:out></c:if>"
                            readonly disabled <c:if test="${entidadUbicacion!=null && entidadUbicacion.compIgualdadOpcionVal!=null && entidadUbicacion.compIgualdadOpcionVal>0}">checked</c:if>/></div>
                </div>
                <div class="form-row" title="<%=meLanbide48I18n.getMensaje(idiomaUsuario,"label.ubicacion.valoracion.puntos.certificados.calidad")%>">
                    <div class="col-sm-6"><%=meLanbide48I18n.getMensaje(idiomaUsuario,"label.entidad.pregunta.certificado.calidad")%></div>
                    <div class="col"></div>
                    <div class="col"></div>
                </div>
                <c:forEach items="${listaCertificadosCalidad}" var="certificadoCalidad" varStatus="contadorCC">
                    <div class="form-row" title="<c:out value = "${certificadoCalidad.descripcion}"/>">
                        <div class="col-sm-6"><div style="margin-left:15px;"><li><c:out value = "${certificadoCalidad.descripcion}"/></li></div></div>
                        <div class="col" style="text-align: center;align-self:center;"><input class="form-control formControlCentrado" type="checkbox" id="certificadoCalidad_<c:out value="${certificadoCalidad.codigo}"/>" readonly disabled value="<c:out value="${certificadoCalidad.codigo}"/>"/></div>
                        <div class="col" style="text-align: center;align-self:center;"><input class="form-control formControlCentrado" type="checkbox" id="validarCertificadoCalidad_<c:out value="${certificadoCalidad.codigo}"/>" readonly disabled/></div>
                    </div>
                </c:forEach>
            </c:otherwise>
        </c:choose>
    </div>
    <div class="container">
        <div class="alert alert-success" role="alert">
            <hr class="my-4">
            <h4 class="alert-heading" style="text-align: center;"><%=meLanbide48I18n.getMensaje(idiomaUsuario,"label.ubicacion.valoracion.datos.seccion.valoracion")%></h4>
            <div>
                <div id="divDatosNoActualizados" style="display: none;float: right;margin-right: 5px;">
                    <small id="textoDatosNoActualizados" class="text-muted"><%=meLanbide48I18n.getMensaje(idiomaUsuario,"label.ubicacion.valoracion.puntuacion.no.actualizada")%></small>
                    <input type="button" id="btnRefrescarDatosValoracion" name="btnRefrescarDatosValoracion" class="botonLargo" onclick="refrescarCalculosApartadoValoracion()" value="<%=meLanbide48I18n.getMensaje(idiomaUsuario,"label.refrescar.datos")%>" />
                    <i class="fa fa-hand-o-down" aria-hidden="true"></i>
                </div>
                <br/>
                <div>
                    <small id="textoDatosNoRegistradosModif" class="text-muted" style="display: none;"><%=meLanbide48I18n.getMensaje(idiomaUsuario,"label.ubicacion.valoracion.puntuacion.no.registrada")%></small>
                </div>
            </div>
            <br/>
            <hr class="my-4">
            <div class="container">
                <div class="form-row">
                    <div class="col-sm-9"><%=meLanbide48I18n.getMensaje(idiomaUsuario,"label.ubicacion.valoracion.puntos.trayectoria")%></div>
                    <div class="col" style="text-align: center;"><input class="form-control formControlNumeroM48" type="text" id="puntuacionTrayectoriaEntidad" value="<c:if test="${colecUbicCTValoracion!=null && colecUbicCTValoracion.puntuacionTrayectoriaEntidad!=null}"><c:out value="${colecUbicCTValoracion.puntuacionTrayectoriaEntidad}"/></c:if>" readonly disabled/></div>
                </div>
                <div class="form-row">
                    <div class="col-sm-9"><%=meLanbide48I18n.getMensaje(idiomaUsuario,"label.ubicacion.valoracion.puntos.ubicacion")%></div>
                    <div class="col" style="text-align: center;"><input class="form-control formControlNumeroM48" type="text" id="puntuacionUbicacionCT" value="<c:if test="${colecUbicCTValoracion!=null && colecUbicCTValoracion.puntuacionUbicacionCT!=null}"><c:out value="${colecUbicCTValoracion.puntuacionUbicacionCT}"/></c:if>" readonly disabled/></div>
                </div>
                <div class="form-row">
                    <div class="col-sm-9"><%=meLanbide48I18n.getMensaje(idiomaUsuario,"label.ubicacion.valoracion.puntos.segundos.locales")%></div>
                    <div class="col" style="text-align: center;"><input class="form-control formControlNumeroM48" type="text" id="puntuacionSegundosLocales" value="<c:if test="${colecUbicCTValoracion!=null && colecUbicCTValoracion.puntuacionSegundosLocales!=null}"><c:out value="${colecUbicCTValoracion.puntuacionSegundosLocales}"/></c:if>" readonly disabled/></div>
                </div>
                <div class="form-row">
                    <c:choose>
                        <c:when test = "${convocatoriaActiva!=null && convocatoriaActiva.proCod=='COLEC' && convocatoriaActiva.id < 30}">
                            <div class="col-sm-9"><%=meLanbide48I18n.getMensaje(idiomaUsuario,"label.ubicacion.valoracion.ubicacion.plan.igualdad")%></div>
                        </c:when>
                        <c:otherwise>
                            <div class="col-sm-9"><%=meLanbide48I18n.getMensaje(idiomaUsuario,"label.entidad.pregunta.compromiso.igualdad")%></div>
                        </c:otherwise>
                    </c:choose>
                    <div class="col" style="text-align: center;"><input class="form-control formControlNumeroM48" type="text" id="puntuacionPlanIgualdad" value="<c:if test="${colecUbicCTValoracion!=null && colecUbicCTValoracion.puntuacionPlanIgualdad!=null}"><c:out value="${colecUbicCTValoracion.puntuacionPlanIgualdad}"/></c:if>" readonly disabled/></div>
                </div>
                <div class="form-row">
                    <div class="col-sm-9"><%=meLanbide48I18n.getMensaje(idiomaUsuario,"label.ubicacion.valoracion.ubicacion.certificado.calidad")%></div>
                    <div class="col" style="text-align: center;"><input class="form-control formControlNumeroM48" type="text" id="puntuacionCertificadoCalidad" value="<c:if test="${colecUbicCTValoracion!=null && colecUbicCTValoracion.puntuacionCertificadoCalidad!=null}"><c:out value="${colecUbicCTValoracion.puntuacionCertificadoCalidad}"/></c:if>" readonly disabled/></div>
                </div>
                <div class="form-row">
                    <div class="col-sm-9"><%=meLanbide48I18n.getMensaje(idiomaUsuario,"label.ubicacion.valoracion.ubicacion.espacio.complementario")%></div>
                    <div class="col" style="text-align: center;"><input class="form-control formControlNumeroM48" type="text" id="puntuacionEspacioComplem" value="<c:if test="${colecUbicCTValoracion!=null && colecUbicCTValoracion.puntuacionEspacioComplem!=null}"><c:out value="${colecUbicCTValoracion.puntuacionEspacioComplem}"/></c:if>" readonly disabled/></div>
                </div>
                <div class="form-row">
                    <div class="col-sm-9"><%=meLanbide48I18n.getMensaje(idiomaUsuario,"label.ubicacion.valoracion.ubicacion.centroemplo.colectivo3")%></div>
                    <div class="col" style="text-align: center;"><input class="form-control formControlNumeroM48" type="text" id="puntuacionCentroEspEmpleo" value="<c:if test="${colecUbicCTValoracion!=null && colecUbicCTValoracion.puntuacionCentroEspEmpleo!=null}"><c:out value="${colecUbicCTValoracion.puntuacionCentroEspEmpleo}"/></c:if>" readonly disabled/></div>
                </div>
                <div class="form-row">
                    <div class="col-sm-9"><%=meLanbide48I18n.getMensaje(idiomaUsuario,"label.ubicacion.valoracion.ubicacion.empinsercio.promoempinsercion.colectivo4")%></div>
                    <div class="col" style="text-align: center;"><input class="form-control formControlNumeroM48" type="text" id="puntuacionEmpOpromEmpInsercion" value="<c:if test="${colecUbicCTValoracion!=null && colecUbicCTValoracion.puntuacionEmpOpromEmpInsercion!=null}"><c:out value="${colecUbicCTValoracion.puntuacionEmpOpromEmpInsercion}"/></c:if>" readonly disabled/></div>
                </div>
                <div class="form-row">
                    <div class="col-sm-9"><%=meLanbide48I18n.getMensaje(idiomaUsuario,"label.ubicacion.valoracion.ubicacion.observaciones")%></div>
                    <div class="col" style="text-align: center;"><textarea class="form-control" id="puntuacionObservaciones"><c:if test="${colecUbicCTValoracion!=null && colecUbicCTValoracion.puntuacionObservaciones!=null}"><c:out value="${colecUbicCTValoracion.puntuacionObservaciones}"/></c:if></textarea></div>
                </div>                        
                <div class="form-row">
                    <div class="col-sm-9"><%=meLanbide48I18n.getMensaje(idiomaUsuario,"label.ubicacion.valoracion.ubicacion.total.puntuacion")%></div>
                    <div class="col" style="text-align: center;"><input class="form-control formControlNumeroM48" type="text" id="puntuacionTotal" value="" readonly disabled/></div>
                </div>                        
            </div>
        </div>
    </div>
    <input type="hidden" id="pantallaEditarDatos" name="pantallaEditarDatos" value="VALORAR"/>
    <input type="hidden" id="codigoConvocatoriaExpediente" name="codigoConvocatoriaExpediente" value="<%=codigoConvocatoriaExpediente%>"/>
    <input type="hidden" id="idBDConvocatoriaExpediente" name="idBDConvocatoriaExpediente" value="<%=idBDConvocatoriaExpediente%>"/>
    <input type="hidden" id="idiomaUsuario" name="idiomaUsuario" value="<%=idiomaUsuario%>"/>
    <input type="hidden" id="numeroExpediente" name="numeroExpediente" value="<%=numExpediente%>"/>
    <input type="hidden" id="ejercicio" name="ejercicio" value="<%=ejercicio%>"/>
    <input type="hidden" id="codigoProcedimiento" name="codigoProcedimiento" value="<%=codProcedimiento%>"/>
    <input type="hidden" id="codigoOrganizacion" name="codigoOrganizacion" value="<%=codOrganizacion%>"/>
    <input type="hidden" id="urlBaseLlamadaM48" name="urlBaseLlamadaM48" value="<%=request.getContextPath()+"/PeticionModuloIntegracion.do"%>"/>
    <input type="hidden" id="idBDColecUbicacionesCT" name="idBDColecUbicacionesCT" value="<c:out value="${ubicacionModif.codId}"/>"/>
    <input type="hidden" id="idBDColecUbicCTValoracion" name="idBDColecUbicCTValoracion" value="<c:if test="${colecUbicCTValoracion!=null && colecUbicCTValoracion.id!=null}"><c:out value="${colecUbicCTValoracion.id}"/></c:if>"/>
    <input type="hidden" id="idBDEntidad" name="idBDEntidad" value="<c:if test="${entidadUbicacion!=null}"><c:out value="${entidadUbicacion.codEntidad}"></c:out></c:if>"/>
</div>


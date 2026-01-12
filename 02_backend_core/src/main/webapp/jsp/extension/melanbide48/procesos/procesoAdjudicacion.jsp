<%-- 
    Document   : procesoAdjudicacion
    Created on : 14-Sep-2021, 16:26:17
    Author     : INGDGC
--%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<meta http-equiv="X-UA-Compatible" content="IE=11">

<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide48.i18n.MeLanbide48I18n" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
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
            //Clase para internacionalizar los mensajes de la aplicación.
            MeLanbide48I18n meLanbide48I18n = MeLanbide48I18n.getInstance();
            String codProcedimiento     = request.getParameter("codProcedimiento");
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
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/melanbide48/colecGestionProcesoAdjudicacion.js"></script>
<script type="text/javascript">var APP_CONTEXT_PATH = '<%=request.getContextPath()%>';</script>

<div class="contenidoPantalla">
    <div class="jumbotron jumbotron-fluid">
        <div class="container">
            <h1 class="display-4"><%=meLanbide48I18n.getMensaje(idiomaUsuario,"procesos.adjudicacion.titulo")%></h1>
            <div class="lead">
                <div class="form-group">
                    <label for="listaConvocatorias"><%=meLanbide48I18n.getMensaje(idiomaUsuario,"label.trayectoria.convocatoria")%></label>
                    <select class="form-control" id="listaConvocatorias">
                        <option value=""><%=meLanbide48I18n.getMensaje(idiomaUsuario,"label.solicitud.combo.selecciona.opcion")%></option>
                    </select>
                </div>
            </div>
        </div>
    </div>
    <div class="container">
        <ul class="list-group">
            <li class="list-group-item disabled"><span><%=meLanbide48I18n.getMensaje(idiomaUsuario,"procesos.adjudicacion.resolucion.provisional")%></span></li>
            <li class="list-group-item disabled"><button type="button" id="btnEjecutarPrcoceso" class="btn btn-primary btn-lg btn-block"><%=meLanbide48I18n.getMensaje(idiomaUsuario,"procesos.adjudicacion.ejecutar")%>&nbsp;<i class="fa fa-cogs" aria-hidden="true"></i></button>
                <div class="list-group-item disabled" id="expedientesEstadoNoOK" style="display: none;">
                    <span id="tituloListaExpedientesEstadoNoOK"></span>
                    <hr/>
                    <div id="listaExpedientesEstadoNoOK"></div>
                </div>
            </li>
            <li class="list-group-item"><button type="button" id="btnGenerarExcel" class="btn btn-primary btn-lg btn-block"><%=meLanbide48I18n.getMensaje(idiomaUsuario,"procesos.adjudicacion.get.excel")%>&nbsp;<i class="fa fa-file-excel-o" aria-hidden="true"></i></button></li>
            <li class="list-group-item disabled"><span><%=meLanbide48I18n.getMensaje(idiomaUsuario,"procesos.adjudicacion.resolucion.consolidacion  ")%></span></li>
        </ul>
    </div>                
    <input type="hidden" id="idiomaUsuario" name="idiomaUsuario" value="<%=idiomaUsuario%>"/>
    <input type="hidden" id="codigoOrganizacion" name="codigoOrganizacion" value="<%=codOrganizacion%>"/>
    <input type="hidden" id="urlBaseLlamadaM48" name="urlBaseLlamadaM48" value="<%=request.getContextPath()+"/PeticionModuloIntegracion.do"%>"/>    
    <input type="hidden" id="codigoProcedimiento" name="codigoProcedimiento" value="<%=codProcedimiento%>"/>
    <input type="hidden" id="textoExpedientesEstadoNoOk" name="textoExpedientesEstadoNoOk" value="<%=meLanbide48I18n.getMensaje(idiomaUsuario,"procesos.adjudicacion.expedientes.estado.incorrecto")%>"/>
</div>

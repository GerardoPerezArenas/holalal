<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>

<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide88.i18n.MeLanbide88I18n" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide88.util.ConfigurationParameter"%>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide88.util.ConstantesMeLanbide88"%>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide88.util.MeLanbide88Utils"%>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide88.vo.MeLanbideConvocatorias" %>
<%@page import="java.util.ArrayList" %>
<%@page import="java.util.List" %>


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

    
    String urlPestanaDatos_socios = (String)request.getAttribute("urlPestanaDatos_socios");
    String urlPestanaDatos_inversiones = (String)request.getAttribute("urlPestanaDatos_inversiones");
    String urlPestanaDatos_subvenciones = (String)request.getAttribute("urlPestanaDatos_subvenciones");
    
    MeLanbideConvocatorias convocatoriaActiva = (MeLanbideConvocatorias)request.getAttribute("convocatoriaActiva");
    String codigoConvocatoriaExpediente = (convocatoriaActiva!=null && convocatoriaActiva.getDecretoCodigo() !=null ? convocatoriaActiva.getDecretoCodigo() : "");
    Integer idBDConvocatoriaExpediente = (convocatoriaActiva!=null ? convocatoriaActiva.getId() : 0);
    
%>


<jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
<jsp:setProperty name="descriptor"  property="idi_cod" value="<%=idiomaUsuario%>" />
<jsp:setProperty name="descriptor"  property="apl_cod" value="<%=apl%>" />
<meta http-equiv="X-UA-Compatible" content="IE=11">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/estilo.css"/>
<link rel="stylesheet" type="text/css" href="<c:url value='/css/table.css'/>">
<link rel="stylesheet" type="text/css" href="<c:url value='/css/extension/melanbide88/melanbide88.css'/>">
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/bootstrap413/bootstrap.min.css" media="all"/>
<link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/css/font-awesome.min.css'/>" media="screen" />
<link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/less/animated.less'/>" media="screen" />

<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/bootstrap413/bootstrap.min.js"></script>
<script type="text/javascript" src="<c:url value='/scripts/popup.js'/>"></script>
<script type="text/javascript" src="<c:url value='/scripts/general.js'/>"></script>
<script type="text/javascript" src="<c:url value='/scripts/extension/melanbide88/gestionTrecoM88API.js'/>"></script>
<script type="text/javascript" src="<c:url value='/scripts/extension/melanbide88/gestionTrecoM88.js'/>"></script>


<script type="text/javascript">
    var tpM88;
    var mensajeValidacion = '';
</script>

<div>
    <div class="tab-page" id="tabPageM88">
        <!-- Datos Comunes para las pestanas -->
        <input type="hidden" id="codigoConvocatoriaExpediente" name="codigoConvocatoriaExpediente" value="<%=codigoConvocatoriaExpediente%>"/>
        <input type="hidden" id="idBDConvocatoriaExpediente" name="idBDConvocatoriaExpediente" value="<%=idBDConvocatoriaExpediente%>"/>
        <input type="hidden" id="numeroExpediente" name="numeroExpediente" value="<%=numExpediente%>"/>
        <input type="hidden" id="codigoOrganizacion" name="codigoOrganizacion" value="<%=codOrganizacion%>"/>
        <input type="hidden" id="codigoProcedimiento" name="codigoProcedimiento" value="<%=codProcedimiento%>"/>
        <input type="hidden" id="urlBaseLlamadaM88" name="urlBaseLlamadaM88" value="<%=request.getContextPath()+"/PeticionModuloIntegracion.do"%>"/>
        <input type="hidden" id="idiomaUsuario" name="idiomaUsuario" value="<%=idiomaUsuario%>"/>
        <!--Literales para cabecera herramientas busqueda Tabla -->
        <input type="hidden" id="descriptor_buscar" value="<%=descriptor.getDescripcion("buscar")%>"/>
        <input type="hidden" id="descriptor_anterior" value="<%=descriptor.getDescripcion("anterior")%>"/>
        <input type="hidden" id="descriptor_siguiente" value="<%=descriptor.getDescripcion("siguiente")%>"/>
        <input type="hidden" id="descriptor_mosFilasPag" value="<%=descriptor.getDescripcion("mosFilasPag")%>"/>
        <input type="hidden" id="descriptor_msgNoResultBusq" value="<%=descriptor.getDescripcion("msgNoResultBusq")%>"/>
        <input type="hidden" id="descriptor_mosPagDePags" value="<%=descriptor.getDescripcion("mosPagDePags")%>"/>
        <input type="hidden" id="descriptor_noRegDisp" value="<%=descriptor.getDescripcion("noRegDisp")%>"/>
        <input type="hidden" id="descriptor_filtrDeTotal" value="<%=descriptor.getDescripcion("filtrDeTotal")%>"/>
        <input type="hidden" id="descriptor_primero" value="<%=descriptor.getDescripcion("primero")%>"/>
        <input type="hidden" id="descriptor_ultimo" value="<%=descriptor.getDescripcion("ultimo")%>"/>
        <input type="hidden" id="msg_msjNoSelecFila" value="<%=meLanbide88I18n.getMensaje(idiomaUsuario, "msg.msjNoSelecFila")%>"/>
        <input type="hidden" id="label_si" value="<%=meLanbide88I18n.getMensaje(idiomaUsuario, "label.si").toUpperCase()%>"/>
        <input type="hidden" id="label_no" value="<%=meLanbide88I18n.getMensaje(idiomaUsuario, "label.no").toUpperCase()%>"/>
        <input type="hidden" name="textoDesplegableSeleccionaOpcion" id="textoDesplegableSeleccionaOpcion" value="<%=meLanbide88I18n.getMensaje(idiomaUsuario,"label.solicitud.combo.selecciona.opcion")%>"/>
        <input type="hidden" name="msg_seguro_eliminar" id="msg_seguro_eliminar" value="<%=meLanbide88I18n.getMensaje(idiomaUsuario,"label.seguro.eliminar.registro")%>"/>
        <input type="hidden" name="msg_datos_procesados_ok" id="msg_datos_procesados_ok" value="<%=meLanbide88I18n.getMensaje(idiomaUsuario,"label.datos.procesados.correctamente")%>"/>
        <input type="hidden" name="msg_datos_procesados_error" id="msg_datos_procesados_error" value="<%=meLanbide88I18n.getMensaje(idiomaUsuario,"label.datos.procesados.error")%>"/>
        <!-- Literales coumnes para cabecera de tablas -->
        <input type="hidden" id="label_tabla_columna_fecha" value="<%=meLanbide88I18n.getMensaje(idiomaUsuario,"label.fecha")%>"/>
        <input type="hidden" id="label_tabla_columna_importe_noiva" value="<%=meLanbide88I18n.getMensaje(idiomaUsuario,"label.importe.noiva")%>"/>
        <input type="hidden" id="label_tabla_columna_importe_euros" value="<%=meLanbide88I18n.getMensaje(idiomaUsuario,"label.importe.euros")%>"/>
        <!-- Fin Datos Comunes para las pestanas -->
        <h2 class="tab" id="pestanaM88"><%=meLanbide88I18n.getMensaje(idiomaUsuario,"label.tituloPestana.principal")%></h2>
        <script type="text/javascript">var tp1_pM88 = tp1.addTabPage(document.getElementById("tabPageM88"));</script>
        <div style="clear: both;">
            <div>
                <div style="clear: both; width: 100%; text-align: left; padding-top: 10px;">
                    <div id="tab-panel-m88" class="tab-pane" style="float: left; font-size: 13px;" align="center"></div>
                    <script type="text/javascript">
                        tpM88 = new WebFXTabPane(document.getElementById("tab-panel-m88"));
                        tpM88.selectedIndex = 0;
                    </script>
                    <div class="tab-page" id="tabPageM88_1" style="height: 250px; font-size: 13px;">
                        <h2 class="tab" id="pestanaM88_1"> <%=meLanbide88I18n.getMensaje(idiomaUsuario,"label.tituloPestana.socios")%></h2>
                        <script type="text/javascript">tpM88.addTabPage(document.getElementById("tabPageM88_1"));</script>
                        <div style="clear: both;">
                            <jsp:include page="<%=urlPestanaDatos_socios%>" flush="true"/>
                        </div>
                    </div>
                    <div class="tab-page" id="tabPageM88_2" style="height: 250px; font-size: 13px;">
                        <h2 class="tab" id="pestanaM88_2"><%=meLanbide88I18n.getMensaje(idiomaUsuario,"label.tituloPestana.inversiones")%></h2>
                        <script type="text/javascript">tpM88.addTabPage(document.getElementById("tabPageM88_2"));</script>
                        <div style="clear: both;">
                            <jsp:include page="<%=urlPestanaDatos_inversiones%>" flush="true">
                            </jsp:include>
                        </div>
                    </div>
                    <div class="tab-page" id="tabPageM88_3" style="height: 250px; font-size: 13px;">
                        <h2 class="tab" id="pestanaM88_3"><%=meLanbide88I18n.getMensaje(idiomaUsuario,"label.tituloPestana.subvenciones")%></h2>
                        <script type="text/javascript">tpM88.addTabPage(document.getElementById("tabPageM88_3"));</script>
                        <div style="clear: both;">
                            <jsp:include page="<%=urlPestanaDatos_subvenciones%>" flush="true"/>
                        </div>
                    </div>
                </div>
            </div>
        </div> 
    </div>
</div>

<script type="text/javascript">

</script>
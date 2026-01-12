<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>

<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide48.i18n.MeLanbide48I18n" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide48.vo.SelectItem"%>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide48.util.ConfigurationParameter"%>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide48.util.ConstantesMeLanbide48"%>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide48.util.MeLanbide48Utils"%>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide48.vo.MeLanbideConvocatorias" %>
<%@ page import="es.altia.common.service.config.Config"%>
<%@ page import="es.altia.common.service.config.ConfigServiceHelper"%>
<%@page import="java.util.ArrayList" %>
<%@page import="java.util.List" %>
<%@page import="org.apache.commons.lang.StringEscapeUtils"%>

<%@page import="es.altia.agora.business.util.GeneralValueObject"%>
<%@page import="es.altia.agora.interfaces.user.web.sge.FichaExpedienteForm"%>

<%
    String sIdioma = request.getParameter("idioma");
    int apl = 5;
    int idiomaUsuario = (sIdioma!=null && sIdioma!="" ? Integer.parseInt(sIdioma) : 1);
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
            }
        }
    }
    catch(Exception ex)
    {
        
    }
    //Clase para internacionalizar los mensajes de la aplicaci�n.
    MeLanbide48I18n meLanbide48I18n = MeLanbide48I18n.getInstance();
    
    Config m_Config = ConfigServiceHelper.getConfig("common");
    String statusBar = m_Config.getString("JSP.StatusBar");
    String nombreModulo     = request.getParameter("nombreModulo");
    String codOrganizacion  = request.getParameter("codOrganizacionModulo");
    String numExpediente    = request.getParameter("numero");
    String codProcedimiento    = MeLanbide48Utils.getCodProcedimientoDeExpediente(numExpediente);
    Integer ejercicioExpediente    = MeLanbide48Utils.getEjercicioDeExpediente(numExpediente);
    String mensajeProgreso = "";
    
    String urlPestanaDatos_asociaciones = (String)request.getAttribute("urlPestanaDatos_asociaciones");
    String urlPestanaDatos_solicitudes = (String)request.getAttribute("urlPestanaDatos_solicitudes");
    String urlPestanaDatos_colectivosyTHSol = (String)request.getAttribute("urlPestanaDatos_colectivosyTHSol");
    String urlPestanaDatos_experPreviaArt53 = (String)request.getAttribute("urlPestanaDatos_experPreviaArt53");
    String urlPestanaDatos_trayectoriaGeneral = (String)request.getAttribute("urlPestanaDatos_trayectoriaGeneral");
    String urlPestanaDatos_ubicacionesCT = (String)request.getAttribute("urlPestanaDatos_ubicacionesCT");
    String urlPestanaDatos_experienciaAcreditable = (String)request.getAttribute("urlPestanaDatos_experienciaAcreditable");
    
    MeLanbideConvocatorias convocatoriaActiva = (MeLanbideConvocatorias)request.getAttribute("convocatoriaActiva");
    String codigoConvocatoriaExpediente = (convocatoriaActiva!=null && convocatoriaActiva.getDecretoCodigo() !=null ? convocatoriaActiva.getDecretoCodigo() : "");
    Integer idBDConvocatoriaExpediente = (convocatoriaActiva!=null ? convocatoriaActiva.getId() : 0);
    
    boolean tramRes = false;
    boolean tramResModif = false;
    
    try
    {
        if(request.getAttribute("tramRes") != null)
        {
            tramRes = (Boolean)request.getAttribute("tramRes");
        }
        if(request.getAttribute("tramResModif") != null)
        {
            tramResModif = (Boolean)request.getAttribute("tramResModif");
        }
    }
    catch(Exception ex)
    {
        
    }
    
    FichaExpedienteForm expForm = (FichaExpedienteForm) session.getAttribute("FichaExpedienteForm");
    GeneralValueObject expedienteVO = expForm.getExpedienteVO();
    
    String userAgent = request.getHeader("user-agent");
    String margenIzqPestanasDatosColec = "";

    if(userAgent.indexOf("MSIE")!=-1) {
        // Internet Explorer
        margenIzqPestanasDatosColec = "";
    }else{
        // Firefox u otro navegador
        margenIzqPestanasDatosColec = "margin-left:10px;";
    }
%>

<%!
    // Funcion para escapar strings para javascript
    private String escape(String str) 
    {
        return StringEscapeUtils.escapeJavaScript(str);
    }
%>

<jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
<jsp:setProperty name="descriptor"  property="idi_cod" value="<%=idiomaUsuario%>" />
<jsp:setProperty name="descriptor"  property="apl_cod" value="<%=apl%>" />


<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/estilo.css"/>
<link rel="stylesheet" type="text/css" href="<c:url value='/css/table.css'/>">
<link rel="stylesheet" type="text/css" href="<c:url value='/css/extension/melanbide48/melanbide48.css'/>">
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/bootstrap413/bootstrap.min.css" media="all"/>
<link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/css/font-awesome.min.css'/>" media="screen" />
<link rel="stylesheet" type="text/css" href="<c:url value='/css/font-awesome-4.6.2/less/animated.less'/>" media="screen" />

<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-1.9.1.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/bootstrap413/bootstrap.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/melanbide48/TablaNuevaColec.js"></script>
<script type="text/javascript" src="<c:url value='/scripts/popup.js'/>"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/extension/melanbide48/colecUtils.js"></script>
<script type="text/javascript" src="<c:url value='/scripts/general.js'/>"></script>
<script type="text/javascript" src="<c:url value='/scripts/calendario.js'/>"></script>
<script type="text/javascript" src="<c:url value='/scripts/validaciones.js'/>"></script>
<script type="text/javascript" src="<c:url value='/scripts/listaComboBox.js'/>"></script>


<script type="text/javascript">   
    var tp39;
    var mensajeValidacion = '';
    
    function refrescarPestanasColec(){
        if("CONV_ANTE-2021"===$("#codigoConvocatoriaExpediente").val()){
            refrescarPestanaTrayectoriaColec(); // En Jsp trayectoria ( nueva convocatoria metodono disponible.
        }else{
            recargarPestanaDatosCompRealxColeTH(); 
            refrescarPestanaExperienciaAcreditable();
        }
        refrescarPestanaUbicacionesCentrosTrabajo();
    }
</script>

<body>
    <div class="tab-page" id="tabPage391" style="height:480px; width: 100%; font-size: 13px;">
        <!-- Datos Comunes para las pestanas -->
        <input type="hidden" id="codigoConvocatoriaExpediente" name="codigoConvocatoriaExpediente" value="<%=codigoConvocatoriaExpediente%>"/>
        <input type="hidden" id="idBDConvocatoriaExpediente" name="idBDConvocatoriaExpediente" value="<%=idBDConvocatoriaExpediente%>"/>
        <input type="hidden" id="numeroExpediente" name="numeroExpediente" value="<%=numExpediente%>"/>
        <input type="hidden" id="ejercicioExpediente" name="ejercicioExpediente" value="<%=ejercicioExpediente%>"/>
        <input type="hidden" id="codigoProcedimiento" name="codigoProcedimiento" value="<%=codProcedimiento%>"/>
        <input type="hidden" id="codigoOrganizacion" name="codigoOrganizacion" value="<%=codOrganizacion%>"/>
        <input type="hidden" id="urlBaseLlamadaM48" name="urlBaseLlamadaM48" value="<%=request.getContextPath()+"/PeticionModuloIntegracion.do"%>"/>
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
        <input type="hidden" id="msg_msjNoSelecFila" value="<%=meLanbide48I18n.getMensaje(idiomaUsuario, "msg.msjNoSelecFila")%>"/>
        <input type="hidden" id="label_si" value="<%=meLanbide48I18n.getMensaje(idiomaUsuario, "label.si").toUpperCase()%>"/>
        <input type="hidden" id="label_no" value="<%=meLanbide48I18n.getMensaje(idiomaUsuario, "label.no").toUpperCase()%>"/>
        <input type="hidden" name="textoDesplegableSeleccionaOpcion" id="textoDesplegableSeleccionaOpcion" value="<%=meLanbide48I18n.getMensaje(idiomaUsuario,"label.solicitud.combo.selecciona.opcion")%>"/>
        <!-- Literales coumnes para cabecera de tablas -->
        <input type="hidden" id="lable_tabla_columna_entidad" value="<%=meLanbide48I18n.getMensaje(idiomaUsuario,"label.entidad.nombre")%>"/>
        <input type="hidden" id="lable_tabla_columna_programa_convocatoria" value="<%=meLanbide48I18n.getMensaje(idiomaUsuario,"label.programa.convocatoria")%>"/>
        <input type="hidden" id="lable_tabla_columna_experiencia_previa" value="<%=meLanbide48I18n.getMensaje(idiomaUsuario,"label.experienciaprevia.articulo53")%>"/>
        <input type="hidden" id="lable_tabla_columna_nombre_adminpublica" value="<%=meLanbide48I18n.getMensaje(idiomaUsuario,"label.solicitud.trayectoria.colectivo1.nombreAdm")%>"/>
        <input type="hidden" id="lable_tabla_columna_descripcion_actividad" value="<%=meLanbide48I18n.getMensaje(idiomaUsuario,"label.solicitud.trayectoria.colectivo1.descActividad")%>"/>
        <input type="hidden" id="lable_tabla_columna_fecha_inicio" value="<%=meLanbide48I18n.getMensaje(idiomaUsuario,"label.solicitud.trayectoria.general.otrosProgramas.fechaInicio")%>"/>
        <input type="hidden" id="lable_tabla_columna_fecha_fin" value="<%=meLanbide48I18n.getMensaje(idiomaUsuario,"label.solicitud.trayectoria.general.otrosProgramas.fechaFin")%>"/>
        <input type="hidden" id="lable_meses" value="<%=meLanbide48I18n.getMensaje(idiomaUsuario,"label.meses")%>"/>
        <input type="hidden" id="label_meses_validar" value="<%=meLanbide48I18n.getMensaje(idiomaUsuario,"label.meses.validar")%>"/>
        <input type="hidden" id="label_tabla_columna_colectivo" value="<%=meLanbide48I18n.getMensaje(idiomaUsuario,"lable.tabla.colectivo.solicitado.th.col1")%>"/>
        <!-- Fin Datos Comunes para las pestanas -->
        <h2 class="tab" id="pestana391"><%=meLanbide48I18n.getMensaje(idiomaUsuario,"label.datosColec.tituloPestana")%></h2>
        <script type="text/javascript">tp1_p391 = tp1.addTabPage( document.getElementById( "tabPage391" ) );</script>
        <div style="clear: both;">
            <div>
                <div style="clear: both; width: 100%; text-align: left; padding-top: 10px;">
                    <div id="tab-panel-391" class="tab-pane" style="float: left; font-size: 13px;" align="center"></div>
                    <script type="text/javascript"> 
                        tp391 = new WebFXTabPane(document.getElementById("tab-panel-391"));
                        tp391.selectedIndex = 0;
                    </script>
                    <div class="tab-page" id="tabPage3911" style="height: 250px; font-size: 13px;">
                        <h2 class="tab" id="pestana3911" style="<%=margenIzqPestanasDatosColec%>"><%=meLanbide48I18n.getMensaje(idiomaUsuario,"label.asociacion.tituloPestana")%></h2>
                        <script type="text/javascript">tp391.addTabPage(document.getElementById("tabPage3911"));</script>
                        <div style="clear: both;">
                            <jsp:include page="<%=urlPestanaDatos_asociaciones%>" flush="true"/>
                        </div>
                    </div>
                    <div class="tab-page" id="tabPage3912" style="height: 250px; font-size: 13px;">
                        <h2 class="tab" id="pestana3912" style="<%=margenIzqPestanasDatosColec%>"><%=meLanbide48I18n.getMensaje(idiomaUsuario,"label.colectivoyTH.solicitado")%></h2>
                        <script type="text/javascript">tp391.addTabPage(document.getElementById("tabPage3912"));</script>
                        <div style="clear: both;">
                            <jsp:include page="<%=urlPestanaDatos_colectivosyTHSol%>" flush="true"/>
                        </div>
                    </div>
                        <c:choose>
                            <c:when test = "${convocatoriaActivaTest!=null && convocatoriaActivaTest.proCod=='COLEC' && convocatoriaActivaTest.id > 2}">
                                <div class="tab-page" id="tabPageM48ExpAcre" style="height: 250px; font-size: 13px;">
                                    <h2 class="tab" id="pestanaM48ExpAcre" style="<%=margenIzqPestanasDatosColec%>"><%=meLanbide48I18n.getMensaje(idiomaUsuario,"label.title.trayectoria.experiencia.acreditable")%></h2>
                                    <script type="text/javascript">tp391.addTabPage( document.getElementById( "tabPageM48ExpAcre" ) );</script>
                                    <div style="clear: both;">
                                        <jsp:include page="<%=urlPestanaDatos_experienciaAcreditable%>" flush="true"/>
                                    </div>
                                </div>
                                <div class="tab-page" id="tabPageM48ExpAcreResumen" style="height: 250px; font-size: 13px;">
                                    <h2 class="tab" id="pestanaM48ExpAcreResumen" style="<%=margenIzqPestanasDatosColec%>"><%=meLanbide48I18n.getMensaje(idiomaUsuario,"label.title.trayectoria.experiencia.acreditable.resumen")%></h2>
                                    <script type="text/javascript">tp391.addTabPage(document.getElementById( "tabPageM48ExpAcreResumen" ) );</script>
                                    <div style="clear: both;">
                                        <jsp:include page="/jsp/extension/melanbide48/experiencia/acreditable/experienciaAcreditableResumen.jsp" flush="true"/>
                                    </div>
                                </div>
                            </c:when>
                            <c:otherwise>
                                <div class="tab-page" id="tabPage3913" style="height: 250px; font-size: 13px;">
                                    <h2 class="tab" id="pestana3913" style="<%=margenIzqPestanasDatosColec%>"><%=meLanbide48I18n.getMensaje(idiomaUsuario,"label.experienciaprevia.articulo53")%></h2>
                                    <script type="text/javascript">tp391.addTabPage( document.getElementById( "tabPage3913" ) );</script>
                                    <div style="clear: both;">
                                        <jsp:include page="<%=urlPestanaDatos_experPreviaArt53%>" flush="true"/>
                                    </div>
                                </div>
                                <div class="tab-page" id="tabPage3914" style="height: 250px; font-size: 13px;">
                                    <h2 class="tab" id="pestana3914" style="<%=margenIzqPestanasDatosColec%>"><%=meLanbide48I18n.getMensaje(idiomaUsuario,"label.trayectoria.general")%></h2>
                                    <script type="text/javascript">tp391.addTabPage( document.getElementById( "tabPage3914" ) );</script>
                                    <div style="clear: both;">
                                        <jsp:include page="<%=urlPestanaDatos_trayectoriaGeneral%>" flush="true"/>
                                    </div>
                                </div>
                            </c:otherwise>
                        </c:choose>
                    <div class="tab-page" id="tabPage3915" style="height: 250px; font-size: 13px;">
                        <h2 class="tab" id="pestana3915" style="<%=margenIzqPestanasDatosColec%>"><%=meLanbide48I18n.getMensaje(idiomaUsuario,"label.ubicaciones.centros")%></h2>
                        <script type="text/javascript">tp391.addTabPage( document.getElementById( "tabPage3915" ) );</script>
                        <div style="clear: both;">
                            <jsp:include page="<%=urlPestanaDatos_ubicacionesCT%>" flush="true"/>
                        </div>
                    </div>
<!--                    <div class="tab-page" id="tabPage3916" style="height: 194px; font-size: 13px;">
                        <h2 class="tab" id="pestana3916" style="< %=margenIzqPestanasDatosColec%>">< %=meLanbide48I18n.getMensaje(idiomaUsuario,"label.solicitud.tituloPestana")%></h2>
                        <script type="text/javascript">tp391.addTabPage( document.getElementById( "tabPage3915" ) );</script>
                        <div style="clear: both;">
                            < jsp:include page="< %=urlPestanaDatos_solicitudes%>" flush="true"/>
                        </div>
                    </div>-->
                </div>
            </div>
        </div> 
    </div>
</body>

<script type="text/javascript">    
    
</script>
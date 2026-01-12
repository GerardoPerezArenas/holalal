<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>

<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="es.altia.flexia.integracion.moduloexterno.lanbide01.vo.Melanbide01HistoSubv"%>
<%@page import="org.apache.logging.log4j.Logger"%>
<%@page import="org.apache.logging.log4j.LogManager"%>
<%@page import="com.google.gson.Gson" %>
<%@page import="com.google.gson.GsonBuilder" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.lanbide01.vo.Melanbide01Decreto"%>
<meta http-equiv="X-UA-Compatible" content="IE=Edge" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<%
    Logger log = LogManager.getLogger(Melanbide01HistoSubv.class);
    String sIdioma = request.getParameter("idioma");
    int idiomaUsuario = (sIdioma!= null && !sIdioma.isEmpty() ? Integer.parseInt(sIdioma):1);
    String nombreModulo = request.getParameter("nombreModulo");
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
        log.error("Error en ls JSP al leer CSS e IDIOMA ususario.",ex);
    }
    List<Melanbide01HistoSubv> listaHistorialSubvencionExpte = (ArrayList<Melanbide01HistoSubv>)request.getAttribute("listaHistorialSubvencionExpte");
    Gson gson = new Gson();
    GsonBuilder gsonB = new GsonBuilder().setDateFormat("dd/MM/yyyy");
    gsonB.serializeNulls();  
    gson=gsonB.create();
    String listaResponseJsonStringHistorial = gson.toJson(listaHistorialSubvencionExpte);
    
%>

<jsp:useBean id="configuracion" scope="request" class="es.altia.flexia.integracion.moduloexterno.lanbide01.util.ConfigurationParameter"  type="es.altia.flexia.integracion.moduloexterno.lanbide01.util.ConfigurationParameter" />
<jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
<jsp:setProperty name="descriptor"  property="idi_cod" value="<%=idiomaUsuario%>" />
<jsp:setProperty name="descriptor"  property="apl_cod" value="<%=apl%>" />
<link rel="StyleSheet" media="screen" type="text/css" href="<%=request.getContextPath()%><%=css%>"/>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/>

<div id="bodyDiv">
    <div style="clear: both;">
        <label class="legendAzul" style="text-align: center; position: relative; left: 18%; "><%=configuracion.getParameter("ETIQUETA_TITULO_PESTANHA_CONCILIACION_HISTORIAL_SUBV_" + idiomaUsuario,nombreModulo)%></label>
        <div id="divGeneral">
            <div id="listaLineaHistorialSubvContenedor"  align="center"></div>
            <div class="botonera">
                <input type="button" id="btnNuevoLineaHistorialSubv" name="btnNuevoLineaHistorialSubv" class="botonGeneral"  value="<%=configuracion.getParameter("ETIQUETA_BOTON_ALTA_" + idiomaUsuario,nombreModulo)%>" onclick="pulsarAltaLineaHistorialSubv();">
                <input type="button" id="btnModificarLineaHistorialSubv" name="btnModificarLineaHistorialSubv" class="botonGeneral" value="<%=configuracion.getParameter("ETIQUETA_BOTON_MODIFICAR_" + idiomaUsuario,nombreModulo)%>" onclick="pulsarModificarLineaHistorialSubv();">
                <input type="button" id="btnEliminarLineaHistorialSubv" name="btnEliminarLineaHistorialSubv"   class="botonGeneral" value="<%=configuracion.getParameter("ETIQUETA_BOTON_ELIMINAR_" + idiomaUsuario,nombreModulo)%>" onclick="pulsarEliminarLineaHistorialSubv();">
            </div>
            <!-- Elementos Html para control desde fichero JS-->
            <input type="hidden" id="listaLineaHistorialSubvExpte" value=""/>
            <script>document.getElementById("listaLineaHistorialSubvExpte").value = JSON.stringify(<%=listaResponseJsonStringHistorial%>, function (key, value) {
                    return value == null ? "" : value;
                });</script>
            <input type="hidden" id="historialSubv_textoColumna1" value="<%=configuracion.getParameter("ETIQUETA_TITULO_HISTORIAL_SUBV_TABLA_COLUMNA1_" + idiomaUsuario,nombreModulo)%>"/>
            <input type="hidden" id="historialSubv_textoColumna2" value="<%=configuracion.getParameter("ETIQUETA_TITULO_HISTORIAL_SUBV_TABLA_COLUMNA2_" + idiomaUsuario,nombreModulo)%>"/>

            <input type="hidden" id="historialSubv_descriptor_buscar" value="<%=descriptor.getDescripcion("buscar")%>"/>
            <input type="hidden" id="historialSubv_descriptor_anterior" value="<%=descriptor.getDescripcion("anterior")%>"/>
            <input type="hidden" id="historialSubv_descriptor_siguiente" value="<%=descriptor.getDescripcion("siguiente")%>"/>
            <input type="hidden" id="historialSubv_descriptor_mosFilasPag" value="<%=descriptor.getDescripcion("mosFilasPag")%>"/>
            <input type="hidden" id="historialSubv_descriptor_msgNoResultBusq" value="<%=descriptor.getDescripcion("msgNoResultBusq")%>"/>
            <input type="hidden" id="historialSubv_descriptor_mosPagDePags" value="<%=descriptor.getDescripcion("mosPagDePags")%>"/>
            <input type="hidden" id="historialSubv_descriptor_noRegDisp" value="<%=descriptor.getDescripcion("noRegDisp")%>"/>
            <input type="hidden" id="historialSubv_descriptor_filtrDeTotal" value="<%=descriptor.getDescripcion("filtrDeTotal")%>"/>
            <input type="hidden" id="historialSubv_descriptor_primero" value="<%=descriptor.getDescripcion("primero")%>"/>
            <input type="hidden" id="historialSubv_descriptor_ultimo" value="<%=descriptor.getDescripcion("ultimo")%>"/>
        </div>
    </div>
    <!-- Seccion para la edicion de Datos -->
    <br /> <br />
    <div id="divGeneralAltaEdicionHistorial" style="display: none">
        <input id="idBDH" type="hidden" value=""/>
        <fieldset>
            <legend style="text-align: center;"><h3 style="color:#004595;"><%=configuracion.getParameter("ETIQUETA_LEGEN_ALTA_EDITAR_HISTORIAL_SUBV_" + idiomaUsuario,nombreModulo)%></h3></legend>
            <div>
                <div class="lineaFormulario">
                    <div class="etiqueta">
                        <%=configuracion.getParameter("ETIQUETA_TITULO_HISTORIAL_SUBV_TABLA_COLUMNA1_" + idiomaUsuario,nombreModulo)%>
                    </div>
                    <div>
                        <input type="text" id="fechaFinInterrupSituacion" class="inputTxtFechaLanbide" value="" placeholder="dd/mm/yyyy"
                               maxlength="10" name="fechaFinInterrupSituacion" size="10"
                               onkeypress = "return permiteSoloFormatoFechas(event);"
                               onfocus="javascript:this.select();"
                               onblur = "validarFormatoFecha(this);"/>
                        <A href="javascript:calClick(event);return false;" onClick="mostrarCalfechaFinInterrupSituacion(event);return false;" onblur="ocultarCalendarioOnBlur(event); return false;" style="text-decoration:none;">
                            <IMG style="border: 0px solid none" height="17" id="calfechaFinInterrupSituacion" name="calfechaFinInterrupSituacion" border="0" src="<%=request.getContextPath()%>/images/calendario/icono.gif" width="25">
                        </A>
                    </div>
                </div>
                <div class="lineaFormulario">
                    <div class="etiqueta">
                        <%=configuracion.getParameter("ETIQUETA_TITULO_HISTORIAL_SUBV_TABLA_COLUMNA2_" + idiomaUsuario,nombreModulo)%>
                    </div>
                    <div>
                        <input type="text" id="fechaProrrReanudSituacion" class="inputTxtFechaLanbide" value="" placeholder="dd/mm/yyyy"
                               maxlength="10" name="fechaProrrReanudSituacion" size="10"
                               onkeypress = "return permiteSoloFormatoFechas(event);"
                               onfocus="javascript:this.select();"
                               onblur = "validarFormatoFecha(this);"/>
                        <A href="javascript:calClick(event);return false;" onClick="mostrarCalfechaProrrReanudSituacion(event);return false;" onblur="ocultarCalendarioOnBlur(event); return false;" style="text-decoration:none;">
                            <IMG style="border: 0px solid none" height="17" id="calfechaProrrReanudSituacion" name="calfechaProrrReanudSituacion" border="0" src="<%=request.getContextPath()%>/images/calendario/icono.gif" width="25">
                        </A>
                    </div>
                </div>
            </div>
            <div class="botonera">
                <input type="button" id="btnAceptarAltaEdicionHistorial" name="btnAceptarAltaEdicionHistorial" class="botonGeneral"  value="<%=configuracion.getParameter("ETIQUETA_BOTON_GUARDAR_" + idiomaUsuario,nombreModulo)%>" onclick="pulsarAceptarAltaEditarDatos('HISTORIALSUBV');">
                <input type="button" id="btnCancelarAltaEdicionHistorial" name="btnCancelarAltaEdicionHistorial" class="botonGeneral" value="<%=configuracion.getParameter("ETIQUETA_BOTON_CANCELAR_" + idiomaUsuario,nombreModulo)%>" onclick="pulsarCancelarAltaEditarDatos('HISTORIALSUBV');">
            </div>
        </fieldset>
    </div>
</div>
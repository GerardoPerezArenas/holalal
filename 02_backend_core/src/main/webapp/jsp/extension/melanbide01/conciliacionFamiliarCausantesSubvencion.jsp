<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/tlds/c.tld" prefix="c" %>

<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="es.altia.flexia.integracion.moduloexterno.lanbide01.vo.Melanbide01DepenPerSut"%>
<%@page import="org.apache.logging.log4j.Logger"%>
<%@page import="org.apache.logging.log4j.LogManager"%>
<%@page import="com.google.gson.Gson" %>
<%@page import="com.google.gson.GsonBuilder" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.plugin.persistence.vo.ValorCampoDesplegableModuloIntegracionVO" %>
<%@page import="es.altia.agora.business.administracion.mantenimiento.TipoDocumentoVO" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.lanbide01.vo.Melanbide01Decreto"%>
<meta http-equiv="X-UA-Compatible" content="IE=Edge" />
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<%
    Logger log = LogManager.getLogger(Melanbide01DepenPerSut.class);
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
    List<Melanbide01DepenPerSut> listaCausanteSubvencionExpte = (ArrayList<Melanbide01DepenPerSut>)request.getAttribute("listaCausanteSubvencionExpte");
    Gson gson = new Gson();
    GsonBuilder gsonB = new GsonBuilder().setDateFormat("dd/MM/yyyy");
    gsonB.serializeNulls();  
    gson=gsonB.create();
    String listaResponseJsonString = gson.toJson(listaCausanteSubvencionExpte);
    
%>

<jsp:useBean id="configuracion" scope="request" class="es.altia.flexia.integracion.moduloexterno.lanbide01.util.ConfigurationParameter"  type="es.altia.flexia.integracion.moduloexterno.lanbide01.util.ConfigurationParameter" />
<jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
<jsp:setProperty name="descriptor"  property="idi_cod" value="<%=idiomaUsuario%>" />
<jsp:setProperty name="descriptor"  property="apl_cod" value="<%=apl%>" />
<link rel="StyleSheet" media="screen" type="text/css" href="<%=request.getContextPath()%><%=css%>"/>
<script type="text/javascript" src="<%=request.getContextPath()%>/scripts/jquery/jquery-ui-1.10.1.min.js"></script>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/jquery-ui-1.10.1.css"/>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/scripts/DataTables/datatables.min.css"/>

<div style="clear: both;"></div>
<div id="bodyDiv">
    <div style="clear: both;">
        <!--<label class="legendAzul" style="text-align: center; position: relative; left: 18%; ">< %=configuracion.getParameter("ETIQUETA_TITULO_PESTANHA_CONCILIACION_CAUSANTES_SUBV_" + idiomaUsuario,nombreModulo)%></label>-->
        <div id="divGeneral">
            <div id="listaCausantesSubvContenedor"  align="center"></div>
            <span><%=configuracion.getParameter("ETIQUETA_CAUSANTES_SUBV_NOTA1_" + idiomaUsuario,nombreModulo)%></span>
            <br />
            <span><%=configuracion.getParameter("ETIQUETA_CAUSANTES_SUBV_NOTA2_" + idiomaUsuario,nombreModulo)%></span>
            <br /><br />
            <div class="botonera">
                <input type="button" id="btnNuevoCausante" name="btnNuevoCausante" class="botonGeneral"  value="<%=configuracion.getParameter("ETIQUETA_BOTON_ALTA_" + idiomaUsuario,nombreModulo)%>" onclick="pulsarAltaCausante();">
                <input type="button" id="btnModificarCausante" name="btnModificarCausante" class="botonGeneral" value="<%=configuracion.getParameter("ETIQUETA_BOTON_MODIFICAR_" + idiomaUsuario,nombreModulo)%>" onclick="pulsarModificarCausante();">
                <input type="button" id="btnEliminarCausante" name="btnEliminarCausante"   class="botonGeneral" value="<%=configuracion.getParameter("ETIQUETA_BOTON_ELIMINAR_" + idiomaUsuario,nombreModulo)%>" onclick="pulsarEliminarCausante();">
            </div>
            <!-- Elementos Html para control desde fichero JS-->
            <input type="hidden" id="listaCausanteSubvencionExpte" value=""/>
            <input type="hidden" id="numeroExpediente" value="<%=request.getParameter("numero")%>"/>
            <script>document.getElementById("listaCausanteSubvencionExpte").value=JSON.stringify(<%=listaResponseJsonString%>,function(key, value) { return value == null ? "" : value });</script>
            <input type="hidden" id="causanteSubv_textoColumna1" value="<%=configuracion.getParameter("ETIQUETA_TITULO_CAUSANTES_SUBV_TABLA_COLUMNA1_" + idiomaUsuario,nombreModulo)%>"/>
            <input type="hidden" id="causanteSubv_textoColumna2" value="<%=configuracion.getParameter("ETIQUETA_TITULO_CAUSANTES_SUBV_TABLA_COLUMNA2_" + idiomaUsuario,nombreModulo)%>"/>
            <input type="hidden" id="causanteSubv_textoColumna3" value="<%=configuracion.getParameter("ETIQUETA_TITULO_CAUSANTES_SUBV_TABLA_COLUMNA3_" + idiomaUsuario,nombreModulo)%>"/>
            <input type="hidden" id="causanteSubv_textoColumna4" value="<%=configuracion.getParameter("ETIQUETA_TITULO_CAUSANTES_SUBV_TABLA_COLUMNA4_" + idiomaUsuario,nombreModulo)%>"/>
            <input type="hidden" id="causanteSubv_textoColumna5" value="<%=configuracion.getParameter("ETIQUETA_TITULO_CAUSANTES_SUBV_TABLA_COLUMNA5_" + idiomaUsuario,nombreModulo)%>"/>
            <input type="hidden" id="causanteSubv_textoColumna6" value="<%=configuracion.getParameter("ETIQUETA_TITULO_CAUSANTES_SUBV_TABLA_COLUMNA6_" + idiomaUsuario,nombreModulo)%>"/>
            <input type="hidden" id="causanteSubv_textoColumna7" value="<%=configuracion.getParameter("ETIQUETA_TITULO_CAUSANTES_SUBV_TABLA_COLUMNA7_" + idiomaUsuario,nombreModulo)%>"/>
            <input type="hidden" id="causanteSubv_textoColumna8" value="<%=configuracion.getParameter("ETIQUETA_TITULO_CAUSANTES_SUBV_TABLA_COLUMNA8_" + idiomaUsuario,nombreModulo)%>"/>
            <input type="hidden" id="causanteSubv_textoColumna9" value="<%=configuracion.getParameter("ETIQUETA_TITULO_CAUSANTES_SUBV_TABLA_COLUMNA9_" + idiomaUsuario,nombreModulo)%>"/>
            <input type="hidden" id="causanteSubv_textoColumna10" value="<%=configuracion.getParameter("ETIQUETA_TITULO_CAUSANTES_SUBV_TABLA_COLUMNA10_" + idiomaUsuario,nombreModulo)%>"/>

            <input type="hidden" id="causanteSubv_descriptor_buscar" value="<%=descriptor.getDescripcion("buscar")%>"/>
            <input type="hidden" id="causanteSubv_descriptor_anterior" value="<%=descriptor.getDescripcion("anterior")%>"/>
            <input type="hidden" id="causanteSubv_descriptor_siguiente" value="<%=descriptor.getDescripcion("siguiente")%>"/>
            <input type="hidden" id="causanteSubv_descriptor_mosFilasPag" value="<%=descriptor.getDescripcion("mosFilasPag")%>"/>
            <input type="hidden" id="causanteSubv_descriptor_msgNoResultBusq" value="<%=descriptor.getDescripcion("msgNoResultBusq")%>"/>
            <input type="hidden" id="causanteSubv_descriptor_mosPagDePags" value="<%=descriptor.getDescripcion("mosPagDePags")%>"/>
            <input type="hidden" id="causanteSubv_descriptor_noRegDisp" value="<%=descriptor.getDescripcion("noRegDisp")%>"/>
            <input type="hidden" id="causanteSubv_descriptor_filtrDeTotal" value="<%=descriptor.getDescripcion("filtrDeTotal")%>"/>
            <input type="hidden" id="causanteSubv_descriptor_primero" value="<%=descriptor.getDescripcion("primero")%>"/>
            <input type="hidden" id="causanteSubv_descriptor_ultimo" value="<%=descriptor.getDescripcion("ultimo")%>"/>
        </div>
        <!-- Seccion para la edicion de Datos -->
        <br /> <br />
        <div id="divGeneralAltaEdicionCausante" style="display: none">
            <input id="idBD" type="hidden" value=""/>
            <fieldset>
                <legend style="text-align: center;"><h3 style="color:#004595;"><%=configuracion.getParameter("ETIQUETA_LEGEN_ALTA_EDITAR_CAUSANTES_SUBV_" + idiomaUsuario,nombreModulo)%></h3></legend>
                <div class="lineaFormulario">
                    <div class="etiqueta">
                        <%=configuracion.getParameter("ETIQUETA_TITULO_CAUSANTES_SUBV_TABLA_COLUMNA1_" + idiomaUsuario,nombreModulo)%>
                    </div>
                    <div>
                        <input id="correlativo" name="correlativo" type="text" class="inputTexto" size="15" maxlength="10" value=""/>
                    </div>
                </div>
                <div>
                    <div class="lineaFormulario">
                        <div class="etiqueta">
                            <%=configuracion.getParameter("ETIQUETA_TITULO_CAUSANTES_SUBV_TABLA_COLUMNA2_" + idiomaUsuario,nombreModulo)%>
                        </div>
                        <div>
                            <select name="tipoDependiente" id="tipoDependiente">
                                <option value=""><%=configuracion.getParameter("ETIQUETA_TEXTO_DEFECTO_DESPLEGABLE_" + idiomaUsuario,nombreModulo)%></option>
                                <c:forEach items="${listaTipoDependiente}" var="tipoDependiente" varStatus="contador">
                                    <option value="<c:out value="${tipoDependiente.codigo}"/>" title="<c:out value="${tipoDependiente.descripcion}"/>"><c:out value="${tipoDependiente.descripcion}"/></option>
                                </c:forEach>
                            </select>
                        </div>
                    </div>
                    <div class="lineaFormulario">
                        <div class="etiqueta">
                            <%=configuracion.getParameter("ETIQUETA_TITULO_CAUSANTES_SUBV_TABLA_COLUMNA3_" + idiomaUsuario,nombreModulo)%>
                        </div>
                        <div>
                            <input id="parentezco" name="parentezco" type="text" class="inputTexto" size="75" maxlength="500" value=""/>
                        </div>
                    </div>
                </div>
                <div>
                    <div class="lineaFormulario">
                        <div class="etiqueta">
                            <%=configuracion.getParameter("ETIQUETA_TITULO_CAUSANTES_SUBV_TABLA_COLUMNA4_" + idiomaUsuario,nombreModulo)%>
                        </div>
                        <div>
                            <input id="nombre" name="nombre" type="text" class="inputTexto" maxlength="500" value="" size="75"/>
                        </div>
                    </div>
                    <div class="lineaFormulario">
                        <div class="etiqueta">
                            <%=configuracion.getParameter("ETIQUETA_TITULO_CAUSANTES_SUBV_TABLA_COLUMNA5_" + idiomaUsuario,nombreModulo)%>
                        </div>
                        <div>
                            <input id="apellidos" name="apellidos" type="text" class="inputTexto" maxlength="500" value="" size="75"/>
                        </div>
                    </div>
                </div>
                <div>
                    <div class="lineaFormulario">
                        <div class="etiqueta">
                            <%=configuracion.getParameter("ETIQUETA_TITULO_CAUSANTES_SUBV_TABLA_COLUMNA6_" + idiomaUsuario,nombreModulo)%>
                        </div>
                        <div>
                            <select name="tipoDocumento" id="tipoDocumento" class="inputTexto">
                                <option value=""><%=configuracion.getParameter("ETIQUETA_TEXTO_DEFECTO_DESPLEGABLE_" + idiomaUsuario,nombreModulo)%></option>
                                <c:forEach items="${listaTipoDocumento}" var="tipoDocumento" varStatus="contador">
                                    <option value="<c:out value="${tipoDocumento.codigo}"/>" title="<c:out value="${tipoDocumento.descripcion}"/>"><c:out value="${tipoDocumento.descripcion}"/></option>
                                </c:forEach>
                            </select>
                        </div>
                    </div>  
                    <div class="lineaFormulario">
                        <div class="etiqueta">
                            <%=configuracion.getParameter("ETIQUETA_TITULO_CAUSANTES_SUBV_TABLA_COLUMNA7_" + idiomaUsuario,nombreModulo)%>
                        </div>
                        <div>
                            <input id="numeroDocumento" name="numeroDocumento" type="text" class="inputTexto" size="30" maxlength="20" value=""/>
                        </div>
                    </div>
                </div>
                <div class="lineaFormulario">
                    <div class="etiqueta">
                        <%=configuracion.getParameter("ETIQUETA_TITULO_CAUSANTES_SUBV_TABLA_COLUMNA8_" + idiomaUsuario,nombreModulo)%>
                    </div>
                    <div>
                        <input type="text" id="fechaNacimientoPD" class="inputTxtFechaLanbide" value="" placeholder="dd/mm/yyyy"
                               maxlength="10" name="fechaNacimientoPD" size="10"
                               onkeypress = "return permiteSoloFormatoFechas(event);"
                               onfocus="javascript:this.select();"
                               onblur = "validarFormatoFecha(this);"/>
                        <A href="javascript:calClick(event);return false;" onClick="mostrarCalfechaNacimientoPD(event);return false;" onblur="ocultarCalendarioOnBlur(event); return false;" style="text-decoration:none;">
                            <IMG style="border: 0px solid" height="17" id="calfechaNacimientoPD" name="calfechaNacimientoPD" border="0" src="<%=request.getContextPath()%>/images/calendario/icono.gif" width="25">
                        </A>
                    </div>
                </div>
                <div>
                    <div class="lineaFormulario">
                        <div class="etiqueta">
                            <%=configuracion.getParameter("ETIQUETA_TITULO_CAUSANTES_SUBV_TABLA_COLUMNA9_" + idiomaUsuario,nombreModulo)%>
                        </div>
                        <div>
                            <select name="esMinusvalido" id="esMinusvalido">
                                <option value=""><%=configuracion.getParameter("ETIQUETA_TEXTO_DEFECTO_DESPLEGABLE_" + idiomaUsuario,nombreModulo)%></option>
                                <c:forEach items="${listaSINO}" var="esMinusvalido" varStatus="contador">
                                    <option value="<c:out value="${esMinusvalido.codigo}"/>" title="<c:out value="${esMinusvalido.descripcion}"/>"><c:out value="${esMinusvalido.descripcion}"/></option>
                                </c:forEach>
                            </select>
                        </div>
                    </div>
                    <div class="lineaFormulario">
                        <div class="etiqueta">
                            <%=configuracion.getParameter("ETIQUETA_TITULO_CAUSANTES_SUBV_TABLA_COLUMNA10_" + idiomaUsuario,nombreModulo)%>
                        </div>
                        <div>
                            <input id="porcentajeMinusvalia" name="porcentajeMinusvalia" type="text" class="inputTexto" size="15" maxlength="5" value=""
                                   onkeypress="return permiteSoloNumeroY2Decimales(event);" 
                                   onfocusout="formatInputAsNumero(this);"/>
                        </div>
                    </div>
                </div>
                <div class="botonera">
                    <input type="button" id="btnAceptarAltaEdicionCausante" name="btnAceptarAltaEdicion" class="botonGeneral"  value="<%=configuracion.getParameter("ETIQUETA_BOTON_GUARDAR_" + idiomaUsuario,nombreModulo)%>" onclick="pulsarAceptarAltaEditarDatos('CAUSANTESSUBV');">
                    <input type="button" id="btnCancelarAltaEdicionCausante" name="btnCancelarAltaEdicion" class="botonGeneral" value="<%=configuracion.getParameter("ETIQUETA_BOTON_CANCELAR_" + idiomaUsuario,nombreModulo)%>" onclick="pulsarCancelarAltaEditarDatos('CAUSANTESSUBV');">
                </div>
            </fieldset>
        </div>
    </div>
</div>


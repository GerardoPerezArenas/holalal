<%-- 
    Document   : experienciaAcreditableCol1
    Created on : 28-Jun-2021, 11:11:02
    Author     : INGDGC
--%>

<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>

<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide48.i18n.MeLanbide48I18n" %>
<%@page import="es.altia.agora.business.escritorio.UsuarioValueObject" %>
<%@page import="java.util.ArrayList" %>
<%@page import="java.util.List" %>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide48.vo.SelectItem"%>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide48.vo.ColecTrayectoriaEntidad"%>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide48.util.ConstantesMeLanbide48"%>
<%@page import="es.altia.flexia.integracion.moduloexterno.melanbide48.util.MeLanbide48Utils"%>
<%@page import="com.google.gson.Gson" %>
<%@page import="com.google.gson.GsonBuilder" %>


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
    String nombreModulo     = request.getParameter("nombreModulo");
    String numExpediente    = request.getParameter("numero");
    Integer ejercicio    = MeLanbide48Utils.getEjercicioDeExpediente(numExpediente);
    String codProcedimiento    = MeLanbide48Utils.getCodProcedimientoDeExpediente(numExpediente);
    String colecTrayectoriaEntidadVOJSON =  (String)request.getAttribute("colecTrayectoriaEntidadVOJSON");

%>

<jsp:useBean id="descriptor" scope="request" class="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean"  type="es.altia.agora.interfaces.user.web.util.TraductorAplicacionBean" />
<jsp:setProperty name="descriptor"  property="idi_cod" value="<%=idiomaUsuario%>" />
<jsp:setProperty name="descriptor"  property="apl_cod" value="<%=apl%>" />

<div>
    <input type="hidden" id="colectivoTE" name="colectivoTE" value="4"/>
    <!-- Literales paracabecera de tabla-->
    <!-- Seccion Pagina JSP Trayectotia -->
    <div class="shadow p-3 mb-5 bg-light rounded etiqueta">
        <p style="text-align: center; border: solid;"><%=meLanbide48I18n.getMensaje(idiomaUsuario,"label.experiencia.acreditable.articulo.13.3.2")%></p>
        <p><%=meLanbide48I18n.getMensaje(idiomaUsuario,"label.experiencia.acreditable.articulo.13.3.2.b")%></p>
    </div>
    <div id="divExperienciaAcreditada4">
        <div class="shadow-none bg-light rounded etiqueta">
            <hr>
            <p><%=meLanbide48I18n.getMensaje(idiomaUsuario,"label.experiencia.acreditable.programas.convocatorias.ordenes.decretos")%></p>
            <hr>
        </div>
        <div id="divColecOrdenesDecretosC4">
            <div style="clear: both;">
                <div id="listaColecOrdenesDecretosC4" name="listaColecOrdenesDecretosC4"></div>
                <div class="botonera">
                    <input type="button" id="botonAnadirColecOrdenesDecretosC4" name="botonAnadirColecOrdenesDecretosC4" class="botonGeneral" value="<%=meLanbide48I18n.getMensaje(idiomaUsuario,"btn.anadir")%>" onclick="anadirEditarColecTrayectoriaEntidad(4,1,null);"/>
                    <input type="button" id="botonModificarColecOrdenesDecretosC4" name="botonModificarColecOrdenesDecretosC4" class="botonGeneral" value="<%=meLanbide48I18n.getMensaje(idiomaUsuario,"btn.modificar")%>" onclick="modificarColecTrayectoriaEntidad(4,1);"/>
                    <input type="button" id="botonEliminarColecOrdenesDecretosC4" name="botonEliminarColecOrdenesDecretosC4" class="botonGeneral" value="<%=meLanbide48I18n.getMensaje(idiomaUsuario,"btn.eliminar")%>" onclick="eliminarColecTrayectoriaEntidad(4,1);"/>
                </div>
            </div>
        </div>
        <div class="shadow-none bg-light rounded etiqueta">
            <hr>
            <p><%=meLanbide48I18n.getMensaje(idiomaUsuario,"label.experiencia.acreditable.convocatorias.lanbide")%></p>
            <hr>
        </div>
        <div id="divColecConvocatoriasC4">
            <div style="clear: both;">
                <div id="listaColecConvocatoriasC4" name="listaColecConvocatoriasC1"></div>
                <div class="botonera">
                    <input type="button" id="botonGuardarColecConvocatoriasC4" name="botonGuardarColecConvocatoriasC4" class="botonGeneral" value="<%=meLanbide48I18n.getMensaje(idiomaUsuario,"btn.guardar")%>" onclick="guardarDatosColecConvocatoriasPredefColectivo(4);"/>
                </div>
            </div>
        </div>
        <div class="shadow-none bg-light rounded etiqueta">
            <hr>
            <p><%=meLanbide48I18n.getMensaje(idiomaUsuario,"label.experiencia.acreditable.otros.programas")%></p>
            <hr>
        </div>
        <div id="divColecOtrosProgramasC4">
            <div style="clear: both;">
                <div id="listaColecOtrosProgramasC4" name="listaColecOtrosProgramasC4"></div>
                <div class="botonera">
                    <input type="button" id="botonAnadirColecOtrosProgramasC4" name="botonAnadirColecOtrosProgramasC4" class="botonGeneral" value="<%=meLanbide48I18n.getMensaje(idiomaUsuario,"btn.anadir")%>" onclick="anadirEditarColecTrayectoriaEntidad(4,3,null);"/>
                    <input type="button" id="botonModificarColecOtrosProgramasC4" name="botonModificarColecOtrosProgramasC4" class="botonGeneral" value="<%=meLanbide48I18n.getMensaje(idiomaUsuario,"btn.modificar")%>" onclick="modificarColecTrayectoriaEntidad(4,3);"/>
                    <input type="button" id="botonEliminarColecOtrosProgramasC4" name="botonEliminarColecOtrosProgramasC4" class="botonGeneral" value="<%=meLanbide48I18n.getMensaje(idiomaUsuario,"btn.eliminar")%>" onclick="eliminarColecTrayectoriaEntidad(4,3);"/>
                </div>
            </div>
        </div>
        <div class="shadow-none bg-light rounded etiqueta">
            <hr>
            <p><%=meLanbide48I18n.getMensaje(idiomaUsuario,"label.experiencia.acreditable.actividades.orientacion.profesional")%></p>
            <hr>
        </div>
        <div id="divColecActividadesOProC4">
            <div style="clear: both;">
                <div id="listaColecActividadesOProC4" name="listaColecActividadesOProC4"></div>
                <div class="botonera">
                    <input type="button" id="botonAnadirColecColecActividadesOProC4" name="botonAnadirColecColecActividadesOProC4" class="botonGeneral" value="<%=meLanbide48I18n.getMensaje(idiomaUsuario,"btn.anadir")%>" onclick="anadirEditarColecTrayectoriaEntidad(4,4,null);"/>
                    <input type="button" id="botonModificarColecActividadesOProC4" name="botonModificarColecActividadesOProC4" class="botonGeneral" value="<%=meLanbide48I18n.getMensaje(idiomaUsuario,"btn.modificar")%>" onclick="modificarColecTrayectoriaEntidad(4,4);"/>
                    <input type="button" id="botonEliminarColecActividadesOProC4" name="botonEliminarColecActividadesOProC4" class="botonGeneral" value="<%=meLanbide48I18n.getMensaje(idiomaUsuario,"btn.eliminar")%>" onclick="eliminarColecTrayectoriaEntidad(4,4);"/>
                </div>
            </div>
        </div>
        <div class="shadow-none bg-light rounded etiqueta">
            <hr>
            <p><%=meLanbide48I18n.getMensaje(idiomaUsuario,"label.experiencia.acreditable.otros.programas.ejecucion")%></p>
            <hr>
        </div>
        <div id="divColecEjecucionProgramasEspC4">
            <div style="clear: both;">
                <div id="listaColecEjecucionProgramasEspC4" name="listaColecEjecucionProgramasEspC4"></div>
                <div class="botonera">
                    <input type="button" id="botonAnadirColecEjecucionProgramasEspC4" name="botonAnadirColecEjecucionProgramasEspC4" class="botonGeneral" value="<%=meLanbide48I18n.getMensaje(idiomaUsuario,"btn.anadir")%>" onclick="anadirEditarColecTrayectoriaEntidad(4,6,null);"/>
                    <input type="button" id="botonModificarColecEjecucionProgramasEspC4" name="botonModificarColecEjecucionProgramasEspC4" class="botonGeneral" value="<%=meLanbide48I18n.getMensaje(idiomaUsuario,"btn.modificar")%>" onclick="modificarColecTrayectoriaEntidad(4,6);"/>
                    <input type="button" id="botonEliminarColecEjecucionProgramasEspC4" name="botonEliminarColecEjecucionProgramasEspC4" class="botonGeneral" value="<%=meLanbide48I18n.getMensaje(idiomaUsuario,"btn.eliminar")%>" onclick="eliminarColecTrayectoriaEntidad(4,6);"/>
                </div>
            </div>
        </div>
    </div>
</div>
